package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.administracion.ParametrosBDVO;
import es.altia.agora.business.administracion.mantenimiento.PermisoProcedimientosRestringidosVO;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import es.altia.util.cache.CacheDatosFactoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.SortedMap;
import org.apache.log4j.Logger;

/**
 *
 * @author oscar.rodriguez
 */
public class PermisoProcRestringidoDAO {
    private static PermisoProcRestringidoDAO instance=null;
    private Logger log = Logger.getLogger(PermisoProcRestringidoDAO.class);

    private PermisoProcRestringidoDAO(){
    }


    public static PermisoProcRestringidoDAO getInstance(){
        if(instance==null)
            instance = new PermisoProcRestringidoDAO();
        return instance;
    }



      /**
     * Recupera los procedimientos restringidos sobre los que tiene permiso un determinado usuario
     * procedimientos. Es necesario indicar que los procedimientos se recuperan aunque el usuario no tenga permisos sobre los mismos
     * @param codUsuario: Código interno del usuario en el sistema
     * @param codApp: Código de la aplicación
     * @param con: Conexión al esquema de BBDD a los que pertenecen los procedimientos
     * @return ArrayList<PermisoProcedimientosRestringidosVO>
     */
    public ArrayList<PermisoProcedimientosRestringidosVO> getProcedimientosRestringidosPermisoUsuario(String codUsuario,String codApp,String[] params,Connection con){
        Statement st = null;
        ResultSet rs = null;
        ArrayList<PermisoProcedimientosRestringidosVO> salida = new ArrayList<PermisoProcedimientosRestringidosVO>();

        /*
        SELECT PRO_COD, USUARIO_PROC_RESTRINGIDO.ORG_COD,USUARIO_PROC_RESTRINGIDO.ENT_COD,A_ORG.ORG_DES AS DESCORGANIZACION ,A_ENT.ENT_NOM AS DESCENTIDAD
FROM FLEXIA_GENERICO.USUARIO_PROC_RESTRINGIDO,FLEXIA_GENERICO.A_ORG,FLEXIA_GENERICO.A_ENT
WHERE USUARIO_PROC_RESTRINGIDO.USU_COD=5 AND USUARIO_PROC_RESTRINGIDO.ORG_COD = A_ORG.ORG_COD AND USUARIO_PROC_RESTRINGIDO.ENT_COD= A_ENT.ENT_COD
AND USUARIO_PROC_RESTRINGIDO.ORG_COD= A_ENT.ENT_ORG
                */

        String sql = "SELECT USUARIO_PROC_RESTRINGIDO.PRO_COD, USUARIO_PROC_RESTRINGIDO.ORG_COD,USUARIO_PROC_RESTRINGIDO.ENT_COD,A_ORG.ORG_DES AS DESCORGANIZACION ,A_ENT.ENT_NOM AS DESCENTIDAD" +
                          " FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO," + GlobalNames.ESQUEMA_GENERICO + "A_ORG," + GlobalNames.ESQUEMA_GENERICO + "A_ENT" +
                          " WHERE USUARIO_PROC_RESTRINGIDO.USU_COD=" + codUsuario +
                          " AND USUARIO_PROC_RESTRINGIDO.ORG_COD = A_ORG.ORG_COD" +
                          " AND USUARIO_PROC_RESTRINGIDO.ENT_COD= A_ENT.ENT_COD" +
                          " AND USUARIO_PROC_RESTRINGIDO.ORG_COD= A_ENT.ENT_ORG";

        log.debug(sql);

        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);

