<!-- JSP de mantenimiento de temas -->
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
    <head>
        <title> Mantenimiento Temas</title>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        
        <%
            UsuarioValueObject usuarioVO = null;
            int idioma = 1;
            String css = "";
            if (session != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }
        %>
        
        
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />
        
        
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
            var util= new Array();
            var utilCerrados= new Array();
            var i;
            var res = 0;
            var ultimo = false;
            var existeCerrado = 0;
            
            function borrarDatos(){
                document.forms[0].codigo.value = '';
                document.forms[0].txtNomeDescripcion.value = '';
            }
            
            function Inicio() {
                window.focus();
                cargaTabla();
            }
            
            function pulsarModificar() {
                existeCerrado=0;
                var cod = document.forms[0].codigo.value;
                var yaExiste = 0;
                if(tab.selectedIndex != -1) {
                    if (validarFormulario()) {
                        for(j=0; j < utilCerrados.length; j++){
                            if (lista_ide[tab.selectedIndex]-utilCerrados[j] == 0){
                                existeCerrado = 1;
                            }
                        }
                        if(existeCerrado == 0){
                            for(l=0; l < lista.length; l++){
                                var lineaSeleccionada;
                                lineaSeleccionada = tab.selectedIndex;
                                if(l == lineaSeleccionada) {
                                    l= l;
                                } else {
                                if ((lista[l][0]) == cod ){
                                    yaExiste = 1;
                                }
                            }
                        }
                        if(yaExiste == 0) {
                            var id = lista_ide[tab.selectedIndex];
                            document.forms[0].identificador.value = id;
                            document.forms[0].opcion.value = 'modificar';
                            document.forms[0].target = "oculto";
                            document.forms[0].action = '<%=request.getContextPath()%>/MantTema.do';
                            document.forms[0].submit();
                        } else {
                        jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                        }
                    } else {
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjNoModificar")%>');
                    }
                }
            } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
            
            function pulsarAlta() {
                var cod = document.forms[0].codigo.value;
                var yaExiste = 0;
                if (validarFormulario()) {
                    for(l=0; l < lista.length; l++){
                        if ((lista[l][0]) == cod ){
                            yaExiste = 1;
                        }
                    }
                    if(yaExiste == 0) {
                        document.forms[0].opcion.value = 'alta';
                        document.forms[0].target = "oculto";
                        document.forms[0].action = '<%=request.getContextPath()%>/MantTema.do';
                        document.forms[0].submit();
                    } else {
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                    }
                }
            }
            
            function pulsarEliminar() {
                existe=0;
                if(tab.selectedIndex != -1) {
                    if (validarFormulario()) {
                        for(l=0; l < util.length; l++){
                            if (lista_ide[tab.selectedIndex]-util[l] == 0){
                                existe = 1;
                            }
                        }
                        var id = lista_ide[tab.selectedIndex];
                        if(existe == 0){
                            document.forms[0].identificador.value = id;
                            document.forms[0].opcion.value = 'eliminar';
                            document.forms[0].target = "oculto";
                            document.forms[0].action = '<%=request.getContextPath()%>/MantTema.do';
                            document.forms[0].submit();
                            limpiarInputs();
                        } else {
                        jsp_alerta('A', '<%=descriptor.getDescripcion("msjManUtil")%>');
                        }
                    }
                } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                }
                
                function recuperaDatos(lista1,lista2,lista4,lista5) {
                    
                    lista_ide = new Array();
                    lista = new Array();
                    util = new Array();
                    utilCerrados = new Array();
                    
                    lista_ide = lista1;
                    lista = lista2;
                    util = lista4;
                    utilCerrados = lista5;
                    
                    limpiarInputs();
                    
                    tab.lineas=lista;
                    refresh();                           
                }
                
                function pulsarSalir(){
                    window.location = "<%=request.getContextPath()%>/MantTema.do?opcion=salir";
                }
                
                function limpiarInputs() {
                    tab.selectLinea(tab.selectedIndex);
                    borrarDatos();                    
                }
                
                /////////////// B�squeda r�pida.
                
                function rellenarDatos(tableObject, rowID){
                    if(rowID>-1 && !tableObject.ultimoTable){
                        document.forms[0].codigo.value = lista[rowID][0];
                        document.forms[0].txtNomeDescripcion.value = lista[rowID][1];
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
                
                function buscar(){
                    var auxCod = new String("");
                    var auxDes = new String("");
                    if((event.keyCode != 40)&&(event.keyCode != 38)&&(event.keyCode != 8)){
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
            }
            
            /////////////// Control teclas.
            
            function checkKeysLocal(evento,tecla) {
                var teclaAuxiliar;
                if(window.event){
                   evento = window.event;
                   teclaAuxiliar = evento.keyCode;
                } else
                   teclaAuxiliar = evento.which;

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
            
            document.onkeydown=checkKeys;
            
        </SCRIPT>
        
    </head>
    
    <body class="bandaBody" onload="javascript:{pleaseWait('off');
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
            
    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("tit_manTema")%></div>
    <div class="contenidoPantalla">
    <table>
        <tr>
            <td id="tabla" align="left">
            </td>
        </tr>
        <tr>
            <td align="left">
                <input type="text" class="inputTextoObligatorio" id="obligatorio"  name="codigo" 
                       maxlength="5" onkeyup="return xAMayusculas(this);" 
                       style="width:10%">
                <input type="text" class="inputTextoObligatorio" id="obligatorio" 
                       name="txtNomeDescripcion"  maxlength="45" onkeyup="return xAMayusculas(this);" 
                       style="width:89%">
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAlta" onClick="pulsarAlta();" alt="<%= descriptor.getDescripcion("toolTip_bAltaTema")%>" title="<%= descriptor.getDescripcion("toolTip_bAltaTema")%>">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdModificar" onClick="pulsarModificar();" title='<%=descriptor.getDescripcion("toolTip_bModificar")%>' alt='<%=descriptor.getDescripcion("toolTip_bModificar")%>'>
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminar" onClick="pulsarEliminar();" title='<%=descriptor.getDescripcion("toolTip_bEliminar")%>' alt='<%=descriptor.getDescripcion("toolTip_bEliminar")%>'>
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> name="cmdLimpiar" onClick="limpiarInputs();" title='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>' alt='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>'>
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'>
    </div>
</div>
</form>

<script language="JavaScript1.2">
        var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'),822);
        tab.addColumna('80','left','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
        tab.addColumna('728','left','<%= descriptor.getDescripcion("gEtiq_desc")%>');
        tab.addColumna('0','left','<%= descriptor.getDescripcion("gEtiq_desc")%>');
        tab.displayCabecera=true;
                    
    function cargaTabla(){
        var cont = 0;
        var cont2 = 0;
        var cont3 = 0;

        lista_ide = new Array();
        lista = new Array();
        <logic:iterate id="elemento" name="MantTemaForm" property="codigos">
        lista_ide[cont] = ['<bean:write name="elemento" property="ide"/>'];
            lista[cont] = ['<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="txtNomeDescripcion"/>', '<bean:write name="elemento" property="activo"/>'];
                cont = cont + 1;
        </logic:iterate>
        <logic:iterate id="ut" name="MantTemaForm" property="utilizados">
                util[cont2] = ['<bean:write name="ut"/>'];
                    cont2 = cont2 + 1;
          </logic:iterate>

           <logic:iterate id="utC" name="MantTemaForm" property="utilizadosCerrados">
                    utilCerrados[cont3] = ['<bean:write name="utC"/>'];
                        cont3 = cont3 + 1;
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
