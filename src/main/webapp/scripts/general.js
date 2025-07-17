var APP_CONTEXT_PATH;

function isUndefined(a) {return ((typeof a == 'undefined') || (a==null));}

function jsp_alerta(tipo,mensaje,title) {
    var resposta = 0;
    if (window.showModalDialog) {
       var msgtitle = ((title)?(title):("FLEXIA")); 
       resposta = window.showModalDialog(APP_CONTEXT_PATH+'/jsp/portafirmas/tpls/Alert.jsp?TipoMSG='+tipo+'&TituloMSG='+msgtitle+'&DescMSG='+mensaje,null,'dialogHeight:160px;dialogWidth:350px;center:yes;status:no;scroll:yes;');
    } else {
        if (tipo == 'A')
            alert(mensaje);
        else
            resposta = (confirm(mensaje))?1:0;
        }
    return resposta;
}

function jspAlerta(tipo,mensaje,title,despois) {
    var msgtitle = ((title)?(title):("FLEXIA")); 
    
    abrirXanelaAuxiliar(APP_CONTEXT_PATH+'/jsp/portafirmas/tpls/Alert.jsp?TipoMSG='+tipo+'&TituloMSG='+msgtitle+'&DescMSG='+mensaje,null,"width=350,height=220,status=no",despois);
}

function jspAlerta3(tipo,mensaje,title) {
    var msgtitle = ((title)?(title):("FLEXIA")); 
    
    abrirXanelaAuxiliar(APP_CONTEXT_PATH+'/jsp/portafirmas/tpls/Alert.jsp?TipoMSG='+tipo+'&TituloMSG='+msgtitle+'&DescMSG='+mensaje,null,"width=450,height=470,status=no");
}
function jspAlerta2(tipo,mensaxe,titulo){
    
    var caller = jspAlerta2.caller.toString();
    var isNext = false;
    var nextStmts = caller.split('\n').filter(function(stmt) {
        if(isNext || stmt.indexOf('jspAlerta2(') >= 0)
            return isNext = true;
        return false;
    });
        
    var dialogoAlerta = document.createElement("div");   
    dialogoAlerta.setAttribute("id","dialogoAlerta");
    
    var textoMensaxe = document.createElement("p");   
    textoMensaxe.innerHTML = mensaxe;
    dialogoAlerta.appendChild(textoMensaxe);
    
    window.document.body.appendChild(dialogoAlerta);
    
    $( "#dialogoAlerta" ).dialog({
        resizable: false,
        width:350,
        height:160,
        modal: true,
        buttons: {
          "Aceptar": function() {
            var returnValue = 1;
            $( this ).dialog( "close" );
            nextStmts[0] = nextStmts[0].replace(/jspAlerta2\(.*\)/g, JSON.stringify(returnValue));
            eval('{\n' + nextStmts.join('\n'));
          },
          "Cancelar": function() {
            var returnValue = 0;
            $( this ).dialog( "close" );
            nextStmts[0] = nextStmts[0].replace(/jspAlerta2\(.*\)/g, JSON.stringify(returnValue));
            eval('{\n' + nextStmts.join('\n'));
          }
        }
    });
    throw 'Execution stopped until showModalDialog is closed';
}

function jsp_alerta_Mediana(tipo,mensaje,title) {
    var resposta = 0;
    if (window.showModalDialog) {
       var msgtitle = ((title)?(title):("FLEXIA")); 
       resposta = window.showModalDialog(APP_CONTEXT_PATH+'/jsp/portafirmas/tpls/Alert.jsp?TipoMSG='+tipo+'&TituloMSG='+msgtitle+'&DescMSG='+mensaje,null,'dialogHeight:160px;dialogWidth:400px;status:no;center:yes');
    } else {
       if (tipo == 'A'){
            mensaje = (mensaje.split("<br>").join("\n")).split("</br>").join(" ");
            mensaje = (mensaje.split("<li>").join("\n - ")).split("</li>").join(" ");
            alert(mensaje);
        }
        else
            resposta = (confirm(mensaje))?1:0;
    }
    return resposta;
}

function jsp_alerta_Grande(tipo,mensaje,title) {
    var resposta = 0;
    if (window.showModalDialog) {
       var msgtitle = ((title)?(title):("FLEXIA")); 
       resposta = window.showModalDialog(APP_CONTEXT_PATH+'/jsp/portafirmas/tpls/Alert.jsp?TipoMSG='+tipo+'&TituloMSG='+msgtitle+'&DescMSG='+mensaje,null,'dialogHeight:260px;dialogWidth:450px;status:no;center:yes');
    } else {
        if (tipo == 'A'){
            mensaje = (mensaje.split("<br>").join("\n")).split("</br>").join(" ");
            mensaje = (mensaje.split("<li>").join("\n - ")).split("</li>").join(" ");
            alert(mensaje);
        }
            
        else
            resposta = (confirm(mensaje))?1:0;
    }
    return resposta;
}

function jsp_alertaConsultaAsiento(tipo,mensaje,title) {
    var resposta = 0;
    if (window.showModalDialog) {
       var msgtitle = ((title)?(title):("FLEXIA")); 
       resposta = window.showModalDialog(APP_CONTEXT_PATH+'/jsp/portafirmas/tpls/AlertConfirmarConsultaAsiento.jsp?TipoMSG='+tipo+'&TituloMSG='+msgtitle+'&DescMSG='+mensaje,null,'dialogHeight:160px;dialogWidth:350px;status:no;center:yes');
    } else {
        if (tipo == 'A'){
            mensaje = (mensaje.split("<br>").join("\n")).split("</br>").join(" ");
            mensaje = (mensaje.split("<li>").join("\n - ")).split("</li>").join(" ");
            alert(mensaje);
        }
        else
            resposta = (confirm(mensaje))?1:0;
    }
    return resposta;
}

function jsp_alertaRegistro(tipo,mensaje,title) {
    var resposta = 0;
    if (window.showModalDialog) {
       var msgtitle = ((title)?(title):("FLEXIA")); 
       resposta = window.showModalDialog(APP_CONTEXT_PATH+'/jsp/registro/tpls/Alert.jsp?TipoMSG='+tipo+'&TituloMSG='+msgtitle+'&DescMSG='+mensaje,null,'center:yes;dialogHeight:160px;dialogWidth:350px;status:no;help:no;unadorned:yes;edge:raisen;scroll:no');
    } else {
       if (tipo == 'A'){
            mensaje = (mensaje.split("<br>").join("\n")).split("</br>").join(" ");
            mensaje = (mensaje.split("<li>").join("\n - ")).split("</li>").join(" ");
            alert(mensaje);
        }
        else
            resposta = (confirm(mensaje))?1:0;
    }
    return resposta;
}
function jsp_alertaExpRel(tipo,mensaje,title){
var resposta1 = 0;
    if (window.showModalDialog) {
       var msgtitle1 = ((title)?(title):("FLEXIA"));
       resposta1 = window.showModalDialog(APP_CONTEXT_PATH+'/jsp/portafirmas/tpls/AlertExpRel.jsp?TipoMSG='+tipo+'&TituloMSG='+msgtitle1+'&DescMSG='+mensaje,null,'center:yes;dialogHeight:190px;dialogWidth:300px;status:no;help:no;unadorned:yes;edge:raisen;scroll:no');
    } else {
       if (tipo == 'A'){
            mensaje = (mensaje.split("<br>").join("\n")).split("</br>").join(" ");
            mensaje = (mensaje.split("<li>").join("\n - ")).split("</li>").join(" ");
            alert(mensaje);
        }
        else		    
            resposta1 = (confirm(mensaje))?1:0;
    }

    return resposta1;
}

function jsp_alerta_grande(tipo,mensaje,title) {
    var resposta = 0;
    if (window.showModalDialog) {
       var msgtitle = ((title)?(title):("FLEXIA"));
       var datosAEnviar = [mensaje];
       
       resposta = window.showModalDialog(APP_CONTEXT_PATH+'/jsp/administracion/tpls/Alert.jsp?TipoMSG='+tipo+'&TituloMSG='+msgtitle,datosAEnviar,'dialogHeight:260px;dialogWidth:550px;status:no;center:yes');
    } else {
       if (tipo == 'A'){
           mensaje = (mensaje.split("<br>").join("\n")).split("</br>").join(" ");
           mensaje = (mensaje.split("<li>").join("\n - ")).split("</li>").join(" ");

           alert(mensaje);
        } else
            resposta = (confirm(mensaje))?1:0;
    }
    return resposta;
}

 function abrirUrlAplicacionExterna(url){
        var options = "toolbar=0,menubar=0,resizable=1,width=800,height=600, dependent=yes,, top=150, left=200";
        window.open(url, "SIGP",options);
}

/* =====================================================================
Variables Generales
======================================================================== */

var classBotonBuscar = 'botonBuscar';

/* ======================================================================
Función PasaAmayusculas
========================================================================== */


function PasaAMayusculas(e) {
  e.keyCode = String.fromCharCode(e.keyCode).toUpperCase().charCodeAt(0);
  //No se permiten los caracteres " ' \ / %
  //Nota: se permite de nuevo / pues es necesario por ej. para busqueda de expedientes pendientes
  //Nota: se permite de nuevo ' por peticion popular. Es necesario escaparlo en cualquier sitio en
  //que se construya un string en javascript que pueda contener '. Se puede hacer con la clase
  //org.apache.commons.lang.StringEscapeUtils mediante StringEscapeUtils.escapeJavaScript(String x)
  //o simplemente usando " " en vez de ' ', si estamos seguros de que el string no contiene "
  cadena = "\"";
  if ((cadena.indexOf(String.fromCharCode(e.keyCode)) != -1)){
     e.keyCode = " ";
  }
/*cadena = "\'";
  if ((cadena.indexOf(String.fromCharCode(e.keyCode)) != -1)){
     e.keyCode = " ";
  }
*/
  cadena = "\\";
  if ((cadena.indexOf(String.fromCharCode(e.keyCode)) != -1)){
     e.keyCode = " ";
  }
/*  cadena = "\/";
  if ((cadena.indexOf(String.fromCharCode(e.keyCode)) != -1)){
     e.keyCode = " ";
  }
*/
  cadena = "\%";
  if ((cadena.indexOf(String.fromCharCode(e.keyCode)) != -1)){
     e.keyCode = " ";
  }
}

