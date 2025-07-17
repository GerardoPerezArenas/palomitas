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
  <title> Mantenimiento de Manzanas </title>
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
		var codDistritos = new Array();
		var desDistritos = new Array();
		var codSecciones = new Array();
		var descSecciones = new Array();
		var cSecciones = new Array();
		var lSecciones = new Array();
		var listaManzanas = new Array();
		var listaManzanasOriginal = new Array();
			
		// FUNCIONES DE CARGA E INICIALIZACION DE DATOS  
		function recuperaDatosIniciales()
		{
    <%	
			Vector listaDistritos = mantForm.getListaDistritos();
      int lengthDistritos = listaDistritos.size();
      int i = 0;
      String codDistritos="";
      String descDistritos="";
	  if(lengthDistritos >0) {
	      for(i=0;i<lengthDistritos-1;i++){
	        GeneralValueObject distritos = (GeneralValueObject)listaDistritos.get(i);
	        codDistritos+="\""+(String)distritos.getAtributo("codDistrito")+"\",";
	        descDistritos+="\""+(String)distritos.getAtributo("descDistrito")+"\",";
	      }
	      GeneralValueObject distritos = (GeneralValueObject)listaDistritos.get(i);
	      codDistritos+="\""+(String)distritos.getAtributo("codDistrito")+"\"";
	      descDistritos+="\""+(String)distritos.getAtributo("descDistrito")+"\"";
	  }
      %>
      codDistritos = [<%=codDistritos%>];
      descDistritos = [<%=descDistritos%>];

   	}//de la funcion

		

function inicializar(){
	recuperaDatosIniciales();
	comboDistrito.addItems(codDistritos,descDistritos);
	deshabilitarFormulario1();
}//de la funcion

function cargarListaSecciones() {
  document.forms[0].opcion.value="cargarSecciones";
  document.forms[0].target="oculto";
  document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Manzanas.do";
  document.forms[0].submit();
}

function cargaListaSecciones(list1,list2,list3,list4) {
  codSecciones = list1;
  descSecciones = list2;
  cSecciones = list3;
  lSecciones = list4;
  comboSeccion.addItems(codSecciones,descSecciones);
}

function pulsarBuscar() {
  if(validarCamposBusqueda()) {
	document.forms[0].cSeccion.value = cSecciones[comboSeccion.selectedIndex][0];
	document.forms[0].letraSeccion.value = lSecciones[comboSeccion.selectedIndex][0];
	document.forms[0].opcion.value="cargarManzanas";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Manzanas.do";
    document.forms[0].submit();
	deshabilitarFormulario();
	habilitarFormulario1();
  } else {
    jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
  }
}

function validarCamposBusqueda() {
  var cD = document.forms[0].codDistrito.value;
  var cS = document.forms[0].codSeccion.value;
  if(cD != "" && cS != "") {
    return true;
  }
  return false;
}

function deshabilitarFormulario() {
  comboDistrito.deactivate();
  comboSeccion.deactivate();
  var botonBuscar = [document.forms[0].botonBuscar];
  deshabilitarGeneral(botonBuscar);
}

function pulsarCancelarBuscar() {
  comboDistrito.activate();
  comboSeccion.activate();
  document.forms[0].codDistrito.value = "";
  document.forms[0].descDistrito.value = "";
  document.forms[0].codSeccion.value = "";
  document.forms[0].descSeccion.value = "";
  var botonBuscar = [document.forms[0].botonBuscar];
  habilitarGeneral(botonBuscar);
  deshabilitarFormulario1();
}

function deshabilitarFormulario1() {
  var vector = [document.forms[0].codManzana];
  habilitarGeneral(vector);
  document.forms[0].codManzana.value = "";
  document.forms[0].descManzana.value = "";
  tablaManzanas.lineas = new Array();
  refresca();
  var vector = [document.forms[0].botonAlta,document.forms[0].botonModificar,document.forms[0].botonBorrar,
                document.forms[0].botonLimpiar];
  deshabilitarGeneral(vector);
  var vectorCampos = [document.forms[0].codManzana,document.forms[0].descManzana];
  deshabilitarGeneral(vectorCampos);
}

function habilitarFormulario1() {
  var vector = [document.forms[0].botonAlta,document.forms[0].botonModificar,document.forms[0].botonBorrar,
                document.forms[0].botonLimpiar];
  habilitarGeneral(vector);
  var vectorCampos = [document.forms[0].codManzana,document.forms[0].descManzana];
  habilitarGeneral(vectorCampos);
}

function cargaListaManzanas(list1,list2) {
  listaManzanasOriginal = list1;
  listaManzanas = list2;
  tablaManzanas.lineas = listaManzanas;
  refresca();
  var vector = [document.forms[0].codManzana];
  habilitarGeneral(vector);
  document.forms[0].codManzana.value = "";
  document.forms[0].descManzana.value = "";
}

