<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.util.ValidarDocumento" %>
<%@page import="java.util.Vector" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO"%>
<%@ page import="es.altia.util.conexion.AdaptadorSQLBD"%>
<%@ page import="es.altia.util.conexion.AdaptadorSQL"%>
<%@ page import="es.altia.agora.webservice.registro.pisa.regexterno.controller.AnotacionRegistroExternoForm" %>
<%@page language="java" contentType="text/html" pageEncoding="ISO-8859-15"%>
<html:html>    
    <head>
        
        <TITLE>::: Registro de salida en Accede :::</TITLE>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        
        <%
            //=================================================================================================
            MantAnotacionRegistroForm mantARForm;
            if (request.getAttribute("MantAnotacionRegistroForm") != null) {
                mantARForm = (MantAnotacionRegistroForm) request.getAttribute("MantAnotacionRegistroForm");
            } else {
                mantARForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
            }
            //=================================================================================================
            //=================================================================================================
            AnotacionRegistroExternoForm anotacionForm;
            if (request.getAttribute("AnotacionRegistroExternoForm") != null) {
                anotacionForm = (AnotacionRegistroExternoForm) request.getAttribute("AnotacionRegistroExternoForm");
            } else {
                anotacionForm = (AnotacionRegistroExternoForm) session.getAttribute("AnotacionRegistroExternoForm");
            }
            //=================================================================================================            

            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            RegistroUsuarioValueObject regUsuarioVO = new RegistroUsuarioValueObject();
            Config m_Conf = ConfigServiceHelper.getConfig("common");
            String JSP_poblacion = m_Conf.getString("JSP.Poblacion");
            String JSP_autoridad = m_Conf.getString("JSP.Registro.Autoridad");
            String pv = "visible";
            if ("no".equals(JSP_poblacion)) {
                pv = "hidden";
            }
            String aut = "visible";
            if ("no".equals(JSP_autoridad)) {
                aut = "hidden";
            }


            int idioma = 1;
            int apl = 97;
            int cod_org = 0;
            int cod_dep = 1;
            int cod_ent = 1;
            int cod_unidOrg = 1;
            boolean permisoMantenimiento = false;
            boolean permisoMantenimientoSalida = false;
            String desc_org = "";
            String desc_ent = "";
            String respOpcion = "";
            String funcion = "";
            String dil = "";
            String deBuzonRechazadas = "";


            String idSesion = session.getId();

            if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null)) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                regUsuarioVO = (RegistroUsuarioValueObject) session.getAttribute("registroUsuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                cod_org = usuarioVO.getOrgCod();
                cod_dep = regUsuarioVO.getDepCod();
                cod_ent = usuarioVO.getEntCod();
                cod_unidOrg = regUsuarioVO.getUnidadOrgCod();
                desc_org = usuarioVO.getOrg();
                desc_ent = usuarioVO.getEnt();
            }

            boolean hayBuscada = false;
            boolean contestacion = false;

            if (session.getAttribute("modoInicio") != null) {
                if ("recargar_buscada".equals((String) session.getAttribute("modoInicio"))) {
                    hayBuscada = true;
                } else if ("contestar_entrada".equals((String) session.getAttribute("modoInicio"))) {
                    contestacion = true;
                }

                session.removeValue("modoInicio");
            }

            String tipoAnotacion = "S";
            if (session.getAttribute("tipoAnotacion") != null) {
                tipoAnotacion = (String) session.getAttribute("tipoAnotacion");
            }
            String titPag;
            String tipo;
            String fech;
            String numOrden;
            String destino;
            String origen;

            if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {
                permisoMantenimiento = usuarioVO.getMantenerEntrada();
                permisoMantenimientoSalida = usuarioVO.getMantenerSalida();
                titPag = "tit_AnotE";
                tipo = "res_tipoEntrada";
                fech = "res_fecE";
                numOrden = "res_numOrdE";
                destino = "res_etiqDestino";
                origen = "res_etiqOrigen";
            } else {
                permisoMantenimiento = usuarioVO.getMantenerSalida();
                titPag = "tit_AnotS";
                tipo = "res_tipoSalida";
                fech = "res_fecS";
                numOrden = "res_numOrdS";
                destino = "res_etiqOrigen";
                origen = "res_etiqDestino";
            }
            if (session.getAttribute("reservas") != null) {
                respOpcion = (String) session.getAttribute("reservas");
                session.removeValue("reservas");
            }
            if (session.getAttribute("deBuzonRechazadas") != null) {
                deBuzonRechazadas = (String) session.getAttribute("reservas");
                session.removeValue("reservas");
            } else {
                deBuzonRechazadas = "no";
                session.removeValue("reservas");
            }

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
        %>
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
        
        <style type="text/css">
            
            /* Override styles from webfxlayout */
            /* Para colocar las pestañas correctamente */
            
            .dynamic-tab-pane-control .tab-page {
                height:		200px;
            }
            
            .dynamic-tab-pane-control .tab-page .dynamic-tab-pane-control .tab-page {
                height:		100px;
            }
            
        </style>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/tabpane.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/listas.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/listaBusquedaTerceros.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
        <link rel="stylesheet" type="text/css" href="<html:rewrite page='/css/estilo.css" media="screen'/>">
        <script type="text/javascript" src="<html:rewrite page='/scripts/altaRE.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/gestionTerceros.js'/>"></script>
        <script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>"></script>
        
        <script type="text/javascript">
            
/********************************************************************/
/* A PARTIR DE AQUI EL NUEVO JAVASCRIPT					*/
/********************************************************************/
var cod_tiposIdInteresado = new Array();
var desc_tiposIdInteresado = new Array();
var cod_intTiposIdInteresado = new Array();
var cod_uors = new Array();
var desc_uors = new Array();
var cod_intUors = new Array();
var cod_procs = new Array();
var desc_procs = new Array();
var tercero=new Array();

function salir(){
	var resultado = jsp_alerta("",'<%= descriptor.getDescripcion("g_confirmSalirApp")%>');
	if (resultado == 1){
		document.forms[0].target = '_top';
      	document.forms[0].action = '<c:url value='/SalirApp.do?app='/><%= usuarioVO.getAppCod()%>';
      	document.forms[0].submit();
	}
}
/********************************************************************/
/* FUNCION CARGAR SOLO LISTAS										*/
/********************************************************************/

function cargarSoloListas() {

     // Cargar la lista de tipos de documento.
     var i = 0;
     <c:forEach var="elemento" items="${requestScope.comboTiposDoc.elementosCombo}">
     cod_tiposIdInteresado[i]='<c:out value="${elemento.codigoElemento}"/>';
     desc_tiposIdInteresado[i]='<c:out value="${elemento.descripcionElemento}"/>';
     cod_intTiposIdInteresado[i]='<c:out value="${elemento.codigoInternoElemento}"/>';
     i++;
     </c:forEach>

     // Cargar la lista de Uors.
     i = 0;
     <c:forEach var="elemento" items="${requestScope.comboUORsAccede.elementosCombo}">
     cod_uors[i]='<c:out value="${elemento.codigoElemento}"/>';
     desc_uors[i]='<c:out value="${elemento.descripcionElemento}"/>';     
     cod_intUors[i]='<c:out value="${elemento.codigoInternoElemento}"/>';
     i++;
     </c:forEach>                 
    // alert(uors);
}

/********************************************************************/
/* FUNCION INICIALIZAR 												*/
/********************************************************************/
function inicializar() {
    <c:if test="${!empty requestScope.errorAlta}">
        jsp_alerta('A', "Alta no efectuada por motivos técnicos");
    </c:if>
//    habilitarImagenCal("calDesde",true);
    habilitarImagenCal("calFechaAnotacion",true);	
    cargarSoloListas();

    comboUor.addItems(cod_uors,desc_uors);
    comboTipoDoc.addItems(cod_tiposIdInteresado,desc_tiposIdInteresado);
    tp1.setSelectedIndex(0);
}

function posicionUOR(codigoUOR){
    var i;
    for (i=0; i<cod_uors.length ; i++) {
        if (codigoUOR==cod_uors[i]) return i;
    }
    return -1; 
}    

function getListaProcsByUor() {
    document.forms[0].cod_intUOR.value = cod_intUors[posicionUOR(document.forms[0].codUor.value)];
    document.forms[0].opcion.value = "cargaProcs";
    document.forms[0].target="oculto";
    document.forms[0].action="<html:rewrite page='/registroExterno/GetListaProcedimientos.do'/>";
    document.forms[0].submit();    
}

function rellenaListasProcs(lista1, lista2) {
    cod_procs = lista1;
    desc_procs = lista2;
    comboProcedimiento.addItems(cod_procs,desc_procs);
}

