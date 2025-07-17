/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.interfaces.user.web.xpdl;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import java.util.Vector;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;



/**
 *Esta clase se encarga de consultar y devolver un listado de los distintos 
 * procedimientos de los que consta la aplicación. 
 * La pagina jsp asociada , mostrara por pantalla este listado , y seleccionando
 * uno de los procedimientos se podrá generar su correspondiente xpdl.
 * También se permitirá importar un .xpdl para insertar un procedimiento en la 
 * BD
 * @author ricardo.iglesias
 */
public class ListaProcedimientosXPDLAction extends ActionSession {

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        if (m_Log.isDebugEnabled()) m_Log.debug("ENTRAMOS EN EL ACTION DE listaProcedimientosXPDL");

        // Recuperamos el usuario y los datos de conexion.
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();

        String uri = request.getRequestURI();
     
        // Recuperamos el formulario asociado.
        if (form == null) {
            form = new ListaProcedimientosXPDLForm();
            if ("request".equals(mapping.getScope())) request.setAttribute(mapping.getAttribute(), form);
            else session.setAttribute(mapping.getAttribute(), form);
        }
        ListaProcedimientosXPDLForm listadoProcForm = (ListaProcedimientosXPDLForm) form;

        // Recuperamos la opcion de la request.
        String opcion = request.getParameter("opcion");
        if (m_Log.isDebugEnabled()) m_Log.debug("LA OPCION EN EL ACTION ES " + opcion);

        if (opcion.equals("cargarListaProcedimientos")) {
            

            try {
                Vector<DefinicionProcedimientosValueObject> listaProcedimientos = cargarListaProcedimientos(params);
                
                listadoProcForm.setListaProcedimientos(listaProcedimientos);
                
                
             }catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "ERROR: " + e.getMessage());
            }

        }
      
        m_Log.debug(mapping.findForward(opcion));
        return mapping.findForward(opcion);

    } 
    
    private Vector<DefinicionProcedimientosValueObject> cargarListaProcedimientos(String params[]) throws AnotacionRegistroException{
        
       DefinicionProcedimientosManager defProcManager=DefinicionProcedimientosManager.getInstance();
       DefinicionProcedimientosValueObject defProcVO = new DefinicionProcedimientosValueObject();
       defProcVO.setListaTiposProcedimientos(defProcManager.getListaTiposProcedimientos(params));
       defProcVO.setListaArea(defProcManager.getListaArea(params));
       return defProcManager.consultar(defProcVO, params);
    
    }
}


