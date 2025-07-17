<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.business.sge.ConsultaExpedientesValueObject" %>
<%@ page import="es.altia.agora.interfaces.user.web.sge.ConsultaExpedientesForm" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.lang.Integer" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>

<html:html>
<head>
<jsp:include page="/jsp/sge/tpls/app-constants.jsp"/>
<TITLE>::: EXPEDIENTES - LISTADO RELACION EXPEDIENTES :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<!-- Estilos -->


<%

    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma = 0;
    int apl = 0;
    String funcion = "";

    if ((session.getAttribute("usuario") != null)) {
        usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
        idioma = usuarioVO.getIdioma();
        apl = usuarioVO.getAppCod();
    }
    String idSesion = session.getId();
    String porCampoSup = request.getParameter("porCampoSup");
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String mostrarLocalizacion = m_Config.getString("ListaExpedientes.localizacion");

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor" property="idi_cod" value="<%= idioma%>"/>
<jsp:setProperty name="descriptor" property="apl_cod" value="<%= apl %>"/>

<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>" media="screen" >
<SCRIPT type="text/javascript"> 
var dataTable = null;
var cols = new Array();
var lista = new Array();
var listaOriginal = new Array();
var listaP = new Array();
var listaSel = new Array();
var fila;
var asunto;
var apellido1;
var estado;
var columnaOrden=0;
var listaCampos=new Array();
//vector que me indica que campos estan activos en las columnas para saber por cual ordenar.
var listaOrden=new Array();
var rowIdSelec=0;
var modoConsultaExpRel;
var desdeFichaExpediente;

<% /* Recuperar el vector de procedimientos de la sesion. */
   ConsultaExpedientesForm f= (ConsultaExpedientesForm)  session.getAttribute("ConsultaExpedientesForm");
   //Vector relacionExpedientes = f.getResultadoConsulta();
   //int numRelacionExpedientes = 0;
   //if ( relacionExpedientes != null ) numRelacionExpedientes = relacionExpedientes.size();
int numRelacionExpedientes = f.getNumRelacionExpedientes();
   boolean encontrada=false;

   String desdeInformesGestion = (String) session.getAttribute("desdeInformesGestion");
   session.removeValue("desdeInformesGestion");

   String usuarioDirectivaLimitacion = (String) session.getAttribute("limitadaDirectivaInformesGestión");
   session.removeValue("limitadaDirectivaInformesGestión");

   if(desdeInformesGestion != null && desdeInformesGestion.equals("si")) {
     desdeInformesGestion = "si";
   } else desdeInformesGestion = "no";
   String todos = (String) session.getAttribute("todos");
   if(todos == null) {
     todos = "";
   }
   String desdeConsulta = request.getParameter("desdeConsulta");
   if (desdeConsulta==null || desdeConsulta.equals("null") || desdeConsulta.equals("")){
        desdeConsulta="si";
   }
  String expRelacionado=request.getParameter("expRelacionado");
   String informesDireccion = request.getParameter("informesDireccion");
   if (informesDireccion == null) informesDireccion = "false";

   boolean enInformesGestion = f.getConsultaExpedientes().isDesdeInformesGestion();

    String paginaPendientes = (String)session.getAttribute("pagina_pendientes");
    String lineasPendientes= (String)session.getAttribute("lineas_pendientes");
    String columnaPendientes = (String)session.getAttribute("columna_pendientes");
    String ordenColumnaPendientes = (String)session.getAttribute("tipoOrden_pendientes");
    
    String desdeFichaExpediente = "";
    if (session.getAttribute("desdeFichaExpediente") != null) {
      if ("si".equals((String) session.getAttribute("desdeFichaExpediente"))){
        desdeFichaExpediente="si";
      }
      session.removeAttribute("desdeFichaExpediente");
    }

%>

/* Para navegacion */
var lineasPagina = 10;

var paginaActual = 1;
var enInformesGestion = <%=enInformesGestion%>;
var inicio = 0;
var fin = 0;
var numeroPaginas;
var idOrden = 0;
var tipoOrdenacion = "true";
// Nº de expedientes que cumplen las condiciones de búsqueda
var numRelacionExpedientes = <%=numRelacionExpedientes%>;

