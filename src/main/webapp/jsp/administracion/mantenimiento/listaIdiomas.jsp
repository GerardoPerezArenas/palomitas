<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="java.util.Vector"%>
<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<html>
<head>
<jsp:include page="/jsp/administracion/tpls/app-constants.jsp"/>
<title>Lista de Idiomas</title>
<%
UsuarioValueObject usuarioVO = new UsuarioValueObject();
int idioma=1;
int apl=5;
if (session.getAttribute("usuario") != null) {
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    apl = usuarioVO.getAppCod();
    idioma = usuarioVO.getIdioma();
}
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" 
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
<script type="text/javascript">
var filas = new Array();

// FUNCIONES DE LIMPIEZA DE CAMPOS
function limpiarFormulario() {
    tablaProcesos.lineas = new Array();
    tablaProcesos.displayTabla();
}

// FUNCIONES DE PULSACION DE BOTONES	
function pulsarCerrar(retorno) {
    self.parent.opener.retornoXanelaAuxiliar(retorno);
}

function pulsarAceptar() {
    if (tablaIdiomas.selectedIndex>-1) {
        var valores = new Array();
        valores[0] = filas[tablaIdiomas.selectedIndex][0];
        valores[1] = '<%=session.getAttribute("MantenimientoIdiomas.codIdiomaEliminar")%>';
        pulsarCerrar(valores);
    }
}
</script>
</head>
<body class="bandaBody" onload="javascript:{pleaseWait('off');}">
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>
<form method="post">
<input type="hidden" name="opcion" value="">
<input type="hidden" name="nuevoCodIdioma" value="">
<input type="hidden" name="codIdiomaEliminar" value="">
<div class="txttitblanco"><%= descriptor.getDescripcion("gbSeleccionar")%></div>
<div class="contenidoPantalla">
    <table style="width:100%">
        <tr>
            <td id="tabla"></td>
        </tr>
    </table>
    <div id="tablaBotones" class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAceptar")%> name="botonAceptar" onClick="pulsarAceptar();" accesskey="A"> 
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="botonCancelar" onClick="pulsarCerrar();" accesskey="M"> 
    </div>				
</div>
</form>
<script type="text/javascript">
<%
if (session.getAttribute("MantenimientoIdiomas.listaIdiomas")!=null && 
            !((Vector)session.getAttribute("MantenimientoIdiomas.listaIdiomas")).isEmpty()) {
%>
    <logic:iterate id="elemento" name="MantenimientoIdiomas.listaIdiomas" scope="session">
        filas[filas.length] = ["<bean:write name="elemento" property="codigoIdioma"/>",
                                          "<bean:write name="elemento" property="nombreIdioma"/>"];
    </logic:iterate>
<%
}
%>
            
var tablaIdiomas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
  
// Tabla con columnas ordenables.
// Anadir tipo de dato a cada columna;
tablaIdiomas.addColumna('20','center','<%= descriptor.getDescripcion("gEtiqCodIdioma")%>','Number');
tablaIdiomas.addColumna('80','center','<%= descriptor.getDescripcion("gEtiqIdioma")%>','String');
tablaIdiomas.displayCabecera=true;
tablaIdiomas.setLineas(filas);
tablaIdiomas.displayTabla();
</script>
</body>
</html>
