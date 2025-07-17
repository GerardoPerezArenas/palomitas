<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic"%>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.UsuariosGruposForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO"%>

<html:html>

<head>
<jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />

<TITLE>::: ADMINISTRACION Unidad Orgánica:::</TITLE>


<%
	int idioma = 1;
	int apl = 1;
	int munic = 0;
        String css="";
	if (session != null) {
		UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
		if (usuario != null) {
			idioma = usuario.getIdioma();
			apl = usuario.getAppCod();
			munic = usuario.getOrgCod();
                        css=usuario.getCss();
		}
	}
%>


<!-- Estilos -->

<jsp:useBean id="descriptor" scope="request"
	class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
	type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor" property="idi_cod" value="<%= idioma%>" />
<jsp:setProperty name="descriptor" property="apl_cod" value="<%= apl %>" />


<!-- Ficheros JavaScript -->

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>
     <SCRIPT type="text/javascript">

	// Declaracion de variables.
	var cod_organizaciones = new Array();
	var desc_organizaciones = new Array();
	var cod_entidades = new Array();
	var desc_entidades = new Array();
	var uor_cods = new Array();
	var uor_descs = new Array();
	var uors = new Array();
	var codCargos = new Array();
	var descCargos = new Array();
	var cargos = new Array();

        // lista con las unidades organicas dependientes
        var unidadesDependientesUOR = new Array();

	/** Funcion encargada de inicializar los datos durante la carga de la pagina **/
	function inicializar() {
		var argVentana = self.parent.opener.xanelaAuxiliarArgs;
		document.forms[0].codOrganizaciones.value = argVentana[0];
		document.forms[0].codEntidades.value = argVentana[1];  
		document.forms[0].codUORs.value = argVentana[2];
		document.forms[0].descUORs.value = argVentana[3];
		document.forms[0].descOrganizaciones.value = argVentana[4];
		document.forms[0].descEntidades.value = argVentana[5];
		
		// Se recupera las lista de organizaciones a las que el usuario esta asociado
		<%
			UsuariosGruposForm bForm =(UsuariosGruposForm)session.getAttribute("UsuariosGruposForm");
			Vector listaOrganizaciones = bForm.getListaOrganizaciones();
			Vector listaOrg = bForm.getListaOrg();
			int lengthOrg = listaOrg.size();
			int lengthOrganizaciones = listaOrganizaciones.size();
			int m=0;
		%>		
		var n=0;
		<%
		for(m=0;m<lengthOrganizaciones;m++) {
			int t=0;
			GeneralValueObject organizaciones = (GeneralValueObject)listaOrganizaciones.get(m);
			String o = (String)organizaciones.getAtributo("codigo");
			for(t=0;t<lengthOrg;t++) {
				String org = (String)listaOrg.get(t);
					if(org.equals(o)) {
					%>
					cod_organizaciones[n] = '<%=(String)organizaciones.getAtributo("codigo")%>';
					desc_organizaciones[n] = '<%=(String)organizaciones.getAtributo("descripcion")%>';
					n++;
					<%
					t = lengthOrg;
				}
	  		}
		}
		%>  
		comboOrganizaciones.addItems(cod_organizaciones, desc_organizaciones); 

		// Se comprueba si ya hay definida una entidad, en ese caso se fijan esos valores y se desactivan 
		// sus campos de seleccion. Esto ocurre en el caso en que se vaya a administrar usuarios mediante
		// el administrados local.
		if(document.forms[0].codEntidad.value != "") {
			document.forms[0].codOrganizaciones.value=cod_organizaciones[0];
			document.forms[0].descOrganizaciones.value=desc_organizaciones[0];
			document.forms[0].codEntidades.value=document.forms[0].codEntidad.value;
			document.forms[0].descEntidades.value=document.forms[0].nombreEntidad.value;
			comboOrganizaciones.deactivate();
			var comboOrg = [document.forms[0].codOrganizaciones,document.forms[0].descOrganizaciones,document.forms[0].botonOrganizaciones];
			deshabilitarGeneral(comboOrg);
			comboEntidades.deactivate();
			var comboEnt = [document.forms[0].codEntidades,document.forms[0].descEntidades,document.forms[0].botonEntidades];
			deshabilitarGeneral(comboEnt);
			listaUORsCargos();
		}
	}

