/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.sge.DefinicionTramitesImportacionValueObject;
import es.altia.common.exception.ImportacionFlujoBibliotecaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author MilaNP
 */
public class TramiteBibliotecaImportadoDAO {
    private static TramiteBibliotecaImportadoDAO instance = null;
    private Logger log = Logger.getLogger(TramiteBibliotecaImportadoDAO.class);

    public TramiteBibliotecaImportadoDAO() {
    }

    public static TramiteBibliotecaImportadoDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        synchronized (TramiteBibliotecaImportadoDAO.class) {
            if (instance == null) {
                instance = new TramiteBibliotecaImportadoDAO();
            }

        }
        return instance;
    }
    
    public boolean grabarImportacionTramiteBiblioteca(Connection con, int codTramiteOrigen,String codProcedimientoOrigen,int codTramiteDestino, String codProcedimientoDestino) throws ImportacionFlujoBibliotecaException{
        PreparedStatement ps = null;
        String sql = "";
        int ins = 0;
            
        try {
            sql = "INSERT INTO TRAMITE_IMPORTADO_BIBLIOTECA (COD_TRAMITE_DESTINO,COD_PROCEDIMIENTO_DESTINO,COD_TRAMITE_BIBLIOTECA,COD_PROCEDIMIENTO_BIBLIOTECA) "+
                    "VALUES (?,?,?,?)";
            
            ps = con.prepareStatement(sql);
            int contbd = 1;
            ps.setInt(contbd++, codTramiteDestino);
            ps.setString(contbd++, codProcedimientoDestino);
            ps.setInt(contbd++, codTramiteOrigen);
            ps.setString(contbd++, codProcedimientoOrigen);
            
            ins = ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error al grabar mapeo de importacion");
            throw new ImportacionFlujoBibliotecaException(981,ex.getMessage());
        } finally {
            try{
                if(ps != null) ps.close();
            } catch(SQLException sqle){
                log.error("Error al liberar los recursos de ls BBDD");
            }
        }
        return ins > 0;
    }
    
    public int getMapeoTramiteImportados(Connection con, String codProcOrigen, String codProcDest, int codTramOrigen) throws ImportacionFlujoBibliotecaException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        int codTramiteDest = -1;
            
        try {
            sql = "SELECT COD_TRAMITE_DESTINO FROM TRAMITE_IMPORTADO_BIBLIOTECA "+
                    "WHERE COD_PROCEDIMIENTO_DESTINO=? AND COD_PROCEDIMIENTO_BIBLIOTECA=? AND COD_TRAMITE_BIBLIOTECA=?";
            
            ps = con.prepareStatement(sql);
            int contbd = 1;
            ps.setString(contbd++, codProcDest);
            ps.setString(contbd++, codProcOrigen);
            ps.setInt(contbd++, codTramOrigen);
            
            rs = ps.executeQuery();
            while(rs.next()){
                codTramiteDest = rs.getInt("COD_TRAMITE_DESTINO");
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("Error al recupera el mapeo de código de destino a partir del código de origen.");
            throw new ImportacionFlujoBibliotecaException(982,ex.getMessage());
        }
        
        
        return codTramiteDest;
    }
}
