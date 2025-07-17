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
        <title>Mantenimiento de Tipos de Entidades</title>
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 3;
            String css="";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css=usuarioVO.getCss();
            }%>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
    <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>

        <script type="text/javascript">
            var lista = new Array();
            var codOrganizaciones = new Array();
            var descOrganizaciones = new Array();
            var datosEntidades = new Array();
            var vectorCamposRejilla = ['codEntidad','descEntidad','directorio','tipoEntidad'];
            var vectorBotones = new Array();
            var vectorCamposBusqueda1 = new Array();
            var vectorCamposRejilla1 = new Array();
            var vectorAnchorsBusqueda = new Array();
            var vectorBotonesBusqueda = new Array();
            
            // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
            function inicializar(){
                window.focus();
                recuperaDatosIniciales();
                vectorBotones = [document.getElementsByName('botonAlta')[0],document.getElementsByName('botonModificar')[0],
                    document.getElementsByName('botonBorrar')[0],document.getElementsByName('botonLimpiar')[0]];
                vectorCamposBusqueda1 = [document.getElementsByName('codOrganizacion')[0],document.getElementsByName('descOrganizacion')[0]];
                vectorCamposRejilla1 = [document.getElementsByName('codEntidad')[0],document.getElementsByName('descEntidad')[0],
                    document.getElementsByName('directorio')[0],document.getElementsByName('tipoEntidad')[0]];
                vectorAnchorsBusqueda = [document.getElementsByName('anchorOrganizacion')[0]];
                vectorBotonesBusqueda = [document.getElementsByName('botonOrganizacion')[0]];
                pulsarCancelarBuscar();
            }
            
            function recuperaDatosIniciales(){
                 <%
            MantenimientosAdminForm bForm =
                    (MantenimientosAdminForm) session.getAttribute("MantenimientosAdminForm");
            Vector listaOrganizaciones = bForm.getListaOrganizaciones();
            int lengthOrganizaciones = listaOrganizaciones.size();
            int i = 0;
    %>
        var j=0;
    <%for (i = 0; i < lengthOrganizaciones; i++) {
                GeneralValueObject organizaciones = (GeneralValueObject) listaOrganizaciones.get(i);%>
                    codOrganizaciones[j] = ['<%=(String) organizaciones.getAtributo("codigo")%>'];
                        descOrganizaciones[j] = ['<%=(String) organizaciones.getAtributo("descripcion")%>'];
                            j++;
                            <%}%>
            }

function cargarTablaEntidades(listaE){
    lista = listaE;
    tablaEntidades.setLineas(lista);
    refresca(tablaEntidades);
}

// FUNCIONES DE LIMPIEZA DE CAMPOS
function limpiarFormulario(){
    tablaEntidades.setLineas(new Array());
    refresca(tablaEntidades);
    limpiarCamposRejilla();
}

function limpiarCamposRejilla(){
    limpiar(vectorCamposRejilla);
    var vector = [document.forms[0].codEntidad];
    habilitarGeneral(vector);
}

