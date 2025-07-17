function isUndefined(a) { return ((typeof a == 'undefined') || (a==null)); }

var ppcX = 4;
var ppcY = 4;

var IsCalendarVisible;
var calfrmName; 
var maxYearList;
var minYearList;
var todayDate = new Date; 
var curDate = new Date;
var pulseDay = new Date; 
var list;
var curImg;
var curDateBox;
var curDateBox2;
var curDateDiaBox;
var curDateMesBox;
var curDateAnoBox;
var minDate = new Date;
var maxDate = new Date;
var hideDropDowns;
var IsUsingMinMax;
var FuncsToRun;

minYearList=todayDate.getFullYear()-150;
maxYearList=todayDate.getFullYear()+10;
IsCalendarVisible=false;

var anhoReplegado=true;

// Funcion que obtiene la posición del evento que se ejecute, el click de ratón
function posicionY(evento){
    return evento.clientY;
}

function posicionX(evento){
    return evento.clientX;
}
function showCalendar(frmName, dteBox, dteDia, dteMes, dteAno, dteBox2, btnImg, list_elements, hideDrops, MnDt, MnMo, MnYr, MxDt, MxMo, MxYr,runFuncs,evento) {
    list = list_elements;
    hideDropDowns = hideDrops;
    FuncsToRun = runFuncs;
    calfrmName = frmName;

    if (IsCalendarVisible) {
        hideCalendar();
    }
    else {
        if (hideDropDowns) {toggleDropDowns('hidden');}
        if ((MnDt!=null) && (MnMo!=null) && (MnYr!=null) && (MxDt!=null) && (MxMo!=null) && (MxYr!=null)) {
            IsUsingMinMax = true;
            minDate.setDate(MnDt);
            minDate.setMonth(MnMo-1);
            minDate.setFullYear(MnYr);
            maxDate.setDate(MxDt);
            maxDate.setMonth(MxMo-1);
            maxDate.setFullYear(MxYr);
        } else {
            IsUsingMinMax = false;
        }        		
		
        curImg = btnImg;
        curDateBox = dteBox;
        curDateBox2 = dteBox2;
        curDateDiaBox=dteDia;
        curDateMesBox=dteMes;
        curDateAnoBox=dteAno;

        anhoReplegado=true;
        var dimContPantalla = document.getElementsByClassName("contenidoPantalla")[0].getBoundingClientRect();
       var scrollContPantalla= document.getElementsByClassName("contenidoPantalla")[0].scrollTop;
      
        ppcY = posicionY(evento)+10 - dimContPantalla.top+scrollContPantalla;
        ppcX = posicionX(evento)+10;

      
     
      
        
        if(ppcX+170>dimContPantalla.width) 
            ppcX = ppcX - 165;
        if(ppcY+170>(dimContPantalla.height+scrollContPantalla) )
            ppcY = ppcY - 133;
        


        domlay('popupcalendar',1,ppcX,ppcY,Calendar(todayDate.getMonth(),todayDate.getFullYear()));       

        IsCalendarVisible = true;
    }
}

function toggleDropDowns(showHow){
    var i; var j;
    for (i=0;i<document.forms.length;i++) {
        for (j=0;j<document.forms[i].elements.length;j++) {
            if (document.forms[i].elements[j].tagName == "SELECT") {
                if (document.forms[i].name != "Cal")
                    document.forms[i].elements[j].style.visibility=showHow;
            }
        }
    }
}

function hideCalendar(){
    domlay('popupcalendar',0,ppcX,ppcY);    
    window.status=' ';
    IsCalendarVisible = false;
    if (hideDropDowns) {toggleDropDowns('visible');}    
}

function ocultarCalendario() {
	if(IsCalendarVisible) {
		IsCalendarVisible = false;
		domlay('popupcalendar',0,ppcX,ppcY);    		
	}
}

function ocultarCalendarioOnBlur(evento) {
}

function replegarCalendario(coordX,coordY) //Esta funcion solo se ejecuta en Firefox, ya que necesitamos las coordenadas del documento
{
    if(anhoReplegado==true){
        
        if ((coordX > ppcX + 190) || (coordY > ppcY + 180) ||
            (coordX < ppcX) || (coordY < ppcY)){
            hideCalendar();
        }
    }
}

function calClick(evento) {
        
        window.focus();
}