function pulsarSalir() {
  self.parent.opener.retornoXanelaAuxiliar();
}

    function pulsarAceptar () {
        var tieneDescendencia = false;
        // si TODAS está habilitado, los campos de la UOR dejan de ser obligatorios
        if(document.forms[0].todas.checked == true) {
	    document.forms[0].descUORs.className = "inputTexto";
            document.forms[0].codUORs.className = "inputTexto";
        }

        if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
            //comboOrganizaciones.activate();
            //comboEntidades.activate();
            var retorno = new Array();
            retorno[0] = document.forms[0].codOrganizaciones.value;
            retorno[1] = document.forms[0].descOrganizaciones.value;
            retorno[2] = document.forms[0].codEntidades.value;
            retorno[3] = document.forms[0].descEntidades.value;
            // si la UOR es "TODAS", se devuelven los arrays con todos los UOR cod real y UOR nombre
            if(document.forms[0].todas.checked == true) {
                var uor_reales = new Array();
                var longitud = uors.length;
                var i = 0;

                for (i = 0; i < longitud; i++) {
                    uor_reales[i] = uors[i].uor_cod;
                }

                retorno[4] = "-1";
                //retorno[4] = uor_cods;
                retorno[5] = "TODAS";

                var uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].codUORs.value, 'A');

            } else {
                var uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].codUORs.value, 'A');
                if(uor != null) retorno[4] = uor.uor_cod;
                else { // ha dado null para alta, buscamos de baja
                    uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].codUORs.value, 'B');
                    if(uor != null) retorno[4] = uor.uor_cod;
                    else retorno[4] = "";
                }
                retorno[5] = document.forms[0].descUORs.value;
                retorno[12] = obtenerPadreUorSeleccionada(retorno[4]);  // el padre es necesario enviarlo para poder realizar la
                                                                        // eliminacion de las uors dependientes cuando aun no se han grabado los cambios
                
                // Comprobamos si hemos elegido la opcion de selecccion de uors en cascada.
                // En ese caso obtenemos todos las uors dependientes de la uor seleccionada
                if(document.forms[0].permisosCascada.checked == true) {                  
                    tieneDescendencia = uorTieneDescendencia(retorno[4]);
                    if (tieneDescendencia){
                        retorno[8]= crearListaCodigosUor(retorno[4]);
                        // creamos la lista de descripciones y la de los padres de las uors hijas
                        var infoListas = crearListaDescripcionesPadresUors(retorno[5]);
                        retorno[9]= infoListas[0];                       
                        retorno[13]= infoListas[1];                        
                        unidadesDependientesUOR = new Array();
                    }
                }
            }
           
            var cargo = buscarUorPorCodVisible(cargos, document.forms[0].codCargos.value);
            if(cargo != null) retorno[6] = cargo.uor_cod;
            else retorno[6] = "";
            retorno[7] = document.forms[0].descCargos.value;
            retorno[10] = document.forms[0].permisosCascada.checked ;
            retorno[11] = tieneDescendencia;    
            //alert("antes de cerrar   retorno8="  + retorno[8] + "   retorno9=" +retorno[9] + "  retorno[10]=" + retorno[10] + "  retorno[11]=" + retorno[11]);
            self.parent.opener.retornoXanelaAuxiliar(retorno);
        }
    }

    // Funcion que recorre el arbol de UORS
    // y devuelve un array con las unidades organicas dependientes de la Uor seleccionada
    function uorTieneDescendencia(codUorSeleccionada){
        var tieneDescendencia = false;     
        // recorremos todos los nodos distintos del seleccionado
        for (var i = 0; i < uors.length; i++) {
           
            if (codUorSeleccionada != uors[i].uor_cod ){
                // miramos si la uor seleccionada es padre de alguna
                // y si lo es agreegamos su hijo para despues borrarlo
                if (codUorSeleccionada == uors[i].uor_pad ){
                    tieneDescendencia = true;
                    unidadesDependientesUOR[unidadesDependientesUOR.length] = uors[i].uor_cod;
                    uorTieneDescendencia(uors[i].uor_cod );
                }
            }
        }
        return tieneDescendencia;
    }

    // Crea la lista con la unidades organicas dependientes de la uor seleccionada
    function crearListaCodigosUor(codUorSeleccionada){
        var listaUors = "";
        listaUors = codUorSeleccionada;
        for (var k =0; k < unidadesDependientesUOR.length; k++) {            
            listaUors += '%$' + unidadesDependientesUOR[k];            
        }
        return listaUors;
    }


    function crearListaDescripcionesPadresUors(descripcionUorSeleccionada){
        var descripcionListaUors = "";
        var padreListaUors = "";
        var padre = "";
        descripcionListaUors = descripcionUorSeleccionada ;
        for (var k =0; k < unidadesDependientesUOR.length; k++) {
            var uor = buscarUorPorCod(unidadesDependientesUOR[k]);          
            descripcionListaUors += '%$' + uor.uor_nom;
            padre = uor.uor_pad;
            if (padre == '') padre = "-1";
            padreListaUors += '%$' + padre;
        }
        return [descripcionListaUors, padreListaUors];
    }

