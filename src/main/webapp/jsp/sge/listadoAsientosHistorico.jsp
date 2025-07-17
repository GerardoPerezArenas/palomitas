<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>


<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.sge.TramitacionValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionForm" %>
<%@page import="java.util.Vector"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%
  int idioma=1;
  int apl=1;
  if (session!=null){
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    if (usuario!=null) {
      idioma = usuario.getIdioma();
      apl = usuario.getAppCod();
    }
  }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");

    String userAgent = request.getHeader("user-agent");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<html:html>
 <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <TITLE>::: EXPEDIENTES  Tramitacion de Expedientes:::</TITLE>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
     <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>" media="screen" >
    <script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    <style type="text/css">
       TR.rojo TD {color:#d74328;}
       TR.negrita TD {font-weight: bold;}
    </style>

<SCRIPT type="text/javascript">
    
var lista = new Array();
var listaObs = new Array();
var listaOriginal = new Array();
var listaP = new Array();
var listaSel = new Array();
var fila;
var ultimo = false;
var pagin;
var asunto;
var apellido1;
var estado;
var encontrado = false;
var consultando = false;

        <c:if test="${not empty requestScope.errores}">
            var msjErrores = new Array();
            var i = 0;
            <c:forEach items="${requestScope.errores}" var="error">
                msjErrores[i++] = '<fmt:message key="BusquedaTercero/falloBusqueda"/> <c:out value="${error}"/>';
            </c:forEach>
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/listadoErrores.jsp',msjErrores,
	'width=420,height=300,status=no',function(datos){});
        </c:if>

<% /* Recupera el vector de asientos de la sesion. */
    Config registroCong = ConfigServiceHelper.getConfig("Registro");
    Boolean pluginTecnicoReferenciaActivo = registroCong.getString("INTEGRACION_TECNICO_REFERENCIA").equals("SI");
   Vector relacionAsientosHistorico = (Vector) session.getAttribute("RelacionAsientosHistorico");
   int numRelacionAsientosHistorico = 0;

   String numero = (String)session.getAttribute("numero_total_anotaciones_historico");
   if(numero!=null && numero!="") 
         numRelacionAsientosHistorico = Integer.parseInt(numero);
   else   
   if ( relacionAsientosHistorico != null ) numRelacionAsientosHistorico = relacionAsientosHistorico.size();
   
   //String recuperar_solo_exp_anulados=(String)session.getAttribute("recuperar_solo_exp_anulados");

   TramitacionForm f= (TramitacionForm)  session.getAttribute("TramitacionForm");
   TramitacionValueObject tVO = f.getTramitacion();
   String codDepartamento = tVO.getCodDepartamento();
   String codUnidadRegistro = tVO.getCodUnidadRegistro();
   String tipoRegistro = tVO.getTipoRegistro();
   String ejerNum = tVO.getEjerNum();


/** prueba **/
   String codUorVisibleDestino = f.getCodUnidadDestinoBuzonEntradaHistorico();
   String codInternoUorDestino = f.getCodUnidadInternoDestinoBuzonEntradaHistorico();
   String codUorVisibleanotacion = f.getCodUnidadOrganicaAnotacionHistorico();
   String codInternoUorAnotacion = f.getCodUnidadInternoAnotacionHistorico();
   String unidadRegistroAsuntoClasificacionSeleccionadaHistorico = f.getUnidadRegistroClasifAsuntoSeleccionado();
   
   
   
   String codAsuntoSeleccionadoHistorico = f.getCodAsuntoSeleccionadoHistorico();
   String unidadRegistroAsuntoSeleccionadaHistorico = f.getUnidadRegistroAsuntoSeleccionadoHistorico();
   String tipoAsuntoRegistroAsuntoSeleccionadoHistorico = f.getTipoRegistroAsuntoSeleccionadoHistorico();
/** prueba **/

   int pos=0;
   if ( (codDepartamento !=null) && (codUnidadRegistro !=null) && (tipoRegistro !=null) && (ejerNum !=null)){
    TramitacionValueObject tramitacion;
    boolean encontrada=false;
    while (!encontrada && (pos<relacionAsientosHistorico.size()))
    {
      tramitacion = (TramitacionValueObject) relacionAsientosHistorico.elementAt(pos);
      String d = tramitacion.getCodDepartamento();
      String uR = tramitacion.getCodUnidadRegistro();
      String tR = tramitacion.getTipoRegistro();
      String eN = tramitacion.getEjerNum();
      if ( d.equals(codDepartamento) && uR.equals(codUnidadRegistro) && tR.equals(tipoRegistro) && eN.equals(ejerNum)) {
        encontrada=true;
      }
      else pos++;
    }
    if (!encontrada) pos=0;
   }

%>

var lineasPagina   = 10;
var paginaActual   = 1;
var numeroPaginas;

var numRelacionAsientosHistorico = "<%=numRelacionAsientosHistorico%>";
var codUorVisibleDestino = "<%=codUorVisibleDestino%>";
var codInternoUorDestino = "<%=codInternoUorDestino%>";
var codUorVisibleanotacion = "<%=codUorVisibleanotacion%>";
var codInternoUorAnotacion = "<%=codInternoUorAnotacion%>";
<% if (pluginTecnicoReferenciaActivo) { %>
var unidadRegistroAsuntoClasificacionSeleccionadaHistorico = "<%=unidadRegistroAsuntoClasificacionSeleccionadaHistorico%>";
<% } %>


var codAsuntoSeleccionadoHistorico = "<%=codAsuntoSeleccionadoHistorico%>";
var unidadRegistroAsuntoSeleccionadaHistorico = "<%=unidadRegistroAsuntoSeleccionadaHistorico%>";
var tipoAsuntoRegistroAsuntoSeleccionadoHistorico = "<%=tipoAsuntoRegistroAsuntoSeleccionadoHistorico%>";


