package es.altia.agora.interfaces.user.web.integracionsw;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.integracionsw.persistence.MantenimientoSWManager;
import es.altia.util.exceptions.InternalErrorException;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

public class ListadoSWAction extends ActionSession {

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response)
            throws IOException, ServletException {

        if (m_Log.isDebugEnabled()) m_Log.debug("ENTRAMOS EN EL ACTION DE MantenimientoIntegracionSW");

        // Recuperamos el usuario y los datos de conexion.
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();

        // Recuperamos la opcion de la request.
        String opcion = request.getParameter("opcion");
        if (m_Log.isDebugEnabled()) m_Log.debug("LA OPCION EN EL ACTION ES " + opcion);

        // Recuperamos el formulario asociado.
        if (form == null) {
            form = new ListadoSWForm();
            if ("request".equals(mapping.getScope())) request.setAttribute(mapping.getAttribute(), form);
            else session.setAttribute(mapping.getAttribute(), form);
        }
        ListadoSWForm listadoForm = (ListadoSWForm) form;

        if (opcion.equals("publicarSW")) {
            String strCodigoSW = request.getParameter("codigoSW");
            int codigoSW = Integer.parseInt(strCodigoSW);
            String strPublicar = request.getParameter("publicar");
            boolean publicar = Boolean.parseBoolean(strPublicar);

            try {
                MantenimientoSWManager manager = MantenimientoSWManager.getInstance();
                boolean puedeCambiarEstado = manager.puedePublicarseServicioWeb(codigoSW, params);

                if (puedeCambiarEstado == publicar) {
                    manager.cambiarEstadoPublicacionSW(codigoSW, publicar, params);
                    listadoForm.setError(null);
                } else if (publicar) {
                    listadoForm.setError("EL SERVICIO WEB NO CUMPLE TODAS LAS CONDICIONES PARA SER PUBLICADO");
                } else {
                    listadoForm.setError("EL SERVICIO WEB NO CUMPLE TODAS LAS CONDICIONES PARA SER DESPUBLICADO");
                }

                Collection listaSW = manager.getListaServiciosWeb(params);
                listadoForm.setListaSW(new Vector(listaSW));

            } catch (InternalErrorException e) {
                e.printStackTrace();
            }
        }

        if (opcion.equals("cargarListadoSW")) {

            try {
                Collection listaSW = MantenimientoSWManager.getInstance().getListaServiciosWeb(params);
                listadoForm.setListaSW(new Vector(listaSW));

                String strRecargaPagina = request.getParameter("recargaPagina");
                if (strRecargaPagina != null && !Boolean.parseBoolean(strRecargaPagina)) opcion = "publicarSW";

            } catch (InternalErrorException e) {
                if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "ERROR: " + e.getMessage());
            }

        }

        if (opcion.equals("eliminarSW")) {

            String strCodigoSW = request.getParameter("codigoSW");
            int codigoSW = Integer.parseInt(strCodigoSW);

            try {
                MantenimientoSWManager manager = MantenimientoSWManager.getInstance();

                manager.eliminarServicioWeb(codigoSW, params);

                Collection listaSW = manager.getListaServiciosWeb(params);
                listadoForm.setListaSW(new Vector(listaSW));

            } catch (InternalErrorException iee) {
                if (m_Log.isErrorEnabled()) m_Log.error(iee.getMessage());
                iee.printStackTrace();
                request.setAttribute("error", "ERROR: " + iee.getMessage());
            }

        }

        return mapping.findForward(opcion);
    }
}
