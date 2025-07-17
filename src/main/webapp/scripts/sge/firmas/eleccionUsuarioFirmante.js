function ViewEleccionUsuarioFirmante() {
    /* Constantes */
    var URL_CARGA_DATOS = APP_CONTEXT_PATH + '/sge/DefinicionFlujosFirma.do';

    /* Datos */
    var textos = null;
    var tabUsuariosFirma = null;
    var datosTabUsuariosFirma = null;
    var portafirmas = null;

    /* Funciones accesibles desde el exterior */
    this.inicializar = _inicializar;
    this.pulsarAceptar = _pulsarAceptar;
    this.pulsarCancelar = _pulsarCancelar;
    this.checkKeysLocal = _checkKeysLocal;

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function _inicializar(opciones) {
        textos = opciones.textos;
        datosTabUsuariosFirma = opciones.datosTabUsuariosFirma;
        portafirmas = opciones.portafirmas;
        
        // Carga de datos para la tabla
        crearTablaUsuariosFirma();
        rellenarTablaUsuariosFirma(datosTabUsuariosFirma);

        document.onmouseup = checkKeys;
        window.focus();
        pleaseWait('off');
    };

    /* Crea la tabla de flujos de firma */
    function crearTablaUsuariosFirma() {
        
        tabUsuariosFirma = null;
        
       
            tabUsuariosFirma = new Tabla(true, textos.buscar, textos.anterior, textos.siguiente
                                           , textos.mosFilasPag, textos.msgNoResultBusq
                                           , textos.mosPagDePags, textos.noRegDisp
                                           , textos.filtrDeTotal, textos.primero
                                           , textos.ultimo
                                           , document.getElementById('tablaUsuariosFirma'));
       
       
                                           
        tabUsuariosFirma.addColumna('0', 'center', ""); // idUsuario oculto del circuito
        tabUsuariosFirma.addColumna('20', 'center', textos.eqTblUsuarioLogin);
        tabUsuariosFirma.addColumna('20', 'center', textos.eqTblUsuarioNIF);
         if (""!=portafirmas && "LAN"==portafirmas) {
            tabUsuariosFirma.addColumna('20', 'center', textos.eqTblUsuarioBuzFir);
        }
        tabUsuariosFirma.addColumna('60', 'center', textos.eqTblUsuarioNombre);
        tabUsuariosFirma.displayCabecera = true;
    }

    /* Rellena la tabla de circuitos de firma con los datos pasados por parametro */
    function rellenarTablaUsuariosFirma(datos) {
        if (datos) {
            tabUsuariosFirma.lineas = datos;
        } else {
            tabUsuariosFirma.lineas = [];
        }
        
        tabUsuariosFirma.displayTabla();
    };

    function altaUsuarioFirmante() {
        var linea = tabUsuariosFirma.getLinea();
        var usuarioFirmante = null;
        
         if (""!=portafirmas && "LAN"==portafirmas) {
            usuarioFirmante = {
                idUsuario: linea[0],
                login: linea[1],
                documento: linea[2],
                buzFir: linea[3],
                nombre: linea[4]
               
            };
        } else {
            usuarioFirmante = {
                idUsuario: linea[0],
                login: linea[1],
                documento: linea[2],
                nombre: linea[3]
            };
        }
        

        if (self.parent.opener.comprobarUsuarioDuplicado
                && self.parent.opener.comprobarUsuarioDuplicado(usuarioFirmante)) {
            pleaseWait('off');
            jsp_alerta('A', textos.msjUsuarioYaExiste);
        } else {
            pleaseWait('off');
            volverPantallaPrincipal(usuarioFirmante);
        }
    }

    /**********************
     *** Funciones AJAX ***
     **********************/
    
    /* Actualizar los datos del circuito del flujo de firma */
    function comprobarUsuarioPortafirmas() {
        try {
            pleaseWait('on');
            
            var linea = tabUsuariosFirma.getLinea();
            
            var criterio = {};
            criterio.opcion = 'comprobarUsuarioPortafirmas';
            criterio.documento = linea[2];

            $.ajax({
                url: URL_CARGA_DATOS,
                type: 'POST',
                async: true,
                data: criterio,
                success: successComprobarUsuarioPortafirmas,
                error: errorAjax
            });
        } catch (Err) {
            errorAjax();
        }
    }

    function successComprobarUsuarioPortafirmas(ajaxResult) {
        successAjax(ajaxResult, processComprobarUsuarioPortafirmas, textos.msjUsuarioNoAnhadirFlujo);
    }

    function processComprobarUsuarioPortafirmas(resultado) {
        if (resultado === true) {
            altaUsuarioFirmante();
        } else {
            jsp_alerta('A', textos.msjUsuarioNoExistePortafirmas);
            pleaseWait('off');
        }
    }

    // FUNCIONES DE PULSACION DE BOTONES
    function _pulsarAceptar() {
        if (tabUsuariosFirma.selectedIndex !== -1) {
            comprobarUsuarioPortafirmas();
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