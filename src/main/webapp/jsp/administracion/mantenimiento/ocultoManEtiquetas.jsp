<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<title>Oculto Mantenimiento Etiquetas</title>
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
	<% 
    MantenimientosAdminForm mantForm =(MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
  %>    
	//Variables para la lista de Etiquetas
	var listaEtiquetas = new Array();
	var listaTablaEtiquetas = new Array();
	//Variable para direccionamiento al frame
  var frame;
    

	function redireccionaFrame()
	{
	  var ventana = '<%=mantForm.getVentana()%>';
	  frame=(ventana=="true")?parent1.mainFrame1:parent.mainFrame;	
	}//de la funcion
	
	
  function cargarListaEtiquetas()
	{
    <%	
			Vector listaEtiquetas = mantForm.getListaEtiquetas();
    %>
    var j=0;
		var m=0;
    <%for(int i=0;i<listaEtiquetas.size();i++)
		  {
        GeneralValueObject etiqueta = (GeneralValueObject)listaEtiquetas.elementAt(i);
    %>
				var nombreTabla='<%=(String)etiqueta.getAtributo("codTabla")%>';
				if (nombreTabla!="")
				{
					listaEtiquetas[m] = ['<%=(String)etiqueta.getAtributo("codEtiq")%>',//0
						'<%=(String)etiqueta.getAtributo("nomEtiq")%>',//1
						'<%=(String)etiqueta.getAtributo("descEtiq")%>',//2
						'<%=(String)etiqueta.getAtributo("codAplic")%>',//3
						'<%=(String)etiqueta.getAtributo("descAplic")%>',//4
						'<%=(String)etiqueta.getAtributo("codTabla")%>',//5
						'<%=(String)etiqueta.getAtributo("descTabla")%>',//6
						'<%=(String)etiqueta.getAtributo("codCampo")%>',//7
						'<%=(String)etiqueta.getAtributo("descCampo")%>'];//8
					listaTablaEtiquetas[m] = ['<%=(String)etiqueta.getAtributo("codEtiq")%>',//0
						'<%=(String)etiqueta.getAtributo("nomEtiq")%>',//1
						'<%=(String)etiqueta.getAtributo("descEtiq")%>',//2
						'<%=(String)etiqueta.getAtributo("codAplic")%>',//3
						'<%=(String)etiqueta.getAtributo("codTabla")%>',//5
						'<%=(String)etiqueta.getAtributo("codCampo")%>'];//7
						m++;
				}
        j++;
    <%}%>
		//Reemplazamos la lista de Etiquetas de la jsp por la actual
    frame.listaEtiquetas = listaEtiquetas;
		//Cargamos la lista (tabla) de Etiquetas de la Tabla de la JSP con la lista actual
    frame.cargarListaEtiquetas(listaTablaEtiquetas);    
  }//de la funcion

	
	function redireccionaOpcion()
	{
		redireccionaFrame();
		var opcion = '<%=request.getParameter("opcion")%>';
		if(opcion=='cargarEtiquetas')
		{
			cargarListaEtiquetas();//Cargamos la lista de Etiquetas
		}
		
	}//de la funcion	
</script>
</head>

<body onLoad="redireccionaOpcion();">

</body>
</html>
