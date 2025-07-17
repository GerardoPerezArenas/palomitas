<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.FusionDivisionForm" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<html>
<head>
<title>Fusion y Division de secciones</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma = 1;
    int apl = 3;
    if (session.getAttribute("usuario") != null) {
        usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
        apl = usuarioVO.getAppCod();
        idioma = usuarioVO.getIdioma();
    }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");

    String userAgent = request.getHeader("user-agent");
%>
        
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript">
// VARIABLES GLOBALES
<%
    FusionDivisionForm fdForm = (FusionDivisionForm) session.getAttribute("FusionDivisionForm");
%>
var lista = new Array();
var codUsuarios = new Array();
var descUsuarios = new Array();
var codEstados = new Array();
var descEstados = new Array();
var botonProcesar = ['botonProcesar'];
var botonDetalle = ['botonDetalle'];
var botonEliminar = ['botonEliminar'];
var botonFinalizar = ['botonFinalizar'];
var botonRetroceder = ['botonRetroceder'];
// INICIALIZACIÓN Y CARGA DE DATOS
function inicializar(){
    habilitarGeneralInputs(botonProcesar,false);
    habilitarGeneralInputs(botonDetalle,false);
    habilitarGeneralInputs(botonEliminar,false);
    habilitarGeneralInputs(botonFinalizar,false);
    habilitarGeneralInputs(botonRetroceder,false);
    recuperarDatosIniciales();
}

function recuperarDatosIniciales(){
<%
    Vector listaUsuarios = fdForm.getListaUsuarios();
    int lengthUsuarios = listaUsuarios.size();
    int i = 0;
    String codUsuarios = "";
    String descUsuarios = "";
    for (i = 0; i < lengthUsuarios - 1; i++) {
        GeneralValueObject usuarios = (GeneralValueObject) listaUsuarios.get(i);
        codUsuarios += "\"" + usuarios.getAtributo("codUsuario") + "\",";
        descUsuarios += "\"" + usuarios.getAtributo("descUsuario") + "\",";
    }
    GeneralValueObject usuarios = (GeneralValueObject) listaUsuarios.get(i);
    codUsuarios += "\"" + usuarios.getAtributo("codUsuario") + "\"";
    descUsuarios += "\"" + usuarios.getAtributo("descUsuario") + "\"";
%>
    codUsuarios = [<%=codUsuarios%>];
    descUsuarios = [<%=descUsuarios%>];
    codEstados = ['P','F'];
    descEstados = ['Pendiente','Finalizado'];
    // CARGAR COMBOS
    comboUsuario.addItems(codUsuarios,descUsuarios);
    comboEstado.addItems(codEstados,descEstados);
}
        
/**************  FUNCIONES PARA LA CARGA DE LOS CALENDARIOS ***********************/
function mostrarCalendario(img,campoFecha,evento){
    var indice = document.getElementById(img).className.indexOf('fa-calendar');
    if (indice!=-1)
        showCalendar('forms[0]',campoFecha,null,null,null,'',img,'',null,null,null,
            null,null,null,null,'',evento);
}//de la funcion
            
//Funcion para la verificación de un campo fecha
function comprobarFecha(inputFecha){
    if(Trim(inputFecha.value)!=''){
        if(!ValidarFechaConFormato(document.forms[0],inputFecha)){
            jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                inputFecha.focus();
                return false;
        }
    }
    return true;
}//de la funcion
                
function actualizarTablaProcesos(){
    habilitarGeneralInputs(botonProcesar,false);
    habilitarGeneralInputs(botonDetalle,false);
    tablaProcesos.lineas = lista;
    refresca(tablaProcesos);
}

// FUNCIONES DE PULSACION DE BOTONES
function pulsarMostrar(){
    document.forms[0].opcion.value="buscarProcesos";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do";
    document.forms[0].submit();
}

function obtenerArgumentosProcesar(){
    var i = tablaProcesos.focusedIndex;
    var argumentos = new Array();
    argumentos[0] = obtenerTipoOperacion();
    argumentos[1] = lista[i];
    document.forms[0].codDistritoOrigen.value = lista[i][6];
    document.forms[0].codSeccionOrigen.value = lista[i][8];
    document.forms[0].letraOrigen.value = lista[i][10];
    return argumentos;
}

var datosProceso = new Array();

function pulsarProcesar(){
    datosProceso = obtenerArgumentosProcesar();
    document.forms[0].opcion.value="inicializarProcesar";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do";
    document.forms[0].submit();
}

