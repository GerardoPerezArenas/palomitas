<%--
  Created by IntelliJ IDEA.
  User: susana.rodriguez
  Date: 29-mar-2007
  Time: 18:02:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Vector" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="es.altia.agora.interfaces.user.web.gestionInformes.FichaInformeForm"%>
<%
    response.setHeader("Cache-control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", 0);

    Log m_Log = LogFactory.getLog(FichaInformeForm.class.getName());
    FichaInformeForm informesForm = (FichaInformeForm) session.getAttribute("FichaInformeForm");
    m_Log.debug("VERIFICACION *******: " + informesForm.getVerificacion());
    out.println(informesForm.toJSONString());
%>
