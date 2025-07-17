package es.altia.agora.business.util;

import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;


public class GlobalNames {

    public static String ESQUEMA_GENERICO;

    private static Config m_ConfigTechnical;

    private static Log m_Log =
            LogFactory.getLog(GlobalNames.class.getName());

    static{ 
         m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
         String jndi = m_ConfigTechnical.getString("CON.jndi");
         String gestor = m_ConfigTechnical.getString("CON.gestor");
         String separador;
         Connection con = null;
         try{
             con=getDataSource(jndi).getConnection();
             if(gestor.toUpperCase().equalsIgnoreCase("SQLSERVER")){
                ResultSet tablas = con.getMetaData().getTables(getDataSource(jndi).getConnection().getCatalog(),null,"A_USU",null);
                if(tablas!=null)
                {
                    boolean first = false;
                    first = tablas.next();                 
                    if (first){
                        String esquema =  tablas.getString(2);
                        ESQUEMA_GENERICO = con.getCatalog()+ "." + esquema + ".";
                    }else{
                        ESQUEMA_GENERICO = con.getCatalog()+ "." + ".";
                    }
                }
                

                /*
                if (tablas.first()){
                        String esquema =  tablas.getString(2);
                        ESQUEMA_GENERICO = getDataSource(jndi).getConnection().getCatalog()+ "." + esquema + ".";
                }else{
                        ESQUEMA_GENERICO = getDataSource(jndi).getConnection().getCatalog()+ "." + ".";
                }
                */


            }else if (gestor.toUpperCase().equalsIgnoreCase("ORACLE")){
                        ESQUEMA_GENERICO = con.getMetaData().getUserName() + ".";                      
            }

         }catch(SQLException sqle){
             sqle.printStackTrace();
             throw new ExceptionInInitializerError(sqle);
         }finally{  
             try{
             if(con != null && !con.isClosed()) con.close();
             }catch(SQLException sqle){
                if(m_Log.isErrorEnabled()) m_Log.error("*** ConexionBD: "+ sqle.toString());
                 throw new ExceptionInInitializerError(sqle);
	}
         }
    }


   private static DataSource getDataSource(String jndi){

       try{
           return
            (DataSource) PortableContext.getInstance().lookup(jndi, DataSource.class);

       }
       catch(TechnicalException te){
            if(m_Log.isErrorEnabled()) m_Log.error("*** GlobalNames: " + te.toString());
           return null;
       }
   }
}





