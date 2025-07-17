<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.FusionDivisionForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>	

<html>
<head>
<title>CONSULTA DOMICILIOS POSTALES</title>
<meta	http-equiv="" content="text/html; charset=iso-8859-1">
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />

<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  int	idioma=1;
  int	apl=3;
  if (session.getAttribute("usuario") != null){	
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    apl = usuarioVO.getAppCod();
    idioma = usuarioVO.getIdioma();	
  }
%>

  <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"	type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
  <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
  <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
  <script type="text/javascript">
    // VARIABLES GLOBALES
    <%
	FusionDivisionForm fdForm =(FusionDivisionForm)session.getAttribute("FusionDivisionForm");
    %>
    var datosConsulta =	new Array();
    var listaDPO = new Array();
    var listaDPOOriginal = new Array();
    var listaTerceros =	new Array();
    var listaTercerosOriginal	= new	Array();
    var datosTerceros =	new Array();
    var tab;

    // FUNCION DE	INICIALIZACIÓN
    function inicializar(){
	pleaseWait1('off',top.mainFrame);
	recuperarConsulta();
	window.focus();
    }	

    // FUNCIONES DE CARGA DE DATOS
    function recuperarConsulta(){
	lineasPagina = 10;
	numeroPagina = 1;	
	pleaseWait1('on',top.mainFrame);
	document.forms[0].lineasPagina.value = lineasPagina;
	document.forms[0].pagina.value = numeroPagina;
	document.forms[0].opcion.value="recargaVerDomicilios";
	document.forms[0].target="oculto";
	document.forms[0].action="<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do";
	document.forms[0].submit();
    }	

    function cargaTablaDPO(){	
	listaDPO = new Array();	
	listaDPOOriginal = new Array();
	var longitud = datosConsulta.length;
	var k	= 0;
	for(j=0;j<longitud;j++){
	  listaDPOOriginal[k] =	datosConsulta[j];	
	  listaDPO[k] = [datosConsulta[j][1],datosConsulta[j][2],datosConsulta[j][15],
	    datosConsulta[j][16],datosConsulta[j][19],datosConsulta[j][20],
	    datosConsulta[j][21],datosConsulta[j][22],datosConsulta[j][23],
	    datosConsulta[j][24],datosConsulta[j][25]];	
	  k++;
	}
	listaSel = listaDPO;
	tablaDPO.lineas =	listaDPO;
	tablaTer.lineas =	new Array();
	refresca(tablaDPO);
	refresca(tablaTer);
	tab =	tablaDPO;
	if (listaDPO.length==0)	
	  jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoDatos")%>');
    }	

    function recuperaBusqueda(datos){
	pleaseWait1('off',top.mainFrame);
	datosConsulta = datos;
	cargaTablaDPO();
    }	

    function cargaTablaTerceros(i){	
	listaTerceros = new Array();
	listaTercerosOriginal =	new Array();
	var listaTer = listaDPOOriginal[i][26];
	var hoja = listaDPOOriginal[i][12];	
	var longitud = listaTer.length;
	var k	= 0;
	for(j=0;j<longitud;j++){
	  var	nombre = "";
	  nombre = (listaTer[j][4]!="")? nombre +	listaTer[j][4]:nombre;
	  nombre = (listaTer[j][2]!="")? nombre +" "+ listaTer[j][2]:nombre;
	  nombre = (listaTer[j][5]!="")? nombre +" "+ listaTer[j][5]:nombre;
	  nombre = (listaTer[j][3]!="")? nombre +" "+ listaTer[j][3]:nombre;
	  nombre = (nombre!="")?nombre+", "+ listaTer[j][1]:nombre + listaTer[j][1];
	  listaTercerosOriginal[k] = listaTer[j];	
	  listaTerceros[k] = [hoja,listaTer[j][9],nombre,
	    listaTer[j][7],listaTer[j][8],listaTer[j][13],listaTer[j][16],listaTer[j][12]];	
	  k++;
	}
	tablaTer.lineas = listaTerceros;
	refresca(tablaTer);
    }	

  /* Para navegacion */	
  var listaSel = new Array();	
  var lineasPagina   = 10;

    // FUNCIONES DE LIMPIEZA DE CAMPOS
    function limpiarFormulario(){
	var vectorCampos = new Array();
	vectorCampos = ['codProvincia','descProvincia','codMunicipio','descMunicipio',
	'codESI','descESI','codNUC','descNUC','distrito','seccion'];
	limpiar(vectorCampos);
    }	

    // FUNCIONES DE PULSACIÓN	DE BOTONES
    function pulsarCerrar(){
	self.close();
    }	
  </script>	
</head>

