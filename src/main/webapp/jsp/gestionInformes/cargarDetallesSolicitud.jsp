<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="es.altia.agora.interfaces.user.web.gestionInformes.SolicitudesInformesForm" %>
<%@ page import="es.altia.agora.business.gestionInformes.SolicitudInformeValueObject" %>
<%--
  Created by IntelliJ IDEA.
  User: daniel.sambad
  Date: 07-feb-2007
  Time: 16:40:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    response.setHeader("Cache-control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", 0);

    Log m_Log = LogFactory.getLog(SolicitudesInformesForm.class.getName());
    SolicitudesInformesForm solicitudInformeForm = (SolicitudesInformesForm) session.getAttribute("SolicitudesInformesForm");
    SolicitudInformeValueObject informe = solicitudInformeForm.getSolicitudInforme();
    if (informe != null) {
        out.println(solicitudInformeForm.toJSONString());
        m_Log.debug("INFORME: " + solicitudInformeForm.toJSONString());
    }
%>
