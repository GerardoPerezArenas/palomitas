package es.altia.agora.business.portafirmas.persistence.manual;

import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoOtroFirmaVO;
import es.altia.agora.business.util.GlobalNames;
import es.altia.util.commons.DateOperations;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.log4j.Logger;

/**
 *
 * @author oscar.rodriguez
 */
public class EDocExtPortafirmasDAO {

    private static EDocExtPortafirmasDAO instance = null;
    private Logger log = Logger.getLogger(EDocExtPortafirmasDAO.class);
    public static final String ESTADO_PENDIENTE_DOCUMENTO = "O";
    public static final String ESTADO_FIRMADO_DOCUMENTO = "F";
    public static final String ESTADO_RECHAZADO_DOCUMENTO = "R";

    private EDocExtPortafirmasDAO(){};
    
    public static EDocExtPortafirmasDAO getInstance(){
        if(instance==null)
            instance = new EDocExtPortafirmasDAO();
        
        return instance;
    }

   /**
      * Recupera los documentos externos al sistema enviados al portafirmas.
      *  @param usuarioDelegado: Tabla hash con el nif y código del usuario en el que se ha delegado la firma de un documento por parte del usuario que tiene
      * como NIF el pasado en el parámetro nifUsuario. Si esta a null quiere decir que no se ha delegado la firma en ningún usuario.
      * @param estado: Estado del documento: O = Pendiente, R = Rechazado, F = Finalizado. Si está a NULL se recupera todos los documentos independientemente
      * del estado de los mismos.
      * @param nifUsuario: Nif del usuario que puede firmar el documento      
      * @param con: Conexión a la base de datos
      * @return ArrayList<DocumentoOtroFirmaVO>
      */
      public ArrayList<DocumentoOtroFirmaVO> getDocumentoExternosPortafirmas(String nifUsuarioDelegado,String nifUsuarioActual,String estado,Connection con){
        ResultSet rs = null;
        Statement st = null;

        ArrayList<DocumentoOtroFirmaVO> documentos = new ArrayList<DocumentoOtroFirmaVO>();
        try{
            String sql = "SELECT PORTAFIRMAS_COD,PORTAFIRMAS_EJERCICIO,PORTAFIRMAS_FECALTA, " +
                              "PORTAFIRMAS_ESTADOFIRMA,PORTAFIRMAS_DESCRIPCION,PORTAFIRMAS_EXTENSION,PORTAFIRMAS_TIPOMIME, " +
                              "PORTAFIRMAS_DOCUMENTO_USUARIO,PORTAFIRMAS_CODUSU_MOD,PORTAFIRMAS_FEC_MODIFICACION " +
                              "FROM E_DOC_EXT_PORTAFIRMAS WHERE  ";

            if(nifUsuarioActual!=null && nifUsuarioDelegado!=null){
                sql+= " PORTAFIRMAS_DOCUMENTO_USUARIO IN ('" + nifUsuarioDelegado + "','" + nifUsuarioActual + "')";
            }else
            if(nifUsuarioActual!=null && nifUsuarioDelegado==null){
                sql+= " PORTAFIRMAS_DOCUMENTO_USUARIO IN ('" + nifUsuarioActual + "')";
            }
                        
            if(estado!=null && !"".equals(estado)){
                // Se filtra los expedientes por estado
                sql+= " AND PORTAFIRMAS_ESTADOFIRMA='" + estado + "'";
            }            
         
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
            
                int codDocumento = rs.getInt("PORTAFIRMAS_COD");
                int ejercicio          = rs.getInt("PORTAFIRMAS_EJERCICIO");                
                java.sql.Timestamp tFechaEnvioFirma    = rs.getTimestamp("PORTAFIRMAS_FECALTA");

                String estadoFirma   = rs.getString("PORTAFIRMAS_ESTADOFIRMA");
                String nombreDocumento = rs.getString("PORTAFIRMAS_DESCRIPCION");
                String extensionDocumento = rs.getString("PORTAFIRMAS_EXTENSION");
                String tipoMime   = rs.getString("PORTAFIRMAS_TIPOMIME");
                String nif  = rs.getString("PORTAFIRMAS_DOCUMENTO_USUARIO");
                String codUsuarioMod = rs.getString("PORTAFIRMAS_CODUSU_MOD");
                java.sql.Timestamp tFechaFirma  = rs.getTimestamp("PORTAFIRMAS_FEC_MODIFICACION");
               
                DocumentoOtroFirmaVO doc = new DocumentoOtroFirmaVO();
                doc.setCodigoDocumento(codDocumento);
                doc.setNifUsuario(nif);                
                doc.setCodigoUsuarioFirma(codUsuarioMod);
                doc.setEjercicio(ejercicio);
                doc.setEstadoFirma(estadoFirma);
                doc.setExtension(extensionDocumento);
                doc.setFechaEnvioFirma(DateOperations.timestampToCalendar(tFechaEnvioFirma));
                doc.setFechaFirma(DateOperations.timestampToCalendar(tFechaFirma));
                doc.setTipoMime(tipoMime);
                doc.setNombreDocumento(nombreDocumento);
                documentos.add(doc);

            }//while
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return documentos;
    }




