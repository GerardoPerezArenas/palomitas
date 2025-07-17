package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.catalogoformularios.model.solicitudes.vo.FormularioTramitadoVO;
import es.altia.catalogoformularios.model.solicitudesfacade.FormularioFacade;
import es.altia.catalogoformularios.util.exceptions.InternalErrorException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Recuperar la lista de ficheros anexos a un formulario pdf
 * @author Diana Piñeiro
 * @version 1.0
 */

public class ListaAnexosAction extends ActionSession {

   protected static Log log =
           LogFactory.getLog(ListaAnexosAction.class.getName());

   public ActionForward performSession(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
       throws IOException, ServletException {

       if(m_Log.isDebugEnabled()) m_Log.debug("En ListaAnexosAction");
       String formPDF = request.getParameter("formPDF");
       if (formPDF==null){
           formPDF = (String) request.getAttribute("formPDF");
       }
       String estado = request.getParameter("estado");
       if (estado==null){
           estado = (String) request.getAttribute("estado");
       }

       Collection rdo = new ArrayList();
       try{
           //llamar a la fachada de formularios
           FormularioFacade facade = new FormularioFacade();
           rdo = facade.getFicherosOfForm(formPDF);
       }catch(InternalErrorException e){
           m_Log.debug("***** Fallo al cargar anexos del formulario '" + formPDF + "':" + e.getMessage());
       }

       if(m_Log.isDebugEnabled()) m_Log.debug("num anexos de '" + formPDF + "' recuperados="+rdo.size());

       HttpSession session = request.getSession();
       session.setAttribute("anexos", rdo);
       //request.setAttribute("anexos", rdo);
       if (!(FormularioTramitadoVO.PENDIENTE.equals(estado) || FormularioTramitadoVO.ERRONEO.equals(estado))){
          session.setAttribute("soloVer", "si");
          //request.setAttribute("soloVer", "si");
       }else{
          session.setAttribute("soloVer", "no");
          //request.setAttribute("soloVer","no");
       }

       if(m_Log.isDebugEnabled()) m_Log.debug("estado '" + estado + "' soloVer="+request.getAttribute("soloVer") + " error="+ request.getAttribute("error"));

       try {
           BeanUtils.setProperty(form, "formulario", formPDF);
       } catch (IllegalAccessException e) {
           throw new ServletException(e);
       } catch (InvocationTargetException e) {
           throw new ServletException(e);
       }

       return (mapping.findForward("Success"));
   }
}
