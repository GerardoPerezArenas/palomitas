/**
 * Devuelve la URL base
 * 
 * @returns {String} Url Base
 */
function getBaseUrl() {
    var fullUrl = window.location.href;
    var fullUrlArr = fullUrl.split("index.php");
    return fullUrlArr[0];
}

function configFull() {
    var toolbar = [
        {name: 'document', items: ['Source']},
        {name: 'clipboard', items: ['Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo']},
        {name: 'basicstyles', items: ['Bold', 'Italic', '-', 'RemoveFormat']},
        {name: 'paragraph', items: ['NumberedList', 'BulletedList', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock']},
        {name: 'links', items: ['Link', 'Unlink']},
        {name: 'insert', items: ['Image', 'Flash', 'Table', 'Iframe', 'AddLayout']},
        {name: 'styles', items: ['Styles', 'Format', 'FontSize']}
    ];

    var config = {
        "toolbar": toolbar,
        "layoutmanager_loadbootstrap": true,
        "extraPlugins": 'layoutmanager'
    };

    return config;
}

function configSimple() {
    var toolbar = [
        {name: 'document', items: ['Source']},
        {name: 'clipboard', items: ['Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo']},
        {name: 'basicstyles', items: ['Bold', 'Italic', '-', 'RemoveFormat']},
        {name: 'paragraph', items: ['NumberedList', 'BulletedList', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock']},
        {name: 'links', items: ['Link', 'Unlink']},
        {name: 'insert', items: ['Image', 'Flash', 'Table', 'Iframe', 'AddLayout']},
        {name: 'styles', items: ['Styles', 'Format', 'FontSize']}
    ];

    var config = {
        "toolbar": toolbar,
        "layoutmanager_loadbootstrap": true,
        "extraPlugins": 'layoutmanager'
    };

    return config;
}

function configExtraUploads() {
    var baseUrl = getBaseUrl();
    var roxyFileman = baseUrl + 'application/views/resources/color_admin/my/plugins/ckeditor/plugins/fileman/index.php?integration=ckeditor';
    var http = new XMLHttpRequest();

    http.open('HEAD', roxyFileman, false);
    http.send();
    var fileExists = http.status != 404;

    if (!fileExists) {
        console.log("Fallo en la configuración de CKEditor Uploads.");
    }

    var config = {
        "filebrowserBrowseUrl": roxyFileman,
        "filebrowserImageBrowseUrl": roxyFileman + '&type=image',
        "removeDialogTabs": 'link:upload;image:upload'
    };

    return config;
}

/**
 * Configuracion principal
 * 
 * @param {string} CKEditor Id del textarea
 * @returns {array} Configuracion para el CKEditor
 */
function createConfig(CKEditor) {
    var config = (typeof ($(CKEditor).hasClass("ckeditor-simple") == "undefined")) ? configFull() : configSimple();
    var configExtra = ($(CKEditor).hasClass("ckeditor-with-upload")) ? configExtraUploads() : "";

    //Hacemos un merge de los 2 objetos
    $.extend(config, configExtra);

    return config;
}

$(document).ready(function () {
    "use strict";
    var ckeditor = '.ckeditor';
    if ($(ckeditor).length) {
        /* CONFIGURACION POR DEFECTO */

        CKEDITOR.config.allowedContent = true;

        //Aplicamos el fondo blanco para que otros estilos no lo sobrecarguen
        CKEDITOR.config.bodyClass = 'bg-white';

        //Actualizamos el desplegable de clases
        CKEDITOR.stylesSet.add('color_admin', [
            // Block-level styles.
            {name: 'IMG con ancho 100%', element: 'img', attributes: {'class': 'width-full'}},
            {name: 'Posicionar izquierda', element: 'p', attributes: {'class': 'pull-left'}},
            {name: 'Posicionar derecha', element: 'p', attributes: {'class': 'pull-right'}}
            // Inline styles.
            //{name: 'CSS Style', element: 'span', attributes: {'class': 'my_style3'}}
        ]);
        CKEDITOR.config.stylesSet = 'color_admin';

        //Actualizamos el desplegable de los Formatos
        CKEDITOR.config.format_tags = 'p;h1;h2;h3;h4;h5;h6';

        //No se puede cambiar el tamaño del textarea
        CKEDITOR.config.resize_enabled = false;

        //Actualizamos el listado de tamaños
        CKEDITOR.config.fontSize_sizes = '10px/f-s-1-0-r;11px/f-s-1-1-r;12px/f-s-1-2-r;13px/f-s-1-3-r;14px/f-s-1-4-r;15px/f-s-1-5-r;16px/f-s-1-6-r;17px/f-s-1-7-r;18px/f-s-1-8-r;19px/f-s-1-9-r;20px/f-s-2-0-r;21px/f-s-2-1-r;22px/f-s-2-2-r;23px/f-s-2-3-r;24px/f-s-2-4-r;25px/f-s-2-5-r;26px/f-s-2-6-r;27px/f-s-2-7-r;28px/f-s-2-8-r;29px/f-s-2-9-r;30px/f-s-3-0-r;';
        CKEDITOR.config.fontSize_style = {
            element: 'span',
            attributes: {'class': '#(size)'},
            overrides: [{element: 'font', attributes: {'size': null}}]
        };

        //Sobrecargamos los botones de alineamiento para que añadan una clase en vez de un style
        CKEDITOR.config.justifyClasses = ['text-left', 'text-center', 'text-right', 'text-justify'];

        CKEDITOR.on('dialogDefinition', function (ev) {
            var dialogName = ev.data.name;
            var dialogDefinition = ev.data.definition;

            if (dialogName == 'image') {
                //Agregamos por defecto la clase max-width-full a las imagenes
                var advTab = dialogDefinition.getContents('advanced');
                var cssField = advTab.get('txtGenClass');
                cssField['default'] = 'max-width-full';
            }
        });
        /* /CONFIGURACION POR DEFECTO */

        var baseUrl = getBaseUrl();

        /* CONFIGURACION VARIABLE */
        $(ckeditor).each(function () {
            var Id = $(this).attr("id");

            var config = createConfig($(this));
            CKEDITOR.replace(Id, {
                on: {
                    instanceReady: function (ev) {
                        //Agregamos los estilos extendidos de color_admin
                        this.document.appendStyleSheet(baseUrl + 'application/views/resources/color_admin/assets/css/style.min.css');
                        this.document.appendStyleSheet(baseUrl + 'application/views/resources/color_admin/my/css/style.css');

                        ev.editor.dataProcessor.htmlFilter.addRules({
                            elements: {
                                $: function (element) {
                                    // Remove style attribute from images
                                    if (element.name == 'img') {
                                        delete element.attributes.style;
                                    }

                                    return element;
                                }
                            }
                        });
                    }
                },
                toolbar: (typeof (config["toolbar"]) == "undefined") ? "" : config["toolbar"],
                filebrowserBrowseUrl: (typeof (config["filebrowserBrowseUrl"]) == "undefined") ? "" : config["filebrowserBrowseUrl"],
                filebrowserImageBrowseUrl: (typeof (config["filebrowserImageBrowseUrl"]) == "undefined") ? "" : config["filebrowserImageBrowseUrl"],
                removeDialogTabs: (typeof (config["removeDialogTabs"]) == "undefined") ? "" : config["removeDialogTabs"],
                extraPlugins: (typeof (config["extraPlugins"]) == "undefined") ? "" : config["extraPlugins"],
                layoutmanager_loadbootstrap: (typeof (config["layoutmanager_loadbootstrap"]) == "undefined") ? "" : config["layoutmanager_loadbootstrap"]
            });
        });
        /* /CONFIGURACION VARIABLE */
    }
});