<!DOCTYPE html>
<%@page contentType="text/html; charset=iso-8859-1"	language="java" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@page import="es.altia.agora.business.registro.RegistroValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@page import="java.util.Vector"%>
<%@page import="java.lang.Integer"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>



<html:html>
    <head>

        <TITLE>::: REGISTRO ENTRADA SALIDA - LISTADO RELACION ANOTACIONES :::</TITLE>

        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />


        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            RegistroUsuarioValueObject regUsuarioVO = new RegistroUsuarioValueObject();
            int idioma=2;
            int apl=1;
            String css="";
            String funcion = "";
            String idSesion = session.getId();

            if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null)){
                usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
                regUsuarioVO = (RegistroUsuarioValueObject)session.getAttribute("registroUsuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                css= usuarioVO.getCss();
            }//if

            // #239565: recuperamos el valor de la propiedad
            MantAnotacionRegistroForm mantARForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
            String mostrarGenerarModelo = mantARForm.getMostrarGenerarModelo();

            String tipoAnotacion="E";
            if (session.getAttribute("tipoAnotacion")!=null)
                tipoAnotacion= (String) session.getAttribute("tipoAnotacion");
            String titPag;
            String tipo;
            String fech;
            String numOrden;

            if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion)) ){
                titPag ="tit_AnotE";
                tipo ="res_tipoEntrada";
                fech="res_fecE";
                numOrden="res_numOrdE";
            }else{
                titPag="tit_AnotS";
                tipo ="res_tipoSalida";
                fech="res_fecS";
                numOrden="res_numOrdS";
            }//if

            boolean mostrarDigitalizar = false;
            String servicioDigitalizacionActivo = (String) session.getAttribute("servicioDigitalizacionActivo");
            if((tipoAnotacion.equals("E") || tipoAnotacion.equals("Relacion_E"))
                    && servicioDigitalizacionActivo != null && servicioDigitalizacionActivo.equalsIgnoreCase("si")){
                mostrarDigitalizar = true;
            }

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String esConsultaSIR = "0";
            if(request.getAttribute("esConsultaSIR")!=null){
                esConsultaSIR = (String) request.getAttribute("esConsultaSIR");
            }
        %>

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

        <!-- Ficheros JavaScript -->

        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/altaRE.js'/>?<%=System.currentTimeMillis()%>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>

        <SCRIPT type="text/javascript">

        var esConsultaSIR = '<%= (request.getAttribute("esConsultaSIR") != null ? request.getAttribute("esConsultaSIR") : "0") %>';
        console.log("modo SIR =", esConsultaSIR);

        var dataTable = null;
            var columnaOrden;
            var tipoOrdenacion;
            var lista = new Array();
            // Array de booleanos, indica que anotaciones de 'lista' tienen observaciones.
            // Se construye al igual que 'lista' en ocultoCargarPaginaRelacionAnotaciones.jsp
            var listaObs = new Array();
            var listaOriginal = new Array();
            var listaP = new Array();
            var listaSel = new Array();
            var fila;
            var ultimo = false;
            var pagin;
            var asunto;
            var apellido1;
            var estado;
            var listaCampos=new Array();
            var idOrden;
            //vector que me indica que campos estan activos en las columnas para saber por cual ordenar.
            var listaOrden=new Array();
            var tipoActual = "";
            <%
                if ("E".equals(tipoAnotacion) || "Relacion_E".equals(tipoAnotacion)) {
            %>
            tipoActual = 'E';
            <%
                } else {
            %>
            tipoActual = 'S';
            <%
                }
            %>

            <% /* Recuperar el vector de anotaciones de la sesion. */
                // #262348: Se comprueba si hay anotaciones para establecer una variable que dará valor al atributo disabled del nuevo botón
                String activo = "";
                int numRelacionAnotaciones = ((Integer)session.getAttribute("NumRelacionAnotaciones")).intValue();
                if(numRelacionAnotaciones <= 0){
                    activo = "disabled";
                }
            %>

            var numRelacionAnotaciones = <%=numRelacionAnotaciones%>;
            /* Para navegacion */
            var lineasPagina   = 10;
            var paginaActual   = 1;
            var ventanaInforme;
            var cols =new Array();
            var anotacionesCheck =  new Array();
            var valoresAnotacionesCheck =  new Array();

            var idiom = <%=idioma%>;
            var aplic = <%=apl%>;

            var mostrarDigitalizar = <%=mostrarDigitalizar%>;
        

            function cargarInicio() {
                console.log("cargarInicio: start");
                // 1) Inicializar checks
                inicializaListasChecks();
                console.log("cargarInicio: checks inicializados");

                // 2) Recuperar orden si viene guardado
                <logic:notEmpty name="MantAnotacionRegistroForm" property="tipoOrden">
                tipoOrdenacion = <bean:write name="MantAnotacionRegistroForm" property="tipoOrden"/>;
                </logic:notEmpty>
                idOrden = '<bean:write name="MantAnotacionRegistroForm" property="columna"/>';
                console.log("cargarInicio: tipoOrden =", tipoOrdenacion, " idOrden =", idOrden);

                columnaOrden = 0;

                console.log("cargarInicio: columnaOrden set to 0");

                // Construcción dinámica de los campos/columnas
                var i = 1;
                console.log("cargarInicio: building listaCampos...");
                <logic:iterate id="campos" name="MantAnotacionRegistroForm" property="camposListados">
                <%-- Si es campo CHECK, lo ponemos en posición 0 --%>
                <logic:equal name="campos" property="nomCampo" value="CHECK">
                listaCampos[0] = [
                    '<bean:write name="campos" property="codCampo"/>',
                    '<bean:write name="campos" property="nomCampo"/>',
                    '<bean:write name="campos" property="tamanoCampo"/>',
                    '<bean:write name="campos" property="actCampo"/>',
                    '<bean:write name="campos" property="ordenCampo"/>'
                ];
                console.log("  listaCampos[0] (CHECK) =", listaCampos[0]);
                </logic:equal>
                <%-- Resto de campos, incrementamos índice --%>
                <logic:notEqual name="campos" property="nomCampo" value="CHECK">
                listaCampos[i] = [
                    '<bean:write name="campos" property="codCampo"/>',
                    '<bean:write name="campos" property="nomCampo"/>',
                    '<bean:write name="campos" property="tamanoCampo"/>',
                    '<bean:write name="campos" property="actCampo"/>',
                    '<bean:write name="campos" property="ordenCampo"/>'
                ];
                console.log("  listaCampos[" + i + "] =", listaCampos[i]);
                i++;
                </logic:notEqual>
                </logic:iterate>
                console.log("cargarInicio: listaCampos built, length =", listaCampos.length);

                // Variables de texto para columnas especiales según entrada/salida
                var cont = 0;
                var destino = "";
                var salida = "";
                <% if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion)) ){ %>
                destino = "<%= descriptor.getDescripcion("rotulo_uniDestino") %>";
                salida  = "<%= descriptor.getDescripcion("rotulo_entrada") %>";
                <% } else { %>
                destino = "<%= descriptor.getDescripcion("rotulo_uniOrigen") %>";
                salida  = "<%= descriptor.getDescripcion("rotulo_salida") %>";
                <% } %>
                console.log("cargarInicio: destino =", destino, ", salida =", salida);

                // Construcción de la configuración de columnas
                cont = 0;
                cols = [
                    { title: "<i class='fa fa-check' aria-hidden='true'></i>", sWidth: parseInt(listaCampos[cont++][2]) + '%', sClass: "centrado"  },
                    { title: "Estado",                       sWidth: parseInt(listaCampos[cont++][2]) + '%', sClass: "centrado" },
                    { title: "<%= descriptor.getDescripcion("rotulo_proc") %>",  sWidth: parseInt(listaCampos[cont++][2]) + '%', sClass: "centrado" },
                    { title: "<%= descriptor.getDescripcion("rotulo_exp") %>",   sWidth: parseInt(listaCampos[cont++][2]) + '%', sClass: "centrado" },
                    { title: "<%= descriptor.getDescripcion("rotulo_usu") %>",   sWidth: parseInt(listaCampos[cont++][2]) + '%', sClass: "centrado" },
                    { title: "<%= descriptor.getDescripcion("rotulo_numAnot") %>", sWidth: parseInt(listaCampos[cont++][2]) + '%', sClass: "centrado" },
                    { title: "<%= descriptor.getDescripcion("rotulo_tipo") %>",  sWidth: parseInt(listaCampos[cont++][2]) + '%', sClass: "centrado" },
                    { title: "<%= descriptor.getDescripcion("rotulo_fechaPres") %>", sWidth: parseInt(listaCampos[cont++][2]) + '%', sClass: "centrado" },
                    { title: "<%= descriptor.getDescripcion("rotulo_fechaGrab") %>", sWidth: parseInt(listaCampos[cont++][2]) + '%', sClass: "centrado" },
                    <% if (("E".equals(tipoAnotacion)) || ("Relacion_E".equals(tipoAnotacion))){ %>
                    { title: "<%= descriptor.getDescripcion("rotulo_remite") %>",   sWidth: parseInt(listaCampos[cont++][2]) + '%', sClass: "centrado" },
                    <% } else { %>
                    { title: "<%= descriptor.getDescripcion("rotuloDestinatario") %>", sWidth: parseInt(listaCampos[cont++][2]) + '%', sClass: "centrado" },
                    <% } %>
                    { title: "<%= descriptor.getDescripcion("rotulo_asunto") %>", sWidth: parseInt(listaCampos[cont++][2]) + '%', sClass: "centrado" },
                    { title: destino, sWidth: parseInt(listaCampos[cont++][2]) + '%', sClass: "centrado" },
                    { title: salida,  sWidth: parseInt(listaCampos[cont++][2]) + '%', sClass: "centrado" }
                ];
                console.log("cargarInicio: cols built, cols.length =", cols.length);

                // 6) Aplicar visibilidad y conservar orden
                for (cont = 0; cont < listaCampos.length; cont++) {
                    if (listaCampos[cont][3] === 'NO') {
                        cols[cont].sClass = "estiloOculto";
                    } else {
                        listaOrden[cont] = listaCampos[cont][4];
                        if (listaCampos[cont][4] == idOrden) {
                            columnaOrden = cont;
                        }
                    }
                }
                console.log("cargarInicio: applied visibility & order, columnaOrden =", columnaOrden);

                paginaActual = 1;

                // 7) Rama SIR vs normal
                <% if ("1".equals(esConsultaSIR)) { %>
                console.log("cargarInicio: esConsultaSIR, including ocultoCargarPaginaAnotaciones.jsp");
                cargarPaginaSIR();
                <% } else { %>
                console.log("cargarInicio: normal load, calling cargaPagina(", paginaActual, ")");
                cargarComboFilasPagina();
                cargaPagina(paginaActual);
                <% } %>
            }


            function enlaces(){
                numeroPaginas = Math.ceil(numRelacionAnotaciones /lineasPagina);
                document.getElementById('enlace').innerHTML = enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>',paginaActual,numeroPaginas,'cargaPagina','<%=descriptor.getDescripcion("numResultados")%>', numRelacionAnotaciones);
            }//enlaces

        function cargarPaginaSIR() {
            // 1) Ver qué hay ahora en el hidden
            console.log("cargarPaginaSIR: antes opcion =", document.getElementById('opcion').value);

            // 2) Asigna el valor SIR
            // Logs de filtros SIR para depuración
            var idSIR = document.getElementById('identificadorRegistroSIRValue');
            var estadoSIR = document.getElementById('codEstadoSIR');
            var unidadSIR = document.getElementById('codigoUnidadDestinoSIRHidden');

            if (idSIR) console.log('cargarPaginaSIR: identificadorRegistroSIRValue =', idSIR.value);
            if (estadoSIR) console.log('cargarPaginaSIR: codEstadoSIR =', estadoSIR.value);
            if (unidadSIR) console.log('cargarPaginaSIR: codigoUnidadDestinoSIRHidden =', unidadSIR.value);

            // Cambiar la opción a consultaOcultaEnSIR
            document.getElementById('opcion').value = "consultaOcultaEnSIR";
            console.log("cargarPaginaSIR: después opcion =", document.getElementById('opcion').value);

            // 3) Envía al iframe
            document.forms[0].target = "oculto";
            document.forms[0].action = "<%=request.getContextPath()%>/MantAnotacionRegistro.do";
            document.forms[0].submit();
        }


        function cargaPagina(numeroPagina){
                // La paginacion en servidor solo la hacemos si no estamos consultando en sir
                <% if (!"1".equals(esConsultaSIR)) { %>
                    lista = new Array();
                    listaOriginal= new Array();
                    document.forms[0].paginaListado.value = numeroPagina;
                    document.forms[0].numLineasPaginaListado.value = lineasPagina;
                    //paso el tipo de ordenacion para que se mantenga igual aunque cambie de pagina
                    document.forms[0].numRelacionAnotaciones.value = numRelacionAnotaciones;
                    document.forms[0].columna.value = idOrden;
                    document.forms[0].tipoOrden.value =tipoOrdenacion;
                    document.forms[0].procedoRelaciones.value=" ";
                    document.forms[0].opcion.value="cargar_pagina";
                    console.log("cargaPagina: Enviando el formulario con la acción 'cargar_pagina'");
                    document.forms[0].target="oculto";
                    document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                    document.forms[0].submit();
                <% } %>
            }//cargaPagina

            function inicializaLista(numeroPagina){
                console.log("Se ejecuta redirecciona");
                paginaActual = numeroPagina;
                listaOriginal = listaSel.slice();
                console.log("inicializaLista: listaSel inicial", JSON.stringify(listaSel));

                var paginaCliente = false;
                if (esConsultaSIR == '1') {
                    paginaCliente = true;
                }

                if (esConsultaSIR) {
                    for (var i = 0; i < listaSel.length; i++) {
                        var estadoSIR = listaSel[i][24] || '';
                        var fechaSIR = listaSel[i][25];
                        if (estadoSIR) {
                            listaSel[i][1] = estadoSIR + '<br><span style="font-size:80%;color:#666;">' + fechaSIR + '</span>';
                        }
                        var numAnotacion = listaSel[i][5] || '';
                        var identificadorSIR = listaSel[i][26] || '';
                        if (identificadorSIR) {
                            listaSel[i][5] = numAnotacion + '<br><span style="font-size:80%;color:#666;">' + identificadorSIR + '</span>';
                        }
                        if (listaSel[i].length > cols.length) {
                            listaSel[i] = listaSel[i].slice(0, cols.length);
                        }
                    }
                    paginaCliente = true;
                }

                // ----------- CHEQUEO PREVENTIVO -----------
                if (listaSel.length > 0) {
                    console.log("cols.length:", cols.length);
                    console.log("listaSel[0].length:", listaSel[0].length);
                    if (cols.length != listaSel[0].length) {
                        alert("ERROR: Las columnas de la tabla (" + cols.length + ") y los datos (" + listaSel[0].length + ") no coinciden.");
                        return;
                    }
                }
                // ----------- FIN CHEQUEO ------------------

                if (dataTable!=null)
                    dataTable.destroy();

                dataTable = $("#tablaAnotaciones").DataTable({
                    data: listaSel,
                    aoColumns: cols,
                    "sort" : false,
                    "info" : paginaCliente,
                    "paging" : paginaCliente,
                    "pageLength": lineasPagina,
                    "autoWidth": false,
                    "language": {
                        "search": "<%=descriptor.getDescripcion("buscar")%>",
                        "previous": "<%=descriptor.getDescripcion("anterior")%>",
                        "next": "<%=descriptor.getDescripcion("siguiente")%>",
                        "lengthMenu": "<%=descriptor.getDescripcion("mosFilasPag")%>",
                        "zeroRecords": "<%=descriptor.getDescripcion("msgNoResultBusq")%>",
                        "info": "<%=descriptor.getDescripcion("mosPagDePags")%>",
                        "infoEmpty": "<%=descriptor.getDescripcion("noRegDisp")%>",
                        "infoFiltered": "<%=descriptor.getDescripcion("filtrDeTotal")%>"
                    }
                });

                // Solo una vez después de crear la tabla
                if (esConsultaSIR == '1') {
                    $('#tablaAnotaciones thead tr th').eq(1).html("Estado SIR");
                }

                for (var i = 0; i < listaSel.length; i++) {
                    $("#tablaAnotaciones tbody tr:nth-child(" + (i+1) + ")")
                        .attr("id", i)
                        .attr("data-ano", listaOriginal[i][0])
                        .attr("data-numero", listaOriginal[i][1]);
                }


                $("#tablaAnotaciones thead tr th").each(function (index,value) {
                    if (listaOrden[index]==-1){
                        value.onclick = function() {jsp_alerta('A','<%=descriptor.getDescripcion("msjNoOrdenacion")%>');}
                    } else {
                        $("#tablaAnotaciones thead tr th:nth-child("+(index+1)+")").removeClass("sorting sorting_asc sorting_desc ");
                        if (columnaOrden!=index)
                            $("#tablaAnotaciones thead tr th:nth-child("+(index+1)+")").addClass("sorting");
                        else if (tipoOrdenacion=="true")
                            $("#tablaAnotaciones thead tr th:nth-child("+(index+1)+")").addClass("sorting_asc");
                        else
                            $("#tablaAnotaciones thead tr th:nth-child("+(index+1)+")").addClass("sorting_desc");

                        value.onclick = function() {
                            $("#tablaAnotaciones thead tr th").off("click");
                            if (columnaOrden==index){
                                tipoOrdenacion = (tipoOrdenacion=="true"?"false":"true");
                            }else {
                                tipoOrdenacion = false;
                            }
                            idOrden = listaOrden[index];
                            columnaOrden=index;
                            document.forms[0].paginaListado.value = 1;
                            document.forms[0].numLineasPaginaListado.value = lineasPagina;
                            document.forms[0].columna.value = listaOrden[index];
                            document.forms[0].tipoOrden.value =tipoOrdenacion;
                            document.forms[0].opcion.value = "cargar_pagina";
                            document.forms[0].target = "oculto";
                            document.forms[0].action = "<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                            document.forms[0].submit();
                        }
                    }
                });

                $("#tablaAnotaciones tbody tr").on("dblclick", function() {
                    seleccionRegistro(this.id);
                });
                if (esConsultaSIR != '1') {
                    enlaces();
                }
                actualizarChecksPagina();
                remarcarFilasObservacion();
                pleaseWait("off");
            }


            //Se remarcan las anotaciones que tengan observaciones
            function remarcarFilasObservacion() {

                if (listaAnotacionesCompleta) {
                    for (var i = 0; i < listaAnotacionesCompleta.length; i++) {
                        if (listaAnotacionesCompleta[i] && listaAnotacionesCompleta[i][16]) {
                            var row = dataTable.row(i).node();
                            $(row).addClass("highlight");
                        }
                    }
                }
            }

            function seleccionRegistro(indice) {
                console.log("[seleccionRegistro] INICIO. indice=", indice, "paginaActual=", paginaActual);

                var ind = parseInt(indice);
                if (isNaN(ind) || ind < 0 || ind >= listaOriginal.length) {
                    console.error("[seleccionRegistro] Índice fuera de rango: ind=", ind, "listaOriginal.length=", listaOriginal.length);
                    alert("Error interno: la fila seleccionada no existe o no es válida. Contacta con soporte.");
                    return;
                }
                if (!listaOriginal[ind]) {
                    console.error("[seleccionRegistro] listaOriginal["+ind+"] es undefined:", listaOriginal);
                    alert("Error interno: fila seleccionada vacía. Contacta soporte.");
                    return;
                }

                var fila = $("#tablaAnotaciones tbody tr[id="+indice+"]");
                var ano = fila.data("ano");
                var numero = fila.data("numero");
                console.log("[seleccionRegistro] Datos fila:", listaOriginal[ind]);
                console.log("[seleccionRegistro] ano:", ano, "numero:", numero);

                // Rellenar campos del form
                document.forms[0].posicionAnotacion.value = (paginaActual - 1) * document.forms[0].numLineasPaginaListado.value + ind + 1;
                document.forms[0].ano.value    = ano;
                document.forms[0].numero.value = numero;
                document.forms[0].opcion.value = "cancelar_anular";
                document.forms[0].target       = "mainFrame";
                document.forms[0].action       = "<%=request.getContextPath()%>/MantAnotacionRegistro.do";

                // ---  Log para asegurarse de que todo está ok antes del submit
                console.log("[seleccionRegistro] Form justo antes de rellenar SIR:", document.forms[0]);


                const idSIRHidden   = document.getElementById('identificadorRegistroSIRValue');
                const estadoSIR     = document.getElementById('codEstadoSIR');
                const unidadDestSIR = document.getElementById('codigoUnidadDestinoSIRHidden');

                /* Formato que espera el back?end, con ?;E;? final */
                if (idSIRHidden) {
                    idSIRHidden.value = `${ano};${numero};E;`;
                    console.log("[seleccionRegistro] identificadorRegistroSIRValue =", idSIRHidden.value);
                }

                /* Resetea los otros filtros para evitar heredar valores antiguos */
                if (estadoSIR)     estadoSIR.value     = '';
                if (unidadDestSIR) unidadDestSIR.value = '';
                /* ??????????????????????????????????????????????????????????????
                   ?  2· ENVÍO DEL FORMULARIO                                   ?
                   ?????????????????????????????????????????????????????????????? */
                document.forms[0].submit();

            } //seleccionRegistro


            function pulsarSalirConsultar() {
                <% if ("Relacion_E".equals(tipoAnotacion) || "E".equals(tipoAnotacion)){ %>
                document.forms[0].opcion.value="Relacion_E";
                <% } else { %>
                document.forms[0].opcion.value="Relacion_S";
                <% } %>
                document.forms[0].target="mainFrame";
                document.forms[0].action="<%=request.getContextPath()%>/MantAnotacionRegistro.do";
                document.forms[0].submit();
            }//pulsarSalirConsultar

            function inicializaListasChecks(){
                for(var index=0; index<numRelacionAnotaciones; index++){
                    anotacionesCheck[index] = 0;
                    valoresAnotacionesCheck[index] = 0;
                }
            }


            function actualizarChecksPagina(){
                var pagina = (paginaActual-1)*lineasPagina;
                var checksPagina = $('.checkLinea');

                $.each(checksPagina,function(index,value){
                    if(anotacionesCheck[pagina+index]==1) {
                        $(value).prop("checked", "checked");
                    } else {
                        $(value).prop("checked", "");
                    }
                });
            }

            function pulsarCheck(){
                var pagina = (paginaActual-1)*lineasPagina;
                var checksPagina = $('.checkLinea');

                $.each(checksPagina,function(index,value){
                    var valor = $(value).val();
                    console.log("Valor recogido en check:", valor); // Añade esto
                    if( $(value).is(':checked')) {
                        anotacionesCheck[pagina+index] = 1;
                        valoresAnotacionesCheck[pagina+index] = valor;
                    } else {
                        anotacionesCheck[pagina+index] = 0;
                        valoresAnotacionesCheck[pagina+index] = 0;
                    }
                });
            }


            function pulsarPrevioImprimir(opcion){
                var salida        = "";
                var separador = '§¥';
                var contador   = 0;

                for(i=0;i<valoresAnotacionesCheck.length;i++){
                    if(valoresAnotacionesCheck[i]!=0){

                        salida = salida + valoresAnotacionesCheck[i];
                        contador++;

                        if(valoresAnotacionesCheck.length-i>1)
                            salida = salida + separador
                    }

                }// for

                if(contador>0 ){
                    if(opcion==0){
                        pulsarImprimir(salida);
                    }else if(opcion==1){
                        pulsarExportarCSV(salida);
                    }else if(opcion==2){
                        pulsarImprimirInforme('justificante_consulta',salida);
                    }
                }else if(contador==0){
                    // El usuario no ha seleccionado ninguna anotacion
                    if(opcion==0){
                        if(jsp_alerta("C","<%=descriptor.getDescripcion("msgErrorAnotacionSel")%>"+ " <%=descriptor.getDescripcion("msgImprimirTodoListado")%>")){
                            pulsarImprimir("");
                        }
                    }else if(opcion==1){
                        if(jsp_alerta("C","<%=descriptor.getDescripcion("msgErrorAnotacionSel")%>"+ " <%=descriptor.getDescripcion("msgExportarTodoLista")%>")){
                            pulsarExportarCSV("");
                        }
                    }else if(opcion==2){
                        <%--if(jsp_alerta("C","<%=descriptor.getDescripcion("msgErrorAnotacionSel")%>"+ " <%=descriptor.getDescripcion("msgImprimirTodoListado")%>")){
                           pulsarImprimirInforme('justificante_consulta',"");
                        }--%>
                        jsp_alerta("A","Debe seleccionar al menos una anotación");

                    }
                }
            }

            function pulsarImprimir(anotaciones){
                pleaseWait('on');
                <% if ("Relacion_E".equals(tipoAnotacion) || "E".equals(tipoAnotacion)){ %>
                document.forms[0].tipo.value="E";
                <% } else { %>
                document.forms[0].tipo.value="S";
                <% } %>
                document.forms[0].listaAnotaciones.value=anotaciones;
                document.forms[0].opcion.value="imprimirListadoAsientos";
                document.forms[0].target="oculto";
                document.forms[0].action="<%=request.getContextPath()%>/InformesRegistro.do";
                document.forms[0].submit();
            }//pulsarImprimir

            function abrirInforme(nombre){
                // A otra página que contiene el fichero PDF.
                if (!(nombre =='')){
                    // PDFS NUEVA SITUACION
                    var sourc = "<%=request.getContextPath()%>/jsp/verPdf.jsp?opcion=null&nombre="+nombre;
                    ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp;jsessionid=<%=idSesion%>?source="+sourc,'ventanaInforme','width=800px,height=550px,status='+ '<%=statusBar%>' + ',toolbar=no,resizable=yes');
                    ventanaInforme.focus();
                    // FIN PDFS NUEVA SITUACION
                }else{
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjNoPDF")%>');
                }//if
            }//abrirInforme

            function cargarComboFilasPagina(){
                var selectorDeFilas = '<select name="filasPagina" id="filasPagina" class ="" onchange="javascript:cambiarFilasPagina();">' +
                    '<option value="10">10</option>' +
                    '<option value="25">25</option>' +
                    '<option value="50">50</option>' +
                    '<option value="100">100</option>' +
                    '</select>';
                document.getElementById('contSelectPax').innerHTML = '<%=descriptor.getDescripcion("mosFilasPag")%>'.replace('_MENU_',selectorDeFilas);
                document.getElementById('filasPagina').value= lineasPagina;
            }

            function cambiarFilasPagina(){
                lineasPagina = document.getElementById('filasPagina').value;

                cargaPagina(1);
            }

            function pulsarImprimirInforme(tipoInforme, anotaciones){
                <%
                    String anoReg = "0";
                    String tipoReg = "";
                    if(request.getAttribute("anoReg")!=null){
                        anoReg = (String) request.getAttribute("anoReg");
                    }
                    if ("Relacion_E".equals(tipoAnotacion) || "E".equals(tipoAnotacion)){
                        tipoReg = "E";
                    } else {
                        tipoReg = "S";
                    }
                %>
                document.forms[0].tipo.value='<%=tipoReg%>';
                document.forms[0].listaAnotaciones.value=anotaciones;
                $('#ejercicioAnotacion').val('<%=anoReg%>');

                if(tipoInforme == 'peticion_consulta')
                    recuperarCodigoOfiReg('peticion_consulta','listadoAnotaciones');
                else
                    pulsarJustificanteEntrada();
            }

            function pulsarExportarCSV(anotaciones){
                pleaseWait('on');
                <% if ("Relacion_E".equals(tipoAnotacion) || "E".equals(tipoAnotacion)){ %>
                document.forms[0].tipo.value="E";
                <% } else { %>
                document.forms[0].tipo.value="S";
                <% } %>
                document.forms[0].listaAnotaciones.value=anotaciones;
                document.forms[0].opcion.value="exportarCSV";
                document.forms[0].target="oculto";
                document.forms[0].action="<%=request.getContextPath()%>/InformesRegistro.do";
                document.forms[0].submit();
            }
            function abrirInformeCSV(nombre,dir) {
                pleaseWait('off');
                var source = "<c:url value='/jsp/verPdf.jsp?opcion=null&tipoFichero=csv&nombre='/>" + nombre+"&dir="+dir;
                var nombreVentana = (top.name == 'ventana' ? 'ventana2' : 'ventana');
                ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp;jsessionid=<%=idSesion%>?source=" + source, nombreVentana, 'width=800px,height=550px,status=' + '<%=statusBar%>' + ',toolbar=no');
                ventanaInforme.focus();
            }

            function mostrarError(codError){
                if(codError="1"){
                    jsp_alerta('A','<%=descriptor.getDescripcion("noImprimirJustificanteListado")%>');
                }
            }

        </SCRIPT>

    </head>

    <BODY class="bandaBody" onload="javascript:{
         cargarInicio();}">

    <jsp:include page="/jsp/hidepage.jsp" flush="true">
        <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
    </jsp:include>

    <html:form action="/MantAnotacionRegistro.do" target="_self">

        <html:hidden property="opcion" styleId="opcion" value=""/>
        <html:hidden  property="ano" value=""/>
        <html:hidden  property="ano" styleId="ano" value=""/>
        <html:hidden  property="numero" value=""/>
        <html:hidden property="ejercicioAnotacion" styleId="ejercicioAnotacion" />
        <html:hidden property="numeroAnotacion" />
        <html:hidden property="posicionAnotacion" />
        <html:hidden property="paginaListado" value="1"/>
        <html:hidden property="numLineasPaginaListado" value="10"/>
        <html:hidden property="columna" value=""/>
        <html:hidden property="tipoOrden" value=""/>
        <input type="hidden" name="tipo">
        <input type="hidden" name="procedoRelaciones">
        <input type="hidden" name="numRelacionAnotaciones">
        <!-- filtros SIR ocultos -->
        <html:hidden
                name="MantAnotacionRegistroForm"
                property="identificadorRegistroSIR"
                styleId="identificadorRegistroSIRValue"/>
        <html:hidden
                name="MantAnotacionRegistroForm"
                property="codEstadoSIR"
                styleId="codEstadoSIR"/>
        <html:hidden
                name="MantAnotacionRegistroForm"
                property="codigoUnidadDestinoSIR"
                styleId="codigoUnidadDestinoSIRHidden"/>


        <!-- #262348 -->
        <input type="hidden" name="codAplicacion" value="">
        <input type="hidden" name="idiomaCuneus" >
        <!-- #262531 -->
        <input type="hidden" name="listaAnotaciones" id="listaAnotaciones" value="">

        <% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion) ){ %>
        <div class="txttitblancoder"><%=descriptor.getDescripcion(titPag)%></div>
        <% } else {%>
        <div class="txttitblanco"><%=descriptor.getDescripcion(titPag)%></div>
        <% } %>
        <div class="contenidoPantalla">
            <table style="width:100%">
                <tr>
                    <td>
                        <div class="dataTables_wrapper paxinacionDataTables">
                            <label id="contSelectPax"></label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table id="tablaAnotaciones" class="xTabla compact tablaDatos" width="99%"></table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="enlace" class="dataTables_wrapper"></div>
                    </td>
                </tr>
            </table>
        </div>
        <div class="capaFooter">
            <div class="botoneraPrincipal">
                <input type="button" title='<%=descriptor.getDescripcion("toolTip_bImprimir")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbImprimir")%>' name="cmdImprimir" onClick="pulsarPrevioImprimir(0);return false;">
                <% if ("S".equals(tipoAnotacion) || "Relacion_S".equals(tipoAnotacion) ){ %>
                <logic:equal name="MantAnotacionRegistroForm" property="mostrarGenerarModelo" value="si">
                    <!-- #262348: Se añade el botón para registros de salidas si el properties lo permite -->
                    <input type="button" id="botonPeticionResp" class="botonMasLargo" title='<%=descriptor.getDescripcion("altModPetRpta")%>' alt='<%=descriptor.getDescripcion("altModPetRpta")%>' value="Imprimir mod. pet. resp." onClick="pulsarImprimirInforme('peticion_consulta');return false;" <%=activo%>/>
                </logic:equal>
                <logic:equal name="MantAnotacionRegistroForm" property="generarJustificanteConsulta" value="si">
                    <!-- #288821: Se añade el botón para generar el justificante de registros de salida si el properties lo permite -->
                    <input type="button" id="botonJusticanteDesdeConsulta" class="botonLargo" title='<%=descriptor.getDescripcion("altJustificSalida")%>' alt='<%=descriptor.getDescripcion("altJustificSalida")%>' value='<%=descriptor.getDescripcion("etiq_btnImprJustif")%>' onClick="pulsarPrevioImprimir(2);return false;" <%=activo%>/>
                </logic:equal>
                <% } else {%>
                <logic:equal name="MantAnotacionRegistroForm" property="generarJustificanteConsulta" value="si">
                    <!-- #288821: Se añade el botón para generar el justificante de registros de entrada si el properties lo permite -->
                    <input type="button" id="botonJusticanteDesdeConsulta" class="botonLargo" title='<%=descriptor.getDescripcion("altJustificEntrada")%>' alt='<%=descriptor.getDescripcion("altJustificEntrada")%>' value='<%=descriptor.getDescripcion("etiq_btnImprJustif")%>' onClick="pulsarPrevioImprimir(2);return false;" <%=activo%>/>
                </logic:equal>
                <% } %>
                <input type="button" title='<%=descriptor.getDescripcion("gbExportarCsv")%>'  class="botonGeneral" value='<%=descriptor.getDescripcion("gbExportarCsv")%>' name="cmdExportarCSV" onclick="pulsarPrevioImprimir(1); return false;">
                <input type="button" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdSalir" onClick="pulsarSalirConsultar();return false;">


            </div>
        </div>
    </html:form>
    </body>
</html:html>
