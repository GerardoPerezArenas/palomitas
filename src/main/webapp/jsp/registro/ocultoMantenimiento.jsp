<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<html>
<head>
<title> Diligencias Diarias </title>

<% 
	UsuarioValueObject usuarioVO = null;
		
   	if (session!=null){
    	usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");      	
	}
%>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/> 
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= usuarioVO.getIdioma()%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>

<script type="text/javascript">
	function redirigir(){
             		window.top.mainFrame.location = "<%=request.getContextPath()%>/jsp/registro/presentacionRegistroES.jsp";
	}	
<%
	String opcion=request.getParameter("opcion");	
	if (opcion.equals("guardar")){
%>
		jsp_alerta("A","<%= descriptor.getDescripcion("msjMant")%>");
		redirigir();
<%
	} else	if (opcion.equals("salir")){
%>
		redirigir();
<%
	} 
%>
</script>

</head>
<body>

</body>
</html>
