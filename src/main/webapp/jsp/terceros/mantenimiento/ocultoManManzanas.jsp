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

  function cargarListaSecciones(){
    <%	
	  Vector listaSecciones = mantForm.getListaSecciones();
	  int i=0;	
      int lengthSecciones = listaSecciones.size();
      String listaCod = "";
      String listaDesc = "";
	  String listaLetra = "";
	  String listaCodigo = "";
	  if(lengthSecciones >0) {
	      for(i=0;i<lengthSecciones-1;i++){
	        GeneralValueObject seccion = (GeneralValueObject)listaSecciones.get(i);
			String codSeccion = (String) seccion.getAtributo("codSeccion");
			String letraSeccion = (String) seccion.getAtributo("letraSeccion");
			String cod = "";
			if(letraSeccion != null && !letraSeccion.equals("") && !letraSeccion.equals(" ")) {
			  cod = codSeccion + letraSeccion;
			} else {
			  cod = codSeccion;
			}
	        listaCod += "[\""+cod+"\"],";
	        listaDesc += "[\""+seccion.getAtributo("descSeccion")+"\"],";
			listaLetra += "[\""+seccion.getAtributo("letraSeccion")+"\"],";
			listaCodigo += "[\""+seccion.getAtributo("codSeccion")+"\"],";
	      }
	      if (lengthSecciones!=0){
			  GeneralValueObject seccion = (GeneralValueObject)listaSecciones.get(i);
			  String codSeccion = (String) seccion.getAtributo("codSeccion");
			  String letraSeccion = (String) seccion.getAtributo("letraSeccion");
			  String cod = "";
			  if(letraSeccion != null && !letraSeccion.equals("") && !letraSeccion.equals(" ")) {
			    cod = codSeccion + letraSeccion;
			  } else {
			    cod = codSeccion;
			  }
		      listaCod += "[\""+cod+"\"]";
		      listaDesc += "[\""+seccion.getAtributo("descSeccion")+"\"]";
			  listaLetra += "[\""+seccion.getAtributo("letraSeccion")+"\"]";
			  listaCodigo += "[\""+seccion.getAtributo("codSeccion")+"\"]";
	       }
	  }
      %>
	  var lCod = new Array();
	  var lDesc = new Array();
	  var lCodigo = new Array();
	  var lLetra = new Array();
	  lCod = [<%=listaCod%>];
	  lDesc = [<%=listaDesc%>];
	  lCodigo = [<%=listaCodigo%>];
	  lLetra = [<%=listaLetra%>];
      parent.mainFrame.cargaListaSecciones(lCod,lDesc,lCodigo,lLetra);   
  }
  
  function cargarListaManzanas(){
    <%	
	  Vector listaManzanas = mantForm.getListaManzanas();
      String listaManzanasOriginal = "";
      String lista = "";
	  if(listaManzanas.size() >0 ) {
	      for(i=0;i<listaManzanas.size()-1;i++){
        GeneralValueObject manzSingular = (GeneralValueObject)listaManzanas.elementAt(i);
        listaManzanasOriginal += "[\""+(String)manzSingular.getAtributo("codPais")+"\","+//0
          "\""+(String)manzSingular.getAtributo("codProvincia")+"\","+//1
          "\""+(String)manzSingular.getAtributo("codMunicipio")+"\","+//2
          "\""+(String)manzSingular.getAtributo("codDistrito")+"\","+//3
          "\""+(String)manzSingular.getAtributo("codSeccion")+"\","+//4
          "\""+(String)manzSingular.getAtributo("letraSeccion")+"\","+//5
          "\""+(String)manzSingular.getAtributo("codManzana")+"\","+//6
          "\""+(String)manzSingular.getAtributo("descManzana")+"\"],";//7
        lista += "[\""+(String)manzSingular.getAtributo("codManzana")+"\","+//6
          "\""+(String)manzSingular.getAtributo("descManzana")+"\"],";//7
      }
	  }
    %>
	var listaOriginal = new Array();
	var lista = new Array();
	listaOriginal = [<%=listaManzanasOriginal%>];
	lista = [<%=lista%>];
    parent.mainFrame.cargaListaManzanas(listaOriginal,lista);
  }

  function noPuedeEliminar() {
    parent.mainFrame.noEliminarManz();
  }

  function redirige(){
	  if(operacion=="cargarSecciones"){
	    cargarListaSecciones();
	  } else if(operacion=="cargarManzanas") {
	    cargarListaManzanas();
	  } else if(operacion=="noPuedeEliminar") {
	    noPuedeEliminar();
	  }
  }
</script>
</head>

<body onLoad="redirige();">

</body>
</html>
