
package es.altia.util.conexion;

public class BDException extends java.lang.Exception{

   /**
    * C�digo del error que gener� la excepci�n
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
    * Constructor b�sico
    */
   public BDException(){
	super();
   }

   /**
    * Constructor
    *
    * @param mensaje Texto descriptivo del error que genero la excepci�n
    */
   public BDException(String mensaje){
	super(mensaje);
   }

   /**
    * Constructor
    *
    * @param mensaje 	Texto descriptivo del error que genero la excepci�n
    * @param errorCode 	C�digo del error que genero la excepci�n
    */
   public BDException(int errorCode,String mensaje){
	super(mensaje);
	this.errorCode = errorCode;
	this.mensaje = mensaje;
   }

   /**
    * Constructor
    *
    * @param mensaje 	Texto descriptivo del error que genero la excepci�n
    * @param errorCode 	C�digo del error que genero la excepci�n
    * @param detalle 	Detalle del error que genero la excepci�n
    */
   public BDException(int errorCode, String mensaje, String detalle){
	super(mensaje);
	this.mensaje=mensaje;
	this.errorCode = errorCode;
	this.detalle= detalle;
   }

   /**
    * C�digo del error que genero la excepci�n
    *
    * @return	C�digo del error.
    */
   public int getErrorCode(){
	return errorCode;
   }

   /**
    * Descripcion del error que genero la excepci�n
    *
    * @return	Descripcion del error
    */
   public String getDescripcion(){
	return detalle;
   }

   /**
    * Devuelve el mensaje de error que genero la excepci�n
    *
    * @return 	Mensaje de error
    */
   public String getMensaje(){
	return mensaje;
   }

   /**
    * Devuelve un mensaje descriptivo de la excepci�n
    *
    * @return	Mensaje descriptivo
    */
   public String toString(){
	Integer error = new Integer(this.getErrorCode());
	return "ERROR: "+ error.toString() + " - " + super.toString();
   }
}