<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.InteresadosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%	UsuarioValueObject usuarioVO = new UsuarioValueObject();
	int idioma=1;
	int apl=4;
	if (session.getAttribute("usuario") != null){
		usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
		apl = usuarioVO.getAppCod();
		idioma = usuarioVO.getIdioma();
	}
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>Interesados</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaBusquedaTerceros.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    <script type="text/javascript">
        var vectorCamposRejilla = ['nombre','codRol','descRol','codMunicipio','codProcedimiento','ejercicio','numero','tercero','versionTercero','domicilio','descDomicilio'];
        var listaTabla = new Array();
        var listaInteresados = new Array();
        var lista = new Array();
        var expediente = new Array();
        var datosRejilla = new Array();
        var codRols = new Array();
        var descRols = new Array();
        var pDef = new Array();
        // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
        function inicializar(){
            expediente = self.parent.opener.xanelaAuxiliarArgs;
            listaInteresados= new Array();
            listaInteresados = expediente[4];
        <%  Vector listaRoles = new Vector();
            InteresadosForm intForm =(InteresadosForm)session.getAttribute("InteresadosForm");
            listaRoles = (Vector) intForm.getListaRoles();
            int lengthRoles = listaRoles.size();%>
        var m=0;
        <%  for(int t=0;t<lengthRoles;t++){%>
                codRols[m] = [	'<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("codRol")%>'];
                descRols[m] = ['<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("descRol")%>'];
                pDef[m] = ['<%=(String)((GeneralValueObject)listaRoles.get(t)).getAtributo("porDefecto")%>'];
                m++;
        <%  }%>                
        
            for (var j=0; j<listaInteresados.length; j++) {
                    listaTabla[j] = [listaInteresados[j][2],listaInteresados[j][5],listaInteresados[j][3]];
            }
            tablaInteresados.lineas = listaTabla;
            tablaInteresados.displayTabla();
            comboRol.addItems(codRols,descRols);
            if(expediente[5] == "soloConsulta") {
              var vector = [document.forms[0].botonT,document.forms[0].botonTer,document.forms[0].cmdVer,
                            document.forms[0].cmdAlta,document.forms[0].cmdModificar,document.forms[0].cmdEliminar,
                            document.forms[0].cmdLimpiar];
              deshabilitarGeneral(vector);
              comboRol.deactivate();
              tablaInteresados.readOnly = true;
            }
        }
        // FUNCIONES DE LIMPIEZA DE CAMPOS
        function limpiarFormulario(){
            tablaInteresados.lineas = new Array();
            tablaInteresados.displayTabla();
            limpiar(vectorCamposRejilla);
        }

        function limpiarCamposRejilla(){
            limpiar(vectorCamposRejilla);
        }

        // FUNCIONES DE PULSACION DE BOTONES

