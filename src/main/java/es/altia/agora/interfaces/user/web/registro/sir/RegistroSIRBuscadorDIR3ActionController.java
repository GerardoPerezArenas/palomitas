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
import es.altia.agora.business.registro.sir.vo.*;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import es.altia.util.ajax.respuesta.RespuestaAjaxUtils;
import es.altia.util.conexion.AdaptadorSQLBD;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.*;


/**
 *
 * @author INGDGC
 */
public class RegistroSIRBuscadorDIR3ActionController extends DispatchAction{
    
    private static Logger LOG = Logger.getLogger(RegistroSIRBuscadorDIR3ActionController.class);
    private final RegistroSIRService registroSIRService = new RegistroSIRService();
    private final RegistroSIRCombosService registroSIRCombosService = new RegistroSIRCombosService();
    private final SIRDestinoRResService sIRDestinoRResService = new SIRDestinoRResService();
    private final ResourceBundle confRegistro  = ResourceBundle.getBundle("Registro");
    
    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    private Gson gson = gsonBuilder.serializeNulls().create();
        
    public ActionForward getListaUnidadOrganicaDestinoSIR(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> respuesta = new HashMap<String,Object>();
        SirUnidadDIR3RespuestaBusqueda sirUnidadDIR3RespuestaBusqueda = new SirUnidadDIR3RespuestaBusqueda();
        RegistroSirActionControllerValidator actionControllerValidator = new RegistroSirActionControllerValidator(request);
        List<SirUnidadDIR3Dto>listaUnidadesDIR3= new ArrayList<SirUnidadDIR3Dto>();
        SirUnidadDIR3Filtros sirUnidadDIR3Filtros = new SirUnidadDIR3Filtros();
        sirUnidadDIR3Filtros.setIdioma(actionControllerValidator.getUsuarioValueObject().getIdioma());
        if(actionControllerValidator.existeDatosSessionUsuario()){
            String _sirUnidadDIR3Filtros = request.getParameter("_sirUnidadDIR3Filtros");
            if(_sirUnidadDIR3Filtros!=null && !_sirUnidadDIR3Filtros.isEmpty() && !_sirUnidadDIR3Filtros.equalsIgnoreCase("null")){
                sirUnidadDIR3Filtros =  (SirUnidadDIR3Filtros)gson.fromJson(_sirUnidadDIR3Filtros, SirUnidadDIR3Filtros.class);
            }
                
            AdaptadorSQLBD adaptadorSQLBD = new AdaptadorSQLBD(actionControllerValidator.getUsuarioValueObject().getParamsCon());
            listaUnidadesDIR3=registroSIRService.getAllSirUnidadDIR3ByFilter(sirUnidadDIR3Filtros, adaptadorSQLBD);
            
            sirUnidadDIR3RespuestaBusqueda.setClasificarYPaginar(sirUnidadDIR3Filtros.isClasificarYPaginar());
            sirUnidadDIR3RespuestaBusqueda.setListaUnidadesDIR3(listaUnidadesDIR3);
            sirUnidadDIR3RespuestaBusqueda.setNumPaginaRecuperar(sirUnidadDIR3Filtros.getNumPaginaRecuperar());
            sirUnidadDIR3RespuestaBusqueda.setNumResultadosPorPagina(sirUnidadDIR3Filtros.getNumResultadosPorPagina());
            sirUnidadDIR3RespuestaBusqueda.setNumResultadosTotal(listaUnidadesDIR3!=null && listaUnidadesDIR3.size()>0 ? listaUnidadesDIR3.get(0).getNumResultadosTotal() : 0);
            sirUnidadDIR3RespuestaBusqueda.setNumPaginasTotal(registroSIRService.calcularNumeroPaginasTotalesPaginador(sirUnidadDIR3RespuestaBusqueda.getNumResultadosTotal(),sirUnidadDIR3Filtros.getNumResultadosPorPagina()));
            sirUnidadDIR3RespuestaBusqueda.setSirUnidadDIR3Filtros(sirUnidadDIR3Filtros);
            sirUnidadDIR3RespuestaBusqueda.setTotalUnidadesDIR3Sistema(registroSIRService.getTotalSirUnidadDIR3Registradas(adaptadorSQLBD));

            respuesta.put("estadoPeticion", "0");
            respuesta.put("response", sirUnidadDIR3RespuestaBusqueda);
        }else{
            log.error("No hemos recibido Datos Usuario o Session en la request, no podemos obtener los parametros de conexion ... ");
            respuesta.put("estadoPeticion", "-1");
            respuesta.put("descPeticion", "No hemos recibido Datos Usuario o Session en la request, no podemos obtener los parametros de conexion");
        }
        Type gsonType = new TypeToken<HashMap>() {}.getType();
        RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta,gsonType), response);
        return null;
    }

    public ActionForward getProvinciasPorComunidadAutonoma(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> respuesta = new HashMap<String,Object>();
        RegistroSirActionControllerValidator actionControllerValidator = new RegistroSirActionControllerValidator(request);
        try {
            if (actionControllerValidator.existeDatosSessionUsuario()) {
                    AdaptadorSQLBD adaptadorSQLBD = new AdaptadorSQLBD(actionControllerValidator.getUsuarioValueObject().getParamsCon());
                    String codigoComunidadAutonoma = request.getParameter("codigoComunidadAutonoma");
                    log.info("codigoComunidadAutonoma : " + codigoComunidadAutonoma);
                    if (codigoComunidadAutonoma != null && !codigoComunidadAutonoma.isEmpty()) {
                        List<GeneralComboVO> listaProvinciasXCA = registroSIRCombosService.getProvinciasXComunidadGeneralCombo(codigoComunidadAutonoma, adaptadorSQLBD);
                        respuesta.put("estadoPeticion","0");
                        respuesta.put("response",listaProvinciasXCA);
                    } else {
                        log.error("Codigo comunindad Autonoma no recibido ");
                        respuesta.put("estadoPeticion", "-1");
                        respuesta.put("descPeticion", "Codigo de comunidad autonoma no recibido");
                    }
            } else {
                log.error("No hemos recibido SESSION en a request, no podemos obtener los parametros de conexion ... ");
                respuesta.put("estadoPeticion", "-1");
                respuesta.put("descPeticion", "No hemos recibido SESSION en la request, no podemos obtener los parametros de conexion");
            }
        } catch (Exception e) {
            LOG.error("getProvinciasPorComunidadAutonoma - Error : " + e.getMessage(),e);
            respuesta.put("estadoPeticion", "-1");
            respuesta.put("descPeticion", "Error al leer los datos de la unidad de Destino. " + e.getMessage());    
        }
        RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
        return null;
    }
    
    public ActionForward getListaComboComunidadAutonoma(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> respuesta = new HashMap<String,Object>();
        RegistroSirActionControllerValidator actionControllerValidator = new RegistroSirActionControllerValidator(request);
        try {
            if (actionControllerValidator.existeDatosSessionUsuario()) {
                    AdaptadorSQLBD adaptadorSQLBD = new AdaptadorSQLBD(actionControllerValidator.getUsuarioValueObject().getParamsCon());
                    List<GeneralComboVO> listaComunidadAutonoma = registroSIRCombosService.getListaComboComunidadAutonoma(adaptadorSQLBD);
                    respuesta.put("estadoPeticion","0");
                    respuesta.put("response",listaComunidadAutonoma);
            } else {
                log.error("No hemos recibido SESSION en a request, no podemos obtener los parametros de conexion ... ");
                respuesta.put("estadoPeticion", "-1");
                respuesta.put("descPeticion", "No hemos recibido SESSION en la request, no podemos obtener los parametros de conexion");
            }
        } catch (Exception e) {
            LOG.error("getListaComboComunidadAutonoma - Error : " + e.getMessage(),e);
            respuesta.put("estadoPeticion", "-1");
            respuesta.put("descPeticion", "Error al leer los datos de combo comunidad autonoma. " + e.getMessage());    
        }
        RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
        return null;
    }
    
    public ActionForward getListaComboNivelAdministrativo(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> respuesta = new HashMap<String,Object>();
        RegistroSirActionControllerValidator actionControllerValidator = new RegistroSirActionControllerValidator(request);
        try {
            if (actionControllerValidator.existeDatosSessionUsuario()) {
                    AdaptadorSQLBD adaptadorSQLBD = new AdaptadorSQLBD(actionControllerValidator.getUsuarioValueObject().getParamsCon());
                    List<GeneralComboVO> listaNivelAdministrativo = registroSIRCombosService.getListaComboNivelAdministrativo(actionControllerValidator.getUsuarioValueObject().getIdUsuario(),adaptadorSQLBD);
                    respuesta.put("estadoPeticion","0");
                    respuesta.put("response",listaNivelAdministrativo);
            } else {
                log.error("No hemos recibido SESSION en a request, no podemos obtener los parametros de conexion ... ");
                respuesta.put("estadoPeticion", "-1");
                respuesta.put("descPeticion", "No hemos recibido SESSION en la request, no podemos obtener los parametros de conexion");
            }
        } catch (Exception e) {
            LOG.error("getListaComboNivelAdministrativo - Error : " + e.getMessage(),e);
            respuesta.put("estadoPeticion", "-1");
            respuesta.put("descPeticion", "Error al leer los datos de combo comunidad autonoma. " + e.getMessage());    
        }
        RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
        return null;
    }
    
    public ActionForward getListaComboOrganismos(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> respuesta = new HashMap<String,Object>();
        RegistroSirActionControllerValidator actionControllerValidator = new RegistroSirActionControllerValidator(request);
        try {
            if (actionControllerValidator.existeDatosSessionUsuario()) {
                    AdaptadorSQLBD adaptadorSQLBD = new AdaptadorSQLBD(actionControllerValidator.getUsuarioValueObject().getParamsCon());
                    List<GeneralComboVO> listaOrganismos = registroSIRCombosService.getListaComboOrganismos(actionControllerValidator.getUsuarioValueObject().getIdUsuario(),adaptadorSQLBD);
                    respuesta.put("estadoPeticion","0");
                    respuesta.put("response",listaOrganismos);
            } else {
                log.error("No hemos recibido SESSION en a request, no podemos obtener los parametros de conexion ... ");
                respuesta.put("estadoPeticion", "-1");
                respuesta.put("descPeticion", "No hemos recibido SESSION en la request, no podemos obtener los parametros de conexion");
            }
        } catch (Exception e) {
            LOG.error("getListaComboOrganismos - Error : " + e.getMessage(),e);
            respuesta.put("estadoPeticion", "-1");
            respuesta.put("descPeticion", "Error al leer los datos de combo comunidad autonoma. " + e.getMessage());    
        }
        RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
        return null;
    }

}
