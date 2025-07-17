///////////////////////////////////////////////////////////////////////////////////////////////////////
// INICIO OBJETO COMBOBOX
///////////////////////////////////////////////////////////////////////////////////////////////////////

CB_RowHeight = 19;
CB_Borde = 1;
CB_Scroll = 15;

var cursor;
var operadorConsulta ="";
if(document.all) cursor = 'hand';
else if(document.getElemenById) cursor = 'pointer';

function CB_OcultarCombo(combo){
	var cmb = document.getElementById('desc'+combo);        	
	if(cmb && cmb.combo){ 	
            cmb.combo.ocultar();	
        }else if(window["combo"+combo]!=undefined){	
            window["combo"+combo].ocultar();	
        }
}

function Combo(nombre,idiomaUsuario){
  this.id = nombre;
  this.idioma = 0;	
  if(idiomaUsuario != undefined && idiomaUsuario != null){	
      this.idioma=idiomaUsuario;	
  }
  //Referenciamos los inputs del codigo y la descripcion.  
  
  /* ORIGINAL
  this.cod = document.getElementById("cod"+nombre);
  this.des = document.getElementById("desc"+nombre);
    */


  var codigos           = document.getElementsByName("cod"+nombre);
  var descripciones  = document.getElementsByName("desc"+nombre);
  var anchors          = document.getElementsByName("anchor"+nombre);
  
  if(codigos!=null && codigos[0]!=null)
      this.cod = codigos[0];
  else{
      //this.cod = "";
  this.cod = document.getElementById("cod"+nombre);
  }

  if(descripciones!=null && descripciones[0]!=null)
      this.des = descripciones[0];
  else{
      //this.des = "";
  this.des = document.getElementById("desc"+nombre);
  }

  if(anchors!=null && anchors[0]!=null) {
      this.anchor = anchors[0];
  }else
  this.anchor = document.getElementById("anchor"+nombre);
  /** ORIGINAL
  this.boton = this.anchor.children(0);
    */
  
  var hijos = new Array();    
  hijos = this.anchor.children;
  if(hijos!=null && hijos.length>=1) this.boton = hijos[0];

  this.selectedIndex = -1;
  this.timer = null;
  
  this.des.introducido = "";
  this.original = null;

  this.codigos = new Array();
  this.items = new Array();
  this.auxItems = new Array();
  this.auxCodigos= new Array();
  this.i_codigos = new Array();
  this.i_items = new Array();
  
  //Creamos la vista del combo, que será un DIV que incluirá la tabla con los elementos de la lista.
  this.base = document.createElement("DIV");
  this.base.combo = this;
  this.base.style.position='absolute';
  this.base.style.display = "none";
  this.base.onblur = function (event) {
    var event = (event) ? event : ((window.event) ? window.event : "");
    this.combo.timer = setTimeout('CB_OcultarCombo("'+this.combo.id+'")',150); 
  };
  this.base.onkeydown=function(event){ 
     var event = (event) ? event : ((window.event) ? window.event : "");
     var tecla = "";
     
     if(event.keyCode) tecla = event.keyCode; else tecla = event.which;

    if (tecla == 8) {
        this.combo.buscaItem("-1");
    } else if(tecla==40){
        this.combo.selectItem(this.combo.selectedIndex+1);
    } else if(tecla == 38){
        this.combo.selectItem(this.combo.selectedIndex-1); 
    } else if(tecla==13){
        this.combo.ocultar();
        if(this.combo.cod) this.combo.cod.select();
        else this.combo.des.select();
    }else{
        if (tecla>95) 
            tecla = tecla-48;
        var letra = String.fromCharCode(tecla);
        this.combo.buscaItem(letra);
    }
    if(window.event){
        event.cancelBubble=true;
        event.returnValue=false;
    }else{
        event.stopPropagation();
        event.preventDefault();
    }
    return false;
  };  
    
  this.view = document.createElement("DIV");
  this.base.appendChild(this.view);
  this.view.combo = this;
  this.view.className = 'xC';
  this.view.style.overflowY = 'auto';
  this.view.style.position = 'relative';
  this.view.onselectstart = function () { return false; }
  this.view.ondblclick = function () { return false; }
  this.view.onclick = function (event){
      event = (event) ? event : ((window.event) ? window.event : "");
           
     var padre = "";
     if(window.event)
         padre =event.srcElement;
     else
         padre = event.target;
    
    if(padre.tagName!='DIV') return;
    var rowID =1;

    if(!!navigator.userAgent.match(/Trident.*rv[ :]*11\./) || navigator.appName.indexOf("Internet Explorer")!=-1){
        // Internet Explorer
  		/*
  		var i = window.event.srcElement.parentElement.sourceIndex;
  		var j = window.event.srcElement.sourceIndex;
          */
        // Se calcula la posición de item del combo que ha sido seleccionado
        var i = padre.parentElement.sourceIndex;
  		var j = padre.sourceIndex;
        rowID = (j-i-1);

    }else{
    
        // Firefox u otro navegador

        /** Se obtiene el valor del item de menú seleccionado, para a partir de él, obtener la posición en el combo y seleccionar dicha fila **/        
        var hijos = padre.childNodes;
        var valorFilaSeleccionada = "";
        if(hijos!=null){
            valorFilaSeleccionada = hijos[0].nodeValue;
        }

        var padreRaiz = padre.parentNode;
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
   }// else       
        
  		this.combo.selectItem(rowID);
		this.combo.ocultar();
		if(this.combo.cod) this.combo.cod.select();
		else this.combo.des.select();
  		return false;
  };  
  this.view.onfocus=function(event){ 
        event = (event) ? event : ((window.event) ? window.event : "");
		if(this.combo.timer!=null)clearTimeout(this.combo.timer);
		this.combo.timer=null;
		this.combo.base.focus();
  };

  //*************************************************  
  this.resize = CB_resize;

  this.addItems = CB_addItems;
  this.addItems2= CB_addItems2;
  this.clearItems = CB_clearItems;  
  this.restoreIndex = CB_restoreIndex;
  this.selectItem = CB_selectItem;
  this.buscaCodigo = CB_buscaCodigo;
  this.buscaItem = CB_buscaItem;
  this.scroll = CB_scroll;
  this.display = CB_display;
  this.ocultar = CB_ocultar;
  this.init = CB_init;
  this.activate = CB_activate;
  this.deactivate = CB_deactivate;
  this.obligatorio = CB_obligatorio;
  this.buscaLinea = CB_buscaLinea;
  this.contieneOperadoresConsulta = CB_contieneOperadoresConsulta;
  this.clearSelected = CB_clearSelected;
  this.change = function(){};
      
  this.init();
}

