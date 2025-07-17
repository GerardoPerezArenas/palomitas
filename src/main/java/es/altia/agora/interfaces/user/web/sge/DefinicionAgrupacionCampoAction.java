package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.DefinicionAgrupacionCamposValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author david.caamano
 * @version 14/03/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 14/03/2013 * #53275 Edición inicial</li>
 * </ol> 
 */
public class DefinicionAgrupacionCampoAction extends ActionSession {

    private final static Logger log = Logger.getLogger(DefinicionCampoAction.class);
    
    @Override
    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
    HttpServletResponse response) throws IOException, ServletException {
        if(log.isDebugEnabled()) log.debug("performSession() : BEGIN");
        HttpSession session = request.getSession();
        String opcion ="";
        DefinicionAgrupacionCamposValueObject agrupacionVO = new DefinicionAgrupacionCamposValueObject();
        DefinicionAgrupacionCampoForm defAgrupacionCampoForm = (DefinicionAgrupacionCampoForm) form;
        if(log.isDebugEnabled()) log.debug("Comprobamos si existe un usuario autenticado");
        if ((session.getAttribute("usuario") != null)) {
            if(log.isDebugEnabled()) log.debug("Existe un usuario autenticado");
            UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();
            int cod_dep;
            int cod_uni;
            cod_dep= usuario.getDepCod();
            cod_uni= usuario.getUnidadOrgCod();
            
            opcion = request.getParameter("opcion");
            if(log.isDebugEnabled()) log.debug("opcion = " + opcion);
            
            if (opcion.equals("inicio")){
                String modoInicio = request.getParameter("nCS");
                String lectura = request.getParameter("lectura");
                if("modificarDesdeProcedimiento".equals(modoInicio) || "modificarDesdeTramite".equals(modoInicio)) {
                    String codAgrupacion = request.getParameter("codAgrupacion");
                    String descAgrupacion = request.getParameter("descAgrupacion");
                    String ordenAgrupacion = request.getParameter("ordenAgrupacion");
                    
                    agrupacionVO.setCodAgrupacion(codAgrupacion);
                    agrupacionVO.setDescAgrupacion(descAgrupacion);
                    agrupacionVO.setOrdenAgrupacion(Integer.valueOf(ordenAgrupacion));
                }//if("modificarDesdeProcedimiento".equals(modoInicio) || "modificarDesdeTramite".equals(modoInicio)) 
                
                session.setAttribute("modoInicio",modoInicio);
                if(lectura != null && !lectura.equals("")) {
                    session.setAttribute("lectura",lectura);
                } else {
                    session.setAttribute("lectura","no");
                }//if(lectura != null && !lectura.equals(""))
                defAgrupacionCampoForm.setAgrupacionVO(agrupacionVO);
            }//if opcion
        }else{
            if(log.isDebugEnabled()) log.debug("MantAnotacionRegistroAction --> no hay usuario");
            opcion = "no_usuario";
        }
        if(log.isDebugEnabled()) log.debug("performSession() : END");
        return (mapping.findForward(opcion));
    }//performSession
    
}//class
