
<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<html>
    <head>
<title>Oculto gestion Terceros</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript">
    
var opcion="<%=request.getParameter("opcion")%>";
var frame=parent.mainFrame;

if (opcion == 'domicilioEliminado') {
    frame.domicilioEliminado();
} else if (opcion == 'terceroEliminado') {
    frame.terceroEliminado();
} else {
    alert('Opcion desconocida en ocultoGestionTerceros: ' + opcion);
}

</script>

</head>
<body>
</body>

</html>
