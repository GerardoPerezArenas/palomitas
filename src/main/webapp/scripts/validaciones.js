	function calcularletra (dni,campo){
    var cadena = 'TRWAGMYFPDXBNJZSQVHLCKET';
    var letra;
    var aux;
    var posicion;
    var longitud;
    var correcto=true;
    longitud=dni.length;
    aux=dni.substring(longitud-1).toUpperCase();
    if ((longitud>=8)&&(longitud<10)){
    	if (isNaN(aux)){	
				posicion = dni.substring(0,longitud-1) % 23;
				letra=cadena.charAt(posicion);
				if (isNaN(dni.substring(0,longitud-1))) {
					jsp_alerta("A","El DNI debe estar formado exclusivamente por digitos");
					return false;
				}
				if(aux!=letra) {
					//jsp_alerta("A","aux= "+aux+" letra= "+letra);
					return false;
				}
			}else{
				posicion = dni.substring(0,longitud) % 23;
				letra=cadena.charAt(posicion);
				if (isNaN(dni.substring(0,longitud))) {
					jsp_alerta("A","El DNI debe estar formado exclusivamente por digitos");
					return false;
				}
				campo.value =dni+letra;
				return true;
			}
    }else {
    	jsp_alerta("A","Debe introducir un DNI válido (8 digitos)");
      return false;
    }
		campo.value=campo.value.toUpperCase();
    return correcto;
	}

function validarDNI (dni) {
    if (document.forms[0].tipo_doc.value=='D')
        if (!calcularletra(dni)) {
            jsp_alerta("A","DNI Incorrecto");
            document.forms[0].num_doc.select();
            return false;
        }
    return true;
    
}

function calcularletra2 (dni)
{
    var cadena = 'TRWAGMYFPDXBNJZSQVHLCKET';
    var letra;
    var aux;
    var posicion;
    var longitud;
    var correcto=true;
    longitud=dni.length;
    aux=dni.substring(longitud-1).toUpperCase();

    if (dni!=""){
        posicion = dni.substring(0,longitud-1) % 23;
        letra=cadena.charAt(posicion);
        if (isNaN(dni.substring(0,longitud-1))) {
            //jsp_alerta("A","El DNI debe estar formado exclusivamente por digitos");
            document.forms[0].num_doc.value="";
            //document.forms[0].LETRA_NIF.value="";
            return false;
        }
        if(aux!=letra) {
                
                //jsp_alerta("A","aux= "+aux+" letra= "+letra);
                return false;
        }
         
    }else {
        //jsp_alerta("A","Debe introducir el DNI");
        return false;
    }
    document.forms[0].num_doc.value=document.forms[0].num_doc.value.toUpperCase();
    return correcto;
}



function validarDNI2(dni) {
    
        if (!calcularletra2(dni)) {
            //jsp_alerta("A","DNI Incorrecto");
            //document.forms[0].num_doc.select();
            return false;
        }
    return true;
    
}


function Trim( str ) {
        var resultStr = "";
        
        resultStr = TrimLeft(str);
        resultStr = TrimRight(resultStr);
        
        return resultStr;
} 

function TrimLeft( str ) {
        var resultStr = "";
        var i = len = 0;
        
        if (str+"" == "undefined" || str == null)       
                return null;

        str += "";

        if (str.length == 0) 
                resultStr = "";
        else {  
                // Loop through string starting at the beginning as long as there
                // are spaces.
                //              len = str.length - 1;
                len = str.length;
                                        
                while ((i <= len) && (str.charAt(i) == " "))
                        i++;
        
                resultStr = str.substring(i, len);
        }
                        
        return resultStr;
} 
                        

function TrimRight( str ) {
        var resultStr = "";
        var i = 0;
        
        if (str+"" == "undefined" || str == null)       
                return null;

        str += "";
                
        if (str.length == 0) 
                resultStr = "";
        else {
                // Loop through string starting at the end as long as there
                // are spaces.
                i = str.length - 1;
                while ((i >= 0) && (str.charAt(i) == " "))
                        i--;
                                                
                        resultStr = str.substring(0, i + 1);
                }
                
                return resultStr;       
} 


function validarFecha(elemento,num) {
//Compruebo que el formato es valido
        var fechaHoy=new Date();
        var mensaje="Fecha no válida.";
        var barra1=elemento.value.indexOf("/");
        var barra2=elemento.value.substring(barra1+1).indexOf("/")+3;
        var dia=elemento.value.substring(0,2);
        var mes=elemento.value.substring(3,5);
        var anho=elemento.value.substring(6);
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
        if (elemento.value.length<10 || (barra1!=2) || (barra2!=5)) {
                if (num==0)
                        jsp_alerta("A",mensaje);
                elemento.select();
                return false;
        }
        if (mes<1 || mes>12) {
                if (num==0)
                        jsp_alerta("A","Mes no válido.");
                elemento.select();                       
                return false;
        }
        if ((dia<1) || (dia>mes_dias[parseInt(mes)]) || (dia==29 && parseInt(mes)==2 && Math.round(anho-(Math.floor(anho/4)*4))!=0)) {
                if (num==0)
                        jsp_alerta("A","Día no válido.");
                elemento.select();                       
                return false;
        }
        var numeros="0123456789";
        for (i=0;i<4;i++)
                if (numeros.indexOf(anho.substring(i,i+1))==-1) {
                        if (num==0)
                                jsp_alerta("A","Caracteres erróneos en el año de la fecha.");
                        elemento.select();                       
                        return false;
                }
        return true;    
}

