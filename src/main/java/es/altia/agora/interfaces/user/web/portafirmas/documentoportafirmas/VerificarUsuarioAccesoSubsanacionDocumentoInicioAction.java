package es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.FichaExpedienteManager;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.struts.DefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.io.PrintWriter;


public class VerificarUsuarioAccesoSubsanacionDocumentoInicioAction  extends DefaultAction {



    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {

        /* Aceptamos la subsanación, por lo que eliminamos de la sesion el parametro de valor seleccionado en caso de cancelar */
        SessionManager.removeSelectedIndex(request);

        /* Save Signature */
        int idUsuarioAutenticado  = SessionManager.getAuthenticatedUser(request).getIdUsuario();
                
        UsuarioValueObject usuarioVO = (UsuarioValueObject)request.getSession().getAttribute("usuario");
        String[] params = usuarioVO.getParamsCon();
        
                
        String codProcedimiento = request.getParameter("codProcedimiento");
        String numDocumento     = request.getParameter("numDocumento");
        String numExpediente    = request.getParameter("numExpediente");
        
        if(codProcedimiento!=null && numDocumento!=null && numExpediente!=null){
            _log.debug("VerificarUsuarioAccesoSubsanacionDocumentoInicioAction codProcedimiento: " + codProcedimiento);
            _log.debug("VerificarUsuarioAccesoSubsanacionDocumentoInicioAction numDocumento: " + numDocumento);

            boolean exito = FichaExpedienteManager.getInstance().verificarSubsanacionDocumento(numExpediente,codProcedimiento,numDocumento,usuarioVO,params);

            PrintWriter out = response.getWriter();
            response.setContentType("text/xml");
            if(exito)
                out.print("autorizado=1");
            else 
                out.print("autorizado=0");
            out.flush();
            out.close();
            
        }       
        
        return null;
    }//doPerform
    
   
      
    

    protected String getMainPageMapping() {
        return GlobalNames.MAINPAGE_GLOBAL_FORWARD;
    }

    protected String getInternalErrorMapping() {
        return GlobalNames.INTERNALERROR_GLOBAL_FORWARD;
    }

    protected String getModelErrorMapping() {
        return GlobalNames.MODELERROR_GLOBAL_FORWARD;
    }
    
   
    
}
