<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<html>
<head>
    <title> Informe Registro </title>

    <jsp:include page="/jsp/informes/tpls/app-constants.jsp" />
    <script src="<c:url value='/scripts/general.js'/>"></script>
    <script language="javascript">
    var Frame;
    var tipoVentana = 'normal';

    function cargar(){
      var nombre ='<%=request.getAttribute("nombre")%>';
      location.href="<c:url value='/jsp/verPdf2.jsp?opcion=null&nombre='/>"+nombre;
    }
    </script>
</head>
<body onLoad="cargar();">
</body>
</html>
