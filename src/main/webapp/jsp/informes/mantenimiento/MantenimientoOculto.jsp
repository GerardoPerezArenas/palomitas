<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

    <jsp:include page="/jsp/informes/tpls/app-constants.jsp" />
</head>
<body>
<script type='text/javascript'>



if ( (top.mainFrame.textOperacion.value == 'M') || (top.mainFrame.textOperacion.value == 'ME'))
{
        top.mainFrame.callbackAceptarModificar();
}
else if (top.mainFrame.textOperacion.value == 'CE')
{
	<%
	String vector=null;
	 if  ( (request.getAttribute("ENTIDADE")!=null) && (!request.getAttribute("ENTIDADE").toString().trim().equals("")) )
	 	{
	 	vector=request.getAttribute("ENTIDADE").toString();
	 	
	 	} else vector="[]";
	%>
	var vEntidad=<%=vector%>;
	<% if  ( (request.getAttribute("CAMPOS")!=null) && (!request.getAttribute("CAMPOS").toString().trim().equals("")) )
	 	{
	 	vector=request.getAttribute("CAMPOS").toString();
	 	
	 	} else vector="[]";
	%>

	var vCampos=<%=vector%>;
	<% if  ( (request.getAttribute("APLICACIONES")!=null) && (!request.getAttribute("APLICACIONES").toString().trim().equals("")) )
	 	{
	 	vector=request.getAttribute("APLICACIONES").toString();
	 	
	 	} else vector="[]";
	%>
	
	var vAplicaciones=<%=vector%>;
	<% if  ( (request.getAttribute("SUBENTIDADES")!=null) && (!request.getAttribute("SUBENTIDADES").toString().trim().equals("")) )
	 	{
	 	vector=request.getAttribute("SUBENTIDADES").toString();
	 	
	 	} else vector="[]";
	%>

	var vSubEntidades=<%=vector%>;
	
	
	top.mainFrame.callbackCubreControlesEntidad(vEntidad,vCampos,vAplicaciones,vSubEntidades);
}
else if  ( (top.mainFrame.textOperacion.value == 'CLE')
		|| (top.mainFrame.textOperacion.value == 'CLEST') )
{	
	<% 
	
	 if  ( (request.getAttribute("vectorInformes")!=null) && (!request.getAttribute("vectorInformes").toString().trim().equals("")) )
	 	{
	 	vector=request.getAttribute("vectorInformes").toString();
	 	
	 	} else vector="[]";
	%>
	var vector=<%=vector%>;
	
	top.mainFrame.callbackCubreTablaInformes(vector);
}
else if (top.mainFrame.textOperacion.value == 'GEP')
{	
	<% 
	
	 if  ( (request.getAttribute("COD_INFORMEXERADOR")!=null) && (!request.getAttribute("COD_INFORMEXERADOR").toString().trim().equals("")) )
	 	{
	 	vector=request.getAttribute("COD_INFORMEXERADOR").toString();
	 	
	 	} else vector="Ok";
	%>
	var vector='<%=vector%>';
	
	top.mainFrame.callbackpulsarGuardar(vector);
}
else if ( (top.mainFrame.textOperacion.value == 'AE') || (top.mainFrame.textOperacion.value == 'A') )
{
	<%
	if  ( (request.getAttribute("COD_ENTIDADEINFORME")!=null) && (!request.getAttribute("COD_ENTIDADEINFORME").toString().trim().equals("")) )
		 	{
		 	vector=request.getAttribute("COD_ENTIDADEINFORME").toString();
		 	
		 	} else vector="[]";
		%>

	var codigo=<%=vector%>;
	
	top.mainFrame.callbackAceptarEngadir(codigo);
}
else if (top.mainFrame.textOperacion.value == 'I')
{
	<%
	if  ( (request.getAttribute("URL_XML")!=null) && (!request.getAttribute("URL_XML").toString().trim().equals("")) )
		 	{
		 	vector=request.getAttribute("URL_XML").toString();
		 	
		 	} else vector="Ok";
		%>

	var urlXML='<%=vector%>';
<%
	if  ( (request.getAttribute("URL_FDOT")!=null) && (!request.getAttribute("URL_FDOT").toString().trim().equals("")) )
		 	{
		 	vector=request.getAttribute("URL_FDOT").toString();
		 	
		 	} else vector="Ok";
		%>
	
	var urlDOC='<%=vector%>';
	<%
		if  ( (request.getAttribute("XML")!=null) && (!request.getAttribute("XML").toString().trim().equals("")) )
			 	{
			 	vector=request.getAttribute("XML").toString();
			 	
			 	} else vector="Ok";
			%>
	
	var XML='<%=vector%>';
	
	top.mainFrame.callbackVisualizaInforme(urlXML,urlDOC,XML);
}
else if ( (top.mainFrame.textOperacion.value == 'BE') || (top.mainFrame.textOperacion.value == 'B') )
{	
	top.mainFrame.callbackEliminarEntidad() ;
	
}else if (top.mainFrame.textOperacion.value == 'BV')
{	
	//var temp=<%=request.getAttribute("VALORES")%>;
	top.mainFrame.callbackBuscarValores(temp) ;
}else if (top.mainFrame.textOperacion.value == 'C')
{
	<% 
	 vector=null;
	 if  ( (request.getAttribute("vectorInforme")!=null) && (!request.getAttribute("vectorInforme").toString().trim().equals("")) )
	 	{
	 	vector=request.getAttribute("vectorInforme").toString();
	 	
	 	} else vector="[]";
	%>
	var vector=<%=vector%>;
	<%
	if  ( (request.getAttribute("vectorSubestructuras")!=null) && (!request.getAttribute("vectorSubestructuras").toString().trim().equals("")) )
		 	{
		 	vector=request.getAttribute("vectorSubestructuras").toString();
		 	
		 	} else vector="[]";
	%>
	var vector2=<%=vector%>;

	top.mainFrame.callbackCubreControlesInforme(vector,vector2);
} else alert('No entro por nada');




//top.oculto.location="about:blank";
</script>
</body>
</html>
