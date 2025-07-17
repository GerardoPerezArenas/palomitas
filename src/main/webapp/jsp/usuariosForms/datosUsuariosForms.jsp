<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.usuariosforms.UsuariosFormsForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.usuariosforms.UsuariosFormsValueObject"%>
<%@page import="es.altia.agora.business.usuariosforms.UsuariosFormsPermisosVO"%>
<%@page import="java.util.Vector"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.business.util.ElementoListaValueObject"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html:html>
<head><jsp:include page="/jsp/usuariosForms/tpls/app-constants.jsp" />
<TITLE>::: FORMULARIOS  Datos de Usuarios:::</TITLE>

<jsp:include page="/jsp/plantillas/Metas.jsp" />


<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css>">


<%
  int idioma=1;
  int apl=1;
  int munic = 0;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
          munic = usuario.getOrgCod();
        }
  }%>
  
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>

<SCRIPT type="text/javascript">

var listaUsuariosTodos = new Array();
var cont = 0;
var cont1 = 0;
var cont2 = 0;
var cont3 = 0;
var loginOriginal = "";

var cod_categoria = new Array();
var desc_categoria = new Array();


// unidades organicas y cargos
var cod_uors = new Array();
var cod_interno_uors = Array();
var desc_uors = new Array();
var cod_cargos = new Array();
var desc_cargos = new Array();
var cod_interno_cargos = Array();

var permisosUsuarioOriginal = new Array();
var permisosUsuario = new Array();

<% //Pestaña de datos especiales. Solo para TVG.
String otros = "visible";
Config m_Conf = ConfigServiceHelper.getConfig("formulariosPdf");
String OtrosDatos = m_Conf.getString("datosUsuarios");
if("no".equals(OtrosDatos)) otros = "hidden";
%>

function validarNif(elemento,num) {
        var valores_numeros="0123456789";
        var correspondencia="TRWAGMYFPDXBNJZSQVHLCKE";
        var mensaje="";
        var letra="-";
        var longitud=elemento.value.length;
        elemento.value=elemento.value.toUpperCase();

        if (longitud==9)
                letra=elemento.value.substring(8);

        if (longitud>0)                
                        if (longitud==9 || longitud==8) {
                                for (var i=0;i<longitud;i++)
                                        if (i==8) {
                                                if (correspondencia.indexOf(elemento.value.charAt(i))<0) {
                                                        mensaje="El N.I.F. debe tener una letra en la última posición";
                                                }
                                        } else {
                                                if (valores_numeros.indexOf(elemento.value.charAt(i))<0) {
                                                        mensaje="El N.I.F. debe tener 8 dígitos.";
                                                }
                                        }
                        } else {
                                mensaje="El N.I.F. debe tener 9 caracteres.";
                        }

                        if (letra!="-") {
                                var numero=elemento.value.toUpperCase().substring(0,longitud-1);
                                modulo=Math.round(numero-(Math.floor(numero/23)*23));

                                if (elemento.value.toUpperCase().charAt(longitud-1)!=correspondencia.charAt(modulo))
                                        mensaje="A letra do NIF é incorrecta.";

                        } else {
                                var numero=elemento.value.toUpperCase().substring(0,longitud);
                                modulo=Math.round(numero-(Math.floor(numero/23)*23));

                                //if (num==0)
                                        elemento.value=elemento.value.toUpperCase()+correspondencia.charAt(modulo);
                        }                

                if (mensaje!="") {
                        if (num==0)
                                jsp_alerta("A",mensaje);
                        elemento.select();
                        return false;
                }
        return true;
}

