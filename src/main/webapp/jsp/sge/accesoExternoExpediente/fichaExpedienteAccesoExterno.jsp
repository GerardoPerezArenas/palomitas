<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.technical.EstructuraCampo"%>
<%@page import="es.altia.agora.technical.CamposFormulario"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.List"%> 
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.business.sge.persistence.manual.DatosSuplementariosDAO"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="es.altia.catalogoformularios.util.DateOperations"%>
<%@ page import="es.altia.catalogoformularios.model.solicitudes.vo.FormularioTramitadoVO"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.ConsultaExpedientesForm" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.ConsultaExpedientePorCampoSupForm" %>
<%@page import="java.util.Enumeration" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="es.altia.agora.business.sge.InteresadoExpedienteVO"%>
<%@page import="es.altia.agora.business.sge.ConsultaExpedientesValueObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DatosPantallaModuloVO"%>
<%@page import="es.altia.flexia.business.comunicaciones.vo.ComunicacionVO"%>
<%@page import="es.altia.flexia.notificacion.vo.NotificacionVO"%>
<%@page import="es.altia.agora.business.sge.DefinicionAgrupacionCamposValueObject"%>
<%@ page import="java.text.MessageFormat"%>
<%@ page import="java.util.ResourceBundle"%>

<html:html>
<head>
    <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>::: Acceso externo a ficha del expediente :::</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%                      
            String fechaFinExpediente = "";
            String desdeConsulta = "";
            String nombre_campo = "";
            String tipo_campo = "";
            int idioma = 1;
            int apl = 4;
            int pestana = 0;
            String soloConsultarExp = "no";
            String codUsu = "";
            boolean mostrarUbicacion = false;
            int codOrganizacion = 0;
            UsuarioValueObject usuario = new UsuarioValueObject();
            FichaExpedienteForm expForm = new FichaExpedienteForm();
            Log _log = LogFactory.getLog(this.getClass());
            if (session.getAttribute("usuario") != null) {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                expForm = (FichaExpedienteForm) session.getAttribute("FichaExpedienteForm");
                idioma = usuario.getIdioma();
                int cUsu = usuario.getIdUsuario();
                codUsu = Integer.toString(cUsu);
                soloConsultarExp = usuario.getSoloConsultarExp();
                codOrganizacion = usuario.getOrgCod();
            }
            fechaFinExpediente = expForm.getFechaFin();
            desdeConsulta = ((String) expForm.getExpedienteVO().getAtributo("desdeConsulta"));
            String pestanaInicial = (String) session.getAttribute("pestana");
            if (pestanaInicial != null) {
                if ("tramites".equals(pestanaInicial)) {
                    pestana = 0;
                }
                session.removeValue("pestana");
            }
            ParametrosTerceroValueObject ptVO = (ParametrosTerceroValueObject) session.getAttribute("parametrosTercero");
            Config m_Config = ConfigServiceHelper.getConfig("common");            
            Config m_ConfigExp = ConfigServiceHelper.getConfig("Expediente");
            Boolean mostrarComunicaciones=false;
            try{
                mostrarComunicaciones = m_ConfigExp.getString(expForm.getCodMunicipio() + "/comunicaciones_expediente_activadas").toUpperCase().equals("SI");            
            }
            catch (Exception e)
            {
                mostrarComunicaciones=false; 
            }

 			Boolean mostrarCheckNotificaciones = false;
            try{
                mostrarCheckNotificaciones = m_ConfigExp.getString(expForm.getCodMunicipio() +"/CHECK_NOTIFICACION_TELEMATICA_NOTIFICACIONES").toUpperCase().equals("SI");
            }catch(Exception e){
                mostrarCheckNotificaciones = false;
            }
            
			Boolean mostrarColumnasNotificaciones = false;
            try{
                mostrarColumnasNotificaciones = m_ConfigExp.getString(expForm.getCodMunicipio()+"/MOSTRAR_TODAS_COLUMNAS_NOTIFICACION").toUpperCase().equals("SI");
            }catch(Exception e){
                mostrarColumnasNotificaciones = false;
            }

			//Se recuperan propiedad del fichero de configuracion Expediente.properties
            Boolean mostrarEnlaceDocumentacion=false;
            try{
                mostrarEnlaceDocumentacion=m_ConfigExp.getString("JSP.enlace_ver_documentacion_otros_documentos").toUpperCase().equals("SI");
            }catch(Exception e){
                mostrarEnlaceDocumentacion=false;
			}

            String statusBar = m_Config.getString("JSP.StatusBar");
            String botones = "", campos = "", ocurrencias = "";
            // Se comprueba si se llega desde alguna de las action de consulta y se establece cual es, si de la
            // consulta de expediente o la consulta de expediente por campo suplementario. Necesario para que desde
            // el boton volver se pueda volver a la consulta de expedientes.
            if (request.getHeader("referer") != null && request.getHeader("referer").endsWith("ConsultaExpedientes.do") && !request.getHeader("referer").contains("opcion=consultarDesdeGestion")) {
                session.setAttribute("consulta", "normal");
            } else if (request.getHeader("referer") != null && request.getHeader("referer").endsWith("ConsultaExpedientePorCampoSup.do")) {
                session.setAttribute("consulta", " campo");
            } else if (request.getHeader("referer") != null && request.getHeader("referer").contains("ConsultaExpedientes.do") && request.getHeader("referer").contains("opcion=consultarDesdeGestion")) {
                session.setAttribute("consulta", "cerrar");
            } else if (request.getHeader("referer") != null && request.getHeader("referer").contains("mainVentana2.jsp")) {
                session.setAttribute("consulta", "cerrar");
            }
            
            FichaExpedienteForm fFormAux = (FichaExpedienteForm) session.getAttribute("FichaExpedienteForm");                                    
            boolean  poseeLoc  = false;
            String poseeLocAux = (String)(fFormAux.getExpedienteVO()).getAtributo("poseeLocalizacion");
            if("0".equals(poseeLocAux))
                poseeLoc = true;

            String lcodProv = "";
            String ldescProv = "";
            String lcodMun = "";
            String ldescMun = "";
            String lcodDocs = "";
            String ldescDocs = "";
            String lpersJF = "";
            String lcodTOC = "";
            String ldescTOC = "";
            String lcodCP = "";
        
            /************* Se comprueba cual es el navegador utilizado por el usuario ******************/
            String userAgent = request.getHeader("user-agent");
            /**** SE COMPRUEBA SI ES NECESARIO O NO CARGAR LA PESTAÑA DE NOTIFICACIONES ****/
            Boolean mostrarNotificaciones = false;
            try{
                Config m_ConfigNotif = ConfigServiceHelper.getConfig("notificaciones");
                mostrarNotificaciones = m_ConfigNotif.getString(expForm.getCodMunicipio() + "/CARGAR_PESTANHA_NOTIFICACIONES_EXPEDIENTE").toUpperCase().equals("SI");
            }catch(Exception e){
                mostrarNotificaciones = false;
           }  
            // Se recuperan propiedades del fichero de configuración common.properties 
           Config m_Conf = ConfigServiceHelper.getConfig("common");           
           // Tamanho maximo de un campo de texto
           Integer tamanoMaximoCampoTexto = new Integer(m_Conf.getString("TamMaximo.CampoTexto"));         
           // Tamanho maximo de un campo numerico
           Integer tamanoMaximoCampoNumerico = new Integer(m_Conf.getString("TamMaximo.InputCampoNumerico"));             

            // #253692: Recuperamos de FichaExpedienteForm el atributo que indica si se muestra la ubicacion de la documentacion o no
            mostrarUbicacion = expForm.isUbicacionDocumentacionVis();

%>  
    
    <link rel="stylesheet" href="<c:url value='/css/jquery-ui-1.10.1.css'/>"/>       
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <script src='<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>'></script>
    <script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-ui-1.10.1.min.js'/>"></script>        
    
    <!-- Beans -->
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script> 
    <script type="text/javascript" src="<c:url value='/scripts/json2.js'/>"></script>    
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/gestionTerceros.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>    
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
    <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>           
    <script type="text/javascript" src="<c:url value='/scripts/fichaExpedienteAccesoExterno.js'/>"></script> 
    <script type="text/javascript" src="<c:url value='/scripts/JavaScriptUtil.js'/>?<%=System.currentTimeMillis()%>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/formateador.js'/>"></script>
    <script type="text/javascript">       
    var listaOtrosDocumentosActualizados = new Array();
    var mensajeGuardarCambios;
    var ventanaTramitacionExterna;
    var filtroPendientes = "<%=(String)session.getAttribute("filtro_pendientes")%>";  
    var lineasPendientes = "<%=(String)session.getAttribute("lineas_pendientes")%>"; 
    var columnaPendientes = "<%=(String)session.getAttribute("columna_pendientes")%>"; 
    var paginaPendientes = "<%=(String)session.getAttribute("pagina_pendientes")%>"; 
    var tipoOrdenPendientes = "<%=(String)session.getAttribute("tipoOrden_pendientes")%>"; 
    var mensajeGuardarCambios;
    var esInteresadoObligatorio = "<%=expForm.isInteresadoObligatorio()%>";        
    var msjTamMaxCampoTexto = "<%=MessageFormat.format(descriptor.getDescripcion("msjNumMaximoCampoTex"), new Object[] {tamanoMaximoCampoTexto.intValue()}) %>";
    var tamanoMaximoCampoTexto    = "<%=tamanoMaximoCampoTexto.intValue()%>";    
    var tamanoMaximoCampoNumerico = parseInt("<%=tamanoMaximoCampoNumerico%>");    
    var mensajeTamanhoMaximoCampoNumerico = "<%= descriptor.getDescripcion("msjNumMaximoCampoNum") %>";    
    var mensajeEtiquetaAlarma = "<%=descriptor.getDescripcion("etiq_verAlarma")%>";
    var mensajeEtiquetaActivar= "<%=descriptor.getDescripcion("gbActivar")%>";
    var mensajeEtiquetaDesactivar = "<%=descriptor.getDescripcion("gbDesactivar")%>";
    var mensajeExitoGrabacionExpediente = "<%=descriptor.getDescripcion("msjDatExpGrab")%>";
    var mensajeSeleccionarFecha = "<%=descriptor.getDescripcion("msgSeleccionarFecha")%>";
    var mensajeNoDatosSuplementarios = "<%=descriptor.getDescripcion("errorDatosSuplMostrar")%>";
    // Variable global booleana que indica si se está consultando el expediente o no
    var gConsultando;
    // Variable global que contiene los nombres de los campos suplementarios del expediente
    var nombreCamposRecargarCalculados  = '';
    // Variable global que contiene los tipos de los campos suplementarios del expediente
    var tiposCamposRecargarCalculados   = '';    
    var listaCampos = new Array();
    var listaAgrupaciones = new Array();
    var contadorValoresCamposSuplementariosExpediente = 0;
    
    var cargadoCamposSuplementarios = false;
    var cargadoOtrosDatos = false;
    var cargadoOtrosDocumentos = false;
    var cargadoComunicaciones  = false;
    var cargadoNotificaciones  = false;
    
    var checkNotificacionesHabilitado = true;
    var expedienteFinalizado = "<%=fechaFinExpediente%>";
    var estadoExpediente = "<%=(String)expForm.getExpedienteVO().getAtributo("estado")%>";
    
    <c:if test="${sessionScope.FichaExpedienteForm.readOnlyCheck}">        
        checkNotificacionesHabilitado = false;    
    </c:if>
        
    // #212448: indica si el trámite a iniciar se cargará de forma directa
    var cargaDirecta = false;
	
	//indica si se muestra el check de notificacion telematica en pestaña Notificaciones 
    var mostrarCheckNotificaciones=<%=mostrarCheckNotificaciones%>;
    var mostrarColumnasNotificaciones = <%=mostrarColumnasNotificaciones%>;

    
     // #289948: indica si se ha enviado notificaci?n electr?nica para que el avance del tr?mite no se pueda cancelar
    // hay tanto redireccionamiento entre jsps ocultas y el action que se declara la variable global para evitar reenviarla en la request continuamente
    var postNotificacionEnviada = false;
    
    function ventanaPopUpModal(opcion,codigo) {
        if (document.forms[0].modoConsulta.value!="si") {
            var source = "<html:rewrite page='/sge/DatosSuplementariosFichero.do'/>?opcion=" + opcion + "&codigo="+ codigo;
            abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + source ,
                "ventana1",'width=520,height=430,scroll=no',function(result){
                    if (result != undefined) {
                        modificaVariableCambios(1);
                        actualizarFicheros(result[0], result[1]);
                    }
                });
        }
    }
    
    function actualizarFicheros(campo, path){
        eval("document.forms[0]."+campo+".value='"+path+"';");
        eval("habilitarBoton(document.forms[0].cmdVisualizar"+campo+");");
        eval("habilitarBoton(document.forms[0].cmdEliminar"+campo+");");
        eval("habilitarBoton(document.forms[0].cmdCSV"+campo+");");
    }
    function actualizarEliminacionFicheros(campo){
        eval("document.forms[0]."+campo+".value='';");
        eval("deshabilitarBoton(document.forms[0].cmdVisualizar"+campo+");");
        eval("deshabilitarBoton(document.forms[0].cmdEliminar"+campo+");");
        eval("deshabilitarBoton(document.forms[0].cmdCSV"+campo+");");
    }
    function onClickDocumento(codigo, nombreFich) {
        window.open("<%=request.getContextPath()%>/VerDocumentoDatosSuplementarios?codigo=" + codigo +
            "&nombreFich=" + nombreFich + "&opcion=0" + '&expHistorico=' + '<%=expForm.isExpHistorico()%>',  "ventana1",
            "left=10, top=10, width=500, height=500, scrollbars=no, menubar=no, location=no, resizable=yes");
    }                       
    
    function onClickEliminarDocumento(codigo) {          
        var msgErr1 = '<%=descriptor.getDescripcion("errEliminarDocCampoSup")%>';
        var msgErr2 = '<%=descriptor.getDescripcion("errEliminarDocCampoSup2")%>';
                
        
        if(marcarEliminadoCampoSuplementarioFichero(codigo,"EXPEDIENTE",msgErr1,msgErr2)){
        
            if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjBorrarFichero")%>') ==1) {
                modificaVariableCambios(1);
                eval("document.forms[0]."+codigo+".value='';");
                eval("deshabilitarBoton(document.forms[0].cmdVisualizar"+codigo+");");
                eval("deshabilitarBoton(document.forms[0].cmdEliminar"+codigo+");");
                eval("deshabilitarBoton(document.forms[0].cmdCSV"+codigo+");");
            }
        }
    }
    
    function onClickCrearCSVDocumento(codigo) {
        if((document.forms[0].existenCambiosSinGrabar.value)==1){
            jsp_alerta('A', 'Existen cambios sin grabar. Para generar un CSV es necesario grabar los datos previamente');
        }
        else{
            if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjCrearCSV")%>') == 1) {
                crearCSVCampoSuplementarioFichero(codigo);
            }
        }
      
    }
    
    var terceroActual;
    var domicilioActual;
    var rolActual;
    var admiteNotificacion;
    var BUSQUEDA_INTERESADO_ACTIVA = true; //busqueda por documento activa
    var tableObject;
    var expediente = new Array();
    var listaTramitesPlugin = new Array();
    var listaTramites = new Array();
    var listaTramitesOriginal = new Array();
    var listaPermisosTramites = new Array();
    var listaEnlaces = new Array();
    var estructuraDS = new Array();
    var valoresDS = new Array();
    var terceros = new Array();
    var cargaRapida = false;
    var listaFormularios = new Array();
    var listaFormulariosOriginal = new Array();
    var listaNotificacionesElectronicas = new Array();
    var nombreCombos = new Array();
    var listaNotificaciones = new Array();
    var listaNotificacionesTabla = new Array();                                   
    var listaInteresadosAnterior;    
    var codTipoDocs  = new Array();
    var descTipoDocs = new Array();
    var persFJ       = new Array();    
        
    // Para roles
    var codRols = new Array();
    var descRols = new Array();
    var pDef = new Array();
    // Para almacenar nombres, direcciones, y documentos
    var listaInteresados = new Array();
    var posInteresadoActual = 0;
    var i=0;
    var codigoUsuario = <%= codUsu %>;
    var admin="";
    // Variable con el codigo del rol por defecto
    var rolPorDefecto = "";
    var descRolPorDefecto = "";
    var unidadesUsuario ="";
    var existenInteresadosNotifElectronica=0;
    var numInteresadosAdmitenNotifElect=0;
                        
    function inicializar(){             
            <logic:iterate name="FichaExpedienteForm" property="codigosUorsPermisoUsuario" id="codigo">
                unidadesUsuario = unidadesUsuario + '<bean:write name="codigo"/>' + '-';        
            </logic:iterate>        
            
            var indice = 0;
            <logic:iterate name="FichaExpedienteForm" property="listaTiposDocumentosTerceros" id="tipoDocumento">
                codTipoDocs[indice] = '<bean:write name="tipoDocumento" property="codigo"/>';
                descTipoDocs[indice]= '<bean:write name="tipoDocumento" property="descripcion"/>';
                persFJ[indice]      = '<bean:write name="tipoDocumento" property="personaFisica"/>';                
                indice++;
            </logic:iterate>        
            
            mensajeGuardarCambios='<%=descriptor.getDescripcion("msjExistenDatosGuardar")%>';
            cargarCombos();
            window.focus();
            tp1.setSelectedIndex(<%=pestana%>);
            <%GeneralValueObject expedienteVO = expForm.getExpedienteVO();
            String notificacionRealizada = (String) expedienteVO.getAtributo("notificacionRealizada");
            String estadoExpediente = (String) expedienteVO.getAtributo("estado");
            if (notificacionRealizada.equals("no")) {%>
                msnj = "<%=descriptor.getDescripcion("msjNotifNoRealiz")%>";
                jsp_alerta("A",msnj);
            <% }%>

            expediente = ["<%=expedienteVO.getAtributo("codMunicipio")%>", "<%=expedienteVO.getAtributo("codProcedimiento")%>",
                "<%=expedienteVO.getAtributo("ejercicio")%>", "<%=expedienteVO.getAtributo("numero")%>"];
            document.forms[0].ejercicio.value = expediente[2];
            admin="<%=(String) expedienteVO.getAtributo("GrupoUsuario")%>";            
            
            // Se cargan los roles
            var m=0;    
            <logic:iterate name="FichaExpedienteForm" property="listaRoles" id="rol">
                codRols[m]  = '<bean:write name="rol" property="codigo"/>';
		descRols[m] = '<bean:write name="rol" property="descripcion"/>';  
                pDef[m]     = '<bean:write name="rol" property="pde"/>';                
                <c:if test="${rol.pde eq 'SI'}">
                    rolPorDefecto    = codRols[m];
                    descRolPorDefecto = descRols[m];
                </c:if>
                m++;
            </logic:iterate>
            
    <%
            String poseeLocalizacion = (String) expedienteVO.getAtributo("poseeLocalizacion");
            Vector tramites = null;
            tramites = (Vector) expForm.getTramites();
            if (tramites != null) {
                int lengthTramites = tramites.size();
      %>
          var j=0;
      <%
          for (int i = 0; i < lengthTramites; i++) {
              GeneralValueObject tramiteVO = (GeneralValueObject) tramites.get(i);%>

                  listaTramites[j] = ["<%=tramiteVO.getAtributo("tramite")%>",
                      "<%=tramiteVO.getAtributo("fehcaInicio")%>",
                      "<%=tramiteVO.getAtributo("fechaFin")%>",
                      "<%=tramiteVO.getAtributo("unidad")%>",
                      "<%=tramiteVO.getAtributo("clasificacion")%>",
                      ""];
        <%if (!tramiteVO.getAtributo("usuarioBloq").equals("")) {
              if (tramiteVO.getAtributo("codUsuarioBloq").toString().equals(String.valueOf(usuario.getIdUsuario()))) {%>
                      listaTramites[j][5]='<span class="fa fa-lock" alt="<%=tramiteVO.getAtributo("usuarioBloq")%>"></span>';
                <%} else {%>
                      listaTramites[j][5]='<span class="fa fa-unlock" alt="<%=tramiteVO.getAtributo("usuarioBloq")%>"></span>';
            <%}
          }%>
          listaTramitesOriginal[j] = ["<%=tramiteVO.getAtributo("ocurrenciaTramite")%>",
              "<%=tramiteVO.getAtributo("codTramite")%>",
              "<%=tramiteVO.getAtributo("tramite")%>",
              "<%=tramiteVO.getAtributo("fehcaInicio")%>",
              "<%=tramiteVO.getAtributo("fechaFin")%>",
              "<%=tramiteVO.getAtributo("unidad")%>",
              "<%=tramiteVO.getAtributo("usuario")%>",
              "<%=tramiteVO.getAtributo("clasificacion")%>",
              "<%=tramiteVO.getAtributo("consultar")%>",
              "<%=tramiteVO.getAtributo("fueraDePlazo")%>",
              "<%=tramiteVO.getAtributo("codUniTramTramite")%>",
              "<%=tramiteVO.getAtributo("tramiteInicio")%>",
              "<%=tramiteVO.getAtributo("codUsuario")%>",
              "<%=tramiteVO.getAtributo("codUsuarioFinalizacion")%>",
              "<%=tramiteVO.getAtributo("usuarioBloq")%>",
              "<%=tramiteVO.getAtributo("codUsuarioBloq")%>",
              "<%=tramiteVO.getAtributo("plazoCercaFin")%>",
			  "<%=tramiteVO.getAtributo("traNotificacionElectronica")%>",
              "<%=tramiteVO.getAtributo("tieneTareasPendientesInicio")%>",
              "<%=tramiteVO.getAtributo("notificacionObligatoria")%>",
              "<%=tramiteVO.getAtributo("certificadoOrganismo")%>"];
              			  
             listaTramitesPlugin[j] = ["<%=tramiteVO.getAtributo("tramite")%>","<%=tramiteVO.getAtributo("codTramite")%>","<%=tramiteVO.getAtributo("ocurrenciaTramite")%>","<%=tramiteVO.getAtributo("extPlugin")%>","<%=tramiteVO.getAtributo("extUrl")%>","<%=tramiteVO.getAtributo("extImplClass")%>","<%=tramiteVO.getAtributo("extIdEnlace")%>","<%=tramiteVO.getAtributo("extBloqueoFinalizarTramite")%>","<%=tramiteVO.getAtributo("extBloqueoRetrocesoTramite")%>"];
             j++;
      <%
                }
            }
      %>          
          tabTramites.lineas = listaTramites;
          tabTramites.displayTabla();
          comboRol.addItems(codRols,descRols);
   <%Vector permisosTramites = null;
            permisosTramites = (Vector) expForm.getPermisosTramites();
            if (permisosTramites != null) {
                int lengthPermisosTramites = permisosTramites.size();%>
                    var j=0;
                    <%for (int i = 0; i < lengthPermisosTramites; i++) {%>
                    listaPermisosTramites[j] = "<%=(String) permisosTramites.get(i)%>";
                    j++;
        <%}
            }%>
            document.forms[0].modoConsulta.value = "<%=(String) expedienteVO.getAtributo("modoConsulta")%>";            
            (document.forms[0].modoConsulta.value=="si" || document.forms[0].modoConsulta.value=="SI")?gConsultando = true:gConsultando=false;
            desdeAltaRE = "<%=(String) expedienteVO.getAtributo("desdeAltaRE")%>";
            document.forms[0].desdeConsulta.value = "<%=(String) expedienteVO.getAtributo("desdeConsulta")%>";
            document.forms[0].expRelacionado.value = "<%=(String) expedienteVO.getAtributo("expRelacionado")%>";
            document.forms[0].deAdjuntar.value = "<%=(String) expedienteVO.getAtributo("deAdjuntar")%>";
            document.forms[0].desdeInformesGestion.value = "<%=(String) expedienteVO.getAtributo("desdeInformesGestion")%>";
            document.forms[0].todos.value = "<%=(String) expedienteVO.getAtributo("todos")%>";
            document.forms[0].observaciones.value = unescape("<%=StringEscapeUtils.escapeJavaScript((String) expedienteVO.getAtributo("observaciones"))%>");
            document.forms[0].asunto.value = unescape("<%=StringEscapeUtils.escapeJavaScript((String) expedienteVO.getAtributo("asunto"))%>");
            document.forms[0].permiteModificarObs.value = "<%=(String) expedienteVO.getAtributo("permiteModificarObs")%>";
            document.forms[0].permiteMantenerDocExt.value = "<%=(String) expedienteVO.getAtributo("permiteMantenerDocExt")%>";

                    // Terceros
                    var indexInt = 0;
                    var listaMostrar = "";
                    
                    <c:forEach var="tercero" items="${FichaExpedienteForm.terceros}">
                    var domicilio = "<str:escape><c:out value="${tercero.domicilio}" escapeXml = "false"/></str:escape>" + ' - ' + '<c:out value="${tercero.municipio}"/>' + ' - ' + '<c:out value="${tercero.provincia}"/>';
                        terceros[indexInt] = [<c:out value="${tercero.codTercero}"/>, <c:out value="${tercero.numVersion}"/>,
                                '<c:out value="${tercero.nombreCompleto}"/>', '<c:out value="${tercero.descRol}"/>',
                                <c:out value="${tercero.codDomicilio}"/>,
                                domicilio,
                                <c:out value="${tercero.porDefecto}"/>, <c:out value="${tercero.codigoRol}"/>,
                                <c:out value="${tercero.mostrar}"/>];
                        terceros[indexInt][15] = "<c:out value='${tercero.txtDoc}'/>";
                        listaMostrar += "<c:out value='${tercero.mostrar}'/>" + "§¥";
                        listaInteresados[indexInt] = "<c:out value='${tercero.tipoDoc}'/>" + "#" + "<c:out value='${tercero.txtDoc}'/>" + "#" + "<c:out value='${tercero.nombreCompleto}'/>" + "#" + domicilio;
                        listaNotificacionesElectronicas[indexInt]=  "<c:out value='${tercero.admiteNotificacion}'/>";
                        if( listaNotificacionesElectronicas[indexInt]=="1") numInteresadosAdmitenNotifElect++;
                        
						indexInt++;
                                                
                                          
                    </c:forEach>
                    // Hay algun interesado que admite notificaciones
                    if(numInteresadosAdmitenNotifElect>0) existenInteresadosNotifElectronica=1;
                    
                    // Hacemos combo rol obligatorio si hay terceros
                    if (terceros.length > 0) hacerComboRolObligatorio(true);
                    else hacerComboRolObligatorio(false);
                    
                        document.forms[0].listaMostrar.value = listaMostrar;
			desactivarAdmiteNotif();
                        valoresInicialesTercero();                        
			inicializarTablaOtrosDocumentos();
                        mostrarCapasBotones('capaBotones3');
                        document.forms[0].desdeExpRel.value="<%=request.getParameter("desdeExpRel")%>";
                        document.forms[0].porCampoSup.value="<%=request.getParameter("porCampoSup")%>";
                        cargarFormularios();

                        if(document.forms[0].modoConsulta.value == "si") {
                            desactivarFormulario();
                            document.forms[0].admiteNotificacion.disabled = true;                            
                            mostrarCapasBotones('capaBotones1');                            
                            // Se cambia el estilo del Botón Imprimir
                             var botonesImprimir = document.getElementsByName("cmdImprimirCons");
                             if(botonesImprimir!=null && botonesImprimir.length>=1){
                                botonesImprimir[0].className = "botonGeneral";
                             }
                             // Se cambia el estilo del Botón Volver
                             var botonesVolver = document.getElementsByName("cmdVolver");
                             if(botonesVolver!=null && botonesVolver.length>=1){
                                botonesVolver[0].className = "botonGeneral";
                             }
                            
                            if (document.forms[0].desdeConsulta.value != "si") { 
                                mostrarCapasBotones('capaBotones11');
                                if(document.forms[0].cmdImprimirCons11){
                                    document.forms[0].cmdImprimirCons11.disabled = false;
                                }//if(document.forms[0].cmdImprimirCons11)
                                if(document.forms[0].cmdVolver11){
                                    document.forms[0].cmdVolver11.disabled = false;
                                }//if(document.forms[0].cmdVolver11)
                                if(document.forms[0].cmdFinalizar){
                                    document.forms[0].cmdFinalizar.disabled = true;
                                }//if(document.forms[0].cmdFinalizar)
                                if(document.forms[0].admiteNotificacion){
                                    document.forms[0].admiteNotificacion.disabled = true;
                                }//if(document.forms[0].admiteNotificacion)
                            }
                            //tabDocumentos.readOnly = true;   
                            var vector = [document.forms[0].botonTerceros];
                            document.forms[0].cmdTramitacionManual.disabled = true; document.getElementById('cmdTramitacionManual').style.color = '#CCCCCC';
                            document.forms[0].cmdTramitacionManual.style.cursor = '';
                            document.forms[0].cmdRetrocederExpediente.disabled = true; document.getElementById('cmdRetrocederExpediente').style.color = '#CCCCCC';
                            document.forms[0].cmdFinalizar.disabled = true;  document.getElementById('cmdFinalizar').style.color = '#CCCCCC';
                            
                            <%if ("no".equals(soloConsultarExp)) {%>
                            if(document.forms[0].fechaFin.value == "") {
                                document.forms[0].cmdRetrocederExpediente.style.cursor = 'hand';
                                document.forms[0].cmdRetrocederExpediente.disabled = true;
                                document.forms[0].cmdExpRel.disabled = false;
                                // Cambiar el estilo del Botón Exp. Relacionados
                                var botonesExpRel = document.getElementsByName("cmdExpRel");
                                if(botonesExpRel!=null && botonesExpRel.length>=1){
                                    botonesExpRel[0].className = "botonMasLargo";
                                }
                            }
                            <% }%>
                            if(document.forms[0].expRelacionado.value == "si") {
                                document.getElementById('expedientesRel').style.visibility='hidden';
                                document.forms[0].cmdRetrocederExpediente.style.cursor = '';
                                document.forms[0].cmdRetrocederExpediente.disabled = true;
                                document.forms[0].cmdExpRel.disabled = true;
                                document.forms[0].cmdFinalizar.style.cursor = '';
                                document.forms[0].cmdFinalizar.disabled = true;
                            }
                            if(document.forms[0].deAdjuntar.value == "si") {
                                mostrarCapasBotones('capaBotones2');
                                if(document.forms[0].cmdCancelar){
                                    document.forms[0].cmdCancelar.disabled = false;
                                    document.forms[0].cmdCancelar.style.color = '#FFFFFF';
                                    document.forms[0].cmdCancelar.style.cursor = 'hand';
                                }//if(document.forms[0].cmdCancelar)
                                if(document.forms[0].cmdAceptar){
                                    document.forms[0].cmdAceptar.disabled = false;
                                    document.forms[0].cmdAceptar.style.color = '#FFFFFF';
                                    document.forms[0].cmdAceptar.style.cursor = 'hand';
                                }//if(document.forms[0].cmdAceptar)
                                
                                if(document.forms[0].cmdRetrocederExpediente){
                                    document.forms[0].cmdRetrocederExpediente.style.cursor = '';
                                    document.forms[0].cmdRetrocederExpediente.disabled = true;
                                }//if(document.forms[0].cmdRetrocederExpediente)
                            }
                            if(document.forms[0].desdeInformesGestion.value == "si") {
                                document.forms[0].cmdRetrocederExpediente.style.cursor = '';
                                document.forms[0].cmdRetrocederExpediente.disabled = true;
                            }
                            if (desdeAltaRE == "si") {
                                document.forms[0].cmdRetrocederExpediente.style.cursor = '';
                                document.forms[0].cmdRetrocederExpediente.disabled = true;
                                document.forms[0].cmdFinalizar.style.cursor = '';
                                document.forms[0].cmdFinalizar.disabled = true;
                            }
                            // Desactivar edicion interesados
                            comboRol.deactivate();
                            comboTipoDoc.deactivate();                            
                            mostrarOcultarIcono("botonEliminarInteresado","off");
                            mostrarOcultarIcono("botonListaDomicilios","off");
                            mostrarOcultarIcono("botonBusquedaTerceros","off");
                            
                            if(document.forms[0].permiteModificarObs.value == "si") {                                 
                                mostrarCapasBotones('capaBotones4');
                                var vector1= [document.forms[0].observaciones];
                                habilitarGeneral(vector1);
                                if(document.forms[0].cmdGrabarObservaciones!=null )                                     
                                    document.forms[0].cmdGrabarObservaciones.disabled = false;                               
                                if(document.forms[0].cmdVolverTramitacion4!=null)
                                    document.forms[0].cmdVolverTramitacion4.disabled = false;
                                if(document.forms[0].cmdImprimir4!=null)
                                    document.forms[0].cmdImprimir4.disabled = false;                                    
                            }
                            
                            if(document.forms[0].permiteMantenerDocExt.value == "si" && desdeAltaRE == 'no') {                                 
                                var botones = [document.forms[0].cmdAltaOtroDocumento, document.forms[0].cmdEliminarOtroDocumento, document.forms[0].cmdVerOtroDocumento];                                    
                                habilitarGeneral(botones);
                            }
                            
                            if(document.forms[0].expHistorico.value=="true"){
                                mostrarCapasBotones('capaBotones12');
                                if(document.forms[0].cmdImprimirCons12!=null ) document.forms[0].cmdImprimirCons12.disabled = false;
                                if(document.forms[0].cmdVolverTramitacion12!=null )  document.forms[0].cmdVolverTramitacion12.disabled = false;
                                
                            }
                        }
                        if(document.forms[0].poseeLocalizacion.value == 0) {
                            mostrarCapas('capaLocalizacion');
                        }                      
    
        if (document.getElementById('capaDatosSuplementarios'))
            document.getElementById('capaDatosSuplementarios').style.visibility='visible';
        inicializarBotonesDatosSuplementarios();

   <%
            if ("si".equals(request.getAttribute("msgExpSinTercero"))) {
   %>
       jsp_alerta('A','<%=descriptor.getDescripcion("msgExpSinTercero")%>');
   <%
            }
   %>
       compruebaModificadoExpediente();

    <%
            ArrayList<InteresadoExpedienteVO> ters = expForm.getTerceros();
            if (ters != null && ters.size() >= 1) {
                int indexMostrar = ters.size() - 1;
                for (int i = 0; i < ters.size(); i++) {
                    InteresadoExpedienteVO inter = ters.get(i);
                    if (inter.isMostrar()) {
                        indexMostrar = i;
                    }
                }
                InteresadoExpedienteVO inter = (InteresadoExpedienteVO) ters.get(indexMostrar);
    %>
        terceroActual    = '<%= inter.getCodTercero() %>';
        domicilioActual  = '<%= StringEscapeUtils.escapeJavaScript(inter.getDomicilio()) %> - <%= StringEscapeUtils.escapeJavaScript(inter.getMunicipio())%> - <%= inter.getProvincia()%>';
        rolActual        = '<%= inter.getCodigoRol() %>';
        admiteNotificacion = '<%=inter.getAdmiteNotificacion()%>';
        document.forms[0].titular.value = "<%= StringEscapeUtils.escapeJavaScript(inter.getNombreCompleto())%>";
        document.forms[0].codTipoDoc.value = "<%=inter.getTipoDoc()%>";
        document.forms[0].descTipoDoc.value = getDescTipoDoc("<%=inter.getTipoDoc()%>");
        document.forms[0].txtDNI.value = "<%=inter.getTxtDoc()%>";
        if (document.forms[0].txtDNI.value=='null') document.forms[0].txtDNI.value='';
        document.forms[0].domicilio.value = domicilioActual;
        if(admiteNotificacion == 1){
                document.forms[0].admiteNotificacion.checked = true;
        }else{
                document.forms[0].admiteNotificacion.checked = false;
        }
            posInteresadoActual= '<%= indexMostrar + 1 %>';
            updateMsgNumInteresados();
            // Se fija el rol
            var descript = new Array();
            descript = getDescRol(getPosTerceroActual());
            comboRol.buscaCodigo(descript[0]);
            if(terceroActual=='' || document.forms[0].modoConsulta.value == "si"){
                mostrarOcultarIcono("botonEliminarInteresado","off");
                mostrarOcultarIcono("botonListaDomicilios","off");
                mostrarOcultarIcono("botonListaInteresados","off");
            }else{
            mostrarOcultarIcono("botonEliminarInteresado","on");
            mostrarOcultarIcono("botonListaDomicilios","on");
            mostrarOcultarIcono("botonListaInteresados","on");
        }

        if (terceroActual!='') mostrarOcultarIcono("botonListaInteresados","on");
        var terSig = getCodTerceroSiguiente();
        var terAnt = getCodTerceroAnterior();

        if(terAnt==-1)
            deshabilitarImagenBoton("imgAnt",true);
        else
            deshabilitarImagenBoton("imgAnt",false);

        if(terSig==-1)
            deshabilitarImagenBoton("imgNext",true);
        else
            deshabilitarImagenBoton("imgNext",false);
       //Si el expediente tiene expedientes relacionados, aun estando finalizado se podran consultar sus exp. relacionados
    <%
      }
    %>
      var tieneExpRelacionados ="<%=(Boolean) expedienteVO.getAtributo("expedientesRelacionados")%>";        
      if(tieneExpRelacionados=="true"){
         // Se habilita el botón de exp. relacionados
         document.forms[0].cmdExpRel.disabled = false;
      }
		
     <%	if ((fechaFinExpediente != null) && (!fechaFinExpediente.equalsIgnoreCase(""))){ %>
            habilitarBotonesOtrosDocumentos();
            habilitarBotonVerNotificacion();
     <% } %>       
            
     if(checkNotificacionesHabilitado){	
           $('#admiteNotificacion').prop('disabled',false);	
       }else{	
           $('#admiteNotificacion').prop('disabled',true);	
	 }
       if(<%=mostrarCheckNotificaciones%>){
         $(".notificacionDatos").css("display", "none"); 
       }
       // #380559
       <%
           if ("si".equals(request.getAttribute("MostrarMensajeRespuestaValRdR"))) {
       %>
            jsp_alerta('A','<%=request.getAttribute("mensajeRespuestaValRdR")%>');
       <%
           }
       %>
      pleaseWait1('off',this);
    }
    
    function enlaces() {
        var htmlString = "";
        if (listaEnlaces.length > 0){
            htmlString += '<table width="100%">';
            for(i=0;i<listaEnlaces.length;i++) {
                htmlString += '<tr><td class="etiqueta">';
                htmlString += '<a href=' + listaEnlaces[i][1];
                htmlString += ' target=blank><b>';
                htmlString += listaEnlaces[i][0] + '</font></b></a></td></tr>';
            }
            htmlString += '</table>';
        }
        return (htmlString);
    }
    function desactivarFormulario() { 
        deshabilitarDatos(document.forms[0]);
        document.forms[0].cmdExpRel.disabled = true;
        document.forms[0].cmdExpRel.style.cursor = 'hand';
        for(var i=0;i<listaDocumentos.length;i++) {
            eval("document.forms[0].documentoEntregado" + i + ".disabled=true");
        }
        if(document.forms[0].cmdImprimirCons != null){
            document.forms[0].cmdImprimirCons.disabled = false;
            document.forms[0].cmdImprimirCons.style.cursor = "hand";
        }//if(document.forms[0].cmdImprimirCons != null)
        if(document.forms[0].cmdVolver){
            document.forms[0].cmdVolver.disabled = false;
            document.forms[0].cmdVolver.style.cursor = "hand";
        }//if(document.forms[0].cmdVolver)
        <%if ("si".equals(m_Config.getString("JSP.Formularios")) && (expForm.getFormularios().size() > 0)) {%>
        document.forms[0].cmdVerAnexo.disabled = false;
        <%}%>
    }
	
 
    function pulsarInteresados(){

        var source = "<c:url value='/sge/Interesados.do?opcion=cargar&codMun='/>"+expediente[0]+"&codProc="+expediente[1]+"&eje="+expediente[2]+"&num="+expediente[3];
        expediente[4] = terceros;
        if(document.forms[0].modoConsulta.value == "si") {
            expediente[5] = "soloConsulta";
        } else {
        expediente[5] = "normal";
    }
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,expediente,
        'width=990,height=410,status='+ '<%=statusBar%>',function(datos){
            if(datos!=undefined){
                document.forms[0].listaCodTercero.value = datos[0];
                document.forms[0].listaVersionTercero.value = datos[1];
                document.forms[0].listaCodDomicilio.value = datos[2];
                document.forms[0].listaRol.value = datos[3];
                document.forms[0].listaMostrar.value = datos[4];
                document.forms[0].titular.value = datos[5];
                var nuevosTerceros = datos[6];
                terceros = new Array();
                terceros = nuevosTerceros;
            }
            pleaseWait1('off',this);
        });
    }
   
    /* Pestaña registro */
    var listaAsientos = new Array();
    var listaAsientosOriginal = new Array();
    var listaObs = new Array();
                    
    function consultarAsiento(i) {
       
        if(permisoConsultarAsiento(i)){
            pleaseWait1('on',this);
            document.forms[0].codDepartamento.value = listaAsientosOriginal[i][0];
            document.forms[0].codUnidadRegistro.value = listaAsientosOriginal[i][1];
            document.forms[0].tipoRegistro.value = listaAsientosOriginal[i][2];
            document.forms[0].ejerNum.value = listaAsientosOriginal[i][3];
            document.forms[0].origenServicio.value = listaAsientosOriginal[i][7];
            document.forms[0].codigoUnidadDestino.value = listaAsientosOriginal[i][8];
            document.forms[0].opcion.value="consultaAsiento";
            document.forms[0].target="mainFrame";
            document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
            document.forms[0].submit();
        }

    } 
    
    function permisoConsultarAsiento(i){
        var ajax = getXMLHttpRequest();

        var exito = false;
        var codigoUnidadDestino= listaAsientosOriginal[i][8];

        var result ="";    
        if(ajax!=null){                                                         
            var url = getContextPath() + "/sge/FichaExpediente.do";       
            var parametros = "&opcion=permisoConsultarAsiento&codigoUnidadDestino=" + codigoUnidadDestino ;
            
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);
            try{            
                if (ajax.readyState==4 && ajax.status==200){
                    var xmlDoc = null;											
                    var text = ajax.responseText;											
                    var datosRespuesta = text.split("=");
                    
                    if(datosRespuesta!=null && datosRespuesta.length==2 && datosRespuesta[0].trim()=="PERMISO" && (datosRespuesta[1].trim()=="SI" || datosRespuesta[1].trim()=="si")){
                        exito=true;
                    }
                    else{
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msgNoPermisoAnotacionExp")%>');
                    }
                }
                
            }catch(Err){
                alert("Error.descripcion: " + Err.description);            
            }
        }

        return exito;
    }
    
    var listaDocumentos = new Array();
    var listaDocumentosOriginalAuxiliar = new Array();
    var listaDocumentosOriginal = new Array();                    
    
    
    /** Se encarga de eliminar un fichero adjunto a un documento de expediente */
    function eliminarAdjuntoDocumento(objeto){
        var posicion = tabDocumentos.selectedIndex;
        if(posicion==-1){
                jsp_alerta('A','<%=descriptor.getDescripcion("msgDocumentoNoSeleccionado")%>');
        }else{

             if((getNumInteresadosExpediente() == 0) && (interesado_Obligatorio()) == "1"){ 
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjInterOblig")%>');                                            
             }else{           
                var nombreAdjunto = listaDocumentos[posicion][3];
                var tipoMime	  = listaDocumentos[posicion][4];
                var fechaAdjunto  = listaDocumentos[posicion][5];

                if(nombreAdjunto.length==0 && tipoMime.length==0 && fechaAdjunto.length==0){
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msgEliminarDocExpNoPosible")%>');
                }else if (("<%=descriptor.getDescripcion("gEtiq_estadoFirmaN")%>"==listaDocumentos[posicion][6]) || (listaDocumentos[posicion][6]=="")) {  //Si no requiere firma
                        if(jsp_alerta('C','<%=descriptor.getDescripcion("msgEliminarDocumentoExpediente")%>')==1)
                        {
                            var codigoDocumento  = listaDocumentosOriginal[posicion][0];
                            var codMunicipio  	 = document.forms[0].codMunicipio.value;
                            var ejercicio     	 = document.forms[0].ejercicio.value;
                            var numExpediente 	 = document.forms[0].numExpediente.value;
                            var codProcedimiento = document.forms[0].codProcedimiento.value;
                            var nombreAdjunto = listaDocumentos[posicion][3];

                            var parametros = "?opcion=documentoEliminar&codigoDocumento=" + codigoDocumento + "&codMunicipio=" + codMunicipio +
                            "&ejercicio=" + ejercicio + "&numExpediente=" + numExpediente + "&codProcedimiento=" + codProcedimiento + "&nombreAdjunto=" + nombreAdjunto +
                            "&tipoMime=" + tipoMime + "&fechaAdjunto=" + fechaAdjunto;

                            var source = "<html:rewrite page='/expediente/FichaExpedienteDocumentosPresentados.do'/>" + parametros;
                            document.forms[0].target="oculto";
                            document.forms[0].action = source;
                            document.forms[0].submit();
                        }
                } else{                    
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msgNoElimDocCircuito")%>');
                }
           }
        }
    }
                                           
    function verificarDocumentosPresentados(objeto){
            var nombrecheck = objeto.name;
            //El nombre del check siempre es "documentoEntregado" mas el ID del documento. El codigo del documento será ese ID final.
            var posicion    = nombrecheck.substring(18,nombrecheck.length);
            var codigoDocumento = listaDocumentosOriginal[posicion][0];
            var codMunicipio  	 = document.forms[0].codMunicipio.value;
            var ejercicio     	 = document.forms[0].ejercicio.value;
            var numExpediente 	 = document.forms[0].numExpediente.value;
            var codProcedimiento = document.forms[0].codProcedimiento.value;

            if(!objeto.checked){
                    // Si el documento deja de estar chequeado y tiene un documento adjunto hay que eliminar el adjunto y desmarcar el documento como presentado
                    var nombreAdjunto = listaDocumentos[posicion][3];
                    var tipoMime	  = listaDocumentos[posicion][4];
                    var fechaAdjunto  = listaDocumentos[posicion][5];
                    var continuar 	  = false;
                    var parametros = "?opcion=desmarcarDocumentoEliminar&codigoDocumento=" + codigoDocumento + "&codMunicipio=" + codMunicipio +
                                    "&ejercicio=" + ejercicio + "&numExpediente=" + numExpediente + "&codProcedimiento=" + codProcedimiento;
                    parametros += "&nombreAdjunto=" + nombreAdjunto + "&tipoMime=" + tipoMime + "&fechaAdjunto=" + fechaAdjunto;

                    if(nombreAdjunto!="" && tipoMime!="" && fechaAdjunto!=""){
                        if(jsp_alerta('C','<%=descriptor.getDescripcion("msgDocExpAdjuntoNoPresentado")%>')==1){
                            continuar = true;
                        }else{
                            objeto.checked = true;
                        }
                    }else{
                        if(jsp_alerta('C','<%=descriptor.getDescripcion("msgDocExpNoPresentado")%>')==1){
                            continuar = true;
                        }else
                            objeto.checked = true;
                    }

                    if(continuar){                                                            
                            if (("<%=descriptor.getDescripcion("gEtiq_estadoFirmaN")%>"==listaDocumentos[posicion][6])||(listaDocumentos[posicion][6]=="")) { 
                            var source = "<html:rewrite page='/expediente/FichaExpedienteDocumentosPresentados.do'/>" + parametros;
                            document.forms[0].target="oculto";
                            document.forms[0].action = source;
                            document.forms[0].submit();
                            }else 
                                {
                                    jsp_alerta("A",'<%=descriptor.getDescripcion("msgNoElimDocCircuito")%>');
                                    objeto.checked = true;
                                }
                    }
            }else{
                    // El documento pasa a estar chequeado, por tanto, se marca como presentado
                    var parametros = "?opcion=marcarDocumentoPresentado&codigoDocumento=" + codigoDocumento + "&codMunicipio=" + codMunicipio +
                                    "&ejercicio=" + ejercicio + "&numExpediente=" + numExpediente + "&codProcedimiento=" + codProcedimiento;

                    var source = "<html:rewrite page='/expediente/FichaExpedienteDocumentosPresentados.do'/>" + parametros;
                    document.forms[0].target="oculto";
                    document.forms[0].action = source;
                    document.forms[0].submit();
            }
    }
                    
      
  
  function verificarFirmaDoc(codDocumento,cD){ 
    var source = "<%=request.getContextPath()%>/expediente/FichaExpedienteDocumentosPresentados.do?opcion=verificarFirma" +
                 "&codDocumentoAdjunto="+codDocumento+"&idDocumento="+cD;
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,[],
        'width=600,height=320,status=no',function(){});
  }
  function pulsarCancelarActualizarDocumentos(){
    var codMunicipio  	 = document.forms[0].codMunicipio.value;
    var ejercicio     	 = document.forms[0].ejercicio.value;
    var numExpediente 	 = document.forms[0].numExpediente.value;
    var codProcedimiento = document.forms[0].codProcedimiento.value;
    
    for(i=0;i<listaDocumentos.length;i++){
        var documento = listaDocumentos[i];
        var inputActual = documento[0];
        var documentoOriginal = listaDocumentosOriginalAuxiliar[i];
        var inputOriginal = documentoOriginal[0];
        var indexCheckedActual   = inputActual.indexOf("CHECKED");
        var indexCheckedOriginal = inputOriginal.indexOf("CHECKED");
        if(indexCheckedActual==-1 && indexCheckedOriginal!=-1){
            // Originalmente figuraba como presentad
            var nombreAdjunto = documento[3];
            var tipoMime  = documento[4];
            var fechaAlta = documento[5];
            var codigoDocumento = listaDocumentosOriginal[i][0];
            // El documento pasa a estar chequeado, por tanto, se marca como presentado
            var parametros = "?opcion=marcarDocumentoPresentado&codigoDocumento=" + codigoDocumento + "&codMunicipio=" + codMunicipio +
                            "&ejercicio=" + ejercicio + "&numExpediente=" + numExpediente + "&codProcedimiento=" + codProcedimiento;

            if(nombreAdjunto.length==0 && tipoMime.length==0 && fechaAlta.length==0){
                listaDocumentosOriginalAuxiliar[i][3] = "";
                listaDocumentosOriginalAuxiliar[i][4] = "";
                listaDocumentosOriginalAuxiliar[i][5] = "";
                listaDocumentosOriginalAuxiliar[i][6] = "";
                listaDocumentosOriginalAuxiliar[i][7] = "";
                listaDocumentosOriginalAuxiliar[i][8] = "";
            }
            var source = "<html:rewrite page='/expediente/FichaExpedienteDocumentosPresentados.do'/>" + parametros;
            document.forms[0].target="oculto";
            document.forms[0].action = source;
            document.forms[0].submit();
        }else{
            if(indexCheckedActual!=-1 && indexCheckedOriginal==-1){
                // Originalmente figuraba como no presentado
                var codigoDocumento = listaDocumentosOriginal[i][0];
                var nombreDocumento = listaDocumentosOriginal[i][1];
                var nombreAdjunto = documento[3];
                var tipoMime  = documento[4];
                var fechaAlta = documento[5];
                var parametros = "?opcion=desmarcarDocumentoEliminar&codigoDocumento=" + codigoDocumento + "&codMunicipio=" + codMunicipio +
				 "&ejercicio=" + ejercicio + "&numExpediente=" + numExpediente + "&codProcedimiento=" + codProcedimiento;
                var continuar = false;
                if(nombreAdjunto.length>0 && tipoMime.length>0 && fechaAlta.length>0){
                    if(jsp_alerta("C",'<%=descriptor.getDescripcion("msgCancelarDocExp1")%> '  + nombreDocumento + ' <%=descriptor.getDescripcion("msgCancelarDocExp2")%>')==1){
                        continuar = true;
                        listaDocumentosOriginalAuxiliar[i][3] = "";
                        listaDocumentosOriginalAuxiliar[i][4] = "";
                        listaDocumentosOriginalAuxiliar[i][5] = "";
                        listaDocumentosOriginalAuxiliar[i][6] = "";
                        listaDocumentosOriginalAuxiliar[i][7] = "";
                        listaDocumentosOriginalAuxiliar[i][8] = "";
                    }
                    else { 
                        continuar = false;
                        listaDocumentosOriginalAuxiliar[i][0] = listaDocumentos[i][0];
                        listaDocumentosOriginalAuxiliar[i][1] = listaDocumentos[i][1];
                        listaDocumentosOriginalAuxiliar[i][2] = listaDocumentos[i][2];
                        listaDocumentosOriginalAuxiliar[i][3] = listaDocumentos[i][3];
                        listaDocumentosOriginalAuxiliar[i][4] = listaDocumentos[i][4];
                        listaDocumentosOriginalAuxiliar[i][5] = listaDocumentos[i][5];
                        listaDocumentosOriginalAuxiliar[i][6] = listaDocumentos[i][6];
                        listaDocumentosOriginalAuxiliar[i][7] = listaDocumentos[i][7];
                        listaDocumentosOriginalAuxiliar[i][8] = listaDocumentos[i][8];
                    }
                }else continuar = true;

                if(continuar){
                    var source = "<html:rewrite page='/expediente/FichaExpedienteDocumentosPresentados.do'/>" + parametros;
                    document.forms[0].target="oculto";
                    document.forms[0].action = source;
                    document.forms[0].submit();
                }
            }
        }  
    }//for    
    tabDocumentos.lineas=listaDocumentosOriginalAuxiliar;
    tabDocumentos.displayTabla();
  }

  function actualizarListaDocumentosOriginalAuxiliar(){
	for(i=0;i<listaDocumentosOriginalAuxiliar.length;i++){
		var documentoOriginal = listaDocumentosOriginalAuxiliar[i];
		var documentoActual   = listaDocumentos[i];

		var nombreAdjuntoOriginal = documentoOriginal[3];
		var tipoMimeOriginal 	  = documentoOriginal[4];
		var fechaAltaOriginal     = documentoOriginal[5];

		var nombreAdjuntoActual   = documentoActual[3];
		var tipoMimeActual   	  = documentoActual[4];
		var fechaAltaActual       = documentoActual[5];
		var estadoFirma			  = documentoActual[6];

		if(nombreAdjuntoOriginal.length>0 && tipoMimeOriginal.length>0 && fechaAltaOriginal.length>0
			&& nombreAdjuntoActual.length==0 && tipoMimeActual.length==0 && fechaAltaActual.length==0 ){
			// Si el documento actualmente no tiene fichero adjunto es porque se ha eliminado o se ha
			// marcado el documento de expediente como no presentado. Inicialmente si tenía fichero adjunto.
			listaDocumentosOriginalAuxiliar[i][3] = "";
			listaDocumentosOriginalAuxiliar[i][4] = "";
			listaDocumentosOriginalAuxiliar[i][5] = "";
			listaDocumentosOriginalAuxiliar[i][6] = "";
			listaDocumentosOriginalAuxiliar[i][7] = "";
                        listaDocumentosOriginalAuxiliar[i][8] = "";
		}
	}
  }

