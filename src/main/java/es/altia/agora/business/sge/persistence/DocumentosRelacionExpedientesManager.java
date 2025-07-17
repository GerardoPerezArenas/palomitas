package es.altia.agora.business.sge.persistence;

import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.sge.persistence.manual.DocumentosRelacionExpedientesDAO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.business.SWFirmaDocRelacionManager;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.SQLException;


public class DocumentosRelacionExpedientesManager {

    private static DocumentosRelacionExpedientesManager instance = null;
    protected static Config conf;
    protected static Config m_ConfigCommon;
    protected static Log m_Log = LogFactory.getLog(DocumentosRelacionExpedientesManager.class.getName());

    protected DocumentosRelacionExpedientesManager() {
        //Queremos usar el fichero de configuración technical y common
        conf = ConfigServiceHelper.getConfig("techserver");
        m_ConfigCommon = ConfigServiceHelper.getConfig("common");
    }

    /**
     * Factory method para el <code>Singelton</code>.
     * @return La unica instancia de DocumentosAplicacionManager
     */
    public static DocumentosRelacionExpedientesManager getInstance() {

        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(DocumentosRelacionExpedientesManager.class) {
                if (instance == null)
                    instance = new DocumentosRelacionExpedientesManager();
            }
        }
        return instance;
    }


    public String consultaXML(GeneralValueObject gVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("DocumentosRelacionExpedientesManager.consultaXML");

        String resultado="";
        try {
            resultado = DocumentosRelacionExpedientesDAO.getInstance().consultaXML(gVO,params);
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Excepción capturada: " + e.toString());
        }
        return (resultado);
    }

    public String consultaXMLRelacion(GeneralValueObject gVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("DocumentosRelacionExpedientesManager.consultaXMLRelacion");

        String resultado="";
        try {
            resultado = DocumentosRelacionExpedientesDAO.getInstance().consultaXMLRelacion(gVO,params);
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Excepción capturada: " + e.toString());
        }
        return (resultado);
    }

     public String consultaXMLRelacionDocumentoNormal(GeneralValueObject gVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("DocumentosRelacionExpedientesManager.consultaXMLRelacionDocumentoNormal");

        String resultado="";
        try {
            resultado = DocumentosRelacionExpedientesDAO.getInstance().consultaXMLRelacionDocumentoNormal(gVO,params);
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Excepción capturada: " + e.toString());
        }
        return (resultado);
    }




    public String consultaXML2Relacion(GeneralValueObject gVO,String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.info("DocumentosRelacionExpedientesManager.consultaXML2Relacion");

        String resultado="";
        try {
            resultado = DocumentosRelacionExpedientesDAO.getInstance().consultaXML2Relacion(gVO,params);
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Excepción capturada: " + e.toString());
        }
        return (resultado);
    }

    public byte[] loadDocumento(GeneralValueObject gVO,String[] params) {

        // Comienzo del metodo.
        m_Log.info("DocumentosRelacionExpedientesManager.loadDocumento");

        byte[] resultado=null;
        try {
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si"))
                resultado = SWFirmaDocRelacionManager.getInstance(params).loadDocumento(gVO);
            else
                resultado = DocumentosRelacionExpedientesDAO.getInstance().loadDocumento(gVO,params);
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Excepción capturada: " + e.toString());
        }
        return resultado;
    }
   

    public int grabarDocumento(GeneralValueObject gVO, String[] params) {

        // Comienzo del metodo.
        m_Log.info("DocumentosRelacionExpedientesManager.grabarDocumento");
        int resultado;

        try {
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si"))
                resultado = SWFirmaDocRelacionManager.getInstance(params).grabarDocumentoRelacion(gVO);
            else
                resultado = DocumentosRelacionExpedientesDAO.getInstance().grabarDocumento(gVO,params);
        } catch (Exception e) {
            resultado = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Excepción capturada: " + e.toString());
        }
        return (resultado);
    }


    public TramitacionExpedientesValueObject cargarDocumentos(TramitacionExpedientesValueObject tEVO, String[] params)
    throws AnotacionRegistroException {

      m_Log.debug("cargarDocumentos");
      try {
        m_Log.debug("Usando persistencia manual");
        tEVO = DocumentosRelacionExpedientesDAO.getInstance().cargarDocumentos(tEVO,params);
        m_Log.debug("docs cargados");
      } catch (AnotacionRegistroException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
        throw new AnotacionRegistroException("Problema técnico de JDBC " + te.getMessage());
      } catch(Exception e) {
        e.printStackTrace();
      }
      m_Log.debug("Fin cargarDocumentos()");
      return tEVO;
    }



     public int grabarDocumentoGestor(GeneralValueObject gVO, String[] params) {

        // Comienzo del metodo.
        m_Log.info("DocumentosRelacionExpedientesManager.grabarDocumento");
        int resultado;

        try {
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si"))
                resultado = SWFirmaDocRelacionManager.getInstance(params).grabarDocumentoRelacion(gVO);
            else
                resultado = DocumentosRelacionExpedientesDAO.getInstance().grabarDocumentoGestor(gVO,params);
        } catch (Exception e) {
            resultado = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Excepción capturada: " + e.toString());
        }
        return (resultado);
    }




     public String getNombreDocumentoRelacionGestor(GeneralValueObject gVO,String[] params){
         Connection con = null;
         String nombreDocumento = "";
         try{
             AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
             con = adapt.getConnection();
             nombreDocumento = DocumentosRelacionExpedientesDAO.getInstance().getNombreDocumentoRelacionGestor(gVO, con);

         }catch(Exception e){
             e.printStackTrace();             
         }finally{
             try{
                 if(con!=null) con.close();
             }catch(SQLException e){
                }
         }

         return nombreDocumento;
     }


     /******/

    public int grabarDocumentoContenido(GeneralValueObject gVO, String[] params) {

        // Comienzo del metodo.
        m_Log.info("DocumentosRelacionExpedientesManager.grabarDocumento");
        int resultado;

        try {
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si"))
                resultado = SWFirmaDocRelacionManager.getInstance(params).grabarDocumentoRelacion(gVO);
            else
                resultado = DocumentosRelacionExpedientesDAO.getInstance().grabarDocumentoContenido(gVO,params);
        } catch (Exception e) {
            resultado = 0;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Excepción capturada: " + e.toString());
        }
        return (resultado);
    }

}