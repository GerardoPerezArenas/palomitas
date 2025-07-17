$(document).ready(function () {
    "use strict";
    $('.bootstrap-tagsinput input').focus(function () {
        $(this).closest('.bootstrap-tagsinput').addClass('bootstrap-tagsinput-focus');
    });

    $('.bootstrap-tagsinput input').focusout(function () {
        $(this).closest('.bootstrap-tagsinput').removeClass('bootstrap-tagsinput-focus');
    });
});