/*
 * Clase Uor
 */
function Uor(uor_cod, uor_cod_vis, uor_estado, uor_fecha_alta, uor_fecha_baja,
                uor_nom, uor_pad, uor_tipo, uor_email, uor_no_reg, uor_cod_accede,uor_rexistro_xeral,
                oficina_registro,uorOculta) {
    this.uor_cod = uor_cod;
    this.uor_pad = uor_pad;
    this.uor_nom = uor_nom;
    this.uor_tipo = uor_tipo;
    this.uor_fecha_alta = uor_fecha_alta;
    this.uor_fecha_baja = uor_fecha_baja;
    this.uor_estado = uor_estado;
    this.uor_cod_vis = uor_cod_vis;
    this.uor_email = uor_email;
    this.uor_no_reg = uor_no_reg;
    this.uor_cod_accede = uor_cod_accede;
    this.uor_rexistro_xeral = uor_rexistro_xeral;
    this.oficina_registro = oficina_registro;
    this.uorOculta = uorOculta;
}


// Métodos de utilidad

// devuelve el objeto tipo Uor con el cod_visible que corresponde al cod_vis y estado del argumento, si no, null
function buscarUorPorCodVisibleEstado(uors, codVis, estado) {
    for(i=0; i<uors.length; i++) {
        if((uors[i].uor_estado == estado)&&(uors[i].uor_cod_vis.toString() == codVis)) {            
            return uors[i];
        }
    }
    return null;
}

// devuelve el objeto tipo Uor con el cod_visible que corresponde al cod_vis
function buscarUorPorCodVisible(uors, codVis) {    
    for(i=0; i<uors.length; i++) {    
        if(uors[i].uor_cod_vis.toString() == codVis) {            
            return uors[i];
        }
    }
    return null;
}

// devuelve el objeto tipo Uor con el cod_visible que corresponde al cod_vis, estado y descripción del argumento, si no, null
function buscarUorPorCodVisibleEstadoDesc(uors, codVis, estado, desc) {
    for(i=0; i<uors.length; i++) {
        if((uors[i].uor_estado == estado)&&(uors[i].uor_cod_vis.toString() == codVis)
                &&(uors[i].uor_nom == desc)) {                
            return uors[i];
        }
    }
    return null;
}

// devuelve el objeto tipo Uor con el código (real) de UOR
function buscarUorPorCod(codigo) {
    for(i=0; i<uors.length; i++) {
        if(uors[i].uor_cod.toString() == codigo) {            
            return uors[i];
        }
    }
    return null;
}

// Devuelve el código visible contenido en el texto mostrado como nodo del árbol de UORs, e.d.,
// del nodo con texto "AAA - Departamento de medios" devuelve "AAA"
function nodo2CodVis(texto_nodo) {
    var posicion = texto_nodo.indexOf(" - ");

    if(posicion == -1) {
        return null;
    }

    return texto_nodo.substring(0, posicion);
}

// Devuelve el nombre o descripción contenido en el texto mostrado como nodo del árbol de UORs, e.d.,
// del nodo con texto "AAA - Departamento de medios" devuelve "AAA"
function nodo2Descripcion(texto_nodo) {
    var posicion = texto_nodo.indexOf(" - ");

    if(posicion == -1) {
        return null;
    }

    return texto_nodo.substring(posicion + 3, texto_nodo.length + 1);
}
