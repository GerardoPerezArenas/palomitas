package es.altia.agora.business.registro.mantenimiento.persistence;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.business.registro.mantenimiento.persistence.manual.MantAsuntosDAO;
import es.altia.agora.business.registro.mantenimiento.MantAsuntosValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;

public class MantAsuntosManager {

    // La instancia unica
    private static MantAsuntosManager instance = null;
    // Log para mensajes
    protected static Log m_Log =
        LogFactory.getLog(MantRolesManager.class.getName());

    protected MantAsuntosManager() {
    }

    /**
     * Factory method para el <code>Singleton</code>.
     * @return La unica instancia de MantAsuntosManager
     */
   public static MantAsuntosManager getInstance() {
     //Si no hay una instancia de esta clase tenemos que crear una.
         if (instance == null) {
             synchronized(MantAsuntosManager.class) {
         if (instance == null)
           instance = new MantAsuntosManager();
             }
         }
         return instance;
   }

   /**
    * Carga los tipos de asunto asociados a la unidad orgánica cuyo código se
    * pasa, si es -1 se cargan todos.
    *
    * @return Un vector de MantAsuntosValueObject que contendrán la información
    * de la clave primaria (codigo, unidadRegistro, tipoEntrada) y descripción del asunto.
    */
   public Vector<MantAsuntosValueObject> cargarAsuntos(int codigoUnidadOrg, String[] params) {

       Vector<MantAsuntosValueObject> asuntos = new Vector<MantAsuntosValueObject>();

       try{
           asuntos = MantAsuntosDAO.getInstance().cargarAsuntos(codigoUnidadOrg, params);
       }catch (Exception e) {
               m_Log.error("JDBC Technical problem " + e.getMessage());
       }
       return asuntos;
   }

   /**
    * Carga los asuntos de la unidad y tipo de registro del RegistroValueObject pasado.
    * @param regVO: Contiene información necesaria para recuperar los asuntos
    * @param recuperarTodosAsuntos: Si está a true se recuperar todos los asuntos de registro, incluido los que han sido dados de baja.
    * Si está a false, solo se recuperan los que no han sido eliminados
    * @param params: Parámetros de conexión a la BBDD
    * @return Un vector de MantAsuntosValueObject que contendrán la información
    * de la clave primaria (codigo, unidadRegistro, tipoEntrada) y descripción del asunto.
    */
   public Vector<MantAsuntosValueObject> buscarAsuntos(RegistroValueObject regVO, boolean recuperarTodosAsuntos, String[] params) {

       Vector<MantAsuntosValueObject> asuntos = new Vector<MantAsuntosValueObject>();

       try{
           asuntos = MantAsuntosDAO.getInstance().buscarAsuntos(regVO,recuperarTodosAsuntos, params);
       }catch (Exception e) {
               m_Log.error("JDBC Technical problem " + e.getMessage());
       }
       return asuntos;
   }

   /**
    * Carga todos los campos y listas de documentos y roles del asunto que se pasa como
    * argumento (usando los atributos codigo, unidadRegistro y tipoRegistro como clave
    * primaria del VO).
    *
    * @return Un MantAsuntosValueObject que contendrá toda la información del asunto.
    */
   public MantAsuntosValueObject cargarAsunto(MantAsuntosValueObject asunto, String[] params) {

       try{
           asunto = MantAsuntosDAO.getInstance().cargarAsunto(asunto, params);
       }catch (Exception e) {
               m_Log.error("JDBC Technical problem " + e.getMessage());
       }
       return asunto;
   }

   /**
    * Graba un nuevo asunto en la BD.
    */
   public void grabarAlta(MantAsuntosValueObject asunto, String[] params) {

       try{
           MantAsuntosDAO.getInstance().grabarAlta(asunto, params);
       }catch (Exception e) {
               m_Log.error("JDBC Technical problem " + e.getMessage());
       }
   }

