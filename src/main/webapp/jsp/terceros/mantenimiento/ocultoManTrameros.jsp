<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

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
  var ventana = "<%=mantForm.getVentana()%>";
	var opcion = "<%=request.getParameter("opcion")%>";
	var codMunicipios = new Array();
	var descMunicipios = new Array();
	var codTipoNumeraciones = new Array();
  var descTipoNumeraciones = new Array();
  var codDistritos = new Array();
  var descDistritos = new Array();
  var codSecciones = new Array();
  var letraSecciones = new Array();
  var codLetraSecciones = new Array();
  var descSecciones = new Array();
  var codSubSecciones = new Array();
  var descSubSecciones = new Array();
  var codManzanas = new Array();
  var descManzanas = new Array();
  var codPostales = new Array();
  var listaTramos = new Array();
  var lista = new Array();
  var frame;
  frame=parent.mainFrame;

  function cargarListaVias(){
    <%
      Vector listaVias = mantForm.getListaVias();
      int lengthVias = listaVias.size();
      String idVias="";
      String codVias="";
      String descVias="";
      String codECOESIVias="";
      for(int i=0;i<lengthVias;i++){
        GeneralValueObject vias = (GeneralValueObject)listaVias.get(i);
        idVias+="\""+(String)vias.getAtributo("idVia")+"\",";
        codVias+="\""+(String)vias.getAtributo("codVia")+"\",";
        descVias+="\""+(String)vias.getAtributo("descVia")+"\",";
        codECOESIVias+="[\""+(String)vias.getAtributo("codECO")+"\","+
          "\""+(String)vias.getAtributo("descECO")+"\","+
          "\""+(String)vias.getAtributo("codESI")+"\","+
          "\""+(String)vias.getAtributo("descESI")+"\","+
          "\""+(String)vias.getAtributo("codNUC")+"\","+
          "\""+(String)vias.getAtributo("descNUC")+"\"],";
      }
      %>
    frame.idVias = [<%=idVias%>];
    frame.codVias = [<%=codVias%>];
    frame.descVias = [<%=descVias%>];
    frame.codECOESIVias = [<%=codECOESIVias%>];
    frame.auxCombo = 'comboVia';
	frame.cargarComboBox(frame.codVias,frame.descVias);
  }

  function cargarListaEsis(){
    <%
      Vector listaESIs = mantForm.getListaEsis();
      int lengthESIs = listaESIs.size();
      String codESIs="";
      String descESIs="";
      String codECOESIs="";
      for(int i=0;i<lengthESIs;i++){
        GeneralValueObject esis = (GeneralValueObject)listaESIs.get(i);
        codESIs+="\""+(String)esis.getAtributo("codEntidadSingular")+"\",";
        descESIs+="\""+(String)esis.getAtributo("nombreOficial")+"\",";
        codECOESIs+="[\""+(String)esis.getAtributo("codEntidadColectiva")+"\","+
          "\""+(String)esis.getAtributo("descEntidadColectiva")+"\","+
          "\""+(String)esis.getAtributo("codEntidadSingular")+"\","+
          "\""+(String)esis.getAtributo("nombreOficial")+"\"],";
      }
    %>
    frame.codESIs = [<%=codESIs%>];
    frame.descESIs = [<%=descESIs%>];
    frame.codECOESIs = [<%=codECOESIs%>];
    frame.auxCombo = 'comboESI';
    frame.cargarComboBox(frame.codESIs,frame.descESIs);
  }

  function cargarListaNucleos(){
    <%
      Vector listaNUCs = mantForm.getListaNucleos();
      int lengthNUCs = listaNUCs.size();
      String codNUCs="";
      String descNUCs="";
      String codECOESINUCs="";
      for(int i=0;i<lengthNUCs;i++){
        GeneralValueObject nucs = (GeneralValueObject)listaNUCs.get(i);
        codNUCs+="\""+(String)nucs.getAtributo("codNUC")+"\",";
        descNUCs+="\""+(String)nucs.getAtributo("descNUC")+"\",";
        codECOESINUCs+="[\""+(String)nucs.getAtributo("codECO")+"\","+
          "\""+(String)nucs.getAtributo("descECO")+"\","+
          "\""+(String)nucs.getAtributo("codESI")+"\","+
          "\""+(String)nucs.getAtributo("descESI")+"\"],";
      }
    %>
    //frame.codNUCs = [<%=codNUCs%>];
    //frame.descNUCs = [<%=descNUCs%>];
    //frame.codECOESINUCs = [<%=codECOESINUCs%>];
    //frame.auxCombo = 'comboNUC';
	frame.codsNUCTramo = [<%=codNUCs%>];
    frame.descsNUCTramo = [<%=descNUCs%>];
	frame.codECOESINUCTramo = [<%=codECOESINUCs%>];
	frame.auxCombo = 'comboNUCTramo';
	var codNuc = new Array();
	var descNuc = new Array();
	codNuc = [<%=codNUCs%>];
	descNuc = [<%=descNUCs%>];
	frame.cargarNUC(codNuc,descNuc);
  }

  function cargarListaMunicipios(){
    <%
		  Vector listaMunicipios = mantForm.getListaMunicipios();
      int lengthMuns = listaMunicipios.size();
    	String codMunicipios="";
      String descMunicipios="";
      for(int i=0;i<lengthMuns;i++){
        GeneralValueObject municipios = (GeneralValueObject)listaMunicipios.get(i);
       if (i != 0) {
                codMunicipios+=",";
                descMunicipios+=",";
            }
        codMunicipios+="\""+(String)municipios.getAtributo("codMunicipio")+"\"";
        descMunicipios+="\""+(String)municipios.getAtributo("nombreOficial")+"\"";
      }
    %>
    frame.codMunicipios = [<%=codMunicipios%>];
    frame.descMunicipios = [<%=descMunicipios%>];
    frame.cargarComboBox(frame.codMunicipios,frame.descMunicipios);
  }

  function cargarListaTramos(){
    <%
			Vector listaTramos = mantForm.getListaTrameros();
      int lengthTramos = listaTramos.size();
    %>
    var j=0;
    <%for(int i=0;i<lengthTramos;i++){
        GeneralValueObject tramo = (GeneralValueObject)listaTramos.get(i);
    %>
        listaTramos[j] = ["<%=(String)tramo.getAtributo("codTipoNumeracion")%>",// 0
          "<%=(String)tramo.getAtributo("descTipoNumeracion")%>",// 1
          "<%=(String)tramo.getAtributo("codPais")%>",// 2
          "<%=(String)tramo.getAtributo("descPais")%>",// 3
          "<%=(String)tramo.getAtributo("codProvincia")%>",// 4
          "<%=(String)tramo.getAtributo("descProvincia")%>",// 5
          "<%=(String)tramo.getAtributo("codMunicipio")%>",// 6
          "<%=(String)tramo.getAtributo("descMunicipio")%>",// 7
          "<%=(String)tramo.getAtributo("idVia")%>",// 8
          "<%=(String)tramo.getAtributo("codVia")%>",// 9
          "<%=(String)tramo.getAtributo("descVia")%>",// 10
          "<%=(String)tramo.getAtributo("codTramo")%>",// 11
          "<%=(String)tramo.getAtributo("codDistrito")%>",// 12
          "<%=(String)tramo.getAtributo("descDistrito")%>",// 13
          "<%=(String)tramo.getAtributo("codSeccion")%>",// 14
          "<%=(String)tramo.getAtributo("descSeccion")%>",// 15
          "<%=(String)tramo.getAtributo("codSubSeccion")%>",// 16
          "<%=(String)tramo.getAtributo("descSubSeccion")%>",// 17
          "<%=(String)tramo.getAtributo("codManzana")%>",// 18
          "<%=(String)tramo.getAtributo("descManzana")%>",// 19
          "<%=(String)tramo.getAtributo("codECO")%>",// 20
          "<%=(String)tramo.getAtributo("descECO")%>",// 21
          "<%=(String)tramo.getAtributo("codESI")%>",// 22
          "<%=(String)tramo.getAtributo("descESI")%>",// 23
          "<%=(String)tramo.getAtributo("codNUC")%>",// 24
          "<%=(String)tramo.getAtributo("descNUC")%>",// 25
          "<%=(String)tramo.getAtributo("codPostal")%>",// 26
          "<%=(String)tramo.getAtributo("numDesde")%>",// 27
          "<%=(String)tramo.getAtributo("letraDesde")%>",// 28
          "<%=(String)tramo.getAtributo("numHasta")%>",// 29
          "<%=(String)tramo.getAtributo("letraHasta")%>", //30
		  "<%=(String)tramo.getAtributo("letraSeccion")%>",// 31
		  "<%=(String)tramo.getAtributo("situacion")%>"];// 32
        lista[j]=[listaTramos[j][27],listaTramos[j][28],listaTramos[j][29],
          listaTramos[j][30],listaTramos[j][13],listaTramos[j][15],
          listaTramos[j][17],listaTramos[j][26],
		  listaTramos[j][23],listaTramos[j][25],listaTramos[j][32]
		  ];
        j++;
    <%}%>
    frame.listaTramosOriginal = listaTramos;
    frame.cargarListaTramos(lista);
  }

  function cargarListaDistritos(){
    <%
			Vector listaDistritos = mantForm.getListaDistritos();
      int lengthDistritos = listaDistritos.size();
    %>
    var j=0;
    <%for(int i=0;i<lengthDistritos;i++){%>
        codDistritos[j] =
          "<%=(String)((GeneralValueObject)listaDistritos.get(i)).getAtributo("codDistrito")%>";
        descDistritos[j] =
          "<%=(String)((GeneralValueObject)listaDistritos.get(i)).getAtributo("descDistrito")%>";
        j++;
    <%}%>
    frame.codDistritos = codDistritos;
    frame.descDistritos = descDistritos;
    frame.cargarComboBox(codDistritos,descDistritos);
  }

	function cargarListaSecciones(){
    <%
			Vector listaSecciones = mantForm.getListaSecciones();
      int lengthSecciones = listaSecciones.size();
    %>
    var j=0;
    <%for(int i=0;i<lengthSecciones;i++){%>
        codSecciones[j] =
          "<%=(String)((GeneralValueObject)listaSecciones.get(i)).getAtributo("codSeccion")%>";
        letraSecciones[j] =
          "<%=(String)((GeneralValueObject)listaSecciones.get(i)).getAtributo("letraSeccion")%>";
        descSecciones[j] =
          "<%=(String)((GeneralValueObject)listaSecciones.get(i)).getAtributo("descSeccion")%>";
		if(letraSecciones[j] != null && letraSecciones[j] !="" && letraSecciones[j] != " ") {
		  codLetraSecciones[j] = [codSecciones[j]+letraSecciones[j]];
		} else {
		  codLetraSecciones[j] = [codSecciones[j]];
		}
        j++;
    <%}%>
	frame.auxCombo = 'comboSeccion1';
    frame.codSecciones = codSecciones;
    frame.letraSecciones = letraSecciones;
    frame.descSecciones = descSecciones;
	frame.cargarSeccion1(codLetraSecciones,descSecciones,codSecciones,letraSecciones);
  }

  function cargarListaSubSecciones(){
    <%
			Vector listaSubSecciones = mantForm.getListaSubSecciones();
      int lengthSubSecciones = listaSubSecciones.size();
    %>
    var j=0;
    <%for(int i=0;i<lengthSubSecciones;i++){%>
        codSubSecciones[j] =
          "<%=(String)((GeneralValueObject)listaSubSecciones.get(i)).getAtributo("codSubSeccion")%>";
        descSubSecciones[j] =
          "<%=(String)((GeneralValueObject)listaSubSecciones.get(i)).getAtributo("descSubSeccion")%>";
        j++;
    <%}%>
    frame.codSubSecciones = codSubSecciones;
    frame.descSubSecciones = descSubSecciones;
    frame.auxCombo = "comboSubSeccion";
	frame.cargarSubSecciones(codSubSecciones,descSubSecciones);
  }

  function cargarListaManzanas(){
    <%
			Vector listaManzanas = mantForm.getListaManzanas();
      int lengthManzanas = listaManzanas.size();
    %>
    var j=0;
    <%for(int i=0;i<lengthManzanas;i++){%>
        codManzanas[j] =
          "<%=(String)((GeneralValueObject)listaManzanas.get(i)).getAtributo("codManzana")%>";
        descManzanas[j] =
          "<%=(String)((GeneralValueObject)listaManzanas.get(i)).getAtributo("descManzana")%>";
        j++;
    <%}%>
    frame.codManzanas = codManzanas;
    frame.descManzanas = descManzanas;
    frame.auxCombo = "comboManzana";
	frame.cargarManzanas(codManzanas,descManzanas);

  }

  function cargarCodPostales(){
    <%
			Vector listaCodPostales = mantForm.getListaCodPostales();
      int lengthCodPostales = listaCodPostales.size();
    %>
    var j=0;
    <%for(int i=0;i<lengthCodPostales;i++){%>
        codPostales[j] ="<%=(String)((GeneralValueObject)listaCodPostales.get(i)).getAtributo("codPostal")%>";
        j++;
    <%}%>
    frame.codPostales = codPostales;
    frame.auxCombo = "comboPostal";
    frame.cargarComboBox(codPostales,codPostales);
  }

	function actualizarSeccion(){
		 if (frame.esperarListaSecc){
		 	frame.comboSeccion1.buscaCodigo(frame.document.forms[0].codSeccion.value+frame.document.forms[0].letraSeccion.value );
	 		frame.esperarListaSecc = false;
	 	}
	}
  if(opcion=="cargarVias"){
    cargarListaVias();
  }else if(opcion=="cargarDistritos"){
    cargarListaDistritos();
  }else if(opcion=="cargarSecciones"){
    cargarListaSecciones();
	//actualizarSeccion();
  }else if(opcion=="cargarSubSecciones"){
    cargarListaSubSecciones();
  }else if(opcion=="cargarManzanas"){
    cargarListaManzanas();
  }else if(opcion=="cargarCodPostales"){
    cargarCodPostales();
  }else if(opcion=="cargarMunicipios"){
    cargarListaMunicipios();
  }else if(opcion=="cargarEsis"){
    cargarListaEsis();
  }else if(opcion=="cargarNucleos"){
    cargarListaNucleos();
  }else if(opcion=="cargarListasBusqueda"){
    cargarListaVias();
    cargarListaEsis();
    cargarCodPostales();
  }else if(opcion=="cargarListasRejilla"){
    cargarListaSubSecciones();
    cargarListaManzanas();
  }else{
    cargarListaTramos();
  }
</script>
</head>

<body>

</body>
</html>
