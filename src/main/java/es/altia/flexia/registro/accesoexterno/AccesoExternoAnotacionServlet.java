package es.altia.flexia.registro.accesoexterno;

import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;
import es.altia.agora.business.escritorio.RegistroUsuarioValueObject;
import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.registro.DiligenciasValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.ReservaOrdenValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.mantenimiento.persistence.MantAsuntosManager;
import es.altia.agora.business.registro.mantenimiento.persistence.MantClasifAsuntosManager;
import es.altia.agora.business.registro.mantenimiento.persistence.MantRegistroExternoManager;
import es.altia.agora.business.registro.mantenimiento.MantTemasValueObject;
import es.altia.agora.business.registro.mantenimiento.persistence.MantDocumentoManager;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.registro.persistence.AuditoriaManager;
import es.altia.agora.business.registro.persistence.DiligenciasManager;
import es.altia.agora.business.registro.persistence.RegistroAperturaCierreManager;
import es.altia.agora.business.registro.persistence.ReservaOrdenManager;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.InteresadosManager;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm;
import es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.Fecha;
import es.altia.catalogoformularios.model.solicitudes.vo.FormularioTramitadoVO;
import es.altia.catalogoformularios.model.solicitudesfacade.FormularioFacade;
import es.altia.catalogoformularios.util.exceptions.InternalErrorException;
import es.altia.common.service.auditoria.ConstantesAuditoria;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.expedientes.relacionados.plugin.PluginExpedientesRelacionados;
import es.altia.flexia.expedientes.relacionados.plugin.PluginExpedientesRelacionadosFlexia;
import es.altia.flexia.expedientes.relacionados.plugin.factoria.PluginExpedientesRelacionadosFactoria;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;

/**
 *
 * @author altia
 */
public class AccesoExternoAnotacionServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(AccesoExternoAnotacionServlet.class);

    private static final Config CONFIG = ConfigServiceHelper.getConfig("common");
    private static final Config CONFIGREG = ConfigServiceHelper.getConfig("Registro");
    private static Config CONFIGAUTH = ConfigServiceHelper.getConfig("authentication");

    private static final String JSP_EXITO = "/jsp/registro/accesoexterno/detalleAnotacionAccesoExterno.jsp";
    private static final String JSP_ERROR = "/jsp/registro/accesoexterno/errorAccesoExterno.jsp";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void defaultAction(HttpServletRequest request, HttpServletResponse response) throws Exception {

        LOG.debug("EJECUTANDO SERVLET: AccesoExternoAnotacionServlet");

        HttpSession session = request.getSession();
        if (session == null) {
            LOG.error("SESION NULA EN AccesoExternoAnotacionServlet.defaultAction");
            return;
        }

        LOG.debug("ID de la sesión: " + session.getId());
        String codOrganizacion = request.getParameter("organizacion");
        String ejercicio = request.getParameter("ejercicio");
        String numAnotacion = request.getParameter("numero");
        String unidadRegistro = request.getParameter("libro");
        String tipoAnotacion = request.getParameter("tipo");

        LOG.debug("Organizacion: " + codOrganizacion);
        LOG.debug("Unidad registro: " + unidadRegistro);
        LOG.debug("Tipo anotación: " + tipoAnotacion);
        LOG.debug("Ejercicio: " + ejercicio);
        LOG.debug("Nº anotación: " + numAnotacion);

        boolean existeLogin = false;

        RequestDispatcher rd = null;
        UsuarioValueObject usuarioVO = null;
        UsuarioEscritorioValueObject usuarioEscritorioVO = null;

        try {
            // Se obtiene de la request el ticket devuelto por CAS
            String ticket = request.getParameter("ticket");
            // Se obtiene de la session la Assertion devuelta por CAS
            Assertion assertion = (AssertionImpl) session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);

            if (ticket != null || assertion != null) {
                LOG.info("Hay sesión activa en CAS");

                String userAuth = request.getRemoteUser();
                LOG.info(String.format("El remoteUser que viene en la request es: %s", userAuth));

                //Comprobamos si ese usuario existe en nuestra bbdd en mayusculas o en minusculas
                userAuth = UsuarioManager.getInstance().dameElCorrecto(userAuth);
                LOG.info(String.format("El usuario autenticado después de pedir el correcto es: %s", userAuth));

                usuarioEscritorioVO = new UsuarioEscritorioValueObject();
                usuarioEscritorioVO.setLogin(userAuth);
                existeLogin = UsuarioManager.getInstance().existeLogin(usuarioEscritorioVO.getLogin());

                if (existeLogin) {
                    int departamento = ConstantesDatos.REG_COD_DEP_DEFECTO;
                    int codEntidad = Integer.parseInt(CONFIGREG.getString("registroExterno/codEnt"));

                    if (unidadRegistro == null || unidadRegistro.equals("")) {
                        unidadRegistro = CONFIGREG.getString("registroExterno/codUor");
                    }
                    if (tipoAnotacion == null || tipoAnotacion.equals("")) {
                        tipoAnotacion = CONFIGREG.getString("registroExterno/tipoAnot");
                    }
                    if ((ejercicio != null && !ejercicio.equals("")) || (numAnotacion != null && !numAnotacion.equals(""))) {
                        LOG.debug("---> Se accede a la carga de la anotación");

                        LOG.debug(String.format("Clave primaria de la anotación (dep/uor/tip/eje/num): %s/%s/%s/%s/%s", departamento, unidadRegistro, tipoAnotacion, ejercicio, numAnotacion));

                        usuarioEscritorioVO = UsuarioManager.getInstance().buscaUsuario(usuarioEscritorioVO);
                        session.setAttribute("usuarioEscritorio", usuarioEscritorioVO);
                        session.setAttribute("tipoAnotacion", tipoAnotacion);

                        String gestor = CONFIGREG.getString("registroExterno/BBDD/gestor");
                        String jndi = CONFIGREG.getString("registroExterno/BBDD/jndi/" + codOrganizacion);

                        String[] params = new String[7];
                        params[0] = gestor;
                        params[6] = jndi;

                        RegistroUsuarioValueObject registroUsuarioVO = null;
                        RegistroValueObject elRegistroESVO = null;
                        MantAnotacionRegistroForm registroForm = null;

                        try {
                            if (params != null) {
                                elRegistroESVO = new RegistroValueObject();
                                registroForm = new MantAnotacionRegistroForm();
                                registroUsuarioVO = new RegistroUsuarioValueObject();

                                //Semeamos el UsuarioValueObject
                                usuarioVO = new UsuarioValueObject();
                                usuarioVO.setIdUsuario(usuarioEscritorioVO.getIdUsuario());
                                usuarioVO.setNombreUsu(usuarioEscritorioVO.getNombreUsu());
                                usuarioVO.setIdioma(usuarioEscritorioVO.getIdiomaEsc());
                                usuarioVO.setOrgCod(Integer.parseInt(codOrganizacion));
                                usuarioVO.setOrg(CONFIGREG.getString("registroExterno/descEnt"));
                                usuarioVO.setAppCod(Integer.parseInt(CONFIGREG.getString("registroExterno/codApl")));
                                usuarioVO.setDepCod(departamento);
                                usuarioVO.setDep("DEPARTAMENTO POR DEFECTO (1)");
                                usuarioVO.setEntCod(codEntidad);
                                usuarioVO.setEnt(CONFIGREG.getString("registroExterno/descEnt"));
                                usuarioVO.setCss("/css/estilo.css");
                                usuarioVO.setUnidadOrgCod(Integer.parseInt(unidadRegistro));
                                usuarioVO.setUnidadOrg(UORsManager.getInstance().getNombreByCodUor(unidadRegistro, params));
                                usuarioVO.setMantenerEntrada(true);
                                usuarioVO.setMantenerSalida(true);
                                usuarioVO.setParamsCon(params);

                                //Seteamos valores del objeto RegistroUsuarioValueObject
                                registroUsuarioVO.setUnidadOrgCod(Integer.parseInt(unidadRegistro));
                                registroUsuarioVO.setDepCod(departamento);
                                //Desde acceso externo no permitimos digitalizar, en principio
                                registroUsuarioVO.setUnidadOrganicaDigit(false);
                                
                                // añadios ambos objetos usuario al session
                                session.setAttribute("usuario", usuarioVO);
                                session.setAttribute("registroUsuario", registroUsuarioVO);
                                
                                String reservas = request.getParameter("reservas");
                                
                                //Obtenemos NUM_OFICINAS_REGISTRO_USUARIO para guardarlo en la session
                                Integer numPermisos = UsuarioManager.getInstance().getNumOficinasRegistroPermiso(usuarioVO.getOrgCod(),usuarioVO.getIdUsuario(), usuarioVO.getUnidadOrgCod(), usuarioVO.getParamsCon());
                                session.setAttribute("numPermisosOficinaRegistro", numPermisos);
                                
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

                                //Seteamos los valores de primary key del objeto RegistroValueObject que es la anotacion
                                elRegistroESVO.setIdentDepart(departamento);
                                elRegistroESVO.setUnidadOrgan(Integer.parseInt(unidadRegistro));
                                elRegistroESVO.setTipoReg(tipoAnotacion);
                                elRegistroESVO.setAnoReg(Integer.parseInt(ejercicio));
                                elRegistroESVO.setNumReg(Long.parseLong(numAnotacion));

                                // Buscamos el asiento por su clave
                                elRegistroESVO = AnotacionRegistroManager.getInstance().getByPrimaryKey(elRegistroESVO, params);

                                if (elRegistroESVO.getFecEntrada() != null) {
                                    //Buscamos los datos para el bloque sga en los casos que se carga este bloque	
                                    if (CONFIGREG.getString("mostrar_datos_sga").toUpperCase().equals("SI")) {
                                        String[] datosSga = AnotacionRegistroManager.getInstance().getDatosSga(elRegistroESVO, params);
                                        elRegistroESVO.setCodigoSga(datosSga[0]);
                                        elRegistroESVO.setExpedienteSga(datosSga[1]);
                                    }
                                    LOG.debug("LA ANOTACION ES: " + elRegistroESVO);
                                    // #guardamos en el form de la anotacion el valor de si el asunto permite o no interesados sin doc
                                    registroForm.setTipoDocInteresadoOblig(elRegistroESVO.isTipoDocInteresadoOblig());

                                    // Comprobamos directivas de usuario para registro
                                    String directivaSalidasUorUsuario = "NO";
                                    if (UsuariosGruposManager.getInstance().tienePermisoDirectiva(ConstantesDatos.REGISTRO_S_SOLO_UORS_USUARIO, usuarioVO.getIdUsuario(), params)) {
                                        directivaSalidasUorUsuario = "SI";
                                    }
                                    session.setAttribute("directiva_salidas_uor_usuario", directivaSalidasUorUsuario);
                                    
                                    // Se guarda en el form la lista de uors segun el tipoAnotacion y el valor de la anterior directiva
                                    Vector nUOR = new Vector();
                                    if (("SI".equals(directivaSalidasUorUsuario)) && (tipoAnotacion != null) && ("S".equals(tipoAnotacion))) {
                                        nUOR = UORsManager.getInstance().getListaUORsPermisoUsuarioNuevo(usuarioVO, params);
                                    } else {
                                        nUOR = UORsManager.getInstance().getListaUORs(false, params);
                                    }
                                    registroForm.setListaNuevasUORs(nUOR);
                                    
                                    // se guarda en el form de la anotaci?n si el asunto bloquea o no la opci?n de modificar la unidad de destino y procedimiento
                                    registroForm.setBloquearDestino(elRegistroESVO.isBloquearDestino());
                                    registroForm.setBloquearProcedimiento(elRegistroESVO.isBloquearProcedimiento());

                                    // Comprobacion de si la anotacion de registro se ha dado de alta desde el servicio web WSRegistroES
                                    registroForm.setRegistroTelematico(elRegistroESVO.isRegistroTelematico());
                                    // Recuperamos la propiedad REGISTRO_TELEMATICO_MODIFICABLE. Si la propiedad es si y el registro no tiene expedientes asociados indicamos que el registro puede ser modificado
                                    boolean regTelemMod = false;
                                    String propRegTelemMod = usuarioVO.getOrgCod() + "/REGISTRO_TELEMATICO_MODIFICABLE";
                                    if (elRegistroESVO.isRegistroTelematico() && (elRegistroESVO.getNumExpedientesRelacionados() != null && elRegistroESVO.getNumExpedientesRelacionados().isEmpty() || elRegistroESVO.getNumExpedientesRelacionados() == null)) {
                                        try {
                                            String valor = CONFIGREG.getString(propRegTelemMod);
                                            LOG.debug("Valor de la propiedad " + propRegTelemMod + " de Registro.properties: " + valor);
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
                                            LOG.debug("No se ha podido recuperar la propiedad " + propRegTelemMod + " de Registro.properties");
                                        }
                                    }
                                    LOG.debug("Es registro telematico modificable? " + registroForm.isRegistroTelematicoModicable());

                                    // TODO cambiado para controlar UOR de baja en destino
                                    registroForm.setCod_uniRegDestinoORD(elRegistroESVO.getIdUndTramitad());
                                    request.setAttribute("codigo_uor_real", registroForm.getCod_uniRegDestinoORD());

                                    //Para buscar en la tabla diligencias
                                    String fechaEnt = elRegistroESVO.getFecEntrada();
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("la fecha de entrada en el action es : " + fechaEnt);
                                    }
                                    DiligenciasValueObject diligencia = new DiligenciasValueObject();
                                    diligencia.setCodDepto(elRegistroESVO.getIdentDepart());
                                    diligencia.setCodUnidad(elRegistroESVO.getUnidadOrgan());
                                    diligencia.setTipo(elRegistroESVO.getTipoReg().charAt(0));
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("el tipo de registro en el diligencia es : " + diligencia.getTipo());
                                    }
                                    if (fechaEnt != null) {
                                        String fechaEnt1 = fechaEnt.substring(0, 10);
                                        diligencia.setFecha(fechaEnt1);
                                        diligencia = DiligenciasManager.getInstance().load(diligencia, params);
                                        String textoDil = diligencia.getAnotacion();
                                        if (LOG.isDebugEnabled()) {
                                            LOG.debug("el texto de la diligencia en el action es : " + textoDil);
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
                                        if (LOG.isDebugEnabled()) {
                                            LOG.debug("la fecha de entrada es **************^^^^^^^^^^^^^^^^ : " + fechaEntrada1);
                                        }
                                        if (LOG.isDebugEnabled()) {
                                            LOG.debug("la fecha del registro cerrado es **************^^^^^^^^^^^^^^^^ : " + fechaRegCerrado);
                                        }
                                        if (fechaRegCerrado == null) {
                                            elRegistroESVO.setAbiertCerrado("abierto");
                                        } else if (fechaEntrada1 == null) {
                                            elRegistroESVO.setAbiertCerrado("cerrado");
                                        } else if (fechaRegCerrado.compareTo(fechaEntrada1) >= 0) {
                                            elRegistroESVO.setAbiertCerrado("cerrado");
                                            if (LOG.isDebugEnabled()) {
                                                LOG.debug(elRegistroESVO.getAbiertCerrado());
                                            }
                                        } else {
                                            elRegistroESVO.setAbiertCerrado("abierto");
                                        }
                                    }
                                    String fallo = elRegistroESVO.getFallo();
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("el fallo en el action es : " + fallo);
                                    }
                                    String modificar = request.getParameter("modificar");
                                    if(modificar == null){
                                        modificar = "1";
                                    }
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("el valor de modificar es: " + modificar);
                                    }
                                    // Recuperamos los terceros asociados a la anotacion.
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

                                    LOG.debug("Rol por defecto cargado: codigo=" + registroForm.getCodRolDefecto()
                                            + ", descripcion=" + registroForm.getDescRolDefecto());

                                    tercero = getTerceroPrincipal(elRegistroESVO, params);
                                    TercerosValueObject terc = (TercerosValueObject) tercero.firstElement();

                                    if (terc != null && !terc.getIdentificador().equals("0")) {

                                        btForm.setListaTerceros(tercero);
                                        session.setAttribute("BusquedaTercerosForm", btForm);
                                        Fecha f;

                                        if (LOG.isDebugEnabled()) {
                                            LOG.debug("modificar es: " + modificar);
                                        }
                                        if ("1".equals(modificar)) {

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

                                            String generarPeticion = CONFIGREG.getString(usuarioVO.getOrgCod() + "/MODELO_PETICION_RESPUESTA");
                                            registroForm.setMostrarGenerarModelo(generarPeticion);

                                        } else {
                                            inicializarListasVacias(elRegistroESVO);
                                        }
                                    } else {
                                        if (LOG.isDebugEnabled()) {
                                            LOG.debug("el dia de anotacion en el action es: " + elRegistroESVO.getDiaAnotacion());
                                        }
                                        if ("1".equals(modificar)) {
                                            elRegistroESVO.setRespOpcion("modificar");
                                            // Cargar listas.
                                            inicializarListas(elRegistroESVO, false, params);

                                        } else {
                                            inicializarListasVacias(elRegistroESVO);
                                        }
                                        ReservaOrdenValueObject reserva = new ReservaOrdenValueObject();
                                        ReservaOrdenManager.getInstance().loadRER(reserva, params);
                                        String fecha = reserva.getFec();
                                        elRegistroESVO.setFecHoraDoc(fecha);
                                        registroForm.setRegistro(elRegistroESVO);
                                        

                                    }
                                    // añadimos la anotación al form y el form a la request
                                    registroForm.setRegistro(elRegistroESVO);
                                    request.setAttribute("MantAnotacionRegistroForm", registroForm);
                                    
                                    // Auditoria de acceso al registro
                                    AuditoriaManager.getInstance().auditarAccesoRegistro(
                                            ConstantesAuditoria.REGISTRO_VER_ANOTACION, usuarioVO, elRegistroESVO);

                                    rd = getServletContext().getRequestDispatcher(JSP_EXITO);

                                } else {
                                    request.setAttribute("errorAccesoExterno", String.format("La anotación %s/%s de tipo %s no existe en la unidad de registro %s",
                                            ejercicio, numAnotacion, tipoAnotacion, unidadRegistro));
                                    rd = getServletContext().getRequestDispatcher(JSP_ERROR);
                                }
                            } else {
                                LOG.debug("no hay paramétros");
                                request.setAttribute("errorAccesoExterno", "Se ha producido un error al obtener la conexión");
                                rd = getServletContext().getRequestDispatcher(JSP_ERROR);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        request.setAttribute("errorAccesoExterno", "No se ha indicado el ejercicio y/o número de anotación y ambos son campos obligatorios.");
                        rd = getServletContext().getRequestDispatcher(JSP_ERROR);
                    }
                } else {
                    request.setAttribute("errorAccesoExterno", "El usuario autenticado no es un usuario válido");
                    rd = getServletContext().getRequestDispatcher(JSP_ERROR);
                }
            } else {
                LOG.info("No hay una sesión activa en CAS");
                String urlLogin = CONFIGAUTH.getString(String.format("Auth/%s/urlLogin", CONFIGAUTH.getString("Auth/accessMode")));
                LOG.info("Se va a redirigir a: " + urlLogin);

                rd = getServletContext().getRequestDispatcher(String.format("/jsp/redirigeCaduca.jsp?opcion=irALoginCAS&url=%s", urlLogin));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            rd.forward(request, response);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {

            LOG.info("Entrando en el servlet AccesoExternoAnotacionServlet");
            defaultAction(req, res);
        } catch (Exception e) {
            LOG.error("ERROR doGet:" + e);
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            defaultAction(req, res);
        } catch (Exception e) {
            LOG.error("ERROR doGet:" + e);
            e.printStackTrace();
        }
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

    /**
     * Metodo que inicializa las listas del formulario: tipo de documentos, tipo
     * de remitentes, tipo de transportes, tipo de actuaciones, tipo de temas,
     *
     * @param elRegistroESVO: RegistroValueObject
     * @param recuperarTodosAsuntos: si estï¿½ a true , al recuperar los asuntos
     * de registro, se recuperan todos los asuntos, incluidos los que han sido
     * dados de baja.Si es false, solo se recuperan los que no han sido
     * eliminados
     * @param params: Parï¿½metros de conexiï¿½n a la BBDD
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
                LOG.debug(" En cargarFormularios, antes de getFormsOfRegistro . ");
                LOG.debug("--> Tipo de Registro : " + elRegistroESVO.getTipoReg());
                LOG.debug("--> Ano de Registro: " + elRegistroESVO.getAnoReg());
                LOG.debug("--> Numero de Registro : " + String.valueOf(elRegistroESVO.getNumReg()));
                LOG.debug("--> Codigo unidad de Registro : " + elRegistroESVO.getUnidadOrgan());
                LOG.debug("--> Codigo codigoDepartamento : " + elRegistroESVO.getIdOrganizacion());
                //Datos que se le pasan
                String tipoReg = elRegistroESVO.getTipoReg();
                int anoEjercicio = elRegistroESVO.getAnoReg();
                Long numReg = elRegistroESVO.getNumReg();
                int unidadReg = elRegistroESVO.getUnidadOrgan();
                int codDep = elRegistroESVO.getIdEntidad();
                rdo = facade.getFormsOfRegistro(tipoReg, anoEjercicio, numReg.intValue(), unidadReg, codDep);
                LOG.debug("-->Vuelta de getFormsOfRegistro");
                LOG.debug("-->Tamano de collection :" + rdo.size());

                //Comprobamos que se han conseguido los datos correctos
                Iterator it = rdo.iterator();
                while (it.hasNext()) {
                    FormularioTramitadoVO formVO = (FormularioTramitadoVO) it.next();
                    LOG.debug("--> Tipo de Registro : " + formVO.getEntrada().getTipo());
                    LOG.debug("--> Ano de Registro: " + formVO.getEntrada().getEjercicio());
                    LOG.debug("--> Numero de Registro : " + formVO.getEntrada().getNum());
                    LOG.debug("--> Codigo unidad de Registro : " + formVO.getEntrada().getUnidadOrganizativa());
                    LOG.debug("--> Codigo codigoDepartamento : " + formVO.getEntrada().getDepartamento());
                }
            }
        } catch (InternalErrorException e) {

            LOG.debug("***** Fallo al cargar Formularios " + e.getMessage());
        }
        LOG.info("--> Fin cargarFormularios");
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

            LOG.debug("***** Fallo al cargar Formularios " + e.getMessage());
        }

        LOG.info("--> Fin cargarAnexosFormularios");
        return sal;
    }
}
