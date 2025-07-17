package es.altia.flexia.notificacion.persistence;

// PAQUETES	IMPORTADOS

import es.altia.common.exception.TechnicalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.util.ArrayList;

import es.altia.flexia.notificacion.vo.*;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;

public class AdjuntoNotificacionManager {

    private static AdjuntoNotificacionManager instance =	null;
    protected static Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log =
            LogFactory.getLog(NotificacionManager.class.getName());


    protected AdjuntoNotificacionManager() {
		// Queremos usar el	fichero de configuración technical
		m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
		// Queremos tener acceso a los mensajes de error localizados
		m_ConfigError	= ConfigServiceHelper.getConfig("error");
	}

    public static AdjuntoNotificacionManager getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (AdjuntoNotificacionManager.class) {
            if (instance == null) {
                instance = new AdjuntoNotificacionManager();
            }

        }
        return instance;
    }




    //Recupera los documentos adjuntos asociados a una determinada notificación de la tabla ADJUNTO_NOTIFICACION.
    public ArrayList<AdjuntoNotificacionVO> getAdjuntos(int codigoNotificacion,String[] params)
    {
        //queremos estar informados de cuando este metodo es ejecutado
	m_Log.debug("getAdjuntos");

	ArrayList<AdjuntoNotificacionVO> res = new ArrayList<AdjuntoNotificacionVO>();

	try{
            res = AdjuntoNotificacionDAO.getInstance().getAdjuntos(codigoNotificacion, params);

	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}
    }


     //Elimina un determinado adjunto de una notificación de la tabla ADJUNTO_NOTIFICACION.
    public boolean eliminarAdjunto(AdjuntoNotificacionVO adjuntoNotificacionVO,String[] params)
    {
        //queremos estar informados de cuando este metodo es ejecutado
	m_Log.debug("eliminarAdjunto");

	boolean res=false;

	try{
            res = AdjuntoNotificacionDAO.getInstance().eliminarAdjunto(adjuntoNotificacionVO, params);

	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}
    }



    //Comprueba si para un determinado trámite de un expediente en concreto, existen documentos de tramitación que estén firmados
    public boolean existeDocumentosFirmados(String numExpediente,int codTramite, int ocurrenciaTramite,String[] params)
    {
        //queremos estar informados de cuando este metodo es ejecutado
	m_Log.debug("existeDocumentosFirmados");

	boolean res=false;

	try{
            res = AdjuntoNotificacionDAO.getInstance().existeDocumentosFirmados(numExpediente,codTramite,ocurrenciaTramite, params);

	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}
    }
    
    
     public boolean existenDocumentosPendientesFirma(String numExpediente,int codTramite, int ocurrenciaTramite,String[] params)
    {
        //queremos estar informados de cuando este metodo es ejecutado
	m_Log.debug("existeDocumentosFirmados");

	boolean res=false;

	try{
            res = AdjuntoNotificacionDAO.getInstance().existenDocumentosPendientesFirma(numExpediente,codTramite,ocurrenciaTramite, params);

	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}
    }
    
    
    
    public ArrayList<AdjuntoNotificacionVO> getListaAdjuntosExternosNotificacionPortafirmas(int codUsuario,String estado,String[] params) throws TechnicalException {
        ArrayList<AdjuntoNotificacionVO> adjuntos = null;
        Connection con = null;        
        AdaptadorSQLBD adapt = null;
        
        try{
            
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();            
            adjuntos = AdjuntoNotificacionDAO.getInstance().getListaAdjuntosExternosNotificacionPortafirmas(codUsuario,estado,con);
            
            
        }catch(Exception e){
            m_Log.error("AdjuntoNotificacionManager.getListaAdjuntosExternosNotificacionPortafirmas error: " + e.getMessage());
            e.printStackTrace();
        } finally{
            try{
                if(con!=null) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return adjuntos;
        
    }
    

    
    /**
     * Reupera un determinado adjunto externo asociado a una notificación electrónca
     * @param codAdjunto: Código del adjunto externo
     * @param params: Parámetros de conexión a la BBDD
     * @return AdjuntoNotificacionVO o null sino se ha podido recuperar
     */
    public AdjuntoNotificacionVO getAdjuntoExternoNotificacion(int codAdjunto,String[] params){
        
        Connection con = null;
        AdjuntoNotificacionVO adjunto = null;
        
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            adjunto = AdjuntoNotificacionDAO.getInstance().getAdjuntoExternoNotificacion(codAdjunto, con);
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();                
                
            }catch(SQLException e){
                m_Log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }        
        return adjunto;
    }
    
    
    
    /**
   * Almacena la firma de un documento externo asociado a una notificación electrónca
   * @param Adjunto: Objeto de tipo AdjuntoNotificacionVO
   * @param params: Parámetros de conexión a la BBDD
   * @return boolean   
   */  
    public boolean setFirmaAdjuntoExternoNotificacion(AdjuntoNotificacionVO adjunto,String[] params){
        Connection con = null;
        boolean exito = false;
        try{
             AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
             con = adapt.getConnection();
             
             exito = AdjuntoNotificacionDAO.getInstance().setFirmaAdjuntoExternoNotificacion(adjunto,con);
        }catch(Exception e){
            exito = false;
            m_Log.error("Error al almacenar firma del adjunto externo a una notificación en BBDD: " + e.getMessage());
        }finally{
            try{
                if(con!=null) con.close();                
                
            }catch(SQLException e){
                m_Log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }        
        return exito;        
    }
    
    
  /**
   * Recupera una determinado documento externo asociado a una notificación electrónica
   * @param codDocumento: Código del documento
   * @param con: Conexión a la BBDD
   * @return AdjuntoNotificacionVO o null sino se ha podido recuperar   
   */  
  public AdjuntoNotificacionVO getDocumentoExternoNotificacion(int codDocumento,String[] params){
      Connection con = null;
      AdjuntoNotificacionVO adjunto = null;
      try{
          AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
          con = adapt.getConnection();
          
          adjunto = AdjuntoNotificacionDAO.getInstance().getDocumentoExternoNotificacion(codDocumento, con);
          
      }catch(Exception e){
          m_Log.error("Error al recuperar el contenido del documento externo asociado a una notificación electrónica: " + e.getMessage());
          e.printStackTrace();
      }finally{
          try{
              if(con!=null) con.close();
              
          }catch(Exception e){
              e.printStackTrace();
          }
      }      
      return adjunto;
  }
    
  
  
/**
   * Modifica el estado de la firma de un documento externo asociado a una notificación
   * @param codAdjunto: Código del adjunto
   * @param codUsuario: Código del usuario que realiza el cambio de estado
   * @param observaciones: Observaciones si las hay. Se tienen en cuanta a la hora de rechazar
   * @param params: Parámetros de conexión a la BBDD
   * @return boolean   
   */  
  public boolean actualizarEstadoFirmaAdjuntoExternoNotificacion(int codAdjunto,int codUsuario, String estado,String observaciones,String[] params){
      Connection con = null;
      boolean exito = false;
      
      try{
          AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
          con = adapt.getConnection();          
          exito = AdjuntoNotificacionDAO.getInstance().actualizarEstadoFirmaAdjuntoExternoNotificacion(codAdjunto,codUsuario,estado,observaciones,con);
          
      }catch(Exception e){
          m_Log.error("Error al recuperar al actualizar el estado de firma de un documento externo asociado a una notificación electrónica: " + e.getMessage());
          e.printStackTrace();
          
      }finally{
          try{
              if(con!=null) con.close();
              
          }catch(Exception e){
              e.printStackTrace();
          }
      }      
      return exito;
  }
 
  
  
  
  
  
  
  public boolean insertarAdjuntoExterno(AdjuntoNotificacionVO adjuntoNotificacionVO,String TIPO_GESTOR,String[] params) throws TechnicalException {
      Connection con = null;
      AdaptadorSQLBD adapt = null;
      boolean salida = false;
      try{
          adapt = new AdaptadorSQLBD(params);
          con = adapt.getConnection();                  
          
          salida = AdjuntoNotificacionDAO.getInstance().insertarAdjuntoExterno(adjuntoNotificacionVO,TIPO_GESTOR, con);
          if(salida) adapt.finTransaccion(con);
          else
              adapt.rollBack(con);
          
      }catch(Exception e){
          e.printStackTrace();
          try{
            adapt.rollBack(con);
          }catch(Exception f){
              m_Log.error("Error al realizar el rollback " + f.getMessage());
          }
          salida = false;
      }finally{
          try{
              adapt.devolverConexion(con);
          }catch(Exception e){
              e.printStackTrace();
          }
      }      
      return salida;      
  }
 
  
  
  public ArrayList<AdjuntoNotificacionVO> getListaAdjuntosExterno(int codNotificacion,String[] params) throws TechnicalException {
      Connection con = null;
      AdaptadorSQLBD adapt = null;
      ArrayList<AdjuntoNotificacionVO> salida = null;
      try{
          adapt = new AdaptadorSQLBD(params);
          con = adapt.getConnection();      
          adapt.inicioTransaccion(con);
          salida = AdjuntoNotificacionDAO.getInstance().getListaAdjuntosExterno(codNotificacion, con);
          
          
      }catch(Exception e){
          e.printStackTrace();
      }finally{
          try{
              adapt.devolverConexion(con);
          }catch(Exception e){
              e.printStackTrace();
          }
      }      
      return salida;      
      
  }
  
  
   public boolean eliminarAdjuntoExterno(int codAdjunto,String[] params) throws TechnicalException{
      
      Connection con = null;
      AdaptadorSQLBD adapt = null;
      boolean exito = false;
      
      try{
          adapt = new AdaptadorSQLBD(params);
          con = adapt.getConnection();        
          adapt.inicioTransaccion(con);
          exito = AdjuntoNotificacionDAO.getInstance().eliminarAdjuntoExterno(codAdjunto, con);
          if(exito) adapt.finTransaccion(con);
          else adapt.rollBack(con);
          
      }catch(Exception e){
          e.printStackTrace();
          exito = false;
          try{
            adapt.rollBack(con);
          }catch(Exception f){
              m_Log.error("Error al realizar el rollback " + f.getMessage());
          }
      }finally{
          try{
              adapt.devolverConexion(con);
          }catch(Exception e){
              e.printStackTrace();
          }
      }      
      return exito;                  
   }
   
   
   public boolean guardarFirma(AdjuntoNotificacionVO adjunto,String[] params) throws TechnicalException {
      Connection con = null;
      AdaptadorSQLBD adapt = null;
      boolean exito = false;
      
      try{
          adapt = new AdaptadorSQLBD(params);
          con = adapt.getConnection();                  
          adapt.inicioTransaccion(con);
          exito = AdjuntoNotificacionDAO.getInstance().guardarFirma(adjunto, con);
          
          if(exito) adapt.finTransaccion(con);
          else adapt.rollBack(con);
              
      }catch(Exception e){
          e.printStackTrace();
          try{
            adapt.rollBack(con);
          }catch(Exception f){
              m_Log.error("Error al realizar el rollback " + f.getMessage());
          }
          exito = false;
      }finally{
          try{
              adapt.devolverConexion(con);
          }catch(Exception e){
              e.printStackTrace();
          }
      }      
      return exito;     
   }
 
   
   public AdjuntoNotificacionVO getInfoDocumentoExternoNotificacion(int codDocumento,String[] params){
       AdaptadorSQLBD adapt = null;
       Connection con = null;
       AdjuntoNotificacionVO adjunto = null;
       
       try{
           adapt = new AdaptadorSQLBD(params);
           con = adapt.getConnection();
           
           adjunto = AdjuntoNotificacionDAO.getInstance().getInfoDocumentoExternoNotificacion(codDocumento,con);
           
       }catch(Exception e){
           e.printStackTrace();
           
       }finally{
           try{
                if(con!=null) con.close();
           }catch(SQLException e){
               e.printStackTrace();
           }
       }       
       return adjunto;
   }
   
   
   /**
    * Recupera los datos de un adjunto, como el código de trámite, expediente, ocurrencia de trámite, etc...
    * @param codAdjunto: Código del adjunto   
    * @param con: Conexión a la BBDD
    * @return Objeto de la clase AdjuntoNotificacionVO  
    */  
    public AdjuntoNotificacionVO getAdjuntoById(int codAdjunto,boolean expedienteHistorico,String[] params){
        AdjuntoNotificacionVO adjunto = null;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            adjunto = AdjuntoNotificacionDAO.getInstance().getAdjuntoById(codAdjunto,expedienteHistorico,con);
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                adapt.devolverConexion(con);
                
            }catch(BDException e){
                e.printStackTrace();
            }
        }
        
        return adjunto;
    }
   
   
}