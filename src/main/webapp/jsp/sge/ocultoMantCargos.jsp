<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.sge.CargosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<title> Oculto para el mantenimiento de Cargos </title>
<%
	CargosForm cForm =(CargosForm)session.getAttribute("CargosExpedienteForm");
	Vector lista = cForm.getLista();
%>

<script>

function redirecciona()
{
  var lista = new Array();
	var i=0;
  	<%while(!lista.isEmpty()){
			GeneralValueObject gVO = (GeneralValueObject)lista.remove(0);%>
			lista[i] = ['<%=(String)gVO.getAtributo("cod")%>', '<%=(String)gVO.getAtributo("desc")%>', 
                  '<%=(String)gVO.getAtributo("cargo")%>', '<%=(String)gVO.getAtributo("tratam")%>'];
    	i++;
  	<%}%>
  parent.mainFrame.recuperaDatos(lista);
}
</script>

</head>
<body onLoad="redirecciona();">
</body>
</html>
