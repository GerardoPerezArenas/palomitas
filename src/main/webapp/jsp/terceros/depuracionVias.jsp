<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@ page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<html>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<title>::: Depuracion Vías :::</title>
<%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma=1;
    int apl=1;
    if (session.getAttribute("usuario") != null){
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        idioma = usuarioVO.getIdioma();
        apl =	usuarioVO.getAppCod();
    }
%>
<jsp:useBean id="descriptor" scope="request"
             class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor" property="idi_cod" value= "<%=idioma%>"/>
<jsp:setProperty name="descriptor" property="apl_cod" value="<%=apl%>"/>

<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">

<script type="text/javascript">
function noVias() {
    rows = new Array();
    tabVias.setLineas(rows);
    tabVias.displayTabla();
    jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoViasRep")%>");
}

function pulsarEliminar() {
    if (tabVias.selectedIndex>=0) {
        if (jsp_alerta("B", "<%=descriptor.getDescripcion("msjDesDepVias")%>")) {
            var codVias = "";
            codVias = codVias + tabVias.getLinea(tabVias.selectedIndex)[2] + ",";
            codVias = codVias.substring(0,codVias.length-1);
            document.forms[0].codVias.value = codVias;
            document.forms[0].target="oculto";
            document.forms[0].action="<%=request.getContextPath()%>/terceros/EliminarDepuracionVias.do";
            document.forms[0].submit();
        }
    } else {
        jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
    }
}

function pulsarVolver() {
    var codProvincia = tabViaSel.getLinea(tabViaSel.selectedIndex)[5];
    var codMunicipio = tabViaSel.getLinea(tabViaSel.selectedIndex)[7];
    document.forms[0].target="mainFrame";
    document.forms[0].action="<%=request.getContextPath()%>/terceros/CargarDepuracionVias.do?actionOrigen=volver&codProvincia=" + codProvincia + "&codMunicipio=" + codMunicipio;
    document.forms[0].submit();
}

function rellenarDatos(tableName,rowID) {    
    activarBotonEliminar();
}

function activarBotonEliminar(){
    document.forms[0].cmdEliminar.disabled=false;
}

function errorEliminarVias() {
    jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoElimDepVias")%>");
}

function rowFunctionClick(){      
}
</script>
<body class="bandaBody" onload="pleaseWait('off');">
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>
<form  method="post" target="_self">
<input  type="hidden" name="codPais" value="<%=(String) request.getAttribute("codPais")%>">
<input  type="hidden" name="codProvincia" value="<%=(String) request.getAttribute("codProvincia")%>">
<input  type="hidden" name="codMunicipio" value="<%=(String) request.getAttribute("codMunicipio")%>">
<input  type="hidden" name="codVia" value="<%=(String) request.getAttribute("codVia")%>">
<input  type="hidden" name="codVias">
<div class="txttitblanco"><%=descriptor.getDescripcion("titDepVias")%></div>
<div class="contenidoPantalla">
<div class="sub3titulo"><%= descriptor.getDescripcion("depVias_selecc")%></div>
    <table style="width:100%">
        <tr>
            <td id="tablaSel"></td>
        </tr>
        <tr>
            <td class="sub3titulo" style="margin-top:10px"><%= descriptor.getDescripcion("depVias_simil")%></td>                                        
        </tr>
        <tr>
            <td id="tabla"></td>                                                    
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bEliminar")%>' class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminar" onClick="pulsarEliminar();return false;">
        <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbVolver")%>' name="cmdVolver" onClick="pulsarVolver();return false;"/>
    </div>   
</div>   
</form>
<script type="text/javascript">
    var rows = new Array();
    var rowsSeleccionada = new Array();
    <logic:iterate id="elemento" name="DepuracionVias.vias" scope="session">
    rows[rows.length] = ["<bean:write name="elemento" property="codTipoVia"/>",
            "<bean:write name="elemento" property="tipoVia"/>",
            "<bean:write name="elemento" property="codVia"/>",
            "<bean:write name="elemento" property="descVia"/>",
            "<bean:write name="elemento" property="nombreCorto"/>",
            "<bean:write name="elemento" property="codProvincia"/>",
            "<bean:write name="elemento" property="provincia"/>",
            "<bean:write name="elemento" property="codMunicipio"/>",
            "<bean:write name="elemento" property="municipio"/>"];
    </logic:iterate>

    var tabVias = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
        '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
        '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
        '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
        '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',
        document.getElementById("tabla"));

    tabVias.addColumna('0','left','codTipoVia');
    tabVias.addColumna('100','center','<%= descriptor.getDescripcion("gEtiqTipoVia")%>');
    tabVias.addColumna('0','left','codVia');
    tabVias.addColumna('280','center','<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
    tabVias.addColumna('250','gEtiq_NomCorto','<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
    tabVias.addColumna('0','left','codProvincia');
    tabVias.addColumna('100','center','<%= descriptor.getDescripcion("gEtiqProvincia")%>');
    tabVias.addColumna('0','left','codMunicipio');
    tabVias.addColumna('160','center','<%= descriptor.getDescripcion("gEtiqMunicipio")%>');
    tabVias.displayCabecera=true;
    tabVias.setLineas(rows);
    tabVias.displayTabla();

    rowsSeleccionada = new Array();
    rowsSeleccionada[rowsSeleccionada.length] = ["<bean:write name="DepuracionVias.viaForm" scope="request" property="codTipoVia"/>",
            "<bean:write name="DepuracionVias.viaForm" scope="request" property="tipoVia"/>",
            "<bean:write name="DepuracionVias.viaForm" scope="request" property="codVia"/>",
            "<bean:write name="DepuracionVias.viaForm" scope="request" property="descVia"/>",
            "<bean:write name="DepuracionVias.viaForm" scope="request" property="nombreCorto"/>",
            "<bean:write name="DepuracionVias.viaForm" scope="request" property="codProvincia"/>",
            "<bean:write name="DepuracionVias.viaForm" scope="request" property="provincia"/>",
            "<bean:write name="DepuracionVias.viaForm" scope="request" property="codMunicipio"/>",
            "<bean:write name="DepuracionVias.viaForm" scope="request" property="municipio"/>"]

    var tabViaSel = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
            '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
            '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
            '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
            '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',
            document.getElementById("tablaSel"));

    tabViaSel.addColumna('0','left','codTipoVia');
    tabViaSel.addColumna('100','center','<%= descriptor.getDescripcion("gEtiqTipoVia")%>');
    tabViaSel.addColumna('0','left','codVia');
    tabViaSel.addColumna('280','center','<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
    tabViaSel.addColumna('250','gEtiq_NomCorto','<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
    tabViaSel.addColumna('0','left','codProvincia');
    tabViaSel.addColumna('100','center','<%= descriptor.getDescripcion("gEtiqProvincia")%>');
    tabViaSel.addColumna('0','left','codMunicipio');
    tabViaSel.addColumna('160','center','<%= descriptor.getDescripcion("gEtiqMunicipio")%>');
    tabViaSel.displayCabecera=true;
    tabViaSel.setLineas(rowsSeleccionada);
    tabViaSel.displayTabla();

</script>
</body>
</html>
