<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>

<%@ page contentType="text/html;charset=ISO_8859-1"%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
  <title> Mantenimiento de Domicilios Normalizados </title>
  <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  ParametrosTerceroValueObject ptVO = null;
  int idioma=1;
  int apl=3;
  if (session.getAttribute("usuario") != null){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
    ptVO = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
  }
%>
  <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
  <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
  <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

  <!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
  <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
  <script type="text/javascript">
    // VARIABLES GLOBALES
    <% 
      MantenimientosTercerosForm mantForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
    %>  
    var codECOs = new Array();
    var descECOs = new Array();
    var codECOESIs = new Array();
    var codECOESIsOld = new Array();
    var codESIs = new Array();
    var descESIs = new Array();
    var codECOESINUCs = new Array();
    var codECOESINUCsOld = new Array();
    var codNUCs = new Array();
    var descNUCs = new Array();
    var codESIsOld = new Array();
    var descESIsOld = new Array();
    var codNUCsOld = new Array();
    var descNUCsOld = new Array();
    var idVias = new Array();
    var codVias = new Array();
    var descVias = new Array();
    var idViasOld = new Array();
    var codViasOld = new Array();
    var descViasOld = new Array();
    var codECOESIVias = new Array();
    var codECOESIViasOld = new Array();
    
    var codTipoNumeraciones = new Array();
    var descTipoNumeraciones = new Array();
    var codDistritos = new Array();
    var descDistritos = new Array();
    var codSecciones = new Array();
    var letraSecciones = new Array();
    var descSecciones = new Array();
    var codSubSecciones = new Array();
    var descSubSecciones = new Array();
    var codManzanas = new Array();
    var descManzanas = new Array();
    var codPostales = new Array();
    var listaDomiciliosOriginal = new Array();
    var listaDomicilios = new Array();
    var tramosOriginal = new Array();
    
    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS  
    function recuperaDatosIniciales(){
    <% 
      Vector listaECOs = mantForm.getListaEcos();
      Vector listaESIs = mantForm.getListaEsis();
      Vector listaNUCs = mantForm.getListaNucleos();
      Vector listaVias = mantForm.getListaVias();
      Vector listaCodPostales = mantForm.getListaCodPostales();
      Vector listaNumeraciones = mantForm.getListaNumeraciones();
      Vector listaTiposVivienda = mantForm.getListaUsoViviendas();
      Vector listaEscaleras = mantForm.getListaEscaleras();
      Vector listaPlantas = mantForm.getListaPlantas();
      int lengthECOs = listaECOs.size();
      int lengthESIs = listaESIs.size();
      int lengthNUCs = listaNUCs.size();
      int lengthVias = listaVias.size();
      int lengthCodPostales = listaCodPostales.size();
      int lengthNumeraciones = listaNumeraciones.size();
      int lengthTiposVivienda = listaTiposVivienda.size();
      int lengthEscaleras = listaEscaleras.size();
      int lengthPlantas = listaPlantas.size();
      int i = 0;
      String codEntColectivas="";
      String descEntColectivas="";
      for(i=0;i<lengthECOs-1;i++){
        GeneralValueObject ecos = (GeneralValueObject)listaECOs.get(i);
        codEntColectivas+="\""+ecos.getAtributo("codECO")+"\",";
        descEntColectivas+="\""+ecos.getAtributo("descECO")+"\",";
      }
      GeneralValueObject ecos = (GeneralValueObject)listaECOs.get(i);
      codEntColectivas+="\""+ecos.getAtributo("codECO")+"\"";
      descEntColectivas+="\""+ecos.getAtributo("descECO")+"\"";
      String codESIs="";
      String descESIs="";
      String codECOESIs="";
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
      String codNUCs="";
      String descNUCs="";
      String codECOESINUCs="";
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
      String idVias="";
      String codVias="";
      String descVias="";
      String codECOESIVias="";
      for(i=0;i<lengthVias-1;i++){
        GeneralValueObject vias = (GeneralValueObject)listaVias.get(i);
        idVias+="\""+vias.getAtributo("idVia")+"\",";
        codVias+="\""+vias.getAtributo("codVia")+"\",";
        descVias+="'"+vias.getAtributo("descVia")+"',";
        codECOESIVias+="[\""+vias.getAtributo("codECO")+"\","+
          "\""+vias.getAtributo("descECO")+"\","+
          "\""+vias.getAtributo("codESI")+"\","+
          "\""+vias.getAtributo("descESI")+"\","+
          "\""+vias.getAtributo("codNUC")+"\","+
          "\""+vias.getAtributo("descNUC")+"\"],";
      }
      GeneralValueObject vias = (GeneralValueObject)listaVias.get(i);
      idVias+="\""+vias.getAtributo("idVia")+"\"";
      codVias+="\""+vias.getAtributo("codVia")+"\"";
      descVias+="'"+vias.getAtributo("descVia")+"'";
      codECOESIVias+="[\""+vias.getAtributo("codECO")+"\","+
        "\""+vias.getAtributo("descECO")+"\","+
        "\""+vias.getAtributo("codESI")+"\","+
        "\""+vias.getAtributo("descESI")+"\","+
        "\""+vias.getAtributo("codNUC")+"\","+
        "\""+vias.getAtributo("descNUC")+"\"]";
      String codPostales="";
      for(i=0;i<lengthCodPostales-1;i++){
        GeneralValueObject codigosPostales = (GeneralValueObject)listaCodPostales.get(i);
        codPostales+="\""+codigosPostales.getAtributo("codPostal")+"\",";
      }
      GeneralValueObject codigosPostales = (GeneralValueObject)listaCodPostales.get(i);
      codPostales+="\""+codigosPostales.getAtributo("codPostal")+"\"";
      String codTipoNumeraciones="";
      String descTipoNumeraciones="";
      for(i=0;i<lengthNumeraciones-1;i++){
        GeneralValueObject numeraciones = (GeneralValueObject)listaNumeraciones.get(i);
        codTipoNumeraciones+="\""+numeraciones.getAtributo("codigo")+"\",";
        descTipoNumeraciones+="\""+numeraciones.getAtributo("descripcion")+"\",";
      }
      GeneralValueObject numeraciones = (GeneralValueObject)listaNumeraciones.get(i);
      codTipoNumeraciones+="\""+numeraciones.getAtributo("codigo")+"\"";
      descTipoNumeraciones+="\""+numeraciones.getAtributo("descripcion")+"\"";
      String codTiposVivienda="";
      String descTiposVivienda="";
      for(i=0;i<lengthTiposVivienda-1;i++){
        GeneralValueObject tiposVivienda = (GeneralValueObject)listaTiposVivienda.get(i);
        codTiposVivienda+="\""+tiposVivienda.getAtributo("codTipoVivienda")+"\",";
        descTiposVivienda+="\""+tiposVivienda.getAtributo("descTipoVivienda")+"\",";
      }
      GeneralValueObject tiposVivienda = (GeneralValueObject)listaTiposVivienda.get(i);
      codTiposVivienda+="\""+tiposVivienda.getAtributo("codTipoVivienda")+"\"";
      descTiposVivienda+="\""+tiposVivienda.getAtributo("descTipoVivienda")+"\"";
      String codEscaleras="";
      String descEscaleras="";
      for(i=0;i<lengthEscaleras-1;i++){
        GeneralValueObject escaleras = (GeneralValueObject)listaEscaleras.get(i);
        codEscaleras+="\""+escaleras.getAtributo("codigo")+"\",";
        descEscaleras+="\""+escaleras.getAtributo("descripcion")+"\",";
      }
      GeneralValueObject escaleras = (GeneralValueObject)listaEscaleras.get(i);
      codEscaleras+="\""+escaleras.getAtributo("codigo")+"\"";
      descEscaleras+="\""+escaleras.getAtributo("descripcion")+"\"";
      String codPlantas="";
      String descPlantas="";
      for(i=0;i<lengthPlantas-1;i++){
        GeneralValueObject plantas = (GeneralValueObject)listaPlantas.get(i);
        codPlantas+="\""+plantas.getAtributo("codigo")+"\",";
        descPlantas+="\""+plantas.getAtributo("descripcion")+"\",";
      }
      GeneralValueObject plantas = (GeneralValueObject)listaPlantas.get(i);
      codPlantas+="\""+plantas.getAtributo("codigo")+"\"";
      descPlantas+="\""+plantas.getAtributo("descripcion")+"\"";
      
    %>
      codECOs = [<%=codEntColectivas%>];
      descECOs = [<%=descEntColectivas%>];
      codESIs = [<%=codESIs%>];
      descESIs = [<%=descESIs%>];
      codECOESIs = [<%=codECOESIs%>];
      codECOESIsOld = [<%=codECOESIs%>];
      codECOESINUCs = [<%=codECOESINUCs%>];
      codECOESINUCsOld = [<%=codECOESINUCs%>];
      codNUCs = [<%=codNUCs%>];
      descNUCs = [<%=descNUCs%>];
      codESIsOld = codESIs;
      descESIsOld = descESIs;
      codNUCsOld = codNUCs;
      descNUCsOld = descNUCs;
      idVias = [<%=idVias%>];
      codVias = [<%=codVias%>];
      descVias = [<%=descVias%>];
      idViasOld = [<%=idVias%>];
      codViasOld = [<%=codVias%>];
      descViasOld = [<%=descVias%>];
      codECOESIVias = [<%=codECOESIVias%>];
      codECOESIViasOld = codECOESIVias;
      codPostales = [<%=codPostales%>];
      codTipoNumeraciones = [<%=codTipoNumeraciones%>];
      descTipoNumeraciones = [<%=descTipoNumeraciones%>];
      codTiposVivienda = [<%=codTiposVivienda%>];
      descTiposVivienda = [<%=descTiposVivienda%>];
      codEscaleras = [<%=codEscaleras%>];
      descEscaleras = [<%=descEscaleras%>];
      codPlantas = [<%=codPlantas%>];
      descPlantas = [<%=descPlantas%>];
    }
    
    function inicializar(){
      recuperaDatosIniciales();
      valoresPorDefecto();
      pulsarCancelarBuscar();
      pleaseWait1("off",top.mainFrame);
      
         
      
    }
    
    // FUNCIONES DE LIMPIEZA, HABILITACION Y DESHABILITACION DE CAMPOS
    var vectorCamposBusqueda = ["codECO","descECO","codESI","descESI","codNUC","descNUC","codVia","descVia"];
    var vectorCamposRejilla1 = ["numDesde","letraDesde","numHasta","letraHasta","bloque","portal",
      "puerta","km","hm"];
    var vectorCamposRejilla = ["numDesde","letraDesde","numHasta","letraHasta",
      "bloque","portal","codEscalera","descEscalera","codPlanta","descPlanta",
      "puerta","km","hm","codTipoVivienda","descTipoVivienda","descPostal",
      "codTipoNumeracion","descTipoNumeracion"];
    var vectorCombosBusqueda = ["comboECO","comboESI","comboNUC","comboVia"];
    var vectorCombosRejilla = ["comboTipoNumeracion","comboTipoVivienda","comboPostal","comboEscalera",
      "comboPlanta"];
    var vectorBotones = ["botonAlta","botonModificar","botonBorrar","botonLimpiar"];
    

    function habilitarCamposBusqueda(habilitar){
      habilitarGeneralCombos(vectorCombosBusqueda,habilitar);
    }

    function habilitarCamposRejilla(habilitar){
      habilitarGeneralInputs(vectorCamposRejilla1,habilitar);
      habilitarGeneralInputs(vectorBotones,habilitar);
      habilitarGeneralCombos(vectorCombosRejilla,habilitar);
    }

    function limpiarFormulario(){
      limpiarCamposBusqueda();
      limpiarCamposRejilla();
      tablaDomicilios.lineas = new Array();
      refresca(tablaDomicilios);
    }
    
    function limpiarCamposBusqueda(){
      limpiar(vectorCamposBusqueda);
    }
    
    function limpiarCamposRejilla(){
      limpiar(vectorCamposRejilla);
    }
    
    function limpiaVial(){
      var codVia = document.forms[0].codVia.value;
      if (codVia!=""){
        document.forms[0].descVia.value = "";
        document.forms[0].codVia.value = "";
      }
    }

    // FUNCIONES DE CARGA DE DATOS DINAMICA
    function cargarListas(){
      document.forms[0].opcion.value="cargarListas";
      document.forms[0].target="oculto";
      document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/DomiciliosNormalizados.do";
      document.forms[0].submit();
    }

    function cargarListaVias(){
      document.forms[0].opcion.value="cargarVias";
      document.forms[0].target="oculto";
      document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/DomiciliosNormalizados.do";
      document.forms[0].submit();
    }

    function cargarListaDomicilios(lista){
      listaDomicilios = lista;
      tablaDomicilios.lineas = lista;
      refresca(tablaDomicilios);
    }

    function valoresPorDefecto(){
      document.forms[0].codPais.value ="<%=ptVO.getPais()%>";
      document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
      document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
      document.forms[0].codMunicipio.value ="<%=ptVO.getMunicipio()%>";
      document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";
      codESIs = codESIsOld;
      descESIs = descESIsOld;
      codECOESIs = codECOESIsOld;
      codECOESINUCs = codECOESINUCsOld;
      codNUCs = codNUCsOld;
      descNUCs = descNUCsOld;
      idVias = idViasOld;
      codVias = codViasOld;
      descVias = descViasOld;
      codECOESIVias = codECOESIViasOld;
      comboECO.addItems(codECOs,descECOs);
      comboESI.addItems(codESIs,descESIs);
      comboNUC.addItems(codNUCs,descNUCs);
      comboVia.addItems(codVias,descVias);
      comboTipoNumeracion.addItems(codTipoNumeraciones,descTipoNumeraciones);
      comboPostal.addItems(codPostales,codPostales);
      comboTipoVivienda.addItems(codTiposVivienda,descTiposVivienda);
      comboEscalera.addItems(codEscaleras,descEscaleras);
      comboPlanta.addItems(codPlantas,descPlantas);
    }

    function buscarIdVia(){
      var cod = document.forms[0].codVia.value;
      var idVia = "";
      for(i=0;i<codVias.length;i++){
        if(codVias[i]==cod){
          idVia=idVias[i];
          break;
        }
      }
      document.forms[0].idVia.value=idVia;
    }
    
    function buscarLetraSeccion(cod){
      for(i=0;i<codSecciones.length;i++){
        if(codSecciones[i]==cod)
          return letraSecciones[i];
      }
      return "";
    }
    
    // FUNCIONES DE VALIDACION DE CAMPOS
    function validarCamposBusqueda(){
      var pais = document.forms[0].codPais.value;
      var provincia = document.forms[0].codProvincia.value;
      var municipio = document.forms[0].codMunicipio.value;
      var codVia = document.forms[0].codVia.value;
      if((pais!="")&&(provincia!="")&&(municipio!="")&&
         (codVia!=""))
        return true;
      return false;
    }

    function validarNumeracion(){
      var codTipoNumeracion = document.forms[0].codTipoNumeracion.value;
      var numDesde = document.forms[0].numDesde.value;
      if((codTipoNumeracion!="")&&((codTipoNumeracion=="0")||((codTipoNumeracion!="0")&&(numDesde!=""))))
        return true;
      return false;
    }
    
    function paridad(num1,num2){
      var res = true;
      if(num1%2!=num2%2)  res = false;
      return res;
    }

    function comprobarNumeros(){
      var tipNum = document.forms[0].codTipoNumeracion.value*1;
      var prNum = document.forms[0].numDesde.value;
      var ulNum = document.forms[0].numHasta.value;
      if(tipNum!=0){
        if(prNum){
          prNum = prNum*1;
          if((tipNum==1)&& esNumeroPar(prNum)){
            jsp_alerta('A','<%=descriptor.getDescripcion("Etiq_Paridad")%>');
            document.forms[0].numDesde.focus();
          }else if((tipNum==2)&& !esNumeroPar(prNum)){
            jsp_alerta('A','<%=descriptor.getDescripcion("Etiq_Paridad")%>');
            document.forms[0].numDesde.focus();
          }
          if(ulNum){
            if(!paridad(prNum,ulNum)){
              jsp_alerta('A','<%=descriptor.getDescripcion("Etiq_Paridad")%>');
              document.forms[0].numDesde.value = '';
              document.forms[0].numHasta.value = '';
              document.forms[0].numDesde.focus();
            }else{
              if(prNum.length!=0 && ulNum.length!=0){
                if(prNum*1>ulNum*1){
                  jsp_alerta('A','<%=descriptor.getDescripcion("Etiq_PrimMenorUlt")%>');
                  document.forms[0].numDesde.value = '';
                  document.forms[0].numHasta.value = '';
                  document.forms[0].numDesde.focus();
                }
              }
            }
          }
        }else{
          jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
        }
      }
    }
    
    function comprobarLetras(){
      var tipNum = document.forms[0].codTipoNumeracion.value*1;
      var prLet = document.forms[0].letraDesde.value;
      var ulLet = document.forms[0].letraHasta.value;
      if(tipNum!=0){
        if(prLet && ulLet){
          if(prLet>ulLet){
            jsp_alerta('A','<%=descriptor.getDescripcion("Etiq_PrimLetraUlt")%>');
            document.forms[0].letraDesde.value = '';
            document.forms[0].letraHasta.value = '';
            document.forms[0].letraDesde.focus();
          }
        }else if(!prLet && ulLet){
          jsp_alerta('A','<%=descriptor.getDescripcion("Etiq_NoExLetra")%>');
          document.forms[0].letraHasta.value = '';
          document.forms[0].letraDesde.focus();
        }
      }
    }

    function distinto(i){
      var numDesde = document.forms[0].numDesde.value;
      var letraDesde = document.forms[0].letraDesde.value;
      var numHasta = document.forms[0].numHasta.value;
      var letraHasta = document.forms[0].letraHasta.value;
      var codTipoNumeracion = document.forms[0].codTipoNumeracion.value;
      var bloque = document.forms[0].bloque.value;
      var portal = document.forms[0].portal.value;
      var codEscalera = document.forms[0].codEscalera.value;
      var codPlanta = document.forms[0].codPlanta.value;
      var codTipoVivienda = document.forms[0].codTipoVivienda.value;
      var descPostal = document.forms[0].descPostal.value;
      var puerta = document.forms[0].puerta.value;
      var km = document.forms[0].km.value;
      var hm = document.forms[0].hm.value;
      var dom = listaDomiciliosOriginal[i];
      if((numDesde==dom[2])&&(letraDesde==dom[3])&&(numHasta==dom[4])&&(letraHasta==dom[5])&&
        (bloque==dom[6])&&(portal==dom[7])&&(codEscalera==dom[8])&&(codPlanta==dom[10])&&
        (puerta==dom[12])&&(km==dom[13])&&(hm==dom[14])&&(codTipoNumeracion==dom[15])&&
        (codTipoVivienda==dom[17])&&(descPostal==dom[19])){
        return false;
      }
      if(codTipoNumeracion!="0"){
        numDesde = numDesde*1;
        var numDesdeDSU = dom[2]*1;
        var letraDesdeDSU = dom[3];
        var numHastaDSU = dom[4];
        var letraHastaDSU = dom[5];
        //alert(numDesde+"-"+letraDesde+" - "+numHasta+"-"+letraHasta+" / "+
        //  numDesdeDSU+"-"+letraDesdeDSU+" - "+numHastaDSU+"-"+letraHastaDSU);
        if(numHasta!=""){
          numHasta = numHasta*1;
          if(numHastaDSU!=""){
            numHastaDSU = numHastaDSU*1;
            if(((numDesde>numDesdeDSU)&&(numDesde<=numHastaDSU))||(numHasta>numDesdeDSU)){
              //alert("Zona 1");
              return false;
            }else if(numHasta==numDesdeDSU){// SON IGUALES,HAY QUE MIRAR LAS LETRAS
              if(((letraDesde<=letraDesdeDSU)&&(letraDesdeDSU<=letraHasta))||
                ((letraDesde<=letraHastaDSU)&&(letraHastaDSU<=letraHasta))||
                ((letraDesde>=letraHastaDSU)&&(letraHastaDSU>=letraHasta))){
                //alert("Zona 2");
                return false;
              }
            }
          }else{ // EL DOMICILIO A COMPARAR NO TIENE NUMERO HASTA
            if((numDesde<=numDesdeDSU)&&(numHasta>numDesdeDSU)){
              //alert("Zona 3");
              return false;
            }else if((numDesde==numDesdeDSU)&&(numHasta==numDesdeDSU)){// SON IGUALES,HAY QUE MIRAR LAS LETRAS
              if((letraDesde<=letraDesdeDSU)&&(letraDesdeDSU<=letraHasta)){
                //alert("Zona 4");
                return false;
              }
            }
          }
        }else{
          if(numHastaDSU!=""){
            numHastaDSU = numHastaDSU*1;
            if((numDesde>=numDesdeDSU)&&(numDesde<=numHastaDSU)&&
              (letraDesde>=letraDesdeDSU)&&(letraDesde<=letraHastaDSU)){
              //alert("Zona 5");
              return false;
            }
          }else{ // EL DOMICILIO A COMPARAR NO TIENE NUMERO HASTA
            if((numDesde==numDesdeDSU)&&(letraDesdeDSU!="")&&(letraDesde==letraDesdeDSU)){
              // SON IGUALES,HAY QUE MIRAR LAS LETRAS
              //alert("Zona 6");
              return false;
            }
          }
        }
      }
      return true;
    }

    function validarCamposRejilla(i){
      if(validarNumeracion()){
        var j=0;
        //alert(listaDomiciliosOriginal);
        for(j=0;j<listaDomiciliosOriginal.length;j++){
          //alert(i+"-"+j);
          if((i!=j) && !distinto(j)){
            jsp_alerta("A",'<%=descriptor.getDescripcion("Etiq_DomExiste")%>');
            return false;
          }
        }
        return true;
      }else
          jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
    }
    
    function filaSeleccionada(){
      var i= tablaDomicilios.focusedIndex;
      if(i>=0 && !tablaDomicilios.ultimoTable) return true;
      return false;
    }

    function esNumeroPar(numero){
      var resto = numero % 2;
      if(resto==0) return true;
      else
        return false;
    }

    function haCambiadoDistrito(i,j){
      var distritoOld = listaDomiciliosOriginal[i][23];
      var seccionOld = listaDomiciliosOriginal[i][24];
      var letraOld = listaDomiciliosOriginal[i][25];
      var distrito = tramosOriginal[j][6];
      var seccion = tramosOriginal[j][7];
      var letra = tramosOriginal[j][8];
      //alert(distritoOld+"-"+distrito+"-"+seccionOld+"-"+seccion+"-"+letraOld+"-"+letra);
      if((distritoOld!=distrito)||(seccionOld!=seccion)||
        (letraOld!=letra))
        return "SI";
      else
        return "NO";
    }

    function buscarTramo(i){
      var j=0;
      var numDesde = document.forms[0].numDesde.value*1;
      var numHasta = document.forms[0].numHasta.value;
      var encontradoTramo = false;
      for(j=0;j<tramosOriginal.length;j++){
        var numDTramo = tramosOriginal[j][9]*1;
        var letraDTramo = tramosOriginal[j][10];
        var numHTramo = tramosOriginal[j][11]*1;
        var letraHTramo = tramosOriginal[j][12];
        //alert(numDesde+"-"+numDTramo+"-"+numHTramo+"-"+tramosOriginal[j][0]);
        if(numHasta!=""){
          numHasta = numHasta*1;
          if(((numDesde>=numDTramo)&&(numDesde<=numHTramo))&&
             ((numHasta>=numDTramo)&&(numHTramo>=numHasta))){
            if(((esNumeroPar(numDesde))&&(tramosOriginal[j][0]=="2"))||
              ((!esNumeroPar(numDesde))&&(tramosOriginal[j][0]=="1"))){
              document.forms[0].codTramo.value = tramosOriginal[j][5]; // CODTRAMO
              document.forms[0].distrito.value = tramosOriginal[j][6]; // DISTRITO
              document.forms[0].seccion.value = tramosOriginal[j][7]; // SECCION
              document.forms[0].letra.value = tramosOriginal[j][8]; // LETRA
              if(i!=undefined)
                document.forms[0].haCambiadoDistrito.value = haCambiadoDistrito(i,j); 
              else
                document.forms[0].haCambiadoDistrito.value = "NO";
              encontradoTramo= true;
              break;
            }
          }
        }else{
          if((numDesde>=numDTramo)&&(numDesde<=numHTramo)){
            if(((esNumeroPar(numDesde))&&(tramosOriginal[j][0]=="2"))||
              ((!esNumeroPar(numDesde))&&(tramosOriginal[j][0]=="1"))){
              document.forms[0].codTramo.value = tramosOriginal[j][5]; // CODTRAMO
              document.forms[0].distrito.value = tramosOriginal[j][6]; // DISTRITO
              document.forms[0].seccion.value = tramosOriginal[j][7]; // SECCION
              document.forms[0].letra.value = tramosOriginal[j][8]; // LETRA
              if(i!=undefined)
                document.forms[0].haCambiadoDistrito.value = haCambiadoDistrito(i,j); 
              else
                document.forms[0].haCambiadoDistrito.value = "NO";
              encontradoTramo= true;
              break;
            }
          }
        }
      }
      //alert(nuevaListaNumeraciones);
      if((j==tramosOriginal.length)&&(!encontradoTramo)){
        jsp_alerta("A",'<%=descriptor.getDescripcion("Etiq_VerificarNumera")%>');
      }
      return encontradoTramo;
    }
    
    // FUNCIONES DE PULSACION DE BOTONES
    function pulsarBuscar(){
      var botonBuscar = ["botonBuscar"];
      if(validarCamposBusqueda()){
        buscarIdVia();
        document.forms[0].opcion.value="cargarDomicilios";
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/DomiciliosNormalizados.do";
        document.forms[0].submit();
        habilitarGeneralInputs(botonBuscar,false);
        habilitarCamposBusqueda(false);
        habilitarCamposRejilla(true);
      }else
        jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
    }
    
    function pulsarAlta(){
      if(!filaSeleccionada()){
        var i;// undefined
        if(validarCamposRejilla(i) && buscarTramo(i)){
          habilitarCamposBusqueda(true);
          habilitarGeneralInputs(numeros,true);
          document.forms[0].opcion.value="alta";
          document.forms[0].target="oculto";
          document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/DomiciliosNormalizados.do";
          document.forms[0].submit();
          habilitarCamposBusqueda(false);
        }
      }
    }

    function pulsarModificar(){
      if(filaSeleccionada()){
        var i = tablaDomicilios.focusedIndex;
        if(validarCamposRejilla(i)&& buscarTramo(i)){
          habilitarCamposBusqueda(true);
          habilitarGeneralInputs(numeros,true);
          document.forms[0].opcion.value="modificar";
          document.forms[0].target="oculto";
          document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/DomiciliosNormalizados.do";
          document.forms[0].submit();
          habilitarCamposBusqueda(false);
          //habilitarGeneralInputs(numeros,false);
        }
      }
    }

    function validarTerceros(){
      var tieneTerceros = document.forms[0].tieneTerceros.value*1;
      //alert(tieneTerceros);
      if(tieneTerceros>0){
        var confirmar = jsp_alerta("",'<%=descriptor.getDescripcion("Etiq_TerceAsocDom")%>');
        if(confirmar)
          return true;
        else
          return false;
      }
      return false;
    }

    function pulsarBorrar(){
      if(filaSeleccionada()){
        if(validarTerceros()){
          habilitarCamposBusqueda(true);
          document.forms[0].opcion.value="eliminar";
          document.forms[0].target="oculto";
          document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/DomiciliosNormalizados.do";
          document.forms[0].submit();
          habilitarCamposBusqueda(false);
          limpiarCamposRejilla();
        }
      }
    }

    function pulsarCancelarBuscar(){
      limpiarFormulario();
      var botonBuscar = ["botonBuscar"];
      habilitarGeneralInputs(botonBuscar,true);
      habilitarCamposBusqueda(true);
      habilitarCamposRejilla(false);
      valoresPorDefecto();
    }
    
    function pulsarLimpiar(){
      limpiarCamposRejilla();
    }
    
    function pulsarSalir(){
      window.location = "<%=request.getContextPath()%>/terceros/mantenimiento/DomiciliosNormalizados.do?opcion=salir"
    }
  </script>
