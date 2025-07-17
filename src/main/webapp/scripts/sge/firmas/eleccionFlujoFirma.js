function ViewEleccionFlujoFirma() {
    /* Datos */
    var textos = null;
    var tabFlujosFirma = null;
    var datosTabFlujosFirma = null;

    /* Funciones accesibles desde el exterior */
    this.inicializar = _inicializar;
    this.pulsarAceptar = _pulsarAceptar;
    this.pulsarCancelar = _pulsarCancelar;
    this.checkKeysLocal = _checkKeysLocal;

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function _inicializar(opciones) {
        textos = opciones.textos;
        datosTabFlujosFirma = opciones.datosTabFlujosFirma;
        
        // Carga de datos para la tabla
        crearTablaUsuariosFirma();
        rellenarTablaFlujosFirma(datosTabFlujosFirma);

        document.onmouseup = checkKeys;
        window.focus();
        pleaseWait('off');
    };

    /* Crea la tabla de flujos de firma */
    function crearTablaUsuariosFirma() {
        tabFlujosFirma = new Tabla(true, textos.buscar, textos.anterior, textos.siguiente
                                           , textos.mosFilasPag, textos.msgNoResultBusq
                                           , textos.mosPagDePags, textos.noRegDisp
                                           , textos.filtrDeTotal, textos.primero
                                           , textos.ultimo
                                           , document.getElementById('tablaFlujosFirma'));
                                           
        tabFlujosFirma.addColumna('0', 'center', ""); // id oculto del flujo
        tabFlujosFirma.addColumna('20', 'center', textos.eqTblFlujoNombre);
        tabFlujosFirma.addColumna('20', 'center', textos.eqTblFlujoNumero);
        tabFlujosFirma.addColumna('60', 'center', textos.eqTblNombresUsuarios);
        tabFlujosFirma.displayCabecera = true;
    }

    /* Rellena la tabla de flujos de firma con los datos pasados por parametro */
    function rellenarTablaFlujosFirma(datos) {
        if (datos) {
            tabFlujosFirma.lineas = datos;
        } else {
            tabFlujosFirma.lineas = [];
        }
        
        tabFlujosFirma.displayTabla();
    };

    function seleccionFlujo() {
        var linea = tabFlujosFirma.getLinea();
        var flujo = {
            id: linea[0],
            nombre: linea[1],
        }

        pleaseWait('off');
        volverPantallaPrincipal(flujo);
    }

    // FUNCIONES DE PULSACION DE BOTONES
    function _pulsarAceptar() {
        if (tabFlujosFirma.selectedIndex !== -1) {
            seleccionFlujo();
        } else {
            jsp_alerta('A', textos.msjNoSelecFila);
        }
    };

    function _pulsarCancelar() {
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