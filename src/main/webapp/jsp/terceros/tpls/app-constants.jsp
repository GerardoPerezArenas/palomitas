<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>

<script type="text/javascript">
<%Config m_Conf = ConfigServiceHelper.getConfig("common");%>
var MOSTRAR_EMPLAZAMIENTO="<%=m_Conf.getString("JSP.Emplazamiento")%>";
var APP_TITLE="<fmt:message key='Terceros.Application.title'/>";
var APP_CONTEXT_PATH="<c:out value='${pageContext.request.contextPath}'/>";
</script>
