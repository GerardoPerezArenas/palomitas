var MONTH_NAMES=new Array('Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre','Ene','Feb','Mar','Abr','May','Jun','Jul','Ago','Sep','Oct','Nov','Dic');
function LZ(x) {return(x<0||x>9?"":"0")+x}


// ------------------------------------------------------------------
// Utility functions for parsing in getDateFromFormat()
// ------------------------------------------------------------------
function _isInteger(val) {
	var digits="1234567890";
	for (var i=0; i < val.length; i++) {
		if (digits.indexOf(val.charAt(i))==-1) { return false; }
		}
	return true;
	}
function _getInt(str,i,minlength,maxlength) {
	for (var x=maxlength; x>=minlength; x--) {
		var token=str.substring(i,i+x);
		if (token.length < minlength) { return null; }
		if (_isInteger(token)) { return token; }
		}
	return null;
	}

// ------------------------------------------------------------------
// getDateFromFormat( date_string , format_string )
//
// This function takes a date string and a format string. It matches
// If the date string matches the format string, it returns the 
// getTime() of the date. If it does not match, it returns 0.
// ------------------------------------------------------------------
function getDateFromFormat(val,format) {
	val=val+"";
	format=format+"";
	var i_val=0;
	var i_format=0;
	var c="";
	var token="";
	var token2="";
	var x,y;
	var now=new Date();
	var year=now.getYear();
	var month=now.getMonth()+1;
	var date=now.getDate();
	var hh=now.getHours();
	var mm=now.getMinutes();
	var ss=now.getSeconds();
	var ampm="";
	
	while (i_format < format.length) {
		// Get next token from format string
		c=format.charAt(i_format);
		token="";
		while ((format.charAt(i_format)==c) && (i_format < format.length)) {
			token += format.charAt(i_format++);
			}
		// Extract contents of value based on format token
		if (token=="yyyy" || token=="yy" || token=="y") {
			if (token=="yyyy") { x=4;y=4; }
			if (token=="yy")   { x=2;y=2; }
			if (token=="y")    { x=2;y=4; }
			year=_getInt(val,i_val,x,y);
			if (year==null) { return 0; }
			i_val += year.length;
			if (year.length==2) {
				if (year > 70) { year=1900+(year-0); }
				else { year=2000+(year-0); }
				}
			}
		else if (token=="MMM"){
			month=0;
			for (var i=0; i<MONTH_NAMES.length; i++) {
				var month_name=MONTH_NAMES[i];
				if (val.substring(i_val,i_val+month_name.length).toLowerCase()==month_name.toLowerCase()) {
					month=i+1;
					if (month>12) { month -= 12; }
					i_val += month_name.length;
					break;
					}
				}
			if ((month < 1)||(month>12)){return 0;}
			}
		else if (token=="MM"||token=="M") {
			month=_getInt(val,i_val,token.length,2);
			if(month==null||(month<1)||(month>12)){return 0;}
			i_val+=month.length;
			}
		else if (token=="dd"||token=="d") {
			date=_getInt(val,i_val,token.length,2);
			if(date==null||(date<1)||(date>31)){return 0;}
			i_val+=date.length;
			}
		else if (token=="hh"||token=="h") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<1)||(hh>12)){return 0;}
			i_val+=hh.length;}
		else if (token=="HH"||token=="H") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<0)||(hh>23)){return 0;}
			i_val+=hh.length;}
		else if (token=="KK"||token=="K") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<0)||(hh>11)){return 0;}
			i_val+=hh.length;}
		else if (token=="kk"||token=="k") {
			hh=_getInt(val,i_val,token.length,2);
			if(hh==null||(hh<1)||(hh>24)){return 0;}
			i_val+=hh.length;hh--;}
		else if (token=="mm"||token=="m") {
			mm=_getInt(val,i_val,token.length,2);
			if(mm==null||(mm<0)||(mm>59)){return 0;}
			i_val+=mm.length;}
		else if (token=="ss"||token=="s") {
			ss=_getInt(val,i_val,token.length,2);
			if(ss==null||(ss<0)||(ss>59)){return 0;}
			i_val+=ss.length;}
		else if (token=="a") {
			if (val.substring(i_val,i_val+2).toLowerCase()=="am") {ampm="AM";}
			else if (val.substring(i_val,i_val+2).toLowerCase()=="pm") {ampm="PM";}
			else {return 0;}
			i_val+=2;}
		else {
			if (val.substring(i_val,i_val+token.length)!=token) {return 0;}
			else {i_val+=token.length;}
			}
		}
	// If there are any trailing characters left in the value, it doesn't match
	if (i_val != val.length) { return 0; }
	// Is date valid for month?
	if (month==2) {
		// Check for leap year
		if ( ( (year%4==0)&&(year%100 != 0) ) || (year%400==0) ) { // leap year
			if (date > 29){ return false; }
			}
		else { if (date > 28) { return false; } }
		}
	if ((month==4)||(month==6)||(month==9)||(month==11)) {
		if (date > 30) { return false; }
		}
	// Correct hours value
	if (hh<12 && ampm=="PM") { hh+=12; }
	else if (hh>11 && ampm=="AM") { hh-=12; }
	var newdate=new Date(year,month-1,date,hh,mm,ss);
	return newdate.getTime();
}
// -------------------------------------------------------------------
// compareDates(date1,date1format,date2,date2format)
//   Compare two date strings to see which is greater.
//   Returns:
//   1 if date1 is greater than date2
//   0 if date2 is greater than date1 of if they are the same
//  -1 if either of the dates is in an invalid format
// -------------------------------------------------------------------
function compareDates(date1,date2) 
{
   var d1=getDateFromFormat(date1,'dd/MM/yyyy');
   var d2=getDateFromFormat(date2,'dd/MM/yyyy');
   
   if (d1==0 || d2==0) 
   {
 	return -1;
   }
   else if (d1 > d2) 
   {
	return 1;
   }
   return 0;

}

