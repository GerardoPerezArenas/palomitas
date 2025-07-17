<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<title>Oculto Mantenimiento Trameros</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
	<% 
    MantenimientosTercerosForm mantForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
  %>    
  var opcion = "<%=mantForm.getOpcion()%>";
  var operacion = "<%=mantForm.getOperacion()%>";

  function cargarListaPartJudiciales(){
    <%	
	  Vector listaPartJudiciales = mantForm.getListaPartJudiciales();
      String listaPartJudicialesOriginal = "";
      String lista = "";
	  int i=0;
	  if(listaPartJudiciales.size() >0 ) {
	      for(i=0;i<listaPartJudiciales.size()-1;i++){
	        GeneralValueObject partJudSingular = (GeneralValueObject)listaPartJudiciales.elementAt(i);
	        listaPartJudicialesOriginal += "[\""+(String)partJudSingular.getAtributo("codPais")+"\","+//0
			  "\""+(String)partJudSingular.getAtributo("codProvinicia")+"\","+//1
	          "\""+(String)partJudSingular.getAtributo("codPartJudicial")+"\","+//1
	          "\""+(String)partJudSingular.getAtributo("nombre")+"\","+//2
	          "\""+(String)partJudSingular.getAtributo("nombreLargo")+"\"],";//3
	        lista += "[\""+(String)partJudSingular.getAtributo("codPartJudicial")+"\","+//1
			  "\""+(String)partJudSingular.getAtributo("nombre")+"\","+//2
			  "\""+(String)partJudSingular.getAtributo("nombreLargo")+"\"],";//7
	      }
	      GeneralValueObject partJudSingular = (GeneralValueObject)listaPartJudiciales.elementAt(i);
	      listaPartJudicialesOriginal += "[\""+(String)partJudSingular.getAtributo("codPais")+"\","+//0
		    "\""+(String)partJudSingular.getAtributo("codProvinicia")+"\","+//1
	        "\""+(String)partJudSingular.getAtributo("codPartJudicial")+"\","+//1
	        "\""+(String)partJudSingular.getAtributo("nombre")+"\","+//2
	        "\""+(String)partJudSingular.getAtributo("nombreLargo")+"\"]";//3
	      lista += "[\""+(String)partJudSingular.getAtributo("codPartJudicial")+"\","+//1
		    "\""+(String)partJudSingular.getAtributo("nombre")+"\","+//2
		    "\""+(String)partJudSingular.getAtributo("nombreLargo")+"\"]";//7
	  }
    %>
	var listaOriginal = new Array();
	var lista = new Array();
	listaOriginal = [<%=listaPartJudicialesOriginal%>];
	lista = [<%=lista%>];
    parent.mainFrame.cargaListaPartJudiciales(listaOriginal,lista);   
  }
  
  function noPuedeEliminar() {
    parent.mainFrame.noEliminarPartJud();
  }
  
  function redirige(){
	  if(operacion=="cargarPartJudiciales"){
	    cargarListaPartJudiciales();
	  } else if(operacion=="noPuedeEliminar") {
	    noPuedeEliminar();
	  }
  }
</script>
</head>

<body onLoad="redirige();">

</body>
</html>
