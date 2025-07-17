<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="es.altia.agora.interfaces.user.web.sge.DocumentosExpedienteForm"%>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.editor.mantenimiento.DocumentosAplicacionForm"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.DocumentosExpedienteForm"%>
<%@page import="es.altia.common.service.config.*" %>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.util.struts.StrutsUtilOperations" %>
<%@page import="org.apache.commons.logging.Log" %>
<%@page import="org.apache.commons.logging.LogFactory" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.Map" %>

    
<%
      UsuarioValueObject usuarioVO = new UsuarioValueObject();
      GeneralValueObject paramsPadron = new GeneralValueObject();
      int idioma=1;
      DocumentosAplicacionForm daForm=null;
      DocumentosExpedienteForm daForm2=null;
      String[] params=null;
      String parametro0 = "";
    String parametro6 = "";
      if (session!=null)
      {
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        params = usuarioVO.getParamsCon();
        idioma =  usuarioVO.getIdioma();
        parametro0 = params[0];//oracle
        parametro6 = params[6];//jndi
        daForm =(DocumentosAplicacionForm)session.getAttribute("EditorDocumentosAplicacionForm");
        daForm2 =(DocumentosExpedienteForm)session.getAttribute("DocumentosExpedienteForm"); 
      }
      Map campos=new HashMap();

      campos=daForm2.getCampos();
          
     
     
%>


<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>Oculto Documentos de Expediente</title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<script>



function redireccionaOpcion(){

    var codDocumento ='<%=request.getAttribute("codDocumento")%>';
    parent.mainFrame.abrirODT(codDocumento);	
}
</script>
</head>
<body onload="redireccionaOpcion()">
</body>
</html>
