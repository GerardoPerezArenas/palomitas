<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%  
    String source = request.getParameter("source");
    String origen = request.getParameter("origen");
    String destino = request.getParameter("destino");
    String tipo = request.getParameter("tipo");
    String descDoc = request.getParameter("descDoc");
    String docu = request.getParameter("docu");
    String ventana = request.getParameter("ventana");
    String nombre = request.getParameter("nombre");
    String apell1 = request.getParameter("apell1");
    String apell2 = request.getParameter("apell2");
    String codTercero = request.getParameter("codTerc");
    String preguntaAlta = request.getParameter("preguntaAlta");
    if (nombre!=null) 
        nombre = nombre.replaceAll("[*]","");

    if (apell1!=null)
        apell1 = apell1.replaceAll("[*]","");
    if (apell2!=null) 
        apell2 = apell2.replaceAll("[*]","");
    String cadena = source+"&destino="+destino+"&tipo="+tipo+"&descDoc="+descDoc+"&docu="+docu+"&nombre="+nombre+
        "&apell1="+apell1+"&apell2="+apell2+"&ventana="+ventana+"&preguntaAlta="+preguntaAlta+"&codTerc="+codTercero+
        "&origen="+origen;
%>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>Terceros y Territorio</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">

 <!--[if lte IE 9]> 
  <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>"> 
  <![endif]--> 
  <jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
</head>
<body style="margin:0">
    <iframe name="oculto" src="about:blank" style="display:none"></iframe>
    <iframe src="about:blank" name="menu" style="display:none"></iframe>
    <iframe src="<%=cadena%>" name="mainFrame" class="iframeUnica" noresize scrolling="NO" frameborder="0" marginwidth="0" marginheight="0"></iframe>
    <SCRIPT type="text/javascript">
     
    </script>
    </body>
</html>
