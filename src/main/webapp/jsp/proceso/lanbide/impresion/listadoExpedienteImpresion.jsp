<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>


<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="java.lang.Integer"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.business.sge.DefinicionProcedimientosValueObject" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Impresion de expedientes del procedimiento CEPAP</title>


<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  int idioma=1;
  int apl=1;
  String css="";
  if (session.getAttribute("usuario") != null){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    apl = usuarioVO.getAppCod();
    idioma = usuarioVO.getIdioma();
    css=usuarioVO.getCss();
  }
  String idSesion = session.getId();
  Config m_Config = ConfigServiceHelper.getConfig("common");
  String statusBar = m_Config.getString("JSP.StatusBar");
  String mostrarLocalizacion = m_Config.getString("ListaExpedientes.localizacion");
%>

<!-- Estilos -->

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva200.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>

<script type="text/javascript">
var lista = new Array();
var lineasExpedienteCEPAP = new Array();
var lineas = new Array();
var datosExpedienteCEPAP = new Array();
var datosFicheroCEPAP = new Array();

/**
        Devuelve un objeto XMLHttpRequest
    */
    function getXMLHttpRequest(){
    var aVersions = [ "MSXML2.XMLHttp.5.0",
    "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
    "MSXML2.XMLHttp","Microsoft.XMLHttp"
    ];

    if (window.XMLHttpRequest){
        // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
        return new XMLHttpRequest();
    }
    else
        if (window.ActiveXObject){
        // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
        for (var i = 0; i < aVersions.length; i++) {
            try {
                var oXmlHttp = new ActiveXObject(aVersions[i]);
                return oXmlHttp;
            }catch (error) {
            //no necesitamos hacer nada especial
            }
        }
        }
    }

// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
function inicializar(){
    
    window.focus();
    recuperaExpedientesCEPAP();
    recuperaFicherosGenerados();
}
function recuperaExpedientesCEPAP(){
    
    <%
    // PARA EXPEDIENTES CEPAP
    Vector relacionExpedientesCEPAP = (Vector) session.getAttribute("RelacionExpedientesCEPAP");
    int numRelacionExpedientesCEPAP = 0;
    int i = 0;
    if ( relacionExpedientesCEPAP != null ) numRelacionExpedientesCEPAP = relacionExpedientesCEPAP.size();
    %>
    var j=0;
    var contador = 0;
    var seleccionado ="false";
    <%for(i=0;i<numRelacionExpedientesCEPAP;i++){
      GeneralValueObject ExpedienteCEPAP = (GeneralValueObject)relacionExpedientesCEPAP.get(i);%>
      datosExpedienteCEPAP[j] = [check(seleccionado,contador),
      "<%=(String)ExpedienteCEPAP.getAtributo("numExpediente")%>",
      "<%=(String)ExpedienteCEPAP.getAtributo("interesados")%>",
      "<%=(String)ExpedienteCEPAP.getAtributo("numRegistro")%>",
      "<%=(String)ExpedienteCEPAP.getAtributo("fechaEntrada")%>",
      "<%=(String)ExpedienteCEPAP.getAtributo("fecPres")%>"];
      lineasExpedienteCEPAP[j] = datosExpedienteCEPAP[j];
      j++;
      contador++;

    <%}%>

    tabImpresionExpCEPAP.lineas = lineasExpedienteCEPAP;
    refresca(tabImpresionExpCEPAP);

}


function listadoExpedientesImpresion(indice){    
    if(indice!=null && indice!=-1){        
        var dato = datosFicheroCEPAP[indice][0];        
        window.open("<%=request.getContextPath()%>/lanbide/impresion/ImpresionExpedientesLanbide.do?opcion=listadoExpedientes&nombreFichero=" + dato,"ventana1","left=10, top=10, width=700, height=310, scrollbars=no, menubar=no, location=no, resizable=no");        
    }    
}

function imprimeEtiq(indice){    
    if(indice!=null && indice!=-1){     
        var dato = datosFicheroCEPAP[indice][0];        
        window.open("<%=request.getContextPath()%>/lanbide/impresion/ImpresionExpedientesLanbide.do?opcion=imprimirEtiquetas&nombreFichero=" + dato,"ventana1","left=10, top=10, width=700, height=310, scrollbars=no, menubar=no, location=no, resizable=no");
    }    
}

