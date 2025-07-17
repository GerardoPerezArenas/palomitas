<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<title>Oculto Mantenimiento Tipos de Documentos</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
	<% 
    MantenimientosTercerosForm mantForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
  %>    
	//Variables para la lista de Tipos de documentos
	var listaTipoDocs = new Array();
	//Variable para direccionamiento al frame
  var frame;
    

	function redireccionaFrame()
	{
	  var ventana = "<%=mantForm.getVentana()%>";
	  frame=(ventana=="true")?parent1.mainFrame1:parent.mainFrame;	
	}//de la funcion
	
	
  function cargarListaTipoDocs()
	{
    <%	
			Vector listaTipoDocs = mantForm.getListaTipoDocs();
    %>
    var j=0;
    <%for(int i=0;i<listaTipoDocs.size();i++)
		  {
        GeneralValueObject tipoDoc = (GeneralValueObject)listaTipoDocs.elementAt(i);
    %>
        listaTipoDocs[j] = ["<%=(String)tipoDoc.getAtributo("codTipoDoc")%>",//0
          "<%=(String)tipoDoc.getAtributo("codigoINE")%>",//1
          "<%=(String)tipoDoc.getAtributo("codigoAccede")%>",//2
          "<%=(String)tipoDoc.getAtributo("duplicado")%>",//3
          "<%=(String)tipoDoc.getAtributo("persFJ")%>",//4
          "<%=(String)tipoDoc.getAtributo("normalizado")%>",//5
          "<%=(String)tipoDoc.getAtributo("descTipoDoc")%>",//6
          "<%=(String)tipoDoc.getAtributo("grupo1")%>",//7
          "<%=(String)tipoDoc.getAtributo("tipo1")%>",//8
          "<%=(String)tipoDoc.getAtributo("grupo2")%>",//9
          "<%=(String)tipoDoc.getAtributo("tipo2")%>",//10
          "<%=(String)tipoDoc.getAtributo("grupo3")%>",//11
          "<%=(String)tipoDoc.getAtributo("tipo3")%>",//12
          "<%=(String)tipoDoc.getAtributo("grupo4")%>",//13
          "<%=(String)tipoDoc.getAtributo("tipo4")%>",//14
          "<%=(String)tipoDoc.getAtributo("grupo5")%>",//15
          "<%=(String)tipoDoc.getAtributo("tipo5")%>",//16
          "<%=(String)tipoDoc.getAtributo("longitudMaxima")%>",//17
					"<%=(String)tipoDoc.getAtributo("validacion")%>"];//18
        j++;
    <%}%>
		//Reemplazamos la lista de tipos e Documentos de la jsp por la actual
    frame.listaTipoDocs = listaTipoDocs;
		//Cargamos la lista de Tipos de Documentos de la Tabla de la JSP con la lista actual
    frame.cargarListaTipoDocs(listaTipoDocs);    
  }//de la funcion

	
	function redireccionaOpcion()
	{
		redireccionaFrame();
		var opcion = "<%=request.getParameter("opcion")%>";
		if(opcion=="cargarTiposDocs")
		{
			cargarListaTipoDocs();//Cargamos la lista de Tipos de Documentos
		}
		
	}//de la funcion	
</script>
</head>

<body onLoad="redireccionaOpcion();">

</body>
</html>
