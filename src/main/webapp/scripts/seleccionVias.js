var enlacesPagina  = 4;
var lineasPagina   = 10;

var paginaActual   = 1;
var paginaInferior = 1;
var paginaSuperior = enlacesPagina;

var inicio = 0;
var fin    = 0;

var listaOriginal = new Array();
var lista         = new Array();

var codigo        = new Array();
var descripcion   = new Array();
var codTVia       = new Array();
var descTVia      = new Array();

var respuesta     = new Array(4);
var codSel  = "";
var descSel = "";
var codTViaSel = "";
var descTViaSel = "";

//Cargamos el vector de datos del frame "principal"

codigo = opener.arrayC;
descripcion = opener.arrayD;
codTVia = opener.arrayCodTVias;
descTVia = opener.arrayDescTVias;

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

function cargarInicio()
{
   listaSel = lista;
   inicializaLista(1);
}

function enlaces()
{
   var htmlString = " ";
   var numeroPaginas = Math.ceil(listaSel.length/lineasPagina);
   
   if (numeroPaginas > 1)
   {
      htmlString += '<table border="0" cellpadding="2" cellspacing="0" align="center"><tr>'
 
      if (paginaActual > 1) {
	  	 htmlString += '<td class="cabecera" width="40" align="center">&nbsp;';
         htmlString += '<a href="javascript:inicializaLista('+ (paginaActual - 1) + ')" target="_self"><b><font color="white"> << </a>&nbsp;&nbsp;';
      } else      htmlString += '<td width="40" align="center">&nbsp;';

      htmlString += '</td><td class="fondob" align="center" width="100">';
      //Calculamos el intervalo de paginas a enlazar
      if (paginaActual > paginaSuperior)
      {
         paginaInferior = paginaInferior + 1;
         paginaSuperior = paginaSuperior + 1;
      }
      if (paginaActual < paginaInferior)
      {
         paginaInferior = paginaInferior - 1;
         paginaSuperior = paginaSuperior - 1;
      }      
      for (var i=0; i < numeroPaginas; i++)
      {
         if (((i+1)>= paginaInferior) && (i<paginaSuperior))
         {
            if ((i+1) == paginaActual)
               htmlString += '<font color="green">'+ (i+1) + '</font>&nbsp;&nbsp;';
            else
               htmlString += '<a href="javascript:inicializaLista('+ (i+1) + ')" target="_self">'+ (i+1) + '</a>&nbsp;&nbsp;';
         }
      }
 
      if (paginaActual < numeroPaginas){
	  	 htmlString += '</td><td class="cabecera" width="40" align="center">&nbsp;';
         htmlString += '<a href="javascript:inicializaLista('+ (paginaActual + 1) + ')" target="_self"><b><font color="white"> >> </a>&nbsp;&nbsp;';
	  } else htmlString += '</td><td width="40" align="center">&nbsp;';
      htmlString += '</td></tr></table>';
   }

   var registroInferior = ((paginaActual - 1) * lineasPagina) + 1;
   var registroSuperior = (paginaActual * lineasPagina);
   if (paginaActual == numeroPaginas)
      registroSuperior = listaSel.length;
   htmlString += '<font class="textoSuelto">Resultados&nbsp;' + registroInferior + '&nbsp;a&nbsp;' + registroSuperior + '&nbsp;de&nbsp;' + listaSel.length + '&nbsp;encontrados.</font>'
   
   return (htmlString);
}
	
function inicializaLista(numeroPagina){
   
   var listaP = new Array();	
   var j = 0;
   
   paginaActual = numeroPagina;
      
   inicio = (numeroPagina - 1) * lineasPagina;
   fin = Math.min(numeroPagina * lineasPagina,listaSel.length);
   
   for (var i=inicio;i<fin;i++)
   {
      listaP[j] = listaSel[i];
      j++;
   }
  

   tab.lineas=listaP;
   refresh();
   
   domlay('enlace',1,0,0,enlaces());
   
/*   
   else
   {
      var cabElt = getElt("cabecera");
      setEltVisibility (cabElt, "hidden");    
      if(document.all)
         document.all.tabla.innerHTML = '<br><br><br><center><h1>No existen Datos</h1></center>';
      else
         document.getElementById('tabla').innerHTML = '<br><br><br><center><h1>No existen Datos</h1></center>';
   }
*/   

}

function buscar()
{
   var j = 0;
   var auxCod = "";
   var auxDes = "";
   var len = document.forms[0].codigo.value.length;

   listaSel = new Array();

   for (var i=0; i<lista.length; i++)
   {
      auxCod = listaOriginal[i][0];
      auxDes = listaOriginal[i][1].toUpperCase();
      
      if ((auxCod.substring(0,len) == document.forms[0].codigo.value) && (auxDes.search(document.forms[0].descripcion.value.toUpperCase()) != -1) )
      {
         listaSel[j] = lista[i];
         j++;
      }
   }
   
   inicializaLista(1);

}

function limpiaDatos()
{

   document.forms[0].codigo.value = "";
   document.forms[0].descripcion.value = "";
   
   listaSel = lista;
   
   inicializaLista(1);
   
}

function enviaSeleccion(codigo){
  codSel = codigo;
  if (tab.selectedIndex >= 0) 
    codSel = tab.lineas[tab.selectedIndex][0];
  else
    codSel = '';
  for(var i=0; i<listaOriginal.length; i++){
    if(codSel == listaOriginal[i][0]){
      descSel = listaOriginal[i][1];
      codTViaSel = listaOriginal[i][2];
      descTViaSel = listaOriginal[i][3];
      break;
    }
  }
  self.close();			
}

