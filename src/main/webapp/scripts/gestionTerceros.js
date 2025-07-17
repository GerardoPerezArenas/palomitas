/// FUNCIONES PARA LIMPIAR LA PAGINA
/// --------------------------------
// constante definida en general.js

function limpiarListaTerceros(){
   tab.lineas = new Array();
   tab.displayTabla();
}

function limpiarDomicilio(){
   document.forms[0].txtIdDomicilio.value = "";
   /*document.forms[0].codPais.value = "";*/ //Es usado en BusquedaTercerosAction, tiene valor por defecto, no borrar
   document.forms[0].descPais.value = "";
   document.forms[0].codProvincia.value = ""; //Es usado en VialesAction
   document.forms[0].descProvincia.value = "";
   document.forms[0].codMunicipio.value = ""; // Es usado en VialesAction
   document.forms[0].descMunicipio.value = "";
   document.forms[0].txtCodVia.value = "";
   document.forms[0].descVia.value = "";
   document.forms[0].codTVia.value	= "";
   document.forms[0].descTVia.value = "";
   document.forms[0].txtDomicilio.value = "";
   document.forms[0].txtNumDesde.value = "";
   document.forms[0].txtLetraDesde.value = "";
   document.forms[0].txtNumHasta.value = "";
   document.forms[0].txtLetraHasta.value = "";
   document.forms[0].txtBloque.value = "";
   document.forms[0].txtPortal.value = "";
   document.forms[0].txtEsc.value = "";
   document.forms[0].txtPlta.value	= "";
   document.forms[0].txtPta.value = "";
   document.forms[0].txtBarriada.value = "";
   document.forms[0].descPostal.value = "";
   document.forms[0].domPrincipal.checked = false;

   limpiarECOESIVIA();
   comboProvincia.selectItem(-1);
   comboMunicipio.selectItem(-1);
   comboMunicipio.addItems([],[]);
   comboPostal.addItems([],[]);
   limpiaCodPostal();
}

function limpiarInteresado(){
   document.forms[0].codTerc.value = "";
   document.forms[0].txtIdTercero.value = "";
   document.forms[0].situacion.value = "";
   document.forms[0].txtVersion.value = "";
   document.forms[0].codTipoDoc.value = "";
   document.forms[0].descTipoDoc.value = "";
   comboTipoDoc.selectItem(-1);
   document.forms[0].txtDNI.value = "";
   document.forms[0].txtInteresado.value = "";
   document.forms[0].txtApell1.value = "";
   document.forms[0].txtPart.value = "";
   document.forms[0].txtApell2.value = "";
   document.forms[0].txtPart2.value = "";
   document.forms[0].txtTelefono.value = "";
   document.forms[0].txtCorreo.value = ""
   document.forms[0].terExpUnidad.checked = false;
}

function limpiarTodo(){
   limpiarInteresado();
   limpiarDomicilio();
   Terceros = new Array();
   datosTabla = new Array();
   limpiarListaTerceros();
   document.forms[0].pagina.value="1";
   document.forms[0].lineasPagina.value="10";
   limpiarCamposSuplementariosTerceroSinModoActual();
}

function limpiarECOESIVIA(){
   comboECO.selectItem(-1);
   comboESI.selectItem(-1);
   document.forms[0].txtCodVia.value="";
   document.forms[0].descVia.value="";
   limpiarVia();
}

function limpiaVial(){
    if (document.forms[0].txtCodVia.value!=""){
       // document.forms[0].txtDomicilio.value = "";
        document.forms[0].txtCodVia.value =	"";
        document.forms[0].codTVia.value = "";
    }
}

function limpiarVia(){
   if (document.forms[0].descVia.value==""){
       //document.forms[0].txtDomicilio.value = "";
       document.forms[0].txtCodVia.value =	"";
       document.forms[0].codTVia.value =	"";
   }
}

function limpiaCodPostal(){
    document.forms[0].descPostal.value = "";
    comboPostal.selectItem(-1);
}

/// FIN FUNCIONES PARA LIMPIAR LA PAGINA

/// FUNCIONES PARA HABILITAR/DESHABILITAR/MOSTRAR/OCULTAR
/// ---------------------------------

function mostrarCapaBotones(capa) {
    // Ocultar todas
    document.getElementById("capaBotonesBusqueda").style.display = "none";
    document.getElementById("capaBotonesSeleccion").style.display = "none";
    document.getElementById("capaBotonesAlta").style.display = "none";
    document.getElementById("capaBotonesAltaDomicilio").style.display = "none";
    document.getElementById("capaBotonesModificar").style.display = "none";

    //Mostrar la seleccionada
    document.getElementById(capa).style.display = "block";
}

function mostrarCapaDomicilios(valor) {
    if (valor) {
        document.getElementById("navegacionDoms").style.visibility = "visible";
        document.getElementById("operacionesDoms").style.visibility = "visible";
        mostrarOrdenDomicilio(true);
        mostrarAltaDomicilio(true);
    } else {
        document.getElementById("navegacionDoms").style.visibility = "hidden";
        document.getElementById("operacionesDoms").style.visibility = "hidden";
        document.getElementById("eliminarDom").style.visibility = "hidden";
        document.getElementById('nuevoDomicilio').style.visibility = 'hidden';
        mostrarOrdenDomicilio(false);
        mostrarAltaDomicilio(false);
    }
}

