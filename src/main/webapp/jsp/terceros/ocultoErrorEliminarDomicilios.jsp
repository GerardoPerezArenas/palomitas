<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@ page contentType="text/html;charset=iso-8859-1"	language="java" %>
<html>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<title>::: Oculto Error Eliminar Domicilios	:::</title>

<script type="text/javascript">
    function inicializar(){
        parent.mainFrame.errorEliminarDomicilios();
    }

</script>
<body onload="inicializar();">
<p>&nbsp;</p><center/>
</body>
</html>
