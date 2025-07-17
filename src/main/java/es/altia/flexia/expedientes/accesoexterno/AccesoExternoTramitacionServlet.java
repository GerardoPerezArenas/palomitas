package es.altia.flexia.expedientes.accesoexterno;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UsuariosGruposDAO;
import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.registro.persistence.AuditoriaManager;
import es.altia.agora.business.sge.ConsultaExpedientesValueObject;
import es.altia.agora.business.sge.InteresadoExpedienteVO;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.FichaExpedienteManager;
import es.altia.agora.business.sge.persistence.manual.ConsultaExpedientesDAO;
import es.altia.agora.business.sge.persistence.manual.FichaExpedienteDAO;
import es.altia.agora.business.sge.persistence.manual.InteresadosDAO;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.TipoDocumentosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.catalogoformularios.model.solicitudes.vo.FormularioTramitadoVO;
import es.altia.catalogoformularios.model.solicitudesfacade.FormularioFacade;
import es.altia.catalogoformularios.util.DateOperations;
import es.altia.catalogoformularios.util.exceptions.InternalErrorException;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.auditoria.ConstantesAuditoria;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.historico.expedientes.dao.ExpedienteDAO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoFactoria;
import es.altia.util.conexion.AdaptadorSQLBD;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

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
public class AccesoExternoTramitacionServlet extends HttpServlet {

    private Logger log = Logger.getLogger(AccesoExternoTramitacionServlet.class);
    
    private static Config m_Config = ConfigServiceHelper.getConfig("common");
    private static Config m_ConfigExp = ConfigServiceHelper.getConfig("Expediente");
    private static Config m_AuthConfig = ConfigServiceHelper.getConfig("authentication");

