package es.altia.agora.interfaces.user.web.integracionsw;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.integracionsw.persistence.MantenimientoSWManager;
import es.altia.agora.business.integracionsw.procesos.AccesoServicioWebWSDL;
import es.altia.agora.business.integracionsw.InfoServicioWebVO;
import es.altia.util.exceptions.InternalErrorException;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;

public class AltaSWAction extends ActionSession {

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response)
            throws IOException, ServletException {

        if (m_Log.isDebugEnabled()) m_Log.debug("ENTRAMOS EN EL ACTION DE AltaSW");

        // Recuperamos el usuario y los datos de conexion.
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();

        // Recuperamos el formulario asociado.
        if (form == null) {
            form = new AltaSWForm();
            if ("request".equals(mapping.getScope())) request.setAttribute(mapping.getAttribute(), form);
            else session.setAttribute(mapping.getAttribute(), form);
        }
        AltaSWForm altaSWForm = (AltaSWForm) form;

        // Recuperamos la opcion de la request.
        String opcion = request.getParameter("opcion");

        if (m_Log.isDebugEnabled()) m_Log.debug("LA OPCION EN EL ACTION ES " + opcion);
        if (opcion.equals("nuevoSW")) {
            String tituloSW = altaSWForm.getTituloNewSW();
            String wsdlSW = altaSWForm.getWsdlNewSW();
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETROS RECUPERADOS EN EL FORM:");
            if (m_Log.isDebugEnabled()) m_Log.debug("TITULO NUEVO SW: " + tituloSW);
            if (m_Log.isDebugEnabled()) m_Log.debug("WSDL NUEVO SW: " + wsdlSW);

            boolean terminadoCorrectamente = false;

            try {
                MantenimientoSWManager manager = MantenimientoSWManager.getInstance();
                // COMPROBAMOS SI YA EXISTE UN SERVICIO WEB CON ESE NOMBRE.
                if (manager.existeServicioWebPorTitulo(tituloSW, params)) {
                    throw new InternalErrorException(new Exception("YA EXISTE UN SERVICIO WEB CON ESE TITULO"));
                }

                AccesoServicioWebWSDL acceso = new AccesoServicioWebWSDL(wsdlSW, tituloSW);
                InfoServicioWebVO infoSW = acceso.getInfoServicioWebVO();
                infoSW = manager.altaServicioWeb(infoSW, params);

                altaSWForm.setCodigoSW(Integer.toString(infoSW.getCodigoSW()));
                terminadoCorrectamente = true;

            } catch (InternalErrorException iee) {
                if (m_Log.isErrorEnabled()) m_Log.error(iee.getNestedException().getMessage());
                iee.printStackTrace();
                request.setAttribute("error", "ERROR: " + iee.getNestedException().getMessage());
            } catch (Exception e) {
                if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "ERROR: " + e.getMessage());
            }

            if (terminadoCorrectamente) {
                opcion = "cerrarVentana";
                request.setAttribute("opcion", opcion);
                request.setAttribute("estado", "SE HA GRABADO LA DEFINICION DEL SERVICIO WEB CORRECTAMENTE");
            } else {
                opcion = "inicioAltaSW";
                request.setAttribute("estado", "NO SE HA PODIDO GRABAR LA DEFINICION DEL SERVICIO WEB");
            }
        }

        return mapping.findForward(opcion);
    }


}