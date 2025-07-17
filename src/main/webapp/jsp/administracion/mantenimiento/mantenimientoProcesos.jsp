<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Mantenimiento de Procesos </title>
	<!-- Estilos -->

<%
	UsuarioValueObject usuarioVO = new UsuarioValueObject();
	int idioma=1;
	int apl=5;
        String css="";
  	if (session.getAttribute("usuario") != null){
		usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    	apl = usuarioVO.getAppCod();
	    idioma = usuarioVO.getIdioma();
            css=usuarioVO.getCss();
	}
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
	<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript">
var lista = new Array();
var cod_Aplicaciones = new Array();
var desc_Aplicaciones = new Array();
var cod_Idiomas = new Array();
var desc_Idiomas = new Array();
var datosProcesos = new Array();
var datosProcesosOriginal = new Array();
var datosDescripciones = new Array();
var datosDescripcionesOriginal = new Array();
var vectorCamposRejilla = ['codProceso','descProceso','formulario'];
var vectorBotones = new Array();
var vectorCamposBusqueda = new Array();
var vectorCamposRejilla1 = new Array();
		
    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
	function inicializar(){	
			window.focus();
			recuperaDatosIniciales();
      		vectorBotones = [document.forms[0].botonAltaIdioma,
                                document.forms[0].botonEliminarIdioma,
                                document.forms[0].botonModificarIdioma,
				document.forms[0].botonAlta,
                                document.forms[0].botonModificar,
                                document.forms[0].botonBorrar,
                                document.forms[0].botonLimpiar];
	vectorCamposBusqueda = [document.forms[0].codAplicacion,
                                document.forms[0].descAplicacion];
	vectorCamposRejilla1 = [document.forms[0].codProceso,
                                document.forms[0].descProceso,
        			document.forms[0].formulario,
                                document.forms[0].descripcion];

			pulsarCancelarBuscar();
			comboAplicacion.addItems(cod_Aplicaciones, desc_Aplicaciones);
                            comboIdiomas.addItems(cod_Idiomas, desc_Idiomas);
                            comboIdiomas.deactivate();
	}
		
	function recuperaDatosIniciales(){
	<% 
      MantenimientosAdminForm bForm = (MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
      Vector listaAplicaciones = bForm.getListaAplicaciones();
	  if (listaAplicaciones == null) listaAplicaciones = new Vector();
	  Vector listaIdiomas = bForm.getListaIdiomas();
	  if (listaIdiomas == null) listaIdiomas = new Vector();
    %>
    	var j=0;
    <%for(int i=0;i<listaAplicaciones.size();i++){
        GeneralValueObject aplicacion = (GeneralValueObject)listaAplicaciones.get(i);%>
      	cod_Aplicaciones[j] = '<%=(String) aplicacion.getAtributo("codigo")%>';
      	desc_Aplicaciones[j++] = '<%=(String) aplicacion.getAtributo("descripcion")%>';
    <%}%>
		j=0;
	<%for(int i=0;i<listaIdiomas.size();i++){
        GeneralValueObject idiomaDesc = (GeneralValueObject)listaIdiomas.get(i);%>
      	cod_Idiomas[j] = '<%=(String) idiomaDesc.getAtributo("codigo")%>';
      	desc_Idiomas[j++] = '<%=(String) idiomaDesc.getAtributo("descripcion")%>';
    <%}%>

	}

    function cargarTablaProcesos(listaOriginal, lista){
	  datosProcesosOriginal = listaOriginal;
      datosProcesos = lista;
      tablaProcesos.lineas = datosProcesos;
	  datosDescripciones = new Array();
	  datosDescripcionesOriginal = new Array();
      refresca(tablaProcesos);
	  refresca(tablaIdiomas);
	  
    }

    // FUNCIONES DE LIMPIEZA DE CAMPOS
    function limpiarFormulario(){
      tablaProcesos.lineas = new Array();
      refresca(tablaProcesos);
	  pulsarLimpiar();
    }

    function limpiarCamposRejilla(){
		limpiar(vectorCamposRejilla);
		comboIdiomas.selectItem(-1);			
		limpiar(['descripcion']);

	}
    
    // FUNCIONES DE PULSACION DE BOTONES
    function pulsarBuscar(){
		var botonBuscar = [document.forms[0].botonBuscar];
		if(validarCamposBusqueda()){
        	document.forms[0].opcion.value="cargarProcesos";
	        document.forms[0].target="oculto";
    	    document.forms[0].action="<%=request.getContextPath()%>/administracion/mantenimiento/Procesos.do";
        	document.forms[0].submit();
			comboAplicacion.deactivate();
      		habilitarGeneral(vectorCamposRejilla1);
			habilitarGeneral(vectorBotones);
			comboIdiomas.activate();
			deshabilitarGeneral(botonBuscar);
		}else jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
	}
    
	
    function pulsarSalir(){
	  document.forms[0].target = "mainFrame";
      document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
      document.forms[0].submit();
    }

    function pulsarEliminar() {
      if(filaSeleccionada(tablaProcesos)) {
		if(jsp_alerta("C",'<%=descriptor.getDescripcion("desEliminarPro")%>') ==1) {
			comboAplicacion.activate();
	        document.forms[0].opcion.value = 'eliminar';
	        document.forms[0].target = "oculto";
	        document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Procesos.do';
	        document.forms[0].submit();       
	        pulsarLimpiar();
		}
      }else 
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }

    function pulsarModificar(){
      if(filaSeleccionada(tablaProcesos)){		
	  	  // Solo son obligatorias las descripciones.
	  	  var di = construirIdiomasDescripciones();
	  	  if (""!=di) {
		  	  comboAplicacion.activate();
		  	  document.forms[0].idiomas_descripciones.value = di;
	          document.forms[0].opcion.value = 'modificar';
	          document.forms[0].target = "oculto";
	          document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Procesos.do';
	          document.forms[0].submit();
	          deshabilitarGeneral(vectorCamposBusqueda);
          	  pulsarLimpiar();
		} else jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');

     } else
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }

    function pulsarAlta(){
	  // Solo son obligatorias las descripciones.
	  var di = construirIdiomasDescripciones();
	  if (""!=di) {
	  	  comboAplicacion.activate();
		  document.forms[0].idiomas_descripciones.value = di;
          document.forms[0].opcion.value = 'alta';
          document.forms[0].target = "oculto";
          document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Procesos.do';
          document.forms[0].submit();
          deshabilitarGeneral(vectorCamposBusqueda);
          pulsarLimpiar();
      } else		
			  jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
    }

    function pulsarCancelarBuscar(){
			limpiarFormulario();
			var botonBuscar = [document.forms[0].botonBuscar];
			habilitarGeneral(botonBuscar);			
      		deshabilitarGeneral(vectorCamposRejilla1);
			deshabilitarGeneral(vectorBotones);
			comboAplicacion.activate();
			comboIdiomas.deactivate();
		}
		
    function pulsarLimpiar(){
			limpiarCamposRejilla();
			datosDescripcionesOriginal= new Array();
			datosDescripciones= new Array();
			tablaIdiomas.lineas=datosDescripciones;
			refresca(tablaIdiomas);
			comboIdiomas.selectItem(-1);			
			limpiar(['descripcion']);
			
	}

    // FUNCIONES DE VALIDACION Y COMPROBACION DEL FORMULARIO
    function noEsta(){
      var cod = document.forms[0].codProceso.value;
      for(i=0;(i<lista.length);i++){
          if((lista[i][0]) == cod)
            return false;
	   }
      return true;
    }

    function filaSeleccionada(tabla){
      var i = tabla.selectedIndex;
      if((i>=0)&&(!tabla.ultimoTable))
          return true;
      return false;
    }
		
    function validarCamposBusqueda(){
      var codOrg = document.forms[0].codAplicacion.value;
      if(codOrg!="")
        return true;
      return false;
    }

    function validarCamposRejilla(){
      var codEnt = document.forms[0].codProceso.value;
      var descEnt = document.forms[0].descProceso.value;
      var tipoEnt = document.forms[0].formulario.value;
      if((codEnt!="")&&(descEnt!="")&&(tipoEnt!=""))
        return true;
      return false;
    }

	function validarCamposRejillaIdiomas(){
      var cod = document.forms[0].codIdiomas.value;
      var desc = document.forms[0].descIdiomas.value;
      var tipo = document.forms[0].descripcion.value;
      if((cod!="")&&(desc!="")&&(tipo!=""))
        return true;
      return false;
    }

	function pulsarNuevaDescripcion(){
		if (validarCamposRejillaIdiomas()){
		  var existe = false;
	      var cod = document.forms[0].codIdiomas.value;
		  var idesc=datosDescripcionesOriginal.length;
	      for(i=0;i<idesc;i++){
	          if(datosDescripcionesOriginal[i][0] == cod)
	            existe = true;
		   }
		  if (existe) 
			 jsp_alerta("A",'<%=descriptor.getDescripcion("msjDescExiste")%>')
		  else {
		 	datosDescripcionesOriginal[idesc] = [document.forms[0].codIdiomas.value,document.forms[0].descIdiomas.value,document.forms[0].descripcion.value];
			datosDescripciones[idesc]=[document.forms[0].descIdiomas.value,document.forms[0].descripcion.value];
			tablaIdiomas.lineas = datosDescripciones;
			refresca(tablaIdiomas);
			actualizarDescProceso(document.forms[0].descripcion.value, document.forms[0].codIdiomas.value);
			comboIdiomas.selectItem(-1);
			limpiar(['descripcion']);

		  }		
		} else  jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>')
	}
	
	function pulsarEliminarDescripcion(){
	  var s = tablaIdiomas.selectedIndex;
	  if((s>=0)&&!tablaIdiomas.ultimoTable){	  
		if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarDesc")%>')) {

			var lOriginal = new Array();
			var l = new Array();
			for(i=0;i<s;i++){
				lOriginal[i]=datosDescripcionesOriginal[i];
				l[i]=datosDescripciones[i];
	   		}
	   		for(i=s+1;i<(datosDescripcionesOriginal.length);i++){
				lOriginal[i-1]=datosDescripcionesOriginal[i];
				l[i-1]=datosDescripciones[i];
	   		}
			datosDescripcionesOriginal = new Array();
			datosDescripciones = new Array();
			datosDescripcionesOriginal = lOriginal;
			datosDescripciones = l;
			tablaIdiomas.lineas = datosDescripciones;
			refresca(tablaIdiomas);
			comboIdiomas.selectItem(-1);
			limpiar(['descripcion']);
		}
	 }
	}
	
	function pulsarModificarDescripcion(){
	  var s = tablaIdiomas.selectedIndex;
	  if((s>=0)&&!tablaIdiomas.ultimoTable){
			var yaExiste = 0;
			var idioma = document.forms[0].codIdiomas.value;
			if (validarCamposRejillaIdiomas()){			
				for(var l=0; l < datosDescripcionesOriginal.length; l++){
					if(l != s) {
						if ((datosDescripcionesOriginal[l][0]) == idioma) yaExiste = 1;
					}
				}				
				if(yaExiste == 0) {
					datosDescripcionesOriginal[s]=[document.forms[0].codIdiomas.value,document.forms[0].descIdiomas.value,document.forms[0].descripcion.value];
					datosDescripciones[s]=[document.forms[0].descIdiomas.value,document.forms[0].descripcion.value];
					tablaIdiomas.lineas=datosDescripciones;
					refresca(tablaIdiomas);
					comboIdiomas.selectItem(-1);
					limpiar(['descripcion']);
				} else {
					jsp_alerta('A','<%=descriptor.getDescripcion("msjDescExiste")%>');
				}
			} else {
				jsp_alerta('A','<%=descriptor.getDescripcion("msjObligTodos")%>');
			}
		} else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
	}	

	function construirIdiomasDescripciones(){
		var cadena ="";
		for (i=0; i < datosDescripcionesOriginal.length; i++) {
			cadena += datosDescripcionesOriginal[i][0]+'§¥' +datosDescripcionesOriginal[i][2]+'§¥';
		}
		return cadena;
	}
	
	function actualizarDescProceso(valor, idioma){
	  var s = tablaProcesos.selectedIndex;
	  if((s>=0)&&!tablaProcesos.ultimoTable){
	  } else {
	  	if (""!=document.forms[0].descProceso.value ) {
			if ('<%=idioma%>'==idioma)
	  			document.forms[0].descProceso.value = valor;
		} else	document.forms[0].descProceso.value = valor;
	  }
	}
	
	</script>
