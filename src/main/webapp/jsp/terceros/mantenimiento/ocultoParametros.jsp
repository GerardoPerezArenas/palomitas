<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<title>Oculto Mantenimiento Terceros</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
	<% 
    MantenimientosTercerosForm mantForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
  %>    
	//Variables para las listas de datos dinámicas (de Provincias y Municipios)
	var codProvincias = new Array();
	var descProvincias = new Array();
	var codMunicipios = new Array();
	var descMunicipios = new Array();
	//Variable para direccionamiento al frame
  var frame;
    

	function redireccionaFrame()
	{
	  var ventana = "<%=mantForm.getVentana()%>";
	  frame=(ventana=="true")?parent1.mainFrame1:parent.mainFrame;	
	}//de la funcion
	

  function cargarListaProvincias()
	{
    <%	
		  Vector listaProvincias = mantForm.getListaProvincias();
    %>
      var j=0;
    <%for(int i=0;i<listaProvincias.size();i++){%>
			codProvincias[j] = "<%=(String)((GeneralValueObject)listaProvincias.get(i)).getAtributo("codigo")%>";
			descProvincias[j] = "<%=(String)((GeneralValueObject)listaProvincias.get(i)).getAtributo("descripcion")%>";
			j++;
    <%}%>
    frame.codProvincias = codProvincias;
    frame.descProvincias = descProvincias;
    frame.cargarComboBox(codProvincias,descProvincias);
  }//de la funcion


  function cargarListaMunicipios()
	{
    <%	
		  Vector listaMunicipios = mantForm.getListaMunicipios();
    %>
      var j=0;
    <%for(int i=0;i<listaMunicipios.size();i++){%>
			codMunicipios[j] = "<%=(String)((GeneralValueObject)listaMunicipios.get(i)).getAtributo("codMunicipio")%>";
			descMunicipios[j] = "<%=(String)((GeneralValueObject)listaMunicipios.get(i)).getAtributo("nombreOficial")%>";
			j++;
    <%}%>
    frame.codMunicipios = codMunicipios;
    frame.descMunicipios = descMunicipios;
    frame.cargarComboBox(codMunicipios,descMunicipios);
  }//de la funcion

	
	function redireccionaOpcion()
	{
		redireccionaFrame();
		var opcion = "<%=request.getParameter("opcion")%>";
		if (opcion=="cargarMunicipios")
		{
			cargarListaMunicipios(); //Cargamos los Municipios			
		}
		else if (opcion=="cargarProvincias")
		{
			cargarListaProvincias(); //Cargamos las Provincias			
		}
	}//de la funcion	
</script>
</head>

<body onLoad="redireccionaOpcion();">

</body>
</html>
