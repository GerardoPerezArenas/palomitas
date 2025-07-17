package es.altia.agora.interfaces.user.web.util;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.persistence.ValidarDocumentoManager;

import java.util.Vector;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Manuel Vera Silvestre
 * @version 1.0
 */

public class ValidarDocumento {

   public String validarDocumento(String[] params, String texto){
	String funcion = "";
	Vector v = ValidarDocumentoManager.getInstance().load(params);
	GeneralValueObject longitudes = new GeneralValueObject();
	String sinDoc = "-1";
	for(int i=0;i<v.size();i++){
	   String[] aux = (String[])v.elementAt(i);
	   int desde = 0;
	   int hasta = Integer.parseInt(aux[1]);
	   int longitud = Integer.parseInt(aux[1]) + Integer.parseInt(aux[3]) +
				Integer.parseInt(aux[5]) + Integer.parseInt(aux[7]) +
				Integer.parseInt(aux[9]);
	   if(longitud == 0)
		sinDoc = aux[0];
	   if(i != 0)
		funcion += "else ";
	   funcion += "if(tipo == " + aux[0] + " && !error){ \r\n ";
	   if(aux[11].equalsIgnoreCase("n")){//es un nif
           //Si tiene longitud menor de 8 se añaden ceros por la izquierda
           funcion += "if ((documento.length > 0) && (documento.length < 8)) {documento = '00000000' + documento;documento = documento.substring(documento.length-8,documento.length);}";
           //FIN: Si tiene longitud menor de 8 se añaden ceros por la izquierda
        funcion += "if(documento.length < 8 || documento.lenght > 9) \r\n" +
		   " error = true; \r\n else{ \r\n var numero = documento.substri" +
		   "ng(" + desde + "," + hasta + "); \r\n error = comprobar('" +
		   aux[2] + "',numero); \r\n ";
	   }
	   else{
		  if(aux[0].equalsIgnoreCase("4")) {
              funcion += "if(documento.length > " + longitud + ") \r\n error =" +
			     " true; \r\n else if(documento.length == 9){ \r\n error = comprobar('" + aux[2] +
			     "',documento.substring(" + desde + "," + hasta + ")); " +
			     "\r\n ";
		  } else {
		    funcion += "if(documento.length != " + longitud + ") \r\n error =" +
			     " true; \r\n else{ \r\n error = comprobar('" + aux[2] +
			     "',documento.substring(" + desde + "," + hasta + ")); " +
			     "\r\n ";
			}
	   }
	   if(Integer.parseInt(aux[3]) > 0){//si hay otro grupo
		desde = hasta;
		hasta += Integer.parseInt(aux[3]);
		if(aux[11].equalsIgnoreCase("n")){//es un nif
		   funcion += "if(!error){ \r\n var letra = letraNIF(numero); \r\n "
			+ "if(documento.length == 8){ eval(\"document.forms[0].\" +" +
			" idDocumento + \".value=documento + letra\"); \r\n } \r\n el"
			+ "se{ \r\n var letradoc = documento.substring(" + desde + ","
			+ hasta + "); \r\n letradoc = letradoc.toUpperCase(); \r\n " +
			"if((letra != letradoc) && (msj == 1)){ \r\n jsp_alerta(\"A" +
			"\",\"Letra del N.I.F. incorrecta\"); \r\n } \r\n } \r\n ";
		}
		else{
		   funcion += "if(!error){ \r\n error = comprobar('" + aux[4] + "',"
			+ "documento.substring(" + desde + "," + hasta + ")); \r\n ";
		}
		if(Integer.parseInt(aux[5]) > 0){//si hay otro grupo
		   desde = hasta;
		   hasta += Integer.parseInt(aux[5]);
		   funcion += "if(!error){ \r\n error = comprobar('" + aux[6] + "',"
			+ "documento.substring(" + desde + "," + hasta + ")); \r\n ";
		   if(Integer.parseInt(aux[7]) > 0){//si hay otro grupo
			desde = hasta;
			hasta += Integer.parseInt(aux[7]);
			funcion += "if(!error){ \r\n error = comprobar('" + aux[8] +
			 "',documento.substring(" + desde + "," + hasta + ")); \r\n ";
			if(Integer.parseInt(aux[9]) > 0){//si hay otro grupo
			   desde = hasta;
			   hasta += Integer.parseInt(aux[9]);
			   funcion += "if(!error) \r\n error = comprobar('" + aux[10]
				+ "',documento.substring(" + desde + "," + hasta + ")" +
				"); \r\n ";
			}
			funcion += "} \r\n ";
		   }
		   funcion += "} \r\n ";
		}
		funcion += "} \r\n ";
	   }
	   funcion += "} \r\n } \r\n ";
	}
	funcion += "if(error){ \r\n jsp_alerta(\"A\",\"" + texto + "\"); \r\n e" +
	   "val(\"document.forms[0].\" + idDocumento + \".select()\"); \r\n }  " +
	   "\r\n } \r\n return(error); \r\n } \r\n function comprobar(tipo,cade" +
	   "na){ \r\n var error = false; \r\n for(var i=0;((i<cadena.length) &&" +
	   " !error);i++){ \r\n if(((tipo == 'n') && !((cadena.charCodeAt(i) > " +
	   "47) && (cadena.charCodeAt(i) < 58))) || ((tipo == 'a') && !(((caden" +
	   "a.charCodeAt(i) > 64) && (cadena.charCodeAt(i) < 91)) || ((cadena.c" +
	   "harCodeAt(i) > 96) && (cadena.charCodeAt(i) < 123)))) || ((tipo == " +
	   "'x') && !(((cadena.charCodeAt(i) > 47) && (cadena.charCodeAt(i) < 5" +
	   "8)) || ((cadena.charCodeAt(i) > 64) && (cadena.charCodeAt(i) < 91))" +
	   " || ((cadena.charCodeAt(i) > 96) && (cadena.charCodeAt(i) < 123))))" +
	   ") \r\n error = true; \r\n } \r\n return error; \r\n } \r\n function" +
	   " letraNIF(nif){ \r\n var cadena = 'TRWAGMYFPDXBNJZSQVHLCKET'; \r\n " +
	   "var pos = nif % 23; \r\n return(cadena.charAt(pos)); \r\n } \r\n fu" +
	   "nction normalAobligatorioDoc(obj){ \r\n if('obligatorio' != obj.id" +
	   "){ \r\n obj.id = \"obligatorio\"; \r\n obj.className = \"input" +
	   "TextoObligatorio\"; \r\n obj.readOnly=false;\r\n } \r\n } \r\n function obligatorioAnormalD" +
	   "oc(obj){ \r\n if('obligatorio' == obj.id){ \r\n ob" +
	   "j.id = \"\"; \r\n obj.className = \"inputTextoDeshabilitado\"; \r\n obj.readOnly=true;\r\n } \r\n } \r\n";
	String cabecera = "function documentoNoValido(idTipo,idDocumento,msj){ " +
	   "\r\n var error = false; \r\n var tipo = eval(\"document.forms[0].\"" +
	   " + idTipo + \".value\");\r\n var documento = eval(\"document.forms[" +
			 "0].\" + idDocumento + \".value\"); \r\n var docForm = eval(\"document.forms[" +
	   "0].\" + idDocumento); \r\n if(msj==0){\r\n if(tipo == " +
	   sinDoc + ") \r\n obligatorioAnormalDoc(docForm); \r\n else \r\n " +
	   "normalAobligatorioDoc(docForm); \r\n } \r\n else{ \r\n if(tipo " +
	   "== '') \r\n error = true; \r\n ";
	return(cabecera + funcion);
   }
   
}