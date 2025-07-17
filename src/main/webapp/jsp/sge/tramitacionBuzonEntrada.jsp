<!DOCTYPE html>
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
    <script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>" media="screen" >
    <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    <style type="text/css">
       TR.rojo TD {color:#d74328;}
       TR.negrita TD {font-weight: bold;}
    </style>

<SCRIPT type="text/javascript">
    
<%  String desdeFichaExpediente = "";
    if (session.getAttribute("desdeFichaExpediente") != null) {
      if ("si".equals((String) session.getAttribute("desdeFichaExpediente"))){
        desdeFichaExpediente="si";
      }
      session.removeValue("desdeFichaExpediente");
    }
      String idSesion = session.getId();
/* Recupera el vector de asientos de la sesion. */

   Vector relacionAsientos = (Vector) session.getAttribute("RelacionAsientos");
   int numRelacionAsientos = 0;
   if ( relacionAsientos != null ) numRelacionAsientos = relacionAsientos.size();

   TramitacionForm f= (TramitacionForm)  session.getAttribute("TramitacionForm");
   TramitacionValueObject tVO = f.getTramitacion();
   String codDepartamento = tVO.getCodDepartamento();
   String codUnidadRegistro = tVO.getCodUnidadRegistro();
   String tipoRegistro = tVO.getTipoRegistro();
   String ejerNum = tVO.getEjerNum();
   int pos=0;
   if ( (codDepartamento !=null) && (codUnidadRegistro !=null) && (tipoRegistro !=null) && (ejerNum !=null)){
    TramitacionValueObject tramitacion = new TramitacionValueObject();
    boolean encontrada=false;
    while (!encontrada && (pos<relacionAsientos.size()))
    {
      tramitacion = (TramitacionValueObject) relacionAsientos.elementAt(pos);
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

   Config registroCong = ConfigServiceHelper.getConfig("Registro");
   boolean prueba = f.getCargarNuevosCriteriosBuzonEntrada();
   
%>

        <c:if test='${not empty requestScope.errorCargaBuzon}'>
            jsp_alerta('A', '<c:out value='${requestScope.errorCargaBuzon}'/>');
        </c:if>
        <c:if test="${not empty requestScope.errores}">
            var msjErrores = new Array();
            var i = 0;
            <c:forEach items="${requestScope.errores}" var="error">
                msjErrores[i++] = '<fmt:message key="BusquedaTercero/falloBusqueda"/> <c:out value="${error}"/>';
            </c:forEach>
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/listadoErrores.jsp',msjErrores,
	'width=420,height=300,status=no',function(){});
        </c:if>

var dataTable = null;
var lista = new Array();
var listaOriginal = new Array();
var listaP = new Array();
var listaSel = new Array();
var fila;
var ultimo = false;
var pagin;
var consultando = false;
var errorFechas = false;

/* Para navegacion Buzon*/
var lineasPagina   = 10;
var paginaActual   = 1;

var anotacionesBuzonCheck =  new Array();
var valoresAnotacionesBuzonCheck =  new Array();

//tabla parametrizable
var cols =new Array(); 
var listaCampos=new Array();
//vector que me indica que campos estan activos en las columnas para saber por cual ordenar.
var listaOrden=new Array();
var idOrden=0;
var columnaOrden=0;
var tipoOrdenacion="true";
var indiceAsuntoSeleccionado  = 0;
// fin tabla parametrizable

var numeroPaginas=Math.ceil(<%=numRelacionAsientos%>/lineasPagina);

// Fin navegacion Buzon

function mostrarCalFechaInicio(evento) {
    var evento = (evento) ? evento : ((window.event) ? window.event : "");
    if (document.getElementById("calFechaInicio").className.indexOf("fa-calendar") != -1 )
      showCalendar('forms[0]','fechaDesde',null,null,null,'','calFechaInicio','',null,null,null,null,null,null,null,null,evento);
}
function mostrarCalFechaFin(evento) {
    var evento = (evento) ? evento : ((window.event) ? window.event : "");
    if (document.getElementById("calFechaFin").className.indexOf("fa-calendar") != -1 )
      showCalendar('forms[0]','fechaHasta',null,null,null,'','calFechaFin','',null,null,null,null,null,null,null,null,evento);
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

function inicializar() {
  // Inicializamos con valores 0 las listas referentes a los checks para que no envie valores undefined
  inicializaListasChecks();  
  habilitarImagenCal("calFechaInicio",true);
  habilitarImagenCal("calFechaFin",true);
  consultando = true;
  window.focus();
  listaSel = lista;
  numAnotacion = <%= pos%>;
   var i=1;
      <logic:iterate id="campos" name="TramitacionForm" property="camposListados">
             <logic:equal name="campos" property="nomCampo" value="CHECK">
                 listaCampos[0] = ['<bean:write name="campos" property="codCampo" />',
                '<bean:write name="campos" property="nomCampo" />',
                '<bean:write name="campos" property="tamanoCampo"/>',
                '<bean:write name="campos" property="actCampo"/>',
                '<bean:write name="campos" property="ordenCampo"/>'];
            </logic:equal>

            <logic:notEqual name="campos" property="nomCampo" value="CHECK">
                listaCampos[i++] = ['<bean:write name="campos" property="codCampo" />',
                '<bean:write name="campos" property="nomCampo" />',
                 '<bean:write name="campos" property="tamanoCampo"/>',
                '<bean:write name="campos" property="actCampo"/>',
                '<bean:write name="campos" property="ordenCampo"/>'];
            </logic:notEqual>
    </logic:iterate>
        /*var i = 0;
        for (i ; i < listaCampos.length ; i++){
            alert("recorremos lista");
            alert("longitud lista : " + listaCampos.length);
            alert("lista campos codigo :"+ listaCampos[i][0]  +" en 2 " + listaCampos[i][2]);
        }*/
    
 var cont = 0;

//creamos la tabla segun los campos de la base de datos
cols=[{title:"<i class='fa fa-check' aria-hidden='true'></i>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("rotulo_estado")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("rotulo_proc")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("rotulo_exp")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("rotulo_usu")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("rotulo_numAnot")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("rotulo_tipo")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("rotulo_fechaPres")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("rotulo_fechaGrab")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("rotulo_remite")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("rotulo_asunto")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("etiqObs")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("rotulo_uniDestino")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("rotulo_entrada")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("res_tipoTrans")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("gEtiq_unidOrg")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("gEtiq_CodAsunto")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},	
            {title:"<%=descriptor.getDescripcion("gEtiq_Asunto")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("tecnicoRef")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"}];
 
      var i = 0;
      for(cont=0;cont<listaCampos.length;cont++){   
         if(listaCampos[cont][3]=='NO'){
            cols[cont].visible = false;
         }else if(listaCampos[cont][3]=='SI'){ 
             listaOrden[i++]=listaCampos[cont][4];
            if(listaCampos[cont][4]==idOrden)
                columnaOrden=cont;
        }
     }  
 cargarComboFilasPagina();
 cargaPagina(1);
}

function cargaPagina(numeroPagina){ 
  lista = new Array();
  listaOriginal= new Array();
  document.forms[0].paginaListado.value = numeroPagina;
  document.forms[0].numLineasPaginaListado.value = lineasPagina;  
  <%
     String codAsuntoSeleccionado = (String)request.getAttribute("cod_asunto_seleccionado");
     String tipoRegistroAsuntoSeleccionado = (String)request.getAttribute("tipo_registro_asunto_seleccionado");
     String unidadRegistroAsuntoSeleccionado = (String)request.getAttribute("unidad_registro_asunto_seleccionado");
     
     if(codAsuntoSeleccionado!=null && !"".equals(codAsuntoSeleccionado)){
  %>
        document.forms[0].codAsuntoSeleccionado.value = '<%=codAsuntoSeleccionado %>';
        document.forms[0].unidadRegistroAsuntoSeleccionado.value = '<%=unidadRegistroAsuntoSeleccionado %>';
        document.forms[0].tipoRegistroAsuntoSeleccionado.value = '<%=tipoRegistroAsuntoSeleccionado %>';
        
  <%
    }
  %>
          
  <%if (registroCong.getString("INTEGRACION_TECNICO_REFERENCIA").equals("SI")) { %>
  <% String unidadRegistroAsuntoClasificacionSeleccionado =  (String)request.getAttribute("unidad_registro_asunto_clasificacion_seleccionado"); %>
        document.forms[0].unidadRegistroClasifAsuntoSeleccionado.value = '<%=unidadRegistroAsuntoClasificacionSeleccionado %>';
  <% } %>
  document.forms[0].opcion.value="cargar_pagina_entrada";
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
  document.forms[0].submit();
}

function inicializaLista(numeroPagina){
    //tableObject=tabBuzon;
    var j = 0;
    paginaActual = numeroPagina;
    listaP = new Array();

    listaP = listaSel;

    if (dataTable!=null)
        dataTable.destroy();

    dataTable = $("#tablaDatos").DataTable( {
            data: listaSel,
            aoColumns: cols,
            "sort" : false,
            "info" : false,
            "paginate" : false,
            "autoWidth": false,
            "language": {
                "search": "<%=descriptor.getDescripcion("buscar")%>",
                "previous": "<%=descriptor.getDescripcion("anterior")%>",
                "next": "<%=descriptor.getDescripcion("siguiente")%>",
                "lengthMenu": "<%=descriptor.getDescripcion("mosFilasPag")%>",
                "zeroRecords": "<%=descriptor.getDescripcion("msgNoResultBusq")%>",
                "info": "<%=descriptor.getDescripcion("mosPagDePags")%>",
                "infoEmpty": "<%=descriptor.getDescripcion("noRegDisp")%>",
                "infoFiltered": "<%=descriptor.getDescripcion("filtrDeTotal")%>"
              }
          } );

    for(var i=0;i<=listaSel.length;i++){
        $('#tablaDatos tbody tr:nth-child('+(i+1)+')').attr('id', i);
    }

    $("#tablaDatos thead tr th").each(function (index,value) {
        if (listaOrden[index]==-1 || listaOrden[index] == undefined){
          value.onclick = function() {jsp_alerta('A','<%=descriptor.getDescripcion("msjNoOrdenacion")%>');} 
      } else {
          $("#tablaDatos thead tr th:nth-child("+(index+1)+")").removeClass("sorting sorting_asc sorting_desc ");
          if (columnaOrden!=index) 
              $("#tablaDatos thead tr th:nth-child("+(index+1)+")").addClass("sorting");
          else if (tipoOrdenacion=="true")
              $("#tablaDatos thead tr th:nth-child("+(index+1)+")").addClass("sorting_asc");
          else 
              $("#tablaDatos thead tr th:nth-child("+(index+1)+")").addClass("sorting_desc");

          value.onclick = function() { 
            $("#tablaDatos thead tr th").off("click");
            if (columnaOrden==index){
                tipoOrdenacion = (tipoOrdenacion=="true"?"false":"true");
            }
            idOrden = listaOrden[index];
            columnaOrden=index;
            document.forms[0].paginaListado.value = 1; //paso 1 pq si cambia el orden tiene que volver a la pagina 1
            document.forms[0].numLineasPaginaListado.value = lineasPagina;
            document.forms[0].columna.value = listaOrden[index];//pasamos el id del campo a ordenar
            document.forms[0].tipoOrden.value =tipoOrdenacion; 
            document.forms[0].opcion.value = "cargar_pagina_entrada";
            document.forms[0].target = "oculto";
            document.forms[0].action = "<c:url value='/sge/Tramitacion.do'/>";
            document.forms[0].submit();
          }
      }
    }); 
        
    $("#tablaDatos tbody tr").on("dblclick", function() {
         consultarAsiento(this.id);
    });
	actualizarChecksPagina();
    domlay('enlace',1,0,0,enlaces());
    if (errorFechas)
        jsp_alerta("A",'<%=descriptor.getDescripcion("etiqErrorFechas")%>');
    
    }
     function inicializaListasChecks(){
        for(var index=0; index< <%=numRelacionAsientos%>; index++){
            anotacionesBuzonCheck[index] = 0;
            valoresAnotacionesBuzonCheck[index] = 0;
        }
    }
    
    
    function actualizarChecksPagina(){
        var pagina = (paginaActual-1)*lineasPagina;
        var checksPagina = $('.checkLinea');
        
        $.each(checksPagina,function(index,value){
            if(anotacionesBuzonCheck[pagina+index]==1) {
                $(value).prop("checked", "checked");
            } else {
                $(value).prop("checked", "");
            }
        });
    }

    function pulsarCheck(){
        var pagina = (paginaActual-1)*lineasPagina;
        var checksPagina = $('.checkLinea');
        
        $.each(checksPagina,function(index,value){
            var valor = $(value).val();
            if( $(value).is(':checked')) {
                anotacionesBuzonCheck[pagina+index] = 1;
                valoresAnotacionesBuzonCheck[pagina+index] = valor;
            } else {
                anotacionesBuzonCheck[pagina+index] = 0;
                valoresAnotacionesBuzonCheck[pagina+index] = 0;
            }
        });
}
// JAVASCRIPT DE LA TABLA DE BUZON ************************************************************

function enlaces() {
    numeroPaginas = Math.ceil(<%=numRelacionAsientos%> /lineasPagina);
    return enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>',paginaActual,numeroPaginas,'cargaPaginaB','<%=descriptor.getDescripcion("numResultados")%>', <%=numRelacionAsientos%>);
	
}

function cargaPaginaB(numeroPagina){
    lista = new Array();
    listaOriginal= new Array();
    document.forms[0].paginaListado.value = numeroPagina;
    document.forms[0].numLineasPaginaListado.value = lineasPagina;
    document.forms[0].columna.value = idOrden;
    document.forms[0].tipoOrden.value =tipoOrdenacion;
    document.forms[0].opcion.value="cargar_pagina_entrada";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
    document.forms[0].submit();
}

// FIN DEL JAVASCRIPT DE LA TABLA DE BUZON **************************************************************


function pulsarSalir() {
   window.location = "<%=request.getContextPath()%>/jsp/sge/presentacionExped.jsp";
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

    cargaPaginaB(1);
}

function cargarExpediente(i) {
  pleaseWait('on');
  document.forms[0].codMunicipio.value = listaOriginalE[i][0];
  document.forms[0].codProcedimiento.value = listaOriginalE[i][1];
  document.forms[0].ejercicio.value = listaOriginalE[i][3];
  document.forms[0].numero.value = listaOriginalE[i][4];
  document.forms[0].opcion.value="cargar";
  document.forms[0].target="mainFrame";
  document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
  document.forms[0].submit();
}

function consultarAsiento(i) {
  pleaseWait('on');
  document.forms[0].codDepartamento.value = listaOriginal[i][0];
  document.forms[0].codUnidadRegistro.value = listaOriginal[i][1];
  document.forms[0].tipoRegistro.value = listaOriginal[i][2];
  document.forms[0].ejerNum.value = listaOriginal[i][3];
  document.forms[0].origenServicio.value = listaOriginal[i][7];
  document.forms[0].opcion.value="consultaAsiento";

  document.forms[0].target="mainFrame";
  document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
  document.forms[0].submit();
}

function pulsarHistorico() {
  var argumentos = new Array();
  argumentos[0] = this;
  var source = "<c:url value='/sge/Tramitacion.do?opcion=historico'/>";
  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,argumentos,
	'width=992,height=800,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){

                        }
                  });
}

function pulsarBuscar() {
    var valido = true;
    if(!comprobarFecha(document.forms[0].fechaDesde)) valido = false;
    if(!comprobarFecha(document.forms[0].fechaHasta)) valido = false;

    <c:if test="${sessionScope.TramitacionForm.cargarNuevosCriteriosBuzonEntrada eq true}">
    var codUorVisibleDestinoBuzonEntrada = document.forms[0].codUnidadDestinoBuzonEntrada.value;
    if(codUorVisibleDestinoBuzonEntrada!=null && codUorVisibleDestinoBuzonEntrada!=""){
        
        for(i=0;codigosUorVisible!=null && i<codigosUorVisible.length;i++){
            if(codigosUorVisible[i]==codUorVisibleDestinoBuzonEntrada){
                break;
            }
        }        
        document.forms[0].codUnidadInternoDestinoBuzonEntrada.value = codigosUnidad[i];
    }
    
    
    var codUorVisibleAnotacion = document.forms[0].codUnidadOrganicaAnotacion.value;
    if(codUorVisibleAnotacion!=null && codUorVisibleAnotacion!=""){
        
        for(i=0;codigosUorVisible!=null && i<codigosUorVisible.length;i++){
            if(codigosUorVisible[i]==codUorVisibleAnotacion){
                break;
            }
        }        
        document.forms[0].codUnidadInternoAnotacion.value = codigosUnidad[i];
    }
    
    buscarCodigoAsunto();
    <%if (registroCong.getString("INTEGRACION_TECNICO_REFERENCIA").equals("SI")) { %>
    buscarCodigoClasificacionAsunto();
    <%  } %>
    </c:if>

    //Si ejercicio es null entonces si comprobamos las fechas
    <c:if test="${sessionScope.TramitacionForm.cargarNuevosCriteriosBuzonEntrada eq true}">
    if (document.forms[0].ejercicioBuzonEntrada.value == "" || document.forms[0].ejercicioBuzonEntrada.value == null){
    </c:if>    
        if(valido){// si las fechas son correctas
            if(document.forms[0].fechaHasta.value!="" && document.forms[0].fechaDesde.value!=""){//si se introducen las 2 fechas
                if(validarFechaAnterior(document.forms[0].fechaDesde.value,document.forms[0].fechaHasta.value)){//validamos que la fecha fin no sea mayor a la de inicio

                    if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
                        pleaseWait('on');
                        document.forms[0].opcion.value="buscar_buzon_entrada";                        
                        document.forms[0].target="mainFrame";
                        document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
                        document.forms[0].submit();
                    }
                }
            }else{

                if(validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
                    pleaseWait('on');
                    document.forms[0].opcion.value="buscar_buzon_entrada";                    
                    document.forms[0].target="mainFrame";
                    document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
                    document.forms[0].submit();
                }
            }
        }
    <c:if test="${sessionScope.TramitacionForm.cargarNuevosCriteriosBuzonEntrada eq true}">
    }else{//se borrran las fechas porque se ha cubierto el ejercicio
     if (document.forms[0].numAnotacionBuzonEntrada.value == "" || document.forms[0].numAnotacionBuzonEntrada.value == null){
        alert("Debe indicar el numero de asiento");
    }else{
            pleaseWait('on');
            document.forms[0].fechaDesde.value = "";
            document.forms[0].fechaHasta.value = "";
            document.forms[0].opcion.value="buscar_buzon_entrada";
            document.forms[0].target="mainFrame";
            document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
            document.forms[0].submit();
            //document.forms[0].fechaDesde.value = "";
            //document.forms[0].fechaHasta.value = "";
        }
    }
    </c:if>  
}

