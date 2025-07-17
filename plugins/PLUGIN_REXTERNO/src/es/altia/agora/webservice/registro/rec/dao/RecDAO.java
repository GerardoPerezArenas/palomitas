/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.webservice.registro.rec.dao;

import es.altia.agora.business.registro.DocumentoValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.jdbc.GeneralOperations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author oscar.rodriguez
 */
public class RecDAO {
    private static RecDAO instance = null;
    private final String BARRA = "/";
    private final String DOT   = ".";
    private final String TIPO_ANOTACION_ENTRADA = "E";
    private final String ESTADO_ANOTACION       = "0";
    private final String COD_DEPARTAMENTO       = "2";    
    private Logger log = Logger.getLogger(RecDAO.class);
    private final int LONGITUD_MAXIMA_NUMANOTACIONREC = 6;

    private RecDAO(){}
    
    public static RecDAO getInstance(){
        if(instance==null)
            instance = new RecDAO();        
        return instance;
    }
    
    /**
     * Recupera los datos de una determinada anotación del REC
     * @param connection: Conexión a la bd
     * @param codigoUnidadRegistro: Código de la unidad de registro o código de la oficina de registro
     * @param numeroAsiento: Número de asiento o registro
     * @param ejercicioAsiento: ejercicio
     * @return TramitacionValueObject
     * @throws java.lang.Exception si ocurre un error grave
     */
    public TramitacionValueObject getAnotacionRec(Connection connection,String codigoUnidadRegistro,String numeroAsiento,String ejercicioAsiento) throws Exception
    {
        log.debug("getAnotacionRec");                
        String tipoAnotacion   = "E";        
        TramitacionValueObject to = new TramitacionValueObject();      
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            
            StringBuffer sb = new StringBuffer();
            
            if(numeroAsiento.length()<=6){            
               for(int i =0;i<LONGITUD_MAXIMA_NUMANOTACIONREC-numeroAsiento.length();i++){
                    sb.append("0");
                }                    
            }
            sb.append(numeroAsiento);
            //Config m_Config = ConfigServiceHelper.getConfig("common");
            ResourceBundle bundle = ResourceBundle.getBundle("es.altia.agora.webservice.registro.rec.configuracion.configuracion");
            codigoUnidadRegistro = bundle.getString("uor/REC/" + codigoUnidadRegistro);
            //codigoUnidadRegistro = m_Config.getString("uor/REC/" + codigoUnidadRegistro);
            log.debug("codigoUnidadRegistro: " +codigoUnidadRegistro);
            
            String busqNumeroAnotacion = ejercicioAsiento + codigoUnidadRegistro + sb.toString();
            String sql = "SELECT NUMEROANOTACION,FECHAANOTACION,INTERESADO,SOLICITA,FORMANOTIFICACION,PERSONA.TIPOPERSONA, " + 
                         "PERSONA.NOMBREPF,PERSONA.APELLIDO1PF,APELLIDO2PF,RAZONSOCIALPJ,UOR_SGE,DESCRIPCION_UOR_SGE,CODIGOPROCEDIMIENTO " + 
                         "FROM ANOTACION,PERSONA WHERE ANOTACION.INTERESADO=PERSONA.CODPERSONA " + 
                         "AND NUMEROANOTACION='" + busqNumeroAnotacion + "'";

            log.debug(sql);

            ps = connection.prepareStatement(sql);                
            rs = ps.executeQuery(sql);


            while(rs.next())
            {
                String numeroAnotacion   = rs.getString("NUMEROANOTACION");
                String fechaAnotacion    = DateOperations.timeStampToString(rs.getTimestamp("FECHAANOTACION"));
                String interesado        = rs.getString("INTERESADO");                    
                String formaNotificacion = rs.getString("FORMANOTIFICACION");
                String solicita          = rs.getString("SOLICITA");                    
                int tipoPersona          = rs.getInt("TIPOPERSONA");
                String nombre            = rs.getString("NOMBREPF");
                String apellido1         = rs.getString("APELLIDO1PF");
                String apellido2         = rs.getString("APELLIDO2PF");
                String razonSocial       = rs.getString("RAZONSOCIALPJ");
                int codigoUorSge         = rs.getInt("UOR_SGE");
                String descripcionUorSge = rs.getString("DESCRIPCION_UOR_SGE");
                String codProcedimiento  = rs.getString("CODIGOPROCEDIMIENTO");                           

              
                String ejercicio = numeroAnotacion.substring(0,4);
                String anotacion = numeroAnotacion.substring(6,numeroAnotacion.length());
                String codUnidadOrganica = numeroAnotacion.substring(4,6);

                String remitente = null;
                boolean isOrganismo = false;                
                //if(tipoPersona==0 || tipoPersona==1 && (nombre!=null && nombre.length()>0)){
                if(tipoPersona!=2){
                    remitente = nombre + " " + apellido1 + " " + apellido2;                    
                    to.setNombre(nombre);
                    to.setApellido1(apellido1);
                    to.setApellido2(apellido2);
                }
                else{
                    remitente = razonSocial;
                    to.setNombre("");
                    to.setApellido1("");
                    to.setApellido2("");
                    isOrganismo = true;
                }

                //TramitacionValueObject to = new TramitacionValueObject();      
                to.setEjercicio(ejercicio);
                to.setFechaAnotacion(fechaAnotacion);
                to.setCodDepartamento(COD_DEPARTAMENTO); //este parametro va a desaparecer de la base de datos
                //to.setEjerNum(ejercicio + BARRA + anotacion);
                to.setEjerNum(anotacion);
                to.setRemitente(remitente);                                        
                to.setTipoRegistro(TIPO_ANOTACION_ENTRADA);
                to.setCodUnidadRegistro(codUnidadOrganica);
                to.setEstado(ESTADO_ANOTACION);                
                to.setAsunto(AdaptadorSQLBD.js_escape(convertirMAYUS(solicita)));
                to.setCodigoUorSge(codigoUorSge);
                to.setDescripcionUorSge(descripcionUorSge);                
                
                to.setCodProcedimiento(codProcedimiento);
                
                
                if(isOrganismo){
                    log.debug("RecDAO interesado es un organismo");
                    to.setTitular("organismo");
                }
                else{
                    log.debug("RecDAO interesado no es un organismo");
                    to.setTitular("interesado");                
                }
            }                           
        }
        catch(SQLException e){
            e.printStackTrace();
            log.error("Error " + e.getMessage());
            throw new Exception(e.getMessage());
        }
        finally{
            GeneralOperations.closeStatement(ps);
            GeneralOperations.closeResultSet(rs);            
        }
        
