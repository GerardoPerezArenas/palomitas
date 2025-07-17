package es.altia.agora.interfaces.user.web.portafirmas.viafirma;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.viafirma.cliente.ViafirmaClientServlet;
import org.viafirma.cliente.exception.CodigoError;
import org.viafirma.cliente.vo.FirmaInfoViafirma;
import org.viafirma.cliente.vo.UsuarioGenericoViafirma;

/**
 * @author david.caamano
 * @version 28/11/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 28-11-2012 * #97301 Edición inicial</li>
 * </ol> 
 */
public class ViaFirmaServlet extends ViafirmaClientServlet {
    
    //Logger
    protected static Log log = LogFactory.getLog(ViaFirmaServlet.class);

    @Override
    public void authenticateOK(UsuarioGenericoViafirma ugv, HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet.");
    }//authenticateOK

    @Override
    public void signOK(FirmaInfoViafirma firmaInfo, HttpServletRequest request, HttpServletResponse response) {
        if(log.isDebugEnabled()) log.debug("signOK() : BEGIN");
        request.setAttribute("firma", firmaInfo);
        try{
            if(log.isDebugEnabled()) log.debug("Hacemos el forward al iFrame de via firma");
            request.getRequestDispatcher("/jsp/portafirmas/resources/viafirma/iFrameViaFirma.jsp?op=").forward(request, response);
        }catch (Exception e) {
            log.error("ViafirmaClientInternetServlet().." + e.getMessage(),e);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("signOK() : END");
    }//signOK

    @Override
    public void error(CodigoError codError, HttpServletRequest request, HttpServletResponse response) {
        if(log.isDebugEnabled()) log.debug("error() : BEGIN");
        try{
            //Gestión de error al autenticar o firmar
            String op = (String)request.getParameter("op") ;
            if(op!= null && op.equals("validar")){
                //por aqui se pasaria si la operación fuera la de loguear el usuario con viaFirma pero en Flexia no se implanta el login
                //Si se quisiera habria que realizar un forward a la pagina de login correspondiente y recoger el codigo de error para
                //mostrarlo en la pantalla.
            }else{
                request.setAttribute("codError", codError);
                request.getRequestDispatcher("/jsp/portafirmas/resources/viafirma/iFrameViaFirma.jsp").forward(request, response);
            }//if(op!= null && op.equals("validar"))
        }catch (Exception e) {
            log.error("ViaFirmaClienteServlet().." + e.getMessage(),e);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("error() : END");
    }//error

    @Override
    public void cancel(HttpServletRequest request, HttpServletResponse response) {
        if(log.isDebugEnabled()) log.debug("cancel() : BEGIN");
        try{
            String op = (String)request.getParameter("op");
            if (op!= null && op.equals("validar")){
                //por aqui se pasaria si la operación fuera la de loguear el usuario con viaFirma pero en Flexia no se implanta el login
                //Si se quisiera habria que realizar un forward a la pagina de login correspondiente.
            }else{
                request.setAttribute("cancel", "Operación de firma cancelada");
                request.getRequestDispatcher("/jsp/portafirmas/resources/viafirma/iFrameViaFirma.jsp?op=").forward(request, response);
            }//if (op!= null && op.equals("validar"))
        }catch (Exception e) {
            log.error("ViaFirmaClienteServlet().." + e.getMessage(),e);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("cancel() : END");
    }//cancel
    
}//class