/* Pestaña Otros Documentos  */
var listaOtrosDocumentos = new Array();


// Funcion que asocia a un expediente un documento seleccionado en
// nuestro de sistema de archivos
function pulsarAltaOtroDocumento()
{    
    if ((getNumInteresadosExpediente()== 0) && (interesado_Obligatorio() == "1")){
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjInterOblig")%>');
    }else{
        source = "<html:rewrite page='/sge/FichaExpedienteDocumento.do'/>?opcion=documentoNuevo";      
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana1',
                'width=500,height=500,status='+ '<%=statusBar%>',function(listaDocsExternosJSON){
            if(listaDocsExternosJSON!=undefined){
                // Se recibe el String en formato JSON con los datos de los documentos externos 
                // una vez dado de alta el nuevo            
                procesarRespuestaListadoDocumentosExternos(listaDocsExternosJSON)
            }
                  });
    }
}

       function mostrarNombre(campoDoc) {            
	    componente = campoDoc;
            nombreArchivoCompleto = componente.value.split("\\"); // Windows
            if (nombreArchivoCompleto.length == 0) {
               nombreArchivoCompleto = componente.value.split("/"); //Linux, una vez más para todos!
            }

            var nombreArchivoSinExtension = nombreArchivoCompleto[nombreArchivoCompleto.length-1];
            var soloNombre = nombreArchivoSinExtension.split(".");

            return soloNombre[soloNombre.length-2];
       }

	function existeOtroDocumento(nombreDocumento){
        var yaExiste = false;

        for(l=0; l < listaOtrosDocumentos.length; l++){
            if (listaOtrosDocumentos[l][1] == nombreDocumento ){
                yaExiste = true;
                break;
            }
        }
        return yaExiste;
    }
	function esTipoDocValido(rutaDocumento){
		var docValido = false;
		
		// implementar funcion 
		docValido=true;
		return docValido;
	}
        
        
        // Elimina un documento asociado al expediente.
function pulsarEliminarOtroDocumento(){ 

    if(tabOtrosDocumentos.selectedIndex != -1) {
        var extension        = listaOtrosDocumentos[tabOtrosDocumentos.selectedIndex][4];
        var nombre          = listaOtrosDocumentos[tabOtrosDocumentos.selectedIndex][1];
        nombre= escape(nombre);
        
        if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjBorrarFichero")%>') ==1) {

            eliminarOtroDocumentoExpediente(tabOtrosDocumentos.selectedIndex,listaOtrosDocumentos[tabOtrosDocumentos.selectedIndex][0],nombre,extension);
        }

    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

// Genera e incrusta el CSV en el documento externo
function pulsarCSVOtroDocumento(){
    if(tabOtrosDocumentos.selectedIndex != -1) {
        if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjCrearCSV")%>') ==1) {
            var codDocInterno  = listaOtrosDocumentos[tabOtrosDocumentos.selectedIndex][0];
            var nombre         = escape(listaOtrosDocumentos[tabOtrosDocumentos.selectedIndex][1]);
            var tipoMime       = listaOtrosDocumentos[tabOtrosDocumentos.selectedIndex][2];
            var extension      = listaOtrosDocumentos[tabOtrosDocumentos.selectedIndex][4];
            
            generarCSVOtroDocumentoExpediente(codDocInterno,nombre , extension, tipoMime);
        }
    } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
}

function habilitarBotonesOtrosDocumentos(){
     if (document.forms[0].expHistorico.value != "true"){
        document.forms[0].cmdAltaOtroDocumento.disabled = false; document.getElementById('cmdAltaOtroDocumento').style.color = '#ffffff';
        document.forms[0].cmdEliminarOtroDocumento.disabled = false; document.getElementById('cmdEliminarOtroDocumento').style.color = '#ffffff';
        <%if ("si".equals(m_Config.getString("JSP.BotonCSV"))){%>
            document.forms[0].cmdCSVOtroDocumento.disabled = false; document.getElementById('cmdCSVOtroDocumento').style.color = '#ffffff';
        <%}%>
    }document.forms[0].cmdVerOtroDocumento.disabled = false; document.getElementById('cmdVerOtroDocumento').style.color = '#ffffff';
}


function cargarComunicacion(i) { 

  pleaseWait1('on',this);
  var source = "<html:rewrite page='/sge/FichaExpediente.do'/>?opcion=consultaComunicacion&idCom="+ listaComunicaciones[i][0];
  var opt ='unadorned:yes;resizable:1;dialogHeight:550px;dialogwidth:985px;scroll:no;status=no';
  abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + source ,"comunicacion"+listaComunicaciones[i][0],
	'width=985,height=550,status=no;scrollbars=no',function(){
                        listaComunicaciones[i][3] = '<%=descriptor.getDescripcion("etiq_Si")%>';
                        if (document.getElementById("tb_tabla4")){ document.getElementById("tb_tabla4").rows[i+1].cells[2].innerText=  '<%=descriptor.getDescripcion("etiq_Si")%>';}
                        pleaseWait1('off',this);
                    });
} 

  function cargarTramitacion(i) {     
       // Si estamos en expedientes relacionados, se deja ver la tramitacion en modo consulta. Es como si se viniese desde consulta
      if(document.forms[0].desdeExpRel.value=='si')
         document.forms[0].desdeConsulta.value = "si";

      if((document.forms[0].modoConsulta.value == "si")&& (document.forms[0].desdeConsulta.value != "si")){
	  jsp_alerta('A',"<%=descriptor.getDescripcion("msjNoCons")%>");
          return;
      }
              if(listaTramitesOriginal[i][4] == null || listaTramitesOriginal[i][4] == "") {  
                i=permisoSobreTramite(i); 
              }
              if(i != -1) {
                  var fichaTramitacionExterna = verificarPantallaExterna(i);                                    
                  if(fichaTramitacionExterna[0]!=null && fichaTramitacionExterna[0]!="" && fichaTramitacionExterna[0]!="null"
                    && fichaTramitacionExterna[1]!=null && fichaTramitacionExterna[1]!="" && fichaTramitacionExterna[1]!="null"){
                      //if ((getNumInteresados()== 0) && (interesado_Obligatorio() == "1")){
                      if ((getNumInteresadosExpediente()== 0) && (interesado_Obligatorio() == "1")){                      
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjInterOblig")%>');
                      }else{
                        pleaseWait1('on',this);                        
                        ventanaTramitacionExterna = window.open(fichaTramitacionExterna[0],'ventanaTramitacionExterna',fichaTramitacionExterna[1]);
                        pleaseWait1('off',this);
                        verificarCierreFichaTramitacionExterna();
                      }                        
                  }else{
                      document.forms[0].ejercicio.value = expediente[2];
                      pleaseWait1('on',this);
                      activarFormulario();
                      document.forms[0].ocurrenciaTramite.value = listaTramitesOriginal[i][0];
                      document.forms[0].codTramite.value = listaTramitesOriginal[i][1];
                      document.forms[0].codUnidadTramitadoraTram.value = listaTramitesOriginal[i][10];
                      document.forms[0].opcion.value="inicioAccesoExterno";
                      document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                      document.forms[0].submit();
                  }// else
              }
               
      }
      
      
 function permisoSobreTramite(i)
 {
          
          
    var ajax = getXMLHttpRequest();
    var codProcedimiento = document.forms[0].codProcedimiento.value;
    var codMunicipio     = document.forms[0].codMunicipio.value;
    var numero           = document.forms[0].numero.value;
    var ejercicio           = document.forms[0].ejercicio.value;   
    var codOrganizacion       = document.forms[0].codMunicipio.value;   
    var expHistorico       = document.forms[0].expHistorico.value;
    var codTramite= listaTramitesOriginal[i][1];
    var ocurrenciaTramite= listaTramitesOriginal[i][0];
    var codUnidadTramitadoraTram=listaTramitesOriginal[i][10];
    var exito=false;
    
    
    var result ="";    
        if(ajax!=null){                                                         
            var url = getContextPath() + "/sge/FichaExpediente.do";       
            var parametros = "&opcion=verificar_permisoTramite&codMunicipio=" + escape(codMunicipio) + "&numExpediente=" + escape(numero) + "&codTramite=" + escape(codTramite) + "&ocurrenciaTramite=" + escape(ocurrenciaTramite)
            + "&codProcedimiento=" + escape(codProcedimiento) + "&expHistorico=" + escape(expHistorico)  + "&ejercicio=" + escape(ejercicio) + "&codUnidadTramitadoraTram=" + escape(codUnidadTramitadoraTram);
                                                   
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);
            try{            
                if (ajax.readyState==4 && ajax.status==200){         
                    // En IE el XML viene en responseText y no en la propiedad responseXML
                   var text = ajax.responseText;                     
                   if (text.trim() != ""){                                        
                       if(text.trim()=="SI")
                          exito = true; 
                    }
                }
            }catch(Err){
                alert("Error.descripcion: " + Err.description);            
            }
        }
        
        if (!exito) 
        {
               i = -1;
                 jsp_alerta('A',"<%=descriptor.getDescripcion("msjNoCons")%>");  
        }
        
        return i;
        
        
     }
    
      
