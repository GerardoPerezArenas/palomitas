<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@ page import="org.apache.struts.upload.FormFile"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteDocumentoForm"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="es.altia.agora.business.registro.RegistroValueObject"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.*"%>
<%@ page import="es.altia.agora.technical.Fecha"%>
<%@ page import="es.altia.agora.business.sge.ExpedienteOtroDocumentoVO"%>


<html>
<head><jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Gestión de Ficheros</title>
<script type="text/javascript">
function inicializar(){        
    // Se recupera el String en formato JSON con el listado de documentos para pasar a /jsp/sge/fichaExpediente.jsp
    var jsonDocumentosExternos = '<%=(String)request.getAttribute("jsonDocumentosExternos")%>';           
    self.parent.opener.retornoXanelaAuxiliar(jsonDocumentosExternos);
}
</script>
</head>
<body onload="inicializar();">
</body>
</html>
