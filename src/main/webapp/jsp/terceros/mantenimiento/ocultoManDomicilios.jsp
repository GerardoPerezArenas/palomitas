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
	var listaTramos = new Array();
  var lista = new Array();
  var frame;
  frame=parent.mainFrame;
    
  function cargarListaVias(){
    <% 
      Vector listaVias = mantForm.getListaVias();
      int i=0;
      int lengthVias = listaVias.size();
      String idVias="";
      String codVias="";
      String descVias="";
      String codECOESIVias="";
      for(i=0;i<lengthVias-1;i++){
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
      GeneralValueObject vias = (GeneralValueObject)listaVias.get(i);
      idVias+="\""+(String)vias.getAtributo("idVia")+"\"";
      codVias+="\""+(String)vias.getAtributo("codVia")+"\"";
      descVias+="\""+(String)vias.getAtributo("descVia")+"\"";
      codECOESIVias+="[\""+(String)vias.getAtributo("codECO")+"\","+
        "\""+(String)vias.getAtributo("descECO")+"\","+
        "\""+(String)vias.getAtributo("codESI")+"\","+
        "\""+(String)vias.getAtributo("descESI")+"\","+
        "\""+(String)vias.getAtributo("codNUC")+"\","+
        "\""+(String)vias.getAtributo("descNUC")+"\"]";
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
      for(i=0;i<lengthESIs-1;i++){
        GeneralValueObject esis = (GeneralValueObject)listaESIs.get(i);
        codESIs+="\""+(String)esis.getAtributo("codEntidadSingular")+"\",";
        descESIs+="\""+(String)esis.getAtributo("nombreOficial")+"\",";
        codECOESIs+="[\""+(String)esis.getAtributo("codEntidadColectiva")+"\","+
          "\""+(String)esis.getAtributo("descEntidadColectiva")+"\","+
          "\""+(String)esis.getAtributo("codEntidadSingular")+"\","+
          "\""+(String)esis.getAtributo("nombreOficial")+"\"],";
      }
      GeneralValueObject esis = (GeneralValueObject)listaESIs.get(i);
      codESIs+="\""+(String)esis.getAtributo("codEntidadSingular")+"\"";
      descESIs+="\""+(String)esis.getAtributo("nombreOficial")+"\"";
      codECOESIs+="[\""+(String)esis.getAtributo("codEntidadColectiva")+"\","+
        "\""+(String)esis.getAtributo("descEntidadColectiva")+"\","+
        "\""+(String)esis.getAtributo("codEntidadSingular")+"\","+
        "\""+(String)esis.getAtributo("nombreOficial")+"\"]";
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
      for(i=0;i<lengthNUCs-1;i++){
        GeneralValueObject nucs = (GeneralValueObject)listaNUCs.get(i);
        codNUCs+="\""+(String)nucs.getAtributo("codNUC")+"\",";
        descNUCs+="\""+(String)nucs.getAtributo("descNUC")+"\",";
        codECOESINUCs+="[\""+(String)nucs.getAtributo("codECO")+"\","+
          "\""+(String)nucs.getAtributo("descECO")+"\","+
          "\""+(String)nucs.getAtributo("codESI")+"\","+
          "\""+(String)nucs.getAtributo("descESI")+"\"],";
      }
      GeneralValueObject nucs = (GeneralValueObject)listaNUCs.get(i);
      codNUCs+="\""+(String)nucs.getAtributo("codNUC")+"\"";
      descNUCs+="\""+(String)nucs.getAtributo("descNUC")+"\"";
      codECOESINUCs+="[\""+(String)nucs.getAtributo("codECO")+"\","+
        "\""+(String)nucs.getAtributo("descECO")+"\","+
        "\""+(String)nucs.getAtributo("codESI")+"\","+
        "\""+(String)nucs.getAtributo("descESI")+"\"]";
    %>
    frame.codNUCs = [<%=codNUCs%>];
    frame.descNUCs = [<%=descNUCs%>];
    frame.codECOESINUCs = [<%=codECOESINUCs%>];
    frame.auxCombo = 'comboNUC';
    frame.cargarComboBox(frame.codNUCs,frame.descNUCs);
  }

  function cargarListaTramos(){
    <% 
      Vector listaTramos = mantForm.getListaTrameros();
      int lengthTramos = listaTramos.size();
      String tramosOriginal="";
      if(lengthTramos>0){
        for(i=0;i<lengthTramos-1;i++){
          GeneralValueObject tramo = (GeneralValueObject)listaTramos.get(i);
          tramosOriginal+="["+
            "\""+tramo.getAtributo("codTipoNumeracion")+"\","+// 0
            "\""+tramo.getAtributo("codPais")+"\","+// 1
            "\""+tramo.getAtributo("codProvincia")+"\","+// 2
            "\""+tramo.getAtributo("codMunicipio")+"\","+// 3
            "\""+tramo.getAtributo("idVia")+"\","+// 4
            "\""+tramo.getAtributo("codTramo")+"\","+// 5
            "\""+tramo.getAtributo("codDistrito")+"\","+// 6
            "\""+tramo.getAtributo("codSeccion")+"\","+// 7
            "\""+tramo.getAtributo("letraSeccion")+"\","+// 8
            "\""+tramo.getAtributo("numDesde")+"\","+// 9
            "\""+tramo.getAtributo("letraDesde")+"\","+// 10
            "\""+tramo.getAtributo("numHasta")+"\","+// 11
            "\""+tramo.getAtributo("letraHasta")+"\"],";// 12
        }
        GeneralValueObject tramo = (GeneralValueObject)listaTramos.get(i);
        tramosOriginal+="["+
          "\""+tramo.getAtributo("codTipoNumeracion")+"\","+// 0
          "\""+tramo.getAtributo("codPais")+"\","+// 1
          "\""+tramo.getAtributo("codProvincia")+"\","+// 2
          "\""+tramo.getAtributo("codMunicipio")+"\","+// 3
          "\""+tramo.getAtributo("idVia")+"\","+// 4
          "\""+tramo.getAtributo("codTramo")+"\","+// 5
          "\""+tramo.getAtributo("codDistrito")+"\","+// 6
          "\""+tramo.getAtributo("codSeccion")+"\","+// 7
          "\""+tramo.getAtributo("letraSeccion")+"\","+// 8
          "\""+tramo.getAtributo("numDesde")+"\","+// 9
          "\""+tramo.getAtributo("letraDesde")+"\","+// 10
          "\""+tramo.getAtributo("numHasta")+"\","+// 11
          "\""+tramo.getAtributo("letraHasta")+"\"]";// 12
      }
    %>
    frame.tramosOriginal = [<%=tramosOriginal%>];
  }
  
  function cargarListaDomicilios(){
    <% 
      Vector listaDomicilios = mantForm.getListaDomicilios();
      int lengthDomicilios = listaDomicilios.size();
      String domiciliosOriginal = "";
      String lista = "";
      if(lengthDomicilios>0){
        for(i=0;i<lengthDomicilios-1;i++){
          GeneralValueObject domicilio = (GeneralValueObject)listaDomicilios.get(i);
          domiciliosOriginal+="["+
            "\""+domicilio.getAtributo("codDPO")+"\","+
            "\""+domicilio.getAtributo("codDSU")+"\","+
            "\""+domicilio.getAtributo("numDesde")+"\","+
            "\""+domicilio.getAtributo("letraDesde")+"\","+
            "\""+domicilio.getAtributo("numHasta")+"\","+
            "\""+domicilio.getAtributo("letraHasta")+"\","+
            "\""+domicilio.getAtributo("bloque")+"\","+
            "\""+domicilio.getAtributo("portal")+"\","+
            "\""+domicilio.getAtributo("codEscalera")+"\","+
            "\""+domicilio.getAtributo("descEscalera")+"\","+
            "\""+domicilio.getAtributo("codPlanta")+"\","+
            "\""+domicilio.getAtributo("descPlanta")+"\","+
            "\""+domicilio.getAtributo("puerta")+"\","+
            "\""+domicilio.getAtributo("km")+"\","+
            "\""+domicilio.getAtributo("hm")+"\","+
            "\""+domicilio.getAtributo("codTipoNumeracion")+"\","+
            "\""+domicilio.getAtributo("descTipoNumeracion")+"\","+
            "\""+domicilio.getAtributo("codTipoVivienda")+"\","+
            "\""+domicilio.getAtributo("descTipoVivienda")+"\","+
            "\""+domicilio.getAtributo("codPostal")+"\","+
            "\""+domicilio.getAtributo("codTramo")+"\","+
            "\""+domicilio.getAtributo("tieneHabitantes")+"\","+
            "\""+domicilio.getAtributo("tieneTerceros")+"\","+
            "\""+domicilio.getAtributo("distrito")+"\","+
            "\""+domicilio.getAtributo("seccion")+"\","+
            "\""+domicilio.getAtributo("letra")+"\"],";
          lista+="["+
            "\""+domicilio.getAtributo("numDesde")+"\","+
            "\""+domicilio.getAtributo("letraDesde")+"\","+
            "\""+domicilio.getAtributo("numHasta")+"\","+
            "\""+domicilio.getAtributo("letraHasta")+"\","+
            "\""+domicilio.getAtributo("bloque")+"\","+
            "\""+domicilio.getAtributo("portal")+"\","+
            "\""+domicilio.getAtributo("codEscalera")+"\","+
            "\""+domicilio.getAtributo("descEscalera")+"\","+
            "\""+domicilio.getAtributo("codPlanta")+"\","+
            "\""+domicilio.getAtributo("descPlanta")+"\","+
            "\""+domicilio.getAtributo("puerta")+"\","+
            "\""+domicilio.getAtributo("km")+"\","+
            "\""+domicilio.getAtributo("hm")+"\","+
            "\""+domicilio.getAtributo("codTipoVivienda")+"\","+
            "\""+domicilio.getAtributo("descTipoVivienda")+"\","+
            "\""+domicilio.getAtributo("codPostal")+"\","+
            "\""+domicilio.getAtributo("codTipoNumeracion")+"\","+
            "\""+domicilio.getAtributo("descTipoNumeracion")+"\"],";
        }
        GeneralValueObject domicilio = (GeneralValueObject)listaDomicilios.get(i);
        domiciliosOriginal+="["+
          "\""+domicilio.getAtributo("codDPO")+"\","+
          "\""+domicilio.getAtributo("codDSU")+"\","+
          "\""+domicilio.getAtributo("numDesde")+"\","+
          "\""+domicilio.getAtributo("letraDesde")+"\","+
          "\""+domicilio.getAtributo("numHasta")+"\","+
          "\""+domicilio.getAtributo("letraHasta")+"\","+
          "\""+domicilio.getAtributo("bloque")+"\","+
          "\""+domicilio.getAtributo("portal")+"\","+
          "\""+domicilio.getAtributo("codEscalera")+"\","+
          "\""+domicilio.getAtributo("descEscalera")+"\","+
          "\""+domicilio.getAtributo("codPlanta")+"\","+
          "\""+domicilio.getAtributo("descPlanta")+"\","+
          "\""+domicilio.getAtributo("puerta")+"\","+
          "\""+domicilio.getAtributo("km")+"\","+
          "\""+domicilio.getAtributo("hm")+"\","+
          "\""+domicilio.getAtributo("codTipoNumeracion")+"\","+
          "\""+domicilio.getAtributo("descTipoNumeracion")+"\","+
          "\""+domicilio.getAtributo("codTipoVivienda")+"\","+
          "\""+domicilio.getAtributo("descTipoVivienda")+"\","+
          "\""+domicilio.getAtributo("codPostal")+"\","+
          "\""+domicilio.getAtributo("codTramo")+"\","+
          "\""+domicilio.getAtributo("tieneHabitantes")+"\","+
          "\""+domicilio.getAtributo("tieneTerceros")+"\","+
          "\""+domicilio.getAtributo("distrito")+"\","+
          "\""+domicilio.getAtributo("seccion")+"\","+
          "\""+domicilio.getAtributo("letra")+"\"]";
        lista+="["+
          "\""+domicilio.getAtributo("numDesde")+"\","+
          "\""+domicilio.getAtributo("letraDesde")+"\","+
          "\""+domicilio.getAtributo("numHasta")+"\","+
          "\""+domicilio.getAtributo("letraHasta")+"\","+
          "\""+domicilio.getAtributo("bloque")+"\","+
          "\""+domicilio.getAtributo("portal")+"\","+
          "\""+domicilio.getAtributo("codEscalera")+"\","+
          "\""+domicilio.getAtributo("descEscalera")+"\","+
          "\""+domicilio.getAtributo("codPlanta")+"\","+
          "\""+domicilio.getAtributo("descPlanta")+"\","+
          "\""+domicilio.getAtributo("puerta")+"\","+
          "\""+domicilio.getAtributo("km")+"\","+
          "\""+domicilio.getAtributo("hm")+"\","+
          "\""+domicilio.getAtributo("codTipoVivienda")+"\","+
          "\""+domicilio.getAtributo("descTipoVivienda")+"\","+
          "\""+domicilio.getAtributo("codPostal")+"\","+
          "\""+domicilio.getAtributo("codTipoNumeracion")+"\","+
          "\""+domicilio.getAtributo("descTipoNumeracion")+"\"]";
      }
    %>
    frame.listaDomiciliosOriginal = [<%=domiciliosOriginal%>];
    frame.cargarListaDomicilios([<%=lista%>]);
  }

  if(opcion=="cargarVias"){
    cargarListaVias();
  }else if(opcion=="cargarListas"){
    cargarListaEsis();
    cargarListaNucleos();
    cargarListaVias();
  }else{
    cargarListaTramos();
    cargarListaDomicilios();
  }
</script>
</head>

<body>

</body>
</html>
