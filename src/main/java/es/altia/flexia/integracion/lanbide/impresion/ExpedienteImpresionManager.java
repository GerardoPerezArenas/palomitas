/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.lanbide.impresion;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.agora.business.sge.SiguienteTramiteTO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.manual.TramitacionExpedientesDAO;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import static es.altia.common.util.io.MimeTypes.ODT;
import static es.altia.common.util.io.MimeTypes.RTF;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.io.IOOperations;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
//import net.sf.jasperreports.engine.export.FontKey;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
//import net.sf.jasperreports.engine.export.PdfFont;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author paz.rodriguez
 */
public class ExpedienteImpresionManager {

    private static ExpedienteImpresionManager instance = null;
    /* Declaracion de servicios */
    protected static Config m_ConfigCommon;
    protected static Config m_ConfigTechnical; // Para el fichero de configuracion technical
    protected static Config m_ConfigError; // Para el fichero de mensajes de error localizados
    private Logger log = Logger.getLogger(ExpedienteImpresionManager.class);


    public static ExpedienteImpresionManager getInstance() {
        if (instance == null) {
        // Sincronización para serializar (no multithread) las invocaciones a este metodo
            synchronized (ExpedienteImpresionManager.class) {
        if (instance == null) {
            instance = new ExpedienteImpresionManager();
        }
        }
        }
        return instance;
    }

    private ExpedienteImpresionManager(){
    }
    
public Vector getExpedientesImpresion(int codOrganizacion,String codProcedimiento, int codTramite, String[] params){
    
    Vector lista = new Vector();
    Connection con = null;
    AdaptadorSQLBD adapt = null;

    try{
        adapt = new AdaptadorSQLBD(params);
        con = adapt.getConnection();
        lista = ExpedienteImpresionDAO.getInstance().getExpedientesImpresion(codOrganizacion,codProcedimiento,codTramite, con);
    }catch(BDException e){
        e.printStackTrace();
    }finally{
        try{
            adapt.devolverConexion(con);
        }catch(BDException e){
            e.printStackTrace();
        }
    }
    return lista;
}

public Vector getFicherosImpresionGenerados(String[] params){
    
    Vector lista = new Vector();
    Connection con = null;
    AdaptadorSQLBD adapt = null;

    try{
        adapt = new AdaptadorSQLBD(params);
        con = adapt.getConnection();
        lista = ExpedienteImpresionDAO.getInstance().getFicherosImpresionGenerados(con);
    }catch(BDException e){
        e.printStackTrace();
    }finally{
        try{
            adapt.devolverConexion(con);
        }catch(BDException e){
            e.printStackTrace();
        }
    }
    return lista;

    }


/***
public Vector consultar(String codExpediente, String tSexo, String tFechNacimiento, UsuarioValueObject usuario, String[] params)
    throws Exception {

    Vector consulta;
    Connection con = null;
    AdaptadorSQLBD adapt = null;
    try {
        adapt = new AdaptadorSQLBD(params);
        con = adapt.getConnection();
        consulta = ExpedienteImpresionDAO.getInstance().consultar(codExpediente, tSexo, tFechNacimiento, usuario, con);
    } catch (Exception ce) {
        log.error("JDBC Technical problem " + ce.getMessage());
        throw new Exception("Problema técnico de JDBC " + ce.getMessage());
    }

    return consulta;
}
**/

public Vector consultar(String codExpediente, String tSexo, String tFechNacimiento, UsuarioValueObject usuario, String[] params)
    throws Exception {
    //ImpresionExpedientesLanbideValueObject salida  = null;
    Vector salida = null;
    Connection con = null;
    AdaptadorSQLBD adapt = null;
    try {
        adapt = new AdaptadorSQLBD(params);
        con = adapt.getConnection();
        salida  = ExpedienteImpresionDAO.getInstance().consultar(codExpediente, tSexo, tFechNacimiento, usuario, con);
        
    } catch (Exception ce) {
        log.error("JDBC Technical problem " + ce.getMessage());
        throw new Exception("Problema técnico de JDBC " + ce.getMessage());
    } finally{
        try{
            if(con!=null) con.close();
        }catch(SQLException e){
            log.error("Error al cerrar una conexión a la BBDD: " + e.getMessage());
        }
    }

    return salida;
}

public String getCertificadoDuplicidad(String numExp, String[] params)
    throws Exception {
    String certificado = "";
    Connection con = null;
    AdaptadorSQLBD adapt = null;
    try {
        adapt = new AdaptadorSQLBD(params);
        con = adapt.getConnection();
        certificado  = ExpedienteImpresionDAO.getInstance().getCertificadoDuplicidad(numExp, con);
        
    } catch (Exception ce) {
        log.error("JDBC Technical problem " + ce.getMessage());
        throw new Exception("Problema técnico de JDBC " + ce.getMessage());
    } finally{
        try{
            if(con!=null) con.close();
        }catch(SQLException e){
            log.error("Error al cerrar una conexión a la BBDD: " + e.getMessage());
        }
    }

    return certificado;
}

public String compruebaExpedientes(String numExp, String certificadoDu, String[] params)
    throws Exception {
    String duplicado = "";
    Connection con = null;
    AdaptadorSQLBD adapt = null;
    try {
        adapt = new AdaptadorSQLBD(params);
        con = adapt.getConnection();
        duplicado  = ExpedienteImpresionDAO.getInstance().compruebaExpedientes(numExp,certificadoDu,con);
        
    } catch (Exception ce) {
        log.error("JDBC Technical problem " + ce.getMessage());
        throw new Exception("Problema técnico de JDBC " + ce.getMessage());
    } finally{
        try{
            if(con!=null) con.close();
        }catch(SQLException e){
            log.error("Error al cerrar una conexión a la BBDD: " + e.getMessage());
        }
    }

    return duplicado;
}

public boolean cerrarTramitesExpediente(String numExp, String[] params) throws Exception {
        boolean insertOK;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            insertOK  = ExpedienteImpresionDAO.getInstance().cerrarTramitesExpediente(numExp,con);

        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            throw new Exception("Problema técnico de JDBC " + ce.getMessage());
        } finally{
        try{
            if(con!=null) con.close();
        }catch(SQLException e){
            log.error("Error al cerrar una conexión a la BBDD: " + e.getMessage());
        }
    }
        return insertOK;
    }

