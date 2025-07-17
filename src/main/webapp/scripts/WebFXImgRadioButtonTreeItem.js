/*
 *	Sub class that adds a image and radio buttons in front of the tree item icon
 *	
 *	input.tree-radio-button {
 *		width:		auto;
 *		margin:		0;
 *		padding:	0;
 *		height:		14px;
 *		vertical-align:	middle;
 *	}
 *
 */
function isUndefined(a) { return ((typeof a == 'undefined') || (a==null)); }
var WEBFXIMGRADIOBUTTONTREEITEM_imgSi = 'fa fa-thumbs-up';
var WEBFXIMGRADIOBUTTONTREEITEM_imgNo ='fa fa-thumbs-down';

function WebFXImgRadioButtonTreeItem(sText, sAction, bValue, eParent, sIcon, sOpenIcon, sEstilo) {
    this._imgSi = WEBFXIMGRADIOBUTTONTREEITEM_imgSi;
	this._imgNo = WEBFXIMGRADIOBUTTONTREEITEM_imgNo;
	this.base = WebFXTreeItem;
	this.base(sText, sAction, eParent, sIcon, sOpenIcon, sEstilo);	
	this._checked = bValue;
}

WebFXImgRadioButtonTreeItem.prototype = new WebFXTreeItem;

WebFXImgRadioButtonTreeItem.prototype.toString = function (nItem, nItemCount) {
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

  // insert img y radio-button de grupo  
  /*
  str += "&nbsp;<img id=\"" + this.id + "-imagen\" class=\"tree-image\" src=\"" + this._srcImage + "\" ";
  str += " onclick=\"webFXTreeHandler.all[this.parentNode.id].clickImage('"+this._action+"')\">";
  */
  // end insert img
  // insert radioButton
  /*
  	str += "<input type=\"checkbox\"  class=\"tree-check-box\"  "
		+ " onclick=\"webFXTreeHandler.all[this.parentNode.id].setCheckedSI(this.checked)\" >SI&nbsp";	
	str += "<input type=\"checkbox\"  class=\"tree-check-box\" "
		+ " onclick=\"webFXTreeHandler.all[this.parentNode.id].setCheckedNO(this.checked)\" >NO&nbsp";	

*/
	str += "<span id=\"" + this.id + "-grupo\" class=\"webfx-tree-texto\" style=\"width:20px; heigth:10px;display:inline;\" >";
 	str += "&nbsp;<span id=\"" + this.id + "-imagenGrupo\" class=\"" + this._imgNo + " tree-image\" ></span>&nbsp;";
  	str += "<input type=\"radio\" name=\""+this.id+"-radio\" class=\"tree-radio-button\" value=\"-1\"" 
		+ " onclick=\"webFXTreeHandler.all[this.parentNode.parentNode.id].setRadio(this.value)\" >Grupo&nbsp"+"</span>";
	str += "<span id=\"" + this.id + "-permiso\" class=\"webfx-tree-texto\" style=\"width:20px; heigth:10px;display:inline;\" >";		
  	str += "<input type=\"radio\" name=\""+this.id+"-radio\" class=\"tree-radio-button\" value=\"1\"" 
		+ " onclick=\"webFXTreeHandler.all[this.parentNode.parentNode.id].setRadio(this.value)\" >SI&nbsp";			
	str += "<input type=\"radio\" name=\""+this.id+"-radio\" class=\"tree-radio-button\" value=\"0\"" 
		+ " onclick=\"webFXTreeHandler.all[this.parentNode.parentNode.id].setRadio(this.value)\" >NO&nbsp</span>";			
				
	// end radioButton
	
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

WebFXImgRadioButtonTreeItem.prototype.getRadio = function () {
	var radio = document.getElementsByName(this.id+"-radio");
	for (var i=0; i<radio.length; i++)		
		if (radio[i].checked) return radio[i].value;	
};

WebFXImgRadioButtonTreeItem.prototype.setRadio = function (valor) {
	var radio = document.getElementsByName(this.id+"-radio");
	for (var i=0; i<radio.length; i++)		
		if (radio[i].value==valor) radio[i].checked=true;	
	this._checked = valor;		
	if (typeof this.onchange == "function")
			this.onchange();
};

WebFXImgRadioButtonTreeItem.prototype.setGrupo = function (valor) {
	var imag = document.getElementById(this.id + "-imagenGrupo");
	if (valor)
		imag.className=this._imgSi;
	else imag.className=this._imgNo;
};

WebFXImgRadioButtonTreeItem.prototype.setSpanGrupo = function (valor) {
	var span = document.getElementById(this.id+"-grupo");
	if (valor)
		span.style.display ='inline';
	else span.style.display ='none';
	if (typeof this.onchange == "function")
			this.onchange();

};

WebFXImgRadioButtonTreeItem.prototype.setSpanPermiso = function (valor) {
	var span = document.getElementById(this.id+"-permiso");
	if (valor)
		span.style.display ='inline';
	else span.style.display ='none';
	if (typeof this.onchange == "function")
			this.onchange();

};

