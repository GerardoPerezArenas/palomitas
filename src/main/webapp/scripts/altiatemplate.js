/*
function verAyuda() {
	alert('No hay ayuda definida para esta aplicación');
}*/

function verAyuda(idioma)
{
    if (idioma == 1)
    {
        jsp_alerta("A",'En este momento no se encuentra ayuda disponible');
    } else {
        jsp_alerta("A",'Nestes intres non se atopa axuda dispoñíbel');
    }
    
}// verAyuda

function salir(texto) {
	var resultado = jsp_alerta("", texto);
	if (resultado == 1) {
	    document.location.href=APP_CONTEXT_PATH+'/SalirApp.do?app=11';
	}
}
