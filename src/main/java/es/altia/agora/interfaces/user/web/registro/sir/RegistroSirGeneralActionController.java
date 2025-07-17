/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.interfaces.user.web.registro.sir;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.registro.sir.service.RegistroSIRCombosService;
import es.altia.agora.business.registro.sir.service.RegistroSIRService;
import es.altia.agora.business.registro.sir.service.SIRDestinoRResService;
import es.altia.agora.business.registro.sir.vo.SirDestinoRRes;
import es.altia.agora.business.registro.sir.vo.SirDestinoRResDto;
import es.altia.agora.business.registro.sir.vo.SirUnidadDIR3;
import es.altia.util.ajax.respuesta.RespuestaAjaxUtils;
import es.altia.util.conexion.AdaptadorSQLBD;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


/**
 *
 * @author INGDGC
 */
public class RegistroSirGeneralActionController extends DispatchAction{
    
    private static Logger LOG = Logger.getLogger(RegistroSirGeneralActionController.class);
    private final RegistroSIRService registroSIRService = new RegistroSIRService();
    private final RegistroSIRCombosService registroSIRCombosService = new RegistroSIRCombosService();
    private final SIRDestinoRResService sIRDestinoRResService = new SIRDestinoRResService();
    private final ResourceBundle confRegistro  = ResourceBundle.getBundle("Registro");
    
    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    private Gson gson = gsonBuilder.serializeNulls().create();


    public ActionForward getDatosSirUnidadDIR3DestinoByFkRRes(ActionMapping mapping, ActionForm form,
                                                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> respuesta = new HashMap<String,Object>();
        RegistroSirActionControllerValidator actionControllerValidator = new RegistroSirActionControllerValidator(request);
        try {
            if (actionControllerValidator.existeDatosSessionUsuario()) {
                AdaptadorSQLBD adaptadorSQLBD = new AdaptadorSQLBD(actionControllerValidator.getUsuarioValueObject().getParamsCon());
                SirDestinoRRes sirDestinoRRes = new SirDestinoRRes();
                int codigoDepartamentoPKRegistro = (request.getParameter("res_dep") != null && !request.getParameter("res_dep").isEmpty() && !request.getParameter("res_dep").equalsIgnoreCase("null") ? Integer.parseInt(request.getParameter("res_dep")):0);
                int codigoUnidadOrganicaPKRegistro = (request.getParameter("res_uor") != null && !request.getParameter("res_uor").isEmpty() && !request.getParameter("res_uor").equalsIgnoreCase("null") ? Integer.parseInt(request.getParameter("res_uor")):0);
                String tipoAnotacionRegistro = (request.getParameter("res_tip")!=null && !request.getParameter("res_tip").isEmpty() && !request.getParameter("res_tip").equalsIgnoreCase("null") ? request.getParameter("res_tip"):"S") ;
                int ano = (request.getParameter("res_eje") != null && !request.getParameter("res_eje").isEmpty() && !request.getParameter("res_eje").equalsIgnoreCase("null") ? Integer.parseInt(request.getParameter("res_eje")):0);
                int numero = (request.getParameter("res_num") != null && !request.getParameter("res_num").isEmpty() && !request.getParameter("res_num").equalsIgnoreCase("null") ? Integer.parseInt(request.getParameter("res_num")):0);
                sirDestinoRRes.setRES_DEP(codigoDepartamentoPKRegistro);
                sirDestinoRRes.setRES_UOR(codigoUnidadOrganicaPKRegistro);
                sirDestinoRRes.setRES_TIP(tipoAnotacionRegistro);
                sirDestinoRRes.setRES_EJE(ano);
                sirDestinoRRes.setRES_NUM(numero);
                SirDestinoRResDto sirDestinoRResData = sIRDestinoRResService.getSirDestinoRResByPKRRes(actionControllerValidator.getUsuarioValueObject().getIdioma()
                        ,sirDestinoRRes, adaptadorSQLBD);
                respuesta.put("estadoPeticion","0");
                respuesta.put("response",sirDestinoRResData);
            } else {
                log.error("No hemos recibido SESSION en a request, no podemos obtener los parametros de conexion ... ");
                respuesta.put("estadoPeticion", "-1");
                respuesta.put("descPeticion", "No hemos recibido SESSION en la request, no podemos obtener los parametros de conexion");
            }
        } catch (Exception e) {
            LOG.error("getDatosSirUnidadDIR3DestinoByFkRRes - Error : " + e.getMessage(),e);
            respuesta.put("estadoPeticion", "-1");
            respuesta.put("descPeticion", "Error al leer los datos de Unidad DIR3 Destino " + e.getMessage());
        }
        RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
        return null;
    }

}