// FUNCIONES DE PULSACION DE BOTONES
function pulsarBuscar(){
    var botonBuscar = [document.forms[0].botonBuscar];
    if(validarCamposBusqueda()){
        document.forms[0].opcion.value="cargarEntidades";
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/administracion/mantenimiento/Entidades.do";
        document.forms[0].submit();
        deshabilitarGeneral(botonBuscar);
        deshabilitarGeneral(vectorCamposBusqueda1);
        habilitarGeneral(vectorBotones);
        habilitarGeneral(vectorCamposRejilla1);
        deshabilitarImagenBotonGeneral(vectorBotonesBusqueda,true);
    }else
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>')
    }

    function pulsarSalir(){
        document.forms[0].target = "mainFrame";
        document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
        document.forms[0].submit();
    }

    function pulsarEliminar() {
        var vector = [document.forms[0].codEntidad];
        habilitarGeneral(vector);
        if(filaSeleccionada(tablaEntidades)) {
            habilitarGeneral(vectorCamposBusqueda1);
            document.forms[0].opcion.value = 'eliminar';
            document.forms[0].target = "oculto";
            document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Entidades.do';
            document.forms[0].submit();
            deshabilitarGeneral(vectorCamposBusqueda1);
            limpiarCamposRejilla();
        }else 
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
        }

        function pulsarModificar(){
            var vector = [document.forms[0].codEntidad];
            habilitarGeneral(vector);
            var cod = document.forms[0].codEntidad.value;
            var yaExiste = 0;
            if(filaSeleccionada(tablaEntidades)){
                if(validarCamposRejilla()){			
                    for(l=0; l < lista.length; l++){
                        var posicionSeleccionada = tablaEntidades.selectedIndex;	  
                        if(l == posicionSeleccionada) {
                            l= l;
                        } else {
                        if ((lista[l][0]) == cod ){
                            yaExiste = 1;
                        }
                    }
                }
                if(yaExiste == 0) {
                    habilitarGeneral(vectorCamposBusqueda1);
                    document.forms[0].opcion.value = 'modificar';
                    document.forms[0].target = "oculto";
                    document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Entidades.do';
                    document.forms[0].submit();
                    deshabilitarGeneral(vectorCamposBusqueda1);
                    limpiarCamposRejilla();
                } else {
                jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                }
            }else
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
            }
            else
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                }

                function pulsarAlta(){
                    if (validarCamposRejilla()){
                        if(noEsta()){
                            habilitarGeneral(vectorCamposBusqueda1);
                            document.forms[0].opcion.value = 'alta';
                            document.forms[0].target = "oculto";
                            document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Entidades.do';
                            document.forms[0].submit();
                            deshabilitarGeneral(vectorCamposBusqueda1);
                            limpiarCamposRejilla();
                        }
                    }else
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
                    }

                    function pulsarCancelarBuscar(){
                        limpiarFormulario();
                        var botonBuscar = [document.forms[0].botonBuscar];
                        habilitarGeneral(botonBuscar);
                        habilitarGeneral(vectorCamposBusqueda1);
                        deshabilitarImagenBotonGeneral(vectorBotonesBusqueda,false);
                        deshabilitarGeneral(vectorCamposRejilla1);
                        deshabilitarGeneral(vectorBotones);
                    }

                    function pulsarLimpiar(){
                        limpiarCamposRejilla();
                        tablaEntidades.selectLinea(-1);
                    }

                    // FUNCIONES DE VALIDACION Y COMPROBACION DEL FORMULARIO
                    function noEsta(indice){
                        var cod = document.forms[0].codEntidad.value;
                        for(i=0;(i<lista.length);i++){
                            if(i!=indice){
                                if((lista[i][0]) == cod) {
                                    jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                                        return false;
                                    }
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

                        function validarCamposBusqueda(){
                            var codOrg = document.forms[0].codOrganizacion.value;
                            if(codOrg!="")
                                return true;
                            return false;
                        }

                        function validarCamposRejilla(){
                            var codEnt = document.forms[0].codEntidad.value;
                            var descEnt = document.forms[0].descEntidad.value;
                            var tipoEnt = document.forms[0].tipoEntidad.value;
                            if((codEnt!="")&&(descEnt!="")&&(tipoEnt!=""))
                                return true;
                            return false;
                        }

                        function recuperaDatos(lista2) {
                            limpiarCamposRejilla();
                            lista = lista2;
                            tablaEntidades.setLineas(lista);      
                            refresca(tablaEntidades);
                        }

                        function noPuedeEliminar() {
                            jsp_alerta('A','<%=descriptor.getDescripcion("msjNoElimEnt")%>');
                                limpiarCamposRejilla();
                                if(tablaEntidades.selectedIndex != -1 ) {
                                    tablaEntidades.selectLinea(tablaEntidades.selectedIndex);
                                    tablaEntidades.selectedIndex = -1;
                                }
                            }
                                                    
        </script>
    </head>
    
<body class="bandaBody" onload="javascript:{pleaseWait('off');inicializar();}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<form name="formulario" method="post">
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="identificador" id="identificador">
    <input  type="hidden"  name="codEntidadAntiguo" value="">

    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantEntidades")%></div>
    <div class="contenidoPantalla">
        <table>
            <tr>
                <td style="width: 60%" class="etiqueta">
                    <%=descriptor.getDescripcion("gEtiq_Organizacion")%></span>
                    <input class="inputTextoObligatorio" type="text" name="codOrganizacion" size="3"
                           onfocus="this.select();" 
                           onchange="javascript:{actualizarValDiv('codOrganizacion','descOrganizacion');
                               divSegundoPlano=true;inicializarValores('codOrganizacion','descOrganizacion',
                                   codOrganizaciones,descOrganizaciones);}">
                    <input class="inputTextoObligatorio" type="text" name="descOrganizacion"    
                           style="width:300" readonly
                           onfocus="javascript:{divSegundoPlano=true;inicializarValores('codOrganizacion',
                           'descOrganizacion',codOrganizaciones,descOrganizaciones);}"
                           onclick="javascript:{divSegundoPlano=false;inicializarValores('codOrganizacion',
                           'descOrganizacion',codOrganizaciones,descOrganizaciones);}">
                    <a name="anchorOrganizacion" 
                       href = 
                       "javascript:{divSegundoPlano=false;inicializarValores('codOrganizacion',
                           'descOrganizacion',codOrganizaciones,descOrganizaciones);}"
                       style="text-decoration:none;" onfocus="javascript:this.focus();">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonOrganizacion"
                             style="cursor:hand;"></span>
                    </a>
                </td>
                <td style="width: 40%;text-align:right">
                    <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" 
                           value="<%=descriptor.getDescripcion("gbBuscar")%>"
                           onClick="pulsarBuscar();" accesskey="B">
                    <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                           value='<%=descriptor.getDescripcion("gbCancelar")%>'
                           onClick="pulsarCancelarBuscar();" accesskey="C">
                </td>
            </tr>
            <tr> 
                <td colspan="2" id="tabla"></td>
            </tr>
            <tr> 
                <td colspan="2"> 
                    <input type="text" class="inputTextoObligatorio"
                           name="codEntidad" maxlength="3" style="width:11%" 
                           onkeyup="return SoloDigitosNumericos(this);">
                    <input name="descEntidad" type="text" class="inputTextoObligatorio" 
                           maxlength="60" style="width:46.5%" 
                           onblur="return xAMayusculas(this);">
                    <input name="directorio" type="text" class="inputTexto" maxlength="255"
                           style="width:32.5%"> 
                    <input name="tipoEntidad" type="text" class="inputTextoObligatorio"  
                           maxlength="1" style="width:8%" 
                           onkeyup="return xAMayusculas(this);">
                </td>
            </tr>
        </TABLE>								
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> 
                        name="botonAlta" id="botonAlta" onClick="pulsarAlta();" accesskey="A">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> 
                        name="botonModificar" id="botonModificar" onClick="pulsarModificar();" accesskey="M">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> 
                        name="botonBorrar" id="botonBorrar" onClick="pulsarEliminar();" accesskey="E">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> 
                        name="botonLimpiar" id="botonLimpiar" onClick="pulsarLimpiar();" accesskey="L">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> 
                                       name="botonSalir" id="botonSalir" onClick="pulsarSalir();" accesskey="S">
        </div>            				            
    
    </div>            				            
