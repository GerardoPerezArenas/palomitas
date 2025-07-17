package es.altia.flexia.interfaces.user.web.administracion.mantenimiento;

import com.google.gson.GsonBuilder;
import es.altia.agora.business.administracion.mantenimiento.AccesoModuloUsuarioVO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.manual.UsuarioDAO;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * Action que es llamada por medio de peticiones AJAX para cargar el contenido de la tabla
 * de acceso de usuarios.
 */
public class CargaInfoAccesoUsuarios extends DispatchAction{
    
    private Logger log = Logger.getLogger(CargaInfoAccesoUsuarios.class);
    
    /**
     * Método que es llamada cuando se desea recuperar la lista de documentos externos de un expediente
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    public ActionForward cargarTablaAccesosModulos(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        UsuarioDAO usuarioDao = UsuarioDAO.getInstance();

        try {

            
            String criterioCodOrganizacion = request.getParameter("codOrganizacion");
            String criterioLoginUsuario = request.getParameter("loginUsuario");
            String criterioNombreUsuario = request.getParameter("nombreUsuario");
            String criterioCodAplicacion = request.getParameter("codAplicacion");
            String criterioOpcionFecha = request.getParameter("opcionFecha");
            String criterioFechaInicio = request.getParameter("fechaInicio");
            String criterioFechaFin = request.getParameter("fechaFin");
            
            Integer idOrganizacion = null;
            Integer idAplicacion = null;
            Calendar fechaInicio = null;
            Calendar fechaFin = null;
            DateOperations.OPERACION_FECHA_BD opcionFecha = null;
            
            if (criterioCodOrganizacion != null && !criterioCodOrganizacion.isEmpty()) {
                idOrganizacion = Integer.valueOf(criterioCodOrganizacion);
            }
            if (criterioCodAplicacion != null && !criterioCodAplicacion.isEmpty()) {
                idAplicacion = Integer.valueOf(criterioCodAplicacion);
            }
            opcionFecha = DateOperations.OPERACION_FECHA_BD.obtieneOperacion(criterioOpcionFecha);
            fechaInicio = DateOperations.toCalendar(criterioFechaInicio, DateOperations.LATIN_DATE_FORMAT);
            fechaFin = DateOperations.toCalendar(criterioFechaFin, DateOperations.LATIN_DATE_FORMAT);

            // Se recupera de la sesión el parámetro usuario con la información del usuario logueado
            HttpSession session = request.getSession();
            UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");

            if (usuarioVO != null) {
                String[] params = usuarioVO.getParamsCon();

                adapt = new AdaptadorSQLBD(params);
                con = adapt.getConnection();

                List<AccesoModuloUsuarioVO> listaAccesos = usuarioDao.buscarAccesosModulos(
                        idOrganizacion,
                        idAplicacion,
                        criterioLoginUsuario,
                        criterioNombreUsuario,
                        opcionFecha,
                        fechaInicio,
                        fechaFin);
                
                if (listaAccesos != null) {
                    retornarJSON(new GsonBuilder().serializeNulls()
                            .setDateFormat(DateOperations.LATIN_DATE_24HOUR_WITHOUT_ZERO_FORMAT)
                            .create()
                            .toJson(listaAccesos), response);
                }
            }
        } catch (BDException e) {
            e.printStackTrace();
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error al recupera la lista de accesos a los módulos: " + e.getMessage());

        } finally {
            adapt.devolverConexion(con);
        }

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