<body class="bandaBody" onLoad="inicializar();">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<form name="formulario" method="post" action="">
    <input  type="hidden" name="opcion">
    <input type="hidden" name="lineasPagina" value="10">
    <input type="hidden" name="pagina" value="1">	

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titDomHabAfect")%></div>
    <div class="contenidoPantalla">
        <table width="100%">	
                <tr>
                    <td align="center">
                        <table width="95%" align="top" bordercolor="#7B9EC0" border="0px" cellspacing="0px" cellpadding="0px" class="fondoCab">
                                <tr>
                                        <td align="center" id="tablaDPO">
                                        </td>	
                                </tr>
                        </table>
        </td>	
        </tr>
        <tr>
        <td align="center">
            <table bgcolor="#ffffff" width="94%" border="0" cellspacing="2" cellpadding="0" style="border-top: #666666 1px solid; border-bottom: #666666 1px solid;border-left: #666666 1px solid;border-right: #666666	1px solid;">
                    <tr>
                            <td width="10%" class="etiqueta">
                                    <%=descriptor.getDescripcion("gEtiq_Provincia")%>
                            </td>	
                            <td width="28%">
                                    <input class="inputTexto" type="text" name="codProvincia"	size="3" readonly>
                                    <input name="descProvincia"	type="text"	class="inputTexto" size="25" readonly onMouseOver="titulo(this);">
                            </td>	
                            <td width="20%" class="etiqueta">
                                    <%=descriptor.getDescripcion("gEtiq_ESI")%>
                            </td>	
                            <td width="28%">
                                    <input class="inputTexto" type="text" name="codESI"	size="3" readonly>
                                    <input name="descESI"	type="text"	class="inputTexto" size="25" readonly onMouseOver="titulo(this);">	
                            </td>	
                            <td width="5%" class="etiqueta">
                                    <%=descriptor.getDescripcion("gEtiq_Distrito")%>
                            </td>	
                            <td width="5%">
                                    <input class="inputTexto" name="distrito1" type="text" size="5">
                            </td>	
                            <td width="5%" class="etiqueta">
                                    <%=descriptor.getDescripcion("gEtiq_Seccion")%>
                            </td>	
                            <td width="5%">
                                    <input class="inputTexto" name="seccion1" type="text" size="5">	
                            </td>	
                    </tr>
                    <tr>
                            <td width="10%" class="etiqueta">
                                    <%=descriptor.getDescripcion("gEtiq_Municipio")%>
                            </td>	
                            <td width="28%">
                                    <input class="inputTexto" type="text" name="codMunicipio"	size="3" readonly>
                                    <input name="descMunicipio"	type="text"	class="inputTexto" size="25" readonly onMouseOver="titulo(this);">
                            </td>	
                            <td width="20%" class="etiqueta">
                                    <%=descriptor.getDescripcion("gEtiq_NUC")%>
                            </td>	
                            <td width="28%">
                                    <input class="inputTexto" type="text" name="codNUC"	size="3" readonly>
                                    <input name="descNUC"	type="text"	class="inputTexto" size="25" readonly onMouseOver="titulo(this);">	
                            </td>	
                            <td class="etiqueta">
                                    <%=descriptor.getDescripcion("gEtiq_Letra")%>	
                            </td>	
                            <td width="5%">
                                    <input class="inputTexto" name="letra1"	type="text"	size="5">
                            </td>	
                            <td class="etiqueta">
                                    <%=descriptor.getDescripcion("gEtiq_Hoja")%>
                            </td>	
                            <td width="5%">
                                    <input class="inputTexto" name="hoja1" type="text" size="5">
                            </td>	
                    </tr>
            </table>
        </td>	
        </tr>
        <tr>
        <td align="center" bgcolor="#e6e6e6">
            <table width="95%" cellpadding="0px" cellspacing="0px">
                    <tr>
                            <td>
                                    <table width="100%" border="0px" cellspacing="0px" cellpadding="0px">
                                            <tr>
                                                    <td align="center" id="tablaTerceros">
                                                    </td>	
                                            </tr>
                                    </table>
                            </td>	
                    </tr>
                    <tr>
                            <td height="50px"	bgcolor="#e6e6e6"><div id="enlace"></div></td>
                    </tr>
            </table>
        </td>	
        </tr>
        </table>
        <div class="botoneraPrincipal">
            <input class="botonGeneral" name="botonCerrar" type="button"  value="<%=descriptor.getDescripcion("gbCerrar")%>" accesskey="C" onclick="javascript:pulsarCerrar();">
        </div>
    </div>
</form>