        return to;        
    }
    
    /**
     * Recupera las anotaciones realizadas en la BBDD del REC
     * @param fechaDesde: Fecha desde la que se hace la búsqueda
     * @param fechaHasta: Fecha tope de búsqueda
     * @return Un Vector de TramitacionValueObject con los datos de las anotaciones recuperados
     */
    public Vector<TramitacionValueObject> getAnotacionesRec(Connection connection,String fechaDesde,String fechaHasta,String origen) throws Exception
    {
        log.debug("getAnotacionesRec");
        Vector asientos = new Vector();
                
        String tipoAnotacion = "E";
        String estadoAnotacion = "0";
                
        PreparedStatement ps = null;
        ResultSet rs = null;       
        
        try{            
            String sql = "SELECT NUMEROANOTACION,FECHAANOTACION,INTERESADO,SOLICITA,FORMANOTIFICACION,PERSONA.TIPOPERSONA, " + 
                         "PERSONA.NOMBREPF,PERSONA.APELLIDO1PF,APELLIDO2PF,RAZONSOCIALPJ,UOR_SGE,DESCRIPCION_UOR_SGE,CODIGOPROCEDIMIENTO " +
                         "FROM ANOTACION,PERSONA WHERE ANOTACION.INTERESADO=PERSONA.CODPERSONA AND ESTADO='0' " +   
                         "AND FECHAANOTACION BETWEEN NVL(TO_DATE('" + fechaDesde + " 00:00:00','DD/MM/YYYY HH24:MI:SS'),'') AND NVL(TO_DATE('"  + fechaHasta + " 23:59:59','DD/MM/YYYY HH24:MI:SS'),'') " +
                         "ORDER BY FECHAANOTACION DESC";            
            log.debug(sql);            
            ps = connection.prepareStatement(sql);            
            rs = ps.executeQuery(sql);


            //Config m_Config = ConfigServiceHelper.getConfig("common");
            ResourceBundle bundle = ResourceBundle.getBundle("es.altia.agora.webservice.registro.rec.configuracion.configuracion");
            while(rs.next())
            {
                String numeroAnotacion   = rs.getString("NUMEROANOTACION");
                String fechaAnotacion    = DateOperations.timeStampToString(rs.getTimestamp("FECHAANOTACION"));
                String interesado        = rs.getString("INTERESADO");                    
                String formaNotificacion = rs.getString("FORMANOTIFICACION");
                String solicita          = rs.getString("SOLICITA");                    
                int tipoPersona          = rs.getInt("TIPOPERSONA");                
                String nombre            = rs.getString("NOMBREPF");
                String apellido1         = rs.getString("APELLIDO1PF");
                String apellido2         = rs.getString("APELLIDO2PF");
                String razonSocial       = rs.getString("RAZONSOCIALPJ");
                int codUorSge            = rs.getInt("UOR_SGE");
                String descripcionUorSge = rs.getString("DESCRIPCION_UOR_SGE");
                String procedimiento     = rs.getString("CODIGOPROCEDIMIENTO");
                
                if(nombre==null || nombre.length()==0)
                    nombre="";
                
                if(apellido1==null || apellido1.length()==0)
                    apellido1="";
                
                if(apellido2==null || apellido2.length()==0)
                    apellido2="";
                
                if(razonSocial==null || razonSocial.length()==0)
                    razonSocial="";
                             
                
                String ejercicio = numeroAnotacion.substring(0,4);
                String anotacion = numeroAnotacion.substring(6,numeroAnotacion.length());
                String codUnidadOrganica = numeroAnotacion.substring(4,6);

                String remitente = null;
                if(tipoPersona==0 || tipoPersona==1 && (nombre!=null && nombre.length()>0)){                   
                    remitente = nombre + " " + apellido1 + " " + apellido2;
                }
                else
                    remitente = razonSocial;

                TramitacionValueObject to = new TramitacionValueObject();                    
                to.setFechaAnotacion(fechaAnotacion);
                to.setFechaDocumento(fechaAnotacion);
                to.setCodDepartamento(COD_DEPARTAMENTO); //este parametro va a desaparecer de la base de datos
                to.setEjerNum(ejercicio + BARRA + anotacion);
                to.setRemitente(remitente);                                        
                to.setTipoRegistro(TIPO_ANOTACION_ENTRADA);
                to.setCodigoUorSge(codUorSge);
                to.setDescripcionUorSge(descripcionUorSge);
                
                //String codUnidadOrganicaSGE = m_Config.getString("uor/REC/" + codUnidadOrganica);
                String codUnidadOrganicaSGE = bundle.getString("uor/REC/" + codUnidadOrganica);
                log.debug("RecDAO.java codUnidadOrganicaSGE: " + codUnidadOrganicaSGE);
                to.setCodUnidadRegistro(codUnidadOrganicaSGE);
                //to.setCodUnidadRegistro(codUnidadOrganica);                
                to.setEstado(ESTADO_ANOTACION);
                to.setOrigen(origen);
                to.setAsunto(AdaptadorSQLBD.js_escape(convertirMAYUS(solicita)));
                to.setProcedimiento(procedimiento);
                // Campos en vacío
                to.setNumExpediente("");
                to.setUsuarioAlta("");
                to.setNumTerceros("1");

                asientos.add(to);
            }                           
        }
        catch(SQLException e){
            e.printStackTrace();
            log.error("Error " + e.getMessage());
            throw new Exception(e.getMessage());
        }
        finally{
            GeneralOperations.closeStatement(ps);
            GeneralOperations.closeResultSet(rs);            
        }
        
        return asientos;        
    }  
    
    
     /**
     * Recupera los documentos de una determinada anotación
     * @param numeroAnotacion: Anotación de la que se recuperan documentos
     * @return Array de DocumentoValueObject
     */
    public DocumentoValueObject[] getDocumentosAnotacion(Connection connection,String numeroAnotacion) throws Exception{
        DocumentoValueObject[] documentos = null;
                
        ArrayList<DocumentoValueObject> docs = new ArrayList<DocumentoValueObject>();        
        Statement st = null;
        ResultSet rs = null;
        
        try{
                                      
            String sql = "SELECT NUMERODOCUMENTO,TITULO,EXTENSION,CONTENIDO " + 
                         "FROM DOCUMENTO WHERE NUMEROANOTACION='" + numeroAnotacion + "'";                             

            log.debug("sql: " + sql);
            st = connection.createStatement();                                
            rs = st.executeQuery(sql);

            while(rs.next())
            {
                DocumentoValueObject dvo = new DocumentoValueObject();
                String numero        = rs.getString("NUMERODOCUMENTO");                                        
                String titulo        = rs.getString("TITULO");                    
                String extension     = rs.getString("EXTENSION");

                log.debug("numero " + numero);
                log.debug("titulo " + titulo);
                log.debug("extension " + extension);                  
                dvo.setCodigo(numero);
                dvo.setNombre(titulo);
                dvo.setExtension(extension);
                docs.add(dvo);
            }
            
            DocumentoValueObject[] aux = new DocumentoValueObject[docs.size()];            
            return (DocumentoValueObject[])docs.toArray(aux);
        }
        catch(SQLException e){
            e.printStackTrace();
            log.error(" " + e.getMessage());
            throw new Exception(e.getMessage());
        }
        finally{
            GeneralOperations.closeStatement(st);
            GeneralOperations.closeResultSet(rs);            
        }    
    }


   /**
     * Recupera un determinado documento de una determinada anotación
     * @param connection: Conexión a la BBDD
     * @param numDocumento: Num documento a recuperar
     * @param numeroAnotacion: Anotación de la que se recuperan documentos
     * @return Array de DocumentoValueObject
     */
    public DocumentoValueObject getDocumentoAnotacion(Connection connection,int numDocumento,String numeroAnotacion) throws Exception{
        DocumentoValueObject[] documentos = null;

        log.debug("getDocumentoAnotacion");
        DocumentoValueObject dvo = new DocumentoValueObject();
        Statement st = null;
        ResultSet rs = null;

        try{
            log.debug("getDocumentoAnotacion params numDocumento: " + numDocumento + ", numeroAnotacion: " + numeroAnotacion);
            String sql = "SELECT NUMERODOCUMENTO,TITULO,EXTENSION,CONTENIDO " +
                         "FROM DOCUMENTO WHERE NUMERODOCUMENTO=" + numDocumento +" AND NUMEROANOTACION='" + numeroAnotacion + "'";

            st = connection.createStatement();
            rs = st.executeQuery(sql);
            log.debug("sql: " + sql);

            while(rs.next())
            {
                String numero        = rs.getString("NUMERODOCUMENTO");
                String titulo        = rs.getString("TITULO");
                String extension     = rs.getString("EXTENSION");
                byte[] contenido     = rs.getBytes("CONTENIDO");
                
                dvo.setCodigo(numero);
                dvo.setNombre(titulo);
                dvo.setExtension(extension);
                dvo.setFichero(contenido);
            }
            
            return dvo;
        }
        catch(SQLException e){
            e.printStackTrace();
            log.error("SQLException " + e.getMessage());
            throw new Exception(e.getMessage());
        }
        finally{
            GeneralOperations.closeStatement(st);
            GeneralOperations.closeResultSet(rs);
        }
    }
    
    
   /**
     * Cambia el estado de una anotación del REC
     * @param connection: conexión a la BBDD 
     * @param numeroAnotacion: Anotación
     * @param estadonotacion: Anotación
     * 
     * @param estado: Estado de la anotación
     * @param codUor: Código de la uor del SGE a la que se asocia la anotación del REC
     * @param descripcion: Nombre de la uor del SGe a la que se asocia la anotación del REC
    *  @throws Exception si ocurre algún error grave
     */
    public void cambiarEstadoAsiento(Connection connection,String numeroAnotacion,int estado,int codUor,String descripcion,String codProcedimiento) throws Exception{
        DocumentoValueObject[] documentos = null;
        
        log.debug("AccesoRegistroRec.cambiarEstadoAsiento parametros");                        
        ArrayList<DocumentoValueObject> docs = new ArrayList<DocumentoValueObject>();        
        Statement st = null;
        ResultSet rs = null;        
        try{                
            
            log.debug("cambiarEstadoAsiento numeroAnotacion: " + numeroAnotacion);
            log.debug("cambiarEstadoAsiento estado: " + estado);
            log.debug("cambiarEstadoAsiento descripcion: " + descripcion);
            
            String sql = "UPDATE ANOTACION SET ESTADO='" + estado + "',UOR_SGE='" + codUor +"',DESCRIPCION_UOR_SGE='" + descripcion + "',CODIGOPROCEDIMIENTO='" + codProcedimiento + "' WHERE NUMEROANOTACION='" + numeroAnotacion + "'";                             
            log.debug(sql);                
            st = connection.createStatement();                                
            int rowsUpdated = st.executeUpdate(sql);
            log.debug("Nº filas actualizadas anotacion: " + rowsUpdated);           
            
        }
        catch(SQLException e){
            e.printStackTrace();
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
        finally{
            GeneralOperations.closeStatement(st);
            GeneralOperations.closeResultSet(rs);            
        }    
    }
    
    /**
     * Recupera los datos de una anotación del REc
     * @param connection
     * @param RegistroValueObject en el que se guarda la información recuperada
     * @throws java.lang.Exception si ocurre algo grave
     */
    public void getInfoAsientoConsulta(Connection connection,RegistroValueObject registroVO) throws Exception{
        log.debug("AccesoRegistroRec.getInfoAsientoConsulta");
        Vector asientos = new Vector();
                
        String tipoAnotacion   = "E";
        String estadoAnotacion = "0";
                
        PreparedStatement ps = null;
        ResultSet rs = null;
                
        String periodo = String.valueOf(registroVO.getAnoReg());
        String numero = String.valueOf(registroVO.getNumReg());
        String tipo = String.valueOf(registroVO.getTipoReg());
        String uor = String.valueOf(registroVO.getUnidadOrgan());
        int organizacion = registroVO.getIdOrganizacion();
        
        StringBuffer sb = new StringBuffer();
        if(numero.length()<=6){            
            for(int i =0;i<LONGITUD_MAXIMA_NUMANOTACIONREC-numero.length();i++){
                sb.append("0");
            }            
        }
        sb.append(numero);
                
        log.debug("RecDAO.getInfoAsientoConsulta uor en origen: " + uor);        
        //Config config = ConfigServiceHelper.getConfig("common");
        ResourceBundle bundle = ResourceBundle.getBundle("es.altia.agora.webservice.registro.rec.configuracion.configuracion");
        log.debug("RecDAO.getInfoAsientoConsulta se recupera de config uor/REC/" + uor);        
        uor = bundle.getString("uor/REC/" + uor);
        //uor = config.getString("uor/REC/" + uor);
        log.debug("RecDAO.getInfoAsientoConsulta uor recuperada de configuración: " + uor);
                
        String numeroAnotacionRec = periodo + uor + sb.toString(); 
        log.debug("getInfoAsientoConsulta numeroAnotacionRec a buscar: " + numeroAnotacionRec);
        
        try{                            
            String sql = "SELECT NUMEROANOTACION,FECHAANOTACION,INTERESADO,SOLICITA,FORMANOTIFICACION,PERSONA.TIPOPERSONA, " + 
                         "TIPONOTIFICACION,PAISDP,PROVINCIADP,MUNICIPIODP,CALLEDP,CODIGOPOSTALDP,TELEFONODP,DIRECCIONCORREOCE,NUMEROFAXNF, " + 
                         "PERSONA.NOMBREPF,PERSONA.APELLIDO1PF,PERSONA.APELLIDO2PF,PERSONA.RAZONSOCIALPJ,PERSONA.DOCUMENTOPF,PERSONA.CIFPJ,UOR_SGE,DESCRIPCION_UOR_SGE,CODIGOPROCEDIMIENTO " + 
                         "FROM ANOTACION,PERSONA,FORMANOTIFICACION " + 
                         "WHERE ANOTACION.INTERESADO=PERSONA.CODPERSONA AND ANOTACION.FORMANOTIFICACION=FORMANOTIFICACION.CODNOTIFICACION " +   
                         "AND ANOTACION.NUMEROANOTACION='" + numeroAnotacionRec  + "'";
            
            log.debug(sql);

            ps = connection.prepareStatement(sql);                        
            rs = ps.executeQuery(sql);
            while(rs.next())
            {
                String numeroAnotacion   = rs.getString("NUMEROANOTACION");
                Timestamp marcaTiempo = rs.getTimestamp("FECHAANOTACION");
                String fechaAnotacion    = DateOperations.timeStampToString(marcaTiempo);
                String interesado        = rs.getString("INTERESADO");
                String formaNotificacion = rs.getString("FORMANOTIFICACION");
                String solicita          = rs.getString("SOLICITA");
                int tipoPersona          = rs.getInt("TIPOPERSONA");
                int tipoNotificacion     = rs.getInt("TIPONOTIFICACION");
                String pais              = rs.getString("PAISDP");
                String provinciadp       = rs.getString("PROVINCIADP");        
                String municipiodp       = rs.getString("MUNICIPIODP");        
                String calledp           = rs.getString("CALLEDP");            
                String codigopostal      = rs.getString("CODIGOPOSTALDP");     
                String telefono          = rs.getString("TELEFONODP");         
                String correo            = rs.getString("DIRECCIONCORREOCE");                 
                String fax               = rs.getString("NUMEROFAXNF");                 
                String nombre            = convertirMAYUS(rs.getString("NOMBREPF"));      
                String apellido1         = convertirMAYUS(rs.getString("APELLIDO1PF"));   
                String apellido2         = convertirMAYUS(rs.getString("APELLIDO2PF"));   
                String razonSocial       = convertirMAYUS(rs.getString("RAZONSOCIALPJ")); 
                String documento         = rs.getString("DOCUMENTOPF");
                String cif               = rs.getString("CIFPJ");
                int uorSge               = rs.getInt("UOR_SGE");
                String descripcionUorSGE = rs.getString("DESCRIPCION_UOR_SGE");
                String codigoProcedimiento= rs.getString("CODIGOPROCEDIMIENTO");
                                
                if(nombre==null || nombre.length()==0)
                    nombre = "";
                
                if(apellido1==null || apellido1.length()==0)
                    apellido1 = "";
                
                if(apellido2==null || apellido2.length()==0)
                    apellido2 = "";
                                
                if(razonSocial==null || razonSocial.length()==0)
                    razonSocial = "";
                
                if(tipoPersona==2){ // Organismo
                    registroVO.setNomCompletoInteresado(razonSocial);
                }
                else{
                     registroVO.setApellido1InteresadoExterno(apellido1);
                     registroVO.setApellido2InteresadoExterno(apellido2);
                     registroVO.setNombreInteresadoExterno(nombre);
                     registroVO.setNomCompletoInteresado(nombre + " " + apellido1 + " " + apellido2);
                }
                   
                
                
                if(codigoProcedimiento!=null && codigoProcedimiento.length()>0){
                    String[] cods = codigoProcedimiento.split("/");
                    registroVO.setNumExpediente(codigoProcedimiento);
                    if(cods!=null && cods.length==3){
                        registroVO.setCodProcedimiento(cods[1]);
                    }
                }
                else
                    registroVO.setNumExpediente("");                
                
                if(tipoNotificacion==0){ // Direccion postal
                    registroVO.setTlfInteresado(telefono);
                    registroVO.setCpInteresado(codigopostal);
                    registroVO.setProvInteresado(provinciadp);
                    registroVO.setMunInteresado(municipiodp);
                    registroVO.setDomCompletoInteresado(calledp);                    
                }                                
                
                if(tipoNotificacion==1){ // Correo
                    registroVO.setEmailInteresado(correo);
                }
                
                if(tipoNotificacion==2){ // Fax
                    registroVO.setTlfInteresado(fax);
                }
                
                //registroVO.setDocumentos(this.getDocumentosAnotacion(connection,numeroAnotacionRec));                
                registroVO.setDocumentos(new DocumentoValueObject[0]);
                registroVO.setListaTemasAsignados(new Vector());
                registroVO.setListaDocsAsignados(new Vector());
                
                ArrayList<DocumentoValueObject> listaDocs = new ArrayList<DocumentoValueObject>();                                                
                registroVO.setTipoAnot(3);
                
                // El documento del interesado es un cif => A PARTIR DE AHORA ES EL CÓDIGO MAP                
                if(cif!=null && cif.length()>0){                    
                    registroVO.setTipoDocInteresado("4"); // => tipoDOC para CÓDIGO MAP
                    registroVO.setDocumentoInteresado(cif);
                }
                                
                if(tipoPersona==1) // Persona jurídica
                {
                    registroVO.setTipoDocInteresado("4");
                    registroVO.setDocumentoInteresado(documento);
                }                              
                
                // El documento del interesado es un nif o nie y no es una persona jurídica                
                if(documento!=null && documento.length()>0 && tipoPersona!=1){                                                            
                    
                    // El documento es un NIE
                    if(validarNie(documento) && !validarNif(documento)){
                        registroVO.setTipoDocInteresado("3");
                        registroVO.setDocumentoInteresado(documento);                        
                    }
                    
                    if(!validarNie(documento) && validarNif(documento)){
                        registroVO.setTipoDocInteresado("1");
                        registroVO.setDocumentoInteresado(documento);
                    }   
                    else{ // Pasaporte                    
                        registroVO.setTipoDocInteresado("2");
                        registroVO.setDocumentoInteresado(documento);
                    } 
                }

                
                // No hay documento para el interesado
                if((documento==null || documento.length()==0) && (cif==null || cif.length()==0)){
                    registroVO.setTipoDocInteresado("0");
                    registroVO.setDocumentoInteresado("");
                }                
                
                registroVO.setFecEntrada(DateOperations.timeStampToString(marcaTiempo) + " " + DateOperations.extraerHoraTimeStamp(marcaTiempo));
                registroVO.setFecHoraDoc(DateOperations.timeStampToString(marcaTiempo) + " " + DateOperations.extraerHoraTimeStamp(marcaTiempo));
                registroVO.setAsunto(convertirMAYUS(solicita));
                registroVO.setObservaciones("RT:#" + numeroAnotacion);                                
                registroVO.setTipoDoc("0");
                registroVO.setTipoReg("1");
                registroVO.setTipoAnot(2);       
                // Se establece el tipo de transporte
                log.debug("Tipo transporte anotación: " + bundle.getString("tipo_transporte_anotacion_rec"));
                log.debug("Descripción tipo transporte: " + bundle.getString("descripcion_transporte_anotacion_rec"));
                registroVO.setCodTipoTransp(bundle.getString("tipo_transporte_anotacion_rec"));
                registroVO.setDescTipoTransporte(bundle.getString("descripcion_transporte_anotacion_rec"));
                // Se recuperan los documentos asociados a la anotación
                registroVO.setDocumentos(this.getDocumentosAnotacion(connection, numeroAnotacionRec));
                registroVO.setTipoRegOrigen("REC");
                // Lista temas 
                registroVO.setListaTemasAsignados(new Vector());
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            log.error("Error " + e.getMessage());
            throw new Exception(e.getMessage());
        }
        finally{
            GeneralOperations.closeStatement(ps);
            GeneralOperations.closeResultSet(rs);            
        } 
    }
    
    /**
     * Convierte a mayúsculas los caracteres de un String
     * @param String a convertir
     * @return String en mayusculas
     */
    private String convertirMAYUS(String valor) {
        if (("".equals(valor)) || (valor == null)) return valor;
        else return valor.toUpperCase();
    }     
    
    
     
   /** Validar un NIE/Tarjeta de residencia
    * @param nie
    * @return Un boolean */     
    private boolean validarNie(String nie) 
    {
        boolean exito = false;
        
        String[] codigos = {"T","R","W","A","G","M","Y","F","P","D","X","B","N","J","Z","S","Q","V","H","L","C","K","E"};
                        
        if(nie.length()==9) 
        {           
            String digitos = nie.substring(1,8);                        
            String letra = nie.substring(8).toUpperCase();
                                                
            if(digitos.length()>=7 && digitos.length()<=8 && isInteger(digitos)) {
                int cociente =Integer.parseInt(digitos);                            
                int codigo = cociente % 23;
                if(letra.equals(codigos[codigo]))
                    exito = true;                
            }
        }        
        return exito;                
    }// validarNie
    

    /**
     * Valida un nif
     * @param Nif
     * @return boolean
     */
    private boolean validarNif(String abc)
    {   
        boolean exito = true;
        
        String dni = abc.substring(0,abc.length()-1);        
        String let = abc.substring(abc.length()-1);
              
        String  cadena="TRWAGMYFPDXBNJZSQVHLCKET";
        int posicion = Integer.parseInt(dni) % 23;
       
        String letra = cadena.substring(posicion,posicion+1);
        
        if (!letra.equals(let.toUpperCase())){        
            exito = false;
        }
       
        return exito;
    }
    
       
    
    public boolean isInteger(String n){
        boolean exito = false;
        try{
            Integer.parseInt(n);
            exito = true;
        }
        catch(NumberFormatException e){           
        }
        return exito;
    }
}
