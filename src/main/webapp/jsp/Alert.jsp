<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<jsp:include page="/jsp/portafirmas/tpls/app-constants.jsp" />
<%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma = 1;
    int apl = 4;
    String css = "";
    if (session.getAttribute("usuario") != null) {
        usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
        apl = usuarioVO.getAppCod();
        idioma = usuarioVO.getIdioma();
        css = usuarioVO.getCss();
    }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" href="<c:url value='/css/estilo.css'/>" type="text/css">
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script language="JavaScript" SRC="<c:url value='/scripts/general.js'/>"></script>
<script>
function aceptarConfirm() {	
   var saida = "1";
   window.returnValue = saida;
   window.close();
}
</script>
<title><c:out value="${param.TituloMSG}"/></title>
</head>
<body>
<div class="txttitblanco"><c:out value="${param.TituloMSG}"/></div>
<div class="contenidoPantalla">
<form action="" method="post" target="_self">
    <div id="textAlerta">
        <div style="float:left">
            <c:choose>
            <c:when test="${param.TipoMSG eq 'A'}">
                <span class="fa fa-exclamation-triangle"></span>
            </c:when>
            <c:otherwise>
                <span class="fa fa-question"></span>
            </c:otherwise>
            </c:choose>
        </div>
        <span class="txtverdebold">
            <%=request.getParameter("DescMSG")%>
        </span>
    </div>
    <div id="botoneraAlerta">
        <c:choose>
        <c:when test="${param.TipoMSG eq 'A'}">
            <input type="button" class="botonGeneral" value="Aceptar" onClick="window.close();">
        </c:when>
        <c:otherwise>
            <input type="button" class="botonGeneral" value="Confirmar" onClick="aceptarConfirm();">
            <input type="button" class="botonGeneral" value="Cancelar" onClick="window.close();">
        </c:otherwise>
        </c:choose>
    </div>
</form>
</div>
</body>
</html>
