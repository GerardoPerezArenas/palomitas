function ViewDefinicionFlujosFirma() {
    /* Constantes */
    var URL_CARGA_DATOS = APP_CONTEXT_PATH + '/sge/DefinicionFlujosFirma.do';

    /* Datos */
    var textos = null;
    var tabFlujosFirma = null;
    var datosTabFlujosFirma = null;
    var datosComboTipoFirma = null;

    /* Campos del formulario de la vista jsp */
    var nombreFlujoFirma = null;
    var codTipoFlujoFirma = null;
    var comboTipoFlujoFirma = null;

    /* Funciones accesibles desde el exterior */
    this.inicializar = _inicializar;
    this.callFromTableToFlujosFirma = _callFromTableToFlujosFirma;
    this.pulsarAltaFlujoFirma = _pulsarAltaFlujoFirma;
    this.pulsarEliminarFlujoFirma = _pulsarEliminarFlujoFirma;
    this.pulsarDefinirFlujoFirma = _pulsarDefinirFlujoFirma;
    this.pulsarActivarDesactivarFlujoFirma = _pulsarActivarDesactivarFlujoFirma;
    this.pulsarLimpiarFlujoFirma = _pulsarLimpiarFlujoFirma;
    this.checkKeysLocal = _checkKeysLocal;

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function _inicializar(opciones) {
        textos = opciones.textos;
        datosTabFlujosFirma = opciones.datosTabFlujosFirma;
        datosComboTipoFirma = opciones.datosComboTipoFirma;
        
        nombreFlujoFirma = $('#nombreFlujoFirma');
        codTipoFlujoFirma = $('#codTipoFlujoFirma');

        // Carga de datos para las tablas y combos
        crearComboTipoFlujosFirma();
        crearTablaFlujosFirma();

        resetPage(datosTabFlujosFirma);

        document.onmouseup = checkKeys;
        window.focus();
        pleaseWait('off');
    };

    /* Crea y rellena el combo de tipos de flujo de firma */
    function crearComboTipoFlujosFirma() {
        comboTipoFlujoFirma = new Combo("TipoFlujoFirma");
        comboTipoFlujoFirma.addItems(datosComboTipoFirma.codigo, datosComboTipoFirma.descripcion);
    }

    /* Crea la tabla de flujos de firma */
    function crearTablaFlujosFirma() {
        tabFlujosFirma = new Tabla(true, textos.buscar, textos.anterior, textos.siguiente
                , textos.mosFilasPag, textos.msgNoResultBusq
                , textos.mosPagDePags, textos.noRegDisp
                , textos.filtrDeTotal, textos.primero
                , textos.ultimo
                , document.getElementById('tablaFlujosFirma'));

        tabFlujosFirma.addColumna('0', 'center', ""); // Codigo del flujo oculto
        tabFlujosFirma.addColumna('46', 'center', textos.eqTblFlujosFirmaFlujo);
        tabFlujosFirma.addColumna('32', 'center', textos.eqTblFlujosFirmaTipo);
        tabFlujosFirma.addColumna('22', 'center', textos.eqTblFlujosFirmaActivo);
        tabFlujosFirma.displayCabecera = true;
    }

    // Evento doble clic de la tabla de flujos de firma
    function _callFromTableToFlujosFirma(rowID, tableName) {
        var linea = datosTabFlujosFirma[rowID];
        abrirPantallaDefinir(linea);
    }

    /* Rellena la tabla de flujos de firma con los datos pasados por parametro */
    function rellenarTablaFlujosFirma(datos) {
        if (datos) {
            tabFlujosFirma.lineas = datos;
        } else {
            tabFlujosFirma.lineas = [];
        }

        tabFlujosFirma.displayTabla();
    }

    /* Recarga la tabla de flujo de firmas y limpia los inputs */
    function resetPage(datos) {
        rellenarTablaFlujosFirma(datos);
        limpiezaFormulario();
    }

    function limpiezaFormulario() {
        nombreFlujoFirma.val('');
        comboTipoFlujoFirma.clearSelected();
        comboTipoFlujoFirma.buscaCodigo(0);
    }

    /**********************
     *** Funciones AJAX ***
     **********************/
    
    /* Insertar un flujo de firma */
    function insertarFlujoFirma() {
        try {
            pleaseWait('on');
            
            var criterio = {};
            criterio.opcion = 'insertarFlujoFirma';
            criterio.nombreFlujo = nombreFlujoFirma.val().toUpperCase();
            criterio.idTipoFlujo = codTipoFlujoFirma.val();

            $.ajax({
                url: URL_CARGA_DATOS,
                type: 'POST',
                async: true,
                data: criterio,
                success: successInsertarFlujoFirma,
                error: errorAjax
            });
        } catch (Err) {
            errorAjax();
        }
    }

    function successInsertarFlujoFirma(ajaxResult) {
        successAjax(ajaxResult, cargarDatosFlujosFirma, textos.msjErrorInternoAplicacion);
    }

    /* Eliminar un flujo de firma */
    function eliminarFlujoFirma() {
        try {
            pleaseWait('on');

            var idFlujo = tabFlujosFirma.getLinea()[0];

            var criterio = {};
            criterio.opcion = 'eliminarFlujoFirma';
            criterio.idFlujo = idFlujo;

            $.ajax({
                url: URL_CARGA_DATOS,
                type: 'POST',
                async: true,
                data: criterio,
                success: successEliminarFlujoFirma,
                error: errorAjax
            });
        } catch (Err) {
            errorAjax();
        }
    }

    function successEliminarFlujoFirma(ajaxResult) {
        successAjax(ajaxResult, cargarDatosFlujosFirma, textos.msjErrorInternoAplicacion);
    }

    /* Activar/Desactivar un flujo de firma */
    function activarDesactivarFlujoFirma() {
        try {
            pleaseWait('on');

            var flujo = tabFlujosFirma.getLinea();
            var idFlujo = flujo[0];
            var activar = (flujo[3] === 'SI') ? false : true;
            
            var criterio = {};
            criterio.opcion = 'activarDesactivarFlujoFirma';
            criterio.idFlujo = idFlujo;
            criterio.activar = activar;

            $.ajax({
                url: URL_CARGA_DATOS,
                type: 'POST',
                async: true,
                data: criterio,
                success: successActivarDesactivarFlujoFirma,
                error: errorAjax
            });
        } catch (Err) {
            errorAjax();
        }
    }

    function successActivarDesactivarFlujoFirma(ajaxResult) {
        successAjax(ajaxResult, cargarDatosFlujosFirma, textos.msjErrorInternoAplicacion);
    }

    /* Carga del listado de flujos de firma */
    function cargarDatosFlujosFirma() {
        try {
            pleaseWait('on');

            var criterio = {};
            criterio.opcion = 'cargarDatosFlujosFirma';

            $.ajax({
                url: URL_CARGA_DATOS,
                type: 'POST',
                async: true,
                data: criterio,
                success: successCargarDatosFlujosFirma,
                error: errorAjax
            });
        } catch (Err) {
            errorAjax();
        }
    }

    function successCargarDatosFlujosFirma(ajaxResult) {
        successAjax(ajaxResult, procesarCargarDatosFlujoFirma, textos.msjErrorInternoAplicacion);
    }
    
    function procesarCargarDatosFlujoFirma(resultado) {
        datosTabFlujosFirma = [];
        
        if (resultado) {
            for (var i = 0; i < resultado.length; i++) {
                var flujo = resultado[i];
                datosTabFlujosFirma[i] = [
                    flujo.id,
                    flujo.nombre,
                    flujo.tipoFirma,
                    (flujo.activo === true) ? 'SI' : 'NO',
                ];
            }
        }
        
        resetPage(datosTabFlujosFirma);
        
        pleaseWait('off');
    }

    // FUNCIONES DE PULSACION DE BOTONES
    function _pulsarAltaFlujoFirma() {
        if (validarAltaFlujo()) {
            insertarFlujoFirma();
        }        
    };

    // Validaciones al dar de alta un flujo de firma
    function validarAltaFlujo() {
        var valido = true;
        var msjError = '';

        var nombreFlujo = nombreFlujoFirma.val();
        var codTipoFlujo = codTipoFlujoFirma.val();

        if (valido) { // Campos obligatorios
            if (!nombreFlujo || !codTipoFlujo) {
                valido = false;
                msjError = textos.msjDatosObligatorios;
            }
        }
        
        if (valido) { // Nombres duplicados
            nombreFlujo = nombreFlujo.toUpperCase();
            for (var i = 0; i < datosTabFlujosFirma.length; i++) {
                if (nombreFlujo === datosTabFlujosFirma[i][1]) {
                    valido = false;
                    msjError = textos.msjExisteNombreFlujoFirma;
                    break;
                }
            }
        }
    
        if (!valido) {
            jsp_alerta('A', msjError);
        }
        
        return valido;
    }

    function _pulsarEliminarFlujoFirma() {
        debugger;
        if (tabFlujosFirma.selectedIndex !== -1) {
            if (jsp_alerta("C", textos.preguntaEliminarFlujoFirma) == 1) {
                eliminarFlujoFirma();
            }
        } else {
            jsp_alerta('A', textos.msjNoSelecFila);
        }
    };

    function _pulsarDefinirFlujoFirma() {
        if (tabFlujosFirma.selectedIndex !== -1) {
            var datosFlujo = datosTabFlujosFirma[tabFlujosFirma.selectedIndex];
            
            abrirPantallaDefinir(datosFlujo);
        } else {
            jsp_alerta('A', textos.msjNoSelecFila);
        }
    };

    /* Abre la pantalla de Definir el circuito de firmas del flujo seleccionado */
    function abrirPantallaDefinir(datosFlujo) {
        var codigoFlujo = datosFlujo[0];
        var opcion = "cargarPantallaDefinicionCircuitosFirma";
        var source = APP_CONTEXT_PATH + '/sge/DefinicionFlujosFirma.do?opcion=' + opcion + '&codigoFlujo=' + codigoFlujo;

        abrirXanelaAuxiliar(APP_CONTEXT_PATH + '/jsp/sge/mainVentana.jsp?source=' + source,
                "ventana1", 'width=850,height=650,scroll=no', function(result) {
                    if (result) {
                    }
                });
    }

    function _pulsarActivarDesactivarFlujoFirma() {
        if (tabFlujosFirma.selectedIndex !== -1) {
            activarDesactivarFlujoFirma();
        } else {
            jsp_alerta('A', textos.msjNoSelecFila);
        }
    };

    function _pulsarLimpiarFlujoFirma() {
        limpiezaFormulario();
    };

    /* Funciones para teclas de acceso rapido */
    function _checkKeysLocal(evento, tecla) {
        var teclaAuxiliar = evento.which;
        var coordx = evento.clientX;
        var coordy = evento.clientY;

        if (teclaAuxiliar === 1) { // CLICK
            if (comboTipoFlujoFirma.base.style.display !== "none" && isClickOutCombo(comboTipoFlujoFirma, coordx, coordy))
                comboTipoFlujoFirma.ocultar();
        }

        if (teclaAuxiliar === 9) { // TAB
            comboTipoFlujoFirma.ocultar();
        }

        keyDel(evento);
    };
}