     public Hashtable<String,String> getUsuarioDelegado(int codigoUsuario,Connection con){
        Hashtable<String,String> salida = null;
        Statement st = null;
        ResultSet rs = null;

        try{


            /*
            String SQL ="SELECT USU_FIR_DEL.USU_COD,USU_FIR_DEL.USU_DELEGADO,A_USU.USU_NIF FROM " + GlobalNames.ESQUEMA_GENERICO + "USU_FIR_DEL," + GlobalNames.ESQUEMA_GENERICO + "A_USU " +
                              "WHERE USU_FIR_DEL.USU_COD=A_USU.USU_COD " +
                              "AND USU_FIR_DEL.USU_DELEGADO=" + codigoUsuario; */

            String SQL ="SELECT USU_COD,USU_DELEGADO FROM " + GlobalNames.ESQUEMA_GENERICO + "USU_FIR_DEL " +
                              "WHERE USU_DELEGADO=" + codigoUsuario;
            
            log.debug(SQL);            
            st = con.createStatement();
            rs = st.executeQuery(SQL);

           String usuQueDelega = null;
           String usuDelegado = null;
           /** SE COMPRUEBA SI AL USUARIO ACTUAL SE LE HA DELEGADO LA FIRMA POR PARTE DE ALGÚN OTRO USUARIO */
            while(rs.next()){
                
                usuQueDelega = rs.getString("USU_COD");
                usuDelegado = rs.getString("USU_DELEGADO");
                //String usuNif           = rs.getString("USU_NIF");
                salida = new Hashtable<String,String>();
                salida.put("COD_USUARIO_QUE_DELEGA",usuQueDelega);
                salida.put("COD_USUARIO_DELEGADO", usuDelegado);
                //salida.put("NIF_USUARIO_DELEGADO",usuNif);
                salida.put("DELEGACION_FIRMA","SI");
            }

            st.close();
            rs.close();
            
            // Se recupera el nif del usuario delegado si lo hubiera
            st = con.createStatement();                        
            SQL = "SELECT USU_NIF FROM " + GlobalNames.ESQUEMA_GENERICO + " A_USU WHERE USU_COD=" + usuDelegado;
            rs = st.executeQuery(SQL);
            while(rs.next()){
                String nifUsuarioDelegado = rs.getString("USU_NIF");
                salida.put("NIF_USUARIO_DELEGADO",nifUsuarioDelegado);
            }
            
            st.close();
            rs.close();


            /****  Se obtiene el nif del usuario que ha delegado la firma en el usuario actual *****/
            if(usuQueDelega!=null && salida!=null){
                // Si al usuario actual le han delegado la firma => Hay que recuperar el DNI este usuario.
                SQL = "SELECT USU_NIF FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE USU_COD=" + usuQueDelega;
                st = con.createStatement();
                rs = st.executeQuery(SQL);
                while(rs.next()){
                    String nifUsuarioQueDelega = rs.getString("USU_NIF");
                    if(nifUsuarioQueDelega!=null && nifUsuarioQueDelega.length()>0)
                        salida.put("NIF_USUARIO_QUE_DELEGA",nifUsuarioQueDelega);
                }
            }//if
            else{
                // Como no se ha delegado en el usuario actual la firma => Se recupera su nif
                SQL = "SELECT USU_NIF FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE USU_COD=" + codigoUsuario;
                st = con.createStatement();
                rs = st.executeQuery(SQL);
                while(rs.next()){
                    String nifUsuarioActual = rs.getString("USU_NIF");
                    if(nifUsuarioActual!=null && nifUsuarioActual.length()>0){
                        salida = new Hashtable<String, String>();
                        salida.put("DELEGACION_FIRMA","NO");
                        salida.put("COD_USUARIO_QUE_DELEGA","-1");
                        salida.put("COD_USUARIO_DELEGADO", "-1");
                        salida.put("NIF_USUARIO_DELEGADO","-1");
                        salida.put("NIF_USUARIO_QUE_DELEGA","-1");
                        salida.put("NIF_USUARIO_ACTUAL",nifUsuarioActual);
                    }//if
                }//while                
            }


        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return salida;
     }


     /**
      * Recupera el contenido de un documento externo enviado al portafirmas
      * @param codigoDocumento: Código del documento
      * @param con:  Conexión a la base de datos
      * @return DocumentoFirmaVO con el contenido del documento
      */
     public DocumentoOtroFirmaVO getDocumento(String codigoDocumento,Connection con){
        DocumentoOtroFirmaVO documento = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT PORTAFIRMAS_TIPOMIME,PORTAFIRMAS_CONTENIDO,PORTAFIRMAS_EXTENSION,PORTAFIRMAS_DESCRIPCION, "
                    + "PORTAFIRMAS_FIRMA, PORTAFIRMAS_ESTADOFIRMA"
                    + ",PORTAFIRMAS_OBSERVACIONES,PORTAFIRMAS_COD, PORTAFIRMAS_CODUSU_MOD FROM E_DOC_EXT_PORTAFIRMAS WHERE PORTAFIRMAS_COD=" + codigoDocumento;

            log.debug("getDocumento SQL: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                documento = new DocumentoOtroFirmaVO();

                byte[] contenido = null;
                // Se lee el contenido binario del documento
                java.io.InputStream stream = rs.getBinaryStream("PORTAFIRMAS_CONTENIDO");
                java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                int c;
                if (stream != null) {
                     while ((c = stream.read()) != -1) {
                         ot.write(c);
                     }
                }
                ot.flush();
                contenido = ot.toByteArray();
                ot.close();

                documento.setContenido(contenido);
                documento.setTipoMime(rs.getString("PORTAFIRMAS_TIPOMIME"));
                documento.setNombreDocumento(rs.getString("PORTAFIRMAS_DESCRIPCION"));
                documento.setExtension(rs.getString("PORTAFIRMAS_EXTENSION"));
                documento.setObservaciones(rs.getString("PORTAFIRMAS_OBSERVACIONES"));
                documento.setCodigoUsuarioFirma(rs.getString("PORTAFIRMAS_CODUSU_MOD"));
                documento.setEstadoFirma(rs.getString("PORTAFIRMAS_ESTADOFIRMA"));
                documento.setCodigoDocumento(rs.getInt("PORTAFIRMAS_COD"));
                
                contenido = null;
                stream = null;
                c = 0;
                // Se lee el contenido binario del documento
                stream = rs.getBinaryStream("PORTAFIRMAS_FIRMA");

                if (rs.wasNull()) {
                    documento.setFirma(null);
                } else {
                    ot = new java.io.ByteArrayOutputStream();
                    if (stream != null) {
                        while ((c = stream.read()) != -1) {
                            ot.write(c);
                        }
                    }
                    ot.flush();
                    contenido = ot.toByteArray();
                    ot.close();
                    documento.setFirma(contenido);
                }
                
            }

        }catch(SQLException e){
            log.error(e.getMessage());
            e.printStackTrace();
        }catch(IOException e){
            log.error(e.getMessage());
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return documento;
    }



   /**
      * Guarda la firma de un documento externo en el portafirmas
      * @param doc: Objeto con el contenido necesario para almacenar la firma de documento externo y para cambiar el estado del documento a firmado
      * @param con:  Conexión a la base de datos
      * @return true si todo bien y false en caso contrario      */
     public boolean guardarFirma(DocumentoOtroFirmaVO doc,Connection con){        
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exito = false;
        try{

            String sql = "UPDATE E_DOC_EXT_PORTAFIRMAS SET PORTAFIRMAS_ESTADOFIRMA=?," +
                              "PORTAFIRMAS_CODUSU_MOD=?,PORTAFIRMAS_FEC_MODIFICACION=?,PORTAFIRMAS_FIRMA=? " +
                              "WHERE PORTAFIRMAS_COD=?";

            log.debug("guardarFirma SQL: " + sql);
            int i=1;
            ps = con.prepareStatement(sql);
            
            ps.setString(i++,this.ESTADO_FIRMADO_DOCUMENTO);
            ps.setInt(i++,Integer.parseInt(doc.getCodigoUsuarioFirma()));
            if(doc.getFechaFirma()!=null)
                ps.setTimestamp(i++,DateOperations.toTimestamp(doc.getFechaFirma()));
            else
                ps.setNull(i++,java.sql.Types.TIMESTAMP);

            // Contenido del fichero
            if(doc.getFirma()!=null && doc.getFirma().length>0){
                java.io.InputStream st = new java.io.ByteArrayInputStream(doc.getFirma());
                ps.setBinaryStream(i++, st, doc.getFirma().length);
            }else
                ps.setNull(i++, java.sql.Types.LONGVARBINARY);

            ps.setInt(i++,doc.getCodigoDocumento());
            
            int rowsUpdated = ps.executeUpdate();
            if(rowsUpdated==1) exito = true;

            log.debug("Número de filas actualizadas: " + rowsUpdated);

        }catch(SQLException e){
            log.error(e.getMessage());
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


      /**
      * Permite marcar como rechazado un documento externo enviado al portafirmas
      * @param doc: Objeto con el contenido necesario para almacenar la firma de documento externo y para cambiar el estado del documento a firmado
      * @param con:  Conexión a la base de datos
      * @return true si todo bien y false en caso contrario      */
     public boolean rechazarDocumento(DocumentoOtroFirmaVO doc,Connection con){
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exito = false;
        try{

            String sql = "UPDATE E_DOC_EXT_PORTAFIRMAS SET PORTAFIRMAS_ESTADOFIRMA=?," +
                              "PORTAFIRMAS_CODUSU_MOD=?,PORTAFIRMAS_FEC_MODIFICACION=?,PORTAFIRMAS_OBSERVACIONES=? " +
                              "WHERE PORTAFIRMAS_COD=?";

            log.debug("guardarFirma SQL: " + sql);
            int i=1;
            ps = con.prepareStatement(sql);

            ps.setString(i++,this.ESTADO_RECHAZADO_DOCUMENTO);
            ps.setInt(i++,Integer.parseInt(doc.getCodigoUsuarioFirma()));
            if(doc.getFechaFirma()!=null)
                ps.setTimestamp(i++,DateOperations.toTimestamp(doc.getFechaFirma()));
            else
                ps.setNull(i++,java.sql.Types.TIMESTAMP);
            ps.setString(i++,doc.getObservaciones());
            ps.setInt(i++,doc.getCodigoDocumento());

            int rowsUpdated = ps.executeUpdate();
            if(rowsUpdated==1) exito = true;

            log.debug("Número de filas actualizadas: " + rowsUpdated);

        }catch(SQLException e){
            log.error(e.getMessage());
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



   /**
      * Recupera información del documento como el nombre, extensión, tipo mime y el nif del usuario al que está dirigido el documento
      * @param codigoDocumento: Código del documento
      * @param con:  Conexión a la base de datos
      * @return DocumentoFirmaVO con la info indicada del documento
      */
     public DocumentoOtroFirmaVO getInfoDocumento(String codigoDocumento,Connection con){
        DocumentoOtroFirmaVO documento = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT PORTAFIRMAS_TIPOMIME,PORTAFIRMAS_EXTENSION,PORTAFIRMAS_DESCRIPCION,PORTAFIRMAS_DOCUMENTO_USUARIO FROM E_DOC_EXT_PORTAFIRMAS " +
                              "WHERE PORTAFIRMAS_COD=" + codigoDocumento;

            log.debug("getInfoDocumento SQL: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                documento = new DocumentoOtroFirmaVO();
                documento.setCodigoDocumento(Integer.parseInt(codigoDocumento));
                documento.setTipoMime(rs.getString("PORTAFIRMAS_TIPOMIME"));
                documento.setNombreDocumento(rs.getString("PORTAFIRMAS_DESCRIPCION"));
                documento.setExtension(rs.getString("PORTAFIRMAS_EXTENSION"));
                documento.setNifUsuario(rs.getString("PORTAFIRMAS_DOCUMENTO_USUARIO"));
            }

        }catch(SQLException e){
            log.error(e.getMessage());
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return documento;
     }


}