</head>

<body class="bandaBody" onLoad="inicializar();">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
  
<form action="" method="get" name="formulario" target="_self">
<input type="hidden" name="opcion">
<input type="hidden" name="codPais" size="3" value="<%=ptVO.getPais()%>">
<input type="hidden" name="idVia" value="">
<input type="hidden" name="codDPO" value="">
<input type="hidden" name="codDSU" value="">
<input type="hidden" name="codTramo" value="">
<input type="hidden" name="tieneHabitantes" value="">
<input type="hidden" name="tieneTerceros" value="">
<input type="hidden" name="distrito" value="">
<input type="hidden" name="seccion" value="">
<input type="hidden" name="letra" value="">
<input type="hidden" name="haCambiadoDistrito" value="">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantDomicilios")%></div>
<div class="contenidoPantalla">
    <table width="100%">
      <tr>
        <td>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="33%">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_Provincia")%>
                    </td>
                  </tr>
                  <tr>
                    <td class="etiqueta">
                      <input class="inputTextoObligatorio" type="text" 
                        id="codProvincia" name="codProvincia" 
                        size="3" readonly> 
                      <input class="inputTextoObligatorio" type="text" 
                        id="descProvincia" name="descProvincia" 
                        style="width:175" readonly>
                    </td>
                  </tr>
                </table>
              </td>
              <td width="33%">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_Municipio")%>
                    </td>
                  </tr>
                  <tr>
                    <td class="etiqueta">
                      <input class="inputTextoObligatorio" type="text" 
                        id="codMunicipio" name="codMunicipio" size="3" readonly>
                      <input id="descMunicipio" name="descMunicipio" type="text" 
                        class="inputTextoObligatorio"	style="width:175" 
                        readonly>
                    </td>
                  </tr>
                </table>
              </td>
              <td width="34%">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_ECO")%>
                    </td>
                  </tr>
                  <tr>
                    <td class="etiqueta">
                      <input name="codECO" type="text" class="inputTexto" 
                        id="codECO" size="3"> 
                      <input name="descECO" type="text" class="inputTexto" 
                        id="descECO" style="width:175" readonly> 
                      <a id="anchotECO" name="anchorECO" href="">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                          name="botonECO" 
                          id="botonECO" 
                          style="cursor:hand;"></span>
                      </a>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="33%">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_ESI")%>
                    </td>
                  </tr>
                  <tr>
                    <td class="etiqueta">
                      <input name="codESI" type="text" class="inputTexto" 
                        id="codESI" size="3"> 
                      <input name="descESI" type="text" class="inputTexto" 
                        id="descESI" style="width:175" 
                        readonly> 
                      <a id="anchorESI" name="anchorESI" href="">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonESI"
                          name="botonESI" 
                          style="cursor:hand;"></span>
                      </a>
                    </td>
                  </tr>
                </table>
              </td>
              <td width="33%">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_NUC")%>
                    </td>
                  </tr>
                  <tr>
                    <td class="etiqueta">
                      <input name="codNUC" type="text" class="inputTexto" 
                        id="codNUC" size="3"> 
                      <input name="descNUC" type="text" class="inputTexto" 
                        id="descNUC" style="width:175" 
                        readonly> 
                      <a id="anchorNUC" name="anchorNUC" href="">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                          id="botonNUC" name="botonNUC" 
                          style="cursor:hand;"></span>
                      </a>
                    </td>
                  </tr>
                </table>
              </td>
              <td width="34%">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_Vial")%>
                    </td>
                  </tr>
                  <tr>
                    <td class="etiqueta"> 
                      <input type="text" class="inputTextoObligatorio" size=3 
                        id="codVia" name="codVia">
                      <input id="descVia" name="descVia" type="text" 
                        class="inputTextoObligatorio" 
                        readonly="true" style="width:175" maxlength=50>
                      <a href="" name="anchorVia" id="anchorVia">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                        id="botonVia" name="botonVia" 
                        style="cursor:hand;"></span>
                      </a>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr> 
        <td> 
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td align="right">
                <input name="botonBuscar" type="button"  class="boton" 
                  id="botonBuscar" 
                  value="<%=descriptor.getDescripcion("gbBuscar")%>"
                  onClick="pulsarBuscar();" accesskey="B">
                <input name="botonCancelar" type="button" class="boton" 
                  id="botonCancelar"
                  value="<%=descriptor.getDescripcion("gbCancelar")%>"
                  onClick="pulsarCancelarBuscar();" accesskey="C">
              </td>
            </tr>
            <tr>
              <td>&nbsp;</td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td> 
          <table width="100%" rules="cols"  border="0" cellspacing="0" 
            cellpadding="0" class="fondoCab">
            <tr>
              <td id="tablaDomicilios"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td> 
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="33%">
                      <table width="100%" border="0" cellspacing="0" 
                        cellpadding="0">
                        <tr>
                          <td class="etiqueta">
                            <%=descriptor.getDescripcion("gEtiq_TipoNum")%>
                          </td>
                        </tr>
                        <tr>
                          <td class="etiqueta">
                            <input id="codTipoNumeracion" name="codTipoNumeracion" 
                              type="text" class="inputTextoObligatorio" size="3"> 
                            <input name="descTipoNumeracion" type="text" 
                              class="inputTextoObligatorio" style="width:125" 
                              readonly> 
                            <a id="anchorTipoNumeracion" name="anchorTipoNumeracion" 
                              href="">
                              <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                id="botonTipoNumeracion" name="botonTipoNumeracion" 
                                
                                style="cursor:hand;"></span> 
                            </a>
                          </td>
                        </tr>
                      </table>
                    </td>
                    <td width="33%">
                      <table width="100%" border="0" cellspacing="0" 
                        cellpadding="0">
                        <tr>
                          <td class="etiqueta">
                            <%=descriptor.getDescripcion("gEtiq_TipoViv")%>
                          </td>
                        </tr>
                        <tr>
                          <td class="etiqueta">
                            <input id="codTipoVivienda" name="codTipoVivienda" 
                              type="text" class="inputTexto" size="3"> 
                            <input name="descTipoVivienda" type="text" 
                              class="inputTexto" style="width:125" 
                              readonly> 
                            <a id="anchorTipoVivienda" name="anchorTipoVivienda" 
                              href="">
                              <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                id="botonTipoVivienda" name="botonTipoVivienda" 
                                
                                style="cursor:hand;"></span> 
                            </a>
                          </td>
                        </tr>
                      </table>
                    </td>
                    <td width="34%">
                      <table width="100%" border="0" cellspacing="0" 
                        cellpadding="0">
                        <tr>
                          <td class="etiqueta">
                            <%=descriptor.getDescripcion("gEtiqCodPostal")%>
                          </td>
                        </tr>
                        <tr>
                          <td class="etiqueta">
                            <input type="text" class="inputTexto" 
                              style="width:75" maxlength=5  
                              id="descPostal" name="descPostal"> 
                            <a id="anchorPostal" name="anchorPostal" href="">
                              <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                id="botonPostal" name="botonPostal" 
                                style="cursor:hand;"></span> 
                            </a>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td>
                <table width="100%" border="0" cellspacing="2" 
                  cellpadding="0">
                  <tr>
                    <td align="center" width="8%" class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_PNumero")%>
                    </td>
                    <td align="center" width="6%" class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_Letra")%>
                    </td>
                    <td align="center" width="8%" class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_UNumero")%>
                    </td>
                    <td align="center" width="6%" class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_Letra")%>
                    </td>
                    <td align="center" width="6%" class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_Bloque")%>
                    </td>
                    <td align="center" width="6%" class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_Portal")%>
                    </td>
                    <td width="16%" class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_Escalera")%>
                    </td>
                    <td width="16%" class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_Planta")%>
                    </td>
                    <td align="center" width="8%" class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_Puerta")%>
                    </td>
                    <td align="center" width="8%" class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_Kilom")%>
                    </td>
                    <td align="center" width="8%" class="etiqueta">
                      <%=descriptor.getDescripcion("gEtiq_Hectom")%>
                    </td>
                  </tr>
                  <tr>
                    <td align="center">
                      <input name="numDesde" type="text" class="inputTexto" 
                        id="numDesde" size=3 onfocus="this.select();" 
                        onkeypress = "javascript:return SoloDigitos(event);"
                        onblur="comprobarNumeros();">
                    </td>
                    <td align="center">
                      <input name="letraDesde" type="text" class="inputTexto" 
                        id="letraDesde" size=3 onfocus="this.select();"
                        onblur="comprobarLetras();">
                    </td>
                    <td align="center">
                      <input name="numHasta" type="text" class="inputTexto" 
                        id="numHasta" onfocus="this.select();" size=3
                        onkeypress = "javascript:return SoloDigitos(event);"
                        onblur="comprobarNumeros();">
                    </td>
                    <td align="center">
                      <input name="letraHasta" type="text" class="inputTexto" 
                        id="letraHasta" onfocus="this.select();" 
                        onkeypress="javascript:PasaAMayusculas(event);" size=3
                        onblur="comprobarLetras();">
                    </td>
                    <td align="center">
                      <input name="bloque" type="text" class="inputTexto" 
                        id="bloque" onfocus="this.select();" 
                        onkeypress="javascript:PasaAMayusculas(event);" size=3>
                    </td>
                    <td align="center">
                      <input name="portal" type="text" class="inputTexto" 
                        id="txtPortal" onfocus="this.select();" 
                        onkeypress="javascript:PasaAMayusculas(event);" size=3>
                    </td>
                    <td align="center">
                      <input id="codEscalera" name="codEscalera" 
                        type="text" class="inputTexto" size="3"> 
                      <input name="descEscalera" type="text" 
                        class="inputTexto" style="width:70" 
                        readonly> 
                      <a id="anchorEscalera" name="anchorEscalera" 
                        href="">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                          id="botonEscalera" name="botonEscalera" 
                          
                          style="cursor:hand;"></span> 
                      </a>
                    </td>
                    <td align="center">
                      <input id="codPlanta" name="codPlanta" 
                        type="text" class="inputTexto" size="3"> 
                      <input name="descPlanta" type="text" 
                        class="inputTexto" style="width:70" 
                        readonly> 
                      <a id="anchorPlanta" name="anchorPlanta" 
                        href="">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                          id="botonPlanta" name="botonPlanta" 
                          
                          style="cursor:hand;"></span> 
                      </a>
                    </td>
                    <td align="center">
                      <input name="puerta" type="text" class="inputTexto" 
                        id="puerta" onfocus="this.select();" 
                        onkeypress="javascript:PasaAMayusculas(event);" size=3>
                    </td>
                    <td align="center">
                      <input name="km" type="text" class="inputTexto" id="km"
                        onfocus="this.select();" size=3>
                    </td>
                    <td align="center">
                      <input name="hm" type="text" class="inputTexto" id="hm"
                        onfocus="this.select();" size=3>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
    <div id="tablaBotones" class="botoneraPrincipal">
        <input type="button" class="boton"  name="botonAlta" onClick="pulsarAlta();" accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>">
        <input type="button" class="boton"  name="botonModificar" onClick="pulsarModificar();"  accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>">
        <input type="button" class="boton" name="botonBorrar" onClick="pulsarBorrar();" accesskey="B" value="<%=descriptor.getDescripcion("gbBorrar")%>">
        <input type="button" class="boton"  name="botonLimpiar" onClick="pulsarLimpiar();" accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>">
        <input type="button" class="boton" name="botonSalir" onClick="pulsarSalir();" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>">
    </div>
