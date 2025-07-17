/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.historico.expedientes.dao; 

import es.altia.agora.business.sge.OperacionExpedienteVO;
import es.altia.agora.business.sge.ValorCampoSuplementarioVO;
import es.altia.agora.business.sge.persistence.manual.OperacionesExpedienteDAO;
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
import es.altia.flexia.historico.expediente.vo.ExpedienteEnvioHistoricoVO;
import es.altia.flexia.historico.expediente.vo.ExpedienteVO;
import es.altia.flexia.historico.expediente.vo.InteresadoExpedienteVO;
import es.altia.flexia.historico.expediente.vo.ListTramOrigVO;
import es.altia.flexia.historico.expediente.vo.NotificacionVO;
import es.altia.flexia.historico.expediente.vo.NotificacionIndividualVO;
import es.altia.flexia.historico.expediente.vo.RegistroRelacionadoExternoVO;
import es.altia.flexia.historico.expediente.vo.RegistroRelacionadoVO;
import es.altia.flexia.historico.expediente.vo.SituacionExpedienteAnuladoVO;
import es.altia.util.conexion.BDException;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.jdbc.JdbcOperations;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author santiagoc
 */
public class HistoricoExpedienteDAO
{
    //Logger
    private static Logger log = Logger.getLogger(HistoricoExpedienteDAO.class);
    
    //Instancia
    private static HistoricoExpedienteDAO instance = null;
    private Config m_CommonProperties; // Para el fichero de contantes
    private Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
    private Config m_ConfigError; // Para los mensajes de error localizados
    private ExpedienteDAO expDAO;
    
    private HistoricoExpedienteDAO()
    {   
        m_CommonProperties = ConfigServiceHelper.getConfig("common");
        // Queremos usar el	fichero de configuración technical
        m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError	= ConfigServiceHelper.getConfig("error");
        expDAO = ExpedienteDAO.getInstance();
    }
    
    public static HistoricoExpedienteDAO getInstance()
    {
        if(instance == null)
        {
            synchronized(HistoricoExpedienteDAO.class)
            {
                instance = new HistoricoExpedienteDAO();
            }
        }
        return instance;
    }   
    
