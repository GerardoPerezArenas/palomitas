<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<%
  response.setContentType("application/vnd.adobe.xfdf");
  String solicitud = (String)session.getAttribute("solicitud");
  Log m_log = LogFactory.getLog(this.getClass().getName());
  if(m_log.isDebugEnabled()) m_log.debug(solicitud);
  session.removeAttribute("solicitud");
%><%=solicitud%>
