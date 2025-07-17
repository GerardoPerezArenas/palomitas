package es.altia.agora.business.sge.plugin.documentos.dao;

import es.altia.agora.business.sge.plugin.documentos.vo.PluginAlmacenamientoVO;
import es.altia.agora.business.sge.plugin.documentos.vo.RepositorioDocumentacionDocumentosExternosPortafirmasVO;
import es.altia.agora.business.sge.plugin.documentos.vo.RepositorioDocumentacionProcedimientoVO;
import es.altia.agora.business.sge.plugin.documentos.vo.RepositorioDocumentacionRegistroVO;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.commons.DateOperations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.log4j.Logger;

/**
 * Objeto de acceso a datos con operaciones que trabajan contra la tabla
 * PLUGIN_DOC_PROCEDIMIENTO
 * @author oscar
 */
public class RepositorioPluginAlmacenamientoDocumentosDAO {
  
    private Logger log = Logger.getLogger(RepositorioPluginAlmacenamientoDocumentosDAO.class);
    
    
    
    public ArrayList<RepositorioDocumentacionProcedimientoVO> getPluginAlmacenamientoDocumentoProcedimiento(Connection con){
        ArrayList<RepositorioDocumentacionProcedimientoVO> salida = new ArrayList<RepositorioDocumentacionProcedimientoVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;        

        try{
            String sql = "SELECT ID,COD_PROCEDIMIENTO,IMPLCLASS_PLUGIN,FECHA_ALTA,PML_VALOR "  + 
                         "FROM PLUGIN_DOC_PROCEDIMIENTO, E_PML WHERE PML_COD=COD_PROCEDIMIENTO ORDER BY PML_VALOR DESC";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){

                RepositorioDocumentacionProcedimientoVO obj = new RepositorioDocumentacionProcedimientoVO();
                obj.setId(rs.getInt("ID"));
                obj.setCodProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                obj.setImplClassPlugin(rs.getString("IMPLCLASS_PLUGIN"));
                obj.setNombreProcedimiento(rs.getString("PML_VALOR"));   
                obj.setFechaAlta(DateOperations.timestampToCalendar(rs.getTimestamp("FECHA_ALTA")));
                salida.add(obj);
            }
        }catch(SQLException e){

            e.printStackTrace();

        }finally{

            try{

                if(ps!=null) ps.close();

                if(rs!=null) rs.close();

            }catch(SQLException e){

                e.printStackTrace();

            }

        }        