function borrarInteresado1(){
    if(dTipoDoc != document.forms[0].descTipoDoc.value || tipoDoc != document.forms[0].codTipoDoc.value) {
            document.forms[0].codTerc.value='0';
            document.forms[0].txtIdTercero.value='0';
            document.forms[0].numModifTerc.value='0';
            document.forms[0].txtVersion.value='0';
            document.forms[0].codDomTerc.value='0';
            document.forms[0].txtDNI.value=''; // Tipo documento.
            document.forms[0].txtInteresado.value=''; // Razon Social.
            document.forms[0].txtApell1.value=''; // Apellidos.
            document.forms[0].txtApell2.value='';
            document.forms[0].txtPart.value=''; // Partículas.
            document.forms[0].txtPart2.value='';
            document.forms[0].txtTelefono.value=''; // Telefono.
            document.forms[0].txtCorreo.value=''; // Email.
            document.forms[0].txtPais.value=''; // Pais.
            document.forms[0].txtProv.value=''; // Provincia.
            document.forms[0].txtMuni.value=''; // Municipio.
            document.forms[0].txtDomicilio.value=''; // Nombre vía.
            document.forms[0].txtPoblacion.value='';
            document.forms[0].txtCP.value=''; // Codigo.
            if (consultando) {
                    var v = new Array(document.forms[0].txtInteresado);
                    habilitarGeneral(v);
            }
    }
}
var datosLength;
var TerceroA = new Array();
	function cargarListaB(datos, boton) {
		TerceroA = datos;
    datosLength = datos.length;
    var lenCasas = 0;
    for(i=0;i<datosLength;i++){
            if (datos[i][18].length>lenCasas)
                    lenCasas=datos[i][18].length;
    }
    if ((datosLength >1)||(lenCasas>1)){
      var argumentos = new Array();
      argumentos =[datos,""];
      var source = APP_CONTEXT_PATH + "/BusquedaTerceros.do?opcion=inicializarBusquedaTerc&ventana=true";
      abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/terceros/mainVentana.jsp?source="+source,argumentos,
            'width=990,height=650',function(TerSel){
	  if (TerSel==undefined) {
                        TerSel=['','','','','','','','','','','','','','','','','','','','',''];
	  }
                    vectorCampos = ['codTerc','numModifTerc','txtIdTercero','txtVersion','codTipoDoc','txtDNI','txtInteresado',
                        'txtApell1','txtApell2','txtPart','txtPart2','txtTelefono','txtCorreo','codDomTerc',
                        'txtIdDomicilio','txtPais','txtProv','txtMuni','txtDomicilio','txtCP','txtPoblacion'];
                    rellenarTercero(TerSel,vectorCampos);
            });
    }else if(lenCasas==1){
      var listaCasas = new Array();
      var vectorCampos = new Array();
      var vectorDatos = new Array();
      var domicilio = "";
      var nombre = "";
      nombre = (TerceroA[0][7]!="")? nombre + TerceroA[0][7]:nombre;
      nombre = (TerceroA[0][5]!="")? nombre +" "+ TerceroA[0][5]:nombre;
      nombre = (TerceroA[0][8]!="")? nombre +" "+ TerceroA[0][8]:nombre;
      nombre = (TerceroA[0][6]!="")? nombre +" "+ TerceroA[0][6]:nombre;
      nombre = (nombre!="")?nombre+", "+ TerceroA[0][4]:nombre + TerceroA[0][4];
      // nombre = TerceroA[0][4] + " " + TerceroA[0][7] + " " + TerceroA[0][5] + " " + 
      //   TerceroA[0][8] + " " + TerceroA[0][6];
      listaCasas = TerceroA[0][18];
      if (listaCasas[0][29] != "") {
          domicilio = (listaCasas[0][20]!="") ? domicilio+listaCasas[0][20]+" ":domicilio;      // DESCRIPCION DEL TIPO DE VIA
          domicilio = (listaCasas[0][29]!="") ? domicilio+listaCasas[0][29]+" ":domicilio;      // DESCRIPCION DE LA VIA
          domicilio = (listaCasas[0][9]!=0) ? domicilio+" "+listaCasas[0][9]:domicilio;         // PRIMER NUMERO DE LA VIA
          domicilio = (listaCasas[0][10]!="") ? domicilio+" "+listaCasas[0][10]+" ":domicilio;  // PRIMERA LETRA DE LA VIA
          domicilio = (listaCasas[0][11]!=0) ? domicilio+" "+listaCasas[0][11]:domicilio;       // ULTIMO NUMERO DE LA VIA
          domicilio = (listaCasas[0][12]!="") ? domicilio+" "+listaCasas[0][12]:domicilio;      // ULTIMA LETRA DE LA VIA
          domicilio = (listaCasas[0][13]!="") ? domicilio+" Bl. "+listaCasas[0][13]:domicilio;  // BLOQUE
          domicilio = (listaCasas[0][14]!="") ? domicilio+" Portal "+listaCasas[0][14]:domicilio; // PORTAL
          domicilio = (listaCasas[0][15]!="") ? domicilio+" Esc. "+listaCasas[0][15]:domicilio; // ESCALERA
          domicilio = (listaCasas[0][16]!="") ? domicilio+" "+listaCasas[0][16]+"º ":domicilio; // PLANTA
          domicilio = (listaCasas[0][17]!="") ? domicilio+listaCasas[0][17]:domicilio;          // PUERTA
      } else {
          domicilio = (listaCasas[0][3]);
      }
      
      vectorCampos = ['codTerc','numModifTerc','txtIdTercero','txtVersion','codTipoDoc','txtDNI','txtInteresado',
                      'txtApell1','txtApell2','txtPart','txtPart2','txtTelefono','txtCorreo','codDomTerc',
                      'txtIdDomicilio','txtPais','txtProv','txtMuni','txtDomicilio','txtCP','txtPoblacion'];
      vectorDatos = [TerceroA[0][0],TerceroA[0][1],TerceroA[0][0],TerceroA[0][1],TerceroA[0][2],TerceroA[0][3],nombre,
                     TerceroA[0][5],TerceroA[0][6],TerceroA[0][7],TerceroA[0][8],TerceroA[0][9],TerceroA[0][10],
                     listaCasas[0][5],listaCasas[0][5],listaCasas[0][0],listaCasas[0][1],listaCasas[0][2],domicilio,
                     listaCasas[0][4],listaCasas[0][18]];
      rellenarTercero(vectorDatos,vectorCampos);
      capaVisible = false;
    }else {
      var vectorCampos = new Array();
      var vectorDatos = new Array();
      var nombre = "";
      nombre = (TerceroA[0][7]!="")? nombre + TerceroA[0][7]:nombre;
      nombre = (TerceroA[0][5]!="")? nombre +" "+ TerceroA[0][5]:nombre;
      nombre = (TerceroA[0][8]!="")? nombre +" "+ TerceroA[0][8]:nombre;
      nombre = (TerceroA[0][6]!="")? nombre +" "+ TerceroA[0][6]:nombre;
      nombre = (nombre!="")?nombre+", "+ TerceroA[0][4]:nombre + TerceroA[0][4];
      // nombre = TerceroA[0][4] + " " + TerceroA[0][7] + " " + TerceroA[0][5] + " " + 
      //   TerceroA[0][8] + " " + TerceroA[0][6];
      vectorCampos = ['codTerc','numModifTerc','txtIdTercero','txtVersion','codTipoDoc','txtDNI','txtInteresado',
                      'txtApell1','txtApell2','txtPart','txtPart2','txtTelefono','txtCorreo'];
      vectorDatos = [TerceroA[0][0],TerceroA[0][1],TerceroA[0][0],TerceroA[0][1],TerceroA[0][2],TerceroA[0][3],nombre,
        TerceroA[0][5],TerceroA[0][6],TerceroA[0][7],TerceroA[0][8],TerceroA[0][9],TerceroA[0][10]];
      rellenarTercero(vectorDatos,vectorCampos);
      capaVisible = false;
    }	
}
         
function pulsarLimpiar() {
    document.forms[0].fechaAnotacion.value = '';
    document.forms[0].horaMinAnotacion.value = '';
    document.forms[0].asunto.value = '';
    document.forms[0].txtExp1.value = '';
    comboUor.selectItem(-1);
    comboTipoDoc.selectItem(-1);
    comboProcedimiento.selectItem(-1);
    document.forms[0].txtDNI.value = '';
    document.forms[0].txtDomicilio.value = '';
    document.forms[0].txtCP.value = '';
    document.forms[0].txtMuni.value = '';
    document.forms[0].txtProv.value = '';
    document.forms[0].txtCorreo.value = '';
    document.forms[0].txtTelefono.value = '';
    document.forms[0].txtInteresado.value = '';
    
}

function pulsarRegistrarAltaAcc() {
    var validado = false;
    validado = validaObligatorios();
    if (validado) {        
        if (document.forms[0].fechaAnotacion.value == '') {
            document.forms[0].fechaAnotacion.value = '<c:out value="${requestScope.fechaActual}"/>';
        }        
        document.forms[0].opcion.value = "aceptarAlta";
        document.forms[0].target="mainFrame";
        document.forms[0].action="<html:rewrite page='/registroExterno/AltaRegistroExterno.do'/>";
        document.forms[0].submit();       
    }
}

function validaObligatorios() {
    var validado = false;
    if ((document.forms[0].asunto.value == '') ||
        (comboTipoDoc.selectedIndex == -1) || (comboUor.selectedIndex == -1) ||
        (document.forms[0].txtDNI.value == '')) {
        jsp_alerta('A', "Faltan datos obligatorios");        
    } else if ((document.forms[0].txtInteresado.value == '') || (document.forms[0].txtDomicilio.value == '')) {
        jsp_alerta('A', "Datos del interesado incompletos");        
    } else { validado = true; }
    
    return validado;
}

