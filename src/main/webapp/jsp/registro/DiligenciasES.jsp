<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.DiligenciasForm" %>
<%@page import="es.altia.agora.business.registro.DiligenciasValueObject" %>
<%@page import="es.altia.agora.technical.Fecha" %>
<%@page import="java.util.Date" %>

<HTML>
    <head>
        <%
            String tipo = request.getParameter("tipo");
            char t = tipo.charAt(0);
            int idioma = 1;
            String fechaBuscada = "";
            String anotacionBuscada = "";
            String css = "";
            DiligenciasForm dForm = new DiligenciasForm();
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    css = usuario.getCss();
                }
                dForm = (DiligenciasForm) session.getAttribute("DiligenciasForm");
                DiligenciasValueObject dVO = new DiligenciasValueObject();
                dVO = dForm.getDiligencia();
                fechaBuscada = dVO.getFechaBuscada();
                anotacionBuscada = dVO.getAnotacionBuscada();

            }

            Fecha f = new Fecha();
            Date dateServidor = new Date();
            String fechaServidor = f.obtenerString(dateServidor);

            String userAgent = request.getHeader("user-agent");

        %>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="1" />
        
        
        <TITLE>
            <%=descriptor.getDescripcion("dil_TitleR" + t)%>
        </TITLE>
        <!-- Estilos -->
        
        <!-- Ficheros javascript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <link rel="stylesheet" href="<%=request.getContextPath()%><%=css%>" type="text/css">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        
        <SCRIPT type="text/javascript" >
            
            var consultando =false;

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
            
        function checkKeysLocal(evento,tecla){
            var teclaAuxiliar = "";
            if(window.event){
                evento = window.event;
                teclaAuxiliar = evento.keyCode;
            }else{
                teclaAuxiliar = evento.which;
            }
            //** Esta funcion se debe implementar en cada JSP para particularizar  **//
            //** las acciones a realizar de las distintas combinaciones de teclas  **//
            if(tecla=="Alt+G"){
                if(document.getElementById('capaBotones2').style.display=='' || document.getElementById('capaBotones3').style.display=='')
                    pulsarGrabar();
            }else if(tecla=="Alt+B"){
                if(document.getElementById('capaBotones3').style.display=='')
                    pulsarBorrar();
                if(document.getElementById('capaBotones1').style.display=='')
                    pulsarBuscar();
            }else if(tecla=="Alt+S"){
                if(document.getElementById('capaBotones1').style.display=='')
                    pulsarSalir();
            }else if(tecla=="Alt+L"){
                if(document.getElementById('capaBotones1').style.display=='')
                    pulsarLimpiar();
                if(document.getElementById('capaBotones3').style.display=='')
                    pulsarListado();
            }else if(tecla=="Alt+A"){
                if(document.getElementById('capaBotones1').style.display=='')
                    pulsarAlta();
            }else if(tecla=="Alt+C"){
                if(document.getElementById('capaBotones3').style.display=='')
                    pulsarCancelarModificar();
                if(document.getElementById('capaBotones2').style.display=='')
                    pulsarCancelarAlta();
            }

            if (teclaAuxiliar == 1){
                if(IsCalendarVisible) replegarCalendario(coordx,coordy);
            }
            if (teclaAuxiliar == 9){
                if(IsCalendarVisible) hideCalendar();
            }
            
        }

// --- Funciones de página
function pulsarSalir(){
    window.location = "<%=request.getContextPath()%>/MantAnotacionRegistro.do?opcion=<%=tipo%>";
}

function pulsarLimpiar() {
    document.forms[0].opcion.value = "";
    document.forms[0].buscado.value = "";
    document.forms[0].fecha.value = "";
    document.forms[0].fechaAnotacion.value = "";
    document.forms[0].txtAnotacion.value = "";
}

function comprobarBusqueda() {
    if(document.forms[0].txtAnotacion.value != "" || document.forms[0].fechaAnotacion.value != "") {
        return true;
    } else {
    return false;
}
}

function comprobarObligatorio() {
    if(document.forms[0].txtAnotacion.value != "" && document.forms[0].fechaAnotacion.value != "") {
        return true;
    } else {
    return false;
}
}

