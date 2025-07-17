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
  <title> Mantenimiento de Nucleos INE </title>
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
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
  <script type="text/javascript">
    // VARIABLES GLOBALES
    <% 
      MantenimientosTercerosForm mantForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
    %>  
    var ventana = "<%=mantForm.getVentana()%>";
    var codProvincias = new Array();
    var descProvincias = new Array();
    var codMunicipios = new Array();
    var descMunicipios = new Array();
    var codMunicipiosDefecto = new Array();
    var descMunicipiosDefecto = new Array();
    var codESIs = new Array();
    var descESIs = new Array();
    var codESIsDefecto = new Array();
    var descESIsDefecto = new Array();
    var listaNucleosOriginal = new Array();
    var listaNucleos = new Array();
    var paisOld = "";
    var provinciaOld = "";
    var municipioOld = "";
  
    /**************  FUNCIONES PARA LA CARGA DE LOS CALENDARIOS ***********************/
    function mostrarCalendario(img,campoFecha){
      var indice = document.getElementById(img).src.indexOf('fa-calendar');
      if (indice!=-1)
        showCalendar('forms[0]',campoFecha,null,null,null,'',img,'',null,null,null,
          null,null,null,null,'');
    }//de la funcion

    //Funcion para la verificación de un campo fecha
    function comprobarFecha(inputFecha){
      if(Trim(inputFecha.value)!=''){
        if(!ValidarFechaConFormato(document.forms[0],inputFecha)){
          jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
          inputFecha.focus();
          return false;
        }
      }
      return true;
    }//de la funcion

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS  
    function recuperaDatosIniciales(){
    <%	
      Vector listaProvincias = mantForm.getListaProvincias();
      Vector listaMunicipios = mantForm.getListaMunicipios();
      Vector listaEsis = mantForm.getListaEsis();
      int lengthProvs = listaProvincias.size();
      int lengthMuns = listaMunicipios.size();
      int lengthEsis = listaEsis.size();
      int i = 0;
    %>
      var j=0;
    <%for(i=0;i<lengthProvs;i++){%>
      codProvincias[j] = "<%=(String)((GeneralValueObject)listaProvincias.get(i)).getAtributo("codigo")%>";
      descProvincias[j] = "<%=(String)((GeneralValueObject)listaProvincias.get(i)).getAtributo("descripcion")%>";
      j++;
    <%}%>
      j=0;
    <%for(i=0;i<lengthMuns;i++){%>
      codMunicipios[j] = "<%=(String)((GeneralValueObject)listaMunicipios.get(i)).getAtributo("codMunicipio")%>";
      descMunicipios[j] = "<%=(String)((GeneralValueObject)listaMunicipios.get(i)).getAtributo("nombreOficial")%>";
      j++;
    <%}%>
      j=0;
    <%for(i=0;i<lengthEsis;i++){%>
      codESIs[j] = "<%=(String)((GeneralValueObject)listaEsis.get(i)).getAtributo("codEntidadSingular")%>";
      descESIs[j] = "<%=(String)((GeneralValueObject)listaEsis.get(i)).getAtributo("nombreOficial")%>";
      j++;
    <%}%>
      codMunicipiosDefecto = codMunicipios;
      descMunicipiosDefecto = descMunicipios;
      codESIsDefecto = codESIs;
      descESIsDefecto = descESIs;
    }
    
  function inicializar(){
    window.focus();
    recuperaDatosIniciales();
      valoresPorDefecto();
    pulsarCancelarBuscar();
      if(ventana=="false"){
      pleaseWait1("off",top.mainFrame);
    
      
    }else{
    pleaseWait1("off",top1.mainFrame1);
        var parametros = self.parent.opener.xanelaAuxiliarArgs;
        rellenarCamposBusqueda(parametros);
        pulsarBuscar();
      }
  }
    
    // FUNCIONES DE LIMPIEZA, HABILITACION Y DESHABILITACION DE CAMPOS
    var vectorCamposBusqueda = ["codESI","descESI"];
    var vectorCamposRejilla = ["codNUC","codINE","descNUC"];
    //var vectorCombosBusqueda = ["comboESI"];
        var vectorBotones = ["botonModificar","botonLimpiar"];
    
    /*function habilitarCamposBusqueda(habilitar){
      habilitarGeneralCombos(vectorCombosBusqueda,habilitar);
    }*/

    function habilitarCamposRejilla(habilitar){
      habilitarGeneralInputs(vectorCamposRejilla,habilitar);
      habilitarGeneralInputs(vectorBotones,habilitar);
    }
    
    function limpiarFormulario(){
      limpiarCamposBusqueda();
      limpiarCamposRejilla();
      document.forms[0].fechaOperacion.value = "";
      tablaNucleos.lineas = new Array();
      refresca(tablaNucleos);
    }
    
    function limpiarCamposBusqueda(){
      limpiar(vectorCamposBusqueda);
    }
    
    function limpiarCamposRejilla(){
      var vector = [document.forms[0].codNUC];
          habilitarGeneral(vector);
      limpiar(vectorCamposRejilla);
    }
    
    // FUNCIONES DE CARGA DE DATOS DINAMICA
    function cargarListaMunicipios(){	
      document.forms[0].opcion.value="cargarMunicipios";
      document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
      document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Nucleos.do";
      document.forms[0].submit();
    }
   
    function cargarListaESIs(){
      document.forms[0].opcion.value="cargarEsis";
      document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
      document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Nucleos.do";
      document.forms[0].submit();
    }
      
    function cargarListaNucleos(lista){
      listaNucleos = lista;
      tablaNucleos.lineas = lista;
      refresca(tablaNucleos);
    }

    function valoresPorDefecto(){
      document.forms[0].codPais.value ="<%=ptVO.getPais()%>";
      document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
      document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
      document.forms[0].codMunicipio.value ="<%=ptVO.getMunicipio()%>";
      document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";
      codMunicipios = codMunicipiosDefecto;
      descMunicipios = descMunicipiosDefecto;
      codESIs = codESIsDefecto;
      descESIs = descESIsDefecto;
      //comboProvincia.addItems(codProvincias,descProvincias);
      //comboMunicipio.addItems(codMunicipios,descMunicipios);
      comboESI.addItems(codESIs,descESIs);
    }

    function rellenarCamposBusqueda(datos){
      rellenar(datos,vectorCamposBusqueda);
    }
    
    // FUNCIONES DE VALIDACION DE CAMPOS
    function validarCamposBusqueda(){
      var pais = document.forms[0].codPais.value;
      var provincia = document.forms[0].codProvincia.value;
      var municipio = document.forms[0].codMunicipio.value;
      var codESI = document.forms[0].codESI.value;
      if((pais!="")&&(provincia!="")&&(municipio!="")&&(codESI!=""))
        return true;
      return false;
    }

    function validarCamposRejilla(){
      var codNUC = document.forms[0].codNUC.value;
      var codINE = document.forms[0].codINE.value;
      var descNUC = document.forms[0].descNUC.value;
      var generarOpe = document.forms[0].generarOperaciones.checked;
      var fechaOpe = document.forms[0].fechaOperacion.value;
      if((codNUC!="")&&(codINE!="")&&(descNUC!="")&&((generarOpe && fechaOpe!="")||!generarOpe))
        return true;
      return false;
    }

    function haCambiadoValor(){
      var i= tablaNucleos.focusedIndex;
      var codINE = document.forms[0].codINE.value;
      var descNUC = document.forms[0].descNUC.value;
      if((codINE!=listaNucleos[i][1])||(descNUC!=listaNucleos[i][2]))
        return true;
      return false;
    }

    
    function noEsta(indice){
      var cod = document.forms[0].codNUC.value;
      for(i=0;(i<listaNucleos.length);i++){
        if(i!=indice){
          if((listaNucleos[i][1]) == cod)
            return false;
        }
      }
      return true;
    }
    
    function filaSeleccionada(tabla){
      var i = tabla.selectedIndex;
      if((i>=0)&&(!tabla.ultimoTable))
          return true;
      return false;
    }

    // FUNCIONES DE PULSACION DE BOTONES
    function pulsarBuscar(){
      var botonBuscar = ["botonBuscar"];
      if(validarCamposBusqueda()){
        document.forms[0].opcion.value="cargarNucleos";
        document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Nucleos.do";
        document.forms[0].submit();
        habilitarGeneralInputs(botonBuscar,false);
        //habilitarCamposBusqueda(false);
        comboESI.deactivate();
        habilitarCamposRejilla(true);
      }else
        jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
    }
    
    function pulsarModificar(){
      if(filaSeleccionada(tablaNucleos)){
        if(validarCamposRejilla()){
          if(haCambiadoValor()){
            if(noEsta(tablaNucleos.selectedIndex)){
              var vector = [document.forms[0].codNUC];
              habilitarGeneral(vector);
              //habilitarCamposBusqueda(true);
              comboESI.activate();
              document.forms[0].opcion.value="modificarNucTerritorio";
              document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
              document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Nucleos.do";
              document.forms[0].submit();
              //habilitarCamposBusqueda(false);
              comboESI.deactivate();
              deshabilitarGeneral(vector);
              limpiarCamposRejilla();
            }else{
              jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
            }
          }else{
            jsp_alerta("A","No se ha producido ningún cambio"); 
          }
        }else
          jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
      }else
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>")
    }


    function pulsarCancelarBuscar(){
      limpiarFormulario();
      var botonBuscar = ["botonBuscar"];
      habilitarGeneralInputs(botonBuscar,true);
      //habilitarCamposBusqueda(true);
      comboESI.activate();
      habilitarCamposRejilla(false);
            valoresPorDefecto();
    }
    
    function pulsarLimpiar(){
      document.forms[0].fechaOperacion.value = "";
      limpiarCamposRejilla();
      if(tablaNucleos.selectedIndex != -1 ) {
          tablaNucleos.selectLinea(tablaNucleos.selectedIndex);
          tablaNucleos.selectedIndex = -1;
        }
    }
    
    function pulsarSalir(){
      if(ventana=="false"){
        document.forms[0].opcion.value="inicializarTerc";
            document.forms[0].target="mainFrame";
            document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
            document.forms[0].submit();
      }else{
        var datosRetorno;
        if(indice>-1)
          datosRetorno = listaNucleosOriginal[indice];
        self.parent.opener.retornoXanelaAuxiliar(datosRetorno);
      }
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
<input type="hidden" name="codNUCAntiguo" value="">
<input type="hidden" name="ventana" value="<%=mantForm.getVentana()%>">
<input type="hidden" name="codProvincia" value="<%=ptVO.getProvincia()%>">
<input type="hidden" name="descProvincia" value="<%=ptVO.getNomProvincia()%>%>">
<input type="hidden" name="codMunicipio" value="<%=ptVO.getMunicipio()%>">
<input type="hidden" name="descMunicipio" value="<%=ptVO.getNomMunicipio()%>">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantNucleos")%></div>
<div class="contenidoPantalla">
    <table width="100%" border="0px" cellspacing="0" cellpadding="0" align="center">
      <tr>
        <td>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="100%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_ESI")%></td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="etiqueta">
                      <input name="codESI" type="text" class="inputTextoObligatorio" id="codESI" size="3"> 
                          <input name="descESI" type="text" class="inputTextoObligatorio" id="descESI" style="width:200" 
                                        readonly> 
                          <a id="anchorESI" name="anchorESI" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonESI" name="botonESI"
                                        style="cursor:hand;"></span></a>
                    </td>
                            <td align="right">
                                  <input name="botonBuscar" type="button"  class="boton" id="botonBuscar" 
                        value="<%=descriptor.getDescripcion("gbBuscar")%>"
                        onClick="pulsarBuscar();" accesskey="B">
                      <input name="botonCancelar" type="button" class="boton" id="botonCancelar"
                        value="<%=descriptor.getDescripcion("gbCancelar")%>"
                        onClick="pulsarCancelarBuscar();" accesskey="C">
                             </td>
                         </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>
          <table width="50%" align="center" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td> 
                <table width="100%" rules="cols" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td id="tablaNucleos"></td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td> 
                <table width="100%" rules="cols" border="0" align="center"  cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="50px" align="right">
                      <input name="codNUC" type="text" class="inputTextoObligatorio" id="numero" size=5
                        maxlength=5 onKeyPress = "javascript:return SoloDigitos(event);">	
                    </td>
                    <td width="50px" align="center">
                      <input name="codINE" type="text" class="inputTextoObligatorio" size=4 maxlength=2
                        onKeyPress="javascript:return SoloDigitos(event);">
                    </td>
                    <td width="210px" align="center">
                      <input name="descNUC" type="text" class="inputTextoObligatorio" size=32 maxlength=30
                        onKeyPress = "javascript:PasaAMayusculas(event);">
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>
                <table width="100%">
                  <tr>
                    <td class="etiqueta">
                      <input name="generarOperaciones" type="checkbox" value="SI" checked>
                      Generar operaciones
                    </td>
                    <td>
                      <input NAME="fechaOperacion" TYPE="text" class='inputTxtFecha' id="fechaOperacion"
                        onfocus = "this.select();"
                        onkeypress = "javascript:return soloCaracteresFecha(event);" size=12>   
                      <span class="fa fa-calendar" aria-hidden="true" name="calendario2" 
                        onClick="calClick();mostrarCalendario('calendario2','fechaOperacion');return false;" 
                        style="cursor:hand"></span> 
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
    <div class="botoneraPrincipal" id="tablaBotones">
                                <input type="button" class="botonGeneral"  name="botonModificar"	onClick="pulsarModificar();" 
            accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>"> 
                          <input type="button" class="botonGeneral"  name="botonLimpiar"	onClick="pulsarLimpiar();" 
            accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>">
                          <input type="button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" 
            accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>">
    </div>		
