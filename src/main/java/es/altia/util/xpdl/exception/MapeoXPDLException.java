package es.altia.util.xpdl.exception;


/**
   * Excepción lanzada durante la interpretación de un fichero XPDL
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

