<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<html>
<head>
<title>Búsqueda</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  ParametrosTerceroValueObject ptVO = null;
  int idioma=1;
  int apl=3;
  int cod_org=1;
  int cod_dep=1;
  int entCod = 1;
  if (session.getAttribute("usuario") != null){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    ptVO = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
    cod_org= usuarioVO.getUnidadOrgCod();
    cod_dep= usuarioVO.getDepCod();
    entCod = usuarioVO.getEntCod();
  }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/domlay.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/estilo.css" type="text/css">
<script type="text/javascript" src="<html:rewrite page='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listaBusquedaTerceros.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/seleccionBusquedaTerceros.js'/>"></script>

<script type="text/javascript">
var ventana = true;
var tipo;

  function cargarListaTerceros(){
    if(Terceros.length>0){
      lista = new Array();
      for (var i=0; i < Terceros.length; i++){
        lista[i]  = [Terceros[i][3],Terceros[i][4],Terceros[i][5],Terceros[i][6],
          Terceros[i][9],Terceros[i][10]];
      }
      tab.lineas=lista;
      refresco();
      var frameOculto = "oculto";
      if(ventana)
        frameOculto = "oculto";
    }else jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoRegCoinc")%>');
  }

  function cerrar(){
    var retorno;
    if(tipo=="TERCEROS"){
      var array = new Array();
      if((Terceros.length>0)&&(indice>=0))
        array = ["",[Terceros[indice]]];
      if(nuevo)
        array = ["NUEVO"];
        self.parent.opener.retornoXanelaAuxiliar(array);
    }else
      self.parent.opener.retornoXanelaAuxiliar(TerceroSel);
  }

  function cambiarSituacion(i){
    var situacion = Terceros[i][12];
    if(situacion=="B"){
      if(jsp_alerta("",'<%=descriptor.getDescripcion("msjUsuarBaja")%>')){
        document.forms[0].situacion.value="A";
        document.forms[0].opcion.value="cambiaSituacionTercero";
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
        document.forms[0].submit();
        Terceros[i][12]="A";
      }else{
        //borrarInteresado();
      }
    }else{
      domlay("desplegable",0,0,0,null);
      capaVisible = false;
    }
  }

  function cargaDomicilios(idTercero){
    listaDoms = new Array();
    document.forms[0].txtIdTercero.value = Terceros[idTercero][0];
    cambiarSituacion(idTercero);
    if(Terceros[idTercero][12]=="A"){
      lCasas = Terceros[idTercero][18];
      for (var i=0; i < lCasas.length; i++){
        var domicilio = "";
        domicilio = (lCasas[i][20]!="") ? domicilio+lCasas[i][20]+" ":domicilio;
        domicilio += lCasas[i][3];
        domicilio = (lCasas[i][9]!=0) ? domicilio+" "+lCasas[i][9]:domicilio;
        domicilio = (lCasas[i][10]!="") ? domicilio+" "+lCasas[i][10]+" ":domicilio;
        domicilio = (lCasas[i][11]!=0) ? domicilio+" "+lCasas[i][11]:domicilio;
        domicilio = (lCasas[i][12]!="") ? domicilio+" "+lCasas[i][12]:domicilio;
        domicilio = (lCasas[i][13]!="") ? domicilio+" Bl. "+lCasas[i][13]:domicilio;
        domicilio = (lCasas[i][14]!="") ? domicilio+" Portal "+lCasas[i][14]:domicilio;
        domicilio = (lCasas[i][15]!="") ? domicilio+" Esc. "+lCasas[i][15]:domicilio;
        domicilio = (lCasas[i][16]!="") ? domicilio+" "+lCasas[i][16]+"º ":domicilio;
        domicilio = (lCasas[i][17]!="") ? domicilio+lCasas[i][17]:domicilio;
        listaDoms[i]  = [lCasas[i][1],lCasas[i][2],domicilio,lCasas[i][4]];
      }
      tabD.lineas= listaDoms;
      refreshD();
    }
    //inicializaListaCasas(1);
  }

  function recuperaDatosIniciales(){
    ventana=true;
    var args = self.parent.opener.xanelaAuxiliarArgs;
    Terceros = args[0];
    tipo = args[1];
    if(Terceros.length>0){
      document.forms[0].cbTipoDoc.value = Terceros[0];
      document.forms[0].txtInteresado.value = Terceros[1];
      document.forms[0].txtApell1.value = Terceros[2];
      document.forms[0].txtApell2.value = Terceros[3];
      document.forms[0].txtTelefono.value = Terceros[4];
      document.forms[0].txtCorreo.value = Terceros[5];
      pulsarBuscar();
    }
    pleaseWait1("off",top.mainFrame);
  }

  function pulsarBuscar(){
    pleaseWait1("on",top.mainFrame);
    document.forms[0].opcion.value="buscarTerceros";
    if(ventana){
      document.forms[0].target="oculto";
      document.forms[0].ventana.value="true";
    }else
      document.forms[0].target="oculto";
    document.forms[0].action="<html:rewrite page='/BusquedaTerceros.do'/>";
    document.forms[0].submit();
    limpiarTodo();
  }

  function pulsarSeleccionar(){
    if(indice>=0)
      cerrar();
    else
      jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
  }

  var nuevo = false;
  function pulsarNuevo(){
    nuevo = true;
    cerrar();
  }

  function pulsarCancelar(){
    indice = -1;
    self.parent.opener.retornoXanelaAuxiliar();
  }
  
  function recuperaBusquedaTerceros(datos){
    pleaseWait1("off",top.mainFrame);
    limpiarTodo();
    Terceros = datos;
    cargarListaTerceros();
  }

  function limpiarDomicilio(){
    tabD.lineas = new Array();
    refreshD();
  }

  function limpiarTodo(){
    Terceros = new Array();
    lista = new Array();
    listaDoms = new Array();
    lCasas = new Array();
    tab.lineas = new Array();
    tabD.lineas = new Array();
    refresco();
  }

  function valoresPorDefecto(){
    document.forms[0].codPais.value ="<%=ptVO.getPais()%>";
    document.forms[0].descPais.value = "<%=ptVO.getNomPais()%>"
    document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
    document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
    document.forms[0].codMunicipio.value ="<%=ptVO.getMunicipio()%>";
    document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";
  }

	document.onmouseup = checkKeys;
	
	var pagin;
	function checkKeysLocal(evento,tecla){
      var teclaAuxiliar;
        if(window.event){
            evento = window.event;
            teclaAuxiliar  = evento.keyCode;
        }else
            teclaAuxiliar  = evento.which;

		keyDel(evento);
    if(teclaAuxiliar == 9){
      if(layerVisible) ocultarDiv();
      if(divSegundoPlano) divSegundoPlano = false;
    }
    if(teclaAuxiliar == 1){
      if(layerVisible) setTimeout("ocultarDiv()",50);
      if(capaVisible) ocultarLista();
      if(divSegundoPlano) divSegundoPlano = false;
    }
    if(teclaAuxiliar == 40){
      if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
      if(tabD==tableObject){
        upDownTable(tabD,listaDoms,teclaAuxiliar);
      }else{
        upDownTable(tab,lista,teclaAuxiliar);
      }
    }

    if(teclaAuxiliar == 38){
      if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
      if(tabD==tableObject){
        upDownTable(tabD,listaDoms,teclaAuxiliar);
      }else{
        upDownTable(tab,lista,teclaAuxiliar);
      }
    }
    if(teclaAuxiliar == 13){
      if(tabD==tableObject){
        pushEnterTable(tabD,listaDoms);
      }else{
        pushEnterTable(tab,lista);
      }
    }
    if(evento.button == 1){
      if(layerVisible) setTimeout("ocultarDiv()",50);
      if(capaVisible) ocultarLista();
      if(divSegundoPlano) divSegundoPlano = false;
    }
	}
