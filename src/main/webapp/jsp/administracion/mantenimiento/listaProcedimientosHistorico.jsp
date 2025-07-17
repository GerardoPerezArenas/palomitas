<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/displaytag-el.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.ProcedimientosHistoricoForm"%>
<%@page import="es.altia.flexia.historico.expediente.vo.ProcedimientoHistoricoVO"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Mantenimiento de Plazo de Paso a Histórico por Procedimientos</title>

 
<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  int idioma=1;
  int apl=5;
  String css="";
  if (session.getAttribute("usuario") != null){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    apl = usuarioVO.getAppCod();
    idioma = usuarioVO.getIdioma();
    css=usuarioVO.getCss();
  }

ProcedimientosHistoricoForm procHistForm = (ProcedimientosHistoricoForm) session.getAttribute("ProcedimientosHistoricoForm");
String resultado = procHistForm.getResultado()==null?"":procHistForm.getResultado();
procHistForm.setResultado("");
session.setAttribute("ProcedimientosHistoricoForm",procHistForm);
Config m_Config = ConfigServiceHelper.getConfig("common");%>
<!-- Estilos -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>

<script type="text/javascript">
var lista = new Array();
var datosCamposDesplegables = new Array();

// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
function inicializar(){
    window.focus();

    if ('<%=resultado%>' != "") {
        pleaseWait("off");
        jsp_alerta('A','<%=resultado%>');
    }
}

function seleccionarFila() {
    var tabla = document.getElementById("tablaProcMes");
    for (var i=0;i < tabla.rows.length;i++) {
        tabla.rows[i].className = "inactiva";
    }
    this.className = "activa";
    document.forms[0].codProcedimientoEditar.value = this.cells.item(0).innerHTML;
    document.forms[0].mesesEditar.value = this.cells.item(1).innerHTML;
    document.forms[0].codProcedimientoEditar.disabled = true;
    document.forms[0].codProcedimientoEditar.className = "inputTextoObligatorio inputTextoDeshabilitado";
}

function pulsarAlta(){
   
    if (document.forms[0].codProcedimientoEditar.disabled ){
          jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
    } else {
          document.forms[0].action = "<%=request.getContextPath()%>/administracion/mantenimiento/ProcedimientosHistorico.do?opcion=alta";
          document.forms[0].submit();
    }
}

function pulsarModificar(){
   
    if (!document.forms[0].codProcedimientoEditar.disabled){
          jsp_alerta('A','<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    } else {
          document.forms[0].codProcedimientoEditar.disabled = false;
          document.forms[0].action = "<%=request.getContextPath()%>/administracion/mantenimiento/ProcedimientosHistorico.do?opcion=modificar";
          document.forms[0].submit();
    }
}

function pulsarEliminar(){
   
    if (!document.forms[0].codProcedimientoEditar.disabled){
        jsp_alerta('A','<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    } else {
        if (jsp_alerta('C','<%=descriptor.getDescripcion("desElimPlazoHis")%>')){
            document.forms[0].codProcedimientoEditar.disabled = false;
            document.forms[0].action = "<%=request.getContextPath()%>/administracion/mantenimiento/ProcedimientosHistorico.do?opcion=eliminar";
            document.forms[0].submit();
        }
    }
}

function pulsarLimpiar(){
    document.forms[0].codProcedimientoEditar.value = "";
    document.forms[0].mesesEditar.value = "";
    document.forms[0].codProcedimientoEditar.disabled = false;
    document.forms[0].codProcedimientoEditar.className = "inputTextoObligatorio";
    var tabla = document.getElementById("tablaProcMes");
    for (var i=0;i < tabla.rows.length;i++) {
        tabla.rows[i].className = "inactiva";
    }
}

function pulsarSalir(){
    document.forms[0].target = "mainFrame";
    document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
    document.forms[0].submit();
}
$(document).ready(function() {
    var tabla = document.getElementById("tablaProcMes");
    for (var i=1;i < tabla.rows.length;i++) {
        tabla.rows[i].onclick=seleccionarFila;
    }
    tabla.rows[0].cells.item(0).style.width = "30%";

    $("#tablaProcMes").DataTable( {
        "lengthMenu": [ 10, 25, 50, 100 ],
        "autoWidth": false,
        "aaSorting": [],
        "language": {
            "search": "<%=descriptor.getDescripcion("buscar")%>",
            "previous": "<%=descriptor.getDescripcion("anterior")%>",
            "next": "<%=descriptor.getDescripcion("siguiente")%>",
            "lengthMenu": "<%=descriptor.getDescripcion("mosFilasPag")%>",
            "zeroRecords": "<%=descriptor.getDescripcion("msgNoResultBusq")%>",
            "info": "<%=descriptor.getDescripcion("mosPagDePags")%>",
            "infoEmpty": "<%=descriptor.getDescripcion("noRegDisp")%>",
            "infoFiltered": "<%=descriptor.getDescripcion("filtrDeTotal")%>",
            "paginate": {
                "first":      "<%=descriptor.getDescripcion("primero")%>",
                "last":       "<%=descriptor.getDescripcion("ultimo")%>",
                "next":       "<%=descriptor.getDescripcion("siguiente")%>",
                "previous":   "<%=descriptor.getDescripcion("anterior")%>",
            },
        }
    } );
});
</script>
</head>

<body class="bandaBody" onload="javascript:{pleaseWait('off');
        inicializar();}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<html:form action="/administracion/mantenimiento/ProcedimientosHistorico.do" method="post">    
<div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("titMantProcHist")%></div>
<div class="contenidoPantalla">
    <table style="width:100%">
        <tr>
            <td>      
                <display:table name="${sessionScope.ProcedimientosHistoricoForm.procedimientos}" id="tablaProcMes" class="xTabla" style="">
                    <display:column property="codProcedimiento" title='<%=descriptor.getDescripcion("gEtiq_Codigo")%>' style="width:30%;text-align:center" sortable="true" sortName="codProcedimiento"/>
                    <display:column property="meses" title='<%=descriptor.getDescripcion("meses")%>' style="width:70%" sortable="true" sortName="meses"/>
                </display:table>
            </td>
        </tr>
        <tr>
            <td>
                <input name="codProcedimientoEditar" id="codProcedimientoEditar" type="text" class="inputTextoObligatorio" maxlength="5" style="width:29.5%" onkeyup="return xAMayusculas(this);">
                <input name="mesesEditar" id="mesesEditar" type="text" onkeyup="return SoloDigitosNumericos(this);" class="inputTextoObligatorio" maxlength="4" style="width:69.5%">
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>"
            name="cmdAlta" onClick="pulsarAlta();" accesskey="A">
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>"
            name="cmdModificar" onClick="pulsarModificar();" accesskey="M">
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>"
            name="cmdLimpiar" onClick="pulsarLimpiar();" accesskey="M">
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>"
            name="cmdEliminar" onClick="pulsarEliminar();" accesskey="E">
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>"
            name="cmdSalir" onClick="pulsarSalir();" accesskey="S">
    </div>
</div>
</html:form>
</body>
</html>