/*funciones de fechas*/
function mostrarCalFechaInicio(evento) {
    var evento = (evento) ? evento : ((window.event) ? window.event : "");
    if (document.getElementById("calFechaInicio").className.indexOf("fa-calendar") != -1 )
      showCalendar('forms[0]','fechaDesdeHistorico',null,null,null,'','calFechaInicio','',null,null,null,null,null,null,null,null,evento);
}
function mostrarCalFechaFin(evento) {
    var evento = (evento) ? evento : ((window.event) ? window.event : "");
    if (document.getElementById("calFechaFin").className.indexOf("fa-calendar") != -1 )
      showCalendar('forms[0]','fechaHastaHistorico',null,null,null,'','calFechaFin','',null,null,null,null,null,null,null,null,evento);
}

function comprobarData(inputFecha) {
  if (Trim(inputFecha.value)!='') {
    if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
      jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
      return false;
    }
  }
 return true;
}

function comprobarFecha(inputFecha) {
  var formato = 'dd/mm/yyyy';
  if (Trim(inputFecha.value)!='') {
    if (consultando) {
      var validas = true;
      var fechaFormateada=inputFecha.value;
      var pos=0;
      var fechas = Trim(inputFecha.value);
      var fechas_array = fechas.split(/[:|&<>!=]/);
      for (var loop=0; loop < fechas_array.length; loop++) {
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
        document.getElementById(inputFecha.name).focus();
        document.getElementById(inputFecha.name).select();
        return false;
      } else {
        inputFecha.value = fechaFormateada;
        return true;
      }
    } else { // No consultando. Unico formato posible: dd/mm/yy o dd/mm/yyyy
      var D = ValidarFechaConFormato(inputFecha.value,formato);
      if (!D[0]){
        jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
        document.getElementById(inputFecha.name).focus();
        document.getElementById(inputFecha.name).select();
        return false;
      } else {
        inputFecha.value = D[1];
        return true;
      }
    }
  }
  return true;
}