</head>

<body class="bandaBody" onload="javascript:{pleaseWait('off');
        inicializar();}">


        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        

<form  method="post">
  <input  type="hidden"  name="opcion" id="opcion">
  <input  type="hidden"  name="codProceso" value="">
  <input  type="hidden" name="idiomas_descripciones" value="">

    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantProcesos")%></div>
    <div class="contenidoPantalla">
        <table style="width: 100%">
            <tr>
                    <td>
                        <table style="width: 100%">
                            <tr>
                            <td class="etiqueta" style="width: 10%"><%=descriptor.getDescripcion("gEtiq_Aplicacion")%>:</td>
                            <td class="etiqueta" style="width: 60%">
                                    <input type="text" name="codAplicacion" id="codAplicacion" size="3" class="inputTextoObligatorio" value="">
                                    <input type="text" name="descAplicacion"  id="descAplicacion" size="43" class="inputTextoObligatorio" readonly="true" value="">
                                    <A href="" id="anchorAplicacion" name="anchorAplicacion"> 
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                            name="botonAplicacion" style="cursor:hand;"></span>
                                    </A>
                            </td>
                            <td style="width: 30%;text-align:right">
                                            <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" 
                                                    value="<%=descriptor.getDescripcion("gbBuscar")%>"
                                                    onClick="pulsarBuscar();" accesskey="B">
                                            <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                                                    value='<%=descriptor.getDescripcion("gbCancelar")%>'
                                                    onClick="pulsarCancelarBuscar();" accesskey="C">
                                    </td>
                            </tr>
                        </table>
                </td>
            </tr>
            <tr> 
            <td id="tabla"></td>
        </tr>
        <tr> 
          <td> 
            <input name="descProceso" type="text" class="inputTextoSinMayusculas" maxlength="80" style="width: 31%" readOnly> 
            <input name="formulario" type="text" class="inputTextoSinMayusculas" style="width:68%" maxlength="255" > 
          </td>
        </tr>
            <tr> 
            <td id="tablaI"></td>
        </tr>
        <tr> 
            <td>
                <input type="text" name="codIdiomas" id="codIdiomas" style="width:3%" class="inputTextoObligatorioSinMayusculas" value="">
                <input type="text" name="descIdiomas"  id="descIdiomas" style="width:25%" class="inputTextoObligatorioSinMayusculas" readonly="true" value="">
                <A href="" id="anchorIdiomas" name="anchorIdiomas" style="text-decoration: none"> 
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonIdiomas"
                        name="botonIdiomas" style="cursor:hand;"></span>
                </A>
                <input name="descripcion" type="text" style="width:68%" class="inputTextoObligatorioSinMayusculas" maxlength="80" >
            </td>
        </tr>									
        <tr> 
            <td align="right">
                <input name="botonAltaIdioma" type="button"  class="botonGeneral" id="botonAltaIdioma" 
                                                                        value="<%=descriptor.getDescripcion("gbAnadir")%> Idioma" 
                                                                        onClick="pulsarNuevaDescripcion();" accesskey="A">
                <input name="botonModificarIdioma" type="button" class="botonGeneral" id="botonModificarIdioma"
                                                                        value='<%=descriptor.getDescripcion("gbModificar")%>'
                                                                        onClick="pulsarModificarDescripcion();" accesskey="E">																											
                <input name="botonEliminarIdioma" type="button" class="botonGeneral" id="botonEliminarIdioma"
                                                                        value='<%=descriptor.getDescripcion("gbEliminar")%>'
                                                                        onClick="pulsarEliminarDescripcion();" accesskey="E">										
            </td>
        </tr>
    </TABLE>								
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> 
                    name="botonAlta" onClick="pulsarAlta();" accesskey="A"> 
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> 
                    name="botonModificar" onClick="pulsarModificar();" accesskey="M"> 
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> 
                    name="botonBorrar" onClick="pulsarEliminar();" accesskey="E"> 
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> 
                    name="botonLimpiar" onClick="pulsarLimpiar();" accesskey="L"> 
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> 
                    name="botonSalir" onClick="pulsarSalir();" accesskey="S">  
    </div>                        
