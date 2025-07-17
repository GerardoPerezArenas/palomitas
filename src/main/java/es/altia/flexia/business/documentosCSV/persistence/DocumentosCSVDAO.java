/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.business.documentosCSV.persistence;

import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Clase que implementa las operaciones de acceso a datos de las comunicaciones de Flexia
 *
 * @author Ricardo
 */
public class DocumentosCSVDAO {
    private static DocumentosCSVDAO instance =	null;
   
    protected static Log m_Log =
            LogFactory.getLog(DocumentosCSVDAO.class.getName());




    /** Constructor
     * 
     */
    protected DocumentosCSVDAO() {
              

     }

     /** Método que inicializa el DAO si no lo está previamente
      * 
      * @return
      */
     public static DocumentosCSVDAO getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (DocumentosCSVDAO.class) {
            if (instance == null) {
                instance = new DocumentosCSVDAO();
            }

        }
        return instance;
    }
     
    /** Método que obtiene la lista de comunicaciones que cumplen los parámetros dados
     * 
     * @param organizacion Identificador de la organización del usuario que creó la comunicación
     * @param ejercicio Ejercicio en el que se creó la comunicación
     * @param numExpediente Número de expediente asociado a la comunicación
     * @param conexion Coenxión de BBDD
     * @return Lista de objetos  ComunicacionVO que cumplen las condiciones
     */
   public String getUrlFromCSV (String organizacion, String CSV,Connection conexion){
        
   
    AdaptadorSQLBD oad = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    
       String salida="";
        try{
            //String sql = "Select ID, ASUNTO, TEXTO, TIPO_DOCUMENTO, DOCUMENTO , NOMBRE, FECHA, NUM_REGISTRO, ORIGEN_REGISTRO, XML_COMUNICACION, FIRMA, PLATAFORMA_FIRMA, COD_ORGANIZACION, EJERCICIO, NUM_EXPEDIENTE, LEIDA from COMUNICACION where COD_ORGANIZACION = ? and EJERCICIO =? and NUM_EXPEDIENTE = ?";
            String sql;
          
            sql= "SELECT CSV_URI FROM METADATO_DOCUMENTO WHERE CSV=?";
            
            if(m_Log.isDebugEnabled()){
                m_Log.debug("CSV " + CSV);
                m_Log.debug("sql " + sql);
            }
            stmt = conexion.prepareStatement(sql);
     
            stmt.setString(1, CSV);
  
            rs = stmt.executeQuery();            
            if(rs.next()){
                salida=rs.getString("CSV_URI");
            }
            else return null;
               
            

        }catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()){m_Log.error(this.getClass().getName() + ".getComunicaciones(): -> Error " + e.getMessage());}
        }
        finally{
            try{
                
            }catch(Exception e){
                e.printStackTrace();
                if(m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".getComunicaciones(): -> Error " + e.getMessage());}
            }
        }
        
        return salida;
    }
    
    
   
}
