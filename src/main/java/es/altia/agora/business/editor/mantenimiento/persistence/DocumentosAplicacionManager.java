package es.altia.agora.business.editor.mantenimiento.persistence;

import java.util.Vector;
import es.altia.common.service.config.*;
import es.altia.common.exception.TechnicalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.editor.mantenimiento.persistence.manual.DocumentosAplicacionDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;


public class DocumentosAplicacionManager
{

   private static DocumentosAplicacionManager instance = null;
   protected static Config conf;
   protected static Log m_Log =
          LogFactory.getLog(DocumentosAplicacionManager.class.getName());

   protected DocumentosAplicacionManager()
   {
     //Queremos usar el fichero de configuración technical
     conf = ConfigServiceHelper.getConfig("techserver");
   }

   /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de DocumentosAplicacionManager
    */
   public static DocumentosAplicacionManager getInstance()
   {
     //Si no hay una instancia de esta clase tenemos que crear una.
     if (instance == null)
     {
       // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
       synchronized(DocumentosAplicacionManager.class)
       {
         if (instance == null)
           instance = new DocumentosAplicacionManager();
       }
     }
     return instance;
   }

   public Vector loadAplicaciones(String[] params,String codigoAplicacionDocumentos)
   {
     //queremos estar informados de cuando este metodo es ejecutado
     m_Log.debug("loadAplicaciones");
     Vector r = new Vector();
     try
     {
       r = DocumentosAplicacionDAO.getInstance().loadAplicaciones(params,codigoAplicacionDocumentos);
     }
     catch (Exception e)
     {
       m_Log.error("Excepción capturada: " + e.toString());
     }
     m_Log.debug("loadAplicaciones");
     return r;
   }


   public Vector loadProcedimientos(String[] params)
   {
     //queremos estar informados de cuando este metodo es ejecutado
     m_Log.debug("loadProcedimientos");
     Vector r = new Vector();
     try
     {
       r = DocumentosAplicacionDAO.getInstance().loadProcedimientos(params);
     }
     catch (Exception e)
     {
       m_Log.error("Excepción capturada: " + e.toString());
     }
     m_Log.debug("loadProcedimientos");
     return r;
   }
   
   public Vector loadTramites(String[] params)
   {
     //queremos estar informados de cuando este metodo es ejecutado
     m_Log.debug("loadTramites");
     Vector r = new Vector();
     try
     {
       r = DocumentosAplicacionDAO.getInstance().loadTramites(params);
     }
     catch (Exception e)
     {
       m_Log.error("Excepción capturada: " + e.toString());
     }
     m_Log.debug("loadTramites");
     return r;
   }


   public Vector loadDocumentos(GeneralValueObject gVO,String[] params)
   {
     //queremos estar informados de cuando este metodo es ejecutado
     m_Log.debug("loadDocumentos");
     Vector r = new Vector();
     try
     {
       r = DocumentosAplicacionDAO.getInstance().loadDocumentos(gVO,params);
      }
     catch (Exception e)
     {
       m_Log.error("Excepción capturada: " + e.toString());
       e.printStackTrace();
     }
     m_Log.debug("loadDocumentos");
     return r;
   }
   
   public Vector loadDocumentosDesdeDefinicion(GeneralValueObject gVO,String[] params)
   {
     //queremos estar informados de cuando este metodo es ejecutado
     m_Log.debug("loadDocumentos");
     Vector r = new Vector();
     try
     {
       r = DocumentosAplicacionDAO.getInstance().loadDocumentosDesdeDefinicion(gVO,params);
      }
     catch (Exception e)
     {
       m_Log.error("Excepción capturada: " + e.toString());
       e.printStackTrace();
     }
     m_Log.debug("loadDocumentos");
     return r;
   }

    public Vector loadEtiquetas(GeneralValueObject gVO, String[] params) throws TechnicalException {
        m_Log.debug("loadEtiquetas");
        Vector r;
        try {
            r = DocumentosAplicacionDAO.getInstance().loadEtiquetas(gVO, params);
        } catch (BDException bde) {
            m_Log.error("Excepción capturada: " + bde.toString());
            bde.printStackTrace();
            throw new TechnicalException("NO SE HAN PODIDO CARGAR LAS ETIQUETAS", bde);
        }
        m_Log.debug("loadEtiquetas");
        return r;
    }
    
     public Vector loadEtiquetasPlantillaODT(GeneralValueObject gVO, String[] params) throws TechnicalException {
        m_Log.debug("loadEtiquetas");
        Vector r;
        try {
            r = DocumentosAplicacionDAO.getInstance().loadEtiquetasPlantillaODT(gVO, params);
        } catch (BDException bde) {
            m_Log.error("Excepción capturada: " + bde.toString());
            bde.printStackTrace();
            throw new TechnicalException("NO SE HAN PODIDO CARGAR LAS ETIQUETAS", bde);
        }
        m_Log.debug("loadEtiquetas");
        return r;
    }
    
    
   
