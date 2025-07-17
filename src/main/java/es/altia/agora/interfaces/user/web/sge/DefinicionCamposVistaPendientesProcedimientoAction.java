package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.CampoListadoPendientesProcedimientoVO;
import es.altia.agora.business.sge.persistence.CamposListadoPendientesProcedimientoManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class DefinicionCamposVistaPendientesProcedimientoAction extends ActionSession{

    private final String ENTRADA = "entrada";
    private Logger log = Logger.getLogger(DefinicionCamposVistaPendientesProcedimientoAction.class);
    
    public ActionForward performSession(	ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

        String opcion = request.getParameter("opcion");
        String codProcedimiento = request.getParameter("codProcedimiento");
        String codMunicipio     = request.getParameter("codMunicipio");
        log.debug("DefinicionCamposVistaPendientesProcedimientoAction ================================>");
        log.debug("El parámetro opción es : " + opcion);
        log.debug("El parámetro codProcedimiento es : " + codProcedimiento);
        log.debug("El parámetro codMunicipio es : " + codMunicipio);

        HttpSession session        = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        String[] params            = usuario.getParamsCon();

        if(ENTRADA.equals(opcion)){

            // Se recupera la lista de campos disponibles
            ArrayList<CampoListadoPendientesProcedimientoVO> camposSeleccionados = 
                    CamposListadoPendientesProcedimientoManager.getInstance().getCamposDisponibles(codProcedimiento,codMunicipio, params);
            log.debug("Se han recuperados " + camposSeleccionados.size() + " campos disponibles ");
            request.setAttribute("lista_campos_pendientes_disponibles",camposSeleccionados);
            
        }

        log.debug("<================================ DefinicionCamposVistaPendientesProcedimientoAction");
        return mapping.findForward(opcion);
    }
}