function pulsarRetroceder(){
    /*
    datosProceso = obtenerArgumentosProcesar();
    document.forms[0].opcion.value="retroceder";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do";
    document.forms[0].submit();
    */
    jsp_alerta('A', '<%=descriptor.getDescripcion("msjSinImpl")%>');
    }

    function pulsarDetalle(){
        datosProceso = obtenerArgumentosProcesar();
        document.forms[0].opcion.value="inicializarVerDetalle";
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do";
        document.forms[0].submit();
    }

    function pulsarEliminar(){
        var confirmar = jsp_alerta("",'<%=descriptor.getDescripcion("msjDesElimProc")%>');
            if(confirmar){
                document.forms[0].opcion.value="eliminarProceso";
                document.forms[0].target="oculto";
                document.forms[0].action="<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do";
                document.forms[0].submit();
            }
        }

        function pulsarFinalizarProceso(){
            var confirmar = jsp_alerta("",'<%=descriptor.getDescripcion("msjDesDProcProc")%>');
                if(confirmar){
                    document.forms[0].opcion.value="finalizarProceso";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do";
                    document.forms[0].submit();
                }
            }

            function obtenerTipoOperacion(){
                var tipo = 'F';
                if(document.forms[0].tipoOperacion[1].checked)
                    tipo = 'D';
                return tipo;
            }

            function pulsarNuevo(){
                var source = "<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do?opcion=inicializarInsertarProceso";
                var tipoOperacion =obtenerTipoOperacion();
                abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source,tipoOperacion,
                        'width=857,height=300,status='+ '<%=statusBar%>',function(ventana){
                                window.setTimeout("pulsarMostrar()",50);
                        });
            }

                function pulsarSalir(){
                    document.forms[0].opcion.value="inicializarTerc";
                    document.forms[0].target="mainFrame";
                    document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                    /* version DANI
                    document.forms[0].opcion.value="salir";
                    document.forms[0].target="mainFrame";
                    document.forms[0].action="<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do";
                    */
                    document.forms[0].submit();
                }

                function abrirInforme(nombre){
                    if (!(nombre =='')) {
                        // PDFS NUEVA SITUACION
                        var sourc = "<%=request.getContextPath()%>/jsp/verPdf.jsp?opcion=null&nombre="+nombre;
                        ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp?source="+sourc,'ventanaInforme','width=800px,height=550px,status='+ '<%=statusBar%>' + ',toolbar=no');
                            ventanaInforme.focus();
                            // FIN PDFS NUEVA SITUACION

                            //ventanaInforme = window.open("/jsp/registro/ver_pdf.jsp?fichero='/pdf/"+nombre+".pdf'",
                            //  "informe", "width=800px,height=550px,toolbar=no");
                            //ventanaInforme.focus();
                        } else {
                        jsp_alerta('A','<%=descriptor.getDescripcion("msjNoPDF")%>');
                        }
                    }//de la funcion
                                    
    </script>