// -------------------------------------------------------------------
//   comparaFechas(date1,date1format,date2,date2format)
//   Compara dos fechas string.
//   Returns:
//   1  si date1 mayor que date2
//   0  if date1 igual que date2
//   -1 if date1 menor que date2
//   100 if alguna de las fechas tiene formato inválido 
// -------------------------------------------------------------------
function comparaFechas(date1,date2) 
{
   var d1=getDateFromFormat(date1,'dd/MM/yyyy');
   var d2=getDateFromFormat(date2,'dd/MM/yyyy');
   
   if (d1==0 || d2==0) 
   {
 	return 100;
   }
   if (d1 > d2) 
   {
	return 1;
   }
   if (d1 < d2) 
   {
	return -1;
   }
   return 0;

}


function sub(fecha,dias)
{
        var anio=fecha.substr(6,4);
        var mes=fecha.substr(3,2);
        var dia=fecha.substr(0,2);
	var fec_actual= new Date(anio,mes, dia);
        var fec_resul=null;
	var fecha_long;
        var num_dias;
        var num_mili;
        var resultado;
 	fecha_long = fec_actual.getTime();
        num_mili = dias * 60 * 60 * 24 * 1000;
        num_mili = fecha_long - num_mili;
        fec_resul= new Date(num_mili);
	mes = parseInt(fec_resul.getMonth());
	if ( mes < 10 ) mes= '0' + mes;
	dia = fec_resul.getDate();
	if ( dia < 10 ) dia= '0' + dia;
	resultado= dia + '/' +  mes + '/' + fec_resul.getFullYear();
        return resultado;
}

function sumaDias(fecha,dias)
{
	var strDia, strMes, strAnio;
	var fechaMili;
	var fec_resul='';
	
	strAnio=fecha.substr(6);
        
        if(fecha.substring(3,5).indexOf('0') == 0)   strMes = parseInt(fecha.substring(4,5));
        else                                         strMes = parseInt(fecha.substring(3,5));
        strMes--;
        
        if(fecha.substring(0,2).indexOf('0') == 0)   strDia = parseInt(fecha.substring(1,2));
        else                                         strDia = parseInt(fecha.substring(0,2));
                
	fechaMili = (new Date(strAnio,strMes, strDia)).getTime();
        fec_resul= new Date(fechaMili +  (dias * 60 * 60 * 24 * 1000) );

	mes = parseInt(fec_resul.getMonth()) +1;
	if(mes<10) mes = '0' + mes.toString();
	
	dia = fec_resul.getDate();
	if (dia<10 ) dia= '0' + dia;
	
        return ( dia + '/' +  mes + '/' + fec_resul.getFullYear() );
}