function validarFechaAnteriorHoy(elemento,num) {
//Compruebo que el formato es valido
        var fechaHoy=new Date();
        var mensaje="Fecha no válida.";
        var barra1=elemento.value.indexOf("/");
        var barra2=elemento.value.substring(barra1+1).indexOf("/")+3;
        var dia=elemento.value.substring(0,2);
        var mes=elemento.value.substring(3,5);
        var anho=elemento.value.substring(6);
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
        if (elemento.value.length<10 || (barra1!=2) || (barra2!=5)) {
                if (num==0)
                        jsp_alerta("A",mensaje);
                elemento.select();
                return false;
        }
        if (mes<1 || mes>12) {
                if (num==0)
                        jsp_alerta("A","Mes no válido.");
                elemento.select();                       
                return false;
        }
        if ((dia<1) || (dia>mes_dias[parseInt(mes)]) || (dia==29 && parseInt(mes)==2 && Math.round(anho-(Math.floor(anho/4)*4))!=0)) {
                if (num==0)
                        jsp_alerta("A","Día no válido.");
                elemento.select();                       
                return false;
        }
        var numeros="0123456789";
        for (i=0;i<4;i++)
                if (numeros.indexOf(anho.substring(i,i+1))==-1) {
                        if (num==0)
                                jsp_alerta("A","Caracteres erróneos en el año de la fecha.");
                        elemento.select();                       
                        return false;
                }
        var miFecha=new Date(anho,(mes-1),dia);
        if (miFecha>fechaHoy) {
                if (num==0)
                        jsp_alerta("A","La fecha no puede ser superior a la de hoy.");
                elemento.select();                       
                return false;
        }
        return true;    
}

function validarFechaNacimiento(elemento,num) {
//Compruebo que el formato es valido
        var fechaHoy=new Date();
        var mensaje="Fecha no válida.";
        var barra1=elemento.value.indexOf("/");
        var barra2=elemento.value.substring(barra1+1).indexOf("/")+3;
        var dia=elemento.value.substring(0,2);
        var mes=elemento.value.substring(3,5);
        var anho=(parseInt(elemento.value.substring(6), '10') + 16).toString();
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
        if (elemento.value.length<10 || (barra1!=2) || (barra2!=5)) {
                if (num==0)
                        jsp_alerta("A",mensaje);
                elemento.select();
                return false;
        }
        if (mes<1 || mes>12) {
                if (num==0)
                        jsp_alerta("A","Mes no válido.");
                elemento.select();                       
                return false;
        }
        if ((dia<1) || (dia>mes_dias[parseInt(mes)]) || (dia==29 && parseInt(mes)==2 && Math.round(anho-(Math.floor(anho/4)*4))!=0)) {
                if (num==0)
                        jsp_alerta("A","Día no válido.");
                elemento.select();                       
                return false;
        }
        var numeros="0123456789";
        for (i=0;i<4;i++)
                if (numeros.indexOf(anho.substring(i,i+1))==-1) {
                        if (num==0)
                                jsp_alerta("A","Caracteres erróneos en el año de la fecha.");
                        elemento.select();                       
                        return false;
                }
        var miFecha=new Date(anho,(mes-1),dia);
        if (miFecha>fechaHoy) {
                if (num==0)
                        jsp_alerta("A","El demandante de empleo debe tener dieciseis años cumplidos.");
                elemento.select();                       
                return false;
        }
        return true;    
}

function validarHora(elemento) {
        
        var mensaje="Hora no válida. Formato HH:MM";
        var dosPuntos=elemento.value.indexOf(":");
        var hora=elemento.value.substring(0,2);
        var minutos=elemento.value.substring(3);

        if ((elemento.value.length !=5) || (dosPuntos!=2)) {
                jsp_alerta("A",mensaje);
                elemento.select();
                return false;
        }
        if(isNaN(hora)){
           jsp_alerta("A","La hora debe ser numérica.");
           elemento.select();
           return false;	
        }
        else{
           if(isNaN(minutos)){
              jsp_alerta("A","Los minutos deben ser numéricos.");
              elemento.select();
              return false;	
           }
           else{
              if (hora<0 || hora>23) {
                 jsp_alerta("A","La hora debe encontrarse entre 0 y 23.");
                 elemento.select();                       
                 return false;
              }
              if (minutos<0 || minutos>59) {
                 jsp_alerta("A","Los minutos debe encontrarse entre 0 y 59.");
                 elemento.select();                       
                 return false;
              }
              return true;
           }
        }    
}


