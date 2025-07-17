<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>
<html>
<head>
<title>Oculto abrir ventana domicilios</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
  function abrirVentanaVerDomicilios(){
    var source = "<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do?opcion=verDomicilios";
    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source,'',
	'width=790,height=480,status='+ '<%=statusBar%>',function(ventana){});
  }

  abrirVentanaVerDomicilios();
</script>
</head>

<body>

</body>
</html>
