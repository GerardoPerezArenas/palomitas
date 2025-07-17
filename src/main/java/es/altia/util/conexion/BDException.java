
package es.altia.util.conexion;

public class BDException extends java.lang.Exception{

   /**
    * Código del error que generó la excepción
    */
   int errorCode;
   /**
    * Detalle del error devuelto por la excepcion
    */
   String detalle;
   /**
    * Mensaje descriptivo de la excepcion
    */
   String mensaje;

   /**
    * Constructor básico
    */
   public BDException(){
	super();
   }

   /**
    * Constructor
    *
    * @param mensaje Texto descriptivo del error que genero la excepción
    */
   public BDException(String mensaje){
	super(mensaje);
   }

   /**
    * Constructor
    *
    * @param mensaje 	Texto descriptivo del error que genero la excepción
    * @param errorCode 	Código del error que genero la excepción
    */
   public BDException(int errorCode,String mensaje){
	super(mensaje);
	this.errorCode = errorCode;
	this.mensaje = mensaje;
   }

   /**
    * Constructor
    *
    * @param mensaje 	Texto descriptivo del error que genero la excepción
    * @param errorCode 	Código del error que genero la excepción
    * @param detalle 	Detalle del error que genero la excepción
    */
   public BDException(int errorCode, String mensaje, String detalle){
	super(mensaje);
	this.mensaje=mensaje;
	this.errorCode = errorCode;
	this.detalle= detalle;
   }

   /**
    * Código del error que genero la excepción
    *
    * @return	Código del error.
    */
   public int getErrorCode(){
	return errorCode;
   }

   /**
    * Descripcion del error que genero la excepción
    *
    * @return	Descripcion del error
    */
   public String getDescripcion(){
	return detalle;
   }

   /**
    * Devuelve el mensaje de error que genero la excepción
    *
    * @return 	Mensaje de error
    */
   public String getMensaje(){
	return mensaje;
   }

   /**
    * Devuelve un mensaje descriptivo de la excepción
    *
    * @return	Mensaje descriptivo
    */
   public String toString(){
	Integer error = new Integer(this.getErrorCode());
	return "ERROR: "+ error.toString() + " - " + super.toString();
   }
}