</script>
</head>

<body class="bandaBody" onLoad="recuperaDatosIniciales();">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<form name="formulario" METHOD=POST target="_self">
<input  type="hidden" name="opcion">
<input type="hidden" name="codTerc">
<input type="hidden" name="codDomTerc">
<input type="hidden" name="numModifTerc">
<input type="hidden" name="txtIdTercero">
<input type="hidden" name="situacion">
<input type="hidden" name="txtVersion">
<input type="hidden" name="txtIdDomicilio">
<input type="hidden" name="cbTipoDoc">
<input type="hidden" name="ventana" value="false">
<INPUT TYPE="hidden" name="txtPart" class="inputTexto" SIZE=5 MAXLENGTH=5 onKeyPress="javascript:PasaAMayusculas(event);">
<INPUT TYPE="hidden" name="txtPart2" class="inputTexto" SIZE=5 MAXLENGTH=5 onKeyPress="javascript:PasaAMayusculas(event);">
<input type="hidden" name="txtTelefono">
<input type="hidden" name="txtCorreo">
<input type="hidden" name="txtInteresado">
<input type="hidden" name="txtApell1">
<input type="hidden" name="txtApell2">
<input type="hidden" name="codPais" size="3" value="<%=ptVO.getPais()%>">
<input type="hidden" name="lineasPagina" value="10">
<input type="hidden" name="pagina" value="1">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("manTer_TitDatos")%></div>
<div class="contenidoPantalla">
    <table border="0" cellpadding="1" cellspacing="1" align="center" width="675px">																				
    <tr>
      <td>
            <table  width="100%" border="0" cellspacing="0" cellpadding="0" class="fondoCab">    
            <tr>
              <td id="tabla" height="120px" align="center"></td>
            </tr>
            </table>
      </td>
    </tr>
    <tr>
      <td>
            <div id="Domicilios">
            <table  width="100%" border="0" cellspacing="0" cellpadding="0" class="fondoCab">    
            <tr>
              <td id="tablaDomicilios" height="120px" align="center"></td>
            </tr>
            </table>
            </div>
      </td>
    </tr>										
    </table>
    <div id="tabla2" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSeleccionar")%> name="cmdSeleccionar" onClick="pulsarSeleccionar();return false;">
        <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbNuevo")%> name="cmdNuevo" onClick="pulsarNuevo();return false;">
        <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar" onClick="pulsarCancelar();return false;">
    </div>
