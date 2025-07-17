/**
 * Carga la configuracion BASICA del calendario
 * 
 * @param {string} defaultView Vista por defecto del calendario
 * @param {date} date Fecha a la que hay que ir en caso de que no estemos en el dia de hoy
 * @returns {undefined} No devuelve nada
 */
function cargarCalendario(defaultView, date) {
    var tipoSemana = "basicWeek"; //basicWeek / agendaWeek
    var tipoDia = "agendaDay"; //basicDay / agendaDay
    var lang = ($("html").attr("lang") != "") ? $("html").attr("lang") : "es";

    $('#calendario').fullCalendar({
        firstDay: 1,
        lang: lang,
        defaultView: defaultView,
        height: "auto",
        contentHeight: "auto",
        allDaySlot: false,
        fixedWeekCount: false,
        slotEventOverlap: true,
        header: {
            left: "prev,today,next",
            center: "title",
            right: /*tipoDia + "," + */tipoSemana + ",month"
        },
        titleFormat: {
            week: "D MMMM [de] YYYY",
            month: "MMMM [de] YYYY",
            day: "D MMMM [de] YYYY"
        },
        events: {
            error: function () {
                $("#error_cargando_eventos").show();
            }
        },
        loading: function (Cargando) {
            if (Cargando) {
                //Mostramos una imagen de loading
                $("#cal-loading").remove();
                $("#calendario").css("visibility", "hidden");
                $("#calendario").prev().append('<p id="cal-loading" class="text-center m-t-40 pos-relative"><span class="spinner m-0 m-t-40"></span></p>');
            } else {
                $("#cal-loading").remove();
                $("#calendario").css("visibility", "visible");
            }
        },
        eventAfterAllRender: function () {
            //Marcamos el dia actual en la fila de dias de la semana
            $(".fc-widget-header th:nth-child(" + ($(".fc-today").index() + 1) + ")").addClass("fc-today");
        },
        eventRender: function (event, element) {
            element.find('.fc-title').html(event.title);
        }
    });

    $("#calendario").fullCalendar('gotoDate', date);

}

$(document).ready(function () {
    var defaultView = "basicWeek"; //month / basicWeek / basicDay / agendaWeek /agendaDay
    cargarCalendario(defaultView, $.now());
});