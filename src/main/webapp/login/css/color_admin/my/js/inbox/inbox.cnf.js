$(document).ready(function () {
    "use strict";
    $('[data-click=email-select-all]').click(function (e) {
        e.preventDefault();
        if ($(this).closest('tr').hasClass('active')) {
            $('.table-email tr').removeClass('active');
        } else {
            $('.table-email tr').addClass('active');
        }
    });

    $('[data-click=email-select-single]').click(function (e) {
        e.preventDefault();
        var targetRow = $(this).closest('tr');
        if ($(targetRow).hasClass('active')) {
            $(targetRow).removeClass('active');
        } else {
            $(targetRow).addClass('active');
        }
    });

    $('[data-click=email-remove]').click(function (e) {
        e.preventDefault();
        var targetRow = $(this).closest('tr');
        $(targetRow).fadeOut().remove();
    });

    $('[data-click=email-highlight]').click(function (e) {
        e.preventDefault();
        if ($(this).hasClass('text-danger')) {
            $(this).removeClass('text-danger');
        } else {
            $(this).addClass('text-danger');
        }
    });
})