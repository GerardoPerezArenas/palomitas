package es.altia.flexia.interfaces.user.web.documentospresentados.action;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.business.documentospresentados.persistence.ExpedienteDocPresentadoManager;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.exception.gestionmensaje.GestionMensajeErrorDokusi;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *  Servlet encargado de recuperar el adjunto asociado a un documento de expediente y de mostrarlo al usuario
 * @author oscar.rodriguez
 */
public class VerDocumentoPresentadoServlet extends HttpServlet{
    private Logger log = Logger.getLogger(VerDocumentoPresentadoServlet.class);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void defaultAction(HttpServletRequest request,HttpServletResponse response) throws Exception {

      log.debug("EJECUTANDO SERVLET: VerDocumentoPresentadoServlet");

      HttpSession session = request.getSession();
      if (session == null) {
          log.error("SESION NULA EN VerDocumentoPresentadoServlet.defaultAction");
          return;
      }

     String codMunicipio          = request.getParameter("codMunicipio");
     String ejercicio                = request.getParameter("ejercicio");
     String numeroExpediente = request.getParameter("numExpediente");
     String codDocumento       = request.getParameter("codigoDocumento");
     String codProcedimiento   = request.getParameter("codProcedimiento");
     
     AlmacenDocumentoTramitacionException exceptionDokusiGestionMensajeError = null;

     UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
     String[] params = usuario.getParamsCon();

     Hashtable<String,Object> datos = new Hashtable<String,Object>();
     datos.put("codDocumento",codDocumento);
     datos.put("codMunicipio",codMunicipio);
     datos.put("ejercicio",ejercicio);
     datos.put("numeroExpediente",numeroExpediente);
     datos.put("params",params);
     datos.put("codProcedimiento",codProcedimiento);

     AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);
     Documento doc = null;
     int tipoDocumento = -1;
     if(!almacen.isPluginGestor())
           tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
      else{
         ResourceBundle bundle = ResourceBundle.getBundle("documentos");
         /**
        String tipoPlugin = bundle.getString(ConstantesDatos.PROPIEDAD_PLUGIN_ALMACENAMIENTO);
        String nombreGestor   = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + tipoPlugin + ConstantesDatos.SUFIJO_PLUGIN_NOMBRE_GESTOR);
        String carpetaRaiz       = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + nombreGestor + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
        */
        String carpetaRaiz   = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ); 
         
        String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);        
        datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
        datos.put("origen",almacen.getNombreServicio());

        TraductorAplicacionBean traductor = new TraductorAplicacionBean();
        traductor.setApl_cod(4);
        traductor.setIdi_cod(usuario.getIdioma());

        /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL **/
        ArrayList<String> listaCarpetas = new ArrayList<String>();
        listaCarpetas.add(carpetaRaiz);
        listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + usuario.getOrg());
        listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
        listaCarpetas.add(numeroExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));
        listaCarpetas.add(traductor.getDescripcion("carpetaDocumentosPresentados"));
        datos.put("listaCarpetas",listaCarpetas);
        tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;

     }

     byte[] fichero = null;
     String nombreFichero = null;
     String tipoContenido  = null;
     
     try{
            FichaExpedienteForm fichaForm = (FichaExpedienteForm)session.getAttribute("FichaExpedienteForm");
            doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);            
            doc.setExpHistorico(fichaForm.isExpHistorico());
            
            if(almacen.isPluginGestor()){
                // Se recupera la información del documento de la base de datos para extraer la extensión del fichero y componer el nombre del fichero
                // para poder obtener su contenido del gestor                                
                doc = ExpedienteDocPresentadoManager.getInstance().getNombreDocumentoPresentado(doc);
                
                log.debug(" ********* nombredocumento: " + doc.getNombreDocumento());
                log.debug(" ********* extensiondocumento: " + doc.getExtension());
                DocumentoGestor docGestor = (DocumentoGestor)doc;
                docGestor.setNombreFicheroCompleto(codDocumento + ConstantesDatos.GUION + doc.getNombreDocumento() + ConstantesDatos.DOT + doc.getExtension());
                doc = almacen.getDocumentoPresentado(docGestor);
            }else
                doc = almacen.getDocumentoPresentado(doc);

            fichero       = doc.getFichero();
            nombreFichero = doc.getNombreDocumento() + "." + doc.getExtension();
            tipoContenido = doc.getTipoMimeContenido();

            log.debug(" >>>>>>>>>>>>>>>>>>>>>>  fichero longitud: " + fichero.length);
            log.debug(" >>>>>>>>>>>>>>>>>>>>>>  fichero nombreFichero: " + nombreFichero);
            log.debug(" >>>>>>>>>>>>>>>>>>>>>>  fichero tipoContenido: " + tipoContenido);

      }catch(AlmacenDocumentoTramitacionException e){
		  e.printStackTrace();
          log.error("Error al recuperar documento : ", e);
          log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
          exceptionDokusiGestionMensajeError = e;
          fichero             = null;
          nombreFichero = null;
          tipoContenido   = null;
      }

      if(fichero != null)
      {
            BufferedOutputStream bos = null;
            try{

                response.setContentType(tipoContenido);
                response.setHeader("Content-Disposition", "inline; filename=" + nombreFichero);
                response.setHeader("Content-Transfer-Encoding", "binary");

                ServletOutputStream out = response.getOutputStream();
                response.setContentLength(fichero.length);
                bos = new BufferedOutputStream(out);
                bos.write(fichero, 0, fichero.length);
                bos.flush();
                bos.close();


            }catch(Exception e){
                if(log.isDebugEnabled()) log.debug("Excepcion en catch de VerDocumentoPresentadoServlet.defaultAction()");
                throw e;
            }finally {
                if(bos != null) bos.close();
            }
      } else {
          log.warn("FICHERO NULO EN VerDocumentoPresentadoServlet.defaultAction");
          try {
              log.error(this.getClass() + " - Preparamos el js para mostrar mensaje error Dokusi en Servelet");
              //ResourceBundle configDocumentosDokusi = ResourceBundle.getBundle("MELANBIDE_DOKUSI");
              UsuarioValueObject usu = (UsuarioValueObject) session.getAttribute("usuario");
              Integer idioma = 1;
              if (usu != null) {
                  idioma = usu.getIdioma();
              }
              GestionMensajeErrorDokusi gestionMensajeErrorDokusi = new GestionMensajeErrorDokusi();
              String textoMensaje = gestionMensajeErrorDokusi.preparaTextoHtmlContenidoMensaje(exceptionDokusiGestionMensajeError, idioma);
              PrintWriter out = response.getWriter();
              out.println(textoMensaje);
          } catch (Exception e) {
              log.error(this.getClass() + " - Error al mostrar mensaje error Dokusi en Servelet - " + e.getMessage());
          }
      }
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
      try{

          log.info("Entrando en el servlet VerDocumentosServlet");
          defaultAction(req, res);
    } catch (Exception e) {
        log.error("ERROR doGet:" + e);
        e.printStackTrace();
    }
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
    try {
        defaultAction(req, res);
    } catch (Exception e) {
        log.error("ERROR doGet:" + e);
        e.printStackTrace();
    }
  }



}
