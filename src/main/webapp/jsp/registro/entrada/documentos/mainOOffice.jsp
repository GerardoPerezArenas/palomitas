<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.editor.mantenimiento.DocumentosAplicacionForm"%>
<%@page import="es.altia.common.service.config.*" %>
<%@page import="es.altia.util.struts.StrutsUtilOperations" %>

<html:html>
    <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <title>Edición de Documentos</title>
        <jsp:include page="/jsp/plantillas/Metas.jsp" />
        <%
                    UsuarioValueObject usuarioVO = new UsuarioValueObject();
                    int idioma = 1;
                    String[] params = null;
                    String parametro0 = "";
                    String parametro6 = "";

                    MantAnotacionRegistroForm registroForm = null;
                    String datos = "";
                    if (session != null) {
                        usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                        params = usuarioVO.getParamsCon();
                        idioma = usuarioVO.getIdioma();
                        parametro0 = params[0];//oracle
                        parametro6 = params[6];//jndi
                        registroForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
                        datos = registroForm.getDatosXML();
                        datos = datos.substring(datos.indexOf(">") + 1);
                    }

                    Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");
                    String hostVirtual = m_ConfigTechnical.getString("hostVirtual");
                    String servlet = "";
                    String urlApplet = "";
                    if ("".equals(hostVirtual)) {
                        servlet = "http://" + request.getHeader("Host") + request.getContextPath();
                        urlApplet = StrutsUtilOperations.getProtocol(request) + "://" + request.getHeader("Host") + request.getContextPath() + "/jsp/editor";
                    } else {
                        servlet = hostVirtual + request.getContextPath();
                        urlApplet = hostVirtual + request.getContextPath() + "/jsp/editor";
                    }
        %>

    </head>

    <body class="bandaBody">
        <applet name="appletOpenOffice" codebase='<%=urlApplet%>'
                code="es.altia.openOffice.AppletOpenOffice" archive="appletOOffice.jar"
                width="1250" height="600">
            <param name ="estado" value="justificante"/>
            <param name="idioma" value='<%=idioma%>'/>
            <param name="xml" value='<%=datos%>'/>
            <param name="urlServlet" value='<%=servlet%>'/>
            <param name="codPlantilla" value='<%=registroForm.getCodPlantilla()%>'/>
            <param name="parametro0" value='<%=parametro0%>'/>
            <param name="parametro6" value='<%=parametro6%>'/>
            <param name="scriptable" value="true"/>
        </applet>

    </body>
</html:html>

