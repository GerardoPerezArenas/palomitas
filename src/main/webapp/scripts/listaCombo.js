///////////////////////////////////////////////////////////////////////////////////////////////////////
// INICIO OBJETO COMBO
///////////////////////////////////////////////////////////////////////////////////////////////////////

CB_RowHeight = 19;
CB_Borde = 1;
var CB_Num = 0;

function CB_addElement(lista,elemento) {
  var i = lista.length;
  lista[i] = elemento;
}

function CB_deleteElement(lista,index){
  if(index<0 || index >= lista.length) return null;
  var val = lista[index];
  var i, j;
  for (i=eval(index);i < (lista.length-1);i++){
    j = i + 1;
    lista[i] = lista[j];
  }
  lista.length--;
  return val;
}


function Combo(par,anchoTabla){
  this.id = 'cb'+(CB_Num++);
  this.parent = par;
  this.parent.combo = this;

  this.multiple = false;   //Si se permite seleccionar más de una fila.
  this.ultimoTable = false;
  this.focusedIndex = -1;
  this.selectedIndex = -1;
  this.anchoTabla = anchoTabla;

  this.columnas = new Array();
  this.lineas = new Array();
  
  this.widthCombo = CB_widthCombo;
  this.addColumna = CB_addColumna;
  this.modifyColumna = CB_modifyColumna;
  this.addLinea = CB_addLinea;
  this.getLinea = CB_getLinea;
  this.setLinea = CB_setLinea;
  this.removeLinea = CB_removeLinea;
  this.selectLinea = CB_selectLinea;
  this.restoreIndex = CB_restoreIndex;
  this.cambiaRow = CB_cambiaRow;
  this.clearCombo = CB_clearCombo;

  this.displayTabla = CB_displayTabla;
  this.displayDatos = function(datos){};
}

function CB_widthCombo(){
  var anchoTotal = this.anchoTabla;
  if(!anchoTotal){	
    for(var i=0;i<this.columnas.length;i++){
      anchoTotal += Number(this.columnas[i][0]);
    }
    this.anchoTabla = anchoTotal;
  }
  return anchoTotal-5;
}

function CB_restoreIndex(){
  if(this.multiple){
	var indices = new Array();
	  for(var i=0; i < this.lineas.length; i++){
	    indices[i] = false;
	  }	  
	  this.selectedIndex = indices;
  }else{
	this.selectedIndex = -1;
  }
  this.focusedIndex = -1;
}

function CB_addColumna(ancho, align){
  var column = new Array();
  column[0] = ancho;
  column[1] = (align || 'left');
  this.columnas[this.columnas.length] = column;
}

function CB_modifyColumna(indice, ancho, align){
  var columnNew = new Array();
  columnNew[0] = ancho;
  columnNew[1] = (align || 'left');
  this.columnas[indice] = columnNew;
}

function CB_addLinea(datos){
  if(datos.length != this.columnas.length) return false;
  CB_addElement(this.lineas,datos);
  this.focusedIndex = -1;
  this.selectedIndex = -1;
  this.displayTabla();
  return true;
}

function CB_getLinea(rowID){
  if (CB_getLinea.arguments.length == 0) rowID = this.selectedIndex;
  if (rowID < 0 || rowID >= this.lineas.length) return false;
  return this.lineas[rowID];
}

function CB_setLinea(datos,rowID){
  if (datos.length != this.columnas.length) return false;
  if (CB_setLinea.arguments.length == 1) rowID = this.selectedIndex;
  if (rowID < 0 || rowID >= this.lineas.length) return false;
  this.lineas[rowID] = datos;
  this.displayTabla();
  return true;
}

function CB_removeLinea(rowID){
  var index = (rowID || this.selectedIndex);
  if (index < 0 || index >= this.lineas.length) return false;
  CB_deleteElement(this.lineas,index);
  this.focusedIndex = -1;
  this.selectedIndex = -1;
  this.displayTabla();
  return true;
}

function CB_clearCombo(){
  this.lineas = new Array();
  this.displayTabla();
  return true;
}