/* ======================================================================
Función: limpiar
Limpia todos los campos que se le pasan en vector
========================================================================== */
function limpiar(vector){
    var longitud = vector.length;
    for(i=0;i<longitud;i++){
    eval("document.forms[0]."+vector[i]+".value=''");
  }
}

/* ======================================================================
Función: rellenar
Rellena todos los campos que se le pasan en vectorCampos con vectorDatos
========================================================================== */
function rellenar(vectorDatos,vectorCampos){
  var longDatos = vectorDatos.length;
  var longCampos = vectorCampos.length;
  if(longDatos==longCampos){
    for(i=0;i<longDatos;i++){
      eval("document.forms[0]."+vectorCampos[i]+".value=vectorDatos[i]");
    }
  }
}

// FUNCION QUE PONE EL VALOR DEL INPUT EN TITLE
function titulo(input){
  input.title = input.value;
}

function SoloDigitos(objeto)
{    
    xAMayusculas(objeto);
      var numeros= "0123456789";
       if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;original!=undefined && i<original.length;i++){
            if(numeros.indexOf(original.charAt(i).toUpperCase())!=-1 || numeros.indexOf(original.charAt(i).toUpperCase())!=-1){
                salida = salida + original.charAt(i);
}
        }
        objeto.value=salida.toUpperCase();
     }//if
}


 /* ======================================================================
Funcion: SoloNumeros
Bloquea un input Texto para que sólo acepte números
========================================================================== */

function SoloDigitosNeg(e,cadena) {

    PasaAMayusculas(e);

    var tecla, caracter;
    var numeros='0123456789';
   var numerosSigno = '-0123456789';

    tecla = e.keyCode;


    if (tecla == null) return true;

    caracter = String.fromCharCode(tecla);

  if (cadena.length < 1) {
    if (numerosSigno.indexOf(caracter) != -1)
      return true;
    } else {
    var largo = cadena.length;
    if (cadena.substring(0,1)=='-')
      largo = largo - 1;
      if ((numeros.indexOf(caracter) != -1)&&(largo < 10))
      return true;
  }
    return false;
}

/* ======================================================================
Funcion: validarNumeroReal
  Llamada: validarNumeroReal(document.forms[0].campoNumero);
  Parametros: objeto; El nombre completo del objeto (this)
  Acción: Va asociada al evento "onBlur" y Bloquea el cuadro de texto
          permitiendo únicamente valores reales para dicho campo, la parte
          decimal (si existe) irá separada por la coma ",". Acepta números
          con la coma al principio.
  Salida: Devuelve true o false dependiendo de si el valor del campo de texto
          que se le pasa como parametro es un número real ó no. Además redirige
          el foco hacia dicho objeto mientras no sea un numero real.
========================================================================== */
function validarNumeroReal(objeto)
{
    var numeros='0123456789';
    var coma=',';
    var punto='.';
    var caracter_contenido='';
    var numero_comas=0;
    var numero_comas_permitidas=1;
    var contenido=objeto.value;
    for(var i=0;i<contenido.length;i++)
    {
      caracter_contenido=contenido.charAt(i);
      if (numeros.indexOf(caracter_contenido,0)==-1) //No es un numero
      {
        if (coma.indexOf(caracter_contenido,0)==-1 && punto.indexOf(caracter_contenido,0)==-1) //No es una coma ni un punto
        {
          jsp_alerta("A","Revise los datos, sólo se aceptan Números (inclusive Reales con coma)");
          objeto.focus();
          return false;
        }
        else
        {
          numero_comas++;
          if (numero_comas>numero_comas_permitidas)
          {
            jsp_alerta("A","Revise los datos, sólo se aceptan Números (inclusive Reales con coma)");
            objeto.focus();
            return(false);
          }
        }//del if
      }//del if
    }//del for
    return(true);
}//de la funcion

/* =======================================================================================================================
Funcion: validarPartesEnteraDecimal
 Llamada: validarPartesEnteraDecimal(document.forms[0].campoNumero,longitudParteEntera,longitudParteDecimal);
 Parametros: objeto; El nombre completo del objeto (this), numero de digitos maximo para la parte entera,numero de digitos
             maximo para la parte decimal
 Acción: Va asociada al evento "onBlur" y asegura que el numero decimal ya evaluado en validarNumeroReal cumple el formato
         indicado en los parametros

 Salida: Devuelve true o false dependiendo de si el valor del campo de texto
         que se le pasa como parametro no excede los maximos indicados para el numero de digitos de la parte entera y
         decimal. Además, redirige el foco y vacia el contenido de dicho objeto cuando esto no se cumple, o sustituye la coma
         por un punto cuando el objeto es correcto (sql server).
========================================================================================================================== */
function validarPartesEnteraDecimal(objeto,longitudParteEntera,longitudParteDecimal)
{
    var coma=',';
    var punto='.';
    var caracter_contenido='';
    var contenido=objeto.value;
    var i=0;
    var uno = new Number(1);
    var valido=true,esDecimal=false;
    var numEnteros = new Number();
    var numDecimales = new Number();
    var longParteEntera = new Number(longitudParteEntera);
    var longParteDecimal = new Number(longitudParteDecimal);
    while (i<contenido.length && valido) {
      caracter_contenido=contenido.charAt(i);
      if ((coma.indexOf(caracter_contenido,0)==-1) && (punto.indexOf(caracter_contenido,0)==-1) && (!esDecimal)) { // Es entero
          numEnteros = numEnteros + uno;
          if (numEnteros>longParteEntera) {
              valido=false;
          }
      } else if (esDecimal) {
        numDecimales = numDecimales + uno;
        if (numDecimales>longParteDecimal) {
            valido=false;
        }
      } else {
        esDecimal=true;
        if (coma.indexOf(caracter_contenido,0)!=-1) {
            objeto.value=contenido.replace(',','.');
        }
      }
      i++;
    }
    if (!valido) {
        jsp_alerta("A","Revise los datos (número máximo de dígitos de la parte entera: "+longitudParteEntera+", número máximo de dígitos de la parte decimal: "+longitudParteDecimal+")");
        objeto.value='';
        objeto.focus();
    }
    return valido;
}//de la funcion

 /* ======================================================================
Funcion: SoloLetras
Bloquea un input Texto para que sólo acepte letras
========================================================================== */
function SoloLetras(e)
{
    PasaAMayusculas(e);

    var tecla, caracter;
    var letras ='abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ';
    tecla = e.keyCode;

    if (tecla == null) return true;

    caracter = String.fromCharCode(tecla);

    if ( (letras.indexOf(caracter) != -1) || tecla==32)
      return true;
    return false;
}


/* ======================================================================
Funcion: SoloLetras_Acentos
Bloquea un input Texto para que sólo acepte letras
========================================================================== */
function SoloLetrasAcentos(e)
{
    PasaAMayusculas(e);

    var tecla, caracter;
    var letras ='abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚáéíóú';
    tecla = e.keyCode;

    if (tecla == null) return true;

    caracter = String.fromCharCode(tecla);

    if ( (letras.indexOf(caracter) != -1) || tecla==32)
      return true;
    return false;
}


 /* ======================================================================
Funcion: SoloAlfanumericos
Bloquea un input Texto para que sólo acepte alfanumericos
========================================================================== */

function SoloAlfanumericos(e) {
    PasaAMayusculas(e);

    var tecla, caracter;
    var alfanumericos = '0123456789abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚáéíóú';

    tecla = e.keyCode;

    if (tecla == null) return true;

    caracter = String.fromCharCode(tecla);

    if ((alfanumericos.indexOf(caracter) != -1) || tecla==32)
    return true;
    return false;
}
/* ======================================================================
Funcion: SoloAlfanumericos_espacio
Bloquea un input Texto para que sólo acepte alfanumericos y espacios
========================================================================== */

function SoloAlfanumericos_espacio(e) {
    PasaAMayusculas(e);

    var tecla, caracter;
    var alfanumericos = '0123456789abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚáéíóú ';

    tecla = e.keyCode;

    if (tecla == null) return true;

    caracter = String.fromCharCode(tecla);

    if ((alfanumericos.indexOf(caracter) != -1) || tecla==32)
    return true;
    return false;
}
 /* ======================================================================
Funcion: SoloPermitidos
Bloquea un input Texto para que sólo acepte alfanumericos
========================================================================== */

function SoloPermitidos(e)
{
    PasaAMayusculas(e);

    var tecla, caracter;
    var alfanumericos = 'ÁÉÍÓÚáéíóú´\'';

    tecla = e.keyCode;

    if (tecla == null) return true;

    caracter = String.fromCharCode(tecla);

    if ((alfanumericos.indexOf(caracter) == -1) || tecla==32)
    return true;
    return false;
}

 /* ======================================================================
Funcion: SoloMail
Bloquea un input Texto para que sólo acepte alfanumericos
========================================================================== */
function SoloMail(e)
{
   // PasaAMayusculas(e);

    var tecla, caracter;
    var alfanumericos = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_@.';

    tecla = e.keyCode;

    if (tecla == null) return true;

    caracter = String.fromCharCode(tecla);

    if ((alfanumericos.indexOf(caracter) != -1))
    return true;
    return false;
}
/* ======================================================================
Funcion: SoloDecimales
Bloquea un input Texto para que sólo acepte decimales
========================================================================== */
function SoloDecimales(e)
{
    PasaAMayusculas(e);

    var tecla, caracter;
  var decimales = '0123456789,';

    tecla = e.keyCode;

    if (tecla == null) return true;

    caracter = String.fromCharCode(tecla);

  if ( (decimales.indexOf(caracter) != -1))
    return true;
    return false;
}

/* ======================================================================
Funcion: SoloFechas
Bloquea el input para que sólo acepte fechas
========================================================================== */