// Oculta/muestra el check de domicilio principal
function mostrarDomPrincipal(valor) {
    if (valor) {
         document.getElementById('divDomPrincipal').style.visibility = 'visible';
    } else {
        document.getElementById('divDomPrincipal').style.visibility = 'hidden';
    }
}

// Oculta/muestra el boton de alta de domicilio
function mostrarAltaDomicilio(valor) {
    if (valor) {
         document.getElementById('nuevoDomicilio').style.visibility = 'visible';
    } else {
        document.getElementById('nuevoDomicilio').style.visibility = 'hidden';
    }
}

// Oculta/muestra el indicador de posicion del domicilio
function mostrarOrdenDomicilio(valor) {
    if (valor) {
         document.getElementById('ordenDomicilio').style.visibility = 'visible';
    } else {
        document.getElementById('ordenDomicilio').style.visibility = 'hidden';
    }
}

function desHabilitarECOESIVIA(valor) {

    if (valor) {
        comboECO.deactivate();
        comboESI.deactivate();
    } else {
        comboECO.activate();
        comboESI.activate();
    }
}

// Deshabilita los campos y combos del interesado
function deshabilitarInteresado(valor) {
    var vector = new Array(document.forms[0].txtDNI,document.forms[0].txtInteresado,
                           document.forms[0].txtApell1,document.forms[0].txtApell2,
                           document.forms[0].txtTelefono,document.forms[0].txtCorreo);
    if (valor) {
        deshabilitarCombo(comboTipoDoc);
        for (var i=0; i<vector.length; i++) vector[i].readOnly = true;
    } else {
        habilitarCombo(comboTipoDoc);
        for (var i=0; i<vector.length; i++) vector[i].readOnly = false;
    }
}

// Deshabilita los campos y combos del domicilio
function deshabilitarDomicilio(valor) {
    deshabilitarImagen([document.getElementById('botonT')], valor); // Boton buscar via
    desHabilitarECOESIVIA(valor);
    document.forms[0].domPrincipal.disabled = valor;

    var vector = new Array(document.forms[0].descVia,document.forms[0].txtNumDesde,
                           document.forms[0].txtLetraDesde,document.forms[0].txtNumHasta,
                           document.forms[0].txtLetraHasta,document.forms[0].txtBloque,
                           document.forms[0].txtPortal,document.forms[0].txtEsc,
                           document.forms[0].txtPlta,document.forms[0].txtPta,
                           document.forms[0].txtDomicilio,document.forms[0].txtBarriada);

    if (valor) {
        for (var i=0; i<vector.length; i++) vector[i].readOnly = true;
        deshabilitarCombo(comboProvincia);
        deshabilitarCombo(comboMunicipio);
        deshabilitarCombo(comboPostal);
    } else {
        for (var i=0; i<vector.length; i++) vector[i].readOnly = false;
        habilitarCombo(comboProvincia);
        habilitarCombo(comboMunicipio);
        habilitarCombo(comboPostal);
    }
}

// Deshabilita un combo manteniendo su estilo
function deshabilitarCombo(combo) {
	if (combo.cod) {
        combo.cod.readOnly = true;
       combo.cod.onblur = function(){ return false; }
    }
    combo.des.readOnly = true;
    combo.des.onclick = function(event){ return false; }
    combo.des.onkeypress = function(event){ return false; }
    combo.anchor.readOnly = true;
    combo.anchor.onclick = function(event){ return false; };
    combo.boton.style.cursor = 'default';
    if (combo.boton.className.indexOf("faDeshabilitado") < 0)
        combo.boton.className += " faDeshabilitado";
}

// Habilita un combo manteniendo su estilo
function habilitarCombo(combo) {
	if (combo.cod) {
        combo.cod.readOnly = false;
        combo.cod.onblur = function(){combo.buscaCodigo(combo.cod.value); return false; }
    }
    //combo.des.readOnly = false;
    combo.des.onclick = function(){ combo.display();return false;  }
    combo.des.onkeypress = function(event){
        var teclaAuxiliar = "";
        if(window.event){
            event = window.event;
            teclaAuxiliar = event.keyCode;
        }else
            teclaAuxiliar = event.which;
    
  		var letra = String.fromCharCode(teclaAuxiliar);
  		if(combo.readOnly) combo.buscaItem(letra);
    }
    combo.anchor.readOnly = false;
	combo.anchor.onclick = function(event){ combo.display();return false;  };
	combo.boton.style.cursor = 'hand';
	combo.boton.className = combo.boton.className.replace(new RegExp('(?:^|\\s)'+ 'faDeshabilitado' + '(?:\\s|$)'),"");
}

function deshabilitarTodo(valor) {
    deshabilitarInteresado(valor);
    deshabilitarDomicilio(valor);
}

function marcarObligatoriosInteresado(valor) {

    var clase = "";
    if (valor) {
        clase = 'inputTextoObligatorio';
    } else {
        clase = 'inputTexto';
    }

    document.getElementById("codTipoDoc").className = clase;
    document.getElementById("descTipoDoc").className = clase;
    document.getElementById("txtDNI").className = clase;
    document.getElementById("txtInteresado").className = clase;
    document.getElementById("txtApell1").className = clase;
}

