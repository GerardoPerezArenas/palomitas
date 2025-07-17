<!-- JSP con formulario de los datos de persona -->
<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.CargosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> MANTENIMIENTO DE CARGOS </title>
<!-- Estilos -->
<%
   UsuarioValueObject usuarioVO = null;
   int idioma = 1;
	 Vector vector = new Vector();
   if (session!=null){
   	 usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
   	 idioma =  usuarioVO.getIdioma();
		 CargosForm cForm =(CargosForm)session.getAttribute("CargosExpedienteForm");
	   vector = cForm.getLista();
  }

%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />
<!-- Ficheros JavaScript -->
<!-- ojo, eliminar los innecesarios -->
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script language="JavaScript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
<script language="JavaScript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script language="JavaScript">

  	var ultimo = false;
	var lista = new Array();
	//var tipos = new Array();

  function cargaTabla(){
		var i=0;
  	<%while(!vector.isEmpty()){
				GeneralValueObject gVO = (GeneralValueObject)vector.remove(0);
		%>
				lista[i] = ['<%=(String)gVO.getAtributo("cod")%>', '<%=(String)gVO.getAtributo("desc")%>', 
         		        '<%=(String)gVO.getAtributo("cargo")%>', '<%=(String)gVO.getAtributo("tratam")%>'];
    		i++;
  	<%}%>
    tab.lineas = lista;
    refresh();
  }

  function borrarDatos(){
    document.forms[0].txtCodigo.value = '';
    document.forms[0].txtDescripcion.value = '';
    document.forms[0].cargo.value = '';
	document.forms[0].tratam.value = '';
  }

  function Inicio(){
  		window.focus();
		//tipos = ['A','I','S','T'];
		//comboTipo.addItems(tipos,tipos);
    	cargaTabla();
  }

  function pulsarSalir(){
    window.location = "<%=request.getContextPath()%>/sge/CargosExpediente.do?opcion=salir";
  }

  function limpiarInputs() {
	tab.selectLinea(tab.selectedIndex);
    borrarDatos();
  }

  function pulsarEliminar() {
		if(tab.selectedIndex != -1) {
			document.forms[0].identificador.value = lista[tab.selectedIndex][0];
			document.forms[0].opcion.value = 'eliminar';
			document.forms[0].target = "oculto";
			document.forms[0].action = '<%=request.getContextPath()%>/sge/CargosExpediente.do';
			document.forms[0].submit();
			limpiarInputs();
		}
			else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
  }

