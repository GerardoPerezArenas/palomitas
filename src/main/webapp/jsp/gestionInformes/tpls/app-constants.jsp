<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ page import="es.altia.common.service.config.*" %>

<%
  Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");
  String hostVirtual = m_ConfigTechnical.getString("hostVirtual");
  %>

<script type="text/javascript">
    var APP_TITLE="<fmt:message key='GestionInformes.Application.title'/>";
    var APP_CONTEXT_PATH="<c:out value='${pageContext.request.contextPath}'/>";
    var DOMAIN_NAME = "";
    var dom='<%=hostVirtual%>';
    if (dom.indexOf("http")==0) {
        DOMAIN_NAME = dom;
    }
</script>
