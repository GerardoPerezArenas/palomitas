package es.altia.util.xpdl.exception;


/**
   * Excepci�n lanzada durante la interpretaci�n de un fichero XPDL
  * @author oscar.rodriguez
  */
public class MapeoXPDLException extends Exception{

        public MapeoXPDLException(){
            super();
        }

        public MapeoXPDLException(String message){
            super(message);
        }

        public MapeoXPDLException(String message,Throwable t){
            super(message,t);
        }

}

