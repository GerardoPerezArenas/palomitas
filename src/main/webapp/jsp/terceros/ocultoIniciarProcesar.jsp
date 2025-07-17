<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>
<html>
<head>
<title>Oculto abrir ventana procesar</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
  var frame = parent.mainFrame;
  function abrirVentanaProcesar(){
    var source = "<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do?opcion=procesar";
    var ventana = 
     abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source,frame.datosProceso,
	'width=790,height=570,status='+ '<%=statusBar%>',function(){
                            frame.pulsarMostrar();
                    });
  }

  abrirVentanaProcesar();
</script>
</head>

<body>

</body>
</html>
