<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page language="java" contentType="text/html" pageEncoding="ISO-8859-15"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Vector"%>

<html>
    <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-15">
        <title>Mantenimiento de firmas </title>
        <!-- Estilos -->
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 4;
            String estilo = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                estilo = usuarioVO.getCss();

            }

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
        %>
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=estilo%>">
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>

        <script type="text/javascript">
            function inicializar(){
                }

                function pulsarSalir(){
                    self.parent.opener.retornoXanelaAuxiliar();
                }


        </script>
    </head>

    <body class="bandaBody" onload="javascript:{pleaseWait('off');
        
            inicializar();}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<form  method="post">
    <html:hidden property="codMunicipio" value="" />
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titVerificarFirma")%></div>
    <div class="contenidoPantalla">
    <div  id="tabla" class="txtnegrobold">
        <c:choose>
            <c:when test="${NotificacionForm.firmaValida}">

                <table style="width:60%" class="txtnegrobold" border="0" cellpadding="2" cellspacing="2">
                    <tr>
                        <td colspan="2" >
                            <c:if test="${FichaExpedienteForm.verificacionFirmaComunicacion}">
                                <%=descriptor.getDescripcion("msgFirmaValidaDocCom")%>
                            </c:if>
                            <c:if test="${!FichaExpedienteForm.verificacionFirmaComunicacion}">
                                <%=descriptor.getDescripcion("msgFirmaValida")%>
                            </c:if>
                        </td>
                    </tr>                                                            
                    <tr>
                        <td colspan="2" >

                        </td>
                    </tr>

                    <c:if test="${NotificacionForm.datosCertificado ne null}">
                    <tr>
                        <td class="etiqueta" style="width:25%"><%=descriptor.getDescripcion("msgDatosCertificado")%></td>
                        <td class="etiqueta" style="width:75%"><strong><c:out value="${NotificacionForm.datosCertificado}"/></strong></td>
                    </tr>
                    </c:if>
                    <c:if test="${NotificacionForm.nifFirmante ne null}">
                     <tr>
                        <td class="etiqueta" style="width:25%"><%=descriptor.getDescripcion("msgNifFirmante")%></td>
                        <td class="etiqueta" style="width:75%"><strong><c:out value="${NotificacionForm.nifFirmante}"/></strong></td>
                    </tr>
                    </c:if>
                    <c:if test="${NotificacionForm.nombreFirmante ne null}">
                     <tr>
                        <td class="etiqueta" style="width:25%"><%=descriptor.getDescripcion("msgNombreFirmante")%></td>
                        <td class="etiqueta" style="width:75%"><strong><c:out value="${NotificacionForm.nombreFirmante}"/></strong></td>
                    </tr>
                    </c:if>
                    <c:if test="${NotificacionForm.emisorCertificado ne null}">
                     <tr>
                        <td class="etiqueta" style="width:25%"><%=descriptor.getDescripcion("msgEmisorCertificado")%></td>
                        <td class="etiqueta" style="width:75%"><strong><c:out value="${NotificacionForm.emisorCertificado}"/></strong></td>
                    </tr>
                    </c:if>
                    <c:if test="${NotificacionForm.validezCertificado ne null}">
                     <tr>
                        <td class="etiqueta" style="width:25%"><%=descriptor.getDescripcion("msgValidezCertificado")%></td>
                        <td class="etiqueta" style="width:75%"><strong><c:out value="${NotificacionForm.validezCertificado}"/></strong></td>
                    </tr>
                    </c:if>
                </table>
            </c:when>
            <c:otherwise>
                 <%=descriptor.getDescripcion("msgFirmaIncorrecta")%>

            </c:otherwise>
        </c:choose>
    </div>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>" name="botonSalir" onClick="pulsarSalir();" accesskey="S">
    </div>
</div>
</form>

</body>
</html>
