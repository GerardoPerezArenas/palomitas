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
  var frame;
  frame=parent.mainFrame;

  function cargarListaMunicipios(){
    <%	
		  Vector listaMunicipios = mantForm.getListaMunicipios();
      int lengthMuns = listaMunicipios.size();
    	int i = 0;
      String codMunicipios="";
      String descMunicipios="";
	  if(lengthMuns >0) {
	      for(i=0;i<lengthMuns-1;i++){
	        GeneralValueObject municipios = (GeneralValueObject)listaMunicipios.get(i);
	        codMunicipios+="\""+(String)municipios.getAtributo("codMunicipio")+"\",";
	        descMunicipios+="\""+(String)municipios.getAtributo("nombreOficial")+"\",";
	      }
	      GeneralValueObject municipios = (GeneralValueObject)listaMunicipios.get(i);
	      codMunicipios+="\""+(String)municipios.getAtributo("codMunicipio")+"\"";
	      descMunicipios+="\""+(String)municipios.getAtributo("nombreOficial")+"\"";
	  }
      %>
      frame.codMunicipios = [<%=codMunicipios%>];
      frame.descMunicipios = [<%=descMunicipios%>];
      frame.cargarComboBox(frame.codMunicipios,frame.descMunicipios);
  }    

  function cargarListaDistritos(){
    <%	
			Vector listaDistritos = mantForm.getListaDistritos();
      int lengthDis = listaDistritos.size();
      String codDistritos="";
      String descDistritos="";
	  if(lengthDis>0) {
	      for(i=0;i<lengthDis-1;i++){
	        GeneralValueObject distritos = (GeneralValueObject)listaDistritos.get(i);
	        codDistritos+="\""+distritos.getAtributo("codDistrito")+"\",";
	        descDistritos+="\""+distritos.getAtributo("descDistrito")+"\",";
	      }
	      GeneralValueObject distritos = (GeneralValueObject)listaDistritos.get(i);
	      codDistritos+="\""+distritos.getAtributo("codDistrito")+"\"";
	      descDistritos+="\""+distritos.getAtributo("descDistrito")+"\"";
	  }
      %>
    frame.codDistritos = [<%=codDistritos%>];
    frame.descDistritos = [<%=descDistritos%>];
    frame.cargarComboBox(frame.codDistritos,frame.descDistritos);
  }

  function cargarListaSecciones(){
    <%	
			Vector listaSecciones = mantForm.getListaSecciones();
      int lengthSecciones = listaSecciones.size();
      String listaSeccionesOriginal = "";
      String lista = "";
	  if(lengthSecciones >0 ) {
	      for(i=0;i<lengthSecciones-1;i++){
	        GeneralValueObject seccion = (GeneralValueObject)listaSecciones.get(i);
	        listaSeccionesOriginal += "[\""+seccion.getAtributo("codPais")+"\","+// 0
	          "\""+seccion.getAtributo("codProvincia")+"\","+// 1
	          "\""+seccion.getAtributo("codMunicipio")+"\","+// 2
	          "\""+seccion.getAtributo("codDistrito")+"\","+// 3
	          "\""+seccion.getAtributo("codSeccion")+"\","+// 4
	          "\""+seccion.getAtributo("letraSeccion")+"\","+// 5
	          "\""+seccion.getAtributo("descSeccion")+"\","+// 6
	          "\""+seccion.getAtributo("nombreLargo")+"\","+// 7
	          "\""+seccion.getAtributo("idObjetoGrafico")+"\","+// 8
	          "\""+seccion.getAtributo("contadorHojas")+"\"],";// 9
	        lista += "[\""+seccion.getAtributo("codSeccion")+"\","+// 0
	          "\""+seccion.getAtributo("letraSeccion")+"\","+// 1
	          "\""+seccion.getAtributo("descSeccion")+"\","+// 2
	          "\""+seccion.getAtributo("nombreLargo")+"\","+// 3
	          "\""+seccion.getAtributo("idObjetoGrafico")+"\","+// 4
	          "\""+seccion.getAtributo("contadorHojas")+"\"],";// 5
	      }
	      GeneralValueObject seccion = (GeneralValueObject)listaSecciones.get(i);
	      listaSeccionesOriginal += "[\""+seccion.getAtributo("codPais")+"\","+// 0
	        "\""+seccion.getAtributo("codProvincia")+"\","+// 1
	        "\""+seccion.getAtributo("codMunicipio")+"\","+// 2
	        "\""+seccion.getAtributo("codDistrito")+"\","+// 3
	        "\""+seccion.getAtributo("codSeccion")+"\","+// 4
	        "\""+seccion.getAtributo("letraSeccion")+"\","+// 5
	        "\""+seccion.getAtributo("descSeccion")+"\","+// 6
	        "\""+seccion.getAtributo("nombreLargo")+"\","+// 7
	        "\""+seccion.getAtributo("idObjetoGrafico")+"\","+// 8
	        "\""+seccion.getAtributo("contadorHojas")+"\"]";// 9
	      lista += "[\""+seccion.getAtributo("codSeccion")+"\","+// 0
	        "\""+seccion.getAtributo("letraSeccion")+"\","+// 1
	        "\""+seccion.getAtributo("descSeccion")+"\","+// 2
	        "\""+seccion.getAtributo("nombreLargo")+"\","+// 3
	        "\""+seccion.getAtributo("idObjetoGrafico")+"\","+// 4
	        "\""+seccion.getAtributo("contadorHojas")+"\"]";// 5
	  }
      %>
    frame.listaSeccionesOriginal = [<%=listaSeccionesOriginal%>];
    frame.cargarListaSecciones([<%=lista%>]);    
  }
  
  function redirige(){
  if(opcion=="cargarMunicipios"){
    cargarListaMunicipios();
  }else if(opcion=="cargarDistritos"){
    cargarListaDistritos();
  }else{
    cargarListaSecciones();
  }
  }
</script>
</head>

<body onLoad="redirige();">

</body>
</html>
