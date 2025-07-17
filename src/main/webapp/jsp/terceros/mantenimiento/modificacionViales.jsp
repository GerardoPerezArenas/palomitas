<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>

<html>
    <head>
        <title>CONSULTA DE HABITANTES</title>
        <meta http-equiv="" content="text/html; charset=iso-8859-1">
        <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
        
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            ParametrosTerceroValueObject paramsTercero = new ParametrosTerceroValueObject();
            int idioma = 1;
            int apl = 3;
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                paramsTercero = (ParametrosTerceroValueObject) session.getAttribute("parametrosTercero");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
            }
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
        %>
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
        <!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script> 
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>" type="text/css">
        <script type="text/javascript">
            // VARIABLES GLOBALES
    <%
            MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm) session.getAttribute("MantenimientosTercerosForm");
            String operacion = mantForm.getOperacion();
    %>
        var operacion = '<%=mantForm.getOperacion()%>';
        var pais = '<%=paramsTercero.getPais()%>';
        var provincia = '<%=paramsTercero.getProvincia()%>';
        var municipio = '<%=paramsTercero.getMunicipio()%>';
        var provinciaN = '<%=paramsTercero.getNomProvincia()%>';
        var municipioN = '<%=paramsTercero.getNomMunicipio()%>';
        var codTipoVias = new Array();
        var descTipoVias = new Array();
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
        var viasOriginal = new Array();
        var codVias = new Array();
        var descVias = new Array();
        var viasOriginalOld = new Array();
        var codViasOld = new Array();
        var descViasOld = new Array();
        var listaNumeraciones = new Array();
        var nuevaListaNumeraciones = new Array();
        var nuevaListaNumeracionesOld = new Array();
        var listaNueva = new Array();
        var lista = new Array();
        var tramosOriginal = new Array();
        
        var botonesNumeracion =['botonIncrementar','botonLimpiarTodos','botonTodosIgual','botonModificarFila','incremento'];
        
        // FUNCION DE CONTROL DE TECLAS
        document.onmouseup = checkKeys; 
        
        // FUNCION DE INICIALIZACIÓN
        function inicializar(){
            pleaseWait1('off',top.mainFrame);
           
                
            document.forms[0].descProvincia.value = provinciaN;
            document.forms[0].descMunicipio.value = municipioN;
            recuperaDatosIniciales();
            tablaNumeracion.lineas = lista;
            refresca(tablaNumeracion);
            habilitarGeneralInputs(botonesNumeracion,false);
            habilitarGeneralInputs(['botonAceptar'],false);
        }  
        
        // FUNCIONES DE CARGA DE COMBOS
        function recuperaDatosIniciales(){
    <%
            Vector listaTipoVias = mantForm.getListaTipoVias();
            Vector listaECOs = mantForm.getListaEcos();
            Vector listaESIs = mantForm.getListaEsis();
            Vector listaNUCs = mantForm.getListaNucleos();
            Vector listaVias = mantForm.getListaVias();
            int lengthTVias = listaTipoVias.size();
            int lengthECOs = listaECOs.size();
            int lengthESIs = listaESIs.size();
            int lengthNUCs = listaNUCs.size();
            int lengthVias = listaVias.size();
            int i = 0;
            String codTipoVias = "";
            String descTipoVias = "";
            for (i = 0; i < lengthTVias - 1; i++) {
                GeneralValueObject tVias = (GeneralValueObject) listaTipoVias.get(i);
                codTipoVias += "\"" + tVias.getAtributo("codTipoVia") + "\",";
                descTipoVias += "\"" + tVias.getAtributo("descTipoVia") + "\",";
            }
            if (lengthTVias > 0) {
                GeneralValueObject tVias = (GeneralValueObject) listaTipoVias.get(i);
                codTipoVias += "\"" + tVias.getAtributo("codTipoVia") + "\"";
                descTipoVias += "\"" + tVias.getAtributo("descTipoVia") + "\"";
            }
            String codEntColectivas = "";
            String descEntColectivas = "";
            for (i = 0; i < lengthECOs - 1; i++) {
                GeneralValueObject ecos = (GeneralValueObject) listaECOs.get(i);
                codEntColectivas += "\"" + ecos.getAtributo("codECO") + "\",";
                descEntColectivas += "\"" + ecos.getAtributo("descECO") + "\",";
            }
            if (lengthECOs > 0) {
                GeneralValueObject ecos = (GeneralValueObject) listaECOs.get(i);
                codEntColectivas += "\"" + ecos.getAtributo("codECO") + "\"";
                descEntColectivas += "\"" + ecos.getAtributo("descECO") + "\"";
            }
            String codESIs = "";
            String descESIs = "";
            String codECOESIs = "";
            for (i = 0; i < lengthESIs - 1; i++) {
                GeneralValueObject esis = (GeneralValueObject) listaESIs.get(i);
                codESIs += "\"" + esis.getAtributo("codEntidadSingular") + "\",";
                descESIs += "\"" + esis.getAtributo("nombreOficial") + "\",";
                codECOESIs += "[\"" + esis.getAtributo("codEntidadColectiva") + "\"," +
                        "\"" + esis.getAtributo("descEntidadColectiva") + "\"," +
                        "\"" + esis.getAtributo("codEntidadSingular") + "\"," +
                        "\"" + esis.getAtributo("nombreOficial") + "\"],";
            }
            if (lengthESIs > 0) {
                GeneralValueObject esis = (GeneralValueObject) listaESIs.get(i);
                codESIs += "\"" + esis.getAtributo("codEntidadSingular") + "\"";
                descESIs += "\"" + esis.getAtributo("nombreOficial") + "\"";
                codECOESIs += "[\"" + esis.getAtributo("codEntidadColectiva") + "\"," +
                        "\"" + esis.getAtributo("descEntidadColectiva") + "\"," +
                        "\"" + esis.getAtributo("codEntidadSingular") + "\"," +
                        "\"" + esis.getAtributo("nombreOficial") + "\"]";
            }
            String codNUCs = "";
            String descNUCs = "";
            String codECOESINUCs = "";
            for (i = 0; i < lengthNUCs - 1; i++) {
                GeneralValueObject nucs = (GeneralValueObject) listaNUCs.get(i);
                codNUCs += "\"" + nucs.getAtributo("codNUC") + "\",";
                descNUCs += "\"" + nucs.getAtributo("descNUC") + "\",";
                codECOESINUCs += "[\"" + nucs.getAtributo("codECO") + "\"," +
                        "\"" + nucs.getAtributo("descECO") + "\"," +
                        "\"" + nucs.getAtributo("codESI") + "\"," +
                        "\"" + nucs.getAtributo("descESI") + "\"],";
            }
            if (lengthNUCs > 0) {
                GeneralValueObject nucs = (GeneralValueObject) listaNUCs.get(i);
                codNUCs += "\"" + nucs.getAtributo("codNUC") + "\"";
                descNUCs += "\"" + nucs.getAtributo("descNUC") + "\"";
                codECOESINUCs += "[\"" + nucs.getAtributo("codECO") + "\"," +
                        "\"" + nucs.getAtributo("descECO") + "\"," +
                        "\"" + nucs.getAtributo("codESI") + "\"," +
                        "\"" + nucs.getAtributo("descESI") + "\"]";
            }
            String codVias = "";
            String descVias = "";
            String viasOriginal = "";
            String codECOESIVias = "";
            for (i = 0; i < lengthVias - 1; i++) {
                GeneralValueObject vias = (GeneralValueObject) listaVias.get(i);
                codVias += "\"" + vias.getAtributo("codVia") + "\",";
                descVias += "\"" + vias.getAtributo("descVia") + "\",";
                viasOriginal += "[\"" + vias.getAtributo("idVia") + "\"," +
                        "\"" + vias.getAtributo("codTipoVia") + "\"," +
                        "\"" + vias.getAtributo("descTipoVia") + "\"," +
                        "\"" + vias.getAtributo("nombreCorto") + "\"" + "],";
            /* Cambio combo viales *		  
            codECOESIVias+="[\""+vias.getAtributo("codECO")+"\","+
            "\""+vias.getAtributo("descECO")+"\","+
            "\""+vias.getAtributo("codESI")+"\","+
            "\""+vias.getAtributo("descESI")+"\","+
            "\""+vias.getAtributo("codNUC")+"\","+
            "\""+vias.getAtributo("descNUC")+"\"],";
             * Fin cambio combo viales */
            }
            if (lengthVias > 0) {
                GeneralValueObject vias = (GeneralValueObject) listaVias.get(i);
                codVias += "\"" + vias.getAtributo("codVia") + "\"";
                descVias += "\"" + vias.getAtributo("descVia") + "\"";
                viasOriginal += "[\"" + vias.getAtributo("idVia") + "\"," +
                        "\"" + vias.getAtributo("codTipoVia") + "\"," +
                        "\"" + vias.getAtributo("descTipoVia") + "\"," +
                        "\"" + vias.getAtributo("nombreCorto") + "\"" + "]";
            }
            /* Cambio combo viales *		
            codECOESIVias+="[\""+vias.getAtributo("codECO")+"\","+
            "\""+vias.getAtributo("descECO")+"\","+
            "\""+vias.getAtributo("codESI")+"\","+
            "\""+vias.getAtributo("descESI")+"\","+
            "\""+vias.getAtributo("codNUC")+"\","+
            "\""+vias.getAtributo("descNUC")+"\"]";
             * Fin cambio combo viales */
      %>
          codTipoVias = [<%=codTipoVias%>];
              descTipoVias = [<%=descTipoVias%>];
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
                                                          viasOriginal = [<%=viasOriginal%>];
                                                              codVias = [<%=codVias%>];
                                                                  descVias = [<%=descVias%>];
                                                                      codECOESIVias = [<%=codECOESIVias%>];
                                                                          codECOESIViasOld = [<%=codECOESIVias%>];
                                                                              codESIsOld = codESIs;
                                                                              descESIsOld = descESIs;
                                                                              codNUCsOld = codNUCs;
                                                                              descNUCsOld = descNUCs;
                                                                              viasOriginalOld = viasOriginal;
                                                                              codViasOld = codVias;
                                                                              descViasOld = descVias;
                                                                              // CARGAR COMBOS
                                                                              comboTipoVia.addItems(codTipoVias,descTipoVias);
                                                                              comboECO.addItems(codECOs,descECOs);
                                                                              comboESI.addItems(codESIs,descESIs);
                                                                              comboNUC.addItems(codNUCs,descNUCs);
                                                                              comboVia.addItems(codVias,descVias);
                                                                          }
                                                                          
                                                                          function actualizarListaVias(){
                                                                              limpiar(['codECO','descECO','codESI','descESI','codNUC','descNUC','codVia','descVia',
                                                                                  'codTVia','descTVia','codViaNuevo','descViaNuevo']);
                                                                              limpiarRejillaNumeracion();
                                                                              document.forms[0].opcion.value="cargarVias";
                                                                              document.forms[0].target="oculto";
                                                                              document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
                                                                              document.forms[0].submit();
                                                                          }
                                                                          
                                                                          function cargarListaVias(){
                                                                              document.forms[0].opcion.value="cargarVias";
                                                                              document.forms[0].target="oculto";
                                                                              document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
                                                                              document.forms[0].submit();
                                                                          }
                                                                          
                                                                          function cargarListas(){
                                                                              document.forms[0].opcion.value="cargarListas";
                                                                              document.forms[0].target="oculto";
                                                                              document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
                                                                              document.forms[0].submit();
                                                                          }
                                                                          
                                                                          function cargarNumeraciones(){
                                                                              document.forms[0].opcion.value="cargarNumeraciones";
                                                                              document.forms[0].target="oculto";
                                                                              document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
                                                                              document.forms[0].submit();
                                                                          }
                                                                          
                                                                          function recuperaNumeraciones(){
                                                                              tablaNumeracion.lineas = lista;
                                                                              refresca(tablaNumeracion);
                                                                              habilitarGeneralInputs(['botonBuscar'],false);
                                                                              comboVia.deactivate();
                                                                              habilitarGeneralInputs(['botonAceptar'],true);
                                                                          }
                                                                          
                                                                          function valoresPorDefecto(){
                                                                              codESIs = codESIsOld;
                                                                              descESIs = descESIsOld;
                                                                              codECOESIs = codECOESIsOld;
                                                                              codECOESINUCs = codECOESINUCsOld;
                                                                              codNUCs = codNUCsOld;
                                                                              descNUCs = descNUCsOld;
                                                                              viasOriginal = viasOriginalOld;
                                                                              codVias = codViasOld;
                                                                              descVias = descViasOld;
                                                                              codECOESIVias = codECOESIViasOld;
                                                                              comboESI.addItems(codESIs,descESIs);
                                                                              comboNUC.addItems(codNUCs,descNUCs);
                                                                              comboVia.addItems(codVias,descVias);
                                                                          }
                                                                          
                                                                          function haCambiadoVial(){
                                                                              var codViaOld = document.forms[0].codVia.value;
                                                                              var descViaOld = document.forms[0].descVia.value;
                                                                              var descViaOldA = descViaOld.split("-");
                                                                              descViaOld = Trim(descViaOldA[0]);
                                                                              var codViaNuevo = document.forms[0].codViaNuevo.value;
                                                                              var descViaNuevo = document.forms[0].descViaNuevo.value;
                                                                              var codTVia = document.forms[0].codTVia.value;
                                                                              var i = comboVia.selectedIndex-1;
                                                                              if(((codViaNuevo!="")&&(codViaNuevo!=codViaOld))||
                                                                                  ((descViaNuevo!="")&&(descViaNuevo!=descViaOld))||
                                                                                  (codTVia!=viasOriginal[i][1])){
                                                                              document.forms[0].haCambiadoVial.value="SI";
                                                                              return true;
                                                                          }else{
                                                                          document.forms[0].haCambiadoVial.value="NO";
                                                                          return false;
                                                                      }
                                                                  }
                                                                  
                                                                  function haCambiadoNumeracion(){
                                                                      var cambiado = document.forms[0].haCambiadoNumeracion.value;
                                                                      document.forms[0].nuevaNumeracion.value=listaNueva;
                                                                      if(cambiado=="SI"){
                                                                          return true;
                                                                      }else{
                                                                      pulsarTodosIgual();
                                                                      document.forms[0].nuevaNumeracion.value=listaNueva;
                                                                      return false;
                                                                  }
                                                              }
                                                              
                                                              function pulsarModificarFila(){
                                                                  var i=0;
                                                                  var numDesde = document.forms[0].numDesde.value;
                                                                  var numHasta = document.forms[0].numHasta.value;
                                                                  if(numDesde!=""){ // HAY NUMERO DESDE
                                                                      numDesde = parseInt(numDesde);
                                                                      if(numHasta!=""){ // HAY NUMERO HASTA
                                                                          numHasta = parseInt(numHasta);
                                                                      }
                                                                      for(i=0;i<lista.length;i++){
                                                                          if(i==tablaNumeracion.focusedIndex){
                                                                              listaNueva[i][1] = numDesde;
                                                                              listaNueva[i][2] = document.forms[0].letraDesde.value;
                                                                              listaNueva[i][3] = numHasta;
                                                                              listaNueva[i][4] = document.forms[0].letraHasta.value;
                                                                              listaNueva[i][5] = "SI";
                                                                              //alert(nuevaListaNumeraciones);
                                                                              lista[i][2] = numDesde+"-"+numHasta;
                                                                              lista[i][3] = lista[i][1];
                                                                              document.forms[0].haCambiadoNumeracion.value="SI";
                                                                              recuperaNumeraciones();
                                                                              limpiar(camposNumeracion);
                                                                          }
                                                                      }
                                                                  }
                                                              }
                                                              
                                                              // FUNCIONES DE PULSACIÓN DE BOTONES
                                                              function pulsarTodosIgual(){
                                                                  var i=0;
                                                                  limpiar(camposNumeracion);
                                                                  for(i=0;i<lista.length;i++){
                                                                      listaNueva[i][1] = nuevaListaNumeraciones[i][1];
                                                                      listaNueva[i][2] = nuevaListaNumeraciones[i][2];
                                                                      listaNueva[i][3] = nuevaListaNumeraciones[i][3];
                                                                      listaNueva[i][4] = nuevaListaNumeraciones[i][4];
                                                                      listaNueva[i][5] = "NO";
                                                                      lista[i][2] = lista[i][0];
                                                                      lista[i][3] = lista[i][1];
                                                                  }
                                                                  document.forms[0].haCambiadoNumeracion.value="NO";
                                                                  recuperaNumeraciones();
                                                              }
                                                              
                                                              function pulsarLimpiar(){
                                                                  limpiar(['codECO','descECO','codESI','descESI','codNUC','descNUC','codVia','descVia',
                                                                      'codTVia','descTVia','codViaNuevo','descViaNuevo']);
                                                                  limpiarRejillaNumeracion();
                                                                  habilitarGeneralInputs(['botonBuscar'],true);
                                                                  comboVia.activate();
                                                                  habilitarGeneralInputs(['botonAceptar'],false);
                                                                  habilitarGeneralInputs(botonesNumeracion,false);	
                                                                  valoresPorDefecto();
                                                              }
                                                              
                                                              function pulsarLimpiarTodos(){
                                                                  var i=0;
                                                                  limpiar(camposNumeracion);
                                                                  for(i=0;i<lista.length;i++){
                                                                      listaNueva[i][1] = nuevaListaNumeraciones[i][1];
                                                                      listaNueva[i][2] = nuevaListaNumeraciones[i][2];
                                                                      listaNueva[i][3] = nuevaListaNumeraciones[i][3];
                                                                      listaNueva[i][4] = nuevaListaNumeraciones[i][4];
                                                                      listaNueva[i][5] = "NO";
                                                                      lista[i][2] = "";
                                                                      lista[i][3] = "";
                                                                  }
                                                                  document.forms[0].haCambiadoNumeracion.value="NO";
                                                                  recuperaNumeraciones();
                                                              }
                                                              
                                                              function pulsarIncrementar(){
                                                                  var incremento = document.forms[0].incremento.value;
                                                                  limpiar(camposNumeracion);
                                                                  if(incremento!=""){
                                                                      var i=0;
                                                                      for(i=0;i<lista.length;i++){
                                                                          if(listaNueva[i][1]!=""){ // ES UNA DSU CON NUMERACION
                                                                              var numDesde = parseInt(listaNueva[i][1]);
                                                                              var numHasta = "";
                                                                              if(listaNueva[i][3]!=""){ // HAY NUMERO HASTA
                                                                                  numHasta = parseInt(listaNueva[i][3]);
                                                                                  numHasta += parseInt(incremento);
                                                                              }
                                                                              numDesde += parseInt(incremento);
                                                                              //alert(numDesde+"-"+numHasta);
                                                                              listaNueva[i][1] = numDesde;
                                                                              listaNueva[i][3] = numHasta;
                                                                              listaNueva[i][5] = "SI";
                                                                              lista[i][2] = numDesde+"-"+numHasta;
                                                                              lista[i][3] = lista[i][1];
                                                                          }
                                                                      }
                                                                      document.forms[0].haCambiadoNumeracion.value="SI";
                                                                      recuperaNumeraciones();
                                                                  }
                                                              }
                                                              
                                                              function esNumeroPar(numero){
                                                                  var resto = numero % 2;
                                                                  if(resto==0) return true;
                                                                  else
                                                                      return false;
                                                              }
                                                              
                                                                  
                                                                  function pulsarAceptar(){
                                                                      var codVia=document.forms[0].codVia.value;
                                                                      if(codVia!=""){
                                                                          var vial = haCambiadoVial();
                                                                          var nume = haCambiadoNumeracion();
                                                                          if(vial||nume){
                                                                            var source = "<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do?opcion=fechaOperacion";
                                                                            abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source,'',
                                                                                      'width=571,height=300,status='+ '<%=statusBar%>',function(fechaOperacion){
                                                                                              if(fechaOperacion!=undefined){
                                                                                                    document.forms[0].fechaOperacion.value = fechaOperacion;
                                                                                                    document.forms[0].nuevaNumeracion.value = normalizarVector(listaNueva);
                                                                                                    document.forms[0].opcion.value="modificarViaTerritorio";
                                                                                                    document.forms[0].target="oculto";
                                                                                                    document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Viales.do";
                                                                                                    document.forms[0].submit();
                                                                                              }
                                                                                      });
                                                                          }else {
                                                                          jsp_alerta("A","<%=descriptor.getDescripcion("gMsgNoCambio")%>");
                                                                          }
                                                                          
                                                                      }
                                                                  }
                                                                  
                                                                  function pulsarCancelar(){
                                                                      document.forms[0].opcion.value="inicializarTerc";
                                                                      document.forms[0].target="mainFrame";
                                                                      document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                                                      document.forms[0].submit();
                                                                  }
                                                                  
                                                                  //función encargada de añadir un token para poder tratar posteriormente el array de javascript en java
                                                                  function normalizarVector(vector){
                                                                      var aux = new Array();
                                                                      for(var i=0; i<vector.length; i++){
                                                                          aux[i] = new Array();
                                                                          for(var j=0; j<vector[i].length; j++){
                                                                              aux[i][j] = vector[i][j] + '¥';
                                                                          }
                                                                          aux[i][j] = '§';
                                                                      }
                                                                      return aux;
                                                                  }
                                                                  
                                                                  function pulsarBuscar() {		
                                                                      if(comboVia.cod.value.length!=0) {
                                                                          cargarNumeraciones();
                                                                      } else {
                                                                      jsp_alerta("A","<%=descriptor.getDescripcion("gMsgNoVia")%>");
                                                                      }
                                                                  }
                                                                  
                                                                  function modificacionRealizada(){
                                                                      pulsarLimpiar();
                                                                      actualizarListaVias();
                                                                  }
        </script>
    </head>
    
    <body class="bandaBody"  onLoad="inicializar();">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