function obtenerPadreUorSeleccionada(codUorSeleccionada){
    var codigoPadre = "-1";
    for (var i = 0; i < uors.length; i++) {
        if (codUorSeleccionada == uors[i].uor_cod ){
            codigoPadre = uors[i].uor_pad;
            if (codigoPadre == '') codigoPadre = "-1";
            return codigoPadre;
        }
    }
    return codigoPadre;
}

function listaEntidades() {
  document.forms[0].organizacion.value=document.forms[0].codOrganizaciones.value;
  document.forms[0].opcion.value="cargarEntidades";
  document.forms[0].target="oculto";
  document.forms[0].action="<%=request.getContextPath()%>/administracion/AutorizacionesInternas.do";
  document.forms[0].submit();	
}

function cargarEntidades(codigos, descripciones) {
    cod_entidades = codigos;
    desc_entidades = descripciones;
    comboEntidades.addItems(codigos, descripciones);
 }
 
function cargarUnidOrgCargos(uor_dtos, codUORs, desUORs, cargosDTO, cod_Cargos, desc_Cargos) {
	uor_cods = codUORs;
	uor_descs = desUORs;
	uors = uor_dtos;
	codCargos = cod_Cargos;
	descCargos = desc_Cargos;
	cargos = cargosDTO;
	comboUORs.addItems(uor_cods, uor_descs);
	comboCargos.addItems(codCargos, descCargos);
}
 
function listaUORsCargos() {
  document.forms[0].organizacion.value=document.forms[0].codOrganizaciones.value;
  document.forms[0].entidad.value=document.forms[0].codEntidades.value;
  document.forms[0].opcion.value="cargarUORsCargos";
  document.forms[0].target="oculto";
  document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
  document.forms[0].submit();
}

function onClickHrefUOR() {
    var datos;
    var argumentos = new Array();
    
    if (document.forms[0].codOrganizaciones.value == '' || document.forms[0].codOrganizaciones.value == null) {
    	jsp_alerta('A', '<%=descriptor.getDescripcion("msjSelecOrg")%>');
    	return;
    }
    else if (document.forms[0].codEntidades.value == '' || document.forms[0].codEntidades.value == null) {
    	jsp_alerta('A', '<%=descriptor.getDescripcion("msjSelecEnt")%>');
    	return;
    } else {
    argumentos[0] = document.forms[0].codUORs.value;
   
    var source = APP_CONTEXT_PATH + "/administracion/UsuariosGrupos.do?opcion=modalUOR" +
                    "&codOrganizacion=" + document.forms[0].codOrganizaciones.value +
                    "&codEntidad=" + document.forms[0].codEntidades.value;

    abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + source, argumentos,
	'width=550,height=460',function(datos){
                        if(datos != null) {
                            document.forms[0].codUnidadesOrganicas.value = datos[2];
                            document.forms[0].descUORs.value = datos[1];
                            document.forms[0].codUORs.value = datos[0];
                        }
                        if((document.forms[0].codUORs.value != '') && (document.forms[0].descUORs.value == '')) {
                            document.forms[0].codUORs.value = '';
                            document.forms[0].codUORs.value = '';
                        }
                    });
    }
}

function onClickHrefCargo() {
    var datos;
    var argumentos = new Array();
    
    if (document.forms[0].codOrganizaciones.value == '' || document.forms[0].codOrganizaciones.value == null) {
    	jsp_alerta('A',  '<%=descriptor.getDescripcion("msjSelecOrg")%>');
    	return;
    }
    else if (document.forms[0].codEntidades.value == '' || document.forms[0].codEntidades.value == null) {
    	jsp_alerta('A',  '<%=descriptor.getDescripcion("msjSelecEnt")%>');
    	return;
    } else {
    
    argumentos[0] = document.forms[0].codCargos.value;
    var source = APP_CONTEXT_PATH + "/administracion/UsuariosGrupos.do?opcion=modalCargo" +
                    "&codOrganizacion=" + document.forms[0].codOrganizaciones.value +
                    "&codEntidad=" + document.forms[0].codEntidades.value;

    abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + source, argumentos,
	'width=750,height=475',function(datos){
                        if(datos != null) {
                            document.forms[0].codCargo.value = datos[2];
                            document.forms[0].descCargos.value = datos[1];
                            document.forms[0].codCargos.value = datos[0];
                        }
                        if((document.forms[0].codCargos.value != '') && (document.forms[0].descCargos.value == '')) {
                            document.forms[0].codCargo.value = '';
                            document.forms[0].codCargos.value = '';
                        }
                    });
}
}	

