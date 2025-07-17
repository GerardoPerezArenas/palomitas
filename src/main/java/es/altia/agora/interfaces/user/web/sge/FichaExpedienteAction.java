package es.altia.agora.interfaces.user.web.sge;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.pdf.codec.Base64; 
import es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject;
import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.OrganizacionesDAO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORsDAO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UsuariosGruposDAO;
import es.altia.agora.business.documentos.helper.CodigoSeguroVerificacionHelper;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.mantenimiento.persistence.MantRegistroExternoManager;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.registro.persistence.AuditoriaManager;
import es.altia.agora.business.sge.*;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.*;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.agora.business.integracionsw.persistence.DefinicionOperacionesSWManager;
import es.altia.agora.business.registro.SimpleRegistroValueObject;
import es.altia.agora.business.registro.persistence.InformesManager;
import es.altia.agora.business.sge.manager.visorregistro.VisorRegistroManager;
import es.altia.agora.business.sge.persistence.manual.FichaExpedienteDAO;
import es.altia.agora.business.sge.persistence.manual.TramitacionExpedientesDAO;
import es.altia.agora.business.terceros.mantenimiento.persistence.TipoDocumentosManager;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm; 
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.agora.technical.EstructuraNotificacion;
import es.altia.agora.webservice.registro.exceptions.RegistroException;
import es.altia.catalogoformularios.model.solicitudes.vo.FormularioTramitadoVO;
import es.altia.catalogoformularios.model.solicitudesfacade.FormularioFacade;
import es.altia.catalogoformularios.model.solicitudes.vo.TramiteTramitadoKey;
import es.altia.catalogoformularios.util.DateOperations;
import es.altia.catalogoformularios.util.exceptions.InternalErrorException;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.mail.MailHelper;
import es.altia.common.service.mail.exception.MailServiceNotActivedException;
import es.altia.flexia.business.comunicaciones.AdjuntoComunicacionFlexiaManager;
import es.altia.flexia.business.comunicaciones.ComunicacionFlexiaManager;
import es.altia.flexia.business.comunicaciones.vo.AdjuntoComunicacionVO;
import es.altia.flexia.business.comunicaciones.vo.ComunicacionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.exception.EjecucionModuloException;
import es.altia.flexia.portafirmas.plugin.PluginPortafirmas;
import es.altia.flexia.portafirmas.plugin.factoria.PluginPortafirmasFactoria;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.FirmaVO;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.manual.ConsultaExpedientesDAO;
import es.altia.agora.business.sge.persistence.manual.InteresadosDAO;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.TipoDocumentosDAO;
import es.altia.agora.interfaces.user.web.administracion.SessionInfo;
import es.altia.flexia.eni.GestionEni;
import es.altia.agora.interfaces.user.web.util.OperacionesExpedienteTraductor;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.EstructuraCampoAgrupado;
import es.altia.arboles.impl.ArbolImpl;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.interfaces.user.web.carga.parcial.fichaexpediente.vo.DatosExpedienteVO;
import es.altia.flexia.interfaces.user.web.carga.parcial.fichaexpediente.vo.ResultadoTratamientoCargaParcialExpedienteVO;
import es.altia.flexia.portafirmasexternocliente.factoria.PluginPortafirmasExternoClienteFactoria;
import es.altia.flexia.portafirmasexternocliente.plugin.PluginPortafirmasExternoCliente;
import es.altia.flexia.historico.expedientes.dao.ExpedienteDAO;
import es.altia.common.service.auditoria.ConstantesAuditoria;
import es.altia.eni.conversoreni.CodigosErrores;
import es.altia.eni.conversoreni.ConversorEniException;
import es.altia.eni.conversoreni.StringUtils;
import es.altia.flexia.eni.exception.CodigoMensajeEni;
import es.altia.flexia.eni.exception.GestionEniException;
import es.altia.flexia.eni.util.GestionEniConstantes;
import es.altia.flexia.integracion.cargaExpediente.CargaExpediente;
import es.altia.util.LectorProperties;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.sqlxmlpdf.GeneralPDF;
import es.altia.util.struts.StrutsUtilOperations;
import es.altia.util.evalua_cadena;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletOutputStream;

public final class FichaExpedienteAction extends ActionSession {

    Config m_Config = ConfigServiceHelper.getConfig("common");
    protected static Config registroConf = ConfigServiceHelper.getConfig("Registro");
 
    public ActionForward performSession(	ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws IOException {

        m_Log.debug("================= FichaExpedienteAction ======================>");
        
        //********************************************************************
        Calendar calendario = Calendar.getInstance();
        m_Log.debug("***TIEMPO -> incio: " + calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND) + ":" + calendario.get(Calendar.MILLISECOND));
        //********************************************************************
        
        HttpSession session = request.getSession();
        UsuarioValueObject usuarioVO = null;
        String[] params = null;
		//Informacion de usuario logado e id de session  para el log
		String usuarioActualLog = SessionInfo.getSessionUserLogin(session);
        usuarioActualLog=(usuarioActualLog!= null && usuarioActualLog!="")?usuarioActualLog.replace("Usuario logado: ", ""):"";
		String infoSesion = session.getId() + " " + usuarioActualLog;

        // Rellenamos el form de FichaExpedienteForm
        if (form == null) {
            m_Log.debug("Rellenamos el form de FichaExpedienteForm");
            form = new FichaExpedienteForm();
            if ("request".equals(mapping.getScope())) {
                request.setAttribute(mapping.getAttribute(), form);
            } else {
                session.setAttribute(mapping.getAttribute(), form);
            }
        }

        FichaExpedienteForm expForm = (FichaExpedienteForm) form;
        // Cogemos el usuario de la sesion
        if (session.getAttribute("usuario") != null) {
            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            params = usuarioVO.getParamsCon();
        }

        String opcion = request.getParameter("opcion");
        if (m_Log.isInfoEnabled()) {
            m_Log.info(infoSesion + " - La opcion en el action FichaExpediente es : " + opcion);
        }
        
        //#463434
        if("actualizarMarcaNotif".equals(opcion)){            
            String codOrganizacion = request.getParameter("organizacion");
            String numExpediente = request.getParameter("numExpediente");
            String checkMarca = request.getParameter("checkMarca");
            m_Log.info(infoSesion + " - La Organización en el action FichaExpediente es : " + codOrganizacion);
            m_Log.info(infoSesion + " - El nº expediente en el action FichaExpediente es : " + numExpediente);
            m_Log.info(infoSesion + " - El checkMarca en el action FichaExpediente es : " + checkMarca);
            String resultado = FichaExpedienteDAO.getInstance().expedienteEnMiCarpeta(params, numExpediente); 
            //expediente en Mi Carpeta
            if (resultado == "2") {
                resultado = FichaExpedienteDAO.getInstance().rolesCorrectos(params, numExpediente);
                //roles correctos
                if (resultado == "6") {
                    resultado = FichaExpedienteDAO.getInstance().actualizarMarcaNotificacion(params, numExpediente, checkMarca);
                    if (resultado == "4" || resultado == "5") {
                        //actualizarInteresados
                        try {
                            // actualizaInteresados() del MELANBIDE43
                            m_Log.info("Actualizar interesados (MELANBIDE43) desde FichaExpedienteAction");
                            final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide43.MELANBIDE43");
                            final Object me43Class = cls.newInstance();
                            final Class[] types = {int.class,int.class,int.class,String.class};
                            final Method method = cls.getMethod("actualizaInteresados", types);
                            String respuestaActualizaInteresados = (String) method.invoke(me43Class, Integer.parseInt(codOrganizacion), -1, -1, numExpediente);
                            m_Log.info("Despues de invoke metodo Actualizar interesados");
                            if (respuestaActualizaInteresados != null) {
                                m_Log.info("Respuesta de llamada a Actualizar interesados : " + respuestaActualizaInteresados);
                            }
                        } catch (Exception e) {
                            m_Log.error("Error al llamar a actualizaInteresados() desde FichaExpedienteAction " + e.getMessage(), e);
                            resultado = "0";
                        }
                    }
                }
            }
            response.setContentType("text/xml"); 
            PrintWriter out = response.getWriter();
            out.println(resultado);
            out.flush();
            out.close(); 
                    
        }
             
        if (("cargar".equals(opcion)) || ("cargarPestTram".equals(opcion))) {

            /************************* PRUEBA *****************/
            String filtroPendientes = request.getParameter("filtro");
            m_Log.debug("==========> CARGAR UN EXPEDIENTE. EL FILTRO DE PENDIENTES ES: " + filtroPendientes);
            
            if(filtroPendientes!=null && !"".equals(filtroPendientes)){
                session.setAttribute("filtro_pendientes",filtroPendientes);                
            }
           
           
            String lineas="";
            if("si".equals(request.getParameter("desdeConsulta")))  lineas=request.getParameter("numLineasPaginaListado");
            else  lineas=request.getParameter("numLineasPaginaListado");
            
          
            if(lineas!=null && !"".equals(lineas)){
                session.setAttribute("lineas_pendientes",lineas);                
            } 
                         
            String pagina="";
            if("si".equals(request.getParameter("desdeConsulta")))  pagina=request.getParameter("paginaListado");
            else  pagina=request.getParameter("paginaListado");
            
            if(pagina!=null && !"".equals(pagina)){
                session.setAttribute("pagina_pendientes",pagina);                
            }
            
            String columna =  request.getParameter("columna"); 
            if(columna!=null && !"".equals(columna)){
                session.setAttribute("columna_pendientes",columna);                
            }
            
             String  tipoOrden = request.getParameter("tipoOrden");
            if(tipoOrden!=null && !"".equals(tipoOrden)){
                session.setAttribute("tipoOrden_pendientes",tipoOrden);                
            }
             m_Log.debug("lineas " + lineas + " ,pagina " +pagina+" ,columna "+columna);
            /************************* PRUEBA *****************/

       
            session.setAttribute("desdeFichaExpediente", "si");
        //busco en que posisicion del select esta el campo por el que tengo que ordenar.
          
            //Actualizamos los documentos del portafirmas externo.
            String codOrganizacion = request.getParameter("codMunicipio");
            String numExpediente = request.getParameter("numero");
            String codUsuario = Integer.toString(usuarioVO.getIdUsuario());
        
            Boolean portafirmasExterno = PluginPortafirmasExternoClienteFactoria.
                    getExistePortafirmasExterno(codOrganizacion);
            
        
            if(portafirmasExterno){
                if(m_Log.isDebugEnabled()) m_Log.debug("Actualizamos los documentos de los tramites pendientes");
                PluginPortafirmasExternoCliente portafirmasExternoCliente = 
                        PluginPortafirmasExternoClienteFactoria.getImplClass(codOrganizacion);
                portafirmasExternoCliente.actualizarFirmasTramitacionExpediente(
                        codOrganizacion, numExpediente, codUsuario, params);
            }//if(portafirmasExterno)

            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", request.getParameter("codMunicipio"));
            gVO.setAtributo("codProcedimiento", request.getParameter("codProcedimiento"));
            gVO.setAtributo("ejercicio", request.getParameter("ejercicio"));
            String numero = request.getParameter("numero");

            // Se recupera la lista de roles del procedimiento para mostrarlo en la ficha del expediente
            GeneralValueObject gvRol = new GeneralValueObject();
            gvRol.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
            gvRol.setAtributo("codProcedimiento",request.getParameter("codProcedimiento"));
                    
            
            if (numero == null) {
                numero = request.getParameter("numeroExp");
            } // Adjuntar o cargar desde asiento.

            gVO.setAtributo("numero", numero);           
            
         
            String expHistorico = request.getParameter("expHistorico");
           
             
            String modoConsulta = request.getParameter("modoConsulta");
            String desdeAltaRE = request.getParameter("desdeAltaRE");
            String desdeConsulta = request.getParameter("desdeConsulta");
            String opcionExpRel = request.getParameter("opcionExpRel");
            String desdeExpRel = request.getParameter("desdeExpRel");
            if (modoConsulta == null) {
                modoConsulta = "no";
            }

            if (desdeAltaRE == null) {
                desdeAltaRE = "no";
            }
                     
            gVO.setAtributo("expHistorico", expHistorico);
            gVO.setAtributo("modoConsulta", modoConsulta);
            gVO.setAtributo("desdeConsulta", desdeConsulta);
            gVO.setAtributo("desdeAltaRE", desdeAltaRE);
            gVO.setAtributo("opcionExpRel", opcionExpRel);
            String expRelacionado = request.getParameter("expRelacionado");

            if (expRelacionado == null) {
                expRelacionado = "no";
            } else {
                String codMunicipioIni = request.getParameter("codMunicipioIni");
                String ejercicioIni = request.getParameter("ejercicioIni");
                String numeroIni = request.getParameter("numeroIni");
                gVO.setAtributo("codMunExpIni", codMunicipioIni);
                gVO.setAtributo("ejercicioExpIni", ejercicioIni);
                gVO.setAtributo("numeroExpIni", numeroIni);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("el expediente relacionado es : " + expRelacionado);
            }
            gVO.setAtributo("expRelacionado", expRelacionado);

            String deAdjuntar = request.getParameter("deAdjuntar");
            if (deAdjuntar == null) {
                deAdjuntar = "no";
            }

            gVO.setAtributo("deAdjuntar", deAdjuntar);
            String desdeInformesGestion = request.getParameter("desdeInformesGestion");

            if (desdeInformesGestion == null) {
                desdeInformesGestion = "no";
            }

            gVO.setAtributo("desdeInformesGestion", desdeInformesGestion);
            String todos = request.getParameter("todos");

            if (todos == null) {
                todos = "";
            }

            gVO.setAtributo("todos", todos);
            gVO.setAtributo("usuario", Integer.toString(usuarioVO.getIdUsuario()));
            gVO.setAtributo("codOrganizacion", Integer.toString(usuarioVO.getOrgCod()));
            gVO.setAtributo("codEntidad", Integer.toString(usuarioVO.getEntCod()));
            gVO.setAtributo("GrupoUsuario", Integer.toString(UsuarioManager.getInstance().getGrupo(usuarioVO)));

            
            UsuariosGruposValueObject usuarioGrupo = new UsuariosGruposValueObject ();
            usuarioGrupo.setCodOrganizacion(String.valueOf(usuarioVO.getOrgCod()));
            usuarioGrupo.setCodUsuario(String.valueOf(usuarioVO.getIdUsuario()));
        
            gVO.setAtributo("comprobarModuloConsultaExpediente","NO");
            // Se comprueba si la ficha del expediente se tiene que mostrar en modo tramitación, o en modo consulta
            if (opcion.equals("cargar") && desdeAltaRE.equals("no") && !"1".equals(opcionExpRel) && !"si".equals(desdeExpRel)) {            
                gVO.setAtributo("comprobarModuloConsultaExpediente","SI");
            }
           
            
           

            //********************************************************************
            calendario = Calendar.getInstance();
            m_Log.debug("***TIEMPO -> cargarExpediente: " + calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND) + ":" + calendario.get(Calendar.MILLISECOND));
            //********************************************************************                        
            
             if("cargar".equalsIgnoreCase(opcion)){
                
                CargaExpediente cargaExpediente = null;
                try {
                    ResourceBundle expPortafirmas = ResourceBundle.getBundle("Expediente");
                    String propiedad = usuarioVO.getOrgCod()+"/opcionCargarExpediente";
                    String className = expPortafirmas.getString(propiedad);
                     m_Log.debug("className: " + className);
                    if (className != null && !"".equals(className.trim())) {
                        Class clase = Class.forName(className);
                        cargaExpediente = (CargaExpediente) clase.newInstance();
                    } else {
                        m_Log.debug("No se ha indicado plugin del servicio a la hora de cargar el expediente");
                    }
                    
                }catch(Exception e){ 
                    m_Log.error("Se ha producido un error instanciando el plugin del servicio a la hora de cargar el expediente: "  + e.getMessage());
                }//try-catch
                
                if (cargaExpediente != null) {
                    try {
                       cargaExpediente.opcionCargarExpediente(request, usuarioVO, codOrganizacion, numExpediente);
                    } catch (Exception e) {
                        m_Log.error("Error a la hora de realizar la carga del expediente " + e.getMessage(), e);
                    }
                }
            }
            
            ArrayList<String> errores = cargarExpediente(expForm,gVO,params,usuarioVO);
            
            //********************************************************************
            calendario = Calendar.getInstance();
            m_Log.debug("***TIEMPO <- cargarExpediente: " + calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND) + ":" + calendario.get(Calendar.MILLISECOND));
            //********************************************************************            

            
            request.setAttribute("errores", errores);

            
           
            
            /**** ORIGINAL
            // Comprobar si se abre el expediente en modo consulta o tramitacion.
            if (opcion.equals("cargar") && desdeAltaRE.equals("no") && !"1".equals(opcionExpRel) && !"si".equals(desdeExpRel)) {
                ConsultaExpedientesManager consExpManager = ConsultaExpedientesManager.getInstance();
                ConsultaExpedientesValueObject consExpVO = new ConsultaExpedientesValueObject();
                consExpVO.setNumeroExpediente(numero);
                consExpVO.setExpHistorico("true".equals(expHistorico)?true:false);
                 
                boolean esPendiente = consExpManager.esPendienteParaUsuario(usuarioVO, consExpVO, params);
                boolean permiteModificarObs=consExpManager.permiteModificarObservacionesUsuario(usuarioVO, consExpVO, params);

                if (esPendiente) {
                        gVO.setAtributo("modoConsulta", "no");
                    }
                if ((!esPendiente)&&(permiteModificarObs)) {
                        gVO.setAtributo("permiteModificarObs", "si");
                    }
             }
             *****/
           