function CB_init(){
  //Guardamos una referencia del combo en el imput de la descripcion
  if(this.cod){
  	this.cod.combo = this;
	this.cod.onfocus=function(){this.select();};
	this.cod.onblur=function(event){  
		if(!this.combo.contieneOperadoresConsulta(this)) this.combo.buscaCodigo(this.value); 
		else{
			var codOld = this.value;
			this.combo.selectItem(-1);
			this.value = codOld;
			this.combo.change();
		}
	};
	this.cod.onkeydown=function(event){ 
        var event = (event) ? event : ((window.event) ? window.event : "");
        var tecla = "";
        if(event.keyCode) tecla = event.keyCode; else tecla = event.which;

		//if(event.keyCode == 40){
        if(tecla == 40){
			this.combo.selectItem(this.combo.selectedIndex+1);
            if(window.event){
			event.cancelBubble=true;
			event.returnValue=false;
            }else{
                event.stopPropagation=true;
                event.preventDefault=false;
            }
		//} else if(event.keyCode == 38){
        } else if(tecla == 38){
			this.combo.selectItem(this.combo.selectedIndex-1); 
            /*
			event.cancelBubble=true;
			event.returnValue=false; */
            if(window.event){
			event.cancelBubble=true;
			event.returnValue=false;
            }else{
                event.stopPropagation=true;
                event.preventDefault=false;
            }

		//} else if(event.keyCode==13) {
        } else if(tecla==13) {
            
			this.combo.display(); 
            /*
			event.cancelBubble=true;
			event.returnValue=false; */
            if(window.event){
			event.cancelBubble=true;
			event.returnValue=false;
            }else{
                event.stopPropagation=true;
                event.preventDefault=false;
            }
		}
		else if(tecla==9)
		{
			this.combo.ocultar(); 
		}
		
	};
  }
  

 /** ORIGINAL
  *this.des.combo = this */
 if(this.des!=null)  this.des.combo = this;

  this.des.onfocus=function(){this.select();};
    this.des.onclick = function(event) {
        this.introducido = "";
        if (this.combo.auxCodigos.length > 0)
            this.combo.addItems(this.combo.auxCodigos, this.combo.auxItems);

        event = (event) ? event : ((window.event) ? window.event : "");

        if (this.combo.cod) {

            if (!this.combo.cod.readOnly) {

                if (!this.combo.contieneOperadoresConsulta(this.combo.cod))
                    this.combo.display();
            }
        } else
        {
            this.combo.display();
        }
        event.stopPropagation();
        return false;
    };
  
  this.des.onkeydown=function(event){ 
      event = (event) ? event : ((window.event) ? window.event : "");
      var tecla = "";
      if(event.keyCode) tecla = event.keyCode; else tecla = event.which;

      if (tecla == 8) {
        this.combo.buscaItem("-1");
        if(window.event){
                event.cancelBubble=true;
                event.returnValue=false;
            }else{
                event.stopPropagation();
                event.preventDefault();
            }
      } else if (tecla == 40){
            this.combo.selectItem(this.combo.selectedIndex+1);
           if(window.event){
                event.cancelBubble=true;
                event.returnValue=false;
            }else{
                event.stopPropagation();
                event.preventDefault();
            }
        } else if(tecla == 38){
            this.combo.selectItem(this.combo.selectedIndex-1); 
            if(window.event){
                event.cancelBubble=true;
                event.returnValue=false;
            }else{
                event.stopPropagation();
                event.preventDefault();
            }
        } else if(tecla==13) {
            this.combo.display(); 
            if(window.event){
                event.cancelBubble=true;
                event.returnValue=false;
            }else{
                event.stopPropagation();
                event.preventDefault();
            }
        }
        else if (tecla==9){
            this.combo.ocultar(); 
        }
  };

  this.des.onkeypress=function(event){ 
      var event = (event) ? event : ((window.event) ? window.event : "");
      var tecla = "";
      if(event.keyCode) tecla = event.keyCode; else tecla = event.which;

  	//	var letra = String.fromCharCode(event.keyCode);
    	var letra = String.fromCharCode(tecla);
  		if(this.readOnly) this.combo.buscaItem(letra);
  };
  
  this.des.onblur=function(event){
    if(!this.readOnly && this.value.length!=0){ 
            if(this.combo.cod){ 
                    if(!this.combo.contieneOperadoresConsulta(this)) this.combo.buscaCodigo(this.combo.cod.value);

                    else{
                            var codOld = this.value;
                            this.combo.selectItem(-1);
                            this.value = codOld;
                            this.combo.change();
                    }
            }//else this.combo.display();
    }
    var isChromium = window.chrome,
            vendorName = window.navigator.vendor,
            isOpera = window.navigator.userAgent.indexOf("OPR") > -1;
      if(isChromium !== null && isChromium !== undefined && vendorName === "Google Inc." && isOpera == false) {
          this.combo.timer = setTimeout('CB_OcultarCombo("'+this.combo.id+'")',150); 
       }else if(navigator.userAgent.indexOf("Firefox") > 0){	
           this.combo.timer = setTimeout('CB_OcultarCombo("'+this.combo.id+'")',150); 	     	
    }

  };
  
  if(this.anchor){ 
  	this.anchor.combo = this;
	this.anchor.onkeydown=function(event){ 

       var event = (event) ? event : ((window.event) ? window.event : "");
       var tecla = "";
       if(event.keyCode) tecla = event.keyCode; else tecla = event.which;

		//if(event.keyCode == 40){
        if(tecla == 40){
			this.combo.selectItem(this.combo.selectedIndex+1);
            /*
			event.cancelBubble=true;
			event.returnValue=false; */
            if(window.event){
			event.cancelBubble=true;
			event.returnValue=false;
            }else{
                event.stopPropagation=true;
                event.preventDefault=false;
            }
		//} else if(event.keyCode == 38){
        } else if(tecla == 38){
			this.combo.selectItem(this.combo.selectedIndex-1); 
            /*
			event.cancelBubble=true;
			event.returnValue=false; */
            if(window.event){
			event.cancelBubble=true;
			event.returnValue=false;
            }else{
                event.stopPropagation=true;
                event.preventDefault=false;
            }
		//} else if(event.keyCode==13) {
        } else if(tecla==13) {
          
			this.combo.display(); 
            /*
			event.cancelBubble=true;
			event.returnValue=false; */
            if(window.event){
			event.cancelBubble=true;
			event.returnValue=false;
            }else{
                event.stopPropagation=true;
                event.preventDefault=false;
            }            
		}
		else if (tecla==9)
		{
			this.combo.ocultar(); 
		}
	};
	this.anchor.onclick=function(event){ 
		if(this.combo.cod){
			if(!this.combo.contieneOperadoresConsulta(this.combo.cod)) this.combo.display(); 
		}else this.combo.display();
                                    event.stopPropagation();
		return false; 
	};
  }
  document.getElementsByClassName("contenidoPantalla")[0].appendChild(this.base);
  this.addItems([],[]);
}

