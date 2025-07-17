package es.altia.agora.interfaces.user.web.sge;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.firma.FirmaFlujoManager;
import es.altia.agora.business.sge.firma.vo.FirmaCircuitoVO;
import es.altia.agora.business.sge.firma.vo.FirmaDocumentoTramiteClave;
import es.altia.agora.business.sge.firma.vo.FirmaFirmanteVO;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoUsuariosVO;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoVO;
import es.altia.agora.business.sge.firma.vo.FirmaTipoVO;
import es.altia.agora.business.sge.firma.vo.FirmaUsuarioVO;
import es.altia.agora.interfaces.user.web.util.ResultadoAjax;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesAjax;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.portafirmasexternocliente.plugin.persistance.PluginPortafirmasExternoClienteManager;
import es.altia.util.commons.WebOperations;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * Action que actúa de controlador y que se encarga de manejar las peticiones
 * que tengan que ver con la definicion de flujos y circuitos de firma
 *
 * @author oscar
 */
public class DefinicionFlujosFirmaAction extends DispatchAction {

    private static final Logger logger = Logger.getLogger(DefinicionFlujosFirmaAction.class);

    /**
     * Carga la pantalla de definicion de flujos de firma.
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward cargarPantallaDefinicionFlujosFirma(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - cargarPantallaDefinicionFlujosFirma");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = null;

        List<FirmaTipoVO> listaFirmaTipo = null;
        List<FirmaFlujoVO> listaFirmaFlujo = null;

        try {
            if (usuario != null) {
                params = usuario.getParamsCon();

                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();

                //Obtenemos el listado de tipos de flujo
                listaFirmaTipo = firmaFlujoManager.getListaTipoFirmas(params);

                //Obtenemos el listado los flujos de firma disponibles
                listaFirmaFlujo = firmaFlujoManager.getListaFlujosFirma(params);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        request.setAttribute("listaFirmaTipo", listaFirmaTipo);
        request.setAttribute("listaFirmaFlujo", listaFirmaFlujo);

        logger.debug("FIN - cargarPantallaDefinicionFlujosFirma");

        return mapping.findForward("cargarPantallaDefinicionFlujosFirma");
    }

    /**
     * Carga la pantalla de definicion de circuitos de firma.
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward cargarPantallaDefinicionCircuitosFirma(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - cargarPantallaDefinicionCircuitosFirma");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = null;

        Integer idFlujoFirma = null;
        List<FirmaCircuitoVO> listaCircuitoFlujo = null;

        try {
            if (usuario != null) {
                params = usuario.getParamsCon();

                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();

                //Obtenemos el listado del circuito de firma del flujo
                idFlujoFirma = NumberUtils.createInteger(request.getParameter("codigoFlujo"));
                listaCircuitoFlujo = firmaFlujoManager.getListaCircuitoFirmasByIdFlujo(idFlujoFirma, params);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        request.setAttribute("listaCircuitoFlujo", listaCircuitoFlujo);
        request.setAttribute("idFlujoFirma", idFlujoFirma);

        logger.debug("FIN - cargarPantallaDefinicionCircuitosFirma");

        return mapping.findForward("cargarPantallaDefinicionCircuitosFirma");
    }

    /**
     * Carga la pantalla de eleccion de usuarios de firma
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward cargarPantallaEleccionUsuarioFirmante(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - cargarPantallaEleccionUsuarioFirmante");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = null;
        
        List<FirmaUsuarioVO> listaUsuariosFirma = null;
        
        try {
            if (usuario != null) {
                
                //Se obtienen los tipos de procedimientos del fichero properties Portafirmas.properties
                ResourceBundle expPortafirmas = ResourceBundle.getBundle("Portafirmas");
                 String propiedad = usuario.getOrgCod()+"/Portafirmas";
                String portafirmas = expPortafirmas.getString(propiedad);
                
                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();
                params = usuario.getParamsCon();

                //Obtenemos el listado los flujos de firma disponibles
                String codOrganizacion = Integer.toString(usuario.getOrgCod());
                listaUsuariosFirma = firmaFlujoManager.getListaUsuariosFirmaDisponibles(
                        codOrganizacion, portafirmas, params);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        request.setAttribute("listaUsuariosFirma", listaUsuariosFirma);
        
        logger.debug("FIN - cargarPantallaEleccionUsuarioFirmante");

        return mapping.findForward("cargarPantallaEleccionUsuarioFirmante");
    }

    /**
     * Carga la pantalla de eleccion de flujo
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward cargarPantallaEleccionFlujoFirma(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - cargarPantallaEleccionFlujoFirma");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = null;

        List<FirmaFlujoUsuariosVO> listaFlujoUsuariosFirma = null;
        
        try {
            if (usuario != null) {
                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();
                params = usuario.getParamsCon();

                //Obtenemos el listado los flujos de firma disponibles
                listaFlujoUsuariosFirma = firmaFlujoManager.getListaFlujosFirmaConUsuarios(Boolean.TRUE, params);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        request.setAttribute("listaFlujoUsuariosFirma", listaFlujoUsuariosFirma);
        
        logger.debug("FIN - cargarPantallaEleccionFlujoFirma");

        return mapping.findForward("cargarPantallaEleccionFlujoFirma");
    }
    
    /**
     * Carga el listado de flujos de firma
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward cargarDatosFlujosFirma(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - cargarDatosFlujosFirma");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        TraductorAplicacionBean traductor = getTraductor(usuario);
        String[] params = null;

        List<FirmaFlujoVO> listaFirmaFlujos = null;
        ResultadoAjax<List<FirmaFlujoVO>> respuesta = new ResultadoAjax<List<FirmaFlujoVO>>();
        respuesta.setStatus(ConstantesAjax.STATUS_AJAX_OK);
        respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_OK));

        try {
            if (usuario != null) {
                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();
                params = usuario.getParamsCon();

                //Obtenemos el listado de flujos de firma
                listaFirmaFlujos = firmaFlujoManager.getListaFlujosFirma(params);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_INTERNO);
            respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_ERROR_INTERNO));
        }

        logger.debug("FIN - cargarDatosFlujosFirma");

        respuesta.setResultado(listaFirmaFlujos);
        WebOperations.retornarJSON(new Gson().toJson(respuesta), response);

        return null;
    }

    /**
     * Carga la pantalla de visualizacion de los estados de un circuito de firmas
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward cargarPantallaVerEstadoCircuitoFirmas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - cargarPantallaVerEstadoCircuitoFirmas");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = null;

        List<FirmaFirmanteVO> listaEstadoFirmas = new ArrayList<FirmaFirmanteVO>();
        FirmaDocumentoTramiteClave clave = null;
        
        try {
            if (usuario != null) {
                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();
                params = usuario.getParamsCon();

                clave = obtenerClavePrimariaDocumentoTramitacion(request);
                
                //Obtenemos el listado de los estados del circuito de firmas
                listaEstadoFirmas = firmaFlujoManager.getListaEstadosCircuitoFirmas(clave, params);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        Gson gson = new Gson();
        request.setAttribute("listaEstadoFirmas", gson.toJson(listaEstadoFirmas));
        request.setAttribute("claveDocumento", gson.toJson(clave));

        logger.debug("FIN - cargarPantallaVerEstadoCircuitoFirmas");

        return mapping.findForward("cargarPantallaVerEstadoCircuitoFirmas");
    }
    
    /**
     * Carga la pantalla de visualizacion del detalle del estado de la firma de un firmante
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward cargarPantallaVerDetalleEstadoFirma(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - cargarPantallaVerEstadoCircuitoFirmas");
        logger.debug("FIN - cargarPantallaVerEstadoCircuitoFirmas");

        return mapping.findForward("cargarPantallaVerDetalleEstadoFirma");
    }
    
    /**
     * Carga la pantalla de definicion del circuito de un flujo de firma personalizado.
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward cargarPantallaDefinicionCircuitoFirmaPersonalizada(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - cargarPantallaDefinicionCircuitoFirmaPersonalizada");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = null;

        Integer idFlujo = null;
        List<FirmaTipoVO> listaFirmaTipo = null;
        FirmaFlujoUsuariosVO firmaFlujoUsuarios = null;

        try {
            if (usuario != null) {
                params = usuario.getParamsCon();
                
                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();

                idFlujo = NumberUtils.createInteger(request.getParameter("idFlujo"));
                
                //Obtenemos el flujos de firma con los usuarios asociados
                firmaFlujoUsuarios = firmaFlujoManager.getFlujoFirmaConUsuarios(idFlujo, Boolean.TRUE, params);
                
                //Obtenemos el listado de tipos de flujo
                listaFirmaTipo = firmaFlujoManager.getListaTipoFirmas(params);
                
                request.setAttribute("idFlujo", idFlujo);
                request.setAttribute("listaFirmaTipo", listaFirmaTipo);
                request.setAttribute("firmaFlujoUsuarios", new Gson().toJson(firmaFlujoUsuarios));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        logger.debug("FIN - cargarPantallaDefinicionCircuitoFirmaPersonalizada");

        return mapping.findForward("cargarPantallaDefinicionCircuitoFirmaPersonalizada");
    }
    
    /**
     * Insertar un flujo de firma
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward insertarFlujoFirma(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - insertarFlujoFirma");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        TraductorAplicacionBean traductor = getTraductor(usuario);
        String[] params = null;

        Integer idFlujo = null;
        ResultadoAjax<Integer> respuesta = new ResultadoAjax<Integer>();
        respuesta.setStatus(ConstantesAjax.STATUS_AJAX_OK);
        respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_OK));

        try {
            if (usuario != null) {
                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();
                params = usuario.getParamsCon();

                String nombreFlujo = request.getParameter("nombreFlujo");
                Integer idTipoFlujo = NumberUtils.createInteger(request.getParameter("idTipoFlujo"));

                // Insertamos el flujo en BBDD
                idFlujo = firmaFlujoManager.insertarFlujoFirma(nombreFlujo, idTipoFlujo, Boolean.FALSE, params);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_INTERNO);
            respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_ERROR_INTERNO));
        }

        logger.debug("FIN - insertarFlujoFirma");

        respuesta.setResultado(idFlujo);
        WebOperations.retornarJSON(new Gson().toJson(respuesta), response);

        return null;
    }

    /**
     * Eliminar un flujo de firma
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward eliminarFlujoFirma(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - eliminarFlujoFirma");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        TraductorAplicacionBean traductor = getTraductor(usuario);
        String[] params = null;

        ResultadoAjax<String> respuesta = new ResultadoAjax<String>();
        respuesta.setStatus(ConstantesAjax.STATUS_AJAX_OK);
        respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_OK));

        try {
            if (usuario != null) {
                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();
                params = usuario.getParamsCon();

                Integer idFlujo = NumberUtils.createInteger(request.getParameter("idFlujo"));

                // Eliminamos el flujo en BBDD
                firmaFlujoManager.eliminarFlujoFirma(idFlujo, params);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_INTERNO);
            respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_ERROR_INTERNO));
        }

        logger.debug("FIN - eliminarFlujoFirma");

        respuesta.setResultado(null);
        WebOperations.retornarJSON(new Gson().toJson(respuesta), response);

        return null;
    }

    /**
     * Activa/Desactiva un flujo de firma
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward activarDesactivarFlujoFirma(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - activarDesactivarFlujoFirma");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        TraductorAplicacionBean traductor = getTraductor(usuario);
        String[] params = null;

        ResultadoAjax<String> respuesta = new ResultadoAjax<String>();
        respuesta.setStatus(ConstantesAjax.STATUS_AJAX_OK);
        respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_OK));

        try {
            if (usuario != null) {
                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();
                params = usuario.getParamsCon();

                Integer idFlujo = NumberUtils.createInteger(request.getParameter("idFlujo"));
                Boolean activar = Boolean.parseBoolean(request.getParameter("activar"));

                // Eliminamos el flujo en BBDD
                firmaFlujoManager.activarDesactivarFlujoFirma(idFlujo, activar, params);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_INTERNO);
            respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_ERROR_INTERNO));
        }

        logger.debug("FIN - activarDesactivarFlujoFirma");

        respuesta.setResultado(null);
        WebOperations.retornarJSON(new Gson().toJson(respuesta), response);

        return null;
    }

    /**
     * Actualizar la lista de firmantes del circuito del flujo de firma
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward actualizarDatosCircuitoFlujo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - actualizarDatosCircuitoFlujo");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        TraductorAplicacionBean traductor = getTraductor(usuario);
        String[] params = null;

        ResultadoAjax<String> respuesta = new ResultadoAjax<String>();
        respuesta.setStatus(ConstantesAjax.STATUS_AJAX_OK);
        respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_OK));

        try {
            if (usuario != null) {
                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();
                params = usuario.getParamsCon();

                Integer idFlujoFirma = NumberUtils.createInteger(request.getParameter("idFlujoFirma"));
                List<String> idsUsuarioCadena = new Gson().fromJson(request.getParameter("idsUsuarioFirmante"), List.class);

                List<Integer> idsUsuario = new ArrayList<Integer>();
                for (String id : idsUsuarioCadena) {
                    idsUsuario.add(Integer.valueOf(id));
                }

                // Actualizamos el circuito en BBDD
                firmaFlujoManager.actualizarDatosCircuitoFlujo(idFlujoFirma, idsUsuario, params);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_INTERNO);
            respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_ERROR_INTERNO));
        }

        logger.debug("FIN - actualizarDatosCircuitoFlujo");

        respuesta.setResultado(null);
        WebOperations.retornarJSON(new Gson().toJson(respuesta), response);

        return null;
    }

    /**
     * Actualiza la lista personalizada de firmantes del flujo y circuito de
     * firma de documentos de tramite. Pasa el documento de tramite a PDF y
     * le aplica el CSV, en caso de que fuese necesario.
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward prepararEnvioFlujoFirmaTramitePersonalizado(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - prepararEnvioFlujoFirmaTramitePersonalizado");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        TraductorAplicacionBean traductor = getTraductor(usuario);
        String[] params = null;
        
        ResultadoAjax<Integer> respuesta = new ResultadoAjax<Integer>();
        respuesta.setStatus(ConstantesAjax.STATUS_AJAX_OK);
        respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_OK));
        
        try {
            if (usuario != null) {
                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();
                params = usuario.getParamsCon();

                Integer codMunicipio = NumberUtils.createInteger(request.getParameter("codMunicipio"));
                String codProcedimiento = request.getParameter("codProcedimiento");
                Integer ejercicio = NumberUtils.createInteger(request.getParameter("ejercicio"));
                String numExpediente = request.getParameter("numExpediente");
                Integer codTramite = NumberUtils.createInteger(request.getParameter("codTramite"));
                Integer codOcurrencia = NumberUtils.createInteger(request.getParameter("codOcurrencia"));
                Integer codDocumento = NumberUtils.createInteger(request.getParameter("codDocumento"));
                FirmaFlujoUsuariosVO flujoCircuito = new Gson().fromJson(request.getParameter("flujoCircuito"), FirmaFlujoUsuariosVO.class);
                Integer idTipoFirma = flujoCircuito.getFlujo().getIdTipoFirma();
                List<Integer> idsUsuario = new ArrayList<Integer>();
                for (FirmaCircuitoVO circuito : flujoCircuito.getUsuariosCircuito()) {
                    idsUsuario.add(Integer.valueOf(circuito.getIdUsuario()));
                }

                // Actualizamos el circuito en BBDD
                firmaFlujoManager.actualizarFlujoCircuitoTramitePersonalizado(
                        codMunicipio, codProcedimiento, ejercicio, numExpediente,
                        codTramite, codOcurrencia, codDocumento,
                        idTipoFirma, idsUsuario, params);
                
                respuesta.setResultado(codDocumento);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_INTERNO);
            respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_ERROR_INTERNO));
        }

        logger.debug("FIN - prepararEnvioFlujoFirmaTramitePersonalizado");

        WebOperations.retornarJSON(new Gson().toJson(respuesta), response);
        return null;
    }

    /**
     * Actualizar el usuario firmante personalizado del documento de trámite.
     * Pasa el documento de tramite a PDF y le aplica el CSV,
     * en caso de que fuese necesario.
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward prepararEnvioUsuarioFirmaTramitePersonalizado(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - prepararEnvioUsuarioFirmaTramitePersonalizado");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        TraductorAplicacionBean traductor = getTraductor(usuario);
        String[] params = null;
        
        ResultadoAjax<Integer> respuesta = new ResultadoAjax<Integer>();
        respuesta.setStatus(ConstantesAjax.STATUS_AJAX_OK);
        respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_OK));
        
        try {
            if (usuario != null) {
                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();
                params = usuario.getParamsCon();

                Integer codMunicipio = NumberUtils.createInteger(request.getParameter("codMunicipio"));
                String codProcedimiento = request.getParameter("codProcedimiento");
                Integer ejercicio = NumberUtils.createInteger(request.getParameter("ejercicio"));
                String numExpediente = request.getParameter("numExpediente");
                Integer codTramite = NumberUtils.createInteger(request.getParameter("codTramite"));
                Integer codOcurrencia = NumberUtils.createInteger(request.getParameter("codOcurrencia"));
                Integer codDocumento = NumberUtils.createInteger(request.getParameter("codDocumento"));
                FirmaUsuarioVO usuarioFirmante = new Gson().fromJson(request.getParameter("usuarioFirmante"), FirmaUsuarioVO.class);

                // Actualizamos el circuito en BBDD
                firmaFlujoManager.actualizarUsuarioTramitePersonalizado(
                        codMunicipio, codProcedimiento, ejercicio, numExpediente,
                        codTramite, codOcurrencia, codDocumento,
                        usuarioFirmante.getIdUsuario(), params);
                
                respuesta.setResultado(codDocumento);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_INTERNO);
            respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_ERROR_INTERNO));
        }

        logger.debug("FIN - prepararEnvioUsuarioFirmaTramitePersonalizado");

        WebOperations.retornarJSON(new Gson().toJson(respuesta), response);
        return null;
    }
    
    /**
     * Comprueba si el usuario existe en el portafirmas externo
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward comprobarUsuarioPortafirmas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - comprobarUsuarioPortafirmas");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        TraductorAplicacionBean traductor = getTraductor(usuario);
        String[] params = null;

        ResultadoAjax<Boolean> respuesta = new ResultadoAjax<Boolean>();
        respuesta.setStatus(ConstantesAjax.STATUS_AJAX_OK);
        respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_OK));

        Boolean existeUsuario = null;
        
        try {
            if (usuario != null) {
                PluginPortafirmasExternoClienteManager pluginPortafirmasExternoManager
                        = PluginPortafirmasExternoClienteManager.getInstance();
                params = usuario.getParamsCon();

                String documento = request.getParameter("documento");

                // Comprobamos si existe el usuario en el portafirmas
                String codOrganizacion = String.valueOf(usuario.getOrgCod());
                
                existeUsuario = pluginPortafirmasExternoManager.comprobarUsuarioPortafirmasPorDocumento(
                        codOrganizacion, documento, params);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_INTERNO);
            respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_ERROR_INTERNO));
        }

        logger.debug("FIN - comprobarUsuarioPortafirmas");

        respuesta.setResultado(existeUsuario);
        WebOperations.retornarJSON(new Gson().toJson(respuesta), response);

        return null;
    }
    
    /**
     * Comprueba si el tipo de firma para un documento de tramitacion que ya ha
     * sido enviado al portafirmas es de tipo nuevo L o U (Flujo o Un Usuario))
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward comprobarSiTipoFirmaFlujoUsuarioDocumentoTramitacion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - comprobarSiTipoFirmaFlujoUsuarioDocumentoTramitacion");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        String[] params = null;
        Boolean esTipoFlujoUsuario = null;
        
        try {
            if (usuario != null) {
                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();
                params = usuario.getParamsCon();
                
                FirmaDocumentoTramiteClave clave = obtenerClavePrimariaDocumentoTramitacion(request);

                esTipoFlujoUsuario = firmaFlujoManager.comprobarSiTipoFirmaFlujoUsuarioDocumentoTramitacion(
                        clave, params);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        logger.debug("FIN - comprobarSiTipoFirmaFlujoUsuarioDocumentoTramitacion");

        WebOperations.retornarJSON(new Gson().toJson(esTipoFlujoUsuario), response);

        return null;
    }
    
    /**
     * Obtiene el flujo y el circuito de firma por defecto de la plantilla
     * de un documento de tramite
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward obtenerFlujoFirmaPorDefectoByDocumentoTramite(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - obtenerFlujoFirmaPorDefectoByDocumentoTramite");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        TraductorAplicacionBean traductor = getTraductor(usuario);
        String[] params = null;

        ResultadoAjax<Map<String, Object>> respuesta = new ResultadoAjax<Map<String, Object>>();
        respuesta.setStatus(ConstantesAjax.STATUS_AJAX_OK);
        respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_OK));

        Map<String, Object> resultado = new HashMap<String, Object>();
        
        try {
            if (usuario != null) {
                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();
                params = usuario.getParamsCon();

                Integer codMunicipio = NumberUtils.createInteger(request.getParameter("codMunicipio"));
                String codProcedimiento = request.getParameter("codProcedimiento");
                Integer ejercicio = NumberUtils.createInteger(request.getParameter("ejercicio"));
                String numExpediente = request.getParameter("numExpediente");
                Integer codTramite = NumberUtils.createInteger(request.getParameter("codTramite"));
                Integer codOcurrencia = NumberUtils.createInteger(request.getParameter("codOcurrencia"));
                Integer codDocumento = NumberUtils.createInteger(request.getParameter("codDocumento"));
                
                // Obtenemos el id del flujo
                Integer idFlujo = firmaFlujoManager.getIdFlujoByDocumentoTramite(
                        codMunicipio, codProcedimiento, ejercicio, numExpediente,
                        codTramite, codOcurrencia, codDocumento,
                        params);
                
                //Obtenemos el flujos de firma con los usuarios asociados
                FirmaFlujoUsuariosVO firmaFlujoUsuarios = firmaFlujoManager.getFlujoFirmaConUsuarios(idFlujo, Boolean.TRUE, params);
                
                resultado.put("codDocumento", codDocumento);
                resultado.put("firmaFlujoUsuarios", firmaFlujoUsuarios);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_INTERNO);
            respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_ERROR_INTERNO));
        }

        logger.debug("FIN - obtenerFlujoFirmaPorDefectoByDocumentoTramite");

        respuesta.setResultado(resultado);
        WebOperations.retornarJSON(new Gson().toJson(respuesta), response);

        return null;
    }
    
    /**
     * Obtiene el usuario de firma por defecto de la plantilla
     * de un documento de tramite
     *
     * @param mapping: ActionMapping
     * @param form: ActionForm
     * @param request: HttpServletRequest
     * @param response: HttpServletResponse
     * @return
     * @throws Exception
     */
    public ActionForward obtenerUsuarioFirmaPorDefectoByDocumentoTramite(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("INI - obtenerUsuarioFirmaPorDefectoByDocumentoTramite");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        TraductorAplicacionBean traductor = getTraductor(usuario);
        String[] params = null;

        ResultadoAjax<Map<String, Object>> respuesta = new ResultadoAjax<Map<String, Object>>();
        respuesta.setStatus(ConstantesAjax.STATUS_AJAX_OK);
        respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_OK));

        Map<String, Object> resultado = new HashMap<String, Object>();
        
        try {
            if (usuario != null) {
                FirmaFlujoManager firmaFlujoManager = FirmaFlujoManager.getInstance();
                params = usuario.getParamsCon();

                Integer codMunicipio = NumberUtils.createInteger(request.getParameter("codMunicipio"));
                String codProcedimiento = request.getParameter("codProcedimiento");
                Integer ejercicio = NumberUtils.createInteger(request.getParameter("ejercicio"));
                String numExpediente = request.getParameter("numExpediente");
                Integer codTramite = NumberUtils.createInteger(request.getParameter("codTramite"));
                Integer codOcurrencia = NumberUtils.createInteger(request.getParameter("codOcurrencia"));
                Integer codDocumento = NumberUtils.createInteger(request.getParameter("codDocumento"));
                
                 //Se obtienen los tipos de procedimientos del fichero properties Portafirmas.properties
                ResourceBundle expPortafirmas = ResourceBundle.getBundle("Portafirmas");
                 String propiedad = usuario.getOrgCod()+"/Portafirmas";
                String portafirmas = expPortafirmas.getString(propiedad);
                
                FirmaUsuarioVO usuarioFirmante = firmaFlujoManager.getUsuarioPorDefectoByDocumentoTramite(
                        codMunicipio, codProcedimiento, ejercicio, numExpediente,
                        codTramite, codOcurrencia, codDocumento, portafirmas,
                        params);
                
                resultado.put("codDocumento", codDocumento);
                resultado.put("usuarioFirmante", usuarioFirmante);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_INTERNO);
            respuesta.setDescStatus(traductor.getDescripcion(ConstantesAjax.ETIQUETA_STATUS_AJAX_ERROR_INTERNO));
        }

        logger.debug("FIN - obtenerUsuarioFirmaPorDefectoByDocumentoTramite");

        respuesta.setResultado(resultado);
        WebOperations.retornarJSON(new Gson().toJson(respuesta), response);

        return null;
    }
    
    /**
     * Devuelve el traductor de textos para este modulo y usuario
     * 
     * @param usuario
     * @return 
     */
    private TraductorAplicacionBean getTraductor(UsuarioValueObject usuario) {
        TraductorAplicacionBean traductor = new TraductorAplicacionBean();
        if (usuario != null) {
            traductor.setApl_cod(usuario.getAppCod());
            traductor.setIdi_cod(usuario.getIdioma());
        } else {
            traductor.setApl_cod(ConstantesDatos.APP_GESTION_EXPEDIENTES);
            traductor.setIdi_cod(ConstantesDatos.IDIOMA_CASTELLANO);
        }
        
        return traductor;
    }

    /**
     * Construye la clave primaria del documento de tramitacion a partir de la request
     * @param request
     * @return 
     */
    private FirmaDocumentoTramiteClave obtenerClavePrimariaDocumentoTramitacion(HttpServletRequest request) {
        Integer codMunicipio = NumberUtils.createInteger(request.getParameter("codMunicipio"));
        String codProcedimiento = request.getParameter("codProcedimiento");
        Integer ejercicio = NumberUtils.createInteger(request.getParameter("ejercicio"));
        String numExpediente = request.getParameter("numExpediente");
        Integer codTramite = NumberUtils.createInteger(request.getParameter("codTramite"));
        Integer codOcurrencia = NumberUtils.createInteger(request.getParameter("codOcurrencia"));
        Integer codDocumento = NumberUtils.createInteger(request.getParameter("codDocumento"));
        
        FirmaDocumentoTramiteClave clave = new FirmaDocumentoTramiteClave(
                codMunicipio,
                codProcedimiento,
                ejercicio,
                numExpediente,
                codTramite,
                codOcurrencia,
                codDocumento
        );
        
        return clave;
    }
}
