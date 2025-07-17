function ViewListaAccesoModulos() {
    /* Constantes para el combo de fechas */
    var COMBO_FECHA_NADA = '-1';
    var COMBO_FECHA_ENTRE = '5';
    
    /* Datos */
    var textos = null;
    var listaOrganizaciones = null;
    var listaAplicaciones = null;
    var tablaAccesosModulo = null;

    /* Campos del formulario de la vista jsp */
    var comboOrganizacion = null;
    var comboAplicacion = null;
    var inputLoginUsuario = null;
    var inputNombreUsuario = null;
    var selectTipoBusquedaFecha = null;
    var inputFechaInicio = null;
    var inputFechaFin = null;

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    this.inicializar = function (opciones) {
        textos = opciones.textos;
        listaOrganizaciones = opciones.listaOrganizaciones;
        listaAplicaciones = opciones.listaAplicaciones;
    
        inputLoginUsuario = document.getElementsByName('loginUsuario')[0];
        inputNombreUsuario = document.getElementsByName('nombreUsuario')[0];
        inputFechaInicio = document.getElementsByName('fechaInicio')[0];
        inputFechaFin = document.getElementsByName('fechaFin')[0];
        selectTipoBusquedaFecha = document.getElementsByName('valorOperCriterioFecha')[0];

        crearCombos();
        crearTablaAccesosModulos();
        rellenarTablaAccesosModulos(new Array());

        document.onmouseup = checkKeys;
        window.focus();
        pleaseWait('off');
    };

    function crearCombos() {
        var listaCodOrganizacion = new Array();
        var listaDescOrganizacion = new Array();
        var listaCodAplicacion = new Array();
        var listaDescAplicacion = new Array();

        comboOrganizacion = new Combo("Organizacion");
        comboAplicacion = new Combo("Aplicacion");

        for (var i = 0; i < listaOrganizaciones.length; i++) {
            listaCodOrganizacion[i] = listaOrganizaciones[i].tabla.codigo;
            listaDescOrganizacion[i] = listaOrganizaciones[i].tabla.descripcion;
        }

        for (var i = 0; i < listaAplicaciones.length; i++) {
            listaCodAplicacion[i] = listaAplicaciones[i].tabla.codigo;
            listaDescAplicacion[i] = listaAplicaciones[i].tabla.descripcion;
        }

        comboOrganizacion.addItems(listaCodOrganizacion, listaDescOrganizacion);
        comboAplicacion.addItems(listaCodAplicacion, listaDescAplicacion);
    };

    function crearTablaAccesosModulos() {
        tablaAccesosModulo = new Tabla(true
                , textos.buscar
                , textos.anterior
                , textos.siguiente
                , textos.mosFilasPag
                , textos.msgNoResultBusq
                , textos.mosPagDePags
                , textos.noRegDisp
                , textos.filtrDeTotal
                , textos.primero
                , textos.ultimo
                , document.getElementById('tabla'));

        tablaAccesosModulo.addColumna('240', 'center', textos.tblAccesoColMovimiento);
        tablaAccesosModulo.addColumna('240', 'center', textos.tblAccesoColOrganizacion);
        tablaAccesosModulo.addColumna('240', 'center', textos.tblAccesoColLogin);
        tablaAccesosModulo.addColumna('240', 'center', textos.tblAccesoColUsuario);
        tablaAccesosModulo.addColumna('100', 'center', textos.tblAccesoColFechaHora);

        tablaAccesosModulo.displayCabecera = true;
    };

    function cargaTablaAccesosModulos(params) {
        pleaseWait('on');
        try {
            var criterio = params || {};
            criterio.opcion = 'cargarTablaAccesosModulos';

            $.ajax({
                url: APP_CONTEXT_PATH + '/administracion/mantenimiento/cargaInfoAccesoUsuarios.do',
                type: 'POST',
                async: true,
                data: criterio,
                success: successCargaTablaAccesosModulos,
                error: errorCargaTablaAccesosModulos
            });
        } catch (Err) {
            errorCargaTablaAccesosModulos();
        }
    };

    function successCargaTablaAccesosModulos(ajaxResult) {
        var accesos = JSON.parse(ajaxResult);
        var datosTabla = new Array();

        if (accesos) {
            var nombreAplicacion = '';
            var nombreOrganizacion = '';
            var loginUsuario = '';
            var nombreUsuario = '';
            var fechaHora = '';

            for (var i = 0; i < accesos.length; i++) {
                if (accesos[i].idAplicacion === 0) {
                    nombreAplicacion = 'LOGIN';
                } else {
                    nombreAplicacion = (accesos[i].nombreAplicacion) ? textos.tblAccesoA.toUpperCase() + ' ' + accesos[i].nombreAplicacion : '';
                }
                nombreOrganizacion = accesos[i].nombreOrganizacion || '';
                loginUsuario = accesos[i].loginUsuario || '';
                nombreUsuario = accesos[i].nombreUsuario || '';
                fechaHora = accesos[i].fechaHora || '';
                datosTabla[i] = [nombreAplicacion, nombreOrganizacion, loginUsuario, nombreUsuario, fechaHora];
            }
        }

        rellenarTablaAccesosModulos(datosTabla);
        pleaseWait('off');
    };

    function errorCargaTablaAccesosModulos() {
        pleaseWait('off');
        jsp_alerta('A', textos.errCargarAccesosModulos);
    };

    function rellenarTablaAccesosModulos(datos) {
        tablaAccesosModulo.lineas = datos;
        refresca(tablaAccesosModulo);
    };

    function refresca(tabla) {
        tabla.displayTabla();
    };

    // FUNCIONES DE LIMPIEZA DE CAMPOS
    function limpiarInputs() {
        inputLoginUsuario.value = '';
        inputNombreUsuario.value = '';
        inputFechaInicio.value = '';
        inputFechaFin.value = '';

        selectTipoBusquedaFecha.value = "-1";
        comboOrganizacion.clearSelected();
        comboAplicacion.clearSelected();
    };

    // FUNCIONES DE PULSACION DE BOTONES
    this.pulsarBuscar = function () {
        if (comprobarFechaBusqueda()) {
            var criterio = {
                codAplicacion: document.forms[0].codAplicacion.value || null,
                codOrganizacion: document.forms[0].codOrganizacion.value || null,
                loginUsuario: inputLoginUsuario.value || null,
                nombreUsuario: inputNombreUsuario.value || null,
                opcionFecha: selectTipoBusquedaFecha.value,
                fechaInicio: inputFechaInicio.value || null,
                fechaFin: inputFechaFin.value || null
            };

            cargaTablaAccesosModulos(criterio);
        }
    };

    this.pulsarLimpiarBuscar = function() {
        limpiarInputs();
        this.cambiarVisibilidadInputFechas();
        tablaAccesosModulo.selectLinea(-1);
    };
    
    this.pulsarSalir = function() {
        document.forms[0].target = "mainFrame";
        document.forms[0].action = APP_CONTEXT_PATH + '/jsp/administracion/presentacionADM.jsp';
        document.forms[0].submit();
    };

    /* Funciones para la comprobacion de las fechas */
    this.cambiarVisibilidadInputFechas = function() {
        var divPrimeraFecha = $('#primerCriterioFecha');
        var divSegundaFecha = $('#segundoCriterioFecha');
        var opcion = selectTipoBusquedaFecha.value;

        if (opcion === COMBO_FECHA_NADA) { // No filtrar por fecha
            divPrimeraFecha.addClass('hideVisibility');
            divSegundaFecha.addClass('hideVisibility');
            inputFechaInicio.value = '';
            inputFechaFin.value = '';
        } else if (opcion === COMBO_FECHA_ENTRE) { // Entre dos fechas
            divPrimeraFecha.removeClass('hideVisibility');
            divSegundaFecha.removeClass('hideVisibility');
        } else {
            divPrimeraFecha.removeClass('hideVisibility');
            divSegundaFecha.addClass('hideVisibility');
            inputFechaFin.value = '';
        }
    };

    this.mostrarCalFechaInicio = function(evento) {
        if (window.event)
            evento = window.event;
        if (document.getElementById("calFechaInicio").className.indexOf("fa-calendar") !== -1)
            showCalendar('forms[0]', 'fechaInicio', null, null, null, '', 'calFechaInicio', '', null, null, null, null, null, null, null, null, evento);

        return false;
    };

    this.mostrarCalFechaFin = function(evento) {
        if (window.event)
            evento = window.event;
        if (document.getElementById("calFechaFin").className.indexOf("fa-calendar") !== -1)
            showCalendar('forms[0]', 'fechaFin', null, null, null, '', 'calFechaFin', '', null, null, null, null, null, null, null, null, evento);

        return false;
    };

    function comprobarFechaBusqueda() {
        var operador = selectTipoBusquedaFecha.value;
        var fechaInicio = inputFechaInicio.value;
        var fechaFin = inputFechaFin.value;
        var valida = true;

        if (operador !== COMBO_FECHA_NADA
                && operador !== COMBO_FECHA_ENTRE
                && (fechaInicio === null || fechaInicio === '')) {
            jsp_alerta('A', textos.msjObligTodos);
            valida = false;
        } else if (operador === COMBO_FECHA_ENTRE
                && ((fechaInicio.length > 0 && fechaFin.length === 0)
                        || (fechaInicio.length === 0 && fechaFin.length > 0)
                        || (fechaInicio.length === 0 && fechaFin.length === 0))) {
            jsp_alerta('A', textos.msjObligTodos);
            valida = false;
        } else if (operador === COMBO_FECHA_ENTRE
                && (fechaInicio.length > 0 && fechaFin.length > 0
                        && !esFechaMenorIgualQue(fechaInicio, fechaFin))) {
            jsp_alerta('A', textos.msjCriterioFechaInicioFin);
            valida = false;
        }

        return valida;
    };

    /* Funciones para teclas de acceso rapido */
     this.checkKeysLocal = function(evento, tecla) {
        var teclaAuxiliar = evento.which;
        var coordx = evento.clientX;
        var coordy = evento.clientY;

        if (teclaAuxiliar === 1) { // CLICK
            if (comboOrganizacion.base.style.display != "none" && isClickOutCombo(comboOrganizacion, coordx, coordy))
                comboOrganizacion.ocultar();
            if (comboAplicacion.base.style.display != "none" && isClickOutCombo(comboAplicacion, coordx, coordy))
                comboAplicacion.ocultar();
            if (IsCalendarVisible)
                replegarCalendarioGeneral(evento);
        }
        
        if (teclaAuxiliar === 9) { // TAB
            comboOrganizacion.ocultar();
            comboAplicacion.ocultar();
            if (IsCalendarVisible)
                hideCalendar();
        }

        keyDel(evento);
    };
}