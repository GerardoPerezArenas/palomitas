package es.altia.agora.business.portafirmas.persistence.manual;

import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoProcedimientoFirmaVO;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.log4j.Logger;


public class DocsProcedimientoPortafirmasManager {

    private static DocsProcedimientoPortafirmasManager instance = null;
    private Logger log = Logger.getLogger(DocsProcedimientoPortafirmasManager.class);
    
    
    private DocsProcedimientoPortafirmasManager() {
    }

    public static DocsProcedimientoPortafirmasManager getInstance() {
        if (instance == null) {
            instance = new DocsProcedimientoPortafirmasManager();
        }

        return instance;
    }

    public ArrayList<DocumentoProcedimientoFirmaVO> getDocumentosProcedimientoPortafirmas(String nifUsuarioDelegado, String nifUsuarioQueDelega, String estado, int organizacion, String[] params)
            throws TechnicalException {
        Connection con = null;
        ArrayList<DocumentoProcedimientoFirmaVO> documentos = null;
        try {
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            documentos = DocsProcedimientoPortafirmasDAO.getInstance().
                    getDocumentoExpedientePortafirmas(nifUsuarioDelegado, nifUsuarioQueDelega, estado, organizacion, con);

        } catch (BDException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return documentos;
    }
    
    public ArrayList<DocumentoProcedimientoFirmaVO> getDocumentoFirma(String codigoFirma, String[] params)
            throws TechnicalException {
        Connection con = null;
        ArrayList<DocumentoProcedimientoFirmaVO> documentos = null;
        try {
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            documentos = DocsProcedimientoPortafirmasDAO.getInstance().
                    getDocumentoFirma(codigoFirma, con);

        } catch (BDException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return documentos;
    }

    /**
     * Recupera el usuario en el que haya podido delegar un determinado usuario la firma de los documentos del portafrimas
     * @param codigoUsuario: Código del usuario que concede la delegación de firma
     * @param params: Parámetros de conexión a la base d edatos
     * @return Hashtable<String,String>
     */
    public Hashtable<String, String> getUsuarioDelegado(int codigoUsuario, String[] params) throws TechnicalException {
        Hashtable<String, String> salida = null;
        Connection con = null;
        try {
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            salida = DocsProcedimientoPortafirmasDAO.getInstance().getUsuarioDelegado(codigoUsuario, con);

        } catch (BDException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return salida;
    }

    public boolean guardarFirma(DocumentoProcedimientoFirmaVO doc, String[] params) {
        Connection con = null;
        boolean exito = false;
        try {
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = DocsProcedimientoPortafirmasDAO.getInstance().guardarFirma(doc, con);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return exito;
    }
        
    public boolean rechazarDocumento(DocumentoProcedimientoFirmaVO doc, String[] params) {
        Connection con = null;
        boolean exito = false;
        try {
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = DocsProcedimientoPortafirmasDAO.getInstance().rechazarDocumento(doc, con);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return exito;
    }

    public DocumentoProcedimientoFirmaVO getDocumentoExpediente (DocumentoProcedimientoFirmaVO doc, String[] params) {
        Connection con = null;
        DocumentoProcedimientoFirmaVO documento = new DocumentoProcedimientoFirmaVO();
        try {
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            documento = DocsProcedimientoPortafirmasDAO.getInstance().getDocumentoExpediente(doc, con);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return documento;
    }

    public boolean subsanarDocumento(DocumentoProcedimientoFirmaVO doc, String[] params) {
        Connection con = null;
        boolean exito = false;
        try {
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = DocsProcedimientoPortafirmasDAO.getInstance().subsanarDocumento(doc, con);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return exito;
    }
    
    
    
    
    public String getNombreDocumentoExpediente(int codDocumentoPresentado,String numExpediente,String codProcedimiento, String[] params) throws TechnicalException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        String salida = null;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            salida = DocsProcedimientoPortafirmasDAO.getInstance().getNombreDocumentoExpediente(codDocumentoPresentado, numExpediente, codProcedimiento, con);
                    
        }catch(BDException e){
            log.error("Error al obtener conexión a la BBDD: " + e.getMessage());
            throw new TechnicalException("Error al obtener conexión a la BBDD: " + e.getMessage(),e);
        }
        catch(TechnicalException e){
            e.printStackTrace();
            throw e;
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                e.printStackTrace();
            }
        }        
        return salida;
    }
    
    
   /**
     * Permite almacenar los datos de la firma de un documento de inicio de expedinete, pero sin almacenar el binario
     * de la firma, porque esta irá a un gestor documental
     * @param doc: Objeto de tipo DocumentoProcedimientoFirmaVO
     * @param con: Conexión a la BBDD
     * @return True si se ha almacenado la firma y false en caso contrario
     * @throws TechnicalException 
     */
     public boolean guardarDatosFirmaSinBinario(DocumentoProcedimientoFirmaVO doc, String[] params) throws TechnicalException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        boolean exito = false;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            exito = DocsProcedimientoPortafirmasDAO.getInstance().guardarDatosFirmaSinBinario(doc, con);
                    
        }catch(BDException e){
            log.error("Error al obtener conexión a la BBDD: " + e.getMessage());
            throw new TechnicalException("Error al obtener conexión a la BBDD: " + e.getMessage(),e);
        }
        catch(TechnicalException e){
            e.printStackTrace();
            throw e;
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                e.printStackTrace();
            }
        }        
        return exito;         
     }
    
    
    
}