    private static final String JSP_EXITO = "/jsp/sge/accesoExternoExpediente/fichaExpedienteAccesoExterno.jsp";
    private static final String JSP_ERROR = "/jsp/sge/accesoExternoExpediente/errorAccesoExterno.jsp";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void defaultAction(HttpServletRequest request, HttpServletResponse response) throws Exception {

        log.debug("EJECUTANDO SERVLET: AccesoExternoTramitacionServlet");

        HttpSession session = request.getSession();
        if (session == null) {
            log.error("SESION NULA EN AccesoExternoTramitacionServlet.defaultAction");
            return;
        }

        log.debug("ID de la sesión: " + session.getId());
        String codOrganizacion = request.getParameter("codOrganizacion");
        String numExpediente = request.getParameter("numExpediente");
        
        log.debug("cod orgnizacion: " + codOrganizacion);
        log.debug("numExpediente: " + numExpediente);

        boolean existeLogin = false;
        
        RequestDispatcher rd = null;
        UsuarioValueObject usuarioVO = null;
        UsuarioEscritorioValueObject usuarioEscritorioVO = null;

        try {
            // Se obtiene de la request el ticket devuelto por CAS
            //String ticket = request.getParameter("ticket");
            String ticket = "ticket";
            // Se obtiene de la session la Assertion devuelta por CAS
            Assertion assertion = (AssertionImpl) session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
            
            if(ticket != null || assertion != null){
                log.info("Hay sesión activa en CAS");  
                
                //String userAuth = request.getRemoteUser();
                String userAuth = "MILA_USU1";
                log.info(String.format("El remoteUser que viene en la request es: %s", userAuth));
                
                //Comprobamos si ese usuario existe en nuestra bbdd en mayusculas o en minusculas
                userAuth=UsuarioManager.getInstance().dameElCorrecto(userAuth);
                log.info(String.format("El usuario autenticado después de pedir el correcto es: %s", userAuth));

                usuarioEscritorioVO = new UsuarioEscritorioValueObject();
                usuarioEscritorioVO.setLogin(userAuth);
                existeLogin = UsuarioManager.getInstance().existeLogin(usuarioEscritorioVO.getLogin());

                if(existeLogin){
                    usuarioEscritorioVO = UsuarioManager.getInstance().buscaUsuario(usuarioEscritorioVO, true);
                    if (usuarioEscritorioVO.getIdUsuario() != 0) {
                        if (numExpediente != null && !numExpediente.equals("")) {
                            log.debug("---> Se accede a la carga del expediente");
                            
                            //Guardamos el objeto UsuarioEscritorioValueObject en sesión
                            session.setAttribute("usuarioEscritorio", usuarioEscritorioVO);

                            String[] partesNum = numExpediente.split("/");
                            String ejercicio = partesNum[0];
                            String codProcedimiento = partesNum[1];
                            log.debug("ejercicio: " + ejercicio);
                            log.debug("procedimiento: " + codProcedimiento);

                            GeneralValueObject gVO = new GeneralValueObject();
                            gVO.setAtributo("codMunicipio", codOrganizacion);
                            gVO.setAtributo("codProcedimiento", codProcedimiento.toUpperCase());
                            gVO.setAtributo("ejercicio", ejercicio);
                            gVO.setAtributo("numero", numExpediente);

                            // Se recupera la lista de roles del procedimiento para mostrarlo en la ficha del expediente
                            GeneralValueObject gvRol = new GeneralValueObject();
                            gvRol.setAtributo("codMunicipio", codOrganizacion);
                            gvRol.setAtributo("codProcedimiento", codProcedimiento);

                            String modoConsulta = "no";
                            String desdeAltaRE = "no";
                            String desdeConsulta = "no";
                            String opcionExpRel = "no";

                            usuarioVO = new UsuarioValueObject();
                            usuarioVO.setIdUsuario(usuarioEscritorioVO.getIdUsuario());
                            usuarioVO.setNombreUsu(usuarioEscritorioVO.getNombreUsu());
                            usuarioVO.setIdioma(usuarioEscritorioVO.getIdiomaEsc());
                            usuarioVO.setOrgCod(Integer.parseInt(codOrganizacion));
                            usuarioVO.setAppCod(Integer.parseInt(m_ConfigExp.getString("tramitacionExterna/codApl")));
                            usuarioVO.setDepCod(ConstantesDatos.REG_COD_DEP_DEFECTO);
                            usuarioVO.setDep("DEPARTAMENTO POR DEFECTO (1)");
                            usuarioVO.setEntCod(Integer.parseInt(m_ConfigExp.getString("tramitacionExterna/codEnt")));
                            usuarioVO.setEnt(m_ConfigExp.getString("tramitacionExterna/descEnt"));
                            usuarioVO.setCss("/css/estilo.css");

                            gVO.setAtributo("usuario", Integer.toString(usuarioVO.getIdUsuario()));
                            gVO.setAtributo("GrupoUsuario", Integer.toString(UsuarioManager.getInstance().getGrupo(usuarioVO)));

                            String gestor = m_ConfigExp.getString("tramitacionExterna/BBDD/gestor");
                            String jndi = m_ConfigExp.getString("tramitacionExterna/BBDD/jndi/" + codOrganizacion);

                            String[] params = new String[7];
                            params[0] = gestor;
                            params[6] = jndi;

                            FichaExpedienteForm expForm = new FichaExpedienteForm();

                            try {
                                if (params != null) {
                                    int estadoExp = FichaExpedienteManager.getInstance().obtenerEstadoExpediente(numExpediente, params);
                                    if(estadoExp!=0) {
                                        modoConsulta = "si";
                                        desdeConsulta = "si";
                                    }

                                    gVO.setAtributo("modoConsulta", modoConsulta);
                                    gVO.setAtributo("desdeConsulta", desdeConsulta);
                                    gVO.setAtributo("desdeAltaRE", desdeAltaRE);
                                    gVO.setAtributo("opcionExpRel", opcionExpRel);
                                    ArrayList<String> errores = cargarExpediente(expForm, gVO, params, usuarioVO);
                                    if (expForm.getFechaInicio() != null && expForm.getProcedimiento() != null) {
                                        usuarioVO.setParamsCon(params);
                                        session.setAttribute("usuario", usuarioVO);
                                        session.setAttribute("FichaExpedienteForm", expForm);
                                        request.setAttribute("errores", errores);

                                        //  Recuperamos de Expediente.properties la propiedad que indica si se muestra el check de notificaciones en solo lectura o no
                                        boolean esSoloLecturaCheck = false;
                                        try{
                                            String notifModificableProp = m_ConfigExp.getString(Integer.toString(usuarioVO.getOrgCod()) + ConstantesDatos.BARRA + ConstantesDatos.NOTIFICACIONES_SOLO_LECTURA); 
                                            if(notifModificableProp!=null && "SI".equalsIgnoreCase(notifModificableProp)){
                                                esSoloLecturaCheck = true;
                                            }
                                        }catch(Exception e){
                                            log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + Integer.toString(usuarioVO.getOrgCod()) + ConstantesDatos.BARRA + ConstantesDatos.NOTIFICACIONES_SOLO_LECTURA + " EN Expediente.properties");
                                            esSoloLecturaCheck = false;
                                        } finally{
                                            expForm.setReadOnlyCheck(esSoloLecturaCheck);
                                        }
                                        //  Recuperamos de Expediente.properties la propiedad que indica si el check de notificaciones se muestra marcado por defecto o no
                                        boolean esMarcadoPDefCheck = false;
                                                        String propiedadCheckActivo = Integer.toString(usuarioVO.getOrgCod()) + ConstantesDatos.BARRA + ConstantesDatos.CHECK_NOTIFICACIONES_ACTIVO;
                                        try{
                                            String valorCheckActivo = m_ConfigExp.getString(propiedadCheckActivo); 
                                            if(valorCheckActivo!=null && "SI".equalsIgnoreCase(valorCheckActivo)){
                                                esMarcadoPDefCheck = true;
                                            }
                                        }catch(Exception e){
                                            log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + propiedadCheckActivo + " EN Expediente.properties");
                                            esMarcadoPDefCheck = false;
                                        } finally{
                                            expForm.setCheckNotifActivo(esMarcadoPDefCheck);
                                        }
                                        // #253692: Recuperamos propiedad que indica si se mostrará el campo de texto con la ubicación de la documentación
                                        boolean mostrarUbicacion = false;
                                        try{
                                            String mostrarUbicacionProp = m_ConfigExp.getString(Integer.toString(usuarioVO.getOrgCod()) + ConstantesDatos.BARRA + ConstantesDatos.MOSTRAR_UBICACION_DOCUMENTACION); 
                                            if(mostrarUbicacionProp!=null && "si".equalsIgnoreCase(mostrarUbicacionProp)){
                                                mostrarUbicacion = true;
                                            }
                                        }catch(Exception e){
                                            log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + Integer.toString(usuarioVO.getOrgCod()) + ConstantesDatos.BARRA + ConstantesDatos.MOSTRAR_UBICACION_DOCUMENTACION + " EN Expediente.properties");
                                            mostrarUbicacion = false;
                                        } finally{
                                            expForm.setUbicacionDocumentacionVis(mostrarUbicacion);
                                        }

                                        try {
                                            String valor = m_ConfigExp.getString(usuarioVO.getOrgCod()+"/MOSTRAR_MARCA_TRAMITE_NOTIFICADO");
                                            if(valor!=null && !valor.equals("") && valor.equalsIgnoreCase("si")){
                                                request.setAttribute("mostrarTramiteNotificado", valor);
                                            }
                                            log.debug("Propiedad '" + usuarioVO.getOrgCod()+ "/MOSTRAR_MARCA_TRAMITE_NOTIFICADO' de Expediente.properties recuperada. Valor = " + valor);
                                        } catch (Exception ex){
                                            log.error("Error al recuperar la propiedad '" + usuarioVO.getOrgCod()+ "/MOSTRAR_MARCA_TRAMITE_NOTIFICADO' de Expediente.properties");
                                        }

                                        // Auditoria de accesos al expediente
                                        try {
                                            AuditoriaManager.getInstance().auditarAccesoExpediente(
                                                    ConstantesAuditoria.EXPEDIENTE_VER,
                                                    usuarioVO,
                                                    gVO);
                                        } catch (TramitacionException te) {
                                            log.error("No se pudo registrar el evento de auditoria", te);
                                        }

                                        rd = getServletContext().getRequestDispatcher(JSP_EXITO);

                                    } else {
                                        request.setAttribute("errorTramitacionExterna", "El número de expediente indicado no existe");
                                        rd = getServletContext().getRequestDispatcher(JSP_ERROR);
                                    }

                                } else {
                                    log.debug("no hay paramétros");
                                    request.setAttribute("errorTramitacionExterna", "Se ha producido un error al obtener la conexión");
                                    rd = getServletContext().getRequestDispatcher(JSP_ERROR);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            request.setAttribute("errorTramitacionExterna", "El número de expediente indicado no es un número de expediente válido");
                            rd = getServletContext().getRequestDispatcher(JSP_ERROR);
                        }
                    } else {
                        request.setAttribute("errorTramitacionExterna", "El usuario autenticado no es un usuario válido");
                        rd = getServletContext().getRequestDispatcher(JSP_ERROR);
                    }               
                } else {
                    request.setAttribute("errorTramitacionExterna", "El usuario autenticado no es un usuario válido");
                    rd = getServletContext().getRequestDispatcher(JSP_ERROR);
                }                
            } else {
                log.info("No hay una sesión activa en CAS");
                String urlLogin = m_AuthConfig.getString(String.format("Auth/%s/urlLogin", m_AuthConfig.getString("Auth/accessMode")));
                log.info("Se va a redirigir a: " + urlLogin);

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

          log.info("Entrando en el servlet AccesoExternoTramitacionServlet");
            defaultAction(req, res);
        } catch (Exception e) {
            log.error("ERROR doGet:" + e);
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            defaultAction(req, res);
        } catch (Exception e) {
            log.error("ERROR doGet:" + e);
            e.printStackTrace();
        }
    }

