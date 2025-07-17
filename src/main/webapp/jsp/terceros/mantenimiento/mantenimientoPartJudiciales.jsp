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
  <title> Mantenimiento de Partidos Judiciales </title>
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
		var codProvincia = new Array();
		var descProvincia = new Array();
		var listaPartJudiciales = new Array();
		var listaPartJudicialesOriginal = new Array();
			
		// FUNCIONES DE CARGA E INICIALIZACION DE DATOS  
		function recuperaDatosIniciales()
		{
    <%	
	  Vector listaProvincias = mantForm.getListaProvincias();
      int lengthProvincias = listaProvincias.size();
      int i = 0;
      String codProvincia="";
      String descProvincia="";
	  if(lengthProvincias >0) {
	      for(i=0;i<lengthProvincias-1;i++){
	        GeneralValueObject provincias = (GeneralValueObject)listaProvincias.get(i);
	        codProvincia+="\""+(String)provincias.getAtributo("codigo")+"\",";
	        descProvincia+="\""+(String)provincias.getAtributo("descripcion")+"\",";
	      }
	      GeneralValueObject provincias = (GeneralValueObject)listaProvincias.get(i);
	      codProvincia+="\""+(String)provincias.getAtributo("codigo")+"\"";
	      descProvincia+="\""+(String)provincias.getAtributo("descripcion")+"\"";
	  }
      %>
      codProvincia = [<%=codProvincia%>];
      descProvincia = [<%=descProvincia%>];

   	}//de la funcion

		

function inicializar(){
	recuperaDatosIniciales();
	comboProvincia.addItems(codProvincia,descProvincia);
	document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
    document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
	deshabilitarFormulario1();
}//de la funcion

function pulsarBuscar() {
  if(validarCamposBusqueda()) {
	document.forms[0].opcion.value="cargarPartJudiciales";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/PartJudiciales.do";
    document.forms[0].submit();
	deshabilitarFormulario();
	habilitarFormulario1();
  } else {
    jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
  }
}

function validarCamposBusqueda() {
  var cD = document.forms[0].codProvincia.value;
  if(cD != "") {
    return true;
  }
  return false;
}

function deshabilitarFormulario() {
  comboProvincia.deactivate();
  var botonBuscar = [document.forms[0].botonBuscar];
  deshabilitarGeneral(botonBuscar);
}

function pulsarCancelarBuscar() {
  comboProvincia.activate();
  var botonBuscar = [document.forms[0].botonBuscar];
  habilitarGeneral(botonBuscar);
  deshabilitarFormulario1();
}

function deshabilitarFormulario1() {
  var vector = [document.forms[0].codPartJudicial];
  habilitarGeneral(vector);
  document.forms[0].codPartJudicial.value = "";
  document.forms[0].nombre.value = "";
  document.forms[0].nombreLargo.value = "";
  tablaPartJudiciales.lineas = new Array();
  refresca();
  var vector = [document.forms[0].botonAlta,document.forms[0].botonModificar,document.forms[0].botonBorrar,
                document.forms[0].botonLimpiar];
  deshabilitarGeneral(vector);
  var vectorCampos = [document.forms[0].codPartJudicial,document.forms[0].nombre,document.forms[0].nombreLargo];
  deshabilitarGeneral(vectorCampos);
}

function habilitarFormulario1() {
  var vector = [document.forms[0].botonAlta,document.forms[0].botonModificar,document.forms[0].botonBorrar,
                document.forms[0].botonLimpiar];
  habilitarGeneral(vector);
  var vectorCampos = [document.forms[0].codPartJudicial,document.forms[0].nombre,document.forms[0].nombreLargo];
  habilitarGeneral(vectorCampos);
}

function cargaListaPartJudiciales(list1,list2) {
  listaPartJudicialesOriginal = list1;
  listaPartJudiciales = list2;
  tablaPartJudiciales.lineas = listaPartJudiciales;
  refresca();
  var vector = [document.forms[0].codPartJudicial];
  habilitarGeneral(vector);
  document.forms[0].codPartJudicial.value = "";
  document.forms[0].nombre.value = "";
  document.forms[0].nombreLargo.value = "";
}

