  var enlacesPagina  = 4;
  var lineasPagina   = 10;

  var paginaActual   = 1;
  var paginaInferior = 1;
  var paginaSuperior = enlacesPagina;

  var inicio = 0;
  var fin    = 0;

  var listaOriginal = new Array();
  var lista         = new Array();
  var listaOrDoms = new Array();
  var listaDoms = new Array();

  var Terceros = new Array();
  var lCasas = new Array();
  var TerceroSel = new Array();


  // Cargamos el vector de datos del frame "principal"
  //Terceros = window.dialogArguments;//opener.Tercero;
  //alert(Terceros);

/*
function derecha(e) {
   if (navigator.appName == 'Netscape' && (e.which == 3 || e.which == 2))
   {
      alert('Botón derecho inhabilitado')
      return false;
   }
   else if (navigator.appName == 'Microsoft Internet Explorer' && (event.button == 2))
   {
      alert('Botón derecho inhabilitado')
   }
}

document.onmousedown=derecha
*/

  function inicializaListaCasas(numeroPagina){
    var listaP = new Array();
    var j = 0;

    paginaActual = numeroPagina;
    inicio = (numeroPagina - 1) * lineasPagina;
    fin = Math.min(numeroPagina * lineasPagina,listaDoms.length);
    for (var i=inicio;i<fin;i++){
      listaP[j] = listaDoms[i];
      j++;
    }
    tabD.lineas=listaP;
    refreshD();
    //domlay('enlace',1,0,0,enlaces());
  }

  function buscar(){
    var j = 0;
    var auxCod = "";
    var auxDes = "";
    var len = document.forms[0].codigo.value.length;

    listaSel = new Array();
    for (var i=0; i<lista.length; i++){
      auxCod = listaOriginal[i][0];
      auxDes = listaOriginal[i][1].toUpperCase();
      if ((auxCod.substring(0,len) == document.forms[0].codigo.value) &&
          (auxDes.search(document.forms[0].descripcion.value.toUpperCase()) != -1)){
        listaSel[j] = lista[i];
        j++;
      }
    }
    inicializaLista(1);
  }

  function limpiaDatos(){
    document.forms[0].codigo.value = "";
    document.forms[0].descripcion.value = "";
    listaSel = lista;
    inicializaLista(1);
  }

  function enviaSeleccion(i){
    
    var nombre = formatearNombre(Terceros[i][4], Terceros[i][5], Terceros[i][6]);
    
    TerceroSel = [Terceros[i][0],Terceros[i][1],Terceros[i][0],Terceros[i][1],Terceros[i][2],Terceros[i][3],nombre ,
        Terceros[i][5],Terceros[i][6],Terceros[i][7],Terceros[i][8],Terceros[i][9],Terceros[i][10]];
    cerrar();
  }

  function enviaSeleccionDoms(i,j){
      
      var nombre = formatearNombre(Terceros[i][4], Terceros[i][5], Terceros[i][6]);
      
      var domicilio = "";
      if (lCasas[j][29] == "") {
          domicilio += lCasas[j][3];                                                                // EMPLAZAMIENTO = DOMICILIO
      } else {
        domicilio = (lCasas[j][20]!="") ? domicilio+lCasas[j][20]+" ":domicilio;        // DESCRIPCION DEL TIPO DE VIA
        domicilio = (lCasas[j][29]!="") ? domicilio+lCasas[j][29]+" ":domicilio;        // DESCRIPCION DE LA VIA
        domicilio = (lCasas[j][9]!=0) ? domicilio+" "+lCasas[j][9]:domicilio;           // PRIMER NUMERO DE LA VIA
        domicilio = (lCasas[j][10]!="") ? domicilio+" "+lCasas[j][10]+" ":domicilio;    // PRIMERA LETRA DE LA VIA
        domicilio = (lCasas[j][11]!=0) ? domicilio+" "+lCasas[j][11]:domicilio;         // ULTIMO NUMERO DE LA VIA
        domicilio = (lCasas[j][12]!="") ? domicilio+" "+lCasas[j][12]:domicilio;        // ULTIMA LETRA DE LA VIA
        domicilio = (lCasas[j][13]!="") ? domicilio+" Bl. "+lCasas[j][13]:domicilio;    // BLOQUE
        domicilio = (lCasas[j][14]!="") ? domicilio+" Portal "+lCasas[j][14]:domicilio; // PORTAL
        domicilio = (lCasas[j][15]!="") ? domicilio+" Esc. "+lCasas[j][15]:domicilio;   // ESCALERA
        domicilio = (lCasas[j][16]!="") ? domicilio+" "+lCasas[j][16]+"º ":domicilio;   // PLANTA
        domicilio = (lCasas[j][17]!="") ? domicilio+lCasas[j][17]:domicilio;            // PUERTA
      }

    TerceroSel = [Terceros[i][0],Terceros[i][1],Terceros[i][0],Terceros[i][1],Terceros[i][2],Terceros[i][3],nombre,
        Terceros[i][5],Terceros[i][6],Terceros[i][7],Terceros[i][8],Terceros[i][9],Terceros[i][10],
        lCasas[j][5],lCasas[j][5],lCasas[j][0],lCasas[j][1],lCasas[j][2],domicilio,lCasas[j][4],lCasas[j][18], Terceros[i][4]];
    cerrar();
  }
  
  function formatearNombre(nombre, apellido1, apellido2) {
      var nombreFormateado = "";
      if (apellido1!="" && apellido1 != undefined) nombreFormateado += apellido1.trim();
      if (apellido2!="" && apellido2 != undefined) {
          if (nombreFormateado.length > 0) nombreFormateado += " " + apellido2.trim();
          else nombreFormateado = apellido2.trim();
      }
      if (nombre!="" && nombre != undefined) {
          if (nombreFormateado.length > 0) nombreFormateado += ", " + nombre.trim();
          else nombreFormateado = nombre.trim();
      }
      return nombreFormateado;
  }