function verificarCierreFichaTramitacionExterna(){
  if(ventanaTramitacionExterna!=null && !ventanaTramitacionExterna.closed){
      timerID = setTimeout("verificarCierreFichaTramitacionExterna()",5);
  }else
  if(ventanaTramitacionExterna!=null && ventanaTramitacionExterna.closed){
      // Se ha cerrado la ficha de tramitacion externa => Se recarga la ficha de expediente
    document.forms[0].opcion.value="cargar";
        document.forms[0].target="mainFrame";
    var desdeConsulta = document.forms[0].modoConsulta.value;
        document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>" + "?modoConsulta=" + desdeConsulta + "&desdeAltaRE=" + desdeAltaRE + "&desdeExpRel=no";
        document.forms[0].submit();
  }
}


     function tieneTramiteCamposObligatoriosSinCubrir(){
     

        var ajax = getXMLHttpRequest();
        
        var exito = false;
        var codMunicipio     = document.forms[0].codMunicipio.value;
        
        var codTramite        = document.forms[0].codTramite.value;
        var ocurrenciaTramite = document.forms[0].ocurrenciaTramite.value;
        var numExpediente = document.forms[0].numero.value;
                
        var result ="";    
        if(ajax!=null){                                                         
            var url = getContextPath() + "/VerificarCamposObligatoriosTramiteSinValor.do";       
            var parametros = "codMunicipio=" + escape(codMunicipio) + "&numExpediente=" + escape(numExpediente) + "&codTramite=" + escape(codTramite) + "&ocurrenciaTramite=" + escape(ocurrenciaTramite);
                                                   
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);
            try{            
                if (ajax.readyState==4 && ajax.status==200){         
                    // En IE el XML viene en responseText y no en la propiedad responseXML
                   var text = ajax.responseText;                           
                   if (text.trim() != ""){                                        
                       if(text.trim()=="0")
                          exito = true; 
                    }
                }
            }catch(Err){
                alert("Error.descripcion: " + Err.description);            
            }
        }
        
        return exito;
     }
    
    /* Pestaa tramitacion */
function cargarTramitacionOculta(i) {
    if(listaTramitesOriginal[i][4] == null || listaTramitesOriginal[i][4] == "") {  
        i=permisoSobreTramite(i); 
    }              
    if(i != -1) {
        document.forms[0].ejercicio.value = expediente[2];                    
        activarFormulario();
        bloquearBotonesTemporal('cmdFinalizar');

        document.forms[0].ocurrenciaTramite.value = listaTramitesOriginal[i][0];
        document.forms[0].codTramite.value = listaTramitesOriginal[i][1];
        document.forms[0].codUnidadTramitadoraTram.value = listaTramitesOriginal[i][10];

        // SOLO COMPROBAMOS SI ENVIAR NOTIFICACIONES SI EL TRÁMITE NO ESTÁ CERRADO
        var fechaCierre = listaTramitesOriginal[i][4];
        var avanzo = false;

        if(listaTramitesOriginal[i][17]=='1')
        {              


            if (listaTramitesOriginal[i][19]=='1' && (fechaCierre==null || fechaCierre=='')){ 
                // Notificación obligatoria

                if(tieneTramiteCamposObligatoriosSinCubrir()){                                

                    jsp_alerta("A",'<%=descriptor.getDescripcion("msgTramCamposObligVacio2")%>');                                


                } else if (existenInteresadosNotifElectronica == 0){
                    avanzo = true;
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msgNoExistenIntNotifElect")%>');	
                } else {
                    // Si el trámite no tiene campos obligatorios sin valor
                    document.forms[0].opcion.value="chequeoDocumentosTramitacionFirmados";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                    document.forms[0].submit();                                                                  
                }


            } else {

                // El trámite admite el envío de notificaciones electrónicas, pero no es
                // obligatorio su envío.

                if (existenInteresadosNotifElectronica==1 && (fechaCierre==null || fechaCierre=='') && jsp_alerta("C",'<%=descriptor.getDescripcion("msgEnviarNotifNoObligatoria")%>')){
                    var enviar = false;
                    if(tieneTramiteCamposObligatoriosSinCubrir()){                                                                        
                        jsp_alerta('A','<%=descriptor.getDescripcion("msgTramCamposObligVacio2")%>');
                        enviar = false;                                        
                    }else
                        enviar = true;

                    if(enviar){
                        document.forms[0].opcion.value="chequeoDocumentosTramitacionFirmados";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                        document.forms[0].submit();
                    }

                }else{ 
                    inicioOculto(document.forms[0].ocurrenciaTramite.value,document.forms[0].codTramite.value,document.forms[0].codUnidadTramitadoraTram.value);
                }

            }


        }
        else {
            avanzo = true;
        }
        if(avanzo)
            inicioOculto(document.forms[0].ocurrenciaTramite.value,document.forms[0].codTramite.value,document.forms[0].codUnidadTramitadoraTram.value);


    }
}
function activarBotonFinalizarTramite(){          
         var instruccion = "eval(document.getElementById('cmdFinalizar').disabled=false);";
         var t=setTimeout(instruccion,1500);
    }

function inicioOculto(ocuTram,codTram,codUnidTram,postNotificacion){
     
        if(postNotificacion) {
            postNotificacionEnviada = true;
        }
        document.getElementById("cmdFinalizar").disabled= true;
        document.forms[0].ocurrenciaTramite.value = ocuTram;
        document.forms[0].codTramite.value = codTram;
        document.forms[0].codUnidadTramitadoraTram.value = codUnidTram;
        document.forms[0].opcion.value="inicioOculto";
        document.forms[0].target="oculto";
        document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
        document.forms[0].submit();
}

function finalizaTramitePostNotificacion()
{   
    inicioOculto(document.forms[0].ocurrenciaTramite.value,document.forms[0].codTramite.value,document.forms[0].codUnidadTramitadoraTram.value);
}		
function mostrarVentanaNotificacionDatos(urlPantallaDatosNotificacion)
{
    var codMunicipio = document.forms[0].codMunicipio.value;
    var codProcedimiento = document.forms[0].codProcedimiento.value;
    var procedimiento = document.forms[0].procedimiento.value;
    var ejercicio = document.forms[0].ejercicio.value;
    var numero = document.forms[0].numero.value;
    var codTramite = document.forms[0].codTramite.value;
    var ocurrenciaTramite = document.forms[0].ocurrenciaTramite.value;

    source = "<html:rewrite page='/Notificacion.do'/>?opcion=mostrarPantallaNotificacion"+"&codMunicipio=" + 
            codMunicipio+"&codProcedimiento=" + codProcedimiento+"&ejercicio=" + ejercicio+"&numero=" + numero+"&codTramite=" + codTramite+"&ocurrenciaTramite=" + ocurrenciaTramite+"&procedimiento=" + procedimiento + "&recargar=no";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana1',
	'width=1100,height=950,status='+ '<%=statusBar%>',function(ventana){
                        if(ventana!=undefined){
                          inicioOculto(document.forms[0].ocurrenciaTramite.value,document.forms[0].codTramite.value,document.forms[0].codUnidadTramitadoraTram.value,"postNotificacion");
                          //actualizaDocs(ventana);
                        }
                    });
}

function pulsarTramitacionManual() {

    var irAlAction=0;
    if((document.forms[0].existenCambiosSinGrabar)!='undefined')irAlAction=irActionConCambios(document.forms[0].existenCambiosSinGrabar.value,mensajeGuardarCambios);
    if(irAlAction==1)
    {
        if((document.forms[0].existenCambiosSinGrabar)!='undefined')document.forms[0].existenCambiosSinGrabar.value=0;
        var datos = new Array();
        datos = listaTramitesOriginal;
        var opcion = "iniciar_tramitacion_manual";
        var source = "<c:url value='/sge/FichaExpediente.do?opcion='/>"+opcion
        +"&codMun="  + document.forms[0].codMunicipio.value
        +"&codProc=" + document.forms[0].codProcedimiento.value
        +"&eje=" + document.forms[0].ejercicio.value
        +"&num=" + document.forms[0].numero.value;

        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,datos,
            'width=990,height=550,status='+ '<%=statusBar%>' + '', 
            function(datosRespuesta){
               // #208372 --> iniciar tramite manual directamente, sin  pasar x el listado
               if(datosRespuesta){
                 cargarTramiteDirecto('tramiteManualSinRecargar');
               }

           });
    }
}


function cargarTramiteDirecto(opcion){
    pleaseWait1('on',this);
    try{
        $.ajax({
            url: '<c:url value='/sge/FichaExpediente.do'/>',
            type: 'POST',
            async: true,
             data: 'numExpediente=' + $('#numExpediente').val() + '&opcion='+opcion,

            success: procesarRespuestaCargarTramiteDirecto,
            error: muestraErrorRespuestaCargarTramiteDirecto
        });           
   }catch(Err){
        pleaseWaitSinFrame('off');
        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorIniciarTrDirecto")%>');
   }
}

function procesarRespuestaCargarTramiteDirecto(ajaxResult){
    pleaseWaitSinFrame('off');
    if(ajaxResult){
        var datos = JSON.parse(ajaxResult);
        var tramites = datos[0];
        var permisos = datos[1];

        listaTramites = new Array();
        listaTramitesOriginal = new Array();
        listaTramitesPlugin = new Array();
        for(var j=0; j<tramites.length; j++){
            var tramite = tramites[j].tabla;
            // lista de tramites para la tabla
            listaTramites[j] = [tramite.tramite,tramite.fehcaInicio,tramite.fechaFin,tramite.unidad,tramite.clasificacion,""];
            if (tramite.usuarioBloq!="") {
                if (tramite.codUsuarioBloq==tramite.usuario) {
                    listaTramites[j][5]='<span class="fa fa-lock" alt="'+tramite.usuarioBloq+'"></span>';
                } else {
                    listaTramites[j][5]='<span class="fa fa-unlock" alt="'+tramite.usuarioBloq+'"></span>';
                }
            }
            // lista de tramistes con todos los datos
            listaTramitesOriginal[j] = [
                tramite.ocurrenciaTramite,tramite.codTramite,tramite.tramite,tramite.fehcaInicio,tramite.fechaFin,tramite.unidad,
                tramite.usuario,tramite.clasificacion,tramite.consultar,tramite.fueraDePlazo,tramite.codUniTramTramite,
                tramite.tramiteInicio,tramite.codUsuario,tramite.codUsuarioFinalizacion,tramite.usuarioBloq,tramite.codUsuarioBloq,
                tramite.plazoCercaFin,tramite.traNotificacionElectronica,tramite.tieneTareasPendientesInicio,
                tramite.notificacionObligatoria,tramite.certificadoOrganismo
            ];
            // lista de tramites con datos de plugin
            listaTramitesPlugin[j] = [
                tramite.tramite,tramite.codTramite,tramite.ocurrenciaTramite,tramite.extPlugin,tramite.extUrl,tramite.extImplClass,
                tramite.extIdEnlace,tramite.extBloqueoFinalizarTramite,tramite.extBloqueoRetrocesoTramite
            ];
        }             
        tabTramites.lineas = listaTramites;
        tabTramites.displayTabla();

        listaPermisosTramites = new Array();
        for (var i = 0; i < permisos.length; i++) {
            listaPermisosTramites[i] = permisos[i];
        }

        // cargamos tramite
        cargarTramitacion(0);
    } else jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorIniciarTrDirecto")%>');
}

function muestraErrorRespuestaCargarTramiteDirecto(){
    pleaseWaitSinFrame('off');
    jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorIniciarTrDirecto")%>');
}


function pulsarRetrocederExpediente() {   
    document.forms[0].cmdRetrocederExpediente.disabled = true ;
    if ((getNumInteresadosExpediente()== 0) && (interesado_Obligatorio() == "1")){
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjInterOblig")%>');
    }else{
        if(preguntarRetroceder()) {
              if((document.forms[0].existenCambiosSinGrabarCampos)!='undefined')irAlAction=irActionConCambios(document.forms[0].existenCambiosSinGrabarCampos.value,mensajeGuardarCambios);
                  if(irAlAction==1)
                  {
                      var ajax = getXMLHttpRequest();
                      /****/
                      if(ajax!=null){
                        pleaseWait1('on',this);
                        var url = "<%= request.getContextPath() %>" + "/sge/FichaExpediente.do";
                        var parametros = "&opcion=verificar_retroceso_expediente_permitido&codProcedimiento=" + document.forms[0].codProcedimiento.value;
                        ajax.open("POST",url,false);
                        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
                        ajax.setRequestHeader("Accept", "text/html, application/xml, text/plain");
                        ajax.send(parametros);
                        try{
                            if (ajax.readyState==4 && ajax.status==200){
                                var xmlDoc = null;											
                                var text = ajax.responseText;											
                                var datosRespuesta = text.split("=");

                                if(datosRespuesta!=null && datosRespuesta.length==2 && datosRespuesta[0].trim()=="PERMITIR" && (datosRespuesta[1].trim()=="SI" || datosRespuesta[1].trim()=="si")){
                                    var COD_TRAM_RETRO= document.forms[0].codTramiteRetroceder.value;
                                    var OCU_TRAM_RETRO= document.forms[0].ocurrenciaTramiteRetroceder.value;
                                    var i = tabTramites.selectedIndex;
                                    if(listaTramitesPlugin[i]!=null && listaTramitesPlugin[i][1]==COD_TRAM_RETRO && listaTramitesPlugin[i][3]!='' 
                                        && listaTramitesPlugin[i][8]=='SI'){

                                        jsp_alerta('A','<%=descriptor.getDescripcion("msgRetrocesoBloqueadoTramExt")%>');
                                    }else{
                                        retrocederExpedienteComprobarFirma();
                                     }
                                }else
                                    jsp_alerta("A",'<%=descriptor.getDescripcion("msgRetrocesoNoPermitido")%>');

                            }
                        }catch(Err){
                        }
                    }// if                  
             }
        }
        else document.forms[0].cmdRetrocederExpediente.disabled = false; 
    }
}

function preguntarRetroceder() {
    var indiceTramiteRetroceder = tabTramites.selectedIndex;
    if(listaTramites.length == 0) {
        jsp_alerta('A',"<%=descriptor.getDescripcion("msjNoHayTram")%>");
        return false;
    } else if(listaTramites.length == 1) {
        //Comprobar primero si el trmite esta bloqueado y luego el usuario que lo bloquea
        if (listaTramitesOriginal[0][14] == "" || ((listaTramitesOriginal[0][14] != "") &&
                (codigoUsuario == listaTramitesOriginal[0][15]))) {
            if(listaTramitesOriginal[0][11] == "si" && listaTramites[0][2]=="") { //Si es tramite de Inicio y no tiene Fecha Fin
                jsp_alerta('A',"<%=descriptor.getDescripcion("msjTramInic")%>");
                return false;
            }else{
                var permisoUnidad = unidadesUsuario.indexOf(listaTramitesOriginal[0][10]+'-');
                if((listaTramites[0][2] !="" && codigoUsuario == listaTramitesOriginal[0][13])
                        || (listaTramites[0][2] =="" && codigoUsuario == listaTramitesOriginal[0][12])
                        || permisoUnidad>-1) {
                    document.forms[0].codTramiteRetroceder.value = listaTramitesOriginal[0][1];
                    document.forms[0].tramiteRetroceder.value = listaTramitesOriginal[0][2];
                    document.forms[0].ocurrenciaTramiteRetroceder.value = listaTramitesOriginal[0][0];
                    document.forms[0].fechaInicioTramiteRetroceder.value = listaTramitesOriginal[0][3];
                    return jsp_alerta('C',"<%=descriptor.getDescripcion("msjRetrocTram")%>" + " " + listaTramites[0][0] + "?");

                } else {
                    jsp_alerta('A',"<%=descriptor.getDescripcion("msjNoUsuario")%>");
                    return false;
                }
            }
        } else {
            jsp_alerta('A',"<%=descriptor.getDescripcion("msjBlqOtroUsu")%>");
        }
    } else {
        //Comprobar primero si el trmite esta bloqueado y luego el usuario que lo bloquea
        if (listaTramitesOriginal[0][14] == "" || ((listaTramitesOriginal[0][14] != "") &&
                (codigoUsuario == listaTramitesOriginal[0][15]))) {
            var lT = new Array();
            for(z=0;z<listaTramites.length;z++) {
                var permisoUnidad = unidadesUsuario.indexOf(listaTramitesOriginal[z][10]+'-');
                if(codigoUsuario == listaTramitesOriginal[z][12] || admin=="1" || permisoUnidad>-1) {

                    var ocurrencia = listaTramitesOriginal[z][0];
                    if(listaTramitesOriginal[z][11] != "si" && z==indiceTramiteRetroceder) {//Si no es tramite de inicio
                        lT[0] = listaTramites[z][0];
                        document.forms[0].codTramiteRetroceder.value = listaTramitesOriginal[z][1];
                        document.forms[0].tramiteRetroceder.value = listaTramitesOriginal[z][2];
                        document.forms[0].ocurrenciaTramiteRetroceder.value = listaTramitesOriginal[z][0];
                        document.forms[0].fechaInicioTramiteRetroceder.value = listaTramitesOriginal[z][3];
                        if((z+1)<listaTramites.length && listaTramites[z][2] =="" ) {
                            z++;
                            lT = preguntarRetroceder2(lT,z);
                        }
                        break;
                    } else if(listaTramitesOriginal[z][11] == "si" && z!=indiceTramiteRetroceder) {//Si es tramite de inicio pero no el seleccionado
                        continue;
                    } else if(listaTramitesOriginal[z][11] == "si" && ocurrencia>1) {//Si es tramite de inicio pero hay ms
                        lT[0] = listaTramites[z][0];
                        document.forms[0].codTramiteRetroceder.value = listaTramitesOriginal[z][1];
                        document.forms[0].tramiteRetroceder.value = listaTramitesOriginal[z][2];
                        document.forms[0].ocurrenciaTramiteRetroceder.value = listaTramitesOriginal[z][0];
                        document.forms[0].fechaInicioTramiteRetroceder.value = listaTramitesOriginal[z][3];
                        if((z+1)<listaTramites.length && listaTramites[z][2] == "" ) {
                            z++;
                            lT = preguntarRetroceder2(lT,z);
                        }

                        break;
                    }
                } else {
                    if(codigoUsuario == listaTramitesOriginal[z][13]) {//Si el usuario es el que finaliza

                        if(listaTramites[z][2] != "") {//Si esta finalizado
                            lT[0] = listaTramites[z][0];
                            document.forms[0].codTramiteRetroceder.value = listaTramitesOriginal[z][1];
                            document.forms[0].tramiteRetroceder.value = listaTramitesOriginal[z][2];
                            document.forms[0].ocurrenciaTramiteRetroceder.value = listaTramitesOriginal[z][0];
                            document.forms[0].fechaInicioTramiteRetroceder.value = listaTramitesOriginal[z][3];
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
            if(lT.length >0) {

                var mensaje = "";
                if(lT.length == 1) {
                    mensaje = "<%=descriptor.getDescripcion("msjRetrocTram")%>" + " ";
                } else {
                    mensaje = "<%=descriptor.getDescripcion("msjRetrocTrams")%>" + ": ";
                }
                mensaje += lT[0];
                mensaje += " ?";
                return jsp_alerta('C',mensaje);
            } else {
                if(indiceTramiteRetroceder==-1){
                    jsp_alerta('A',"<%=descriptor.getDescripcion("retrocExpSinTramSelecc")%>");
                }else {
                    jsp_alerta('A',"<%=descriptor.getDescripcion("msjNoUsuario")%>");
                }
                return false;
            }
        } else {
            jsp_alerta('A',"<%=descriptor.getDescripcion("msjBlqOtroUsu")%>");
        }
    }
}

function preguntarRetroceder2(lista,indice) {
    for(z=indice;z<listaTramites.length;z++) {
        if(listaTramites[z][2] =="" && codigoUsuario == listaTramitesOriginal[z][12]) {
            break;
        } else {
            var permisoUnidad = unidadesUsuario.indexOf(listaTramitesOriginal[z][10]+'-');
       if(admin == "1" || (listaTramites[z][2] !="" && codigoUsuario == listaTramitesOriginal[z][13])
        || permisoUnidad>-1) {//Si esta finalizado y el usuario es el que finaliza

        //Si esta finalizado y el usuario es el que finaliza

            lista[1] = listaTramites[z][0];
            document.forms[0].codTramiteRetroceder2.value = listaTramitesOriginal[z][1];
            document.forms[0].tramiteRetroceder2.value = listaTramitesOriginal[z][2];
            document.forms[0].ocurrenciaTramiteRetroceder2.value = listaTramitesOriginal[z][0];
            document.forms[0].fechaInicioTramiteRetroceder2.value = listaTramitesOriginal[z][3];
            break;
        }
    }
}
return lista;
}

function actualizarTramites(listaTramitesNueva){
    listaTramitesOriginal= new Array();
    listaTramites=new Array();
    listaTramitesOriginal = listaTramitesNueva;

    var i=0;
    for (i=0; i<listaTramitesOriginal.length; i++) {
        listaTramites[i] = [listaTramitesOriginal[i][2], listaTramitesOriginal[i][3], listaTramitesOriginal[i][4], listaTramitesOriginal[i][5], listaTramitesOriginal[i][7], listaTramitesOriginal[i][14]];

        if (!listaTramites[i][5]=="") {
            if ("<%=codUsu%>" == listaTramitesOriginal[i][15]) {
                listaTramites[i][5]='<span class="fa fa-lock" alt="' + listaTramites[i][5]+'"></span>';
            } else {
            listaTramites[i][5]='<span class="fa fa-lock" alt="' + listaTramites[i][5]+'"></span>';
        }
    }
}
tabTramites.lineas = listaTramites;
tabTramites.displayTabla();
}

function cargarFormularios(){
 <%
            ArrayList formularios = (ArrayList) expForm.getFormularios();
            if (formularios != null) {
                for (int i = 0; i < formularios.size(); i++) {
                    GeneralValueObject vo = (GeneralValueObject) formularios.get(i);
%>
	var imagen;
        var existenAnexosForms=0;
        if(('<%=vo.getAtributo("tieneAnexo")%>')=='si')
        {
            imagen='<span class="fa fa-paperclip"></span>'
            existenAnexosForms=1;
         }else imagen='';


     if (document.getElementById("pestana7") != null) {

     marcado2=true;
     var selected4 = document.getElementById("pestana7").className;
        var changeSelected4 = false;
        if ((selected4 == "tab page") || (selected4 == "tab anexos")|| (selected4 == "tab-remark selected")) {
            changeSelected4 = true;
        }


        if (marcado2) {

            if(existenAnexosForms==1){

			document.getElementById("pestana7").className="tab anexos";
			}
            else if (changeSelected4) {

			document.getElementById("pestana7").className="tab-remark selected";
			}
            else document.getElementById("pestana7").className="tab-remark";
        } else{
        if (changeSelected4) document.getElementById("pestana7").className="tab selected";
        else document.getElementById("pestana7").className="tab";

        }
     }

    listaFormularios[<%= i %>] = ['<%=vo.getAtributo("codigo")%>',
        '<%=vo.getAtributo("descripcion")%>',
        '<%=vo.getAtributo("fecMod")%>',
        '<%=vo.getAtributo("usuario")%>',
        imagen];

        listaFormulariosOriginal[<%= i %>] = ['<%=vo.getAtributo("tipo")%>',
            '<%=vo.getAtributo("codigo")%>',
            '<%=vo.getAtributo("descripcion")%>',
            '<%=vo.getAtributo("fecMod")%>',
            '<%=vo.getAtributo("usuario")%>',
            '<%=vo.getAtributo("estado")%>'];

  <%    }
                out.print("/* EN expForm.formularios habia " + formularios.size() + "*/");
            } else {
                out.print("/* EN expForm.formularios formularios nulo");
            }
  %>
             
      tabFormularios.lineas=listaFormularios;
      tabFormularios.displayTabla();
  }

  function pulsarVerAnexoPDF(){
      var i = tabFormularios.selectedIndex;
      if(i == -1) {
          jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
          }else{
          var source = "<%=request.getContextPath()%>/sge/ListaAnexos.do?formPDF=" + listaFormulariosOriginal[i][1] +
          "&estado=" + listaFormulariosOriginal[i][5];
          abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana',
	'width=500,height=500,status='+ '<%=statusBar%>',function(mensaje){
                        if (mensaje!=null){
                            jsp_alerta("A",mensaje);
                        }
                  });
          }
      }

      /* Fin pestaa formularios*/

      function pulsarVolver11() {
        cerrarVentanaSiDesdeAltaRE();

          if ((document.forms[0].expRelacionado.value == "si") && (document.forms[0].desdeExpRel.value=="si")) {
              document.forms[0].modoConsulta.value = "no";
              document.forms[0].target="mainFrame";
              document.forms[0].action='<c:url value='/sge/ConsultaExpedientes.do?opcion=expedientesRelacionados'/>&nCS=' +
                    document.forms[0].codMunExpIni.value + '&codMun=' + document.forms[0].ejercicioExpIni.value +
                    '&codProc=' + document.forms[0].numeroExpIni.value + '&codProc=no';
              document.forms[0].submit();
          } else {
          if (document.forms[0].desdeConsulta.value == "null" || document.forms[0].desdeConsulta.value == "no"){
              //Abrimos el expediente desde ConsultaExpediente o desde el botn de expedientes relacionados de la ficha del exp.
              document.forms[0].opcion.value="consultarListado";
              document.forms[0].target="mainFrame";
              document.forms[0].action="<c:url value='/sge/ConsultaExpedientes.do'/>";
              document.forms[0].submit();
          }else{//Abrimos el expediente desde tramitacionExpedientesPendientes, entonces volvemos a tramitacionExpedientesPendientes
          pulsarVolverTramitacion();
      }
  }
}

function pulsarExpedientesRelacionados() {
    var argumentos = new Array();
    var nCS = document.forms[0].codMunicipio.value;
    var codMun = document.forms[0].ejercicio.value;
    var codProc = document.forms[0].numExpediente.value;
    var codTram = document.forms[0].modoConsulta.value;
    var desdeConsulta = document.forms[0].modoConsulta.value;
    var source = '<c:url value='/sge/ConsultaExpedientes.do?opcion=expedientesRelacionados'/>';
    var irAlAction=0;
    
    if ((getNumInteresadosExpediente()== 0) && (interesado_Obligatorio() == "1")&&(document.forms[0].modoConsulta.value!='si')){
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjInterOblig")%>');
    }else{
        if  (document.forms[0].modoConsulta.value!='si'){
            if((document.forms[0].existenCambiosSinGrabar)!='undefined') irAlAction=irActionConCambios(document.forms[0].existenCambiosSinGrabar.value,mensajeGuardarCambios);
        }else 
            var irAlAction=1;

        if(irAlAction==1)        
        {
            if  (document.forms[0].modoConsulta.value!='si'){
                if((document.forms[0].existenCambiosSinGrabar)!='undefined') document.forms[0].existenCambiosSinGrabar.value=0;
            }
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source+"&nCS="+nCS+"&codMun="+
                    codMun+"&codProc="+codProc+"&codTram="+codTram+"&desdeConsulta="+desdeConsulta + "&desdeAltaRE=" + desdeAltaRE,
                    argumentos,'width=990,height=650,status='+ '<%=statusBar%>' + '',
                    function (datos) {
                        recargarPagina();
                    });
        }
    }
}

   
function abrirDomicilios() {
    var irAlAction=0;

    if((document.forms[0].existenCambiosSinGrabar)!='undefined')irAlAction=irActionConCambios(document.forms[0].existenCambiosSinGrabar.value,mensajeGuardarCambios);
    if(irAlAction==1) {
        if((document.forms[0].existenCambiosSinGrabar)!='undefined') document.forms[0].existenCambiosSinGrabar.value=0;
        var argumentos = new Array();
        var source = "<c:url value='/sge/TramitacionExpedientes.do?opcion=abrirDomicilios'/>"
        +"&codMun="  + document.forms[0].codMunicipio.value
        +"&codProc=" + document.forms[0].codLocalizacion.value
        +"&eje=" + document.forms[0].ejercicio.value
        +"&num=" + document.forms[0].numExpediente.value;

        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,
                argumentos,'width=930,height=430,status='+ '<%=statusBar%>',function (datos) {
            if(datos!=undefined){
                document.forms[0].localizacion.value = datos[0];
                document.forms[0].codLocalizacion.value = datos[1];
                document.forms[0].refCatastral.value = datos[2];
            }

            // Se refresca la pagina
            document.forms[0].opcion.value="cargar";
            document.forms[0].target="mainFrame";
            document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>" + "?modoConsulta=" + document.forms[0].modoConsulta.value + "&desdeAltaRE=" + desdeAltaRE + "&desdeExpRel=no";
            document.forms[0].submit();
        });
    }
}// abrirDomicilios
        
function grabacionExpediente(respOpcion,plistaDocumentosOriginal,plistaDocumentos,plistaDocumentosOriginalAuxiliar) {
     window.focus();
     if(respOpcion == "grabado") {                   
         camposSuplementariosVistaExpediente();                
         //jsp_alerta('A',"<%=descriptor.getDescripcion("msjDatExpGrab")%>");
     } else {
         jsp_alerta('A',"<%=descriptor.getDescripcion("msjDatExpNoGrab")%>");
     }
 }

function comprobarFecha(inputFecha) {
    if (Trim(inputFecha.value)!='') {
        if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
            jsp_alerta("A","<%=descriptor.getDescripcion("fechaNoVal")%>");
                return false;
        }
    }
    return true;
}

function cubrirListaMostrar() {
    var codRol = document.forms[0].codRol.value;
    if(document.forms[0].listaMostrar.value==null || document.forms[0].listaMostrar.value.length==0){
        document.forms[0].listaMostrar.value = codRol + "§¥";
    }
    else{
        document.forms[0].listaMostrar.value = document.forms[0].listaMostrar.value + "§¥";
    }
}

// Comprueba que no se exceda el tamaño maximo dl asunto y observaciones
function verificarTamanoTextArea(){
    var asunto = document.forms[0].asunto.value;
    var observaciones = document.forms[0].observaciones.value;

    var mensaje = "";
    if(asunto!=null && asunto.length>2000)
       mensaje += '<%=descriptor.getDescripcion("etiq_MaxCAsunto")%> 2000 <br/>';
    if(observaciones!=null && observaciones.length>1000)
       mensaje += '<%=descriptor.getDescripcion("etiq_MaxCObserv")%> 1000 <br/>';

    return mensaje;
}
		
// #253692: Comprueba que  el texto introducido como ubicacion es válido
function prepararValidarUbicacionDocumentacion(){
    var valido = false;
    var primGrupo = null;
    var segGrupo = null;
    var ubicacion = $('#ubicacionDoc').val();
    <% 
        if(mostrarUbicacion) {
            ResourceBundle properties = ResourceBundle.getBundle("Lanbide");  
            String primGrupo = properties.getString(Integer.toString(codOrganizacion) + "/UBICACION_DOCUMENTACION_PRIMERGRUPOLETRAS"); 
            String segGrupo = properties.getString(Integer.toString(codOrganizacion) + "/UBICACION_DOCUMENTACION_SEGUNDOGRUPOLETRAS");
    %>
            primGrupo = '<%=primGrupo%>';
            segGrupo = '<%=segGrupo%>';
            valido = validarUbicacionDocumentacion(ubicacion,primGrupo,segGrupo);
    <% } else { %>
            valido = true;
    <% } %>
    
    return valido;
}

function pulsarGrabarGeneral() { 
    cubrirListaMostrar();
    agregaListaNotif();
    var resultado = false;
    var validarObs = verificarTamanoTextArea();
    var comprobar  = ChequearCamposCalculados();
    
    if (comprobar!=null && "" != comprobar.trim() && comprobar.trim()!="0" && comprobar.trim()!="1") 
    {        
        var campos_fin = comprobar.split("#");
        msnj = "<%=descriptor.getDescripcion("msjExpresionErr")%>" + ":" ;
        for (i = 0; i < campos_fin.length; i++) 
        {            
            msnj = msnj + "<br>" + "     " + campos_fin[i];                 
        }      
        jsp_alerta("A",msnj);      
        resultado = false
    }
    else if (campos_vacios == true) 
    {        
        msnj = "<%=descriptor.getDescripcion("msjValidarExpresion")%>";    
        if(jsp_alerta("C",msnj) ==1)             
        {
            resultado = true;
        }
    }
    else  
        resultado = true;
        
    if (resultado ==true) 
    {
        if(validarObs!=null && validarObs.length>0)
            jsp_alerta('A',validarObs);
        else 
        { 
            if(prepararValidarUbicacionDocumentacion()) {      // #253692  
                if( validarObligatorios("<%=descriptor.getDescripcion("msjObligTodos")%>"))         
                {
                    var continuar=0;
                    var interesado = interesado_Obligatorio();
                    if  (getNumInteresados()>0)
                    {
                        continuar=1;
                    }
                    else if (interesado != "1")               
                    {
                        continuar=jsp_alerta("C",'<%=descriptor.getDescripcion("msjGrabarSinInteresado")%>');  //Muestra un mensaje indicando que no hemos introducido ningun interesado
                    }
                    else
                    {
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjInterOblig")%>');
                        continuar = 0;
                    }    
                    if(continuar ==1)
                    {     
                            modificaVariableCambios(0);
                            document.forms[0].opcion.value="grabarExpediente";
                            document.forms[0].target="oculto";
                            document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
                            document.forms[0].submit();
                    }
                }
            } else jsp_alerta("A",'<%=descriptor.getDescripcion("msjUbicDocFormatoNoValido")%>');
        }
    }
}