/********************************************************************/
/* HASTA AQUI EL NUEVO JAVASCRIPT					*/
/********************************************************************/            
            var EsquemaGenerico = "<%=es.altia.agora.business.util.GlobalNames.ESQUEMA_GENERICO%>"
            
            // almacenar en vars algunos mensajes de la app
            mensajeCodigoIncorrecto ='<%=descriptor.getDescripcion("msjNoCodigo")%>'; // Idioma
            var mensajeFecha='<%=descriptor.getDescripcion("fechaNoVal")%>';
            var mensajeHora = '<%=descriptor.getDescripcion("horaNoVal")%>';
                          
        // Para operaciones duplicar y contestar.
        var ejercicioBuscada='';
        var numeroBuscada='';
        
        // Listas de valores.

        
        
        var cod_estadosAnotaciones= new Array(0,1,2,9,-9);
        var desc_estadosAnotaciones= new Array('<%=descriptor.getDescripcion("etiq_pendientes")%>',
            '<%=descriptor.getDescripcion("etiq_aceptadas")%>',
            '<%=descriptor.getDescripcion("etiq_rechazadas")%>',
            '<%=descriptor.getDescripcion("etiq_anuladas")%>',
            '<%=descriptor.getDescripcion("etiq_noAnuladas")%>');
            
            
            <% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion)) {%>
            cod_tipoEntrada = [0,1];
            desc_tipoEntrada = ['<%=descriptor.getDescripcion("entradaOrd")%>'
                ,'<%=descriptor.getDescripcion("salidaSIR")%>'];
                <% } else {%>
                cod_tipoEntrada = [0,1,2];
                desc_tipoEntrada = ['<%=descriptor.getDescripcion("entradaOrd")%>','<%=descriptor.getDescripcion("DestOtroReg")%>',
                    '<%=descriptor.getDescripcion("procOtroReg")%>'];
                    <% }%>
                    //  Campos de la tabla Terceros
                    var Terceros = new Array();
                    
                    var tab;
                    var activado = false;
                    
                    // Funciones rejilla
                    var lista= new Array();
                    var i;
                    var cont = 0;
                    
                    
                    //Variable para saber si le doy a pulsarBuscar o pulsarCancelarModificar
                    var mod;
                    
                    var fechaHoy;
                    var fecha;
                    
                    var modificarGrabar=0;
                    
                    function cargarListaTipoDocs(){
                        inicializarValores('codTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);
                    }
                    
                    function mostrarDescripcionTipoDoc(){
                        if (!(Trim(document.forms[0].codTipoDoc.value) == '')) {
                            actualizarDescripcion('codTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);
                        } else document.forms[0].descTipoDoc.value="";
                    }
                    
                    function recuperaListaProcedimientos(cP,dP) {
                        inicializarValores('cod_procedimiento','desc_procedimiento',cP,dP);
                    }
                    
                    
                    
                    
                    // ------ Funciones de página
                    
                    // Diligencias anulacion.
                    var estadoAnot;
                    var diligenciaAnulacion="";
                    

                    
                    function habilitarDocumento(valor){
                        var vectorI =	["txtDNI"];
                        habilitarGeneralInputs(vectorI,valor);
                    }
                    
                    function analizarDocumento(){
                        if(document.forms[0].codTipoDoc.value =="0"){
                            habilitarDocumento(false);
                        }
                        else{habilitarDocumento(true);}
                    }
                    
                    
                    <% if (permisoMantenimiento) {%>
                    /********************************************************************/
                    /* FUNCION PULSAR ALTA 												*/
                    /********************************************************************/
                    
                    function pulsarAlta() {
                        document.forms[0].opcionAltaDesdeConsulta.value="NO";
                        ocultarLista();
                        ocultarCalendario();
                        borrarPaginaBuscada();
                        clean();
                        consultando=false;
                        <% if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {%>
                        document.forms[0].opcion.value="init_alta_entrada";
                        <% } else {%>
                        document.forms[0].opcion.value="init_alta_salida";
                        <% }%>
                        document.forms[0].target="oculto";
                        document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
                        document.forms[0].submit();
                    }
                    /********************************************************************/
                    /* FUNCION PULSAR CANCELAR ALTA										*/
                    /********************************************************************/
                    
                    function pulsarCancelarAlta() {
                        
                        if (document.forms[0].opcionAltaDesdeConsulta.value=="SI")
                            {pulsarCancelarModificar();}
                        else
                            {
                                ocultarLista();
                                ocultarCalendario();
                                borrarPaginaBuscada();
                                document.forms[0].codTerc.value="0";
                                document.forms[0].codDomTerc.value="0";
                                document.forms[0].numModifTerc.value="0";
                                clean();
                                inicializar();
                                modificando('N');
                                document.forms[0].horaMinAnotacion.value="";
                                if (document.getElementById("capaTiposEstadoAnotacion"))
                                    document.getElementById("capaTiposEstadoAnotacion").style.visibility='visible';
                            }
                        }
                        
                        
                        /********************************************************************/
                        /* FUNCION PULSAR ALTA DESDE CONSULTA								*/
                        /********************************************************************/
                        
                        function pulsarAltaDesdeConsulta() {
                            document.forms[0].opcionAltaDesdeConsulta.value="SI";
                            desactivarNavegacion();
                            ocultarLista();
                            ocultarCalendario();
                            borrarPaginaBuscada();
                            clean();
                            consultando=false;
                            <% if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {%>
                            document.forms[0].opcion.value="init_alta_entrada";
                            <% } else {%>
                            document.forms[0].opcion.value="init_alta_salida";
                            <% } %>
                            document.forms[0].target="oculto";
                            document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
                            document.forms[0].submit();
                        }
                        
                        <% }%>
                        
                        /********************************************************************/
                        /* FUNCION NO RECUPERA DATOS										*/
                        /********************************************************************/
                        
                        function noRecuperaDatos()
                        {
                            borrarPaginaBuscada();
                            clean();
                            desactivarFormulario();
                            deshabilitarBuscar();
                            ocultarInfSoloConsulta();
                            modificando('N');
                            if ( document.getElementById("capaBotones3") ) {
                                document.getElementById("capaBotones3").style.visibility = 'visible'
                                document.getElementById("capaCunho").style.visibility = 'visible';
                                document.forms[0].cmdCancelarBuscada.disabled=false;
                                document.forms[0].cmdCuneus.disabled=false;
                                if (document.forms[0].cmdModificar) document.forms[0].cmdModificar.disabled=true;
                                if (document.forms[0].cmdAnular) document.forms[0].cmdAnular.disabled=true;
                                if (document.forms[0].cmdDuplicar) document.forms[0].cmdDuplicar.disabled=true;
                                if (document.forms[0].cmdAltaDesdeConsulta) document.forms[0].cmdAltaDesdeConsulta.disabled=true;
                                if (document.forms[0].cmdContestar)
                                    document.forms[0].cmdContestar.disabled=true;
                            }
                            if (document.getElementById('capaBotonesNuevaConsulta').style.visibility = 'visible')
                                document.forms[0].cmdListado.disabled=false;
                        }
                        
                        /********************************************************************/
                        /* FUNCION RECUPERA DATOS											*/
                        /********************************************************************/
                        
                        function recuperaDatos(datos,datosTercero,listaTemas,lista_CODtiposDocumentos, lista_DESCtiposDocumentos,lista_CODactuaciones,
                            lista_DESCactuaciones, lista_CODtiposTransportes, lista_DESCtiposTransportes, lista_CODtiposRemitentes,
                            lista_DESCtiposRemitentes, lista_CODtemas, lista_DESCtemas,lista_CODidInteresado, lista_DESCidInteresado,
                            lista_CODdpto, lista_DESCdpto, fecha, listaDocs, listFormularios, listFormulariosOriginal)
                        {
                            
                            fechaHoy = fecha;
                            if (unescape(datos[60])=='null') {
                                document.forms[0].observaciones.value = '';
                            } else {
                            document.forms[0].observaciones.value =unescape(datos[60]);
                        }
                        //Borrar interesado.
                        document.forms[0].codTipoDoc.value='';
                        document.forms[0].descTipoDoc.value='';
                        borrarInteresado1();
                        
                        cod_tiposIdInteresado = lista_CODidInteresado;
                        desc_tiposIdInteresado = lista_DESCidInteresado;
                        cod_departamentos = lista_CODdpto;
                        desc_departamentos = lista_DESCdpto;
                        if(datos[51] == "si") {
                            document.getElementById("capaDiligencia").style.visibility = 'visible';
                            document.forms[0].botonDiligencias.disabled=false;
                            habilitarImagenBoton("capaDiligencia",true);
                        } else document.getElementById("capaDiligencia").style.visibility = 'hidden';
                        
                        document.forms[0].fechaAnotacion.value= datos[1] +"/"+datos[2]+"/"+datos[3];
                        //document.forms[0].fechaDocumento.value= datos[4] +"/"+datos[5]+"/"+datos[6];
                        <% if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {%>
                        document.forms[0].horaMinDocumento.value= datos[7] + ":"+ datos[8];
                        <% }%>
                        document.forms[0].asunto.value = unescape(datos[9]);
                        document.forms[0].cod_tipoTransporte.value = datos[10];
                        document.forms[0].txtCodigoDocumento.value = datos[11];
                        document.forms[0].cbTipoEntrada.value = datos[12];
                        actualizarDescripcion('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);
                        mostrarDestino();
                        document.forms[0].cod_tipoRemitente.value = datos[13];
                        document.forms[0].txtNumTransp.value = datos[14];
                        //document.forms[0].cod_departamentoOrixe.value = datos[17];
                        document.forms[0].cod_unidadeRexistroOrixe.value = datos[18];
                        //document.forms[0].cod_dptoDestino.value = datos[15];
                        document.forms[0].cod_uniRegDestino.value = datos[16];
                        document.forms[0].cod_orgDestino.value = datos[19];
                        //document.forms[0].cod_entDestino.value = datos[20];
                        document.forms[0].cod_orgOrigen.value = datos[21];
                        //document.forms[0].cod_entidadOrigen.value = datos[22];
                        document.forms[0].txtNomeDocumento.value = datos[23];
                        document.forms[0].txtNomeTipoRemitente.value = datos[24];
                        document.forms[0].desc_tipoTransporte.value = datos[25];
                        document.forms[0].cod_actuacion.value = datos[26];
                        document.forms[0].txtNomeActuacion.value = datos[27];
                        document.forms[0].desc_orgDestino.value = datos[28];
                        //document.forms[0].desc_entDestino.value = datos[29];
                        //document.forms[0].desc_dptoDestino.value = datos[30];
                        document.forms[0].desc_uniRegDestino.value = datos[31];
                        document.forms[0].desc_orgOrigen.value = datos[32];
                        //document.forms[0].desc_entidadOrigen.value = datos[33];
                        //document.forms[0].desc_departamentoOrixe.value = datos[34];
                        document.forms[0].desc_unidadeRexistroOrixe.value = datos[35];
                        document.forms[0].tipoRegistroOrigen.value = datos[36];
                        document.forms[0].txtExp1Orixe.value = datos[38];
                        var apañito = datos[37];
                        document.forms[0].cod_uniRegDestinoORD.value = datos[42]; // cod UOR
                        document.forms[0].cod_uor.value = datos[43]; // cod visible
                        document.forms[0].ano.value=datos[44];
                        document.forms[0].numero.value=datos[45];
                        document.forms[0].horaMinAnotacion.value=datos[46]+":"+datos[47];
                        var igual=datos[48];
                        //document.forms[0].desc_dptoDestinoORD.value = datos[49]; TODO rmved
                        document.forms[0].desc_uniRegDestinoORD.value = datos[50];
                        textoDil = datos[52];
                        document.forms[0].autoridad.value = unescape(datos[61]);
                        
                        document.forms[0].cmdCancelarBuscada.disabled=false;
                        document.forms[0].cmdCuneus.disabled=false;
                        if (document.forms[0].cmdDuplicar) document.forms[0].cmdDuplicar.disabled=false;
                        if (document.forms[0].cmdAltaDesdeConsulta) document.forms[0].cmdAltaDesdeConsulta.disabled=false;
                        if (document.forms[0].cmdContestar) document.forms[0].cmdContestar.disabled=false;
                        if(igual == "cerrado") {
                            if (document.forms[0].cmdModificar) document.forms[0].cmdModificar.disabled=true;
                            if (document.forms[0].cmdAnular) document.forms[0].cmdAnular.disabled=true;
                        } else {
                        if (document.forms[0].cmdModificar) document.forms[0].cmdModificar.disabled=false;
                        if (document.forms[0].cmdAnular) document.forms[0].cmdAnular.disabled=false;
                    }
                    lista=listaTemas;
                    tab.lineas=lista;
                    refresh("tab");
                    
                    recuperaBusquedaTerceros(datosTercero);
                    
                    cod_tiposDocumentos = lista_CODtiposDocumentos;
                    desc_tiposDocumentos = lista_DESCtiposDocumentos;
                    cod_tiposTransportes = lista_CODtiposTransportes;
                    desc_tiposTransportes= lista_DESCtiposTransportes;
                    cod_tiposRemitentes= lista_CODtiposRemitentes;
                    desc_tiposRemitentes= lista_DESCtiposRemitentes;
                    cod_temas=lista_CODtemas;
                    desc_temas=lista_DESCtemas;
                    
                    document.forms[0].codTerc.value = datos[39];
                    document.forms[0].codDomTerc.value = datos[40];
                    document.forms[0].numModifTerc.value = datos[41];
                    
                    if(modificarGrabar != 1) {
                        document.getElementById("capaBotones3").style.visibility = 'visible';
                        document.getElementById("capaCunho").style.visibility = 'visible';
                        modificarGrabar = 0;
                    }
                    if(mod == 1) {
                        habilitarBuscar();
                        document.getElementById("capaBotones3").style.visibility = 'visible';
                        document.getElementById("capaCunho").style.visibility = 'visible';
                        document.getElementById("capaBotones4").style.visibility = 'hidden';
                    }
                    else {
                        deshabilitarBuscar();
                    }
                    tp1.setSelectedIndex(0);
                    
                    // ANULADAS
                    estadoAnot = datos[53];
                    diligenciaAnulacion ='';
                    if (estadoAnot == 9 )  {
                        diligenciaAnulacion = unescape(datos[54]);
                        domlay('capaEstado',1,0,0,'');
                        document.getElementById('botonDiligenciasAnulacion').disabled=false;
                        if (igual != "cerrado") {
                            domlay('capaDesAnular', 1,0,0,'');
                            if (document.getElementById('botonRecuperarAnulada')) document.getElementById('botonRecuperarAnulada').disabled=false;
                        } else {
                        domlay('capaDesAnular', 0,0,0,'');
                        if (document.getElementById('botonRecuperarAnulada')) document.getElementById('botonRecuperarAnulada').disabled=true;
                    }
                    if (document.forms[0].cmdModificar) document.forms[0].cmdModificar.disabled=true;
                    if (document.forms[0].cmdAnular) document.forms[0].cmdAnular.disabled=true;
                } else if (estadoAnot == 1) { // No se pueden modificar.
                if (document.forms[0].cmdModificar) document.forms[0].cmdModificar.disabled=true;
                if (document.forms[0].cmdAnular) document.forms[0].cmdAnular.disabled=true;
            } else {
            domlay('capaEstado',0,0,0,'');
            domlay('capaDesAnular',0,0,0,'');
        }
        
        // CONTESTADAS
        <% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion)) {%>
        if ( (datos[55]!='') && (datos[56]!='') ) {
            mostrarCapaAnotacionContestada(true, datos[55], datos[56], '<%= descriptor.getDescripcion("gEtiqAnotContest") %>');
            } else mostrarCapaAnotacionContestada(false,' ',' ','');
            <% }%>
            
            
            /* Enlace con SGE.
            Fecha: 22/07/2003. */
            document.forms[0].txtExp1.value=datos[57];
            document.forms[0].cod_procedimiento.value=datos[58];
            document.forms[0].desc_procedimiento.value=datos[59];
        }
        /********************************************************************/
        /* FUNCION RECUPERA DATOS 1 										*/
        /********************************************************************/
        
        function recuperaDatos1(datos,lista_CODtiposDocumentos, lista_DESCtiposDocumentos,lista_CODactuaciones, lista_DESCactuaciones,
            lista_CODtiposTransportes, lista_DESCtiposTransportes, lista_CODtiposRemitentes, lista_DESCtiposRemitentes,
            lista_CODtemas, lista_DESCtemas,lista_CODidInteresado, lista_DESCidInteresado,lista_CODdpto, lista_DESCdpto) {
        document.forms[0].fechaAnotacion.value = datos[1]+"/"+datos[2]+"/"+datos[3];
        document.forms[0].horaMinAnotacion.value=datos[4]+":"+datos[5];
//        document.forms[0].fechaDocumento.value = datos[1]+"/"+datos[2]+"/"+datos[3];
        <%
            if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {
%>
    document.forms[0].horaMinDocumento.value=datos[4]+":"+datos[5];
    <% }%>
    
    cod_tiposDocumentos = lista_CODtiposDocumentos;
    desc_tiposDocumentos = lista_DESCtiposDocumentos;
    cod_tiposTransportes = lista_CODtiposTransportes;
    desc_tiposTransportes= lista_DESCtiposTransportes;
    cod_tiposRemitentes= lista_CODtiposRemitentes;
    desc_tiposRemitentes= lista_DESCtiposRemitentes;
    cod_tiposIdInteresado = lista_CODidInteresado;
    desc_tiposIdInteresado = lista_DESCidInteresado;
    cod_departamentos = lista_CODdpto;
    desc_departamentos = lista_DESCdpto;
    cod_temas=lista_CODtemas
    desc_temas=lista_DESCtemas
    
    <% if ("reservas".equals(respOpcion)) {%>
    document.forms[0].cmdCancelarBuscada.disabled=false;
    document.forms[0].cmdCuneus.disabled=false;
    if (document.forms[0].cmdModificar) document.forms[0].cmdModificar.disabled=false;
    if (document.forms[0].cmdAnular) document.forms[0].cmdAnular.disabled=true;
    if (document.forms[0].cmdDuplicar) document.forms[0].cmdDuplicar.disabled=true;
    if (document.forms[0].cmdAltaDesdeConsulta) document.forms[0].cmdAltaDesdeConsulta.disabled=true;
    if (document.forms[0].cmdContestar) document.forms[0].cmdContestar.disabled=true;
    <% } else {%>
    if (document.forms[0].cmdAnular) document.forms[0].cmdAnular.disabled=false;
    if (document.forms[0].cmdDuplicar) document.forms[0].cmdDuplicar.disabled=false;
    if (document.forms[0].cmdAltaDesdeConsulta) document.forms[0].cmdAltaDesdeConsulta.disabled=false;
    <% }%>
    desactivarOrigenYExpediente();
    if(mod == 1) {
        habilitarBuscar();
        mostrarCapasBotones('capaBotones3');
        document.getElementById("capaCunho").style.visibility = 'visible';
    }
    tp1.setSelectedIndex(0);
}

/********************************************************************/
/* FUNCION ANOTACION RECUPERADA										*/
/********************************************************************/

function anotacionRecuperada(mnsj) {
    jsp_alerta("A", mnsj);
    habilitarBuscar();
    document.forms[0].tipoConsulta[1].checked=true;
    document.forms[0].opcion.value="recargar_consulta";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
    document.forms[0].submit();
}


<% if (permisoMantenimiento) {%>
    /********************************************************************/
    /* FUNCION REGISTRAR MODIFICAR										*/
    /********************************************************************/
    
    function pulsarRegistrarModificar() {
        var t = esUorBaja(document.forms[0].cod_uniRegDestinoORD.value);
        //alert('esUorBaja devuelve ' + t + ' para'  + document.forms[0].cod_uniRegDestinoORD.value);
        
        if(t == null) {
            alert('Problema tecnico al registrar');
            return;
        }
        else if(t == true) {
            jsp_alerta("A", '<%=descriptor.getDescripcion("msjNoGrabarUORBaja")%>');
                return;
            }
            
            if (validarObligatoriosAqui()) {
                if ( (comprobarFecha(document.forms[0].fechaAnotacion)) && (comprobarFecha(document.forms[0].fechaDocumento) ) ){
                    
                    // Comprueba si la fecha de entrada es posterior a la actual
                    if (comparaFecha()) {
                        var l = new Array();
                        for (i=0; i < lista.length; i++) {
                            l[i]=lista[i][0]+'§¥'; // Solo códigos
                        }
                        if(document.forms[0].cod_procedimiento.value != "" && document.forms[0].txtExp1.value =="") {
                            jsp_alerta("",'<%=descriptor.getDescripcion("msjProcSinExp")%>');
                            } else {
                            document.forms[0].listaTemas.value=l;
                            document.forms[0].opcion.value="grabarModificaciones";
                            document.forms[0].target="oculto";
                            document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
                            document.forms[0].submit();
                            desactivarFormulario();
                            modificando('N');
                        }
                    } else {
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoGrabarAnot")%>');
                    }
                }
            }
        }
        
        function pulsarRegistrarModificar2() {
            
            var t = esUorBaja(document.forms[0].cod_uniRegDestinoORD.value);
            //alert('esUorBaja devuelve ' + t + ' para'  + document.forms[0].cod_uniRegDestinoORD.value);
            
            if(t == null) {
                alert('Problema tecnico al registrar');
                return;
            }
            else if(t == true) {
                jsp_alerta("A", '<%=descriptor.getDescripcion("msjNoGrabarUORBaja")%>');
                    return;
                }
                
                if (validarObligatoriosAqui()) {
                    if ( (comprobarFecha(document.forms[0].fechaAnotacion)) && (comprobarFecha(document.forms[0].fechaDocumento) ) ){
                        var l = new Array();
                        for (i=0; i < lista.length; i++) {
                            l[i]=lista[i][0]+'§¥'; // Solo códigos
                        }
                        if(document.forms[0].cod_procedimiento.value != "" && document.forms[0].txtExp1.value =="") {
                            jsp_alerta("",'<%=descriptor.getDescripcion("msjProcSinExp")%>');
                            } else {
                            pulsarImprimirCuneus('si');
                        }
                    }
                }
            }
            
            
            /********************************************************************/
            /* FUNCION PULSAR BUZON												*/
            /********************************************************************/
            
            function pulsarBuzon() {
                document.forms[0].action="<%=request.getContextPath()%>/Buzon.do";
                document.forms[0].target="oculto";
                document.forms[0].submit();
            }
            
            <% }%>
            
            <% if (permisoMantenimiento) {%>
            
            // ---------- contestar
            <% if (permisoMantenimientoSalida) {%>
            <% 	if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {%>
            <% }
     }
     if (contestacion) {%>
             /********************************************************************/
             /* FUNCION CARGAR LISTAS											*/
             /********************************************************************/
             
             function cargarListas() {
                 document.forms[0].codTerc.value= '<bean:write name="MantAnotacionRegistroForm" property="codTerc"/>';
                 document.forms[0].codDomTerc.value= '<bean:write name="MantAnotacionRegistroForm" property="codDomTerc"/>';
                 document.forms[0].numModifTerc.value= '<bean:write name="MantAnotacionRegistroForm" property="numModifTerc"/>';
                 cargarSoloListas();
                 activarFormulario();
                 deshabilitarBuscar();
                 desactivarOrigenYExpediente();
                 desactivarInteresado();
                 ocultarInfSoloConsulta();
                 
                 tp1.setSelectedIndex(0);
                 mostrarDestino();
                 actualizarDescripcion('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);
                 document.forms[0].asunto.value = unescape('<bean:write name="MantAnotacionRegistroForm" property="asunto"/>');
             }
             <% } %>
             
             <% }%>

             
             <% if (permisoMantenimiento) {%>
             
             // devuelve true si el codigo de la uor del argumento esta de baja
             function esUorBaja(uor_a_buscar) {
                 
                 var encontrado = buscarUorPorCod(uor_a_buscar);
                 if(encontrado == null) {
                     return false;
                 }
                 
                 
                 if(encontrado.uor_estado == 'B') {
                     return true;
                 }
                 else if(encontrado.uor_estado == 'A') {
                     return false;
                 }
                 
                 return null;
             }
             
 function validarObligatoriosAqui() {
     desactivarCampoDocumento();
     var msjObl = '<%=descriptor.getDescripcion("msjObligTodos")%>';
     if (!validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>'))
         return false;
         //Combos.
         if (document.forms[0].cbTipoEntrada.value != 0 && document.forms[0].cbTipoEntrada.value != 1
             && document.forms[0].cbTipoEntrada.value != 2)
         {
             jsp_alerta('A','<%=descriptor.getDescripcion("msjAbrirCond10")%>');
                 tp1.setSelectedIndex(0);
                 document.forms[0].cbTipoEntrada.focus();
                 return false;
             }

             // Se asigna valor por defecto la hora actual tanto a la hora de entrada como a la de presentación
             var ahora = getHoraMin();
             if ((document.forms[0].horaMinAnotacion.value == null) || (document.forms[0].horaMinAnotacion.value=="")) {
                 document.forms[0].horaMinAnotacion.value = ahora;
             }

             // Solo si estamos en el registro de entrada asignamos valor a hora presentacion y hacemos comprobaciones
             <% if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {%>

             if ((document.forms[0].horaMinDocumento.value == null) || (document.forms[0].horaMinDocumento.value=="")) {
                 document.forms[0].horaMinDocumento.value = ahora;
             }  	 
             // Tomamos la fecha/hora de entrada.                        
             var fechaEntr = new Date(document.forms[0].fechaAnotacion.value.substring(6,10), 
                 eval(document.forms[0].fechaAnotacion.value.substring(3,5)-1), 
                 document.forms[0].fechaAnotacion.value.substring(0,2),
                 document.forms[0].horaMinAnotacion.value.substring(0,2),
                 document.forms[0].horaMinAnotacion.value.substring(3,5));

                 // ---> Hora del documento. Partimos de que el formato esta bien.
                 var h = document.forms[0].horaMinDocumento.value.substring(0,2);
                 var m = document.forms[0].horaMinDocumento.value.substring(3,5);
                 if ((h < 0) || (h > 23))
                     {
                         jsp_alerta('A','<%=descriptor.getDescripcion("msjAnotCond1")%>');
                             tp1.setSelectedIndex(0);
                             document.forms[0].horaMinDocumento.focus();
                             return false;
                         }

                         if ((m < 0) ||(m > 59))
                             {
                                 jsp_alerta('A','<%=descriptor.getDescripcion("msjAnotCond1")%>');
                                     tp1.setSelectedIndex(0);
                                     document.forms[0].horaMinDocumento.focus();
                                     return false;
                                 }

                                 <% }%>

                                 // ---> Terceros.
                                 if ( (document.forms[0].codTerc.value == "0") || (document.forms[0].codTerc.value == "") || (document.forms[0].codTerc.value == null)
                                     || (document.forms[0].codDomTerc.value == "0") || (document.forms[0].codDomTerc.value == "") || (document.forms[0].codDomTerc.value == null)
                                     || (document.forms[0].numModifTerc.value == "0") || (document.forms[0].numModifTerc.value == "") || (document.forms[0].numModifTerc.value == null) ) {
                                 jsp_alerta('A','<%=descriptor.getDescripcion("msjAnotCondTerc")%>');
                                     tp1.setSelectedIndex(1);
                                     document.forms[0].txtDNI.focus();
                                     return false;

                                 }

                                 // ---> DESTINO
                                 <% if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {%>
                                 if (document.forms[0].cbTipoEntrada.value==0) {
                                     /*if (Trim(document.forms[0].cod_dptoDestinoORD.value) == '')
                                     {
                                         jsp_alerta('A','<%=descriptor.getDescripcion("msjAnotDestOrdDpto")%>');
                                             tp1.setSelectedIndex(2);
                                             document.forms[0].cod_dptoDestinoORD.focus();
                                             return false;
                                         }*/
                                         onchangeCod_uniRegDestinoORD();
                                         if (Trim(document.forms[0].cod_uniRegDestinoORD.value)=='')
                                             {
                                                 jsp_alerta('A','<%=descriptor.getDescripcion("msjAnotDestOrdUni")%>');
                                                     tp1.setSelectedIndex(0);
                                                     document.forms[0].cod_uor.focus(); // TODO el otro no aceptaba el focus
                                                     return false;

                                                 }
                                             }
                                             else if (document.forms[0].cbTipoEntrada.value ==1){    
                                                 if (Trim(document.forms[0].cod_orgDestino.value) == '')
                                                     {
                                                         jsp_alerta('A','<%=descriptor.getDescripcion("msjAnotDestOtrRegOrg")%>');
                                                             tp1.setSelectedIndex(2);
                                                             document.forms[0].cod_orgDestino.focus();
                                                             return false;
                                                         }
                                                         /*if (Trim(document.forms[0].cod_entDestino.value)=='')
                                                         {
                                                             jsp_alerta('A','<%=descriptor.getDescripcion("msjAnotDestOtrRegEnt")%>');
                                                                 tp1.setSelectedIndex(2);
                                                                 document.forms[0].cod_entDestino.focus();
                                                                 return false;

                                                             }*/
                                                         } else if (document.forms[0].cbTipoEntrada.value ==2){
                                                         onchangeCod_uniRegDestinoORD();
                                                         if (Trim(document.forms[0].cod_uniRegDestinoORD.value)=='')
                                                             {
                                                                 jsp_alerta('A','<%=descriptor.getDescripcion("msjAnotDestOrdUni")%>');
                                                                     tp1.setSelectedIndex(2);
                                                                     document.forms[0].cod_uor.focus();
                                                                     return false;
                                                                 }
                                                                     }
                                                                     else { // No hay un tipo de entrada seleccionado
                                                                         jsp_alerta('A','FALTA TIPO ENTRADA');
                                                                         tp1.setSelectedIndex(0);
                                                                         document.forms[0].cbTipoEntrada.select();
                                                                         return false;
                                                                     }
                                                                     <% } else {%>
                                                                     document.forms[0].cbTipoEntrada.value ==0; // Salida siempre ordinaria ?
                                                                     /*if (Trim(document.forms[0].cod_dptoDestinoORD.value) == '')
                                                                     {
                                                                         jsp_alerta('A','<%=descriptor.getDescripcion("msjAnotDestOrdDpto")%>');
                                                                             tp1.setSelectedIndex(2);
                                                                             document.forms[0].cod_dptoDestinoORD.focus();
                                                                             return false;
                                                                         }*/
                                                                         onchangeCod_uniRegDestinoORD();
                                                                         if (Trim(document.forms[0].cod_uniRegDestinoORD.value)=='')
                                                                             {
                                                                                 jsp_alerta('A','<%=descriptor.getDescripcion("msjAnotDestOrdUni")%>');
                                                                                     tp1.setSelectedIndex(2);
                                                                                     document.forms[0].cod_uor.focus();
                                                                                     return false;
                                                                                 }
                                                                                 <% } %>
                                                                                 return true;
                                                                             }

                                                                             <% }%>

 <% if (permisoMantenimiento) {%>
 /********************************************************************/
 /* FUNCION REGISTRAR ALTA											*/
 /********************************************************************/

 function pulsarRegistrarAlta() {
     var t = esUorBaja(document.forms[0].cod_uniRegDestinoORD.value);    

     if(t == null) {
         alert('Problema tecnico al registrar');
         return;
     }
     else if(t == true) {
         jsp_alerta("A", '<%=descriptor.getDescripcion("msjNoGrabarUORBaja")%>');
             return;
         }

         if (validarObligatoriosAqui())
             {
                 if ( (comprobarFecha(document.forms[0].fechaAnotacion)) && (comprobarFecha(document.forms[0].fechaDocumento) ) ){

                     if (comparaFecha() ) {
                         document.forms[0].opcion.value="registrar_alta_confirmada";
                         <% if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {%>
                         document.forms[0].opcion.value="registrar_alta_entrada";
                         <% } else {%>
                         document.forms[0].opcion.value="registrar_alta_salida";
                         <% }%>

                         // TEMAS
                         var l = new Array();
                         for (i=0; i < lista.length; i++)
                             l[i]=lista[i][0]+'§¥'; // Solo códigos
                         if(document.forms[0].cod_procedimiento.value != "" && document.forms[0].txtExp1.value =="") {
                             jsp_alerta("",'<%=descriptor.getDescripcion("msjProcSinExp")%>');
                             } else {
                             document.forms[0].listaTemas.value=l;
                             document.forms[0].target="oculto";
                             if (document.forms[0].opcionAltaDesdeConsulta.value=="SI")
                                 {
                                     document.forms[0].pendienteBuzon.value="no";
                                 }
                                 document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";

                                 document.forms[0].submit();
                             }
                         }else{
                         jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoGrabarAnot")%>');
                         }
                     }
                 }
             }

 <% }%>
 /********************************************************************/
 /* FUNCION BUSCAR DOC TIPO DOC										*/
 /********************************************************************/

 function buscarDocTipoDoc() {

     var tipo = document.forms[0].codTipoDoc.value;
     if (!documentoNoValido("codTipoDoc","txtDNI",1)){
         document.forms[0].txtDNI.value=document.forms[0].txtDNI.value.toUpperCase();
         document.forms[0].opcion.value="buscar";
         document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
         document.forms[0].target="oculto";
         document.forms[0].submit();
     }
 }
 <% if (permisoMantenimiento) {%>
function abrirTerceros() {
    var codTercero = document.forms[0].codTerc.value;
    var source; 
    if ((codTercero!=null) && (codTercero!='') && (codTercero!='0')) {
         source = '<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializar&destino=registroAlta&tipo=B&codTerc='+codTercero;
    }else{
         source = '<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializar&destino=registroAlta&tipo=NoAlta&descDoc=&docu=';
    }
    abrirXanelaAuxiliar(('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,'terceros',
	'width=990,height=650,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            vectorCampos = ['codTerc','numModifTerc','txtIdTercero','txtVersion','codTipoDoc','txtDNI','txtInteresado',
                                'txtApell1','txtApell2','txtPart','txtPart2','txtTelefono','txtCorreo','codDomTerc',
                                'txtIdDomicilio','txtPais','txtProv','txtMuni','txtDomicilio','txtCP','txtPoblacion'];
                            rellenarTercero(datos,vectorCampos);
                            var vector = new Array(document.forms[0].txtInteresado);
                            deshabilitarGeneral(vector);
                        }
                 });
 }
 <% }%>
     /********************************************************************/
     /* FUNCION PULSAR BUSCAR TERCEROS									*/
     /********************************************************************/

     function pulsarBuscarTerceros(){
         if (document.forms[0].codTipoDoc.value == <%=m_Conf.getString("tercero.codUOR")%>) {
             var datos;
             var argumentos = new Array();
             argumentos[0] = document.forms[0].txtDNI.value;
             abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + APP_CONTEXT_PATH + 
                     "/MantAnotacionRegistro.do?opcion=seleccionUOR",argumentos,
                     'width=990,height=650',function(datos){
                            if(datos != null) {
                                document.forms[0].txtDNI.value = datos[0].toUpperCase();
                                document.forms[0].opcion.value="buscar";
                                document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                document.forms[0].target="oculto";
                                document.forms[0].submit();
                            }
                    });
         } else {
         var argumentos = new Array();
         argumentos['modo'] = 'seleccion';
         argumentos['terceros'] = [];
         var source = '<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializarBusquedaTerc&ventana=true';
         abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,argumentos,
	'width=990,height=650,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            mostrarDatosTercero(datos);
                            var vector = new Array(document.forms[0].txtInteresado);
                            deshabilitarGeneral(vector);
                            document.forms[0].codTerc.value=datos[0];
                        }
                    }
                 });
     }
     
     function mostrarDatosTercero(datos) {
         
         // Mostramos el primer tercero con rol por defecto o
         // el indicado por la variable 'terceroCargado', si no es nula
         var porDefecto=0;
         // relleno los campos del formulario de la pestaa interesados
         document.forms[0].codTerc.value      =datos[0];
         document.forms[0].codDomTerc.value   =datos[4];
         document.forms[0].numModifTerc.value =1;
         document.forms[0].txtInteresado.value=datos[6]; // Razon Social.
         document.forms[0].txtTelefono.value  =datos[11]; // Telefono.
         document.forms[0].txtCorreo.value    =datos[12]; // Email.
         document.forms[0].txtPais.value      =datos[14]; // Pais.
         document.forms[0].txtProv.value      =datos[16]; // Provincia.
         document.forms[0].txtMuni.value      =datos[17]; // Municipio.
         document.forms[0].txtDomicilio.value =datos[18]; // Nombre via
         document.forms[0].txtCP.value        =datos[19]; // Codigo.
         document.forms[0].codTipoDoc.value    =datos[4]; // codDNI.
         document.forms[0].txtDNI.value       =datos[5]; // dni.
         
         // Cambiar el tercero antes de modificar el combo (se produce onchange)
         terceroActual = porDefecto;
         mostrarDescripcionTipoDoc();
     }

     /********************************************************************/
     /* FUNCION RECUPERA BUSQUEDA TERCEROS								*/
     /********************************************************************/
     function recuperaBusquedaTercerosSeleccionar(datos, posTerc, posDom) {
         recuperaBusquedaTerceros(datos);
     }

     function buildRequestInsercionDirectaTercero(datos) {
         var tercAInsertar = new Array();
         var domAInsertar = new Array();
         tercAInsertar = datos;
         domAInsertar = datos[18][0];

         var request = 'txtIdTercero=' + tercAInsertar[0] + '&codTipoDoc=' + tercAInsertar[2] + '&txtDNI=' + tercAInsertar[3] +
         '&txtInteresado=' + tercAInsertar[4] + '&txtApell1=' + tercAInsertar[5] + '&txtApell2=' + tercAInsertar[6] + 
         '&txtPart=' + tercAInsertar[7] + '&txtPart2=' + tercAInsertar[8] + '&txtTelefono=' + tercAInsertar[9] +
         '&txtCorreo=' + tercAInsertar[10] + '&codPais=' + domAInsertar[6] + '&codProvincia=' + domAInsertar[7] +
         '&codMunicipio=' + domAInsertar[8] + '&descVia=' + domAInsertar[29] + '&codTVia=' + domAInsertar[19] + 
         '&txtNormalizado=' + domAInsertar[24] + '&codUso=' + domAInsertar[21] + '&txtCodVia=' + domAInsertar[23] + 
         '&txtNumDesde=' + domAInsertar[9] + '&txtNumHasta=' + domAInsertar[11] + '&domActual=0&descUso=' + domAInsertar[22] +
         '&descTVia=' + domAInsertar[20] + '&descProvincia=' + domAInsertar[1] + '&descMunicipio=' + domAInsertar[2] + 
         '&descPostal=' + domAInsertar[4] + '&txtBarriada=' + domAInsertar[18] + '&txtDomicilio=' + domAInsertar[3] + 
         '&txtLetraDesde=' + domAInsertar[10] + '&txtLetraHasta=' + domAInsertar[12] + '&txtBloque=' + domAInsertar[13] + 
         '&txtPortal=' + domAInsertar[14] + '&txtEsc=' + domAInsertar[15] + '&txtPlta=' + domAInsertar[16] + 
         '&txtPta=' + domAInsertar[17] + '&codESI=' + domAInsertar[27];
         return request;

     }

     function recuperaBusquedaTerceros(datos){
         var MAX=0;

         Terceros = datos;
     if (datos.length>0){
         if (datos.length == 1 && Terceros[0][0] == '0') {
             var request = buildRequestInsercionDirectaTercero(Terceros[0]);
             document.forms[0].opcion.value = "grabarTercDomExterno";
             document.forms[0].target = "oculto";
             document.forms[0].action = '<%=request.getContextPath()%>/BusquedaTerceros.do?posTerc=0&posDom=0&' + request;
             document.forms[0].submit();
         } else {
         cargarListaB(datos,'botonT');
         var v = new Array(document.forms[0].txtInteresado);
         deshabilitarGeneral(v);
        }
        dTipoDoc = document.forms[0].descTipoDoc.value;
        tipoDoc = document.forms[0].codTipoDoc.value;
     }else{
    if (!alta) {
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoRegistros")%>');
        document.forms[0].codTerc.value = '0';
        document.forms[0].codDomTerc.value = '0';
        document.forms[0].numModifTerc.value = '0';
    }
         if(jsp_alerta("",'<%=descriptor.getDescripcion("msjNuevoInteresado")%>')){
                var tipo = document.forms[0].codTipoDoc.value;
                var descDoc = document.forms[0].descTipoDoc.value;
                var docu = document.forms[0].txtDNI.value;
                var source; 
                if (document.getElementById("capaBotones2").style.visibility == 'visible'){
                    source = '<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializar&destino=registroAlta&tipo='+tipo+'&descDoc='+descDoc+'&docu='+docu;
                 }else{
                     source = '<%=request.getContextPath()%>/BusquedaTerceros.do?opcion=inicializar&destino=registroActualizar&tipo='+tipo+'&descDoc='+descDoc+'&docu='+docu;
                 }
                 abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,'terceros',
	'width=990,height=650,status='+ '<%=statusBar%>',function(datos){
                        if(ventana!=undefined){
                            vectorCampos = ['codTerc','numModifTerc','txtIdTercero','txtVersion','codTipoDoc','txtDNI','txtInteresado',
                                'txtApell1','txtApell2','txtPart','txtPart2','txtTelefono','txtCorreo','codDomTerc',
                                'txtIdDomicilio','txtPais','txtProv','txtMuni','txtDomicilio','txtCP','txtPoblacion'];
                            rellenarTercero(ventana,vectorCampos);
                            var vector = new Array(document.forms[0].txtInteresado);
                            deshabilitarGeneral(vector);
                        }
                        if ( (document.forms[0].codTerc.value == "0") || (document.forms[0].codTerc.value == "") || (document.forms[0].codTerc.value == null)
                            || (document.forms[0].codDomTerc.value == "0") || (document.forms[0].codDomTerc.value == "") || (document.forms[0].codDomTerc.value == null)
                            || (document.forms[0].numModifTerc.value == "0") || (document.forms[0].numModifTerc.value == "") || (document.forms[0].numModifTerc.value == null) ) {
                            borrarInteresado();
                        }
                        dTipoDoc = document.forms[0].descTipoDoc.value;
                        tipoDoc = document.forms[0].codTipoDoc.value;
                 });
             }else{
                if ( (document.forms[0].codTerc.value == "0") || (document.forms[0].codTerc.value == "") || (document.forms[0].codTerc.value == null)
                    || (document.forms[0].codDomTerc.value == "0") || (document.forms[0].codDomTerc.value == "") || (document.forms[0].codDomTerc.value == null)
                    || (document.forms[0].numModifTerc.value == "0") || (document.forms[0].numModifTerc.value == "") || (document.forms[0].numModifTerc.value == null) ) {
                    borrarInteresado();
                }
                dTipoDoc = document.forms[0].descTipoDoc.value;
                tipoDoc = document.forms[0].codTipoDoc.value;
            }
     }
 }
 /********************************************************************/
 /* FUNCION BUSCAR DOC RAZON SOCIAL									*/
 /********************************************************************/

 function buscarDocRazonSocial() {
     open('<%=request.getContextPath()%>/jsp/busqRazonSocial.html', 'Sizewindow', 'width=375,height=200,scrollbars=no,toolbar=no')
     }

     /********************************************************************/
     /* FUNCION BORRAR PAGINA BUSCADA									*/
     /********************************************************************/

     function borrarPaginaBuscada() {
         if(mod != 1) {
             document.forms[0].ano.value="";
             document.forms[0].numero.value="";
         }
         document.forms[0].fechaDocumento.value=''; // Fecha documento
         <% 	if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {%>
         document.forms[0].horaMinDocumento.value="";
         <% }%>

         document.forms[0].fechaAnotacion.value=''; // Fecha entrada
         document.forms[0].horaMinAnotacion.value="";
         document.forms[0].asunto.value=''; // Asunto
         document.forms[0].autoridad.value='';
         document.forms[0].txtCodigoDocumento.value=''; // Tipo documento.
         //document.forms[0].txtNomeDocumento.value='';
         actualizarDescripcion('txtCodigoDocumento','txtNomeDocumento',cod_tiposDocumentos,desc_tiposDocumentos);
         document.forms[0].cbTipoEntrada.value=0; // Tipo entrada.
         actualizarDescripcion('cbTipoEntrada','txtNomeTipoEntrada',cod_tipoEntrada,desc_tipoEntrada);
         mostrarDestino();
         document.forms[0].cod_tipoRemitente.value=''; // Tipo remitente.
         document.forms[0].txtNomeTipoRemitente.value='';
         document.forms[0].txtNumTransp.value=''; // Num transporte.
         document.forms[0].cod_tipoTransporte.value=''; // Tipo transporte.
         document.forms[0].desc_tipoTransporte.value='';
         document.forms[0].cod_actuacion.value=''; // Actuacion.
         document.forms[0].txtNomeActuacion.value='';
         // Pestaña 2.
         document.forms[0].codTipoDoc.value=''; // Tipo documento.
         document.forms[0].descTipoDoc.value='';
         document.forms[0].txtDNI.value=''; // Documento.
         document.forms[0].txtInteresado.value=''; // Razon Social.
         document.forms[0].txtApell1.value=''; // Apellidos.
         document.forms[0].txtApell2.value='';
         document.forms[0].txtPart.value=''; // Partículas.
         document.forms[0].txtPart2.value='';
         document.forms[0].txtTelefono.value=''; // Telefono.
         document.forms[0].txtCorreo.value=''; // Email.
         document.forms[0].txtPais.value=''; // Pais.
         document.forms[0].txtProv.value=''; // Provincia.
         document.forms[0].txtMuni.value=''; // Municipio.
         document.forms[0].txtDomicilio.value=''; // Nombre vía.
         document.forms[0].txtPoblacion.value='';
         document.forms[0].txtCP.value=''; // Codigo.
         // Pestaña 3.
         document.forms[0].cod_orgDestino.value=''; // Organizacion.
         document.forms[0].desc_orgDestino.value='';
         //document.forms[0].cod_entDestino.value=''; // Entidad.
         //document.forms[0].desc_entDestino.value='';
         //document.forms[0].cod_dptoDestino.value=''; // Departamento.
         //document.forms[0].desc_dptoDestino.value='';
         document.forms[0].cod_uniRegDestino.value=''; // Unidad de rexistro.
         document.forms[0].desc_uniRegDestino.value='';
         document.forms[0].txtExp1.value=''; // Expediente relacionado.
         //document.forms[0].cod_dptoDestinoORD.value=''; // Departamento tipo entrada ordinaria. TODO rmved
         //document.forms[0].desc_dptoDestinoORD.value=''; TODO rmved
         document.forms[0].cod_uniRegDestinoORD.value=''; // Unidad de rexistro.
         document.forms[0].cod_uor.value=''; // Unidad de registro cod visible
         document.forms[0].desc_uniRegDestinoORD.value='';
         document.forms[0].cod_procedimiento.value = '';
         document.forms[0].desc_procedimiento.value = '';
         //document.forms[0].cod_departamentoOrixe.value='';
         //document.forms[0].desc
         //_departamentoOrixe.value='';
         document.forms[0].cod_orgOrigen.value=''; // Organizacion Origen.
         document.forms[0].desc_orgOrigen.value='';
         document.forms[0].cod_unidadeRexistroOrixe.value='';
         document.forms[0].desc_unidadeRexistroOrixe.value='';
         document.forms[0].txtExp1Orixe.value='';
         if (document.getElementById("capaTiposEstadoAnotacion").style.visibility =='visible') {
             document.forms[0].cod_estadoAnotacion.value='';
             document.forms[0].desc_estadoAnotacion.value='';
         }
         //Pestaña 5
         document.forms[0].observaciones.value = '';
     }

     <% if (permisoMantenimiento) {%>
     /********************************************************************/
     /* FUNCION INICIAR ALTA												*/
     /********************************************************************/

     function iniciarAlta(lista_CODtiposDocumentos, lista_DESCtiposDocumentos, lista_CODtiposTransportes, lista_DESCtiposTransportes,lista_CODtiposRemitentes, lista_DESCtiposRemitentes, lista_CODtemas, lista_DESCtemas,lista_CODidInteresado, lista_DESCidInteresado,lista_CODdpto, lista_DESCdpto, fechaServidor, horaServidor, fecha)
     {
         alta=true;
         noObligatorioToObligatorioConsulta();
         ocultarInfSoloConsulta();
         activarFormulario();
         deshabilitarBuscar();
         desactivarOrigenYExpediente();
         desactivarInteresado();
         var vCamposCodigo= camposCodigo();
         cambiarLongMaxInput(vCamposCodigo,longMaxInputCodigo);
         var vCamposFecha = camposFecha();
         cambiarLongMaxInput(vCamposFecha,longMaxInputFecha);
         modificando('S');

         // KR --------> Desaparecen los ocultos.
         /* fechaHoy = new Date();
         var fecha = obtenerFechaString();
         document.forms[0].fechaAnotacion.value=fecha;
         document.forms[0].fechaDocumento.value=fecha;
         */
         // ----------> fin KR.
         document.forms[0].ano.value ='';
         document.forms[0].numero.value ='';
         horaMin();
         cod_tiposDocumentos = lista_CODtiposDocumentos;
         desc_tiposDocumentos = lista_DESCtiposDocumentos;
         cod_tiposTransportes = lista_CODtiposTransportes;
         desc_tiposTransportes= lista_DESCtiposTransportes;
         cod_tiposRemitentes= lista_CODtiposRemitentes;
         desc_tiposRemitentes= lista_DESCtiposRemitentes;
         cod_temas= lista_CODtemas;
         desc_temas= lista_DESCtemas;
         cod_tiposIdInteresado = lista_CODidInteresado;
         desc_tiposIdInteresado = lista_DESCidInteresado;
         cod_departamentos = lista_CODdpto;
         desc_departamentos = lista_DESCdpto;

         fechaHoy=fecha;

         document.forms[0].fechaAnotacion.value=fechaServidor;
         document.forms[0].fechaDocumento.value=fechaServidor;
         if (horaServidor!=undefined)
             {
                 document.forms[0].horaMinAnotacion.value="";
<%
if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))) {
%>
 document.forms[0].horaMinDocumento.value=horaServidor;
 <% }%>
}
mostrarCapasBotones('capaBotones2');
tp1.setSelectedIndex(0);
<% 	if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion)) {%>
tipoDeRemitentePorDefecto();
<% }%>
tipoDeDocumentoPorDefecto();
}
     /********************************************************************/
     /* FUNCION TIPO DE REMITENTE POR DEFECTO							*/
     /********************************************************************/
     
     function tipoDeRemitentePorDefecto() {
         for(var j=0;j<desc_tiposRemitentes.length;j++) {
             if(desc_tiposRemitentes[j] == "ADMINISTRACION LOCAL") {
                 document.forms[0].cod_tipoRemitente.value = cod_tiposRemitentes[j];
                 document.forms[0].txtNomeTipoRemitente.value = desc_tiposRemitentes[j];
             }
         }
     }
     /********************************************************************/
     /* FUNCION TIPO DE DOCUMENTO POR DEFECTO							*/
     /********************************************************************/
     // Situa el NIF como Tipo de Identificador del Interesado por defecto
     function tipoDeDocumentoPorDefecto() {
         for(var j=0;j<cod_tiposIdInteresado.length;j++) {
             if(cod_tiposIdInteresado[j] == "1") {
                 document.forms[0].codTipoDoc.value = cod_tiposIdInteresado[j];
                 document.forms[0].descTipoDoc.value = desc_tiposIdInteresado[j];
             }
         }
     }
     
     /********************************************************************/
     /* FUNCION REGISTRO CERRADO											*/
     /********************************************************************/
     
     function registroCerrado() {
         
         var mnsj='<%=descriptor.getDescripcion("msjRegCAnot")%>'
         jsp_alerta("A", mnsj);
         activarFormulario();
         if (document.getElementById('capaBotones2'))
             if (document.getElementById('capaBotones2').style.visibility=='visible') {
                 document.forms[0].cmdRegistrarAlta.disabled=false;
                 document.forms[0].cmdCancelarAlta.disabled=false;
             }
             if (document.getElementById('capaBotones5'))
                 if (document.getElementById('capaBotones5').style.visibility=='visible') {
                     document.forms[0].cmdRegistrarDuplicar.disabled=false;
                     document.forms[0].cmdCancelarDuplicar.disabled=false;
                 }
                 
             }
             /********************************************************************/
             /* FUNCION CONFIRMAR ALTA											*/
             /********************************************************************/
             
             function confirmarAlta() {
                 var mnsj='<%=descriptor.getDescripcion("msjUltAnotPosterior")%>'
                 activarFormulario();
                 deshabilitarBuscar();
                 desactivarOrigenYExpediente();
                 desactivarInteresado();
                 
                 if(jsp_alerta("",mnsj)){
                     document.forms[0].opcion.value="registrar_alta_confirmada";
                     // TEMAS
                     var l = new Array();
                     for (i=0; i < lista.length; i++)
                         {
                             l[i]=lista[i][0]+'§¥'; // Solo códigos
                         }
                         document.forms[0].listaTemas.value=l;
                         //document.forms[0].asunto.value = escape(document.forms[0].asuntoSinEscape.value);
                         document.forms[0].target="oculto";
                         document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                         document.forms[0].submit();
                     }
                 }
                 /********************************************************************/
                 /* FUNCION NO EXISTE EXPEDIENTE										*/
                 /********************************************************************/
                 
                 function noExisteExpediente() {
                     var mnsj='<%=descriptor.getDescripcion("msjNoExistExp")%>'
                     activarFormulario();
                     deshabilitarBuscar();
                     desactivarOrigenYExpediente();
                     desactivarInteresado();
                     jsp_alerta('A',mnsj)
                 }
                 /********************************************************************/
                 /* FUNCION PROC MAL RELACIONADO 									*/
                 /********************************************************************/
                 
                 function procMalRelacionado() {
                     var mnsj='<%=descriptor.getDescripcion("msjProcMalRel")%>'
                     activarFormulario();
                     deshabilitarBuscar();
                     desactivarOrigenYExpediente();
                     desactivarInteresado();
                     jsp_alerta('A',mnsj);
                 }
                 /********************************************************************/
                 /* FUNCION ALTA ENTRADA REGISTRADA									*/
                 /********************************************************************/
                 
                 function altaEntradaRegistrada(ej,num) {
                     //var mnsj='Alta '+ej+'/'+num+' <%=descriptor.getDescripcion("msjAnotHecha2")%>';
                     //jsp_alerta("A", mnsj);
                     desactivarFormulario();
                     modificando('N');
                     borrarPaginaBuscada();
                     borrarDatos();
                     borrarInteresado();
                     ocultarLista();
                     ocultarCalendario();
                     borrarPaginaBuscada();
                     document.forms[0].codTerc.value="0";
                     document.forms[0].codDomTerc.value="0";
                     document.forms[0].numModifTerc.value="0";
                     clean();
                     document.forms[0].horaMinAnotacion.value="";
                     document.forms[0].codTipoDoc.value="";
                     document.forms[0].descTipoDoc.value="";
                     document.forms[0].cbTipoEntrada.value=-1;
                     document.forms[0].txtNomeTipoEntrada.value="";
                     document.forms[0].txtCodigoDocumento.value='';
                     document.forms[0].tipoConsulta[1].checked = true;
                     
                     document.forms[0].ano.value = ej;
                     document.forms[0].numero.value = num;
                     
                     document.forms[0].opcion.value="consultar";
                     document.forms[0].target="oculto";
                     document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                     document.forms[0].submit();
                 }
                 
                 <% }%>
                 
                 /********************************************************************/
                 /* FUNCION MOSTRAR LISTA UNID REG DESTINO							*/
                 /********************************************************************/
                 
                 
                 function mostrarListaUnidRegDestino(){
                     
                     if (  //(Trim(document.forms[0].cod_orgDestino.value) == '<%= cod_org%>')
                         //&& (Trim(document.forms[0].cod_entDestino.value) == '<%= cod_ent%>')
                         (Trim(document.forms[0].cod_orgDestino.value) != '')
                         //&& (Trim(document.forms[0].cod_entDestino.value) != '')
                         //&& (Trim(document.forms[0].cod_dptoDestino.value) != '')
                     ){
                     var condiciones = new Array();
                     
                     //condiciones[0]='UOR_DEP'+'§¥';
                     //condiciones[1]= document.forms[0].cod_dptoDestino.value ;
                     condiciones[0]='UOREX_ORG'+'§¥';
                     condiciones[1]=document.forms[0].cod_orgDestino.value ;
                     muestraListaTabla('UOREX_COD','UOREX_NOM',EsquemaGenerico + 'A_UOREX A_UOREX',condiciones,'cod_uniRegDestino','desc_uniRegDestino','botonUnidadeRexistro','100');
                 } else document.forms[0].cod_uniRegDestino.value=''; // Si viene del onchange permaneceria
             }
             /********************************************************************/
             /* FUNCION MOSTRAR LISTA UNID REG ORIGEN							*/
             /********************************************************************/
             
             function mostrarListaUnidRegOrigen(){
                 
                 if ( (Trim(document.forms[0].cod_orgOrigen.value) != '')){
                     var condiciones = new Array();
                     condiciones[0]='UOREX_ORG'+'§¥';
                     condiciones[1]=document.forms[0].cod_orgOrigen.value ;
                     muestraListaTabla('UOREX_COD','UOREX_NOM',EsquemaGenerico + 'A_UOREX A_UOREX',condiciones,'cod_unidadeRexistroOrixe','desc_unidadeRexistroOrixe','botonUnidadeRexistroOrigen','100');
                 } else document.forms[0].cod_unidadeRexistroOrixe.value=''; // Si viene del onchange permaneceria
             }
             
             /********************************************************************/
             /* FUNCION HORAMIN	 												*/
             /********************************************************************/
             
             function horaMin() {
                 
                 var today = new Date();
                 var hoy;
                 var hora;
                 var min;
                 
                 hoy = today.toString();
                 if (hoy.substring(9,10)==" ") {
                     hora = hoy.substring(10,12);
                     min = hoy.substring(13,15);
                 } else {
                 hora = hoy.substring(11,13);
                 min = hoy.substring(14,16);
             }
             <% if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {%>
             /*document.forms[0].txtHoraDoc.value = hora;
             document.forms[0].txtMinDoc.value = min;
             */
             document.forms[0].horaMinDocumento.value=hora+":"+min;
             <%}%>
             /* document.forms[0].txtHoraEnt.value = hora;
             document.forms[0].txtMinEnt.value = min;
             */
             document.forms[0].horaMinAnotacion.value=hora+":"+min;
         }
         
         function horaMinDoc() {
             var today = new Date();
             var hoy;
             var hora;
             var min;
             
             hoy = today.toString();
             if (hoy.substring(9,10)==" ") {
                 hora = hoy.substring(10,12);
                 min = hoy.substring(13,15);
             } else {
             hora = hoy.substring(11,13);
             min = hoy.substring(14,16);
         }
         <% if ("E".equals(tipoAnotacion)) {%>
         /*document.forms[0].txtHoraDoc.value = hora;
         document.forms[0].txtMinDoc.value = min;
         */
         document.forms[0].horaMinDocumento.value=hora+":"+min;
         <%}%>
     }
     
     /* Devuelve la hora y minuto actuales en un string de formato hh:mm */
     function getHoraMin() {
         
         var today = new Date();
         var hoy;
         var hora;
         var min;
         
         hoy = today.toString();
         if (hoy.substring(9,10)==" ") {
             hora = hoy.substring(10,12);
             min = hoy.substring(13,15);
         } else {
         hora = hoy.substring(11,13);
         min = hoy.substring(14,16);
     }
     return hora+":"+min;
 }
 
 /********************************************************************/
 /* FUNCION COMPROBAR FECHA											*/
 /********************************************************************/
 
 function comprobarFecha(inputFecha) {
     var formato = 'dd/mm/yyyy';
     if (Trim(inputFecha.value)!='') {
         
         if (consultando) {
             var validas = true;
             var fechaFormateada=inputFecha.value;
             var pos=0;
             var fechas = Trim(inputFecha.value);
             var fechas_array = fechas.split(/[:|&<>!=]/);
             
             
             for (var loop=0; loop < fechas_array.length; loop++)
                 {
                     f = fechas_array[loop];
                     formato = formatoFecha(Trim(f));
                     var D = ValidarFechaConFormato(f,formato);
                     if (!D[0]) validas=false;
                     else {
                         if (fechaFormateada.indexOf(f,pos) != -1) {
                             var toTheLeft = fechaFormateada.substring(0, fechaFormateada.indexOf(f));
                             var toTheRight = fechaFormateada.substring(fechaFormateada.indexOf(f)+f.length, fechaFormateada.length);
                             pos=fechaFormateada.indexOf(f,pos);
                             fechaFormateada = toTheLeft + D[1]+ toTheRight;
                         }
                     }
                 }
                 
                 if (!validas) {
                     jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                         tp1.setSelectedIndex(0);
                         inputFecha.focus();
                         return false;
                     } else {
                     inputFecha.value = fechaFormateada;
                     return true;
                 }
                 
             } else { // No consultando. Unico formato posible: dd/mm/yy o dd/mm/yyyy
             var D = ValidarFechaConFormato(inputFecha.value,formato);
             if (!D[0]){
                 jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                     tp1.setSelectedIndex(0);
                     inputFecha.focus();
                     return false;
                 } else {
                 inputFecha.value = D[1];
                 return true;
             }
         }
     }
     return true;
 }
 
 // --------------------------------------------- RELACIÓN DE ANOTACIONES -------------------------------------------
 
 var anoSeleccionada;
 var numeroSeleccionada;
 var numAnotaciones;
 
 var consultando=false;
 var alta=false;
 
 
 function desactivarCampoDocumento() {
     if(document.forms[0].codTipoDoc.value == "0") {
         obligatorioAnormalDoc(document.forms[0].txtDNI)
     }
 }
 