function validarObligatorios(mnsjErrorTodos, mnsjErrorUno)
{
	var valor = true;
	var i = 0;
	var max = 0;
	var mnsjT;
	var mnsj1;
	if (mnsjErrorTodos!=null) 
   		mnsjT = mnsjErrorTodos;
	else mnsjT = 'Debe rellenar todos los campos obligatorios';
	if (mnsjErrorUno!=null) 
		mnsj1 = mnsjErrorUno;
	else mnsj1 = 'Debe rellenar el campo obligatorio';

	var ele = document.forms[0].elements;
	var obligatorios = new Array();
	var index = 0;
	for(j=0;j<ele.length;j++){
		if(ele[j].className.toLowerCase().indexOf("obligatorio")!=-1) {     
            obligatorios[index++] = ele[j];
	}
	}
    
    var entrar = false;
    if(document.forms[0].elements['obligatorio']!=null)
        entrar = true;

    // Campos obligatorios
    if(entrar){
        
        var elementos = new Array();
        elementos = document.forms[0].elements;
        if(elementos!=null){
            max = elementos['obligatorio'].length;        
        }
        
        var elementosOblig = new Array();
        elementosOblig = elementos['obligatorio'];

		if (max > 1){   
			for(var i=0; i < max; i++){
                if(elementosOblig[i].value.length==0){                    
					jsp_alerta("A", mnsjT);
					document.forms[0].elements['obligatorio'][i].focus();
					valor = false;
					break;
				}
			}
		}else{
            
             if(elementosOblig[0]!=null && elementosOblig[0].value.length==0){
				jsp_alerta("A", mnsj1);
				document.forms[0].elements['obligatorio'].focus();
				valor = false;
			}
		}
	}else{

      if(obligatorios.length!=0){        
		max = obligatorios.length;
		if (max > 1){   
			for(var i=0; i < max; i++){
				if (obligatorios[i].value.length == 0){
					jsp_alerta("A", mnsjT);
					obligatorios[i].select();
					valor = false;
					break;
				}
			}
		}else{
			if (obligatorios[0].value.length == 0){
				jsp_alerta("A", mnsj1);
				obligatorios[i].select();
				valor = false;
			}
		}
	}

	} 

	return (valor);
}


function validarFechasObligatorias() {
   var valor = true;
   var i = 0;
   var max = 0;

   // validamos todas las fechas obligatorias
   var auxiliar = document.forms[0].elements['obligatorioFecha'];
   
   //if (document.forms[0].elements['obligatorioFecha'])
   if(auxiliar!=null && auxiliar!=undefined)
   {
      max = document.forms[0].elements['obligatorioFecha'].length;
      if (max > 1)
      {   
         for( i=0; i < max; i++)
         {
            if (document.forms[0].elements['obligatorioFecha'][i].value.length == 0)
            {
               jsp_alerta("A",'Debe rellenar todos los campos obligatorios');
               document.forms[0].elements['obligatorioFecha'][i].select();
               valor = false;
               break;
            }
            else if (validarFecha(document.forms[0].elements['obligatorioFecha'][i],'0') == false)
            {
               document.forms[0].elements['obligatorioFecha'][i].select();
               valor = false;
               break;                  
            }
         }
      }
      else {
         if (document.forms[0].elements['obligatorioFecha']!=null && 
                 document.forms[0].elements['obligatorioFecha'].value.length == 0) {
            jsp_alerta("A",'Debe rellenar el campo obligatorio.');
            document.forms[0].elements['obligatorioFecha'].select();
            valor = false;
         } else if (validarFecha(document.forms[0].elements['obligatorioFecha'],'0') == false){
            document.forms[0].elements['obligatorioFecha'].select();
            valor = false;
         }
      }
   }

   return (valor);

}

function validarHorasObligatorias()
{

   var valor = true;
   var i = 0;
   var max = 0;
   var auxiliar = document.forms[0].elements['obligatorioHora'];
   
   //if (document.forms[0].elements['obligatorioHora'])
   if(auxiliar!=null && auxiliar!=undefined)
   {
      max = document.forms[0].elements['obligatorioHora'].length;
      if (max > 1)
      {   
         for( i=0; i < max; i++)
         {
            if (document.forms[0].elements['obligatorioHora'][i].value.length == 0)
            {
               jsp_alerta("A",'Debe rellenar todos los campos obligatorios');
               document.forms[0].elements['obligatorioHora'][i].select();
               valor = false;
               break;
            }
            else if (validarHora(document.forms[0].elements['obligatorioHora'][i],'0') == false)
            {
               document.forms[0].elements['obligatorioHora'][i].select();
               valor = false;
               break;                  
            }
         }
      }
      else
      {
         if (document.forms[0].elements['obligatorioHora'].value.length == 0)
         {
            jsp_alerta("A",'Debe rellenar el campo obligatorio.');
            document.forms[0].elements['obligatorioHora'].select();
            valor = false;
         }
         else if (validarHora(document.forms[0].elements['obligatorioHora'],'0') == false)
         {
            document.forms[0].elements['obligatorioHora'].select();
            valor = false;
         }
      }
   }

   return (valor);

}

