package es.altia.agora.business.sge.persistence;


import es.altia.agora.business.sge.CampoSuplementarioFicheroVO;
import java.sql.Connection;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.ValorCampoSuplementarioVO;
import es.altia.agora.business.sge.persistence.manual.DatosSuplementariosDAO;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.EstructuraCampoAgrupado;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatosSuplementariosManager {

  // Mi propia instancia usada en el metodo getInstance.
  private static DatosSuplementariosManager instance = null;

  // Para el fichero de configuracion technical.
  protected static Config m_ConfigTechnical;
  // Para el fichero de mensajes de error localizados.
  protected static Config m_ConfigError;
  protected static Log m_Log =
          LogFactory.getLog(DatosSuplementariosManager.class.getName());


  public DatosSuplementariosManager() {
    //Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }


  /**
  * Factory method para el <code>Singelton</code>.
  * @return La unica instancia de FichaExpedienteManager
  */
  public static DatosSuplementariosManager getInstance() {
    //Si no hay una instancia de esta clase tenemos que crear una.
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
      synchronized(DatosSuplementariosManager.class) {
        if (instance == null)
        instance = new DatosSuplementariosManager();
      }
    }
    return instance;
  }


  public int grabarDatosSuplementarios(Vector estructuraDatosSuplementarios,Vector valoresDatosSuplementarios, String[] params) {

    m_Log.debug("grabarDatosSuplementarios");
    int res = 0;
    AdaptadorSQLBD oad = null;
    Connection con = null; 
    Vector docs = new Vector();   
    AlmacenDocumento almacen = null;    

    try{
       oad = new AdaptadorSQLBD(params);
       con = oad.getConnection();
       oad.inicioTransaccion(con);
        
      //Guarda todos los datos suplementarios menos los de tipo fichero 
      res = DatosSuplementariosDAO.getInstance().grabarDatosSuplementariosConsultas(estructuraDatosSuplementarios,valoresDatosSuplementarios, params, oad, con);
            
      m_Log.debug("grabarDatosSuplementarios - Datos suplementarios insertados correctamente");
      
    } catch (Exception e) {
        res = 0;            
        if (m_Log.isErrorEnabled()) {m_Log.error("Excepcion capturada en: " + getClass().getName());}        
        e.printStackTrace();
    } finally {
        try {
            if (res == 0){
                oad.rollBack(con);
            }else            
                oad.finTransaccion(con);
            
            oad.devolverConexion(con);
        } catch (BDException bde) {            
            bde.printStackTrace();
            
        }
        return res;
    }    
  }
  
  public int grabarDatosSuplementariosFichero(Vector estructuraDatosSuplementarios,Vector valoresDatosSuplementarios, String[] params) {

    m_Log.debug("grabarDatosSuplementarios");
    int res = 0;
    AdaptadorSQLBD oad = null;
    Connection con = null; 
    Vector docs = new Vector();   
    AlmacenDocumento almacen = null;    

    try{
       oad = new AdaptadorSQLBD(params);
       con = oad.getConnection();
       oad.inicioTransaccion(con);
        
      //Guarda todos los datos suplementarios menos los de tipo fichero 
      res = DatosSuplementariosDAO.getInstance().grabarDatosSuplementariosConsultasFichero(estructuraDatosSuplementarios,valoresDatosSuplementarios, params, oad, con);
            
      m_Log.debug("grabarDatosSuplementarios - Datos suplementarios insertados correctamente");
      
    } catch (Exception e) {
        res = 0;            
        if (m_Log.isErrorEnabled()) {m_Log.error("Excepcion capturada en: " + getClass().getName());}        
        e.printStackTrace();
    } finally {
        try {
            if (res == 0){
                oad.rollBack(con);
            }else            
                oad.finTransaccion(con);
            
            oad.devolverConexion(con);
        } catch (BDException bde) {            
            bde.printStackTrace();
            
        }
        return res;
    }    
  }

  public int grabarDatosSuplementariosConsultas(Vector estructuraDatosSuplementarios,Vector valoresDatosSuplementarios, String[] params, AdaptadorSQLBD oad, Connection con) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("setDatoNumerico");
    int res = 0;

    try{
      res = DatosSuplementariosDAO.getInstance().grabarDatosSuplementariosConsultas(estructuraDatosSuplementarios,valoresDatosSuplementarios,params,oad, con);
    }catch (TechnicalException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      return res;
    }
  }

    public int grabarDatosSuplementariosTramite(Vector estructuraDatosSuplementarios, GeneralValueObject valoresDatosSuplementarios, String[] params, TramitacionExpedientesValueObject tEVO) {

        m_Log.debug("grabarDatosSuplementariosTramite");
        int res = 0;
        AdaptadorSQLBD oad = null;
        Connection con = null; 
        Vector docs = new Vector();   
        AlmacenDocumento almacen = null;    

        try{
           oad = new AdaptadorSQLBD(params);
           con = oad.getConnection();
           oad.inicioTransaccion(con);

          //Guarda todos los datos suplementarios menos los de tipo fichero 
          res = DatosSuplementariosDAO.getInstance().grabarDatosSuplementariosTramite(estructuraDatosSuplementarios, valoresDatosSuplementarios, params, tEVO, oad, con);
          
          m_Log.debug("grabarDatosSuplementariosTramite - Datos suplementarios insertados correctamente");

        } catch (Exception e) {
            res = 0;
            if (m_Log.isErrorEnabled()) {m_Log.error("Excepcion capturada en: " + getClass().getName());}        
            e.printStackTrace();
        } finally {
            try {
                if (res == 0){oad.rollBack(con);}                   
                oad.finTransaccion(con);
                oad.devolverConexion(con);
            } catch (BDException bde) {
                bde.getMensaje();
            }
            return res;
        }    
    } 
    
     public int grabarDatosSuplementariosFicheroTramite(Vector estructuraDatosSuplementarios, Vector valoresDatosSuplementarios, String[] params, TramitacionExpedientesValueObject tEVO) {
 
        m_Log.debug("grabarDatosSuplementariosFicheroTramite");
        int res = 0;
        AdaptadorSQLBD oad = null;
        Connection con = null; 
        
        try{
           oad = new AdaptadorSQLBD(params);
           con = oad.getConnection();
           oad.inicioTransaccion(con);

          //Guarda todos los datos suplementarios menos los de tipo fichero 
          res = DatosSuplementariosDAO.getInstance().grabarDatosSuplementariosFicheroTramite(estructuraDatosSuplementarios, valoresDatosSuplementarios, params, tEVO, oad, con);
          
          m_Log.debug("grabarDatosSuplementariosFicheroTramite - Datos suplementarios tipo fichero insertados correctamente");

        } catch (Exception e) {
            res = 0;
            if (m_Log.isErrorEnabled()) {m_Log.error("Excepcion capturada en: " + getClass().getName());}        
            e.printStackTrace();
        } finally {
            try {
                if (res == 0){oad.rollBack(con);}                   
                oad.finTransaccion(con);
                oad.devolverConexion(con);
            } catch (BDException bde) {
                bde.getMensaje();
            }
            return res;
        }    
    }
    

