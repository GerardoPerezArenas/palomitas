<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<html>
<head>
<title> Oculto para el mantenimiento de Organizaciones Externas </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script>

function redirecciona()
{
  var cont = 0;
  var lista = new Array();
  
  var resultado = '<bean:write name="MantRegistroExternoForm" property="resultado" />';
  <logic:iterate id="elemento" name="MantRegistroExternoForm" property="listaOrganizacionesExternas">
    lista[cont] = ['<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="descripcion"/>'];
    cont = cont + 1;
  </logic:iterate>
	
  parent.mainFrame.recuperaDatos(resultado, lista);

}
</script>
</head>
<body onLoad="redirecciona();">
</body>

</html>
