/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.interfaces.user.web.registro.sir;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.altia.agora.business.registro.sir.service.RegistroSIRCombosService;
import es.altia.agora.business.registro.sir.service.RegistroSIRService;
import es.altia.agora.business.registro.sir.service.SIRDestinoRResService;
import es.altia.agora.business.registro.sir.vo.SirDestinoRRes;
import es.altia.agora.business.registro.sir.vo.SirDestinoRResDto;
import es.altia.agora.business.registro.sir.vo.SirUnidadDIR3;
import es.altia.agora.business.registro.sir.vo.SirUnidadDIR3Dto;
import es.altia.agora.business.registro.sir.vo.SirUnidadDIR3Filtros;
import es.altia.agora.business.registro.sir.vo.SirUnidadDIR3RespuestaBusqueda;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import es.altia.util.ajax.respuesta.RespuestaAjaxUtils;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;


/**
 *
 * @author INGDGC
 */
public class RegistroSalidaActionController extends DispatchAction{
    
    private static Logger LOG = Logger.getLogger(RegistroSalidaActionController.class);
    private final RegistroSIRService registroSIRService = new RegistroSIRService();
    private final RegistroSIRCombosService registroSIRCombosService = new RegistroSIRCombosService();
    private final SIRDestinoRResService sIRDestinoRResService = new SIRDestinoRResService();
    private final ResourceBundle confRegistro  = ResourceBundle.getBundle("Registro");
    
    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    private Gson gson = gsonBuilder.serializeNulls().create();
        

    public ActionForward getUnidadOrganicaOrigenSalidaSIR(ActionMapping mapping, ActionForm form,
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
            LOG.error("getUnidadOrganicaOrigenSalidaSIR - Error : " + e.getMessage(),e);
            respuesta.put("estadoPeticion", "-1");
            respuesta.put("descPeticion", "Error al leer los datos de la unidad de Destino. " + e.getMessage());    
        }
        RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
        return null;
    }

    public ActionForward getDatosSirUnidadDIR3SalidaDestinoByFkRRes(ActionMapping mapping, ActionForm form,
                                                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new RegistroSirGeneralActionController().getDatosSirUnidadDIR3DestinoByFkRRes(mapping,form,request,response);
    }

}