function ValidarFechaConFormato(fecha, formato) {
  if (formato==null) formato ="dd/mm/yyyy";
  if (formato=="mm/yyyy")
      fecha = "01/"+fecha;
  else if (formato=="yyyy")
      fecha ="01/01/"+fecha;
  else if (formato =="mmyyyy")
      fecha = "01"+fecha;

  var D = DataValida(fecha);
  if (formato == "dd/mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr() : fecha;
  else if (formato == "mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
  else if (formato == "yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(6) : fecha;
  else if (formato == "mmyyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
  return D;
}
/*fin funciones de fechas*/


function cargarInicio() {
    habilitarImagenCal("calFechaInicio",true);
    habilitarImagenCal("calFechaFin",true);
    consultando = true;
    window.focus();
    listaSel = lista;
    numAnotacion = <%= pos%>;
     cargarComboFilasPagina();
    cargaPagina(1);
}

function enlaces() {
    numeroPaginas = Math.ceil(<%=numRelacionAsientosHistorico%> /lineasPagina);
    return enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>',paginaActual,numeroPaginas,'cargaPagina','<%=descriptor.getDescripcion("numResultados")%>', <%=numRelacionAsientosHistorico%>);	
}


function cargaPagina(numeroPagina){     
    var fechaDesde = document.forms[0].fechaDesdeHistorico.value;
    var fechaHasta = document.forms[0].fechaHastaHistorico.value;

    if(codInternoUorDestino!=null && codInternoUorDestino!=""){        
        document.forms[0].codUnidadInternoDestinoBuzonEntradaHistorico.value=codInternoUorDestino;
    }
    
    if(codInternoUorAnotacion!=null && codInternoUorAnotacion!=""){        
        document.forms[0].codUnidadInternoAnotacionHistorico.value=codInternoUorAnotacion;
    }
    
    if(codAsuntoSeleccionadoHistorico!=null && codAsuntoSeleccionadoHistorico!="") 
        document.forms[0].codAsuntoSeleccionadoHistorico.value = codAsuntoSeleccionadoHistorico;
        
    if(unidadRegistroAsuntoSeleccionadaHistorico!=null && unidadRegistroAsuntoSeleccionadaHistorico!="")     
        document.forms[0].unidadRegistroAsuntoSeleccionadoHistorico.value = unidadRegistroAsuntoSeleccionadaHistorico;
    
    if(tipoAsuntoRegistroAsuntoSeleccionadoHistorico!=null && tipoAsuntoRegistroAsuntoSeleccionadoHistorico!="")
        document.forms[0].tipoRegistroAsuntoSeleccionadoHistorico.value   = tipoAsuntoRegistroAsuntoSeleccionadoHistorico;
    
    <% if (pluginTecnicoReferenciaActivo) { %>
    if(unidadRegistroAsuntoClasificacionSeleccionadaHistorico!=null && unidadRegistroAsuntoClasificacionSeleccionadaHistorico!="")
        document.forms[0].unidadRegistroClasifAsuntoSeleccionado.value   = unidadRegistroAsuntoClasificacionSeleccionadaHistorico;
    <% } %>
        
    lista = new Array();
    listaOriginal= new Array();
    document.forms[0].paginaListado.value = numeroPagina;
    document.forms[0].numLineasPaginaListado.value = lineasPagina;
    document.forms[0].opcion.value="cargar_paginaHistorico";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>?fechaDesde=" + fechaDesde + "&fechaHasta=" + fechaHasta;  
    document.forms[0].submit();
}

function cargarComboFilasPagina(){
    var selectorDeFilas = '<select name="filasPagina" id="filasPagina" class ="" onchange="cambiarFilasPagina();">' + 
                                    '<option value="10">10</option>' + 
                                    '<option value="25">25</option>' + 
                                    '<option value="50">50</option>' + 
                                    '<option value="100">100</option>' + 
                                '</select>';
    document.getElementById('contSelectPax').innerHTML = '<%=descriptor.getDescripcion("mosFilasPag")%>'.replace('_MENU_',selectorDeFilas); 
    document.getElementById('filasPagina').value= lineasPagina;
}

function cambiarFilasPagina(){ 
    lineasPagina = document.getElementById('filasPagina').value;

    cargaPagina(1);
}

// Cambia pq ahora es todo el vector.
function inicializaLista(numeroPagina){
    
  var j = 0;

  paginaActual = numeroPagina;
  listaP = new Array();

  inicio =0;
  fin = lineasPagina;
  listaP = listaSel;

  tab.lineas=listaP;
  tab.observaciones=listaObs;
  refresh();

  domlay('enlace',1,0,0,enlaces());

}

function seleccionRegistro(indice) {
  document.forms[0].codDepartamento.value = listaOriginal[indice][0];
  document.forms[0].codUnidadRegistro.value = listaOriginal[indice][1];
  document.forms[0].tipoRegistro.value = listaOriginal[indice][2];
  document.forms[0].ejerNum.value = listaOriginal[indice][3];
  document.forms[0].origenServicio.value = listaOriginal[indice][8];
  document.forms[0].deHistorico.value = "si";
  document.forms[0].opcion.value="consultaAsiento";
  document.forms[0].target="mainFrame";
  document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
  document.forms[0].submit();
}

function pulsarSalir() {
  top.close();
}

function checkKeysLocal(evento,tecla){
    var teclaAuxiliar="";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

    if (teclaAuxiliar == 38 || teclaAuxiliar == 40){
		upDownTable(tab,lista,teclaAuxiliar);
	}

	if(teclaAuxiliar == 13){
		if((tab.selectedIndex>-1)&&(tab.selectedIndex < lista.length)&&(!ultimo)){
			callFromTableTo(tab.selectedIndex,tab.id);
		}
	}
	keyDel(evento);
}

function pulsarRecuperar() {
    document.forms[0].codDepartamento.value = listaOriginal[tab.selectedIndex][0];
    document.forms[0].codUnidadRegistro.value = listaOriginal[tab.selectedIndex][1];
    document.forms[0].tipoRegistro.value = listaOriginal[tab.selectedIndex][2];
    document.forms[0].ejerNum.value = listaOriginal[tab.selectedIndex][3];
    document.forms[0].origenServicio.value = listaOriginal[tab.selectedIndex][8];
    document.forms[0].opcion.value="comprobarExpediente";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
    document.forms[0].submit();
}

function recuperar(){
    if(tab.selectedIndex != -1){
        var bloquearPantalla;
        var permitimosDesasociar;
        var numExpedientesRelacionados;
        
        //Hacemos las comprobaciones pertinentes antes de recuperar la lista de expedientes relacionados
        var ajax = getXMLHttpRequest();
        var salida = "";
        var mensaje = "";
        
        if(ajax!=null){
            
            var cod_departamento = listaOriginal[tab.selectedIndex][0];
            var cod_unidad_reg =   listaOriginal[tab.selectedIndex][1];
            var ejercicio = listaOriginal[tab.selectedIndex][3];
            var tipo_reg = listaOriginal[tab.selectedIndex][2];
            
            var url = "<c:url value='/sge/Tramitacion.do?opcion=comprobacionesDesasociacion'/>";     
            var parametros ="&cod_departamento=" + cod_departamento + "&cod_unidad_reg=" + cod_unidad_reg + "&ejercicio=" + ejercicio +
                "&tipo_reg=" + tipo_reg;

            ajax.open("POST",url,false); 
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);
            
            try{
                if (ajax.readyState==4 && ajax.status==200){
                    var xmlDoc = null;
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        // En IE el XML viene en responseText y no en la propiedad responseXML
                        var text = ajax.responseText;                                
                        xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
                        xmlDoc.async="false";
                        xmlDoc.loadXML(text);
                    }else{
                        // En el resto de navegadores el XML se recupera de la propiedad responseXML
                        xmlDoc = ajax.responseXML;
                    }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                }//if (ajax.readyState==4 && ajax.status==200)               
            }catch(Err){    
                alert("Error.descripcion: " + Err.description);
            }//try-catch
            
            nodos = xmlDoc.getElementsByTagName("RESPUESTA");
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="BLOQUEAR_PANTALLA"){
                    bloquearPantalla = hijos[j].childNodes[0].nodeValue;
                }else if(hijos[j].nodeName=="PERMITIMOS_DESASOCIAR"){
                    permitimosDesasociar = hijos[j].childNodes[0].nodeValue;
                }else if(hijos[j].nodeName=="NUM_EXPEDIENTES_ASOCIADOS"){
                    numExpedientesRelacionados = hijos[j].childNodes[0].nodeValue;
                }//
            }//for(j=0;hijos!=null && j<hijos.length;j++)
            
            if(permitimosDesasociar == "true"){
                if (numExpedientesRelacionados!= "0"){
                    var cod_departamento = listaOriginal[tab.selectedIndex][0];
                    var cod_unidad_reg =   listaOriginal[tab.selectedIndex][1];
                    var ejercicio = listaOriginal[tab.selectedIndex][3];
                    var tipo_reg = listaOriginal[tab.selectedIndex][2];  
                    var source = "<c:url value='/sge/Tramitacion.do?opcion=verAsociados'/>";                        
                    source = source + "&cod_departamento=" + cod_departamento + "&cod_unidad_reg=" + cod_unidad_reg + "&ejercicio=" + ejercicio +
                        "&tipo_reg=" + tipo_reg +"&bloquearPantalla="+bloquearPantalla;	

                    var pantalla = '<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source;           
                    abrirXanelaAuxiliar(pantalla,'',
                            'width=615,height=430,status='+ '<%=statusBar%>',function(resultado){
                                    if (resultado == "@Cancelar"){
                                        document.forms[0].expedientes_eliminar.value = "";
                                    }else{
                                        document.forms[0].expedientes_eliminar.value = resultado;
                                        pulsarRecuperar();
                                    }                     
                            });
                }else{
                    document.forms[0].expedientes_eliminar.value = "";
                    pulsarRecuperar();            
                }//if (numeroExpedientesAsociados!= "0")
            }else{
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgNoPermiteDesasociar")%>');
            }//if(permitimosDesasociar == "true")
        }else{
            jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrorAjax")%>');
        }//if(ajax!=null)
    }else {
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }//if(tab.selectedIndex != -1)
    
}//recuperar