</div>
</form>

<div id="desplegable" style="overflow-y: auto; overflow-x: no; visibility: hidden; BORDER: 0px"></div>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaCombo.js"></script>

<script language="JavaScript1.2">
  var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tabla"));
	  
  tab.addColumna('100','center','<%=descriptor.getDescripcion("gEtiq_Documento")%>');
  tab.addColumna('100','center','<%=descriptor.getDescripcion("gEtiq_Nombre")%>');
  tab.addColumna('100','center','<%=descriptor.getDescripcion("gEtiqApellido1Part")%>');
  tab.addColumna('100','center','<%=descriptor.getDescripcion("gEtiqApellido2Part")%>');
  tab.addColumna('100','center','<%=descriptor.getDescripcion("gEtiqTelfFax")%>');
  tab.addColumna('100','center','<%=descriptor.getDescripcion("gEtiqEmail")%>');
  tab.displayCabecera=true;
  tab.displayTabla();

  function refresco(){
    tab.displayTabla();
    tabD.displayTabla();
  }

  var tabD = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaDomicilios"));

  tabD.addColumna('100','left','<%=descriptor.getDescripcion("gEtiq_Provincia")%>');
  tabD.addColumna('140','left','<%=descriptor.getDescripcion("gEtiq_Municipio")%>');
  tabD.addColumna('270','left','<%=descriptor.getDescripcion("gEtiq_Domicilio")%>');
  tabD.addColumna('100','center','<%=descriptor.getDescripcion("gEtiqCodPostal")%>');
  tabD.displayCabecera=true;
  tabD.displayTabla(); 

  function refreshD(){
    tabD.displayTabla();
  }

  var indice;
  
  function pintaDatos(datos){
    var i=tab.selectedIndex;
    limpiarDomicilio();
    if((i>=0)&&(!tab.ultimoTable)){
      indice=i;
      if(tipo!="SGE"){
        cargaDomicilios(i);
      }
    }
  }
  
  tab.displayDatos = pintaDatos;

  var indice1;
  
  function pintaDatos1(datos){
    var i=tabD.selectedIndex;
    if((i>=0)&&(!tabD.ultimoTable)){
      indice1=i;
    }
  }

  tabD.displayDatos = pintaDatos1;

  function callFromTableTo(rowID,tableName){
    if(tab.id == tableName){
      if(tipo=="TERCEROS"){
        var i=tab.selectedIndex;
        if(i>=0){
          indice=i;
          cerrar();
        }
      }else if(tipo=="SGE"){
        var i=tab.selectedIndex;
        if(i>=0){
          enviaSeleccion(i);
        }
      }
    }else if(tabD.id == tableName){
      if(tipo!="TERCEROS"){
        var i=tabD.selectedIndex;
        if(i>=0){
          indice1=i;
          enviaSeleccionDoms(indice,indice1);
        }
      }
    }
  }

  var tableObject=tab;
  function rellenarDatos(tableName,listName){
    tableObject = tableName;
    if(tab == tableName){
      pintaDatos(listName);
    }else if(tabD == tableName){
      pintaDatos1(listName);
    }
  }
</script>

</body>
</html>