<form name="formulario" method="post" action="" target="_self">
<input type="hidden" name="opcion" value="">
<input type="hidden" name="haCambiadoVial" value="NO">
<input type="hidden" name="haCambiadoNumeracion" value="NO">
<input type="hidden" name="nuevaNumeracion" value="">
<input type="hidden" name="operacion" value='<%=mantForm.getOperacion()%>'>
<input type="hidden" name="idVia" value="">
<input type="hidden" name="fechaOperacion" value="">
            
    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("tit_modViales")%></div>
    <div class="contenidoPantalla">
        <table  width="100%" cellspacing="0px" cellpadding="0px" border="0px">
            <tr>
                <td class="etiqueta" colspan="2"><%=descriptor.getDescripcion("gEtiq_ECO")%></td>
                <td class="etiqueta" colspan="2"><%=descriptor.getDescripcion("gEtiq_NUC")%></td>
            </tr>          
            <tr>
                <td colspan="2">
                    <input name="descProvincia" type="hidden">
                    <input name="codECO" type="text" class="inputTexto" id="codECO" size="3"> 
                    <input name="descECO" type="text" class="inputTexto" id="descECO" style="width:350" readonly> 
                    <a id="anchotECO" name="anchorECO" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                                                    id="botonECO" name="botonECO" style="cursor:hand;"></span></a>
                </td>
                <td colspan="2">
                    <input name="codNUC" type="text" class="inputTexto" id="codNUC" size="3"> 
                    <input name="descNUC" type="text" class="inputTexto" id="descNUC" style="width:350" readonly> 
                    <a id="anchotNUC" name="anchorNUC" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                                                    id="botonNUC" name="botonNUC" style="cursor:hand;"></span></a>
                </td>                  
            </tr>
            <tr>
                <td class="etiqueta" colspan="2"><%=descriptor.getDescripcion("gEtiq_ESI")%></td>
                <td class="etiqueta" colspan="2"><%=descriptor.getDescripcion("gEtiq_Vial")%></td>
            </tr>
            <tr>
                <td class="etiqueta" colspan="2">
                    <input name="descMunicipio" type="hidden"> 
                    <input name="codESI" type="text" class="inputTexto" id="codESI" size="3"> 
                    <input name="descESI" type="text" class="inputTexto" id="descESI" style="width:350" readonly> 
                    <a id="anchotESI" name="anchorESI" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                                                    id="botonESI" name="botonESI" style="cursor:hand;"></span></a>
                </td>
                <td colspan="2">
                    <input type="text" class="inputTextoObligatorio" size=3 id="codVia" name="codVia">
                    <input id="descVia" name="descVia" type="text" class="inputTextoObligatorio" readonly="true" 
                           style="width:350" maxlength=50>
                    <a href="" name="anchorVia" id="anchorVia"><span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                                                        id="botonVia" name="botonVia" 
                                                                    style="cursor:hand;"></span></a>
                </td>
            </tr>
            <tr>
                <td colspan="4" align="right" style="padding: 5px 15px 5px 0;">
                    <input name="botonBuscar" type="button" class="botonGeneral" id="botonBuscar" accesskey="B" 
                           onClick="pulsarBuscar();" value='<%=descriptor.getDescripcion("gbBuscar")%>'>
                    <input name="botonLimpiar" type="button" class="botonGeneral" id="botonLimpiar" accesskey="L" 
                           onClick="pulsarLimpiar();" value='<%=descriptor.getDescripcion("gbLimpiar")%>' >
                </td>
            </tr>
            <tr> 
                <td colspan="4" class="sub3titulo" style="height: 20px">
                    &nbsp;&nbsp;<%=descriptor.getDescripcion("gEtiq_CambioVias")%>
                </td>
            </tr>
            <tr>
                <td class="etiqueta"><%=descriptor.getDescripcion("manTer_EtiqTVI")%></td> 					
                <td> 
                    <input class="inputTexto" type="text" id="codTVia" name="codTVia" size="3">
                    <input id="descTVia" name="descTVia" type="text"  class="inputTexto"	style="width:300" readonly>
                    <a id="anchorTVia" name="anchorTVia" href="">	<span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                                                       id="botonTVia" name="botonTVia" style="cursor:hand;"></span></a>
                </td>
                <td class="etiqueta"><%=descriptor.getDescripcion("gEtiq_NuevoVial")%></td>
                <td >
                    <input type="text" class="inputTexto" size=3 id="codViaNuevo" name="codViaNuevo">
                    <input id="descViaNuevo" name="descViaNuevo" type="text" class="inputTexto"
                           style="width:300" maxlength=50
                           onblur="return xAMayusculas(this);">
                </td>
            </tr>
        </table>
        <table style="width:100%">
        <tr> 
            <TD class="sub3titulo" style="height: 20px" colspan="2">
                &nbsp;&nbsp;<%=descriptor.getDescripcion("gEtiq_CambioNum")%>
            </td>
        </tr>
        <tr> 
            <td style="width: 65%">
                <table width="100%" border="0" cellspacing="4" cellpadding="0" align="center">
                    <tr>
                        <td>
                            <table align="center" border="0">
                             <tr>
                                <td  id="tablaNumeracion"></td>
                             </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">
                            <table style="width:100%">
                                <tr style="text-align: center">
                                    <td style="width: 26%">
                                        <input name="numDesdeOld" type="text" class="inputTexto" readonly="true" size="2"> -
                                        <input name="numHastaOld" type="text" class="inputTexto" readonly="true" size="2">
                                    </td>
                                    <td style="width: 24%">
                                        <input name="letraDesdeOld" type="text" class="inputTexto" readonly="true" size="2"> -
                                        <input name="letraHastaOld" type="text" class="inputTexto" readonly="true" size="2">
                                    </td>
                                    <td style="width: 26%">
                                        <input name="numDesde" type="text" class="inputTexto" size="2"> -
                                        <input name="numHasta" type="text" class="inputTexto" size="2">
                                    </td>
                                    <td style="width: 24%">
                                        <input name="letraDesde" type="text" class="inputTexto" readonly="true" size="2"> -
                                        <input name="letraHasta" type="text" class="inputTexto" readonly="true" size="2">
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td style="width: 35%" >								
                <table style="width: 100%; text-align: center;"  border="0" cellspacing="4" cellpadding="0">  
                    <tr>
                        <td align="center" class="etiqueta" style="height: 10px"></td>
                    </tr>
                    <tr>
                        <td class="etiqueta">
                            <input name="botonTodosIgual" type="button" class="botonGeneral" id="botonTodosIgual" accesskey="T" onClick="pulsarTodosIgual();" 
                                   value='<%=descriptor.getDescripcion("etiqTodosIgual")%>'>
                        </td>
                    </tr>
                    <tr>
                        <td class="etiqueta">
                            <input name="botonLimpiarTodos" type="button" class="botonGeneral" id="botonLimpiarTodos" accesskey="L" onClick="pulsarLimpiarTodos();" 
                                   value='<%=descriptor.getDescripcion("etiqLimpiarTodos")%>'>
                        </td>
                    </tr>
                    <tr>
                        <td class="etiqueta">
                            <input class="botonGeneral" name="botonIncrementar" type="button" value='<%=descriptor.getDescripcion("gbInc")%>' accesskey="I" onClick="pulsarIncrementar();">
                        </td>
                        <td class="etiqueta" align="left">
                            <%=descriptor.getDescripcion("etiqIncIgual")%>&nbsp;
                            <input class="inputTexto" name="incremento" type="text" size="3" maxlength="3" onkeyup="return SoloDigitosNumericos(this);">
                        </td>										
                    </tr>
                    <tr>
                        <td align="left" class="etiqueta" style="height: 10px"></td>
                    </tr>
                    <tr>
                        <td class="etiqueta">
                            <input class="botonGeneral" name="botonModificarFila" type="button" id="botonModificarFila" value="Modificar" accesskey="M" onClick="pulsarModificarFila();">											
                        </td>
                        <td class="etiqueta" align="left">
                            <input name="generarOperaciones" type="checkbox" value="SI" checked>
                            <%=descriptor.getDescripcion("etiqGenOper")%>
                        </td>
                    </tr>
                    </table>
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input class="botonGeneral" name="botonAceptar" id="botonAceptar" type="button" value='<%=descriptor.getDescripcion("gbAceptar")%>' accesskey="A" onClick="pulsarAceptar();"> 
            <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar" accesskey="C" onClick="pulsarCancelar();" value='<%=descriptor.getDescripcion("gbSalir")%>'>
        </div>                                        				
    </div>                                        				
