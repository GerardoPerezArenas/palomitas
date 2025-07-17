function ViewDefinicionCircuitosFirma() {
    /* Constantes */
    var URL_CARGA_DATOS = APP_CONTEXT_PATH + '/sge/DefinicionFlujosFirma.do';
    
    /* Datos */
    var textos = null;
    var idFlujoFirma = null;
    var datosTabCircuitosFirma = null;
    var tabCircuitosFirma = null;
    var maxNumOrden = null;
    var hayCambios = false;
    
    /* Funciones accesibles desde el exterior */
    this.inicializar = _inicializar;
    this.pulsarAltaFirmanteCircuitoFirma = _pulsarAltaFirmanteCircuitoFirma;
    this.pulsarEliminarFirmanteCircuitoFirma = _pulsarEliminarFirmanteCircuitoFirma;
    this.pulsarAceptarCircuitoFirma = _pulsarAceptarCircuitoFirma;
    this.pulsarCancelarCircuitoFirma = _pulsarCancelarCircuitoFirma;
    this.comprobarUsuarioDuplicado = _comprobarUsuarioDuplicado;
    this.checkKeysLocal = _checkKeysLocal;
    
    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function _inicializar(opciones) {
        textos = opciones.textos;
        idFlujoFirma = opciones.idFlujoFirma;
        datosTabCircuitosFirma = opciones.datosTabCircuitosFirma;
        maxNumOrden = datosTabCircuitosFirma.length;
        
        crearTablaCircuitosFirma();
        resetPage(datosTabCircuitosFirma);
        
        document.onmouseup = checkKeys;
        window.focus();
        pleaseWait('off');
    };

    /* Crea la tabla de flujos de firma */
    function crearTablaCircuitosFirma() {
        tabCircuitosFirma = new Tabla(true, textos.buscar, textos.anterior, textos.siguiente
                                           , textos.mosFilasPag, textos.msgNoResultBusq
                                           , textos.mosPagDePags, textos.noRegDisp
                                           , textos.filtrDeTotal, textos.primero
                                           , textos.ultimo
                                           , document.getElementById('tablaCircuitosFirma'));
                                           
        tabCircuitosFirma.addColumna('0', 'center', ""); // idFlujoFirma oculto del circuito
        tabCircuitosFirma.addColumna('0', 'center', ""); // idUsuario oculto del circuito
        tabCircuitosFirma.addColumna('10', 'center', textos.eqTblCircuitosFirmaOrden);
        tabCircuitosFirma.addColumna('70', 'center', textos.eqTblCircuitosFirmaUsuario);
        tabCircuitosFirma.addColumna('20', 'center', textos.eqTblCircuitosFirmaDocumento);
        tabCircuitosFirma.displayCabecera = true;
    }

    /* Rellena la tabla de circuitos de firma con los datos pasados por parametro */
    function rellenarTablaCircuitosFirma(datos) {
        if (datos) {
            tabCircuitosFirma.lineas = datos;
        } else {
            tabCircuitosFirma.lineas = [];
        }
        
        tabCircuitosFirma.displayTabla();
    };

    /* Recarga la tabla de circuitos de firmas */
    function resetPage(datos) {
        rellenarTablaCircuitosFirma(datos);        
    };

    // FUNCIONES DE PULSACION DE BOTONES
    function _pulsarAltaFirmanteCircuitoFirma() {
        eleccionAltaFirmante();
    };
    
    function _pulsarEliminarFirmanteCircuitoFirma() {
        if (tabCircuitosFirma.selectedIndex !== -1) {
            if (jsp_alerta("C", textos.preguntaEliminarUsuarioCircuitoFirma) == 1) {
                eliminarFirmanteCircuitoFirma();
            }
        } else {
            jsp_alerta('A', textos.msjNoSelecFila);
        }
    };
    
    function _pulsarAceptarCircuitoFirma() {
        if (hayCambios) {
            actualizarDatosCircuitoFlujo();
        } else {
            volverPantallaPrincipal();
        }
    };
    
   function _pulsarCancelarCircuitoFirma() {
        if (!hayCambios || (hayCambios && jsp_alerta('C', textos.msjExistenDatosGuardar))) {
            volverPantallaPrincipal();
        }
    };

    /**********************
     *** Funciones AJAX ***
     **********************/

    /* Actualizar los datos del circuito del flujo de firma */
    function eleccionAltaFirmante() {
        var opcion = "cargarPantallaEleccionUsuarioFirmante";
        var source = APP_CONTEXT_PATH + '/sge/DefinicionFlujosFirma.do?opcion=' + opcion;

        abrirXanelaAuxiliar(APP_CONTEXT_PATH + '/jsp/sge/mainVentana.jsp?source=' + source,
                "ventana1", 'width=850,height=650,scroll=no', function(result) {
                    if (result) {
                        altaFirmanteCircuitoFirma(result);
                    }
                });
    }
    
    /* Inserta un nuevo firmante en el circuito
     */
    function altaFirmanteCircuitoFirma(usuarioFirmante) {
        pleaseWait('on');
        
        var orden = maxNumOrden;

        var firmante = [
            idFlujoFirma,
            usuarioFirmante.idUsuario,
            ++orden,
            usuarioFirmante.nombre,
            usuarioFirmante.documento
        ];

        datosTabCircuitosFirma.push(firmante);
        maxNumOrden = orden;

        hayCambios = true;
        resetPage(datosTabCircuitosFirma);
        
        pleaseWait('off');
    }
    
    // Comprueba si un usuario ya existe en el circuito
    function _comprobarUsuarioDuplicado(usuarioFirmante) {
        var duplicado = false;
        
        for (var i = 0; i < datosTabCircuitosFirma.length; i++) {
            if (usuarioFirmante.idUsuario == datosTabCircuitosFirma[i][1]) {
                duplicado = true;
                break;
            }
        }
        
        return duplicado;
    }
    
    /* Actualizar los datos del circuito del flujo de firma */
    function actualizarDatosCircuitoFlujo() {
        try {
            pleaseWait('on');
            
            var criterio = {};
            criterio.opcion = 'actualizarDatosCircuitoFlujo';
            criterio.idFlujoFirma = idFlujoFirma;
            criterio.idsUsuarioFirmante = obtenerListaFirmantes();

            $.ajax({
                url: URL_CARGA_DATOS,
                type: 'POST',
                async: true,
                data: criterio,
                success: successActualizarDatosCircuitoFlujo,
                error: errorAjax
            });
        } catch (Err) {
            errorAjax();
        }
    }

    function successActualizarDatosCircuitoFlujo(ajaxResult) {
        successAjax(ajaxResult, processActualizarDatosCircuitoFlujo, textos.msjErrorInternoAplicacion);
    }

    function processActualizarDatosCircuitoFlujo() {
        jsp_alerta('A', textos.msgDatosGrabados);
        volverPantallaPrincipal();
    }

    /* Obtiene el listado de usuarios que forman el circuito de firma y lo convierte
     * a JSON. El orden es el de entrada en el array.
     */
    function obtenerListaFirmantes() {
        var listaFirmantes = [];
        
        if (datosTabCircuitosFirma && datosTabCircuitosFirma.length > 0) {
            for (var i = 0; i < datosTabCircuitosFirma.length; i++) {
                listaFirmantes[i] = datosTabCircuitosFirma[i][1];
            }
        }
        
        return JSON.stringify(listaFirmantes);
    }

    /* Eliminar un firmante del circuito del flujo de firma */
    function eliminarFirmanteCircuitoFirma() {
        pleaseWait('on');

        var indiceBorrar = tabCircuitosFirma.selectedIndex;
        if (indiceBorrar > -1) {
            datosTabCircuitosFirma.splice(indiceBorrar, 1);
        }
        
        // Reordenacion del orden de los firmantes
        maxNumOrden = 0;
        for (var i = 0; i < datosTabCircuitosFirma.length; i++) {
            datosTabCircuitosFirma[i][2] = ++maxNumOrden;
        }
        
        hayCambios = true;
        resetPage(datosTabCircuitosFirma);
        pleaseWait('off');
    }

    /* Funciones para teclas de acceso rapido */
    function _checkKeysLocal(evento, tecla) {
        var teclaAuxiliar = evento.which;
        var coordx = evento.clientX;
        var coordy = evento.clientY;

        keyDel(evento);
    };
}