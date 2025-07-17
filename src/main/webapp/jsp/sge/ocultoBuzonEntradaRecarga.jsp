<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>Oculto Buzon de Entrada</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<script src="<c:url value='/scripts/general.js'/>"></script>
<script>

var cont = 0;	
var lista = new Array();
var lista_exp = new Array();
var lista_res = new Array();
var lista_ter = new Array();

<logic:iterate id="elemento" name="BuzonEntradaSGEForm" property="iniciados">
	var expediente = '<bean:write name="elemento" property="codProcedimiento"/>'+'.'+'<bean:write name="elemento" property="ejercicio"/>'+'.'+'<bean:write name="elemento" property="numero"/>';
	if('..' == expediente) expediente = '';
	lista[cont] = [	'<bean:write name="elemento" property="registro" />', '<bean:write name="elemento" property="solicitante"/>', 
					expediente, '<bean:write name="elemento" property="nomProcedimiento" />'];      
	lista_exp[cont] = [	'<bean:write name="elemento" property="codProcedimiento"/>', '<bean:write name="elemento" property="ejercicio"/>',
						'<bean:write name="elemento" property="numero"/>', '<bean:write name="elemento" property="codMunicipio"/>', 
						'<bean:write name="elemento" property="fechaInicio"/>'];
	lista_res[cont] = [	'<bean:write name="elemento" property="departamentoRes"/>', '<bean:write name="elemento" property="unidadOrgRes"/>',
						'<bean:write name="elemento" property="ejercicioRes"/>', '<bean:write name="elemento" property="numeroRes"/>', 
						'<bean:write name="elemento" property="tipoRes"/>'];
	lista_ter[cont] = ['<bean:write name="elemento" property="tercero" />', '<bean:write name="elemento" property="version"/>', '<bean:write name="elemento" property="idDomicilio"/>'];
	cont = cont + 1;
</logic:iterate>

parent.mainFrame.cargaTabla(lista, lista_exp, lista_res, lista_ter);
	
</script>
</head>

<BODY>		
</BODY>
</html:html>