</div>                        
</form>

<script type="text/javascript">  

    var tablaProcesos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
            
    tablaProcesos.addColumna('270',null,'<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
    tablaProcesos.addColumna('600',null,'<%= descriptor.getDescripcion("gEtiq_Formulario")%>');
    tablaProcesos.displayCabecera = true;  
  
  function refresca(tabla){
    tabla.displayTabla();
  }

  function rellenarDatos(tableName,rowID){
      tableObject = tableName;
   	 if(tablaProcesos == tableName){
      var i = rowID;
      limpiarCamposRejilla();
      if((i>=0)&&!tableName.ultimoTable){
	  	var listaDatos = [datosProcesosOriginal[i][0],datosProcesos[i][0],datosProcesos[i][1]];
		rellenar(listaDatos,vectorCamposRejilla);		
		datosDescripciones = new Array();
		datosDescripcionesOriginal = new Array();	
		datosDescripcionesOriginal = datosProcesosOriginal[i][4];
		for (var j=0; j<datosDescripcionesOriginal.length; j++)
			datosDescripciones[j] = [datosDescripcionesOriginal[j][1],datosDescripcionesOriginal[j][2]];
      	tablaIdiomas.lineas = datosDescripciones;						
		refresca(tablaIdiomas);
      }
	}else if(tablaIdiomas == tableName){
	    var i = rowID;
      	if((i>=0)&&!tableName.ultimoTable){			
			comboIdiomas.buscaCodigo(datosDescripcionesOriginal[i][0]);
			document.forms[0].descripcion.value=datosDescripcionesOriginal[i][2];
		}
    }

  } 

  var tablaIdiomas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaI'));
    tablaIdiomas.addColumna('270',null,'<%= descriptor.getDescripcion("gEtiq_Idioma")%>');
    tablaIdiomas.addColumna('600',null,'<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
  tablaIdiomas.displayCabecera = true;  
  
  tablaIdiomas.displayTabla();

  var tablaObject = tablaProcesos;
    
  var comboAplicacion = new Combo("Aplicacion");
  var comboIdiomas = new Combo("Idiomas");


  <%String Agent = request.getHeader("user-agent");%>

var coordx=0;
var coordy=0;


<%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>


  document.onmouseup = checkKeys; 
  
    function checkKeysLocal(evento, tecla){
         var teclaAuxiliar="";
          if(window.event){
            evento = window.event;
            teclaAuxiliar =evento.keyCode;
          }else
                teclaAuxiliar =evento.which;

        keyDel(evento);

        if(teclaAuxiliar == 40 || teclaAuxiliar == 38){
      if(tablaProcesos==tablaObject)
                upDownTable(tablaProcesos,datosProcesos,teclaAuxiliar);
      else{
                upDownTable(tablaIdiomas,datosDescripciones,teclaAuxiliar);
      }
    }    
        
        if(teclaAuxiliar == 13){
            if(tablaProcesos==tablaObject)
            pushEnterTable(tablaProcesos,datosProcesos);
            else
            pushEnterTable(tablaIdiomas,datosDescripciones);
        }
        if(teclaAuxiliar == 1){
            if (comboAplicacion.base.style.visibility == "visible" && isClickOutCombo(comboAplicacion,coordx,coordy)) setTimeout('comboAplicacion.ocultar()',20);
            if (comboIdiomas.base.style.visibility == "visible" && isClickOutCombo(comboIdiomas,coordx,coordy)) setTimeout('comboIdiomas.ocultar()',20);
        }
        if(teclaAuxiliar == 9){
            if (comboAplicacion.base.style.visibility == "visible") comboAplicacion.ocultar();
            if (comboIdiomas.base.style.visibility == "visible") comboIdiomas.ocultar();
        }
}
</script>
</body>
</html>