function CB_buscaCodigo(cod){
	if(cod==null || cod == undefined) return true;
	var str = cod;        
	if(str==''){
		this.selectItem(0);
	}else if(this.codigos[this.selectedIndex]!=str){ 
		var i = this.i_codigos[str+''];
		if(i!=null && i!=undefined){
			this.selectItem(i);
		} else { 
		    if(this.des.readOnly) jsp_alerta('A','Código inexistente');
			this.selectItem(-1);			
			return false;
		}
	}
	return true;
}

function CB_buscaLinea(cod){    
	if(cod==null || cod == 'undefined') return true;
	var str = cod;

	if(this.selectedIndex>=0 && this.selectedIndex < this.items.length){
  		if(this.view.children[this.selectedIndex].className!='xCDisabled'){	
                                         this.view.children[this.selectedIndex].className = 'xCSelected';	
                                    }
  	}

	if(str==''){
		this.selectedIndex = 0;
	}else if(this.codigos[this.selectedIndex]!=str){ 
		var i = this.i_codigos[str+''];
		if(i!=null && i!=undefined){
			this.selectedIndex = i;
		} else { 		  
			this.selectedIndex = -1;					
		}
	}
	
  	if(this.selectedIndex >= 0 && this.selectedIndex < this.items.length){
  		if(this.view.children[this.selectedIndex].className!='xCDisabled'){	
                                        this.view.children[this.selectedIndex].className = 'xCSelected';	
                                     }
		this.scroll();		
  		if(this.cod) this.cod.value = this.codigos[this.selectedIndex];
  		this.des.value = this.items[this.selectedIndex];
  	}else{
  		if(this.cod) this.cod.value = "";
  		this.des.value = "";
	}	

	return true;
}

