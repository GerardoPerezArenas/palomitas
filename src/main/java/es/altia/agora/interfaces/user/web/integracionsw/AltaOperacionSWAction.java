package es.altia.agora.interfaces.user.web.integracionsw;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.integracionsw.persistence.MantenimientoSWManager;
import es.altia.agora.business.integracionsw.persistence.DefinicionOperacionesSWManager;
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

public class AltaOperacionSWAction extends ActionSession {

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        if (m_Log.isDebugEnabled()) m_Log.debug("ENTRAMOS EN EL ACTION DE AltaOperacionSW");

        // Recuperamos el usuario y los datos de conexion.
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();

        // Recuperamos el formulario asociado.
        if (form == null) {
            form = new AltaOperacionSWForm();
            if ("request".equals(mapping.getScope())) request.setAttribute(mapping.getAttribute(), form);
            else session.setAttribute(mapping.getAttribute(), form);
        }
        AltaOperacionSWForm altaOpSWForm = (AltaOperacionSWForm) form;

        // Recuperamos la opcion de la request.
        String opcion = request.getParameter("opcion");
        if (m_Log.isDebugEnabled()) m_Log.debug("LA OPCION EN EL ACTION ES " + opcion);

        if (opcion.equals("inicioAltaOperacionSW")) {
            String strCodigoSW = request.getParameter("codigoSW");
            int codigoSW = Integer.parseInt(strCodigoSW);
            try {
                Collection operaciones =
                        MantenimientoSWManager.getInstance().getOperacionesByServicioWeb(codigoSW, params);
                altaOpSWForm.setOperacionesCombo(new Vector(operaciones));

            } catch (InternalErrorException e) {
                e.printStackTrace();
            }
        }

        if (opcion.equals("confirmaAltaOp")) {
            try {
                // Recuperamos los parametros del formulario.
                String strCodigoSW = altaOpSWForm.getCodigoSW();
                int codigoSW = Integer.parseInt(strCodigoSW);
                String nombreOp = altaOpSWForm.getNombreOpSW();
                String tituloOp = altaOpSWForm.getTituloOpSW();

                DefinicionOperacionesSWManager manager = DefinicionOperacionesSWManager.getInstance();
                boolean existeOp = manager.existeOperacionByTitulo(tituloOp, codigoSW, params);

                if (existeOp) {
                    throw new InternalErrorException(
                            new Exception("YA EXISTE UNA OPERACION CON EL MISMO TITULO PARA ESTE SERVICIO WEB"));
                } else {

                    int codOpDef = manager.creaEstructuraArray(codigoSW, nombreOp, tituloOp, params);
                    altaOpSWForm.setCodigoOpDef(Integer.toString(codOpDef));

                    opcion = "cerrarVentana";
                    request.setAttribute("opcion", opcion);
                    
                }
            } catch (InternalErrorException e) {
                e.printStackTrace();
            }
        }
        if (m_Log.isDebugEnabled()) m_Log.debug("LA OPCION EN EL ACTION ES " + opcion);

        return mapping.findForward(opcion);
    }
}