</head>
<body class="bandaBody" onload="inicializar()">
<form action="" method="post" name="formulario" id="formulario">
    <input type="hidden" name="opcion" value="">
    <input type="hidden" name="codProceso" value="">
    <input type="hidden" name="codDistritoOrigen" value="">
    <input type="hidden" name="codSeccionOrigen" value="">
    <input type="hidden" name="letraOrigen" value="">

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titFusDivSecc")%></div>
    <div class="contenidoPantalla">
        <table style="width: 100%" >
            <tr>
                <td>
                    <table width="100%">
                        <tr>
                            <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("etiqOperDes")%>: </td>
                            <td style="width: 10%" class="etiqueta">
                                <label>
                                    <input name="tipoOperacion" type="radio" value="F" checked>
                                    <%=descriptor.getDescripcion("etiqFusion")%>
                                </label>
                            </td>
                            <td class="etiqueta">
                                <label>
                                    <input type="radio" name="tipoOperacion" value="D">
                                    <%=descriptor.getDescripcion("etiqDivis")%>
                                </label>
                            </td>
                        </tr>
                    </table>

                </td>
            </tr>
            <tr>
                <td>
                    <table width="100%">
                        <tr>
                            <td style="width: 17%" class="etiqueta"><%=descriptor.getDescripcion("etiqFecProc")%>:</td>
                            <td style="width: 6%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Desde")%>:</td>
                            <td style="width: 20%">
                                <input class="inputTxtFechaObligatorio" type="text" size="11"
                                       maxlength="10" name="fechaDesde"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus = "this.select();">
                                <a name="anchorDesde" id="anchorDesde"
                                   href="javascript:calClick(event);return false;"
                                   onClick="mostrarCalendario('Desde','fechaDesde',event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;"
                                   style="text-decoration:none;">
                                    <span class="fa fa-calendar" aria-hidden="true" name="Desde" id="Desde"></span>
                                </a>
                            </td>
                            <td style="width: 6%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Hasta")%>:</td>
                            <td>
                                <input class="inputTxtFechaObligatorio" type="text" size="11"
                                       maxlength="10" name="fechaHasta"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus = "this.select();">
                                <a name="anchorHasta" id="anchorHasta"
                                   href="javascript:calClick(event);return false;"
                                   onClick="mostrarCalendario('Hasta','fechaHasta',event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;"
                                   style="text-decoration:none;">
                                    <span class="fa fa-calendar" aria-hidden="true" name="Hasta" id="Hasta"></span>
                                </a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <table width="100%">
                        <tr>
                            <td style="width: 8%" class="etiqueta"><%=descriptor.getDescripcion("etiqUsuar")%>: </td>
                            <td style="width: 30%">
                                <input name="codUsuario" type="text" class="inputTexto"
                                       id="codUsuario" size="3">
                                <input name="descUsuario" type="text" class="inputTexto"
                                       id="descUsuario" style="width:150" readonly>
                                <a id="anchotUsuario" name="anchorUsuario" href="">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                         id="botonUsuario" name="botonUsuario"
                                         style="cursor:hand;"></span>
                                </a>
                            </td>
                            <td style="width: 8%" class="etiqueta"><%=descriptor.getDescripcion("etiqEstado")%>: </td>
                            <td>
                                <input name="codEstado" type="text" class="inputTexto"
                                       id="codEstado" size="3">
                                <input name="descEstado" type="text" class="inputTexto"
                                       id="descEstado" style="width:150" readonly>
                                <a id="anchotEstado" name="anchorEstado" href="">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                         id="botonEstado" name="botonEstado"
                                         style="cursor:hand;"></span>
                                </a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td style="padding-bottom: 15px;padding-left:700px">
                    <input class="botonGeneral" name="botonMostrar" type="button" id="botonMostrar"
                                                   accesskey="M" value="<%=descriptor.getDescripcion("etiqMostrar")%>"
                                                   onclick="pulsarMostrar();">
                </td>
            </tr>
            </tr>
            <tr>
                <td id="tablaProcesos"></td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input name="botonProcesar" type="button" class="botonGeneral" id="botonProcesar"
                                       accesskey="P" value="<%=descriptor.getDescripcion("etiqProc")%>"
                                       onclick="pulsarProcesar();">
            <input name="botonRetroceder" type="button" class="botonGeneral" id="botonRetroceder"
                                       accesskey="R" value="<%=descriptor.getDescripcion("etiqRetroc")%>"
                                       onclick="pulsarRetroceder();">
            <input name="botonDetalle" type="button" class="botonGeneral" id="botonDetalle"
                                       accesskey="D" value="<%=descriptor.getDescripcion("etiqDetalle")%>"
                                       onclick="pulsarDetalle();">
            <input name="botonNuevo" type="button" class="botonGeneral" id="botonNuevo"
                                       accesskey="N" value="<%=descriptor.getDescripcion("gbNuevo")%>"
                                       onclick="pulsarNuevo();">
            <input name="botonEliminar" type="button" class="botonGeneral" id="botonEliminar"
                                       accesskey="E" value="<%=descriptor.getDescripcion("gbEliminar")%>"
                                       onclick="pulsarEliminar();">
            <input name="botonFinalizar" type="button" class="botonLargo" id="botonFinalizar"
                                       accesskey="F" value="<%=descriptor.getDescripcion("gbFinProc")%>" 
                                       onclick="pulsarFinalizarProceso();">
            <input name="botonSalir" type="button" class="botonGeneral" id="botonSalir"
                                       accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>"
                                       onclick="pulsarSalir();">
        </div>            
    </div>            
</form>