function inicializar() {

    var h = 0;
    <%
    UsuariosFormsForm uForm = new UsuariosFormsForm();
    uForm = (UsuariosFormsForm) session.getAttribute("UsuariosFormsForm");
    UsuariosFormsValueObject uVO = new UsuariosFormsValueObject();
    uVO = uForm.getUsuariosForms();

    // lista con todos los permisos de un determinado usuario
    Vector listaPermisosUsuario = uForm.getListaUsuariosFormsPermisos();

    Vector lUsuariosForms = (Vector) uVO.getListaTodosUsuarios();

    if(lUsuariosForms != null) {
	for(int k=0;k<lUsuariosForms.size();k++) {
		GeneralValueObject g = new GeneralValueObject();
		g = (GeneralValueObject) lUsuariosForms.elementAt(k);
		%>
		listaUsuariosTodos[h] = ['<%=g.getAtributo("login")%>'];
		h++;
		<%
	}
    }

    // obtenemos la lista de permisos del usuario
    for(int z=0;z<listaPermisosUsuario.size();z++) {
        UsuariosFormsPermisosVO usuFormsPermisosVO = new UsuariosFormsPermisosVO();
	usuFormsPermisosVO = (UsuariosFormsPermisosVO) listaPermisosUsuario.elementAt(z);
	%>
	permisosUsuarioOriginal[h] = [ '<%=usuFormsPermisosVO.getCodUnidadOrganicaPermiso()%>','<%=usuFormsPermisosVO.getCodCargoPermiso()%>'];
        permisosUsuario[h] = ['<%=usuFormsPermisosVO.getCodVisibleUnidadOrganicaPermiso()%>','<%=usuFormsPermisosVO.getNombreUnidadOrganicaPermiso()%>' ,'<%=usuFormsPermisosVO.getCodVisibleCargoPermiso()%>',
               '<%=usuFormsPermisosVO.getNombreCargoPermiso()%>','<%=usuFormsPermisosVO.getCodUnidadOrganicaPermiso()%>','<%=usuFormsPermisosVO.getCodCargoPermiso()%>'];
	h++;
        //this.imprimirListaPermisosUsuario(0);
	<%
    }

    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    %>

    var argVentana = self.parent.opener.xanelaAuxiliarArgs;
    if(argVentana != null ) {
	loginOriginal = argVentana[0];
    }

    tp1.setSelectedIndex(0);

    <%
    String modo = "";
    modo = (String) session.getAttribute("modo");
    session.removeAttribute("modo");
    Log m_log = LogFactory.getLog(this.getClass().getName());
    if(m_log.isDebugEnabled()) m_log.debug("El modo es: " + modo);

    UsuariosFormsForm bForm =(UsuariosFormsForm)session.getAttribute("UsuariosFormsForm");

    //Combo de cargos del usuario.
    Vector listaPerfiles = bForm.getListaPerfiles();
    int lengthPerfiles = listaPerfiles.size();
    int i = 0;
    %>
    var j=0;
    <%
    for(i=0;i<lengthPerfiles;i++){
            GeneralValueObject perfiles = (GeneralValueObject)listaPerfiles.get(i);
            %>
            cod_cargos[j] = ['<%=(String)perfiles.getAtributo("codPerfil")%>'];
            desc_cargos[j] = ['<%=(String)perfiles.getAtributo("descPerfil")%>'];
            cod_interno_cargos [j] = ['<%=(String)perfiles.getAtributo("cod_interno_Perfil")%>'];
            j++;
    <%}%>

    //Combo de categorías profesionales del usuario.
    <% Vector listaCatProfesionales = bForm.getListaCatProfesionales();
    int lengthCategorias = listaCatProfesionales.size(); %>
    j=0;
    <%
    for(i=0;i<lengthCategorias;i++){
            GeneralValueObject cat = (GeneralValueObject)listaCatProfesionales.get(i);
            %>
            cod_categoria[j] = ['<%=(String)cat.getAtributo("cod_catProf")%>'];
            desc_categoria[j] = ['<%=(String)cat.getAtributo("desc_catProf")%>'];
            j++;
    <%}%>

    //Combo de unidades organicas del usuario.
    <% Vector listaDepartamentos = bForm.getListaUors();
    int lengthDepartamentos = listaDepartamentos.size();
    %>
    j=0;
    <%for(i=0;i<lengthDepartamentos;i++){
            GeneralValueObject departamento = (GeneralValueObject)listaDepartamentos.get(i);
            %>
            cod_uors[j] = ['<%=(String)departamento.getAtributo("cod_departamento")%>'];
            desc_uors[j] = ['<%=StringEscapeUtils.escapeJavaScript((String)departamento.getAtributo("desc_departamento")) %>'];
            cod_interno_uors [j] = ['<%=(String)departamento.getAtributo("cod_interno_dep")%>'];
            j++;
    <%}%>

    //Entidades de la empresa.
    if(document.forms[0].codEntidad.value=="C"){document.forms[0].entidad[0].checked=true};
    if(document.forms[0].codEntidad.value=="R"){document.forms[0].entidad[1].checked=true};
    if(document.forms[0].codEntidad.value=="T"){document.forms[0].entidad[2].checked=true};

    //Carga de combos.

 
    comboCategorias.addItems (cod_categoria,desc_categoria);
    
    comboUOR.addItems(cod_uors, desc_uors);
   
    comboCargo.addItems(cod_cargos,desc_cargos);

    // tabla Unidades organicas
    tabUnidadesOrganicas.lineas = permisosUsuario;
    refrescaUnidadesOrganicas();

    if (document.forms[0].codCatProfesional.value=="") {
        comboCategorias.buscaLinea(-1);
    }
}//Fin inicializar