function ValidarFecha(dateStr) {

   var datePat = /^(\d{1,2})(\/|-)(\d{1,2})\2(\d{2}|\d{4})$/;
   var matchArray = dateStr.match(datePat);

   return (matchArray != null);
 }

/* ======================================================================
Funcion: soloCaracteresFechaConsultando
Bloquea el input para que sólo acepte caracteres de consulta de fechas
========================================================================== */
function soloCaracteresFechaConsultando(e) {
  PasaAMayusculas(e);
  var tecla, caracter;
  var alfanumericos = '0123456789/_-.:|&<>!';
  tecla = e.keyCode;
  if (tecla == null) return true;
  caracter = String.fromCharCode(tecla);
  if ((alfanumericos.indexOf(caracter) != -1) || tecla==32)
    return true;
  return false;
}


function comprobarFecha(inputFecha) {
  var formato = 'dd/mm/yyyy';
  if (Trim(inputFecha.value)!='') {

      var validas = true;
      var fechaFormateada=inputFecha.value;
      var pos=0;

      var fechas = Trim(inputFecha.value);
      var fechas_array = fechas.split(/[:|&<>!]/);

      for (var loop=0; loop < fechas_array.length; loop++)
      {
        f = fechas_array[loop];
        formato = formatoFecha(Trim(f));
        var D = ValidarFechaConFormato(f,formato);
        if (!D[0]) validas=false;
        else {
          if (fechaFormateada.indexOf(f) != -1) {
            var toTheLeft = fechaFormateada.substring(0, fechaFormateada.indexOf(f));
            var toTheRight = fechaFormateada.substring(fechaFormateada.indexOf(f)+f.length, fechaFormateada.length);
            pos=fechaFormateada.indexOf(f,pos);
            fechaFormateada = toTheLeft + D[1]+ toTheRight;
          }
        }
      }
        if (!validas) {
            jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
            //tp1.setSelectedIndex(0);
            inputFecha.focus();
            return false;
      } else {
          inputFecha.value = fechaFormateada;
          return true;
      }
  }
  return true;
}

function formatoFecha(f){
  var parts = Trim(f).split(/[_\-\.\/]/);
    if (parts.length == 3) return 'dd/mm/yyyy';
  else if (parts.length == 2) return 'mm/yyyy';
  else if (parts.length == 1) {
    if (f.length == 4) return 'yyyy'
    //else if (f.length == 6) return 'mmyyyy';
    else return 'dd/mm/yyyy';
  }
}
 /* ======================================================================
Funcion: Marca los obligatorios en amarillo si vacíos
Cambiada x Kr
========================================================================== */

 function MarcarObligatorios(objeto) {

  if (objeto.value == '')	objeto.style.background = 'white';
  else objeto.style.background = 'white';
}

/*************************************************************************/
/*			Cierra el div de Mensaje									 */
/*************************************************************************/

function CerrarMensaje()
{
  document.all.mensaje.style.visibility = 'hidden';
  alert ('antes del aceptar a true');
  aceptarMens = 1;
}

/***************************************************************************/
/*   			    Muestra un mensaje alert 	     				*/
/***************************************************************************/
function mostrarAlertNoModal(mensaje)
{
  alert('al inicio');
  var Win = open("","newwin","toolbar=no,location=no,directories=no,height=160,width=300");
  Win.document.open();
  Win.document.write("<html><head><title>Form Validation Check</title></head>");
  Win.document.write("<body>");
  Win.document.write("<div align=\"center\">");
  Win.document.write("<h1><i>Oops!</i></h1><hr width=\"80%\">");
  Win.document.write(mensaje);
  Win.document.write("<form><input type=button value=\" OK \" ");
  Win.document.write("onClick=\"window.close()\"> </form></div></body></html>");
  Win.document.close();
  alert('el final');
}


/*************************************************************************/
/*			Recupera un parámetro de la url            		*/
/************************************************************************/
function valorParametro(parametro)
{
  var devolver = null;
  var nombre = document.location.toString();
  var pos = 0;
  pos = nombre.lastIndexOf(parametro);
  if (pos != -1)
  {
    var valor = nombre.substring(pos+1+parametro.length,nombre.length);
    pos = valor.indexOf('&');
    if (pos != -1)
    {
      devolver = valor.substring(0,pos);
    }
    else
    {
      devolver = valor;
    }
  }
  return devolver;
}

/*************************/
/* Muestra mensaje alert */
/*************************/

function mostrarAlertAviso(mensaje) {

   var dirUrl = "mensajeAlert.html?mens=" + mensaje;

   window.showModalDialog(dirUrl,window,"help:0;status:0;dialogWidth:300px;scrollbars:0;center:1;dialogHeight:150px;");
}

/***************************************************************************/
/*   			    Calcula la posicion vertical media      				*/
/***************************************************************************/
function calcularPosTabla(tabla)
{
  // Personalizado.

  var num = 0;

  // Partimos de que la altura es 600.
  var alturaPant = 0.80*screen.availHeight;
  var alturaTab = tabla.height;

  num = ((alturaPant - alturaTab)/2);

  return num;
}
function deshabilitarIconos(vector, valor) {
    for (i=0;vector!='' && i<vector.length;i++) {
		if (typeof(vector[i]) !== "undefined") {
			var className = vector[i].className;
			if (valor == true) {
				vector[i].disabled = true;
				vector[i].style.cursor = 'default';
				if (className.indexOf("faDeshabilitado") < 0)
					className += " faDeshabilitado";
			} else {
				vector[i].disabled = false;
				vector[i].style.cursor = 'hand';
				className = className.replace(new RegExp('(?:^|\\s)'+ 'faDeshabilitado' + '(?:\\s|$)'),"");
			}
			vector[i].className = className;
			
			var pai = vector[i].parentNode;
			if (pai.tagName=="A")
				deshabilitarAnchor(pai,valor);
		}
    }
}

function deshabilitarAnchor(pai, valor){
    if(valor == true)
        pai.style.pointerEvents = 'none';
    else
        pai.style.pointerEvents = '';
}

/***************************************************************************/
/*				Habilita imagen						            		   */
/***************************************************************************/
function habilitarImagen(vector, valor) {
    deshabilitarIconos(vector, !valor);
}

/***************************************************************************/
/*				Deshabilita imagen					               		   */
/***************************************************************************/
function deshabilitarImagen(vector, valor) {
    deshabilitarIconos(vector, valor);
}

/*******************************************************************************************************************/
/*				Habilita imagen Botones Desplegables cuyos nombres son introducidos en un Array           		   */
/*******************************************************************************************************************/
function habilitarImagenBotonGeneral(vector, valor){
    deshabilitarIconos(vector, !valor);
}

/***********************************************************************************************************************/
/*				Deshabilita imagen Botones Desplegables cuyos nombres son introducidos en un Array             		   */
/***********************************************************************************************************************/

function deshabilitarImagenBotonGeneral(vector, valor){
    deshabilitarIconos(vector, valor);
}

/***************************************************************************/
/*				Habilita imagen Botón Desplegable                		   */
/***************************************************************************/
function habilitarImagenBoton(id, valor){
    var vector = [document.getElementById(id)];
    deshabilitarIconos(vector, !valor);
}

/***************************************************************************/
/*				Deshabilita imagen Botón Desplegable                		   */
/***************************************************************************/
function deshabilitarImagenBoton(id, valor){
    var vector = [document.getElementById(id)];
    deshabilitarIconos(vector, valor);
}

/***************************************************************************/
/*   			    Deshabilita el boton que recibe         			   */
/***************************************************************************/
function deshabilitarBoton(obj) {  
   if(obj!=null){
        obj.disabled = 1;
        obj.style.cursor = 'default';

        var botones = document.getElementsByName(obj.name);
        if(botones!=null && botones.length==1){
          botones[0].className = "botonGeneralDeshabilitado";
        }
   }
}

/***************************************************************************/
/*   			    Habilita el boton que recibe            			   */
/***************************************************************************/
function habilitarBoton(obj) {
  obj.disabled = 0;
  obj.style.cursor = 'hand';
  var botones = document.getElementsByName(obj.name);
  if(botones!=null && botones.length==1){
    botones[0].className = "botonGeneral";
  }
}

function deshabilitarBotonLargo(obj) {  
   if(obj!=null){
        obj.disabled = 1;
        obj.style.cursor = 'default';

        var botones = document.getElementsByName(obj.name);
        if(botones!=null && botones.length==1){
          botones[0].className = "botonMasLargoDeshabilitado";
        }
   }
}

/***************************************************************************/
/*   			    Habilita el boton que recibe            			   */
/***************************************************************************/
function habilitarBotonLargo(obj) {
  obj.disabled = 0;
  obj.style.cursor = 'hand';
  var botones = document.getElementsByName(obj.name);
  if(botones!=null && botones.length==1){
    botones[0].className = "botonMasLargo";
  }
}

/***************************************************************************/
/* cambia el estilo de todos los campos de Normal a Obligatorio            */
/***************************************************************************/
function normalAobligatorio(vectorObj){
  var el;
  for (i=0;i<vectorObj.length;i++){
    el=vectorObj[i];
    if('inputTexto' == el.className){
      el.id = "obligatorio";
      el.className = "inputTextoObligatorio";
    }
    if('inputTxtFecha' == el.className){
      el.id="obligatorio";
      el.className = "inputTxtFechaObligatorio";
    }
    if('inputCombo' == el.className){
      el.id="obligatorio";
      el.className = "inputComboObligatorio";
    }
    if('textareaTexto' == el.className){
      el.id="obligatorio";
      el.className = "textareaTextoObligatorio";
    }
  }
}

/***************************************************************************/
/* cambia el estilo de todos los campos de Obligatorio a Normal            */
/***************************************************************************/
function obligatorioAnormal(vectorObj){
  var el;
  for (i=0;i<vectorObj.length;i++){
    el=vectorObj[i];
    if('inputTextoObligatorio' == el.className || el.className.indexOf('inputTextoObligatorio') != -1){
      el.id = "";
      el.className = "inputTexto";
    }
    if('inputTxtFechaObligatorio' == el.className){
      el.id="";
      el.className = "inputTxtFecha";
    }
    if('inputComboObligatorio' == el.className){
      el.id="";
      el.className = "inputCombo";
    }
    if('textareaTextoObligatorio' == el.className){
      el.id="";
      el.className = "textareaTexto";
    }
  }
}