function marcarObligatoriosDomicilio(valor) {

    var clase = "";
    if (valor) {
        clase = 'inputTextoObligatorio';
    } else {
        clase = 'inputTexto';
    }

    document.getElementById("codProvincia").className = clase;
    document.getElementById("descProvincia").className = clase;
    document.getElementById("codMunicipio").className = clase;
    document.getElementById("descMunicipio").className = clase;
    document.getElementById("descVia").className = clase;
}

/// FIN FUNCIONES PARA HABILITAR/DESHABILITAR O MOSTRAR/OCULTAR

/// FUNCIONES DE VALIDACION
/// -----------------------

  function SoloDigitosConsulta(e) {
      PasaAMayusculas(e);

      var tecla, caracter;
      var numeros= "0123456789&|():><!=";

      tecla = e.keyCode;

      if (tecla == null) return true;

      caracter = String.fromCharCode(tecla);

      if (numeros.indexOf(caracter) != -1) {
          return true;
      } else {
      return false;
      }
  }

function algunoNoVacio(vector){
   var res	= false;
   for(i=0;i<vector.length;i++){
       var valor =	eval("document.forms[0]."+vector[i]+".value");
       if(valor.trim()!=""){
           res	= true;
           break;
       }
   }
   return res;
}

function esPersonaFisica(){
    var pFJ	= document.forms[0].perFJ.value;
    if(pFJ==1){
        return true;
    }else{
        return false;
    }
}

function viaBuscada(){
    var buscada = false;
    if(document.forms[0].descVia.value!="" && document.forms[0].txtCodVia.value !="") {
        buscada = true;
    } else {
        limpiarVia();
        buscada = true;
    }
    return buscada;
}

/// FIN FUNCIONES DE VALIDACION

/// FUNCIONES AUXILIARES
/// --------------------

function formatearNombreCompleto(tercero) {
      var nombreFormateado = "";
      // Apellido 1
      if (tercero[5]!="" && tercero[5]!= undefined) nombreFormateado += tercero[5].trim();
      // Apellido 2
      if (tercero[6]!="" && tercero[6] != undefined) {
          if (nombreFormateado.length > 0) nombreFormateado += " " + tercero[6].trim();
          else nombreFormateado = tercero[6].trim();
      }
      // Nombre
      if (tercero[4]!="" && tercero[4] != undefined) {
          if (nombreFormateado.length > 0) nombreFormateado += ", " + tercero[4].trim();
          else nombreFormateado = tercero[4].trim();
      }
      return nombreFormateado;
  }

// Formatea un domicilio
function formatearDireccionSinMunicipio(domicilio) {

    var resultado = "";

    if (domicilio[20]!=null && domicilio[20]!="") resultado += domicilio[20] + " "; // Tipo via
    if (domicilio[29]!=null && domicilio[29]!="") resultado += domicilio[29] + " "; // Descripcion via
    if (domicilio[3]!=null && domicilio[3]!="") resultado += domicilio[3] + " "; // Emplazamiento
    if (domicilio[9]!=null && domicilio[9]!="") resultado += " " + domicilio[9]; // Numero desde
    if (domicilio[10]!=null && domicilio[10]!="") resultado += " " + domicilio[10] + " "; // Letra desde
    if (domicilio[11]!=null && domicilio[11]!="") resultado += " - " + domicilio[11]; // Numero hasta
    if (domicilio[12]!=null && domicilio[12]!="") resultado += " " + domicilio[12]; // Letra hasta
    if (domicilio[13]!=null && domicilio[13]!="") resultado += " Bl. " + domicilio[13]; // Bloque
    if (domicilio[14]!=null && domicilio[14]!="") resultado += " Portal " + domicilio[14]; // Portal
    if (domicilio[15]!=null && domicilio[15]!="") resultado += " Esc. " + domicilio[15]; // Escalera
    if (domicilio[16]!=null && domicilio[16]!="") resultado += " " + domicilio[16] + "º"; // Planta
    if (domicilio[17]!=null && domicilio[17]!="") resultado += " " + domicilio[17]; // Puerta

    return resultado;
}

function formatearDireccion(domicilio) {

    var resultado = formatearDireccionSinMunicipio(domicilio);

    // Municipio y provincia
    if (resultado != "") resultado += " - ";
    resultado += domicilio[2] + " - " + domicilio[1];

    return resultado;
}

// Devuelve el indice en que esta el dom principal en el vector de domicilios
// del tercero, por defecto 0.
function indiceDomPrincipal(tercero) {
    var codDomPrincipal = tercero[20];
    var domicilios = tercero[18];
    for(var i=0; i<domicilios.length; i++) {
        if (domicilios[i][5] == codDomPrincipal) return i;
    }
    return 0;
}

// Formatea el tercero y domicilio cuyo indice se indica en el formato
// esperado por las paginas de registro y expedientes.
function formatearArrayTercero(tercero, indiceDom) {

   var domicilio = tercero[18][indiceDom];

   var nombreTxt = formatearNombreCompleto(tercero);
   var domicilioTxt = formatearDireccionSinMunicipio(domicilio);

   return   [tercero[0],  // 0 id
             tercero[1],  // 1 version
             tercero[0],  // 2 id
             tercero[1],  // 3 version
             tercero[2],  // 4 codigo tipo doc
             tercero[3],  // 5 doc
             nombreTxt,   // 6 nombre completo
             tercero[5],  // 7 apellido 1
             tercero[6],  // 8 apellido 2
             tercero[7],  // 9 part 1
             tercero[8],  // 10 part 2
             tercero[9],  // 11 telefono
             tercero[10], // 12 email
             domicilio[5],// 13 id
             domicilio[5],// 14 id
             domicilio[0],// 15 pais txt
             domicilio[1],// 16 provincia txt
             domicilio[2],// 17 municipio txt
             domicilioTxt,// 18 domicilio completo
             domicilio[4],// 19 CP
             domicilio[18],// 20 poblacion
             tercero[4], // 21 nombre
             tercero]; // 22 array original para compatibilidad
}