            Hashtable<String,String> jndis = new Hashtable<String, String>();
            // Se recuperan los procedimientos de tipo restringido sobre los que un determinado usuario tiene permiso
            while(rs. next()) {

                String codProcedimiento = rs.getString("PRO_COD");
                String codOrganizacion   = rs.getString("ORG_COD");
                String codigoEntidad       = rs.getString("ENT_COD");
                String descEntidad          = rs.getString("DESCENTIDAD");
                String descOrganizacion  = rs.getString("DESCORGANIZACION");

                
                PermisoProcedimientosRestringidosVO proc = new PermisoProcedimientosRestringidosVO();
                proc.setCodProcedimiento(codProcedimiento);
                proc.setCodEntidad(codigoEntidad);
                proc.setCodUsuario(codUsuario);
                proc.setCodMunicipio(codOrganizacion);
                proc.setDescEntidad(descEntidad);
                proc.setDescOrganizacion(descOrganizacion);
                
                
                /*** Se recupera la definición del procedimiento del esquema correspondiente  ***/
                String jndi = null;
                if(!jndis.contains(codOrganizacion)){
                    jndi = this.getJndi(codOrganizacion, codigoEntidad, con);
                    jndis.put(codOrganizacion,jndi);
                }else{
                    jndi = jndis.get(codOrganizacion);
                }

                log.debug(" =========> Valor del jndi: " + jndi);
                String[] paramsNuevo = new String[7];
                paramsNuevo[0] = params[0];
                paramsNuevo[6] = jndi;

                String descProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, paramsNuevo);
                proc.setDescProcedimiento(descProcedimiento);
                salida.add(proc);

            }


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try{
                if(rs!=null) rs.close();
                if(st!=null) st.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return salida;
    }



    private String getJndi(String codOrganizacion,String codEntidad,Connection con){

        String jndi = null;
        SortedMap<ArrayList<String>,ParametrosBDVO> listaParametrosBD = (SortedMap<ArrayList<String>,ParametrosBDVO>)CacheDatosFactoria.getImplParametrosBD().getDatos();
        
        if (listaParametrosBD!=null) {
            for(Map.Entry<ArrayList<String>,ParametrosBDVO> entry : listaParametrosBD.entrySet()) {
                ParametrosBDVO parametrosBD = entry.getValue();
                if (parametrosBD.getCodOrganizacion() == Integer.parseInt(codOrganizacion) && 
                        parametrosBD.getCodEntidad() == Integer.parseInt(codEntidad)){
                    jndi = parametrosBD.getJndi();
                    break;
                }
            }
        }
        return jndi;
    }

  /**
     * Inserta los permisos de un determinado usuario sobre procedimientos restringidos. Para poder hacer esto, previamente al usuario ha tenido
     * que aplicarse la correspondiente directiva.
     * @param permisos: ArrayList<PermisoProcedimientosRestringidosVO>
     * @param codUsuario: Código del usuario al que se le ha asignado la directiva de procedimientos restringidos
     * @param conexion: Conexión a la base de datos
     * @return Un boolean
     */
    public boolean insertarPermisosUsuarioProcedimientosRestringidos(ArrayList<PermisoProcedimientosRestringidosVO> permisos,int codUsuario,Connection conexion){
        boolean exito = false;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;

        try{

            
            String sqlDirectiva = "SELECT COUNT(*) AS NUM FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU_DIR WHERE USU=" + codUsuario + " AND DIR='PROCEDIMIENTOS_RESTRINGIDOS'";
            log.debug(sqlDirectiva);
            st = conexion.createStatement();
            rs = st.executeQuery(sqlDirectiva);
            int num = 0;
            while(rs.next()){
                num = rs.getInt("NUM");
            }

            st.close();
            rs.close();
            
            if(num==1){
                // Si existe la directiva de procedimientos restringidos, se eliminan los permisos que tuviese
                log.debug("el usuario " + codUsuario + " tiene asignada la directiva de procedimientos restringidos");
                String sql = "DELETE FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE  USU_COD=" + codUsuario;
                log.debug("sql:  " + sql);
                st = conexion.createStatement();
                int rowsDeleted = st.executeUpdate(sql);
                log.debug("rowsDeleted:  " + rowsDeleted);
                st.close();
                
                // Se insertan los permisos del usuario de nuevo
                
                int contador = 0;
                for(int i=0;i<permisos.size();i++){
                    PermisoProcedimientosRestringidosVO permiso = permisos.get(i);

                    sql = "INSERT INTO USUARIO_PROC_RESTRINGIDO(PRO_COD,USU_COD,ORG_COD,ENT_COD) VALUES (?,?,?,?)";
                    log.debug(sql);
                    ps = conexion.prepareStatement(sql);
                    int z=1;
                    ps.setString(z++, permiso.getCodProcedimiento());
                    ps.setInt(z++, codUsuario);
                    ps.setInt(z++, Integer.parseInt(permiso.getCodMunicipio()));
                    ps.setInt(z++, Integer.parseInt(permiso.getCodEntidad()));
                    
                    int rowsInserted = ps.executeUpdate();
                    if(rowsInserted==1) contador++;
                    log.debug(" ==========> Nº de filas insertadas: " + rowsInserted);

                }// for                
                if(contador==permisos.size()) exito = true;
            }//if
            else exito  = true; // Si no tiene la directiva se da por correcta la operación

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



  /**
     * Comprueba si un usuario tiene permiso sobre un determinado procedimiento restringido
     * @param codUsuario: Código del usuario
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento: Código del procedimiento
     * @param conexion: Conexión a la base de datos
     * @return Un boolean
     */
    public boolean tieneUsuarioPermisoSobreProcedimientoRestringido(String codUsuario,String codOrganizacion,String codProcedimiento,Connection conexion) throws TechnicalException{
        boolean exito = false;        
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT COUNT(*) AS NUM FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO " +
                              "WHERE USU_COD=" + codUsuario + " AND PRO_COD='" + codProcedimiento + "' AND ORG_COD=" + codOrganizacion;
            log.debug(sql);
            st = conexion.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;
            while(rs.next()){
                num = rs.getInt("NUM");
                if(num>=1) exito = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
        }// finally

        return exito;
    }




}
