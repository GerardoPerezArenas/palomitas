/**
 * WSTramitacionFlexiaImplSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.4 Apr 22, 2006
 * (06:55:48 PDT) WSDL2Java emitter.
 */
package es.altia.flexiaWS.documentos;


import es.altia.flexia.business.documentosCSV.persistence.DocumentosCSVDAO;

import es.altia.flexiaWS.documentos.bd.datos.InfoConexionVO;

import es.altia.flexiaWS.documentos.bd.util.*;

import es.altia.flexiaWS.documentos.bd.datos.SalidaFicheroDocumento;

import es.altia.util.conexion.AdaptadorSQLBD;
import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import es.altia.flexiaWS.documentos.bd.datos.FicheroDocumentoVO;
import es.altia.util.struts.StrutsUtilOperations;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;

import es.altia.agora.business.documentos.DocumentoManager;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.exception.JustificanteRegistroException;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.sge.MetadatosDocumentoVO;
import es.altia.agora.business.sge.persistence.CSVPendienteVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.ConstantesWS;
import es.altia.common.exception.TechnicalException;
import es.altia.common.util.io.MimeTypes;
import es.altia.flexia.interfaces.user.web.registro.informes.EjecutaJustificantePDFFactoria;
import es.altia.flexia.interfaces.user.web.registro.informes.IEjecutaJustificantePDF;
import es.altia.flexia.registro.justificante.vo.JustificanteRegistroPersonalizadoVO;
import es.altia.flexiaWS.documentos.bd.datos.AnotacionVO;
import es.altia.flexiaWS.documentos.bd.datos.DocumentoRegistroVO;
import es.altia.flexiaWS.documentos.bd.datos.EstadoOperacionVO;
import es.altia.flexiaWS.documentos.bd.datos.SalidaCodigoCSV;
import es.altia.flexiaWS.documentos.bd.datos.SalidaJustificante;
import es.altia.util.conexion.BDException;
import es.altia.util.security.vo.AuthToken;
import es.altia.util.security.SeguridadManager;
import org.apache.axis.MessageContext;
import org.apache.axis.encoding.Base64;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;



public class WSDocumentosFlexiaImplSoapBindingImpl {
    
    Log m_Log = LogFactory.getLog(es.altia.flexiaWS.documentos.WSDocumentosFlexiaImplSoapBindingImpl.class);

    /**
     * Devuelve los tramites iniciados en el expediente.
     *
     * @param expediente
     * @param jndi Acceso a la BD
     * @return
     * @throws java.rmi.RemoteException
     */
    //TODO igualar con finalizarTramiteResolucion agora cuando solucionado retroceder tramite con formularios
   
    
    