function cargarInicio() {
    <c:if test="${not empty sessionScope.errorConsulta}">
        jsp_alerta('A', "<fmt:message key="${sessionScope.errorConsulta}"/>");
        document.forms[0].cmdImprimir.disabled = true;
    </c:if>

      var i=0;
       <logic:iterate id="campos" name="ConsultaExpedientesForm" property="camposListados">
                listaCampos[i] = ['<bean:write name="campos" property="codCampo" />',
                '<bean:write name="campos" property="nomCampo" />',
                 '<bean:write name="campos" property="tamanoCampo"/>',
                '<bean:write name="campos" property="actCampo"/>',
             '<bean:write name="campos" property="ordenCampo"/>'];
      i++;
    </logic:iterate>
    
var cont = 0;
cols=[{title:"<%=descriptor.getDescripcion("etiqNumExpediente")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("bDefProc")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("gEtiq_interesado")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: ""},
            {title:"<%=descriptor.getDescripcion("gEtiq_Asunto")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("gEtiq_Local")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("gEtiq_fecIni")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("gEtiq_fecFin")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: ""},
            {title:"<%=descriptor.getDescripcion("etiq_estado")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("etiqUsuInicio")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("etiqUnidadInicio")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"},
            {title:"<%=descriptor.getDescripcion("etiq_Inicio")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%', sClass: "centrado"}];

      for(cont=0;cont<listaCampos.length;cont++){

         if(listaCampos[cont][3]=='NO'){
                 cols[cont].sClass = "estiloOculto";
         }else if(listaCampos[cont][3]=='SI'){
             listaOrden[cont]=listaCampos[cont][4];
             if(listaCampos[cont][4]==idOrden)
                    columnaOrden=cont;
         }
     }

    document.forms[0].desdeConsulta.value = "<%=desdeConsulta%>";
    document.forms[0].porCampoSup.value = "<%=porCampoSup%>";
    window.focus();
    listaSel = lista;

    document.forms[0].expRelacionado.value = "<bean:write name="ConsultaExpedientesForm" property="expRelacionado"/>";
    if (document.forms[0].expRelacionado.value == "si") {
        document.forms[0].codMunExpIni.value = "<bean:write name="ConsultaExpedientesForm" property="codMunicipioIni"/>";
        document.forms[0].ejercicioExpIni.value = "<bean:write name="ConsultaExpedientesForm" property="ejercicioIni"/>";
        document.forms[0].numeroExpIni.value = "<bean:write name="ConsultaExpedientesForm" property="numeroExpedienteIni"/>";
    }
    document.forms[0].deAdjuntar.value = "<bean:write name="ConsultaExpedientesForm" property="deAdjuntar"/>";
    //Los necesito para hacer la recarga con criterios de busqueda
     document.forms[0].codProcedimiento.value ="<bean:write name="ConsultaExpedientesForm" property="codigoProcedimiento"/>";
     document.forms[0].tipoBusqueda.value ="<bean:write name="ConsultaExpedientesForm" property="tipoBusqueda"/>";
     document.forms[0].numeroExpediente.value ="<bean:write name="ConsultaExpedientesForm" property="numeroExpediente"/>";
     modoConsultaExpRel="<bean:write name="ConsultaExpedientesForm" property="modoConsultaExpRel"/>";

    var paginaPendientes ='<%= paginaPendientes%>';
    desdeFichaExpediente ='<%= desdeFichaExpediente%>';
    
    cargarComboFilasPagina();
    
    if(paginaPendientes!='null' && paginaPendientes!='undefined' && 'si'==desdeFichaExpediente) 
        cargaPagina(paginaPendientes);
    else 
        cargaPagina(1);
}

function enlaces() {
    numeroPaginas = Math.ceil(numRelacionExpedientes/lineasPagina);
    return enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>',paginaActual,numeroPaginas,'cargaPagina');
}


function cargaPagina(numeroPagina) {
    lista = new Array();
    listaOriginal = new Array();
    pleaseWait('on');
    document.forms[0].paginaListado.value = numeroPagina;
    document.forms[0].numLineasPaginaListado.value = lineasPagina;
    document.forms[0].columna.value = idOrden;
    document.forms[0].tipoOrden.value =tipoOrdenacion;
    document.forms[0].desdeFichaExpediente.value = '<%= desdeFichaExpediente%>'; 
    document.forms[0].opcion.value = "cargar_pagina";
    document.forms[0].target = "oculto";
    document.forms[0].action = "<c:url value='/sge/ConsultaExpedientes.do'/>";
    document.forms[0].submit();
}


// Cambia pq ahora es todo el vector.
function inicializaLista(numeroPagina,numTotExpedientes) {
    if(numTotExpedientes!=null && numTotExpedientes>0 && enInformesGestion)
        numRelacionExpedientes = Number(numTotExpedientes);
    
    paginaActual = numeroPagina;
    listaP = new Array();
    inicio = 0;
    fin = lineasPagina;
    listaP = listaSel;
    if (dataTable!=null)
        dataTable.destroy();

    dataTable = $("#tablaExpedientes").DataTable( {
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
        $('#tablaExpedientes tbody tr:nth-child('+(i+1)+')').attr('id', i);
    }

    $("#tablaExpedientes thead tr th").each(function (index,value) {
        if (listaOrden[index]==-1){
          value.onclick = function() {jsp_alerta('A','<%=descriptor.getDescripcion("msjNoOrdenacion")%>');} 
      } else {
          $("#tablaExpedientes thead tr th:nth-child("+(index+1)+")").removeClass("sorting sorting_asc sorting_desc ");
          if (columnaOrden!=index) 
              $("#tablaExpedientes thead tr th:nth-child("+(index+1)+")").addClass("sorting");
          else if (tipoOrdenacion=="true")
              $("#tablaExpedientes thead tr th:nth-child("+(index+1)+")").addClass("sorting_asc");
          else 
              $("#tablaExpedientes thead tr th:nth-child("+(index+1)+")").addClass("sorting_desc");

          value.onclick = function() { 
            $("#tablaExpedientes thead tr th").off("click");
            if (columnaOrden==index){
                tipoOrdenacion = (tipoOrdenacion=="true"?"false":"true");
            }
            idOrden = listaOrden[index];
            columnaOrden=index;
            document.forms[0].paginaListado.value = 1;
            document.forms[0].numLineasPaginaListado.value = lineasPagina;
            document.forms[0].columna.value = listaOrden[index];//pasamos el id del campo a ordenar                       
            document.forms[0].tipoOrden.value =tipoOrdenacion; //si es -1 ordena ascendente si es 0 ordena descendente   
            desdeFichaExpediente='no';
            document.forms[0].desdeFichaExpediente.value = '<%= desdeFichaExpediente%>';           
            document.forms[0].opcion.value = "cargar_pagina";
            document.forms[0].target = "oculto";
            document.forms[0].action = "<c:url value='/sge/ConsultaExpedientes.do'/>";
            document.forms[0].submit();
          }
      }
    }); 
    
    $("#tablaExpedientes tbody tr").on("click", function(){
        $("#tablaExpedientes tbody tr").removeClass("activa").addClass("inactiva");	
        $("#tablaExpedientes tbody tr[id=" + this.id + "]").removeClass("inactiva").addClass("activa");	
        rowFunctionClick(this.id);	
       	
    });
        
    $("#tablaExpedientes tbody tr").on("dblclick", function() {
         seleccionRegistro(this.id);
    });
    domlay('enlace', 1, 0, 0, enlaces());
    pleaseWait("off");
}

function ocultaBotonesParaHistorico(hayExpedientesHistorico)
{
    
    if(hayExpedientesHistorico=="true")
        {
            document.forms[0].cmdImprimir.disabled = true;
            document.forms[0].cmdExportarCsv.disabled = true;
        }
}


function seleccionRegistro(indice) {
<% if (informesDireccion.equals("true") || desdeInformesGestion.equals("si")) { %>
    // En informes de direccion o de gestion abrimos la ficha de expediente en un popup
    var source="<%= request.getContextPath() %>/sge/FichaExpediente.do?opcion=cargar" +
            "&expHistorico=" + listaOriginal[indice][8] +
            "&modoConsulta=si&desdeAltaRE=si" +
            "&codMunicipio=" + listaOriginal[indice][0] + "&ejercicio=" + listaOriginal[indice][2] +
            "&numero=" + listaOriginal[indice][3] + "&codProcedimiento=" + listaOriginal[indice][1];

    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana',
	'width=992,height=650,left=150,top=75,toolbar=no,scrollbars=yes,status='+ '<%=statusBar%>',function(){});
<% } else { %>
    if (document.forms[0].numeroExpIni.value == listaOriginal[indice][3])
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoRelReflex")%>");
    else if ((document.forms[0].expRelacionado.value == "si") && (document.forms[0].insertarExpRel.value == "si")) {
        document.forms[0].codMunicipio.value = listaOriginal[indice][0];
        document.forms[0].ejercicio.value = listaOriginal[indice][2];
        document.forms[0].numero.value = listaOriginal[indice][3];
        document.forms[0].modoConsulta.value = "no";
        document.forms[0].opcion.value = "insertarExpedienteRelacionado";
        document.forms[0].target = "oculto";
        document.forms[0].action = "<c:url value='/sge/ConsultaExpedientes.do'/>";
        document.forms[0].submit();
    } else {
        if (listaOriginal[indice][7]!='') 
            mostrarAvisoRelacion(listaOriginal[indice][7],indice);
        else{
            pleaseWait('on');
            document.forms[0].codMunicipio.value = listaOriginal[indice][0];
            document.forms[0].codProcedimiento.value = listaOriginal[indice][1];
            document.forms[0].ejercicio.value = listaOriginal[indice][2];
            document.forms[0].numero.value = listaOriginal[indice][3];
            document.forms[0].modoConsulta.value = "si";
            document.forms[0].opcion.value = "cargar";
            document.forms[0].target = "mainFrame";
            document.forms[0].action = "<c:url value='/sge/FichaExpediente.do'/>"+ "?expHistorico=" + listaOriginal[indice][8];        
            document.forms[0].submit();
        }
    }
<% } %>
}

function mostrarAvisoRelacion(relacion,indice) {
  	var opcionExp = jsp_alerta('B','<br> <%=descriptor.getDescripcion("msjExpRelAbierta")%> <br> <%=descriptor.getDescripcion("etiq_numRel")%> : '+ relacion
  				+ '<br> <br><%=descriptor.getDescripcion("msjAbrirExpModConsul")%> <br><br>' ); 
  	if (opcionExp == 1){
        
        document.forms[0].codMunicipio.value = listaOriginal[indice][0];
        document.forms[0].codProcedimiento.value = listaOriginal[indice][1];
        document.forms[0].ejercicio.value = listaOriginal[indice][2];
        document.forms[0].numero.value = listaOriginal[indice][3];
        document.forms[0].modoConsulta.value = "si";
        document.forms[0].opcionExpRel.value = "1";
        document.forms[0].opcion.value = "cargar";
        document.forms[0].target = "mainFrame";
        document.forms[0].action = "<c:url value='/sge/FichaExpediente.do'/>";
        document.forms[0].submit();
  	}
}


function grabarExpRel(respOpcion) {
    if (respOpcion == "grabarExpRel") {
        document.forms[0].opcion.value = "expRel";
        document.forms[0].target = "mainFrame";
        document.forms[0].action = "<c:url value='/sge/ConsultaExpedientes.do'/>";
        document.forms[0].submit();
    } else {
        jsp_alerta("A", "<%=descriptor.getDescripcion("altaNoHecha")%>");
    }
}

function pulsarSalirConsultar() {

<% if(desdeInformesGestion.equals("si")) { %>

    document.forms[0].todos.value = '<%= todos %>';
    document.forms[0].opcion.value = "volverCargar";
    document.forms[0].target = "mainFrame";
    document.forms[0].action = "<c:url value='/informes/Informes.do'/>";
    document.forms[0].submit();

<% } else { %>
    if ((document.forms[0].expRelacionado.value == "si")) {
        document.forms[0].codMunicipio.value = document.forms[0].codMunExpIni.value;
        document.forms[0].ejercicio.value = document.forms[0].ejercicioExpIni.value;
        document.forms[0].numeroExpediente.value = document.forms[0].numeroExpIni.value;
    }
    document.forms[0].opcion.value = "inicio";
    document.forms[0].target = "mainFrame";
    if (document.forms[0].porCampoSup.value == "si") {
        document.forms[0].action = "<c:url value='/sge/ConsultaExpedientePorCampoSup.do'/>";
    } else {
        document.forms[0].action = "<c:url value='/sge/ConsultaExpedientes.do'/>";
    }
    document.forms[0].submit();
<% } %>
}

/* IMPRIMIR consulta */

function pulsarExportarCSV(){
    pleaseWait('on');
    document.forms[0].opcion.value = "exportarCSV";
    document.forms[0].target = "oculto";
    document.forms[0].action = "<c:url value='/sge/ConsultaExpedientes.do'/>";
    document.forms[0].submit();
}

function pulsarImprimir() {
    pleaseWait('on');
    document.forms[0].opcion.value = "imprimir";
    document.forms[0].target = "oculto";
    document.forms[0].action = "<c:url value='/sge/ConsultaExpedientes.do'/>";
    document.forms[0].submit();
}

function pulsarAnadir() {
    var i = rowIdSelec;
    if (i != -1) {
        document.forms[0].expRelacionado.value = "si";
        document.forms[0].insertarExpRel.value = "si";
        fila = parseInt(i);
        seleccionRegistro(fila);
    } else {
        jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
    }
}

function abrirInforme(nombre,dir) {
    pleaseWait('off');
    var source = "<c:url value='/jsp/verPdf.jsp?opcion=null&nombre='/>" + nombre+"&dir="+dir;
    var nombreVentana = (top.name == 'ventana' ? 'ventana2' : 'ventana');
    ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp;jsessionid=<%=idSesion%>?source=" + source, nombreVentana, 'width=800px,height=550px,status=' + '<%=statusBar%>' + ',toolbar=no');
    ventanaInforme.focus();
}


function abrirInformeCSV(nombre,dir) {
    pleaseWait('off');
    var source = "<c:url value='/jsp/verPdf.jsp?opcion=null&tipoFichero=csv&nombre='/>" + nombre+"&dir="+dir;
    var nombreVentana = (top.name == 'ventana' ? 'ventana2' : 'ventana');
    ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp;jsessionid=<%=idSesion%>?source=" + source, nombreVentana, 'width=800px,height=550px,status=' + '<%=statusBar%>' + ',toolbar=no');
    ventanaInforme.focus();
}

/* Fin IMPRIMIR consulta */


function checkKeysLocal(tecla, evento) {
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
    if ('Alt+C' == tecla) pulsarSalirConsultar();

    if (tecla == 38 || tecla == 40) {
        upDownTable(tab, lista);
    }
    if (tecla == 13) {
        if ((tab.selectedIndex > -1) && (!tab.ultimoTable)) {
            callFromTableTo(tab.selectedIndex, tab.id);
        }
    }
    keyDel(aux);
}

function rowFunction(rowId,gridJstContainer){
    rowIdSelec = rowId;
    seleccionRegistro(rowId,gridJstContainer.getRowCount());
}
function rowFunctionClick(rowID,gridJstContainer){
    if(modoConsultaExpRel!="si") 
        return;
    else 
        rowIdSelec=rowID;
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
</SCRIPT>
</head>
<BODY class="bandaBody" onload="javascript:{cargarInicio();}">
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>
<html:form action="/sge/ConsultaExpedientes.do" target="_self">
<html:hidden property="opcion" value=""/>
<html:hidden property="paginaListado" value="1"/>
<html:hidden property="numLineasPaginaListado" value="10"/>
<html:hidden property="numeroExpedienteBus" value=""/>
<input type="hidden" name="codMunicipio">
<input type="hidden" name="codProcedimiento">
<input type="hidden" name="numeroExpediente">
<input type="hidden" name="ejercicio">
<input type="hidden" name="numero">
<input type="hidden" name="codMunExpIni" value="">
<input type="hidden" name="ejercicioExpIni" value="">
<input type="hidden" name="numeroExpIni" value="">
<input type="hidden" name="modoConsulta">
<input type="hidden" name="expRelacionado" value="">
<input type="hidden" name="deAdjuntar" value="">
<input type="hidden" name="desdeInformesGestion" value="no">
<input type="hidden" name="todos" value="">
<input type="hidden" name="idioma" value="es">
<input type="hidden" name="desdeConsulta">
<input type="hidden" name="insertarExpRel" value="no">
<input type="hidden" name="porCampoSup">
<input type="hidden" name="opcionExpRel">
<input type="hidden" name="tipoBusqueda">

<html:hidden property="columna" value=""/>
<html:hidden property="tipoOrden" value=""/>
<html:hidden  property="desdeFichaExpediente" value=""/>

<div class="txttitblanco"><%=descriptor.getDescripcion("inf_Expedientes")%></div>
<div class="contenidoPantalla">
    <table style="width:100%">
        <tr>
            <td>
                <div class="dataTables_wrapper paxinacionDataTables">
                    <label id="contSelectPax"></label>
                </div>
            </td>
        </tr>            
        <tr>
            <td>
               <table id="tablaExpedientes" class="xTabla compact tablaDatos" width="99%"></table>
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
    <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
         <% if (!"si".equals(expRelacionado)){%>
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbExportarCsv")%>" name="cmdExportarCsv" onClick="pulsarExportarCSV();return false;">
       <% }if ("si".equals(expRelacionado)) {%>
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAnadir")%>" name="cmdAnadir" onClick="pulsarAnadir();return false;">
        <% } else {%>
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbImprimir")%>" name="cmdImprimir" onClick="pulsarImprimir();return false;">
        <% }%>
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdSalir" onClick="pulsarSalirConsultar();return false;">
    </DIV>
    <% if ("si".equals(desdeInformesGestion) && ("si".equals(usuarioDirectivaLimitacion))) {%>
        <DIV id="capaAlertas" STYLE="width:100%;height:0px; float:left; text-align: left; padding-top: 50px; padding-left: 5px; font-size: 10px; font-style: italic">
            <%=descriptor.getDescripcion("etiq_limitacionConsulta")%>
        </DIV>
    <%}%>
</DIV>
</html:form>
</BODY>

</html:html>
