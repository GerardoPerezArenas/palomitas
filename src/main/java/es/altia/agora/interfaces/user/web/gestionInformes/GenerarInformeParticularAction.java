package es.altia.agora.interfaces.user.web.gestionInformes;

import es.altia.agora.business.gestionInformes.particular.InformeParticularFacade;
import es.altia.agora.business.gestionInformes.particular.InformeParticularFactory;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.common.exception.TechnicalException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

public class GenerarInformeParticularAction extends ActionSession {

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {

        InformeParticularForm infParticularForm = (InformeParticularForm) form;

        try {
            // Obtengo los datos del usuario de la sesión.
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            int codOrganizacion = usuario.getOrgCod();
            String url = request.getSession().getServletContext().getRealPath("/");

            InformeParticularFacade infFacade = (InformeParticularFacade) request.getSession().getAttribute("fachadaInformeParticular");
            if (infFacade == null) {
                infFacade = InformeParticularFactory.getImpl(infParticularForm.getTipoInforme(), usuario.getParamsCon());
            }

            GeneralValueObject informeDatosConsulta = infFacade.recuperarDatosRequest(request);

            informeDatosConsulta.setAtributo("codOrganizacion", codOrganizacion);
            Collection datosInforme = infFacade.recuperaDatosInforme(informeDatosConsulta,usuario);
            String rutaInforme = infFacade.generarInforme(informeDatosConsulta, datosInforme,usuario,
            		url,this.getServlet().getServletContext().getRealPath(""));

            
            /*Cuando se genera el informe debemos comprobar el parametro formato del request.
             * Si existe, es que se pueden elegir varios formatos de archivo y debemos actualizar
             * el form. En caso de que sea nulo, es que no existe esta posibilidad y 
             * se deja el tipo del properties.
             * */
           if (request.getParameter("formato")!=null)
            		infParticularForm.setTipoFichero(request.getParameter("formato"));
            		m_Log.debug("TIPO DE FICHERO = " + infParticularForm.getTipoFichero());
          
            
            infParticularForm.setRutaInforme(rutaInforme);
            infParticularForm.setEstadoInforme("informeGenerado");

        } catch (TechnicalException te) {
            Vector<String> errores = new Vector<String>();
            errores.add("ERROR:" + te.getMessage());
            infParticularForm.setErrores(errores);
        }

        request.setAttribute("InformeParticularForm", infParticularForm);
        return mapping.findForward("verInforme");

    } 
}
