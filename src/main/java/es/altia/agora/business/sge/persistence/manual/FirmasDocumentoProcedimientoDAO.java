package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.sge.FirmasDocumentoProcedimientoVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 *
 * @author Tiffany
 */
public class FirmasDocumentoProcedimientoDAO {

    private Logger log = Logger.getLogger(FirmasDocumentoProcedimientoDAO.class);
    private static FirmasDocumentoProcedimientoDAO instance = null;

    public static FirmasDocumentoProcedimientoDAO getInstance() {
        if (instance == null) {
            instance = new FirmasDocumentoProcedimientoDAO();
        }
        return instance;
    }

    public FirmasDocumentoProcedimientoDAO() {
    }

    public ArrayList<FirmasDocumentoProcedimientoVO> getFirmasDocumento(String documento, String municipio,
            String procedimiento, Connection con) throws TechnicalException {

        ArrayList<FirmasDocumentoProcedimientoVO> resultado = new ArrayList<FirmasDocumentoProcedimientoVO>();
        FirmasDocumentoProcedimientoVO firmaVO = new FirmasDocumentoProcedimientoVO();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT e.ID_FIRMA, e.FIRMA_USUARIO,e.FIRMA_ORDEN,e.FIRMA_COD_DOC, e.FIRMA_MUN,e.FIRMA_PROC,e.FIRMA_UOR,"
                + "e.FIRMA_CARGO,e.FIRMA_TRA_SUB, e.FIRMA_FIN_REC, UOR_NOM, CAR_NOM, USU_NOM FROM E_DEF_FIRMA e "
                + "LEFT JOIN E_DOP ON (DOP_MUN=FIRMA_MUN AND DOP_PRO=FIRMA_PROC "
                + "AND DOP_COD=FIRMA_COD_DOC) "
                + "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_USU ON (USU_COD=FIRMA_USUARIO) LEFT JOIN "
                + "A_UOR ON (UOR_COD=FIRMA_UOR) LEFT JOIN A_CAR ON (CAR_COD=FIRMA_CARGO) "
                + "WHERE DOP_COD=? AND DOP_PRO=? AND DOP_MUN=?";
        log.debug("Firmas asociadas a documento: " + sql);
        try {
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, Integer.parseInt(documento));
            ps.setString(i++, procedimiento);
            ps.setInt(i++, Integer.parseInt(municipio));
            rs = ps.executeQuery();

            while (rs.next()) {
                firmaVO = new FirmasDocumentoProcedimientoVO();
                firmaVO.setCargo(rs.getString("FIRMA_CARGO"));
                firmaVO.setNomCargo(rs.getString("CAR_NOM"));
                firmaVO.setCodDocumento(documento);
                firmaVO.setCodigo(rs.getString("ID_FIRMA"));
                firmaVO.setMunicipio(municipio);
                firmaVO.setOrden(rs.getString("FIRMA_ORDEN"));
                firmaVO.setProcedimiento(rs.getString("FIRMA_PROC"));
                firmaVO.setUor(rs.getString("FIRMA_UOR"));
                firmaVO.setNomUor(rs.getString("UOR_NOM"));
                firmaVO.setUsuario(rs.getString("FIRMA_USUARIO"));
                firmaVO.setNomUsuario(rs.getString("USU_NOM"));
				firmaVO.setTramitar(rs.getString("FIRMA_TRA_SUB"));
                firmaVO.setFinalizaRechazo(rs.getString("FIRMA_FIN_REC"));
                resultado.add(firmaVO);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            return resultado;
        }
    }


    
public ArrayList<FirmasDocumentoProcedimientoVO> getTodasFirmasDocumentoPorProcedimiento(String municipio,
            String procedimiento, Connection con) throws TechnicalException {

        log.debug(" getTodasFirmasDocumentoPorProcedimiento");
        ArrayList<FirmasDocumentoProcedimientoVO> resultado = new ArrayList<FirmasDocumentoProcedimientoVO>();
        FirmasDocumentoProcedimientoVO firmaVO = new FirmasDocumentoProcedimientoVO();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT  e.ID_FIRMA,e.FIRMA_USUARIO,e.FIRMA_ORDEN,e.FIRMA_COD_DOC, e.FIRMA_MUN,e.FIRMA_PROC,e.FIRMA_UOR,"
                + "e.FIRMA_CARGO,e.FIRMA_TRA_SUB, e.FIRMA_FIN_REC, UOR_NOM, CAR_NOM, USU_NOM "
                + "FROM E_DEF_FIRMA E LEFT JOIN E_DOP ON (DOP_MUN=FIRMA_MUN AND DOP_PRO=FIRMA_PROC "
                + "AND DOP_COD=FIRMA_COD_DOC) "
                + "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_USU ON (USU_COD=FIRMA_USUARIO) LEFT JOIN "
                + "A_UOR ON (UOR_COD=FIRMA_UOR) LEFT JOIN A_CAR ON (CAR_COD=FIRMA_CARGO) "
                + "WHERE  DOP_PRO=? AND DOP_MUN=?";
        log.debug("Firmas asociadas a todos los documentos del procedimiento: " + sql);
        try {
            ps = con.prepareStatement(sql);
            int i = 1;

            ps.setString(i++, procedimiento);
            ps.setInt(i++, Integer.parseInt(municipio));
            rs = ps.executeQuery();

            while (rs.next()) {
                firmaVO = new FirmasDocumentoProcedimientoVO();
                firmaVO.setCargo(rs.getString("FIRMA_CARGO")==null?"":rs.getString("FIRMA_CARGO"));
                firmaVO.setNomCargo(rs.getString("CAR_NOM"));
                firmaVO.setCodDocumento(rs.getString("FIRMA_COD_DOC"));
                firmaVO.setCodigo(rs.getString("ID_FIRMA"));
                firmaVO.setMunicipio(municipio);
                firmaVO.setOrden(rs.getString("FIRMA_ORDEN")==null?"":rs.getString("FIRMA_ORDEN"));
                firmaVO.setProcedimiento(rs.getString("FIRMA_PROC"));
                firmaVO.setUor(rs.getString("FIRMA_UOR")==null?"":rs.getString("FIRMA_UOR"));
                firmaVO.setNomUor(rs.getString("UOR_NOM"));
                firmaVO.setUsuario(rs.getString("FIRMA_USUARIO")==null?"":rs.getString("FIRMA_USUARIO"));
                firmaVO.setNomUsuario(rs.getString("USU_NOM"));
				firmaVO.setTramitar(rs.getString("FIRMA_TRA_SUB"));
                firmaVO.setFinalizaRechazo(rs.getString("FIRMA_FIN_REC"));
                resultado.add(firmaVO);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            return resultado;
        }
    }

    
public void guardarListaFirmas(ArrayList<FirmasDocumentoProcedimientoVO> listaFirmas, String codMunicipio, String codProcedimiento, String codDocumento, Connection con) throws TechnicalException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlDelete = "DELETE FROM E_DEF_FIRMA WHERE FIRMA_MUN=? AND FIRMA_PROC=? AND FIRMA_COD_DOC=?";
        String sqlInsert = "INSERT INTO E_DEF_FIRMA(ID_FIRMA,FIRMA_UOR,FIRMA_CARGO,FIRMA_USUARIO,FIRMA_ORDEN,FIRMA_COD_DOC,FIRMA_MUN,FIRMA_PROC, FIRMA_FIN_REC,FIRMA_TRA_SUB) VALUES (?,?,?,?,?,?,?,?,?,?)";
        String sqlMax = "SELECT MAX(ID_FIRMA)+1 AS MAXIMO FROM E_DEF_FIRMA";
        try {
            ps = con.prepareStatement(sqlDelete);
            int i = 1;
            ps.setInt(i++, Integer.parseInt(codMunicipio));
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, Integer.parseInt(codDocumento));
            log.debug(sqlDelete);
            ps.executeUpdate();
            SigpGeneralOperations.closeStatement(ps);

            ps = con.prepareStatement(sqlMax);
            rs = ps.executeQuery();
            int codigo = 1;
            
            if (rs.next()) {
                codigo = rs.getInt("MAXIMO");
            }
            
            for (Iterator it = listaFirmas.iterator(); it.hasNext();) {
                FirmasDocumentoProcedimientoVO firmaVO = (FirmasDocumentoProcedimientoVO) it.next();
                ps = con.prepareStatement(sqlInsert);
                int j = 1;
                ps.setInt(j++, codigo++);
                
                if ("null".equalsIgnoreCase(firmaVO.getUor()) || "".equals(firmaVO.getUor()) || firmaVO.getUor() == null) {
                    ps.setNull(j++, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(j++, Integer.parseInt(firmaVO.getUor()));
                }                
                if ("null".equals(firmaVO.getCargo()) || "".equals(firmaVO.getCargo()) ||  firmaVO.getCargo() == null) {
                    ps.setNull(j++, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(j++, Integer.parseInt(firmaVO.getCargo()));
                }
                if ("null".equals(firmaVO.getUsuario()) || "".equals(firmaVO.getUsuario()) ||  firmaVO.getUsuario() == null) {
                    ps.setNull(j++, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(j++, Integer.parseInt(firmaVO.getUsuario()));
                }
                ps.setInt(j++, Integer.parseInt(firmaVO.getOrden()));                
                ps.setInt(j++, Integer.parseInt(codDocumento));
                ps.setInt(j++, Integer.parseInt(codMunicipio));
                ps.setString(j++, codProcedimiento);
                if (firmaVO.getFinalizaRechazo() == null || firmaVO.getFinalizaRechazo().equals("false")|| 
                    "".equals(firmaVO.getFinalizaRechazo()) || "null".equals(firmaVO.getFinalizaRechazo())) {
                   ps.setString(j++, "0");
                }
                else{
                    ps.setString(j++, "1");
                }
                if ("1".equals(firmaVO.getTramitar())) {
                   ps.setString(j++, "1");
                }else{
                    ps.setString(j++, "0");
                }
                log.debug(sqlInsert);
                log.debug("UOR:\t"+firmaVO.getUor());
                log.debug("Cargo:\t"+firmaVO.getCargo());
                log.debug("Usuario:\t"+firmaVO.getUsuario());
                log.debug("Orden:\t"+firmaVO.getOrden());
                log.debug("Cod Documento:\t"+codDocumento);
                log.debug("Cod Municipio:\t"+codMunicipio);
                log.debug("Cod Procedimiento:\t"+codProcedimiento);
                log.debug("FinalizaRechazo:\t"+firmaVO.getFinalizaRechazo());
				log.debug("Tramitar:\t"+firmaVO.getTramitar());
                log.debug("\n");
                ps.executeUpdate();
                SigpGeneralOperations.closeStatement(ps);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        } finally {
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.closeResultSet(rs);
        }
    }
    
    //Devuelve null en caso de que la firma que le pasemos sea la ultima del circuito
    public FirmasDocumentoProcedimientoVO getFirmaSiguiente(int codFirma, int codDocumento, Connection con) throws TechnicalException {
        FirmasDocumentoProcedimientoVO firmaSiguiente = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
            String sql = "SELECT DOC_FIRMA_USUARIO, DOC_FIRMA_UOR, DOC_FIRMA_CARGO , PRESENTADO_MUN, UOR_NOM, CAR_NOM FROM E_DOCS_FIRMAS "                
            + "LEFT JOIN E_DOCS_PRESENTADOS ON (ID_DOC_PRESENTADO=PRESENTADO_COD) "
            + "LEFT JOIN A_UOR ON (UOR_COD=DOC_FIRMA_UOR) "
             + "LEFT JOIN A_CAR ON (CAR_COD=DOC_FIRMA_CARGO)"
            + "WHERE ID_DOC_PRESENTADO=? AND DOC_FIRMA_ORDEN= (SELECT DOC_FIRMA_ORDEN FROM E_DOCS_FIRMAS WHERE ID_DOC_FIRMA = ?)+1";                    
        try {
            ps = con.prepareStatement(sql);            
            int i=1;
            ps.setInt(i++, codDocumento);
            ps.setInt(i++, codFirma);
            log.debug(sql);
            rs = ps.executeQuery();

            if (rs.next()) {//No devolvera resultados si se llega al final del circuito
                firmaSiguiente = new FirmasDocumentoProcedimientoVO();                
                firmaSiguiente.setUsuario(rs.getString("DOC_FIRMA_USUARIO"));
                firmaSiguiente.setProcedimiento(rs.getString("DOC_FIRMA_UOR"));
                firmaSiguiente.setCargo(rs.getString("DOC_FIRMA_CARGO"));
                firmaSiguiente.setNomUor(rs.getString("UOR_NOM"));
                firmaSiguiente.setNomCargo(rs.getString("CAR_NOM"));             
                firmaSiguiente.setMunicipio(rs.getString("PRESENTADO_MUN"));
            } 
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            return firmaSiguiente;
        }
    }

    public boolean tieneFirmasConfigurables(String documento, String municipio,
            String procedimiento, Connection con) throws TechnicalException {
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean firmasConfigurables = false;
        
        String sql = "SELECT * FROM E_DEF_FIRMA WHERE FIRMA_UOR=-888 AND FIRMA_MUN=? "
                + "AND FIRMA_COD_DOC=? AND FIRMA_PROC=?";
        
        try {
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, Integer.parseInt(municipio));
            ps.setInt(i++, Integer.parseInt(documento));
            ps.setString(i++, procedimiento);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                firmasConfigurables=true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            return firmasConfigurables;
        }
    }
    
    public boolean permiteSubsanacion (int documento, int municipio, String procedimiento,
            int docPresentado, int idNumFirma, Connection con) throws TechnicalException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean tramitar = false;
        log.debug(FirmasDocumentoProcedimientoDAO.class.getName() + "--> permiteSubsanacion");
        String sql = "SELECT FIRMA_TRA_SUB FROM E_DEF_FIRMA F INNER JOIN E_DOCS_FIRMAS DF "
                + "ON (F.FIRMA_ORDEN = DF.DOC_FIRMA_ORDEN AND DF.ID_DOC_PRESENTADO = ? "
                + "AND DF.ID_DOC_FIRMA = ? AND F.FIRMA_MUN = ? AND F.FIRMA_PROC = ? AND F.FIRMA_COD_DOC = ?)";
        log.debug("permiteSubsanacion SQL: " + sql);
        try {
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, docPresentado);
            ps.setInt(i++, idNumFirma);
            ps.setInt(i++, municipio);
            ps.setString(i++, procedimiento);
            ps.setInt(i++, documento);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("FIRMA_TRA_SUB") == 1) {
                    tramitar = true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
        log.debug(FirmasDocumentoProcedimientoDAO.class.getName() + "<-- permiteSubsanacion");
        return tramitar;
    }
}