function validarNumerosObligatorios()
{

   var valor = true;
   var i = 0;
   var max = 0;

   // validamos todos los numeros obligatorios
   //if (document.forms[0].elements['obligatorioNumero'])
   var auxiliar = document.forms[0].elements['obligatorioNumero'];

   if (auxiliar!=null && auxiliar!=undefined)
   {
      max = document.forms[0].elements['obligatorioNumero'];
      if (max > 1)
      {   
         for( i=0; i < max; i++)
         {
            if (document.forms[0].elements['obligatorioNumero'][i].value.length == 0)
            {
               jsp_alerta("A",'Debe rellenar todos los campos obligatorios');
               document.forms[0].elements['obligatorioNumero'][i].select();
               valor = false;
               break;
            }
            else if (isNaN(document.forms[0].elements['obligatorioNumero'][i].value))
            {
               jsp_alerta("A",'El campo sólo admite valores numéricos');
               document.forms[0].elements['obligatorioNumero'][i].select();
               valor = false;
               break;                  
            }
         }
      }
      else
      {
         //if (document.forms[0].elements['obligatorioNumero'].value.length == 0)
         if (document.forms[0].elements['obligatorioNumero']!=null && document.forms[0].elements['obligatorioNumero'].value.length == 0)
         {
            jsp_alerta("A",'Debe rellenar el campo obligatorio.');
            document.forms[0].elements['obligatorioNumero'].select();
            valor = false;
         }
         else if (isNaN(document.forms[0].elements['obligatorioNumero'].value))
         {
            jsp_alerta("A",'El campo sólo admite valores numéricos');
            document.forms[0].elements['obligatorioNumero'].select();
            valor = false;
         }
      }
   }

   return (valor);

}

function validarDecimalesObligatorios()
{

   var valor = true;
   var i = 0;
   var max = 0;

   // validamos todos los decimales obligatorios
   if (document.forms[0].elements['obligatorioDecimal'])
   {
      max = document.forms[0].elements['obligatorioDecimal'].length;
      if (max > 1)
      {   
         for( i=0; i < max; i++)
         {
            if (document.forms[0].elements['obligatorioDecimal'][i].value.length == 0)
            {
               jsp_alerta("A",'Debe rellenar todos los campos obligatorios');
               document.forms[0].elements['obligatorioDecimal'][i].select();
               valor = false;
               break;
            }
            else if ((isNaN(document.forms[0].elements['obligatorioDecimal'][i].value)) &&
                     (isNaN(document.forms[0].elements['obligatorioDecimal'][i].value.replace(",","")))
                    )
            {
               jsp_alerta("A",'El campo sólo admite valores numéricos');
               document.forms[0].elements['obligatorioDecimal'][i].select();
               valor = false;
               break;                  
            }
         }
      }
      else
      {
         if (document.forms[0].elements['obligatorioDecimal'].value.length == 0)
         {
            jsp_alerta("A",'Debe rellenar el campo obligatorio.');
            document.forms[0].elements['obligatorioDecimal'].select();
            valor = false;
         }
         else if (isNaN(document.forms[0].elements['obligatorioDecimal'].value.replace(",","")))
         {
            jsp_alerta("A",'El campo sólo admite valores numéricos.');
            document.forms[0].elements['obligatorioDecimal'].select();
            valor = false;
         }
      }
   }

   return (valor);

}

function validarFechas()
{

   var valor = true;
   var i = 0;
   var max = 0;

   //if (document.forms[0].elements['fecha'])
   var auxiliar = document.forms[0].elements['fecha'];
   
   // validamos todas las fechas
   if(auxiliar!=null && auxiliar!=undefined)
   {
      max = document.forms[0].elements['fecha'].length;
      if (max > 1)
      {
         for( i=0; i < max; i++)
         {
            if (document.forms[0].elements['fecha'][i].value.length > 0)
            {
               if (validarFecha(document.forms[0].elements['fecha'][i],'0') == false)
               {
                  document.forms[0].elements['fecha'][i].select();
                  valor = false;
                  break;
               }
            }
         }
      }
      else
      {
         //if ((document.forms[0].elements['fecha'].value.length > 0) && (validarFecha(document.forms[0].elements['fecha'],'0') == false))
         if (document.forms[0].elements['fecha']!=null && (document.forms[0].elements['fecha'].value.length > 0) && (validarFecha(document.forms[0].elements['fecha'],'0') == false))
         {
            document.forms[0].elements['fecha'].select();
            valor = false;
         }
      }
   }

   return (valor);

}

function validarHoras()
{

   var valor = true;
   var i = 0;
   var max = 0;

 var auxiliar = document.forms[0].elements['hora'];

   // validamos todas las horas
   i = 0;
   //if (document.forms[0].elements['hora'])
   if(auxiliar!=null && auxiliar!=undefined)
   {
      max = document.forms[0].elements['hora'].length;
      if (max > 1)
      {
         for( i=0; i < max; i++)
         {
            if ((document.forms[0].elements['hora'][i].value.length > 0) && (validarHora(document.forms[0].elements['hora'][i]) == false))
            {
               document.forms[0].elements['hora'][i].select();
               valor = false;
               break;
            }
         }
      }
      else
      {
         //if ((document.forms[0].elements['hora'].value.length > 0) && (validarHora(document.forms[0].elements['hora']) == false))
         if (document.forms[0].elements['hora']!=null && (document.forms[0].elements['hora'].value.length > 0) && (validarHora(document.forms[0].elements['hora']) == false))
         {
            document.forms[0].elements['hora'].select();
            valor = false;
         }
      }
   }

   return (valor);

}