   /**
    * Modifica los datos de un asunto en la BD.
    */
   public void grabarModificacion(MantAsuntosValueObject asunto, MantAsuntosValueObject anterior, String[] params) {

       try{
           MantAsuntosDAO.getInstance().grabarModificacion(asunto, anterior, params);
       }catch (Exception e) {
               m_Log.error("JDBC Technical problem " + e.getMessage());
       }
   }

    /**
     *  Elimina un asunto con sus documentos de la BD.
     *
     * @param asunto El MantAsuntosValueObject que representa el asunto.
     * @param idUsuario Identificador interno del usuario.
     * @param params Parámetros de conexión a BD (para inicializar el adaptador).
     */
    public void eliminarAsunto(MantAsuntosValueObject asunto, int idUsuario, String[] params) {

        try{

            MantAsuntosDAO.getInstance().cambiarEstadoAsunto(asunto, idUsuario, 1,params);

        }catch (Exception e) {
            m_Log.error("JDBC Technical problem " + e.getMessage());
        }
    }
 /**
     *  Elimina un asunto con sus documentos de la BD.
     *
     * @param asunto El MantAsuntosValueObject que representa el asunto.
     * @param idUsuario Identificador interno del usuario.
     * @param params Parámetros de conexión a BD (para inicializar el adaptador).
     */
    public void altaLogica(MantAsuntosValueObject asunto, int idUsuario, String[] params) {

        try{

            MantAsuntosDAO.getInstance().cambiarEstadoAsunto(asunto, idUsuario, 0,params);

        }catch (Exception e) {
            m_Log.error("JDBC Technical problem " + e.getMessage());
        }
    }



   public Vector<MantAsuntosValueObject> getAsuntosCodificadosPermisoUsuario(UsuarioValueObject usuario,String tipoEntrada,String[] params) {
       Vector<MantAsuntosValueObject> asuntos = null;
       AdaptadorSQLBD adapt = null;
       Connection con = null;
       try{
           adapt = new AdaptadorSQLBD(params);
           con = adapt.getConnection();
           asuntos = MantAsuntosDAO.getInstance().getAsuntosCodificadosPermisoUsuario(usuario,"E", con);

       }catch(BDException e){
            m_Log.error("Error al recuperar los asuntos codificados de la base de datos " + e.getMessage());

       }finally{
           try{
               adapt.devolverConexion(con);
           }catch(Exception e){
               e.printStackTrace();
           }
       }

       return asuntos;
   }

    /**
    * Carga los asuntos de la unidad y tipo de registro del RegistroValueObject pasado.
    * @param regVO: Contiene información necesaria para recuperar los asuntos
    * @param recuperarTodosAsuntos: Si está a true se recuperar todos los asuntos de registro, incluido los que han sido dados de baja.
    * Si está a false, solo se recuperan los que no han sido eliminados
    * @param params: Parámetros de conexión a la BBDD
    * @return Un vector de MantAsuntosValueObject que contendrán la información
    * de la clave primaria (codigo, unidadRegistro, tipoEntrada) y descripción del asunto.
    */
   public Vector<MantAsuntosValueObject> buscarAsuntosClasificacion(Integer codigoClasificacion,RegistroValueObject regVO, boolean recuperarTodosAsuntos, String[] params) {

      m_Log.debug("BuscarAsuntosClasificacion.BEGIN. El valor del Registro es:"+regVO);
      m_Log.debug("BuscarAsuntosClasificacion.BEGIN. El codigo de Clasificacion es:"+codigoClasificacion);
      m_Log.debug("BuscarAsuntosClasificacion.BEGIN. El valor de recuperarTodosAsuntos es:"+recuperarTodosAsuntos);

      Vector<MantAsuntosValueObject> asuntos = new Vector<MantAsuntosValueObject>();

       try{
           asuntos = MantAsuntosDAO.getInstance().buscarAsuntosClasificacion(codigoClasificacion, regVO, recuperarTodosAsuntos, params);
       }catch (Exception e) {
               m_Log.error("JDBC Technical problem " + e.getMessage());
       }
       m_Log.debug("BuscarAsuntosClasificacion.END. Tamaño del vector devuelto:"+asuntos.size());
       return asuntos;
   }
}
