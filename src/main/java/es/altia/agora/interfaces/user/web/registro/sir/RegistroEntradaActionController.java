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
public class RegistroEntradaActionController extends DispatchAction{
    
    private static Logger LOG = Logger.getLogger(RegistroEntradaActionController.class);
    private final RegistroSIRService registroSIRService = new RegistroSIRService();
    private final RegistroSIRCombosService registroSIRCombosService = new RegistroSIRCombosService();
    private final SIRDestinoRResService sIRDestinoRResService = new SIRDestinoRResService();
    private final ResourceBundle confRegistro  = ResourceBundle.getBundle("Registro");
    
    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    private Gson gson = gsonBuilder.serializeNulls().create();
        

    public ActionForward getUnidadOrganicaOrigenEntradaSIR(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> respuesta = new HashMap<String,Object>();
        RegistroSirActionControllerValidator actionControllerValidator = new RegistroSirActionControllerValidator(request);
        try {
            if (actionControllerValidator.existeDatosSessionUsuario()) {
                    AdaptadorSQLBD adaptadorSQLBD = new AdaptadorSQLBD(actionControllerValidator.getUsuarioValueObject().getParamsCon());
                    String unidadDir3origenProperties = confRegistro.getString("UNIDAD_ORIGEN_SIR_CODIGO_DIR3");
                    log.info("unidadDir3origenProperties : " + unidadDir3origenProperties);
                    if (unidadDir3origenProperties != null && !unidadDir3origenProperties.isEmpty()) {
                        SirUnidadDIR3 unidadDIR3Origen = registroSIRService.getUnidadDir3ByCodigo(unidadDir3origenProperties, adaptadorSQLBD);
                        respuesta.put("estadoPeticion","0");
                        respuesta.put("response",unidadDIR3Origen);
                    } else {
                        log.error("Unidad origen no configurada Properties Registro.properties ");
                        respuesta.put("estadoPeticion", "-1");
                        respuesta.put("descPeticion", "Unidad Origen no configurada Properties Registro.properties");
                    }
            } else {
                log.error("No hemos recibido SESSION en a request, no podemos obtener los parametros de conexion ... ");
                respuesta.put("estadoPeticion", "-1");
                respuesta.put("descPeticion", "No hemos recibido SESSION en la request, no podemos obtener los parametros de conexion");
            }
        } catch (Exception e) {
            LOG.error("getUnidadOrganicaOrigenEntradaSIR - Error : " + e.getMessage(),e);
            respuesta.put("estadoPeticion", "-1");
            respuesta.put("descPeticion", "Error al leer los datos de la unidad de Destino. " + e.getMessage());    
        }
        RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
        return null;
    }

    public ActionForward getDatosSirUnidadDIR3EntradaDestinoByFkRRes(ActionMapping mapping, ActionForm form,
                                                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new RegistroSirGeneralActionController().getDatosSirUnidadDIR3DestinoByFkRRes(mapping,form,request,response);
    }

    public ActionForward getCodigoAsuntoEntradasSIR(ActionMapping mapping, ActionForm form,
                                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> respuesta = new HashMap<String,Object>();
        RegistroSirActionControllerValidator actionControllerValidator = new RegistroSirActionControllerValidator(request);
        try {
            if (actionControllerValidator.existeDatosSessionUsuario()) {
                AdaptadorSQLBD adaptadorSQLBD = new AdaptadorSQLBD(actionControllerValidator.getUsuarioValueObject().getParamsCon());
                String codigoAsuntoEntradasSir = confRegistro.getString("CODIGO_ASUNTO_ENTRADAS_SIR");
                log.info("codigoAsuntoEntradasSir: " + codigoAsuntoEntradasSir);
                if (codigoAsuntoEntradasSir != null && !codigoAsuntoEntradasSir.isEmpty()) {
                    respuesta.put("estadoPeticion","0");
                    respuesta.put("response",codigoAsuntoEntradasSir);
                } else {
                    log.error("Asunto para entradas SIR no configurado  Properties Registro.properties ");
                    respuesta.put("estadoPeticion", "-1");
                    respuesta.put("descPeticion", "Asunto Entradas SIR no configurado Properties Registro.properties");
                }
            } else {
                log.error("No hemos recibido SESSION en a request, no podemos obtener los parametros de conexion ... ");
                respuesta.put("estadoPeticion", "-1");
                respuesta.put("descPeticion", "No hemos recibido SESSION en la request, no podemos obtener los parametros de conexion");
            }
        } catch (Exception e) {
            LOG.error("getCodigoAsuntoEntradasSIR - Error : " + e.getMessage(),e);
            respuesta.put("estadoPeticion", "-1");
            respuesta.put("descPeticion", "Error al leer los datos del Asunto Entradas SIR. " + e.getMessage());
        }
        RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
        return null;
    }

}