function pulsarSalir() {
    
    var listas = crearListas();
    var listaCodTercero = listas[0];
    var listaVersionTercero = listas[1];
    var listaCodDomicilios = listas[2];
    var listaRol = listas[3];
    var listaMostrar = listas[4];
    var titular = buscaPrincipal();
    var retorno = new Array();
    retorno = [listaCodTercero, listaVersionTercero, listaCodDomicilios, listaRol, listaMostrar, titular, listaInteresados];
    self.parent.opener.retornoXanelaAuxiliar(retorno);
}        

        function buscaPrincipal(){
            var res = "";
            for(i=0;(i<listaInteresados.length);i++){
                if(listaInteresados[i][8] == 1){
                    res = listaInteresados[i][2];
                    break;
                } else {
                    res = listaInteresados[listaInteresados.length-1][2];
                }
            }
            return res;
        }

        function pulsarEliminar() {
            if(tablaInteresados.selectedIndex != -1) {
                if(validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
                    if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarInt")%>')) {
                        
                        var list = new Array();
                        var listOriginal = new Array();
                        tamIndex=tablaInteresados.selectedIndex;
                        tamLength=tablaInteresados.lineas.length;
                                                
                        for (i=0; i<tamLength-1; i++){
                            if(i<tamIndex){
                                list[i] = listaTabla[i];
                                listOriginal[i] = listaInteresados[i];
                            }else{
                                list[i]=listaTabla[i+1];
                                listOriginal[i]=listaInteresados[i+1];
                            }
                        }
                        
                        // Actualizamos el interesado marcado para mostrar si es necesario
                        // En necesario cuando se elimina el interesado marcado para mostrar.
                        if (listaInteresados[tamIndex][8] == true && listOriginal.length > 0) {
                            // Buscamos el primer interesado con Rol por Defecto y se lo asignamos.
                            var marcado = false;
                            for (i = 0; i < listOriginal.length; i++) {
                                if (listOriginal[i][6] == true) {
                                    listOriginal[i][8] = true;
                                    marcado = true;
                                    break;
                                }    
                            }
                            //Si no hemos encontrado ninguno, marcamos el primer interesado de la lista.
                            if (!marcado) {
                                listOriginal[0][8] = true;
                            }    
                        }
                        
                        listaTabla=list;
                        listaInteresados = listOriginal;
                        
                        tablaInteresados.lineas=list;
                        tablaInteresados.displayTabla();
                        limpiar(vectorCamposRejilla);

                    } else {
                        tablaInteresados.selectLinea(tablaInteresados.selectedIndex);
                        tablaInteresados.selectedIndex = -1;
                        limpiar(vectorCamposRejilla);
                    }
                } else {
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
                }
            } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
        }

        function pulsarModificar() {
            var domic = document.forms[0].domicilio.value;
            var yaExiste = 0;
            if(tablaInteresados.selectedIndex != -1) {
                if (validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {

                    for(var l=0; l < listaTabla.length; l++){
                        if(l != tablaInteresados.selectedIndex) {
                            if ((listaTabla[l][1]) == domic) yaExiste = 1;
                        }
                    }

                    if(yaExiste == 0) {
                        listaTabla[tablaInteresados.selectedIndex]=[document.forms[0].nombre.value,document.forms[0].descDomicilio.value,document.forms[0].descRol.value];
                        var pD = "";
                        
                        for(var u=0;u<codRols.length;u++) {
                            if(document.forms[0].codRol.value == codRols[u][0]) {
                                if (pDef[u][0] == 1) pD = true;
                                else pD = false;
                            }
                        }
                        
                        // Vamos a actualizar si el interesado debe ser marcado como para mostrar o no.
                        var mostrar = false;
                        if (listaInteresados.length == 1) {
                            // El primer interesado siempre se marca como para mostrar.
                            mostrar = true;
                        } else if (pD == true) {
                            // Si el que vamos a dar de alta es Rol por Defecto, comprobamos que ninguno de los existentes lo sea.
                            var indexMostrar = -1;
                            var existePorDefecto = false;
                            for (var u = 0; u < listaInteresados.length; u++) {
                                if (listaInteresados[u][6] == true) existePorDefecto = true;
                                if (listaInteresados[u][8] == true) indexMostrar = u;
                            }    
                            
                            if (!existePorDefecto) {
                                if (indexMostrar >= 0) listaInteresados[indexMostrar][8] = false;
                                mostrar = true;
                            }    
                        }    
                        
                        listaInteresados[tablaInteresados.selectedIndex] = [document.forms[0].tercero.value,document.forms[0].versionTercero.value,
                                                                            document.forms[0].nombre.value,document.forms[0].descRol.value,
                                                                            document.forms[0].domicilio.value,document.forms[0].descDomicilio.value,
                                                                            pD,document.forms[0].codRol.value, mostrar];
                        tablaInteresados.lineas=listaTabla;
                        tablaInteresados.displayTabla();
                        limpiar(vectorCamposRejilla);
                    } else {
                        jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                    }
                } else {
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
                }
            } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
        }

        function pulsarAlta() {
            var domic = document.forms[0].domicilio.value;
            var yaExiste = 0;
            if(validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
                if(noEsta()){
                    
                    listaTabla[listaTabla.length] = [document.forms[0].nombre.value, 
                        document.forms[0].descDomicilio.value, document.forms[0].descRol.value];
                    
                    var pD = "";                    
                    for(var u=0; u<codRols.length; u++) {
                        if(document.forms[0].codRol.value == codRols[u][0]) {
                            if (pDef[u][0] == 1) pD = true;
                            else pD = false;
                        }
                    }
                    
                    // Vamos a comprobar si tiene que ser marcado como para mostrar en listados.
                    var mostrar = false;
                    if (listaInteresados.length == 0) {
                        // El primer interesado siempre se marca como para mostrar.
                        mostrar = true;
                    } else if (pD == true) {
                        // Si el que vamos a dar de alta es Rol por Defecto, comprobamos que ninguno de los existentes lo sea.
                        var indexMostrar = -1;
                        var existePorDefecto = false;
                        for (var u = 0; u < listaInteresados.length; u++) {
                            if (listaInteresados[u][6] == true) existePorDefecto = true;
                            if (listaInteresados[u][8] == true) indexMostrar = u;
                        }    
                        
                        if (!existePorDefecto) {
                            if (indexMostrar >= 0) listaInteresados[indexMostrar][8] = false;
                            mostrar = true;
                        }    
                    }    
                        
                    listaInteresados[listaInteresados.length] = [document.forms[0].tercero.value,document.forms[0].versionTercero.value,
                                            document.forms[0].nombre.value,document.forms[0].descRol.value,
                                            document.forms[0].domicilio.value,document.forms[0].descDomicilio.value,
                                            pD,document.forms[0].codRol.value, mostrar];                                                            
                    
                    tablaInteresados.lineas=listaTabla;
                    tablaInteresados.displayTabla();
                    limpiar(vectorCamposRejilla);
                } else {
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                }
            }
        }

        function crearListas() {
            var listaCodTercero = "";
            var listaVersionTercero = "";
            var listaCodDomicilios= "";
            var listaRol = "";
            var listaMostrar = "";
            for (i=0; i < listaTabla.length; i++) {
                listaCodTercero += listaInteresados[i][0]+'зе';
                listaVersionTercero += listaInteresados[i][1]+'зе';
                listaRol += listaInteresados[i][7]+ 'зе';
                listaCodDomicilios += listaInteresados[i][4]+'зе';
                listaMostrar += listaInteresados[i][8] + 'зе';
            }
            var listas = new Array();
            listas = [listaCodTercero, listaVersionTercero, listaCodDomicilios, listaRol, listaMostrar];
            return listas;
        }

        // FUNCIONES DE VALIDACION Y COMPROBACION DEL FORMULARIO
        function noEsta(){
            var cod = document.forms[0].tercero.value;
            var ver = document.forms[0].versionTercero.value;
            for(i=0;(i<listaInteresados.length);i++){
                    if((listaInteresados[i][0] == cod) && (listaInteresados[i][1] == ver))
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

        function validarCamposRejilla(){
            if((document.forms[0].nombre.value!="")&&(document.forms[0].descRol.value!=""))
                return true;
            return false;
        }

        function pulsarBuscarTerc() {
            var argumentos = new Array();
            argumentos =[new Array(),""];
            var source = "<c:url value='/BusquedaTerceros.do?opcion=inicializarBusquedaTerc&ventana=true&preguntaAlta=si'/>";
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,argumentos,
	'width=998,height=700,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                            document.forms[0].tercero.value = datos[2];
                            document.forms[0].versionTercero.value = datos[3];
                            document.forms[0].nombre.value = datos[6];
                            document.forms[0].domicilio.value = datos[14];
                            document.forms[0].descDomicilio.value = datos[18];
                        }
                    });
        }

        function abrirTerceros() {
            var codTercero = document.forms[0].tercero.value;
            if ((codTercero!=null) && (codTercero!='')) {
              var source = "<c:url value='/BusquedaTerceros.do'/>" + '?opcion=inicializar&destino=registroAlta&tipo=B&codTerc='+codTercero;
            }else {
                var source = "<c:url value='/BusquedaTerceros.do?opcion=inicializar&destino=registroAlta&tipo=NoAlta&descDoc=&docu='/>";
            }
            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,'terceros',
	'width=998,height=530,status='+ '<%=statusBar%>',function(datos){
                            // Comprobacion de que no se ha dejado un tercero sin domicilio
                        if (datos!=null && datos[0]!=null && datos[0][0] == "terceroSinDomicilio") 
                               terceroSinDomicilio(datos[0][1],"<%=descriptor.getDescripcion("msjNoGrabDomTercExt")%>",
                                        '<%=request.getContextPath()%>',function (datos2){
                                                if(datos2!=undefined){
                                                     if (datos2[0][0] == "eliminarTercero") {
                                                         limpiarCamposRejilla();
                                                     } else {
                                                         document.forms[0].tercero.value = datos2[2];
                                                         document.forms[0].versionTercero.value = datos2[3];
                                                         document.forms[0].nombre.value = datos2[6];
                                                         document.forms[0].domicilio.value = datos2[14];
                                                         document.forms[0].descDomicilio.value = datos2[18];
                                                     }
                                                 }
                                        });
                        else
                            // Otra comprobacion, si se ha eliminado un tercero, no intentar cargar los datos devueltos                  
                           if(datos!=undefined){
                                if (datos[0][0] == "eliminarTercero") {
                                    limpiarCamposRejilla();
                                } else {
                                    document.forms[0].tercero.value = datos[2];
                                    document.forms[0].versionTercero.value = datos[3];
                                    document.forms[0].nombre.value = datos[6];
                                    document.forms[0].domicilio.value = datos[14];
                                    document.forms[0].descDomicilio.value = datos[18];
                                }
                            }
                    });
        }
       
        function nombreCompleto(nom, pa1, ap1, pa2, ap2){
            var titular = "";
            if(""!=pa1) titular += pa1 + " ";
            else if(""!=ap1) titular += ap1 + " ";
            else if(""!=pa2) titular += pa2 + " ";
            else if(""!=ap2) titular += ap2;
            else if(""!=nom) titular += ", " +nom;
            return titular;
        }

        function pulsarVer() {
          if(tablaInteresados.selectedIndex != -1) {
              var i = tablaInteresados.selectedIndex;
              var codTerc = listaInteresados[i][0];
              var versTerc = listaInteresados[i][1];
              var codDom = listaInteresados[i][4];
              var source = "<c:url value='/BusquedaTerceros.do?opcion=verTercero&nCS='/>"+codTerc+"&codMun="+versTerc+"&codProc="+codDom;
              abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
                        'width=650,height=510,status='+ '<%=statusBar%>',function(datos){
                                if(datos!=undefined){

                                }
                        });
          } else {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
          }
        }
    </script>
