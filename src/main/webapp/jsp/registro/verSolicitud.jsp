<%@page import="es.altia.agora.business.registro.BuzonWCValueObject,es.altia.agora.interfaces.user.web.registro.BuzonWCForm" %>
<%BuzonWCForm f= (BuzonWCForm)  session.getAttribute("BuzonWCForm");
  BuzonWCValueObject buzonVO = f.getBuzonWC();
  session.setAttribute("solicitud",buzonVO.getXML());
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>Untitled</title>
	<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
</head>
<body onload="parent.mainFrame.muestraSolicitud();">
</body>
</html>