</SCRIPT>
</head>
<BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); inicializar()}">

        
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        <!-- Fin mensaje de esperar mientras carga  -->
        <html:form action="/registroExterno/RegistroExterno.do" target="_self">
            
            <!-- código de la UOR -->
            <html:hidden  property="cod_intUOR" value=""/>
            <html:hidden  property="opcion" value=""/>
            <html:hidden  property="codigoDocumento" value=""/> <!-- Utilizado para eliminar el documento -->
            
            <html:hidden  property="tipo_select"   value=""/>
            <html:hidden  property="col_cod"   value=""/>
            <html:hidden  property="col_desc"   value=""/>
            <html:hidden  property="nom_tabla"   value=""/>
            <html:hidden  property="input_cod"   value=""/>
            <html:hidden  property="input_desc"   value=""/>
            <html:hidden  property="column_valor_where" value=""/>
            <html:hidden  property="whereComplejo" value=""/>
            <html:hidden  property="codTerc" value="0"/>
            <html:hidden  property="codDomTerc" value="0"/>
            <html:hidden  property="numModifTerc" value="0"/>
            <html:hidden  property="txtIdTercero" value=""/>
            <html:hidden  property="txtIdDomicilio" value=""/>
            <html:hidden  property="txtVersion" value=""/>
            <input type="hidden" name="situacion">
            <html:hidden  property="modificar" value="0"/>
            <html:hidden  property="duplicar" value="1"/>
            <html:hidden  property="hayTexto" value=""/>
            
            <html:hidden  property="listaTemas" value="" />
            
            <html:hidden property="acceso" value="consultar" />
            <html:hidden property="pendienteBuzon" value="no"/>
            <input type="hidden" name="ventana" value="false">
            <html:hidden property="aplicacion" value=""/>
            <html:hidden property="posicionAnotacion" value=""/>
            <input type="hidden" name="lineasPagina" value="10">
            <input type="hidden" name="pagina" value="1">
            
            <input type="hidden" name="posicionCuneus" >
            <input type="hidden" name="idiomaCuneus" >
            <input type="hidden" name="nCopiasCuneus" >
            <input type="hidden" name="opcionAltaDesdeConsulta">
            <input type="hidden" name="grabarDuplicar">
            <html:hidden  property="destino" value="" />
            
            <% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion) || contestacion) {%>
            <!-- contestacion -->
            <html:hidden property="ejercicioAnotacionContestada" value=""/>
            <html:hidden property="numeroAnotacionContestada" value=""/>
            <% }%>
            