var operadorConsulta = '|&:<>!=';
var operadorConsultaNulo=operadorConsulta+'#';

function contieneOperadoresConsulta(campo,cjtoOp){
    var contiene=false;
    if(campo != null) {
        var v = campo.value;
        for (i = 0; i < v.length; i++){
            var c = v.charAt(i);
            if (cjtoOp.indexOf(c) != -1)  contiene=true;
        }
    }
    return contiene;
}

function onchangeCodUOR() {
    //if (!contieneOperadoresConsulta(document.forms[0].codUnidadesOrganicas,operadorConsulta)) {
        var uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].codUORs.value, 'A');
        if(uor != null) {
            document.forms[0].codUnidadesOrganicas.value = uor.uor_cod;
            document.forms[0].descUORs.value = uor.uor_nom;
        }
        else { // ha dado null para alta, buscamos de baja
            uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].codUORs.value, 'B');
            if(uor != null) {
                document.forms[0].codUnidadesOrganicas.value = uor.uor_cod;
                document.forms[0].descUORs.value = uor.uor_nom;
            }
        }
        if(uor == null) {
            //alert('no existe');
            document.forms[0].codUnidadesOrganicas.value = '';
            document.forms[0].descUORs.value = '';
            document.forms[0].codUORs.value = '';
        }
    //}
}

function onchangeCodCargo() {
    //if (!contieneOperadoresConsulta(document.forms[0].codUnidadesOrganicas,operadorConsulta)) {
        var cargo = buscarUorPorCodVisibleEstado(cargos, document.forms[0].codCargos.value, 'A');
        if(cargo != null) {
            document.forms[0].codCargo.value = cargo.uor_cod;
            document.forms[0].descCargos.value = cargo.uor_nom;
        }
        else { // ha dado null para alta, buscamos de baja
            cargo = buscarUorPorCodVisibleEstado(cargos, document.forms[0].codCargos.value, 'B');
            if(cargo != null) {
                document.forms[0].codCargo.value = cargo.uor_cod;
                document.forms[0].descCargos.value = cargo.uor_nom;
            }
        }
        if(cargo == null) {
            //alert('no existe');
            document.forms[0].codCargo.value = '';
            document.forms[0].descCargos.value = '';
            document.forms[0].codCargos.value = '';
        }
    //}
}

</SCRIPT>

</head>

<body class="bandaBody"
	onload="javascript:{ pleaseWait('off'); 
        inicializar()}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        