function pulsarGrabarObservaciones() {
    cubrirListaMostrar();
                        var validarObs = verificarTamanoTextArea();
    if(validarObs!=null && validarObs.length>0)
         jsp_alerta('A',validarObs);
    else
    if( validarObligatorios("<%=descriptor.getDescripcion("msjObligTodos")%>")) {
        modificaVariableCambios(0);
        document.forms[0].opcion.value="grabarExpedienteObservaciones";
        document.forms[0].target="oculto";
        document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
        document.forms[0].submit();
    }
}

function cerrarVentanaSiDesdeAltaRE() {
    if (desdeAltaRE == "si") {
        volverPantallaPrincipal();
    }
}

function pulsarVolverTramitacion() {
    cerrarVentanaSiDesdeAltaRE();
    var desdeConsulta = "<%= desdeConsulta %>";
    if (desdeConsulta == "si") {
        var consulta = "<%= (String) session.getAttribute("consulta") %>";
        if (consulta == 'null') consulta = 'normal';
        if(consulta.trim()=="normal") {

            pleaseWait1('on',this);
            document.forms[0].target="mainFrame";
            document.forms[0].action="<c:url value='/sge/ConsultaExpedientes.do?opcion=volverConsultar'/>";
            document.forms[0].submit();
        }
        else if(consulta.trim()=="campo")
            {
                pleaseWait1('on',this);
                document.forms[0].target="mainFrame";
                document.forms[0].action="<c:url value='/sge/ConsultaExpedientePorCampoSup.do?opcion=volverDesdeFichaExpediente'/>";
                document.forms[0].submit();
            }
        } else {
            //if((parent.existenCambiosSinGrabar)!='undefined')irAlAction=irActionConCambios(parent.existenCambiosSinGrabar.value,mensajeGuardarCambios);
            if((document.forms[0].existenCambiosSinGrabar)!='undefined')irAlAction=irActionConCambios(document.forms[0].existenCambiosSinGrabar.value,mensajeGuardarCambios);
            if(irAlAction==1)
            {
                //if((parent.existenCambiosSinGrabar)!='undefined')parent.existenCambiosSinGrabar.value=0;
                if((document.forms[0].existenCambiosSinGrabar)!='undefined') document.forms[0].existenCambiosSinGrabar.value=0;
                // original
                //document.forms[0].opcion.value="expedientesPendientes";                                                                
                document.forms[0].opcion.value="volverPantallaPendientes";
                document.forms[0].target="mainFrame";
                document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>?volverPantallaExpedientesPendientes=SI&filtroPendientes="+ filtroPendientes+"&lineasPendientes="+ lineasPendientes+"&columnaPendientes="+ columnaPendientes+"&paginaPendientes="+ paginaPendientes+"&tipoOrdenPendientes="+ tipoOrdenPendientes+"";
                document.forms[0].submit();
            }
    }
}

function pulsarImprimir() {
    pleaseWait1('on',this);
    document.forms[0].opcion.value="imprimirExpediente";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
    document.forms[0].submit();
}

function pulsarExportar() {
	var numExpediente = $("#numero").val();
	pleaseWait1('on',this);
	try{
		$.ajax({
			url: '<%=request.getContextPath()%>/sge/FichaExpediente.do',
			type: 'POST',
			async: true,
			data: {'opcion': 'exportarExpediente', 'numExpediente':numExpediente},
			success: procesarRespuestaImprimirResultados,
			error: errorEnLaConexion 
		}); 
		} catch (Err) {
			pleaseWait1('off',this);
			jsp_alerta("A", '<%=descriptor.getDescripcion("msgErrGenServ")%>');
		}
}

 function procesarRespuestaImprimirResultados(result){
	pleaseWait1('on',this);
	
	if(result){
		var resultado = JSON.parse(result);
		var datos = resultado.tabla;
		if(datos!=undefined && datos.error!=undefined && datos.error.length>0){
			jsp_alerta("A",datos.error)
		}else {
			document.forms[0].pathExportZip.value = datos.fichero;
			document.forms[0].opcion.value="descargarFichero";
			document.forms[0].target="mainFrame";
			document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
			document.forms[0].submit();
		}
	} else {
		jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
	}
	
	pleaseWait1('off',this);
}

function errorEnLaConexion(){
	pleaseWait1('off',this);
	jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');

}

function abrirInforme(nombre){
    var source = "<c:url value='/jsp/verPdf.jsp?opcion=null&nombre='/>"+nombre;
    ventanaInforme = window.open('<%=request.getContextPath()%>/jsp/mainVentana.jsp?source='+source,'ventana','width=800px,height=550px,status='+ '<%=statusBar%>' + ',toolbar=no, resizable');
        ventanaInforme.focus();
}

function pulsarFinalizar(accion) {   
    var i = tabTramites.selectedIndex;  
    if(i != -1) {
        if ((getNumInteresadosExpediente()== 0) && (interesado_Obligatorio() == "1")){
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjInterOblig")%>');
        }else{    
            
                if (listaTramitesOriginal[i][15]!="" && listaTramitesOriginal[i][15]!=codigoUsuario){
                    jsp_alerta("A","<%=descriptor.getDescripcion("msjNoFinalBloqueo")%>");
                }else{
                    if((document.forms[0].existenCambiosSinGrabar)!='undefined')
                        var irAlAction=irActionConCambios(document.forms[0].existenCambiosSinGrabar.value,mensajeGuardarCambios);
                    if(irAlAction==1) {
                        if((document.forms[0].existenCambiosSinGrabar)!='undefined') 
                            document.forms[0].existenCambiosSinGrabar.value=0;                                        
                        cargarTramitacionOculta(i);
                    } 
                }
            } 
        
    } else {
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
    }
}
function consultarTramites(nCS,oblig,literalFinalizarEntrada){
    literalFinalizar = literalFinalizarEntrada;
    var datosAEnviarTramites = new Array();
    datosAEnviarTramites[0] = nCS;
    datosAEnviarTramites[1] = oblig;
     // #289948: par?metro que indica si se ha notificado y el avance ya no puede ser cancelado
    datosAEnviarTramites[2] = postNotificacionEnviada;
    
    var source1 = "<c:url value='/jsp/sge/accesoExternoExpediente/listaTramitesAccesoExterno.jsp?opcion=null'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source1,
        datosAEnviarTramites,'width=790,height=500,status='+ '<%=statusBar%>', 
        function(datosConsultaT) {
        if(datosConsultaT!=undefined && datosConsultaT[0] =="si") {
            document.forms[0].listaCodTramites.value = datosConsultaT[1];
            document.forms[0].listaModoTramites.value = datosConsultaT[2];
            document.forms[0].listaUtrTramites.value = datosConsultaT[3];
            document.forms[0].listaTramSigNoCumplenCondEntrada.value= datosConsultaT[4];
            if(datosConsultaT[5]=="tramite"){
                cargaDirecta = true;
            } else cargaDirecta = false;
            
            if(datosConsultaT[1] !="") {
                document.forms[0].opcion.value=literalFinalizar;
            } else {
                document.forms[0].opcion.value="finalizarSinCondAccesoExterno";
            }
            document.forms[0].target="oculto";
            document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";

            //En tramite que no cumple condiciones de entrada
            if(datosConsultaT[4].indexOf("no")!=-1){
                var source = "<c:url value='/jsp/sge/informacionTramiteCondEntradaNoValidas.jsp'/>";
                var datosEntrada = new Array();
                abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,
                    datosEntrada,'width=700,height=550,status='+ '<%=statusBar%>', 
                    function(datosInformacion) {
                        document.forms[0].submit();
                    });
            }else{
                document.forms[0].submit();
            }
        } else { activarBotonFinalizarTramite();
        }
    });
}

function procesoFinalizar(entro,validarFirma,accion,obliEntrada,obligatorioDesfEntrada,accionAfirmativaEntrada,AccionNegativaEntrada,pregunta,fechaFin,bloqueo) {
    obli = obliEntrada;                        
    obligatorioDesf = obligatorioDesfEntrada;                        
    accionAfirmativa= accionAfirmativaEntrada;                        
    AccionNegativa= AccionNegativaEntrada;                       

    var i = tabTramites.selectedIndex;
    if(i != -1) {
        document.forms[0].bloqueo.value=bloqueo;                            
        if(entro==0){
            jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
			activarBotonFinalizarTramite();
            }else if(validarFirma==0){
            jsp_alerta("A",'<fmt:message key='Sge.TramitacionExpedientesForm.FinalizacionTramite.Error.AlgunDocumentoSinFirmar'/>');
        }else if(fechaFin != 'null' && fechaFin != '') {
            jsp_alerta("A","No se puede finalizar el trámite porque ya esta finalizado");
			activarBotonFinalizarTramite();
        }else{                            
            if(listaTramitesOriginal[i][2]==listaTramitesPlugin[i][0] && listaTramitesOriginal[i][1]==listaTramitesPlugin[i][1] && (listaTramitesPlugin[i][7]=="SI" || listaTramitesPlugin[i][7]=="si")){
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorFichaExtFin")%>');
            }else
            if(listaTramitesOriginal[i][18]=="SI" || listaTramitesOriginal[i][18]=="si"){
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgTareasPendientesIniAlert")%>');
            }else{
                    if(accion == "F") { 
                        if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondFinal")%>") == 1) {
                            document.forms[0].opcion.value="finalizarExpAccesoExterno";
                            document.forms[0].target="oculto";
                            document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                            document.forms[0].submit();
                        }
                    } else if(accion=="T"){
                        consultarTramites(0,obli,"finalizarConTramAccesoExterno");
                    } else if(accion == "R") {
                    var datosAEnviar = new Array();
                    datosAEnviar[0] = accion;
                    
                    var source = "<c:url value='/jsp/sge/favorDesfavor.jsp?opcion=null'/>";
                    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,
                        datosAEnviar,'width=700,height=400,status='+ '<%=statusBar%>', 
                        function(datosConsulta){
                        if(datosConsulta!=undefined){
                            if(datosConsulta[0] =="siFavorable") {
                                if(accionAfirmativa == "T") {
                                    consultarTramites(1,obli,"finalizarResFavConTramAccesoExterno");
                                } else {
                                if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondFinal")%>") == 1) {
                                    document.forms[0].opcion.value="finalizarResFavAccesoExterno";
                                    document.forms[0].target="oculto";
                                    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                                    document.forms[0].submit();
                                }
                            }
                        } else if(datosConsulta[0] =="noFavorable") {
                        if(AccionNegativa == "T") {
                            consultarTramites(2,obligatorioDesf,"finalizarResDesfavConTramAccesoExterno");
                        } else { 
                        if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondFinal")%>") == 1) {
                            document.forms[0].opcion.value="finalizarResDesfavAccesoExterno";
                            document.forms[0].target="oculto";
                            document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                            document.forms[0].submit();
                        }
                    }
                }
            } else {
             activarBotonFinalizarTramite();
            }
            });
        } else if(accion== "P") {
            var datosAEnviar = new Array();
            datosAEnviar[0] = accion;
            datosAEnviar[1] = pregunta;
            var source = "<c:url value='/jsp/sge/favorDesfavor.jsp?opcion=null'/>";
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,
                datosAEnviar,'width=700,height=400,status='+ '<%=statusBar%>', 
                function(datosConsulta){
                    if(datosConsulta!=undefined){
                        if(datosConsulta[0] =="siFavorable") {
                             if(accionAfirmativa == "T") {
                                consultarTramites(1,obli,"finalizarPregFavConTramAccesoExterno");
                            } else { 
                            if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondFinal")%>") == 1) {
                                document.forms[0].opcion.value="finalizarPregFavAccesoExterno";
                                document.forms[0].target="oculto";
                                document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                                document.forms[0].submit();
                            }
                        }
                    } else if(datosConsulta[0] =="noFavorable") { 
                        if(AccionNegativa == "T") { 
                            consultarTramites(2,obligatorioDesf,"finalizarPregDesfavConTramAccesoExterno");
                        } else { 
                        if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondFinal")%>") == 1) {
                            document.forms[0].opcion.value="finalizarPregDesfavAccesoExterno";
                            document.forms[0].target="oculto";
                            document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                            document.forms[0].submit();
                        }
                    }
                }
            }
        });
    }else{ 
        if(jsp_alerta("C","<%=descriptor.getDescripcion("msjCondSinCond")%>") == 1) {
            document.forms[0].ocurrenciaTramite.value = listaTramitesOriginal[i][0];
            document.forms[0].codTramite.value = listaTramitesOriginal[i][1];
            document.forms[0].codUnidadTramitadoraTram.value = listaTramitesOriginal[i][10];
            document.forms[0].opcion.value="finalizarSinCondAccesoExterno";
            document.forms[0].target="oculto";
            document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
            document.forms[0].submit();
        }else{
		 activarBotonFinalizarTramite();	
	}
		
    }
}// else tiene tareas pendientes de inicio
}//fecha fin
}else{
jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
}

}

function finalizacionTramite(respOpcion, procedimientoAsociado) {
    if ("noFinalizado"==respOpcion) {
        msnj = "<%=descriptor.getDescripcion("msjTramNoFinalizado")%>";
        jsp_alerta("A",msnj);
    } else if ("yaFinalizado"==respOpcion) {
        msnj = "<%=descriptor.getDescripcion("msjExpYaFinalizado")%>";
        jsp_alerta("A",msnj);
        recargarPagina();
    } else if (respOpcion == "AutoRetrocedido") {
        jsp_alerta("A", "<%=descriptor.getDescripcion("msjTramAutoRet")%>")
    } else if (respOpcion == "FinalizadoNormal") {
        jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoIniciadosTodos")%>");
        recargarPagina();
    } else { 
        if(cargaDirecta){
            cargarTramiteDirecto('cargarTramiteDirecto');
        } else{
            recargarPagina();
        }
    }
}
function finalizarExpediente(mensaje) {
    jsp_alerta("A",mensaje);
}

function ErrorExpresion(respOpcion) {
        
    if ("ErrorExpresion"== respOpcion.substring(0,14)){    
        var campos = respOpcion.substring(15,respOpcion.length);
        var campos_fin = campos.split("#");
        msnj = "<%=descriptor.getDescripcion("msjExpresionErr")%>" + ":" ;
        for (i = 0; i < campos_fin.length; i++) 
        {            
            msnj = msnj + "<br>" + "     " + campos_fin[i];                 
        }      
        jsp_alerta("A",msnj);                        
    }  
}

function pulsarVolver1() {
    pleaseWait1('on',this);
    document.forms[0].opcion.value="cargarPestTram";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
    document.forms[0].submit();
}

function pulsarVolver() {
    cerrarVentanaSiDesdeAltaRE();
    var consulta = "<%= (String) session.getAttribute("consulta") %>";
    if (consulta == 'null') consulta = 'normal';
    if(consulta.trim()=="normal") {

            pleaseWait1('on',this);
            document.forms[0].target="mainFrame";
        document.forms[0].action="<c:url value='/sge/ConsultaExpedientes.do?opcion=volverConsultar'/>";
            document.forms[0].submit();
        }
        else
            if(consulta.trim()=="campo")
                {
                    pleaseWait1('on',this);
                    document.forms[0].target="mainFrame";
                    document.forms[0].action="<c:url value='/sge/ConsultaExpedientePorCampoSup.do?opcion=volverDesdeFichaExpediente'/>";
                    document.forms[0].submit();
                }
                else
                    if(consulta.trim()=="cerrar")
                        {
                            // Se cierra la ventana
                            top.close();
                        }
}


function tramitesPendientes(notifRealizada, resultadoFinalizar) {
    finalizacionTramite(resultadoFinalizar);
    if (notifRealizada=="no"){
        msnj = "<%=descriptor.getDescripcion("msjNotifNoRealiz")%>";
        jsp_alerta("A",msnj);
    }
}

function finalizacionExpediente(respOpcion, procedimientoAsociado){
    if ("expedienteNoFinalizado"==respOpcion) {
        msnj = "<%=descriptor.getDescripcion("msjExpNoFinalizado")%>";
        jsp_alerta("A",msnj);
    } else if ("expedienteConTramitesIniciados"==respOpcion) {
        msnj = "<%=descriptor.getDescripcion("msjTramIniciado")%>";
        jsp_alerta("A",msnj);
    } else if ("noFinalizadoFirmasExpediente"==respOpcion) {
        msnj = "<%=descriptor.getDescripcion("msjDocsConFirmas")%>";
        jsp_alerta("A",msnj);
    } else {
        msnj = "<%=descriptor.getDescripcion("msjExpFinalizado")%>";
        jsp_alerta("A",msnj);
        recargarPagina();
    }
}

function enviarInfoExpIniciado(uorInicioExp, uorInicioTram) {
    document.forms[0].opcion.value="enviarInfoExpIniciado";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>?uorInicioExp=" + uorInicioExp + "&uorInicioTram=" + uorInicioTram;
    document.forms[0].submit();
}

function pulsarVerDocumentos(){
    var source = "<c:url value='/sge/FichaExpediente.do?opcion=verDocumentacion'/>";    
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source=' + source,null,
            'width=996,height=710,status='+ '<%=statusBar%>', function(){});
}
function pulsarFinalizarExpediente(){
       if((document.forms[0].existenCambiosSinGrabar)!='undefined')var irAlAction=irActionConCambios(document.forms[0].existenCambiosSinGrabar.value,mensajeGuardarCambios);
       if(irAlAction==1) {
           if((document.forms[0].existenCambiosSinGrabar)!='undefined')document.forms[0].existenCambiosSinGrabar.value=0;

            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source=' + 
                "<%=request.getContextPath()%>/jsp/sge/finExpNoConvencional.jsp",null,
                'width=950,height=640,status='+ '<%=statusBar%>', function(datos){
                if(datos){
                    document.forms[0].justificacion.value=datos[0];
                    document.forms[0].autoriza.value=datos[1];
                    document.forms[0].opcion.value="finalizarExpedienteNoConvencional";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                    document.forms[0].submit();
                }
           });
       }
}

function MostrarInformacionFinalizacion (){

    var argumentos = new Array();
    var codMun = document.forms[0].codMunicipio.value;
    var codEje = document.forms[0].ejercicio.value;
    var codNum = document.forms[0].numExpediente.value;

    var source = "<c:url value='/sge/TramitacionExpedientes.do?opcion=consultaFinalizarExpedienteNoConvencional'/>";
    abrirXanelaAuxiliar(source+"&codMun="+codMun+"&codEje="+codEje+"&codNum="+codNum,argumentos,
        'width=990,height=450,status='+ '<%=statusBar%>', function(){});
 }

function volverFinalizacionNoConvencional(expFinalizado) {
    if (expFinalizado=="finalizadoNoConvencional") {
        jsp_alerta('A',"<%=descriptor.getDescripcion("msjExpFinNC")%>");
            document.forms[0].opcion.value="expedientesPendientes";
            document.forms[0].target="mainFrame";
            document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
            document.forms[0].submit();
        }else jsp_alerta('A',"<%=descriptor.getDescripcion("msjExpNoFinNC")%>");
}

function cargarCombos() {
    comboTipoDoc.addItems(codTipoDocs,descTipoDocs);
}

function pulsarBuscarTercero() {
    cargaRapida=true;
    var documento = document.forms[0].txtDNI.value;
    var codigoDoc = document.forms[0].codTipoDoc.value;

    if(BUSQUEDA_INTERESADO_ACTIVA && codigoDoc!=null && codigoDoc!='undefined' && documento!=null && documento!='undefined'){

        if (!validarDocumento()) return;
       pleaseWait1('on',this);
        this.pulsarBuscarTerceros = function(){};
        document.forms[0].txtDNI.value=document.forms[0].txtDNI.value.toUpperCase();
        document.forms[0].opcion.value="buscar";
        document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do?desde=expedientes";
        document.forms[0].target="oculto";
        document.forms[0].submit();
    }
}


// Carga la ventana de terceros con el tercero pasado
function terceroBuscado(datos) { //oculto
    pleaseWait1('off',this);
    var argumentos = new Array();
    argumentos['modo'] = 'seleccion';
    argumentos['terceros'] = datos;
    argumentos['domicilio'] = getCodDomicilio(terceroActual);

    var source = '<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializar&ventana=true';

    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,
        argumentos,"width=1000,height=900,status="+ "<%=statusBar%>",
        function (tercero) {
        if (tercero != undefined) {
            var separador = '§¥';
           modificaVariableCambios(1);
            if (tercero[0] == '0') { // Se trata de un tercero externo                                        
                var request = buildRequestInsercionDirectaTercero(tercero[22]);
                document.forms[0].opcion.value = "grabarTercDomExterno";
                document.forms[0].target = "oculto";
                document.forms[0].action = '<%=request.getContextPath()%>/BusquedaTerceros.do?' + request;
                document.forms[0].submit();
                return;
            }

            if(document.forms[0].listaCodTercero.value!="undefined"){

                var lista = document.forms[0].listaCodTercero.value;
                var LISTA_COD = lista.split(separador);

                var codTercero = tercero[2];
                var codDomicilio = tercero[13];
                var domicilio = tercero[18] + ' - ' + tercero[17] + ' - ' + tercero[16];

                // Determinamos si se trata de un tercero ya cargado
                // en ese caso lo mostramos antes de actualizarlo
                var pos = existeTercero(codTercero);
                if(cargaRapida) pos=0;
                if (pos != -1) {
                  cargarTerceroPosicion(pos);
                }

                if(codTercero==terceroActual){
                    document.forms[0].titular.value = tercero[6];
                    document.forms[0].domicilio.value = domicilio;

                    var posActualTercero = getPosTerceroActual();
                    modificarListaInteresados(tercero[4],tercero[5],tercero[6],domicilio,posActualTercero);

                     if(document.forms[0].listaVersionTercero.value!="undefined"){
                        if( document.forms[0].listaVersionTercero.value==null ||  document.forms[0].listaVersionTercero.value.length==0){
                            document.forms[0].listaVersionTercero.value = tercero[1] + '§¥';
                        }
                        else{
                            var listaVerAux = document.forms[0].listaVersionTercero.value.split('§¥');
                            listaVerAux[pos] = tercero[1];
                            var contador = 0;
                            document.forms[0].listaVersionTercero.value = '';
                            while (contador<listaVerAux.length-1) {
                                document.forms[0].listaVersionTercero.value +=  listaVerAux[contador]+ '§¥';
                                contador++;
                            }
                            document.forms[0].listaVersionTercero.value +=  listaVerAux[contador];
                        }
                    }
                    BUSQUEDA_INTERESADO_ACTIVA = false;

                    document.forms[0].codTipoDoc.value = tercero[4];
                    document.forms[0].descTipoDoc.value = getDescTipoDoc(tercero[4]);
                    document.forms[0].txtDNI.value = tercero[5];
                    // Fin de tratamiento del rol

                    // Se almacenan en las correspondientes variables los datos del tercero, domicilio y rol actual
                    terceroActual = tercero[2];
                    domicilioActual =  tercero[13];

                    if(document.forms[0].listaCodDomicilio.value!="undefined"){

                        if( document.forms[0].listaCodDomicilio.value==null ||  document.forms[0].listaCodDomicilio.value.length==0){
                            document.forms[0].listaCodDomicilio.value = tercero[13] + '§¥';
                        }
                        else{
                            var listaVerAux = document.forms[0].listaCodDomicilio.value.split('§¥');
                            listaVerAux[pos] = tercero[13];
                            var contador = 0;
                            document.forms[0].listaCodDomicilio.value = '';
                            while (contador<listaVerAux.length-1) {
                                document.forms[0].listaCodDomicilio.value +=  listaVerAux[contador]+ '§¥';
                                contador++;
                            }
                            document.forms[0].listaCodDomicilio.value +=  listaVerAux[contador];

                        }
                    }
                    var terAnt = getCodTerceroAnterior();
                    if(terAnt==-1)
                        deshabilitarImagenBoton("imgAnt",true);
                    else
                        deshabilitarImagenBoton("imgAnt",false);
                } else {
                    // Se da de alta un nuevo tercero
                    if(document.forms[0].listaCodTercero.value!="undefined"){
                        if(document.forms[0].listaCodTercero.value==null || document.forms[0].listaCodTercero.value.length==0){
                            document.forms[0].listaCodTercero.value = tercero[2] + '§¥';
                        }
                        else{
                            document.forms[0].listaCodTercero.value += tercero[2] + '§¥';
                        }
                    }

                if(document.forms[0].listaCodDomicilio.value!="undefined"){

                    if( document.forms[0].listaCodDomicilio.value==null ||  document.forms[0].listaCodDomicilio.value.length==0){
                            document.forms[0].listaCodDomicilio.value = tercero[13] + '§¥';
                    }
                    else{
                            document.forms[0].listaCodDomicilio.value +=  tercero[13] + '§¥';
                    }
                }
                document.forms[0].titular.value = tercero[6];
                document.forms[0].domicilio.value = domicilio;

                // SE SELECCIONA EL ROL POR DEFECTO Y SI HACE FALTA QUE LO CAMBIE EL USUARIO
                var pD = "";
                for(var u=0; u<codRols.length; u++) {
                    if (pDef[u] == "SI"){ pD = u;}
                }
               
                
                if(document.forms[0].listaRol.value!="undefined"){
                    if( document.forms[0].listaRol.value==null ||  document.forms[0].listaRol.value.length==0){
                            document.forms[0].listaRol.value = codRols[pD] + '§¥';
                    }
                    else{
                            document.forms[0].listaRol.value +=  codRols[pD] + '§¥';
                    }
                }
                if(document.forms[0].listaMostrar.value!="undefined"){
                    if( document.forms[0].listaMostrar.value==null ||  document.forms[0].listaMostrar.value.length==0){
                            document.forms[0].listaMostrar.value = "true" + '§¥';
                    }
                    else{
                            document.forms[0].listaMostrar.value +=  "true" + '§¥';
                    }
                }
                if(document.forms[0].listaVersionTercero.value!="undefined"){
                    if( document.forms[0].listaVersionTercero.value==null ||  document.forms[0].listaVersionTercero.value.length==0){
                            document.forms[0].listaVersionTercero.value = tercero[1] + '§¥';
                    }
                    else{
                            document.forms[0].listaVersionTercero.value +=  tercero[1]+ '§¥';
                    }
                }
                insertarListaInteresados(tercero[4],tercero[5],tercero[6],tercero[18]);
                posInteresadoActual++;
                updateMsgNumInteresados();
                BUSQUEDA_INTERESADO_ACTIVA = false;

                document.forms[0].codTipoDoc.value = tercero[4];
                document.forms[0].descTipoDoc.value = getDescTipoDoc(tercero[4]);
                document.forms[0].txtDNI.value = tercero[5];
                // Fin de tratamiento del rol
                // Se almacenan en las correspondientes variables los datos del tercero, domicilio y rol actual
                terceroActual = tercero[2];
                domicilioActual =  tercero[13];
                rolActual = codRols[pD];

                comboRol.buscaCodigo(codRols[pD]);
                var terAnt = getCodTerceroAnterior();
                if(terAnt==-1)
                    deshabilitarImagenBoton("imgAnt",true);
                else
                    deshabilitarImagenBoton("imgAnt",false);
                }
                actualizarNotificacion(<c:out value="${sessionScope.FichaExpedienteForm.checkNotifActivo}"/>);
                // Se inserta el tercero por defecto
                insertarTerceroPorDefecto(tercero);
                if(getNumInteresados()==1){
                    mostrarOcultarIcono("botonListaDomicilios","on");
                    mostrarOcultarIcono("botonListaInteresados","off");
                    mostrarOcultarIcono("botonEliminarInteresado","on");
                }
                if(getNumInteresados()>1){
                    mostrarOcultarIcono("botonListaDomicilios","on");
                    mostrarOcultarIcono("botonListaInteresados","on");
                    mostrarOcultarIcono("botonEliminarInteresado","on");
                }
            }
        }
    });
}

// Recupera el cdigo del domicilio correspondiente a un determinada tercero
function getCodDomicilio(codTercero)
{
    var separador = '§¥';
    var salida = 0;

    var pos = getPosTerceroActual();
    var lista = document.forms[0].listaCodDomicilio.value;
    if(lista!=null && lista.length>=1){
        var LISTA = lista.split(separador);
        salida = LISTA[pos];
    }
    return salida;
}
function cargarTerceroExpedientes(datos){
    terceroBuscado(datos);
}

