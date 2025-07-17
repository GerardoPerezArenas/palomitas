<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<title>Oculto Mantenimiento Municipios</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
	<% 
    MantenimientosTercerosForm mantForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
  %>    
  var ventana = "<%=mantForm.getVentana()%>";
	var opcion = "<%=request.getParameter("opcion")%>";
	var listaMunicipios = new Array();
  var lista = new Array();
  var frame;
  frame=(ventana=="true")?parent1.mainFrame1:parent.mainFrame;
    

  function cargarListaMunicipios(){
    <%	
      Vector listaMuns = mantForm.getListaMunicipios();
      String listaMunicipios = "";
      String lista = "";
      int i=0;
      for(i=0;i<listaMuns.size()-1;i++){
        GeneralValueObject municipio = (GeneralValueObject)listaMuns.get(i);
        listaMunicipios += "[\""+(String)municipio.getAtributo("codPais")+"\","+
          "\""+(String)municipio.getAtributo("codProvincia")+"\","+
          "\""+(String)municipio.getAtributo("codMunicipio")+"\","+
          "\""+(String)municipio.getAtributo("partidoJudicial")+"\","+
          "\""+(String)municipio.getAtributo("comarca")+"\","+
          "\""+(String)municipio.getAtributo("nombreOficial")+"\","+
          "\""+(String)municipio.getAtributo("nombreLargo")+"\","+
          "\""+(String)municipio.getAtributo("digitoControl")+"\","+
          "\""+(String)municipio.getAtributo("superficie")+"\","+
          "\""+(String)municipio.getAtributo("altitud")+"\","+
          "\""+(String)municipio.getAtributo("kmtsACapital")+"\","+
          "\""+(String)municipio.getAtributo("latitudNorte")+"\","+
          "\""+(String)municipio.getAtributo("latitudSur")+"\","+
          "\""+(String)municipio.getAtributo("longitudEste")+"\","+
          "\""+(String)municipio.getAtributo("longitudOeste")+"\","+
          "\""+(String)municipio.getAtributo("situacion")+"\"],";
        lista += "[\""+(String)municipio.getAtributo("codMunicipio")+"\","+
          "\""+(String)municipio.getAtributo("nombreOficial")+"\","+
          "\""+(String)municipio.getAtributo("nombreLargo")+"\","+
          //"\""+(String)municipio.getAtributo("partidoJudicial")+"\","+
          "\""+(String)municipio.getAtributo("comarca")+"\","+
          "\""+(String)municipio.getAtributo("digitoControl")+"\","+
          "\""+(String)municipio.getAtributo("superficie")+"\","+
          "\""+(String)municipio.getAtributo("altitud")+"\","+
          "\""+(String)municipio.getAtributo("kmtsACapital")+"\","+
          "\""+(String)municipio.getAtributo("latitudNorte")+"\","+
          "\""+(String)municipio.getAtributo("latitudSur")+"\","+
          "\""+(String)municipio.getAtributo("longitudEste")+"\","+
          "\""+(String)municipio.getAtributo("longitudOeste")+"\"],";
      }
      if (listaMuns.size()!=0){
	      GeneralValueObject municipio = (GeneralValueObject)listaMuns.get(i);
	      listaMunicipios += "[\""+(String)municipio.getAtributo("codPais")+"\","+
	        "\""+(String)municipio.getAtributo("codProvincia")+"\","+
	        "\""+(String)municipio.getAtributo("codMunicipio")+"\","+
	        "\""+(String)municipio.getAtributo("partidoJudicial")+"\","+
	        "\""+(String)municipio.getAtributo("comarca")+"\","+
	        "\""+(String)municipio.getAtributo("nombreOficial")+"\","+
	        "\""+(String)municipio.getAtributo("nombreLargo")+"\","+
	        "\""+(String)municipio.getAtributo("digitoControl")+"\","+
	        "\""+(String)municipio.getAtributo("superficie")+"\","+
	        "\""+(String)municipio.getAtributo("altitud")+"\","+
	        "\""+(String)municipio.getAtributo("kmtsACapital")+"\","+
	        "\""+(String)municipio.getAtributo("latitudNorte")+"\","+
	        "\""+(String)municipio.getAtributo("latitudSur")+"\","+
	        "\""+(String)municipio.getAtributo("longitudEste")+"\","+
	        "\""+(String)municipio.getAtributo("longitudOeste")+"\","+
	        "\""+(String)municipio.getAtributo("situacion")+"\"]";
	      lista += "[\""+(String)municipio.getAtributo("codMunicipio")+"\","+
	        "\""+(String)municipio.getAtributo("nombreOficial")+"\","+
	        "\""+(String)municipio.getAtributo("nombreLargo")+"\","+
	        //"\""+(String)municipio.getAtributo("partidoJudicial")+"\","+
	        "\""+(String)municipio.getAtributo("comarca")+"\","+
	        "\""+(String)municipio.getAtributo("digitoControl")+"\","+
	        "\""+(String)municipio.getAtributo("superficie")+"\","+
	        "\""+(String)municipio.getAtributo("altitud")+"\","+
	        "\""+(String)municipio.getAtributo("kmtsACapital")+"\","+
	        "\""+(String)municipio.getAtributo("latitudNorte")+"\","+
	        "\""+(String)municipio.getAtributo("latitudSur")+"\","+
	        "\""+(String)municipio.getAtributo("longitudEste")+"\","+
	        "\""+(String)municipio.getAtributo("longitudOeste")+"\"]";
        }
      %>
    frame.listaMunicipiosOriginal = [<%=listaMunicipios%>];
    frame.cargarListaMunicipios([<%=lista%>]);    
  }
//  cargarListaMunicipios();
</script>
</head>

<body onLoad="cargarListaMunicipios();">

</body>
</html>