</div>
</form>
<script type="text/javascript">
  var indice;
  var tablaDomicilios = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaDomicilios"),775);
  tablaDomicilios.addColumna('50','left','<%= descriptor.getDescripcion("gEtiq_N")%>');
  tablaDomicilios.addColumna('50','left','<%= descriptor.getDescripcion("gEtiq_L")%>');
  tablaDomicilios.addColumna('50','left','<%= descriptor.getDescripcion("gEtiq_N")%>');
  tablaDomicilios.addColumna('50','left','<%= descriptor.getDescripcion("gEtiq_L")%>');
  tablaDomicilios.addColumna('50','left','<%= descriptor.getDescripcion("gEtiq_Blq")%>');
  tablaDomicilios.addColumna('50','left','<%= descriptor.getDescripcion("gEtiq_Por")%>');
  tablaDomicilios.addColumna('0','left','');
  tablaDomicilios.addColumna('50','left','<%= descriptor.getDescripcion("gEtiq_Esc")%>');  
  tablaDomicilios.addColumna('0','left','');
  tablaDomicilios.addColumna('50','left','<%= descriptor.getDescripcion("gEtiq_Plta")%>');
  tablaDomicilios.addColumna('50','left','<%= descriptor.getDescripcion("gEtiq_Pta")%>'); 
  tablaDomicilios.addColumna('50','left','<%= descriptor.getDescripcion("gEtiq_Km")%>');
  tablaDomicilios.addColumna('50','left','<%= descriptor.getDescripcion("gEtiq_Hm")%>');
  tablaDomicilios.addColumna('0','left','');
  tablaDomicilios.addColumna('80','left','<%= descriptor.getDescripcion("gEtiq_TipoViv")%>');
  tablaDomicilios.addColumna('80','left','<%= descriptor.getDescripcion("gEtiqCodPostal")%>');
  tablaDomicilios.addColumna('0','left','');
  tablaDomicilios.addColumna('0','left','');
  tablaDomicilios.displayCabecera=true;
  tablaDomicilios.displayTabla();
  
  function refresca(tabla){
    tabla.displayTabla();
  }

  var botonEliminar = ["botonBorrar"];
  function validarHabitantes(){
    var tieneHabitantes = document.forms[0].tieneHabitantes.value*1;
    if(tieneHabitantes>0){
      habilitarGeneralInputs(botonEliminar,false);
    }else{
      habilitarGeneralInputs(botonEliminar,true);
    }
  }
  
  var vectorCamposDomicilio = ["codDPO","codDSU","codTramo","tieneHabitantes","tieneTerceros","distrito",
    "seccion","letra"];

  function rellenarDatos(tableName,rowID){
    if(tablaDomicilios==tableName){
      var i=rowID;
      indice = rowID;
      limpiarCamposRejilla();
      if(i>=0 && !tableName.ultimoTable){
        var vectorDatosDomicilio = [listaDomiciliosOriginal[i][0],listaDomiciliosOriginal[i][1],
          listaDomiciliosOriginal[i][20],listaDomiciliosOriginal[i][21],listaDomiciliosOriginal[i][22],
          listaDomiciliosOriginal[i][23],listaDomiciliosOriginal[i][24],listaDomiciliosOriginal[i][25]];
        var vectorDatosRejilla = listaDomicilios[i];
        rellenar(vectorDatosRejilla,vectorCamposRejilla);
        rellenar(vectorDatosDomicilio,vectorCamposDomicilio);
        validarTipoNumeracion();
        validarHabitantes();
      }else{
        limpiarCamposRejilla();
      }
    }
  }	//Se ejecuta al cambiar la selección en una Tabla Simple.

