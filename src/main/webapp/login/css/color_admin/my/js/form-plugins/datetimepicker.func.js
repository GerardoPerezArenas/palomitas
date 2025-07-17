/**
 * Parche que para que funcionen los rangos de fechas con formato YYYY-m-d
 * 
 * @param {type} datum Fecha en formato Y-m-d H:i:s
 * @returns {String} Fecha en formato Y/m/d
 */
function getDate(datum) {
    fn = datum.split(" ");
    fn = fn[0].split("-");

    return fn[0] + '/' + fn[1] + '/' + fn[2];
}

/**
 * Parche que para que funcionen los rangos de horas con formato H:i
 * 
 * @param {string} datum Fecha en formato Y-m-d H:i:s
 * @returns {String} Hora en formato H:i
 */
function getTime(datum) {
    fn = datum.split(" ");
    //En caso de que no haya hora la ponemos a 0
    if (typeof (fn[1]) == "undefined") {
        fn[1] = "00:00:00";
    }

    fn = fn[1].split(":");

    return fn[0] + ":" + fn[1];
}

/**
 * Comprueba si 2 fechas son la misma
 * 
 * @param {JQUERY obj} fechaInicio Fecha de inicio
 * @param {JQUERY obj} fechaFin Fecha de fin
 * @returns {Boolean} Devuelve 1 si las fechas son las mismas, 0 si no lo son
 */
function comprobarMismaFecha(fechaInicio, fechaFin) {
    var dateInicio = getDate($(fechaInicio).val());
    var dateFin = getDate($(fechaFin).val());

    return (dateInicio == dateFin) ? 1 : 0;

}

/**
 * Configura que la fechaInicio no sea posterior que la fechaFin y que la fechaFin
 * no sea anterior a la fechaInicio
 * 
 * @param {date} fechaInicio
 * @param {date} fechaFin
 * @returns {undefined}
 */
function comprobarFechaInicioFechaFin(fechaInicio, fechaFin) {
    $(fechaInicio).datetimepicker({
        maxDate: $(fechaFin).val() ? getDate($(fechaFin).val()) : false
    });
    $(fechaFin).datetimepicker({
        minDate: $(fechaInicio).val() ? getDate($(fechaInicio).val()) : false
    });
}

/**
 * Configura que la horaInicio no sea posterior que la horaFin y que la horaFin
 * no sea anterior a la horaInicio
 * 
 * @param {time} horaInicio
 * @param {time} horaFin
 * @returns {undefined}
 */
function comprobarHoraInicioHoraFin(horaInicio, horaFin) {
    var esMismaFecha = comprobarMismaFecha($(horaInicio), $(horaFin));

    if (esMismaFecha) {
        $(horaInicio).datetimepicker({
            maxTime: getTime($(horaFin).val())
        });

        $(horaFin).datetimepicker({
            minTime: getTime($(horaInicio).val())
        });
    } else {
        $(horaInicio).datetimepicker({
            maxTime: "23:59"
        });

        $(horaFin).datetimepicker({
            minTime: "00:00"
        });
    }
}

/**
 * Comprueba que a la hora de enviar el formulario, la fecha de inicio sea interior
 * a la fecha de fin
 * 
 * @param {Datetime} fechaInicio
 * @param {Datetime} fechaFin
 * @returns {Boolean}
 */
function comprobarFechaInicioFechaFinOk(fechaInicio, fechaFin) {
    var datePartsI = $(fechaInicio).val().split(' ');
    if (typeof (datePartsI[1]) == "undefined") {
        datePartsI[1] = '00:00:00';
    }
    var dateI = new Date(datePartsI[0] + "T" + datePartsI[1]);
    
    var datePartsF = $(fechaFin).val().split(' ');
    if (typeof (datePartsF[1]) == "undefined") {
        datePartsF[1] = '00:00:00';
    }
    var dateF = new Date(datePartsF[0] + "T" + datePartsF[1]);

    return (dateI > dateF) ? 0 : 1;
}