function buscarCodigoAsunto(){
    var codAsunto  = document.forms[0].codAsuntoBuzonEntrada;
    var descAsunto = document.forms[0].descAsuntoBuzonEntrada;

    document.forms[0].codAsuntoSeleccionado.value            = "";
    document.forms[0].unidadRegistroAsuntoSeleccionado.value = "";
    document.forms[0].tipoRegistroAsuntoSeleccionado.value   = "";
    
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
                document.forms[0].codAsuntoSeleccionado.value= codigosAsuntos[i];
                document.forms[0].unidadRegistroAsuntoSeleccionado.value = unidadRegistroAsuntos[i];
                document.forms[0].tipoRegistroAsuntoSeleccionado.value=tiposRegistrosAsuntos[i];
            }
        }
    }
}


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



 function pulsarLimpiar(){
     <c:if test="${sessionScope.TramitacionForm.cargarNuevosCriteriosBuzonEntrada eq true}">
        document.forms[0].ejercicioBuzonEntrada.value= "";
        document.forms[0].numAnotacionBuzonEntrada.value= "";
        document.forms[0].documentoBuzonEntrada.value= "";
        document.forms[0].nombreBuzonEntrada.value= "";
        document.forms[0].primerApellidoBuzonEntrada.value= "";
        document.forms[0].segundoApellidoBuzonEntrada.value= "";
        document.forms[0].codUnidadDestinoBuzonEntrada.value= "";
        document.forms[0].descUnidadDestinoBuzonEntrada.value= "";
        document.forms[0].codAsuntoBuzonEntrada.value= "";
        document.forms[0].descAsuntoBuzonEntrada.value= "";
        document.forms[0].codAsuntoSeleccionado.value = "";
        document.forms[0].unidadRegistroAsuntoSeleccionado.value = "";
        document.forms[0].tipoRegistroAsuntoSeleccionado.value = "";
        document.forms[0].codUnidadOrganicaAnotacion.value= "";
        document.forms[0].descUnidadOrganicaAnotacion.value= "";
        document.forms[0].codRegistroTelematico.value= "";
        document.forms[0].descRegistroTelematico.value= "";
        
        document.forms[0].codRegPendCatalogacion.value= "";
        document.forms[0].descRegPendCatalogacion.value= "";
     
        <%if (registroCong.getString("INTEGRACION_TECNICO_REFERENCIA").equals("SI")) { %>
        document.forms[0].codClasificacionAsuntos.value = "";
        document.forms[0].descClasificacionAsuntos.value = "";
        document.forms[0].codTecnicoReferencia.value = "";
        document.forms[0].descTecnicoReferencia.value = "";
        document.forms[0].unidadRegistroClasifAsuntoSeleccionado.value = "";
        <% }  %>
        
     </c:if>
 }
 
 function cargarComboRegistroTelematico(){
       var codigosRangos = new Array();
       var descripcionesRangos = new Array();

       codigosRangos[0]         = "0";
       descripcionesRangos[0] = '<%=descriptor.getDescripcion("etiq_Si")%>';
       codigosRangos[1]         = "1";
       descripcionesRangos[1] = '<%=descriptor.getDescripcion("etiq_No")%>';
       
       comboRegistroTelematico.addItems(codigosRangos,descripcionesRangos);
 }
 
 function cargarComboRegPendCatalogacion(){
       var codigosRangos = new Array();
       var descripcionesRangos = new Array();

       codigosRangos[0]         = "0";
       descripcionesRangos[0] = '<%=descriptor.getDescripcion("etiq_Si")%>';

       comboRegPendCatalogacion.addItems(codigosRangos,descripcionesRangos);

 }
 
 function pulsarPrevioImprimir(){
        var salida        = "";
        var separador = '§¥';
        var contador   = 0;
        if(listaSel.length>0){
        for(i=0;i<valoresAnotacionesBuzonCheck.length;i++){
            if(valoresAnotacionesBuzonCheck[i]!=0){ 
                salida = salida + valoresAnotacionesBuzonCheck[i];
                contador++;
                if(valoresAnotacionesBuzonCheck.length-i>1)
                    salida = salida + separador
            }
       }// for

       if(contador>0 ){
            exportarCSV(salida);	
       } else if(contador==0){
            // El usuario no ha seleccionado ninguna anotación
            if(jsp_alerta("C","<%=descriptor.getDescripcion("msgErrorAnotacionSel")%>"+ " <%=descriptor.getDescripcion("msgExportarTodoLista")%>")){	
                exportarCSV("");	
            }	
            	
       }
       }else{
            jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorNoResultadosExportar")%>");
       }
 }
 
 function exportarCSV(anotaciones){
        pleaseWait('on');	
        document.forms[0].tipoRegistro.value="E";	
        document.forms[0].listaAnotacionesBuzon.value=anotaciones;	
        document.forms[0].opcion.value="exportarBuzonEntradaCSV";	
        document.forms[0].target="oculto";	
        document.forms[0].action="<%=request.getContextPath()%>/InformesRegistro.do";	
        document.forms[0].submit();
 }
 
 function abrirInformeCSV(nombre,dir) {
    pleaseWait('off');	
    var source = "<c:url value='/jsp/verPdf.jsp?opcion=null&tipoFichero=csv&nombre='/>" + nombre+"&dir="+dir;
    document.getElementById('iframe_buzon_csv').src = source;	
}
</SCRIPT>


