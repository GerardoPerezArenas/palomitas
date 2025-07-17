<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<title>Oculto Mantenimiento de Plantas</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
	<% 
    MantenimientosTercerosForm mantForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
  %>    
	//Variables para la lista de  Organismos Externos
	var listaPlantas = new Array();
	//Variable para direccionamiento al frame
  var frame;
    

	function redireccionaFrame()
	{
	  var ventana = "<%=mantForm.getVentana()%>";
	  frame=(ventana=="true")?parent1.mainFrame1:parent.mainFrame;	
	}//de la funcion
	
	
  function cargarListaPlantas()
	{
    <%	
			Vector listaPlantas = mantForm.getListaPlantas();
    %>
    var j=0;
    <%for(int i=0;i<listaPlantas.size();i++)
		  {
        GeneralValueObject planta = (GeneralValueObject)listaPlantas.elementAt(i);
    %>
        listaPlantas[j] = ["<%=(String)planta.getAtributo("codigo")%>",//0
					"<%=(String)planta.getAtributo("descripcion")%>"];//17
        j++;
    <%}%>
		//Reemplazamos la lista de Organismos Externos de la jsp por la actual
    frame.listaPlantas = listaPlantas;
		//Cargamos la lista de Organismos Externos de la Tabla de la JSP con la lista actual
    frame.cargarListaPlantas(listaPlantas);    
  }//de la funcion

	
	function redireccionaOpcion()
	{
		redireccionaFrame();
		var opcion = "<%=request.getParameter("opcion")%>";
		if(opcion=="cargarPlantas")
		{
			cargarListaPlantas();//Cargamos la lista de Organismos Externos
		}
		
	}//de la funcion	
</script>
</head>

<body onLoad="redireccionaOpcion();">

</body>
</html>