/***************************************************************************/
/* Deshabilita todos los campos cuyos nombres son introducidos en un Array */
/***************************************************************************/
function deshabilitarGeneral(vectorObj){
    var el;
    for (i=0;i<vectorObj.length;i++){
        el=vectorObj[i];

        if(el!=null){
            if (('inputTexto' == el.className)||('inputTextoObligatorio' == el.className)){
                el.disabled=true;      
                el.style.cursor = 'default';
                el.className += " inputTextoDeshabilitado";
            } else if (('inputTxtFecha' == el.className)||('inputTxtFechaObligatorio' == el.className)){
                el.disabled=true;
                el.style.cursor = 'default';
                el.className += " inputTxtFechaDeshabilitado";
            } else if (('inputCombo' == el.className)||('inputComboObligatorio' == el.className)){
                el.disabled=true;
                el.style.cursor = 'default';
                el.className += " inputComboDeshabilitado";
            } else if(('textareaTexto' == el.className)||('textareaTextoObligatorio' == el.className)){
                el.readOnly=true;
                el.style.cursor = 'default';
                el.className += " textareaTextoDeshabilitado";
            } else if('boton' == el.className){
                el.disabled=true;
                el.style.cursor = 'default';
            } else if('botonLargo' == el.className){
                el.disabled=true;
                el.style.cursor = 'default';
                var botones = document.getElementsByName(el.name);
                if(botones!=null && botones.length==1){
                      botones[0].className = "botonLargoDeshabilitado";
                }
            } else if('botonLargo2' == el.className) {
                el.disabled=true;
                el.style.cursor = 'default';
                var botones = document.getElementsByName(el.name);
                if(botones!=null && botones.length==1){
                      botones[0].className = "botonLargo2Deshabilitado";
                }
            } else if('botonGeneral' == el.className){
                el.disabled=true;
                el.style.cursor = 'default';

                var botones = document.getElementsByName(el.name);
                if(botones!=null && botones.length==1){
                      botones[0].className = "botonGeneralDeshabilitado";
                }
            } else if(el.src || 'botonMasLargo' == el.className || 'boton' == el.className){
                el.disabled=true;
                el.style.cursor = 'default';
            }else if('radio' == el.type){
                el.disabled = true;
            }
        }
    }
}

/***************************************************************************/
/*  Habilita todos los campos cuyos nombres son introducidos en un Array   */
/***************************************************************************/
function habilitarGeneral(vectorObj){
    var el;
    var clase;
    for (i=0;i<vectorObj.length;i++){
        el = vectorObj[i];
        clase = el.className.split(" ");

        if (el!=null){
            if (el.className=='botonGeneral'){
                el.disabled = false;
                el.style.cursor = 'hand';
                var botones = document.getElementsByName(el.name);
                if(botones!=null && botones.length==1){
                    botones[0].className = 'botonGeneral';
                }
            } else if (el.className=='botonGeneralDeshabilitado'){
                el.disabled = false;
                el.style.cursor = 'hand';
                var botones = document.getElementsByName(el.name);
                if(botones!=null && botones.length==1){                
                    botones[0].className = 'botonGeneral';
                }           
            } else if ('botonLargo' == el.className) {
                el.disabled = false;
                el.style.cursor = 'hand';
                var botones = document.getElementsByName(el.name);
                if(botones!=null && botones.length==1) {
                    botones[0].className = 'botonLargo';
                }
            } else if ('botonMasLargo' == el.className) {
                el.disabled = false;
                el.style.cursor = 'hand';
                var botones = document.getElementsByName(el.name);
                if(botones!=null && botones.length==1) {
                    botones[0].className = 'botonMasLargo';
                }
            } else if ('botonLargo2Deshabilitado' == el.className){
                el.disabled = false;
                el.style.cursor = 'hand';
                 var botones = document.getElementsByName(el.name);
                if (botones!=null && botones.length==1) {
                    botones[0].className = 'botonLargo2';
                }
            } else if ('botonLargoDeshabilitado' == el.className) {
                el.disabled = false;
                el.style.cursor = 'hand';
                var botones = document.getElementsByName(el.name);
                if(botones!=null && botones.length==1) {
                    botones[0].className = 'botonLargo';
                }
            } else if ((el.readOnly || el.disabled)&&('textareaTextoObligatorio,textareaTextoDeshabilitado'==clase || 'textareaTexto,textareaTextoDeshabilitado'==clase)){
                el.readOnly = false;
                el.disabled = false;
                el.className = clase.slice(0,clase.indexOf(","));
            } else if ((el.readOnly || el.disabled)&&('inputTextoObligatorio,inputTextoDeshabilitado'==clase || 'inputTexto,inputTextoDeshabilitado'==clase)){
                el.readOnly = false;
                el.disabled = false;
                el.className = clase.slice(0,clase.indexOf(","));
            }
             else if ((el.readOnly || el.disabled)&&('inputTxtFechaObligatorio,inputTxtFechaDeshabilitado'==clase || 'inputTxtFecha,inputTxtFechaDeshabilitado'==clase)){
                el.readOnly = false;
                el.disabled = false;
                el.className = clase.slice(0,clase.indexOf(","));
            }
            else if('radio' == el.type){
                el.disabled = false;
            }  else if('checkbox' == el.type){
                el.disabled = false; 
            }else if('button' == el.type && 'cmdRelaciones'== el.name){//habilita boton de relaciones FFox
                el.disabled = false;
                el.style.cursor = 'hand';
                el.style.color = '#ffffff';
            }
            if (el.src || 'boton' == el.className){
                el.disabled = false;
                el.style.cursor = 'hand';
            } 
        }
    }// for
}

/*************************************************************************************/
/* Habilita o deshabilita cualquier Input. En el vector solo se pasan los nombres de */
/* los objetos, ya que luego se hace un eval                                         */
/*************************************************************************************/
function habilitarGeneralInputs(vectorObj,habilitar){
  var el;
  var clase;
  for (i=0;i<vectorObj.length;i++){
    el = eval("document.forms[0]."+vectorObj[i]);
    if(habilitar){
      clase = el.className.split(" ");
      if (clase.length == 2){
        el.disabled = false;
        el.readOnly=false;
        el.className = clase[0];
      }
      if(el.src){
        el.disabled=false;
        el.style.cursor = 'hand';
      }
      if('boton' == el.className){
        el.disabled = false;
        el.style.cursor = 'hand';
      }
      if('botonGeneral' == el.className){
        el.disabled = false;
        el.style.cursor = 'hand';
        var botones = document.getElementsByName(el.name);
        if(botones!=null && botones.length==1){
           botones[0].className = "botonGeneral";
        }
      }

      if('botonGeneralDeshabilitado' == el.className){
        el.disabled = false;
        el.style.cursor = 'hand';
        var botones = document.getElementsByName(el.name);
        if(botones!=null && botones.length==1){
           botones[0].className = "botonGeneral";
        }
      }
    }else{
      if (('inputTexto' == el.className)||('inputTextoObligatorio' == el.className)){
        el.disabled = true;
        el.style.cursor = 'default';
        el.className += " inputTextoDeshabilitado";
      }
      if (('inputTxtFecha' == el.className)||('inputTxtFechaObligatorio' == el.className)){
        el.disabled = true;
        el.style.cursor = 'default';
        el.className += " inputTxtFechaDeshabilitado";
      }
      if (('inputCombo' == el.className)||('inputComboObligatorio' == el.className)){
        el.disabled = true;
        el.style.cursor = 'default';
        el.className += " inputComboDeshabilitado";
      }
      if(('textareaTexto' == el.className)||('textareaTextoObligatorio' == el.className)){
        el.readOnly = true;
        el.style.cursor = 'default';
        el.className += " textareaTextoDeshabilitado";
      }
      if(el.src){
        el.disabled=true;
        el.style.cursor = 'default';
      }
      if('boton' == el.className){
        el.disabled=true;
        el.style.cursor = 'default';
      }
      if('botonGeneral' == el.className){
        el.disabled = true;
        el.style.cursor = 'hand';
        var botones = document.getElementsByName(el.name);        
        if(botones!=null && botones.length==1){
           botones[0].className = "botonGeneralDeshabilitado";
        }
      }
    }
  }
}

/*************************************************************************************
Nombre de la Función: habilitarGeneralCombos
Parámetros:
    1. Un vector con los nombres (identificadores) de los combos que queremos des/habilitar
    2. Un valor true ó false dependiendo de si se quieren ó no habilitar
Acción: Habilita/Deshabilita el vector de combos cuyos identificadores
    se le pasn como primer parámetro dependiendo del valor del segundo parametro.
Salida: No devuelve ninguna salida.
*************************************************************************************/
function habilitarGeneralCombos(vectorNombres,valor)
{
  for(var i=0;i<vectorNombres.length;i++)
  {
    var combo=eval(""+vectorNombres[i]);
    if (valor==true)
    {
      combo.activate();
    }
    else if (valor==false)
    {
      combo.deactivate();
    }//del if
  }//del for
}//de la funcion


/***************************************************************************/
/*Deshabilita todos los campos del formulario para no poder introducir datos*/
/***************************************************************************/
function deshabilitarDatos(frm){
  var inputs = frm.getElementsByTagName("INPUT");
  var sel      = frm.getElementsByTagName("SELECT");
  var area   = frm.getElementsByTagName("TEXTAREA");
  var icon    = frm.getElementsByClassName("fa");

  deshabilitarGeneral(inputs);
  deshabilitarGeneral(sel);
  deshabilitarGeneral(area);
  deshabilitarIconos(icon);
}