</head>

<body class="bandaBody" onload="inicializar();">
    <form name="formulario" method="post">
        <input  type="hidden"  name="opcion" id="opcion">
        <input  type="hidden"  name="codMunicipio" id="codMunicipio">
        <input  type="hidden"  name="codProcedimiento" id="codProcedimiento">
        <input  type="hidden"  name="ejercicio" id="ejercicio">
        <input  type="hidden"  name="numero" id="numero">
        <input  type="hidden"  name="tercero" id="tercero">
        <input  type="hidden"  name="versionTercero" id="versionTercero">
        <input  type="hidden"  name="domicilio" id="domicilio">
        <input type="hidden" name="listaCodTercero" value="">
        <input type="hidden" name="listaVersionTercero" value="">
        <input type="hidden" name="listaCodDomicilio" value="">
        <input type="hidden" name="listaRol" value="">
        <input type="hidden" name="listaMostrar" value="">
        
        <div id="titulo" class="txttitblanco">Lista de Interesados         
        <div class="contenidoPantalla">
            <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" style="padding-bottom: 20px">
                <tr>
                    <td align="center" id="tabla"></td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" cellpadding="1px" cellspacing="0px" border="0px">
                            <tr>
                                <td style="width: 40%" align="center">
                                    <input type="text" class="inputTextoObligatorio" id="nombre" name="nombre" style="width:330px" readonly>
                                    <span class="fa fa-search" aria-hidden="true"  name="botonT" alt="Buscar Interesado" style="cursor:pointer; border: 0px none" onclick="javascript:pulsarBuscarTerc();"></span>
                                    <span class="fa fa-users" aria-hidden="true"  name="botonTer" alt="Mantenimiento de Terceros" style="cursor:pointer; border: 0px none" onclick="javascript:abrirTerceros();"></span>
                                </td>
                                <td style="width: 42%" align="center">
                                    <input type="text" class="inputTextoObligatorio" id="descDomicilio" name="descDomicilio" style="width:395px" readonly>
                                </td>
                                <td align="center">
                                    <input type="text" class="inputTextoObligatorio" id="codRol" name="codRol" style="width:15px" maxlength="2" onKeyPress="javascript:return SoloDigitos(event);">
                                    <input type="text" class="inputTextoObligatorio" id="descRol" name="descRol" style="width:130px" readOnly="true" >
                                    <a href="" id="anchorRol" name="anchorRol">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonRol" name="botonRol" style="cursor:pointer; border: 0px none"></span>
                                    </a>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>                                                
            </table>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbVer")%>' name="cmdVer" onClick="pulsarVer();" accesskey="V">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAlta" onClick="pulsarAlta();" accesskey="A">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdModificar" onClick="pulsarModificar();" accesskey="M">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminar" onClick="pulsarEliminar();" accesskey="E">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> name="cmdLimpiar" onClick="limpiar(vectorCamposRejilla);" accesskey="L">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" accesskey="S">
        </div>         
    </div>         
