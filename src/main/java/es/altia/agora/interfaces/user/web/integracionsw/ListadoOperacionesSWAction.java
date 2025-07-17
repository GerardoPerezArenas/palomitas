package es.altia.agora.interfaces.user.web.integracionsw;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.integracionsw.persistence.DefinicionOperacionesSWManager;
import es.altia.agora.business.integracionsw.persistence.MantenimientoSWManager;
import es.altia.agora.business.integracionsw.persistence.DefinicionSWTramitacionManager;
import es.altia.agora.business.integracionsw.InfoServicioWebVO;
import es.altia.agora.business.integracionsw.exception.FalloPublicacionException;
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

public class ListadoOperacionesSWAction extends ActionSession {

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        if (m_Log.isDebugEnabled()) m_Log.debug("ENTRAMOS EN EL ACTION DE ListadoOperacionesSW");

        // Recuperamos el usuario y los datos de conexion.
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();

        // Recuperamos el formulario asociado.
        if (form == null) {
            form = new ListadoOperacionesSWForm();
            if ("request".equals(mapping.getScope())) request.setAttribute(mapping.getAttribute(), form);
            else session.setAttribute(mapping.getAttribute(), form);
        }
        ListadoOperacionesSWForm listadoOpsSWForm = (ListadoOperacionesSWForm) form;

        // Recuperamos la opcion de la request.
        String opcion = request.getParameter("opcion");
        if (m_Log.isDebugEnabled()) m_Log.debug("LA OPCION EN EL ACTION ES " + opcion);

        if (opcion.equals("iniciaCargaOperaciones")) {
            String strCodigoSW = request.getParameter("codigoSW");
            int codigoSW = Integer.parseInt(strCodigoSW);

            try {
                InfoServicioWebVO infoSW = MantenimientoSWManager.getInstance().getInfoGeneralSWByCodigo(codigoSW, params);
                listadoOpsSWForm.setCodigoSW(Integer.toString(infoSW.getCodigoSW()));
                listadoOpsSWForm.setTituloSW(infoSW.getTituloSW());
                listadoOpsSWForm.setWsdlSW(infoSW.getWsdlSW());

                Collection listaOperaciones =
                        DefinicionOperacionesSWManager.getInstance().getOpsDefinidasBySW(codigoSW, params);

                listadoOpsSWForm.setListaOperaciones(new Vector(listaOperaciones));
            } catch (InternalErrorException e) {
                if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "ERROR: " + e.getMessage());
            }

        }

        if (opcion.equals("publicarInforme")) {
            String strCodOperacion = request.getParameter("codOperacion");
            String strPublicar = request.getParameter("publicar");
            String strCodigoSW = request.getParameter("codServicioWeb");
            int codOperacion = Integer.parseInt(strCodOperacion);
            boolean publicar = Boolean.parseBoolean(strPublicar);
            int codigoSW = Integer.parseInt(strCodigoSW);

            DefinicionOperacionesSWManager manager = DefinicionOperacionesSWManager.getInstance();
            try {
                if (publicar) {
                    // Publicamos Operacion.
                    manager.isOperacionDefinida(codOperacion, params);
                    manager.cambiaEstadoPublicacion(codOperacion, publicar, params);
                    listadoOpsSWForm.setError(null);
                } else {
                    // Despublicamos operacion.
                    boolean puedeDespublicarse = 
                            !DefinicionSWTramitacionManager.getInstance().existeOperacionAsociada(codOperacion, params);
                    if (puedeDespublicarse) {
                        manager.cambiaEstadoPublicacion(codOperacion, publicar, params);
                        listadoOpsSWForm.setError(null);
                    } else listadoOpsSWForm.setError("NO SE PUEDE DESPUBLICAR LA OPERACION PORQUE HA SIDO ASOCIADA A " +
                            "ALGUN TRAMITE DE ALGUN PROCEDIMIENTO");
                }
                Collection listaOperaciones = manager.getOpsDefinidasBySW(codigoSW, params);
                listadoOpsSWForm.setListaOperaciones(new Vector(listaOperaciones));
            } catch (InternalErrorException iee) {
                iee.printStackTrace();
            } catch (FalloPublicacionException fpe) {
                listadoOpsSWForm.setError(fpe.getMessage());
            }
        }

        return mapping.findForward(opcion);

    }
}