/***************************************************************************/
/*Habilita todos los campos del formulario para poder introducir datos*/
/***************************************************************************/
function habilitarDatos(frm){
  var inputs = frm.getElementsByTagName("INPUT");
  var sel     = frm.getElementsByTagName("SELECT");
  var area  = frm.getElementsByTagName("TEXTAREA");
  var icon   = frm.getElementsByClassName("fa");

  habilitarGeneral(inputs);
  habilitarGeneral(sel);
  habilitarGeneral(area);
  deshabilitarIconos(icon);
}

function obtenerUltimaPosicion (valor,caracter)
{
    var pos=-1;

    for(var i=0;i<valor.length;i++)
    {
       if(valor.charAt(i)==caracter)
       {
           pos=i;
       }
    }
    return pos;
}

/***************************************************************************/
/* Cambia el atributo class de todos los campos incluidos en vectorObj     */
/* de obligatorio a no obligatorio dependiendo del tipo de campo.          */
/* 14/01/2002								   */
/***************************************************************************/
function noObligatorioToObligatorioGeneral(vectorObj){
  var el;
  var clase;
  for (i=0;i<vectorObj.length;i++){
    el=vectorObj[i];
    if ('inputTexto' == el.className){
      el.className = "inputTextoObligatorio";
    }
    if ('inputTxtFecha' == el.className){
      el.className = "inputTxtFechaObligatorio";
    }
    if ('inputCombo' == el.className){
      el.className = "inputComboObligatorio";
    }
    if('textareaTexto' == el.className){
      el.className = "textareaTextoObligatorio";
    }
  }
}

/***************************************************************************/
/* Cambia el atributo class de todos los campos obligatorios 		   */
/* de obligatorio a no obligatorio dependiendo del tipo de campo	   */
/***************************************************************************/
function obligatorioToNoObligatorio(frm){
  var el;
  //var inputs = frm.all.tags("INPUT");
  var inputs = frm.getElementsByTagName("INPUT");
  
  for (i=0;i<inputs.length;i++){
    el=inputs[i];
    if ('inputTextoObligatorio' == el.className){
      el.className = "inputTexto";
    }
    if ('inputTxtFechaObligatorio' == el.className){
      el.className = "inputTxtFecha";
    }
  }
  //var sel= frm.all.tags("SELECT");
  var sel= frm.getElementsByTagName("SELECT");
  for (i=0;i<sel.length;i++){
    el=sel[i];
    if ('inputComboObligatorio' == el.className){
      el.className = "inputCombo";
    }
  }
  //var area = frm.all.tags("TEXTAREA");
  var area = frm.getElementsByTagName("TEXTAREA");
  for (i=0;i<area.length;i++){
    el=area[i];
    if('textareaTextoObligatorio' == el.className){
      el.className = "textareaTexto";
    }
  }
}

/***************************************************************************/
/* Cambia el atributo class de todos los campos con id=Obligatorio         */
/* de no obligatorio a obligatorio dependiendo del tipo de campo
/***************************************************************************/

function noObligatorioToObligatorio(frm){
  var el;
  //var inputs = frm.all.tags("INPUT");
  var inputs = frm.getElementsByTagName("INPUT");
  for (i=0;i<inputs.length;i++){
    el=inputs[i];
    if (('inputTexto' == el.className) && ('obligatorio'== el.id) ){
      el.className = "inputTextoObligatorio";
    }
    if (('inputTxtFecha' == el.className)  && ('obligatorio'== el.id) ){
      el.className = "inputTxtFechaObligatorio";
    }
  }
  //var sel= frm.all.tags("SELECT");
  var sel= frm.getElementsByTagName("SELECT");
  for (i=0;i<sel.length;i++){
    el=sel[i];
    if ( ('inputCombo' == el.className)  && ('obligatorio'== el.id) ){
      el.className = "inputTextoObligatorio";
    }
  }

  //var area = frm.all.tags("TEXTAREA");
  var area = frm.getElementsByTagName("TEXTAREA");
  for (i=0;i<area.length;i++){
    el=area[i];
    if(('textareaTexto' == el.className)  && ('obligatorio'== el.id) ){
      el.className = "textareaTextoObligatorio";
    }
  }

}


/***************************************************************************/
/* Cambia el atributo readonly de todos los campos incluidos en vectorObj  */
/* de true a false.          						   */
/* 15/01/2002								   */
/***************************************************************************/
function readonlyToNoReadonlyGeneral(vectorObj){
  var el;
  for (i=0;i<vectorObj.length;i++){
    el=vectorObj[i];
    el.readOnly=false;
    el.tabIndex="0";
  }
}

/**************************************************************/
/*  Trim .- Elimina los espacios en blanco de la izq y dcha	  */
/**************************************************************/
function Trim( cadena) {
  var resultado = '';

  resultado = lTrim(cadena);
  resultado = rTrim(resultado);

  return resultado;
}

/**************************************************************/
/*  Trim Izquierdo 							  */
/**************************************************************/
function TrimL( cadenaDato )
{
  var devuelve = '';
  var cadena = cadenaDato +'';
  /*if (cadena == 'undefined')
    return devuelve;*/
  var resultStr = '';
  var i = 0;
  var longitud = 0;

  if (cadena.length == 0)
    resultado = '';
  else
  {
    longitud = cadena.length;
      while ((i <= longitud) && (cadena.charAt(i) == ' '))
      i++;
      resultado = cadena.substring(i,longitud);
    }
    return resultado;
}


function lTrim(cadena)
{

     cadena = cadena + '';
  if (cadena == 'undefined')
    cadena = '';
     return cadena.replace(/^\s*/gi, "");
}

/**************************************************************/
/*  Trim derecho   							  */
/**************************************************************/
function TrimR(cadenaDato)
{
  var devuelve = '';
  var cadena = cadenaDato +'';
  var resultStr = '';
  var i = 0;

  if (cadena.length == 0)
    resultado = '';
  else
  {
      i = cadena.length - 1;
      while ((i >= 0) && (cadena.charAt(i) == ' '))
      i--;
      resultado = cadena.substring(0, i + 1);
  }
  return resultStr;
}

function rTrim(cadena)
{
     cadena = cadena + '';
  if (cadena == 'undefined')
    cadena = '';
     return cadena.replace(/\s+$/gi, "");
}


/**************************************************************/
/* Si los 3 campos cubiertos comprueba si es una fecha valida */
/**************************************************************/
function esFecha (objDia,objMes,objAno)
{
  var dia = objDia.value;
  var mes = objMes.value;
  var ano = objAno.value;

  if ((dia.length == 2) && (mes.length == 2) && (ano.length == 4) )
  {
    dataValida = new Date(ano, mes - 1, dia);
    if  (formateaIntToString( Number( dataValida.getDate() )  , 2 ) != dia )
    {
      mostrarAlertAviso(UserErr(14872)); //icono admiracion
      //alert('O día non e válido');
      objDia.select();
      return false;
    }
    if  ( formateaIntToString( Number( dataValida.getMonth() ) , 2 ) != (mes - 1) )
    {
      mostrarAlertAviso(UserErr(14872)); //icono admiracion
      //alert('O mes non e válido');
      objMes.select();
      return false;
    }
    if ( ( formateaAno( Number( dataValida.getFullYear() )) != ano ) || (ano < 1990))
    {
      mostrarAlertAviso(UserErr(14872)); //icono admiracion
      //alert('O ano non e válido');
      objAno.select();
      return false;
    }
  }
  return true;
}

/**************************************************************/
/* Formatea el un año a 4 cifras							  */
/**************************************************************/
function formateaAno(cadena)
{
  if (cadena==null)
    cadena = '';
  var resultado = Trim(cadena.toString());
  var longCad = resultado.length;
  if (longCad == 2)
  {
    if (resultado > '80')
      resultado = '19' + resultado;
    else
        resultado = '20' + resultado;
  }
  else
    resultado = formateaIntToString(resultado,4);
  return resultado;
}

/**************************************************************/
/* Formatea un entero a un string de 'longitud' de caracteres */
/**************************************************************/
function formateaIntToString(cadena, longitud)
{
  if (cadena == null)
    cadena = '';
  var resultado = cadena.toString();
  var longCad = resultado.length;

  if (longCad > 0)
  {
    longitud = longitud - longCad;
    while (longitud != 0)
    {
      resultado = '0' + resultado;
      longitud --;
    }
  }
  else
  {
    resultado = '';
  }
  return resultado;
}

/******************************************* */
/* Compara dos fechas. Devuelve 0 si iguales */
/* 1 si la primera es mayor	             */
/* 2 si la segunda es mayor	             */
/******************************************* */
function comparaFechas(fecha1,fecha2)
{
  var devuelve;
  var resta = diasDiferencia(fecha1,fecha2);
  if (resta == 0)  //son iguales
    devuelve = 0;
  if (resta > 0)  //la primera es mayor
    devuelve = 1;
  if (resta < 0)
    devuelve =  2;  //la segunda es mayor
  return (devuelve);
}

/******************************************* */
/* Compara dos fechas con sus horas. Devuelve 0 si iguales */
/* 1 si la primera es mayor	             */
/* 2 si la segunda es mayor	             */
/******************************************* */
function comparaFechasHoras(fecha1,fecha2)
{
  var devuelve;
  var resta = minutosDiferencia(fecha1,fecha2);
  if (resta == 0)  //son iguales
    devuelve = 0;
  if (resta > 0)  //la primera es mayor
    devuelve = 1;
  if (resta < 0)
    devuelve =  2;  //la segunda es mayor
  return (devuelve);
}

/**************************************************************/
/* Obtiene los días de diferencia entre dos fechas    		  */
/**************************************************************/
function diasDiferencia(fecha1,fecha2)
{
  var SECOND = 1000;
  var MINUTE = 60*SECOND;
  var HOUR = 60*MINUTE;
  var DAY = 24*HOUR;
  var devuelve = 0;

  var resta = (fecha1.getTime() - fecha2.getTime())/DAY;
  return (Math.floor(resta));
}

/**************************************************************/
/* Obtiene los minutos de diferencia entre dos fechas    		  */
/**************************************************************/
function minutosDiferencia(fecha1,fecha2)
{
  var SECOND = 1000;
  var MINUTE = 60*SECOND;

  var resta = (fecha1.getTime() - fecha2.getTime())/MINUTE;
  return (Math.floor(resta));
}

