<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>


<html>
<head><jsp:include page="/jsp/planeamiento/tpls/app-constants.jsp" />
<title> Oculto para el mantenimiento de Relaciones de Bienes </title>

<script>

function redirecciona()
{
  var cont = 0;
  var lis = new Array();

    <logic:iterate id="elemento" name="MantenimientoRelacionesBienForm" property="relacionesBien">
        lis[cont] = ['<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="descripcion"/>'];
        cont = cont + 1;
    </logic:iterate>
    parent.mainFrame.recuperaDatos(lis);
}
</script>

</head>
<body onLoad="redirecciona();">

<p>&nbsp;<p><center>


</body>
</html>
