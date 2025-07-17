package es.altia.agora.interfaces.user.web.escritorio;

import es.altia.common.service.config.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

public final class LogoutAction extends Action {

    protected static Log mlog =
            LogFactory.getLog(LogoutAction.class.getName());
    protected static Config m_Conf = ConfigServiceHelper.getConfig("common");

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        mlog.debug("LogoutAction--------->BEGIN");
        HttpSession session = request.getSession();
        //Invalidamos la sesion
        session.invalidate();
        mlog.debug("LogoutAction--------->END");
        return null;
    }
}