</form>
<script type="text/javascript">
            var tablaNumeracion = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaNumeracion'));
            
            var g = tablaNumeracion.addGrupoColumna('Número actual');
            // DATOS DE IDENTIFICACION
            g.addColumna('90',null,'Número');
            g.addColumna('90',null,'Letra');
            // DATOS DE INFORMACION
            g = tablaNumeracion.addGrupoColumna('Nuevo número');
            g.addColumna('90',null,'Número');
            g.addColumna('90',null,'Letra');
            
            tablaNumeracion.displayCabecera = true;
            
            function refresca(tabla){
                tabla.displayTabla();
            }
            
            function callFromTableTo(rowID,tableName){
                if(tablaNumeracion.id == tableName){
                }
            }

<%String Agent = request.getHeader("user-agent");%>

var coordx=0;
var coordy=0;


<%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>



            function checkKeysLocal(evento,tecla){
               var teclaAuxiliar = "";
                if(window.event){
                    evento         = window.event;
                    teclaAuxiliar =  evento.keyCode;
                }else
                    teclaAuxiliar =  evento.which;
                keyDel(evento);

                if (teclaAuxiliar == 38) upDownTable(tablaNumeracion,lista,teclaAuxiliar);
                if (teclaAuxiliar == 40) upDownTable(tablaNumeracion,lista,teclaAuxiliar);
                if (teclaAuxiliar == 13) pushEnterTable(tablaNumeracion,lista);
                if (teclaAuxiliar == 1)
                {
                   if (comboTipoVia.base.style.visibility == "visible" && isClickOutCombo(comboTipoVia,coordx,coordy)) setTimeout('comboTipoVia.ocultar()',20);
                   if (comboECO.base.style.visibility == "visible" && isClickOutCombo(comboECO,coordx,coordy)) setTimeout('comboECO.ocultar()',20);
                   if (comboESI.base.style.visibility == "visible" && isClickOutCombo(comboESI,coordx,coordy)) setTimeout('comboESI.ocultar()',20);
                   if (comboNUC.base.style.visibility == "visible" && isClickOutCombo(comboNUC,coordx,coordy)) setTimeout('comboNUC.ocultar()',20);
                   if (comboVia.base.style.visibility == "visible" && isClickOutCombo(comboVia,coordx,coordy)) setTimeout('comboVia.ocultar()',20);
                }
                if (teclaAuxiliar == 9) {
                    comboTipoVia.ocultar();
                    comboESI.ocultar();
                    comboNUC.ocultar();
                    comboECO.ocultar();
                    comboVia.ocultar();
                }


            }
            
            var camposNumeracion = ['numDesdeOld','letraDesdeOld','numHastaOld','letraHastaOld',
                'numDesde','letraDesde','numHasta','letraHasta', 'incremento'];
            
            function limpiarRejillaNumeracion(){
                tablaNumeracion.lineas = new Array();
                limpiar(camposNumeracion);
                refresca(tablaNumeracion);
            }
            
            var botonModificarFila =['botonModificarFila'];
            
            function rellenarDatos(tableName,listName){
                var i = tablaNumeracion.focusedIndex;
                limpiar(camposNumeracion);
                if((i>=0)&&!tablaNumeracion.ultimoTable){
                    if(nuevaListaNumeraciones[i][6]!="0"){
                        var datos = [nuevaListaNumeraciones[i][1],nuevaListaNumeraciones[i][2],
                            nuevaListaNumeraciones[i][3],nuevaListaNumeraciones[i][4],
                            listaNueva[i][1],listaNueva[i][2],
                            listaNueva[i][3],listaNueva[i][4],"0"];
                        rellenar(datos,camposNumeracion);
                        habilitarGeneralInputs(botonesNumeracion,true);
                    }else{
                    habilitarGeneralInputs(botonesNumeracion,false);
                }
            }else{
            habilitarGeneralInputs(botonesNumeracion,false);;
        }
    }
    
    var comboTipoVia = new Combo("TVia");
    var comboECO = new Combo("ECO");
    var comboESI = new Combo("ESI");
    var comboNUC = new Combo("NUC");
    var comboVia = new Combo("Via");
    var auxCombo = 'comboVia';
    
    comboECO.change = 
    function() { 
        auxCombo='comboESI'; 
        limpiar(['codESI','descESI','codNUC','descNUC','codVia','descVia','codTVia','descTVia',
            'codViaNuevo','descViaNuevo']);		
        limpiarRejillaNumeracion();
        comboNUC.addItems([],[]);
        if(comboECO.des.value.length!=0){
            cargarListas();			
            comboVia.addItems([],[]);
        }else{
        comboESI.addItems([],[]);
        valoresPorDefecto();
    }	
}