function pulsarAlta() {
  if(validarCamposRejilla()) {
    var cod = document.forms[0].codPartJudicial.value;
	var existe = 0;
    for(i=0;(i<listaPartJudiciales.length);i++){
       if((listaPartJudiciales[i][0]) == cod)  {
	     existe = 1;
	   }   
    }
	if(existe == 0) {
	  comboProvincia.activate();
	  document.forms[0].opcion.value="alta";
      document.forms[0].target="oculto";
      document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/PartJudiciales.do";
      document.forms[0].submit();
	  comboProvincia.deactivate();
	} else {
	  jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
	}
  } else {
    jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
  }
}

function pulsarModificar() {
  if(tablaPartJudiciales.selectedIndex != -1) {
	  if(validarCamposRejilla()) {
	    var cod = document.forms[0].codPartJudicial.value;
		var existe = 0;
	    for(i=0;(i<listaPartJudiciales.length);i++){
	       if((listaPartJudiciales[i][0]) == cod && i!=tablaPartJudiciales.selectedIndex)  {
		     existe = 1;
		   }   
	    }
		if(existe == 0) {
		  comboProvincia.activate();
		  var vector = [document.forms[0].codPartJudicial];
	      habilitarGeneral(vector);
		  document.forms[0].opcion.value="modificar";
	      document.forms[0].target="oculto";
	      document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/PartJudiciales.do";
	      document.forms[0].submit();
	      deshabilitarGeneral(vector);
		  comboProvincia.deactivate();
		} else {
		  jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
		}
	  } else {
	    jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
	  }
  } else {
    jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
  }
}

function validarCamposRejilla() {
  var c = document.forms[0].codPartJudicial.value;
  if(c != "") {
    return true;
  }
  return false;
}

function pulsarLimpiar() {
  var vector = [document.forms[0].codPartJudicial];
  habilitarGeneral(vector);
  document.forms[0].codPartJudicial.value = "";
  document.forms[0].nombre.value = "";
  document.forms[0].nombreLargo.value = "";
}

function pulsarLimpiar2() {
  pulsarLimpiar();
  if(tablaPartJudiciales.selectedIndex != -1 ) {
    tablaPartJudiciales.selectLinea(tablaPartJudiciales.selectedIndex);
    tablaPartJudiciales.selectedIndex = -1;
  }
}

function pulsarSalir()	{
	document.forms[0].opcion.value="inicializarTerc";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
    document.forms[0].submit();
}//de la funcion

function pulsarEliminar() {
  if(tablaPartJudiciales.selectedIndex != -1) {
    if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimPartJud")%>') ==1) {
		comboProvincia.activate();
	    var vector = [document.forms[0].codPartJudicial];
	    habilitarGeneral(vector);
	    document.forms[0].opcion.value="eliminar";
	    document.forms[0].target="oculto";
	    document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/PartJudiciales.do";
	    document.forms[0].submit();
	    deshabilitarGeneral(vector);
	    comboProvincia.deactivate();
		pulsarLimpiar();
		if(tablaPartJudiciales.selectedIndex != -1 ) {
		    tablaPartJudiciales.selectLinea(tablaPartJudiciales.selectedIndex);
		    tablaPartJudiciales.selectedIndex = -1;
		}
	}
  } else {
    jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
  }
}

function noEliminarPartJud() {
  jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimPartJud")%>");
}

		
  </script>
</head>

<body class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        inicializar();}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
	
