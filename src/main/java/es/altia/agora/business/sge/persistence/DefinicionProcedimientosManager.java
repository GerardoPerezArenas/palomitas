// NOMBRE DEL PAQUETE
package es.altia.agora.business.sge.persistence;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.mantenimiento.PermisoProcedimientosRestringidosVO;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import java.util.Vector;
import java.sql.Connection;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.sge.persistence.manual.DefinicionProcedimientosDAO;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.DefinicionCampoValueObject;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.DocumentoExpedienteVO;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.expedientes.anulacion.exception.VerificacionFinNoConvencionalInstanceException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.SQLException;
import java.util.ArrayList;
import es.altia.flexia.expedientes.anulacion.plugin.factoria.VerificacionFinNoConvencionalExpedienteFactoria;
import es.altia.flexia.expedientes.anulacion.plugin.VerificacionFinNoConvencionalExpediente;


/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DiligenciasManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class DefinicionProcedimientosManager {

  private static DefinicionProcedimientosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(DefinicionProcedimientosManager.class.getName());


  protected DefinicionProcedimientosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static DefinicionProcedimientosManager getInstance() {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(DefinicionProcedimientosManager.class) {
        if (instance == null) {
          instance = new DefinicionProcedimientosManager();
        }      
    }
    return instance;
  }

  public Vector getListaTiposDepartamentos(String[] params)
       throws AnotacionRegistroException{

      Vector res = null;

    m_Log.debug("getListaTiposDepartamentos");

    try {

        m_Log.debug("Usando persistencia manual");
        res = DefinicionProcedimientosDAO.getInstance().getListaTiposDepartamentos(params);
        m_Log.debug("Tipos de departamentos obtenidos");
        //We want to be informed when this method has finalized
        m_Log.debug("getListaTiposDepartamentos");

    } catch (Exception ce) {
        res = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
    }

    return res;
  }

  public Vector getListaArea(String[] params)
       throws AnotacionRegistroException{

      Vector res = null;

    m_Log.debug("getListaArea");

    try {

        m_Log.debug("Usando persistencia manual");
        res = DefinicionProcedimientosDAO.getInstance().getListaArea(params);
        m_Log.debug("Tipos de area obtenidos");
        //We want to be informed when this method has finalized
        m_Log.debug("getListaTiposArea");

    } catch (Exception ce) {
        res = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
    }

    return res;
  }

  public Vector getListaUnidadInicio(String[] params)
       throws AnotacionRegistroException{

      Vector res = null;

    m_Log.debug("getListaTiposUnidadInicio");

    try {

        m_Log.debug("Usando persistencia manual");
        res = DefinicionProcedimientosDAO.getInstance().getListaUnidadInicio(params);
        m_Log.debug("Tipos de unidades de inicio obtenidos");
        //We want to be informed when this method has finalized
        m_Log.debug("getListaTiposUnidadInicio");

    } catch (Exception ce) {
        res = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
    }

    return res;
  }

  public Vector getTablaUnidadInicio(DefinicionProcedimientosValueObject defProcVO,String[] params)
      throws AnotacionRegistroException{

     Vector res = null;

   m_Log.debug("getTablaUnidadInicio");

   try {

       m_Log.debug("Usando persistencia manual");
       res = DefinicionProcedimientosDAO.getInstance().getTablaUnidadInicio(defProcVO,params);
       m_Log.debug("Tipos de unidades de inicio obtenidos");
       //We want to be informed when this method has finalized
       m_Log.debug("getTablaUnidadInicio");

   } catch (Exception ce) {
       res = null;
       m_Log.error("JDBC Technical problem " + ce.getMessage());
       throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
   }

   return res;
  }


  public Vector getListaTiposProcedimientos(String[] params)
       throws AnotacionRegistroException{

      Vector res = null;

    m_Log.debug("getListaTiposProcedimientos");

    try {

        m_Log.debug("Usando persistencia manual");
        res = DefinicionProcedimientosDAO.getInstance().getListaTiposProcedimientos(params);
        m_Log.debug("Tipos de procedimientos obtenidos");
        //We want to be informed when this method has finalized
        m_Log.debug("getListaTiposProcedimientos");

    } catch (Exception ce) {
        res = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
    }

    return res;
  }

  public Vector getListaTiposNaturaleza(String[] params)
       throws AnotacionRegistroException{

      Vector res = null;

    m_Log.debug("getListaTiposNaturaleza");

    try {

        m_Log.debug("Usando persistencia manual");
        res = DefinicionProcedimientosDAO.getInstance().getListaTiposNaturaleza(params);
        m_Log.debug("Tipos de procedimientos obtenidos");
        //We want to be informed when this method has finalized
        m_Log.debug("getListaTiposNaturaleza");

    } catch (Exception ce) {
        res = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
    }

    return res;
  }

    public Vector getListaCamposDesplegables(String[] params)
         throws AnotacionRegistroException{

        Vector res = null;

      m_Log.debug("getListaCamposDesplegables");

      try {

          m_Log.debug("Usando persistencia manual");
          res = DefinicionProcedimientosDAO.getInstance().getListaCamposDesplegables(params);
          m_Log.debug("Campos Desplegables obtenidos");
          //We want to be informed when this method has finalized
          m_Log.debug("getListaCamposDesplegables");

      } catch (Exception ce) {
          res = null;
          m_Log.error("JDBC Technical problem " + ce.getMessage());
          throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
      }

      return res;
    }

  public Vector getListaPlantillas(String[] params)
       throws AnotacionRegistroException{

      Vector res = null;

    m_Log.debug("getListaPlantillas");

    try {

        m_Log.debug("Usando persistencia manual");
        res = DefinicionProcedimientosDAO.getInstance().getListaPlantillas(params);
        m_Log.debug("Tipos de plantillas obtenidos");
        //We want to be informed when this method has finalized
        m_Log.debug("getListaPlantillas");

    } catch (Exception ce) {
        res = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
    }

    return res;
  }

  public Vector getListaMascaras(String[] params)
       throws AnotacionRegistroException{

      Vector res = null;

    m_Log.debug("getListaMascaras");

    try {

        m_Log.debug("Usando persistencia manual");
        res = DefinicionProcedimientosDAO.getInstance().getListaMascaras(params);
        m_Log.debug("Tipos de plantillas obtenidos");
        //We want to be informed when this method has finalized
        m_Log.debug("getListaMascaras");

    } catch (Exception ce) {
        res = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
    }

    return res;
  }

  public Vector getListaRelacionTipoDatoPlantillas(String[] params)
       throws AnotacionRegistroException{

      Vector res = null;

    m_Log.debug("getListaRelacionTipoDatoPlantillas");

    try {

        m_Log.debug("Usando persistencia manual");
        res = DefinicionProcedimientosDAO.getInstance().getListaRelacionTipoDatoPlantillas(params);
        m_Log.debug("Tipos de plantillas obtenidos");
        //We want to be informed when this method has finalized
        m_Log.debug("getListaRelacionTipoDatoPlantillas");

    } catch (Exception ce) {
        res = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
    }

    return res;
  }

  public Vector getListaRelacionTipoDatoMascaras(String[] params)
       throws AnotacionRegistroException{

      Vector res = null;

    m_Log.debug("getListaRelacionTipoDatoMascaras");

    try {

        m_Log.debug("Usando persistencia manual");
        res = DefinicionProcedimientosDAO.getInstance().getListaRelacionTipoDatoMascaras(params);
        m_Log.debug("Tipos de plantillas obtenidos");
        //We want to be informed when this method has finalized
        m_Log.debug("getListaRelacionTipoDatoMascaras");

    } catch (Exception ce) {
        res = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
    }

    return res;
  }

  public Vector getListaTipoDato(String[] params)
       throws AnotacionRegistroException{

      Vector res = null;

    m_Log.debug("getListaTipoDato");

    try {

        m_Log.debug("Usando persistencia manual");
        res = DefinicionProcedimientosDAO.getInstance().getListaTipoDato(params);
        m_Log.debug("Tipos de datos obtenidos");
        //We want to be informed when this method has finalized
        m_Log.debug("getListaTipoDato");

    } catch (Exception ce) {
        res = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
    }

    return res;
  }

  /**
   * Devuelve la lista de documentos para el procedimiento indicado.
   * 
   * @param proc GeneralValueObject con el municipio y codigo del procedimiento en 'codMunicipio' y 'codProcedimiento'. 
   * @return Una lista de los nombres de documentos del procedimiento en forma de ElementoListaValueObject, con
   *         el nombre del documento en el atributo 'descripcion'.
   */
  public Vector<ElementoListaValueObject> getListaDocumentos(GeneralValueObject proc, String[] params) {
      
      Vector<ElementoListaValueObject> docs = new Vector<ElementoListaValueObject>();

      try {
          m_Log.debug("Usando persistencia manual->getListaDocumentos");

          docs = DefinicionProcedimientosDAO.getInstance().getListaDocumentos(proc,params);          

      } catch (Exception ce) {
          m_Log.error("JDBC Technical problem " + ce.getMessage());
          ce.printStackTrace();
      }
      return docs;      
  }

  public ArrayList<DocumentoExpedienteVO> getListaDocumentos(DefinicionTramitesValueObject proc, String[] params) {
      ArrayList<DocumentoExpedienteVO> docs = new ArrayList<DocumentoExpedienteVO>();
      try {
          m_Log.debug("Usando persistencia manual->getListaDocumentos");

          docs = DefinicionProcedimientosDAO.getInstance().getListaDocumentos(proc,params);

      } catch (Exception ce) {
          m_Log.error("JDBC Technical problem " + ce.getMessage());
          ce.printStackTrace();
      }
      return docs;    
  }
  public int insert(Connection conexion,DefinicionProcedimientosValueObject defProcVO,String[] params)
    throws AnotacionRegistroException {
    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("insert");
    int i;

    try {
        m_Log.debug("Usando persistencia manual");

        i = DefinicionProcedimientosDAO.getInstance().insert(conexion,defProcVO,params);

        if (i>0) m_Log.debug("procedimiento insertado correctamente");
        else m_Log.debug("procedimiento no insertado");
        //We want to be informed when this method has finalized
        m_Log.debug("insert");

    } catch (Exception ce) {
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());

    }
    return i;
    }

    public DefinicionProcedimientosValueObject buscar(DefinicionProcedimientosValueObject defProcVO,String[] params)
       throws AnotacionRegistroException{

      DefinicionProcedimientosValueObject dPVO = null;

    m_Log.debug("buscar");

    try {

        m_Log.debug("Usando persistencia manual");
        dPVO = DefinicionProcedimientosDAO.getInstance().buscar(defProcVO,params);
        m_Log.debug("buscado");
        //We want to be informed when this method has finalized
        m_Log.debug("buscar");

    } catch (Exception ce) {
        dPVO = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
    }

    return dPVO;
  }

  public Vector consultar(DefinicionProcedimientosValueObject defProcVO,String[] params)
       throws AnotacionRegistroException{

      Vector consulta = null;

    m_Log.debug("consultar");

    try {

        m_Log.debug("Usando persistencia manual");
        consulta = DefinicionProcedimientosDAO.getInstance().consultar(defProcVO,params);
        m_Log.debug("consultado");
        //We want to be informed when this method has finalized
        m_Log.debug("consultar");

    } catch (Exception ce) {
        consulta = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
    }

    return consulta;
  }

  public String obtenerJndi(DefinicionProcedimientosValueObject defProcVO,String[] params)
       throws AnotacionRegistroException{

      String jndi = "";

    m_Log.debug("obtenerJndi");

    try {

        m_Log.debug("Usando persistencia manual");
        jndi = DefinicionProcedimientosDAO.getInstance().obtenerJndi(defProcVO,params);
        m_Log.debug("obteniendoJndi");
        //We want to be informed when this method has finalized
        m_Log.debug("obtenerJndi");

    } catch (Exception ce) {
        jndi = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
    }

    return jndi;
  }


  public int modificar(DefinicionProcedimientosValueObject defProcVO,String[] params)
    throws AnotacionRegistroException {
    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("modificar");
    int i;

    try {
        m_Log.debug("Usando persistencia manual");

        i = DefinicionProcedimientosDAO.getInstance().modificar(defProcVO,params);

        m_Log.debug("procedimiento modificado correctamente");
        //We want to be informed when this method has finalized
        m_Log.debug("modificar");

    } catch (Exception ce) {
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());

    }
    return i;
    }

    public int eliminar(DefinicionProcedimientosValueObject defProcVO,String[] params)
    throws AnotacionRegistroException {
    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("eliminar");
    int i;

    try {
        m_Log.debug("Usando persistencia manual");

        i = DefinicionProcedimientosDAO.getInstance().eliminar(defProcVO,params);

        m_Log.debug("procedimiento eliminado correctamente");
        //We want to be informed when this method has finalized
        m_Log.debug("eliminar");

    } catch (Exception ce) {
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());

    }
    return i;
    }


    public Vector catalogoProcedimientos(DefinicionProcedimientosValueObject defProcVO,String[] params)
    throws TramitacionException {

      Vector catalogo = null;

      m_Log.debug("catalogoProcedimientos");

    try {

        m_Log.debug("Usando persistencia manual");
        catalogo = DefinicionProcedimientosDAO.getInstance().catalogoProcedimientos(defProcVO,params);
        m_Log.debug("catalogoProcedimientos");


    } catch (Exception ce) {
        catalogo = null;
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        throw new TramitacionException("Problema técnico de JDBC " + ce.getMessage(), ce);
    }
    m_Log.debug("catalogoProcedimientos");
    return catalogo;
  }

    public Vector catalogoProcedimientosTramites(DefinicionProcedimientosValueObject defProcVO, String[] params)
            throws TramitacionException {

        Vector tramites;

        m_Log.debug("catalogoProcedimientosTramites");

        try {

            m_Log.debug("Usando persistencia manual");
            tramites = DefinicionProcedimientosDAO.getInstance().catalogoProcedimientosTramites(defProcVO, params);
            m_Log.debug("catalogoProcedimientosTramites");


        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + ce.getMessage(), ce);
        }
        m_Log.debug("catalogoProcedimientosTramites");
        return tramites;
    }

    /**
     * Metodo encargado de llamar al DAO correspondiente así como controlar y lanzar hacia arriba las excepciones.
     * Obtiene una descripcion en forma de Vector de los campos deplegables asociados a un procedimiento.
     * @param   codigoProc  Codigo del Procedimiento del que se quieren buscar campos desplegables.
     * @param   params      Parametros de Conexion a la BB.DD.
     * @return              Vector describiendo los campos desplegables asociados a un procedimiento.
     * @throws TramitacionException Si hay algun fallo en la llamada al DAO.
     */
    public Vector obtenerCamposDesplegables(String codigoProc, String[] params)
    throws TramitacionException {

        m_Log.debug("DefinicionProcedimientosManager --> Inicio obtenerCamposDesplegables");
        Vector camposDesplegables;

        try {
            camposDesplegables = DefinicionProcedimientosDAO.getInstance().obtenerCampoDesplegables(codigoProc, params);            
        } catch (Exception exc) {
            m_Log.error("ERROR - " + exc.getMessage());
            throw new TramitacionException("Problema técnico:" + exc.getMessage(), exc);
        }

        m_Log.debug("DefinicionProcedimientosManager --> Fin obtenerCamposDesplegables");
        return camposDesplegables;
    }
    
    public DefinicionProcedimientosValueObject getOpcionesInicioProcedimiento(String codProcedimiento, String[] params, UsuarioValueObject uVO) throws TramitacionException, TechnicalException {
        
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        try {
            con = oad.getConnection();
            return DefinicionProcedimientosDAO.getInstance().getOpcionesInicioProcedimiento(codProcedimiento, con, oad, uVO);
        } catch (BDException bde) {
            throw new TechnicalException(bde.getMensaje(), bde);
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
        }                
    }


    /**
     * Recupera la información relativa al plazo de finalización de un procedimiento
     * @param definicion: DefinicionProcedimientosValueObject
     * @param params
     * @return
     * @throws es.altia.agora.business.sge.exception.TramitacionException
     * @throws es.altia.common.exception.TechnicalException
     */
    public DefinicionProcedimientosValueObject getPlazosFinalizacion(DefinicionProcedimientosValueObject definicion,Connection con)  throws TramitacionException, TechnicalException{

        try{            
            definicion = DefinicionProcedimientosDAO.getInstance().getPlazosFinalizacion(definicion, con);

        }catch(Exception e){
            throw new TechnicalException(e.getMessage(), e);
        }

        return definicion;
    }


     /**
     * Recupera la información relativa al plazo de finalización de un procedimiento
     * @param definicion: DefinicionProcedimientosValueObject
     * @param params
     * @return
     * @throws es.altia.agora.business.sge.exception.TramitacionException
     * @throws es.altia.common.exception.TechnicalException
     */
    public DefinicionProcedimientosValueObject getPlazosFinalizacion(DefinicionProcedimientosValueObject definicion,String[] params)  throws TramitacionException, TechnicalException{

        Connection con = null;
        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            definicion = DefinicionProcedimientosDAO.getInstance().getPlazosFinalizacion(definicion, con);

        }catch(Exception e){
            throw new TechnicalException(e.getMessage(), e);
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                
            }
        }

        return definicion;
    }


    /**
     * Recupera la descripción de un procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param params: Parámetros de conexión a la BBDD
     * @return String con la descripción
     */
    public String getDescripcionProcedimiento(String codProcedimiento,String[] params)
    {
        Connection con = null;
        String descripcion = null;
        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            descripcion  = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento,con);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return descripcion;
    }
    
    /**
     * Recupera la descripción de un procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param params: Parámetros de conexión a la BBDD
     * @return String con la descripción
     */
    public String getDescripcionMultiIdiProcedimiento(String codProcedimiento,String[] params)
    {
        Connection con = null;
        String descripcion = null;
        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            descripcion  = DefinicionProcedimientosDAO.getInstance().getDescripcionMultiIdiProcedimiento(codProcedimiento,con);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return descripcion;
    }



    /**
     * Recupera los procedimientos restringidos de una determinada organización
     * @param codUsuario: Código del usuario
     * @param codOrganizacion: Código de la organización
     * @param codEntidad: Código de la entidad
     * @param params: Parámetros de conexión al esquema correspondiente
     */
    public ArrayList<PermisoProcedimientosRestringidosVO> getProcedimientosRestringidos(String codUsuario,String codOrganizacion,String codEntidad,String[] params){

       ArrayList<PermisoProcedimientosRestringidosVO> procs = new ArrayList<PermisoProcedimientosRestringidosVO>();
       Connection con = null;

       try{
           // Se selecciona el jndi para el esquema apropiado
           String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(codEntidad, codOrganizacion, codUsuario, params);
           String[] paramsNuevos = new String[7];
           paramsNuevos[0] = params[0]; // Nombre del gestor utilizado
           paramsNuevos[6] = jndi;          // Recurso JNDI

           // Se recuperan los procedimentos marcados como restringidos de una determinada organización
           AdaptadorSQLBD adapt = new AdaptadorSQLBD(paramsNuevos);
           con = adapt.getConnection();
           procs = DefinicionProcedimientosDAO.getInstance().getProcedimientosRestringidos(codOrganizacion,codEntidad,con);
           m_Log.debug(" ************ Número de procedimientos restringidos  " + procs.size() + " de la organización " + codOrganizacion + ", codEntidad: " + codEntidad
                   + " y jndi: " + jndi);
           
       }catch(Exception e){
           e.printStackTrace();           
       }finally{
           try{
               if(con!=null && !con.isClosed()) con.close();
           }catch(SQLException e){
               e.printStackTrace();
           }
           
       }

       return procs;

    }// getProcedimentosRestringidos



    /**
     * Recupera la lista de procedimientos necesarios para rellenar el combo de la bandeja de expedientes pendientes. Tiene en cuenta que si
     * el procedimiento está restringido, se comprueba si el usuario tiene permiso para visualizarlo y trabajar con él
     * @param codUsuario: Código del usuario
     * @param params: Parámetros de conexión a la BBDD
     * @return Vector
     */
   public Vector getListaProcedimientosFiltroBandejaPendientes(UsuarioValueObject usuario, String[] params)
    {
        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaProcedimientosFiltroBandejaPendientes");
        Connection con = null;
        Vector salida = new Vector();
        AdaptadorSQLBD adapt = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            salida = DefinicionProcedimientosDAO.getInstance().getListaProcedimientosFiltroBandejaPendientes(usuario, con);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try{
                adapt.devolverConexion(con);

            } catch(BDException sqle) {
                sqle.printStackTrace();
            }
        }

        return salida;
    }


    /**
     * Comprueba si un procedimiento está restringido
     * el procedimiento está restringido, se comprueba si el usuario tiene permiso para visualizarlo y trabajar con él
     * @param codProcedimiento: Código del procedimiento
     * @param params: Parámetros de conexión a la BBDD
     * @return boolean
     */
    public boolean estaProcedimientoRestringido(String codProcedimiento, String[] params) throws TechnicalException {
        boolean exito = false;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = DefinicionProcedimientosDAO.getInstance().estaProcedimientoRestringido(codProcedimiento, con);
        } catch (BDException bDException) {
           bDException.printStackTrace();
        }finally{
            SigpGeneralOperations.devolverConexion(adapt, con);
        }

        return exito;
    }


   /**
     * Recupera el código del área de un procedimiento
     * @param codProcedimiento: Código del procedimiento
     * @param params: Parámetros de conexión a la BBDD
     * @return String con el código del área indicado en la definición del procedimiento
     * @throws java.sql.TechnicalException si ocurre un error durante el acceso a la BBDD
     */
    public String getCodigoAreaProcedimiento(String codProcedimiento,String[] params) throws TechnicalException{
        Connection con = null;
        String descripcion = null;
        try{
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            descripcion  = DefinicionProcedimientosDAO.getInstance().getCodigoAreaProcedimiento(codProcedimiento,con);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return descripcion;
    }


    /***
     * Recupera el plugin de finalización no convencional a recuperar para verificar si se puede o no finalizar
     * los expedientes de un determinado procedimiento
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento: Código del procedimiento
     * @param params: Parámetros de conexión a la BBDD
     */
    public VerificacionFinNoConvencionalExpediente getPluginFinalizacionNoConvencional(int codOrganizacion,String codProcedimiento,String[] params) throws VerificacionFinNoConvencionalInstanceException{
        VerificacionFinNoConvencionalExpediente plugin = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;

        try{
            adapt = new AdaptadorSQLBD(params);
            con   = adapt.getConnection();


            DefinicionProcedimientosValueObject defProcVO = DefinicionProcedimientosDAO.getInstance().getPluginFinalizacionNoConvencional(codProcedimiento, con);
            if(defProcVO==null){
                // El procedimiento no tiene asociado un plugin de finalización no convencional => Se recupera el plugin de anulación de expediente por defecto
                // de Flexia, que siempre permite finalizar el expediente
                plugin = VerificacionFinNoConvencionalExpedienteFactoria.getImplClassDefecto();
            }else{
                // El procedimiento tiene asociado un plugin de finalización no convencional, se recupera
                plugin = VerificacionFinNoConvencionalExpedienteFactoria.getImplClass(codOrganizacion, defProcVO.getCodServicioFinalizacion());
                if(!plugin.getImplClass().equals(defProcVO.getImplClassServicioFinalizacion())){
                    // Sino coincide la clase de implementación del plugin del procedimiento con la recuperada del fichero de configuración, se devuelve
                    // el plugin de finalización por defecto
                    m_Log.error(" La clase que implementa el plugin para el procedimiento " + codProcedimiento +  " es " + defProcVO.getImplClassServicioFinalizacion() + " y no coincide con la recuperada del fichero de configuración que es: " + plugin.getImplClass());
                    plugin = VerificacionFinNoConvencionalExpedienteFactoria.getImplClassDefecto();
                }
            }


        }catch(BDException e){
            m_Log.error("Error al recuperar una conexión a la BBDD: " + e.getMessage());

        }catch(TechnicalException e){
            m_Log.error("Error al recuperar una conexión a la BBDD: " + e.getMessage());

        }catch(VerificacionFinNoConvencionalInstanceException e){
            m_Log.error(e.getMessage());
            throw e;
        }
        finally{
            try{
               adapt.devolverConexion(con);
            }catch(Exception e){
                m_Log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }
        
        return plugin;

    }
    
    public boolean existeExpedientesProcedimiento(String codProcedimiento,String[] params){
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        boolean existenExpProc = false;

        try{
            adapt = new AdaptadorSQLBD(params);
            con   = adapt.getConnection();

            existenExpProc = DefinicionProcedimientosDAO.getInstance().existeExpedientesProcedimiento(codProcedimiento, con);
            
        }catch(BDException bde){
            m_Log.error("Error al recuperar una conexión a la BBDD: " + bde.getMessage());

        }catch(SQLException sqle){
            m_Log.error("Error al recuperar una conexión a la BBDD: " + sqle.getMessage());

        }
        finally{
            try{
               adapt.devolverConexion(con);
            }catch(Exception e){
                m_Log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }
        
        return existenExpProc;
    }
    
}