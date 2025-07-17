package es.altia.agora.webservice.tramitacion.servicios;

import es.altia.common.exception.FunctionalException;

public class WSException extends FunctionalException {

	/**
	 * Indica si el servicio web que lanza la excepcion tiene que ejecutarse
	 * con exito obligatoriamente o no
	 */
	private boolean mandatoryExecution;

	/**
	* Create a <code>WSException</code>
	* and set the exception error message.
	* Pone a <code>false</code> la propiedad que indica si el servicio web que
	* lanza la excepcion tiene que ejecutarse con exito obligatoriamente o no
	* @param theMessage the message
	*/

	public WSException(String theMessage) {
		this(theMessage, false, null);
	}

	/**
	* Create a <code>WSException</code>
	* and set the exception error message.
	*
	* @param mandatoryExecution indica si el servicio web que
	* 	lanza la excepcion tiene que ejecutarse con exito obligatoriamente o no
	* @param theMessage the message
	*/
	public WSException(String theMessage, boolean mandatoryExecution) {
		this(theMessage, mandatoryExecution, null);
	}

	/**
	* Create a <code>WSException</code>
	* and set the exception object that caused this exception.
	* Pone a <code>false</code> la propiedad que indica si el servicio web que
	* lanza la excepcion tiene que ejecutarse con exito obligatoriamente o no
	* @param theMessage   the detail of the error message
	* @param theException the original exception
	*/
	public WSException(String theMessage, Throwable theException) {
		this(theMessage, false, theException);
	}

	/**
	* Create a <code>WSException</code>
	* and set the exception object that caused this exception.
	* @param theMessage   the detail of the error message
	* @param mandatoryExecution indica si el servicio web que
	* 	lanza la excepcion tiene que ejecutarse con exito obligatoriamente o no
	* @param theException the original exception
	*/
	public WSException(String theMessage, boolean mandatoryExecution,
			Throwable theException) {
		super(theMessage, theException);
		this.mandatoryExecution = mandatoryExecution;
	}

	/**
	 *
	 * @return <code>true</code> si el servicio web que
	* 	lanza la excepcion tiene que ejecutarse con exito obligatoriamente,
	 *  <code>false</code> en caso contrario
	 */
	public boolean isMandatoryExecution() {
		return mandatoryExecution;
	}
}