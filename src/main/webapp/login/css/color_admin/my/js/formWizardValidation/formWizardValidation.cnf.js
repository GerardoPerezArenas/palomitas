/**
 * Recoge la altura del contenido del iframe, y hace que este mida lo que mide el contenido.
 * 
 * @param {int} index Indice del iframe activo
 * 
 * @returns {undefined}
 */
function resizeIframes(index) {
    if (!$.isNumeric(index)) {
        index = 0;
    }
    index += 1;

    /*$(".tab-content iframe").height(0);//Ponemos el tamaño de los iframes a cero para todos, que al principio están ocultos.
     $(".tab-content iframe").each(function () {
     $(this).height($(this).contents().height() + 15);
     });*/

    //var iframeActivo = "#step" + index + " iframe:not(.vzaar-video-player)";
    //$(iframeActivo).height($(iframeActivo).contents().height() + 20);

    var iframeActivo = $("#step" + index + " iframe").attr("id");
    iframeActivo = document.getElementById(iframeActivo);

    iframeActivo.style.height = iframeActivo.contentWindow.document.body.scrollHeight + 'px';
}

function progressBar(ui) {
    var panel = ui.panel;
    var total = $(panel).parent().children().length;
    var current = ui.index + 1;
    var percent = (current / total) * 100;
    $(".wizard-nav li.active .progress-bar").css({width: percent + '%'});
    $(".wizard-nav li.active .progress-bar").html(parseInt(percent) + '%');
}

function pagination(index) {
    $(".wizard .bwizard.active .wizard-current").html(index + 1);
}

function paginationCreate() {
    $.each($(".wizard .tab-content").children(), function () {
        var total = $(this).children(".well").children().length;
        $(this).find(".pager.bwizard-buttons .previous").after(
                '<li><div class="display-inline-block"><span class="wizard-current">1</span>/' + total + '</div></li>'
                );
    });
}

function validarWizard(formularioAValidar) {
    if (false === $('.wizard-form').parsley().validate(formularioAValidar)) {
        return false;
    }

    return true;
}

/**
 * Si hay videos de vzaar dentro de las pestañas, en Firefox puede que no se vean
 * Hay que actulizar el SRC para que se vea bien
 * 
 * @returns {undefined}
 */
function setVzaarVideo() {
    if ($.browser.mozilla) {
        $(".bwizard-activated .vzaar-video-player").each(function () {
            var isActualizado = ($(this).attr("data-src") == 1) ? 1 : 0;
            if (!isActualizado) {
                var src = $(this).attr("src");
                $(this).attr("src", "");
                $(this).attr("src", src);
                $(this).attr("data-src", 1);
            }
        });
    }
}

function handleBootstrapWizardsValidation() {
    var backBtnText = '<i class="fa fa-2x fa-arrow-left"></i>';
    var nextBtnText = '<i class="fa fa-2x fa-arrow-right"></i>';


    if ($("#formacion-wizard").length) {
        $("#formacion-wizard").bwizard({
            backBtnText: backBtnText,
            nextBtnText: nextBtnText,
            show: function (e, ui) {
                resizeIframes(ui.index); //Ajustamos la altura del iframe activo
                progressBar(ui);
                pagination(ui.index);
                setVzaarVideo();
            }
        });
    }

    if ($("#formacion-test-wizard").length) {
        $("#formacion-test-wizard").bwizard({
            backBtnText: backBtnText,
            nextBtnText: nextBtnText,
            show: function (e, ui) {
                resizeIframes(ui.index); //Ajustamos la altura del iframe activo
                progressBar(ui);
                pagination(ui.index);
                setVzaarVideo();
            }
        });
    }

    if ($("#ejecucion-wizard").length) {
        $("#ejecucion-wizard").bwizard({
            backBtnText: backBtnText,
            nextBtnText: nextBtnText,
            show: function (e, ui) {
                resizeIframes(ui.index); //Ajustamos la altura del iframe activo
                progressBar(ui);
                pagination(ui.index);
                setVzaarVideo();
            }/*,
             validating: function (e, ui) {
             if ($('#wizard-form').length) {
             if (ui.index == 0) {
             //return validarWizard('planificacion');
             }
             if (ui.index == 1) {
             return validarWizard('registro');
             }
             }
             }*/
        });
    }

    if ($("#analisis_mejora-wizard").length) {
        $("#analisis_mejora-wizard").bwizard({
            backBtnText: backBtnText,
            nextBtnText: nextBtnText,
            show: function (e, ui) {
                resizeIframes(ui.index); //Ajustamos la altura del iframe activo
                progressBar(ui);
                pagination(ui.index);
                setVzaarVideo();
            }
        });
    }
}

$(document).ready(function () {
    "use strict";

    if ($(".wizard").length) {
        handleBootstrapWizardsValidation();
        paginationCreate();
    }

    $(".wizard .nav.nav-tabs a").click(function () {
        var Url = $(this).attr("href");
        window.location.href = Url;
    });
});

$('iframe').on('load', function () {
    //Ajustamos la altura del primer iframe
    resizeIframes(0);
});