</head>

<BODY class="bandaBody" onload="javascript:{ pleaseWait('off');  inicializar()}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include> 

<html:form action="/sge/Tramitacion.do" target="_self">

<html:hidden  property="opcion" value=""/>
<html:hidden property="paginaListado" value="1"/>
<html:hidden property="numLineasPaginaListado" value="12"/>
<html:hidden  property="codDepartamento" value=""/>
<html:hidden  property="codUnidadRegistro" value=""/>
<html:hidden  property="tipoRegistro" value=""/>
<html:hidden  property="ejerNum" value=""/>
<html:hidden property="origenServicio" value = ""/>

<input type="hidden" name="codMunicipio" >
<input type="hidden" name="codProcedimiento" >
<input type="hidden" name="ejercicio" >
<input type="hidden" name="numero" >
<input type="hidden" name="uor" >
    
    
<html:hidden property="columna" value=""/>
<html:hidden property="tipoOrden" value=""/>

<html:hidden property="codUnidadInternoDestinoBuzonEntrada" value=""/>
<html:hidden property="codUnidadInternoAnotacion" value=""/>


<html:hidden property="codAsuntoSeleccionado" value=""/>
<html:hidden property="unidadRegistroAsuntoSeleccionado" value=""/>
<html:hidden property="tipoRegistroAsuntoSeleccionado" value=""/>
<%if (registroCong.getString("INTEGRACION_TECNICO_REFERENCIA").equals("SI")) { %>
<html:hidden property="unidadRegistroClasifAsuntoSeleccionado" value=""/>
<% } %>
<html:hidden property="listaAnotacionesBuzon" value=""/>