function sumaMeses(fechaEntrada,numMeses) {

   var anio=parseInt(fechaEntrada.substr(6,4));
   var mes=parseInt(fechaEntrada.substr(3,2))-1;
   var dia=parseInt(fechaEntrada.substr(0,2));
   var fechaAux= new Date(anio, mes, dia);
   var fecha = new Date(fechaAux.getYear(),fechaAux.getMonth() + numMeses,fechaAux.getDate());

   mes = parseInt(fecha.getMonth() + 1);
   if ( mes < 10 ) mes= '0' + mes;
   dia = fecha.getDate();
   if ( dia < 10 ) dia= '0' + dia;
   resultado= dia + '/' +  mes + '/' + fecha.getFullYear();
   return(resultado);

}

function sumaMeses2(fechaEntrada,numMeses) {

   var anio=parseInt(fechaEntrada.substr(6,4));
   var mes=parseInt(fechaEntrada.substr(4,2))-1;
   var dia=parseInt(fechaEntrada.substr(0,2));
   var fechaAux= new Date(anio, mes, dia);
   var fecha = new Date(fechaAux.getYear(),fechaAux.getMonth() + numMeses,fechaAux.getDate());

   mes = parseInt(fecha.getMonth() + 1);
   if ( mes < 10 ) mes= '0' + mes;
   dia = fecha.getDate();
   if ( dia < 10 ) dia= '0' + dia;
   resultado= dia + '/' +  mes + '/' + fecha.getFullYear();
   return(resultado);

}

//Pasando un fecha en formato Date la pasa a formato dd/mm/yyyy

function mostrarFecha(fecha) {

   if (fecha.getDate()<10)
      var la_fecha = '0'+fecha.getDate();
   else 
      var la_fecha = fecha.getDate();
   var mes = fecha.getMonth()+1;
   if (mes<10)
       la_fecha = la_fecha+"/0"+mes;
   else 
      la_fecha = la_fecha+"/"+mes;
   la_fecha = la_fecha+"/"+fecha.getYear();
   return la_fecha
}


function literalFecha(fecha, idioma){
   var ano, dia, i, literalFecha, literalMes, mes;
   var meses = new Array();
   
   if(idioma=='castellano') meses = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];  	
   if(idioma=='euskera')    meses = ['Urtarrila', 'Otsaila', 'Martxoa', 'Apirila', 'Maiatza', 'Ekaina', 'Uztaila', 'Abuztua', 'Iraila', 'Urria', 'Azaroa', 'Abendua'];  	
      
   if(fecha.substring(0,2).indexOf('0') == 0)  dia = fecha.substring(1,2);
   else                                        dia = fecha.substring(0,2);
   
   mes = parseInt(fecha.substring(3,5), 10) - 1;
   ano = fecha.substring(6);

   for(i=0;i<12;i++){
      if(i == mes){
         literalMes = meses[i];
         break;
      }
   }

   if(idioma=='castellano')    literalFecha  =  dia + ' de ' + literalMes + ' de ' + ano;
   if(idioma=='euskera')       literalFecha  =  ano + 'ko ' + literalMes + 'k ' + dia;
                 
   return literalFecha;           
}


function fechaJuliana(fecha){
   var fechaJuliana='';
   var mes;
   var dias=0;
   var i;
   var anho=fecha.substring(6);
   var dias_mes=new Array(13);
   dias_mes[1]=31;
   dias_mes[2]=28;  if(Math.round(anho-(Math.floor(anho/4)*4))==0)   dias_mes[2]=29;
   dias_mes[3]=31;
   dias_mes[4]=30;
   dias_mes[5]=31;
   dias_mes[6]=30;
   dias_mes[7]=31;
   dias_mes[8]=31;
   dias_mes[9]=30;
   dias_mes[10]=31;
   dias_mes[11]=30;
   dias_mes[12]=31;
   
   mes = parseInt(fecha.substring(3,5), 10); 
   
   for(i=1; i<mes ; i++)
      dias = dias + dias_mes[i];
      
   dias = ( dias + parseInt(fecha.substring(0,2), 10) ).toString();  
   
   if(dias.length == 1)  dias = '00' + dias;
   if(dias.length == 2)  dias = '0'  + dias;   
         
   fechaJuliana = fecha.substring(8) + dias ;
   
   return fechaJuliana;
}

function parsearFecha(fecha){
    function numConCero(n){ 
        return n<10 ? "0" + n : n; 
    };
    return numConCero(fecha.getUTCDate()) + "/"
         + numConCero(fecha.getUTCMonth() + 1) + "/"
         + fecha.getUTCFullYear();
}