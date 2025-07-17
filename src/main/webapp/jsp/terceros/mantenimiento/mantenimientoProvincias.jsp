<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Mantenimiento de Provincias</title>
        <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
        
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 3;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }
        %>
        
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
        <!-- Ficheros JavaScript -->
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">

        <script>
            // VARIABLES GLOBALES
            var lista = new Array();
            var listaOriginal = new Array();
            var codPaises = new Array();
            var descPaises = new Array();
            var codAutonomias = new Array();
            var descAutonomias = new Array();
            
            var vectorCamposBusqueda = ["codPais","descPais"];
            var vectorCamposRejilla = ["txtCodigo","codAutonomia","descAutonomia","txtDescripcion","txtNombreLargo"];
            
            // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
            function inicializar(){
                recuperaDatosIniciales();
                deshabilitarFormulario1();
                limpiarFormulario();
            }
            
            function recuperaDatosIniciales(){
                 <%
            MantenimientosTercerosForm bForm =
                    (MantenimientosTercerosForm) session.getAttribute("MantenimientosTercerosForm");
            Vector listaPaises = bForm.getListaPaises();
            int lengthPaises = listaPaises.size();
            int i = 0;
            String codPaises = "";
            String descPaises = "";
            for (i = 0; i < lengthPaises - 1; i++) {
                GeneralValueObject paises = (GeneralValueObject) listaPaises.get(i);
                codPaises += "\"" + (String) paises.getAtributo("codigo") + "\",";
                descPaises += "\"" + (String) paises.getAtributo("descripcion") + "\",";
            }
            GeneralValueObject paises = (GeneralValueObject) listaPaises.get(i);
            codPaises += "\"" + (String) paises.getAtributo("codigo") + "\"";
            descPaises += "\"" + (String) paises.getAtributo("descripcion") + "\"";
     %>
         codPaises = [<%=codPaises%>];
             descPaises = [<%=descPaises%>];
             }
             
             function cargaTablaProvincias(){
                 limpiarFormulario();
                 document.forms[0].opcion.value = "cargarProvincias";
                 document.forms[0].target = "oculto";
                 document.forms[0].action = "<%=request.getContextPath()%>/terceros/mantenimiento/Provincias.do";
                 document.forms[0].submit();
             }
             
             // FUNCIONES DE LIMPIEZA DE CAMPOS
             function limpiarFormulario(){
                 tablaProvincias.lineas = new Array();
                 refresca(tablaProvincias);
                 limpiarInputs();
             }
             
             function limpiarInputs(){
                 document.forms[0].txtCodigo.value = "";
                 document.forms[0].codAutonomia.value = "";
                 document.forms[0].descAutonomia.value = "";
                 document.forms[0].txtDescripcion.value = "";
                 document.forms[0].txtNombreLargo.value = "";
                 var vector = [document.forms[0].txtCodigo];
                 habilitarGeneral(vector);
             }
             
             function pulsarLimpiar() {
                 limpiarInputs();
                 if(tablaProvincias.selectedIndex != -1 ) {
                     tablaProvincias.selectLinea(tablaProvincias.selectedIndex);
                     tablaProvincias.selectedIndex = -1;
                 }
             }
             
             // FUNCIONES DE PULSACION DE BOTONES
             function pulsarSalir(){
                 
                 document.forms[0].target = "mainFrame";
                 document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                 document.forms[0].submit();
             }
             
             function pulsarEliminar() {
                 if(tablaProvincias.selectedIndex != -1) {
                     if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimProv")%>') ==1) {
                         var vector = [document.forms[0].txtCodigo];
                         habilitarGeneral(vector);
                         document.forms[0].codPais.disabled = false;
                         document.forms[0].opcion.value = "eliminar";
                         document.forms[0].target = "oculto";
                         document.forms[0].action = "<%=request.getContextPath()%>/terceros/mantenimiento/Provincias.do";
                         document.forms[0].submit();
                         document.forms[0].codPais.disabled = true;
                         deshabilitarGeneral(vector);
                     }
                 }
                 else jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                 }
                 
                 function noEliminarProv() {
                     jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoElimProv")%>");
                     }
                     
                     function pulsarModificar(){
                         var existe = 0;
                         var cod = document.forms[0].txtCodigo.value;
                         if(tablaProvincias.selectedIndex != -1){
                             if(validarFormulario()){			
                                 for(var i=0; (i < lista.length) && (existe == 0); i++){
                                     if((lista[i][0]) == cod && cod != lista[tablaProvincias.selectedIndex][0] )
                                         existe = 1;
                                 }
                                 if(existe == 0) {
                                     var vector = [document.forms[0].txtCodigo];
                                     habilitarGeneral(vector);
                                     document.forms[0].codPais.disabled = false;
                                     document.forms[0].opcion.value = "modificar";
                                     document.forms[0].target = "oculto";
                                     document.forms[0].action = "<%=request.getContextPath()%>/terceros/mantenimiento/Provincias.do";
                                     document.forms[0].submit();
                                     document.forms[0].codPais.disabled = true;
                                     deshabilitarGeneral(vector);
                                 } else {
                                 jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
                                 }	
                             }
                         } else {
                         jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                         }
                     }
                     
                     function pulsarAlta(){
                         document.forms[0].codPais.disabled = false;
                         var cod = document.forms[0].txtCodigo.value;
                         var existe = 0;
                         if (validarFormulario()){
                             for(var i=0; (i < lista.length) && (existe == 0); i++){
                                 if((lista[i][0]) == cod)
                                     existe = 1;
                             }
                             if(existe == 0){
                                 document.forms[0].opcion.value = "alta";
                                 document.forms[0].target = "oculto";
                                 document.forms[0].action = "<%=request.getContextPath()%>/terceros/mantenimiento/Provincias.do";
                                 document.forms[0].submit();
                             } else {
                             jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
                             }
                         }
                         document.forms[0].codPais.disabled = true;
                     }
                     
                     function recuperaDatos(datos) {
                         limpiarInputs();
                         lista = datos[0];
                         listaOriginal = datos[1];
                         codAutonomias = datos[2];
                         descAutonomias = datos[3];
                         tablaProvincias.lineas=lista;
                         refresca(tablaProvincias);
                     }
                     
                     function pulsarBuscar() {
                         if(document.forms[0].codPais.value != ""){
                             cargaTablaProvincias();
                             deshabilitarFormulario();
                             habilitarFormulario1();
                         } else {
                         jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                         }
                     }
                     
                     function pulsarCancelarBuscar() {
                         limpiarFormulario();
                         habilitarFormulario();
                         deshabilitarFormulario1();
                     }
                     
                     function habilitarFormulario() {
                         document.forms[0].botonBuscar.disabled = false;
                         document.forms[0].botonBuscar.classname = "botonGeneral";
                         var vector1 = [document.forms[0].codPais,document.forms[0].descPais];
                         habilitarGeneral(vector1);
                         var vectorAnchorsBusqueda = [document.all.anchorPais];
                         var vectorBotonesBusqueda = document.getElementsByName("botonPais");
                         deshabilitarImagenBotonGeneral(vectorBotonesBusqueda,false);
                     }
                     
                     function deshabilitarFormulario() {
                         document.forms[0].botonBuscar.disabled = true;
                         document.forms[0].botonBuscar.classname = "botonGeneralDeshabilitado";
                         var vector1 = [document.forms[0].codPais,document.forms[0].descPais];
                         deshabilitarGeneral(vector1);
                         var vectorAnchorsBusqueda = [document.all.anchorPais];
                         var vectorBotonesBusqueda = document.getElementsByName("botonPais");
                         deshabilitarImagenBotonGeneral(vectorBotonesBusqueda,true);
                     }
                     
                     function deshabilitarFormulario1() {
                         var vector = [document.forms[0].txtCodigo,document.forms[0].codAutonomia,document.forms[0].descAutonomia,
                             document.forms[0].txtDescripcion,document.forms[0].txtNombreLargo];
                         deshabilitarGeneral(vector);

                         document.forms[0].cmdAlta.disabled = true;
                         document.forms[0].cmdModificar.disabled = true;
                         document.forms[0].cmdLimpiar.disabled = true;
                         document.forms[0].cmdEliminar.disabled = true;
                         
                         document.forms[0].cmdAlta.className = "botonGeneralDeshabilitado";
                         document.forms[0].cmdModificar.className = "botonGeneralDeshabilitado";
                         document.forms[0].cmdLimpiar.className = "botonGeneralDeshabilitado";
                         document.forms[0].cmdEliminar.className = "botonGeneralDeshabilitado";


                         //document.forms[0].cmdSalir.disabled = true;
                     }
                     
                     function habilitarFormulario1() {
                         var vector = [document.forms[0].txtCodigo,document.forms[0].codAutonomia,document.forms[0].descAutonomia,
                             document.forms[0].txtDescripcion,document.forms[0].txtNombreLargo];
                         habilitarGeneral(vector);
                         document.forms[0].cmdAlta.disabled = false;
                         document.forms[0].cmdModificar.disabled = false;
                         document.forms[0].cmdLimpiar.disabled = false;
                         document.forms[0].cmdEliminar.disabled = false;

                         document.forms[0].cmdAlta.className = "botonGeneral";
                         document.forms[0].cmdModificar.className = "botonGeneral";
                         document.forms[0].cmdLimpiar.className = "botonGeneral";
                         document.forms[0].cmdEliminar.className = "botonGeneral";

                         //document.forms[0].cmdSalir.disabled = false;
                     }
        </script>
    </head>
    <body onload="javascript:{pleaseWait('off');
        inicializar();}">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
    <form name="formulario" method="post">
        <input  type="hidden"  name="opcion" id="opcion">
        <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantProvincias")%></div>
        <div class="contenidoPantalla">
            <table >
                <tr>
                    <td class="etiqueta" style="width: 60%">
                        <span class="etiqueta" style="width: 20%"><%= descriptor.getDescripcion("gEtiq_Pais")%></span>
                         <input class="inputTextoObligatorio" type="text" name="codPais" size="3"
                                onfocus="javascript:this.select();"  style="margin-left:10px"
                                onchange="javascript:{actualizarValDiv('codPais','descPais');divSegundoPlano=true;inicializarValores('codPais','descPais',codPaises,descPaises);}">
                         <input class="inputTextoObligatorio" type="text" name="descPais"  style="width:120;height:17" readonly
                                onfocus="javascript:{divSegundoPlano=true;inicializarValores('codPais', 'descPais',codPaises,descPaises);}" 
                                onclick="javascript:{divSegundoPlano=false;inicializarValores('codPais', 'descPais',codPaises,descPaises);}" >
                         <a name = "anchorPais" href="javascript:{divSegundoPlano=false;inicializarValores('codPais', 'descPais',codPaises,descPaises);}" style="text-decoration:none;" 
                            onfocus="javascript:this.focus();"> 
                            <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonPais" style="cursor:hand;"></span>
                        </a>
                    </td>
                    <td style="width: 40%;text-align:right">
                        <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" 
                               value="<%=descriptor.getDescripcion("gbBuscar")%>"
                               onClick="pulsarBuscar();" accesskey="B">
                        <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                               value="<%=descriptor.getDescripcion("gbCancelar")%>"
                               onClick="pulsarCancelarBuscar();" accesskey="C">
                </td>
            </tr>
            <tr> 
                <td colspan="2" id="tablaProvincias"></td>
            </tr>
            <tr> 
                <td colspan="2">
                    <input type="text" class="inputTextoObligatorio" id="obligatorio"  
                           name="txtCodigo" maxlength="2" style="width:11%" 
                           onkeyup="return SoloDigitosNumericos(this);">
                    <input class="inputTexto" type="text" name="codAutonomia" size="3"
                           style="width:4%" onfocus="javascript:this.select();" 
                           onchange="javascript:{actualizarValDiv('codAutonomia','descAutonomia');divSegundoPlano=true;inicializarValores('codAutonomia','descAutonomia',codAutonomias,descAutonomias);}">
                    <input class="inputTexto" type="text" name="descAutonomia"  style="width:36%" readonly
                           onfocus="javascript:{divSegundoPlano=true;inicializarValores('codAutonomia', 'descAutonomia',codAutonomias,descAutonomias);}" 
                           onclick="javascript:{divSegundoPlano=false;inicializarValores('codAutonomia', 'descAutonomia',codAutonomias,descAutonomias);}">
                    <a href="javascript:{divSegundoPlano=false;inicializarValores('codAutonomia', 'descAutonomia',codAutonomias,descAutonomias);}" style="text-decoration:none;" 
                       onfocus="javascript:this.focus();"> 
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonAutonomia" style="cursor:hand;"></span>
                    </a>
                    <input name="txtDescripcion" type="text" class="inputTextoObligatorio" id="obligatorio" 
                           maxlength="25" style="width:21.5%" onblur="return xAMayusculas(this);">

                    <input type="text" class="inputTexto"  name="txtNombreLargo" maxlength="50" 
                           style="width:21.5%" onblur="return xAMayusculas(this);">
            </td>
        </tr>
    </TABLE>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> 
              name="cmdAlta" onClick="pulsarAlta();" accesskey="A"> 
               <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> 
              name="cmdModificar" onClick="pulsarModificar();" accesskey="M"> 
           <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> 
              name="cmdEliminar" onClick="pulsarEliminar();" accesskey="E">
           <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> 
                  name="cmdLimpiar" onClick="pulsarLimpiar();" accesskey="L"> 
           <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> 
                  name="cmdSalir" onClick="pulsarSalir();" accesskey="S">  
    </div>    
