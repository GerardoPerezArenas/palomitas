<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<html>
<head>
<title>Oculto Mantenimiento Trameros</title>
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
	<% 
    MantenimientosTercerosForm mantForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
  %>    
  var opcion = "<%=mantForm.getOpcion()%>";
  var operacion = "<%=mantForm.getOperacion()%>";

  function cargarListaAutonomias(){
    <%	
	  Vector listaAutonomias = mantForm.getListaAutonomias();
      String listaAutonomiasOriginal = "";
      String lista = "";
	  int i=0;
	  if(listaAutonomias.size() >0 ) {
	      for(i=0;i<listaAutonomias.size()-1;i++){
	        GeneralValueObject autonSingular = (GeneralValueObject)listaAutonomias.elementAt(i);
	        listaAutonomiasOriginal += "[\""+(String)autonSingular.getAtributo("codPais")+"\","+//0
	          "\""+(String)autonSingular.getAtributo("codigo")+"\","+//1
	          "\""+(String)autonSingular.getAtributo("descripcion")+"\","+//2
	          "\""+(String)autonSingular.getAtributo("nombreLargo")+"\"],";//3
	        lista += "[\""+(String)autonSingular.getAtributo("codigo")+"\","+//1
			  "\""+(String)autonSingular.getAtributo("descripcion")+"\","+//2
			  "\""+(String)autonSingular.getAtributo("nombreLargo")+"\"],";//7
	      } 
	      if (listaAutonomias.size()!=0){
		      GeneralValueObject autonSingular = (GeneralValueObject)listaAutonomias.elementAt(i);
		      listaAutonomiasOriginal += "[\""+(String)autonSingular.getAtributo("codPais")+"\","+//0
		        "\""+(String)autonSingular.getAtributo("codigo")+"\","+//1
		        "\""+(String)autonSingular.getAtributo("descripcion")+"\","+//2
		        "\""+(String)autonSingular.getAtributo("nombreLargo")+"\"]";//3
		      lista += "[\""+(String)autonSingular.getAtributo("codigo")+"\","+//1
			    "\""+(String)autonSingular.getAtributo("descripcion")+"\","+//2
			    "\""+(String)autonSingular.getAtributo("nombreLargo")+"\"]";//7
		}
	  }
    %>
	var listaOriginal = new Array();
	var lista = new Array();
	listaOriginal = [<%=listaAutonomiasOriginal%>];
	lista = [<%=lista%>];
    parent.mainFrame.cargaListaAutonomias(listaOriginal,lista);   
  }
  
  function noPuedeEliminar() {
    parent.mainFrame.noEliminarAuton();
  }
  
  function redirige(){
	  if(operacion=="cargarAutonomias"){
	    cargarListaAutonomias();
	  } else if(operacion=="noPuedeEliminar") {
	    noPuedeEliminar();
	  }
  }
</script>
</head>

<body onLoad="redirige();">

</body>
</html>
