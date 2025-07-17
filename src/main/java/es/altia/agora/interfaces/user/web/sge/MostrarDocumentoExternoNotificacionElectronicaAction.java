package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.manual.DefinicionProcedimientosDAO;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.notificacion.persistence.AdjuntoNotificacionDAO;
import es.altia.flexia.notificacion.persistence.AdjuntoNotificacionManager;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.flexia.registro.justificante.util.FileOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * Devuelve el contenido binario de un documento externo que se ha adjuntado a una notificación electrónica
 * @author oscar
 */
public class MostrarDocumentoExternoNotificacionElectronicaAction extends ActionSession{
    
    
      private Logger log = Logger.getLogger(MostrarDocumentoExternoNotificacionElectronicaAction.class);

        public ActionForward performSession(	ActionMapping mapping,
                    ActionForm form,
                    HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {

      m_Log.debug("performSession");
      String codDocumento = request.getParameter("codDocumento");
      String codTramite = request.getParameter("codTramite");
      String ocurrenciaTramite = request.getParameter("ocurrenciaTramite");
      String numExpediente = request.getParameter("numExpediente");
      String ejercicio     = request.getParameter("ejercicio");
      String codMunicipio  = request.getParameter("codMunicipio");

      log.debug(" =============> MostrarDocumentoExternoNotificacionElectronicaAction codDocumento: " + codDocumento);
      BufferedOutputStream bos= null;      
      String nombreFichero = "";
      String tipoMime = "";
      
      byte[] file = null;
      AdaptadorSQLBD adapt = null;
      Connection con = null;
      
      try{
          
          HttpSession session = request.getSession();
          UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
          
          adapt = new AdaptadorSQLBD(usuario.getParamsCon());
          con = adapt.getConnection();
          
          if(!"".equals(codDocumento)){
            
            if(usuario!=null){                
                                
                String codProcedimiento = null;
                String[] datosExp = numExpediente.split("/");
                String[] params = usuario.getParamsCon();
                codProcedimiento = datosExp[1];
                                
                Hashtable<String,Object> datos = new Hashtable<String,Object>();
                datos.put("codMunicipio",codMunicipio);                
                datos.put("ejercicio",ejercicio);
                datos.put("numeroExpediente",numExpediente);              
                datos.put("codTramite",codTramite);
                datos.put("ocurrenciaTramite",ocurrenciaTramite);
                datos.put("params",params);                
                datos.put("codDocumento",codDocumento);
                datos.put("codProcedimiento",codProcedimiento);

                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);
                
                Documento doc = null;
                int tipoDocumento = -1;
                if(!almacen.isPluginGestor())
                  tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
                else{
                    
                  codProcedimiento = numExpediente.split("[/]")[1];
                  String nombreProcedimiento = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento, con);
                  AdjuntoNotificacionVO adjunto = AdjuntoNotificacionDAO.getInstance().getInfoDocumentoExternoNotificacion(Integer.parseInt(codDocumento),con);
                  
                  datos.put("nombreDocumento",FileOperations.getNombreArchivo(adjunto.getNombre()));
                  datos.put("extension",FileOperations.getExtension(adjunto.getNombre()));
                  datos.put("tipoMime", adjunto.getContentType());            

                  ResourceBundle bundle = ResourceBundle.getBundle("documentos");              
                  String carpetaRaiz  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ); 
                  
                  
                  datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);                  
                  ArrayList<String> listaCarpetas = new ArrayList<String>();
                  listaCarpetas.add(carpetaRaiz);
                  listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + usuario.getOrg());
                  listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                  listaCarpetas.add(numExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));                    
                  listaCarpetas.add(ConstantesDatos.CARPETA_DOCUMENTOS_EXTERNOS_NOTIFICACION);
                  datos.put("listaCarpetas",listaCarpetas);
                  
                  tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
               }

                doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                doc = almacen.getDocumentoExternoNotificacion(doc,con);

                file  = doc.getFichero();
                nombreFichero = doc.getNombreDocumento() + ConstantesDatos.DOT + doc.getExtension();
                tipoMime = doc.getTipoMimeContenido();
            }
          }
        }catch(BDException e){
            e.printStackTrace();                  
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
        }                               
        catch(AlmacenDocumentoTramitacionException e){
            e.printStackTrace();                  
            log.error("Error al otener una conexión a la BBDD: " + e.getMessage());
        }finally{
          
            try{
                adapt.devolverConexion(con);
                
            }catch(Exception e){
                e.printStackTrace();
            }          
        }                               

        if(file != null){
          if (log.isDebugEnabled())  log.debug("DOCUMENTO OK ::: " + file.length);
          response.setHeader("Content-disposition","attachment;filename="+nombreFichero);
          response.setContentType(tipoMime);
          response.setHeader("Content-Transfer-Encoding", "binary");
          response.setContentLength(file.length);
          ServletOutputStream out = response.getOutputStream();
          bos = new BufferedOutputStream(out);
          bos.write(file, 0, file.length);
        }
      
     return null;
  }
        
}
