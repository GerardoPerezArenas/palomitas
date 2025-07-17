<!-- JSP con formulario de los datos de persona -->
<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
    <head>
        <title> MANTENIMIENTO DE ACTUACIONES </title>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
       
        
        
        <%
            UsuarioValueObject usuarioVO = null;
            int idioma = 1;
            String css="";
            if (session != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                css=usuarioVO.getCss();
            }
            String userAgent = request.getHeader("user-agent");
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />
        
        <!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
         <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" >
            
            var codigo = new Array();
            var descripcion = new Array();
            var fechaDesde = new Array();
            var fechaHasta = new Array();
            var cont = 0;
            var cont2 = 0;
            var cont3 = 0;
            var res = 0;
            var lista_ide = new Array();
            var lista = new Array();
            var util= new Array();
            var utilCerrados= new Array();
            var act = false;
            var ultimo = false;
            var existe = 0;
            var existeCerrado = 0;
            
            // ------ Calendario
            function mostrarCalDesde(evento) {
                if(window.event) evento = window.event;
                if (document.getElementById("calDesde").className.indexOf("fa-calendar") != -1 )
                    showCalendar('forms[0]','inputFechaDesde',null,null,null,'','calDesde','',null,null,null,null,null,null,null, '',evento);
            }
            
            function mostrarCalHasta(evento) {
                if(window.event) evento = window.event;
                if (document.getElementById("calHasta").className.indexOf("fa-calendar") != -1 )
                    //showCalendar('forms[0]','inputFechaHasta',null,null,null,'','calHasta','');
                     showCalendar('forms[0]','inputFechaHasta',null,null,null,'','calHasta','',null,null,null,null,null,null,null, '',evento);
            }
            
            function cargaTabla(){
                <logic:iterate id="b" name="ManActuacionesForm" property="codigos">
                lista_ide[cont] = ['<bean:write name="b" property="ide"/>'];
                    lista[cont] = ['<bean:write name="b" property="codigo"/>', '<bean:write name="b" property="descripcion"/>',
                        '<bean:write name="b" property="fechaDesde"/>', '<bean:write name="b" property="fechaHasta"/>'];
                        cont = cont + 1;
                        </logic:iterate>
                        <logic:iterate id="ut" name="ManActuacionesForm" property="utilizados">
                        util[cont2] = ['<bean:write name="ut"/>'];
                            cont2 = cont2 + 1;
                            </logic:iterate>
                            
                            <logic:iterate id="utC" name="ManActuacionesForm" property="utilizadosCerrados">
                            utilCerrados[cont3] = ['<bean:write name="utC"/>'];
                                cont3 = cont3 + 1;
                                </logic:iterate>
                                
                                tab.lineas = lista;
                                refresh();
                            }
                            
                            function borrarDatos(){
                                document.forms[0].txtCodigo.value = '';
                                document.forms[0].txtDescripcion.value = '';
                                document.forms[0].inputFechaDesde.value = '';
                                document.forms[0].inputFechaHasta.value = '';
                            }
                            
                            function Inicio() {
                                window.focus();
                                cargaTabla();
                                habilitarImagenCal("calDesde",true);
                                habilitarImagenCal("calHasta",true);
                            }
                            
                            function formateaFecha(dia, mes, ano) {
                                dia.value = formateaIntToString(dia .value,2);
                                mes.value = formateaIntToString(mes.value,2);
                                ano.value = formateaAno(ano.value,2);
                            }
                            
                            
                            function comprobarCamposFechas(inputFechaA, inputFechaD){
                                
                                if (comprobarFecha(inputFechaA)) {
                                    
                                    if(inputFechaD.value.length != 0){
                                        if (comprobarFecha(inputFechaD) ) {
                                            var fechaA  = new Date(inputFechaA.value.substring(6,10), eval(inputFechaA.value.substring(3,5)-1), inputFechaA.value.substring(0,2));
                                            var fechaD  = new Date(inputFechaD.value.substring(6,10), eval(inputFechaD.value.substring(3,5)-1), inputFechaD.value.substring(0,2));
                                            
                                            if (comparaFechas(fechaA,fechaD) == 1) {
                                                jsp_alerta("A",'<%=descriptor.getDescripcion("msjFDMayor")%>');
                                                    document.forms[0].inputFechaHasta.focus();
                                                    return 0;
                                                }else{
                                                return 1;
                                            }
                                        }else{
                                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjFHNoVal")%>');
                                            return 0;
                                        }
                                    }else 
                                    
                                    if(inputFechaD.value.length == 0) {
                                        return 1;
                                    }else{
                                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjFHVacio")%>');
                                        return 0;
                                    }
                                }else{
                                jsp_alerta("A",'<%=descriptor.getDescripcion("msjFDNoVal")%>');
                                    return 0;
                                }
                            }
                            
                            function pulsarSalir(){
                                window.location = "<%=request.getContextPath()%>/ManActuaciones.do?opcion=salir";
                            }
                            
                            function limpiarInputs() {
                                tab.selectLinea(tab.selectedIndex);
                                borrarDatos();
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
                                            document.forms[0].action = '<%=request.getContextPath()%>/ManActuaciones.do';
                                            document.forms[0].submit();
                                        } else {
                                        jsp_alerta('A', '<%=descriptor.getDescripcion("msjManUtil")%>');
                                        }
                                    }
                                } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                                }
                                
                                function pulsarModificar() {
                                    existeCerrado=0;
                                    var cod = document.forms[0].txtCodigo.value;
                                    var yaExiste = 0;
                                    if(tab.selectedIndex != -1) {
                                        if ((validarFormulario())&&(comprobarCamposFechas(document.forms[0].inputFechaDesde, document.forms[0].inputFechaHasta))){  
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
                                                document.forms[0].action = '<%=request.getContextPath()%>/ManActuaciones.do';
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
                                    var cod = document.forms[0].txtCodigo.value;
                                    var yaExiste = 0;
                                    if (validarFormulario()) {
                                        if ( comprobarCamposFechas(document.forms[0].inputFechaDesde, document.forms[0].inputFechaHasta) ){
                                            for(l=0; l < lista.length; l++){
                                                if ((lista[l][0]) == cod ){
                                                    yaExiste = 1;
                                                }
                                            }
                                            if(yaExiste == 0) {
                                                document.forms[0].opcion.value = 'alta';
                                                document.forms[0].target = "oculto";
                                                document.forms[0].action = '<%=request.getContextPath()%>/ManActuaciones.do';
                                                document.forms[0].submit();
                                            } else {
                                            jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
                                            }
                                        }
                                    }
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
                                
                                /////////////// Búsqueda rápida.
                                
                                function rellenarDatos(tableObject, rowID){
                                    if(rowID>-1 && !tableObject.ultimoTable){	
                                        with(document.forms[0]){
                                            txtCodigo.value = lista[rowID][0];
                                            txtDescripcion.value = lista[rowID][1];			
                                            inputFechaDesde.value = lista[rowID][2];
                                            inputFechaHasta.value = ((lista[rowID][3]).length!=0)?lista[rowID][3]:"";
                                        }
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
                                    if((event.keyCode != 40)&&(event.keyCode != 38)){
                                        if(event.keyCode != 13){
                                            auxCod = document.forms[0].txtCodigo.value;
                                            auxDes = document.forms[0].txtDescripcion.value;
                                            if(event.keyCode == 8 && auxDes.length == 0) borrarDatos();
                                            selecFila(auxDes);
                                        }else{
                                        if((tab.selectedIndex>-1)&&(tab.selectedIndex < lista.length)){
                                            document.forms[0].txtCodigo.value = lista[tab.selectedIndex][0];
                                            document.forms[0].txtDescripcion.value = lista[tab.selectedIndex][1];
                                            var fDesde = lista[tab.selectedIndex][2];
                                            var fHasta = lista[tab.selectedIndex][3];
                                            document.forms[0].inputFechaDesde.value = fDesde;
                                            document.forms[0].inputFechaHasta.value = fHasta;
                                            auxDes = lista[tab.selectedIndex][1];
                                        }
                                    }
                                }
                            }
                            
                            /////////////// Control teclas.
                            //Usado para el calendario
                            var coordx=0;
                            var coordy=0;


                             <%if(userAgent.indexOf("MSIE")==-1) {%> //Que no sea IE
                                window.addEventListener('mousemove', function(e) {
                                    coordx = e.clientX;
                                    coordy = e.clientY;
                                }, true);
                            <%}%>

                                document.onmouseup = checkKeys;

                            function checkKeysLocal(evento,tecla) {
                                var teclaAuxiliar;
                                if(window.event){
                                    evento          = window.event;
                                    teclaAuxiliar  =  evento.keyCode;
                                }else
                                    teclaAuxiliar  =  evento.which;

                                if('Alt+M'==tecla) pulsarModificar();
                                if('Alt+A'==tecla) pulsarAlta();
                                if('Alt+E'==tecla) pulsarEliminar();
                                if('Alt+L'==tecla) limpiarInputs();
                                if('Alt+S'==tecla) pulsarSalir();
                                
                                //if (event.keyCode == 38 || event.keyCode == 40){
                                if (teclaAuxiliar== 38 || teclaAuxiliar == 40){
                                    upDownTable(tab,lista,teclaAuxiliar);
                                }
                                //if (event.keyCode == 13) buscar();
                                if (teclaAuxiliar== 13) buscar();


                                if (teclaAuxiliar == 1){
                                    if(IsCalendarVisible) replegarCalendario(coordx,coordy);
                                }
                                if (teclaAuxiliar == 9){
                                    if(IsCalendarVisible) hideCalendar();
                                }



                                keyDel(evento);
                            }
                            
                            document.onkeydown=checkKeys;
                            function comprobarFecha(inputFecha) {
                                if (Trim(inputFecha.value)!='') {
                                    if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
                                        jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                                            inputFecha.focus();
                                            return false;
                                        } 
                                    } 
                                    return true;
                                }
                                
        </script>
        <meta http-equiv="" content="text/html; charset=iso-8859-1">
    </head>
    
    <body class="bandaBody"  scroll=no onload="javascript:{pleaseWait('off');
        }">
 
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        
        <form name="formulario" method="post">
            <input  type="hidden"  name="listaIdes" id="listaIdes">
            <input  type="hidden"  name="listaCodigos" id="listaCodigos">
            <input  type="hidden"  name="listaDescripciones" id="listaDescripciones">
            <input  type="hidden"  name="listaFechasDesde" id="listaFechasDesde">
            <input  type="hidden"  name="listaFechasHasta" id="listaFechasHasta">
            <input  type="hidden"  name="listaActuaciones" id="listaActuaciones">
            <input  type="hidden"  name="opcion" id="opcion">
            <input  type="hidden"  name="identificador" id="identificador">
            
<div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("tit_manAct")%></div>
<div class="contenidoPantalla">
    <table style="width: 100%;" cellpadding="0px" align="center">
        <tr>
            <td >
                <div id="tabla"></div>
            </td>
        </tr>
        <tr>
            <td>
                <input type="text" class="inputTextoObligatorio" id="obligatorio"  
                       name="txtCodigo" maxlength="6" onkeyup="return xAMayusculas(this);"
                       style="width:7.5%">
                <input name="txtDescripcion" id="obligatorio" type="text" 
                       class="inputTextoObligatorio" maxlength="50" onkeyup="return xAMayusculas(this);"
                       style="width:61%">
                <INPUT TYPE="text" id="obligatorio"  class="inputTxtFechaObligatorio" 
                       maxlength="12" name="inputFechaDesde"
                       onkeyup = "javascript:return SoloCaracteresFecha(this);"
                       onblur = "javascript:return comprobarFecha(this);"
                       onfocus = "this.select();" style="width:12%">
                <a href="javascript:calClick(event);return false;" onClick="mostrarCalDesde(event);return false;" 
                   onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration: none">
                    <span class="fa fa-calendar" aria-hidden="true" name="calDesde" id="calDesde" alt="Data" ></span>
                </a>
                <INPUT TYPE="text" class="inputTxtFecha" maxlength="10" name="inputFechaHasta"
                       onkeyup = "javascript:return SoloCaracteresFecha(this);"
                       onblur = "javascript:return comprobarFecha(this);"
                       onfocus = "this.select();" style="width:11%">
                <a href="javascript:calClick(event);return false;" onClick="mostrarCalHasta(event);return false;" 
                   onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration: none">
                    <span class="fa fa-calendar" aria-hidden="true" name="calHasta" id="calHasta" alt="Data" ></span>
                </a>
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAlta" onClick="pulsarAlta();" alt="<%= descriptor.getDescripcion("toolTip_bAltaActuacion")%>" title="<%= descriptor.getDescripcion("toolTip_bAltaActuacion")%>">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdModificar" onClick="pulsarModificar();" title='<%=descriptor.getDescripcion("toolTip_bModificar")%>' alt='<%=descriptor.getDescripcion("toolTip_bModificar")%>'>
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminar" onClick="pulsarEliminar();" title='<%=descriptor.getDescripcion("toolTip_bEliminar")%>' alt='<%=descriptor.getDescripcion("toolTip_bEliminar")%>'>
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> name="cmdLimpiar" onClick="limpiarInputs();" title='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>' alt='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>'>
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'>
    </div>
</div>
</form>
        
        
        <script>
            var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'),850);
            tab.addColumna('60','left','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
                tab.addColumna('510','left','<%= descriptor.getDescripcion("gEtiq_desc")%>');
                    tab.addColumna('128','center','<%= descriptor.getDescripcion("gEtiq_desde")%>');
                        tab.addColumna('128','center','<%= descriptor.getDescripcion("gEtiq_hasta")%>');
                            tab.displayCabecera=true;
                            function refresh(){
                                tab.displayTabla();
                            }
                            
                            function comprobarCodigo(seleccion,lineas,codigo){
                                for (i=0; i < lineas.length; i++){
                                    if (i!=seleccion){
                                        if(lineas[i][0]==(eval("document.forms[0]."+codigo+".value"))){
                                            jsp_alerta('Este código ya existe');
                                            return false;
                                        }
                                    }
                                }
                                return true;
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
                                
        </script>
        <script> Inicio(); </script>
        
        
        
    </body>
</html>
