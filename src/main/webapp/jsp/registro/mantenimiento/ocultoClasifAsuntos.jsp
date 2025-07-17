<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ page import="es.altia.agora.interfaces.user.web.registro.mantenimiento.MantClasifAsuntosForm" %>
<%@ page import="es.altia.agora.business.registro.mantenimiento.MantClasifAsuntosValueObject" %>
<%@ page import="java.util.ArrayList" %>

<html>
<head>
<title> Oculto para el mantenimiento de Clasificaciones de asuntos </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script>

function redirecciona()
{
    var lista_claves = new Array();
    var lista        = new Array();
    var i=0;    
        
     <logic:iterate id="clasifAsunto" name="MantClasifAsuntosForm" property="clasifAsuntos">
         lista_claves[i] = ['<bean:write name="clasifAsunto" property="codigo" />']
         lista[i] = ['<bean:write name="clasifAsunto" property="codigo" />',
                     '<bean:write name="clasifAsunto" property="descripcion"/>'];
         i++;
      </logic:iterate>    
      
      parent.mainFrame.repintarTabla(lista_claves,lista);      
}
</script>
</head>
<body onLoad="redirecciona();">
</body>

</html>

