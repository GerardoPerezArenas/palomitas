package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.integracionsw.AvanzarRetrocederSWVO;
import es.altia.agora.business.sge.DefinicionAgrupacionCamposValueObject;
import es.altia.agora.business.sge.DefinicionCampoValueObject;
import es.altia.agora.business.sge.DefinicionTramitesImportacionValueObject;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.FlujoSalidaTramiteVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.exception.ImportacionFlujoBibliotecaException;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.StringUtils;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.jdbc.GeneralOperations;
import es.altia.util.jdbc.sqlbuilder.QueryResult;
import es.altia.util.jdbc.sqlbuilder.SqlExecuter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;
import oracle.sql.BLOB;
import org.apache.log4j.Logger;

/**
 *
 * @author MilaNP
 */
public class DefinicionTramitesImportacionDAO {

    private static DefinicionTramitesImportacionDAO instance = null;
    private Logger log = Logger.getLogger(DefinicionTramitesImportacionDAO.class);
    
    protected static Config m_ConfigTechnical;
    protected static String idiomaDefecto;

    public DefinicionTramitesImportacionDAO() {
        // Queremos usar el fichero de configuracion techserver para cargar el idioma por defecto
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        idiomaDefecto = m_ConfigTechnical.getString("idiomaDefecto");
    }

    public static DefinicionTramitesImportacionDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        synchronized (DefinicionTramitesImportacionDAO.class) {
            if (instance == null) {
                instance = new DefinicionTramitesImportacionDAO();
            }

        }
        return instance;
    }

    public ArrayList<Integer> getCodsTramitesImportacion (Connection con, String codProcedimiento, int codMunicipio) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql;
        int contbd = 1;
        ArrayList<Integer> listaCodigos = new ArrayList<Integer>();
            
        try {
            //codigos de tramites
            sql = "SELECT TRA_COD FROM E_TRA "+
                    "WHERE TRA_PRO=? AND TRA_MUN=? AND TRA_FBA IS NULL ";
            
            ps = con.prepareStatement(sql);
            ps.setString(contbd++, codProcedimiento);
            ps.setInt(contbd++, codMunicipio);
            rs = ps.executeQuery();
            while(rs.next()){
                int codigo = rs.getInt("TRA_COD");
                listaCodigos.add(codigo);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("Error al recuperos los códigos de los trámites del procedimiento.");
            throw new SQLException();
        } finally {
            try {
                if(rs != null) rs.close();
                if(ps != null) ps.close();
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de la BBDD.");
            }
        }
       
        return listaCodigos;
    }
    
    public ArrayList<DefinicionTramitesImportacionValueObject> getTramitesImportacion(String codProcedimiento, int codMunicipio, Connection con) throws ImportacionFlujoBibliotecaException {
        //Queremos estar informados de cuando	este metodo es ejecutado
        log.debug("entra en getTramitesImportacion en DefinicionTramitesDAO");

        ArrayList<DefinicionTramitesImportacionValueObject> flujoAImportar = new ArrayList<DefinicionTramitesImportacionValueObject>();
        ArrayList<AvanzarRetrocederSWVO> defSWTram = null;
        ArrayList<Integer> listaCodsTram = null;
            
            
        try {
            listaCodsTram = new ArrayList<Integer>(this.getCodsTramitesImportacion(con, codProcedimiento, codMunicipio));
            if(listaCodsTram.size()>0){
                for(Integer codTramite : listaCodsTram){
                    DefinicionTramitesImportacionValueObject defTramImpVO = new DefinicionTramitesImportacionValueObject();
                    DefinicionTramitesValueObject defTramVO = new DefinicionTramitesValueObject();
                    defTramVO.setCodMunicipio(String.valueOf(codMunicipio));
                    
                    // PESTAÑA DATOS
                    this.getDatosTramite(con, codProcedimiento, codMunicipio, codTramite, defTramVO);

                    // PESTAÑA DOCUMENTOS
                    this.getDatosDocsTram(con, codProcedimiento, codMunicipio, codTramite, defTramVO);

                    // PESTAÑA ENLACES
                    this.getEnlacesTramite(con, codProcedimiento, codMunicipio, codTramite, defTramVO);

                    // PESTAÑA CONDICIONES DE ENTRADA
                    this.getCondEntTramite(con, codProcedimiento, codMunicipio, codTramite, defTramVO);

                    //PESTAÑA CONDICIONES DE SALIDA
                    this.getCondSalTramite(con, codProcedimiento, codMunicipio, codTramite, defTramVO);

                    // PESTAÑA CAMPOS SUPLEMENTARIOS
                    this.getCamposSuplTramite(con, codProcedimiento, codMunicipio, codTramite, defTramVO);

                    //PESTAÑA INTEGRACIONES -> PARAMETROS SW (DEF_TRA_SW)
                    defSWTram = this.getListaConfSW(codMunicipio, codProcedimiento, codTramite, con);
                    defTramVO.setListaConfSW(new Vector(defSWTram));
                    
                    defTramImpVO.setCodMunicipio(codMunicipio);
                    defTramImpVO.setCodProcedimientoBiblioteca(codProcedimiento);
                    defTramImpVO.setCodTramiteBiblioteca(codTramite);
                    defTramImpVO.setDefTramVO(defTramVO);
                    
                    flujoAImportar.add(defTramImpVO);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            if (log.isDebugEnabled()) {
                log.error(e.getMessage());
            }
            throw new ImportacionFlujoBibliotecaException(80,e.getMessage());
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        log.debug("FIN getTramitesImportacion");

        return flujoAImportar;
    }

    public boolean grabarTramitesImportacion(ArrayList<DefinicionTramitesImportacionValueObject> flujoAImportar, String codProcedimiento, String codBiblioteca, Connection con) throws ImportacionFlujoBibliotecaException {
        if (log.isDebugEnabled()) {
            log.debug("DefinicionTramitesDAO -> grabarTramitesImportacion:  BEGIN");
        }

        ArrayList<Boolean> listaExitos = new ArrayList<Boolean>();
        boolean exitoTotal = true;
        for (DefinicionTramitesImportacionValueObject defTramImpVO : flujoAImportar) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            String sql = "";
            int contbd = 1;
            boolean exitoTram = false, exitoDoc = false, exitoEnl = false;
            boolean entra = false, exitoCSup = false, exitoSW = false;
            ArrayList<DefinicionTramitesValueObject> listaDoc = null;
            ArrayList<Integer> codsEnlaces = null;
            ArrayList<DefinicionAgrupacionCamposValueObject> listaAgrupaciones = null;
            ArrayList<DefinicionCampoValueObject> listaCampos = null;
            ArrayList<AvanzarRetrocederSWVO> listaConfSW = null;
            int codMunicipio = defTramImpVO.getCodMunicipio();
            DefinicionTramitesValueObject defTramVO = defTramImpVO.getDefTramVO();
            defTramImpVO.setCodProcedimientoDest(codProcedimiento);

            try {
                // buscar codigo nuevo tramite
                sql = "SELECT TRA_COD "
                        + "FROM E_TRA "
                        + "WHERE TRA_MUN=? AND TRA_PRO=? "
                        + "ORDER BY TRA_COD DESC";

                if (log.isDebugEnabled()) {
                    log.debug("Query de búsqueta de último código de trámite" + sql);
                }

                ps = con.prepareStatement(sql);
                ps.setInt(contbd++, codMunicipio);
                ps.setString(contbd++, codProcedimiento);
                rs = ps.executeQuery();
                int codTramite = 0;
                while (rs.next()) {
                    codTramite = rs.getInt("TRA_COD");
                    break;
                }
                rs.close();
                ps.close();
                int nuevoCodTramite = codTramite + 1;
                defTramImpVO.setCodTramiteDest(nuevoCodTramite);

                //PESTAÑA DATOS
                exitoTram = this.grabarDatosTramite(defTramVO, con, codProcedimiento, nuevoCodTramite, codMunicipio);

                //pestaña documentos
                listaDoc = new ArrayList<DefinicionTramitesValueObject>(defTramVO.getListaDocumentos());
                if (listaDoc.size() > 0) {
                    entra = true;
                    exitoDoc = this.grabarDocsTramite(con, codProcedimiento, nuevoCodTramite, codMunicipio, listaDoc);
                } else entra = false;
                exitoDoc = !entra || (entra && exitoDoc);

                //pestaña enlaces
                codsEnlaces = new ArrayList<Integer>(defTramVO.getListaCodigoEnlaces());
                if (codsEnlaces.size() > 0) {
                    entra = true;
                    exitoEnl = this.grabarEnlacesTramite(defTramVO, con, codProcedimiento, nuevoCodTramite, codMunicipio, codsEnlaces);
                } else entra = false;
                exitoEnl = !entra || (entra && exitoEnl);

                //las pestañas de condiciones de entrada y salida se graban al final, cuando ya hay mapeo

                //pestaña datos suplementarios
                listaAgrupaciones = new ArrayList<DefinicionAgrupacionCamposValueObject>(defTramVO.getListaAgrupaciones());
                listaCampos = new ArrayList<DefinicionCampoValueObject>(defTramVO.getListaCampos());
                if (listaAgrupaciones.size() > 0 || listaCampos.size() > 0) {
                    entra = true;
                    exitoCSup = this.grabarCamposSupl(con, codProcedimiento, nuevoCodTramite, codMunicipio, listaAgrupaciones, listaCampos);
                } else entra = false;
                exitoCSup = !entra || (entra && exitoCSup);

                //PESTAÑA INTEGRACIONES -> PARAMETROS SW (DEF_TRA_SW)
                listaConfSW = new ArrayList<AvanzarRetrocederSWVO>(defTramVO.getListaConfSW());
                if(listaConfSW.size()>0) {
                    entra = true;
                    exitoSW = this.grabarConfSW(con, codProcedimiento, nuevoCodTramite, codMunicipio, listaConfSW);
                } else entra = false;
                exitoSW = !entra || (entra && exitoSW);

            } catch (SQLException ex) {
                ex.printStackTrace();
                if (log.isDebugEnabled()) {
                    log.debug(ex.getMessage());
                }
            }
            boolean mapeo = exitoTram && exitoDoc && exitoEnl &&  exitoCSup && exitoSW;
            boolean insMapeo = true;
            if(mapeo){
                int codTramOrigen = defTramImpVO.getCodTramiteBiblioteca();
                int codTramDest = defTramImpVO.getCodTramiteDest();
                insMapeo = TramiteBibliotecaImportadoDAO.getInstance().grabarImportacionTramiteBiblioteca(con, codTramOrigen, codBiblioteca, codTramDest, codProcedimiento);
            }
            listaExitos.add(mapeo && insMapeo);
        }
        boolean exitoCond = this.grabarCondEntYCondSalTramiteImportado(con, flujoAImportar, codBiblioteca, codProcedimiento);
                
        for(Boolean bool : listaExitos){
            if(bool.equals(false)){
                exitoTotal = false;
                break;
            }
        }

        return exitoTotal;
    }
    
    public boolean grabarCondEntYCondSalTramiteImportado(Connection con, ArrayList<DefinicionTramitesImportacionValueObject> flujoAImportar, String codProcOrigen, String codProcDest) throws ImportacionFlujoBibliotecaException{
        ArrayList<Boolean> listaExitos = new ArrayList<Boolean>();
        boolean exitoTotal = true;
        for(DefinicionTramitesImportacionValueObject defTramImpVO : flujoAImportar){
            PreparedStatement ps = null;
            String sql = "";
            int contbd = 1;
            boolean  entra = false, exitoCE = false,exitoCSal = false;
            ArrayList<DefinicionTramitesValueObject> listaCondEntrada = null;
            int codMunicipio = defTramImpVO.getCodMunicipio();
            DefinicionTramitesValueObject defTramVO = defTramImpVO.getDefTramVO();
            int nuevoCodTramite = defTramImpVO.getCodTramiteDest();
            // condiciones entrada
            listaCondEntrada = new ArrayList<DefinicionTramitesValueObject>(defTramVO.getListasCondEntrada());
            if (listaCondEntrada.size() > 0) {
                entra = true;
                exitoCE = this.grabarCondsEnt(con, codProcDest, codProcOrigen, nuevoCodTramite, codMunicipio, listaCondEntrada);
            } else entra = false;
            exitoCE = !entra || (entra && exitoCE);

            //PESTAÑA CONDICIONES DE SALIDA
            exitoCSal = this.grabarCondsSal(defTramVO, con, codProcDest, codProcOrigen, nuevoCodTramite, codMunicipio);
            listaExitos.add(exitoCE && exitoCSal);
        }
        for(Boolean bool : listaExitos){
            if(bool.equals(false)){
                exitoTotal = false;
                break;
            }
        }

        return exitoTotal;
    }

    public void getDatosTramite(Connection con, String codProcedimiento, int codMunicipio, int codTramite, DefinicionTramitesValueObject defTramVO) throws ImportacionFlujoBibliotecaException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql;
        int contbd = 1;
        ArrayList<Integer> listaUnidadesTramitadoras = new ArrayList<Integer>();

        try {
            //1. tramite E_TRA,E_TML
            sql = "SELECT TRA_PLZ,TRA_UND,TRA_OCU,TRA_MUN,TRA_VIS,TRA_UIN,TRA_UTR,TRA_CLS,TRA_ARE,TRA_FBA,TRA_COU,"
                    + "TRA_PRE,TRA_INS,TRA_WS_COD,TRA_WS_OB,TRA_UTI,TRA_UTF,TRA_USI,TRA_USF,TRA_INI,TRA_INF,TRA_WST_COD,"
                    + "TRA_WST_OB,TRA_WSR_COD,TRA_WSR_OB,TRA_PRR,TRA_CAR,TRA_FIN,TRA_GENERARPLZ,TRA_NOTCERCAFP,TRA_NOTFUERADP,"
                    + "TRA_TIPNOTCFP,TRA_TIPNOTFDP,TRA_NOTIFICACION_ELECTRONICA,TRA_COD_TIPO_NOTIFICACION,TRA_TIPO_USUARIO_FIRMA,"
                    + "TRA_OTRO_COD_USUARIO_FIRMA,TRA_TRAM_EXT_PLUGIN,TRA_TRAM_EXT_URL,TRA_TRAM_EXT_IMPLCLASS,"
                    + "TRA_TRAM_ID_ENLACE_PLUGIN,TRA_NOTIF_ELECT_OBLIG,TRA_NOTIF_FIRMA_CERT_ORG,COD_DEPTO_NOTIFICACION,TML_VALOR, "
                    + "TRA_NOTIF_UITI, TRA_NOTIF_UITF, TRA_NOTIF_UIEI, TRA_NOTIF_UIEF,TRA_NOTIFICADO , TRA_NOTIF_USUTRA_FINPLAZO, TRA_NOTIF_USUEXP_FINPLAZO, TRA_NOTIF_UOR_FINPLAZO "
                    + "FROM E_TRA,E_TML WHERE TRA_PRO=? AND TRA_COD=? AND TRA_MUN=? AND TRA_FBA IS NULL "
                    + "AND e_tra.TRA_MUN=e_tml.TML_MUN AND e_tra.TRA_PRO=e_tml.TML_PRO "
                    + " AND e_tra.TRA_COD=e_tml.TML_TRA AND e_tml.TML_CMP='NOM' AND e_tml.TML_LENG=?";

            if (log.isDebugEnabled()) {
                log.debug("DefinicionTramitesImportacionDAO, getTramitesImportacion->getDatosTramite: Sentencia SQL:" + sql);
            }
            ps = con.prepareStatement(sql);
            ps.setString(contbd++, codProcedimiento);
            ps.setInt(contbd++, codTramite);
            ps.setInt(contbd++, codMunicipio);
            ps.setInt(contbd++, Integer.parseInt(idiomaDefecto));
            rs = ps.executeQuery();
            while (rs.next()) {
                defTramVO.setNumeroTramite(rs.getString("TRA_COU"));
                defTramVO.setNombreTramite(rs.getString("TML_VALOR"));
                defTramVO.setDisponible(rs.getString("TRA_VIS"));
                defTramVO.setCodUnidadInicio(rs.getString("TRA_UIN"));
                defTramVO.setCodUnidadTramite(rs.getString("TRA_UTR"));
                defTramVO.setPlazo(rs.getString("TRA_PLZ"));
                defTramVO.setPlazoFin(rs.getInt("TRA_FIN"));
                defTramVO.setUnidadesPlazo(rs.getString("TRA_UND"));
                defTramVO.setOcurrencias(rs.getString("TRA_OCU"));
                defTramVO.setCodClasifTramite(rs.getString("TRA_CLS"));
                defTramVO.setTramitePregunta(rs.getString("TRA_PRE"));
                defTramVO.setCodAreaTra(String.valueOf(rs.getInt("TRA_ARE")));
                
                String instrucc = rs.getString("TRA_INS");
                if (instrucc != null) {
                    defTramVO.setInstrucciones(AdaptadorSQLBD.js_escape(instrucc));
                } else {
                    defTramVO.setInstrucciones(null);
                }

                if (!(rs.getString("TRA_UTI")).equals("N")) {
                    defTramVO.setNotUnidadTramitIni("1");
                } else {
                    defTramVO.setNotUnidadTramitIni("0");
                }

                if (!(rs.getString("TRA_UTF")).equals("N")) {
                    defTramVO.setNotUnidadTramitFin("1");
                } else {
                    defTramVO.setNotUnidadTramitFin("0");
                }

                if (!(rs.getString("TRA_USI")).equals("N")) {
                    defTramVO.setNotUsuUnidadTramitIni("1");
                } else {
                    defTramVO.setNotUsuUnidadTramitIni("0");
                }

                if (!(rs.getString("TRA_USF")).equals("N")) {
                    defTramVO.setNotUsuUnidadTramitFin("1");
                } else {
                    defTramVO.setNotUsuUnidadTramitFin("0");
                }

                if (!(rs.getString("TRA_INI")).equals("N")) {
                    defTramVO.setNotInteresadosIni("1");
                } else {
                    defTramVO.setNotInteresadosIni("0");
                }

                if (!(rs.getString("TRA_INF")).equals("N")) {
                    defTramVO.setNotInteresadosFin("1");
                } else {
                    defTramVO.setNotInteresadosFin("0");
                }

                if(!(rs.getString("TRA_NOTIF_UITI")).equals("N")) {
                    defTramVO.setNotUsuInicioTramiteIni("1");
                } else {
                   defTramVO.setNotUsuInicioTramiteIni("0");
                }

                if(!(rs.getString("TRA_NOTIF_UITF")).equals("N")) {
                    defTramVO.setNotUsuInicioTramiteFin("1");
                } else {
                    defTramVO.setNotUsuInicioTramiteFin("0");
                }

                if(!(rs.getString("TRA_NOTIF_UIEI")).equals("N")){
                    defTramVO.setNotUsuInicioExpedIni("1");
                } else {
                    defTramVO.setNotUsuInicioExpedIni("0");
                }

                if(!(rs.getString("TRA_NOTIF_UIEF")).equals("N")){
                    defTramVO.setNotUsuInicioExpedFin("1");
                } else {
                    defTramVO.setNotUsuInicioExpedFin("0");
                }
                
                if(!(rs.getString("TRA_NOTIF_USUTRA_FINPLAZO")).equals("N")){	
                    defTramVO.setNotUsuTraFinPlazo("1");	
                } else {
	
                    defTramVO.setNotUsuTraFinPlazo("0");	
                }	
                if(!(rs.getString("TRA_NOTIF_USUEXP_FINPLAZO")).equals("N")){	
                    defTramVO.setNotUsuExpFinPlazo("1");	
                } else {	
                    defTramVO.setNotUsuExpFinPlazo("0");	
                }	
                if(!(rs.getString("TRA_NOTIF_UOR_FINPLAZO")).equals("N")){	
                    defTramVO.setNotUORFinPlazo("1");	
                } else {	
                    defTramVO.setNotUORFinPlazo("0");	
                }
                
                defTramVO.setCodExpRel(rs.getString("TRA_PRR"));
                defTramVO.setCodCargo(rs.getString("TRA_CAR"));
                defTramVO.setGenerarPlazos(rs.getBoolean("TRA_GENERARPLZ"));
                defTramVO.setNotificarCercaFinPlazo(rs.getBoolean("TRA_NOTCERCAFP"));
                defTramVO.setNotificarFueraDePlazo(rs.getBoolean("TRA_NOTFUERADP"));
                defTramVO.setTipoNotCercaFinPlazo(rs.getInt("TRA_TIPNOTCFP"));
                defTramVO.setTipoNotFueraDePlazo(rs.getInt("TRA_TIPNOTFDP"));

                defTramVO.setAdmiteNotificacionElectronica(rs.getString("TRA_NOTIFICACION_ELECTRONICA"));
                defTramVO.setCodigoTipoNotificacionElectronica(rs.getString("TRA_COD_TIPO_NOTIFICACION"));
                defTramVO.setTipoUsuarioFirma(rs.getString("TRA_TIPO_USUARIO_FIRMA"));
                defTramVO.setCodigoOtroUsuarioFirma(rs.getString("TRA_OTRO_COD_USUARIO_FIRMA"));
                defTramVO.setCodPluginPantallaTramitacionExterna(rs.getString("TRA_TRAM_EXT_PLUGIN"));
                defTramVO.setImplClassPluginPantallaTramitacionExterna(rs.getString("TRA_TRAM_EXT_IMPLCLASS"));
                defTramVO.setUrlPluginPantallaTramitacionExterna(rs.getString("TRA_TRAM_EXT_URL"));
                defTramVO.setCodUrlPluginPantallaTramitacionExterna("TRA_TRAM_ID_ENLACE_PLUGIN");

                defTramVO.setCertificadoOrganismoFirmaNotificacion(rs.getInt("TRA_NOTIF_FIRMA_CERT_ORG") == 1);
                defTramVO.setNotificacionElectronicaObligatoria(rs.getInt("TRA_NOTIF_ELECT_OBLIG") == 1);
                defTramVO.setCodDepartamentoNotificacion(rs.getString("COD_DEPTO_NOTIFICACION"));
                defTramVO.setTramiteNotificado(rs.getInt("TRA_NOTIFICADO")==1);

                defTramVO.setTramWSCod(rs.getString("TRA_WS_COD"));
                defTramVO.setTramWSOb(rs.getString("TRA_WS_OB"));
                defTramVO.setTramWSTCod(rs.getString("TRA_WST_COD"));
                defTramVO.setTramWSTOb(rs.getString("TRA_WST_OB"));
                defTramVO.setTramWSRCod(rs.getString("TRA_WSR_COD"));
                defTramVO.setTramWSROb(rs.getString("TRA_WSR_OB"));
            }
            rs.close();
            ps.close();

            //2.unidades tramitadoras E_TRA_UTR
            sql = "SELECT TRA_UTR_COD FROM E_TRA_UTR WHERE TRA_MUN=? AND TRA_PRO=? AND TRA_COD=?";
            if (log.isDebugEnabled()) {
                log.debug("DefinicionTramitesDAO, getTramitesImportacion->getDatosTramite: Sentencia SQL:" + sql);
            }
            ps = con.prepareStatement(sql);
            contbd = 1;
            ps.setInt(contbd++, codMunicipio);
            ps.setString(contbd++, codProcedimiento);
            ps.setInt(contbd++, codTramite);
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer codigo = rs.getInt("TRA_UTR_COD");
                listaUnidadesTramitadoras.add(codigo);
            }

            defTramVO.setListaUnidadesTramitadoras(new Vector(listaUnidadesTramitadoras));
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error al recuperar datos de tramite");
            throw new ImportacionFlujoBibliotecaException(81,ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de BBDD");
            }
        }
    }

    public void getDatosDocsTram(Connection con, String codProcedimiento, int codMunicipio, int codTramite, DefinicionTramitesValueObject defTramVO) throws ImportacionFlujoBibliotecaException {
        ArrayList<DefinicionTramitesValueObject> listaDocumentos = new ArrayList<DefinicionTramitesValueObject>();

        ResultSet rs = null;
        Statement st = null;
        
        try {
                   
            SqlExecuter sqlExecuter = new SqlExecuter();
            // Creacion de la sentencia SQL
            sqlExecuter.select("DOT_MUN", "DOT_COD", "DOT_TDO", "DOT_VIS", "DOT_FRM", "DOT_PLT", 
                    "DOT_ACTIVO", "PLT_DES", "PLT_APL", "PLT_DOC", "PLT_INT", "PLT_REL", "PLT_EDITOR_TEXTO")
                    .from("E_DOT")
                    .innerEquiJoin("A_PLT", "E_DOT.DOT_PLT", "A_PLT.PLT_COD")
					.andEquals("E_DOT.DOT_PRO", "A_PLT.PLT_PRO")
					.andEquals("E_DOT.DOT_TRA", "A_PLT.PLT_TRA")
                    .whereEqualsParametrizado("DOT_MUN")
                    .andEqualsParametrizado("DOT_PRO")
                    .andEqualsParametrizado("DOT_TRA");
            // Introduccion de valores y log de la sentencia SQL
            sqlExecuter.setValues(codMunicipio, codProcedimiento, codTramite).logSqlDebug(log);
            // Obtencion del resultado
            
            st = con.createStatement();
            log.debug("consulta :" +  sqlExecuter.toString());
            rs = st.executeQuery(sqlExecuter.toString());
            
            
            //QueryResult resultado = sqlExecuter.executeQuery(con);
            // Se iteran las filas del resultado
            while (rs.next()) {
                DefinicionTramitesValueObject dTVO = new DefinicionTramitesValueObject();
                java.io.InputStream streamEntrada = rs.getBinaryStream("PLT_DOC");
                
                if (streamEntrada != null) {
                    log.debug("Hay plantilla de doc de tramitación");
                    java.io.ByteArrayOutputStream streamSalida = new java.io.ByteArrayOutputStream();
                    int c;
                    while ((c = streamEntrada.read()) != -1) {
                        streamSalida.write(c);
                    }
                    streamSalida.flush();
                    streamSalida.close();
                    streamEntrada.close();
                    if (streamSalida != null) {
                        dTVO.setByteArrayPlantillaTramitacion(streamSalida.toByteArray());
                    }
                }

                dTVO.setCodigoDoc(rs.getString("DOT_COD"));
                dTVO.setCodTipoDoc(rs.getString("DOT_TDO"));
                dTVO.setVisibleInternet(rs.getString("DOT_VIS"));
                dTVO.setFirma(rs.getString("DOT_FRM"));
                dTVO.setDocActivo(rs.getString("DOT_ACTIVO"));
                dTVO.setCodMunicipio(rs.getString("DOT_MUN"));
                dTVO.setCodPlantilla(rs.getString("DOT_PLT"));
                dTVO.setInteresado(rs.getString("PLT_INT"));
                dTVO.setRelacion(rs.getString("PLT_REL"));
                String nombreDocumento = rs.getString("PLT_DES");
                dTVO.setNombreDoc(nombreDocumento);
                dTVO.setPlantilla(nombreDocumento);
                dTVO.setCodAplicacion(rs.getString("PLT_APL"));
                dTVO.setEditorTexto(rs.getString("PLT_EDITOR_TEXTO"));
                /**
                InputStream stream = rs.getBinaryStream("PLT_DOC");
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int c;
                while ((c = stream.read())!= -1){
                    buffer.write(c);
                }
                buffer.flush();
                buffer.close();
                stream.close();
                */
                listaDocumentos.add(dTVO);
            }
            defTramVO.setListaDocumentos(new Vector(listaDocumentos));
        } catch (Exception ex) {
            log.error("Error al recuperar datos de documentos de tramite: " + ex.getMessage());
            log.error("Error al recuperar datos de documentos de tramite: " + ex.getCause());
            throw new ImportacionFlujoBibliotecaException(82, ex.getMessage());
        } finally {
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();                
            }catch(Exception e){
                e.printStackTrace();
            }
        } 
    }

    public void getEnlacesTramite(Connection con, String codProcedimiento, int codMunicipio, int codTramite, DefinicionTramitesValueObject defTramVO) throws ImportacionFlujoBibliotecaException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql;
        int contbd = 1;
        ArrayList<Integer> listaCodEnlaces = new ArrayList<Integer>();
        ArrayList<String> listaDescEnlaces = new ArrayList<String>();
        ArrayList<String> listaUrlEnlaces = new ArrayList<String>();
        ArrayList<Integer> listaEstEnlaces = new ArrayList<Integer>();

        try {
            sql = "SELECT TEN_MUN,TEN_COD,TEN_DES,TEN_URL,TEN_EST "
                    + "FROM E_TEN "
                    + "WHERE TEN_MUN=? AND TEN_PRO=? AND TEN_TRA=?";

            if (log.isDebugEnabled()) {
                log.debug("DefinicionTramitesDAO, getTramitesImportacion->getEnlacesTramite: Sentencia SQL:" + sql);
            }
            ps = con.prepareStatement(sql);
            ps.setInt(contbd++, codMunicipio);
            ps.setString(contbd++, codProcedimiento);
            ps.setInt(contbd++, codTramite);
            rs = ps.executeQuery();

            while (rs.next()) {
                listaCodEnlaces.add(rs.getInt("TEN_COD"));
                listaDescEnlaces.add(rs.getString("TEN_DES"));
                listaUrlEnlaces.add(rs.getString("TEN_URL"));
                listaEstEnlaces.add(rs.getInt("TEN_EST"));
            }
            defTramVO.setListaCodigoEnlaces(new Vector(listaCodEnlaces));
            defTramVO.setListaDescripcionEnlaces(new Vector(listaDescEnlaces));
            defTramVO.setListaEnlaces(new Vector(listaUrlEnlaces));
            defTramVO.setListaEstadoEnlaces(new Vector(listaEstEnlaces));
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error al recuperar datos de enlaces de tramite");
            throw new ImportacionFlujoBibliotecaException(83,ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de BBDD");
            }
        }
    }

    public void getCondEntTramite(Connection con, String codProcedimiento, int codMunicipio, int codTramite, DefinicionTramitesValueObject defTramVO) throws ImportacionFlujoBibliotecaException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql;
        int contbd = 1;
        ArrayList<DefinicionTramitesValueObject> listaCondsEntrada = new ArrayList<DefinicionTramitesValueObject>();

        try {
            sql = "SELECT ENT_COD,ENT_TIPO,ENT_EXP,ENT_CTR,ENT_EST, ENT_DOC "
                    + "FROM E_ENT "
                    + "WHERE ENT_MUN=? AND ENT_PRO=? AND ENT_TRA=?";

            if (log.isDebugEnabled()) {
                log.debug("DefinicionTramitesDAO, getTramitesImportacion->getCondEntTramite: Sentencia SQL:" + sql);
            }
            ps = con.prepareStatement(sql);
            ps.setInt(contbd++, codMunicipio);
            ps.setString(contbd++, codProcedimiento);
            ps.setInt(contbd++, codTramite);

            rs = ps.executeQuery();
            while (rs.next()) {
                DefinicionTramitesValueObject dTVO = new DefinicionTramitesValueObject();

                dTVO.setCodCondEntrada(rs.getString("ENT_COD"));
                String tipo = rs.getString("ENT_TIPO");
                dTVO.setTipoCondEntrada(tipo);
                if ("E".equals(tipo)) {
                    dTVO.setExpresionCondEntrada(rs.getString("ENT_EXP"));
                } else if ("T".equals(tipo)) {
                    dTVO.setEstadoTramiteCondEntrada(rs.getString("ENT_EST"));
                    dTVO.setIdTramiteCondEntrada(rs.getString("ENT_CTR"));
                } else if ("D".equals(tipo)) {
                    dTVO.setEstadoTramiteCondEntrada(rs.getString("ENT_EST"));
                    dTVO.setCodDocumentoCondEntrada(rs.getString("ENT_DOC"));
                }
                listaCondsEntrada.add(dTVO);
            }
            defTramVO.setListasCondEntrada(new Vector(listaCondsEntrada));
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error al recuperar datos de condiciones de entrada de tramite");
            throw new ImportacionFlujoBibliotecaException(84,ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de BBDD");
            }
        }
    }

    public void getCondSalTramite(Connection con, String codProcedimiento, int codMunicipio, int codTramite, DefinicionTramitesValueObject defTramVO) throws ImportacionFlujoBibliotecaException  {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql;
        int contbd = 1;
        ArrayList<FlujoSalidaTramiteVO> listaFlujoSalida = new ArrayList<FlujoSalidaTramiteVO>();

        try {
            //1. condicion salida E_SAL,E_SML
            sql = "SELECT SAL_TAC,SAL_TAA,SAL_TAN,SAL_OBL,SAL_OBLD,SML_VALOR "
                    + "FROM E_SAL, E_SML "
                    + "WHERE E_SAL.SAL_MUN=E_SML.SML_MUN (+) "
                    + "AND E_SAL.SAL_PRO=E_SML.SML_PRO (+) AND E_SAL.SAL_TRA=E_SML.SML_TRA (+) "
                    + "AND (SML_CMP='TXT' OR SML_CMP IS NULL) AND (SML_LENG=? OR SML_LENG IS NULL) "
                    + "AND SAL_MUN=? AND SAL_PRO=? AND SAL_TRA=?";

            if (log.isDebugEnabled()) {
                log.debug("DefinicionTramitesDAO, getTramitesImportacion->getCondSalTramite: Sentencia SQL:" + sql);
            }
            ps = con.prepareStatement(sql);
            ps.setInt(contbd++, Integer.parseInt(idiomaDefecto));
            ps.setInt(contbd++, codMunicipio);
            ps.setString(contbd++, codProcedimiento);
            ps.setInt(contbd++, codTramite);
            rs = ps.executeQuery();
            while (rs.next()) {
                String tipoCondicion = rs.getString("SAL_TAC");
                defTramVO.setTipoCondicion(tipoCondicion);
                    if (tipoCondicion != null && tipoCondicion.equals("P")) {
                        defTramVO.setTexto(rs.getString("SML_VALOR"));
                    } else {
                        defTramVO.setTexto("");
                    }

                String tipoCondicionFav = rs.getString("SAL_TAA");
                if (tipoCondicionFav != null && "T".equals(tipoCondicionFav)) {
                    defTramVO.setTipoFavorableSI("TramiteSI");
                } else if (tipoCondicionFav != null && "F".equals(tipoCondicionFav)) {
                    defTramVO.setTipoFavorableSI("FinalizacionSI");
                } else {
                    defTramVO.setTipoFavorableSI("");
                }
                String tipoCondicionDesf = rs.getString("SAL_TAN");
                if (tipoCondicionDesf != null && "T".equals(tipoCondicionDesf)) {
                    defTramVO.setTipoFavorableNO("TramiteNO");
                } else if (tipoCondicionDesf != null && "F".equals(tipoCondicionDesf)) {
                    defTramVO.setTipoFavorableNO("FinalizacionNO");
                } else {
                    defTramVO.setTipoFavorableNO("");
                }
                defTramVO.setObligatorio(rs.getString("SAL_OBL"));
                defTramVO.setObligatorioDesf(rs.getString("SAL_OBLD"));
            
                //2. flujo de salida E_FLS
                if (tipoCondicion != null && (tipoCondicion.equals("T") || tipoCondicion.equals("P") || tipoCondicion.equals("R"))) {
                    sql = "SELECT FLS_NUC,FLS_NUS,FLS_CTS "
                            + "FROM E_FLS "
                            + "WHERE FLS_MUN=? AND FLS_PRO=? AND FLS_TRA=?";

                    if (log.isDebugEnabled()) {
                        log.debug("DefinicionTramitesDAO, getTramitesImportacion->getCondSalTramite: Sentencia SQL:" + sql);
                    }
                    ps = con.prepareStatement(sql);
                    contbd = 1;
                    ps.setInt(contbd++, codMunicipio);
                    ps.setString(contbd++, codProcedimiento);
                    ps.setInt(contbd++, codTramite);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        FlujoSalidaTramiteVO fSVO = new FlujoSalidaTramiteVO();
                        fSVO.setCodigoTramiteDestino(String.valueOf(rs.getInt("FLS_CTS")));
                        fSVO.setNumeroCondicionSalida(String.valueOf(rs.getInt("FLS_NUC")));
                        fSVO.setNumeroSecuencia(String.valueOf(rs.getInt("FLS_NUS")));

                        listaFlujoSalida.add(fSVO);
                    }
                    defTramVO.setFlujoSalidaTramiteImportacion(listaFlujoSalida);
                }
            }
            rs.close();
            ps.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error al recuperar datos de condiciones de salida de tramite");
            throw new ImportacionFlujoBibliotecaException(85,ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de BBDD");
            }
        }
    }

    public void getCamposSuplTramite(Connection con, String codProcedimiento, int codMunicipio, int codTramite, DefinicionTramitesValueObject defTramVO) throws ImportacionFlujoBibliotecaException  {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql;
        int contbd = 1;
        ArrayList<DefinicionAgrupacionCamposValueObject> listaAgrupaciones = new ArrayList<DefinicionAgrupacionCamposValueObject>();
        ArrayList<DefinicionCampoValueObject> listasCampos = new ArrayList<DefinicionCampoValueObject>();

        try {
            //1.Agrupaciones de campos E_TCA_GROUP
            sql = "SELECT TCA_ID_GROUP, TCA_DESC_GROUP, TCA_ORDER_GROUP, TCA_ACTIVE "
                    + "FROM E_TCA_GROUP "
                    + "WHERE TCA_PRO = ? AND TCA_TRA = ?";

            if (log.isDebugEnabled()) {
                log.debug("DefinicionTramitesDAO, getTramitesImportacion->getCamposSuplTramite: Sentencia SQL:" + sql);
            }
            ps = con.prepareStatement(sql);
            ps.setString(contbd++, codProcedimiento);
            ps.setInt(contbd++, codTramite);
            rs = ps.executeQuery();
            while (rs.next()) {
                DefinicionAgrupacionCamposValueObject dacVO = new DefinicionAgrupacionCamposValueObject();
                dacVO.setCodAgrupacion(rs.getString("TCA_ID_GROUP"));
                dacVO.setDescAgrupacion(rs.getString("TCA_DESC_GROUP"));
                dacVO.setOrdenAgrupacion(rs.getInt("TCA_ORDER_GROUP"));
                dacVO.setAgrupacionActiva(rs.getString("TCA_ACTIVE"));

                listaAgrupaciones.add(dacVO);
            }
            defTramVO.setListaAgrupaciones(new Vector(listaAgrupaciones));

            rs.close();
            ps.close();

            //2. campos suplementarios E_TCA
            sql = "SELECT TCA_COD,TCA_DES,TCA_PLT,TCA_TDA,TCA_TAM,TCA_MAS,TCA_OBL,TCA_NOR,TCA_ROT,TCA_VIS,TCA_ACTIVO,"
                    + "TCA_DESPLEGABLE,TCA_OCULTO,TCA_BLOQ,PLAZO_AVISO, PERIODO_PLAZO,EXPRESION_CAMPO_NUM_TRAM.EXPRESION AS EXPRESION,"
                    + "EXPRESION_CAMPO_CAL_TRAM.EXPRESION AS EXPRESION_CAL,TCA_TRA,PCA_GROUP, TCA_POS_X, TCA_POS_Y "
                    + "FROM E_TCA,EXPRESION_CAMPO_NUM_TRAM,EXPRESION_CAMPO_CAL_TRAM "
                    + "WHERE E_TCA.TCA_MUN=EXPRESION_CAMPO_NUM_TRAM.COD_ORGANIZACION (+) AND E_TCA.TCA_PRO=EXPRESION_CAMPO_NUM_TRAM.COD_PROCEDIMIENTO (+) "
                    + "AND E_TCA.TCA_COD=EXPRESION_CAMPO_NUM_TRAM.COD_CAMPO (+) AND E_TCA.TCA_TRA=EXPRESION_CAMPO_NUM_TRAM.COD_TRAMITE (+) "
                    + "AND E_TCA.TCA_MUN=EXPRESION_CAMPO_CAL_TRAM.COD_ORGANIZACION (+) AND E_TCA.TCA_PRO=EXPRESION_CAMPO_CAL_TRAM.COD_PROCEDIMIENTO (+) "
                    + "AND E_TCA.TCA_COD=EXPRESION_CAMPO_CAL_TRAM.COD_CAMPO (+) AND E_TCA.TCA_TRA=EXPRESION_CAMPO_CAL_TRAM.COD_TRAMITE (+) "
                    + "AND TCA_MUN=? AND TCA_PRO=? AND TCA_TRA=?";
            if (log.isDebugEnabled()) {
                log.debug("DefinicionTramitesDAO, getTramitesImportacion->getCamposSuplTramite: Sentencia SQL:" + sql);
            }
            ps = con.prepareStatement(sql);
            contbd = 1;
            ps.setInt(contbd++, codMunicipio);
            ps.setString(contbd++, codProcedimiento);
            ps.setInt(contbd++, codTramite);
            rs = ps.executeQuery();
            while (rs.next()) {
                DefinicionCampoValueObject dCVO = new DefinicionCampoValueObject();
                dCVO.setCodCampo(rs.getString("TCA_COD"));
                dCVO.setDescCampo(rs.getString("TCA_DES"));
                String codTipoDato = rs.getString("TCA_TDA");
                dCVO.setCodTipoDato(codTipoDato);
                if (codTipoDato.equals("6") || codTipoDato.equals("10")) { //desplegables o desplegables externos
                    dCVO.setCodPlantilla(rs.getString("TCA_DESPLEGABLE"));
                } else {
                    dCVO.setCodPlantilla(rs.getString("TCA_PLT"));
                }
                dCVO.setTamano(rs.getString("TCA_TAM"));
                dCVO.setDescMascara(rs.getString("TCA_MAS"));
                dCVO.setObligat(rs.getString("TCA_OBL"));
                dCVO.setOrden(rs.getString("TCA_NOR"));
                dCVO.setRotulo(rs.getString("TCA_ROT"));
                dCVO.setVisible(rs.getString("TCA_VIS"));
                dCVO.setActivo(rs.getString("TCA_ACTIVO"));
                dCVO.setOculto(rs.getString("TCA_OCULTO"));
                dCVO.setBloqueado(rs.getString("TCA_BLOQ"));
                dCVO.setPlazoFecha(rs.getString("PLAZO_AVISO"));
                dCVO.setCheckPlazoFecha(rs.getString("PERIODO_PLAZO"));
                dCVO.setValidacion(rs.getString("EXPRESION"));
                dCVO.setOperacion(rs.getString("EXPRESION_CAL"));
                String codAgrupacion = rs.getString("PCA_GROUP");
                if (codAgrupacion != null) {
                    dCVO.setCodAgrupacion(codAgrupacion);
                } else {
                    dCVO.setCodAgrupacion("DEF");
                }
                dCVO.setPosX(rs.getString("TCA_POS_X"));
                dCVO.setPosY(rs.getString("TCA_POS_Y"));

                listasCampos.add(dCVO);
            }
            defTramVO.setListaCampos(new Vector(listasCampos));
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error al recuperar datos de datos suplementarios de tramite");
            throw new ImportacionFlujoBibliotecaException(86,ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de BBDD");
            }
        }
    }

    protected ArrayList<AvanzarRetrocederSWVO> getListaConfSW(int codMunicipio, String codProcedimiento, int codTramite, Connection con) throws ImportacionFlujoBibliotecaException  {
        ArrayList<AvanzarRetrocederSWVO> lista = new ArrayList<AvanzarRetrocederSWVO>();
        Hashtable<Integer, Hashtable<String, GeneralValueObject>> datos = null;
        try {

            datos = this.getMapConfSW(codMunicipio, codProcedimiento, codTramite, con);
            Set<Integer> keySet = datos.keySet();
            ArrayList<Integer> claves = new ArrayList<Integer>();
            claves.addAll(keySet);

            for (int i = (claves.size() - 1); i >= 0; i--) {
                int clave = claves.get(i);
                log.debug(" -- Tratando operaciones del orden " + clave);
                // Se recuperan los elementos de una fila de operaciones
                Hashtable<String, GeneralValueObject> orden = datos.get(clave);
                AvanzarRetrocederSWVO avRetSWVO = new AvanzarRetrocederSWVO();
                if (!orden.containsKey("INICIAR")) {
                    avRetSWVO.setCodIniciar(-1);
                    avRetSWVO.setCfoIniciar(-1);
                } else {
                    GeneralValueObject gVO = orden.get("INICIAR");
                    int codOp = (Integer) gVO.getAtributo("COD_OP");
                    long cfo = (Long) gVO.getAtributo("CFO");
                    String nombreOperacion = (String) gVO.getAtributo("NOMBRE_OPERACION");
                    String tipoOrigen = (String) gVO.getAtributo("TIPO_ORIGEN");
                    String nombreModulo = (String) gVO.getAtributo("NOMBRE_MODULO");

                    avRetSWVO.setCodIniciar(codOp);
                    avRetSWVO.setCfoIniciar(cfo);
                    //avRetSWVO.setNombreOperacion(nombreOperacion.trim());
                    //avRetSWVO.setTipoOrigenOperacion(tipoOrigen);
                    avRetSWVO.setNombreOperacionIniciar(nombreOperacion);
                    avRetSWVO.setTipoOperacionIniciar(tipoOrigen);
                    if (nombreModulo != null || !"".equals(nombreModulo) || !"null".equals(nombreModulo)) {
                        avRetSWVO.setNombreModuloIniciar(nombreModulo);
                    } else {
                        avRetSWVO.setNombreModuloIniciar("");
                    }

                    avRetSWVO.setTipoRetroceso(-1);
                    avRetSWVO.setNumeroOrden((Integer) gVO.getAtributo("NUMERO_ORDEN"));
                }

                if (!orden.containsKey("AVANZAR")) {
                    avRetSWVO.setCodAvanzar(-1);
                    avRetSWVO.setCfoAvanzar(-1);
                } else {
                    GeneralValueObject gVO = orden.get("AVANZAR");
                    int codOp = (Integer) gVO.getAtributo("COD_OP");
                    long cfo = (Long) gVO.getAtributo("CFO");
                    String nombreOperacion = (String) gVO.getAtributo("NOMBRE_OPERACION");
                    String tipoOrigen = (String) gVO.getAtributo("TIPO_ORIGEN");
                    avRetSWVO.setCodAvanzar(codOp);
                    avRetSWVO.setCfoAvanzar(cfo);
                    String nombreModulo = (String) gVO.getAtributo("NOMBRE_MODULO");

                    avRetSWVO.setNombreOperacionAvanzar(nombreOperacion);
                    avRetSWVO.setTipoOperacionAvanzar(tipoOrigen);

                    if (nombreModulo != null || !"".equals(nombreModulo) || !"null".equals(nombreModulo)) {
                        avRetSWVO.setNombreModuloAvanzar(nombreModulo);
                    } else {
                        avRetSWVO.setNombreModuloAvanzar("");
                    }

                    avRetSWVO.setTipoRetroceso(-1);
                    avRetSWVO.setNumeroOrden((Integer) gVO.getAtributo("NUMERO_ORDEN"));
                }

                if (!orden.containsKey("RETROCEDER")) {
                    avRetSWVO.setCodRetroceder(-1);
                    avRetSWVO.setCfoRetroceder(-1);
                } else {
                    GeneralValueObject gVO = orden.get("RETROCEDER");
                    int codOp = (Integer) gVO.getAtributo("COD_OP");
                    long cfo = (Long) gVO.getAtributo("CFO");
                    String nombreOperacion = (String) gVO.getAtributo("NOMBRE_OPERACION");
                    String tipoOrigen = (String) gVO.getAtributo("TIPO_ORIGEN");
                    String nombreModulo = (String) gVO.getAtributo("NOMBRE_MODULO");
                    String tipoOperacionRetroceso = (String) gVO.getAtributo("TIPO_OPERACION_RETROCESO");

                    avRetSWVO.setCodRetroceder(codOp);
                    avRetSWVO.setCfoRetroceder(cfo);

                    avRetSWVO.setNombreOperacionRetroceder(nombreOperacion);
                    avRetSWVO.setTipoOperacionRetroceder(tipoOrigen);

                    if (nombreModulo != null || !"".equals(nombreModulo) || !"null".equals(nombreModulo)) {
                        avRetSWVO.setNombreModuloRetroceder(nombreModulo);
                    } else {
                        avRetSWVO.setNombreModuloRetroceder("");
                    }

                    if (tipoOperacionRetroceso != null && tipoOperacionRetroceso.length() > 0) {
                        avRetSWVO.setTipoRetroceso(Integer.parseInt(tipoOperacionRetroceso));
                    }
                    avRetSWVO.setNumeroOrden((Integer) gVO.getAtributo("NUMERO_ORDEN"));
                }

                lista.add(avRetSWVO);
            }

        } catch (TechnicalException e) {
            e.printStackTrace();
            throw new ImportacionFlujoBibliotecaException(87,e.getMessage());
        } catch (InternalErrorException e) {
            e.printStackTrace();
            throw new ImportacionFlujoBibliotecaException(87,e.getMessage());
        }

        return lista;
    }

    protected Hashtable<Integer, Hashtable<String, GeneralValueObject>> getMapConfSW(int codMunicipio, String codProcedimiento, int codTramite, Connection con) throws TechnicalException, InternalErrorException {

        Hashtable<Integer, Hashtable<String, GeneralValueObject>> salida = new Hashtable<Integer, Hashtable<String, GeneralValueObject>>();

        String sqlListaConfSW = "SELECT DEF_TRA_OP,DEF_TRA_ORD,DEF_TRA_AVZ, DEF_TRA_CFO,DEF_TRA_NOMBRE_OPERACION,DEF_TRA_TIPO_ORIGEN_OPERACION,DEF_TRA_NOMBRE_MODULO,TIPO_OP_RETROCESO FROM DEF_TRA_SW WHERE "
                + "DEF_TRA_MUN = " + codMunicipio + " AND DEF_TRA_PRO ='" + codProcedimiento + "' AND DEF_COD_TRA =" + codTramite + " ORDER BY DEF_TRA_ORD,DEF_TRA_AVZ";
        Statement st = null;
        ResultSet rs = null;

        try {
            st = con.createStatement();
            rs = st.executeQuery(sqlListaConfSW);
            int avz = -1;
            long cfo = -1;
            int i = 1;
            int ordenNuevo = 0;

            while (rs.next()) {
                int codOp = rs.getInt("DEF_TRA_OP");
                avz = rs.getInt("DEF_TRA_AVZ");
                cfo = rs.getLong("DEF_TRA_CFO");
                ordenNuevo = rs.getInt("DEF_TRA_ORD");
                String tipoOrigen = rs.getString("DEF_TRA_TIPO_ORIGEN_OPERACION");
                String nombreModulo = rs.getString("DEF_TRA_NOMBRE_MODULO");
                String tipoOperacionRetroceso = rs.getString("TIPO_OP_RETROCESO");

                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("COD_OP", codOp);
                gVO.setAtributo("CFO", cfo);
                gVO.setAtributo("NOMBRE_OPERACION", rs.getString("DEF_TRA_NOMBRE_OPERACION"));
                gVO.setAtributo("TIPO_ORIGEN", tipoOrigen);
                gVO.setAtributo("NOMBRE_MODULO", nombreModulo);
                gVO.setAtributo("TIPO_OPERACION_RETROCESO", tipoOperacionRetroceso);
                gVO.setAtributo("NUMERO_ORDEN", ordenNuevo);

                Hashtable<String, GeneralValueObject> aux = new Hashtable<String, GeneralValueObject>();
                if (avz == 0) {
                    aux.put("RETROCEDER", gVO);
                } else if (avz == 1) {
                    aux.put("AVANZAR", gVO);
                } else if (avz == 2) {
                    aux.put("INICIAR", gVO);
                }
                salida.put(i, aux);
                i++;
            }// while

        } catch (SQLException sqle) {
            log.error("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA OBTENER LA LISTA DE SW AV/RET DEL TRAMITE", sqle);
        } finally {
            GeneralOperations.closeResultSet(rs);
            GeneralOperations.closeStatement(st);
        }

        return salida;
    }

    public boolean grabarDatosTramite(DefinicionTramitesValueObject defTramVO, Connection con, String codProcedimiento, int nuevoCodTramite, int codMunicipio) throws ImportacionFlujoBibliotecaException {
        PreparedStatement ps = null;
        String sql = "", sqlUT = "";
        int insTra = 0, insTml = 0, insUT = 0, numUnidTram = 0;
        int contbd = 1;
        ArrayList<Integer> listaUnidades = null;

        try {
            //1.datos de tramite
            sql = "INSERT INTO E_TRA (TRA_PRO,TRA_COD,TRA_PLZ,TRA_UND,TRA_OCU,TRA_MUN,TRA_VIS,TRA_UIN,TRA_UTR,TRA_CLS,TRA_ARE,TRA_FBA,"
                    + "TRA_COU,TRA_PRE,TRA_INS,TRA_WS_COD,TRA_WS_OB,TRA_UTI,TRA_UTF,TRA_USI,TRA_USF,TRA_INI,TRA_INF, TRA_NOTIF_UITI, TRA_NOTIF_UITF, TRA_NOTIF_UIEI, TRA_NOTIF_UIEF, "
                     + "TRA_NOTIF_USUTRA_FINPLAZO, TRA_NOTIF_USUEXP_FINPLAZO, TRA_NOTIF_UOR_FINPLAZO, "
                    + "TRA_WST_COD,TRA_WST_OB,"
                    + "TRA_WSR_COD,TRA_WSR_OB,TRA_PRR,TRA_CAR,TRA_FIN,TRA_GENERARPLZ,TRA_NOTCERCAFP,TRA_NOTFUERADP,TRA_TIPNOTCFP,"
                    + "TRA_TIPNOTFDP,TRA_NOTIFICACION_ELECTRONICA,TRA_COD_TIPO_NOTIFICACION,TRA_TIPO_USUARIO_FIRMA,TRA_OTRO_COD_USUARIO_FIRMA,"
                    + "TRA_TRAM_EXT_PLUGIN,TRA_TRAM_EXT_URL,TRA_TRAM_EXT_IMPLCLASS,TRA_TRAM_ID_ENLACE_PLUGIN,TRA_NOTIF_ELECT_OBLIG,TRA_NOTIF_FIRMA_CERT_ORG,"
                    + "COD_DEPTO_NOTIFICACION,TRA_NOTIFICADO) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            ps = con.prepareStatement(sql);
            ps.setString(contbd++, codProcedimiento);
            ps.setInt(contbd++, nuevoCodTramite);
            String plazo = defTramVO.getPlazo();
            if (plazo != null) {
                ps.setString(contbd++, plazo);
            } else {
                ps.setNull(contbd++, Types.INTEGER);
            }
            String udsPlazo = defTramVO.getUnidadesPlazo();
            if (udsPlazo != null) {
                ps.setString(contbd++, udsPlazo);
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            String ocurrencias = defTramVO.getOcurrencias();
            if (ocurrencias != null) {
                ps.setString(contbd++, ocurrencias);
            } else {
                ps.setNull(contbd++, Types.INTEGER);
            }
            ps.setInt(contbd++, codMunicipio);
            ps.setInt(contbd++, Integer.parseInt(defTramVO.getDisponible()));
            String codUnidadInicio = defTramVO.getCodUnidadInicio();
            if (codUnidadInicio != null) {
                ps.setInt(contbd++, Integer.parseInt(codUnidadInicio));
            } else {
                ps.setNull(contbd++, Types.INTEGER);
            }
            String codUnidadTramite = defTramVO.getCodUnidadTramite();
            if (codUnidadTramite != null) {
                ps.setInt(contbd++, Integer.parseInt(codUnidadTramite));
            } else {
                ps.setNull(contbd++, Types.INTEGER);
            }
            String codClasifTramite = defTramVO.getCodClasifTramite();
            if (codClasifTramite != null) {
                ps.setInt(contbd++, Integer.parseInt(codClasifTramite));
            } else {
                ps.setNull(contbd++, Types.INTEGER);
            }
            String area = defTramVO.getCodAreaTra();
            if (area != null) {
                ps.setInt(contbd++, Integer.parseInt(area));
            } else {
                ps.setNull(contbd++, Types.INTEGER);
            }
            String fBaja = defTramVO.getFechaBaja();
            if (fBaja != null) {
                ps.setDate(contbd++, Date.valueOf(fBaja));
            } else {
                ps.setNull(contbd++, Types.DATE);
            }
            String codTraVisible = defTramVO.getNumeroTramite();
            if (codTraVisible != null) {
                ps.setInt(contbd++, Integer.parseInt(codTraVisible));
            } else {
                ps.setNull(contbd++, Types.INTEGER);
            }
            ps.setInt(contbd++, Integer.parseInt(defTramVO.getTramitePregunta()));
            String instrucc = defTramVO.getInstrucciones();
            if (instrucc != null) {
                ps.setString(contbd++, AdaptadorSQLBD.js_unescape(instrucc));
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            String tramWSCod = defTramVO.getTramWSCod();
            if (tramWSCod != null) {
                ps.setString(contbd++, tramWSCod);
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            String tramWSOb = defTramVO.getTramWSOb();
            if (tramWSOb != null) {
                ps.setString(contbd++, tramWSOb);
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            String uniTramIni = defTramVO.getNotUnidadTramitIni();
            if (uniTramIni.equals("1")) {
                ps.setString(contbd++, "S");
            } else {
                ps.setString(contbd++, "N");
            }
            String uniTramFin = defTramVO.getNotUnidadTramitFin();
            if (uniTramFin.equals("1")) {
                ps.setString(contbd++, "S");
            } else {
                ps.setString(contbd++, "N");
            }
            String usuUniTramIni = defTramVO.getNotUsuUnidadTramitIni();
            if (usuUniTramIni.equals("1")) {
                ps.setString(contbd++, "S");
            } else {
                ps.setString(contbd++, "N");
            }
            String usuUniTramFin = defTramVO.getNotUsuUnidadTramitFin();
            if (usuUniTramFin.equals("1")) {
                ps.setString(contbd++, "S");
            } else {
                ps.setString(contbd++, "N");
            }
            String interesadosIni = defTramVO.getNotInteresadosIni();
            if (interesadosIni.equals("1")) {
                ps.setString(contbd++, "S");
            } else {
                ps.setString(contbd++, "N");
            }
            String interesadosFin = defTramVO.getNotInteresadosFin();
            if (interesadosFin.equals("1")) {
                ps.setString(contbd++, "S");
            } else {
                ps.setString(contbd++, "N");
            }
            
            String usuInicioTramiteIni = defTramVO.getNotUsuInicioTramiteIni();
            if(usuInicioTramiteIni.equals("1")){
                ps.setString(contbd++, "S");
            }else {
                ps.setString(contbd++, "N");
            }

            String usuInicioTramiteFin = defTramVO.getNotUsuInicioTramiteFin();
            if(usuInicioTramiteFin.equals("1")){
                ps.setString(contbd++, "S");
            }else {
                ps.setString(contbd++, "N");
            }

            String usuInicioExpedIni = defTramVO.getNotUsuInicioExpedIni();
            if(usuInicioExpedIni.equals("1")){
                ps.setString(contbd++, "S");
            }else {
                ps.setString(contbd++, "N");
            }

            String usuInicioExpedFin = defTramVO.getNotUsuInicioExpedFin();
            if(usuInicioExpedFin.equals("1")){
                ps.setString(contbd++, "S");
            }else {
                ps.setString(contbd++, "N");
            }
            String notUsuTraFinPlazo = defTramVO.getNotUsuTraFinPlazo();	
            if(notUsuTraFinPlazo.equals("1")){	
                ps.setString(contbd++,"S");	
            } else {	
                ps.setString(contbd++, "N");	
            }	
            String notUsuExpFinPlazo = defTramVO.getNotUsuExpFinPlazo();	
            if(notUsuExpFinPlazo.equals("1")){	
                ps.setString(contbd++,"S");	
            }else{	
                ps.setString(contbd++,"N");	
            }	
            String notUORFinPlazo = defTramVO.getNotUORFinPlazo();	
            if(notUORFinPlazo.equals("1")){	
                ps.setString(contbd++,"S");	
            } else {	
                ps.setString(contbd++,"N");	
            }
            String tramWSTCod = defTramVO.getTramWSTCod();
            if (tramWSTCod != null) {
                ps.setString(contbd++, tramWSTCod);
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            String tramWSTOb = defTramVO.getTramWSTOb();
            if (tramWSTOb != null) {
                ps.setString(contbd++, tramWSTOb);
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            String tramWSRCod = defTramVO.getTramWSRCod();
            if (tramWSRCod != null) {
                ps.setString(contbd++, tramWSRCod);
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            String tramWSROb = defTramVO.getTramWSROb();
            if (tramWSROb != null) {
                ps.setString(contbd++, tramWSROb);
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            String codExpRel = defTramVO.getCodExpRel();
            if (codExpRel != null) {
                ps.setString(contbd++, codExpRel);
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            String cargo = defTramVO.getCodCargo();
            if (cargo != null) {
                ps.setInt(contbd++, Integer.parseInt(cargo));
            } else {
                ps.setNull(contbd++, Types.INTEGER);
            }
            ps.setInt(contbd++, defTramVO.getPlazoFin());
            ps.setBoolean(contbd++, defTramVO.isGenerarPlazos());
            ps.setBoolean(contbd++, defTramVO.getNotificarCercaFinPlazo());
            ps.setBoolean(contbd++, defTramVO.getNotificarFueraDePlazo());
            ps.setInt(contbd++, defTramVO.getTipoNotCercaFinPlazo());
            ps.setInt(contbd++, defTramVO.getTipoNotFueraDePlazo());
            String notifElect = defTramVO.getAdmiteNotificacionElectronica();
            if (notifElect != null) {
                ps.setString(contbd++, notifElect);
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            String codTipoNotif = defTramVO.getCodigoTipoNotificacionElectronica();
            if (codTipoNotif != null) {
                ps.setInt(contbd++, Integer.parseInt(codTipoNotif));
            } else {
                ps.setNull(contbd++, Types.INTEGER);
            }
            String usuFirma = defTramVO.getTipoUsuarioFirma();
            if (usuFirma != null) {
                ps.setString(contbd++, usuFirma);
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            String otroUsuFirma = defTramVO.getCodigoOtroUsuarioFirma();
            if (otroUsuFirma != null) {
                ps.setInt(contbd++, Integer.parseInt(otroUsuFirma));
            } else {
                ps.setNull(contbd++, Types.INTEGER);
            }
            String codPlugin = defTramVO.getCodPluginPantallaTramitacionExterna();
            if (codPlugin != null) {
                ps.setString(contbd++, codPlugin);
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            String urlPlugin = defTramVO.getUrlPluginPantallaTramitacionExterna();
            if (urlPlugin != null) {
                ps.setString(contbd++, urlPlugin);
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            String implClassPlugin = defTramVO.getImplClassPluginPantallaTramitacionExterna();
            if (implClassPlugin != null) {
                ps.setString(contbd++, implClassPlugin);
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            String codUrlPlugin = defTramVO.getCodUrlPluginPantallaTramitacionExterna();
            if (codUrlPlugin != null) {
                ps.setString(contbd++, codUrlPlugin);
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            ps.setBoolean(contbd++, defTramVO.getNotificacionElectronicaObligatoria());
            ps.setBoolean(contbd++, defTramVO.getCertificadoOrganismoFirmaNotificacion());
            String codDeptNotif = defTramVO.getCodDepartamentoNotificacion();
            if (codDeptNotif != null) {
                ps.setString(contbd++, codDeptNotif);
            } else {
                ps.setNull(contbd++, Types.VARCHAR);
            }
            ps.setInt(contbd++, (defTramVO.isTramiteNotificado()?1:0));

            insTra = ps.executeUpdate();
            if (log.isDebugEnabled()) {
                log.debug("Query de inserción en E_TRA: " + sql);
            }

            ps.close();

            //2. multilenguaje de trámite
            sql = "INSERT INTO E_TML (TML_MUN,TML_PRO,TML_TRA,TML_CMP,TML_LENG,TML_VALOR) "
                    + "VALUES (?,?,?,?,?,?)";

            contbd = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(contbd++, codMunicipio);
            ps.setString(contbd++, codProcedimiento);
            ps.setInt(contbd++, nuevoCodTramite);
            ps.setString(contbd++, "NOM");
            ps.setString(contbd++, idiomaDefecto);
            ps.setString(contbd++, defTramVO.getNombreTramite());
            insTml = ps.executeUpdate();

            if (log.isDebugEnabled()) {
                log.debug("Query de inserción en E_TML: " + sql);
            }

            ps.close();

            //3. unidades tramitadoras
            listaUnidades = new ArrayList<Integer>(defTramVO.getListaUnidadesTramitadoras());
            numUnidTram = listaUnidades.size();
            if (numUnidTram > 0) {
                sqlUT = "INSERT INTO E_TRA_UTR (TRA_MUN,TRA_PRO,TRA_COD,TRA_UTR_COD) VALUES (?,?,?,?)";
                for (int i = 0; i < listaUnidades.size(); i++) {
                    sql = sqlUT;
                    ps = con.prepareStatement(sql);
                    contbd = 1;
                    ps.setInt(contbd++, codMunicipio);
                    ps.setString(contbd++, codProcedimiento);
                    ps.setInt(contbd++, nuevoCodTramite);
                    ps.setInt(contbd, listaUnidades.get(i));
                    insUT += ps.executeUpdate();
                    ps.close();
                }
                if (log.isDebugEnabled()) {
                    log.debug("Query de inserción de unidades tramitadoras: " + sqlUT);
                    log.debug("Filas insertadas: " + insUT);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error al grabar datos de tramite");
            throw new ImportacionFlujoBibliotecaException(91,ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de BBDD");
            }
        }
        
        return (insTra > 0 && insTml > 0 && insUT == numUnidTram);
    }

    public boolean grabarDocsTramite(Connection con, String codProcedimiento, int nuevoCodTramite, int codMunicipio, ArrayList<DefinicionTramitesValueObject> listaDoc) throws ImportacionFlujoBibliotecaException  {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "", sqlDot = "", sqlPlt = "", sqlSelect = "";
        int insDot = 0, insPlt = 0;
        int contbd;

        try {
            
            sqlDot = "INSERT INTO E_DOT (DOT_MUN,DOT_PRO,DOT_TRA,DOT_COD,DOT_TDO,DOT_VIS,DOT_FRM,DOT_PLT,DOT_ACTIVO) "
                    + "VALUES (?,?,?,?,?,?,?,?,?)";
            sqlPlt = "INSERT INTO A_PLT (PLT_COD,PLT_DES,PLT_APL,PLT_DOC,PLT_PRO,PLT_TRA,PLT_INT,PLT_REL, "
                    + "PLT_EDITOR_TEXTO) "
                    + "VALUES (?,?,?,?,?,?,?,?,?)";
            for (DefinicionTramitesValueObject dTVO : listaDoc) {
                //obtengo codigo consecutivo de plantilla
                sqlSelect = "SELECT PLT_COD FROM A_PLT ORDER BY PLT_COD DESC";
                int codPlt = 0;
                ps = con.prepareStatement(sqlSelect);
                rs = ps.executeQuery();
                while(rs.next()){
                    codPlt = rs.getInt("PLT_COD");
                    break;
                }
                codPlt++;
                ps.close();
                
                sql = sqlPlt;
                contbd = 1;
                ps = con.prepareStatement(sql);
                ps.setInt(contbd++, codPlt);
                ps.setString(contbd++, dTVO.getPlantilla());
                ps.setInt(contbd++, Integer.parseInt(dTVO.getCodAplicacion()));
                byte[] plantillaTram = dTVO.getByteArrayPlantillaTramitacion();
                if(plantillaTram != null && plantillaTram.length > 0){
                    InputStream stream = new ByteArrayInputStream(plantillaTram);
                    ps.setBinaryStream(contbd++, stream,plantillaTram.length);
                } else
                    ps.setNull(contbd++, Types.BINARY);
                ps.setString(contbd++, codProcedimiento);
                ps.setInt(contbd++, nuevoCodTramite);
                ps.setString(contbd++, dTVO.getInteresado());
                ps.setString(contbd++, dTVO.getRelacion());
                ps.setString(contbd++, dTVO.getEditorTexto());
                insPlt += ps.executeUpdate();
                ps.close();
                
                sql = sqlDot;
                ps = con.prepareStatement(sql);
                contbd = 1;
                ps.setInt(contbd++, codMunicipio);
                ps.setString(contbd++, codProcedimiento);
                ps.setInt(contbd++, nuevoCodTramite);
                ps.setInt(contbd++, Integer.parseInt(dTVO.getCodigoDoc()));
                String tipoDoc = dTVO.getCodTipoDoc();
                if(StringUtils.isNotNullOrEmptyOrNullString(tipoDoc))
                    ps.setInt(contbd++, Integer.parseInt(tipoDoc));
                else
                    ps.setNull(contbd++, Types.INTEGER);
                ps.setString(contbd++, dTVO.getVisibleInternet());
                String firma = dTVO.getFirma();
                if(StringUtils.isNotNullOrEmptyOrNullString(firma))
                    ps.setString(contbd++, firma);
                else
                    ps.setNull(contbd++, Types.CHAR);
                ps.setInt(contbd++, codPlt);
                ps.setString(contbd++, dTVO.getDocActivo());

                insDot += ps.executeUpdate();
                ps.close();
            }
            if (log.isDebugEnabled()) {
                log.debug("Query de inserción de documentos: " + sqlDot);
                log.debug("Filas insertadas: " + insDot);
                log.debug("Query de inserción de información de plantilla del documentos: " + sqlPlt);
                log.debug("Filas insertadas: " + insPlt);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error al grabar datos de documentos de tramite");
            throw new ImportacionFlujoBibliotecaException(92,ex.getMessage());
        } finally {
            try {
                if(rs != null) rs.close();
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de BBDD");
            }
        }
        
        return (insDot > 0 && insDot == insPlt && insDot == listaDoc.size());
    }

    public boolean grabarEnlacesTramite(DefinicionTramitesValueObject defTramVO, Connection con, String codProcedimiento, int nuevoCodTramite, int codMunicipio, ArrayList<Integer> codsEnlaces) throws ImportacionFlujoBibliotecaException  {
        PreparedStatement ps = null;
        String sql = "", sqlEnl = "";
        int insEnl = 0;
        int contbd = 1;
        ArrayList<String> descsEnlaces = null;
        ArrayList<String> urlsEnlaces = null;
        ArrayList<Integer> estadosEnlaces = null;

        try {
            descsEnlaces = new ArrayList<String>(defTramVO.getListaDescripcionEnlaces());
            urlsEnlaces = new ArrayList<String>(defTramVO.getListaEnlaces());
            estadosEnlaces = new ArrayList<Integer>(defTramVO.getListaEstadoEnlaces());
            sqlEnl = "INSERT INTO E_TEN (TEN_MUN,TEN_PRO,TEN_TRA,TEN_COD,TEN_DES,TEN_URL,TEN_EST) "
                    + "VALUES (?,?,?,?,?,?,?)";
            for (int i = 0; i < codsEnlaces.size(); i++) {
                sql = sqlEnl;
                ps = con.prepareStatement(sql);
                ps.setInt(contbd++, codMunicipio);
                ps.setString(contbd++, codProcedimiento);
                ps.setInt(contbd++, nuevoCodTramite);
                ps.setInt(contbd++, codsEnlaces.get(i));
                ps.setString(contbd++, descsEnlaces.get(i));
                ps.setString(contbd++, urlsEnlaces.get(i));
                ps.setInt(contbd++, estadosEnlaces.get(i));
                insEnl += ps.executeUpdate();
                ps.close();
            }
            if (log.isDebugEnabled()) {
                log.debug("Query de inserción de enlaces:" + sqlEnl);
                log.debug("Filas insertadas:" + insEnl);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error al grabar datos de enlaces de tramite");
            throw new ImportacionFlujoBibliotecaException(93,ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de BBDD");
            }
        }
        
        return (insEnl == codsEnlaces.size());
    }

    public boolean grabarCondsEnt(Connection con, String codProcedimiento, String codBiblioteca, int nuevoCodTramite, int codMunicipio, ArrayList<DefinicionTramitesValueObject> listaCondEntrada) throws ImportacionFlujoBibliotecaException  {
        PreparedStatement ps = null;
        String sql = "", sqlCE = "";
        int insCE = 0;
        int contbd = 1;

        try {
            sqlCE = "INSERT INTO E_ENT (ENT_MUN,ENT_PRO,ENT_TRA,ENT_COD,ENT_CTR,ENT_EST,ENT_TIPO,ENT_EXP,ENT_DOC) "
                    + "VALUES (?,?,?,?,?,?,?,?,?)";
            for (DefinicionTramitesValueObject dTVO : listaCondEntrada) {
                sql = sqlCE;
                ps = con.prepareStatement(sql);
                String tipo = dTVO.getTipoCondEntrada();
                ps.setInt(contbd++, codMunicipio);
                ps.setString(contbd++, codProcedimiento);
                ps.setInt(contbd++, nuevoCodTramite);
                ps.setInt(contbd++, Integer.parseInt(dTVO.getCodCondEntrada()));
                if (tipo.equals("T")) {
                    int ctr = Integer.parseInt(dTVO.getIdTramiteCondEntrada());
                    int nuevoCodCtr = TramiteBibliotecaImportadoDAO.getInstance().getMapeoTramiteImportados(con, codBiblioteca, codProcedimiento, ctr);
                    ps.setInt(contbd++, nuevoCodCtr);
                } else {
                    ps.setInt(contbd++, 0);
                }
                if (tipo.equals("T") || tipo.equals("D")) {
                    ps.setString(contbd++, dTVO.getEstadoTramiteCondEntrada());
                } else {
                    ps.setString(contbd++, " ");
                }
                ps.setString(contbd++, tipo);
                if (tipo.equals("E")) {
                    ps.setString(contbd++, dTVO.getExpresionCondEntrada());
                } else {
                    ps.setNull(contbd++, Types.VARCHAR);
                }
                if (tipo.equals("D")) {
                    ps.setInt(contbd++, Integer.parseInt(dTVO.getCodDocumentoCondEntrada()));
                } else {
                    ps.setNull(contbd++, Types.INTEGER);
                }
                insCE += ps.executeUpdate();
                ps.close();
            }
            if (log.isDebugEnabled()) {
                log.debug("Query de inserción de condición de entrada: " + sqlCE);
                log.debug("Filas insertadas: " + insCE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error al grabar datos de condiciones de entrada de tramite");
            throw new ImportacionFlujoBibliotecaException(94,ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de BBDD");
            }
        }
        
        return (insCE == listaCondEntrada.size());
    }

public boolean grabarCondsSal(DefinicionTramitesValueObject defTramVO, Connection con, String codProcedimiento, String codBiblioteca, int nuevoCodTramite, int codMunicipio) throws ImportacionFlujoBibliotecaException  {
        PreparedStatement ps = null, ps2 = null;
        String sql = "", sqlSal = "", sqlSML = "", sqlFls = "";
        int insSal = 0, insSalP = 0, insFls = 0;
        int contbd = 1;
        boolean exitoP = false, exitoT = false;
        ArrayList<FlujoSalidaTramiteVO> flujoSalida = null;

        try {
            //1. condicion salida
            sqlSal = "INSERT INTO E_SAL (SAL_MUN,SAL_PRO,SAL_TRA,SAL_TAC,SAL_TAA,SAL_TAN,SAL_OBL,SAL_OBLD) "
                    + "VALUES (?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sqlSal);
            ps.setInt(contbd++, codMunicipio);
            ps.setString(contbd++, codProcedimiento);
            ps.setInt(contbd++, nuevoCodTramite);
            String tipo = defTramVO.getTipoCondicion();
            ps.setString(contbd++, tipo);
            String tipoCF = defTramVO.getTipoFavorableSI();
            if (tipoCF.equals("TramiteSI")) {
                ps.setString(contbd++, "T");
            } else if (tipoCF.equals("FinalizacionSI")) {
                ps.setString(contbd++, "F");
            } else {
                ps.setNull(contbd++, Types.CHAR);
            }
            String tipoCD = defTramVO.getTipoFavorableNO();
            if (tipoCD.equals("TramiteNO")) {
                ps.setString(contbd++, "T");
            } else if (tipoCD.equals("FinalizacionNO")) {
                ps.setString(contbd++, "F");
            } else {
                ps.setNull(contbd++, Types.CHAR);
            }
            String oblig = defTramVO.getObligatorio();
            if (oblig != null) {
                ps.setInt(contbd++, Integer.parseInt(oblig));
            } else {
                ps.setNull(contbd++, Types.INTEGER);
            }
            String obligD = defTramVO.getObligatorioDesf();
            if (obligD != null) {
                ps.setInt(contbd++, Integer.parseInt(obligD));
            } else {
                ps.setNull(contbd++, Types.INTEGER);
            }
            insSal = ps.executeUpdate();
            if (tipo != null && tipo.equals("P")) {
                exitoP = true;
                sqlSML = "INSERT INTO E_SML (SML_TRA,SML_MUN,SML_PRO,SML_CMP,SML_LENG,SML_VALOR) "
                        + "VALUES (?,?,?,?,?,?)";
                contbd = 1;
                ps2 = con.prepareStatement(sqlSML);
                ps2.setInt(contbd++, nuevoCodTramite);
                ps2.setInt(contbd++, codMunicipio);
                ps2.setString(contbd++, codProcedimiento);
                ps2.setString(contbd++, "TXT");
                ps2.setString(contbd++, idiomaDefecto);
                ps2.setString(contbd++, defTramVO.getTexto());
                insSalP = ps2.executeUpdate();
                ps2.close();
                if (log.isDebugEnabled()) {
                    log.debug("Si la condición de salida es de tipo pregunta, query: " + sqlSML);
                }
            }
            //2. flujo de salida
            if (tipo != null && (tipo.equals("T") || tipo.equals("P") || tipo.equals("R"))) {
                exitoT = true;
                flujoSalida = new ArrayList<FlujoSalidaTramiteVO>(defTramVO.getFlujoSalidaTramiteImportacion());
                sqlFls = "INSERT INTO E_FLS (FLS_MUN,FLS_PRO,FLS_TRA,FLS_NUC,FLS_NUS,FLS_CTS) "
                        + "VALUES (?,?,?,?,?,?)";
                for (FlujoSalidaTramiteVO fstVO : flujoSalida) {
                    int cts = Integer.parseInt(fstVO.getCodigoTramiteDestino());
                    int nuevoCodCts = TramiteBibliotecaImportadoDAO.getInstance().getMapeoTramiteImportados(con, codBiblioteca, codProcedimiento, cts);
                    sql = sqlFls;
                    ps2 = con.prepareStatement(sql);
                    contbd = 1;
                    ps2.setInt(contbd++, codMunicipio);
                    ps2.setString(contbd++, codProcedimiento);
                    ps2.setInt(contbd++, nuevoCodTramite);
                    ps2.setInt(contbd++, Integer.parseInt(fstVO.getNumeroCondicionSalida()));
                    ps2.setInt(contbd++, Integer.parseInt(fstVO.getNumeroSecuencia()));
                    ps2.setInt(contbd++, nuevoCodCts);
                    insFls += ps2.executeUpdate();
                    ps2.close();
                }
                if (log.isDebugEnabled()) {
                    log.debug("Si la condicion de salida es de tipo tramite, query: " + sqlFls);
                    log.debug("Filas insertadas para el flujo de salida: " + insFls);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Query de inserción de condición de salida: " + sqlSal);
            }
        } catch (ImportacionFlujoBibliotecaException ex) {
            throw new ImportacionFlujoBibliotecaException(982,ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error al grabar datos de condiciones de salida de tramite");
            throw new ImportacionFlujoBibliotecaException(95,ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de BBDD");
            }
        }
        
        exitoP = !exitoP || (exitoP && insSalP > 0);
        exitoT = !exitoT || (exitoT && insFls > 0 && flujoSalida != null && insFls == flujoSalida.size());
        
        return (insSal > 0 && exitoP && exitoT);
    }

    public boolean grabarCamposSupl(Connection con, String codProcedimiento, int nuevoCodTramite, int codMunicipio,
            ArrayList<DefinicionAgrupacionCamposValueObject> listaAgrupaciones, ArrayList<DefinicionCampoValueObject> listaCampos) throws ImportacionFlujoBibliotecaException  {

        PreparedStatement ps = null, ps2 = null;
        String sql = "", sqlTcaG = "", sqlTca = "";
        int insTcaG = 0, insTca = 0, insCN = 0, insCC = 0;
        int contbd = 1;
        boolean entraExp = false;

        try {
            //1.Agrupaciones de campos
            if (listaAgrupaciones.size() > 0) {
                sqlTcaG = "INSERT INTO E_TCA_GROUP (TCA_ID_GROUP,TCA_DESC_GROUP,TCA_ORDER_GROUP,TCA_PRO,TCA_ACTIVE,TCA_TRA) "
                        + "VALUES (?,?,?,?,?,?)";
                for (DefinicionAgrupacionCamposValueObject dACVO : listaAgrupaciones) {
                    sql = sqlTcaG;
                    ps = con.prepareStatement(sql);
                    ps.setString(contbd++, dACVO.getCodAgrupacion());
                    ps.setString(contbd++, dACVO.getDescAgrupacion());
                    ps.setInt(contbd++, dACVO.getOrdenAgrupacion());
                    ps.setString(contbd++, codProcedimiento);
                    ps.setString(contbd++, dACVO.getAgrupacionActiva());
                    ps.setInt(contbd++, nuevoCodTramite);
                    insTcaG += ps.executeUpdate();
                    ps.close();
                }
                if (log.isDebugEnabled()) {
                    log.debug("Query de inserción de agrupaciones: " + sqlTcaG);
                    log.debug("Filas insertadas: " + insTcaG);
                }
            }

            //2. campos
            if (listaCampos.size() > 0) {
                sqlTca = "INSERT INTO E_TCA (TCA_MUN,TCA_PRO,TCA_TRA,TCA_COD,TCA_DES,TCA_PLT,TCA_TDA,TCA_TAM,TCA_MAS,TCA_OBL,TCA_NOR,TCA_ROT,"
                        + "TCA_VIS,TCA_ACTIVO,TCA_DESPLEGABLE,TCA_OCULTO,TCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,TCA_POS_X,TCA_POS_Y) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                for (DefinicionCampoValueObject dCVO : listaCampos) {
                    sql = sqlTca;
                    ps = con.prepareStatement(sql);
                    contbd = 1;
                    String codTipoDato = dCVO.getCodTipoDato();
                    String codCampo = dCVO.getCodCampo();
                    ps.setInt(contbd++, codMunicipio);
                    ps.setString(contbd++, codProcedimiento);
                    ps.setInt(contbd++, nuevoCodTramite);
                    ps.setString(contbd++, codCampo);
                    ps.setString(contbd++, dCVO.getDescCampo());
                    if (!codTipoDato.equals("6") && !codTipoDato.equals("10")) { //desplegables o desplegables externos
                        ps.setString(contbd++, dCVO.getCodPlantilla());
                    } else {
                        ps.setString(contbd++, codTipoDato);
                    }
                    ps.setString(contbd++, codTipoDato);
                    ps.setString(contbd++, dCVO.getTamano());
                    ps.setString(contbd++, dCVO.getDescMascara());
                    ps.setString(contbd++, dCVO.getObligat());
                    ps.setString(contbd++, dCVO.getOrden());
                    ps.setString(contbd++, dCVO.getRotulo());
                    ps.setString(contbd++, dCVO.getVisible());
                    ps.setString(contbd++, dCVO.getActivo());
                    if (codTipoDato.equals("6") || codTipoDato.equals("10")) { //desplegables o desplegables externos
                        ps.setString(contbd++, dCVO.getCodPlantilla());
                    } else {
                        ps.setNull(contbd++, Types.VARCHAR);
                    }
                    ps.setString(contbd++, dCVO.getOculto());
                    ps.setString(contbd++, dCVO.getBloqueado());
                    ps.setString(contbd++, dCVO.getPlazoFecha());
                    ps.setString(contbd++, dCVO.getCheckPlazoFecha());
                    ps.setString(contbd++, dCVO.getCodAgrupacion());
                    ps.setString(contbd++, dCVO.getPosX());
                    ps.setString(contbd++, dCVO.getPosY());
                    insTca += ps.executeUpdate();

                    if (codTipoDato.equals("1")) {
                        entraExp = true;
                        sql = "INSERT INTO EXPRESION_CAMPO_NUM_TRAM (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_TRAMITE,COD_CAMPO,EXPRESION) "
                                + "VALUES (?,?,?,?,?)";
                        contbd = 1;
                        ps2 = con.prepareStatement(sql);
                        ps2.setInt(contbd++, codMunicipio);
                        ps2.setString(contbd++, codProcedimiento);
                        ps2.setInt(contbd++, nuevoCodTramite);
                        ps2.setString(contbd++, codCampo);
                        ps2.setString(contbd++, dCVO.getValidacion() != null ? dCVO.getValidacion() : " ");
                        insCN += ps2.executeUpdate();
                        ps2.close();
                        if (log.isDebugEnabled()) {
                            log.debug("Si el tipo del campo es numérico, query: " + sql);
                        }
                    } else if (codTipoDato.equals("8") || codTipoDato.equals("9")) {
                        entraExp = true;
                        sql = "INSERT INTO EXPRESION_CAMPO_CAL_TRAM (COD_ORGANIZACION,COD_PROCEDIMIENTO,COD_TRAMITE,COD_CAMPO,TIPO_DATO,EXPRESION) "
                                + "VALUES (?,?,?,?,?,?)";
                        contbd = 1;
                        ps2 = con.prepareStatement(sql);
                        ps2.setInt(contbd++, codMunicipio);
                        ps2.setString(contbd++, codProcedimiento);
                        ps2.setInt(contbd++, nuevoCodTramite);
                        ps2.setString(contbd++, codCampo);
                        ps2.setInt(contbd++, Integer.parseInt(codTipoDato));
                        ps2.setString(contbd++, dCVO.getOperacion() != null ? dCVO.getOperacion() : " ");
                        insCC += ps2.executeUpdate();
                        ps2.close();
                        if (log.isDebugEnabled()) {
                            log.debug("Si el tipo del campo es calculado, query: " + sql);
                        }
                    }
                    ps.close();
                }
                if (log.isDebugEnabled()) {
                    log.debug("Query de inserción de campos suplementarios: " + sqlTca);
                    log.debug("Filas insertadas: " + insTca);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error al grabar datos de campos suplementarios de tramite");
            throw new ImportacionFlujoBibliotecaException(96,ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de BBDD");
            }
        }
        
        boolean exitoTcaG = (insTcaG == listaAgrupaciones.size());
        boolean exitoEntraExp = entraExp && (insCN+insCC) > 0 && (insCN+insCC) <= listaCampos.size();
        boolean exitoTca = (insTca == listaCampos.size() && (!entraExp || exitoEntraExp));
        
        return (exitoTcaG && exitoTca);
    }

    private boolean grabarConfSW(Connection con, String codProcedimiento, int codTramite, int codMunicipio, ArrayList<AvanzarRetrocederSWVO> listaConf) throws ImportacionFlujoBibliotecaException  {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "", sqlSW = "", sqlSelect = "";
        int insSW = 0;
        int codOperacion = -1, condAvanzar = -1, tipoRetroceso = -1;
        String tipoOrigen = "", nombOp = "", nombMod = "";

        try {
            sqlSW = "INSERT INTO DEF_TRA_SW (DEF_TRA_CFO,DEF_TRA_MUN,DEF_TRA_PRO,DEF_COD_TRA,DEF_TRA_AVZ,"
                    + "DEF_TRA_OBL,DEF_TRA_OP,DEF_TRA_ORD,DEF_TRA_TIPO_ORIGEN_OPERACION,DEF_TRA_NOMBRE_OPERACION,DEF_TRA_NOMBRE_MODULO,TIPO_OP_RETROCESO) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            for (AvanzarRetrocederSWVO confSW : listaConf) {
                long codCfo = -1;
                sqlSelect = "SELECT DEF_TRA_CFO FROM DEF_TRA_SW ORDER BY DEF_TRA_CFO DESC";
                ps = con.prepareStatement(sqlSelect);
                rs = ps.executeQuery();
                while(rs.next()){
                    codCfo = rs.getLong("DEF_TRA_CFO");
                    break;
                }
                codCfo++;
                ps.close();
                
                if (confSW.getCodIniciar() != -1) { //opcion = "INICIAR";
                    codOperacion = confSW.getCodIniciar();
                    condAvanzar = 2;
                    tipoOrigen = confSW.getTipoOperacionIniciar();
                    nombOp = confSW.getNombreOperacionIniciar();
                    nombMod = confSW.getNombreModuloIniciar();
                } else if (confSW.getCodAvanzar() != -1) { //opcion = "AVANZAR";
                    codOperacion = confSW.getCodAvanzar();
                    condAvanzar = 1;
                    tipoOrigen = confSW.getTipoOperacionAvanzar();
                    nombOp = confSW.getNombreOperacionAvanzar();
                    nombMod = confSW.getNombreModuloAvanzar();
                } else if (confSW.getCodRetroceder() != -1) { //opcion = "RETROCEDER";
                    codOperacion = confSW.getCodRetroceder();
                    condAvanzar = 0;
                    tipoOrigen = confSW.getTipoOperacionRetroceder();
                    nombOp = confSW.getNombreOperacionRetroceder();
                    nombMod = confSW.getNombreModuloRetroceder();
                    tipoRetroceso = confSW.getTipoRetroceso();
                }

                sql = sqlSW;
                ps = con.prepareStatement(sql);
                int contbd = 1;
                ps.setLong(contbd++, codCfo);
                ps.setInt(contbd++, codMunicipio);
                ps.setString(contbd++, codProcedimiento);
                ps.setInt(contbd++, codTramite);
                ps.setInt(contbd++, condAvanzar);
                ps.setInt(contbd++, 0);
                ps.setInt(contbd++, codOperacion);
                ps.setInt(contbd++, confSW.getNumeroOrden());
                ps.setString(contbd++, tipoOrigen);
                if (nombOp != null && !"".equals(nombOp)) {
                    ps.setString(contbd++, nombOp);
                } else {
                    ps.setNull(contbd++, Types.VARCHAR);
                }

                if (nombMod != null && !"".equals(nombMod)) {
                    ps.setString(contbd++, nombMod);
                } else {
                    ps.setNull(contbd++, Types.VARCHAR);
                }

                ps.setInt(contbd++, tipoRetroceso);

                insSW += ps.executeUpdate();
            }
            if (log.isDebugEnabled()) {
                log.debug("Query de inserción de configuración de SW: " + sqlSW);
                log.debug("Filas insertadas: " + insSW);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error al grabar la configuración de SW");
            throw new ImportacionFlujoBibliotecaException(97,e.getMessage());
        } finally {
            try {
                if(rs != null) rs.close();
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de BBDD");
            }
        }
        
        return (insSW == listaConf.size());
    }
    
    private java.io.InputStream manageDocs(QueryResult resultado) throws SQLException {
        // manejando formato del documento, BLOB, LONG RAW... 
        log.debug("clase del documento : " + resultado.get("PLT_DOC").getClass());
        if (resultado.get("PLT_DOC").getClass().equals(BLOB.class)) {
            BLOB blob = resultado.get("PLT_DOC", BLOB.class);
            log.debug("documento transformado a BLOB");
            return blob.getBinaryStream();
        } else {
            log.debug("documento transformado directamente a InputStream");
            byte [] bytedocs = resultado.get("PLT_DOC",byte[].class);
            return new ByteArrayInputStream(bytedocs);
        }
    }

                
    
}