function pulsarModificar(){
   	if(tab.selectedIndex != -1){
		if(validarFormulario()){
   			document.forms[0].opcion.value = 'modificar';
      		document.forms[0].target = "oculto";
 	    	document.forms[0].action = '<%=request.getContextPath()%>/sge/CargosExpediente.do';
   	  		document.forms[0].submit();
		}
	}else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

  function pulsarAlta(){
    var cod = document.forms[0].txtCodigo.value;
    var existe = 0;
    if (validarFormulario()){
    	for(var i=0; (i < lista.length) && (existe == 0); i++){
      	if((lista[i][0]) == cod)
          existe = 1;
      }
      if(existe == 0){
        document.forms[0].opcion.value = 'alta';
        document.forms[0].target = "oculto";
        document.forms[0].action = '<%=request.getContextPath()%>/sge/CargosExpediente.do';
        document.forms[0].submit();
      }
			else
	      jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
		}
	}

  function recuperaDatos(lista2) {
    limpiarInputs();
	lista = lista2;
    tab.lineas=lista;
   	refresh();
  }

  /////////////// Búsqueda rápida.

  function rellenarDatos(tableObject, rowID){
	if(rowID>-1 && !tableObject.ultimoTable){
		document.forms[0].txtCodigo.value = lista[tab.selectedIndex][0];
		document.forms[0].txtDescripcion.value = lista[tab.selectedIndex][1];
		document.forms[0].cargo.value = lista[tab.selectedIndex][2];
		document.forms[0].tratam.value = lista[tab.selectedIndex][3];
	}else borrarDatos();
  }

  function selecFila(des){
	var indexOld = tab.selectedIndex;
    if(des.length != 0){
      for (var x=0; x<lista.length; x++){
        var auxLis = new String(lista[x][1]);
        auxLis = auxLis.substring(0,des.length);
        if(auxLis == des){
		  if (x!=indexOld)tab.selectLinea(x);
		  break;
        }
      }
    }else tab.selectLinea(-1);
  }

  function buscar(evento){
       var aux=null;
        if(window.event)
            aux = window.event;
        else
            aux = evento;


    var auxDes = new String("");
    if((aux != 40)&&(aux != 38)){
      if(aux != 13){
        auxDes = document.forms[0].txtDescripcion.value;
        if(aux == 8 && auxDes.length == 0) borrarDatos();
        selecFila(auxDes);
      }else{
        if((tab.selectedIndex>-1)&&(tab.selectedIndex < lista.length)){
          document.forms[0].txtCodigo.value = lista[tab.selectedIndex][0];
		  document.forms[0].txtDescripcion.value = lista[tab.selectedIndex][1];
		  document.forms[0].cargo.value = lista[tab.selectedIndex][2];
		  document.forms[0].tratam.value = lista[tab.selectedIndex][3];
          auxDes = lista[tab.selectedIndex][1];
        }
      }
    }
  }
  /////////////// Control teclas.

  function checkKeysLocal(evento, tecla) {

    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

    if('Alt+M'==tecla) pulsarModificar();
    if('Alt+A'==tecla) pulsarAlta();
    if('Alt+E'==tecla) pulsarEliminar();
    if('Alt+L'==tecla) limpiarInputs();
    if('Alt+S'==tecla) pulsarSalir();

    if (teclaAuxiliar == 38 || teclaAuxiliar == 40){
		upDownTable(tab,lista,teclaAuxiliar);
	}
    
    if (teclaAuxiliar == 13) buscar(evento);
    keyDel(evento);
  }

  document.onkeydown=checkKeys;

</script>
<meta http-equiv="" content="text/html; charset=iso-8859-1">
</head>
<body class="bandaBody" scroll=no onload="javascript:{pleaseWait('off');}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<form name="formulario" method="post">
 <input  type="hidden"  name="opcion" id="opcion">
 <input  type="hidden"  name="identificador" id="identificador">

<div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("tit_cargos")%></div>
<div class="contenidoPantalla">
    <table>
        <tr>
            <td id="tabla"></td>
        </tr>
        <tr>
            <td>
            <!-- Codigo -->
            <input type="text" class="inputTextoObligatorio" id="obligatorio"  name="txtCodigo" style="width:7%" maxlength="3" onkeyup="return SoloDigitosNumericos(this);">
            <!-- Descripcion -->
            <input name="txtDescripcion" id="obligatorio" type="text" class="inputTextoObligatorio" style="width:47%"  maxlength="60" onkeyup="return xAMayusculas(this); buscar(evento);">
            <!-- Cargo que firma -->
            <input name="cargo" id="obligatorio" type="text" class="inputTextoObligatorio"  style="width:33%" maxlength="60" onkeyup="return xAMayusculas(this);">
            <!-- Tratamiento -->
            <input name="tratam" id="obligatorio" type="text" class="inputTextoObligatorio"  style="width:11%" maxlength="15" onkeyup="return xAMayusculas(this);">
            </td>
        </tr>
    </table>
    <div id="tabla2" class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAlta" onClick="pulsarAlta();">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdModificar" onClick="pulsarModificar();">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminar" onClick="pulsarEliminar();">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> name="cmdLimpiar" onClick="limpiarInputs();">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();">
    </div>
</div>
</form>

<script language="JavaScript1.2">
  var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'),700);
  tab.addColumna('50','left','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
  tab.addColumna('320','left','<%= descriptor.getDescripcion("gEtiq_desc")%>');
  tab.addColumna('227','left','<%= descriptor.getDescripcion("e_cargo")%>');
  tab.addColumna('79','left','<%= descriptor.getDescripcion("e_tratam")%>');
  tab.displayCabecera=true;

  function refresh(){
 	tab.displayTabla();
  }

 // var comboTipo = new Combo("Tipo");

</script>
<script> Inicio(); </script>
</body>
</html>