function pulsarSalir() {
    self.parent.opener.retornoXanelaAuxiliar();
}

var JS_DEBUG_LEVEL = 50;

                            function pulsarAceptar () {
                                var desc = document.forms[0].login.value;
                                var yaExiste = 0;
                                var t=0;

                                for(l=0; l < listaUsuariosTodos.length; l++){
                                    if(loginOriginal != "") {
                                        if ((listaUsuariosTodos[l][0]) == desc && loginOriginal != desc){
                                            yaExiste = 1;
                                        }
                                    } else {
                                        if ((listaUsuariosTodos[l][0]) == desc){
                                            yaExiste = 1;
                                        }
                                    }
                                }

                                if(loginOriginal!=desc){
                                    t=1;
                                }

                                if(document.forms[0].entidad[0].checked){document.forms[0].codEntidad.value="C"};
                                if(document.forms[0].entidad[1].checked){document.forms[0].codEntidad.value="R"};
                                if(document.forms[0].entidad[2].checked){document.forms[0].codEntidad.value="T"};

                                var nif = document.forms[0].nif;
                                var password = document.forms[0].password.value;
                                var password2 = document.forms[0].password2.value;

                                if (document.forms[0].codCatProfesional.value=="") {document.forms[0].codCatProfesional.value="999"}

                                if (password == password2) {
                                    if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
                                        if(validarNif(nif,0)){
                                        if (usuarioTienePermisos()){

<% if("alta".equals(modo)){ %>
                    if(yaExiste == 0 ) {
                        jsp_alerta('A','<%=descriptor.getDescripcion("msjUsuarioSge")%>');
                        crearListasPermisos();
                        document.forms[0].opcion.value="insertarUsuario";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/UsuariosForms.do";
                        document.forms[0].submit();

                    } else {
                        jsp_alerta('A','<%=descriptor.getDescripcion("msjLoginExist")%>');
                    }
<%} else { %>
                    if(yaExiste == 0 && t==0) {
                        jsp_alerta('A','<%=descriptor.getDescripcion("msjUsuarioSge")%>');
                        crearListasPermisos();
                        document.forms[0].opcion.value="grabar_modificacion";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/UsuariosForms.do";
                        document.forms[0].submit();
                    } else if (yaExiste==0 && t==1){
                        jsp_alerta ('A','<%=descriptor.getDescripcion("msjCambioLogin")%>');
                    } else {
                        jsp_alerta('A','<%=descriptor.getDescripcion("msjLoginExist")%>');
                    }
<% } %>
                }else{  // sino tiene permisos
                     jsp_alerta('A','<%=descriptor.getDescripcion("msjUsuSinPermisos")%>');
                }
                }
            }

        } else {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjContrDif")%>');
        }

    }//Fin pulsarAceptar

     function devolver () {
        var retorno = new Array();
        self.parent.opener.retornoXanelaAuxiliar(retorno);
     }



     function selectRow(rowID,tableName){
          if(tabUnidadesOrganicas.selectedIndex >= 0) {
            tabUnidadesOrganicas.desactivaRow(tabUnidadesOrganicas.selectedIndex);
        }

          tabUnidadesOrganicas.selectedIndex = rowID;
          tabUnidadesOrganicas.activaRow(rowID);

          if(tabUnidadesOrganicas.rowID != -1) {
              document.forms[0].codUOR.value =  permisosUsuario[rowID][0];
              document.forms[0].descUOR.value =  permisosUsuario[rowID][1];
              document.forms[0].codCargo.value = permisosUsuario[rowID][2];
              document.forms[0].descCargo.value = permisosUsuario[rowID][3];
        }
     }



    function pulsarAltaTablaUnidadesOrganicas(){
        var codUOR = document.forms[0].codUOR.value;
        var codCargo = document.forms[0].codCargo.value;
        var codInternoUOR = obtenerCodInterno(codUOR,0);
        var codInternoCargo = obtenerCodInterno(codCargo,1);
       
        var yaExiste = 0;

        if(comprobarObligatoriosTablaUORS() ) {
            for(l=0; l < permisosUsuarioOriginal.length; l++){
                if (permisosUsuario[l][4] == codInternoUOR ){
                    yaExiste = 1;
                }
            }

            if(yaExiste == 0) {
                var lineas = tabUnidadesOrganicas.lineas;
                i = lineas.length;
                //if (document.forms[0].codCargo.value=="") {
                  //  document.forms[0].codCargo.value="0";
                //}
                permisosUsuario[i]=[document.forms[0].codUOR.value, document.forms[0].descUOR.value, document.forms[0].codCargo.value, document.forms[0].descCargo.value, codInternoUOR, codInternoCargo];
                permisosUsuarioOriginal[i]=[codInternoUOR, codInternoCargo];

                tabUnidadesOrganicas.lineas = permisosUsuario;
                refrescaUnidadesOrganicas();
                borrarDatosTablaUnidadesOrganicas();
            } else {
                jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>' + ": " + document.forms[0].codUOR.value + " - "+ document.forms[0].descUOR.value);
            }        
        } else {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosPermiso")%>');
        }
}



