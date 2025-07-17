package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

import es.altia.agora.business.administracion.mantenimiento.persistence.ParametrosManager;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ParametrosAction extends ActionSession {

    public ActionForward performSession(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        m_Log.debug("================= ParametrosAction ======================>");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        ParametrosForm paramsForm = (ParametrosForm) form;
        String formName = mapping.getAttribute();

        // Parametros propios de esta accion
        String opcion = request.getParameter("opcion");
        m_Log.debug("Opcion : " + opcion);

        // Recuperamos los parametros de conexión
        String[] params = usuario.getParamsCon();

        // ---- OPCION CARGAR ----
        if (opcion.equals("cargar")){
        // Carga la página de parametros con los valores actuales de los parametros.

            try {
                HashMap<String, String> parametros =
                    ParametrosManager.getInstance().obtenerParametros(params);
                paramsForm.setParametros(parametros);
            } catch (Exception e) {
                opcion = "fallo";
                e.printStackTrace();
            }

        //---- OPCION GRABAR ----
        } else if(opcion.equals("grabar")) {
        // Graba los valores de los parametros.

            try {
                ParametrosManager.getInstance().grabarParametros(paramsForm.getParametros(), params);
            } catch (Exception e) {
                opcion = "fallo";
                e.printStackTrace();
            }
            
        //---- OPCION SALIR ----
        } else if(opcion.equals("salir")) {
        // Abandona la página de parametros.

            if ((session.getAttribute(formName) != null))
                session.removeAttribute(formName);
        }

        m_Log.debug("<================= ParametrosAction ======================");
        return (mapping.findForward(opcion));
    }
}