public boolean cambioEstadoExpediente(String numExp, String[] params) throws Exception {
        boolean insertOK;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            insertOK  = ExpedienteImpresionDAO.getInstance().cambioEstadoExpediente(numExp,con);
            if(insertOK)ExpedienteImpresionDAO.getInstance().cambioEstadoExpedienteInsert(numExp,con);

        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            throw new Exception("Problema técnico de JDBC " + ce.getMessage());
        } finally{
        try{
            if(con!=null) con.close();
        }catch(SQLException e){
            log.error("Error al cerrar una conexión a la BBDD: " + e.getMessage());
        }
    }
        return insertOK;
    }

public boolean cambioObservExpediente(String numExp, String[] params) throws Exception {
        boolean insertOK;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            insertOK  = ExpedienteImpresionDAO.getInstance().cambioObservExpediente(numExp,con);

        } catch (Exception ce) {
            log.error("JDBC Technical problem " + ce.getMessage());
            throw new Exception("Problema técnico de JDBC " + ce.getMessage());
        } finally{
        try{
            if(con!=null) con.close();
        }catch(SQLException e){
            log.error("Error al cerrar una conexión a la BBDD: " + e.getMessage());
        }
    }
        return insertOK;
    }

public Vector finalizarTramitesImpresion (TramitacionExpedientesValueObject tEVO, String[] params)  throws TramitacionException, TechnicalException, WSException, EjecucionSWException {

    Vector devolver = new Vector();    
    int resultado = 0;
    log.debug(ExpedienteImpresionManager.class.getName() + "--> finalizarTramitesImpresion");
    try {
        log.debug("************* ExpedienteImpresionManager.finalizarTramitesImpresion ==============>");
        /*
        oad = new AdaptadorSQLBD(params);
        
        resultado = ExpedienteImpresionDAO.getInstance().finalizarTramiteImpresion(oad, con, tEVO);
        if (resultado > 0) {
            */
            log.debug("************* TramitacionExpedientesDAO.finalizacionComunConTramites ==============>");
            devolver = TramitacionExpedientesDAO.getInstance().finalizarConTramites(tEVO, params);
            log.debug("************* TramitacionExpedientesDAO.finalizacionComunConTramites <==============");
             
        //}
        log.debug("************* ExpedienteImpresionManager.finalizarTramitesImpresion ==============>");
    } catch (EjecucionSWException eswe) {
        log.debug(" ============ ERROR.ExpedienteImpresionManager.finalizarTramitesImpresion: " + eswe.getMessage());
       throw eswe;
    } catch (TramitacionException te) {
        log.debug(" ============ ERROR.ExpedienteImpresionManager.finalizarTramitesImpresion: " + te.getMessage());
       throw te;
    } 
    log.debug(TramitacionExpedientesDAO.class.getName() + "<=========== finalizarTramitesImpresion");
    return devolver;
    }



public Vector<SiguienteTramiteTO> getFlujoSalida(Connection con,
            String codOrg, String codProc, String codTram, int numCod)
        throws SQLException {

    Vector<SiguienteTramiteTO> listaTramitesFavorables = new Vector<SiguienteTramiteTO>();
    listaTramitesFavorables =ExpedienteImpresionDAO.getInstance().getFlujoSalida(con, codOrg, codProc, codTram, 0);

    return listaTramitesFavorables;

}