function pulsarModificarTablaUnidadesOrganicas() {
    if(tabUnidadesOrganicas.selectedIndex != -1) {
        var codUOR = document.forms[0].codUOR.value;
        var codCargo = document.forms[0].codCargo.value;
        var codInternoUOR = obtenerCodInterno(codUOR,0);
        var codInternoCargo = obtenerCodInterno(codCargo,1);

        var yaExiste = 0;

        if(comprobarObligatoriosTablaUORS()) {
          for(l=0; l < permisosUsuarioOriginal.length; l++){
                if (permisosUsuario[l][4] == codInternoUOR){
                  yaExiste = 1; // ya existe esa unidad organica, ya tiene un cargo asociado
                }              
          }
          if(yaExiste == 0) {   // si no existe lo damos de alta
              var j = tabUnidadesOrganicas.selectedIndex;
              permisosUsuario[j]=[document.forms[0].codUOR.value, document.forms[0].descUOR.value, document.forms[0].codCargo.value, document.forms[0].descCargo.value,codInternoUOR, codInternoCargo];
              permisosUsuarioOriginal[j]=[codInternoUOR, codInternoCargo];
              
              tabUnidadesOrganicas.lineas=permisosUsuario;
              refrescaUnidadesOrganicas();
              borrarDatosTablaUnidadesOrganicas();

          } else {  // si existe la unidad organica, modificamos el cargo que habia por el actual
                    // confirmacion de la modificacion del cargo
                if (jsp_alerta('C','<%=descriptor.getDescripcion("msjCambiarCargoUsuario")%>') ){
                    var j = tabUnidadesOrganicas.selectedIndex;
                    var permisoActual = permisosUsuario[j];                   
                        this.imprimirListaPermisosUsuario(0);
                    permisosUsuario[j]=[permisoActual[0], permisoActual[1], document.forms[0].codCargo.value, document.forms[0].descCargo.value,permisoActual[4], codInternoCargo];
                    permisosUsuarioOriginal[j]=[permisoActual[j][4], codInternoCargo];
                        this.imprimirListaPermisosUsuario(0);
                    tabUnidadesOrganicas.lineas=permisosUsuario;
                    refrescaUnidadesOrganicas();
                    borrarDatosTablaUnidadesOrganicas();
                }
          }
        } else {
          jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosPermiso")%>');
        }
    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');

    }


    function pulsarEliminarTablaUnidadesOrganicas(){
        
        if(tabUnidadesOrganicas.selectedIndex != -1) {
            if(jsp_alerta('', '<%=descriptor.getDescripcion("msjPermisoExiste")%>')) {
                var list = new Array();
                var listOrig = new Array();
                tamIndex= tabUnidadesOrganicas.selectedIndex;
                tamLength= tabUnidadesOrganicas.lineas.length;                

                for (i=tamIndex - 1; i < permisosUsuario.length - 1; i++){
                    if (i + 1 <= permisosUsuario.length - 2){
                        permisosUsuario[i + 1]=permisosUsuario[i + 2];
                        permisosUsuarioOriginal[i + 1]=permisosUsuarioOriginal[i + 2];
                    }
                }
                for(j=0; j < permisosUsuario.length-1 ; j++){
                    list[j] = permisosUsuario[j];
                    listOrig[j] = permisosUsuarioOriginal[j];
                }
                tabUnidadesOrganicas.lineas=list;
                refrescaUnidadesOrganicas();
                borrarDatosTablaUnidadesOrganicas();

                permisosUsuario=list;
                permisosUsuarioOriginal = listOrig;
            } else {
                tabUnidadesOrganicas.selectLinea( tabUnidadesOrganicas.selectedIndex);
                tabUnidadesOrganicas.selectedIndex = -1;
                borrarDatosTablaUnidadesOrganicas();
            }
        } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }


    

    function pulsarLimpiarTablaUnidadesOrganicas() {
         this.borrarDatosTablaUnidadesOrganicas();
    }


function crearListasPermisos() {
  var listaCodUnidadesOrganicas = "";
  var listaCodCargos = "";

  for (i=0; i < permisosUsuarioOriginal.length; i++) {
    listaCodUnidadesOrganicas +=permisosUsuarioOriginal[i][0]+'§¥';
    listaCodCargos +=permisosUsuarioOriginal[i][1]+'§¥';
  }
  document.forms[0].listaCodUnidadesOrganicas.value = listaCodUnidadesOrganicas;
  document.forms[0].listaCodCargos.value = listaCodCargos;
  
  return;
}


// obtenemos el codigo interno tanto de una unidad organica como de un cargo a partir del codigo visible que se le pase como parametro.
function obtenerCodInterno(codVisible, tipo){
    var resultado;
    if (tipo == 0){
        for(j=0; j < cod_uors.length; j++){
            if ((cod_uors[j] == codVisible)  ){
                resultado = cod_interno_uors [j];
            }
        }
    }else if (tipo==1){
        for(j=0; j < cod_cargos.length; j++){
            if ((cod_cargos[j] == codVisible)  ){
                resultado = cod_interno_cargos [j];
            }
        }
    }
    return resultado;
}

function imprimirListaPermisosUsuario(tipo){
    if (tipo==0){ // lista que se muestra en pantalla de permisos del usuario
        for(l=0; l < permisosUsuario.length; l++){
            //alert("  permisosUsuario[" + l + "][0]=" + permisosUsuario[l][0] + "  permisosUsuario[" + l + "][1]=" + permisosUsuario[l][1] + "  permisosUsuario[" + l + "][2]=" + permisosUsuario[l][2] +
            //"  permisosUsuario[" + l + "][3]=" + permisosUsuario[l][3] + "  permisosUsuario[" + l + "][4]=" + permisosUsuario[l][4] + "  permisosUsuario[" + l + "][5]=" + permisosUsuario[l][5]);
        }
    }
}

function comprobarObligatoriosTablaUORS() {
    if ((document.forms[0].descUOR.value == '') || (document.forms[0].descCargo.value == '')
        || (document.forms[0].descCargo.value == '') || (document.forms[0].codCargo.value == '') ){
        return false;
    } else {
        return true;
    }
}

function borrarDatosTablaUnidadesOrganicas(){
    document.forms[0].codUOR.value = "";
    document.forms[0].descUOR.value = "";
    document.forms[0].codCargo.value = "";
    document.forms[0].descCargo.value = "";
    document.forms[0].botonCargo.style.color="#0B3090 !important";
    var comboCargos = [document.forms[0].codCargo,document.forms[0].descCargo,document.forms[0].botonCargo];
    habilitarGeneral(comboCargos);
   
     if(tabUnidadesOrganicas.selectedIndex >= 0) {

         tabUnidadesOrganicas.desactivaRow(tabUnidadesOrganicas.selectedIndex);
         tabUnidadesOrganicas.restoreIndex();         
    }
}

function usuarioTienePermisos(){
    if (permisosUsuario.length > 0) {
        return true;
    }
    else {
        return false;
    }
}

</SCRIPT>

</head>

<body class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        inicializar()}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>


