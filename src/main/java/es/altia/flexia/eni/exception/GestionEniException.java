package es.altia.flexia.eni.exception;

import es.altia.util.exceptions.ExcepcionConCodigo;

/**
 * Representa un error durante la importacion o exportacion de documentos y expedientes ENI. La creacion de la excepcion
 * ha de llevar un codigo relacionado con el tipo de error y un mensaje descriptivo. Ademas, es posible añadir una
 * excepcion durante la creacion para saber que otra excepcion puede estar encapsulando.
 */
public class GestionEniException extends ExcepcionConCodigo {

	/**
	 * Crea una excepcion del proceso ENI.
	 *
	 * @param codigo Codigo del error de {@link CodigoMensajeEni}, que hace referencia a un mensaje de
	 * resources/eni/mensajes_xx_XX.properties
	 * @param mensaje Mensaje de descripcion del error
	 * @param excepcion Excepcion que ha resultado en el lanzamiento de esta excepcion
	 * @param parametros Parametros para completar el mensaje del fichero properties en caso de que sea necesario
	 */
	public GestionEniException(String codigo, String mensaje, Throwable excepcion, Object... parametros) {
		super(codigo, mensaje, excepcion, parametros);
	}

	/**
	 * Crea una excepcion del proceso ENI.
	 *
	 * @param codigo Codigo del error de {@link CodigoMensajeEni}, que hace referencia a un mensaje de
	 * resources/eni/mensajes_xx_XX.properties
	 * @param mensaje Mensaje de descripcion del error
	 * @param parametros Parametros para completar el mensaje del fichero properties en caso de que sea necesario
	 */
	public GestionEniException(String codigo, String mensaje, Object... parametros) {
		super(codigo, mensaje, parametros);
	}

}
