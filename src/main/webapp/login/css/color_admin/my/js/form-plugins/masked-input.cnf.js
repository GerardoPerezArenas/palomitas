$(document).ready(function () {
    "use strict";
    if ($('.masked-input-date').length) {
        $(".masked-input-date").mask("99/99/9999");
    }
    if ($('.masked-input-phone').length) {
        $(".masked-input-phone").mask("(999) 999-9999");
    }

    if ($('.masked-input-tid').length) {
        $(".masked-input-tid").mask("99-9999999");
    }

    if ($('.masked-input-ssn').length) {
        $(".masked-input-ssn").mask("999-99-9999");
    }

    if ($('.masked-input-pno').length) {
        $(".masked-input-pno").mask("aaa-9999-a");
    }

    if ($('.masked-input-pkey').length) {
        $(".masked-input-pkey").mask("a*-999-a999");
    }
});