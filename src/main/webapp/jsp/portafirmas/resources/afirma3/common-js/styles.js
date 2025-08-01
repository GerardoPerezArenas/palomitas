/*
 * Este fichero forma parte del Cliente @firma. 
 * El Cliente @firma es un aplicativo de libre distribucion cuyo codigo fuente puede ser consultado
 * y descargado desde www.ctt.map.es.
 * Copyright 2009,2010,2011 Gobierno de Espana
 * Este fichero se distribuye bajo licencia GPL version 3 segun las
 * condiciones que figuran en el fichero 'licence' que se acompana. Si se distribuyera este 
 * fichero individualmente, deben incluirse aqui las condiciones expresadas alli.
 */

function getStyles(doc)
{
	if(!doc)
	{
		return "";
	}

	var xml = "";
	
	if(!isEmpty(doc.styleSheets))
	{
		var i;
		for(i=0; i<doc.styleSheets.length; i++)
		{
			var stylesheet = doc.styleSheets[i];
			xml += getStyleSheetText(stylesheet) + '\n';
		}
	}
	
	var text = xml;
	var aux;	
	while(text.indexOf("-moz")!=-1 && aux!=text)
	{	/* Eliminamos las reglas que comiencen por "-moz", exclusivas de mozilla */
		aux = text;
		text= text.replace(/-moz[^\:]*\:[^\;\}]*;/, "");
		text= text.replace(/-moz[^\:]*\:[^\;\}]*}/, "}");
	}
	
	aux = "";
	while(text.indexOf("rgb")!=-1 && aux!=text)
	{	/* Convertimos los rgb pq el visor no los interpreta bien */
		aux = text;
		
		var posIni = aux.search(/rgb[\\s]*\([^\)]*\)/);
		var posFin = aux.indexOf(")", posIni);
		var rgbStr1 = aux.substring(posIni, posFin+1);
		var pos1 = rgbStr1.indexOf("(");
		var pos2 = rgbStr1.indexOf(")");
		var rgbStr2 = rgbStr1.substring(pos1+1, pos2);
		var rgbNum = rgbStr2.split(",");
		var red   = toHex(rgbNum[0]);
		var green = toHex(rgbNum[1]);
		var blue  = toHex(rgbNum[2]);
		var newColor = "#"+red+green+blue;
		
		text= text.replace(rgbStr1, newColor);
	}
	xml = text;
	
	return xml;
}

function getStyleSheetText(stylesheet)
{
	if(!stylesheet)
	{
		return "";
	}

	var xml = "";

	// stylesheet.cssRules -> Mozilla, stylesheet.rules -> IE4+
	var cssRules = stylesheet.cssRules?stylesheet.cssRules:stylesheet.rules; 
	if(!isEmpty(cssRules))
	{
		var j;
		for(j=0; j<cssRules.length; j++)
		{
			var cssRule = cssRules[j];
			xml+= getStyleCSSRuleText(cssRule) + '\n';
		}
	}
	
	if(!isEmpty(stylesheet.imports))
	{	/* IE4+ */
		var k;
		for(k=0; k<stylesheet.imports.length; k++)
		{
			var imp = stylesheet.imports[k];
			xml += getStyleSheetText(imp) + '\n';
		}
	}
	
	return xml;
}

function getStyleCSSRuleText(cssRule)
{
	if(!cssRule)
	{
		return "";
	}

	var xml = "";

	if(cssRule && cssRule.type!= 3)
	{	/* cssRule.type = 3 -> import */
		if(cssRule.cssText)
		{	/* W3C DOM -> IE6+, NN6+, Mozilla */
			xml = cssRule.cssText;
		}
		else if(cssRule.selectorText && cssRule.style.cssText)
		{	/* IE4+ */
			xml = cssRule.selectorText + " { " + cssRule.style.cssText+" }";
		}
	}
	
	return xml;
}

