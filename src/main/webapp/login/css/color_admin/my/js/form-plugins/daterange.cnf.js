$(document).raedy(function() {
    "use strict";    
    if ($('.default-daterange').length) {
        $('.default-daterange').daterangepicker({
            opens: 'right',
            format: 'DD/MM/YYYY',
            separator: ' hasta ',
            parentEl: '.default-daterange',
            startDate: moment().subtract('days', 29),
            endDate: moment(),
            locale: {
                applyLabel: 'Aplicar',
                cancelLabel: 'Cancelar',
                fromLabel: 'Desde',
                toLabel: 'Hasta',
                weekLabel: 'S',
                customRangeLabel: 'Rango personalizado',
                daysOfWeek: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
                monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
                firstDay: 1
            }
        },
        function (start, end) {
            $('.default-daterange input[name="start"]').val(start.format('DD/MM/YYYY'));
            $('.default-daterange input[name="end"]').val(end.format('DD/MM/YYYY'));
        });
    }

    if ($('.advance-daterange span').length) {
        $('.advance-daterange span').html(moment().subtract('days', 29).format('D MMMM, YYYY') + ' - ' + moment().format('D MMMM, YYYY'));
    }

    if ($('.advance-daterange').length) {
        $('.advance-daterange').daterangepicker({
            format: 'DD/MM/YYYY',
            startDate: moment().subtract(29, 'days'),
            endDate: moment(),
            dateLimit: {days: 60},
            showDropdowns: true,
            showWeekNumbers: true,
            timePicker: false,
            timePickerIncrement: 1,
            timePicker12Hour: true,
            ranges: {
                'Hoy': [moment(), moment()],
                'Ayer': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                'Últimos 7 Días': [moment().subtract(6, 'days'), moment()],
                'Últimos 30 Días': [moment().subtract(29, 'days'), moment()],
                'Este Mes': [moment().startOf('month'), moment().endOf('month')],
                'Mes pasado': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
            },
            opens: 'right',
            drops: 'down',
            buttonClasses: ['btn', 'btn-sm'],
            applyClass: 'btn-primary',
            cancelClass: 'btn-default',
            separator: ' hasta ',
            locale: {
                applyLabel: 'Aplicar',
                cancelLabel: 'Cancelar',
                fromLabel: 'Desde',
                toLabel: 'Hasta',
                weekLabel: 'S',
                customRangeLabel: 'Rango personalizado',
                daysOfWeek: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
                monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
                firstDay: 1
            }
        }, function (start, end, label) {
            $('.advance-daterange span').html(start.format('DD/MM/YYYY') + ' - ' + end.format('DD/MM/YYYY'));
        });
    }
});