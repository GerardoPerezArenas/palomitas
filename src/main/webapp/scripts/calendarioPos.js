function isUndefined(a) { return ((typeof a == 'undefined') || (a==null)); }
var DEFAULT_CALENDAR_IMAGES_DEL="/images/calendario/btn_del_small.gif";
var DEFAULT_CALENDAR_IMAGES_CLOSE="/images/calendario/btn_close_small.gif";
var DEFAULT_CALENDAR_IMAGES_UP="/images/calendario/icono.gif";
var DEFAULT_CALENDAR_IMAGES_OVER="/images/calendario/icono.gif";
var DEFAULT_CALENDAR_IMAGES_DOWN="/images/calendario/icono.gif";
var DEFAULT_CALENDAR_IMAGES_MAS="/images/calendario/mas.gif";
var DEFAULT_CALENDAR_IMAGES_MENOS="/images/calendario/menos.gif";
var DEFAULT_CALENDAR_IMAGES_ICONO="/images/calendario/icono.gif";
var DEFAULT_CALENDAR_IMAGES_ICONO2="/images/calendario/icono2.gif";

var CALENDAR_IMAGES_DEL   = APP_CONTEXT_PATH + DEFAULT_CALENDAR_IMAGES_DEL;
var CALENDAR_IMAGES_CLOSE = APP_CONTEXT_PATH + DEFAULT_CALENDAR_IMAGES_CLOSE;
var CALENDAR_IMAGES_UP    = APP_CONTEXT_PATH + DEFAULT_CALENDAR_IMAGES_UP;
var CALENDAR_IMAGES_OVER  = APP_CONTEXT_PATH + DEFAULT_CALENDAR_IMAGES_OVER;
var CALENDAR_IMAGES_DOWN  = APP_CONTEXT_PATH + DEFAULT_CALENDAR_IMAGES_DOWN;
var CALENDAR_IMAGES_MAS   = APP_CONTEXT_PATH + DEFAULT_CALENDAR_IMAGES_MAS;
var CALENDAR_IMAGES_MENOS = APP_CONTEXT_PATH + DEFAULT_CALENDAR_IMAGES_MENOS;
var CALENDAR_IMAGES_ICONO = APP_CONTEXT_PATH + DEFAULT_CALENDAR_IMAGES_ICONO;
var CALENDAR_IMAGES_ICONO2= APP_CONTEXT_PATH + DEFAULT_CALENDAR_IMAGES_ICONO2;


var ppcIE=((navigator.appName == "Microsoft Internet Explorer") || ((navigator.appName == "Netscape") && (parseInt(navigator.appVersion)==5)));
var ppcNN6=((navigator.appName == "Netscape") && (parseInt(navigator.appVersion)==5));
//var ppcIE=(navigator.appName == "Microsoft Internet Explorer");
var ppcNN=((navigator.appName == "Netscape")&&(document.layers));
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
var img_del;
var img_close;
img_del=new Image();
img_del.src=(!isUndefined(CALENDAR_IMAGES_DEL))?(CALENDAR_IMAGES_DEL):(DEFAULT_CALENDAR_IMAGES_DEL);
img_close=new Image();
img_close.src=(!isUndefined(CALENDAR_IMAGES_CLOSE))?(CALENDAR_IMAGES_CLOSE):(DEFAULT_CALENDAR_IMAGES_CLOSE);

minYearList=todayDate.getFullYear()-150;
maxYearList=todayDate.getFullYear()+10;
IsCalendarVisible=false;

var anhoReplegado=true;

img_Date_UP=new Image();
img_Date_UP.src=(!isUndefined(CALENDAR_IMAGES_UP))?(CALENDAR_IMAGES_UP):(DEFAULT_CALENDAR_IMAGES_UP);

img_Date_OVER=new Image();
img_Date_OVER.src=(!isUndefined(CALENDAR_IMAGES_OVER))?(CALENDAR_IMAGES_OVER):(DEFAULT_CALENDAR_IMAGES_OVER);

