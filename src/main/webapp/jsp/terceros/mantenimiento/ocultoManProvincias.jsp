<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Provincias</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script type="text/javascript">
		// VARIABLES GLOBALES
		var lista = new Array();
    var listaOriginal = new Array();
    var codAutonomias = new Array();
		var descAutonomias = new Array();
		
		// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
		function inicializar(){
			cargaTablaProvincias();
		}
		
		function cargaTablaProvincias(){
		<% 
      MantenimientosTercerosForm bForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
      Vector listaProvincias = bForm.getListaProvincias();
      Vector listaAutonomias = bForm.getListaAutonomias();
      int lengthAutonomias = listaAutonomias.size();
      int lengthProvincias = listaProvincias.size();
      int i = 0;
      String listaOriginal = "";
      String lista = "";
      for(i=0;i<lengthProvincias-1;i++){
        GeneralValueObject provincias = (GeneralValueObject)listaProvincias.get(i);
        listaOriginal+= "[\""+(String)provincias.getAtributo("codigo")+"\","+
          "\""+(String)provincias.getAtributo("codAutonomia")+"\","+
          "\""+(String)provincias.getAtributo("descAutonomia")+"\","+
          "\""+(String)provincias.getAtributo("descripcion")+"\","+
          "\""+(String)provincias.getAtributo("nombreLargo")+"\"],";
        lista+= "[\""+(String)provincias.getAtributo("codigo")+"\","+
          "\""+(String)provincias.getAtributo("descAutonomia")+"\","+
          "\""+(String)provincias.getAtributo("descripcion")+"\","+
          "\""+(String)provincias.getAtributo("nombreLargo")+"\"],";
      }
      if (lengthProvincias!=0){
	      GeneralValueObject provincias = (GeneralValueObject)listaProvincias.get(i);
	        listaOriginal+= "[\""+(String)provincias.getAtributo("codigo")+"\","+
	          "\""+(String)provincias.getAtributo("codAutonomia")+"\","+
	          "\""+(String)provincias.getAtributo("descAutonomia")+"\","+
	          "\""+(String)provincias.getAtributo("descripcion")+"\","+
	          "\""+(String)provincias.getAtributo("nombreLargo")+"\"]";
	        lista+= "[\""+(String)provincias.getAtributo("codigo")+"\","+
	          "\""+(String)provincias.getAtributo("descAutonomia")+"\","+
	          "\""+(String)provincias.getAtributo("descripcion")+"\","+
	          "\""+(String)provincias.getAtributo("nombreLargo")+"\"]";
      }
      String codAutonomias = "";
      String descAutonomias = "";
      for(i=0;i<lengthAutonomias-1;i++){
        GeneralValueObject autonomias = (GeneralValueObject)listaAutonomias.get(i);
        codAutonomias+="\""+(String)autonomias.getAtributo("codigo")+"\",";
        descAutonomias+="\""+(String)autonomias.getAtributo("descripcion")+"\",";
      }
      GeneralValueObject autonomias = (GeneralValueObject)listaAutonomias.get(i);
      codAutonomias+="\""+(String)autonomias.getAtributo("codigo")+"\"";
      descAutonomias+="\""+(String)autonomias.getAtributo("descripcion")+"\"";
      %>
      listaOriginal = [<%=listaOriginal%>];
      lista = [<%=lista%>];
      codAutonomias = [<%=codAutonomias%>];
      descAutonomias = [<%=descAutonomias%>];
      
      var datos = new Array();
      datos = [lista,listaOriginal,codAutonomias,descAutonomias];
			parent.mainFrame.recuperaDatos(datos);
		}

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
