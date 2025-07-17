existeFechaFin = (!$('#fecha_fin').length) ? 0 : 1;

function comprobarFechas() {
    $("form").submit(function (e) {
        if (existeFechaFin) {
            var fechaInicioFechaFinOK = comprobarFechaInicioFechaFinOk($("#fecha_inicio"), $("#fecha_fin"));
            if (!fechaInicioFechaFinOK) {
                e.preventDefault();
                alert("La fecha de inicio es posterior a la fecha de fin");
            }
        }
    });
}

$(document).ready(function () {
    "use strict";

    var lang = ($("html").attr("lang") != "") ? $("html").attr("lang") : "es";
    var isDefined = $.fn.datetimepicker.defaults;

    if (typeof isDefined != "undefined") {
        $.fn.datetimepicker.defaults.locale = lang;
    }

    if ($('.datetimepicker.datetime').length) {
        $('.datetimepicker.datetime').datetimepicker({
            format: "YYYY-MM-DD HH:mm"
        });
    }

    if ($('.datetimepicker.date').length) {
        $('.datetimepicker.date').datetimepicker({
            format: "YYYY-MM-DD"
        });
    }

    if ($('.datetimepicker.time').length) {
        $('.datetimepicker.time').datetimepicker({
            format: "LT"
        });
    }

    //Procedemos a vincular las fechas inicio con las fechas fin (en caso de que haya)
    $("#fecha_inicio-content").each(function (i, obj) {
        $('#fecha_inicio-content').datetimepicker();
        $('#fecha_fin-content').datetimepicker({
            useCurrent: false
        });

        if (existeFechaFin) {
            $("#fecha_inicio-content").on("dp.change", function (e) {
                $("#fecha_fin-content").data("DateTimePicker").minDate(e.date);
            });
            $("#fecha_fin-content").on("dp.change", function (e) {
                $("#fecha_inicio-content").data("DateTimePicker").maxDate(e.date);
            });
        }
    });

    comprobarFechas();
});