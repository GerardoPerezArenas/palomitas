function ViewVerDetalleEstadoFirma() {
    /* Datos */
    var textos = null;
    var claveDocumento = null;
    var firmante = null;
    
    /* Funciones accesibles desde el exterior */
    this.inicializar = _inicializar;
    this.pulsarSalir = _pulsarSalir;
    this.checkKeysLocal = _checkKeysLocal;
    
    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function _inicializar(opciones) {
        textos = opciones.textos;

        if (parent.window.opener.xanelaAuxiliarArgs) {
            claveDocumento = parent.window.opener.xanelaAuxiliarArgs.claveDocumento;
            firmante = parent.window.opener.xanelaAuxiliarArgs.firmante;
        }

        rellenarDatos();
        
        document.onmouseup = checkKeys;
        window.focus();
        pleaseWait('off');
    };

    function rellenarDatos() {
        var nombreUsuario = firmante.id + ' - ' + firmante.nombre;
        var estadoFirma = getEstadoFirma(firmante);
        var observaciones = firmante.observaciones;
        var fechaFirmaString = '';
        
        if (firmante.fechaFirma) {
            var fechaFirma = firmante.fechaFirma;
            // dd/mm/yyyy HH:MI:SS 
            // Objeto Calendar, por lo que el código de mes va de 0 a 11
            fechaFirmaString = leftPadZero(fechaFirma.dayOfMonth, 2) + '/'
                    + leftPadZero(fechaFirma.month + 1, 2) + '/'
                    + fechaFirma.year + ' '
                    + leftPadZero(fechaFirma.hourOfDay, 2) + ':'
                    + leftPadZero(fechaFirma.minute, 2) + ':'
                    + leftPadZero(fechaFirma.second, 2);
        }
            
        $('#usuarioFirma').val(nombreUsuario);
        $('#estadoFirma').val(estadoFirma);
        $('#observaciones').val(observaciones);
        $('#fechaFirma').val(fechaFirmaString);
    }

    /* Devuelve el texto del estado de la firma */
    function getEstadoFirma(firmante) {
        var estadoTexto = '';
        
        if (firmante.estadoFirma === 'O') {
            estadoTexto = textos.eqEstadoPendiente;
        } else if (firmante.estadoFirma === 'R') {
            estadoTexto = textos.eqEstadoRechazado;
        } else if (firmante.estadoFirma === 'F') {
            estadoTexto = textos.eqEstadoFirmado;
        }
        
        return estadoTexto;
    }

    // FUNCIONES DE PULSACION DE BOTONES
    function _pulsarSalir() {
        volverPantallaPrincipal();
    };
    
    /* Funciones para teclas de acceso rapido */
    function _checkKeysLocal(evento, tecla) {
        var teclaAuxiliar = evento.which;
        var coordx = evento.clientX;
        var coordy = evento.clientY;

        keyDel(evento);
    };
}