function verMensaje(respOpcion,numExp) {
  if(respOpcion == "expedienteAsociado") {
    var mensaje = '<%=descriptor.getDescripcion("msjAsientAdjExp1")%>' + numExp + '<%=descriptor.getDescripcion("msjAsientAdjExp2")%>';
	var i = jsp_alerta("C",mensaje);
	if(i == 1) {
	  recuperarAsiento();
          jsp_alerta("A",'<%=descriptor.getDescripcion("msgAnotacionOk")%>');
	}
  } else if(respOpcion == "sinExpedienteAsociado") {
    var i = jsp_alerta("C",'<%=descriptor.getDescripcion("msjAsientAdjExp3")%>');
	if(i == 1) {
	  recuperarAsiento();
          jsp_alerta("A",'<%=descriptor.getDescripcion("msgAnotacionOk")%>');
	}
  }
}

function recuperarAsiento() {
  document.forms[0].codDepartamento.value = listaOriginal[tab.selectedIndex][0];
  document.forms[0].codUnidadRegistro.value = listaOriginal[tab.selectedIndex][1];
  document.forms[0].tipoRegistro.value = listaOriginal[tab.selectedIndex][2];
  document.forms[0].ejerNum.value = listaOriginal[tab.selectedIndex][3];
  document.forms[0].origenServicio.value = listaOriginal[tab.selectedIndex][8];
  document.forms[0].opcion.value="recuperarAsiento";
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
  document.forms[0].submit();
}

function pulsarLimpiar(){
     <c:if test="${sessionScope.TramitacionForm.cargarNuevosCriteriosBuzonEntradaHistorico eq true}">
        document.forms[0].ejercicioBuzonEntradaHistorico.value= "";
        document.forms[0].numAnotacionBuzonEntradaHistorico.value= "";
        document.forms[0].documentoBuzonEntradaHistorico.value= "";
        document.forms[0].nombreBuzonEntradaHistorico.value= "";
        document.forms[0].primerApellidoBuzonEntradaHistorico.value= "";
        document.forms[0].segundoApellidoBuzonEntradaHistorico.value= "";
        document.forms[0].codUnidadDestinoBuzonEntradaHistorico.value= "";
        document.forms[0].descUnidadDestinoBuzonEntradaHistorico.value= "";
        document.forms[0].codAsuntoBuzonEntradaHistorico.value= "";
        document.forms[0].descAsuntoBuzonEntradaHistorico.value= "";
        document.forms[0].codUnidadOrganicaAnotacionHistorico.value= "";
        document.forms[0].descUnidadOrganicaAnotacionHistorico.value= "";
        document.forms[0].codRegistroTelematico.value = "";
        document.forms[0].descRegistroTelematico.value = "";
        <% if (pluginTecnicoReferenciaActivo) { %>
        document.forms[0].codClasificacionAsuntos.value = "";
        document.forms[0].descClasificacionAsuntos.value = "";
        document.forms[0].codTecnicoReferencia.value = "";
        document.forms[0].descTecnicoReferencia.value = "";
        document.forms[0].unidadRegistroClasifAsuntoSeleccionado.value = "";
        <% } %>

     </c:if>
 }