    /**
     * Grabar el contenido del objeto de la clase ExpedienteVO en la tabla HIST_E_EXP
     * 
     * @param exp
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarExpedienteHistorico(ExpedienteVO exp, long idProceso, Connection con) throws Exception {
        log.debug("HistoricoExpedienteDAO.grabarExpedienteHistorico()::BEGIN");
        PreparedStatement ps = null;
        try {
            String query = "insert into HIST_E_EXP (ID_PROCESO,EXP_PRO,EXP_EJE,EXP_NUM,EXP_FEI,EXP_FEF," +
                "EXP_EST,EXP_MUN,EXP_USU,EXP_UOR,EXP_PEND,EXP_TRA,EXP_TOCU,EXP_LOC,EXP_CLO,EXP_OBS," +
                "EXP_ASU,EXP_REF,EXP_IMP,EXP_UBICACION_DOC)" + 
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            ps= con.prepareStatement(query);
            
            JdbcOperations.setValues(ps,1,
                    idProceso,
                    exp.getCodProcedimiento(),
                    exp.getEjercicio(),
                    exp.getNumExpediente(),
                    DateOperations.toTimestamp(exp.getFechaInicio()),
                    DateOperations.toTimestamp(exp.getFechaFin()),
                    exp.getEstado(),
                    exp.getCodOrganizacion(),
                    exp.getCodUsuario(),
                    exp.getCodUorInicio(),
                    DateOperations.toTimestamp(exp.getFechaPendiente()),
                    exp.getCodTramitePendiente(),
                    exp.getOcurrenciaTramitePendiente(),
                    exp.getLocalizacion(),
                    exp.getCodLocalizacion(),
                    exp.getObservaciones(),
                    exp.getAsunto(),
                    exp.getReferencia(),
                    exp.getImportante(),
                    exp.getUbicacionDocumentacion());

            int res = ps.executeUpdate();
            return res > 0;
        }
        catch(Exception ex) {
            ex.printStackTrace();
            log.error("Se ha producido un error en el metodo expedienteEnvioHistoricoDAO.grabarExpedienteHistorico: " + ex.getMessage() , ex);
            throw new Exception(ex);
        } finally {
            if(ps != null)
                ps.close();
        }
    }
    
    /**
     * Grabar el contenido del cada objeto de la clase CronoVO en la tabla HIST_E_CRO
     * 
     * @param cronograma
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarTramitesExpediente(ArrayList<CronoVO> cronograma, long idProceso, Connection con) throws Exception {
        log.debug("HistoricoExpedienteDAO.grabarTramitesExpediente()::BEGIN");
        PreparedStatement ps = null;
        
        try {
            String sql = "INSERT INTO HIST_E_CRO (ID_PROCESO,CRO_PRO,CRO_EJE,CRO_NUM,CRO_TRA," +
                "CRO_FEI,CRO_FEF,CRO_USU,CRO_UTR,CRO_MUN,CRO_OCU,CRO_FIP,CRO_FLI,CRO_FFP,"+
                "CRO_RES,CRO_OBS,CRO_USF,CRO_AVISADOCFP,CRO_AVISADOFDP) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            int res = 0;
            int total = 0;
            for(CronoVO crono : cronograma) {
                try {
                    ps = con.prepareStatement(sql);

                    JdbcOperations.setValues(ps,1,			
                            idProceso,
                            crono.getCodProcedimiento(),
                            crono.getEjercicio(),
                            crono.getNumExpediente(),
                            crono.getCodTramite(),
                            DateOperations.toTimestamp(crono.getFechaInicio()),
                            DateOperations.toTimestamp(crono.getFechaFin()),
                            crono.getCodUsuario(),
                            crono.getCodUorTramitadora(),
                            crono.getCodOrganizacion(),
                            crono.getOcurrenciaTramite(),
                            DateOperations.toTimestamp(crono.getFechaInicioPlazo()),
                            DateOperations.toTimestamp(crono.getFechaLimite()),
                            DateOperations.toTimestamp(crono.getFechaFinPlazo()),
                            crono.getReserva(),
                            crono.getObservaciones(),
                            crono.getUsuarioFinalizacion(),
                            crono.getAvisoCercanaFinPlazo(),
                            crono.getAvisoCercanaFinPlazo());
 
                    log.debug("sql "+sql);
                    log.debug("parametros proceso"+idProceso+",proc="+crono.getCodProcedimiento()+",ejer"+crono.getEjercicio()+
                            ",numExp"+crono.getNumExpediente()+",codTra="+crono.getCodTramite()+",org="+crono.getCodTramite()+",ocurrencia"+crono.getOcurrenciaTramite());
                    res = ps.executeUpdate();
                            
                    if(res <= 0) 
                        throw new Exception("Error al insertar en "+sql);
                    else
                        total += res;
                }
                finally {
                    if(ps != null)
                        ps.close();
                }
            }
            return total == cronograma.size();
        } catch(Exception ex) {
            ex.printStackTrace();
            log.error(" ERROR: Se ha producido un error en el metodo expedienteEnvioHistoricoDAO.grabarTramitesExpediente: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }
    
    /**
     * Grabar el contenido del objeto de la clase ListTramOrigVO en la tabla HIST_LIST_TRAM_ORIG
     * 
     * @param listaTramites
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarListaTramitesOrigen(ArrayList<ListTramOrigVO> listaTramites, long idProceso, Connection con) throws Exception
    {
        log.debug("HistoricoExpedienteDAO.grabarListaTramitesOrigen()::BEGIN");
        Statement st = null;
        try
        {
            String sql_tablaHistListTramOrig = m_ConfigTechnical.getString("SQL.HIST_LIST_TRAM_ORIG");
            
            String sql_idProceso = m_ConfigTechnical.getString("SQL.HIST_LIST_TRAM_ORIG.idProceso");
            String sql_ejercicio = m_ConfigTechnical.getString("SQL.HIST_LIST_TRAM_ORIG.ejercicio");
            String sql_codPro = m_ConfigTechnical.getString("SQL.HIST_LIST_TRAM_ORIG.codPro");
            String sql_codMun = m_ConfigTechnical.getString("SQL.HIST_LIST_TRAM_ORIG.codMun");
            String sql_numExp = m_ConfigTechnical.getString("SQL.HIST_LIST_TRAM_ORIG.numExp");
            String sql_codTraOrigen = m_ConfigTechnical.getString("SQL.HIST_LIST_TRAM_ORIG.codTraOrigen");
            String sql_ocuTraOrigen = m_ConfigTechnical.getString("SQL.HIST_LIST_TRAM_ORIG.ocuTraOrigen");
            String sql_codTraDestino = m_ConfigTechnical.getString("SQL.HIST_LIST_TRAM_ORIG.codTraDestino");
            String sql_ocuTraDestino = m_ConfigTechnical.getString("SQL.HIST_LIST_TRAM_ORIG.ocuTraDestino");
            
            String query_p1 = "insert into "+sql_tablaHistListTramOrig+"("
                          +sql_idProceso+", "+sql_ejercicio+", "+sql_codPro+", "+sql_codMun
                          +", "+sql_numExp+", "+sql_codTraOrigen+", "+sql_ocuTraOrigen+", "+sql_codTraDestino
                          +", "+sql_ocuTraDestino+")"
                          +" values(";
            
            String queryCompleta = null;
            int res = 0;
            int total = 0;
            for(ListTramOrigVO t : listaTramites)
            {
                try
                {
                    queryCompleta = query_p1 
                              + idProceso
                              +", "+(t.getEjercicio())  
                              +", "+(t.getCodProcedimiento() != null ? "'"+t.getCodProcedimiento()+"'" : "null")
                              +", "+(t.getCodMunicipio())  
                              +", "+(t.getNumExpediente() != null ? "'"+t.getNumExpediente()+"'" : "null")
                              +", "+(t.getCodTramiteOrigen())
                              +", "+(t.getOcurrenciaTramiteOrigen())
                              +", "+(t.getCodTramiteDestino())
                              +", "+(t.getOcurrenciaTramiteDestino())
                              +")";
                    st = con.createStatement();
                    res = st.executeUpdate(queryCompleta);
                    if(res <= 0)
                        throw new Exception("Error al insertar en "+sql_tablaHistListTramOrig);
                    else
                        total += res;
                }
                finally
                {
                    if(st != null)
                        st.close();
                }
            }
            return total == listaTramites.size();
        }
        catch(Exception ex) {
            ex.printStackTrace();
            log.error("ERROR Se ha producido un error en el metodo expedienteEnvioHistoricoDAO.grabarListaTramitesOrigen: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }
    
    /**
     * Grabar el contenido del objeto de la clase InteresadoExpedienteVO en la tabla HIST_E_EXT
     * 
     * @param interesados
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarInteresadosExpediente(ArrayList<InteresadoExpedienteVO> interesados, long idProceso, Connection con) throws Exception
    {
        log.debug("HistoricoExpedienteDAO.grabarInteresadosExpediente()::BEGIN");
        Statement st = null;
        try
        {
            
            String sql_tablaHistEExt = m_ConfigTechnical.getString("SQL.HIST_E_EXT");
            
            String sql_idProceso = m_ConfigTechnical.getString("SQL.HIST_E_EXT.idProceso");
            String sql_extMun = m_ConfigTechnical.getString("SQL.HIST_E_EXT.extMun");
            String sql_extEje = m_ConfigTechnical.getString("SQL.HIST_E_EXT.extEje");
            String sql_extNum = m_ConfigTechnical.getString("SQL.HIST_E_EXT.extNum");
            String sql_extTer = m_ConfigTechnical.getString("SQL.HIST_E_EXT.extTer");
            String sql_extNvr = m_ConfigTechnical.getString("SQL.HIST_E_EXT.extNvr");
            String sql_extDot = m_ConfigTechnical.getString("SQL.HIST_E_EXT.extDot");
            String sql_extRol = m_ConfigTechnical.getString("SQL.HIST_E_EXT.extRol");
            String sql_extPro = m_ConfigTechnical.getString("SQL.HIST_E_EXT.extPro");
            String sql_mostrar = m_ConfigTechnical.getString("SQL.HIST_E_EXT.mostrar");
            String sql_extNotificacionElectronica = m_ConfigTechnical.getString("SQL.HIST_E_EXT.extNotificacionElectronica");
            
            String query_p1 = "insert into "+sql_tablaHistEExt+"("
                          +sql_idProceso+", "+sql_extMun+", "+sql_extEje+", "+sql_extNum
                          +", "+sql_extTer+", "+sql_extNvr+", "+sql_extDot+", "+sql_extRol
                          +", "+sql_extPro+", "+sql_mostrar+", "+sql_extNotificacionElectronica+")"
                          +" values(";
            
            String queryCompleta = null;
            int res = 0;
            int total = 0;
            for(InteresadoExpedienteVO i : interesados)
            {
                try
                {
                    queryCompleta = query_p1 
                              + idProceso
                              +", "+(i.getCodMunicipio())
                              +", "+(i.getEjercicio())
                              +", "+(i.getNumExpediente() != null ? "'"+i.getNumExpediente()+"'" : "null")  
                              +", "+(i.getCodTercero())
                              +", "+(i.getVersionTercero())
                              +", "+(i.getCodDomicilio())
                              +", "+(i.getCodRol())
                              +", "+(i.getCodProcedimiento() != null ? "'"+i.getCodProcedimiento()+"'" : "null")
                              +", "+(i.getMostrar())
                              +", "+(i.getNotificacionElectronica() != null ? "'"+i.getNotificacionElectronica()+"'" : "null")
                              +")";
                    
                    log.debug(" (TRAZA DE CONTROL) SQL: " + queryCompleta);
                    st = con.createStatement();
                    res = st.executeUpdate(queryCompleta);
                    if(res <= 0)
                        throw new Exception("Error al insertar en "+sql_tablaHistEExt);
                    else
                        total += res;
                }
                finally
                {
                    if(st != null)
                        st.close();
                }
            }
            return total == interesados.size();
        }
        catch(Exception ex){
            ex.printStackTrace();
            log.error("ERROR Se ha producido un error en el metodo expedienteEnvioHistoricoDAO.grabarInteresadosExpediente: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }
    
    /**
     * Grabar el contenido los objetos de la clase DocumentoPresentadoVO de parámetro documentos,
     * en la tabla HIST_E_DOCS_PRESENTADOS.
     * 
     * @param documentos
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarDocumentosPresentados(ArrayList<DocumentoPresentadoVO> documentos, long idProceso, Connection con) throws Exception
    {
        log.debug("HistoricoExpedienteDAO.grabarDocumentosPresentados()::BEGIN");
        PreparedStatement st = null;
        try
        {
            String sql_tablaHistEDocsPresentados = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS");
            
            String sql_idProceso = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.idProceso");
            String sql_presentadoCod = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.presentadoCod");
            String sql_presentadoMun = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.presentadoMun");
            String sql_presentadoEje = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.presentadoEje");
            String sql_presentadoNum = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.presentadoNum");
            String sql_presentadoPro = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.presentadoPro");
            String sql_presentadoCodDoc = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.presentadoCodDoc");
            String sql_presentadoContenido = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.presentadoContenido");
            String sql_presentadoTipo = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.presentadoTipo");
            String sql_presentadoExtension = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.presentadoExtension");
            String sql_presentadoOrigen = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.presentadoOrigen");
            String sql_presentadoFechaAlta = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.presentadoFechaAlta");
            String sql_presentadoNombre = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.presentadoNombre");
            String sql_presentadoCodUsuAlta = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.presentadoCodUsuAlta");
            String sql_presentadoCodUsuMod = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.presentadoCodUsuMod");
            String sql_presentadoFechaMod = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_PRESENTADOS.presentadoFechaMod");
            
            String query = "insert into "+sql_tablaHistEDocsPresentados+"("
                          +sql_idProceso+", "+sql_presentadoCod+", "+sql_presentadoMun+", "+sql_presentadoEje
                          +", "+sql_presentadoNum+", "+sql_presentadoPro+", "+sql_presentadoCodDoc+", "+sql_presentadoContenido
                          +", "+sql_presentadoTipo+", "+sql_presentadoExtension+", "+sql_presentadoOrigen
                          +", "+sql_presentadoFechaAlta+", "+sql_presentadoNombre+", "+sql_presentadoCodUsuAlta+", "+sql_presentadoCodUsuMod
                          +", "+sql_presentadoFechaMod
                          +")"
                          +" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            int res = 0;
            int total = 0;
            byte[] contenido = null;
            for(DocumentoPresentadoVO d : documentos)
            {
                try
                {
                    contenido = expDAO.getContenidoDocumentoPresentado(d.getCodPresentado(), con);

                    st = con.prepareStatement(query);

                    st.setLong(1, idProceso);
                    st.setInt(2, d.getCodPresentado());
                    st.setInt(3, d.getCodMunicipio());
                    st.setInt(4, d.getEjercicio());
                    if(d.getNumExpediente() != null && !d.getNumExpediente().equals(""))
                        st.setString(5, d.getNumExpediente());
                    else
                        st.setNull(5, java.sql.Types.VARCHAR);
                    if(d.getCodProcedimiento() != null && !d.getCodProcedimiento().equals(""))
                        st.setString(6, d.getCodProcedimiento());
                    else
                        st.setNull(6, java.sql.Types.VARCHAR);
                    st.setInt(7, d.getCodDocumentoPresentado());
                   
                    st.setBytes(8, contenido);

                    if(d.getTipoMime() != null && !d.getTipoMime().equals(""))
                        st.setString(9, d.getTipoMime());
                    else
                        st.setNull(9, java.sql.Types.VARCHAR);
                    if(d.getExtension() != null && !d.getExtension().equals(""))
                        st.setString(10, d.getExtension());
                    else
                        st.setNull(10, java.sql.Types.VARCHAR);
                    if(d.getOrigen() != null && !d.getOrigen().equals(""))
                        st.setString(11, d.getOrigen());
                    else
                        st.setNull(11, java.sql.Types.VARCHAR);
                    if(d.getFechaAlta() != null)
                        st.setTimestamp(12, new java.sql.Timestamp(d.getFechaAlta().getTimeInMillis()));
                    else
                        st.setNull(12, java.sql.Types.TIMESTAMP);
                    if(d.getNombreDocumento() != null && !d.getNombreDocumento().equals(""))
                        st.setString(13, d.getNombreDocumento());
                    else
                        st.setNull(13, java.sql.Types.VARCHAR);
                    st.setInt(14, d.getCodUsuarioAlta());
                    if(d.getCodUsuarioModificacion() != null)
                        st.setInt(15, d.getCodUsuarioModificacion());
                    else
                        st.setNull(15, java.sql.Types.INTEGER);
                    if(d.getFechaModificacion() != null)
                        st.setTimestamp(16, new java.sql.Timestamp(d.getFechaModificacion().getTimeInMillis()));
                    else
                        st.setNull(16, java.sql.Types.TIMESTAMP);

                    res = st.executeUpdate();
                    if(res <= 0)
                        throw new Exception("Error al insertar en "+sql_tablaHistEDocsPresentados);
                    else
                        total += res;

                    if(d.getFirmas() != null && d.getFirmas().size() > 0)
                    {
                        boolean resultadoGrabar = this.grabarFirmaDocumentosPresentados(d.getFirmas(), idProceso, con);
                        if(!resultadoGrabar)
                            throw new BDException("Error al grabar firmaDocumentosPresentados para el expediente "+d.getNumExpediente()+" y documento "+d.getCodDocumentoPresentado());
                    }
                }
                finally
                {
                    if(st != null)
                        st.close();
                }
            }
            return total == documentos.size();
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error en el metodo expedienteEnvioHistoricoDAO.grabarDocumentosPresentados: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }
    
    /**
     * Grabar el contenido los objetos de la clase DocsFirmasVO del parámetro firmas, en la tabla HIST_E_DOCS_FIRMAS.
     * 
     * @param firmas
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarFirmaDocumentosPresentados(ArrayList<DocsFirmasVO> firmas, long idProceso, Connection con) throws Exception
    {
		log.debug("HistoricoExpedienteDAO.grabarFirmaDocumentosPresentados()::BEGIN");
        PreparedStatement st = null;
        try
        {
            String sql_tablaHistEDocsFirmas = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_FIRMAS");
            
            String sql_idProceso = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_FIRMAS.idProceso");
            String sql_idDocFirma = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_FIRMAS.idDocFirma");
            String sql_docFirmaEstado = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_FIRMAS.docFirmaEstado");
            String sql_docFirmaOrden = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_FIRMAS.docFirmaOrden");
            String sql_docFirmaUor = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_FIRMAS.docFirmaUor");
            String sql_docFirmaCargo = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_FIRMAS.docFirmaCargo");
            String sql_docFirmaUsuario = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_FIRMAS.docFirmaUsuario");
            String sql_docFirmaFecha = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_FIRMAS.docFirmaFecha");
            String sql_idDocPresentado = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_FIRMAS.idDocPresentado");
            String sql_docFechaEnvio = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_FIRMAS.docFechaEnvio");
            String sql_firma = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_FIRMAS.firma");
            String sql_docFirmaObservaciones = m_ConfigTechnical.getString("SQL.HIST_E_DOCS_FIRMAS.docFirmaObservaciones");
            
            String query = "insert into "+sql_tablaHistEDocsFirmas+"("
                          +sql_idProceso+", "+sql_idDocFirma+", "+sql_docFirmaEstado+", "+sql_docFirmaOrden
                          +", "+sql_docFirmaUor+", "+sql_docFirmaCargo+", "+sql_docFirmaUsuario+", "+sql_docFirmaFecha
                          +", "+sql_idDocPresentado+", "+sql_docFechaEnvio+", "+sql_firma
                          +", "+sql_docFirmaObservaciones
                          +")"
                          +" values(?,?,?,?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            
            int res = 0;
            int total = 0;
            byte[] contenidoFirma = null;
            for(DocsFirmasVO f : firmas)
            {
                try
                {
                    contenidoFirma = expDAO.getContenidoFirmaDocumentoPresentado(f.getIdDocPresentado(), f.getIdDocFirma(), con);

                    st = con.prepareStatement(query);

                    st.setLong(1, idProceso);
                    st.setInt(2, f.getIdDocFirma());
                    if(f.getDocFirmaEstado() != null && !f.getDocFirmaEstado().equals(""))
                        st.setString(3, f.getDocFirmaEstado());
                    else
                        st.setNull(3, java.sql.Types.VARCHAR);
                    st.setInt(4, f.getDocFirmaOrden());
                    if(f.getDocFirmaUor() != null)
                        st.setInt(5, f.getDocFirmaUor());
                    else
                        st.setInt(5, java.sql.Types.INTEGER);
                    if(f.getDocFirmaCargo() != null)
                        st.setInt(6, f.getDocFirmaCargo());
                    else
                        st.setNull(6, java.sql.Types.INTEGER);
                    if(f.getDocFirmaUsuario() != null)
                        st.setInt(7, f.getDocFirmaUsuario());
                    else
                        st.setNull(7, java.sql.Types.INTEGER);
                    if(f.getDocFirmaFecha() != null)
                        st.setTimestamp(8, new java.sql.Timestamp(f.getDocFirmaFecha().getTimeInMillis()));
                    else
                        st.setNull(8, java.sql.Types.TIMESTAMP);
                    st.setInt(9, f.getIdDocPresentado());
                    if(f.getDocFechaEnvio() != null)
                        st.setTimestamp(10, new java.sql.Timestamp(f.getDocFechaEnvio().getTimeInMillis()));
                    else
                        st.setNull(10, java.sql.Types.TIMESTAMP);
                    if(contenidoFirma != null && contenidoFirma.length > 0)
                    {
                        java.io.InputStream is = new java.io.ByteArrayInputStream(contenidoFirma);
                        st.setBinaryStream(11, is, contenidoFirma.length);
                    }
                    else
                    {
                        st.setNull(11,java.sql.Types.BINARY);
                    }
                    if(f.getObservaciones() != null && !f.getObservaciones().equals(""))
                        st.setString(12, f.getObservaciones());
                    else
                        st.setNull(12, java.sql.Types.VARCHAR);



                    res = st.executeUpdate();
                    if(res <= 0)
                        throw new Exception("Error al insertar en "+sql_tablaHistEDocsFirmas);
                    else
                        total += res;
                }
                finally
                {
                    if(st != null)
                        st.close();
                }
            }
            return total == firmas.size();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Error grabarFirmaDocumentosPresentados(): " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }
    
    /**
     * Grabar el contenido los objetos de la clase DocExtVO del parámetro documentos,
     * en la tabla HIST_E_DOC_EXT.
     * 
     * @param documentos
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarDocumentosExternos(ArrayList<DocExtVO> documentos, long idProceso, Connection con) throws Exception
    {
        log.debug("HistoricoExpedienteDAO.grabarDocumentosExternos()::BEGIN");
        PreparedStatement st = null;
        try
        {
            String sql_tablaHistEDocExt = m_ConfigTechnical.getString("SQL.HIST_E_DOC_EXT");
            
            String sql_idProceso = m_ConfigTechnical.getString("SQL.HIST_E_DOC_EXT.idProceso");
            String sql_docExtNum = m_ConfigTechnical.getString("SQL.HIST_E_DOC_EXT.docExtNum");
            String sql_docExtMun = m_ConfigTechnical.getString("SQL.HIST_E_DOC_EXT.docExtMun");
            String sql_docExtEje = m_ConfigTechnical.getString("SQL.HIST_E_DOC_EXT.docExtEje");
            String sql_docExtCod = m_ConfigTechnical.getString("SQL.HIST_E_DOC_EXT.docExtCod");
            String sql_docExtNom = m_ConfigTechnical.getString("SQL.HIST_E_DOC_EXT.docExtNom");
            String sql_docExtFal = m_ConfigTechnical.getString("SQL.HIST_E_DOC_EXT.docExtFal");
            String sql_docExtFil = m_ConfigTechnical.getString("SQL.HIST_E_DOC_EXT.docExtFil");
            String sql_docExtTip = m_ConfigTechnical.getString("SQL.HIST_E_DOC_EXT.docExtTip");
            String sql_docExtExt = m_ConfigTechnical.getString("SQL.HIST_E_DOC_EXT.docExtExt");
            String sql_docExtOrigen = m_ConfigTechnical.getString("SQL.HIST_E_DOC_EXT.docExtOrigen");
            
            String query = "insert into "+sql_tablaHistEDocExt+"("
                          +sql_idProceso+", "+sql_docExtNum+", "+sql_docExtMun+", "+sql_docExtEje
                          +", "+sql_docExtCod+", "+sql_docExtNom+", "+sql_docExtFal+", "+sql_docExtFil
                          +", "+sql_docExtTip+", "+sql_docExtExt+", "+sql_docExtOrigen
                          +")"
                          +" values(?,?,?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            int res = 0;
            int total = 0;
            byte[] contenido = null;
            for(DocExtVO d : documentos)
            {
                try
                {
                    contenido = expDAO.getContenidoDocumentoExterno(d.getCodMunicipio(), d.getEjercicio(), d.getNumExpediente(), d.getCodDocumento(), con);

                    st = con.prepareStatement(query);
                    st.setLong(1, idProceso);
                    if(d.getNumExpediente() != null && !d.getNumExpediente().equals(""))
                        st.setString(2, d.getNumExpediente());
                    else
                        st.setNull(2, java.sql.Types.VARCHAR);
                    st.setInt(3, d.getCodMunicipio());
                    st.setInt(4, d.getEjercicio());
                    st.setInt(5, d.getCodDocumento());
                    if(d.getNombreDocumento() != null && !d.getNombreDocumento().equals(""))
                        st.setString(6, d.getNombreDocumento());
                    else
                        st.setNull(6, java.sql.Types.VARCHAR);
                    if(d.getFechaDocumento() != null)
                        st.setTimestamp(7, new java.sql.Timestamp(d.getFechaDocumento().getTimeInMillis()));
                    else
                        st.setNull(8, java.sql.Types.TIMESTAMP);
                    if(contenido != null && contenido.length > 0)
                    {
                        java.io.InputStream is = new java.io.ByteArrayInputStream(contenido);
                        st.setBinaryStream(8, is, contenido.length);
                    }
                    else
                    {
                        st.setNull(8,java.sql.Types.BINARY);
                    }
                    if(d.getTipoDocumento() != null && !d.getTipoDocumento().equals(""))
                        st.setString(9, d.getTipoDocumento());
                    else
                        st.setNull(9, java.sql.Types.VARCHAR);
                    if(d.getExtension() != null && !d.getExtension().equals(""))
                        st.setString(10, d.getExtension());
                    else
                        st.setNull(10, java.sql.Types.VARCHAR);
                    if(d.getOrigen() != null && !d.getOrigen().equals(""))
                        st.setString(11, d.getOrigen());
                    else
                        st.setNull(11, java.sql.Types.VARCHAR);

                    res = st.executeUpdate();
                    if(res <= 0)
                        throw new Exception("Error al insertar en "+sql_tablaHistEDocExt);
                    else
                        total += res;
                }
                finally
                {
                    if(st != null)
                        st.close();
                }
            }
            return total == documentos.size();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Error al grabar los documentos externos en el histórico: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }
    
    /**
     * Grabar el contenido los objetos de la clase NotificacionVO del parámetro notificaciones,
     * en la tabla HIST_NOTIFICACION.
     * 
     * @param notificaciones
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarNotificaciones(ArrayList<NotificacionVO> notificaciones, long idProceso, Connection con) throws Exception
    {
        log.debug("HistoricoExpedienteDAO.grabarNotificaciones()::BEGIN");
        PreparedStatement st = null;
		
        PreparedStatement psIndv = null;
        ResultSet rsIndv = null;
		
        try
        {
            
            String sql_tablaHistNotificacion = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION");
            
            String sql_idProceso = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.idProceso");
            String sql_codigoNotificacion = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.codigoNotificacion");
            String sql_numExpediente = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.numExpediente");
            String sql_codProcedimiento = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.codProcedimiento");
            String sql_ejercicio = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.ejercicio");
            String sql_codMunicipio = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.codMunicipio");
            String sql_codTramite = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.codTramite");
            String sql_ocuTramite = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.ocuTramite");
            String sql_actoNotificado = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.actoNotificado");
            String sql_caducidadNotificacion = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.caducidadNotificacion");
            String sql_firma = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.firma");
            String sql_textoNotificacion = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.textoNotificacion");
            String sql_firmada = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.firmada");
            String sql_xmlNotificacion = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.xmlNotificacion");
            String sql_fechaEnvio = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.fechaEnvio");
            String sql_registroRt = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.registroRt");
            String sql_fechaAcuse = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.fechaAcuse");
            String sql_resultado = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION.resultado");
            
            String query = "insert into "+sql_tablaHistNotificacion+"("
                          +sql_idProceso+", "+sql_codigoNotificacion+", "+sql_numExpediente+", "+sql_codProcedimiento
                          +", "+sql_ejercicio+", "+sql_codMunicipio+", "+sql_codTramite+", "+sql_ocuTramite
                          +", "+sql_actoNotificado+", "+sql_caducidadNotificacion+", "+sql_firma+", "+sql_textoNotificacion
                          +", "+sql_firmada+", "+sql_xmlNotificacion+", "+sql_fechaEnvio+", "+sql_registroRt
                          +", "+sql_fechaAcuse+", "+sql_resultado
                          +")"
                          +" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            
            int res = 0;
            int total = 0;
            boolean resultadoGrabar = true;
            for(NotificacionVO n : notificaciones)
            {
                try
                {
                    st = con.prepareStatement(query);
                    st.setLong(1, idProceso);
                    st.setInt(2, n.getCodNotificacion());
                    st.setString(3, n.getNumExpediente());
                    st.setString(4, n.getCodProcedimiento());
                    st.setInt(5, n.getEjercicio());
                    st.setInt(6, n.getCodMunicipio());
                    st.setInt(7, n.getCodTramite());
                    st.setInt(8, n.getOcurrenciaTramite());
                    st.setString(9, n.getActoNotificado());
                    if(n.getCaducidadNotificacion() != null)
                        st.setInt(10, n.getCaducidadNotificacion());
                    else
                        st.setNull(10, java.sql.Types.INTEGER);
                    if(n.getFirma() != null && n.getFirma().length > 0)
                    {
                        java.io.InputStream is = new java.io.ByteArrayInputStream(n.getFirma());
                        st.setBinaryStream(11, is, n.getFirma().length);
                    }
                    else
                    {
                        st.setNull(11,java.sql.Types.BINARY);
                    }
                    st.setString(12, n.getTextoNotificacion());
                    st.setString(13, n.getFirmada());
                    st.setString(14, n.getXmlNotificacion());
                    st.setTimestamp(15, n.getFechaEnvio() != null ? new java.sql.Timestamp(n.getFechaEnvio().getTimeInMillis()) : null);
                    st.setString(16, n.getNumRegistroTelematico());
                    st.setTimestamp(17, n.getFechaAcuse() != null ? new java.sql.Timestamp(n.getFechaAcuse().getTimeInMillis()) : null);
                    st.setString(18, n.getResultado());

                    res = st.executeUpdate();
                    if(res <= 0)
                        throw new Exception("Error al insertar en "+sql_tablaHistNotificacion);
                    else
                        total += res;
					
                    // Si además, una notificación tiene adjuntos, habrá que almacenarlos, para ello se invocará a los métodos grabarAdjuntoNotificacion() o grabarAdjuntoExternoNotificacion().
                    if(n.getAdjuntos() != null && n.getAdjuntos().size() > 0)
                    {
                        resultadoGrabar = grabarAdjuntoNotificacion(n.getAdjuntos(), idProceso, con);
                        if(!resultadoGrabar)
                            throw new BDException("Error al grabar adjuntosNotificacion para el expediente "+n.getNumExpediente()+" y notificacion "+n.getCodNotificacion());
                    }
                    if(n.getAdjuntosExternos() != null && n.getAdjuntosExternos().size() > 0)
                    {
                        resultadoGrabar = grabarAdjuntoExternoNotificacion(n.getAdjuntosExternos(), idProceso, con);
                        if(!resultadoGrabar)
                            throw new BDException("Error al grabar adjuntosExternosNotificacion para el expediente "+n.getNumExpediente()+" y notificacion "+n.getCodNotificacion());
                    }
                    // Además habrá que almacenar los autorizados de la notificación, y para ello hay que llamar a grabarAutorizadosNotificacion()
                    if(n.getAutorizados() != null && n.getAutorizados().size() > 0)
                    {
                        resultadoGrabar = grabarAutorizadosNotificacion(n.getAutorizados(), idProceso, con);
                        if(!resultadoGrabar)
                            throw new BDException("Error al grabar autorizadosNotificacion para el expediente "+n.getNumExpediente()+" y notificacion "+n.getCodNotificacion());
                    }   
					
                    //INI Comprobamos si la notificacion tiene notificaciones individuales y las historificamos
                    ArrayList<NotificacionIndividualVO> notificacionesIndividuales = new ArrayList<NotificacionIndividualVO>();

                    String sql = "SELECT NOTIFICACION_INDIVIDUAL.* FROM NOTIFICACION_INDIVIDUAL WHERE REGISTRO_RT ='" + n.getCodNotificacion() + "'";

                    psIndv = con.prepareStatement(sql);
                    rsIndv = psIndv.executeQuery();

                    while(rsIndv.next()){

                            NotificacionIndividualVO notif = new NotificacionIndividualVO();

                            notif.setCodigoNotificacionIndividual(rsIndv.getInt("CODIGO_NOTIFICACION_INDIVIDUAL"));
                            notif.setCodigoNotificacion(rsIndv.getInt("CODIGO_NOTIFICACION"));
                            notif.setEstadoNotificacionIndividual(rsIndv.getString("ENVIADA"));
                            notif.setNumeroRegistroTelematico(rsIndv.getString("REGISTRO_RT"));
                            notif.setResultado(rsIndv.getString("RESULTADO"));

                            notificacionesIndividuales.add(notif);
                    }

                    boolean histNotIndv = grabarNotificacionesIndividuales(notificacionesIndividuales, idProceso, con);

                    //FIN Comprobamos si la notificacion tiene notificaciones individuales y las historificamos
                }
                finally
                {
                    if(st != null) st.close();
                    if(rsIndv != null) rsIndv.close();
                    if(psIndv != null) psIndv.close();
                }
            }
            return total == notificaciones.size();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Error al grabar las notificacione de un expediente en el histórico: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }
	
	
	/**
     * Grabar el contenido los objetos de la clase NotificacionIndividualVO del parámetro notificacionesIndividuales,
     * en la tabla HIST_NOTIFICACION_INDIVIDUAL.
     * 
     * @param notificacionesIndividuales
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarNotificacionesIndividuales(ArrayList<NotificacionIndividualVO> notificacionesIndividuales, long idProceso, Connection con) throws Exception
    {
        PreparedStatement st = null;
        try
        {
            
            String sql_tablaHistNotificacionIndividual = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION_INDIVIDUAL");
            
            String sql_idProceso = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION_INDIVIDUAL.idProceso");
            String sql_codigoNotificacionIndividual = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION_INDIVIDUAL.codigoNotificacionIndividual");
            String sql_codigoNotificacion = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION_INDIVIDUAL.codigoNotificacion");
            String sql_autorizado = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION_INDIVIDUAL.autorizado");
            String sql_estadoNotificacionIndividual = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION_INDIVIDUAL.estadoNotificacionIndividual");
            String sql_fechaAcuse = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION_INDIVIDUAL.fechaAcuse");
            String sql_resultado = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION_INDIVIDUAL.resultado");
            String sql_numeroRegistroTelematico = m_ConfigTechnical.getString("SQL.HIST_NOTIFICACION_INDIVIDUAL.numeroRegistroTelematico");

            
            String query = "insert into "+sql_tablaHistNotificacionIndividual+"("
                          +sql_idProceso+", "+sql_codigoNotificacionIndividual+", "+sql_codigoNotificacion+", "+sql_autorizado
                          +", "+sql_estadoNotificacionIndividual+", "+sql_fechaAcuse+", "+sql_resultado+", "+sql_numeroRegistroTelematico
                          +")"
                          +" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            
            int res = 0;
            int total = 0;
            boolean resultadoGrabar = true;
            for(NotificacionIndividualVO n : notificacionesIndividuales)
            {
                try
                {
                    st = con.prepareStatement(query);
                    st.setLong(1, idProceso);
                    st.setInt(2, n.getCodigoNotificacionIndividual());
                    st.setInt(3, n.getCodigoNotificacion());
                    st.setInt(4, n.getAutorizado().getCodTercero());
                    st.setString(5, n.getEstadoNotificacionIndividual());
                    st.setTimestamp(6, n.getFechaAcuse() != null ? new java.sql.Timestamp(n.getFechaAcuse().getTimeInMillis()) : null);
                    st.setString(7, n.getResultado());
                    st.setString(8, n.getNumeroRegistroTelematico());
					
                    res = st.executeUpdate();
                    if(res <= 0)
                        throw new Exception("Error al insertar en "+sql_tablaHistNotificacionIndividual);
                    else
                        total += res;
  
                }
                finally
                {
                    if(st != null)
                        st.close();
                }
            }
            return total == notificacionesIndividuales.size();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Error al grabar las notificaciones individuales de un expediente en el histórico: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }
	
    
    /**
     * Grabar el contenido los objetos de la clase AdjuntoNotificacionVO del parámetro adjuntos,
     * en la tabla HIST_ADJUNTO_NOTIFICACION.
     * 
     * @param adjuntos
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarAdjuntoNotificacion(ArrayList<AdjuntoNotificacionVO> adjuntos, long idProceso, Connection con) throws Exception
    {
        Statement st = null;
        try
        {   
            String sql_tablaHistAdjuntoNotificacion = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_NOTIFICACION");
            
            String sql_idProceso = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_NOTIFICACION.idProceso");
            String sql_codigoNotificacion = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_NOTIFICACION.codigoNotificacion");
            String sql_codMunicipio = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_NOTIFICACION.codMunicipio");
            String sql_ejercicio = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_NOTIFICACION.ejercicio");
            String sql_numExpediente = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_NOTIFICACION.numExpediente");
            String sql_codProcedimiento = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_NOTIFICACION.codProcedimiento");
            String sql_codTramite = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_NOTIFICACION.codTramite");
            String sql_ocuTramite = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_NOTIFICACION.ocuTramite");
            String sql_numUnidadDoc = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_NOTIFICACION.numUnidadDoc");
            
            String query_p1 = "insert into "+sql_tablaHistAdjuntoNotificacion+"("
                          +sql_idProceso+", "+sql_codigoNotificacion+", "+sql_codMunicipio+", "+sql_ejercicio
                          +", "+sql_numExpediente+", "+sql_codProcedimiento+", "+sql_codTramite+", "+sql_ocuTramite
                          +", "+sql_numUnidadDoc
                          +")"
                          +" values(";
            
            String queryCompleta = null;
            int res = 0;
            int total = 0;
            for(AdjuntoNotificacionVO a : adjuntos)
            {
                try
                {
                    queryCompleta = query_p1 
                              + idProceso
                              +", "+(a.getCodNotificacion())
                              +", "+(a.getCodMunicipio())
                              +", "+(a.getEjercicio())
                              +", "+(a.getNumExpediente() != null ? "'"+a.getNumExpediente()+"'" : "null")
                              +", "+(a.getCodProcedimiento() != null ? "'"+a.getCodProcedimiento()+"'" : "null")
                              +", "+(a.getCodTramite())
                              +", "+(a.getOcurrenciaTramite())
                              +", "+(a.getNumUnidadDocumento())
                              +")";
                    
                    log.debug(" (TRAZA DE CONTROL) SQL: " + queryCompleta);
                    st = con.createStatement();
                    res = st.executeUpdate(queryCompleta);
                    if(res <= 0)
                        throw new Exception("Error al insertar en "+sql_tablaHistAdjuntoNotificacion);
                    else
                        total += res;
                }
                finally
                {
                    if(st != null)
                        st.close();
                }
            }
            return total == adjuntos.size();
        }
        catch(Exception ex) {
            log.error("Se ha producido un error al grabar la lista de adjuntos de una notificación de un expediente en el histórico: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }
    
    /**
     * Grabar el contenido los objetos de la clase AdjuntoExtNotificacionVO del parámetro adjuntos,
     * en la tabla HIST_ADJUNTO_EXT_NOTIFICACION.
     * 
     * @param adjuntos
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarAdjuntoExternoNotificacion(ArrayList<AdjuntoExtNotificacionVO> adjuntos, long idProceso, Connection con) throws Exception
    {
        PreparedStatement st = null;
        try
        {
            String sql_tablaHistAdjuntoExtNotificacion = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION");
            
            String sql_idProceso = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.idProceso");
            String sql_id = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.id");
            String sql_codMunicipio = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.codMunicipio");
            String sql_numExpediente = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.numExpediente");
            String sql_codTramite = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.codTramite");
            String sql_ocuTramite = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.ocuTramite");
            String sql_firma = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.firma");
            String sql_fecha = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.fecha");
            String sql_contenido = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.contenido");
            String sql_idNotificacion = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.idNotificacion");
            String sql_plataformaFirma = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.plataformaFirma");
            String sql_codUsuarioFirma = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.codUsuarioFirma");
            String sql_nombre = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.nombre");
            String sql_tipoMime = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.tipoMime");
            String sql_estadoFirma = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.estadoFirma");
            String sql_fechaFirma = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.fechaFirma");
            String sql_codUsuarioRechazo = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.codUsuarioRechazo");
            String sql_fechaRechazo = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.fechaRechazo");
            String sql_observacionesRechazo = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.observacionesRechazo");
            String sql_tipoCertificadoFirma = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_EXT_NOTIFICACION.tipoCertificadoFirma");
            
            String query = "insert into "+sql_tablaHistAdjuntoExtNotificacion+"("
                          +sql_idProceso+", "+sql_id+", "+sql_codMunicipio+", "+sql_numExpediente
                          +", "+sql_codTramite+", "+sql_ocuTramite+", "+sql_firma+", "+sql_fecha
                          +", "+sql_contenido+", "+sql_idNotificacion+", "+sql_plataformaFirma+", "+sql_codUsuarioFirma
                          +", "+sql_nombre+", "+sql_tipoMime+", "+sql_estadoFirma+", "+sql_fechaFirma
                          +", "+sql_codUsuarioRechazo+", "+sql_fechaRechazo+", "+sql_observacionesRechazo+", "+sql_tipoCertificadoFirma
                          +")"
                          +" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            int res = 0;
            int total = 0;
            byte[] contenido = null;
            for(AdjuntoExtNotificacionVO a : adjuntos)
            {
                try
                {
                    contenido = expDAO.getContenidoDocumentoExternoNotificacion(a.getIdNotificacion(), a.getId(), con);

                    st = con.prepareStatement(query);
                    st.setLong(1, idProceso);
                    st.setInt(2, a.getId());
                    st.setInt(3, a.getCodMunicipio());
                    st.setString(4, a.getNumExpediente());
                    st.setInt(5, a.getCodTramite());
                    st.setInt(6, a.getOcurrenciaTramite());
                    st.setString(7,a.getFirma());
                    st.setTimestamp(8, a.getFecha() != null ? new java.sql.Timestamp(a.getFecha().getTimeInMillis()) : null);
                    if(contenido != null && contenido.length > 0)
                    {
                        java.io.InputStream is = new java.io.ByteArrayInputStream(contenido);
                        st.setBinaryStream(9, is, contenido.length);
                    }
                    else
                    {
                        st.setNull(9,java.sql.Types.BINARY);
                    }
                    if(a.getIdNotificacion() != null)
                        st.setInt(10, a.getIdNotificacion());
                    else
                        st.setNull(10, java.sql.Types.INTEGER);
                    st.setString(11, a.getPlataformaFirma());
                    if(a.getCodUsuarioFirma() != null)
                        st.setInt(12, a.getCodUsuarioFirma());
                    else
                        st.setNull(12, java.sql.Types.INTEGER);
                    st.setString(13, a.getNombre());
                    st.setString(14, a.getTipoMime());
                    st.setString(15, a.getEstadoFirma());
                    st.setTimestamp(16, a.getFechaFirma() != null ? new java.sql.Timestamp(a.getFechaFirma().getTimeInMillis()) : null);
                    if(a.getCodUsuarioRechazo() != null)
                        st.setInt(17, a.getCodUsuarioRechazo());
                    else
                        st.setNull(17, java.sql.Types.INTEGER);
                    st.setTimestamp(18, a.getFechaRechazo() != null ? new java.sql.Timestamp(a.getFechaRechazo().getTimeInMillis()) : null);
                    st.setString(19, a.getObservacionesRechazo());
                    if(a.getTipoCertificadoFirma() != null)
                        st.setInt(20, a.getTipoCertificadoFirma());
                    else
                        st.setNull(20, java.sql.Types.INTEGER);

                    res = st.executeUpdate();
                    if(res <= 0)
                        throw new Exception("Error al insertar en "+sql_tablaHistAdjuntoExtNotificacion);
                    else
                        total += res;
                }
                finally
                {
                    if(st != null)
                        st.close();
                }
            }
            return total == adjuntos.size();
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al grabar la lista de documentos externos adjuntados a una notificación en el histórico: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }
    
    /**
     * Grabar el contenido los objetos de la clase AutorizadoNotificacionVO del parámetro adjuntos,
     * en la tabla HIST_AUTORIZADO_NOTIFICACION.
     * 
     * @param autorizados
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarAutorizadosNotificacion(ArrayList<AutorizadoNotificacionVO> autorizados, long idProceso, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String sql_tablaHistAutorizadoNotificacion = m_ConfigTechnical.getString("SQL.HIST_AUTORIZADO_NOTIFICACION");            
            String sql_idProceso = m_ConfigTechnical.getString("SQL.HIST_AUTORIZADO_NOTIFICACION.idProceso");
            String sql_codigoNotificacion = m_ConfigTechnical.getString("SQL.HIST_AUTORIZADO_NOTIFICACION.codigoNotificacion");
            String sql_codMunicipio = m_ConfigTechnical.getString("SQL.HIST_AUTORIZADO_NOTIFICACION.codMunicipio");
            String sql_ejercicio = m_ConfigTechnical.getString("SQL.HIST_AUTORIZADO_NOTIFICACION.ejercicio");
            String sql_numExpediente = m_ConfigTechnical.getString("SQL.HIST_AUTORIZADO_NOTIFICACION.numExpediente");
            String sql_codTercero = m_ConfigTechnical.getString("SQL.HIST_AUTORIZADO_NOTIFICACION.codTercero");
            String sql_verTercero = m_ConfigTechnical.getString("SQL.HIST_AUTORIZADO_NOTIFICACION.verTercero");
            
            String query_p1 = "insert into "+sql_tablaHistAutorizadoNotificacion+"("
                          +sql_idProceso+", "+sql_codigoNotificacion+", "+sql_codMunicipio+", "+sql_ejercicio
                          +", "+sql_numExpediente+", "+sql_codTercero+", "+sql_verTercero
                          +")"
                          +" values(";
            
            String queryCompleta = null;
            int res = 0;
            int total = 0;
            for(AutorizadoNotificacionVO a : autorizados)
            {
                try
                {
                    queryCompleta = query_p1 
                              + idProceso
                              +", "+(a.getCodNotificacion())
                              +", "+(a.getCodMunicipio())
                              +", "+(a.getEjercicio())
                              +", "+(a.getNumExpediente() != null ? "'"+a.getNumExpediente()+"'" : "null")
                              +", "+(a.getCodTercero())
                              +", "+(a.getVersionTercero())
                              +")";
                    
                    log.debug(" (TRAZA DE CONTROL) SQL: " + queryCompleta);
                    st = con.createStatement();
                    res = st.executeUpdate(queryCompleta);
                    if(res <= 0)
                        throw new Exception("Error al insertar en "+sql_tablaHistAutorizadoNotificacion);
                    else
                        total += res;
                }
                finally
                {
                    if(st != null)
                        st.close();
                }
            }
            return total == autorizados.size();
        }
        catch(Exception ex){
            ex.printStackTrace();
            log.error("Se ha producido un error al grabar los autorizados de una notificación en el histórico: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }
    
    /**
     * Grabar el contenido los objetos de la clase ComunicacionVO del parámetro comunicaciones,
     * en la tabla HIST_COMUNICACION.
     * 
     * @param comunicaciones
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarComunicaciones(ArrayList<ComunicacionVO> listaComunicaciones, long idProceso, Connection con) throws Exception {
        log.debug("HistoricoExpedienteDAO.grabarComunicaciones()::BEGIN");
        PreparedStatement ps = null;
        
        String sql = "INSERT INTO HIST_COMUNICACION (ID_PROCESO,ID,ASUNTO,TEXTO,TIPO_DOCUMENTO,DOCUMENTO," +
            "NOMBRE,FECHA,NUM_REGISTRO,ORIGEN_REGISTRO,XML_COMUNICACION,FIRMA," +
            "PLATAFORMA_FIRMA,COD_ORGANIZACION,EJERCICIO,NUM_EXPEDIENTE,LEIDA) " + 
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
        try{
            for (ComunicacionVO comunicacion : listaComunicaciones) {
                ps= con.prepareStatement(sql);
                JdbcOperations.setValues(ps,1,
                        idProceso,
                        comunicacion.getId(),
                        comunicacion.getAsunto(),
                        comunicacion.getTexto(),
                        comunicacion.getTipoDocumento(),
                        comunicacion.getDocumento(),
                        comunicacion.getNombre(),
                        DateOperations.toTimestamp(comunicacion.getFecha()),
                        comunicacion.getNumRegistro(),
                        comunicacion.getOrigenRegistro(),
                        comunicacion.getXmlComunicacion(),
                        comunicacion.getFirma(),
                        comunicacion.getPlataformaFirma(),
                        comunicacion.getCodOrganizacion(),
                        comunicacion.getEjercicio(),
                        comunicacion.getNumExpediente(),
                        comunicacion.getLeida());

                ps.executeUpdate();
                ps.close();
                //TODO  Si además, si una comunicación tiene algún adjunto, habrá que grabarlo invocando a la operación grabarAdjuntoNotificacion()
            }
            
            return true;
        }catch(SQLException e){
            log.error("Error al grabar las comunicaciones de un expediente en el histórico: " + e.getMessage());
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
    
    /**
     * Grabar el contenido los objetos de la clase AdjuntoComunicacionVO del parámetro adjuntos,
     * en la tabla HIST_ADJUNTO_COMUNICACION.
     * 
     * @param adjuntos
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarAdjuntosComunicacion(ArrayList<AdjuntoComunicacionVO> adjuntos, long idProceso, Connection con) throws Exception
    {
        PreparedStatement st = null;
        try
        {
            String sql_tablaHistAdjuntoComunicacion = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_COMUNICACION");
            
            String sql_idProceso = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_COMUNICACION.idProceso");
            String sql_id = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_COMUNICACION.id");
            String sql_idComunicacion = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_COMUNICACION.idComunicacion");
            String sql_nombre = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_COMUNICACION.nombre");
            String sql_tipoMime = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_COMUNICACION.tipoMime");
            String sql_fecha = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_COMUNICACION.fecha");
            String sql_contenido = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_COMUNICACION.contenido");
            String sql_firma = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_COMUNICACION.firma");
            String sql_plataformaFirma = m_ConfigTechnical.getString("SQL.HIST_ADJUNTO_COMUNICACION.plataformaFirma");
            
            String query = "insert into "+sql_tablaHistAdjuntoComunicacion+"("
                          +sql_idProceso+", "+sql_id+", "+sql_idComunicacion+", "+sql_nombre
                          +", "+sql_tipoMime+", "+sql_fecha+", "+sql_contenido+", "+sql_firma
                          +", "+sql_plataformaFirma
                          +")"
                          +" values(?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            int res = 0;
            int total = 0;
            byte[] contenido = null;
            for(AdjuntoComunicacionVO a : adjuntos)
            {
                try
                {
                    contenido = expDAO.getContenidoAdjuntoComunicacion(a.getId(), a.getIdComunicacion(), con);

                    st = con.prepareStatement(query);

                    st.setLong(1, idProceso);
                    st.setInt(2, a.getId());
                    st.setInt(3, a.getIdComunicacion());
                    st.setString(4, a.getNombre());
                    st.setString(5, a.getTipoMime());
                    st.setTimestamp(6, a.getFecha() != null ? new java.sql.Timestamp(a.getFecha().getTimeInMillis()) : null);
                    if(contenido != null && contenido.length > 0)
                    {
                        java.io.InputStream is = new java.io.ByteArrayInputStream(contenido);
                        st.setBinaryStream(7, is, contenido.length);
                    }
                    else
                    {
                        st.setNull(7,java.sql.Types.BINARY);
                    }
                    st.setString(8, a.getFirma());
                    st.setString(9, a.getPlataformaFirma());

                    res = st.executeUpdate();
                    if(res <= 0)
                        throw new Exception("Error al insertar en "+sql_tablaHistAdjuntoComunicacion);
                    else
                        total += res;
                }
                finally
                {
                    if(st != null)
                        st.close();
                }
            }
            return total == adjuntos.size();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un error en el grabar los archivos adjuntos de una comunicación en el histórico: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st != null)
                st.close();
        }
    }
    
    /**
     * Grabar el contenido los objetos de la clase RegistroRelacionadoVO del parámetro adjuntos,
     * en la tabla HIST_E_EXR
     * 
     * @param registros
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarRegistrosRelacionados(ArrayList<RegistroRelacionadoVO> registros, long idProceso, 
            Connection con) throws Exception {
        log.debug("HistoricoExpedienteDAO.grabarRegistrosRelacionados()::BEGIN");
        PreparedStatement ps = null;
                
        try{
            for (RegistroRelacionadoVO reg : registros) {
                String sql = "INSERT INTO HIST_E_EXR (ID_PROCESO,EXR_DEP,EXR_UOR,EXR_TIP,EXR_EJR,EXR_NRE,EXR_ORI,EXR_EJE," +
                    "EXR_NUM,EXR_TOP,EXR_MUN,EXR_PRO,EXR_ORIGEN) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
                ps= con.prepareStatement(sql);
                JdbcOperations.setValues(ps,1,
                        idProceso,
                        reg.getCodDepartamentoRegistro(),
                        reg.getCodUnidadRegistro(),
                        reg.getTipoAnotacionRegistro(),
                        reg.getEjercicioAnotacionRegistro(),
                        reg.getNumAnotacionRegistro(),
                        reg.getOrigenAnotacionRegistro(),
                        reg.getEjercicioExpediente(),
                        reg.getNumExpediente(),
                        reg.getTipoOperacion(),
                        reg.getCodMunicipioExpediente(),
                        reg.getCodProcedimiento(),
                        reg.getOrigenExpediente());

                ps.executeUpdate();
                ps.close();
            }
            
            return true;
        }catch(SQLException e){
            log.error("Error al grabar las anotaciones de registro asociadas a un expediente en el histórico: " + e.getMessage());
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
    
    /**
     * Grabar el contenido los objetos de la clase RegistroRelacionadoExternoVO del parámetro adjuntos,
     * en la tabla HIST_E_EXR_EXT
     * 
     * @param registros
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarRegistrosExternosRelacionados(ArrayList<RegistroRelacionadoExternoVO> registros, long idProceso, Connection con,String[] params) throws Exception
    {
        log.debug("iHstoricoExpedienteDAO.grabarRegistrosExternosRelacionados()::BEGIN");
        Statement st = null;
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            
            String sql_tablaHistEExr = m_ConfigTechnical.getString("SQL.HIST_E_EXREXT");
            
            String sql_idProceso = m_ConfigTechnical.getString("SQL.HIST_E_EXREXT.idProceso");
            String sql_exrextUor = m_ConfigTechnical.getString("SQL.HIST_E_EXREXT.exrextUor");
            String sql_exrextTip = m_ConfigTechnical.getString("SQL.HIST_E_EXREXT.exrextTip");
            String sql_exrextEjr = m_ConfigTechnical.getString("SQL.HIST_E_EXREXT.exrextEjr");
            String sql_exrextNre = m_ConfigTechnical.getString("SQL.HIST_E_EXREXT.exrextNre");
            String sql_exrextMun = m_ConfigTechnical.getString("SQL.HIST_E_EXREXT.exrextMun");
            String sql_exrextNum = m_ConfigTechnical.getString("SQL.HIST_E_EXREXT.exrextNum");
            String sql_exrextOri = m_ConfigTechnical.getString("SQL.HIST_E_EXREXT.exrextOri");
            String sql_exrextTop = m_ConfigTechnical.getString("SQL.HIST_E_EXREXT.exrextTop");
            String sql_exrextSer = m_ConfigTechnical.getString("SQL.HIST_E_EXREXT.exrextSer");
            String sql_exrextPro = m_ConfigTechnical.getString("SQL.HIST_E_EXREXT.exrextPro");
            String sql_exrextFecalta = m_ConfigTechnical.getString("SQL.HIST_E_EXREXT.exrextFecalta");
            
            String query_p1 = "insert into "+sql_tablaHistEExr+"("
                          +sql_idProceso+", "+sql_exrextUor+", "+sql_exrextTip+", "+sql_exrextEjr
                          +", "+sql_exrextNre+", "+sql_exrextMun+", "+sql_exrextNum+", "+sql_exrextOri
                          +", "+sql_exrextTop+", "+sql_exrextSer+", "+sql_exrextPro+", "+sql_exrextFecalta
                          +")"
                          +" values(";
            
            String queryCompleta = null;
            int res = 0;
            int total = 0;
            for(RegistroRelacionadoExternoVO r : registros)
            {
                try
                {
                    queryCompleta = query_p1 
                              + idProceso
                              +", "+(r.getCodUnidadRegistro())
                              +", "+(r.getTipoAnotacionRegistro() != null ? "'"+r.getTipoAnotacionRegistro()+"'" : "null")
                              +", "+(r.getEjercicioAnotacionRegistro() != null ? "'"+r.getEjercicioAnotacionRegistro()+"'" : "null")
                              +", "+(r.getNumAnotacionRegistro())
                              +", "+(r.getCodMunicipioRegistro())
                              +", "+(r.getNumExpediente() != null ? "'"+r.getNumExpediente()+"'" : "null")
                              +", "+(r.getOrigenAnotacionRegistro() != null ? "'"+r.getOrigenAnotacionRegistro()+"'" : "null")
                              +", "+(r.getTipoOperacion() != null ? "'"+r.getTipoOperacion()+"'" : "null")
                              +", "+(r.getSerie() != null ? "'"+r.getSerie()+"'" : "null")
                              +", "+(r.getCodProcedimiento() != null ? "'"+r.getCodProcedimiento()+"'" : "null");
                              if (params[0].equals("sqlserver") || params[0].equals("SQLSERVER")) {
                                 queryCompleta+= ", "+(r.getFechaAlta() != null ? "CONVERT('"+sdf.format(r.getFechaAlta().getTime())+"', 'dd/MM/yyyy hh24:mi:ss')" : "null")  ;
                              }else
                              {
                                queryCompleta+=", "+(r.getFechaAlta() != null ? "TO_TIMESTAMP(datetime,'"+sdf.format(r.getFechaAlta().getTime())+"', 103)" : "null")  ;
                              }                             
                              queryCompleta+=")";
                              
                    st = con.createStatement();
                    log.debug(" (TRAZA DE CONTROL) SQL: " + queryCompleta);
                    res = st.executeUpdate(queryCompleta);
                    if(res <= 0)
                        throw new Exception("Error al insertar en "+sql_tablaHistEExr);
                    else
                        total += res;
                }
                finally
                {
                    if(st != null)
                        st.close();
                }
            }
            return total == registros.size();
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al grabar las anotaciones de registro externas asociadas a un expediente en el histórico: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }
    
    /**
     * Grabar el contenido los objetos de la clase DocumentoTramitacionVO del parámetro documentos,
     * en la tabla HIST_E_CRD
     * 
     * @param documentos
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarDocumentosTramitacion(ArrayList<DocumentoTramitacionVO> documentos, long idProceso, Connection con) throws Exception
    {
        log.debug("HistoricoExpedienteDAO.grabarDocumentosTramitacion()::BEGIN");
        PreparedStatement st = null;
        try
        {
            String sql_tablaHistECrd = m_ConfigTechnical.getString("SQL.HIST_E_CRD");
            
            String sql_idProceso = m_ConfigTechnical.getString("SQL.HIST_E_CRD.idProceso");
            String sql_crdMun = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdMun");
            String sql_crdPro = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdPro");
            String sql_crdEje = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdEje");
            String sql_crdNum = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdNum");
            String sql_crdTra = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdTra");
            String sql_crdOcu = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdOcu");
            String sql_crdNud = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdNud");
            String sql_crdFal = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdFal");
            String sql_crdFmo = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdFmo");
            String sql_crdUsc = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdUsc");
            String sql_crdUsm = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdUsm");
            String sql_crdFil = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdFil");
            String sql_crdDes = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdDes");
            String sql_crdDot = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdDot");
            String sql_crdFirEst = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdFirEst");
            String sql_crdExpFd = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdExpFd");
            String sql_crdDocFd = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdDocFd");
            String sql_crdFirFd = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdFirFd");
            String sql_crdFinf = m_ConfigTechnical.getString("SQL.HIST_E_CRD.crdFinf");
            
            String query = "insert into "+sql_tablaHistECrd+"("
                          +sql_idProceso+", "+sql_crdMun+", "+sql_crdPro+", "+sql_crdEje
                          +", "+sql_crdNum+", "+sql_crdTra+", "+sql_crdOcu+", "+sql_crdNud
                          +", "+sql_crdFal+", "+sql_crdFmo+", "+sql_crdUsc+", "+sql_crdUsm
                          +", "+sql_crdFil+", "+sql_crdDes+", "+sql_crdDot+", "+sql_crdFirEst
                          +", "+sql_crdExpFd+", "+sql_crdDocFd+", "+sql_crdFirFd+", "+sql_crdFinf
                          +")"
                          +" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);            
            int res = 0;
            int total = 0;
            byte[] contenido = null;
            for(DocumentoTramitacionVO d : documentos)
            {
                try{
                    contenido = expDAO.getContenidoDocumentoTramitacion(d.getCodMunicipio(), d.getEjercicio(), d.getCodProcedimiento(), d.getNumExpediente(), d.getNumDocumento(),d.getCodTramite(),d.getOcurrenciaTramite(),con);

                    st = con.prepareStatement(query);
                    st.setLong(1, idProceso);
                    st.setInt(2, d.getCodMunicipio());
                    st.setString(3, d.getCodProcedimiento());
                    st.setInt(4, d.getEjercicio());
                    st.setString(5, d.getNumExpediente());
                    st.setInt(6, d.getCodTramite());
                    st.setInt(7, d.getOcurrenciaTramite());
                    st.setInt(8, d.getNumDocumento());
                    st.setTimestamp(9, d.getFechaAlta() != null ? new java.sql.Timestamp(d.getFechaAlta().getTimeInMillis()) : null);
                    st.setTimestamp(10, d.getFechaModificacion() != null ? new java.sql.Timestamp(d.getFechaModificacion().getTimeInMillis()) : null);
                    st.setInt(11, d.getCodUsuarioAlta());
                    if(d.getCodUsuarioModificacion() != null)
                        st.setInt(12, d.getCodUsuarioModificacion());
                    else
                        st.setNull(12, java.sql.Types.INTEGER);
                    if(contenido != null && contenido.length > 0)
                    {
                        java.io.InputStream is = new java.io.ByteArrayInputStream(contenido);
                        st.setBinaryStream(13, is, contenido.length);
                    }
                    else
                    {
                        st.setNull(13,java.sql.Types.BINARY);
                    }
                    st.setString(14, d.getNombreDocumento());
                    st.setInt(15, d.getCodDocumento());
                    st.setString(16, d.getEstadoFirma());
                    st.setString(17, d.getFd());
                    st.setString(18, d.getDocFd());
                    if(d.getFirFd() != null)
                        st.setInt(19, d.getFirFd());
                    else
                        st.setNull(19, java.sql.Types.INTEGER);
                    st.setTimestamp(20, d.getFechaInforme() != null ? new java.sql.Timestamp(d.getFechaInforme().getTimeInMillis()) : null);

                    res = st.executeUpdate();
                    if(res <= 0)
                        throw new Exception("Error al insertar en "+sql_tablaHistECrd);
                    else
                        total += res;

                    if(d.getFirmas() != null && d.getFirmas().size() > 0)
                    {
                        boolean resultadoGrabar = true;
                        for(DocumentoTramitacionFirmaVO f : d.getFirmas())
                        {
                            resultadoGrabar = this.grabarFirmaDocumentoTramitacion(f, idProceso, con);
                            if(!resultadoGrabar)
                                throw new BDException("Error al grabar firmaDocumentosTramitacion para el expediente "+d.getNumExpediente()+" y documento "+d.getCodDocumento());
                        }
                    }
                    if(d.getUsuariosFirmantes()!= null && d.getUsuariosFirmantes().size() > 0)
                    {
                        boolean resultadoGrabar = true;
                        for(DocumentoTramitacionFirmantesVO f : d.getUsuariosFirmantes())
                        {
                            resultadoGrabar = this.grabarUsuarioFirmanteDocumentoTramitacion(f, idProceso, con);
                            if(!resultadoGrabar)
                                throw new BDException("Error al grabar usuarioFirmanteDocumentosTramitacion para el expediente "+d.getNumExpediente()+" y documento "+d.getCodDocumento());
                        }
                    }
                    if(d.getFirmaFlujo()!= null && d.getFirmaFlujo().size() > 0)
                    {
                        boolean resultadoGrabar = true;
                        for(DocumentoTramitacionFlujoVO f : d.getFirmaFlujo())
                        {
                            resultadoGrabar = this.grabarFirmaFlujoDocumentoTramitacion(f, idProceso, con);
                            if(!resultadoGrabar)
                                throw new BDException("Error al grabar firmaFlujoDocumentosTramitacion para el expediente "+d.getNumExpediente()+" y documento "+d.getCodDocumento());
                        }
                    }
                }
                finally
                {
                    if(st != null)
                        st.close();
                }
            }
            return total == documentos.size();
        }
        catch(Exception ex)
        {           
            log.error("Se ha producido un error al grabar la lista de documentos de tramitación: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }
    
    /**
     * Grabar el contenido del objeto de la clase DocumentoTramitacionFirmaVO del parámetro firma,
     * en la tabla HIST_E_CRD_FIR
     * 
     * @param firma
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarFirmaDocumentoTramitacion(DocumentoTramitacionFirmaVO firma, long idProceso, Connection con) throws Exception
    {
        PreparedStatement st = null;
        try
        {
            String sql_tablaHistECrdFir = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR");
            
            String sql_idProceso = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.idProceso");
            String sql_crdMun = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.crdMun");
            String sql_crdPro = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.crdPro");
            String sql_crdEje = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.crdEje");
            String sql_crdNum = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.crdNum");
            String sql_crdTra = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.crdTra");
            String sql_crdOcu = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.crdOcu");
            String sql_crdNud = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.crdNud");
            String sql_usuCod = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.usuCod");
            String sql_firEst = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.firEst");
            String sql_fir = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.fir");
            String sql_fxFirma = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.fxFirma");
            String sql_observ = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.observ");
            String sql_usuFir = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.usuFir");
            String sql_crdCodPfExt = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.crdCodPfExt");
            String sql_crdIdSolPfExt = m_ConfigTechnical.getString("SQL.HIST_E_CRD_FIR.crdIdSolPfExt");
            
            byte[] contenidoFirma = expDAO.getContenidoFirmaDocumentoTramitacion(firma.getCodMunicipio(), firma.getEjercicio(), firma.getCodProcedimiento(), firma.getNumExpediente(), firma.getCodTramite(), firma.getOcurrenciaTramite(), firma.getNumeroDocumento(), con);
            
            String query = "insert into "+sql_tablaHistECrdFir+"("
                          +sql_idProceso+", "+sql_crdMun+", "+sql_crdPro+", "+sql_crdEje
                          +", "+sql_crdNum+", "+sql_crdTra+", "+sql_crdOcu+", "+sql_crdNud
                          +", "+sql_usuCod+", "+sql_firEst+", "+sql_fir+", "+sql_fxFirma
                          +", "+sql_observ+", "+sql_usuFir+", "+sql_crdCodPfExt+", "+sql_crdIdSolPfExt
                          +")"
                          +"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.prepareStatement(query);
            
            st.setLong(1, idProceso);
            st.setInt(2, firma.getCodMunicipio());
            st.setString(3, firma.getCodProcedimiento());
            st.setInt(4, firma.getEjercicio());
            st.setString(5, firma.getNumExpediente());
            st.setInt(6, firma.getCodTramite());
            st.setInt(7, firma.getOcurrenciaTramite());
            st.setInt(8, firma.getNumeroDocumento());
            st.setInt(9, firma.getCodUsuario());
            st.setString(10, firma.getEstadoFirma());
            if(contenidoFirma != null && contenidoFirma.length > 0)
            {
                java.io.InputStream is = new java.io.ByteArrayInputStream(contenidoFirma);
                st.setBinaryStream(11, is, contenidoFirma.length);
            }
            else
            {
                st.setNull(11,java.sql.Types.BINARY);
            }
            st.setTimestamp(12, firma.getFxFirma() != null ? new java.sql.Timestamp(firma.getFxFirma().getTimeInMillis()) : null);
            st.setString(13, firma.getObservaciones());
            if(firma.getCodUsuarioFirma() != null)
              st.setInt(14, firma.getCodUsuarioFirma());
            else
                st.setNull(14, java.sql.Types.INTEGER);
            st.setString(15, firma.getPfExt());
            st.setString(16, firma.getSolPfExt());
            
            
            int res = st.executeUpdate();
            return res > 0;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al grabar la firma de un documento de tramitación en el histórico: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st != null)
                st.close();
        }
    }
    
    /**
     * Grabar el contenido del objeto de la clase DocumentoTramitacionFirmantesVO del atributo usuariosFirmande del documento de tramitación
     * en la tabla HIST_E_CRD_FIR_FIRMANTES
     * 
     * @param firma
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarUsuarioFirmanteDocumentoTramitacion(DocumentoTramitacionFirmantesVO firma, long idProceso, Connection con) throws Exception
    {
        PreparedStatement st = null;
        try
        {
            String sql_tablaHistECrdFir = "HIST_E_CRD_FIR_FIRMANTES";

            String sql_idProceso = "ID_PROCESO";
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
            String sql_fir = "FIRMA";
            
            byte[] contenidoFirma = expDAO.getContenidoFirmaFirmanteDocumentoTramitacion(firma.getCodMunicipio(), firma.getEjercicio(), firma.getCodProcedimiento(), firma.getNumExpediente(), firma.getCodTramite(), firma.getOcurrenciaTramite(), firma.getNumeroDocumento(), con);
            
            String query = "insert into "+sql_tablaHistECrdFir+"("
                          +sql_idProceso+", "+sql_crdMun+", "+sql_crdPro+", "+sql_crdEje
                          +", "+sql_crdNum+", "+sql_crdTra+", "+sql_crdOcu+", "+sql_crdNud
                          +", "+sql_usuCod+", "+sql_orden+", "+sql_firEst+", "+sql_firFecha
                          +", "+sql_fir+")"
                          +"values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.prepareStatement(query);
            int contbd = 1;
            st.setLong(contbd++, idProceso);
            st.setInt(contbd++, firma.getCodMunicipio());
            st.setString(contbd++, firma.getCodProcedimiento());
            st.setInt(contbd++, firma.getEjercicio());
            st.setString(contbd++, firma.getNumExpediente());
            st.setInt(contbd++, firma.getCodTramite());
            st.setInt(contbd++, firma.getOcurrenciaTramite());
            st.setInt(contbd++, firma.getNumeroDocumento());
            st.setInt(contbd++, firma.getIdUsuario());
            st.setInt(contbd++, firma.getOrden());
            st.setString(contbd++, firma.getEstadoFirma());
            st.setTimestamp(contbd++, firma.getFechaFirma()!= null ? new java.sql.Timestamp(firma.getFechaFirma().getTimeInMillis()) : null);
            if(contenidoFirma != null && contenidoFirma.length > 0)
            {
                java.io.InputStream is = new java.io.ByteArrayInputStream(contenidoFirma);
                st.setBinaryStream(contbd++, is, contenidoFirma.length);
            }
            else
            {
                st.setNull(contbd++,java.sql.Types.BINARY);
            }            
            
            int res = st.executeUpdate();
            return res > 0;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al grabar un usuario firmante de un documento de tramitación en el histórico: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st != null)
                st.close();
        }
    }
    
    /**
     * Grabar el contenido del objeto de la clase DocumentoTramitacionFlujoVO del atributo firmaFlujo de un documento de tramitación
     * en la tabla HIST_E_CRD_FIR_FLUJO
     * 
     * @param firma
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarFirmaFlujoDocumentoTramitacion(DocumentoTramitacionFlujoVO firma, long idProceso, Connection con) throws Exception
    {
        PreparedStatement st = null;
        try
        {
            String sql_tablaHistECrdFir = "HIST_E_CRD_FIR_FLUJO";

            String sql_idProceso = "ID_PROCESO";
            String sql_crdMun = "COD_MUNICIPIO";
            String sql_crdPro = "COD_PROCEDIMIENTO";
            String sql_crdEje = "EJERCICIO";
            String sql_crdNum = "NUM_EXPEDIENTE";
            String sql_crdTra = "COD_TRAMITE";
            String sql_crdOcu = "COD_OCURRENCIA";
            String sql_crdNud = "COD_DOCUMENTO";
            String sql_idTipoFirma = "ID_TIPO_FIRMA";
            
            String query = "insert into "+sql_tablaHistECrdFir+"("
                          +sql_idProceso+", "+sql_crdMun+", "+sql_crdPro+", "+sql_crdEje
                          +", "+sql_crdNum+", "+sql_crdTra+", "+sql_crdOcu+", "+sql_crdNud
                          +", "+sql_idTipoFirma+")"
                          +"values (?,?,?,?,?,?,?,?,?)";
            log.debug(" (TRAZA DE CONTROL) SQL: " + query);
            st = con.prepareStatement(query);
            int contbd = 1;
			
            st.setLong(contbd++, idProceso);
            st.setInt(contbd++, firma.getCodMunicipio());
            st.setString(contbd++, firma.getCodProcedimiento());
            st.setInt(contbd++, firma.getEjercicio());
            st.setString(contbd++, firma.getNumExpediente());
            st.setInt(contbd++, firma.getCodTramite());
            st.setInt(contbd++, firma.getOcurrenciaTramite());
            st.setInt(contbd++, firma.getNumeroDocumento());
			st.setLong(contbd++, firma.getIdTipoFirma());            
            
            int res = st.executeUpdate();
            return res > 0;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al grabar el flujo de firma de un documento de tramitación en el histórico: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st != null)
                st.close();
        }
    }
    
    /**
     * Grabar el contenido del objeto de la clase SituacionExpedienteAnuladoVO del parámetro situacion,
     * en la tabla HIST_E_EXPSIT
     * 
     * @param situacion
     * @param idProceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean grabarSituacionExpedienteAnulado(SituacionExpedienteAnuladoVO situacExp, long idProceso, 
            Connection con) throws Exception {
        log.debug("HistoricoExpedienteDAO.grabarSituacionExpedienteAnulado()::BEGIN");
        PreparedStatement ps = null;
                
        try{
            String sql = "INSERT INTO HIST_E_EXPSIT (ID_PROCESO,EXPSIT_NUM,EXPSIT_JUST,EXPSIT_USUARIO,"+
                "EXPSIT_AUTORIZA,EXPSIT_MUN,EXPSIT_EJE) " + 
                "VALUES (?,?,?,?,?,?,?)";

            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps= con.prepareStatement(sql);

            JdbcOperations.setValues(ps,1,
					idProceso,
					situacExp.getNumExpediente(),
					situacExp.getJustificacion(),
					situacExp.getUsuario(),
					situacExp.getAutoriza(),
					situacExp.getCodMunicipio(),
					situacExp.getEjercicio());

            ps.executeUpdate();
            return true;
        }catch(SQLException e){
            log.error("Error al grabar la causa de anulación de un expediente en el histórico: " + e.getMessage());
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

    /**
     * Grabar las operaciones de un expediente en la tabla HIST_OPERACIONES_EXPEDIENTE
     * 
     * @param operaciones
     * @param idProceso
     * @param con
     * @throws Exception 
     */
    public void grabarListaOperacionesHist (ArrayList<OperacionExpedienteVO> operaciones, 
            long idProceso, Connection con) throws Exception {
        log.debug("HistoricoExpedienteDAO.grabarOperacionesHist()::BEGIN");
        PreparedStatement ps = null;
                
        try{
            String sql = "INSERT INTO HIST_OPERACIONES_EXPEDIENTE (ID_PROCESO, ID_OPERACION, COD_MUNICIPIO, EJERCICIO, " + 
                "NUM_EXPEDIENTE, TIPO_OPERACION, FECHA_OPERACION, COD_USUARIO, DESCRIPCION_OPERACION) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";
            
            for (OperacionExpedienteVO operacion : operaciones) {
                OperacionExpedienteVO operConDesc = OperacionesExpedienteDAO.getInstance().recuperarOperacion(operacion.getIdOperacion(), false, con);
                log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
                ps= con.prepareStatement(sql);

                int i = 1;
                ps.setLong(i++,idProceso);
                ps.setInt(i++,operacion.getIdOperacion());
                ps.setInt(i++,operacion.getCodMunicipio());
                ps.setInt(i++,operacion.getEjercicio());
                ps.setString(i++,operacion.getNumExpediente());
                ps.setInt(i++,operacion.getTipoOperacion());
                ps.setTimestamp(i++, DateOperations.toTimestamp(operacion.getFechaOperacion()));
                ps.setInt(i++, operacion.getCodUsuario());     
                ps.setString(i++, operConDesc.getDescripcionOperacion());

                ps.executeUpdate();
                ps.close();
            }
        }catch(SQLException e){
            log.error("Error al grabar las operaciones de un expediente en el histórico: " + e.getMessage());
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

    public ExpedienteVO getExpedienteHistorico(int codOrganizacion,String numExpediente,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        String eje = numExpediente.substring(0, 4);
        ExpedienteVO resultado = null;
        try{
            String sql = "SELECT ID_PROCESO,EXP_PRO,EXP_EJE,EXP_NUM,EXP_FEI,EXP_FEF,EXP_EST,EXP_MUN,EXP_USU," +
                "EXP_UOR,EXP_PEND,EXP_TRA,EXP_TOCU,EXP_LOC,EXP_CLO,EXP_OBS,EXP_ASU,EXP_REF,EXP_IMP,EXP_UBICACION_DOC " + 
                    "FROM HIST_E_EXP WHERE EXP_MUN = ? AND EXP_EJE = ? AND EXP_NUM = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setInt(i++,Integer.parseInt(eje));
            ps.setString(i++,numExpediente);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                resultado = new ExpedienteVO();
                resultado.setIdProceso(rs.getLong("ID_PROCESO"));
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
            log.error("Error al grabar un expediente en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<CronoVO> getTramitesExpedienteHistorico(int codOrganizacion,String numExpediente,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<CronoVO> resultado = new ArrayList<CronoVO>();
        
        String sql = "SELECT CRO_PRO,CRO_EJE,CRO_TRA,CRO_FEI,CRO_FEF,CRO_USU," + 
                "CRO_UTR,CRO_MUN,CRO_OCU,CRO_FIP,CRO_FLI,CRO_FFP,CRO_RES,CRO_OBS,CRO_USF," + 
                "CRO_AVISADOCFP,CRO_AVISADOFDP " + 
                "FROM HIST_E_CRO WHERE CRO_MUN = ? AND CRO_NUM = ?";
            
        try{
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                CronoVO crono = new CronoVO();
                crono.setCodProcedimiento(rs.getString("CRO_PRO"));
                crono.setEjercicio(rs.getInt("CRO_EJE"));
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
            log.error("Error al recuperar el cronograma de un expediente del histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<ListTramOrigVO> getListaTramitesOrigenHistorico(int codOrganizacion,String numExpediente,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<ListTramOrigVO> resultado = new ArrayList<ListTramOrigVO>();
        
        try{
            String sql = "SELECT EJERCICIO,COD_PRO,COD_TRA_ORIGEN,OCU_TRA_ORIGEN,COD_TRA_DESTINO," +
                    "OCU_TRA_DESTINO " + 
                    "FROM HIST_LIST_TRAM_ORIG WHERE COD_MUN = ? AND NUM_EXP = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                ListTramOrigVO tramite = new ListTramOrigVO();
                tramite.setEjercicio(rs.getInt("EJERCICIO"));
                tramite.setCodProcedimiento(rs.getString("COD_PRO"));
                tramite.setCodMunicipio(codOrganizacion);
                tramite.setNumExpediente(numExpediente);
                tramite.setCodTramiteOrigen(rs.getInt("COD_TRA_ORIGEN"));
                tramite.setOcurrenciaTramiteOrigen(rs.getInt("OCU_TRA_ORIGEN"));
                tramite.setCodTramiteDestino(rs.getInt("COD_TRA_DESTINO"));
                tramite.setOcurrenciaTramiteDestino(rs.getInt("OCU_TRA_DESTINO"));
                
                resultado.add(tramite);
            }
        }catch(SQLException e){
            log.error("Error al recuperar el listado de trámites de origen y destino de un expediente en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<InteresadoExpedienteVO> getListaInteresadoExpedienteHistorico(int codOrganizacion,String numExpediente,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<InteresadoExpedienteVO> resultado = new ArrayList<InteresadoExpedienteVO>();
        
        try{
            String sql = "SELECT EXT_EJE,EXT_TER,EXT_NVR,EXT_DOT,EXT_ROL," +
                    "EXT_PRO,MOSTRAR,EXT_NOTIFICACION_ELECTRONICA " + 
                    "FROM HIST_E_EXT WHERE EXT_MUN = ? AND EXT_NUM = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                InteresadoExpedienteVO interesado = new InteresadoExpedienteVO();
                interesado.setEjercicio(rs.getInt("EXT_EJE"));
                interesado.setCodMunicipio(codOrganizacion);
                interesado.setNumExpediente(numExpediente);
                interesado.setCodTercero(rs.getInt("EXT_TER"));
                interesado.setVersionTercero(rs.getInt("EXT_NVR"));
                interesado.setCodDomicilio(rs.getInt("EXT_DOT"));
                interesado.setCodRol(rs.getInt("EXT_ROL"));
                interesado.setCodProcedimiento(rs.getString("EXT_PRO"));
                interesado.setMostrar(rs.getInt("MOSTRAR"));
                interesado.setNotificacionElectronica(rs.getString("EXT_NOTIFICACION_ELECTRONICA"));

                resultado.add(interesado);
            }
        }catch(SQLException e){
            log.error("Error al recuperar los autorizados de un expediente del histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<DocumentoPresentadoVO> getListaDocumentosPresentadosHistorico(int codOrganizacion,String numExpediente,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<DocumentoPresentadoVO> resultado = new ArrayList<DocumentoPresentadoVO>();
        
        try{
            String sql = "SELECT PRESENTADO_COD,PRESENTADO_EJE," +
                    "PRESENTADO_PRO,PRESENTADO_COD_DOC,PRESENTADO_CONTENIDO,PRESENTADO_TIPO," +
                    "PRESENTADO_EXTENSION,PRESENTADO_ORIGEN,PRESENTADO_FECHA_ALTA,PRESENTADO_NOMBRE," +
                    "PRESENTADO_COD_USU_ALTA,PRESENTADO_COD_USU_MOD,PRESENTADO_FECHA_MOD " + 
                    "FROM HIST_E_DOCS_PRESENTADOS WHERE PRESENTADO_MUN = ? AND PRESENTADO_NUM = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                DocumentoPresentadoVO documento = new DocumentoPresentadoVO();
                documento.setCodPresentado(rs.getInt("PRESENTADO_COD"));
                documento.setCodMunicipio(codOrganizacion);
                documento.setEjercicio(rs.getInt("PRESENTADO_EJE"));
                documento.setNumExpediente(numExpediente);
                documento.setCodProcedimiento(rs.getString("PRESENTADO_PRO"));
                documento.setCodDocumentoPresentado(rs.getInt("PRESENTADO_COD_DOC"));
                documento.setContenido(rs.getBytes("PRESENTADO_CONTENIDO"));
                documento.setTipoMime(rs.getString("PRESENTADO_TIPO"));
                documento.setExtension(rs.getString("PRESENTADO_EXTENSION"));
                documento.setOrigen(rs.getString("PRESENTADO_ORIGEN"));
                documento.setFechaAlta(DateOperations.timestampToCalendar(rs.getTimestamp("PRESENTADO_FECHA_ALTA")));
                documento.setNombreDocumento(rs.getString("PRESENTADO_NOMBRE"));
                documento.setCodUsuarioAlta(rs.getInt("PRESENTADO_COD_USU_ALTA"));
                documento.setCodUsuarioModificacion(rs.getInt("PRESENTADO_COD_USU_MOD"));
                documento.setFechaModificacion(DateOperations.timestampToCalendar(rs.getTimestamp("PRESENTADO_FECHA_MOD")));
                
                resultado.add(documento);
            }
        }catch(SQLException e){
            log.error("Error al recuperar la lista de documentos de inicio de un expediente de histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<DocsFirmasVO> getListaFirmasDocumentoPresentadoHistorico(int idDocPresentado,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<DocsFirmasVO> resultado = new ArrayList<DocsFirmasVO>();
        
        try{
            String sql = "SELECT ID_DOC_FIRMA,DOC_FIRMA_ESTADO,DOC_FIRMA_ORDEN," +
                    "DOC_FIRMA_UOR,DOC_FIRMA_CARGO,DOC_FIRMA_USUARIO,DOC_FIRMA_FECHA," +
                    "DOC_FECHA_ENVIO,FIRMA,DOC_FIRMA_OBSERVACIONES " + 
                    "FROM HIST_E_DOCS_FIRMAS WHERE ID_DOC_FIRMA = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps= con.prepareStatement(sql);
            ps.setInt(1,idDocPresentado);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                DocsFirmasVO firma = new DocsFirmasVO();
                firma.setIdDocFirma(rs.getInt("ID_DOC_FIRMA"));
                firma.setDocFirmaEstado(rs.getString("DOC_FIRMA_ESTADO"));
                firma.setDocFirmaOrden(rs.getInt("DOC_FIRMA_ORDEN"));
                firma.setDocFirmaUor(rs.getInt("DOC_FIRMA_UOR"));
                firma.setDocFirmaCargo(rs.getInt("DOC_FIRMA_CARGO"));
                firma.setDocFirmaUsuario(rs.getInt("DOC_FIRMA_USUARIO"));
                firma.setDocFirmaFecha(DateOperations.timestampToCalendar(rs.getTimestamp("DOC_FIRMA_FECHA")));
                firma.setIdDocPresentado(idDocPresentado);
                firma.setDocFechaEnvio(DateOperations.timestampToCalendar(rs.getTimestamp("DOC_FECHA_ENVIO")));
                firma.setFirma(rs.getBytes("FIRMA"));
                firma.setObservaciones(rs.getString("DOC_FIRMA_OBSERVACIONES"));
                
                resultado.add(firma);
            }
        }catch(SQLException e){
            log.error("Error al recuperar el listado de firma de un documento de inicio de expediente del histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<DocExtVO> getListaDocumentosExternosHistorico(int codOrganizacion,String numExpediente,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<DocExtVO> resultado = new ArrayList<DocExtVO>();
        
        try{
            String sql = "SELECT DOC_EXT_EJE,DOC_EXT_COD,DOC_EXT_NOM,DOC_EXT_FAL," + 
                    "DOC_EXT_TIP,DOC_EXT_EXT,DOC_EXT_ORIGEN " + 
                    "FROM HIST_E_DOC_EXT WHERE DOC_EXT_MUN = ? AND DOC_EXT_NUM = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                DocExtVO documento = new DocExtVO();
                documento.setNumExpediente(numExpediente);
                documento.setCodMunicipio(codOrganizacion);
                documento.setEjercicio(rs.getInt("DOC_EXT_EJE"));
                documento.setCodDocumento(rs.getInt("DOC_EXT_COD"));
                documento.setNombreDocumento(rs.getString("DOC_EXT_NOM"));
                documento.setFechaDocumento(DateOperations.timestampToCalendar(rs.getTimestamp("DOC_EXT_FAL")));
                documento.setTipoDocumento(rs.getString("DOC_EXT_TIP"));
                documento.setExtension(rs.getString("DOC_EXT_EXT"));
                documento.setOrigen(rs.getString("DOC_EXT_ORIGEN"));                
                resultado.add(documento);
            }
        }catch(SQLException e){
            log.error("Error al recuperar el listado de documentos externos de un expediente en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }

    public ArrayList<NotificacionVO> getListaNotificacionesHistorico(int codOrganizacion,String numExpediente,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<NotificacionVO> resultado = new ArrayList<NotificacionVO>();
        
        try{
            String sql = "SELECT CODIGO_NOTIFICACION,COD_PROCEDIMIENTO,EJERCICIO,COD_TRAMITE," + 
                    "OCU_TRAMITE,ACTO_NOTIFICADO,CADUCIDAD_NOTIFICACION,FIRMA,TEXTO_NOTIFICACION," + 
                    "FIRMADA,XML_NOTIFICACION,FECHA_ENVIO,REGISTRO_RT,FECHA_ACUSE,RESULTADO " + 
                    "FROM HIST_NOTIFICACION WHERE COD_MUNICIPIO = ? AND NUM_EXPEDIENTE = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                NotificacionVO notificacion = new NotificacionVO();
                notificacion.setCodNotificacion(rs.getInt("CODIGO_NOTIFICACION"));
                notificacion.setNumExpediente(numExpediente);
                notificacion.setCodProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                notificacion.setEjercicio(rs.getInt("EJERCICIO"));
                notificacion.setCodMunicipio(codOrganizacion);
                notificacion.setCodTramite(rs.getInt("COD_TRAMITE"));
                notificacion.setCodTramite(rs.getInt("OCU_TRAMITE"));
                notificacion.setActoNotificado(rs.getString("ACTO_NOTIFICADO"));
                notificacion.setCaducidadNotificacion(rs.getInt("CADUCIDAD_NOTIFICACION"));
                notificacion.setFirma(rs.getBytes("FIRMA"));
                notificacion.setTextoNotificacion(rs.getString("TEXTO_NOTIFICACION"));
                notificacion.setFirmada(rs.getString("FIRMADA"));
                notificacion.setXmlNotificacion(rs.getString("XML_NOTIFICACION"));
                notificacion.setFechaEnvio(DateOperations.timestampToCalendar(rs.getTimestamp("FECHA_ENVIO")));
                notificacion.setNumRegistroTelematico(rs.getString("REGISTRO_RT"));
                notificacion.setFechaAcuse(DateOperations.timestampToCalendar(rs.getTimestamp("FECHA_ACUSE")));
                notificacion.setResultado(rs.getString("RESULTADO"));
                
                resultado.add(notificacion);
            }
        }catch(SQLException e){
            log.error("Error al recuperar el listado de notificaciones de un expediente del histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<AdjuntoNotificacionVO> getListaAdjuntosNotificacionHistorico(int codigoNotificacion,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<AdjuntoNotificacionVO> resultado = new ArrayList<AdjuntoNotificacionVO>();
        
        try{
            String sql = "SELECT COD_MUNICIPIO,EJERCICIO,NUM_EXPEDIENTE,COD_PROCEDIMIENTO," + 
                    "COD_TRAMITE,OCU_TRAMITE,NUM_UNIDAD_DOC " + 
                    "FROM HIST_ADJUNTO_NOTIFICACION WHERE CODIGO_NOTIFICACION = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps= con.prepareStatement(sql);
            ps.setInt(1,codigoNotificacion);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                AdjuntoNotificacionVO adjunto = new AdjuntoNotificacionVO();
                adjunto.setCodNotificacion(codigoNotificacion);
                adjunto.setCodMunicipio(rs.getInt("COD_MUNICIPIO"));
                adjunto.setEjercicio(rs.getInt("EJERCICIO"));
                adjunto.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                adjunto.setCodProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                adjunto.setCodTramite(rs.getInt("COD_TRAMITE"));
                adjunto.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
                adjunto.setNumUnidadDocumento(rs.getInt("NUM_UNIDAD_DOC"));
                
                resultado.add(adjunto);
            }
        }catch(SQLException e){
            log.error("Error al recuperar la lista de adjuntos de una determinada notificación en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return resultado;
    }
    
    public ArrayList<AdjuntoExtNotificacionVO> getListaAdjuntosExternosNotificacionHistorico(int idNotificacion,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<AdjuntoExtNotificacionVO> resultado = new ArrayList<AdjuntoExtNotificacionVO>();
        
        try{
            String sql = "SELECT ID,COD_MUNICIPIO,NUM_EXPEDIENTE,COD_TRAMITE,OCU_TRAMITE,FIRMA," + 
                    "FECHA,PLATAFORMA_FIRMA,COD_USUARIO_FIRMA,NOMBRE,TIPO_MIME," +
                    "ESTADO_FIRMA,FECHA_FIRMA,COD_USUARIO_RECHAZO,FECHA_RECHAZO," +
                    "OBSERVACIONES_RECHAZO,TIPO_CERTIFICADO_FIRMA " + 
                    "FROM HIST_ADJUNTO_EXT_NOTIFICACION WHERE ID_NOTIFICACION = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);   
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,idNotificacion);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                AdjuntoExtNotificacionVO adjExt = new AdjuntoExtNotificacionVO();
                adjExt.setId(rs.getInt("ID"));
                adjExt.setCodMunicipio(rs.getInt("COD_MUNICIPIO"));
                adjExt.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                adjExt.setCodTramite(rs.getInt("COD_TRAMITE"));
                adjExt.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
                adjExt.setFirma(rs.getString("FIRMA"));
                adjExt.setFecha(DateOperations.timestampToCalendar(rs.getTimestamp("FECHA")));
                adjExt.setIdNotificacion(idNotificacion);
                adjExt.setPlataformaFirma(rs.getString("PLATAFORMA_FIRMA"));
                adjExt.setCodUsuarioFirma(rs.getInt("COD_USUARIO_FIRMA"));
                adjExt.setNombre(rs.getString("NOMBRE"));
                adjExt.setTipoMime(rs.getString("TIPO_MIME"));
                adjExt.setEstadoFirma(rs.getString("ESTADO_FIRMA"));
                adjExt.setFechaFirma(DateOperations.timestampToCalendar(rs.getTimestamp("FECHA_FIRMA")));
                adjExt.setCodUsuarioRechazo(rs.getInt("COD_USUARIO_RECHAZO"));
                adjExt.setFechaRechazo(DateOperations.timestampToCalendar(rs.getTimestamp("FECHA_RECHAZO")));
                adjExt.setObservacionesRechazo(rs.getString("OBSERVACIONES_RECHAZO"));
                adjExt.setTipoCertificadoFirma(rs.getInt("TIPO_CERTIFICADO_FIRMA"));
                
                resultado.add(adjExt);
            }
        }catch(SQLException e){
            log.error("Error al recuperar la lista de adjuntos externos de una determinada notificación en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<AutorizadoNotificacionVO> getListaAutorizadosNotificacionHistorico(int codNotificacion,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<AutorizadoNotificacionVO> resultado = new ArrayList<AutorizadoNotificacionVO>();
        
        try{
            String sql = "SELECT COD_MUNICIPIO,EJERCICIO,NUM_EXPEDIENTE,COD_TERCERO,VER_TERCERO " + 
                    "FROM HIST_AUTORIZADO_NOTIFICACION WHERE CODIGO_NOTIFICACION = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);            
            ps= con.prepareStatement(sql);
            ps.setInt(1,codNotificacion);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                AutorizadoNotificacionVO autorizado = new AutorizadoNotificacionVO();
                autorizado.setCodNotificacion(codNotificacion);
                autorizado.setCodMunicipio(rs.getInt("COD_MUNICIPIO"));
                autorizado.setEjercicio(rs.getInt("EJERCICIO"));
                autorizado.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                autorizado.setCodTercero(rs.getInt("COD_TERCERO"));
                autorizado.setVersionTercero(rs.getInt("VER_TERCERO"));
                
                resultado.add(autorizado);
            }
        }catch(SQLException e){
            log.error("Error al recuperar la lista de autorizados de una notificación en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<ComunicacionVO> getListaComunicacionesHistorico(int codOrganizacion,String numExpediente,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<ComunicacionVO> resultado = new ArrayList<ComunicacionVO>();
        
        try{
            String sql = "SELECT ID,ASUNTO,TEXTO,TIPO_DOCUMENTO,DOCUMENTO,NOMBRE,FECHA," +
                    "NUM_REGISTRO,ORIGEN_REGISTRO,XML_COMUNICACION,FIRMA,PLATAFORMA_FIRMA," +
                    "EJERCICIO,LEIDA " + 
                    "FROM HIST_COMUNICACION WHERE COD_ORGANIZACION = ? AND NUM_EXPEDIENTE = ?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            
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
                comunicacion.setEjercicio(rs.getInt("EJERCICIO"));
                comunicacion.setNumExpediente(numExpediente);
                comunicacion.setLeida(rs.getInt("LEIDA"));
                                
                resultado.add(comunicacion);
            }
        }catch(SQLException e){
            log.error("Error al recuperar el listado de comunicaciones de un expediente en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<AdjuntoComunicacionVO> getListaAdjuntosComunicacionHistorico(int idComunicacion,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<AdjuntoComunicacionVO> resultado = new ArrayList<AdjuntoComunicacionVO>();
        
        try{
            String sql = "SELECT ID,NOMBRE,TIPO_MIME,FECHA,FIRMA,PLATAFORMA_FIRMA " + 
                    "FROM HIST_ADJUNTO_COMUNICACION WHERE ID_COMUNICACION = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,idComunicacion);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                AdjuntoComunicacionVO adjComunicacion = new AdjuntoComunicacionVO();
                adjComunicacion.setId(rs.getInt("ID"));
                adjComunicacion.setIdComunicacion(idComunicacion);
                adjComunicacion.setNombre(rs.getString("NOMBRE"));
                adjComunicacion.setTipoMime(rs.getString("TIPO_MIME"));
                adjComunicacion.setFecha(DateOperations.timestampToCalendar(rs.getTimestamp("FECHA")));
                adjComunicacion.setFirma(rs.getString("FIRMA"));
                adjComunicacion.setPlataformaFirma(rs.getString("PLATAFORMA_FIRMA"));
                                
                resultado.add(adjComunicacion);
            }
        }catch(SQLException e){
            log.error("Error al recuperar el listado de adjuntos de una determinada comunicación en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<RegistroRelacionadoVO> getListaRegistroRelacionadoHistorico(int codOrganizacion,String numExpediente,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<RegistroRelacionadoVO> resultado = new ArrayList<RegistroRelacionadoVO>();
        
        try{
            String sql = "SELECT EXR_DEP,EXR_UOR,EXR_TIP,EXR_EJR,EXR_NRE,EXR_ORI,EXR_EJE,EXR_TOP," +
                    "EXR_PRO,EXR_ORIGEN " + 
                    "FROM HIST_E_EXR WHERE EXR_MUN = ? AND EXR_NUM = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                RegistroRelacionadoVO reg = new RegistroRelacionadoVO();
                reg.setCodDepartamentoRegistro(rs.getInt("EXR_DEP"));
                reg.setCodUnidadRegistro(rs.getInt("EXR_UOR"));
                reg.setTipoAnotacionRegistro(rs.getString("EXR_TIP"));
                reg.setEjercicioAnotacionRegistro(rs.getString("EXR_EJR"));
                reg.setNumAnotacionRegistro(rs.getInt("EXR_NRE"));
                reg.setOrigenAnotacionRegistro(rs.getInt("EXR_ORI"));
                reg.setEjercicioExpediente(rs.getInt("EXR_EJE"));
                reg.setNumExpediente(numExpediente);
                reg.setTipoOperacion(rs.getString("EXR_TOP"));
                reg.setCodMunicipioExpediente(codOrganizacion);
                reg.setCodProcedimiento(rs.getString("EXR_PRO"));
                reg.setOrigenExpediente(rs.getString("EXR_ORIGEN"));
                
                resultado.add(reg);
            }
        }catch(SQLException e){
            log.error("Error al recuperar el listado de anotaciones de registro asociados a un determinado expediente en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<RegistroRelacionadoExternoVO> getListaRegistroRelacionadoExternoHistorico(int codOrganizacion,String numExpediente,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<RegistroRelacionadoExternoVO> resultado = new ArrayList<RegistroRelacionadoExternoVO>();
        
        try{
            String sql = "SELECT EXREXT_UOR,EXREXT_TIP,EXREXT_EJR,EXREXT_NRE,EXREXT_ORI,EXREXT_TOP," +
                    "EXREXT_SER,EXREXT_PRO,EXREXT_FECALTA " + 
                    "FROM HIST_E_EXREXT WHERE EXREXT_MUN = ? AND EXREXT_NUM = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                RegistroRelacionadoExternoVO regExterno = new RegistroRelacionadoExternoVO();
                regExterno.setCodUnidadRegistro(rs.getInt("EXREXT_UOR"));
                regExterno.setTipoAnotacionRegistro(rs.getString("EXREXT_TIP"));
                regExterno.setEjercicioAnotacionRegistro(rs.getString("EXREXT_EJR"));
                regExterno.setNumAnotacionRegistro(rs.getLong("EXREXT_NRE"));
                regExterno.setCodMunicipioRegistro(codOrganizacion);
                regExterno.setNumExpediente(numExpediente);
                regExterno.setOrigenAnotacionRegistro(rs.getString("EXREXT_ORI"));
                regExterno.setTipoOperacion(rs.getString("EXREXT_TOP"));
                regExterno.setSerie(rs.getString("EXREXT_SER"));
                regExterno.setCodProcedimiento(rs.getString("EXREXT_PRO"));
                regExterno.setFechaAlta(DateOperations.timestampToCalendar(rs.getTimestamp("EXREXT_FECALTA")));
                
                resultado.add(regExterno);
            }
        }catch(SQLException e){
            log.error("Error al recuperar el listado de anotaciones de registro externas de un expediente en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<DocumentoTramitacionVO> getListaDocumentosTramitacionHistorico(int codOrganizacion,String numExpediente,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<DocumentoTramitacionVO> resultado = new ArrayList<DocumentoTramitacionVO>();
        
        try{
            String sql = "SELECT CRD_PRO,CRD_EJE,CRD_TRA,CRD_OCU,CRD_NUD,CRD_FAL,CRD_FMO," +
                    "CRD_USC,CRD_USM,CRD_FIL,CRD_DES,CRD_DOT,CRD_FIR_EST,CRD_EXP_FD,CRD_DOC_FD," +
                    "CRD_FIR_FD,CRD_FINF " +
                    "FROM HIST_E_CRD WHERE CRD_MUN = ? AND CRD_NUM = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            log.debug(" (TRAZA DE CONTROL) PARÁMETROS DE LA SQL: " + codOrganizacion + "-" + numExpediente); 
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                DocumentoTramitacionVO documento = new DocumentoTramitacionVO();
                documento.setCodMunicipio(codOrganizacion);
                documento.setCodProcedimiento(rs.getString("CRD_PRO"));
                documento.setEjercicio(rs.getInt("CRD_EJE"));
                documento.setNumExpediente(numExpediente);
                documento.setCodTramite(rs.getInt("CRD_TRA"));
                documento.setOcurrenciaTramite(rs.getInt("CRD_OCU"));
                documento.setNumDocumento(rs.getInt("CRD_NUD"));
                documento.setFechaAlta(DateOperations.timestampToCalendar(rs.getTimestamp("CRD_FAL")));
                documento.setFechaModificacion(DateOperations.timestampToCalendar(rs.getTimestamp("CRD_FMO")));
                documento.setCodUsuarioAlta(rs.getInt("CRD_USC"));
                documento.setCodUsuarioModificacion(rs.getInt("CRD_USM"));
                documento.setContenido(rs.getBytes("CRD_FIL"));
                documento.setNombreDocumento(rs.getString("CRD_DES"));
                documento.setCodDocumento(rs.getInt("CRD_DOT"));
                documento.setEstadoFirma(rs.getString("CRD_FIR_EST"));
                documento.setFd(rs.getString("CRD_EXP_FD"));
                documento.setDocFd(rs.getString("CRD_DOC_FD"));
                
                String crdFirFd = rs.getString("CRD_FIR_FD");
                if(crdFirFd==null)
                    documento.setFirFd(null);
                else { 
                    documento.setFirFd(new Integer(crdFirFd));                    
                }
                
                /** original
                documento.setFirFd(rs.getInt("CRD_FIR_FD"));
                **/
                documento.setFechaInforme(DateOperations.timestampToCalendar(rs.getTimestamp("CRD_FINF")));                
                resultado.add(documento);
            }
        }catch(SQLException e){
            log.error("Error al recuperar la lista de documentos de tramitación de un expediente en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<DocumentoTramitacionFirmaVO> getFirmaDocumentoTramitacionHistorico(int codOrganizacion,
            String numExpediente,int codTramite, int ocurrenciaTramite,int idDocumento,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<DocumentoTramitacionFirmaVO> resultado = new ArrayList<DocumentoTramitacionFirmaVO>();
        
        try{
            String sql = "SELECT CRD_PRO,CRD_EJE,USU_COD,FIR_EST,FX_FIRMA,OBSERV,USU_FIR," +
                    "CRD_COD_PF_EXT,CRD_ID_SOL_PF_EXT " +
                    "FROM HIST_E_CRD_FIR WHERE CRD_MUN = ? AND CRD_NUM = ? AND CRD_TRA = ? AND CRD_OCU = ? AND CRD_NUD = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrenciaTramite);
            ps.setInt(i++,idDocumento);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                DocumentoTramitacionFirmaVO firmaDocumento = new DocumentoTramitacionFirmaVO();
                firmaDocumento.setCodMunicipio(codOrganizacion);
                firmaDocumento.setCodProcedimiento(rs.getString("CRD_PRO"));
                firmaDocumento.setEjercicio(rs.getInt("CRD_EJE"));
                firmaDocumento.setNumExpediente(numExpediente);
                firmaDocumento.setCodTramite(codTramite);
                firmaDocumento.setOcurrenciaTramite(ocurrenciaTramite);
                firmaDocumento.setNumeroDocumento(idDocumento);
                firmaDocumento.setCodUsuario(rs.getInt("USU_COD"));
                firmaDocumento.setEstadoFirma(rs.getString("FIR_EST"));
                firmaDocumento.setFxFirma(DateOperations.timestampToCalendar(rs.getTimestamp("FX_FIRMA")));
                firmaDocumento.setObservaciones(rs.getString("OBSERV"));
                firmaDocumento.setCodUsuarioFirma(rs.getInt("USU_FIR"));
                firmaDocumento.setPfExt(rs.getString("CRD_COD_PF_EXT"));
                firmaDocumento.setSolPfExt(rs.getString("CRD_ID_SOL_PF_EXT"));
                
                resultado.add(firmaDocumento);
            }
        }catch(SQLException e){
            log.error("Error al recuperar la firma de un determinado documento de tramitación de un expediente en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<DocumentoTramitacionFirmantesVO> getUsuFirmantesDocumentoTramitacionHistorico(int codOrganizacion,
            String numExpediente,int codTramite, int ocurrenciaTramite,int idDocumento,Connection con) throws Exception{
        log.debug("HistoricoExpedienteDAO.getUsuFirmantesDocumentoTramitacionHistorico()::BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<DocumentoTramitacionFirmantesVO> resultado = new ArrayList<DocumentoTramitacionFirmantesVO>();
        
        try{
            String sql = "SELECT COD_PROCEDIMIENTO, EJERCICIO, ID_USUARIO, ORDEN, ESTADO_FIRMA, FECHA_FIRMA, FIRMA " +
                    "FROM HIST_E_CRD_FIR_FIRMANTES WHERE COD_MUNICIPIO = ? AND NUM_EXPEDIENTE = ? AND COD_TRAMITE = ? AND " +
					"COD_OCURRENCIA = ? AND COD_DOCUMENTO = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
			log.debug(" (TRAZA DE CONTROL) PARÁMETROS DE LA SQL: " + codOrganizacion + "-" + numExpediente + "-" + codTramite
				+ "-" + ocurrenciaTramite + "-" + idDocumento); 
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrenciaTramite);
            ps.setInt(i++,idDocumento);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                DocumentoTramitacionFirmantesVO firmaDocumento = new DocumentoTramitacionFirmantesVO();
                firmaDocumento.setCodMunicipio(codOrganizacion);
                firmaDocumento.setCodProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                firmaDocumento.setEjercicio(rs.getInt("EJERCICIO"));
                firmaDocumento.setNumExpediente(numExpediente);
                firmaDocumento.setCodTramite(codTramite);
                firmaDocumento.setOcurrenciaTramite(ocurrenciaTramite);
                firmaDocumento.setNumeroDocumento(idDocumento);
                firmaDocumento.setIdUsuario(rs.getInt("ID_USUARIO"));
                firmaDocumento.setEstadoFirma(rs.getString("ESTADO_FIRMA"));
                firmaDocumento.setFechaFirma(DateOperations.timestampToCalendar(rs.getTimestamp("FECHA_FIRMA")));
				firmaDocumento.setOrden(rs.getInt("ORDEN"));
				
                resultado.add(firmaDocumento);
            }
        }catch(SQLException e){
            log.error("Error al recuperar los usuarios firmantes de un determinado documento de tramitación de un expediente en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public ArrayList<DocumentoTramitacionFlujoVO> getFirmaFlujoDocumentoTramitacionHistorico(int codOrganizacion,
            String numExpediente,int codTramite, int ocurrenciaTramite,int idDocumento,Connection con) throws Exception{
        log.debug("HistoricoExpedienteDAO.getFirmaFlujoDocumentoTramitacionHistorico()::BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<DocumentoTramitacionFlujoVO> resultado = new ArrayList<DocumentoTramitacionFlujoVO>();
        
        try{
            String sql = "SELECT COD_PROCEDIMIENTO, EJERCICIO, ID_TIPO_FIRMA " +
                    "FROM HIST_E_CRD_FIR_FLUJO WHERE COD_MUNICIPIO = ? AND NUM_EXPEDIENTE = ? AND COD_TRAMITE = ? AND " +
					"COD_OCURRENCIA = ? AND COD_DOCUMENTO = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
			log.debug(" (TRAZA DE CONTROL) PARÁMETROS DE LA SQL: " + codOrganizacion + "-" + numExpediente + "-" + codTramite
				+ "-" + ocurrenciaTramite + "-" + idDocumento); 
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocurrenciaTramite);
            ps.setInt(i++,idDocumento);
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                DocumentoTramitacionFlujoVO firmaDocumento = new DocumentoTramitacionFlujoVO();
                firmaDocumento.setCodMunicipio(codOrganizacion);
                firmaDocumento.setCodProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                firmaDocumento.setEjercicio(rs.getInt("EJERCICIO"));
                firmaDocumento.setNumExpediente(numExpediente);
                firmaDocumento.setCodTramite(codTramite);
                firmaDocumento.setOcurrenciaTramite(ocurrenciaTramite);
                firmaDocumento.setNumeroDocumento(idDocumento);
                firmaDocumento.setIdTipoFirma(rs.getInt("ID_TIPO_FIRMA"));
                
                resultado.add(firmaDocumento);
            }
        }catch(SQLException e){
            log.error("Error al recuperar el flujo de firma de un determinado documento de tramitación de un expediente en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public SituacionExpedienteAnuladoVO getSituacionExpedienteAnulado(int codOrganizacion,String numExpediente,Connection con) throws Exception{
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        SituacionExpedienteAnuladoVO resultado = null;
        
        try{
            String sql = "SELECT EXPSIT_JUST,EXPSIT_USUARIO,EXPSIT_AUTORIZA " + 
                    "FROM HIST_E_EXPSIT WHERE EXPSIT_MUN = ? AND EXPSIT_NUM = ? AND EXPSIT_EJE = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            ps= con.prepareStatement(sql);
            int i = 1;
            int ejercicio = Integer.parseInt(numExpediente.substring(0, 4));
            
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,numExpediente);
            ps.setInt(i++,ejercicio);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                resultado = new SituacionExpedienteAnuladoVO();
                resultado.setNumExpediente(numExpediente);
                resultado.setJustificacion(rs.getString("EXPSIT_JUST"));
                resultado.setUsuario(rs.getString("EXPSIT_USUARIO"));
                resultado.setAutoriza(rs.getString("EXPSIT_AUTORIZA"));
                resultado.setCodMunicipio(codOrganizacion);
                resultado.setEjercicio(ejercicio);
            }
        }catch(SQLException e){
            log.error("Error al recuperar la causa de anulación de un expediente en el históricos: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
        public ArrayList<OperacionExpedienteVO> getListaOperacionesHist (int codOrganizacion, 
                String numExpediente, Connection con) throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList <OperacionExpedienteVO> resultado = new ArrayList <OperacionExpedienteVO>();
    
        try {
            String sql = "SELECT ID_OPERACION, COD_MUNICIPIO, EJERCICIO, NUM_EXPEDIENTE, " + 
                    "TIPO_OPERACION, FECHA_OPERACION, COD_USUARIO, DESCRIPCION_OPERACION" +
                  " FROM HIST_OPERACIONES_EXPEDIENTE" +
                  " WHERE COD_MUNICIPIO = ? AND NUM_EXPEDIENTE = ? ORDER BY FECHA_OPERACION DESC";
            
            if (log.isDebugEnabled())log.debug(sql);
         
            ps = con.prepareStatement(sql);
            ps.setInt(1, codOrganizacion);
            ps.setString(2, numExpediente);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                OperacionExpedienteVO operacionExpedienteVO = new OperacionExpedienteVO();
                operacionExpedienteVO.setIdOperacion(rs.getInt("ID_OPERACION"));
                operacionExpedienteVO.setEjercicio(rs.getInt("EJERCICIO"));                
                operacionExpedienteVO.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));                                
                operacionExpedienteVO.setTipoOperacion(rs.getInt("TIPO_OPERACION"));
                Timestamp fecha = rs.getTimestamp("FECHA_OPERACION");
                operacionExpedienteVO.setFechaOperacion(DateOperations.timestampToCalendar(fecha)); 
                operacionExpedienteVO.setCodUsuario(rs.getInt("COD_USUARIO"));
                operacionExpedienteVO.setDescripcionOperacion(rs.getString("DESCRIPCION_OPERACION"));                                
             
                resultado.add(operacionExpedienteVO);
            }
            rs.close();
       }catch(SQLException e){
            if (log.isDebugEnabled()) {log.error(e.getMessage());}
            e.printStackTrace();            
            throw new Exception(e.getMessage(), e);
        } finally {
            try{
                if (ps!=null) {ps.close();}
                if (rs!=null) {rs.close();}                
            }catch(SQLException sqle){
                if (log.isDebugEnabled()) {log.error(sqle.getMessage());}                
                sqle.printStackTrace();                
                throw new Exception(sqle.getMessage(), sqle);
            }
        }
        return resultado;
    }
    
    public ArrayList<ValorCampoSuplementarioVO> getValoresDatosSuplementarios(int codMunicipio, String numero, 
            AdaptadorSQLBD oad,Connection con) throws Exception {        
        
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
            String sql = "SELECT TNU_EJE,TNU_COD," + oad.convertir("TNU_VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null) + " AS TNU_VALOR " +
                "FROM HIST_E_TNU WHERE TNU_MUN =? AND TNU_NUM =?";
            
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
                valor.setEjercicio(rs.getInt("TNU_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodDatoSuplementario(rs.getString("TNU_COD"));
                valor.setValorDatoSuplementario(rs.getString(3));
                
                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de campos numéricos de expediente " + numero + " en el histórico: " + e.getMessage());
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
                    "FROM HIST_E_TNUT WHERE TNUT_MUN =? AND TNUT_NUM =?";            
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
            log.error("Error al recuperar los valores de campos numéricos a nivel de trámite del expediente " + numero + " en el historico: " + e.getMessage());
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
            
            String sql = "SELECT TXT_EJE,TXT_COD,TXT_VALOR " + 
                    "FROM HIST_E_TXT WHERE TXT_MUN = ? AND TXT_NUM = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            
            int i= 1;
            ps = con.prepareStatement(sql);            
            ps.setInt(i++,codMunicipio);
            ps.setString(i++,numero);
            
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
            log.error("Error al recuperar los valores de campos texto a nivel del expediente " + numero + " en el historico: " + e.getMessage());
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
                "FROM HIST_E_TXTT WHERE TXTT_MUN  = ? AND txtt_num =?";
            
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
            log.error("Error al recuperar los valores de campos texto a nivel de trámite del expediente " + numero + " en el historico: " + e.getMessage());
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
            sql += " FROM HIST_E_TFE WHERE tfe_mun =? AND tfe_num =?";
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
            log.error("Error al recuperar los valores de campos fecha a nivel de del expediente " + numero + " en el historico: " + e.getMessage());
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
                "FROM HIST_E_TFET WHERE tfet_mun =? AND tfet_num =?";

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
            log.error("Error al recuperar los valores de campos fecha a nivel de trámite del expediente " + numero + " en el historico: " + e.getMessage());
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
            String sql = "SELECT TTL_EJE,TTL_COD,TTL_VALOR FROM HIST_E_TTL WHERE ttl_mun = ? AND ttl_num =?";
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
            log.error("Error al recuperar los valores de campos texto largo a nivel del expediente " + numero + " en el historico: " + e.getMessage());
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
                    "FROM HIST_E_TTLT WHERE ttlt_mun = ? AND ttlt_num =?";

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
            log.error("Error al recuperar los valores de campos texto largo a nivel trámite del expediente " + numero + " en el historico: " + e.getMessage());
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
                "FROM HIST_E_TFI WHERE tfi_mun =? AND tfi_num =?";
            
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
            log.error("Error al recuperar los valores de campos fichero a nivel del expediente " + numero + " en el historico: " + e.getMessage());
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
                    "FROM HIST_E_TFIT WHERE tfit_mun =? AND tfit_num =?";
            
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
            log.error("Error al recuperar los valores de campos fichero a nivel de trámite del expediente " + numero + " en el historico: " + e.getMessage());
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
            String sql = "SELECT TDE_EJE,TDE_COD,TDE_VALOR FROM HIST_E_TDE WHERE tde_mun=? AND tde_num =?";            
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
                valor.setEjercicio(rs.getInt("TDE_EJE"));
                valor.setNumExpediente(numero);
                valor.setCodDatoSuplementario(rs.getString("TDE_COD"));
                valor.setValorDatoSuplementario(rs.getString("TDE_VALOR"));

                resultado.add(valor);
            }
        } catch (Exception e) {
            resultado = null;
            e.printStackTrace();
            log.error("Error al recuperar los valores de campos desplegable a nivel de expediente " + numero + " en el historico: " + e.getMessage());
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
                "FROM HIST_E_TDET WHERE tdet_mun =? AND tdet_num =?";
            
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
            log.error("Error al recuperar los valores de campos desplegable a nivel trámite del expediente " + numero + " en el historico: " + e.getMessage());
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
                "FROM HIST_E_TNUC WHERE tnuc_mun  = " + codMunicipio + " AND tnuc_num ='" + numero + "'";

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
            log.error("Error al recuperar los valores de campos numéricos calculados a nivel del expediente " + numero + " en el historico: " + e.getMessage());
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
                "FROM HIST_E_TNUCT WHERE tnuct_mun = " + codMunicipio + " AND tnuct_num ='" + numero + "'";

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
            log.error("Error al recuperar los valores de campos numéricos calculados a nivel de trámite del expediente " + numero + " en el historico: " + e.getMessage());
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
                "FROM HIST_E_TFEC WHERE tfec_mun = " + codMunicipio + " AND tfec_num ='" + numero + "'";

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
            log.error("Error al recuperar los valores de campos fecha calculados a nivel del expediente " + numero + " en el historico: " + e.getMessage());
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
                    "FROM HIST_E_TFECT WHERE tfect_mun = " + codMunicipio + " AND tfect_num ='" + numero + "'";

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
            log.error("Error al recuperar los valores de campos fecha calculados a nivel de trámite del expediente " + numero + " en el historico: " + e.getMessage());
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

            String sql = "SELECT TDEX_EJE,TDEX_COD,TDEX_VALOR, TDEX_CODSEL FROM HIST_E_TDEX WHERE TDEX_MUN = " + codMunicipio +
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
            log.error("Error al recuperar los valores de campos desplegables externos definidos a nivel de expediente " + numero + " en el historico: " + e.getMessage());
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
                    "FROM HIST_E_TDEXT WHERE TDEXT_MUN = " + codMunicipio + " AND TDEXT_NUM ='" + numero +"'";

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
            log.error("Error al recuperar los valores de campos desplegables externos definidos a nivel de trámite en el expediente " + numero + " del historico: " + e.getMessage());
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
    
    public byte[] getContenidoDocumentoExterno(DocExtVO documento, Connection con) throws Exception {
    
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[] resultado = null;
        
        try{
            String sql = "SELECT DOC_EXT_FIL FROM HIST_E_DOC_EXT WHERE DOC_EXT_COD = ?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,documento.getCodDocumento());
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                resultado = rs.getBytes("DOC_EXT_FIL");
            }
        }catch(SQLException e){
            log.error("Error al recuperar el contenido de un documento externo de un documento de un expediente en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
            
    public byte[] getContenidoAdjuntoExternoNotificacion(AdjuntoExtNotificacionVO adjunto, Connection con) throws Exception {
    
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[] resultado = null;
        
        try{
            String sql = "SELECT CONTENIDO FROM HIST_ADJUNTO_EXT_NOTIFICACION WHERE ID = ?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,adjunto.getId());
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                resultado = rs.getBytes("CONTENIDO");
            }
        }catch(SQLException e){
            log.error("Error al recuperar el contenido de un adjunto externo de una notificación en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
            
    public byte[] getContenidoAdjuntoComunicacion(AdjuntoComunicacionVO adjComunicacion, Connection con) throws Exception {
    
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[] resultado = null;
        
        try{
            String sql = "SELECT CONTENIDO FROM HIST_ADJUNTO_COMUNICACION WHERE ID = ?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,adjComunicacion.getId());
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                resultado = rs.getBytes("CONTENIDO");
            }
        }catch(SQLException e){
            log.error("Error al recuperar el contenido de un adjunto de una notificación en el histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
            
    public byte[] getContenidoFirmaDocumentoTramitacion(DocumentoTramitacionFirmaVO docTramFirma, Connection con) throws Exception {
    
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[] resultado = null;
        
        try{
            String sql = "SELECT FIR FROM HIST_E_CRD_FIR WHERE CRD_MUN = ? AND " +
                    "CRD_PRO = ? AND CRD_EJE = ? AND CRD_NUM = ? AND CRD_TRA = ? AND CRD_OCU = ? " + 
                    "AND CRD_NUD = ? AND USU_COD = ?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,docTramFirma.getCodMunicipio());
            ps.setString(i++,docTramFirma.getCodProcedimiento());
            ps.setInt(i++,docTramFirma.getEjercicio());
            ps.setString(i++,docTramFirma.getNumExpediente());
            ps.setInt(i++,docTramFirma.getCodTramite());
            ps.setInt(i++,docTramFirma.getOcurrenciaTramite());
            ps.setInt(i++,docTramFirma.getNumeroDocumento());
            ps.setInt(i++,docTramFirma.getCodUsuario());
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                resultado = rs.getBytes("FIR");
            }
        }catch(SQLException e){
            log.error("Error al recuperar el contenido la firma de un documento de tramitación del histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
            
    public byte[] getContenidoFirmaUsuFirmanteDocumentoTramitacion(DocumentoTramitacionFirmantesVO docTramFirma, Connection con) throws Exception {
    
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[] resultado = null;
        
        try{
            String sql = "SELECT FIRMA FROM HIST_E_CRD_FIR_FIRMANTES WHERE COD_MUNICIPIO = ? AND " +
                    "COD_PROCEDIMIENTO = ? AND EJERCICIO = ? AND NUM_EXPEDIENTE = ? AND COD_TRAMITE = ? AND COD_OCURRENCIA = ? " + 
                    "AND COD_DOCUMENTO = ? AND ID_USUARIO = ?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            
            ps= con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,docTramFirma.getCodMunicipio());
            ps.setString(i++,docTramFirma.getCodProcedimiento());
            ps.setInt(i++,docTramFirma.getEjercicio());
            ps.setString(i++,docTramFirma.getNumExpediente());
            ps.setInt(i++,docTramFirma.getCodTramite());
            ps.setInt(i++,docTramFirma.getOcurrenciaTramite());
            ps.setInt(i++,docTramFirma.getNumeroDocumento());
            ps.setInt(i++,docTramFirma.getIdUsuario());
            
            rs = ps.executeQuery();
            
            while (rs.next()){
                resultado = rs.getBytes("FIRMA");
            }
        }catch(SQLException e){
            log.error("Error al recuperar el contenido de la firma de un usuario firmante de un documento de tramitación del histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
            
    public byte[] getContenidoFicheroCampoSuplementario(ValorCampoSuplementarioVO valor, Connection con) throws Exception {
    
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[] resultado = null;
        
        try{
            String sql = "SELECT TFI_VALOR FROM HIST_E_TFI WHERE TFI_MUN = ? AND " +
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
            log.error("Error al recuperar el contenido de un campo de tipo fichero de un expediente del histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
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
            String sql = "SELECT TFIT_VALOR FROM HIST_E_TFIT WHERE TFIT_MUN = ? AND TFIT_PRO = ? AND " +
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
            log.error("Error al recuperar el contenido de un campo de tipo fichero a nivel de trámite de un expediente del histórico: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
            
    public void eliminarExpedienteHistorico(Long idProceso, int idUsuario, Connection con,String[] params) throws Exception {
        
        try{ 
            ExpedienteEnvioHistoricoVO envio = getEnvioHistorico(idProceso,con);
            borrarTablasHistorico(idProceso,con);
            insertarRegistroEnvioHistorico(envio, idUsuario,con,params);
        }catch(Exception e){
            log.error("Error al recuperar el expediente de la tabla de expedientes históricos: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }
    }

    public ExpedienteEnvioHistoricoVO getEnvioHistorico(Long idProceso, Connection con) throws Exception {
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ExpedienteEnvioHistoricoVO resultado = null;
        
        try{
            String sql = "SELECT COD_ORGANIZACION,NUM_EXPEDIENTE,FECHA_ENVIO_ARCHIVO,PROCESADO,FECHA_PROCESO," +
                    "ERROR_PROCESO,COD_USUARIO,FECHA_RECUPERACION,COD_USUARIO_RECUP " + 
                    "FROM EXP_ENVIO_HISTORICO WHERE ID = ?";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            ps= con.prepareStatement(sql);
            ps.setLong(1,idProceso);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                resultado = new ExpedienteEnvioHistoricoVO();
                resultado.setId(idProceso);
                resultado.setCodOrganizacion(rs.getInt("COD_ORGANIZACION"));
                resultado.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                resultado.setFecEnvArchivo(DateOperations.timestampToCalendar(rs.getTimestamp("FECHA_ENVIO_ARCHIVO")));
                resultado.setProcesado(rs.getInt("PROCESADO"));
                resultado.setFechaProceso(DateOperations.timestampToCalendar(rs.getTimestamp("FECHA_PROCESO")));
                resultado.setErrorProceso(rs.getString("ERROR_PROCESO"));
                resultado.setCodUsuario(rs.getInt("COD_USUARIO"));
                resultado.setFechaRecuperacion(DateOperations.timestampToCalendar(rs.getTimestamp("FECHA_RECUPERACION")));
                resultado.setCodUsuarioRecuperacion(rs.getInt("COD_USUARIO_RECUP"));
            }
        }catch(SQLException e){
            log.error("Error al recuperar el contenido de la tabla EXP_ENVIO_HISTORICO: " + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage(),e);
        }finally{
            try{
                if(ps!=null) ps.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return resultado;
    }
    
    public void borrarTablasHistorico(Long idProceso, Connection con) throws Exception {
        
        PreparedStatement ps = null;
        
        try{ //Cada tabla de expedientes históricos tiene una clave externa con delete cascade referenciada a la tabla de envíos
            String sql = "DELETE FROM EXP_ENVIO_HISTORICO WHERE ID = ?";
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            ps= con.prepareStatement(sql);
            ps.setLong(1,idProceso);
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al eliminar el expediente de la tabla de expedientes históricos: " + e.getMessage());
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
    
    public void insertarRegistroEnvioHistorico(ExpedienteEnvioHistoricoVO envio, int idUsuario,  Connection con,String[] params) throws Exception {
        
        PreparedStatement ps = null;
         String sql ="";
        try{ //Cada tabla de expedientes históricos tiene una clave externa con delete cascade referenciada a la tabla de envíos
               if (params[0].equals("sqlserver") || params[0].equals("SQLSERVER")) {
                   sql= "INSERT INTO EXP_ENVIO_HISTORICO"
                           + " (COD_ORGANIZACION,NUM_EXPEDIENTE,FECHA_ENVIO_ARCHIVO,PROCESADO,FECHA_PROCESO,ERROR_PROCESO,COD_USUARIO,FECHA_RECUPERACION,COD_USUARIO_RECUP) "
                           + " VALUES (?,?,?,?,?,'"+envio.getErrorProceso()+"',?,?,?)";
               }
               else{
                   sql = "INSERT INTO EXP_ENVIO_HISTORICO VALUES (?,?,?,?,?,?,?,?,?,?)";
               }
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            
            ps= con.prepareStatement(sql);
            int i = 1;
             if (!params[0].equals("sqlserver") && !params[0].equals("SQLSERVER")) {
                 ps.setLong(i++,envio.getId());             
             }
            ps.setInt(i++,envio.getCodOrganizacion());
            ps.setString(i++,envio.getNumExpediente());
            ps.setTimestamp(i++,DateOperations.toTimestamp(envio.getFecEnvArchivo()));
            ps.setInt(i++,envio.getProcesado());
            ps.setTimestamp(i++,DateOperations.toTimestamp(envio.getFechaProceso()));
            if (!params[0].equals("sqlserver") && !params[0].equals("SQLSERVER")) 
                ps.setString(i++,envio.getErrorProceso());
            ps.setInt(i++,envio.getCodUsuario());
            ps.setTimestamp(i++,new Timestamp((new Date()).getTime()));
            ps.setInt(i++,idUsuario);
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al insertar registro en la tabla EXP_ENVIO_HISTORICO: " + e.getMessage());
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
    
    public boolean grabarValoresDatosSuplementarios(Long idProceso, ArrayList<ValorCampoSuplementarioVO> valoresDatosSup, 
            AdaptadorSQLBD oad,Connection con) throws Exception {        
        log.debug("HIstoricoExpedienteDAO.grabarValoresDatosSuplementarios()::BEGIN");
        try{ 
            for(ValorCampoSuplementarioVO valorDato: valoresDatosSup) {
                if (valorDato.getTipoDatoSuplementario() == 1) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoNumerico(oad, con, valorDato, idProceso);
                    } else {
                        insertarDatoNumericoTramite(oad, con, valorDato, idProceso);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 2) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoTexto(oad, con, valorDato, idProceso);
                    } else {
                        insertarDatoTextoTramite(oad, con, valorDato, idProceso);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 3) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoFecha(oad, con, valorDato, idProceso);
                    } else {
                        insertarDatoFechaTramite(oad, con, valorDato, idProceso);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 4) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoTextoLargo(oad, con, valorDato, idProceso);
                    } else {
                        insertarDatoTextoLargoTramite(oad, con, valorDato, idProceso);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 5) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoFichero(oad, con, valorDato, idProceso);
                    } else {
                        insertarDatoFicheroTramite(oad, con, valorDato, idProceso);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 6) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoDesplegable(oad, con, valorDato, idProceso);
                    } else {
                        insertarDatoDesplegableTramite(oad, con, valorDato, idProceso);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 8) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoNumericoCal(oad, con, valorDato, idProceso);
                    } else {
                        insertarDatoNumericoTramiteCal(oad, con, valorDato, idProceso);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 9) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoFechaCal(oad, con, valorDato, idProceso);
                    } else {
                        insertarDatoFechaTramiteCal(oad, con, valorDato, idProceso);
                    }
                } else if (valorDato.getTipoDatoSuplementario() == 10) {
                    if (valorDato.getCodTramite() <= 0) {
                        insertarDatoDesplegableExt(oad, con, valorDato, idProceso);
                    } else {
                        insertarDatoDesplegableExtTramite(oad, con, valorDato, idProceso);
                    }
                }
            }
            return true;
        }catch(Exception e){
            log.error("Error al grabar el expediente en las tablas de expedientes activos: ", e);
            throw new Exception(e);
        }
        
    }
    
    public void insertarDatoNumerico(AdaptadorSQLBD oad, Connection con, 
           ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{
        
        Statement stmt = null;
                   
        try {
            String sql = "INSERT INTO HIST_E_TNU (ID_PROCESO, TNU_MUN,TNU_EJE,TNU_NUM,TNU_COD,TNU_VALOR) " + 
                    "VALUES ("+idProceso+","+ valor.getCodOrganizacion() + ","+ valor.getEjercicio() +",'" + valor.getNumExpediente() + 
                    "','"+valor.getCodDatoSuplementario() +"',"+ oad.convertir("'" + valor.getValorDatoSuplementario() + "'", 
                            oad.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);             
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException e){
            log.error("Error al grabar el valor campo numérico de expediente en el histórico: " + e.getMessage());
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
        Connection con,ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{
    
        Statement stmt = null;
                   
        try {
            String sql = "INSERT INTO HIST_E_TNUT (ID_PROCESO, TNUT_MUN,TNUT_PRO,TNUT_EJE,TNUT_NUM,TNUT_TRA,TNUT_OCU,TNUT_COD,TNUT_VALOR) " + 
                    "VALUES ("+idProceso+","+ valor.getCodOrganizacion() + ",'" + valor.getProcedimiento() + "',"+ valor.getEjercicio() + ",'" + 
                    valor.getNumExpediente() + "'," + valor.getCodTramite() + "," + valor.getOcurrenciaTramite() + ",'" + 
                    valor.getCodDatoSuplementario()+"',"+ oad.convertir("'" + valor.getValorDatoSuplementario() + "'", 
                    oad.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql); 
            
            int i =1;
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException e){
            log.error("Error al grabar el valor campo numérico de trámite en el histórico: " + e.getMessage());
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
           ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{
        PreparedStatement ps = null;
        
        try {
            
            String sql = "INSERT INTO HIST_E_TXT (ID_PROCESO, TXT_MUN,TXT_EJE,TXT_NUM,TXT_COD,TXT_VALOR) " + 
                    "VALUES (?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            ps = con.prepareStatement(sql);
            JdbcOperations.setValues(ps,1,
				idProceso,
				valor.getCodOrganizacion(),
				valor.getEjercicio(),
				valor.getNumExpediente(),
				valor.getCodDatoSuplementario(),
				valor.getValorDatoSuplementario());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor campo de texto en el histórico: " + e.getMessage());
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
           ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{
        
        PreparedStatement ps = null;
        
        try {
            
            String sql = "INSERT INTO HIST_E_TXTT (ID_PROCESO,TXTT_MUN,TXTT_PRO,TXTT_EJE,TXTT_NUM,TXTT_TRA,TXTT_OCU,TXTT_COD,TXTT_VALOR) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);   
            
            int i =1;
            ps = con.prepareStatement(sql);
            JdbcOperations.setValues(ps,1,
				idProceso,
				valor.getCodOrganizacion(),
				valor.getProcedimiento(),
				valor.getEjercicio(),
				valor.getNumExpediente(),
				valor.getCodTramite(),
				valor.getOcurrenciaTramite(),
				valor.getCodDatoSuplementario(),
				valor.getValorDatoSuplementario());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor campo de texto de trámite en el histórico: " + e.getMessage());
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
           ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{
        
        PreparedStatement ps = null;
                
        String sql = "";

        try {
            sql = "INSERT INTO HIST_E_TFE (ID_PROCESO,TFE_MUN,TFE_EJE,TFE_NUM,TFE_COD,TFE_VALOR,TFE_FEC_VENCIMIENTO,PLAZO_ACTIVADO) " + 
                    "VALUES (?,?,?,?,?,?,?,?)";

            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);  
            int i =1;
            ps = con.prepareStatement(sql);
            JdbcOperations.setValues(ps,1,
					idProceso,
					valor.getCodOrganizacion(),
					valor.getEjercicio(),
					valor.getNumExpediente(),
					valor.getCodDatoSuplementario(),
					DateOperations.toTimestamp(valor.getValorDatoSuplementario()),
					DateOperations.toTimestamp(valor.getValorFechaVencimiento()),
					Integer.parseInt(valor.getValorPlazoActivo()));          
            
            log.debug(sql);
       
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor campo de fecha en el histórico: " + e.getMessage());
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
           ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{

        PreparedStatement ps = null;
                
        String sql = "";

        try {
            sql = "INSERT INTO HIST_E_TFET (ID_PROCESO,TFET_MUN,TFET_PRO,TFET_EJE,TFET_NUM,TFET_TRA,TFET_OCU,TFET_COD,TFET_VALOR,TFET_FEC_VENCIMIENTO,PLAZO_ACTIVADO) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);  
            int i =1;
            ps = con.prepareStatement(sql);
            JdbcOperations.setValues(ps,1,
					idProceso,
					valor.getCodOrganizacion(),
					valor.getProcedimiento(),
					valor.getEjercicio(),
					valor.getNumExpediente(),
					valor.getCodTramite(),
					valor.getOcurrenciaTramite(),
					valor.getCodDatoSuplementario(),
					DateOperations.toTimestamp(valor.getValorDatoSuplementario()),
					DateOperations.toTimestamp(valor.getValorFechaVencimiento()),
					Integer.parseInt(valor.getValorPlazoActivo()));             
            
            log.debug(sql);
       
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor campo de fecha de trámite en el histórico: " + e.getMessage());
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
           ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{
        
        PreparedStatement ps = null;
        
        try {
            
            String sql = "INSERT INTO HIST_E_TTL (ID_PROCESO,TTL_MUN,TTL_EJE,TTL_NUM,TTL_COD,TTL_VALOR) " + 
                    "VALUES (?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);  
            
            int i =1;
            ps = con.prepareStatement(sql);
            JdbcOperations.setValues(ps,1,
                idProceso,
                valor.getCodOrganizacion(),
                valor.getEjercicio(),
                valor.getNumExpediente(),
                valor.getCodDatoSuplementario(),
                valor.getValorDatoSuplementario());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor campo de texto largo en el histórico: " + e.getMessage());
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
           ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{
        
        PreparedStatement ps = null;
        
        try {
            String sql = "INSERT INTO HIST_E_TTLT (ID_PROCESO,TTLT_MUN,TTLT_PRO,TTLT_EJE,TTLT_NUM,TTLT_TRA,TTLT_OCU,TTLT_COD,TTLT_VALOR) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i =1;
            ps = con.prepareStatement(sql);
            JdbcOperations.setValues(ps,1,
					idProceso,
					valor.getCodOrganizacion(),
					valor.getProcedimiento(),
					valor.getEjercicio(),
					valor.getNumExpediente(),
					valor.getCodTramite(),
					valor.getOcurrenciaTramite(),
					valor.getCodDatoSuplementario(),
					valor.getValorDatoSuplementario());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor campo de texto largo de trámite en el histórico: " + e.getMessage());
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
           ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{
        
        byte [] contenido = ExpedienteDAO.getInstance().getContenidoFicheroCampoSuplementario(valor, con);
        
        PreparedStatement ps = null;
        
        try {
            String sql = "INSERT INTO HIST_E_TFI (ID_PROCESO,TFI_MUN,TFI_EJE,TFI_NUM,TFI_COD,TFI_VALOR,TFI_MIME,TFI_NOMFICH,TFI_TAMANHO) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i =1;
            ps = con.prepareStatement(sql);
            JdbcOperations.setValues(ps,1,
				idProceso,
				valor.getCodOrganizacion(),
				valor.getEjercicio(),
				valor.getNumExpediente(),
				valor.getCodDatoSuplementario(),
				contenido,
				valor.getTipoMimeFichero(),
				valor.getValorDatoSuplementario(),
				valor.getLongitudFichero());
            
            ps.executeUpdate();
        }catch(SQLException e){
             log.error("Error al grabar el valor campo fichero de expediente en el histórico: " + e.getMessage());
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
             ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{
        
        byte [] contenido = ExpedienteDAO.getInstance().getContenidoFicheroCampoSuplementarioTramite(valor, con);
        
        PreparedStatement ps = null;
        
        try {
            String sql = "INSERT INTO HIST_E_TFIT (ID_PROCESO,TFIT_MUN,TFIT_PRO,TFIT_EJE,TFIT_NUM,TFIT_TRA,TFIT_OCU," + 
                    "TFIT_COD,TFIT_VALOR,TFIT_MIME,TFIT_NOMFICH,TFIT_TAMANHO,TFIT_ORIGEN) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i =1;
            ps = con.prepareStatement(sql);
            JdbcOperations.setValues(ps,1,
					idProceso,
					valor.getCodOrganizacion(),
					valor.getProcedimiento(),
					valor.getEjercicio(),
					valor.getNumExpediente(),
					valor.getCodTramite(),
					valor.getOcurrenciaTramite(),
					valor.getCodDatoSuplementario(),
					contenido,
					valor.getTipoMimeFichero(),
					valor.getValorDatoSuplementario(),
					valor.getLongitudFichero(),
					valor.getOrigenFichero());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor campo fichero de trámite en el histórico: " + e.getMessage());
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
           ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{
        
        PreparedStatement ps = null;
        
        try {
            
            String sql = "INSERT INTO HIST_E_TDE (ID_PROCESO,TDE_MUN,TDE_EJE,TDE_NUM,TDE_COD,TDE_VALOR) " + 
                    "VALUES (?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i =1;
            ps = con.prepareStatement(sql);
            JdbcOperations.setValues(ps,1,
					idProceso,
					valor.getCodOrganizacion(),
					valor.getEjercicio(),
					valor.getNumExpediente(),
					valor.getCodDatoSuplementario(),
					valor.getValorDatoSuplementario());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor campo desplegable en el histórico: " + e.getMessage());
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
        ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{
        
        PreparedStatement ps = null;
        
        try {
            String sql = "INSERT INTO HIST_E_TDET (ID_PROCESO,TDET_MUN,TDET_PRO,TDET_EJE,TDET_NUM,TDET_TRA,TDET_OCU,TDET_COD,TDET_VALOR) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i =1;
            ps = con.prepareStatement(sql);
            JdbcOperations.setValues(ps,1,
					idProceso,
					valor.getCodOrganizacion(),
					valor.getProcedimiento(),
					valor.getEjercicio(),
					valor.getNumExpediente(),
					valor.getCodTramite(),
					valor.getOcurrenciaTramite(),
					valor.getCodDatoSuplementario(),
					valor.getValorDatoSuplementario());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor campo desplegable de trámite en el histórico: " + e.getMessage());
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
           ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{
        
        Statement stmt = null;
                   
        try {
            String sql = "INSERT INTO HIST_E_TNUC (ID_PROCESO,TNUC_MUN,TNUC_EJE,TNUC_NUM,TNUC_COD,TNUC_VALOR) " + 
                    "VALUES ("+idProceso+","+ valor.getCodOrganizacion() + ","+ valor.getEjercicio() +",'" + valor.getNumExpediente() + 
                    "','"+valor.getCodDatoSuplementario() +"',"+ oad.convertir("'" + valor.getValorDatoSuplementario() + "'", 
                            oad.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i =1;
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException e){
            log.error("Error al grabar el valor campo numerico calculado de expediente en el histórico: " + e.getMessage());
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
           ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{

        Statement stmt = null;
                   
        try {
            String sql = "INSERT INTO HIST_E_TNUCT (ID_PROCESO,TNUCT_MUN,TNUCT_PRO,TNUCT_EJE,TNUCT_NUM,TNUCT_TRA,TNUCT_OCU,TNUCT_COD,TNUCT_VALOR) " + 
                    "VALUES ("+idProceso+","+ valor.getCodOrganizacion() + ",'" + valor.getProcedimiento() + "',"+ valor.getEjercicio() + ",'" + 
                    valor.getNumExpediente() + "'," + valor.getCodTramite() + "," + valor.getOcurrenciaTramite() + ",'" + 
                    valor.getCodDatoSuplementario()+"',"+ oad.convertir("'" + valor.getValorDatoSuplementario() + "'", 
                    oad.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i =1;
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException e){
            log.error("Error al grabar el valor campo numerico calculado de trámite en el histórico: " + e.getMessage());
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
           ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{
        
        PreparedStatement ps = null;
                
        String sql = "";

        try {
            sql = "INSERT INTO HIST_E_TFEC (ID_PROCESO,TFEC_MUN,TFEC_EJE,TFEC_NUM,TFEC_COD,TFEC_VALOR,TFEC_FEC_VENCIMIENTO,PLAZO_ACTIVADO) " + 
                    "VALUES (?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setLong(i++, idProceso);
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
            
            log.debug(sql);
       
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor campo fecha calculado en el histórico: " + e.getMessage());
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
           ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{
        
        PreparedStatement ps = null;
                
        String sql = "";

        try {
            sql = "INSERT INTO HIST_E_TFECT (ID_PROCESO,TFECT_MUN,TFECT_PRO,TFECT_EJE,TFECT_NUM,TFECT_TRA," + 
                    "TFECT_OCU,TFECT_COD,TFECT_VALOR,TFECT_FEC_VENCIMIENTO,PLAZO_ACTIVADO) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setLong(i++, idProceso);
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
            
            log.debug(sql);
       
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor campo fecha calculado de trámite en el histórico: " + e.getMessage());
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
           ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{

        PreparedStatement ps = null;
        
        try {
            
            String sql = "INSERT INTO HIST_E_TDEX (ID_PROCESO,TDEX_MUN,TDEX_EJE,TDEX_NUM,TDEX_COD,TDEX_VALOR, TDEX_CODSEL) " + 
                    "VALUES (?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setLong(i++, idProceso);
            ps.setInt(i++,valor.getCodOrganizacion());
            ps.setInt(i++,valor.getEjercicio());
            ps.setString(i++,valor.getNumExpediente());
            ps.setString(i++,valor.getCodDatoSuplementario());
            ps.setString(i++,valor.getValorDatoSuplementario());
            ps.setString(i++,valor.getCodValorDatoSuplementario());
            
            ps.executeUpdate();
        }catch(SQLException e){
            log.error("Error al grabar el valor campo desplegable externo en el histórico: " + e.getMessage());
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
           ValorCampoSuplementarioVO valor, Long idProceso) throws Exception{

        PreparedStatement ps = null;
        
        try {
            String sql = "INSERT INTO HIST_E_TDEXT (ID_PROCESO,TDEXT_MUN,TDEXT_PRO,TDEXT_EJE,TDEXT_NUM,TDEXT_TRA," + 
                    "TDEXT_OCU,TDEXT_COD,TDEXT_VALOR, TDEXT_CODSEL) " + 
                    "VALUES (?,?,?,?,?,?,?,?,?,?)";
            
            log.debug(" (TRAZA DE CONTROL) SQL: " + sql);
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setLong(i++, idProceso);
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
            log.error("Error al grabar el valor campo desplegable externo de trámite en el histórico: " + e.getMessage());
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
}
