/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.util;

/**
 *
 * @author david.vidal
 */
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.log4j.Logger;
 
public class evalua_cadena {
    public static Logger log= Logger.getLogger(evalua_cadena.class);
 
    public static String evalua(String cadena) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        String resultado = new String("");                
        cadena = cadena.replace(",",".");
        try {
            Object result = engine.eval(cadena);                            
            resultado = result.toString();       
            resultado = resultado.replace(".",",");
        } catch(ScriptException se) {
            se.printStackTrace();
            log.debug("Error en evaluacion de cadena : " + cadena);
        }        
        return resultado;
    }
    public static String evalua_calculos(String cadena) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        String resultado = new String("");                
        cadena = cadena.replace(",",".");
        try {
            Object result = engine.eval(cadena);                            
            resultado = result.toString();
            resultado= FormatoNumero2Decimal(resultado);            
            resultado = resultado.replace(".",",");
        } catch(ScriptException se) {
            se.printStackTrace();
            log.debug("Error en evaluacion de cadena : " + cadena);
        }        
        return resultado;
    }
    private static String FormatoNumero2Decimal(String numero){
        String resultado = "";
        try{
            Double num = new Double(numero);            
            DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
            DecimalFormat format = new DecimalFormat("#0.00",simbolo);
            resultado = format.format(num);                      
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
}