<html:form action="/administracion/UsuariosGrupos.do" target="_self">
    <html:hidden property="tipo_select" value="" />
    <html:hidden property="col_cod" value="" />
    <html:hidden property="col_desc" value="" />
    <html:hidden property="nom_tabla" value="" />
    <html:hidden property="input_cod" value="" />
    <html:hidden property="input_desc" value="" />
    <html:hidden property="column_valor_where" value="" />
    <html:hidden property="opcion" value="" />
    <html:hidden property="codEntidad" />
    <html:hidden property="nombreEntidad" />
    <input type="hidden" name="organizacion" id="organizacion">
    <input type="hidden" name="entidad" id="entidad">
        
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_UnidOrgUsu")%></div>
    <div class="contenidoPantalla">
        <TABLE id="tablaDatosGral">
            <tr>
                <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Organizacion")%>:</td>
                <td class="columnP">
                    <input type=text id="obligatorio" class="inputTextoObligatorio"
                           name="codOrganizaciones" size="8" maxlength="5"
                           onkeyup="return SoloDigitosNumericos(this);"/>
                    <input type=text style="width:450;height:17"
                           class="inputTextoObligatorio" name="descOrganizaciones"
                           readonly="true"/> 
                    <A href="" id="anchorOrganizaciones" name="anchorOrganizaciones"> 
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                             id="botonOrganizaciones" name="botonOrganizaciones"
                             style="cursor:hand;"></span> 
                    </A>
                </td>
            </tr>
            <tr>
                <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Entidad")%>:</td>
                <td class="columnP">
                    <input type=text id="obligatorio"  
                           class="inputTextoObligatorio" name="codEntidades" size="8"
                           onkeyup="return SoloDigitosNumericos(this);"/>
                    <input type="text" style="width:450;height:17"
                           class="inputTextoObligatorio" name="descEntidades" readonly/> 
                    <A href="" id="anchorEntidades" name="anchorEntidades"> 
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                             id="botonEntidades" name="botonEntidades"></span>
                    </A>
                </td>
            </tr>
            <tr>
                <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_unidOrg")%>:</td>
                <td class="columnP">
                    <input type="hidden" name="codUnidadesOrganicas" id="codUOR"/> 
                    <input type="text" class="inputTextoObligatorio" name="codUORs"
                           size="8" onkeyup="return xAMayusculas(this);"/>
                    <input type="text" style="width:450;height:17"
                           class="inputTextoObligatorioSinMayusculas" name="descUORs" readonly/>
                    <A href="javascript:{onClickHrefUOR()}" id="anchorUORs" name="anchorUORs"> 
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                             id="botonUORs" name="botonUORs"></span>
                    </A>
                </td>
            </tr>

            <tr>
                <td colspan="1"  class="etiqueta"><%=descriptor.getDescripcion("gEtiqTodas")%>
                    <input type="checkbox" class="" name="todas" id="todas" />                                                                        
                </td>

                <td colspan="1"  class="etiqueta"><%=descriptor.getDescripcion("gEtiqPermisosCascada")%>:
                    <input type="checkbox" class="" name="permisosCascada" id="permisosCascada" />
                </td>
            </tr>

            <tr>
                <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Cargo")%>:</td>
                <td class="columnP">
                    <input type="hidden" name="codCargo" id="codCargo"/> 
                    <input type="text" class="inputTexto" name="codCargos" size="8"
                           onkeyup="return xAMayusculas(this);"/>
                    <input type="text" style="width:450;height:17" class="inputTexto"
                           name="descCargos" readonly/> 
                           <A href="javascript:{onClickHrefCargo()}" id="anchorCargos" name="anchorCargos"> 
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                             id="botonCargos" name="botonCargos"></span> 
                    </A>
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <INPUT type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAceptar")%>' name="cmdAceptar" onClick="pulsarAceptar();" />
            <INPUT type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>' name="cmdSalir" onClick="pulsarSalir();" />
        </div>                        
    </div>                        
</html:form>
<script type="text/javascript">
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

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }
    if (teclaAuxiliar == 1){
        if (comboOrganizaciones.base.style.visibility == "visible" && isClickOutCombo(comboOrganizaciones,coordx,coordy)) setTimeout('comboOrganizaciones.ocultar()',20);
        if (comboEntidades.base.style.visibility == "visible" && isClickOutCombo(comboEntidades,coordx,coordy)) setTimeout('comboEntidades.ocultar()',20);
        if (comboUORs.base.style.visibility == "visible" && isClickOutCombo(comboUORs,coordx,coordy)) setTimeout('comboUORs.ocultar()',20);
        if (comboCargos.base.style.visibility == "visible" && isClickOutCombo(comboCargos,coordx,coordy)) setTimeout('comboCargos.ocultar()',20);
    }

    if (teclaAuxiliar == 9){
        if (comboOrganizaciones.base.style.visibility == "visible") comboOrganizaciones.ocultar();
        if (comboEntidades.base.style.visibility == "visible") comboEntidades.ocultar();
        if (comboUORs.base.style.visibility == "visible") comboUORs.ocultar();
        if (comboCargos.base.style.visibility == "visible") comboCargos.ocultar();
    }

    keyDel(evento);
}

var comboOrganizaciones = new Combo("Organizaciones");
var comboEntidades = new Combo("Entidades");
var comboUORs = new Combo("UORs");
var comboCargos = new Combo("Cargos");

comboOrganizaciones.change = function() {
	auxCombo = 'comboEntidades';
	comboEntidades.selectItem(-1);
	if (comboOrganizaciones.des.value.length != 0) {
		listaEntidades();
	} else {
		comboEntidades.addItems([], []);
	}
}	

comboEntidades.change = function() {
	comboUORs.selectItem(-1);
	comboCargos.selectItem(-1);
	if (comboEntidades.des.value.length != 0) {
		listaUORsCargos();
	} else {
		comboUORs.addItems([], []);
		comboCargos.addItems([], []);
	}
}

comboUORs.buscaCodigo = function() {
	onchangeCodUOR();
}
comboCargos.buscaCodigo = function() {
	onchangeCodCargo();
}
comboUORs.anchor.onclick = function() {}
comboCargos.anchor.onclick = function() {}


</script>
</BODY>
</html:html>