/**************************************************************/
/* Obtiene la fecha actual en el formato dd/mm/yyyy  		  */
/**************************************************************/

function obtenerFechaString() {

  var fecha='';
  var fechaD = new Date();

  fecha = formateaIntToString(Number(fechaD.getDate()),2)+'/';
  fecha = fecha +formateaIntToString((Number(fechaD.getMonth())+1),2)+'/';
  fecha = fecha +Number(fechaD.getYear());

  return fecha;
}


/**************************************************************/
/* Compara dos fechas en el formato dd/mm/yyyy  	    	  */
/**************************************************************/

function comparaFechasString(dia1, mes1, ano1, dia2, mes2, ano2) {

  var fecha1 = dia1 + '/' + mes1 + '/' + ano1;
  var fecha2 = dia2 + '/' + mes2 + '/' + ano2;

  return (fecha1==fecha2);
}

/**************************************************************************************/
/* Comprueba si los 3 campos cubiertos son una fecha valida sin "soltar" alertas      */
/**************************************************************************************/
function esFechaSinAlertas (objDia,objMes,objAno)
{
  var dia = objDia.value;
  var mes = objMes.value;
  var ano = objAno.value;

  if ((dia.length == 2) && (mes.length == 2) && (ano.length == 4) )
  {
    dataValida = new Date(ano, mes - 1, dia);
    if  (formateaIntToString( Number( dataValida.getDate() )  , 2 ) != dia )
    {
      objDia.select();
      return false;
    }
    if  ( formateaIntToString( Number( dataValida.getMonth() ) , 2 ) != (mes - 1) )
    {
      objMes.select();
      return false;
    }
    if ( ( formateaAno( Number( dataValida.getFullYear() )) != ano ) || (ano < 1990))
    {
      objAno.select();
      return false;
    }
    return true;
  }else{
    if (dia.length == 0)
      objDia.select();
    if (mes.length == 0)
      objMes.select();
    if (ano.length == 0)
      objAno.select();
    return false;
  }

}

/**************************************************************************************/
/* Devuelve la fecha actual en el formato DD/MM/YYYY                                  */
/**************************************************************************************/
function fechaHoy()
   {

      var hoy = new Date();

      var dia = hoy.getDate().toString();
      if (dia.length == 1)
      {
         dia = '0' + dia;
      }
      var mes = (hoy.getMonth() + 1).toString();
      if (mes.length == 1)
      {
         mes = '0' + mes;
      }

      return( dia + '/' + mes + '/' + hoy.getFullYear());
   }

/**************************************************************************************/
/* Comprueba si los 2 campos cubiertos son una hora valida sin "soltar" alertas      */
/**************************************************************************************/
function esHoraSinAlertas (objHora,objMinuto){

  var hora = objHora.value;
  var minuto = objMinuto.value;

  if ((hora.length == 2) && (minuto.length == 2)) {
    if ((hora<0)||(hora>23)) {
        objHora.select();
          return false;
      }
    if ((minuto<0)||(minuto>59)) {
        objMinuto.select();
          return false;
      }
    return true;
  }else{
    if (hora.length == 0){
      objHora.select();
    }
    if (minuto.length == 0){
      objMinuto.select();
    }
    return false;
  }

}

   /**************************************************************************************/
   /* Función que muestra un mensaje mientras se carga la página.                        */
   /**************************************************************************************/

   function pleaseWait(valor) {
    if(top.frames["mainFrame"]!=undefined) {
       if(valor=='on'){
            top.frames["mainFrame"].document.getElementById('hidepage').style.display = 'inherit';
        } else if(valor=='off')	{
            top.frames["mainFrame"].document.getElementById('hidepage').style.display = 'none';
        }
    } else {
        pleaseWaitSinFrame(valor);
    }
   }
   
   function pleaseWaitCustom(value, msg, defaultMsg, width, height) {
       var hidepage = top.frames["mainFrame"].document.querySelectorAll('#hidepage')[0];
       var contenedorHidepage = top.frames["mainFrame"].document.querySelectorAll('#hidepage > .contenedorHidepage')[0];
       if(!width) {
           width = 275; //reestablecer
       }
       if(!height) {
           height = 254; //reestablecer
       }
       if(!msg) {
           msg = defaultMsg;
       }
       contenedorHidepage.style.width = width + 'px';
       contenedorHidepage.style.height = height + 'px';
       top.frames["mainFrame"].document.querySelectorAll('#hidepage > .contenedorHidepage > .textoHide > span')[0].innerHTML = msg;
       
       pleaseWait(value);
   }

   function pleaseWait1(valor,frame) {
        if(valor=='on') {
          frame.document.getElementById('hidepage').style.display = 'inherit';
        } else if(valor=='off') {
          frame.document.getElementById('hidepage').style.display = 'none';
        }
   }
   
   function pleaseWaitSinFrame(valor) {
       var hidepage = document.getElementById('hidepage');
       if(!hidepage){
           hidepage = parent.document.getElementById('hidepage');
       }
       
        if(valor=='on') {
          hidepage.style.display = 'inherit';
        } else if(valor=='off') {
          hidepage.style.display = 'none';
        }
   }
// Legacy compatibility: some pages call `pleaseWailt`
if (typeof window.pleaseWailt === "undefined") {
    function pleaseWailt(valor) {
        return pleaseWait(valor);
    }
}
   /**************************************************************************************/
   /* Función que modifica la variable de control de carga de procesos desde el menu     */
   /**************************************************************************************/

   function modificando(valor)
   {

      top.menu.modificando = valor;

   }


   /**************************************************************************************/
   /*            Funciones que controlan las teclas presionadas                          */
   /**************************************************************************************/

function checkKeysLocal(){
   //** Esta funcion se debe implementar en cada JSP para particularizar  **//
   //** las acciones a realizar de las distintas combinaciones de teclas  **//

   return false;
}


function keyDel(evento){

if(evento!=null){ 
    if(evento.keyCode){
        if(evento.keyCode==8){
            //evento.keyCode = 0;
            return false;
            }
    }else
        if(evento.which){
            if(evento.which==8){
            //evento.which = 0;
            return false;
            }
        }

}
}

function checkKeys(evento){

    var aux = null;
    if(window.event)
        aux = window.event;
    else
        aux = evento;

   //******  Captura las teclas presionadas  *****//

   //Obtenemos las teclas presionadas
   var tecla = getKeys(aux);

   //Particularizamos llamadas a funciones locales del JSP
   checkKeysLocal(evento,tecla);
}




function getKey(evento)
{  
    var tecla;
    if(evento.keyCode)
        tecla =evento.keyCode;
    else
        tecla = evento.which;

 // Devuelve el valor alfanumerico de la tecla presionada   
 switch(tecla){
   case 65:
      return ("A");break;
   case 66:
      return ("B");break;
   case 67:
      return ("C");break;
   case 68:
      return ("D");break;
   case 69:
      return ("E");break;
   case 70:
      return ("F");break;
   case 71:
      return ("G");break;
   case 72:
      return ("H");break;
   case 73:
      return ("I");break;
   case 74:
      return ("J");break;
   case 75:
      return ("K");break;
   case 76:
      return ("L");break;
   case 77:
      return ("M");break;
   case 78:
      return ("N");break;
   case 79:
      return ("O");break;
   case 80:
      return ("P");break;
   case 81:
      return ("Q");break;
   case 82:
      return ("R");break;
   case 83:
      return ("S");break;
   case 84:
      return ("T");break;
   case 85:
      return ("U");break;
   case 86:
      return ("V");break;
   case 87:
      return ("W");break;
   case 88:
      return ("X");break;
   case 89:
      return ("Y");break;
   case 90:
      return ("Z");break;
   default:
      return("");break;
 }
}


function getKeys(evento){
   //  Devuelve combinaciones de teclas presionadas
   var letra = getKey(evento);

   if (evento.altKey && (letra != "")){
      return("Alt+" + letra);
   }else
   if (evento.ctrlKey && (letra != "")){
      return("Ctrl+" + letra);
   }else
   return letra;
}

document.onkeydown=checkKeys;

function textCounter(field, maxlimit) {
	if ((field.value.length > maxlimit) && !((event.keyCode == 8)||(event.keyCode == 37)||(event.keyCode == 38)||
											 (event.keyCode == 39)||(event.keyCode == 40)||(event.keyCode == 46)))
		return false;
	else
		return true;
}

function irActionConCambios(valorVariable,mensaje)
{    
  var irAlAction=0;
  if(valorVariable==0)
	{
		irAlAction=1;
	}
	else
	{
		if((jsp_alerta('C',mensaje)==1))
		{
			irAlAction=1;
		}
		else
		{
			irAlAction=0;
		}
	}    
    return irAlAction;
}

function modificaVariableCambios(valor)
{
   //Sin nada, porque se redefine en fichaExpediente y no se redefine en busqueda por campos. Al usar las mismas plantillas es necesario esta funcion en blanco

}

/**
   Esta función será la que se utiliza para convertir a mayusculas el contenido de los campos de texto en lugar de la función PasaAMayusculas.
   Se combina con el estilo para las cajas de texto que pone el contenido del campo en mayusculas
   */
function xAMayusculas(objeto){
    var cadena1 = "\"";
    var cadena2 = "\\";
    var cadena3 = "\%";

    if (objeto){
            var original = objeto.value;
            var salida = "";
            for(i=0;original!=undefined && i<original.length;i++){
                    if(cadena1.indexOf(original.charAt(i))==-1 && cadena2.indexOf(original.charAt(i))==-1 && cadena3.indexOf(original.charAt(i))==-1){
                            salida = salida + original.charAt(i);
                    }
            }
            objeto.value=salida.toUpperCase();
    }
}