<script type="text/javascript" language="JavaScript1.2">
  var tablaDPO = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaDPO'));	
  tablaDPO.addColumna('45','left','<%= descriptor.getDescripcion("gEtiq_Codigo")%>');
  tablaDPO.addColumna('290','left','<%= descriptor.getDescripcion("gEtiq_Vial")%>');
  tablaDPO.addColumna('40','left','Nº');
  tablaDPO.addColumna('40','left','Let');
  tablaDPO.addColumna('40','left','<%= descriptor.getDescripcion("gEtiq_Blq")%>');
  tablaDPO.addColumna('40','left','Por');
  tablaDPO.addColumna('40','left','<%= descriptor.getDescripcion("gEtiq_Esc")%>');
  tablaDPO.addColumna('40','left','<%= descriptor.getDescripcion("gEtiq_Plta")%>');	
  tablaDPO.addColumna('40','left','<%= descriptor.getDescripcion("gEtiq_Pta")%>');
  tablaDPO.addColumna('40','left','<%= descriptor.getDescripcion("gEtiq_Km")%>');
  tablaDPO.addColumna('40','left','<%= descriptor.getDescripcion("gEtiq_Hm")%>');
  tablaDPO.displayCabecera=true;
  refresca(tablaDPO);

  var vectorCampos = ['codProvincia','descProvincia','codMunicipio','descMunicipio',
    'codESI','descESI','codNUC','descNUC','distrito1','seccion1','letra1','hoja1'];	
  
  function pintarTablaDPO(datos){
    var i = tablaDPO.focusedIndex;
    if((i>=0)&&(!tablaDPO.ultimoTable)){
        indice=i;
        cargaTablaTerceros(i);
        rellenarCampos(i);
    }else{
        tablaTer.lineas =	new Array();
        refresca(tablaTer);
        limpiar(vectorCampos);
    }	
  }

  tablaDPO.displayDatos = pintarTablaDPO;	

  var tablaTer = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaTerceros'));
  tablaTer.addColumna('45','left','<%= descriptor.getDescripcion("gEtiq_Hoja")%>');	
  tablaTer.addColumna('40','left','<%= descriptor.getDescripcion("gEtiq_Ord")%>');
  tablaTer.addColumna('310','left','<%= descriptor.getDescripcion("gEtiq_ApNombre")%>');
  tablaTer.addColumna('70','left','<%= descriptor.getDescripcion("gEtiq_TipoDoc")%>');
  tablaTer.addColumna('70','left','<%= descriptor.getDescripcion("gEtiq_Documento")%>');
  tablaTer.addColumna('70','left','<%= descriptor.getDescripcion("gEtiq_Sexo")%>');	
  tablaTer.addColumna('40','left','<%= descriptor.getDescripcion("gEtiq_Op")%>');
  tablaTer.addColumna('40','left','<%= descriptor.getDescripcion("gEtiq_Situacion")%>');
  tablaTer.displayCabecera=true;
  tablaTer.multiple = false;
  refresca(tablaTer);

  // FUNCIONES GENERALES DE TABLAS
  function refresca(tabla){
    tabla.displayTabla();
  }

  function rellenarCampos(i){	
    var vectorCampos = new Array();	
    var vectorDatos = new Array();
    vectorDatos = [listaDPOOriginal[i][3],listaDPOOriginal[i][4],	
	listaDPOOriginal[i][5],listaDPOOriginal[i][6],
	listaDPOOriginal[i][9],listaDPOOriginal[i][10],	
	listaDPOOriginal[i][11],listaDPOOriginal[i][12],
	listaDPOOriginal[i][13],listaDPOOriginal[i][14],listaDPOOriginal[i][31],
	listaDPOOriginal[i][16]];
    limpiar(vectorCampos);
    rellenar(vectorDatos,vectorCampos);
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////
  var	tableObject=tablaDPO;
  function rellenarDatos(tableName,listName){
    tableObject =	tableName;
    if(tablaDPO == tableName){
	pintarTablaDPO(listName);
    }	
  }
  
  function checkKeysLocal(evento,tecla){
    var teclaAuxiliar;
        if(window.event){
            evento = window.event;
            teclaAuxiliar  = evento.keyCode;
        }else
            teclaAuxiliar  = evento.which;

		keyDel(evento);

    if(teclaAuxiliar ==	40 ||	teclaAuxiliar == 38){
	if(tablaTer==tableObject)
	  upDownTable(tablaTer,listaTerceros,teclaAuxiliar);
	else{	
	  upDownTable(tablaDPO,listaDPO,teclaAuxiliar);
	}
    }	

    if(teclaAuxiliar ==	13){
	if(tablaTer==tableObject)
	  pushEnterTable(tablaTer,listaTerceros);	
	else
	  pushEnterTable(tablaDPO,listaDPO);
    }	
  }
</script>
</body>
</html>
