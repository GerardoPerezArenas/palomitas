<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> Oculto cargar pagina </title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<script>
function redirecciona(){
   parent.mainFrame.criterioBusquedaLimpio();
}
</script>

</head>
<body onLoad="redirecciona();">
</body>
</html>