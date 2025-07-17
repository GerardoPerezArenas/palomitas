package es.altia.flexia.registro.justificante.persistence.bd;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.registro.justificante.vo.JustificanteRegistroPersonalizadoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import org.apache.log4j.Logger;

public class JustificanteRegistroPersonalizadoDAO {
    private Logger log = Logger.getLogger(JustificanteRegistroPersonalizadoDAO.class);
    private static JustificanteRegistroPersonalizadoDAO instance = null;

    private JustificanteRegistroPersonalizadoDAO(){}
    public static JustificanteRegistroPersonalizadoDAO getInstance(){
        if(instance==null)
            instance = new JustificanteRegistroPersonalizadoDAO();

        return instance;
    }


    /**
     * Da de alta un justificante de registro en la BBDD
     * @param justificante: Objeto de la clase JustificanteRegistroPersonalizadoVO
     * @param con: Cónexión a la BBDD
     * @return boolean
     * @throws java.sql.SQLException si ocurre algún error durante el acceso a la BBDD
     * @throws java.lang.Exception
     */
    public boolean altaJustificante(JustificanteRegistroPersonalizadoVO justificante, Connection con) throws SQLException,Exception{
        boolean exito = false;
        PreparedStatement ps = null;
        String sql;
        
        try{
            if(justificante.getTipoJustificante()==0){
                sql = "INSERT INTO JUSTIFICANTE_REG_PERSO(NOMBRE_JUSTIFICANTE,EXTENSION_JUSTIFICANTE,DESCRIPCION_JUSTIFICANTE,DEFECTO_JUSTIFICANTE,RUTA_JUSTIFICANTE) " +
                         "VALUES(?,?,?,?,?)";
                log.debug(sql);
            } else {
                sql = "INSERT INTO JUSTIF_REG_MODELO_PET_RESP(NOMBRE_MODELO,EXTENSION_MODELO,DESCRIPCION_MODELO,DEFECTO_MODELO,RUTA_MODELO) " +
                         "VALUES(?,?,?,?,?)";
                log.debug(sql);
            }
            
            ps = con.prepareStatement(sql);
            int i=1;
            ps.setString(i++,justificante.getNombreJustificante());
            ps.setString(i++,justificante.getExtensionJustificante());
            //ps.setLong(i++,justificante.getTamanhoJustificante());
            ps.setString(i++,justificante.getDescripcionJustificante());
            if(justificante.isDefecto()) ps.setInt(i++,1);
            else ps.setInt(i++,0);
            ps.setString(i++,justificante.getRutaDiscoJustificante());

            int rowsUpdated = ps.executeUpdate();
            log.debug("rowsUpdated: " + rowsUpdated);
            if(rowsUpdated==1) exito = true;
            
            ps.close();
            if(rowsUpdated==1){

                exito = true;
            }


        }catch(SQLException e){
            log.error("Error en operación altaJustificante: " + e.getMessage());
            throw e;
        }catch(Exception e){
            log.error("Error en operación altaJustificante: " + e.getMessage());
            throw e;
        }
        finally{
            try{
                if(ps!=null) ps.close();
            }catch(SQLException e){
                log.error("Erro en claúsula finally: " + e.getMessage());
            }
        }
        return exito;
    }