            if("si".equals(expRelacionado)){
                gVO.setAtributo("modoConsulta", "si");
                gVO.setAtributo("desdeConsulta", "no");
            }            
            if ("cargarPestTram".equals(opcion)) {
                opcion = "cargar";
                session.setAttribute("pestana", "tramites");
            }
            if(opcion.equals("cargar")){
                ResourceBundle propertiesExp = ResourceBundle.getBundle("Expediente");                                   
                //  Recuperamos de Expediente.properties la propiedad que indica si se muestra el check de notificaciones en solo lectura o no
                boolean esSoloLecturaCheck = false;
                try{
                    String notifModificableProp = propertiesExp.getString(Integer.toString(usuarioVO.getOrgCod()) + ConstantesDatos.BARRA + ConstantesDatos.NOTIFICACIONES_SOLO_LECTURA); 
                    if(notifModificableProp!=null && "SI".equalsIgnoreCase(notifModificableProp)){
                        esSoloLecturaCheck = true;
                    }
                }catch(Exception e){
                    m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + Integer.toString(usuarioVO.getOrgCod()) + ConstantesDatos.BARRA + ConstantesDatos.NOTIFICACIONES_SOLO_LECTURA + " EN Expediente.properties");
                    esSoloLecturaCheck = false;
                } finally{
                    expForm.setReadOnlyCheck(esSoloLecturaCheck);
                }
                //  Recuperamos de Expediente.properties la propiedad que indica si el check de notificaciones se muestra marcado por defecto o no
                boolean esMarcadoPDefCheck = false;
				String propiedadCheckActivo = Integer.toString(usuarioVO.getOrgCod()) + ConstantesDatos.BARRA + ConstantesDatos.CHECK_NOTIFICACIONES_ACTIVO;
                try{
                    String valorCheckActivo = propertiesExp.getString(propiedadCheckActivo); 
                    if(valorCheckActivo!=null && "SI".equalsIgnoreCase(valorCheckActivo)){
                        esMarcadoPDefCheck = true;
                    }
                }catch(Exception e){
                    m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + propiedadCheckActivo + " EN Expediente.properties");
                    esMarcadoPDefCheck = false;
                } finally{
                    expForm.setCheckNotifActivo(esMarcadoPDefCheck);
                }
                // #253692: Recuperamos propiedad que indica si se mostrará el campo de texto con la ubicación de la documentación
                boolean mostrarUbicacion = false;
                try{
                    String mostrarUbicacionProp = propertiesExp.getString(Integer.toString(usuarioVO.getOrgCod()) + ConstantesDatos.BARRA + ConstantesDatos.MOSTRAR_UBICACION_DOCUMENTACION); 
                    if(mostrarUbicacionProp!=null && "si".equalsIgnoreCase(mostrarUbicacionProp)){
                        mostrarUbicacion = true;
                    }
                }catch(Exception e){
                    m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + Integer.toString(usuarioVO.getOrgCod()) + ConstantesDatos.BARRA + ConstantesDatos.MOSTRAR_UBICACION_DOCUMENTACION + " EN Expediente.properties");
                    mostrarUbicacion = false;
                } finally{
                    expForm.setUbicacionDocumentacionVis(mostrarUbicacion);
                }
                
                try {
                    String valor = propertiesExp.getString(usuarioVO.getOrgCod()+"/MOSTRAR_MARCA_TRAMITE_NOTIFICADO");
                    if(valor!=null && !valor.equals("") && valor.equalsIgnoreCase("si")){
                        request.setAttribute("mostrarTramiteNotificado", valor);
                    }
                    m_Log.debug("Propiedad '" + usuarioVO.getOrgCod()+ "/MOSTRAR_MARCA_TRAMITE_NOTIFICADO' de Expediente.properties recuperada. Valor = " + valor);
                } catch (Exception ex){
                    m_Log.error("Error al recuperar la propiedad '" + usuarioVO.getOrgCod()+ "/MOSTRAR_MARCA_TRAMITE_NOTIFICADO' de Expediente.properties");
                }
            }


            

            /*************************COMPROBACIÓN DE DIRECTIVAS DE USUARIO***************************/
            /**
             * Se comprueba si el usuario tiene permiso sobre la directiva NO_TRAMITAR. Si la tiene, entonces
             * se carga la ficha del expediente en modoConsulta que deshabilita los botones de tramitación.
             * hay que mirar que no se consulta un expediente de una relaicón, en cuyo la directiva de ususario es indiferente.
             */            
            if (!"1".equals(opcionExpRel)) {
                //********************************************************************
                calendario = Calendar.getInstance();
                m_Log.debug("***TIEMPO OP -> UsuariosGruposManager.getInstance().tienePermisoDirectiva: " + calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND) + ":" + calendario.get(Calendar.MILLISECOND));
                //********************************************************************            

                if (UsuariosGruposManager.getInstance().tienePermisoDirectiva(ConstantesDatos.NO_TRAMITAR, usuarioVO.getIdUsuario(), params)) {
                    m_Log.debug("******* FichaExpedienteAction hay directiva de no tramitar nunca");
                    gVO.setAtributo("modoConsulta", "si");
                    gVO.setAtributo("permiteModificarObs", "no");

                } else {
                    m_Log.debug("******* FichaExpedienteAction NO hay directiva de no tramitar nunca.");
                }
                
                //********************************************************************
                calendario = Calendar.getInstance();
                m_Log.debug("***TIEMPO OP <- UsuariosGruposManager.getInstance().tienePermisoDirectiva: " + calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND) + ":" + calendario.get(Calendar.MILLISECOND));
                //********************************************************************            

            }

            /*En caso de venir desde una consulta, miramos si el ususario tiene activada o no la directiva que le
             impide trmaitar desde las consultas de expedientes.*/
            if (desdeConsulta!=null && "SI".equals(desdeConsulta.toUpperCase())) {
                
                //********************************************************************
                calendario = Calendar.getInstance();
                m_Log.debug("***TIEMPO OP -> UsuariosGruposManager.getInstance().tienePermisoDirectiva: " + calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND) + ":" + calendario.get(Calendar.MILLISECOND));
                //********************************************************************            
                
                if (UsuariosGruposManager.getInstance().tienePermisoDirectiva(ConstantesDatos.NO_TRAMITAR_MODOCONSULTA, usuarioVO.getIdUsuario(), params)) {
                    m_Log.debug("******* FichaExpedienteAction hay directiva de no tramitar en modo consulta");
                    gVO.setAtributo("modoConsulta", "si");
                    gVO.setAtributo("permiteModificarObs", "no");
                } else {
                    m_Log.debug("******* FichaExpedienteAction NO hay directiva de no tramitar en modo consulta");
                }
                
                //********************************************************************
                calendario = Calendar.getInstance();
                m_Log.debug("***TIEMPO OP <- UsuariosGruposManager.getInstance().tienePermisoDirectiva: " + calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND) + ":" + calendario.get(Calendar.MILLISECOND));
                //********************************************************************            

                
            } 
            
            // Comprobar si el usuario tiene permisos sobre la directiva NO_TRAMITAR_SOLO_MANT_OTROSDOC: no puede tramitar pero si mantener la pestana 'Otros documentos'
                
            //********************************************************************
            calendario = Calendar.getInstance();
            m_Log.debug("***TIEMPO OP -> UsuariosGruposManager.getInstance().tienePermisoDirectiva: " + calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND) + ":" + calendario.get(Calendar.MILLISECOND));
            //********************************************************************            

            if (UsuariosGruposManager.getInstance().tienePermisoDirectiva(ConstantesDatos.NO_TRAMITAR_SOLO_MANT_OTROSDOC, usuarioVO.getIdUsuario(), params)) {
                m_Log.debug("******* FichaExpedienteAction hay directiva de solo poder mantener la pestaña 'Otros documentos' en la tramitación");
                gVO.setAtributo("modoConsulta", "si");
                gVO.setAtributo("permiteModificarObs", "no");
                gVO.setAtributo("permiteMantenerDocExt", "si");
            } else {
                m_Log.debug("******* FichaExpedienteAction NO hay directiva de solo poder mantener la pestaña 'Otros documentos' en la tramitación");
            }

            //********************************************************************
            calendario = Calendar.getInstance();
            m_Log.debug("***TIEMPO OP <- UsuariosGruposManager.getInstance().tienePermisoDirectiva: " + calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND) + ":" + calendario.get(Calendar.MILLISECOND));
            //********************************************************************            

                
                      
         // Auditoria de accesos al expediente
            try {
                AuditoriaManager.getInstance().auditarAccesoExpediente(
                        ConstantesAuditoria.EXPEDIENTE_VER,
                        usuarioVO,
                        gVO);
            } catch (TramitacionException te) {
                m_Log.error("No se pudo registrar el evento de auditoria", te);
            }   
            
        //********************************************************************
        calendario = Calendar.getInstance();
        m_Log.debug("***TIEMPO fin : " + calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND) + ":" + calendario.get(Calendar.MILLISECOND));
        //********************************************************************            
            
            
            
            
        } else if ("calculo_expresion".equals(opcion)){            
            GeneralValueObject gVO = new GeneralValueObject();
            String codMunicipio_exp = request.getParameter("codMunicipio");
            String codProcedimiento_exp = request.getParameter("codProcedimiento");            
            String retorno = "";
            String retorno1 ="";
            String retorno2 ="";
            String retorno3 ="";
            
            gVO.setAtributo("desdeJsp", "si");
            gVO.setAtributo("codMunicipio", codMunicipio_exp);
            gVO.setAtributo("codProcedimiento", codProcedimiento_exp);
            
            Vector resultado =  calcular_campos_expresion(params, expForm, session, request, gVO);
            for (int i=0;i<resultado.size();i++)
            {
                GeneralValueObject g= (GeneralValueObject) resultado.elementAt(i); 
                retorno1= (String) g.getAtributo("cod_campo");
                retorno2= (String) g.getAtributo("valor");
                retorno3= (String) g.getAtributo("origen");                
                retorno = retorno + retorno1 + "|" + retorno2 +"|"+retorno3 + "#";  
                 m_Log.debug("Retorno "+i+" del campo calculado: "+retorno);
            }
            response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            out.println(retorno);
            out.flush();
            out.close();
        }else if("ChequearCamposCalculados".equals(opcion)){            
            TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
            UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");

            tramExpVO.setCodMunicipio(expForm.getCodMunicipio());
            tramExpVO.setCodProcedimiento(expForm.getCodProcedimiento());
            tramExpVO.setEjercicio(expForm.getEjercicio());
            tramExpVO.setNumeroExpediente(expForm.getNumExpediente());
            tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario())); 
            tramExpVO.setBloqueo("no");

            GeneralValueObject gVO = new GeneralValueObject();
            gVO = expForm.getExpedienteVO();
            
            String ChequearCamposExpresion = TramitacionExpedientesManager.getInstance().tratarCamposExpresion(tramExpVO,gVO,request,params);
            response.setContentType("text/xml"); 
            PrintWriter out = response.getWriter();
            out.println(ChequearCamposExpresion);
            out.flush();
            out.close(); 
                    
        }else if ("validaCamposCalculados".equals(opcion)){               
            GeneralValueObject gVO = new GeneralValueObject();
                                    
            gVO.setAtributo("codMunicipio", request.getParameter("codMunicipio"));
            gVO.setAtributo("codProcedimiento", request.getParameter("codProcedimiento"));
            gVO.setAtributo("ocurrencia",request.getParameter("ocurrencia"));
            gVO.setAtributo("tramite",request.getParameter("tramite"));
            gVO.setAtributo("numero",request.getParameter("numero"));

            
            String resultado = FichaExpedienteDAO.getInstance().validarCamposCal(params, gVO);             
            response.setContentType("text/xml"); 
            PrintWriter out = response.getWriter();
            out.println(resultado);
            out.flush();
            out.close();   
            
        }else if ("recuperarExpresion".equals(opcion)){  
             
            GeneralValueObject gVO = new GeneralValueObject();
                                    
            gVO.setAtributo("codMunicipio", request.getParameter("codMunicipio"));
            gVO.setAtributo("codProcedimiento", request.getParameter("codProcedimiento"));
            gVO.setAtributo("campo",request.getParameter("campo"));            

            
            String resultado = FichaExpedienteDAO.getInstance().recuperarExpresion(params, gVO);              
            response.setContentType("text/xml"); 
            PrintWriter out = response.getWriter();
            out.println(resultado);
            out.flush();
            out.close();
        }else if ("recuperarExterno".equals(opcion)){              
            GeneralValueObject gVO = new GeneralValueObject();
                                     
            gVO.setAtributo("codMunicipio", request.getParameter("codMunicipio"));
            gVO.setAtributo("codProcedimiento", request.getParameter("codProcedimiento"));
            gVO.setAtributo("campo",request.getParameter("campo"));        
            
            ArrayList<GeneralValueObject> resultados = new ArrayList<GeneralValueObject>();
            
            resultados = FichaExpedienteManager.getInstance().recuperarExternos(params,gVO);                        

            String codigos_externos="";
            String datos_externos="";
            String valores_externos="";
            
            if(resultados!=null)
            {
                 for(int i=0;i<resultados.size();i++)
                {
                    GeneralValueObject gVO1=new GeneralValueObject();
                    gVO1=resultados.get(i);
                    if ("".equals(codigos_externos))
                        codigos_externos = (String)gVO1.getAtributo("codigo");
                    else
                        codigos_externos = codigos_externos + "||" + (String) gVO1.getAtributo("codigo"); 

                }



                for(int i=0;i<resultados.size();i++)
                {
                    GeneralValueObject gVO1=new GeneralValueObject();
                    gVO1=resultados.get(i);
                    if ("".equals(datos_externos))
                        datos_externos = (String)gVO1.getAtributo("valor");
                    else
                        datos_externos = datos_externos + "||" + (String) gVO1.getAtributo("valor"); 

                }
                valores_externos=codigos_externos+"$$"+datos_externos;
                
            }                                                
            response.setContentType("text/xml; charset=ISO-8859-1"); 
            PrintWriter out = response.getWriter();
            out.println(valores_externos);         
            out.flush(); 
            out.close();
        }else if ("InteresadoOblig".equals(opcion)){              
            GeneralValueObject gVO = new GeneralValueObject();
                                     
            gVO.setAtributo("codMunicipio", request.getParameter("codMunicipio"));
            gVO.setAtributo("codProcedimiento", request.getParameter("codProcedimiento"));            
            
            String interesados = FichaExpedienteManager.getInstance().InteresadoObligatorio(params,gVO);                        

            response.setContentType("text/xml"); 
            PrintWriter out = response.getWriter();
            out.println(interesados);
            out.flush();
            out.close();
        }else if ("cargarEnVentana".equals(opcion)) { 
            GeneralValueObject gVO = new GeneralValueObject();
            String codMunicipio = request.getParameter("codMun"); 
            String codProcedimiento = request.getParameter("codProc"); 
            String ejercicio = request.getParameter("nCS"); 
            String numeroExpediente = request.getParameter("codTram"); 
            gVO.setAtributo("codMunicipio", codMunicipio);
            gVO.setAtributo("codProcedimiento", codProcedimiento);
            gVO.setAtributo("ejercicio", ejercicio);
            gVO.setAtributo("numero", numeroExpediente);
            String modoConsulta = "si";
            gVO.setAtributo("modoConsulta", modoConsulta);
            String expRelacionado = "si";
            gVO.setAtributo("expRelacionado", expRelacionado);
            String deAdjuntar = "si";
            gVO.setAtributo("deAdjuntar", deAdjuntar);
            gVO.setAtributo("usuario", Integer.toString(usuarioVO.getIdUsuario()));
            gVO.setAtributo("codOrganizacion", Integer.toString(usuarioVO.getOrgCod()));
            gVO.setAtributo("codEntidad", Integer.toString(usuarioVO.getEntCod()));                    
            ArrayList<String> errores = cargarExpediente(expForm, gVO, params, usuarioVO);
            request.setAttribute("errores", errores);
            opcion = "cargar";

        } else if (("iniciarExpediente".equals(opcion)) || ("iniciarExpedienteAsiento".equals(opcion))) {
            int res = 0;
            GeneralValueObject gVO = new GeneralValueObject();
            m_Log.debug("CODIGO DE MUNICIPIO: " + request.getParameter("codMunicipio"));
            gVO.setAtributo("codMunicipio", request.getParameter("codMunicipio"));
            gVO.setAtributo("codProcedimiento", request.getParameter("codProcedimiento"));
            gVO.setAtributo("ejercicio", request.getParameter("ejercicio"));
            gVO.setAtributo("usuario", Integer.toString(usuarioVO.getIdUsuario()));
            gVO.setAtributo("nomUsuario", usuarioVO.getNombreUsu());
            gVO.setAtributo("codOrganizacion", Integer.toString(usuarioVO.getOrgCod()));
            gVO.setAtributo("codEntidad", Integer.toString(usuarioVO.getEntCod()));
            gVO.setAtributo("codAplicacion", Integer.toString(usuarioVO.getAppCod()));
            gVO.setAtributo("codUOR", request.getParameter("uor"));
            gVO.setAtributo("codTercero", request.getParameter("codTerc"));
            gVO.setAtributo("codDomicilio", request.getParameter("codDomTerc"));
            gVO.setAtributo("version", request.getParameter("numModifTerc"));
            gVO.setAtributo("valorOpcionPermanencia", request.getParameter("valorOpcionPermanencia"));
            gVO.setAtributo("descProcedimiento", request.getParameter("procedimiento"));            
            gVO.setAtributo("unidadTramiteInicioSeleccionada", request.getParameter("unidadTramiteInicioSeleccionada"));
             gVO.setAtributo(ConstantesDatos.ORIGEN_LLAMADA_NOMBRE_PARAMETRO, ConstantesDatos.ORIGEN_LLAMADA_INTERFAZ_WEB);

            m_Log.info(infoSesion + " - Se inicia expediente de ejerc/proc = " + gVO.getAtributo("codProcedimiento") + "/" + gVO.getAtributo("ejercicio"));

            
            //TipoDocumentosManager tipoDocumManager = TipoDocumentosManager.getInstance();
            //Vector tiposDocs = tipoDocumManager.getListaTipoDocumentos(params);
            //expForm.setListaTiposDocumentos(tiposDocs);
            //expForm.setListaRoles(this.obtenerRolesProcedimiento(gVO, params));
            

            /*** INICIO: SE VACIAN LOS SIGUIENTES CAMPOS DEL FORMULARIO DE EXPEDIENTES **/
            session.removeAttribute("filtro_pendientes");            
            /*** FIN: SE VACIAN LOS SIGUIENTES CAMPOS DEL FORMULARIO DE EXPEDIENTES **/

            DefinicionTramitesValueObject defTramVO = new DefinicionTramitesValueObject();
            if ("iniciarExpedienteAsiento".equals(opcion)) {                
                gVO.setAtributo("numero", request.getParameter("numeroExp"));
                MantAnotacionRegistroForm registroForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
                RegistroValueObject elRegistroESVO = registroForm.getRegistro();
                gVO.setAtributo("codDepartamento", Integer.toString(elRegistroESVO.getIdentDepart()));
                gVO.setAtributo("codUnidadRegistro", Integer.toString(elRegistroESVO.getUnidadOrgan()));
                gVO.setAtributo("tipoAsiento", elRegistroESVO.getTipoReg());
                gVO.setAtributo("ejercicioAsiento", Integer.toString(elRegistroESVO.getAnoReg()));
                gVO.setAtributo("numeroAsiento", Long.toString(elRegistroESVO.getNumReg()));
                gVO.setAtributo("asunto", AdaptadorSQLBD.js_unescape(elRegistroESVO.getAsunto()));
                gVO.setAtributo("origenServicio", elRegistroESVO.getIdServicioOrigen());
                gVO.setAtributo("procedimientoAsiento", elRegistroESVO.getCodProcedimiento());
                gVO.setAtributo("munProcedimiento", elRegistroESVO.getMunProcedimiento());
                gVO.setAtributo("fechaAnotacion",elRegistroESVO.getFecEntrada());
                gVO.setAtributo("estAnotacion", Integer.toString(elRegistroESVO.getEstAnotacion()));
                
                // #303601: Recuperamos de base de datos PRO_EXPNUMANOT que indica si para este procedimiento los expedientes se numeran segun el anio del asiento
                String numeracionExp = "anoActual";
                try {
                    int codOrg = Integer.parseInt(request.getParameter("codMunicipio"));
                    boolean numeracionAsiento = FichaExpedienteManager.getInstance().expProcNumeracionAnhoAnotacion(codOrg, request.getParameter("codProcedimiento"), params);
                    if(numeracionAsiento) numeracionExp = "anoAsiento";
					m_Log.debug(infoSesion + " - El ejercicio que forma el nombre delexpediente se corresponde con: " + numeracionAsiento);
                } catch (BDException bdex){
                    bdex.printStackTrace();
                } catch (SQLException sqlex){
                    sqlex.printStackTrace();
                } catch (Exception ex){
                    m_Log.error("Error al parsear el parámetro de la request codMunicipio");
                    ex.printStackTrace();
                } 
                gVO.setAtributo("numeracionExpediente", numeracionExp);
                
                // #308045: Iniciar expediente desde un asiento del buzon de entrada se hace a través del plugin_re. Se debe comprobar si el servicio de digitalizacion
                // está activo. Se piensa en incluir en PLUGIN_REXTERNO un properties con esta propiedad, pero para evitar que se cambie en la lce y no en el plugin
                // se opta por obtener la propiedad en la lce y pasarsela a iniciarExpedienteAsiento del plugin como un atributo del objeto
                String servDigitalizacionAct = (String) session.getAttribute("servicioDigitalizacionActivo");
                m_Log.debug("antes de iniciarexpedienteasiento :: servDigitalizacionAct: " + servDigitalizacionAct);
                if(servDigitalizacionAct == null) {
                    try{
                        servDigitalizacionAct = registroConf.getString(usuarioVO.getOrgCod() + "/SERVICIO_DIGITALIZACION_ACTIVO");
                    }catch(Exception e){
                        m_Log.error("Se ha producido un error recuperando la propiedad '" + usuarioVO.getOrgCod() + "/SERVICIO_DIGITALIZACION_ACTIVO' de Registro.properties");
                    }
                }
                if(servDigitalizacionAct!=null && servDigitalizacionAct.equalsIgnoreCase("si")){             
                    gVO.setAtributo("servicioDigitalizacionActivo", servDigitalizacionAct);               
                } else {
                    gVO.setAtributo("servicioDigitalizacionActivo", "no");                
                }
                
                // #320123: Se necesita enviar al VO un atributo que indique si se trata de un registro telematico para en tal caso no retramitar el documento
                // usando el servicio de Adaptadores (DigitalizacionDocumentosLanbideManager.retramitarDocumentoInicioExpediente())
                gVO.setAtributo("esRegTelematico", elRegistroESVO.isRegistroTelematico());
                
                try {
                    VisorRegistroManager.getInstance().iniciarExpedienteAsiento(gVO, usuarioVO, params);
                    //  ini Obtener el plazo y el tipo e insertarlo en el gVO
                    defTramVO.setTxtCodigo((String) gVO.getAtributo("codProcedimiento"));
                    defTramVO.setCodigoTramite((String) gVO.getAtributo("codTramite"));
                    defTramVO = DefinicionTramitesManager.getInstance().getTramite(defTramVO, (String) gVO.getAtributo("codMunicipio"), params);
                    if (defTramVO.getPlazo() != null) {
                        if (!defTramVO.getPlazo().equals("")) {
                            gVO.setAtributo("plazo", defTramVO.getPlazo());
                            gVO.setAtributo("tipoPlazo", defTramVO.getUnidadesPlazo());
                    }
                        m_Log.debug("PLAZO       : " + gVO.getAtributo("plazo"));
                        m_Log.debug("TIPO PLAZO  : " + gVO.getAtributo("tipoPlazo"));
                    }
                    //  fin Obtener el plazo y el tipo e insertarlo en el gVO
                    if (notificar(usuarioVO, gVO)) {
                        gVO.setAtributo("notificacionRealizada", "si");
                    } else {
                        gVO.setAtributo("notificacionRealizada", "no");
                    }
                    gVO.setAtributo("tipoAlta", ConstantesDatos.TIPO_ALTA_EXP_ASIENTO);
                    OperacionesExpedienteManager.getInstance().registrarAltaExpediente(gVO,params);
                    
                    session.removeAttribute("MantAnotacionRegistroForm");
                    request.setAttribute("expedienteCreado", gVO.getAtributo("numero"));
                    

                    Vector valoresDatosSuplementariosTramite = new Vector();
                    Vector valoresDatosSuplementarios = new Vector();
                    Vector<EstructuraCampo> estructuraDatosSuplementariosTramiteAux = new Vector<EstructuraCampo>();
                    Vector<EstructuraCampo> estructuraDatosSuplementariosTramite = new Vector<EstructuraCampo>();
                    Vector estructuraDatosSuplementarios = new Vector(); 
                    EstructuraCampo eC = new EstructuraCampo();
                    GeneralValueObject generalVO = new GeneralValueObject();                    
                    RegistroValueObject documento=null;
                    List <GeneralValueObject> listaDatosSuplementarios = new ArrayList<GeneralValueObject>();
                    List <GeneralValueObject> listaDatosTramite = new ArrayList<GeneralValueObject>();
                    List <GeneralValueObject> listaInteresados = new ArrayList<GeneralValueObject>();
                    String nombreModulo = "";
                    List documentos = registroForm.getRegistro().getListaDocsAsignados();                
                    Vector estructuraDatosSuplementariosAux = FichaExpedienteManager.getInstance().cargaEstructuraDatosSuplementariosProcedimiento(gVO, params); 
                    int resSup=1;
                    int resInt=1;
                    byte[] fichero = null;

                    //Recorre la lista de documentos, quedandose solo con el documento indicado que incluye los datos suplementarios del expediente.
                    //Si por error envian varios documentos, quedara con el ultimo.
                    for( int i=0; i<documentos.size() ; i ++ ){
                        RegistroValueObject documentoAux = (RegistroValueObject)documentos.get(i);
                        if (documentoAux.getNombreDoc().equalsIgnoreCase(ConstantesDatos.NOMBRE_FICHERO_DATOS_INTEGRACION_SOLICITUD)){
                            //Recupera el contenido del documento mediante el plugin
                            int codOrganizacion = usuarioVO.getOrgCod();
                            int codUnidadRegistro = documentoAux.getUnidadOrgan();  
                            
                            //AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance().getImplClass(Integer.toString(codOrganizacion));
                            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(codOrganizacion)).getImplClassRegistro(Integer.toString(codOrganizacion));
                            
                            Hashtable<String,Object> datos = new Hashtable<String,Object>();
                            datos.put("identDepart",Integer.valueOf(documentoAux.getDptoUsuarioQRegistra()));
                            datos.put("unidadOrgan",new Integer(codUnidadRegistro));
                            datos.put("anoReg",new Integer(documentoAux.getAnoReg()));
                            datos.put("numReg",new Long(documentoAux.getNumReg()));
                            datos.put("tipoReg",documentoAux.getTipoReg());
                            datos.put("nombreDocumento",documentoAux.getNombreDoc());            
                            datos.put("documentoRegistro",new Boolean(true));
                            datos.put("params",params);
                            
                            int tipoDocumento = -1;
                            if(!almacen.isPluginGestor())
                                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                            else{
                                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                                datos.put("codMunicipio",Integer.toString(codOrganizacion));
                                datos.put("tipoMime",documentoAux.getTipoDoc());
                                datos.put("extension", MimeTypes.guessExtensionFromMimeType(documentoAux.getTipoDoc()));

                                if(almacen.isPluginGestor()){
                                    //  Si se trata de un plugin de un gestor documental, se pasa la información extra necesaria                                    
                                    ResourceBundle config = ResourceBundle.getBundle("documentos");                                   
                                    String carpetaRaiz  = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ); 
                                    
                                    Connection con = null;
                                    String descripcionOrganizacion = null;
                                    String descripcionUnidadRegistro = null;
                                    AdaptadorSQLBD adapt = null;
                                    try{
                                        adapt = new AdaptadorSQLBD(params);
                                        con = adapt.getConnection();

                                        descripcionOrganizacion   = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(codOrganizacion, con);                            
                                        UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],String.valueOf(codUnidadRegistro));
                                        if (uorDTO!=null)
                                            descripcionUnidadRegistro = uorDTO.getUor_nom();
                                    }catch(BDException e){
                                        if (m_Log.isErrorEnabled()) {m_Log.error("Error al recuperar una conexión a la BBDD: " + e.getMessage());}
                                    }finally{
                                        try{
                                            adapt.devolverConexion(con);
                                        }catch(BDException e){
                                            if (m_Log.isErrorEnabled()) {m_Log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());}
                                        }
                                    }

                                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                                    listaCarpetas.add(carpetaRaiz);
                                    listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
                                    listaCarpetas.add(codUnidadRegistro + ConstantesDatos.GUION + descripcionUnidadRegistro);

                                    if(documentoAux.getTipoReg().equalsIgnoreCase("E"))
                                        listaCarpetas.add(ConstantesDatos.DESCRIPCION_ENTRADAS_REGISTRO);
                                    else
                                    if(documentoAux.getTipoReg().equalsIgnoreCase("S"))
                                        listaCarpetas.add(ConstantesDatos.DESCRIPCION_SALIDAS_REGISTRO);    

                                    listaCarpetas.add(documentoAux.getAnoReg() + ConstantesDatos.GUION + documentoAux.getNumReg());
                                    datos.put("listaCarpetas",listaCarpetas);
                                 }                                    
                                
                            }
                            
                            try{
                               Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                               doc = almacen.getDocumentoRegistro(doc);
 
                               if(doc!=null && doc.getFichero()!=null && doc.getFichero().length>0){
                                   fichero = doc.getFichero();
                                   documentoAux.setDoc(fichero);
                               }
                            }catch(AlmacenDocumentoTramitacionException e){
                               e.printStackTrace();
                            }
                            
                            documento = documentoAux;
                        }
                    }
                    
                    if (documento != null){
                        
                        request.setAttribute("nombreDocumento", documento.getNombreDoc());
                        
                        //Recupera los datos suplementarios del documento xml
                        //listaDatosSuplementarios = leerDatosSuplementariosXML(documento,ConstantesDatos.TAG_XML_CAMPOSVARIABLES);
                        
                        Hashtable<String,List <GeneralValueObject>> camposSuplementarios = leerDatosSuplementariosXML(documento,ConstantesDatos.TAG_XML_CAMPOSVARIABLES);
                        listaDatosSuplementarios = camposSuplementarios.get("CAMPOS_EXPEDIENTE");
                        listaDatosTramite = camposSuplementarios.get("CAMPOS_TRAMITE");
                        
                                
                        //Recupera los datos de los interesados del documento xml
                        listaInteresados = leerInteresadosFicheroXML(documento, ConstantesDatos.TAG_XML_INTERESADOS, ConstantesDatos.TAG_XML_DOMICILIO, ConstantesDatos.TAG_XML_DATOSSUPLEMENTARIOS);
                        
                        //Recupera el nombre del modulo del documento xml
                        nombreModulo = leerAtributoFicheroXML(documento,ConstantesDatos.TAG_XML_FLX_EXTENSION,ConstantesDatos.TAG_XML_CODIGOMODULO,ConstantesDatos.ATRIBUTO_XML_COD);
                        
                        // Se recupera el número de expediente relacionado si es que ha sido comunicado                        
                        String numExpedienteRelacionado = leerValorNodoFicheroXML(documento,ConstantesDatos.TAG_XML_EXPEDIENTE_RELACIONADO);
                        
                        //if (listaDatosSuplementarios == null || listaInteresados == null || nombreModulo == null || "".equals(nombreModulo)){                            
                        if (listaDatosSuplementarios == null || listaInteresados == null){                            
                            if (m_Log.isErrorEnabled()) {m_Log.error("FichaExpedienteAction: Error el leer el fichero XML");}
                            request.setAttribute("errorLeerFichero", "Error leer fichero");
                        }else {
                           
                            
                            /*****************************************************************************************
                             ** SI DEL XML SE HAN RECUPERADO VALORES DE CAMPOS DE TRÁMITE, SE PROCEDE
                             ** A RECUPERAR LA ESTRUCTURA DE LOS CAMPOS SUPLEMENTARIOS DEL TRÁMITE DE INICIO,
                             ** Y A COMPONER LA LISTA DE VALORES PARA PROCEDER A SU GRABACIÓN
                             ** 
                             ** 
                             **/
                            if(listaDatosTramite.size()>0){                                
                                estructuraDatosSuplementariosTramiteAux = TramitacionExpedientesManager.getInstance().recuperarEstructuraCamposSuplementariosTramiteInicio(usuarioVO.getOrgCod(),(String)gVO.getAtributo("codTramite"),(String)gVO.getAtributo("codProcedimiento"),params);
                                boolean haiValores = false;
                                for ( int i = 0; i < estructuraDatosSuplementariosTramiteAux.size(); i++ ){
                                     eC = (EstructuraCampo) estructuraDatosSuplementariosTramiteAux.elementAt(i);
                                     for ( int j = 0; j < listaDatosTramite.size(); j++ ){
                                         generalVO = (GeneralValueObject) listaDatosTramite.get(j);
                                         String codCampo = (String) generalVO.getAtributo(ConstantesDatos.TAG_XML_CODIGO);
                                         String valorCampo = (String) generalVO.getAtributo(ConstantesDatos.TAG_XML_VALOR);
                                         if (eC.getCodCampo().equalsIgnoreCase(codCampo)){
                                            estructuraDatosSuplementariosTramite.add(eC);
                                            gVO.setAtributo(codCampo, valorCampo);
                                            haiValores = true;
                                            break;
                                         }
                                     }
                                 }
                                
                                if(estructuraDatosSuplementariosTramite.size()>0 && haiValores){
                                    TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
                                    String numExpediente = (String)gVO.getAtributo("numero");
                                    String codTramite    = (String)gVO.getAtributo("codTramite");
                                    String ocurrenciaTramite = (String)gVO.getAtributo("ocurrenciaTramite");
                                    
                                    String[] datosExp = numExpediente.split("/");
                                    String ejercicio        = datosExp[0];
                                    String codProcedimiento = datosExp[1];
                                    
                                    tEVO.setCodProcedimiento(codProcedimiento);
                                    tEVO.setCodTramite(codTramite);
                                    tEVO.setOcurrenciaTramite(ocurrenciaTramite);
                                    tEVO.setNumeroExpediente(numExpediente);
                                    tEVO.setEjercicio(ejercicio);
                                    tEVO.setCodMunicipio(Integer.toString(usuarioVO.getOrgCod()));                                    
                                    resSup = DatosSuplementariosManager.getInstance().grabarDatosSuplementariosTramite(estructuraDatosSuplementariosTramite, gVO, params,tEVO);                                                                        
                                    if (resSup == 1)
                                        resSup = DatosSuplementariosManager.getInstance().grabarDatosSuplementariosFicheroTramite(estructuraDatosSuplementariosTramite, valoresDatosSuplementariosTramite, params,tEVO);
                                }
                            } 
                               
                                    
                            /***************************************/        
                            //Guarda los datos suplementarios 
                           if (listaDatosSuplementarios.size() > 0){
                                //Filtra de la estructuta de datos suplementarios los que viene en el documento
                                for ( int i = 0; i < estructuraDatosSuplementariosAux.size(); i++ ){
                                     eC = (EstructuraCampo) estructuraDatosSuplementariosAux.elementAt(i);
                                     for ( int j = 0; j < listaDatosSuplementarios.size(); j++ ){
                                         generalVO = (GeneralValueObject) listaDatosSuplementarios.get(j);
                                         String codCampo = (String) generalVO.getAtributo(ConstantesDatos.TAG_XML_CODIGO);
                                         String valorCampo = (String) generalVO.getAtributo(ConstantesDatos.TAG_XML_VALOR);
                                        if (eC.getCodCampo().equalsIgnoreCase(codCampo)){
                                            estructuraDatosSuplementarios.add(eC);
                                            gVO.setAtributo(codCampo, valorCampo);
                                            valoresDatosSuplementarios.add(gVO);
                                        }
                                     }
                                 }
                                
                                if (estructuraDatosSuplementarios.size() >0 && valoresDatosSuplementarios.size() > 0){
                                   resSup = DatosSuplementariosManager.getInstance().grabarDatosSuplementarios(estructuraDatosSuplementarios, valoresDatosSuplementarios, params);
                                   if (resSup == 1) resSup =  DatosSuplementariosManager.getInstance().grabarDatosSuplementariosFichero(estructuraDatosSuplementarios, valoresDatosSuplementarios, params);
                                }
                                
                                
                                //resSup = DatosSuplementariosManager.getInstance().grabarDatosSuplementarios(estructuraDatosSuplementarios, valoresDatosSuplementarios, params);
                            }
                            if (resSup == 0){
                                if(m_Log.isErrorEnabled()) {m_Log.error("FichaExpedienteAction: Error el guardar los datos suplementarios del fichero");}
                                request.setAttribute("errorGuardarDatosSupFichero", "Error guardar datos suplementarios fichero");
                            }  
                     
                            if (listaInteresados.size() > 0){
                                //Actualiza los datos de los interesados
                                resInt = FichaExpedienteManager.getInstance().actualizarExpedienteAsiento(listaInteresados, gVO,numExpedienteRelacionado, params);
                                switch (resInt){
                                    case 0:
                                         if(m_Log.isErrorEnabled()) {m_Log.error("FichaExpedienteAction: Error al guardar los interesados del fichero");}                                        
                                         request.setAttribute("errorGuardarInteresadosFichero", "Error guardar interesados fichero");
                                          break;
                                    case 2:
                                         if(m_Log.isErrorEnabled()) {m_Log.error("FichaExpedienteAction: Error al guardar los interesados del fichero. Faltan campos obligatorios");}                                            
                                         request.setAttribute("errorGuardarInteresadosCampOblig", "Error guardar interesados campos obligatorios");
                                          break;
                                        
                                    case 3:
                                         if(m_Log.isErrorEnabled()) {m_Log.error("FichaExpedienteAction: Error al guardar los interesados del fichero. El domicilio de algún interesado está incompleto, tienen que enviar la provincia, municipio, nombre vía y/o emplazamiento");}                                            
                                         request.setAttribute("errorGuardarDomicilioInteresadosIncompleto", "El domicilio de algunos de los interesados está incompleto");
                                         break;

                                    case 4:
                                         if(m_Log.isErrorEnabled()) {m_Log.error("FichaExpedienteAction: Error al guardar los interesados del fichero. No existe el interesado asociado a la anotación");}                                            
                                         request.setAttribute("errorNoExisteInteresadoAsociadoAnotacion", "No existe el interesado asociado a la notación");
                                         break;

                                    case 5:
                                         if(m_Log.isErrorEnabled()) {m_Log.error("FichaExpedienteAction: Error al guardar los interesados del fichero. No se ha podido actualizar el campo notificación electronica del interesado asociado a la anotación");}                                            
                                         request.setAttribute("errorExpedienteRelacionadoNoExiste", "El domicilio de algunos de los interesados está incompleto");
                                         break;
                                        
                                    case 6:
                                         if(m_Log.isErrorEnabled()) {m_Log.error("FichaExpedienteAction: Error: el expediente " + numExpedienteRelacionado + " no existe y no se puede relacionar con el expediente recién iniciado");}                                            
                                         request.setAttribute("errorExpedienteRelacionadoNoExiste", "Error: el expediente " + numExpedienteRelacionado + " no existe y no se puede relacionar con el expediente recién iniciado");
                                         request.setAttribute("expedienteRelacionado",numExpedienteRelacionado);
                                         break;                                        
                                        
                                    case 7:
                                         if(m_Log.isErrorEnabled()) {m_Log.error("FichaExpedienteAction: Error: el expediente " + numExpedienteRelacionado + " no existe y no se puede relacionar con el expediente recién iniciado");}                                            
                                         request.setAttribute("errorRelacionarExpedienteIniciado", "Error: se ha producido un error técnico al relacionar el expediente " + numExpedienteRelacionado + " con el expediente recién iniciado");
                                         request.setAttribute("expedienteRelacionado",numExpedienteRelacionado);
                                         break;                                                                                
                                        
                                        
                                    default: break;
                                }  
                            }
                    
                            if (nombreModulo.length()>0){
                                ModuloIntegracionExternoFactoria factoria = ModuloIntegracionExternoFactoria.getInstance();
                                ModuloIntegracionExterno modulo = factoria.getImplClass(usuarioVO.getOrgCod(), nombreModulo);                                             
                                if(modulo!=null){
                                    String value = new String(documento.getDoc(), "ISO-8859-1");
                                    try{
                                        modulo.cargarExpedienteExtension(usuarioVO.getOrgCod(), (String)gVO.getAtributo("numero") , value); 
                                    }catch(Exception e){
                                        m_Log.error(" Error al ejecutar la operacion cargarExpedienteExtension del módulo " + modulo.getNombreModulo() + ": " + e.getMessage());
                                        e.printStackTrace();
                                        
                                        request.setAttribute("errorEjecucionExtension", e.getMessage());
                                        
                                    }
                                }else{
                                    if(m_Log.isErrorEnabled()) {m_Log.error("FichaExpedienteAction: Error al cargar los datos de extensión");}      
                                     request.setAttribute("errorCargarExtension", "Error cargar extension");
                                }                                    
                            }
                        }
                    }
                    
                    try{
                            Config m_ConfigExp = ConfigServiceHelper.getConfig("Expediente");
                            Boolean envioMailsInicioExpBuzonDuplicar =m_ConfigExp.getString(Integer.toString(usuarioVO.getOrgCod())  + "/envioMailInicioExpedienteBuzonDuplicar").toUpperCase().equals("SI");
                            if(envioMailsInicioExpBuzonDuplicar){ sendMail(request, gVO, params, defTramVO);}
                            
                    }catch(Exception e)
                    {
                             sendMail(request, gVO, params, defTramVO); 
                    }
                    
                    
                } catch (Exception e) {
                    request.setAttribute("error", e.getMessage());
                }
                return mapping.findForward("verNumero");

            } else {
                gVO.setAtributo("numero", request.getParameter("numero"));
                res = FichaExpedienteManager.getInstance().iniciarExpediente(gVO, params);
                if (res > 0) {
                    //  ini Obteber el plazo y el tipo e insertarlo en el gVO
                    defTramVO.setTxtCodigo((String) gVO.getAtributo("codProcedimiento"));
                    defTramVO.setCodigoTramite((String) gVO.getAtributo("codTramite"));
                    defTramVO = DefinicionTramitesManager.getInstance().getTramite(defTramVO, (String) gVO.getAtributo("codMunicipio"), params);
                    if (defTramVO.getPlazo() != null) {
                        if (!defTramVO.getPlazo().equals("")) {
                            gVO.setAtributo("plazo", defTramVO.getPlazo());
                            gVO.setAtributo("tipoPlazo", defTramVO.getUnidadesPlazo());
                        }
                        m_Log.debug("PLAZO       : " + gVO.getAtributo("plazo"));
                        m_Log.debug("TIPO PLAZO  : " + gVO.getAtributo("tipoPlazo"));
                    }
                    //  fin Obteber el plazo y el tipo e insertarlo en el gVO
                    if (notificar(usuarioVO, gVO)) {
                        gVO.setAtributo("notificacionRealizada", "si");
                    } else {
                        gVO.setAtributo("notificacionRealizada", "no");
                    }
                    
                    gVO.setAtributo("tipoAlta", ConstantesDatos.TIPO_ALTA_EXP_NORMAL);
                    OperacionesExpedienteManager.getInstance().registrarAltaExpediente(gVO,params);  
                }
            }
 
            if (res >= 0) {
                /* Miramos si tiene exp relacionados, y en base a eso mostraremos el botón
                 * de exp relacionados de un color u otro
                 */
                String codMunicipio1 = request.getParameter("codMunicipio");
//                String ejercicio1 = request.getParameter("ejercicio");
//                String numero = request.getParameter("numeroExp");
                String ejercicio1 = (String)gVO.getAtributo("ejercicio");
                String numero = (String)gVO.getAtributo("numero");
                
                boolean expedientesRelacionados = FichaExpedienteManager.getInstance().tieneExpedientesRelacionados(codMunicipio1, ejercicio1, numero, params);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("--> Booleano expREl : " + expedientesRelacionados);
                }

                gVO.setAtributo("expedientesRelacionados", expedientesRelacionados);
                // 'desdeAsiento' se usa para saber que estamos iniciando un expediente desde un asiento
                gVO.setAtributo("desdeAsiento", "si");
                    cargarExpediente(expForm, gVO, params, usuarioVO);
                opcion = "cargar";
                try{
                            Config m_ConfigExp = ConfigServiceHelper.getConfig("Expediente");
                            Boolean envioMailsInicioExpBuzonDuplicar =m_ConfigExp.getString(Integer.toString(usuarioVO.getOrgCod())  + "/envioMailInicioExpedienteBuzonDuplicar").toUpperCase().equals("SI");
                            if(envioMailsInicioExpBuzonDuplicar) sendMail(request, gVO, params, defTramVO);
                            
                    }catch(Exception e)
                    {
                             sendMail(request, gVO, params, defTramVO); 
                    }
                

                if (res == 2) {
                    request.setAttribute("msgExpSinTercero", "si");
                }
                request.setAttribute("expedienteCreado", gVO.getAtributo("numero"));
            } else {
                m_Log.debug("FichaExpedienteAction: NO SE HA INICIADO EL EXPEDIENTE");
                request.setAttribute("error", "NO SE HA INICIADO EL EXPEDIENTE");
            }
            return mapping.findForward("verNumero");
        } else if ("consultaAsiento".equals(opcion)) {

            if (m_Log.isDebugEnabled()){m_Log.debug("FichaExpediente action -> Inicio ConsultaAsiento");}
            // Obteniendo datos de la request.
            String codDpto = request.getParameter("codDepartamento");
            String codUOR = request.getParameter("codUnidadRegistro");
            String tipoReg = request.getParameter("tipoRegistro");
            session.setAttribute("tipoAnotacion", tipoReg);
            String ejerNum = request.getParameter("ejerNum");
            StringTokenizer codigos = new StringTokenizer(ejerNum, "/", false);
            String ejercicio = codigos.nextToken();
            String numero = codigos.nextToken();
            String origenServicio = request.getParameter("origenServicio");
           
            // Incilializando VOs y las listas desplegables del asiento.
            RegistroValueObject elRegistroESVO = new RegistroValueObject();
            MantAnotacionRegistroForm registroForm = new MantAnotacionRegistroForm();
            inicializarListas(elRegistroESVO, params);
            
            elRegistroESVO.setMunProcedimiento(expForm.getCodMunicipio());
            elRegistroESVO.setCodProcedimiento(expForm.getCodProcedimiento());
            elRegistroESVO.setAnoReg(Integer.parseInt(expForm.getEjercicio()));
            elRegistroESVO.setNumExpediente(expForm.getNumExpediente());
           
            
            // Rellenado el VO del asiento con los datos de la request correspondientes.
            elRegistroESVO.setIdentDepart(Integer.parseInt(codDpto));
            elRegistroESVO.setUnidadOrgan(Integer.parseInt(codUOR));
            elRegistroESVO.setTipoReg(tipoReg);
            elRegistroESVO.setNumReg(Long.valueOf(numero).longValue());
            elRegistroESVO.setAnoReg(Integer.parseInt(ejercicio));
            elRegistroESVO.setIdServicioOrigen(origenServicio);
            elRegistroESVO.setEstAnotacion(1); //Aceptada

            // Llamado al manager para realizar la consulta.
            elRegistroESVO = VisorRegistroManager.getInstance().getInfoAsientoConsulta(elRegistroESVO, params);

            registroForm.setTipoRegistro(tipoReg);
            registroForm.setCbTipoDoc(elRegistroESVO.getTipoDocInteresado());
            registroForm.setTxtDNI(elRegistroESVO.getDocumentoInteresado());
            registroForm.setTxtInteresado(elRegistroESVO.getNomCompletoInteresado());
            registroForm.setTxtTelefono(elRegistroESVO.getTlfInteresado());
            registroForm.setTxtCorreo(elRegistroESVO.getEmailInteresado());
            registroForm.setTxtProv((elRegistroESVO.getProvInteresado() != null) ? elRegistroESVO.getProvInteresado() : "");
            registroForm.setTxtMuni((elRegistroESVO.getMunInteresado() != null) ? elRegistroESVO.getMunInteresado() : "");
            registroForm.setTxtDomicilio((elRegistroESVO.getDomCompletoInteresado() != null) ? elRegistroESVO.getDomCompletoInteresado() : "");
            registroForm.setTxtPoblacion((elRegistroESVO.getPoblInteresado() != null) ? elRegistroESVO.getPoblInteresado() : "");
            registroForm.setTxtCP((elRegistroESVO.getCpInteresado() != null) ? elRegistroESVO.getCpInteresado() : "");
            registroForm.setRegistroTelematico(elRegistroESVO.isRegistroTelematico());

            TipoDocumentosManager tipoDocumManager = TipoDocumentosManager.getInstance();
            if (m_Log.isDebugEnabled()){m_Log.debug("FichaExpediente action -> Obteniendo lista de documentos...");}
            Vector tiposDocs = tipoDocumManager.getListaTipoDocumentos(params);
            expForm.setListaTiposDocumentos(tiposDocs);
            
            if (m_Log.isDebugEnabled()){m_Log.debug("FichaExpediente action -> Obteniendo lista de intersados...");}
            // Carga de interesados de la anotacion
            Vector vInteresados = getListaInteresados(elRegistroESVO, params);
            if (vInteresados.isEmpty()){
                //Si no tiene registro de flexia y no hay interesados se buscan en el registro telematico externo
                try {
                    if (m_Log.isDebugEnabled()){m_Log.debug("FichaExpediente action -> Obteniendo lista de interesados del RT externo...");}
                    vInteresados = VisorRegistroManager.getInstance().getInteresados(elRegistroESVO, params);
                } catch (RegistroException ex) {
                    m_Log.error("FichaExpedienteAction: Error buscando interesados : " + ex.getMensaje());
                }
            }
            registroForm.setListaInteresados(vInteresados);
    
            // Carga de roles
            if (m_Log.isDebugEnabled()){m_Log.debug("FichaExpediente action -> Obteniendo lista de roles...");}
            registroForm.setListaRoles(getListaRoles(elRegistroESVO, params));
            registroForm.setRegistro(elRegistroESVO);

            if(registroForm.getRelaciones()==null){        
                if (m_Log.isDebugEnabled()){m_Log.debug("FichaExpediente action -> Obteniendo lista de relaciones...");}
                registroForm.setRelaciones(new Vector<SimpleRegistroValueObject>());
            }
            
            // Guardando los resultados en la sesion para visualizarlos.
            session.setAttribute("modoInicio", "soloConsulta");
            session.setAttribute("MantAnotacionRegistroForm", registroForm);
            // #291976: añadimos a la sesion la propiedad SERVICIO_DIGITALIZACION_ACTIVO de Registro.properties si no se ha añadido
            String servDigitalizacionAct = (String) session.getAttribute("servicioDigitalizacionActivo");
            if(servDigitalizacionAct == null) {
                try{
                    servDigitalizacionAct = registroConf.getString(usuarioVO.getOrgCod() + "/SERVICIO_DIGITALIZACION_ACTIVO");
                }catch(Exception e){
                    m_Log.error("Se ha producido un error recuperando la propiedad '" + usuarioVO.getOrgCod() + "/SERVICIO_DIGITALIZACION_ACTIVO' de Registro.properties");
                }
                if(servDigitalizacionAct!=null && servDigitalizacionAct.equalsIgnoreCase("si")){
                    session.setAttribute("servicioDigitalizacionActivo", servDigitalizacionAct);               
                }
            }
            
            // Auditoria de acceso al registro
            try {
                AuditoriaManager.getInstance().auditarAccesoAnotacion(
                        ConstantesAuditoria.EXPEDIENTE_VER_ANOTACION_DESDE_EXPEDIENTE,
                        usuarioVO, elRegistroESVO);
            } catch (TramitacionException te) {
                m_Log.error("No se pudo registrar el evento de auditoria", te);
            }
            if (m_Log.isDebugEnabled()){m_Log.debug("FichaExpediente action -> Redirigiendo...");}
            opcion = "consultaAsiento";

        } else if ("permisoConsultarAsiento".equals(opcion)) {
             
           String codigoUnidadDestino = request.getParameter("codigoUnidadDestino");
           String permiso="SI";

            
            String directiva_salidas_uor_usuario="NO";
           
            if (UsuariosGruposManager.getInstance().tienePermisoDirectiva(ConstantesDatos.REGISTRO_S_SOLO_UORS_USUARIO, usuarioVO.getIdUsuario(), params)) {
                m_Log.debug("******* MantAnotacionRegistroAction hay directiva de usuario que solo puede acceder a uors a las que tenga permiso");
                directiva_salidas_uor_usuario = "SI";

            }
            
            Vector resultado = new Vector();
            
            if("SI".equals(directiva_salidas_uor_usuario)){
                resultado = UORsDAO.getInstance().getListaUORsPorCodigoPermisoUsuario(Integer.parseInt(codigoUnidadDestino),usuarioVO,params);
                if (resultado.isEmpty()){
                   permiso="NO" ;
                   
                }
            } 
            
            response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            out.println("PERMISO=" + permiso);
            out.flush();
            out.close();
            

        }else if ("actualizarDocumentos".equals(opcion)) {
            GeneralValueObject gVO = expForm.getExpedienteVO();

            Vector docs = (Vector) expForm.getDocumentos();
            for (int i = 0; i < docs.size(); i++) {
                GeneralValueObject dVO = (GeneralValueObject) docs.elementAt(i);
                String entregado = request.getParameter("documentoEntregado" + i);
                if (entregado == null) {
                    dVO.setAtributo("ENTREGADO", "NO");
                } else {
                    dVO.setAtributo("ENTREGADO", "SI");
                }
            }
            gVO.setAtributo("listaDocumentos", docs);
            int num = FichaExpedienteManager.getInstance().actualizaListaDocumentosExpediente(gVO, params);
            if (num > 0) {
                expForm.setDocumentos(docs);
            }

        } else if ("consultaComunicacion".equals(opcion)){
            //INICIO #72851 Cargar datos de una comunicación
            
            //Cogemos de la url el id de la comunicacion
            Long idCom = Long.parseLong(request.getParameter("idCom"));
            
            
            //Cargamos los datos de la comunicacion
            if (idCom!= null && !idCom.equals("")){
                cargarDatosComunicacion(idCom,params,expForm);
            
                //Hacemos el return correspondiente
                return mapping.findForward("verComunicacion");
            }
        
        }else if ("iniciar_tramitacion_manual".equals(opcion)) {
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", request.getParameter("codMun"));
            gVO.setAtributo("codProcedimiento", request.getParameter("codProc"));
            gVO.setAtributo("ejercicio", request.getParameter("eje"));
            gVO.setAtributo("usuario", Integer.toString(usuarioVO.getIdUsuario()));
            gVO.setAtributo("codOrganizacion", Integer.toString(usuarioVO.getOrgCod()));
            gVO.setAtributo("codEntidad", Integer.toString(usuarioVO.getEntCod()));
            //gVO.setAtributo("codUOR",Integer.toString(usuarioVO.getUnidadOrgCod()));
            gVO.setAtributo("numero", request.getParameter("num"));
            cargarTramitesDisponibles(expForm, gVO, params);

        } else 
        if("verificar_retroceso_expediente_permitido".equals(opcion)){
            /** PUEDE HABER MÓDULOS EXTERNOS QUE NO PERMITEN RETROCEDER UN EXPEDIENTE COMO EL MÓDULO DE INTEGRACIÓN FLEXIA-MERCURIO ***/
            /** SE COMPRUEBA SI EL PROCEDIMIENTO AL QUE PERTENECE EL EXPEDIENTE TIENE ALGUNA OPERACIÓN DE MODULOS ASOCIADAS A SUS TRÁMITES, Y EN ESE 
             *  CASO, SE COMPRUEBA SI EL MÓDULO ESTÁ CARGADO Y SI PERMITE RETROCEDER EL EXPEDIENTE
             */
            String codProcedimiento = request.getParameter("codProcedimiento");
            String permitir = "SI";
            if(codProcedimiento!=null && !"".equals(codProcedimiento)){
                ArrayList<String> modulos = DefinicionOperacionesSWManager.getInstance().getModulos(codProcedimiento, usuarioVO.getParamsCon());
                // Entre los posibles módulos que tengan alguna operación asignada a algún trámite
                for(int i=0;i<modulos.size();i++){

                    try{
                        ResourceBundle FILE_CONFIG_MODULO = ResourceBundle.getBundle(modulos.get(i));
                        if(FILE_CONFIG_MODULO!=null){

                            String valor = FILE_CONFIG_MODULO.getString(usuarioVO.getOrgCod() + ConstantesDatos.MODULO_INTEGRACION + modulos.get(i) + ConstantesDatos.BARRA
                                    + codProcedimiento + "/RETROCESO/DESACTIVAR");
                            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                                permitir ="NO";
                                break;
                            }
                        }
                    }catch(Exception e){ 
                        e.printStackTrace();
                        permitir = "SI";
                    }
 
                }// for                 
            }// if
            
            
            response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            out.println("PERMITIR=" + permitir);
            out.flush();
            out.close();

            
        }
        
        
        if ("verificar_permisoTramite".equals(opcion)) {


            AdaptadorSQLBD adapt = null;
            Connection con = null;

            String permitir = "SI";

            try {
                adapt = new AdaptadorSQLBD(params);
                con = adapt.getConnection();
                m_Log.debug("******* FichaExpedienteAction verificar_permisoTramite");
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codMunicipio", request.getParameter("codMunicipio"));
                gVO.setAtributo("codProcedimiento", request.getParameter("codProcedimiento"));
                gVO.setAtributo("ejercicio", request.getParameter("ejercicio"));
                gVO.setAtributo("usuario", Integer.toString(usuarioVO.getIdUsuario()));
                gVO.setAtributo("codOrganizacion", Integer.toString(usuarioVO.getOrgCod()));
                gVO.setAtributo("codEntidad", Integer.toString(usuarioVO.getEntCod()));
                gVO.setAtributo("numero", request.getParameter("numExpediente"));
                gVO.setAtributo("codTramite", request.getParameter("codTramite"));
                gVO.setAtributo("ocurrenciaTramite", request.getParameter("ocurrenciaTramite"));
                gVO.setAtributo("expHistorico", request.getParameter("expHistorico"));
 
 
                permitir = FichaExpedienteDAO.getInstance().verificarPermisosTramite(gVO, adapt, con);
                m_Log.debug("resultado verificarPermisosTramite: " + permitir);

            } catch (Exception e) {
                e.printStackTrace();


            } finally {
                try { 
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    if (m_Log.isErrorEnabled()) {
                        m_Log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
                    }
                }
            }
            response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            out.println(permitir);
            out.flush();
            out.close();



        } else if(("verificar_tramite_pendiente_firma").equals(opcion)){
            m_Log.debug("verificar_tramite_pendiente_firma");
            
            TramitacionExpedientesValueObject traExpVO = new TramitacionExpedientesValueObject();
            traExpVO.setNumeroExpediente(request.getParameter("numero"));
            traExpVO.setEjercicio(request.getParameter("ejercicio"));
            traExpVO.setCodTramite(request.getParameter("codTramite"));
            traExpVO.setOcurrenciaTramite(request.getParameter("ocuTramite"));
            traExpVO.setCodMunicipio(request.getParameter("codMunicipio"));
            traExpVO.setCodProcedimiento(request.getParameter("codProcedimiento"));
            
            boolean tieneDocPendienteFirma = FichaExpedienteManager.getInstance().documentosTramitePendienteFirma(traExpVO, params);
            retornarJSON(new Gson().toJson(tieneDocPendienteFirma), response);
            return null;
        }
        else if ("retroceder_expediente".equals(opcion)) {
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", request.getParameter("codMun"));
            gVO.setAtributo("codProcedimiento", request.getParameter("codProc"));
            gVO.setAtributo("ejercicio", request.getParameter("eje"));
            gVO.setAtributo("usuario", Integer.toString(usuarioVO.getIdUsuario()));
            gVO.setAtributo("nomUsuario", usuarioVO.getNombreUsu());
            gVO.setAtributo("codOrganizacion", Integer.toString(usuarioVO.getOrgCod()));
            gVO.setAtributo("codEntidad", Integer.toString(usuarioVO.getEntCod()));
            gVO.setAtributo("numero", request.getParameter("num"));
            gVO.setAtributo("codTramiteRetroceder", request.getParameter("codTramiteRetroceder"));
            gVO.setAtributo("tramiteRetroceder", request.getParameter("tramiteRetroceder"));
            gVO.setAtributo("ocurrenciaTramiteRetroceder", request.getParameter("ocurrenciaTramiteRetroceder"));
            gVO.setAtributo("fechaInicioTramiteRetroceder", request.getParameter("fechaInicioTramiteRetroceder"));
            gVO.setAtributo("fechaInicio", request.getParameter("fechaInicio"));
            gVO.setAtributo("codigoIdiomaUsuario",Integer.toString(usuarioVO.getIdioma()));
            gVO.setAtributo(ConstantesDatos.ORIGEN_LLAMADA_NOMBRE_PARAMETRO, ConstantesDatos.ORIGEN_LLAMADA_INTERFAZ_WEB);
            boolean mensajeNoFinalizado = false; //flag para mostrar msg

            // Comprobamos que el tramite no tenga formularios asociados ni documentos firmados digitalmente
            boolean tieneFormularios = tieneFormularios(gVO);
            boolean tieneDocumentosFirmados = TramitacionExpedientesManager.getInstance().tieneDocumentosFirmados(gVO, params);
            
            boolean tramiteNoRetrocedido = false;
            RespuestaRetrocesoTramiteVO respuesta = null;
            // Se vacía la lista de trámites destino
            expForm.setTramitesDestino(new ArrayList<TramitacionExpedientesValueObject>());
            if(!tieneFormularios && !tieneDocumentosFirmados)
            {
                try {
                    //FichaExpedienteManager.getInstance().retrocederExpediente(gVO, params);

                    respuesta = FichaExpedienteManager.getInstance().retrocederExpedienteMetodoAtrasNuevo(gVO, params);
                    if(respuesta!=null){
                        m_Log.debug("respuesta del restroceso de un expediente :" +respuesta.getTipoRespuesta());
                        String tipoRespuesta = respuesta.getTipoRespuesta();
                        
                        // Se registra la operacion si se ha retrocedido algun tramite
                        if (!ConstantesDatos.EXPEDIENTE_FINALIZADO_NO_RETROCEDER.equals(tipoRespuesta) &&
                            !ConstantesDatos.TRAMITE_FINALIZADO_CON_TRAMITES_POSTERIORES.equals(tipoRespuesta) && !ConstantesDatos.TRAMITE_REABRIR.equals(tipoRespuesta)) {
                            
                            OperacionesExpedienteManager.getInstance().registrarRetrocederTramite(gVO, params);
                        }
                        
                        if(ConstantesDatos.TRAMITE_FINALIZADO_CON_TRAMITES_POSTERIORES.equals(tipoRespuesta) ||
                           ConstantesDatos.EXPEDIENTE_FINALIZADO_NO_RETROCEDER.equals(tipoRespuesta) ||
                           ConstantesDatos.TRAMITE_ORIGEN_CON_TRAMITES_ABIERTOS.equals(tipoRespuesta)){
                            tramiteNoRetrocedido = true;
                            expForm.setTramitesDestino(respuesta.getTramitesDestino());
                        } else if (!ConstantesDatos.TRAMITE_RETROCESO_DE_ESTADO_FINALIZADO.equals(tipoRespuesta) && !ConstantesDatos.TRAMITE_REABRIR.equals(tipoRespuesta)){ // Se ha retrocedido
                            expForm.setRespOpcion("");
                            
                            // Se registra la reapertura del tramite origen
                            gVO.setAtributo("codTramiteOrigenReabrir", Integer.toString(respuesta.getCodTramiteOrigenReabrir()));
                            gVO.setAtributo("ocurrenciaTramiteOrigenReabrir", Integer.toString(respuesta.getOcurrenciaTramiteOrigenReabrir()));
                            gVO.setAtributo("nomTramiteOrigenReabrir", respuesta.getNomTramiteOrigenReabrir());
                            gVO.setAtributo("fecIniTramiteOrigenReabrir", respuesta.getFecIniTramiteOrigenReabrir());
                            
                            OperacionesExpedienteManager.getInstance().registrarRetrocederTramiteOrigen(gVO, params);
                        } else if (ConstantesDatos.TRAMITE_REABRIR.equals(tipoRespuesta)) {
                            expForm.setRespOpcion("");
                            m_Log.debug("objeto respuesta" + gVO.toString());
                            OperacionesExpedienteManager.getInstance().registrarReabrirTramite(gVO, params);
                        } else {
                            expForm.setRespOpcion("");
                        }
                    }else
                         m_Log.debug("respuesta vacia");
                        expForm.setRespOpcion(""); // Se pone a vacío el atributo respOpcion

                } catch (TramitacionException te) {
                    m_Log.error("Lanzada TramitacionException en FichaExpedienteAction retrocederExpediente");
                    expForm.setMensajeSW(te.getMessage());
                    mensajeNoFinalizado = true;
                } catch (EjecucionSWException eswe) {
                    m_Log.error("************** Lanzada EjecucionSWException en FichaExpedienteAction retrocederExpediente **************");
                    ponerMensajeFalloSW(expForm, eswe);
                    mensajeNoFinalizado = true;
                } catch(EjecucionModuloException e){
                    m_Log.error("************* Lanzada EjecucionModuloException en FichaExpedienteAction retrocederExpediente: " + e.getMensaje() + " ******************");
                    expForm.setMensajeSW(e.getMensaje());
                    mensajeNoFinalizado = true;
                }finally
                {
                    if(mensajeNoFinalizado){
                        try{
                            Config m_ConfigExp = ConfigServiceHelper.getConfig("Expediente");
                            Boolean mostrarMensajeSW =m_ConfigExp.getString(Integer.toString(usuarioVO.getOrgCod())  + "/mostrar_mensaje_error_WS_retroceso").toUpperCase().equals("NO");
                            if(mostrarMensajeSW) expForm.setMensajeSW("");
                        }catch(Exception e)
                        {

                        }
                    }
        
                }
            }else{
                tramiteNoRetrocedido = false;
                if(tieneFormularios && tieneDocumentosFirmados)
                    expForm.setRespOpcion(ConstantesDatos.TRAMITE_CON_FORMULARIOS_DOCUMENTOS_FIRMADOS);
                else
                if(tieneFormularios && !tieneDocumentosFirmados)
                    expForm.setRespOpcion(ConstantesDatos.TRAMITE_CON_FORMULARIOS_FIRMADOS);
                else
                if(!tieneFormularios && tieneDocumentosFirmados)
                    expForm.setRespOpcion(ConstantesDatos.TRAMITE_CON_DOCUMENTOS_FIRMADOS);
            }

            if(tramiteNoRetrocedido){
                expForm.setRespOpcion(respuesta.getTipoRespuesta());
            }else{

                if (mensajeNoFinalizado) {
                    expForm.setRespOpcion("noFinalizado");
                } else {
                    Vector eDS = expForm.getEstructuraDatosSuplementarios();
                    Vector vDS = FichaExpedienteManager.getInstance().cargaValoresDatosSuplementarios(gVO, eDS, params);
                    expForm.setValoresDatosSuplementarios(vDS);
                }
            }

            m_Log.debug("Mensaje no finalizado --> " + mensajeNoFinalizado);
            Vector tramites = FichaExpedienteManager.getInstance().cargaTramites(gVO, params);
            expForm.setTramites(tramites);
            
            Vector permisosTramites = FichaExpedienteDAO.getInstance().cargaPermisosTramites(tramites,gVO,params);
            expForm.setPermisosTramites(permisosTramites);

        } else if ("grabarExpediente".equals(opcion)) {            
			m_Log.debug("Grabar Expediente - Inicio");
            GeneralValueObject gVO = new GeneralValueObject();
            gVO = expForm.getExpedienteVO();
            
           GeneralValueObject gVODocumentos = new GeneralValueObject();
           gVODocumentos.setAtributo("codMunicipio",expForm.getCodMunicipio());
           gVODocumentos.setAtributo("codProcedimiento",expForm.getCodProcedimiento());
           gVODocumentos.setAtributo("ejercicio",expForm.getEjercicio());
           gVODocumentos.setAtributo("numero",expForm.getNumExpediente());
           
           try {
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");                
                tramExpVO.setCodMunicipio(expForm.getCodMunicipio());
                tramExpVO.setCodProcedimiento(expForm.getCodProcedimiento());
                tramExpVO.setEjercicio(expForm.getEjercicio());
                tramExpVO.setNumeroExpediente(expForm.getNumExpediente());
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario())); 
                tramExpVO.setBloqueo("no");                
                String ChequearCamposExpresion = TramitacionExpedientesManager.getInstance().tratarCamposExpresion(tramExpVO,gVO,request,params);          
                int resultado = 0;         
                
                if ("0".equals(ChequearCamposExpresion) || "1".equals(ChequearCamposExpresion))
                {                    
                    String listaCodTercero = request.getParameter("listaCodTercero");
                    String listaVersionTercero = request.getParameter("listaVersionTercero");
                    String listaCodDomicilio = request.getParameter("listaCodDomicilio");
                    String listaRol = request.getParameter("listaRol");
                    String listaMostrar = request.getParameter("listaMostrar");
                    String listaNotificacionesElectronica = request.getParameter("listaNotificacionesElectronicas");
                    String ubicacionDoc = request.getParameter("ubicacionDoc");

                    gVO.setAtributo("listaCodTercero", listaTemasSeleccionados(listaCodTercero));
                    gVO.setAtributo("listaVersionTercero", listaTemasSeleccionados(listaVersionTercero));
                    gVO.setAtributo("listaCodDomicilio", listaTemasSeleccionados(listaCodDomicilio));
                    gVO.setAtributo("listaRol", listaTemasSeleccionados(listaRol));
                    gVO.setAtributo("listaMostrar", listaTemasSeleccionados(listaMostrar));
                    gVO.setAtributo("listaNotificacionesElectronica", listaTemasSeleccionados(listaNotificacionesElectronica));
                    gVO.setAtributo("usuario", Integer.toString(usuario.getIdUsuario()));
                    gVO.setAtributo("nomUsuario", usuarioVO.getNombreUsu());
                    gVO.setAtributo("ubicacionDoc", ubicacionDoc);

                    // #30018 Validar roles
                    m_Log.debug("Guardar Expediente - INIT validacion de roles");
                    Vector validacionRolesListaRol = (Vector) gVO.getAtributo("listaRol");
                    Vector validacionRolesListaTerceros = (Vector) gVO.getAtributo("listaCodTercero");
                    if (validacionRolesListaRol.size() != validacionRolesListaTerceros.size()) {
                        resultado = -600;
                    }
                    Set<String> rolSet = new HashSet<String>();
                    for (int i = 0; i < validacionRolesListaRol.size(); i++) {
                        String rol = (String) validacionRolesListaRol.get(i);
                        rol = rol.replaceAll("¥", "").replaceAll("§", "");
                        if (!rolSet.add(rol)) {
                            resultado = -601;
                        }
                    }/*
                    try {
                        Set<String> docSet = new HashSet<String>();
                        for (int i = 0; i < validacionRolesListaTerceros.size(); i++) {
                            String documento = "";
                            String codTercero = "";

                            codTercero = ((Vector<String>) gVO.getAtributo("listaCodTercero")).get(i);

                            codTercero = codTercero.replaceAll("¥", "").replaceAll("§", "");

                            documento = FichaExpedienteManager.getInstance().obtenerDocumentoTercero(codTercero, params);
							if (!docSet.add(documento)) {
                                resultado = -602;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    m_Log.debug("Guardar Expediente - FIN validacion de roles");
                    if (resultado > -599) {
                        // #253692
                        resultado = FichaExpedienteManager.getInstance().grabarExpediente(gVO, params);

                        // Se carga la lista de documentos del expediente
                        Vector documentos = FichaExpedienteManager.getInstance().cargaListaDocumentosExpediente(gVODocumentos, params);
                        expForm.setDocumentos(documentos);

                        int resultadoGrabarDatosSuplementarios = 0;

                        m_Log.debug("Datos Suplementarios");
                        if (resultado > 0) {
                            resultadoGrabarDatosSuplementarios = grabarDatosSuplementarios(expForm, gVO, request, params);

                            if (resultadoGrabarDatosSuplementarios == 1) {
                                resultadoGrabarDatosSuplementarios = grabarDatosSuplementariosFichero(expForm, gVO, request, params);
                                if (resultadoGrabarDatosSuplementarios != 1)
                                    request.setAttribute("errorGrabarDatosSuplFichero", "1");
                            }

                            if (resultadoGrabarDatosSuplementarios == 1) {
                                // Se han insertado correctamente los valores de los campos suplementarios, incluidos los de
                                // tipo fichero. Entonces hay que actualizar en la sesion el estado de los ficheros
                                // que tenian estado NUEVO, por GRABADO.
                                GeneralValueObject ESTADOS = expForm.getListaEstadoFicheros();
                                GeneralValueObject LONGITUDES_FICHEROS = expForm.getListaLongitudFicherosDisco();

                                Hashtable tabla = ESTADOS.getTabla();
                                Enumeration claves = tabla.keys();
                                // Se han grabado los campos suplementarios, por tanto, se actualiza el contenido
                                // de la lista de ficheros
                                while (claves.hasMoreElements()) {
                                    String clave = (String) claves.nextElement();
                                    Integer estado = (Integer) tabla.get(clave);

                                    m_Log.debug("   ================ CLAVE: " + clave + " CON ESTADO: " + estado);
                                    if (estado == ConstantesDatos.ESTADO_DOCUMENTO_NUEVO) {
                                        ESTADOS.setAtributo(clave, new Integer(ConstantesDatos.ESTADO_DOCUMENTO_GRABADO));
                                    } else if (estado == ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO) {
                                        // Se pone el estado del fichero a vacio
                                        ESTADOS.setAtributo(clave, new Integer(ConstantesDatos.ESTADO_DOCUMENTO_VACIO));
                                        // Al ser eliminado el fichero, se pone su longitud a cero
                                        LONGITUDES_FICHEROS.setAtributo(clave, new Integer(0));
                                    }
                                }
                                expForm.setListaEstadoFicheros(ESTADOS);
                                expForm.setListaLongitudFicherosDisco(LONGITUDES_FICHEROS);
                            }
                            OperacionesExpedienteManager.getInstance().registrarGrabarExpediente(gVO, params);
                        }
                    }
                    m_Log.debug("Fin Datos Suplementarios");
                    // grabamos los documentos asignados al expediente en la pestania 'Otros Documentos'                    

                    String respOpcion = "";
                    if (resultado > 0) {
                        respOpcion = "grabado";
                    } else if (resultado == -600) {
                        respOpcion = "validacionRolErrorUno";
                    } else if (resultado == -601) {
                        respOpcion = "validacionRolErrorDos";
                    } else if (resultado == -602) {
                        respOpcion = "validacionRolErrorTres";
                    } else {
                        respOpcion = "noGrabado";
                    }
                    expForm.setRespOpcion(respOpcion);

                    /*****/
                    // SE COMPRUEBA SI HAY QUE LLAMAR A ALGUNA OPERACION JAVASCRIPT DE LAS PANTALLAS DE EXPEDIENTE DE UN MÓDULO EXTERNO, QUE
                    // ESTAN ASOCIADOS AL PROCEDIMIENTO DE ESTE EXPEDIENTE EN CONCRETO. ESTAS FUNCIONES JAVASCRIPT DEBEN TENER DIFERENTES NOMBRES
                    // EN EL CASO DE QUE HUBIESE MÁS DE UNA PANTALLA DE DIFERENTES MÓDULOS ASOCIADAS A UN MISMO PROCEDIMIENTO.


                    ArrayList<String> opsJavascript = ModuloIntegracionExternoFactoria.getInstance().getFuncionesJavascriptActualizarPantallaExpediente(usuarioVO.getOrgCod(), expForm.getCodProcedimiento());
                    expForm.setOperacionesJavascript(opsJavascript);

                    /*******/                   
                }
                else
                {                    
                    expForm.setRespOpcion("ErrorExpresion" + "#" + ChequearCamposExpresion );
                }
            }
            catch (Exception e) {                    
                e.printStackTrace();
            }
            opcion = "fichaExpediente";
            
        } else if ("grabarExpedienteObservaciones".equals(opcion)) {
            GeneralValueObject gVO = new GeneralValueObject();
            gVO = expForm.getExpedienteVO();
            // Pestaña de documentos

            int resultado = 0;

                resultado = FichaExpedienteManager.getInstance().grabarExpedienteObservaciones(gVO, params);

                // grabamos los documentos asignados al expediente en la pestaña 'Otros Documentos'
                // Estos documentos se podrán dar de alta incluso cuando el expediente estee Finalizado
                //FichaExpedienteManager.getInstance().grabarOtrosDocumentosExpediente(expForm.getCodMunicipio(), expForm.getEjercicio(), expForm.getNumero(), expForm.getOtrosDocumentosExpediente(), params);


            String respOpcion = "";
            if (resultado > 0) {
                respOpcion = "grabadoSoloObs";
            } else {
                respOpcion = "noGrabado";
            }
            expForm.setRespOpcion(respOpcion);
            opcion = "fichaExpediente";

        } else if ("listaUnidadesUsuario".equals(opcion)) {
            GeneralValueObject g = new GeneralValueObject();
            g.setAtributo("usuario", Integer.toString(usuarioVO.getIdUsuario()));
            g.setAtributo("codOrganizacion", Integer.toString(usuarioVO.getOrgCod()));
            g.setAtributo("codEntidad", Integer.toString(usuarioVO.getEntCod()));
            Vector listaUnidadesUsuario = new Vector();
            listaUnidadesUsuario = FichaExpedienteManager.getInstance().getListaUnidadesUsuario(g, params);
            expForm.setListaUnidadesUsuario(listaUnidadesUsuario);
        } else if ("actualizarInteresados".equals(opcion)) {
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", request.getParameter("codMunicipio"));
            gVO.setAtributo("codProcedimiento", request.getParameter("codProcedimiento"));
            gVO.setAtributo("ejercicio", request.getParameter("ejercicio"));
            String numero = request.getParameter("numero");
            gVO.setAtributo("numero", numero);
            //Vector<InteresadoExpedienteVO> terceros = InteresadosManager.getInstance().getListaInteresados(gVO, params);
            ArrayList<InteresadoExpedienteVO> terceros = InteresadosManager.getInstance().getListaInteresadosExpediente(gVO, params);
            expForm.setTerceros(terceros);
            opcion = "cargar";
        } else if ("imprimirExpediente".equals(opcion)) {
            UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");	
            String idioma=Integer.toString(usuario.getIdioma());	
            String claveIdioma=InformesManager.getInstance().getClaveIdioma(params,idioma);	
            m_Log.debug("********* Idioma "+idioma+" *********************************");	
            m_Log.debug("********* claveIdioma "+claveIdioma+" *********************************");
            GeneralValueObject gVO = new GeneralValueObject();
            // Si es una baja multiple aparecen todos en la misma hoja.
            gVO.setAtributo("baseDir", m_Config.getString("PDF.base_dir"));
            gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
            gVO.setAtributo("usuDir", usuarioVO.getDtr());
            gVO.setAtributo("pdfFile", "SGE");
            String protocolo = StrutsUtilOperations.getProtocol(request);
            m_Log.debug("PROTOCOLO en uso :" + protocolo);
            gVO.setAtributo("estilo", "css/verProcedimiento.css");
            Vector ficheros = new Vector();
            GeneralPDF pdf = new GeneralPDF(params, gVO);
           String plantilla = "verExpediente_"+claveIdioma;
            
            // #253692: Si en el properties el valor es 'no', enviamos un dato vacío
            // al parsear el xml si el valor de ubicacion es vacío no imprimimos el dato
            if(!expForm.isUbicacionDocumentacionVis())
                expForm.setUbicacionDoc(null);
            //
            m_Log.debug("EXPEDIENTE " + expForm.getNumExpediente());
            String textoXML = crearXML(expForm,usuarioVO,params);
            m_Log.debug("textoXML "+ textoXML);
            pdf = new GeneralPDF(params, gVO);
            ficheros.add(pdf.transformaXML(textoXML, plantilla));
            request.setAttribute("nombre", pdf.getPdf(ficheros));
            // Auditoria de accesos al expediente
            try {
                AuditoriaManager.getInstance().auditarAccesoExpediente(
                        ConstantesAuditoria.EXPEDIENTE_IMPRIMIR,
                        usuarioVO,
                        expForm.getExpedienteVO());
            } catch (TramitacionException te) {
                m_Log.error("No se pudo registrar el evento de auditoria", te);
            }

  } else if("verDocumentacion".equals(opcion)) {
      
      m_Log.debug("Ver toda la documentacion del expediente " + expForm.getNumExpediente());   
                 
          Vector<FicheroVO> ficherosExpediente = FichaExpedienteDAO.getInstance().cargarFicherosExpediente(expForm.getNumExpediente(),usuarioVO.getOrgCod(),expForm.isExpHistorico(),params);            
          request.setAttribute("ficherosExpediente",ficherosExpediente);
          
           // añadimos a la sesion la propiedad SERVICIO_DIGITALIZACION_ACTIVO de Registro.properties
            Boolean digitalizacionActivo = false;
            String servDigitalizacionAct = (String) session.getAttribute("servicioDigitalizacionActivo");
            if(servDigitalizacionAct == null) {
                try{
                    servDigitalizacionAct = registroConf.getString(usuarioVO.getOrgCod() + "/SERVICIO_DIGITALIZACION_ACTIVO");
                }catch(Exception e){
                    m_Log.error("Se ha producido un error recuperando la propiedad '" + usuarioVO.getOrgCod() + "/SERVICIO_DIGITALIZACION_ACTIVO' de Registro.properties");
                }
            }
            if(servDigitalizacionAct!=null && servDigitalizacionAct.equalsIgnoreCase("si")){
                    session.setAttribute("servicioDigitalizacionActivo", servDigitalizacionAct);
                    digitalizacionActivo = true;
            }
            
          
          //Ficheros de inicio de expediente
          GeneralValueObject gVODocumentos = new GeneralValueObject();
          gVODocumentos.setAtributo("codMunicipio",expForm.getCodMunicipio());
          gVODocumentos.setAtributo("codProcedimiento",expForm.getCodProcedimiento());
          gVODocumentos.setAtributo("ejercicio",expForm.getEjercicio());
          gVODocumentos.setAtributo("numero",expForm.getNumExpediente());

           /** nuevo **/
           // Se carga la lista de documentos del expediente
           Vector<FicheroVO> documentos = FichaExpedienteManager.getInstance().cargaDocumentosExpediente(gVODocumentos,expForm.isExpHistorico(),params);
           Vector<FicheroVO> auxiliar = new Vector<FicheroVO>();
           for(int i=0;documentos!=null && i<documentos.size();i++){
               FicheroVO f = documentos.get(i);
               f.setCodigo(f.getCodigoDocumentoPresentado()+ "-" + f.getMunicipio() + "-" +  f.getEjercicio() + "-" + f.getNumero());
               auxiliar.add(f);
           }
           
           request.setAttribute("documentos",auxiliar);
           
           
          // Leemos los ficheros del registro.
          // Para los enlaces de la jsp se creará un código formado por ejercicio, número y tipo de
          // asiento y nombre del fichero y se guardará en el form un Hashmap relacionando este código 
          // con el FicheroVO. Estos FicheroVO guardados en el form se usarán para recuperar los 
          // ficheros en VerDocumentoServlet.
          HashMap<String,FicheroVO> relacionFicheros = new HashMap<String,FicheroVO>();          
          Vector<FicheroVO> ficherosRegistroEntrada = FichaExpedienteManager.getInstance().cargarFicherosRegistro("E",expForm.getNumExpediente(),expForm.getCodMunicipio(),expForm.isExpHistorico(),params);  
          String registroAnterior ="";
          for (FicheroVO fichero : ficherosRegistroEntrada) {                                                        
              fichero.setCodigo(fichero.getEjercicio()+ "§¥" + fichero.getNumero() + "§¥" + fichero.getTipo() + "§¥" + fichero.getNombre() + "§¥" + fichero.getUor() + "§¥" + fichero.getDep() + "§¥" + fichero.getTipoContenido());
              fichero.setNombreAsiento(fichero.getEjercicio()+ "/" + fichero.getNumero());
              if(digitalizacionActivo){
                  fichero= FichaExpedienteManager.getInstance().cargarTipoDocumentalFicherosRegistro(fichero, params);
              }
               if (!registroAnterior.equals(fichero.getNombreAsiento())) {
                  fichero.setNuevoRegistro("true");
                  registroAnterior = fichero.getNombreAsiento();
              }
              relacionFicheros.put(fichero.getCodigo(), fichero);              
          }          
          request.setAttribute("ficherosRegistroEntrada",ficherosRegistroEntrada);
          request.setAttribute("numeroExpediente",expForm.getNumExpediente());
          request.setAttribute("expedienteHistorico",expForm.isExpHistorico());
          
          // Registro de salida
          Vector<FicheroVO> ficherosRegistroSalida = 
              FichaExpedienteManager.getInstance().cargarFicherosRegistro("S",expForm.getNumExpediente(),expForm.getCodMunicipio(),expForm.isExpHistorico(),params);
          for (FicheroVO fichero : ficherosRegistroSalida) {                            
              fichero.setCodigo(fichero.getEjercicio()+ "-" + fichero.getNumero() + "-" + 
                                fichero.getTipo() + "-" + fichero.getNombre());
              fichero.setNombreAsiento(fichero.getEjercicio()+ "/" + fichero.getNumero());
              relacionFicheros.put(fichero.getCodigo(), fichero);              
          }          
          request.setAttribute("ficherosRegistroSalida",ficherosRegistroSalida);
          
          // Ficheros Aportados Anteriormente a registro
          ArrayList<FicheroVO> ficherosAportadosAnteriorEntrada
                  = FichaExpedienteManager.getInstance().cargarListaFicherosAportadosAnterior("E", expForm.getNumExpediente(), params);
          request.setAttribute("ficherosAportadosAnteriorEntrada", ficherosAportadosAnteriorEntrada);

          // Ficheros Aportados Anteriormente desde registro
          ArrayList<FicheroVO> ficharosAportadosAnteriorSalida
                  = FichaExpedienteManager.getInstance().cargarListaFicherosAportadosAnterior("S", expForm.getNumExpediente(), params);
          request.setAttribute("ficherosAportadosAnteriorSalida", ficharosAportadosAnteriorSalida);
          
          // Trámites. Se crea un código y se guarda en el HashMap igual que con los ficheros del registro.
          Vector<FicheroVO> ficherosTramites =
              FichaExpedienteManager.getInstance().cargarFicherosTramites(usuarioVO, expForm.getNumExpediente(),
                      expForm.getCodMunicipio(),expForm.isExpHistorico(),params);
          String tramiteAnterior = "";
          for (FicheroVO fichero : ficherosTramites) {
              if (fichero.getTipoContenido().equals("none")) {
                  fichero.setCodigo("DOC-" + fichero.getMunicipio() + "-" + fichero.getExpediente() + "-" + fichero.getTramite() + "-" +
                          fichero.getOcurrencia() + "-" + fichero.getCodigoFicheroTramite());
              } else {
                  fichero.setCodigo("ANEXO-" + fichero.getMunicipio() + "-" + fichero.getExpediente() + "-" + fichero.getTramite() + "-" +
                          fichero.getOcurrencia() + "-" + fichero.getCodigoFicheroTramite());
              }
              // Se fija el valor para indicar en la jsp si hay que dibujar un nuevo tramite
              if (!tramiteAnterior.equals(fichero.getNombreTramite())) {
                  fichero.setNuevoTramite("true");
                  tramiteAnterior = fichero.getNombreTramite();
              }
              relacionFicheros.put(fichero.getCodigo(), fichero);
          }          
          request.setAttribute("ficherosTramites",ficherosTramites);
          
          ArrayList<ExpedienteOtroDocumentoVO> otrosDocumentosExpediente = new ArrayList<ExpedienteOtroDocumentoVO>();          
          otrosDocumentosExpediente = FichaExpedienteManager.getInstance().obtenerOtrosDocumentosExpediente(expForm.getCodMunicipio(), expForm.getEjercicio(), expForm.getNumExpediente(),expForm.isExpHistorico(),params);          
          
          request.setAttribute("documentosExternosExpediente",otrosDocumentosExpediente);
                    
          // Por último, añadimos la relación de ficheros al form          
          request.setAttribute("relacionFicheros",relacionFicheros);
  }else if("actualizarDocumentacionAsociada".equals(opcion)){
      m_Log.debug("inicio---> actualizarDocumentacionAsociada");
      String numeroExpediente = request.getParameter("numeroExpediente");
      String expedienteHistorico = request.getParameter("expedienteHistorico");
      m_Log.debug("numeroExpediente "+numeroExpediente);
      m_Log.debug("expedienteHistorico "+expedienteHistorico);
      
       
          Vector<FicheroVO> ficherosRegistroEntrada = FichaExpedienteManager.getInstance().cargarFicherosRegistro("E",expForm.getNumExpediente(),expForm.getCodMunicipio(),expForm.isExpHistorico(),params);  
          
          for (FicheroVO fichero : ficherosRegistroEntrada) {                                                        
              fichero.setCodigo(fichero.getEjercicio()+ "§¥" + fichero.getNumero() + "§¥" + fichero.getTipo() + "§¥" + fichero.getNombre() + "§¥" + fichero.getUor() + "§¥" + fichero.getDep() + "§¥" + fichero.getTipoContenido());
              fichero.setNombreAsiento(fichero.getEjercicio()+ "/" + fichero.getNumero());
              fichero= FichaExpedienteManager.getInstance().cargarTipoDocumentalFicherosRegistro(fichero, params);               
          }   
      retornarJSON(new Gson().toJson(ficherosRegistroEntrada), response);
      
  }  else if ("copiarExpediente".equals(opcion))
    {
        int res=0;
        GeneralValueObject gVO = new GeneralValueObject();
        m_Log.debug("CODIGO DE MUNICIPIO: " + request.getParameter("codMunicipio"));


        gVO.setAtributo("codMunicipio", request.getParameter("codMunicipio"));
        gVO.setAtributo("codProcedimiento", request.getParameter("codProcedimiento"));
        gVO.setAtributo("ejercicio", request.getParameter("ejercicio"));
        gVO.setAtributo("usuario", Integer.toString(usuarioVO.getIdUsuario()));
        gVO.setAtributo("nomUsuario", usuarioVO.getNombreUsu());
        gVO.setAtributo("codOrganizacion", Integer.toString(usuarioVO.getOrgCod()));
        gVO.setAtributo("codEntidad", Integer.toString(usuarioVO.getEntCod()));
        gVO.setAtributo("codAplicacion", Integer.toString(usuarioVO.getAppCod()));
        gVO.setAtributo("codUOR", request.getParameter("codUnidadOrganicaExp"));
        gVO.setAtributo("asunto", request.getParameter("asunto"));
        gVO.setAtributo("observaciones", request.getParameter("observaciones"));
        gVO.setAtributo(ConstantesDatos.ORIGEN_LLAMADA_NOMBRE_PARAMETRO, ConstantesDatos.ORIGEN_LLAMADA_INTERFAZ_WEB);
        
        String numExpedienteOriginal=request.getParameter("numExpediente");
        gVO.setAtributo("numExpedienteOriginal", numExpedienteOriginal);

        gVO.setAtributo("unidadTramiteInicioSeleccionada", request.getParameter("codUnidadOrganicaExp"));

            TipoDocumentosManager tipoDocumManager = TipoDocumentosManager.getInstance();
            Vector tiposDocs = tipoDocumManager.getListaTipoDocumentos(params);
            expForm.setListaTiposDocumentos(tiposDocs);
            //expForm.setListaRoles(this.obtenerRolesProcedimiento(gVO, params));
            expForm.setListaRoles(InteresadosManager.getInstance().getListaRoles(usuarioVO.getOrgCod(),(String)gVO.getAtributo("codProcedimiento"),params));

            /*** INICIO: SE VACIAN LOS SIGUIENTES CAMPOS DEL FORMULARIO DE EXPEDIENTES **/
            session.removeAttribute("filtro_pendientes");
            /*** FIN: SE VACIAN LOS SIGUIENTES CAMPOS DEL FORMULARIO DE EXPEDIENTES **/
            DefinicionTramitesValueObject defTramVO = new DefinicionTramitesValueObject();
            res = FichaExpedienteManager.getInstance().iniciarExpediente(gVO, params);
                if (res > 0) {
                    //  ini Obteber el plazo y el tipo e insertarlo en el gVO
                    defTramVO.setTxtCodigo((String) gVO.getAtributo("codProcedimiento"));
                    defTramVO.setCodigoTramite((String) gVO.getAtributo("codTramite"));
                    defTramVO = DefinicionTramitesManager.getInstance().getTramite(defTramVO, (String) gVO.getAtributo("codMunicipio"), params);
                    if (defTramVO.getPlazo() != null) {
                        if (!defTramVO.getPlazo().equals("")) {
                            gVO.setAtributo("plazo", defTramVO.getPlazo());
                            gVO.setAtributo("tipoPlazo", defTramVO.getUnidadesPlazo());
                        }
                        m_Log.debug("PLAZO       : " + gVO.getAtributo("plazo"));
                        m_Log.debug("TIPO PLAZO  : " + gVO.getAtributo("tipoPlazo"));
                    }
                    //  fin Obteber el plazo y el tipo e insertarlo en el gVO
                    if (notificar(usuarioVO, gVO)) {
                        gVO.setAtributo("notificacionRealizada", "si");
                    } else {
                        gVO.setAtributo("notificacionRealizada", "no");
                    }
                    gVO.setAtributo("tipoAlta", ConstantesDatos.TIPO_ALTA_EXP_COPIA);
                    OperacionesExpedienteManager.getInstance().registrarAltaExpediente(gVO,params);  
                }
            if (res >= 0) {
                /* Miramos si tiene exp relacionados, y en base a eso mostraremos el botón
                 * de exp relacionados de un color u otro
                 */
                String codMunicipio1 = request.getParameter("codMunicipio");
//                String ejercicio1 = request.getParameter("ejercicio");
//                String numero = request.getParameter("numeroExp");
                String ejercicio1 = (String)gVO.getAtributo("ejercicio");
                String numero = (String)gVO.getAtributo("numero");

                gVO.setAtributo("numExpedienteNuevo", numero);
               

                //Copiamos los terceros del expediente Original en el nuevo expediente
                boolean tercerosCopiados = FichaExpedienteManager.getInstance().copiarInteresados(gVO,params);
                if(!tercerosCopiados)
                request.setAttribute("error", "1");


                FichaExpedienteManager.getInstance().copiarLocalizacion(gVO,params);


                 //Copiamos los datos suplementarios del expediente Original en el nuevo expediente
                boolean datosSuplCopiados = FichaExpedienteManager.getInstance().copiarDatosSuplementarios(gVO,params);
                if(!datosSuplCopiados)
                request.setAttribute("error", "2");
                
                if(!tercerosCopiados&&!datosSuplCopiados)  request.setAttribute("error", "3");

                //Relacionar los expedientes original y nuevo
                ConsultaExpedientesValueObject consExpVO = new ConsultaExpedientesValueObject();
                consExpVO.setCodMunicipioIni(codMunicipio1);
                String[] partesNumExp = null;
                partesNumExp=numero.split("/");
                consExpVO.setEjercicio(partesNumExp[0]);
                
                consExpVO.setNumeroExpedienteIni(numExpedienteOriginal);
                consExpVO.setEjercicioIni(request.getParameter("ejercicio"));
                consExpVO.setCodMunicipio(request.getParameter("codMunicipio"));
                consExpVO.setNumeroExpediente(numero);
                
                int resultado = ConsultaExpedientesManager.getInstance().insertExpedientesRelacionados(consExpVO, params);

                if (resultado > 0){
                    consExpVO.setUsuario(Integer.toString(usuarioVO.getIdUsuario()));
                    consExpVO.setNombreUsuario(usuarioVO.getNombreUsu());
                    OperacionesExpedienteManager.getInstance().registrarAnhadirRelacion(consExpVO, params);
                }

                boolean expedientesRelacionados = FichaExpedienteManager.getInstance().tieneExpedientesRelacionados(codMunicipio1, ejercicio1, numero, params);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("--> Booleano expREl : " + expedientesRelacionados);
                }

                gVO.setAtributo("expedientesRelacionados", expedientesRelacionados);
                // 'desdeAsiento' se usa para saber que estamos iniciando un expediente desde un asiento
                
                cargarExpediente(expForm, gVO, params, usuarioVO);
                opcion = "cargar";
                try{
                        Config m_ConfigExp = ConfigServiceHelper.getConfig("Expediente");
                        Boolean envioMailsInicioExpBuzonDuplicar =m_ConfigExp.getString(Integer.toString(usuarioVO.getOrgCod())  + "/envioMailInicioExpedienteBuzonDuplicar").toUpperCase().equals("SI");
                        if(envioMailsInicioExpBuzonDuplicar) sendMail(request, gVO, params, defTramVO);
                            
                }catch(Exception e)
                {
                        sendMail(request, gVO, params, defTramVO); 
                }

                if (res == 2) {
                    request.setAttribute("msgExpSinTercero", "si");
                }
                request.setAttribute("expedienteCreado", gVO.getAtributo("numero"));
            } else {
                m_Log.debug("FichaExpedienteAction: NO SE HA INICIADO EL EXPEDIENTE");
                request.setAttribute("error", "0");
            }
            return mapping.findForward("verNumeroCopiado");

            //INICIO #72851 verificarmos la firma de una comunicacion
    }else if ("verificarFirmaComunicacion".equals(opcion)){
        return verficarFirmaComunicacion(expForm, mapping, request,usuarioVO);
        
    } //Verificamos la firma del adjunto de una comunicacion
    else if ("verificarFirmaDocComunicacion".equals(opcion)){
        return verficarFirmaDocumentoComunicacion(expForm, mapping, request,usuarioVO);
        
    }//FIN #72851 
        
    else if(("tramiteManualSinRecargar".equals(opcion)) /* #212448*/ || "cargarTramiteDirecto".equals(opcion)){ 
        // opcion añadida en #208372 para iniciar directamente un tramite manual
        GeneralValueObject gVO = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        Vector listaTramites = null;
        ArrayList<String> permisosTr = null;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            String numExpediente = request.getParameter("numExpediente");
            String[] partes = numExpediente.split("/");
            gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio",Integer.toString(usuarioVO.getOrgCod()));
            gVO.setAtributo("codOrganizacion",Integer.toString(usuarioVO.getOrgCod()));
            gVO.setAtributo("codEntidad",Integer.toString(usuarioVO.getEntCod()));
            gVO.setAtributo("codProcedimiento",partes[1]);
            gVO.setAtributo("ejercicio",partes[0]);
            gVO.setAtributo("numero",numExpediente);
            gVO.setAtributo("usuario",Integer.toString(usuarioVO.getIdUsuario()));
            gVO.setAtributo("expHistorico",String.valueOf(false));
            listaTramites = FichaExpedienteDAO.getInstance().cargaTramites(gVO,adapt,con,false);
            permisosTr = new ArrayList<String>(FichaExpedienteDAO.getInstance().cargaPermisosTramites(listaTramites,gVO, con));
            // devolvemos los datos como String en formato json
            ArrayList datos = new ArrayList();
            datos.add(listaTramites);
            datos.add(permisosTr);
            retornarJSON(new Gson().toJson(datos), response);
            return null;
        } catch(BDException bde){
            bde.printStackTrace();
            m_Log.error("Error al obtener una conexión a la bbdd");
        } finally {
            if(con != null) try {
                adapt.devolverConexion(con);
            } catch (BDException ex) {
                ex.printStackTrace();
                m_Log.error("Error al liberar la conexión a la bbdd.");
            }
        }
    } else if(opcion.equals("seleccionUOR")){ // #254454: mostrar arbol de uors en ficha expediente
        ArrayList<UORDTO> listaUORs = new ArrayList<UORDTO>();
        ArbolImpl arbolUORs = new ArbolImpl();
        UORsManager managerUORs = UORsManager.getInstance();

        listaUORs = new ArrayList<UORDTO>(managerUORs.getListaUORsPorNoVisRegistro('0', params));

        String vengoPagina = request.getParameter("vengoPagina");            
        boolean ordenarNombre = false;
        if(vengoPagina!=null && "true".equals(vengoPagina)){
            ordenarNombre = false;                        
        }else{
            request.setAttribute("marcarCodigo","false");
            // Si la petición no llega de la página que muestra el árbol de uors.
            ResourceBundle config = ResourceBundle.getBundle("Expediente");
            try{
                String tipoOrdenacion = config.getString(usuarioVO.getOrgCod() + "/ORDENACION_ARBOL");
                if(tipoOrdenacion!=null && "NOMBRE".equals(tipoOrdenacion)){
                    ordenarNombre = true;
                }                            
            }catch(Exception e){      
                // Se ordena por código
                ordenarNombre = false;
            }
        }

        if(ordenarNombre) 
            request.setAttribute("marcarCodigo","false");
        else
            request.setAttribute("marcarCodigo","true");

        arbolUORs = managerUORs.getArbolUORs(false,true, ordenarNombre, params);

        m_Log.debug("Recuperadas " + listaUORs.size() + " UORs");
        m_Log.debug("Recuperadas " + (arbolUORs.contarNodos() - 1) + " UORs en el Ã¡rbol");
        request.setAttribute("arbolUORs", arbolUORs);
        request.setAttribute("listaUORs", listaUORs);
        opcion = "seleccionUOR";
    } else if (opcion.equalsIgnoreCase("cargarNombre")) { // #254454: ordenar arbol de UORs
        m_Log.debug("Recargando datos de las UORs en la sesiÃ³n");
        ArrayList<UORDTO> nUOR = new ArrayList<UORDTO>();
        ArbolImpl arbolUORs = new ArbolImpl();

        nUOR = new ArrayList<UORDTO>(UORsManager.getInstance().getListaUORs(false,params));
        //nUOR = new ArrayList<UORDTO>(UORsManager.getInstance().getListaUORsPorNoVisRegistro('0',params));
        arbolUORs = UORsManager.getInstance().getArbolUORs(false,true, true, params);
        m_Log.debug("Cargadas " + nUOR.size() + " UORs");
        m_Log.debug("Cargado Ã¡rbol:" + arbolUORs.contarNodos());

        request.setAttribute("listaUORs", nUOR);
        request.setAttribute("arbolUORs", arbolUORs);

        opcion = "cargarUORsPorNombre";
    } else if ("cargarListaOperacionesExpediente".equals(opcion)){
        boolean isExpHistorico = expForm.isExpHistorico();
        
        TraductorAplicacionBean traductor = new TraductorAplicacionBean();
        traductor.setApl_cod(ConstantesDatos.APP_GESTION_EXPEDIENTES);
        traductor.setIdi_cod(usuarioVO.getIdioma());
        ArrayList <OperacionExpedienteVO> listaOperacionesExpediente = OperacionesExpedienteManager.getInstance().recuperarOperacionesExpediente(usuarioVO.getOrgCod(),expForm.getNumExpediente(),isExpHistorico,traductor,params);
        expForm.setListaOperacionesExpediente(listaOperacionesExpediente);
    } else if ("cargarDetalleOperacionExpediente".equals(opcion)){
        String idOperacion = request.getParameter("operacion");
        boolean isExpHistorico = expForm.isExpHistorico();
        
        if (idOperacion != null && !idOperacion.equals("")){
            OperacionExpedienteVO operacionExpediente = OperacionesExpedienteManager.getInstance().recuperarOperacion(Integer.valueOf(idOperacion).intValue(), isExpHistorico, params);
            
            TraductorAplicacionBean traductor = new TraductorAplicacionBean();
            traductor.setApl_cod(ConstantesDatos.APP_GESTION_EXPEDIENTES);
            traductor.setIdi_cod(usuarioVO.getIdioma());
            String detalles = OperacionesExpedienteTraductor.traducir(traductor, operacionExpediente.getDescripcionOperacion());
            
            request.setAttribute("detalleOperacionExpediente",detalles);
        }
        
        } else if ("exportarExpediente".equals(opcion)) {

            m_Log.debug("<===================== FichaExpedienteAction ======================");
            String numExpediente = request.getParameter("numExpediente");
            try {
                exportarExpediente(numExpediente, request, response);
            } catch (IOException ex) {
                m_Log.debug("Error al obtener la respuesta: " + ex);
            }
        } else if ("descargarFichero".equals(opcion)) {
            try {
                descargarFichero(request, response);
            } catch (Exception ex) {
                Logger.getLogger(FichaExpedienteAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        m_Log.debug("<===================== FichaExpedienteAction ======================");

        return (mapping.findForward(opcion));

    }
    
    private ActionForward verficarFirmaComunicacion(FichaExpedienteForm form, ActionMapping mapping,HttpServletRequest request, UsuarioValueObject usuario){
        if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".verificarFirmaComunicacion() INICIO");}
    
        try{

            byte[] documento = null;
            int codOrganizacion = usuario.getOrgCod();
            String[] params = usuario.getParamsCon();
             
            String idComunicacion = request.getParameter("idCom");
            if (idComunicacion!=null && !idComunicacion.equals("")){
                Long longComId= Long.parseLong(idComunicacion);
            
                //Cogemos los datos de la comunicacion
                if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".verificarFirmaComunicacion() Obteniendo comunicacion con id " + longComId);}
                ComunicacionVO com = ComunicacionFlexiaManager.getInstance().getComunicacion(longComId, params);

                String firma = com.getFirma();
                if (com.getXmlComunicacion()!=null){
                    documento = com.getXmlComunicacion().getBytes();

                    File f = File.createTempFile("prueba", "temp");
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(documento);
                    fos.flush();
                    fos.close();

                    DocumentoFirmadoVO docFirmado = new DocumentoFirmadoVO();
                    docFirmado.setFicheroFirma(f);
                    docFirmado.setFirma(firma);
                    docFirmado.setTipoMime(MimeTypes.XML[0]);

                    if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".verificarFirmaComunicacion() Validando la firma");}
                    PluginPortafirmas plugin = PluginPortafirmasFactoria.getImplClass(String.valueOf(codOrganizacion));
                    ArrayListFirmasVO datosFirma = plugin.verificarFirmaInfo(docFirmado);
                    
                    form.setFirmaValida(false);
                    if(!datosFirma.isEmpty() && datosFirma.get(0) != null){
                        FirmaVO infoFirma = (FirmaVO) datosFirma.get(0);
                        form.setDatosCertificado(infoFirma.getAsuntoCertificado());
                        form.setEmisorCertificado(infoFirma.getEmisorCertificado());
                        form.setNombreFirmante(infoFirma.getNombrePersona());
                        form.setNifFirmante(infoFirma.getNif());
                        form.setValidezCertificado(infoFirma.getValidez());
                        form.setFirmaValida(infoFirma.getValido());
                        form.setVerificacionFirmaComunicacion(new Boolean(true));                        
                        
                    }
                    
                    if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".verificarFirmaComunicacion() Fin de verificacion de firma");}
                    f.delete();

                    if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".verificarFirmaComunicacion() Firma valida " + form.getFirmaValida());}
                    
                }else{
                    form.setFirmaValida(false);
                }
                
                
               
               
            }
            
        }catch (Exception e)
        {
            m_Log.error(this.getClass().getName() + ".verificarFirmaComunicacion() Error : " + e.getMessage());
        }
        return mapping.findForward("verificarFirmaComunicacion");
    
    }
    
    private ActionForward verficarFirmaDocumentoComunicacion(FichaExpedienteForm form, ActionMapping mapping,HttpServletRequest request, UsuarioValueObject usuario){
        if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".verficarFirmaDocumentoComunicacion() INICIO");}
    
        try{

            byte[] documento = null;
            byte[] documento64  = null;
            int codOrganizacion = usuario.getOrgCod();
            String[] params = usuario.getParamsCon();
             
            String idComunicacion = request.getParameter("idCom");
            String idAdjunto = request.getParameter("idAdj");
            if (idComunicacion!=null && !idComunicacion.equals("") && idAdjunto!=null && !idAdjunto.equals("")){
                Long longComId= Long.parseLong(idComunicacion);
                Long longAdjId = Long.parseLong(idAdjunto);
                
                //Cogemos los datos de la comunicacion
                if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".verficarFirmaDocumentoComunicacion() Obteniendo adjunto con id " + longAdjId + " de la comunicacion " + longComId);}
                AdjuntoComunicacionVO adjuntoVO = AdjuntoComunicacionFlexiaManager.getInstance().getAdjunto(longComId, longAdjId, params);
                String firma = adjuntoVO.getFirma();
                if (adjuntoVO.getContenido()!=null){
                    documento =adjuntoVO.getContenido().getBytes();
                    documento64 = Base64.decode(adjuntoVO.getContenido());
                
                    File f = File.createTempFile("prueba", "temp");
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(documento);
                    fos.flush();
                    fos.close();

                    DocumentoFirmadoVO docFirmado = new DocumentoFirmadoVO();
                    docFirmado.setFicheroFirma(f);
                    docFirmado.setFirma(firma);
                    docFirmado.setFicheroHash64(new String(documento64));
                    docFirmado.setTipoMime(adjuntoVO.getTipoMime());
                    
                    if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".verficarFirmaDocumentoComunicacion() Validando la firma");}
                    PluginPortafirmas plugin = PluginPortafirmasFactoria.getImplClass(String.valueOf(codOrganizacion));
                    ArrayListFirmasVO datosFirma = plugin.verificarFirmaInfo(docFirmado);
                    
                    form.setFirmaValida(false);
                    if(!datosFirma.isEmpty() && datosFirma.get(0) != null){
                        FirmaVO infoFirma = (FirmaVO) datosFirma.get(0);
                        form.setDatosCertificado(infoFirma.getAsuntoCertificado());
                        form.setEmisorCertificado(infoFirma.getEmisorCertificado());
                        form.setNombreFirmante(infoFirma.getNombrePersona());
                        form.setNifFirmante(infoFirma.getNif());
                        form.setValidezCertificado(infoFirma.getValidez());
                        form.setFirmaValida(infoFirma.getValido());
                        form.setVerificacionFirmaComunicacion(new Boolean(false));                        
                        
                    }

                    if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".verficarFirmaDocumentoComunicacion() Fin de verificacion de firma");}
                    f.delete();

                    if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".verficarFirmaDocumentoComunicacion() Firma valida " + form.getFirmaValida());}
                    
                }else{
                    form.setFirmaValida(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(this.getClass().getName() + ".verficarFirmaDocumentoComunicacion() Error : " + e.getMessage());
            
        }
        return mapping.findForward("verificarFirmaDocComunicacion");
    
    }
    
    
    private int grabarDatosSuplementarios(FichaExpedienteForm fEF, GeneralValueObject gVO, HttpServletRequest request, String[] params) throws IOException {
        int res = 0;
        m_Log.info("FichaExpendienteAction::grabarDatosSuplementarios,fechaVencimiento");
        //Vector estructuraDatosSuplementarios = new Vector();
        //estructuraDatosSuplementarios = fEF.getEstructuraDatosSuplementarios();
        
        // Se obtiene la estructura de los campos suplementarios en formato JSON
        String jsonEstructuraCamposSuplementarios = fEF.getJsonEstructuraCamposSuplementarios();
        // Se parse el JSON con la estructura de los campos suplementarios
        Gson gson = new Gson();
        java.lang.reflect.Type tipo = new TypeToken<ResultadoTratamientoCargaParcialExpedienteVO>(){}.getType();
        ResultadoTratamientoCargaParcialExpedienteVO resultado =(ResultadoTratamientoCargaParcialExpedienteVO)gson.fromJson(jsonEstructuraCamposSuplementarios,tipo);
        
        Vector<EstructuraCampoAgrupado> estructuraCamposFinal = new Vector<EstructuraCampoAgrupado>();
        
        if(resultado!=null && resultado.getAgrupaciones()!=null && resultado.getAgrupaciones().size()>0){
            ArrayList<DefinicionAgrupacionCamposAgrupadosValueObject> agrupaciones = resultado.getAgrupaciones();                    
            for(int i=0;agrupaciones!=null && i<agrupaciones.size();i++){       
                if(agrupaciones.get(i).getEstructura()!=null)
                    estructuraCamposFinal.addAll(agrupaciones.get(i).getEstructura());                
            }            
        }
        
        GeneralValueObject listaFicheros;
        listaFicheros = fEF.getListaFicheros();
        GeneralValueObject listaTiposFicheros;
        listaTiposFicheros = fEF.getListaTiposFicheros();
        Config m_Conf = ConfigServiceHelper.getConfig("common");
        String campo = "E_PLT.CodigoPlantillaFichero";
        String tipoDatoFichero = m_Conf.getString(campo);
        campo = "E_PLT.CodigoCampoDesplegable";
        String tipoDatoDesplegable = m_Conf.getString(campo);
        
        /** nuevo ***/
        GeneralValueObject listaEstadosFicheros = fEF.getListaEstadoFicheros();
        GeneralValueObject listaRutaFicherosNuevos = fEF.getListaRutaFicherosDisco();
        GeneralValueObject listaMetadatosFicherosNuevos = fEF.getListaMetadatosFicheros();
        /** nuevo ***/
        
        //for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
        for (int i=0;i<estructuraCamposFinal.size();i++) {
            //EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
            EstructuraCampoAgrupado eC = (EstructuraCampoAgrupado) estructuraCamposFinal.get(i);
            m_Log.debug("CODIGO ... " + eC.getCodCampo());
            m_Log.debug("NOMBRE ... " + eC.getDescCampo());
            m_Log.debug("TIPO DATO ... " + eC.getCodTipoDato());
            eC.setCampoActivo("activar" + eC.getCodCampo());
            eC.setFechaVencimiento("fechaVencimiento" + eC.getCodCampo()) ;
            m_Log.debug("ACTIVAR/DESACTIVAR ... " + eC.getCampoActivo());

            
            if (eC.getCodTipoDato().equals(tipoDatoFichero)) { // 5 = FICHERO
                m_Log.debug("Procesado FICHERO ... ");
                if (listaFicheros.getAtributo(eC.getCodCampo()) != "") {
                    m_Log.debug("FICHERO ........................ NO VACIO ");
                    
                    
                    String tipoFichero = (String) listaTiposFicheros.getAtributo(eC.getCodCampo());
                    String aux = request.getParameter(eC.getCodCampo());
            
                    if(tipoFichero!=null && !"".equals(tipoFichero)){
                        // Si se puede recuperar el valor del fichero
                        String[] matriz = aux.split("/");
                        aux = matriz[matriz.length - 1];
                        gVO.setAtributo(eC.getCodCampo() + "_NOMBRE", aux);
                        gVO.setAtributo(eC.getCodCampo() + "_TIPO", tipoFichero);
                        
                        Integer estadoFichero = (Integer)listaEstadosFicheros.getAtributo(eC.getCodCampo());            
                        String rutaFicheroDisco = (String)listaRutaFicherosNuevos.getAtributo(eC.getCodCampo());
                        MetadatosDocumentoVO metadatosFichero = (MetadatosDocumentoVO)listaMetadatosFicherosNuevos.getAtributoONulo(eC.getCodCampo());
                        
                        gVO.setAtributo(eC.getCodCampo() + "_ESTADO",estadoFichero.toString());
                        gVO.setAtributo(eC.getCodCampo() + "_RUTA",rutaFicheroDisco);
                        
                       boolean generarCSV = true;
                       if (fEF.getCodMunicipio() != null) {
                           generarCSV = CodigoSeguroVerificacionHelper.incrustarCSVenJustificante(Integer.parseInt(fEF.getCodMunicipio().trim()), registroConf);
                       }
                        if (generarCSV) {
                            gVO.setAtributo(eC.getCodCampo() + "_METADATOS", metadatosFichero);
                        } else {
                            gVO.setAtributo(eC.getCodCampo() + "_METADATOS", null);
                        }
                        
                    }
                } else {
                    m_Log.debug("FICHERO ........................ VACIO ");
                    gVO.setAtributo(eC.getCodCampo(), null);
                    gVO.setAtributo(eC.getCodCampo() + "_NOMBRE", "");
                    gVO.setAtributo(eC.getCodCampo() + "_TIPO", "");
                    gVO.setAtributo(eC.getCodCampo() + "_METADATOS", null);
                }
               
            } else if (eC.getCodTipoDato().equals(tipoDatoDesplegable)) {
                m_Log.debug("Procesado DESPLEGABLE ... ");
                gVO.setAtributo(eC.getCodCampo(), request.getParameter("cod" + eC.getCodCampo()));
                m_Log.debug("DESPLEGABLE ........................ " + request.getParameter("cod" + eC.getCodCampo()));
			}else if (eC.getCodTipoDato().equals("3")){
                m_Log.debug("Busqueda del tipo FECHA:" + eC.getCodTipoDato());
                        
                m_Log.debug("Valor activar/desactivar:" + request.getParameter("activar" + eC.getCodCampo()));
                gVO.setAtributo(eC.getCampoActivo(), request.getParameter("activar" + eC.getCodCampo()));                
                gVO.setAtributo(eC.getCodCampo(), request.getParameter(eC.getCodCampo()));
            
            } else {
                gVO.setAtributo(eC.getCodCampo()+"_CODSEL", request.getParameter(eC.getCodCampo()+"_CODSEL"));            
                gVO.setAtributo(eC.getCodCampo(), request.getParameter(eC.getCodCampo()));
            }
        }
        gVO.setAtributo("estDatosSuplementarios", estructuraCamposFinal);
        res = DatosSuplementariosManager.getInstance().grabarDatosSuplementarios(estructuraCamposFinal,gVO,params);        
        return res;
    }
    
    
    
     private int grabarDatosSuplementariosFichero(FichaExpedienteForm fEF, GeneralValueObject gVO, HttpServletRequest request, String[] params) throws IOException {
        int res = 0;
        m_Log.info("FichaExpendienteAction::grabarDatosSuplementarios,fechaVencimiento");
        //Vector estructuraDatosSuplementarios = new Vector();
        //estructuraDatosSuplementarios = fEF.getEstructuraDatosSuplementarios();
        
        // Se obtiene la estructura de los campos suplementarios en formato JSON
        String jsonEstructuraCamposSuplementarios = fEF.getJsonEstructuraCamposSuplementarios();
        // Se parse el JSON con la estructura de los campos suplementarios
        Gson gson = new Gson();
        java.lang.reflect.Type tipo = new TypeToken<ResultadoTratamientoCargaParcialExpedienteVO>(){}.getType();
        ResultadoTratamientoCargaParcialExpedienteVO resultado =(ResultadoTratamientoCargaParcialExpedienteVO)gson.fromJson(jsonEstructuraCamposSuplementarios,tipo);
        
        
        ArrayList<EstructuraCampoAgrupado> estructuraCamposFinal = new ArrayList<EstructuraCampoAgrupado>();
        
        if(resultado!=null && resultado.getAgrupaciones()!=null && resultado.getAgrupaciones().size()>0){
            
            ArrayList<DefinicionAgrupacionCamposAgrupadosValueObject> agrupaciones = resultado.getAgrupaciones();                    
            for(int i=0;agrupaciones!=null && i<agrupaciones.size();i++){       
                
                if(agrupaciones.get(i).getEstructura()!=null)
                    estructuraCamposFinal.addAll(agrupaciones.get(i).getEstructura());                
            }            
        }
        
        ArrayList<GeneralValueObject> valoresDatosSuplementarios = new ArrayList<GeneralValueObject>();
        GeneralValueObject listaFicheros;
        listaFicheros = fEF.getListaFicheros();
        GeneralValueObject listaTiposFicheros;
        listaTiposFicheros = fEF.getListaTiposFicheros();
        Config m_Conf = ConfigServiceHelper.getConfig("common");
        String campo = "E_PLT.CodigoPlantillaFichero";
        String tipoDatoFichero = m_Conf.getString(campo);
        campo = "E_PLT.CodigoCampoDesplegable";
        String tipoDatoDesplegable = m_Conf.getString(campo);
        
        /** nuevo ***/
        GeneralValueObject listaEstadosFicheros = fEF.getListaEstadoFicheros();
        GeneralValueObject listaRutaFicherosNuevos = fEF.getListaRutaFicherosDisco();
        /** nuevo ***/
        
        //for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
        for (int i=0;i<estructuraCamposFinal.size();i++) {
        
            //EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
            EstructuraCampoAgrupado eC = (EstructuraCampoAgrupado) estructuraCamposFinal.get(i);
            m_Log.debug("CODIGO ... " + eC.getCodCampo());
            m_Log.debug("NOMBRE ... " + eC.getDescCampo());
            m_Log.debug("TIPO DATO ... " + eC.getCodTipoDato());
            eC.setCampoActivo("activar" + eC.getCodCampo());
            eC.setFechaVencimiento("fechaVencimiento" + eC.getCodCampo()) ;
            m_Log.debug("ACTIVAR/DESACTIVAR ... " + eC.getCampoActivo());

            
            if (eC.getCodTipoDato().equals(tipoDatoFichero)) { // 5 = FICHERO
                m_Log.debug("Procesado FICHERO ... ");
                if (listaFicheros.getAtributo(eC.getCodCampo()) != "") {
                    m_Log.debug("FICHERO ........................ NO VACIO ");
                    
                    
                    String tipoFichero = (String) listaTiposFicheros.getAtributo(eC.getCodCampo());
                    String aux = request.getParameter(eC.getCodCampo());
            
                    if(tipoFichero!=null && !"".equals(tipoFichero)){
                        // Si se puede recuperar el valor del fichero
                        String[] matriz = aux.split("/");
                        aux = matriz[matriz.length - 1];
                        gVO.setAtributo(eC.getCodCampo() + "_NOMBRE", aux);
                        gVO.setAtributo(eC.getCodCampo() + "_TIPO", tipoFichero);
                        
                        Integer estadoFichero = (Integer)listaEstadosFicheros.getAtributo(eC.getCodCampo());            
                        String rutaFicheroDisco = (String)listaRutaFicherosNuevos.getAtributo(eC.getCodCampo());
                        
                        gVO.setAtributo(eC.getCodCampo() + "_ESTADO",estadoFichero.toString());
                        gVO.setAtributo(eC.getCodCampo() + "_RUTA",rutaFicheroDisco);
                        
                    }
                } else {
                    m_Log.debug("FICHERO ........................ VACIO ");
                    gVO.setAtributo(eC.getCodCampo(), null);
                    gVO.setAtributo(eC.getCodCampo() + "_NOMBRE", "");
                    gVO.setAtributo(eC.getCodCampo() + "_TIPO", "");
                }
               
            }
            valoresDatosSuplementarios.add(gVO);
        }
        
        res = DatosSuplementariosManager.getInstance().grabarDatosSuplementariosFichero(estructuraCamposFinal,valoresDatosSuplementarios,params);        
        return res;
    }
    
    

 /**
     * Método que recupera de BBDD toda la información necesaria para cargar la ficha del expediente
     * @param expForm: Objeto de la clase FichaExpedienteForm con los datos del expediente
     * @param gVO: Objeto de la clase GeneralValueObject
     * @param params: Parámetros de conexión a la BDD
     * @param usuarioVO: Objeto de la clase UsuarioValueObject con los datos del usuario tramitador logueado
     * @param comprobarModoConsultaExpediente: True para comprobar si el expediente se tiene que mostrar en modo consulta o tramitación. False 
     *                                         en caso contrario
     * @return ArrayList<String> con la lista de errores
     */
    private ArrayList<String> cargarExpediente(FichaExpedienteForm expForm, GeneralValueObject gVO, String[] params, UsuarioValueObject usuarioVO) {

        AdaptadorSQLBD adapt = null;
        Connection con = null;
        ArrayList errores = new ArrayList<String>();
        boolean comprobarModoConsultaExpediente = false;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            Calendar calendario = null;
            
            if(("SI").equalsIgnoreCase((String)gVO.getAtributo("comprobarModuloConsultaExpediente"))) {                
                comprobarModoConsultaExpediente = true;
            }
            
            /**************************** NUEVO **********************************************/
            // Se recuperan los códigos de la unidades organizativas sobre las que tiene permiso el usuario tramitador.
            // Se ha modificado la consulta, puesto que se recuperaba información de más
            ArrayList<String> codUorsUsuario = UsuariosGruposDAO.getInstance().getListaCodigosUnidadesOrganicasUsuario(usuarioVO.getIdUsuario(),usuarioVO.getOrgCod(), con);             
            expForm.setCodigosUorsPermisoUsuario(codUorsUsuario);                 
              
            // Se recupera los tipos de documentos que pueden tener los terceros
            expForm.setListaTiposDocumentosTerceros(TipoDocumentosDAO.getInstance().getListaTipoDocumentos(con));
            
            // Se recupera la lista de roles del procedimiento al que pertenece el expediente
            expForm.setListaRoles(InteresadosDAO.getInstance().getListaRoles(usuarioVO.getOrgCod(),(String)gVO.getAtributo("codProcedimiento"),con));
           
            String expHistorico = (String)gVO.getAtributo("expHistorico"); 
            
            // Se comprueba si el expediente está en el histórico
            if (expHistorico == null){
                
                String numero = (String)gVO.getAtributo("numero");                
                expHistorico = (ExpedienteDAO.getInstance().estaExpedienteActivo(Integer.parseInt((String)gVO.getAtributo("codMunicipio")),                                       
                           Integer.parseInt((String) gVO.getAtributo("ejercicio")),numero,con))?"false":"true";
                    
                m_Log.debug(" =============> FichaExpedienteAction.do expHistorico: " + expHistorico);
            } 
             
            expForm.setExpHistorico("true".equals(expHistorico)?true:false);
            gVO.setAtributo("expHistorico", expHistorico);
            
            
            /**************************** NUEVO **********************************************/
            
            gVO = FichaExpedienteDAO.getInstance().cargaExpediente(gVO, adapt, con);            
            
             /****SE COMPRUEBA SI EL EXPEDIENTE SE MUESTRA EN MODO TRAMITACION O EN MODO CONSULTA  *****/
            if (comprobarModoConsultaExpediente) {
                
                
                ConsultaExpedientesValueObject consExpVO = new ConsultaExpedientesValueObject();
                consExpVO.setNumeroExpediente((String)gVO.getAtributo("numero"));
                consExpVO.setExpHistorico("true".equals(expHistorico)?true:false);
                
                boolean esPendiente = false;
                
                String desdeConsulta = (String)gVO.getAtributo("desdeConsulta");                
                if(desdeConsulta!=null && !"".equals(desdeConsulta) && desdeConsulta.equalsIgnoreCase("SI")) { 
                    // Se comprueba si el expediente está pendiente, y si el usuario tiene permiso sobre la unidad de inicio
                    // del expediente, o sobre alguna de las unidades tramitadoras de alguno de los trámites
                    esPendiente = ConsultaExpedientesDAO.getInstance().esPendienteParaUsuario(usuarioVO, consExpVO, con);                    
                }
                    
                
                int estado  = Integer.parseInt(gVO.getAtributo("estado").toString());
                
                boolean permiteModificarObs = false;
                // Si el expediente está finalizado o anulado, se comprueba si el usuario tramitador tiene permiso
                // para poder modificar las observaciones del expediente. Si el expediente está pendiente no se hace dicha consulta                
                if(estado==ConstantesDatos.ESTADO_EXPEDIENTE_FINALIZADO || estado==ConstantesDatos.ESTADO_EXPEDIENTE_ANULADO)
                    permiteModificarObs= ConsultaExpedientesDAO.getInstance().permiteModificarObservacionesUsuario(usuarioVO, consExpVO, con);

                
                if (esPendiente) {
                    gVO.setAtributo("modoConsulta", "no");
                }
                
                if ((!esPendiente)&&(permiteModificarObs)) {
                        gVO.setAtributo("permiteModificarObs", "si");
                }
                
            }// if
            
            expForm.setExpedienteVO(gVO);
            expForm.setUbicacionDoc((String) gVO.getAtributo("ubicacionDoc"));
            /********************** NUEVO ********************************/
            
            
            /* Miramos si tiene exp relacionados, y en base a eso mostraremos el botón
             * de exp relacionados de un color u otro
             */
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");  
                        
            boolean expedientesRelacionados = FichaExpedienteDAO.getInstance().tieneExpedientesRelacionados(codMunicipio, ejercicio, numero, con);
            gVO.setAtributo("expedientesRelacionados", expedientesRelacionados);
            
            ArrayList<InteresadoExpedienteVO> lTerceros = InteresadosDAO.getInstance().getListaInteresadosExpediente(gVO,adapt,con);
            expForm.setTerceros(lTerceros);
           
            Vector tramites = FichaExpedienteDAO.getInstance().cargaTramites(gVO,adapt,con,false);
            expForm.setTramites(tramites);
            
            //Vector permisosTramites = FichaExpedienteDAO.getInstance().cargaPermisosTramites(tramites,gVO, con);
            //expForm.setPermisosTramites(permisosTramites);

            gVO.setAtributo("desdeJsp", "si");

            Vector estructuraDatosSuplementarios = new Vector();
            expForm.setEstructuraDatosSuplementarios(estructuraDatosSuplementarios);
          
            
            Vector valoresDatosSuplementarios = new Vector();
            expForm.setValoresDatosSuplementarios(valoresDatosSuplementarios);            
            
            expForm.setListaAgrupacionesCampos(new Vector());
            
            expForm.setFormularios(cargarFormularios(gVO, usuarioVO));                      
            expForm.setNotificacionRealizada((String) gVO.getAtributo("notificacionRealizada"));

            ArrayList<ModuloIntegracionExterno> modulos = ModuloIntegracionExternoFactoria.getInstance().getImplClassModuloConPantallaExpediente(Integer.parseInt(codMunicipio), (String)gVO.getAtributo("codProcedimiento"), false,usuarioVO.getIdUsuario(),params);
            
            m_Log.debug("NUMERO DE MÓDULOS RECUPERADOS: " + modulos.size());
            expForm.setModulosExternos(modulos);
           
                        
        }catch(Exception e){
            e.printStackTrace();            
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                m_Log.error("Error al ceerrar la conexión a la BBDD: " + e.getMessage());
            }
            
        }
        
        return errores;
    }

    private void cargarTramitesDisponibles(FichaExpedienteForm expForm, GeneralValueObject gVO, String[] params) {

        Vector tramitesDisponibles;
        tramitesDisponibles = FichaExpedienteManager.getInstance().cargaTramitesDisponibles(gVO, params);
        expForm.setTramitesDisponibles(tramitesDisponibles);
    }
    
    private Vector listaTemasSeleccionados(String listTemasSelecc) {
        Vector lista = new Vector();
        StringTokenizer valores;
        if (listTemasSelecc != null) {
            valores = new StringTokenizer(listTemasSelecc, "§¥", false);
            while (valores.hasMoreTokens()) {
                String valor = valores.nextToken();
                lista.addElement(valor);
            }
        }
        return lista;
    }
    
    public boolean notificar(UsuarioValueObject usuario, GeneralValueObject gVO) {
        boolean resultado = true;
        try {

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("ENTRA EN NOTIFICAR DE FICHAEXPEDIENTEACTION");
            }
            EstructuraNotificacion estructuraNotificacion = (EstructuraNotificacion) gVO.getAtributo("mailsUsuariosAlIniciar");
            Config m_ConfigApplication = ConfigServiceHelper.getConfig("techserver");
            String asunto = m_ConfigApplication.getString("mail.subject");
            String contenido;
            Vector emailsUOR;
            Vector emailsUsusUOR;
            Vector emailsInteresados;
            String emailUsuarioInicioTramite;
            String emailUsuarioInicioExped;
            MailHelper mailHelper = new MailHelper();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("UNIDAD TRAMITADORA DE tramite q se inicia en NOTIFICAR: " + gVO.getAtributo("nombreUORTramiteIniciado"));
            }

            String asuntoExp =  (String) gVO.getAtributo("asunto");
            if (asuntoExp==null || asuntoExp.equals("")) asuntoExp="Sin asunto";
            else asuntoExp = StringEscapeUtils.unescapeJavaScript(asuntoExp);

            /*Comprobamos si tenemos una variable que contenga la descripción del procedimiento. En caso de que
             * no la tengamos, ponemos el código.*/
            String procedimiento ="";
            if (((String) gVO.getAtributo("desProcedimiento")!=null) &&
                    (!((String)gVO.getAtributo("desProcedimiento")).equals("")))
                procedimiento = (String) gVO.getAtributo("desProcedimiento");
            else procedimiento = (String) gVO.getAtributo ("codProcedimiento");


            // Reemplazos de campos en el asunto y el contenido del mensaje
            emailsUOR = estructuraNotificacion.getListaEMailsUOR();
            emailsUsusUOR = estructuraNotificacion.getListaEMailsUsusUOR();
            emailsInteresados = estructuraNotificacion.getListaEMailsInteresados();
            emailUsuarioInicioTramite = estructuraNotificacion.getListaEmailsUsuInicioTramite();
            emailUsuarioInicioExped = estructuraNotificacion.getListaEmailsUsuInicioExped();
            asunto = asunto.replaceAll("@expediente@", (String) gVO.getAtributo("numero"));

            //Notificaciones para la unidad orgánica
            for (int j = 0; j < emailsUOR.size(); j++) {
                contenido = m_ConfigApplication.getString("mail.contentTramiteInicioUOR");
                contenido = contenido.replaceAll("@usuario@", usuario.getNombreUsu());
                contenido = contenido.replaceAll("@expediente@", (String) gVO.getAtributo("numero"));
                contenido = contenido.replaceAll("@tramite@", estructuraNotificacion.getNombreTramite());
                contenido = contenido.replaceAll("@asunto@", asuntoExp);
                contenido = contenido.replaceAll("@procedimiento@", procedimiento);
                contenido = contenido.replaceAll("@unidadTramitadora@",  (String) gVO.getAtributo("nombreUORTramiteIniciado"));

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("PLAZO TRÁMITE : " + gVO.getAtributo("plazo"));
                }
                if (gVO.getAtributo("plazo") != null) {
                    if (!gVO.getAtributo("plazo").equals("")) {
                        if (gVO.getAtributo("tipoPlazo").equals("H")) {
                            contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoH");
                        } else if (gVO.getAtributo("tipoPlazo").equals("N")) {
                            contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoN");
                        } else if (gVO.getAtributo("tipoPlazo").equals("M")) {
                            contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoM");
                        }
                        contenido = contenido.replaceAll("@plazo@", (String) gVO.getAtributo("plazo"));
                    }
                }
                mailHelper.sendMail((String) emailsUOR.elementAt(j), asunto, contenido);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MAIL DE TRAMITE DE INICIO . CONTENIDO " + contenido);
                }
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MAIL DE TRAMITE DE INICIO ENVIADO A " + emailsUOR.elementAt(j));
                }
            }

            //Notificaciones para los usuarios de la unidad orgánica
            for (int j = 0; j < emailsUsusUOR.size(); j++) {
                contenido = m_ConfigApplication.getString("mail.contentTramiteInicioUsusUOR");
                contenido = contenido.replaceAll("@expediente@", (String) gVO.getAtributo("numero"));
                contenido = contenido.replaceAll("@unidadTramitadora@", (String) gVO.getAtributo("nombreUORTramiteIniciado"));
                contenido = contenido.replaceAll("@tramite@", estructuraNotificacion.getNombreTramite());
                contenido = contenido.replaceAll("@asunto@", asuntoExp);
                contenido = contenido.replaceAll("@procedimiento@", procedimiento);

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("PLAZO TRÁMITE : " + gVO.getAtributo("plazo"));
                }
                if (gVO.getAtributo("plazo") != null) {
                    if (!gVO.getAtributo("plazo").equals("")) {
                        if (gVO.getAtributo("tipoPlazo").equals("H")) {
                            contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoH");
                        } else if (gVO.getAtributo("tipoPlazo").equals("N")) {
                            contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoN");
                        } else if (gVO.getAtributo("tipoPlazo").equals("M")) {
                            contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoM");
                        }
                        contenido = contenido.replaceAll("@plazo@", (String) gVO.getAtributo("plazo"));
                    }
                }
                mailHelper.sendMail((String) emailsUsusUOR.elementAt(j), asunto, contenido);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MAIL DE TRAMITE DE INICIO . CONTENIDO " + contenido);
                }
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MAIL DE TRAMITE DE INICIO ENVIADO A " + emailsUsusUOR.elementAt(j));
                }
            }

            //Notificaciones para el interesado. Debemos comprobar si existe, ya que solo se dará este caso
            //siendo el trámite de inicio cuando se llegue por un servicio web. En ese caso, el gVO tendrá un
            //atributo llamado "notificarInteresados" con valor "si".
            if ("si".equals((String)gVO.getAtributo("notificarInteresados"))){
                        for (int j = 0; j < emailsInteresados.size(); j++) {
                contenido = m_ConfigApplication.getString("mail.contentTramiteInicioInteresados");
                contenido = contenido.replaceAll("@expediente@", (String) gVO.getAtributo("numero"));
                contenido = contenido.replaceAll("@unidadTramitadora@", (String) gVO.getAtributo("nombreUORTramiteIniciado"));
                contenido = contenido.replaceAll("@tramite@", estructuraNotificacion.getNombreTramite());
                contenido = contenido.replaceAll("@asunto@", asuntoExp);
                contenido = contenido.replaceAll("@procedimiento@", procedimiento);

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("PLAZO TRÁMITE : " + gVO.getAtributo("plazo"));
                }
                if (gVO.getAtributo("plazo") != null) {
                    if (!gVO.getAtributo("plazo").equals("")) {
                        if (gVO.getAtributo("tipoPlazo").equals("H")) {
                            contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoH");
                        } else if (gVO.getAtributo("tipoPlazo").equals("N")) {
                            contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoN");
                        } else if (gVO.getAtributo("tipoPlazo").equals("M")) {
                            contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoM");
                        }
                        contenido = contenido.replaceAll("@plazo@", (String) gVO.getAtributo("plazo"));
                    }
                }
                mailHelper.sendMail((String) emailsInteresados.elementAt(j), asunto, contenido);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MAIL DE TRAMITE DE INICIO . CONTENIDO " + contenido);
                }
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MAIL DE TRAMITE DE INICIO ENVIADO A " + emailsInteresados.elementAt(j));
                }
            }
        }

            //Notificamos al Usuario que ha iniciado el Tramite
            if (emailUsuarioInicioTramite != null && !emailUsuarioInicioTramite.equals("")) {
                contenido = m_ConfigApplication.getString("mail.contentTramiteInicioUsusUOR");
                contenido = contenido.replaceAll("@expediente@", (String) gVO.getAtributo("numero"));
                contenido = contenido.replaceAll("@unidadTramitadora@", (String) gVO.getAtributo("nombreUORTramiteIniciado"));
                contenido = contenido.replaceAll("@tramite@", estructuraNotificacion.getNombreTramite());
                contenido = contenido.replaceAll("@asunto@", asuntoExp);
                contenido = contenido.replaceAll("@procedimiento@", procedimiento);

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("PLAZO TR?MITE : " + gVO.getAtributo("plazo"));
                }

                if (gVO.getAtributo("plazo") != null) {
                    if (!gVO.getAtributo("plazo").equals("")) {
                        if (gVO.getAtributo("tipoPlazo").equals("H")) {
                            contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoH");
                        } else if (gVO.getAtributo("tipoPlazo").equals("N")) {
                            contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoN");
                        } else if (gVO.getAtributo("tipoPlazo").equals("M")) {
                            contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoM");
                        }

                        contenido = contenido.replaceAll("@plazo@", (String) gVO.getAtributo("plazo"));
                    }
                }

                mailHelper.sendMail((String) emailUsuarioInicioTramite, asunto, contenido);

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MAIL DE TRAMITE DE INICIO . CONTENIDO " + contenido);
                }

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MAIL DE TRAMITE DE INICIO ENVIADO A " + emailUsuarioInicioTramite);
                }
            }

            //Notificamos al Usuario que ha iniciado el Expediente
            if (emailUsuarioInicioExped != null && !emailUsuarioInicioExped.equals("")) {
                contenido = m_ConfigApplication.getString("mail.contentTramiteInicioUsusUOR");
                contenido = contenido.replaceAll("@expediente@", (String) gVO.getAtributo("numero"));
                contenido = contenido.replaceAll("@unidadTramitadora@", (String) gVO.getAtributo("nombreUORTramiteIniciado"));
                contenido = contenido.replaceAll("@tramite@", estructuraNotificacion.getNombreTramite());
                contenido = contenido.replaceAll("@asunto@", asuntoExp);
                contenido = contenido.replaceAll("@procedimiento@", procedimiento);

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("PLAZO TR?MITE : " + gVO.getAtributo("plazo"));
                }

                if (gVO.getAtributo("plazo") != null) {
                    if (!gVO.getAtributo("plazo").equals("")) {
                        if (gVO.getAtributo("tipoPlazo").equals("H")) {
                            contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoH");
                        } else if (gVO.getAtributo("tipoPlazo").equals("N")) {
                            contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoN");
                        } else if (gVO.getAtributo("tipoPlazo").equals("M")) {
                            contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoM");
                        }
                        contenido = contenido.replaceAll("@plazo@", (String) gVO.getAtributo("plazo"));
                    }
                }

                mailHelper.sendMail((String) emailUsuarioInicioExped, asunto, contenido);

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MAIL DE TRAMITE DE INICIO . CONTENIDO " + contenido);
                }

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("MAIL DE TRAMITE DE INICIO ENVIADO A " + emailUsuarioInicioExped);
                }
            }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SALE DE NOTIFICAR DE FICHAEXPEDIENTEACTION");
            }
        } catch (MailServiceNotActivedException e) {
            m_Log.debug("Servicio de mail no activado");
            //Servicio de mail no activado, funcionamiento normal
            resultado = false;
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error(e.getMessage());
            }
            resultado = false;
        }
        return resultado;
    }

    private Collection cargarFormularios(GeneralValueObject gVO, UsuarioValueObject usuarioVO) {
        Collection rdo = new ArrayList();
        Collection sal = new ArrayList();
        try{
        if ("si".equals(m_Config.getString("JSP.Formularios"))) {
            //llamar a la fachada de formularios
            FormularioFacade facade = new FormularioFacade((String)gVO.getAtributo("codOrganizacion"));
            rdo = facade.getFormsOfExpediente((String) gVO.getAtributo("numero"),
                    (String)gVO.getAtributo("codOrganizacion"));
        }
        }catch(InternalErrorException e){
            e.printStackTrace();
            m_Log.debug("***** Fallo al cargar Formularios " + e.getMessage());
        }

        Iterator it = rdo.iterator();

        while (it.hasNext()) {
            FormularioTramitadoVO formVO = (FormularioTramitadoVO) it.next();
            GeneralValueObject genVO = new GeneralValueObject();

            //	genVO.setAtributo("codigo",Integer.toString(UsuarioManager.getInstance().getGrupo(usuarioVO)));
            genVO.setAtributo("codigo", formVO.getCodigo());
            genVO.setAtributo("descripcion", formVO.getDescripcion());
            genVO.setAtributo("fecMod", DateOperations.toString(formVO.getFecMod(), "dd-MM-yyyy"));
            genVO.setAtributo("tipo", formVO.getTipo());
            genVO.setAtributo("estado", formVO.getEstado());
            genVO.setAtributo("usuario", UsuarioManager.getInstance().getNombre(formVO.getUsuario()));

            try{
                if ("si".equals(m_Config.getString("JSP.Formularios"))) {
                    //llamar a la fachada de formularios
                    FormularioFacade facade = new FormularioFacade();
                    Collection rdo1 = new ArrayList();
                    rdo1 = facade.getFicherosOfForm((String)formVO.getCodigo());
                    if(rdo1.size()>0)
                    {
                       genVO.setAtributo("tieneAnexo", "si");

                    }else
                    {
                       genVO.setAtributo("tieneAnexo", "no");
                    }
                }
            }catch(InternalErrorException e){

                m_Log.debug("***** Fallo al cargar Formularios " + e.getMessage());
            }



            sal.add(genVO);
        }

        return sal;
    }

    private boolean tieneFormularios(GeneralValueObject gVO) {
        int codMunicipio = Integer.parseInt((String) gVO.getAtributo("codMunicipio"));
        String codProc = (String) gVO.getAtributo("codProcedimiento");
        int ejercicio = Integer.parseInt((String) gVO.getAtributo("ejercicio"));
        String numExp = (String) gVO.getAtributo("numero");
        int codTram = Integer.parseInt((String) gVO.getAtributo("codTramiteRetroceder"));
        int ocurTram = Integer.parseInt((String) gVO.getAtributo("ocurrenciaTramiteRetroceder"));

        try {
            if ("si".equals(m_Config.getString("JSP.Formularios"))) {
                FormularioFacade facade = new FormularioFacade();
                TramiteTramitadoKey infoTramite = new TramiteTramitadoKey(codMunicipio, codProc, ejercicio, numExp, codTram, ocurTram);
                Collection res = facade.getFormsOfTramite(infoTramite);
                return res.size() > 0;
            } else {
                return false;
            }

        } catch (InternalErrorException iee) {
            m_Log.debug("***** Error al intentar cargar formularios");
            return true;
        }
    }

    /**
     * Pone en el VO el mensaje adecuado con el error que devuelve un servicio
     * web en una WSException
     * @param expForm
     * @param wse
     */
    private void ponerMensajeFalloSW(FichaExpedienteForm expForm, EjecucionSWException eswe) {
        String msg = "Fallo en ejecución de servicio web obligatorio. ";
        expForm.setMensajeSW(msg + eswe.getMensaje());
    }

    private String preparaTexto(String cad, String opcion) {
        String resul = "";
        String[] aux = null;        
        aux = cad.trim().split("\r\n");
        for (int j = 0; j < aux.length; j++) {            
            resul = resul + "<" + opcion + "><linea>";
            String texto="";
            texto=aux[j].replace("&", "&amp;");
            texto=(texto.replace("<", "&lt;")).replace(">", "&gt;");
            resul = resul + texto;
            resul = resul + "</linea></" + opcion + ">";
        }
        return resul;
    }
    
    
    private String crearXML(FichaExpedienteForm expForm,UsuarioValueObject usuarioVO,String[] params) throws AnotacionRegistroException {
        StringBuffer textoXml = new StringBuffer("");
        textoXml.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        textoXml.append("<procedimiento>");

        //DATOS GENERALES
        textoXml.append("<numero>").append(expForm.getNumExpediente()).append("</numero>");
        textoXml.append("<procedimiento>").append(expForm.getProcedimiento()).append("</procedimiento>");
        
        GeneralValueObject gvo = InteresadosManager.getInstance().getDocumentosNombresInteresadosImpresionExpediente(expForm.getNumExpediente(), params);                
        String nombres    = (String)gvo.getAtributo("nombres");
        String documentos = (String)gvo.getAtributo("documentos");
        
        if(nombres==null || "".equals(nombres)){
            textoXml.append("<interesado>--</interesado>");
        } else {
            textoXml.append("<interesado>").append(nombres).append("</interesado>");
        }       
        
        
        if(documentos==null || "".equals(documentos)){
            textoXml.append("<documento>--</documento>");
        } else {
            textoXml.append("<documento>").append(documentos).append("</documento>");
        }
         
        if (expForm.getLocalizacion() == null || expForm.getLocalizacion().equals("")) {
            textoXml.append("<localizacion>--</localizacion>");
        } else {
            textoXml.append("<localizacion>").append(expForm.getLocalizacion()).append("</localizacion>");
        }
        // #253692
        if (expForm.getUbicacionDoc()!= null && !expForm.getUbicacionDoc().equals("")) {
            textoXml.append("<ubicacionDoc>").append(expForm.getUbicacionDoc()).append("</ubicacionDoc>");
        }
        textoXml.append("<fechaInicio>").append(expForm.getFechaInicio()).append("</fechaInicio>");
        if (expForm.getFechaFin() == null || expForm.getFechaFin().equals("")) {
            textoXml.append("<fechaFin>").append("--").append("</fechaFin>");
        } else {
            textoXml.append("<fechaFin>").append(expForm.getFechaFin()).append("</fechaFin>");
        }
        textoXml.append("<usuario>").append(expForm.getUsuario()).append("</usuario>");
        textoXml.append("<unidadInicio>").append(expForm.getDescUnidadOrganicaExp()).append("</unidadInicio>");

        if (expForm.getAsunto() == null || expForm.getAsunto().equals("")) {
            expForm.setAsunto("--");
        }
        if (expForm.getObservaciones() == null || expForm.getObservaciones().equals("")) {
            expForm.setObservaciones("--");
        }
        textoXml.append(preparaTexto(expForm.getAsunto(), "asunto"));
        textoXml.append(preparaTexto(expForm.getObservaciones(), "observaciones"));

        ConsultaExpedientesValueObject consExpVO = new ConsultaExpedientesValueObject();
        consExpVO.setCodMunicipio(expForm.getCodMunicipio());
        consExpVO.setEjercicio(expForm.getEjercicio());
        consExpVO.setNumeroExpediente(expForm.getNumExpediente());
        Vector expedientesRelacionados = ConsultaExpedientesManager.getInstance().getExpedientesRelacionados(consExpVO, params);

        if (expedientesRelacionados.size() != 0) {
            for (int i = 0; i < expedientesRelacionados.size(); i++) {
                textoXml.append("<expedientesRelacionados>");
                ConsultaExpedientesValueObject cEVO = new ConsultaExpedientesValueObject();
                cEVO = (ConsultaExpedientesValueObject) expedientesRelacionados.get(i);
                String numExp = cEVO.getNumeroExpedienteRel();
                textoXml.append("<expRel>").append(numExp).append("</expRel>");
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("EXPEDIENTES RELACIONADOS " + numExp);
                }
                textoXml.append("</expedientesRelacionados>");
            } 
        } else {
            textoXml.append("<expedientesRelacionados><expRel>--</expRel></expedientesRelacionados>");
        }
        
        
        
        /***************** prueba *********************************************/
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        ArrayList<AsientoFichaExpedienteVO> listaAsientos = null;
        ArrayList<DefinicionAgrupacionCamposAgrupadosValueObject> agrupaciones = null;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("numero",expForm.getNumExpediente());
            gVO.setAtributo("codMunicipio",expForm.getCodMunicipio());
            gVO.setAtributo("expHistorico",Boolean.toString(expForm.isExpHistorico()));
                               
            FichaExpedienteDAO.getInstance().getRegistroRelacionado(gVO,con);
            HashMap resultadoConsulta = VisorRegistroManager.getInstance().cargaListaAsientosExpediente(gVO, usuarioVO, params);                
            listaAsientos = (ArrayList<AsientoFichaExpedienteVO>) resultadoConsulta.get("resultados");
            
            DatosExpedienteVO dato = new DatosExpedienteVO();
            dato.setCodOrganizacion(usuarioVO.getOrgCod());
            dato.setEjercicio(Integer.parseInt(expForm.getEjercicio()));
            dato.setNumExpediente(expForm.getNumExpediente());
            dato.setCodProcedimiento(expForm.getCodProcedimiento());
            dato.setCodTramite(null);
            dato.setDesdeJsp(ConstantesDatos.DESDE_JSP_FICHA_EXPEDIENTE);
            dato.setConsultaCampos(null);
            dato.setExpHistorico(expForm.isExpHistorico()) ;
                        
            agrupaciones = FichaExpedienteDAO.getInstance().cargaEstructuraAgrupacionCampos(dato,expForm.getCodProcedimiento(), adapt,con,params);
            
            
            for(int w = 0; agrupaciones!=null && w<agrupaciones.size(); w++){            
                DefinicionAgrupacionCamposAgrupadosValueObject agrupacion = agrupaciones.get(w);                
                String codAgrupacion = agrupacion.getCodAgrupacion();
            
                textoXml.append("<agrupacion>");
                textoXml.append("<nombreAgrupacion>");
                textoXml.append(agrupacion.getDescAgrupacion());
                textoXml.append("</nombreAgrupacion>");

                ArrayList<EstructuraCampoAgrupado> estructura = agrupacion.getEstructura();
                
                for (int k = 0; estructura!=null && k<estructura.size(); k++) {                                      
                    EstructuraCampoAgrupado estructuraCampo = estructura.get(k);
                    String valor = "";
                    
                        if (estructuraCampo.getCodTramite() == null) {
                            textoXml.append("<datosExpediente>");
                            textoXml.append("<nombreDE>").append(estructuraCampo.getRotulo()).append("</nombreDE>");
                            
                            ValorCampoSuplementarioVO valorCampo = estructuraCampo.getValorCampo();
                            
                            valor = valorCampo.getValorDatoSuplementario();                            
                            if (valor == null || valor.equals("")) {
                                valor = "--";
                            }
                            
                            if (valor!= null) {  
                                if (estructuraCampo.getCodTipoDato().equals(m_Config.getString("E_PLT.CodigoCampoDesplegable"))) {
                                    Vector codDespl = estructuraCampo.getListaCodDesplegable();
                                    Vector descDespl = estructuraCampo.getListaDescDesplegable();
                                    for (int l = 0; l < codDespl.size(); l++) {
                                        //Primero le quito las comillas al valor del vector
                                        String aux = codDespl.get(l).toString().substring(1, codDespl.get(l).toString().length() - 1);
                                        if (aux.equals(valorCampo.getValorDatoSuplementario())) {
                                            //Primero le quito las comillas al valor del vector
                                            String aux2 = descDespl.get(l).toString().substring(1, descDespl.get(l).toString().length() - 1);
                                             if(aux2.indexOf("|")!=-1){	
                                                String[]auxValores=aux2.split(Pattern.quote("|"));	
                                                if(usuarioVO.getIdioma()!=1){	
                                                    aux2=auxValores[1];	
                                                }else{	
                                                    aux2=auxValores[0];	
                                                }	
                                            }
                                            textoXml.append(preparaTexto(valor + " : " + aux2, "valorDE"));
                                            break;
                                        }
                                    }
                                } else {
                                    textoXml.append(preparaTexto(valor, "valorDE"));
                                }
                            } else {
                                textoXml.append("<valorDE>").append("--").append("</valorDE>");
                            }

                            if(estructuraCampo.getPosX()!=null){
                                String coordenadaX=estructuraCampo.getPosX();
                                String coordenadaY=estructuraCampo.getPosX();
                                textoXml.append("<posX>");
                                textoXml.append(coordenadaX);
                                textoXml.append("</posX>");
                                textoXml.append("<posY>");
                                textoXml.append(coordenadaY);
                                textoXml.append("</posY>");

                            }else{
                               textoXml.append("<posX>");
                               textoXml.append("</posX>");
                               textoXml.append("<posY>");
                               textoXml.append("</posY>");
                            }
                            
                            textoXml.append("</datosExpediente>");
                        }                    
                }
                textoXml.append("</agrupacion>");
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        
        DefinicionAgrupacionCamposAgrupadosValueObject agrupacionCamposTramites = null;
        // Nos quedamos con la agrupación de campos suplementarios definidos a nivel de trámite
        for(int i=0;agrupaciones!=null && i<agrupaciones.size();i++){
            if(agrupaciones.get(i).getCodAgrupacion()!=null && "DVT".equalsIgnoreCase(agrupaciones.get(i).getCodAgrupacion())){
                agrupacionCamposTramites = agrupaciones.get(i);
                break;
            }// if
        }// for
               
        
        
        /***************** prueba *********************************************/        
        Vector tramites = expForm.getTramites();        
        //TRÁMITES
        for (int i = tramites.size() - 1; i >= 0; i--) {
            textoXml.append("<tramite>");
            GeneralValueObject tramiteVO = new GeneralValueObject();
            tramiteVO = (GeneralValueObject) tramites.elementAt(i);

            textoXml.append("<nombreTramite>").append(tramiteVO.getAtributo("tramite")).append("</nombreTramite>");
            textoXml.append("<fechaInicioTramite>").append(tramiteVO.getAtributo("fehcaInicio")).append("</fechaInicioTramite>");
            //Necesitamos para el usuario obtener Su nombre completo
            String nombreUsuario="";
            String codUsuario=(String) tramiteVO.getAtributo("codUsuarioFinalizacion");
           
            if((codUsuario!=null) && (!"".equals(codUsuario))){
              nombreUsuario=dameNombreUsuario(codUsuario,params);
            }
            textoXml.append("<usuarioTramite>").append(nombreUsuario).append("</usuarioTramite>");
            if (tramiteVO.getAtributo("fechaFin") == null || tramiteVO.getAtributo("fechaFin").equals("")) {
                textoXml.append("<fechaFinTramite>").append("").append("</fechaFinTramite>");
            } else {
                textoXml.append("<fechaFinTramite>").append(tramiteVO.getAtributo("fechaFin")).append("</fechaFinTramite>");
            }
            textoXml.append("<unidadTramite>").append(tramiteVO.getAtributo("unidad")).append("</unidadTramite>");
            textoXml.append("<clasificacionTramite>").append(tramiteVO.getAtributo("clasificacion")).append("</clasificacionTramite>");

            
            
            
            ArrayList<EstructuraCampoAgrupado> estructuraCamposTramite = agrupacionCamposTramites.getEstructura();            
            for(int k = 0; estructuraCamposTramite!=null && k<estructuraCamposTramite.size(); k++) {              
                EstructuraCampoAgrupado estructura = estructuraCamposTramite.get(k);
                if ((tramiteVO.getAtributo("codTramite")).equals(estructura.getCodTramite()) && // #238429: SE AÑADE LA COMPARACIÓN DE OCURRENCIAS
                        (tramiteVO.getAtributo("ocurrenciaTramite")).equals(estructura.getOcurrencia())) {
                    textoXml.append("<campoSuplementario>");
                    textoXml.append("<nombreCS>").append(estructura.getRotulo()).append("</nombreCS>");
                    String valor = estructura.getValorCampo().getValorDatoSuplementario();
                    if (valor == null || valor.equals("")) {
                        valor = "--";
                    }
                    if (valor != null) {
                        if (estructura.getCodTipoDato().equals(m_Config.getString("E_PLT.CodigoCampoDesplegable"))) {
                            Vector codDespl = estructura.getListaCodDesplegable();
                            Vector descDespl = estructura.getListaDescDesplegable();
                            for (int l = 0; l < codDespl.size(); l++) {
                                //Primero le quito las comillas al valor del vector
                                String aux = codDespl.get(l).toString().substring(1, codDespl.get(l).toString().length() - 1);
                                if (aux.equals(valor)) {
                                    //Primero le quito las comillas al valor del vector
                                    String aux2 = descDespl.get(l).toString().substring(1, descDespl.get(l).toString().length() - 1);
                                    textoXml.append(preparaTexto(valor + " : " + aux2, "valorCS"));
                                    break;
                                }
                            }
                        } else {
                            textoXml.append(preparaTexto(valor, "valorCS"));
                        }
                    } else {
                        textoXml.append("<valorCS>").append("").append("</valorCS>");
                    }
                    textoXml.append("</campoSuplementario>");
                }
            }// for             
            textoXml.append("</tramite>");
        }
        
        
        // SE AÑADEN LAS ANOTACIONES DE REGISTRO ASOCIADAS CON EL EXPEDIENTE
        for (int i=0; listaAsientos!=null && i< listaAsientos.size(); i++ ) {
            textoXml.append("<registro>");
            AsientoFichaExpedienteVO asiento = listaAsientos.get(i);
            String eN = asiento.getIdAsiento();
            textoXml.append("<numRegistro>").append( eN).append("</numRegistro>");
            String tR = asiento.getTipoAsiento();
            textoXml.append("<tipoRegistro>").append( tR).append("</tipoRegistro>");
            String fA = asiento.getFechaAsiento();
            textoXml.append("<fechaRegistro>").append( fA).append("</fechaRegistro>");
            String r = asiento.getNombreCompleto();
            textoXml.append("<remitenteRegistro>").append( r).append("</remitenteRegistro>");
            String a = asiento.getAsuntoAsiento();
            textoXml.append("<asuntoRegistro>").append( a).append("</asuntoRegistro>");
            textoXml.append("</registro>");
        }
      
        textoXml.append("</procedimiento>");
        return textoXml.toString();
    }
    
    
   
    
    
    /* *************************************************************************************
    Función que inicializa las listas del formulario:
    tipo de documentos,
    tipo de remitentes,
    tipo de transportes,
    tipo de actuaciones, y
    tipo de temas.
     ************************************************************************************* */
    private void inicializarListas(RegistroValueObject elRegistroESVO, String[] params)
            throws AnotacionRegistroException {

        elRegistroESVO.setListaTiposDocumentos(AnotacionRegistroManager.getInstance().getListaTiposDocumentos(params));
        elRegistroESVO.setListaTiposRemitentes(AnotacionRegistroManager.getInstance().getListaTiposRemitentes(params));
        elRegistroESVO.setListaTiposTransportes(AnotacionRegistroManager.getInstance().getListaTiposTransportes(params));
        elRegistroESVO.setListaTemas(AnotacionRegistroManager.getInstance().getListaTemas(params));
        elRegistroESVO.setListaTiposIdInteresado(AnotacionRegistroManager.getInstance().getListaTiposIdInteresado(params));
        elRegistroESVO.setListaDepartamentos(AnotacionRegistroManager.getInstance().getListaDepartamentos(params));
        elRegistroESVO.setListaActuaciones(AnotacionRegistroManager.getInstance().getListaActuaciones(params));
        elRegistroESVO.setListaOrganizacionesExternas(MantRegistroExternoManager.getInstance().loadOrganizacionesExternas(params));
    }

     /**
     *  Obtiene la lista de roles correspondientes al código y municipio de procedimiento que se pasan
     *  en el GeneralValueObject. Los roles del procedimiento traen de "valor por defecto" 1 y 0
     *  que se traducen a SI y NO.
     *
     *  @returns Un Vector de GeneralValueObject con los roles del procedimiento.
     */
    private Vector<GeneralValueObject> obtenerRolesProcedimiento(GeneralValueObject procedimiento, String[] params){

        Vector<GeneralValueObject> rolesProc = InteresadosManager.getInstance().getListaRoles(procedimiento, params);
        for (GeneralValueObject rolProc : rolesProc) {
            rolProc.setAtributo("porDefecto", rolProc.getAtributo("porDefecto").equals("1") ? "SI" : "NO");
        }
        return rolesProc;
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

    private Vector<GeneralValueObject> getListaRoles(RegistroValueObject elRegistroESVO, String[] params)
    {
        String proc = elRegistroESVO.getCodProcedimientoRoles();
        Vector<GeneralValueObject> roles = new Vector<GeneralValueObject>();
        if (proc != null && !proc.equals("") && !proc.equals("null")) {
            GeneralValueObject procGVO = new GeneralValueObject();
            procGVO.setAtributo("codMunicipio", elRegistroESVO.getMunProcedimiento());
            procGVO.setAtributo("codProcedimiento", proc);
            roles= obtenerRolesProcedimiento(procGVO, params);
        } else {
           roles = InteresadosManager.getInstance().getListaRolesRegistro(params);
        }
        return roles;
    }
    
    private void sendMail(HttpServletRequest request, GeneralValueObject gVO, String[] params, DefinicionTramitesValueObject defTramVO) {

        /*******************************************************************/
        // Se envia el correo para notificar el inicio del expediente                
        // una vez inicializado el expediente => Se envía el correo 
        //String numero = request.getParameter("numeroExp");
        String numero= (String) gVO.getAtributo("numero");
        String ejercicioAsiento= (String) gVO.getAtributo("ejercicioAsiento");
        String numeroAsiento= (String) gVO.getAtributo("numeroAsiento");
        String procedimiento= "";
        m_Log.debug("Se ha inicializado el expediente => Se envía el mail");
        TercerosManager terceroManager = TercerosManager.getInstance();
        m_Log.debug(">>>>>>>El numero de registro es: " + numero);
        m_Log.debug(">>>>>>>Código tercero para el que se recupera el mail: " + request.getParameter("codTerc"));
        String to = "";
        if (request.getParameter("codTerc") != null && request.getParameter("codTerc").length() >= 1) {
            to = terceroManager.getEmailTercero(request.getParameter("codTerc"), params);
        }

        m_Log.debug(">>>>>>>destinatario del mail: " + to);
        if (to != null && to.length() >= 1) {
            // Se recupera el asunto y mensaje del correo
            String asunto = m_ConfigTechnical.getString("mail.subject").replaceAll("@expediente@", numero);
            //String mensaje =   m_ConfigTechnical.getString("mail.contentInicioTramiteInteresados");
            String mensaje = m_ConfigTechnical.getString("mail.contentInicioTramiteInteresados").replaceAll("@tramite@", defTramVO.getNombreTramite()).replaceAll("@expediente@", numero);
            
            
            try{
                 procedimiento ="";
                 if (((String) gVO.getAtributo("desProcedimiento")!=null) &&
                    (!((String)gVO.getAtributo("desProcedimiento")).equals("")))
                procedimiento = (String) gVO.getAtributo("desProcedimiento");
                else {
                     
                     procedimiento=DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(defTramVO.getTxtCodigo(), params);
                     
                 }
            }catch (Exception e)
            {
              e.printStackTrace();  
            }
             
           
            mensaje=mensaje.replaceAll("@procedimiento@", procedimiento);            
            if((ejercicioAsiento!=null)&&(numeroAsiento!=null))
            mensaje=mensaje.replaceAll("@numeroRegistro@", "(núm registro: "+ejercicioAsiento+"/"+numeroAsiento+")");
                    
            

            m_Log.debug("Nombre del trámite: " + defTramVO.getNombreTramite());
            m_Log.debug("codigo de tramite: " + (String) gVO.getAtributo("codTramite"));
            m_Log.debug("Mensaje de notificación: " + mensaje);
            m_Log.debug("Asunto del mail: " + asunto);

            MailHelper mailer = new MailHelper();
            try {
                mailer.sendMail(to, asunto, mensaje);
            } catch (Exception e) {
                // Si ocurre algún error durante el envío del correo
                e.printStackTrace();
                m_Log.error("Error al enviar e-mail con notificación de inicio de expediente para expediente " + numero + ": " + e.getMessage());
            }
        }
    // una vez inicializado el expediente => Se envía el correo 
    /*******************************************************************/
    }
    
    
    private void cargarDatosComunicacion(Long idComunicacion, String[] params,FichaExpedienteForm expForm ){
        try{
            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".cargarDatosComunicacion() : INICIO  -> ID comunicacion" + idComunicacion);}
            
            //Instanciamos el manager de comunicaciones
            ComunicacionFlexiaManager comManager = ComunicacionFlexiaManager.getInstance();
            
            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".cargarDatosComunicacion() : INICIO  -> Buscando comunicacion...");}
            //LLamamos a la funcion de obtención de comunicacion
            ComunicacionVO comVO = comManager.getComunicacion(idComunicacion, params);
            
            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".cargarDatosComunicacion() : INICIO  -> Buscada  comunicacion...");}
            
            //Si la comunicacion no estaba leida la marcamos como leida
            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".cargarDatosComunicacion() : INICIO  -> Marcando la  comunicacion como leida...");}
            comVO.setLeida(comManager.marcarComunicacion(idComunicacion, params));
            
            
            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".cargarDatosComunicacion() : INICIO  -> Devolviendo  comunicacion...");}
            //Setamos el valor del formulario
            expForm.setComunicacionVO(comVO);
            
                        
            if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".cargarDatosComunicacion() : FIN");}
            
        }catch (Exception e){
            m_Log.error(this.getClass().getName() + ".cargarDatosComunicacion() : ERROR  " + e.getMessage());
        }
    }
    private Vector calcular_campos_expresion(String[] params,FichaExpedienteForm expForm,HttpSession session,HttpServletRequest request,GeneralValueObject gVO)
    {

          Vector expresiones_calculadas = new Vector();
          AdaptadorSQLBD abd = null;
          Connection con = null;         


          try 
          {               
                abd = new AdaptadorSQLBD(params); 
                con = abd.getConnection();        
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario"); 
                String valor_exp = new String();
                
                tramExpVO.setCodMunicipio(expForm.getCodMunicipio()); 
                tramExpVO.setCodProcedimiento(expForm.getCodProcedimiento()); 
                tramExpVO.setEjercicio(expForm.getEjercicio()); 
                tramExpVO.setNumeroExpediente(expForm.getNumExpediente()); 
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario())); 
                tramExpVO.setBloqueo("no");
                
                Vector estructuraDSExpediente = FichaExpedienteDAO.getInstance().cargaEstructuraDatosSuplementarios(gVO, abd, con);                
                Vector valoresDSExpediente = new Vector();
                Vector estructuraDSTramites = new Vector();    
                Vector valoresDSTramites = new Vector();    
                
                //*************************************************
                //*************************************************                                
                HashMap campos = new HashMap();                
                CamposFormulario CampF = null;
                                                                  
                for(int i=0;i<estructuraDSExpediente.size();i++) 
                {
                      EstructuraCampo CC = (EstructuraCampo) estructuraDSExpediente.elementAt(i);                                                                       
                      campos.put(CC.getCodCampo(),request.getParameter(CC.getCodCampo()));                               
                      CampF = new CamposFormulario(campos);                              
                      valoresDSExpediente.addElement(CampF);
                }                
                //**************************************************
                //**************************************************                
                Vector ExpresionesCalculadas = TramitacionExpedientesDAO.getInstance().getListaCamposExpresionCalculada(abd, con, tramExpVO); 
                GeneralValueObject g;                 
                for (int i=0; i< ExpresionesCalculadas.size(); i++) 
                {
                    g= (GeneralValueObject) ExpresionesCalculadas.elementAt(i); 
                    GeneralValueObject h = new GeneralValueObject();
                    
                    String expresion = (String) g.getAtributo("expresion"); 
                    String codTramite = (String) g.getAtributo("codTramite");                                
                    String codigoCampo = (String)g.getAtributo("cod_campo");
                    String origen = (String)g.getAtributo("origen");  
                    
                       m_Log.debug("Expresion campo calculado del campo: "+codigoCampo+" :"+expresion);
                    
                    //Para reconocer si se trata de una expresion de fecha o de un campo numerico buscaremos en la expresion las etiquetas que utilizamos para fechas                    
                    if (expresion != null || !"".equals(expresion))
                    {
                        if (expresion.indexOf(";MESES;")>0)
                            valor_exp = recupera_valor_expresion_fec(expresion,valoresDSExpediente,estructuraDSExpediente);
                        else
                            valor_exp = recupera_valor_expresion_num(expresion,valoresDSExpediente,estructuraDSExpediente);                    
                    }
                     m_Log.debug("valor campo calculado del campo: "+codigoCampo+" :"+valor_exp);
                    
                    h.setAtributo("cod_campo", codigoCampo);
                    h.setAtributo("origen", origen);
                    h.setAtributo("valor", valor_exp);
                    expresiones_calculadas.add(h);
                }
            }
            catch (Exception e) {                    
                e.printStackTrace();            
            }finally{
                try{
                    abd.devolverConexion(con);
                    
                }catch(BDException e){
                    m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                }
          }
          return expresiones_calculadas;        
    }
    
    private String recupera_valor_expresion_num(String expresion, Vector valores, Vector estructura)
    {
        String campo = "";
        String valor_campo = "";
        String tramite = "";
        boolean valido = true;
        String retorno = "";

        expresion = expresion.replace(" x "," * ");
        expresion = expresion.replace(" X "," * ");

        for (int i=0; i<estructura.size(); i++){
                EstructuraCampo eC = (EstructuraCampo) estructura.elementAt(i);
                CamposFormulario cF = (CamposFormulario) valores.get(i);
                
                campo  = eC.getCodCampo();                                
                valor_campo = cF.getString(eC.getCodCampo());   
                tramite = eC.getCodTramite();
                if (Campo_sin_valor(expresion,campo) && "".equals(valor_campo)) 
                    valido = false;
                
                if (tramite == null)
                {
                    expresion = expresion.replace(" " + campo + " "," " + valor_campo + " ");
                    expresion = expresion.replace("(" + campo + " ","(" + valor_campo + " ");
                    expresion = expresion.replace(" " + campo + ")"," " + valor_campo + ")");
                    expresion = expresion.replace("(" + campo + ")","(" + valor_campo + ")");
                }
                else
                {
                    expresion = expresion.replace(" " + campo + "_T" + tramite + " "," " + valor_campo + " ");
                    expresion = expresion.replace("(" + campo + "_T" + tramite + " ","(" + valor_campo + " ");
                    expresion = expresion.replace(" " + campo + "_T" + tramite + ")"," " + valor_campo + ")");
                    expresion = expresion.replace("(" + campo + "_T" + tramite + ")","(" + valor_campo + ")");
                }
        }
        if (valido == true)
            return evalua_cadena.evalua_calculos(expresion);
        else
            return "";        
    }
    private String recupera_valor_expresion_fec(String expresion, Vector valores, Vector estructura)
    {
        String aux = "";
        String campo = "";
        String valor_campo = "";
        String tramite = "";
        String operacion = "";
        String cadena = "";
        String dia = "0";
        String mes = "0";
        String ano = "0";
        String resultado= "";
        int posicion = 0;
        int inicio = 0;
        boolean valido = true;
        String retorno = "";
        
        try 
        {
            Date fecha = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
            //Calendar cal = Calendar.getInstance(); 
            Calendar cal = new GregorianCalendar(); 

            inicio = expresion.indexOf("(");
            if (expresion.indexOf("+")> 0)
                operacion ="SUMA";
            else
                operacion ="RESTA";


            for (int i=0; i<estructura.size(); i++){
                    EstructuraCampo eC = (EstructuraCampo) estructura.elementAt(i);
                    CamposFormulario cF = (CamposFormulario) valores.get(i);

                    campo  = eC.getCodCampo();                                
                    valor_campo = cF.getString(eC.getCodCampo());   
                    tramite = eC.getCodTramite();
                    if (Campo_sin_valor(expresion,campo) && "".equals(valor_campo)) 
                        valido = false;
                    else if (Campo_sin_valor(expresion,campo) &&  valor_campo == null)
                        valido = false;
                    
                    if (tramite == null)                        
                        aux = campo + " ";
                    else
                        aux = campo + "_T" + tramite + " ";
                        
                    
                    if (expresion.indexOf(aux)>=0 && valido == true)
                    {
                        fecha = sdf.parse(valor_campo);                    
                        cadena = expresion.substring(inicio,expresion.length());
                        cadena = cadena.replace("(","");
                        cadena = cadena.replace(")","");

                        posicion = cadena.indexOf(";DIAS;");
                        dia = cadena.substring(0,posicion);
                        cadena = cadena.substring(posicion + 6, cadena.length());

                        posicion = cadena.indexOf(";MESES;");
                        mes = cadena.substring(0,posicion);
                        cadena = cadena.substring(posicion + 7, cadena.length());

                        posicion = cadena.indexOf(";ANOS;");
                        ano = cadena.substring(0,posicion);                    
                    }                               
            }
            if (operacion.equals("RESTA"))
            {
               dia = "-"+dia;
               mes = "-"+mes;
               ano = "-"+ano;            
            }
            
            cal.setTime(fecha); 
            resultado = sdf.format(cal.getTime()); 
            cal.add(Calendar.DATE, Integer.parseInt(dia)); 
            resultado = sdf.format(cal.getTime()); 
            cal.add(Calendar.MONTH, Integer.parseInt(mes));             
            resultado = sdf.format(cal.getTime()); 
            cal.add(Calendar.YEAR, Integer.parseInt(ano)); 
            resultado = sdf.format(cal.getTime()); 
        }
        catch (Exception e) {                    
                e.printStackTrace();
        }
        if (valido == true)
            return resultado;
        else
            return "";       
    }
     private boolean Campo_sin_valor(String expresion, String campo)
    {        
        boolean vacio = false;
        if (expresion.indexOf(" " + campo + " ") >= 0)
            vacio = true;
        else if (expresion.indexOf("(" + campo + ")") >= 0)
            vacio = true;
        else if (expresion.indexOf("(" + campo + " ") >= 0)
            vacio = true;
        else if (expresion.indexOf(" " + campo + ")") >= 0)
            vacio = true;
        else if (expresion.indexOf(" " + campo + "_T") >= 0)
            vacio = true;
        else if (expresion.indexOf("(" + campo + "_T") >= 0)
            vacio = true;    
        return vacio;
    }        
     
     /**
      * Método auxiliar que se utiliza para recuperar el nombre completo
      * de un usuario a partir de su código.
      * 
      * @param codUsuario
      * @return Método auxiliar que se u
      */
     private String dameNombreUsuario( String codUsuario, String[] params ){
       m_Log.debug("dameNombreUsuario.BEGIN: ");
       m_Log.debug("dameNombreUsuario.codUsuario: "+ codUsuario);
       
       String nombreUsuario=""; 
       if(codUsuario!=null){
        try{
             int codUsuarioInt=Integer.parseInt(codUsuario);
             UsuarioManager usuarioManager=UsuarioManager.getInstance();
             nombreUsuario=usuarioManager.getNombreUsuario(params, codUsuarioInt);
        } catch(Exception e){
           m_Log.debug("Saltó excepción en el método dameNombreUsuario");
           e.printStackTrace();
        }
       }
       m_Log.debug("dameNombreUsuario.END.Resultado devuelto: "+ nombreUsuario);
       return nombreUsuario;
     }
     
     
      /**
      * Método auxiliar que lee el valor de un tag de un fichero xml pasandole la lista de tags
      * @param documento Objeto que contiene el documento
      * @param padre Tag padre
      * @param hijo Tag hijo
      * @param tags Lista de tags a leer
      * @param subHijo Tag subHijo
      * @param tagsSubHijo Lista de tags a leer del subHijo
      * @return Lista de elementos 
      */
     //private List<GeneralValueObject> leerDatosSuplementariosXML(RegistroValueObject documento, String padre){
     private Hashtable<String,List <GeneralValueObject>> leerDatosSuplementariosXML(RegistroValueObject documento, String padre){
         
       if(m_Log.isDebugEnabled()){ m_Log.debug("FichaExpedienteAction.leerDatosSuplementariosXML(): Inicio lectura tags fichero " + documento.getNombreDoc());}
        
       List <GeneralValueObject> listaCamposExpediente = new ArrayList<GeneralValueObject>();
       List <GeneralValueObject> listaCamposTramite = new ArrayList<GeneralValueObject>();
       
       Hashtable<String,List <GeneralValueObject>> salida = new Hashtable<String, List<GeneralValueObject>>();

       try{
            byte[] ficheroBytes = documento.getDoc();
            String fichero = new String(ficheroBytes,ConstantesDatos.CODIFICACION_ISO_8859_1);            
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new StringReader(fichero));
            Element nodoRaiz = doc.getRootElement();
            
            if (nodoRaiz != null){
                Element nodoPadre = nodoRaiz.getChild(padre);
                if (nodoPadre != null){
                    List listaNodosHijos = nodoPadre.getChildren();
                    for ( int i = 0; i < listaNodosHijos.size(); i++ ){
                        Element nodoHijo = (Element) listaNodosHijos.get(i);
                        String codCampo = nodoHijo.getChildTextTrim(ConstantesDatos.TAG_XML_CODCAMPO);
                        String valorCampo = nodoHijo.getChildTextTrim(ConstantesDatos.TAG_XML_VALORCAMPO);                        
                        GeneralValueObject generalVO = new GeneralValueObject();                                              
                        generalVO.setAtributo(ConstantesDatos.TAG_XML_CODIGO, codCampo);
                        generalVO.setAtributo(ConstantesDatos.TAG_XML_VALOR, valorCampo);
                        
                        String tramite = nodoHijo.getChildTextTrim(ConstantesDatos.TAG_XML_TRAMITE);
                        if (tramite != null && "S".equalsIgnoreCase(tramite)){
                            listaCamposTramite.add(generalVO);                                                          
                        }else{
                            // No existe el tag <Tramite> o no tiene valor
                            listaCamposExpediente.add(generalVO);                            
                        }
                    }
                }
            }
        }catch ( IOException io ) {
            //lista = null;
            listaCamposExpediente = null;            
            listaCamposTramite = null;            
           if(m_Log.isErrorEnabled()){ m_Log.error("FichaExpedienteAction.leerDatosSuplementariosXML(): Error al procesar el fichero XML. IOExecption: " + io.getMessage());}
        }catch ( JDOMException jdo ) {
            //lista = null;
            listaCamposExpediente = null;            
            listaCamposTramite = null;            
            if(m_Log.isErrorEnabled()){ m_Log.error("FichaExpedienteAction.leerDatosSuplementariosXML(): Error al procesar el fichero XML. JDOM Exception: " + jdo.getMessage());}
        }catch ( Exception e) {
            //lista = null;
            listaCamposExpediente = null;            
            listaCamposTramite = null;            
            if(m_Log.isErrorEnabled()){ m_Log.error("FichaExpedienteAction.leerDatosSuplementariosXML(): Error al procesar el fichero XML. Exception: " + e.getMessage());}
        }
        
        if(m_Log.isDebugEnabled()){ m_Log.debug("FichaExpedienteAction.leerDatosSuplementariosXML(): Fin lectura tags fichero " + documento.getNombreDoc());}
              
        //return lista;
        salida.put("CAMPOS_EXPEDIENTE", listaCamposExpediente);
        salida.put("CAMPOS_TRAMITE", listaCamposTramite);
        
        return salida;

     }
     
     
      /**
      * Método auxiliar que lee el valor de un tags de interesados del fichero xml
      * @param documento Objeto que contiene el documento
      * @param padre Tag padre
      * @param hijo1 Tag hijo
      * @param hijo2 Tag hijo
      * @return Lista de elementos 
      */
     private List<GeneralValueObject> leerInteresadosFicheroXML(RegistroValueObject documento, String padre, String hijo1, String hijo2){
         
       if(m_Log.isDebugEnabled()){ m_Log.debug("FichaExpedienteAction.leerInteresadosFicheroXML(): Inicio lectura tags fichero " + documento.getNombreDoc());}
        
       GeneralValueObject generalVO = new GeneralValueObject();
       Vector lista = new Vector(); 
       List <GeneralValueObject> listaFinal = new ArrayList<GeneralValueObject>();        
       

       try{
            byte[] ficheroBytes = documento.getDoc();
            String fichero = new String(ficheroBytes,ConstantesDatos.CODIFICACION_ISO_8859_1);            
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new StringReader(fichero));
            Element nodoRaiz = doc.getRootElement();
            
            if (nodoRaiz != null){
                Element nodoNivel1 = nodoRaiz.getChild(padre);
                if (nodoNivel1 != null){
                    List listaNivel2 = nodoNivel1.getChildren();
                    for ( int j = 0; j < listaNivel2.size(); j++ ){  
                         Element nodoNivel2 = (Element) listaNivel2.get(j);
                         if (nodoNivel2 != null){
                            List listaNivel3 = nodoNivel2.getChildren();
                            for ( int k = 0; k < listaNivel3.size(); k++ ){ 
                                Element nodoNivel3 = (Element) listaNivel3.get(k);
                                String nombreNodoNivel3 = (String) nodoNivel3.getName();

                                if (nombreNodoNivel3.equals(hijo1)){
                                    List listaNivel41 = nodoNivel3.getChildren();
                                    for (int l=0; l < listaNivel41.size();l++){
                                         Element nodoNivel41 = (Element) listaNivel41.get(l);
                                         String nombreNodoNivel41 = (String) nodoNivel41.getName();
                                         String codCampo = nodoNivel41.getValue();
                                         generalVO.setAtributo(nombreNodoNivel41, codCampo);
                                    }
                                }else{
                                    if (nombreNodoNivel3.equals(hijo2)){ 
                                        List listaNivel41 = nodoNivel3.getChildren();
                                        for (int m=0; m < listaNivel41.size();m++){
                                             Element nodoNivel41 = (Element) listaNivel41.get(m);
                                             String codCampo = nodoNivel41.getChildTextTrim(ConstantesDatos.TAG_XML_CODCAMPO);
                                             String valorCampo = nodoNivel41.getChildTextTrim(ConstantesDatos.TAG_XML_VALORCAMPO);
                                             GeneralValueObject generalVO1 = new GeneralValueObject();                                              
                                             generalVO1.setAtributo(ConstantesDatos.TAG_XML_CODIGO, codCampo);
                                             generalVO1.setAtributo(ConstantesDatos.TAG_XML_VALOR, valorCampo);                                             
                                             lista.add(generalVO1);
                                        }                                        
                                    }else{
                                       String codCampo = nodoNivel3.getValue();
                                       generalVO.setAtributo(nombreNodoNivel3, codCampo);
                                    }
                                }
                            }
                             GeneralValueObject generalSalidaVO = new GeneralValueObject(); 
                             generalSalidaVO.setAtributo("datosInteresado", generalVO);                             
                             generalSalidaVO.setAtributo("datosSuplementarios", lista);                             
                             generalVO = new GeneralValueObject();
                             lista = new Vector();  
                             listaFinal.add(generalSalidaVO);
                         }
                    }
                }
            }
        }catch ( IOException io ) {
            listaFinal = null;
           if(m_Log.isErrorEnabled()){ m_Log.error("FichaExpedienteAction.leerInteresadosFicheroXML(): Error al procesar el fichero XML. IOExecption: " + io.getMessage());}
        }catch ( JDOMException jdo ) {
            listaFinal = null;
            if(m_Log.isErrorEnabled()){ m_Log.error("FichaExpedienteAction.leerInteresadosFicheroXML(): Error al procesar el fichero XML. JDOM Exception: " + jdo.getMessage());}
        }catch ( Exception e) {
            listaFinal = null;
            if(m_Log.isErrorEnabled()){ m_Log.error("FichaExpedienteAction.leerInteresadosFicheroXML(): Error al procesar el fichero XML. Exception: " + e.getMessage());}
        }
        
        if(m_Log.isDebugEnabled()){ m_Log.debug("FichaExpedienteAction.leerInteresadosFicheroXML(): Fin lectura tags fichero " + documento.getNombreDoc());}
              
        return listaFinal;

     }
     
     
      /**
      * Método auxiliar que lee el valor de un tag de un fichero xml
      * @param documento Objeto que contiene el documento
      * @param padre Tag padre
      * @param hijo Tag hijo
      * @param atributo Atributo a leer
      * @return Valor del atributo
      */
     private String leerAtributoFicheroXML(RegistroValueObject documento, String padre, String hijo, String nomAtributo){

       if(m_Log.isDebugEnabled()){ m_Log.debug("FichaExpedienteAction.leerAtributoFicheroXML(): Inicio lectura tags fichero " + documento.getNombreDoc());}
        
       String atributo = "";

       try{
            byte[] ficheroBytes = documento.getDoc();
            String fichero = new String(ficheroBytes,ConstantesDatos.CODIFICACION_ISO_8859_1);            
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new StringReader(fichero));
            Element nodoRaiz = doc.getRootElement();
            
            if (nodoRaiz != null){
                Element nodoPadre = nodoRaiz.getChild(padre);
                if (nodoPadre != null){
                    Element nodoHijo = nodoPadre.getChild(hijo);
                    if (nodoHijo != null){
                        atributo = nodoHijo.getAttributeValue(nomAtributo);
                    }
                }
            }
        }catch ( IOException io ) {
            atributo = null;
           if(m_Log.isErrorEnabled()){ m_Log.error("FichaExpedienteAction.leerAtributoFicheroXML(): Error al procesar el fichero XML. IOExecption: " + io.getMessage());}
        }catch ( JDOMException jdo ) {
            atributo = null;
            if(m_Log.isErrorEnabled()){ m_Log.error("FichaExpedienteAction.leerAtributoFicheroXML(): Error al procesar el fichero XML. JDOM Exception: " + jdo.getMessage());}
        }catch ( Exception e) {
            atributo = null;
            if(m_Log.isErrorEnabled()){ m_Log.error("FichaExpedienteAction.leerAtributoFicheroXML(): Error al procesar el fichero XML. Exception: " + e.getMessage());}
        }
        
        if(m_Log.isDebugEnabled()){ m_Log.debug("FichaExpedienteAction.leerAtributoFicheroXML(): Fin lectura tags fichero " + documento.getNombreDoc());}
              
        return atributo;

     }
     
      /**
      * Método auxiliar que lee el valor de un tag de un fichero xml
      * @param documento Objeto que contiene el documento
      * @param padre Tag padre
      * @param hijo Tag hijo
      * @param atributo Atributo a leer
      * @return Valor del atributo
      */
     private String leerValorNodoFicheroXML(RegistroValueObject documento, String padre){

       if(m_Log.isDebugEnabled()){ m_Log.debug("FichaExpedienteAction.leerValorNodoFicheroXML(): Inicio lectura tags fichero " + documento.getNombreDoc());}        
       String valor = "";

       try{
            byte[] ficheroBytes = documento.getDoc();
            String fichero = new String(ficheroBytes,ConstantesDatos.CODIFICACION_ISO_8859_1);            
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new StringReader(fichero));
            Element nodoRaiz = doc.getRootElement();
            
            if (nodoRaiz != null){
                Element nodoPadre = nodoRaiz.getChild(padre);
                if (nodoPadre != null){                    
                    valor = nodoPadre.getValue();                    
                }
            }
        }catch ( IOException io ) {
            valor = null;
           if(m_Log.isErrorEnabled()){ m_Log.error("FichaExpedienteAction.leerValorNodoFicheroXML(): Error al procesar el fichero XML. IOExecption: " + io.getMessage());}
        }catch ( JDOMException jdo ) {
            valor = null;
            if(m_Log.isErrorEnabled()){ m_Log.error("FichaExpedienteAction.leerValorNodoFicheroXML(): Error al procesar el fichero XML. JDOM Exception: " + jdo.getMessage());}
        }catch ( Exception e) {
            valor = null;
            if(m_Log.isErrorEnabled()){ m_Log.error("FichaExpedienteAction.leerValorNodoFicheroXML(): Error al procesar el fichero XML. Exception: " + e.getMessage());}
        }
        
        if(m_Log.isDebugEnabled()){ m_Log.debug("FichaExpedienteAction.leerValorNodoFicheroXML(): Fin lectura tags fichero " + documento.getNombreDoc());}              
        return valor;

     }
     
     
      /**
     * Método llamado para devolver un String en formato JSON al cliente que ha realiza la petición 
     * a alguna de las operaciones de este action
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se devuelve la salida
     * al cliente que ha realizado la solicitud
     */
    private void retornarJSON(String json,HttpServletResponse response){
        
        try{
            if(json!=null){
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
	
	/**
	 * Metodo al que se llama para empezar la exportacion ENI.
	 * @param numExpediente
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void exportarExpediente(String numExpediente, HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
		GeneralValueObject respuesta = new GeneralValueObject();
		String pathFileZip = "";
		try {
			GestionEni gestionEni = new GestionEni();
			pathFileZip = gestionEni.generarDocumentoEni(numExpediente, usuario);
			
			if(StringUtils.isNotNullOrEmpty(pathFileZip)){
				respuesta.setAtributo("fichero", pathFileZip);
			}
			
		} catch (ConversorEniException ex) {
			respuesta.setAtributo("error", comprobarErrorExportacionEni(ex, numExpediente, usuario.getIdioma()));
			deleteFilebyPath(pathFileZip);
		} catch (TechnicalException ex) { 
			m_Log.debug("Se ha producido un error al guardar el historico de la exportacion ENI: ", ex);
			//En caso de que queramos enviar un popUp y cortar el flujo de la exportación, deberiamos descomentar la siguiente linea.
			//respuesta.setAtributo("error", "La exportación se ha realizado con exito, pero hubo errores al almacenar el histórico");
		} catch (GestionEniException ex) {
			LectorProperties properties = new LectorProperties(GestionEniConstantes.PROPERTIES_MENSAJES_ENI, usuario.getIdioma());
			respuesta.setAtributo("error", properties.getMensaje(ex.getCodigo()));
			deleteFilebyPath(pathFileZip);
		}
		retornarJSON(new Gson().toJson(respuesta), response);
	}
	
	
	public static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

		System.out.println("Writing '" + fileName + "' to zip file");

		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
	
	
	/**
	 * Metodo que controla los errores conocidos de le libreria conversorENI.
	 * @param ex
	 * @param numExpediente
	 * @param codIdioma
	 * @return 
	 */
	private String comprobarErrorExportacionEni(ConversorEniException ex, String numExpediente, int codIdioma) {
			LectorProperties properties = new LectorProperties(GestionEniConstantes.PROPERTIES_MENSAJES_ENI, codIdioma);
		if (CodigosErrores.COLECCION_INDICE_CONTENIDO_VACIO.equalsIgnoreCase(ex.getCodigo())) {
			return properties.getMensaje(CodigoMensajeEni.ERROR_EXPEDIENTE_SIN_DOCUMENTOS.getCodigo(), numExpediente);
		}else{
			return properties.getMensaje(CodigoMensajeEni.ERROR_CONVERSION_ENI.getCodigo());
		}
	}
	
	/**
	 * Metodo encargado de que, una vez, creado el zip de la exportacion ENI, se mande al navegador para exportarlo.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public ActionForward descargarFichero(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String pathExportZip = request.getParameter("pathExportZip");
		
		if (StringUtils.isNotNullOrEmpty(pathExportZip)) {
				File fileZip = new File(pathExportZip);
			try {
				byte[] result = Files.readAllBytes(fileZip.toPath());
				String nombreFichero = pathExportZip.substring(pathExportZip.lastIndexOf('/') + 1);
				response.setContentType("application/zip");
				response.setHeader("Content-Disposition", "attachment; filename=" + nombreFichero);
				
				ServletOutputStream out = response.getOutputStream();
				response.setContentLength(result.length);
				BufferedOutputStream bos = new BufferedOutputStream(out);
				bos.write(result, 0, result.length);
				bos.flush();
				bos.close();
			} catch (IOException ex) {
				m_Log.error("Error al encontrar el archivo para exportar: ", ex);
			}finally{
				fileZip.delete();
			}
		}
		return null;
	}
	
	/**
	 * Metodo creado para borrar el Zip en caso de error.
	 */
	private void deleteFilebyPath(String path) {
		if (StringUtils.isNotNullOrEmpty(path)) {
			File fichero = new File(path);
			if (fichero != null) {
				fichero.delete();
			}
		}
	}
	
} 