/**
     public TramitacionExpedientesValueObject getInfoTramite(String codProc, String lListaExpedientesSeleccionados, String codTramiteImpresion, String[] params) {
         AdaptadorSQLBD adapt = null;
         Connection con = null;
         TramitacionExpedientesValueObject tVO = new TramitacionExpedientesValueObject();
         
         try{
             adapt = new AdaptadorSQLBD(params);
             con = adapt.getConnection();         
             tVO = ExpedienteImpresionDAO.getInstance().getInfoTramite(codProc, lListaExpedientesSeleccionados, codTramiteImpresion, con);
         
         }catch(BDException e){
             log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
         }finally{
             
             try{
                adapt.devolverConexion(con);
             }catch(Exception e){
                 log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
             }
         }
         
         return tVO;
     } ***/

     
     
     public byte[] getContenidoDocumento(String nombre, String[] params) {
         AdaptadorSQLBD adapt = null;
         Connection con = null;
         byte[] contenido = null;
         try{
             adapt = new AdaptadorSQLBD(params);
             con = adapt.getConnection();
             
             contenido = ExpedienteImpresionDAO.getInstance().getContenidoDocumento(nombre, con);
             
         }catch(Exception e){
             log.error("Error al recuperar el contenido del documento de impresión en formato excel: " + e.getMessage());
         }finally{
             try{
                adapt.devolverConexion(con);
             }catch(Exception e){
                 log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
             }
         }
         
         return contenido;
     }
     
     public ArrayList<String> getExpediente(String nombre, String[] params) {
         AdaptadorSQLBD adapt = null;
         Connection con = null;
         ArrayList<String> contenido = new ArrayList<String>();
         try{
             adapt = new AdaptadorSQLBD(params);
             con = adapt.getConnection();
             
             contenido = ExpedienteImpresionDAO.getInstance().getExpediente(nombre, con);
             
         }catch(Exception e){
             log.error("Error al recuperar el contenido del documento de impresión en formato excel: " + e.getMessage());
         }finally{
             try{
                adapt.devolverConexion(con);
             }catch(Exception e){
                 log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
             }
         }
         
         return contenido;
     }
     
     public ArrayList<Participantes> getParticipantes(String expediente, String[] params) {
         AdaptadorSQLBD adapt = null;
         Connection con = null;
         ArrayList<Participantes> contenido = new ArrayList<Participantes>();
         try{
             adapt = new AdaptadorSQLBD(params);
             con = adapt.getConnection();
             
             contenido = ExpedienteImpresionDAO.getInstance().leerDatosParticipantes(expediente, con);
             
         }catch(Exception e){
             log.error("Error en getParticipantes " + e.getMessage());
         }finally{
             try{
                adapt.devolverConexion(con);
             }catch(Exception e){
                 log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
             }
         }
         
         return contenido;
     }

     
     
      public ArrayList<ImpresionExpedientesLanbideValueObject> getListaExpedientesDocumento(int codOrganizacion, String nombreFichero, String[] params) {
          ArrayList<ImpresionExpedientesLanbideValueObject> salida = null;
          AdaptadorSQLBD adapt = null;
          Connection con = null;
          
          try{
              adapt = new AdaptadorSQLBD(params);
              con = adapt.getConnection();
           
              salida = ExpedienteImpresionDAO.getInstance().getListaExpedientesDocumento(codOrganizacion,nombreFichero, con);
              
          }catch(BDException e){
              log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
          }finally{
              try{
                  adapt.devolverConexion(con);
              }catch(BDException e){
                  log.error("Error al cerrar una conexión a la BBDD: " + e.getMessage());
              }
          }          
          return salida;
      }
      
      public static Subreport inicializarSubreport(String subreportName, String xmlReport, String xpathRootExpr, Map<String, Object> subreportParams) throws JRException, ParserConfigurationException, SAXException, IOException
        {
            Subreport subreport = null;
            try
            {
                String urlSubreport = "/es/altia/flexia/integracion/lanbide/impresion/etiquetas.jasper";
                InputStream jasperReportAsStream = ExpedienteImpresionManager.class.getResourceAsStream(urlSubreport);
                JasperReport subreportJR = (JasperReport) JRLoader.loadObject(jasperReportAsStream);

                JRXmlDataSource ds = null;
                if(xmlReport != null)
                {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    factory.setValidating(false);
                    factory.setNamespaceAware(false);
                    factory.setIgnoringComments(true);
                    DocumentBuilder db = factory.newDocumentBuilder();
                    InputSource inStream = new InputSource(new StringReader(xmlReport));
                    //db.setErrorHandler(new MeLanbide03ReportErrorHandler());
                    Document doc = db.parse(inStream);
                    ds = new JRXmlDataSource(doc, xpathRootExpr);
                }
                subreport = new Subreport(subreportJR, ds, subreportParams);
            }
            catch(Exception ex)
            {
                
                //log.error("Error en inicializarSubreport: " + ex.toString());
            }
            return subreport;
        }
      
      public static byte[] runMasterReportWithSubreports(String masterReportName, Map<String, Object> reportParams, List<Subreport> subreportList, String reportFormat)
        {
            byte[] result = null;
            String urlMasterReport = "/es/altia/flexia/integracion/lanbide/impresion/etiquetas.jasper";

            if(reportParams == null)
            {
                reportParams = new HashMap();
            }

            JasperPrint jasperPrint = null;
            try 
            {
                InputStream jasperReportAsStream = ExpedienteImpresionManager.class.getResourceAsStream(urlMasterReport);
                System.setProperty("java.awt.headless", "true"); 

                JasperReport masterReport = (JasperReport) JRLoader.loadObject(jasperReportAsStream);

                if(subreportList == null || subreportList.isEmpty())
                {
                    jasperPrint = JasperFillManager.fillReport(masterReport, reportParams, new JREmptyDataSource(1));
                }
                else
                {
                    jasperPrint = JasperFillManager.fillReport(masterReport, reportParams, new JRBeanCollectionDataSource(subreportList));
                }
            } 
            catch (Exception e) 
            {
                e.printStackTrace();        	
                jasperPrint = null;

            }

            if (jasperPrint!=null) 
            {
                final ByteArrayOutputStream outAux = new ByteArrayOutputStream();
                try
                {
                    JRAbstractExporter exporter = null;
                    /*
                    Desactivamos fuentes personalizadas en el report - la version 6 de jasperreport no soporta esta funcionalidad
                    
                    exporter = new JRPdfExporter();
                    FontKey keyArial = new FontKey("Arial", false, false);  
                    PdfFont fontArial = new PdfFont("Helvetica","Cp1252",false); 

                    FontKey keyArialBold = new FontKey("Arial", true, false);  
                    PdfFont fontArialBold = new PdfFont("Helvetica-Bold","Cp1252",false); 

                    FontKey keyDialog = new FontKey("Dialog", false, false);  
                    PdfFont fontDialog = new PdfFont("Helvetica","Cp1252",false);

                    FontKey keyDialogBold = new FontKey("Dialog", false, false);  
                    PdfFont fontDialogBold = new PdfFont("Helvetica-Bold","Cp1252",false);


                    Map fontMap = new HashMap();
                    fontMap.put(keyArial,fontArial);
                    fontMap.put(keyArialBold,fontArialBold);
                    fontMap.put(keyDialog,fontDialog);
                    fontMap.put(keyDialogBold,fontDialogBold);
                    
                    exporter.setParameter(JRExporterParameter.FONT_MAP,fontMap);   
                    */
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, (outAux));
                    exporter.exportReport();
                    result = (outAux.toByteArray());
                } 
                catch(Exception e) 
                {
                     e.printStackTrace();
                } 
                finally 
                {
                    IOOperations.closeOutputStreamSilently(outAux);
                }
            }
            return result;
        }
      public static String generarXML (String reportVO){
        //if(log.isDebugEnabled()) log.debug("generarXML() : BEGIN ");
        String xml = new String();
        StringBuilder certificados = new StringBuilder();
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        
        xml += getDoctype();
        xml += getNodoPrincipal(certificados.toString());
        //if(log.isDebugEnabled()) log.debug("generarXML() : END ");
        return xml;
    }//generarXML
      
      private static String getDoctype(){
        String doctype = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
        return doctype;
    }//getDoctype
    
    private static String getNodoPrincipal(String valor){
        String nodo = "certificados";
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor))
            xml.append("<").append(nodo).append(">").append(valor).append("</").append(nodo).append(">");
        else
            xml.append("<").append(nodo).append(">").append(" ").append("</").append(nodo).append(">");
        return xml.toString();
    }//getNodoPrincipal
    
    public List<String> getExpMismoCertificadoPorExp(String numExp, String certificado, String[] params) throws Exception{
    Connection con = null;
    AdaptadorSQLBD adapt = null;

    try{
        adapt = new AdaptadorSQLBD(params);
        con = adapt.getConnection();
       List<String> lista = ExpedienteImpresionDAO.getInstance().getExpMismoCertificadoPorExp(numExp, certificado, con);
       return lista;
    }catch(BDException e){
        e.printStackTrace();
    }finally{
        try{
            adapt.devolverConexion(con);
        }catch(BDException e){
            e.printStackTrace();
        }
    }
    return null;
}
}