function buildRequestInsercionDirectaTercero(datos) {
    var tercAInsertar = new Array();
    var domAInsertar = new Array();

    tercAInsertar = datos;
    var valoresCamposSuplementarios = "";
    for(i=0;i<datos[22].length;i++){
        var elemento = datos[22][i];
        var codigoCampo = elemento[1];
        var valorCampo = elemento[4]; 
        valoresCamposSuplementarios = valoresCamposSuplementarios + codigoCampo + ";" + valorCampo + '§¥';																				
    }

    domAInsertar = datos[18][0];
    var request = 'txtIdTercero=' + tercAInsertar[0] + '&codTipoDoc=' + tercAInsertar[2] + '&txtDNI=' + tercAInsertar[3] +
    '&txtInteresado=' + tercAInsertar[4] + '&txtApell1=' + tercAInsertar[5] + '&txtApell2=' + tercAInsertar[6] +
    '&txtPart=' + tercAInsertar[7] + '&txtPart2=' + tercAInsertar[8] + '&txtTelefono=' + tercAInsertar[9] +
    '&txtCorreo=' + tercAInsertar[10] + '&codTerceroOrigen=' + tercAInsertar[21] + '&codPais=' + domAInsertar[6] + '&codProvincia=' + domAInsertar[7] +
    '&codMunicipio=' + domAInsertar[8] + '&descVia=' + domAInsertar[29] + '&codTVia=' + domAInsertar[19] +
    '&txtNormalizado=' + domAInsertar[24] + '&codUso=' + domAInsertar[21] + '&txtCodVia=' + domAInsertar[23] +
    '&txtNumDesde=' + domAInsertar[9] + '&txtNumHasta=' + domAInsertar[11] + '&domActual=0&descUso=' + domAInsertar[22] +
    '&descTVia=' + domAInsertar[20] + '&descProvincia=' + domAInsertar[1] + '&descMunicipio=' + domAInsertar[2] +
    '&descPostal=' + domAInsertar[4] + '&txtBarriada=' + domAInsertar[18] + '&txtDomicilio=' + domAInsertar[3] +
    '&txtLetraDesde=' + domAInsertar[10] + '&txtLetraHasta=' + domAInsertar[12] + '&txtBloque=' + domAInsertar[13] +
    '&txtPortal=' + domAInsertar[14] + '&txtEsc=' + domAInsertar[15] + '&txtPlta=' + domAInsertar[16] +
    '&txtPta=' + domAInsertar[17] + '&codESI=' + domAInsertar[27] + "&CAMPOS_SUPLEMENTARIOS=" + valoresCamposSuplementarios;
    return request;
}
// Función que recupera los terceros encontrados realizados mediante una busqueda
// lanzada directamente desde la pantalla de la ficha de expediente.
function recuperaBusquedaTerceros(datos){ 
    pleaseWait1('off',this);
    if(datos.length>1){
        // Hay mas de un interesado => Se abre la ventana de gestion de terceros
	terceros = datos;
        pulsarBuscarTerc("cargar");

    } else if(datos.length==1){ 
        modificaVariableCambios(1);
        // La busqueda de tercero ha devuelto un unico resultado.
	if (datos[0][0] == '0') { // Se trata de un tercero externo
            
            var request = buildRequestInsercionDirectaTercero(datos[0]);
            document.forms[0].opcion.value = "grabarTercDomExterno";
            document.forms[0].target = "oculto";
            document.forms[0].action = '<%=request.getContextPath()%>/BusquedaTerceros.do?' + request;
            document.forms[0].submit();
            return;
        }
        
        
        // El tercero se ha recuperado de la base de datos del SIGP
        var codTercero = datos[0][0];
        var version = datos[0][1];
        var tipoDoc = datos[0][2];
        var doc = datos[0][3];
        var nombre = datos[0][4];
        var apel1 = datos[0][5];
        var apel2 = datos[0][6];

        // obtenemos el indice de la direccion principal
        var indiceDireccionPrincipal = indiceDomPrincipal(datos[0]);
        var direccion = datos[0][18];
        var provincia = direccion[indiceDireccionPrincipal][1];
        var localidad = direccion[indiceDireccionPrincipal][2];
        var via1       = direccion[indiceDireccionPrincipal][3];
        var codDomicilio = direccion[indiceDireccionPrincipal][5];
        var tipoVia      =  direccion[indiceDireccionPrincipal][20];
        var via2         =  direccion[indiceDireccionPrincipal][29];

        var txtNumDesde = direccion[indiceDireccionPrincipal][9];
        var txtLetraDesde = direccion[indiceDireccionPrincipal][10];
        var txtNumHasta = direccion[indiceDireccionPrincipal][11];
        var txtLetraHast= direccion[indiceDireccionPrincipal][12];
        var txtBloque = direccion[indiceDireccionPrincipal][13];
        var txtPortal = direccion[indiceDireccionPrincipal][14];
        var txtEsc = direccion[indiceDireccionPrincipal][15];
        var txtPlta = direccion[indiceDireccionPrincipal][16];
        var txtPta =direccion[indiceDireccionPrincipal][17];
        var caracteristicasDom="";

        caracteristicasDom += ( txtNumDesde!='')?(' '+txtNumDesde):'';
        caracteristicasDom += ( txtLetraDesde!='')?(' '+txtLetraDesde):'';
        caracteristicasDom += ( txtNumHasta !='')?(' - '+txtNumHasta):'';
        caracteristicasDom += ( txtLetraHast !='')?(' '+txtLetraHast):'';
        caracteristicasDom += ( txtBloque !='')?(' Bl. '+txtBloque):'';
        caracteristicasDom += ( txtPortal !='')?(' Portal '+txtPortal):'';
        caracteristicasDom += ( txtEsc !='')?(' Esc. '+txtEsc):'';
        caracteristicasDom += ( txtPlta !='')?(' '+txtPlta + 'º'):'';
        caracteristicasDom += ( txtPta!='')?(' '+txtPta):'';

        if(document.forms[0].listaCodTercero.value!="undefined"){
            if(document.forms[0].listaCodTercero.value==null || document.forms[0].listaCodTercero.value.length==0){
                document.forms[0].listaCodTercero.value = codTercero + '§¥';
            }
            else{
                document.forms[0].listaCodTercero.value += codTercero + '§¥';
            }
        }
        if(document.forms[0].listaCodDomicilio.value!="undefined"){
            if( document.forms[0].listaCodDomicilio.value==null ||  document.forms[0].listaCodDomicilio.value.length==0){
                document.forms[0].listaCodDomicilio.value = codDomicilio + '§¥';
            }
            else{
                document.forms[0].listaCodDomicilio.value +=  codDomicilio  + '§¥';
            }
        }

        var nombreCompleto = '';
        if (apel1 != '') nombreCompleto += apel1;
        if (apel2 != '') nombreCompleto += ' ' + apel2 + ',';
        else if (nombreCompleto != '') nombreCompleto += ',';
        nombreCompleto += nombre;
        var domicilio = tipoVia + " " +  via2 + " " + via1 +" "+caracteristicasDom+ " - " + localidad + " - " + provincia;
        document.forms[0].titular.value = nombre;
        document.forms[0].domicilio.value = domicilio;
        // SE SELECCIONA EL ROL POR DEFECTO Y SI HACE FALTA QUE LO CAMBIE EL USUARIO
        var pD = "";
        for(var u=0; u<codRols.length; u++) {
            if (pDef[u] == "SI"){ pD = u;}
        }
        
        hacerComboRolObligatorio(true);
                                            
        if(document.forms[0].listaRol.value!="undefined"){
            if( document.forms[0].listaRol.value==null ||  document.forms[0].listaRol.value.length==0){
                document.forms[0].listaRol.value = codRols[pD] + '§¥';
            }
            else{
                document.forms[0].listaRol.value +=  codRols[pD] + '§¥';
            }
        }

        if(document.forms[0].listaMostrar.value!="undefined"){
            if( document.forms[0].listaMostrar.value==null ||  document.forms[0].listaMostrar.value.length==0){
                document.forms[0].listaMostrar.value = "true" + '§¥';
            }
            else{
                document.forms[0].listaMostrar.value +=  "true" + '§¥';
            }
        }

        if(document.forms[0].listaVersionTercero.value!="undefined"){
            if( document.forms[0].listaVersionTercero.value==null ||  document.forms[0].listaVersionTercero.value.length==0){
                document.forms[0].listaVersionTercero.value = version + '§¥';
            }
            else{
                document.forms[0].listaVersionTercero.value +=  version + '§¥';
            }
        }

        insertarListaInteresados(tipoDoc,doc,nombreCompleto,domicilio);
        posInteresadoActual++;
        updateMsgNumInteresados();
        BUSQUEDA_INTERESADO_ACTIVA = false;

        document.forms[0].codTipoDoc.value = tipoDoc;
        document.forms[0].descTipoDoc.value = getDescTipoDoc(tipoDoc);
        document.forms[0].txtDNI.value = doc;

        // Fin de tratamiento del rol
        // Se almacenan en las correspondientes variables los datos del tercero, domicilio y rol actual
        terceroActual = codTercero;
        domicilioActual =  codDomicilio;
        rolActual = codRols[pD];
       comboRol.buscaCodigo(codRols[pD]);
        var terAnt = getCodTerceroAnterior();
        if(terAnt==-1)
            deshabilitarImagenBoton("imgAnt",true);
        else
            deshabilitarImagenBoton("imgAnt",false);
        // Se inserta el tercero por defecto
        insertarTerceroPorDefecto(datos[0]);
        actualizarNotificacion(<c:out value="${sessionScope.FichaExpedienteForm.checkNotifActivo}"/>);

        if(getNumInteresados()==1){
            mostrarOcultarIcono("botonListaDomicilios","on");
            mostrarOcultarIcono("botonListaInteresados","off");
            mostrarOcultarIcono("botonEliminarInteresado","on");
        }
        if(getNumInteresados()>1){
            mostrarOcultarIcono("botonListaDomicilios","on");
            mostrarOcultarIcono("botonListaInteresados","on");
            mostrarOcultarIcono("botonEliminarInteresado","on");
        }

    } else {
        if(jsp_alerta("",'<%=descriptor.getDescripcion("msjAltaTercero")%>')) {
            var argumentos = new Array();
            argumentos['modo'] = 'alta';
            argumentos['tipodoc'] = document.forms[0].codTipoDoc.value;
            argumentos['doc'] = document.forms[0].txtDNI.value;
            var source = '<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializar&ventana=true';

            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,
                argumentos,"width=990,height=900,status="+ "<%=statusBar%>",function (datos) {
                if(datos!=undefined) {
                     modificaVariableCambios(1);
                   if (datos[0] == '0') { // Se trata de un tercero externo
                        var request = buildRequestInsercionDirectaTercero(datos[22]);
                        document.forms[0].opcion.value = "grabarTercDomExterno";
                        document.forms[0].target = "oculto";
                        document.forms[0].action = '<%=request.getContextPath()%>/BusquedaTerceros.do?' + request;
                        document.forms[0].submit();
                        return;
                   }

                   if(document.forms[0].listaCodTercero.value!="undefined"){
                        if(document.forms[0].listaCodTercero.value==null || document.forms[0].listaCodTercero.value.length==0){
                            document.forms[0].listaCodTercero.value = datos[2] + '§¥';
                        } else{
                            document.forms[0].listaCodTercero.value += datos[2] + '§¥';
                        }
                    }
                    if(document.forms[0].listaCodDomicilio.value!="undefined"){
                        if( document.forms[0].listaCodDomicilio.value==null ||  document.forms[0].listaCodDomicilio.value.length==0){
                            document.forms[0].listaCodDomicilio.value = datos[13] + '§¥';
                        } else{
                            document.forms[0].listaCodDomicilio.value +=  datos[13] + '§¥';
                        }
                    }
                    document.forms[0].titular.value = datos[6];
                    var domicilio = datos[18] + ' - ' + datos[17] + ' - ' + datos[16];
                    document.forms[0].domicilio.value = domicilio;

                    // SE SELECCIONA EL ROL POR DEFECTO Y SI HACE FALTA QUE LO CAMBIE EL USUARIO
                    var pD = "";
                    for(var u=0; u<codRols.length; u++) {
                        if (pDef[u] == "SI"){ pD = u;}
                    }
                    

                    if(document.forms[0].listaRol.value!="undefined"){
                        if( document.forms[0].listaRol.value==null ||  document.forms[0].listaRol.value.length==0){
                            document.forms[0].listaRol.value = codRols[pD] + '§¥';
                        } else{
                            document.forms[0].listaRol.value +=  codRols[pD] + '§¥';
                        }
                    }
                    if(document.forms[0].listaMostrar.value!="undefined"){
                        if( document.forms[0].listaMostrar.value==null ||  document.forms[0].listaMostrar.value.length==0){
                            document.forms[0].listaMostrar.value = "true" + '§¥';
                        } else{
                            document.forms[0].listaMostrar.value +=  "true" + '§¥';
                        }
                    }
                    if(document.forms[0].listaVersionTercero.value!="undefined"){
                        if( document.forms[0].listaVersionTercero.value==null ||  document.forms[0].listaVersionTercero.value.length==0){
                            document.forms[0].listaVersionTercero.value = datos[1] + '§¥';
                        } else{
                            document.forms[0].listaVersionTercero.value +=  datos[1]+ '§¥';
                        }
                    }

                    insertarListaInteresados(datos[4],datos[5],datos[6],domicilio);
                    posInteresadoActual++;
                    updateMsgNumInteresados();
                    BUSQUEDA_INTERESADO_ACTIVA = false;

                    document.forms[0].codTipoDoc.value = datos[4];
                    document.forms[0].descTipoDoc.value = getDescTipoDoc(datos[4]);
                    document.forms[0].txtDNI.value = datos[5];

                    actualizarNotificacion(<c:out value="${sessionScope.FichaExpedienteForm.checkNotifActivo}"/>);

                    // Rol obligatorio al haber tercero
                    hacerComboRolObligatorio(true);
                    // Fin de tratamiento del rol
                    // Se almacenan en las correspondientes variables los datos del tercero, domicilio y rol actual
                    terceroActual = datos[2];
                    domicilioActual =  datos[13];
                    rolActual = codRols[pD];
                    comboRol.buscaCodigo(codRols[pD]);
                    var terAnt = getCodTerceroAnterior();
                    if(terAnt==-1) deshabilitarImagenBoton("imgAnt",true);
                    else deshabilitarImagenBoton("imgAnt",false);

                    // Se inserta el tercero por defecto
                    terceros = new Array();
                    insertarTerceroPorDefecto(datos);
                    if(getNumInteresados()==1){
                        mostrarOcultarIcono("botonListaDomicilios","on");
                        mostrarOcultarIcono("botonListaInteresados","off");
                        mostrarOcultarIcono("botonEliminarInteresado","on");
                    }
                    if(getNumInteresados()>1){
                        mostrarOcultarIcono("botonListaDomicilios","on");
                        mostrarOcultarIcono("botonListaInteresados","on");
                        mostrarOcultarIcono("botonEliminarInteresado","on");
                    }
                }// if datos!=undefined
            });
        }
    }
}

// Para un tipo de documento se busca la descripcin que le corresponde en el array global de descripcin de tipos de documentos
function getDescTipoDoc(tipo)
{
    var posCodTipo = 0;
    for(i=0;i<codTipoDocs.length;i++){
        if(codTipoDocs[i]==tipo){
            posCodTipo=i;break;
        }
    }
    return descTipoDocs[posCodTipo];
}

function cargarTercero(){
    if(terceroActual!=null){
        // Se cargan los datos del tercero actual para abrir la ventana de terceros
        document.forms[0].opcion.value="buscar_por_id";
        document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do?cargarTerceroExp=si&codTerc=" + terceroActual;
        document.forms[0].target="oculto";
        document.forms[0].submit();
        pleaseWait1('on',this);
    }
}

// Funcion para acceder a la pantalla de gestion de terceros
function pulsarBuscarTerc(opcion) {

    // Chequeo de modo consulta para no abrir la pantalla en dicho caso.
    if(document.forms[0].modoConsulta.value == "si") return;
    var argumentos = new Array();
    <% if(ptVO!=null){ %>
        var codPais = "<%=ptVO.getPais()%>";
        var codProvincia = "<%=ptVO.getProvincia()%>";
        var codMunicipio =  "<%=ptVO.getMunicipio()%>";
        var lineas = document.forms[0].lineasPagina.value;
        var pagina = document.forms[0].pagina.value;
        var terceroCodUOR = <%=m_Conf.getString("tercero.codUOR")%>;
    <% } else { %>
        //Indicamos valores por defecto
        var codPais = "108";
        var codProvincia = "99";
        var codMunicipio =  "999";
        var lineas = document.forms[0].lineasPagina.value;
        var pagina = document.forms[0].pagina.value;
        var terceroCodUOR = <%=m_Conf.getString("tercero.codUOR")%>;
    <% } %>
        
    if (document.forms[0].codTipoDoc.value == terceroCodUOR && (document.forms[0].txtDNI.value == '' || document.forms[0].txtDNI.value==null)) {
        abrirListadoUORs(terceroCodUOR);
   } else {
    
        if(terceroActual!=null && !isNaN(parseInt(terceroActual)) && opcion!=null && opcion=="alta"){
            // Si ya hay un tercero incluido en el expediente, se invoca la funcion
            // para cargar los datos de dicho tercero en la ventana de gestión de terceros.
            cargarTercero();
        } else {
            pleaseWait1('on',this);
            if(opcion!=null && opcion=="alta") {
                       // No hay ningun interesado y se quiere dar de alta un nuevo interesado
                // en el expediente.
                var argumentos = new Array();
                argumentos['modo'] = 'seleccion';
                argumentos['terceros']= terceros;
                if(document.forms[0].codTipoDoc.value!=null && document.forms[0].codTipoDoc.value.length>0)
                    argumentos['tipodoc'] = document.forms[0].codTipoDoc.value;

                if(document.forms[0].txtDNI.value!=null && document.forms[0].txtDNI.value.length>0)
                    argumentos['doc'] = document.forms[0].txtDNI.value;
               }

            if(opcion!=null && opcion=="cargar") {
                var argumentos = new Array();
                argumentos['modo'] = 'seleccion';
                argumentos['tipodoc'] = document.forms[0].codTipoDoc.value;
                argumentos['doc'] = document.forms[0].txtDNI.value;
                argumentos['terceros'] = terceros;
            }

            var navegador="";

            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                // Internet Explorer
                navegador="IE";
            }else{
                // Firefox u otro navegador
                navegador="FF";
            }

            var source = "<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializar&ventana=true&preguntaAlta=si&lineasPagina=" + lineas + '&pagina=' + pagina + "&codTipoDoc=" + document.forms[0].codTipoDoc.value + "&txtDNI=" + document.forms[0].txtDNI.value
                    + "&codPais=" + codPais + "&codProvincia=" + codProvincia + "&codMunicipio=" + codMunicipio + "&navegador=" + navegador;

            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,argumentos,
               "width=998,height=900,status="+ "<%=statusBar%>",function (datos) {
                if(datos!=undefined) {
                     modificaVariableCambios(1);
                    // Se recuperan los datos del tercero seleccionado en la pantalla de gestión de terceros.
                    if (datos[0] == '0') {
                        // Se trata de un tercero externo
                        var request = buildRequestInsercionDirectaTercero(datos[22]);
                        document.forms[0].opcion.value = "grabarTercDomExterno";
                        document.forms[0].target = "oculto";
                        document.forms[0].action = '<%=request.getContextPath()%>/BusquedaTerceros.do?' + request;
                        document.forms[0].submit();
                        return;
                    }

                 pleaseWait1('on',this);

                    if(document.forms[0].listaCodTercero.value!="undefined"){

                        if(document.forms[0].listaCodTercero.value==null || document.forms[0].listaCodTercero.value.length==0){
                            document.forms[0].listaCodTercero.value = datos[2] + '§¥';
                        } else{
                            document.forms[0].listaCodTercero.value += datos[2] + '§¥';
                        }
                    }

                        if(document.forms[0].listaCodDomicilio.value!="undefined"){
                            if( document.forms[0].listaCodDomicilio.value==null ||  document.forms[0].listaCodDomicilio.value.length==0){
                                document.forms[0].listaCodDomicilio.value = datos[13] + '§¥';
                            }
                            else{
                                document.forms[0].listaCodDomicilio.value +=  datos[13] + '§¥';
                            }
                        }

                        document.forms[0].titular.value = datos[6];
                        var domicilio = datos[18] + ' - ' + datos[17] + ' - ' + datos[16];
                        document.forms[0].domicilio.value = domicilio;

                        // SE SELECCIONA EL ROL POR DEFECTO Y SI HACE FALTA QUE LO CAMBIE EL USUARIO
                        var pD = "";
                        for(var u=0; u<codRols.length; u++) {
                            if (pDef[u] == "SI"){ pD = u;}
                        }
                        

                        if(document.forms[0].listaRol.value!="undefined"){
                            if( document.forms[0].listaRol.value==null ||  document.forms[0].listaRol.value.length==0){
                                document.forms[0].listaRol.value = codRols[pD] + '§¥';
                            }
                            else{
                                document.forms[0].listaRol.value +=  codRols[pD] + '§¥';
                            }
                        }

                        if(document.forms[0].listaMostrar.value!="undefined"){
                            if( document.forms[0].listaMostrar.value==null ||  document.forms[0].listaMostrar.value.length==0){
                                document.forms[0].listaMostrar.value = "true" + '§¥';
                            }
                            else{
                                document.forms[0].listaMostrar.value +=  "true" + '§¥';
                            }
                        }

                        if(document.forms[0].listaVersionTercero.value!="undefined"){
                            if( document.forms[0].listaVersionTercero.value==null ||  document.forms[0].listaVersionTercero.value.length==0){
                                document.forms[0].listaVersionTercero.value = datos[1] + '§¥';
                            }
                            else{
                                document.forms[0].listaVersionTercero.value +=  datos[1]+ '§¥';
                            }
                        }

                        insertarListaInteresados(datos[4],datos[5],datos[6],domicilio);

                        posInteresadoActual++;
                        updateMsgNumInteresados();
                        BUSQUEDA_INTERESADO_ACTIVA = false;

                        document.forms[0].codTipoDoc.value = datos[4];
                        document.forms[0].descTipoDoc.value = getDescTipoDoc(datos[4]);
                        document.forms[0].txtDNI.value = datos[5];

                        // Rol obligatorio al haber tercero
                        hacerComboRolObligatorio(true);
                        // Fin de tratamiento del rol

                        // Se almacenan en las correspondientes variables los datos del tercero, domicilio y rol actual
                        terceroActual = datos[2];
                        domicilioActual =  datos[13];
                        rolActual = codRols[pD];
                        comboRol.buscaCodigo(codRols[pD]);
                        var terAnt = getCodTerceroAnterior();
                        if(terAnt==-1)
                            deshabilitarImagenBoton("imgAnt",true);
                        else
                            deshabilitarImagenBoton("imgAnt",false);

                        // Se inserta el tercero por defecto
                        terceros = new Array();
                        insertarTerceroPorDefecto(datos);

                        if(getNumInteresados()==1){
                            mostrarOcultarIcono("botonListaDomicilios","on");
                            mostrarOcultarIcono("botonListaInteresados","off");
                            mostrarOcultarIcono("botonEliminarInteresado","on");
                        }

                        if(getNumInteresados()>1){
                            mostrarOcultarIcono("botonListaDomicilios","on");
                            mostrarOcultarIcono("botonListaInteresados","on");
                            mostrarOcultarIcono("botonEliminarInteresado","on");
                        }

                        actualizarNotificacion(<c:out value="${sessionScope.FichaExpedienteForm.checkNotifActivo}"/>);
                    }
                    pleaseWait1('off',this);
                });
            }
        }
    }

function actualizarNotificacion(valor){
	if(valor===true) valor = 1;
	else if(valor===false) valor = 0;

    listaNotificacionesElectronicas.push(valor);

    if(valor==0)
            document.forms[0].admiteNotificacion.checked =false;
    else
            document.forms[0].admiteNotificacion.checked =true;
}

/** Inserta los datos a mostrar de cada interesado para mostrarlos una vez que se recorre adelante y atras la
*  lista de interesados */
function insertarListaInteresados(tipodoc,doc,nombre,direccion){
  document.forms[0].titular.value = nombre;
    var pos = 0;
    var separador = "#";

    pos = listaInteresados.length;
    var dato = tipodoc + separador + doc  + separador + nombre + separador + direccion;
    listaInteresados[pos] = dato;                                                
}

/** Modifica los datos a mostrar de cada interesado para mostrarlos una vez que se recorre adelante y atras la
*  lista de interesados */
function modificarListaInteresados(tipodoc,doc,nombre,direccion,posicion){
    var separador = "#";

    var dato = tipodoc + separador + doc  + separador + nombre + separador + direccion;
    listaInteresados[posicion] = dato;
}

/** Elimina los elementos de una determinada posicion del array listaInteresados
*/
function deleteListaInteresados(pos){
    var pos = 0;
    var separador = "#";

    delete listaInteresados[pos];
}

function updateMsgNumInteresados() {
    var msg = posInteresadoActual + " de " + getNumInteresados();
    document.getElementById("msgInteresados").innerHTML = msg;
}

/** Comprueba si el codigo de un rol es un rol por defecto */
 function isRolDefecto(valorRol){
    var exito = false;
    var x=-1;

    for(i=0;i<codRols.length;i++){
            if(codRols[i]==valorRol)x=i;
    }
    if(x==-1)return exito;

    if(pDef[x]=="SI")
        exito = true;
    return exito;
}

function modificarRolInteresado()
{

    var separador = '§¥';
    var cRol = document.forms[0].codRol.value;
    var dRol = document.forms[0].descRol.value;
    var posTerceroActual = getPosTerceroActual();

    var LIST_ROL = new Array();
    LIST_ROL = document.forms[0].listaRol.value.split(separador);

    // Se recupera el rol actual
    rolActual = LIST_ROL[posTerceroActual];
    if(cRol!=rolActual && terceroActual != null){

        var roles = document.forms[0].listaRol.value;
        var mostrar = document.forms[0].listaMostrar.value;
        var lRoles = new Array();
        var lMostrar = new Array();
        var nuevaListaRoles ="";
        var nuevaListaMostrar ="";

        lRoles = roles.split(separador);
        lMostrar = mostrar.split(separador);

        for(j=0;j<lRoles.length;j++){
            if(j==posTerceroActual)
                nuevaListaRoles += cRol+ separador;
            else
                if(lRoles[j]!='' && lRoles[j].length>=1)
                    nuevaListaRoles += lRoles[j] + separador;
            }

            lRoles = nuevaListaRoles.split(separador);

            for(j=0;j<lMostrar.length;j++){
                if(j==posTerceroActual){
                    var sal = "false";

                    if(isRolDefecto(lRoles[j]))
                        sal = "true";
                    nuevaListaMostrar += sal+ separador;
                }
                else
                    if(lMostrar[j]!='' && lMostrar[j].length>=1)
                        nuevaListaMostrar += lMostrar[j] + separador;
                }

                document.forms[0].listaRol.value = nuevaListaRoles;
                document.forms[0].listaMostrar.value = nuevaListaMostrar;
                terceros[posTerceroActual][3] = dRol;
                terceros[posTerceroActual][7] = cRol;
                modificaVariableCambios(1);
            }

}

/*
* Devuelve la posicion del tercero actual n el array de terceros */
function getPosTerceroActual()
{
    var separador = '§¥';
    var lista = document.forms[0].listaCodTercero.value;
    var listTerceros = lista.split(separador);
    var pos = 0;

    for(i=0;i<listTerceros.length;i++)
        {
            if(listTerceros[i]!='' && terceroActual==listTerceros[i]){
                pos = i;
                break;
            }
        }// for
        return pos;
}

/*
* Devuelve el código de un tercero siguiente a uno dado. -1 si no hay  */
function getCodTerceroSiguiente(codigo){
    var separador = '§¥';
    var lista = document.forms[0].listaCodTercero.value;
    var listTerceros = lista.split(separador);
    var salida = -1;
    for(i=0;i<listTerceros.length;i++)
        {
            if(listTerceros[i]!='' && terceroActual==listTerceros[i]){
                if((i+1)>=listTerceros.length){
                    salida = -1;break;
                }
                else
                    {
                        if(listTerceros[i+1]!=''){
                            salida = listTerceros[i+1];break;
                        }
                    }
                }
            }// for
     return salida;
}

/*
* Devuelve el código de un tercero anterior a uno dado. -1 si no hay  */
function getCodTerceroAnterior(codigo)
{
    var separador = '§¥';
    var lista = document.forms[0].listaCodTercero.value;
    var listTerceros = lista.split(separador);
    var salida = -1;
    for(i=0;i<listTerceros.length;i++)
        {
            if(listTerceros[i]!='' && terceroActual==listTerceros[i]){
                if((i-1)>=0){
                    if(listTerceros[i-1]!=-1){
                        salida = listTerceros[i-1];break;
                    }
                }
                else{
                    salida = -1;break;
                }
            }
        }// for
        return salida;
    }

    /*
    * Devuelve la posición del tercero actual n el array de terceros */
    function getCodTercero(pos)
    {
        var separador = '§¥';
        var lista = document.forms[0].listaCodTercero.value;
        var listTerceros = lista.split(separador);
        return listTerceros[pos];
    }

function getNumInteresados() {
    var separador = '§¥';
    var lista = document.forms[0].listaCodTercero.value;
    var listTerceros = lista.split(separador);
    var contador = 0;

    for(i=0;i<listTerceros.length;i++) {
        if(listTerceros[i]!=''){
            contador++;
        }
    }// for
    return contador;
} 


// Para la posicion del tercero actual, se recupera el rol y la descripcion que le corresponde
function getDescRol(pos){
    var separador = '§¥';
    var listaRol = document.forms[0].listaRol.value.split(separador);
    var codRol  =0;
    var posRol = 0;
                                                                                
    // Se busca el rol del tercero en la lista de roles de interesados
    for(j=0;j<listaRol.length;j++){
        if(listaRol[j]!='' && j==pos){
            codRol = listaRol[j];
            break;
        }
    }

    // Se busca la descripcin del rol en el array de descripciones de roles
    // Se obtiene la posicion del codigo de rol del array de codigos de rol
    for(i=0;i<codRols.length;i++){
        if(codRols[i]!='' && codRols[i]==codRol){
            posRol = i;
            break;
        }
    }

    var salida = new Array();
    salida[0]=codRol;
    salida[1]=descRols[posRol];
    return salida;
}

   /**  Carga el tercero cuya posicion se indica */
  function cargarTerceroPosicion(pos) {
      var num = getNumInteresados();
      var separador = '§¥';

      var inter = listaInteresados[pos];
      var linter = new Array();
      linter = inter.split("#");
      document.forms[0].titular.value = linter[2];
      document.forms[0].domicilio.value = linter[3];
      document.forms[0].codTipoDoc.value = linter[0];
      document.forms[0].descTipoDoc.value = getDescTipoDoc(linter[0]);
      document.forms[0].txtDNI.value = linter[1];
      terceroActual = getCodTercero(pos);
      posInteresadoActual= pos + 1;
      updateMsgNumInteresados();

      // Se fija el rol
      var descript = new Array();
      descript = getDescRol(pos);
      comboRol.buscaCodigo(descript[0]);

      var terSig = getCodTerceroSiguiente();
      var terAnt = getCodTerceroAnterior();

      if(terAnt==-1)
          deshabilitarImagenBoton("imgAnt",true);
      else
          deshabilitarImagenBoton("imgAnt",false);

      if(terSig==-1)
          deshabilitarImagenBoton("imgNext",true);
      else
          deshabilitarImagenBoton("imgNext",false);
  }