function pulsarAlta() {
  if(validarCamposRejilla()) {
    var cod = document.forms[0].codManzana.value;
	var existe = 0;
    for(i=0;(i<listaManzanas.length);i++){
       if((listaManzanas[i][0]) == cod)  {
	     existe = 1;
	   }   
    }
	if(existe == 0) {
	  comboDistrito.activate();
	  comboSeccion.activate();
	  document.forms[0].opcion.value="alta";
      document.forms[0].target="oculto";
      document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Manzanas.do";
      document.forms[0].submit();
	  comboDistrito.deactivate();
	  comboSeccion.deactivate();
	} else {
	  jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
	}
  } else {
    jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
  }
}

function pulsarModificar() {
  if(tablaManzanas.selectedIndex != -1) {
	  if(validarCamposRejilla()) {
	    var cod = document.forms[0].codManzana.value;
		var existe = 0;
	    for(i=0;(i<listaManzanas.length);i++){
	       if((listaManzanas[i][0]) == cod && i!=tablaManzanas.selectedIndex)  {
		     existe = 1;
		   }   
	    }
		if(existe == 0) {
		  comboDistrito.activate();
		  comboSeccion.activate();
		  var vector = [document.forms[0].codManzana];
	      habilitarGeneral(vector);
		  document.forms[0].opcion.value="modificar";
	      document.forms[0].target="oculto";
	      document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Manzanas.do";
	      document.forms[0].submit();
	      deshabilitarGeneral(vector);
		  comboDistrito.deactivate();
		  comboSeccion.deactivate();
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
  var c = document.forms[0].codManzana.value;
  if(c != "") {
    return true;
  }
  return false;
}

function pulsarLimpiar() {
  var vector = [document.forms[0].codManzana];
  habilitarGeneral(vector);
  document.forms[0].codManzana.value = "";
  document.forms[0].descManzana.value = "";
}

function pulsarLimpiar2() {
  pulsarLimpiar();
  if(tablaManzanas.selectedIndex != -1 ) {
    tablaManzanas.selectLinea(tablaManzanas.selectedIndex);
    tablaManzanas.selectedIndex = -1;
  }
}

function pulsarSalir()	{
	document.forms[0].opcion.value="inicializarTerc";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
    document.forms[0].submit();
}//de la funcion

function pulsarEliminar() {
  if(tablaManzanas.selectedIndex != -1) {
    if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimManz")%>') ==1) {
        comboDistrito.activate();
        comboSeccion.activate();
        var vector = [document.forms[0].codManzana];
        habilitarGeneral(vector);
        document.forms[0].opcion.value="eliminar";
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Manzanas.do";
        document.forms[0].submit();
        deshabilitarGeneral(vector);
        comboDistrito.deactivate();
        comboSeccion.deactivate();
        pulsarLimpiar();
        if(tablaManzanas.selectedIndex != -1 ) {
            tablaManzanas.selectLinea(tablaManzanas.selectedIndex);
            tablaManzanas.selectedIndex = -1;
        }
    }
  } else {
    jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
  }
}

function noEliminarManz() {
  jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimManz")%>");
}

		
  </script>
</head>

<body class="bandaBody" onload="javascript:{ pleaseWait('off'); inicializar();}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
	
<form action="" method="get" name="formulario" target="_self">
<input type="hidden" name="opcion">
<input type="hidden" name="codPais" size="3" value="<%=ptVO.getPais()%>">
<input type="hidden" name="codMunicipioAntiguo" value="">
<input type="hidden" name="codProvincia" value="<%=ptVO.getProvincia()%>">
<input type="hidden" name="descProvincia" value="<%=ptVO.getNomProvincia()%>">
<input type="hidden" name="codMunicipio" value="<%=ptVO.getMunicipio()%>">
<input type="hidden" name="descMunicipio" value="<%=ptVO.getNomMunicipio()%>">
<input type="hidden" name="cSeccion" value="">
<input type="hidden" name="letraSeccion" value="">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantManz")%></div>
<div class="contenidoPantalla">
    <table width="100%">
        <tr>
            <td>
                <table width="100%">
                    <tr>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td width="50%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Distrito")%>:</td>
                        <td width="50%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Seccion")%>:</td>
                      </tr>
                        <tr>
                          <td width="50%" class="columnP" align="left">
                            <table width="100%" cellpadding="0px" cellspacing="0px">
                                  <tr>
                                    <td width="70%" align="left">
                                        <input type="text" class="inputTextoObligatorio" name="codDistrito" size="3"
                                               onKeyPress = "javascript:return SoloDigitos(event);">
                                        <input type="text" class="inputTextoObligatorio" name="descDistrito" style="width:200" readonly="true">
                                                </td>
                                                <td width="30%" valign="bottom" align="left">
                                                      <A href="" name="anchorDistrito" id="anchorDistrito">
                                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonDistrito" id="botonDistrito" style="cursor:hand;"></span>
                                                      </A>
                                                </td>
                                            </tr>
                                          </table>
                                        </td>
                                        <td width="50%" class="columnP" align="left">
                                            <table width="100%" cellpadding="0px" cellspacing="0px">
                                                  <tr>
                                                    <td width="70%" align="left">
                                                      <input type="text" class="inputTextoObligatorio" name="codSeccion" size="3"
                                                               onKeyPress = "javascript:PasaAMayusculas(event);">
                                                      <input type="text" class="inputTextoObligatorio" name="descSeccion" style="width:200" readonly="true">
                                                </td>
                                                <td width="30%" valign="bottom" align="left">
                                                      <A href="" name="anchorSeccion" id="anchorSeccion">
                                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonSeccion" id="botonSeccion" style="cursor:hand;"></span>
                                                      </A>
                                                </td>
                                                </tr>
                                              </table>
                                            </td>
                                          </tr>
                                          <tr>
                                                <td colspan="2">&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td width="50%"></td>
                                            <td width="50%" align="right">
                                                <input name="botonBuscar" type="button"  class="boton" id="botonBuscar" 
                                                                              value="<%=descriptor.getDescripcion("gbBuscar")%>"
                                                                              onClick="pulsarBuscar();" accesskey="B">
                                                <input name="botonCancelar" type="button" class="boton" id="botonCancelar"
                                                  value="<%=descriptor.getDescripcion("gbCancelar")%>"
                                                  onClick="pulsarCancelarBuscar();" accesskey="C">
                        </td>
                      </tr>	
                      <tr>
                              <td colspan="2">&nbsp;</td>
                      </tr>	
              </table>
            </td>
        </tr>
        <tr>
          <td> 
            <table width="100%" rules="cols"  border="0" cellspacing="0" cellpadding="0" class="fondoCab">

            <tr>
            <td id="tablaManzanas">
                                    </td>
    </tr>
    </table>
    </td>
            </tr>
            <tr>
                      <td> 
        <table width="100%" rules="cols"  border="0" align="center"  cellpadding="0" cellspacing="0">
          <tr>
            <td width="20%" align="center"> 
                                                                                            <input name="codManzana" type="text" class="inputTextoObligatorio" size=4 maxlength=3 
                                                                                                    onKeyPress = "javascript:return SoloDigitos(event);">	
                                    </td>
            <td width="80%"  align="center">
                                                                                            <input name="descManzana" type="text" class="inputTexto" size=40 maxlength=80
                                                                                                    onKeyPress = "javascript:PasaAMayusculas(event);">
                                    </td>
                              </tr>
        </table>   
      </td>
            </tr>
    </table>
    <div class="botoneraPrincipal" id="tablaBotones">
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
  var tablaManzanas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaManzanas"));
  tablaManzanas.addColumna("100","center",'<%=descriptor.getDescripcion("gEtiq_Codigo")%>');
  tablaManzanas.addColumna("580","left",'<%=descriptor.getDescripcion("gEtiq_Descrip")%>');
  tablaManzanas.displayCabecera=true;
  tablaManzanas.displayTabla();
  

