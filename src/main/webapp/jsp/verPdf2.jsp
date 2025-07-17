<html>
<head>
<%  String nombre = "";
    nombre = request.getParameter("nombre"); %>
<script type="text/javascript">
var APP_CONTEXT_PATH="<%=request.getContextPath()%>";
var nombre = '<%=nombre%>';
function redirecciona(){
  window.focus();
  document.forms[0].nombre.value = nombre;
  document.forms[0].tipoFichero.value = "pdf";
  document.forms[0].action="<%=request.getContextPath()%>/VerPDF";
  document.forms[0].submit();
}
</script>
</head>
<body onLoad="redirecciona();">
<form>
<input type="hidden" name="nombre">
<input type="hidden" name="tipoFichero">
</form>
</body>
</html>
