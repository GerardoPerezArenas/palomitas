<!-- JSP de mantenimiento de roles -->

<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
    <head>
        <title>  Mantenimiento Transporte</title>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        
        
        <%
            int idioma = 1;
            String css = "";
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    css = usuario.getCss();
                }

            }
        %>
        
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="1" />
        
        
        <!-- ***********************		FICHERO JAVASCRIPT 	**************************    -->
        
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
        <script type="text/javascript">
            var lista_ide = new Array();
            var lista= new Array();
            var i;
            var res = 0;
            var pde = false;
            var ultimo = false;
            var existe = 0;
            var existeCerrado = 0;
            
            function borrarDatos(){
                
                document.forms[0].codigo.value = '';
                document.forms[0].txtNomeDescripcion.value = '';
                document.forms[0].porDefecto.checked=false;
                document.forms[0].activo.checked=false;
                document.forms[0].consultaWeb.checked=false;
                
            }
            
            function Inicio() {
                window.focus();
                cargaTabla();
            }
            
            function pulsarEliminar() {
                if(tab.selectedIndex != -1) {
                    if (validarFormulario()) {
                        if(lista[tab.selectedIndex][2]=='NO'){                            
                            var id = lista_ide[tab.selectedIndex];                            
                                document.forms[0].identificador.value = id;
                                document.forms[0].opcion.value = 'eliminar';
                                document.forms[0].target = "oculto";
                                document.forms[0].action = '<%=request.getContextPath()%>/MantRoles.do';
                                document.forms[0].submit();                          
                        }else{jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoElimPde")%>');}
                    }
                } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
                
                function pulsarModificar() {
                    
                    var modificar=0;
                    existeCerrado=0;
                    var cod = document.forms[0].codigo.value;
                    var yaExiste = 0;
                    
                    if(tab.selectedIndex != -1) {
                        if (validarFormulario()) {
                            for(z=0; z < lista.length; z++){
                                if ((lista[z][2] == 'SI' )&&(document.forms[0].porDefecto.checked==true)){
                                    modificar = 1;
                                    ide=lista_ide[z][0];
                                }
                            }
                            if(lista[tab.selectedIndex][2]=='NO'){
                                if(document.forms[0].activo.checked==true) {
                                    if(existe == 0) {
                                        var id = lista_ide[tab.selectedIndex];

                                        if ((modificar==1)){
                                            document.forms[0].idModificar.value=ide;
                                        }else{
                                            document.forms[0].idModificar.value=-2;
                                        }
                                        if(document.forms[0].porDefecto.checked==true){
                                            document.forms[0].pde.value="SI"
                                        }else{
                                            document.forms[0].pde.value="NO"
                                        }
                                        if(document.forms[0].activo.checked==true){
                                            document.forms[0].act.value="SI"
                                        }else{
                                            document.forms[0].act.value="NO"
                                        }                                        
                                        if(document.forms[0].consultaWeb.checked==true){
                                            document.forms[0].cw.value="SI"
                                        }else{
                                            document.forms[0].cw.value="NO"
                                        }                                                                                
                                        document.forms[0].identificador.value = id;
                                        document.forms[0].opcion.value = 'modificar';
                                        document.forms[0].target = "oculto";
                                        document.forms[0].action = '<%=request.getContextPath()%>/MantRoles.do';
                                        document.forms[0].submit();
                                    } else {
                                        jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                                    }
                                }else {                                    
                                        jsp_alerta('A','<%=descriptor.getDescripcion("msjNoModNoAct")%>');                                        
                                }
                            }else{
                                jsp_alerta('A','<%=descriptor.getDescripcion("msjNoModPde")%>');
                            }
                        }
                    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                }
            
            function pulsarAlta() {
                var modificar=0;
                var cod = document.forms[0].codigo.value;
                var yaExiste = 0;
                if (validarFormulario()) {
                    for(l=0; l < lista.length; l++){
                        if ((lista[l][0]) == cod ){
                            yaExiste = 1;
                        }
                        
                        if ((lista[l][2] == 'SI')&& (document.forms[0].porDefecto.checked==true)){
                            modificar = 1;
                            ide=lista_ide[l][0];
                        }
                    }
                    if(yaExiste == 0) {
                        if (modificar==1){
                            document.forms[0].idModificar.value=ide;
                        }else{
                        document.forms[0].idModificar.value=-2;
                    }
                    if(document.forms[0].porDefecto.checked==true){
                        document.forms[0].pde.value="SI"
                    }else{
                    document.forms[0].pde.value="NO"
                }
                //si es el primero en insertar le ponemos el valor por defecto
                if(lista.length==0){
                    document.forms[0].pde.value="SI"
                }
                if(document.forms[0].consultaWeb.checked==true){
                    document.forms[0].cw.value="SI"
                }else{
                    document.forms[0].cw.value="NO"                
                }
                
                if(document.forms[0].activo.checked==true){
                    document.forms[0].act.value="SI"
                }else{
                    document.forms[0].act.value="NO"                
                }
                
                document.forms[0].opcion.value = 'alta';
                document.forms[0].target = "oculto";
                document.forms[0].action = '<%=request.getContextPath()%>/MantRoles.do';
                document.forms[0].submit();
            } else {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
            }
        }
    }
    
    function pulsarSalir(){
        window.location = "<%=request.getContextPath()%>/MantRoles.do?opcion=salir";
    }
    
    function limpiarInputs() {
        tab.selectLinea(tab.selectedIndex);
        borrarDatos();
    }
    
    function recuperaDatos(lista1,lista2,lista4,lista5) {
        lista_ide = new Array();
        lista = new Array();
        
        lista_ide = lista1;
        lista = lista2;
        
        
        limpiarInputs();
        
        tab.lineas=lista;
        refresh();
    }
    
    /////////////// Búsqueda rápida.
    
    function rellenarDatos(tableObject, rowID){
        if(rowID>-1 && !tableObject.ultimoTable){
            document.forms[0].codigo.value = lista[rowID][0];
            document.forms[0].txtNomeDescripcion.value = lista[rowID][1];
            if(lista[rowID][2]=='SI'){
                document.forms[0].porDefecto.checked=true;
            }else{document.forms[0].porDefecto.checked=false;}
            if(lista[rowID][3]=='SI'){
                document.forms[0].activo.checked=true;
                document.forms[0].activo.disabled=true;
            }else{
                document.forms[0].activo.checked=false;
                document.forms[0].activo.disabled=false;
            }            
            if(lista[rowID][4]=='SI'){
                document.forms[0].consultaWeb.checked=true;
            }else{document.forms[0].consultaWeb.checked=false;}
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
    
    /*
    function buscar(){
        var auxCod = new String("");
        var auxDes = new String("");
        if((event.keyCode != 40)&&(event.keyCode != 38)){
            if(event.keyCode != 13){
                auxCod = document.forms[0].codigo.value;
                auxDes = document.forms[0].txtNomeDescripcion.value;
                if(event.keyCode == 8 && auxDes.length == 0) borrarDatos();
                selecFila(auxDes);
            }else{
            if((tab.selectedIndex>-1)&&(tab.selectedIndex < lista.length)){
                document.forms[0].codigo.value = lista[tab.selectedIndex][0];
                document.forms[0].txtNomeDescripcion.value = lista[tab.selectedIndex][1];
                auxDes = lista[tab.selectedIndex][1];
            }
        }
    }
} */
    
    function buscar(tecla){
        var auxCod = new String("");
        var auxDes = new String("");
        //if((event.keyCode != 40)&&(event.keyCode != 38)){
        if((tecla != 40)&&(tecla != 38)){
            //if(event.keyCode != 13){
            if(tecla != 13){
                auxCod = document.forms[0].codigo.value;
                auxDes = document.forms[0].txtNomeDescripcion.value;
                //if(event.keyCode == 8 && auxDes.length == 0) borrarDatos();
                if(tecla == 8 && auxDes.length == 0) borrarDatos();
                selecFila(auxDes);
            }else{
            if((tab.selectedIndex>-1)&&(tab.selectedIndex < lista.length)){
                document.forms[0].codigo.value = lista[tab.selectedIndex][0];
                document.forms[0].txtNomeDescripcion.value = lista[tab.selectedIndex][1];
                auxDes = lista[tab.selectedIndex][1];
            }
        }
}
}


/////////////// Control teclas.

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar;
    if(window.event){
        evento = window.event;
        teclaAuxiliar  = evento.keyCode;
    }else
        teclaAuxiliar  = evento.which;

    if('Alt+M'==tecla) pulsarModificar();
    if('Alt+A'==tecla) pulsarAlta();
    if('Alt+E'==tecla) pulsarEliminar();
    if('Alt+L'==tecla) limpiarInputs();
    if('Alt+S'==tecla) pulsarSalir();
    
    //if (event.keyCode == 38 || event.keyCode == 40){
    if (teclaAuxiliar == 38 || teclaAuxiliar == 40){
        upDownTable(tab,lista,teclaAuxiliar);
    }
    
    //if (event.keyCode == 13) buscar();
    if (teclaAuxiliar == 13) buscar();
    keyDel(evento);
}

/** 
* Llamada al pulsar sobre el campo código, comprueba si se esta modificando algun rol
* (si hay alguna fila seleccionada) y en ese caso no permite editar este campo. Solo
* se permite editarlo para un alta de un rol.
*/
function comprobarModificableCodigo() {
    if (tab.selectedIndex != -1) { // Se esta editando una entrada
        //document.forms[0].codigo.readonly = true; <- no funciona
        document.forms[0].porDefecto.focus();
    } else {                       // Se esta dando una entrada de alta
    //document.forms[0].codigo.readonly = false; <- no funciona
}
}

document.onkeydown=checkKeys;

        </SCRIPT>
        
    </head>
    
    <body class="bandaBody" onload="javascript:{ pleaseWait('off');
       }" >
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        <form method="post">
            <input  type="hidden"  name="listaIdes" id="listaIdes">
            <input  type="hidden"  name="listaCodigos" id="listaCodigos">
            <input  type="hidden"  name="listaDescripciones" id="listaDescripciones">
            <input  type="hidden"  name="listaActuaciones" id="listaActuaciones">
            <input  type="hidden"  name="opcion" id="opcion">
            <input  type="hidden"  name="identificador" id="identificador">
            <input  type="hidden"  name="pde" id="pde">
            <input  type="hidden"  name="act" id="act">
            <input  type="hidden"  name="cw" id="cw">
            <input  type="hidden"  name="idModificar" id="idModificar">

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titMantRoles")%></div>
    <div class="contenidoPantalla" valign="top">
        <!-- Tabla de Datos -->
        <table>
            <tr>
                <td>
                    <div id="tabla" ></div>
              </td>
            </tr>

            <tr>
                <td>
                    <!-- Codigo -->
                    <input type="text" class="inputTextoObligatorio" id="obligatorio"  name="codigo"  
                           maxlength="2" onkeyup="return xAMayusculas(this);" onFocus="javascript:comprobarModificableCodigo();"
                           style="width:11%">
                    <!-- Descripcion -->
                    <input type="text" class="inputTextoObligatorio" id="obligatorio" name="txtNomeDescripcion" 
                           maxlength="70" onblur="return xAMayusculas(this);"
                           style="width:56.5%">
                    <!-- Por Defecto -->
                    <input type="checkbox"  id="obligatorio" name="porDefecto" onkeyup="return xAMayusculas(this);"
                           style="width:9%">
                    <input type="checkbox"  id="obligatorio" name="activo" style="width:9%">
                    <input type="checkbox"  id="obligatorio" name="consultaWeb" style="width:9%">
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAlta" onClick="pulsarAlta();" alt="<%= descriptor.getDescripcion("toolTip_bAltaMantRoles")%>" title="<%= descriptor.getDescripcion("toolTip_bAltaMantRoles")%>">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdModificar" onClick="pulsarModificar();" title='<%=descriptor.getDescripcion("toolTip_bModificar")%>' alt='<%=descriptor.getDescripcion("toolTip_bModificar")%>'>
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminar" onClick="pulsarEliminar();" title='<%=descriptor.getDescripcion("toolTip_bEliminar")%>' alt='<%=descriptor.getDescripcion("toolTip_bEliminar")%>'>
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> name="cmdLimpiar" onClick="limpiarInputs();" title='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>' alt='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>'>
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'>
        </div>       
    </div>       
</form>
<script>
            var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tabla"));
            
            tab.addColumna('100','center','<%=descriptor.getDescripcion("gEtiq_codigo")%>',715);
                tab.addColumna('506','center','<%=descriptor.getDescripcion("gEtiq_desc")%>',710);
                    tab.addColumna('94','center','<%=descriptor.getDescripcion("gEtiq_pde")%>');
                    tab.addColumna('94','center','<%=descriptor.getDescripcion("gEtiq_act")%>');
                    tab.addColumna('94','center','<%=descriptor.getDescripcion("gEtiq_cw")%>');
                    tab.displayCabecera=true;   
                    
                    function cargaTabla(){
                        var cont = 0;
                        var cont2 = 0;
                        var cont3 = 0;
                        
                        lista_ide = new Array();
                        lista = new Array();
                        <logic:iterate id="elemento" name="MantRolesForm" property="codigos">
                        lista_ide[cont] = ['<bean:write name="elemento" property="ide"/>'];
                            lista[cont] = ['<bean:write name="elemento" property="ide" />', '<bean:write name="elemento" property="txtNomeDescripcion"/>','<bean:write name="elemento" property="porDefecto"/>','<bean:write name="elemento" property="activo"/>','<bean:write name="elemento" property="consultaWeb"/>'];                            
                                cont = cont + 1;
                                </logic:iterate>
                                tab.lineas=lista;
                                refresh();
                            }
                            
                            function refresh(){
                                tab.displayTabla();
                            }
                            
                            function comprobarCodigo2(lineas,codigo){
                                for (i=0; i < lineas.length; i++){
                                    if(lineas[i][0]==(eval("document.forms[0]."+codigo+".value"))){
                                        jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                                            return false;
                                        }
                                    }
                                    return true;
                                }
                                
                                function comprobarCodigo(seleccion,lineas,codigo){
                                    for (i=0; i < lineas.length; i++){
                                        if (i!=seleccion){
                                            if(lineas[i][0]==(eval("document.forms[0]."+codigo+".value"))){
                                                jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                                                    return false;
                                                }
                                            }
                                        }
                                        return true;
                                    }
                                    
        </script>
        <script> Inicio(); </script>
        
    </BODY>
    
</html>
