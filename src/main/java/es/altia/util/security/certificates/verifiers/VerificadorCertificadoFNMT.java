package es.altia.util.security.certificates.verifiers;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.util.security.certificates.VerificadorCertificado;
import es.fnmtrcm.verifcert.FNMTVerifCert;
import es.fnmtrcm.verifcert.FNMTVerifINI;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
	Implementaci�n de Certificado para la gesti�n de los certificados Clase 2 de la FNMT.<br>
	<br>
	@author Altia Consultores (BCS)
	@version  1.0, 30 Septiembre 2003
*/
public class VerificadorCertificadoFNMT implements VerificadorCertificado {
    private static final String CLSNAME="VerificadorCertificadoFNMT";
    protected static Log _log =
            LogFactory.getLog(VerificadorCertificadoFNMT.class.getName());

	private X509Certificate certificado= null;
	private String rutaParametros = null;
	private boolean valido=false;
	private boolean revocado = true;
	private int motivoInvalidez=0;

	private FNMTVerifCert certificadoVerificado=null;

	/**
    	Verifica las situaci�n del certificado y obtiene los atributos del mismo.
    	<br>

    	@param rutaParametros	Ruta completa al fichero de par�metros para el verificador.
    	@param certificado		Certificado que se quiere verificar.
    	@since 1.0
    */
	public void verifica(String rutaParametros, X509Certificate certificado) {
		try {
			this.certificado = certificado;
			this.rutaParametros  = rutaParametros;
			FNMTVerifINI fnmtIni=new FNMTVerifINI(rutaParametros);
	        certificadoVerificado = new FNMTVerifCert(fnmtIni,certificado);
	        certificadoVerificado.CargaCert();
	    } catch (NoSuchAlgorithmException e) {
            motivoInvalidez = ERROR_ALGORITMO_NO_VALID0;
        } catch (InvalidKeyException e) {
            motivoInvalidez = ERROR_CLAVE_NO_VALIDA;
        } catch (NoSuchProviderException e){
            motivoInvalidez = ERROR_PROVEEDOR_NO_VALID0;
        } catch (SignatureException e) {
            motivoInvalidez = ERROR_FIRMA_NO_VALIDA;
        } catch (CertificateException e) {
        	String cual = e.getClass().getName();
        	if (cual.equals("CertificateExpiredException"))
        		motivoInvalidez = ERROR_CERTIFICADO_CADUCADO;
        	else if (cual.equals("CertificateExpiredException"))
        		motivoInvalidez = ERROR_CERTIFICADO_AUN_NO_VALIDO;
        	else
        		motivoInvalidez = ERROR_CERTIFICADO_GRAL;
        } catch (Exception e) {
	    	motivoInvalidez = ERROR_INTERNO;
            _log.error(CLSNAME+".verifica() Error interno:"+e.getClass() +"\n con el mensaje:	" + e.getMessage() +	"\n Traza: \n",e);
		}//try-catch
	}

	/**
    	Nos dice si el certificado es v�lido.<br>
    	<br>

    	@return true o false en funci�n de la validez del certificado.
    	@since 1.0
    */
    public boolean esValido() {
    	return valido;
	}

    /**
    	En caso de que el certificado no sea v�lido, devuelve el motivo de la invalidez.<br>
    	<br>

    	@return motivo de la invalidez..
    	@since 1.0
    */
    public int motivoInvalidez() {
    	return motivoInvalidez;
    }

	/**
    	Nos dice si el certificado ha sido revocado o no.<br>
    	<br>

    	@return devuelve un booleano que indica si el estado de revocaci�n del
    			certificado.
    	@since 1.0
    */
    public boolean esRevocado() {
    	return certificadoVerificado.isRevocado();
    }

    /**
    	En caso de que este revocado, nos devuelve la fecha de revocaci�n del certificado.<br>
    	<br>

    	@return La fecha de revocaci�n.
    	@since 1.0
    */
    public String fechaRevocacion() {
    	return certificadoVerificado.fechaRevocacion();
	}

    /**
    	En caso de que este revocado, nos devuelve una cadena con el motivo de revocaci�n
    	de revocaci�n del certificado.<br>
    	<br>

    	@return devuelve el motivo.
    	@since 1.0
    */
    public String motivoRevocacion() {
    	return certificadoVerificado.motivo();
    }

    /**
		Devuelve el NIF asociado al certificado<br>
		<br>

		@return Cadena NIF.
		@since 1.0
    */
    public String nifSujeto() {
        if (certificadoVerificado == null)
            _log.warn(CLSNAME+".nifSujeto() Certificado Verificado es null");
        else if (certificadoVerificado.AtrbFNMT() == null)
            _log.warn(CLSNAME+".nifSujeto() atrbFNMT de certificadoVerificado es nulo");
    	return (String) certificadoVerificado.AtrbFNMT().get("fnmtNif");
    }//nifSujeto
}//class