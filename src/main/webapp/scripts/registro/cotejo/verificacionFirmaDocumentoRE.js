function ViewVerificacionFirmaDocumentoRE() {    
    /* Datos */
    var textos = null;

    /* Campos del formulario de la vista jsp */
    var inputIndexFichero = null;
    var inputDatosCertificado = null;
    var inputIdFirmante = null;
    var inputNombreFirmante = null;
    var inputEmisorCertificado = null;
    var inputValidezCertificado = null;
    
    /* Funciones accesibles desde el exterior */
    this.inicializar = _inicializar;
    this.pulsarSalir = _pulsarSalir;
    this.checkKeysLocal = _checkKeysLocal;
    
    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function _inicializar(opciones) {
        textos = opciones.textos;

        inputDatosCertificado = $('#datosCertificado');
        inputIdFirmante = $('#idFirmante');
        inputNombreFirmante = $('#nombreFirmante');
        inputEmisorCertificado = $('#emisorCertificado');
        inputValidezCertificado = $('#validezCertificado');
        inputIndexFichero = $('#indexFichero');
                
        document.onmouseup = checkKeys;
        window.focus();
        pleaseWait('off');
    };

    // FUNCIONES DE PULSACION DE BOTONES
    function _pulsarSalir() {
        var action = APP_CONTEXT_PATH + '/registro/DocumentoRegistro.do?opcion=consultarDocumentoCotejado'
                + '&indexFichero=' + inputIndexFichero.val();
        
        document.forms[0].target = "mainFrame";
        document.forms[0].action = action;
        document.forms[0].submit();
    };

    /* Funciones para teclas de acceso rapido */
    function _checkKeysLocal(evento, tecla) {
        var teclaAuxiliar = evento.which;
        var coordx = evento.clientX;
        var coordy = evento.clientY;

        keyDel(evento);
    };
}