function validarNumeros()
{

   var valor = true;
   var i = 0;
   var max = 0;

    //if (document.forms[0].elements['numero'])
   var auxiliar = document.forms[0].elements['numero'];
   
   // validamos todas los numeros
   if(auxiliar!=null && auxiliar!=undefined)
   {
      max = document.forms[0].elements['numero'].length;
      if (max > 1)
      {
         for( i=0; i < max; i++)
         {
            if (isNaN(document.forms[0].elements['numero'][i].value))
            {
               jsp_alerta("A",'El campo sólo admite valores numéricos');
               document.forms[0].elements['numero'][i].select();
               valor = false;
               break;
            }
         }
      }
      else
      {
         if (isNaN(document.forms[0].elements['numero'].value))
         {
            jsp_alerta("A",'El campo sólo admite valores numéricos');
            document.forms[0].elements['numero'].select();
            valor = false;
         }
      }
   }

   return (valor);

}

function validarDecimales()
{

   var valor = true;
   var i = 0;
   var max = 0;

   // validamos todos los numeros decimales
   if (document.forms[0].elements['decimal'])
   {
      max = document.forms[0].elements['decimal'].length;
      if (max > 1)
      {
         for( i=0; i < max; i++)
         {
            if ((isNaN(document.forms[0].elements['decimal'][i].value)) &&
                (isNaN(document.forms[0].elements['decimal'][i].value.replace(",","")))
               )
            {
               jsp_alerta("A",'El campo sólo admite valores numéricos.');
               document.forms[0].elements['decimal'][i].select();
               valor = false;
               break;
            }
         }
      }
      else
      {
         if (isNaN(document.forms[0].elements['decimal'].value.replace(",","")))
         {
            jsp_alerta("A",'El campo sólo admite valores numéricos');
            document.forms[0].elements['decimal'].select();
            valor = false;
         }
      }
   }

   return (valor);

}

function validarEmailsObligatorios() { 
   var valor = true;
   var i = 0;
   var max = 0;

   // validamos todos los emails obligatorios
   //if (document.forms[0].elements['obligatorioEmail'])
   var auxiliar = document.forms[0].elements['obligatorioEmail'];
   
   if (auxiliar!=null && auxiliar!=undefined)
   {
      max = document.forms[0].elements['obligatorioEmail'].length;
      if (max > 1)
      {   
         for( i=0; i < max; i++)
         {
            if (document.forms[0].elements['obligatorioEmail'][i].value.length == 0)
            {
               jsp_alerta("A",'Debe rellenar todos los campos obligatorios');
               document.forms[0].elements['obligatorioEmail'][i].select();
               valor = false;
               break;
            }
            else if (noesemail(document.forms[0].elements['obligatorioEmail'][i].value))
            {
               jsp_alerta("A","El email no es válido");
               document.forms[0].elements['obligatorioEmail'][i].select();
               valor = false;
               break;                  
            }
         }
      }
      else
      {
         //if (document.forms[0].elements['obligatorioEmail'].value.length == 0)
         if (document.forms[0].elements['obligatorioEmail']!=null && document.forms[0].elements['obligatorioEmail'].value.length == 0)
         {
            jsp_alerta("A",'Debe rellenar el campo obligatorio.');
            document.forms[0].elements['obligatorioEmail'].select();
            valor = false;
         }
         else if (noesemail(document.forms[0].elements['obligatorioEmail'].value))
         {
            jsp_alerta("A",'El email no es válido');
            document.forms[0].elements['obligatorioEmail'].select();
            valor = false;
         }
      }
   }

   return (valor); 
   
}

function validarEmails(){
    var valor = true;
    var i = 0;
    var max = 0;
    // validamos todas los numeros
    //if (document.forms[0].elements['email']){
    var auxiliar = document.forms[0].elements['email'];
        
   if(auxiliar!=null && auxiliar!=undefined){        
        max = document.forms[0].elements['email'].length;
        if (max > 1){
            for( i=0; i < max; i++){
                if (document.forms[0].elements['email'].value = '') continue;            
                if (noesemail(document.forms[0].elements['email'][i].value)){
                    jsp_alerta("A",'El email no es válido');
                    document.forms[0].elements['email'][i].select();
                    valor = false;
                    break;
                }
            }
        }else{
            if (document.forms[0].elements['email'].value != '') {
                if (noesemail(document.forms[0].elements['email'].value)){
                    jsp_alerta("A",'El email no es válido');
                    document.forms[0].elements['email'].select();
                    valor = false;
                }
            }
        }
    }
    return (valor);
}

