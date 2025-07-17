<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<html>
<head>
<title> Apertura Rexistro de Saída</title>

<script src="<%=request.getContextPath()%>/scripts/general.js"></script>

<!-- Ficheros JavaScript -->

<script src="<%=request.getContextPath()%>/scripts/general.js"></script>

<%
	UsuarioValueObject usuarioVO = new UsuarioValueObject();
	int idioma=2;

	if (session.getAttribute("usuario") != null){
		usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
		idioma = usuarioVO.getIdioma();
	}

%>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="1" />
<script type="text/javascript">
function redirecciona(){
	var opcion = '<bean:write name="AperturaCierreRegistroForm" property="opcion"/>';
	var mnsx;
	var dia;
	var mes;
	var ano;
	var tipo="E";

	if (opcion == 'abrir_entrada')
		mnsx='<%=descriptor.getDescripcion("msj_abiertofRE")%>';

	else if (opcion == 'cerrar_entrada')
		mnsx='<%=descriptor.getDescripcion("msj_cerradofRE")%>';
	     else {
	     	  tipo="S";
	     	  if (opcion == 'abrir_salida')
		  	mnsx='<%=descriptor.getDescripcion("msj_abiertofRS")%>';
		  else if (opcion == 'cerrar_salida')
			   mnsx='<%=descriptor.getDescripcion("msj_cerradofRS")%>';
		  }

	if (opcion == 'abrir_entrada' || opcion == 'abrir_salida')	
	{
		dia = <bean:write name="AperturaCierreRegistroForm" property="txtDiaAbrir"/>;
		mes = <bean:write name="AperturaCierreRegistroForm" property="txtMesAbrir"/>;
		ano = <bean:write name="AperturaCierreRegistroForm" property="txtAnoAbrir"/>;		
		
	}
	else {
		dia = <bean:write name="AperturaCierreRegistroForm" property="txtDiaCerrar"/>;
		mes = <bean:write name="AperturaCierreRegistroForm" property="txtMesCerrar"/>;
		ano = <bean:write name="AperturaCierreRegistroForm" property="txtAnoCerrar"/>;
	}
	
	jsp_alerta("A",mnsx + " <b>" + dia + "/" + mes + "/" + ano + "</b>");

    	parent.mainFrame.location = "<%=request.getContextPath()%>/MantAnotacionRegistro.do?opcion="+tipo;
}
</script>
</head>
<body onLoad="redirecciona();">
<p>&nbsp;<p><center/>
</body>
</html>