   public GeneralValueObject loadDocumento(GeneralValueObject gVO,String[] params)
   {
     //queremos estar informados de cuando este metodo es ejecutado
     m_Log.debug("loadDocumento");
     GeneralValueObject r=null;
     try
     {
       r = DocumentosAplicacionDAO.getInstance().loadDocumento(gVO,params);
     }
     catch (Exception e)
     {
       m_Log.error("Excepción capturada: " + e.toString());
     }
     m_Log.debug("loadDocumento");
     return (r);
   }


   public int eliminaDocumento(GeneralValueObject gVO,String[] params)
   {
     //queremos estar informados de cuando este metodo es ejecutado
     m_Log.debug("eliminaDocumento");
     int resultadoEliminacion=0;
     try
     {
       resultadoEliminacion = DocumentosAplicacionDAO.getInstance().eliminaDocumento(gVO,params);
     }
     catch (Exception e)
     {
       m_Log.error("Excepción capturada: " + e.toString());
     }
     m_Log.debug("eliminaDocumento");
     return (resultadoEliminacion);
   }
   public int eliminaDocumentoAplicacion(GeneralValueObject gVO,String[] params)
   {
     //queremos estar informados de cuando este metodo es ejecutado
     m_Log.debug("eliminaDocumento");
     int resultadoEliminacion=0;
     try
     {
       resultadoEliminacion = DocumentosAplicacionDAO.getInstance().eliminaDocumentoAplicacion(gVO,params);
     }
     catch (Exception e)
     {
       m_Log.error("Excepción capturada: " + e.toString());
     }
     m_Log.debug("eliminaDocumento");
     return (resultadoEliminacion);
   }
   
   public int documentoVisibleExterior(GeneralValueObject gVO, String[] params){	
       m_Log.debug("documentoVisibleExterior");	
       int result=0;	
       try{	
           result=DocumentosAplicacionDAO.getInstance().documentoVisibleExterior(gVO, params);	
       }catch(Exception e){	
           m_Log.error("Excepci?n capturada: "+e.toString());	
       }	
       return result;	
   }
   
   public int grabarDocumento(GeneralValueObject gVO,String[] params)
   {
     //queremos estar informados de cuando este metodo es ejecutado
     m_Log.debug("grabarDocumento");
     int resultado=0;
     try
     {
       resultado = DocumentosAplicacionDAO.getInstance().grabarDocumento(gVO,params);
     }
     catch (Exception e)
     {
       m_Log.error("Excepción capturada: " + e.toString());
     }
     m_Log.debug("grabarDocumento");
     return (resultado);
   }
   
   public int modificarInteresado(GeneralValueObject gVO,String[] params)
   {
     //queremos estar informados de cuando este metodo es ejecutado
     m_Log.debug("modificarInteresado");
     int resultado=0;
     try
     {
       resultado = DocumentosAplicacionDAO.getInstance().modificarInteresado(gVO,params);
     }
     catch (Exception e)
     {
       m_Log.error("Excepción capturada: " + e.toString());
     }
     m_Log.debug("modificarInteresado");
     return (resultado);
   }
   
   public boolean esDocumentoParaRelacion(int codMunicipio, String codProcedimiento, int codTramite, int codDocumento, String[] params){
        AdaptadorSQLBD adapt = null;
        Connection con  = null;
        boolean paraRelacion = false;
        
        try{

            adapt= new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            paraRelacion = DocumentosAplicacionDAO.getInstance().esDocumentoParaRelacion(codMunicipio, codProcedimiento, codTramite, codDocumento, con);
            
        }catch(Exception e){
            m_Log.error("Ha ocurrido un error al comprobar si el documento se ha creado para una relación.");
            e.printStackTrace();
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                m_Log.error("Ha ocurrido un error al cerrar la conexión de base de datos.");
            }
        }
        
        return paraRelacion;
   }
    public Vector loadDespPlantillaActiva (String []params, String nombreCampo) throws TechnicalException {
        m_Log.debug("loadDespPlantillaActiva");
        Vector r;
        try {
            r = DocumentosAplicacionDAO.getInstance().loadDespPlantillaActiva(params, nombreCampo);
        } catch (Exception bde) {
            m_Log.error("Excepción capturada: " + bde.toString());
            bde.printStackTrace();
            throw new TechnicalException("NO SE HAN PODIDO CARGAR LOS DATOS DEL DESPLEGABLE PLANTILLAS ACTIVAS", bde);
        }
        m_Log.debug("loadDespPlantillaActiva");
        return r;
    }

}