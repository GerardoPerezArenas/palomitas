package es.altia.agora.interfaces.user.web.infDireccion;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.RegistroUsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.util.CargaMenu;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class InformesDireccionAction extends ActionSession {

    public ActionForward performSession(	ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        String opcion ="";
        
        if ((session.getAttribute("usuario") != null)) {
            UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");

            if (form == null) {
                form = new InformesDireccionForm();
                if ("request".equals(mapping.getScope()))
                    request.setAttribute(mapping.getAttribute(), form);
                else
                    session.setAttribute(mapping.getAttribute(), form);
            }

            opcion = request.getParameter("opcion");

            if (opcion.equals("consultaExpedientes")){
                usuario.setApp("CONSULTA DE EXPEDIENTES");
                usuario.setAppCod(4);
                String soloConsultaExpedientes = UsuarioManager.getInstance().soloConsultarExpedientes(usuario);
                usuario.setSoloConsultarExp(soloConsultaExpedientes);
                session.setAttribute("usuario", usuario);
                CargaMenu cargaMenu = new CargaMenu();
                session.setAttribute("menuAppUsu",cargaMenu.getMenu(usuario));
                                
            } else if(opcion.equals("ExpAreaProcedimiento") || opcion.equals("ExpUnidTramitadora") ||
                    opcion.equals("ExpProcUnidTramit") || opcion.equals("ExpProcTipoTramite")) {                
                usuario.setApp("CONSULTA DE EXPEDIENTES");
                usuario.setAppCod(4);
                session.setAttribute("usuario", usuario);
                
            } else if(opcion.equals("registroLibroE")||opcion.equals("registroLibroS")
                    || opcion.equals("registroUORE") || opcion.equals("registroUORS") || opcion.equals("registroTotales")){
               
                usuario.setApp("REGISTRO");
                usuario.setAppCod(1);
                session.setAttribute("usuario", usuario);
                
                Vector<UORDTO> vecUOR = UsuarioManager.getInstance().buscaUnidadOrgInforme(usuario);
                if (session.getAttribute("vecUOR") != null) session.removeAttribute("vecUOR");
                session.setAttribute("vecUOR", vecUOR);
                
                String uorCod = request.getParameter("unidadOrgCod");
                if (uorCod==null) uorCod="";

                if ("".equals(uorCod)) {
                    session.setAttribute("opcionSinUOR", opcion);
                    opcion = "seleccionarUOR";
                } else {
                    String uorNom = request.getParameter("unidadOrg");
                    usuario.setUnidadOrgCod(Integer.parseInt(uorCod));
                    usuario.setUnidadOrg(uorNom);
                    if(session.getAttribute("registroUsuario") != null) session.removeAttribute("registroUsuario");
                    RegistroUsuarioValueObject registroUsuarioVO =
                            new RegistroUsuarioValueObject(usuario.getDepCod(), usuario.getUnidadOrgCod(), usuario.getUnidadOrg());
                    session.setAttribute("registroUsuario", registroUsuarioVO);
                }
            }
        } else { // No hay usuario.            
            opcion = "no_usuario";
        }
        
        return (mapping.findForward(opcion));
    }
}
