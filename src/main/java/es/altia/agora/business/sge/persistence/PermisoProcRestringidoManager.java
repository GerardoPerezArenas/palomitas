package es.altia.agora.business.sge.persistence;

import es.altia.agora.business.administracion.mantenimiento.PermisoProcedimientosRestringidosVO;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;
import es.altia.agora.business.sge.persistence.manual.PermisoProcRestringidoDAO;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.log4j.Logger;

/**
 *
 * @author oscar.rodriguez
 */
public class PermisoProcRestringidoManager {
    private static PermisoProcRestringidoManager instance=null;
    private Logger log = Logger.getLogger(PermisoProcRestringidoManager.class);

    public static PermisoProcRestringidoManager getInstance(){
        if(instance==null)
            instance = new PermisoProcRestringidoManager();

        return instance;
    }



    public ArrayList<PermisoProcedimientosRestringidosVO> getProcedimientosRestringidosPermisoUsuario(String codUsuario,String codApp,String[] params){
        Connection con = null;
        ArrayList<PermisoProcedimientosRestringidosVO> procedimientos = new ArrayList<PermisoProcedimientosRestringidosVO>();

        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            procedimientos = PermisoProcRestringidoDAO.getInstance().getProcedimientosRestringidosPermisoUsuario(codUsuario, codApp, params,con);
            log.debug(" =============> Número de procedimientos sobre los que tiene permiso el usuario: " +procedimientos.size());
            // Se cierra la conexión con el esquema genérico
            con.close();

            // A continuación, para cada procedimiento hay que recuperar su descripción de su esquema correspondiente
            Hashtable<String,String> jndis = new Hashtable<String,String>();
            for(int i=0;procedimientos!=null && i<procedimientos.size();i++){
                PermisoProcedimientosRestringidosVO procedimiento = procedimientos.get(i);

                String codEntidad           = procedimiento.getCodEntidad();
                String codOrganizacion   = procedimiento.getCodMunicipio();
                String codProcedimiento = procedimiento.getCodProcedimiento();

                String jndi = null;
                if(jndis.containsKey(codOrganizacion))
                    jndi = jndis.get(codOrganizacion);
                else{
                    jndi = UsuariosGruposManager.getInstance().obtenerJNDI(codEntidad, codOrganizacion, codApp, params);
                    jndis.put(codOrganizacion, jndi);
                }

                log.debug(" =============> recurso jndi a utilizar: " + jndi);
                // Ya se dispone de los parámetros de conexión al esquema correspondiente
                String[] paramsNuevos = new String[7];
                paramsNuevos[0] = params[0]; // Nombre del gestor utilizado
                paramsNuevos[6] = jndi;          // Recurso JNDI
                log.debug(" ========> NUEVOS PARAMETROS DE CONEXIÓN: " + paramsNuevos);
                String descripcionProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, paramsNuevos);
                procedimiento.setDescProcedimiento(descripcionProcedimiento);
            }


        }catch(Exception e){
            e.printStackTrace();
        }

        return procedimientos;
    }


      /**
     * Comprueba si un usuario tiene permiso sobre un determinado procedimiento restringido
     * @param codUsuario: Código del usuario
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento: Código del procedimiento
     * @param params: Parámetros de conexión a la base de datos
     * @return Un boolean
     */
    public boolean tieneUsuarioPermisoSobreProcedimientoRestringido(String codUsuario,String codOrganizacion,String codProcedimiento,String[] params) throws TechnicalException{
       boolean exito = false;
       Connection con = null;
       AdaptadorSQLBD adapt = null;
       try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = PermisoProcRestringidoDAO.getInstance().tieneUsuarioPermisoSobreProcedimientoRestringido(codUsuario, codOrganizacion, codProcedimiento, con);
        } catch (BDException bDException) {
            bDException.printStackTrace();
       }finally{
            SigpGeneralOperations.devolverConexion(adapt, con);
           }
       return exito;
    }
    

}
