<html>
<head>
<%  String nombre = "";
    String tipoFichero = "";
    String directorio ="";
    String opcion="";
    nombre = request.getParameter("nombre");
    tipoFichero = request.getParameter("tipoFichero");
    directorio = request.getParameter("dir"); // Directorio en el que se almacenan los pdf generados para el catálogo
    opcion = request.getParameter("opcion"); 
    
    %>
<script type="text/javascript">
var APP_CONTEXT_PATH="<%=request.getContextPath()%>";
var nombre = '<%=nombre%>';
var tipoFichero = '<%=tipoFichero%>';
var directorio = '<%=directorio%>';
var opcion = '<%=opcion%>';
if ((tipoFichero==null) || (tipoFichero=='null') || (tipoFichero=='')) {
    tipoFichero = 'pdf';
}
function redirecciona(){
  window.focus();
  
  if(opcion=='ODT')  tipoFichero='odt';
     
  if (tipoFichero=='html') {
    // Es imprescindible abrir en una ventana nueva sin frames para poder imprimir 
    // bien el cuño en la Epson TM-295 (aunque esto se hace ya en altaRE.jsp, aqui
    // es necesario especificar el target correcto.
    document.forms[0].target="_self";

  }else {      
    document.forms[0].target="mainFrame";
  } 
  document.forms[0].nombre.value = nombre;
  document.forms[0].tipoFichero.value = tipoFichero;
  document.forms[0].directorio.value = directorio;
  document.forms[0].action="<%=request.getContextPath()%>/VerPDF";
  document.forms[0].submit();  
}
</script>
</head>
<body onLoad="redirecciona();">
<form>
<input type="hidden" name="nombre">
<input type="hidden" name="tipoFichero">
<input type="hidden" name="directorio">
</form>
</body>
</html>