function CB_displayTabla(){
  var fechaini = new Date();
  this.restoreIndex();  
  CB_Num += 1;
  str = '<table id="'+this.id+'" name="'+this.id+'" style="width:100%; height:100%" cellspacing="0" cellpadding="0" onselectstart="return false;" class="xC">';
  str += '<tr><td id="c'+this.id+'" onclick="sCR(event)">';
  for(var i=0; i < this.lineas.length; i++){
    var lin = this.lineas[i];	
    str += '<div>'
                    +(lin[0]==''?'&nbsp;':lin[0])+
              '</div>';
  }
  str += '</td></tr>';
  str += '</table>';
  this.parent.innerHTML = str;
}

function CB_selectLinea(rowID){	
  if(!this.multiple && this.selectedIndex >= 0) {
    this.cambiaRow(this.selectedIndex,'xCSelected');
  }
  
  var isSelected = false;
  if(this.multiple){
    isSelected = (this.selectedIndex[rowID]);
  }else{
 	isSelected = (this.selectedIndex == rowID);
  }

  if(isSelected) {
	this.cambiaRow(rowID,'xC');
	if(this.multiple){        
       this.selectedIndex[rowID] = false;
    }
	else{
		this.focusedIndex = -1;
		this.selectedIndex = -1;
	}
    
	var datos = new Array();
	for (i=0; i<this.columnas.length;i++){
      datos[i] = '';
	}
    
	this.displayDatos(datos);
  }else{
	this.cambiaRow(rowID,'xCSelected');
	if(this.multiple) this.selectedIndex[rowID] = true;
	else{
		this.selectedIndex = rowID;
		this.focusedIndex = rowID;
	}
	this.displayDatos(this.lineas[rowID]);
  }	 
}

function CB_cambiaRow(rowID,clase){
	var selRow = null; 
	var selCell = null; 

    selRow = getRow(this.id,rowID);
    selRow.className = clase;
}

function sCR(evento){
  var objetivo;
  if(window.event) evento = window.event;
  
  if(window.event)
      objetivo = evento.srcElement;
  else
      objetivo = evento.target;

  var rowID;var tableName;
  if(navigator.appName.indexOf("Internet Explorer")!=-1){
        if(objetivo.tagName!='DIV') return;
        var i = objetivo.parentElement.sourceIndex;
        var j = objetivo.sourceIndex;
        rowID = (j-i-1);
        tableName = objetivo.parentElement.id.substr(1);        
  }else{

        tableName = objetivo.parentNode.id.substr(1);

         /** Se obtiene el valor del item de menú seleccionado, para a partir de él, obtener la posición en el combo y seleccionar dicha fila **/
        var hijos = objetivo.childNodes;
        var valorFilaSeleccionada = "";
        if(hijos!=null){
            valorFilaSeleccionada = hijos[0].nodeValue;
        }

        var padreRaiz = objetivo.parentNode;
        var hijosRaiz = padreRaiz.childNodes;
        for(z=0;z<hijosRaiz.length;z++){
            var nietos = hijosRaiz[z].childNodes;
            if(nietos!=null && nietos.length>0){
                if(nietos[0].nodeValue == valorFilaSeleccionada){
                    break;
                }
            }
        }
        // En z está la posición de la fila seleccionada por el usuario
        rowID = z;
  }

    with(eval('document.all.'+tableName+'.parentElement')){
      combo.selectLinea(rowID);
  }

  callComboClick(rowID,tableName);
  return false;
}

function callComboClick(rowID,idTableName){ 
	window.focus(); //Evita el scroll del DIV para scrollControlCombo.
	
    var tableObject;
    if(navigator.appName.indexOf("Internet Explorer")!=-1){
        tableObject = eval('document.all.'+idTableName+'.parentElement.combo');
    }else
        tableObject = eval('document.all.'+idTableName+'.parentNode.combo');
    
	tableObject.ultimoTable = false;
    
	rellenarDatos(tableObject, rowID); //No es general. 

    
     ocultarDiv();  
    
}

