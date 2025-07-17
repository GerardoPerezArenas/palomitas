/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.util.xpdl.exceptions;

/**
 *
 * @author ricardo.iglesias
 */
public class ParsingException extends RuntimeException {
        
    public ParsingException() {
        super();
    }
    
    public ParsingException(String message) {
        super(message);
    }
    
    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ParsingException(Throwable cause) {
        super(cause);
    }

}