function CB_buscaItem(letra){
    if (letra) {
        if (letra == "-1"){
            if (this.des.introducido.length > 0)
                this.des.introducido = this.des.introducido.substr(0,this.des.introducido.length-1);
            if (this.auxItems.length > 0){
                this.items = this.auxItems;
                this.codigos = this.auxCodigos;
            }
        } else {
            var regex = new RegExp("[a-z]");
            if (regex.test(letra))
                letra = letra.toUpperCase();
            this.des.introducido += letra;
        }
    }
    if (this.des.introducido == ""){
        this.selectItem(0);
        return true;
    }
    this.des.value = this.des.introducido;
    var novoItems = [];
    var novoCodigos = [];
    for(var i=0;i<this.items.length;i++){
        if(this.items[i].toUpperCase().indexOf(this.des.introducido.toUpperCase()) >= 0){
            novoItems.push(this.items[i]);
            novoCodigos.push(this.codigos[i]);
        }
    }
    if (this.auxItems.length == 0) {
        this.auxItems = this.items;
        this.auxCodigos = this.codigos;
    }
    this.addItems(novoCodigos,novoItems);
    
    return true;
}

function CB_display(){
	if(this.base.style.display != ""){
		this.resize();
		this.original = this.selectedIndex;
		this.base.style.display = "";
		if(this.cod) this.buscaCodigo(this.cod.value);
		else {
			for(i=0;i<this.items.length;i++){
				if(this.items[i].toUpperCase()==this.des.value.toUpperCase()){
					this.selectItem(i);
					break;
				}
			}
			if(this.selectedIndex < 0) this.selectItem(0);			
		}
		this.base.focus();
	}else this.base.style.display = "none";
}