/// FIN FUNCIONES AUXILIARES

/// FUNCIONES PARA MOSTRAR DATOS
/// ----------------------------

// Carga los datos de un tercero y su domicilio principal (o el primero si no hay principal)
function mostrarTercero(indice) {
    terceroActual = indice;
    var tercero = Terceros[indice];
    limpiarInteresado();
    limpiarDomicilio();

    var vectorCampos = new Array();
    var vectorDatos =	new Array();

    vectorCampos=['codTerc','numModifTerc','txtIdTercero','txtVersion','codTipoDoc',
        'txtInteresado','txtApell1','txtApell2','txtPart','txtPart2','txtTelefono',
        'txtCorreo'];
    vectorDatos	=[tercero[0],tercero[1],tercero[0],tercero[1],tercero[2],
        tercero[4],tercero[5],tercero[6],tercero[7],tercero[8],tercero[9],
        tercero[10]];
    rellenar(vectorDatos,vectorCampos);
    if (tercero[18].length == 1) {
        Terceros[terceroActual][20] = Terceros[terceroActual][18][0][5];
    }
    if (tercero[18].length > 0) {
        mostrarDomicilio(tercero, indiceDomPrincipal(tercero));
    } else { // El tercero no tiene ningún domicilio
        mostrarCapaDomicilios(false);
        mostrarDomPrincipal(false);
        mostrarAltaDomicilio(true); // Boton de alta de domicilios para permitir asignar un domicilio
    }
    
    mostrarValoresCamposTerceroSeleccionado(tercero[22]);

    comboTipoDoc.buscaCodigo(document.forms[0].codTipoDoc.value);
    // El doc se borra al cambiar el combo de tipo doc
    document.forms[0].txtDNI.value = tercero[3];
    cambiarSituacion(tercero);
}

function mostrarDomicilio(tercero, i) {
    domicilioActual = i;
    mostrarCapaDomicilios(true);
    mostrarDomPrincipal(true);

    var domicilio = tercero[18][i];
    var txtCodVia = (domicilio[23]==0) ? '' : domicilio[23];
    var numDesde = (domicilio[9]==0) ? '' : domicilio[9];
    var numHasta = (domicilio[11]==0) ? '' : domicilio[11];
    var vectorCampos = new Array();
    var vectorDatos =	new Array();

    vectorCampos=['codDomTerc','txtIdDomicilio','codPais','codProvincia','codMunicipio',
        'descPais','descProvincia','descMunicipio','txtDomicilio','descPostal','txtBarriada',
        'txtCodVia','txtNumDesde','txtLetraDesde','txtNumHasta','txtLetraHasta','descVia','codTVia',
        'descTVia', 'txtNormalizado','txtBloque','txtPortal','txtEsc','txtPlta',
        'txtPta', 'codECO', 'codESI', 'descESI', 'idVia'];

    vectorDatos	=[domicilio[5],domicilio[5],domicilio[6],domicilio[7],domicilio[8],
        domicilio[0],domicilio[1],domicilio[2],domicilio[3],domicilio[4],domicilio[18],
        txtCodVia,numDesde,domicilio[10],numHasta,domicilio[12],domicilio[29],domicilio[19],
        domicilio[20],domicilio[24],domicilio[13],domicilio[14],domicilio[15],domicilio[16],
        domicilio[17],domicilio[26],domicilio[27],domicilio[28],domicilio[23]];
    rellenar(vectorDatos,vectorCampos);

    // Domicilio principal
    if (domicilio[5] == tercero[20]) {
        document.forms[0].domPrincipal.checked = true;
    } else {
        document.forms[0].domPrincipal.checked = false;
    }

    // Flechas de navegación
    if (i < 1) {
        deshabilitarImagen([document.getElementById('flechaAnterior')], true);
    } else {
        habilitarImagen([document.getElementById('flechaAnterior')], true);
    }
    if (i > tercero[18].length - 2) {
        deshabilitarImagen([document.getElementById('flechaSiguiente')], true);
    } else {
        habilitarImagen([document.getElementById('flechaSiguiente')], true);
    }

    // Orden domicilio
    document.getElementById('ordenDomicilio').firstChild.data =
        (i + 1) + ' de ' + tercero[18].length;

    // Boton eliminar domicilio
    if (tercero[18].length < 2) {
        document.getElementById("eliminarDom").style.visibility = "hidden";
    } else {
        document.getElementById("eliminarDom").style.visibility = "visible";
    }

}