function recuperaFicherosGenerados(){ 
    
    <%
    // PARA fichero generados expediente CEPAP
    Vector relacionFicherosExcelCEPAP = (Vector) session.getAttribute("RelacionFicherosImpresionGenerados");
    int numRelacionFicherosExcelCEPAP = 0;
    int z = 0;
    if ( relacionFicherosExcelCEPAP != null ) numRelacionFicherosExcelCEPAP = relacionFicherosExcelCEPAP.size();
    %>
    var k=0;
    var contexto = "<%=request.getContextPath()%>";
    
    <%for(z=0;z<numRelacionFicherosExcelCEPAP;z++){
      GeneralValueObject ficheroGenerado = (GeneralValueObject)relacionFicherosExcelCEPAP.get(z);%>
      datosFicheroCEPAP[k] = ['<%=(String)ficheroGenerado.getAtributo("numExpediente")%>',
      '<%=(String)ficheroGenerado.getAtributo("fechaGeneracion")%>','<a href=\"#\" title=\"' +'<%=descriptor.getDescripcion("gEtiqAltExpedRelacionados")%>' + '\" onclick=\"listadoExpedientesImpresion(' + k + ');\">' + '<%=descriptor.getDescripcion("gbVer")%>' + '</a>'];

      lineas[k] = datosFicheroCEPAP[k];
      k++;
    <%}%>
    tabFichExelCEPAP.lineas = lineas;
    refresca(tabFichExelCEPAP);
}

function pulsarSeleccTodos(){
    
    var nodoCheck = document.getElementsByTagName("input");
    var varCheck = "true";

    for (i=0; i<nodoCheck.length; i++){
        if (nodoCheck[i].type == "checkbox" && nodoCheck[i].disabled == false) {
            nodoCheck[i].checked = varCheck;
        }
    }
}

function pulsarGenerarFichero(){ 
        
    getSeleccionados();
    var c=0;
    var ajax = getXMLHttpRequest();
    var parametros ="opcion=exportarCEPAPCSV"

    //Procesamos las selecciones
    if(listaNumExpSeleccionados.length > 0){
        // No la pasamos por url, se corta con 7435 caracteres la pasomos como objeto hidden en el form
        //parametros += "&listaExpedientesSeleccionados=";
        var primera = true;
        var string = "";
                
        for(c=0; listaNumExpSeleccionados != null && c<listaNumExpSeleccionados.length; c++){            
            var numExp = listaNumExpSeleccionados[c];
            var datos = numExp.split(",");        
            //string += numExp + "-" ;
            string += datos[0] + "-" ;
        }
        //parametros += string;
        
        document.forms[0].target = "oculto";
        document.forms[0].action = '<%=request.getContextPath()%>/lanbide/impresion/ImpresionExpedientesLanbide.do?' + parametros;       
        if(document.getElementById("listaExpedientesSeleccionados")!=null){
            document.getElementById("listaExpedientesSeleccionados").value=string;
        }else{
            var hiddenField = document.createElement("input");      
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("id", "listaExpedientesSeleccionados");
            hiddenField.setAttribute("name", "listaExpedientesSeleccionados");
            hiddenField.setAttribute("value",string);
            document.forms[0].appendChild(hiddenField);
        }
        document.forms[0].submit();      
        if(document.getElementById("listaExpedientesSeleccionados")!=null){
            document.getElementById("listaExpedientesSeleccionados").value="";
        }
        recuperaFicherosGenerados();        
    }else
        jsp_alerta("A","<%=descriptor.getDescripcion("msjErrorExpSelect")%>");
}


// Visualizar un documento asociado al expediente de la pestaña 'Otros Documentos'
function pulsarVisualizarDocumentoCSV(){
    
    if(tabFichExelCEPAP.selectedIndex != -1) {
    var nombreFichero = datosFicheroCEPAP[tabFichExelCEPAP.selectedIndex][0];
    var directorio='<%=usuarioVO.getDtr()%>';
    abrirInformeCSV(nombreFichero,directorio);

    } else jsp_alerta('A','<%=descriptor.getDescripcion("msjFicheroNoSelecc")%>');
}


function getSeleccionados(){

     var c=0;
     var j=0;

     listaSeleccionados=new Array();
     listaNumExpSeleccionados=new Array();
     for(c=0; lineasExpedienteCEPAP != null && c<lineasExpedienteCEPAP.length; c++){
       if (document.getElementById("Seleccionado"+c)!=null){
            if (document.getElementById("Seleccionado"+c).checked){
                var vExp = lineasExpedienteCEPAP[c].toString().substring(lineasExpedienteCEPAP[c].toString().indexOf(",")+1);

                listaNumExpSeleccionados[j]=vExp;
                listaSeleccionados[j]="1";
                j++;

            }
        }
     }
}

