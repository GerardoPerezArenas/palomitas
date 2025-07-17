function ViewConsultaCotejoDocumentoRE() {    
    /* Datos */
    var textos = null;

    /* Campos del formulario de la vista jsp */
    var inputIndexFichero = null;
    var inputFechaCotejo = null;
    var inputOrigen = null;
    var inputOrgano = null;
    var inputTipoFirma = null;
    var inputCorreo = null;
    var columnaEmail = null;
    
    /* Funciones accesibles desde el exterior */
    this.inicializar = _inicializar;
    this.pulsarDescargarCopia = _pulsarDescargarCopia;
    this.pulsarEnviarEmail = _pulsarEnviarEmail;
    this.pulsarVerificarFirma = _pulsarVerificarFirma;
    this.pulsarSalir = _pulsarSalir;
    this.enviarEmail = _enviarEmail;
    this.checkKeysLocal = _checkKeysLocal;
    
    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function _inicializar(opciones) {
        textos = opciones.textos;

        inputIndexFichero = $('#indexFichero');
        inputFechaCotejo = $('#fechaCaptura');
        inputOrigen = $('#origen');
        inputOrgano = $('#organo');
        inputTipoFirma = $('#tipoFirma');
        inputCorreo = $('#correo');
        inputCorreo.placeholder();
        columnaEmail = $('#columnaEmail');
                
        document.onmouseup = checkKeys;
        window.focus();
        pleaseWait('off');
    };

    // FUNCIONES DE PULSACION DE BOTONES
    function _pulsarDescargarCopia() {
        var indice = inputIndexFichero.val();
        var attachment = true;
        var action = APP_CONTEXT_PATH + "/VerDocumentoRegistro?opcion=0"
                + "&nombre=" + indice
                + "&codigo=" + indice
                + "&attachment=" + attachment;
        
        document.forms[0].target = "oculto";
        document.forms[0].action = action;
        document.forms[0].submit();
    };

    function _pulsarEnviarEmail() {
        columnaEmail.removeClass('hideVisibility');
    };
    
    function _pulsarVerificarFirma() {
        var action = APP_CONTEXT_PATH + '/registro/DocumentoRegistro.do?opcion=verificacionFirmaDocumento'
                + '&indexFichero=' + inputIndexFichero.val();
        
        document.forms[0].target = "mainFrame";
        document.forms[0].action = action;
        document.forms[0].submit();
    };
    
    function _pulsarSalir() {
        volverPantallaPrincipal();
    };

    function _enviarEmail() {
        var direccionEmail = inputCorreo.val();
        
        if (validarEmail(direccionEmail)) {
            try {
                pleaseWait('on');

                var criterio = {};
                criterio.opcion = 'enviarMailCotejo';
                criterio.indexFichero = inputIndexFichero.val();
                criterio.correo = direccionEmail;

                $.ajax({
                    url: APP_CONTEXT_PATH + '/registro/DocumentoRegistro.do',
                    type: 'POST',
                    async: true,
                    data: criterio,
                    success: successEnviarEmail,
                    error: errorEnviarMail
                });
            } catch (Err) {
                errorEnviarMail();
            }
        } else {
            jsp_alerta('A', textos.errFormatoEmailIncorrecto);
        }
    };

    function successEnviarEmail(ajaxResult) {
        inputCorreo.val('');
        columnaEmail.addClass('hideVisibility');
        
        jsp_alerta('A', textos.msjEnviarEmailCorrecto);
        pleaseWait('off');
    };

    function errorEnviarMail() {
        jsp_alerta('A', textos.errEnviarEmail);
        pleaseWait('off');
    };

    /* Funciones para teclas de acceso rapido */
    function _checkKeysLocal(evento, tecla) {
        var teclaAuxiliar = evento.which;
        var coordx = evento.clientX;
        var coordy = evento.clientY;

        keyDel(evento);
    };
}