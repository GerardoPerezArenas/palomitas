/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.portafirmasexternocliente.exception;

/**
 * @author david.caamano
 * @version 05/03/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 05/03/2013 * #53275 Edición inicial</li>
 * </ol> 
 */
public class PortafirmasExternoException extends Exception {
    
    public PortafirmasExternoException(String message){
        super(message);
    }//PortafirmasExternoException
    
     public PortafirmasExternoException(String message,Throwable ex){
        super(message,ex);
    }//PortafirmasExternoException
    
}//class