function CB_ocultar(){
	if(this.selectedIndex >= this.items.length) this.selectedIndex = -1;
    this.base.style.display = "none";
	if(this.selectedIndex != this.original) this.change();
	this.original = this.selectedIndex;
}

//********************************************************** //
// Calculamos el tamaño y posicion que tendrá el Combo.      //
//***********************************************************//
function CB_resize(){
	
	var alto = 0;	
	var altoElemento = this.des.offsetHeight;
	var altoVentana = document.documentElement.clientHeight;// Para que funcione en IE9 document.body.height devuelve un valor incorrecto
	var altoEncima = getOffsetTop(this.des); //this.des.getBoundingClientRect().top;	
	var altoDebajo = altoVentana-(altoEncima+altoElemento);	
	var altoMayor = (altoDebajo>altoEncima?altoDebajo:altoEncima);	
	var numItems = this.items.length;
	var maxi = ((10*CB_RowHeight)+1)+(2*CB_Borde)+CB_Scroll;
	var maxDiv = (maxi<altoDebajo?maxi:(maxi<altoEncima?maxi:altoMayor));	
	var ctrlMayor = (maxi<altoDebajo?1:(maxi<altoEncima?-1:(altoDebajo>altoEncima?2:-2)));

	if(numItems > 10) numItems = 10;

	for(var i=0;i<numItems;i++){
                        if((alto+CB_RowHeight) < maxDiv) alto += CB_RowHeight;
	}
	if(numItems == 0) alto = CB_RowHeight;
	pX = getOffsetLeft(this.des);	
	pY = (((ctrlMayor==1)||(ctrlMayor==2))?altoEncima+altoElemento:altoEncima-(alto+2*CB_Borde+CB_Scroll));
	if(isTabPage(this.des)){
		pX++;
		pY++;
	}
	
	
	if (typeof (this.base.style.posTop) !== "undefined") //es IE 9
	{
		this.base.style.posLeft   = pX;
		this.base.style.posTop    = pY-document.getElementsByClassName("contenidoPantalla")[0].getBoundingClientRect().top;		
		this.base.style.posHeight = this.view.style.posHeight = (alto+2*CB_Borde+CB_Scroll);
		this.base.style.posWidth  = this.view.style.posWidth  = this.des.offsetWidth+((this.view.scrollHeight==this.view.clientHeight)?0:16);
	}
	else {
		this.base.style.left   = +pX+"px";
		this.base.style.top    = pY-document.getElementsByClassName("contenidoPantalla")[0].getBoundingClientRect().top+"px";	
		this.base.style.height = this.view.style.height = (alto+2*CB_Borde+CB_Scroll)+"px";
		this.base.style.width  = this.view.style.width  = this.des.offsetWidth+((this.view.scrollHeight==this.view.clientHeight)?0:16)+"px";
	
	}
	//this.base.style.top    = pY-document.getElementsByClassName("contenidoPantalla")[0].getBoundingClientRect().top;
	
}

//*******************************//
// Reinicia el item selecionado  //
//*******************************//
function CB_restoreIndex(){
  	this.selectedIndex = -1;
}

function CB_obligatorio(esObligatorio){
	if(esObligatorio){
		if ('inputTextoObligatorio' != this.des.className){				
  			this.codigos.shift();
  			this.items.shift();
			if(this.cod) this.cod.className = 'inputTextoObligatorio';
			this.des.className = 'inputTextoObligatorio';
			if(this.selectedIndex > 0) this.selectedIndex--;			
		}else{
			return;
		}
	}else{
		if ('inputTextoObligatorio' == this.des.className){	
  			this.codigos = [""].concat(this.codigos);
  			this.items   = [""].concat(this.items);
			if(this.cod) this.cod.className = 'inputTexto';
			this.des.className = 'inputTexto';
			if(this.selectedIndex >= 0) this.selectedIndex++;
		}else{
			return;
		}
	}
	var str = '';
  	for(var i=0; i < this.codigos.length; i++){
  		this.i_codigos[this.codigos[i]+''] = i;
		str += '<DIV>'+((this.items[i])?this.items[i]:'&nbsp;')+'</DIV>';
  	}
	this.view.innerHTML = str;
	
  	//this.selectedIndex = -1;
	//if(this.cod) this.cod.value = '';  
	//this.des.value = '';
	this.selectItem(this.selectedIndex);
  	return;
}

