<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=UTF-8" language="java" import="java.util.Map" pageEncoding="UTF-8" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.sge.TramitacionValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionForm" %>
<%@page import="java.util.Vector"%>
<%@page import="java.lang.Integer"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.business.sge.DefinicionProcedimientosValueObject" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.ValoresCriterioBusquedaExpPendientesVO" %>
<%@page import="java.util.ArrayList" %>
<%
  int idioma=1;
  int apl=1;
  int codOrg=0;
  UsuarioValueObject usuario = null;
  if (session!=null){
    usuario = (UsuarioValueObject)session.getAttribute("usuario");
    if (usuario!=null) {
      idioma = usuario.getIdioma();
      apl = usuario.getAppCod();
      codOrg = usuario.getOrgCod();
    }
  }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    Config m_Expedientes = ConfigServiceHelper.getConfig("Expediente");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String mostrarLocalizacion = m_Config.getString("ListaExpedientes.localizacion");
    String codProcedimientoFiltro = (String)session.getAttribute("codigo_procedimiento_exp_pendientes");
    String codRangoTemporalFiltro = (String)session.getAttribute("codigo_rango_temp_exp_pendientes");
    if (codRangoTemporalFiltro == null || "".equals(codRangoTemporalFiltro)) 
        codRangoTemporalFiltro = m_Expedientes.getString(codOrg+"/rango_resultados_expedientes_pendientes");
    if (codRangoTemporalFiltro == null || "".equals(codRangoTemporalFiltro)) 
        codRangoTemporalFiltro = "0";
    
    String paginaPendientes = (String)session.getAttribute("pagina_pendientes");
    String lineasPendientes= (String)session.getAttribute("lineas_pendientes");
    String columnaPendientes = (String)session.getAttribute("columna_pendientes");
    String ordenColumnaPendientes = (String)session.getAttribute("tipoOrden_pendientes");

    /************* Se comprueba cual es el navegador utilizado por el usuario ******************/
    String userAgent = request.getHeader("user-agent");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<html:html>
 <head>
<jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: EXPEDIENTES  Tramitacion de Expedientes:::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script> 
<script type="text/javascript" src="<c:url value='/scripts/json2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>

<SCRIPT type="text/javascript"> 
 var dataTable = null;
 var codProcedimientoFiltro = "<%=codProcedimientoFiltro%>";
 var codRangoTemporalFiltro = "<%=codRangoTemporalFiltro%>";
<%  

String desdeFichaExpediente = "";
    if (session.getAttribute("desdeFichaExpediente") != null) {
      if ("si".equals((String) session.getAttribute("desdeFichaExpediente"))){
        desdeFichaExpediente="si";
      }
      session.removeAttribute("desdeFichaExpediente");
    }

   TramitacionForm f= (TramitacionForm)  session.getAttribute("TramitacionForm");
   TramitacionValueObject tVO = f.getTramitacion();
    String leyenda=f.getLeyenda();
    if (leyenda==null) leyenda="expedientesPendientes";


   // PARA EXPEDIENTES
   int numRelacionExpedientes = 0;

    /** prueba **/
   if (request.getAttribute("numero_expedientes_pendientes")!= null ){
       int oNumRelacionExpedientesAuxiliar = (Integer)request.getAttribute("numero_expedientes_pendientes");      
       numRelacionExpedientes = oNumRelacionExpedientesAuxiliar;
   }
    /** prueba **/

   String codMunicipio = tVO.getCodMunicipio();
   String codProcedimiento = tVO.getCodProcedimiento();
   String ejercicio = tVO.getEjercicio();
   String numero = tVO.getNumero();

    
   int posE=0;
   String lines="10";

   boolean tieneProcedimientoVistaExpedientesPropia = f.getTieneProcedimientoVistaExpedientesPropia();
   
    if (tVO.getNumLineasPaginaListado()!=null && !"".equals(tVO.getNumLineasPaginaListado()))
          lines=tVO.getNumLineasPaginaListado();
      else
          lines="10";
   
  
    /** prueba **/
%>
var listaE = new Array();
var listaOriginalE = new Array();
// Lista con los datos de previsualizacion
var listaPreVis = new Array();
var filaE;
var ultimoE = false;
var paginE;

/* Para navegacion Expedientes*/
var enlacesPaginaE  = 10;
var lineasPaginaE   = <%=lines%>;
var paginaActualE   = 1;
var numeroPaginasE;

numeroPaginasE=Math.ceil(<%=numRelacionExpedientes%>/lineasPaginaE);

if (numeroPaginasE < enlacesPaginaE) paginaSuperiorE= numeroPaginasE;
var filtro='<%=leyenda%>';

// Fin navegacion expedientes

//tabla parametrizable
var cols =new Array(); 
var listaCampos=new Array();
//vector que me indica que campos estan activos en las columnas para saber por cual ordenar.
var listaOrden=new Array();
var listaOrdenColumna=new Array();
var columnaOrden=0;
var tipoOrdenacion="true";
var desdeFichaExpediente='';

var tieneProcedimientoVistaExpedientesPropia = <%=tieneProcedimientoVistaExpedientesPropia%>;


/****** prueba ******/
var listaCodigosBusqueda         = new Array();
var listaDescripcionesBusqueda   = new Array();
var listaCamposCriteriosBusqueda = new Array();
/****** prueba ******/


