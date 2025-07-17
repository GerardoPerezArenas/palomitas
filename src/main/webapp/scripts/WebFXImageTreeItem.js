/* 
 *	Subclase que añade una imagen con una acción especificada, a la derecha de 
 *  la etiqueta del nodo.
 * 
 *  Basado en el componenente webfxcheckboxtreeitem creado por Erik Arvidsson.
 *
 *
 *	* Created by Erik Arvidsson (http://webfx.eae.net/contact.html#erik)
 *
 *	* Disclaimer:	This is not any official WebFX component. It was created due to
 *	*			demand and is just a quick and dirty implementation. If you are
 *	*			interested in this functionality the contact us
 *	*			http://webfx.eae.net/contact.html
 *
 *	 Es necesario añadir una clase ccs para dicha imagen.
 *	 Ejemplo:
 *	
 *	input.tree-image {
 *			width: 22px;
 *      height: 22px;
 *	}
 *
 */

function WebFXImageTreeItem(sText, sAction, eParent, sIcon, sOpenIcon, sEstilo, sSrcImage, sActionImagen) {  
  this._srcImage =(sSrcImage)?sSrcImage:webFXTreeConfig.srcImage;
  this._action = (sActionImagen)?sActionImagen:"";
	this.base = WebFXTreeItem;
	this.base(sText, sAction, eParent, sIcon, sOpenIcon, sEstilo);
  
}

WebFXImageTreeItem.prototype = new WebFXTreeItem;

WebFXImageTreeItem.prototype.desHabilitarImagenes = function(b) { 
	document.getElementById(this.id + '-plus').disabled = b;
  document.getElementById(this.id + '-icon').disabled = b;  
  document.getElementById(this.id + '-imagen').disabled = b;
  if (b) {
    document.getElementById(this.id + '-imagen').style.cursor = 'default';
  } else {
    document.getElementById(this.id + '-imagen').style.cursor = 'hand';
  }
  document.getElementById(this.id + '-plus').style.cursor = 'default';
  document.getElementById(this.id + '-icon').style.cursor = 'default';
}


WebFXImageTreeItem.prototype.toString = function (nItem, nItemCount) {
	var foo = this.parentNode;
	var indent = '';
	if (nItem + 1 == nItemCount) { this.parentNode._last = true; }
	var i = 0;
	while (foo.parentNode) {
		foo = foo.parentNode;
		indent = "<img id=\"" + this.id + "-indent-" + i + "\" src=\"" + ((foo._last)?webFXTreeConfig.blankIcon:webFXTreeConfig.iIcon) + "\">" + indent;
		i++;
	}
	this._level = i;
	if (this.childNodes.length) { this.folder = 1; }
	else { this.open = false; }
	if ((this.folder) || (webFXTreeHandler.behavior != 'classic')) {
		if (!this.icon) { this.icon = webFXTreeConfig.folderIcon; }
		if (!this.openIcon) { this.openIcon = webFXTreeConfig.openFolderIcon; }
	}
	else if (!this.icon) { this.icon = webFXTreeConfig.fileIcon; }
	var label = this.text.replace(/</g, '&lt;').replace(/>/g, '&gt;');
  if (!this.estilo) { this.estilo = webFXTreeConfig.defaultStyle;}
	var str = "<div id=\"" + this.id + "\" ondblclick=\"webFXTreeHandler.toggle(this);\" class=\""+this.estilo+"\" onkeydown=\"return webFXTreeHandler.keydown(this, event)\">";
	str += indent;
	str += "<img id=\"" + this.id + "-plus\" src=\"" + ((this.folder)?((this.open)?((this.parentNode._last)?webFXTreeConfig.lMinusIcon:webFXTreeConfig.tMinusIcon):((this.parentNode._last)?webFXTreeConfig.lPlusIcon:webFXTreeConfig.tPlusIcon)):((this.parentNode._last)?webFXTreeConfig.lIcon:webFXTreeConfig.tIcon)) + "\" onclick=\"webFXTreeHandler.toggle(this);\">"
		
	str += "<span id=\"" + this.id + "-icon\" class=\"webfx-tree-icon " + ((webFXTreeHandler.behavior == 'classic' && this.open)?this.openIcon:this.icon) + "\" onclick=\"webFXTreeHandler.select(this);\"></span><a href=\"" + this.action + "\" id=\"" + this.id + "-anchor\" onfocus=\"webFXTreeHandler.focus(this);\" onblur=\"webFXTreeHandler.blur(this);\">" + label + "</a>";

  // insertar imagen
  str += "&nbsp;<span id=\"" + this.id + "-imagen\" class=\"tree-image " + this._srcImage + "\" ";
  str += " onclick=\"webFXTreeHandler.all[this.parentNode.id].clickImage('"+this._action+"')\"></span>";
  // fin insertar imagen

  str += "</div>";
  
	str += "<div id=\"" + this.id + "-cont\" class=\"webfx-tree-container\" style=\"display: " + ((this.open)?'block':'none') + ";\">";
	for (var i = 0; i < this.childNodes.length; i++) {
		str += this.childNodes[i].toString(i,this.childNodes.length);
	}
	str += "</div>";
	this.plusIcon = ((this.parentNode._last)?webFXTreeConfig.lPlusIcon:webFXTreeConfig.tPlusIcon);
	this.minusIcon = ((this.parentNode._last)?webFXTreeConfig.lMinusIcon:webFXTreeConfig.tMinusIcon);  
	return str;
}

WebFXImageTreeItem.prototype.clickImage = function (nmbFuncion) {
  eval(nmbFuncion+"('"+this.id+"')");  	
};

