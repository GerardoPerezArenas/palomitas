package es.altia.agora.interfaces.user.web.util;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.persistence.DocumentosExpedienteManager;
import es.altia.agora.business.sge.persistence.OperacionesExpedienteManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.common.service.config.*;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.sge.DocumentosExpedienteForm;
import es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm;
import es.altia.agora.technical.ConstantesDatos;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class GrabarDocOfficeServlet extends HttpServlet
{
  protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");

  protected static Log log =
          LogFactory.getLog(GrabarDocOfficeServlet.class.getName());

  public void init(ServletConfig config) throws ServletException
  {
    super.init(config);
  }
  
  public void defaultAction(HttpServletRequest req,HttpServletResponse res) throws Exception
  {
      log.debug ("Entrando en el servlet de grabación de documentos.");
      HttpSession session=req.getSession();
      
      Enumeration t=session.getAttributeNames();
      while (t.hasMoreElements()){log.debug ("-"+t.nextElement().toString());}
      
      FichaExpedienteForm fef = (FichaExpedienteForm) session.getAttribute("FichaExpedienteForm");
      String codMunicipio = fef.getCodMunicipio();
      String codProcedimiento = fef.getCodProcedimiento();
      String ejercicio = fef.getEjercicio();
      String numExpediente = fef.getNumExpediente();
      
      DocumentosExpedienteForm def = (DocumentosExpedienteForm) session.getAttribute("DocumentosExpedienteForm");
      String codDocumento = def.getCodDocumento();
      String nombreDocumento = def.getNombreDocumento();
      String ocurrenciaTramite = def.getOcurrenciaTramite();
      String codTramite = def.getCodTramite();
      String numDocumento = def.getNumeroDocumento();
      log.debug (numDocumento+"-----------");
      
      
      UsuarioValueObject uVO = (UsuarioValueObject) session.getAttribute("usuario");
      String codUsuario = String.valueOf(uVO.getIdUsuario());
      String codApl = String.valueOf(uVO.getAppCod());
        
      String[] params = new String[7];
      params = uVO.getParamsCon();
                
        Hashtable<String,Object> datos = new Hashtable<String,Object>();
        datos.put("codMunicipio",codMunicipio);
        datos.put("codProcedimiento",codProcedimiento);
        datos.put("ejercicio",ejercicio);
        datos.put("numeroExpediente",numExpediente);
        datos.put("codTramite",codTramite);
        datos.put("ocurrenciaTramite",ocurrenciaTramite);
        datos.put("codDocumento",codDocumento);
        datos.put("codUsuario",codUsuario);        
        if(numDocumento!=null) datos.put("numeroDocumento",numDocumento);
        datos.put("perteneceRelacion","false");
        datos.put("params",params);
        
        ObjectInputStream os = new ObjectInputStream(req.getInputStream());
        Vector d = (Vector) os.readObject();
        String titulo = new String ((byte[])d.get(0));
        byte[] dataBytes = (byte[]) d.get(1);
        
        
        datos.put("nombreDocumento",titulo);
        datos.put("fichero",dataBytes);
        
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(uVO.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(uVO.getOrgCod()),codProcedimiento);
        Documento doc = null;
        int tipoDocumento = -1;
        if(!almacen.isPluginGestor())
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        else{
            String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(Integer.toString(uVO.getOrgCod()), codProcedimiento,codTramite,params);
            String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);            
            datos.put("codigoVisibleTramite",codigoVisibleTramite);
            datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);           
            datos.put("extension",ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE);
            datos.put("tipoMime",ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE);
           
            /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
            ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");            
            String carpetaRaiz = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + uVO.getOrgCod() + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
            
            String descripcionOrganizacion = uVO.getOrg();
            ArrayList<String> listaCarpetas = new ArrayList<String>();
            listaCarpetas.add(carpetaRaiz);
            listaCarpetas.add(uVO.getOrgCod() + ConstantesDatos.GUION + descripcionOrganizacion);
            listaCarpetas.add(codProcedimiento+ ConstantesDatos.GUION + nombreProcedimiento);
            listaCarpetas.add(numExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));

            datos.put("listaCarpetas",listaCarpetas);
            /*** FIN  ***/
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
        }

        doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
        try{
            boolean guardado = almacen.setDocumento(doc);
            OperacionesExpedienteManager.getInstance().registrarAltaDocumentoTramite(doc,uVO.getNombreUsu(),params);
        }catch(Exception e){
            e.printStackTrace();
        }
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

