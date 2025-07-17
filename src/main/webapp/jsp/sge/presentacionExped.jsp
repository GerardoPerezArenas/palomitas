<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> GESTIÓN AYUNTAMIENTOS - Página de inicio </title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<LINK REL="stylesheet" MEDIA="screen" TYPE="text/css" href="<c:url value='/css/estilo.css'/>">

</head>

<!-- onLoad para que los submenus se vean en este frame -->

<body class="bandaBody" onload="javascript:{  }">

<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<center>
<table cellpadding="5" cellspacing="5" border="0" width="350">
<tr>
  <td width="33%"><img src="<c:url value='/images/portada/Diputacion.gif'/>" border="0">
  </td>
  <td align="center"><img width="40%" heigth="40%" src="<c:url value='/images/portada/Lousame.gif'/>" border="0"></td>
  <td></td>
</tr>
<tr>
  <td colspan="2" width="33%" class="txtverdeboldgrangran"> 
  <%
		UsuarioValueObject usuarioVO = new UsuarioValueObject();
		if (session.getAttribute("usuario") != null)	
  			usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
  %>
    <br><br><%= usuarioVO.getEnt()%>
    <br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp<%= usuarioVO.getDep()%>
    <br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= usuarioVO.getUnidadOrg()%>
  </td>
</tr>
</table>
</center>
</body>
</html>