</div>		
</form>

<script type="text/javascript">
  var indice;
  var tablaNucleos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaNucleos"));
  
  
  tablaNucleos.addColumna("50","center",'<%= descriptor.getDescripcion("gEtiq_Codigo")%>');
  tablaNucleos.addColumna("50","left",'<%=descriptor.getDescripcion("gEtiq_CodigoINE")%>');
  tablaNucleos.addColumna("210","left",'<%=descriptor.getDescripcion("gEtiq_NomOficial")%>');
  tablaNucleos.addColumna("0","left",'<%=descriptor.getDescripcion("gEtiq_NomLargo")%>');
  tablaNucleos.addColumna("0","left",'<%=descriptor.getDescripcion("gEtiq_IdObjGrafico")%>');
  tablaNucleos.addColumna("0","left",'<%=descriptor.getDescripcion("gEtiq_Situacion")%>');
  
  tablaNucleos.displayCabecera=true;
  tablaNucleos.displayTabla();
  
  function refresca(tabla){
    tabla.displayTabla();
  }

  function rellenarDatos(tableName,rowID){
    if(tablaNucleos==tableName){
      var i=rowID;
      indice = rowID;
      limpiarCamposRejilla();
      if((i>=0)&& (!tableName.ultimoTable)){
        var vector = [document.forms[0].codNUC];
      deshabilitarGeneral(vector);
    var vectorDatosRejilla = [listaNucleosOriginal[i][4],listaNucleosOriginal[i][7],
          listaNucleosOriginal[i][5]];
        rellenar(vectorDatosRejilla,vectorCamposRejilla);
        document.forms[0].codNUCAntiguo.value = listaNucleosOriginal[i][4];
      }
    }
  }	//Se ejecuta al cambiar la selección en una Tabla Simple. 

  
  // FUNCION DE CONTROL DE TECLAS
  document.onmouseup = checkKeys; 

  function checkKeysLocal(evento,tecla){
      var teclaAuxiliar;
    if(window.event){
        evento = window.event;
        teclaAuxiliar  = evento.keyCode;
    }else
        teclaAuxiliar  = evento.which;

    //** Esta funcion se debe implementar en cada JSP para particularizar  **//
    //** las acciones a realizar de las distintas combinaciones de teclas  **//
    keyDel(evento);
    if(teclaAuxiliar == 40){
      upDownTable(tablaNucleos,listaNucleos,teclaAuxiliar);
    }
    
    if(teclaAuxiliar == 38){
      upDownTable(tablaNucleos,listaNucleos,teclaAuxiliar);
    }  
  }

  // COMBOS
  //var comboProvincia = new Combo("Provincia");
  //var comboMunicipio = new Combo("Municipio");
  var comboESI = new Combo("ESI");
  //var auxCombo = "comboMunicipio";
  
  /*comboProvincia.change = 
    function() { 
      auxCombo="comboMunicipio"; 
      limpiar(["codMunicipio","descMunicipio","codESI","descESI"]);
      if(comboProvincia.des.value.length!=0){
        cargarListaMunicipios();
        comboESI.addItems([],[]);
      }else{
        comboMunicipio.addItems([],[]);
        comboESI.addItems([],[]);
      }		
    } 
  
  comboMunicipio.change = 
    function() { 
      auxCombo = "comboESI";
      limpiar(["codESI","descESI"]);
      if(comboMunicipio.des.value.length!=0){
        cargarListaESIs();        
      }else{
        comboESI.addItems([],[]);
      }		
    }*/
  
  function cargarComboBox(cod, des){
    eval(auxCombo+".addItems(cod,des)");
  }
</script>
</body>
</html>