        return salida;

    }
    
    public String getDigitalizacionProcedimiento(String codProcedimiento, Connection con){
        PreparedStatement ps=null;
        ResultSet rs=null; 
        String idDigitalizacion=null;
        
        try{
            String sql="SELECT DIGIT_DOC_REGISTRO FROM PLUGIN_DOC_PROCEDIMIENTO WHERE COD_PROCEDIMIENTO=?";
            ps=con.prepareStatement(sql);
            ps.setString(1, codProcedimiento);
            rs=ps.executeQuery();
            
            while(rs.next()){
                idDigitalizacion=rs.getString("DIGIT_DOC_REGISTRO");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return idDigitalizacion;
    }

    
    private int getPluginAlmacenamientoProcedimiento(String codProcedimiento,Connection con){
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idAlmacen = -1;
        
        try{
            String sql ="SELECT ID_ALMACEN FROM PLUGIN_DOC_PROCEDIMIENTO,ALMACEN_DOC_DISPONIBLES " + 
                        "WHERE COD_PROCEDIMIENTO=? AND ID_ALMACEN=ALMACEN_DOC_DISPONIBLES.ID";
                        
            ps = con.prepareStatement(sql);
            ps.setString(1,codProcedimiento);
            rs = ps.executeQuery();
            
        
            while(rs.next()){
                idAlmacen =rs.getInt("ID_ALMACEN");
            }
            
        }catch(SQLException e){
            e.printStackTrace();
            
        }finally{
            
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return idAlmacen;
    }
    
    
    
    /**
     * Recupera los plugins de cada procedimiento existente en la organización
     * @param con: Conexión a la BBDD 
     * @return ArrayList<RepositorioDocumentacionProcedimientoVO>
     */
    public ArrayList<RepositorioDocumentacionProcedimientoVO> getListadoProcedimientosPlugin(Hashtable<Integer,PluginAlmacenamientoVO> plugins,Connection con, boolean indicarDigitalizacion){
        ArrayList<RepositorioDocumentacionProcedimientoVO> salida = new ArrayList<RepositorioDocumentacionProcedimientoVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            
            String sql = "SELECT PML_COD,PML_VALOR FROM E_PML ORDER BY PML_COD ASC";                                                 
            log.debug(sql);
            
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                           
                RepositorioDocumentacionProcedimientoVO obj = new RepositorioDocumentacionProcedimientoVO();                
                obj.setCodProcedimiento(rs.getString("PML_COD"));               
                obj.setNombreProcedimiento(rs.getString("PML_VALOR"));   
                
                int idPlugin = this.getPluginAlmacenamientoProcedimiento(obj.getCodProcedimiento(), con);
                if(idPlugin!=-1){
                    PluginAlmacenamientoVO plugin = plugins.get(idPlugin);                    
                    obj.setNombrePlugin(plugin.getNombre());              
                    obj.setImplClassPlugin(plugin.getImplClass());   
                    obj.setIdPlugin(plugin.getId());
                    
                }else{
                    // Al no tener plugin, se le asigna el de por defecto que es el de base de datos.
                    obj.setIdPlugin(ConstantesDatos.ID_PLUGIN_IMPLCLASS_PLUGIN_ALMACENAMIENTO_BBDD);
                    obj.setImplClassPlugin(ConstantesDatos.IMPLCLASS_PLUGIN_ALMACENAMIENTO_BBDD);
                    obj.setNombrePlugin(ConstantesDatos.NOMBRE_PLUGIN_ALMACENAMIENTO_BBDD);                    
                }
                
                if(indicarDigitalizacion){
                    String idDigitalizacion=this.getDigitalizacionProcedimiento(obj.getCodProcedimiento(), con);
                    if(idDigitalizacion!=null){
                        obj.setDigitalizacion(idDigitalizacion);
                    }
                }
                
                salida.add(obj);            
            }
            
        }catch(SQLException e){
            log.error("Error al recuperar plugin almacenamiento por procedimiento: " + e.getMessage());
            e.printStackTrace();
            
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return salida;
    }
    
    

    
    
    /**
     * Recupera los plugin a ejecutar para el módulo de Registro
     * @param con: Conexión a la BBDD
     * @return ArrayList<RepositorioDocumentacionRegistroVO>
     */
     public ArrayList<RepositorioDocumentacionRegistroVO> getPluginAlmacenamientoDocumentoRegistro(Connection con){
        ArrayList<RepositorioDocumentacionRegistroVO> salida = new ArrayList<RepositorioDocumentacionRegistroVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            String sql = "SELECT ID,IMPLCLASS_PLUGIN,FECHA_ALTA,ID_ALMACEN "  + 
                         "FROM PLUGIN_DOC_REGISTRO";
            log.debug(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                RepositorioDocumentacionRegistroVO obj = new RepositorioDocumentacionRegistroVO();
                obj.setId(rs.getInt("ID"));                
                obj.setImplClassPlugin(rs.getString("IMPLCLASS_PLUGIN"));                
                obj.setIdAlmacen(rs.getInt("ID_ALMACEN"));
                salida.add(obj);
            }
            
        }catch(SQLException e){
            log.error("Error al recuperar plugin almacenamiento por registro: " + e.getMessage());
            e.printStackTrace();
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return salida;
    }
    
    
    /**
     * Recupera los plugin a ejecutar para el procesamientos de los documentos externos enviados al protafirmas
     * @param con: Conexión a la BBDD
     * @return ArrayList<RepositorioDocumentacionDocumentosExternosPortafirmasVO>
     */
     public ArrayList<RepositorioDocumentacionDocumentosExternosPortafirmasVO> getPluginAlmacenamientoDocumentosExternosPortafirmas(Connection con){
        ArrayList<RepositorioDocumentacionDocumentosExternosPortafirmasVO> salida = new ArrayList<RepositorioDocumentacionDocumentosExternosPortafirmasVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            String sql = "SELECT ID,IMPLCLASS_PLUGIN,FECHA_ALTA "  + 
                         "FROM PLUGIN_DOC_REGISTRO";
            log.debug(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                RepositorioDocumentacionDocumentosExternosPortafirmasVO obj = new RepositorioDocumentacionDocumentosExternosPortafirmasVO();
                obj.setId(rs.getInt("ID"));                
                obj.setImplClassPlugin(rs.getString("IMPLCLASS_PLUGIN"));                
                salida.add(obj);
            }
            
        }catch(SQLException e){
            log.error("Error al recuperar plugin almacenamiento por documentos externos enviados al portafirmas: " + e.getMessage());
            e.printStackTrace();
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return salida;
    } 
     
     
     
     /************* NUEVO **************/
     
     
     /**
      * Recupera la lista de plugin de almacenamiento disponibles
      * @param con: Conexión a la BBDD
      * @return ArrayList<PluginAlmacenamientoVO>
      */
     public ArrayList<PluginAlmacenamientoVO> getPluginAlmacenamientoDisponible(Connection con){
        ArrayList<PluginAlmacenamientoVO> salida = new ArrayList<PluginAlmacenamientoVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            String sql = "SELECT ID,NOMBRE,IMPLCLASS_PLUGIN,FECHA_ALTA FROM ALMACEN_DOC_DISPONIBLES ORDER BY ID";
            log.debug(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                PluginAlmacenamientoVO p = new PluginAlmacenamientoVO();
                p.setId(rs.getInt("ID"));
                p.setNombre(rs.getString("NOMBRE"));
                p.setImplClass(rs.getString("IMPLCLASS_PLUGIN"));                
                p.setFechaAlta(DateOperations.timestampToCalendar(rs.getTimestamp("FECHA_ALTA")));
                salida.add(p);
            }
            
        }catch(SQLException e){
            log.error("Error al recuperar plugin almacenamiento por documentos externos enviados al portafirmas: " + e.getMessage());
            e.printStackTrace();
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return salida;
    }  
     
     
    /**
     * Da de alta registro en la tabla PLUGIN_DOC_PROCEDIMIENTO
     * @param repositorio: ArrayList<RepositorioDocumentacionProcedimientoVO> con los datos a insertar
     * @param gestor: Tipo de gestor de base de datos
     * @param con: Conexión a la BBDD
     */
    public void grabarPluginPorProcedimiento(ArrayList<RepositorioDocumentacionProcedimientoVO> repositorio,String gestor,String conDigitalizacion,Connection con) throws SQLException{        
        PreparedStatement ps = null; 
        ResultSet rs = null;
        Calendar fechaActual = Calendar.getInstance();
        String sql = null;  
        StringBuilder sqlInto = null;  
        StringBuilder sqlValues = null;
        
        try{
            for(RepositorioDocumentacionProcedimientoVO repoProc : repositorio){  
                boolean existeRepoProc = false;
                sql = "SELECT COUNT(*) AS NUM FROM PLUGIN_DOC_PROCEDIMIENTO WHERE COD_PROCEDIMIENTO = ?";
                log.debug("¿Existe almacenamiento definidido para " + repoProc.getCodProcedimiento() + "? Query = " + sql);
                ps = con.prepareStatement(sql);
                ps.setString(1,repoProc.getCodProcedimiento());
                rs = ps.executeQuery();
                if(rs.next()){
                    if(rs.getInt("NUM")>0)
                        existeRepoProc = true;
                }
                rs.close();
                ps.close();
                
                if(existeRepoProc){ // Actualizamos
                    sql = "UPDATE PLUGIN_DOC_PROCEDIMIENTO SET IMPLCLASS_PLUGIN=?, ID_ALMACEN=?, FECHA_ALTA=?";
                    if(conDigitalizacion != null && conDigitalizacion.equalsIgnoreCase("si"))
                        sql += ",DIGIT_DOC_REGISTRO=?";
                    sql += " WHERE COD_PROCEDIMIENTO = ?";
                    log.debug("Actualizamos el almacenamiento definidido para " + repoProc.getCodProcedimiento() + ". Query = " + sql);
                    ps = con.prepareStatement(sql);
                    int index = 1;
                    ps.setString(index++, repoProc.getImplClassPlugin());
                    ps.setInt(index++, repoProc.getIdPlugin());
                    ps.setTimestamp(index++,DateOperations.toTimestamp(fechaActual));
                    if(conDigitalizacion != null && conDigitalizacion.equalsIgnoreCase("si")){
                        String digitalizacion = repoProc.getDigitalizacion();
                        if(digitalizacion == null || digitalizacion.equals("") || digitalizacion.equals("0"))
                            digitalizacion = "NO";

                        ps.setString(index++,digitalizacion);
                    }
                    ps.setString(index++, repoProc.getCodProcedimiento());
                    int updatedRows = ps.executeUpdate();
                    log.debug("Número filas actualizadas: " + updatedRows);
                    ps.close();
                } else { // Insertamos
                    sqlInto = new StringBuilder("INSERT INTO PLUGIN_DOC_PROCEDIMIENTO(");
                    sqlValues = new StringBuilder(") VALUES(");
                    if(gestor.equalsIgnoreCase("ORACLE")){
                        sqlInto.append("ID,");
                        sqlValues.append("SEQ_PLUGIN_DOC_PROCEDIMIENTO.nextval,");
                    }

                    sqlInto.append("COD_PROCEDIMIENTO,IMPLCLASS_PLUGIN,FECHA_ALTA,ID_ALMACEN");
                    sqlValues.append("?,?,?,?");

                    if(conDigitalizacion != null && conDigitalizacion.equalsIgnoreCase("si")){
                        sqlInto.append(",DIGIT_DOC_REGISTRO");
                        sqlValues.append(",?");
                    }

                    sql = sqlInto.toString() + sqlValues.toString() + ")";
                    log.debug("Insertamos la definición de almacenamiento para " + repoProc.getCodProcedimiento() + ". Query = " + sql);
                    ps = con.prepareStatement(sql);
                    int index = 1;
                    ps.setString(index++, repoProc.getCodProcedimiento());
                    ps.setString(index++, repoProc.getImplClassPlugin());
                    ps.setTimestamp(index++,DateOperations.toTimestamp(fechaActual));
                    ps.setInt(index++, repoProc.getIdPlugin());
                    if(conDigitalizacion != null && conDigitalizacion.equalsIgnoreCase("si")){
                        String digitalizacion = repoProc.getDigitalizacion();
                        if(digitalizacion == null || digitalizacion.equals("") || digitalizacion.equals("0"))
                            digitalizacion = "NO";

                        ps.setString(index++,digitalizacion);
                    }
                    int insertedRows = ps.executeUpdate();
                    log.debug("Número filas insertadas: " + insertedRows);
                    ps.close();
                }
            }
            
            
        }catch(SQLException e){
            log.error("Error al recuperar plugin almacenamiento por documentos externos enviados al portafirmas: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }finally{
            try{
                if(rs!=null) rs.close();                
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        
    }

    
    
   public void grabarPluginPorRegistro(ArrayList<RepositorioDocumentacionRegistroVO> repositorio,String gestor,Connection con) throws SQLException{        
        PreparedStatement ps = null;        
        Calendar fechaActual = Calendar.getInstance();
        
        try{
            String sql = null;                        
            
            sql = "DELETE FROM PLUGIN_DOC_REGISTRO";
            ps = con.prepareStatement(sql);
            
            int rowsDeleted = ps.executeUpdate();
            
            if(gestor.equalsIgnoreCase("ORACLE")){
                sql = "INSERT INTO PLUGIN_DOC_REGISTRO(ID,IMPLCLASS_PLUGIN,ID_ALMACEN,FECHA_ALTA) " + 
                       "VALUES(SEQ_PLUGIN_DOC_REGISTRO.nextval,?,?,?)";
            }else
            if(gestor.equalsIgnoreCase("SQLSERVER")){
                sql = "INSERT INTO PLUGIN_DOC_REGISTRO(IMPLCLASS_PLUGIN,ID_ALMACEN,FECHA_ALTA) " + 
                      "VALUES(?,?,?)";
            }
            log.debug(sql);
                
            for(int i=0;i<repositorio.size();i++){                
                int z=1;
                ps = con.prepareStatement(sql);
                String implClass = (repositorio.get(i).getImplClassPlugin()!=null && repositorio.get(i).getImplClassPlugin().length()>0)?repositorio.get(i).getImplClassPlugin():ConstantesDatos.IMPLCLASS_PLUGIN_ALMACENAMIENTO_BBDD;                
                ps.setString(z++,implClass);
                ps.setInt(z++,repositorio.get(i).getIdAlmacen());                
                ps.setTimestamp(z++,DateOperations.toTimestamp(fechaActual));
                
                int rowsInserted = ps.executeUpdate();
                log.debug("Número filas insertadas: " + rowsInserted);
            }
            
            
        }catch(SQLException e){
            log.error("Error al recuperar plugin almacenamiento por documentos externos enviados al portafirmas: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        
    }   
    
    
    
    /**
     * Recupera los plugins de cada procedimiento existente en la organización
     * @param con: Conexión a la BBDD 
     * @return ArrayList<RepositorioDocumentacionProcedimientoVO>
     */
    public ArrayList<RepositorioDocumentacionRegistroVO> getListadoPluginRegistro(Hashtable<Integer,PluginAlmacenamientoVO> plugins,Connection con){
        ArrayList<RepositorioDocumentacionRegistroVO> salida = new ArrayList<RepositorioDocumentacionRegistroVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{            
            String sql = "SELECT ID,IMPLCLASS_PLUGIN,ID_ALMACEN,FECHA_ALTA FROM PLUGIN_DOC_REGISTRO";                                                 
            log.debug(sql);
            
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            RepositorioDocumentacionRegistroVO obj = null;
            
            while(rs.next()){                             
                obj = new RepositorioDocumentacionRegistroVO(); 
                obj.setId(rs.getInt("ID"));               
                obj.setImplClassPlugin(rs.getString("IMPLCLASS_PLUGIN"));   
                obj.setIdAlmacen(rs.getInt("ID_ALMACEN"));
                obj.setFechaAlta(DateOperations.timestampToCalendar(rs.getTimestamp("FECHA_ALTA")));
                                
                PluginAlmacenamientoVO plugin = plugins.get(obj.getIdAlmacen());
                obj.setNombrePlugin(plugin.getNombre());                                
            }       
            // Sino se ha recuperado el plugin de base de datos, se asigna el plugin genérico de almacenamiento
            // que es el de base de datos
            if(obj==null){
                obj = new RepositorioDocumentacionRegistroVO();
                // Al no tener plugin, se le asigna el de por defecto que es el de base de datos.
                obj.setIdAlmacen(ConstantesDatos.ID_PLUGIN_IMPLCLASS_PLUGIN_ALMACENAMIENTO_BBDD);
                obj.setImplClassPlugin(ConstantesDatos.IMPLCLASS_PLUGIN_ALMACENAMIENTO_BBDD);
                obj.setNombrePlugin(ConstantesDatos.NOMBRE_PLUGIN_ALMACENAMIENTO_BBDD);                                   
            } 
            
            salida.add(obj);          
            
           
        }catch(SQLException e){
            log.error("Error al recuperar plugin almacenamiento por procedimiento: " + e.getMessage());
            e.printStackTrace();
            
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return salida;
    }
}
