<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<html>
<head>
<title> Oculto listado relaciones </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script type="text/javascript">

function redirecciona() {
    var opcion = '<bean:write name="MantAnotacionRegistroForm" property="respOpcionForm"/>';

    if (opcion=="existe"){
        parent.mainFrame.insertarRelacion();
    } else if (opcion != "cerrarVentanaBusqueda") {
        parent.mainFrame.alertarAsientoInexistente();
    }
}

</script>
</head>
<body onLoad="redirecciona();">
<p>&nbsp;</p>
</body>
</html>