/** Elimina el tercero que actualmente est seleccionado */
function eliminarTerceroActual() {
    var separador = '§¥';
    
    if(jsp_alerta("C","¿Desea eliminar este interesado?")==1){  
            if(terceroActual!=null && terceroActual.length>=1){
                modificaVariableCambios(1);
                var posTerceroActual = getPosTerceroActual(terceroActual);
                var sigTerActual = getCodTerceroSiguiente(terceroActual);
                var antTerActual = getCodTerceroAnterior(terceroActual);
                var listaTerceros = document.forms[0].listaCodTercero.value;
                var listaDomicilio = document.forms[0].listaCodDomicilio.value
                var listaVersion = document.forms[0].listaVersionTercero.value;
                var listaMostrar = document.forms[0].listaMostrar.value
                var listaRol = document.forms[0].listaRol.value;

                var LCodTerceros   = new Array();
                var LCodDomicilios = new Array();
                var LVersion       = new Array();
                var LMostrar       = new Array();
                var LRol           = new Array();
                var LNotificaciones= new Array();
                var nuevosTerceros   = "";
                var nuevosDomicilios = "";
                var nuevosVersion    = "";
                var nuevosMostrar    = "";
                var nuevosRol        = "";

                LCodTerceros   = listaTerceros.split(separador);
                LCodDomicilios = listaDomicilio.split(separador);
                LVersion       = listaVersion.split(separador);
                LMostrar       = listaMostrar.split(separador);
                LRol           = listaRol.split(separador);

                for(i=0;i<LCodTerceros.length;i++) {
                    if(LCodTerceros[i]!='' && i!=posTerceroActual)
                        nuevosTerceros += LCodTerceros[i] + separador;
                }

                for(i=0;i<LCodDomicilios.length;i++) {
                    if(LCodDomicilios[i]!='' && i!=posTerceroActual)
                        nuevosDomicilios += LCodDomicilios[i] + separador;
                }

                for(i=0;i<LVersion.length;i++) {
                    if(LVersion[i]!='' && i!=posTerceroActual)
                        nuevosVersion += LVersion[i] + separador;
                }

                for(i=0;i<LRol.length;i++){
                        if(LRol[i]!='' && i!=posTerceroActual)
                            nuevosRol += LRol[i] + separador;
                }

                for(i=0;i<LMostrar.length;i++){
                        if(LMostrar[i]!='' && i!=posTerceroActual)
                            nuevosMostrar += LMostrar[i] + separador;
                }


                var notifActual = listaNotificacionesElectronicas[posTerceroActual];
                document.forms[0].admiteNotificacion.checked = false;

                actualizarArrayGlobalListaNotificacionesElectronicas(posTerceroActual);

                agregaListaNotif();

                document.forms[0].titular.value = '';
                document.forms[0].domicilio.value = '';
                document.forms[0].codTipoDoc.value = '';
                document.forms[0].descTipoDoc.value = '';
                document.forms[0].txtDNI.value = '';
                document.forms[0].codRol.value = '';
                document.forms[0].descRol.value = '';

                document.forms[0].listaCodTercero.value = nuevosTerceros;
                document.forms[0].listaCodDomicilio.value = nuevosDomicilios;
                document.forms[0].listaVersionTercero.value = nuevosVersion;
                document.forms[0].listaRol.value = nuevosRol;
                document.forms[0].listaMostrar.value = nuevosMostrar;


                // Se modifica el tercero
                var nuevosTerceros = new Array();
                var nuevaListaInteresados = new Array();
                var j=0;
                for (var i=0; i< terceros.length; i++){
                    if (i!=posTerceroActual) {
                        nuevosTerceros[j] = terceros[i];
                        nuevaListaInteresados[j] = listaInteresados[i];
                        j++;
                    }
                }
                terceros = nuevosTerceros;
                listaInteresados = nuevaListaInteresados;

                if(document.forms[0].listaCodTercero.value.length!='') {
                    if(sigTerActual!='' && sigTerActual!=-1){
                        terceroActual = sigTerActual;
                    }else if(antTerActual!='' && antTerActual!=-1) {
                                terceroActual = antTerActual;
                    }

                    var pos = getPosTerceroActual();
                    var inter = listaInteresados[pos];
                    var linter = new Array();
                    linter = inter.split("#");
                    document.forms[0].titular.value = linter[2];
                    document.forms[0].domicilio.value = linter[3];
                    document.forms[0].codTipoDoc.value = linter[0];
                    document.forms[0].descTipoDoc.value = getDescTipoDoc(linter[0]);
                    document.forms[0].txtDNI.value = linter[1];
                    terceroActual = getCodTercero(pos);
                    posInteresadoActual= pos +1;
                    var descript = new Array();
                    descript = getDescRol(pos);
                    document.forms[0].codRol.value = descript[0];
                    document.forms[0].descRol.value = descript[1];

                    updateMsgNumInteresados();

                    modificarListaInteresados(linter[0],linter[1],linter[2], linter[3],pos);

                    var antTerActual = getCodTerceroAnterior(terceroActual);
                    var sigTerActual = getCodTerceroSiguiente(terceroActual);

                    if(antTerActual==-1)
                        deshabilitarImagenBoton("imgAnt",true);
                    else
                        deshabilitarImagenBoton("imgAnt",false);

                    if(sigTerActual==-1)
                        deshabilitarImagenBoton("imgNext",true);
                    else
                        deshabilitarImagenBoton("imgNext",false);

                    var num = getNumInteresados();
                    if(num==1){
                        mostrarOcultarIcono("botonListaDomicilios","on");
                        mostrarOcultarIcono("botonListaInteresados","off");
                        mostrarOcultarIcono("botonEliminarInteresado","on");
                    }

                    if(num>1){
                        mostrarOcultarIcono("botonListaDomicilios","on");
                        mostrarOcultarIcono("botonListaInteresados","on");
                        mostrarOcultarIcono("botonEliminarInteresado","on");
                    }

                    if(listaNotificacionesElectronicas!=null && listaNotificacionesElectronicas.length>0){
                            if(listaNotificacionesElectronicas[pos]==0){
                                    document.forms[0].admiteNotificacion.checked = false;
                            }else
                                    document.forms[0].admiteNotificacion.checked = true;
                    }//if
                    
                     if(checkNotificacionesHabilitado){	
                        $('#admiteNotificacion').prop('disabled',false);	
                    }else{	
                        $('#admiteNotificacion').prop('disabled',true);	
                    }
                }
                else{
                    document.forms[0].titular.value = "";
                    document.forms[0].domicilio.value = "";
                    document.forms[0].codTipoDoc.value = "";
                    document.forms[0].descTipoDoc.value = "";
                    document.forms[0].txtDNI.value = "";
                    document.forms[0].codRol.value= "";
                    document.forms[0].descRol.value= "";
                    document.forms[0].listaCodTercero.value= "";
                    document.forms[0].listaCodDomicilio.value= "";
                    document.forms[0].listaVersionTercero.value = nuevosVersion;
                    document.forms[0].listaRol.value= "";
                    document.forms[0].listaMostrar.value= "";
                    terceroActual = null;

                    // Rol no obligatorio al no haber tercero
                    hacerComboRolObligatorio(false);

                    deshabilitarImagenBoton("imgAnt",true);
                    deshabilitarImagenBoton("imgNext",true);

                    mostrarOcultarIcono("botonListaDomicilios","off");
                    mostrarOcultarIcono("botonListaInteresados","off");
                    mostrarOcultarIcono("botonEliminarInteresado","off");
                    posInteresadoActual = 0;

                    BUSQUEDA_INTERESADO_ACTIVA = true;
                }

                updateMsgNumInteresados();
            }
            else jspAlerta("A","No hay un interesado seleccionado");
        }
}


    /**
     * Se actualiza el array listaNotificacionesElectronicas eliminando una posición determinada
     */
    function actualizarArrayGlobalListaNotificacionesElectronicas(posicion){

        listaNotificacionesElectronicas.splice(posicion,1);
        numInteresadosAdmitenNotifElect=0;
        existenInteresadosNotifElectronica=0;
        for(var indexInt=0;indexInt<listaNotificacionesElectronicas.length;indexInt++){
            if( listaNotificacionesElectronicas[indexInt]=="1") numInteresadosAdmitenNotifElect++;
        }
        // Hay algun interesado que admite notificaciones
        if(numInteresadosAdmitenNotifElect>0) existenInteresadosNotifElectronica=1;
        desactivarAdmiteNotif()
    }

function hacerComboRolObligatorio(valor) {
    if (valor) {
        document.getElementById('codRol').className = 'inputTextoObligatorio';
        document.getElementById('descRol').className = 'inputTextoObligatorio';
    } else {
        document.getElementById('codRol').className = 'inputTexto';
        document.getElementById('descRol').className = 'inputTexto';
    }
    // Es necesario recargar
    var codigoActual = document.forms[0].codRol.value;
    comboRol.addItems(codRols, descRols);
    comboRol.buscaCodigo(codigoActual);
}

function pulsarListaTerceros() {
    // Abrimos dialogo                                                                                                               
    var source = "<%=request.getContextPath()%>/jsp/registro/listadoInteresados.jsp?opcion=";

    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,terceros,"width=955,height=500,status="+ "<%=statusBar%>",function (indiceNuevo) {
        if (indiceNuevo!=undefined) {
            var separador = '§¥';                                                                                                                
            var listaCods = new Array();
            listaCods = document.forms[0].listaCodTercero.value.split(separador);
            var codTercero = listaCods[indiceNuevo];
            var pos = getPosTerceroActual();

            var inter = listaInteresados[indiceNuevo];
            var linter = new Array();
            linter = inter.split("#");
            document.forms[0].titular.value = linter[2];
            document.forms[0].domicilio.value = linter[3];
            document.forms[0].codTipoDoc.value = linter[0];
            document.forms[0].descTipoDoc.value = getDescTipoDoc(linter[0]);
            document.forms[0].txtDNI.value = linter[1];
            terceroActual = codTercero;
            posInteresadoActual= getPosTerceroActual()+1;

            var descript = new Array();
            descript = getDescRol(indiceNuevo);
            comboRol.buscaCodigo(descript[0]);

            updateMsgNumInteresados();

            var terSig = getCodTerceroSiguiente();
            var terAnt = getCodTerceroAnterior();

            if(terAnt==-1)
                    deshabilitarImagenBoton("imgAnt",true);
            else
                    deshabilitarImagenBoton("imgAnt",false);

            if(terSig==-1)
                    deshabilitarImagenBoton("imgNext",true);
            else
                    deshabilitarImagenBoton("imgNext",false);

            modificarListaInteresados(linter[0],linter[1],linter[2], linter[3],indiceNuevo);
        }
    });
}


	/********************************************************************/
	/* FUNCION INSERTARTERCEROPORDEFECTO                                */
	/********************************************************************/
	/**
	* Mete el tercero devuelto en 'datos' en la lista de terceros, con el rol
	* por defecto. Si el tercero ya existe se sobreescriben sus datos con los
	* devueltos. Si no existe en la lista de terceros, se inserta en el lugar
	* del primer tercero con rol por defecto.
	*/
	function insertarTerceroPorDefecto(datos) { 	
            /** Habilitamos el check de notificaciones Electronicas en caso de que 	
            *  usuario tramitador disponga de permiso para modificar la marca, 	
            *  en caso contrario este permanece bloqueado	
            */	
           if(checkNotificacionesHabilitado){	
                $('#admiteNotificacion').prop('disabled',false);	
           }else{	
                $('#admiteNotificacion').prop('disabled',true);	
           }

		// Determinamos en que posicion se insertara el tercero
		var pos = determinarPosicionInsercion(datos[0]);                                                                                                                    
		// Determinamos si es una nueva entrada en la lista, en tal caso
		// se usa el rol por defecto, si no se conserva el rol actual.
		var esNuevo = (terceros[pos].length == 0);

		// Introducimos el nuevo tercero con rol por defecto 
		terceros[pos][0]  = datos[0];
	   
		
		terceros[pos][1]  = datos[3];
		terceros[pos][2]  = document.forms[0].titular.value;
		
		if (esNuevo) {
			terceros[pos][3]  = descRolPorDefecto;
		}
		
		terceros[pos][5]  = document.forms[0].domicilio.value;
		if (esNuevo) {
			terceros[pos][6]  = "SI";
			terceros[pos][7]  = rolPorDefecto;
		}
		
		terceros[pos][15] = document.forms[0].txtDNI.value;
																															
	}

	/**
	* Determina la posicion en el array de terceros en la que hay que introducir
	* el nuevo tercero. Si ya existe se toma su posicion actual. Si
	* no existe se toma la posicion posterior a la ultima del array.
	*/
	function determinarPosicionInsercion(codigoTercero) {
		// Comprobamos que no exista ya
		var pos = existeTercero(codigoTercero);

		if (pos == -1) { // No existe
			pos = terceros.length;
			terceros[pos] = new Array(); // Hacemos sitio para el nuevo
		}

		return pos;
	}


	/**
	* Mira en la lista de terceros si existe el tercero con el codigo pasado y en ese
	* caso devuelve su posicion en la lista, si no existe devuelve -1.
	*/
	function existeTercero(codigo) {
		for(i=0; i<terceros.length; i++) if (terceros[i][0] == codigo) return i;
		return -1;
	}


function pulsarListaDomicilios() {
        // Se cargan los datos del tercero actual para obtener los domicilios
        document.forms[0].opcion.value="buscar_por_id_doms";
        document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do?codTerc=" + terceroActual;
        document.forms[0].target="oculto";
        document.forms[0].submit();
        pleaseWait1('on',this);
}

function mostrarListaDomicilios(Terceros) { //oculto
        pleaseWait1('off',this);
        // Abrimos dialogo
        var argumentos = new Array();
        argumentos['domicilios'] =  Terceros[0][18];
        var source = "<%=request.getContextPath()%>/jsp/sge/listaDomicilios.jsp?opcion=";

        abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source,
            argumentos,"width=900,height=500,status="+ "<%=statusBar%>",function (codigoNuevoDom) {
            if (codigoNuevoDom!=undefined) {
                var domicilios = Terceros[0][18];
                var separador = '§¥';
                for(var i = 0; i<domicilios.length; i++) {
                    if (domicilios[i][5] == codigoNuevoDom[0]) {
                            var tercero = formatearArrayTercero(Terceros[0], i);
                            insertarTerceroPorDefecto(tercero[0]);
                            var posTerceroActual = getPosTerceroActual();

                            var listaDom = new Array();
                            var nuevosDomicilios="";

                            listaDom = document.forms[0].listaCodDomicilio.value.split(separador);
                            for(j=0;j<listaDom.length;j++)
                                    {
                                            if(j==posTerceroActual && listaDom[posTerceroActual]!='')
                                                    nuevosDomicilios = nuevosDomicilios + codigoNuevoDom[0] + separador;
                                            else
                                                    if(listaDom[j]!='')
                                                            nuevosDomicilios = nuevosDomicilios + listaDom[j] + separador;

                                            }
                                            document.forms[0].listaCodDomicilio.value = nuevosDomicilios;

                                            var tipoDoc = Terceros[0][2];
                                            var doc = Terceros[0][3];
                                            var nombre = Terceros[0][4];
                                            var apel1 = Terceros[0][5];
                                            var apel2 = Terceros[0][6];
                                            var nombreCompleto ="";
                                            if(apel2.length>0 || apel1.length>0) {
                                                if (apel2.length > 0) apel2 = " " + apel2;
                                                nombreCompleto = apel1 + apel2 + ", " + nombre;
                                            } else {
                                                nombreCompleto = nombre;
                                            }

                                            var domicilio = codigoNuevoDom[1];
                                            document.forms[0].titular.value = nombreCompleto;
                                            document.forms[0].domicilio.value = domicilio;
                                            modificarListaInteresados(tipoDoc,doc,nombreCompleto,domicilio,posTerceroActual);
                                    }
                            }
                    }
    });
}

function mostrarOcultarIcono(nombre,flag){
        if(flag=="on")
                document.getElementById(nombre).style.visibility='inherit';
        else
                document.getElementById(nombre).style.visibility='hidden';
}

function modificaVariableCambios(valor)
{
    if((document.forms[0].existenCambiosSinGrabar)!='undefined') document.forms[0].existenCambiosSinGrabar.value=valor;
    if(valor==0)
    {
    if(( document.forms[0].existenCambiosSinGrabarCampos)!='undefined') document.forms[0].existenCambiosSinGrabarCampos.value=0;
    }

}
function obtenerVariableCambios()
{
    if((document.forms[0].existenCambiosSinGrabar)!='undefined')return document.forms[0].existenCambiosSinGrabar.value;
    else return 0;
}
function modificaVariableCambiosCamposSupl()
{
    if((document.forms[0].existenCambiosSinGrabar)!='undefined') document.forms[0].existenCambiosSinGrabar.value=1;
    if((document.forms[0].existenCambiosSinGrabarCampos)!='undefined')document.forms[0].existenCambiosSinGrabarCampos.value=1;

}


function pulsarAltaAdjuntoDocumento(){ 
    var posicion = tabDocumentos.selectedIndex;
    if(posicion==-1){
        jsp_alerta('A','<%=descriptor.getDescripcion("msgDocumentoNoSeleccionado")%>');
    }else if((getNumInteresadosExpediente() == 0) && (interesado_Obligatorio()) == "1"){ 
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjInterOblig")%>');
    }else{ 
        var codigoDocumento  = listaDocumentosOriginal[posicion][0];
        var codMunicipio  	 = document.forms[0].codMunicipio.value;
        var ejercicio     	 = document.forms[0].ejercicio.value;
        var numExpediente 	 = document.forms[0].numExpediente.value;
        var codProcedimiento = document.forms[0].codProcedimiento.value;
        var codUnidadOrganicaExp = document.forms[0].codUnidadOrganicaExp.value;		
        var nombreAdjunto = listaDocumentos[posicion][3];
        var tipoMime	  = listaDocumentos[posicion][4];
        var fechaAdjunto  = listaDocumentos[posicion][5];
        var parametros = "";
        var continuar = false;
        // Si no existe un adjunto es que se da de alta
        if(nombreAdjunto.length==0 && tipoMime.length==0 && fechaAdjunto.length==0){
            parametros = "?opcion=mostrarAlta&codigoDocumento=" + codigoDocumento + "&codMunicipio=" + codMunicipio +
                         "&ejercicio=" + ejercicio + "&numExpediente=" + numExpediente + "&codProcedimiento=" + codProcedimiento + "&codUnidadOrganicaExp=" + codUnidadOrganicaExp;
            continuar = true;
        }else{ // Si existe un adjunto es que se modifica su contenido
            if ("<%=descriptor.getDescripcion("gEtiq_estadoFirmaN")%>"==listaDocumentos[posicion][6]) {  //Si no requiere firma
                parametros = "?opcion=mostrarModificacion&codigoDocumento=" + codigoDocumento + "&codMunicipio=" + codMunicipio +
                             "&ejercicio=" + ejercicio + "&numExpediente=" + numExpediente + "&codProcedimiento=" + codProcedimiento + "&nombreOriginalAdjunto=" + nombreAdjunto+ "&codUnidadOrganicaExp=" + codUnidadOrganicaExp;
                if(jsp_alerta("C",'<%=descriptor.getDescripcion("msgModDocAdjuntoAsociado")%>')==1){
                        continuar = true;
                }
            } else jsp_alerta("A",'<%=descriptor.getDescripcion("msgNoModifDocCircuito")%>');
        }

        if(continuar){
            var source = "<html:rewrite page='/expediente/FichaExpedienteDocumentosPresentados.do'/>" + parametros;
            abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source="+source,
                'ventana1','width=600,height=500,scrollbar=no,status='+ '<%=statusBar%>', 
                function(ventana){
                    if(ventana!=null){
                        listaDocumentos = JSON.parse(ventana);
                        tabDocumentos.lineas=listaDocumentos;
                        tabDocumentos.displayTabla();
                    }
                });
        }// if
    }
}

function pulsarVerFirmasAdjuntoDocumento() {
  var posicion = tabDocumentos.selectedIndex;
  if(posicion!=-1){

    if (""!=listaDocumentos[posicion][6]) {
      if ("<%=descriptor.getDescripcion("gEtiq_estadoFirmaN")%>"!=listaDocumentos[posicion][6]) {
        var codDocumentoAdjunto = listaDocumentos[posicion][7]; //el codigo unico del documento adjunto
        var source = "<c:url value='/expediente/FichaExpedienteDocumentosPresentados.do?opcion=verFirmas'/>" +
                     "&codDocumentoAdjunto="+codDocumentoAdjunto;
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,
            [],'width=800,height=400,status='+ '<%=statusBar%>',function(){});
         
      } else jsp_alerta('A', '<%=descriptor.getDescripcion("msgDocumentoNoRequiereFirma")%>');
    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msgDocumentoNoAdjunto")%>');
  } else jsp_alerta('A', '<%=descriptor.getDescripcion("msgDocumentoNoSeleccionado")%>');
}

function verDocumentoExpediente(){
    var posicion                = tabDocumentos.selectedIndex;
    if(posicion!=-1){
        var codigoDocumento   = listaDocumentosOriginal[posicion][0];
        var codMunicipio      = document.forms[0].codMunicipio.value;
        var ejercicio         = document.forms[0].ejercicio.value;
        var numExpediente 	  = document.forms[0].numExpediente.value;
        var codProcedimiento  = document.forms[0].codProcedimiento.value;
        var nombreAdjunto = listaDocumentos[posicion][3];
        var tipoMime	  = listaDocumentos[posicion][4];
        var fechaAdjunto  = listaDocumentos[posicion][5];

        if(nombreAdjunto.length>0 && tipoMime.length>0 && fechaAdjunto.length>0){
            var parametros = "?codigoDocumento=" + codigoDocumento + "&codMunicipio=" + codMunicipio +
                "&ejercicio=" + ejercicio + "&numExpediente=" + numExpediente + "&codProcedimiento=" + codProcedimiento;

            window.open("<%=request.getContextPath()%>/VerDocumentoPresentado" + parametros, "ventana1",
                                        "left=10, top=10, width=650, height=500, scrollbars=no, menubar=no, location=no, resizable=yes");
        }else
            jsp_alerta('A', '<%=descriptor.getDescripcion("msgEliminarDocExpNoPosible")%>');
   }
   else 
       jsp_alerta('A', '<%=descriptor.getDescripcion("msgDocumentoNoSeleccionado")%>');
}

function agregaListaNotif(){
    var contador = 0;
    document.forms[0].listaNotificacionesElectronicas.value = '';

    while (contador<listaNotificacionesElectronicas.length) {
            document.forms[0].listaNotificacionesElectronicas.value +=  listaNotificacionesElectronicas[contador]+ '§¥';
            contador++;
    }
}

function onClickAdmiteNotificacion(){
    if(checkNotificacionesHabilitado){        
	if(listaInteresados.length>0){
            var valor = document.forms[0].admiteNotificacion.checked;
            var posTercActual = getPosTerceroActual();
            var notificacion  = listaNotificacionesElectronicas[posTercActual];

            if(notificacion==0 && valor==true){
                    listaNotificacionesElectronicas[posTercActual] = 1 ;
                    numInteresadosAdmitenNotifElect++;
                    modificaVariableCambios(1);

            }else
            if(notificacion==1 && valor==false){
                    listaNotificacionesElectronicas[posTercActual] = 0;
                    numInteresadosAdmitenNotifElect--;
                    modificaVariableCambios(1);
            }
            //actualizamos valor de variable existe algun interesado que admite e-notif
            if(numInteresadosAdmitenNotifElect>0) existenInteresadosNotifElectronica=1;
            else existenInteresadosNotifElectronica=0;
	}
    } else {        
        if($("input[name=admiteNotificacion]").is(':checked')) {  
            $("input[name=admiteNotificacion]").removeAttr("checked");
        } else {  
            $("input[name=admiteNotificacion]").attr('checked','checked');
        } 
        return false;
    }
}


function pulsarCopiarExpediente()
{
    if ((getNumInteresadosExpediente()== 0) && (interesado_Obligatorio() == "1")){
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjInterOblig")%>');
    }else{
        if(jsp_alerta("C",'<%=descriptor.getDescripcion("msgCopiarExpediente")%>')==1)
        {
            if((document.forms[0].existenCambiosSinGrabar)!='undefined')irAlAction=irActionConCambios(document.forms[0].existenCambiosSinGrabar.value,mensajeGuardarCambios);
            if(irAlAction==1){
            document.forms[0].opcion.value="copiarExpediente";
            document.forms[0].target="oculto";
            document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
            document.forms[0].submit();
            }

        }
    }
}
function recibirNumero(datos) {
         if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjIniExp1")%>' + " " + datos+ " " +'<%=descriptor.getDescripcion("msgTramitarCopiaExpediente")%>')==1){
            document.forms[0].numero.value = datos;
            document.forms[0].ejercicio.value = datos.substring(0,4);
            document.forms[0].opcion.value="cargar";
            document.forms[0].target="mainFrame";
            document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
            document.forms[0].submit();
    }
}



    function cargarNotificacion(){        
        var index = tabNotificaciones.selectedIndex;		
        if(index!=-1){		
            var codNotificacion = listaNotificaciones[index][0];
            var numRegistro =  listaNotificaciones[index][4];
            var parametros = "?opcion=detalleNotificacion&codNotificacion=" + codNotificacion + "&numRegistro=" + numRegistro;
            var url = "<%=request.getContextPath()%>/Notificacion.do" + parametros;
            abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source=" + url,
                null,'width=950,height=560,scrollbars=no,status=no',function(){});
        }else
            jsp_alerta("A","<%=descriptor.getDescripcion("msgErrSelectNotif")%>");
    }


    function mostrarMensajeAnulacionExpediente(){        
        jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorAnularExpediente")%>");
    }


    function mostrarMsgErrorVerificacionAnulacionExpediente(){
        jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorVerificarAnulacion")%>");
    }
	
    function habilitarBotonRetroceso(){
    	document.forms[0].cmdRetrocederExpediente.disabled = false;
        cargadoOtrosDatos=false;
        cargadoCamposSuplementarios=false;
    }
        
    // Cuando se cargue toda la página, se llama a esta función JQUERY
    $(function(){     

        // Manejador del evento onclick sobre la pestaña "Datos Suplementarios"
        $("#pestana2").on('click',function(){                                  
           if(!estanCargadosCamposSuplementarios()){
               cargarCamposSuplementarios();
           }
        });
        
        // Manejador del evento onclick sobre la pestaña "Otros documentos"
        $("#pestana8").on('click',function(){          
           if(!cargadoOtrosDocumentos){
                var codMunicipio  = $("#codMunicipio").val();
                var ejercicio     = $("#ejercicio").val();
                var numExpediente = $("#numero").val();
                pleaseWaitSinFrame('on');
                try{
                     $.ajax({
                         url: '<c:url value="/expediente/cargaFicha.do"/>',
                         type: 'POST',
                         async: true,
                          data: 'codMunicipio=' + codMunicipio + '&ejercicio=' + ejercicio + '&numero=' + numExpediente 
                                + '&opcion=cargarDocumentosExternosExpediente' + '&expHistorico=' + '<%=expForm.isExpHistorico()%>',
                         
                         success: procesarRespuestaListadoDocumentosExternos,
                         error: muestraErrorRespuestaListadoDocumentosExternos
                     });           
                }catch(Err){
                    pleaseWaitSinFrame('off');
                    mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CARGAR_LISTADO_DOCUMENTOS_EXTERNOS);
                } 
          }           
        });
        
        
        // Manejador del evento onclick sobre la pestaña "Comunicaciones"
        $("#pestana9").on('click',function(){           
           if(!cargadoComunicaciones){
                var numExpediente = $("#numero").val();
                pleaseWait1('on',this);
                try{
                     $.ajax({
                         url:  '<c:url value="/expediente/cargaFicha.do"/>',
                         type: 'POST',
                         async: true,
                         data: 'numero=' + numExpediente + '&opcion=cargarComunicacionesExpediente'
                                + '&expHistorico=' + '<%=expForm.isExpHistorico()%>',
                         success: procesarRespuestaListadoComunicacionesExpediente,
                         error: muestraErrorRespuestaListadoComunicacionesExpediente
                     });           
                }catch(Err){
                    pleaseWait1('off',this);
                    mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CARGAR_LISTADO_COMUNICACIONES_ELECTRONICAS);
                } 
           }
        });
        
        
        // Manejador del evento onclick sobre la pestaña "Notificaciones"
        $("#pestana900").on('click',function(){                      
           if(!cargadoNotificaciones){
                pleaseWaitSinFrame('on');
                var codMunicipio  = $("#codMunicipio").val();
                var ejercicio     = $("#ejercicio").val();
                var numExpediente = $("#numero").val();
                var tramiteNotificado = (tabTramitesNotif==null)?"0":"1";
				marcarExpedienteNotifTelematica();

                try{
                     $.ajax({
                         url: '<c:url value="/expediente/cargaFicha.do"/>',
                         type: 'POST',
                         async: true,
                         data: 'numero=' + numExpediente + '&opcion=cargarNotificacionesExpediente'
                                + '&expHistorico=' + '<%=expForm.isExpHistorico()%>' + '&tramiteNotificado=' + tramiteNotificado,
                         success: procesarRespuestaListadoNotificacionesExpediente,
                         error: muestraErrorRespuestaListadoNotificacionesExpediente
                     });
                     
                }catch(Err){
                    pleaseWaitSinFrame('off');
                    mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CARGAR_LISTADO_DOCUMENTOS_EXTERNOS);
                }  
            }
        });         

        
        // Manejador del evento onclick sobre la pestaña "Otros datos"
        $("#pestana3").on('click',function(){                      
           if(!cargadoOtrosDatos){
                pleaseWaitSinFrame('on');           
                var codProcedimiento =$("#codProcedimiento").val();
                var numExpediente = $("#numero").val();
                var ejercicio     = $("#ejercicio").val();

                try{              
                     $.ajax({
                         url: '<c:url value="/expediente/cargaFicha.do"/>',
                         type: 'POST',
                         async: true,
                         data: 'numero=' + numExpediente + '&codProcedimiento=' + codProcedimiento + '&ejercicio=' + ejercicio + 
                            '&opcion=cargarOtrosDatos' + '&expHistorico=' + '<%=expForm.isExpHistorico()%>',
                         success: procesarRespuestaOtrosDatosExpediente,
                         error: muestraErrorRespuestaOtrosDatosExpediente
                     });           
                }catch(Err){
                    pleaseWaitSinFrame('off');
                    mostrarMensajeErrorCargarParcialExpediente(TIPO_ERROR_CARGAR_LISTADO_OTROS_DATOS_EXPEDIENTE);
                }  
           }
        });   
    });
     
   /** Devuelve la descripción correspondiente a un determinado estado para una comunicación
       asociada a un expediente */
   function descripcionComunicacionLeida(flag){                
        return flag?'<%=descriptor.getDescripcion("etiq_Si")%>':'<%=descriptor.getDescripcion("etiq_No")%>';
   }
    
   /** En base a estado cuyo valor es numérico, esta función devuelve el texto obtenido de la variable de 
        idioma correspondiente */
    function descripcionEstadoNotificacion(estado){
        var descripcion = "";        
        switch(parseInt(estado)){
            case 0:
                descripcion = '<%=descriptor.getDescripcion("etiqEstadoNoLeida")%>';
                break;                
            case 1:
                descripcion = '<%=descriptor.getDescripcion("etiqEstadoLeida")%>';
                break;    
            case 2:
                descripcion = '<%=descriptor.getDescripcion("etiqEstadoRehusada")%>';
                break;        
            case 3:
                descripcion = '<%=descriptor.getDescripcion("etiqEstadoRechazo")%>';
                break;
            case 4:
                descripcion = '<%=descriptor.getDescripcion("etiqEstadoLeidaRechazada")%>';
                break; 
            default:
                descripcion = "";
                break;            
        }
        return descripcion;
    }
    
   function descripcionEstadoFirmaDocumentoInicio(estado){
        var descripcion = "";    
        try{
        switch(estado){
            case 'N':
                descripcion = '<%=descriptor.getDescripcion("gEtiq_estadoFirmaN")%>';
                break;                
            case 'O':
                descripcion = '<%=descriptor.getDescripcion("gEtiq_estadoFirmaP")%>';
                break;    
            case 'S':
                descripcion = '<%=descriptor.getDescripcion("gEtiq_estadoSubsanar")%>';
                break;        
            case 'F':
                descripcion = '<%=descriptor.getDescripcion("gEtiq_estadoFirmaF")%>';
                break;   
            case 'R':
                descripcion = '<%=descriptor.getDescripcion("gEtiq_estadoFirmaR")%>';
                break;    
            case 'X':
                descripcion = '<%=descriptor.getDescripcion("gEtiq_estadoFirmaX")%>';
                break;        
            default:
                descripcion = "";
                break;            
        }
        }catch(Err){
            alert("Error descripcionEstadoFirmaDocumentoInicio: " + Err.description);
        }
        return descripcion;
    }
    
    function getEnlaceVerificarFirmaDocumentoInicio(codDocumentoAdjuntoExpediente,codigoDocumento){                        
        return "<a href=\"javascript:verificarFirmaDoc('" + codDocumentoAdjuntoExpediente + "','"+ codigoDocumento + "');\">" + "<%=descriptor.getDescripcion("linkVerificarFirma")%>" + "</a>";
    }  
        


    /**
      * Muestra un mensaje de error que viene determinado por un variable de tipo númerico que se pasa como 
      * parámetro
      * @param: tipo ==> Este valor se obtiene de variables globales definidas en fichaExpediente.js, como 
      *                  TIPO_ERROR_ELIMINAR_DOCUMENTO_EXTERNO, ...
      */
    function mostrarMensajeErrorCargarParcialExpediente(tipo){            
        pleaseWaitSinFrame('off');
        switch(tipo) {
            case TIPO_ERROR_ELIMINAR_DOCUMENTO_EXTERNO:
                jsp_alerta('A','<%=descriptor.getDescripcion("errorEliminarDocExterno")%>');
                break;                
            case TIPO_ERROR_CARGAR_LISTADO_DOCUMENTOS_EXTERNOS:
                jsp_alerta('A','<%=descriptor.getDescripcion("errorGetListadoDocsExternos")%>');
                break;                
            case TIPO_ERROR_CARGAR_LISTADO_NOTIFICACIONES_ELECTRONICAS:
                jsp_alerta('A','<%=descriptor.getDescripcion("errorListadoNotificaciones")%>');
                break;                     
            case TIPO_ERROR_CARGAR_LISTADO_COMUNICACIONES_ELECTRONICAS:
                jsp_alerta('A','<%=descriptor.getDescripcion("errorListadoComunicaciones")%>');
                break;                         
            case TIPO_ERROR_CARGAR_LISTADO_OTROS_DATOS_EXPEDIENTE:
                jsp_alerta('A','<%=descriptor.getDescripcion("errorListadoOtrosDatos")%>');
                break;                                             
            case TIPO_ERROR_CARGAR_LISTADO_DATOS_SUPLEMENTARIOS:
                jsp_alerta('A','<%=descriptor.getDescripcion("errorGetDatosSuplementarios")%>');
                break;            
            case TIPO_ERROR_ACTUALIZAR_CAMPOS_NUMERICOS_ALARMAS:
                jsp_alerta('A','<%=descriptor.getDescripcion("errEditCamposNumeroAlarmas")%>');
                break;            
            case TIPO_ERROR_TRAMITE_NOTIFICADO_NO_INICIADO:
                jsp_alerta('A','<%=descriptor.getDescripcion("msjErrTramiteNotifNoIni")%>');
                break; 
            case TIPO_ERROR_FILA_NO_SELECCIONADA:
                jsp_alerta('A', '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("msjNoSelecFila"))%>');
                break;
            case TIPO_ERROR_CREAR_CSV_DOCUMENTO:
                jsp_alerta('A','<%=descriptor.getDescripcion("errorCrearCSVDoc")%>');
                break;
            case TIPO_ERROR_CSV_DOCUMENTO_YA_EXISTE:
                jsp_alerta('A','<%=descriptor.getDescripcion("errorCSVDocYaExiste")%>');
                break;
            case TIPO_ERROR_CSV_DOCUMENTO_FORMATO_NO_SOPORTADO:
                jsp_alerta('A','<%=descriptor.getDescripcion("errorCSVFormatoNoSoportado")%>');
                break;
            case TIPO_ERROR_CSV_CODIGO_DESCONOCIDO:
                jsp_alerta('A','<%=descriptor.getDescripcion("errCodigoDesconocidoCampoSup")%>');
                break;
            case TIPO_OK_CSV_GENERADO_CORRECTO:
                jsp_alerta('A','<%=descriptor.getDescripcion("msjCSVGeneradoOK")%>'+'. '+'<%=descriptor.getDescripcion("msjExpGrabarAutomat")%>');
                pulsarGrabarGeneral();
                break;
            default:
                jsp_alerta('A','<%=descriptor.getDescripcion("errorGenericoParcialExpediente")%>');
                break;
        }
    }
    
    function mostrarMensajesErrorAsientosRegistroExternos(errores){        
        var msjErrores = new Array();
        for(var i=0;errores!=null && i<errores.length;i++){
            msjErrores[i] = '<fmt:message key="BusquedaTercero/falloBusqueda"/>' + ' ' + errores[i];
        } 
        if(msjErrores.length>0)
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/listadoErrores.jsp',msjErrores,
                    'width=420,height=300,status=no',function(){});

    }
    
    
   function mostrarVentana(mensaje,title) {
       
       $('#popupTextoLargo').fadeIn('slow');
		$('.popup-overlay').fadeIn('slow');
		$('.popup-overlay').height($(window).height());
      domlay('capaTitulo',1,0,0,title);
      domlay('capaTextoLargo',1,0,0,mensaje);
                
   }
   
   function cerrarVentanaTextoLargo(){
       $('#popupTextoLargo').fadeOut('slow');
	$('.popup-overlay').fadeOut('slow');
	return false;
   }

   function pulsarListaOperacionesExpedientes() {
       // Abrimos dialogo
       var source = "<c:url value='/sge/FichaExpediente.do?opcion=cargarListaOperacionesExpediente'/>";	
       abrirXanelaAuxiliar(source,"aux",
            'width=950,height=450,status=' + '<%=statusBar%>',function(){});
   }