function getRow(tableName,rowID){
 	var tab = eval("document.all.c"+tableName);
    /* ORIGINAL
     * return tab.childNodes(rowID);
     */
	return tab.childNodes[rowID];
}
///////////////////////////////////////////////////////////////////////////////////////////////////////
// FIN OBJETO COMBO
///////////////////////////////////////////////////////////////////////////////////////////////////////

var tabDiv;
var layerVisible = false;
var ultimo = false;
var listaCod = new Array();
var listaDesc = new Array();
var listaDescDiv = new Array();
/*var auxDescDiv = new Array();*/
var codDiv = "codigo";
var descDiv = "descripcion";
var codOld = "";
var descOld = "";
var divSegundoPlano = false;
var indexSegundoPlano = -1;
var pX;
var pY;
var valorBusqueda = -1;

///////////////////////////////////////////////////////////////////////////////////////////////////////
var contedorListaCombo = document.getElementById('comboDesplegable');
if (contedorListaCombo == null) {
    contedorListaCombo = document.createElement("div");
    contedorListaCombo.id = 'comboDesplegable';
    contedorListaCombo.className = 'comboDesplegable';
    contedorListaCombo.style.display = 'none';
    document.body.getElementsByClassName("contenidoPantalla")[0].appendChild(contedorListaCombo);
}

tabDiv = new Combo(contedorListaCombo,100);
tabDiv.addColumna('100');
tabDiv.className ="comboDesplegable";

///////////////////////////////////////////////////////////////////////////////////////////////////////

