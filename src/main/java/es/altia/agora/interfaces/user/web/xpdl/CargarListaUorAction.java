package es.altia.agora.interfaces.user.web.xpdl;

import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.arboles.impl.ArbolImpl;
import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Se encarga de cargar el árbol de UORS
 * @author oscar.rodriguez
 */
public class CargarListaUorAction extends ActionSession {

    private Logger log = Logger.getLogger(CargarListaUorAction.class);
    private final String MOSTRAR_ARBOL  = "mostrarArbol";

    public ActionForward performSession(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
    
        log.debug(" ========> CargarListaUorAction ");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");

        int codOrganizacion = usuario.getOrgCod();
        int codEntidad     = usuario.getEntCod();        
        String[] params = usuario.getParamsCon();


        // Variable opción que indica la operación que se realizar
        String opcion = request.getParameter("opcion");

        if("cargarUors".equals(opcion)){            
            // seleccionar el jndi para el esquema apropiado
            String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(Integer.toString(codEntidad), Integer.toString(codOrganizacion), String.valueOf(usuario.getAppCod()), params);
            String[] paramsNuevos = new String[7];
            paramsNuevos[0] = params[0]; // "oracle";
            paramsNuevos[6] = jndi;

            //===============================
            // nuevas UORs
            Vector nuevasUOR = UORsManager.getInstance().getListaUORs(false,paramsNuevos);
            m_Log.debug("Cargadas " + nuevasUOR.size() + " UORs");
            //arbol de jerarquía de uors
            ArbolImpl arbol = UORsManager.getInstance().getArbolUORs(false,false,false, paramsNuevos);
            m_Log.debug("Cargadas " + (arbol.contarNodos() -1) + " UORs en el árbol");

            request.setAttribute("arbolUORs", arbol);
            request.setAttribute("listaUORs", nuevasUOR);
            opcion = MOSTRAR_ARBOL;
        }else
         if("seleccionUorCodigo".equals(opcion)){           
            // nuevas UORs
            Vector uors = UORsManager.getInstance().getListaUORsPorNoVisRegistro('0', params);
            m_Log.debug("Recuperadas " + uors.size() + " UORs");
            //registroForm.setListaNuevasUORs(nuevasUOR);
            //arbol de jerarquía de uors
            ArbolImpl arboluors = UORsManager.getInstance().getArbolUORs(false,true,false, params);
            m_Log.debug("Recuperadas " + (arboluors.contarNodos() - 1) + " UORs en el árbol");
            request.setAttribute("opcion","cargarCodigo");
            request.setAttribute("arbolUORs", arboluors);
            request.setAttribute("listaUORs", uors);            
            opcion = MOSTRAR_ARBOL;
            return mapping.findForward(opcion);
         }else
         if("seleccionUorNombre".equals(opcion)){            
            Vector nUOR = UORsManager.getInstance().getListaUORs(false,params);//.getListaUORsPorNombre(params);
            // arbol de jerarquía de uors
            ArbolImpl arbolUORs = UORsManager.getInstance().getArbolUORs(false,false, true, params);
            m_Log.debug("Cargado árbol:" + arbolUORs.contarNodos());
            request.setAttribute("opcion","cargarNombre");
            request.setAttribute("listaUORs", nUOR);
            request.setAttribute("arbolUORs", arbolUORs);
            opcion = MOSTRAR_ARBOL;            
            return mapping.findForward(opcion);
         }
    
        return mapping.findForward(opcion);

  } //performSession
    
}// CargarListaUorAction