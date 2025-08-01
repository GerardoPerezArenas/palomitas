/* Generated by Together */
package es.altia.agora.interfaces.user.web.planeamiento.action.mantenimiento;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.planeamiento.mantenimiento.persistence.MantRelacionBienManager;
import es.altia.agora.business.planeamiento.mantenimiento.MantRelacionBienValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.planeamiento.form.mantenimiento.MantenimientoRelacionesBienForm;
import es.altia.agora.interfaces.user.web.planeamiento.form.mantenimiento.MantenimientoRelacionBienForm;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.io.IOException;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class CargarRelacionesBienAction extends ActionSession {

    protected static Log m_Log =
            LogFactory.getLog(CargarRelacionesBienAction.class.getName());

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        m_Log.info("CargarRelacionesBienAction.perform");
        String[] params = null;
        HttpSession session = request.getSession();

        if (session.getAttribute("usuario") != null){
            UsuarioValueObject usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
            params = usuarioVO.getParamsCon();
        }

        if (form == null) {
            m_Log.debug("Rellenamos el form de Mantenimiento de RelacionesBien");
            form = new MantenimientoRelacionesBienForm();
            request.setAttribute(mapping.getAttribute(), form);
        }
        MantenimientoRelacionBienForm relacionBienForm = null;
        Iterator relacionesBienIt = MantRelacionBienManager.getInstance().findAll(params).iterator();
        MantRelacionBienValueObject relacionBienVO = null;
        Collection relacionesBien = new ArrayList();
        while (relacionesBienIt.hasNext()) {
            relacionBienVO = (MantRelacionBienValueObject) relacionesBienIt.next();
            relacionBienForm = new MantenimientoRelacionBienForm();
            relacionBienForm.setCodigo(relacionBienVO.getCodigo());
            relacionBienForm.setDescripcion(relacionBienVO.getDescripcion());
            relacionesBien.add(relacionBienForm);
        }
        ((MantenimientoRelacionesBienForm) form).setRelacionesBien(relacionesBien);
        session.setAttribute("MantenimientoRelacionesBienForm", form);
        return (mapping.findForward("default"));
    }
}