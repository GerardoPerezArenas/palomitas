/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.eni.util;

/**
 *
 * @author Juangc
 */
public class DocumentNotFoundException extends Exception{
    
    public DocumentNotFoundException() { 
        super();
    }
    
    public DocumentNotFoundException(String message) {
        super(message);
    }

    public DocumentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentNotFoundException(Throwable cause) {
        super(cause);
    }
    
}
