/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.eni.util;

/**
 *
 * @author Kevin
 */
public enum GestionEniEnumAlias {
	ALIAS_DOCUMENTO_ENI_BINARIO("ALIAS_DOCUMENTO_ENI_BINARIO"),
	ALIAS_DOCUMENTO_ENI_BLOB("ALIAS_DOCUMENTO_ENI_BLOB"),
	ALIAS_DOCUMENTO_ENI_STRING_XML("ALIAS_DOCUMENTO_ENI_STRING_XML"),
	
	ALIAS_FIRMA_DOCUMENTO("ALIAS_FIRMA_DOCUMENTO"),
	ALIAS_FIRMA_DOCUMENTO_BLOB("ALIAS_FIRMA_DOCUMENTO_BLOB"),
	
	ALIAS_FORMATO_DOCUMENTO("ALIAS_FORMATO_DOCUMENTO"),
	ALIAS_FORMATO_DOCUMENTO_MIME("ALIAS_FORMATO_DOCUMENTO_MIME"),
	ALIAS_FORMATO_NOMBRE_DOCUMENTO("ALIAS_FORMATO_NOMBRE_DOCUMENTO");

	private final String Alias;
	
	GestionEniEnumAlias(String Alias){
		this.Alias = Alias;
	};
	
 public String getAlias(){
	 return Alias;
 }
}