function domlay(id,trigger,lax,lay,content) {
    if (document.getElementById(id)==null) {
        var divCalendar = document.createElement("div");
        divCalendar.id =id;
        document.body.getElementsByClassName("contenidoPantalla")[0].appendChild(divCalendar);
    }

    // Layer visible
    if (trigger=="1"){
        if (document.layers){ document.layers[''+id+''].visibility = "show"; }
        else if (document.all){ document.all[''+id+''].style.visibility = "visible"; }
        else if (document.getElementById){ document.getElementById(''+id+'').style.visibility = "visible" ;    }
        }
    // Layer hidden
    else if (trigger=="0"){
        if (document.layers) document.layers[''+id+''].visibility = "hide"
        else if (document.all) document.all[''+id+''].style.visibility = "hidden"
        else if (document.getElementById) document.getElementById(''+id+'').style.visibility = "hidden"             
        }
    // Set horizontal position  
	
	//compatibilidad IE9 + Edge +firefox + Chrome
    if (lax){
		if (document.getElementById(''+id+'').style.posLeft !== "undefined") {document.getElementById(''+id+'').style.posLeft=lay;}  //Firefox + Chrome ignoran esto			
		document.getElementById(''+id+'').style.left=lax+"px"; //IE9 ignora esto
	}
    // Set vertical position	
    if (lay){
		if (document.getElementById(''+id+'').style.posTop !== "undefined") {document.getElementById(''+id+'').style.posTop=lay;}	  //Firefox + Chrome ignoran esto	
		document.getElementById(''+id+'').style.top=lay+"px"; //IE9 ignora esto
			
	}
	
	
    // change content

    if (content){
    if (document.layers){
        sprite=document.layers[''+id+''].document;
        // add father layers if needed! document.layers[''+father+'']...
        sprite.open();
        sprite.write(content);
        sprite.close();
        }
    else if (document.all) document.all[''+id+''].innerHTML = content;  
    else if (document.getElementById){
        //Thanx Reyn!
        rng = document.createRange();
        el = document.getElementById(''+id+'');
        rng.setStartBefore(el);
        htmlFrag = rng.createContextualFragment(content)
        while(el.hasChildNodes()) el.removeChild(el.lastChild);
        el.appendChild(htmlFrag);
        // end of Reyn ;)
        }
    }
}