img_Date_DOWN=new Image();
img_Date_DOWN.src=(!isUndefined(CALENDAR_IMAGES_DOWN))?(CALENDAR_IMAGES_DOWN):(DEFAULT_CALENDAR_IMAGES_DOWN);


function calSwapImg(whatID, NewImg,override) {
    if (document.images) {
     if (!( IsCalendarVisible && override )) {
        document.images[whatID].src = eval(NewImg + ".src");
     }
    }
    window.status=' ';
    return true;
}


function getOffsetLeft (el) {

    var ol = el.offsetLeft;
    while ((el = el.offsetParent) != null)
        ol += el.offsetLeft;
    return ol;
}

function getOffsetTop (el) {
    var ot = el.offsetTop;
    while((el = el.offsetParent) != null)
        ot += el.offsetTop;
    return ot;
}

// Funcion que obtiene la posición del evento que se ejecute, el click de ratón
function posicionY(evento){
    var y = evento.clientY;
    return y;
}

function posicionX(evento){
    var x = evento.clientX;
    return x;
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
        if (document.images['calbtn1']!=null ) document.images['calbtn1'].src=img_del.src;
        if (document.images['calbtn2']!=null ) document.images['calbtn2'].src=img_close.src;
        
        if (hideDropDowns) {toggleDropDowns('hidden');}
        if ((MnDt!=null) && (MnMo!=null) && (MnYr!=null) && (MxDt!=null) && (MxMo!=null) && (MxYr!=null)) {
            IsUsingMinMax = true;
            minDate.setDate(MnDt);
            minDate.setMonth(MnMo-1);
            minDate.setFullYear(MnYr);
            maxDate.setDate(MxDt);
            maxDate.setMonth(MxMo-1);
            maxDate.setFullYear(MxYr);
        }
        else {
            IsUsingMinMax = false;
        }        		
		
        curImg = btnImg;
        curDateBox = dteBox;
	curDateBox2 = dteBox2;
	curDateDiaBox=dteDia;
	curDateMesBox=dteMes;
	curDateAnoBox=dteAno;

        anhoReplegado=true;
        
        if ( ppcIE ) {
        
            //ppcY = posicionY(evento);
            //ppcX = posicionX(evento);
			ppcY = getOffsetTop(document.images[btnImg]);
			ppcX = getOffsetLeft(document.images[btnImg]);
			ppcX += 25;
           
			var docWidth = window.innerWidth;
			var body = document.body, html = document.documentElement;

			var docHeight = Math.max( body.scrollHeight, body.offsetHeight, 
                       html.clientHeight, html.scrollHeight, html.offsetHeight );
			
			if((ppcY + 130) > docHeight){
				ppcY = ppcY - 130;
			}
			
			if((ppcX + 210) > docWidth){
				ppcX = ppcX - 210;
			}
			
			
           
           /* if ((ppcY<1)||(ppcX<1))
            {
               ppcX = document.images[btnImg].x;		
			if(ppcX>800) ppcX = ppcX - 170;  		 
                ppcY = document.images[btnImg].y + document.images[btnImg].height+80;
                
            }else{

                if(ppcX+170>anchoVentana) ppcX = ppcX - 175;
                if(ppcY+170>altoVentana) ppcY = ppcY - 163 - document.images[btnImg].height;
            }*/
        }
        else if (ppcNN){
            ppcX = document.images[btnImg].x;		
			if(ppcX>800) ppcX = ppcX - 170;  		 
            ppcY = document.images[btnImg].y + document.images[btnImg].height;
        }

        domlay('popupcalendar',1,ppcX,ppcY,Calendar(todayDate.getMonth(),todayDate.getFullYear()));       

        //domlay('popupcalendar',1,ppcX,ppcY,Calendar(curDate.getMonth(),curDate.getFullYear()));

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
    calSwapImg(curImg, 'img_Date_UP');    
    IsCalendarVisible = false;
    if (hideDropDowns) {toggleDropDowns('visible');}    
}