function noesemail(texto) { 
    var tiene = 0;
    
    if(texto == '')
       return true;
    
    for(var i = 0;i < texto.length;i++){ // recorro letra por letra
        var onechar = texto.charAt(i); 
        if (onechar == '@'){ // busco una arroba en cada letra
            tiene = 1; 
        } 
    } 
    if (tiene == 1) // controlo si existe o no una arroba
        return false; 
    else  
        return true; 
}


function validarTelefonosObligatorios()
{
   var valor = true;
   var i = 0;
   var max = 0;

   // validamos todos los numeros obligatorios
   //if (document.forms[0].elements['obligatorioTelefono'])
   var auxiliar = document.forms[0].elements['obligatorioTelefono'];
   
   if (auxiliar!=null && auxiliar!=undefined)
   {
      max = document.forms[0].elements['obligatorioTelefono'].length;
      if (max > 1)
      {   
         for( i=0; i < max; i++)
         {
            if (document.forms[0].elements['obligatorioTelefono'][i].value.length == 0)
            {
               jsp_alerta("A",'Debe rellenar todos los campos obligatorios');
               document.forms[0].elements['obligatorioTelefono'][i].select();
               valor = false;
               break;
            }
            else if (noEsTelefono(document.forms[0].elements['obligatorioTelefono'][i].value))
            {
               jsp_alerta("A",'El número de teléfono no es válido');
               document.forms[0].elements['obligatorioTelefono'][i].select();
               valor = false;
               break;                  
            }
         }
      }
      else
      {
         if (document.forms[0].elements['obligatorioTelefono']!=null && document.forms[0].elements['obligatorioTelefono'].value.length == 0)
         {
            jsp_alerta("A",'Debe rellenar el campo obligatorio.');
            document.forms[0].elements['obligatorioTelefono'].select();
            valor = false;
         }
         else if (noEsTelefono(document.forms[0].elements['obligatorioTelefono'].value))
         {
            jsp_alerta("A",'El número de teléfono no es válido');
            document.forms[0].elements['obligatorioTelefono'].select();
            valor = false;
         }
      }
   }

   return (valor);

}

function validarTelefonos()
{

   var valor = true;
   var i = 0;
   var max = 0;

   // validamos todas los numeros de telefono
   //if (document.forms[0].elements['telefono'])
   var auxiliar = document.forms[0].elements['telefono'];
   
   if(auxiliar!=null && auxiliar!=undefined)
   {
      max = document.forms[0].elements['telefono'].length;
      if (max > 1)
      {
         for( i=0; i < max; i++)
         {
            if (noEsTelefono(document.forms[0].elements['telefono'][i].value))
            {
               jsp_alerta("A",'El número de teléfono no es válido');
               document.forms[0].elements['telefono'][i].select();
               valor = false;
               break;
            }
         }
      }
      else
      {
         if (noEsTelefono(document.forms[0].elements['telefono'].value))
         {
            jsp_alerta("A",'El número de teléfono no es válido');
            document.forms[0].elements['telefono'].select();
            valor = false;
         }
      }
   }

   return (valor);

}


function noEsTelefono(telefono){
       
   var i;
   var primerosTres = telefono.substring(0,3);
   var erroneos = ['901', '902', '903', '904', '905', '906'];
   
   if(telefono =='')
      return false;
   
   else{
      if(telefono.length != 9)
         return true;
   
      else{
         if(isNaN(telefono))
            return true;
       
         else{
            if(telefono.charAt(0) != '9' && telefono.charAt(0) != '6')
               return true;
       
            else{
               for(i=0;i<6;i++){
                  if(primerosTres == erroneos[i]){
                     i='error';
                     break;   
                  }
               }
               if(i=='error') return true;
               else           return false; 
            }
         }
      }
   }
}

function validarFaxesObligatorios()
{

   var valor = true;
   var i = 0;
   var max = 0;

   // validamos todos los numeros obligatorios
   //if (document.forms[0].elements['obligatorioFax'])
   var auxiliar = document.forms[0].elements['obligatorioFax'];
   
   if (auxiliar!=null && auxiliar!=undefined)
   {
      max = document.forms[0].elements['obligatorioFax'].length;
      if (max > 1)
      {   
         for( i=0; i < max; i++)
         {
            if (document.forms[0].elements['obligatorioFax'][i].value.length == 0)
            {
               jsp_alerta("A",'Debe rellenar todos los campos obligatorios');
               document.forms[0].elements['obligatorioFax'][i].select();
               valor = false;
               break;
            }
            else if (noEsFax(document.forms[0].elements['obligatorioFax'][i].value))
            {
               jsp_alerta("A",'El número de fax no es válido');
               document.forms[0].elements['obligatorioFax'][i].select();
               valor = false;
               break;                  
            }
         }
      }
      else
      {
         //if (document.forms[0].elements['obligatorioFax'].value.length == 0)
         if (document.forms[0].elements['obligatorioFax']!=null && document.forms[0].elements['obligatorioFax'].value.length == 0)
         {
            jsp_alerta("A",'Debe rellenar el campo obligatorio.');
            document.forms[0].elements['obligatorioFax'].select();
            valor = false;
         }
         else if (noEsFax(document.forms[0].elements['obligatorioFax'].value))
         {
            jsp_alerta("A",'El número de fax no es válido');
            document.forms[0].elements['obligatorioFax'].select();
            valor = false;
         }
      }
   }

   return (valor);

}

