/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.historico.expedientes.dao;

import es.altia.agora.business.sge.OperacionExpedienteVO;
import es.altia.agora.business.sge.ValorCampoSuplementarioVO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.historico.expediente.vo.AdjuntoComunicacionVO;
import es.altia.flexia.historico.expediente.vo.AdjuntoExtNotificacionVO;
import es.altia.flexia.historico.expediente.vo.AdjuntoNotificacionVO;
import es.altia.flexia.historico.expediente.vo.AutorizadoNotificacionVO;
import es.altia.flexia.historico.expediente.vo.ComunicacionVO;
import es.altia.flexia.historico.expediente.vo.CronoVO;
import es.altia.flexia.historico.expediente.vo.DocExtVO;
import es.altia.flexia.historico.expediente.vo.DocsFirmasVO;
import es.altia.flexia.historico.expediente.vo.DocumentoPresentadoVO;
import es.altia.flexia.historico.expediente.vo.DocumentoTramitacionFirmaVO;
import es.altia.flexia.historico.expediente.vo.DocumentoTramitacionFirmantesVO;
import es.altia.flexia.historico.expediente.vo.DocumentoTramitacionFlujoVO;
import es.altia.flexia.historico.expediente.vo.DocumentoTramitacionVO;
import es.altia.flexia.historico.expediente.vo.ExpedienteVO;
import es.altia.flexia.historico.expediente.vo.InteresadoExpedienteVO;
import es.altia.flexia.historico.expediente.vo.ListTramOrigVO;
import es.altia.flexia.historico.expediente.vo.NotificacionVO;
import es.altia.flexia.historico.expediente.vo.RegistroRelacionadoExternoVO;
import es.altia.flexia.historico.expediente.vo.RegistroRelacionadoVO;
import es.altia.flexia.historico.expediente.vo.SituacionExpedienteAnuladoVO;
import java.sql.Blob;
import java.sql.Clob;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.jdbc.sqlbuilder.SqlBuilder;
import es.altia.util.jdbc.sqlbuilder.SqlExecuter;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author santiagoc
 */
public class ExpedienteDAO
{
    //Logger
    private static Logger log = Logger.getLogger(ExpedienteDAO.class);
    
    //Instancia
    private static ExpedienteDAO instance = null;
    private Config m_CommonProperties; // Para el fichero de contantes
    private Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
    private Config m_ConfigError; // Para los mensajes de error localizados
    
    private ExpedienteDAO()
    {   
        m_CommonProperties = ConfigServiceHelper.getConfig("common");
        // Queremos usar el	fichero de configuración technical
        m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError	= ConfigServiceHelper.getConfig("error");
    }
    
    public static ExpedienteDAO getInstance()
    {
        if(instance == null)
        {
            synchronized(ExpedienteDAO.class)
            {
                instance = new ExpedienteDAO();
            }
        }
        return instance;
    }   
    
    /**
     * Este método recupera la información que tiene un expediente determinado en la tabla E_EXP,
     * se leen todos los campos y se almacena en los atributos correspondiente de un objeto instancia de la clase
     * es.altia.flexia.historico.expediente.vo.ExpedienteVO
     * 
     * @param codOrganizacion
     * @param ejercicio
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public ExpedienteVO getExpediente(int codOrganizacion,int ejercicio, String numExpediente, 
            Connection con) throws Exception {
        log.debug("ExpedienteDAO.getExpediente()::BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        ExpedienteVO resultado = null;
        try{
            String sql = "SELECT EXP_PRO,EXP_EJE,EXP_NUM,EXP_FEI,EXP_FEF,EXP_EST,EXP_MUN,EXP_USU," +
                "EXP_UOR,EXP_PEND,EXP_TRA,EXP_TOCU,EXP_LOC,EXP_CLO,EXP_OBS,EXP_ASU,EXP_REF,EXP_IMP,EXP_UBICACION_DOC " + 
                    "FROM E_EXP WHERE EXP_MUN = ? AND EXP_EJE = ? AND EXP_NUM = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setInt(i++,ejercicio);
            ps.setString(i++,numExpediente);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                resultado = new ExpedienteVO();
                resultado.setCodProcedimiento(rs.getString("EXP_PRO"));
                resultado.setEjercicio(rs.getInt("EXP_EJE"));
                resultado.setNumExpediente(rs.getString("EXP_NUM"));
                resultado.setFechaInicio(DateOperations.timestampToCalendar(rs.getTimestamp("EXP_FEI")));
                resultado.setFechaFin(DateOperations.timestampToCalendar(rs.getTimestamp("EXP_FEF")));
                resultado.setEstado(rs.getInt("EXP_EST"));
                resultado.setCodOrganizacion(rs.getInt("EXP_MUN"));
                resultado.setCodUsuario(rs.getInt("EXP_USU"));
                resultado.setCodUorInicio(rs.getInt("EXP_UOR"));
                resultado.setFechaPendiente(DateOperations.timestampToCalendar(rs.getTimestamp("EXP_PEND")));
                resultado.setCodTramitePendiente(rs.getInt("EXP_TRA"));
                resultado.setOcurrenciaTramitePendiente(rs.getInt("EXP_TOCU"));
                resultado.setLocalizacion(rs.getString("EXP_LOC"));
                resultado.setCodLocalizacion(rs.getInt("EXP_CLO"));
                resultado.setObservaciones(rs.getString("EXP_OBS"));
                resultado.setAsunto(rs.getString("EXP_ASU"));
                resultado.setReferencia(rs.getString("EXP_REF"));
                resultado.setImportante(rs.getString("EXP_IMP"));
                resultado.setUbicacionDocumentacion(rs.getString("EXP_UBICACION_DOC"));
            }
        }catch(SQLException e){
            log.error("Error al recuperar el expediente de la tabla de expedientes activos: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    /**
     * Recupera los trámites de un expediente, recuperando la información de la tabla E_CRO.
     * Usa los parámetros codOrganizacion, ejercicio, codProcedimient y numExpediente para filtrar por la consultar
     * 
     * @param codOrganizacion
     * @param ejercicio
     * @param codProcedimiento
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<CronoVO> getTramitesExpediente(int codOrganizacion,int ejercicio,
            String codProcedimiento,String numExpediente, Connection con) throws Exception {
        log.debug("ExpedienteDAO.getTramitesExpediente()::BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<CronoVO> resultado = new ArrayList<CronoVO>();
        
        String sql = "SELECT CRO_PRO,CRO_TRA,CRO_FEI,CRO_FEF,CRO_USU," + 
                "CRO_UTR,CRO_MUN,CRO_OCU,CRO_FIP,CRO_FLI,CRO_FFP,CRO_RES,CRO_OBS,CRO_USF," + 
                "CRO_AVISADOCFP,CRO_AVISADOFDP " + 
                "FROM E_CRO WHERE CRO_MUN = ? AND CRO_NUM = ? AND CRO_EJE = ? AND CRO_PRO=?";
        log.debug(" (TRAZA DE CONTROL) SQL: " + sql);  
        try{
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            ps.setInt(i++,ejercicio);
            ps.setString(i++,codProcedimiento);
            
            rs = ps.executeQuery();            
            while (rs.next()){
                CronoVO crono = new CronoVO();
                crono.setCodProcedimiento(rs.getString("CRO_PRO"));
                crono.setEjercicio(ejercicio);
                crono.setNumExpediente(numExpediente);
                crono.setCodTramite(rs.getInt("CRO_TRA"));
                crono.setFechaInicio(DateOperations.timestampToCalendar(rs.getTimestamp("CRO_FEI")));
                crono.setFechaFin(DateOperations.timestampToCalendar(rs.getTimestamp("CRO_FEF")));
                crono.setCodUsuario(rs.getInt("CRO_USU"));
                crono.setCodUorTramitadora(rs.getInt("CRO_UTR"));
                crono.setCodOrganizacion(codOrganizacion);
                crono.setOcurrenciaTramite(rs.getInt("CRO_OCU"));
                crono.setFechaInicioPlazo(DateOperations.timestampToCalendar(rs.getTimestamp("CRO_FIP")));
                crono.setFechaLimite(DateOperations.timestampToCalendar(rs.getTimestamp("CRO_FLI")));
                crono.setFechaFinPlazo(DateOperations.timestampToCalendar(rs.getTimestamp("CRO_FFP")));
                crono.setReserva(rs.getInt("CRO_RES"));
                crono.setObservaciones(rs.getString("CRO_OBS"));
                crono.setUsuarioFinalizacion(rs.getInt("CRO_USF"));
                crono.setAvisoCercanaFinPlazo(rs.getInt("CRO_AVISADOCFP"));
                crono.setAvisoCercanaFinPlazo(rs.getInt("CRO_AVISADOFDP"));
                
                resultado.add(crono);
            }
        }catch(SQLException e){
            log.error("Error al recuperar el expediente de la tabla de trámites de expedientes activos: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(rs!=null) rs.close();                
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    /**
     * Recupera la lista de trámites de origen de un expediente, recuperando la información de la tabla LIST_TRAM_ORIG.
     * Usa los parámetros codOrganizacion, ejercicio, codProcedimientO y numExpediente para filtrar por la consultar.
     * 
     * @param codOrganizacion
     * @param ejercicio
     * @param codProcedimiento
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<ListTramOrigVO> getListaTramitesOrigen(int codOrganizacion,int ejercicio,String codProcedimiento,String numExpediente, Connection con) throws Exception
    {
        log.debug("ExpedienteDAO.getListaTramitesOrigen()::BEGIN");
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql_tabla_listTramOrig = m_ConfigTechnical.getString("SQL.LIST_TRAM_ORIG");
            
            String sql_ejercicio = m_ConfigTechnical.getString("SQL.LIST_TRAM_ORIG.ejercicio");
            String sql_codPro = m_ConfigTechnical.getString("SQL.LIST_TRAM_ORIG.codPro");
            String sql_codMun = m_ConfigTechnical.getString("SQL.LIST_TRAM_ORIG.codMun");
            String sql_numExp = m_ConfigTechnical.getString("SQL.LIST_TRAM_ORIG.numExp");
            String sql_codTraOrigen = m_ConfigTechnical.getString("SQL.LIST_TRAM_ORIG.codTraOrigen");
            String sql_ocuTraOrigen = m_ConfigTechnical.getString("SQL.LIST_TRAM_ORIG.ocuTraOrigen");
            String sql_codTraDestino = m_ConfigTechnical.getString("SQL.LIST_TRAM_ORIG.codTraDestino");
            String sql_ocuTraDestino = m_ConfigTechnical.getString("SQL.LIST_TRAM_ORIG.ocuTraDestino");
            
            
            String query = "select * from "+sql_tabla_listTramOrig+" where "+sql_codMun+" = "+codOrganizacion+" and "+sql_ejercicio+" = "+ejercicio+" and "+sql_codPro+" = '"+codProcedimiento+"' and "+sql_numExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ListTramOrigVO lt = null;
            ArrayList<ListTramOrigVO> retList = new ArrayList<ListTramOrigVO>();
            
            while(rs.next())
            {
                lt = new ListTramOrigVO();
                lt.setCodMunicipio(rs.getInt(sql_codMun));
                lt.setCodProcedimiento(rs.getString(sql_codPro));
                lt.setCodTramiteDestino(rs.getInt(sql_codTraDestino));
                lt.setCodTramiteOrigen(rs.getInt(sql_codTraOrigen));
                lt.setEjercicio(rs.getInt(sql_ejercicio));
                lt.setNumExpediente(rs.getString(sql_numExp));
                lt.setOcurrenciaTramiteDestino(rs.getInt(sql_ocuTraDestino));
                lt.setOcurrenciaTramiteOrigen(rs.getInt(sql_ocuTraOrigen));
                retList.add(lt);
            }
            return retList;
        }
        catch(Exception ex) {
            ex.printStackTrace();
            log.error("Se ha producido un erro en el metodo expedienteDAO.getListaTramitesOrigen: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }
    }
    
    /**
     * Recupera la lista de trámites de origen de un expediente, recuperando la información de la tabla E_EXT.
     * Usa los parámetros codOrganizacion, ejercicio, codProcedimiento y numExpediente para filtrar por la consultar.
     * 
     * @param codOrganizacion
     * @param ejercicio
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<InteresadoExpedienteVO> getInteresadosExpediente(int codOrganizacion, int  ejercicio, String numExpediente, Connection con) throws Exception
    {
        log.debug("ExpedienteDAO.getInteresadosExpediente()::BEGIN");
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql_tabla_eExt = m_ConfigTechnical.getString("SQL.E_EXT");
            
            String sql_extMun = m_ConfigTechnical.getString("SQL.E_EXT.extMun");
            String sql_extEje = m_ConfigTechnical.getString("SQL.E_EXT.extEje");
            String sql_extNum = m_ConfigTechnical.getString("SQL.E_EXT.extNum");
            String sql_extTer = m_ConfigTechnical.getString("SQL.E_EXT.extTer");
            String sql_extNvr = m_ConfigTechnical.getString("SQL.E_EXT.extNvr");
            String sql_extDot = m_ConfigTechnical.getString("SQL.E_EXT.extDot");
            String sql_extRol = m_ConfigTechnical.getString("SQL.E_EXT.extRol");
            String sql_extPro = m_ConfigTechnical.getString("SQL.E_EXT.extPro");
            String sql_mostrar = m_ConfigTechnical.getString("SQL.E_EXT.mostrar");
            String sql_extNotificacionElectronica = m_ConfigTechnical.getString("SQL.E_EXT.extNotificacionElectronica");
            
            
            String query = "select * from "+sql_tabla_eExt+" where "+sql_extMun+" = "+codOrganizacion+" and "+sql_extEje+" = "+ejercicio+" and "+sql_extNum+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ArrayList<InteresadoExpedienteVO> retList = new ArrayList<InteresadoExpedienteVO>();
            InteresadoExpedienteVO ie = null;
            
            while(rs.next())
            {
                ie = new InteresadoExpedienteVO();
                ie.setCodDomicilio(rs.getInt(sql_extDot));
                ie.setCodMunicipio(rs.getInt(sql_extMun));
                ie.setCodProcedimiento(rs.getString(sql_extPro));
                ie.setCodRol(rs.getInt(sql_extRol));
                ie.setCodTercero(rs.getInt(sql_extTer));
                ie.setEjercicio(rs.getInt(sql_extEje));
                ie.setMostrar(rs.getInt(sql_mostrar));
                ie.setNotificacionElectronica(rs.getString(sql_extNotificacionElectronica));
                ie.setNumExpediente(rs.getString(sql_extNum));
                ie.setVersionTercero(rs.getInt(sql_extNvr));
                retList.add(ie);
            }       
            return retList;
        }
        catch(Exception ex) {
            ex.printStackTrace();
            log.error("Se ha producido un error en el metodo expedienteDAO.getInteresadosExpediente: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }
    }
    
    /**
     * Recupera la lista de documentos de inicio asociados al expediente, recuperando la información de la tabla E_DOCS_PRESENTADOS.
     * No se leerá el contenido del campo E_DOCS_PRESENTADOS.PRESENTADO_CONTENIDO para evitar cargarlo en memoria
     * ya que puede haber un número bastante elevado de archivos, y que éstos sean de gran tamaño.
     * Se leerá el contenido cuando haga falta, para poder moverlo de una tabla a otra.
     * 
     * @param codOrganizacion
     * @param ejercicio
     * @param codProcedimiento
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<DocumentoPresentadoVO> getListaDocumentosPresentados(int codOrganizacion,int  ejercicio, String codProcedimiento, String numExpediente, Connection con) throws Exception
    {
        log.debug("ExpedienteDAO.getListaDocumentosPresentados()::BEGIN");
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql_tabla_eDocsPresentados = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS");
            
            String sql_presentadoCod = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoCod");
            String sql_presentadoMun = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoMun");
            String sql_presentadoEje = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoEje");
            String sql_presentadoNum = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoNum");
            String sql_presentadoPro = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoPro");
            String sql_presentadoCodDoc = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoCodDoc");
            String sql_presentadoTipo = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoTipo");
            String sql_presentadoExtension = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoExtension");
            String sql_presentadoOrigen = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoOrigen");
            String sql_presentadoFechaAlta = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoFechaAlta");
            String sql_presentadoNombre = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoNombre");
            String sql_presentadoCodUsuAlta = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoCodUsuAlta");
            String sql_presentadoCodUsuMod = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoCodUsuMod");
            String sql_presentadoFechaMod = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoFechaMod");
                       
            String query = "select"
                          +" "+sql_presentadoCod
                          +", "+sql_presentadoMun
                          +", "+sql_presentadoEje
                          +", "+sql_presentadoNum
                          +", "+sql_presentadoPro
                          +", "+sql_presentadoCodDoc
                          +", "+sql_presentadoTipo
                          +", "+sql_presentadoExtension
                          +", "+sql_presentadoOrigen
                          +", "+sql_presentadoFechaAlta
                          +", "+sql_presentadoNombre
                          +", "+sql_presentadoCodUsuAlta
                          +", "+sql_presentadoCodUsuMod
                          +", "+sql_presentadoFechaMod
                          +" from "+sql_tabla_eDocsPresentados+" where "+sql_presentadoMun+" = "+codOrganizacion+" and "+sql_presentadoEje+" = "+ejercicio+" and "+sql_presentadoPro+" = '"+codProcedimiento+"' and "+sql_presentadoNum+" = '"+numExpediente+"'";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
                        
            ArrayList<DocumentoPresentadoVO> retList = new ArrayList<DocumentoPresentadoVO>();
            DocumentoPresentadoVO d = null;
            int valorInt;
            java.sql.Timestamp valorDate = null;
            GregorianCalendar cal = null;
            
            while(rs.next()) {
                d = new DocumentoPresentadoVO();
                d.setCodMunicipio(rs.getInt(sql_presentadoMun));
                d.setCodPresentado(rs.getInt(sql_presentadoCod));
                d.setCodProcedimiento(rs.getString(sql_presentadoPro));
                d.setCodDocumentoPresentado(rs.getInt(sql_presentadoCodDoc));
                d.setCodUsuarioAlta(rs.getInt(sql_presentadoCodUsuAlta));
                d.setCodUsuarioModificacion(rs.getInt(sql_presentadoCodUsuMod));
                d.setEjercicio(rs.getInt(sql_presentadoEje));
                d.setExtension(rs.getString(sql_presentadoExtension));
                valorDate = rs.getTimestamp(sql_presentadoFechaAlta);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    d.setFechaAlta(cal);
                }
                valorDate = rs.getTimestamp(sql_presentadoFechaMod);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    d.setFechaModificacion(cal);
                }
                d.setNombreDocumento(rs.getString(sql_presentadoNombre));
                d.setNumExpediente(rs.getString(sql_presentadoNum));
                d.setOrigen(rs.getString(sql_presentadoOrigen));
                d.setTipoMime(rs.getString(sql_presentadoTipo));
				// Por cada documento presentado, se recupera además la lista de firmas que cada uno pueda tener.
                d.setFirmas(this.getFirmasDocumentoPresentado(d.getCodPresentado(), con));
                
                retList.add(d);
            }
            return retList;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un erro en el metodo expedienteDAO.getListaDocumentosPresentados: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally {
            try{
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Recupera el contenido binario del campo E_DOCS_PRESENTADOS.PRESENTADO_CONTENIDO de un determinado documento presentado.
     * Puede darse el caso de que el documento binario no esté alojado en base de datos, y que haya sido enviado a un gestor documental.
     * En este caso, devolverá un null.
     * 
     * @param con
     * @param codPresentado
     * @return
     * @throws Exception 
     */
    public byte[] getContenidoDocumentoPresentado(int codPresentado, Connection con) throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select PRESENTADO_CONTENIDO from E_DOCS_PRESENTADOS where PRESENTADO_COD = ?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            ps= con.prepareStatement(query);
            
            int i = 1;
            
            ps.setInt(i++,codPresentado);
            
            rs = ps.executeQuery();
            
