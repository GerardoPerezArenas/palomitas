/**
 * Llama a la funcion que el elemento toggleSwitch tenga que llamar
 * 
 * @param {function} _function Nombre de la funcion que se va a ejecutar
 * @returns {undefined}
 */
function toggleSwitchChange(_function) {
    $('.switch-radio1').on('switchChange.bootstrapSwitch', function (event, state) {
        _function($(this));
        //@todo Pendiente de implementar un fallback en caso de que la funcion falle
        //Se espera que la version 3.3.3 del plugin bootstrapSwitch incluya un fallback
    });
}

$(document).ready(function () {
    $('.switch-radio1').bootstrapSwitch();
}); 