function validarFaxes()
{

   var valor = true;
   var i = 0;
   var max = 0;

   // validamos todas los numeros de telefono
   //if (document.forms[0].elements['fax'])
   var auxiliar = document.forms[0].elements['fax'];
   
   if(auxiliar!=null && auxiliar!=undefined)
   {
      max = document.forms[0].elements['fax'].length;
      if (max > 1)
      {
         for( i=0; i < max; i++)
         {
            if (noEsFax(document.forms[0].elements['fax'][i].value))
            {
               jsp_alerta("A",'El número de fax no es válido');
               document.forms[0].elements['fax'][i].select();
               valor = false;
               break;
            }
         }
      }
      else
      {
         if (noEsFax(document.forms[0].elements['fax'].value))
         {
            jsp_alerta("A",'El número de fax no es válido');
            document.forms[0].elements['fax'].select();
            valor = false;
         }
      }
   }

   return (valor);

}

function noEsFax(fax){
       
   var i;
   var primerosTres = fax.substring(0,3);
   var erroneos = ['901', '902', '903', '904', '905', '906'];
   
   if(fax =='')
      return false;
   
   else{
      if(fax.length != 9)
         return true;
   
      else{
         if(isNaN(fax))
            return true;
       
         else{
            if(fax.charAt(0) != '9')
               return true;
       
            else{
               for(i=0;i<6;i++){
                  if(primerosTres == erroneos[i]){
                     i='error';
                     break;   
                  }
               }
               if(i=='error') return true;
               else           return false; 
            }
         }
      }
   }
}



