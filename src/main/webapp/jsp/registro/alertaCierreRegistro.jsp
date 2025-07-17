<%-- 
    Document   : alertaCierreRegistro
    Created on : 12-nov-2008, 10:48:20
    Author     : juan.jato
--%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>

<%
	UsuarioValueObject usuarioVO = new UsuarioValueObject();
	int idioma=1;

	if (session.getAttribute("usuario") != null){
		usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
		idioma = usuarioVO.getIdioma();
	}

%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="1" />

<html>
<head><jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" href="<c:url value='/css/estilo.css'/>" type="text/css">
<script language="JavaScript" SRC="<c:url value='/scripts/general.js'/>"></script>
<script>
    
function devolver(respuesta) {
   self.parent.opener.retornoXanelaAuxiliar(respuesta);
}

</script>
        <title><c:out value="${param.TituloMSG}"/></title>
</head>
<body class="alertaShowModal">
    <center>
       <table bgcolor="#FFFFFF" width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td bgcolor="#FFFFFF" align="center">
              <span class="fa fa-question"></span>
          </td>
          <td bgcolor="#FFFFFF" align="left" class="txtverdebold" id="alert_stl"><%=request.getParameter("DescMSG")%></td>
        </tr>

        <tr>
          <td bgcolor="#FFFFFF" width="100%" colspan="2" align="center">
             <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <td bgcolor="#FFFFFF" width="33%" align="center">
                   <input type="button" class="boton" value="<%=descriptor.getDescripcion("gbVerReservas")%>" onClick="devolver('verReservas');">
                </td>
                <td bgcolor="#FFFFFF" width="33%" align="center">
                   <input type="button" class="boton" value="<%=descriptor.getDescripcion("gbAnular")%>" onClick="devolver('anularReservas');">
                </td>
                <td bgcolor="#FFFFFF" width="33%" align="center">
                   <input type="button" class="boton" value="<%=descriptor.getDescripcion("gbCancelar")%>" onClick="devolver('cancelar');">
                </td>
             </table>
          </td>
        </tr>
        <tr height="35px">
          <td bgcolor="#FFFFFF" width="100%" colspan="2" align="center"></td>
        </tr>
      </table>
    </center>
</body>

</html>