function check(seleccionado,contador){
    var fila = '';
    if(seleccionado=="true"){
        fila+= '<input type="checkbox" value="true" checked='+seleccionado+' id="Seleccionado'+contador+'" />';
    } else{
        fila+='<input type="checkbox" value="true" id="Seleccionado'+contador+'" />';
    }
        fila += '&nbsp;';
 return fila;
}

function abrirInformeCSV(nombre,dir) { 
    pleaseWait('off');        
    var source = "<c:url value='/jsp/verPdf.jsp?opcion=null&tipoFichero=csv&nombre='/>" + nombre+"&dir="+dir;
    var nombreVentana = (top.name == 'ventana' ? 'ventana2' : 'ventana');
    ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp;jsessionid=<%=idSesion%>?source=" + source, nombreVentana, 'width=800px,height=550px,status=' + '<%=statusBar%>' + ',toolbar=no,resizable=yes');
    ventanaInforme.focus(); 
    
    
}

function actualizarImpresion(){
    document.forms[0].opcion.value = 'cargarDocumentosDesdeDefinicion';
	document.forms[0].target = "oculto";
	document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";
	document.forms[0].submit();
        
    
}
function cargaDocumentos(){
	document.forms[0].opcion.value = 'cargarDocumentosDesdeDefinicion';
	document.forms[0].target = "oculto";
	document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";
	document.forms[0].submit();
 }

</script>
</head>

<body class="bandaBody" onload="javascript:{pleaseWait('off');inicializar();}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>


<form name="formulario" method="post">
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="identificador" id="identificador">

    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_ImpreExpCEPAP")%></div>
    <div class="contenidoPantalla">
        <table style="width:100%">
        <tr>
            <td>
                <div id="tablaImpresionExpCEPAP"></div>
            </td>
        </tr>
        <tr>
            <td style="padding-top:25px">
                <div id="tablaFichExelCEPAP"></div>
            </td>
        </tr>
    </table>
    <div id="tablaBotones" class="botoneraPrincipal">
        <input type= "button" class="botonLargo" value="<%=descriptor.getDescripcion("gbSeleccTodos")%>"
                name="all" id="all" onClick="pulsarSeleccTodos();">
        <input type= "button" class="botonLargo" value="<%=descriptor.getDescripcion("gbGenerarFichero")%>"
                name="generarFichero" onClick="pulsarGenerarFichero();">
        <input type= "button" class="botonLargo" value="<%=descriptor.getDescripcion("etiqVerDocumento")%>"
                name="cmdVerOtroDocumento" id="cmdVerOtroDocumento" onClick="pulsarVisualizarDocumentoCSV();return false;">
    </div>
</div>
</form>

<script type="text/javascript">
  var tabImpresionExpCEPAP;
  var tabFichExelCEPAP;

  //Tabla de expedientes estado pendiente  del procedimiento CEPAP
    tabImpresionExpCEPAP = new Tabla200(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaImpresionExpCEPAP'));
    tabImpresionExpCEPAP.addColumna('35','center',"");
    tabImpresionExpCEPAP.addColumna('130','center','<%= descriptor.getDescripcion("gEtiq_NumExp")%>');
    tabImpresionExpCEPAP.addColumna('230','center','<%=descriptor.getDescripcion("gEtiqSolicitante")%>');
    tabImpresionExpCEPAP.addColumna('145','center','<%=descriptor.getDescripcion("gEtiqAnotRegistro")%>');
    tabImpresionExpCEPAP.addColumna('145','center','<%=descriptor.getDescripcion("gEtiqFecPresRegistro")%>');
    tabImpresionExpCEPAP.addColumna('145','center','Fecha solicitud');
    tabImpresionExpCEPAP.displayCabecera=true;

    //Tabla MELANBIDE03_IMPRESION_CEPAP, fichero excel con expedientes procedim CEPAP
    tabFichExelCEPAP = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaFichExelCEPAP'));
    tabFichExelCEPAP.addColumna('450','center','<%= descriptor.getDescripcion("gEtiq_NomFich")%>');
    tabFichExelCEPAP.addColumna('250','center','<%= descriptor.getDescripcion("gEtiq_FechGenerado")%>');
    tabFichExelCEPAP.addColumna('198','center','<%= descriptor.getDescripcion("gEtiqExpedRelacionados")%>');
    tabFichExelCEPAP.displayCabecera=true;

    function refresca(tablaCampoSup){
    tablaCampoSup.displayTabla();
  }

</script>

</body>
</html>
