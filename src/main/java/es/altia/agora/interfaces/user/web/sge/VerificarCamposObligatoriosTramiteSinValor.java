package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.TramitacionExpedientesManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.BDException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author oscar
 */
public class VerificarCamposObligatoriosTramiteSinValor extends ActionSession {

    private Logger log = Logger.getLogger(VerificarCamposObligatoriosTramiteSinValor.class);
    
    
    public ActionForward performSession(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        
        int codMunicipio      = 0;
        String numExpediente     = request.getParameter("numExpediente");
        String codTramite        = request.getParameter("codTramite");
        String ocurrenciaTramite = request.getParameter("ocurrenciaTramite");
        String[] params = null;
        boolean exito = false;
        int salida = -1;
        
        log.debug("numExpediente: " + numExpediente + ",codTramite: " + codTramite + ",ocurrenciaTramite: " + ocurrenciaTramite);
        
        if(numExpediente!=null && codTramite!=null && ocurrenciaTramite!=null){
            
            HttpSession session = request.getSession();
            if(session!=null){            
                UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
                params = usuario.getParamsCon();
                codMunicipio = usuario.getOrgCod();

            }

            try{

                TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
                String[] datos = numExpediente.split("/");
                
                tEVO.setEjercicio(datos[0]);
                tEVO.setCodProcedimiento(datos[1]);
                tEVO.setNumeroExpediente(numExpediente);
                tEVO.setCodTramite(codTramite);
                tEVO.setOcurrenciaTramite(ocurrenciaTramite);
                tEVO.setCodMunicipio(Integer.toString(codMunicipio));
                
                
                // Rellenar los atributos necesarios del objeto tEVO con el núm expedinete, cod tramite y demás.
                if(TramitacionExpedientesManager.getInstance().tieneTramiteCamposObligatoriosSinValor(tEVO, params))
                    salida = 0;
                else 
                    salida = -1;
                
            }catch(TechnicalException e){
                log.error(e.getMessage());
                salida = 1;
            }catch(BDException e){
                log.error(e.getMessage());
                salida = 2;
            }
        }
        
        
        try{
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            
            /** salida: 0 --> OK
                        1 --> Error al comprobar si el trámite tiene o no campos obligatorios sin valor
                        2 --> Error al obtener conexión a la BBDD
                       -1 --> Error técnico
             **/
            out.println(salida);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        
        
        return null;
    }
}