function CB_addItems(listaCodigos,listaItems){
  this.codigos = listaCodigos;
  this.items   = listaItems;	
        if (this.des.className.indexOf('inputTextoObligatorio')<0){		
            this.codigos = [""].concat(this.codigos);	
            this.items   = [""].concat(this.items);	
        }else if(this.codigos==null || this.codigos.length==0){	
            this.codigos = [""];	
            this.items   = [""];	
        }	
         var str = '';	
         for(var i=0; i < this.codigos.length; i++){	
             this.i_codigos[this.codigos[i]+''] = i;	
             	
             if (this.items[i]){	
                 var auxItem = (this.items[i]);	
                 if (this.idioma > 1){	
                     if (auxItem.indexOf("|") > -1)	
                         auxItem = auxItem.split("|")[1];	
                  } else if(this.idioma == 1){	
                      auxItem = auxItem.split("|")[0];	
                 }	
                 this.items[i] = auxItem;	
             }	
             str += '<DIV>' + ((this.items[i])?this.items[i]:'&nbsp;') + '</DIV>';	
         }	
         this.view.innerHTML = str;	
         this.selectedIndex = -1;	
         return true;
}


function CB_addItems2(listaCodigos,listaItems,listaEstados){	
    this.codigos = listaCodigos;
    this.items   = listaItems;
    this.estados=listaEstados;
    var estados=listaEstados;	
    if (this.des.className.indexOf('inputTextoObligatorio')<0){				
            this.codigos = [""].concat(this.codigos);	
            this.items   = [""].concat(this.items);	
            this.estados = [""].concat(this.estados);	
    }else if(this.codigos==null || this.codigos.length==0){	
            this.codigos = [""];	
            this.items   = [""];	
            this.estados = [""];	
    }	
    var str = '';	
    for(var i=0; i < this.codigos.length; i++){	
            this.i_codigos[this.codigos[i]+''] = i;
            if (this.items[i]){	
                 var auxItem = (this.items[i]);	
                 if (this.idioma > 1){	
                     if (auxItem.indexOf("|") > -1)	
                         auxItem = auxItem.split("|")[1];	
                  } else if(this.idioma == 1){	
                      auxItem = auxItem.split("|")[0];	
                 }	
                 this.items[i] = auxItem;	
             }
            var est=estados[i];	
            if(this.estados[i]!="B"){	
            str += '<DIV>'+((this.items[i])?this.items[i]:'&nbsp;')+'</DIV>';	
            }else{	
                str += '<DIV  class="xCDisabled">'+((this.items[i])?this.items[i]:'&nbsp;')+'</DIV>';	
            }	
    }	
    this.view.innerHTML = str;	
    this.selectedIndex = -1;	
    return true;	
}

function CB_clearItems(){
  	this.addItems([""],[""]);
	if(this.cod) this.cod.value = '';
	this.des.value = '';
}

function CB_selectItem(rowID) {
    arglen = arguments.length;
    var old = this.selectedIndex;
    var index = (arglen != 0) ? rowID : this.selectedIndex;
    if (this.selectedIndex >= 0 && this.selectedIndex < this.items.length) {
        if (this.view.children[this.selectedIndex].className != 'xCDisabled') {
            this.view.children[this.selectedIndex].className = 'xC';
            /** ORIGINAL
             this.view.children(this.selectedIndex).className = 'xC';	
             */
        } else {
            var disabled = this.selectedIndex;
        }
    }
    if (index >= 0 && index < this.items.length && this.view.children[index].className != 'xCDisabled') {
        //this.view.children(index).className = 'xCSelected';
        this.view.children[index].className = 'xCSelected';
        this.selectedIndex = index;
        this.scroll();
        if (this.cod)
            this.cod.value = this.codigos[index];
        this.des.value = this.items[index];
    } else if (index >= 0 && this.view.children[index].className == 'xCDisabled') {
        if (old > 0) {
            this.selectedIndex = old;
            this.scroll();
            if (this.cod)
                this.cod.value = this.codigos[old];
            this.des.value = this.items[old];
        } else {
            this.selectedIndex = -1;
            if (this.cod)
                this.cod.value = "";
            this.des.value = "";
        }
        if (this.view.children[this.selectedIndex].className != 'xCDisabled') {
            this.view.children[this.selectedIndex].className = 'xCSelected';
        }
    } else {
        if (index < 0) {
            this.selectedIndex = -1;
        } else if (index >= this.items.length)
            this.selectedIndex = this.items.length;
        if (this.cod)
            this.cod.value = "";
        this.des.value = "";
    }
    if (this.selectedIndex != old && this.base.style.display != '' && this.selectedIndex != disabled)
        this.change();
}