    private ArrayList<String> cargarExpediente(FichaExpedienteForm expForm, GeneralValueObject gVO, String[] params, UsuarioValueObject usuarioVO) {

        AdaptadorSQLBD adapt = null;
        Connection con = null;
        ArrayList<String> errores = new ArrayList<String>();
        boolean comprobarModoConsultaExpediente = false;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            Calendar calendario = null;

            if (("SI").equalsIgnoreCase((String) gVO.getAtributo("comprobarModuloConsultaExpediente"))) {
                comprobarModoConsultaExpediente = true;
            }

            /**
             * ************************** NUEVO *********************************************
             */
            // Se recuperan los códigos de la unidades organizativas sobre las que tiene permiso el usuario tramitador.
            // Se ha modificado la consulta, puesto que se recuperaba información de más
            ArrayList<String> codUorsUsuario = UsuariosGruposDAO.getInstance().getListaCodigosUnidadesOrganicasUsuario(usuarioVO.getIdUsuario(), usuarioVO.getOrgCod(), con);
            expForm.setCodigosUorsPermisoUsuario(codUorsUsuario);

            // Se recupera los tipos de documentos que pueden tener los terceros
            expForm.setListaTiposDocumentosTerceros(TipoDocumentosDAO.getInstance().getListaTipoDocumentos(con));

            // Se recupera la lista de roles del procedimiento al que pertenece el expediente
            expForm.setListaRoles(InteresadosDAO.getInstance().getListaRoles(usuarioVO.getOrgCod(), (String) gVO.getAtributo("codProcedimiento"), con));

            String expHistorico = (String) gVO.getAtributo("expHistorico");

            // Se comprueba si el expediente está en el histórico
            if (expHistorico == null) {

                String numero = (String) gVO.getAtributo("numero");
                expHistorico = (ExpedienteDAO.getInstance().estaExpedienteActivo(Integer.parseInt((String) gVO.getAtributo("codMunicipio")),
                        Integer.parseInt((String) gVO.getAtributo("ejercicio")), numero, con)) ? "false" : "true";

                log.debug(" =============> FichaExpedienteAction.do expHistorico: " + expHistorico);
            }

            expForm.setExpHistorico("true".equals(expHistorico));
            gVO.setAtributo("expHistorico", expHistorico);

            /**
             * ************************** NUEVO *********************************************
             */
            gVO = FichaExpedienteDAO.getInstance().cargaExpediente(gVO, adapt, con);

            /**
             * **SE COMPRUEBA SI EL EXPEDIENTE SE MUESTRA EN MODO TRAMITACION O
             * EN MODO CONSULTA  ****
             */
            if (comprobarModoConsultaExpediente) {

                ConsultaExpedientesValueObject consExpVO = new ConsultaExpedientesValueObject();
                consExpVO.setNumeroExpediente((String) gVO.getAtributo("numero"));
                consExpVO.setExpHistorico("true".equals(expHistorico) ? true : false);

                boolean esPendiente = false;

                String desdeConsulta = (String) gVO.getAtributo("desdeConsulta");
                if (desdeConsulta != null && !"".equals(desdeConsulta) && desdeConsulta.equalsIgnoreCase("SI")) {
                    // Se comprueba si el expediente está pendiente, y si el usuario tiene permiso sobre la unidad de inicio
                    // del expediente, o sobre alguna de las unidades tramitadoras de alguno de los trámites
                    esPendiente = ConsultaExpedientesDAO.getInstance().esPendienteParaUsuario(usuarioVO, consExpVO, con);
                }

                int estado = Integer.parseInt((String) gVO.getAtributo("estado"));

                boolean permiteModificarObs = false;
                // Si el expediente está finalizado o anulado, se comprueba si el usuario tramitador tiene permiso
                // para poder modificar las observaciones del expediente. Si el expediente está pendiente no se hace dicha consulta                
                if (estado == ConstantesDatos.ESTADO_EXPEDIENTE_FINALIZADO || estado == ConstantesDatos.ESTADO_EXPEDIENTE_ANULADO) {
                    permiteModificarObs = ConsultaExpedientesDAO.getInstance().permiteModificarObservacionesUsuario(usuarioVO, consExpVO, con);
                }
                if("si".equals((String) gVO.getAtributo("modoConsulta"))){
                    if (esPendiente) {
                        gVO.setAtributo("modoConsulta", "no");
                    }

                    if ((!esPendiente) && (permiteModificarObs)) {
                        gVO.setAtributo("permiteModificarObs", "si");
                    }
                }

            }// if

            expForm.setExpedienteVO(gVO);
            expForm.setUbicacionDoc((String) gVO.getAtributo("ubicacionDoc"));
            /**
             * ******************** NUEVO *******************************
             */

            /* Miramos si tiene exp relacionados, y en base a eso mostraremos el botón
             * de exp relacionados de un color u otro
             */
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

            boolean expedientesRelacionados = FichaExpedienteDAO.getInstance().tieneExpedientesRelacionados(codMunicipio, ejercicio, numero, con);
            gVO.setAtributo("expedientesRelacionados", expedientesRelacionados);

            ArrayList<InteresadoExpedienteVO> lTerceros = InteresadosDAO.getInstance().getListaInteresadosExpediente(gVO, adapt, con);
            expForm.setTerceros(lTerceros);

            Vector tramites = FichaExpedienteDAO.getInstance().cargaTramites(gVO, adapt, con, false);
            expForm.setTramites(tramites);

            gVO.setAtributo("desdeJsp", "si");

            Vector estructuraDatosSuplementarios = new Vector();
            expForm.setEstructuraDatosSuplementarios(estructuraDatosSuplementarios);

            Vector valoresDatosSuplementarios = new Vector();
            expForm.setValoresDatosSuplementarios(valoresDatosSuplementarios);

            expForm.setListaAgrupacionesCampos(new Vector());

            expForm.setFormularios(cargarFormularios(gVO, usuarioVO));
            expForm.setNotificacionRealizada((String) gVO.getAtributo("notificacionRealizada"));

            ArrayList<ModuloIntegracionExterno> modulos = ModuloIntegracionExternoFactoria.getInstance().getImplClassModuloConPantallaExpediente(Integer.parseInt(codMunicipio), (String) gVO.getAtributo("codProcedimiento"), false, usuarioVO.getIdUsuario(), params);

            log.debug("NUMERO DE MÓDULOS RECUPERADOS: " + modulos.size());
            expForm.setModulosExternos(modulos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }

        }

