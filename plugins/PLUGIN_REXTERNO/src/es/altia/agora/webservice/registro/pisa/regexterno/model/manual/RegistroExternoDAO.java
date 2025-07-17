package es.altia.agora.webservice.registro.pisa.regexterno.model.persistence.manual;

import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.util.combo.ComboCVO;
import es.altia.agora.interfaces.user.web.util.combo.ElementoComboCVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author ivan.perez
 */
public class RegistroExternoDAO {
    
    protected static Log m_Log = LogFactory.getLog(RegistroExternoDAO.class.getName());
    private static ResourceBundle m_ct =
            ResourceBundle.getBundle("es.altia.agora.webservice.registro.pisa.cliente.configuracion.configuracion");    
    private static ResourceBundle m_TipoDocs =
            ResourceBundle.getBundle("es.altia.agora.webservice.registro.pisa.cliente.constantes.TipoDocumento");
    private static RegistroExternoDAO instance = null;

    protected RegistroExternoDAO() {}

    public static RegistroExternoDAO getInstance() {
        if (instance == null) {
            synchronized (RegistroExternoDAO.class) {
                if (instance == null) instance = new RegistroExternoDAO();
            }
        }
        return instance;
    }
    
    public String traducirUnidad(Connection connection, int codUnidad) {
        
        PreparedStatement ps = null;
        String sql;
        ResultSet rs = null;
        String codAccede = "";
        try {       
            sql = "SELECT UOR_COD_ACCEDE, UOR_COD FROM A_UOR WHERE UOR_COD = " + codUnidad + " " +
            "AND UOR_COD_ACCEDE IS NOT NULL";

            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) codAccede = rs.getString(1);
             
             
                        
        } catch (Exception e) {
            e.printStackTrace();
            codAccede = null;
        } finally {
            try {
                if (connection != null) {
                    rs.close();
                    ps.close();                    
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            }

        }    
        return codAccede;            
    }
    
    public String traducirTipoDocumento(String tipoDocumento) {
        return m_TipoDocs.getString(tipoDocumento);
    }
    
    public void guardarReferenciaExpediente(Connection con, RegistroValueObject registroVO) throws SQLException {
        String sqlInsertAnotacion = "INSERT INTO E_EXREXT (EXREXT_UOR, EXREXT_TIP, EXREXT_EJR, EXREXT_NRE, EXREXT_MUN, " +
                "EXREXT_NUM, EXREXT_ORI, EXREXT_TOP, EXREXT_SER, EXREXT_PRO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps;

        ps = con.prepareStatement(sqlInsertAnotacion);
        int i = 1;
        ps.setInt(i++, registroVO.getUnidadOrgan());
        ps.setString(i++, registroVO.getTipoReg());
        ps.setInt(i++, registroVO.getAnoReg());
        ps.setLong(i++, registroVO.getNumReg());
        ps.setInt(i++, Integer.parseInt(registroVO.getMunInteresado()));
        ps.setString(i++, registroVO.getNumExpediente());
        ps.setInt(i++, 1);
        ps.setString(i++, "1");
        ps.setString(i++, registroVO.getIdServicioOrigen());
        ps.setString(i, registroVO.getCodProcedimiento());

        int insertedRows = ps.executeUpdate();

        if (insertedRows != 1) throw new SQLException("ERROR EN EL VALOR DEVUELTO POR LA CONSULTA A BBDD " +
                "(Valor = " + insertedRows);        
    }
    
    
    public String[] obtenerOrganizacionEntidad(int organizacion) {
        String[] organizacionEntidad = new String[2];
        organizacionEntidad[0] = m_ct.getString("Pisa." + organizacion + ".organizacion");
        organizacionEntidad[1] = m_ct.getString("Pisa." + organizacion + ".entidad");
        return organizacionEntidad;
    }
        
    public ComboCVO getComboUnidadesAccede(Connection con) {
        ComboCVO combo = new ComboCVO();
        Vector<ElementoComboCVO> listaElementosCombo = new Vector<ElementoComboCVO>();
        Vector uorsAccede = getUORsAccede(con);  
        int i =0;
        for(Iterator it = uorsAccede.iterator();it.hasNext();) {
            GeneralValueObject gVO = (GeneralValueObject)it.next();
            ElementoComboCVO elementoCombo = new ElementoComboCVO();
            String codigoElemento = (String)gVO.getAtributo("codUORVis");
            String descripcionElemento = (String)gVO.getAtributo("nombreUOR");
            String codigoInternoElemento = (String)gVO.getAtributo("codUORInt");
            elementoCombo.setCodigoElemento(codigoElemento);
            elementoCombo.setDescripcionElemento(descripcionElemento);
            elementoCombo.setCodigoInternoElemento(codigoInternoElemento);
            listaElementosCombo.addElement(elementoCombo);            
            i++;
        }        
        combo.setElementosCombo(listaElementosCombo);
        return combo;        
    }
    /**
     * @param con: conexión    
     * @return Vector con las uors que tienen su correspondiente valor en Accede
     */        
    private Vector getUORsAccede(Connection con) {

        PreparedStatement ps = null;
        String sql;
        ResultSet rs = null;
        Vector lista = new Vector();

        try {
            sql = "SELECT UOR_COD_VIS,UOR_COD,UOR_NOM  FROM A_UOR WHERE UOR_COD_ACCEDE IS NOT NULL";


            if (m_Log.isDebugEnabled()) m_Log.debug("RegistroExternoDAO : getUORsAccede --> " + sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                GeneralValueObject uor = new GeneralValueObject();
                String codUORVis = rs.getString(1);
                String codUORInt = rs.getString(2);
                String nombreUOR = rs.getString(3);
                
                uor.setAtributo("codUORVis", codUORVis);
                uor.setAtributo("codUORInt", codUORInt);
                uor.setAtributo("nombreUOR", nombreUOR);
                lista.addElement(uor);
            }

            if (m_Log.isDebugEnabled()) m_Log.debug("RegistroExternoDAO : getUORsAccede");

        } catch (Exception e) {
            e.printStackTrace();
            lista = null;
        } finally {
            try {
                if (con != null) {
                    rs.close();
                    ps.close();                    
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            }

        }
        return lista;
    }
    
}