function actualizaPersonaFJ(){
    var tipo = document.forms[0].codTipoDoc.value;
    var i;
    if (tipo!="") {
        for(i=0;i<codTipoDocs.length;i++) {
            if(codTipoDocs[i]==tipo) break;
        }
        if(i<codTipoDocs.length) {
            document.forms[0].perFJ.value= persFJ[i];
        } else {
            document.forms[0].perFJ.value=1;
        }

        if(!esPersonaFisica()) {
            document.getElementById("txtApell1").className = "inputTexto";
            document.forms[0].txtApell1.value="";
            document.forms[0].txtApell1.readOnly=true;
            document.forms[0].txtApell2.value="";
            document.forms[0].txtApell2.readOnly=true;
        } else {
            if (modoActual != seleccion && modoActual != altaDom) { // En estos modos no se puede editar
                document.forms[0].txtApell1.readOnly=false;
                document.forms[0].txtApell2.readOnly=false;
            }
            if (modoActual == alta || modoActual == modif) {
                document.getElementById("txtApell1").className = "inputTextoObligatorio";
            }
        }

        // Sin documento
        if (tipo==0) {
            document.forms[0].txtDNI.readOnly=true;
            document.getElementById("txtDNI").className = "inputTexto";
        } else {
            if (modoActual != seleccion && modoActual != altaDom) { // En estos modos no se puede editar
                document.forms[0].txtDNI.readOnly=false;
            }
            if (modoActual == alta || modoActual == modif) {
                document.getElementById("txtDNI").className = "inputTextoObligatorio";
            }
        }
     }
}

  function validarCIF(cif)
    {
        par = 0;
        non = 0;
        letras="ABCDEFGHJKLMNPQRSUVW";
        letrasInicio="KPQS";
        letrasFin="ABEH";
        letrasPosiblesFin="JABCDEFGHI";
        let=cif.charAt(0).toUpperCase();


        if (cif.length!=9)
        {
            return false;
        } else{
            caracterControl =cif.charAt(8).toUpperCase();
        }

        for (zz=2;zz<8;zz+=2)
        {
            par = par+parseInt(cif.charAt(zz));
        }

        for (zz=1;zz<9;zz+=2)
        {
            nn = 2*parseInt(cif.charAt(zz));
            if (nn > 9) nn = 1+(nn-10)
            non = non+nn
        }

        parcial = par + non;
        control = (10 - ( parcial % 10));
        if (control==10) control=0;


    /*
    * El valor del último carácter:
    * Será una LETRA si la clave de entidad es K, P, Q ó S.
    * Será un NUMERO si la entidad es A, B, E ó H.
    * Para otras claves de entidad: el dígito podrá ser tanto número como letra.
    * */

    if (letrasInicio.indexOf(let)!=-1){
        return (letrasPosiblesFin.charAt(control)==caracterControl);
    } else if (letrasFin.indexOf(let)!=-1){
        return (caracterControl==control);
    } else if (letras.indexOf(let)!=-1){
        return ((letrasPosiblesFin.charAt(control)==caracterControl)||(caracterControl==control));
    } else{
        return false;
    }
    }

function validarNie(campo) {
    var documento = campo.value;
    var LONGITUD = 9;

    // Si se trata de un NIF
    // Primero comprobamos la longitud, si es distinta de la esperada, rechazamos.    
    if (documento.length != LONGITUD) {        
        campo.value = '';
        campo.focus();
        return false;
    }

    // Comprobas que el formato se corresponde con el de un NIE
    var primeraLetra = documento.substring(0,1);
    var numero = documento.substring(1,8);
    var ultimaLetra = documento.substring(8,9);
    if (!(isNaN(primeraLetra) && !isNaN(numero) && isNaN(ultimaLetra))) {
        campo.value = '';
        campo.focus();
        return false;
    }
    primeraLetra=primeraLetra.toUpperCase();
    ultimaLetra=ultimaLetra.toUpperCase();

    // Comprobamos que la primera letra es X, Y, o Z modificando el numero como corresponda.
    if (primeraLetra == "Y") numero = parseInt(numero,10) + 10000000;
    else if (primeraLetra == "Z") numero = parseInt(numero,10) + 20000000;
    else if (primeraLetra != "X") {
        campo.value = '';
        campo.focus();
        return false;
    }

    // Validamos el caracter de control.
    var letraCorrecta = getLetraNif(numero);
    if (ultimaLetra != letraCorrecta) {
        campo.value = '';
        campo.focus();
        return false;
    }
    return true;
}
/// FIN FUNCIONES PARA MOSTRAR DATOS

