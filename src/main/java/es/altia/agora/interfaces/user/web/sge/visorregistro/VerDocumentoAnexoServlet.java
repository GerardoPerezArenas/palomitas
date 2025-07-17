package es.altia.agora.interfaces.user.web.sge.visorregistro;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.OrganizacionesDAO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.exception.gestionmensaje.GestionMensajeErrorDokusi;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Permite visualizar un documento anexado a una anotación procedente del buzón de entrada
 * @author oscar.rodriguez
 */
public class VerDocumentoAnexoServlet extends HttpServlet{

   protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");
   protected static String DOT = ".";
   //protected static Log log =
    //        LogFactory.getLog(VerDocumentoServlet.class.getName());
    protected static Logger log = Logger.getLogger(VerDocumentoAnexoServlet.class);

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }
 
  
  public void defaultAction(HttpServletRequest req,HttpServletResponse res) throws Exception {

      // Se recupera el ejercicio, número de anotación y código del documento a visualizar
      String ejercicio   = req.getParameter("ejercicio");
      String numero      = req.getParameter("numero");
      String codigo      = req.getParameter("codigo");
      String uor         = req.getParameter("uor");     // Unidad tramitadora de destino
      String tipoReg     = req.getParameter("tipoReg"); // Tipo registro de origen: REC,SGE,...
      String pnombreFichero   = req.getParameter("nombreFichero");
      String tipoMime         = req.getParameter("extensionFichero");
      String fechaFichero     = req.getParameter("fechaFichero");
      String tipoRegistro     = req.getParameter("tipoRegistro");
      String codUorRegistro   = req.getParameter("codUorRegistro");
      String codDepartamento  = req.getParameter("codDepartamento");      
      String embedded = req.getParameter("embedded"); // indica si el documento se muestra en el visor o se descargar
     
      log.debug("defaultAction");
      log.debug("ejercicio: " + ejercicio);
      log.debug("numero: " + numero);
      log.debug("codigo: " + codigo);
      log.debug("uor: " + uor);
      log.debug("tipoRegOrigen: " + tipoReg);
      log.debug("nombreFichero: " + pnombreFichero);
      log.debug("tipoMime: " + tipoMime);
      log.debug("fechaFichero: " + fechaFichero);
      log.debug("tipoRegistro: " + tipoRegistro); 
      log.debug("codUorRegistro: " + codUorRegistro); 
      log.debug("embedded: "+embedded);

      RegistroValueObject reg = new RegistroValueObject();
       // Se recuperan los datos del usuario de la sesión
      HttpSession session = req.getSession();
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
      String[] params = usuario.getParamsCon();
      
      AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassRegistro(Integer.toString(usuario.getOrgCod()));
      Hashtable<String,Object> datos = new Hashtable<String,Object>();

      int codOrganizacion = usuario.getOrgCod();            
      datos.put("anoReg",new Integer(ejercicio));
      datos.put("numReg",new Long(numero));
      datos.put("tipoReg","E");
      datos.put("nombreDocumento",pnombreFichero);            
      datos.put("documentoRegistro",new Boolean(true));
      datos.put("params",params);
      datos.put("tipoMime",tipoMime);
      datos.put("identDepart",new Integer(codDepartamento));

      int tipoDocumento = -1;
      String extension = MimeTypes.guessExtensionFromMimeType(tipoMime);
      
      if(!almacen.isPluginGestor()){
        tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;        
      }      
      else{
          tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;

          datos.put("codMunicipio",Integer.toString(codOrganizacion));          
          datos.put("extension", extension);
                    
          if(almacen.isPluginGestor()){
              //  Si se trata de un plugin de un gestor documental, se pasa la información
              // extra necesaria                                    
              ResourceBundle config = ResourceBundle.getBundle("documentos");              
              String carpetaRaiz  = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
              
              Connection con = null;
              String descripcionOrganizacion = null;
              String descripcionUnidadRegistro = null;
              AdaptadorSQLBD adapt = null;
              try{
                  adapt = new AdaptadorSQLBD(params);
                  con = adapt.getConnection();

                  descripcionOrganizacion   = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(codOrganizacion, con);
                  UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],codUorRegistro);
                  if (uorDTO!=null)
                      descripcionUnidadRegistro = uorDTO.getUor_nom();
              }catch(BDException e){
                  log.error("Error al recuperar una conexión a la BBDD: " + e.getMessage());
              }finally{
                  try{
                      adapt.devolverConexion(con);
                  }catch(BDException e){
                      log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
                  }
              }

              ArrayList<String> listaCarpetas = new ArrayList<String>();
              listaCarpetas.add(carpetaRaiz);
              listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
              listaCarpetas.add(codUorRegistro + ConstantesDatos.GUION + descripcionUnidadRegistro);
              listaCarpetas.add(ConstantesDatos.DESCRIPCION_ENTRADAS_REGISTRO);
              listaCarpetas.add(ejercicio + ConstantesDatos.GUION + numero);
              datos.put("listaCarpetas",listaCarpetas);
          }                       
       }

        try{
            Documento documento = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);            
            documento = almacen.getDocumentoRegistroConsulta(documento,tipoReg);
            
            byte[] fichero = null;
            String nombreFichero = null;
            String contentDisposition = null;
            if(documento!=null && documento.getFichero()!=null && documento.getFichero().length>0){
                fichero = documento.getFichero();
				nombreFichero = documento.getNombreDocumento();
                if (StringUtils.isNotEmpty(extension)) {
                     nombreFichero = nombreFichero + "." + extension;
                }
                                
                if(embedded.equalsIgnoreCase("true")){
                   contentDisposition = "inline";
                } else {
                    contentDisposition = "attachment";
                }
                
                res.setContentType(tipoMime);

                res.setHeader("Content-Disposition", contentDisposition+"; filename=" + nombreFichero);
                ServletOutputStream out = res.getOutputStream();
                res.setContentLength(fichero.length);
                BufferedOutputStream bos = new BufferedOutputStream(out);
                bos.write(fichero, 0, fichero.length);
                bos.close();                   
            }else
                log.error("No se ha podido recuperar el archivo y por tanto no se puede descargar");
            
           
            

        }catch(AlmacenDocumentoTramitacionException e){
			e.printStackTrace();
            log.error("Error AlmacenDocumentoTramitacionException al visualizar Documento en " + this.getClass().getName(), e);
            try {
                log.error(this.getClass() + " - Preparamos el js para mostrar mensaje error Dokusi en Servlet");
                //ResourceBundle configDocumentosDokusi = ResourceBundle.getBundle("MELANBIDE_DOKUSI");
                UsuarioValueObject usu = (UsuarioValueObject) session.getAttribute("usuario");
                Integer idioma = 1;
                if (usu != null) {
                    idioma = usu.getIdioma();
                }
                GestionMensajeErrorDokusi gestionMensajeErrorDokusi = new GestionMensajeErrorDokusi();
                String textoMensaje = gestionMensajeErrorDokusi.preparaTextoHtmlContenidoMensaje(e, idioma);
                PrintWriter out = res.getWriter();
                out.println(textoMensaje);
            }catch (Exception e1) {
                log.error(this.getClass() + " - Error al mostrar mensaje error Dokusi en Servlet - " + e1.getMessage());
            }
        }catch(Exception e){
			e.printStackTrace();
            log.error("Error Exception al visualizar Documento en " + this.getClass().getName(), e);
        }
      
  
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
      try     {

          
          defaultAction(req, res);
    } catch (Exception e) {
          log.error("ERROR doGet:" + e);
    }
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
    try {
        defaultAction(req, res);
    } catch (Exception e) {
        log.error("ERROR doGet:" + e);
    }
  }

   /**
   * Devuelve la extension de fichero correspondiente al tipo MIME pasado.
   */
  private String getExtension(String tipoMIME) {
      if (tipoMIME.equals("application/msword")) return ".doc";
      if (tipoMIME.equals("application/pdf"))    return ".pdf";
      if (tipoMIME.equals("image/jpeg")) return ".jpg";
      if (tipoMIME.equals("image/pjpeg")) return ".jpg";
      if (tipoMIME.equals("image/gif")) return ".gif";
      if (tipoMIME.equals("image/png")) return ".png";
      return "";
  }

}