<div class="txttitblancoder"><%=descriptor.getDescripcion("tit_AnotS")%></div>
<div class="contenidoPantalla" valign="center">
    <div class="tab-pane" id="tab-pane-1" >
<!-- ------------------------------------------------------------------ -->
<!--                           PESTANAS                                         -->
<!-- ------------------------------------------------------------------ -->
                <script type="text/javascript">
                    tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
                </script>
                <!-- CAPA 1: DATOS GENERALES------------------------------ -->
                <div class="tab-page" id="tabPage1" style="height:270px" >
                    <h2 class="tab"><%=descriptor.getDescripcion("res_pestana1")%></h2>
                    <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
                    <TABLE id ="tablaDatosGral" class="contenidoPestanha" border="0px" cellspacing="0px" cellpadding="0px" STYLE=" height:350px">
                        <TR>
                            <TD>
                                <TABLE STYLE=" height:75%;width:95%;margin-top:30px;" cellspacing="0px" cellpadding="0px" border="0px"  ALIGN="center" >
                                    <TR>  
                                        <TD style="width:19%"   class="etiqueta"> <%=descriptor.getDescripcion("res_fecS")%>:</TD>
                                        <TD   class="columnP">
                                            <html:text styleId="obligatorio"  styleClass="inputTxtFechaObligatorio" size="18"  property="fechaAnotacion"
                                                    onkeypress = "javascript:if (consultando) return soloCaracteresFechaConsultando(event); else return soloCaracteresFecha(event);"
                                                    onblur = "javascript:return comprobarFecha(this);"
                                                    onfocus = "javascript: this.select();"/>
                                            &nbsp;
                                            <A href="javascript:calClick();return false;" onClick="mostrarCalFechaAnotacion();return false;" style="text-decoration:none;">
                                                <span class="fa fa-calendar" aria-hidden="true" name="calFechaAnotacion" alt="Fecha" ></span>
                                            </A>
                                        </TD>
                                <TD class="etiqueta" style="width:15%"><%=descriptor.getDescripcion("res_HoraSal")%>:</TD>
                                <TD  class="columnP">
                                    <html:text styleClass="inputTxtFecha" size="7" maxlength="5" property="horaMinAnotacion"
                                               onkeypress = "javascript:return soloCaracteresHora(event);"
                                               onblur = "javascript:return comprobarHora(this, mensajeHora);"
                                               onfocus = "javascript: this.select();" />
                                </TD>
                              </TR>
                                <TR>
                                    <TD class="etiqueta"><%=descriptor.getDescripcion("res_asunto")%>:</TD>
                                    <TD colspan="3" class="columnP">
                                        <html:textarea styleId="obligatorio" styleClass="textareaTextoObligatorio" cols="98" rows="5" property="asunto"  maxlength="2" onkeydown="return textCounter(this,4000);" onkeypress="javascript:PasaAMayusculas(event);" value=""></html:textarea>
                                    </TD>
                                </TR>
                        <!-- Unidad Organica -->
                            <TR>
                                <TD  class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidTramit")%>:</TD>
                                <TD colspan="3" class="columnP" >
                                    <html:hidden property="cod_uniRegDestinoORD" value=""/>
                                    <html:text styleClass="inputTextoObligatorio" styleId="codUor" property="codUor" size="8"/>
                                    <html:text styleClass="inputTextoObligatorio" styleId="descUor" property="descUor" style="width:543px;height:17px" readonly="true"/>
                                    <A href="javascript:{onClickHref_uniRegDestinoORD();}" style="text-decoration:none;" id="anchorUor" name="anchorUor"
                                       >
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonUor" name="botonUor" style="cursor:hand;"></span>
                                    </A>
                                </TD>
                            </TR>
                            <TR>
                                <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiqProc")%>:</TD>
                                <TD colspan="3" class="columnP">                                                                                
                                    <html:text styleClass="inputTexto"  styleId = "codProcedimiento" property="codProcedimiento" size="8" onclick="javascript:getListaProcsByUor()"/>
                                    <html:text styleClass="inputTexto" styleId = "descProcedimiento" property="descProcedimiento" style="width:543px;height:17px" readonly="true"/>                                                                                           
                                    <A href="" style="text-decoration:none;" id="anchorProcedimiento" name="anchorProcedimiento"
                                       >
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProcedimiento" name="botonProcedimiento" style="cursor:hand;"></span>
                                    </A>
                                </TD>
                            </TR>
                            <!-- Expediente Relacionado -->
                            <TR>
                                <TD class="etiqueta"><%=descriptor.getDescripcion("res_ExpRel")%>:</TD>
                                <TD colspan="3" class="columnP">
                                    <html:text  styleClass="inputTexto" size="99" maxlength="30"  property="txtExp1"
                                                onfocus="this.select();"
                                                onkeypress="javascript:PasaAMayusculas(event);"/>
                                </TD>
                            </TR>           
                        </TABLE>
                </TD>
             </TR>           
        </TABLE>
    </div>
    <!-- CAPA 2: INTERESADo ------------------------------ -->
    <div class="tab-page" id="tabPage2" style="height:270px">
        <h2 class="tab"><%=descriptor.getDescripcion("res_pestana3")%></h2>
        <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>
          <TABLE id ="tablaInteresado" class="contenidoPestanha" border="0px" cellspacing="0px" cellpadding="0px" STYLE=" height:350px">
            <TR>
                <TD>
                    <TABLE STYLE=" height:75%;width:95%;margin-top:30px;" cellspacing="0px" cellpadding="0px" border="0px"  ALIGN="center" >
                        <TR>
                            <TD width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTiDoc")%>:</TD>
                            <TD  class="columnP">
                                <html:text styleId="codTipoDoc" property="codTipoDoc" styleClass="inputTextoObligatorio" size="3"/>
                                <html:text styleClass="inputTextoObligatorio" styleId="descTipoDoc" property="descTipoDoc" style="width:140;height:17" readonly="true"/>
                                <A href="javascript:{onClickDescTipoDoc();}" style="text-decoration:none;" id="anchorTipoDoc" name="anchorTipoDoc"
                                   >
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoDoc" name="botonTipoDoc" style="cursor:hand;"></span>
                                </A>
                            </TD>
                            <TD width="15%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDocumento")%>:</TD>
                            <TD class="columnP">
                                <html:text styleClass="inputTextoObligatorio"  size="12" maxlength="16" property="txtDNI" styleId="obligatorio" onchange="javascript:buscarDocTipoDoc();" onkeypress="javascript:PasaAMayusculas(event);"/>
                                <span class="fa fa-search" aria-hidden="true"  name="botonT" alt="Buscar Interesado"
                                     onclick="javascript:pulsarBuscarTerceros();"></span>
                                <span class="fa fa-users" aria-hidden="true" name="botonTer" alt="Mantenimiento de Terceros"
                                     style="visibility:hidden" onclick="javascript:abrirTerceros();"></span>                                                                                
                            </TD>
                        </TR>
                        <TR>
                            <TD  class="etiqueta"><%=descriptor.getDescripcion("gEtiqNombr")%>/<BR><%=descriptor.getDescripcion("gEtiqRazonSoc")%>:</TD>
                            <TD  COLSPAN="3">
                                <html:text  styleClass="inputTextoDeshabilitado" style="width:100%;" maxlength="50"  property="txtInteresado" readonly="true" tabindex="-1" onkeypress="javascript:PasaAMayusculas(event);"/>
                            </TD>
                        </TR>
                        <TR>
                            <TD  class="etiqueta"><%=descriptor.getDescripcion("gEtiqTelfFax")%>:</TD>
                            <TD class="etiqueta">
                                <html:text  property="txtTelefono" styleClass="inputTextoDeshabilitado" style="width:120;" maxlength="20" readonly="true" tabindex="-1"/>
                            </TD>
                            <TD  class="etiqueta"><%=descriptor.getDescripcion("gEtiqEmail")%>:</TD>
                            <TD  COLSPAN="2" class="columnP">
                                <html:text  property="txtCorreo" styleClass="inputTextoDeshabilitado" style="width:299;" maxlength="30" readonly="true" tabindex="-1"/>
                            </TD>
                        </TR>
                        <TR>
                            <TD class="etiqueta"><!--Particula:--></TD>
                            <TD  class="columnP">
                                <html:hidden  property="txtPart" styleClass="inputTextoDeshabilitado" size="10" maxlength="5" readonly="true" tabindex="-1"/>
                            </TD>
                            <TD  class="etiqueta"><!--Primer Apellido:--></TD>
                            <TD  COLSPAN="2" class="columnP">
                                <html:hidden  property="txtApell1" styleClass="inputTextoDeshabilitado" size="42" maxlength="60" readonly="true" tabindex="-1"/>
                            </TD>
                        </TR>
                        <TR>
                            <TD class="etiqueta"><!--Partcula:--></TD>
                            <TD class="columnP">
                                <html:hidden  property="txtPart2" styleClass="inputTextoDeshabilitado" size="10" maxlength="5" readonly="true" tabindex="-1"/>
                            </TD>
                            <TD class="etiqueta"><!--Segundo Apellido:--></TD>
                            <TD  COLSPAN="2" class="columnP">
                                <html:hidden  property="txtApell2" styleClass="inputTextoDeshabilitado" size="42" maxlength="60" readonly="true" tabindex="-1"/>
                            </TD>
                        </TR>
                        <TR>
                            <TD class="sub3titulo" COLSPAN="5" bgcolor="#DCDCCC">&nbsp;<%=descriptor.getDescripcion("gEtiqDomicilio")%></TD>
                        </TR>
                        <TR>
                            <TD  class="etiqueta" STYLE="margin-top:5px"><%=descriptor.getDescripcion("gEtiqProvincia")%>:</TD>
                            <TD  class="columnP" >
                                <html:hidden property="txtPais"  styleClass="inputTextoDeshabilitado" styleId="txtPais" size="27" readonly="true" tabindex="-1"/>
                            <html:text property="txtProv"  styleClass="inputTextoDeshabilitado" styleId="txtProv" style="width:95%;" readonly="true" tabindex="-1"/> </TD>
                            <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiqMunicipio")%>:</TD>
                            <TD  COLSPAN="2" class="columnP" >
                            <html:text property="txtMuni"  styleClass="inputTextoDeshabilitado" styleId="txtMuni" style="width:100%;" readonly="true" tabindex="-1"/></TD>
                        </TR>
                        <TR>
                            <TD  class="etiqueta"><%=descriptor.getDescripcion("gEtiqDomicilio")%>:</TD>
                            <TD  COLSPAN="3" class="columnP">
                                <html:text  styleClass="inputTextoDeshabilitado" property="txtDomicilio" style="width:100%;" maxlength="60" readonly="true" tabindex="-1"/>
                            </TD>
                        </TR>
                        <tr>
                            <TD style="visibility:hidden" width="13%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqPoblacion")%>:</TD>
                            <td style="visibility:hidden" width="63%" class="columnP">
                                <html:text  styleClass="inputTextoDeshabilitado" style="width:95%;" maxlength="40" property="txtPoblacion" readonly="true" tabindex="-1"/>
                            </td>
                            <td  class="etiqueta"><%=descriptor.getDescripcion("gEtiqCodPostal")%>:</td>
                            <td COLSPAN="2" class="columnP">
                                <html:text  styleClass="inputTextoDeshabilitado" style="width:56;" maxlength="5" property="txtCP" readonly="true" tabindex="-1"/>
                            </td>
                        </tr>
                    </TABLE>
                    </TD>
                 </TR>
             </TABLE>
         </div>
     </div>
    <div class="botoneraPrincipal">
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bGrabar")%>'
                   class="botonGeneral" value='<%=descriptor.getDescripcion("gbGrabar")%>'
                   name="cmdRegistrarAlta" onClick="pulsarRegistrarAltaAcc();return false;">   </td> 
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>'
                   class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>'
                   name="cmdCancelarAlta" onClick="salir();return false;"/>
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>'
                class="botonGeneral" value='<%=descriptor.getDescripcion("gbLimpiar")%>'
                name="cmdLimpiar" onClick="pulsarLimpiar();return false;"/>
    </div>