function SoloCaracterValidos(objeto) {
   var valores='1234567890ABCDEFGHIJKLMNÑOPQRSTUVWYXZ';
   var numeros='1234567890';
   xAMayusculas(objeto);

    if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;i<original.length;i++){
            
            if(valores.indexOf(original.charAt(i).toUpperCase())!=-1 || numeros.indexOf(original.charAt(i).toUpperCase())!=-1 || original.charAt(i)==" "){
                salida = salida + original.charAt(i);
            }
        }
        objeto.value=salida.toUpperCase();
    }
}


 function SoloDigitosConsulta(objeto) {      
      xAMayusculas(objeto);
      var tecla, caracter;
      var numeros= "0123456789&|():><!=";

      if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;i<original.length;i++){
            if(numeros.indexOf(original.charAt(i).toUpperCase())!=-1){
                salida = salida + original.charAt(i);
            }
        }
        objeto.value=salida.toUpperCase();
     }//if
  }


function SoloDigitosNumericos (objeto){
    var tecla = "";
    if(event.keyCode)
        tecla = event.keyCode;
    else
        tecla = event.which;
    
    if (tecla!=9)      {     //NO ES EL TABULADOR
        xAMayusculas(objeto);
        var numeros= "0123456789";
        if (objeto){
            var original = objeto.value;
            var salida = "";
            for(i=0;original!=undefined && i<original.length;i++){
                if(numeros.indexOf(original.charAt(i).toUpperCase())!=-1 || numeros.indexOf(original.charAt(i).toUpperCase())!=-1){
                    salida = salida + original.charAt(i);
                }
            }
            objeto.value=salida.toUpperCase();
        }//if
    } else objeto.select();
}

 /**
   Hace lo mismo que xAMayusculas pero sin pasar a mayúsculas
   */
    function xValidarCaracteres(objeto)
	{
		var cadena1 = "\"";
		var cadena2 = "\\";
		var cadena3 = "\%";
                var retornar=false;

		if (objeto){
			var original = objeto.value;
			var salida = "";
			for(i=0;original!=undefined && i<original.length;i++){
				if(cadena1.indexOf(original.charAt(i))==-1 && cadena2.indexOf(original.charAt(i))==-1 && cadena3.indexOf(original.charAt(i))==-1){
					salida = salida + original.charAt(i);
				}else retornar=true;
				}
			if(retornar)objeto.value=salida;
		}
	}


function SoloCaracteresFechaConsultando(objeto) {
   var valores= "0123456789/_-.:|&<>!";
   xAMayusculas(objeto);

    if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;i<original.length;i++){
            if(valores.indexOf(original.charAt(i).toUpperCase())!=-1){
                salida = salida + original.charAt(i);
            }
        }
        objeto.value=salida.toUpperCase();
    }
}


function SoloAlfanumericos(objeto) {    
    xAMayusculas(objeto);
    var alfanumericos = '0123456789abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚáéíóú';

    if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;i<original.length;i++){
            if(alfanumericos.indexOf(original.charAt(i).toUpperCase())!=-1){
                salida = salida + original.charAt(i);
            }
        }
        objeto.value=salida.toUpperCase();
    }
}
function SoloAlfanumericos_espacio(objeto) {    
    xAMayusculas(objeto);
    var alfanumericos = '0123456789abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚáéíóú ';

    if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;i<original.length;i++){
            if(alfanumericos.indexOf(original.charAt(i).toUpperCase())!=-1){
                salida = salida + original.charAt(i);
            }
        }
        objeto.value=salida.toUpperCase();
    }
}

/* ======================================================================
Funcion: SoloMail
Bloquea un input Texto para que sólo acepte alfanumericos
========================================================================== */
function SoloMail(objeto) {
   // PasaAMayusculas(e);

    var tecla, caracter;
    var alfanumericos = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_@.';

    if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;i<original.length;i++){
            if(alfanumericos.indexOf(original.charAt(i))!=-1){
                salida = salida + original.charAt(i);
            }
        }
        objeto.value=salida;
    }
}

function bloquearBotonesTemporal(boton){
    eval ("document.getElementsByName('"+boton+"').disabled=true;");
	var instruccion = "eval(document.getElementsByName('"+boton+"').disabled=false);";
    var t=setTimeout(instruccion,1500);
}

//RUTINAS BASICAS CROSSBROWSER

/** Controla los caracteres para aquellos campos que admiten minúsculas **/
function ControlarCaracteres(e) {
 var IEXPLORER = true;
  if(window.event){
      e =window.event;      
  }else
      IEXPLORER = false;

   if(IEXPLORER){
       
       // Internet Explorer
      e.keyCode = String.fromCharCode(e.keyCode).charCodeAt(0);

      cadena = "\"";
      if ((cadena.indexOf(String.fromCharCode(e.keyCode)) != -1)){
         e.keyCode = " ";
      }

      cadena = "\\";
      if ((cadena.indexOf(String.fromCharCode(e.keyCode)) != -1)){
         e.keyCode = " ";
      }

      cadena = "\%";
      if ((cadena.indexOf(String.fromCharCode(e.keyCode)) != -1)){
         e.keyCode = " ";
      }
  }else{

      cadena = "\"";
      if ((cadena.indexOf(String.fromCharCode(e.which)) != -1)){
        e.cancelBubble = true;
         e.preventDefault();
      }

      cadena = "\\";
      if ((cadena.indexOf(String.fromCharCode(e.which)) != -1)){         
         e.cancelBubble = true;
         e.preventDefault();
      }

      cadena = "\%";
      if ((cadena.indexOf(String.fromCharCode(e.which)) != -1)){         
         e.cancelBubble = true;
         e.preventDefault();
      }

  }
}

//SE EJECUTA PARA FIREFOX CUANDO PINCHAMOS FUERA DE LOS LIMITES DEL COMBO
function isClickOutCombo(combo,coordx,coordy)
{
    var alto=combo.base.style.height;
    var ancho=combo.base.style.width;
    var top=combo.base.style.top;
    var izq=combo.base.style.left;
    izq=parseInt(izq.replace("px",""));
    top=parseInt(top.replace("px",""));
    ancho=parseInt(ancho.replace("px",""));
    alto=parseInt(alto.replace("px",""));

    var posX=izq+ancho;
    var posY=top+alto;
  
    if((coordx<izq)||(coordx>posX)||(coordy<top)||(coordy>posY))
    {
        return true;
    }

    else return false;

}

//SE EJECUTA CUANDO PINCHAMOS FUERA DE LOS LIMITES DEL COMBO
function isClickOutComboContains(combo,evento)
{
    var vistaDesplegable = combo.view;
    var vistaDescripcion = combo.des;
    var vistaAnchor = combo.anchor;
    
    if (!vistaDescripcion.contains(evento.target)
            && !vistaDesplegable.contains(evento.target)
            && !vistaAnchor.contains(evento.target)) {
        return true;
    } else {
        return false;
    }
}

function borrarYDeshabilitarExpediente() {
    document.forms[0].cod_procedimiento.value='';
    document.forms[0].desc_procedimiento.value='';
    document.forms[0].txtExp1.value = '';
    var vectorExp = [document.forms[0].txtExp1];
    deshabilitarGeneral(vectorExp);
    var vectorImg = new Array(document.getElementsByName("consultaExpediente")[0]);
    deshabilitarImagen(vectorImg, true);
}

function habilitarExpediente() {
    var vectorImg = new Array(document.getElementsByName("consultaExpediente")[0]);
    habilitarImagen(vectorImg, true);
    var vectorExp = [document.forms[0].txtExp1];
    habilitarGeneral(vectorExp);

}


function SoloCaracterNumeroExpediente(objeto) {
   var valores='1234567890ABCDEFGHIJKLMNÑOPQRSTUVWYXZ/';
   var numeros='1234567890';
   xAMayusculas(objeto);

    if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;i<original.length;i++){

            if(valores.indexOf(original.charAt(i).toUpperCase())!=-1 || numeros.indexOf(original.charAt(i).toUpperCase())!=-1 || original.charAt(i)==" "){
                salida = salida + original.charAt(i);
            }
        }
        objeto.value=salida.toUpperCase();
    }
}



    /**
     * Consulta al servidor cual es el número de interesados de un expediente en ese momento
     * @returns int
     */
    function getNumInteresadosExpediente()
    {         
        var ajax = getXMLHttpRequest();
        var numExpediente = document.forms[0].numero.value;   
        var numero = 0;
        
        if(ajax!=null)
        {               
            var url = APP_CONTEXT_PATH + "/sge/TramitacionExpedientes.do";              
            var parametros = "&opcion=numeroInteresadosExpediente&numExpediente=" + escape(numExpediente);
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);
            try
            {            
                if (ajax.readyState==4 && ajax.status==200)
                {                    
                   var text = ajax.responseText;                        
                   var salida = text.split("=");
                   if(salida!=null && salida.length==2){
                       if(salida[0]=="numero"){
                           numero = parseInt(salida[1]);
                       }
                   }
                }                
            }catch(Err){
                alert("Error.descripcion: " + Err.description);
            }
        }
        return numero;
    }
    