function pulsarBuscar() {
    var valido = true;
    if(!comprobarFecha(document.forms[0].fechaDesdeHistorico)) valido = false;
    if(!comprobarFecha(document.forms[0].fechaHastaHistorico)) valido = false;

    document.forms[0].codUnidadInternoDestinoBuzonEntradaHistorico.value = "";
    <c:if test="${sessionScope.TramitacionForm.cargarNuevosCriteriosBuzonEntradaHistorico eq true}">
    var codUorVisibleDestinoBuzonEntrada = document.forms[0].codUnidadDestinoBuzonEntradaHistorico.value;
    if(codUorVisibleDestinoBuzonEntrada!=null && codUorVisibleDestinoBuzonEntrada!=""){
        for(i=0;codigosUorVisible!=null && i<codigosUorVisible.length;i++){
             if(codigosUorVisible[i]==codUorVisibleDestinoBuzonEntrada){
                break;
            }
        }
        document.forms[0].codUnidadInternoDestinoBuzonEntradaHistorico.value = codigosUnidad[i];
    }
    
     document.forms[0].codUnidadInternoAnotacionHistorico.value="";
    
     var codUorVisibleDestinoBuzonEntradaHistorico = document.forms[0].codUnidadOrganicaAnotacionHistorico.value;
    if(codUorVisibleDestinoBuzonEntradaHistorico!=null && codUorVisibleDestinoBuzonEntradaHistorico!=""){
        for(i=0;codigosUorVisible!=null && i<codigosUorVisible.length;i++){
             if(codigosUorVisible[i]==codUorVisibleDestinoBuzonEntradaHistorico){
                break;
            }
        }
        document.forms[0].codUnidadInternoAnotacionHistorico.value = codigosUnidad[i];
    }
    </c:if>


    buscarCodigoAsunto();
    <% if (pluginTecnicoReferenciaActivo) { %>
    buscarCodigoClasificacionAsunto();
    <% } %>
     //Si ejercicio es null entonces si comprobamos las fechas
    if (document.forms[0].ejercicioBuzonEntradaHistorico.value == "" || document.forms[0].ejercicioBuzonEntradaHistorico.value == null){
    if(valido){// si las fechas son correctas
        if(document.forms[0].fechaHastaHistorico.value!="" && document.forms[0].fechaDesdeHistorico.value!=""){//si se introducen las 2 fechas
            if(validarFechaAnterior(document.forms[0].fechaDesdeHistorico.value,document.forms[0].fechaHastaHistorico.value)){//validamos que la fecha fin no sea mayor a la de inicio

                if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
                    pleaseWait('on');
                    document.forms[0].opcion.value="buscar_historico";
                    document.forms[0].target="mainFrame";
                    document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
                    document.forms[0].submit();
                }
            }
        }else{

            if(validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
                pleaseWait('on');
                document.forms[0].opcion.value="buscar_historico";
                document.forms[0].target="mainFrame";
                document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
                document.forms[0].submit();
            }
        }
    }
    }else{
     if (document.forms[0].numAnotacionBuzonEntradaHistorico.value == "" || document.forms[0].numAnotacionBuzonEntradaHistorico.value == null){
                    alert("Debe indicar el numero de asiento");
                    }else{
                        pleaseWait('on');
                        document.forms[0].fechaDesdeHistorico.value = "";
                        document.forms[0].fechaHastaHistorico.value = "";
                        document.forms[0].opcion.value="buscar_historico";
                        document.forms[0].target="mainFrame";
                        document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
                        document.forms[0].submit();
                    }
    }

}
function buscarCodigoAsunto(){
    var codAsunto  = document.forms[0].codAsuntoBuzonEntradaHistorico;
    var descAsunto = document.forms[0].descAsuntoBuzonEntradaHistorico;

    document.forms[0].codAsuntoSeleccionadoHistorico.value            = "";
    document.forms[0].unidadRegistroAsuntoSeleccionadoHistorico.value = "";
    document.forms[0].tipoRegistroAsuntoSeleccionadoHistorico.value   = "";

    if(codAsunto!=null && descAsunto!=null && comboAsunto!=null){

        if(codAsunto.value!="" && descAsunto.value!=""){
            var encontrado = false;
            for(i=0;i<codigosAsuntos.length;i++){
                if(codigosAsuntos[i]==codAsunto.value.trim() && descripcionesAsuntos[i]==descAsunto.value.trim()){
                   encontrado = true;
                   break;
                }
            }

            if(encontrado){
                document.forms[0].codAsuntoSeleccionadoHistorico.value= codigosAsuntos[i];
                document.forms[0].unidadRegistroAsuntoSeleccionadoHistorico.value = unidadRegistroAsuntos[i];
                document.forms[0].tipoRegistroAsuntoSeleccionadoHistorico.value=tiposRegistrosAsuntos[i];
            }
        }

    }
}
<% if (pluginTecnicoReferenciaActivo) { %>
function buscarCodigoClasificacionAsunto(){
    var codClasifAsunto  = document.forms[0].codClasificacionAsuntos;
    var descClasifAsunto = document.forms[0].descClasificacionAsuntos;

    document.forms[0].unidadRegistroClasifAsuntoSeleccionado.value = "";
    
    if(codClasifAsunto!=null && descClasifAsunto!=null){        

        if(codClasifAsunto.value!="" && descClasifAsunto.value!=""){
            var encontrado = false;
            for(i=0;i<codigoAsuntoClasif.length;i++){
                if(codigoAsuntoClasif[i]==codClasifAsunto.value && descripcionAsuntoClasif[i]==descClasifAsunto.value){
                   encontrado = true;
                   break;
                }
            }
            
            if(encontrado){
                document.forms[0].unidadRegistroClasifAsuntoSeleccionado.value = unidadRegistroClasifAsuntoSeleccionado[i];
            }
        }
    }
}
<% } %>


