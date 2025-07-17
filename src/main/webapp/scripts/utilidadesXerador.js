//
// Utilidades varias
//

/* ============================================================================================
DESC: Posiciona un combo por el value
=============================================================================================== */
function PosicionarComboPorValue(combo,elemento)
{
        for (i=0;i < combo.options.length;i++){         
                
                if(combo.options[i].value == elemento)
                {
                        combo.selectedIndex = i;
                        i = combo.options.length; //Pa q salga
                }
        }
}



/* ======================================================================
FUNCTION:	Trim
			 
INPUT:  		str (string): the string to be altered
	
RETURN: 		A string with no leading or trailing spaces;
				returns null if invalid arguments were passed

DESC:			This function removes all leading and tralining spaces from a string.

CALLS:		This function depends on the functions TrimLeft & TrimRight
				They must be included in order for this function to work properly.
====================================================================== */
function Trim( str ) {
	var resultStr = "";
	
	resultStr = TrimLeft(str);
	resultStr = TrimRight(resultStr);
	
	return resultStr;
} // end Trim


function estaSeguroBen() {
return '¿Está seguro?';
}
/* ======================================================================
FUNCTION:       unescapeArray
 
INPUT:  miArray: Array en el que hacer unescape a las cadenas
DESC:   hace un unescape a String en miArray (o array de arrays)
	CUIDADIN: ¡¡Es recursiva!! Tiene recursividad terminal así que
	es trivial pasarlo a iterativo.Se usa la recta y listo.
====================================================================== */
function unescapeArray(miArray)
{
var valor;
if ( (miArray) && (miArray.length) ) {
	for (var i=0;i<miArray.length;i++) {
		valor=miArray[i];
		if (valor.reverse) { // Testeo si es un array
			// si es un array recursivo !!! no me fio un pelo de la recursividad en javascript
			unescapeArray(valor);
			} else { // Si no es un array es un string normal
			miArray[i]=unescape(valor);
			}
	}
}
return miArray;
}


/* ============================================================================================
DESC: Borra todos los options de un combo
=============================================================================================== */
function LimpiaCombo(contenedor)
{
	if (contenedor.options)
	{
	var numOpciones = contenedor.options.length-1; 
	for (i= numOpciones; i>=0 ; i--)
	{
		contenedor.options.remove(contenedor.options[i]);
	}
	}
}




function cargaObjetoComboDesdeCombo(objetoCombo,combo)
{
var codigos=new Array();
var descs=new Array();

for (var i=0;i<combo.options.length;i++) {
	codigos[i]=combo.options[i].value;
	descs[i]=combo.options[i].text;
}
objetoCombo.addItems(codigos,descs);
}


/* ============================================================================================
DESC: Devuelve referencia al objeto con id que se le pasa
=============================================================================================== */
function getObjetoPorId(id) {
var elemento;

if (document.all) elemento=eval('document.all.'+id);
else if (document.getElementById) elemento=eval('document.getElementById(\''+id+'\')');

return elemento;

}


/* ============================================================================================
DESC: Cubre el combo (text,value) con el array de arrays
=============================================================================================== */
function CubreComboValueTexto(combo,arrayDatos,paramTextNoSelec,escapeado,nousarValorDefecto)
{
		var textoNoSelec = 'SEN SELECCION';
		if ((paramTextNoSelec)&& (paramTextNoSelec!=null)&&(paramTextNoSelec!=''))
			textoNoSelec = paramTextNoSelec;
		var opcion = new Option();
		
		if (!nousarValorDefecto) 
		{
			opcion.value = '';
			opcion.text = textoNoSelec;   
			combo.options[combo.options.length]= opcion;
		}
		
        for (i=0;i < arrayDatos.length;i++)
		{
			opcion = new Option();
			opcion.value = escapeado?unescape(arrayDatos[i][0]):(arrayDatos[i][0]);
			opcion.text = escapeado?unescape(arrayDatos[i][1]):(arrayDatos[i][1]);
			                         
			combo.options[combo.options.length]= opcion;
        }
}



function HabilitaBoton(boton,valor)
{

    var botonValor = boton.id;
    var elemento = getObjetoPorId(botonValor);

    if (valor)
        elemento.style.color  = '#ffffff';
    else
        elemento.style.color  = '#CCCCCC';

        boton.disabled = !valor;
        
}
//
//
//