</form>

<script type="text/javascript">
	var comboRol = new Combo("Rol");
	var tablaInteresados = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
	tablaInteresados.addColumna('390','center','<%=descriptor.getDescripcion("gEtiqNombre")%>');
	tablaInteresados.addColumna('390','center','<%=descriptor.getDescripcion("tit_Domicilios")%>');
	tablaInteresados.addColumna('170','center','<%=descriptor.getDescripcion("etiqRol")%>');
	tablaInteresados.displayCabecera=true;

	function rellenarDatos(tableName,rowID){
		var i = rowID;
		if((i>=0)&&!tableName.ultimoTable){
			if(expediente[5] != "soloConsulta") {
			  datosRejilla = [listaInteresados[i][2],listaInteresados[i][7],listaTabla[i][2],expediente[0],expediente[1],expediente[2],expediente[3],listaInteresados[i][0],listaInteresados[i][1],listaInteresados[i][4],listaTabla[i][1]];
			  rellenar(datosRejilla,vectorCamposRejilla);
			}
		}else limpiar(vectorCamposRejilla);
	}

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
	function checkKeysLocal(evento,tecla){
        var teclaAuxiliar = "";
        if(window.event){
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else
            teclaAuxiliar = evento.which;

		keyDel(evento);
		if (teclaAuxiliar == 38 || teclaAuxiliar == 40) upDownTable(tablaInteresados,listaInteresados,teclaAuxiliar);
                if (teclaAuxiliar == 1){
                   if (comboRol.base.style.visibility == "visible" && isClickOutCombo(comboRol,coordx,coordy)) setTimeout('comboRol.ocultar()',20);
                }
                if (teclaAuxiliar == 9){
                   comboRol.ocultar();
                }
	}
</script></body></html>