function normalAobligatorioDoc(obj){
 	if('obligatorio' != obj.id){
 		obj.id = "obligatorio";
 		obj.className = "inputTextoObligatorio";
 		//obj.readOnly=false;
 	}
 }

 function obligatorioAnormalDoc(obj){
 	if('obligatorio' == obj.id){
 		obj.id = "";
 		//obj.className = "inputTextoObligatorio inputTextoDeshabilitado";
 		//obj.readOnly=true;
 	}
 }

 function documentoNoValido(idTipo, idDocumento, msj) {
	var error = false;
	var tipo = eval("document.forms[0]." + idTipo + ".value");
	var documento = eval("document.forms[0]." + idDocumento + ".value");
	var docForm = eval("document.forms[0]." + idDocumento);

	if (msj == 0) {
		if (tipo == 0)
			obligatorioAnormalDoc(docForm);
		else
			normalAobligatorioDoc(docForm);
	} else {
		if (tipo == '')
			error = true;
		if (tipo == 1 && !error) {              //Caso NIF
			return !validarNif(docForm);
		} else if (tipo == 4 && !error) {         //Caso CIF            

			if (documento.length != 9)
				error = true;
			else {
				error = comprobar('a', documento.substring(0, 1));
				if (error)
				{
					/** original
					 error = !validarCIF(documento.substring(0,9));
					 **/
					if (!validarCIF(documento.substring(0, 9))) {
						if (!validarNifDocumentoTipoCif(documento.substring(0, 9))) {
							if (!validarNieDocumentoTipoCif(documento.substring(0, 9))) {
								error = true;
							} else
								error = false;
						} else
							error = false;
					} else
						error = false;
				}
			}
		} else if (tipo == 3 && !error) {          //Caso NIE
			error = !validarNie(docForm);
		} else if (tipo == 0 && !error) {         //Caso sin documento
			if (documento.length != 0)
				error = true;
			else {
				error = comprobar('a', documento.substring(0, 0));
			}
		}
		if (error) {
			if (tipo == '') {
				jsp_alerta("A", "Documento Incorrecto");
				eval("document.forms[0]." + idTipo + ".select()");
			} else {
				jsp_alerta("A", "Documento Incorrecto");
				eval("document.forms[0]." + idDocumento + ".focus()");
                                if(docForm.value != ""){
                                    docForm.value = "";
                                }
			}
		}
	}
	return(error);
}

 function comprobar(tipo,cadena){
 	var error = false;
 	for(var i=0;((i<cadena.length) && !error);i++){
         if(((tipo == 'n') && !((cadena.charCodeAt(i) > 47) && (cadena.charCodeAt(i) < 58)))
                 || ((tipo == 'a') && !(((cadena.charCodeAt(i) > 64) && (cadena.charCodeAt(i) < 91))
                 || ((cadena.charCodeAt(i) > 96) && (cadena.charCodeAt(i) < 123))))
                 || ((tipo == 'x') && !(((cadena.charCodeAt(i) > 47) && (cadena.charCodeAt(i) < 58))
                 || ((cadena.charCodeAt(i) > 64) && (cadena.charCodeAt(i) < 91))
                 || ((cadena.charCodeAt(i) > 96) && (cadena.charCodeAt(i) < 123)))))
 			error = true;
 	}
 	return error;
 }

function getLetraNif(dni) {
    var lockup = 'TRWAGMYFPDXBNJZSQVHLCKE';
    return lockup.charAt(dni % 23);
}

function trimTerceroBuscado(){
  /*Se hace trim a todos los criterios para evitar buscar por espacios.*/
  document.getElementById("txtInteresado").value=document.getElementById("txtInteresado").value.trim();
  document.getElementById("txtApell1").value=document.getElementById("txtApell1").value.trim();
  document.getElementById("txtApell2").value=document.getElementById("txtApell2").value.trim();
  document.getElementById("txtTelefono").value=document.getElementById("txtTelefono").value.trim();
  document.getElementById("txtCorreo").value=document.getElementById("txtCorreo").value.trim();
  document.getElementById("txtLetraDesde").value=document.getElementById("txtLetraDesde").value.trim();
  document.getElementById("txtLetraHasta").value=document.getElementById("txtLetraHasta").value.trim();
  document.getElementById("txtBloque").value=document.getElementById("txtBloque").value.trim();
  document.getElementById("txtPortal").value=document.getElementById("txtPortal").value.trim();
  document.getElementById("txtEsc").value=document.getElementById("txtEsc").value.trim();
  document.getElementById("txtPlta").value=document.getElementById("txtPlta").value.trim();
  document.getElementById("txtPta").value=document.getElementById("txtPta").value.trim();
  document.getElementById("txtDomicilio").value=document.getElementById("txtDomicilio").value.trim();
}


function validarCamposSuplementariosTercero(){
    var valido = true;
    if(CARGAR_DATOS_SUPLEMENTARIOS!=null && (CARGAR_DATOS_SUPLEMENTARIOS=="SI" || CARGAR_DATOS_SUPLEMENTARIOS=="si"))
    {
        
        var contador = 0;

        for(i=0;datosSuplementarios!=null && i<datosSuplementarios.length;i++){
            var codCampo    = datosSuplementarios[i][0];
            var tipo        = datosSuplementarios[i][1];
            var obligatorio = datosSuplementarios[i][2];
            if(obligatorio==1){ // Si la definición del campo indica que es obligatorio
                if(tipo!=6){
                    eval("var nombre = document.forms[0]." + codCampo + ".value");
                    eval('var datos = document.getElementsByName("' + codCampo + '");');
                    eval('if(datos!=null && datos.length==1 && datos[0].value=="") contador++');
                }else{
                    // Se trata de un campo desplegable
                    var codDesplegable  = "cod" + codCampo;
                    var descDesplegable = "desc" + codCampo;

                    eval('var datos = document.getElementsByName("' + codDesplegable + '");');
                    eval('if(datos!=null && datos.length==1 && datos[0].value=="") contador++');

                    eval('datos = document.getElementsByName("' + descDesplegable + '");');
                    eval('if(datos!=null && datos.length==1 && datos[0].value=="") contador++');
                }
            }
         }

         if(contador>0) valido = false;
    }

    return valido;
}

