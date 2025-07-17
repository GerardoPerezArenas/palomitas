package es.altia.flexia.business.documentospresentados.persistence;

import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.util.commons.DateOperations;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * Operaciones sobre la tabla E_DOC_PRESENTADOS
 * @author oscar.rodriguez
 */
public class ExpedienteDocPresentadoDAO {

    private static ExpedienteDocPresentadoDAO instance;
    private Logger log = Logger.getLogger(ExpedienteDocPresentadoDAO.class);
    private ExpedienteDocPresentadoDAO(){}

    public static ExpedienteDocPresentadoDAO getInstance(){
        if(instance==null)
            instance = new ExpedienteDocPresentadoDAO();

        return instance;
    }

    
    private int getCodigoMaximo(Connection con){
        Statement st = null;
        ResultSet rs = null;
        int salida = 1;

        try{
            String sql = "SELECT MAX(PRESENTADO_COD) AS NUM FROM E_DOCS_PRESENTADOS";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                salida = rs.getInt("NUM") + 1;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return salida;
    } 


    /**
     * Comprueba si un documento de expediente está marcado como presentado, si no lo está lo marca como tal
     * @param doc: Objeto con la información necesaria del documento
     * @param con: Conexión a la base de datos
     * @return True si todo bien y false en caso contrario
     */
    public boolean comprobarDocumentoPresentado(Documento doc,Connection con){
        boolean exito = false;
        PreparedStatement ps  =null;
        ResultSet rs = null;

        try{
            // Se comprueba primero si el documento figura como presentado, sino lo está habrá que guardarlo como tal
            String sql  ="SELECT COUNT(*) AS NUM FROM E_DOE WHERE " +
                         "DOE_MUN=? AND DOE_EJE=? AND DOE_NUM=? AND DOE_PRO=? AND DOE_COD=?";

            int i=1;
            ps = con.prepareStatement(sql);            
            ps.setInt(i++, doc.getCodMunicipio());
            ps.setInt(i++,doc.getEjercicio());
            ps.setString(i++,doc.getNumeroExpediente());
            ps.setString(i++,doc.getCodProcedimiento());
            ps.setInt(i++, doc.getCodDocumento());

            boolean docPresentado =false;
            rs = ps.executeQuery();
            while(rs.next()){
                int num = rs.getInt("NUM");
                if(num==1) docPresentado= true;
            }
            ps.close();
            rs.close();

            if(!docPresentado){
                // Se marca el documento como presentado
                sql = "INSERT INTO E_DOE(DOE_MUN,DOE_EJE,DOE_NUM,DOE_PRO,DOE_COD,DOE_FEC) VALUES(?,?,?,?,?,?)";
                ps = con.prepareStatement(sql);
                i=1;
                ps.setInt(i++, doc.getCodMunicipio());
                ps.setInt(i++,doc.getEjercicio());
                ps.setString(i++,doc.getNumeroExpediente());
                ps.setString(i++,doc.getCodProcedimiento());
                ps.setInt(i++, doc.getCodDocumento());
                ps.setTimestamp(i++, DateOperations.toSQLTimestamp(Calendar.getInstance()));
                ps.executeUpdate();
            }
            exito = true;
           
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

        return exito;
    }


    public boolean setDocumentoPresentado(Documento doc,Connection con){
        PreparedStatement ps = null;
        boolean exito = false;

        try{
            // Se comprueba primero si el documento figura como presentado, sino lo está habrá que guardarlo como tal
            if(comprobarDocumentoPresentado(doc, con)){
                
                String sql = "";
                String gestor = ResourceBundle.getBundle("techserver").getString("CON.gestor");
                if(gestor.equalsIgnoreCase("ORACLE")) { 
                    sql = "INSERT INTO E_DOCS_PRESENTADOS(PRESENTADO_COD,PRESENTADO_MUN,PRESENTADO_EJE,PRESENTADO_NUM," +
                          "PRESENTADO_PRO,PRESENTADO_COD_DOC,PRESENTADO_CONTENIDO,PRESENTADO_TIPO,PRESENTADO_EXTENSION," +
                          "PRESENTADO_ORIGEN,PRESENTADO_FECHA_ALTA,PRESENTADO_NOMBRE,PRESENTADO_COD_USU_ALTA) VALUES(SEQ_E_DOCS_PRESENTADOS.nextval,?,?,?,?,?,?,?,?,?,?,?,?)";                                        
                }else 
                if(gestor.equalsIgnoreCase("SQLSERVER")) {                 
                   sql = "INSERT INTO E_DOCS_PRESENTADOS(PRESENTADO_COD,PRESENTADO_MUN,PRESENTADO_EJE,PRESENTADO_NUM," +
                        "PRESENTADO_PRO,PRESENTADO_COD_DOC,PRESENTADO_CONTENIDO,PRESENTADO_TIPO,PRESENTADO_EXTENSION," +
                        "PRESENTADO_ORIGEN,PRESENTADO_FECHA_ALTA,PRESENTADO_NOMBRE,PRESENTADO_COD_USU_ALTA) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                    
                }
                
                /** original
                // No sólo se marca el documento como presentado, sino que también se le adjunta un archivo.
                String sql = "INSERT INTO E_DOCS_PRESENTADOS(PRESENTADO_COD,PRESENTADO_MUN,PRESENTADO_EJE,PRESENTADO_NUM," +
                        "PRESENTADO_PRO,PRESENTADO_COD_DOC,PRESENTADO_CONTENIDO,PRESENTADO_TIPO,PRESENTADO_EXTENSION," +
                        "PRESENTADO_ORIGEN,PRESENTADO_FECHA_ALTA,PRESENTADO_NOMBRE,PRESENTADO_COD_USU_ALTA) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        **/
                ps = con.prepareStatement(sql);
                int i=1;
              
                if (gestor.equalsIgnoreCase("SQLSERVER")) {
                    ps.setInt(i++, getCodigoMaximo(con));
                }

                ps.setInt(i++, doc.getCodMunicipio());
                ps.setInt(i++,doc.getEjercicio());
                ps.setString(i++,doc.getNumeroExpediente());
                ps.setString(i++,doc.getCodProcedimiento());
                ps.setInt(i++, doc.getCodDocumento());

                if(doc.getFichero()==null || doc.getFichero().length==0)
                    ps.setNull(i++,java.sql.Types.LONGVARBINARY);
                else{
                    java.io.InputStream st = new java.io.ByteArrayInputStream(doc.getFichero());
                    ps.setBinaryStream(i++, st, doc.getFichero().length);
                }

                ps.setString(i++,doc.getTipoMimeContenido());
                ps.setString(i++,doc.getExtension());
                ps.setString(i++,doc.getOrigen());
                ps.setTimestamp(i++, DateOperations.toSQLTimestamp(Calendar.getInstance()));
                ps.setString(i++, doc.getNombreDocumento());
                ps.setInt(i++,doc.getCodUsuario());

                int rowsInserted = ps.executeUpdate();
                if(rowsInserted==1) exito = true;
          }
          
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(ps!=null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return exito;
    }


    /**
     * Recupera un documento que ya ha sido presentado, incluido su contenido
     * @param doc: Datos necesario para recuperar el documento
     * @param con: Conexión a la base de datos
     * @return Documento
     */
     public Documento getDocumentoPresentado(Documento doc,Connection con){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            
            String sql = "";
            
            if(!doc.isExpHistorico()) {
                sql = "SELECT PRESENTADO_TIPO, PRESENTADO_CONTENIDO,PRESENTADO_EXTENSION, PRESENTADO_NOMBRE " +
                      "FROM E_DOCS_PRESENTADOS WHERE " +
                      "PRESENTADO_MUN=? AND PRESENTADO_EJE=? AND PRESENTADO_NUM=? AND " +
                      "PRESENTADO_PRO=? AND PRESENTADO_COD_DOC=?";
            } else {
                sql = "SELECT PRESENTADO_TIPO, PRESENTADO_CONTENIDO,PRESENTADO_EXTENSION, PRESENTADO_NOMBRE " +
                      "FROM HIST_E_DOCS_PRESENTADOS WHERE " +
                      "PRESENTADO_MUN=? AND PRESENTADO_EJE=? AND PRESENTADO_NUM=? AND " +
                      "PRESENTADO_PRO=? AND PRESENTADO_COD_DOC=?";                
            }
            
            /** original
            String sql = "SELECT PRESENTADO_TIPO, PRESENTADO_CONTENIDO,PRESENTADO_EXTENSION, PRESENTADO_NOMBRE " +
                              "FROM E_DOCS_PRESENTADOS WHERE " +
                              "PRESENTADO_MUN=? AND PRESENTADO_EJE=? AND PRESENTADO_NUM=? AND " +
                              "PRESENTADO_PRO=? AND PRESENTADO_COD_DOC=?";
                              */
            ps = con.prepareStatement(sql);
            int i=1;            
            ps.setInt(i++, doc.getCodMunicipio());
            ps.setInt(i++,doc.getEjercicio());
            ps.setString(i++,doc.getNumeroExpediente());
            ps.setString(i++,doc.getCodProcedimiento());
            ps.setInt(i++, doc.getCodDocumento());
                        
            rs = ps.executeQuery();
            byte[] contenido = null;
            while(rs.next()){
                java.io.InputStream st = rs.getBinaryStream("PRESENTADO_CONTENIDO");
                java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                int c;
                if (st != null) {
                     while ((c = st.read()) != -1) {
                         ot.write(c);
                     }
                
                ot.flush();
                contenido = ot.toByteArray();
                ot.close();
                st.close();
                }
                doc.setFichero(contenido);
                doc.setTipoMimeContenido(rs.getString("PRESENTADO_TIPO"));
                doc.setExtension(rs.getString("PRESENTADO_EXTENSION"));
                doc.setNombreDocumento(rs.getString("PRESENTADO_NOMBRE"));
            }
            

        }catch(SQLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return doc;
    }


     public Documento getContenidoDocumentoPresentado(Documento doc,Connection con){
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[] contenido = null;
        try{
            String sql = "SELECT PRESENTADO_TIPO, PRESENTADO_CONTENIDO,PRESENTADO_EXTENSION, PRESENTADO_NOMBRE " +
                              "FROM E_DOCS_PRESENTADOS WHERE " +
                              "PRESENTADO_MUN=? AND PRESENTADO_EJE=? AND PRESENTADO_NUM=? AND " +
                              "PRESENTADO_PRO=? AND PRESENTADO_COD=?";

            ps = con.prepareStatement(sql);
            int i=1;            
            ps.setInt(i++, doc.getCodMunicipio());
            ps.setInt(i++,doc.getEjercicio());
            ps.setString(i++,doc.getNumeroExpediente());
            ps.setString(i++,doc.getCodProcedimiento());
            ps.setInt(i++, doc.getCodDocumento());
                        
            rs = ps.executeQuery();            
            while(rs.next()){
                java.io.InputStream st = rs.getBinaryStream("PRESENTADO_CONTENIDO");
                java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                int c;
                if (st != null) {
                     while ((c = st.read()) != -1) {
                         ot.write(c);
                     }
                
                ot.flush();
                contenido = ot.toByteArray();
                ot.close();
                st.close();
                }
                doc.setFichero(contenido);
                doc.setExtension(rs.getString("PRESENTADO_EXTENSION"));
                doc.setTipoMimeContenido(rs.getString("PRESENTADO_TIPO"));
                doc.setNombreDocumento(rs.getString("PRESENTADO_NOMBRE"));
            }
            

        }catch(SQLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return doc;
    }


     /**
      * Elimina un documento ya presentado de la base de datos
      * @param doc: Documento con los datos del documento de expediente a eliminar
      * @param con: Conexión a la base de datos
      * @return True si todo bien y false en caso contrario
      */
     public boolean eliminarDocumentoPresentado(Documento doc,Connection con){
        PreparedStatement ps = null;
        boolean exito = false;

        try{          

            String sql = null;
            if(doc.isEliminarSoloAdjunto()){
                // Sólo se elimina el adjunto y el documento siga apareciendo como presentado
                sql = "DELETE FROM E_DOCS_PRESENTADOS WHERE PRESENTADO_MUN=? AND PRESENTADO_EJE=? AND PRESENTADO_NUM=? AND " +
                                  "PRESENTADO_PRO=? AND PRESENTADO_COD_DOC=?";
            }else{
                // Se desmarca el documento como presentado. Si tuviese asignado un fichero adjunto se elimina por cascada de la tabla E_DOCS_PRESENTADOS
                sql = "DELETE FROM E_DOE WHERE DOE_MUN=? AND DOE_EJE=? AND DOE_NUM=? AND DOE_PRO=? AND DOE_COD=? AND DOE_FEC IS NOT NULL";
            }

            ps = con.prepareStatement(sql);
            int i=1;
            ps.setInt(i++, doc.getCodMunicipio());
            ps.setInt(i++,doc.getEjercicio());
            ps.setString(i++,doc.getNumeroExpediente());
            ps.setString(i++,doc.getCodProcedimiento());
            ps.setInt(i++, doc.getCodDocumento());

            int rowsDeleted = ps.executeUpdate();
            if(rowsDeleted==1) exito = true;

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(ps!=null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return exito;
    }


     /**
      * Modifica el contenido de un fichero adjunto a un documento de expediente en base de datos
      * @param doc: Objeto con la información necesaria del documento
      * @param con: Conexión a la base de datos
      * @return True si todo bien y false en caso contrario
      */
     public boolean modificarDocumentoPresentado(Documento doc,Connection con){
        PreparedStatement ps = null;
        boolean exito = false;

        try{
                
                String sql = "UPDATE E_DOCS_PRESENTADOS SET PRESENTADO_CONTENIDO=?,PRESENTADO_TIPO=?,PRESENTADO_EXTENSION=?, " +
                                  "PRESENTADO_FECHA_MOD=?, PRESENTADO_COD_USU_MOD=?, PRESENTADO_NOMBRE=? WHERE " +
                                  "PRESENTADO_MUN=? AND PRESENTADO_EJE=? AND PRESENTADO_NUM=? AND " +
                                  "PRESENTADO_PRO=? AND PRESENTADO_COD_DOC=?";
                                
                log.debug("sql: " + sql);
                
                ps = con.prepareStatement(sql);
                int i=1;
                
               if(doc.getFichero()==null || doc.getFichero().length==0)
                    ps.setNull(i++,java.sql.Types.LONGVARBINARY);
                else{
                    java.io.InputStream st = new java.io.ByteArrayInputStream(doc.getFichero());
                    ps.setBinaryStream(i++, st, doc.getFichero().length);
                }
                ps.setString(i++,doc.getTipoMimeContenido());
                ps.setString(i++,doc.getExtension());
                ps.setTimestamp(i++, DateOperations.toSQLTimestamp(Calendar.getInstance()));
                ps.setInt(i++,doc.getCodUsuario());
                ps.setString(i++,doc.getNombreDocumento());
                ps.setInt(i++, doc.getCodMunicipio());
                ps.setInt(i++,doc.getEjercicio());
                ps.setString(i++,doc.getNumeroExpediente());
                ps.setString(i++,doc.getCodProcedimiento());
                ps.setInt(i++, doc.getCodDocumento());

                int rowsInserted = ps.executeUpdate();
                if(rowsInserted==1) exito = true;
        

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(ps!=null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return exito;
    }
     
     
     /**
      * Recupera el nombre, extensión y tipo mime de un documento de inicio de expediente
      * @param doc: Objeto que implementa la interfaz Documento, con la información del documento a consultar      
      * @param con: Conexión a la BBDD
      * @return Objeto que implementa la interfaz Documento con los datos recuperados
      */
     public Documento getNombreDocumentoPresentado(Documento doc,Connection con){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String sql = "";
            
            if(!doc.isExpHistorico()) { 
                sql = "SELECT PRESENTADO_TIPO,PRESENTADO_EXTENSION, PRESENTADO_NOMBRE " +
                      "FROM E_DOCS_PRESENTADOS WHERE " +
                      "PRESENTADO_MUN=? AND PRESENTADO_EJE=? AND PRESENTADO_NUM=? AND " +
                      "PRESENTADO_PRO=? AND PRESENTADO_COD_DOC=?";
            } else {
                sql = "SELECT PRESENTADO_TIPO,PRESENTADO_EXTENSION, PRESENTADO_NOMBRE " +
                      "FROM HIST_E_DOCS_PRESENTADOS WHERE " +
                      "PRESENTADO_MUN=? AND PRESENTADO_EJE=? AND PRESENTADO_NUM=? AND " +
                      "PRESENTADO_PRO=? AND PRESENTADO_COD_DOC=?";
            }

            ps = con.prepareStatement(sql);
            int i=1;            
            ps.setInt(i++, doc.getCodMunicipio());
            ps.setInt(i++,doc.getEjercicio());
            ps.setString(i++,doc.getNumeroExpediente());
            ps.setString(i++,doc.getCodProcedimiento());
            ps.setInt(i++, doc.getCodDocumento());
                        
            rs = ps.executeQuery();
            byte[] contenido = null;
            while(rs.next()){                
                doc.setTipoMimeContenido(rs.getString("PRESENTADO_TIPO"));
                doc.setExtension(rs.getString("PRESENTADO_EXTENSION"));
                doc.setNombreDocumento(rs.getString("PRESENTADO_NOMBRE"));
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return doc;
    } 
}