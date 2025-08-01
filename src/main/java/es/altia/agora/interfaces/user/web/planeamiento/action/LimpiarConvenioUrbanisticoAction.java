/* Generated by Together */
package es.altia.agora.interfaces.user.web.planeamiento.action;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.planeamiento.form.ConvenioUrbanisticoForm;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class LimpiarConvenioUrbanisticoAction extends ActionSession {

    protected static Log m_Log =
            LogFactory.getLog(LimpiarConvenioUrbanisticoAction.class.getName());

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        m_Log.info("LimpiarConvenioUrbanisticoAction.perform");
        String[] params = null;
        HttpSession session = request.getSession();

        ConvenioUrbanisticoForm convenioForm = (ConvenioUrbanisticoForm)
                session.getAttribute("ConvenioUrbanisticoForm");

        Collection registrosConvenio = (Collection) session.getAttribute("registrosConvenio");
        Iterator registrosConvenioIt = null;
        if (registrosConvenio!=null) {
            registrosConvenioIt = registrosConvenio.iterator();
            registrosConvenio = new ArrayList();
            Integer numRegistro = new Integer(request.getParameter("numeroRegistro"));
            int i = 1;
            while (registrosConvenioIt.hasNext()) {
                ConvenioUrbanisticoForm convForm = (ConvenioUrbanisticoForm) registrosConvenioIt.next();
                if (numRegistro.intValue() == i) {
                    ConvenioUrbanisticoForm convenForm = (ConvenioUrbanisticoForm) convForm.clone();
                    registrosConvenio.add(convenForm);
                } else {
                    registrosConvenio.add(convForm);
                }
                i++;
            }
            session.setAttribute("registrosConvenio", registrosConvenio);
        }

        convenioForm.setPartesFirmantes(new ArrayList());
        convenioForm.setAnotaciones(new ArrayList());
        convenioForm.setRectificaciones(new ArrayList());
        session.setAttribute("ConvenioUrbanisticoForm", convenioForm);

        return (mapping.findForward("default"));
    }
}