<form action="" method="get" name="formulario" target="_self">
<input type="hidden" name="opcion">
<input type="hidden" name="codPais" size="3" value="<%=ptVO.getPais()%>">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantPartJud")%></div>
<div class="contenidoPantalla">
    <table width="100%">
        <tr>
            <td>
                <table width="100%" border="0px" cellspacing="0" cellpadding="0">
                    <tr>
                        <td >&nbsp;</td>
                    </tr>
                    <tr>
                        <td width="100%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Provincia")%>:</td>
                    </tr>
                    <tr>
                        <td width="100%" class="columnP">
                            <table width="100%" cellpadding="0px" cellspacing="0px">
                                <tr>
                                    <td width="40%" align="left">
                                        <input type="text" class="inputTextoObligatorio" name="codProvincia" size="2"
                                               onKeyPress = "javascript:return SoloDigitos(event);">
                                        <input type="text" class="inputTextoObligatorio" name="descProvincia" style="width:200" readonly="true">
                                    </td>
                                    <td width="10%" valign="bottom" align="left">
                                        <A href="" name="anchorProvincia" id="anchorProvincia">
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonProvincia" id="botonProvincia" style="cursor:hand;"></span>
                                        </A>
                                    </td>
                                    <td width="20%"></td>
                                    <td width="30%" align="rigth">
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
                        <tr>
                            <td >&nbsp;</td>
                        </tr>
                    </table>
                </td>
    </tr>
    <tr>
        <td id="tablaPartJudiciales"></td>
    </tr>
    <tr>
        <td> 
            <table width="100%">
              <tr>
                <td width="10%" align="center"> 
                    <input name="codPartJudicial" type="text" class="inputTextoObligatorio" size=4 maxlength=2 
                            onKeyPress = "javascript:return SoloDigitos(event);">	
                </td>
                <td width="40%"  align="center">
                    <input name="nombre" type="text" class="inputTexto" size=30 maxlength=25
                            onKeyPress = "javascript:PasaAMayusculas(event);">
                </td>
                <td width="50%"  align="center">
                        <input name="nombreLargo" type="text" class="inputTexto" size=40 maxlength=50
                                onKeyPress = "javascript:PasaAMayusculas(event);">
                </td>
            </tr>
        </table>   
      </td>
        </tr>
    </table>
    <div id="tablaBotones" class="botoneraPrincipal">
        <input type="button" class="botonGeneral"  name="botonAlta"	onClick="pulsarAlta();" 
            accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>"> 
        <input type="button" class="botonGeneral"  name="botonModificar"	onClick="pulsarModificar();" 
            accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>">
        <input type="button" class="botonGeneral" name="botonBorrar"	onClick="pulsarEliminar();" 
            accesskey="E" value="<%=descriptor.getDescripcion("gbEliminar")%>">
        <input type="button" class="botonGeneral"  name="botonLimpiar"	onClick="pulsarLimpiar2();" 
            accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>">
        <input type="button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" 
            accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>"> 
    </div>				
</div>				
</form>
<script language="JavaScript1.2">
  var indice;
  var tablaPartJudiciales = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaPartJudiciales"));
  tablaPartJudiciales.addColumna("70","center",'<%=descriptor.getDescripcion("gEtiq_Codigo")%>');
  tablaPartJudiciales.addColumna("270","left",'<%=descriptor.getDescripcion("gEtiq_NomLargo")%>');
  tablaPartJudiciales.addColumna("330","left",'<%=descriptor.getDescripcion("gEtiq_NomOficial")%>');
  tablaPartJudiciales.displayCabecera=true;
  tablaPartJudiciales.displayTabla();
  

function refresca()	{
  tablaPartJudiciales.displayTabla();
}//de la funcion

function rellenarDatos(tableName,rowID)	{
  if(rowID != -1){
     var i=rowID;
     if(i>=0){
	   var vector = [document.forms[0].codPartJudicial];
	   deshabilitarGeneral(vector);
       document.forms[0].codPartJudicial.value = listaPartJudiciales[i][0];
	   document.forms[0].nombre.value = listaPartJudiciales[i][1];
	   document.forms[0].nombreLargo.value = listaPartJudiciales[i][2];
	 }
  } 
} //de la funcion

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

	function checkKeysLocal(evento,tecla)
	{
         var teclaAuxiliar = "";
        if(window.event){
            evento         = window.event;
            teclaAuxiliar =  evento.keyCode;
        }else
            teclaAuxiliar =  evento.which;

		keyDel(evento);
	    if (teclaAuxiliar == 38) upDownTable(tablaPartJudiciales,listaPartJudiciales,teclaAuxiliar);
	    if (teclaAuxiliar == 40) upDownTable(tablaPartJudiciales,listaPartJudiciales,teclaAuxiliar);
            if (teclaAuxiliar == 1) {if (comboProvincia.base.style.visibility == "visible" && isClickOutCombo(comboProvincia,coordx,coordy)) setTimeout('comboProvincia.ocultar()',20); }
            if (teclaAuxiliar == 9) comboProvincia.ocultar();
	}//de la funcion
	
	var comboProvincia = new Combo("Provincia");	
	
	
</script>
</body>
</html>
