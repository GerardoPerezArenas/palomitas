<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<title>Oculto Padron</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
  <% 
      MantenimientosTercerosForm mantForm =
        (MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
  %>
  
  var opcion = '<%=mantForm.getOpcion()%>';
  var operacion = '<%=mantForm.getOperacion()%>';
  var frame;
  frame=parent.mainFrame;
    
  function cargarListaVias(){
    <% 
      Vector listaVias = mantForm.getListaVias();
      int lengthVias = listaVias.size();
      int i=0;
      String codVias="";
      String descVias="";
      String viasOriginal="";
      String codECOESIVias="";
        if(lengthVias>0){
        for(i=0;i<lengthVias-1;i++){
          GeneralValueObject vias = (GeneralValueObject)listaVias.get(i);
          codVias+="\""+(String)vias.getAtributo("codVia")+"\",";
          descVias+="\""+(String)vias.getAtributo("descVia")+"\",";
          viasOriginal+="[\""+(String)vias.getAtributo("idVia")+"\","+
            "\""+(String)vias.getAtributo("codTipoVia")+"\","+
            "\""+(String)vias.getAtributo("descTipoVia")+"\","+
		 "\""+vias.getAtributo("nombreCorto")+"\""+ "],";
		/* Cambio combo viales *			
          codECOESIVias+="[\""+(String)vias.getAtributo("codECO")+"\","+
            "\""+(String)vias.getAtributo("descECO")+"\","+
            "\""+(String)vias.getAtributo("codESI")+"\","+
            "\""+(String)vias.getAtributo("descESI")+"\","+
            "\""+(String)vias.getAtributo("codNUC")+"\","+
            "\""+(String)vias.getAtributo("descNUC")+"\"],";
		* Fin cambio combo viales */			
        }
        GeneralValueObject vias = (GeneralValueObject)listaVias.get(i);
        codVias+="\""+(String)vias.getAtributo("codVia")+"\"";
        descVias+="\""+(String)vias.getAtributo("descVia")+"\"";
        viasOriginal+="[\""+(String)vias.getAtributo("idVia")+"\","+
          "\""+(String)vias.getAtributo("codTipoVia")+"\","+
          "\""+(String)vias.getAtributo("descTipoVia")+"\","+
		 "\""+vias.getAtributo("nombreCorto")+"\""+ "]";
		/* Cambio combo viales *		  
        codECOESIVias+="[\""+(String)vias.getAtributo("codECO")+"\","+
          "\""+(String)vias.getAtributo("descECO")+"\","+
          "\""+(String)vias.getAtributo("codESI")+"\","+
          "\""+(String)vias.getAtributo("descESI")+"\","+
          "\""+(String)vias.getAtributo("codNUC")+"\","+
          "\""+(String)vias.getAtributo("descNUC")+"\"]";
		* Fin cambio combo viales */
      }
    %>
    frame.viasOriginal = [<%=viasOriginal%>];
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
      if(lengthESIs>0){
        for(i=0;i<lengthESIs-1;i++){
          GeneralValueObject esis = (GeneralValueObject)listaESIs.get(i);
          codESIs+="\""+esis.getAtributo("codEntidadSingular")+"\",";
          descESIs+="\""+esis.getAtributo("nombreOficial")+"\",";
          codECOESIs+="[\""+esis.getAtributo("codEntidadColectiva")+"\","+
            "\""+esis.getAtributo("descEntidadColectiva")+"\","+
            "\""+esis.getAtributo("codEntidadSingular")+"\","+
            "\""+esis.getAtributo("nombreOficial")+"\"],";
        }
        GeneralValueObject esis = (GeneralValueObject)listaESIs.get(i);
        codESIs+="\""+esis.getAtributo("codEntidadSingular")+"\"";
        descESIs+="\""+esis.getAtributo("nombreOficial")+"\"";
        codECOESIs+="[\""+esis.getAtributo("codEntidadColectiva")+"\","+
          "\""+esis.getAtributo("descEntidadColectiva")+"\","+
          "\""+esis.getAtributo("codEntidadSingular")+"\","+
          "\""+esis.getAtributo("nombreOficial")+"\"]";
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
      if(lengthNUCs>0){
        for(i=0;i<lengthNUCs-1;i++){
          GeneralValueObject nucs = (GeneralValueObject)listaNUCs.get(i);
          codNUCs+="\""+nucs.getAtributo("codNUC")+"\",";
          descNUCs+="\""+nucs.getAtributo("descNUC")+"\",";
          codECOESINUCs+="[\""+nucs.getAtributo("codECO")+"\","+
            "\""+nucs.getAtributo("descECO")+"\","+
            "\""+nucs.getAtributo("codESI")+"\","+
            "\""+nucs.getAtributo("descESI")+"\"],";
        }
        GeneralValueObject nucs = (GeneralValueObject)listaNUCs.get(i);
        codNUCs+="\""+nucs.getAtributo("codNUC")+"\"";
        descNUCs+="\""+nucs.getAtributo("descNUC")+"\"";
        codECOESINUCs+="[\""+nucs.getAtributo("codECO")+"\","+
          "\""+nucs.getAtributo("descECO")+"\","+
          "\""+nucs.getAtributo("codESI")+"\","+
          "\""+nucs.getAtributo("descESI")+"\"]";
      }
    %>
    frame.codNUCs = [<%=codNUCs%>];
    frame.descNUCs = [<%=descNUCs%>];
    frame.codECOESINUCs = [<%=codECOESINUCs%>];
    frame.auxCombo = 'comboNUC';
    frame.cargarComboBox(frame.codNUCs,frame.descNUCs);
  }

  function cargarListaNumeraciones(){
    <%  
      Vector listaNums = mantForm.getListaNumeraciones();
      int lengthNums = listaNums.size();
      String listaNumeraciones = "";
      String listaNueva = "";
      String nuevaListaNumeraciones = "";
      String lista = "";
      String idDSUAnterior = "";
      if(lengthNums>0){
        for(i=0;i<lengthNums-1;i++){
          GeneralValueObject nums = (GeneralValueObject)listaNums.get(i);
          listaNueva+="[\""+nums.getAtributo("codDSU")+"\","+
            "\""+nums.getAtributo("numDesde")+"\","+
            "\""+nums.getAtributo("letraDesde")+"\","+
            "\""+nums.getAtributo("numHasta")+"\","+
            "\""+nums.getAtributo("letraHasta")+"\",\"NO\","+
            "\""+nums.getAtributo("codTipoNumeracion")+"\"],";
          lista+="[\""+nums.getAtributo("numDesde")+"-"+
            nums.getAtributo("numHasta")+"\","+
            "\""+nums.getAtributo("letraDesde")+"-"+
            nums.getAtributo("letraHasta")+"\",\"\",\"\"],";
        }
        GeneralValueObject nums = (GeneralValueObject)listaNums.get(i);
        listaNueva+="[\""+nums.getAtributo("codDSU")+"\","+
          "\""+nums.getAtributo("numDesde")+"\","+
          "\""+nums.getAtributo("letraDesde")+"\","+
          "\""+nums.getAtributo("numHasta")+"\","+
          "\""+nums.getAtributo("letraHasta")+"\",\"NO\","+
          "\""+nums.getAtributo("codTipoNumeracion")+"\"]";
        lista+="[\""+nums.getAtributo("numDesde")+"-"+
          nums.getAtributo("numHasta")+"\","+
          "\""+nums.getAtributo("letraDesde")+"-"+
          nums.getAtributo("letraHasta")+"\",\"\",\"\"]";
      }
    %>
    frame.listaNueva = [<%=listaNueva%>];
    frame.nuevaListaNumeraciones = [<%=listaNueva%>];
    frame.lista = [<%=lista%>];
    frame.recuperaNumeraciones();
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
  
  if(opcion=="cargarVias"){
    cargarListaVias();
  }else if(opcion=="cargarListas"){
    cargarListaEsis();
    cargarListaNucleos();
    cargarListaVias();
  }else if(opcion=="cargarNumeraciones"){
    cargarListaTramos();
    cargarListaNumeraciones();
  }else{
    //recuperaDatosIniciales();
  }
  if(operacion=="SI"){
    jsp_alerta("A","Modificación realizada");
	frame.modificacionRealizada();
  }else if(operacion=="NO"){
    jsp_alerta("A","Modificación no realizada");
  }
</script>
</head>

<body>

</body>
</html>