<html:form action="/UsuariosForms.do" target="_self">
<html:hidden  property="opcion" value=""/>
<input type="hidden" name="listaCodUnidadesOrganicas" value="" />
<input type="hidden" name="listaCodCargos" value="" />

<table  width="100%" height="500px" cellpadding="0px" cellspacing="0px">
	<tr>
		<td width="805px" height="500px">
			<table width="100%" height="100%" cellpadding="1px" valign="middle" cellspacing="0px" border="0px" bgcolor="#666666">
			<tr>
				<td>
					<table width="100%" height="100%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
						<tr>
							<td id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_datUsu")%></td>
						</tr>
						<tr>
							<td width="100%" height="1px" bgcolor="#666666"></td>
						</tr>
						<tr>
							<td width="100%" bgcolor="#e6e6e6" align="center" valign="top">
                                                            <!-- Separador. -->
                                                            <table height="3px" cellpadding="0px" cellspacing="0px">
                                                                <tr>
                                                                    <td></td>
                                                                </tr>
                                                            </table>
                                           



<!-- Fin separador. -->

<div class="tab-pane" id="tab-pane-1">
<script type="text/javascript"> tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
</script>


	<!-- CAPA 1: DATOS GENERALES ------------------------------ -->
        <div class="tab-page" id="tabPage1" style="height:400px" >

            <h2 class="tab"><%=descriptor.getDescripcion("gEtiq_datGen")%></h2>

            <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>

            <TABLE id ="tablaDatosGral" width="740px" cellspacing="4px" cellpadding="3px" border="0" bgcolor="#FFFFFF">

                <tr>
                    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Nombre")%>:</td>
                    <td width="80%" class="columnP">
                        <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="nombre" size="25" maxlength="20"/>
                    </td>
                </tr>


                <tr>
                    <td width="20%"  class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Apellido1")%>:</td>
                    <td width="80%" class="columnP">
                        <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="apellido1" size="25" maxlength="20"/>
                    </td>
                </tr>

                <tr>
                    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Apellido2")%>:</td>
                    <td width="80%" class="columnP">
                            <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="apellido2" size="25" maxlength="20"/>
                    </td>
                </tr>


                <tr>
                    <td width="20%"  class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Nif")%>:</td>
                    <td width="80%" class="columnP">
                        <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="nif" size="15" maxlength="9"
                           onkeypress="javascript:PasaAMayusculas(event);"/>
                    </td>
                </tr>

                <tr>
                    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_login")%>:</td>
                    <td width="80%" class="columnP">
                        <html:text styleId="obligatorio" styleClass="inputTextoDeshabilitado" property="login" size="20" maxlength="250"/>
                    </td>
                </tr>

                <tr>
                    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Pass")%>:</td>
                    <td width="80%" class="columnP">
                        <html:password styleId="obligatorio" styleClass="inputTextoObligatorio" property="password" size="20" maxlength="20"/>
                    </td>
                </tr>

                <tr>
                    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Pass2")%>:</td>
                    <td width="80%" class="columnP">
                        <html:password styleId="obligatorio" styleClass="inputTextoObligatorio" property="password2" size="20" maxlength="20"/>
                    </td>
                </tr>

                <tr>
                    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Mail")%>:</td>
                    <td width="80%" class="columnP">
                        <html:text styleClass="inputTexto" property="email" size="40" maxlength="100"/>
                    </td>
                </tr>

               

                <tr>
                    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_estado")%>:</td>
                    <td width="80%" class="columnP">
                    <html:select styleId="obligatorio" styleClass="inputTextoObligatorio" property="bloqueado">
                        <html:option value="0"><%=descriptor.getDescripcion("gEtiq_Dblq")%> </html:option>
                        <html:option value="1"><%=descriptor.getDescripcion("gEtiq_Bloq")%> </html:option>
                    </html:select>
                    </td>
                </tr>

            </table>
        </div>

          <!-- CAPA 2: ------------------------------ -->
          <div  class="tab-page" id="tabPage3" style="height:400px">
              <h2  class="tab"><%=descriptor.getDescripcion("etiq_uors")%></h2>
              <script type="text/javascript">tp1_p3 = tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>

               <TABLE id ="tablaUnidadesOrganicas" width="70%" cellspacing="4px" cellpadding="3px" border="0" bgcolor="#FFFFFF">
                   <tr>
                   <td style="width: 72%" id="tablaUORS"></td>
                    <td widht="25%" align="center">
                        <input type= "button" class="botonGeneral" style="margin:5px" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAltaTablaUnidadesOrganicas" onclick="pulsarAltaTablaUnidadesOrganicas();">
                        <input type= "button" class="botonGeneral" style="margin:5px" value="<%=descriptor.getDescripcion("gbModificar")%>" name="cmdModificarTablaUnidadesOrganicas" onclick="pulsarModificarTablaUnidadesOrganicas();">
                        <input type= "button" class="botonGeneral" style="margin:5px" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminarTablaUnidadesOrganicas" onclick="pulsarEliminarTablaUnidadesOrganicas();">
                        <input type= "button" class="botonGeneral" style="margin:5px" value="<%=descriptor.getDescripcion("etiqLimpiar")%>" name="cmdLimpiarTablaUnidadesOrganicas" onclick="pulsarLimpiarTablaUnidadesOrganicas();">
                    </td>
                   </tr>
                   <tr><td height="4px"></td></tr>

                   <tr>
                       <td width="100%">
                       <table width="100%" rules="none" border="0px" align="center"  cellpadding="0" cellspacing="0">
                       <tr>

                            <td width="9%" class="etiqueta"><%=descriptor.getDescripcion("etiqUOR")%>:</td>
                            <td width="41%" class="columnP">
                                
                                <html:text  styleClass="inputTextoObligatorio" property="codUOR" size="6"
                                          onkeypress="javascript:return PasaAMayusculas(event);"/>
                                <html:text  styleClass="inputTextoObligatorio" style="width:150px" property="descUOR"
                                          size="25" readonly="true"/>
                                <A href="" id="anchorUOR" name="anchorUOR"
                                    >
                                   <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonUOR"
                                        name="botonUOR"></span>
                                </A>
                            </td>

                            <td width="8%" class="etiqueta"><%=descriptor.getDescripcion("etiqCargo")%>:</td>
                            <td width="42%" class="columnP">
                                <html:text styleClass="inputTextoObligatorio" property="codCargo" size="6"
                                onkeypress="javascript:return SoloDigitos(event);"/>
                                <html:text  styleClass="inputTextoObligatorio" style="width:150px" property="descCargo" size="25px" readonly="true"/>
                                <A href="" id="anchorCargo" name="anchorCargo"
                                   >
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonCargo" name="botonCargo"></span>
                                </A>
                            </td>
                        </tr>
                        </table>
                       </td> 
                   </tr>

               </TABLE>
          </div> 




	<!-- CAPA 2: ------------------------------ -->

        <div style="visibility:<%=otros%>" class="tab-page" id="tabPage2" style="height:450px">

            <h2 style="visibility:<%=otros%>" class="tab"><%=descriptor.getDescripcion("gEtiq_Otros")%></h2>

            <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>

            <TABLE id ="tablaOtros" width="740px" cellspacing="0px" cellpadding="1px" border="0" bgcolor="#FFFFFF">

                <tr>
                    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_CatProf")%>:</td>
                    <td width="80%" class="columnP">
                <html:text styleClass="inputTexto" property="codCatProfesional" size="2"
                           onkeypress="javascript:return SoloDigitos(event);"/>
                <html:text styleClass="inputTexto" property="descCatProfesional"
                           size="35" readonly="true"/>
                <A href="" id="anchorCatProfesional" name="anchorCatProfesional">
                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonCatProfesional"
                         name="botonCatProfesional"></span>
                </A>
                </td>
                </tr>
                <tr>
                    <TD width="30%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Entidad")%>:</TD>
                    <TD width="70%" class="columnP">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <html:hidden styleClass="inputTexto" property="codEntidad"/>
                            <tr>
                                <td width="22%" class="columnP" valign="top">
                                    <input type="radio" name="entidad" class="textoSuelto" value="C"><%=descriptor.getDescripcion("gEtiq_crtvg")%>
                                </td>
                                <td width="22%" class="columnP" valign="top">
                                    <input type="radio" name="entidad" class="textoSuelto" value="R"><%=descriptor.getDescripcion("gEtiq_rg")%>
                                </td>
                                <td width="22%" class="columnP" valign="top">
                                    <input type="radio" name="entidad" class="textoSuelto" value="T"><%=descriptor.getDescripcion("gEtiq_tvg")%>&nbsp;
                                </td>
                                <td width="33%"></td>
                            </tr>
                        </table>
                    </TD>
                </tr>
            </TABLE>
        </div>