function cancelaSeleccion(){
  respuesta = null;
  self.close();			
}


//Seleccion Multiple
var listaSeleccion  = new Array();
var arraySeleccion  = new Array();
var elemento = -1;
codSel = opener.arrayS;

function obtenerCheck(indice)
{
   var texto = '<input type="checkbox" class="check" name="seleccionado'+indice+'" value="si" disabled="true" ';
   for (var i=0; i<codSel.length; i++)
   {
      if (codigo[indice] == codSel[i])
      {
         texto = texto + ' checked ';
         listaSeleccion [indice] = "S";
         break;
      } 
   }
   texto = texto +'>';
   return (texto);
}

function getOrden(cod)
{
   for (var i=0; i<codigo.length; i++)
   {
      if (codigo[i] == cod)
         break;
   }
   return(i);
}

function pintaDatos(datos)
{
   var indice = 0;
   var lineas = tab.lineas;
   if (tab.selectedIndex != -1)
   {
      indice = getOrden(lineas[tab.selectedIndex][1]);

      // De Blanco a Amarillo
      if (elemento != indice)
      {
         if(eval("document.forms[0].seleccionado"+indice+".checked == true;"))
         {
            eval("document.forms[0].seleccionado"+indice+".checked = false;");
            listaSeleccion[indice] = "N";
         }
         else
         {
            eval("document.forms[0].seleccionado"+indice+".checked = true;");
            listaSeleccion[indice] = "S";
         }
      }
      else
      {
         if(eval("document.forms[0].seleccionado"+elemento+".checked == true;"))
         {
            eval("document.forms[0].seleccionado"+elemento+".checked = false;");
            listaSeleccion[elemento] = "S";
         }
         else
         {
            eval("document.forms[0].seleccionado"+elemento+".checked = true;");
            listaSeleccion[elemento] = "N";
         }
      }
      elemento = indice;

   }
   else
   {
      // De Amarillo a Blanco
      if(eval("document.forms[0].seleccionado"+elemento+".checked == true;"))
      {
         eval("document.forms[0].seleccionado"+elemento+".checked = false;");
         listaSeleccion[elemento] = "N";
      }
      else
      {
         eval("document.forms[0].seleccionado"+elemento+".checked = true;");
         listaSeleccion[elemento] = "S";
      }
   }
}

function crearVectorSeleccionados()
{
   var j = 0;
   for(var i=0; i<listaSeleccion.length; i++)
   {
      if (listaSeleccion[i] == 'S')
      {
         arraySeleccion[j]  = [listaOriginal[i][0],listaOriginal[i][1]];
         j++;
      }      
   }
}


//Seleccion Multiple Tipo
function limpiaCombo(indice)
{
   for( var i=1; i>=0; i--)
   {
      eval("document.forms[0].tipo" + indice + ".options[i] = null");
   }
   eval("document.forms[0].tipo"+indice+".disabled = true;");
   
   //Actualizamos el vector de seleccionados
   listaSeleccion[indice] = "N";
   listaSeleccionTipo[indice] = "";
}

function rellenaCombo(indice)
{
   var opcion   = document.createElement("OPTION");
   opcion.value = "O";
   opcion.text  = "Obligatorio";
   eval("document.forms[0].tipo"+indice+".options[document.forms[0].tipo" + indice + ".length]=opcion");   
   opcion       = document.createElement("OPTION");
   opcion.value = "D";
   opcion.text  = "Deseable";
   eval("document.forms[0].tipo"+indice+".options[document.forms[0].tipo" + indice + ".length]=opcion");         
   eval("document.forms[0].tipo"+indice+".disabled = false;");
   
   //Actualizamos el vector de seleccionados
   listaSeleccion[indice] = "S";
   listaSeleccionTipo[indice] = "O";
}

function habilitaCombo(indice)
{
   if (eval("document.forms[0].seleccionado"+indice+".checked == true"))
      rellenaCombo(indice);
   else
      limpiaCombo(indice);
      
}

function obtenerCheckTipo(indice)
{
   var texto = '<input type="checkbox" class="check" name="seleccionado'+indice+'" value="si" onClick="habilitaCombo('+indice+')" ';
   for (var i=0; i<codSel.length; i++)
   {
      if (codigo[indice] == codSel[i])
      {
         texto = texto + ' checked ';
         listaSeleccion [indice] = "S";
         break;
      } 
   }
   texto = texto +'>';
   return (texto);
   
}

function actualizaListaTipo(indice)
{
   listaSeleccionTipo[indice] = eval("document.forms[0].tipo"+indice+".value");
}

function obtenerTipo(indice)
{
   var encontrado = false;
   var texto = '<select name="tipo'+indice+'" size="1" style="width:90" onChange="actualizaListaTipo('+indice+')" ';
   
   for (var i=0; i<codSel.length; i++)
   {
      if (codigo[indice] == codSel[i])
      {
         texto = texto + '>';
         if (arraySelTipo[i] == "O")
         {
            texto = texto + '<option value="O">Obligatorio</option>' +
                            '<option value="D">Deseable</option>';
         }
         else if (arraySelTipo[i] == "D")
         {
            texto = texto + '<option value="D">Deseable</option>' +
                            '<option value="O">Obligatorio</option>';
         }
         listaSeleccionTipo[indice] = arraySelTipo[i];
 
         encontrado = true;
         break;
      }
   }
   
   if (!encontrado)
      texto = texto + ' disabled="true">';
   texto = texto + '</select>';
   
   return texto;
}
