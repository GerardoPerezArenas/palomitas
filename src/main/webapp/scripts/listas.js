
var ppcIE=((navigator.appName == "Microsoft Internet Explorer") || ((navigator.appName == "Netscape") && (parseInt(navigator.appVersion)==5)));
var ppcNN=((navigator.appName == "Netscape")&&(document.layers));

capaVisible = false;

var mensajeCodigoIncorrecto = 'Código incorrecto'; //Por defecto mensaje en castellano.
var frameOculto = 'oculto';

function esPar(numero)
{
   if (numero==(Math.ceil(numero/2))*2)
      return true;
   return false;
   
}

function getOffsetLeft1 (el) {
    var ol = el.offsetLeft;
    while ((el = el.offsetParent) != null)
        ol += el.offsetLeft;
    return ol;
}

function getOffsetTop1 (el) {
    var ot = el.offsetTop;
    while((el = el.offsetParent) != null)
        ot += el.offsetTop;
    return ot;
}

function posicionaCapa(ancho,alto)
{

   if ( ppcIE ) {
      ppcX = getOffsetLeft1(document.images[botonActual]);
      var anchura = 0;
      //if (ppcX + 180 > screen.width)
      //   anchura = ppcX + 180 - screen.width;      pantalla Completa
      if (ppcX + ancho > 800)
         anchura = ppcX + ancho + 16 - 800;
      ppcX = ppcX - anchura;
      ppcY = getOffsetTop1(document.images[botonActual]) + document.images[botonActual].height;
      var altura = 0;
      if (ppcY + alto > 475) {
      	altura = document.images[botonActual].height + alto;
      	ppcX = ppcX + 1;
      }
      ppcY = ppcY - altura; 
      ppcY = ppcY +1;

   }
   else if( ppcNN) {
      ppcX = document.images[botonActual].x;              
      anchura = 0;
      //if (ppcX + 180 > screen.width)
      //   anchura = ppcX + 180 - screen.width;      pantalla Completa
      if(ppcX + ancho > 800)
         anchura = ppcX + ancho + 16 - 800;
      ppcX = ppcX - anchura;
      ppcY = document.images[botonActual].y + document.images[botonActual].height;
      var altura = 0;
      if (ppcY + alto > 475) {
      	altura = document.images[botonActual].height + alto;
      	ppcX = ppcX + 1;
      }
      ppcY = ppcY - altura; 
      ppcY = ppcY +1;

   }

}

var ppcX = 0;
var ppcY = 0;

function cargarDesc(campoCod, campoDesc, arrayCod, arrayDesc) {

   var valor = "";
   i=0;
   eval("valor = document.forms[0]."+campoCod+".value");
   if (valor != "")
   {
       encontrado = 'false';
       while (encontrado == 'false' && i<arrayCod.length) {
    	  if (valor == arrayCod[i]) {
    	     encontrado = 'true';
    	     posicion = i;
    	  }
    	  else
    	     i++;
       }
       if (encontrado == 'false')
       {
          //alert("Código incorrecto.");
          jsp_alerta('A',mensajeCodigoIncorrecto);         
          eval("document.forms[0]."+campoCod+".value=''");
          eval("document.forms[0]."+campoDesc+".value=''");
          eval("document.forms[0]."+campoCod+".select()");
       }
       else  
    	  eval("document.forms[0]."+campoDesc+".value=\""+arrayDesc[i]+"\"");
   }
   else
      eval("document.forms[0]."+campoDesc+".value=''");
}

// Más de dos condiciones en el WHERE, se especifican en el array col_valor_Where a1,v1,a2,v2,...
// target1 
// target2

function muestraListaTabla(colCod, colDesc,nomTabla, arrayWhere, inputCod, inputDesc, boton, ancho, target1, target2, condicionCompleja) {
    if (capaVisible){
      domlay('desplegable',0,0,0,null);
      capaVisible = false;
    }else{
      botonActual=boton;
      anchura=ancho;
      if (target2 != null){ 
        document.forms[0].target= target2;
        frameOculto = target2;
      }else{ 
        document.forms[0].target = 'oculto';
        frameOculto = 'oculto';
      }
      document.forms[0].tipo_select.value ='lista';
      document.forms[0].col_cod.value     = colCod;
      document.forms[0].col_desc.value    = colDesc;
      document.forms[0].nom_tabla.value   = nomTabla;
      document.forms[0].column_valor_where.value   = arrayWhere;
	  if (condicionCompleja != null)
		document.forms[0].whereComplejo.value = condicionCompleja;
	  else 
		  if (document.forms[0].whereComplejo)
			document.forms[0].whereComplejo.value = '';
	      // else: Va un nulo.
      document.forms[0].input_cod.value   = inputCod;
      document.forms[0].input_desc.value  = inputDesc;
      if (target1 != null) 
        document.forms[0].target1.value = target1;
      document.forms[0].action=APP_CONTEXT_PATH + "/CargarSelect.do";
      document.forms[0].submit();
    }
}

function updateDescripcion(descripcion, inputDesc, inputCod)
{
    var desc = descripcion;
    if ((desc == '---') && (inputCod != 'undefined'))
    {       
       jsp_alerta('A',mensajeCodigoIncorrecto);         
       eval("document.forms[0]."+inputCod+".value=''");
       eval("document.forms[0]."+inputDesc+".value=''");
       eval("document.forms[0]."+inputCod+".select()");
    }
    else
       eval("document.forms[0]." + inputDesc + ".value = desc;");
    //document.forms[0].target='mainFrame';
}

function ocultarLista() {

      if (capaVisible)
      {
         domlay('desplegable',0,0,0,null);
         capaVisible = false;
      }
}