</script>

</head>
<body class="bandaBody" onload="pleaseWait1('on',this);inicializar();">
    <iframe name="oculto" src="about:blank" style="display:none"></iframe>
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>
<html:form action="/sge/FichaExpediente.do" method="post">
<html:hidden  property="opcion" value=""/>
<html:hidden  property="codDepartamento" value=""/>
<html:hidden  property="codUnidadRegistro" value=""/>
<html:hidden  property="tipoRegistro" value=""/>
<html:hidden  property="ejerNum" value=""/>
<html:hidden  property="codMunicipio" styleId="codMunicipio"/>
<html:hidden  property="codProcedimiento" styleId="codProcedimiento"/>
<html:hidden  property="codTramite" value=""/>
<html:hidden  property="ocurrenciaTramite" value=""/>
<html:hidden  property="pathExportZip" value=""/>
<html:hidden  property="ejercicio" styleId="ejercicio"/>        
<html:hidden  property="numero" styleId="numero"/>
<html:hidden  property="codUnidadOrganicaExp" />
<html:hidden property="origenServicio" value = ""/>
<html:hidden  property="bloqueo" value=""/>
<html:hidden property="codigoUnidadDestino" value = ""/>
<html:hidden  property="expHistorico" styleId="expHistorico"/>
<input type="hidden" name="codUnidadTramitadoraTram" >
<input type="hidden" name="idioma" id="idioma" value="<%=idioma%>"/>

<html:hidden  property="poseeLocalizacion" />
<html:hidden  property="codLocalizacion" />

<input type="hidden" name="modoConsulta" id="modoConsulta" value="">
<input type="hidden" name="desdeConsulta">
<input type="hidden" name="desdeExpRel">
<input type="hidden" name="expRelacionado">
<input type="hidden" name="deAdjuntar">
<input type="hidden" name="porCampoSup">
<input type="hidden" name="permiteModificarObs">
<input type="hidden" name="permiteMantenerDocExt">
<input type="hidden" name="desdeInformesGestion">
<input type="hidden" name="todos">

<html:hidden  property="codMunExpIni"/>
<html:hidden  property="ejercicioExpIni"/>
<html:hidden  property="numeroExpIni"/>

<input type="hidden" name="listaCodTercero" value="">
<input type="hidden" name="listaVersionTercero" value="">
<input type="hidden" name="listaCodDomicilio" value="">
<input type="hidden" name="listaRol" value="">
<input type="hidden" name="listaMostrar" value="">
<input type="hidden" name="codTramiteRetroceder" value="">
        <input type="hidden" name="tramiteRetroceder" value="">
<input type="hidden" name="ocurrenciaTramiteRetroceder" value="">
<input type="hidden" name="fechaInicioTramiteRetroceder" value="">
<input type="hidden" name="codTramiteRetroceder2" value="">
        <input type="hidden" name="tramiteRetroceder2" value="">
<input type="hidden" name="ocurrenciaTramiteRetroceder2" value="">
<input type="hidden" name="fechaInicioTramiteRetroceder2" value="">
<input type="hidden" name="listaCodTramites" value="">
<input type="hidden" name="justificacion" value="">
<input type="hidden" name="autoriza" value="">
<input type="hidden" name="lineasPagina" value="10">
<input type="hidden" name="pagina" value="1">
<input type="hidden" name="listaModoTramites" value=""/>
<input type="hidden" name="listaUtrTramites" value=""/>
<input type="hidden" name="listaTramSigNoCumplenCondEntrada" value=""/>
<input type="hidden" name="procedimientoAsociadoAIniciar" value=""/>
<input type="hidden" name="existenCambiosSinGrabarCampos" value=""/>
<input type="hidden" name="ubicacionMensajeSW" value="FICHAEXPEDIENTE"/>
<input type="hidden" name="listaNotificacionesElectronicas"/>
<input type="hidden" name="existenCambiosSinGrabar" value="0"/>
<html:hidden  property="codigoDocumento" value=""/> <!-- Utilizado para eliminar un documento de la pestaña Otros Documentos-->
<input type="hidden" name="paginaListadoPendientes"/>
<input type="hidden" name="filtroPendientes"/>
<input type="hidden" name="idComunicacion">
<input type="hidden" name="jsonEstructuraCamposSuplementarios" id="jsonEstructuraCamposSuplementarios"/>
<div class="txttitblanco"><%=descriptor.getDescripcion("tit_fichExp")%></div>
<div class="encabezadoGris">
    <span class="etiqueta">
         <script language="javascript">
                  var expedienteCercaFinPlazo = <%=(Boolean)expedienteVO.getAtributo("cercaPlazoExpediente")%>;
                  var expedienteFueraPlazo = <%=(Boolean)expedienteVO.getAtributo("fueraPlazoExpediente")%>;

                    if(expedienteCercaFinPlazo){
                        document.write("&nbsp;<span class='fa fa-clock-o' title='<%=descriptor.getDescripcion("etiq_eCercaFinPl")%>'></span>");
                    }
                    else
                    if(expedienteFueraPlazo){
                        document.write("&nbsp;<span class='fa fa-exclamation-triangle' title='<%=descriptor.getDescripcion("etiq_fueraPl")%>'></span>");
                    }
        </script>
        &nbsp;<%=descriptor.getDescripcion("etiq_numExp")%>:
    </span>
    <html:text styleId="numExpediente" property="numExpediente" styleClass="inputTexto" style="width:14%" readonly="true" onmouseover="titulo(this);"/>
    <html:text styleId="procedimiento" property="procedimiento" styleClass="inputTexto" style="width:70%;margin-left: 1%" readonly="true" onmouseover="titulo(this);"/>
	<a href="#" onclick="pulsarListaOperacionesExpedientes();" title="<%=descriptor.getDescripcion("tit_listMovExp")%>" alt="<%=descriptor.getDescripcion("tit_listMovExp")%>">
    	<%=descriptor.getDescripcion("etiqOperaciones")%>
    </a>
    <% if ("1".equals(estadoExpediente)) {%>
        <a onclick="javascript:MostrarInformacionFinalizacion();">
            <span name="botonInf" id="inf" alt="Consultar informacin acerca de la suspension de este expediente" class="fa fa-info-circle"></span>
        </a>
    <% }%>
</div>
<div class="contenidoPantalla">
    <div class="tab-pane" id="tab-pane-1">
        <script type="text/javascript">
            tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
        </script>
        <!-- CAPA 1: DATOS GENERALES ------------------------------ -->
        <div class="tab-page" id="tabPage1">
            <h2 class="tab" id="pestana1"><%=descriptor.getDescripcion("etiqDatos")%></h2>
            <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
            <TABLE id ="tablaDatosGral" class="contenidoPestanha">
                <tr>
                    <td class="etiqueta" style="width: 14%">
                        <%=descriptor.getDescripcion("etiqIdentificacion")%>:
                    </td>
                    <td class="columnP">
                        <input id="codTipoDoc" name="codTipoDoc" type="text" class="inputTexto" style="width:3%"
                               onkeyup="return SoloDigitosNumericos(this);">
                        <input id="descTipoDoc" name="descTipoDoc" type="text" class="inputTexto" style="width:20%" readonly  >
                        <a class="contFa" id="anchorTipoDoc" name="anchorTipoDoc" href="">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoDoc" name="botonTipoDoc" alt ="<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>"></span>
                        </a>
                        <span class="etiqueta" style="width: 12%;margin:0 1% 0 1.5%"><%=descriptor.getDescripcion("gEtiqDocumento")%>:</span>
                            <%if(userAgent.indexOf("MSIE")!=-1) {%>
                            <input type="text" class="inputTexto" style="width:20%" maxlength="16" name="txtDNI" id="txtDNI"  class="inputTexto"  onchange="javascript:pulsarBuscarTercero();" onkeypress="javascript:PasaAMayusculas(event);">
                            <%}else{%>
                            <input type="text" class="inputTexto" style="width:20%" maxlength="16" name="txtDNI" id="txtDNI"  class="inputTexto"  onkeyup="return xAMayusculas(this);"
                                   onblur="javascript:pulsarBuscarTercero();">
                            <%}%>
                        <span id="botonBusquedaTerceros" style="display:inline;visibility:visible">
                            <a class="contFa" onclick="javascript:pulsarBuscarTerc('alta');">
                                <span class="fa fa-users" aria-hidden="true" alt="Gestion de interesados"/></span>
                            </a>
                        </span>
                        <a class="contFa" onclick="javascript:terceroAnterior();">
                            <span class="fa fa-arrow-circle-o-left" aria-hidden="true" name="imgAnt" id="imgAnt" style="" title="<%=descriptor.getDescripcion("etiqInterAnt")%>"></span>
                        </a>
                        <span id="msgInteresados" class="etiqueta" style="width: 12%;margin:0 0.5%">0 de 0</span>
                        <a class="contFa" onclick="javascript:terceroSiguiente();">
                            <span class="fa fa-arrow-circle-o-right" aria-hidden="true" name="imgNext" id="imgNext" style="" title="<%=descriptor.getDescripcion("etiqInterSig")%>"></span>
                        </a>
                        <span id="botonEliminarInteresado" style="display:inline;visibility:hidden">
                            <a class="contFa" onclick="javascript:eliminarTerceroActual();">
                                <span class="fa fa-trash" alt="<%=descriptor.getDescripcion("altEliminarInteresado")%>" title="<%=descriptor.getDescripcion("altEliminarInteresado")%>"></span>
                            </a>
                        </span>
                        <span id="botonListaInteresados" style="display:inline;visibility:hidden;margin-left:10%">
                            <a class="contFa" onclick="javascript:pulsarListaTerceros();">
                                <span class="fa fa-list" alt="<%=descriptor.getDescripcion("etiq_listaInt")%>" 
                                      title="<%=descriptor.getDescripcion("etiq_listaInt")%>"></span>
                            </a>
                        </span>
                        <span id="botonListaDomicilios" style="display:inline;visibility:hidden">
                            <a class="contFa" onclick="javascript:pulsarListaDomicilios();">
                                <span class="fa fa-map-marker " alt="<%=descriptor.getDescripcion("manTer_TitDomis")%>" 
                                      title="<%=descriptor.getDescripcion("manTer_TitDomis")%>"></span>
                            </a>
                        </span>
                    </td>
                </tr>
                <TR>
                    <TD class="etiqueta"> <%=descriptor.getDescripcion("etiq_IntPrin")%>:</td>
                    <td class="columnP">
                        <html:text styleId="titular" property="titular" styleClass="inputTexto" style="width:66.5%" readonly="true" onmouseover="titulo(this);"/>
                        <span class="etiqueta">Roles</span>
                        <input type="text" class="inputTexto" id="codRol" name="codRol" style="width:3%" maxlength="2" onchange=" modificaVariableCambios(1);" onkeyup="return SoloDigitosNumericos(this);">
                        <input type="text" class="inputTexto" id="descRol" name="descRol" style="width:20%" readOnly="true">
                        <a href="" id="anchorRol" name="anchorRol">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonRol" name="botonRol" style="cursor:pointer; border: 0px none"></span>
                        </a>
                    </td>
                </TR>
                <tr>
                    <td class="etiqueta">Domicilio:</td>
                    <td class="columnP">
                        <html:text styleClass="inputTexto" style="width:95%" property="domicilio" readonly="true"/>
                    </td>
                </tr>
                <tr>
                <%
                    if(poseeLoc)
                    {
                %>
                    <DIV id="capaLocalizacion" name="capaLocalizacion" STYLE="width:100%; height:0px; visibility:hidden; " >
                            <td class="etiqueta"><%=descriptor.getDescripcion("etiq_localiz")%>:</td>
                            <td class="columnP">
                                <html:text styleClass="inputTexto" style="width:95%" property="localizacion" readonly="true" />
                                <span class="fa fa-home" aria-hidden="true"  STYLE="border: 0px" name="botonTer" style="cursor:'hand'" alt="Domicilios"
                                     onclick="javascript:abrirDomicilios();" ></span>
                                <html:hidden property="refCatastral"/>
                            </td>
                    </DIV>
                <%
                    }
                %>
                <tr>
                    <td class="etiqueta" id="etiquetaNotifEletronica" style="width:14%">
                        <% if(!mostrarCheckNotificaciones){%>
                                <%=descriptor.getDescripcion("gEtiq_notifElectronicas")%>:
                        <%}else if(mostrarCheckNotificaciones && mostrarUbicacion){%>
                                <%=descriptor.getDescripcion("gEtiq_ubicacionDocu")%>:
                        <%}%>
                    </td>
                    <td class="columnP">
                        <input type="checkbox" class="notificacionDatos" name="admiteNotificacion" id="admiteNotificacion" onClick="javascript:onClickAdmiteNotificacion();" />
                        <!-- #253692 -->
                        <% if(mostrarUbicacion){ %>
                            <span id="capaUbicacionDoc" >
                                <%if(!mostrarCheckNotificaciones){%>
                                <label class="etiqueta" style="padding: 45px"><%=descriptor.getDescripcion("gEtiq_ubicacionDocu")%>:</label>
                                <%}%>
                                <html:text styleClass="inputTexto" styleId="ubicacionDoc" property="ubicacionDoc" style="width:10%" onkeyup="return xAMayusculas(this);" maxlength="10" />
                            </span>
                        <% } %>
                    </td>
                </tr>
                <tr>
                    <td class="etiqueta" colspan="2">
                        <span><%=descriptor.getDescripcion("gEtiq_Asunto")%>:</span>
                        <span style="margin-left: 45%"><%=descriptor.getDescripcion("etiqObs")%>:</span>
                    </td>
                </tr>
                <tr>
                    <td class="columnP" colspan="2">
                        <div style="float:left;width:49%">
                            <html:textarea styleClass="textareaTexto" style="text-transform: none;width:100%" cols="70" 
                                           rows="4" property="asunto" onchange=" modificaVariableCambios(1);"
                                            value=""></html:textarea>
                        </div>
                        <div style="float:left;width:49%;margin-left:1%">
                            <html:textarea styleClass="textareaTexto" style="text-transform: none;width:100%" 
                                           cols="73" rows="4" property="observaciones" onchange=" modificaVariableCambios(1);"
                                           value=""></html:textarea>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="etiqueta" style="width:14%"><%=descriptor.getDescripcion("gEtiq_fecIni")%>:</td>
                    <td>
                        <div class="columnP" style="float:left;width:12%">
                            <html:text styleClass="inputTxtFecha" style="width:100%" maxlength="9" property="fechaInicio" readonly="true"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onfocus = "this.select();"/>
                        </div>
                        <div class="etiqueta" style="float:left;width: 19%;padding:5px 5px 0 0;text-align:right"><%=descriptor.getDescripcion("gEtiq_fecFin")%>:</div>
                        <div class="columnP" style="float:left;width: 10%">
                            <html:text styleClass="inputTxtFecha" style="width:100%" maxlength="9" property="fechaFin" readonly="true"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onfocus = "this.select();"/>
                        </div>
                        <div class="columnP"  id="expedientesRel" name="expedientesRel" style="float:left;width:2%;margin-left:10px;visibility:visible">
                                <% Boolean expRelacionados = (Boolean) expedienteVO.getAtributo("expedientesRelacionados");
                                if (expRelacionados.booleanValue()) {%>
                                <input type="button" id="botonLargoResaltado" Class="botonLargoResaltado" accesskey="E" value="<%=descriptor.getDescripcion("gbExpRel")%>" name="cmdExpRel" onClick="pulsarExpedientesRelacionados();">
                                <%} else {%>
                                <input type="button" Class="botonLargo" accesskey="E" value="<%=descriptor.getDescripcion("gbExpRel")%>" name="cmdExpRel" onClick="pulsarExpedientesRelacionados();">
                                <%}%>
                        </div>
                    </td>
                </tr>
                <tr style="padding-bottom: 5px">
                    <td class="etiqueta" style="width:14%"><%=descriptor.getDescripcion("gEtiq_usuar")%>:</td>
                    <td>
                        <div class="columnP" style="float:left;width:42%">
                            <html:text styleClass="inputTexto" property="usuario" style="width:100%" maxlength="255" readonly="true"
                                       onblur="return xAMayusculas(this);"/>
                        </div>
                        <div class="etiqueta" style="float:left;width:15%;padding:5px 5px 0 0;margin-left:1%"><%=descriptor.getDescripcion("etiq_unidIni")%>:</div>
                        <div class="columnP" style="float:left;width:40%">
                            <html:text styleClass="inputTexto" property="descUnidadOrganicaExp" style="width:100%" maxlength="255" readonly="true"
                                       onblur="return xAMayusculas(this);"/>
                        </div>
                    </td>
                </tr>
                <TR>
                    <td colspan="2" class="sub3titulo">
                        <%=descriptor.getDescripcion("res_pestana2")%>
                    </td>
                </TR>
                <tr style="padding-top: 3px; padding-bottom: 5px">
                    <td  id="tablaTramites" class="tablaP" colspan="2"></td>
                </tr>
                <TR>
                    <TD style="width: 100%;text-align:center" colspan="2">
                        <input type="button" class="botonMasLargo" accesskey="I" value="<%=descriptor.getDescripcion("bIniciarTramMan")%>" name="cmdTramitacionManual"  id="cmdTramitacionManual" onclick="pulsarTramitacionManual();return false;">
                        <input type="button" class="botonMasLargo" accesskey="R" value="<%=descriptor.getDescripcion("bRetrocederExp")%>" name="cmdRetrocederExpediente" id="cmdRetrocederExpediente" onclick="pulsarRetrocederExpediente();return false;">
                        <input type= "button" class="botonMasLargo" accesskey="F" name="cmdFinalizar" id="cmdFinalizar" onclick="pulsarFinalizar();" value="<%=descriptor.getDescripcion("gbFinalizar")%>">
                    </TD>
                </TR>
            </table>
        </div>
        <!-- CAMPOS SUPLEMENTARIOS -->        
        <div class="tab-page" id="tabPage2">
            <h2 class="tab" id="pestana2"><%=descriptor.getDescripcion("etiqDatosSupl")%></h2>
            <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>
            <TABLE id ="tablaDatosSuplementarios" class="contenidoPestanha">
                <TR>
                    <TD class="sub3titulo"><%=descriptor.getDescripcion("etiqDatosSupl")%></TD>
                </TR>
                <TR style="padding-bottom:5px">
                    <TD style="width: 100%">
                    <!--  DENTRO DE ESTE DIV IRÁ EL LISTADO CON LOS CAMPOS SUPLEMENTARIOS -->
                       <DIV id="capaDatosSuplementarios" name="capaDatosSuplementarios"  style="width: 100%;overflow-x: hidden"></DIV>
                    </TD>
                </TR>
            </TABLE>
             <div id="popupTextoLargo" style="display: none;">
                <div class="content-popup">
                <div>
                    <h2><text id ="capaTitulo"></text></h2>
                    <textarea id="capaTextoLargo" styleClass="textareaTexto" style="text-transform: none;width:100%" cols="150" rows="18" readonly></textarea>
                    <div id="dialog_botonera" style="margin-top: 10px; margin-right: 5px; float: right">
                        <input type="button" class="botonGeneral" value="Cerrar" onClick="cerrarVentanaTextoLargo();">
                    </div>
                </div>
            </div>
            </div>
        </div>                                                
        <!-- CAMPOS SUPLEMENTARIOS -->
        <!-- CAPA 3: REGISTRO -------------------------------->
        <div class="tab-page" id="tabPage3">
            <h2 class="tab" id="pestana3"><%=descriptor.getDescripcion("etiqOtrosDatos")%></h2>
            <script type="text/javascript">tp1_p3 = tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>
            <table class="contenidoPestanha">
                <TR>
                    <TD class="sub3titulo"><%=descriptor.getDescripcion("etiqDocs")%></TD>
                </TR>
                <tr style="padding-top:5px">
                    <td  id="tablaDocumentos"></td>
                </tr>
                <tr>
                    <td>
                        <table width="100%">
                        <tr>
                            <td width="40%">
                              <% 	
                                if(!mostrarEnlaceDocumentacion) {%>	
                                <a href="#" class="documentos" onClick="javascript:pulsarVerDocumentos()"><%=descriptor.getDescripcion("etiqVerDocs")%></a>
                                <%}%>
                            </td>
                            <td align="right" width="60%">
                                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAnexar")%>" name="cmdAltaAdjuntoDocumento" onClick="pulsarAltaAdjuntoDocumento();return false;">
                                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbVerFirmas")%>" name="cmdVerFirmasAdjuntoDocumento" onClick="pulsarVerFirmasAdjuntoDocumento();return false;">
                                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminarDocumentoExpediente" onClick="eliminarAdjuntoDocumento();return false;">
                                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbVer")%>" name="cmdVerDocumentoExpediente" onClick="verDocumentoExpediente();return false;">
                                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelarActualizarDocumentos" onClick="pulsarCancelarActualizarDocumentos();return false;">
                            </td>
                            <td>&nbsp;</td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <TR>
                    <TD class="sub3titulo"><%=descriptor.getDescripcion("etiqRegistro")%></TD>
                </TR>
                <tr>
                    <td id="tablaAsientos"></td>
                </tr>

                <TR id="filaSubtituloEnlaces" style="display:none">
                    <TD class="sub3titulo"><%=descriptor.getDescripcion("gbEnlaces")%></TD>
                </TR>
                <TR id="filaContenidoEnlaces" style="display:none">
                    <TD style="width: 100%">
                        <DIV id="capaEnlaces" name="capaEnlaces" style="height:100px;overflow:auto;"></DIV>
                    </TD>
                </TR>

            </table>
        </div>
        <%
    /* CAPA 7: FORMULARIOS ------------------------------ */

    if ("si".equals(m_Config.getString("JSP.Formularios")) && (expForm.getFormularios().size() > 0)) {
        %>
        <div class="tab-page" id="tabPage7">
            <h2 class="tab" id="pestana7"><%=descriptor.getDescripcion("etFichaFormularios")%></h2>
            <script type="text/javascript">tp1_p7 = tp1.addTabPage(document.getElementById( "tabPage7" ) );</script>
            <table width="100%">
                <tr>
                    <td id="tablaFormularios"></td>
                </tr>
                <TR>
                    <TD style="width: 100%;text-align:center">
                        <input name="cmdVerAnexo" class="botonGeneral"
                               onclick="pulsarVerAnexoPDF();" accesskey="A"
                               type="button" value="<%=descriptor.getDescripcion("gbVerAnexo")%>">
                    </td>
                </tr>
            </table>
        </div>
        <% } else {%>
        <div style="display:none">
            <table>
                <tr><td id="tablaFormularios"></td></tr>
            </table>
        </div>
        <%}%>
        <!-- CAPA 8: OTROS DOCUMENTOS -->
        <div class="tab-page" id="tabPage8">
            <h2 class="tab" id="pestana8"> <%=descriptor.getDescripcion("etiqOtrosDocumentos")%> </h2>
            <script type="text/javascript">tp1_p8 = tp1.addTabPage( document.getElementById( "tabPage8" ) );</script>
            <table class="contenidoPestanha">
                <tr>
                    <td id="tablaOtrosDocumentos">
                    </td>
                </tr>
                <tr>
                    <td  style="padding-top:10px;text-align:center">
                        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("etiqAltaDoc")%>" name="cmdAltaOtroDocumento"  id="cmdAltaOtroDocumento" onClick="pulsarAltaOtroDocumento();return false;">
                        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminarOtroDocumento" id="cmdEliminarOtroDocumento" onClick="pulsarEliminarOtroDocumento();return false;">
                        <input type="button" class="botonLargoVerDocumento" value="<%=descriptor.getDescripcion("etiqVerDocumento")%>" name="cmdVerOtroDocumento" id="cmdVerOtroDocumento"  onClick="pulsarVisualizarOtroDocumento();return false;">
                         <%
                        if ("si".equals(m_Config.getString("JSP.BotonCSV"))){
                        %>
                            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCSV")%>" name="cmdCSVOtroDocumento" id="cmdCSVOtroDocumento" onClick="pulsarCSVOtroDocumento();return false;">
                        <% } %>
                    </td>
                </tr>
                <tr>	
                     <td>	
                        <% if(mostrarEnlaceDocumentacion) {%>	
                        <input type="button" class="botonTodaDocumentacion" value="<%=descriptor.getDescripcion("etiqVerDocs")%>" onclick="javascript:pulsarVerDocumentos()">	
                        <%}%>  	
                    </td>
                </tr>
            </table>
        </div>
       <!-- CAPA 9: COMUNICACIONES -->
        <% if (mostrarComunicaciones){%>
        <div class="tab-page" id="tabPage9">
            <h2 class="tab" id="pestana9"> <%=descriptor.getDescripcion("gEtiq_comunicaciones")%> </h2>
            <script type="text/javascript">tp1_p9 = tp1.addTabPage( document.getElementById( "tabPage9" ) );</script>
            <table width="100%" class="contenidoPestanha">
                <tr>
                    <td id="tablaComunicaciones"></td>
                </tr>
            </table>
        </div>
        <%}%>

 
        <!-- CAPA 10: NOTIFICACIONES ELECTRONICAS -->
            <% if (mostrarNotificaciones){%>
            <div class="tab-page" id="tabPage900">
                <h2 class="tab" id="pestana900"> <%=descriptor.getDescripcion("gEtiq_notificaciones")%> </h2>
                <script type="text/javascript">tp1_p9 = tp1.addTabPage( document.getElementById( "tabPage900" ) );</script>
                <table class="contenidoPestanha">
					<% if(mostrarCheckNotificaciones){%>
                    <tr style="height: 80px;">
                        <td>
                            <span class="columnaP"><input type="checkbox" name="expNotificacionTelematica" id="expNotificacionTelematica" /></span>
                            <span class="etiqueta" style="font-weight: bold; color: #000000;"><%=descriptor.getDescripcion("gEtiq_expNofifTelematica")%></span>
                            <span><input type="button" class="botonMasLargo" value="<%=descriptor.getDescripcion("gbActualizarCheckNotif")%>" name="cmdActualizarCheckNotif" onclick="pulsarActualizarCheckNotif();"></span>
                        </td>
                    </tr>
					 <%}%>
                    <tr><td class="sub3titulo"><%=descriptor.getDescripcion("not_electronicas")%></td></tr>
                    <tr><td id="tablaNotificaciones" class="tablaNotificaciones" style="text-align:center"></td></tr>
                    <tr><td>&nbsp;</td></tr>
                    <tr><td align="center"><input type="button" id="btnVerNotificacion" name="btnVerNotificacion" class="botonGeneral" value="Ver" onclick="javascript:cargarNotificacion();"></td></tr>
                    <c:if test="${!empty requestScope.mostrarTramiteNotificado}">
                        <tr><td>&nbsp;</td></tr>
                        <tr><td class="sub3titulo"><%=descriptor.getDescripcion("etiqTabTitTramitesNotificados")%></td></tr>
                        <tr><td id="tablaTramitesNotif" class="tablaTramitesNotif" style="text-align:center"></td></tr>
                    </c:if>
                </table>
            </div>
            <%}%>
            
            
            
                    <!-------------- MODULOS DE INTEGRACIÓN EXTERNOS ----------------->
        <%
            ArrayList<ModuloIntegracionExterno> modulos = expForm.getModulosExternos();
            for(int i=0;modulos!=null && i<modulos.size();i++)
            {
                ModuloIntegracionExterno modulo = modulos.get(i);                                                
                String nombreModulo = modulo.getNombreModulo();                                                
                ArrayList<DatosPantallaModuloVO> pantallas = modulo.getListaPantallasExpediente();

                for(int j=0;pantallas!=null && j<pantallas.size();j++)
                { 
                    String url = pantallas.get(j).getUrl();
                    String operacion = pantallas.get(j).getOperacionProceso();                                                                                                     

        %>     

        <jsp:include page="<%=url%>" flush="true">                                                    
           <jsp:param name="nombreModulo" value="<%=nombreModulo%>"/>
           <jsp:param name="codOrganizacionModulo" value="<%=expForm.getCodMunicipio()%>"/>
           <jsp:param name="numero" value="<%=expForm.getNumero()%>"/>
           <jsp:param name="ejercicio" value="<%=expForm.getEjercicio()%>"/>
           <jsp:param name="operacionProceso" value="<%=operacion%>"/>
           <jsp:param name="idioma" value="<%=idioma%>"/>
           <jsp:param name="tipo" value="0"/>
           <jsp:param name="codProcedimiento" value="<%=expForm.getCodProcedimiento()%>"/>
       </jsp:include>

       <%
                }// for pantallas
            }// for modulos
        %>
    <!--------------- MODULOS DE INTEGRACIÓN EXTERNOS ---------------->
    
        </div>

        <div id="capaBotones1" name="capaBotones1" STYLE="display:none" class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbImprimir")%>" name="cmdImprimirCons" onclick="pulsarImprimir();">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbVolver")%>" name="cmdVolver" onclick="pulsarVolver();">
        </div>
        <div id="capaBotones4" name="capaBotones4" STYLE="display:none" class="botoneraPrincipal">
            <input type="button" class="botonGeneral" style="color: #ffffff" value="<%=descriptor.getDescripcion("gbImprimir")%>" name="cmdImprimir4" id="cmdImprimir4" onclick="pulsarImprimir();">
            <input type="button" class="botonGeneral" style="color: #ffffff" value="<%=descriptor.getDescripcion("gbGrabar")%>" name="cmdGrabarObservaciones" id="cmdGrabarObservaciones" onclick="pulsarGrabarObservaciones();">
            <input type="button" class="botonGeneral" style="color: #ffffff"value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdVolverTramitacion4" id="cmdVolverTramitacion4" onclick="pulsarVolverTramitacion();">
        </div>
        <div id="capaBotones11" name="capaBotones11" STYLE="display:none" class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbImprimir")%>" name="cmdImprimirCons11" id="cmdImprimirCons11" style="color:#ffffff" onclick="pulsarImprimir();">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbVolver")%>" name="cmdVolver11" id="cmdVolver11" style="color:#ffffff" onclick="pulsarVolver11();">
        </div>
        <div id="capaBotones2" name="capaBotones2" STYLE="display:none" class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onclick="pulsarAceptar();">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar" onclick="pulsarCancelar();">
        </div>
        <div id="capaBotones3" name="capaBotones3" STYLE="display:none" class="botoneraPrincipal">
            <input type="button" class="botonMasLargo"  value="<%=descriptor.getDescripcion("gbDuplicarExpediente")%>" name="cmdFinExpedi" onclick="pulsarCopiarExpediente();">
            <input type="button" class="botonMasLargo"  value="<%=descriptor.getDescripcion("gbFinExpediente")%>" name="cmdFinExpedi" onclick="pulsarFinalizarExpediente();">
            <input type="button" class="botonGeneral"  value="<%=descriptor.getDescripcion("gbImprimir")%>" name="cmdImprimir" onclick="pulsarImprimir();">
            <%
            if ("SI".equals(m_Config.getString("JSP.BotonExportar"))){
            %>
            <input type="button" class="botonGeneral"  value="Exportar" name="cmdExportar" onclick="pulsarExportar();">
            <% } %>
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbGrabar")%>" name="cmdGrabarGeneral" onclick="pulsarGrabarGeneral();">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdVolverTramitacion" onclick="pulsarVolverTramitacion();">
        </div>
        <div id="capaBotones12" name="capaBotones12" STYLE="display:none" class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbImprimir")%>" name="cmdImprimirCons12" id="cmdImprimirCons12" style="color:#ffffff" onclick="pulsarImprimir();">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbVolver")%>" name="cmdVolverTramitacion12" id="cmdVolverTramitacion12" style="color:#ffffff" onclick="pulsarVolverTramitacion();">
        </div>
    </div>
