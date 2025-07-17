package es.altia.util.ajax.respuesta;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

public class RespuestaAjaxUtils {
    /**
     * M�todo llamado para devolver un String en formato JSON al cliente que ha realiza la petici�n 
     * a alguna de las operaciones de este action
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a trav�s del cual se devuelve la salida
     * al cliente que ha realizado la solicitud
     */
    public static void retornarJSON(String json,HttpServletResponse response){
        
        try{
            if(json!=null){
                response.setCharacterEncoding("UTF-8");                
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