    /**
     * Recupera los justificante de registro existentes
     * @param con: Conexión a la BBDD
     * @return ArrayList<JustificanteRegistroPersonalizadoVO>
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
 public ArrayList<JustificanteRegistroPersonalizadoVO> getJustificantes(Connection con, Map<String, Boolean> tipoJustificante) throws SQLException,Exception{
        ArrayList<JustificanteRegistroPersonalizadoVO> justificantes = new ArrayList<JustificanteRegistroPersonalizadoVO>();
        Statement st = null;
        ResultSet rs = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        
        // Determinamos los tipos de justificante que se deben obtener	
        boolean tipoModeloPeticion = false;	
        if (tipoJustificante != null) {	
            tipoModeloPeticion = tipoJustificante.get(ConstantesDatos.TIPO_JUSTIFICANTE_MODELO_PETICION_RESPUESTA);	
        }

        try{
           
            String sql = "SELECT NOMBRE_JUSTIFICANTE,EXTENSION_JUSTIFICANTE,DESCRIPCION_JUSTIFICANTE, " +
                         "DEFECTO_JUSTIFICANTE,RUTA_JUSTIFICANTE FROM JUSTIFICANTE_REG_PERSO";
            

            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                JustificanteRegistroPersonalizadoVO j = new JustificanteRegistroPersonalizadoVO();
                int defecto = rs.getInt("DEFECTO_JUSTIFICANTE");
                j.setDefecto(false);
                j.setDescripcionDefecto(ConstantesDatos.JUSTIFICANTE_NO_ACTIVO);
                
                if(defecto==1){
                    j.setDefecto(true);
                    j.setDescripcionDefecto(ConstantesDatos.JUSTIFICANTE_ACTIVO);
                }
                
                j.setDescripcionJustificante(rs.getString("DESCRIPCION_JUSTIFICANTE"));
                j.setExtensionJustificante(rs.getString("EXTENSION_JUSTIFICANTE"));
                j.setNombreJustificante(rs.getString("NOMBRE_JUSTIFICANTE"));
                j.setRutaDiscoJustificante(rs.getString("RUTA_JUSTIFICANTE"));
                j.setTipoJustificante(0);
                justificantes.add(j);
            }
            
             // Cargar los justificantes de tipo modelos peticion respuesta	
            if (tipoModeloPeticion) {	
                String sql2 = "SELECT NOMBRE_MODELO,EXTENSION_MODELO,DESCRIPCION_MODELO, " +	
                             "DEFECTO_MODELO,RUTA_MODELO FROM JUSTIF_REG_MODELO_PET_RESP";		
                log.debug(sql2);	
                st2 = con.createStatement();	
                rs2 = st.executeQuery(sql2);	
                while(rs2.next()){	
                    JustificanteRegistroPersonalizadoVO j = new JustificanteRegistroPersonalizadoVO();	
                    int defecto = rs2.getInt("DEFECTO_MODELO");	
                    j.setDefecto(false);	
                    j.setDescripcionDefecto(ConstantesDatos.JUSTIFICANTE_NO_ACTIVO);		
                    if(defecto==1){	
                        j.setDefecto(true);	
                        j.setDescripcionDefecto(ConstantesDatos.JUSTIFICANTE_ACTIVO);	
                    }		
                    j.setDescripcionJustificante(rs2.getString("DESCRIPCION_MODELO"));	
                    j.setExtensionJustificante(rs2.getString("EXTENSION_MODELO"));	
                    j.setNombreJustificante(rs2.getString("NOMBRE_MODELO"));	
                    j.setRutaDiscoJustificante(rs2.getString("RUTA_MODELO")); 	
                    j.setTipoJustificante(1);	
                    justificantes.add(j);
                }

            }

        }catch(SQLException e){
            log.error("Error en operación getJustificantes: " + e.getMessage());
            throw e;
        }catch(Exception e){
            log.error("Error en operación getJustificantes: " + e.getMessage());
            throw e;
        }
        finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(st2!=null) st2.close();
                if(rs2!=null) rs2.close();
                
            }catch(SQLException e){
                log.error("Erro en claúsula finally: " + e.getMessage());
            }
        }
        return justificantes;
    }



     /**
     * Elimina un determinado justificante de BBDD
     * @param con: Conexión a la BBDD
     * @return boolean
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
     public boolean eliminarJustificante(JustificanteRegistroPersonalizadoVO justificante, Connection con) throws SQLException,Exception{
        boolean exito = false;
        PreparedStatement ps = null;
        String sql;

        try{
            if(justificante.getTipoJustificante()==0){
                sql = "DELETE FROM JUSTIFICANTE_REG_PERSO WHERE NOMBRE_JUSTIFICANTE=?";
                log.debug(sql);
            } else {
                sql = "DELETE FROM JUSTIF_REG_MODELO_PET_RESP WHERE NOMBRE_MODELO=?";
                log.debug(sql);
            }
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setString(i++,justificante.getNombreJustificante());            
            int rowsDeleted = ps.executeUpdate();
            if(rowsDeleted==1) exito = true;

        }catch(SQLException e){
            log.error("Error en operación eliminarJustificante: " + e.getMessage());
            throw e;
        }catch(Exception e){
            log.error("Error en operación eliminarJustificante: " + e.getMessage());
            throw e;
        }
        finally{
            try{
                if(ps!=null) ps.close();
                
            }catch(SQLException e){
                log.error("Erro en claúsula finally: " + e.getMessage());
            }
        }
        
        return exito;
    }



   /**
     * Comprueba si existe un determinado justificante con un mismo nombre en base de datos
     * @param con: Conexión a la BBDD
     * @return boolean
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
     public boolean existeJustificante(String nombreJustificante, Connection con) throws SQLException,Exception{
        boolean exito = false;
        Statement st = null;
        ResultSet rs = null;

        try{
            String sql = "SELECT COUNT(*) AS NUM FROM JUSTIFICANTE_REG_PERSO WHERE NOMBRE_JUSTIFICANTE='" + nombreJustificante + "'";
            log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;
            while(rs.next()){
                num = rs.getInt("NUM");
            }

            if(num>=1) exito = true;

        }catch(SQLException e){
            log.error("Error en operación existeJustificante: " + e.getMessage());
            throw e;
        }catch(Exception e){
            log.error("Error en operación existeJustificante: " + e.getMessage());
            throw e;
        }
        finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                log.error("Error en claúsula finally: " + e.getMessage());
            }
        }

        return exito;
    }


    /**
     * Activa o desactiva un determinado justificante como de registro
     * @param nombreJustificante: Nombre del justificante
     * @param tipo: toma los siguientes valores:
     *          0 --> Desactivado
     *          1 --> Activado
     * @param con: Conexión a la BBDD
     * @return true si activado o desactivado un justificante
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
     public boolean activarJustificante(JustificanteRegistroPersonalizadoVO justificante, int tipo,Connection con) throws SQLException,Exception{
        boolean exito = false;
        Statement st = null;
        
        try{            
            String sql = "UPDATE JUSTIFICANTE_REG_PERSO SET DEFECTO_JUSTIFICANTE=" + tipo + " WHERE NOMBRE_JUSTIFICANTE='" + justificante.getNombreJustificante() + "'";
            if(justificante.getTipoJustificante()==1)
                sql = "UPDATE JUSTIF_REG_MODELO_PET_RESP SET DEFECTO_MODELO=" + tipo + " WHERE NOMBRE_MODELO='" + justificante.getNombreJustificante() + "'";
            log.debug(sql);
            st = con.createStatement();
            int rowsUpdated = st.executeUpdate(sql);
            log.debug("Número de filas afectadas " + rowsUpdated);

            if(rowsUpdated==1) exito = true;


        }catch(SQLException e){
            log.error("Error en operación activarJustificante: " + e.getMessage());
            throw e;
        }catch(Exception e){
            log.error("Error en operación activarJustificante: " + e.getMessage());
            throw e;
        }
        finally{
            try{
                if(st!=null) st.close();
                
            }catch(SQLException e){
                log.error("Error en claúsula finally: " + e.getMessage());
            }
        }
        return exito;
        
    }// activarJustificante



   /**
     * Comprueba si existe algún justificante activo y devuelve su nombre en caso de que exista
     * @param con: Conexión a la BBDD
     * @return int: Nombre del justificante o null sino existe
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
     public String existeJustificanteActivo(int tipo, Connection con) throws SQLException,Exception{
        String salida = null;
        Statement st = null;
        ResultSet rs = null;

        try{
            // Se ponen todos los justificantes desactivados
            String sql = "SELECT NOMBRE_JUSTIFICANTE FROM JUSTIFICANTE_REG_PERSO WHERE DEFECTO_JUSTIFICANTE=1";
            if(tipo==1) 
                sql = "SELECT NOMBRE_MODELO FROM JUSTIF_REG_MODELO_PET_RESP WHERE DEFECTO_MODELO=1";
                
            log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                salida = rs.getString(1);
            }

        }catch(SQLException e){
            log.error("Error en operación existeJustificanteActivo: " + e.getMessage());
            throw e;
        }catch(Exception e){
            log.error("Error en operación existeJustificanteActivo: " + e.getMessage());
            throw e;
        }
        finally{
            try{
                if(st!=null) st.close();

            }catch(SQLException e){
                log.error("Error en claúsula finally: " + e.getMessage());
            }
        }
        return salida;

    }// existeJustificanteActivo


   /**
     * Recupera el justificante de registro que está activo si es que hay alguno
     * @param tipoInforme Tipo de informe por el que se pregunta: JUSTIFICANTE o MODELO_PETICION_RPTA
     * @param con: Conexión a la BBDD
     * @return JustificanteRegistroPersonalizadoVO o null si no está activo
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
     public JustificanteRegistroPersonalizadoVO getJustificanteActivo(String tipoInforme, Connection con) throws SQLException,Exception{
        JustificanteRegistroPersonalizadoVO salida = null;
        Statement st = null;
        ResultSet rs = null;
        String sql;
        String sufijo;

        try{
            if(tipoInforme.indexOf("peticion")!=-1){
                sql = "SELECT NOMBRE_MODELO,EXTENSION_MODELO,DESCRIPCION_MODELO, " +
                         "DEFECTO_MODELO,RUTA_MODELO FROM JUSTIF_REG_MODELO_PET_RESP " +
                         "WHERE DEFECTO_MODELO=1" ;
                sufijo = "MODELO";
            } else {
                sql = "SELECT NOMBRE_JUSTIFICANTE,EXTENSION_JUSTIFICANTE,DESCRIPCION_JUSTIFICANTE, " +
                         "DEFECTO_JUSTIFICANTE,RUTA_JUSTIFICANTE FROM JUSTIFICANTE_REG_PERSO " +
                         "WHERE DEFECTO_JUSTIFICANTE=1" ;
                sufijo = "JUSTIFICANTE";
            }

            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                salida = new JustificanteRegistroPersonalizadoVO();
                int defecto = rs.getInt("DEFECTO_"+sufijo);
                salida.setDefecto(false);
                salida.setDescripcionDefecto(ConstantesDatos.JUSTIFICANTE_NO_ACTIVO);

                if(defecto==1){
                    salida.setDefecto(true);
                    salida.setDescripcionDefecto(ConstantesDatos.JUSTIFICANTE_ACTIVO);
                }

                salida.setDescripcionJustificante(rs.getString("DESCRIPCION_"+sufijo));
                salida.setExtensionJustificante(rs.getString("EXTENSION_"+sufijo));
                salida.setNombreJustificante(rs.getString("NOMBRE_"+sufijo));
                salida.setRutaDiscoJustificante(rs.getString("RUTA_"+sufijo));                

            }

        }catch(SQLException e){
            log.error("Error en operación getJustificanteActivo: " + e.getMessage());
            throw e;
        }catch(Exception e){
            log.error("Error en operación getJustificanteActivo: " + e.getMessage());
            throw e;
        }
        finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                log.error("Erro en claúsula finally: " + e.getMessage());
            }
        }
        return salida;
    }


    /**
     * Recupera un determinado justificante
     * @param con: Conexión a la BBDD
     * @return JustificanteRegistroPersonalizadoVO o null si no está activo
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
     public JustificanteRegistroPersonalizadoVO getJustificante(String justificante,Connection con) throws SQLException,Exception{
        JustificanteRegistroPersonalizadoVO salida = null;
        Statement st = null;
        ResultSet rs = null;

        try{

            String sql = "SELECT NOMBRE_JUSTIFICANTE,EXTENSION_JUSTIFICANTE,DESCRIPCION_JUSTIFICANTE, " +
                         "DEFECTO_JUSTIFICANTE,RUTA_JUSTIFICANTE FROM JUSTIFICANTE_REG_PERSO " +
                         "WHERE NOMBRE_JUSTIFICANTE='" + justificante + "'" ;

            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                salida = new JustificanteRegistroPersonalizadoVO();
                int defecto = rs.getInt("DEFECTO_JUSTIFICANTE");
                salida.setDefecto(false);
                salida.setDescripcionDefecto(ConstantesDatos.JUSTIFICANTE_NO_ACTIVO);

                if(defecto==1){
                    salida.setDefecto(true);
                    salida.setDescripcionDefecto(ConstantesDatos.JUSTIFICANTE_ACTIVO);
                }

                salida.setDescripcionJustificante(rs.getString("DESCRIPCION_JUSTIFICANTE"));
                salida.setExtensionJustificante(rs.getString("EXTENSION_JUSTIFICANTE"));
                salida.setNombreJustificante(rs.getString("NOMBRE_JUSTIFICANTE"));
                salida.setRutaDiscoJustificante(rs.getString("RUTA_JUSTIFICANTE"));
                
            }

        }catch(SQLException e){
            log.error("Error en operación getJustificanteActivo: " + e.getMessage());
            throw e;
        }catch(Exception e){
            log.error("Error en operación getJustificanteActivo: " + e.getMessage());
            throw e;
        }
        finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                log.error("Erro en claúsula finally: " + e.getMessage());
            }
        }
        return salida;
    }


}