            if(rs.next()){
                return rs.getBytes("PRESENTADO_CONTENIDO");
            } else {
                return null;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un erro en el metodo expedienteDAO.getContenidoDocumentoPresentado: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(ps != null)
                ps.close();
        }
    }
    
    /**
     * Recupera la lista de firmas de un determinado documento de inicio de expediente, si es que ha sido firmado alguna vez.
     * Cada firma se corresponde con una de las firmas en el circuito de firmas.
     * El parámetro codDocumentoPresentado, se utiliza para filtrar la búsqueda por el campo E_DOCS_FIRMAS.ID_DOC_PRESENTADO.
     * No se leerá el contenido del campo E_DOCS_FIRMAS.FIRMA para evitar cargarlo en memoria
     * ya que puede haber un número bastante elevado de archivos, y que éstos sean de gran tamaño.
     * Se leerá el contenido cuando haga falta, para poder moverlo de una tabla a otra.
     * 
     * @param codDocumentoPresentado
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<DocsFirmasVO> getFirmasDocumentoPresentado(int codDocumentoPresentado, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql_tabla_eDocsFirmas = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS");
            
            String sql_idDocFirma = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS.idDocFirma");
            String sql_docFirmaEstado = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS.docFirmaEstado");
            String sql_docFirmaOrden = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS.docFirmaOrden");
            String sql_docFirmaUor = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS.docFirmaUor");
            String sql_docFirmaCargo = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS.docFirmaCargo");
            String sql_docFirmaUsuario = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS.docFirmaUsuario");
            String sql_docFirmaFecha = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS.docFirmaFecha");
            String sql_idDocPresentado = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS.idDocPresentado");
            String sql_docFechaEnvio = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS.docFechaEnvio");
            //String sql_firma = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS.firma");
            String sql_docFirmaObservaciones = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS.docFirmaObservaciones");
            
            String query = "select"
                          +" "+sql_idDocFirma
                          +", "+sql_docFirmaEstado
                          +", "+sql_docFirmaOrden
                          +", "+sql_docFirmaUor
                          +", "+sql_docFirmaCargo
                          +", "+sql_docFirmaUsuario
                          +", "+sql_docFirmaFecha
                          +", "+sql_idDocPresentado
                          +", "+sql_docFechaEnvio
                          +", "+sql_docFirmaObservaciones
                          +" from "+sql_tabla_eDocsFirmas+" where "+sql_idDocPresentado+" = "+codDocumentoPresentado;
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ArrayList<DocsFirmasVO> retList = new ArrayList<DocsFirmasVO>();
            DocsFirmasVO firma = null;
            int valorInt;
            java.sql.Timestamp valorDate = null;
            GregorianCalendar cal = null;
            
            while(rs.next())
            {
                firma = new DocsFirmasVO();
                valorDate = rs.getTimestamp(sql_docFechaEnvio);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    firma.setDocFechaEnvio(cal);
                }
                valorInt = rs.getInt(sql_docFirmaCargo);
                if(!rs.wasNull())
                    firma.setDocFirmaCargo(valorInt);
                firma.setDocFirmaEstado(rs.getString(sql_docFirmaEstado));
                valorDate = rs.getTimestamp(sql_docFirmaFecha);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    firma.setDocFirmaFecha(cal);
                }
                firma.setDocFirmaOrden(rs.getInt(sql_docFirmaOrden));
                valorInt = rs.getInt(sql_docFirmaUor);
                if(!rs.wasNull())
                    firma.setDocFirmaUor(valorInt);
                valorInt = rs.getInt(sql_docFirmaUsuario);
                if(!rs.wasNull())
                    firma.setDocFirmaUsuario(valorInt);
                firma.setIdDocFirma(rs.getInt(sql_idDocFirma));
                firma.setIdDocPresentado(rs.getInt(sql_idDocPresentado));
                firma.setObservaciones(rs.getString(sql_docFirmaObservaciones));
                retList.add(firma);
            }
            return retList;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un erro en el metodo expedienteDAO.getFirmasDocumentoPresentado: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }
    }
    
    /**
     * Recupera el contenido binario de una firma de un documento, siempre y cuando esta haya sido alojada en base de datos,
     * y no en un gestor documental. Se lee del campo E_DOCS_FIRMA.FIRMA. Si la firma no está en base de datos, se devuelve un null.
     * 
     * @param codDocumentoPresentado
     * @param idDocFirma
     * @param con
     * @return
     * @throws Exception 
     */
    public byte[] getContenidoFirmaDocumentoPresentado(int codDocumentoPresentado, int idDocFirma, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql_tabla_eDocsFirmas = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS");
            
            String sql_idDocFirma = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS.idDocFirma");
            String sql_idDocPresentado = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS.idDocPresentado");
            String sql_firma = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS.firma");
            
            String query = "select "+sql_firma+" from "+sql_tabla_eDocsFirmas+" where "+sql_idDocPresentado+" = "+codDocumentoPresentado+" and "+sql_idDocFirma+" = "+idDocFirma;
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            if(rs.next())
            {
                return rs.getBytes(sql_firma);
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un erro en el metodo expedienteDAO.getContenidoFirmaDocumentoPresentado", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }
    }
    
    /**
     * Recupera la lista de documentos externos que están asociados a un determinado expediente.
     * Los 3 primeros parámetros se usan para filtrar la consulta, que se hace contra la tabla E_DOC_EXT.
     * No se recupera el contenido binario del documento del campo E_DOC_EXT.DOC_EXT_FIL,
     * ya que sólo leerá cuando sea necesario y se haya que grabarlo en base de datos
     * 
     * @param codOrganizacion
     * @param ejercicio
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<DocExtVO> getListaDocumentosExternos(int codOrganizacion, int ejercicio, String numExpediente, Connection con) throws Exception
    {
        log.debug("ExpedienteDAO.getListaDocumentosExternos()::BEGIN");
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql_tabla_eDocExt = m_ConfigTechnical.getString("SQL.E_DOC_EXT");
            
            String sql_docExtNum = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtNum");
            String sql_docExtMun = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtMun");
            String sql_docExtEje = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtEje");
            String sql_docExtCod = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtCod");
            String sql_docExtNom = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtNom");
            String sql_docExtFal = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtFal");
            String sql_docExtTip = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtTip");
            String sql_docExtExt = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtExt");
            String sql_docExtOrigen = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtOrigen");
            
            String query = "select"
                          +" "+sql_docExtNum
                          +", "+sql_docExtMun
                          +", "+sql_docExtEje
                          +", "+sql_docExtCod
                          +", "+sql_docExtNom
                          +", "+sql_docExtFal
                          +", "+sql_docExtTip
                          +", "+sql_docExtExt
                          +", "+sql_docExtOrigen
                          +" from "+sql_tabla_eDocExt+" where "+sql_docExtMun+" = "+codOrganizacion+" and "+sql_docExtEje+" = "+ejercicio+" and "+sql_docExtNum+" = '"+numExpediente+"'";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ArrayList<DocExtVO> retList = new ArrayList<DocExtVO>();
            DocExtVO d = null;
            java.sql.Timestamp valorDate = null;
            GregorianCalendar cal = null;
            
            while(rs.next())
            {
                d = new DocExtVO();
                d.setCodDocumento(rs.getInt(sql_docExtCod));
                d.setCodMunicipio(rs.getInt(sql_docExtMun));
                d.setEjercicio(rs.getInt(sql_docExtEje));
                d.setExtension(rs.getString(sql_docExtExt));
                valorDate = rs.getTimestamp(sql_docExtFal);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    d.setFechaDocumento(cal);
                }
                d.setNombreDocumento(rs.getString(sql_docExtNom));
                d.setNumExpediente(rs.getString(sql_docExtNum));
                d.setOrigen(rs.getString(sql_docExtOrigen));
                d.setTipoDocumento(rs.getString(sql_docExtTip));
                retList.add(d);
            }
            
            return retList;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un erro en el metodo expedienteDAO.getListaDocumentosExternos: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }
    }
    
    /**
     * Recupera el contenido binario de un determinado documento externo.
     * Se lee del campo E_DOC_EXT.DOC_EXT_FIL. Sino existe el documento devuelve un null.
     * 
     * @param codOrganizacion
     * @param ejercicio
     * @param numExpediente
     * @param codDocumentoExterno
     * @param con
     * @return
     * @throws Exception 
     */
    public byte[] getContenidoDocumentoExterno(int codOrganizacion, int ejercicio, String numExpediente, int codDocumentoExterno, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql_tabla_eDocExt = m_ConfigTechnical.getString("SQL.E_DOC_EXT");
            
            String sql_docExtNum = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtNum");
            String sql_docExtMun = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtMun");
            String sql_docExtEje = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtEje");
            String sql_docExtCod = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtCod");
            String sql_docExtFil = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtFil");
            
            String query = "select"
                          +" "+sql_docExtFil
                          +" from "+sql_tabla_eDocExt+" where "+sql_docExtMun+" = "+codOrganizacion+" and "+sql_docExtEje+" = "+ejercicio+" and "+sql_docExtNum+" = '"+numExpediente+"' and "+sql_docExtCod+" = "+codDocumentoExterno;
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            if(rs.next())
            {
                return rs.getBytes(sql_docExtFil);
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un erro en el metodo expedienteDAO.getContenidoDocumentoExterno: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }
    }
    
    
    /**
     * Recupera la lista de notificaciones asociadas al expediente.
     * Los 4 primeros parámetros se usan para filtrar la consulta, que se hace contra la tabla NOTIFICACION.
     * No se recupera, por cada notificación, la lista de autorizados, su lista de adjuntos ni su lista de adjuntos externos
     * ya que se implementan métodos específicos para ello
     * 
     * @param codOrganizacion
     * @param ejercicio
     * @param numExpediente
     * @param codProcedimiento
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<NotificacionVO> getListaNotificaciones(int codOrganizacion, int ejercicio, String numExpediente, String codProcedimiento,Connection con) throws Exception
    {
        log.debug("ExpedienteDAO.getListaNotificaciones()::BEGIN");
        Statement st = null;
        ResultSet rs = null;
        try
        {   
            String sql_tabla_notificacion = m_ConfigTechnical.getString("SQL.NOTIFICACION");
            
            String sql_codigoNotificacion = m_ConfigTechnical.getString("SQL.NOTIFICACION.codigoNotificacion");
            String sql_numExpediente = m_ConfigTechnical.getString("SQL.NOTIFICACION.numExpediente");
            String sql_codProcedimiento = m_ConfigTechnical.getString("SQL.NOTIFICACION.codProcedimiento");
            String sql_ejercicio = m_ConfigTechnical.getString("SQL.NOTIFICACION.ejercicio");
            String sql_codMunicipio = m_ConfigTechnical.getString("SQL.NOTIFICACION.codMunicipio");
            String sql_codTramite = m_ConfigTechnical.getString("SQL.NOTIFICACION.codTramite");
            String sql_ocuTramite = m_ConfigTechnical.getString("SQL.NOTIFICACION.ocuTramite");
            String sql_actoNotificado = m_ConfigTechnical.getString("SQL.NOTIFICACION.actoNotificado");
            String sql_caducidadNotificacion = m_ConfigTechnical.getString("SQL.NOTIFICACION.caducidadNotificacion");
            String sql_firma = m_ConfigTechnical.getString("SQL.NOTIFICACION.firma");
            String sql_textoNotificacion = m_ConfigTechnical.getString("SQL.NOTIFICACION.textoNotificacion");
            String sql_firmada = m_ConfigTechnical.getString("SQL.NOTIFICACION.firmada");
            String sql_xmlNotificacion = m_ConfigTechnical.getString("SQL.NOTIFICACION.xmlNotificacion");
            String sql_fechaEnvio = m_ConfigTechnical.getString("SQL.NOTIFICACION.fechaEnvio");
            String sql_registroRt = m_ConfigTechnical.getString("SQL.NOTIFICACION.registroRt");
            String sql_fechaAcuse = m_ConfigTechnical.getString("SQL.NOTIFICACION.fechaAcuse");
            String sql_resultado = m_ConfigTechnical.getString("SQL.NOTIFICACION.resultado");
            
            String query = "select * from "+sql_tabla_notificacion+" where "+sql_codMunicipio+" = "+codOrganizacion+" and "+sql_ejercicio+" = "+ejercicio+" and "+sql_numExpediente+" = '"+numExpediente+"' and "+sql_codProcedimiento+" = '"+codProcedimiento+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);        
            
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ArrayList<NotificacionVO> retList = new ArrayList<NotificacionVO>();
            NotificacionVO n = null;
            int valorInt;
            java.sql.Timestamp valorDate = null;
            GregorianCalendar cal = null;
            Blob blob = null;
            
            while(rs.next())
            {
                n = new NotificacionVO();
                n.setActoNotificado(rs.getString(sql_actoNotificado));
                valorInt = rs.getInt(sql_caducidadNotificacion);
                if(!rs.wasNull())
                    n.setCaducidadNotificacion(valorInt);
                n.setCodMunicipio(rs.getInt(sql_codMunicipio));
                n.setCodNotificacion(rs.getInt(sql_codigoNotificacion));
                n.setCodProcedimiento(rs.getString(sql_codProcedimiento));
                n.setCodTramite(rs.getInt(sql_codTramite));
                n.setEjercicio(rs.getInt(sql_ejercicio));
                valorDate = rs.getTimestamp(sql_fechaAcuse);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    n.setFechaAcuse(cal);
                }
                valorDate = rs.getTimestamp(sql_fechaEnvio);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    n.setFechaEnvio(cal);
                }
                blob = rs.getBlob(sql_firma);
                if(blob != null)
                {
                    n.setFirma(IOUtils.toByteArray(blob.getBinaryStream()));
                }
                n.setFirmada(rs.getString(sql_firmada));
                n.setNumExpediente(rs.getString(sql_numExpediente));
                n.setNumRegistroTelematico(rs.getString(sql_registroRt));
                n.setOcurrenciaTramite(rs.getInt(sql_ocuTramite));
                n.setResultado(rs.getString(sql_resultado));
                n.setTextoNotificacion(rs.getString(sql_textoNotificacion));
                n.setXmlNotificacion(rs.getString(sql_xmlNotificacion));
                // Por cada notificación recuperada, habrá que recuperar:
                // - adjuntos
                // - autorizados
                // - adjuntos externos
                n.setAdjuntos(this.getListaAdjuntosNotificacion(n.getCodNotificacion(), codOrganizacion, ejercicio, numExpediente, con));
                n.setAutorizados(this.getListaAutorizadosNotificacion(codOrganizacion, ejercicio, numExpediente, n.getCodNotificacion(), con));
                n.setAdjuntosExternos(this.getListaAdjuntosExternosNotificacion(n.getCodNotificacion(), con));         
                retList.add(n);
            }
            return retList;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un erro en el metodo expedienteDAO.getListaNotificaciones: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }
    }
    
    /**
     * Recupera la lista de adjuntos de una notificación.
     * El 4 primeros parámetros se utilizan para filtrar la consulta sobre la tabla ADJUNTO_NOTIFICACION.
     * 
     * @param idNotificacion
     * @param codOrganizacion
     * @param ejercicio
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<AdjuntoNotificacionVO> getListaAdjuntosNotificacion(int idNotificacion, int codOrganizacion,int ejercicio,String numExpediente, Connection con)  throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {   
            String sql_tabla_adjuntoNotificacion = m_ConfigTechnical.getString("SQL.ADJUNTO_NOTIFICACION");
            
            String sql_codigoNotificacion = m_ConfigTechnical.getString("SQL.ADJUNTO_NOTIFICACION.codigoNotificacion");
            String sql_codMunicipio = m_ConfigTechnical.getString("SQL.ADJUNTO_NOTIFICACION.codMunicipio");
            String sql_ejercicio = m_ConfigTechnical.getString("SQL.ADJUNTO_NOTIFICACION.ejercicio");
            String sql_numExpediente = m_ConfigTechnical.getString("SQL.ADJUNTO_NOTIFICACION.numExpediente");
            String sql_codProcedimiento = m_ConfigTechnical.getString("SQL.ADJUNTO_NOTIFICACION.codProcedimiento");
            String sql_codTramite = m_ConfigTechnical.getString("SQL.ADJUNTO_NOTIFICACION.codTramite");
            String sql_ocuTramite = m_ConfigTechnical.getString("SQL.ADJUNTO_NOTIFICACION.ocuTramite");
            String sql_numUnidadDoc = m_ConfigTechnical.getString("SQL.ADJUNTO_NOTIFICACION.numUnidadDoc");
            
            String query = "select * from "+sql_tabla_adjuntoNotificacion+" where "+sql_codMunicipio+" = "+codOrganizacion+" and "+sql_ejercicio+" = "+ejercicio+" and "+sql_numExpediente+" = '"+numExpediente+"' and "+sql_codigoNotificacion+" = "+idNotificacion;
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ArrayList<AdjuntoNotificacionVO> retList = new ArrayList<AdjuntoNotificacionVO>();
            AdjuntoNotificacionVO adj = null;
            
            while(rs.next())
            {
                adj = new AdjuntoNotificacionVO();
                adj.setCodMunicipio(rs.getInt(sql_codMunicipio));
                adj.setCodNotificacion(rs.getInt(sql_codigoNotificacion));
                adj.setCodProcedimiento(rs.getString(sql_codProcedimiento));
                adj.setCodTramite(rs.getInt(sql_codTramite));
                adj.setEjercicio(rs.getInt(sql_ejercicio));
                adj.setNumExpediente(rs.getString(sql_numExpediente));
                adj.setNumUnidadDocumento(rs.getInt(sql_numUnidadDoc));
                adj.setOcurrenciaTramite(rs.getInt(sql_ocuTramite));
                retList.add(adj);
            }
            return retList;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un erro en el metodo expedienteDAO.getListaAdjuntosNotificacion: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }
    }
    
    /**
     * Recupera la lista de interesados del expediente a los que se le envía la notificación.
     * Los 4 primeros parámetros se usan para filtrar la consulta, que se hace contra la tabla AUTORIZADO_NOTIFICACION.
     * 
     * @param codOrganizacion
     * @param ejercicio
     * @param numExpediente
     * @param idNotificacion
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<AutorizadoNotificacionVO> getListaAutorizadosNotificacion(int codOrganizacion, int ejercicio, String numExpediente, int idNotificacion, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {   
            String sql_tabla_autorizadoNotificacion = m_ConfigTechnical.getString("SQL.AUTORIZADO_NOTIFICACION");
            
            String sql_codigoNotificacion = m_ConfigTechnical.getString("SQL.AUTORIZADO_NOTIFICACION.codigoNotificacion");
            String sql_codMunicipio = m_ConfigTechnical.getString("SQL.AUTORIZADO_NOTIFICACION.codMunicipio");
            String sql_ejercicio = m_ConfigTechnical.getString("SQL.AUTORIZADO_NOTIFICACION.ejercicio");
            String sql_numExpediente = m_ConfigTechnical.getString("SQL.AUTORIZADO_NOTIFICACION.numExpediente");
            String sql_codTercero = m_ConfigTechnical.getString("SQL.AUTORIZADO_NOTIFICACION.codTercero");
            String sql_verTercero = m_ConfigTechnical.getString("SQL.AUTORIZADO_NOTIFICACION.verTercero");
            
            String query = "select * from "+sql_tabla_autorizadoNotificacion+" where "+sql_codMunicipio+" = "+codOrganizacion+" and "+sql_ejercicio+" = "+ejercicio+" and "+sql_numExpediente+" = '"+numExpediente+"' and "+sql_codigoNotificacion+" = "+idNotificacion;
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ArrayList<AutorizadoNotificacionVO> retList = new ArrayList<AutorizadoNotificacionVO>();
            AutorizadoNotificacionVO a = null;
            
            while(rs.next())
            {
                a = new AutorizadoNotificacionVO();
                a.setCodMunicipio(rs.getInt(sql_codMunicipio));
                a.setCodNotificacion(rs.getInt(sql_codigoNotificacion));
                a.setCodTercero(rs.getInt(sql_codTercero));
                a.setEjercicio(rs.getInt(sql_ejercicio));
                a.setNumExpediente(rs.getString(sql_numExpediente));
                a.setVersionTercero(rs.getInt(sql_verTercero));
                retList.add(a);
            }
            return retList;
        }
        catch(Exception ex){
            ex.printStackTrace();
            log.error("Se ha producido un erro en el metodo expedienteDAO.getListaAutorizadosNotificacion: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            try{
                if(rs != null) rs.close();
                if(st != null) st.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Recupera la lista de documentos externos asociados a una determinada notificación electrónica.
     * El parámetro idNotificacion se usa para filtrar la consulta, que se hace contra la tabla ADJUNTO_EXT_NOTIFICACION.
     * No se lee el contenido del campo ADJUNTO_EXT_NOTIFICACION.CONTENIDO.
     * 
     * @param idNotificacion
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<AdjuntoExtNotificacionVO> getListaAdjuntosExternosNotificacion(int idNotificacion, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {   
            String sql_tabla_adjuntoExtNotificacion = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION");
            
            String sql_id = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.id");
            String sql_codMunicipio = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.codMunicipio");
            String sql_numExpediente = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.numExpediente");
            String sql_codTramite = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.codTramite");
            String sql_ocuTramite = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.ocuTramite");
            String sql_firma = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.firma");
            String sql_fecha = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.fecha");
            String sql_idNotificacion = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.idNotificacion");
            String sql_plataformaFirma = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.plataformaFirma");
            String sql_codUsuarioFirma = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.codUsuarioFirma");
            String sql_nombre = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.nombre");
            String sql_tipoMime = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.tipoMime");
            String sql_estadoFirma = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.estadoFirma");
            String sql_fechaFirma = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.fechaFirma");
            String sql_codUsuarioRechazo = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.codUsuarioRechazo");
            String sql_fechaRechazo = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.fechaRechazo");
            String sql_observacionesRechazo = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.observacionesRechazo");
            String sql_tipoCertificadoFirma = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.tipoCertificadoFirma");
            
            String query = "select"
                         + " "+sql_id
                         + ", "+sql_codMunicipio
                         + ", "+sql_numExpediente
                         + ", "+sql_codTramite
                         + ", "+sql_ocuTramite
                         + ", "+sql_firma
                         + ", "+sql_fecha
                         + ", "+sql_idNotificacion
                         + ", "+sql_plataformaFirma
                         + ", "+sql_codUsuarioFirma
                         + ", "+sql_nombre
                         + ", "+sql_tipoMime
                         + ", "+sql_estadoFirma
                         + ", "+sql_fechaFirma
                         + ", "+sql_codUsuarioRechazo
                         + ", "+sql_fechaRechazo
                         + ", "+sql_observacionesRechazo
                         + ", "+sql_tipoCertificadoFirma
                         + " from "+sql_tabla_adjuntoExtNotificacion+" where "+sql_idNotificacion+" = "+idNotificacion;
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ArrayList<AdjuntoExtNotificacionVO> retList = new ArrayList<AdjuntoExtNotificacionVO>();
            AdjuntoExtNotificacionVO adj = null;
            int valorInt;
            java.sql.Timestamp valorDate = null;
            GregorianCalendar cal = null;
            Clob clob = null;
            
            while(rs.next())
            {
                adj = new AdjuntoExtNotificacionVO();
                adj.setCodMunicipio(rs.getInt(sql_codMunicipio));
                adj.setCodTramite(rs.getInt(sql_codTramite));
                valorInt = rs.getInt(sql_codUsuarioFirma);
                if(!rs.wasNull())
                    adj.setCodUsuarioFirma(valorInt);
                valorInt = rs.getInt(sql_codUsuarioRechazo);
                if(!rs.wasNull())
                    adj.setCodUsuarioRechazo(valorInt);
                //adj.setContenido(contenido);
                adj.setEstadoFirma(rs.getString(sql_estadoFirma));
                valorDate = rs.getTimestamp(sql_fecha);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    adj.setFecha(cal);
                }
                valorDate = rs.getTimestamp(sql_fechaFirma);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    adj.setFechaFirma(cal);
                }
                valorDate = rs.getTimestamp(sql_fechaRechazo);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    adj.setFechaRechazo(cal);
                }
                adj.setFirma(rs.getString(sql_firma));
                adj.setId(rs.getInt(sql_id));
                valorInt = rs.getInt(sql_idNotificacion);
                if(!rs.wasNull())
                    adj.setIdNotificacion(valorInt);
                adj.setNombre(rs.getString(sql_nombre));
                adj.setNumExpediente(rs.getString(sql_numExpediente));
                adj.setObservacionesRechazo(rs.getString(sql_observacionesRechazo));
                adj.setOcurrenciaTramite(rs.getInt(sql_ocuTramite));
                adj.setPlataformaFirma(rs.getString(sql_plataformaFirma));
                valorInt = rs.getInt(sql_tipoCertificadoFirma);
                if(!rs.wasNull())
                    adj.setTipoCertificadoFirma(valorInt);
                adj.setTipoMime(rs.getString(sql_tipoMime));
                retList.add(adj);
            }
            return retList;
        }
        catch(Exception ex){
            ex.printStackTrace();
            log.error("Se ha producido un erro en el metodo expedienteDAO.getListaAdjuntosExternosNotificacion: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }
    }
    
    /**
     * Recupera el contenido binario de un determinado documento externo adjunto a una notificación.
     * Reside en el campo  ADJUNTO_EXT_NOTIFICACION.CONTENIDO.
     * 
     * @param idNotificacion
     * @param idDocumento
     * @param con
     * @return
     * @throws Exception 
     */
    public byte[] getContenidoDocumentoExternoNotificacion(int idNotificacion, int idDocumento, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {   
            String sql_tabla_adjuntoExtNotificacion = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION");
            
            String sql_id = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.id");
            String sql_idNotificacion = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.idNotificacion");
            String sql_contenido = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.contenido");
            
            String query = "select "+sql_contenido+" from "+sql_tabla_adjuntoExtNotificacion+" where "+sql_id+" = "+idDocumento+" and "+sql_idNotificacion+" = "+idNotificacion;
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            Blob blob = null;
            
            if(rs.next())
            {
                blob = rs.getBlob(sql_contenido);
                if(blob != null)
                {
                    return IOUtils.toByteArray(blob.getBinaryStream());
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un erro en el metodo expedienteDAO.getContenidoDocumentoExternoNotificacion: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            try{
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Recupera la lista de comunicaciones que han sido enviadas a un expediente.
     * Los 3 primeros parámetros se utilizan para filtrar la búsqueda en la tabla COMUNICACION.
     * No se recupera la lista de adjuntos para cada comunicación
     * ya que se implementa un método específico para ello.
     * 
     * @param codOrganizacion
     * @param ejercicio
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<ComunicacionVO> getListaComunicaciones(int codOrganizacion, int ejercicio,
            String numExpediente, Connection con) throws Exception {
        log.debug("ExpedienteDAO.getListaComunicaciones()::BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<ComunicacionVO> resultado = new ArrayList<ComunicacionVO>();
        
        try{
            String sql = "SELECT ID,ASUNTO,TEXTO,TIPO_DOCUMENTO,DOCUMENTO,NOMBRE,FECHA," +
                    "NUM_REGISTRO,ORIGEN_REGISTRO,XML_COMUNICACION,FIRMA,PLATAFORMA_FIRMA," +
                    "LEIDA " + 
                    "FROM COMUNICACION WHERE COD_ORGANIZACION = ? AND NUM_EXPEDIENTE = ? " + 
                    "AND EJERCICIO = ?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            ps.setInt(i++,ejercicio);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                ComunicacionVO comunicacion = new ComunicacionVO();
                
                comunicacion.setId(rs.getInt("ID"));
                comunicacion.setAsunto(rs.getString("ASUNTO"));
                comunicacion.setTexto(rs.getString("TEXTO"));
                comunicacion.setTipoDocumento(rs.getInt("TIPO_DOCUMENTO"));
                comunicacion.setDocumento(rs.getString("DOCUMENTO"));
                comunicacion.setNombre(rs.getString("NOMBRE"));
                comunicacion.setFecha(DateOperations.timestampToCalendar(rs.getTimestamp("FECHA")));
                comunicacion.setNumRegistro(rs.getString("NUM_REGISTRO"));
                comunicacion.setOrigenRegistro(rs.getString("ORIGEN_REGISTRO"));
                comunicacion.setXmlComunicacion(rs.getString("XML_COMUNICACION"));
                comunicacion.setFirma(rs.getString("FIRMA"));
                comunicacion.setPlataformaFirma(rs.getString("PLATAFORMA_FIRMA"));
                comunicacion.setCodOrganizacion(codOrganizacion);
                comunicacion.setEjercicio(ejercicio);
                comunicacion.setNumExpediente(numExpediente);
                comunicacion.setLeida(rs.getInt("LEIDA"));
                // Por cada comunicación recuperada, se invoca a getListaAdjuntosComunicacion(), para recuperar los archivos adjuntos asociados a la comunicación.
                comunicacion.setAdjuntos(this.getListaAdjuntosComunicacion(comunicacion.getId(), con));
                                
                resultado.add(comunicacion);
            }
        }catch(SQLException e){
            log.error("Error al recuperar el expediente de la tabla de expedientes activos: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(rs!=null) rs.close();                
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    /**
     * Recupera la lista de adjuntos a una comunicación que han sido enviadas a un expediente.
     * El primer parámetro se utiliza para filtrar la búsqueda en la tabla ADJUNTO_COMUNICACION.
     * No se lee el contenido del campo ADJUNTO_COMUNICACION.CONTENIDO,
     * ya que se hará sólo en el momento en el que sea necesario para grabarlo en otra tabla.
     * 
     * @param idComunicacion
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<AdjuntoComunicacionVO> getListaAdjuntosComunicacion( int idComunicacion, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {   
            String sql_tabla_adjuntosComunicacion = m_ConfigTechnical.getString("SQL.ADJUNTO_COMUNICACION");
            
            String sql_id = m_ConfigTechnical.getString("SQL.ADJUNTO_COMUNICACION.id");
            String sql_idComunicacion = m_ConfigTechnical.getString("SQL.ADJUNTO_COMUNICACION.idComunicacion");
            String sql_nombre = m_ConfigTechnical.getString("SQL.ADJUNTO_COMUNICACION.nombre");
            String sql_tipoMime = m_ConfigTechnical.getString("SQL.ADJUNTO_COMUNICACION.tipoMime");
            String sql_fecha = m_ConfigTechnical.getString("SQL.ADJUNTO_COMUNICACION.fecha");
            String sql_firma = m_ConfigTechnical.getString("SQL.ADJUNTO_COMUNICACION.firma");
            String sql_plataformaFirma = m_ConfigTechnical.getString("SQL.ADJUNTO_COMUNICACION.plataformaFirma");
            
            String query = "select * from "+sql_tabla_adjuntosComunicacion+" where "+sql_idComunicacion+" = "+idComunicacion;
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ArrayList<AdjuntoComunicacionVO> retList = new ArrayList<AdjuntoComunicacionVO>();
            AdjuntoComunicacionVO a = null;
            java.sql.Timestamp valorDate = null;
            GregorianCalendar cal = null;
            
            while(rs.next())
            {
                a = new AdjuntoComunicacionVO();
                valorDate = rs.getTimestamp(sql_fecha);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    a.setFecha(cal);
                }
                a.setFirma(rs.getString(sql_firma));
                a.setId(rs.getInt(sql_id));
                a.setIdComunicacion(rs.getInt(sql_idComunicacion));
                a.setNombre(rs.getString(sql_nombre));
                a.setPlataformaFirma(rs.getString(sql_plataformaFirma));
                a.setTipoMime(rs.getString(sql_tipoMime));
                retList.add(a);
            }
            return retList;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();                    
            log.error("Se ha producido un erro en el metodo expedienteDAO.getListaAdjuntosComunicacion: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            try{
                if(rs != null) rs.close();
                if(st != null) st.close();
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Recupera el contenido binario de un determinado adjunto de una comunicación determinada.
     * Se obtiene el contenido del campo ADJUNTO_COMUNICACION.CONTENIDO.
     * 
     * @param idDocumento
     * @param idComunicacion
     * @param con
     * @return
     * @throws Exception 
     */
    public byte[] getContenidoAdjuntoComunicacion( int idDocumento, int idComunicacion, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {   
            String sql_tabla_adjuntosComunicacion = m_ConfigTechnical.getString("SQL.ADJUNTO_COMUNICACION");
            
            String sql_id = m_ConfigTechnical.getString("SQL.ADJUNTO_COMUNICACION.id");
            String sql_idComunicacion = m_ConfigTechnical.getString("SQL.ADJUNTO_COMUNICACION.idComunicacion");
            String sql_contenido = m_ConfigTechnical.getString("SQL.ADJUNTO_COMUNICACION.contenido");
            
            String query = "select "+sql_contenido+" from "+sql_tabla_adjuntosComunicacion+" where "+sql_id+" = "+idDocumento+" and "+sql_idComunicacion+" = "+idComunicacion;
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            Blob blob = null;
            
            if(rs.next())
            {
                blob = rs.getBlob(sql_contenido);
                if(blob != null)
                {
                    return IOUtils.toByteArray(blob.getBinaryStream());
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un erro en el metodo expedienteDAO.getContenidoAdjuntoComunicacion: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            try{
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Recupera la lista de anotaciones de registro relacionadas con un expediente.
     * Los 2 primeros parámetros son necesarios para filtrar por los campos E_EXR.EXR_EJE y E_EXR.EXR_NUM de la tabla E_EXR.
     * 
     * @param codOrganizacion
     * @param ejercicio
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<RegistroRelacionadoVO> getListaRegistroRelacionado(int codOrganizacion, int ejercicio, 
            String numExpediente, Connection con) throws Exception {
        log.debug("ExpedienteDAO.getListaRegistroRelacionado()::BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<RegistroRelacionadoVO> resultado = new ArrayList<RegistroRelacionadoVO>();
        
        try{
            String sql = "SELECT EXR_DEP,EXR_UOR,EXR_TIP,EXR_EJR,EXR_NRE,EXR_ORI,EXR_TOP," +
                    "EXR_PRO,EXR_ORIGEN " + 
                    "FROM E_EXR WHERE EXR_MUN = ? AND EXR_NUM = ? AND EXR_EJE = ?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            ps.setInt(i++,ejercicio);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                RegistroRelacionadoVO reg = new RegistroRelacionadoVO();
                reg.setCodDepartamentoRegistro(rs.getInt("EXR_DEP"));
                reg.setCodUnidadRegistro(rs.getInt("EXR_UOR"));
                reg.setTipoAnotacionRegistro(rs.getString("EXR_TIP"));
                reg.setEjercicioAnotacionRegistro(rs.getString("EXR_EJR"));
                reg.setNumAnotacionRegistro(rs.getInt("EXR_NRE"));
                reg.setOrigenAnotacionRegistro(rs.getInt("EXR_ORI"));
                reg.setEjercicioExpediente(ejercicio);
                reg.setNumExpediente(numExpediente);
                reg.setTipoOperacion(rs.getString("EXR_TOP"));
                reg.setCodMunicipioExpediente(codOrganizacion);
                reg.setCodProcedimiento(rs.getString("EXR_PRO"));
                reg.setOrigenExpediente(rs.getString("EXR_ORIGEN"));
                
                resultado.add(reg);
            }
        }catch(SQLException e){
            log.error("Error al recuperar el expediente de la tabla de expedientes activos: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(rs!=null) rs.close();                
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    /**
     * Recupera la lista de anotaciones de registro relacionadas con un expediente.
     * Los 2 primeros parámetros son necesarios para filtrar por los campos E_EXREXT.EXREXT_MUN y E_EXREXT.EXREXT_NUM de la tabla E_EXREXT.
     * 
     * @param codMunicipio
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<RegistroRelacionadoExternoVO> getListaRegistroRelacionadoExterno(int codMunicipio, String numExpediente, Connection con) throws Exception
    {
		log.debug("ExpedienteDAO.getListaRelacionadoExterno()::BEGIN");
	
        Statement st = null;
        ResultSet rs = null;
        try
        {   
            String sql_tabla_eExrExt = m_ConfigTechnical.getString("SQL.E_EXREXT");

            String sql_exrextUor = m_ConfigTechnical.getString("SQL.E_EXREXT.exrextUor");
            String sql_exrextTip = m_ConfigTechnical.getString("SQL.E_EXREXT.exrextTip");
            String sql_exrextEjr = m_ConfigTechnical.getString("SQL.E_EXREXT.exrextEjr");
            String sql_exrextNre = m_ConfigTechnical.getString("SQL.E_EXREXT.exrextNre");
            String sql_exrextMun = m_ConfigTechnical.getString("SQL.E_EXREXT.exrextMun");
            String sql_exrextNum = m_ConfigTechnical.getString("SQL.E_EXREXT.exrextNum");
            String sql_exrextOri = m_ConfigTechnical.getString("SQL.E_EXREXT.exrextOri");
            String sql_exrextTop = m_ConfigTechnical.getString("SQL.E_EXREXT.exrextTop");
            String sql_exrextSer = m_ConfigTechnical.getString("SQL.E_EXREXT.exrextSer");
            String sql_exrextPro = m_ConfigTechnical.getString("SQL.E_EXREXT.exrextPro");
            String sql_exrextFecalta = m_ConfigTechnical.getString("SQL.E_EXREXT.exrextFecalta");
            
            String query = "select * from "+sql_tabla_eExrExt+" where "+sql_exrextMun+" = "+codMunicipio+" and "+sql_exrextNum+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ArrayList<RegistroRelacionadoExternoVO> retList = new ArrayList<RegistroRelacionadoExternoVO>();
            RegistroRelacionadoExternoVO r = null;
            
            java.sql.Timestamp valorDate = null;
            GregorianCalendar cal = null;
            
            while(rs.next())
            {
                r = new RegistroRelacionadoExternoVO();
                r.setCodMunicipioRegistro(rs.getInt(sql_exrextMun));
                r.setCodProcedimiento(rs.getString(sql_exrextPro));
                r.setCodUnidadRegistro(rs.getInt(sql_exrextUor));
                r.setEjercicioAnotacionRegistro(rs.getString(sql_exrextEjr));
                valorDate = rs.getTimestamp(sql_exrextFecalta);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    r.setFechaAlta(cal);
                }
                r.setNumAnotacionRegistro(rs.getLong(sql_exrextNre));
                r.setNumExpediente(rs.getString(sql_exrextNum));
                r.setOrigenAnotacionRegistro(rs.getString(sql_exrextOri));
                r.setSerie(rs.getString(sql_exrextSer));
                r.setTipoAnotacionRegistro(rs.getString(sql_exrextTip));
                r.setTipoOperacion(rs.getString(sql_exrextTop));
                retList.add(r);
            }
            return retList;
        }
        catch(Exception ex){
            ex.printStackTrace();
            log.error("Se ha producido un erro en el metodo expedienteDAO.getListaRegistroRelacionadoExterno: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }
    }
    
    /**
     * Recupera la lista de documentos de tramitación.
     * Los 4 primeros parámetros son necesarios para filtrar la consulta sobre la tabla E_CRD.
     * No se obtiene el contenido binario del documento que reside en el campo E_CRD.CRD_FIL.
     * 
     * @param codMunicipio
     * @param ejercicio
     * @param codProcedimiento
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<DocumentoTramitacionVO> getListaDocumentosTramitacion(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente, Connection con) throws Exception
    {
        log.debug("ExpedienteDAO.getListaDocumentosTramitacion()::BEGIN");
        Statement st = null;
        ResultSet rs = null;
        try
        {   
            String sql_tabla_eCrd = m_ConfigTechnical.getString("SQL.E_CRD");
            
            String sql_codMunicipio = m_ConfigTechnical.getString("SQL.E_CRD.codMunicipio");
            String sql_codProcedimiento = m_ConfigTechnical.getString("SQL.E_CRD.codProcedimiento");
            String sql_ejercicio = m_ConfigTechnical.getString("SQL.E_CRD.ejercicio");
            String sql_numeroExpediente = m_ConfigTechnical.getString("SQL.E_CRD.numeroExpediente");
            String sql_codTramite = m_ConfigTechnical.getString("SQL.E_CRD.codTramite");
            String sql_ocurrencia = m_ConfigTechnical.getString("SQL.E_CRD.ocurrencia");
            String sql_numeroDocumento = m_ConfigTechnical.getString("SQL.E_CRD.numeroDocumento");
            String sql_fechaAlta = m_ConfigTechnical.getString("SQL.E_CRD.fechaAlta");
            String sql_fechaModificacion = m_ConfigTechnical.getString("SQL.E_CRD.fechaModificacion");
            String sql_codUsuarioCreac = m_ConfigTechnical.getString("SQL.E_CRD.codUsuarioCreac");
            String sql_codUsuarioModif = m_ConfigTechnical.getString("SQL.E_CRD.codUsuarioModif");
            String sql_descripcion = m_ConfigTechnical.getString("SQL.E_CRD.descripcion");
            String sql_codDocumento = m_ConfigTechnical.getString("SQL.E_CRD.codDocumento");
            String sql_estadoFirma = m_ConfigTechnical.getString("SQL.E_CRD.estadoFirma");
            String sql_expedienteFirmaDoc = m_ConfigTechnical.getString("SQL.E_CRD.expedienteFirmaDoc");
            String sql_documentoFirmaDoc = m_ConfigTechnical.getString("SQL.E_CRD.documentoFirmaDoc");
            String sql_firmaFirmaDoc = m_ConfigTechnical.getString("SQL.E_CRD.firmaFirmaDoc");
            String sql_finf = m_ConfigTechnical.getString("SQL.E_CRD.finf");
            
            String query = "select"
                          +" "+sql_codMunicipio
                          +", "+sql_codProcedimiento
                          +", "+sql_ejercicio
                          +", "+sql_numeroExpediente
                          +", "+sql_codTramite
                          +", "+sql_ocurrencia
                          +", "+sql_numeroDocumento
                          +", "+sql_fechaAlta
                          +", "+sql_fechaModificacion
                          +", "+sql_codUsuarioCreac
                          +", "+sql_codUsuarioModif
                          +", "+sql_descripcion
                          +", "+sql_codDocumento
                          +", "+sql_estadoFirma
                          +", "+sql_expedienteFirmaDoc
                          +", "+sql_documentoFirmaDoc
                          +", "+sql_firmaFirmaDoc
                          +", "+sql_finf
                          +" from "+sql_tabla_eCrd+" where "+sql_codMunicipio+" = "+codMunicipio+" and "+sql_ejercicio+" = "+ejercicio+" and "+sql_codProcedimiento+" = '"+codProcedimiento+"' and "+sql_numeroExpediente+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query); 
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ArrayList<DocumentoTramitacionVO> retList = new ArrayList<DocumentoTramitacionVO>();
            DocumentoTramitacionVO d = null;
            
            int valorInt;
            java.sql.Timestamp valorDate = null;
            GregorianCalendar cal = null;
            
            while(rs.next())
            {
                d = new DocumentoTramitacionVO();
                d.setCodDocumento(rs.getInt(sql_codDocumento));
                d.setCodMunicipio(rs.getInt(sql_codMunicipio));
                d.setCodProcedimiento(rs.getString(sql_codProcedimiento));
                d.setCodTramite(rs.getInt(sql_codTramite));
                d.setCodUsuarioAlta(rs.getInt(sql_codUsuarioCreac));
                valorInt = rs.getInt(sql_codUsuarioModif);
                if(!rs.wasNull())
                    d.setCodUsuarioModificacion(valorInt);
                d.setDocFd(rs.getString(sql_documentoFirmaDoc));
                d.setEjercicio(rs.getInt(sql_ejercicio));
                d.setEstadoFirma(rs.getString(sql_estadoFirma));
                d.setFd(rs.getString(sql_expedienteFirmaDoc));
                valorDate = rs.getTimestamp(sql_fechaAlta);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    d.setFechaAlta(cal);
                }
                valorDate = rs.getTimestamp(sql_finf);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    d.setFechaInforme(cal);
                }
                valorDate = rs.getTimestamp(sql_fechaModificacion);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    d.setFechaModificacion(cal);
                }
                valorInt = rs.getInt(sql_firmaFirmaDoc);
                if(!rs.wasNull())
                    d.setFirFd(valorInt);
                d.setNombreDocumento(rs.getString(sql_descripcion));
                d.setNumDocumento(rs.getInt(sql_numeroDocumento));
                d.setNumExpediente(rs.getString(sql_numeroExpediente));
                d.setOcurrenciaTramite(rs.getInt(sql_ocurrencia));
				// Por cada documento se invoca a:
				// - ExpedienteDAO.getFirmaDocumentoTramitacion()
				// - ExpedienteDAO.getUsuariosFirmantesDocumentoTramitacion()
				// - ExpedienteDAO.getFirmaFlujoDocumentoTramitacion()
                d.setFirmas(this.getFirmaDocumentoTramitacion(codMunicipio, ejercicio, codProcedimiento, numExpediente, d.getCodTramite(), d.getOcurrenciaTramite(), d.getNumDocumento(), con));
                d.setUsuariosFirmantes(this.getUsuariosFirmantesDocumentoTramitacion(codMunicipio, ejercicio, codProcedimiento, numExpediente, d.getCodTramite(), d.getOcurrenciaTramite(), d.getNumDocumento(), con));
                d.setFirmaFlujo(this.getFirmaFlujoDocumentoTramitacion(codMunicipio, ejercicio, codProcedimiento, numExpediente, d.getCodTramite(), d.getOcurrenciaTramite(), d.getNumDocumento(), con));
                retList.add(d);
            }
            return retList;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un erro en el metodo expedienteDAO.getListaDocumentosTramitacion: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(rs != null)
                rs.close();
            if(st != null)
                st.close();
        }
    }
    
    /**
     * Recupera el contenido binario de un determinado documento de tramitación, el contenido del mismo se obtiene en la tabla E_CRD.CRD_FIL.
     * Sino existe el documento, entonces se devuelve un null, ya que el contenido estará alojado en un gestor documental.
     * 
     * @param codMunicipio: Código del municipio/organización
     * @param ejercicio: Ejercicio
     * @param codProcedimiento: Código del procedimiento
     * @param numExpediente: Número del expediente
     * @param numeroDocumento: Número del documento
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param con
     * @return
     * @throws Exception 
     */
    public byte[] getContenidoDocumentoTramitacion(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,int numeroDocumento, int codTramite,int ocurrenciaTramite,Connection con) throws Exception
    {
        PreparedStatement ps = null;        
        ResultSet rs = null;
        byte[] contenido = null;
        
        try {   
            /**
            String sql_tabla_eCrd = m_ConfigTechnical.getString("SQL.E_CRD");            
            String sql_codMunicipio = m_ConfigTechnical.getString("SQL.E_CRD.codMunicipio");
            String sql_codProcedimiento = m_ConfigTechnical.getString("SQL.E_CRD.codProcedimiento");
            String sql_ejercicio = m_ConfigTechnical.getString("SQL.E_CRD.ejercicio");
            String sql_numeroExpediente = m_ConfigTechnical.getString("SQL.E_CRD.numeroExpediente");
            String sql_numeroDocumento = m_ConfigTechnical.getString("SQL.E_CRD.numeroDocumento");
            String sql_fichero = m_ConfigTechnical.getString("SQL.E_CRD.fichero");            
            //String query = "select "+sql_fichero+" from "+sql_tabla_eCrd+" where "+sql_codMunicipio+" = "+codMunicipio+" and "+sql_ejercicio+" = "+ejercicio+" and "+sql_codProcedimiento+" = '"+codProcedimiento+"' and "+sql_numeroExpediente+" = '"+numExpediente+"' and "+sql_numeroDocumento+" = "+numeroDocumento;
            */
            String query = "SELECT CRD_FIL FROM E_CRD WHERE CRD_MUN=? AND CRD_EJE=? AND CRD_PRO=? AND CRD_NUM=? AND CRD_NUD=? AND CRD_TRA=? AND CRD_OCU=?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            int i=1;            
            ps = con.prepareStatement(query);             
            ps.setInt(i++,codMunicipio);
            ps.setInt(i++,ejercicio);
            ps.setString(i++,codProcedimiento);
            ps.setString(i++,numExpediente);
            ps.setInt(i++,numeroDocumento);
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrenciaTramite);
            
            rs = ps.executeQuery();            
            while(rs.next()) {
                contenido = rs.getBytes("CRD_FIL");
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
            log.error("Se ha producido un erro en el metodo expedienteDAO.getContenidoDocumentoTramitacion: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally{
            if(rs!=null) rs.close();
            if(ps!=null) ps.close();
        }
        return contenido;
    }
    
    
    
    /**
     * Recupera la lista de documentos de tramitación asociados un expediente y a una determinada ocurrencia de trámite.
     * Los 7 primeros parámetros son necesarios para filtrar la consulta sobre la tabla E_CRD_FIR por los siguientes campos E_CRD_FIR.CRD_MUN,
     * E_CRD_FIR.CRD_EJE, E_CRD_FIR.CRD_PRO, E_CRD_FIR.CRD_NUM, E_CRD_FIR.CRD_TRA, E_CRD_FIR.CRD_OCU, E_CRD_FIR.CRD_NUD respectivamente.
     * No se recupera el contenido binario de la firma, que reside en el campo E_CRD_FIR.FIR.
     * 
     * @param codMunicipio
     * @param ejercicio
     * @param codProcedimiento
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numDocumento
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<DocumentoTramitacionFirmaVO> getFirmaDocumentoTramitacion(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente, int codTramite, int ocurrenciaTramite, int numDocumento,Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {   
            String sql_tabla_eCrdFir = m_ConfigTechnical.getString("SQL.E_CRD_FIR");

            String sql_crdMun = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdMun");
            String sql_crdPro = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdPro");
            String sql_crdEje = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdEje");
            String sql_crdNum = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdNum");
            String sql_crdTra = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdTra");
            String sql_crdOcu = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdOcu");
            String sql_crdNud = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdNud");
            String sql_usuCod = m_ConfigTechnical.getString("SQL.E_CRD_FIR.usuCod");
            String sql_firEst = m_ConfigTechnical.getString("SQL.E_CRD_FIR.firEst");
            String sql_fxFirma = m_ConfigTechnical.getString("SQL.E_CRD_FIR.fxFirma");
            String sql_observ = m_ConfigTechnical.getString("SQL.E_CRD_FIR.observ");
            String sql_usuFir = m_ConfigTechnical.getString("SQL.E_CRD_FIR.usuFir");
            String sql_crdCodPfExt = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdCodPfExt");
            String sql_crdIdSolPfExt = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdIdSolPfExt");
            
            String query = "select "
                          +" "+sql_crdMun
                          +", "+sql_crdPro
                          +", "+sql_crdEje
                          +", "+sql_crdNum
                          +", "+sql_crdTra
                          +", "+sql_crdOcu
                          +", "+sql_crdNud
                          +", "+sql_usuCod
                          +", "+sql_firEst
                          +", "+sql_fxFirma
                          +", "+sql_observ
                          +", "+sql_usuFir
                          +", "+sql_crdCodPfExt
                          +", "+sql_crdIdSolPfExt
                          +" from "+sql_tabla_eCrdFir+" where "+sql_crdMun+" = "+codMunicipio+" and "+sql_crdEje+" = "+ejercicio+" and "+sql_crdPro+" = '"+codProcedimiento+"' and "+sql_crdNum+" = '"+numExpediente+"' and "+sql_crdTra+" = "+codTramite+" and "+sql_crdOcu+" = "+ocurrenciaTramite+" and "+sql_crdNud+" = "+numDocumento;
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ArrayList<DocumentoTramitacionFirmaVO> retList = new ArrayList<DocumentoTramitacionFirmaVO>();
            DocumentoTramitacionFirmaVO d = null;
            
            int valorInt;
            java.sql.Timestamp valorDate = null;
            GregorianCalendar cal = null;
            
            while(rs.next())
            {
                d = new DocumentoTramitacionFirmaVO();  
                d.setCodMunicipio(rs.getInt(sql_crdMun));
                d.setCodProcedimiento(rs.getString(sql_crdPro));
                d.setCodTramite(rs.getInt(sql_crdTra));
                d.setCodUsuario(rs.getInt(sql_usuCod));
                valorInt = rs.getInt(sql_usuFir);
                if(!rs.wasNull())
                    d.setCodUsuarioFirma(valorInt);
                d.setEjercicio(rs.getInt(sql_crdEje));
                d.setEstadoFirma(rs.getString(sql_firEst));
                valorDate = rs.getTimestamp(sql_fxFirma);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    d.setFxFirma(cal);
                }
                d.setNumExpediente(rs.getString(sql_crdNum));
                d.setNumeroDocumento(rs.getInt(sql_crdNud));
                d.setObservaciones(rs.getString(sql_observ));
                d.setOcurrenciaTramite(rs.getInt(sql_crdOcu));
                d.setPfExt(rs.getString(sql_crdCodPfExt));
                d.setSolPfExt(rs.getString(sql_crdIdSolPfExt));
                retList.add(d);
            }
            return retList;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un erro en el metodo expedienteDAO.getFirmaDocumentoTramitacion: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            try{
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Recupera la lista de documentos de tramitación asociados un expediente y a una determinada ocurrencia de trámite.
     * Los 7 primeros parámetros son necesarios para filtrar la consulta sobre la tabla E_CRD_FIR_FIRMANTES por los siguientes campos 
	 * E_CRD_FIR_FIRMANTES.COD_MUNICIPIO, E_CRD_FIR_FIRMANTES.EJERCICIO, E_CRD_FIR_FIRMANTES.COD_PROCEDIMIENTO, E_CRD_FIR_FIRMANTES.NUM_EXPEDIENTE, 
	 * E_CRD_FIR_FIRMANTES.COD_TRAMITE, E_CRD_FIR_FIRMANTES.COD_OCURRENCIA, E_CRD_FIR_FIRMANTES.COD_DOCUMENTO respectivamente.
     * No se recupera el contenido binario de la firma, que reside en el campo E_CRD_FIR_FIRMANTES.FIRMA
     * 
     * @param codMunicipio
     * @param ejercicio
     * @param codProcedimiento
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numDocumento
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<DocumentoTramitacionFirmantesVO> getUsuariosFirmantesDocumentoTramitacion(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente, int codTramite, int ocurrenciaTramite, int numDocumento,Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {   
            String sql_tabla_eCrdFir = "E_CRD_FIR_FIRMANTES";

            String sql_crdMun = "COD_MUNICIPIO";
            String sql_crdPro = "COD_PROCEDIMIENTO";
            String sql_crdEje = "EJERCICIO";
            String sql_crdNum = "NUM_EXPEDIENTE";
            String sql_crdTra = "COD_TRAMITE";
            String sql_crdOcu = "COD_OCURRENCIA";
            String sql_crdNud = "COD_DOCUMENTO";
            String sql_usuCod = "ID_USUARIO";
            String sql_orden = "ORDEN";
            String sql_firEst = "ESTADO_FIRMA";
            String sql_firFecha = "FECHA_FIRMA";
            
            String query = "select "
                          +" "+sql_crdMun
                          +", "+sql_crdPro
                          +", "+sql_crdEje
                          +", "+sql_crdNum
                          +", "+sql_crdTra
                          +", "+sql_crdOcu
                          +", "+sql_crdNud
                          +", "+sql_usuCod
                          +", "+sql_orden
                          +", "+sql_firEst
                          +", "+sql_firFecha
                          +" from "+sql_tabla_eCrdFir+" where "+sql_crdMun+" = "+codMunicipio+" and "+sql_crdEje+" = "+ejercicio+" and "+sql_crdPro+" = '"+codProcedimiento+"' and "+sql_crdNum+" = '"+numExpediente+"' and "+sql_crdTra+" = "+codTramite+" and "+sql_crdOcu+" = "+ocurrenciaTramite+" and "+sql_crdNud+" = "+numDocumento;
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ArrayList<DocumentoTramitacionFirmantesVO> listaFirmantes = new ArrayList<DocumentoTramitacionFirmantesVO>();
            DocumentoTramitacionFirmantesVO usuario = null;
            
            java.sql.Timestamp valorDate = null;
            GregorianCalendar cal = null;
            
            while(rs.next())
            {
                usuario = new DocumentoTramitacionFirmantesVO();  
                usuario.setCodMunicipio(rs.getInt(sql_crdMun));
                usuario.setCodProcedimiento(rs.getString(sql_crdPro));
                usuario.setCodTramite(rs.getInt(sql_crdTra));
                usuario.setIdUsuario(rs.getInt(sql_usuCod));
				usuario.setOrden(rs.getInt(sql_orden));
                usuario.setEjercicio(rs.getInt(sql_crdEje));
                usuario.setEstadoFirma(rs.getString(sql_firEst));
                valorDate = rs.getTimestamp(sql_firFecha);
                if(valorDate != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(valorDate);
                    usuario.setFechaFirma(cal);
                }
                usuario.setNumExpediente(rs.getString(sql_crdNum));
                usuario.setNumeroDocumento(rs.getInt(sql_crdNud));
                usuario.setOcurrenciaTramite(rs.getInt(sql_crdOcu));
                listaFirmantes.add(usuario);
            }
            return listaFirmantes;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un error en el metodo expedienteDAO.getUsuariosFirmantesDocumentoTramitacion: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            try{
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Recupera la lista de documentos de tramitación asociados un expediente y a una determinada ocurrencia de trámite.
     * Los 7 primeros parámetros son necesarios para filtrar la consulta sobre la tabla E_CRD_FIR_FLUJO por los siguientes campos:
	 * E_CRD_FIR_FLUJO.COD_MUNICIPIO, E_CRD_FIR_FLUJO.EJERCICIO, E_CRD_FIR_FLUJO.COD_PROCEDIMIENTO, E_CRD_FIR_FLUJO.NUM_EXPEDIENTE, 
	 * E_CRD_FIR_FLUJO.COD_TRAMITE, E_CRD_FIR_FLUJO.COD_OCURRENCIA, E_CRD_FIR_FLUJO.COD_DOCUMENTO respectivamente.
     * 
     * @param codMunicipio
     * @param ejercicio
     * @param codProcedimiento
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numDocumento
     * @param con
     * @return
     * @throws Exception 
     */
    public ArrayList<DocumentoTramitacionFlujoVO> getFirmaFlujoDocumentoTramitacion(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente, int codTramite, int ocurrenciaTramite, int numDocumento,Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {   
            String sql_tabla_eCrdFir = "E_CRD_FIR_FLUJO";

            String sql_crdMun = "COD_MUNICIPIO";
            String sql_crdPro = "COD_PROCEDIMIENTO";
            String sql_crdEje = "EJERCICIO";
            String sql_crdNum = "NUM_EXPEDIENTE";
            String sql_crdTra = "COD_TRAMITE";
            String sql_crdOcu = "COD_OCURRENCIA";
            String sql_crdNud = "COD_DOCUMENTO";
            String sql_idTipoFirma = "ID_TIPO_FIRMA";
            
            String query = "select "
                          +" "+sql_crdMun
                          +", "+sql_crdPro
                          +", "+sql_crdEje
                          +", "+sql_crdNum
                          +", "+sql_crdTra
                          +", "+sql_crdOcu
                          +", "+sql_crdNud
                          +", "+sql_idTipoFirma
                          +" from "+sql_tabla_eCrdFir+" where "+sql_crdMun+" = "+codMunicipio+" and "+sql_crdEje+" = "+ejercicio+" and "+sql_crdPro+" = '"+codProcedimiento+"' and "+sql_crdNum+" = '"+numExpediente+"' and "+sql_crdTra+" = "+codTramite+" and "+sql_crdOcu+" = "+ocurrenciaTramite+" and "+sql_crdNud+" = "+numDocumento;
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            ArrayList<DocumentoTramitacionFlujoVO> listaFlujos = new ArrayList<DocumentoTramitacionFlujoVO>();
            DocumentoTramitacionFlujoVO flujo = null;
            
            while(rs.next())
            {
                flujo = new DocumentoTramitacionFlujoVO();  
                flujo.setCodMunicipio(rs.getInt(sql_crdMun));
                flujo.setCodProcedimiento(rs.getString(sql_crdPro));
                flujo.setCodTramite(rs.getInt(sql_crdTra));
                flujo.setIdTipoFirma(rs.getLong(sql_idTipoFirma));
                flujo.setEjercicio(rs.getInt(sql_crdEje));
                flujo.setNumExpediente(rs.getString(sql_crdNum));
                flujo.setNumeroDocumento(rs.getInt(sql_crdNud));
                flujo.setOcurrenciaTramite(rs.getInt(sql_crdOcu));
                listaFlujos.add(flujo);
            }
            return listaFlujos;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un error en el metodo expedienteDAO.getFirmaFlujoDocumentoTramitacion: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            try{
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Recupera el contenido binario de la firma de un documento de tramitación, que reside en el campo  E_CRD_FIR.FIR.
     * Sino existe, es que, o bien el documento no ha sido firmado, o bien la firma está alojada en un gestor documental.
     * 
     * @param codMunicipio
     * @param ejercicio
     * @param codProcedimiento
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numDocumento
     * @param con
     * @return
     * @throws Exception 
     */
    public byte[] getContenidoFirmaDocumentoTramitacion(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente, int codTramite, int ocurrenciaTramite, int numDocumento,Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {   
            String sql_tabla_eCrdFir = m_ConfigTechnical.getString("SQL.E_CRD_FIR");

            String sql_crdMun = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdMun");
            String sql_crdPro = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdPro");
            String sql_crdEje = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdEje");
            String sql_crdNum = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdNum");
            String sql_crdTra = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdTra");
            String sql_crdOcu = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdOcu");
            String sql_crdNud = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdNud");
            String sql_fir = m_ConfigTechnical.getString("SQL.E_CRD_FIR.fir");
            
            String query = "select "+sql_fir+" from "+sql_tabla_eCrdFir+" where "+sql_crdMun+" = "+codMunicipio+" and "+sql_crdEje+" = "+ejercicio+" and "+sql_crdPro+" = '"+codProcedimiento+"' and "+sql_crdNum+" = '"+numExpediente+"' and "+sql_crdTra+" = "+codTramite+" and "+sql_crdOcu+" = "+ocurrenciaTramite+" and "+sql_crdNud+" = "+numDocumento;
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            if(rs.next())
            {
                return rs.getBytes(sql_fir);
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un error en el metodo expedienteDAO.getContenidoFirmaDocumentoTramitacion: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            try{
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Recupera el contenido binario de la firma de un firmante del documento de tramitación, que reside en el campo  E_CRD_FIR.FIR.
     * Sino existe, es que, o bien el documento no ha sido firmado, o bien la firma está alojada en un gestor documental.
     * 
     * @param codMunicipio
     * @param ejercicio
     * @param codProcedimiento
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numDocumento
     * @param con
     * @return
     * @throws Exception 
     */
    public byte[] getContenidoFirmaFirmanteDocumentoTramitacion(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente, int codTramite, int ocurrenciaTramite, int numDocumento,Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {   
            String sql_tabla_eCrdFir = "E_CRD_FIR_FIRMANTES";

            String sql_crdMun = "COD_MUNICIPIO";
            String sql_crdPro = "COD_PROCEDIMIENTO";
            String sql_crdEje = "EJERCICIO";
            String sql_crdNum = "NUM_EXPEDIENTE";
            String sql_crdTra = "COD_TRAMITE";
            String sql_crdOcu = "COD_OCURRENCIA";
            String sql_crdNud = "COD_DOCUMENTO";
            String sql_fir = "FIRMA";
            
            String query = "select "+sql_fir+" from "+sql_tabla_eCrdFir+" where "+sql_crdMun+" = "+codMunicipio+" and "+sql_crdEje+" = "+ejercicio+" and "+sql_crdPro+" = '"+codProcedimiento+"' and "+sql_crdNum+" = '"+numExpediente+"' and "+sql_crdTra+" = "+codTramite+" and "+sql_crdOcu+" = "+ocurrenciaTramite+" and "+sql_crdNud+" = "+numDocumento;
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            if(rs.next())
            {
                return rs.getBytes(sql_fir);
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un error en el metodo expedienteDAO.getContenidoFirmaDocumentoTramitacion: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            try{
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Si un expediente está anulado, se puede recuperar las causas por las cuales se anuló.
     * Para ello se debe consultar en la tabla E_EXPSIT filtrando por los 3 primeros parámetros.
     * 
     * @param codMunicipio
     * @param ejercicio
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public SituacionExpedienteAnuladoVO getSituacionExpedienteAnulado(int codMunicipio, int ejercicio, 
            String numExpediente,Connection con) throws Exception {
        log.debug("ExpedienteDAO.getSituacionExpedienteAnulado()::BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        SituacionExpedienteAnuladoVO resultado = null;
        
        try{
            String sql = "SELECT EXPSIT_JUST,EXPSIT_USUARIO,EXPSIT_AUTORIZA " + 
                    "FROM E_EXPSIT WHERE EXPSIT_MUN = ? AND EXPSIT_NUM = ? AND EXPSIT_EJE = ?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
                    
            ps= con.prepareStatement(sql);
            int i = 1;
            
            ps.setInt(i++,codMunicipio);
            ps.setString(i++,numExpediente);
            ps.setInt(i++,ejercicio);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                resultado = new SituacionExpedienteAnuladoVO();
                resultado.setNumExpediente(numExpediente);
                resultado.setJustificacion(rs.getString("EXPSIT_JUST"));
                resultado.setUsuario(rs.getString("EXPSIT_USUARIO"));
                resultado.setAutoriza(rs.getString("EXPSIT_AUTORIZA"));
                resultado.setCodMunicipio(codMunicipio);
                resultado.setEjercicio(ejercicio);
            }
        }catch(SQLException e){
            e.printStackTrace();
            log.error("Error al recuperar el expediente de la tabla de expedientes activos: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(rs!=null) rs.close();                
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    /**
     * Cuando el expediente ha sido enviado a las tablas del histórico,
     * habrá que eliminar toda la información de la tablas en las que se encontraba originalmente.
     * 
     * @param codMunicipio
     * @param ejercicio
     * @param numExpediente
     * @param con
     * @param codProcedimiento
     * @return
     * @throws Exception 
     */
    public SituacionExpedienteAnuladoVO borrarDatosExpediente(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        try
        {
            //TODO tener en cuenta metadatos de documentos
			
            //E_CRD_FIR
            borrarFirmaDocumentoTramitacion(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
            
            //E_CRD_FIR_FIRMANTES
            borrarUsuariosFirmantesDocumentoTramitacion(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
            
            //E_CRD_FIR_FLUJO
            borrarFirmaFlujoDocumentoTramitacion(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
            
            //E_CRD
            borrarListaDocumentosTramitacion(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
            
            //ADJUNTO_EXT_NOTIFICACION
            borrarAdjuntosExtNotificacion(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);

            //AUTORIZADO_NOTIFICACION        
            borrarAutorizadosNotificacion(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
            
            //ADJUNTO_NOTIFICACION        
            borrarAdjuntosNotificacion(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
            
            //NOTIFICACION        
            borrarListaNotificaciones(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
            
            //ADJUNTO_COMUNICACION
            //COMUNICACION
            borrarComunicaciones(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
            
            //E_DOCS_FIRMAS    
            //E_DOCS_PRESENTADOS
            borrarDocumentosPresentados(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
            
            //E_DOC_EXT
            borrarDocumentosExternos(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);

            //E_EXR
            borrarListaRegistroRelacionado(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
            
            //E_EXREXT
            borrarListaRegistroRelacionadoExterno(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
            
            //E_EXT
            borrarInteresadosExpediente(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
            
            //LIST_TRAM_ORIG
            borrarListaTramitesOrigen(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
            
            //E_CRO
            borrarTramitesExpediente(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
            
            //E_EXPSIT            
            borrarSituacionExpedienteAnulado(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);

            //OPERACIONES_EXPEDIENTE
            borrarOperacionesExpediente(codMunicipio, ejercicio, numExpediente, con);

            //E_ALE: no se ha pasado a histórico, sólo se borra.            
            borrarAlertas(codMunicipio, ejercicio, numExpediente, con);
            
            //E_EXP
            borrarExpediente(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un erro en el metodo expedienteDAO.borrarDatosExpediente", ex);
            throw new Exception(ex);
        }
        return null;
    }
    
    private void borrarFirmaDocumentoTramitacion(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = m_ConfigTechnical.getString("SQL.E_CRD_FIR");
            campoMun = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdMun");
            campoEje = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdEje");
            campoExp = m_ConfigTechnical.getString("SQL.E_CRD_FIR.crdNum");
            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }
            }
        }catch(Exception e){
            e.printStackTrace();            
            log.error("Error al borrar la firma de un documento de tramitación: " + e.getMessage());
            throw e;
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente + ":" + ex.getMessage());
            }
        }
    }
    
    private void borrarUsuariosFirmantesDocumentoTramitacion(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = "E_CRD_FIR_FIRMANTES";
            campoMun = "COD_MUNICIPIO";
            campoEje = "EJERCICIO";
            campoExp = "NUM_EXPEDIENTE";
            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }
            }
        }catch(Exception e){
            e.printStackTrace();            
            log.error("Error al borrar los usuarios firmantes de un documento de tramitación: " + e.getMessage());
            throw e;
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente + ":" + ex.getMessage());
            }
        }
    }
    
    private void borrarFirmaFlujoDocumentoTramitacion(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = "E_CRD_FIR_FLUJO";
            campoMun = "COD_MUNICIPIO";
            campoEje = "EJERCICIO";
            campoExp = "NUM_EXPEDIENTE";
            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }
            }
        }catch(Exception e){
            e.printStackTrace();            
            log.error("Error al borrar el flujo de firma de un documento de tramitación: " + e.getMessage());
            throw e;
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente + ":" + ex.getMessage());
            }
        }
    }
    
    private void borrarListaDocumentosTramitacion(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = m_ConfigTechnical.getString("SQL.E_CRD");
            campoMun = m_ConfigTechnical.getString("SQL.E_CRD.codMunicipio");
            campoEje = m_ConfigTechnical.getString("SQL.E_CRD.ejercicio");
            campoExp = m_ConfigTechnical.getString("SQL.E_CRD.numeroExpediente");
            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error al borrar la lista de documentos de tramitación del expediente " + numExpediente + ": " + e.getMessage());
            throw e;
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
            }
        }
    }
    
    private void borrarAdjuntosExtNotificacion(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION");
            campoMun = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.codMunicipio");
            campoExp = m_ConfigTechnical.getString("SQL.ADJUNTO_EXT_NOTIFICACION.numExpediente");
            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error al borrar la lista de adjuntos externos de una notificación del expediente " + numExpediente + ": " + e.getMessage());
            throw e;
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
            }
        }
    }
    
    private void borrarAutorizadosNotificacion(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = m_ConfigTechnical.getString("SQL.AUTORIZADO_NOTIFICACION");
            campoMun = m_ConfigTechnical.getString("SQL.AUTORIZADO_NOTIFICACION.codMunicipio");
            campoEje = m_ConfigTechnical.getString("SQL.AUTORIZADO_NOTIFICACION.ejercicio");
            campoExp = m_ConfigTechnical.getString("SQL.AUTORIZADO_NOTIFICACION.numExpediente");
            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }  
            }
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
            }
        }
    }
    
    private void borrarAdjuntosNotificacion(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = m_ConfigTechnical.getString("SQL.ADJUNTO_NOTIFICACION");
            campoMun = m_ConfigTechnical.getString("SQL.ADJUNTO_NOTIFICACION.codMunicipio");
            campoEje = m_ConfigTechnical.getString("SQL.ADJUNTO_NOTIFICACION.ejercicio");
            campoExp = m_ConfigTechnical.getString("SQL.ADJUNTO_NOTIFICACION.numExpediente");
            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }  
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error al eliminar los adjuntos de una notificación del expediente " + numExpediente + ": " + e.getMessage());
            throw e;
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex){
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
            }
        }
    }
    
    private void borrarListaNotificaciones(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = m_ConfigTechnical.getString("SQL.NOTIFICACION");
            campoMun = m_ConfigTechnical.getString("SQL.NOTIFICACION.codMunicipio");
            campoEje = m_ConfigTechnical.getString("SQL.NOTIFICACION.ejercicio");
            campoExp = m_ConfigTechnical.getString("SQL.NOTIFICACION.numExpediente");

            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }  
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error al eliminar las notificaciones del expediente: " + numExpediente + ": " + e.getMessage());
            throw e;
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
            }
        }
    }
    
    private void borrarComunicaciones(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            ArrayList<ComunicacionVO> comunicaciones = this.getListaComunicaciones(codMunicipio, ejercicio, numExpediente, con);
            String campoIdComunicacion = null;
            String campoIdComunicacionAdj = null;
            for(ComunicacionVO cAct : comunicaciones)
            {
                //ADJUNTO_COMUNICACION  
                nombreTabla = m_ConfigTechnical.getString("SQL.ADJUNTO_COMUNICACION");
                campoIdComunicacionAdj = m_ConfigTechnical.getString("SQL.ADJUNTO_COMUNICACION.idComunicacion");

                query = "select count(*) TOTAL from "+nombreTabla+" where "+campoIdComunicacionAdj+" = "+cAct.getId();
                log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    total = rs.getInt("TOTAL");
                    if(total > 0)
                    {
                        query = "delete from "+nombreTabla+" where "+campoIdComunicacionAdj+" = "+cAct.getId();
                        log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                        stEliminar = con.createStatement();
                        eliminados = st.executeUpdate(query);
                        if(eliminados != total)
                            throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                    }
                }
                try
                {
                    if(rs != null)
                        rs.close();
                    if(st != null)
                        st.close();
                    if(stEliminar != null)
                        stEliminar.close();
                }
                catch(Exception ex)
                {
                    log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }

                //COMUNICACION
                nombreTabla = m_ConfigTechnical.getString("SQL.COMUNICACION");
                campoIdComunicacion = m_ConfigTechnical.getString("SQL.COMUNICACION.id");
                campoMun = m_ConfigTechnical.getString("SQL.COMUNICACION.codOrganizacion");
                campoEje = m_ConfigTechnical.getString("SQL.COMUNICACION.ejercicio");
                campoExp = m_ConfigTechnical.getString("SQL.COMUNICACION.numExpediente");

                query = "select count(*) TOTAL from "+nombreTabla+" where "+campoIdComunicacion+" = "+cAct.getId()+" and "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    total = rs.getInt("TOTAL");
                    if(total > 0)
                    {
                        query = "delete from "+nombreTabla+" where "+campoIdComunicacion+" = "+cAct.getId()+" and "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                        log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                        stEliminar = con.createStatement();
                        eliminados = st.executeUpdate(query);
                        if(eliminados != total)
                            throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                    }
                }
                try
                {
                    if(rs != null)
                        rs.close();
                    if(st != null)
                        st.close();
                    if(stEliminar != null)
                        stEliminar.close();
                }
                catch(Exception ex)
                {
                    log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error al eliminar las comunicaciones del expediente " + numExpediente + ":" + e.getMessage());
            throw e;
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
            }
        }
    }
    
    private void borrarDocumentosPresentados(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            ArrayList<DocumentoPresentadoVO> documentos = this.getListaDocumentosPresentados(codMunicipio, ejercicio, codProcedimiento, numExpediente, con);
            String campoIdDocPres = null;
            String campoIdDocPresFirma = null;
            for(DocumentoPresentadoVO docAct : documentos)
            {
                //E_DOCS_FIRMAS    
                nombreTabla = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS");
                campoIdDocPresFirma = m_ConfigTechnical.getString("SQL.E_DOCS_FIRMAS.idDocPresentado");

                query = "select count(*) TOTAL from "+nombreTabla+" where "+campoIdDocPresFirma+" = "+docAct.getCodPresentado();
                log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    total = rs.getInt("TOTAL");
                    if(total > 0)
                    {
                        query = "delete from "+nombreTabla+" where "+campoIdDocPresFirma+" = "+docAct.getCodPresentado();
                        log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                        stEliminar = con.createStatement();
                        eliminados = st.executeUpdate(query);
                        if(eliminados != total)
                            throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                    }   
                }
                try
                {
                    if(rs != null)
                        rs.close();
                    if(st != null)
                        st.close();
                    if(stEliminar != null)
                        stEliminar.close();
                }
                catch(Exception ex)
                {
                    log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                } 

                //E_DOCS_PRESENTADOS
                nombreTabla = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS");
                campoIdDocPres = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoCod");
                campoMun = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoMun");
                campoEje = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoEje");
                campoExp = m_ConfigTechnical.getString("SQL.E_DOCS_PRESENTADOS.presentadoNum");

                query = "select count(*) TOTAL from "+nombreTabla+" where "+campoIdDocPres+" = "+docAct.getCodPresentado()+" and "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    total = rs.getInt("TOTAL");
                    if(total > 0)
                    {
                        query = "delete from "+nombreTabla+" where "+campoIdDocPres+" = "+docAct.getCodPresentado()+" and "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                        log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                        stEliminar = con.createStatement();
                        eliminados = st.executeUpdate(query);
                        if(eliminados != total)
                            throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                    }
                }
                try
                {
                    if(rs != null)
                        rs.close();
                    if(st != null)
                        st.close();
                    if(stEliminar != null)
                        stEliminar.close();
                }
                catch(Exception ex)
                {
                    log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error al eliminar los documentos de inicio del expediente " + numExpediente + ": " + e.getMessage());
            throw e;
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
            }
        }
    }
    
    private void borrarDocumentosExternos(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = m_ConfigTechnical.getString("SQL.E_DOC_EXT");
            campoMun = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtMun");
            campoEje = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtEje");
            campoExp = m_ConfigTechnical.getString("SQL.E_DOC_EXT.docExtNum");

            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error al eliminar los documentos externos del expediente " + numExpediente + ": " + e.getMessage());
            throw e;
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
            }
        }
    }
    
    private void borrarListaRegistroRelacionado(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = m_ConfigTechnical.getString("SQL.E_EXR");
            campoMun = m_ConfigTechnical.getString("SQL.E_EXR.codMunicipio");
            campoEje = m_ConfigTechnical.getString("SQL.E_EXR.ejercicio");
            campoExp = m_ConfigTechnical.getString("SQL.E_EXR.numero");

            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error al borrar la lista de registros relacionados del expediente: " + numExpediente + ":" + e.getMessage());
            throw e;
            
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
            }
        }
    }
    
    private void borrarListaRegistroRelacionadoExterno(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = m_ConfigTechnical.getString("SQL.E_EXREXT");
            campoMun = m_ConfigTechnical.getString("SQL.E_EXREXT.exrextMun");
            campoExp = m_ConfigTechnical.getString("SQL.E_EXREXT.exrextNum");

            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error al borrar la lista de registros externos relacionados con el expediente: " + numExpediente + ": " + e.getMessage());
            throw e;            
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
            }
        }
    }
    
    private void borrarInteresadosExpediente(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = m_ConfigTechnical.getString("SQL.E_EXT");
            campoMun = m_ConfigTechnical.getString("SQL.E_EXT.extMun");
            campoEje = m_ConfigTechnical.getString("SQL.E_EXT.extEje");
            campoExp = m_ConfigTechnical.getString("SQL.E_EXT.extNum");

            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error al eliminar los interesados del expediente " + numExpediente + ": " + e.getMessage());
            throw e;
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
            }
        }
    }
    
    private void borrarListaTramitesOrigen(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = m_ConfigTechnical.getString("SQL.LIST_TRAM_ORIG");
            campoMun = m_ConfigTechnical.getString("SQL.LIST_TRAM_ORIG.codMun");
            campoEje = m_ConfigTechnical.getString("SQL.LIST_TRAM_ORIG.ejercicio");
            campoExp = m_ConfigTechnical.getString("SQL.LIST_TRAM_ORIG.numExp");

            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }
            }
        }catch(Exception e) { 
            e.printStackTrace();
            log.error("Error al eliminar la lista de trámites de origen del expediente: " + numExpediente + ": " + e.getMessage());
            throw e;
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
            }
        }
    }
    
    private void borrarTramitesExpediente(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = m_ConfigTechnical.getString("SQL.E_CRO");
            campoMun = m_ConfigTechnical.getString("SQL.E_CRO.croMun");
            campoEje = m_ConfigTechnical.getString("SQL.E_CRO.croEje");
            campoExp = m_ConfigTechnical.getString("SQL.E_CRO.croNum");

            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error al eliminar los trámites del expediente " + numExpediente + ": " + e.getMessage());
            throw e;
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
            }
        }
    }
    
    private void borrarSituacionExpedienteAnulado(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = m_ConfigTechnical.getString("SQL.E_EXPSIT");
            campoMun = m_ConfigTechnical.getString("SQL.E_EXPSIT.expSitMun");
            campoEje = m_ConfigTechnical.getString("SQL.E_EXPSIT.expSitEje");
            campoExp = m_ConfigTechnical.getString("SQL.E_EXPSIT.expSitNum");

            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error al eliminar la causa de la anulación del expediente " + numExpediente + ":" + e.getMessage());
            throw e;
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
            }
        }
    }
    
    private void borrarOperacionesExpediente(int codMunicipio, int ejercicio, String numExpediente,
            Connection con) throws Exception {
        PreparedStatement ps = null;
        
        try {
            String sql = "DELETE FROM OPERACIONES_EXPEDIENTE WHERE COD_MUNICIPIO = ? AND " + 
                    "EJERCICIO = ? AND NUM_EXPEDIENTE = ?";
            
            if (log.isDebugEnabled()) log.debug(sql);
            
            ps = con.prepareStatement(sql);
            
            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setInt(i++, ejercicio);
            ps.setString(i++, numExpediente);
            
            ps.executeUpdate();
            ps.close();
         }catch (Exception e){
            if (log.isDebugEnabled()) {log.error(e.getMessage());}
            e.printStackTrace();            
            throw e;
        } finally {
            try{
                if (ps!=null) {ps.close();}
            }catch (SQLException esql){
                if (log.isDebugEnabled()) {log.error(esql.getMessage());} 
                esql.printStackTrace();                
                throw new Exception(esql.getMessage(), esql);
            }
         }
    }

    private void borrarAlertas(int codMunicipio, int ejercicio, String numExpediente,Connection con) 
            throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String query = "DELETE FROM E_ALE WHERE ALE_MUN = ? AND ALE_EJE = ? AND ALE_NUM = ?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setInt(i++,codMunicipio);
            ps.setInt(i++,ejercicio);
            ps.setString(i++,numExpediente);
            ps.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
            log.error("Error al eliminar las alertas del expediente " + numExpediente + ":" + e.getMessage());
            throw e;
        } finally {
            try {
                if(rs != null) rs.close();
                if(ps != null) ps.close();
            } catch(Exception ex) {
                log.error("Error al cerrar PreparedStatement/Resultset tras eliminar registros de la tabla E_ALE para el expediente "+numExpediente);
            }
        }
    }
    
    private void borrarExpediente(int codMunicipio, int ejercicio, String codProcedimiento, String numExpediente,Connection con) throws Exception
    {
        Statement st = null;
        Statement stEliminar = null;
        ResultSet rs = null;
        String query = null;
        String nombreTabla = null;
        String campoMun = null;
        String campoEje = null;
        String campoExp = null;
        int total = 0;
        int eliminados = 0;
        try
        {
            nombreTabla = m_ConfigTechnical.getString("SQL.E_EXP");
            campoMun = m_ConfigTechnical.getString("SQL.E_EXP.expMun");
            campoEje = m_ConfigTechnical.getString("SQL.E_EXP.expEje");
            campoExp = m_ConfigTechnical.getString("SQL.E_EXP.expNum");

            query = "select count(*) TOTAL from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                total = rs.getInt("TOTAL");
                if(total > 0)
                {
                    query = "delete from "+nombreTabla+" where "+campoMun+" = "+codMunicipio+" and "+campoEje+" = "+ejercicio+" and "+campoExp+" = '"+numExpediente+"'";
                    log.debug(" (TRAZA DE CONTROL) SQL: " + query);
                    stEliminar = con.createStatement();
                    eliminados = st.executeUpdate(query);
                    if(eliminados != total)
                        throw new Exception("No se han eliminado correctamente todos los datos de la tabla "+nombreTabla+" para el expediente "+numExpediente);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error al eliminar el expediente " + numExpediente + ": " + e.getMessage());
            throw e;
        }
        finally
        {
            try
            {
                if(rs != null)
                    rs.close();
                if(st != null)
                    st.close();
                if(stEliminar != null)
                    stEliminar.close();
            }
            catch(Exception ex)
            {
                log.error("Error al cerrar statement/Resultset tras eliminar registros de la tabla "+nombreTabla+" para el expediente "+numExpediente);
            }
        }
    }
    public void grabarExpediente(ExpedienteVO expediente, Connection con) throws Exception{
        
        PreparedStatement ps = null;
                
        try{
			Map<String, Object> camposYValores = new HashMap<String, Object>();			
			// Valores y respectivas columnas para el insert
			camposYValores.put("EXP_PRO", expediente.getCodProcedimiento());
			camposYValores.put("EXP_EJE", expediente.getEjercicio());
			camposYValores.put("EXP_NUM", expediente.getNumExpediente());
			camposYValores.put("EXP_FEI", DateOperations.toTimestamp(expediente.getFechaInicio()));
			camposYValores.put("EXP_FEF", DateOperations.toTimestamp(expediente.getFechaFin()));
			camposYValores.put("EXP_EST", expediente.getEstado());
			camposYValores.put("EXP_MUN", expediente.getCodOrganizacion());
			camposYValores.put("EXP_USU", expediente.getCodUsuario());
			camposYValores.put("EXP_UOR", expediente.getCodUorInicio());
			camposYValores.put("EXP_PEND", DateOperations.toTimestamp(expediente.getFechaPendiente()));
			camposYValores.put("EXP_TRA", expediente.getCodTramitePendiente());
			camposYValores.put("EXP_TOCU", expediente.getOcurrenciaTramitePendiente());
			camposYValores.put("EXP_LOC", expediente.getLocalizacion());
			camposYValores.put("EXP_CLO", expediente.getCodLocalizacion());
			camposYValores.put("EXP_OBS", expediente.getObservaciones());
			camposYValores.put("EXP_ASU", expediente.getAsunto());
			camposYValores.put("EXP_REF", expediente.getReferencia());
			camposYValores.put("EXP_IMP", expediente.getImportante());
			camposYValores.put("EXP_UBICACION_DOC", expediente.getUbicacionDocumentacion());
			
			SqlExecuter consulta = new SqlExecuter();
			consulta.insertWithValues("E_EXP", camposYValores).logSqlDebug(log);
			consulta.executeUpdate(con);
			
        }catch(SQLException e){
            log.error("Error al grabar el expediente en las tabla E_EXP: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
       
    public void grabarTramitesExpediente(ArrayList<CronoVO> listaCrono,Connection con) throws Exception{
        
        PreparedStatement ps = null;
                
        String sql = "INSERT INTO E_CRO (CRO_PRO,CRO_EJE,CRO_NUM,CRO_TRA,CRO_FEI,CRO_FEF,CRO_USU," +
            "CRO_UTR,CRO_MUN,CRO_OCU,CRO_FIP,CRO_FLI,CRO_FFP,CRO_RES,CRO_OBS,CRO_USF," +
            "CRO_AVISADOCFP,CRO_AVISADOFDP) "+
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
        try{
            for (CronoVO crono : listaCrono) {
                ps= con.prepareStatement(sql);
                int i = 1;
                ps.setString(i++,crono.getCodProcedimiento());
                ps.setInt(i++,crono.getEjercicio());
                ps.setString(i++,crono.getNumExpediente());
                ps.setInt(i++,crono.getCodTramite());
                ps.setTimestamp(i++,DateOperations.toTimestamp(crono.getFechaInicio()));
                ps.setTimestamp(i++,DateOperations.toTimestamp(crono.getFechaFin()));
                ps.setInt(i++,crono.getCodUsuario());
                ps.setInt(i++,crono.getCodUorTramitadora());
                ps.setInt(i++,crono.getCodOrganizacion());
                ps.setInt(i++,crono.getOcurrenciaTramite());
                ps.setTimestamp(i++,DateOperations.toTimestamp(crono.getFechaInicioPlazo()));
                ps.setTimestamp(i++,DateOperations.toTimestamp(crono.getFechaLimite()));
                ps.setTimestamp(i++,DateOperations.toTimestamp(crono.getFechaFinPlazo()));
                ps.setInt(i++,crono.getReserva());
                ps.setString(i++,crono.getObservaciones());
                ps.setInt(i++,crono.getUsuarioFinalizacion());
                ps.setInt(i++,crono.getAvisoCercanaFinPlazo());
                ps.setInt(i++,crono.getAvisoCercanaFinPlazo());

                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar las ocurrencias de trámites en la tabla E_CRO: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    
    }
    
    public void grabarListaTramitesOrigen(ArrayList<ListTramOrigVO> listaTram,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        
        try{
            for (ListTramOrigVO tramite : listaTram) {
                String sql = "INSERT INTO LIST_TRAM_ORIG (EJERCICIO,COD_PRO,COD_MUN,NUM_EXP," + 
                    "COD_TRA_ORIGEN,OCU_TRA_ORIGEN,COD_TRA_DESTINO,OCU_TRA_DESTINO) " + 
                    "VALUES (?,?,?,?,?,?,?,?)";
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
                ps= con.prepareStatement(sql);
                int i = 1;
                ps.setInt(i++,tramite.getEjercicio());
                ps.setString(i++,tramite.getCodProcedimiento());
                ps.setInt(i++,tramite.getCodMunicipio());
                ps.setString(i++,tramite.getNumExpediente());
                ps.setInt(i++,tramite.getCodTramiteOrigen());
                ps.setInt(i++,tramite.getOcurrenciaTramiteOrigen());
                ps.setInt(i++,tramite.getCodTramiteDestino());
                ps.setInt(i++,tramite.getOcurrenciaTramiteDestino());
                
                ps.executeUpdate();
            }
            
        }catch(SQLException e){
            log.error("Error al grabar la lista de trámites de origen en la tabla LIST_TRAM_ORIG: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    
    }
    
    public void grabarListaInteresadoExpediente(ArrayList<InteresadoExpedienteVO> listaInteresados,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        
        try{
            for (InteresadoExpedienteVO interesado : listaInteresados) {
                String sql = "INSERT INTO E_EXT (EXT_MUN,EXT_EJE,EXT_NUM,EXT_TER,EXT_NVR,EXT_DOT," +
                    "EXT_ROL,EXT_PRO,MOSTRAR,EXT_NOTIFICACION_ELECTRONICA) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?)";
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
                ps= con.prepareStatement(sql);
                int i = 1;
                
                ps.setInt(i++,interesado.getCodMunicipio());
                ps.setInt(i++,interesado.getEjercicio());
                ps.setString(i++,interesado.getNumExpediente());
                ps.setInt(i++,interesado.getCodTercero());
                ps.setInt(i++,interesado.getVersionTercero());
                ps.setInt(i++,interesado.getCodDomicilio());
                ps.setInt(i++,interesado.getCodRol());
                ps.setString(i++,interesado.getCodProcedimiento());
                ps.setInt(i++,interesado.getMostrar());
                ps.setString(i++,interesado.getNotificacionElectronica());
                
                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar los interesados de un expediente en la tabla E_EXT: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
       
    }
    
    public void grabarListaDocumentosPresentados(ArrayList<DocumentoPresentadoVO> listaDocs,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        
        try{
            String gestor = ResourceBundle.getBundle("techserver").getString("CON.gestor");
            
            for (DocumentoPresentadoVO documento : listaDocs) {                
                                
                String sql = "INSERT INTO E_DOCS_PRESENTADOS (PRESENTADO_COD,PRESENTADO_MUN," +
                    "PRESENTADO_EJE,PRESENTADO_NUM,PRESENTADO_PRO,PRESENTADO_COD_DOC," +
                    "PRESENTADO_CONTENIDO,PRESENTADO_TIPO,PRESENTADO_EXTENSION," +
                    "PRESENTADO_ORIGEN,PRESENTADO_FECHA_ALTA,PRESENTADO_NOMBRE," +
                    "PRESENTADO_COD_USU_ALTA,PRESENTADO_COD_USU_MOD,PRESENTADO_FECHA_MOD) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
                ps= con.prepareStatement(sql);
                int i = 1;
                
                ps.setInt(i++,documento.getCodPresentado());
                ps.setInt(i++,documento.getCodMunicipio());
                ps.setInt(i++,documento.getEjercicio());
                ps.setString(i++,documento.getNumExpediente());
                ps.setString(i++,documento.getCodProcedimiento());
                ps.setInt(i++,documento.getCodDocumentoPresentado());
                ps.setBytes(i++,documento.getContenido());
                ps.setString(i++,documento.getTipoMime());
                ps.setString(i++,documento.getExtension());
                ps.setString(i++,documento.getOrigen());
                ps.setTimestamp(i++,DateOperations.toTimestamp(documento.getFechaAlta()));
                ps.setString(i++,documento.getNombreDocumento());
                ps.setInt(i++,documento.getCodUsuarioAlta());
                ps.setInt(i++,documento.getCodUsuarioModificacion());
                ps.setTimestamp(i++,DateOperations.toTimestamp(documento.getFechaModificacion()));

                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar documentos de inicio en el tabla E_DOCS_PRESENTADOS: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }
    
    public void grabarListaFirmasDocumentoPresentado(ArrayList<DocsFirmasVO> listaFirmasDoc,Connection con) throws Exception{
        
        PreparedStatement ps = null;
                
        try{
            for (DocsFirmasVO firma : listaFirmasDoc) {
                String sql = "INSERT INTO E_DOCS_FIRMAS (ID_DOC_FIRMA,DOC_FIRMA_ESTADO," +
                    "DOC_FIRMA_ORDEN,DOC_FIRMA_UOR,DOC_FIRMA_CARGO,DOC_FIRMA_USUARIO," +
                    "DOC_FIRMA_FECHA,ID_DOC_PRESENTADO,DOC_FECHA_ENVIO,FIRMA " +
                    "DOC_FIRMA_OBSERVACIONES) "+
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
                ps= con.prepareStatement(sql);
                
                int i = 1;
                
                ps.setInt(i++,firma.getIdDocFirma());
                ps.setString(i++,firma.getDocFirmaEstado());
                ps.setInt(i++,firma.getDocFirmaOrden());
                ps.setInt(i++,firma.getDocFirmaUor());
                ps.setInt(i++,firma.getDocFirmaCargo());
                ps.setInt(i++,firma.getDocFirmaUsuario());
                ps.setTimestamp(i++,DateOperations.toTimestamp(firma.getDocFirmaFecha()));
                ps.setInt(i++,firma.getIdDocPresentado());
                ps.setTimestamp(i++,DateOperations.toTimestamp(firma.getDocFechaEnvio()));
                ps.setBytes(i++,firma.getFirma());
                ps.setString(i++,firma.getObservaciones());

                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar la firma de un documento de inicio en la tabla E_DOCS_FIRMAS: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
    }
    
    
    public void grabarListaDocumentosExternos(ArrayList<DocExtVO> listaDocsExt,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        
        try{
            for (DocExtVO documento : listaDocsExt) {
                byte [] contenido = HistoricoExpedienteDAO.getInstance().getContenidoDocumentoExterno(documento, con);

                String sql = "INSERT INTO E_DOC_EXT (DOC_EXT_NUM,DOC_EXT_MUN,DOC_EXT_EJE," +
                    "DOC_EXT_COD,DOC_EXT_NOM,DOC_EXT_FAL,DOC_EXT_FIL,DOC_EXT_TIP,DOC_EXT_EXT," +
                    "DOC_EXT_ORIGEN) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?)";
                
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);            
                ps= con.prepareStatement(sql);
                int i = 1;
                
                ps.setString(i++,documento.getNumExpediente());
                ps.setInt(i++,documento.getCodMunicipio());
                ps.setInt(i++,documento.getEjercicio());
                ps.setInt(i++,documento.getCodDocumento());
                ps.setString(i++,documento.getNombreDocumento());
                ps.setTimestamp(i++,DateOperations.toTimestamp(documento.getFechaDocumento()));
                ps.setBytes(i++,contenido);
                ps.setString(i++,documento.getTipoDocumento());
                ps.setString(i++,documento.getExtension());
                ps.setString(i++,documento.getOrigen());

                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar la lista de documentos externos en E_DOC_EXT: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }

    public void grabarListaNotificaciones(ArrayList<NotificacionVO> listaNotificaciones,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        
        try{
            for (NotificacionVO notificacion : listaNotificaciones){
                String sql = "INSERT INTO NOTIFICACION (CODIGO_NOTIFICACION,NUM_EXPEDIENTE," +
                    "COD_PROCEDIMIENTO,EJERCICIO,COD_MUNICIPIO,COD_TRAMITE,OCU_TRAMITE," +
                    "ACTO_NOTIFICADO,CADUCIDAD_NOTIFICACION,FIRMA,TEXTO_NOTIFICACION,FIRMADA," +
                    "XML_NOTIFICACION,FECHA_ENVIO,REGISTRO_RT,FECHA_ACUSE,RESULTADO) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
                
                ps= con.prepareStatement(sql);
                int i = 1;
                            
                ps.setInt(i++,notificacion.getCodNotificacion());
                ps.setString(i++,notificacion.getNumExpediente());
                ps.setString(i++,notificacion.getCodProcedimiento());
                ps.setInt(i++,notificacion.getEjercicio());
                ps.setInt(i++,notificacion.getCodMunicipio());
                ps.setInt(i++,notificacion.getCodTramite());
                ps.setInt(i++,notificacion.getCodTramite());
                ps.setString(i++,notificacion.getActoNotificado());
                ps.setInt(i++,notificacion.getCaducidadNotificacion());
                ps.setBytes(i++,notificacion.getFirma());
                ps.setString(i++,notificacion.getTextoNotificacion());
                ps.setString(i++,notificacion.getFirmada());
                ps.setString(i++,notificacion.getXmlNotificacion());
                ps.setTimestamp(i++,DateOperations.toTimestamp(notificacion.getFechaEnvio()));
                ps.setString(i++,notificacion.getNumRegistroTelematico());
                ps.setTimestamp(i++,DateOperations.toTimestamp(notificacion.getFechaAcuse()));
                ps.setString(i++,notificacion.getResultado());

                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar la lista de notificaciones en la tabla NOTIFICACION: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
    }
    
    public void grabarListaAdjuntosNotificacion(ArrayList<AdjuntoNotificacionVO> listaAdjNotif,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        
        try{
            for (AdjuntoNotificacionVO adjunto : listaAdjNotif) {
                String sql = "INSERT INTO ADJUNTO_NOTIFICACION (CODIGO_NOTIFICACION,COD_MUNICIPIO," +
                    "EJERCICIO,NUM_EXPEDIENTE,COD_PROCEDIMIENTO,COD_TRAMITE,OCU_TRAMITE," +
                    "NUM_UNIDAD_DOC) " + 
                    "VALUES (?,?,?,?,?,?,?,?)";
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);

                ps= con.prepareStatement(sql);
                
                int i = 1;
                            
                ps.setInt(i++,adjunto.getCodNotificacion());
                ps.setInt(i++,adjunto.getCodMunicipio());
                ps.setInt(i++,adjunto.getEjercicio());
                ps.setString(i++,adjunto.getNumExpediente());
                ps.setString(i++,adjunto.getCodProcedimiento());
                ps.setInt(i++,adjunto.getCodTramite());
                ps.setInt(i++,adjunto.getOcurrenciaTramite());
                ps.setInt(i++,adjunto.getNumUnidadDocumento());

                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar adjuntos de una notificación en la tabla ADJUNTO_NOTIFICACION: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }
    
    public void grabarListaAdjuntosExternosNotificacion(ArrayList<AdjuntoExtNotificacionVO> listaAdjExNotif,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        
        try{
            for (AdjuntoExtNotificacionVO adjExt : listaAdjExNotif) {
                byte [] contenido = HistoricoExpedienteDAO.getInstance().getContenidoAdjuntoExternoNotificacion(adjExt, con);
                
                String sql = "INSERT INTO ADJUNTO_EXT_NOTIFICACION (ID,COD_MUNICIPIO,NUM_EXPEDIENTE," +
                    "COD_TRAMITE,OCU_TRAMITE,FIRMA,FECHA,CONTENIDO,ID_NOTIFICACION," +
                    "PLATAFORMA_FIRMA,COD_USUARIO_FIRMA,NOMBRE,TIPO_MIME,ESTADO_FIRMA," +
                    "FECHA_FIRMA,COD_USUARIO_RECHAZO,FECHA_RECHAZO,OBSERVACIONES_RECHAZO," +
                    "TIPO_CERTIFICADO_FIRMA) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
                
                ps= con.prepareStatement(sql);
                int i = 1;

                ps.setInt(i++,adjExt.getId());
                ps.setInt(i++,adjExt.getCodMunicipio());
                ps.setString(i++,adjExt.getNumExpediente());
                ps.setInt(i++,adjExt.getCodTramite());
                ps.setInt(i++,adjExt.getOcurrenciaTramite());
                ps.setString(i++,adjExt.getFirma());
                ps.setTimestamp(i++,DateOperations.toTimestamp(adjExt.getFecha()));
                ps.setBytes(i++,contenido);
                ps.setInt(i++,adjExt.getIdNotificacion());
                ps.setString(i++,adjExt.getPlataformaFirma());
                ps.setInt(i++,adjExt.getCodUsuarioFirma());
                ps.setString(i++,adjExt.getNombre());
                ps.setString(i++,adjExt.getTipoMime());
                ps.setString(i++,adjExt.getEstadoFirma());
                ps.setTimestamp(i++,DateOperations.toTimestamp(adjExt.getFechaFirma()));
                ps.setInt(i++,adjExt.getCodUsuarioRechazo());
                ps.setTimestamp(i++,DateOperations.toTimestamp(adjExt.getFechaRechazo()));
                ps.setString(i++,adjExt.getObservacionesRechazo());
                ps.setInt(i++,adjExt.getTipoCertificadoFirma());
                
                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar la lista de adjuntos externos de una notificación en la tabla ADJUNTO_EXT_NOTIFICACION: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }
    
    public void grabarListaAutorizadosNotificacion(ArrayList<AutorizadoNotificacionVO> listaAutorizadosNotif,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        
        try{
            for (AutorizadoNotificacionVO autorizado : listaAutorizadosNotif) {
                String sql = "INSERT INTO AUTORIZADO_NOTIFICACION (CODIGO_NOTIFICACION,COD_MUNICIPIO," +
                    "EJERCICIO,NUM_EXPEDIENTE,COD_TERCERO,VER_TERCERO) " + 
                    "VALUES (?,?,?,?,?,?)";
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
                ps= con.prepareStatement(sql);
                
                 int i = 1;
                 
                ps.setInt(i++,autorizado.getCodNotificacion());
                ps.setInt(i++,autorizado.getCodMunicipio());
                ps.setInt(i++,autorizado.getEjercicio());
                ps.setString(i++,autorizado.getNumExpediente());
                ps.setInt(i++,autorizado.getCodTercero());
                ps.setInt(i++,autorizado.getVersionTercero());                
                ps.executeUpdate();
            }
        }catch(SQLException e){
            
            log.error("Error al grabar los autorizados de una notificación en la tabla AUTORIZADO_NOTIFICACION: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }
    
    public void grabarListaComunicaciones(ArrayList<ComunicacionVO> listaComunicaciones,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        
        String sql = "INSERT INTO COMUNICACION (ID,ASUNTO,TEXTO,TIPO_DOCUMENTO,DOCUMENTO," +
            "NOMBRE,FECHA,NUM_REGISTRO,ORIGEN_REGISTRO,XML_COMUNICACION,FIRMA," +
            "PLATAFORMA_FIRMA,COD_ORGANIZACION,EJERCICIO,NUM_EXPEDIENTE,LEIDA) " + 
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
        try{
            for (ComunicacionVO comunicacion : listaComunicaciones) {
                ps= con.prepareStatement(sql);
                int i = 1;
                
                ps.setInt(i++,comunicacion.getId());
                ps.setString(i++,comunicacion.getAsunto());
                ps.setString(i++,comunicacion.getTexto());
                ps.setInt(i++,comunicacion.getTipoDocumento());
                ps.setString(i++,comunicacion.getDocumento());
                ps.setString(i++,comunicacion.getNombre());
                ps.setTimestamp(i++,DateOperations.toTimestamp(comunicacion.getFecha()));
                ps.setString(i++,comunicacion.getNumRegistro());
                ps.setString(i++,comunicacion.getOrigenRegistro());
                ps.setString(i++,comunicacion.getXmlComunicacion());
                ps.setString(i++,comunicacion.getFirma());
                ps.setString(i++,comunicacion.getPlataformaFirma());
                ps.setInt(i++,comunicacion.getCodOrganizacion());
                ps.setInt(i++,comunicacion.getEjercicio());
                ps.setString(i++,comunicacion.getNumExpediente());
                ps.setInt(i++,comunicacion.getLeida());

                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar la lista de comunicaciones en la tabla COMUNICACION: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }
    
    public void grabarListaAdjuntosComunicacion(ArrayList<AdjuntoComunicacionVO> listaAdjuntosCom,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        
        try{
            for (AdjuntoComunicacionVO adjComunicacion : listaAdjuntosCom) {
                byte [] contenido = HistoricoExpedienteDAO.getInstance().getContenidoAdjuntoComunicacion(adjComunicacion, con);

                String sql = "INSERT INTO ADJUNTO_COMUNICACION (ID,ID_COMUNICACION,NOMBRE,TIPO_MIME," +
                    "FECHA,CONTENIDO,FIRMA,PLATAFORMA_FIRMA) " + 
                    "VALUES (?,?,?,?,?,?,?,?)";
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
                
                ps= con.prepareStatement(sql);
                int i = 1;
            
                ps.setInt(i++,adjComunicacion.getId());
                ps.setInt(i++,adjComunicacion.getIdComunicacion());
                ps.setString(i++,adjComunicacion.getNombre());
                ps.setString(i++,adjComunicacion.getTipoMime());
                ps.setTimestamp(i++,DateOperations.toTimestamp(adjComunicacion.getFecha()));
                ps.setBytes(i++,contenido);
                ps.setString(i++,adjComunicacion.getFirma());
                ps.setString(i++,adjComunicacion.getPlataformaFirma());
                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar el adjunto de la comunicación en la tabla ADJUNTO_COMUNICACION: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }
    
    public void grabarListaRegistroRelacionado(ArrayList<RegistroRelacionadoVO> listaRegistrosRel,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        // #253742: Añadir valor de fecha actual al insertar
        java.util.Date fechoraActual = new java.util.Date();
        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(fechoraActual.getTime());  
                
        try{
            for (RegistroRelacionadoVO reg : listaRegistrosRel) {
                String sql = "INSERT INTO E_EXR (EXR_DEP,EXR_UOR,EXR_TIP,EXR_EJR,EXR_NRE,EXR_ORI,EXR_EJE," +
                    "EXR_NUM,EXR_TOP,EXR_MUN,EXR_PRO,EXR_ORIGEN,EXR_FECHAINSMOD) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
                
                ps= con.prepareStatement(sql);
                int i = 1;
                
                ps.setInt(i++,reg.getCodDepartamentoRegistro());
                ps.setInt(i++,reg.getCodUnidadRegistro());
                ps.setString(i++,reg.getTipoAnotacionRegistro());
                ps.setString(i++,reg.getEjercicioAnotacionRegistro());
                ps.setInt(i++,reg.getNumAnotacionRegistro());
                ps.setInt(i++,reg.getOrigenAnotacionRegistro());
                ps.setInt(i++,reg.getEjercicioExpediente());
                ps.setString(i++,reg.getNumExpediente());
                ps.setString(i++,reg.getTipoOperacion());
                ps.setInt(i++,reg.getCodMunicipioExpediente());
                ps.setString(i++,reg.getCodProcedimiento());
                ps.setString(i++,reg.getOrigenExpediente());
                ps.setTimestamp(i++,sqlTimestamp);

                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar la lista de anotaciones de registro relacionados con un expediente en la tabla E_EXR: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }
    
    public void grabarListaRegistroRelacionadoExterno(ArrayList<RegistroRelacionadoExternoVO> listaRegistrosRelExt,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        
        try{
            for (RegistroRelacionadoExternoVO regExterno : listaRegistrosRelExt) {
                String sql = "INSERT INTO E_EXREXT (EXREXT_UOR,EXREXT_TIP,EXREXT_EJR,EXREXT_NRE," + 
                    "EXREXT_MUN,EXREXT_NUM,EXREXT_ORI,EXREXT_TOP,EXREXT_SER,EXREXT_PRO," +
                    "EXREXT_FECALTA) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);

                ps= con.prepareStatement(sql);
                int i = 1;
            
                ps.setInt(i++,regExterno.getCodUnidadRegistro());
                ps.setString(i++,regExterno.getTipoAnotacionRegistro());
                ps.setString(i++,regExterno.getEjercicioAnotacionRegistro());
                ps.setLong(i++,regExterno.getNumAnotacionRegistro());
                ps.setInt(i++,regExterno.getCodMunicipioRegistro());
                ps.setString(i++,regExterno.getNumExpediente());
                ps.setString(i++,regExterno.getOrigenAnotacionRegistro());
                ps.setString(i++,regExterno.getTipoOperacion());
                ps.setString(i++,regExterno.getSerie());
                ps.setString(i++,regExterno.getCodProcedimiento());
                ps.setTimestamp(i++,DateOperations.toTimestamp(regExterno.getFechaAlta()));

                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar la lista de registros externos en la tabla E_EXREXT: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }
    
    public void grabarListaDocumentosTramitacion(ArrayList<DocumentoTramitacionVO> listaDocsTramitacion,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        
        try{
            for (DocumentoTramitacionVO documento : listaDocsTramitacion) {
                String sql = "INSERT INTO E_CRD (CRD_MUN,CRD_PRO,CRD_EJE,CRD_NUM,CRD_TRA,CRD_OCU," +
                    "CRD_NUD,CRD_FAL,CRD_FMO,CRD_USC,CRD_USM,CRD_FIL,CRD_DES,CRD_DOT,CRD_FIR_EST," +
                    "CRD_EXP_FD,CRD_DOC_FD,CRD_FIR_FD,CRD_FINF) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
                ps= con.prepareStatement(sql);
                int i = 1;
            
                ps.setInt(i++,documento.getCodMunicipio());
                ps.setString(i++,documento.getCodProcedimiento());
                ps.setInt(i++,documento.getEjercicio());
                ps.setString(i++,documento.getNumExpediente());
                ps.setInt(i++,documento.getCodTramite());
                ps.setInt(i++,documento.getOcurrenciaTramite());
                ps.setInt(i++,documento.getNumDocumento());
                ps.setTimestamp(i++,DateOperations.toTimestamp(documento.getFechaAlta()));
                ps.setTimestamp(i++,DateOperations.toTimestamp(documento.getFechaModificacion()));
                ps.setInt(i++,documento.getCodUsuarioAlta());
                ps.setInt(i++,documento.getCodUsuarioModificacion());
                ps.setBytes(i++,documento.getContenido());
                ps.setString(i++,documento.getNombreDocumento());
                ps.setInt(i++,documento.getCodDocumento());
                ps.setString(i++,documento.getEstadoFirma());
                ps.setString(i++,documento.getFd());
                ps.setString(i++,documento.getDocFd());
                if(documento.getFirFd()==null)
                    ps.setNull(i++,java.sql.Types.INTEGER);
                else
                    ps.setInt(i++,documento.getFirFd());
                ps.setTimestamp(i++,DateOperations.toTimestamp(documento.getFechaInforme()));
                
                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar la lista de documentos de tramitación de un expediente en la tabla E_CRD: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }
    
    public void grabarFirmaDocumentoTramitacion(ArrayList<DocumentoTramitacionFirmaVO> listaFirmasDocTram,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        
        try{
            for (DocumentoTramitacionFirmaVO firmaDocumento : listaFirmasDocTram) {
                byte [] contenido = HistoricoExpedienteDAO.getInstance().getContenidoFirmaDocumentoTramitacion(firmaDocumento, con);

                
                String sql = "INSERT INTO E_CRD_FIR (CRD_MUN,CRD_PRO,CRD_EJE,CRD_NUM,CRD_TRA," +
                    "CRD_OCU,CRD_NUD,USU_COD,FIR_EST,FIR,FX_FIRMA,OBSERV,USU_FIR,CRD_COD_PF_EXT," +
                    "CRD_ID_SOL_PF_EXT) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
                
                ps= con.prepareStatement(sql);
                int i = 1;
                            
                ps.setInt(i++,firmaDocumento.getCodMunicipio());
                ps.setString(i++,firmaDocumento.getCodProcedimiento());
                ps.setInt(i++,firmaDocumento.getEjercicio());
                ps.setString(i++,firmaDocumento.getNumExpediente());
                ps.setInt(i++,firmaDocumento.getCodTramite());
                ps.setInt(i++,firmaDocumento.getOcurrenciaTramite());
                ps.setInt(i++,firmaDocumento.getNumeroDocumento());
                ps.setInt(i++,firmaDocumento.getCodUsuario());
                ps.setString(i++,firmaDocumento.getEstadoFirma());
                ps.setBytes(i++,contenido);
                ps.setTimestamp(i++,DateOperations.toTimestamp(firmaDocumento.getFxFirma()));
                ps.setString(i++,firmaDocumento.getObservaciones());
                ps.setInt(i++,firmaDocumento.getCodUsuarioFirma());
                ps.setString(i++,firmaDocumento.getPfExt());
                ps.setString(i++,firmaDocumento.getSolPfExt());
                
                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar la firma de un documento de tramitación en la tabla E_CRD_FIR: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }
    
    public void grabarUsuFirmantesDocumentoTramitacion(ArrayList<DocumentoTramitacionFirmantesVO> listaFirmantes,Connection con) throws Exception{
        log.debug("ExpedienteDAO.grabarUsuFirmantesDocumentoTramitacion()::BEGIN");
        PreparedStatement ps = null;
        
        try{
            for (DocumentoTramitacionFirmantesVO firmaDocumento : listaFirmantes) {
                byte [] contenido = HistoricoExpedienteDAO.getInstance().getContenidoFirmaUsuFirmanteDocumentoTramitacion(firmaDocumento, con);

                
                String sql = "INSERT INTO E_CRD_FIR_FIRMANTES (COD_MUNICIPIO, COD_PROCEDIMIENTO, EJERCICIO, NUM_EXPEDIENTE, " +
						"COD_TRAMITE, COD_OCURRENCIA, COD_DOCUMENTO, ID_USUARIO, ORDEN, ESTADO_FIRMA, FECHA_FIRMA, FIRMA) " + 
						"VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
				
				StringBuilder trazaParams = new StringBuilder(" (TRAZA DE CONTROL) PARÁMETROS DE LA SQL: ")
						.append(firmaDocumento.getCodMunicipio()).append("-").append(firmaDocumento.getCodProcedimiento()).append("-")
						.append(firmaDocumento.getEjercicio()).append("-").append(firmaDocumento.getNumExpediente()).append("-")
						.append(firmaDocumento.getCodTramite()).append("-").append(firmaDocumento.getOcurrenciaTramite()).append("-")
						.append(firmaDocumento.getNumeroDocumento()).append("-").append(firmaDocumento.getIdUsuario()).append("-")
						.append(firmaDocumento.getOrden()).append("-").append(firmaDocumento.getEstadoFirma()).append("-")
						.append(firmaDocumento.getFechaFirma());
                log.debug(trazaParams);
                
                ps= con.prepareStatement(sql);
                int i = 1;
                            
                ps.setInt(i++,firmaDocumento.getCodMunicipio());
                ps.setString(i++,firmaDocumento.getCodProcedimiento());
                ps.setInt(i++,firmaDocumento.getEjercicio());
                ps.setString(i++,firmaDocumento.getNumExpediente());
                ps.setInt(i++,firmaDocumento.getCodTramite());
                ps.setInt(i++,firmaDocumento.getOcurrenciaTramite());
                ps.setInt(i++,firmaDocumento.getNumeroDocumento());
                ps.setInt(i++,firmaDocumento.getIdUsuario());
                ps.setInt(i++,firmaDocumento.getOrden());
                ps.setString(i++,firmaDocumento.getEstadoFirma());
                ps.setTimestamp(i++,DateOperations.toTimestamp(firmaDocumento.getFechaFirma()));
                ps.setBytes(i++,contenido);
                
                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar la firma de un usuario firmante de un documento de tramitación en la tabla E_CRD_FIR_FIRMANTES: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }
    
    public void grabarFirmaFlujoDocumentoTramitacion(ArrayList<DocumentoTramitacionFlujoVO> listaFlujoFirma,Connection con) throws Exception{
        log.debug("ExpedienteDAO.grabarFirmaFlujoDocumentoTramitacion()::BEGIN");
        PreparedStatement ps = null;
        
        try{
            for (DocumentoTramitacionFlujoVO firmaDocumento : listaFlujoFirma) {                
                String sql = "INSERT INTO E_CRD_FIR_FLUJO (COD_MUNICIPIO, COD_PROCEDIMIENTO, EJERCICIO, NUM_EXPEDIENTE, " +
						"COD_TRAMITE, COD_OCURRENCIA, COD_DOCUMENTO, ID_TIPO_FIRMA) " + 
                    "VALUES (?,?,?,?,?,?,?,?)";
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
                
				StringBuilder trazaParams = new StringBuilder(" (TRAZA DE CONTROL) PARÁMETROS DE LA SQL: ")
						.append(firmaDocumento.getCodMunicipio()).append("-").append(firmaDocumento.getCodProcedimiento()).append("-")
						.append(firmaDocumento.getEjercicio()).append("-").append(firmaDocumento.getNumExpediente()).append("-")
						.append(firmaDocumento.getCodTramite()).append("-").append(firmaDocumento.getOcurrenciaTramite()).append("-")
						.append(firmaDocumento.getNumeroDocumento()).append("-").append(firmaDocumento.getIdTipoFirma());
                log.debug(trazaParams);
				
                ps= con.prepareStatement(sql);
                int i = 1;
                            
                ps.setInt(i++,firmaDocumento.getCodMunicipio());
                ps.setString(i++,firmaDocumento.getCodProcedimiento());
                ps.setInt(i++,firmaDocumento.getEjercicio());
                ps.setString(i++,firmaDocumento.getNumExpediente());
                ps.setInt(i++,firmaDocumento.getCodTramite());
                ps.setInt(i++,firmaDocumento.getOcurrenciaTramite());
                ps.setInt(i++,firmaDocumento.getNumeroDocumento());
                ps.setLong(i++,firmaDocumento.getIdTipoFirma());
                
                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar el flujo de firma de un documento de tramitación en la tabla E_CRD_FIR_FLUJO: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }
    
    public void grabarSituacionExpedienteAnulado(SituacionExpedienteAnuladoVO situacExp,Connection con) throws Exception{
        
        PreparedStatement ps = null;
                
        try{
            String sql = "INSERT INTO E_EXPSIT (EXPSIT_NUM,EXPSIT_JUST,EXPSIT_USUARIO,EXPSIT_AUTORIZA," +
                "EXPSIT_MUN,EXPSIT_EJE) " + 
                "VALUES (?,?,?,?,?,?)";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps= con.prepareStatement(sql);
            int i = 1;

            ps.setString(i++,situacExp.getNumExpediente());
            ps.setString(i++,situacExp.getJustificacion());
            ps.setString(i++,situacExp.getUsuario());
            ps.setString(i++,situacExp.getAutoriza());
            ps.setInt(i++,situacExp.getCodMunicipio());
            ps.setInt(i++,situacExp.getEjercicio());

            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar la causa de anulación del expediente en la tabla E_EXPSIT: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }
    
    
    public void grabarOperaciones(ArrayList<OperacionExpedienteVO> operaciones,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        
        try{
            String sql = "INSERT INTO OPERACIONES_EXPEDIENTE (ID_OPERACION, COD_MUNICIPIO, EJERCICIO, " + 
                "NUM_EXPEDIENTE, TIPO_OPERACION, FECHA_OPERACION, COD_USUARIO, DESCRIPCION_OPERACION) " +
              "VALUES (?,?,?,?,?,?,?,?)";
            
            for (OperacionExpedienteVO operacion : operaciones) {
                log.error(" (TRAZA DE CONTROL) SQL: " + sql);
                ps= con.prepareStatement(sql);
                int i = 1;
            
                ps.setInt(i++,operacion.getIdOperacion());
                ps.setInt(i++,operacion.getCodMunicipio());
                ps.setInt(i++,operacion.getEjercicio());
                ps.setString(i++,operacion.getNumExpediente());
                ps.setInt(i++,operacion.getTipoOperacion());
                ps.setTimestamp(i++, DateOperations.toTimestamp(operacion.getFechaOperacion()));
                ps.setInt(i++, operacion.getCodUsuario());
                ps.setString(i++, operacion.getDescripcionOperacion());
                ps.executeUpdate();
            }
        }catch(SQLException e){
            log.error("Error al grabar la lista de operaciones de un expediente en la tabla OPERACIONES_EXPEDIENTE: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }
    
    public void grabarValoresDatosSuplementarios(ArrayList<ValorCampoSuplementarioVO> valoresDatosSup, 
            AdaptadorSQLBD oad,Connection con) throws Exception {        
        
        try{ 
            for(ValorCampoSuplementarioVO valorDato: valoresDatosSup) {
                if (valorDato.getTipoDatoSuplementario() == 1) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoNumerico(oad, con, valorDato);
                    } else {
                        insertarDatoNumericoTramite(oad, con, valorDato);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 2) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoTexto(oad, con, valorDato);
                    } else {
                        insertarDatoTextoTramite(oad, con, valorDato);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 3) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoFecha(oad, con, valorDato);
                    } else {
                        insertarDatoFechaTramite(oad, con, valorDato);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 4) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoTextoLargo(oad, con, valorDato);
                    } else {
                        insertarDatoTextoLargoTramite(oad, con, valorDato);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 5) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoFichero(oad, con, valorDato);
                    } else {
                        insertarDatoFicheroTramite(oad, con, valorDato);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 6) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoDesplegable(oad, con, valorDato);
                    } else {
                        insertarDatoDesplegableTramite(oad, con, valorDato);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 8) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoNumericoCal(oad, con, valorDato);
                    } else {
                        insertarDatoNumericoTramiteCal(oad, con, valorDato);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 9) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoFechaCal(oad, con, valorDato);
                    } else {
                        insertarDatoFechaTramiteCal(oad, con, valorDato);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 10) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoDesplegableExt(oad, con, valorDato);
                    } else {
                        insertarDatoDesplegableExtTramite(oad, con, valorDato);
                    }
                }
            }

        }catch(Exception e){
            log.error("Error al grabar el expediente en las tablas de expedientes activos: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }
        
    }
    
    public void insertarDatoNumerico(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor) throws Exception{
        
        Statement stmt = null;
                   
        try {
            String sql = "INSERT INTO E_TNU (TNU_MUN,TNU_EJE,TNU_NUM,TNU_COD,TNU_VALOR) " + 
                    "VALUES ("+ valor.getCodOrganizacion() + ","+ valor.getEjercicio() +",'" + valor.getNumExpediente() + 
                    "','"+valor.getCodDatoSuplementario() +"',"+ oad.convertir("'" + valor.getValorDatoSuplementario() + "'", 
                            oad.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            int i =1;
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException e){            
            log.error("Error al el valor de un campo numérico de expediente para " + valor.getNumExpediente() + ": " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(stmt!=null) stmt.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }

    public void insertarDatoNumericoTramite(AdaptadorSQLBD oad, 
        Connection con,ValorCampoSuplementarioVO valor) throws Exception{
    
        Statement stmt = null;
                   
        try {
            String sql = "INSERT INTO E_TNUT (TNUT_MUN,TNUT_PRO,TNUT_EJE,TNUT_NUM,TNUT_TRA,TNUT_OCU,TNUT_COD,TNUT_VALOR) " + 
                    "VALUES ("+ valor.getCodOrganizacion() + ",'" + valor.getProcedimiento() + "',"+ valor.getEjercicio() + ",'" + 
                    valor.getNumExpediente() + "'," + valor.getCodTramite() + "," + valor.getOcurrenciaTramite() + ",'" + 
                    valor.getCodDatoSuplementario()+"',"+ oad.convertir("'" + valor.getValorDatoSuplementario() + "'", 
                    oad.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);            
            
            int i =1;
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException e){
            log.error("Error al grabar el valor de un campo numérico de trámite del expediente " + valor.getNumExpediente()  + ": " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(stmt!=null) stmt.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }

    public void insertarDatoTexto(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor) throws Exception{
        PreparedStatement ps = null;
        
        try {
            
            String sql = "INSERT INTO E_TXT (TXT_MUN,TXT_EJE,TXT_NUM,TXT_COD,TXT_VALOR) " + 
                    "VALUES (?,?,?,?,?)";            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);            
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setString(i++,valor.getCodDatoSuplementario());
            ps.setString(i++,valor.getValorDatoSuplementario());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor en un campo suplementario de texto a nivel de expediente para " + valor.getNumExpediente() + ": " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }

    public void insertarDatoTextoTramite(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor) throws Exception{
        
        PreparedStatement ps = null;
        
        try {
            
            String sql = "INSERT INTO E_TXTT (TXTT_MUN,TXTT_PRO,TXTT_EJE,TXTT_NUM,TXTT_TRA,TXTT_OCU,TXTT_COD,TXTT_VALOR) " + 
                    "VALUES (?,?,?,?,?,?,?,?)";            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);            
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setString(i++,valor.getProcedimiento());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setInt(i++,valor.getCodTramite());
            ps.setInt(i++,valor.getOcurrenciaTramite());
            ps.setString(i++,valor.getCodDatoSuplementario());
            ps.setString(i++,valor.getValorDatoSuplementario());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor de un campo de tipo texto corto en un trámite del expediente " + valor.getNumExpediente() + ": " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }

    public void insertarDatoFecha(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor) throws Exception{
        
        PreparedStatement ps = null;
                
        String sql = "";

        try {
            sql = "INSERT INTO E_TFE (TFE_MUN,TFE_EJE,TFE_NUM,TFE_COD,TFE_VALOR,TFE_FEC_VENCIMIENTO,PLAZO_ACTIVADO) " + 
                    "VALUES (?,?,?,?,?,?,?)";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setString(i++,valor.getCodDatoSuplementario());
            
            if(valor.getValorDatoSuplementario()==null)
                ps.setNull(i++,java.sql.Types.TIMESTAMP);
            else
                ps.setTimestamp(i++,DateOperations.toTimestamp(valor.getValorDatoSuplementario()));

            if(valor.getValorFechaVencimiento()==null)
                ps.setNull(i++,java.sql.Types.TIMESTAMP);
            else
                ps.setTimestamp(i++,DateOperations.toTimestamp(valor.getValorFechaVencimiento()));

            ps.setInt(i++,Integer.parseInt(valor.getValorPlazoActivo()));                
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar un valor en un campo de tipo fecha a nivel de expediente para " + valor.getNumExpediente() + ": " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }

    public void insertarDatoFechaTramite(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor) throws Exception{

        PreparedStatement ps = null;
                
        String sql = "";

        try {
            sql = "INSERT INTO E_TFET (TFET_MUN,TFET_PRO,TFET_EJE,TFET_NUM,TFET_TRA,TFET_OCU,TFET_COD,TFET_VALOR,TFET_FEC_VENCIMIENTO,PLAZO_ACTIVADO) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?)";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setString(i++,valor.getProcedimiento());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setInt(i++,valor.getCodTramite());
            ps.setInt(i++,valor.getOcurrenciaTramite());
            ps.setString(i++,valor.getCodDatoSuplementario());
            
            if(valor.getValorDatoSuplementario()==null)
                ps.setNull(i++,java.sql.Types.TIMESTAMP);
            else
                ps.setTimestamp(i++,DateOperations.toTimestamp(valor.getValorDatoSuplementario()));

            if(valor.getValorFechaVencimiento()==null)
                ps.setNull(i++,java.sql.Types.TIMESTAMP);
            else
                ps.setTimestamp(i++,DateOperations.toTimestamp(valor.getValorFechaVencimiento()));

            ps.setInt(i++,Integer.parseInt(valor.getValorPlazoActivo()));                
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor en un campo suplementario de fecha de trámite para el expediente: " + valor.getNumExpediente() + ": " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }

    public void insertarDatoTextoLargo(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor) throws Exception{
        
        PreparedStatement ps = null;
        
        try {
            
            String sql = "INSERT INTO E_TTL (TTL_MUN,TTL_EJE,TTL_NUM,TTL_COD,TTL_VALOR) " + 
                    "VALUES (?,?,?,?,?)";            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);            
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setString(i++,valor.getCodDatoSuplementario());
            ps.setString(i++,valor.getValorDatoSuplementario());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor en un campo de tipo texto largo para el expediente " + valor.getNumExpediente() + ": " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }
    
    public void insertarDatoTextoLargoTramite(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor) throws Exception{
        
        PreparedStatement ps = null;
        
        try {
            String sql = "INSERT INTO E_TTLT (TTLT_MUN,TTLT_PRO,TTLT_EJE,TTLT_NUM,TTLT_TRA,TTLT_OCU,TTLT_COD,TTLT_VALOR) " + 
                    "VALUES (?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);        
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setString(i++,valor.getProcedimiento());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setInt(i++,valor.getCodTramite());
            ps.setInt(i++,valor.getOcurrenciaTramite());
            ps.setString(i++,valor.getCodDatoSuplementario());
            ps.setString(i++,valor.getValorDatoSuplementario());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor en un campo de texto largo para un trámite del expediente " + valor.getNumExpediente() + ":" + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }

    public void insertarDatoFichero(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor) throws Exception{
        
        byte [] contenido = HistoricoExpedienteDAO.getInstance().getContenidoFicheroCampoSuplementario(valor, con);
        
        PreparedStatement ps = null;
        
        try {
            String sql = "INSERT INTO E_TFI (TFI_MUN,TFI_EJE,TFI_NUM,TFI_COD,TFI_VALOR,TFI_MIME,TFI_NOMFICH,TFI_TAMANHO) " + 
                    "VALUES (?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);        
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setString(i++,valor.getCodDatoSuplementario());
            ps.setBytes(i++,contenido);
            ps.setString(i++,valor.getTipoMimeFichero());
            ps.setString(i++,valor.getValorDatoSuplementario());
            ps.setInt(i++,valor.getLongitudFichero());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el un archivo en un campo de tipo fichero a nivel de expediente " +  valor.getNumExpediente() + ": " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }

      public void insertarDatoFicheroTramite(AdaptadorSQLBD oad, Connection con, 
             ValorCampoSuplementarioVO valor) throws Exception{
        
        byte [] contenido = HistoricoExpedienteDAO.getInstance().getContenidoFicheroCampoSuplementarioTramite(valor, con);
        
        PreparedStatement ps = null;
        
        try {
            String sql = "INSERT INTO E_TFIT (TFIT_MUN,TFIT_PRO,TFIT_EJE,TFIT_NUM,TFIT_TRA,TFIT_OCU," + 
                    "TFIT_COD,TFIT_VALOR,TFIT_MIME,TFIT_NOMFICH,TFIT_TAMANHO,TFIT_ORIGEN) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);  
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setString(i++,valor.getProcedimiento());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setInt(i++,valor.getCodTramite());
            ps.setInt(i++,valor.getOcurrenciaTramite());
            ps.setString(i++,valor.getCodDatoSuplementario());
            ps.setBytes(i++,contenido);
            ps.setString(i++,valor.getTipoMimeFichero());
            ps.setString(i++,valor.getValorDatoSuplementario());
            ps.setInt(i++,valor.getLongitudFichero());
            ps.setString(i++,valor.getOrigenFichero());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar un archivo en un campo de tipo fichero definido a nivel de trámite del expediente " + valor.getNumExpediente() + ":" + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }
     
    public void insertarDatoDesplegable(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor) throws Exception{
        
        PreparedStatement ps = null;
        
        try {
            
            String sql = "INSERT INTO E_TDE (TDE_MUN,TDE_EJE,TDE_NUM,TDE_COD,TDE_VALOR) " + 
                    "VALUES (?,?,?,?,?)";            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);  
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setString(i++,valor.getCodDatoSuplementario());
            ps.setString(i++,valor.getValorDatoSuplementario());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar un valor en un campo de tipo desplegable del expediente " + valor.getNumExpediente() + ":" + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }

    public void insertarDatoDesplegableTramite(AdaptadorSQLBD oad, Connection con, 
        ValorCampoSuplementarioVO valor) throws Exception{
        
        PreparedStatement ps = null;
        
        try {
            String sql = "INSERT INTO E_TDET (TDET_MUN,TDET_PRO,TDET_EJE,TDET_NUM,TDET_TRA,TDET_OCU,TDET_COD,TDET_VALOR) " + 
                    "VALUES (?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);  
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setString(i++,valor.getProcedimiento());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setInt(i++,valor.getCodTramite());
            ps.setInt(i++,valor.getOcurrenciaTramite());
            ps.setString(i++,valor.getCodDatoSuplementario());
            ps.setString(i++,valor.getValorDatoSuplementario());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor de un campo suplementario de tipo desplegable en un trámite del expediente " + valor.getNumExpediente() + ":" + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }

    public void insertarDatoNumericoCal(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor) throws Exception{
        
        Statement stmt = null;
                   
        try {
            String sql = "INSERT INTO E_TNUC (TNUC_MUN,TNUC_EJE,TNUC_NUM,TNUC_COD,TNUC_VALOR) " + 
                    "VALUES ("+ valor.getCodOrganizacion() + ","+ valor.getEjercicio() +",'" + valor.getNumExpediente() + 
                    "','"+valor.getCodDatoSuplementario() +"',"+ oad.convertir("'" + valor.getValorDatoSuplementario() + "'", 
                            oad.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);  
            
            int i =1;
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException e){
            log.error("Error al grabar un valor en un campo suplementario numérico calculado en el expediente " + valor.getNumExpediente() + ":" + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(stmt!=null) stmt.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }     
    
    public void insertarDatoNumericoTramiteCal(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor) throws Exception{

        Statement stmt = null;
                   
        try {
            String sql = "INSERT INTO E_TNUCT (TNUCT_MUN,TNUCT_PRO,TNUCT_EJE,TNUCT_NUM,TNUCT_TRA,TNUCT_OCU,TNUCT_COD,TNUCT_VALOR) " + 
                    "VALUES ("+ valor.getCodOrganizacion() + ",'" + valor.getProcedimiento() + "',"+ valor.getEjercicio() + ",'" + 
                    valor.getNumExpediente() + "'," + valor.getCodTramite() + "," + valor.getOcurrenciaTramite() + ",'" + 
                    valor.getCodDatoSuplementario()+"',"+ oad.convertir("'" + valor.getValorDatoSuplementario() + "'", 
                    oad.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);  
            int i =1;
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException e){
            log.error("Error al grabar un valor en un campo suplementario numérico calculado de un trámite del expediente " + valor.getNumExpediente() + ":" + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(stmt!=null) stmt.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }

    public void insertarDatoFechaCal(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor) throws Exception{
        
        PreparedStatement ps = null;
                
        String sql = "";

        try {
            sql = "INSERT INTO E_TFEC (TFEC_MUN,TFEC_EJE,TFEC_NUM,TFEC_COD,TFEC_VALOR,TFEC_FEC_VENCIMIENTO,PLAZO_ACTIVADO) " + 
                    "VALUES (?,?,?,?,?,?,?)";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);  
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setString(i++,valor.getCodDatoSuplementario());
            
            if(valor.getValorDatoSuplementario()==null)
                ps.setNull(i++,java.sql.Types.TIMESTAMP);
            else
                ps.setTimestamp(i++,DateOperations.toTimestamp(valor.getValorDatoSuplementario()));

            if(valor.getValorFechaVencimiento()==null)
                ps.setNull(i++,java.sql.Types.TIMESTAMP);
            else
                ps.setTimestamp(i++,DateOperations.toTimestamp(valor.getValorFechaVencimiento()));

            ps.setInt(i++,Integer.parseInt(valor.getValorPlazoActivo()));                
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar un valor en un campo de tipo fecha calculado del expediente " + valor.getNumExpediente() + ":" + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }

    public void insertarDatoFechaTramiteCal(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor) throws Exception{
        
        PreparedStatement ps = null;
                
        String sql = "";

        try {
            sql = "INSERT INTO E_TFECT (TFECT_MUN,TFECT_PRO,TFECT_EJE,TFECT_NUM,TFECT_TRA," + 
                    "TFECT_OCU,TFECT_COD,TFECT_VALOR,TFECT_FEC_VENCIMIENTO,PLAZO_ACTIVADO) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?)";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setString(i++,valor.getProcedimiento());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setInt(i++,valor.getCodTramite());
            ps.setInt(i++,valor.getOcurrenciaTramite());
            ps.setString(i++,valor.getCodDatoSuplementario());
            
            if(valor.getValorDatoSuplementario()==null)
                ps.setNull(i++,java.sql.Types.TIMESTAMP);
            else
                ps.setTimestamp(i++,DateOperations.toTimestamp(valor.getValorDatoSuplementario()));

            if(valor.getValorFechaVencimiento()==null)
                ps.setNull(i++,java.sql.Types.TIMESTAMP);
            else
                ps.setTimestamp(i++,DateOperations.toTimestamp(valor.getValorFechaVencimiento()));

            ps.setInt(i++,Integer.parseInt(valor.getValorPlazoActivo()));                
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar un valor en un campo de fecha calculado de un trámite del expediente " + valor.getNumExpediente() + ":" + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }

    public void insertarDatoDesplegableExt(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor) throws Exception{

        PreparedStatement ps = null;
        
        try {
            
            String sql = "INSERT INTO E_TDEX (TDEX_MUN,TDEX_EJE,TDEX_NUM,TDEX_COD,TDEX_VALOR, TDEX_CODSEL) " + 
                    "VALUES (?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setString(i++,valor.getCodDatoSuplementario());
            ps.setString(i++,valor.getValorDatoSuplementario());
            ps.setString(i++,valor.getCodValorDatoSuplementario());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor de un campo desplegable externo en el expediente " + valor.getNumExpediente() + ": " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }
    
    public void insertarDatoDesplegableExtTramite(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor) throws Exception{

        PreparedStatement ps = null;
        
        try {
            String sql = "INSERT INTO E_TDEXT (TDEXT_MUN,TDEXT_PRO,TDEXT_EJE,TDEXT_NUM,TDEXT_TRA," + 
                    "TDEXT_OCU,TDEXT_COD,TDEXT_VALOR,TDEXT_CODSEL) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?)";            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setString(i++,valor.getProcedimiento());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setInt(i++,valor.getCodTramite());
            ps.setInt(i++,valor.getOcurrenciaTramite());
            ps.setString(i++,valor.getCodDatoSuplementario());
            ps.setString(i++,valor.getValorDatoSuplementario());
            ps.setString(i++,valor.getCodValorDatoSuplementario());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor de un campo desplegable externo en un trámite del expediente " + valor.getNumExpediente() + ":" + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    
    public ArrayList<ValorCampoSuplementarioVO> getValoresDatosSuplementarios(int codMunicipio, String numero, 
            AdaptadorSQLBD oad,Connection con) throws Exception {        
        log.debug("ExpedienteDAO.getValoresDatosSuplementarios()::BEGIN");
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();
		
        resultado.addAll(getValoresNumericos(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresNumericosTramite(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresTexto(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresTextoTramite(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresFecha(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresFechaTramite(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresTextoLargo(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresTextoLargoTramite(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresFichero(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresFicheroTramite(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresDesplegable(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresDesplegableTramite(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresNumericosCal(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresNumericosTramiteCal(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresFechaCal(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresFechaTramiteCal(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresDesplegableExt(oad, con, codMunicipio, numero));
        resultado.addAll(getValoresDesplegableExtTramite(oad, con, codMunicipio, numero));

        return resultado; 
    }

    public ArrayList<ValorCampoSuplementarioVO> getValoresNumericos(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();

        try { 
            
            String[] datos = numero.split("/");
            String ejercicio = datos[0];
            
            
            String sql = "SELECT TNU_EJE,TNU_COD," + oad.convertir("TNU_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null) + " AS TNU_VALOR " +
                         "FROM E_TNU WHERE TNU_MUN =? AND TNU_NUM =? AND TNU_EJE=?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,codMunicipio);
            ps.setString(i++,numero);           
            ps.setInt(i++,Integer.parseInt(ejercicio));
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(1);
                valor.setCodOrganizacion(codMunicipio);
                valor.setEjercicio(rs.getInt("TNU_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodDatoSuplementario(rs.getString("TNU_COD"));
                valor.setValorDatoSuplementario(rs.getString(3));
                
                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos numéricos definidos a nivel de expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return resultado;
    }

    public ArrayList<ValorCampoSuplementarioVO> getValoresNumericosTramite(AdaptadorSQLBD oad, 
        Connection con, int codMunicipio, String numero) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();

        try {
            String sql = "SELECT TNUT_PRO,TNUT_EJE,TNUT_TRA,TNUT_OCU,TNUT_COD,"
                    + oad.convertir("TNUT_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null)
                    + " AS TNUT_VALOR " +
                    "FROM E_TNUT WHERE TNUT_MUN =? AND TNUT_NUM =?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,codMunicipio);
            ps.setString(i++,numero);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(1);
                valor.setCodOrganizacion(codMunicipio);
                valor.setProcedimiento(rs.getString("TNUT_PRO"));
                valor.setEjercicio(rs.getInt("TNUT_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodTramite(rs.getInt("TNUT_TRA"));
                valor.setOcurrenciaTramite(rs.getInt("TNUT_OCU"));
                valor.setCodDatoSuplementario(rs.getString("TNUT_COD"));
                valor.setValorDatoSuplementario(rs.getString(6));
                
                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de campos numéricos a nivel de trámite del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return resultado;
    }

    public ArrayList<ValorCampoSuplementarioVO> getValoresTexto(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();

        try {
            String[] datos = numero.split("/");
            String ejercicio = datos[0];
            
            String sql = "SELECT TXT_EJE,TXT_COD,TXT_VALOR " + 
                         "FROM E_TXT WHERE TXT_MUN = ? AND TXT_NUM = ? AND TXT_EJE=?";            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i= 1;
            ps = con.prepareStatement(sql);            
            ps.setInt(i++,codMunicipio);
            ps.setString(i++,numero);
            ps.setInt(i++,Integer.parseInt(ejercicio));            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(2);
                valor.setCodOrganizacion(codMunicipio);
                valor.setEjercicio(rs.getInt("TXT_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodDatoSuplementario(rs.getString("TXT_COD"));
                valor.setValorDatoSuplementario(rs.getString("TXT_VALOR"));

                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos de tipo texto a nivel de expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }

        return resultado;
    }

    public ArrayList<ValorCampoSuplementarioVO> getValoresTextoTramite(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();

        try {
            
            String sql = "SELECT TXTT_PRO,TXTT_EJE,TXTT_TRA,TXTT_OCU,TXTT_COD,TXTT_VALOR " + 
                "FROM E_TXTT WHERE TXTT_MUN  = ? AND txtt_num =?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,codMunicipio);
            ps.setString(i++,numero);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(2);
                valor.setCodOrganizacion(codMunicipio);
                valor.setProcedimiento(rs.getString("TXTT_PRO"));
                valor.setEjercicio(rs.getInt("TXTT_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodTramite(rs.getInt("TXTT_TRA"));
                valor.setOcurrenciaTramite(rs.getInt("TXTT_OCU"));
                valor.setCodDatoSuplementario(rs.getString("TXTT_COD"));
                valor.setValorDatoSuplementario(rs.getString("TXTT_VALOR"));
                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos suplementarios de tipo texto definidos a nivel de trámite del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps!= null) {
                    ps.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }

        return resultado;
    }

    public ArrayList<ValorCampoSuplementarioVO> getValoresFecha(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();
        String sql = "";

        try {
            sql = "SELECT TFE_EJE,TFE_COD," + oad.convertir("TFE_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + 
                " AS TFE_VALOR_E," + oad.convertir("TFE_FEC_VENCIMIENTO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + 
                " AS TFE_FEC_VENCIMIENTO_E" + ",PLAZO_ACTIVADO ";
            sql += " FROM E_TFE WHERE tfe_mun =? AND tfe_num =?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,codMunicipio);
            ps.setString(i++,numero);
            
            if (log.isDebugEnabled()) {
                log.debug(sql);
            }
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(3);
                valor.setCodOrganizacion(codMunicipio);
                valor.setEjercicio(rs.getInt("TFE_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodDatoSuplementario(rs.getString("TFE_COD"));
                valor.setValorDatoSuplementario(rs.getString(3));
                valor.setValorFechaVencimiento(rs.getString(4));
                valor.setValorPlazoActivo(String.valueOf(rs.getInt("PLAZO_ACTIVADO")));
                
                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos de tipo fecha definidos a nivel del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return resultado;
    }

    public ArrayList<ValorCampoSuplementarioVO> getValoresFechaTramite(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();
        String sql = "";
        
        try {
            sql = "SELECT TFET_PRO,TFET_EJE,TFET_TRA,TFET_OCU,TFET_COD," +  
                oad.convertir("TFET_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFET_VALOR," + 
                oad.convertir("TFET_FEC_VENCIMIENTO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + 
                " AS TFET_FEC_VENCIMIENTO,PLAZO_ACTIVADO " + 
                "FROM E_TFET WHERE tfet_mun =? AND tfet_num =?";

            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i =1;
            ps = con.prepareStatement(sql);            
            ps.setInt(i++,codMunicipio);
            ps.setString(i++,numero);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(3);
                valor.setCodOrganizacion(codMunicipio);
                valor.setProcedimiento(rs.getString("TFET_PRO"));
                valor.setEjercicio(rs.getInt("TFET_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodTramite(rs.getInt("TFET_TRA"));
                valor.setOcurrenciaTramite(rs.getInt("TFET_OCU"));
                valor.setCodDatoSuplementario(rs.getString("TFET_COD"));
                valor.setValorDatoSuplementario(rs.getString(6));
                valor.setValorFechaVencimiento(rs.getString(7));
                valor.setValorPlazoActivo(String.valueOf(rs.getInt("PLAZO_ACTIVADO")));
                
                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos de tipo fecha de trámite del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return resultado;
    }

    public ArrayList<ValorCampoSuplementarioVO> getValoresTextoLargo(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();

        try {
            String sql = "SELECT TTL_EJE,TTL_COD,TTL_VALOR FROM E_TTL WHERE ttl_mun = ? AND ttl_num =?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i=1;
            pst = con.prepareStatement(sql);
            pst.setInt(i++,codMunicipio);
            pst.setString(i++,numero);            
            rs = pst.executeQuery();
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(4);
                valor.setCodOrganizacion(codMunicipio);
                valor.setEjercicio(rs.getInt("TTL_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodDatoSuplementario(rs.getString("TTL_COD"));
                valor.setValorDatoSuplementario(rs.getString("TTL_VALOR"));
                
                resultado.add(valor);
            }

        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos de tipo texto largo definidos a nivel del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return resultado;
    }
    
    public ArrayList<ValorCampoSuplementarioVO> getValoresTextoLargoTramite(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();

        try {
            String sql = "SELECT TTLT_PRO,TTLT_EJE,TTLT_TRA,TTLT_OCU,TTLT_COD,TTLT_VALOR " + 
                    "FROM E_TTLT WHERE ttlt_mun = ? AND ttlt_num =?";

            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i=1;
            pst = con.prepareStatement(sql);
            pst.setInt(i++,codMunicipio);
            pst.setString(i++,numero);
            
            rs = pst.executeQuery();
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(4);
                valor.setCodOrganizacion(codMunicipio);
                valor.setProcedimiento(rs.getString("TTLT_PRO"));
                valor.setEjercicio(rs.getInt("TTLT_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodTramite(rs.getInt("TTLT_TRA"));
                valor.setOcurrenciaTramite(rs.getInt("TTLT_OCU"));
                valor.setCodDatoSuplementario(rs.getString("TTLT_COD"));
                valor.setValorDatoSuplementario(rs.getString("TTLT_VALOR"));
                
                resultado.add(valor);
            }

        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos de tipo texto largo definidos a nivel trámite del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return resultado;
    }

    public ArrayList<ValorCampoSuplementarioVO> getValoresFichero(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();

        try {
            String sql = "SELECT TFI_EJE,TFI_COD,TFI_MIME,TFI_NOMFICH,TFI_TAMANHO " +
                "FROM E_TFI WHERE tfi_mun =? AND tfi_num =?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,codMunicipio);
            ps.setString(i++,numero);            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(5);
                valor.setCodOrganizacion(codMunicipio);
                valor.setEjercicio(rs.getInt("TFI_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodDatoSuplementario(rs.getString("TFI_COD"));
                valor.setTipoMimeFichero(rs.getString("TFI_MIME"));
                valor.setValorDatoSuplementario(rs.getString("TFI_NOMFICH"));
                valor.setLongitudFichero(rs.getInt("TFI_TAMANHO"));                
                resultado.add(valor);
            }
            
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos de tipo fichero definidos a nivel del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return resultado;
    }

      public ArrayList<ValorCampoSuplementarioVO> getValoresFicheroTramite(AdaptadorSQLBD oad, Connection con, 
              int codMunicipio, String numero) {
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();

        try {
            String sql = "SELECT TFIT_PRO,TFIT_EJE,TFIT_TRA,TFIT_OCU,TFIT_COD,TFIT_MIME," + 
                    "TFIT_NOMFICH,TFIT_TAMANHO,TFIT_ORIGEN " + 
                    "FROM E_TFIT WHERE tfit_mun =? AND tfit_num =?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,codMunicipio);
            ps.setString(i++,numero);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(5);
                valor.setCodOrganizacion(codMunicipio);
                valor.setProcedimiento(rs.getString("TFIT_PRO"));
                valor.setEjercicio(rs.getInt("TFIT_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodTramite(rs.getInt("TFIT_TRA"));
                valor.setOcurrenciaTramite(rs.getInt("TFIT_OCU"));
                valor.setCodDatoSuplementario(rs.getString("TFIT_COD"));
                valor.setTipoMimeFichero(rs.getString("TFIT_MIME"));
                valor.setValorDatoSuplementario(rs.getString("TFIT_NOMFICH"));
                valor.setLongitudFichero(rs.getInt("TFIT_TAMANHO"));
                valor.setOrigenFichero(rs.getString("TFIT_ORIGEN"));
                              
                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos de tipo fichero definidos a nivel de trámite del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                    log.error("Excepcion capturada en: " + getClass().getName());
            }
        }

        return resultado;
    }
     
    public ArrayList<ValorCampoSuplementarioVO> getValoresDesplegable(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();

        try {
            
            String[] datos = numero.split("/");
            String ejercicio = datos[0];
            String sql = "SELECT TDE_EJE,TDE_COD,TDE_VALOR FROM E_TDE WHERE tde_mun=? AND tde_num =? AND tde_eje=?";            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,codMunicipio);
            ps.setString(i++,numero);
            ps.setInt(i++,Integer.parseInt(ejercicio));
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(6);
                valor.setCodOrganizacion(codMunicipio);
                valor.setEjercicio(rs.getInt("TDE_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodDatoSuplementario(rs.getString("TDE_COD"));
                valor.setValorDatoSuplementario(rs.getString("TDE_VALOR"));

                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos de tipo desplegado definidos a nivel del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return resultado;
    }

    public ArrayList<ValorCampoSuplementarioVO> getValoresDesplegableTramite(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();

        try {
            String sql = "SELECT TDET_PRO,TDET_EJE,TDET_TRA,TDET_OCU,TDET_COD,TDET_VALOR "+
                "FROM E_TDET WHERE tdet_mun =? AND tdet_num =?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,codMunicipio);
            ps.setString(i++,numero);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(6);
                valor.setCodOrganizacion(codMunicipio);
                valor.setProcedimiento(rs.getString("TDET_PRO"));
                valor.setEjercicio(rs.getInt("TDET_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodTramite(rs.getInt("TDET_TRA"));
                valor.setOcurrenciaTramite(rs.getInt("TDET_OCU"));
                valor.setCodDatoSuplementario(rs.getString("TDET_COD"));
                valor.setValorDatoSuplementario(rs.getString("TDET_VALOR"));
                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos de tipo desplegado definidos a nivel de trámite del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
                
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }

        return resultado;
    }

    public ArrayList<ValorCampoSuplementarioVO> getValoresNumericosCal(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {
        Statement st = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();

        try {
            st = con.createStatement();
            String sql = "SELECT TNUC_EJE,TNUC_COD," + 
                oad.convertir("TNUC_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null) + " AS TNUC_VALOR " + 
                "FROM E_TNUC WHERE tnuc_mun  = " + codMunicipio + " AND tnuc_num ='" + numero + "'";

            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            rs = st.executeQuery(sql);
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(8);
                valor.setCodOrganizacion(codMunicipio);
                valor.setEjercicio(rs.getInt("TNUC_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodDatoSuplementario(rs.getString("TNUC_COD"));
                valor.setValorDatoSuplementario(rs.getString(3));
                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos númerico de tipo calculado definidos a nivel del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return resultado;
    }       
    
    public ArrayList<ValorCampoSuplementarioVO> getValoresNumericosTramiteCal(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {
        Statement st = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();

        try {
            st = con.createStatement();
           
            String sql = "SELECT TNUCT_PRO,TNUCT_EJE,TNUCT_TRA,TNUCT_OCU,TNUCT_COD," +
                oad.convertir("TNUCT_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null) + " AS TNUCT_VALOR "+
                "FROM E_TNUCT WHERE tnuct_mun = " + codMunicipio + " AND tnuct_num ='" + numero + "'";

            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            rs = st.executeQuery(sql);
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(8);
                valor.setCodOrganizacion(codMunicipio);
                valor.setProcedimiento(rs.getString("TNUCT_PRO"));
                valor.setEjercicio(rs.getInt("TNUCT_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodTramite(rs.getInt("TNUCT_TRA"));
                valor.setOcurrenciaTramite(rs.getInt("TNUCT_OCU"));
                valor.setCodDatoSuplementario(rs.getString("TNUCT_COD"));
                valor.setValorDatoSuplementario(rs.getString(6));
                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos númerico de tipo calculado definidos a nivel de trámite del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }

        return resultado;
    }

    public ArrayList<ValorCampoSuplementarioVO> getValoresFechaCal(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {
        Statement st = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();
        String sql = "";

        try {
            st = con.createStatement();

            sql = "SELECT TFEC_EJE,TFEC_COD," + 
                oad.convertir("TFEC_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFEC_VALOR," + 
                oad.convertir("TFEC_FEC_VENCIMIENTO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFEC_FEC_VENCIMIENTO," +
                "PLAZO_ACTIVADO " +
                "FROM E_TFEC WHERE tfec_mun = " + codMunicipio + " AND tfec_num ='" + numero + "'";

            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            rs = st.executeQuery(sql);
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(9);
                valor.setCodOrganizacion(codMunicipio);
                valor.setEjercicio(rs.getInt("TFEC_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodDatoSuplementario(rs.getString("TFEC_COD"));
                valor.setValorDatoSuplementario(rs.getString(3));
                valor.setValorFechaVencimiento(rs.getString(4));
                valor.setValorPlazoActivo(String.valueOf(rs.getInt("PLAZO_ACTIVADO")));
                
                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos fecha de tipo calculado definidos a nivel del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return resultado;
    }

    public ArrayList<ValorCampoSuplementarioVO> getValoresFechaTramiteCal(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {
        
        Statement st = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();
        String sql = "";

        try {
            st = con.createStatement();
            
            sql = "SELECT TFECT_PRO,TFECT_EJE,TFECT_TRA,TFECT_OCU,TFECT_COD," + 
                    oad.convertir("TFECT_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFECT_VALOR," + 
                    oad.convertir("TFECT_FEC_VENCIMIENTO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS TFECT_FEC_VENCIMIENTO," + 
                    "PLAZO_ACTIVADO " + 
                    "FROM E_TFECT WHERE tfect_mun = " + codMunicipio + " AND tfect_num ='" + numero + "'";

            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            rs = st.executeQuery(sql);
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(9);
                valor.setCodOrganizacion(codMunicipio);
                valor.setProcedimiento(rs.getString("TFECT_PRO"));
                valor.setEjercicio(rs.getInt("TFECT_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodTramite(rs.getInt("TFECT_TRA"));
                valor.setOcurrenciaTramite(rs.getInt("TFECT_OCU"));
                valor.setCodDatoSuplementario(rs.getString("TFECT_COD"));
                valor.setValorDatoSuplementario(rs.getString(6));
                valor.setValorFechaVencimiento(rs.getString(7));
                valor.setValorPlazoActivo(String.valueOf(rs.getInt("PLAZO_ACTIVADO")));
                
                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos fecha de tipo calculado definidos a nivel de trámite del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }

        return resultado;
    }

    public ArrayList<ValorCampoSuplementarioVO> getValoresDesplegableExt(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {

        Statement st = null;
        ResultSet rs = null;
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();

        try {
            st = con.createStatement();

            String sql = "SELECT TDEX_EJE,TDEX_COD,TDEX_VALOR, TDEX_CODSEL FROM E_TDEX WHERE TDEX_MUN = " + codMunicipio +
                    " AND TDEX_NUM ='" + numero + "'";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            rs = st.executeQuery(sql);            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(10);
                valor.setCodOrganizacion(codMunicipio);
                valor.setEjercicio(rs.getInt("TDEX_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodDatoSuplementario(rs.getString("TDEX_COD"));
                valor.setValorDatoSuplementario(rs.getString("TDEX_VALOR"));
                valor.setCodValorDatoSuplementario(rs.getString("TDEX_CODSEL"));
                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos desplegables de tipo externo definidos a nivel del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }

        return resultado;
    }
    
    public ArrayList<ValorCampoSuplementarioVO> getValoresDesplegableExtTramite(AdaptadorSQLBD oad, Connection con, 
            int codMunicipio, String numero) {

        Statement st = null;
        ResultSet rs = null;
        
        ArrayList<ValorCampoSuplementarioVO> resultado = new ArrayList<ValorCampoSuplementarioVO>();

        try {
            st = con.createStatement();

            String sql = "SELECT TDEXT_PRO,TDEXT_EJE,TDEXT_TRA,TDEXT_OCU,TDEXT_COD,TDEXT_VALOR, TDEXT_CODSEL "+
                    "FROM E_TDEXT WHERE TDEXT_MUN = " + codMunicipio + " AND TDEXT_NUM ='" + numero +"'";

            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            rs = st.executeQuery(sql);
            
            while (rs.next()) {
                ValorCampoSuplementarioVO valor = new ValorCampoSuplementarioVO();
                valor.setTipoDatoSuplementario(10);
                valor.setCodOrganizacion(codMunicipio);
                valor.setProcedimiento(rs.getString("TDEXT_PRO"));
                valor.setEjercicio(rs.getInt("TDEXT_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodTramite(rs.getInt("TDEXT_TRA"));
                valor.setOcurrenciaTramite(rs.getInt("TDEXT_OCU"));
                valor.setCodDatoSuplementario(rs.getString("TDEXT_COD"));
                valor.setValorDatoSuplementario(rs.getString("TDEXT_VALOR"));
                valor.setCodValorDatoSuplementario(rs.getString("TDEXT_CODSEL"));
                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de los campos desplegables de tipo externo definidos a nivel de trámite del expediente " + numero + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }

        return resultado;
    }
    
    public byte[] getContenidoFicheroCampoSuplementario(ValorCampoSuplementarioVO valor, Connection con) throws Exception {
    
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[] resultado = null;
        
        try{
            String sql = "SELECT TFI_VALOR FROM E_TFI WHERE TFI_MUN = ? AND " +
                    "TFI_EJE = ? AND TFI_NUM = ? AND TFI_COD = ?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setString(i++,valor.getCodDatoSuplementario());
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                resultado = rs.getBytes("TFI_VALOR");
            }
        }catch(SQLException e){
            log.error("Error al recuperar los valores de los campos fichero definidos a nivel del expediente " + valor.getNumExpediente() + ": " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(rs!=null) rs.close();                
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
            
    public byte[] getContenidoFicheroCampoSuplementarioTramite(ValorCampoSuplementarioVO valor, Connection con) throws Exception {
    
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[] resultado = null;
        
        try{
            String sql = "SELECT TFIT_VALOR FROM E_TFIT WHERE TFIT_MUN = ? AND TFIT_PRO = ? AND " +
                    "TFIT_EJE = ? AND TFIT_NUM = ? AND TFIT_TRA = ? AND TFIT_OCU = ? AND TFIT_COD = ?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setString(i++,valor.getProcedimiento());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setInt(i++,valor.getCodTramite());
            ps.setInt(i++,valor.getOcurrenciaTramite());
            ps.setString(i++,valor.getCodDatoSuplementario());
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                resultado = rs.getBytes("TFIT_VALOR");
            }
        }catch(SQLException e){
            log.error("Error al recuperar los valores de los campos fichero definidos a nivel trámite del expediente " + valor.getNumExpediente() + ": " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(rs!=null) rs.close();                
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
            
    
    
    
     /**
     * Comprueba si un determinado expediente está activo, es decir, si está en la tabla E_EXP.
     * Sino lo está, entonces es que está en la tabla HIST_E_EXP
     * @param codOrganizacion
     * @param ejercicio
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean estaExpedienteActivo(int codOrganizacion,int ejercicio, String numExpediente, 
            Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs  = null;
        boolean exito = false;
        int numero = 0;
        
        try{
            String sql = "SELECT COUNT(*) AS NUM " + 
                         "FROM E_EXP WHERE EXP_MUN = ? AND EXP_EJE = ? AND EXP_NUM = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setInt(i++,ejercicio);
            ps.setString(i++,numExpediente);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                numero = rs.getInt("NUM");
            }
            
            if(numero>=1) exito = true;
            
        }catch(SQLException e){
            log.error("Error al recuperar el expediente de la tabla de expedientes activos: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
                if(rs!=null) rs.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return exito;
    }
    
    
}