</html:form>
<script>                                                                                                           
        var campos_vacios = false;                                                                
                
        var comboRol = new Combo("Rol");
        comboRol.change = modificarRolInteresado;

        var tabTramites = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaTramites'));

        tabTramites.addColumna('375','left',"<%=descriptor.getDescripcion("gEtiq_tramite")%>");
            tabTramites.addColumna('75','left',"<%=descriptor.getDescripcion("etiqFechIni")%>");
                tabTramites.addColumna('75','left',"<%=descriptor.getDescripcion("etiqFechFin")%>");
                    tabTramites.addColumna('205','left',"<%=descriptor.getDescripcion("res_etiqUnid")%>");
                        tabTramites.addColumna('161','left',"<%=descriptor.getDescripcion("etiqClasif")%>");
                            tabTramites.addColumna('25','center',"");
                            tabTramites.displayCabecera=true;
                            
                            tabTramites.colorLinea=function(rowID) {
                                var fueraDePlazo = listaTramitesOriginal[rowID][9];
                                var finalizado = listaTramitesOriginal[rowID][4];
                                var cercaFinPlazo=listaTramitesOriginal[rowID][16];

                                var tramite = listaTramitesOriginal[rowID];
                                if(finalizado!="")
                                    return 'gris';
                                else if(fueraDePlazo=="si")
                                    return 'rojoT';
                                else if(cercaFinPlazo=="si")
                                    return 'plazoCercaFin';
                                else
                                    return '';
                            }

                            tabTramites.displayDatos = pintaDatosTramites;

                            function pintaDatosTramites() {
                                tableObject = tabTramites;
                            }

                            var comboTipoDoc = new Combo("TipoDoc");

                            comboTipoDoc.change = function() {
                                document.forms[0].txtDNI.value="";                                
                            }


                            function cargarComboBox(cod, des){ 
                                eval(auxCombo+".addItems(cod,des)");
                            }

                            tableObject = tabTramites;
                            // JAVASCRIPT DE LA TABLA DE ASIENTOS
                            var tabAsientos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaAsientos'));

                            tabAsientos.addColumna('80','left',"<%= descriptor.getDescripcion("etiq_ejNum")%>");
                                tabAsientos.addColumna('25','left',"<%= descriptor.getDescripcion("gEtiqTipo")%>");
                                    tabAsientos.addColumna('100','left',"<%= descriptor.getDescripcion("gEtiq_fAnot")%>");
                                        tabAsientos.addColumna('340','left',"<%= descriptor.getDescripcion("gEtiq_Remit")%>");
                                            tabAsientos.addColumna('340','left',"<%= descriptor.getDescripcion("gEtiq_Asunto")%>");
                                                tabAsientos.displayCabecera=true;

                                                tabAsientos.displayDatos = pintaDatosAsientos;

                                                function pintaDatosAsientos() {
                                                    tableObject = tabAsientos;
                                                }
                                                // FIN DE LOS JAVASCRIPT'S DE LA TABLA DE ASIENTOS

                                                // JAVASCRIPT DE LA TABLA DE DOCUMENTOS
                                                var tabDocumentos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaDocumentos'));
                 
                                                tabDocumentos.addColumna('20','left','');
                                                tabDocumentos.addColumna('150','left',"<%= descriptor.getDescripcion("gEtiq_nombre")%>");
                                                tabDocumentos.addColumna('190','left',"<%= descriptor.getDescripcion("gEtiq_cond")%>");
                                                tabDocumentos.addColumna('190','left',"<%= descriptor.getDescripcion("etiq_NomFichero")%>");
                                                tabDocumentos.addColumna('100','left',"<%= descriptor.getDescripcion("etiq_tipFichero")%>");
                                                tabDocumentos.addColumna('75','left',"<%= descriptor.getDescripcion("gEtiq_fechaAlta")%>");
                                                tabDocumentos.addColumna('90','left',"<%= descriptor.getDescripcion("gEtiq_estadoFirma")%>");
                                                tabDocumentos.addColumna('0','left','');                                                
                                                tabDocumentos.addColumna('70','left',"<%= descriptor.getDescripcion("linkVerificarFirma")%>");
                                                tabDocumentos.displayCabecera=true;
                                                        // FIN DE LOS JAVASCRIPT'S DE LA TABLA DE DOCUMENTOS

                                                    // JAVASCRIPT DE LA TABLA DE FORMULARIOS
                                                    var tabFormularios = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaFormularios'));
                                                    tabFormularios.addColumna('75','left',"<%= descriptor.getDescripcion("etCodForm")%>");
                                                    tabFormularios.addColumna('600','left',"<%= descriptor.getDescripcion("etDesForm")%>");
                                                    tabFormularios.addColumna('80','left',"<%= descriptor.getDescripcion("etFechaForm")%>");
                                                    tabFormularios.addColumna('90','left',"<%= descriptor.getDescripcion("etUsuarioForm")%>");
                                                    tabFormularios.addColumna('20','left',"");
                                                    tabFormularios.displayCabecera=true;

                                                    tabFormularios.colorLinea=function(rowID) {
                                                        if(listaFormulariosOriginal[rowID][0]!="0"){

                                                            return 'gris';
                                                        }
                                                    }

                                                    // FIN DE LOS JAVASCRIPT'S DE LA TABLA DE FORMULARIOS

                                                 //JAVASCRIPT COMUNICACIONES
                                                <% if (mostrarComunicaciones){%>
                                                    var listaComunicaciones = new Array();
                                                    var tabComunicaciones = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaComunicaciones'));
                                                    tabComunicaciones.addColumna('0','left','');
                                                    tabComunicaciones.addColumna('150','left',"<%= descriptor.getDescripcion("gEtiq_registro")%>");
                                                    tabComunicaciones.addColumna('400','center',"<%= descriptor.getDescripcion("gEtiq_asunto")%>");
                                                    tabComunicaciones.addColumna('50','center',"<%= descriptor.getDescripcion("gEtiq_leida")%>");
                                                    tabComunicaciones.addColumna('150','center',"<%= descriptor.getDescripcion("gEtiq_fechaEnvio")%>");
                                                    tabComunicaciones.addColumna('0','left','');
                                                    tabComunicaciones.displayCabecera=true;

                                                    tabComunicaciones.lineas=listaComunicaciones;
                                                    tabComunicaciones.displayTabla();
                                                  <% } %>
														
														
                                                     var mostrarTramiteNotificados=' <%=request.getAttribute("mostrarTramiteNotificado")%>';
                                                  <% if (mostrarNotificaciones){%>                                                            
                                                     var tabNotificaciones;
                                                     tabNotificaciones = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaNotificaciones'));
                                                     <% if(mostrarColumnasNotificaciones){%>
                                                     tabNotificaciones.addColumna('100','center',"<%= descriptor.getDescripcion("etiqFechaSolEnvio")%>");
                                                     <%}%>
                                                     tabNotificaciones.addColumna('100','center',"<%= descriptor.getDescripcion("gEtiq_fechaEnvio")%>");
                                                     tabNotificaciones.addColumna('200','center',"<%= descriptor.getDescripcion("gEtiq_actoNotif")%>");                                                            
                                                     tabNotificaciones.addColumna('150','center',"<%= descriptor.getDescripcion("gEtiq_tramiteNotif")%>");
                                                     tabNotificaciones.addColumna('95','center',"<%= descriptor.getDescripcion("gEtiq_estadoNotif")%>");
                                                     tabNotificaciones.addColumna('100','center',"<%= descriptor.getDescripcion("etiqFecAcuseNotif")%>");
                                                     <%if (mostrarColumnasNotificaciones){%>
                                                     tabNotificaciones.addColumna('80','center',"<%= descriptor.getDescripcion("etiqResultNotif")%>");
                                                     tabNotificaciones.addColumna('300','center',"<%= descriptor.getDescripcion("etiqCodNotificacion")%>");
                                                     <%} else {%>
                                                     tabNotificaciones.addColumna('200','left',"<%= descriptor.getDescripcion("etiqInteresadoNotif")%>");
													 <%}%>
                                                     tabNotificaciones.displayCabecera=true;
                                                     tabNotificaciones.displayTabla();
                                                     
                                                     var tabTramitesNotif = null;
                                                     <c:if test="${!empty requestScope.mostrarTramiteNotificado}">
                                                        tabTramitesNotif = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaTramitesNotif'));
                                                        tabTramitesNotif.addColumna('361','left',"<%= descriptor.getDescripcion("gEtiq_tramite")%>");
                                                        tabTramitesNotif.addColumna('75','left',"<%= descriptor.getDescripcion("etiqFechIni")%>");                                                            
                                                        tabTramitesNotif.addColumna('75','center',"<%= descriptor.getDescripcion("etiqFechFin")%>");
                                                        tabTramitesNotif.addColumna('280','left',"<%= descriptor.getDescripcion("etiqDSupTram")%>");
                                                        tabTramitesNotif.addColumna('100','center',"<%= descriptor.getDescripcion("etiqDocs")%>");
                                                        tabTramitesNotif.displayCabecera=true;
                                                        tabTramitesNotif.displayTabla();
                                                    </c:if>
                                                     
                                                 <% } %>
							
                                                    // JAVASCRIPT DE LA TABLA DE OTROS  DOCUMENTOS
                                                    var tabOtrosDocumentos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
                                                            '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
                                                            '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
                                                            '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
                                                            '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',
                                                            document.getElementById('tablaOtrosDocumentos'));
                                                     tabOtrosDocumentos.addColumna('0','left','');
                                                    tabOtrosDocumentos.addColumna('440','left',"<%= descriptor.getDescripcion("gEtiq_nombre")%>");
						    tabOtrosDocumentos.addColumna('150','center',"<%= descriptor.getDescripcion("gEtiq_TipoDocu")%>");
                                                    tabOtrosDocumentos.addColumna('150','center',"<%= descriptor.getDescripcion("etiqFechaAlta")%>");
                                                    tabOtrosDocumentos.addColumna('0','left','');
                                                    tabOtrosDocumentos.displayCabecera=true;
						
                                                    function refrescaOtrosDocumentos() {
                                                        tabOtrosDocumentos.displayTabla();
                                                    }

                                                    /* GENERALES */

                                                    function callFromTableTo(rowID,tableName){                                                                            
                                                        if  (document.forms[0].modoConsulta.value!='si'){

                                                            if((document.forms[0].existenCambiosSinGrabar)!='undefined')var irAlAction=irActionConCambios(document.forms[0].existenCambiosSinGrabar.value,mensajeGuardarCambios);
                                                        }else var irAlAction=1;


                                                        if(tabAsientos.id == tableName){
                                                            if(irAlAction==1)
                                                            {
                                                                if  (document.forms[0].modoConsulta.value!='si'){
                                                                    if((document.forms[0].existenCambiosSinGrabar)!='undefined') document.forms[0].existenCambiosSinGrabar.value=0;                                                                                        
                                                                }
                                                                consultarAsiento(rowID);
                                                            }
                                                        } else if(tabTramites.id == tableName){                                                                                
                                                            if(irAlAction==1){
                                                                if  (document.forms[0].modoConsulta.value!='si'){
                                                                    if((document.forms[0].existenCambiosSinGrabar)!='undefined') document.forms[0].existenCambiosSinGrabar.value=0;
                                                                }
                                                                cargarTramitacion(rowID);
                                                            }
                                                          <% if (mostrarComunicaciones){%>       
                                                        } else if(tabComunicaciones && tabComunicaciones.id == tableName){                                                                                
                                                               cargarComunicacion(rowID);
                                                          <%}%>     

                                                        }else if (tabFormularios.id == tableName){
                                                            window.open("<html:rewrite page='/AbrirPDFFormulario'/>?codigo=" + listaFormularios[rowID][0] + "&opcion=imprimir","ventanaForm");
                                                            }
                                                    }
									
                                                    var coordx=0;
                                                    var coordy=0;

                                                   
                                                   window.addEventListener('mousemove', function(e) {
                                                        coordx = e.clientX;
                                                        coordy = e.clientY;
                                                    }, true);
                                                 


    document.onmouseup = checkKeys;

    function checkKeysLocal(evento,tecla){ 
        if('Alt+V'==tecla) {
            if(document.getElementById('capaBotones1').style.display=='')
               pulsarVolver();
            else if(document.getElementById('capaBotones3').style.display=='')
                pulsarVolverTramitacion();
            else if(document.getElementById('capaBotones4').style.display=='')
                pulsarVolverTramitacion();
            }else if('Alt+A'==tecla) {
            if(document.getElementById('capaBotones2').style.display=='')
                pulsarAceptar();
            } else if('Alt+C'==tecla) {
                if(document.getElementById('capaBotones2').style.display=='')  pulsarCancelar();
                if(tp1.getSelectedIndex() == 4) pulsarCancelarActualizarDocumentos();
            } else if('Alt+G'==tecla) {
                if(document.getElementById('capaBotones3').style.display=='') pulsarGrabarGeneral();
                else 
                if(document.getElementById('capaBotones4').style.display=='') pulsarGrabarObservaciones();
            }

            var aux=null;
            if(window.event)
                aux = window.event;
            else
                aux = evento;

            var tecla = 0;
            if(aux.keyCode)
                tecla = aux.keyCode;
            else
                tecla = aux.which;

            if( (tecla == 40)|| (tecla == 38) ){
                if(tabAsientos==tableObject) {
                    upDownTable(tabAsientos,listaAsientos,tecla);
                } else if(tabTramites==tableObject){
                    upDownTable(tabTramites,listaTramites,tecla);
                } else if(tabDocumentos==tableObject){
                    upDownTable(tabDocumentos,listaDocumentos,tecla);
                }
            }

            if(tecla == 13){
                 if((tabAsientos.selectedIndex>-1)&&(tabAsientos.selectedIndex < listaAsientos.length)){
                     callFromTableTo(tabAsientos.selectedIndex,tabAsientos.id);
                 }
                 if((tabTramites.selectedIndex>-1)&&(tabTramites.selectedIndex < listaTramites.length)){
                     callFromTableTo(tabTramites.selectedIndex,tabTramites.id);
                  } else if($("#txtDNI").is(":focus")){	
                    pulsarBuscarTercero();		
                 }
                 
            }
            
            if ((tecla == 0)||(tecla == 1)){
           
                if (comboRol.base.style.visibility == "visible" && isClickOutCombo(comboRol,coordx,coordy)) setTimeout('comboRol.ocultar()',20);
                if (comboTipoDoc.base.style.visibility == "visible" && isClickOutCombo(comboTipoDoc,coordx,coordy)) setTimeout('comboTipoDoc.ocultar()',20);                                                           
                                               
                 if(IsCalendarVisible) replegarCalendario(coordx,coordy);
                  
            }
            
            if (tecla == 9){
                comboRol.ocultar();
                comboTipoDoc.ocultar();
            }  
            keyDel(aux);
}
                                            
                                            
 function inicializarBotonesDatosSuplementarios(){
    var botones=[];
    var j=0;
    if("<%=(String) expedienteVO.getAtributo("modoConsulta")%>" == "si") {
        <%String[] matriz = campos.split(",");
            String[] matrizOcurrencias = ocurrencias.split(",");
            String aux = "";
            for (int i = 0; i < matriz.length; i++) {
                aux = matriz[i];
                if (matrizOcurrencias[i] != null && !"".equals(matrizOcurrencias[i]) && !matrizOcurrencias[i].equals("null")) {
                    aux = matriz[i] + "_" + matrizOcurrencias[i];
                }%>
                if ((<%=(aux != null)%>) && (<%=(!aux.equals(""))%>) && (eval("document.forms[0].<%=aux%>.value")!="")) {
                    botones[j]= document.forms[0].cmdVisualizar<%=aux.toUpperCase()%>;
                    j=j+1;
                }
                if (document.getElementById("imagenBoton<%=matriz[i].toUpperCase()%>")!=null) 
                    document.getElementById("imagenBoton<%=matriz[i].toUpperCase()%>").style.color="#f6f6f6";
                    <%}%>

                    habilitarGeneral(botones);
                }
            }
            function antesDeCambiarPestana() {
                compruebaModificadoExpediente();
            }

            function compruebaModificadoExpediente(){
                var expRelacionados = "no";
    <%
            Boolean expRelacionados = (Boolean) expedienteVO.getAtributo("expedientesRelacionados");
            if (expRelacionados.booleanValue()) {
    %>
        expRelacionados ="si";
    <%
            }
    %>

        //tabPage1 datos
        var selected1 = document.getElementById("pestana1").className;
        var changeSelected1 = false;
        if ((selected1 == "tab selected") || (selected1 == "tab-remark selected")) {
            changeSelected1 = true;
        }
        if ((document.forms[0].titular.value!="")||(document.forms[0].asunto.value!="")||(expRelacionados=="si")||(document.forms[0].observaciones.value!="")) {
            if (changeSelected1){
                document.getElementById("pestana1").className="tab-remark selected";}
            else {
                document.getElementById("pestana1").className="tab-remark";}
        } else{
        if (changeSelected1){
            document.getElementById("pestana1").className="tab selected";}
        else{ document.getElementById("pestana1").className="tab";}
    }
    
    
    //tabPage3 documentos
    if (document.getElementById("pestana3") != null) {
        var marcado = false;
        var entregado = null;
        for (var i = 0; i < listaDocumentos.length; i++) {
            entregado = document.getElementById("documentoEntregado" + i);
            if (entregado!=null && entregado.checked) marcado = true;
        }

        if(!marcado){
            // Si no hay documentos, se comprueba si hay asientos asociados para marcar la pestaña "Otros datos"
            if(listaAsientos.length>0)
                marcado=true;
        }

        var selected3 = document.getElementById("pestana3").className;
        var changeSelected3 = false;
        if ((selected3 == "tab page") || (selected3 == "tab-remark selected")) {
            changeSelected3 = true;
        }
        if (marcado) {
            if (changeSelected3) document.getElementById("pestana3").className="tab-remark selected";
            else document.getElementById("pestana3").className="tab-remark";
        } else{
        if (changeSelected3) document.getElementById("pestana3").className="tab selected";
        else document.getElementById("pestana3").className="tab";

    }
    }
    
                                                                                                                                                        
    //tabPage3 documentos
    if (document.getElementById("pestana9") != null) {       
        var selected9 = document.getElementById("pestana9").className;
        var changeSelected9 = false;
        if ((selected9 == "tab page") || (selected3 == "tab-remark selected")) {
            changeSelected9 = true;
        }
        if (changeSelected9) document.getElementById("pestana9").className="tab selected";
        else document.getElementById("pestana9").className="tab";

    }    
}

function recargarCamposCalculados() { 
    var ajax = getXMLHttpRequest();
    var codProcedimiento = document.forms[0].codProcedimiento.value;
    var codMunicipio     = document.forms[0].codMunicipio.value;
    var numero           = document.forms[0].numero.value;
    var result ="";
    var i;
    var valor = ""; 
    var parametrosFinal = "";
    var continuar=0;
    
    if(ajax!=null){                
        var url = "<%=request.getContextPath() %>" + "/sge/FichaExpediente.do";                
        var cadena =  nombreCamposRecargarCalculados;  
        var campos2 = cadena.split("#");
        var parametros = "&opcion=calculo_expresion&codMunicipio=" + escape(codMunicipio) + "&codProcedimiento=" + escape(codProcedimiento)
                       + "&numero=" + escape(numero); 
        
        var cadena2 = tiposCamposRecargarCalculados;        
        if((cadena2.indexOf("8")!=-1)||(cadena2.indexOf("9")!=-1)){
            //Si no hay campos calculados no hacemos nada en esta parte
            continuar=1;
        }
        
        if (cadena != "" && continuar==1){
            for (i = 0; i < campos2.length; i++){   
                valor = $('#' + campos2[i]).val();
                if(valor!="" && valor!=undefined && valor!=null){
                    valor = valor.replace(".","");
                    valor = valor.replace(",",".");                                        
                    parametrosFinal = parametrosFinal + "&" + campos2[i] + "=" + valor;
                }
            }
        }
        
        if(parametrosFinal!=null && parametros!="" && continuar==1){                
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            parametros = parametros + parametrosFinal;
            ajax.send(parametros);

            try
            {            
                if (ajax.readyState==4 && ajax.status==200){         
                    // En IE el XML viene en responseText y no en la propiedad responseXML
                   var text = ajax.responseText;               
                   var datosRespuesta = text.split("#");               
                   campos_vacios = false;
                   for (i = 0; i < datosRespuesta.length - 1 ; i++){
                       
                       var linea = datosRespuesta[i].split("|");
                       linea[1] = linea[1].replace(".",",");   						
                       //eval("document.forms[0]."+linea[0]+".value='"+linea[1]+"';");  
					   // BCS compatibilidad IE9		
					   eval("document.forms[0].elements['"+linea[0]+"'].value='"+linea[1]+"';");
                       result = eval("document.forms[0].elements['"+linea[0]+"'].value;");                   
                       eval("document.forms[0].cmdVerOpe_"+linea[0]+".style.display='';");
                       if (result == ""){
                           campos_vacios = true;                       
                       }                   
                   }
                }// if
                
            }catch(Err){
                alert("recargarCamposCalculados() error: " + Err.message);
            }
        }// if
   }//if(ajax!=null)      
}//recargarDatosCalculados

function ValidaCamposCalculados(){  
    var ajax = getXMLHttpRequest();
    var codProcedimiento = document.forms[0].codProcedimiento.value;
    var codMunicipio     = document.forms[0].codMunicipio.value;
    var numero           = document.forms[0].numero.value;
    if(ajax!=null) {                 
        var i = tabTramites.selectedIndex;        
        var ocurrenciaTramite = listaTramitesOriginal[i][0];
        var codTramite        = listaTramitesOriginal[i][1];                                
        var url = "<%=request.getContextPath() %>" + "/sge/FichaExpediente.do";        
        
        var parametros = "&opcion=validaCamposCalculados&codMunicipio=" + escape(codMunicipio) + "&codProcedimiento=" + escape(codProcedimiento)
                            + "&numero=" + escape(numero) + "&ocurrencia=" + escape(ocurrenciaTramite) + "&tramite=" + escape(codTramite);
                    
        ajax.open("POST",url,false);
        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
        ajax.send(parametros);
        try {            
            if (ajax.readyState==4 && ajax.status==200) {         
                // En IE el XML viene en responseText y no en la propiedad responseXML
               var text = ajax.responseText;                                                            
               if (text.trim() == "1") {
                   return false;
                } else
                   return true;
            } else
                return false;
        }catch(Err){
            alert("Error.descripcion: " + Err.description);
            return false;
        }
    }//if(ajax!=null)
}//ValidaDatosCalculados



function pulsarVerExterno(nombre){  
    var codProcedimiento = document.forms[0].codProcedimiento.value;
    var codMunicipio     = document.forms[0].codMunicipio.value;       
    var valor = eval("document.forms[0]."+nombre+".value");             
    abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mostrarExterno.jsp'/>?codMunicipio=" + 
            escape(codMunicipio) + "&codProcedimiento='" + escape(codProcedimiento) + "'&nombreCampo='" + 
            nombre+"'&valor_dato='"+valor+"'&opcion_pr_tra='procedimiento'",'',
            'width=700,height=450,scrollbars=no',function(result){
                if (result != null){      
                    eval("document.forms[0]."+nombre+"_CODSEL.value='"+result[0]+"'");
                    eval("document.forms[0]."+nombre+".value='"+result[1]+"'");
                }        
            });
}

function ChequearCamposCalculados(){ 
    var ajax = getXMLHttpRequest();
    var codProcedimiento = document.forms[0].codProcedimiento.value;
    var codMunicipio     = document.forms[0].codMunicipio.value;
    var numero           = document.forms[0].numero.value;
    var result ="";
    
    var parametrosCampos = "";
    if(ajax!=null) {                              
        var url = getContextPath() + "/sge/FichaExpediente.do";        
        var cadena = "<%=nombre_campo%>";
        var campos2 = cadena.split("#");
       
        var parametros = "&opcion=ChequearCamposCalculados&codMunicipio=" + escape(codMunicipio) + "&codProcedimiento=" + escape(codProcedimiento)
                            + "&numero=" + escape(numero);                    
        if (cadena != ""){
            for (i = 0; i < campos2.length; i++) {
                valor = eval("document.forms[0]."+campos2[i]+".value");
                if(valor!=null && valor!=""){
                    valor = valor.replace(".","");
                    valor = valor.replace(",",".");                                    
                    parametrosCampos = parametrosCampos + "&" + campos2[i] + "=" + valor;
                }
            }
        }        
        
        if(parametrosCampos!=null && parametrosCampos!=""){        
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            parametros = parametros + parametrosCampos;
            ajax.send(parametros);
            try {            
                if (ajax.readyState==4 && ajax.status==200) {         
                    // En IE el XML viene en responseText y no en la propiedad responseXML
                   var text = ajax.responseText;  
                   return text;
                }
            }catch(Err){ 
                alert("Error.descripcion: " + Err.description);
                return "";
            }
        }//if
    }//if(ajax!=null) 
}

function getContextPath(){
    var contextPath = '<%=request.getContextPath()%>';
    return contextPath;
}//getContextPath

function getMensajesError(codMensaje){
    var mensajeError = "";
    if("msjDocIncorrecto" == codMensaje){
        mensajeError = '<%=descriptor.getDescripcion("msjDocIncorrecto")%>';
    }else if("msjLetraNif"  == codMensaje){
        mensajeError = '<%=descriptor.getDescripcion("msjLetraNif")%>';
    }else if("msjPregContinuar" == codMensaje){
        mensajeError = '<%=descriptor.getDescripcion("msjPregContinuar")%>';
    }else if("msgNoFecha" == codMensaje){
        mensajeError = '<%=descriptor.getDescripcion("msgNoFecha")%>';
    }else if("msgNoPlazo" == codMensaje){
        mensajeError = '<%=descriptor.getDescripcion("msgNoPlazo")%>';
    }
    return mensajeError;
}

function retrocederExpedienteComprobarFirma(){
     pleaseWait1('on',this);
    try{
        var datos = {};

        datos.opcion = 'verificar_tramite_pendiente_firma';
        datos.codTramite = document.forms[0].codTramiteRetroceder.value;
        datos.ocuTramite = document.forms[0].ocurrenciaTramiteRetroceder.value;
        datos.numero = document.forms[0].numero.value;
        datos.ejercicio = document.forms[0].ejercicio.value;
        datos.procedimiento = document.forms[0].codProcedimiento.value;
        datos.codMunicipio=document.forms[0].codMunicipio.value;
        datos.codProcedimiento = document.forms[0].codProcedimiento.value;

         $.ajax({
            url: getContextPath() + "/sge/FichaExpediente.do" ,
            type: 'POST',
            async: true,
            data: datos,
            success: procesarRespuestaRetrocederExpediente,
            error: errorProcesarRespuestaRetrocederExpediente 


        }); 

    }catch(Err){
        pleaseWaitSinFrame('off');


        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
    }
}

function procesarRespuestaRetrocederExpediente(ajaxResult){
    if(ajaxResult!=null){
        pendienteFirma = JSON.parse(ajaxResult);
        if((pendienteFirma && jsp_alerta("C",'<%=descriptor.getDescripcion("msjRetrocederPendienteFirma")%>'))|| !pendienteFirma ){
            var source = "<c:url value='/sge/FichaExpediente.do?'/>"
                        +"&codMun="  + document.forms[0].codMunicipio.value
                        +"&codProc=" + document.forms[0].codProcedimiento.value
                        +"&eje=" + document.forms[0].ejercicio.value
                        +"&num=" + document.forms[0].numero.value;
             document.forms[0].opcion.value="retroceder_expediente";
             document.forms[0].target="oculto";
             document.forms[0].action=source;
             document.forms[0].submit();
                                            
         } else {
            pleaseWaitSinFrame('off');
            $("#cmdRetrocederExpediente").prop("disabled",false);
        }
                                           
   }else{
      pleaseWaitSinFrame("off");  
      jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
   }
   
}

function errorProcesarRespuestaRetrocederExpediente(){
    pleaseWaitSinFrame("off");
    jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
    
}

function refreshMarcasNotifTelematica(marca){ 
    pleaseWait1('on',this);
    
    if (marca == 1) {
        //marcar al representante(rol 2) ó en su defecto, al interesado (rol 1)
        //el rol es terceros[i][7]
        var hayRepresentante = 0;
        for (var i = 0; i < listaNotificacionesElectronicas.length; i++) {
            var rol = terceros[i][7];
            if (rol == 2) {
                listaNotificacionesElectronicas[i]=1;
                hayRepresentante = 1;
            } else {
                listaNotificacionesElectronicas[i]=0;
            }
        }
        if (hayRepresentante == 0) {
            for (var i = 0; i < listaNotificacionesElectronicas.length; i++) {
                var rol = terceros[i][7];
                if (rol == 1) {
                    listaNotificacionesElectronicas[i]=1;
                } else {
                    listaNotificacionesElectronicas[i]=0;
                }
            }
        }
        existenInteresadosNotifElectronica=1;
    }else if (marca == 0) {
        //desmarcar a todos
        for (var i = 0; i < listaNotificacionesElectronicas.length; i++) {
            listaNotificacionesElectronicas[i]=0;
        }
    }
 
    pleaseWait1('off',this);
}

function pulsarActualizarCheckNotif(){
    var ajax = getXMLHttpRequest();

    var organizacion = $('#codMunicipio').val();
    //var numExpediente = document.forms[0].numExpediente.value;
    var numExpediente = $('#numExpediente').val();
    var checkMarca = 0;
    if($('#expNotificacionTelematica').prop('checked')) {
        checkMarca = 1;
    }
  
    if(ajax!=null){                                                         
        var url = getContextPath() + "/sge/FichaExpediente.do";       
        var parametros = "&opcion=actualizarMarcaNotif&organizacion=" + organizacion + "&numExpediente=" + numExpediente + "&checkMarca=" + checkMarca;

        ajax.open("POST",url,false);
        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
        ajax.send(parametros);
        try{            
            if (ajax.readyState==4 && ajax.status==200){											
                var text = ajax.responseText;
                var result = text.trim();

                if(result == '1') {
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msgNoExpedienteMiCarpeta")%>');
                }
                else if (result == '3'){
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msgRolesIncorrectos")%>');
                }
                else if (result == '4'){
                    refreshMarcasNotifTelematica(0);
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msgQuitarMarcaNotif")%>');
                }
                else if (result == '5'){
                    refreshMarcasNotifTelematica(1);
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msgPonerMarcaNotif")%>');
                }
                else {
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorGenerico")%>');
                }
            }

        }catch(Err){
            alert("Error.descripcion: " + Err.description);            
        }
    }

}

function recargarPagina(){
    window.location.reload();
}

    </script>
    <div id="desplegable" style="overflow-y: auto; overflow-x: no; visibility: hidden; BORDER: 0px"></div>
</body>
</html:html>