function marcarObligatoriosCamposSuplementarios(valor) {

    if(CARGAR_DATOS_SUPLEMENTARIOS!=null && (CARGAR_DATOS_SUPLEMENTARIOS=="SI" || CARGAR_DATOS_SUPLEMENTARIOS=="si"))
    {
        var clase = "";
        if (valor)
            clase = "'inputTextoObligatorio'";
        else
            clase = "'inputTexto'";

        for(i=0;datosSuplementarios!=null && i<datosSuplementarios.length;i++){
            var codCampo    = datosSuplementarios[i][0];
            var tipo        = datosSuplementarios[i][1];
            var obligatorio = datosSuplementarios[i][2];
            if(obligatorio==1){ // Si la definición del campo indica que es obligatorio
                if(tipo!=6){
                    eval('var datos = document.getElementsByName("' + codCampo + '");');
                    eval('if(datos!=null && datos.length==1) datos[0].className=' + clase);

                }else{
                    // Se trata de un desplegable
                    var codDesplegable  = "cod" + codCampo;
                    var descDesplegable ="desc" + codCampo;

                    var datos = document.getElementsByName("' + codDesplegable + '");
                    if(datos!=null && datos.length==1) datos[0].className= + clase;

                    datos = document.getElementsByName("' + descDesplegable + '");
                    if(datos!=null && datos.length==1) datos[0].className= + clase;
                }
            }//if
        }
    }
}

function limpiarCamposSuplementariosTercero() {

    if(CARGAR_DATOS_SUPLEMENTARIOS!=null && (CARGAR_DATOS_SUPLEMENTARIOS=="SI" || CARGAR_DATOS_SUPLEMENTARIOS=="si"))
    {
        if(modoActual==1 || modoActual==2){

            for(i=0;datosSuplementarios!=null && i<datosSuplementarios.length;i++){
                var codCampo = datosSuplementarios[i][0];
                var tipo     = datosSuplementarios[i][1];
                if(tipo!=6){
                    eval('var datos = document.getElementsByName("' + codCampo + '");');
                    eval('if(datos!=null && datos.length==1) datos[0].value="";');
                }
                else{
                    // Se trata de un desplegable
                    var codDesplegable  = "cod" + codCampo;
                    var descDesplegable ="desc" + codCampo;

                    eval('var datos = document.getElementsByName("' + codDesplegable + '");');
                    eval('if(datos!=null && datos.length==1) datos[0].value="";');

                    eval('datos = document.getElementsByName("' + descDesplegable + '");');
                    eval('if(datos!=null && datos.length==1) datos[0].value="";');
                }// else
            }//for
        }// if
    }
 }

/** Limpiar los campos de formulario correspondientes a los campos suplementarios sin tener en cuenta el modo actual
 * existente en la pantalla de gestión de terceros  */
function limpiarCamposSuplementariosTerceroSinModoActual() {

    if(CARGAR_DATOS_SUPLEMENTARIOS!=null && (CARGAR_DATOS_SUPLEMENTARIOS=="SI" || CARGAR_DATOS_SUPLEMENTARIOS=="si"))
    {
        for(i=0;datosSuplementarios!=null && i<datosSuplementarios.length;i++){
            var codCampo = datosSuplementarios[i][0];
            var tipo     = datosSuplementarios[i][1];

            if(tipo==5){
                eval('var datos = document.getElementsByName("' + codCampo + '");');
                eval('if(datos!=null && datos.length==1) datos[0].value="";');
                // Se deshabilitan los botones del campoFichero.jsp
                eval("document.forms[0].cmdVisualizar" + codCampo + ".disabled=true;");
                eval("document.forms[0].cmdVisualizar" + codCampo + ".className='botonGeneralDeshabilitado';");
                eval("document.forms[0].cmdEliminar" + codCampo + ".disabled=true;");
                eval("document.forms[0].cmdEliminar" + codCampo + ".className='botonGeneralDeshabilitado';");
            }
            else
            if(tipo==6)
            {
                // Se trata de un desplegable
                var codDesplegable  = "cod" + codCampo;
                var descDesplegable ="desc" + codCampo;

                eval('var datos = document.getElementsByName("' + codDesplegable + '");');
                eval('if(datos!=null && datos.length==1) datos[0].value="";');

                eval('datos = document.getElementsByName("' + descDesplegable + '");');
                eval('if(datos!=null && datos.length==1) datos[0].value="";');
            }else
            {
                eval('var datos = document.getElementsByName("' + codCampo + '");');
                eval('if(datos!=null && datos.length==1) datos[0].value="";');
            }
        }
    }
 }

function mostrarValoresCamposTerceroSeleccionado(dato){    
    if(CARGAR_DATOS_SUPLEMENTARIOS!=null && (CARGAR_DATOS_SUPLEMENTARIOS=="SI" || CARGAR_DATOS_SUPLEMENTARIOS=="si"))
    {
        // Se limpia los valores que pueda haber en los campos suplementarios
        limpiarCamposSuplementariosTerceroSinModoActual();

        if(dato!=null && dato.length>=1){
            for(i=0;i<dato.length;i++){

                var codTercero  = dato[i][0];
                var codCampo    = dato[i][1];
                var tipo        = dato[i][2];
                var obligatorio = dato[i][3];
                var valor       = dato[i][4];
                var activo      = dato[i][5];

                if(activo=='SI'){
                    if(tipo==6){
                        eval("combo" + codCampo + ".deactivate();");
                        if(valor!=null && valor!='' && valor!='null' && valor!='NULL'){
                            eval("combo" + codCampo + ".buscaLinea('" + valor.trim() + "');");
                        }

                    }else
                    if(tipo==5){
                        if(valor!=null && valor!='' && valor!='null' && valor!='NULL'){
                            eval("document.forms[0]." + codCampo + ".value='" + valor + "';");
                        }
                        eval("document.forms[0]." + codCampo + ".disabled=true;");
                        // Se habilita el botón de Visualizar el fichero
                        eval("document.forms[0].cmdVisualizar" + codCampo + ".disabled=false;");
                        eval("document.forms[0].cmdVisualizar" + codCampo + ".className='botonGeneral';");
                    }
                    else{
                        if(valor!=null && valor!='' && valor!='null' && valor!='NULL'){
                            eval("document.forms[0]." + codCampo + ".value='" + valor + "';");
                        }
                        eval("document.forms[0]." + codCampo + ".disabled=true;");
                    }
                }

            }// for
        }//if
    }
}