</form>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaCombo.js"></script>
<script type="text/javascript">
var tablaEntidades = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
tablaEntidades.addColumna('100','center','<%= descriptor.getDescripcion("gEtiq_Codigo")%>','Number');
tablaEntidades.addColumna('430',null,'<%= descriptor.getDescripcion("gEtiq_Descrip")%>','String');
tablaEntidades.addColumna('300',null,'<%= descriptor.getDescripcion("gEtiq_DirTrabajo")%>','String');
tablaEntidades.addColumna('80','center','<%= descriptor.getDescripcion("gEtiq_sit")%>','String');
tablaEntidades.displayCabecera = true;

function refresca(tabla){
    tabla.displayTabla();
}
                            
    function rellenarDatos(tableName,rowID){
        if(tableName==tablaEntidades){
            limpiarCamposRejilla();
            var vector = [document.forms[0].codEntidad];
            deshabilitarGeneral(vector);
            if((tableName.selectedIndex>=0)&&!tableName.ultimoTable){
                var p =tableName.selectedIndex;	  
                rellenar(lista[p],vectorCamposRejilla);
                document.forms[0].codEntidadAntiguo.value = lista[p][0];
            } else {
            habilitarGeneral(vector);
        }
    }
} 

document.onmouseup = checkKeys; 

function checkKeysLocal(evento, tecla){
    var teclaAuxiliar="";
    if(window.event){
          evento = window.event;
          teclaAuxiliar =evento.keyCode;
    }else
          teclaAuxiliar =evento.which;

      keyDel(evento);

     if((layerVisible)||(divSegundoPlano)) buscar(evento);

      if(teclaAuxiliar == 9){
    if(layerVisible) ocultarDiv();
    if(divSegundoPlano) divSegundoPlano = false;
    }
    if(teclaAuxiliar == 1){
    if(layerVisible) setTimeout('ocultarDiv()',50);
    if(capaVisible) ocultarLista();
    if(divSegundoPlano) divSegundoPlano = false; 
    }
      if(teclaAuxiliar == 40){
    if((layerVisible)||(divSegundoPlano)) upDown();
          upDownTable(tablaEntidades,lista,teclaAuxiliar);
    }
      if(teclaAuxiliar == 38){
    if((layerVisible)||(divSegundoPlano)) upDown();
          upDownTable(tablaEntidades,lista,teclaAuxiliar);
    }  
      if (evento.button == 1){
    if(layerVisible) setTimeout('ocultarDiv()',50);
    if(capaVisible) ocultarLista();
    if(divSegundoPlano) divSegundoPlano = false;      
    }	
    if (teclaAuxiliar == 13) pushEnterTable(tablaEntidades,lista);
}
</script>
</body>
</html>
