<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<html>
<head><jsp:include page="/jsp/editor/tpls/app-constants.jsp" />
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script>
    function mostrarAlerta() {
      jsp_alerta("A","La operación se realizó correctamente");
    }
    </script>
</head>
<body onLoad="mostrarAlerta();">
    <FORM METHOD=POST>
    </form>
</body>
</html>