function inicializarValores(campoCod, campoDesc, arrayCod, arrayDesc){	
    
    var evalCod =eval("document.all." + campoCod);
    if(!evalCod.disabled){
	if(divSegundoPlano){
		cargaTablaEnDiv(campoCod, campoDesc, arrayCod, arrayDesc);				
	}else{
		if(!layerVisible){
			cargaTablaEnDiv(campoCod, campoDesc, arrayCod, arrayDesc);						
		}else{
			ocultarDiv();  
		}
	}
    }	
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

function actualizarValDiv(cDiv,dDiv){
	codDiv = cDiv;		
	descDiv = dDiv;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

function getOffsetLeft1(el) {
    var ol = el.offsetLeft;
    while ((el = el.offsetParent) != null)
        ol += el.offsetLeft;
    return ol;
}

function getOffsetTop1(el) {
    var ot = el.offsetTop;
    while((el = el.offsetParent) != null)
        ot += el.offsetTop;
    return ot;
}

function isTabPage(el) {    
	var pane = false;
    while((el = el.parentElement) != null){
        if (el.className == 'tab-page') pane = true;
	}
    return pane;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////  

function configDiv(arrayDesc,ele){ 	
	var alto = 0;	
	var altoElemento = ele.offsetHeight;
	var altoVentana = document.body.clientHeight;// Para que funcione en IE9 document.body.height devuelve un valor incorrecto
	var altoEncima = getOffsetTop1(ele);
	var altoDebajo = altoVentana-(altoEncima+altoElemento);
	var altoMayor = (altoDebajo>altoEncima?altoDebajo:altoEncima);
	var numLineas = arrayDesc.length;
	var max = ((10*CB_RowHeight)+1)+(2*CB_Borde);
	var maxDiv = (max<altoDebajo?max:(max<altoEncima?max:altoMayor));	
	var ctrlMayor = (max<altoDebajo?1:(max<altoEncima?-1:(altoDebajo>altoEncima?2:-2)));

	if ('inputTextoObligatorio' != ele.className) numLineas++;
	if ('inputTextoObligatorio' == ele.className && numLineas==0) numLineas++;
	if(numLineas > 10) numLineas = 10;

	for(var i=0;i<numLineas;i++){
                      if((alto+CB_RowHeight) < maxDiv) alto += CB_RowHeight;
	}

	pX = getOffsetLeft1(ele);	
	pY = (((ctrlMayor==1)||(ctrlMayor==2))?altoEncima+altoElemento:altoEncima-(alto+2*CB_Borde));
	if(isTabPage(ele)){
		pX++;
		pY++;
	}	
	corregirPosDiv();

	return (alto+2*CB_Borde);		
}

function corregirPosDiv(){}

///////////////////////////////////////////////////////////////////////////////////////////////////////

function arrayToString(objArray){
  	var str = new String("");
  	for(var i = 0;i<objArray.length;i++){
    	str = str + objArray[i];
  	}
  	return str;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

function borrarDatosDiv(cod,des){
	var evalCod =eval("document.all." + cod);
  	var evalDesc =eval("document.all." + des);	
	if(evalCod.value.length == 0) evalDesc.value = "";	
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

function camposDistintosDiv(cod,des){
	var evalCod =eval("document.all." + cod);
  	var evalDesc =eval("document.all." + des);
	if((cod==codDiv)&&(des==descDiv)){
		if((codOld == evalCod.value)&&(descOld == evalDesc.value)){
			return false;
		}else{
			return true;
		}
	}
}
///////////////////////////////////////////////////////////////////////////////////////////////////////

function pintaDatosDiv(datos){	
    // Carga los inputs con los valores de la fila seleccionada    
    var evalCod =eval("document.all." + codDiv);
    var evalDesc =eval("document.all." + descDiv);	
	if(tabDiv.selectedIndex != -1){		
		evalCod.value = listaCod[tabDiv.selectedIndex];
		evalDesc.value = listaDesc[tabDiv.selectedIndex];					
	}    
 }

 tabDiv.displayDatos = pintaDatosDiv;

///////////////////////////////////////////////////////////////////////////////////////////////////////

function refresh(capa){
	if(capa =="tabDiv")  tabDiv.displayTabla();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

function ocultarDiv(){
    var evalCod =eval("document.all." + codDiv);
    if (typeof evalCod != 'undefined'){
        document.all.comboDesplegable.style.display = "none";
        /*auxDescDiv = new Array();	*/
        indexSegundoPlano = tabDiv.selectedIndex;	
        layerVisible = false;
        divSegundoPlano = true;
        evalCod.focus();					
    }
}

function ocultarDivNoFocus(){
    var evalCod =eval("document.all." + codDiv);
    document.all.comboDesplegable.style.display = "none";
    /*auxDescDiv = new Array();	*/
    indexSegundoPlano = tabDiv.selectedIndex;	
    layerVisible = false;
    divSegundoPlano = true;				
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

function cargaTablaEnDiv(campoCod, campoDesc, arrayCod, arrayDesc){	
	var vacio = new Array();
	valorBusqueda = -1;
	codDiv = campoCod;
	descDiv = campoDesc;  	
	
	listaCod = new Array();
	listaDesc = new Array();
	listaDescDiv = new Array();
	var evalCod =eval("document.all." + campoCod);   		
	var evalDesc =eval("document.all." + campoDesc);		
	codOld = evalCod.value;
	descOld = evalDesc.value;

	if (arrayDesc.length != 0){				
		if ('inputTextoObligatorio' != evalDesc.className){								
			listaCod[0] = "";			
			listaDesc[0] = "";
			
			vacio[0] = "";
			listaDescDiv[0] = vacio;
		}		
		for(var i=0;i<arrayDesc.length;i++){						
			listaCod[listaCod.length] = arrayCod[i];
			listaDesc[listaDesc.length] = arrayDesc[i];

			var elemento = new Array();
    		elemento[0] = arrayDesc[i];
    		listaDescDiv[listaDescDiv.length] = elemento;
		}		
	}else{
		listaCod[0] = "";			
		listaDesc[0] = "";

		vacio[0] = "";
		listaDescDiv[0] = vacio;
	}

	if(!divSegundoPlano){
		var ancho = evalDesc.offsetWidth;

		tabDiv.anchoTabla = ancho;   
		tabDiv.modifyColumna(0,ancho);

		tabDiv.lineas = listaDescDiv;				
		refresh("tabDiv");		
		
		var alto = configDiv(arrayDesc, evalDesc);
                                    
            
		//Dimensiones capa
                                  
                                    if (typeof (document.all.comboDesplegable.style.posTop) != "undefined") {
                                            document.all.comboDesplegable.style.posWidth = ancho;
                                            document.all.comboDesplegable.style.posHeight = alto;
                                    }
                                    
                                   document.all.comboDesplegable.style.width = ancho+"px";
		document.all.comboDesplegable.style.height = alto+"px";


		//Posicionamos capa					
		//BCS compatibilidad IE9
		
		document.all.comboDesplegable.style.position = "absolute";
                
		if (document.all.comboDesplegable.style.posLeft !== "undefined") document.all.comboDesplegable.style.posLeft = pX;
		document.all.comboDesplegable.style.left = pX+"px";

		
                                    //if (document.all.comboDesplegable.style.posTop !== "undefined") document.all.comboDesplegable.style.posTop = pY;
		//document.all.comboDesplegable.style.top = pY+"px";
		if (document.all.comboDesplegable.style.posTop !== "undefined") document.all.comboDesplegable.style.posTop = pY - document.getElementsByClassName("contenidoPantalla")[0].getBoundingClientRect().top;
		document.all.comboDesplegable.style.top = (pY - document.getElementsByClassName("contenidoPantalla")[0].getBoundingClientRect().top)+"px";
		             
			
		document.all.comboDesplegable.style.display = "";
		layerVisible = true;
	}
	selecCodDes(evalCod.value,0);
		
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

function selecCodDes(str,num){	    
   	if(str){
        var evalCod =eval("document.all." + codDiv);
        var evalDesc =eval("document.all." + descDiv);
            var indice = 0;
            var x = valorBusqueda+1;               
            while(true){	
                var auxLis = "";
                if(num == 0){				
                    auxLis = listaCod[x];
                         
                }else{
                    auxLis = new String(listaDesc[x]);
                    auxLis = auxLis.substring(0,str.length);   
                }
                        
                if(auxLis == str){				
                    if(!divSegundoPlano){	
                        if(tabDiv.selectedIndex!=-1) tabDiv.cambiaRow(tabDiv.selectedIndex,TB_Fondo,TB_Fuente);
                        tabDiv.selectedIndex = x;
                        tabDiv.cambiaRow(x,'xCSelected');
                        scrollControl();	
                    }else{ 
                        indexSegundoPlano = x;					
                    }
				  
                    valorBusqueda = x;

                    evalCod.value = listaCod[x];
                    evalDesc.value = listaDesc[x];	
                    break;
                }else{			
                    indice++;				
                    if(indice==listaCod.length+1) break;				
                }		
                if(x!=listaCod.length) x++;
                else x=0;				
            }		
            if((num == 0)&&(indice == listaCod.length+1)){			
                jsp_alerta('A','Código inexistente');
                evalCod.value = "";
                evalDesc.value = "";			
            }		
    }else{		
        selecPrimeraFila();	
    }				

}

///////////////////////////////////////////////////////////////////////////////////////////////////////

function selecPrimeraFila(){	
	var evalCod =eval("document.all." + codDiv);
	var evalDesc =eval("document.all." + descDiv);
	if(!divSegundoPlano){						
    	tabDiv.selectedIndex = 0;						
	    document.all.comboDesplegable.scrollTop = 0;		
		tabDiv.cambiaRow(0,'xCSelected');
	}else{
		indexSegundoPlano = 0;																		
	}			
	evalCod.value = listaCod[0];
	evalDesc.value = listaDesc[0];
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

function upDown(event){    
	var evalCod =eval("document.all." + codDiv);
    var evalDesc =eval("document.all." + descDiv);	
        var tecla = "";
    if(event.keyCode)
        tecla = event.keyCode;
    else
        tecla = event.which;

	if(!divSegundoPlano){		
		if(tabDiv.selectedIndex>-1) tabDiv.cambiaRow(tabDiv.selectedIndex,'xC');
	}
  	if(tecla == 40){
   		if(!divSegundoPlano){
			if(tabDiv.selectedIndex != listaDescDiv.length-1){
		   		if(tabDiv.selectedIndex < listaDescDiv.length-1) tabDiv.selectedIndex++;
			}else{
    			ultimo = true;
    		}	
		}else{
			if(indexSegundoPlano < listaDescDiv.length) indexSegundoPlano++;				
		}    	
  	}
  	if(tecla == 38){
   		if(!divSegundoPlano){
			if(!ultimo){
		   		if(tabDiv.selectedIndex > -1) tabDiv.selectedIndex--;
			}else{
    			ultimo = false;
	   		}	
		}else{
			if(indexSegundoPlano > -1) indexSegundoPlano--;					
		}    	
  	}
	if(!divSegundoPlano){
		if(!ultimo){		
			if(tabDiv.selectedIndex == -1){				
				evalCod.value = "";
       			evalDesc.value = "";				
			}else{	    						
				tabDiv.cambiaRow(tabDiv.selectedIndex,'xCSelected');
       			evalCod.value = listaCod[tabDiv.selectedIndex];
       			evalDesc.value = listaDesc[tabDiv.selectedIndex];	
				valorBusqueda = tabDiv.selectedIndex;
	   			scrollControl();
				rellenarDatos(tabDiv, tabDiv.selectedIndex);
			}			
		}else{			
			evalCod.value = "";
	    	evalDesc.value = "";			
		}		
	}else{
		if((indexSegundoPlano == -1)||(indexSegundoPlano == listaDescDiv.length)){			
			evalCod.value = "";
   			evalDesc.value = "";						
		}else{							
			evalCod.value = listaCod[indexSegundoPlano];
    		evalDesc.value = listaDesc[indexSegundoPlano];
			valorBusqueda = indexSegundoPlano;
			rellenarDatos(tabDiv, indexSegundoPlano);
		}		
	}  			
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

function scrollControl(){
	var selRow = getRow(tabDiv.id,tabDiv.selectedIndex);	
  	var selDiv = document.all.comboDesplegable;

  	if(selRow.offsetTop < selDiv.scrollTop){
    	selDiv.scrollTop = selRow.offsetTop;
  	}else{
    	if(selRow.offsetTop >= (selDiv.scrollTop+selDiv.clientHeight-selRow.clientHeight)){
      		selDiv.scrollTop = (selRow.offsetTop-selDiv.clientHeight+selRow.clientHeight);
    	}
  	}
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

function buscar(event){ 
    var evalCod =eval("document.all." + codDiv);
    var evalDesc =eval("document.all." + descDiv);		
    if (typeof evalCod != 'undefined'){
        if((document.activeElement.name==evalCod.name)||(document.activeElement.name==evalDesc.name)){
        var auxDescDivStr = "";
        var tecla = "";
        if(event.keyCode)
            tecla = event.keyCode;
        else
            tecla = event.which;

        if((tecla != 40)&&(tecla != 38)){
            if(tecla != 13){
                    if(tecla == 8){
                                    auxDescDivStr = "";
                    }else{
                            var realkey = String.fromCharCode(tecla);
                                    auxDescDivStr = realkey;
                    }     	
                    selecCodDes(auxDescDivStr);
            }else{
                if(!divSegundoPlano){
                    if((tabDiv.selectedIndex>-1)&&(tabDiv.selectedIndex < listaDescDiv.length)&&(!ultimo)){
                            evalCod.value = listaCod[tabDiv.selectedIndex];
                            evalDesc.value = listaDesc[tabDiv.selectedIndex];
                            auxDes = listaDesc[tabDiv.selectedIndex];                    
                            ocultarDiv();
                        }
                    }
                }
            }
        }
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

function rellenarDatos(tableName,rowID){}	//Se ejecuta al cambiar la selección en un Combo. 

///////////////////////////////////////////////////////////////////////////////////////////////////////