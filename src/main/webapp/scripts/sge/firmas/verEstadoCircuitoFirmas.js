function ViewVerEstadoCircuitoFirmas() {
    /* Constantes */
    var URL_CARGA_DATOS = APP_CONTEXT_PATH + '/sge/DefinicionFlujosFirma.do';
    
    /* Datos */
    var textos = null;
    var claveDocumento = null;
    var estadosFirmas = null;
    var datosTabEstadoFirmas = null;
    var tabEstadoFirmas = null;
    
    /* Funciones accesibles desde el exterior */
    this.inicializar = _inicializar;
    this.pulsarSalir = _pulsarSalir;
    this.verDetalleFirmaFirmante = _verDetalleFirmaFirmante;
    this.checkKeysLocal = _checkKeysLocal;
    
    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function _inicializar(opciones) {
        textos = opciones.textos;

        if (opciones.claveDocumento) {
            claveDocumento = JSON.parse(opciones.claveDocumento);
        }
        
        datosTabEstadoFirmas = [];
        if (opciones.estadosFirmas) {
            estadosFirmas = JSON.parse(opciones.estadosFirmas);
            
            for (var i = 0; i < estadosFirmas.length; i++) {
                datosTabEstadoFirmas[i] = [
                    estadosFirmas[i].id,
                    estadosFirmas[i].orden,
                    estadosFirmas[i].nombre,
                    getEstadoFirma(estadosFirmas[i])
                ];
            }
        }
        
        crearTablaEstadoFirmas();
        rellenarTabla(datosTabEstadoFirmas);
        
        document.onmouseup = checkKeys;
        window.focus();
        pleaseWait('off');
    };

    /* Crea la tabla de flujos de firma */
    function crearTablaEstadoFirmas() {
        tabEstadoFirmas = new Tabla(true, textos.buscar, textos.anterior, textos.siguiente
                                           , textos.mosFilasPag, textos.msgNoResultBusq
                                           , textos.mosPagDePags, textos.noRegDisp
                                           , textos.filtrDeTotal, textos.primero
                                           , textos.ultimo
                                           , document.getElementById('tablaEstadoFirmas'));
        
        tabEstadoFirmas.addColumna('0', 'center', ''); // idUsuario
        tabEstadoFirmas.addColumna('10', 'center', textos.eqTblVerFirmaOrden);
        tabEstadoFirmas.addColumna('55', 'center', textos.eqTblVerFirmaUsuario);
        tabEstadoFirmas.addColumna('35', 'center', textos.eqTblVerFirmaEstado);
        tabEstadoFirmas.displayCabecera = true;
    }

    function rellenarTabla(datos) {
        if (datos) {
            tabEstadoFirmas.lineas = datos;
        } else {
            tabEstadoFirmas.lineas = [];
        }
        
        tabEstadoFirmas.displayTabla();
    };

    /* Devuelve el texto del estado de la firma y, de haberla, el enlace al detalle de la misma */
    function getEstadoFirma(estadoFirma) {
        var estadoTexto = '';
        
        if (estadoFirma.estadoFirma === 'O') {
            estadoTexto = textos.eqEstadoPendiente;
        } else if (estadoFirma.estadoFirma === 'R') {
            estadoTexto = textos.eqEstadoRechazado;
            estadoTexto += ' ' + getEnlaceDetalleFirma(estadoFirma.id);
        } else if (estadoFirma.estadoFirma === 'F') {
            estadoTexto = textos.eqEstadoFirmado;
            estadoTexto += ' ' + getEnlaceDetalleFirma(estadoFirma.id);
        }
        
        return estadoTexto;
    }

    /* Devuelve una cadena html con el enlace a los detalles de la firma */
    function getEnlaceDetalleFirma(idUsuario) {
        var enlace = '';
        enlace = '<a href="javascript:viewVerEstadoCircuitoFirmas.verDetalleFirmaFirmante(' + idUsuario + ')">(' + textos.eqVerDatosFirma + ')</a>';
        return enlace;
    }

    // FUNCIONES DE PULSACION DE BOTONES
    function _pulsarSalir() {
        volverPantallaPrincipal();
    };

    /* Muestra los detalles de la firma del usuario */
    function _verDetalleFirmaFirmante(idUsuario) {
        var estadoFirmante = null;
        for (var i = 0; i < estadosFirmas.length; i++) {
            if (estadosFirmas[i].id === idUsuario) {
                estadoFirmante = estadosFirmas[i];
                break;
            }
        }
        
        var opcion = "cargarPantallaVerDetalleEstadoFirma";
        var source = URL_CARGA_DATOS + '?opcion=' + opcion;
        var args = {
			claveDocumento: claveDocumento,
			firmante: estadoFirmante
		};

        abrirXanelaAuxiliar(APP_CONTEXT_PATH + '/jsp/sge/mainVentana.jsp?source=' + source,
                args, 'width=850,height=650,scroll=no', function(result) {
                    if (result) {
                    }
                });
    };
    
    /* Funciones para teclas de acceso rapido */
    function _checkKeysLocal(evento, tecla) {
        var teclaAuxiliar = evento.which;
        var coordx = evento.clientX;
        var coordy = evento.clientY;

        keyDel(evento);
    };
}