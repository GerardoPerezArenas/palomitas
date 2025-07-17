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
  <title> Mantenimiento de Ecos INE </title>
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
    var listaEcosOriginal = new Array();
    var listaEcos = new Array();
    
    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS  
    function recuperaDatosIniciales(){
    <%	
      Vector listaProvincias = mantForm.getListaProvincias();
      Vector listaMunicipios = mantForm.getListaMunicipios();
      int lengthProvs = listaProvincias.size();
      int lengthMuns = listaMunicipios.size();
      int i = 0;
      String codProvincias="";
      String descProvincias="";
      for(i=0;i<lengthProvs-1;i++){
        GeneralValueObject provincias = (GeneralValueObject)listaProvincias.get(i);
        codProvincias+="\""+(String)provincias.getAtributo("codigo")+"\",";
        descProvincias+="\""+(String)provincias.getAtributo("descripcion")+"\",";
      }
      GeneralValueObject provincias = (GeneralValueObject)listaProvincias.get(i);
      codProvincias+="\""+(String)provincias.getAtributo("codigo")+"\"";
      descProvincias+="\""+(String)provincias.getAtributo("descripcion")+"\"";
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
      codProvincias = [<%=codProvincias%>];
      descProvincias = [<%=descProvincias%>];
      codMunicipios = [<%=codMunicipios%>];
      descMunicipios = [<%=descMunicipios%>];
      codMunicipiosDefecto = codMunicipios;
      descMunicipiosDefecto = descMunicipios;
    }
    
    function inicializar(){
      window.focus();
      recuperaDatosIniciales();
        valoresPorDefecto();
      pulsarCancelarBuscar();
          if(ventana=="false"){
        pleaseWait1("off",top.mainFrame);
        
           
          
        pulsarBuscar();
      }else{
        pleaseWait1("off",top1.mainFrame1);
            var parametros = self.parent.opener.xanelaAuxiliarArgs;
            rellenarCamposBusqueda(parametros);
            pulsarBuscar();
        }
    }

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
    
    // FUNCIONES DE LIMPIEZA, HABILITACION Y DESHABILITACION DE CAMPOS
    //var vectorCamposBusqueda = ["codProvincia","descProvincia","codMunicipio","descMunicipio"];
    var vectorCamposRejilla = ["codECO","descECO","codINE"];
    //var vectorCombosBusqueda = ["comboProvincia","comboMunicipio"];
    var vectorBotones = ["botonModificar","botonLimpiar"];
    
    function habilitarCamposBusqueda(habilitar){
      //habilitarGeneralCombos(vectorCombosBusqueda,habilitar);
    }

    function habilitarCamposRejilla(habilitar){
      habilitarGeneralInputs(vectorCamposRejilla,habilitar);
      habilitarGeneralInputs(vectorBotones,habilitar);
    }
    
    function limpiarFormulario(){
      limpiarCamposBusqueda();
      limpiarCamposRejilla();
      document.forms[0].fechaOperacion.value = "";
      tablaEcos.lineas = new Array();
      refresca(tablaEcos);
    }
    
    function limpiarCamposBusqueda(){
      //limpiar(vectorCamposBusqueda);
    }
    
    function limpiarCamposRejilla(){
      var vector = [document.forms[0].codECO];
          habilitarGeneral(vector);
      limpiar(vectorCamposRejilla);
    }
    
    // FUNCIONES DE CARGA DE DATOS DINAMICA
    function cargarListaMunicipios(){	
      document.forms[0].opcion.value="cargarMunicipios";
      document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
      document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Trameros.do";
      document.forms[0].submit();
    }
   
    function cargarListaEcos(lista){
      listaEcos = lista;
      tablaEcos.lineas = lista;
      refresca(tablaEcos);
    }

    function valoresPorDefecto(){
      document.forms[0].codPais.value ="<%=ptVO.getPais()%>";
      document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
      document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
      document.forms[0].codMunicipio.value ="<%=ptVO.getMunicipio()%>";
      document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";
      codMunicipios = codMunicipiosDefecto;
      descMunicipios = descMunicipiosDefecto;
      //comboProvincia.addItems(codProvincias,descProvincias);
      //comboMunicipio.addItems(codMunicipios,descMunicipios);
    }

    function rellenarCamposBusqueda(datos){
      //rellenar(datos,vectorCamposBusqueda);
    }
    
    
    // FUNCIONES DE VALIDACION DE CAMPOS
    function validarCamposBusqueda(){
      var pais = document.forms[0].codPais.value;
      var provincia = document.forms[0].codProvincia.value;
      var municipio = document.forms[0].codMunicipio.value;
      if((pais!="")&&(provincia!="")&&(municipio!=""))
        return true;
      return false;
    }

    function validarCamposRejilla(){
      var codECO = document.forms[0].codECO.value;
      var codINE = document.forms[0].codINE.value;
      var descECO = document.forms[0].descECO.value;
      var generarOpe = document.forms[0].generarOperaciones.checked;
      var fechaOpe = document.forms[0].fechaOperacion.value;
      if((codECO!="")&&(codINE!="")&&(descECO!="")&&
        ((generarOpe && fechaOpe!="")||!generarOpe))
        return true;
      return false;
    }

    function haCambiadoValor(){
      var i= tablaEcos.focusedIndex;
      var codINE = document.forms[0].codINE.value;
      var descECO = document.forms[0].descECO.value;
      if((codINE!=listaEcos[i][1])||(descECO!=listaEcos[i][2]))
        return true;
      return false;
    }

    function noEsta(indice){
      var cod = document.forms[0].codINE.value;
      for(i=0;(i<listaEcos.length);i++){
        if(i!=indice){
          if((listaEcos[i][1]) == cod)
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
      //var botonBuscar = ["botonBuscar"];
      if(validarCamposBusqueda()){
        document.forms[0].opcion.value="cargarEcos";
        document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Ecos.do";
        document.forms[0].submit();
        //habilitarGeneralInputs(botonBuscar,false);
        habilitarCamposBusqueda(false);
        habilitarCamposRejilla(true);
      }else
        jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
    }
    
    function pulsarModificar(){
      if(filaSeleccionada(tablaEcos)){
        if(validarCamposRejilla()){
          if(haCambiadoValor()){
            if(noEsta(tablaEcos.selectedIndex)){
              var vector = [document.forms[0].codECO];
              habilitarGeneral(vector);
              document.forms[0].situacion.value = "";
              habilitarCamposBusqueda(true);
              document.forms[0].opcion.value="modificarEcoTerritorio";
              document.forms[0].target="oculto";
              document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Ecos.do";
              document.forms[0].submit();
              habilitarCamposBusqueda(false);
              habilitarGeneral(vector);
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
      //var botonBuscar = ["botonBuscar"];
      //habilitarGeneralInputs(botonBuscar,true);
      habilitarCamposBusqueda(true);
        habilitarCamposRejilla(false);
        valoresPorDefecto();
    }
    
    function pulsarLimpiar(){
      limpiarCamposRejilla();
      document.forms[0].fechaOperacion.value = "";
      if(tablaEcos.selectedIndex != -1 ) {
          tablaEcos.selectLinea(tablaEcos.selectedIndex);
          tablaEcos.selectedIndex = -1;
        }
    }
    
    function pulsarSalir(){
      document.forms[0].opcion.value="inicializarTerc";
      document.forms[0].target="mainFrame";
      document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
      document.forms[0].submit();
    }
  </script>
</head>

<body class="bandaBody" onLoad="inicializar();">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
  
<form action="" method="get" name="formulario" target="_self">
<input type="hidden" name="opcion">
<input type="hidden" name="tipo_select"   value="">
<input type="hidden" name="col_cod"   value="">
<input type="hidden" name="col_desc"   value="">
<input type="hidden" name="nom_tabla"   value="">
<input type="hidden" name="input_cod"   value="">
<input type="hidden" name="input_desc"   value="">
<input type="hidden" name="target1"  value="">
<input type="hidden" name="column_valor_where" value="">
<input type="hidden" name="codPais" size="3" value="<%=ptVO.getPais()%>">
<input type="hidden" name="codECOAntiguo" value="">
<input type="hidden" name="ventana" value="<%=mantForm.getVentana()%>">
<input type="hidden" name="codProvincia" value="<%=codProvincias%>">
<input type="hidden" name="descProvincia" value="<%=descProvincias%>">
<input type="hidden" name="codMunicipio" value="<%=codMunicipios%>">
<input type="hidden" name="descMunicipio" value="<%=descMunicipios%>">
<input type="hidden" name="situacion" value="">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantEcos")%></div>
<div class="contenidoPantalla">
    <table width="100%">
      <tr>
        <td> 
          <table width="100%" align="center" rules="cols" border="0" cellspacing="0" cellpadding="0" class="fondoCab">
            <tr>
              <td id="tablaEcos" align="center">
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td> 
          <table width="100%" align="center" rules="cols"  border="0" align="center"  cellpadding="0" cellspacing="0">
            <tr>
              <td width="55px"  align="center">
                <input name="codECO" type="text" class="inputTextoObligatorio" id="numero" size=5
                  maxlength=5 
                  onKeyPress = "javascript:return SoloDigitos(event);">	
              </td>
              <td width="55px" align="center">
                <input name="codINE" type="text" class="inputTextoObligatorio" size=2 maxlength=2
                  onKeyPress="javascript:return SoloDigitos(event);">
              </td>
              <td width="230px">
                <input name="descECO" type="text" class="inputTextoObligatorio" size=30 maxlength=25
                  onKeyPress="javascript:PasaAMayusculas(event);">
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
    <div id="tablaBotones" class="botoneraPrincipal">
        <input type="button" class="botonGeneral"  name="botonModificar" onClick="pulsarModificar();" 
            accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>">
        <input type="button" class="botonGeneral"  name="botonLimpiar" onClick="pulsarLimpiar();" 
            accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>">
        <input type="button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" 
            accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>">
    </div>		
</div>		

</form>

<script type="text/javascript">
  var indice;
  var tablaEcos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaEcos"));
  tablaEcos.addColumna("50","center",'<%=descriptor.getDescripcion("gEtiq_Codigo")%>');
  tablaEcos.addColumna("50","left",'<%=descriptor.getDescripcion("gEtiq_CodigoINE")%>');
  tablaEcos.addColumna("235","left",'<%=descriptor.getDescripcion("gEtiq_NomOficial")%>');
  tablaEcos.addColumna("0","left",'<%=descriptor.getDescripcion("gEtiq_NomOficial")%>');
  tablaEcos.addColumna("0","left",'<%=descriptor.getDescripcion("gEtiq_NomOficial")%>');
  tablaEcos.displayCabecera=true;
  tablaEcos.displayTabla();
  
  function refresca(tabla){
    tabla.displayTabla();
  }

  function rellenarDatos(tableName,rowID){
    if(tablaEcos==tableName){
      var i=rowID;
      indice = rowID;
      limpiarCamposRejilla();
      if((i>=0)&&!tablaEcos.ultimoTable){
        var vector = [document.forms[0].codECO];
      deshabilitarGeneral(vector);
    var vectorDatosRejilla = [listaEcosOriginal[i][3],listaEcosOriginal[i][4],
          listaEcosOriginal[i][6]];
        rellenar(vectorDatosRejilla,vectorCamposRejilla);
        document.forms[0].codECOAntiguo.value = listaEcosOriginal[i][3];
      }
    }
  } 

  // FUNCION DE CONTROL DE TECLAS
  document.onmouseup = checkKeys; 

  function checkKeysLocal(evento,tecla){
        var teclaAuxiliar;
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

    //** Esta funcion se debe implementar en cada JSP para particularizar  **//
    //** las acciones a realizar de las distintas combinaciones de teclas  **//
    keyDel(evento);
    if(teclaAuxiliar == 40){
      upDownTable(tablaEcos,listaEcos,teclaAuxiliar);
    }
    if(teclaAuxiliar == 38){
      upDownTable(tablaEcos,listaEcos,teclaAuxiliar);
    }  
  }


  /*// COMBOS
  var comboProvincia = new Combo("Provincia");
  var comboMunicipio = new Combo("Municipio");
  
  var auxCombo = "comboMunicipio";
  
  comboProvincia.change = 
    function() { 
      auxCombo="comboMunicipio"; 
      limpiar(["codMunicipio","descMunicipio"]);
      if(comboProvincia.des.value.length!=0){
        cargarListaMunicipios();
      }else{
        comboMunicipio.addItems([],[]);
      }		
    } 
  
  function cargarComboBox(cod, des){
    eval(auxCombo+".addItems(cod,des)");
  }*/
</script>
</body>
</html>
