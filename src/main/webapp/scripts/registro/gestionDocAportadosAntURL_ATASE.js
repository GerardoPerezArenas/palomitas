/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function(){
    /*
     * $($("#aportadosAnterior td:contains('DescargaArchivo')[id!='tablaAnt']")[2]).append('<br /><a href="'+$($('#aportadosAnterior td:contains("DescargaArchivo")[id!="tablaAnt"]')[2]).text()+'" class="botonGeneral">  Ver Documento</a>')
     */
    agregarLinkDocumentosAportados();
});

function agregarLinkDocumentosAportados(){
    var celdas = $("#aportadosAnterior td:contains('/DescargaArchivo?fichero')[id!='tablaAnt']");
    for (var id = 0; id < celdas.length; id++) {
        var textHTMLInsertar='<br /><a href="#" class="botonGeneral" target="_blank">Ver Documento</a>';
        var $celdaJQuery = $(celdas[id]);
        var textoURl = $celdaJQuery.text();
//        if(textoURl.indexOf("https")>=0){
//            //continuamos, tiene https segura
//        }else{ // agregamos la s para evitar error de MIxed COntent
//            textoURl=textoURl.replace("http","https");
//        }
        //Agregamos la url
        textHTMLInsertar=textHTMLInsertar.replace("#",textoURl);
        $celdaJQuery.append(textHTMLInsertar);        
    }
}
