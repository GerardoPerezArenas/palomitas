package es.altia.agora.interfaces.user.web.integracionsw;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.business.integracionsw.persistence.MantenimientoSWManager;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.util.exceptions.InternalErrorException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

public class MantenimientoIntegracionSWAction extends ActionSession {

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        if (m_Log.isDebugEnabled()) m_Log.debug("ENTRAMOS EN EL ACTION DE MantenimientoIntegracionSW");

        // Recuperamos la opcion de la request.
        String opcion = request.getParameter("opcion");

        // Recuperamos el usuario y los datos de conexion.
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();
        
        // Recuperamos el formulario asociado.
        if (form == null) {
            m_Log.debug("Rellenamos el form de Ficha Informe");
            form = new MantenimientoIntegracionSWForm();
            if ("request".equals(mapping.getScope())) request.setAttribute(mapping.getAttribute(), form);
            else session.setAttribute(mapping.getAttribute(), form);
        }
        MantenimientoIntegracionSWForm mantIntegrForm = (MantenimientoIntegracionSWForm)form;

        if (opcion.equals("inicioMantenimiento")) {
            try {
                Collection listaSW = MantenimientoSWManager.getInstance().getListaServiciosWeb(params);
                mantIntegrForm.setListaSW(new Vector(listaSW));
            } catch (InternalErrorException e) {
                //TODO: Redireccion para mostrar mensajes de error.
                e.printStackTrace();
            }
        }

        return mapping.findForward(opcion);

	}
}
