package es.altia.flexia.interfaces.user.web.adminLocal.pluginDocumentos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.plugin.documentos.dao.RepositorioPluginAlmacenamientoDocumentosManager;
import es.altia.agora.business.sge.plugin.documentos.vo.PluginAlmacenamientoVO;
import es.altia.agora.business.sge.plugin.documentos.vo.RepositorioDocumentacionProcedimientoVO;
import es.altia.agora.business.sge.plugin.documentos.vo.RepositorioDocumentacionRegistroVO;
import es.altia.agora.business.sge.plugin.documentos.vo.SalidaTratamientoRepositorioDocumentacionVO;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.BDException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * Action que actúa de controlador y que se encarga de manejar las peticiones que 
 * tengan que ver con el mantenimiento del plugin de almacenamiento utilizado para la parte de registro
 * @author oscar
 */
public class MantenimientoAlmacenamientoDocumentoRegistroAction extends DispatchAction{
   
   private Logger log = Logger.getLogger(MantenimientoAlmacenamientoDocumentoProcedimientoAction.class);
    
    /**
     * Carga la pantalla de mantenimiento de plugin de almacenamiento por procedimiento. Recupera la información
     * necesaria de base de datos, y que será utilizada para mostrar en la pantalla
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return 
     * @throws Exception 
     */
    public ActionForward cargarPantalla(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        ArrayList<PluginAlmacenamientoVO> plugins = new ArrayList<PluginAlmacenamientoVO>();
        ArrayList<RepositorioDocumentacionProcedimientoVO> asignaciones = new ArrayList<RepositorioDocumentacionProcedimientoVO>();
                
        HttpSession session = request.getSession();        
        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        String[] params = null;
        
        if(usuario!=null){                    
            params = usuario.getParamsCon();              
            Hashtable<String,Object> salida = null;
            try{
                RepositorioPluginAlmacenamientoDocumentosManager manager = new RepositorioPluginAlmacenamientoDocumentosManager();
                salida = manager.getInfoCargaPantallaMantenimientoPluginRegistro(params);
                
                if(salida!=null){
                    plugins = (ArrayList<PluginAlmacenamientoVO>)salida.get("listaPlugin");                    
                    asignaciones = (ArrayList<RepositorioDocumentacionProcedimientoVO>)salida.get("asignacionPluginRegistro");                    
                }
            }catch(Exception e){
                e.printStackTrace();
            }                        
        }

        request.setAttribute("plugins",plugins);
        request.setAttribute("asignaciones",asignaciones);
        
        return mapping.findForward("pantalla");
    } 
    
    
    
    /**
     * Grabar la selección realizada en la que se pasan     
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return 
     * @throws Exception 
     */
    public ActionForward grabar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        SalidaTratamientoRepositorioDocumentacionVO salida = new SalidaTratamientoRepositorioDocumentacionVO();
        ArrayList<RepositorioDocumentacionRegistroVO> coleccion = null;
        Gson gson = new Gson();
        String parametro = request.getParameter("parametro");    
                
        log.debug("parametro: " + parametro);
        try{
            if(parametro!=null && parametro.length()>0){

                java.lang.reflect.Type tipo = new TypeToken<ArrayList<RepositorioDocumentacionRegistroVO>>(){}.getType();
                coleccion =(ArrayList<RepositorioDocumentacionRegistroVO>)gson.fromJson(parametro,tipo);
                
                HttpSession session = request.getSession();
                UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
                if(usuario!=null){
                    String[] params = usuario.getParamsCon();
                    
                    RepositorioPluginAlmacenamientoDocumentosManager manager = new RepositorioPluginAlmacenamientoDocumentosManager();
                    manager.grabarPluginPorRegistro(usuario.getOrgCod(),coleccion, params);
                    salida.setStatus(0);
                    salida.setDescStatus("OK");                    
                }
            }
        }
        catch(BDException e){
            e.printStackTrace();
            salida.setStatus(1);
            salida.setDescStatus("Error al obtener una conexión a la BBDD");
            
        }catch(TechnicalException e){
            e.printStackTrace();
            salida.setStatus(2);
            salida.setDescStatus("Error al grabar el plugin de almacenamiento por registro");
        }
        catch(Exception e){
            e.printStackTrace();
            salida.setStatus(3);
            salida.setDescStatus("Error al grabar el plugin de almacenamiento por registro");
        }
        
        retornarJSON(gson.toJson(salida),response);
        return null;
    }
    
    
     /**
     * Método llamado para devolver un String en formato JSON al cliente que ha realiza la petición 
     * a alguna de las operaciones de este action
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se devuelve la salida
     * al cliente que ha realizado la solicitud
     */
    private void retornarJSON(String json,HttpServletResponse response){
        
        try{
            if(json!=null){
                response.setCharacterEncoding("UTF-8");                
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
}