     public es.altia.flexiaWS.documentos.bd.datos.SalidaFicheroDocumento getDocumentoByCSV(String csv,  InfoConexionVO infoConexion) throws java.rmi.RemoteException {
         
         
        AdaptadorSQLBD oad = null;
        Connection con = null;
        InputStream resultadoUrl = null;
        byte[] fichero = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpURLConnection conexion = null;
        SalidaFicheroDocumento salida=new SalidaFicheroDocumento();
        DocumentosCSVDAO documentoCSVDAO = DocumentosCSVDAO.getInstance();
        String org = infoConexion.getOrganizacion();
        String apli = infoConexion.getAplicacion();
        
        Vector mns = new Vector();
        
        String aplicaciones = Configuracion.getAplicacion();        
        String entidad = Configuracion.getEntidad(org);
        String urlFlexia=Configuracion.getUrlFlexia();
        
        
        boolean aplicacionTrue = false;
         
        try {
            
            
            aplicacionTrue = comprobarAplicacion(aplicaciones, apli);
            m_Log.debug("-->aplicacionTrue " + aplicacionTrue);
            if (aplicacionTrue) {
            } else {
                
                salida.setIncidencias("La aplicación no tiene permisos para acceder a la conexión");
                salida.setCodigoError(-1);
                mns.add("La aplicación no tiene permisos para acceder a la conexión");
            }
            
        } catch (Throwable e) {
            e.printStackTrace();
            
            mns.add("No se pudo obtener una conexion a BD");
        }
        
        
          if (aplicacionTrue) {
            
            try {
                // Inicio de la operacion de alta de tercero y domicilio
                // Iniciamos transaccion
                        // Iniciamos transaccion
                String[] params = es.altia.flexiaWS.tramitacion.bd.util.Configuracion.getParamsBD(org);
                oad = new AdaptadorSQLBD(params);
                con = oad.getConnection();
                
                String url = documentoCSVDAO.getUrlFromCSV(org, csv, con);

                if(url==null || "".equals(url))
                {
                     salida.setCodigoError(1);
                     salida.setIncidencias("No existe referencia al CSV");
                     mns.add("No existe referencia al CSV");
                     
                }else{

                    try{
                        
                 
                    String ruta=urlFlexia+url+"&desdeWS=SI";
                    ruta=ruta.replaceAll(" ","%20");
                    URL urlServlet = new URL(ruta);
                    conexion = (HttpURLConnection) urlServlet.openConnection();

                    } catch (Exception e)
                    {
                        salida.setCodigoError(2);
                        salida.setIncidencias("Error al conectar con servlet");
                        return salida;
                    }
                    
                    resultadoUrl = conexion.getInputStream();

                    IOUtils.copy(resultadoUrl, out);

                    fichero = out.toByteArray();

                    if (fichero.length == 0) {

                        salida.setCodigoError(3);
                         salida.setIncidencias("El fichero no existe");                        

                    }else{

                        System.out.println("fichero " + fichero);

                        System.out.println("\n tamaño fichero en WS " + fichero.length);

                        FicheroDocumentoVO ficheroVO = new FicheroDocumentoVO();
                        ficheroVO.setBytes(fichero);
                        ficheroVO.setNombreDoc("pdfFromCSV");
                        ficheroVO.setTipomime("application/pdf");
                        salida.setResultado(ficheroVO);
                        salida.setCodigoError(0);
                    }
                    
                }
                

             } catch (Exception e) {

                 e.printStackTrace();
                 salida.setCodigoError(100);
                 salida.setIncidencias("Error al obtener el fichero");
             } finally {
                 try {
                     // Devolver la conexion
                     if (con != null && !con.isClosed()) {
                         con.close();
                     }
                 } catch (SQLException e) {
                     if (m_Log.isDebugEnabled()) {
                         m_Log.debug("Fallo al devolver la conexion");
                     }
                     e.printStackTrace();
                 }
                 IOUtils.closeQuietly(resultadoUrl);
                 if (conexion != null) {
                     conexion.disconnect();
                 }
             }

         }

    return salida;
    }
     
     
    /**
     * Genera un codigo seguro de verificacion para que una aplicacion externa
     * pueda insertarlo en el documento.
     * 
     * @param nombreDocumento Se utiliza en la generacion de la semilla
     * @param infoConexion
     * @return
     * @throws java.rmi.RemoteException 
     */
     public es.altia.flexiaWS.documentos.bd.datos.SalidaFicheroDocumento getCodigoCSV(String nombreDocumento,  InfoConexionVO infoConexion) throws java.rmi.RemoteException {
          
         
        AdaptadorSQLBD oad = null;
        Connection con = null;
        InputStream resultadoUrl = null;
        byte[] fichero = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpURLConnection conexion = null;
        SalidaFicheroDocumento salida=new SalidaFicheroDocumento();
        DocumentoManager documentoManager = DocumentoManager.getInstance();
        String org = infoConexion.getOrganizacion();
        String apli = infoConexion.getAplicacion();
        
        Vector mns = new Vector();
        
        String aplicaciones = Configuracion.getAplicacion();        
        String entidad = Configuracion.getEntidad(org);
        String urlFlexia=Configuracion.getUrlFlexia();
        String usarToken="NO";
        usarToken=Configuracion.getUsoToken();
        
        
        boolean aplicacionTrue = false;
        
        try {
            
            
            aplicacionTrue = comprobarAplicacion(aplicaciones, apli);
            
            if("SI".equals(usarToken)) 
            {
                salida.setIncidencias("La aplicación utiliza sistema de tokens para obtener el CSV, el método a usar es getCodigoCSVToken");
                salida.setCodigoError(200);
                mns.add("La aplicación utiliza sistema de tokens para obtener el CSV, el método a usar es getCodigoCSVToken");
                return salida;
            }
            m_Log.debug("-->aplicacionTrue " + aplicacionTrue);
            if (aplicacionTrue) {
            } else {
                
                salida.setIncidencias("La aplicación no tiene permisos para acceder a la conexión");
                salida.setCodigoError(-1);
                mns.add("La aplicación no tiene permisos para acceder a la conexión");
            }
            
        } catch (Throwable e) {
            e.printStackTrace();
            
            mns.add("Error al comprobar aplicación");
        }
        
        
          if (aplicacionTrue) {
            
            try { 
                
                        String CSV=documentoManager.crearCodigoSeguroVerificacion("", nombreDocumento);
                        salida.setCodigoError(0);
                        salida.setIncidencias(CSV);              

             } catch (Exception e) {

                 e.printStackTrace();
                 salida.setCodigoError(100);
                 salida.setIncidencias("Error al obtener el fichero");
             } finally {
                 try {
                     // Devolver la conexion
                     if (con != null && !con.isClosed()) {
                         con.close();
                     }
                 } catch (SQLException e) {
                     if (m_Log.isDebugEnabled()) {
                         m_Log.debug("Fallo al devolver la conexion");
                     }
                     e.printStackTrace();
                 }
                 IOUtils.closeQuietly(resultadoUrl);
                 if (conexion != null) {
                     conexion.disconnect();
                 }
             }

         }

    return salida;
    }
     
     
    public es.altia.flexiaWS.documentos.bd.datos.SalidaCodigoCSV getCodigoCSVToken(String nombreDocumento, InfoConexionVO infoConexion) throws java.rmi.RemoteException {
        EstadoOperacionVO estado = new EstadoOperacionVO();
        SalidaCodigoCSV salida = new SalidaCodigoCSV();
        salida.setEstado(estado);
        
        try {
            String apli = infoConexion.getAplicacion();
            String aplicaciones = Configuracion.getAplicacion();
            String codOrganizacion = infoConexion.getOrganizacion();
            
            // Comprobamos que la aplicacion tenga permiso para acceder al servicio web
            // No es por usuario, si no por el objeto INFO_CONEXION
            boolean aplicacionTrue = comprobarAplicacion(aplicaciones, apli);

            m_Log.debug("-->aplicacionTrue " + aplicacionTrue);

            if (aplicacionTrue) {
                MessageContext context = MessageContext.getCurrentContext();
                HttpServletRequest httpRequest =
                        (HttpServletRequest) context.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
  
                obtenerCodigoCSV(nombreDocumento, httpRequest, codOrganizacion, salida);
            } else {
                estado.setIncidencias("La aplicación no tiene permisos para acceder a la conexión");
                estado.setCodigoError(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            estado.setCodigoError(100);
            estado.setIncidencias("Error al obtener el csv");
            salida.setCsv("");
            salida.setToken("");
        }

        return salida;
    }
     
    /**
     * Inserta el documento de registro en el almacen de documentos
     * 
     * @param token
     * @param documento
     * @param infoConexion
     * @return
     * @throws java.rmi.RemoteException 
     */
    public EstadoOperacionVO setDocumentoRegistroSeguro(
            String token, DocumentoRegistroVO documento, InfoConexionVO infoConexion)
            throws java.rmi.RemoteException {

        m_Log.debug("setDocumentoRegistro - INI");
        
        EstadoOperacionVO estado = new EstadoOperacionVO();
        estado.setCodigoError(ConstantesWS.STATUS_WS_OK);
        estado.setIncidencias(ConstantesWS.DESC_STATUS_WS_OK);

        try {
            String aplicacion = infoConexion.getAplicacion();
            String aplicacionesPermitidas = Configuracion.getAplicacion();

            // Comprobacion de permisos
            boolean tienePermiso = comprobarAplicacion(aplicacionesPermitidas, aplicacion);

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("La aplicacion tiene permiso? = %b", tienePermiso));
            }

            if (tienePermiso) {
                insertarDocumentoRegistro(token, documento, infoConexion, estado);
            } else {
                estado.setIncidencias(ConstantesWS.DESC_STATUS_WS_NO_PERMISO);
                estado.setCodigoError(ConstantesWS.STATUS_WS_NO_PERMISO);
                m_Log.error(ConstantesWS.DESC_STATUS_WS_NO_PERMISO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(String.format(
                    "Error al intentar almacenar el documento de registro: %s",
                    e.getMessage()));
            
            estado.setIncidencias(ConstantesWS.DESC_STATUS_WS_ERROR_INTERNO);
            estado.setCodigoError(ConstantesWS.STATUS_WS_ERROR_INTERNO);
        }

        m_Log.debug("setDocumentoRegistro - FIN");
        
        return estado;
    }
    
    
    
    @Deprecated
    public EstadoOperacionVO setDocumentoRegistro(DocumentoRegistroVO documento, InfoConexionVO infoConexion)
            throws java.rmi.RemoteException {

        m_Log.debug("setDocumentoRegistro - INI");
        
        EstadoOperacionVO estado = new EstadoOperacionVO();
        estado.setCodigoError(ConstantesWS.STATUS_WS_OK);
        estado.setIncidencias(ConstantesWS.DESC_STATUS_WS_OK);

        try {
            String aplicacion = infoConexion.getAplicacion();
            String aplicacionesPermitidas = Configuracion.getAplicacion();

            // Comprobacion de permisos
            boolean tienePermiso = comprobarAplicacion(aplicacionesPermitidas, aplicacion);

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("La aplicacion tiene permiso? = %b", tienePermiso));
            }
            
             String usarToken="NO";
             usarToken=Configuracion.getUsoToken();
             if (StringUtils.isNotEmpty(documento.getCodigoSeguroVerificacion())) {
                if("SI".equals(usarToken)) 
                 {
                   estado.setIncidencias("La aplicación utiliza sistema de tokens para grabar el doc con CSV, el método a usar es setDocumentoRegistroSeguro");
                   estado.setCodigoError(200);

                   return estado;
               }
            }

            if (tienePermiso) {
                AnotacionRegistroManager anotacionRegistroManager = AnotacionRegistroManager.getInstance();
                String codOrganizacion = infoConexion.getOrganizacion();
                String[] params = es.altia.flexiaWS.tramitacion.bd.util.Configuracion.getParamsBD(codOrganizacion);
                byte[] fichero = Base64.decode(documento.getFichero());                
                
                GeneralValueObject parametros = new GeneralValueObject();
                parametros.setAtributo("paramsBBDD", params);
                parametros.setAtributo("idOrganizacion", NumberUtils.createInteger(codOrganizacion));
                parametros.setAtributo("ejercicio", documento.getEjercicio());
                parametros.setAtributo("numero", documento.getNumeroAnotacion());
                parametros.setAtributo("codTip", documento.getTipoEntrada());
                parametros.setAtributo("codOur", documento.getCodUor());
                parametros.setAtributo("codDep", documento.getCodDepartamento());
                parametros.setAtributo("entregado", ConstantesDatos.NO);
                parametros.setAtributo("nombreDocumentoCSV", documento.getNombreDocumento());
                parametros.setAtributo("fechaDoc", documento.getFechaDocumento());
                parametros.setAtributo("tipoMimeCSV", documento.getTipoMime());
                parametros.setAtributo("extensionCSV", MimeTypes.guessExtensionFromMimeType(documento.getTipoMime()));
                parametros.setAtributo("fichero", fichero);
                parametros.setAtributo("rutaCompletaFichero", "");
                if (StringUtils.isNotEmpty(documento.getCodigoSeguroVerificacion())) {
                    parametros.setAtributo("isExpHistorico", Boolean.FALSE);
                    String urlCsv = anotacionRegistroManager
                            .generarUriCsvDescargaFichero(parametros);
                    
                    MetadatosDocumentoVO metadatos = new MetadatosDocumentoVO();
                    metadatos.setCsv(documento.getCodigoSeguroVerificacion());
                    metadatos.setCsvAplicacion(infoConexion.getAplicacion());
                    metadatos.setCsvUri(urlCsv);
                    
                    parametros.setAtributo("metadatosCSV", metadatos);
                }
                
                anotacionRegistroManager.grabarDocumentoCSV(parametros);
            } else {
                estado.setIncidencias(ConstantesWS.DESC_STATUS_WS_NO_PERMISO);
                estado.setCodigoError(ConstantesWS.STATUS_WS_NO_PERMISO);
                m_Log.error(ConstantesWS.DESC_STATUS_WS_NO_PERMISO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(String.format(
                    "Error al intentar almacenar el documento de registro: %s",
                    e.getMessage()));
            
            estado.setIncidencias(String.format("%s: %s",
                            ConstantesWS.DESC_STATUS_WS_ERROR_INTERNO, e.getMessage()));
            estado.setCodigoError(ConstantesWS.STATUS_WS_ERROR_INTERNO);
        }

        m_Log.debug("setDocumentoRegistro - FIN");
        
        return estado;
    }
    
    public SalidaJustificante getJustificanteRegistro(AnotacionVO datosAnotacion, InfoConexionVO infoConexion)
            throws java.rmi.RemoteException {
        String params[] = null;
        JustificanteRegistroPersonalizadoVO justificanteActivo = null;
        String xml = null;
        String directorio = null;
        String formatoFecha = null;
        String oficinaRegistro = null;
        byte[] informe = null;
        SalidaJustificante salida = new SalidaJustificante();
        

        try {
            String aplicacion = infoConexion.getAplicacion();
            String aplicacionesPermitidas = Configuracion.getAplicacion();

            // Comprobacion de permisos
            boolean tienePermiso = comprobarAplicacion(aplicacionesPermitidas, aplicacion);

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("La aplicacion tiene permiso? = %b", tienePermiso));
            }
            
            if(tienePermiso) {
                String tipoInforme = "justificante";
                String codOrganizacion = infoConexion.getOrganizacion();
                params = es.altia.flexiaWS.tramitacion.bd.util.Configuracion.getParamsBD(codOrganizacion);
                
                // Se recupera el justificante de registro personalizado que esté activo, si lo hay
                justificanteActivo = JusticanteRegistro.getPlantillaActiva(params);
                if(justificanteActivo != null){
                    directorio = JusticanteRegistro.getRutaPlantillas(Integer.parseInt(codOrganizacion));
                    oficinaRegistro = JusticanteRegistro.getOficinaRegistro(datosAnotacion, params);
                    
                    //Generamos el xml con los datos que tendra el justificante
                    formatoFecha = JusticanteRegistro.getFormatoFecha(Integer.parseInt(codOrganizacion));
                    xml = JusticanteRegistro.generarXml(datosAnotacion, codOrganizacion, formatoFecha, params);
                    
                    //Obenemos el array de bytes correspondiente al justificante
                    IEjecutaJustificantePDF pdf = EjecutaJustificantePDFFactoria.getInstance().getImplClass(Integer.parseInt(codOrganizacion));
                    
                    try {
                        informe = pdf.generaJustificantePDF(directorio, justificanteActivo.getNombreJustificante(), oficinaRegistro, xml, true);
                        if(informe != null){
                            salida.setCodigoError(0);
                            salida.setIncidencias("OK");
                            salida.setFichero(informe);
                        } else {
                            throw new JustificanteRegistroException("Ha ocurrido un error al obtener el byte[] del justificante de registro");
                        }
                    } catch (JustificanteRegistroException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new JustificanteRegistroException("Ha ocurrido un error al obtener el byte[] del justificante de registro");
                    }
                }
            } else {
                String mensaje = "La aplicación no tiene permisos para acceder";
                salida.setCodigoError(3);
                salida.setIncidencias(mensaje);
                m_Log.error(mensaje);
            }
        } catch (TechnicalException te){
            String mensaje = String.format("Ha ocurrido un error al obtener la plantilla activa: %s", te.getMessage());
            salida.setCodigoError(4);
            salida.setIncidencias(mensaje);
            m_Log.error(mensaje);
            te.printStackTrace();
        } catch (BDException bde){
            String mensaje = String.format("Ha ocurrido un error al obtener los datos para generar el informe: %s", bde.getMessage());
            salida.setCodigoError(5);
            salida.setIncidencias(mensaje);
            m_Log.error(mensaje);
            bde.printStackTrace();
        } catch (AnotacionRegistroException are){
            String mensaje = String.format("Ha ocurrido un error al obtener el xml con los datos del justificante: %s", are.getMessage());
            salida.setCodigoError(6);
            salida.setIncidencias(mensaje);
            m_Log.error(mensaje);
            are.printStackTrace();
        } catch (JustificanteRegistroException jre){
            String mensaje = String.format("Ha ocurrido un error al obtener el array de bytes del justificante de registro activo: %s", jre.getMessage());
            salida.setCodigoError(7);
            salida.setIncidencias(mensaje);
            m_Log.error(mensaje);
            jre.printStackTrace();
        } catch (Exception e){String mensaje = String.format("Ha ocurrido un error en la operación: %s", e.getMessage());
            salida.setCodigoError(8);
            salida.setIncidencias(mensaje);
            m_Log.error(mensaje);
            e.printStackTrace();
        } 
        
        return salida;
    }
   