function getXMLHttpRequest(){
  var aVersions = [ "MSXML2.XMLHttp.5.0",
      "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
      "MSXML2.XMLHttp","Microsoft.XMLHttp"
    ];

  if (window.XMLHttpRequest){
          // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
          return new XMLHttpRequest();
  }else if (window.ActiveXObject){
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
</SCRIPT>
</head>
<BODY class="bandaBody" onload="javascript:{pleaseWait('off');cargarInicio();}">
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>
<html:form action="/sge/Tramitacion.do" target="_self">
<html:hidden  property="opcion" value=""/>
<html:hidden  property="expedientes_eliminar" value=""/>
<html:hidden property="paginaListado" value="1"/>
<html:hidden property="numLineasPaginaListado" value="15"/>
<html:hidden  property="codDepartamento" value=""/>
<html:hidden  property="codUnidadRegistro" value=""/>
<html:hidden  property="tipoRegistro" value=""/>
<html:hidden  property="ejerNum" value=""/>
<html:hidden  property="origenServicio" value=""/>
<html:hidden property="codUnidadInternoDestinoBuzonEntradaHistorico" value=""/>
<html:hidden property="codUnidadInternoAnotacionHistorico" value=""/>
<html:hidden property="codAsuntoSeleccionadoHistorico" value=""/>
<html:hidden property="unidadRegistroAsuntoSeleccionadoHistorico" value=""/>
<html:hidden property="tipoRegistroAsuntoSeleccionadoHistorico" value=""/>
<html:hidden property="unidadRegistroClasifAsuntoSeleccionado" value=""/>
<input type="hidden" name="deHistorico" >
<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titAsientHis")%></div>
<div class="contenidoPantalla">
    <!--PAZ:CRITERIOS DE BÚSQUEDCA -->
    <table cellspacing="0px" class="cuadroFondoBlanco" style="margin-top: 1px; margin-bottom: 1px;">
        <tr>
            <td>
                <c:if test="${sessionScope.TramitacionForm.cargarNuevosCriteriosBuzonEntradaHistorico eq true}">
                <!-- NUEVOS CRITERIOS DE BÚSQUEDCA -->
                <table width="100%" border="0">
                <tr>
                    <td width="13%" class="etiqueta"><%=descriptor.getDescripcion("etiqNEntradaBuzonEntrada")%></td>
                    <td colspan="5">
                        <html:text styleClass="inputTexto" property="ejercicioBuzonEntradaHistorico" size="4" maxlength="4"/>
                        <label for="barra">/</label>
                        <html:text styleClass="inputTexto" property="numAnotacionBuzonEntradaHistorico" size="6" maxlength="6"/>
                    </td>
                </tr>
                <tr>
                    <td class="etiqueta"><%=descriptor.getDescripcion("etiqDocBuzonEntrada")%></td>
                    <td width="22%">
                        <html:text styleClass="inputTexto" property="documentoBuzonEntradaHistorico" size="30" maxlength="25"/>
                    </td>
                    <td width="13%" class="etiqueta"><%=descriptor.getDescripcion("etiqNombreBuzonEntrada")%></td>
                    <td colspan="2">
                        <html:text styleClass="inputTexto" property="nombreBuzonEntradaHistorico" size="30" maxlength="80"/>
                    </td>
                </tr>
                <tr>
                    <td  class="etiqueta"><%=descriptor.getDescripcion("etiqApe1BuzonEntrada")%></td>
                    <td  class="etiqueta">
                        <html:text styleClass="inputTexto" property="primerApellidoBuzonEntradaHistorico" size="30" maxlength="100"/>
                    </td>
                    <td width="13%" class="etiqueta"><%=descriptor.getDescripcion("etiqApe2BuzonEntrada")%></td>
                    <td class="etiqueta">
                        <html:text styleClass="inputTexto" property="segundoApellidoBuzonEntradaHistorico" size="30" maxlength="100"/>
                    </td>
                </tr>
                <tr>
                    <td width="13%" class="etiqueta">
                        <%=descriptor.getDescripcion("etiqUniDestBuzonEntrada")%>
                    </td>
                    <td colspan="5">
                        <html:text styleClass="inputTexto" property="codUnidadDestinoBuzonEntradaHistorico" size="9" maxlength="10" onkeyup="return xAMayusculas(this);"/>
                        <html:text styleClass="inputTexto" property="descUnidadDestinoBuzonEntradaHistorico"  style="width:445px;height:17" readonly="true"/>
                        <A href="" style="text-decoration:none;" id="anchorUnidadDestinoBuzonEntradaHistorico" name="anchorUnidad">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonUnidad" name="botonUnidad" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                        </A>
                    </td>
                </tr>
                <tr>
                    <td width="13%" class="etiqueta">
                         <%=descriptor.getDescripcion("etiqUnidOrganica")%>
                    </td>
                    <td colspan="5">
                        <html:text styleClass="inputTexto" property="codUnidadOrganicaAnotacionHistorico" size="9" maxlength="10" onkeyup="return xAMayusculas(this);"/>
                        <html:text styleClass="inputTexto" property="descUnidadOrganicaAnotacionHistorico"  style="width:445px;height:17" readonly="true"/>
                        <A href="" style="text-decoration:none;" id="anchorUnidadOrganicaAnotacionHistorico" name="anchorUnidad">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonUnidad" name="botonUnidad" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                        </A>
                    </td>
                </tr>
                <tr>
                    <td width="13%" class="etiqueta">
                       <%=descriptor.getDescripcion("etiqCodAsuBuzonEntrada")%>
                    </td>
                    <td colspan="5">
                        <html:text styleClass="inputTexto" property="codAsuntoBuzonEntradaHistorico" size="9" maxlength="10" onkeyup="return xAMayusculas(this);"/>
                        <html:text styleClass="inputTexto" property="descAsuntoBuzonEntradaHistorico"  style="width:445px;height:17" readonly="true"/>
                        <A href="" style="text-decoration:none;" id="anchorAsuntoBuzonEntradaHistorico" name="anchorAsunto">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonAsunto" name="botonAsunto" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                        </A>
                    </td>
                </tr>
                
                <% if (pluginTecnicoReferenciaActivo) { %>
                <tr>
                    <td width="13%" class="etiqueta">
                    <%=descriptor.getDescripcion("clasifAsunto")%>
                    </td>
                    <td colspan="5">
                        <html:text styleClass="inputTexto" property="codClasificacionAsuntos" size="9" maxlength="10" onkeyup="return xAMayusculas(this);"/>
                        <html:text styleClass="inputTexto" property="descClasificacionAsuntos"  style="width:445px;height:17" readonly="true"/>
                        <A href="" style="text-decoration:none;" id="anchorClasificacionAsuntos" name="anchorClasificacion">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonClasificacion" name="botonClasificacion" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                        </A>
                    </td>                                                            
                </tr>     
                
                
                <tr>
                    <td width="13%" class="etiqueta">
                        <%=descriptor.getDescripcion("tecnicoRef")%>
                    </td>
                    <td colspan="5">
                        <html:text styleClass="inputTexto" property="codTecnicoReferencia" size="9" maxlength="10" onkeyup="return xAMayusculas(this);"/>
                        <html:text styleClass="inputTexto" property="descTecnicoReferencia"  style="width:445px;height:17" readonly="true"/>
                        <A href="" style="text-decoration:none;" id="anchorTecnicoReferencia" name="anchorTecnico">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonTecnico" name="botonTecnico" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                        </A>
                    </td>                                                            
                </tr> 
                <% } %>
                </table>
                <!-- NUEVOS CRITERIOS DE BÚSQUEDCA -->
                </c:if>
                <table width="100%" border="0">
                    <tr>
                        <td style="width:13%" class="etiqueta"><%=descriptor.getDescripcion("etiqFechDesde")%>:</td>
                        <td style="width:20%" class="columnP">
                            <html:text styleClass="inputTxtFechaObligatorio" size="12" maxlength="10" property="fechaDesdeHistorico"  styleId="fechaDesdeHistorico"
                                       onkeyup = "javascript:if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus = "this.select();"/>
                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaInicio(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
                                <span class="fa fa-calendar" aria-hidden="true" style="border:0px none" id="calFechaInicio" name="calFechaInicio" alt='<%=descriptor.getDescripcion("gEtiqFecha")%>' ></span>
                            </A>
                        </td>
                        <td style="width:10%" class="etiqueta"><%=descriptor.getDescripcion("etiqFechHasta")%>:</td>
                        <td style="width:20%" class="columnP">
                            <html:text styleClass="inputTxtFechaObligatorio" size="12" maxlength="10" property="fechaHastaHistorico" styleId="fechaHastaHistorico"
                                       onkeyup = "javascript:if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus = "this.select();"/>
                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFin(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
                                <span class="fa fa-calendar" aria-hidden="true" style="border:0px none" id="calFechaFin" name="calFechaFin" alt='<%=descriptor.getDescripcion("gEtiqFecha")%>' ></span>
                            </A>
                            <logic:present name="org.apache.struts.action.ERROR">
                                <script type="text/javascript">errorFechas=true;</script>
                            </logic:present>
                        </td>
                         </tr>	
                    <tr>	
                        <td style="width:13%" class="etiqueta"><%=descriptor.getDescripcion("etiqRegTelematico")%></td>	
                        <td>	
                            <html:hidden property="codRegistroTelematico" styleId="codRegistroTelematico" maxlength="1"/>
                            <html:text styleClass="inputTexto" property="descRegistroTelematico" styleId="descRegistroTelematico"  size="12" readonly="true"/>
                            <a id="anchorRegistroTelematico" name="anchorRegistroTelematico" href="">
                                <span class="fa fa-chevron-circle-down enTxttitblanco" aria-hidden="true" id="botonRango" name="botonRango" style="cursor:pointer;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;" ></span>
                            </a>
	
                        </td>	
                        <td align="left" style="width: 100%;" colspan="2">
                            <input type= "button" class="botonGeneral" accesskey="H" value='<%=descriptor.getDescripcion("gbBuscar")%>' name="cmdBuscar" onclick="pulsarBuscar();">
                             <c:if test="${sessionScope.TramitacionForm.cargarNuevosCriteriosBuzonEntrada eq true}">
                                <input type= "button" class="botonGeneral" accesskey="H" value='<%=descriptor.getDescripcion("gbLimpiar")%>' name="cmdLimpiar" onclick="pulsarLimpiar();">
                             </c:if>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <table class="contenidoPestanha">    
        <tr>
            <td>
                <div class="dataTables_wrapper paxinacionDataTables">
                    <label id="contSelectPax"></label>
                </div>
            </td>
        </tr>            
        <tr>
            <td id="tablaLineas"></td>
        </tr>
        <tr>
            <td style="width: 100%">
                <div id="enlace" class="dataTables_wrapper"></div>
                </td>
        </tr>
    </table>
     </div>
      <div class="capaFooter">
    <DIV id="capaBotonesConsulta" name="capaBotonesConsulta" class="botoneraPrincipal" >
        <input type="button" class="botonGeneral" accesskey="R" value='<%=descriptor.getDescripcion("gbRecuperar")%>' name="cmdRecuperar" onclick="recuperar();return false;">
        <input type="button" class="botonGeneral" accesskey="C" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="cmdSalir" onclick="pulsarSalir();return false;">
    </DIV>
</DIV>

</html:form>
<script type="text/javascript">
  var tab = new Tabla(false,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaLineas'));

  tab.addColumna('90','left','<%= descriptor.getDescripcion("etiq_ejNum")%>');
  tab.addColumna('140','center','<%= descriptor.getDescripcion("gEtiq_fAnot")%>');
  tab.addColumna('240','left','<%= descriptor.getDescripcion("gEtiq_Remit")%>');
  tab.addColumna('340','left','<%= descriptor.getDescripcion("rotulo_asunto")%>');
  tab.addColumna('120','left','<%= descriptor.getDescripcion("etiq_estado")%>');
  tab.addColumna('340','left','tecnico de referencia')
  tab.displayCabecera=true;
  
  tab.colorLinea=function(rowID){
    var estado = listaOriginal[rowID][7];
    if(estado=="RECHAZADA") {
      return 'rojo';
    }
    // Se añade este codigo para los asientos
    // con observaciones que hay que resaltar    
    if (listaObs.length > rowID) {
      if (listaObs[rowID]) return 'inactivaObs';
      else return 'inactiva';
    } else {
      return 'inactiva';
    }
  }

function refresh(){
  tab.displayTabla();
}

  function callFromTableTo(rowID,tableName){
    if(tab.id == tableName){
      fila=parseInt(rowID);
      seleccionRegistro(fila);
    }
  }

    /*******************************************************************/
    /****************** DEFINICIÓN DE LOS COMBOS ***********************/
    /*******************************************************************/


    /*** COMBO PARA LA UNIDAD DE DESTINO DEL BUZÓN DE ENTRADA ***/
    var comboUnidad = null;
    var comboUnidad = null;
    var comboAsunto = null;
    var comboUor = null;
    var comboRegistroTelematico =new Combo("RegistroTelematico");
    cargarComboRegistroTelematico();

    <c:if test="${sessionScope.TramitacionForm.cargarNuevosCriteriosBuzonEntradaHistorico eq true}">
        
        <% if (pluginTecnicoReferenciaActivo) { %>
        var comboClasificacion = null;
    comboClasificacion = new Combo("ClasificacionAsuntos");
    var codigoAsuntoClasif = new Array();
    var descripcionAsuntoClasif = new Array();
    var unidadRegistroClasifAsuntoSeleccionado = new Array();
    
    var contador = 0;
    <logic:iterate name="TramitacionForm" property="listaClasificacionAsuntos" id="unidad">
       codigoAsuntoClasif[contador]       = '<bean:write name="unidad" property="codigo" ignore="true"/>';
        descripcionAsuntoClasif[contador]   = '<bean:write name="unidad" property="descripcion" ignore="true"/>';
        unidadRegistroClasifAsuntoSeleccionado[contador] = '<bean:write name="unidad" property="unidadRegistro" ignore="true"/>';
        contador++;
    </logic:iterate>
    
    comboClasificacion.addItems(codigoAsuntoClasif,descripcionAsuntoClasif);
    
    var comboTecnicoReferencia = null;
    comboTecnicoReferencia = new Combo("TecnicoReferencia");
    var nifTecnico = new Array();
    var nomeTecnico = new Array();
    var nomeCompletoTecnico = new Array();
    
     var contador = 0;
    <logic:iterate name="TramitacionForm" property="tecnicosReferencia" id="unidad">
        nifTecnico[contador]       = '<bean:write name="unidad" property="documentosIdentificacion" ignore="true"/>';
        nomeTecnico[contador]   =  '<bean:write name="unidad" property="apellido1" ignore="true"/> <bean:write name="unidad" property="apellido2" ignore="true"/>, <bean:write name="unidad" property="nombre" ignore="true"/>';
        contador++;
    </logic:iterate>

    comboTecnicoReferencia.addItems(nifTecnico,nomeTecnico);    
     <% } %>   
   
    comboUnidad = new Combo("UnidadDestinoBuzonEntradaHistorico");
    var codigosUnidad = new Array();
    var codigosUorVisible = new Array();
    var descripcionesUnidad = new Array();
    var contador = 0;
    <logic:iterate name="TramitacionForm" property="listaUnidadesDestinoBuzon" id="unidad">
        codigosUnidad[contador]       = '<bean:write name="unidad" property="uor_cod" ignore="true"/>';
        codigosUorVisible[contador]   = '<bean:write name="unidad" property="uor_cod_vis" ignore="true"/>';
        descripcionesUnidad[contador] = '<bean:write name="unidad" property="uor_nom" ignore="true"/>';
        contador++;
    </logic:iterate>

    comboUnidad.addItems(codigosUorVisible,descripcionesUnidad);
    
    comboUor = new Combo("UnidadOrganicaAnotacionHistorico");
    comboUor.addItems(codigosUorVisible,descripcionesUnidad);


    /*** COMBO PARA LA UNIDAD DE DESTINO DEL BUZÓN DE ENTRADA ***/
    comboAsunto = new Combo("AsuntoBuzonEntradaHistorico");
    var codigosAsuntos        = new Array();
    var descripcionesAsuntos  = new Array();
    var codigosInternosAsunto = new Array();
    var unidadRegistroAsuntos = new Array();
    var tiposRegistrosAsuntos = new Array();

    var contador = 0;
    <logic:iterate name="TramitacionForm" property="listaAsuntosCodificadosBuzon" id="asunto">
        unidadRegistroAsuntos[contador] = '<bean:write name="asunto" property="unidadRegistro" ignore="true"/>';
        tiposRegistrosAsuntos[contador] = '<bean:write name="asunto" property="tipoRegistro" ignore="true"/>';
        codigosAsuntos[contador]        = '<bean:write name="asunto" property="codigo" ignore="true"/>';
        descripcionesAsuntos[contador]  = '<bean:write name="asunto" property="descripcion" ignore="true"/>';
        contador++;
    </logic:iterate>

    comboAsunto.addItems(codigosAsuntos,descripcionesAsuntos);
    </c:if>


    /*******************************************************************/
    /****************** DEFINICIÓN DE LOS COMBOS ***********************/
    /*******************************************************************/

    <c:if test="${not empty requestScope.busquedaExtendidaDesactivada}">
        var msjErrBusqExtendida = new Array();
        var i = 0;
        <c:forEach items="${requestScope.busquedaExtendidaDesactivada}" var="error">
            msjErrBusqExtendida[i++] = '<fmt:message key="BusquedaTercero/busquedaExtendidaDesactivada"/> <c:out value="${error}"/>';
        </c:forEach>
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/listadoErrores.jsp',msjErrBusqExtendida,
	'width=420,height=300,status='+ '<%=statusBar%>',function(){});
    </c:if>
        
     function cargarComboRegistroTelematico(){
         var codRegistroTelematico = new Array();
         var descripcionRegistroTelematico = new Array();
         
         codRegistroTelematico[0] = "0";
         descripcionRegistroTelematico[0] = '<%=descriptor.getDescripcion("etiq_Si")%>';
         codRegistroTelematico[1] = "1";
         descripcionRegistroTelematico[1] = '<%=descriptor.getDescripcion("etiq_No")%>';
         
         comboRegistroTelematico.addItems(codRegistroTelematico,descripcionRegistroTelematico);
     }   
</script>
</BODY>
</html:html>
