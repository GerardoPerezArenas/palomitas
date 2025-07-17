<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@page import="es.altia.agora.business.registro.RegistroValueObject" %>


<html>
<head>
<title> Oculto para el mantenimiento de Documentos </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script>

function redirecciona()
{
  var cont = 0;
  var cont2 = 0;
  var cont3 = 0;
  var listaIde = new Array();
  var lis = new Array();
  var uti = new Array();
  var utiCerrados = new Array();
  <logic:iterate id="b" name="ManActuacionesForm" property="codigos">
      listaIde[cont] = ['<bean:write name="b" property="ide"/>'];
      lis[cont] = ['<bean:write name="b" property="codigo"/>', '<bean:write name="b" property="descripcion"/>',
                     '<bean:write name="b" property="fechaDesde"/>', '<bean:write name="b" property="fechaHasta"/>'];
      cont = cont + 1;
  </logic:iterate>
  <logic:iterate id="ut" name="ManActuacionesForm" property="utilizados">
      uti[cont2] = ['<bean:write name="ut"/>'];
      cont2 = cont2 + 1;
  </logic:iterate>

  <logic:iterate id="utC" name="ManActuacionesForm" property="utilizadosCerrados">
      utiCerrados[cont3] = ['<bean:write name="utC"/>'];
      cont3 = cont3 + 1;
  </logic:iterate>

  parent.mainFrame.recuperaDatos(listaIde,lis,uti,utiCerrados);

}
</script>

</head>
<body onLoad="redirecciona();">

<input type="hidden" name="opcion" >

<p>&nbsp;<p><center>


</body>
</html>
