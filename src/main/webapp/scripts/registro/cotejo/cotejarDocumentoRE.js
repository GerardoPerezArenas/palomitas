function ViewCotejarDocumentoRE() {
    /* Datos */
    var textos = null;

    /* Campos del formulario de la vista jsp */
    var inputTituloDoc = null;
    var inputFirmaBase64 = null;
    var inputCodTipoDocumental = null;
    var inputCodEstadoElaboracion = null;
    var comboTipoDocumental = null;
    var comboEstadoElaboracion = null;

    var listaTipoDocumental = null;
    var listaEstadoElaboracion = null;
    var valorDefectoTipoDocumental = null;
    var valorDefectoEstadoElaboracion = null;

    /* Funciones accesibles desde el exterior */
    this.inicializar = _inicializar;
    this.mostrarMsjError = _mostrarMsjError;
    this.pulsarCotejar = _pulsarCotejar;
    this.pulsarSalir = _pulsarSalir;
    this.finalizarCotejo = _finalizarCotejo;
    this.checkKeysLocal = _checkKeysLocal;

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function _inicializar(opciones) {
        textos = opciones.textos;
        listaTipoDocumental = opciones.listaTipoDocumental;
        listaEstadoElaboracion = opciones.listaEstadoElaboracion;
        valorDefectoTipoDocumental = opciones.valorDefectoTipoDocumental;
        valorDefectoEstadoElaboracion = opciones.valorDefectoEstadoElaboracion;

        crearCombos();

        inputTituloDoc = $('#tituloDoc');
        inputFirmaBase64 = $('#firmaBase64');
        inputCodTipoDocumental = $('#codTipoDocumental');
        inputCodEstadoElaboracion = $('#codEstadoElaboracion');

        document.onmouseup = checkKeys;
        window.focus();
        pleaseWait('off');
    };

    function crearCombos() {
        comboTipoDocumental = new Combo("TipoDocumental");
        comboEstadoElaboracion = new Combo("EstadoElaboracion");

        comboTipoDocumental.addItems(listaTipoDocumental.codigo, listaTipoDocumental.descripcion);
        comboEstadoElaboracion.addItems(listaEstadoElaboracion.codigo, listaEstadoElaboracion.descripcion);

        comboTipoDocumental.buscaCodigo(valorDefectoTipoDocumental);
        comboEstadoElaboracion.buscaCodigo(valorDefectoEstadoElaboracion);
    }

    // FUNCIONES DE PULSACION DE BOTONES
    function _pulsarCotejar() {
        pleaseWait('on');
        var source = APP_CONTEXT_PATH + '/registro/DocumentoRegistro.do?opcion=documentoCotejarFirma';                
        document.forms[0].target = "oculto";
        document.forms[0].action = source;
        document.forms[0].submit();
    };

    function _pulsarSalir() {
        volverPantallaPrincipal();
    };

    // Funciones de cotejado de documentos
    function _finalizarCotejo(ficheroBase64) {
        pleaseWait('on');

        var criterio = {};
        criterio.opcion = 'finalizarCotejarDocumento';
        criterio.firmaBase64 = ficheroBase64;
        criterio.tituloDoc = inputTituloDoc.val();
        criterio.codTipoDocumental = inputCodTipoDocumental.val();
        criterio.codEstadoElaboracion = inputCodEstadoElaboracion.val();

        try {
            $.ajax({
                url: APP_CONTEXT_PATH + '/registro/DocumentoRegistro.do',
                type: 'POST',
                async: true,
                data: criterio,
                success: successFinalizarCotejo,
                error: errorFinalizarCotejo
            });
        } catch (error) {
            errorFinalizarCotejo();
        }
    };

    function successFinalizarCotejo() {
        // Se refresca la tabla de documentos de los registros
        var source = APP_CONTEXT_PATH + '/registro/DocumentoRegistro.do?opcion=refrescarListaDocumentoRE';                
        document.forms[0].target = "oculto";
        document.forms[0].action = source;
        document.forms[0].submit();

        // Se redirige a la pantalla de consulta de documentos cotejados
        var source = APP_CONTEXT_PATH + '/registro/DocumentoRegistro.do?opcion=consultarDocumentoCotejado';
        document.forms[0].target = "mainFrame";
        document.forms[0].action = source;
        document.forms[0].submit();
    };

    function errorFinalizarCotejo() {
        _mostrarMsjError(textos.msjErrorInternoCotejo);
    };

    function _mostrarMsjError(msjError) {
        if (msjError) {
            jsp_alerta('A', msjError);
        }
        
        pleaseWait('off');
    }

    /* Funciones para teclas de acceso rapido */
    function _checkKeysLocal(evento, tecla) {
        var teclaAuxiliar = evento.which;
        var coordx = evento.clientX;
        var coordy = evento.clientY;

        if (teclaAuxiliar === 9) {
            comboTipoDocumental.ocultar();
            comboEstadoElaboracion.ocultar();
            return false;
        }

        keyDel(evento);
    };
}