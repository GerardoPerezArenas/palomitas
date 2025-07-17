package es.altia.agora.interfaces.user.web.util;

import es.altia.agora.business.administracion.mantenimiento.persistence.OrganizacionesManager;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.persistence.OperacionesExpedienteManager;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.common.service.config.*;
import es.altia.agora.interfaces.user.web.sge.DocumentosRelacionExpedientesForm;
import es.altia.agora.technical.ConstantesDatos;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class GrabarDocRelacionOfficeServlet extends HttpServlet
{
  protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");

  protected static Log log =
          LogFactory.getLog(GrabarDocRelacionOfficeServlet.class.getName());

  public void init(ServletConfig config) throws ServletException
  {
    super.init(config);
  }
  public void defaultAction(HttpServletRequest req,HttpServletResponse res) throws Exception {
      log.debug ("Entrando en el servlet de grabación de documentos.");
      HttpSession session=req.getSession();
      
      Enumeration t=session.getAttributeNames();
      while (t.hasMoreElements()){log.debug ("-"+t.nextElement().toString());}
      
      DocumentosRelacionExpedientesForm dref = (DocumentosRelacionExpedientesForm) session.getAttribute("DocumentosRelacionExpedientesForm");
      String codMunicipio = dref.getCodMunicipio();
      String codProcedimiento = dref.getCodProcedimiento();
      String ejercicio = dref.getEjercicio();
      String codDocumento = dref.getCodDocumento();
      String ocurrenciaTramite = dref.getOcurrenciaTramite();
      String codTramite = dref.getCodTramite();
      String numDocumento = dref.getNumeroDocumento();
      String numRelacion = dref.getNumeroRelacion();
      String opcionGrabar = dref.getOpcionGrabar();
      String tipoPlantilla =dref.getTipoPlantilla();
      
      
      log.debug (opcionGrabar);
      log.debug (tipoPlantilla);
      
      log.debug (numDocumento+"-----------");
            
      UsuarioValueObject uVO = (UsuarioValueObject) session.getAttribute("usuario");
      String codUsuario = String.valueOf(uVO.getIdUsuario());

        String[] params = new String[7];
        params = uVO.getParamsCon();

        log.debug (numDocumento);
        
        String contentType = req.getContentType();
        log.debug ("contentType: "+contentType);
            
        ObjectInputStream os = new ObjectInputStream(req.getInputStream());
        Vector d = (Vector) os.readObject();
        String titulo = new String ((byte[])d.get(0));
        byte[] dataBytes = (byte[]) d.get(1);
        log.debug (titulo);
        log.debug (dataBytes.length);
        
        Hashtable<String,Object> datos = new Hashtable<String,Object>();
        datos.put("codMunicipio",codMunicipio);
        datos.put("codProcedimiento",codProcedimiento);
        datos.put("ejercicio",ejercicio);
        datos.put("numeroRelacion",numRelacion);
        datos.put("codTramite",codTramite);
        datos.put("ocurrenciaTramite",ocurrenciaTramite);
        datos.put("codDocumento",codDocumento);
        datos.put("codUsuario",codUsuario);
        datos.put("nombreDocumento",titulo); 
        if(numDocumento!=null) datos.put("numeroDocumento",numDocumento);
        datos.put("opcionGrabar", opcionGrabar);
        datos.put("params",params);
        datos.put("fichero",dataBytes);
        datos.put("perteneceRelacion","true");
        
        //AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance().getImplClass(Integer.toString(usuario.getOrgCod()));
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);
        Documento doc = null;
        int tipoDocumento = -1;
        if(!almacen.isPluginGestor())
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        else{
            String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(codMunicipio, codProcedimiento, codTramite, params);
            String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);            
            // Se recupera la lista de los expedientes que forman parte de la relación
            ArrayList<String> listadoExpedientes = TramitacionManager.getInstance().getExpedientesRelacion(codMunicipio, codProcedimiento, numRelacion, ejercicio, params);
            String descripcionOrganizacion = OrganizacionesManager.getInstance().getDescripcionOrganizacion(codMunicipio, params);
            
            datos.put("codigoVisibleTramite",codigoVisibleTramite);
            datos.put("listaExpedientesRelacion",listadoExpedientes);
            datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
            datos.put("extension",ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE);
            datos.put("tipoMime",ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE);
            /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
            ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");           
            String carpetaRaiz  = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ); 

            ArrayList<String> listaCarpetas = new ArrayList<String>();
            listaCarpetas.add(carpetaRaiz);
            listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + descripcionOrganizacion);
            listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
            listaCarpetas.add(ConstantesDatos.PREFIJO_DOCUMENTO_RELACION + numRelacion.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));
            datos.put("listaCarpetas",listaCarpetas);
            /** FIN **/
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
        }

        doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
        boolean documentoGrabado = almacen.setDocumento(doc);
        OperacionesExpedienteManager.getInstance().registrarAltaDocumentoTramite(doc,uVO.getNombreUsu(),params);
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
      try     {
      defaultAction(req, res);
    } catch (Exception e) {
          if (log.isErrorEnabled()) log.error("ERROR doGet:" + e);
          e.printStackTrace();
    }
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
    try {
        defaultAction(req, res);
    } catch (Exception e) {
            if (log.isErrorEnabled()) log.error("ERROR doPost:" + e);
            e.printStackTrace();
    }
  }
  
  
  
  
  
  
  
}