/* ============================================================================================
DESC: Valida el formato de un campo fecha
PARAM: 	elemento -- El campo fecha
		num -- Si 0 --> Muestra alerts 
			   sino --> No muestra alerts  
=============================================================================================== */
function validarFecha(elemento,num) {
//Compruebo que el formato es valido
		if (elemento.value =='')
			return true;
        var fechaHoy=new Date();
        var mensaje='Data non v'+unescape('%E1')+'lida.';
				
		var aux = new Array(); 		
		aux = elemento.value.split('/');
		if (aux.length == 1)
		{
			aux = new Array(); 		
			aux = elemento.value.split(' ');
			if (aux.length == 1)
			{
				aux = new Array(); 		
				aux = elemento.value.split('-');
			}	
		}	
		if (aux.length != 3)
		{
                if (num==0)
                        alert('Data non v'+unescape('%E1')+'lida.');
                elemento.select();                       
                return false;
		}
		
		var dia=aux[0];
        var mes=aux[1];
        var anho=aux[2];

        var mes_dias=new Array(13);
        mes_dias[1]=31;
        mes_dias[2]=29;
        mes_dias[3]=31;
        mes_dias[4]=30;
        mes_dias[5]=31;
        mes_dias[6]=30;
        mes_dias[7]=31;
        mes_dias[8]=31;
        mes_dias[9]=30;
        mes_dias[10]=31;
        mes_dias[11]=30;
        mes_dias[12]=31;

        if (mes<1 || mes>12) {
                if (num==0)
                        alert('Mes non v'+unescape('%E1')+'lido.');
                elemento.select();                       
                return false;
        }
        if ((dia<1) || (dia>mes_dias[parseInt(mes)]) || (dia==29 && parseInt(mes)==2 && Math.round(anho-(Math.floor(anho/4)*4))!=0)) {
                if (num==0)
                        alert('D'+unescape('%ED')+'a non v'+unescape('%E1')+'lido.');
                elemento.select();                       
                return false;
        }
        var numeros="0123456789";
        for (i=0;i<4;i++)
                if (numeros.indexOf(anho.substring(i,i+1))==-1) {
                        if (num==0)
                                alert('Caracteres err'+unescape('%F3')+'neos no ano da data.');
                        elemento.select();                       
                        return false;
                }
		elemento.value =dia+"/"+mes+"/"+anho;
        return true;    
}

/* ============================================================================================
DESC: Valida el formato de un campo hora
PARAM: 	elemento -- El campo fecha
		num -- Si 0 --> Muestra alerts 
			   sino --> No muestra alerts  
=============================================================================================== */
function validarHora(elemento,num) {
//Compruebo que el formato es valido
		if (elemento.value =='')
			return true;
        var fechaHoy=new Date();
        var mensaje="Hora non válida.";
				
		var aux = new Array(); 		
		aux = elemento.value.split(':');
		if (aux.length == 1)
		{
			aux = new Array(); 		
			aux = elemento.value.split(' ');
		}	
		if (aux.length != 2)
		{
                if (num==0)
                        alert('Hora non v'+unescape('%E1')+'lida.');
                elemento.select();                       
                return false;
		}
		
		var hora=aux[0];
        var minu=aux[1];

        if (hora<0 || hora>23) 
		{
                if (num==0)
                        alert('Hora non v'+unescape('%E1')+'lida.');
                elemento.select();                       
                return false;
        }
        if (minu<0 || minu>59) 
		{
                if (num==0)
                        alert('Minutos non v'+unescape('%E1')+'lidos.');
                elemento.select();                       
                return false;
        }
		elemento.value =hora+":"+minu;
        return true;    
}





// FECHAS CON FORMATOS: DDMMYY, DDMMYYYY,DD/MM/YY,DD/MM/YYYY,DD-MM-YY,DD-MM-YYYY,DD MM YY,DD MM YYYY.
function soloCaracteresFecha(e) {                
    PasaAMayusculas(e);    
    var tecla, caracter;
    var alfanumericos = '0123456789/-';
    tecla = e.keyCode;
   	if (tecla == null) return true;
    caracter = String.fromCharCode(tecla);        
    if ((alfanumericos.indexOf(caracter) != -1) || tecla==32)
		return true;
    return false;   
}


// HORA: HH:MM, HHMM,HH MM
function soloCaracteresHora(e) {                      
    PasaAMayusculas(e);    
    var tecla, caracter;
    var alfanumericos = '0123456789:';
    tecla = e.keyCode;
   	if (tecla == null) return true;
    caracter = String.fromCharCode(tecla);        
    if ((alfanumericos.indexOf(caracter) != -1) || tecla==32)
		return true;
    return false;   
}




 /* ======================================================================
Funcion: SoloNumeros
Bloquea un input Texto para que sólo acepte números
========================================================================== */

function SoloDigitos(e) {                      

   PasaAMayusculas(e);    

    var tecla, caracter;
    var numeros='0123456789';

    tecla = e.keyCode;

    if (tecla == null) return true;
		
    caracter = String.fromCharCode(tecla);
      
    if (numeros.indexOf(caracter) != -1)
		return true;
    return false;   
}