function validarFormulario()
{
   var valor = true;
   
   /*if (document.all || )
   { */
      valor = validarObligatorios();
      if ( valor )
      {
          
         valor = validarFechasObligatorias();
         

         if( valor )
         {
            valor = validarHorasObligatorias();
            if( valor )
           {
               valor = validarNumerosObligatorios();
               if( valor )
               {
                  valor= validarTelefonosObligatorios();
                  if( valor )
                  {
                     valor = validarFaxesObligatorios();
               	     if( valor )
               	     {
               	        valor = validarEmailsObligatorios(); 
               	        if( valor )
               	        {
               	           valor = validarFechas();
                           if( valor )
                           {
                              valor = validarHoras();
                              if( valor )
                              {
                                 valor = validarNumeros();
                                 if( valor )
                                 {
                                    valor = validarTelefonos();                   
                                    if( valor )
                                    {
                                       valor = validarFaxes();
                                       if( valor )
                                       {
                                          valor = validarEmails(); 	
                                       }	
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   //}
   return (valor);
}


function validarLista(codigo,descripcion)
{
   if ((eval("document.forms[0]."+codigo+".value") != "") && (eval("document.forms[0]."+descripcion+".value") == "---"))
   {
      jsp_alerta("A","Código incorrecto.");
      eval("document.forms[0]."+codigo+".value=''");
      eval("document.forms[0]."+descripcion+".value=''");
      eval("document.forms[0]."+codigo+".select()");
      return false;
   }
   return true;
}

function validarCodigo(codigo,descripcion)
{
   if(validarLista(codigo,descripcion))
      return true;
   else
      return false;   
}


function comprobarCodigo(seleccion,lineas,codigo)
{	
   for (i=0; i < lineas.length; i++)
   {
      if (i!=seleccion)
      {  
        if(lineas[i][0]==(eval("document.forms[0]."+codigo+".value")))
        {
        	jsp_alerta("A",'Este código ya existe');
        	return false;
        }
      }
   }
   return true;
}


//Valida que el codigo postal pertenezca a la provincia
function validarCodigoPostal(campoCodigoProv, campoCodigoPostal)
{
  if (campoCodigoProv.value == '66'|| campoCodigoProv.value == '99'){
      return true;
  }else {
     if(campoCodigoPostal.value.length!=5){
          jsp_alerta("A","El código postal no tiene la longitud correcta");
          return false;
     }
     if(campoCodigoProv.value == campoCodigoPostal.value.substring(0,2))
        return true;
     else{
        jsp_alerta("A","El código postal no pertenece a la provincia seleccionada");
        campoCodigoPostal.select();

        return false;      	
     }
  }
}

// FECHAS CON FORMATOS: DDMMYY, DDMMYYYY,DD/MM/YY,DD/MM/YYYY,DD_MM_YY,DD_MM_YYYY,DD-MM-YY,DD-MM-YYYY.
/*
function soloCaracteresFecha(e) {                      
    PasaAMayusculas(e);    
    var tecla, caracter;
    var alfanumericos = '0123456789/_-.';
    tecla = e.keyCode;
   	if (tecla == null) return true;
    caracter = String.fromCharCode(tecla);        
    if ((alfanumericos.indexOf(caracter) != -1) || tecla==32)
		return true;
    return false;   
}*/



// FECHAS CON FORMATOS: DDMMYY, DDMMYYYY,DD/MM/YY,DD/MM/YYYY,DD_MM_YY,DD_MM_YYYY,DD-MM-YY,DD-MM-YYYY.
function SoloCaracteresFecha(objeto) {
    var tecla = "";
    if(event.keyCode)
        tecla = event.keyCode;
    else
        tecla = event.which;
    
    if (tecla!=9) {  //NO ES EL TABULADOR    
        var valores='0123456789/_-.';
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
    } else objeto.select();
}

function LS(x) {return String(x).replace(/\b(\d)\b/g, '0$1')}
function LZ(x) {return(x<0||x>9?"":"0")+x}
function LZZ(x) {return(x<0||x>99?x:"0"+LZ(x))}

function TrimS() { 
 return (this.toString() ? this.toString().replace(/\s+$|^\s*/g, "") : "")}
String.prototype.trim = TrimS

Date.prototype.ISOlocaldateStr =
 new Function("with (this) return " +
  "LZ(getDate())+'/'+LZ(getMonth()+1)+'/'+LZ(getFullYear())")

function DataValida(Q) {
 var Mon, x, Rex, B, Y, ND=0
 Q=Q.trim()
 var separador = ' _-./'
 F=0
 for (i = 0; i < Q.length; i++)
 {   
     var c = Q.charAt(i);
     if (separador.indexOf(c) != -1)  F=2;
 }

 if (F==0) {Rex = /(\d{2})(\d{2})(\d+)$/     // D5+ as Y+MMDD
  Q = (Q.search(Rex)==-1 ? '' : Q.replace(Rex, '$3 $2 $1') ) // split
  } // optional paragraph

 Rex = /^(\d+)\D+(\d+)\D+(\d+)$/ // three digit fields
 if (F==2) Q = Q.replace(Rex, '$3 $2 $1') // EU
 
 B = Rex.test(Q) // Split into $1 $2 $3
 
 if (B) with (RegExp) {Y = +$1
  if (Y<100) Y += (Y<60?2000:1900)      // optional century line
   else if ((Y>=100)&&(Y<1000)) Y+=2000 
  with (ND = new Date(Y, $2-1, $3))
   B = ((getMonth()==$2-1) && (getDate()==$3))} // test Y M D valid
 return [B, ND] // [Valid, DateObject]
 } // end DataValida

function ValidarFechaConFormato(formulario, inputFecha) { 
	with (formulario) {
	  var D = DataValida(inputFecha.value);
	  inputFecha.value = ( D[0] ? D[1].ISOlocaldateStr() : inputFecha.value );
	  return D[0]; 
	} 
 }

function DevolverFechaConFormato(fecha) {  
  formato ="dd/mm/yyyy";
  var D = DataValida(fecha);
  if (formato == "dd/mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr() : fecha;
  else if (formato == "mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
  else if (formato == "yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(6) : fecha;
  else if (formato == "mmyyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
  return D[1];
}

function validarFechaAnterior(fechaAnterior,fechaPosterior) {
//Suponiendo que ambas son validas
    var dia1 = fechaAnterior.substring(0,2);
    var mes1 = fechaAnterior.substring(3,5);
    var anho1 = fechaAnterior.substring(6);
    var dia2 = fechaPosterior.substring(0,2);
    var mes2 = fechaPosterior.substring(3,5);
    var anho2 = fechaPosterior.substring(6);
    var fecha1 = new Date(anho1,(mes1-1),dia1);
    var fecha2 = new Date(anho2,(mes2-1),dia2);
    if (fecha1>fecha2) {
        jsp_alerta("A","La fecha anterior no puede ser superior a la posterior");
        return false;
    }
    return true;
}

// Valida si una fecha es menor que otra.
// El formato debe ser el siguiente: dd/mm/yyyy
function esFechaMenorIgualQue(fecha1, fecha2) {
    var exito = false;

    if (fecha1 === fecha2 || fecha1 < fecha2)
        exito = true;

    if (fecha1 !== '' && fecha2 !== '') {
        var inicio = fecha1.split("/");
        var fin = fecha2.split("/");

        var diaIni = inicio[0];
        var mesIni = inicio[1];
        var anhoIni = inicio[2];

        var diaFin = fin[0];
        var mesFin = fin[1];
        var anhoFin = fin[2];

        var dateInicio = new Date(anhoIni, mesIni, diaIni, 0, 0, 0, 0);
        var dateFin = new Date(anhoFin, mesFin, diaFin, 0, 0, 0, 0);

        if (dateInicio === dateFin || dateInicio < dateFin)
            exito = true;
    }

    return exito;
};

// Funciones que estaban en una clase JAVA, ValidarDocumento.java y que las cargaba cuando se llamaba a esta clase

/**
comprueba que la cadena es del tipo indicado
n -> numerico
a -> letra
x -> alfanumericio
return  true    --> hay error
        false   --> no hay error
*/
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

 function letraNIF(nif){
 	var cadena = 'TRWAGMYFPDXBNJZSQVHLCKET';
 	var pos = nif % 23;
 	return(cadena.charAt(pos));
 }
 
 
 // Validacion de correo electronico	
function validarEmail(email) {	
    var regexp = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;	
    return regexp.test(email);	
}
