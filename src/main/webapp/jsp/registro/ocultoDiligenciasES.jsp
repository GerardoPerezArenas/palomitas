<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<html>
<head>
<title> Diligencias Diarias </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
  var correcto = '<bean:write name="DiligenciasForm" property="correcto"/>';
	function buscar(){
  	var datos  = new Array();
  	datos[1] = '<bean:write name="DiligenciasForm" property="fecha"/>';
  	datos[2] = unescape('<bean:write name="DiligenciasForm" property="anotacion"/>');

  	parent.mainFrame.recuperaDatos(datos);
	}
	
	function grabar(){
    parent.mainFrame.grabado(correcto);
	}
	
	function borrar(){
    parent.mainFrame.borrado(correcto);
	}
	
	function registroCerrado(){
  	jsp_alerta("A","El registro está cerrado, no se puede editar la Diligencia");
	}
</script>

<%
  String opcion=request.getParameter("opcion");
  Log m_log = LogFactory.getLog(this.getClass().getName());
  if(m_log.isDebugEnabled()) m_log.debug("<"+opcion+">");
  if (opcion.equals("buscar")){%>
    <script>buscar()</script>
<%}else if (opcion.equals("grabar")){%>
    <script>grabar()</script>
<%}else if (opcion.equals("borrar")){%>
    <script>borrar()</script>
<%}else if (opcion.equals("registroCerrado")){%>
    <script>registroCerrado()</script>
<%}%>

</head>
<body>
</body>
</html>
