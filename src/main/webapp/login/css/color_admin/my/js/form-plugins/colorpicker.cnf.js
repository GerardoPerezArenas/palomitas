$(document).ready(function() {    
    "use strict";
    if ($('.colorpicker').length) {
        $('.colorpicker').colorpicker({format: 'hex'});
    }
    if ($('.colorpicker-prepend').length) {
        $('.colorpicker-prepend').colorpicker({format: 'hex'});
    }
    if ($('.colorpicker-rgba').length) {
        $('.colorpicker-rgba').colorpicker();
    }
});