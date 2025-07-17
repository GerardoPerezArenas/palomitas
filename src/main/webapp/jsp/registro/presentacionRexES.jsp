<%@page contentType="text/html; charset=iso-8859-1" language="java"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<html>
<head>
<title>GESTIÓN AYUNTAMIENTOS - Páxina de inicio</title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<LINK REL="stylesheet" MEDIA="screen" TYPE="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
</head>
<body onload="javascript:{ }">
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
  <td width="33%"><img src="<%=request.getContextPath()%>/images/portada/Diputacion.gif" border="0">
  </td>
  <td align="center"><img width="40%" heigth="40%" src="<%=request.getContextPath()%>/images/portada/Lousame.gif" border="0"></td>
<td></td>
</tr>
<tr>
  <td colspan="2" width="33%" class="txtverdeboldgrangran">
    <%	UsuarioValueObject usuarioVO = new UsuarioValueObject();
		if (session.getAttribute("usuario") != null)	
  			usuarioVO = (UsuarioValueObject)session.getAttribute("usuario"); %>
      <br><br><%= usuarioVO.getEnt()%>
      <br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp<%= usuarioVO.getDep()%>
      <br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= usuarioVO.getUnidadOrg()%>
  </td>
</tr>
</table>
</center>
</body>
</html>
