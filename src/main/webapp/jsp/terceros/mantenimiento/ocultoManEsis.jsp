<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
<head>
<title>Oculto Mantenimiento Municipios</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<%
  int idioma=1;
  int apl=1;
  int munic = 0;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
          munic = usuario.getOrgCod();
        }
  }
%>


<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
	<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
	<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />


<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
	<% 
    MantenimientosTercerosForm mantForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
  %>    
	//Variables para las listas de Entidades Singulares
	var listaEntSingulares = new Array();
  var listaEntSingularesOrig = new Array();
  var listaEntSingularesCompleta = new Array();
	//Variables para las listas de datos dinámicas (de Municipios y Entidades colectivas)
	var codMunicipios = new Array();
	var descMunicipios = new Array();
	var codEntColectivas = new Array();
	var descEntColectivas = new Array();	
	//Variable para direccionamiento al frame
  var frame;
    

	function redireccionaFrame()
	{
	  var ventana = "<%=mantForm.getVentana()%>";
	  frame=(ventana=="true")?parent1.mainFrame1:parent.mainFrame;	
	}//de la funcion
	
	
  function cargarListaEntSingulares(){
    <%	
			Vector listaEntSingulares = mantForm.getListaEsis();
			if (listaEntSingulares == null) listaEntSingulares = new Vector();

      int i=0;
      String listaESIOriginal = "";
      String lista = "";	  
      for(i=0;i<listaEntSingulares.size()-1;i++){
        GeneralValueObject entSingular = (GeneralValueObject)listaEntSingulares.elementAt(i);
        listaESIOriginal += "[\""+(String)entSingular.getAtributo("codPais")+"\","+//0
          "\""+(String)entSingular.getAtributo("codProvincia")+"\","+//1
          "\""+(String)entSingular.getAtributo("codMunicipio")+"\","+//2
          "\""+(String)entSingular.getAtributo("codEntidadSingular")+"\","+//3
          "\""+(String)entSingular.getAtributo("codEntidadColectiva")+"\","+//4
          "\""+(String)entSingular.getAtributo("digitoControl")+"\","+//5
          "\""+(String)entSingular.getAtributo("ine")+"\","+//6
          "\""+(String)entSingular.getAtributo("nombreOficial")+"\","+//7
          "\""+(String)entSingular.getAtributo("nombreLargo")+"\","+//8
          "\""+(String)entSingular.getAtributo("kmtsACapital")+"\","+//9
          "\""+(String)entSingular.getAtributo("altitud")+"\","+//10
          "\""+(String)entSingular.getAtributo("imagen")+"\","+//11
          "\""+(String)entSingular.getAtributo("situacion")+"\","+//12
		  "\""+(String)entSingular.getAtributo("descEntidadColectiva")+"\"],";//13
        lista += "[\""+(String)entSingular.getAtributo("ine")+"\","+//6
		  "\""+(String)entSingular.getAtributo("digitoControl")+"\","+//5
          "\""+(String)entSingular.getAtributo("nombreOficial")+"\","+//7
          "\""+(String)entSingular.getAtributo("nombreLargo")+"\","+//8
          "\""+(String)entSingular.getAtributo("descEntidadColectiva")+"\","+//4
          "\""+(String)entSingular.getAtributo("situacion")+"\"],";//12
      }
	  if (listaEntSingulares.size() > 0) {
      GeneralValueObject entSingular = (GeneralValueObject)listaEntSingulares.elementAt(i);
      listaESIOriginal += "[\""+(String)entSingular.getAtributo("codPais")+"\","+//0
        "\""+(String)entSingular.getAtributo("codProvincia")+"\","+//1
        "\""+(String)entSingular.getAtributo("codMunicipio")+"\","+//2
        "\""+(String)entSingular.getAtributo("codEntidadSingular")+"\","+//3
        "\""+(String)entSingular.getAtributo("codEntidadColectiva")+"\","+//4
        "\""+(String)entSingular.getAtributo("digitoControl")+"\","+//5
        "\""+(String)entSingular.getAtributo("ine")+"\","+//6
        "\""+(String)entSingular.getAtributo("nombreOficial")+"\","+//7
        "\""+(String)entSingular.getAtributo("nombreLargo")+"\","+//8
        "\""+(String)entSingular.getAtributo("kmtsACapital")+"\","+//9
        "\""+(String)entSingular.getAtributo("altitud")+"\","+//10
        "\""+(String)entSingular.getAtributo("imagen")+"\","+//11
        "\""+(String)entSingular.getAtributo("situacion")+"\","+//12
        "\""+(String)entSingular.getAtributo("descEntidadColectiva")+"\"]";//13
      lista += "[\""+(String)entSingular.getAtributo("ine")+"\","+//6
		"\""+(String)entSingular.getAtributo("digitoControl")+"\","+//5
        "\""+(String)entSingular.getAtributo("nombreOficial")+"\","+//7
        "\""+(String)entSingular.getAtributo("nombreLargo")+"\","+//8
        "\""+(String)entSingular.getAtributo("descEntidadColectiva")+"\","+//4
        "\""+(String)entSingular.getAtributo("situacion")+"\"]";//12
		}
    %>
	
	<%	
			Vector listaEntSingularesCompleta = mantForm.getListaEsisCompleta();
			if (listaEntSingularesCompleta == null) listaEntSingularesCompleta = new Vector();
      int j=0;
      String listaESICompleta = "";
      for(j=0;j<listaEntSingularesCompleta.size()-1;j++){
        GeneralValueObject entSingular1 = (GeneralValueObject)listaEntSingularesCompleta.elementAt(j);
        listaESICompleta += "[\""+(String)entSingular1.getAtributo("codEntidadSingular")+"\","+
		"\""+(String)entSingular1.getAtributo("codEntidadColectiva")+"\","+//4
		"\""+(String)entSingular1.getAtributo("nombreOficial")+"\","+//7
        "\""+(String)entSingular1.getAtributo("digitoControl")+"\","+//5
        "\""+(String)entSingular1.getAtributo("ine")+"\"],";
      }
	if (listaEntSingularesCompleta.size() > 0) {
      GeneralValueObject entSingular1 = (GeneralValueObject)listaEntSingularesCompleta.elementAt(j);
      listaESICompleta += "[\""+(String)entSingular1.getAtributo("codEntidadSingular")+"\","+
		"\""+(String)entSingular1.getAtributo("codEntidadColectiva")+"\","+//4
		"\""+(String)entSingular1.getAtributo("nombreOficial")+"\","+//7
        "\""+(String)entSingular1.getAtributo("digitoControl")+"\","+//5
        "\""+(String)entSingular1.getAtributo("ine")+"\"]"//6
	  ;
	  }
    %>
	
    frame.listaEntSingularesOriginal = [<%=listaESIOriginal%>];
	frame.listaEntSingularesCompleta = [<%=listaESICompleta%>];
    frame.cargarListaEntSingulares([<%=lista%>]);
  }//de la funcion


  function cargarListaMunicipios(){
    <%	
		  Vector listaMunicipios = mantForm.getListaMunicipios();
      int lengthMuns = listaMunicipios.size();
    	String codMunicipios="";
      String descMunicipios="";
      for(i=0;i<lengthMuns-1;i++){
        GeneralValueObject municipios = (GeneralValueObject)listaMunicipios.get(i);
        codMunicipios+="\""+(String)municipios.getAtributo("codMunicipio")+"\",";
        descMunicipios+="\""+(String)municipios.getAtributo("nombreOficial")+"\",";
      }
      GeneralValueObject municipios = (GeneralValueObject)listaMunicipios.get(i);
      codMunicipios+="\""+(String)municipios.getAtributo("codMunicipio")+"\"";
      descMunicipios+="\""+(String)municipios.getAtributo("nombreOficial")+"\"";
      %>
      codMunicipios = [<%=codMunicipios%>];
      descMunicipios = [<%=descMunicipios%>];
      frame.codMunicipios = codMunicipios;
      frame.descMunicipios = descMunicipios;
      frame.inicializarValores("codMunicipio","descMunicipio",codMunicipios,descMunicipios);
  }//de la funcion


  function cargarListaEntColectivas()
	{
    <%	
		  Vector listaEntColectivas = mantForm.getListaEcos();
      int lengthEntColectivas = listaEntColectivas.size();
      String codEntColectivas="";
      String descEntColectivas="";
      for(i=0;i<lengthEntColectivas-1;i++){
        GeneralValueObject ecos = (GeneralValueObject)listaEntColectivas.get(i);
        codEntColectivas+="\""+(String)ecos.getAtributo("codECO")+"\",";
        descEntColectivas+="\""+(String)ecos.getAtributo("descECO")+"\",";
      }
      GeneralValueObject ecos = (GeneralValueObject)listaEntColectivas.get(i);
      codEntColectivas+="\""+(String)ecos.getAtributo("codECO")+"\"";
      descEntColectivas+="\""+(String)ecos.getAtributo("descECO")+"\"";
      %>
      codEntColectivas = [<%=codEntColectivas%>];
      descEntColectivas = [<%=descEntColectivas%>];
      frame.codEntColectivas = codEntColectivas;
      frame.descEntColectivas = descEntColectivas;
      //frame.inicializarValores("codEntidadColectiva","descEntidadColectiva",codEntColectivas,descEntColectivas);
  }//de la funcion
	
	function redireccionaOpcion()
	{
		redireccionaFrame();
		var opcion = "<%=mantForm.getOpcion()%>";
    var operacion = "<%=mantForm.getOperacion()%>";
		if (opcion=="cargarMunicipios")
		{
			cargarListaMunicipios(); //Cargamos los Municipios			
		}
		else if(opcion=="cargarEcos")
		{
			cargarListaEntColectivas();//Cargamos las Entidades Colectivas
		}else if(opcion=="modificarEsiTerritorio"){
      if(operacion=="SI"){
        jsp_alerta("A","<%=descriptor.getDescripcion("msjModRealizada")%>");
      }else if(operacion=="NO"){
        jsp_alerta("A","<%=descriptor.getDescripcion("msjModNonRealizada")%>");
      }
      cargarListaEntSingulares();
		}
		else  //Si el parametro opcion es vacio cargo la lista de Ent. Colectivas
		{
			cargarListaEntSingulares();//Cargamos las Entidades Singulares
		}//del if
	}//de la funcion	
</script>
</head>

<body onLoad="redireccionaOpcion();">

</body>
</html>
