package es.altia.agora.interfaces.user.web.registro;

import com.google.gson.Gson;
import es.altia.agora.business.administracion.mantenimiento.CamposListadosParametrizablesVO;
import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORsDAO;
import es.altia.agora.business.documentos.helper.CodigoSeguroVerificacionHelper;
import es.altia.agora.business.escritorio.RegistroUsuarioValueObject;
import es.altia.agora.business.editor.mantenimiento.persistence.DocumentosAplicacionManager;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.planeamiento.persistence.manual.AnotacionRegistroDAO;
import es.altia.agora.business.registro.DiligenciasValueObject;
import es.altia.agora.business.registro.HistoricoMovimientoValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.ReservaOrdenValueObject;
import es.altia.agora.business.registro.SimpleRegistroValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.mantenimiento.HojaArbolClasifAsuntosValueObject;
import es.altia.agora.business.registro.mantenimiento.MantAsuntosValueObject;
import es.altia.agora.business.registro.mantenimiento.MantTemasValueObject;
import es.altia.agora.business.registro.mantenimiento.persistence.ManActuacionesManager;
import es.altia.agora.business.registro.mantenimiento.persistence.MantRegistroExternoManager;
import es.altia.agora.business.registro.mantenimiento.persistence.MantAsuntosManager;
import es.altia.agora.business.registro.mantenimiento.persistence.MantClasifAsuntosManager;
import es.altia.agora.business.registro.mantenimiento.persistence.MantTemaManager;
import es.altia.agora.business.registro.mantenimiento.persistence.MantDocumentoManager;
import es.altia.agora.business.registro.persistence.AuditoriaManager;
import es.altia.agora.business.registro.mantenimiento.persistence.manual.MantAsuntosDAO;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.registro.persistence.DiligenciasManager;
import es.altia.agora.business.registro.persistence.HistoricoMovimientoManager;
import es.altia.agora.business.registro.persistence.RegistroAperturaCierreManager;
import es.altia.agora.business.registro.persistence.ReservaOrdenManager;
import es.altia.agora.business.registro.persistence.ImpresionCuneusManager;
import es.altia.agora.business.registro.sir.exception.ServicioSIRException;
import es.altia.agora.business.registro.sir.service.RegistroSIRExceptionService;
import es.altia.agora.business.registro.sir.service.RegistroSIRService;
import es.altia.agora.business.registro.sir.service.SIRDestinoRResService;
import es.altia.agora.business.registro.sir.vo.SirRegistroSalidaResponse;
import es.altia.agora.business.sge.DocumentoAnotacionRegistroVO;
import es.altia.agora.business.sge.InteresadoExpedienteVO;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.business.sge.persistence.InteresadosManager;
import es.altia.agora.business.sge.persistence.TramitacionExpedientesManager;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.flexia.sir.GestionSir;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.business.util.HistoricoAnotacionHelper;
import es.altia.agora.interfaces.user.web.administracion.SessionInfo;
import es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm;
import es.altia.agora.interfaces.user.web.util.HistoricoAnotacionTraductorHTML;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.Fecha;
import es.altia.arboles.impl.ArbolImpl;
import es.altia.catalogoformularios.model.solicitudes.vo.FormularioTramitadoVO;
import es.altia.catalogoformularios.model.solicitudesfacade.FormularioFacade;
import es.altia.catalogoformularios.util.exceptions.InternalErrorException;
import es.altia.common.exception.CriticalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.mail.MailHelper;
import es.altia.flexia.expedientes.relacionados.plugin.PluginExpedientesRelacionados;
import es.altia.flexia.expedientes.relacionados.plugin.PluginExpedientesRelacionadosFlexia;
import es.altia.flexia.expedientes.relacionados.plugin.factoria.PluginExpedientesRelacionadosFactoria;
import es.altia.flexia.interfaces.user.web.registro.informes.EjecutaJustificantePDFFactoria;
import es.altia.flexia.interfaces.user.web.registro.informes.IEjecutaJustificantePDF;
import es.altia.flexia.registro.digitalizacion.lanbide.persistence.DigitalizacionDocumentosLanbideManager;
import es.altia.flexia.registro.justificante.persistence.bd.JustificanteRegistroPersonalizadoManager;
import es.altia.flexia.registro.justificante.vo.JustificanteRegistroPersonalizadoVO;
import es.altia.flexiaWS.documentos.bd.util.JusticanteRegistro;
import es.altia.util.commons.DateOperations;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.pdf.PdfMerger;
import es.altia.util.sqlxmlpdf.GeneralPDF;
import es.altia.util.struts.StrutsUtilOperations;
import java.io.ByteArrayInputStream;

import es.lanbide.lan6.adaptadoresPlatea.aiir.beans.Lan6Anexo;
import es.lanbide.lan6.adaptadoresPlatea.aiir.beans.Lan6AsientoRegistralRespuesta;
import es.lanbide.lan6.adaptadoresPlatea.aiir.beans.Lan6InteresadoAsientoRegistral;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import es.altia.common.service.auditoria.ConstantesAuditoria;
import es.altia.agora.interfaces.user.web.util.ResultadoAjax;
import es.altia.agora.technical.ConstantesAjax;
import es.altia.common.exception.TechnicalException;
import es.altia.common.util.UtilidadesRegistro;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.RespRetramitarDocumentosRegistroCambioProcVO;
import es.altia.flexia.registro.digitalizacion.lanbide.persistence.DocumentosRegistroLanbideRetramitacionService;
import es.altia.flexia.registro.digitalizacion.lanbide.persistence.MelanbideDokusiPlateaProService;
import es.altia.flexia.sir.exception.CodigoMensajeSir;
import es.altia.flexia.sir.exception.GestionSirException;
import es.altia.flexia.sir.util.GestionSirConstantes;
import es.altia.util.LectorProperties;
import es.altia.util.StringUtils;
import es.altia.util.commons.WebOperations;
import es.altia.util.conexion.BDException;
import es.altia.util.documentos.DocumentOperations;
import es.altia.util.exceptions.JODReportsException;

import java.io.BufferedOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.FileUtils;

public final class MantAnotacionRegistroAction extends ActionSession {

    private final static String REG_DUPLICAR_RESERVAS_MOSTRAR_CUNEUS = "Registro/duplicarReservas/mostrarCuneus";
    //Definimos esta variable para indicar el tamano maximo del campo asunto, 
    //un asunto que tenga mas caracteres qeu el TAMANO_MAXIMO falla al intentar
    //insertar en la BD, pq es un campo demasiado grande
    private final static int TAMANO_MAXIMO = 2000;

    protected static Config m_Conf = ConfigServiceHelper.getConfig("common");
    protected static Config registroConf = ConfigServiceHelper.getConfig("Registro");

    // Gestion Slaidas Lanbide SIR
    private static final RegistroSIRService registroSIRService = new RegistroSIRService();
    private static final SIRDestinoRResService sIRDestinoRResService = new SIRDestinoRResService();
    private static final MelanbideDokusiPlateaProService melanbideDokusiPlateaProService = new MelanbideDokusiPlateaProService();
    RegistroSIRExceptionService registroSIRExceptionService = new RegistroSIRExceptionService();
    TraductorAplicacionBean traductorAplicacionBean = new TraductorAplicacionBean();
    // Fin gestion Lanbide Salidas SIR

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        m_Log.debug("perform");
        HttpSession session = request.getSession();
        String usuarioActualLog = SessionInfo.getSessionUserLogin(session);
        usuarioActualLog = (usuarioActualLog != null && usuarioActualLog != "") ? usuarioActualLog.replace("Usuario logado: ", "") : "";
        m_Log.info(session.getId() + " " + usuarioActualLog + " MantAnotacionRegistroAction ----> opcion: " + request.getParameter("opcion") + ". " + SessionInfo.getSessionUserLogin(session));

        String opcion = "";
        UsuarioValueObject usuarioVO = null;
        RegistroUsuarioValueObject regUsuarioVO = null;
        String codigoUorRegistro = request.getParameter("codigoUorRegistro");

        // Asignar valor por defecto para tipoEntrada si no llega del formulario
        String tipoEntradaParam = request.getParameter("cbTipoEntrada");
        if (tipoEntradaParam == null || "".equals(tipoEntradaParam.trim())) {
            ((MantAnotacionRegistroForm) form).setCbTipoEntrada(0);
        }



        if (session.getAttribute("usuario") != null && "cargarNombre".equalsIgnoreCase(request.getParameter("opcion"))) {
            m_Log.info(session.getId() + " " + usuarioActualLog + " Caso : usuario != null && " + request.getParameter("opcion"));
            // nuevas UORs
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();

            String directiva_salidas_uor_usuario = "NO";

            String tipoES = (String) session.getAttribute("tipoAnotacion");

            m_Log.info(session.getId() + " " + usuarioActualLog + "  Tipo anotacion " + tipoES);

            m_Log.debug(" session.getAttribute(directiva_salidas_uor_usuario) " + session.getAttribute("directiva_salidas_uor_usuario"));

            if (tipoES != null) {
                if ("S".equals(tipoES) || "Relacion_S".equals(tipoES) || "Salida".equals(tipoES) || "S".equals(request.getParameter("opcion"))) {

                    if (session.getAttribute("directiva_salidas_uor_usuario") != null) {
                        directiva_salidas_uor_usuario = (String) session.getAttribute("directiva_salidas_uor_usuario");
                    }

                } else {
                    directiva_salidas_uor_usuario = "NO";

                }
            }

            Vector nUOR = new Vector();
            if (("SI".equals(directiva_salidas_uor_usuario)) && (tipoES != null) && ("S".equals(tipoES) || "Relacion_S".equals(tipoES))) {
                nUOR = UORsManager.getInstance().getListaUORsPermisoUsuarioNuevo(usuario, params);
            } else {
                nUOR = UORsManager.getInstance().getListaUORs(false, params);
            }

            MantAnotacionRegistroForm registroForm = (MantAnotacionRegistroForm) form;
            registroForm.setListaNuevasUORs(nUOR);

            // arbol de jerarquia de uors
            ArbolImpl arbolUORs = new ArbolImpl();
            if (("SI".equals(directiva_salidas_uor_usuario)) && (tipoES != null) && ("S".equals(tipoES) || "Relacion_S".equals(tipoES))) {
                arbolUORs = UORsManager.getInstance().getArbolUORsPermisoUsuario(true, true, usuario, params);
            } else {
                arbolUORs = UORsManager.getInstance().getArbolUORs(false, true, true, params);
            }
            m_Log.info(session.getId() + " " + usuarioActualLog + " Cargado Arbol:" + arbolUORs.contarNodos());

            registroForm.setArbol(arbolUORs);
            request.setAttribute("listaUORs", nUOR);
            request.setAttribute("arbolUORs", arbolUORs);

            opcion = "cargarNombre";
            return mapping.findForward(opcion);
        } else {
            if (session.getAttribute("usuario") != null && "seleccionUOR".equalsIgnoreCase(request.getParameter("opcion"))) {
                m_Log.info(session.getId() + " " + usuarioActualLog + " Caso : session.getAttribute(\"usuario\") != null && \"seleccionUOR\".equalsIgnoreCase(request.getParameter(\"opcion\"))");
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                String[] params = usuario.getParamsCon();
                Vector uors = new Vector();
                ArbolImpl arboluors = new ArbolImpl();

                String vengoPagina = request.getParameter("vengoPagina");
                String directiva_salidas_uor_usuario = "NO";

                String tipoES = (String) session.getAttribute("tipoAnotacion");

                m_Log.info(session.getId() + " " + usuarioActualLog + "  Tipo anotacion " + tipoES);

                m_Log.debug(" session.getAttribute(directiva_salidas_uor_usuario) " + session.getAttribute("directiva_salidas_uor_usuario"));

                if (tipoES != null) {
                    if ("S".equals(tipoES) || "Relacion_S".equals(tipoES) || "Salida".equals(tipoES) || "S".equals(request.getParameter("opcion"))) {

                        if (session.getAttribute("directiva_salidas_uor_usuario") != null) {
                            directiva_salidas_uor_usuario = (String) session.getAttribute("directiva_salidas_uor_usuario");
                        }

                    } else {
                        directiva_salidas_uor_usuario = "NO";

                    }
                }

                if (("SI".equals(directiva_salidas_uor_usuario)) && (tipoES != null) && ("S".equals(tipoES) || "Relacion_S".equals(tipoES))) {
                    if (request.getParameter("consultando") != null && !request.getParameter("consultando").equals("")
                            && request.getParameter("consultando").equalsIgnoreCase("si")) {
                        uors = UORsManager.getInstance().getListaUORsPermisoUsuarioNuevo(usuario, params);
                        arboluors = UORsManager.getInstance().getArbolUORsPermisoUsuario(false, false, usuario, params);

                    } else {
                        uors = UORsManager.getInstance().getListaUORsPorNoVisRegistroPermisoUsuario('0', usuario, params);
                        arboluors = UORsManager.getInstance().getArbolUORsPermisoUsuario(true, false, usuario, params);
                    }
                } else if (request.getParameter("consultando") != null && !request.getParameter("consultando").equals("")
                        && request.getParameter("consultando").equalsIgnoreCase("si")) {
                    uors = UORsManager.getInstance().getListaUORs(false, params);

                    boolean ordenarNombre = false;
                    if (vengoPagina != null && "true".equals(vengoPagina)) {
                        ordenarNombre = false;
                    } else {
                        request.setAttribute("marcarCodigo", "false");
                        // Si la peticiï¿½n no llega de la pï¿½gina que muestra el ï¿½rbol de uors.
                        ResourceBundle config = ResourceBundle.getBundle("Registro");
                        try {
                            String tipoOrdenacion = config.getString(usuario.getOrgCod() + "/ORDENACION_ARBOL");
                            if (tipoOrdenacion != null && "NOMBRE".equals(tipoOrdenacion)) {
                                ordenarNombre = true;
                            }
                        } catch (Exception e) {
                            // Se ordena por cï¿½digo
                            ordenarNombre = false;
                        }
                    }

                    if (ordenarNombre) {
                        request.setAttribute("marcarCodigo", "false");
                    } else {
                        request.setAttribute("marcarCodigo", "true");
                    }

                    arboluors = UORsManager.getInstance().getArbolUORs(false, false, ordenarNombre, params);

                } else {
                    uors = UORsManager.getInstance().getListaUORsPorNoVisRegistro('0', params);

                    /**
                     * *** PRUEBA *****
                     */
                    boolean ordenarNombre = false;
                    if (vengoPagina != null && "true".equals(vengoPagina)) {
                        ordenarNombre = false;
                    } else {
                        request.setAttribute("marcarCodigo", "false");
                        // Si la peticiï¿½n no llega de la pï¿½gina que muestra el ï¿½rbol de uors.
                        ResourceBundle config = ResourceBundle.getBundle("Registro");
                        try {
                            String tipoOrdenacion = config.getString(usuario.getOrgCod() + "/ORDENACION_ARBOL");
                            if (tipoOrdenacion != null && "NOMBRE".equals(tipoOrdenacion)) {
                                ordenarNombre = true;
                            }
                        } catch (Exception e) {
                            // Se ordena por cï¿½digo
                            ordenarNombre = false;
                        }
                    }

                    if (ordenarNombre) {
                        request.setAttribute("marcarCodigo", "false");
                    } else {
                        request.setAttribute("marcarCodigo", "true");
                    }

                    /**
                     * *** PRUEBA ******
                     */
                    //arboluors = UORsManager.getInstance().getArbolUORs(false,true, false, params);
                    arboluors = UORsManager.getInstance().getArbolUORs(false, true, ordenarNombre, params);
                }

                m_Log.debug("Recuperadas " + uors.size() + " UORs");
                m_Log.debug("Recuperadas " + (arboluors.contarNodos() - 1) + " UORs en el Arbol");
                request.setAttribute("arbolUORs", arboluors);
                request.setAttribute("listaUORs", uors);
                opcion = "seleccionUOR";
                return mapping.findForward(opcion);
            } else if (session.getAttribute("usuario") != null && "seleccionUORFiltroUsu".equalsIgnoreCase(request.getParameter("opcion"))) {
                m_Log.info(session.getId() + " " + usuarioActualLog + " Caso : session.getAttribute(\"usuario\") != null && \"seleccionUORFiltroUsu\".equalsIgnoreCase(request.getParameter(\"opcion\"))");
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                String[] params = usuario.getParamsCon();
                Vector uors = new Vector();
                ArbolImpl arboluors = new ArbolImpl();

                session.setAttribute("directiva_salidas_uor_usuario", "SI");
                session.setAttribute("tipoAnotacion", "S");

                if (request.getParameter("consultando") != null && !request.getParameter("consultando").equals("")
                        && request.getParameter("consultando").equalsIgnoreCase("si")) {
                    uors = UORsManager.getInstance().getListaUORsPermisoUsuario(usuario, params);
                    arboluors = UORsManager.getInstance().getArbolUORsPermisoUsuario(false, false, usuario, params);

                } else {
                    uors = UORsManager.getInstance().getListaUORsPorNoVisRegistroPermisoUsuario('0', usuario, params);
                    arboluors = UORsManager.getInstance().getArbolUORsPermisoUsuario(true, false, usuario, params);
                }

                m_Log.debug("Recuperadas " + uors.size() + " UORs");
                m_Log.debug("Recuperadas " + (arboluors.contarNodos() - 1) + " UORs en el Ã¡rbol");
                request.setAttribute("arbolUORs", arboluors);
                request.setAttribute("listaUORs", uors);
                opcion = "seleccionUOR";
                return mapping.findForward(opcion);
            }
        }

