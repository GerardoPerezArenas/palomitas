/* Generated by Together */
package es.altia.agora.interfaces.user.web.planeamiento.action;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.planeamiento.form.ConvenioUrbanisticoForm;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
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

public final class InsertarPromotorConvenioAction extends ActionSession {

    protected static Log m_Log =
            LogFactory.getLog(InsertarPromotorConvenioAction.class.getName());

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        m_Log.info("InsertarPromotorConvenioAction.perform");
        HttpSession session = request.getSession();

        ConvenioUrbanisticoForm convenioForm = (ConvenioUrbanisticoForm)
                session.getAttribute("ConvenioUrbanisticoForm");
        Collection firmantes = convenioForm.getPartesFirmantes();
        firmantes.add(form);
        convenioForm.setPartesFirmantes(firmantes);
        session.setAttribute("ConvenioUrbanisticoForm", convenioForm);

        return (mapping.findForward("default"));
    }
}