comboESI.change = 
function() { 
    auxCombo='comboNUC'; 
    limpiar(['codNUC','descNUC','codVia','descVia','codTVia','descTVia','codViaNuevo','descViaNuevo']);
    limpiarRejillaNumeracion();
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
    limpiar(['codVia','descVia','codTVia','descTVia','codViaNuevo','descViaNuevo']);
    limpiarRejillaNumeracion();
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
    var codESI = document.forms[0].codESI.value;
    var codNUC = document.forms[0].codNUC.value;
    limpiarRejillaNumeracion();
    if(comboVia.cod.value.length!=0){
        // var i = comboVia.selectedIndex-1; // No es obligatorio
        var i = comboVia.selectedIndex;
        document.forms[0].idVia.value = viasOriginal[i][0];
        document.forms[0].codTVia.value = viasOriginal[i][1];
        document.forms[0].descTVia.value = viasOriginal[i][2];
        document.forms[0].codViaNuevo.value = codVias[i];
        /*
        var descVia1 = descVias[i].split("-");
        var descVia = Trim(descVia1[0]);
        */
        var descVia = viasOriginal[i][3];
        document.forms[0].descViaNuevo.value = descVia;
        /* Cambio combo viales *
        document.forms[0].codECO.value = codECOESIVias[i][0];
        document.forms[0].descECO.value = codECOESIVias[i][1];
        document.forms[0].codESI.value = codECOESIVias[i][2];
        document.forms[0].descESI.value = codECOESIVias[i][3];
        document.forms[0].codNUC.value = codECOESIVias[i][4];
        document.forms[0].descNUC.value = codECOESIVias[i][5];
        * Fin cambio combo viales */
        //cargarNumeraciones();
    }
}

function cargarComboBox(cod, des){
    eval(auxCombo+".addItems(cod,des)");
}

        </script>
    </body>
</html>