<div class="txttitblanco"><%=descriptor.getDescripcion("tit_BuzEnt")%></div>
<div class="contenidoPantalla">						
    <table style="width: 100%;">
        <tr>
            <td>
                <c:if test="${sessionScope.TramitacionForm.cargarNuevosCriteriosBuzonEntrada eq true}">
                <!-- NUEVOS CRITERIOS DE BÚSQUEDCA -->
                <table width="100%" border="0">
                <tr>
                    <td width="13%" class="etiqueta"><%=descriptor.getDescripcion("etiqNEntradaBuzonEntrada")%></td>
                    <td colspan="5">
                        <html:text styleClass="inputTexto" property="ejercicioBuzonEntrada" size="4" maxlength="4"/>
                        <label for="barra">/</label>
                        <html:text styleClass="inputTexto" property="numAnotacionBuzonEntrada" size="6" maxlength="6"/>
                    </td>
                </tr>
                <tr>
                    <td class="etiqueta"><%=descriptor.getDescripcion("etiqDocBuzonEntrada")%></td>
                    <td width="22%">
                        <html:text styleClass="inputTexto" property="documentoBuzonEntrada" size="30" maxlength="25"/>
                    </td>
                    <td width="13%" class="etiqueta"><%=descriptor.getDescripcion("etiqNombreBuzonEntrada")%></td>
                    <td colspan="2">
                        <html:text styleClass="inputTexto" property="nombreBuzonEntrada" size="30" maxlength="80"/>
                    </td>

                </tr>
                <tr>

                    <td  class="etiqueta"><%=descriptor.getDescripcion("etiqApe1BuzonEntrada")%></td>
                    <td  class="etiqueta">
                        <html:text styleClass="inputTexto" property="primerApellidoBuzonEntrada" size="30" maxlength="100"/>
                    </td>
                    <td width="13%" class="etiqueta"><%=descriptor.getDescripcion("etiqApe2BuzonEntrada")%></td>
                    <td class="etiqueta">
                        <html:text styleClass="inputTexto" property="segundoApellidoBuzonEntrada" size="30" maxlength="100"/>
                    </td>
                </tr>
                <tr>
                    <td width="13%" class="etiqueta">
                        <%=descriptor.getDescripcion("etiqUniDestBuzonEntrada")%>
                    </td>
                    <td colspan="5">
                        <html:text styleClass="inputTexto" property="codUnidadDestinoBuzonEntrada" size="9" maxlength="10" onkeyup="return xAMayusculas(this);"/>
                        <html:text styleClass="inputTexto" property="descUnidadDestinoBuzonEntrada"  style="width:445px;height:17" readonly="true"/>
                        <A href="" style="text-decoration:none;" id="anchorUnidadDestinoBuzonEntrada" name="anchorUnidad">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonUnidad" name="botonUnidad" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                        </A>
                    </td>                                                            
                </tr>
                <tr>
                    <td width="13%" class="etiqueta">
                      <%=descriptor.getDescripcion("etiqUnidOrganica")%>:
                    </td>
                    <td colspan="5">
                        <html:text styleClass="inputTexto" property="codUnidadOrganicaAnotacion" size="9" maxlength="10" onkeyup="return xAMayusculas(this);"/>
                        <html:text styleClass="inputTexto" property="descUnidadOrganicaAnotacion"  style="width:445px;height:17" readonly="true"/>
                        <A href="" style="text-decoration:none;" id="anchorUnidadOrganicaAnotacion" name="anchorUnidad">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonUnidad" name="botonUnidad" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                        </A>
                    </td>                                                            
                </tr>
                <tr>
                    <td width="13%" class="etiqueta">
                        <%=descriptor.getDescripcion("etiqCodAsuBuzonEntrada")%>
                    </td> 
                    <td colspan="5">
                        <html:text styleClass="inputTexto" property="codAsuntoBuzonEntrada" size="9" maxlength="10" onkeyup="return xAMayusculas(this);"/>
                        <html:text styleClass="inputTexto" property="descAsuntoBuzonEntrada"  style="width:445px;height:17" readonly="true"/>
                        <A href="" style="text-decoration:none;" id="anchorAsuntoBuzonEntrada" name="anchorAsunto">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonAsunto" name="botonAsunto" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                        </A>
                    </td>
                </tr>
                
                 <%if (registroCong.getString("INTEGRACION_TECNICO_REFERENCIA").equals("SI")) {%>
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
                <%}%>
                
                </table>                                                
                <!-- NUEVOS CRITERIOS DE BÚSQUEDCA -->
                </c:if>

                <table width="100%" border="0">
                    <tr>
                        <td style="width:13%" class="etiqueta"><%=descriptor.getDescripcion("etiqFechDesde")%>:</td>
                        <td style="width:20%" class="columnP">
                            <html:text styleClass="inputTxtFechaObligatorio" size="12" maxlength="10" property="fechaDesde"  styleId="fechaDesde"
                                       onkeyup = "javascript:if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus = "this.select();"
                                       />
                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaInicio(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;" >
                                <span class="fa fa-calendar" aria-hidden="true" style="border:0px none" id="calFechaInicio" name="calFechaInicio" alt='<%=descriptor.getDescripcion("gEtiqFecha")%>' ></span>
                            </A>
                        </td>
                        <td style="width:10%" class="etiqueta"><%=descriptor.getDescripcion("etiqFechHasta")%>:</td>
                        <td style="width:57%" class="columnP">
                            <html:text styleClass="inputTxtFechaObligatorio" size="12" maxlength="10" property="fechaHasta" styleId="fechaHasta"
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
                                <span class="fa fa-chevron-circle-down enTxttitblanco" aria-hidden="true" id="botonRango" name="botonRango" style="cursor:hand;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;" ></span>
                            </a>
                        </td>
                        <td style="width:13%" class="etiqueta"><%=descriptor.getDescripcion("etiqRegPendCatalogacion")%></td>
                        <td>
                            <html:hidden property="codRegPendCatalogacion" styleId="codRegPendCatalogacion" maxlength="1"/>
                            <html:text styleClass="inputTexto" property="descRegPendCatalogacion" styleId="descRegPendCatalogacion"  size="12" readonly="true"/>
                            <a id="anchorRegPendCatalogacion" name="anchorRegPendCatalogacion" href="">
                                <span class="fa fa-chevron-circle-down enTxttitblanco" aria-hidden="true" id="botonRango" name="botonRango" style="cursor:hand;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;" ></span>
                            </a>
                        </td>
                    </tr>
                    <tr>
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
        <tr>
            <td>
                <div class="dataTables_wrapper paxinacionDataTables">
                    <label id="contSelectPax"></label>
                </div>
            </td>
        </tr>            
        <tr>
            <td style="width: 100%" align="center">
                <table id="tablaDatos" class="xTabla compact" width="99%"></table>
            </td>
        </tr>
        <tr>
            <td>
                <div id="enlace" class="dataTables_wrapper"></div>
            </td>
        </tr>                                        
    </table>
    </div>
    <div class="capaFooter">
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" accesskey="H" value='<%=descriptor.getDescripcion("gbHistorico")%>' name="cmdHistorico" onclick="pulsarHistorico();">
 		<input type="button" class="botonGeneral" value="Exportar CSV" name="cmdExportarCSV" target="_blanck" onclick="pulsarPrevioImprimir();">
    </div>    
</div>    
<iframe id="iframe_buzon_csv" style="display:none;"></iframe>
</html:form>
 <script type="text/javascript">

  //Usado para el calendario
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

        if (teclaAuxiliar == 1){
            if(IsCalendarVisible) replegarCalendario(coordx,coordy);
            if (comboUnidad!=null && comboUnidad!=undefined && comboUnidad.base.style.visibility == "visible" && isClickOutCombo(comboUnidad,coordx,coordy)) setTimeout('comboUnidad.ocultar()',20);
            if (comboAsunto!=null && comboAsunto!=undefined && comboAsunto.base.style.visibility == "visible" && isClickOutCombo(comboAsunto,coordx,coordy)) setTimeout('comboAsunto.ocultar()',20);

        }
        if (teclaAuxiliar == 9){
            if(IsCalendarVisible) hideCalendar();
            if (comboUnidad!=null && comboUnidad!=undefined && comboUnidad.base.style.visibility == "visible" && isClickOutCombo(comboUnidad,coordx,coordy)) setTimeout('comboUnidad.ocultar()',20);
            if (comboAsunto!=null && comboAsunto!=undefined && comboAsunto.base.style.visibility == "visible" && isClickOutCombo(comboAsunto,coordx,coordy)) setTimeout('comboAsunto.ocultar()',20);
        }
}
    
    /*******************************************************************/
    /****************** DEFINICIÓN DE LOS COMBOS ***********************/
    /*******************************************************************/

    /*** COMBO PARA LA UNIDAD DE DESTINO DEL BUZÓN DE ENTRADA ***/
    var comboUnidad = null;
    var comboAsunto = null;
    var comboUor=null;
    
    <%if (registroCong.getString("INTEGRACION_TECNICO_REFERENCIA").equals("SI")) { %>
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
    

    <c:if test="${sessionScope.TramitacionForm.cargarNuevosCriteriosBuzonEntrada eq true}">
    comboUnidad = new Combo("UnidadDestinoBuzonEntrada");
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
    
    comboUor = new Combo("UnidadOrganicaAnotacion");
    comboUor.addItems(codigosUorVisible,descripcionesUnidad);


    /*** COMBO PARA LA UNIDAD DE DESTINO DEL BUZÓN DE ENTRADA ***/
    comboAsunto = new Combo("AsuntoBuzonEntrada");
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
	'width=600,height=300,status='+ '<%=statusBar%>',function(){});
    </c:if>

    /*CREAMOS EL COMBO registro telematico*/
    var comboRegistroTelematico = new Combo("RegistroTelematico");
    cargarComboRegistroTelematico();
    
    /*CREAMOS EL COMBO registro pendiente de catalogacion*/
    var comboRegPendCatalogacion = new Combo("RegPendCatalogacion");
    cargarComboRegPendCatalogacion();
 </script>
<script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
</BODY>
</html:html>