function refresca()	{
  tablaManzanas.displayTabla();
}//de la funcion

function rellenarDatos(tableName,rowID)	{
  if(rowID != -1){
     var i=rowID;
     if(i>=0){
	   var vector = [document.forms[0].codManzana];
	   deshabilitarGeneral(vector);
       document.forms[0].codManzana.value = listaManzanas[i][0];
	   document.forms[0].descManzana.value = listaManzanas[i][1];
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
	    if (teclaAuxiliar == 38) upDownTable(tablaManzanas,listaManzanas,teclaAuxiliar);
	    if (teclaAuxiliar == 40) upDownTable(tablaManzanas,listaManzanas,teclaAuxiliar);
            if (teclaAuxiliar == 1){

		if (comboDistrito.base.style.visibility == "visible" && isClickOutCombo(comboProvincia,coordx,coordy)) setTimeout('comboDistrito.ocultar()',20);
		if (comboSeccion.base.style.visibility == "visible" && isClickOutCombo(comboProvincia,coordx,coordy)) setTimeout('comboSeccion.ocultar()',20);
            }
            if (teclaAuxiliar == 9){

		if (comboDistrito.base.style.visibility == "visible") setTimeout('comboDistrito.ocultar()',20);
                if (comboSeccion.base.style.visibility == "visible") comboSeccion.ocultar();
            }

	}//de la funcion
	
	var comboDistrito = new Combo("Distrito");
	var comboSeccion = new Combo("Seccion");
	
	
comboDistrito.change = 
    function() { 
      document.forms[0].codSeccion.value = "";
	  document.forms[0].descSeccion.value = "";
	  auxCombo="comboSeccion"; 
      if(comboDistrito.des.value.length!=0){
        var vector = [document.forms[0].codManzana];
	    deshabilitarGeneral(vector);
		cargarListaSecciones();
      }else{
        comboSeccion.addItems([],[]);
      }		
    } 
	
	
</script>
</body>
</html>