function Calendar(whatMonth,whatYear) {
    var output = '';
    var datecolwidth;
    var startMonth;
    var startYear;
    startMonth=whatMonth;
    startYear=whatYear;

    curDate.setMonth(whatMonth);
    curDate.setFullYear(whatYear);
    curDate.setDate(todayDate.getDate());
    
    output = "<div style=\"position:relative;clear:both;z-index:3;\">";
    output += '<form name="Cal"><table width="170" border="0" class="cal-Table" cellspacing="0" cellpadding="0"><tr>';
  
     
    output += '<td class="cal-HeadCell" valign="middle" align="center" width="100%"><a href="javascript:scrollMonth(-1);" class="calDir"><span class="fa fa-caret-left"></span></a>&nbsp;<SELECT class="cal-TextBox" name="cboMonth" onChange="changeMonth();">';
    for (month=0; month<12; month++) {
        if (month == whatMonth) 
            output += '<OPTION VALUE="' + month + '" SELECTED>' + names[month] + '</OPTION>';
        else                
            output += '<OPTION VALUE="' + month + '">'          + names[month] + '</OPTION>';
    }

    output += '</SELECT><SELECT class="cal-TextBox" onclick="anhoReplegado=false" NAME="cboYear" onChange="changeYear();">';

    for (year=minYearList; year<maxYearList; year++) {
        if (year == whatYear) 
            output += '<OPTION VALUE="' + year + '" SELECTED>' + year + '<\/OPTION>';
        else
            output += '<OPTION VALUE="' + year + '">' + year + '<\/OPTION>';
    }

    output += '</SELECT>&nbsp;<a href="javascript:scrollMonth(1);" class="calDir"><span class="fa fa-caret-right"></span></a></td></tr><tr><td width="100%" align="center">';

    firstDay = new Date(whatYear,whatMonth,1);
    startDay = firstDay.getDay() - 1;

    startDay=startDay==-1?6:startDay==6?-1:startDay;
    
    if (((whatYear % 4 == 0) && (whatYear % 100 != 0)) || (whatYear % 400 == 0))
         days[1] = 29;
    else
         days[1] = 28;

    output += '<table width="185" cellspacing="1" cellpadding="2" border="0"><tr>';

    for (i=0; i<7; i++) {
        if (i==0 || i==6) {
            datecolwidth="15%"
        }
        else {
            datecolwidth="14%"
        }
        output += '<td class="cal-HeadCell" width="' + datecolwidth + '" align="center" valign="middle">'+ dow[i] +'</td>';
    }
    
    output += '</tr><tr>';

    var column = 0;
    var lastMonth = whatMonth - 1;
    var lastYear = whatYear;
    if (lastMonth == -1) { lastMonth = 11; lastYear=lastYear-1;}

    for (i=0; i<startDay; i++, column++) {
    	if(column==6)
           output += getDayLink((days[lastMonth]-startDay+i+1),true,lastMonth,lastYear,true);
        else   
           output += getDayLink((days[lastMonth]-startDay+i+1),true,lastMonth,lastYear,false);
    }

    for (i=1; i<=days[whatMonth]; i++, column++) {
        if(column==6)
           output += getDayLink(i,false,whatMonth,whatYear,true);
        else
           output += getDayLink(i,false,whatMonth,whatYear,false);
        
        if (column == 6) {
            output += '</tr><tr>';
            column = -1;
        }
    }
       
    var nextMonth = whatMonth+1;
    var nextYear = whatYear;
    if (nextMonth==12) { nextMonth=0; nextYear=nextYear+1;}
    
    if (column > 0) {
        for (i=1; column<7; i++, column++) {
           if(column==6)	
              output +=  getDayLink(i,true,nextMonth,nextYear,true);
           else
              output +=  getDayLink(i,true,nextMonth,nextYear,false);
        }
        output += '</tr></table></td></tr>';
    }
    else {
        output = output.substr(0,output.length-4); // remove the <tr> from the end if there's no last row
        output += '</table></td></tr>';
    }
    
    output += '<tr class="subtitulo"><td align="center"><input type="button" class="boton" name="anular" value="Anular fecha" onclick="fechaNula();hideCalendar();">&nbsp;&nbsp;<a href="#" onclick="javascript:hideCalendar();" style="text-decoration:none;"><span style="color:red;font-family:Verdana,Arial;font-size:20px"><strong>X</strong></a></span></td></tr><\/form><\/table>';
    output += "</div>";
   
    curDate.setDate(1);
    curDate.setMonth(startMonth);
    curDate.setFullYear(startYear);
    return output;
}

function getDayLink(linkDay,isGreyDate,linkMonth,linkYear,domingo) {
    var templink;
        var fech = new Date(linkYear+'/'+(linkMonth+1)+'/'+linkDay);
        var k = fech.getDay();
    if (!(IsUsingMinMax)) {
        if (isGreyDate) {
            templink='<td align="center" class="cal-GreyDate">' + linkDay + '</td>';
        }
        else {
            if (isDayToday(linkDay)) {
                          if(k==1 || list=='') {
                templink='<td align="center" class="cal-DayCell">' + '<a class="cal-TodayLink" href="javascript:changeDay(' + linkDay + ');">' + linkDay + '</a>' +'</td>';
                          } else {
                                templink='<td align="center" class="cal-DayCell">' + '<a class="cal-TodayLink">' + linkDay + '</a>' +'</td>';                       
                          }
            }
            else {
                          if(k==1 || list=='') {
                             if(domingo) 	
                                templink='<td align="center" class="cal-DayCell">' + '<a class="cal-DayFestivo" href="javascript:changeDay(' + linkDay + ');">' + linkDay + '</a>' +'</td>';
                             else   
                                templink='<td align="center" class="cal-DayCell">' + '<a class="lun-Link" href="javascript:changeDay(' + linkDay + ');">' + linkDay + '</a>' +'</td>';
                          } 
                          else {
                             if(domingo) 	
                                 templink='<td align="center" class="cal-DayCell">' + '<a class="cal-DayFestivo">' + linkDay + '</a>' +'</td>';
                             else    
                                 templink='<td align="center" class="cal-DayCell">' + '<a class="cal-DayLink">' + linkDay + '</a>' +'</td>';
                          }                             
            }
        }
    }
    else {
        if (isDayValid(linkDay,linkMonth,linkYear)) {

            if (isGreyDate){
                templink='<td align="center" class="cal-GreyDate">' + linkDay + '</td>';
            }
            else {
                if (isDayToday(linkDay)) {
                                  if(k==1 || list=='') {
                    templink='<td align="center" class="cal-DayCell">' + '<a class="cal-TodayLink" href="javascript:changeDay(' + linkDay + ');">' + linkDay + '</a>' +'</td>';
                                  } else {
                                    templink='<td align="center" class="cal-DayCell">' + '<a class="cal-TodayLink">' + linkDay + '</a>' +'</td>';
                                  }                               
                }
                else {
                                  if(k==1 || list=='') {  
                                     if(domingo)
                                        templink='<td align="center" class="cal-DayCell">' + '<a class="cal-DayFestivo" href="javascript:changeDay(' + linkDay + ');">' + linkDay + '</a>' +'</td>';
                                     else
                                        templink='<td align="center" class="cal-DayCell">' + '<a class="lun-Link" href="javascript:changeDay(' + linkDay + ');">' + linkDay + '</a>' +'</td>';
                                  } 
                                  else {
                                     if(domingo)	
                                        templink='<td align="center" class="cal-DayFestivo">' + '<a class="cal-DayLink">' + linkDay + '</a>' +'</td>';
                                     else
                                        templink='<td align="center" class="cal-DayCell">' + '<a class="cal-DayLink">' + linkDay + '</a>' +'</td>'; 
                                  }
                }
            }
        }
        else {
            templink='<td align="center" class="cal-GreyInvalidDate">'+ linkDay + '</td>';
        }
    }
    return templink;
}

