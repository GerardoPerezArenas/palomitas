var ppcIE=((navigator.appName == "Microsoft Internet Explorer"));
var ppcNN=((navigator.appName == "Netscape")&&(document.layers));

function completarConCeros(codTotal,codObtenido){
   var ceros='';
  
   if((codTotal!='')&& (codObtenido!='')){
	for(i=codObtenido.length;i < codTotal.length;i++) ceros+='0';
   }
  
	
   return ceros+codObtenido;

}



function obtenerFecha(fecha){
 
        if(fecha!=''){	        	        
	        var barra1=fecha.indexOf("-");	        
	        var barra2=barra1+(fecha.substring(barra1+1).indexOf("-")+1);	        
	        var dia=fecha.substring(barra2+1,11);
	        var mes=fecha.substring(barra1+1,7);
	        var ano=fecha.substring(0,4);	        
	        fecha=dia+'/'+mes+'/'+ano;
	        
        }
  return fecha;
}

function capitalize(words) {
var tmpStr, tmpChar, preString, postString, strlen;
tmpStr = words.toLowerCase();
stringLen = tmpStr.length;
if (stringLen > 0)
{
  for (i = 0; i < stringLen; i++)
  {
    if (i == 0)
	{
      tmpChar = tmpStr.substring(0,1).toUpperCase();
      postString = tmpStr.substring(1,stringLen);
      tmpStr = tmpChar + postString;
    }
    else
	{
      tmpChar = tmpStr.substring(i,i+1);
      if (tmpChar == " " && i < (stringLen-1))
	  {
      tmpChar = tmpStr.substring(i+1,i+2).toUpperCase();
      preString = tmpStr.substring(0,i+1);
      postString = tmpStr.substring(i+2,stringLen);
      tmpStr = preString + tmpChar + postString;
      }
    }
  }
}
return tmpStr;
}

function trim(text){
      var mytext=new String(text);
      while (mytext.charAt(0) == ' ' || mytext.charAt(0) == '-' || mytext.charCodeAt(0) == 160) {
        mytext = mytext.substring(1,mytext.length);
      };
      while (mytext.charAt(mytext.length - 1) == ' ' || mytext.charAt(mytext.length - 1) == '-' || mytext.charCodeAt(mytext.length - 1) == 160) {
        mytext = mytext.substring(0, (mytext.length - 1));
      };
      return mytext;
}


function desactivacionElements( desactivar ){
   var i=0;
   var max=document.forms[0].elements.length;
   
   for(i=0;i<max;i++){
      document.forms[0].elements.item(i).disabled=desactivar;
      
      if(desactivar && document.forms[0].elements[i].type != 'button' && document.forms[0].elements[i].type != 'submit') 
         document.forms[0].elements[i].style.backgroundColor= "#FFFFFF";
   } 
}

function desactivacionAnchors( activacion, id )
{

   if (document.anchors.item(id))
   {
      var max=document.anchors.item(id).length;
      if(max>1)
      {
         for(var i=0; i<max; i++)
         {
            document.anchors.item(id, i).href='#';
         }
      }
      else
      {
         if (ppcIE)
            document.anchors.item(id).href='#';
      }
   }
}    
  
