/**
 * Marcamos la primera tab y el primer contenedor como activos, 
 * ya que el plugin no lo hace automaticamente
 */
$(document).ready(function () {
    //Evitamos el caso de que el primer elemento sea la flecha de desplazamiento
    $(".nav-tabs > :first-child:not(.prev-button)").addClass("active");
    //En caso de que haya una flecha de desplazamiento, marcamos el siguiente elemento
    $(".nav-tabs > .prev-button:first-child + li").addClass("active");
    $(".tab-content > :first-child").addClass("active in");
});