function isDayToday(isDay) {
    if ((curDate.getFullYear() == todayDate.getFullYear()) && (curDate.getMonth() == todayDate.getMonth()) && (isDay == todayDate.getDate())) {
        return true;
    }
    else {
        return false;
    }
}

function isDayValid(validDay, validMonth, validYear){
    
    curDate.setDate(validDay);
    curDate.setMonth(validMonth);
    curDate.setFullYear(validYear);
    
    if ((curDate>=minDate) && (curDate<=maxDate)) {
        return true;
    }
    else {
        return false;
    }
}

function padout(number) { return (number < 10) ? '0' + number : number; }

function clearDay() {
    if (curDateBox != null)
		eval('document.' + calfrmName + '.' + curDateBox + '.value = \'\'');
    if (curDateDiaBox != null)
		eval('document.' + calfrmName + '.' + curDateDiaBox + '.value = \'\'');
	if (curDateMesBox != null)
		eval('document.' + calfrmName + '.' + curDateMesBox + '.value = \'\'');
    if (curDateAnoBox != null)
		eval('document.' + calfrmName + '.' + curDateAnoBox + '.value = \'\'');
    if(curDateBox2!='') eval('document.' + calfrmName + curDateBox2 + '.value = \'\'');	
    hideCalendar();
    if (FuncsToRun!=null)
        eval(FuncsToRun); 
}

function changeDay(whatDay) {
    pulseDay.setDate(whatDay);
	var aux = curDate.getMonth();
	pulseDay.setMonth(aux);
	aux = curDate.getYear();
	pulseDay.setYear(aux);
    curDate.setDate(whatDay);
    if (curDateBox != null)
		eval('document.' + calfrmName + '.' + curDateBox + '.value = "'+ padout(curDate.getDate()) + '/' + mes[curDate.getMonth()] + '/' + curDate.getFullYear() + '"');	
    if (curDateDiaBox != null)
		eval('document.' + calfrmName + '.'+ curDateDiaBox+'.value = "'+ padout(curDate.getDate()) + '"');	
    if (curDateMesBox != null)
		eval('document.' + calfrmName + '.'+ curDateMesBox+'.value = "'+ mes[curDate.getMonth()] + '"');	
    if (curDateAnoBox != null)
		eval('document.' + calfrmName + '.'+ curDateAnoBox+'.value = "'+ curDate.getFullYear() + '"');	
    //eval('document.' + calfrmName + '.' + curDateBox + '.disabled = false');	    
    //eval('document.' + calfrmName + '.' + curDateBox + '.focus()');	

	if(curDateBox2!=''){
	  var dat = week_last(curDate);	
      eval('document.' + calfrmName +  '.' + curDateBox2 + '.value = "'+ padout(dat.getDate()) + '/' + mes[dat.getMonth()] + '/' + dat.getFullYear() + '"');		
	}
	if(list!='') {

      eval('listaTabla = week(pulseDay);');

	  eval('mostrarLista();');
	}
    hideCalendar();
    if (FuncsToRun!=null)
        eval(FuncsToRun); 
    
    try{
        recargarCamposCalculados();
        
    }catch(err){        
    }
}

