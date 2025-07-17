package es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.action;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.servicio.LocalizacionExpedienteDocumentoExternoPortafirmas;
import es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.servicio.exception.LocalizacionExpedienteDocumentoExternoPortafirmasException;
import es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.vo.DocumentoExternoPortafirmasVO;
import es.altia.flexia.portafirmas.documentos.externos.localizacion.expediente.vo.SalidaServicioLocalizacionExpedientePortafirmasVO;
import es.altia.util.commons.NumericOperations;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action que es llamado para obtener el número de expediente que puede estar asociado
 * a un determinado documento externo enviado al portafirmas. Se llama a través de una llamada AJAX
 * 
 * Invoca a los servicios web de localización de expediente para obtenerlo.
 * @author oscar
 */
public class VerificarNumeroExpedienteDocumentoExternoAction extends ActionSession{
    
    Logger log = Logger.getLogger(VerificarNumeroExpedienteDocumentoExternoAction.class);
    
    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
        
        UsuarioValueObject usuario = (UsuarioValueObject)request.getSession().getAttribute("usuario");        
        int codOrganizacion = usuario.getOrgCod();
        String codDocumento = request.getParameter("codDocumento");
        String[] params = usuario.getParamsCon();
        String mensaje = null;
        SalidaServicioLocalizacionExpedientePortafirmasVO salida = null;
        
        if(NumericOperations.isInteger(codDocumento)){
            // Si el identificador del documento es un número entero
            
            DocumentoExternoPortafirmasVO doc = new DocumentoExternoPortafirmasVO();
            doc.setCodDocumento(Integer.parseInt(codDocumento));
            doc.setCodOrganizacion(codOrganizacion);
            doc.setParams(params);
            
            try{
                salida = LocalizacionExpedienteDocumentoExternoPortafirmas.getInstance().getExpediente(doc);                
                if(salida!=null && salida.getStatus()==0 && salida.getNumExpediente()!=null){
                    mensaje = salida.getNumExpediente();
                }else
                    mensaje = "";
                
            }catch(LocalizacionExpedienteDocumentoExternoPortafirmasException e){
                log.error("Error al tratar de localizar el expediente al que pertenece un documento externo del portafirmas: " + e.getMessage());
            }
            
            
            try{
                
                response.setContentType("text/xml");
                response.setCharacterEncoding("ISO-8859-1");
                PrintWriter out = response.getWriter();
                out.print(mensaje);
                out.flush();
                out.close();
                
            }catch(Exception e){
                log.error("Se ha producido un error actualizando la tabla de unidades competenciales ", e);
            }//try-catch
            
        }
        
        return null;
    }
}