</div>
<!-- Fin botones ALTA. -->       
<!-- Fin BOTONES. -->
            
</html:form>


<div id="desplegable" style="overflow-y: auto; overflow-x: no; visibility: hidden; BORDER: 0px"></div>
        
<script type="text/javascript">
            
    tp1_p1.setPrimerElementoFoco(document.forms[0].fechaAnotacion);
    tp1_p2.setPrimerElementoFoco(document.forms[0].codTipoDoc);
            


    var comboProcedimiento= new Combo("Procedimiento");
    var comboUor= new Combo("Uor");
    
    comboUor.change = function() {
        getListaProcsByUor();
        
    }
    
    function antesDeCambiarPestana() {}
    var comboTipoDoc= new Combo("TipoDoc");

<%String userAgent = request.getHeader("user-agent");%>

var coordx=0;
var coordy=0;


<%if(userAgent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>


    document.onmouseup = checkKeys;

    function checkKeysLocal(evento,tecla) {
        var teclaAuxiliar = "";
        if(window.event){
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else{
            teclaAuxiliar = evento.which;
        }
    }
    
    if (teclaAuxiliar == 9){
        
        comboProcedimiento.ocultar();
        comboUor.ocultar();
        comboTipoDoc.ocultar();
      
    }
    if (teclaAuxiliar == 1){
        
        if (comboProcedimiento.base.style.visibility == "visible" && isClickOutCombo(comboProcedimiento,coordx,coordy)) setTimeout('comboProcedimiento.ocultar()',20);
        if (comboUor.base.style.visibility == "visible" && isClickOutCombo(comboUor,coordx,coordy)) setTimeout('comboUor.ocultar()',20);
        if (comboTipoDoc.base.style.visibility == "visible" && isClickOutCombo(comboTipoDoc,coordx,coordy)) setTimeout('comboTipoDoc.ocultar()',20);

        

    }

</script>        
        
    </BODY>
    
</html:html>
