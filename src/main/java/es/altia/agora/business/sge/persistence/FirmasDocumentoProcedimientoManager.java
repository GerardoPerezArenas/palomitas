package es.altia.agora.business.sge.persistence;

import es.altia.agora.business.sge.FirmasDocumentoProcedimientoVO;
import es.altia.agora.business.sge.persistence.manual.FirmasDocumentoProcedimientoDAO;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Tiffany
 */
public class FirmasDocumentoProcedimientoManager {

    private static FirmasDocumentoProcedimientoManager instance = null;
    private Logger log = Logger.getLogger(CamposListadoPendientesProcedimientoManager.class);

    public static FirmasDocumentoProcedimientoManager getInstance() {
        if (instance == null) {
            instance = new FirmasDocumentoProcedimientoManager();
        }
        return instance;
    }

    /**
     * Devuelve la lista de firmas asociadas a un determinado documento.
     * @param codDocumento Código interno de documento en E_DOP
     * @param municipio Municipio
     * @param procedimiento Cñodigo de procedimiendo
     * @return  Lista de firmas del documento pasdo como parámetro
     */
    public ArrayList<FirmasDocumentoProcedimientoVO> getFirmasDocumento(String codDocumento, String municipio,
            String procedimiento, String[] params) throws TechnicalException {

        ArrayList<FirmasDocumentoProcedimientoVO> resultado = new ArrayList<FirmasDocumentoProcedimientoVO>();
        Connection con = null;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);

        try {
            log.debug("getFirmasDocumento ========>");
            con = adapt.getConnection();
            resultado = FirmasDocumentoProcedimientoDAO.getInstance().
                    getFirmasDocumento(codDocumento, municipio, procedimiento, con);
            log.debug(" El número de campos disponibles es: " + resultado.size());
            log.debug("<======== getFirmasDocumento ");
        } catch (BDException ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(FirmasDocumentoProcedimientoManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            SigpGeneralOperations.devolverConexion(adapt, con);
            return resultado;
        }
    }

     /**
     * Devuelve la lista de firmas asociadas a un determinado procedimiento.
     * @param codDocumento Código interno de documento en E_DOP
     * @param municipio Municipio
     * @param procedimiento Cñodigo de procedimiendo
     * @return  Lista de firmas del documento pasdo como parámetro
     */
    public ArrayList<FirmasDocumentoProcedimientoVO> getTodasFirmasDocumentoPorProcedimiento(String municipio,
            String procedimiento, String[] params) throws TechnicalException {

        ArrayList<FirmasDocumentoProcedimientoVO> resultado = new ArrayList<FirmasDocumentoProcedimientoVO>();
        Connection con = null;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);

        try {
            log.debug("getFirmasDocumento ========>");
            con = adapt.getConnection();
            resultado = FirmasDocumentoProcedimientoDAO.getInstance().
                    getTodasFirmasDocumentoPorProcedimiento(municipio, procedimiento, con);
            log.debug(" El número de campos disponibles es: " + resultado.size());
            log.debug("<======== getFirmasDocumento ");
        } catch (BDException ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(FirmasDocumentoProcedimientoManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            SigpGeneralOperations.devolverConexion(adapt, con);
            return resultado;
        }
    }

    public void guardarListaFirmas(ArrayList<FirmasDocumentoProcedimientoVO> listaFirmas, String codMunicipio, String codProcedimiento, String codDocumento, String[] params) throws TechnicalException {
        Connection con = null;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);

        try {
            log.debug("guardarListaFirmas ========>");
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            FirmasDocumentoProcedimientoDAO.getInstance().guardarListaFirmas(listaFirmas,codMunicipio, codProcedimiento, codDocumento, con);
            SigpGeneralOperations.commit(adapt, con);
            log.debug("<======== guardarListaFirmas ");
        } catch (BDException ex) {
            SigpGeneralOperations.rollBack(adapt, con);
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(FirmasDocumentoProcedimientoManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new TechnicalException("Error de Base de datos en guardarListaFirmas", ex);
        } finally {
            SigpGeneralOperations.devolverConexion(adapt, con);            
        }
    }

    public FirmasDocumentoProcedimientoVO getFirmaSiguiente(int codFirma,int codDocumento, String[] params) throws TechnicalException {
        Connection con = null;
        FirmasDocumentoProcedimientoVO firma = new FirmasDocumentoProcedimientoVO();
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);

        try {
            log.debug("getFirmaSiguiente ========>");
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            firma = FirmasDocumentoProcedimientoDAO.getInstance().getFirmaSiguiente(codFirma,codDocumento, con);
            SigpGeneralOperations.commit(adapt, con);
            log.debug("<======== getFirmaSiguiente ");
        } catch (BDException ex) {
            SigpGeneralOperations.rollBack(adapt, con);
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(FirmasDocumentoProcedimientoManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new TechnicalException("Error de Base de datos en getFirmaSiguiente", ex);
        } finally {
            SigpGeneralOperations.devolverConexion(adapt, con);
            return firma;
        }
    }

    public boolean tieneFirmasConfigurables(String codDocumento, String municipio,
            String procedimiento, String[] params) throws TechnicalException{
        Connection con = null;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        boolean existenFirmasConfigurables = false;

        try {
            log.debug("getFirmaSiguiente ========>");
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            existenFirmasConfigurables = FirmasDocumentoProcedimientoDAO.getInstance().
                    tieneFirmasConfigurables(codDocumento, municipio, procedimiento, con);
            SigpGeneralOperations.commit(adapt, con);
            log.debug("<======== tieneFirmasConfigurables ");
        } catch (BDException ex) {
            SigpGeneralOperations.rollBack(adapt, con);
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(FirmasDocumentoProcedimientoManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new TechnicalException("Error de Base de datos en getFirmaSiguiente", ex);
        } finally {
            SigpGeneralOperations.devolverConexion(adapt, con);
            return existenFirmasConfigurables;
        }
    }

    public boolean permiteSubsanacion (int documento, int municipio,
            String procedimiento, int docPresentado, int idNumFirma, String[] params) 
            throws TechnicalException {
        Connection con = null;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        boolean tramitar = false;
        try {
            log.debug(FirmasDocumentoProcedimientoManager.class.getName() + " --> permiteSubsanacion");
            con = adapt.getConnection();
            tramitar = FirmasDocumentoProcedimientoDAO.getInstance().
                    permiteSubsanacion(documento, municipio, procedimiento, docPresentado, idNumFirma, con);
            log.debug(FirmasDocumentoProcedimientoManager.class.getName() + " <-- permiteSubsanacion: " + tramitar);
        } catch (BDException ex) {
            SigpGeneralOperations.rollBack(adapt, con);
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(FirmasDocumentoProcedimientoManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new TechnicalException("Error de Base de datos en permiteSubsanacion", ex);
        } finally {
            SigpGeneralOperations.devolverConexion(adapt, con);
            return tramitar;
        }
    }
}
