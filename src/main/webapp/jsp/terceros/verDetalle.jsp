<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %> 
<%@page import="es.altia.agora.interfaces.user.web.terceros.FusionDivisionForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<html>
<head>
<title>CONSULTA DOMICILIOS POSTALES</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<meta	http-equiv="" content="text/html; charset=iso-8859-1">
<!-- Estilos -->
  <style>
  .xTabla {	cursor:'hand';table-layout:fixed;border-collapse:collapse;}	
  TABLE.xTabla TR	{ background-color:'#FFFFFF';	}
  TABLE.xTabla TD	{ color:'#000000';border-collapse:collapse; border:#CCCCCC 1px solid; font-family :	Verdana, Arial, Helvetica, sans-serif; font-size : 11px; font-weight : normal; }
  </style>
<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  int	idioma=1;
  int	apl=3;
  if (session.getAttribute("usuario") != null){	
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    apl = usuarioVO.getAppCod();
    idioma = usuarioVO.getIdioma();	
  }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>

  <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"	type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
  <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>"	/>
  <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<!-- Ficheros JavaScript -->    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
  <script type="text/javascript">
    // VARIABLES GLOBALES
    <%
	FusionDivisionForm fdForm =(FusionDivisionForm)session.getAttribute("FusionDivisionForm");
    %>
    var datosConsulta =	new Array();

    // FUNCION DE	INICIALIZACIÓN
    function inicializar(){
	pleaseWait1('off',top.mainFrame);
	cargaTablaVias();	
    }	

    function cargaTablaVias(){
	<%
	  GeneralValueObject datosVO = fdForm.getDatosVO();
	  Vector vias = (Vector)datosVO.getAtributo("vias");
	  int	lengthVias = vias.size();
	  int	i = 0;
	  String tabla = "<table width=\"100%\" border=\"0\" class=\"xTabla\" cellspacing=\"0\" cellpadding=\"3\">";
	  for(i=0;i<lengthVias;i++){
	    GeneralValueObject via = (GeneralValueObject)vias.get(i);
	    tabla+="<tr><td class=\"titulo\" bgcolor=\"#E6E6E6\">";	
	    tabla+="Vía :	"+via.getAtributo("codVia")+"	"+via.getAtributo("descVia");	
	    tabla+="</td><tr>";	
	    Vector numeraciones	= (Vector)via.getAtributo("numeraciones");
	    int lengthNum	= numeraciones.size();
	    int j=0;
	    for(j=0;j<lengthNum;j++){	
		GeneralValueObject numeracion	= (GeneralValueObject)numeraciones.get(j);
		int tipoNumeracion = Integer.parseInt((String)numeracion.getAtributo("tipoNumeracion"));
		String tipoNumer = "";
		if(tipoNumeracion==0){
		  tipoNumer	= "Sin Numeración";
		}else	if(tipoNumeracion==1){
		  tipoNumer	= "Numeración impar";
		}else	if(tipoNumeracion==2){
		  tipoNumer	= "Numeración par";
		}
		tabla+="<tr><td class=\"subtitulo\"><blockquote>";
		tabla+=tipoNumer;	
		tabla+="</blockquote></td><tr>";
		Vector tramos = (Vector)numeracion.getAtributo("tramos");
		int lengthTramos = tramos.size();
		int k=0;
		for(k=0;k<lengthTramos;k++){
		  GeneralValueObject tramo = (GeneralValueObject)tramos.get(k);
		  tabla+="<tr><td	class=\"subsubtitulo\"><blockquote><blockquote>";
		  tabla+="Tramo "+k+": "+tramo.getAtributo("numDesde")+"-"+tramo.getAtributo("letraDesde");
		  tabla+=" a "+tramo.getAtributo("numHasta")+"-"+tramo.getAtributo("letraHasta");
		  tabla+=" Habitantes: "+tramo.getAtributo("habitantes");
		  tabla+="</blockquote></blockquote></td><tr>";	
		}
	    }	
	  }
	  tabla+="</table>";
	%>
	var tabla =	document.getElementById("tablaVias");
	var stringTabla =	'<%=tabla%>';
	tabla.innerHTML =	stringTabla;
    }	

    // FUNCIONES DE PULSACIÓN	DE BOTONES
    function abrirInforme(nombre){	
		if (!(nombre =='')) {
			// PDFS NUEVA SITUACION
		    var sourc = "<%=request.getContextPath()%>/jsp/verPdf.jsp?opcion=null&nombre="+nombre;
		    ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp?source="+sourc,'ventanaInforme','width=800px,height=550px,status='+ '<%=statusBar%>' + ',toolbar=no');
		    ventanaInforme.focus();
		    // FIN PDFS NUEVA SITUACION
			
			//ventanaInforme = window.open("/jsp/registro/ver_pdf.jsp?fichero='/pdf/"+nombre+".pdf'", 
	        //  "informe", "width=800px,height=550px,toolbar=no");
		} else {
		  jsp_alerta('A','<%=descriptor.getDescripcion("msjNoPDF")%>');
		}	  
	}//de	la funcion

    function pulsarImprimir(){
	document.forms[0].opcion.value="imprimirVerDetalle";
	document.forms[0].target="oculto";
	document.forms[0].action="<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do";
	document.forms[0].submit();
    }	

    function pulsarCerrar(){
	self.close();
    }	
  </script>	
</head>

<body class="bandaBody" onLoad="inicializar();">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<form name="formulario" method="post" action="">
    <input  type="hidden" name="opcion">
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titDetViasTramProc")%></div>
    <div class="contenidoPantalla">
        <table border="0px" cellpadding="0px" cellspacing="0px" align="center">
            <tr>
                <td>
                    <div id="tablaVias" style="WIDTH:600px;	HEIGHT:300px;overflow-x:auto;overflow-y:auto;background-color:'#FFFFFF';"></div>
                </td>	
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input class="botonGeneral" name="botonImprimir" type="button" value="<%=descriptor.getDescripcion("gbImprimir")%>" accesskey="I" onclick="pulsarImprimir();">
            <input class="botonGeneral" name="botonCerrar"	type="button" value="<%=descriptor.getDescripcion("gbCerrar")%>" accesskey="C" onclick="pulsarCerrar();">
        </div>
    </div>
</form>
</body>
</html>