</div>    
</form>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaCombo.js"></script>
<script type="text/javascript">
    var tablaProvincias = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaProvincias"));
    tablaProvincias.addColumna("100","center",'<%= descriptor.getDescripcion("gEtiq_Codigo")%>');
    tablaProvincias.addColumna("400",'left','<%= descriptor.getDescripcion("gEtiq_Autonomia")%>');
    tablaProvincias.addColumna("200",'left','<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
    tablaProvincias.addColumna("200",'left','<%= descriptor.getDescripcion("gEtiq_NomLargo")%>');
    tablaProvincias.displayCabecera=true;
                            
function refresca(tabla){
    tabla.displayTabla();
}

function pintaDatosProvincias(datos){
    // Carga los inputs con los valores de la fila seleccionada
    var i = tablaProvincias.selectedIndex;
    limpiarInputs();
    if((i>=0)&&!tablaProvincias.ultimoTable){
        var vector = [document.forms[0].txtCodigo];
        deshabilitarGeneral(vector);
        document.forms[0].txtCodigo.value = listaOriginal[i][0];
        document.forms[0].codAutonomia.value = listaOriginal[i][1];
        document.forms[0].descAutonomia.value = listaOriginal[i][2];
        document.forms[0].txtDescripcion.value = listaOriginal[i][3];
        document.forms[0].txtNombreLargo.value = listaOriginal[i][4];

    }
}

tablaProvincias.displayDatos = pintaDatosProvincias;

function rellenarDatos(tableName,listName){
    if(!((layerVisible)||(divSegundoPlano))) 
        pintaDatosProvincias(listName);
} 

document.onmouseup = checkKeys; 

function checkKeysLocal(evento,tecla){
    var teclaAuxiliar = "";
    if(window.event){
        evento          = window.event;
        teclaAuxiliar  =  evento.keyCode;
    }else
        teclaAuxiliar  =  evento.which;

    if((layerVisible)||(divSegundoPlano)) buscar(tecla);
    keyDel(evento);

    if (teclaAuxiliar == 9){
        if(layerVisible) ocultarDiv();
        if(divSegundoPlano) divSegundoPlano = false;
        return false;
    }
    if (teclaAuxiliar == 1){
        if(layerVisible) setTimeout("ocultarDiv()",50);
        if(capaVisible) ocultarLista();

        if(divSegundoPlano) divSegundoPlano = false;
    }
    if (evento.button == 1){
        if(layerVisible) setTimeout("ocultarDiv()",50);
        if(capaVisible) ocultarLista();

        if(divSegundoPlano) divSegundoPlano = false;      
    }	
    if((teclaAuxiliar == 38)||(teclaAuxiliar == 40)){

        if((layerVisible)||(divSegundoPlano)) 
            upDown(teclaAuxiliar);
        else
            upDownTable(tablaProvincias,lista,teclaAuxiliar);

    }

    if (teclaAuxiliar == 13) pushEnterTable(tablaProvincias,lista);

    if((evento.button == 38)||(evento.button == 40)){

        if((layerVisible)||(divSegundoPlano))
            upDown(teclaAuxiliar);
        else
            upDownTable(tablaProvincias,lista,teclaAuxiliar);

    }

    if (evento.button == 13) pushEnterTable(tablaProvincias,lista);

}
////////////////////////////////////////////////////////////////////////////////////////////////////////

function onBlurPais(cod, des){	
    if(camposDistintosDiv(cod, des)){
        cargaTablaProvincias();
    }
}
</script>
</body>
</html>