<script type="text/javascript">

            //Usado para el calendario
            var coordx=0;
            var coordy=0;


            <%if(userAgent.indexOf("MSIE")==-1) {%> //Que no sea IE
                window.addEventListener('mousemove', function(e) {
                    coordx = e.clientX;
                    coordy = e.clientY;
                }, true);
            <%}%>


            var tablaProcesos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaProcesos'));
            
            var cabecera = "";
            tablaProcesos.addColumna('0','center','Código');
            tablaProcesos.addColumna('170','center','<%=descriptor.getDescripcion("gEtiq_Descrip")%>');
            tablaProcesos.addColumna('90','center','<%=descriptor.getDescripcion("gEtiqFecha")%>');
            tablaProcesos.addColumna('0','center','codUsuario');
            tablaProcesos.addColumna('90','center','<%=descriptor.getDescripcion("etiqUsuar")%>');
            tablaProcesos.addColumna('90','center','<%=descriptor.getDescripcion("etiqEstado")%>');
            tablaProcesos.addColumna('0','center','codDistritoOrigen');
            tablaProcesos.addColumna('90','center','<%=descriptor.getDescripcion("etiqDistOrig")%>');
            tablaProcesos.addColumna('0','center','codSeccionOrigen');
            tablaProcesos.addColumna('90','center','<%=descriptor.getDescripcion("etiqSeccOrig")%>');
            tablaProcesos.addColumna('0','center','Letra Origen');
            tablaProcesos.addColumna('0','center','codDistritoDestino');
            tablaProcesos.addColumna('90','center','<%=descriptor.getDescripcion("etiqDistDest")%>');
            tablaProcesos.addColumna('0','center','codSeccionDestino');
            tablaProcesos.addColumna('90','center','<%=descriptor.getDescripcion("etiqSeccDest")%>');
            tablaProcesos.addColumna('0','center','Letra Destino');
            tablaProcesos.addColumna('0','center','iniciado');
                                            
                                            tablaProcesos.displayCabecera = true;
                                            tablaProcesos.displayTabla();
                                            tablaProcesos.colorLinea=function(rowID){
                                            }
                                            
                                            function refresca(tabla){
                                                tabla.displayTabla();
                                            }
                                            
                                            document.onmouseup = checkKeys;
                                            
                                            function callFromTableTo(rowID,tableName){
                                                if(tablaProcesos.id == tableName){
                                                }
                                            }
                                            
                                            function checkKeysLocal(evento,tecla){
                                                var teclaAuxiliar = "";
                                                if(window.event){
                                                    evento         = window.event;
                                                    teclaAuxiliar =  evento.keyCode;
                                                }else
                                                    teclaAuxiliar =  evento.which;

                                                keyDel(evento);
                                                if (teclaAuxiliar == 38) upDownTable(tablaProcesos,lista,teclaAuxiliar);
                                                if (teclaAuxiliar == 40) upDownTable(tablaProcesos,lista,teclaAuxiliar);
                                                if (teclaAuxiliar == 13){
                                                    // if (comboUsuario.base.style.visibility == "visible")comboUsuario.ocultar();
                                                   //else  comboUsuario.display();
                                                    pushEnterTable(tablaProcesos,lista);
                                                }
                                                if (teclaAuxiliar == 1) {
                                                   
                                                    if(IsCalendarVisible) replegarCalendario(coordx,coordy);
                                                    if (comboUsuario.base.style.visibility == "visible" && isClickOutCombo(comboUsuario,coordx,coordy)) setTimeout('comboUsuario.ocultar()',20);
                                                        
                                                    if (comboEstado.base.style.visibility == "visible" && isClickOutCombo(comboEstado,coordx,coordy)) setTimeout('comboEstado.ocultar()',20);
                                                }
                                                if (teclaAuxiliar == 9) {
                                                    if(IsCalendarVisible) hideCalendar();
                                                    comboUsuario.ocultar(); 
                                                    comboEstado.ocultar();
                                                }
                                            }
                                            
                                            
                                            function rellenarDatos(tableName,listName){
                                                var i = tablaProcesos.focusedIndex;
                                                if((i>=0)&&!tablaProcesos.ultimoTable){
                                                    document.forms[0].codProceso.value= lista[i][0];
                                                    habilitarGeneralInputs(botonRetroceder,true);
                                                    if(lista[i][5]=='P'){
                                                        habilitarGeneralInputs(botonProcesar,true);
                                                        habilitarGeneralInputs(botonFinalizar,true);
                                                    }else{
                                                    habilitarGeneralInputs(botonProcesar,false);
                                                    habilitarGeneralInputs(botonFinalizar,false);
                                                }
                                                if(lista[i][16]=="SI"){
                                                    habilitarGeneralInputs(botonEliminar,false);
                                                    habilitarGeneralInputs(botonDetalle,true);
                                                    habilitarGeneralInputs(botonFinalizar,true);
                                                }else{
                                                habilitarGeneralInputs(botonEliminar,true);
                                                habilitarGeneralInputs(botonDetalle,false);
                                                habilitarGeneralInputs(botonFinalizar,false);
                                            }
                                        }else{
                                        habilitarGeneralInputs(botonProcesar,false);
                                        habilitarGeneralInputs(botonDetalle,false);
                                        habilitarGeneralInputs(botonEliminar,false);
                                        habilitarGeneralInputs(botonFinalizar,false);
                                        habilitarGeneralInputs(botonRetroceder,false);
                                    }
                                }
                                
                                // COMBOS
                                var comboUsuario = new Combo("Usuario");
                                var comboEstado = new Combo("Estado");
        </script>
    </body>
</html>