function ocultarCalendario() {
	if(IsCalendarVisible) {
		IsCalendarVisible = false;
		domlay('popupcalendar',0,ppcX,ppcY);    		
	}
}

/*
// Oculta el calendario solamente si se ha clicado fuera del mismo
// Si se clica dentro hay que repetir el evento para que funcione el calendario
function ocultarCalendarioOnBlur() {
    if ((posicionX() > ppcX + 190) || (posicionY() > ppcY + 180) || 
        (posicionX() < ppcX) || (posicionY() < ppcY)){        
        hideCalendar();
    } else {        
        event.repeat;
    }
*/


function ocultarCalendarioOnBlur(evento) {
    /**
    evento = (evento) ? evento : ((window.event) ? window.event : "");
    var celda = (evento.target) ? evento.target : evento.srcElement;
    if (IsCalendarVisible)  {
        if ((posicionX(evento) > ppcX + 190) || (posicionY(evento) > ppcY + 180) ||
            (posicionX(evento) < ppcX) || (posicionY(evento) < ppcY)){
            hideCalendar();
        } else {
            //event.repeat;
            evento.repeat;
        }
    }**/
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
    if (lax){
        if (document.layers){document.layers[''+id+''].left = lax}
        else if (document.all){document.all[''+id+''].style.left=lax}
        else if (document.getElementById){document.getElementById(''+id+'').style.left=lax+"px"}
        }
    // Set vertical position
    if (lay){
        if (document.layers){document.layers[''+id+''].top = lay}
        else if (document.all){document.all[''+id+''].style.top=lay}
        else if (document.getElementById){document.getElementById(''+id+'').style.top=lay+"px"}
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
    
    if (ppcNN6) {
        output = "<div style=\"position:relative;clear:both;z-index:3;\">";
        output += '<form name="Cal"><table width="170" border="0" class="cal-Table" cellspacing="0" cellpadding="0"><tr>';
    }
    else {
        output += '<table width="185" class="cal-Table" cellspacing="0" cellpadding="0"><form name="Cal"><tr>';
    }
     
    output += '<td class="cal-HeadCell" valign="middle" align="center" width="100%"><a href="javascript:scrollMonth(-1);" class="cal-DayLink"><img src="'+((!isUndefined(CALENDAR_IMAGES_MENOS))?(CALENDAR_IMAGES_MENOS):(DEFAULT_CALENDAR_IMAGES_MENOS))+'" border="0"></a>&nbsp;<SELECT class="cal-TextBox" NAME="cboMonth" onChange="changeMonth();">';
    for (month=0; month<12; month++) {
        if (month == whatMonth) output += '<OPTION VALUE="' + month + '" SELECTED>' + names[month] + '<\/OPTION>';
        else                output += '<OPTION VALUE="' + month + '">'          + names[month] + '<\/OPTION>';
    }

    output += '<\/SELECT><SELECT class="cal-TextBox" onclick="anhoReplegado=false" NAME="cboYear" onChange="changeYear();">';

    for (year=minYearList; year<maxYearList; year++) {
        if (year == whatYear) output += '<OPTION VALUE="' + year + '" SELECTED>' + year + '<\/OPTION>';
        else              output += '<OPTION VALUE="' + year + '">'          + year + '<\/OPTION>';
    }

    output += '<\/SELECT>&nbsp;<a href="javascript:scrollMonth(1);" class="cal-DayLink"><img src="'+((!isUndefined(CALENDAR_IMAGES_MAS))?(CALENDAR_IMAGES_MAS):(DEFAULT_CALENDAR_IMAGES_MAS))+'" border="0"></a><\/td><\/tr><tr><td width="100%" align="center">';

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
        else
        {
            datecolwidth="14%"
        }
        output += '<td class="cal-HeadCell" width="' + datecolwidth + '" align="center" valign="middle">'+ dow[i] +'<\/td>';
    }
    
    output += '<\/tr><tr>';

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
            output += '<\/tr><tr>';
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
        output += '<\/tr><\/table><\/td><\/tr>';
    }
    else {
        output = output.substr(0,output.length-4); // remove the <tr> from the end if there's no last row
        output += '<\/table><\/td><\/tr>';
    }
    
    /*
    if (ppcNN6) {
        output += '<\/table><\/form>';
    }
    else {
		*/
        output += '<tr class="subtitulo"><td align="center"><input type="button" class="boton" name="anular" value="Anular fecha" onclick="fechaNula();hideCalendar();">&nbsp;&nbsp;<a href="#" onclick="javascript:hideCalendar();" style="text-decoration:none;"><span style="color:red;font-family:Verdana,Arial;font-size:20px"><strong>X</strong></a></span></td></tr><\/form><\/table>';
    //}
		
    if (ppcNN6) {
        output += "</div>";
    }
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
            templink='<td align="center" class="cal-GreyDate">' + linkDay + '<\/td>';
        }
        else {
            if (isDayToday(linkDay)) {
                          if(k==1 || list=='') {
                templink='<td align="center" class="cal-DayCell">' + '<a class="cal-TodayLink" onmouseover="self.status=\' \';return true" href="javascript:changeDay(' + linkDay + ');">' + linkDay + '<\/a>' +'<\/td>';
                          } else {
                                templink='<td align="center" class="cal-DayCell">' + '<a class="cal-TodayLink" onmouseover="self.status=\' \';return true">' + linkDay + '<\/a>' +'<\/td>';                       
                          }
            }
            else {
                          if(k==1 || list=='') {
                             if(domingo) 	
                                templink='<td align="center" class="cal-DayCell">' + '<a class="cal-DayFestivo" onmouseover="self.status=\' \';return true" href="javascript:changeDay(' + linkDay + ');">' + linkDay + '<\/a>' +'<\/td>';
                             else   
                                templink='<td align="center" class="cal-DayCell">' + '<a class="lun-Link" onmouseover="self.status=\' \';return true" href="javascript:changeDay(' + linkDay + ');">' + linkDay + '<\/a>' +'<\/td>';
                          } 
                          else {
                             if(domingo) 	
                                 templink='<td align="center" class="cal-DayCell">' + '<a class="cal-DayFestivo" onmouseover="self.status=\' \';return true;">' + linkDay + '<\/a>' +'<\/td>';
                             else    
                                 templink='<td align="center" class="cal-DayCell">' + '<a class="cal-DayLink" onmouseover="self.status=\' \';return true;">' + linkDay + '<\/a>' +'<\/td>';
                          }                             
            }
        }
    }
    else {
        if (isDayValid(linkDay,linkMonth,linkYear)) {

            if (isGreyDate){
                templink='<td align="center" class="cal-GreyDate">' + linkDay + '<\/td>';
            }
            else {
                if (isDayToday(linkDay)) {
                                  if(k==1 || list=='') {
                    templink='<td align="center" class="cal-DayCell">' + '<a class="cal-TodayLink" onmouseover="self.status=\' \';return true" href="javascript:changeDay(' + linkDay + ');">' + linkDay + '<\/a>' +'<\/td>';
                                  } else {
                                    templink='<td align="center" class="cal-DayCell">' + '<a class="cal-TodayLink" onmouseover="self.status=\' \';return true">' + linkDay + '<\/a>' +'<\/td>';
                                  }                               
                }
                else {
                                  if(k==1 || list=='') {  
                                     if(domingo)
                                        templink='<td align="center" class="cal-DayCell">' + '<a class="cal-DayFestivo" onmouseover="self.status=\' \';return true" href="javascript:changeDay(' + linkDay + ');">' + linkDay + '<\/a>' +'<\/td>';
                                     else
                                        templink='<td align="center" class="cal-DayCell">' + '<a class="lun-Link" onmouseover="self.status=\' \';return true" href="javascript:changeDay(' + linkDay + ');">' + linkDay + '<\/a>' +'<\/td>';
                                  } 
                                  else {
                                     if(domingo)	
                                        templink='<td align="center" class="cal-DayFestivo">' + '<a class="cal-DayLink" onmouseover="self.status=\' \';return true">' + linkDay + '<\/a>' +'<\/td>';
                                     else
                                        templink='<td align="center" class="cal-DayCell">' + '<a class="cal-DayLink" onmouseover="self.status=\' \';return true">' + linkDay + '<\/a>' +'<\/td>'; 
                                  }
                }
            }
        }
        else {
            templink='<td align="center" class="cal-GreyInvalidDate">'+ linkDay + '<\/td>';
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
    
    if (ppcIE) {
        monthCheck = document.forms["Cal"].cboMonth.selectedIndex + amount;
    }
    else if (ppcNN) {
        monthCheck = document.popupcalendar.document.forms["Cal"].cboMonth.selectedIndex + amount;    
    }
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
    
    if (ppcIE) {
        curDate.setMonth(document.forms["Cal"].cboMonth.options[monthCheck].value);
    }
    else if (ppcNN) {
        curDate.setMonth(document.popupcalendar.document.forms["Cal"].cboMonth.options[monthCheck].value );
    }
    domlay('popupcalendar',1,ppcX,ppcY,Calendar(curDate.getMonth(),curDate.getFullYear()));
}

function changeMonth() {

    if (ppcIE) {        
        curDate.setMonth(document.forms["Cal"].cboMonth.options[document.forms["Cal"].cboMonth.selectedIndex].value);
        domlay('popupcalendar',1,ppcX,ppcY,Calendar(curDate.getMonth(),curDate.getFullYear()));
    }
    else if (ppcNN) {

        curDate.setMonth(document.popupcalendar.document.forms["Cal"].cboMonth.options[document.popupcalendar.document.forms["Cal"].cboMonth.selectedIndex].value);
        domlay('popupcalendar',1,ppcX,ppcY,Calendar(curDate.getMonth(),curDate.getFullYear()));
    }

}

function changeYear() {
    if (ppcIE) {

        curDate.setFullYear(document.forms["Cal"].cboYear.options[document.forms["Cal"].cboYear.selectedIndex].value);
        domlay('popupcalendar',1,ppcX,ppcY,Calendar(curDate.getMonth(),curDate.getFullYear()));
    }
    else if (ppcNN) {

        curDate.setFullYear(document.popupcalendar.document.forms["Cal"].cboYear.options[document.popupcalendar.document.forms["Cal"].cboYear.selectedIndex].value);
        domlay('popupcalendar',1,ppcX,ppcY,Calendar(curDate.getMonth(),curDate.getFullYear()));
    }
   

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
var anchoVentana=790;
var altoVentana=590;

function habilitarImagenCal(elementoQueLlama, valor)
{
   if (valor == true)
   {
     document.getElementById(elementoQueLlama).src=(!isUndefined(CALENDAR_IMAGES_ICONO))?(CALENDAR_IMAGES_ICONO):(DEFAULT_CALENDAR_IMAGES_ICONO);
   }
   else
   {
     document.getElementById(elementoQueLlama).src=(!isUndefined(CALENDAR_IMAGES_ICONO2))?(CALENDAR_IMAGES_ICONO2):(DEFAULT_CALENDAR_IMAGES_ICONO2);
   }
}

function deshabilitarImagenCal(elementoQueLlama, valor)
{
   if (valor == true)
   {
     document.getElementById(elementoQueLlama).src=(!isUndefined(CALENDAR_IMAGES_ICONO2))?(CALENDAR_IMAGES_ICONO2):(DEFAULT_CALENDAR_IMAGES_ICONO2);
   }
   else
   {
     document.getElementById(elementoQueLlama).src=(!isUndefined(CALENDAR_IMAGES_ICONO))?(CALENDAR_IMAGES_ICONO):(DEFAULT_CALENDAR_IMAGES_ICONO);
   }
}