// fin tabla parametrizable
function inicializar() {  
  
  pleaseWait('on');
  window.focus();
    var i=0;
      var z=0;
      <logic:present name="TramitacionForm" property="camposListados">
       <logic:iterate id="campos" name="TramitacionForm" property="camposListados">
            if(!tieneProcedimientoVistaExpedientesPropia){
             // Se añade un nuevo campo con la etiqueta del idioma en la lista de campos que forman las columnas del listado
                listaCampos[i] = ['<bean:write name="campos" property="codCampo" />',
                                  '<bean:write name="campos" property="nomCampo" />',
                                  '<bean:write name="campos" property="tamanoCampo"/>',
                                  '<bean:write name="campos" property="actCampo"/>',
                                  '<bean:write name="campos" property="ordenCampo"/>',
                                  '<bean:write name="campos" property="campoSuplementario"/>',
                                  '<bean:write name="campos" property="columnaCampo"/>'];                                  
                i++;
            }
    </logic:iterate>
      </logic:present>


      <logic:present name="TramitacionForm" property="camposListadoProcedimiento">
           <logic:iterate id="campos" name="TramitacionForm" property="camposListadoProcedimiento">           
             if(tieneProcedimientoVistaExpedientesPropia){                 
                // Se añade un nuevo campo con la etiqueta del idioma en la lista de campos que forman las columnas del listado
                listaCampos[i] = ['<bean:write name="campos" property="codCampo" />',
                                  '<bean:write name="campos" property="nomCampo" />',
                                  '<bean:write name="campos" property="tamanoCampo"/>',
                                  '<bean:write name="campos" property="actCampo"/>',
                                  '<bean:write name="campos" property="ordenCampo"/>',
                                  '<bean:write name="campos" property="etiquetaIdioma"/>',
                                  '<bean:write name="campos" property="campoSuplementario"/>'
                                 ];
                i++;
             }

           </logic:iterate>           
      </logic:present>

var cont = 0;
var tieneVistaPorProcedimiento=false;
if(codProcedimientoFiltro!=null && codProcedimientoFiltro.length>0 && codProcedimientoFiltro!="null" && 
        codProcedimientoFiltro!="" && tieneProcedimientoVistaExpedientesPropia) {     
    tieneVistaPorProcedimiento=true;
    /*** AÑADIDO PARA GENERAR LAS COLUMNAS EN EL CASO DEL FILTRADO POR PROCEDIMIENTO Y SIEMPRE Y CUANDO HAYA VISTA DE EXP. PENDIENTES PARA DICHO PROCEDIMIENTO ** */
    for(z=0;z<listaCampos.length;z++){
       var columna ={title:listaCampos[z][5], sWidth:parseInt(listaCampos[z][2])+ '%' , sClass: "centrado"};
       cols[z] = columna;
    }        
} else {     
    // Definicion de las columnas para la vista general de expedientes pendientes. VISTA GENERAL DE EXPEDIENTES PENDIENTES
    cols=[{ title:"  ", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},
        { title:"<%=descriptor.getDescripcion("etiqNumExpediente")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},
        { title:"<%=descriptor.getDescripcion("bDefProc")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},
        { title:"<%=descriptor.getDescripcion("gEtiq_interesado")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},
        { title:"<%=descriptor.getDescripcion("gEtiq_Asunto")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},         
        { title:"<%=descriptor.getDescripcion("gEtiq_Local")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},
        { title:"<%=descriptor.getDescripcion("gEtiq_fecIni")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},
        { title:"<%=descriptor.getDescripcion("gEtiq_fecFin")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},
        { title:"<%=descriptor.getDescripcion("etiq_estado")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},
        { title:"<%=descriptor.getDescripcion("etiqUsuInicio")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},
        { title:"<%=descriptor.getDescripcion("etiqUnidadInicio")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},
        { title:"<%=descriptor.getDescripcion("etiqUnidadTramitadora")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},                                    
        { title:" ", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},
        { title:"<%=descriptor.getDescripcion("etiq_Inicio")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"},
        { title:"<%=descriptor.getDescripcion("etiqAlarma")%>", sWidth:parseInt(listaCampos[cont++][2])+ '%',sClass: "centrado"}];
}

for(cont=0;cont<listaCampos.length;cont++){            
   if(listaCampos[cont][3]=='NO'){         
        cols[cont].sClass = "estiloOculto";
   }else if(listaCampos[cont][3]=='SI'){
        //guarda las columnas que estan activas ordenadas segun la posicion
        listaOrden[cont]=listaCampos[cont][4];
        if(codProcedimientoFiltro==null || codProcedimientoFiltro=="null" || codProcedimientoFiltro.length==0) 
           listaOrdenColumna[cont]=listaCampos[cont][6];
        else  
            listaOrdenColumna[cont]='';
    }
}// for
      
if(tieneVistaPorProcedimiento==false){ 
    <% if (!mostrarLocalizacion.equals("si")) { %>
    cols[5].sClass="estiloOculto"; //No mostramos la columna localizacion
    <%}%>  
 }  
   
  var paginaPendientes ='<%= paginaPendientes%>';
  desdeFichaExpediente ='<%= desdeFichaExpediente%>';
 
  numAnotacionE = <%= posE%>;
  if (numAnotacionE != 0){      
    pagE = buscarAnotacionE(numAnotacionE);
  }
  else{  
        pagE=1;
      
        if((paginaPendientes!='null')&&(paginaPendientes!='undefined')&&('si'==desdeFichaExpediente)){
              cargaPagina(paginaPendientes);
              
        } else
            cargaPagina(1);
    }
    
    document.forms[0].columnaPendientes.value='<%= columnaPendientes%>';
    document.forms[0].ordenColumnaPendientes.value='<%= ordenColumnaPendientes%>';
}

function cargaPagina(numeroPaginaE){ 
  listaE = new Array();
  listaOriginalE= new Array();
  listaPreVis = new Array();
  var codRangoTempFiltro = document.forms[0].codFiltroRangoTemp.value;
  
  document.forms[0].paginaListado.value = numeroPaginaE;
  document.forms[0].numLineasPaginaListado.value = lineasPaginaE;
  document.forms[0].desdeFichaExpediente.value = '<%= desdeFichaExpediente%>';
  if (filtro=='expedientesPendientes'){
  document.forms[0].opcion.value="cargar_pagina_exp_pendientes";
  }
  else {
      document.forms[0].opcion.value="cargar_pagina_exp_filtrados";
      
  }
  
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>" + "?codRangoTempFiltro="+codRangoTempFiltro;  
  document.forms[0].submit();
}

function inicializaLista(numeroPaginaE,numLineasPag){  
  paginaActualE = numeroPaginaE;
  lineasPaginaE = numLineasPag;  
  document.forms[0].numLineasPaginaListado.value = numLineasPag;  
  numeroPaginasE = Math.ceil(<%=numRelacionExpedientes%>/lineasPaginaE);
  
  if (dataTable!=null)
    dataTable.destroy();

  dataTable = $("#tablaExpPendientes").DataTable( {
            data: listaE,
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
        
        if(codProcedimientoFiltro!=null && codProcedimientoFiltro.length>0 && codProcedimientoFiltro!="null" && 
        codProcedimientoFiltro!="" && tieneProcedimientoVistaExpedientesPropia) { 
            dataTable.clear();
            dataTable.draw();

            for(i=0;i<listaSelE.length;i++){
                var newRow =  $('#tablaExpPendientes').dataTable().fnAddData( listaSelE[i]);
                var theNode = $('#tablaExpPendientes').dataTable().fnSettings().aoData[newRow[0]].nTr;
                theNode.setAttribute('id', i);
                var fueraDePlazo = listaOriginalE[i][7];
                var plazoCercaFin=listaOriginalE[i][10];
                var pendiente = listaOriginalE[i][8];
                if (fueraDePlazo=="si") theNode.className='fueraDePlazo';
                else if (plazoCercaFin=="si")theNode.className= 'plazoCercaDeFin';
                else if (pendiente=="si") theNode.className='pendienteEstaUnidad';
                else theNode.className='pendienteOtraUnidad';
            }
            dataTable.draw();  
        }

  $("#tablaExpPendientes thead tr th").each(function (index,value) {
      if (listaOrden[index]==0 || listaOrden[index]==20){
        value.onclick = function() {jsp_alerta('A','<%=descriptor.getDescripcion("msjNoOrdenacion")%>');} 
    } else {
        $("#tablaExpPendientes thead tr th:nth-child("+(index+1)+")").removeClass("sorting sorting_asc sorting_desc ");
        if (columnaOrden!=index) 
            $("#tablaExpPendientes thead tr th:nth-child("+(index+1)+")").addClass("sorting");
        else if (tipoOrdenacion=="true")
            $("#tablaExpPendientes thead tr th:nth-child("+(index+1)+")").addClass("sorting_asc");
        else 
            $("#tablaExpPendientes thead tr th:nth-child("+(index+1)+")").addClass("sorting_desc");
        
        value.onclick = function() { 
            $("#tablaExpPendientes thead tr th").off("click");
            if (columnaOrden==index){
                tipoOrdenacion = (tipoOrdenacion=="true"?"false":"true");
            }

            columnaOrden=index;
            nombreColumnaOrden=listaOrdenColumna[index];
            //paso 1 pq si cambia el orden tiene que volver a la pagina 1
            document.forms[0].paginaListado.value = 1;
            document.forms[0].numLineasPaginaListado.value = lineasPaginaE;
            //pasamos el id del campo a ordenar
            document.forms[0].columna.value = listaOrden[index];    
            document.forms[0].nombreColumna.value = nombreColumnaOrden;
            //si es -1 ordena ascendente si es 0 ordena descendente
            document.forms[0].tipoOrden.value = tipoOrdenacion;
            if (filtro=='expedientesPendientes'){
              document.forms[0].opcion.value = "cargar_pagina_exp_pendientes";
            } else {
              document.forms[0].opcion.value="cargar_pagina_exp_filtrados";
            }

            document.forms[0].target = "oculto";
            document.forms[0].action = "<c:url value='/sge/Tramitacion.do'/>" + 
                    "?codRangoTempFiltro="+document.forms[0].codFiltroRangoTemp.value;  
            document.forms[0].submit();
        }
    }
  }); // fin definicion funcion setSort

  $("#tablaExpPendientes tbody tr").on("click", function() {
     $("#tablaExpPendientes tbody tr").removeClass("activa").addClass("inactiva");
     $("#tablaExpPendientes tbody tr[id=" + this.id + "]").removeClass("inactiva").addClass("activa");
     
     // Cargar los datos en los campos de previsualizacion
     // #218055: los datos referentes a tramites abiertos se carga por ajax
     var codMun = listaOriginalE[this.id][0];
     var numExp = listaPreVis[this.id][0];
     $.post('<c:url value='/sge/Tramitacion.do'/>',{'opcion':'recuperarDatosExpediente','codOrganizacion':codMun,'numExpediente':numExp},function(ajaxResult){
        var strTramites = "";
        var strInteresados = "";
        var strAlarmas = "";
        if(ajaxResult){
            var datos = JSON.parse(ajaxResult);
            var tramAbiertos = (datos.tabla).tramites;
            var alarmas = (datos.tabla).camposAlarmas;
            var listaInt = (datos.tabla).interesados;
            if(tramAbiertos.length>0){
                for(var j=0;j<tramAbiertos.length;j++){
                    strTramites += tramAbiertos[j];
                    if(j<tramAbiertos.length-1) strTramites += "<br/>";
                }
            }
            if(listaInt.length>0){
                for(var j=0;j<listaInt.length;j++){
                    strInteresados += listaInt[j];
                    if(j<listaInt.length-1) strInteresados += "<br/>";
                }
            }
            if(alarmas.length>0){
                for(var j=0;j<alarmas.length;j++){
                    strAlarmas += alarmas[j];
                    if(j<alarmas.length-1) strAlarmas += "<br/>";
                }
            }
        }
        document.getElementById('tramPend').innerHTML = strTramites;
        document.getElementById('terceros').innerHTML = strInteresados;
        document.getElementById('alarmasVencidas').innerHTML = alarmas;
     });
    document.getElementById('numExp').firstChild.data = numExp;

    if (listaPreVis[this.id][7] == 'S'){
       document.forms[0].expDestacado.checked = true;
    }else{
        document.forms[0].expDestacado.checked = false;
    }
    document.forms[0].expDestacado.disabled = false;

    document.getElementById('asunto').firstChild.data = listaPreVis[this.id][3];
    document.getElementById('usuarioInicio').firstChild.data = listaPreVis[this.id][4];
    document.getElementById('unidadInicio').firstChild.data = listaPreVis[this.id][5];
    document.getElementById('fechaInicio').firstChild.data = listaPreVis[this.id][6];    

  });
  
  $("#tablaExpPendientes tbody tr").on("dblclick", function() {
       cargarExpediente(this.id);
  });
  
  enlacesE();
  pleaseWait('off');        
}

// JAVASCRIPT DE LA TABLA DE EXPEDIENTES PENDIENTES *******************************************************************

function buscarAnotacionE(i) { 
  paginaActualE=1;
  i = i+1;
  paginaActualE = Math.ceil(i/lineasPaginaE);
  return paginaActualE;
}

function enlacesE() {
        document.getElementById('enlaceE').innerHTML = enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>',paginaActualE,numeroPaginasE,'cargaPaginaE','<%=descriptor.getDescripcion("numResultados")%>',<%=numRelacionExpedientes%>);
}

function cargaPaginaE(numeroPaginaE){
    pleaseWait('on');
  listaE = new Array();
  listaOriginalE= new Array();
  listaPreVis = new Array();
  document.forms[0].paginaListado.value = numeroPaginaE;
  document.forms[0].numLineasPaginaListado.value = lineasPaginaE;
  var codRangoTempFiltro = document.forms[0].codFiltroRangoTemp.value;

  if (filtro=='expedientesPendientes'){
  document.forms[0].opcion.value="cargar_pagina_exp_pendientes";
  }
  else {
      document.forms[0].opcion.value="cargar_pagina_exp_filtrados";
  }
  
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>" + 
                  "?codRangoTempFiltro="+codRangoTempFiltro;
  document.forms[0].submit();
}
// FIN DEL JAVASCRIPT DE LA TABLA DE EXPEDIENTES PENDIENTES ***********************************************************

function pulsarIniciarExpediente() {
  var source = "<c:url value='/sge/Tramitacion.do?opcion=iniciarExpediente'/>";

  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana',
	'width=700,height=600,status='+ '<%=statusBar%>',function(datosConsulta){
                        if(datosConsulta!=undefined){
                          if(datosConsulta[0] =="si") {
                            document.forms[0].codMunicipio.value=datosConsulta[1];
                            document.forms[0].codProcedimiento.value=datosConsulta[2];
                            document.forms[0].uor.value=datosConsulta[3];
                            // Se pasa la unidad del tramite de inicio seleccionada si la hubiera
                            document.forms[0].unidadTramiteInicioSeleccionada.value = datosConsulta[4];      
                            document.forms[0].opcion.value="iniciarExpediente";
                            document.forms[0].target="oculto";
                            document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
                            document.forms[0].submit();
                          }
                        }
                  });
}

function pulsarDestacarExpediente(){
   
    // comprobamos si el expediente marcado esta como destacado o no  
    var numeroExp = document.getElementById('numExp').firstChild.data;

    if ((numeroExp!='') && (document.forms[0].expDestacado.checked)){
       // preguntamos si queresmos marcar como destacado el expediente
       if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjDestacarExp")%>') ==1) {

             document.forms[0].opcion.value="destacarExpediente";
             document.forms[0].numero.value = numeroExp;
             document.forms[0].ejercicio.value = numeroExp.substring(0,4);
             document.forms[0].target="oculto";
             //document.forms[0].target="mainFrame";
             document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
             document.forms[0].submit();
             document.forms[0].expDestacado.checked = false;    // lo inicializamos al varlor por defecto

       }else document.forms[0].expDestacado.checked = false;

    }else if  ((numeroExp!='') && (!document.forms[0].expDestacado.checked)){
        // preguntamos si queresmos desmarcar el expediente
       if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjDesmarcarExp")%>') ==1) {

             document.forms[0].opcion.value="desmarcarExpediente";
             document.forms[0].numero.value = numeroExp;
             document.forms[0].ejercicio.value = numeroExp.substring(0,4);
             document.forms[0].target="oculto";
             //document.forms[0].target="mainFrame";
             document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
             document.forms[0].submit();
       
        }else document.forms[0].expDestacado.checked = true;

    }else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');

    
}

function recibirNumero(datos) {
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjIniExp1")%>' + " " + datos);
        document.forms[0].numero.value = datos;
        document.forms[0].ejercicio.value = datos.substring(0,4);
        document.forms[0].opcion.value="cargar";
        document.forms[0].target="mainFrame";
        document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
        document.forms[0].submit();
    }

function pulsarSalir() {
   window.location = "<%=request.getContextPath()%>/jsp/sge/presentacionExped.jsp";
}

/***** cargarExpediente *************
 Carga un expediente seleccionado en la tabla de expedientes pendientes
*************************************/
function cargarExpediente(i) {
if (listaOriginalE[i][9] == "") {
    pleaseWait('on');
    document.forms[0].codMunicipio.value = listaOriginalE[i][0];
    document.forms[0].codProcedimiento.value = listaOriginalE[i][1];
    document.forms[0].ejercicio.value = listaOriginalE[i][3];
    document.forms[0].numero.value = listaOriginalE[i][4];
    document.forms[0].opcion.value="cargar";
    document.forms[0].target="mainFrame";
    // original
    //document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
    document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>?filtro=" + filtro;    
    document.forms[0].submit();
} else {
    //jsp_alerta('A','<%=descriptor.getDescripcion("msjExpRelAbierta")%> : '+'<%=m_Config.getString("constante.relacion")%>'+listaOriginalE[i][9]);
    document.forms[0].codMunicipio.value = listaOriginalE[i][0];
    document.forms[0].codProcedimiento.value = listaOriginalE[i][1];
    document.forms[0].ejercicio.value = listaOriginalE[i][3];
    document.forms[0].numero.value = listaOriginalE[i][4];
    mostrarAvisoRelacion(listaOriginalE[i][9]);
}
}

/***** pulsarBuscarExpediente ******
 Busca el expediente que introduce el usuario
***********************************/
function pulsarBuscarExpediente()
{
  if (document.forms[0].numeroExpediente.value.length != 0) 
  {	
  document.forms[0].opcion.value="existeExp";
  document.forms[0].target="oculto";
  document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
  document.forms[0].submit();
  }  
}

/****** cargarExpedineteDirecto ******
 Cargo el expediente de forma directa a traves de la busqueda
 del numero de expediente
*************************************/
function cargarExpedienteDirecto() 
{
  pleaseWait('on');
  document.forms[0].opcion.value="cargar";
  document.forms[0].target="mainFrame";
  // original
  //document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>";
  document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>?filtro=" + filtro;
  document.forms[0].submit();
}

/****** mostarAviso *****************
 Muestra el aviso cuando el expediente no existe
************************************/
function mostrarAviso() {
  jsp_alerta('A','<%=descriptor.getDescripcion("msjNoExistExpPend")%>');
  document.forms[0].numeroExpediente.value = "";  
}

/****** mostarAvisoRelacion **********    
 Muestra el aviso cuando el expediente pertenece a una relación abierta
**************************************/
function mostrarAvisoRelacion(relacion) {
    //jsp_alerta('A','<%=descriptor.getDescripcion("msjExpRelAbierta")%> <br> <%=descriptor.getDescripcion("etiq_numRel")%> : '+relacion);

  	var opcionExp = jsp_alerta('B','<br> <%=descriptor.getDescripcion("msjExpRelAbierta")%> <br> <%=descriptor.getDescripcion("etiq_numRel")%> : '+ relacion 
  				+ '<br> <br><%=descriptor.getDescripcion("msjAbrirExpModConsul")%> <br><br>' );
    // Si opcionExpRel==1 entonces se abre el expediente en modo consulta 
    // sino no se hace nada
    document.forms[0].opcionExpRel.value = opcionExp;
  	if (opcionExp == 1){
  		document.forms[0].modoConsulta.value = "si";
  		cargarExpedienteDirecto();    
  	}
}

function checkKeysLocal(evento,tecla) {
    var aux=null;
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }
    else
        teclaAuxiliar = evento.which;

    keyDel(evento);
    if ((teclaAuxiliar == 40) || (teclaAuxiliar == 38)){ // Arriba/abajo
        upDownTable(tab,datosTabla,teclaAuxiliar);
    } 
    

    if (teclaAuxiliar == 1){
        if (comboFiltroProc.base.style.visibility == "visible") setTimeout('comboFiltroProc.ocultar()',20);
    }

    if (teclaAuxiliar == 9){
        comboFiltroProc.ocultar();
    }

    if(evento.button == 9){
        comboFiltroProc.ocultar();
    }
}


function pulsarBuscar(){    
    var codCriterio = document.forms[0].codCriterioBusqueda.value;
    var descCriterio = document.forms[0].descCriterioBusqueda.value;
    var codProcFiltro = document.forms[0].codFiltroProc.value;
    var codRangoTempFiltro = document.forms[0].codFiltroRangoTemp.value;

    var leyenda = "<%=leyenda%>";

    var codigo = document.forms[0].codigoCriterioBusqueda.value;
    var tipoCampoCriterio =  document.forms[0].tipoCampoCriterioBusqueda.value;
    var campoSuple = document.forms[0].campoSuplementarioCriterioBusqueda.value;
    var tipoCampoSuple = document.forms[0].tipoCampoSuplementarioCriterioBusqueda.value;
    var codDesplegable = document.forms[0].codigoDesplegable.value;
    var valores = document.forms[0].valoresCriterioBusqueda.value;
    var usuarioTramitador = document.forms[0].usuarioTramitador.value;
        
    var parametrosBusqueda = "&codigoCriterioBusqueda=" + codigo + "&tipoCampoCriterioBusqueda=" +
        tipoCampoCriterio + "&campoSuplementarioCriterioBusqueda=" + campoSuple + "&tipoCampoSuplementarioCriterioBusqueda="
      + tipoCampoSuple + "&codigoDesplegable=" +codDesplegable + "&valoresCriterioBusqueda=" + valores ;

    if(codCriterio!=null && codCriterio!="" && descCriterio!=null && descCriterio!=""){
        if(leyenda=="expedientesPendientes"){
            document.forms[0].opcion.value= filtro;
            document.forms[0].target="mainFrame";
            document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>?codProcedimientoFiltro=" + 
                    codProcFiltro + "&codRangoTempFiltro=" + codRangoTempFiltro + parametrosBusqueda + 
                    "&usuarioTramitador=" + usuarioTramitador;
            document.forms[0].submit();
        }else{
            // Se filtra por alguna de las leyendas
            document.forms[0].opcion.value = leyenda;
            document.forms[0].target="mainFrame";
            document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>?codProcedimientoFiltro=" + 
                    codProcFiltro + "&codRangoTempFiltro=" + codRangoTempFiltro + parametrosBusqueda + 
                    "&usuarioTramitador=" + usuarioTramitador;
            document.forms[0].submit();
        }        
    }else{
        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrSelectCriterioPend")%>');
    }
}

function actualizarVariablesFormularioCriterioBusqueda(codigo,tipoCampo,operador,esCampoSuplementario,tipoCampoSuplementario,codigoDesplegable,valores){
 
   if(codigo!="" && tipoCampo!=""){
        document.forms[0].codigoCriterioBusqueda.value                 = codigo;
        document.forms[0].tipoCampoCriterioBusqueda.value              = tipoCampo;
        document.forms[0].campoSuplementarioCriterioBusqueda.value     = esCampoSuplementario;
        document.forms[0].tipoCampoSuplementarioCriterioBusqueda.value = tipoCampoSuplementario;
        document.forms[0].codigoDesplegable.value                      = codigoDesplegable;
        document.forms[0].valoresCriterioBusqueda.value                = valores;

        if(tipoCampo!="I"){            
            document.forms[0].operadorCriterioBusqueda.value  = operador;   
        }else
        if(tipoCampo=="I"){           
            document.forms[0].operadorCriterioBusqueda.value  = "-1";            
        }//if        
   }// if
   pleaseWait("off");
}


function limpiarCriterio(){    
    document.forms[0].codigoCriterioBusqueda.value                 = "";
    document.forms[0].tipoCampoCriterioBusqueda.value              = "";
    document.forms[0].campoSuplementarioCriterioBusqueda.value     = "";
    document.forms[0].tipoCampoSuplementarioCriterioBusqueda.value = "";
    document.forms[0].codigoDesplegable.value                      = "";
    document.forms[0].operadorCriterioBusqueda.value               = "";
    document.forms[0].valoresCriterioBusqueda.value                = "";

    // SE HACE UNA LLAMADA SINCRONA POR AJAX PARA PODER VACIAR EL CRITERIO DE BÚSQUEDA    
    document.forms[0].opcion.value = "limpiarCriterioBusqueda";
    document.forms[0].target       = "oculto";
    document.forms[0].action       = "<c:url value='/sge/Tramitacion.do'/>";
    document.forms[0].submit();
}

function criterioBusquedaLimpio(){
    var codProcFiltro = document.forms[0].codFiltroProc.value;
    var codRangoTempFiltro = document.forms[0].codFiltroRangoTemp.value;
    comboCriterioBusqueda.selectItem(-1);
    var resultado = jsp_alerta("A",'<%=descriptor.getDescripcion("msgLimpiarCriterioBusqueda")%>');
    // Se recarga la pagina de expedientes pendientes
    document.forms[0].opcion.value = "expedientesPendientes";
    document.forms[0].target       = "mainFrame";
    document.forms[0].action       = "<c:url value='/sge/Tramitacion.do'/>?codProcedimientoFiltro=" + 
            codProcFiltro + "&codRangoTempFiltro=" + codRangoTempFiltro;
    document.forms[0].submit();
}
</SCRIPT>
</head>
<BODY class="bandaBody" onload="javascript:{inicializar();}">
    <jsp:include page="/jsp/hidepage.jsp" flush="true">
        <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
    </jsp:include>
    <html:form action="/sge/Tramitacion.do" target="_self">

    <html:hidden  property="opcion" value=""/>
    <html:hidden property="paginaListado" value="1"/>
    <html:hidden property="numLineasPaginaListado" />
    <html:hidden  property="codDepartamento" value=""/>
    <html:hidden  property="codUnidadRegistro" value=""/>
    <html:hidden  property="tipoRegistro" value=""/>
    <html:hidden  property="ejerNum" value=""/>
    <html:hidden  property="desdeFichaExpediente" value=""/>

    <input type="hidden" name="codMunicipio" >
    <input type="hidden" name="codProcedimiento" >
    <input type="hidden" name="ejercicio" >
    <input type="hidden" name="numero" >
    <input type="hidden" name="uor" >
    <input type="hidden" name="modoConsulta">
    <input type="hidden" name="opcionExpRel" >
        
    <html:hidden property="columna" value=""/>
    <html:hidden property="tipoOrden" value=""/>  
    <html:hidden property="nombreColumna" value=""/>  

    <input type="hidden" name="unidadTramiteInicioSeleccionada" value=""/>

    <html:hidden property="codigoCriterioBusqueda" value=""/>
    <html:hidden property="tipoCampoCriterioBusqueda" value=""/>
    <html:hidden property="campoSuplementarioCriterioBusqueda" value=""/>
    <html:hidden property="tipoCampoSuplementarioCriterioBusqueda" value=""/>
    <html:hidden property="codigoDesplegable" value=""/>
    <html:hidden property="operadorCriterioBusqueda" value=""/>
    <html:hidden property="valoresCriterioBusqueda" value=""/>
    
    <html:hidden property="columnaPendientes" value=""/>
    <html:hidden property="ordenColumnaPendientes" value=""/>    
    <html:hidden property="usuarioTramitador"/>
    <div class="txttitblanco">
            <%=descriptor.getDescripcion("tit_ExpPend")%>
          <!--  FILTRO POR PROCEDIMIENTO --->
            <input id="codFiltroRangoTemp" name="codFiltroRangoTemp" type="text" style="display:none" maxlength="1"
                onkeyup="return xAMayusculas(this);" 
                onchange="javascript:{filtrarPorProcedimientoYRango();}"
                onblur     ="javascript:{filtrarPorProcedimientoYRango();}"
                onfocus   ="javascript:{filtrarPorProcedimientoYRango();}">
            <input id="descFiltroRangoTemp" name="descFiltroRangoTemp"  type="text" class="inputTexto" style="width:10%" readonly
                onchange="javascript:{filtrarPorProcedimientoYRango();}"
                onblur     ="javascript:{filtrarPorProcedimientoYRango();}"
                onfocus   ="javascript:{filtrarPorProcedimientoYRango();}">
            <a id="anchorFiltroRangoTemp" name="anchorFiltroRangoTemp" href="">
               <span class="fa fa-chevron-circle-down enTxttitblanco" aria-hidden="true" id="botonRango" name="botonRango" style="cursor:hand;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
            </a>
                <span class="etiqueta" style="color:white;margin-left:0.5%"><%= descriptor.getDescripcion("etiqFiltroProc")%></span>                        
                <input id="codFiltroProc" name="codFiltroProc" type="text" class="inputTexto"  style="width:4%" maxlength="5"
                    onkeyup="return xAMayusculas(this);"
                    onchange="javascript:{filtrarPorProcedimientoYRango();}"
                    onblur     ="javascript:{filtrarPorProcedimientoYRango();}"
                    onfocus   ="javascript:{filtrarPorProcedimientoYRango();}">
                <input id="descFiltroProc" name="descFiltroProc" type="text" class="inputTexto" style="width:28%" readonly 
                    onchange ="javascript:{filtrarPorProcedimientoYRango();}"
                    onblur      ="javascript:{filtrarPorProcedimientoYRango();}"
                    onfocus    ="javascript:{filtrarPorProcedimientoYRango();}">
                <a id="anchorFiltroProc" name="anchorFiltroProc" href="">
                    <span class="fa fa-chevron-circle-down enTxttitblanco" aria-hidden="true" id="botonTipoDoc" name="botonTipoDoc" style="cursor:hand;" alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" style="cursor:hand;"></span>
                </a>
    </div>
    <div class="contenidoPantalla">
        <table style="width:100%">
            <tr>
                <td colspan="4">
                    <div class="dataTables_wrapper paxinacionDataTables">
                        <label id="contSelectPax"></label>
                    </div>
                </td>
            </tr>            
            <TR>
                <td colspan="5">
                    <table id="tablaExpPendientes" class="xTabla compact tablaDatos">
                    </table>
                </td>
            </TR>                  
            <TR>
                <td colspan="5">
                    <div id="enlaceE" STYLE="width:100%" class="dataTables_wrapper"></div>
                </td>
            </TR>                  
            <tr>
                <td colspan="5" class="etiqueta">
                    <%= descriptor.getDescripcion("etiq_leyenda")%>:
                </td>
            </tr>
            <tr>
                <td>
                    <c:url value="/sge/Tramitacion.do" var="plazoCercaFin">
                       <c:param name="opcion" value="plazoCercaFin"/>
                   </c:url>
                   <a href="<c:out value='${plazoCercaFin}'/>" onclick="pleaseWait('on');" class="cercaFueraPlazo" title="<%= descriptor.getDescripcion("etiq_cercanaFinPl")%>">
                       <%= descriptor.getDescripcion("etiq_cercanaFinPl")%>
                   </a>
               </td>
               <td>
                     <c:url value="/sge/Tramitacion.do" var="fueraPlazo">
                       <c:param name="opcion" value="fueraPlazo"/>
                   </c:url>
                   <a href="<c:out value='${fueraPlazo}'/>" onclick="pleaseWait('on');" class="fueraPlazo" title="<%= descriptor.getDescripcion("etiq_fueraPl")%>">
                       <%= descriptor.getDescripcion("etiq_fueraPl")%>
                   </a>
               </td>
               <td>
                    <c:url value="/sge/Tramitacion.do" var="pendientesEstaUnidad">
                       <c:param name="opcion" value="pendientesEstaUnidad"/>
                   </c:url>
                   <a href="<c:out value='${pendientesEstaUnidad}'/>" onclick="pleaseWait('on');" class="pendientesEstaUnidad" title="<%= descriptor.getDescripcion("etiq_pendEstaUni")%>">
                       <%= descriptor.getDescripcion("etiq_pendEstaUni")%>
                   </a>
               </td>
               <td>
                    <c:url value="/sge/Tramitacion.do" var="pendientesOtraUnidad">
                       <c:param name="opcion" value="pendientesOtraUnidad"/>
                   </c:url>
                   <a href="<c:out value='${pendientesOtraUnidad}'/>" onclick="pleaseWait('on');" class="pendientesOtraUnidad" title="<%= descriptor.getDescripcion("etiq_pendOtraUni")%>">
                       <%= descriptor.getDescripcion("etiq_pendOtraUni")%>
                   </a>
               </td>
               <td>
                    <c:url value="/sge/Tramitacion.do" var="usuarioTramitador">
                       <c:param name="opcion" value="inicio"/>
                       <c:param name="usuarioTramitador">
                           <%=usuario.getIdUsuario()%>
                       </c:param>
                   </c:url>
                   <a href="<c:out value='${usuarioTramitador}'/>" onclick="pleaseWait('on');" title="<%= descriptor.getDescripcion("misExpedientes")%>">
                       <%= descriptor.getDescripcion("misExpedientes")%>
                   </a>
               </td>
            </tr>
            <tr>
            <td>
                <c:url value="/sge/Tramitacion.do" var="expPlazoCercaFin">
                    <c:param name="opcion" value="expPlazoCercaFin"/>
                </c:url>
                <a href="<%=request.getContextPath()%>/sge/Tramitacion.do?opcion=expPlazoCercaFin" onclick="pleaseWait('on');" class="otros" title="<%= descriptor.getDescripcion("etiq_expCercaFinPl")%>">
                    <%= descriptor.getDescripcion("etiq_expCercaFinPl")%>
                </a>
                <span class='fa fa-clock-o'></span>
            </td>                                                                    
            <td align="top">
                  <c:url value="/sge/Tramitacion.do" var="expFueraPlazo">
                    <c:param name="opcion" value="expFueraPlazo"/>
                </c:url>
                <a href="<%=request.getContextPath()%>/sge/Tramitacion.do?opcion=expFueraPlazo"  onclick="pleaseWait('on');" class="otros" title="<%= descriptor.getDescripcion("etiq_expFueraPl")%>">
                    <%= descriptor.getDescripcion("etiq_expFueraPl")%>
                </a>
                <span class='fa fa-exclamation-triangle'></span>
            </td>
            <td align="top">
                  <c:url value="/sge/Tramitacion.do" var="expImportantes">
                          <c:param name="opcion" value="expImportantes"/>
                  </c:url>
                  <a href="<%=request.getContextPath()%>/sge/Tramitacion.do?opcion=expImportantes" onclick="pleaseWait('on');" class="otros" title="<%= descriptor.getDescripcion("etiq_expDestacados")%>">
                       <%= descriptor.getDescripcion("etiq_expDestacados")%>
                  </a>
                  <span class='fa fa-flag'></span>
            </td>
             <td align="top">
                  <c:url value="/sge/Tramitacion.do" var="pendientesEstaUnidadCargo">
                          <c:param name="opcion" value="pendientesEstaUnidadCargo"/>
                  </c:url>
                  <a href="<c:out value='${pendientesEstaUnidadCargo}'/>" onclick="pleaseWait('on');" class="pendientesEstaUnidad" title="<%= descriptor.getDescripcion("etiq_pendEstaUniCargo")%>">
                       <%= descriptor.getDescripcion("etiq_pendEstaUniCargo")%>
                  </a>
             </td>
             <td align="top">
                  <c:url value="/sge/Tramitacion.do" var="expAlarmas">
                          <c:param name="opcion" value="expAlarmas"/>
                  </c:url>
                  <a href="<%=request.getContextPath()%>/sge/Tramitacion.do?opcion=expAlarmas" onclick="pleaseWait('on');" class="otros" title="<%= descriptor.getDescripcion("etiq_expAlarmas")%>">
                       <%= descriptor.getDescripcion("etiq_expAlarmas")%>
                  </a>
                  <span class='fa fa-bell'></span>
            </td>
        </tr>
        <tr>                                                  
            <td colspan="5" class="etiqueta">
                <%=descriptor.getDescripcion("titCriterioBusquedaPendientes")%>:
            </td>
        </tr>
        <tr>
            <td colspan="5" style="width: 16%" class="columnP">
                <input id="codCriterioBusqueda" name="codCriterioBusqueda" type="text" class="inputTexto" size="1" maxlength="5"
                        onkeyup="return xAMayusculas(this);"
                        onchange="javascript:{filtrarPorProcedimientoYRango();}"
                        onblur  ="javascript:{filtrarPorProcedimientoYRango();}"
                        onfocus ="javascript:{filtrarPorProcedimientoYRango();}" style="display:none">
                <input id="descCriterioBusqueda" name="descCriterioBusqueda" type="text" class="inputTexto" size="30" readonly
                    onchange ="javascript:{filtrarPorProcedimientoYRango();}"
                    onblur      ="javascript:{filtrarPorProcedimientoYRango();}"
                    onfocus    ="javascript:{filtrarPorProcedimientoYRango();}"
                    style="margin-left:0px;width:200px">

                <a id="anchorCriterioBusqueda" name="anchorCriterioBusqueda" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoDoc" name="botonTipoDoc" style="cursor:hand;"	alt = "<%=descriptor.getDescripcion("altDesplegable")%>" title = "<%=descriptor.getDescripcion("altDesplegable")%>" border="0" style="cursor:hand;"></span></a>
                &nbsp;                                                                
                <span class="fa fa-search" aria-hidden="true"  id="botonTerceros" name="botonTerceros" style="cursor:hand;border-style: hidden;"
                     onclick="javascript:filtrarCriterioBusqueda();" alt='<%=descriptor.getDescripcion("altBuscarExpPendCriterio")%>' title='<%=descriptor.getDescripcion("altBuscarExpPendCriterio")%>'/></span>
                <input type="button" class="botonLargo2" accesskey="I" style="float:right" value='<%=descriptor.getDescripcion("gbIniciar")%>' name="cmdIniciar"   onclick="pulsarIniciarExpediente();">
            </td>
        </tr>                                         
        <tr>
            <td colspan="5" class="sub3titulo"><%=descriptor.getDescripcion("titInfoExpediente")%></td>
        </tr>
        <tr>
            <td colspan="5" style="padding-left:20px">
                <div style="float:left;width:50%;vertical-align:top">
                    <div style="width:100%;vertical-align:top" class="etiqueta">
                        <%=descriptor.getDescripcion("etiq_expDestacado")%>:
                        <input type="checkbox" id="expDestacado" name="expDestacado" align="left"  disabled  onclick="pulsarDestacarExpediente();"  />
                    </div>
                    <div style="width:100%;vertical-align:top" class="etiqueta">
                        <%=descriptor.getDescripcion("etiqNumExpediente")%>:&nbsp;
                        <span id="numExp" class="textoSuelto" align="left">&nbsp;</span>
                    </div>
                    <div style="width:100%;vertical-align:top" class="etiqueta">
                        <%=descriptor.getDescripcion("etiqIntExpediente")%>:
                        <span id="terceros" class="textoSuelto">&nbsp;</span>
                    </div>
                    <div style="width:100%;vertical-align:top" class="etiqueta">
                        <%=descriptor.getDescripcion("etiqTramPend")%>:<br>
                        <div style="padding-left:5px" id="tramPend" class="textoSuelto"></div>
                    </div>
                </div>
                <div style="float:left;width:50%;vertical-align:top">
                    <div style="width:100%;vertical-align:top" class="etiqueta">
                        <%=descriptor.getDescripcion("gEtiq_Asunto")%>:&nbsp;
                        <span id="asunto" class="textoSuelto" align="left">&nbsp;</span>
                    </div>
                    <div style="width:100%;vertical-align:top" class="etiqueta">
                        <%=descriptor.getDescripcion("etiqUsuInicio")%>:&nbsp;
                        <span id="usuarioInicio" class="textoSuelto" align="left">&nbsp;</span>
                    </div>
                    <div style="width:100%;vertical-align:top" class="etiqueta">
                        <%=descriptor.getDescripcion("etiqUnidadInicio")%>:&nbsp;
                        <span id="unidadInicio" class="textoSuelto" align="left">&nbsp;</span>
                    </div>
                    <div style="width:100%;vertical-align:top" class="etiqueta">
                        <%=descriptor.getDescripcion("etiqFechaInicio")%>:&nbsp;
                        <span id="fechaInicio" class="textoSuelto" align="left">&nbsp;</span>
                    </div>
                    <div style="width:100%;vertical-align:top" class="etiqueta">
                        <%=descriptor.getDescripcion("etiq_expAlarmas")%>:&nbsp;
                        <span id="alarmasVencidas" class="textoSuelto" align="left">&nbsp;</span>
                    </div>
                </div>
            </td>  
        </tr>
    </TABLE>
</div>                                    
</html:form>
<script type="text/javascript">
    // Creación del combo para realizar el filtrado
    var comboFiltroProc = new Combo("FiltroProc");
    cargarComboFiltroProcedimientos();

    // Se indica que función se llama cuando cambie el valor del combo
    comboFiltroProc.change=filtrarPorProcedimientoYRango;
    
    // Combo para el criterio de búsqueda
    var comboCriterioBusqueda = new Combo("CriterioBusqueda");
    cargarComboCriterioBusqueda();
    //comboCriterioBusqueda.change = filtrarCriterioBusqueda;

    // Creación del combo para rango temporal de expedientes
    var comboFiltroRangoTemp = new Combo("FiltroRangoTemp");
    cargarComboFiltroRangoTemporal();

    // Se indica que función se llama cuando cambie el valor del combo
    comboFiltroRangoTemp.change=filtrarPorProcedimientoYRango;

    cargarComboFilasPagina();

    /**
   * Esta funcion carga el combo de filtro de procedimientos
   **/
function cargarComboFiltroProcedimientos(){
    var codigosProcedimientos = new Array();
    var descripcionesProcedimientos = new Array();

    // Código de la opción "TODOS LOS PROCEDIMIENTOS".
    // Es lo suficientemente grande para que no haya un código de procedimiento que coincida
    codigosProcedimientos[0]         = "";
    descripcionesProcedimientos[0] = '<%=descriptor.getDescripcion("etiqFiltroProcExpPendientes")%>';

    var contador = 1;
    <%
        Vector procedimientos = (Vector)request.getAttribute("procedimientosFiltroExpPendientes");
        for(int i=0;procedimientos!=null && i<procedimientos.size();i++)
        {
                DefinicionProcedimientosValueObject proc = (DefinicionProcedimientosValueObject)procedimientos.get(i);
    %>
                codigosProcedimientos[contador]          = "<%=proc.getTxtCodigo()%>";
                descripcionesProcedimientos[contador] = "<%=proc.getTxtDescripcion()%>";
                contador++;
    <%
        } //for
    %>
    comboFiltroProc.addItems(codigosProcedimientos,descripcionesProcedimientos);

    if(codProcedimientoFiltro=="" || codProcedimientoFiltro=="null" || codProcedimientoFiltro.length==0){              
        comboFiltroProc.deactivate();        
        comboFiltroProc.selectItem(1);        
        comboFiltroProc.activate();
    }
}

    
function filtrarPorProcedimientoYRango(){ 
    pleaseWait("on");
    var codProcFiltro = document.forms[0].codFiltroProc.value;
    var descProcFiltro = document.forms[0].descFiltroProc.value;
    var codRangoTempFiltro = document.forms[0].codFiltroRangoTemp.value;
    // 
    codProcFiltro=codProcFiltro.toUpperCase();
    descProcFiltro=descProcFiltro.toUpperCase;
    //var usuarioTramitador = document.forms[0].usuarioTramitador.value;
     
    if(descProcFiltro=='<%=descriptor.getDescripcion("etiqFiltroProcExpPendientes")%>'){
        window.location.href = "<%=request.getContextPath()%>/sge/Tramitacion.do?opcion=expedientesPendientes"+
            "&pantalla=expedientesPendientes&codRangoTempFiltro=" + codRangoTempFiltro;                
    }
    else
    if((codProcFiltro!=undefined && codProcFiltro!=codProcedimientoFiltro && codProcedimientoFiltro!=null) ||
        codRangoTempFiltro != codRangoTemporalFiltro){
        window.location.href = "<%=request.getContextPath()%>/sge/Tramitacion.do?opcion=inicio&codProcedimientoFiltro=" 
                + codProcFiltro + "&pantalla=expedientesPendientes&codRangoTempFiltro=" + codRangoTempFiltro;                    
    }
}

/******************************** CRITERIOS DE BÚSQUEDA *****************************************/
/**
   * Esta funcion carga el combo de filtro de procedimientos
   **/
function cargarComboCriterioBusqueda(){
     var indice = 0;
    <logic:present name="TramitacionForm" property="criteriosBusquedaExpPendientes">
       <logic:iterate id="criterio" name="TramitacionForm" property="criteriosBusquedaExpPendientes">
        // Se añade un nuevo campo con la etiqueta del idioma en la lista de campos que forman las columnas del listado
        listaCodigosBusqueda[indice]         = ['<bean:write name="criterio" property="codigo" />'];
        listaDescripcionesBusqueda[indice]   = ['<bean:write name="criterio" property="nombre" />'];
        listaCamposCriteriosBusqueda[indice] = ['<bean:write name="criterio" property="codigo" />',
                                                '<bean:write name="criterio" property="nombre" />',
                                                '<bean:write name="criterio" property="campoSuplementario"/>',
                                                '<bean:write name="criterio" property="tipoCampoSuplementario"/>',
                                                '<bean:write name="criterio" property="tipoCampoFijo"/>',                                                
                                                '<bean:write name="criterio" property="codigoDesplegable"/>'];
        indice++;
       </logic:iterate>
     </logic:present>     
     comboCriterioBusqueda.addItems(listaCodigosBusqueda,listaDescripcionesBusqueda);
     comboCriterioBusqueda.activate();
}

function cargarComboFiltroRangoTemporal(){
    var codigosRangos = new Array();
    var descripcionesRangos = new Array();

    codigosRangos[0]         = "0";
    descripcionesRangos[0] = '<%=descriptor.getDescripcion("gbTodos")%>';
    codigosRangos[1]         = "1";
    descripcionesRangos[1] = '<%=descriptor.getDescripcion("ultSeisM")%>';
    codigosRangos[2]         = "2";
    descripcionesRangos[2] = '<%=descriptor.getDescripcion("ultDoceM")%>';
    
    comboFiltroRangoTemp.addItems(codigosRangos,descripcionesRangos);
    
    comboFiltroRangoTemp.deactivate();        
    comboFiltroRangoTemp.selectItem(parseInt(codRangoTemporalFiltro)+1);        
    comboFiltroRangoTemp.activate();
}

function cargarComboFilasPagina(){
    var selectorDeFilas = '<select name="filasPagina" id="filasPagina" class ="" onchange="javascript:cambiarFilasPagina();">' + 
                                    '<option value="10">10</option>' + 
                                    '<option value="25">25</option>' + 
                                    '<option value="50">50</option>' + 
                                    '<option value="100">100</option>' + 
                                '</select>';
    document.getElementById('contSelectPax').innerHTML = '<%=descriptor.getDescripcion("mosFilasPag")%>'.replace('_MENU_',selectorDeFilas); 
    
    var lineasSeleccionadas = '<%=tVO.getNumLineasPaginaListado()%>' == 'null' || '<%=tVO.getNumLineasPaginaListado()%>' == ''?'10':'<%=tVO.getNumLineasPaginaListado()%>';
    document.getElementById('filasPagina').value= lineasSeleccionadas;
    document.forms[0].numLineasPaginaListado.value = lineasSeleccionadas;
}

function cambiarFilasPagina(){ 
    lineasPaginaE = document.getElementById('filasPagina').value;
    document.forms[0].numLineasPaginaListado.value = lineasPaginaE;

    cargaPaginaE(1);
}

function tratarValores(valores){
    var separador = "§¥";
    var salida ="";
    if(valores!=null && valores.length>0){
        var datos = new Array();
        datos = valores.split(separador);
        for(i=0;i<datos.length;i++){
            salida = salida + datos[i];
            if(datos.length-i>1)
                salida = salida + separador;
        }// for
    }// if
    return salida;
}

function filtrarCriterioBusqueda(){        
    var codCriterioBusqueda = document.forms[0].codCriterioBusqueda.value;
    var descCriterioBusqueda = document.forms[0].descCriterioBusqueda.value;

    if(codCriterioBusqueda!=null && codCriterioBusqueda!="" && descCriterioBusqueda!=null && descCriterioBusqueda!=""){    
        var esCampoSuplementario;
        var tipoCampoSuplementario;
        var tipoCampoFijo;
        var codigoDesplegable;

        /** Se buscan el resto de información del campo seleccionado **/
        for(i=0;i<listaCamposCriteriosBusqueda.length;i++){
            var codigo = listaCamposCriteriosBusqueda[i][0];
            if(codigo==codCriterioBusqueda){
                esCampoSuplementario   = listaCamposCriteriosBusqueda[i][2];
                tipoCampoSuplementario = listaCamposCriteriosBusqueda[i][3];
                tipoCampoFijo          = listaCamposCriteriosBusqueda[i][4];                
                codigoDesplegable      = listaCamposCriteriosBusqueda[i][5];

                break;
            }
        }// for        
        // Se abre la ventana popup en la que se introduce el criterio de búsqueda
        var argumentos = new Array();
        
        // Valores que toma el criterio seleccionado
        var valoresCriterio  = tratarValores(document.forms[0].valoresCriterioBusqueda.value);

        var parametro = "?codigo=" + codCriterioBusqueda + "&nombre=" + descCriterioBusqueda + "&esCampoSuplementario=" + esCampoSuplementario + "&tipoCampoSuplementario=" + tipoCampoSuplementario + "&tipoCampoFijo="+ tipoCampoFijo
                      + "&codigoDesplegable=" + codigoDesplegable + "&operadorSeleccionado=" + document.forms[0].operadorCriterioBusqueda.value
                      + "&valoresSeleccionado=" + valoresCriterio + "&codigoCampoSeleccionado=" + document.forms[0].codigoCriterioBusqueda.value;

        var source = "<%=request.getContextPath()%>/CriterioBusquedaExpedientesPendientes.do" + parametro;
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source, argumentos,
	'width=800,height=450,status='+ '<%=statusBar%>',function(datos){
                            if(datos!=undefined && datos.length>0){
                                var separador = "§¥";
                                var codigo      = datos[0];
                                
                                if(codigo!="LIMPIAR"){
                                    if(datos[1]!="I" && datos[1]!="DN"){
                                        document.forms[0].codigoCriterioBusqueda.value                 = datos[0];
                                        document.forms[0].tipoCampoCriterioBusqueda.value              = datos[1];
                                        document.forms[0].campoSuplementarioCriterioBusqueda.value     = datos[2];
                                        document.forms[0].tipoCampoSuplementarioCriterioBusqueda.value = datos[3];
                                        document.forms[0].codigoDesplegable.value                      = datos[4];
                                        document.forms[0].operadorCriterioBusqueda.value               = datos[5];

                                        var valores = "";
                                        for(i=6;i<datos.length;i++){
                                            valores = valores + datos[i];
                                            if(datos.length-i>1)
                                                valores = valores + separador;
                                        }// for

                                    }else
                                    if(datos[1]=="I"){
                                        document.forms[0].codigoCriterioBusqueda.value                 = datos[0];
                                        document.forms[0].tipoCampoCriterioBusqueda.value              = datos[1];
                                        document.forms[0].campoSuplementarioCriterioBusqueda.value     = datos[2];
                                        document.forms[0].tipoCampoSuplementarioCriterioBusqueda.value = datos[3];
                                        document.forms[0].codigoDesplegable.value                      = datos[4];
                                        document.forms[0].operadorCriterioBusqueda.value               = "-1";

                                        var valores="";
                                        for(i=5;i<datos.length;i++){
                                            valores = valores + datos[i];
                                            if(datos.length-i>1)
                                                valores = valores + separador;
                                        }// for

                                    }else
                                    if(datos[1]=="DN"){
                                        document.forms[0].codigoCriterioBusqueda.value                 = datos[0];
                                        document.forms[0].tipoCampoCriterioBusqueda.value              = datos[1];
                                        document.forms[0].campoSuplementarioCriterioBusqueda.value     = datos[2];
                                        document.forms[0].tipoCampoSuplementarioCriterioBusqueda.value = datos[3];
                                        document.forms[0].codigoDesplegable.value                      = datos[4];
                                        document.forms[0].operadorCriterioBusqueda.value               = datos[5];
                                        valores = datos[6];
                                    }

                                    document.forms[0].valoresCriterioBusqueda.value = valores;
                                    pulsarBuscar();
                                }else{
                                    limpiarCriterio();
                                }
                            } else{            
                                // Se comprueba si hay algun criterio de búsqueda, porque en ese caso ese será el actual y deberá ser el
                                // que está marcado en el desplegable
                                if(document.forms[0].codigoCriterioBusqueda.value!=null && document.forms[0].codigoCriterioBusqueda.value!=""){
                                    comboCriterioBusqueda.buscaCodigo(document.forms[0].codigoCriterioBusqueda.value);
                                }else // si no había niingún criterio de búsqueda seleccionado se deja el desplegable con el ítem vacío
                                    comboCriterioBusqueda.selectItem(-1);

                            }
                    });
    }//if
    else{        
        jsp_alerta("A",'<%=descriptor.getDescripcion("msgCriterioNoSeleccionado")%>');
    }
}

/******************************** CRITERIOS DE BÚSQUEDA *****************************************/

<%String Agent = request.getHeader("user-agent");%>

var coordx=0;
var coordy=0;


<%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>

    document.onmouseup = checkKeys;

  function checkKeysLocal(evento,tecla){
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

    //** Esta funcion se debe implementar en cada JSP para particularizar  **//
    //** las acciones a realizar de las distintas combinaciones de teclas  **//
    keyDel(evento);
    if(teclaAuxiliar == 1){
        if (comboFiltroProc.base.style.visibility == "visible" && isClickOutCombo(comboFiltroProc,coordx,coordy)) setTimeout('comboFiltroProc.ocultar()',20);
        if (comboCriterioBusqueda.base.style.visibility == "visible" && isClickOutCombo(comboCriterioBusqueda,coordx,coordy)) setTimeout('comboCriterioBusqueda.ocultar()',20);
    }

    if(teclaAuxiliar == 9){
        comboFiltroProc.ocultar();
        comboCriterioBusqueda.ocultar();
    }
    
    // #209815
    if(teclaAuxiliar==13 && document.forms[0].descCriterioBusqueda.value!='') { // si hay un criterio de búsqueda y pulsamos enter
        filtrarCriterioBusqueda();
    }

    }

</script>
</BODY>
</html:html>