function scrollMonth(amount) {
    var monthCheck;
    var yearCheck;
    
    monthCheck = document.getElementsByName("cboMonth")[0].selectedIndex + amount;

    if (monthCheck < 0) {
        yearCheck = curDate.getFullYear() - 1;
        if ( yearCheck < minYearList ) {
            yearCheck = minYearList;
            monthCheck = 0;
        }
        else {
            monthCheck = 11;
        }
        curDate.setFullYear(yearCheck);
    }
    else if (monthCheck >11) {
        yearCheck = curDate.getFullYear() + 1;
        if ( yearCheck > maxYearList-1 ) {
            yearCheck = maxYearList-1;
            monthCheck = 11;
        }
        else {
            monthCheck = 0;
        }      
        curDate.setFullYear(yearCheck);
    }
    
    curDate.setMonth(monthCheck);

    domlay('popupcalendar',1,ppcX,ppcY,Calendar(curDate.getMonth(),curDate.getFullYear()));
}

function changeMonth() {
    curDate.setMonth(document.getElementsByName("cboMonth")[0].options[document.getElementsByName("cboMonth")[0].selectedIndex].value);
    domlay('popupcalendar',1,ppcX,ppcY,Calendar(curDate.getMonth(),curDate.getFullYear()));
}

function changeYear() {
    curDate.setFullYear(document.getElementsByName("cboYear")[0].options[document.getElementsByName("cboYear")[0].selectedIndex].value);
    domlay('popupcalendar',1,ppcX,ppcY,Calendar(curDate.getMonth(),curDate.getFullYear()));
}

function week_last(date) {
   var dia = date.getDate();
   var dia_fin = dia+6;
   var m = date.getMonth();
   var y = date.getYear();
 
   var auxDate = new Date(y,m,dia_fin); 
   return auxDate;
}

function week(date) {
 
   var listaDias = new Array();
   var auxDate = date;
   var dia = date.getDate();

   var dia_fin = dia+6;

   var m = date.getMonth();
   var y = date.getYear();   
 
   var j = 0;
   var x = 0;
   
   
   for(var k = dia; k <= dia_fin; k++) {
   	 var auxDia = new Date(y,m,k);   
     listaDias[j] = auxDia;
	 j++;	   	 
   }  
   
   return listaDias;  
}

function makeArray0() {
    for (i = 0; i<makeArray0.arguments.length; i++)
        this[i] = makeArray0.arguments[i];
}

function fechaNula(){
    	 if (curDateBox != null)
				eval('document.' + calfrmName + '.' + curDateBox + '.value = ""');
    	 if (curDateDiaBox != null)
				eval('document.' + calfrmName + '.' + curDateDiaBox + '.value = ""');
	     if (curDateMesBox != null)
			eval('document.' + calfrmName + '.' + curDateMesBox + '.value = ""');
		 if (curDateAnoBox != null)
	    	 eval('document.' + calfrmName + '.' + curDateAnoBox + '.value = ""');
		 //eval('document.' + calfrmName +  '.' + curDateBox2 + '.value = ""');
                  if (FuncsToRun!=null)
        eval(FuncsToRun);
}


function estaVisible() {
	return IsCalendarVisible;
}



var names     = new makeArray0('Ene','Feb','Mar','Abr','May','Jun','Jul','Ago','Sep','Oct','Nov','Dic');
var mes	      = new makeArray0('01','02','03','04','05','06','07','08','09','10','11','12');
var days      = new makeArray0(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
var dow       = new makeArray0('L','M','X','J','V','S','D');

var diasFestivos= new Array();

/***************************************************************************/
/*  HABILITAR IMAGEN CALENDARIO 					   */
/***************************************************************************/
function habilitarImagenCal(elementoQueLlama, valor) {
    var vector = [document.getElementById(elementoQueLlama)];
    deshabilitarIconos(vector, !valor);
}

function deshabilitarImagenCal(elementoQueLlama, valor) {
    var vector = [document.getElementById(elementoQueLlama)];
    deshabilitarIconos(vector, valor);
}

//Esta funcion funciona para todo los navegadores	
function replegarCalendarioGeneral(evento) {
    var elemento = document.getElementById('popupcalendar');	
    	
    if (evento.target !== elemento && !elemento.contains(evento.target)) {	
        hideCalendar();	
    }	
}