/**************************************************************************/
/* Funciones para manejar ventanas auxiliares no modales que de cara al usuario simulan */
/* showModalDialog                                                                                                                  */
/**************************************************************************/
function retornoXanelaAuxiliar(){
}
function rehabilitarXanelaPrincipal(){
    if (parent.frames["mainFrame"] && parent.frames["mainFrame"].document.getElementById("divBackground")){
        parent.frames["mainFrame"].document.body.removeChild(parent.frames["mainFrame"].document.getElementById("divBackground"));
    } else
        if (document.getElementById("divBackground"))   
            document.body.removeChild(document.getElementById("divBackground"));
    
    if (parent.frames["menu"] && parent.frames["menu"].document && parent.frames["menu"].document.getElementById("divBackgroundCab"))
        parent.frames["menu"].document.body.removeChild(parent.frames["menu"].document.getElementById("divBackgroundCab"));

    window.focus();
}
function abrirXanelaAuxiliar(fonte,argumentos,opcions,despois){
    var eChrome = !!window.chrome && !!window.chrome.webstore;
    var eFirefox = typeof InstallTrigger !== 'undefined';
    var ancho = 900;
    var alto = 500;
    if (opcions!=null) {
        opcions=opcions.replace(/\s+/g,'');
        opcions= opcions.toLowerCase();
        if (opcions.indexOf('menubar') < 0)
           opcions += ',menubar=no';
        
        if (opcions.indexOf('resizable') < 0)
           opcions += ',resizable=yes';
        
        if (opcions.indexOf('titlebar') < 0)
           opcions += ',titlebar=no';
        
        if (opcions.indexOf('toolbar') < 0)
           opcions += ',toolbar=no';

        var posWidth = opcions.indexOf('width');
        if (posWidth < 0)
           opcions += ',width=900';
       else {
           posWidth += 6;
           var auxOp = opcions.substring(posWidth);
           var posFin = auxOp.indexOf(",")==-1?0:auxOp.indexOf(","); 
           ancho = auxOp.substring(0,posFin);
       }
        
        var posHeight = opcions.indexOf('height');
        if (opcions.indexOf('height') < 0)
           opcions += ',height=500';
       else {
           posHeight += 7;
           var auxOp = opcions.substring(posHeight);
           var posFin = auxOp.indexOf(",")==-1?0:auxOp.indexOf(",");
           alto = auxOp.substring(0,posFin);
       }
    } else {
        opcions += 'menubar=no,resizable=yes,titlebar=no,toolbar=no,width=900,height=500';
    }
    var posX = 0;
    var posY = 0;
    
    if (eFirefox) {
        posX = window.screenX+(window.outerWidth - ancho)/2;
        posY = window.screenY + (window.outerHeight - alto)/2;
        opcions += ',left=' + posX + ',top=' + posY;
    } 
    
    xanelaAuxiliarArgs = argumentos;
    xanelaAuxiliar = window.open(fonte,"_blank",opcions);
    
    if (!eFirefox){
        posX = window.screenX+(window.outerWidth - ancho)/2;
        posY = window.screenY + (window.outerHeight - alto)/2;
        xanelaAuxiliar.moveTo(posX,posY);
    }
        
    temporizadorRehabilitarXanela = setInterval(function() {   
        if(xanelaAuxiliar && xanelaAuxiliar.closed) {
            clearInterval(temporizadorRehabilitarXanela);
            rehabilitarXanelaPrincipal();
            despois();
        }  
    }, 1000); 
    window.onbeforeunload = function(){
        if (xanelaAuxiliar && !xanelaAuxiliar.closed)
            xanelaAuxiliar.close();
    };
    if (!eFirefox && !eChrome)
        window.onfocus = function(){
             if(xanelaAuxiliar && !xanelaAuxiliar.closed){
                xanelaAuxiliar.focus();
            }
        };
    
    if (parent.mainFrame){
        var divBackground = document.createElement("div");   
        divBackground.setAttribute("id","divBackground");
        parent.frames["mainFrame"].document.body.appendChild(divBackground);
    }else{
        var divBackground = document.createElement("div");   
        divBackground.setAttribute("id","divBackground");
        document.body.appendChild(divBackground);
    }
    if (parent.frames["menu"] && parent.frames["menu"].name){
        var divBackgroundCab = document.createElement("div");   
        divBackgroundCab.setAttribute("id","divBackgroundCab");
        parent.frames["menu"].document.body.appendChild(divBackgroundCab);
    }

    window.retornoXanelaAuxiliar = function (retorno){
            clearInterval(temporizadorRehabilitarXanela);
            xanelaAuxiliar.close();
            rehabilitarXanelaPrincipal();
            despois(retorno);
        };
      
    xanelaAuxiliar.focus();
}

function navegadorEsIE9(){
    var navegador = navigator.appName; 
    var version = navigator.appVersion;
    if (navegador == "Microsoft Internet Explorer" && version.indexOf("MSIE 9.0") != -1) 
	return true;
    return false;
}

// Clona un array
function clonarArray(existingArray) {
   var newObj = (existingArray instanceof Array) ? [] : {};
   
   for (i in existingArray) {
      if (i == 'clone') continue;
      if (existingArray[i] && typeof existingArray[i] == "object") {
         newObj[i] = clonarArray(existingArray[i]);
      } else {
         newObj[i] = existingArray[i]
      }
   }
   
   return newObj;
}

// Obtiene la extension de un fichero
function getFilenameExtension(filename) {
    return filename.slice((Math.max(0, filename.lastIndexOf(".")) || Infinity) + 1);
}

/* 
 * Vuelve a la pantalla principal desde un popup
 */
function volverPantallaPrincipal(valorRetorno) {
    self.parent.opener.retornoXanelaAuxiliar(valorRetorno);
}

/*
 * Padding a la izquierda con el caracter (charRellenar) deseado
 * y con una longitud minima (len)
 * 
 * @param {type} cadena cadena deseada
 * @param {type} charRellenar caracteres a rellenar
 * @param {type} len cantidad de 
 * @returns {String}
 */
function leftPad(cadena, charRellenar, len) {
	var valor = ((new Array(len + 1).join(charRellenar)) + cadena).slice(-len);
	
	return valor;
}

/*
 * Padding a la izquierda con el caracter '0'
 * y con una longitud minima (len)
 * 
 * @param {type} cadena cadena deseada
 * @param {type} len cantidad de 
 * @returns {String}
 */
function leftPadZero(cadena, len) {
	var valor = ((new Array(len + 1).join('0')) + cadena).slice(-len);
	
	return valor;
}

/**
 * Muestra en pantalla el mensaje retornado por el objeto ResultadoAjax.java
 * 
 * @param {type} ajaxResult objeto de tipo ResultadoAjax.java
 * @param {type} msjGenerico mensaje generico que se muestra en caso de no existir mensaje en ajaxResult
 * @param {type} tipoVentanaDialogo tipo de ventana de dialogo para la funcion jsp_alerta (por defecto 'A')
 * @returns {Number} valor devuelto por jsp_alerta
 */
function mostrarVentanaMensajeStatusAjax(ajaxResult, msjGenerico, tipoVentanaDialogo) {
    var msgError = obtenerMensajeStatusAjax(ajaxResult, msjGenerico);
    
    var tipoVentana = 'A';
        if (!tipoVentanaDialogo) {
        tipoVentana = tipoVentanaDialogo;
    }

    return jsp_alerta(tipoVentana, msgError);
}

/**
 * Extrae el mensaje retornado por el objeto ResultadoAjax.java o, si no existe, el mensaje por defecto pasado
 * por parametro.
 * 
 * @param {type} ajaxResult objeto de tipo ResultadoAjax.java
 * @param {type} msjGenerico mensaje generico que se muestra en caso de no existir mensaje en ajaxResult
 * @returns {String} mensaje devuelto por el objeto o el mensaje generico
 */
function obtenerMensajeStatusAjax(ajaxResult, msjGenerico) {
    var msgError = "";
    if (ajaxResult && ajaxResult.descStatus) {
        msgError = ajaxResult.descStatus;
    } else if (msjGenerico) {
        msgError = msjGenerico;
    } else {
        msgError = "Error interno de la aplicación";
    }

    return msgError;
}

/**
 * Procesa la respuesta ajax. Si hay error muestra el mensaje de error devuelto.
 * Si hay error y el mensaje esta vacio se muestra el mensaje de error pasado
 * como parametro.
 * Si la respuesta es ok (codigo devuelto 0) se procesa la funcion callback.
 * 
 * @param {type} ajaxResult objeto de tipo ResultadoAjax.java
 * @param {type} callback funcion que se ejecuta si la respuesta es OK
 * @param {type} msjErrorGenerico mensaje de error por defecto
 */
/* Funciones ajax comunes */
function successAjax(ajaxResult, callback, msjErrorGenerico) {
    var jsonResult = null;
    
    if (ajaxResult) {
        jsonResult = JSON.parse(ajaxResult);
    }

    if (jsonResult && jsonResult.status === 0) {
        callback(jsonResult.resultado);
    } else {
        errorAjax(ajaxResult, msjErrorGenerico);
    }
}

/**
 * Muestra una pantalla el error devuelto por el objeto ajax. El objeto es del tipo
 * ResultadoAjax definido en java.
 * Incluye llamada a la funcion pleaseWait('off').
 * 
 * @param {type} ajaxResult objeto de tipo ResultadoAjax.java
 * @param {type} msjErrorGenerico mensaje de error por defecto
 */
function errorAjax(ajaxResult, msjErrorGenerico) {
    var json = null;

    if (ajaxResult) {
        try {
            json = JSON.parse(ajaxResult);
        } catch (err) {
            json = null;
        }
    }
    
    mostrarVentanaMensajeStatusAjax(
            json,
            msjErrorGenerico,
            'A'
            );

    pleaseWait('off');
}

/* Crea el radiobutton de tipo de firma */
function crearGrupoRadioButtons(idEtiquetaPadre, grupoRadiobuttons, valores, titulo) {
    if (idEtiquetaPadre && grupoRadiobuttons && valores && valores.codigo && valores.codigo.length > 0) {
        var codigos = valores.codigo;
        var descripciones = valores.descripcion;

        // Titulo de los radio buttons
        var innerHtml = '';
        if (titulo) {
            innerHtml = '<div><label class="etiqueta">' + titulo + ':</label></div><div>';
        }

        // RadioButtons
        for (var i = 0; i < codigos.length; i++) {
            var label = '<label id="' + grupoRadiobuttons + codigos + '">' + descripciones[i] + '</label>';
            var radioButton = 
                    '<input type="radio" name="' + grupoRadiobuttons + '" value="' + codigos[i] + '" />';
            innerHtml = innerHtml + label + radioButton;
        }

        document.getElementById(idEtiquetaPadre).innerHTML = innerHtml + '</div>';
    }
}

/**
 * Función auxiliar para convertir un JSON a un objeto sin lanzar excepciones
 * 
 * @param {type} object JSON a convertir a un objeto
 * @return {String} objeto objeto resultante del JSON
 */
function parseJSON(json) {
    var object = null;

    if (json) {
        try {
            object = JSON.parse(json);
        } catch (err) {
            object = null;
        }
    }

    return object;
    }
