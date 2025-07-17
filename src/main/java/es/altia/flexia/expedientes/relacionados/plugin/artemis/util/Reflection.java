package es.altia.flexia.expedientes.relacionados.plugin.artemis.util;

import es.altia.flexia.expedientes.relacionados.plugin.artemis.vo.ExpedienteArtemisVO;
import java.lang.reflect.Method;

/**
 *
 * @author oscar.rodriguez
 */
public class Reflection {

    /**
     * Recupera el valor de un método de un objeto determinado
     * @param objeto: Objeto a consulta de la clase ExpedienteArtemisVO
     * @param nombreMetodo: Nombre del método del objeto del que se quiere obtener su valor     
     * @return String con el valor del método
     */
   public static String getValorMetodo(ExpedienteArtemisVO objeto,String nombreMetodo){
        String valor ="";

        try{
            Class clase = Class.forName(objeto.getClass().getName());
            clase.newInstance();

            Method metodo = clase.getMethod(nombreMetodo,null);
            valor = (String)metodo.invoke(objeto,null);

        }catch(ClassNotFoundException e){
            e.printStackTrace();            
        }catch(Exception e){
            e.printStackTrace();
        }


        return valor;
    }


}
