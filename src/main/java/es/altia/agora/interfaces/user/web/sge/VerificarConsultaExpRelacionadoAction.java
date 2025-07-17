package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
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
 * Action que se encarga de comprobar si existe un determinado expediente relacionado y si el usuario actual tiene permiso 
 * de consulta sobre el mismo
 * @author oscar.rodriguez
 */
public class VerificarConsultaExpRelacionadoAction extends ActionSession {

    private Logger log = Logger.getLogger(VerificarConsultaExpRelacionadoAction.class);    
    protected static Config m_Conf = ConfigServiceHelper.getConfig("common");

    private void mostrar(HttpServletResponse response,String mensaje){
        
        try{
            PrintWriter out = response.getWriter();
            out.println("||---->>> " + mensaje);
        }
        catch(Exception e){
            
        }
       
    }
    public ActionForward performSession(ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws IOException, ServletException {
        
        
        //mostrar(response,"Verificarconsulta - entrada");
        log.debug("=========> VerificarConsultaExpRelacionadoAction - init <=======================");

        String codExp = request.getParameter("codExp");  // Código del exp. relacionado a una anotación del registro de entrada
        log.debug("Código expediente relacionado: " + codExp);
        //mostrar(response,"Verificarconsulta - codExp: " + codExp);
        HttpSession session = request.getSession();
        UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = usuarioVO.getParamsCon();
        log.debug("Parametros de conexión: " + params);
        //mostrar(response,"Verificarconsulta - params: " + params);
        TramitacionManager manager = TramitacionManager.getInstance();
        
        boolean permisoUnidadInicio = manager.tienePermisoSobreUnidadInicio(usuarioVO.getOrgCod(), usuarioVO.getIdUsuario(), codExp, params);
        
        if(permisoUnidadInicio)
        {
            //mostrar(response,"VerificarConsultaExpRelacionadoAction: hay permiso sobre la unidad de inicio");
            log.debug("VerificarConsultaExpRelacionadoAction: hay permiso sobre la unidad de inicio");
        }
        else{
            //mostrar(response,"VerificarConsultaExpRelacionadoAction:no hay permiso sobre la unidad de inicio");
            log.debug("VerificarConsultaExpRelacionadoAction:no hay permiso sobre la unidad de inicio");
        }
                
        boolean permisoUnidadesTramitadoras = manager.tienePermisoUnidadesTramitadoras(codExp, usuarioVO.getIdUsuario(),usuarioVO.getOrgCod(),params);
        
        if(permisoUnidadesTramitadoras)
        {
            //mostrar(response,"VerificarConsultaExpRelacionadoAction: hay permiso sobre alguna de las unidades tramitadoras");
            log.debug("VerificarConsultaExpRelacionadoAction:no hay permiso sobre alguna de las unidades tramitadoras");
        }
        else{
            //mostrar(response,"VerificarConsultaExpRelacionadoAction:no hay permiso sobre alguna de las unidades tramitadoras");
            log.debug("VerificarConsultaExpRelacionadoAction:no hay permiso sobre alguna de las unidades tramitadoras");
        }
        
        
        if(permisoUnidadesTramitadoras && permisoUnidadInicio)
        {
            // Se recupera el código de municipio del expediente necesario para pasarlo a FichaExpediente
            String codMunicipio = manager.getCodMunicipio(codExp,params);
            log.debug("Hay permiso sobre unidad de inicio y sobre alguna de las unidades tramitadoras del expediente");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("permiso=si-" + codMunicipio);            
        }
        
        log.debug("=========> VerificarConsultaExpRelacionadoAction - end  <=======================");
        return null;
    }
}