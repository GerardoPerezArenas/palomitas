package es.altia.util.security.certificates;

import java.security.cert.X509Certificate;

/**
	Esta interfase nos permite encapsular la gesti�n de un certificado.
	De esta forma en funci�n del origen del certificado a usar podremos usar la clase
	adecuada para realizar la verificaci�nes y comprobaciones de las listas de revocados.<br>
	<br>
	@author Alia Consultores (BCS)
	@version  1.0, 30 Septiembre 2003
*/
public interface VerificadorCertificado {
	public final int ERROR_INTERNO = 1;
	public final int ERROR_CERTIFICADO_GRAL = 10;
	public final int ERROR_CERTIFICADO_CADUCADO = 11;
	public final int ERROR_CERTIFICADO_AUN_NO_VALIDO = 12;
	public final int ERROR_CERTIFICADO_REVOCADO = 13;

	public final int ERROR_PROVEEDOR_NO_VALID0 = 20;
	public final int ERROR_ALGORITMO_NO_VALID0 = 30;
	public final int ERROR_CLAVE_NO_VALIDA = 40;
	public final int ERROR_FIRMA_NO_VALIDA = 50;

	/**
    	Verifica las situaci�n del certificado y obtiene los atributos del mismo.
    	<br>

    	@param rutaParametros	Ruta completa al fichero de par�metros para el verificador.
    	@param certificado		Ceritificado que se quiere verificar.
    	@since 1.0
    */
	public void verifica(String rutaParametros, X509Certificate certificado);

	/**
    	Nos dice si el certificado es v�lido.<br>
    	<br>

    	@return true o false en funci�n de la validez del certificado.
    	@since 1.0
    */
    public boolean esValido();

    /**
    	En caso de que el certificado no sea v�lido, devuelve el motivo de la invalidez.<br>
    	<br>

    	@return motivo de la invalidez..
    	@since 1.0
    */
    public int motivoInvalidez();

	/**
    	Nos dice si el certificado ha sido revocado o no.<br>
    	<br>

    	@return devuelve un booleano que indica si el estado de revocaci�n del
    			certificado.
    	@since 1.0
    */
    public boolean esRevocado();

    /**
    	En caso de que este revocado, nos devuelve la fecha de revocaci�n del certificado.<br>
    	<br>

    	@return La fecha de revocaci�n.
    	@since 1.0
    */
    public String fechaRevocacion();

    /**
    	En caso de que este revocado, nos devuelve una cadena con el motivo de revocaci�n
    	de revocaci�n del certificado.<br>
    	<br>

    	@return devuelve el motivo.
    	@since 1.0
    */
    public String motivoRevocacion();

    /**
    	Devuelve el NIF asociado al certificado<br>
    	<br>

    	@return Cadena NIF.
    	@since 1.0
    */
    public String nifSujeto();
}//class