<div id="tablaBotones" class="botoneraPrincipal">
    <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar" 
             onClick="pulsarAceptar();">
    <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir"  onClick="pulsarSalir();">
</div>

</html:form>

<script language="JavaScript1.2">  

var comboCategorias = new Combo("CatProfesional");
var comboUOR = new Combo("UOR");
var comboCargo = new Combo("Cargo");

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
        if (comboCategorias.base.style.visibility == "visible" && isClickOutCombo(comboCategorias,coordx,coordy)) setTimeout('comboCategorias.ocultar()',20);
        if (comboUOR.base.style.visibility == "visible" && isClickOutCombo(comboUOR,coordx,coordy)) setTimeout('comboUOR.ocultar()',20);
        if (comboCargo.base.style.visibility == "visible" && isClickOutCombo(comboCargo,coordx,coordy)) setTimeout('comboCargo.ocultar()',20);
    }
    if (teclaAuxiliar == 9){
        comboCategorias.ocultar();
        comboUOR.ocultar();
        comboCargo.ocultar();

    }

	keyDel(evento);
}

var tabUnidadesOrganicas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaUORS'));


tabUnidadesOrganicas.addColumna('70','center','');
tabUnidadesOrganicas.addColumna('300','center','<%= descriptor.getDescripcion("etiqUOR")%>');
tabUnidadesOrganicas.addColumna('100','center','');
tabUnidadesOrganicas.addColumna('100','center','<%= descriptor.getDescripcion("etiqCargo")%>');
tabUnidadesOrganicas.addColumna('0','center','');
tabUnidadesOrganicas.addColumna('0','center','');

tabUnidadesOrganicas.displayCabecera=true;

function refrescaUnidadesOrganicas() {
  tabUnidadesOrganicas.displayTabla();
}


</script></BODY></html:html>
