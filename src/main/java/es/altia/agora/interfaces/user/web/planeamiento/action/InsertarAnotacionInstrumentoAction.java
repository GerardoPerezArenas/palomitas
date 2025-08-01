/* Generated by Together */
package es.altia.agora.interfaces.user.web.planeamiento.action;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.planeamiento.form.InstrumentoPlaneamientoForm;
import es.altia.agora.interfaces.user.web.planeamiento.form.AnotacionForm;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class InsertarAnotacionInstrumentoAction extends ActionSession {

    protected static Log m_Log =
            LogFactory.getLog(InsertarAnotacionInstrumentoAction.class.getName());

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        m_Log.info("InsertarAnotacionInstrumentoAction.perform");
        HttpSession session = request.getSession();

        InstrumentoPlaneamientoForm instrumentoForm = (InstrumentoPlaneamientoForm)
                session.getAttribute("InstrumentoPlaneamientoForm");
        Collection anotaciones = instrumentoForm.getAnotaciones();
        Iterator anotacionesIt = anotaciones.iterator();
        AnotacionForm anotacionForm = (AnotacionForm) form;
        String comentario = "";
        if (anotacionForm.getNumeroAnotacion().equals("")) {
            Integer numero = new Integer(1);
            while (anotacionesIt.hasNext()) {
                numero = new Integer((new Integer(((AnotacionForm) anotacionesIt.next()).getNumeroAnotacion())).intValue()
                        +1);
            }
            anotacionForm.setNumeroAnotacion(numero.toString());
            comentario = anotacionForm.getComentarioAnotacion().replaceAll("\r\n", "@intro@");
            anotacionForm.setComentarioAnotacion(comentario);
            anotaciones.add(anotacionForm);
        } else {
            AnotacionForm anotForm = null;
            anotaciones = new ArrayList();
            while (anotacionesIt.hasNext()) {
                anotForm = (AnotacionForm) anotacionesIt.next();
                if (anotForm.getNumeroAnotacion().equals(anotacionForm.getNumeroAnotacion())) {
                    anotForm.setFechaAnotacion(anotacionForm.getFechaAnotacion());
                    anotForm.setComentarioAnotacion(anotacionForm.getComentarioAnotacion().replaceAll("\r\n", "@intro@"));
                }
                anotaciones.add(anotForm);
            }
        }
        instrumentoForm.setAnotaciones(anotaciones);
        session.setAttribute("InstrumentoPlaneamientoForm", instrumentoForm);

        return (mapping.findForward("default"));
    }
}