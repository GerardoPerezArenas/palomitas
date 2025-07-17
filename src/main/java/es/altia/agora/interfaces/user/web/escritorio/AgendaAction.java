package es.altia.agora.interfaces.user.web.escritorio;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.altia.agora.business.escritorio.AgendaElement;
import es.altia.agora.business.escritorio.Simulacion;
import es.altia.agora.interfaces.user.web.util.ActionSession;

public final class AgendaAction extends ActionSession {

	public ActionForward performSession(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws IOException, ServletException {

		HttpSession sesion = request.getSession(false);

        Simulacion sim = null;
		if (sesion == null) {
            m_Log.error("error: no existe session");            
        }else sim = (Simulacion) sesion.getAttribute("bd");

		if (sim==null) {
            m_Log.error("error: no existe sim dentro da session");
        }

		String ver = "";
		ver = (String) request.getParameter("ver");

		if (ver.equals("1")) {
			Collection colec = sim.get();
			Iterator iter = colec.iterator();
			int i=0;
			while (iter.hasNext()) {
				i++;
				AgendaElement aE = (AgendaElement)iter.next();
			}

			return (new ActionForward(mapping.getInput()));
		}

		// We cath the attributes from request
		String day = (String) request.getParameter("day");
		String month = (String) request.getParameter("month");
		String year = (String) request.getParameter("year");
		String note = (String) request.getParameter("note");
		String title = (String) request.getParameter("titulo");


		AgendaElement aE = new AgendaElement(day,month,year,note,title);
		sim.add(aE);

		return (new ActionForward(mapping.getInput()+"?day="+day+"&month="+month+"&year="+year));

	}

}