public int grabarDatosSuplementariosTramite2(Vector estructuraDatosSuplementarios, GeneralValueObject valoresDatosSuplementarios, Connection con, TramitacionExpedientesValueObject tEVO, AdaptadorSQLBD oad, String[] params) {

        m_Log.debug("grabarDatosSuplementariosTramite2");
        int res = 0;
        Vector docs = new Vector();   
        AlmacenDocumento almacen = null;    
        boolean exito = false;

        try{

          //Guarda todos los datos suplementarios menos los de tipo fichero 
          res = DatosSuplementariosDAO.getInstance().grabarDatosSuplementariosTramite2(estructuraDatosSuplementarios, valoresDatosSuplementarios, con, tEVO, oad);

          /**
          //Se define un plugin para insertar documentos de tipo de datos suplementario fichero
          if (res == 1){

              for (int i = 0; i < valoresDatosSuplementarios.size(); i++) {
                    EstructuraCampo eC = new EstructuraCampo();
                    eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                    String codTipoDato = eC.getCodTipoDato();

                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO = (GeneralValueObject) valoresDatosSuplementarios.elementAt(i);

                    //Obtiene la implementacion del plugin correspondiente
                    almacen = AlmacenDocumentoTramitacionFactoria.getInstance().getImplClass((String) gVO.getAtributo("codOrganizacion")); 

                    if ("5".equals(codTipoDato) && almacen != null) {

                            byte[] valorDato = null;
                            String mime = "";
                            String nombre = "";

                            String codMunicipio = tEVO.getCodMunicipio();
                            String codProcedimiento = tEVO.getCodProcedimiento();
                            String ejercicio = tEVO.getEjercicio();
                            String numeroExpediente = tEVO.getNumeroExpediente();
                            String codTramite = tEVO.getCodTramite();
                            String ocurrencia = tEVO.getOcurrenciaTramite();                            
                            String codDato = eC.getCodCampo();
                            
                            Hashtable<String,Object> datos = new Hashtable<String,Object>();  
                            if (!gVO.getAtributo(codDato).equals("")) {
                                valorDato = (byte[]) gVO.getAtributo(eC.getCodCampo());
                                nombre = (String) gVO.getAtributo(eC.getCodCampo() + "_NOMBRE");
                                mime = (String) gVO.getAtributo(eC.getCodCampo() + "_TIPO");
                                datos.put("nombreDocumento",nombre);                            
                                datos.put("tipoMime",mime);
                                datos.put("fichero",valorDato);
                            }
  
                            datos.put("codMunicipio",codMunicipio);
                            datos.put("codProcedimiento",codProcedimiento);
                            datos.put("ejercicio",ejercicio);
                            datos.put("numeroExpediente",numeroExpediente);
                            datos.put ("codTramite",codTramite);
                            datos.put("ocurrenciaTramite",ocurrencia);
                            datos.put("codTipoDato",codDato);
                            String[] datosFichero = nombre.split("[.]");
                            datos.put("extension",datosFichero[datosFichero.length-1]); 

                            int tipoDocumento = -1;
                            if(!almacen.isPluginGestor()){
                                //Plugin BBDDD
                                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                            }else{
                                 //Plugin Gestor Alfresco. Solo inserta los docuemntos en la BD de Flexia
                                 tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                            } 

                            Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);

                            docs.add(doc);
                    }
                }

                if (almacen != null && docs != null && docs.size()>0){
                    exito = almacen.setDocumentosDatosSuplementariosTramite(con, docs, params);
                    if (!exito){res = 0;}
                }

          }
          ***/
          
          m_Log.debug("grabarDatosSuplementariosTramite2 - Datos suplementarios insertados correctamente");
        
        } catch (Exception e) {
            res = 0;
            if (m_Log.isErrorEnabled()) {m_Log.error("Excepcion capturada en: " + getClass().getName());}        
            e.printStackTrace();
        } finally {
            return res;
        }    
    }
    
    
    
     public int grabarDocumentosDatosSuplementarios(Connection con, Vector docs, String[] params) {

        m_Log.debug("grabarDocumentosDatosSuplementarios");
        int res = 0;

        try{
          res = DatosSuplementariosDAO.getInstance().grabarDocumentosDatosSuplementarios(con, docs, params);
        }catch (TechnicalException te) {
          m_Log.error("JDBC Technical problem " + te.getMessage());
        }catch (Exception e) {
          e.printStackTrace();
        }finally{
          return res;
        }
      }
     
     /**
      public int grabarDocumentosDatosSuplementariosTramite(Connection con, Vector docs, String[] params) {

        m_Log.debug("grabarDocumentosDatosSuplementariosTramite");
        int res = 0;

        try{
          res = DatosSuplementariosDAO.getInstance().grabarDocumentosDatosSuplementariosTramite(con, docs, params);
        }catch (TechnicalException te) {
          m_Log.error("JDBC Technical problem " + te.getMessage());
        }catch (Exception e) {
          e.printStackTrace();
        }finally{
          return res;
        }
      }
      */
    
    public byte[] getFichero(String codigo,String municipio,String ejercicio,String numero, String[] params){
      DatosSuplementariosDAO datosDAO = DatosSuplementariosDAO.getInstance();
      byte[] resultado = null;
      try{
        resultado = datosDAO.getFichero(codigo, municipio, ejercicio, numero, params);
      }catch(Exception e){
      }
      return resultado;
    }

    public String getTipoFichero(String codigo,String municipio,String ejercicio,String numero, String[] params){
      DatosSuplementariosDAO datosDAO = DatosSuplementariosDAO.getInstance();
      String resultado = null;
      try{
        resultado = datosDAO.getTipoFichero(codigo, municipio, ejercicio, numero, params);
      }catch(Exception e){
      }
      return resultado;
    }
    
    
    /**
     * Permite recuperar un determinado campo suplementario de tipo fichero definido a nivel de expediente
     * @param codigo: Código del campo suplementario
     * @param codMunicipio: Código de la organización/Municipio
     * @param ejercicio: Ejercicio
     * @param numero: Número del expediente
     * @param connection: Conexión a la BBDD
     * @return
     * @throws TechnicalException 
     */    
    public CampoSuplementarioFicheroVO getCampoSuplementarioFicheroExpediente(String codigo, String codMunicipio, 
             String ejercicio, String numero, boolean expHistorico, String[] params) throws TechnicalException {
         AdaptadorSQLBD adapt = null;
         Connection con = null;
         CampoSuplementarioFicheroVO campo = null;
         
         try{
             adapt = new AdaptadorSQLBD(params);
             con = adapt.getConnection();
             
              campo = DatosSuplementariosDAO.getInstance().getCampoSuplementarioFicheroExpediente(codigo, 
                     codMunicipio, ejercicio, numero, expHistorico, con);
             
         }catch(BDException e){
             m_Log.error("Error al recuperar conexion a la BBDD: " + e.getMessage());
         }finally{
             
             try{
                 if(con!=null) con.close();
             }catch(Exception e){
                 m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
             }
         }
         
         return campo;
     }
    
     
   /**
     * Permite recuperar un determinado campo suplementario de tipo fichero definido a nivel de tramite
     * @param codigo: Código del campo suplementario
     * @param codMunicipio: Código de la organización/Municipio
     * @param ejercicio: Ejercicio
     * @param numero: Número del expediente
     * @param connection: Conexión a la BBDD
     * @return
     * @throws TechnicalException 
     */    
      public CampoSuplementarioFicheroVO getCampoSuplementarioFicheroTramite(String codigo, 
             String codMunicipio, String ejercicio, String numero,int codTramite, int ocurrenciaTramite,
             boolean expHistorico, String[] params) throws TechnicalException {
         AdaptadorSQLBD adapt = null;
         Connection con = null;
         CampoSuplementarioFicheroVO campo = null;
         
         try{
             adapt = new AdaptadorSQLBD(params);
             con = adapt.getConnection();
              campo = DatosSuplementariosDAO.getInstance().getCampoSuplementarioFicheroTramite(codigo, 
                     codMunicipio, ejercicio, numero, codTramite,ocurrenciaTramite, expHistorico, con);
             
         }catch(BDException e){
             m_Log.error("Error al recuperar conexion a la BBDD: " + e.getMessage());
         }finally{             
             try{
                 if(con!=null) con.close();
             }catch(Exception e){
                 m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
             }
         }         
         return campo;
     }
     
     
     /**
     * Permite eliminar un documento de un determinado campo suplementario de tipo fichero definido a nivel de expediente
     * @param doc: Documento
     * @return boolean: Indicando si la eliminación se realizó correctamente
     * @throws TechnicalException 
     */    
     public boolean eliminarDocumentoDatosSuplementarios(Documento doc) throws TechnicalException {
         
         AdaptadorSQLBD adapt = null;
         Connection con = null;
         boolean exito = false;
         
         try{
             adapt = new AdaptadorSQLBD(doc.getParams());
             con = adapt.getConnection();
             
             exito = DatosSuplementariosDAO.getInstance().eliminarDocumentoDatosSuplementarios(doc, con);
             
         }catch(BDException e){
             if (m_Log.isErrorEnabled()) {m_Log.error("Error al recuperar conexion a la BBDD: " + e.getMessage());}
             throw new TechnicalException(e.getMensaje());
         }finally{             
             try{
                 if(con!=null) {con.close();}
             }catch(Exception e){
                 if (m_Log.isErrorEnabled()) {m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());}
                 throw new TechnicalException(e.getMessage());                 
             }
         }         
         return exito;
     }
     
     
          /**
     * Permite eliminar un documento de un determinado campo suplementario de tipo fichero definido a nivel de tramite
     * @param doc: Documento
     * @return boolean: Indicando si la eliminación se realizó correctamente
     * @throws TechnicalException 
     */    
     public boolean eliminarDocumentoDatosSuplementariosTramite(Documento doc) throws TechnicalException {
         
         AdaptadorSQLBD adapt = null;
         Connection con = null;
         boolean exito = false;
         
         try{
             adapt = new AdaptadorSQLBD(doc.getParams());
             con = adapt.getConnection();
             
             exito = DatosSuplementariosDAO.getInstance().eliminarDocumentoDatosSuplementariosTramite(doc, con);
             
         }catch(BDException e){
             if (m_Log.isErrorEnabled()) {m_Log.error("Error al recuperar conexion a la BBDD: " + e.getMessage());}
             throw new TechnicalException(e.getMensaje());
         }finally{             
             try{
                 if(con!=null) {con.close();}
             }catch(Exception e){
                 if (m_Log.isErrorEnabled()) {m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());}
                 throw new TechnicalException(e.getMessage());                 
             }
         }         
         return exito;
     }
     
     

     public GeneralValueObject getInfoCampoSuplementarioFicheroExpediente(int codOrganizacion,
             String codCampo,String numExpediente,boolean expHistorico,String[] params){
          
         Connection con = null;
         AdaptadorSQLBD adapt = null;
         GeneralValueObject gVO = null;
         
         try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            gVO = DatosSuplementariosDAO.getInstance().getInfoCampoSuplementarioFicheroExpediente(codOrganizacion,
                    codCampo,numExpediente,expHistorico,con);
            
         }catch(BDException e){
            m_Log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
             
         }finally{
             try{
                adapt.devolverConexion(con);
             }catch(BDException e){
                 
             }
         }         
         return gVO;
    }
     
     
     
        
      public GeneralValueObject getInfoCampoSuplementarioFicheroTramite(int codOrganizacion,int codTramite,
              int ocurrenciaTramite,String codCampo,String numExpediente,boolean expHistorico,String[] params){
           
         Connection con = null;
         AdaptadorSQLBD adapt = null;
         GeneralValueObject gVO = null;
         
         try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            gVO = DatosSuplementariosDAO.getInstance().getInfoCampoSuplementarioFicheroTramite(codOrganizacion,
                    codTramite,ocurrenciaTramite,codCampo,numExpediente,expHistorico,con);
            
         }catch(BDException e){
            m_Log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
             
         }finally{
             try{
                adapt.devolverConexion(con);
             }catch(BDException e){
                 
             }
         }         
         return gVO;
     }
     


   /********************** NUEVO ***********************/ 
      
   public int grabarDatosSuplementarios(Vector<EstructuraCampoAgrupado> estructuraDatosSuplementarios,GeneralValueObject valoresDatosSuplementarios, String[] params) {

        m_Log.debug("grabarDatosSuplementarios");
        int res = 0;
        AdaptadorSQLBD oad = null;
        Connection con = null;                 

        try{
           oad = new AdaptadorSQLBD(params);
           con = oad.getConnection();
           oad.inicioTransaccion(con);

          //Guarda todos los datos suplementarios menos los de tipo fichero 
          res = DatosSuplementariosDAO.getInstance().grabarDatosSuplementariosConsultas(estructuraDatosSuplementarios,valoresDatosSuplementarios, params, oad, con);

          m_Log.debug("grabarDatosSuplementarios - Datos suplementarios insertados correctamente");

        } catch (Exception e) {
            res = 0;            
            if (m_Log.isErrorEnabled()) {m_Log.error("Excepcion capturada en: " + getClass().getName());}        
            e.printStackTrace();
        } finally {
            try {
                if (res == 0){
                    oad.rollBack(con);
                }else            
                    oad.finTransaccion(con);

                oad.devolverConexion(con);
            } catch (BDException bde) {            
                bde.printStackTrace();

            }
            return res;
        }    
    }
   
   
    public int grabarDatosSuplementariosFichero(ArrayList<EstructuraCampoAgrupado> estructuraDatosSuplementarios,ArrayList<GeneralValueObject> valoresDatosSuplementarios, String[] params) {

        m_Log.debug("grabarDatosSuplementarios");
        int res = 0;
        AdaptadorSQLBD oad = null;
        Connection con = null;                 

        try{
           oad = new AdaptadorSQLBD(params);
           con = oad.getConnection();
           oad.inicioTransaccion(con);

          //Guarda todos los datos suplementarios menos los de tipo fichero 
          res = DatosSuplementariosDAO.getInstance().grabarDatosSuplementariosConsultasFichero(estructuraDatosSuplementarios,valoresDatosSuplementarios, params, oad, con);

          m_Log.debug("grabarDatosSuplementarios - Datos suplementarios insertados correctamente");

        } catch (Exception e) {
            res = 0;            
            if (m_Log.isErrorEnabled()) {m_Log.error("Excepcion capturada en: " + getClass().getName());}        
            e.printStackTrace();
        } finally {
            try {
                if (res == 0){
                    oad.rollBack(con);
                }else            
                    oad.finTransaccion(con);

                oad.devolverConexion(con);
            } catch (BDException bde) {            
                bde.printStackTrace();

            }
            return res;
        }    
    }

      
/**
     * Recupera los valores de los campos suplementarios que tiene que actualizar en la ficha del 
     * expediente, tras grabar el mismo. Concretamente son los valores de los campos de tipo numérico
     * y los de tipo fecha co
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param params: Parámetros de conexión
     * @return ArrayList<ValorSuplementarioVO>
     */
    public ArrayList<ValorCampoSuplementarioVO> getCamposExpedienteActualizarEnFicha(int codOrganizacion,String numExpediente,String[] params){
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        ArrayList<ValorCampoSuplementarioVO> salida = new ArrayList<ValorCampoSuplementarioVO>();
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con   = adapt.getConnection();
            
            salida = DatosSuplementariosDAO.getInstance().getCamposExpedienteActualizarEnFicha(codOrganizacion, numExpediente, con);
            
        }catch(BDException e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return salida;
    }
      
      
}

