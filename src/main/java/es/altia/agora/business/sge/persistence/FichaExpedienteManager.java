package es.altia.agora.business.sge.persistence;

import es.altia.agora.business.sge.FirmasDocumentoExpedienteVO;
import es.altia.agora.business.sge.FirmasDocumentoProcedimientoVO;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.HashMap;

import es.altia.common.service.config.*;
import es.altia.agora.business.sge.FicheroVO;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.*;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.sge.persistence.manual.FichaExpedienteDAO;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.geninformes.utils.Utilidades;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.agora.business.sge.ExpedienteOtroDocumentoVO;
import es.altia.agora.business.sge.RespuestaRetrocesoTramiteVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.ValorCampoSuplementarioVO;
import es.altia.agora.business.sge.persistence.manual.TramitacionExpedientesDAO;
import es.altia.agora.business.sge.persistence.manual.ExpedienteOtroDocumentoDAO;
import es.altia.agora.business.sge.persistence.manual.InteresadosDAO;
import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.DomiciliosDAO;
import es.altia.agora.business.terceros.persistence.manual.DatosSuplementariosTerceroDAO;
import es.altia.agora.business.terceros.persistence.manual.TercerosDAO;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.interfaces.user.web.util.FormateadorTercero;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.agora.webservice.registro.GestorAccesoRegistro;
import es.altia.common.util.Utilities;
import es.altia.flexia.historico.expediente.vo.DocumentoTramitacionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.exception.EjecucionModuloException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class FichaExpedienteManager {

    private final static String ID_SERVICIO_POR_DEFECTO = "SGE";

  // Mi propia instancia usada en el metodo getInstance.
  private static FichaExpedienteManager instance = null;

  // Para el fichero de configuracion technical.
  protected static Config m_ConfigTechnical;
    protected static Config m_ConfigCommon;
  // Para el fichero de mensajes de error localizados.
  protected static Config m_ConfigError;
  protected static Log m_Log =
          LogFactory.getLog(FichaExpedienteManager.class.getName());


  protected FichaExpedienteManager() {
    //Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
        m_ConfigCommon = ConfigServiceHelper.getConfig("common");
  }


  /**
  * Factory method para el <code>Singelton</code>.
  * @return La unica instancia de FichaExpedienteManager
  */
  public static FichaExpedienteManager getInstance() {
    //Si no hay una instancia de esta clase tenemos que crear una.
    synchronized(FichaExpedienteManager.class) {
        if (instance == null)
        instance = new FichaExpedienteManager();      
    }
    return instance;
  }

  public GeneralValueObject cargaExpediente(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("cargaExpedientes");

    try{
      gVO = FichaExpedienteDAO.getInstance().cargaExpediente(gVO, params);
    }catch (TechnicalException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      return gVO;
    }
  }

    public Vector cargaTramites(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("cargaTramites");

    Vector tramites = new Vector();
    
    try{
      tramites = FichaExpedienteDAO.getInstance().cargaTramites(gVO, params);
    }catch (TechnicalException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      return tramites;
    }
  }

    public HashMap cargaListaAsientosExpediente(GeneralValueObject gVO, UsuarioValueObject usuarioVO, String[] params)
            throws TramitacionException {

        m_Log.debug("FichaExpedienteManager --> cargaListaAsientosExpediente");
        GestorAccesoRegistro gestorRegistro = crearGestor(usuarioVO.getOrgCod());
        return gestorRegistro.cargaListaAsientosExpediente(gVO, usuarioVO, params);

  }


  public Vector cargaListaDocumentosExpediente(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("cargaListaDocumentosExpediente");

    Vector documentos = new Vector();

    try{
      documentos = FichaExpedienteDAO.getInstance().cargaListaDocumentosExpediente(gVO, params);
    }catch (TechnicalException te) {
      documentos = new Vector(); // Vacío
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      documentos = new Vector(); // Vacío
      e.printStackTrace();
    }finally{
      return documentos;
    }
  }

  
  /**
  public Vector cargaListaEnlaces(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("cargaListaEnlaces");

    Vector enlaces = new Vector();

    try{
      enlaces = FichaExpedienteDAO.getInstance().cargaListaEnlaces(gVO, params);
    }catch (TechnicalException te) {
      enlaces = new Vector(); // Vacío
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      enlaces = new Vector(); // Vacío
      e.printStackTrace();
    }finally{
      return enlaces;
    }
  }  **/


  public boolean tieneExpedientesRelacionados(String codMunicipio, String ejercicio, String numero, String [] params){
	  //queremos estar informados de cuando este metodo es ejecutado
	  m_Log.debug("Dentro de Ficha Expediente Manager : tieneExpedientesRelacionados ");
	   
	  boolean resultado = false;
	  try{
		  resultado  = FichaExpedienteDAO.getInstance().tieneExpedientesPendientes(codMunicipio, ejercicio, numero, params);
	  }catch (TechnicalException te) {
	      resultado=false;
	      m_Log.error("JDBC Technical problem " + te.getMessage());
	  }catch (Exception ce) {
	      resultado = false;
	      m_Log.error("JDBC Technical problem " + ce.getMessage());
          ce.printStackTrace();
      }
      return resultado;
  }
  

  public int actualizaListaDocumentosExpediente(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("ActualizaListaDocumentosExpediente");

    int resultado = 0;
    try{
      resultado  = FichaExpedienteDAO.getInstance().actualizaListaDocumentosExpediente(gVO, params);
    }catch (TechnicalException te) {
      resultado=0;
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      resultado = 0;
      e.printStackTrace();
    }finally{
      return resultado;
    }
  }

  public int grabarExpediente(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("grabarExpediente");

    int resultado = 0;
    try{
      resultado  = FichaExpedienteDAO.getInstance().grabarExpediente(gVO, params);
    }catch (TechnicalException te) {
      resultado=0;
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      resultado = 0;
      e.printStackTrace();
    }finally{
      return resultado;
    }
  }
   public int grabarExpedienteObservaciones(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("grabarExpedienteObservaciones");

    int resultado = 0;
    try{
      resultado  = FichaExpedienteDAO.getInstance().grabarExpedienteObservaciones(gVO, params);
    }catch (TechnicalException te) {
      resultado=0;
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      resultado = 0;
      e.printStackTrace();
    }finally{
      return resultado;
    }
  }

  public int iniciarExpediente(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("insertarExpediente");

    int resultado = 0;
    try{
      resultado  = FichaExpedienteDAO.getInstance().iniciarExpediente(gVO, params);
    }catch (TechnicalException te) {
      resultado=0;
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      resultado = 0;
      e.printStackTrace();
    }finally{
      return resultado;
    }
  }
      
  
  public Vector cargaTramitesDisponibles(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("cargaExpedientes");

    Vector tramites = new Vector();

    try{
      tramites = FichaExpedienteDAO.getInstance().cargaTramitesDisponibles(gVO, params);
    }catch (TechnicalException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      return tramites;
    }
  }

    public void retrocederExpediente(GeneralValueObject gVO, String[] params) throws TramitacionException, EjecucionSWException {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("retrocederExpediente");

        try {
        
        	FichaExpedienteDAO.getInstance().retrocederExpedienteCH(gVO, params);
                
        } catch (EjecucionSWException eswe) {
            throw eswe;
        } catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException(te.getMessage(), te);
        } catch(Exception e){
            m_Log.error("Exception problem " + e.getMessage());
            throw new TramitacionException(e.getMessage(), e);
        }
    }
    
      
    
  public Vector cargaEstructuraDatosSuplementarios(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("cargaEstructuraDatosSuplementarios");
    Vector lista = new Vector();

    try{
      lista = FichaExpedienteDAO.getInstance().cargaEstructuraDatosSuplementarios(gVO, params);
    }catch (TechnicalException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      return lista;
    }
  }
  
   
  public Vector cargaEstructuraInteresados(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("cargaEstructuraInteresados");
    Vector lista = new Vector();

    try{
      lista = FichaExpedienteDAO.getInstance().cargaEstructuraInteresados(gVO, params);
    }catch (TechnicalException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      return lista;
    }
  }

    public Vector cargaValoresDatosSuplementarios(GeneralValueObject gVO,Vector eCs, String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("cargaValoresDatosSuplementarios");
      Vector lista = new Vector();

      try{
        lista = FichaExpedienteDAO.getInstance().cargaValoresDatosSuplementarios(gVO,eCs, params);
      }catch (TechnicalException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        e.printStackTrace();
      }finally{
        return lista;
      }
    }

    public Vector cargaValoresDatosSuplEtiquetas(GeneralValueObject gVO, Vector eCs, String[] params) {

        //Queremos estar informados de cuando este metodo es ejecutado
        if (m_Log.isDebugEnabled()) m_Log.debug("COMIENZO DE FichaExpedienteManager.cargaValoresDatosSuplementarios()");
        Vector lista = new Vector();

        try {
            lista = FichaExpedienteDAO.getInstance().cargaValoresDatosSuplEtiquetas(gVO, eCs, params);
        } catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public GeneralValueObject cargaValoresFicheros(GeneralValueObject gVO,Vector eCs, String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("cargaValoresDatosSuplementarios");
      GeneralValueObject lista = new GeneralValueObject();

      try{
        lista = FichaExpedienteDAO.getInstance().cargaValoresFicheros(gVO,eCs, params);
      }catch (TechnicalException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        e.printStackTrace();
      }finally{
        return lista;
      }
    }
     
    
    
    
    
    public GeneralValueObject cargaTiposFicheros(GeneralValueObject gVO,Vector eCs, String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("cargaValoresDatosSuplementarios");
      GeneralValueObject lista = new GeneralValueObject();

      try{
        lista = FichaExpedienteDAO.getInstance().cargaTiposFicheros(gVO,eCs, params);
      }catch (TechnicalException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        e.printStackTrace();
      }finally{
        return lista;
      }
    }    
    
    
     /**
     * Recupera los nombres de los ficheros correspondientes a los datos suplementarios de un expediente
     * @param gVO
     * @param eCs
     * @param params
     * @return
     */
     public GeneralValueObject cargaNombresFicheros(GeneralValueObject gVO,Vector eCs, String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug(">>>>>>cargaNombresFicheros");
      GeneralValueObject lista = new GeneralValueObject();

      try{
        lista = FichaExpedienteDAO.getInstance().cargaNombreFicheros(gVO,eCs, params);
      }catch (TechnicalException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        e.printStackTrace();
      }finally{
        m_Log.debug("<<<<<<<<<cargaNombresFicheros");  
        return lista;
      }
    }    
     
     
   /**
     * Recupera los nombres de los ficheros y son longitudes correspondientes a los datos suplementarios de tipo fichero, 
     * definidos a nivel de expediente
     * @param gVO: Objeto de tipo GeneralValueObject con los datos del expediente
     * @param eCs: Estructura de los campos suplementarios
     * @param params: Parámetros de conexión a la BBDD
     * @return Hashtable<String,GeneralValueObject> con 2 elementos, uno con los nombre y otro con las longitudes de los 
     * ficheros
     
     public Hashtable<String,GeneralValueObject> cargaNombresLongitudesFicheros(GeneralValueObject gVO,Vector eCs, String[] params) {        
         Hashtable<String,GeneralValueObject> salida = new Hashtable<String, GeneralValueObject>();
        
         Connection con = null;
         try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
             
            salida = FichaExpedienteDAO.getInstance().cargaNombreLongitudFicheros(gVO,eCs,con);
              
              
         }catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
         }catch (Exception e) {
             e.printStackTrace();
         }finally{
             try{
                 if(con!=null) con.close();
             }catch(Exception e){
                 e.printStackTrace();
             }
         }        
         return salida;
     }    */
     
     
     
    


    public ArrayList<ExpedienteOtroDocumentoVO> obtenerOtrosDocumentosExpediente(String municipio, String ejecicio, String numeroExpediente, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("obtenerOtrosDocumentosExpediente");
        ArrayList<ExpedienteOtroDocumentoVO> listaOtrosDocsExpediente = new ArrayList<ExpedienteOtroDocumentoVO>();

        try {
            listaOtrosDocsExpediente = ExpedienteOtroDocumentoDAO.getInstance().listaOtrosDocumentosExpediente(municipio, ejecicio, numeroExpediente, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return listaOtrosDocsExpediente;
        }
    }


    /**
     * Obtiene la lista de documentos externos asociados al expediente
     * @param municipio: Código del municipio
     * @param ejecicio: Ejercicio
     * @param numeroExpediente: Número del expediente
     * @param expedienteHistorico: True si el expediente está en el histórico y false en caso contrario
     * @param params: Parámetros de conexión a la BBDD
     * @return ArrayList<ExpedienteOtroDocumentoVO>
     */
    public ArrayList<ExpedienteOtroDocumentoVO> obtenerOtrosDocumentosExpediente(String municipio, String ejecicio, String numeroExpediente, boolean expedienteHistorico,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("obtenerOtrosDocumentosExpediente");
        ArrayList<ExpedienteOtroDocumentoVO> listaOtrosDocsExpediente = new ArrayList<ExpedienteOtroDocumentoVO>();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try {
            
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
         
            listaOtrosDocsExpediente = ExpedienteOtroDocumentoDAO.getInstance().listaOtrosDocumentosExpediente(municipio, ejecicio, numeroExpediente, expedienteHistorico?"true":"false", con);
            
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error al obtener el listado de documentos externos del expediente: " + e.getMessage());
            
        } finally {
            try{
                adapt.devolverConexion(con);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }        
        return listaOtrosDocsExpediente;
    }

    
   
    

  public Vector getListaUnidadesUsuario(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("getListaUnidadesUsuario");
    Vector lista = new Vector();

    try{
      lista = FichaExpedienteDAO.getInstance().getListaUnidadesUsuario(gVO, params);
    }catch (TechnicalException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      return lista;
    }
  }

    private GestorAccesoRegistro crearGestor(int codOrganizacion) {

        Config m_ConfigTerceros = ConfigServiceHelper.getConfig("Registro");

        /*
        * Buscamos los identificadores de los servicios de busqueda instalados en el fichero techserver.
        * El resultado de la llamada sera una cadena con todos los identificadores separados por ';'.
        * Si no se lee ningun identificador del fichero de configuración, se utiliza el servicio de busqeuda por
        * defecto (Identificador = SGE).
        */
        String prefijoOrg = "Registro/" + codOrganizacion + "/";
        String strIdsServicios = m_ConfigTerceros.getString(prefijoOrg + "serviciosDisp");
        if (strIdsServicios == null || "".equals(strIdsServicios.trim())) {
            strIdsServicios = ID_SERVICIO_POR_DEFECTO;
            m_Log.error("NO SE HA PODIDO RECUPERAR UNA CADENA DE IDENTIFICADORES PARA LOS SERVICIOS DE BUSQUEDA DE " +
                    "TERCEROS. INICIAMOS CON EL SERVICIO DE BUSQEUDA POR DEFECTO");
        }

        /*
         * Transformamos la cadena de identificadores en un vector y cremos una instancia de la clase encargada de
         * gestionar la busqueda.
         */
        Vector<String> idsServiciosBusq = new Vector<String>();
        StringTokenizer idsTokenizer = new StringTokenizer(strIdsServicios, ";");
        while (idsTokenizer.hasMoreTokens()) idsServiciosBusq.add(idsTokenizer.nextToken());
        if (idsServiciosBusq.size() == 0) {
            idsServiciosBusq.add(ID_SERVICIO_POR_DEFECTO);
            m_Log.error("HA OCURRIDO UN ERROR AL PROCESAR LA CADENA DE IDENTIFICADORES DE SERVICIOS DE BUSQUEDA DE " +
                    "TERCEROS. INICIAMOS CON EL SERVICIO DE BUSQEUDA POR DEFECTO");
        }

        return new GestorAccesoRegistro(idsServiciosBusq, prefijoOrg);
    }

    public boolean tieneTramitesAbiertos(GeneralValueObject gVO, String[] params) throws TramitacionException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        boolean retorno=false;
        try {
            
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            
            retorno= FichaExpedienteDAO.getInstance().tieneTramitesAbiertos(gVO, con);
            
           } catch (TechnicalException te) {
            throw new TramitacionException(te.getMessage());
            
          } catch (BDException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled())
                m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
       
                e.printStackTrace();
            if (m_Log.isErrorEnabled())
                m_Log.error("Excepcion en " + getClass().getName() + "con mensaje: " + e.getMessage());
          } finally {
             try{
                oad.devolverConexion(con);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }        

        return retorno;
        
    }
    
    /**
     * Carga el listado de ficheros aportados a registro del tipo indicado para los asientos relacionados con 
     * el expediente indicado. Carga los datos necesarios para la posterior recuperacion de su contenido (no 
     * carga el array de bytes que forma el contenido del fichero). El estado de la anotacion debe ser 1
     * (aceptada).
     *
     * @return Un vector de ficheroVO.
     */
    public Vector<FicheroVO> cargarFicherosRegistro(String tipoRegistro, String numExpediente, String codMunicipio, String[] params) {

        m_Log.debug("FichaExpedienteManager->cargarFicherosRegistro");

        Vector<FicheroVO> ficheros = new Vector<FicheroVO>();

        try{
            ficheros = FichaExpedienteDAO.getInstance().cargarFicherosRegistro(tipoRegistro, numExpediente, codMunicipio, params);
        }catch (TechnicalException te) {
          m_Log.error("JDBC Technical problem " + te.getMessage());
        }catch (Exception e) {
          e.printStackTrace();
        }
        return ficheros;        
    }
    
    
     /**
     * Carga el listado de ficheros aportados a registro del tipo indicado para los asientos relacionados con 
     * el expediente indicado. Carga los datos necesarios para la posterior recuperacion de su contenido (no 
     * carga el array de bytes que forma el contenido del fichero). El estado de la anotacion debe ser 1
     * (aceptada).
     * @param tipoRegistro: Tipo de registro (E:Entrada, S: Salida)
     * @param numExpediente: Número del expediente
     * @param codMunicipio: Código del municipio
     * @param expedienteHistorico: True si está en el histórico y false en caso contrario
     * @param params: Parámetros de conexión a la BBDD     
     * @return Un vector de ficheroVO.
     */
    public Vector<FicheroVO> cargarFicherosRegistro(String tipoRegistro, String numExpediente, String codMunicipio,boolean expedienteHistorico,String[] params) {

        m_Log.debug("FichaExpedienteManager->cargarFicherosRegistro");

        Vector<FicheroVO> ficheros = new Vector<FicheroVO>();

        try{
            ficheros = FichaExpedienteDAO.getInstance().cargarFicherosRegistro(tipoRegistro, numExpediente, codMunicipio,expedienteHistorico,params);
        }catch (TechnicalException te) {
          m_Log.error("JDBC Technical problem " + te.getMessage());
        }catch (Exception e) {
          e.printStackTrace();
        }
        return ficheros;        
    }
    
    /**
     * Carga el listado de documentos aportados anteriormente por el ciudadano relacionados 
     * con el expediente indicado. 
     * @param String: Numero del expediente para el que se recuperan los documentos
     * @param params: Parametro de conexion a la BBDD
     * @return Un vector de ficheroVO
     */
    public ArrayList<FicheroVO> cargarListaFicherosAportadosAnterior(String tipo, String numExpediente, String[] params){
        m_Log.debug("FichaExpedienteManager -> cargarListaFicherosAportadosAnterior");
        
        ArrayList<FicheroVO> ficheros = new ArrayList<FicheroVO>();

        try{
            ficheros = FichaExpedienteDAO.getInstance().cargarListaFicherosAportadosAnterior(tipo, numExpediente, params);
        }catch(TechnicalException te){
            m_Log.error("JDBC Technical problem "+te.getMessage());
        }catch(Exception e){
            e.printStackTrace();
        }

        return ficheros;
    }    
    
    /**
     * Carga el listado de ficheros aportados a registro del tipo indicado para los asientos relacionados con 
     * el expediente indicado. Carga los datos necesarios para la posterior recuperacion de su contenido (no 
     * carga el array de bytes que forma el contenido del fichero). El estado de la anotacion debe ser 1
     * (aceptada).
     * @param gVO: Objeto de la clave GeneralValueObject con los datos del expediente para el que se recuperan los documentos
     * de inicio
     * @param expedienteHistorico: True si el expediente está en el histórico, y false en caso contrario
     * @param params: Parámetro de conexión a la BBDD
     * @return Un vector de ficheroVO.
     */
    public Vector<FicheroVO> cargaDocumentosExpediente(GeneralValueObject gVO,boolean expedienteHistorico,String[] params) {

        m_Log.debug("FichaExpedienteManager->cargarFicherosRegistro");

        Vector<FicheroVO> ficheros = new Vector<FicheroVO>();

        try{
            ficheros = FichaExpedienteDAO.getInstance().cargaDocumentosExpediente(gVO,expedienteHistorico,params);
            
        }catch (TechnicalException te) {
          m_Log.error("JDBC Technical problem " + te.getMessage());
        }catch (Exception e) {
          e.printStackTrace();
        }
        return ficheros;        
    }
    
    /**
     * Carga el listado de ficheros existentes en los trámites del expediente indicado. Carga los datos necesarios 
     * para la posterior recuperacion de su contenido (no carga el array de bytes que forma el contenido del fichero). 
     * Se tiene en cuenta si el usuario tiene permisos para los trámites.
     *
     * @return Un vector de ficheroVO.
     */
     public Vector<FicheroVO> cargarFicherosTramites(UsuarioValueObject usuarioVO, 
            String numExpediente, String codMunicipio, boolean expHistorico, String[] params) {
        
        
        m_Log.debug("FichaExpedienteManager->cargarFicherosTramites");

        Vector<FicheroVO> ficheros = new Vector<FicheroVO>();

        try{
            ficheros = FichaExpedienteDAO.getInstance().cargarFicherosTramites(usuarioVO,
                    numExpediente, codMunicipio, expHistorico, params);
        }catch (TechnicalException te) {
          m_Log.error("JDBC Technical problem " + te.getMessage());
        }catch (Exception e) {
          e.printStackTrace();
        }
        return ficheros; 
    }
    
    /**
     * Devuelve el contenido binario de un fichero guardado en la BD.
     * 
     * @param fichero  FicheroVO que contiene los datos para recuperar el fichero de BD.
     * 
     * @return  Contenido del fichero en forma de array de bytes.
     */
    public byte[] cargarValorFichero(FicheroVO fichero, String[] params) {
        
        m_Log.debug("FichaExpedienteManager->cargarValorFichero");

        byte[] valor = new byte[0];

        try{
            valor = FichaExpedienteDAO.getInstance().cargarValorFichero(fichero, params);
        }catch (TechnicalException te) {
          m_Log.error("JDBC Technical problem " + te.getMessage());
        }catch (Exception e) {
          e.printStackTrace();
        }
        return valor;
    }
    
    public boolean existeExpedienteConDocumento(String[] params, int codOrganizacion, String codProcedimiento, int numDocumento) throws TramitacionException {
        try {
            return FichaExpedienteDAO.getInstance().existeDocumentoExpediente(params, codOrganizacion, codProcedimiento, numDocumento);
        } catch (TechnicalException ex) {
            throw new TramitacionException(ex.getMessage(), ex);
        }
    }

     /**
     * Añade a las observaciones de un procedimiento lo que hay en el parámetro observaciones
     * @param codProcedimiento: Cod del procedimiento
     * @param observaciones: Observaciones a añadir
     * @param params: Parámetros de conexión a la BBDD
     */
    public void guardarObservaciones(String codProcedimiento,String observaciones, String[] params){
        try{
            FichaExpedienteDAO.getInstance().guardarObservaciones(codProcedimiento, observaciones, params);
        }
        catch(Exception e){
            e.printStackTrace();
            m_Log.error(e.getMessage());
        }
    }

     /**
     * Recupera los datos del registro relacionado a un expediente y lo guarda en los campos "codUnidadRegistro",
     * "numeroAsiento" y "ejercicioAsiento" del parámetro gVO
     * @param gVO
     * @param params
     */
    public void getRegistroRelacionado(GeneralValueObject gVO, String[] params){
        try {
            FichaExpedienteDAO.getInstance().getRegistroRelacionado(gVO, params);
        }
        catch(Exception e){
            m_Log.error(e.getMessage());
        }
    }

    
    private TramitacionExpedientesValueObject generalValueObjectToTramitacionExpedientesValueObject(GeneralValueObject gVO){
        TramitacionExpedientesValueObject teVO = new TramitacionExpedientesValueObject();

        teVO.setCodMunicipio((String)gVO.getAtributo("codMunicipio"));
        teVO.setCodProcedimiento((String)gVO.getAtributo("codProcedimiento"));
        teVO.setEjercicio((String)gVO.getAtributo("ejercicio"));
        teVO.setCodTramite((String)gVO.getAtributo("codTramiteRetroceder"));
        teVO.setNumeroExpediente((String)gVO.getAtributo("numero"));
        teVO.setNumero((String)gVO.getAtributo("numero"));
        teVO.setOcurrenciaTramite((String)gVO.getAtributo("ocurrenciaTramiteRetroceder"));
        
        return teVO;
    } 


    /**
     * Método de retroceso de un expediente con el nuevo método de vuelta atrás
     * @param gVO: GeneralValueObject con los datos de trámite a retroceder
     * @param params: Parámetros de conexión a la base de datos
     * @throws es.altia.agora.business.sge.exception.TramitacionException
     * @throws es.altia.agora.business.integracionsw.exception.EjecucionSWException
     */
      public RespuestaRetrocesoTramiteVO  retrocederExpedienteMetodoAtrasNuevo(GeneralValueObject gVO, String[] params) throws TramitacionException, EjecucionSWException,EjecucionModuloException {

         RespuestaRetrocesoTramiteVO respuesta = null;
          //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("retrocederExpedienteMetodoAtrasNuevo");
        Connection con = null;
        try {
            String numExpediente      = (String)gVO.getAtributo("numero");
            String codTramite            = (String)gVO.getAtributo("codTramiteRetroceder");
            String ocurrenciaTramite  = (String)gVO.getAtributo("ocurrenciaTramiteRetroceder");
            String ejercicio                = (String)gVO.getAtributo("ejercicio");
            String codProcedimiento   = (String)gVO.getAtributo("codProcedimiento");
            String codMunicipio          = (String)gVO.getAtributo("codMunicipio");
            String fechaInicio            = (String)gVO.getAtributo("fechaInicio");
            
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            m_Log.debug(" ====> FichaExpedienteManger.retrocederExpedienteMetodoAtrasNuevo. Se pretende retroceder el  trámite " + codTramite + " con ocurrencia: " + ocurrenciaTramite);

            TramitacionExpedientesDAO teDAO = TramitacionExpedientesDAO.getInstance();
            boolean expFinalizado = teDAO.isExpedienteFinalizado(numExpediente,codProcedimiento,Integer.parseInt(codMunicipio),Integer.parseInt(ejercicio),con);
            
            if(!expFinalizado){
                boolean tramiteFinalizado = teDAO.isFinalizadoTramite(gVO, con);
                if(tramiteFinalizado){                    
                    // Si el trámite está finalizado => Se comprueba si es origen de algún trámite posterior
                    
                    // Se comprueba si el tramite es origen de algun otro trámite
                    boolean esOrigen = teDAO.esOrigenDeAlgunTramite(Integer.parseInt(codTramite),Integer.parseInt(ocurrenciaTramite),Integer.parseInt(ejercicio),codProcedimiento,Integer.parseInt(codMunicipio),numExpediente,con);                    
                    if(esOrigen){
                        // El trámite a retroceder está finalizado y es origen de algún tramite, por tanto, no se puede retroceder y se informa al usuario
                        ArrayList<TramitacionExpedientesValueObject> tramitesAbiertos = teDAO.getTramitesAbiertosPor(generalValueObjectToTramitacionExpedientesValueObject(gVO), con);
                        respuesta = new RespuestaRetrocesoTramiteVO();
                        respuesta.setTramitesDestino(tramitesAbiertos);                        
                        respuesta.setTipoRespuesta(ConstantesDatos.TRAMITE_FINALIZADO_CON_TRAMITES_POSTERIORES);

                        // Se muestra el error porque no se puede retroceder el expediente
                        if(con!=null) con.close();
                        
                    }else{
                                              
                        // Se comprueba la fecha de implantación si existe el parámetro
                        ResourceBundle bundle = ResourceBundle.getBundle("common");

                        String sFechaImplantacion = null;
                        try{
                           sFechaImplantacion= bundle.getString("fecha_implantacion_vuelta_atras_nueva");
                        }catch(Exception e){
                            e.printStackTrace();
                            m_Log.error(e.getMessage());
                        }

                        if(sFechaImplantacion!=null && sFechaImplantacion.length()>0) {
                            SimpleDateFormat sf       = new SimpleDateFormat("dd/MM/yyyy");
                            Date dFechaImplantacion = sf.parse(sFechaImplantacion);
                            Calendar cFechaFin = teDAO.getFechaFinTramite(gVO, con);
                            Calendar cFechaImplantacion = Calendar.getInstance();
                            cFechaImplantacion.clear();
                            // Se cierra la conexión a la base de datos
                            if(con!=null) con.close();
                            cFechaImplantacion.setTime(dFechaImplantacion);
                            if(cFechaFin.after(cFechaImplantacion)){
                                //Se retrocede por el método nuevo porque se ha finalizado después de la implantación
                                m_Log.debug(" ==============> SE RETROCEDE POR EL MÉTODO NUEVO PORQUE LA FECHA DE FIN DEL TRÁMITE ES POSTERIOR A LA DE IMPLANTACIÓN");
                                respuesta = FichaExpedienteDAO.getInstance().retrocederExpedienteCHMetodoNuevo(gVO, params);
                                m_Log.debug(" SE HA RETROCEDIDO POR EL MÉTODO NUEVO <===================== ");
                            }else
                            if(cFechaFin.before(cFechaImplantacion)){
                                // Se retrocede por el método viejo
                                m_Log.debug(" ==============> SE RETROCEDE POR EL MÉTODO VIEJO PORQUE LA FECHA DE FIN DEL TRÁMITE ES ANTERIOR A LA DE IMPLANTACIÓN");
                                //FichaExpedienteDAO.getInstance().retrocederExpedienteCH(gVO, params);
                                respuesta = FichaExpedienteDAO.getInstance().retrocederExpedienteCHViejoRedefinido(gVO,params);
                                m_Log.debug(" SE HA RETROCEDIDO POR EL MÉTODO VIEJO <===================== ");
                            }
                        }else{
                            // TODO: Si no existe el parametro fecha de implantación, se da por hecho que es una instalación nueva y
                            // se retrocede por el nuevo método de vuelta atrás
                            respuesta = FichaExpedienteDAO.getInstance().retrocederExpedienteCHMetodoNuevo(gVO, params);
                        }
                    }
                }else{
                        // El expediente está pendiente, entonces se comprueba si está en la lista de trámites de origen. Si lo está entonces,   se retrocede por el método nuevo, sino lo está, se hace por el viejo
                       // Se retrocede el trámite pasandolo a estado pendiente por el método nuevo
                        TramitacionExpedientesValueObject tramiteOrigen = teDAO.getTramiteOrigen(Integer.parseInt(codTramite), Integer.parseInt(ocurrenciaTramite), Integer.parseInt(ejercicio), codProcedimiento,Integer.parseInt(codMunicipio) ,numExpediente, con);
                        if(tramiteOrigen!=null && tramiteOrigen.getCodTramite()!=null && Utilidades.isInteger(tramiteOrigen.getCodTramite()) && tramiteOrigen.getOcurrenciaTramite()!=null && Utilidades.isInteger(tramiteOrigen.getOcurrenciaTramite())){
                            // Si el trámite está en la lista de trámites de origen, entonces se retrocede por el MÉTODO NUEVO.                             
                            respuesta =  FichaExpedienteDAO.getInstance().retrocederExpedienteCHMetodoNuevo(gVO, params);
                           
                        }else{                        
                            // El trámite no está en la lista de trámites de origen, entonces se retrocede por el MÉTODO VIEJO
                             m_Log.debug(" ==============> SE RETROCEDE POR EL MÉTODO VIEJO ");
                             //FichaExpedienteDAO.getInstance().retrocederExpedienteCH(gVO, params);
                             respuesta = FichaExpedienteDAO.getInstance().retrocederExpedienteCHViejoRedefinido(gVO,params);
                             m_Log.debug(" SE HA RETROCEDIDO POR EL MÉTODO VIEJO <===================== ");
                        }

                         if(con!=null) con.close();
                }
            }else{
                 if(con!=null) con.close();
                // Si el expediente está finalizado no se podrá retroceder => Se muestra el correspondiente mensaje de error
                respuesta = new RespuestaRetrocesoTramiteVO();                
                respuesta.setTipoRespuesta(ConstantesDatos.EXPEDIENTE_FINALIZADO_NO_RETROCEDER);
            }

            
        } catch (EjecucionSWException eswe) {
            eswe.printStackTrace();
            respuesta = null;
            throw eswe;
        }  catch(EjecucionModuloException e){
            m_Log.error(e.getMessage());
            respuesta = null;
            throw e;                    
        }
        catch (TechnicalException te) {
            te.printStackTrace();
            respuesta = null;
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException(te.getMessage(), te);
        } catch(Exception e){
            e.printStackTrace();
            respuesta = null;
            m_Log.error("Exception problem " + e.getMessage());
            throw new TramitacionException(e.getMessage(), e);
        }
        finally{
            try{
                 if(con!=null) con.close();
            }catch(Exception e){
                m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }

        return respuesta;
    }

   
     public String getFechaInicio(GeneralValueObject gVO,String[] params){
         String fecha ="";
         Connection con = null;
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            fecha = FichaExpedienteDAO.getInstance().getFechaInicio(gVO,con);
        }catch(Exception e){

        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

         return fecha;
     }

public ArrayList<GeneralValueObject> recuperarExternos(String[] params,GeneralValueObject gVO){
         String retorno ="";
         Connection con = null;
         ArrayList<GeneralValueObject> resultados = new ArrayList<GeneralValueObject>();
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
              
            String CampoExterno = FichaExpedienteDAO.getInstance().recuperarCodigoExterno(con, gVO); 
            String[] parametros = FichaExpedienteDAO.getInstance().recuperarParametrosConexionExternos(con, CampoExterno); 
            resultados = FichaExpedienteDAO.getInstance().recuperarDatosExternos(parametros);
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

         return resultados;
     }
public String InteresadoObligatorio(String[] params,GeneralValueObject gVO){ 
         String retorno ="";
         Connection con = null;
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();             
            String InteresadosObligatorio = FichaExpedienteDAO.getInstance().interesadoObligatorio(con, gVO);              
            retorno = InteresadosObligatorio;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
         return retorno;
     } 
public Vector cargaEstructuraDatosSuplementariosProcedimiento(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("cargaEstructuraDatosSuplementariosProcedimiento");
    Vector lista = new Vector();

    try{
        lista = FichaExpedienteDAO.getInstance().cargaEstructuraDatosSuplementariosProcedimiento(gVO, params);
    }catch (TechnicalException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      return lista;
    }
  }

public boolean copiarInteresados(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("copiarInteresados");
    boolean interesados=false;

    try{
        interesados = FichaExpedienteDAO.getInstance().copiarInteresados(gVO, params);
    }catch (TechnicalException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      return interesados;
    }
  }
public boolean copiarLocalizacion(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("copiarLocalizacion");
    boolean localizacion=false;

    try{
        localizacion = FichaExpedienteDAO.getInstance().copiarLocalizacion(gVO, params);
    }catch (TechnicalException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      return localizacion;
    }
  }
public boolean copiarDatosSuplementarios(GeneralValueObject gVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("copiarDatosSuplementarios");
    boolean datosSupl=false;

    try{
        datosSupl = FichaExpedienteDAO.getInstance().copiarDatosSuplementarios(gVO, params);
    }catch (TechnicalException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      return datosSupl;
    }
  }

    public void iniciarCircuitoFirmas(ArrayList<FirmasDocumentoProcedimientoVO> firmas,int codDocPresentado, int codUnidadOrganicaExp, String[] params) throws TechnicalException {
        try {
            AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
            Connection con = abd.getConnection();
            FichaExpedienteDAO.getInstance().iniciarCircuitoFirmas(firmas, codDocPresentado, codUnidadOrganicaExp, con);
        } catch (BDException ex) {
            Logger.getLogger(FichaExpedienteManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new TechnicalException(("ERROR EN GETCONNECTION EN INICIARCIRCUITOFIRMAS"), ex);
        }
    }

    public ArrayList<FirmasDocumentoExpedienteVO> getFirmasDocumento(int codDocumentoAdjunto, String[] params) throws TechnicalException {
        ArrayList<FirmasDocumentoExpedienteVO> firmasDocumentoExpediente = new ArrayList<FirmasDocumentoExpedienteVO>();
        try {
            AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
            Connection con = abd.getConnection();
            firmasDocumentoExpediente = FichaExpedienteDAO.getInstance().getFirmasDocumento(codDocumentoAdjunto,con);
        } catch (BDException ex) {
            Logger.getLogger(FichaExpedienteManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new TechnicalException(("ERROR EN GETCONNECTION EN GETFIRMASDOCUMENTO"), ex);
        }
        return firmasDocumentoExpediente;
    }


  public byte[] cargarFirmaDocumento(Integer codDocumento , String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    if (m_Log.isDebugEnabled()){m_Log.debug("cargarFirmaDocumento");}
    byte[] salida=null;
    try{
      salida = FichaExpedienteDAO.getInstance().cargarFirmaDocumento(codDocumento,params);
    }catch (Exception e) {
      m_Log.error("cargarFirmaDocumento(): Error " + e.getMessage());
      e.printStackTrace();
    }finally{
      return salida;
    }
  }
 
  
  
  public boolean verificarSubsanacionDocumento(String numExpediente,String codProcedimiento,String numDocumento,UsuarioValueObject usuario,String[] params){    
      
      AdaptadorSQLBD adapt = null;
      Connection con = null;
      boolean exito = false;
      
      m_Log.debug(" FichaExpedienteManager.verificarSubsanacionDocumento =============>");
      try{
          adapt = new AdaptadorSQLBD(params);          
          con = adapt.getConnection();
          
          exito = FichaExpedienteDAO.getInstance().verificarSubsanacionDocumento(numExpediente, codProcedimiento, numDocumento, usuario, con);
                    
      }catch(BDException e){
          e.printStackTrace();
      }finally{
          try{
              if(con!=null) con.close();
          }catch(SQLException e){
              e.printStackTrace();
          }
      }
      
      m_Log.debug(" FichaExpedienteManager.verificarSubsanacionDocumento <=============");
      return exito;      
  }
  
    public int obtenerEstadoExpediente(String numExpediente, String[] params) {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        int estado = -1;

        m_Log.debug(" FichaExpedienteManager.obtenerEstadoExpediente =============>");
        try{
            adapt = new AdaptadorSQLBD(params);          
            con = adapt.getConnection();

            estado = FichaExpedienteDAO.getInstance().obtenerEstadoExpediente(numExpediente, con);

        }catch(BDException e){
            m_Log.error("Ha ocurrido un error al obtener la conexión a la base de datos: " + e.getMessage(), e);
        }catch(SQLException e){
            m_Log.error("Ha ocurrido un error al consultar el estado del expediente: " + e.getMessage(), e);
        }catch(Exception e){
            m_Log.error("Ha ocurrido un error al consultar el estado del expediente: " + e.getMessage(), e);
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                m_Log.error("Ha ocurrido un error al liberar la conexión de la base de datos: " + e.getMessage(), e);
            }
        }

        m_Log.debug(" FichaExpedienteManager.obtenerEstadoExpediente <=============");
        return estado;
    }
  
   private String concatenaNombreTercero(TercerosValueObject terVO){        
        return FormateadorTercero.getDescTercero(terVO.getNombre(), terVO.getApellido1(), terVO.getApellido2(), false);
   }
  
    private GeneralValueObject obtenerDatosCodPostal(TercerosValueObject terVO){

        GeneralValueObject gVO = new GeneralValueObject();
        DomicilioSimpleValueObject domVO = (DomicilioSimpleValueObject)terVO.getDomicilios().get(0);
        gVO.setAtributo("codPais",domVO.getIdPais());
        gVO.setAtributo("codProvincia", domVO.getIdProvincia());
        gVO.setAtributo("codMunicipio", domVO.getIdMunicipio());
        gVO.setAtributo("descPostal",domVO.getCodigoPostal());
        gVO.setAtributo("defecto", 0);
        return gVO;
    }
    
    
    
    
    /**
     * 
     * @param codOrganizacion: Código de la organización:
     * @param usuario: Código del usuario de Flexia que realiza la oeración
     * @param gVODatosInteresado: Objeto de la clase GeneralValueObject con los datos del interesado
     * @param gVO: Objeto de la clase GeneralValueObject con los datos del expediente
     * @param con: Conexión a la BBDD
     * @return int con los valores:
     *                  0 --> OK
     *                 -1 --> No se ha podido actualizar la marca de notificación electrónica del interesado en el expedinete
     *                 -2 --> Error al dar de alta el domicilio
     *                 -3 --> Error al dar de alta los campos suplementarios del interesado del expediente
     * @throws TechnicalException 
     */
    private int altaDomicilioInteresadoPrincipalExpedienteRolDefecto(String codOrganizacion,String usuario,GeneralValueObject gVOInteresado,GeneralValueObject gVO,AdaptadorSQLBD adapt,Connection con) throws TechnicalException{
        int salida = -1;
        int idTercero =-1;
        int versionTercero = -1;
        Vector gVODatosSuplementarios = new Vector();
        Vector estructuraDatosSuplementarios = new Vector();       
        Vector valoresDatosSuplementarios = new Vector();      
        
        try{
            
            GeneralValueObject gVODatosInteresado = (GeneralValueObject) gVOInteresado.getAtributo("datosInteresado"); 
            
            
            // Se trata de actualizar un interesado principal que viene con Domicilio, el código de rol de procedimiento
            // en el que se inicia expediente, y la marca de si admite o no notificación electrónica en el expedinete
            String codRol = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_ROL)).trim(); 
            String notificacionElectronica = "0";
            if(gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOTIFICACIONELECTRONICA)!=null)
                notificacionElectronica = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOTIFICACIONELECTRONICA)).trim();                   

            
            gVO.setAtributo("codRol",codRol);
            gVO.setAtributo("notifElect",notificacionElectronica);

            TercerosValueObject tercero = FichaExpedienteDAO.getInstance().existeInteresadoRegistro(gVO, con);
            if (tercero != null){

                idTercero = Integer.parseInt(tercero.getCodTerceroOrigen());
                versionTercero = Integer.parseInt(tercero.getVersion());

                int rowsUpdate = FichaExpedienteDAO.getInstance().actualizarInteresadoExpediente(gVO, tercero, con);

                if(rowsUpdate!=1){
                    salida = -1;
                }else{

                    String rol = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_ROL)).trim(); 
                    String provincia = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_PROVINCIA));            
                    if (provincia != null) {
                     provincia = provincia.trim();    
                    }else{
                        provincia = m_ConfigCommon.getString(codOrganizacion + ConstantesDatos.CODIGO_PROVINCIA_DESCONOCIDO);
                    }

                    String municipio = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_MUNICIPIO));
                    if (municipio != null){
                        municipio = municipio.trim();
                    }else{
                        municipio = m_ConfigCommon.getString(codOrganizacion + ConstantesDatos.CODIGO_MUNICIPIO_DESCONOCIDO);
                    }
                    String nombreVia = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOMBREVIA);
                    if (nombreVia != null) {
                        nombreVia = nombreVia.trim();
                    }else{
                        nombreVia = m_ConfigCommon.getString(codOrganizacion + ConstantesDatos.DESCRIPCION_VIA_DESCONOCIDA);
                    }

                    String emplazamiento = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_EMPLAZAMIENTO);
                    if (emplazamiento != null) {emplazamiento = emplazamiento.trim();}
                    String apellido2 = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_APELLIDO2);
                    if (apellido2 != null) {apellido2 = apellido2.trim();}
                    String telefono = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_TELEFONO);
                    if (telefono != null) {telefono = telefono.trim();}
                    String email = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_EMAIL);
                    if (email != null) {email = email.trim();}
                    String tipoVia = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_TIPOVIA);
                    if (tipoVia != null) {tipoVia = tipoVia.trim();}            
                    String letraDesde = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_LETRADESDE);
                    if (letraDesde != null) {letraDesde = letraDesde.trim();}            
                    String letraHasta = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_LETRAHASTA);
                    if (letraHasta != null) {letraHasta = letraHasta.trim();}            
                    String bloque = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_BLOQUE);
                    if (bloque != null) {bloque = bloque.trim();}            
                    String portal = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_PORTAL);
                    if (portal != null) {portal = portal.trim();}            
                    String escalera = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_ESCALERA);
                    if (escalera != null) {escalera = escalera.trim();}            
                    String planta = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_PLANTA);
                    if (planta != null) {planta = planta.trim();}            
                    String puerta = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_PUERTA);
                    if (puerta != null) {puerta = puerta.trim();}            
                    String codigoPostal = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_CODIGOPOSTAL);
                    if (codigoPostal != null) {codigoPostal = codigoPostal.trim();}            
                    String numeroDesde = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NUMERODESDE);
                    if (numeroDesde != null) {numeroDesde = numeroDesde.trim();}            
                    String numeroHasta = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NUMEROHASTA);
                    if (numeroHasta != null) {numeroHasta = numeroHasta.trim();}            
                    String pais = ConstantesDatos.CODIGO_PAIS_ESPAÑA;
                    String notifElectronica = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOTIFICACIONELECTRONICA);
                    if (notifElectronica != null) {notifElectronica = notifElectronica.trim();}                       
                    //El campo codigo de via no se recupera del XML porque no se va utilizar al ser diferentes los codigos de vía que 
                    //manejar ellos con los que tenemos nosotros en Flexia


                    //Da de alta el domicilio (T_DOM, T_DMM)
                    DomicilioSimpleValueObject domicilio = new DomicilioSimpleValueObject();
                    domicilio.setIdPais(pais);
                    domicilio.setIdProvincia(provincia);
                    domicilio.setIdMunicipio(municipio);
                    domicilio.setDescVia(nombreVia);

                    if (tipoVia != null && tipoVia.length()>0) {domicilio.setTipoVia(tipoVia);}
                    else
                    domicilio.setTipoVia(Integer.toString(es.altia.agora.technical.ConstantesDatos.TIPO_VIA_SINVIA));
                    if (emplazamiento != null && emplazamiento.length()>0) {domicilio.setDomicilio(emplazamiento);}
                    if (letraDesde != null && letraDesde.length()>0) {domicilio.setLetraDesde(letraDesde);}
                    if (letraHasta != null && letraHasta.length()>0) {domicilio.setLetraHasta(letraHasta);}
                    if (numeroDesde != null && numeroDesde.length()>0) {domicilio.setNumDesde(numeroDesde);}        
                    if (numeroHasta != null && numeroHasta.length()>0) {domicilio.setNumHasta(numeroHasta);}
                    if (bloque != null && bloque.length()>0) {domicilio.setBloque(bloque);}
                    if (portal != null && portal.length()>0) {domicilio.setPortal(portal);}
                    if (escalera != null && escalera.length()>0) {domicilio.setEscalera(escalera);}
                    if (planta != null && planta.length()>0) {domicilio.setPlanta(planta);}
                    if (puerta != null && puerta.length()>0) {domicilio.setPuerta(puerta);}
                    if (codigoPostal != null && codigoPostal.length()>0) {domicilio.setCodigoPostal(codigoPostal);}
                    domicilio.setEsDomPrincipal("true");


                    // Se busca el domicilio
                    DomiciliosDAO domiciliosDAO = DomiciliosDAO.getInstance();
                    int idDomicilio = domiciliosDAO.buscarDireccion(domicilio, con);

                    if(idDomicilio==-1){
                        m_Log.debug("=====================> FichaExpedienteManager.actualizarExpedienteAsiento: Se ha buscado el domicilio pero no existe");
                        // No se ha encontrado el domicilio, entonces se procede a dar de alta
                        idDomicilio = domiciliosDAO.altaDomicilio(domicilio, usuario, con);

                    }else{
                        m_Log.debug("=====================> FichaExpedienteManager.actualizarExpedienteAsiento: Se ha buscado el domicilio y existe");
                    }

                    m_Log.debug(" ************************* idDomicilio: " + idDomicilio);

                    gVO.setAtributo("codMunicipio", codOrganizacion);
                    gVO.setAtributo("codigoTercero",String.valueOf(idTercero));  
                    gVO.setAtributo("versionTercero",Integer.toString(versionTercero));
                    gVO.setAtributo("domicilio",String.valueOf(idDomicilio)); 
                    gVO.setAtributo("rol",rol); 
                    gVO.setAtributo("notifElectronica", notifElectronica);                

                    // Se da de alta el domicilio para el tercero
                    m_Log.debug("=====================> FichaExpedienteManager actualizarExpedienteAsiento: Antes de proceder a dar de alta el domicilio para el tercero");
                    boolean altaDomicilio = TercerosDAO.getInstance().altaDomicilioTercero(idTercero, idDomicilio,usuario, con);
                    m_Log.debug("=====================> FichaExpedienteManager actualizarExpedienteAsiento: Después de proceder a dar de alta el domicilio para el tercero: " + altaDomicilio);
                    if (altaDomicilio==false){
                        salida = -2;
                    }else{                            
                   
                         m_Log.debug(" ************************* Se procede a dar de alta los datos suplementarios ***************");

                        //Inserta los datos suplementarios del interesado 
                        gVODatosSuplementarios = (Vector) gVOInteresado.getAtributo("datosSuplementarios");  

                        if (gVODatosSuplementarios != null && gVODatosSuplementarios.size()>0){                        

                            
                                try{
                                    Vector estructuraDatosSuplementariosAux = DatosSuplementariosTerceroDAO.getInstance().cargaEstructuraDatosSuplementariosTercero(codOrganizacion, adapt, con);
                                    if (estructuraDatosSuplementariosAux != null){
                                        //Filtra de la estructuta de datos suplementarios los que viene en el documento                        
                                        for (int j = 0; j < estructuraDatosSuplementariosAux.size(); j++ ){
                                           EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementariosAux.elementAt(j);
                                           for ( int k = 0; k < gVODatosSuplementarios.size(); k++ ){
                                                GeneralValueObject generalVO = (GeneralValueObject) gVODatosSuplementarios.get(k); 
                                                String codCampo = (String) generalVO.getAtributo(ConstantesDatos.TAG_XML_CODIGO);
                                                String valorCampo = (String) generalVO.getAtributo(ConstantesDatos.TAG_XML_VALOR);
                                                if (eC.getCodCampo().equalsIgnoreCase(codCampo)){
                                                    estructuraDatosSuplementarios.add(eC);
                                                    gVO.setAtributo(codCampo, valorCampo);
                                                    valoresDatosSuplementarios.add(gVO);
                                                }                                
                                           }// for
                                        }// for
                                        DatosSuplementariosTerceroDAO.getInstance().grabarDatosSuplementarios(codOrganizacion,String.valueOf(idTercero),estructuraDatosSuplementarios,valoresDatosSuplementarios,adapt,con);
                                    }
                                    salida = 0;
                                }catch(Exception e){
                                    salida = -3;
                                    e.printStackTrace();
                                }

                       }// if
                       else salida = 0;
                        
                    }// else    
                }
            }
                                    
       }catch(Exception e){
           e.printStackTrace();
           throw new TechnicalException("Error al tratar los datos del interesesado principal: " + e.getMessage(),e);
       }
        
       return salida;
    }
    
    
    
    
    /**
     * Método llamado únicamente y exclusivamente para guardar datos de interesado en un expediente, grabar campos 
     * suplementarios en el expediente y/o en el trámite de inicio y además para poder grabar valores de campos de tercero
     * en los interesados del expediente. La información se extrae de un documento de registro en formato XML con un determinado formato
     * @param listaInteresados: Colección con los interesados a dar de alta en un expediente
     * @param gVO: Objeto de la clase GeneralValueObject con la información del expediente
     * @param numExpedienteRelacionado: Número del expediente relacionado
     * @param params: Parámetros de conexión a la BBDD
     * @return int que puede tomar los siguientes valores:
     *  0 --> Error al dar de alta los interesados en el expedinete
     *  1 --> OK
     *  2 --> Error al guardar interesados, faltan campos obligatorios con información sobre los mismos                                          
     *  3 --> Error al guardar los interesados del fichero. El domicilio de algún interesado está incompleto, tienen que enviar la provincia, municipio, nombre vía y/o emplazamiento
     *  4 --> El domicilio de algunos de los interesados está incompleto                                         
     *  5 --> Error al guardar los interesados del fichero. No existe el interesado asociado a la anotación
     *  6 --> Se intenta relacionar el expediente iniciado con un expediente que no existe
     *  7 --> Error técnico al intentar relacionar el expediente recien iniciado con el expediente comunicado 
     */
    public int actualizarExpedienteAsiento(List listaInteresados, GeneralValueObject gVO, String numExpedienteRelacionado,String[] params) {

      m_Log.debug("FichaExpedienteManager.actualizarExpedienteAsiento2");

      Vector listaTerceros = new Vector();
      GeneralValueObject gVOInteresado = new GeneralValueObject();
      GeneralValueObject gVODatosInteresado = new GeneralValueObject(); 
      Vector gVODatosSuplementarios = new Vector();
      Vector estructuraDatosSuplementarios = new Vector();       
      Vector valoresDatosSuplementarios = new Vector();      
      TercerosValueObject terceroVO = new TercerosValueObject();
      TercerosValueObject terceroVOAux = new TercerosValueObject();
      EstructuraCampo eC = new EstructuraCampo(); 
      GeneralValueObject generalVO = new GeneralValueObject();       
      boolean actulizarTercero = false;
      int resultado = 0;
      int tipoDoc = 0;
      int idTercero = 0;      
      int versionTercero = -1;
      String usuario          = (String)gVO.getAtributo("usuario");
      String codOrganizacion  = (String)gVO.getAtributo("codMunicipio");
      String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
      int COD_ROL_DEFECTO_PROCEDIMIENTO = -1;
      
      AdaptadorSQLBD adapt = null;
      Connection con = null;
      try{
          
        adapt = new AdaptadorSQLBD(params);
        con = adapt.getConnection();
        adapt.inicioTransaccion(con);
        
        boolean errorPrimerPaso = false;
        if(numExpedienteRelacionado!=null && !"".equals(numExpedienteRelacionado)){
            
            if(!FichaExpedienteDAO.getInstance().existeExpediente(Integer.parseInt(codOrganizacion),numExpedienteRelacionado, con)){
                // Si se comunica un expediente a relacionar con el expediente ya iniciado, y este expediente no existe, entonces se comunica error.
                resultado = 6;     
                errorPrimerPaso = true;
            }else{
                // El expediente existe, entonces, se procede a realizar el expediente ya iniciado desde el buzón
                // de entrada, con el indicado en el parámetro numExpedienteRelaciado
                if(!FichaExpedienteDAO.getInstance().relacionarExpediente(Integer.parseInt(codOrganizacion),(String) gVO.getAtributo("numero"), numExpedienteRelacionado, con)){
                    resultado = 7;
                    errorPrimerPaso = true;
                }                
            }            
        }
            

        if(!errorPrimerPaso){
            COD_ROL_DEFECTO_PROCEDIMIENTO = TramitacionExpedientesDAO.getInstance().getCodRolPorDefecto(codProcedimiento, con);
        
            //Comprueba los campos obligatorios del XML
            for ( int i = 0; i < listaInteresados.size(); i++ ){

                gVOInteresado = (GeneralValueObject) listaInteresados.get(i);

                gVODatosInteresado = (GeneralValueObject) gVOInteresado.getAtributo("datosInteresado");

                //Si el interesado trae dos tags se considera que es el interesado principal del registro sino es cualquier otro interesado
                if (gVODatosInteresado.getSize() == ConstantesDatos.NUMERO_TAGS_INTERESADO_PRINCIPAL){

                    String notifElectronica = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOTIFICACIONELECTRONICA);
                    if (notifElectronica != null) {notifElectronica = notifElectronica.trim();}
                    String rol = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_ROL);   
                    if (rol != null) {rol = rol.trim();}                   

                    if (rol == null || rol.length()==0 || notifElectronica == null || notifElectronica.length()==0){
                        return 2;
                    }

                }else{

                    String documento = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NIF);
                    if (documento != null) {documento = documento.trim();}
                    String nombre = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOMBRE);
                    if (nombre != null) {nombre = nombre.trim();}
                    String apellido1 = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_APELLIDO1);            
                    if (apellido1 != null) {apellido1 = apellido1.trim();}

                    String provincia = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_PROVINCIA);
                    if (provincia != null) {provincia = provincia.trim();}
                    String municipio = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_MUNICIPIO); 
                    if (municipio !=null) {municipio = municipio.trim();}
                    String nombreVia = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOMBREVIA);
                    if (nombreVia != null){nombreVia = nombreVia.trim();}
                    String emplazamiento = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_EMPLAZAMIENTO);                        
                    if (emplazamiento != null) {emplazamiento = emplazamiento.trim();}
                    String rol = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_ROL);   
                    if (rol != null) {rol = rol.trim();}                  

                    boolean validarDocumento = true;
                    if(rol!=null && !"".equals(rol)){                    
                        int iCodRol = Integer.parseInt(rol);

                        if(iCodRol==COD_ROL_DEFECTO_PROCEDIMIENTO){
                            validarDocumento = false;
                        }                    
                    }


                    
                    if(validarDocumento){
                        //Valida los campos obligatorios
                       if (documento == null || documento.length()==0 || rol == null || rol.length()==0 || 
                            nombre == null || nombre.length()==0 || apellido1 == null || apellido1.length()==0 ){                           
                    
                           
                           
                           if((documento==null || (documento!=null && "0".equals(documento))) && nombre!=null && !"".equals(nombre) && apellido1==null){
                           }
                           else{
                               
                               // Se comprueba si se ha introducido el documento, y este es un CIF
                               m_Log.debug( " ==============> Se trata de un documento: " + Utilities.validaCIF(documento));
                               if(!(documento!=null && !"".equals(documento) && nombre!=null && !"".equals(nombre) && Utilities.validaCIF(documento)) && !(documento!=null && !"".equals(documento) && nombre!=null && !"".equals(nombre) && Utilities.validaNIF(documento)))
                                   return 2;
                           }
                       }  
                    }
                    
                    
                    /*** original
                    if(validarDocumento){
                        //Valida los campos obligatorios
                       if (documento == null || documento.length()==0 || rol == null || rol.length()==0 || 
                            nombre == null || nombre.length()==0 || apellido1 == null || apellido1.length()==0 ){                           
                           
                           if((documento==null || (documento!=null && "0".equals(documento))) && nombre!=null && !"".equals(nombre) && apellido1==null){
                           }
                           else
                            return 2;
                       }  
                    }
                    **/
                    
                    if(provincia==null && municipio==null){
                       if(nombreVia==null && emplazamiento==null){
                       }else{
                            return 3;
                       }

                    }else
                    if(provincia!=null && municipio!=null){
                        if(nombreVia!=null || emplazamiento!=null){
                        }else{
                            return 3;
                       }
                    }else{
                        return 3;
                    }

                    if(!validarDocumento){
                        // Si esta variable está a false es porque el rol coincide con el rol del interesado principal
                        // en el procedimiento en el que ya se ha iniciado un expediente. 
                        // Si se llega hasta aquí es porque los datos del domicilio son correctos, por tanto, lo 
                        // que se pretende es añadir un nuevo domicilio para el interesado con rol principal en el expediente
                        gVODatosInteresado.setAtributo("altaDomicilioInteresadoRolDefecto","SI");
                        gVOInteresado.setAtributo("datosInteresado",gVODatosInteresado);
                        listaInteresados.set(i,gVOInteresado);
                    }
                }
            }

            ResourceBundle config = ResourceBundle.getBundle("common");
            GeneralValueObject interesadoRolPrincipal = null;        
            ArrayList<GeneralValueObject> listaInteresadosTratar = new ArrayList<GeneralValueObject>();

            for(int i=0;i<listaInteresados.size();i++){

                GeneralValueObject datosInteresado = (GeneralValueObject) listaInteresados.get(i);
                GeneralValueObject interesado = (GeneralValueObject) datosInteresado.getAtributo("datosInteresado"); 

                String altaDomicilioInteresadoRolDefecto = (String)interesado.getAtributo("altaDomicilioInteresadoRolDefecto");
                if(altaDomicilioInteresadoRolDefecto!=null && altaDomicilioInteresadoRolDefecto.equalsIgnoreCase("SI")){
                    interesadoRolPrincipal = datosInteresado;
                }else
                    listaInteresadosTratar.add(datosInteresado);
            }



            boolean continuar = true;
            if(interesadoRolPrincipal!=null){
                try{
                    // Se procesa el interesado con rol principal porque hay que dar de alta el domicilio y asignarselo 
                    // al interesado del expediente
                    int salida = this.altaDomicilioInteresadoPrincipalExpedienteRolDefecto(codOrganizacion,usuario,interesadoRolPrincipal,gVO,adapt,con);                
                    m_Log.debug(" ======================> salida de la operación altaDomicilioInteresadoPrincipalExpedienteRolDefecto() salida: " + salida);

                }catch(TechnicalException e){
                    e.printStackTrace();
                    continuar = false;
                }
            }


            if(continuar){
                for ( int i = 0; i < listaInteresados.size(); i++ ){

                    gVOInteresado = (GeneralValueObject) listaInteresados.get(i);

                    gVODatosInteresado = (GeneralValueObject) gVOInteresado.getAtributo("datosInteresado"); 
                    
                    String altaDomicilioInteresadoRolDefecto = (String)gVODatosInteresado.getAtributo("altaDomicilioInteresadoRolDefecto");


                    //Si el interesado trae dos tags se considera que es el interesado principal del registro sino es cualquier otro interesado
                    if (gVODatosInteresado.getSize() == ConstantesDatos.NUMERO_TAGS_INTERESADO_PRINCIPAL){

                        String codRol = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_ROL)).trim(); 
                        String notificacionElectronica = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOTIFICACIONELECTRONICA)).trim();                   

                        gVO.setAtributo("codRol",codRol);
                        gVO.setAtributo("notifElect",notificacionElectronica);

                        TercerosValueObject tercero = FichaExpedienteDAO.getInstance().existeInteresadoRegistro(gVO, con);
                        if (tercero != null){
                            int rowsUpdate = FichaExpedienteDAO.getInstance().actualizarInteresadoExpediente(gVO, tercero, con);
                            if (rowsUpdate == 0){
                                return 5;
                            }else resultado = 1;
                        }else{
                            return 4;
                        }
                    }   
                    else{

                       
                        String documento = null;
                        String nombre = null;
                        String apellido1 = null;
                        String apellido2 = null;
                        TercerosValueObject interesadoExpediente = null;
                        boolean establecerDomicilioEnExpediente = true;
                        if(altaDomicilioInteresadoRolDefecto!=null && "SI".equalsIgnoreCase(altaDomicilioInteresadoRolDefecto)){
                            // Si el interesado es el interesado con rol principal en el expediente, entonces es que
                            // se pretende dar de alta su domicilio
                            interesadoExpediente = FichaExpedienteDAO.getInstance().getInteresadoRolPorDefecto(codOrganizacion, (String)gVO.getAtributo("numero"), con);                    
                            documento = interesadoExpediente.getDocumento();
                            tipoDoc   = Integer.parseInt(interesadoExpediente.getTipoDocumento());
                            nombre    = interesadoExpediente.getNombre();
                            apellido1 = interesadoExpediente.getApellido1();
                            apellido2 = interesadoExpediente.getApellido2();


                            String codRol = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_ROL)).trim(); 
                            String notificacionElectronica = "0";
                            if(gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOTIFICACIONELECTRONICA)!=null)
                                notificacionElectronica = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOTIFICACIONELECTRONICA)).trim();      

                            GeneralValueObject aux = new GeneralValueObject();                    
                            aux.setAtributo("notifElect",notificacionElectronica);
                            aux.setAtributo("codRol",codRol); 
                            aux.setAtributo("codMunicipio",(String)gVO.getAtributo("codMunicipio"));
                            aux.setAtributo("ejercicio",(String)gVO.getAtributo("ejercicio"));
                            aux.setAtributo("numero",(String)gVO.getAtributo("numero"));

                            if (interesadoExpediente != null){
                                int rowsUpdate = FichaExpedienteDAO.getInstance().actualizarInteresadoExpediente(aux, interesadoExpediente, con);
                                if (rowsUpdate == 0){
                                    return 5;
                                }
                            }else{
                                return 4;
                            }

                            establecerDomicilioEnExpediente = false;

                        }else{
                            documento = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NIF)).trim();
                            nombre = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOMBRE)).trim();
                            if(((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_APELLIDO1))!=null)
                                apellido1 = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_APELLIDO1)).trim();

                        }
                        
                        String rol = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_ROL)).trim(); 
                        String provincia = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_PROVINCIA));            
                        if (provincia != null) {
                         provincia = provincia.trim();    
                        }else{
                            provincia = m_ConfigCommon.getString(codOrganizacion + ConstantesDatos.CODIGO_PROVINCIA_DESCONOCIDO);
                        }

                        String municipio = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_MUNICIPIO));
                        if (municipio != null){
                            municipio = municipio.trim();
                        }else{
                            municipio = m_ConfigCommon.getString(codOrganizacion + ConstantesDatos.CODIGO_MUNICIPIO_DESCONOCIDO);
                        }
                        String nombreVia = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOMBREVIA);
                        if (nombreVia != null) {
                            nombreVia = nombreVia.trim();
                        }else{
                            nombreVia = m_ConfigCommon.getString(codOrganizacion + ConstantesDatos.DESCRIPCION_VIA_DESCONOCIDA);
                        }

                        String emplazamiento = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_EMPLAZAMIENTO);
                        if (emplazamiento != null) {emplazamiento = emplazamiento.trim();}
                        //String apellido2 = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_APELLIDO2);
                        apellido2 = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_APELLIDO2);
                        if (apellido2 != null) {apellido2 = apellido2.trim();}
                        String telefono = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_TELEFONO);
                        if (telefono != null) {telefono = telefono.trim();}
                        String email = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_EMAIL);
                        if (email != null) {email = email.trim();}
                        String tipoVia = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_TIPOVIA);
                        if (tipoVia != null) {tipoVia = tipoVia.trim();}            
                        String letraDesde = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_LETRADESDE);
                        if (letraDesde != null) {letraDesde = letraDesde.trim();}            
                        String letraHasta = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_LETRAHASTA);
                        if (letraHasta != null) {letraHasta = letraHasta.trim();}            
                        String bloque = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_BLOQUE);
                        if (bloque != null) {bloque = bloque.trim();}            
                        String portal = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_PORTAL);
                        if (portal != null) {portal = portal.trim();}            
                        String escalera = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_ESCALERA);
                        if (escalera != null) {escalera = escalera.trim();}            
                        String planta = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_PLANTA);
                        if (planta != null) {planta = planta.trim();}            
                        String puerta = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_PUERTA);
                        if (puerta != null) {puerta = puerta.trim();}            
                        String codigoPostal = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_CODIGOPOSTAL);
                        if (codigoPostal != null) {codigoPostal = codigoPostal.trim();}            
                        String numeroDesde = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NUMERODESDE);
                        if (numeroDesde != null) {numeroDesde = numeroDesde.trim();}            
                        String numeroHasta = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NUMEROHASTA);
                        if (numeroHasta != null) {numeroHasta = numeroHasta.trim();}            
                        String pais = ConstantesDatos.CODIGO_PAIS_ESPAÑA;
                        String notifElectronica = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOTIFICACIONELECTRONICA);
                        if (notifElectronica != null) {notifElectronica = notifElectronica.trim();}                       
                        //El campo codigo de via no se recupera del XML porque no se va utilizar al ser diferentes los codigos de vía que 
                        //manejar ellos con los que tenemos nosotros en Flexia

                        
                        /*** ORIGINAL
                        //Recupera el tipo de documento
                        if (Utilities.validaNIF(documento)){
                            tipoDoc = ConstantesDatos.TIPO_DOCUMENTO_NIF;
                        }else if (Utilities.validaCIF(documento)){
                            tipoDoc = ConstantesDatos.TIPO_DOCUMENTO_CIF;
                        }else if (Utilities.validarNie(documento)){
                            tipoDoc = ConstantesDatos.TIPO_DOCUMENTO_NIE;
                        }else
                        if(documento==null || "0".equals(documento) || "".equals(documento)){
                            documento = "";
                            tipoDoc = ConstantesDatos.TIPO_DOCUMENTO_SIN_DOCUMENTO;                            
                        }
                        **/
                        
                        
                        /************ PRUEBA *******************/
                        //Recupera el tipo de documento
                        if (Utilities.validaNIF(documento) && nombre!=null && apellido1!=null && apellido1.length()>0){
                            // Se trata de un tercero cuyo documento es un NIF, ya que al menos tiene el primer apellido
                            tipoDoc = ConstantesDatos.TIPO_DOCUMENTO_NIF;
                        }else 
                        if(Utilities.validaNIF(documento) && nombre!=null && (apellido1==null || apellido1.length()==0) && (apellido2==null || apellido2.length()==0)){
                            // Si el documento es un NIF, y sólo se ha enviado un nombre sin apellidos.
                            // Se entiende que se trata de un CIF, ya que en Flexia por configuración se permite el alta
                            // de cif que sean un NIF.
                            tipoDoc = ConstantesDatos.TIPO_DOCUMENTO_CIF;
                        }else
                        if (Utilities.validaCIF(documento)){
                            tipoDoc = ConstantesDatos.TIPO_DOCUMENTO_CIF;
                        }else if (Utilities.validarNie(documento)){
                            tipoDoc = ConstantesDatos.TIPO_DOCUMENTO_NIE;
                        }else                        
                        if(documento==null || "0".equals(documento) || "".equals(documento)){
                            documento = "";
                            tipoDoc = ConstantesDatos.TIPO_DOCUMENTO_SIN_DOCUMENTO;                            
                        }
                        
                        
                        
                        
                        //Rellena los campos para la busqueda de terceros. Se busca por los criterios del domicilio porque puede que exista el 
                        //mismo tercero con distintos domicilios.
                        //No se utiliza como criterios de búsqueda los codigos de vía, porque los valores que enviarían en el XML son diferentes a 
                        //los codigos que se manejan en Flexia.
                        CondicionesBusquedaTerceroVO co = new CondicionesBusquedaTerceroVO();
                        co.setDocumento(documento);
                        co.setTipoDocumento(tipoDoc);            
                        co.setNombre(nombre);
                        co.setApellido1(apellido1);
                        if (apellido2 != null && apellido2.length()>0) {co.setApellido2(apellido2);}

                        //Realiza la busqueda de terceros
                        listaTerceros  = TercerosDAO.getInstance().getTercero(co, params,con);

                        //El tercero existe
                        if ((listaTerceros != null) && (listaTerceros.size() > 0)){ 

                            m_Log.debug(" =============> El número de terceros recuperados es: " + listaTerceros.size());
                            //Si se diera el caso excepcional de que existan varios terceros con el mismo domicilio, 
                            //lo que hacemos es quedarnos con el tercero más reciente
                            terceroVO = new TercerosValueObject();
                            terceroVO.setIdentificador("-1");
                            for ( int j = 0; j < listaTerceros.size(); j++ ){
                                terceroVOAux = (TercerosValueObject) listaTerceros.get(j);
                                if (Integer.valueOf(terceroVOAux.getIdentificador()) > Integer.valueOf(terceroVO.getIdentificador())){
                                    terceroVO = terceroVOAux;
                                }
                            }

                            m_Log.debug(" =======================> terceroVO identificador : " + terceroVO.getIdentificador() + ", documento: " + terceroVO.getDocumento() + ", nombre: " + terceroVO.getNombre() + "apellido1: " + terceroVO.getApellido1());
                            if(terceroVO.getVersion()!=null && !"".equals(terceroVO.getVersion()))
                                versionTercero = Integer.parseInt(terceroVO.getVersion());

                            m_Log.debug(" ======> 2");

                            //Actualiza los campos email y telefono
                            if (email != null && email.length()>0){
                                if (terceroVO.getEmail() != null && !terceroVO.getEmail().equals(email)){
                                    actulizarTercero = true;
                                }
                            }

                            m_Log.debug(" ======> 3");

                            if (telefono != null && telefono.length()>0){
                                if (terceroVO.getTelefono()!= null && !terceroVO.getTelefono().equals(telefono)){
                                    actulizarTercero = true;
                                }
                            }
                            //Actualiza los datos de email y telefono de tecero si han sido modificados
                            m_Log.debug(" ======> 4");
                            m_Log.debug(" ============> VALOR ACTUALIZARTERCERO: " + actulizarTercero);
                            if (actulizarTercero){
                                m_Log.debug(" 5");
                                terceroVO.setEmail(email);
                                terceroVO.setTelefono(telefono);
                                idTercero = TercerosDAO.getInstance().updateTercero(terceroVO,con,params);
                                //if (idTercero==0){return 0;}
                                if (idTercero==0){ 
                                    resultado = 0;
                                }
                            }else{
                                m_Log.debug("  6===========> identificador: " + terceroVO.getIdentificador());
                                idTercero = Integer.valueOf(terceroVO.getIdentificador()).intValue();
                                m_Log.debug("  7===========> idTercero: " + idTercero);
                            }

                        }else{ //El tercero NO existe

                            m_Log.debug(" =============> NO EXISTE EL TERCERO " + nombre + "," + apellido1 + "," + apellido2);
                            //Da de alta el tercero
                            terceroVO = new TercerosValueObject();
                            terceroVO.setDocumento(documento);
                            terceroVO.setTipoDocumento(String.valueOf(tipoDoc));
                            terceroVO.setNombre(nombre);                        
                            terceroVO.setApellido1(apellido1);
                            terceroVO.setApellido2(apellido2);
                            terceroVO.setNormalizado("1");
                            terceroVO.setSituacion('A');
                            terceroVO.setOrigen("SGE");
                            terceroVO.setUsuarioAlta((String)gVO.getAtributo("usuario"));
                            terceroVO.setModuloAlta((String)gVO.getAtributo("codAplicacion"));                
                            terceroVO.setTelefono(telefono);
                            terceroVO.setEmail(email);
                            terceroVO.setNombreCompleto(concatenaNombreTercero(terceroVO));

                            idTercero = TercerosDAO.getInstance().setTercero(terceroVO, params,con);
                            versionTercero = 1;

                            //if (idTercero==0){return 0;}                

                        }

                         m_Log.debug(" *****************************>>  idTercero: " + idTercero + ",versionTercero: " + versionTercero);

                         if (idTercero==0){
                             resultado=0; // Error
                         }else{

                            //Da de alta el domicilio (T_DOM, T_DMM)
                            DomicilioSimpleValueObject domicilio = new DomicilioSimpleValueObject();
                            domicilio.setIdPais(pais);
                            domicilio.setIdProvincia(provincia);
                            domicilio.setIdMunicipio(municipio);
                            domicilio.setDescVia(nombreVia);

                            if (tipoVia != null && tipoVia.length()>0) {domicilio.setTipoVia(tipoVia);}
                            else
                            domicilio.setTipoVia(Integer.toString(es.altia.agora.technical.ConstantesDatos.TIPO_VIA_SINVIA));
                            if (emplazamiento != null && emplazamiento.length()>0) {domicilio.setDomicilio(emplazamiento);}
                            if (letraDesde != null && letraDesde.length()>0) {domicilio.setLetraDesde(letraDesde);}
                            if (letraHasta != null && letraHasta.length()>0) {domicilio.setLetraHasta(letraHasta);}
                            if (numeroDesde != null && numeroDesde.length()>0) {domicilio.setNumDesde(numeroDesde);}        
                            if (numeroHasta != null && numeroHasta.length()>0) {domicilio.setNumHasta(numeroHasta);}
                            if (bloque != null && bloque.length()>0) {domicilio.setBloque(bloque);}
                            if (portal != null && portal.length()>0) {domicilio.setPortal(portal);}
                            if (escalera != null && escalera.length()>0) {domicilio.setEscalera(escalera);}
                            if (planta != null && planta.length()>0) {domicilio.setPlanta(planta);}
                            if (puerta != null && puerta.length()>0) {domicilio.setPuerta(puerta);}
                            if (codigoPostal != null && codigoPostal.length()>0) {domicilio.setCodigoPostal(codigoPostal);}
                            domicilio.setEsDomPrincipal("true");


                            // Se busca el domicilio
                            DomiciliosDAO domiciliosDAO = DomiciliosDAO.getInstance();
                            int idDomicilio = domiciliosDAO.buscarDireccion(domicilio, con);
                            if(idDomicilio==-1){
                                m_Log.debug("=====================> FichaExpedienteManager.actualizarExpedienteAsiento: Se ha buscado el domicilio pero no existe");
                                // No se ha encontrado el domicilio, entonces se procede a dar de alta
                                idDomicilio = domiciliosDAO.altaDomicilio(domicilio, usuario, con);

                            }else{
                                m_Log.debug("=====================> FichaExpedienteManager.actualizarExpedienteAsiento: Se ha buscado el domicilio y existe");
                            }

                            m_Log.debug(" ************************* idDomicilio: " + idDomicilio);

                            gVO.setAtributo("codMunicipio", codOrganizacion);
                            gVO.setAtributo("codigoTercero",String.valueOf(idTercero));  
                            gVO.setAtributo("versionTercero",Integer.toString(versionTercero));
                            gVO.setAtributo("domicilio",String.valueOf(idDomicilio)); 
                            gVO.setAtributo("rol",rol); 
                            gVO.setAtributo("notifElectronica", notifElectronica);                

                            // Se da de alta el domicilio para el tercero
                            m_Log.debug("=====================> FichaExpedienteManager actualizarExpedienteAsiento: Antes de proceder a dar de alta el domicilio para el tercero");
                            boolean altaDomicilio = TercerosDAO.getInstance().altaDomicilioTercero(idTercero, idDomicilio,usuario, con);
                            m_Log.debug("=====================> FichaExpedienteManager actualizarExpedienteAsiento: Después de proceder a dar de alta el domicilio para el tercero: " + altaDomicilio);
                            if (altaDomicilio==false){
                                resultado = 0;
                            }                            


                            boolean interesadosInserted = true;
                            if(establecerDomicilioEnExpediente){

                                m_Log.debug("=====================> FichaExpedienteManager actualizarExpedienteAsiento antes de altaInteresado ");
                                interesadosInserted = InteresadosDAO.getInstance().altaInteresado(gVO, con);
                                m_Log.debug("=====================> FichaExpedienteManager actualizarExpedienteAsiento despues de altaInteresado: " + interesadosInserted);                        
                            }

                          
                            if (!interesadosInserted){
                                resultado=0;
                            }else{

                                m_Log.debug(" ************************* Se procede a dar de alta los datos suplementarios ***************");

                                //Inserta los datos suplementarios del interesado 
                                gVODatosSuplementarios = (Vector) gVOInteresado.getAtributo("datosSuplementarios");  

                                if (gVODatosSuplementarios != null && gVODatosSuplementarios.size()>0){
                                    try{                                    
                                        //Vector estructuraDatosSuplementariosAux = DatosSuplementariosTerceroManager.getInstance().cargaEstructuraDatosSuplementariosTercero(codOrganizacion,params);
                                        Vector estructuraDatosSuplementariosAux = DatosSuplementariosTerceroDAO.getInstance().cargaEstructuraDatosSuplementariosTercero(codOrganizacion, adapt, con);

                                        if (estructuraDatosSuplementariosAux != null){
                                            //Filtra de la estructuta de datos suplementarios los que viene en el documento                        
                                            for (int j = 0; j < estructuraDatosSuplementariosAux.size(); j++ ){
                                               eC = (EstructuraCampo) estructuraDatosSuplementariosAux.elementAt(j);
                                               for ( int k = 0; k < gVODatosSuplementarios.size(); k++ ){
                                                    generalVO = (GeneralValueObject) gVODatosSuplementarios.get(k); 
                                                    String codCampo = (String) generalVO.getAtributo(ConstantesDatos.TAG_XML_CODIGO);
                                                    String valorCampo = (String) generalVO.getAtributo(ConstantesDatos.TAG_XML_VALOR);
                                                    if (eC.getCodCampo().equalsIgnoreCase(codCampo)){
                                                        estructuraDatosSuplementarios.add(eC);
                                                        gVO.setAtributo(codCampo, valorCampo);
                                                        valoresDatosSuplementarios.add(gVO);
                                                    }                                
                                               }
                                            }

                                            //DatosSuplementariosTerceroManager.getInstance().grabarDatosSuplementarios(codOrganizacion,String.valueOf(idTercero),estructuraDatosSuplementarios,valoresDatosSuplementarios,params);
                                            DatosSuplementariosTerceroDAO.getInstance().grabarDatosSuplementarios(codOrganizacion,String.valueOf(idTercero),estructuraDatosSuplementarios,valoresDatosSuplementarios,adapt,con);

                                        }
                                        resultado = 1;

                                    }catch(TechnicalException e){
                                        e.printStackTrace();
                                        m_Log.error(e.getMessage());
                                        resultado = 0;
                                    }
                                }else resultado = 1;        
                            }
                         }
                    }
                }
            }else resultado=0;
      
        }// if(!errorPrimerPaso)
        
      }catch (Exception e) {
            m_Log.debug("=====================> ERROR: FichaExpedienteManager actualizarExpedienteAsiento: " + e.getMessage());
            resultado = 0;
            e.printStackTrace();
            
            try{
                adapt.rollBack(con);
                
            }catch(BDException f){
                m_Log.debug(" =====================> ERROR FichaExpedienteManager.actualizarExpedienteAsiento: Error al realizar rollback de la transaccion:  " + e.getMessage());
            }
      }finally{
          try{
              if(resultado==1) 
                  adapt.finTransaccion(con);
              else
                  adapt.rollBack(con);
              
          }catch(BDException f){
               m_Log.error(" =====================> ERROR FichaExpedienteManager.actualizarExpedienteAsiento: Error al realizar rollback de la transaccion:  " + f.getMessage());
          }
          
          
          try{
            adapt.devolverConexion(con);
                
          }catch(BDException e){
              e.printStackTrace();              
          }          
      }
          
      m_Log.debug(" =========================> FichaExpedienteManager.actualizar devolviendo: " + resultado);
      return resultado;
    }
       

    
   /**
     * Comprueba si un expediente esta un expediente activo, o bien, un expediente que está en el histórico
     * @param ejercicio: Ejercicio del expediente
     * @param numExpediente: Número del expedinete
     * @param con: Conexión a la BBDD
     * @return int que puede tomar los siguientes valores:
     *          0 --> Expediente activo
     *          1 --> Expediente historico
     *          2 --> Expediente no existe
     */
    public int estaExpedienteHistorico(int ejercicio,String numExpediente,String[] params) {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        int salida =-1;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            salida = FichaExpedienteDAO.getInstance().estaExpedienteHistorico(ejercicio, numExpediente, con);
            
        }catch(Exception e){
            e.printStackTrace();
        } finally{
            try{
                if(con!=null) con.close();                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return salida;
    
    }
    
    public boolean documentosTramitePendienteFirma(TramitacionExpedientesValueObject traVO, String[] params){
        m_Log.debug("documentosTramitePendienteFirma: BEGIN");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        boolean pendienteFirma = false;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            ArrayList<DocumentoTramitacionVO> listaDocsTramite = FichaExpedienteDAO.getInstance().getListaDocumentosTramite(traVO, con);
            
            for(DocumentoTramitacionVO document: listaDocsTramite){
                String firma = document.getEstadoFirma();
                if(firma.equals("T")|| firma.equals("O") || firma.equals("E")|| firma.equals("L")|| firma.equals("U")){
                    pendienteFirma = true;
                }
            
            }
        }catch(Exception e){
            e.printStackTrace();
        } finally{
            try{
                if(con!=null) con.close();                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        
        return pendienteFirma;
        
    }
    
    public boolean expProcNumeracionAnhoAnotacion(int codOrganizacion, String codProcedimiento, String[] paramsBD) throws BDException, SQLException{
        m_Log.info("FichaExpedienteManager.expProcNumeracionAnhoAnotacion()::BEGIN");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        boolean numeracionAnual = false;
        
        try{
            adapt = new AdaptadorSQLBD(paramsBD);
            con = adapt.getConnection();
            
            numeracionAnual = FichaExpedienteDAO.getInstance().expProcNumeracionAnhoAnotacion(codOrganizacion, codProcedimiento, con);
        } catch (BDException bdex){
            m_Log.error("Ha ocurrido un error al obtener una conexión a la base de datos.");
            bdex.printStackTrace();
            throw bdex;
        } finally {
            try {
                if(con!= null) con.close();
            } catch (SQLException ex){
                m_Log.error("Ha ocurrido un error al cerrar la conexión a la base de datos");
            }
        }
        
        m_Log.debug("FichaExpedienteManager.expProcNumeracionAnhoAnotacion()::END");
        return numeracionAnual;
    }
    
    public FicheroVO cargarTipoDocumentalFicherosRegistro(FicheroVO ficheroVO, String[] params){
        AdaptadorSQLBD oad = null;
        Connection con = null;
        
        if (m_Log.isDebugEnabled()){m_Log.debug("cargarTipoDocumentalFicherosRegistro");}
        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            
            ficheroVO = FichaExpedienteDAO.getInstance().cargarTipoDocumentalFicherosRegistro(ficheroVO,con);
        }catch (Exception e) {
            m_Log.error("cargarTipoDocumentalFicherosRegistro(): Error " + e.getMessage());
            e.printStackTrace();
        }finally{
            try {
                SigpGeneralOperations.devolverConexion(oad, con);
            } catch (TechnicalException ex) {
                ex.printStackTrace();
                m_Log.error("Error: " + ex.getMessage());
            }
        }
        
        return ficheroVO;
    }
    
	public String obtenerDocumentoTercero (String codTercero, String params[]) {
        String documento = "";
        if (m_Log.isDebugEnabled()) m_Log.debug("obtenerDocumentoTercero");

        try {
            documento = FichaExpedienteDAO.getInstance().obtenerDocumentoTercero(codTercero, params);
        } catch (Exception e) {
            m_Log.error("obtenerDocumentoTercero: Error " + e.getMessage());
            e.printStackTrace();
        }
        return documento;
    }
 }
