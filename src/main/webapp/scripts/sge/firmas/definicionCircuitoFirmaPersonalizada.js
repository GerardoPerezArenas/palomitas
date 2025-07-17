function ViewDefinicionCircuitoFirmaPersonalizada() {
    /* Constantes */
    var ETIQUETA_GRUPO_RADIO_TIPO_FIRMA = 'radioTipoFirma';
    var NOMBRE_GRUPO_RADIO_TIPO_FIRMA = 'tipoFirma';
    
    /* Datos */
    var textos = null;
    var flujoCircuito = null;
    var datosComboTipoFirma = null;
    var tabCircuitosFirmaPersonalizada = null;
    var datosTabCircuitoFirmaPersonalizada = null;
    var idTipoFirmaSeleccionado = null;
    var maxNumOrden = null;
    
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

        try {
            datosComboTipoFirma = opciones.datosComboTipoFirma;
            
            // Los datos del flujo pueden venir desde la jsp o como argumento
            // desde la ventana principal.
            if (opciones.datosFlujoUsuariosFirma) {
                flujoCircuito = JSON.parse(opciones.datosFlujoUsuariosFirma);
            } else { // Hubo algun error al cargar los datos del flujo
                throw "Error al intentar obtener los datos del flujo";
            }
            
            datosTabCircuitoFirmaPersonalizada = convertirFirmaCircuitoATabla(flujoCircuito.usuariosCircuito);
            maxNumOrden = datosTabCircuitoFirmaPersonalizada.length;

            // Tipo firma
            crearRadioTipoFirma();

            // Tabla circuito de firmas
            crearTablaCircuitosFirmaPersonalizada();

            document.onmouseup = checkKeys;
            window.focus();
            pleaseWait('off');
        } catch (err) {
            jsp_alerta('A', textos.msjErrorInternoAplicacion);
        }
    };

    // Convierte un objeto de tipo firmaCircuitoVO (JAVA) a una array para el datatable
    function convertirFirmaCircuitoATabla(listaFirmaCircuitoVO) {
        var listaUsuarios = new Array();
        
        if (listaFirmaCircuitoVO) {
            for (var i = 0; i < listaFirmaCircuitoVO.length; i++) {
                listaUsuarios.push([
                    listaFirmaCircuitoVO[i].idUsuario,
                    listaFirmaCircuitoVO[i].orden,
                    listaFirmaCircuitoVO[i].nombreUsuario,
                    listaFirmaCircuitoVO[i].documentoUsuario
                ]);
            }
        }
        
        return listaUsuarios;
    }

    // Convierte un objeto de tipo firmaCircuitoVO (JAVA) a una array para el datatable
    function convertirTablaAFirmaCircuito(listaUsuarios) {
        var listaFirmaCircuitoVO = new Array();
        
        if (listaUsuarios) {
            for (var i = 0; i < listaUsuarios.length; i++) {
                listaFirmaCircuitoVO.push({
                    idUsuario: listaUsuarios[i][0],
                    orden: listaUsuarios[i][1],
                    nombreUsuario: listaUsuarios[i][2],
                    documentoUsuario: listaUsuarios[i][3]
                })
            }
        }
        
        return listaFirmaCircuitoVO;
    }

    // Crea los radiobuttons para el tipo de firma
    function crearRadioTipoFirma() {
        crearGrupoRadioButtons(ETIQUETA_GRUPO_RADIO_TIPO_FIRMA, NOMBRE_GRUPO_RADIO_TIPO_FIRMA, datosComboTipoFirma, textos.etiqModoFirma);
        seleccionarRadioTipoFirma(NOMBRE_GRUPO_RADIO_TIPO_FIRMA, flujoCircuito.flujo.idTipoFirma);
        
        $('input[name=' + NOMBRE_GRUPO_RADIO_TIPO_FIRMA + ']').change(function(e) {
            idTipoFirmaSeleccionado = $(this).val();
        });
    }

    // Selecciona el radio button dependiendo del valor
    function seleccionarRadioTipoFirma(grupo, valor) {
        if (grupo && valor) {
            var radiobuttons = $('input[name=' + grupo + '][value=' + valor + ']');
            
            if (radiobuttons) {
                $('input[name=' + grupo + '][value=' + valor + ']').prop('checked', true);
                idTipoFirmaSeleccionado = valor;
            }
        }
    }

    /* Crea la tabla de flujos de firma */
    function crearTablaCircuitosFirmaPersonalizada() {
        tabCircuitosFirmaPersonalizada = new Tabla(true, textos.buscar, textos.anterior, textos.siguiente
                                           , textos.mosFilasPag, textos.msgNoResultBusq
                                           , textos.mosPagDePags, textos.noRegDisp
                                           , textos.filtrDeTotal, textos.primero
                                           , textos.ultimo
                                           , document.getElementById('tablaCircuitosFirmaPersonalizada'));
                                           
        tabCircuitosFirmaPersonalizada.addColumna('0', 'center', ""); // idUsuario oculto del circuito
        tabCircuitosFirmaPersonalizada.addColumna('10', 'center', textos.eqTblCircuitosFirmaOrden);
        tabCircuitosFirmaPersonalizada.addColumna('70', 'center', textos.eqTblCircuitosFirmaUsuario);
        tabCircuitosFirmaPersonalizada.addColumna('20', 'center', textos.eqTblCircuitosFirmaDocumento);
        tabCircuitosFirmaPersonalizada.displayCabecera = true;
        
        resetPage(datosTabCircuitoFirmaPersonalizada);
    }

    /* Rellena la tabla de circuitos de firma con los datos pasados por parametro */
    function rellenarTablaCircuitosFirmaPersonalizada(datos) {
        if (datos) {
            tabCircuitosFirmaPersonalizada.lineas = datos;
        } else {
            tabCircuitosFirmaPersonalizada.lineas = new Array();
        }
        
        tabCircuitosFirmaPersonalizada.displayTabla();
    };

    /* Recarga la tabla de circuitos de firmas */
    function resetPage(datos) {
        rellenarTablaCircuitosFirmaPersonalizada(datos);        
    };

    // FUNCIONES DE PULSACION DE BOTONES
    function _pulsarAltaFirmanteCircuitoFirma() {
        eleccionAltaFirmante();
    };
    
    function _pulsarEliminarFirmanteCircuitoFirma() {
        if (tabCircuitosFirmaPersonalizada.selectedIndex !== -1) {
            if (jsp_alerta("C", textos.preguntaEliminarUsuarioCircuitoFirma) == 1) {
                eliminarFirmanteCircuitoFirma();
            }
        } else {
            jsp_alerta('A', textos.msjNoSelecFila);
        }
    };
    
    function _pulsarAceptarCircuitoFirma() {
        var valido = true;
        
        // Verificar que existe un modo de firma seleccionado
        if (!idTipoFirmaSeleccionado) {
            valido = false;
            jsp_alerta('A', textos.msjTipoFlujoFirmaObligatorio);
        }
        
        // Verificar que existe al menos un usuarios en el circuito
        if (!datosTabCircuitoFirmaPersonalizada || datosTabCircuitoFirmaPersonalizada.length < 1) {
            valido = false;
            jsp_alerta('A', textos.msjCircuitoFirmaUsuarioOblig);
        }
        
        if (valido) {
            retornarFlujoPersonalizado();
        }
    };
    
    function _pulsarCancelarCircuitoFirma() {
        volverPantallaPrincipal();
    };

    /* Anadir un usuario al circuito de firmas */
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
    
    // Inserta un nuevo firmante en el circuito
    function altaFirmanteCircuitoFirma(usuarioFirmante) {
        pleaseWait('on');
        
        var orden = maxNumOrden;

        var firmante = [
            usuarioFirmante.idUsuario,
            ++orden,
            usuarioFirmante.nombre,
            usuarioFirmante.documento
        ];

        datosTabCircuitoFirmaPersonalizada.push(firmante);
        maxNumOrden = orden;

        resetPage(datosTabCircuitoFirmaPersonalizada);
        
        pleaseWait('off');
    }
    
    // Comprueba si un usuario ya existe en el circuito
    function _comprobarUsuarioDuplicado(usuarioFirmante) {
        var duplicado = false;
        
        for (var i = 0; i < datosTabCircuitoFirmaPersonalizada.length; i++) {
            if (usuarioFirmante.idUsuario == datosTabCircuitoFirmaPersonalizada[i][0]) {
                duplicado = true;
                break;
            }
        }
        
        return duplicado;
    }
    
    /* Eliminar un firmante del circuito del flujo de firma */
    function eliminarFirmanteCircuitoFirma() {
        pleaseWait('on');

        var indiceBorrar = tabCircuitosFirmaPersonalizada.selectedIndex;
        if (indiceBorrar > -1) {
            datosTabCircuitoFirmaPersonalizada.splice(indiceBorrar, 1);
        }
        
        // Reordenacion del orden de los firmantes
        maxNumOrden = 0;
        for (var i = 0; i < datosTabCircuitoFirmaPersonalizada.length; i++) {
            datosTabCircuitoFirmaPersonalizada[i][1] = ++maxNumOrden;
        }
        
        resetPage(datosTabCircuitoFirmaPersonalizada);
        pleaseWait('off');
    }
    
    // Retorna a la pantalla principal y devuelve el flujo personalizado
    function retornarFlujoPersonalizado() {
        flujoCircuito.flujo.idTipoFirma = idTipoFirmaSeleccionado;
        flujoCircuito.usuariosCircuito = convertirTablaAFirmaCircuito(datosTabCircuitoFirmaPersonalizada);
        var json = JSON.stringify(flujoCircuito);
        
        volverPantallaPrincipal(json);
    }

    /* Funciones para teclas de acceso rapido */
    function _checkKeysLocal(evento, tecla) {
        var teclaAuxiliar = evento.which;
        var coordx = evento.clientX;
        var coordy = evento.clientY;

        keyDel(evento);
    };
}
