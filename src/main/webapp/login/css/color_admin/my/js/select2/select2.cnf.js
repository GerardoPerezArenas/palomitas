$(document).ready(function () {
    "use strict";
    if ($(".select2").length) {
        var lang = ($("html").attr("lang") != "") ? $("html").attr("lang") : "es";
        
        $(".select2").select2({
            placeholder: (jQuery.type($(this).attr("data-placeholder")) === "undefined") ? "" : $(this).attr("data-placeholder"),
            language: lang,
            width: '100%'
        });
    }
});