       private boolean comprobarAplicacion(String aplicaciones, String apli) {
        
        Vector<String> palabras = new Vector<String>();
        StringTokenizer tokenizer = new StringTokenizer(aplicaciones, ";");
        while (tokenizer.hasMoreTokens()) {
            palabras.add(tokenizer.nextToken());
        }
        return palabras.contains(apli);
        
    }
       
   private String obtenerHostLocal(HttpServletRequest req) throws Exception
  {
    try
    {
      String host="";
      if(req.getHeader("Host") != null)
      {
        host = req.getHeader("Host") + req.getContextPath();                    
        String protocolo = StrutsUtilOperations.getProtocol(req);                        
        if(host != null)
            host= protocolo + "://" + host + "/";
      }
      return host;
    }
    catch (Exception e)
    {
      throw e;
    }
  }

    /**
     * Obtiene el CSV a procesar y el toiken correspondiente, comprobando previamente
     * que se tiene las credenciales correctas
     * 
     * @param nombreDocumento
     * @param httpRequest
     * @param salida
     */
    private void obtenerCodigoCSV(String nombreDocumento, HttpServletRequest httpRequest,
            String codOrganizacion, SalidaCodigoCSV salida)
            throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        EstadoOperacionVO estado = salida.getEstado();
        
        try {
            DocumentoManager documentoManager = DocumentoManager.getInstance();
            SeguridadManager seguridadManager = SeguridadManager.getInstance();
            boolean operacionCorrecta = true;
            
            String[] params = es.altia.flexiaWS.tramitacion.bd.util.Configuracion.getParamsBD(codOrganizacion);
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);
            