function pulsarBuscar(){
    if (comprobarBusqueda()) {
        if (comprobarFecha(document.forms[0].fechaAnotacion) ) {	
            document.formulario.fecha.value = document.forms[0].fechaAnotacion.value;
            document.forms[0].opcion.value="buscar<%=tipo%>";
            document.forms[0].target="mainFrame";
            document.forms[0].action="<%=request.getContextPath()%>/Diligencias.do";
            document.forms[0].submit();
        }
    } else {
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjCampoBusq")%>');
    }
}

function pulsarListado(){
    document.forms[0].opcion.value="listad<%=tipo%>";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<%=request.getContextPath()%>/Diligencias.do";
    document.forms[0].submit();
}


function mostrarCalDesde(evento) {
    if(window.event) evento = window.event;
    if (document.getElementById("calDesde").className.indexOf("fa-calendar") != -1 )
        showCalendar('forms[0]','fechaAnotacion',null,null,null,'','calDesde','',null,null,null,null,null,null,null,null,evento);
}

function recuperaDatos(datos){
    document.forms[0].fechaAnotacion.value=datos[1];
    document.forms[0].txtAnotacion.value = datos[2];
    jsp_alerta("A","<%=descriptor.getDescripcion("msjNoDiligencia")%>");
        document.forms[0].txtAnotacion.focus();
    }
    
    function pulsarGrabar(){
        if (comprobarObligatorio()) {
            var texto = document.forms[0].txtAnotacion.value;
            if(texto.length <4000) {
                var fechaH = '<%=fechaServidor%>';
                var fechaHo = fechaH.split('/');
                var fechaHoy = new Date(fechaHo[2],fechaHo[1],fechaHo[0]);
                var fecha = document.forms[0].fechaAnotacion.value.split('/');
                var fechaAnotacion = new Date(fecha[2],fecha[1],fecha[0]);
                if((comprobarFecha(document.forms[0].fechaAnotacion))&&
                    (comparaFechas(fechaHoy,fechaAnotacion)<=1)){
                if (document.formulario.txtAnotacion.value == '') {
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjAnotacion")%>');
                        document.formulario.txtAnotacion.focus();
                        return;
                    }
                    document.formulario.fecha.value =document.forms[0].fechaAnotacion.value;
                    document.formulario.opcion.value = "grabar<%=tipo%>";
                    document.formulario.target = "oculto";
                    document.formulario.action = "<%=request.getContextPath()%>/Diligencias.do";
                    document.formulario.submit();
                }else{
                jsp_alerta("A",'<%=descriptor.getDescripcion("msjFechaNV")%>');
                }
            } else {
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjDilGrande")%>');
            }
        } else {
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
        }
    }
    
    function grabado(correcto){
        if(correcto=="0"){
            jsp_alerta("A",'<%=descriptor.getDescripcion("gEtiqDilGrabada")%>');
                obligatorioToNoObligatorio(document.forms[0]);
                mostrarCapasBotones('capaBotones1');
                consultando = true;
                pulsarLimpiar();
            }else{
            jsp_alerta("A",'<%=descriptor.getDescripcion("gEtiqDilNoGrabada")%>');
            }
        }
        
        function pulsarBorrar(){
            if (validarFormulario()) {
                if (comprobarFecha(document.forms[0].fechaAnotacion) ) {
                    if (document.formulario.txtAnotacion.value == '') {
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjAnotacion")%>');
                            document.formulario.txtAnotacion.focus();
                            return;
                        }
                        if (jsp_alerta("",'<%=descriptor.getDescripcion("msjBorrarDiligencia")%>')){
                            document.formulario.fecha.value = document.forms[0].fechaAnotacion.value;
                            document.formulario.opcion.value = "borrar<%=tipo%>";
                            document.formulario.target = "oculto";
                            document.formulario.action = "<%=request.getContextPath()%>/Diligencias.do";
                            document.formulario.submit();
                        }
                    }
                }
            }
            
            function borrado(correcto){
                if(correcto=="0"){
                    jsp_alerta("A",'<%=descriptor.getDescripcion("gEtiqDilBorrada")%>');
                        obligatorioToNoObligatorio(document.forms[0]);
                        mostrarCapasBotones('capaBotones1');
                        consultando = true;
                        pulsarLimpiar();
                    }else{
                    jsp_alerta("A",'<%=descriptor.getDescripcion("gEtiqDilNoBorrada")%>');
                    }
                }
                
                function inicializar(){
                    obligatorioToNoObligatorio(document.forms[0]);
                    mostrarCapasBotones('capaBotones1');
                    consultando = true;
                    habilitarImagenCal("calDesde",true);
                    document.forms[0].fechaAnotacion.focus();
                    document.all.tabla2.style.marginTop = 0;//calcularPosTabla(document.all.tabla1);
                    document.forms[0].buscado.value = "NO";
                    document.forms[0].txtAnotacion.value='';
                    pleaseWait('off');
                    var a = unescape('<bean:write name="DiligenciasForm" property="anotacion"/>');
                        var f = '<bean:write name="DiligenciasForm" property="fecha"/>';
                        if(a != null && a != "") {
                            document.forms[0].txtAnotacion.value = a;
                            mostrarCapasBotones('capaBotones3');
                            noObligatorioToObligatorio(document.forms[0]);
                            document.forms[0].buscado.value = "SI";
                        }
                        if(f != null && f != "") {
                            document.forms[0].fechaAnotacion.value = f;
                        }
                        document.forms[0].txtAnotacionBuscada.value = '<%= anotacionBuscada%>';
                        document.forms[0].fechaAnotacionBuscada.value = '<%= fechaBuscada%>';
                    }
                    
                    function pulsarAlta() {
                        noObligatorioToObligatorio(document.forms[0]);
                        mostrarCapasBotones('capaBotones2');
                        consultando = false;
                        pulsarLimpiar();
                    }
                    
                    function pulsarCancelarAlta() {
                        pulsarLimpiar();
                        mostrarCapasBotones('capaBotones1');
                        obligatorioToNoObligatorio(document.forms[0]);
                        consultando = true;
                    }
                    
                    function pulsarCancelarModificar() {
                        pulsarLimpiar();
                        mostrarCapasBotones('capaBotones1');
                        obligatorioToNoObligatorio(document.forms[0]);
                        consultando = true;
                    }
                    
                    function comprobarFecha(inputFecha) {
                        var formato = 'dd/mm/yyyy';
                        if (Trim(inputFecha.value)!='') {
                            if (consultando) {
                                var validas = true;
                                var fechaFormateada=inputFecha.value;
                                var pos=0;
                                var fechas = Trim(inputFecha.value);
                                var fechas_array = fechas.split(/[:|&<>!=]/);
                                for (var loop=0; loop < fechas_array.length; loop++) {
                                    f = fechas_array[loop];	
                                    formato = formatoFecha(Trim(f)); 
                                    var D = ValidarFechaConFormato(f,formato);
                                    if (!D[0]) {
                                        validas=false;
                                    } else {
                                    if (fechaFormateada.indexOf(f,pos) != -1) { 
                                        var toTheLeft = fechaFormateada.substring(0, fechaFormateada.indexOf(f));
                                        var toTheRight = fechaFormateada.substring(fechaFormateada.indexOf(f)+f.length, fechaFormateada.length);
                                        pos=fechaFormateada.indexOf(f,pos);
                                        fechaFormateada = toTheLeft + D[1]+ toTheRight;
                                    }
                                }
                            }
                            if (!validas) {
                                jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                                    inputFecha.focus();
                                    return false;
                                } else {
                                inputFecha.value = fechaFormateada;
                                return true;
                            }
                        } else { // No consultando. Unico formato posible: dd/mm/yy o dd/mm/yyyy
                        if (Trim(inputFecha.value)!='') {
                            if (!ValidarFechaConFormato(inputFecha.value,formato)){
                                jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                                    inputFecha.focus();
                                    return false;
                                } 
                            }
                        }
                    }
                    return true;
                }
                
                function soloCaracteresFechaConsultando(e) {                      
                    PasaAMayusculas(e);   
                    var tecla, caracter;
                    var alfanumericos = '0123456789/_-.:|&><!=';
                    tecla = e.keyCode;
                    if (tecla == null) return true;
                    caracter = String.fromCharCode(tecla);      
                    if ((alfanumericos.indexOf(caracter) != -1) || tecla==32)
                        return true;
                    return false;   
                }
                
                function ValidarFechaConFormato(fecha, formato) { 
                    if (formato==null) formato ="dd/mm/yyyy";
                    if (formato=="mm/yyyy") 
                        fecha = "01/"+fecha;
                    else if (formato=="yyyy") 
                        fecha ="01/01/"+fecha;
                    else if (formato =="mmyyyy")
                        fecha = "01"+fecha;
                    
                    var D = DataValida(fecha);
                    if (formato == "dd/mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr() : fecha; 
                    else if (formato == "mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
                    else if (formato == "yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(6) : fecha;
                    else if (formato == "mmyyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
                    return D;  
                }
                
                
        </SCRIPT>
        
    </head>
    
    <BODY class="bandaBody" onload="javascript:{}">
        
        <FORM name = "formulario" METHOD=POST target="_self">
        
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<FORM name = "formulario" METHOD=POST target="_self">
    <INPUT type="hidden" name="opcion">
    <input type="hidden" name="buscado">
    <input type="hidden" name="fecha">
    <input type="hidden" name="txtAnotacionBuscada" value ="">
    <input type="hidden" name="fechaAnotacionBuscada" value ="">
            
    <%if ('S' == t) {%>
    <div id="titulo" class="txttitblancoder"><%=descriptor.getDescripcion("dil_TitPrinR" + t)%></div>
    <% } else {%>
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("dil_TitPrinR" + t)%></div>
    <% }%>
    <div class="contenidoPantalla">
        <TABLE  id="tabla2" ALIGN="center" >
            <TR>
                <TD width="1%"></TD>
                <TD width="10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqFecha")%>:</TD>
                <TD width="80%" class="columnP">
                    <INPUT TYPE="text" id="obligatorio"  class="inputTxtFechaObligatorio" size="10" name="fechaAnotacion"
                           onkeyup = "javascript:if (consultando) return SoloCaracteresFechaConsultando(this); else return SoloCaracteresFecha(this);"
                           onblur = "javascript:return comprobarFecha(this);"
                           onfocus = "this.select();">
                    <span class="fa fa-calendar" aria-hidden="true" id="calDesde" name="calDesde" 
                         onClick="calClick(event);mostrarCalDesde(event);return false;" style="cursor:hand" alt="<%=descriptor.getDescripcion("altFecha")%>" title="<%=descriptor.getDescripcion("altFecha")%>"/></span>
                </TD>
            </TR>
            <TR>
                <TD width="1%"></TD>
                <TD width="10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqAnotacion")%>:</TD>
                <TD width="80%" class="columnP">
                    <textarea class="textareaTextoObligatorio" id="obligatorio" size="100" cols="80" rows="5" name="txtAnotacion" WRAP="virtual"
                              onblur="return xAMayusculas(this);"
                              onKeyDown="return textCounter(this,4000);">
                    </textarea>
                </TD>
            </TR>
        </TABLE>
        <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal" style="display:none">
            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbBuscar")%> name="cmdBuscar" onClick="pulsarBuscar();" title='<%=descriptor.getDescripcion("toolTip_bBuscar")%>' alt='<%=descriptor.getDescripcion("toolTip_bBuscar")%>'>
            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAlta" onClick="pulsarAlta();" title='<%=descriptor.getDescripcion("toolTip_bAlta")%>' alt='<%=descriptor.getDescripcion("toolTip_bAlta")%>'>
            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> name="cmdLimpiar" onClick="pulsarLimpiar();" title='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>' alt='<%=descriptor.getDescripcion("toolTip_bLimpiar")%>'>
            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" alt="<%=descriptor.getDescripcion("toolTip_bVolver")%>" title="<%=descriptor.getDescripcion("toolTip_bVolver")%>">
        </DIV>
        <DIV id="capaBotones2" name="capaBotones2" class="botoneraPrincipal" style="display:none">
            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbGrabar")%> name="cmdGrabarAlta" onClick="pulsarGrabar();">
            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelarAlta" onClick="pulsarCancelarAlta();">
        </DIV>
        <DIV id="capaBotones3" name="capaBotones3" class="botoneraPrincipal" style="display:none">
            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbGrabar")%> name="cmdGrabarModificar" onClick="pulsarGrabar();">
            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbBorrar")%> name="cmdBorrar" onClick="pulsarBorrar();">
            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelarModificar" onClick="pulsarCancelarModificar();">
            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbListado")%> name="cmdListado" onClick="pulsarListado();">
        </DIV>
    </DIV>
</FORM>

<SCRIPT>
            inicializar();
            
            function mostrarCapasBotones(nombreCapa) {
                document.getElementById('capaBotones1').style.display='none';
                document.getElementById('capaBotones2').style.display='none';
                document.getElementById('capaBotones3').style.display='none';
                document.getElementById(nombreCapa).style.display='';
            }
        </SCRIPT>
        
    </BODY>
    
</HTML>
