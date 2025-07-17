<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO"%>
<%@page import="java.util.Vector"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Cargar Unidades Organicas</title>

<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>

  <script type="text/javascript">
		// VARIABLES GLOBALES
    	var uors_dto = new Array();
		var uorcods = new Array();
		var uordescs = new Array();
		var cargoDTO = new Array();
		var codCargos = new Array();
		var descCargos = new Array();
	<% 
	//	 === UORs
	Vector listaUORDTOs = (Vector)request.getAttribute("listaUORs");
	if (listaUORDTOs == null) listaUORDTOs = new Vector();
	
	for (int j=0; j<listaUORDTOs.size(); j++) {
		UORDTO dto = (UORDTO)listaUORDTOs.get(j);%>
		// array con los objetos tipo uor mapeados por el array de arriba
		uors_dto[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
		// array con los códigos visibles
		uorcods[<%=j%>] = '<%=dto.getUor_cod_vis()%>';
		uordescs[<%=j%>] = '<%=StringEscapeUtils.escapeJavaScript(dto.getUor_nom())%>';
	<%}%>
	
	<%
	// === Cargos
	Vector listaCargos = (Vector)request.getAttribute("listaCargos");
	if (listaCargos == null) listaCargos = new Vector();
	
	for (int j=0; j<listaCargos.size(); j++) {
		UORDTO dto = (UORDTO)listaCargos.get(j);%>
		// array con los objetos tipo uor mapeados por el array de arriba
		cargoDTO[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
		// array con los códigos visibles
		codCargos[<%=j%>] = '<%=dto.getUor_cod_vis()%>';
		descCargos[<%=j%>] = '<%=dto.getUor_nom()%>';
	<%}%>
	
	// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
	function inicializar(){
			parent.mainFrame.cargarUnidOrgCargos(
					uors_dto, uorcods, uordescs, 
					cargoDTO, codCargos, descCargos);
	}

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