function habilitarCamposSuplementarios(flag) {

    if(CARGAR_DATOS_SUPLEMENTARIOS!=null && (CARGAR_DATOS_SUPLEMENTARIOS=="SI" || CARGAR_DATOS_SUPLEMENTARIOS=="si"))
    {
        for(i=0;datosSuplementarios!=null && i<datosSuplementarios.length;i++){
            var codCampo = datosSuplementarios[i][0];
            var tipo     = datosSuplementarios[i][1];
            var activo     = datosSuplementarios[i][3];

            if(activo=='SI'){
                if(tipo==6){
                    if(flag){
                        eval("combo" + codCampo + ".activate();");
                    }
                    else{
                        eval("combo" + codCampo + ".deactivate();");
                    }
                }else
                if(tipo==5){
                    if(flag){
                        eval("document.forms[0]." + codCampo + ".disabled=false;");
                        // Se habilita el botón de Visualizar el fichero
                        eval("document.forms[0].cmdVisualizar" + codCampo + ".disabled=false;");
                        eval("document.forms[0].cmdVisualizar" + codCampo + ".className='botonGeneral';");
                        eval("document.forms[0].cmdEliminar" + codCampo + ".disabled=false;");
                        eval("document.forms[0].cmdEliminar" + codCampo + ".className='botonGeneral';");
                    }else{
                        eval("document.forms[0]." + codCampo + ".disabled=true;");
                        eval("document.forms[0].cmdVisualizar" + codCampo + ".disabled=true;");
                        eval("document.forms[0].cmdVisualizar" + codCampo + ".className='botonGeneralDeshabilitado';");
                        eval("document.forms[0].cmdEliminar" + codCampo + ".disabled=true;");
                        eval("document.forms[0].cmdEliminar" + codCampo + ".className='botonGeneralDeshabilitado';");
                    }
                }
                else{
                    if(flag)
                        eval("document.forms[0]." + codCampo + ".disabled=false;");
                    else
                        eval("document.forms[0]." + codCampo + ".disabled=true;");

                }// else
            }
        }//for
    }
 }
 
    /**
     * Función utilizada cuando se ha indicado que un tipo de documento es un CIF, pero se va a valir
     * si se trata de un NIF. No vacía el campo de formulario con el documento como hace la función
     * @param {String} campo: Cadena de caracteres con el documento a validar
     * @returns {Boolean}
     */
    function validarNieDocumentoTipoCif(documento) {
        var LONGITUD = 9;

        // Si se trata de un NIE
        // Primero comprobamos la longitud, si es distinta de la esperada, rechazamos.    
        if (documento.length != LONGITUD) {                        
            return false;
        }

        // Comprobas que el formato se corresponde con el de un NIE
        var primeraLetra = documento.substring(0,1);
        var numero = documento.substring(1,8);
        var ultimaLetra = documento.substring(8,9);
        if (!(isNaN(primeraLetra) && !isNaN(numero) && isNaN(ultimaLetra))) {                
            return false;
        }
        primeraLetra=primeraLetra.toUpperCase();
        ultimaLetra=ultimaLetra.toUpperCase();

        // Comprobamos que la primera letra es X, Y, o Z modificando el numero como corresponda.
        if (primeraLetra == "Y") numero = parseInt(numero,10) + 10000000;
        else if (primeraLetra == "Z") numero = parseInt(numero,10) + 20000000;
        else if (primeraLetra != "X") {                
            return false;
        }

        // Validamos el caracter de control.
        var letraCorrecta = getLetraNif(numero);
        if (ultimaLetra != letraCorrecta) {                
            return false;
        }
        return true;
    }

    /**
      * Función utilizada cuando se ha indicado que un tipo de documento es un CIF, pero se va a valir
      * si se trata de un NIF. No vacía el campo de formulario con el documento como hace la función
      * validarNif de gestionTerceros.jsp
      * @param {document.forms[0].text} campo
      * @returns {Boolean
      */  
    function validarNifDocumentoTipoCif(abc) {        
        var dni=abc.substring(0,abc.length-1);
        let=abc.charAt(abc.length-1);
        if (!isNaN(let)) {
            return false;
        } else {
            cadena="TRWAGMYFPDXBNJZSQVHLCKET";
            posicion = dni % 23;
            letra = cadena.substring(posicion,posicion+1);
            letra=letra.toUpperCase();
            if (letra!=let.toUpperCase()) return false;
        }
        return true;
    }