        return errores;
    }

    private Collection cargarFormularios(GeneralValueObject gVO, UsuarioValueObject usuarioVO) {
        Collection rdo = new ArrayList();
        Collection sal = new ArrayList();
        try {
            if ("si".equals(m_Config.getString("JSP.Formularios"))) {
                //llamar a la fachada de formularios
                FormularioFacade facade = new FormularioFacade((String) gVO.getAtributo("codOrganizacion"));
                rdo = facade.getFormsOfExpediente((String) gVO.getAtributo("numero"),
                        (String) gVO.getAtributo("codOrganizacion"));
            }
        } catch (InternalErrorException e) {
            e.printStackTrace();
            log.debug("***** Fallo al cargar Formularios " + e.getMessage());
        }

        Iterator it = rdo.iterator();

        while (it.hasNext()) {
            FormularioTramitadoVO formVO = (FormularioTramitadoVO) it.next();
            GeneralValueObject genVO = new GeneralValueObject();

            genVO.setAtributo("codigo", formVO.getCodigo());
            genVO.setAtributo("descripcion", formVO.getDescripcion());
            genVO.setAtributo("fecMod", DateOperations.toString(formVO.getFecMod(), "dd-MM-yyyy"));
            genVO.setAtributo("tipo", formVO.getTipo());
            genVO.setAtributo("estado", formVO.getEstado());
            genVO.setAtributo("usuario", UsuarioManager.getInstance().getNombre(formVO.getUsuario()));

            try {
                if ("si".equals(m_Config.getString("JSP.Formularios"))) {
                    //llamar a la fachada de formularios
                    FormularioFacade facade = new FormularioFacade();
                    Collection rdo1 = new ArrayList();
                    rdo1 = facade.getFicherosOfForm((String) formVO.getCodigo());
                    if (rdo1.size() > 0) {
                        genVO.setAtributo("tieneAnexo", "si");

                    } else {
                        genVO.setAtributo("tieneAnexo", "no");
                    }
                }
            } catch (InternalErrorException e) {

                log.debug("***** Fallo al cargar Formularios " + e.getMessage());
            }

            sal.add(genVO);
        }

        return sal;
    }

    private boolean comprobarAplicacion(String aplicaciones, String apli) {

        Vector<String> palabras = new Vector<String>();
        StringTokenizer tokenizer = new StringTokenizer(aplicaciones, ";");
        while (tokenizer.hasMoreTokens()) {
            palabras.add(tokenizer.nextToken());
        }
        return palabras.contains(apli);

    }
}