function CB_scroll(){

/** ORIGINAL
	var selRow = this.view.children(this.selectedIndex);	
  */
    var selRow = this.view.children[this.selectedIndex];    
  	var selDiv = this.view;	
	
  	if(selRow.offsetTop < selDiv.scrollTop)
    	selDiv.scrollTop = selRow.offsetTop;
  	else if(selRow.offsetTop > (selDiv.scrollTop+selDiv.clientHeight-selRow.offsetHeight))		
      	selDiv.scrollTop = (selRow.offsetTop-selDiv.clientHeight+selRow.offsetHeight);    	  	
}

function CB_activate(){	
    var clase = new Array();
    if(this.cod){
            clase = this.cod.className.split(" ");
            if(clase[clase.length-1] == "inputTextoDeshabilitado"){
                    this.cod.disabled = false;
                    CB_removeClass(this.cod);
            }
    }

    clase = this.des.className.split(" ");
    if(clase[clase.length-1] == "inputTextoDeshabilitado"){
            this.des.disabled = false;
            CB_removeClass(this.des);
    }

    if(this.anchor){ 
            this.anchor.disabled = false;
            this.anchor.onclick = function(){ this.combo.display();return false; };	
    }

    if(this.boton){
                this.boton.style.cursor = 'hand';
                this.boton.className = this.boton.className.replace(new RegExp('(?:^|\\s)'+ 'faDeshabilitado' + '(?:\\s|$)'),"");
    }
}

function CB_deactivate(){
    var clase = new Array();
    if(this.cod){
            clase = this.cod.className.split(" ");
            if(clase[clase.length-1] != "inputTextoDeshabilitado"){
                    this.cod.disabled = true;
                    this.cod.className += " inputTextoDeshabilitado";
            }
    }

    clase = this.des.className.split(" ");
    if(clase[clase.length-1] != "inputTextoDeshabilitado"){
            this.des.disabled = true;
            this.des.className += " inputTextoDeshabilitado";
    }

    if(this.anchor){		
            this.anchor.disabled = true;
            this.anchor.onclick = function(){ return false; };
    }

    if(this.boton){  
        this.boton.style.cursor = 'default';
        if (this.boton.className.indexOf("faDeshabilitado") < 0)
            this.boton.className += " faDeshabilitado";
    }
}

function CB_removeClass(ele){
	var clase = ele.className.split(" ");
    if (clase.length > 1){
		ele.className = "";
		for(i=0;i<clase.length-1;i++){
			if(i==0) ele.className += clase[i];
			else ele.className += " " + clase[i];			
		}		
    }
}

function CB_contieneOperadoresConsulta(campo){
	var contiene=false;
    var v = campo.value;
    for (i = 0; i < v.length; i++){   
		var c = v.charAt(i);
        if (operadorConsulta.indexOf(c) != -1) contiene=true;
	}
    return contiene;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////
// FIN OBJETO COMBO
///////////////////////////////////////////////////////////////////////////////////////////////////////
function getOffsetLeft(el) {
	var ol = el.offsetLeft;
    while ((el = el.offsetParent) != null)
        ol += el.offsetLeft;
    return ol;
}

function getOffsetTop(el) {
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

function CB_clearSelected(){
    this.buscaLinea(-1);
    
    if (this.items) {
        for (var i = 0; i < this.items.length; i++) {
            this.view.children[i].className = '';
        }
    }
    
    return true;
}
