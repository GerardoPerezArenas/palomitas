
/* Generated by Together */
package es.altia.agora.interfaces.user.web.registro;

import es.altia.agora.business.escritorio.RegistroUsuarioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.ReservaOrdenValueObject;
import es.altia.agora.business.registro.persistence.RegistroAperturaCierreManager;
import es.altia.agora.business.registro.persistence.ReservaOrdenManager;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.util.ActionSession;

import java.io.IOException;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class ReservaOrdenAction extends ActionSession {

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        m_Log.debug("================= ReservaOrdenAction ======================>");
        ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
        HttpSession session = request.getSession();

        // Validaremos los parametros del request especificados

        ActionErrors errors = new ActionErrors();
        String opcion = request.getParameter("opcion");
        m_Log.info("OPCION: " + opcion);

        // Rellenamos el form de ReservaOrden

        if (form == null) {
            m_Log.debug("Rellenamos el form de RegistroSaida");
            form = new ReservaOrdenForm();
            if ("request".equals(mapping.getScope())) {
                request.setAttribute(mapping.getAttribute(), form);
            } else {
                session.setAttribute(mapping.getAttribute(), form);
            }
        }

        if ("salir".equals(opcion) || "salirS".equals(opcion) || "salirE".equals(opcion)) {
            /* Borramos de la sesion el form y redirigimos al jsp de inicio*/
            session.removeAttribute("ReservaOrdenForm");

        } else {
            ReservaOrdenForm reservaForm = (ReservaOrdenForm) form;
            ReservaOrdenValueObject reservaVO = new ReservaOrdenValueObject();
            ReservaOrdenManager reservaMan = ReservaOrdenManager.getInstance();

            /* Obtenemos ahora el ReservaOrden de la capa de negocio*/

            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();
            RegistroUsuarioValueObject registroUsuario = (RegistroUsuarioValueObject) session.getAttribute("registroUsuario");
            int codUnidad = registroUsuario.getUnidadOrgCod();
            int codDepto = registroUsuario.getDepCod();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Codigo de departamento: " + codDepto);
                m_Log.debug("Codigo de unidad: " + codUnidad);
            }
            String tipoReg = null;
            if (opcion.equals("cargarE") || opcion.equals("numeroE")) {
                tipoReg = "E";
            }
            if (opcion.equals("cargarS") || opcion.equals("numeroS")) {
                tipoReg = "S";
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("El tipo de registro es " + tipoReg);
            }

            RegistroValueObject registro = new RegistroValueObject();
            registro.setTipoReg(tipoReg);
            registro.setIdentDepart(codDepto);
            registro.setUnidadOrgan(codUnidad);

            reservaVO.setCodDepto(codDepto);
            reservaVO.setCodUnidad(codUnidad);
            reservaVO.setTipoReg(tipoReg);
            
            if (opcion.equals("cargarE") || opcion.equals("cargarS")) {
                Date fechaCierre = RegistroAperturaCierreManager.getInstance().getFechaRegistroCerrado(registro, params);
                Calendar calActual = Calendar.getInstance();
                calActual.set(Calendar.HOUR_OF_DAY, 0);
                calActual.set(Calendar.MINUTE, 0);
                calActual.set(Calendar.SECOND, 0);
                calActual.set(Calendar.MILLISECOND, 0);
                Date fechaActual = calActual.getTime();
                
                reservaVO.setA(fechaCierre == null || fechaActual.after(fechaCierre));                    
                                                
            } else {
                
                Calendar fechaActual = Calendar.getInstance();
                int anhoActual = fechaActual.get(Calendar.YEAR);
                reservaVO.setEjercicio(Integer.toString(anhoActual));
                reservaVO.setFecha(fechaActual.getTime());
                
                String cant = request.getParameter("cantidad");                
                reservaVO.setCantidad(cant);
                reservaVO.setUsuario(usuario.getIdUsuario());
                
                reservaMan.insert(reservaVO, params);

            }

            reservaForm.setReservaOrden(reservaVO);
            session.setAttribute("tipoAnotacion", tipoReg);
        }
        /* Redirigimos al JSP de salida*/
        m_Log.debug("<================= ReservaOrdenAction ======================");
        return (mapping.findForward(opcion));

    }
}