<%String Agent = request.getHeader("user-agent");%>

var coordx=0;
var coordy=0;


<%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>
  
  // FUNCION DE CONTROL DE TECLAS
  document.onmouseup = checkKeys; 

  function checkKeysLocal(evento,tecla){
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

    //** Esta funcion se debe implementar en cada JSP para particularizar  **//
    //** las acciones a realizar de las distintas combinaciones de teclas  **//
    keyDel(evento);
    if(teclaAuxiliar == 40){
      upDownTable(tablaDomicilios,listaDomicilios,teclaAuxiliar);
    }
    if(teclaAuxiliar == 38){
      upDownTable(tablaDomicilios,listaDomicilios,teclaAuxiliar);
    }

    if (teclaAuxiliar == 9){

        if (comboECO.base.style.visibility == "visible") comboECO.ocultar();
        if (comboESI.base.style.visibility == "visible") comboESI.ocultar();
        if (comboNUC.base.style.visibility == "visible") comboNUC.ocultar();
        if (comboVia.base.style.visibility == "visible") comboVia.ocultar();
        if (comboTipoNumeracion.base.style.visibility == "visible") comboTipoNumeracion.ocultar();
        if (comboTipoVivienda.base.style.visibility == "visible") comboTipoVivienda.ocultar();
        if (comboPostal.base.style.visibility == "visible") comboPostal.ocultar();
        if (comboEscalera.base.style.visibility == "visible") comboEscalera.ocultar();
        if (comboPlanta.base.style.visibility == "visible") comboPlanta.ocultar();


    }
    if (teclaAuxiliar == 1){

        if (comboECO.base.style.visibility == "visible" && isClickOutCombo(comboECO,coordx,coordy)) setTimeout('comboECO.ocultar()',20);
        if (comboESI.base.style.visibility == "visible" && isClickOutCombo(comboESI,coordx,coordy)) setTimeout('comboESI.ocultar()',20);
        if (comboNUC.base.style.visibility == "visible" && isClickOutCombo(comboNUC,coordx,coordy)) setTimeout('comboNUC.ocultar()',20);
        if (comboVia.base.style.visibility == "visible" && isClickOutCombo(comboVia,coordx,coordy)) setTimeout('comboVia.ocultar()',20);
        if (comboTipoNumeracion.base.style.visibility == "visible" && isClickOutCombo(comboTipoNumeracion,coordx,coordy)) setTimeout('comboTipoNumeracion.ocultar()',20);
        if (comboTipoVivienda.base.style.visibility == "visible" && isClickOutCombo(comboTipoVivienda,coordx,coordy)) setTimeout('comboTipoVivienda.ocultar()',20);
        if (comboPostal.base.style.visibility == "visible" && isClickOutCombo(comboPostal,coordx,coordy)) setTimeout('comboPostal.ocultar()',20);
        if (comboEscalera.base.style.visibility == "visible" && isClickOutCombo(comboEscalera,coordx,coordy)) setTimeout('comboEscalera.ocultar()',20);
        if (comboPlanta.base.style.visibility == "visible") setTimeout('comboPlanta.ocultar()',20);
    }
  }

  var comboECO = new Combo("ECO");
  var comboESI = new Combo("ESI");
  var comboNUC = new Combo("NUC");
  var comboVia = new Combo("Via");
  var comboTipoNumeracion = new Combo("TipoNumeracion");
  var comboTipoVivienda = new Combo("TipoVivienda");
  var comboPostal = new Combo("Postal");
  var comboEscalera = new Combo("Escalera");
  var comboPlanta = new Combo("Planta");

  var auxCombo = "comboVia";
  
  comboECO.change = 
    function() { 
      auxCombo='comboESI'; 
      limpiar(['codESI','descESI','codNUC','descNUC','codVia','descVia']);
      cargarListas();
    }

  comboESI.change = 
    function() { 
      auxCombo='comboNUC'; 
      limpiar(['codNUC','descNUC','codVia','descVia']);
      if(comboESI.cod.value.length!=0){
        var i = comboESI.selectedIndex-1;
        if(i>=0){
          document.forms[0].codECO.value = codECOESIs[i][0];
          document.forms[0].descECO.value = codECOESIs[i][1];
        }
        cargarListas(); 			
        comboVia.addItems([],[]);
      }else{
        //valoresPorDefecto();
      }		
    } 

  comboNUC.change = 
    function() { 
      auxCombo='comboVia'; 
      limpiar(['codVia','descVia']);
      if(comboNUC.cod.value.length!=0){
        var i = comboNUC.selectedIndex-1;
        if(i>=0){
          document.forms[0].codECO.value = codECOESINUCs[i][0];
          document.forms[0].descECO.value = codECOESINUCs[i][1];
          document.forms[0].codESI.value = codECOESINUCs[i][2];
          document.forms[0].descESI.value = codECOESINUCs[i][3];
        }
        cargarListaVias();
      }else {
        //valoresPorDefecto();
      }
    }

   comboVia.change = 
    function() { 
      auxCombo='comboVia'; 
      if(comboVia.cod.value.length!=0){
        var i = comboVia.selectedIndex;
        if(i>=0){
          document.forms[0].codECO.value = codECOESIVias[i][0];
          document.forms[0].descECO.value = codECOESIVias[i][1];
          document.forms[0].codESI.value = codECOESIVias[i][2];
          document.forms[0].descESI.value = codECOESIVias[i][3];
          document.forms[0].codNUC.value = codECOESIVias[i][4];
          document.forms[0].descNUC.value = codECOESIVias[i][5];
        }
      }else {
        //valoresPorDefecto();
      }
    }
  
  var numeros = ["numDesde","letraDesde","numHasta","letraHasta"];

  function validarTipoNumeracion(){
    var tipoNumeracion = document.forms[0].codTipoNumeracion.value;
    if(tipoNumeracion=="0"){
      rellenar(["","S","","S"],numeros);
      document.forms[0].numDesde.className = "inputTexto";
      habilitarGeneralInputs(numeros,false);
    }else{
      habilitarGeneralInputs(numeros,true);
      document.forms[0].numDesde.className += " inputTextoObligatorio";
    }
  }

  comboTipoNumeracion.change = 
    function() { 
      limpiar(numeros);
      if(comboTipoNumeracion.cod.value.length!=0){
        validarTipoNumeracion();
      }else{
        //valoresPorDefecto();
      }		
    } 

  function cargarComboBox(cod, des){
    eval(auxCombo+".addItems(cod,des)");
  }

</script>
</body>
</html>
