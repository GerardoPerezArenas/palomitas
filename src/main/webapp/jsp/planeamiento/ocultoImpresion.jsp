<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<html>
<head><jsp:include page="/jsp/planeamiento/tpls/app-constants.jsp" />
    <title> Informe Registro </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <script language="javascript">
    function cargar() {
      var nombre ='<%=request.getAttribute("nombre")%>';
      parent.mainFrame.abrirInforme(nombre);
    }
    </script>
</head>
<body onLoad="cargar();">
</body>
</html>
