package es.altia.agora.business.administracion.exception;

public class LoginDuplicadoException extends Exception {

    public LoginDuplicadoException(){
        super();
    }

    public LoginDuplicadoException(String message){
        super(message);
    }
 }