            // Generar token de autenticacion
            AuthToken tokenAuth = null;
            if (operacionCorrecta) {
                tokenAuth = seguridadManager.getAuthTokenExternos(httpRequest, con, oad, params);

                if (tokenAuth == null || StringUtils.isEmpty(tokenAuth.getToken())) {
                    estado.setCodigoError(101);
                    estado.setIncidencias("Error al generar el token");
                    operacionCorrecta = false;
                }
            }

            // Generar CSV
            String CSV = null;
            if (operacionCorrecta) {
                CSV = documentoManager.crearCodigoSeguroVerificacion("", nombreDocumento);

                if (StringUtils.isEmpty(CSV)) {
                    estado.setCodigoError(102);
                    estado.setIncidencias("Error al generar el csv");
                    operacionCorrecta = false;
                }
            }

            // Grabar CSV en tabla pendiente de procesar
            if (operacionCorrecta) {
                CSVPendienteVO csvPendiente = new CSVPendienteVO();
                csvPendiente.setIdToken(tokenAuth.getId());
                csvPendiente.setCsv(CSV);
                documentoManager.insertarCsvPendienteProcesar(csvPendiente, con);
            }

            if (operacionCorrecta) {
                SigpGeneralOperations.commit(oad, con);
                
                salida.setCsv(CSV);
                salida.setToken(tokenAuth.getToken());
                estado.setCodigoError(0);
                estado.setIncidencias("");
                
                m_Log.debug("Obtenido CSV con exito");
            } else {
                m_Log.debug("No se ha podido obtener el CSV con exito");
                
                SigpGeneralOperations.rollBack(oad, con);
            }
        } catch (SecurityException se) {
            m_Log.error(se.getMessage());
            salida.setCsv("");
            salida.setToken("");
            estado.setIncidencias(se.getMessage());
            estado.setCodigoError(-1);
            
            SigpGeneralOperations.rollBack(oad, con);
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(String.format("No se pudo obtener el CSV: %s", e.getMessage()));
            SigpGeneralOperations.rollBack(oad, con);
            
            throw new TechnicalException("No se pudo obtener el CSV", e);
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }

    /**
     * Inserta un documento de registro en el almacen
     * 
     * @param token
     * @param documento
     * @param infoConexion
     * @param estado
     * @throws TechnicalException 
     */
    private void insertarDocumentoRegistro(
            String token, DocumentoRegistroVO documento,
            InfoConexionVO infoConexion, EstadoOperacionVO estado)
            throws TechnicalException {
        
        AdaptadorSQLBD oad = null;
        Connection con = null;
        
        try {
            DocumentoManager documentoManager = DocumentoManager.getInstance();
            String codOrganizacion = infoConexion.getOrganizacion();
            String[] params = es.altia.flexiaWS.tramitacion.bd.util.Configuracion.getParamsBD(codOrganizacion);
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);

            String csv = documento.getCodigoSeguroVerificacion();

            // Si contiene CSV, comprobamos si coinciden el token y el csv.
            // Si no tiene CSV insertamos el documento de forma normal 
            boolean verificacionToken = true;
            if (StringUtils.isNotEmpty(csv)) {
                verificacionToken = documentoManager.existeCsvPendienteProcesar(token, csv, con);
            }

            if (verificacionToken) {
                AnotacionRegistroManager anotacionRegistroManager = AnotacionRegistroManager.getInstance();
                byte[] fichero = Base64.decode(documento.getFichero());

                GeneralValueObject parametros = new GeneralValueObject();
                parametros.setAtributo("paramsBBDD", params);
                parametros.setAtributo("conexionBBDD", con);
                parametros.setAtributo("idOrganizacion", NumberUtils.createInteger(codOrganizacion));
                parametros.setAtributo("ejercicio", documento.getEjercicio());
                parametros.setAtributo("numero", documento.getNumeroAnotacion());
                parametros.setAtributo("codTip", documento.getTipoEntrada());
                parametros.setAtributo("codOur", documento.getCodUor());
                parametros.setAtributo("codDep", documento.getCodDepartamento());
                parametros.setAtributo("entregado", ConstantesDatos.NO);
                parametros.setAtributo("nombreDocumentoCSV", documento.getNombreDocumento());
                parametros.setAtributo("fechaDoc", documento.getFechaDocumento());
                parametros.setAtributo("tipoMimeCSV", documento.getTipoMime());
                parametros.setAtributo("extensionCSV", MimeTypes.guessExtensionFromMimeType(documento.getTipoMime()));
                parametros.setAtributo("fichero", fichero);
                parametros.setAtributo("rutaCompletaFichero", "");
                if (StringUtils.isNotEmpty(csv)) {
                    parametros.setAtributo("isExpHistorico", Boolean.FALSE);
                    String urlCsv = anotacionRegistroManager
                            .generarUriCsvDescargaFichero(parametros);

                    MetadatosDocumentoVO metadatos = new MetadatosDocumentoVO();
                    metadatos.setCsv(csv);
                    metadatos.setCsvAplicacion(infoConexion.getAplicacion());
                    metadatos.setCsvUri(urlCsv);

                    parametros.setAtributo("metadatosCSV", metadatos);
                }

                // Almacenamos el documento
                anotacionRegistroManager.grabarDocumentoCSV(parametros);

                // Borramos el csv pendiente de procesar y eliminamos el token
                if (StringUtils.isNotEmpty(csv)) {
                    documentoManager.eliminarCsvPendienteProcesarToken(token, csv, con);
                }
                
                SigpGeneralOperations.commit(oad, con);
            } else {
                estado.setIncidencias(ConstantesWS.DESC_STATUS_WS_ERROR_TOKEN_CSV_INCORRECTO);
                estado.setCodigoError(ConstantesWS.STATUS_WS_ERROR_TOKEN_CSV_INCORRECTO);
                m_Log.error(ConstantesWS.DESC_STATUS_WS_ERROR_TOKEN_CSV_INCORRECTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(String.format(
                    "Error al intentar almacenar el documento de registro: %s",
                    e.getMessage()));

            estado.setIncidencias(ConstantesWS.DESC_STATUS_WS_ERROR_INTERNO);
            estado.setCodigoError(ConstantesWS.STATUS_WS_ERROR_INTERNO);

            SigpGeneralOperations.rollBack(oad, con);
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }
    
    
}