        // Si no existe en la sesion el atributo 'registroUsuario' para obtener
        // el codigo de unidad de registro, usamos 'codigoUorRegistro', (existe
        // en el caso de alta de anotacion de salida desde tramitacion)
        if (session.getAttribute("usuario") != null
                && (session.getAttribute("registroUsuario") != null || codigoUorRegistro != null
                || "cargarNombre".equals(request.getParameter("opcion"))
                || "cargarCodigo".equals(request.getParameter("opcion")))) {
            m_Log.info(session.getId() + " " + usuarioActualLog
                    + " Caso : " + session.getAttribute("usuario") + " " + codigoUorRegistro + " " + session.getAttribute("registroUsuario") + " " + request.getParameter("opcion"));

            int cod_org;
            int cod_ent;
            int cod_dep;
            int cod_uni;
            String usuarioQRegistra;
            String dptoUsuarioQRegistra;
            String unidOrgUsuarioQRegistra;

            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            regUsuarioVO = (RegistroUsuarioValueObject) session.getAttribute("registroUsuario");
            cod_org = usuarioVO.getOrgCod();
            cod_ent = usuarioVO.getEntCod();

            // Traductor Segun idioma de Usuario
            traductorAplicacionBean.setApl_cod(ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA);
            traductorAplicacionBean.setIdi_cod(usuarioVO.getIdioma());

            // #291976: aï¿½adimos a la sesion la propiedad SERVICIO_DIGITALIZACION_ACTIVO de Registro.properties
            String servDigitalizacionAct = (String) session.getAttribute("servicioDigitalizacionActivo");
            m_Log.info(session.getId() + " " + usuarioActualLog + " (String) session.getAttribute(\"servicioDigitalizacionActivo\") : " + servDigitalizacionAct);
            if (servDigitalizacionAct == null) {
                m_Log.info(session.getId() + " " + usuarioActualLog + " servicioDigitalizacionActivo No viene en Session - Asignamos desde properties");
                try {
                    servDigitalizacionAct = registroConf.getString(usuarioVO.getOrgCod() + "/SERVICIO_DIGITALIZACION_ACTIVO");
                } catch (Exception e) {
                    m_Log.error("Se ha producido un error recuperando la propiedad '" + usuarioVO.getOrgCod() + "/SERVICIO_DIGITALIZACION_ACTIVO' de Registro.properties");
                }
                if (servDigitalizacionAct != null && servDigitalizacionAct.equalsIgnoreCase("si")) {
                    session.setAttribute("servicioDigitalizacionActivo", servDigitalizacionAct);
                }
            }
            m_Log.info(session.getId() + " " + usuarioActualLog + " servicioDigitalizacionActivo Recuperado O asiganado desde properties a Session si no venia " + servDigitalizacionAct);

            // Si existe en la request el atributo 'codigoUorRegistro' lo
            // usamos en vez del que contiene el RegistroUsuarioValueObject.
            if (codigoUorRegistro != null) {
                regUsuarioVO = new RegistroUsuarioValueObject();
                regUsuarioVO.setDepCod(ConstantesDatos.REG_COD_DEP_DEFECTO);
                regUsuarioVO.setUnidadOrgCod(Integer.parseInt(codigoUorRegistro));
                session.setAttribute("registroUsuario", regUsuarioVO);
            }

            cod_dep = regUsuarioVO.getDepCod();
            cod_uni = regUsuarioVO.getUnidadOrgCod();
            usuarioQRegistra = String.valueOf(usuarioVO.getIdUsuario());
            dptoUsuarioQRegistra = String.valueOf(usuarioVO.getDepCod());
            unidOrgUsuarioQRegistra = String.valueOf(usuarioVO.getUnidadOrgCod());
            String[] params = usuarioVO.getParamsCon();

            MantAnotacionRegistroForm registroForm = null;
            RegistroValueObject elRegistroESVO = null;

            if (form == null) {
                m_Log.debug("Rellenamos el form de RegistroEntradaSalida");
                form = new MantAnotacionRegistroForm();
                if ("request".equals(mapping.getScope())) {
                    request.setAttribute(mapping.getAttribute(), form);
                } else {
                    session.setAttribute(mapping.getAttribute(), form);
                }
            }

            registroForm = (MantAnotacionRegistroForm) form;

            String directiva_salidas_uor_usuario = "NO";

            String tipoES = null;
            if (!"E".equals((String) request.getParameter("opcion"))) {
                tipoES = (String) session.getAttribute("tipoAnotacion");
            }

            if (tipoES != null) {
                if ("S".equals(tipoES)
                        || "Relacion_S".equals(tipoES)
                        || ("S".equals((String) request.getParameter("opcion")))
                        || ("Relacion_S".equals((String) request.getParameter("opcion")))) {

                    if (UsuariosGruposManager.getInstance().tienePermisoDirectiva(ConstantesDatos.REGISTRO_S_SOLO_UORS_USUARIO, usuarioVO.getIdUsuario(), params)) {
                        m_Log.debug("******* MantAnotacionRegistroAction hay directiva de usuario que solo puede acceder a uors a las que tenga permiso");

                        session.setAttribute("directiva_salidas_uor_usuario", "SI");
                        directiva_salidas_uor_usuario = "SI";

                    } else {
                        m_Log.debug("******* MantAnotacionRegistroAction   NO hay directiva de usuario que solo puede acceder a uors a las que tenga permiso");
                        session.setAttribute("directiva_salidas_uor_usuario", "NO");
                    }
                } else {
                    session.setAttribute("directiva_salidas_uor_usuario", "NO");
                    directiva_salidas_uor_usuario = "NO";

                }
            }
            if ("salida_desde_tramitar".equals((String) request.getParameter("opcion"))) {
                if (UsuariosGruposManager.getInstance().tienePermisoDirectiva(ConstantesDatos.REGISTRO_S_SOLO_UORS_USUARIO, usuarioVO.getIdUsuario(), params)) {
                    m_Log.debug("******* MantAnotacionRegistroAction hay directiva de usuario que solo puede acceder a uors a las que tenga permiso");
                    session.setAttribute("directiva_salidas_uor_usuario", "SI");
                    directiva_salidas_uor_usuario = "SI";

                }
            }

            Vector nuevasUOR = new Vector();

            nuevasUOR = (Vector) session.getAttribute("nuevasUOREnRegistro");

            if (nuevasUOR == null) {
                if (("SI".equals(directiva_salidas_uor_usuario))) {
                    nuevasUOR = UORsManager.getInstance().getListaUORsPermisoUsuarioNuevo(usuarioVO, params);
                } else {
                    nuevasUOR = UORsManager.getInstance().getListaUORs(false, params);
                }
                m_Log.debug("Recuperadas " + nuevasUOR.size() + " UORs");
                registroForm.setListaNuevasUORs(nuevasUOR);
                session.setAttribute("nuevasUOREnRegistro", nuevasUOR);
            }

            //arbol de jerarquia de uors
            ArbolImpl arbol = new ArbolImpl();

            arbol = (ArbolImpl) session.getAttribute("arbolEnRegistro");

            if (arbol == null) {
                if (("SI".equals(directiva_salidas_uor_usuario))) {
                    arbol = UORsManager.getInstance().getArbolUORsPermisoUsuario(false, false, usuarioVO, params);
                } else {
                    arbol = UORsManager.getInstance().getArbolUORs(false, false, false, params);

                }
                m_Log.debug("Recuperadas " + (arbol.contarNodos() - 1) + " UORs en el Arbol");
                registroForm.setArbol(arbol);
                session.setAttribute("arbolEnRegistro", arbol);
            }

            //TENEMOS QUE RECUPERAR LAS COLUMNAS A MOSTRAR EN EL LISTADO Y EL TAMANO
            //SABEMOS QUE EN ESTE CASO SE TRATA DEL LISTADO TIPO 3 QUE ES ANOTACIONES
            CamposListadosParametrizablesVO gVO1 = new CamposListadosParametrizablesVO();
            Vector listaCamposListados = AnotacionRegistroManager.getInstance().getCamposListado(3, params);

            opcion = request.getParameter("opcion");

            String BLOQUEO_FECHA_HORA_ANOTACION = "no";
            try {
                // Si no esta el parametro de bloqueo de fecha y hora => El funcionamiento es el de siempre
                BLOQUEO_FECHA_HORA_ANOTACION = registroConf.getString(ConstantesDatos.BLOQUEO_FECHA_HORA_ANOTACION);

            } catch (Exception e) {
            }

            /**
             * ********** SE COMPRUEBA SI EL PLUGIN DE EXPEDIENTES RELACIONADOS
             * CARGADOS ES EL DE FLEXIA O EL DE UN GESTOR DE EXPEDIENTES EXTERNO *******
             */
            PluginExpedientesRelacionados plugin = PluginExpedientesRelacionadosFactoria.getImplClass(Integer.toString(usuarioVO.getOrgCod()));
            if (plugin instanceof PluginExpedientesRelacionadosFlexia) {
                session.setAttribute("plugin_exp_relacionados_flexia", "SI");
            } else {
                session.setAttribute("plugin_exp_relacionados_flexia", "NO");
            }

            /**
             * *********************************** FIN COMPROBACION PLUGIN
             * EXPEDIENTES RELACIONADOS *********************************************
             */
            m_Log.debug(" =====> BLOQUEO_FECHA_HORA_ANOTACION " + BLOQUEO_FECHA_HORA_ANOTACION);
            registroForm.setBloquearFechaHoraPresentacion("NO");
            if ("si".equalsIgnoreCase(BLOQUEO_FECHA_HORA_ANOTACION)) {
                registroForm.setBloquearFechaHoraPresentacion("SI");
            }

            //Comprobamos si en el fichero de propiedades de registro existe una entrada que nos indice si el campo asunto serï¿½ obligatorio
            m_Log.info(session.getId() + " " + usuarioActualLog + " Recuperamos del fichero de propiedades de registro la entrada que indica si el asunto"
                    + " es obligatorio, si la entrada no existe se toma por defecto el valor falso");
            Boolean asuntoObligatorio = false;
            try {
                String valorPropiedad = registroConf.getString(String.valueOf(cod_org) + ConstantesDatos.OBLIGATORIO_ASUNTO_CODIFICADO_REGISTRO);
                if (valorPropiedad.equalsIgnoreCase("SI")) {
                    asuntoObligatorio = true;
                }//if(valorPropiedad.equalsIgnoreCase("SI"))
            } catch (Exception ex) {
                m_Log.error("Se ha producido un error recuperando la propiedad que indica si el campo asunto es obligatorio", ex);
            }//try-catch
            m_Log.info(session.getId() + " " + usuarioActualLog + " El valor de la propiedad que indica si el asunto es obligatorio es = " + asuntoObligatorio);
            registroForm.setObligatorioAsuntoCodificado(asuntoObligatorio);

            //Comprobamos si en el fichero de propiedades de registro existe una entrada que nos indique si hay que realizar la busqueda
            //ajax del tercero
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Recuperamos del fichero de propiedades de registro la entrada que indica si hay"
                        + " que realizar la busqueda ajax del tercero, si no existe tomamos por defecto el valor S");
            }
            Boolean busquedaTercero = false;
            try {
                String valorPropiedad = registroConf.getString(String.valueOf(cod_org) + ConstantesDatos.BUSQUEDA_AJAX_TERCERO);
                if (valorPropiedad.equalsIgnoreCase("SI")) {
                    busquedaTercero = true;
                }//if(valorPropiedad.equalsIgnoreCase("SI"))
            } catch (Exception ex) {
                m_Log.error("Se ha producido un error recuperando la propiedad que indica si hay que realizar la busqueda ajax del tercero", ex);
            }//try-catch
            m_Log.info(session.getId() + " " + usuarioActualLog + " El valor de la propiedad que indica si se hara la busqueda ajax es = " + busquedaTercero);
            registroForm.setBusquedaAjaxTercero(busquedaTercero);

            m_Log.info(session.getId() + " " + usuarioActualLog + " Estamos en caso : " + opcion);
            if ("init_alta_entrada".equals(opcion) || "init_alta_salida".equals(opcion)) {
                /* -----------------------------------  INICIAR ALTA ENTRADA/SALIDA ---------------------------------------------------- */
                elRegistroESVO = new RegistroValueObject();
                elRegistroESVO.setIdOrganizacion(cod_org);
                elRegistroESVO.setIdEntidad(cod_ent);
                elRegistroESVO.setIdentDepart(cod_dep);
                elRegistroESVO.setUnidadOrgan(cod_uni);
                // Datos usuario que registra.
                elRegistroESVO.setUsuarioQRegistra(usuarioQRegistra);
                elRegistroESVO.setDptoUsuarioQRegistra(dptoUsuarioQRegistra);
                elRegistroESVO.setUnidOrgUsuarioQRegistra(unidOrgUsuarioQRegistra);
                // Fechas del servidor.
                Fecha f = new Fecha();
                Date fSistema = new Date();
                String hora = f.construirHora(fSistema);
                elRegistroESVO.setFecEntrada(Fecha.obtenerString(fSistema) + " " + hora);
                elRegistroESVO.setFecHoraDoc(Fecha.obtenerString(fSistema) + " " + hora);

                String fecha_almacenada = (String) request.getSession().getAttribute("fecha");
                String hora_almacenada = (String) request.getSession().getAttribute("hora");
                // Hora del servidor.
                String buzon = request.getParameter("pendienteBuzon");
                m_Log.info(session.getId() + " " + usuarioActualLog + " buzon: " + buzon);
                if (buzon.equalsIgnoreCase("si")) {
                    elRegistroESVO.setHayBuzon(true);
                }
                if ("init_alta_entrada".equals(opcion)) {
                    elRegistroESVO.setTipoReg("E");
                    if (fecha_almacenada != null) {
                        elRegistroESVO.setFecEntrada(fecha_almacenada + " " + hora_almacenada);
                        elRegistroESVO.setFecHoraDoc(fecha_almacenada + " " + hora_almacenada);
                    }
                } else {
                    elRegistroESVO.setTipoReg("S");
                }
                inicializarListas(elRegistroESVO, false, params); // Cargar listas.
                elRegistroESVO.setListaTemasAsignados(new Vector());
                elRegistroESVO.setRespOpcion(opcion);

                opcion = "init_alta_entrada"; // Segun redireccion de struts_config.xml

                String ejercicio = request.getParameter("ano");
                m_Log.info(session.getId() + " " + usuarioActualLog + " el ano es : " + ejercicio);
                String numero = request.getParameter("numero");
                m_Log.info(session.getId() + " " + usuarioActualLog + " el numero es : " + numero);
                if ((numero != null) && (!numero.equals(""))) {
                    elRegistroESVO.setNumReg(Long.parseLong(numero));
                }
                if ((ejercicio != null) && (!ejercicio.equals(""))) {
                    elRegistroESVO.setAnoReg(Integer.parseInt(ejercicio));
                }
                if (elRegistroESVO.getNumReg() != 0 && elRegistroESVO.getAnoReg() != 0) {
                    elRegistroESVO = AnotacionRegistroManager.getInstance().getByPrimaryKey(elRegistroESVO, params);
                } else {
                    elRegistroESVO.setListaDocsAsignados(new Vector<RegistroValueObject>());
                    elRegistroESVO.setListaDocsAnteriores(new Vector<RegistroValueObject>());
                }

                //Para buscar en la tabla diligencias
                String fechaEnt = elRegistroESVO.getFecEntrada();
                m_Log.info(session.getId() + " " + usuarioActualLog + " la fecha de entrada en el action es  : " + fechaEnt);
                DiligenciasValueObject diligencia = new DiligenciasValueObject();
                diligencia.setCodDepto(elRegistroESVO.getIdentDepart());
                diligencia.setCodUnidad(elRegistroESVO.getUnidadOrgan());
                diligencia.setTipo(elRegistroESVO.getTipoReg().charAt(0));
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("el tipo de registro en la diligencia es : " + diligencia.getTipo());
                }
                if (fechaEnt != null) {
                    String fechaEnt1 = fechaEnt.substring(0, 10);
                    diligencia.setFecha(fechaEnt1);
                    diligencia = DiligenciasManager.getInstance().load(diligencia, params);
                    String textoDil = diligencia.getAnotacion();
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("el texto de la diligencia en el action es : " + textoDil);
                    }
                    elRegistroESVO.setTextoDiligencia(textoDil);
                    if (!textoDil.equals("") && textoDil != null) {
                        elRegistroESVO.setHayTexto("si");
                    } else {
                        elRegistroESVO.setHayTexto("no");
                    }
                }
                session.removeAttribute("DiligenciasForm");
                /* -----------------------------------  FIN: INICIAR ALTA ENTRADA/SALIDA ---------------------------------------------------- */
            } else if ("registrar_alta_entrada".equals(opcion) || "registrar_alta_salida".equals(opcion)
                    || "duplicar".equals(opcion) || "registrar_alta_confirmada".equals(opcion)
                    || "registrar_alta_tramitar".equals(opcion)) {
                /* --------------------------------  REGISTRAR ALTA ENTRADA/SALIDA, DUPLICAR... ---------------------------------------------- */
                elRegistroESVO = registroForm.getRegistro();
                int ejercicioAnotacionDuplicadaOriginal = elRegistroESVO.getAnoReg();
                m_Log.debug("Codigo UOR en MantAnotacionRegistroAction: " + elRegistroESVO.getIdUndTramitad());
                // si es duplicar, puede que venga de un contestar -> borra constestacion
                if (opcion.equals("duplicar")) {
                    elRegistroESVO.setEjercicioAnotacionContestada(null);
                    elRegistroESVO.setNumeroAnotacionContestada(null);
                    elRegistroESVO.setEstAnotacion(0);
                }

                elRegistroESVO.setUsuarioQRegistra(usuarioQRegistra);
                elRegistroESVO.setDptoUsuarioQRegistra(dptoUsuarioQRegistra);
                elRegistroESVO.setUnidOrgUsuarioQRegistra(unidOrgUsuarioQRegistra);

                // tratar si va al buzon
                String buzon = request.getParameter("pendienteBuzon");
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("buzon: " + buzon);
                }
                if (buzon.equalsIgnoreCase("si")) {
                    elRegistroESVO.setHayBuzon(true);
                } else {
                    elRegistroESVO.setHayBuzon(false);
                }

                // Fecha de grabacion
                String fechaGrabacion = registroForm.getFechaDocumento();
                elRegistroESVO.setFecHoraDoc(fechaGrabacion);
                m_Log.info(session.getId() + " " + usuarioActualLog + " Fecha de grabacion: " + fechaGrabacion);

                // Fecha de presentacion
                String fechaPresentacion = request.getParameter("fechaAnotacion");
                if (fechaPresentacion == null) {
                    fechaPresentacion = registroForm.getFechaAnotacion();
                }
                String horaMinPresentacion = request.getParameter("horaMinAnotacion");
                // Si la hora esta en blanco ponemos la actual, independientemente de la fecha
                if (horaMinPresentacion == null || "".equals(horaMinPresentacion)) {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                    horaMinPresentacion = parser.format(calendar.getTime());
                }
                String fechaHoraPresentacion = fechaPresentacion + " " + horaMinPresentacion;

                // Comprobamos fecha y hora presentacion <= fecha y hora actual
                Date fechaAnotacion = Fecha.obtenerDateCompleto2(fechaHoraPresentacion);
                Date fechaActual = new Date();
                if (fechaActual.compareTo(fechaAnotacion) < 0) {
                    m_Log.info(session.getId() + " " + usuarioActualLog + " ALERTAR La fecha de entrada es posterior a la fecha actual : " + fechaActual + " < " + fechaAnotacion);
                    elRegistroESVO.setRespOpcion("fecha_posterior_a_actual");
                    registroForm.setRegistro(elRegistroESVO);
                    m_Log.debug("<================= MantAnotacionRegistroAction ======================");
                    return mapping.findForward("init_alta_entrada");
                }

                elRegistroESVO.setFecEntrada(fechaHoraPresentacion);
                int year = Calendar.getInstance().get(Calendar.YEAR);
                elRegistroESVO.setAnoReg(year);
                elRegistroESVO.setEjercicioAnotacionDuplicadaOrigen(ejercicioAnotacionDuplicadaOriginal);

                m_Log.debug("Fecha de presentacion: " + fechaHoraPresentacion);

                // Fecha de documento
                String fechaDocumento = request.getParameter("fechaDoc");
                elRegistroESVO.setFechaDocu(fechaDocumento);
                m_Log.debug("Fecha de documento: " + fechaDocumento);

                // Recuperar la lista de temas.
                String listTemasSelecc = request.getParameter("listaTemas");
                elRegistroESVO.setListaTemasAsignados(listaTemasSeleccionados(listTemasSelecc));

                try {
                    if ("registrar_alta_confirmada".equals(opcion)) {
                        elRegistroESVO.setRespOpcion("alta_confirmada");
                    } else {
                        elRegistroESVO.setRespOpcion("alta_sin_confirmar");
                    }

                    //recojo los datos para insertar los interesados.
                    String CodTercero = request.getParameter("listaCodTercero");
                    String VersionTercero = request.getParameter("listaVersionTercero");
                    String CodDomicilio = request.getParameter("listaCodDomicilio");
                    String Rol = request.getParameter("listaRol");

                    Vector listaCodTercero = null;
                    listaCodTercero = listaTemasSeleccionados(CodTercero);
                    elRegistroESVO.setlistaCodTercero(listaCodTercero);

                    Vector listaVersionTercero = null;
                    listaVersionTercero = listaTemasSeleccionados(VersionTercero);
                    elRegistroESVO.setlistaVersionTercero(listaVersionTercero);

                    Vector listaCodDomicilio = null;
                    listaCodDomicilio = listaTemasSeleccionados(CodDomicilio);
                    elRegistroESVO.setlistaCodDomicilio(listaCodDomicilio);

                    Vector listaRol = null;
                    listaRol = listaTemasSeleccionados(Rol);
                    elRegistroESVO.setlistaRol(listaRol);
                    elRegistroESVO.setCodTipoRemit(request.getParameter("cod_tipoRemitente"));
                    elRegistroESVO.setCodTipoDoc(request.getParameter("txtCodigoDocumento"));
                    //Obtenemos los documentos presentados, para indicar cuales fueron presentados en el momento del Alta

                    /**
                     * * SE ALMACENA EN EL OBJETO elRegistroESVO EL Cï¿½DIGO DE LA
                     * OFICINA DE REGISTRO DEL USUARIO *
                     */
                    elRegistroESVO.setCodOficinaRegistro(regUsuarioVO.getCodOficinaRegistro());

                    StringTokenizer valores = null;
                    String txtListaDocEntregados = request.getParameter("txtListaDocEntregados");
                    m_Log.info(session.getId() + " " + usuarioActualLog + " txtListaDocEntregados: " + txtListaDocEntregados);
                    valores = new StringTokenizer(txtListaDocEntregados, "ï¿½ï¿½", false);
                    Vector docsEntregados = new Vector();
                    while (valores.hasMoreTokens()) {
                        String valor = valores.nextToken();
                        docsEntregados.addElement(valor);

                    }

                    MantAnotacionRegistroForm regESForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
                    Vector docs = regESForm.getListaDocsAsignados();
                    m_Log.info(session.getId() + " " + usuarioActualLog + " Vector docs = regESForm.getListaDocsAsignados(); " + (docs != null ? docs.size() : "null"));
                    Vector docsFinal = new Vector();
                    m_Log.info(session.getId() + " " + usuarioActualLog + " docsEntregados.size() " + (docsEntregados != null ? docsEntregados.size() : "null"));
                    if (docs.size() == docsEntregados.size()) {
                        m_Log.info(session.getId() + " " + usuarioActualLog + " docs.size()==docsEntregados.size() --> Recorre lista de Documentos Asignados, rellena la lista docsFinal.add(regDoc); y regDoc.setEntregado((String)docsEntregados.elementAt(i)); ");
                        for (int i = 0; i < docs.size(); i++) {
                            RegistroValueObject regDoc = (RegistroValueObject) docs.elementAt(i);
                            regDoc.setEntregado((String) docsEntregados.elementAt(i));

                            docsFinal.add(regDoc);
                        }
                    } else {
                        m_Log.info(session.getId() + " " + usuarioActualLog + " docs.size()==docsEntregados.size() --> No se cumple la condicion. Asiganamos todos los documentos Asiganados.");
                        docsFinal = docs;
                    }

                    regESForm.setListaDocsAsignados(docsFinal);

                    //Vamos a comprobar el tamano del asunto, porque si el asunto
                    //tiene mas de 2000 caracteres,(fallaria al intentar insertar en la BD)
                    //va a ser necesario truncar,y coger los primeros 1000 caracteres
                    //Tarea: 86904
                    if (elRegistroESVO.getAsunto() != null) {
                        String asunto = elRegistroESVO.getAsunto();
                        m_Log.debug("El asunto que estamos pasando es: " + asunto);

                        if (asunto.length() > TAMANO_MAXIMO) {
                            m_Log.debug("La longitud del asunto es: " + asunto.length());
                            asunto = asunto.substring(0, TAMANO_MAXIMO - 1);
                            m_Log.debug("La longitud del asuntro truncado es: " + asunto.length());
                            elRegistroESVO.setAsunto(asunto);
                        }

                    }

                    //Estamos seteando el munProcedimiento, este valor tiene que ser igual
                    //al idOrganizacion.
                    //Tarea:92322
                    m_Log.debug("El idOrganizacion es:" + elRegistroESVO.getIdOrganizacion());
                    if ((elRegistroESVO.getIdOrganizacion() > -1)) {
                        String munProcedimiento = String.valueOf(new Integer(elRegistroESVO.getIdOrganizacion()));
                        m_Log.debug("El munProcedimiento es:" + munProcedimiento);
                        elRegistroESVO.setMunProcedimiento(munProcedimiento);

                    }

                    if (!"duplicar".equals(opcion)) {
                        m_Log.error("(NO ERROR) Traza de control - No estamos duplicando: " + opcion + ". Digitalizaciï¿½n: " + elRegistroESVO.isFinDigitalizacion());
                        AnotacionRegistroManager.getInstance().insertRegistroValueObject(elRegistroESVO, params);
                        m_Log.error("(NO ERROR) Traza de control - Se ha creado la entrada : " + elRegistroESVO.getNumReg());

                        //si esta el servicio de digitalizaciï¿½n activo  y se finaliza el proceso de digtalizaciï¿½n de documentos
                        String digitalizacionActivo = registroConf.getString(usuarioVO.getOrgCod() + "/SERVICIO_DIGITALIZACION_ACTIVO");
                        m_Log.info(session.getId() + " " + usuarioActualLog + " digitalizacionActivo " + digitalizacionActivo);
                        if (digitalizacionActivo.equalsIgnoreCase("si")) {
                            if (elRegistroESVO.isFinDigitalizacion()) {
                                DigitalizacionDocumentosLanbideManager digitalizarManager = DigitalizacionDocumentosLanbideManager.getInstance();
                                ArrayList<DocumentoAnotacionRegistroVO> documentosRegistro = (ArrayList<DocumentoAnotacionRegistroVO>) digitalizarManager.getDocumentosRegistro(elRegistroESVO, params);
                                m_Log.info(session.getId() + " " + usuarioActualLog + " Documentos de registro recuperados: " + documentosRegistro.size());
                                String codProcedimiento = AnotacionRegistroManager.getInstance().getCodProcedimientoRegistro(elRegistroESVO, params);
                                // se cambia el documento de contexto Registro a Tramitaciï¿½n
                                m_Log.info(session.getId() + " " + usuarioActualLog + " codProcedimiento : " + codProcedimiento);
                                if (codProcedimiento != null && !codProcedimiento.isEmpty()) {
                                    for (DocumentoAnotacionRegistroVO docuReg : documentosRegistro) {
                                        String idDocumento = docuReg.getIdDocGestor();
                                        try {
                                            digitalizarManager.tramitarDocumento(elRegistroESVO.getUnidadOrgan(), elRegistroESVO.getAnoReg(), elRegistroESVO.getNumReg(), idDocumento, codProcedimiento, docuReg.isCompulsado(), params);
                                        } catch (Exception le) {
                                            m_Log.error("MantAnotacionREgistroAction problem " + le.getMessage(), le);
                                            AnotacionRegistroManager.getInstance().cancelarFinDigitalizacionDocumentos(elRegistroESVO, params);
                                            opcion = "error";
                                            elRegistroESVO.setRespOpcion("errorDigitAlta");
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        m_Log.info(session.getId() + " " + usuarioActualLog + " Es una duplicacion de la entrada. elRegistroESVO : " + (elRegistroESVO != null ? elRegistroESVO.getAnoAnotacion() + "/" + elRegistroESVO.getNumReg() : ""));
                        AnotacionRegistroManager.getInstance().duplicar(elRegistroESVO, params);
                    }

                    // #586104 Dar de alta salida en SIR
                    // #681353 Alta Entradas en el SIR - Enviar si se finaliza el proceso de anexo documentacion.
                    // Se envia al sir cuando se finalice y esten tramitados los documentos para que no de error de contexto en Registro
                    if ("1".equalsIgnoreCase(registroConf.getString("INTEGRACION_SIR_LANBIDE"))) {
                        try {
                            if (elRegistroESVO.getCodigoUnidadDestinoSIR() == null || elRegistroESVO.getCodigoUnidadDestinoSIR().isEmpty()) {
                                String codUnidadDestinoSIRHidden = request.getParameter("codigoUnidadDestinoSIRHidden");
                                if (codUnidadDestinoSIRHidden != null)
                                    elRegistroESVO.setCodigoUnidadDestinoSIR(codUnidadDestinoSIRHidden);
                            }
                            if (elRegistroESVO.getUorCodVisible() == null || elRegistroESVO.getUorCodVisible().isEmpty()) {
                                String cod_uniRegDestinoORD = request.getParameter("cod_uniRegDestinoORD");
                                if (cod_uniRegDestinoORD != null) {
                                    String codUnidadVisible = UORsManager.getInstance().getCodigoVisibleUorByCodUor(cod_uniRegDestinoORD, params);
                                    elRegistroESVO.setUorCodVisible(codUnidadVisible);
                                }
                            }
                            if ("S".equalsIgnoreCase(elRegistroESVO.getTipoReg()) && elRegistroESVO.getTipoAnot() == 1) {
                                SirRegistroSalidaResponse sirRegistroSalidaResponse
                                        = registroSIRService.enviarSalidaSistemaSir(elRegistroESVO, usuarioVO.getIdioma(), params, cod_dep, cod_uni, false);
                                elRegistroESVO.setCodEstadoRespGestionSIR(sirRegistroSalidaResponse.getCodEstadoRespGestionSIR());
                                elRegistroESVO.setDescEstadoRespGestionSIR(sirRegistroSalidaResponse.getDescEstadoRespGestionSIR());

                            } else if ("E".equalsIgnoreCase(elRegistroESVO.getTipoReg()) && elRegistroESVO.getTipoAnot() == 1) {
                                if (elRegistroESVO.isFinDigitalizacion()) {
                                    m_Log.info("Proceso de anexo de documentacion finalizado - Alta - Enviamos registro al SIR ");
                                    if (elRegistroESVO.getUorCodVisible() == null || elRegistroESVO.getUorCodVisible().isEmpty()) {
                                        String codigoVisible = UORsManager.getInstance().getCodigoVisibleUorByCodUor(elRegistroESVO.getIdUndTramitad(), params);
                                        elRegistroESVO.setUorCodVisible(codigoVisible);
                                    }
                                    SirRegistroSalidaResponse sirRegistroSalidaResponse
                                            = registroSIRService.enviarEntradaSistemaSir(elRegistroESVO, usuarioVO.getIdioma(), params, cod_dep, cod_uni, true, usuarioVO);
                                    elRegistroESVO.setCodEstadoRespGestionSIR(sirRegistroSalidaResponse.getCodEstadoRespGestionSIR());
                                    elRegistroESVO.setDescEstadoRespGestionSIR(sirRegistroSalidaResponse.getDescEstadoRespGestionSIR());
                                } else {
                                    m_Log.info("Envio al SIR Activado pero Proceso de anexo de documentacion AUN NO finalizado - No Enviamos registro al SIR ");
                                }
                            }
                        } catch (Exception ex) {
                            m_Log.error("Error al Crear la salida de registro dada de alta en Regexlan en el SIR - creandoSalidas: " + ex.getMessage(), ex);
                            elRegistroESVO.setCodEstadoRespGestionSIR("msgErrorNoDadaAltaSIR");
                            elRegistroESVO.setDescEstadoRespGestionSIR(MessageFormat.format(
                                    traductorAplicacionBean.getDescripcion("msgErrorNoDadaAltaSIR"),
                                    ex.getMessage()
                            ));
                            m_Log.info(session.getId() + " " + usuarioActualLog + " <================= MantAnotacionRegistroAction ======================");
                        }
                    } else {
                        m_Log.info("INTEGRACION_SIR_LANBIDE - Alta - No activo o no configurado en el fichero Registro.properties");
                    }

                    if ("registro_cerrado".equals(elRegistroESVO.getRespOpcion())
                            || "registrar_alta_sin_confirmar".equals(elRegistroESVO.getRespOpcion())
                            || "no_existe_expediente".equals(elRegistroESVO.getRespOpcion())
                            || "proc_mal_relacionado".equals(elRegistroESVO.getRespOpcion())) {
                        opcion = "init_alta_entrada"; // Otro valor => cambiar struts_config.xml
                    } else if ("actualizacion_ya_realizada".equals(elRegistroESVO.getRespOpcion())) {
                        opcion = "actualizacion_ya_realizada";//la actualizaciÃ³n del buzÃ³n ya la realizÃ³ otro usuario
                    } else if ("registrar_alta_entrada_denegada".equals(elRegistroESVO.getRespOpcion())) {
                        opcion = "error";
                        elRegistroESVO.setRespOpcion(opcion);
                    } else {
                        elRegistroESVO.setRespOpcion("alta_entrada_registrada");
                        if (opcion.equals("registrar_alta_tramitar")) {
                            elRegistroESVO.setRespOpcion("alta_tramitar_registrada");
                        }
                        // Se envian los correos para notificar el alta del asiento a las unidades
                        // organicas especificadas
                        notificarUnidadesOrganicas(registroForm);
                        opcion = "init_alta_entrada"; // Otro valor => cambiar struts_config.xml
                    }

                } catch (Exception ce) {
                    m_Log.error("MantAnotacionRegistroAction problem" + ce.getMessage());
                    opcion = "error";
                    elRegistroESVO.setRespOpcion(opcion);
                }

                /* --------------------------------  FIN: REGISTRAR ALTA ENTRADA/SALIDA, DUPLICAR... ------------------------------------------- */
            } else if ("buscar".equals(opcion) || "iniciar_duplicar".equals(opcion) || "contestar".equals(opcion)
                    || "responder".equals(opcion) || "relacionar".equals(opcion) || "cancelar_contestar".equals(opcion)) {

                session.removeAttribute("BusquedaTercerosForm");
                String reservas = request.getParameter("reservas");

                elRegistroESVO = new RegistroValueObject();

                elRegistroESVO.setRespOpcion(opcion);
                elRegistroESVO.setIdOrganizacion(cod_org);
                elRegistroESVO.setIdEntidad(cod_ent);
                elRegistroESVO.setIdentDepart(cod_dep);
                elRegistroESVO.setUnidadOrgan(cod_uni);

                // Manteniendo numero de pagina, y numero de registros por pagina
                elRegistroESVO.setPaginaListado(registroForm.getPaginaListado());
                elRegistroESVO.setNumLineasPaginaListado(registroForm.getNumLineasPaginaListado());

                /**
                 *                  */
                m_Log.debug(" ************** buscar POSICION_ANOTACION registroForm: "
                        + registroForm.getRegistro().getPosicionAnotacion());
                if (registroForm.getRegistro() != null && registroForm.getRegistro().getPosicionAnotacion() != null) {
                    elRegistroESVO.setPosicionAnotacion(registroForm.getRegistro().getPosicionAnotacion());
                } else {
                    elRegistroESVO.setPosicionAnotacion(request.getParameter("posicionAnotacion"));
                }
                /**/

                // Adaptado para SALIDA -------------------------------------------
                String tipo = (String) session.getAttribute("tipoAnotacion");
                m_Log.info(session.getId() + " " + usuarioActualLog + " Tipo de anotacion: " + tipo);
                if (tipo != null) {
                    if ("E".equals(tipo) || "Relacion_E".equals(tipo)) {
                        elRegistroESVO.setTipoReg("E");
                    } else {
                        elRegistroESVO.setTipoReg("S");
                    }
                } else {
                    elRegistroESVO.setTipoReg("E");
                }
                //TODO comprobar nulidad ejercicio
                String ejercicio = request.getParameter("ano");
                if (ejercicio != null && !ejercicio.trim().isEmpty()) {
                    m_Log.info(session.getId() + " " + usuarioActualLog + " Ejercicio (request): " + ejercicio);
                    elRegistroESVO.setAnoReg(Integer.parseInt(ejercicio.trim()));
                } else {
                    m_Log.warn(session.getId() + " " + usuarioActualLog + " Ejercicio nulo o vacío");
                }
                // numero de registro
                //TODO comprobar nulidad numero
                String numero = request.getParameter("numero");
                if (numero != null && !numero.trim().isEmpty()) {
                    m_Log.info(session.getId() + " " + usuarioActualLog + " Numero (request): " + numero);
                    elRegistroESVO.setNumReg(Long.parseLong(numero.trim()));
                } else {
                    m_Log.warn(session.getId() + " " + usuarioActualLog + " Numero nulo o vacío");
                }
                // Buscamos el asiento por su clave
                elRegistroESVO = AnotacionRegistroManager.getInstance().getByPrimaryKey(elRegistroESVO, params);

                //Buscamos los datos para el bloque sga en los casos que se carga este bloque	
                if (registroConf.getString("mostrar_datos_sga").toUpperCase().equals("SI")) {
                    String[] datosSga = AnotacionRegistroManager.getInstance().getDatosSga(elRegistroESVO, params);
                    elRegistroESVO.setCodigoSga(datosSga[0]);
                    elRegistroESVO.setExpedienteSga(datosSga[1]);
                    m_Log.info(session.getId() + " " + usuarioActualLog + " --------->Codigo Sga:" + elRegistroESVO.getCodigoSga());
                    m_Log.info(session.getId() + " " + usuarioActualLog + " ---------->Expediente Sga:" + elRegistroESVO.getExpedienteSga());
                }
                m_Log.debug("LA ANOTACION ES: " + elRegistroESVO);
                // #guardamos en el form de la anotacion el valor de si el asunto permite o no interesados sin doc
                registroForm.setTipoDocInteresadoOblig(elRegistroESVO.isTipoDocInteresadoOblig());

                // se guarda en el form de la anotaci?n si el asunto bloquea o no la opci?n de modificar la unidad de destino y procedimiento
                registroForm.setBloquearDestino(elRegistroESVO.isBloquearDestino());
                registroForm.setBloquearProcedimiento(elRegistroESVO.isBloquearProcedimiento());
                if (opcion.equals("iniciar_duplicar")) {
                    m_Log.debug("Borrando datos de contestar");
                    elRegistroESVO.setEjercicioAnotacionContestada(null);
                    elRegistroESVO.setNumeroAnotacionContestada(null);
                }

                // Comprobaciï¿½n de si la anotaciï¿½n de registro se ha dado de alta desde el servicio web WSRegistroES
                registroForm.setRegistroTelematico(elRegistroESVO.isRegistroTelematico());
                // Recuperamos la propiedad REGISTRO_TELEMATICO_MODIFICABLE. Si la propiedad es si y el registro no tiene expedientes asociados indicamos que el registro puede ser modificado
                boolean regTelemMod = false;
                String propRegTelemMod = usuarioVO.getOrgCod() + "/REGISTRO_TELEMATICO_MODIFICABLE";
                if (elRegistroESVO.isRegistroTelematico() && (elRegistroESVO.getNumExpedientesRelacionados() != null && elRegistroESVO.getNumExpedientesRelacionados().isEmpty() || elRegistroESVO.getNumExpedientesRelacionados() == null)) {
                    try {
                        String valor = registroConf.getString(propRegTelemMod);
                        m_Log.debug("Valor de la propiedad " + propRegTelemMod + " de Registro.properties: " + valor);
                        if (valor != null) {
                            if (valor.equalsIgnoreCase("si") || elRegistroESVO.getCodAsunto().equals(valor)) {
                                regTelemMod = true;
                            } else if (valor.contains(";")) {
                                String[] codigos = valor.split(";");
                                for (String codigoAsunto : codigos) {
                                    if (elRegistroESVO.getCodAsunto().equals(codigoAsunto)) {
                                        regTelemMod = true;
                                        break;
                                    }
                                }
                            }
                        }
                        registroForm.setRegistroTelematicoModicable(regTelemMod);
                    } catch (Exception ex) {
                        m_Log.debug("No se ha podido recuperar la propiedad " + propRegTelemMod + " de Registro.properties");
                    }
                }
                m_Log.debug("Es registro telematico modificable? " + registroForm.isRegistroTelematicoModicable());

                // TODO cambiado para controlar UOR de baja en destino
                registroForm.setCod_uniRegDestinoORD(elRegistroESVO.getIdUndTramitad());
                request.setAttribute("codigo_uor_real", registroForm.getCod_uniRegDestinoORD());

                //Para buscar en la tabla diligencias
                String fechaEnt = elRegistroESVO.getFecEntrada();
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("la fecha de entrada en el action es : " + fechaEnt);
                }
                DiligenciasValueObject diligencia = new DiligenciasValueObject();
                diligencia.setCodDepto(elRegistroESVO.getIdentDepart());
                diligencia.setCodUnidad(elRegistroESVO.getUnidadOrgan());
                diligencia.setTipo(elRegistroESVO.getTipoReg().charAt(0));
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("el tipo de registro en el diligencia es : " + diligencia.getTipo());
                }
                if (fechaEnt != null) {
                    String fechaEnt1 = fechaEnt.substring(0, 10);
                    diligencia.setFecha(fechaEnt1);
                    diligencia = DiligenciasManager.getInstance().load(diligencia, params);
                    String textoDil = diligencia.getAnotacion();
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("el texto de la diligencia en el action es : " + textoDil);
                    }
                    elRegistroESVO.setTextoDiligencia(textoDil);
                    if (!textoDil.equals("") && textoDil != null) {
                        elRegistroESVO.setHayTexto("si");
                    } else {
                        elRegistroESVO.setHayTexto("no");
                    }
                    session.removeAttribute("DiligenciasForm");
                }
                // Fin del buscar en la tabla diligencias

                // tercero
                Vector tercero;
                if (!("1".equals(reservas))) {

                    Date fechaRegCerrado = RegistroAperturaCierreManager.getInstance().getFechaRegistroCerrado(elRegistroESVO, params);

                    String fechaEntrada = elRegistroESVO.getFecEntrada();
                    Date fechaEntrada1 = Fecha.obtenerDate(fechaEntrada);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("la fecha de entrada es **************^^^^^^^^^^^^^^^^ : " + fechaEntrada1);
                    }
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("la fecha del registro cerrado es **************^^^^^^^^^^^^^^^^ : " + fechaRegCerrado);
                    }
                    if (fechaRegCerrado == null) {
                        elRegistroESVO.setAbiertCerrado("abierto");
                    } else if (fechaEntrada1 == null) {
                        elRegistroESVO.setAbiertCerrado("cerrado");
                    } else if (fechaRegCerrado.compareTo(fechaEntrada1) >= 0) {
                        elRegistroESVO.setAbiertCerrado("cerrado");
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(elRegistroESVO.getAbiertCerrado());
                        }
                    } else {
                        elRegistroESVO.setAbiertCerrado("abierto");
                    }
                }
                String fallo = elRegistroESVO.getFallo();
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("el fallo en el action es : " + fallo);
                }
                String modificar = request.getParameter("modificar");
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("el numero de modificar es: " + modificar);
                }

                if ("bien".equals(fallo)) {
                    // Recuperamos los terceros asociados a la anotaciÃ³n.
                    BusquedaTercerosForm btForm = new BusquedaTercerosForm();
                    btForm.setListaInteresados(getListaInteresados(elRegistroESVO, params));

                    // Hay que pasar tambien el codigo y descripcion del rol por defecto
                    // Si hay procedimiento obtenemos sus roles, si no los del grupo de roles.
                    String proc = elRegistroESVO.getCodProcedimientoRoles();
                    Vector<GeneralValueObject> roles = new Vector<GeneralValueObject>();
                    if (proc != null && !proc.equals("") && !proc.equals("null")) {
                        GeneralValueObject procGVO = new GeneralValueObject();
                        procGVO.setAtributo("codMunicipio", elRegistroESVO.getMunProcedimiento());
                        procGVO.setAtributo("codProcedimiento", proc);
                        roles = obtenerRolesProcedimiento(procGVO, params);
                    } else {
                        roles = InteresadosManager.getInstance().getListaRolesRegistro(params);
                    }
                    // Comprobamos cual es el codigo del rol por defecto
                    for (GeneralValueObject rol : roles) {
                        if (rol.getAtributo("porDefecto").equals("SI")) {
                            registroForm.setCodRolDefecto((String) rol.getAtributo("codRol"));
                            registroForm.setDescRolDefecto((String) rol.getAtributo("descRol"));
                            break;
                        }
                    }
                    registroForm.setListaRoles(roles);

                    m_Log.debug("Rol por defecto cargado: codigo=" + registroForm.getCodRolDefecto()
                            + ", descripcion=" + registroForm.getDescRolDefecto());

                    tercero = getTerceroPrincipal(elRegistroESVO, params);
                    TercerosValueObject terc = (TercerosValueObject) tercero.firstElement();

                    if (terc != null && !terc.getIdentificador().equals("0")) {

                        btForm.setListaTerceros(tercero);
                        session.setAttribute("BusquedaTercerosForm", btForm);
                        Fecha f;

                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug("modificar es: " + modificar);
                        }
                        if ("1".equals(modificar)) {
                            if ("iniciar_duplicar".equals(opcion)) {

                                /* ANHADIDO PARA SOLUCIONAR INCIDENCIA DEL 8/08/2006 */
                                String tipo_Entrada = m_Conf.getString("FechaAltaRegistro");
                                if ((elRegistroESVO.getTipoReg().equals("E")) && (tipo_Entrada.equals("0"))) {
                                    f = new Fecha();
                                    Date fSistema = new Date();
                                    String hora = f.construirHora(fSistema);
                                    elRegistroESVO.setFecEntrada(Fecha.obtenerString(fSistema) + " " + hora);
                                } else {
                                    request.setAttribute("fechaAnotacion", "");
                                }

                                f = new Fecha();
                                Date fechaHoraActual = new Date();
                                String fechaHoraTxt = f.construirFechaCompleta(fechaHoraActual);
                                elRegistroESVO.setFecEntrada(fechaHoraTxt);
                                elRegistroESVO.setFecHoraDoc(fechaHoraTxt);
                                m_Log.debug("Cambiada la fecha del registroVO a " + fechaHoraTxt);

                                elRegistroESVO.setRespOpcion("iniciarDuplicar");
                                inicializarListas(elRegistroESVO, false, params); // Cargar listas                              

                                opcion = "buscar"; //

                            } else if ("contestar".equals(opcion) || "responder".equals(opcion) || "relacionar".equals(opcion)) {

                                // Insertamos en el VO relaciÃ³n con el asiento contestado/respondido/relacionado
                                SimpleRegistroValueObject relacion = new SimpleRegistroValueObject();
                                relacion.setUor(Integer.toString(elRegistroESVO.getUnidadOrgan()));
                                relacion.setDep(Integer.toString(elRegistroESVO.getIdentDepart()));
                                relacion.setTipo(elRegistroESVO.getTipoReg());
                                relacion.setEjercicio(Integer.toString(elRegistroESVO.getAnoReg()));
                                relacion.setNumero(Long.toString(elRegistroESVO.getNumReg()));
                                Vector<SimpleRegistroValueObject> relaciones = new Vector<SimpleRegistroValueObject>();
                                relaciones.add(relacion);
                                elRegistroESVO.setRelaciones(relaciones);

                                // Para los casos contestar y responder solo se copia el
                                // asunto si ha sido definido para ambos registros.
                                if ("contestar".equals(opcion) || "responder".equals(opcion)) {
                                    String codAsunto = elRegistroESVO.getCodAsunto();
                                    if (!"".equals(codAsunto) && codAsunto != null) {
                                        MantAsuntosValueObject asunto = new MantAsuntosValueObject();
                                        asunto.setCodigo(codAsunto);
                                        asunto.setUnidadRegistro(registroForm.getUniRegAsunto());
                                        asunto = MantAsuntosManager.getInstance().cargarAsunto(asunto, params);
                                        // Borramos si no es para ambos
                                        if (!asunto.getTipoRegistro().equals("A")) {
                                            elRegistroESVO.setCodAsunto("");
                                        }
                                    }
                                }

                                if ("contestar".equals(opcion)) {
                                    // Recarga jsp
                                    opcion = "cancelar_anular";
                                    elRegistroESVO.setTipoReg("S");
                                    if ("E".equals(tipo)) {
                                        session.setAttribute("tipoAnotacion", "S");
                                    } else {
                                        session.setAttribute("tipoAnotacion", "Relacion_S");
                                    }
                                    session.setAttribute("tipoAnotacionBuscada", "E");
                                } else if ("responder".equals(opcion)) {
                                    // Recarga jsp
                                    opcion = "cancelar_anular";
                                    elRegistroESVO.setTipoReg("E");
                                    if ("S".equals(tipo)) {
                                        session.setAttribute("tipoAnotacion", "E");
                                    } else {
                                        session.setAttribute("tipoAnotacion", "Relacion_E");
                                    }
                                    session.setAttribute("tipoAnotacionBuscada", "S");
                                } else {
                                    // Relacionar, no es necesario recargar altaRE
                                    opcion = "buscar";
                                    elRegistroESVO.setRespOpcion("relacionar");
                                    session.setAttribute("tipoAnotacion", tipo);
                                    session.setAttribute("tipoAnotacionBuscada", elRegistroESVO.getTipoReg());
                                }

                                f = new Fecha();
                                Date fEntr = new Date();
                                String hora = f.construirHora(new Date());
                                session.setAttribute("ejercicioAnotacionBuscada", ejercicio);
                                session.setAttribute("numeroAnotacionBuscada", numero);
                                elRegistroESVO.setAnoReg(0);
                                elRegistroESVO.setNumReg(0L);
                                elRegistroESVO.setFecEntrada(Fecha.obtenerString(fEntr) + " " + hora);
                                elRegistroESVO.setFecHoraDoc(Fecha.obtenerString(fEntr) + " " + hora);
                                elRegistroESVO.setTipoAnot(0);

                                // Datos tercero.
                                if (m_Log.isDebugEnabled()) {
                                    m_Log.debug("TipoDoc" + terc.getTipoDocumento());
                                }
                                //int tInter= (terc.getTipoDocumento().equals(""))?0:Integer.parseInt(terc.getTipoDocumento());
                                String tInter = (terc.getTipoDocumento().equals("")) ? "0" : terc.getTipoDocumento();
                                elRegistroESVO.setTipoDocInteresado(tInter);
                                inicializarListas(elRegistroESVO, false, params);
                                session.setAttribute("modoInicio", "contestar_entrada");

                            } else if ("cancelar_contestar".equals(opcion)) {
                                elRegistroESVO.setTipoReg("S");
                                session.setAttribute("tipoAnotacion", "E");
                                session.setAttribute("modoInicio", "recargar_buscada");
                                opcion = "cancelar_anular"; // Recarga jsp
                                // No hay que recargar las listas.
                            } else { // buscar

                                elRegistroESVO.setRespOpcion("modificar");
                                elRegistroESVO.setNumExpedienteBD(elRegistroESVO.getNumExpediente());

                                // Cargar listas.
                                inicializarListas(elRegistroESVO, false, params);

                                // #120581: obtener fecha baja tipo doc	
                                String pulsarModificar = request.getParameter("pulsarModificar");
                                if (pulsarModificar != null && pulsarModificar.equals("1")) {
                                    Calendar calAltaAnot = Calendar.getInstance();
                                    Calendar calBajaDoc = null;
                                    String txtCodDoc = registroForm.getTxtCodigoDocumento();
                                    MantTemasValueObject temaVO = MantDocumentoManager.getInstance().getTemaByCod(txtCodDoc, params);
                                    if (temaVO.getFecha() != null && !temaVO.getFecha().equals("")) {
                                        calBajaDoc = Calendar.getInstance();
                                        calBajaDoc.setTime(Fecha.obtenerDate(temaVO.getFecha()));
                                    }
                                    calAltaAnot.setTime(Fecha.obtenerDate(registroForm.getFechaAnotacion()));
                                    if (calBajaDoc != null && calBajaDoc.after(calAltaAnot)) {
                                        request.setAttribute("esFechaBajaPosterior", "1");
                                        request.setAttribute("tipoDocumentoPreSel", temaVO);
                                    }
                                    request.setAttribute("pulsarModificar", pulsarModificar);

                                    // Despues de cargar la jsp se situa sobre la pestana indicada
                                    String irAPestana = request.getParameter("irAPestana");
                                    request.setAttribute("irAPestana", irAPestana);
                                }
                                // #239565
                                String generarPeticion = registroConf.getString(usuarioVO.getOrgCod() + "/MODELO_PETICION_RESPUESTA");
                                registroForm.setMostrarGenerarModelo(generarPeticion);
                            }
                        } else {
                            inicializarListasVacias(elRegistroESVO);
                        }
                    } else {
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug("el dia de anotacion en el action es: " + elRegistroESVO.getDiaAnotacion());
                        }
                        if ("1".equals(modificar)) {
                            elRegistroESVO.setRespOpcion("modificar");
                            // Cargar listas.
                            inicializarListas(elRegistroESVO, false, params);

                        } else {
                            inicializarListasVacias(elRegistroESVO);
                        }
                        if ("1".equals(reservas)) {
                            // Se va a dar de alta una anotacion reservada.
                            session.removeAttribute("BusquedaTercerosForm");
                            String ano = request.getParameter("ano");
                            if (ano != null && !ano.trim().isEmpty()) {
                                m_Log.info(session.getId() + " " + usuarioActualLog + " ano (request): " + ano);
                                elRegistroESVO.setAnoReg(Integer.parseInt(ejercicio.trim()));
                            } else {
                                m_Log.warn(session.getId() + " " + usuarioActualLog + " ano nulo o vacío");
                            }

                            String num = request.getParameter("num");
                            if (num != null && !num.trim().isEmpty()) {
                                m_Log.info(session.getId() + " " + usuarioActualLog + " Num (request): " + num);
                                elRegistroESVO.setNumReg(Long.parseLong(num.trim()));
                            } else {
                                m_Log.warn(session.getId() + " " + usuarioActualLog + " Num nulo o vacío");
                            }
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("EL NUMERO DE LA ANOTACION ES:" + elRegistroESVO.getAnoReg() + "/" + elRegistroESVO.getNumReg());
                            }

                            String res = "reservas";
                            elRegistroESVO.setRespOpcion(res);
                            session.setAttribute("reservas", res);

                            registroForm.setRegistro(elRegistroESVO);
                            //m_Log.info("Salida de opcion buscar/iniciar_duplicar/contestar/cancelar_contestar por iniciar");
                            m_Log.info(session.getId() + " " + usuarioActualLog + " Opcion antes de dejar MantAnotacionRegistroAction: " + opcion);
                            m_Log.debug("<================= MantAnotacionRegistroAction ======================");

                            // Leemos las propiedades de configuraciÃ³n del fichero
                            // que serÃ¡n pasadas a la pÃ¡gina jsp.
                            String mostrarCuneus = registroConf.getString(REG_DUPLICAR_RESERVAS_MOSTRAR_CUNEUS);
                            request.setAttribute("mostrarCuneus", mostrarCuneus);

                            return (mapping.findForward("cargarAltaRE"));

                        } else {
                            ReservaOrdenValueObject reserva = new ReservaOrdenValueObject();
                            ReservaOrdenManager.getInstance().loadRER(reserva, params);
                            String fecha = reserva.getFec();
                            elRegistroESVO.setFecHoraDoc(fecha);
                            registroForm.setRegistro(elRegistroESVO);
                            //m_Log.info("Salida de opcion buscar/iniciar_duplicar/contestar/cancelar_contestar por noTercero");
                            m_Log.info(session.getId() + " " + usuarioActualLog + " Opcion antes de dejar MantAnotacionRegistroAction: " + opcion);
                            m_Log.debug("<================= MantAnotacionRegistroAction ======================");
                            return (mapping.findForward("noTercero"));
                        }
                    }
                    // Auditoria de acceso al registro
                    if ("buscar".equals(opcion)) {
                        AuditoriaManager.getInstance().auditarAccesoRegistro(
                                ConstantesAuditoria.REGISTRO_VER_ANOTACION, usuarioVO, elRegistroESVO);
                    }

                } else {
                    //m_Log.info("Salida de opcion buscar/iniciar_duplicar/contestar/cancelar_contestar por noHay");
                    m_Log.info(session.getId() + " " + usuarioActualLog + " ----------------------------OpciÃ³n antes de dejar MantAnotacionRegistroAction: " + opcion);
                    m_Log.debug("<================= MantAnotacionRegistroAction ======================");
                    return mapping.findForward("noHay");
                }

                m_Log.info(session.getId() + " " + usuarioActualLog + "  +++++++++++++++++++++++++++++        MantAnotacionRegistroAction ----> opcion" + opcion);

            } else if ("recuperarDocumentos".equals(opcion)) {

                //FIN DE BUSCAR, INICIAR_DUPLICAR, CONTESTAR
            } else if ("salida_desde_tramitar".equals(opcion)) {

                // VO de la anotacion
                elRegistroESVO = new RegistroValueObject();
                elRegistroESVO.setIdOrganizacion(cod_org);
                elRegistroESVO.setIdEntidad(cod_ent);
                elRegistroESVO.setIdentDepart(cod_dep);
                elRegistroESVO.setUnidadOrgan(cod_uni);
                elRegistroESVO.setAnoReg(0);
                elRegistroESVO.setNumReg(0L);
                elRegistroESVO.setTipoAnot(0);
                elRegistroESVO.setTipoReg("S");
                session.setAttribute("tipoAnotacion", "S");
                session.setAttribute("tipoAnotacionBuscada", "S");

                // Datos usuario que registra
                elRegistroESVO.setUsuarioQRegistra(usuarioQRegistra);
                elRegistroESVO.setDptoUsuarioQRegistra(dptoUsuarioQRegistra);
                elRegistroESVO.setUnidOrgUsuarioQRegistra(unidOrgUsuarioQRegistra);

                // Fechas del servidor
                Fecha f = new Fecha();
                Date fSistema = new Date();
                String hora = f.construirHora(fSistema);
                elRegistroESVO.setFecEntrada(Fecha.obtenerString(fSistema) + " " + hora);
                elRegistroESVO.setFecHoraDoc(Fecha.obtenerString(fSistema) + " " + hora);

                // Valores de combos y valor por defecto de tipo de doc del asiento
                inicializarListas(elRegistroESVO, false, params);
                elRegistroESVO.setListaTemasAsignados(new Vector());
                for (ElementoListaValueObject doc : (Vector<ElementoListaValueObject>) elRegistroESVO.getListaTiposDocumentos()) {
                    if (doc.getActivo() != null && doc.getActivo().equals("SI")) {
                        elRegistroESVO.setCodTipoDoc(doc.getCodigo());
                        elRegistroESVO.setDescTipoDoc(doc.getDescripcion());
                        break;
                    }
                }

                // Carga de los datos del tramite/expediente desde el que se genera
                TramitacionExpedientesForm tramForm
                        = (TramitacionExpedientesForm) session.getAttribute("TramitacionExpedientesForm");

                // Procedimiento y expediente relacionado             
                elRegistroESVO.setNumExpediente(tramForm.getNumeroExpediente());
                elRegistroESVO.setCodProcedimiento(tramForm.getCodProcedimiento());
                elRegistroESVO.setDescProcedimiento(tramForm.getProcedimiento());

                // Interesados = interesados del expediente
                session.removeAttribute("BusquedaTercerosForm");
                BusquedaTercerosForm BTForm = new BusquedaTercerosForm();

                GeneralValueObject gVOExp = new GeneralValueObject();
                gVOExp.setAtributo("ejercicio", tramForm.getEjercicio());
                gVOExp.setAtributo("numero", tramForm.getNumeroExpediente());
                gVOExp.setAtributo("codProcedimiento", tramForm.getCodProcedimiento());
                gVOExp.setAtributo("codMunicipio", tramForm.getCodMunicipio());

                Vector<GeneralValueObject> listaInteresados
                        = obtenerInteresadosProcedimiento(gVOExp, elRegistroESVO, registroForm, params);

                BTForm.setListaInteresados(listaInteresados);
                session.setAttribute("BusquedaTercerosForm", BTForm);

                // Hay que pasar tambien el codigo y descripcion del rol por defecto
                // del procedimiento.
                Vector<GeneralValueObject> roles = obtenerRolesProcedimiento(gVOExp, params);

                // Comprobamos cual es el codigo del rol por defecto
                for (GeneralValueObject rol : roles) {
                    if (rol.getAtributo("porDefecto").equals("SI")) {
                        registroForm.setCodRolDefecto((String) rol.getAtributo("codRol"));
                        registroForm.setDescRolDefecto((String) rol.getAtributo("descRol"));
                        break;
                    }
                }
                registroForm.setListaRoles(roles);
                m_Log.info(session.getId() + " " + usuarioActualLog + " Rol por defecto cargado: codigo=" + registroForm.getCodRolDefecto()
                        + ", descripcion=" + registroForm.getDescRolDefecto());

                // Extracto = nombre del tramite
                elRegistroESVO.setAsunto(tramForm.getTramite());

                // Unidad de origen = unidad de inicio del expediente
                String codUor = tramForm.getCodUnidadOrganicaExp();
                m_Log.info(session.getId() + " " + usuarioActualLog + " codUor del expediente " + codUor);
                elRegistroESVO.setIdUndTramitad(codUor);
                request.setAttribute("codigo_uor_real", codUor);
                UORDTO unidadInicio = buscarUor(codUor, nuevasUOR);
                if (unidadInicio != null) {
                    elRegistroESVO.setUorCodVisible(unidadInicio.getUor_cod_vis());
                    elRegistroESVO.setNomUniDestinoOrd(unidadInicio.getUor_nom());
                }

                // Documentos del tramite
                elRegistroESVO.setListaDocsAsignados(
                        TramitacionExpedientesManager.getInstance().cargarDocumentosTramite(
                                tramForm.getTramitacionExpedientes(), params));

                elRegistroESVO.setListaDocsAnteriores(new Vector());

                // Relaciones del asiento
                elRegistroESVO.setRelaciones(new Vector());

                // Opcion para cargar altaRE, se controla que se cargue en modo de
                // alta mediante respOpcion y modoInicio.
                opcion = "cargarAltaRE";
                elRegistroESVO.setRespOpcion(opcion);
                session.setAttribute("modoInicio", "alta_desde_tramitar");

                // FIN DE ALTA DE SALIDA DESDE TRAMITAR
            } else if ("grabarModificaciones".equals(opcion) || "grabarModificacionesConfirmada".equals(opcion)) {

                RegistroValueObject registro = registroForm.getRegistro();
                //registro.setOpcionPermanencia(registroForm.getOpcionPermanencia());
                registro.setOpcionPermanencia(getOpcionPermanencia(registro.getTipoRegOrigen(), registroForm.getTxtExp1(), session));
                registro.setUsuarioQRegistra(usuarioQRegistra);
                registro.setDptoUsuarioQRegistra(dptoUsuarioQRegistra);
                registro.setUnidOrgUsuarioQRegistra(unidOrgUsuarioQRegistra);

                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                ResourceBundle expRegistro = ResourceBundle.getBundle("Registro");
                String propiedad = usuario.getOrgCod() + "/EliminarCamposTrasCambiarTEntrada";
                String eliminarCamposTrasCambiarTEntrada = expRegistro.getString(propiedad);

                if ((registro.getTipoAnot() == 1) &&
                        (eliminarCamposTrasCambiarTEntrada == null || (eliminarCamposTrasCambiarTEntrada != null && !"0".equals(eliminarCamposTrasCambiarTEntrada)))) {
                    registro.setNumExpediente("");
                    registro.setCodProcedimiento("");
                }

                // Cambiar de estado rechazado a pendiente.
                if (registro.getEstAnotacion() == 2) {
                    registro.setEstAnotacion(0);
                    m_Log.info(session.getId() + " " + usuarioActualLog + " MantAnotacionRegistroAction: CAMBIO DEL ESTADO DE LA ANOTACION DE RECHAZADO A PENDIENTE");
                }

            registro.setRespOpcion(opcion);
            String listTemasSelecc = request.getParameter("listaTemas");
            Vector v = listaTemasSeleccionados(listTemasSelecc);
            Vector<RegistroValueObject> temas = new Vector<RegistroValueObject>();

            for (int j = 0; j < v.size(); j++) {
                RegistroValueObject reg = new RegistroValueObject();
                String s = (String) v.elementAt(j);
                reg.setCodigoTema(s);
                temas.addElement(reg);
            }
            registro.setListaTemasAsignados(temas);

            // Fecha de grabacion
            String fechaGrabacion = registroForm.getFechaDocumento();
            if (fechaGrabacion == null || "".equalsIgnoreCase(fechaGrabacion)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar fecha = Calendar.getInstance();
                fechaGrabacion = sdf.format(fecha.getTime());
            }//if(fechaGrabacion == null || "".equalsIgnoreCase(fechaGrabacion))
            registro.setFecHoraDoc(fechaGrabacion);
            m_Log.info(session.getId() + " " + usuarioActualLog + " Fecha de grabacion: " + fechaGrabacion);

            // Fecha de presentacion
            String fechaPresentacion = "";
            String horaMinPresentacion = request.getParameter("horaMinAnotacion");
            if (registro.getContador() == 0) {
                fechaPresentacion = request.getParameter("fechaAnotacion");
            } else {
                fechaPresentacion = request.getParameter("fecPresRes");
            }
            if (fechaPresentacion == null) {
                fechaPresentacion = registroForm.getFechaAnotacion();
            }

            // Si la hora esta en blanco ponemos la actual, independientemente de la fecha
            if (horaMinPresentacion == null || "".equals(horaMinPresentacion)) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                horaMinPresentacion = parser.format(calendar.getTime());
            }
            String fechaHoraPresentacion = fechaPresentacion + " " + horaMinPresentacion;

            // Comprobamos fecha y hora presentacion <= fecha y hora actual
            Date fechaAnotacion = Fecha.obtenerDateCompleto2(fechaHoraPresentacion);
            Date fechaActual = new Date();
            if (fechaActual.compareTo(fechaAnotacion) < 0) {
                m_Log.info(session.getId() + " " + usuarioActualLog + " ALERTAR La fecha de entrada es posterior a la fecha actual : " + fechaActual + " < " + fechaAnotacion);
                registro.setRespOpcion("fecha_posterior_a_actual");
                registroForm.setRegistro(registro);
                m_Log.info(session.getId() + " " + usuarioActualLog + " <================= MantAnotacionRegistroAction ======================");
                return mapping.findForward("modifyHecha");
            }
            int year = -1;
            registro.setFecEntrada(fechaHoraPresentacion);
            String grabarDesdeReserva = (String) session.getAttribute("grabarDesdeReserva");
            if ("1".equals(grabarDesdeReserva)) {
                year = Integer.parseInt(registroForm.getAnoAnotacion());
                session.removeAttribute("grabarDesdeReserva");
                registro.setAnoReg(year);
            }

            m_Log.info(session.getId() + " " + usuarioActualLog + " Fecha de presentacion: " + fechaHoraPresentacion);

            // Fecha de documento
            String fechaDocumento = request.getParameter("fechaDoc");
            registro.setFechaDocu(fechaDocumento);
            m_Log.info(session.getId() + " " + usuarioActualLog + " Fecha de documento: " + fechaDocumento);

            session.removeAttribute("BusquedaTercerosForm");

            if ("grabarModificacionesConfirmada".equals(opcion)) {
                registro.setRespOpcion("actualizacion_confirmada");
            } else {
                registro.setRespOpcion("actualizacion_sin_confirmar");
            }

            //recojo los datos para insertar los interesados.
            String CodTercero = request.getParameter("listaCodTercero");
            String VersionTercero = request.getParameter("listaVersionTercero");
            String CodDomicilio = request.getParameter("listaCodDomicilio");
            String Rol = request.getParameter("listaRol");

            m_Log.debug(" +++++++++++++++++++++++++++++++ CodTercero" + CodTercero);
            m_Log.debug(" +++++++++++++++++++++++++++++++ VersionTercero" + VersionTercero);
            m_Log.debug(" +++++++++++++++++++++++++++++++ CodDomicilio" + CodDomicilio);
            m_Log.debug(" +++++++++++++++++++++++++++++++ Rol" + Rol);

            Vector listaCodTercero = null;
            listaCodTercero = listaTemasSeleccionados(CodTercero);
            registro.setlistaCodTercero(listaCodTercero);

            Vector listaVersionTercero = null;
            listaVersionTercero = listaTemasSeleccionados(VersionTercero);
            registro.setlistaVersionTercero(listaVersionTercero);

            Vector listaCodDomicilio = null;
            listaCodDomicilio = listaTemasSeleccionados(CodDomicilio);
            registro.setlistaCodDomicilio(listaCodDomicilio);

            Vector listaRol = null;
            listaRol = listaTemasSeleccionados(Rol);
            registro.setlistaRol(listaRol);

            if (listaCodTercero.size() > 0) {
                m_Log.debug(" +++++++++++++++++++++++++++++++ listaCodTercero" + listaCodTercero.size());

            }

            for (int i = 0; i < listaCodDomicilio.size(); i++) {
                m_Log.debug(" ******* listaCodTercero" + listaCodTercero.elementAt(i));
            }
            m_Log.info(session.getId() + " " + usuarioActualLog + " ************************************** datos" + VersionTercero + "-" + CodDomicilio + "-" + Rol);

            // SE INDICA CUAL ES LA OFICINA DE REGISTRO DEL USUARIO
            registro.setCodOficinaRegistro(new Integer(regUsuarioVO.getCodOficinaRegistro()));

            try {
                Boolean finDigitalizacionAntesMod = false;
                String procedimientoAntesMod = null;
                String servicioDigitalizacionActivo = (String) session.getAttribute("servicioDigitalizacionActivo");
                if (servicioDigitalizacionActivo == null) {
                    m_Log.info(session.getId() + " " + usuarioActualLog + " servicioDigitalizacionActivo No recuperado en Session, asignamos 'no' ");
                    servicioDigitalizacionActivo = "no";
                }
                m_Log.info(session.getId() + " " + usuarioActualLog + " servicioDigitalizacionActivo " + servicioDigitalizacionActivo);

                if (grabarDesdeReserva == null && servicioDigitalizacionActivo.equalsIgnoreCase("si")) {

                    finDigitalizacionAntesMod = AnotacionRegistroManager.getInstance().getFinDigitalizacionAnotacion(registro, params);
                    procedimientoAntesMod = AnotacionRegistroManager.getInstance().getCodProcedimientoRegistro(registro, params);
                    m_Log.info(session.getId() + " " + usuarioActualLog + " finDigitalizacionAntesMod - procedimientoAntesMod : " + finDigitalizacionAntesMod + "-" + procedimientoAntesMod);
                }

                    if ("1".equalsIgnoreCase(registroForm.getRetramitarDocumentosCambioProcedimiento())) {
                        // En caso de que se pierda el Procedimiento ente el envio desde JSP y este punto
                        // Validamos que no llegue procedimieto a null si tenemos que retramitar.No dejamos.
                        if (StringUtils.isNullOrEmptyOrNullString(registro.getCodProcedimiento())) {
                            m_Log.info("No se valida la retramitacion: " + registro.getAnoReg() + "/" + registro.getNumReg()
                                    + ". Codigo de procedimiento Vacio"
                            );
                            registro.setRespOpcion("errorModifyRetramitar");
                            registro.setRetramitarDocumentosDetalleOPeracion("No se valida la retramitacion: " + registro.getAnoReg() + "/" + registro.getNumReg()
                                    + ". Campo Codigo de procedimiento Vacio");
                            registroForm.setRegistro(registro);
                            m_Log.info(session.getId() + " " + usuarioActualLog + " <================= MantAnotacionRegistroAction ======================");
                            return mapping.findForward("error");
                        }
                    }

                    AnotacionRegistroManager.getInstance().modify(registro, finDigitalizacionAntesMod, params);

                    //DIGITALIZACIï¿½N LANBIDE
                    if (servicioDigitalizacionActivo.equalsIgnoreCase("si")) {
                        //comprueba que se finaliza la digitalizaciï¿½n por primera vez
                        m_Log.info(session.getId() + " " + usuarioActualLog + " Entramos en servicioDigitalizacionActivo.equalsIgnoreCase(\"si\")");
                        if (registro.isFinDigitalizacion() && !finDigitalizacionAntesMod) {
                            m_Log.info(session.getId() + " " + usuarioActualLog + " Se marca el registro como finalizado por primera vez(registro.isFinDigitalizacion() && !finDigitalizacionAntesMod) -> comprueba si tiene documentos para tramitar");
                            DigitalizacionDocumentosLanbideManager digitalizacionManager = DigitalizacionDocumentosLanbideManager.getInstance();
                            ArrayList<DocumentoAnotacionRegistroVO> documentosRegistro = (ArrayList<DocumentoAnotacionRegistroVO>) digitalizacionManager.getDocumentosRegistro(registro, params);
                            m_Log.info(session.getId() + " " + usuarioActualLog + " Documentos de registro compulsados recuperados: " + documentosRegistro.size());
                            String codProcedimiento = AnotacionRegistroManager.getInstance().getCodProcedimientoRegistro(registro, params);
                            m_Log.info(session.getId() + " " + usuarioActualLog + " codProcedimiento : " + codProcedimiento);
                            // se cambia el documento de contexto Registro a Tramitaciï¿½n
                            if (codProcedimiento != null && !codProcedimiento.equals("")) {
                                for (DocumentoAnotacionRegistroVO docReg : documentosRegistro) {
                                    String idDocumento = docReg.getIdDocGestor();
                                    try {
                                        digitalizacionManager.tramitarDocumento(registro.getUnidadOrgan(), registro.getAnoReg(), registro.getNumReg(), idDocumento, codProcedimiento, docReg.isCompulsado(), params);
                                    } catch (Exception ex) {
                                        m_Log.error(ex.getMessage(), ex);
                                        AnotacionRegistroManager.getInstance().cancelarFinDigitalizacionDocumentos(registro, params);
                                        registro.setFinDigitalizacion(false);
                                        opcion = "error";
                                        registro.setRespOpcion("errorFinDigit");
                                    }
                                }
                            }

                        } else {
                            m_Log.info(session.getId() + " " + usuarioActualLog + " El registro ya habï¿½a sido finalizado con anterioridad.");
                        }
                    }

                    // Una vez modificado en Registro en BD - Retramitamos por cambio de procedimiento  
                    // SI NO HAY ERRORES 
                    if (!"error".equalsIgnoreCase(opcion)) {
                        if ("1".equalsIgnoreCase(registroForm.getRetramitarDocumentosCambioProcedimiento())) {
                            DocumentosRegistroLanbideRetramitacionService retramitacioService = new DocumentosRegistroLanbideRetramitacionService();
                            RespRetramitarDocumentosRegistroCambioProcVO respuestaRetramitacioService
                                    = retramitacioService.retramitarTodosDocumentosERCambioProcedimiento(cod_org, registro, registro.getCodProcedimiento(), params, "LANDIS");
                            if (respuestaRetramitacioService.isErrorEnlaPeticion()) {
                                m_Log.info("Anotacion de Registro Modificada. Pero No se pudo retramitar la documentacion : "
                                        + respuestaRetramitacioService.getDetalleErrorEnlaPeticion()
                                        + " - " + respuestaRetramitacioService.getDatosDocumentosRetramitadosAsString()
                                );
                                opcion = "error";
                                registro.setRespOpcion("errorModifyRetramitar");
                                registro.setRetramitarDocumentosDetalleOPeracion(respuestaRetramitacioService.getDetalleErrorEnlaPeticion());
                            } else {
                                m_Log.info("Registro Modificado y Documentacion Retramitada a " + registro.getCodProcedimiento());
                            }
                        }
                    }

                    //Guardar datos de Entradas/Salidas SIR Al modificar
                    // #586104 Dar de alta salida en SIR
                    // #681353 Alta Entradas en el SIR - Enviar si se finaliza el proceso de anexo documentacion.
                    // Se envia al sir cuando se finalice y esten tramitados los documentos para que no de error de contexto en Registro
                    if ("1".equalsIgnoreCase(registroConf.getString("INTEGRACION_SIR_LANBIDE"))) {
                        try {
                            if (registro.getCodigoUnidadDestinoSIR() == null || registro.getCodigoUnidadDestinoSIR().isEmpty()) {
                                String codUnidadDestinoSIRHidden = request.getParameter("codigoUnidadDestinoSIRHidden");
                                if (codUnidadDestinoSIRHidden != null)
                                    registro.setCodigoUnidadDestinoSIR(codUnidadDestinoSIRHidden);
                            }

                            if (registro.getUorCodVisible() == null || registro.getUorCodVisible().isEmpty()) {
                                String cod_uniRegDestinoORD = request.getParameter("cod_uniRegDestinoORD");
                                if (cod_uniRegDestinoORD != null) {
                                    String codUnidadVisible = UORsManager.getInstance().getCodigoVisibleUorByCodUor(cod_uniRegDestinoORD, params);
                                    registro.setUorCodVisible(codUnidadVisible);
                                }
                            }


                            if ("S".equalsIgnoreCase(registro.getTipoReg()) && registro.getTipoAnot() == 1) {
                                SirRegistroSalidaResponse sirRegistroSalidaResponse
                                        = registroSIRService.enviarSalidaSistemaSir(registro, usuarioVO.getIdioma(), params, cod_dep, cod_uni, true);
                                registro.setCodEstadoRespGestionSIR(sirRegistroSalidaResponse.getCodEstadoRespGestionSIR());
                                registro.setDescEstadoRespGestionSIR(sirRegistroSalidaResponse.getDescEstadoRespGestionSIR());

                            } else if ("E".equalsIgnoreCase(registro.getTipoReg()) && registro.getTipoAnot() == 1) {
                                if (registro.isFinDigitalizacion()) {
                                    m_Log.info("Proceso de anexo de documentacion finalizado - Modificando - Enviamos registro al SIR ");
                                    if (registro.getUorCodVisible() == null || registro.getUorCodVisible().isEmpty()) {
                                        String codigoVisible = UORsManager.getInstance().getCodigoVisibleUorByCodUor(registro.getIdUndTramitad(), params);
                                        registro.setUorCodVisible(codigoVisible);
                                    }
                                    SirRegistroSalidaResponse sirRegistroSalidaResponse
                                            = registroSIRService.enviarEntradaSistemaSir(registro, usuarioVO.getIdioma(), params, cod_dep, cod_uni, true, usuarioVO);
                                    registro.setCodEstadoRespGestionSIR(sirRegistroSalidaResponse.getCodEstadoRespGestionSIR());
                                    registro.setDescEstadoRespGestionSIR(sirRegistroSalidaResponse.getDescEstadoRespGestionSIR());
                                } else {
                                    m_Log.info("Envio al SIR Activado pero Proceso de anexo de documentacion AUN NO finalizado - No Enviamos registro al SIR ");
                                }

                            }
                        } catch (Exception ex) {
                            m_Log.error("Error al Crear la salida de registro dada de alta en Regexlan en el SIR - ModificandoSalidas: " + ex.getMessage(), ex);
                            registro.setCodEstadoRespGestionSIR("msgErrorNoDadaAltaSIR");
                            registro.setDescEstadoRespGestionSIR(MessageFormat.format(
                                    traductorAplicacionBean.getDescripcion("msgErrorNoDadaAltaSIR"),
                                    ex.getMessage()
                            ));
                            m_Log.info(session.getId() + " " + usuarioActualLog + " <================= MantAnotacionRegistroAction ======================");
                        }
                    } else {
                        m_Log.info("INTEGRACION_SIR_LANBIDE - Modificar - No activo o no configurado en el fichero Registro.properties");
                    }

                } catch (Exception e) {
                    m_Log.error(e.getMessage(), e);
                    opcion = "error";
                    registro.setRespOpcion("errorModify");
                }

                m_Log.info(session.getId() + " " + usuarioActualLog + " la respuesta en el action es : " + registro.getRespOpcion());
                elRegistroESVO = registro;

                request.setAttribute("POSICION_ANOTACION", elRegistroESVO.getPosicionAnotacion());
                request.setAttribute("ano", elRegistroESVO.getAnoReg());
                request.setAttribute("num", elRegistroESVO.getNumReg());
                if (registro.getRespOpcion().equals("modify_realizado")
                        || "registrar_actualizacion_sin_confirmar".equals(registro.getRespOpcion())
                        || (registro.getRespOpcion().equals("no_existe_expediente"))
                        || "proc_mal_relacionado".equals(elRegistroESVO.getRespOpcion())
                        || "registrar_actualizacion_fecha_anterior_sin_confirmar".equals(registro.getRespOpcion())
                        || "registrar_actualizacion_fecha_posterior_sin_confirmar".equals(registro.getRespOpcion())) {
                    m_Log.info(session.getId() + " " + usuarioActualLog + " Opcion antes de dejar MantAnotacionRegistroAction: " + opcion);
                    m_Log.info(session.getId() + " " + usuarioActualLog + " <================= MantAnotacionRegistroAction ======================");
                    return mapping.findForward("modifyHecha");
                } else if (registro.getRespOpcion().equals("registro_cerrado")) {
                    m_Log.info(session.getId() + " " + usuarioActualLog + " Opcion antes de dejar MantAnotacionRegistroAction: " + opcion);
                    m_Log.info(session.getId() + " " + usuarioActualLog + " <================= MantAnotacionRegistroAction ======================");
                    return mapping.findForward("modifyHecha");
                }

            } else if ("grabarDuplicarModificaciones".equals(opcion) || ("grabarModificacionesConfirmada2".equals(opcion))) {

                RegistroValueObject registro = registroForm.getRegistro();
                // Datos usuario que registra.
                registro.setUsuarioQRegistra(usuarioQRegistra);
                registro.setDptoUsuarioQRegistra(dptoUsuarioQRegistra);
                registro.setUnidOrgUsuarioQRegistra(unidOrgUsuarioQRegistra);

                m_Log.info(session.getId() + " " + usuarioActualLog + " Fecha Entrada: " + registro.getFecEntrada());

                // Cambiar de estado rechazado a pendiente.
                if (registro.getEstAnotacion() == 2) {
                    registro.setEstAnotacion(0);
                    m_Log.info(session.getId() + " " + usuarioActualLog + " MantAnotacionRegistroAction: CAMBIO DEL ESTADO DE LA ANOTACION DE RECHAZADO A PENDIENTE");
                }
                registro.setRespOpcion(opcion);
                String listTemasSelecc = request.getParameter("listaTemas");
                Vector v = listaTemasSeleccionados(listTemasSelecc);
                Vector<RegistroValueObject> temas = new Vector<RegistroValueObject>();
                for (int j = 0; j < v.size(); j++) {
                    RegistroValueObject reg = new RegistroValueObject();
                    String s = (String) v.elementAt(j);
                    reg.setCodigoTema(s);
                    temas.addElement(reg);
                }

                registro.setListaTemasAsignados(temas);

                // Adaptado para SALIDA -------------------------------------------
                String tipo = (String) session.getAttribute("tipoAnotacion");
                String fechaDoc = request.getParameter("fechaDocumento");
                String horaMinDoc = "00:00";

                if (tipo != null) {
                    if (("E".equals(tipo)) || ("Relacion_E".equals(tipo))) {
                        horaMinDoc = request.getParameter("horaMinDocumento");
                        registro.setTipoReg("E");
                    } else {
                        registro.setTipoReg("S");
                    }
                } else {
                    registro.setTipoReg("E");
                    horaMinDoc = request.getParameter("horaMinDocumento");
                }
                String fecHora = fechaDoc + " " + horaMinDoc;
                registro.setFecHoraDoc(fecHora);

                // Fecha de presentacion
                String fechaEnt = "";
                String horaMinAnotacion = request.getParameter("horaMinAnotacion");;
                if (registro.getContador() == 0) {
                    fechaEnt = request.getParameter("fechaAnotacion");

                } else {
                    fechaEnt = request.getParameter("fecPresRes");
                }
                String fecHora1 = fechaEnt + " " + horaMinAnotacion;
                registro.setFecEntrada(fecHora1);
                String fechaDocu = request.getParameter("fechaDoc");
                registro.setFechaDocu(fechaDocu);

                session.removeAttribute("BusquedaTercerosForm");

                if ("grabarDuplicarModificaciones".equals(opcion)) {
                    registro.setRespOpcion("actualizacion_sin_confirmar2");
                } else {
                    registro.setRespOpcion("actualizacion_confirmada2");
                }

                //recojo los datos para insertar los interesados.
                String CodTercero = request.getParameter("listaCodTercero");
                String VersionTercero = request.getParameter("listaVersionTercero");
                String CodDomicilio = request.getParameter("listaCodDomicilio");
                String Rol = request.getParameter("listaRol");

                Vector listaCodTercero = listaTemasSeleccionados(CodTercero);
                registro.setlistaCodTercero(listaCodTercero);

                Vector listaVersionTercero = listaTemasSeleccionados(VersionTercero);
                registro.setlistaVersionTercero(listaVersionTercero);

                Vector listaCodDomicilio = listaTemasSeleccionados(CodDomicilio);
                registro.setlistaCodDomicilio(listaCodDomicilio);

                Vector listaRol = listaTemasSeleccionados(Rol);
                registro.setlistaRol(listaRol);

                // SE INDICA CUAL ES LA OFICINA DE REGISTRO DEL USUARIO                        
                registro.setCodOficinaRegistro(regUsuarioVO.getCodOficinaRegistro());

                AnotacionRegistroManager.getInstance().modify(registro, false, params);

                m_Log.info(session.getId() + " " + usuarioActualLog + " la respuesta en el action es : " + registro.getRespOpcion());
                elRegistroESVO = registro;

                if (registro.getRespOpcion().trim().equals("modify_realizado")) {

                    m_Log.info(session.getAttribute("reservas") + "   Opcion antes de dejar MantAnotacionRegistroAction: duplicar " + opcion);
                    m_Log.info(session.getId() + " " + usuarioActualLog + " <================= MantAnotacionRegistroAction 2======================");
                    try {
                        AnotacionRegistroManager.getInstance().duplicar(registro, params);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return mapping.findForward("modifyHecha");
                } else if ("registrar_actualizacion_sin_confirmar".equals(registro.getRespOpcion())
                        || (registro.getRespOpcion().equals("no_existe_expediente"))
                        || "proc_mal_relacionado".equals(elRegistroESVO.getRespOpcion())
                        || registro.getRespOpcion().equals("registro_cerrado")) {
                    m_Log.info(session.getId() + " " + usuarioActualLog + " Opcion antes de dejar MantAnotacionRegistroAction: " + opcion);
                    m_Log.info(session.getId() + " " + usuarioActualLog + " <================= MantAnotacionRegistroAction 22======================");
                    return mapping.findForward("modifyHecha");
                }

            } else if ("iniciar_anular".equals(opcion)) {

                elRegistroESVO = registroForm.getRegistro();
                // Al venir de modificar estÃ¡ vacÃ­o.
                elRegistroESVO.setIdentDepart(cod_dep);
                elRegistroESVO.setUnidadOrgan(cod_uni);
                String ejercicio = request.getParameter("ano");
                m_Log.info(session.getId() + " " + usuarioActualLog + " el ejercicio es : " + ejercicio);
                String numero = request.getParameter("numero");
                m_Log.info(session.getId() + " " + usuarioActualLog + " el numero es : " + numero);
                elRegistroESVO.setNumReg(Long.parseLong(numero));
                elRegistroESVO.setAnoReg(Integer.parseInt(ejercicio));
                String tipo = (String) session.getAttribute("tipoAnotacion");
                if (tipo != null) {
                    if ("E".equals(tipo) || "Relacion_E".equals(tipo)) {
                        elRegistroESVO.setTipoReg("E");
                    } else {
                        elRegistroESVO.setTipoReg("S");
                    }
                    elRegistroESVO = AnotacionRegistroManager.getInstance().getByPrimaryKey(elRegistroESVO, params);

                    // Asunto "escapeado". No puede ser nulo
                    elRegistroESVO.setAsunto(AdaptadorSQLBD.js_unescape(elRegistroESVO.getAsunto()));

                    // Comprobar q REGISTRO esta abierto.
                    Date fEntr = Fecha.obtenerDate(elRegistroESVO.getFecEntrada());
                    Date fRegCerrado = RegistroAperturaCierreManager.getInstance().getFechaRegistroCerrado(elRegistroESVO, params);
                    if (fRegCerrado == null || fEntr.compareTo(fRegCerrado) >= 0) {
                        elRegistroESVO.setRespOpcion("anulacion_autorizada");
                    } else {
                        elRegistroESVO.setRespOpcion("anulacion_no_autorizada");
                    }
                } else {
                    elRegistroESVO.setRespOpcion("anulacion_no_autorizada");
                }
                opcion = "anular";

                // Si se viene de modificar RESERVA.
                if (session.getAttribute("registroValueObjectConsulta") == null) {
                    RegistroValueObject regVO = new RegistroValueObject();
                    regVO.setNumReg(elRegistroESVO.getNumReg());
                    regVO.setAnoReg(elRegistroESVO.getAnoReg());
                    regVO.setTipoReg(elRegistroESVO.getTipoReg());
                    regVO.setIdOrganizacion(elRegistroESVO.getIdOrganizacion());
                    regVO.setIdEntidad(elRegistroESVO.getIdEntidad());
                    regVO.setIdentDepart(elRegistroESVO.getIdentDepart());
                    regVO.setUnidadOrgan(elRegistroESVO.getUnidadOrgan());
                    regVO.setTipoAnot(-1);
                    regVO.setEstAnotacion(-10);

                    int pagina = Integer.parseInt(elRegistroESVO.getPaginaListado());
                    int count = Integer.parseInt(elRegistroESVO.getNumLineasPaginaListado());
                    int startIndex = (pagina - 1) * count + 1;
                    session.setAttribute("registroValueObjectConsulta", regVO);
                    Vector res = AnotacionRegistroManager.getInstance().relacionRegistroValueObject(regVO, params, startIndex, count, 0, "");
                    session.setAttribute("RelacionAnotaciones", res);
                }

            } // Fin iniciar anular
            else if ("anular".equals(opcion)) {

                String strNumPagina = registroForm.getPaginaListado();
                String strCount = registroForm.getNumLineasPaginaListado();
                boolean digitalizacion = false;
                m_Log.info(session.getId() + " " + usuarioActualLog + " PAGINA: " + strNumPagina + " | CUENTA: " + strCount);

                m_Log.debug(" *************************** POSICION ANOTACION REQUEST: " + request.getParameter("posicionAnotacion"));

                elRegistroESVO = registroForm.getRegistro(); // EstÃ¡ en sesiÃ³n

                elRegistroESVO.setEstAnotacion(9);
                // Datos usuario que registra.
                elRegistroESVO.setUsuarioQRegistra(usuarioQRegistra);
                elRegistroESVO.setDptoUsuarioQRegistra(dptoUsuarioQRegistra);
                elRegistroESVO.setUnidOrgUsuarioQRegistra(unidOrgUsuarioQRegistra);

                m_Log.debug(" *************************** POSICION ANOTACION elRegistroESVO: " + elRegistroESVO.getPosicionAnotacion());
                try {
                    //Boolean nos dice si hay digitalizaciï¿½n o no
                    servDigitalizacionAct = (String) session.getAttribute("servicioDigitalizacionActivo");
                    m_Log.debug("(String) session.getAttribute(\"servicioDigitalizacionActivo\") : " + servDigitalizacionAct);
                    if (servDigitalizacionAct == null) {
                        m_Log.debug("servicioDigitalizacionActivo No viene en Session - Asignamos desde properties");
                        try {
                            servDigitalizacionAct = registroConf.getString(usuarioVO.getOrgCod() + "/SERVICIO_DIGITALIZACION_ACTIVO");
                        } catch (Exception e) {
                            m_Log.debug("Se ha producido un error recuperando la propiedad '" + usuarioVO.getOrgCod() + "/SERVICIO_DIGITALIZACION_ACTIVO' de Registro.properties");
                        }
                        if (servDigitalizacionAct != null && servDigitalizacionAct.equalsIgnoreCase("si")) {
                            session.setAttribute("servicioDigitalizacionActivo", servDigitalizacionAct);
                            digitalizacion = true;
                        }
                    } else {
                        digitalizacion = true;
                    }

                    m_Log.debug("servicioDigitalizacionActivo Recuperado O asiganado desde properties a Session si no venia " + servDigitalizacionAct);
                    elRegistroESVO.setRespOpcion("actualizacion_confirmada");
                    AnotacionRegistroManager.getInstance().anular(elRegistroESVO, digitalizacion, params);
                    // Llegados aqui suponemos que esta bien.
                    if ("modify_no_realizado".equals(elRegistroESVO.getRespOpcion())) {
                        elRegistroESVO.setRespOpcion("anulacion_no_realizada");
                    } else if ("modify_no_autorizado".equals(elRegistroESVO.getRespOpcion())) {
                        elRegistroESVO.setRespOpcion("anulacion_no_autorizada");
                    } else {
                        elRegistroESVO.setRespOpcion("anulacion_realizada");
                    }

                } catch (Exception ce) {
                    m_Log.error("MantAnotacionRegistroAction problem" + ce.getMessage(), ce);
                    opcion = "error";
                    elRegistroESVO.setRespOpcion(opcion);
                }
            } // Fin anular
            else if ("cancelar_anular".equals(opcion)) {

                m_Log.debug("[cancel_annul] Iniciar con año=" + request.getParameter("año") + ", número=" + request.getParameter("número") + ", tipoEntrada=" + request.getParameter("cbTipoEntrada"));
                m_Log.info(session.getId() + " " + usuarioActualLog + " desdeEntradasRechazadas" + request.getParameter("desdeEntradasRechazadas"));
                m_Log.info(session.getId() + " " + usuarioActualLog + " desdePendientesFinalizar" + request.getParameter("desdePendientesFinalizar"));
                if ("S".equals(request.getParameter("desdeEntradasRechazadas")) || "S".equals(request.getParameter("desdePendientesFinalizar"))) {
                    RegistroValueObject storedConsulta = new RegistroValueObject();

                    if ("S".equals(request.getParameter("desdeEntradasRechazadas"))) {
                        session.setAttribute("entradasRechazadas", "S");
                        session.removeAttribute("entradasPendientes");
                        storedConsulta.setEstAnotacion(ConstantesDatos.REG_ANOTACION_ESTADO_RECHAZADA);
                    } else if ("S".equals(request.getParameter("desdePendientesFinalizar"))) {
                        storedConsulta.setEstAnotacion(ConstantesDatos.REG_ANOTACION_BUSCAR_TODAS);
                        session.setAttribute("entradasPendientes", "S");
                        session.removeAttribute("entradasRechazadas");
                    }

                    m_Log.debug("guardamos los datos de la anotacion para realizar la busqueda");
                    if ((request.getParameter("ano") != null) && (request.getParameter("numero") != null)) {
                        if ((!"".equals(request.getParameter("ano").trim())) && (!"".equals(request.getParameter("numero").trim()))) {
                            storedConsulta.setAnoReg(Integer.parseInt(request.getParameter("ano")));
                            m_Log.debug("aï¿½o anotacion " + request.getParameter("ano"));
                            storedConsulta.setNumReg(Long.parseLong(request.getParameter("numero")));
                            m_Log.debug("numero anotaciï¿½n " + request.getParameter("numero"));
                        }

                        storedConsulta.setIdentDepart(regUsuarioVO.getDepCod());
                        storedConsulta.setUnidadOrgan(regUsuarioVO.getUnidadOrgCod());
                        storedConsulta.setTipoReg("E");
                        storedConsulta.setTipoAnot(-1);
                        session.setAttribute("registroValueObjectConsulta", storedConsulta);
                        m_Log.debug("fin almacenar datos");
                    }
                } else {
                    session.removeAttribute("entradasRechazadas");
                    session.removeAttribute("entradasPendientes");
                }
                elRegistroESVO = registroForm.getRegistro();
                if (elRegistroESVO != null) {
                    m_Log.debug("[cancelar_anular] anotacion recuperada ano=" + elRegistroESVO.getAnoReg()
                            + ", numero=" + elRegistroESVO.getNumReg() + ", tipoAnot=" + elRegistroESVO.getTipoAnot());
                }
                inicializarListasVacias(elRegistroESVO);
                elRegistroESVO.setListaActuaciones(new Vector());
                if ((request.getParameter("ano") != null) && (request.getParameter("numero") != null)) {
                    if ((!"".equals(request.getParameter("ano").trim())) && (!"".equals(request.getParameter("numero").trim()))) {
                        elRegistroESVO.setAnoReg(Integer.parseInt(request.getParameter("ano")));
                        elRegistroESVO.setNumReg(Long.parseLong(request.getParameter("numero")));
                    }
                    // Tercero.
                    m_Log.debug("[cancelar_anular] anotacion recuperada ano=" + elRegistroESVO.getAnoReg() + ", numero=" + elRegistroESVO.getNumReg() + ", tipoAnot=" + elRegistroESVO.getTipoAnot());
                    TercerosValueObject elTercero = new TercerosValueObject();
                    elTercero.setIdDomicilio(String.valueOf(elRegistroESVO.getDomicInter()));
                    elTercero.setIdentificador(String.valueOf(elRegistroESVO.getCodInter()));
                    elTercero.setVersion(String.valueOf(elRegistroESVO.getNumModInfInt()));
                    Vector tercero = TercerosManager.getInstance().getByHistorico(elTercero, params);
                    TercerosValueObject terc = (TercerosValueObject) tercero.firstElement();
                    if (!terc.getIdentificador().equals("0")) {
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(terc.getTipoDocumento());
                        }
                        String tDoc = (terc.getTipoDocumento().equals("")) ? "0" : terc.getTipoDocumento();
                        registroForm.setCbTipoDoc(tDoc);
                        // Los demas datos permanecen.
                    }
                    // Fin tercero
                }
                // Hay que pasar tambien el codigo y descripcion del rol por defecto
                // Si hay procedimiento obtenemos sus roles, si no los roles por defecto.
                String proc = elRegistroESVO.getCodProcedimientoRoles();
                Vector<GeneralValueObject> roles = new Vector<GeneralValueObject>();
                if (proc != null && !proc.equals("") && !proc.equals("null")) {
                    GeneralValueObject procGVO = new GeneralValueObject();
                    procGVO.setAtributo("codMunicipio", elRegistroESVO.getMunProcedimiento());
                    procGVO.setAtributo("codProcedimiento", proc);
                    roles = obtenerRolesProcedimiento(procGVO, params);
                } else {
                    roles = InteresadosManager.getInstance().getListaRolesRegistro(params);
                }
                // Comprobamos cual es el codigo del rol por defecto
                for (GeneralValueObject rol : roles) {
                    if (rol.getAtributo("porDefecto").equals("SI")) {
                        registroForm.setCodRolDefecto((String) rol.getAtributo("codRol"));
                        registroForm.setDescRolDefecto((String) rol.getAtributo("descRol"));
                        break;
                    }
                }
                registroForm.setListaRoles(roles);
                m_Log.debug("Rol por defecto cargado: codigo=" + registroForm.getCodRolDefecto()
                        + ", descripcion=" + registroForm.getDescRolDefecto());

                // Si venimos de completar reserva.
                session.setAttribute("modoInicio", "recargar_buscada");
                opcion = "cancelar_anular";
            } // Fin cancelar anular.
            else if ("desAnular".equals(opcion)) {

                elRegistroESVO = registroForm.getRegistro(); // Esta en sesion
                String strIdOrg = String.valueOf(elRegistroESVO.getIdOrganizacion());
                String strIdEnt = String.valueOf(elRegistroESVO.getIdEntidad());
                boolean digitalizacion = false;
                if ((elRegistroESVO.getOrgEntDest() != null) && (elRegistroESVO.getOrgDestino() != null)) {
                    if (strIdOrg.equals(elRegistroESVO.getOrgDestino()) && strIdEnt.equals(elRegistroESVO.getOrgEntDest())) {
                        elRegistroESVO.setEstAnotacion(1);
                    } else {
                        elRegistroESVO.setEstAnotacion(0);
                    }
                } else {
                    elRegistroESVO.setEstAnotacion(0);
                }
                elRegistroESVO.setDilAnulacion(null);
                // Datos usuario que registra.
                elRegistroESVO.setUsuarioQRegistra(usuarioQRegistra);
                elRegistroESVO.setDptoUsuarioQRegistra(dptoUsuarioQRegistra);
                elRegistroESVO.setUnidOrgUsuarioQRegistra(unidOrgUsuarioQRegistra);

                opcion = "anular";
                try {
                    elRegistroESVO.setRespOpcion("actualizacion_confirmada");
                    AnotacionRegistroManager.getInstance().anular(elRegistroESVO, digitalizacion, params);
                    // Llegados aquiï¿½ suponemos que esta bien.
                    if ("modify_no_realizado".equals(elRegistroESVO.getRespOpcion())) {
                        elRegistroESVO.setRespOpcion("anulacion_no_realizada");
                    } else if ("modify_no_autorizado".equals(elRegistroESVO.getRespOpcion())) {
                        elRegistroESVO.setRespOpcion("anulacion_no_autorizada");
                    } else if ("registro_cerrado".equals(elRegistroESVO.getRespOpcion())) {
                        elRegistroESVO.setRespOpcion("registro_cerrado");
                    } else {
                        elRegistroESVO.setRespOpcion("desAnulacion_realizada");
                    }
                } catch (Exception ce) {
                    m_Log.error("MantAnotacionRegistroAction problem" + ce.getMessage(), ce);
                    opcion = "error";
                    elRegistroESVO.setRespOpcion(opcion);
                }
            } // Fin desAnular
            //RelacionBusqueda_E es cuando viene de busqueda de anotaciones
            else if ("Relacion_E".equals(opcion) || "Relacion_S".equals(opcion)
                    || "S".equals(opcion) || "E".equals(opcion)
                    || "Salida".equalsIgnoreCase(opcion) || "Entrada".equalsIgnoreCase(opcion)
                    || "RelacionBusqueda_E".equalsIgnoreCase(opcion) || "RelacionBusqueda_S".equalsIgnoreCase(opcion)) {

                form = new MantAnotacionRegistroForm();
                session.removeAttribute("reservas");
                session.removeAttribute("registroValueObjectConsulta");
                session.removeAttribute("RelacionAnotaciones");
                session.removeAttribute("modoInicio");

                if ("request".equals(mapping.getScope())) {
                    request.setAttribute(mapping.getAttribute(), form);
                } else {
                    session.setAttribute(mapping.getAttribute(), form);
                }

                registroForm = (MantAnotacionRegistroForm) form;

                directiva_salidas_uor_usuario = "NO";

                if (("Relacion_S".equals(opcion))
                        || ("S".equals(request.getParameter("opcion")))
                        || ("Salida".equalsIgnoreCase(opcion))
                        || ("RelacionBusqueda_S".equalsIgnoreCase(opcion))) {

                    if (UsuariosGruposManager.getInstance().tienePermisoDirectiva(ConstantesDatos.REGISTRO_S_SOLO_UORS_USUARIO, usuarioVO.getIdUsuario(), params)) {
                        m_Log.debug("******* MantAnotacionRegistroAction hay directiva de usuario que solo puede acceder a uors a las que tenga permiso");

                        session.setAttribute("directiva_salidas_uor_usuario", "SI");
                        directiva_salidas_uor_usuario = "SI";

                    } else {
                        m_Log.debug("******* MantAnotacionRegistroAction   NO hay directiva de usuario que solo puede acceder a uors a las que tenga permiso");
                        session.setAttribute("directiva_salidas_uor_usuario", "NO");
                    }
                } else {
                    session.setAttribute("directiva_salidas_uor_usuario", "NO");
                    directiva_salidas_uor_usuario = "NO";

                }

                //===============================
                //nuevas UORs
                registroForm.setListaNuevasUORs(nuevasUOR);
                // arbol de jerarquÃ­a de uors
                registroForm.setArbol(arbol);
                //===============================
                // Cargar rol por defecto
                Vector<GeneralValueObject> roles = InteresadosManager.getInstance().getListaRolesRegistro(params);
                if (roles != null) {
                    for (GeneralValueObject rol : roles) {
                        if (rol.getAtributo("porDefecto").equals("SI")) {
                            registroForm.setCodRolDefecto((String) rol.getAtributo("codRol"));
                            registroForm.setDescRolDefecto((String) rol.getAtributo("descRol"));
                            break;
                        }
                    }
                }
                registroForm.setListaRoles(roles);
                m_Log.debug("Rol por defecto cargado: codigo=" + registroForm.getCodRolDefecto()
                        + ", descripcion=" + registroForm.getDescRolDefecto());

                elRegistroESVO = new RegistroValueObject();
                elRegistroESVO.setIdOrganizacion(cod_org);
                elRegistroESVO.setIdEntidad(cod_ent);
                elRegistroESVO.setIdentDepart(cod_dep);
                elRegistroESVO.setUnidadOrgan(cod_uni);
                if ("Relacion_E".equals(opcion) || "E".equals(opcion) || "Entrada".equalsIgnoreCase(opcion)) {
                    elRegistroESVO.setTipoReg("E");
                    session.setAttribute("tipoAnotacion", "Relacion_E");
                    opcion = "cargarAltaRE";
                } else if ("Relacion_S".equals(opcion) || "S".equals(opcion) || "Salida".equalsIgnoreCase(opcion)) {
                    elRegistroESVO.setTipoReg("S");
                    session.setAttribute("tipoAnotacion", "Relacion_S");
                    opcion = "cargarAltaRE";
                } //viene de listado de busqueda  de entradas y tiene que cargar la busqueda
                else if ("RelacionBusqueda_E".equals(opcion)) {
                    elRegistroESVO.setTipoReg("E");
                    session.setAttribute("tipoAnotacion", "Relacion_E");
                    opcion = "cargarBusquedaEntradas";
                } else if ("RelacionBusqueda_S".equals(opcion)) {
                    elRegistroESVO.setTipoReg("S");
                    session.setAttribute("tipoAnotacion", "Relacion_S");
                    opcion = "cargarBusquedaEntradas";
                } else {
                    opcion = "iniciar";
                }
                //Date fRegAbierto = RegistroAperturaCierreManager.getInstance().getFechaRegistroAbierto(elRegistroESVO,params);
                Fecha f = new Fecha();
                elRegistroESVO.setFecEntrada(null);

                // Por problemas de fechas partidas.
                // Cargar listas.
                inicializarListas(elRegistroESVO, false, params);
                elRegistroESVO.setFecEntrada(null);
                elRegistroESVO.setListaTemasAsignados(new Vector());
                registroForm.setObligatorioAsuntoCodificado(asuntoObligatorio);
                registroForm.setBusquedaAjaxTercero(busquedaTercero);
            } else if ("cargarNombre".equalsIgnoreCase(opcion)) {
                // =======================
                // nuevas UORs
                elRegistroESVO = registroForm.getRegistro();
                registroForm.setRegistro(elRegistroESVO);
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                m_Log.debug("Recargando datos de las UORs en la sesion");

                Vector nUOR = new Vector();
                if (("SI".equals(directiva_salidas_uor_usuario)) && (tipoES != null) && ("S".equals(tipoES) || "Relacion_S".equals(tipoES))) {
                    nUOR = UORsManager.getInstance().getListaUORsPermisoUsuarioNuevo(usuario, params);
                } else {
                    nUOR = UORsManager.getInstance().getListaUORs(false, params);
                }//.getListaUORsPorNombre(params);

                m_Log.debug("Cargadas " + nUOR.size() + " UORs");
                registroForm.setListaNuevasUORs(nUOR);

                ArbolImpl arbolUORs = new ArbolImpl();
                if (("SI".equals(directiva_salidas_uor_usuario)) && (tipoES != null) && ("S".equals(tipoES) || "Relacion_S".equals(tipoES))) {
                    arbolUORs = UORsManager.getInstance().getArbolUORsPermisoUsuario(false, true, usuario, params);
                } else {
                    arbolUORs = UORsManager.getInstance().getArbolUORs(false, false, true, params);
                }
                // arbol de jerarquÃ­a de uors

                m_Log.debug("Cargado Ã¡rbol:" + arbol.contarNodos());
                registroForm.setArbol(arbolUORs);
                request.setAttribute("listaUORs", nUOR);
                request.setAttribute("arbolUORs", arbolUORs);

                opcion = "cargarNombre";
            } else if ("consultarAnotacionRelacion".equals(opcion)) {
                //No se usa, pero hay que leer el VO en esta variable o se pierde.
                elRegistroESVO = registroForm.getRegistro();

                RegistroValueObject asiento = new RegistroValueObject();

                asiento.setTxtInteresado(elRegistroESVO.getTxtInteresado());

                asiento.setIdOrganizacion(cod_org);
                asiento.setIdEntidad(cod_ent);
                asiento.setIdentDepart(cod_dep);
                asiento.setUnidadOrgan(cod_uni);

                asiento.setTipoAnot(-1);

                if (request.getParameter("ano") != null) {
                    if (!"".equals(request.getParameter("ano").trim())) {
                        asiento.setAnoReg(Integer.parseInt(request.getParameter("ano")));
                    }
                }
                if (request.getParameter("numero") != null) {
                    if (!"".equals(request.getParameter("numero").trim())) {
                        asiento.setNumReg(Long.parseLong(request.getParameter("numero")));
                    }
                }
                String asunto = request.getParameter("codAsunto");
                asiento.setCodAsunto(asunto);
                String observacion = request.getParameter("observaciones");
                asiento.setObservaciones(observacion);
                String descAsunto = request.getParameter("asunto");
                asiento.setAsunto(descAsunto);
                String fechaAnotacion = request.getParameter("fechaAnotacion");
                asiento.setFecEntrada(fechaAnotacion);
                String fechaDocumento = request.getParameter("fechaDocumento");
                asiento.setFecHoraDoc(fechaDocumento);
                String tipoReg = (String) session.getAttribute("tipEntrada");
                if (tipoReg == null) {
                    tipoReg = request.getParameter("tipoReg");
                }
                elRegistroESVO.setTipoReg(tipoReg);
                asiento.setTipoReg(tipoReg);
                String uniRegDest = request.getParameter("cod_uor");
                asiento.setIdUndTramitad(uniRegDest);
                String numExp = request.getParameter("txtExp1");
                asiento.setNumExpediente(numExp);
                if (request.getParameter("codInter") != null) {
                    if (!"".equals(request.getParameter("codInter").trim())) {
                        int codInt = Integer.parseInt(request.getParameter("codInter"));
                        asiento.setCodInter(codInt);
                    }
                }

                if (request.getParameter("codEstado") != null) {
                    if ("".equals(request.getParameter("codEstado").trim())) {
                        asiento.setEstAnotacion(-10); // Todas
                    } else if ("9".equals(request.getParameter("codEstado").trim())) {
                        asiento.setEstAnotacion(9);
                    } else if ("1".equals(request.getParameter("codEstado").trim())) {
                        asiento.setEstAnotacion(1);
                    } else if ("2".equals(request.getParameter("codEstado").trim())) {
                        asiento.setEstAnotacion(2);
                    } else if ("0".equals(request.getParameter("codEstado").trim())) {
                        asiento.setEstAnotacion(0);
                    } else if ("-9".equals(request.getParameter("codEstado").trim())) {
                        asiento.setEstAnotacion(-9);
                    } else if ("3".equals(request.getParameter("codEstado").trim())) {
                        asiento.setEstAnotacion(3);
                    } else {
                        asiento.setEstAnotacion(-10);
                    }
                } else {
                    asiento.setEstAnotacion(-10);
                }

                String tipoConsulta = request.getParameter("tipoConsulta");
                m_Log.debug("EL TIPO DE CONSULTA AQUI ES: " + tipoConsulta);

                String tipoDocumentoInteresado = request.getParameter("codTipoDoc");
                String tipoPersonaFisicJurid = null;
                if (tipoDocumentoInteresado != null) {
                    if (!"".equals(tipoDocumentoInteresado.trim()) && (tipoDocumentoInteresado.trim().indexOf('|') == -1) && (tipoDocumentoInteresado.trim().indexOf('&') == -1) && (tipoDocumentoInteresado.trim().indexOf(':') == -1)) {
                        tipoPersonaFisicJurid = AnotacionRegistroManager.getInstance().getIndicadorPersonaFisicaJuridica(tipoDocumentoInteresado.trim(), params);
                        m_Log.debug("ENTOR: " + tipoPersonaFisicJurid);
                        if (tipoPersonaFisicJurid == null) {
                            tipoPersonaFisicJurid = "1";
                            m_Log.debug("ENTOR: " + tipoPersonaFisicJurid);
                        }
                    }
                }

                try {

                    int numAnots = AnotacionRegistroManager.getInstance().getNumeroTotalAnotaciones(asiento, params);
                    m_Log.debug("RECUPERADAS " + numAnots + " ANOTACIONES QUE CUMPLEN LOS CRITERIOS");
                    session.setAttribute("NumRelacionAnotaciones", numAnots);

                } catch (Exception ce) {
                    m_Log.error("MantAnotacionRegistroAction problem" + ce.getMessage());
                    session.setAttribute("RelacionAnotaciones", new Vector());
                    session.setAttribute("NumRelacionAnotaciones", 0);
                } finally {
                    RegistroValueObject storedConsulta = new RegistroValueObject();
                    storedConsulta.copy(asiento);
                    session.setAttribute("registroValueObjectConsulta", storedConsulta);

                    if ("listado".equals(tipoConsulta)) {
                        opcion = "consultarListadoBusqueda";
                    }

                }
            } else if ("consultaEnSIR".equals(opcion)) {
                /*
                 * Los nombres posibles son: cd_intercambio, cd_ent_reg_origen, cd_ent_reg_destino, cd_estado,
                 * num_registro, num_registro_inicial, ds_num_transporte, ds_resumen, fe_registro, ds_nombre,
                 * ds_apellido1, ds_apellido2, ds_identificador, cd_doc_fisica
                 *
                 * Los posibles operadores son: =, like, in, >=, <=
                 *
                 * Los valores posibles para cd_doc_fisica son:
                 *   1 = Acompaña documentación física requerida
                 *   2 = Acompaña documentación física complementaria
                 *   3 = No acompaña documentación física
                 */
                // 1) Log de todos los parámetros recibidos
                java.util.Enumeration<String> paramNames = request.getParameterNames();
                while (paramNames.hasMoreElements()) {
                    String paramName = paramNames.nextElement();
                    String[] values  = request.getParameterValues(paramName);
                    m_Log.info("Parametro recibido: " + paramName + " = " + java.util.Arrays.toString(values));
                }

                // 2) Recuperar los filtros principales y crear un objeto RegistroValueObject con estos filtros, además de tipo entrada 1, para almacenar la consulta en sesión
                String identificador          = request.getParameter("identificadorRegistroSIRValue");
                String codEstadoSIR           = request.getParameter("codEstadoSIR");
                String codigoUnidadDestinoSIR = request.getParameter("codigoUnidadDestinoSIRHidden");
                // codigoUnidadOrigenSIRHidden no corresponde TODO
                String codigoUnidadOrigenSIR = request.getParameter("codigoUnidadOrigenSIRHidden");
                String ejercicioFiltro = request.getParameter("ano");

                m_Log.info("identificadorRegistroSIR = [" + identificador + "]");
                m_Log.info("codEstadoSIR            = [" + codEstadoSIR + "]");
                m_Log.info("codigoUnidadDestinoSIR  = [" + codigoUnidadDestinoSIR + "]");
                m_Log.info("ejercicioFiltro  = [" + ejercicioFiltro + "]");
                RegistroValueObject filtro = new RegistroValueObject();
                filtro.setTipoAnot(1);
                filtro.setIdentificadorRegistroSIR(identificador);
                if (codEstadoSIR != null && !codEstadoSIR.trim().isEmpty()) {
                    filtro.setEstadoSIR(Integer.parseInt(codEstadoSIR));
                }
                filtro.setCodigoUnidadDestinoSIR(codigoUnidadDestinoSIR);
                String tipo = (String) session.getAttribute("tipoAnotacion");
                filtro.setTipoReg("E".equals(tipo) || "Relacion_E".equals(tipo) ? "E" : "S");
                filtro.setUnidadOrgan(regUsuarioVO.getUnidadOrgCod());
                filtro.setIdentDepart(regUsuarioVO.getDepCod());
                filtro.setEstAnotacion(ConstantesDatos.REG_ANOTACION_BUSCAR_TODAS);
                session.setAttribute("registroValueObjectConsulta", filtro);

                // 3) Invocación al servicio y construcción del vector final
                Vector listaFinal = new Vector();

                int contador = 0;
                try {
                    // 3a) Llamada al servicio SIR
                    List<Lan6AsientoRegistralRespuesta> listaRegistros =
                            registroSIRService.buscarRegistrosSIR(codEstadoSIR, identificador, codigoUnidadDestinoSIR, codigoUnidadOrigenSIR, params);

                    if (listaRegistros != null && ejercicioFiltro != null && !ejercicioFiltro.trim().isEmpty()) {
                        try {
                            int yearFilter = Integer.parseInt(ejercicioFiltro.trim());
                            java.util.List<Lan6AsientoRegistralRespuesta> filtrada = new java.util.ArrayList<Lan6AsientoRegistralRespuesta>();
                            java.util.Calendar cal = java.util.Calendar.getInstance();
                            for (Lan6AsientoRegistralRespuesta r : listaRegistros) {
                                if (r.getFechaRegistro() != null) {
                                    cal.setTime(r.getFechaRegistro());
                                    if (cal.get(java.util.Calendar.YEAR) == yearFilter) {
                                        filtrada.add(r);
                                    }
                                }
                            }
                            listaRegistros = filtrada;
                        } catch (NumberFormatException e) {
                            m_Log.warn("Ano no valido para filtrar: " + ejercicioFiltro, e);
                        }
                    }
                    m_Log.info("Número de registros SIR recuperados: " +
                            (listaRegistros != null ? listaRegistros.size() : "null"));

                    if (listaRegistros != null) {
                        for (Lan6AsientoRegistralRespuesta registroSIR : listaRegistros) {
                            // Preparar filtro VO
                            //RegistroValueObject filtro = new RegistroValueObject();
                            filtro.setIdentificadorRegistroSIR(registroSIR.getIdentificadorIntercambio());
                            /*String tipo = (String) session.getAttribute("tipoAnotacion");
                            filtro.setTipoReg("E".equals(tipo) || "Relacion_E".equals(tipo) ? "E" : "S");
                            filtro.setUnidadOrgan(regUsuarioVO.getUnidadOrgCod());
                            filtro.setIdentDepart(regUsuarioVO.getDepCod());*/
                            //filtro.setTipoAnot(-1);
                            //filtro.setEstAnotacion(ConstantesDatos.REG_ANOTACION_BUSCAR_TODAS);

                            // Llamada a la lógica de relación
                            Vector datosAnotacion = AnotacionRegistroManager
                                    .getInstance()
                                    .relacionRegistroValueObject(filtro, params, 1, listaRegistros.size(), 0, null);

                            if (datosAnotacion != null
                                    && !datosAnotacion.isEmpty()
                                    && datosAnotacion.firstElement() instanceof Collection<?>) {
                                datosAnotacion = new Vector((Collection<?>) datosAnotacion.firstElement());
                                m_Log.info("Vector interno obtenido: tamaño " + datosAnotacion.size());

                                // Rellenar los campos SIR (índices 24?26)
//                  // aquí podrías inyectar filtros adicionales más tarde
                                datosAnotacion.add(registroSIR.getEstado());              // índice 24
                                datosAnotacion.add(DateOperations.toString(registroSIR.getFechaEstado(), DateOperations.LATIN_DATETIME_FORMAT));         // índice 25 aplicar un
                                datosAnotacion.add(registroSIR.getIdentificadorIntercambio()); // índice 26

                                // Insertar en listaFinal sin convertir a VO
                                listaFinal.add(datosAnotacion);
                                contador++;
                            }
                        }
                    } else {
                        m_Log.warn("listaRegistros es NULL, se muestra lista vacía");
                    }
                } catch (Exception e) {
                    m_Log.error("Error al obtener los datos SIR o al construir la lista", e);
                }

                /*
                 * 4) Guardar la lista en sesión y volcar filtros en el form bean
                 */
                request.setAttribute("esConsultaSIR", "1");
                session.setAttribute("RelacionAnotaciones",     listaFinal);
                session.setAttribute("NumRelacionAnotaciones", listaFinal.size());

                // volcamos los filtros en el form bean para la JSP oculta
                registroForm.setCamposListados(listaCamposListados);

                registroForm.setIdentificadorRegistroSIR(       identificador);
                registroForm.setCodEstadoSIR(                    codEstadoSIR);
                registroForm.setCodigoUnidadDestinoSIR(           codigoUnidadDestinoSIR);

                // muy importante: volver a guardar el form bean en sesión
                session.setAttribute("MantAnotacionRegistroForm", registroForm);

                m_Log.info("[consultaEnSIR] registros finales = " + contador);

                // forward a la vista que incluye el oculto + JS redirecciona()
                return mapping.findForward("consultaEnSIR");

            } else if ("consultaOcultaEnSIR".equals(opcion)) {
                // Esta opcion lo unico que hace es cargar la jsp ocultoCargarPaginaRelacionAnotaciones que es la jsp que muestra en la tabla los datos obtenidos en 'consultaEnSIR'

                request.setAttribute("esConsultaSIR", "1");
                return mapping.findForward("consultaOcultaEnSIR");
            } else if ("consultar".equals(opcion)) {
                elRegistroESVO = registroForm.getRegistro();
                elRegistroESVO.setAnoReg(0);
                elRegistroESVO.setNumReg(0L);
                elRegistroESVO.setAutoridad(registroForm.getAutoridad());
                if (request.getParameter("ano") != null) {
                    if (!"".equals(request.getParameter("ano").trim())) {
                        elRegistroESVO.setAnoReg(Integer.parseInt(request.getParameter("ano")));
                    }
                    request.setAttribute("anoReg", request.getParameter("ano"));
                    session.setAttribute("anoEjercicioUltimaConsulta", request.getParameter("ano"));
                }
                if (request.getParameter("numero") != null) {
                    if (!"".equals(request.getParameter("numero").trim())) {
                        elRegistroESVO.setNumReg(Long.parseLong(request.getParameter("numero")));
                    }
                }
                if (request.getParameter("cbTipoEntrada") != null) {
                    if ("".equals(request.getParameter("cbTipoEntrada").trim())) {
                        elRegistroESVO.setTipoAnot(-1);
                    }
                } else {
                    elRegistroESVO.setTipoAnot(-1);
                }

                String asunto = request.getParameter("asunto");
                elRegistroESVO.setAsunto(asunto);
                String observacion = request.getParameter("observaciones");
                elRegistroESVO.setObservaciones(observacion);
                String fechaAnotacion = request.getParameter("fechaAnotacion");
                elRegistroESVO.setFecEntrada(fechaAnotacion);
                String fechaDocumento = request.getParameter("fechaDocumento");
                elRegistroESVO.setFecHoraDoc(fechaDocumento);

                /* Enlace con SGE.
                 * Fecha: 22/07/2003 */
                String fechaDocu = request.getParameter("fechaDoc");
                elRegistroESVO.setFechaDocu(fechaDocu);

                if (request.getParameter("cod_estadoAnotacion") != null) {
                    if ("".equals(request.getParameter("cod_estadoAnotacion").trim())) {
                        elRegistroESVO.setEstAnotacion(-10); // Todas
                    } else if ("9".equals(request.getParameter("cod_estadoAnotacion").trim())) {
                        elRegistroESVO.setEstAnotacion(9);
                    } else if ("1".equals(request.getParameter("cod_estadoAnotacion").trim())) {
                        elRegistroESVO.setEstAnotacion(1);
                    } else if ("2".equals(request.getParameter("cod_estadoAnotacion").trim())) {
                        elRegistroESVO.setEstAnotacion(2);
                    } else if ("0".equals(request.getParameter("cod_estadoAnotacion").trim())) {
                        elRegistroESVO.setEstAnotacion(0);
                    } else if ("-9".equals(request.getParameter("cod_estadoAnotacion").trim())) {
                        elRegistroESVO.setEstAnotacion(-9);
                    } else if ("3".equals(request.getParameter("cod_estadoAnotacion").trim())) {
                        elRegistroESVO.setEstAnotacion(3);
                    } else {
                        elRegistroESVO.setEstAnotacion(-10);
                    }
                } else {
                    elRegistroESVO.setEstAnotacion(-10);
                }

                m_Log.debug("----- estado anotacion " + elRegistroESVO.getEstAnotacion());
                // Recuperar la lista de temas.
                String listTemasSelecc = request.getParameter("listaTemas");
                elRegistroESVO.setListaTemasAsignados(listaTemasSeleccionados(listTemasSelecc));

                // Tipo de consulta.
                String tipoConsulta = request.getParameter("tipoConsulta");
                m_Log.debug("EL TIPO DE CONSULTA AQUI ES: " + tipoConsulta);

                // Busqueda por nombre/ap1/ap2 o razon social o sus combinaciones
                String tipoDocumentoInteresado = request.getParameter("cbTipoDoc");
                String tipoPersonaFisicJurid = null;
                if (tipoDocumentoInteresado != null) {
                    if (!"".equals(tipoDocumentoInteresado.trim()) && (tipoDocumentoInteresado.trim().indexOf('|') == -1) && (tipoDocumentoInteresado.trim().indexOf('&') == -1) && (tipoDocumentoInteresado.trim().indexOf(':') == -1)) {
                        tipoPersonaFisicJurid = AnotacionRegistroManager.getInstance().getIndicadorPersonaFisicaJuridica(tipoDocumentoInteresado.trim(), params);
                        if (tipoPersonaFisicJurid == null) {
                            tipoPersonaFisicJurid = "1";
                        }
                    }
                }

                // Para navegacion entre asientos relacionados, se crea un VO nuevo
                String tipoAsiento = request.getParameter("tipoAsiento");
                if (tipoAsiento != null && !"".equals(tipoAsiento.trim())) {
                    elRegistroESVO = new RegistroValueObject();
                    elRegistroESVO.setTipoReg(tipoAsiento);
                    elRegistroESVO.setAnoReg(Integer.parseInt(request.getParameter("ejercicioAsiento")));
                    elRegistroESVO.setNumReg(Long.parseLong(request.getParameter("numeroAsiento")));
                    elRegistroESVO.setAutoridad(registroForm.getAutoridad());
                    elRegistroESVO.setUnidadOrgan(Integer.parseInt(registroForm.getUnidadOrgan()));
                    elRegistroESVO.setIdentDepart(Integer.parseInt(registroForm.getIdentDepart()));
                    elRegistroESVO.setTipoAnot(-1);
                    elRegistroESVO.setEstAnotacion(ConstantesDatos.REG_ANOTACION_BUSCAR_TODAS);
                }

                elRegistroESVO.setCodTipoDoc(registroForm.getTxtCodigoDocumento());
                elRegistroESVO.setCodTipoTransp(registroForm.getCod_tipoTransporte());
                elRegistroESVO.setCodTipoRemit(registroForm.getCod_tipoRemitente());

                if (!busquedaTercero) {
                    String numDoc = request.getParameter("txtDNI");
                    if (numDoc != null && !numDoc.equalsIgnoreCase("")) {
                        elRegistroESVO.setDocumentoInteresado(numDoc);
                    }//if(numDoc != null && !numDoc.equalsIgnoreCase(""))
                }//if(!busquedaTercero)

                elRegistroESVO.setCamposListados(listaCamposListados);
                //m_Log.debug("CAMPOS LISTADO ");

                elRegistroESVO.setRegistroTelematico(registroForm.isRegistroTelematico());

                try {
                    int numAnots = AnotacionRegistroManager.getInstance().getNumeroTotalAnotaciones(elRegistroESVO, params);
                    m_Log.debug("RECUPERADAS " + numAnots + " ANOTACIONES QUE CUMPLEN LOS CRITERIOS");
                    session.setAttribute("NumRelacionAnotaciones", numAnots);
                } catch (Exception ce) {
                    m_Log.error("MantAnotacionRegistroAction problem" + ce.getMessage(), ce);
                    session.setAttribute("RelacionAnotaciones", new Vector());
                    session.setAttribute("NumRelacionAnotaciones", 0);
                } finally {
                    RegistroValueObject storedConsulta = new RegistroValueObject();
                    storedConsulta.copy(elRegistroESVO);
                    session.setAttribute("registroValueObjectConsulta", storedConsulta);

                    if ("listado".equals(tipoConsulta)) {
                        opcion = "consultarListado";
                    }
                    // else /* Consulta registro a registro, opcion="consultar" */

                    // Opcion para navegacion entre asientos relacionados
                    if (tipoAsiento != null && !"".equals(tipoAsiento.trim())) {
                        opcion = "cargarAltaRE";
                        session.setAttribute("modoInicio", "asientoRelacionado");
                        session.setAttribute("tipoAnotacion", tipoAsiento);
                    }

                    // Inicializamos a vacio.
                    form = new MantAnotacionRegistroForm();
                    registroForm = (MantAnotacionRegistroForm) form;
                    if ("request".equals(mapping.getScope())) {
                        request.setAttribute(mapping.getAttribute(), form);
                    } else {
                        session.setAttribute(mapping.getAttribute(), form);
                    }
                    //===============================
                    //nuevas UORs
                    registroForm.setListaNuevasUORs(nuevasUOR);
                    // arbol de jerarquÃ­a de uors
                    registroForm.setArbol(arbol);
                    //===============================
                    elRegistroESVO = new RegistroValueObject();
                    inicializarListasVacias(elRegistroESVO);
                    elRegistroESVO.setListaActuaciones(new Vector()); // Provisional. Eliminar en limpieza.
                    elRegistroESVO.setCamposListados(listaCamposListados);
                    registroForm.setRegistro(elRegistroESVO);
                   // #262348
                    String generarPeticion = registroConf.getString(usuarioVO.getOrgCod() + "/MODELO_PETICION_RESPUESTA");
                    registroForm.setMostrarGenerarModelo(generarPeticion);
                    // #288821
                    String propiedadJustificante = usuarioVO.getOrgCod() + "/GENERAR_JUSTIFICANTE_DESDE_LISTADO";
                    String generarJustificanteConsulta = "no";
                    try {
                        generarJustificanteConsulta = registroConf.getString(propiedadJustificante);
                    } catch (MissingResourceException mre) {
                        m_Log.error("Ha ocurrido un error al obtener la propiedad " + propiedadJustificante + " de Registro.properties");
                    } catch (CriticalException ce) {
                        m_Log.error("Ha ocurrido un error al obtener la propiedad " + propiedadJustificante + " de Registro.properties");
                    }
                    registroForm.setGenerarJustificanteConsulta(generarJustificanteConsulta);
                    m_Log.info(session.getId() + " " + usuarioActualLog + " ------- opcion: " + opcion);
                }
            } else if ("cargar_pagina".equals(opcion)) {
                //si estas variables tiene valor tenemos que ordenar
                //por una columna en concreo y de forma asc o desc segun tipoOrden
                m_Log.debug("ORDENAR    ");
                int columna = 0;
                String tipoOrden = "";
                String relaciones = "";
                try {
                    relaciones = request.getParameter("procedoRelaciones");
                    //recibo el id del campo que pertece a la columna por la que hay que ordenar
                    columna = Integer.parseInt(request.getParameter("columna"));
                    tipoOrden = request.getParameter("tipoOrden");
                    m_Log.debug("ORDENAR antes de buscar    " + columna + " , " + tipoOrden);
                    //busco en que posisicion del select esta el campo por el que tengo que ordenar.  

                    // if(columna==1){columna=columna;}   
                    if ("false".equals(tipoOrden)) {
                        tipoOrden = "DESC";
                    } else if ("true".equals(tipoOrden)) {
                        tipoOrden = "ASC";
                    }

                    String strNumPagina = request.getParameter("paginaListado");
                    int numPagina = Integer.parseInt(strNumPagina);
                    String strCount = request.getParameter("numLineasPaginaListado");
                    int count = Integer.parseInt(strCount);
                    int startIndex = (numPagina - 1) * count + 1;
                    RegistroValueObject storedConsulta = (RegistroValueObject) session.getAttribute("registroValueObjectConsulta");
                    RegistroValueObject regBusqueda = new RegistroValueObject();
                    regBusqueda.copy(storedConsulta);
                    if (("SI".equals(directiva_salidas_uor_usuario)) && (tipoES != null) && ("S".equals(tipoES) || "Relacion_S".equals(tipoES))) {
                        regBusqueda.setDirectivaUsuPermisoUor(directiva_salidas_uor_usuario);
                    } else {
                        regBusqueda.setDirectivaUsuPermisoUor("NO");
                    }
                    regBusqueda.setUsuarioLogueado(usuarioVO);
                    Vector res = AnotacionRegistroManager.getInstance().relacionRegistroValueObject(regBusqueda, params, startIndex, count, columna, tipoOrden);
                    session.setAttribute("RelacionAnotaciones", res);
                    session.setAttribute("DigitalizacionActiva", servDigitalizacionAct);
                    int numAnots = 0;
                    if ("1".equalsIgnoreCase(strNumPagina)) {
                        //int numAnots = AnotacionRegistroManager.getInstance().getNumeroTotalAnotaciones(regBusqueda, params);
                        if (request.getParameter("numRelacionAnotaciones") != null) {

                            numAnots = Integer.parseInt(request.getParameter("numRelacionAnotaciones"));
                        } else if (session.getAttribute("numRelacionAnotaciones") != null) {
                            numAnots = (Integer) session.getAttribute("NumRelacionAnotaciones");
                        }
                        session.setAttribute("NumRelacionAnotaciones", numAnots);
                    }//if("1".equalsIgnoreCase(strNumPagina))
                    session.setAttribute("paginaListado", strNumPagina);
                    session.setAttribute("relaciones", relaciones);
                    elRegistroESVO = registroForm.getRegistro();
                    regBusqueda.setNumLineasPaginaListado(strCount);

                } catch (Exception e) {
                    m_Log.error("MantAnotacionRegistroAction problem" + e.getMessage(), e);
                    session.setAttribute("RelacionAnotaciones", new Vector());
                    session.setAttribute("NumRelacionAnotaciones", 0);
                } finally {
                    registroForm.setRegistro(elRegistroESVO);
                }
                registroForm.setColumna(columna);
                registroForm.setTipoOrden(tipoOrden);
                opcion = "cargar_pagina";
            } else if ("determinar_anotacion".equals(opcion)) {
                int columna = registroForm.getColumna();
                String tipoOrden = registroForm.getTipoOrden();
                m_Log.debug("ORDENAR antes de buscar    " + columna + " , " + tipoOrden);
                if ("false".equals(tipoOrden)) {
                    tipoOrden = "DESC";
                } else if ("true".equals(tipoOrden)) {
                    tipoOrden = "ASC";
                }

                m_Log.debug("POSICION: " + registroForm.getPosicionAnotacion());
                String strNumPagina = registroForm.getPaginaListado();
                String strCount = registroForm.getNumLineasPaginaListado();
                m_Log.debug("PAGINA: " + strNumPagina + " | CUENTA: " + strCount);

                String strPosAnotacion = request.getParameter("posicionAnotacion");
                int posAnotacion = -1;
                try {
                    posAnotacion = Integer.parseInt(strPosAnotacion);
                } catch (NumberFormatException e) {
                }
                if (posAnotacion == -1) {
                    posAnotacion = 1;
                }
                RegistroValueObject storedConsulta = (RegistroValueObject) session.getAttribute("registroValueObjectConsulta");
                elRegistroESVO = new RegistroValueObject();
                elRegistroESVO.copy(storedConsulta);
                elRegistroESVO.setPosicionAnotacion(strPosAnotacion);
                elRegistroESVO.setNumLineasPaginaListado(strCount);
                elRegistroESVO.setPaginaListado(strNumPagina);

                if (("SI".equals(directiva_salidas_uor_usuario)) && (tipoES != null) && ("S".equals(tipoES) || "Relacion_S".equals(tipoES))) {
                    elRegistroESVO.setDirectivaUsuPermisoUor(directiva_salidas_uor_usuario);
                } else {
                    elRegistroESVO.setDirectivaUsuPermisoUor("NO");
                }
                elRegistroESVO.setUsuarioLogueado(usuarioVO);

                Vector res = AnotacionRegistroManager.getInstance().relacionRegistroValueObject(elRegistroESVO, params, posAnotacion, 1, columna, tipoOrden);
                if (session.getAttribute("NumRelacionAnotaciones") == null) {
                    session.setAttribute("NumRelacionAnotaciones", new Integer(res.size()));
                }
                session.setAttribute("RelacionAnotaciones", res);
                opcion = "determinar_anotacion";

            } else if ("determinarEncontrada".equals(opcion)) {

                String posicionAnotacion = registroForm.getRegistro().getPosicionAnotacion();
                m_Log.debug(" **************** determinarEncontrada: " + posicionAnotacion);
                elRegistroESVO = new RegistroValueObject();
                if ("".equals(posicionAnotacion)) {
                    elRegistroESVO.setPosicionAnotacion("1");
                } else {
                    elRegistroESVO.setPosicionAnotacion(posicionAnotacion);
                }
                elRegistroESVO.setNumLineasPaginaListado("10");
                elRegistroESVO.setPaginaListado("1");
                opcion = "determinar_anotacion";

            } else if ("recargar_encontradas".equals(opcion)) {
                int ano = 0;
                Long numero = 0L;
                if ((request.getParameter("ano") != null) && (request.getParameter("numero") != null)) {
                    if ((!"".equals(request.getParameter("ano").trim())) && (!"".equals(request.getParameter("numero").trim()))) {
                        ano = Integer.parseInt(request.getParameter("ano"));
                        numero = Long.parseLong(request.getParameter("numero"));
                    }
                }

                RegistroValueObject datosRegistro = registroForm.getRegistro();
                elRegistroESVO = new RegistroValueObject();
                elRegistroESVO.setTipoReg(datosRegistro.getTipoReg());
                elRegistroESVO.setAnoReg(ano);
                elRegistroESVO.setNumReg(numero);
                elRegistroESVO.setUnidadOrgan(datosRegistro.getUnidadOrgan());
                elRegistroESVO.setIdentDepart(datosRegistro.getIdentDepart());
                elRegistroESVO.setTipoAnot(-1);
                elRegistroESVO.setEstAnotacion(ConstantesDatos.REG_ANOTACION_BUSCAR_TODAS);

                String POSICION_ANOTACION = datosRegistro.getPosicionAnotacion();

                try {
                    Vector res = AnotacionRegistroManager.getInstance().relacionRegistroValueObject(elRegistroESVO, params, 1, 1, 0, "");
                    session.setAttribute("RelacionAnotaciones", res);
                    session.setAttribute("NumRelacionAnotaciones", new Integer(res.size()));
                    session.setAttribute("modoInicio", "hayModificada");
                    opcion = "cancelar_anular";

                    // Rellenamos el Value Object con parametros por defecto.
                    //===============================
                    elRegistroESVO = new RegistroValueObject();
                    inicializarListasVacias(elRegistroESVO);

                    // Inicializamos a vacio el formulario.
                    form = new MantAnotacionRegistroForm();
                    registroForm = (MantAnotacionRegistroForm) form;

                    //elRegistroESVO.setPosicionAnotacion("1");
                    elRegistroESVO.setNumLineasPaginaListado("10");
                    elRegistroESVO.setPaginaListado("1");
                    elRegistroESVO.setPosicionAnotacion(POSICION_ANOTACION);

                    m_Log.debug(" **************** recargar_encontradas POSIOCION ANOTACION elRegistroESVO: "
                            + elRegistroESVO.getPosicionAnotacion());

                    if ("request".equals(mapping.getScope())) {
                        request.setAttribute(mapping.getAttribute(), form);
                    } else {
                        session.setAttribute(mapping.getAttribute(), form);
                    }
                    registroForm.setListaNuevasUORs(nuevasUOR);
                    registroForm.setArbol(arbol);
                    // Cargar rol por defecto
                    Vector<GeneralValueObject> roles = InteresadosManager.getInstance().getListaRolesRegistro(params);
                    for (GeneralValueObject rol : roles) {
                        if (rol.getAtributo("porDefecto").equals("SI")) {
                            registroForm.setCodRolDefecto((String) rol.getAtributo("codRol"));
                            registroForm.setDescRolDefecto((String) rol.getAtributo("descRol"));
                            break;
                        }
                    }
                    registroForm.setListaRoles(roles);
                    m_Log.debug("Rol por defecto cargado: codigo=" + registroForm.getCodRolDefecto()
                            + ", descripcion=" + registroForm.getDescRolDefecto());
                    registroForm.setObligatorioAsuntoCodificado(asuntoObligatorio);
                    registroForm.setBusquedaAjaxTercero(busquedaTercero);
                } catch (Exception ce) {
                    m_Log.error("MantAnotacionRegistroAction problem" + ce.getMessage());
                    opcion = "error";
                    elRegistroESVO.setRespOpcion(opcion);
                }

            } else if ("recargar_consulta".equals(opcion)) {
                int ano = 0;
                Long numero = 0L;
                if ((request.getParameter("ano") != null) && (request.getParameter("numero") != null)) {
                    if ((!"".equals(request.getParameter("ano").trim())) && (!"".equals(request.getParameter("numero").trim()))) {
                        ano = Integer.parseInt(request.getParameter("ano"));
                        numero = Long.parseLong(request.getParameter("numero"));
                    }
                }

                RegistroValueObject storedConsulta = (RegistroValueObject) session.getAttribute("registroValueObjectConsulta");
                elRegistroESVO = new RegistroValueObject();
                elRegistroESVO.copy(storedConsulta);

                int count;
                int startIndex;
                if (registroForm.getNumLineasPaginaListado().equals("")) {
                    // La recarga viene a partir de un modificar de una anotaciÃ³n en particular.
                    count = 1;
                    startIndex = 1;
                } else {
                    // La recarga viene de una relaciÃ³n de anotaciones.
                    count = Integer.parseInt(registroForm.getNumLineasPaginaListado());
                    int pagina = Integer.parseInt(registroForm.getPaginaListado());
                    startIndex = (pagina - 1) * count + 1;
                }

                try {
                    Vector res = AnotacionRegistroManager.getInstance().relacionRegistroValueObject(elRegistroESVO, params, startIndex, count, 0, "");
                    session.setAttribute("RelacionAnotaciones", res);
                    session.setAttribute("modoInicio", "recargar_buscada");
                    opcion = "cancelar_anular";

                    // Rellenamos el Value Object con parametros por defecto.
                    //===============================
                    elRegistroESVO = new RegistroValueObject();
                    inicializarListasVacias(elRegistroESVO);
                    if ((ano != 0) && (numero != 0)) {
                        elRegistroESVO.setAnoReg(ano);
                        elRegistroESVO.setNumReg(numero);
                    }
                    elRegistroESVO.setPosicionAnotacion(registroForm.getPosicionAnotacion());
                    elRegistroESVO.setNumLineasPaginaListado(registroForm.getNumLineasPaginaListado());
                    elRegistroESVO.setPaginaListado(registroForm.getPaginaListado());

                    // Inicializamos a vacio el formulario.
                    form = new MantAnotacionRegistroForm();
                    registroForm = (MantAnotacionRegistroForm) form;
                    if ("request".equals(mapping.getScope())) {
                        request.setAttribute(mapping.getAttribute(), form);
                    } else {
                        session.setAttribute(mapping.getAttribute(), form);
                    }
                    registroForm.setListaNuevasUORs(nuevasUOR);
                    registroForm.setArbol(arbol);
                    // Cargar rol por defecto
                    Vector<GeneralValueObject> roles = InteresadosManager.getInstance().getListaRolesRegistro(params);
                    for (GeneralValueObject rol : roles) {
                        if (rol.getAtributo("porDefecto").equals("SI")) {
                            registroForm.setCodRolDefecto((String) rol.getAtributo("codRol"));
                            registroForm.setDescRolDefecto((String) rol.getAtributo("descRol"));
                            break;
                        }
                    }
                    registroForm.setListaRoles(roles);
                    m_Log.debug("Rol por defecto cargado: codigo=" + registroForm.getCodRolDefecto()
                            + ", descripcion=" + registroForm.getDescRolDefecto());

                } catch (Exception ce) {
                    m_Log.error("MantAnotacionRegistroAction problem" + ce.getMessage());
                    opcion = "error";
                    elRegistroESVO.setRespOpcion(opcion);
                }
            } else if ("ver_consulta_listado".equals(opcion)) {
                m_Log.debug("*******  COLUMNA    " + registroForm.getColumna());
                m_Log.debug("*******  TIPOoRDEN    " + registroForm.getTipoOrden());
                elRegistroESVO = registroForm.getRegistro();
                if ((request.getParameter("ano") != null) && (request.getParameter("numero") != null)) {
                    if ((!"".equals(request.getParameter("ano").trim())) && (!"".equals(request.getParameter("numero").trim()))) {
                        elRegistroESVO.setAnoReg(Integer.parseInt(request.getParameter("ano")));
                        elRegistroESVO.setNumReg(Long.parseLong(request.getParameter("numero")));
                        request.setAttribute("anoReg", request.getParameter("ano"));
                    }
                }

                elRegistroESVO.setCamposListados(listaCamposListados);
                opcion = "consultarListado";
            } else if ("listaProcedimientos".equals(opcion) || "listaProcedimientosExpRel".equals(opcion)) {
                elRegistroESVO = registroForm.getRegistro();
                String uor = elRegistroESVO.getIdUndTramitad();
                Vector listaProcedimientos;

                if (uor == null || "".equals(uor)) {
                    listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientosVigentes(params);
                } else {
                    listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientosUOR(uor, params);
                }

                elRegistroESVO.setListaProcedimientos(listaProcedimientos);
                registroForm.setRegistro(elRegistroESVO);
            } else if ("imprimirCuneus".equals(opcion)) {
                elRegistroESVO = registroForm.getRegistro();

                //Ponemos la fecha en el formato que nos pidan
                String fechaEntrada = elRegistroESVO.getFecEntrada();
                m_Log.debug("fechaEntrada " + fechaEntrada);
                SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Calendar fechaEntradaC = Calendar.getInstance();
                try {
                    if ((fechaEntrada != null) && !(fechaEntrada.equals(""))) {
                        fechaEntradaC = Calendar.getInstance();
                        fechaEntradaC.setTime(spf.parse(fechaEntrada));
                    }

                    String fecEntrada = null;
                    String formatoFechaRegistro = registroConf.getString(String.valueOf(cod_org) + ConstantesDatos.FORMATO_FECHA_SELLO_REGISTRO);
                    m_Log.debug("formatoFechaRegistro " + formatoFechaRegistro);
                    SimpleDateFormat spf2 = new SimpleDateFormat(formatoFechaRegistro);
                    if (fechaEntradaC != null) {
                        fecEntrada = spf2.format(fechaEntradaC.getTime());
                        m_Log.debug("fecEntrada " + fecEntrada);
                    }

                    elRegistroESVO.setFecEntrada(fecEntrada);

                } catch (Exception e) {
                    m_Log.error("Saltï¿½ excepciï¿½n formateando la fecha, en ManAntoacionRegistroAction", e);
                    e.printStackTrace();

                }

                String tipoReg = elRegistroESVO.getTipoReg();
                int codUor = elRegistroESVO.getUnidadOrgan();
                long numeroAnotacion = elRegistroESVO.getNumReg();
                int resEje = elRegistroESVO.getAnoReg();
                int codigoOficina = dameCodigoOficina(codUor, numeroAnotacion, resEje, tipoReg, params);
                //Necesitamos recuperar el nombre de Oficina
                String nombreOficina = dameNombreDeOficina(codigoOficina, params);
                elRegistroESVO.setNombreOficinaRegistro(nombreOficina);
                String posicionSello = request.getParameter("posicionCuneus");
                String idiomaSello = request.getParameter("idiomaCuneus");
                String nCopiasSello = request.getParameter("nCopiasCuneus");
                String protocolo = StrutsUtilOperations.getProtocol(request);
                m_Log.debug("PROTOCOLO en uso : " + protocolo);
                String sUrl = protocolo + "://" + request.getHeader("Host") + request.getContextPath();
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MantAnotacionRegistroAction. ImprimirSello. posicion cuÃ±o: " + posicionSello);
                }
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MantAnotacionRegistroAction. ImprimirSello. idioma cuÃ±o: " + idiomaSello);
                }
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MantAnotacionRegistroAction. ImprimirSello. nCopias cuÃ±o: " + nCopiasSello);
                }
                String realPath = this.getServlet().getServletContext().getRealPath("");
                request.setAttribute("nombre", ImpresionCuneusManager.getInstance().imprimirCuneus(elRegistroESVO,
                        usuarioVO, posicionSello, idiomaSello, nCopiasSello, sUrl, realPath));
                request.setAttribute("tipoFichero", ImpresionCuneusManager.getInstance().tipoFichero());
                opcion = "redirige";
            } else if ("imprimirCuneus2".equals(opcion)) {
                elRegistroESVO = registroForm.getRegistro();

                elRegistroESVO = registroForm.getRegistro();
                //Ponemos la fecha en el formato que nos pidan
                String fechaEntrada = elRegistroESVO.getFecEntrada();
                SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar fechaEntradaC = Calendar.getInstance();
                try {
                    if ((fechaEntrada != null) && !(fechaEntrada.equals(""))) {
                        fechaEntradaC = Calendar.getInstance();
                        fechaEntradaC.setTime(spf.parse(fechaEntrada));
                    }

                    String fecEntrada = null;
                    String formatoFechaRegistro = registroConf.getString("FORMATO_FECHA_SELLO_REGISTRO");
                    SimpleDateFormat spf2 = new SimpleDateFormat(formatoFechaRegistro);
                    if (fechaEntradaC != null) {
                        fecEntrada = spf2.format(fechaEntradaC.getTime());
                    }

                    elRegistroESVO.setFecEntrada(fecEntrada);

                } catch (Exception e) {
                    m_Log.error("Saltï¿½ excepciï¿½n formateando la fecha, en ManAntoacionRegistroAction", e);
                    e.printStackTrace();

                }

                String tipoReg = elRegistroESVO.getTipoReg();
                int codUor = elRegistroESVO.getUnidadOrgan();
                long numeroAnotacion = elRegistroESVO.getNumReg();
                int resEje = elRegistroESVO.getAnoReg();
                int codigoOficina = dameCodigoOficina(codUor, numeroAnotacion, resEje, tipoReg, params);
                //Necesitamos recuperar el nombre de Oficina
                String nombreOficina = dameNombreDeOficina(codigoOficina, params);
                elRegistroESVO.setNombreOficinaRegistro(nombreOficina);
                String posicionSello = request.getParameter("posicionCuneus");
                String idiomaSello = request.getParameter("idiomaCuneus");
                String nCopiasSello = request.getParameter("nCopiasCuneus");
                String protocolo = StrutsUtilOperations.getProtocol(request);
                m_Log.debug("PROTOCOLO en uso : " + protocolo);
                String sUrl = protocolo + "://" + request.getHeader("Host") + request.getContextPath();
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MantAnotacionRegistroAction. ImprimirSello. posicion cuÃ±o: " + posicionSello);
                }
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MantAnotacionRegistroAction. ImprimirSello. idioma cuÃ±o: " + idiomaSello);
                }

                String realPath = this.getServlet().getServletContext().getRealPath("");
                String url = ImpresionCuneusManager.getInstance().imprimirCuneus(elRegistroESVO,
                        usuarioVO, posicionSello, idiomaSello, nCopiasSello, sUrl, realPath);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(url);
                }
                //response.setCharacterEncoding();
                request.setAttribute("nombre", url);
                request.setAttribute("tipoFichero", ImpresionCuneusManager.getInstance().tipoFichero());
                response.sendRedirect(url);
                opcion = "redirige";
            } else if ("seleccionUOR".equals(opcion)) {
                elRegistroESVO = registroForm.getRegistro();
                registroForm.setRegistro(elRegistroESVO);
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                Vector uors = new Vector();
                ArbolImpl arboluors = new ArbolImpl();

                if (("SI".equals(directiva_salidas_uor_usuario)) && (tipoES != null) && ("S".equals(tipoES) || "Relacion_S".equals(tipoES))) {
                    uors = UORsManager.getInstance().getListaUORsPorNoVisRegistroPermisoUsuario('0', usuario, params);
                    arboluors = UORsManager.getInstance().getArbolUORsPermisoUsuario(true, false, usuario, params);
                } else {
                    uors = UORsManager.getInstance().getListaUORsPorNoVisRegistro('0', params);
                    arboluors = UORsManager.getInstance().getArbolUORs(false, true, false, params);
                }

                m_Log.debug("Recuperadas " + (arboluors.contarNodos() - 1) + " UORs en el Ã¡rbol");
                request.setAttribute("arbolUORs", arboluors);
                request.setAttribute("listaUORs", uors);
                opcion = "seleccionUOR";
            } else if ("seleccionUORFiltroUsu".equals(opcion)) {
                m_Log.info(session.getId() + " " + usuarioActualLog + " Estamos en caso :  " + opcion);
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                elRegistroESVO = registroForm.getRegistro();
                registroForm.setRegistro(elRegistroESVO);
                Vector uors = UORsManager.getInstance().getListaUORsPorNoVisRegistroPermisoUsuario('0', usuario, params);
                m_Log.debug("Recuperadas " + uors.size() + " UORs");
                ArbolImpl arboluors = UORsManager.getInstance().getArbolUORsPermisoUsuario(true, false, usuario, params);
                m_Log.debug("Recuperadas " + (arboluors.contarNodos() - 1) + " UORs en el Ã¡rbol");
                request.setAttribute("arbolUORs", arboluors);
                request.setAttribute("listaUORs", uors);
                opcion = "seleccionUOR";

            } else if ("seleccionClasificacion".equals(opcion)) {
                elRegistroESVO = registroForm.getRegistro();
                String codAsunto = request.getParameter("codAsunto");

                if (codAsunto == null) {
                    codAsunto = "";
                }
                m_Log.debug("El registro pasado es: " + elRegistroESVO);
                m_Log.debug("El codigoAsunto que tenemos es: " + codAsunto);

                //Necesitamos recuperar el arbol y sus estructuras
                ArrayList<HojaArbolClasifAsuntosValueObject> arbolClasifAsuntos = elRegistroESVO.getArbolClasifAsuntos();

                registroForm.setArbolClasifAsuntos(arbolClasifAsuntos);
                ArrayList<String> descripciones = new ArrayList<String>();
                ArrayList<Integer> codigos = new ArrayList<Integer>();
                ArrayList<Vector<MantAsuntosValueObject>> hijos = new ArrayList<Vector<MantAsuntosValueObject>>();
                //Tenemos que obtener el hijo que hay que desplegar
                int hijoParaDesplegar = 0;
                hijoParaDesplegar = MantClasifAsuntosManager.getInstance().dameHijoParaDesplegar(arbolClasifAsuntos, codAsunto);

                m_Log.debug("Construimos el arbol");
                for (int i = 0; i < arbolClasifAsuntos.size(); i++) {

                    HojaArbolClasifAsuntosValueObject hoja = arbolClasifAsuntos.get(i);
                    Vector<MantAsuntosValueObject> hijosHoja = hoja.getAsuntosHijos();
                    String descripcion = hoja.getDescripcion();
                    Integer codigo = hoja.getCodigo();
                    descripciones.add(descripcion);
                    codigos.add(codigo);
                    hijos.add(hijosHoja);

                }

                registroForm.setArbolClasifAsuntos(arbolClasifAsuntos);
                registroForm.setHijosArbolClasifAsuntos(hijos);
                request.setAttribute("arbolClasifAsuntos", arbolClasifAsuntos);
                request.setAttribute("hijos", hijos);
                request.setAttribute("descripcions", descripciones);
                request.setAttribute("codigos", codigos);
                //El hijo que vamos a desplegar serï¿½ el que tenga la clasificaciï¿½n, a la que pertenezca
                //el codAsunto seleccionado
                m_Log.debug("El hijo para desplegar es.... :" + hijoParaDesplegar);
                request.setAttribute("hijoParaDesplegar", hijoParaDesplegar);
                request.setAttribute("codAsuntoSel", codAsunto);
                m_Log.debug("A ï¿½rbore ten un tamaï¿½o de.... :" + arbolClasifAsuntos.size());
                m_Log.debug("Salimos de la opcion:  SELECCION CLASIFICACION");
            } else if (opcion.equals("obtenerOficinaRegistro")) {
                String ofiReg = "SINOFI";
                String tipoAnotacion = request.getParameter("tipoRegistro");
                String tipoInforme = request.getParameter("tipoInforme");

                String origenPeticion = request.getParameter("origen");
                String ejerc = request.getParameter("ejercicio");
                if (origenPeticion == null || !origenPeticion.equals("listadoAnotaciones")) {
                    String numeroAnotacion = request.getParameter("numeroAnotacion");
                    RegistroValueObject registro = new RegistroValueObject();
                    registro.setTipoReg(tipoAnotacion);
                    registro.setAnoReg(Integer.parseInt(ejerc));
                    registro.setNumReg(Integer.parseInt(numeroAnotacion));

                    registro.setUnidadOrgan(cod_uni);
                    registro = AnotacionRegistroManager.getInstance().obtenerOficinaRegistro(registro, params);
                    if (registro.getCodOficinaRegistro() != 0 || registro.getNombreOficinaRegistro() != null) {
                        ofiReg = UORsDAO.getInstance().getCodigoVisibleUorByCodUor(registro.getCodOficinaRegistro().toString(), params[6]);
                    }
                } else if (origenPeticion.equals("listadoAnotaciones")) {
                    RegistroValueObject storedConsulta = (RegistroValueObject) session.getAttribute("registroValueObjectConsulta");
                    RegistroValueObject regBusqueda = new RegistroValueObject();
                    regBusqueda.copy(storedConsulta);

                    ArrayList<UORDTO> listaUORs = (ArrayList<UORDTO>) AnotacionRegistroManager.getInstance().relacionCampoRegistroValueObject(regBusqueda, "RES_OFI", params);
                    boolean primerRegistroMirado = true;
                    for (UORDTO elemento : listaUORs) {
                        if (elemento.getUor_cod_vis() != null) {
                            if (primerRegistroMirado) {
                                primerRegistroMirado = false;
                                ofiReg = elemento.getUor_cod_vis();
                            } else if (!ofiReg.equals("SINOFI") && !elemento.getUor_cod_vis().equals(ofiReg)) {
                                ofiReg = "VARIAS";
                                break;
                            }
                        }
                    }
                }

                GeneralValueObject datos = new GeneralValueObject();
                datos.setAtributo("tipoInforme", tipoInforme);
                datos.setAtributo("oficina", ofiReg);
                datos.setAtributo("ejercicioAnotacion", ejerc);
                datos.setAtributo("tipoAnotacion", tipoAnotacion);
                datos.setAtributo("codApp", usuarioVO.getAppCod());

                // devolvemos los datos como String en formato json
                retornarJSON(new Gson().toJson(datos), response);
                return null;

            } else if ("justificanteEntrada".equals(opcion)) {
                elRegistroESVO = registroForm.getRegistro();

                //Si el acceso es externo no se informa el contenido de MantAnotacionRegistroForm en la request, por lo que los valores necesarios se recuperan directamente de esta
                if (request.getParameter("esAccesoExterno") != null && ((String) request.getParameter("esAccesoExterno")).equals("si")) {
                    elRegistroESVO = new Gson().fromJson((String) request.getParameter("mantARForm"), RegistroValueObject.class);
                }

                if (elRegistroESVO.getCodigoUnidadDestinoSIR() == null || elRegistroESVO.getCodigoUnidadDestinoSIR().isEmpty()) {
                    String codUnidadDestinoSIRHidden = request.getParameter("codigoUnidadDestinoSIRHidden");
                    if (codUnidadDestinoSIRHidden != null)
                        elRegistroESVO.setCodigoUnidadDestinoSIR(codUnidadDestinoSIRHidden);
                }

                if (elRegistroESVO.getUorCodVisible() == null || elRegistroESVO.getUorCodVisible().isEmpty()) {
                    String cod_uniRegDestinoORD = request.getParameter("cod_uniRegDestinoORD");
                    if (cod_uniRegDestinoORD != null) {
                        String codUnidadVisible = UORsManager.getInstance().getCodigoVisibleUorByCodUor(cod_uniRegDestinoORD, params);
                        elRegistroESVO.setUorCodVisible(codUnidadVisible);
                    }
                }

                if (elRegistroESVO.getlistaCodTercero() == null || elRegistroESVO.getlistaCodTercero().isEmpty()) {
                    String listaCodTerceroSIRHidden = request.getParameter("listaCodTerceroSIRHidden");
                    if (listaCodTerceroSIRHidden != null)
                        elRegistroESVO.setlistaCodTercero(listaTemasSeleccionados(listaCodTerceroSIRHidden));
                }
                if (elRegistroESVO.getlistaVersionTercero() == null || elRegistroESVO.getlistaVersionTercero().isEmpty()) {
                    String listaVersionTerceroSIRHidden = request.getParameter("listaVersionTerceroSIRHidden");
                    if (listaVersionTerceroSIRHidden != null)
                        elRegistroESVO.setlistaVersionTercero(listaTemasSeleccionados(listaVersionTerceroSIRHidden));
                }
                if (elRegistroESVO.getlistaCodDomicilio() == null || elRegistroESVO.getlistaCodDomicilio().isEmpty()) {
                    String listaCodDomicilioSIRHidden = request.getParameter("listaCodDomicilioSIRHidden");
                    if (listaCodDomicilioSIRHidden != null)
                        elRegistroESVO.setlistaCodDomicilio(listaTemasSeleccionados(listaCodDomicilioSIRHidden));
                }

                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codAplicacion", request.getParameter("codAplicacion"));
                gVO.setAtributo("aplicacion", request.getParameter("aplicacion"));
                gVO.setAtributo("nombreDocumento", request.getParameter("descripcionJus"));
                gVO.setAtributo("idioma", request.getParameter("idiomaCuneus"));

                String labideSIRActivo = registroConf.getString("INTEGRACION_SIR_LANBIDE");
                boolean generarJustificanteSir = ("1".equalsIgnoreCase(labideSIRActivo) && "E".equalsIgnoreCase(elRegistroESVO.getTipoReg()) && elRegistroESVO.getTipoAnot() == 1 ? true : false);

                // #239565
                String tipoInforme;
                if (request.getParameter("tipoJustificante") != null && !request.getParameter("tipoJustificante").equals("undefined")) {
                    tipoInforme = request.getParameter("tipoJustificante");
                    gVO.setAtributo("codOfiReg", request.getParameter("codOfiReg"));
                    if (tipoInforme.indexOf("_consulta") != -1 && request.getParameter("ejercicioAnotacion") != null) {
                        elRegistroESVO.setAnoReg(Integer.parseInt(request.getParameter("ejercicioAnotacion")));
                        elRegistroESVO.setTipoReg(request.getParameter("tipoAnotacion"));
                        elRegistroESVO.setUnidadOrgan(cod_uni);
                        elRegistroESVO.setNumReg(0L);
                    }

                } else {
                    tipoInforme = "justificante";
                }
                m_Log.debug("Tipo del informe a generar: " + tipoInforme);

                gVO.setAtributo("ejercicio", String.valueOf(elRegistroESVO.getAnoReg()));
                gVO.setAtributo("numero", String.valueOf(elRegistroESVO.getNumReg()));
                gVO.setAtributo("codOur", String.valueOf(elRegistroESVO.getUnidadOrgan()));
                gVO.setAtributo("codTip", String.valueOf(elRegistroESVO.getTipoReg()));
                gVO.setAtributo("tipoInforme", tipoInforme);

                String propiedadFormatoFecha = usuarioVO.getOrgCod() + "/FORMATO_FECHA_JUSTIFICANTE_REGISTRO";
                String formatoFecha = null;
                try {
                    formatoFecha = registroConf.getString(propiedadFormatoFecha);
                } catch (Exception e) {

                    m_Log.error("Error al recuperar la propiedad " + propiedadFormatoFecha + " de Registro.properties");
                }
                gVO.setAtributo("formatoFecha", formatoFecha);

                String xml;
                xml = AnotacionRegistroManager.getInstance().consultaXML(gVO, params);

                String regNum = String.valueOf(elRegistroESVO.getAnoReg()) + "/" + String.valueOf(elRegistroESVO.getNumReg());
                registroForm.setRegNum(regNum);
                registroForm.setRegUor(String.valueOf(elRegistroESVO.getUnidadOrgan()));
                registroForm.setRegTip(String.valueOf(elRegistroESVO.getTipoReg()));
                registroForm.setCodPlantilla(request.getParameter("aplicacion"));
                registroForm.setDatosXML(xml);

                JustificanteRegistroPersonalizadoVO justif = null;
                String uorRegistro = null;
                try {

                    //uorRegistro = AnotacionRegistroDAO.getInstance().getNombreUORRegistro(elRegistroESVO.getTipoReg(),elRegistroESVO.getAnoReg(),elRegistroESVO.getNumReg(),elRegistroESVO.getIdentDepart(), params);
                    m_Log.debug(" ====== MantAnotacionRegistroAction antes de getOficinaUorRegistro() =================>");
                    uorRegistro = AnotacionRegistroDAO.getInstance().getOficinaUorRegistro(elRegistroESVO.getTipoReg(), elRegistroESVO.getAnoReg(), elRegistroESVO.getNumReg(), elRegistroESVO.getIdentDepart(), params);
                    m_Log.debug(" ====== uorRegistro: " + uorRegistro);
                    // Se recupera el justificante de registro personalizado que estï¿½ activo, si lo hay
                    if (generarJustificanteSir)
                        justif = JusticanteRegistro.getJustificantebyName("justificanteRegistroSIR", params);
                    else
                        justif = JustificanteRegistroPersonalizadoManager.getInstance().getJustificanteActivo(tipoInforme, params);
                    m_Log.debug(" ====== justificante activo: " + (justif != null ? justif.toString() : "null"));

                } catch (Exception e) {
                    e.printStackTrace();
                    m_Log.error(" ======= Error al verificar justificante activo: " + e.getMessage(), e);
                }

                boolean errorJustificante = false;
                if (justif != null) {
                    // Se realiza la redirecciï¿½n al action que se encarga de mostrar el justificante
                    // personalizado en formato PDF
                    String propiedad = usuarioVO.getOrgCod() + ConstantesDatos.RUTA_PLANTILLAS_JUSTIFICANTE;
                    m_Log.debug("======== carpeta plantillas registro: " + propiedad);

                    String directorio = "";
                    try {

                        m_Log.debug(" ================== MantAnotacionRegistroAction antes de recuperar directorio con jasper =================>");
                        ResourceBundle configRegistro = ResourceBundle.getBundle("Registro");
                        directorio = configRegistro.getString(propiedad);
                        m_Log.debug(" ================== MantAnotacionRegistroAction directorio de alojamiento de la plantilla: " + directorio);
                    } catch (Exception e) {
                        m_Log.error("Error al recuperar la carpeta que contiene las plantillas de registro: " + e.getMessage());
                        errorJustificante = true;
                    }

                    m_Log.debug(" MantAnotacionRegistroAction errorJustificante: " + errorJustificante);

                    if (errorJustificante) {
                        request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "DIRECTORIO_PLANTILLAS_NO_EXISTE");
                    } else {

                        IEjecutaJustificantePDF pdf = EjecutaJustificantePDFFactoria.getInstance().getImplClass(usuarioVO.getOrgCod());
                        m_Log.debug(" ===========  MantAnotacionRegistroAction variable clase que genera pdf: " + pdf);
                        if (pdf == null) {
                            m_Log.debug(" ===========  MantAnotacionRegistroAction instancia variable pdf nula");
                            m_Log.debug("No se ha podido instanciar la clase que muestra el pdf con el justificante de registro");
                            request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "GENERACION_PDF_IMPL_CLASS_DESCONOCIDA");
                        } else {

                            String nombrePdfGenerado = null;
                            try {
                                boolean barcode = true;
                                if (tipoInforme.indexOf("peticion") != -1) {
                                    barcode = false;
                                }
                                m_Log.debug(" =========== MantAnotacionRegistroAction pdf, antes de generar el PDF ");
                                if (generarJustificanteSir) {
                                    RegistroSIRService registroSIRService = new RegistroSIRService();
                                    AdaptadorSQLBD adaptadorSQLBD = new AdaptadorSQLBD(params);
                                    Vector<RegistroValueObject> listaDocumentos = AnotacionRegistroManager.getInstance().getListaDocumentos(elRegistroESVO, params);
                                    List<Lan6Anexo> lan6DocumentoEntradaSIRList = registroSIRService.getListaAnexosEntradasRegEnvioSIR(listaDocumentos, adaptadorSQLBD, usuarioVO);
                                    List<Lan6InteresadoAsientoRegistral> lan6InteresadoAsientoRegistralList = registroSIRService.getListaLan6InteresadoAsientoRegistral(elRegistroESVO.getlistaCodTercero(), elRegistroESVO.getlistaVersionTercero(), elRegistroESVO.getlistaCodDomicilio(), params);
                                    int idioma = (request.getParameter("idiomaCuneus") != null ? Integer.valueOf(request.getParameter("idiomaCuneus")) : 1);
                                    Map<String, Object> parametrosJustificanteSIR = registroSIRService.prepararParametrosJustificanteRegistro_SIR(elRegistroESVO, lan6InteresadoAsientoRegistralList, lan6DocumentoEntradaSIRList, idioma, adaptadorSQLBD);
                                    nombrePdfGenerado = pdf.ejecutaJustificantePDF_SIR(directorio, justif.getNombreJustificante(), uorRegistro, parametrosJustificanteSIR, directorio);
                                } else
                                    nombrePdfGenerado = pdf.ejecutaJustificantePDF(directorio, justif.getNombreJustificante(), uorRegistro, xml, barcode, directorio);
                                m_Log.debug(" =========== MantAnotacionRegistroAction nombrePdfgenerado: " + nombrePdfGenerado);
                            } catch (Exception e) {
                                m_Log.error("*********** Error al generar el pdf correspondiente al justificante de registro activo: " + e.getMessage());
                                request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "GENERACION_PDF_INCORRECTA");
                            }

                            if (nombrePdfGenerado != null && !"".equals(nombrePdfGenerado)) {
                                m_Log.debug("nombrePdfGenerado: " + nombrePdfGenerado);
                                // Obtenemos la propiedad que indica si se quiere generar e incrustar el csv
                                boolean generarCSV = CodigoSeguroVerificacionHelper.incrustarCSVenJustificante(elRegistroESVO.getIdOrganizacion(), registroConf);
                                if (generarCSV) {
                                    // Generar, incrustar y almacenar el CSV
                                    gVO.setAtributo("idOrganizacion", elRegistroESVO.getIdOrganizacion());
                                    gVO.setAtributo("codDep", Integer.toString(elRegistroESVO.getIdentDepart()));
                                    gVO.setAtributo("usuario", usuarioVO);
                                    gVO.setAtributo("paramsBBDD", usuarioVO.getParamsCon());
                                    gVO.setAtributo("directorio", directorio);
                                    gVO.setAtributo("nombrePdfGenerado", nombrePdfGenerado);
                                    gVO.setAtributo("tipoMimeOriginal", MimeTypes.PDF[0]);
                                    gVO.setAtributo("extensionOriginal", MimeTypes.FILEEXTENSION_PDF);
                                    gVO.setAtributo("isExpHistorico", Boolean.FALSE);

                                    if (AnotacionRegistroManager.getInstance().generarCSVJustificante(gVO)) {
                                        // Refrescamos la lista de documentos
                                        Vector documentos = AnotacionRegistroManager.getInstance().getListaDocumentos(elRegistroESVO, usuarioVO.getParamsCon());
                                        if (documentos != null) {
                                            elRegistroESVO.setListaDocsAsignados(documentos);
                                            request.setAttribute("FICHERO_JUSTIFICANTE", nombrePdfGenerado);
                                            request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "GENERACION_PDF_CORRECTA");
                                        } else {
                                            m_Log.debug("No se ha generado el CSV del justificante de registro correctamente");
                                            request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "GENERACION_PDF_INCORRECTA");
                                        }
                                    } else {
                                        m_Log.debug("No se ha generado el CSV del justificante de registro correctamente");
                                        request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "GENERACION_PDF_INCORRECTA");
                                    }
                                } else {
                                    request.setAttribute("FICHERO_JUSTIFICANTE", nombrePdfGenerado);
                                    request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "GENERACION_PDF_CORRECTA");
                                }
                            } else {
                                m_Log.debug("No se ha generado el pdf del justificante de registro correctamente");
                                request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "GENERACION_PDF_INCORRECTA");
                            }// else
                        }

                    }// else

                    opcion = "justificanteRegistroPersonalizado";
                    m_Log.info(session.getId() + " " + usuarioActualLog + "  ==================> Redirigiendo a opcion: " + opcion);

                } else {

                    Vector plantillas = AnotacionRegistroManager.getInstance().getListaDocumentosJustificantes(params);
                    registroForm.setListaPlantillasJustificantes(plantillas);

                    opcion = "ocultoJustificanteEntrada";
                }

            } else if ("justificanteEntradaDesdeConsulta".equals(opcion)) { // #288821
                elRegistroESVO = registroForm.getRegistro();
                JustificanteRegistroPersonalizadoVO justif = null;
                String tipoInforme = "justificante";
                m_Log.debug("Tipo del informe a generar: " + tipoInforme);
                try {
                    // Se recupera el justificante de registro personalizado que estï¿½ activo, si lo hay
                    justif = JustificanteRegistroPersonalizadoManager.getInstance().getJustificanteActivo(tipoInforme, params);
                    m_Log.debug(" ====== justificante activo: " + justif);
                } catch (Exception e) {
                    e.printStackTrace();
                    m_Log.error(" ======= Error al verificar justificante activo: " + e.getMessage());
                }
                boolean errorJustificante = false;
                if (justif != null) {
                    String anotaciones = request.getParameter("listaAnotaciones");
                    ArrayList<List<Long>> listaNumReg;
                    if (anotaciones.equals("")) {
                        RegistroValueObject storedConsulta = (RegistroValueObject) session.getAttribute("registroValueObjectConsulta");
                        storedConsulta.setUsuarioLogueado(usuarioVO);
                        if (UsuariosGruposManager.getInstance().tienePermisoDirectiva(ConstantesDatos.REGISTRO_S_SOLO_UORS_USUARIO, usuarioVO.getIdUsuario(), params)) {
                            storedConsulta.setDirectivaUsuPermisoUor("SI");
                        }
                        listaNumReg = (ArrayList<List<Long>>) AnotacionRegistroManager.getInstance().relacionCampoRegistroValueObject(storedConsulta, "RES_NUM", params);
                    } else {
                        listaNumReg = new ArrayList<List<Long>>();
                        String[] listaAnotaciones = anotaciones.split(ConstantesDatos.SEPARADOR);
                        String[] partesAnot;
                        for (int pos = 0; pos < listaAnotaciones.length; pos++) {
                            partesAnot = listaAnotaciones[pos].split(";");
                            listaNumReg.add(Arrays.asList(Long.parseLong(partesAnot[1]), Long.parseLong(partesAnot[0])));
                        }
                    }

                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codAplicacion", request.getParameter("codAplicacion"));
                    gVO.setAtributo("idioma", request.getParameter("idiomaCuneus"));

                    elRegistroESVO.setTipoReg(request.getParameter("tipo"));
                    elRegistroESVO.setUnidadOrgan(cod_uni);
                    elRegistroESVO.setIdentDepart(cod_dep);

                    gVO.setAtributo("codOur", String.valueOf(elRegistroESVO.getUnidadOrgan()));
                    gVO.setAtributo("codTip", String.valueOf(elRegistroESVO.getTipoReg()));
                    gVO.setAtributo("tipoInforme", tipoInforme);

                    String propiedadFormatoFecha = usuarioVO.getOrgCod() + "/FORMATO_FECHA_JUSTIFICANTE_REGISTRO";
                    String formatoFecha = null;
                    try {
                        formatoFecha = registroConf.getString(propiedadFormatoFecha);
                    } catch (Exception e) {
                        m_Log.error("Error al recuperar la propiedad " + propiedadFormatoFecha + " de Registro.properties");
                    }
                    gVO.setAtributo("formatoFecha", formatoFecha);

                    String xml;
                    String propiedadDirectorio = usuarioVO.getOrgCod() + ConstantesDatos.RUTA_PLANTILLAS_JUSTIFICANTE;
                    String directorio = "";
                    String uorRegistro = null;

                    try {
                        directorio = registroConf.getString(propiedadDirectorio);
                        m_Log.debug("======== carpeta plantillas registro: " + directorio);
                    } catch (Exception e) {
                        m_Log.error("Error al recuperar la carpeta que contiene las plantillas de registro: " + e.getMessage());
                        errorJustificante = true;
                    }
                    if (errorJustificante) {
                        request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "DIRECTORIO_PLANTILLAS_NO_EXISTE");
                    } else {
                        IEjecutaJustificantePDF pdf = EjecutaJustificantePDFFactoria.getInstance().getImplClass(usuarioVO.getOrgCod());
                        m_Log.debug(" ===========  MantAnotacionRegistroAction variable clase que genera pdf: " + pdf);
                        if (pdf == null) {
                            m_Log.debug(" ===========  MantAnotacionRegistroAction instancia variable pdf nula");
                            m_Log.debug("No se ha podido instanciar la clase que muestra el pdf con el justificante de registro");
                            request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "GENERACION_PDF_IMPL_CLASS_DESCONOCIDA");
                        } else {
                            ArrayList<InputStream> listaPdfs = new ArrayList<InputStream>();
                            boolean barcode = true;
                            if (tipoInforme.indexOf("peticion") != -1) {
                                barcode = false;
                            }
                            for (List numero : listaNumReg) {
                                Long numRegistro = (Long) numero.get(0);
                                Long anoRegistro = (Long) numero.get(1);
                                elRegistroESVO.setNumReg((numRegistro.longValue()));
                                elRegistroESVO.setAnoReg((anoRegistro.intValue()));
                                gVO.setAtributo("numero", Long.toString(elRegistroESVO.getNumReg()));
                                gVO.setAtributo("ejercicio", Integer.toString(elRegistroESVO.getAnoReg()));
                                // Obtenemos el xml con los datos de la anotaciï¿½n
                                xml = AnotacionRegistroManager.getInstance().consultaXML(gVO, params);

                                uorRegistro = AnotacionRegistroDAO.getInstance().getOficinaUorRegistro(elRegistroESVO.getTipoReg(), elRegistroESVO.getAnoReg(), elRegistroESVO.getNumReg(), elRegistroESVO.getIdentDepart(), params);
                                m_Log.debug(" ====== uorRegistro: " + uorRegistro);
                                // Obtenemos el array de bytes del pdf generado
                                byte[] informe = null;
                                try {
                                    m_Log.debug(" =========== MantAnotacionRegistroAction pdf, antes de generar el PDF ");
                                    informe = pdf.generaJustificantePDF(directorio, justif.getNombreJustificante(), uorRegistro, xml, barcode);
                                } catch (Exception e) {
                                    m_Log.error("*********** Error al generar el pdf correspondiente al justificante de registro activo: " + e.getMessage());
                                }
                                if (informe != null) {
                                    m_Log.debug(" =========== MantAnotacionRegistroAction tamaï¿½o byte[] pdf generado: " + informe.length);
                                    listaPdfs.add(new ByteArrayInputStream(informe));
                                }
                            }
                            if (listaPdfs.size() > 0) {
                                String nombreFicheroDestino = justif.getNombreJustificante() + System.currentTimeMillis() + ".pdf";
                                File destFile = new File(directorio, nombreFicheroDestino);
                                PdfMerger.concatPDFs(listaPdfs, new FileOutputStream(destFile), true);
                                request.setAttribute("FICHERO_JUSTIFICANTE", nombreFicheroDestino);
                                request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "GENERACION_MASIVA_PDF_CORRECTA");
                            } else {
                                m_Log.debug("No se ha generado el pdf de justificante de registro para ninguna anotaciï¿½n. No se puede hacer el merge.");
                                request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "GENERACION_PDF_INCORRECTA");
                            }// else
                        }
                    }
                } else {
                    m_Log.debug("No hay ninguna plantilla .jasper activa.");
                    request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "NO_EXISTE_PLANTILLA_ACTIVA");
                }

                // Se realiza la redirecciï¿½n al action que se encarga de mostrar el justificante
                // personalizado en formato PDF
                opcion = "justificanteRegistroPersonalizado";
                m_Log.info(session.getId() + " " + usuarioActualLog + "  ==================> Redirigiendo a opcion: " + opcion);

            } else if ("generaJustificanteEntrada".equals(opcion)) {
                elRegistroESVO = registroForm.getRegistro();

                Vector plantillas = AnotacionRegistroManager.getInstance().getListaDocumentosJustificantes(params);
                registroForm.setListaPlantillasJustificantes(plantillas);

                opcion = "justificanteEntrada";

            } else if ("altaDocumento".equals(opcion)) {
                elRegistroESVO = registroForm.getRegistro();

                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codAplicacion", request.getParameter("codAplicacion"));
                gVO.setAtributo("aplicacion", request.getParameter("aplicacion"));
                gVO.setAtributo("nombreDocumento", request.getParameter("descripcionJus"));
                gVO.setAtributo("idioma", request.getParameter("idiomaCuneus"));

                gVO.setAtributo("ejercicio", String.valueOf(elRegistroESVO.getAnoReg()));
                gVO.setAtributo("numero", String.valueOf(elRegistroESVO.getNumReg()));
                gVO.setAtributo("codOur", String.valueOf(elRegistroESVO.getUnidadOrgan()));
                gVO.setAtributo("codTip", String.valueOf(elRegistroESVO.getTipoReg()));
                gVO.setAtributo("codDep", String.valueOf(elRegistroESVO.getIdentDepart()));

                // #239565: Es necesario aï¿½adir un atributo a gVO que indique que el tipo de informe es 'justificante' para que consultaXML funcione correctamente
                gVO.setAtributo("tipoInforme", "justificante");

                String xml = "";
                Map mapaEtiq = new HashMap();
                if (!request.getParameter("editorJustif").equals("ODT")) {
                    xml = AnotacionRegistroManager.getInstance().consultaXML(gVO, params);

                    if (m_Conf.getString("editorPlantillas").equalsIgnoreCase("WORD")) {
                        opcion = "altaDocumentoJustificante";
                    } else if (m_Conf.getString("editorPlantillas").equalsIgnoreCase("OOFFICE")) {
                        opcion = "altaDocumentoJustificanteOOffice";
                    }
                } else {
                    mapaEtiq = AnotacionRegistroManager.getInstance().consultaHashEtiquetasValor(gVO, params);
                    opcion = "justificanteODT";
                }

                registroForm.setCodPlantilla(request.getParameter("aplicacion"));
                registroForm.setDatosXML(xml);
                registroForm.setCampos(mapaEtiq);

                if (opcion.equals("justificanteODT")) {
                    ResultadoAjax<String> respuesta = new ResultadoAjax<String>();
                    respuesta.setStatus(ConstantesAjax.STATUS_AJAX_OK);
                    respuesta.setDescStatus(ConstantesAjax.DESC_STATUS_AJAX_OK);
                    DocumentosAplicacionManager docAplMan = DocumentosAplicacionManager.getInstance();
                    GeneralValueObject justifGVO = new GeneralValueObject();
                    justifGVO.setAtributo("codDocumento", registroForm.getCodPlantilla());

                    BufferedOutputStream bos = null;
                    byte[] fichero = null;

                    try {
                        justifGVO = docAplMan.loadDocumento(justifGVO, params);
                        String nombreDocumento = (String) justifGVO.getAtributo("nombre");

                        String extension = ConstantesDatos.EXTENSION_JUSTIFICANTE_REGISTRO_OPPENOFFICE;
                        if (justifGVO != null) {
                            String PATH_DIR_DOC = null;
                            try {
                                PATH_DIR_DOC = DocumentOperations.crearDocumentoDesdePlantilla(gVO, nombreDocumento, extension, (byte[]) justifGVO.getAtributo("fichero"), mapaEtiq, session.getId(), true);
                                if (PATH_DIR_DOC != null) {
                                    fichero = FileUtils.readFileToByteArray(new File(PATH_DIR_DOC.toString()));
                                    if (fichero != null) {
                                        respuesta.setResultado(PATH_DIR_DOC);

                                        // Obtenemos la propiedad que indica si se quiere generar e incrustar el csv
                                        boolean generarCSV = CodigoSeguroVerificacionHelper.incrustarCSVenJustificante(usuarioVO.getOrgCod(), registroConf);
                                        if (generarCSV) {
                                            gVO.setAtributo("idOrganizacion", elRegistroESVO.getIdOrganizacion());
                                            gVO.setAtributo("codDep", Integer.toString(elRegistroESVO.getIdentDepart()));
                                            gVO.setAtributo("usuario", usuarioVO);
                                            gVO.setAtributo("paramsBBDD", usuarioVO.getParamsCon());

                                            gVO.setAtributo("nombrePdfGeneradoCompleto", PATH_DIR_DOC);
                                            gVO.setAtributo("tipoMimeOriginal", MimeTypes.PDF[0]);
                                            gVO.setAtributo("extensionOriginal", MimeTypes.FILEEXTENSION_PDF);
                                            gVO.setAtributo("isExpHistorico", Boolean.FALSE);

                                            if (AnotacionRegistroManager.getInstance().generarCSVJustificante(gVO)) {
                                                // Refrescamos la lista de documentos
                                                Vector documentos = AnotacionRegistroManager.getInstance().getListaDocumentos(elRegistroESVO, usuarioVO.getParamsCon());
                                                if (documentos != null) {
                                                    elRegistroESVO.setListaDocsAsignados(documentos);
                                                    request.setAttribute("FICHERO_JUSTIFICANTE", nombreDocumento);
                                                    request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "GENERACION_PDF_CORRECTA");
                                                } else {
                                                    m_Log.debug("No se ha generado el CSV del justificante de registro correctamente");
                                                    request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "GENERACION_PDF_INCORRECTA");
                                                }
                                            } else {
                                                m_Log.debug("No se ha generado el CSV del justificante de registro correctamente");
                                                request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "GENERACION_PDF_INCORRECTA");
                                            }
                                        } else {
                                            request.setAttribute("FICHERO_JUSTIFICANTE", nombreDocumento);
                                            request.setAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO", "GENERACION_PDF_CORRECTA");
                                        }
                                    } else {
                                        throw new TechnicalException("No se ha generado correctamente el archivo pdf de salida");
                                    }
                                } else {
                                    throw new TechnicalException("No se ha generado correctamente el archivo pdf de salida");
                                }
                            } catch (JODReportsException jre) {
                                jre.printStackTrace();
                                throw new TechnicalException(jre.getMessage());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                throw new TechnicalException(ex.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        respuesta.setStatus(1);
                        respuesta.setDescStatus("Ha ocurrido un error en la generaci?n del justificante desde plantilla .odt");
                        m_Log.error("Error: " + e.getMessage());
                    }

                    WebOperations.retornarJSON(new Gson().toJson(respuesta), response);
                    return null;
                }

                //**************************************
                // CARGAR ASUNTO
                //**************************************
            } else if ("cargarAsunto".equals(opcion)) {
                // Tomamos los valores de la clave primaria del asunto
                elRegistroESVO = registroForm.getRegistro();
                MantAsuntosValueObject asunto = new MantAsuntosValueObject();
                asunto.setCodigo(registroForm.getCodAsunto());
                asunto.setUnidadRegistro(registroForm.getUniRegAsunto());

                // Recuperamos el asunto. Si no hay asunto seleccionado indicamos unidad
                // tramitadora = '-1' para cargar solo los roles en altaRE.jsp->cargarDatosAsunto.
                if (!registroForm.getRegistro().getCodAsunto().equals("")) {
                    asunto = MantAsuntosManager.getInstance().cargarAsunto(asunto, params);
                } else {
                    asunto.setUnidadTram("-1");
                }

                // Obtenemos los roles del procedimiento o por defecto.
                Vector<GeneralValueObject> rolesNuevos
                        = obtenerRoles(asunto.getProcedimiento(), asunto.getMunProc(), params);

                // Comprobamos cual es el codigo del rol por defecto y lo asignamos
                // al form para poder usarlo en la jsp para altas de terceros.
                for (GeneralValueObject rol : rolesNuevos) {
                    if (rol.getAtributo("porDefecto").equals("SI")) {
                        registroForm.setCodRolDefecto((String) rol.getAtributo("codRol"));
                        registroForm.setDescRolDefecto((String) rol.getAtributo("descRol"));
                        break;
                    }
                }
                registroForm.setListaRoles(rolesNuevos);
                registroForm.setMun_procedimiento(asunto.getMunProc());
                // Creamos un mapping de los roles anteriores a los nuevos,
                // casando los de igual nombre y el resto al rol por defecto.
                String proc = asunto.getProcedimiento();
                m_Log.debug("Codigo procedimiento anterior: '" + registroForm.getCodProcedimientoRoles() + "', codigo nuevo: '" + proc + "'");
                Vector<GeneralValueObject> rolesAnteriores
                        = obtenerRoles(registroForm.getCodProcedimientoRoles(), registroForm.getMun_procedimiento(), params);
                mapearRoles(rolesAnteriores, rolesNuevos, registroForm);

                // Obtenemos los documentos del procedimiento, si el asunto tiene procedimiento.
                Vector<ElementoListaValueObject> docs
                        = obtenerDocsProcedimiento(asunto.getProcedimiento(), asunto.getMunProc(), params);

                // Si hay docs del procedimiento se descartan los del asunto
                if (docs.size() > 0) {
                    asunto.setListaDocs(docs);
                }

                // Se asignan los documentos al form, conservando los existentes
                // que tengan contenido.
                asignarDocumentos(asunto.getListaDocs(), registroForm);

                // Asignamos el asunto al form.
                registroForm.setAsuntoVO(asunto);
                registroForm.setRespOpcion("cargarAsunto");

                // Comprobamos si se trata de un acceso externo y si es así lo indicamos en la respuesta
                String esAccesoExterno = request.getParameter("esAccesoExterno");
                if (esAccesoExterno != null && ((String) esAccesoExterno).equals("si")) {
                    request.setAttribute("esAccesoExterno", esAccesoExterno);
                }
                //**************************************
                // COMPROBAR CARGAR ASUNTO
                // Se comprueba si al cargar el asunto se perdera algun documento
                // o se cambiara algun rol.
                //**************************************
            } else if ("comprobarCargarAsunto".equals(opcion)) {
                // Tomamos los valores de la clave primaria del asunto
                elRegistroESVO = registroForm.getRegistro();
                MantAsuntosValueObject asunto = new MantAsuntosValueObject();
                asunto.setCodigo(registroForm.getCodAsunto());
                asunto.setMunProc(registroForm.getMun_procedimiento());

                /**
                 * original
                 * asunto.setUnidadRegistro(registroForm.getUniRegAsunto());
                 */
                /**
                 * prueba *
                 */
                asunto.setUnidadRegistro(registroForm.getUnidadOrgan());
                /**
                 * prueba **
                 */

                // Recuperamos el asunto.
                if (!registroForm.getCodAsunto().equals("")) {
                    asunto = MantAsuntosManager.getInstance().cargarAsunto(asunto, params);
                } else {
                    asunto.setProcedimiento("");
                }

                // Obtenemos los roles del procedimiento o por defecto e
                // insertamos las descripciones en una lista.
                Vector<GeneralValueObject> rolesNuevos
                        = obtenerRoles(asunto.getProcedimiento(), asunto.getMunProc(), params);
                ArrayList<String> descRolesNuevos = new ArrayList<String>();
                for (GeneralValueObject rol : rolesNuevos) {
                    descRolesNuevos.add((String) rol.getAtributo("descRol"));
                }

                // Comprobamos si los roles de los terceros tienen todos mapeo.
                boolean sePierdenRoles = false;
                String listaDescRol = request.getParameter("listaDescRol");
                StringTokenizer rolesAnteriores = new StringTokenizer(listaDescRol, "§¥", false);
                while (rolesAnteriores.hasMoreTokens()) {
                    String rol = rolesAnteriores.nextToken();
                    if (!descRolesNuevos.contains(rol)) {
                        sePierdenRoles = true;
                        break;
                    }
                }

                // Obtenemos los documentos del procedimiento, si el asunto tiene procedimiento.
                Vector<ElementoListaValueObject> docs
                        = obtenerDocsProcedimiento(asunto.getProcedimiento(), asunto.getMunProc(), params);

                // Si no hay docs del procedimiento NUEVO, nos quedamos con los del asunto.
                if (docs.size() == 0) {
                    docs = asunto.getListaDocs();
                }

                // Comprobamos si se pierde algun documento
                boolean sePierdenDocumentos = false;
                if (docs.size() >= 0) {
                    ArrayList<String> docsNuevos = new ArrayList<String>();

                    if (docs.size() > 0) {
                        for (ElementoListaValueObject doc : docs) {
                            docsNuevos.add(doc.getDescripcion());
                        }
                    }

                    for (RegistroValueObject doc
                            : (Vector<RegistroValueObject>) registroForm.getListaDocsAsignados()) {

                        String tipoDoc = doc.getTipoDoc();
                        // Si no tienen tipo no tienen contenido y pueden ser eliminados
                        if ((tipoDoc == null || tipoDoc.equals(""))
                                && !docsNuevos.contains(doc.getNombreDoc())) {
                            sePierdenDocumentos = true;
                            break;
                        }
                    }
                }

                if (sePierdenRoles && sePierdenDocumentos) {
                    registroForm.setRespOpcion("rolesYDocs");
                } else if (sePierdenRoles) {
                    registroForm.setRespOpcion("roles");
                } else if (sePierdenDocumentos) {
                    registroForm.setRespOpcion("docs");
                } else {
                    registroForm.setRespOpcion("confirmado");
                }


                //**************************************
                // CARGAR PROCEDIMIENTO
                //**************************************
            } else {
                if ("recargarComboAsuntos".equals(opcion)) {
                    elRegistroESVO = registroForm.getRegistro();
                    MantAsuntosValueObject asunto = new MantAsuntosValueObject();

                    Vector<MantAsuntosValueObject> asuntos = MantAsuntosManager.getInstance().buscarAsuntos(elRegistroESVO, true, params);
                    m_Log.debug("Se han recuperado " + asuntos.size() + " asuntos");
                    request.setAttribute("asuntos_combo", asuntos);
                    opcion = "recargarComboAsuntos";

                } else if ("cargarProcedimiento".equals(opcion)) {
                    // No se usa, pero hay que leer el VO en esta variable o se pierde.
                    elRegistroESVO = registroForm.getRegistro();

                    // Obtenemos los roles del procedimiento o por defecto.
                    Vector<GeneralValueObject> rolesNuevos
                            = obtenerRoles(registroForm.getCod_procedimiento(), registroForm.getMun_procedimiento(), params);

                    // Comprobamos cual es el codigo del rol por defecto y lo asignamos
                    // al form para poder usarlo en la jsp para altas de terceros.
                    String codRolDefecto = "1";
                    for (GeneralValueObject rol : rolesNuevos) {
                        if (rol.getAtributo("porDefecto").equals("SI")) {
                            codRolDefecto = (String) rol.getAtributo("codRol");
                            registroForm.setCodRolDefecto(codRolDefecto);
                            registroForm.setDescRolDefecto((String) rol.getAtributo("descRol"));
                            break;
                        }
                    }
                    registroForm.setListaRoles(rolesNuevos);

                    // Creamos un mapping de los roles anteriores a los nuevos,
                    // casando los de igual nombre y el resto al rol por defecto.
                    String proc = registroForm.getCod_procedimiento();
                    m_Log.debug("Codigo procedimiento anterior: '" + registroForm.getCodProcedimientoRoles() + "', codigo nuevo: '" + proc + "'");
                    Vector<GeneralValueObject> rolesAnteriores
                            = obtenerRoles(registroForm.getCodProcedimientoRoles(), registroForm.getMun_procedimiento(), params);
                    mapearRoles(rolesAnteriores, rolesNuevos, registroForm);

                    // Obtenemos los documentos del procedimiento
                    Vector<ElementoListaValueObject> docs
                            = obtenerDocsProcedimiento(registroForm.getCod_procedimiento(), registroForm.getMun_procedimiento(), params);

                    // Se asignan los documentos al form, conservando los existentes
                    // que tengan contenido.
                    asignarDocumentos(docs, registroForm);

                    registroForm.setRespOpcion("cargarProcedimiento");

                    // Comprobamos si se trata de un acceso externo y si es así lo indicamos en la respuesta
                    String esAccesoExterno = request.getParameter("esAccesoExterno");
                    if (esAccesoExterno != null && ((String) esAccesoExterno).equals("si")) {
                        request.setAttribute("esAccesoExterno", esAccesoExterno);
                    }

                    //**************************************
                    // COMPROBAR CARGAR PROCEDIMIENTO
                    // Se comprueba si al cargar el procedimiento se perdera algun
                    // documento o se cambiara algun rol.
                    //**************************************
                } else if ("comprobarCargarProcedimiento".equals(opcion)) {

                    // No se usa, pero hay que leer el VO en esta variable o se pierde.
                    elRegistroESVO = registroForm.getRegistro();

                    // Obtenemos los roles del procedimiento o por defecto.
                    Vector<GeneralValueObject> rolesNuevos
                            = obtenerRoles(registroForm.getCod_procedimiento(), registroForm.getMun_procedimiento(), params);

                    // Insertamos las descripciones de los roles nuevos en un lista
                    ArrayList<String> descRolesNuevos = new ArrayList<String>();
                    for (GeneralValueObject rol : rolesNuevos) {
                        descRolesNuevos.add((String) rol.getAtributo("descRol"));
                    }

                    // Comprobamos si los roles de los terceros tienen todos mapeo.
                    boolean sePierdenRoles = false;
                    String listaDescRol = request.getParameter("listaDescRol");
                    StringTokenizer rolesAnteriores = new StringTokenizer(listaDescRol, "§¥", false);
                    while (rolesAnteriores.hasMoreTokens()) {
                        String rol = rolesAnteriores.nextToken();
                        if (!descRolesNuevos.contains(rol)) {
                            sePierdenRoles = true;
                            break;
                        }
                    }

                    // Obtenemos los documentos del procedimiento, si el asunto tiene procedimiento.
                    Vector<ElementoListaValueObject> docs
                            = obtenerDocsProcedimiento(registroForm.getCod_procedimiento(), registroForm.getMun_procedimiento(), params);

                    // Comprobamos si se pierde algun documento
                    boolean sePierdenDocumentos = false;
                    if (docs.size() > 0) {
                        ArrayList<String> docsNuevos = new ArrayList<String>();
                        for (ElementoListaValueObject doc : docs) {
                            docsNuevos.add(doc.getDescripcion());
                        }
                        for (RegistroValueObject doc
                                : (Vector<RegistroValueObject>) registroForm.getListaDocsAsignados()) {

                            String tipoDoc = doc.getTipoDoc();
                            // Si no tienen tipo no tienen contenido y pueden ser eliminados
                            if ((tipoDoc == null || tipoDoc.equals(""))
                                    && !docsNuevos.contains(doc.getNombreDoc())) {
                                sePierdenDocumentos = true;
                                break;
                            }
                        }
                    }

                    if (sePierdenRoles && sePierdenDocumentos) {
                        registroForm.setRespOpcion("rolesYDocs");
                    } else if (sePierdenRoles) {
                        registroForm.setRespOpcion("roles");
                    } else if (sePierdenDocumentos) {
                        registroForm.setRespOpcion("docs");
                    } else {
                        registroForm.setRespOpcion("confirmado");
                    }

                    //**************************************
                    // EXISTE ASIENTO
                    // Comprueba la existencia de un asiento
                    //**************************************
                } else if ("existeAsiento".equals(opcion)) {
                    //No se usa, pero hay que leer el VO en esta variable o se pierde.
                    elRegistroESVO = registroForm.getRegistro();

                    SimpleRegistroValueObject asiento = new SimpleRegistroValueObject();
                    asiento.setTipo(request.getParameter("codTipoAsiento"));
                    asiento.setEjercicio(request.getParameter("ano"));
                    asiento.setNumero(request.getParameter("numero"));
                    asiento.setUor(registroForm.getUnidadOrgan());
                    asiento.setDep(registroForm.getIdentDepart());

                    try {
                        boolean existeAsiento = AnotacionRegistroManager.getInstance().existeAsiento(asiento, params);
                        m_Log.debug("EXISTE ASIENTO:" + existeAsiento);
                        if (existeAsiento) {
                            registroForm.setRespOpcion("existe");
                        } else {
                            registroForm.setRespOpcion("noExiste");
                        }
                    } catch (Exception ce) {
                        m_Log.error("MantAnotacionRegistroAction problem" + ce.getMessage());
                        ce.printStackTrace();
                        registroForm.setRespOpcion("noExiste");
                    }
                    //buscar una anotacion en relaciones
                } else if ("busquedaEntradas".equals(opcion)) {
                    // Guardamos el registro de trabajo en la sesion para no perder sus valores.
                    elRegistroESVO = registroForm.getRegistro();
                    session.setAttribute("registroTrabajo", elRegistroESVO);
                    session.setAttribute("tipoTrabajo", elRegistroESVO.getTipoReg());
                    inicializarListas(elRegistroESVO, false, params); // Cargar listas.
                    session.setAttribute("tipEntrada", request.getParameter("tipo"));
                } else if ("cerrarBusquedaRelaciones".equals(opcion)) {
                    // Descartamos la informaciÃ³n guardada de la bÃºsqueda.
                    session.removeAttribute("registroValueObjectConsulta");

                    // Recuperamos la informaciÃ³n de la anotacion de trabajo
                    elRegistroESVO = new RegistroValueObject();
                    elRegistroESVO = (RegistroValueObject) session.getAttribute("registroTrabajo");
                    elRegistroESVO.setTipoReg((String) session.getAttribute("tipoTrabajo"));
                    session.removeAttribute("registroTrabajo");

                    elRegistroESVO.setRespOpcion("cerrarVentanaBusqueda");
                    registroForm.setRespOpcion("cerrarVentanaBusqueda");
                    session.setAttribute("tipEntrada", session.getAttribute("tipoTrabajo"));
                    String tA = (String) session.getAttribute("tipoTrabajo");
                    session.setAttribute("tipoAnotacion", tA);
                } else if ("cargarTemas".equals(opcion)) {
                    String strBusqueda = request.getParameter("busqueda");
                    boolean busqueda = Boolean.parseBoolean(strBusqueda);

                    String strModificando = request.getParameter("modificando");
                    String codActuacion = request.getParameter("codActuacion");

                    Vector temas = MantTemaManager.getInstance().buscaTemas(params);
                    request.setAttribute("temas", temas);

                    String listTemasSelecc = request.getParameter("listaTemas");
                    Vector listaCodigosTemas = listaTemasSeleccionados(listTemasSelecc);
                    Vector temasSeleccionados = new Vector();
                    for (Object objCodTema : listaCodigosTemas) {
                        String codTema = (String) objCodTema;
                        for (Object objTema : temas) {
                            MantTemasValueObject temaVO = (MantTemasValueObject) objTema;
                            if (temaVO.getCodigo().equals(codTema)) {
                                temasSeleccionados.add(temaVO);
                            }
                        }
                    }
                    m_Log.debug("NUMERO DE TEMAS SELECCIONADOS = " + temasSeleccionados.size());
                    request.setAttribute("temasSeleccionados", temasSeleccionados);

                    if (strModificando.equals("S")) {
                        String fechaAnotacion = request.getParameter("fechaAnotacion");
                        Vector actuaciones = ManActuacionesManager.getInstance().buscaActuacionesByFecha(params, fechaAnotacion);
                        request.setAttribute("actuaciones", actuaciones);
                        m_Log.debug("SE HAN RECUPERADO " + actuaciones.size() + " ACTUACIONES");
                    } else {
                        Vector actuaciones = ManActuacionesManager.getInstance().buscaActuaciones(params);
                        request.setAttribute("actuaciones", actuaciones);
                        if (busqueda) {
                            strModificando = "S";
                        }
                    }

                    request.setAttribute("modificando", strModificando);
                    request.setAttribute("codActuacion", codActuacion);

                    elRegistroESVO = registroForm.getRegistro();

                    //**************************************
                    // CARGAR HISTORICO
                    // Carga el historico de movimientos de un asiento
                    //**************************************
                } else if ("cargarHistorico".equals(opcion)) {
                    elRegistroESVO = registroForm.getRegistro();

                    //Si el acceso es externo no se informa el contenido de MantAnotacionRegistroForm en la request, por lo que los valores necesarios se recuperan directamente de esta
                    if (request.getParameter("esAccesoExterno") != null && ((String) request.getParameter("esAccesoExterno")).equals("si")) {
                        elRegistroESVO = new RegistroValueObject();
                        elRegistroESVO.setIdentDepart(Integer.parseInt((String) request.getParameter("depReg")));
                        elRegistroESVO.setUnidadOrgan(Integer.parseInt((String) request.getParameter("uniReg")));
                        elRegistroESVO.setTipoReg((String) request.getParameter("tipoReg"));
                        elRegistroESVO.setAnoReg(Integer.parseInt((String) request.getParameter("anoReg")));
                        elRegistroESVO.setNumReg(Long.parseLong((String) request.getParameter("numReg")));
                    }

                    // Clave para buscar la anotaciÃ³n en el histÃ³rico
                    String claveHistorico
                            = HistoricoAnotacionHelper.crearClaveHistorico(elRegistroESVO);
                    m_Log.debug("Buscar los movimientos de la anotacion " + claveHistorico);
                    m_Log.info(session.getId() + " " + usuarioActualLog + " (NO ERROR)Traza control opciï¿½n  cargarHistorico antes de llamar a obtenerHistoricoAnotacion ");
                    Vector<HistoricoMovimientoValueObject> listadoMovimientos
                            = HistoricoMovimientoManager.getInstance().obtenerHistoricoAnotacion(claveHistorico, params);
                    m_Log.info(session.getId() + " " + usuarioActualLog + " (NO ERROR)Traza control opciï¿½n  cargarHistorico despues de llamar a obtenerHistoricoAnotacion ");
                    // Hay que obtener las descripciones de cada tipo de movimiento
                    // Creamos un traductor para el usuario actual
//                    TraductorAplicacionBean traductor = new TraductorAplicacionBean();
//                    traductor.setApl_cod(ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA);
//                    traductor.setIdi_cod(usuarioVO.getIdioma());

                    m_Log.info(session.getId() + " " + usuarioActualLog + " (NO ERROR)Traza control opciï¿½n  cargarHistorico antes de llamar a getDescripcionMovimiento ");
                    for (HistoricoMovimientoValueObject hvo : listadoMovimientos) {
                        hvo.setDescMovimiento(
                                HistoricoAnotacionTraductorHTML.getDescripcionMovimiento(
                                        traductorAplicacionBean, hvo.getTipoMovimiento())
                        );
                        m_Log.debug(hvo);
                    }

                    registroForm.setMovimientosHistorico(listadoMovimientos);
                    m_Log.info(session.getId() + " " + usuarioActualLog + " (NO ERROR)Traza control opciï¿½n  cargarHistorico END ");

                    //**************************************
                    // CARGAR MOVIMIENTO HISTORICO
                    // Carga los detalles de un movimiento de un asiento
                    //**************************************
                } else if ("cargarMovimientoHistorico".equals(opcion)) {
                    elRegistroESVO = registroForm.getRegistro();
                    String codigoMovimiento = request.getParameter("operacion");
                    m_Log.debug("Buscar el movimiento numero " + codigoMovimiento);

                    // Recuperamos el movimiento por su cÃ³digo
                    HistoricoMovimientoValueObject mov
                            = HistoricoMovimientoManager.getInstance().obtenerMovimiento(Integer.parseInt(codigoMovimiento), params);
                    m_Log.debug(mov);

                    // Transformar la informaciÃ³n en HTML para mostrar
//                    TraductorAplicacionBean mensajes = new TraductorAplicacionBean();
//                    mensajes.setApl_cod(ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA);
//                    mensajes.setIdi_cod(usuarioVO.getIdioma());
                    String detalles
                            = HistoricoAnotacionTraductorHTML.traducirDetallesHTML(traductorAplicacionBean, mov);

                    registroForm.setDetallesMovimientoHTML(detalles);

                } else if ("cargaDatosBusqueda".equals(opcion)) {
                    String strAnoCarga = request.getParameter("anoCarga");
                    int anoCarga = Integer.parseInt(strAnoCarga);
                    String strNumeroCarga = request.getParameter("numeroCarga");
                    Long numeroCarga = Long.parseLong(strNumeroCarga);
                    String libroRegistro = request.getParameter("libroRegistro");

                    m_Log.debug("BUSCAMOS INFORMACION DE LA ANOTACION: " + libroRegistro + "/" + anoCarga + "/" + numeroCarga);
                    RegistroValueObject regCargaBusqueda = new RegistroValueObject();
                    regCargaBusqueda.setAnoReg(anoCarga);
                    regCargaBusqueda.setNumReg(numeroCarga);
                    regCargaBusqueda.setTipoReg(libroRegistro);
                    elRegistroESVO = registroForm.getRegistro();
                    regCargaBusqueda.setIdentDepart(elRegistroESVO.getIdentDepart());
                    regCargaBusqueda.setUnidadOrgan(elRegistroESVO.getUnidadOrgan());
                    regCargaBusqueda = AnotacionRegistroManager.getInstance().getByPrimaryKey(regCargaBusqueda, params);

                    if (regCargaBusqueda.getFallo().equals("fallo")) {
                        request.setAttribute("errorCargaDatos", "0");
                        m_Log.debug("ERROR AL CARGAR LA ANOTACIï¿½?N. NO EXISTE");
                        return mapping.findForward(opcion);
                    }

                    if (regCargaBusqueda.getEstAnotacion() == 9) {
                        request.setAttribute("errorCargaDatos", "1");
                        m_Log.debug("ERROR AL CARGAR LA ANOTACION. ESTA ANULADA");
                        return mapping.findForward(opcion);
                    }

                    if (regCargaBusqueda.getContador() > 0) {
                        request.setAttribute("errorCargaDatos", "2");
                        m_Log.debug("ERROR AL CARGAR LA ANOTACION. ES UNA RESERVA");
                        return mapping.findForward(opcion);
                    }

                    // Creo la relaciÃ³n con la anotacion buscada.
                    regCargaBusqueda.getRelaciones().clear();
                    SimpleRegistroValueObject simpleRegRel = new SimpleRegistroValueObject();
                    simpleRegRel.setDep(Integer.toString(elRegistroESVO.getIdentDepart()));
                    simpleRegRel.setEjercicio(strAnoCarga);
                    simpleRegRel.setNumero(strNumeroCarga);
                    simpleRegRel.setTipo(libroRegistro);
                    simpleRegRel.setUor(Integer.toString(elRegistroESVO.getUnidadOrgan()));
                    regCargaBusqueda.getRelaciones().add(simpleRegRel);

                    // Recuperamos los terceros asociados a la anotaciÃ³n.
                    BusquedaTercerosForm btForm = new BusquedaTercerosForm();
                    btForm.setListaInteresados(getListaInteresados(regCargaBusqueda, params));
                    Vector tercero = getTerceroPrincipal(elRegistroESVO, params);
                    btForm.setListaTerceros(tercero);

                    request.setAttribute("regCargaBusqueda", regCargaBusqueda);
                    request.setAttribute("tercCargaBusqueda", btForm);
                    return mapping.findForward(opcion);

                } else if ("comprobarAsociacionMultiple".equalsIgnoreCase(opcion)) {
                    //Recuperamos del fichero de propiedades de registro la propiedad que indica si se permite la la asociaciï¿½n o el inicio de multiples
                    //expedientes con una misma entrada en el registro
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("Recuperamos la propiedad que indica si se pueden asociar o iniciar mas de un expediente con"
                                + "la misma entrada de registro ");
                    }
                    Boolean opcionPermanencia = true;
                    usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                    try {
                        String registroOrigen = (String) request.getParameter("origenRegistro");

                        String origenesPermitidos = null;
                        try {
                            origenesPermitidos = registroConf.getString(usuarioVO.getOrgCod() + ConstantesDatos.SERVICIO_VALIDO_PERMANENCIA_BUZON_ENTRADA);
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("origenesPermitidos = " + origenesPermitidos);
                            }
                        } catch (Exception ex) {
                            m_Log.error("Se ha producido un error recuperando los origenes de registro que permiten mantener la anotaciï¿½n en el buzï¿½n", ex);
                            opcionPermanencia = false;
                        }//try-catch

                        //Recuperamos el valor que indica si se permite la permanencia de la anotaciï¿½n en el buzï¿½n
                        String permanencia = null;
                        try {
                            permanencia = registroConf.getString(usuarioVO.getOrgCod() + ConstantesDatos.PERMANENCIA_ANOTACION_BUZON_ENTRADA);
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("permanencia = " + permanencia);
                            }
                        } catch (Exception ex) {
                            m_Log.error("Se ha producido un error recuperando el valor que indica si se que permite mantener la anotaciï¿½n en el buzï¿½n", ex);
                            opcionPermanencia = false;
                        }//try-catch

                        if (opcionPermanencia) {
                            if (permanencia.equalsIgnoreCase("SI")) {
                                String numExpediente = (String) request.getParameter("numExpediente");
                                if (m_Log.isDebugEnabled()) {
                                    m_Log.debug("Recuperamos el nï¿½mero de expediente seleccionado = " + numExpediente);
                                }
                                if (numExpediente != null && !"".equalsIgnoreCase(numExpediente)) {
                                    String[] datosNumExpediente = numExpediente.split("/");
                                    String ano = datosNumExpediente[0];
                                    String codProc = datosNumExpediente[1];
                                    String exp = datosNumExpediente[2];

                                    if (m_Log.isDebugEnabled()) {
                                        m_Log.debug("Comprobamos que el origen de la anotaciï¿½n este incluido en los servicios vï¿½lidos");
                                    }
                                    String[] origenes = origenesPermitidos.split(";");
                                    Boolean origenValido = false;
                                    for (int x = 0; x < origenes.length; x++) {
                                        if (registroOrigen.equalsIgnoreCase(origenes[x])) {
                                            if (m_Log.isDebugEnabled()) {
                                                m_Log.debug("El origen de del registro es vï¿½lido");
                                            }
                                            origenValido = true;
                                        }//if(registroOrigen.equalsIgnoreCase(origenes[x]))
                                    }//for(int x=0; x<origenes.length; x++)

                                    if (!origenValido) {
                                        if (m_Log.isDebugEnabled()) {
                                            m_Log.debug("El origen de la anotaciï¿½n no es vï¿½lido para mantener la anotaciï¿½n en el buzï¿½n -> " + registroOrigen);
                                        }
                                        opcionPermanencia = false;
                                    }//if(!origenValido)

                                    if (opcionPermanencia) {
                                        if (m_Log.isDebugEnabled()) {
                                            m_Log.debug("Expediente seleccionado = " + datosNumExpediente);
                                        }
                                        if (m_Log.isDebugEnabled()) {
                                            m_Log.debug("cï¿½digo de procedimiento seleccionado = " + codProc);
                                        }
                                        if (m_Log.isDebugEnabled()) {
                                            m_Log.debug("Recuperamos los expedientes para los que no se puede "
                                                    + "mantener la anotaciï¿½n en el buzï¿½n");
                                        }
                                        try {
                                            String codProcsRestringidos = registroConf.getString(usuarioVO.getOrgCod()
                                                    + ConstantesDatos.PROCEDIMIENTOS_RESTRINGIDOS_PERMANENCIA_ANOTACION);
                                            if (m_Log.isDebugEnabled()) {
                                                m_Log.debug("Cï¿½digos procedimientos restringidos = " + codProcsRestringidos);
                                            }
                                            String[] procsRestringidos = codProcsRestringidos.split(";");
                                            for (int i = 0; i < procsRestringidos.length; i++) {
                                                if (codProc.equalsIgnoreCase(procsRestringidos[i])) {
                                                    if (m_Log.isDebugEnabled()) {
                                                        m_Log.debug("El procedimiento seleccionado es restringido");
                                                    }
                                                    opcionPermanencia = false;
                                                }//if(codProc.equalsIgnoreCase(procsRestringidos[i]))
                                            }//for(int i=0; i<procsRestringidos.length; i++)
                                        } catch (Exception ex) {
                                            opcionPermanencia = false;
                                            m_Log.error("Se ha producido un error recuperando los procedimientos restringidos "
                                                    + "que no pueden mantener la anotaciï¿½n en el buzï¿½n", ex);
                                        }//try-catch
                                    }//if(opcionPermanencia)
                                } else {
                                    opcionPermanencia = false;
                                }//if(numExpediente != null && !"".equalsIgnoreCase(numExpediente))
                            } else {
                                opcionPermanencia = false;
                            }//if(permanencia.equalsIgnoreCase("SI"))
                        }//if(opcionPermanencia)

                    } catch (Exception ex) {
                        opcionPermanencia = false;
                        m_Log.error("Se ha producido un error recuperando la propiedad que indica si las anotaciones pueden permanecer en el buzï¿½n", ex);
                    }//try-catch
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("opcion = comprobarAsociacionMultiple : END");
                    }
                    StringBuffer xmlSalida = new StringBuffer();
                    xmlSalida.append("<RESPUESTA>");
                    xmlSalida.append("<OPCION_PERMANENCIA>");
                    xmlSalida.append(opcionPermanencia);
                    xmlSalida.append("</OPCION_PERMANENCIA>");
                    xmlSalida.append("</RESPUESTA>");
                    try {
                        response.setContentType("text/xml");
                        response.setCharacterEncoding("UTF-8");
                        PrintWriter out = response.getWriter();
                        out.print(xmlSalida.toString());
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        m_Log.error("Se ha producido un error generando el XML para la respuesta " + e.getMessage());
                    }//try-catch
                    return null;
                } else if ("enviarRegistro".equals(opcion)) {
                    return enviarRegistro(mapping, request, response, Integer.toString(cod_uni), tipoES, params);
                } else if ("buscarEntradasRechazadas".equals(opcion)) {
                    String fechaDesde = request.getParameter("fechaDesde");
                    String fechaHasta = request.getParameter("fechaHasta");
                    String codAsunto = request.getParameter("codAsunto");
                    String ejercicio = request.getParameter("ejercicio");

                    m_Log.debug("-- CRITERIOS BUSQUEDA --");
                    m_Log.debug("mis rechazadas -->" + request.getParameter("misRechazadas"));
                    m_Log.debug("usuario --> " + usuarioVO.getIdUsuario());
                    m_Log.debug("fecha desde --> " + fechaDesde);
                    m_Log.debug("fecha hasta --> " + fechaHasta);
                    m_Log.debug("codigo Asunto--> " + codAsunto);
                    m_Log.debug("departamento de registro---> " + regUsuarioVO.getDepCod());
                    m_Log.debug("uor de registro---> " + regUsuarioVO.getUnidadOrgCod());

                    RegistroValueObject regRechazadaVO = new RegistroValueObject();
                    if (fechaDesde != null && fechaHasta != null && !fechaDesde.equals("") && !fechaHasta.equals("")) {
                        regRechazadaVO.setFechaDesde(fechaDesde);
                        regRechazadaVO.setFechaHasta(fechaHasta);
                    }
                    regRechazadaVO.setCodAsunto(codAsunto);

                    if (request.getParameter("misRechazadas").equals("true")) {
                        regRechazadaVO.setMisRechazadas(true);
                        regRechazadaVO.setUsuarioLogueado(usuarioVO);
                    } else {
                        regRechazadaVO.setMisRechazadas(false);
                    }
                    // session.setAttribute("registroValueObjectConsulta", regRechazadaVO);
                    regRechazadaVO.setEstAnotacion(2);
                    regRechazadaVO.setIdentDepart(regUsuarioVO.getDepCod());
                    regRechazadaVO.setUnidadOrgan(regUsuarioVO.getUnidadOrgCod());
                    regRechazadaVO.setTipoReg("E");
                    regRechazadaVO.setTipoAnot(-1);
                    regRechazadaVO.setAnoReg(Integer.parseInt(ejercicio));

                    ArrayList listadoEntradasRechazadas = new ArrayList();

                    listadoEntradasRechazadas = AnotacionRegistroManager.getInstance().getListadoEntradasRechazadas(regRechazadaVO, params);

                    RegistroValueObject storedConsulta = new RegistroValueObject();
                    storedConsulta.copy(regRechazadaVO);
                    session.setAttribute("registroValueObjectConsultaRechazadas", storedConsulta);
                    session.setAttribute("tipoAnotacion", "E");

                    // devolvemos los datos como String en formato json
                    retornarJSON(new Gson().toJson(listadoEntradasRechazadas), response);
                    return null;
                } else if (opcion.equals("volver_listado_rechazadas")) {
                    session.removeAttribute("entradasRechazadas");
                    RegistroValueObject storedConsulta = (RegistroValueObject) session.getAttribute("registroValueObjectConsultaRechazadas");

                    request.setAttribute("misRechazadas", storedConsulta.isMisRechazadas());

                    if (storedConsulta.getCodAsunto() != null) {
                        request.setAttribute("codAsunto", storedConsulta.getCodAsunto());
                    }
                    if (storedConsulta.getFechaDesde() != null) {
                        request.setAttribute("fechaDesde", storedConsulta.getFechaDesde());
                    }
                    if (storedConsulta.getFechaHasta() != null) {

                        request.setAttribute("fechaHasta", storedConsulta.getFechaHasta());
                    }
                } else if (opcion.equals("entradasPendientesFinalizar")) {
                    m_Log.debug("-- CRITERIOS BUSQUEDA --");
                    m_Log.debug("usuario --> " + usuarioVO.getIdUsuario());
                    String filtro = request.getParameter("filtro");
                    RegistroValueObject pendientesFinalizarVO = new RegistroValueObject();
                    pendientesFinalizarVO.setUsuarioLogueado(usuarioVO);
                    ArrayList listadoPendientesFinalizar = new ArrayList();
                    listadoPendientesFinalizar = AnotacionRegistroManager.getInstance().getListadoPendientesFinalizar(pendientesFinalizarVO, filtro, params);

                    // devolvemos los datos como String en formato json
                    retornarJSON(new Gson().toJson(listadoPendientesFinalizar), response);
                    return null;
                } else if (opcion.equals("comprobarFormatoInteresado")) {
                    retornarJSON(new Gson().toJson(TercerosManager.getInstance().esTerceroFormatoNifValido(request.getParameter("codigoTercero"), params)), response);
                    return null;
                } else if (opcion.equals("volver_listado_pendientes_finalizar")) {
                    session.removeAttribute("entradasPendientes");
                } else if (opcion.equals("recuperarAsuntos")) {
                    m_Log.info(session.getId() + " " + usuarioActualLog + " recuperar asuntos combo");

                    Vector<MantAsuntosValueObject> listadoAsuntos = new Vector();
                    RegistroValueObject regVO = new RegistroValueObject();
                    regVO.setTipoReg("E");

                    regVO.setUnidadOrgan(regUsuarioVO.getUnidadOrgCod());

                    listadoAsuntos = MantAsuntosDAO.getInstance().buscarAsuntos(regVO, false, params);
                    // devolvemos los datos como String en formato json
                    retornarJSON(new Gson().toJson(listadoAsuntos), response);

                    return null;
                } else {
                    m_Log.info(session.getId() + " " + usuarioActualLog + " " + opcion + "  MantAnotacionRegistroAction, perform --> opcion: no valida SIN IMPLEMENTAR");
                    opcion = "error";
                } // Fin if opcion: cerrar_salida
            }
            /* Asignamos el AperturaCierreRegistro al formulario*/
            registroForm.setRegistro(elRegistroESVO);
            m_Log.info(session.getId() + " " + usuarioActualLog + "  ==================  MANTANOTACIONREGISTROACTION POSICION ANOTACION " + registroForm.getRegistro().getPosicionAnotacion() + " =============");

        } else { // No hay usuario.
            m_Log.info(session.getId() + " " + usuarioActualLog + " MantAnotacionRegistroAction --> no hay usuario");
            opcion = "no_usuario";
        }

        m_Log.info(session.getId() + " " + usuarioActualLog + " Opcion antes de dejar MantAnotacionRegistroAction: " + opcion);
        m_Log.info(session.getId() + " " + usuarioActualLog + " <================= MantAnotacionRegistroAction ======================");

        /* Redirigimos al JSP de salida */
        return (mapping.findForward(opcion));
    }

    /**
     * Obtiene la lista de roles correspondientes al cÃ³digo y municipio de
     * procedimiento que se pasan en el GeneralValueObject. Los roles del
     * procedimiento traen de "valor por defecto" 1 y 0 que se traducen a SI y
     * NO.
     *
     * @returns Un Vector de GeneralValueObject con los roles del procedimiento.
     */
    private Vector<GeneralValueObject> obtenerRolesProcedimiento(GeneralValueObject procedimiento, String[] params) {

        Vector<GeneralValueObject> rolesProc = InteresadosManager.getInstance().getListaRoles(procedimiento, params);
        for (GeneralValueObject rolProc : rolesProc) {
            rolProc.setAtributo("porDefecto", rolProc.getAtributo("porDefecto").equals("1") ? "SI" : "NO");
        }
        return rolesProc;
    }

    // Obtener la lista de temas seleccionados de un input cuyo value es una variable
    // declarada como un array javascript.
    // RegistroValueObject tiene un vector que contiene la lista de cÃ³digos de temas.
    private Vector listaTemasSeleccionados(String listTemasSelecc) {
        Vector lista = new Vector();
        StringTokenizer codigos = null;

        if (listTemasSelecc != null) {
            codigos = new StringTokenizer(listTemasSelecc, "§¥,", false);

            while (codigos.hasMoreTokens()) {
                String cod = codigos.nextToken();
                lista.addElement(cod);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("-->" + cod);
                }
            }

        }
        return lista;
    }

    /*
    Precondiciones: El numero de elementos es par.
     */
    private Hashtable listaCriterios(String listCriterios) {
        Hashtable lista = new Hashtable();
        StringTokenizer elementos = null;

        if (listCriterios != null) {
            elementos = new StringTokenizer(listCriterios, "§¥,", false);

            while (elementos.hasMoreTokens()) {
                String clave = elementos.nextToken();
                String criterio = elementos.nextToken(); //Precondicion.
                lista.put(clave, criterio);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MantAnotacionRegistroAction: listaCriterios --> (" + clave + "," + criterio + ")");
                }
            }

        }
        return lista;
    }

    /**
     * Mï¿½todo que inicializa las listas del formulario: tipo de documentos, tipo
     * de remitentes, tipo de transportes, tipo de actuaciones, tipo de temas,
     *
     * @param elRegistroESVO:        RegistroValueObject
     * @param recuperarTodosAsuntos: si estï¿½ a true , al recuperar los asuntos
     *                               de registro, se recuperan todos los asuntos, incluidos los que han sido
     *                               dados de baja.Si estï¿½ a false, sï¿½lo se recuperan los que no han sido
     *                               eliminados
     * @param params:                Parï¿½metros de conexiï¿½n a la BBDD
     */
    private void inicializarListas(RegistroValueObject elRegistroESVO,
                                   boolean recuperarTodosAsuntos, String[] params)
            throws AnotacionRegistroException, TramitacionException {

        //Recuperamos la lista de procedimientos.
        String uor = elRegistroESVO.getIdUndTramitad();
        if (uor == null || "".equals(uor)) {
            elRegistroESVO.setListaProcedimientos(TramitacionManager.getInstance().getListaProcedimientosVigentes(params));
        } else {
            elRegistroESVO.setListaProcedimientos(TramitacionManager.getInstance().getListaProcedimientosUOR(uor, params));
        }
        elRegistroESVO.setListaTiposDocumentos(AnotacionRegistroManager.getInstance().getListaTiposDocumentos(params));
        elRegistroESVO.setListaTiposDocumentosAlta(AnotacionRegistroManager.getInstance().getListaTiposDocumentosAlta(params));
        elRegistroESVO.setListaTiposRemitentes(AnotacionRegistroManager.getInstance().getListaTiposRemitentes(params));
        elRegistroESVO.setListaTiposTransportes(AnotacionRegistroManager.getInstance().getListaTiposTransportes(params));
        elRegistroESVO.setListaTemas(AnotacionRegistroManager.getInstance().getListaTemas(params));
        //Para mostrar los formularios asociados
        elRegistroESVO.setListaFormulariosAsociados(cargarFormularios(elRegistroESVO));
        elRegistroESVO.setListaFormulariosAnexos(cargarAnexosFormularios(elRegistroESVO));
        elRegistroESVO.setListaTiposIdInteresado(AnotacionRegistroManager.getInstance().getListaTiposIdInteresado(params));
        elRegistroESVO.setListaDepartamentos(AnotacionRegistroManager.getInstance().getListaDepartamentos(params));
        elRegistroESVO.setListaActuaciones(AnotacionRegistroManager.getInstance().getListaActuaciones(params));
        elRegistroESVO.setListaOrganizacionesExternas(MantRegistroExternoManager.getInstance().loadOrganizacionesExternas(params));
        elRegistroESVO.setListaAsuntos(MantAsuntosManager.getInstance().buscarAsuntos(elRegistroESVO, recuperarTodosAsuntos, params));
        //Ahora vamos a tener tambien el arbol de clasificaciones de asuntos
        elRegistroESVO.setArbolClasifAsuntos(MantClasifAsuntosManager.getInstance().getArbolClasifAsuntos(elRegistroESVO, recuperarTodosAsuntos, params));

    }

    private Collection cargarFormularios(RegistroValueObject elRegistroESVO) {
        Collection rdo = new ArrayList();
        try {
            Config m_Config = ConfigServiceHelper.getConfig("common");
            if ("si".equals(m_Config.getString("JSP.Formularios.Registro"))) {
                //llamar a la fachada de formularios
                FormularioFacade facade = new FormularioFacade(String.valueOf(elRegistroESVO.getIdOrganizacion()));
                m_Log.debug(" En cargarFormularios, antes de getFormsOfRegistro . ");
                m_Log.debug("--> Tipo de Registro : " + elRegistroESVO.getTipoReg());
                m_Log.debug("--> Ano de Registro: " + elRegistroESVO.getAnoReg());
                m_Log.debug("--> Numero de Registro : " + String.valueOf(elRegistroESVO.getNumReg()));
                m_Log.debug("--> Codigo unidad de Registro : " + elRegistroESVO.getUnidadOrgan());
                m_Log.debug("--> Codigo codigoDepartamento : " + elRegistroESVO.getIdOrganizacion());
                //Datos que se le pasan
                String tipoReg = elRegistroESVO.getTipoReg();
                int anoEjercicio = elRegistroESVO.getAnoReg();
                Long numReg = elRegistroESVO.getNumReg();
                int unidadReg = elRegistroESVO.getUnidadOrgan();
                int codDep = elRegistroESVO.getIdEntidad();
                rdo = facade.getFormsOfRegistro(tipoReg, anoEjercicio, numReg.intValue(), unidadReg, codDep);
                m_Log.debug("-->Vuelta de getFormsOfRegistro");
                m_Log.debug("-->Tamano de collection :" + rdo.size());

                //Comprobamos que se han conseguido los datos correctos
                Iterator it = rdo.iterator();
                while (it.hasNext()) {
                    FormularioTramitadoVO formVO = (FormularioTramitadoVO) it.next();
                    m_Log.debug("--> Tipo de Registro : " + formVO.getEntrada().getTipo());
                    m_Log.debug("--> Ano de Registro: " + formVO.getEntrada().getEjercicio());
                    m_Log.debug("--> Numero de Registro : " + formVO.getEntrada().getNum());
                    m_Log.debug("--> Codigo unidad de Registro : " + formVO.getEntrada().getUnidadOrganizativa());
                    m_Log.debug("--> Codigo codigoDepartamento : " + formVO.getEntrada().getDepartamento());
                }
            }
        } catch (InternalErrorException e) {

            m_Log.debug("***** Fallo al cargar Formularios " + e.getMessage());
        }
        m_Log.info("--> Fin cargarFormularios");
        return rdo;
    }

    private Vector cargarAnexosFormularios(RegistroValueObject elRegistroESVO) {
        Collection rdo = new ArrayList();
        Vector sal = new Vector();

        try {
            Config m_Config = ConfigServiceHelper.getConfig("common");
            if ("si".equals(m_Config.getString("JSP.Formularios.Registro"))) {
                //llamar a la fachada de formularios
                Collection coleccion = elRegistroESVO.getListaFormulariosAsociados();
                Iterator iter = coleccion.iterator();

                if (coleccion != null) {
                    while (iter.hasNext()) {
                        FormularioTramitadoVO formTramVO = (FormularioTramitadoVO) iter.next();
                        GeneralValueObject genVO = new GeneralValueObject();
                        String codigoInforme = (String) formTramVO.getCodigo();
                        FormularioFacade facade = new FormularioFacade(
                                String.valueOf(elRegistroESVO.getIdOrganizacion()));

                        rdo = facade.getFicherosOfForm(codigoInforme);
                        if (rdo.size() > 0) {
                            genVO.setAtributo("codigoInforme", codigoInforme);
                            genVO.setAtributo("tieneAnexo", "si");

                        } else {
                            genVO.setAtributo("codigoInforme", codigoInforme);
                            genVO.setAtributo("tieneAnexo", "no");
                        }
                        sal.add(genVO);
                    }
                }

            }
        } catch (InternalErrorException e) {

            m_Log.debug("***** Fallo al cargar Formularios " + e.getMessage());
        }

        m_Log.info("--> Fin cargarAnexosFormularios");
        return sal;
    }

    private void inicializarListasVacias(RegistroValueObject elRegistroESVO) {
        elRegistroESVO.setListaProcedimientos(new Vector());
        elRegistroESVO.setListaTiposDocumentos(new Vector());
        elRegistroESVO.setListaTiposDocumentosAlta(new Vector());
        elRegistroESVO.setListaTiposRemitentes(new Vector());
        elRegistroESVO.setListaTiposTransportes(new Vector());
        elRegistroESVO.setListaActuaciones(new Vector());
        elRegistroESVO.setListaTiposIdInteresado(new Vector());
        elRegistroESVO.setListaDepartamentos(new Vector());
        elRegistroESVO.setListaTemas(new Vector());

    }

    /* FunciÃ³n: ImprimirCuneus*/
    private String imprimirCuneus(RegistroValueObject elRegistroESVO, UsuarioValueObject usuVO, String posicionSello,
                                  String idiomaSello, String nCopiasSello, String sUrl) {

        GeneralValueObject gVO = new GeneralValueObject();
        Vector<File> ficheros = new Vector<File>();
        String plantilla = "cuneusRegistro";
        String sObservaciones = elRegistroESVO.getObservaciones();
        sObservaciones = AdaptadorSQLBD.js_unescape(sObservaciones);
        gVO.setAtributo("baseDir", m_Conf.getString("PDF.base_dir"));
        gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
        gVO.setAtributo("usuDir", usuVO.getDtr());
        gVO.setAtributo("pdfFile", "registro");
        String estilo = "css/informeCuneus.css";
        gVO.setAtributo("estilo", estilo);
        GeneralPDF pdf = new GeneralPDF(usuVO.getParamsCon(), gVO);

        // Obtenemos el codigo visible de la UOR a partir de su codigo interno.
        Vector unidades = UORsManager.getInstance().getListaUORsPorCodigo(usuVO.getUnidadOrgCod(), usuVO.getParamsCon());
        UORDTO unidad = (UORDTO) unidades.get(0);
        long numReg = elRegistroESVO.getNumReg();

        String textoXML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><CUNEUS>";
        String contenidoXML = "<COPIA>"
                + "<POSICION_CUNEUS>" + posicionSello + "</POSICION_CUNEUS>"
                + "<REGISTRO>" + usuVO.getUnidadOrg() + "</REGISTRO>"
                + "<NOMBREOFICINA>" + elRegistroESVO.getNombreOficinaRegistro() + "</NOMBREOFICINA>"
                + "<CODREGISTRO>" + unidad.getUor_cod_vis() + "</CODREGISTRO>"
                + "<TIPO_REGISTRO>" + elRegistroESVO.getTipoReg() + "</TIPO_REGISTRO>"
                + "<AYTO>" + usuVO.getEnt() + "</AYTO><EJERCICIO>" + elRegistroESVO.getAnoReg() + "</EJERCICIO>"
                + "<NUMERO>" + numReg + "</NUMERO>"
                + "<FECHA>" + elRegistroESVO.getFecEntrada() + "</FECHA>"
                + "<IDIOMA>" + idiomaSello + "</IDIOMA>"
                + "<ESCUDO>" + sUrl + usuVO.getOrgIco() + "</ESCUDO>"
                + "<OBSERVACIONES>" + (sObservaciones != null ? sObservaciones.trim() : "") + "</OBSERVACIONES>"
                + "</COPIA>";
        String finXML = "</CUNEUS>";

        for (int i = 0; i < Integer.parseInt(nCopiasSello); i++) {
            textoXML += contenidoXML;
            if (i != Integer.parseInt(nCopiasSello) - 1) {
                textoXML += pdf.construyeTablaVacia("saltoPagina");
            }
        }
        textoXML += finXML;
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("MantAnotacionRegistroAction. imprimirCuneus. " + textoXML);
        }
        File f = pdf.transformaXML(textoXML, plantilla);

        String nombreFichero = null;
        if (f != null) {
            ficheros.add(f);
            nombreFichero = pdf.getPdf(ficheros);
        }
        return nombreFichero;
    }

    /**
     * EnvÃ­a correos notificando el alta del asiento a las unidades orgÃ¡nicas
     * que se hayan especificado.
     *
     * @param registroForm El form que contiene la informacion del asiento y
     *                     lista de uors.
     */
    private void notificarUnidadesOrganicas(MantAnotacionRegistroForm registroForm) {

        // En Lanbide existe una unica unidad de registro por lo que el email se enviara siempre desde la misma direccion (definida en techserver.propertie)
        // En dicho cliente el envï¿½o es independiente de que la unidad de registro tenga o no configurado un mail.

        Vector<String> codigosUorsNotificar = registroForm.getListaUorsCorreo();
        Vector<UORDTO> listaUors = registroForm.getListaNuevasUORs();

        if (registroForm.isEnviarCorreo() && codigosUorsNotificar.size() > 0) {
            // Datos para construir el mensaje
            // Tipo de registro
            String tipoRegistro;
            if (registroForm.getRegistro().getTipoReg().equals("E")) {
                tipoRegistro = "entrada";
            } else {
                tipoRegistro = "salida";
            }
            String fecha = registroForm.getFechaAnotacion(); // Fecha
            String hora = registroForm.getHoraMinAnotacion(); // Hora
            String numero = registroForm.getEjercicioAnotacion() + "/" + registroForm.getNumeroAnotacion(); // Numero
            m_Log.debug("Se notifica acerca de la anotacion " + numero + " del registro de "
                    + tipoRegistro + " de fecha " + fecha + "-" + hora);

            // Construimos el asunto y cuerpo del mensaje
            Config m_Conf_Registro = ConfigServiceHelper.getConfig("Registro");
            String asunto = m_Conf_Registro.getString("mail.altaRegistro.subject");
            asunto = asunto.replaceAll("@tiporegistro@", tipoRegistro);
            asunto = asunto.replaceAll("@numanotacion@", numero);
            String mensaje = m_Conf_Registro.getString("mail.altaRegistro.content");
            mensaje = mensaje.replaceAll("@numanotacion@", numero);
            mensaje = mensaje.replaceAll("@fecha@", fecha);
            mensaje = mensaje.replaceAll("@hora@", hora);

            m_Log.debug("Asunto del mensaje: " + asunto);
            m_Log.debug("Mensaje de notificacion: " + mensaje);

            // Pasamos a enviar correo a cada UOR
            MailHelper mailer = new MailHelper();
            UORDTO uorDestino = null;
            String correoDestino;
            for (String cod : codigosUorsNotificar) {
                uorDestino = buscarUor(cod, listaUors);
                if (uorDestino != null) {
                    correoDestino = uorDestino.getUor_email();
                    m_Log.debug("- " + uorDestino.getUor_nom() + ", correo: " + correoDestino);

                    // Enviamos el correo
                    if (correoDestino != null && !correoDestino.equals("")) {
                        try {
                            mailer.sendMail(correoDestino, asunto, mensaje);
                        } catch (Exception e) {
                            m_Log.error("Error al enviar correo:");
                            e.printStackTrace();
                        }
                    }
                } else {
                    m_Log.debug("- UOR " + cod + " NO ENCONTRADA");
                }
            }
        } else {
            m_Log.debug("No se ha indicado ninguna UOR para ser notificada.");
        }
    }

    /**
     * Busca en la lista la uor del codigo indicado.
     *
     * @param codigo Codigo de la uor que se quiere encontrar.
     * @param uors   Lista de todas las uors en la que buscar.
     * @return La uor correspondiente al codigo pasado, null si no se encuentra.
     */
    private UORDTO buscarUor(String codigo, Vector<UORDTO> uors) {
        for (UORDTO uor : uors) {
            if (uor.getUor_cod().equals(codigo)) {
                return uor;
            }
        }
        return null;
    }

    /**
     * Obtiene la lista de roles correspondientes al cÃ³digo de procedimiento y
     * cÃ³digo de municipio pasados. Si el codigo de procedimiento es nulo o
     * vacÃ­o devuelve los roles por defecto.
     */
    private Vector<GeneralValueObject> obtenerRoles(String proc, String mun, String[] params) {

        Vector<GeneralValueObject> roles = new Vector<GeneralValueObject>();
        if (proc != null && !proc.equals("") && !proc.equals("null")) {
            GeneralValueObject procGVO = new GeneralValueObject();
            procGVO.setAtributo("codMunicipio", mun);
            procGVO.setAtributo("codProcedimiento", proc);
            roles = obtenerRolesProcedimiento(procGVO, params);
        } else {
            roles = InteresadosManager.getInstance().getListaRolesRegistro(params);
        }

        m_Log.debug("ROLES RECUPERADOS:");
        for (GeneralValueObject rol : roles) {
            m_Log.debug(rol.getAtributo("codRol") + "-" + rol.getAtributo("descRol") + "-" + rol.getAtributo("porDefecto"));
        }

        return roles;
    }

    /**
     * Crea un mapeo de los roles anteriores a los nuevos, intentando casar los
     * codigos de los roles anteriores con los nuevos cuando tienen el mismo
     * nombre. Si no hay ningun rol nuevo con el mismo nombre que uno antiguo,
     * se mapea al rol por defecto de los roles nuevos.
     * <p>
     * Los resultados del mapeo se guardan en tres vectores en el form, que
     * contienen: codigos antiguos, codigos nuevos y descripciones nuevas.
     *
     * @param rolesAnteriores Roles existentes.
     * @param rolesNuevos     Roles nuevos a los que se mapea.
     * @param form            Form del registro.
     */
    public void mapearRoles(Vector<GeneralValueObject> rolesAnteriores,
                            Vector<GeneralValueObject> rolesNuevos,
                            MantAnotacionRegistroForm form) {

        // Obtenemos el codigo y descripcion por defecto de los nuevos roles
        String codRolDefecto = "";
        String descRolDefecto = "";
        for (GeneralValueObject rol : rolesNuevos) {
            if (rol.getAtributo("porDefecto").equals("SI")) {
                codRolDefecto = (String) rol.getAtributo("codRol");
                descRolDefecto = (String) rol.getAtributo("descRol");
                break;
            }
        }

        // Hacemos el mapeo
        Vector<String> codAnteriores = new Vector<String>();
        Vector<String> codNuevos = new Vector<String>();
        Vector<String> descNuevas = new Vector<String>();

        for (GeneralValueObject rolAnterior : rolesAnteriores) {
            String descAnterior = (String) rolAnterior.getAtributo("descRol");
            codAnteriores.add((String) rolAnterior.getAtributo("codRol"));

            // Buscamos un rol nuevo del mismo nombre
            boolean encontrado = false;
            for (GeneralValueObject rolNuevo : rolesNuevos) {
                String descNuevo = (String) rolNuevo.getAtributo("descRol");
                if (descNuevo.equals(descAnterior)) {
                    codNuevos.add((String) rolNuevo.getAtributo("codRol"));
                    descNuevas.add(descNuevo);
                    encontrado = true;
                    break;
                }
            }

            // Si no se ha encontrado un rol del mismo nombre, mapeo al rol por defecto
            if (!encontrado) {
                codNuevos.add(codRolDefecto);
                descNuevas.add(descRolDefecto);
            }
        }

        // Asignamos el mapeo al form
        form.setCodRolesAnteriores(codAnteriores);
        form.setCodRolesNuevos(codNuevos);
        form.setDescRolesNuevos(descNuevas);

        m_Log.debug("MAPEO DE ROLES : ");
        for (int i = 0; i < codAnteriores.size(); i++) {
            m_Log.debug(codAnteriores.elementAt(i) + " => " + codNuevos.elementAt(i) + ", " + descNuevas.elementAt(i));
        }
    }

    /**
     * Recupera la lista de documentos del procedimiento indicado.
     *
     * @param codProc Codigo del procedimiento.
     * @param munProc Codigo del municipio del procedimiento.
     * @param params  Parametros de conexion a BD.
     * @return Vector de ElementoListaValueObject con los documentos.
     */
    private Vector<ElementoListaValueObject> obtenerDocsProcedimiento(
            String codProc, String munProc, String[] params) {

        Vector<ElementoListaValueObject> docs
                = new Vector<ElementoListaValueObject>();

        if (codProc != null && !codProc.equals("") && !codProc.equals("null")) {
            GeneralValueObject procGVO = new GeneralValueObject();
            procGVO.setAtributo("codMunicipio", munProc);
            procGVO.setAtributo("codProcedimiento", codProc);
            docs = DefinicionProcedimientosManager.getInstance().getListaDocumentos(procGVO, params);
        }

        m_Log.debug("Recuperados " + docs.size() + " documentos");
        return docs;
    }

    /**
     * Devuelve la lista de interesados del expediente en formato de Vector de
     * GeneralValueObject y asigna los datos al RegistroValueObject y a al form.
     */
    private Vector<GeneralValueObject> obtenerInteresadosProcedimiento(
            GeneralValueObject gVOExpediente, RegistroValueObject regVO, MantAnotacionRegistroForm registroForm, String[] params) {

        // Borramos los datos del tercero principal que puedan existir
        registroForm.setTxtDNI("");
        registroForm.setTxtInteresado("");
        registroForm.setTxtDomicilio("");
        registroForm.setTxtProv("");
        registroForm.setTxtMuni("");
        registroForm.setTxtCP("");
        registroForm.setTxtTelefono("");
        registroForm.setTxtCorreo("");

        Vector<InteresadoExpedienteVO> listaInteresadosExp
                = InteresadosManager.getInstance().getListaInteresados(gVOExpediente, params);
        Vector<GeneralValueObject> listaInteresados = new Vector<GeneralValueObject>();

        // Traducir a GeneralValueObject la lista
        for (InteresadoExpedienteVO expVO : listaInteresadosExp) {
            GeneralValueObject genVO = new GeneralValueObject();

            genVO.setAtributo("codigoTercero", Integer.toString(expVO.getCodTercero()));
            genVO.setAtributo("versionTercero", Integer.toString(expVO.getNumVersion()));
            genVO.setAtributo("titular", expVO.getNombreCompleto());
            genVO.setAtributo("rol", Integer.toString(expVO.getCodigoRol()));
            genVO.setAtributo("descRol", expVO.getDescRol());
            // Para los procedimientos, el valor de porDefecto es
            // boolean, hay que traducirlo a 'SI' o 'NO'
            if (expVO.isPorDefecto()) {
                genVO.setAtributo("porDefecto", "SI");
            } else {
                genVO.setAtributo("porDefecto", "NO");
            }
            genVO.setAtributo("domicilio", Integer.toString(expVO.getCodDomicilio()));

            genVO.setAtributo("telefono", expVO.getTelf());
            genVO.setAtributo("email", expVO.getEmail());
            genVO.setAtributo("tip", expVO.getTipoDoc());
            genVO.setAtributo("doc", expVO.getTxtDoc());
            genVO.setAtributo("cp", expVO.getCp());
            genVO.setAtributo("pais", expVO.getPais());
            genVO.setAtributo("provincia", expVO.getProvincia());
            genVO.setAtributo("municipio", expVO.getMunicipio());
            genVO.setAtributo("descDomicilio", expVO.getDomicilio());

            // Interesado principal
            if (expVO.isMostrar()) {
                regVO.setDomicInter(expVO.getCodDomicilio());
                regVO.setCodInter(expVO.getCodTercero());
                regVO.setNumModInfInt(expVO.getNumVersion());
                regVO.setTipoDocInteresado(expVO.getTipoDoc());
                // Rellenar los datos del form
                registroForm.setTxtDNI(expVO.getTxtDoc());
                registroForm.setTxtInteresado(expVO.getNombreCompleto());
                registroForm.setTxtDomicilio(expVO.getDomicilio());
                registroForm.setTxtProv(expVO.getProvincia());
                registroForm.setTxtMuni(expVO.getMunicipio());
                registroForm.setTxtCP(expVO.getCp());
                registroForm.setTxtTelefono(expVO.getTelf());
                registroForm.setTxtCorreo(expVO.getEmail());
            }

            listaInteresados.add(genVO);
        }

        return listaInteresados;
    }

    /**
     * Asigna la lista de documentos al form, conservando los documentos
     * existentes en el form que tengan contenido y aÃ±adiendo solo los
     * documentos nuevos que no tengan un nombre ya existente entre los
     * conservados. Si la lista pasada estÃ¡ vacÃ­a se conservan todos los del
     * form, con contenido o sin el.
     */
    private void asignarDocumentos(Vector<ElementoListaValueObject> listaDocs,
                                   MantAnotacionRegistroForm registroForm) {

        Vector<RegistroValueObject> docsNuevos = new Vector<RegistroValueObject>();

        if (listaDocs.size() >= 0) {
            docsNuevos = new Vector<RegistroValueObject>();
            ArrayList<String> docsExistentes = new ArrayList<String>();

            // Conservamos los documentos que tienen contenido.
            for (RegistroValueObject doc
                    : (Vector<RegistroValueObject>) registroForm.getListaDocsAsignados()) {

                String tipoDoc = doc.getTipoDoc();
                // Si tienen tipo tienen contenido
                if (tipoDoc != null && !tipoDoc.equals("")) {
                    docsNuevos.add(doc);
                    if (listaDocs.size() > 0) {
                        docsExistentes.add(doc.getNombreDoc());
                    }
                }
            }

            if (listaDocs.size() > 0) {
                // Se aÃ±aden los documentos del asunto, pero solo si no
                // existe ya un documento del mismo nombre.
                for (ElementoListaValueObject docAsunto : listaDocs) {
                    m_Log.debug(docAsunto.getDescripcion());
                    //Comprobamos si no existe
                    if (!docsExistentes.contains(docAsunto.getDescripcion())) {
                        RegistroValueObject reg = new RegistroValueObject();
                        reg.setDoc(new byte[0]);
                        reg.setNombreDoc(docAsunto.getDescripcion());
                        reg.setTipoDoc("");
                        reg.setDocNormal(true);
                        reg.setEntregado("N");
                        reg.setCompulsado("NO");
                        docsNuevos.add(reg);
                    }
                }
            }

        }

        // Se asigna la nueva lista de documentos
        registroForm.setListaDocsAsignados(docsNuevos);
    }

    private Vector listaInteresadosSeleccionados(String listSelecc) {
        Vector lista = new Vector();
        StringTokenizer codigos;

        if (listSelecc != null) {
            codigos = new StringTokenizer(listSelecc, "§¥", false);

            while (codigos.hasMoreTokens()) {
                String cod = codigos.nextToken();
                lista.addElement(cod);
            }

        }
        return lista;
    }

    private Vector getListaInteresados(RegistroValueObject datos, String[] params) {
        GeneralValueObject gVO = new GeneralValueObject();

        gVO.setAtributo("ejercicio", String.valueOf(datos.getAnoReg()));
        gVO.setAtributo("numero", String.valueOf(datos.getNumReg()));
        gVO.setAtributo("codOur", String.valueOf(datos.getUnidadOrgan()));
        gVO.setAtributo("codDep", String.valueOf(datos.getIdentDepart()));
        gVO.setAtributo("codTip", String.valueOf(datos.getTipoReg()));
        gVO.setAtributo("codProc", String.valueOf(datos.getCodProcedimientoRoles()));
        gVO.setAtributo("munProc", String.valueOf(datos.getMunProcedimiento()));

        return InteresadosManager.getInstance().getListaInteresadosRegistro(gVO, params);
    }

    private Vector getTerceroPrincipal(RegistroValueObject datos, String[] params) {
        TercerosValueObject elTercero = new TercerosValueObject();
        int domicInter = datos.getDomicInter();
        int codInter = datos.getCodInter();
        int numModInfInt = datos.getNumModInfInt();
        elTercero.setIdDomicilio(String.valueOf(domicInter));
        elTercero.setIdentificador(String.valueOf(codInter));
        elTercero.setVersion(String.valueOf(numModInfInt));

        return TercerosManager.getInstance().getByHistorico(elTercero, params);
    }

    private Boolean getOpcionPermanencia(String origenRegistro, String numExpediente, HttpSession session) {
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("getOpcionPermanencia() : BEGIN");
        }
        //Recuperamos del fichero de propiedades de registro la propiedad que indica si se permite la la asociaciï¿½n o el inicio de multiples
        //expedientes con una misma entrada en el registro
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("Recuperamos la propiedad que indica si se pueden asociar o iniciar mas de un expediente con"
                    + "la misma entrada de registro ");
        }
        Boolean opcionPermanencia = true;
        UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
        try {
            String registroOrigen = origenRegistro;

            String origenesPermitidos = null;
            try {
                origenesPermitidos = registroConf.getString(usuarioVO.getOrgCod() + ConstantesDatos.SERVICIO_VALIDO_PERMANENCIA_BUZON_ENTRADA);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("origenesPermitidos = " + origenesPermitidos);
                }
            } catch (Exception ex) {
                m_Log.error("Se ha producido un error recuperando los origenes de registro que permiten mantener la anotaciï¿½n en el buzï¿½n", ex);
                opcionPermanencia = false;
            }//try-catch

            //Recuperamos el valor que indica si se permite la permanencia de la anotaciï¿½n en el buzï¿½n
            String permanencia = null;
            try {
                permanencia = registroConf.getString(usuarioVO.getOrgCod() + ConstantesDatos.PERMANENCIA_ANOTACION_BUZON_ENTRADA);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("permanencia = " + permanencia);
                }
            } catch (Exception ex) {
                m_Log.error("Se ha producido un error recuperando el valor que indica si se que permite mantener la anotaciï¿½n en el buzï¿½n", ex);
                opcionPermanencia = false;
            }//try-catch
            if (opcionPermanencia) {
                if (permanencia.equalsIgnoreCase("SI")) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("Recuperamos el nï¿½mero de expediente seleccionado = " + numExpediente);
                    }
                    if (numExpediente != null && !"".equalsIgnoreCase(numExpediente)) {
                        String[] datosNumExpediente = numExpediente.split("/");
                        String ano = datosNumExpediente[0];
                        String codProc = datosNumExpediente[1];
                        String exp = datosNumExpediente[2];

                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug("Comprobamos que el origen de la anotaciï¿½n este incluido en los servicios vï¿½lidos");
                        }
                        String[] origenes = origenesPermitidos.split(";");
                        Boolean origenValido = false;
                        for (int x = 0; x < origenes.length; x++) {
                            if (registroOrigen.equalsIgnoreCase(origenes[x])) {
                                if (m_Log.isDebugEnabled()) {
                                    m_Log.debug("El origen de del registro es vï¿½lido");
                                }
                                origenValido = true;
                            }//if(registroOrigen.equalsIgnoreCase(origenes[x]))
                        }//for(int x=0; x<origenes.length; x++)

                        if (!origenValido) {
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("El origen de la anotaciï¿½n no es vï¿½lido para mantener la anotaciï¿½n en el buzï¿½n -> " + registroOrigen);
                            }
                            opcionPermanencia = false;
                        }//if(!origenValido)

                        if (opcionPermanencia) {
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("Expediente seleccionado = " + datosNumExpediente);
                            }
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("cï¿½digo de procedimiento seleccionado = " + codProc);
                            }
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("Recuperamos los expedientes para los que no se puede "
                                        + "mantener la anotaciï¿½n en el buzï¿½n");
                            }
                            try {
                                String codProcsRestringidos = registroConf.getString(usuarioVO.getOrgCod()
                                        + ConstantesDatos.PROCEDIMIENTOS_RESTRINGIDOS_PERMANENCIA_ANOTACION);
                                if (m_Log.isDebugEnabled()) {
                                    m_Log.debug("Cï¿½digos procedimientos restringidos = " + codProcsRestringidos);
                                }
                                String[] procsRestringidos = codProcsRestringidos.split(";");
                                for (int i = 0; i < procsRestringidos.length; i++) {
                                    if (codProc.equalsIgnoreCase(procsRestringidos[i])) {
                                        if (m_Log.isDebugEnabled()) {
                                            m_Log.debug("El procedimiento seleccionado es restringido");
                                        }
                                        opcionPermanencia = false;
                                    }//if(codProc.equalsIgnoreCase(procsRestringidos[i]))
                                }//for(int i=0; i<procsRestringidos.length; i++)
                            } catch (Exception ex) {
                                opcionPermanencia = false;
                                m_Log.error("Se ha producido un error recuperando los procedimientos restringidos "
                                        + "que no pueden mantener la anotaciï¿½n en el buzï¿½n", ex);
                            }//try-catch
                        }//if(opcionPermanencia)
                    } else {
                        opcionPermanencia = false;
                    }//if(numExpediente != null && !"".equalsIgnoreCase(numExpediente))
                } else {
                    opcionPermanencia = false;
                }//if(permanencia.equalsIgnoreCase("SI"))
            }//if(opcionPermanencia)
        } catch (Exception ex) {
            opcionPermanencia = false;
            m_Log.error("Se ha producido un error recuperando la propiedad que indica si las anotaciones pueden permanecer en el buzï¿½n", ex);
        }//try-catch
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("getOpcionPermanencia() : END");
        }
        return opcionPermanencia;
    }//getOpcionPermanencia

    /**
     * Mï¿½todo auxiliar para la funcionalidad de ImprimirCuneus Ahora se necesita
     * imprimir el nombre de oficina
     *
     * @param codigoOficina codigo de la oficina
     * @param params        nombre de la oficina
     */
    private String dameNombreDeOficina(int codigoOficina, String[] params) {
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("dameNombreDeOficina() : BEGIN");
        }
        String nombreOficina = "";
        nombreOficina = AnotacionRegistroManager.getInstance().dameNombreOficina(codigoOficina, params);
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("dameNombreDeOficina() : END");
        }
        return nombreOficina;
    }

    /**
     * Mï¿½todo auxiliar para la funcionalidad de ImprimirCuneus Ahora se necesita
     * imprimir el nombre de oficina, y para ello necesitamos el codigo de
     * oficina
     *
     * @param codUor       codigo de la unidad Organizativa
     * @param numAnotacion numero de la Anotacion de oficina
     */
    private int dameCodigoOficina(int codUor, long numAnotacion, int resEje, String tipoReg, String[] params) {
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("dameCodigoDeOficina() : BEGIN");
        }
        int codigoOficina;
        codigoOficina = AnotacionRegistroManager.getInstance().dameCodigoOficina(codUor, numAnotacion, resEje, tipoReg, params);
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("dameCodigoDeOficina() : END");
        }
        return codigoOficina;
    }

    /**
     * Mï¿½todo llamado para devolver un String en formato JSON al cliente que ha
     * realiza la peticiï¿½n a alguna de las operaciones de este action
     *
     * @param json:     String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a travï¿½s del cual se
     *                  devuelve la salida al cliente que ha realizado la solicitud
     */
    private void retornarJSON(String json, HttpServletResponse response) {

        try {
            if (json != null) {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Envï¿½a los datos necesarios (PK) de un registro para recuperar toda la
     * informaciï¿½n pertinente de la base de datos y enviarla a la oficina
     * apropiada.
     *
     * @param mapping
     * @param request
     * @param uor
     * @param tipoES
     * @param params
     * @return
     */
    private ActionForward enviarRegistro(ActionMapping mapping, HttpServletRequest request,
                                         HttpServletResponse response, String uor, String tipoES, String[] params) {

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        int ejercicio = Integer.valueOf(request.getParameter("anoEjercicio"));
        int numeroRegistro = Integer.valueOf(request.getParameter("numeroRegistro"));
        int departamento = 1; // Por ahora siempre es asï¿½
        String tipo = UtilidadesRegistro.parsearTipoEntradaSalida(tipoES);

        GestionSir gestionSir = new GestionSir();
        LectorProperties properties = new LectorProperties(GestionSirConstantes.PROPERTIES_MENSAJES_SIR, usuario.getIdioma());
        try {
            try {
                gestionSir.enviarAsiento(uor, tipo, ejercicio, numeroRegistro, departamento, params);
            } catch (BDException ex) {
                response.getWriter().write(properties.getMensaje(CodigoMensajeSir.ERROR_ENVIAR_ASIENTO.getCodigo()));
            } catch (GestionSirException ex) {
                m_Log.debug("Error al enviar registro", ex);
                response.getWriter().write(properties.getMensaje(ex.getCodigo(), ex.getParametros()));
            }
        } catch (Exception ex) {
            m_Log.error("Se ha producido un error al enviar el registro", ex);
        }
        return mapping.findForward("enviarRegistro");
    }


    @SuppressWarnings("unchecked")
    private RegistroValueObject getRegistro(Vector datosAnotacionOriginal) {
        // 1) Log tamaño y contenido original
        if (datosAnotacionOriginal == null) {
            m_Log.warn("getRegistro: datosAnotacionOriginal es null");
            return new RegistroValueObject();
        } else {
            m_Log.debug("getRegistro: datosAnotacionOriginal.size()="
                    + datosAnotacionOriginal.size()
                    + ", contenido=" + datosAnotacionOriginal);
        }

        // 2) Aplana todas las capas de Vector anidados
        Vector datosAnotacion = datosAnotacionOriginal;

        m_Log.debug("getRegistro: datosAnotacion aplanado.size()="
                + datosAnotacion.size()
                + ", contenido=" + datosAnotacion);

        if (datosAnotacion.isEmpty()) {
            m_Log.warn("getRegistro: datosAnotacion vacío tras desanidar");
            return new RegistroValueObject();
        }

        RegistroValueObject reg = new RegistroValueObject();
        Object v;

        // ? Campos 0?20 (identificadores, asunto, terceros, etc.) ?
        v = safeElem(datosAnotacion, 0);
        if (hasText(v)) reg.setIdentDepart(toInteger(v));
        v = safeElem(datosAnotacion, 1);
        if (hasText(v)) reg.setUnidadOrgan(toInteger(v));
        reg.setTipoReg(toString(safeElem(datosAnotacion, 2)));
        v = safeElem(datosAnotacion, 3);
        if (hasText(v)) reg.setAnoReg(toInteger(v));
        v = safeElem(datosAnotacion, 4);
        if (hasText(v)) reg.setNumReg(toLong(v));

        reg.setFecEntrada(           toString(safeElem(datosAnotacion, 5)));
        reg.setFecHoraDoc(           toString(safeElem(datosAnotacion, 6)));
        reg.setAsunto(               toString(safeElem(datosAnotacion, 7)));
        reg.setNombreInteresado(     toString(safeElem(datosAnotacion, 8)));
        reg.setApellido1Interesado(  toString(safeElem(datosAnotacion, 9)));
        reg.setApellido2Interesado(  toString(safeElem(datosAnotacion, 10)));
        reg.setNomOrganizacionOrigen(toString(safeElem(datosAnotacion, 11)));
        reg.setOrgDestino(           toString(safeElem(datosAnotacion, 12)));

        v = safeElem(datosAnotacion, 13);
        if (hasText(v)) reg.setEstAnotacion(toInteger(v));
        reg.setTipoDoc(              toString(safeElem(datosAnotacion, 14)));
        v = safeElem(datosAnotacion, 15);
        if (hasText(v)) reg.setNumTerceros(toInteger(v));

        reg.setObservaciones(        toString(safeElem(datosAnotacion, 16)));
        reg.setCodProcedimiento(     toString(safeElem(datosAnotacion, 17)));
        reg.setNumExpediente(        toString(safeElem(datosAnotacion, 18)));
        reg.setUsuarioQRegistra(     toString(safeElem(datosAnotacion, 19)));

        v = safeElem(datosAnotacion, 20);
        if (hasText(v)) reg.setTipoAnot(toInteger(v));


        // ? RAW_FECHA_DOCU ? índice 21 ? la fecha de documento tal cual viene de la consulta
        Object rawFechaDocu = safeElem(datosAnotacion, 21);
        if (hasText(rawFechaDocu)) {
            // Asegúrate de que tu VO tenga este setter:
            reg.setFechaDocu(toString(rawFechaDocu));
        }

        // FIN DIGITALIZACIÓN ? índice 22
        v = safeElem(datosAnotacion, 22);
        if (hasText(v)) reg.setFinDigitalizacion(toBoolean(v));

        // ESTADO EXPEDIENTE ? índice 23
        v = safeElem(datosAnotacion, 23);
        if (hasText(v)) reg.setEstadoExpediente(toInteger(v));

        // ESTADO SIR ? índice 24
        v = safeElem(datosAnotacion, 24);
        if (hasText(v)) reg.setEstadoSIR(toInteger(v));

        // FECHA ESTADO SIR ? índice 25
        Object v25 = safeElem(datosAnotacion, 25);
        if (hasText(v25)) {
            reg.setFechaEstadoSIR(toDateOrParse(v25));
        }

        // IDENTIFICADOR REGISTRO SIR ? índice 26
        reg.setIdentificadorRegistroSIR(toString(safeElem(datosAnotacion, 26)));

        return reg;
    }


    private Object safeElem(Vector v, int idx) {
        if (v == null || idx < 0 || idx >= v.size()) return null;
        return v.elementAt(idx);
    }

    private boolean hasText(Object o) {
        return o != null && o.toString().trim().length() > 0;
    }

    // =======================
    // Métodos de conversión
    // =======================
    private Integer toInteger(Object value) {
        if (value == null) return null;
        String s = value.toString().trim();
        if (s.isEmpty()) return null;
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            m_Log.warn("toInteger: valor no numérico '" + s + "', devolviendo null", e);
            return null;
        }
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        String s = value.toString().trim();
        if (s.isEmpty()) return null;
        try {
            return Long.valueOf(s);
        } catch (NumberFormatException e) {
            m_Log.warn("toLong: valor no numérico '" + s + "', devolviendo null", e);
            return null;
        }
    }

    private Boolean toBoolean(Object value) {
        if (value == null) return null;
        String s = value.toString().trim().toLowerCase();
        if (s.isEmpty()) return null;
        if ("1".equals(s) || "true".equals(s))  return Boolean.TRUE;
        if ("0".equals(s) || "false".equals(s)) return Boolean.FALSE;
        m_Log.warn("toBoolean: valor inesperado '" + s + "', devolviendo null");
        return null;
    }

    private String toString(Object value) {
        return (value != null) ? value.toString() : null;
    }

    private Date toDateOrParse(Object value) {
        if (value instanceof Date) return (Date) value;
        if (value instanceof String) {
            String s = ((String) value).trim();
            if (!s.isEmpty()) {
                for (String fmt : new String[]{
                        "EEE MMM dd HH:mm:ss z yyyy",
                        "dd/MM/yyyy HH:mm:ss",
                        "yyyy-MM-dd HH:mm:ss"
                }) {
                    try {
                        return new SimpleDateFormat(fmt, Locale.ENGLISH).parse(s);
                    } catch (ParseException ignored) { }
                }
                m_Log.warn("toDateOrParse: no pude parsear fecha '" + s + "'");
            }
        }
        return null;
    }
}

