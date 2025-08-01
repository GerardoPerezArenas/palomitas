/* Generated by Together */
package es.altia.agora.interfaces.user.web.planeamiento.action.mantenimiento;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.planeamiento.mantenimiento.persistence.MantGradoProteccionManager;
import es.altia.agora.business.planeamiento.mantenimiento.MantGradoProteccionValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.planeamiento.form.mantenimiento.MantenimientoGradosProteccionForm;
import es.altia.agora.interfaces.user.web.planeamiento.form.mantenimiento.MantenimientoGradoProteccionForm;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class InsertarGradoProteccionAction extends ActionSession {

    protected static Log m_Log =
            LogFactory.getLog(InsertarGradoProteccionAction.class.getName());

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        m_Log.info("InsertarGradoProteccionAction.perform");
        String[] params = null;
        HttpSession session = request.getSession();

        if (session.getAttribute("usuario") != null){
            UsuarioValueObject usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
            params = usuarioVO.getParamsCon();
        }

        MantenimientoGradosProteccionForm gradosProteccionForm = (MantenimientoGradosProteccionForm) form;
        MantGradoProteccionValueObject gradoProteccionVO = new MantGradoProteccionValueObject(gradosProteccionForm.getCodigo(), gradosProteccionForm.getDescripcion());
        MantGradoProteccionManager.getInstance().create(gradoProteccionVO, params);

        Collection gradosProteccion = ((MantenimientoGradosProteccionForm) session.getAttribute("MantenimientoGradosProteccionForm")).getGradosProteccion();
        MantenimientoGradoProteccionForm gradoProteccionForm = new MantenimientoGradoProteccionForm();
        gradoProteccionForm.setCodigo(gradosProteccionForm.getCodigo());
        gradoProteccionForm.setDescripcion(gradosProteccionForm.getDescripcion());
        gradosProteccion.add(gradoProteccionForm);
        gradosProteccionForm.setGradosProteccion(gradosProteccion);

        session.setAttribute("MantenimientoGradosProteccionForm", gradosProteccionForm);
        return (mapping.findForward("default"));
    }
}