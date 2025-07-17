<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.technical.Fecha" %>
<%@page import="java.util.Date" %>
<%@page import="java.util.Calendar"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="java.util.Vector" %>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.IdiomasManager" %>
<%@ page import="es.altia.agora.business.util.GeneralValueObject" %>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>

<html>
    <head>
        <%
            String tipo = "E";
            char t = tipo.charAt(0);
            int idioma = 1;
            String css = "";
            String[] params = null;
            UsuarioValueObject usuario = null;
            if (session != null) {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    css = usuario.getCss();
                    params = usuario.getParamsCon();
                }
            }
            // Fecha actual del servidor.
            Fecha f = new Fecha();
            Date dateServidor = new Date();
            String fechaServidor = f.obtenerString(dateServidor);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateServidor);
            cal.set(Calendar.MONTH, 0);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date dateInicioAno = cal.getTime();
            String fechaInicioAno = f.obtenerString(dateInicioAno);

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");

            Vector listaIdiomas = IdiomasManager.getInstance().getListaIdiomas(params);

            String userAgent = request.getHeader("user-agent");
            
            MantAnotacionRegistroForm mantARForm;
if (request.getAttribute("MantAnotacionRegistroForm") != null) mantARForm = (MantAnotacionRegistroForm) request.getAttribute("MantAnotacionRegistroForm");
else mantARForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
        
      
        %>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        <jsp:useBean id="desc" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="desc"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="desc"  property="apl_cod" value="1" />
        <TITLE>::: REGISTRO DE ENTRADA - Entradas Rechazadas - Pendientes Fin Digitalización :::</TITLE>
        <meta http-equiv="X-UA-Compatible" content="IE=EDGE"/>
        <jsp:include page="/jsp/plantillas/Metas.jsp" />

        <!-- Estilos -->
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%><%=css%>" type="text/css">

        <!-- Ficheros javascript -->
        <script src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>

    </head>
    <body class="bandaBody" onload="javascript:{inicializar(); } ">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=desc.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>



        <form name = "formulario" METHOD=POST target="_self">
            <input type="hidden" name="fechaInicio" value="">
            <input type="hidden" name="fechaFin" value="">
            <input type ="hidden" name="ano" value="">
            <input type="hidden" name="numero" value="">
            <input type="hidden" name="opcion" value="">
            <input type="hidden" name="tipoAnotacion" value="E">
            <input type="hidden" name="desdeEntradasRechazadas">
            <input type="hidden" name="desdePendientesFinalizar">

            <input type="hidden" name="posicionAnotacion" value=""/>
            <html:hidden property="paginaListado" value="1"/>
            <html:hidden property="numLineasPaginaListado" value="10"/>
            <html:hidden property="columna" value=""/>
            <html:hidden property="tipoOrden" value=""/>


            <div id="titulo" name= "cambiarTitulo"  class="txttitblanco"><%=desc.getDescripcion("titEntRec")%></div>

            <div class="contenidoPantalla">
               <!-- Selector de capas ajustado para que 'Entradas Pendientes de Finalización' sea la opción predeterminada y aparezca primero -->
               <div id="selectorCapa" style="margin-bottom: 15px;">
                   <span class="etiqueta" style="margin-left:15px;">
                       <%=desc.getDescripcion("etiqPendFinDigit")%>:
                       <input type="radio" name="capaActiva" value="2" id="radioPendientes" onclick="activarDesactivarCapa(this.value);" checked="checked">
                   </span>
                   <span class="etiqueta">
                       <%=desc.getDescripcion("etiqEntRechazadas")%>:
                       <input type="radio" name="capaActiva" value="1" id="radioRechazadas" onclick="activarDesactivarCapa(this.value);">
                   </span>
               </div>

                <div id="capaEntradasRechazadas">
                    <div class="sub3titulo" style="width: 97%;"><%=desc.getDescripcion("etiqFiltrosRech")%></div>
                    <div class="camposFormulario camposFormulario-inputUnico col-sm-12">
                        <label class="etiqueta" style="width:15.5%"><%=desc.getDescripcion("etiqMisRechazadas")%>:</label>
                        <input type="checkbox" name="checkMisRechazadas" id="checkMisRechazadas" checked>
                    </div>

                    <div class="camposFormulario camposFormulario-inputUnico col-sm-12">
                        <label class="etiqueta"><%=desc.getDescripcion("gEtiqFechaDesde")%>:</label>
                        <input type="text" id="fechaDesde" class="inputTxtFechaObligatorio" size="12" maxlength="10" name="fechaDesde" 
                           onkeyup="javascript:return SoloCaracteresFecha(this);"
                           onblur="javascript:return comprobarFecha(this);"
                           onfocus="this.select();">
                        <a href="javascript:calClick(event); return false;" style="margin-right: 15px;text-decoration: none" onClick="mostrarCalDesde(event);
                                                        return false;" onblur="ocultarCalendarioOnBlur(event);
                                                                return false;">
                            <span class="fa fa-calendar" aria-hidden="true" style="border: 0; height:17px; width: 5px" name="calDesde" id="calDesde"></span>
                        </a> 
                    
                        <label class="etiqueta"><%=desc.getDescripcion("gEtiqFechaHasta")%>:</label>
                        <input type="text" id="fechaHasta" class="inputTxtFecha" size="12" maxlength="10" name="fechaHasta" 
                           onkeyup="javascript:return SoloCaracteresFecha(this)"
                           onblur="javascript:return comprobarFecha(this);"
                           onfocus="this.select();">
                        <a href="javascript:calClick(event); return false;" onClick="mostrarCalHasta(event);
                                                     return false;" onblur="ocultarCalendarioOnBlur(event);
                                                             return false;">
                            <span class="fa fa-calendar" aria-hidden="true" style="border:0;height:17px; width:5px;" name="calHasta" id="calHasta"></span>
                        </a>
                    </div>

                    <div class="camposFormulario camposFormulario-inputUnico col-sm-12">
                        <label class="etiqueta"><%=desc.getDescripcion("etiqueta_asunto")%>:</label>
                        <input type="text" class="inputTexto" id="codAsunto" name="codAsunto" maxlenght="10"  style="width:8%"/>
                        <input type="text"  class="inputTexto" id="descAsunto" name="descAsunto"style="width:65%" readonly/>
                        <a href="" style="text-decoration:none;" name="anchorAsunto" id="anchorAsunto">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true"  name="botonAsunto" style="cursor:pointer;" id="botonAsunto"></span>
                        </a>
                    </div>

                    <input type="button" class="botonGeneral botonDerecha" value=<%=desc.getDescripcion("gbLimpiar")%> name="cmdLimpiar" onclick="pulsarLimpiar();"
                           alt="<%=desc.getDescripcion("toolTip_bLimpiar")%>" title="<%=desc.getDescripcion("toolTip_bLimpiar")%>" >  
                    <input type="button" class="botonGeneral botonDerecha" value=<%=desc.getDescripcion("gbBuscar")%> name="cmdBuscar" onclick="pulsarBuscar();"
                           alt="<%=desc.getDescripcion("toolTip_bBuscar")%>" title="<%=desc.getDescripcion("toolTip_bBuscar")%>" >
                 
                    <div id="tablaEntradasRechazadas">  </div>             

                   
             
                </div>
              <!-- Capa de Entradas Pendientes de Finalizar -->
          <div id="capaPendientesFinalizar" style="display:none">
                             <div class="sub3titulo" style="width: 97%;"><%=desc.getDescripcion("etiqFiltrosPendi")%></div>

                                 <span class="etiqueta" style="margin-left:15px;"><%=desc.getDescripcion("etiqMisEntradas")%>
                                       <input type="radio" name="tipoEntrada" value="1" id="radioMisEntradas" onclick="cargarPendientesFinalizar(this.value);">
                                 </span>
                                 <span class="etiqueta" style="margin-left:15px;"><%=desc.getDescripcion("etiqEntradasMiOficina")%>
                                   <input type="radio"  name="tipoEntrada" value="2" id="radioEntradasMiOficina" onclick="cargarPendientesFinalizar(this.value);">
                                </span>


                                <span class="etiqueta" style="margin-left:15px;"><%=desc.getDescripcion("etiqTodasEntradas")%>
                                   <input type="radio"  name="tipoEntrada" value="3" id="radioTodasEntradas" onclick="cargarPendientesFinalizar(this.value);">
                               </span>
                                   <br>

                             <div id="tablaPendientesFinalizar" style="margin-top :15px"></div>
                         </div>
                         <div class="botoneraPrincipal" ">
                             <input type= "button" class="botonGeneral" value=<%=desc.getDescripcion("gbSalir")%> name="cmdSalir"  onClick="pulsarSalir();"
                                    alt="<%=desc.getDescripcion("toolTip_bVolver")%>" title="<%=desc.getDescripcion("toolTip_bVolver")%>">
                         </div>
                     </div>
                 </form>

        <div id="popupcalendar" class="textoSuelto"></div>

        <script>
            var APP_CONTEXT_PATH = "<%=request.getContextPath()%>";
            var URL_CARGA_DATOS = APP_CONTEXT_PATH + '/MantAnotacionRegistro.do';
            var cod_Asunto = new Array();
            var desc_Asunto = new Array();

            var listaEntradasPendientes = new Array();
            var listaEntradasPendientesOriginal = new Array();
            
            var cargaInicialPendientes = true;
            //Usado para el calendario
            var coordx = 130;
            var coordy = 100;


            <%if(userAgent.indexOf("MSIE")==-1) {%> //Que no sea IE
            window.addEventListener('mousemove', function(e) {
                coordx = e.clientX;
                coordy = e.clientY;
            }, true);
            <%}%>

            document.onmouseup = checkKeys;
            var comboAsunto = new Combo("Asunto");

            $("#codAsunto").focusout(function() {
                comboAsunto.buscaCodigo($("#codAsunto").val().toUpperCase());
            });
            
            // tabla Entradas Rechazadas
            var tablaEntradasRechazadas = new Tabla(true, '<%=desc.getDescripcion("buscar")%>', '<%=desc.getDescripcion("anterior")%>', '<%=desc.getDescripcion("siguiente")%>', '<%=desc.getDescripcion("mosFilasPag")%>', '<%=desc.getDescripcion("msgNoResultBusq")%>', '<%=desc.getDescripcion("mosPagDePags")%>', '<%=desc.getDescripcion("noRegDisp")%>', '<%=desc.getDescripcion("filtrDeTotal")%>', '<%=desc.getDescripcion("primero")%>', '<%=desc.getDescripcion("ultimo")%>', document.getElementById("tablaEntradasRechazadas"), null);

            tablaEntradasRechazadas.addColumna('50', 'center', '<%=desc.getDescripcion("rotulo_numAnot")%>');
            tablaEntradasRechazadas.addColumna('70', 'center', '<%=desc.getDescripcion("rotulo_fechaPres")%>');
            tablaEntradasRechazadas.addColumna('70', 'center', '<%=desc.getDescripcion("rotulo_fechaGrab")%>');
            tablaEntradasRechazadas.addColumna('100', 'center', '<%=desc.getDescripcion("rotulo_remite")%>');
            tablaEntradasRechazadas.addColumna('100', 'center', '<%=desc.getDescripcion("etiq_Extracto")%>');
            tablaEntradasRechazadas.addColumna('100', 'center', '<%=desc.getDescripcion("etiqueta_asunto")%>');
            tablaEntradasRechazadas.addColumna('100', 'center', '<%=desc.getDescripcion("rotulo_uniDestino")%>');
            tablaEntradasRechazadas.addColumna('100', 'center', '<%=desc.getDescripcion("rotulo_usu")%>');
            tablaEntradasRechazadas.addColumna('100','center','<%=desc.getDescripcion("rotulo_usuRechazo")%>');

            tablaEntradasRechazadas.displayCabecera = true;
 
            // tabla Entradas Pendientese de Finalizar Digitalización
            var tablaPendientesFinalizar = new Tabla(true, '<%=desc.getDescripcion("buscar")%>', '<%=desc.getDescripcion("anterior")%>', '<%=desc.getDescripcion("siguiente")%>', '<%=desc.getDescripcion("mosFilasPag")%>', '<%=desc.getDescripcion("msgNoResultBusq")%>', '<%=desc.getDescripcion("mosPagDePags")%>', '<%=desc.getDescripcion("noRegDisp")%>', '<%=desc.getDescripcion("filtrDeTotal")%>', '<%=desc.getDescripcion("primero")%>', '<%=desc.getDescripcion("ultimo")%>', document.getElementById("tablaPendientesFinalizar"), null);
            tablaPendientesFinalizar.addColumna('50', 'center', '<%=desc.getDescripcion("rotulo_numAnot")%>');
            tablaPendientesFinalizar.addColumna('70', 'center', '<%=desc.getDescripcion("rotulo_fechaPres")%>');
            tablaPendientesFinalizar.addColumna('70', 'center', '<%=desc.getDescripcion("rotulo_fechaGrab")%>');
            tablaPendientesFinalizar.addColumna('100', 'center', '<%=desc.getDescripcion("rotulo_remite")%>');
            tablaPendientesFinalizar.addColumna('100', 'center', '<%=desc.getDescripcion("etiq_Extracto")%>');
            tablaPendientesFinalizar.addColumna('100', 'center', '<%=desc.getDescripcion("etiqueta_asunto")%>');
            tablaPendientesFinalizar.addColumna('100', 'center', '<%=desc.getDescripcion("rotulo_uniDestino")%>');
            tablaPendientesFinalizar.addColumna('100', 'center', '<%=desc.getDescripcion("rotulo_usu")%>');
           
            tablaPendientesFinalizar.displayCabecera = true;
            tablaPendientesFinalizar.displayTabla();
            
            function pulsarBuscar() {
                var misRechazadas = $("#checkMisRechazadas").is(":checked");

                if (comprobarCampos() == 1) {
                    var fechaDesde = $("#fechaDesde").val();
                    var fechaHasta = $("#fechaHasta").val();

                    var datos = {};
                    datos.opcion = 'buscarEntradasRechazadas';
                    datos.misRechazadas = misRechazadas;
                    datos.fechaDesde = fechaDesde.substring(0, 2) + '/' + fechaDesde.substring(3, 5) + '/' + fechaDesde.substring(6, 10);
                    datos.fechaHasta = fechaHasta.substring(0, 2) + '/' + fechaHasta.substring(3, 5) + '/' + fechaHasta.substring(6, 10);
                    datos.ejercicio = fechaDesde.substring(6, 10);

                    if (comboAsunto.selectedIndex != -1) {
                        datos.codAsunto = $('input[name=codAsunto]').val();
                    }
                    pleaseWait('on');
                    try {
                        $.ajax({
                            url: URL_CARGA_DATOS,
                            type: 'POST',
                            async: true,
                            data: datos,
                            success: successPulsarBuscarEntradasRechazadas,
                            error: mostrarErrorRespuestaAjax
                        });
                    } catch (Err) {
                        pleaseWait('off');
                        jsp_alerta("A", '<%=desc.getDescripcion("msgErrGenServ")%>');
                    }
                }

            }


            function successPulsarBuscarEntradasRechazadas(ajaxResult) {
                pleaseWait('off');
                var entradasRechazadas = JSON.parse(ajaxResult);

                var contadorAsientos = 0;
                if (entradasRechazadas != null && entradasRechazadas != undefined) {
                    listaEntradasRechazadas = new Array();
                    listaEntradasRechazadasOriginal = new Array();
                    for (var i = 0; i < entradasRechazadas.length; i++) {
                        var numeroAnotacion = entradasRechazadas[i].numReg;
                        var ejercicio = entradasRechazadas[i].ejeOrigen;
                        var numeroAnotCompleto = ejercicio + '/' + numeroAnotacion;
                        var diaP = entradasRechazadas[i].fecEntrada.substring(0, 2);
                        var mesP = entradasRechazadas[i].fecEntrada.substring(3, 5);
                        var anoP = entradasRechazadas[i].fecEntrada.substring(6, 10);
                        var horaP = entradasRechazadas[i].fecEntrada.substring(11, 19);
                        var fechaPresentacion = diaP + '-' + mesP + '-' + anoP + '<br/>' + horaP;
                        var diaG = entradasRechazadas[i].fechaDocu.substring(0, 2);
                        var mesG = entradasRechazadas[i].fechaDocu.substring(3, 5);
                        var anoG = entradasRechazadas[i].fechaDocu.substring(6, 10);
                        var horaG = entradasRechazadas[i].fechaDocu.substring(11, 19);
                        var fechaGrabacion = diaG + '-' + mesG + '-' + anoG + '<br/>' + horaG;
                        var nombreInteresado = entradasRechazadas[i].nombreInteresado;
                        var apellido1Interesado = entradasRechazadas[i].apellido1Interesado;
                        var apellido2Interesado = entradasRechazadas[i].apellido2Interesado;
                        var masInteresados = entradasRechazadas[i].masInteresados;
                        var nombreCompleto = getNombreCompletoInteresado(nombreInteresado != undefined ? nombreInteresado.trim() : '', apellido1Interesado != undefined ? apellido1Interesado.trim() : '', apellido2Interesado != undefined ? apellido2Interesado.trim() : '', masInteresados);
                        var extracto = entradasRechazadas[i].asunto;
                        var asunto = '';
                        if (entradasRechazadas[i].listaAsuntos[0].codigo) {
                            asunto = entradasRechazadas[i].listaAsuntos[0].codigo + '<br\>' + entradasRechazadas[i].listaAsuntos[0].descripcion;
                        }
                        var unidadDestino = entradasRechazadas[i].uorCodVisible;
                        var usuarioAlta = entradasRechazadas[i].usuarioQRegistra;
                        var usuarioRechazo = entradasRechazadas[i].usuarioRechazo;

                        listaEntradasRechazadasOriginal[contadorAsientos] = [numeroAnotacion, ejercicio, fechaPresentacion, fechaGrabacion, nombreCompleto, extracto, asunto, unidadDestino, usuarioAlta, usuarioRechazo];
                        listaEntradasRechazadas[contadorAsientos] = [numeroAnotCompleto, fechaPresentacion, fechaGrabacion, nombreCompleto, extracto, asunto, unidadDestino, usuarioAlta, usuarioRechazo];
                        contadorAsientos++;

                    }

                    tablaEntradasRechazadas.lineas = listaEntradasRechazadas;

                    tablaEntradasRechazadas.displayTabla(); 
                }
            }
            function limpiarTablaPendientes(){
                     tablaPendientesFinalizar.lineas = [];
                     tablaPendientesFinalizar.displayTabla();
           }
            
            function cargarPendientesFinalizar(filtro){
                var datos={};
                datos.opcion = 'entradasPendientesFinalizar';
                datos.filtro = filtro ;
                limpiarTablaPendientes();
                try {
                        $.ajax({
                            url: URL_CARGA_DATOS,
                            type: 'POST',
                            async: true,
                            data: datos,
                            success: successCargarPendientesFinalizar,
                            error: mostrarErrorRespuestaAjax
                        });
                    } catch (Err) {
                        pleaseWait('off');
                        jsp_alerta("A", '<%=desc.getDescripcion("msgErrGenServ")%>');
                    }
                }
            
            
            function successCargarPendientesFinalizar(ajaxResult){
                if(ajaxResult){
                    var entradasPendientes = JSON.parse(ajaxResult);
                    
                    var contadorAsientos = 0;
                    if (entradasPendientes != null && entradasPendientes != undefined) {
                    listaPendientes = new Array();
                    listaPendientesOriginal = new Array();
                    for (var i = 0; i < entradasPendientes.length; i++) {
                        var numeroAnotacion = entradasPendientes[i].numReg;
                        var ejercicio = entradasPendientes[i].ejeOrigen;
                        var numeroAnotCompleto = ejercicio + '/' + numeroAnotacion;
                        var diaP = entradasPendientes[i].fecEntrada.substring(0, 2);
                        var mesP = entradasPendientes[i].fecEntrada.substring(3, 5);
                        var anoP = entradasPendientes[i].fecEntrada.substring(6, 10);
                        var horaP = entradasPendientes[i].fecEntrada.substring(11, 19);
                        var fechaPresentacion = diaP + '-' + mesP + '-' + anoP + '<br/>' + horaP;
                        var diaG = entradasPendientes[i].fechaDocu.substring(0, 2);
                        var mesG = entradasPendientes[i].fechaDocu.substring(3, 5);
                        var anoG = entradasPendientes[i].fechaDocu.substring(6, 10);
                        var horaG = entradasPendientes[i].fechaDocu.substring(11, 19);
                        var fechaGrabacion = diaG + '-' + mesG + '-' + anoG + '<br/>' + horaG;
                        var nombreInteresado = entradasPendientes[i].nombreInteresado;
                        var apellido1Interesado = entradasPendientes[i].apellido1Interesado;
                        var apellido2Interesado = entradasPendientes[i].apellido2Interesado;
                        var masInteresados = entradasPendientes[i].masInteresados;
                        var nombreCompleto = getNombreCompletoInteresado(nombreInteresado != undefined ? nombreInteresado.trim() : '', apellido1Interesado != undefined ? apellido1Interesado.trim() : '', apellido2Interesado != undefined ? apellido2Interesado.trim() : '', masInteresados);
                        var extracto = entradasPendientes[i].asunto;
                        var asunto = '';
                        if (entradasPendientes[i].listaAsuntos != null && entradasPendientes[i].listaAsuntos != "" && entradasPendientes[i].listaAsuntos[0].codigo) {
                            asunto = entradasPendientes[i].listaAsuntos[0].codigo + '<br\>' + entradasPendientes[i].listaAsuntos[0].descripcion;
                        }
                        var unidadDestino = entradasPendientes[i].uorCodVisible;
                        var usuarioAlta = entradasPendientes[i].usuarioQRegistra;

                        listaPendientesOriginal[contadorAsientos] = [numeroAnotacion, ejercicio, fechaPresentacion, fechaGrabacion, nombreCompleto, extracto, asunto, unidadDestino, usuarioAlta];
                        listaPendientes[contadorAsientos] = [numeroAnotCompleto, fechaPresentacion, fechaGrabacion, nombreCompleto, extracto, asunto, unidadDestino, usuarioAlta];
                        contadorAsientos++;

                        }
                     cargaInicialPendientes = false;   
                     tablaPendientesFinalizar.lineas = listaPendientes;

                     tablaPendientesFinalizar.displayTabla();
                    }
                }
            
            }
             // Función para cambiar el título según la capa activa
             function cambiarTitulo(capa) {
                var titulo = document.getElementById("titulo");
                if (capa === '1') {
                    titulo.innerHTML = "<%=desc.getDescripcion("titEntRec")%>"; // Título para 'Entradas Rechazadas'
                  } else if (capa === '2') {
                    titulo.innerHTML = "<%=desc.getDescripcion("titPenFin")%>"; // Título para 'Entradas Pendientes de Finalizar'
                  }
             }

            function activarDesactivarCapa(capa){
                if (capa == '1') {
                cambiarTitulo("1");
                    $("#capaEntradasRechazadas").show();
                    $("#capaPendientesFinalizar").hide();
                } else if (capa = '2') {
                cambiarTitulo("2");
                    $("#capaEntradasRechazadas").hide();
                    $("#capaPendientesFinalizar").show();

                }
            }

            function getDescripcionCortaAsuntoAsiento(descripcion) {
                var salida = descripcion;

                if (descripcion != null && descripcion != "" && descripcion.length > 40)
                    salida = descripcion.substring(0, 40) + "...";

                return salida;
            }
            
            
  

            function getNombreCompletoInteresado(nombre, apellido1, apellido2, masInteresados) {
                var nombreCompleto = "";
                try {
                    if (apellido1 != null && apellido1 != undefined && apellido1.length > 0 && apellido1 != "")
                        nombreCompleto += apellido1 + nombreCompleto;

                    if (apellido2 != null && apellido2 != undefined && apellido2.length > 0 && apellido2 != "")
                        nombreCompleto += " " + apellido2;

                    if (nombre != null && nombre != undefined && nombre.length > 0) {
                        if (nombreCompleto.length == 0 || nombreCompleto == "")
                            nombreCompleto += nombre;
                        else
                            nombreCompleto += ", " + nombre;
                    }

                    if (masInteresados)
                        nombreCompleto += " Y OTROS";

                } catch (Err) {
                    jsp_alerta("A",'<%=desc.getDescripcion("msgErrGenServ")%>' + nombreCompleto);
                }
                return nombreCompleto;
            }


            function cargaAsuntos() {
                pleaseWait('on');
                var datos = {};
                datos.opcion = 'recuperarAsuntos';

                try {
                    $.ajax({
                        url: URL_CARGA_DATOS,
                        type: 'POST',
                        async: true,
                        data: datos,
                        success: procesarRespuestaCargarAsuntos,
                        error: mostrarErrorRespuestaAjax
                    });
                } catch (Err) {
                    pleaseWait('off');
                    jsp_alerta("A", '<%=desc.getDescripcion("msgErrGenServ")%>');
                }
            }
            
            function procesarRespuestaCargarAsuntos(ajaxResult) {
                pleaseWait('off');
                if (ajaxResult) {
                    var listadoAsuntos = JSON.parse(ajaxResult);
                    if (listadoAsuntos.length > 0) {
                        for (var i = 0; i < listadoAsuntos.length; i++) {
                            cod_Asunto[i] = listadoAsuntos[i].codigo;
                            desc_Asunto[i] = listadoAsuntos[i].descripcion;
                        }
                        comboAsunto.selectItem(-1);
                        comboAsunto.addItems(cod_Asunto, desc_Asunto);
                    }
                    
                    busquedaInicio();
                } else {
                    jsp_alerta("A", "<%=desc.getDescripcion("msgErrGenServ")%>");
                }
            }


            function mostrarErrorRespuestaAjax(){
                jsp_alerta("A",'<%=desc.getDescripcion("msgErrGenServ")%>');
            }
            
            function pulsarSalir() {
                window.location = "<%=request.getContextPath()%>/MantAnotacionRegistro.do?opcion=E";
            }


            function pulsarLimpiar(){
                $("#codAsunto").val("");
                comboAsunto.selectItem(-1);
                
                $("#fechaDesde").val("");
                $("#fechaHasta").val("");
            }
           function inicializar() {
               pleaseWait('on');
               var opcion = '<%= request.getParameter("opcion") %>';
               if (opcion == 'volver_pendientes') {
                   activarDesactivarCapa("2");
                   $("#radioPendientes").prop("checked", true);
                   cargaAsuntos();
                   pleaseWait('off');
               } else {
                   activarDesactivarCapa("2");  // Esto garantiza que se muestre por defecto las entradas pendientes
                   cargaAsuntos();
               }
           }

            
            
            function busquedaInicio() {
            <%
               String fDesde = "";
               String fHasta = "";
               String codigoAsunto ="";
               Boolean misRechazadas = true;
                            
               if(request.getAttribute("fechaDesde")!=null) {
                   fDesde = (String) request.getAttribute("fechaDesde");
               } else {
                   fDesde = fechaInicioAno;
               }
               if(request.getAttribute("fechaHasta")!=null){
                   fHasta = (String) request.getAttribute("fechaHasta");
               } else {
                   fHasta = fechaServidor;
               }
               if(request.getAttribute("codAsunto")!=null)
                   codigoAsunto = (String) request.getAttribute("codAsunto");
               if(request.getAttribute("misRechazadas")!=null)
    
               if(request.getAttribute("misRechazadas")!=null){
                   misRechazadas = (Boolean) request.getAttribute("misRechazadas");;
               }
            %>
                $('#fechaDesde').val('<%=fDesde%>');
                $('#fechaHasta').val('<%=fHasta%>');
                
                
                comboAsunto.buscaCodigo("<%=codigoAsunto%>");


                if (!<%=misRechazadas%>) {
                    $('#checkMisRechazadas').prop("checked", false);
                }

                habilitarImagenCal("calDesde", true);
                habilitarImagenCal("calHasta", true);


                pulsarBuscar();


            }
            // --- Calendario y validación fechas

            function mostrarCalDesde(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calDesde").className.indexOf("fa-calendar") != -1)
                    showCalendar('forms[0]', 'fechaDesde', null, null, null, '', 'calDesde', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalHasta(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calHasta").className.indexOf("fa-calendar") != -1)
                    showCalendar('forms[0]', 'fechaHasta', null, null, null, '', 'calHasta', '', null, null, null, null, null, null, null, null, evento);
            }

            function comprobarFecha(inputFecha) {
                if (Trim(inputFecha.value) != '') {

                    if (!ValidarFechaConFormato(document.forms[0], inputFecha)) {
                        jsp_alerta("A", '<%=desc.getDescripcion("fechaNoVal")%>');
                        return false;
                    }
                }
                return true;
            }
            
            function comprobarCampos() {
                var r = 1;

                if (document.forms[0].fechaDesde.value == '') {
                    var r = jsp_alerta("A", '<%=desc.getDescripcion("msjAbrirCond10")%>');
                    document.forms[0].fechaDesde.select();
                    r = 0;
                }
                else {
                    if (document.forms[0].fechaHasta.value == '') {
                        var fecha = '<%=fechaServidor%>';
                        document.forms[0].fechaHasta.value = fecha;
                    }
                    var fd = new Date(document.forms[0].fechaDesde.value.substring(6, 10), eval(document.forms[0].fechaDesde.value.substring(3, 5) - 1), document.forms[0].fechaDesde.value.substring(0, 2));
                    var fh = new Date(document.forms[0].fechaHasta.value.substring(6, 10), eval(document.forms[0].fechaHasta.value.substring(3, 5) - 1), document.forms[0].fechaHasta.value.substring(0, 2));

                    if (comparaFechas(fd, fh) == 1) {
                        jsp_alerta("A", '<%=desc.getDescripcion("msjFechasNV")%>');
                        r = 0;
                    }
                }
                var anoD = document.forms[0].fechaDesde.value.substring(6, 10);
                var anoH = document.forms[0].fechaHasta.value.substring(6, 10);


                return r;
            }

            // Funcion redefinida para procesar las pulsaciones sobre las tablas (evento onDbClick).
            function callFromTableTo(rowId, tableName) {
                  pleaseWait("on");   
                 if (tableName==tablaEntradasRechazadas.id){
                    document.forms[0].desdeEntradasRechazadas.value = 'S';
                    document.forms[0].desdePendientesFinalizar.value = 'N';
                    document.forms[0].ano.value = listaEntradasRechazadasOriginal[rowId][1]; //ejercicio
                    document.forms[0].numero.value = listaEntradasRechazadasOriginal[rowId][0]; //num Anotación
                 } else if (tableName==tablaPendientesFinalizar.id){
                    document.forms[0].desdePendientesFinalizar.value = 'S';
                    document.forms[0].desdeEntradasRechazadas.value = 'N';
                    document.forms[0].ano.value = listaPendientesOriginal[rowId][1]; //ejercicio
                    document.forms[0].numero.value = listaPendientesOriginal[rowId][0];// num Anotación 
                 }
                 
                 document.forms[0].posicionAnotacion.value = 1;
                 document.forms[0].opcion.value = "cancelar_anular";
                 document.forms[0].target ="mainFrame";
                 document.forms[0].action = URL_CARGA_DATOS;
                 document.forms[0].submit();
            }

            function checkKeysLocal(evento, tecla) {
                var teclaAuxiliar = "";
                if (window.event) {
                    evento = window.event;
                    teclaAuxiliar = evento.keyCode;
                } else {
                    teclaAuxiliar = evento.which;
                }
                //** Esta funcion se debe implementar en cada JSP para particularizar  **//
                //** las acciones a realizar de las distintas combinaciones de teclas  **//
                if (tecla == "Alt+S") {
                    pulsarSalir();
                }

                if (teclaAuxiliar == 9) {
                    if (IsCalendarVisible)
                        hideCalendar();
                }
                if (teclaAuxiliar == 1) {
                    if (IsCalendarVisible)
                        replegarCalendario(coordx, coordy);
                }

            }

        </script>
    </body>
</html>