function fechaHoy()
   {
      // Devuelve la fecha actual en formato dd/mm/yyyy

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

   function fechaHoraHoy()
   {
      // Devuelve la fecha y hora actual en formato "dd/mm/yyyy hh:mi"

      var hoy = new Date();
      var hora = (hoy.getHours() < 10)? '0' + hoy.getHours():hoy.getHours();
      var min = (hoy.getMinutes() < 10)? '0' + hoy.getMinutes():hoy.getMinutes();


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

      
      return( dia + '/' + mes + '/' + hoy.getFullYear() + ' ' + hora + ':' + min);

   }

   function horaHoy()
   {
      // Devuelve la fecha y hora actual en formato "dd/mm/yyyy hh:mi"

      var hoy = new Date();
      var hora = (hoy.getHours() < 10)? '0' + hoy.getHours():hoy.getHours();
      var min = (hoy.getMinutes() < 10)? '0' + hoy.getMinutes():hoy.getMinutes();
    
     
      return( hora + ':' + min);

    }
    
    //Inic Raquel
    //Compara si hora2 > hora1
    //Si hora2 >= hora1 -> return true
    //Si hora2 < hora1 -> return false
    //No compara los minutos, solo las horas
    function comparaHoras(hora1, hora2)
    {
    	var pos1 = hora1.toString().indexOf(':');
    	var horaAux1 = parseInt((hora1.toString().substring(0, pos1)), 10);
    	var pos2 = hora2.toString().indexOf(':');
    	var horaAux2 = parseInt((hora2.toString().substring(0, pos2)), 10);
    	
    	if (horaAux2 > horaAux1)
    		return true;
	else if (horaAux2 == horaAux1)
		return true;
	else
		return false;
    }
    //Compara si hora2 es mayor q hora1, atendiendo tambien a los minutos
    function comparaHorasMinutos(hora1, hora2)
    {
    	var pos1 = hora1.toString().indexOf(':');
    	var horaAux1 = parseInt((hora1.toString().substring(0, pos1)), 10);
    	var minAux1 = parseInt((hora1.toString().substring(pos1+1)), 10);
    	var pos2 = hora2.toString().indexOf(':');
    	var horaAux2 = parseInt((hora2.toString().substring(0, pos2)), 10);
    	var minAux2 = parseInt((hora2.toString().substring(pos2+1)), 10);
    	
    	if (horaAux2 > horaAux1)
    	{
    		return true;
    	}
	else if (horaAux2 == horaAux1)
	{
		if (minAux2 > minAux1)
			return true;
		else
			return false;
	}
	else
	{
		return false;
	}
    }
    //Fin Raquel

var capa         = '<div id="capaDescripcion" style="position:absolute;width:180px;height:100px;overflow:auto;">&nbsp;</div>';
var declararCapa = true;

function formatoDescripcion(texto)
{
   //*** Funcion utilizada en "muestraDescripcion()" para dar el formato de la capa  ***//  
   return ('<table cellspacing="0" cellpadding="2" border="0"><tr><td class="descripcion">'+texto+'</style></td></tr></table>');
}



function obtenerDiscapacidadesValidas(ArrayCodigos,ArrayDescripciones){
	var longitud=eval(ArrayCodigos+'.length');
	//alert("ArrayCodigos"+longitud);
	for(var k=0;k<longitud;k++){
		var aux=eval(ArrayCodigos+'['+k+'].substring(0,2)');
		if(parseInt(aux)==0){
			//alert(ArrayCodigos[k]);
			eliminarElementoArrayS(ArrayCodigos,k);
			eliminarElementoArrayS(ArrayDescripciones,k);
			longitud=eval(ArrayCodigos+'.length');
			k=k-1;
			//alert(eval(ArrayCodigos+'['+(k+1)+']'));
		}
		//alert(ArrayCodigos[i].substring(0,1));
	}
	
}

function eliminarElementoArrayS(ArrayElementos,posicionElemento){
	var ArrayAux=new Array();
	var j=0;
	var longitudArray=eval(ArrayElementos+'.length');
	//alert("ArrayElementos"+longitudArray);
	if(posicionElemento >= longitudArray)return;
	for(var i=0;i < longitudArray;i++){		
		if(i!=posicionElemento){
			ArrayAux[j]=eval(ArrayElementos+'['+i+'];');
			j++;
		}
	}
	//alert("ArrayAux"+ArrayAux.length);
	//alert("ArrayAux"+ArrayAux.length);
	eval(ArrayElementos+'=ArrayAux;');
}



var controlVentanaConfirmacion='cerrada';
var ventanaConfirmacion='';

function confirmacion(literal1, literal2){
   var obj1 = document.createElement("input");  obj1.type = "hidden";  obj1.name = "texto1";  obj1.value = literal1;  document.forms[0].appendChild(obj1);
   var obj2 = document.createElement("input");  obj2.type = "hidden";  obj2.name = "texto2";  obj2.value = literal2;  document.forms[0].appendChild(obj2);
         
   document.forms[0].target='oculto';	
   document.forms[0].action=APP_CONTEXT_PATH + "/Confirmacion.do";
   document.forms[0].submit();	
}

function cerrarVentanaConfirm(){
   if(controlVentanaConfirmacion == 'abierta' && !ventanaConfirmacion.closed){
       controlVentanaConfirmacion = 'cerrada';
       ventanaConfirmacion.window.close();
    }	
}


function obtenerHuellaPDF(tipo_doc, num_doc, accion){
   var huella    = '';
   var sumaDNI   = 0;
   var letrasDNI = '';
   var fecha     = fechaHoy();
   var i;
   
   for(i=0;i<num_doc.length;i++){
      if(! isNaN(num_doc.charAt(i))) sumaDNI   = sumaDNI   +  parseInt(num_doc.charAt(i), 10);
      else                           letrasDNI = letrasDNI +  num_doc.charAt(i);
   }    
   
   huella = tipo_doc + sumaDNI.toString() + letrasDNI + fecha.substring(0,2) + fecha.substring(3,5) + fecha.substring(8) + accion;
   
   while(huella.length!=16)
      huella = numeroAleatorio() + huella; 
   
   return huella;
}

function numeroAleatorio(){
   var num = (Math.random()*10).toString();
   if(num.indexOf('.')==1) num = num.substring(0,1);
   else                    num = num.substring(0,2);  
   return num;
}