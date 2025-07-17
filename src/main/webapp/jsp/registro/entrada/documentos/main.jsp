<!doctype html>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.editor.mantenimiento.DocumentosAplicacionForm"%>
<%@page import="es.altia.common.service.config.*" %>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.util.struts.StrutsUtilOperations" %>

<html:html>
 <head>
     <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE10"/>
     <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>Edición de Documentos</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%
      UsuarioValueObject usuarioVO = new UsuarioValueObject();
      GeneralValueObject paramsPadron = new GeneralValueObject();
      int idioma=1;
      DocumentosAplicacionForm daForm=null;
      String[] params=null;
      if (session!=null)
      {
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        params = usuarioVO.getParamsCon();
        idioma =  usuarioVO.getIdioma();
        daForm =(DocumentosAplicacionForm)session.getAttribute("EditorDocumentosAplicacionForm");
      }
      String aplicacion=request.getParameter("aplicacion");
      Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");
      String macroPlantillas = m_ConfigTechnical.getString("macroPlantillas");
      String hostVirtual = m_ConfigTechnical.getString("hostVirtual");

       String dominioFinal = "";
    if(hostVirtual==null || hostVirtual.length()==0) {
        dominioFinal = StrutsUtilOperations.getProtocol(request) +"://" + macroPlantillas + request.getContextPath();
    } else {
        dominioFinal = hostVirtual+ request.getContextPath();
    }
    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="6" />
    <!-- Ficheros JavaScript -->
    <script language="VBScript" src="<c:url value='/scripts/documentos.vbs'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
     <script type="text/javascript">

        function mensajeError(clave) {
            if (clave == 'ErrorMessages.Document.NotTemplate') {
                jsp_alerta('A', '<fmt:message key="ErrorMessages.Document.NotTemplate"/>');
            }
            if (clave == 'ErrorMessages.Document.NotOpen') {
                jsp_alerta('A', '<fmt:message key="ErrorMessages.Document.NotOpen"/>');
            }
            if (clave == 'ErrorMessages.Word.NotOpen') {
                jsp_alerta('A', '<fmt:message key="ErrorMessages.Word.NotOpen"/>');
            }
        }

    </script>

</head>

<body class="bandaBody" onload="Inicio()">
    <html:form enctype="multipart/form-data" action="/MantAnotacionRegistro.do" method="POST" target="oculto">
    <html:hidden property="datosXML"/>
    <html:hidden property="regNum"/>
    <html:hidden property="regUor"/>
    <html:hidden property="regTip"/>
     <html:hidden property="codPlantilla"/>

    <input type="Hidden" name="sessionID" value="<%=session.getId()%>">
    </html:form>
    <script type="text/vbscript" language="VBScript">
    Sub Inicio()
        Dim plantilla
        If Not(document.forms(0).codPlantilla.Value="") Then
                plantilla = "<%=dominioFinal%>/temp/documentos/<%=params[0]%>/<%=params[6]%>/Documento" & document.forms(0).codPlantilla.value & ".doc"
            End If
            Call ejecutaDocumentoJustificante(plantilla,document.forms(0).datosXML.Value,False,"<%= dominioFinal%>")

    End Sub


    </script>
 </body>
</html:html>

