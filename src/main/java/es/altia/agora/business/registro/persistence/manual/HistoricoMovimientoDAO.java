package es.altia.agora.business.registro.persistence.manual;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.business.registro.HistoricoMovimientoValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.Fecha;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Clob;
import java.util.Date;

public class HistoricoMovimientoDAO {
    
    //Para informacion de logs.
    protected static Log m_Log =
          LogFactory.getLog(HistoricoMovimientoDAO.class.getName());
    //La instancia de esta clase
    private static HistoricoMovimientoDAO instance = new HistoricoMovimientoDAO();

    protected void HistoricoMovimientoDAO() {
    }
    
    public static HistoricoMovimientoDAO getInstance() {
        return instance;
    }

    /**
     * Inserta en la tabla de movimientos un movimiento, usando la fecha y hora 
     * actuales del servidor como las del movimiento.
     * @param vo HistoricoMovimientoValueObject con los datos a insertar.
     * @param con Conexión a la BD con transacción abierta.
     * @param params Parámetros de conexión a la BD.
     * @throws java.sql.SQLException
     * @throws es.altia.common.exception.TechnicalException
     */
    public void insertarMovimientoHistorico(HistoricoMovimientoValueObject vo, Connection con, String[] params) 
        throws TechnicalException{
                
        AdaptadorSQLBD abd = null;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        m_Log.debug("HistoricoMovimientoDAO->insertarMovimientoHistorico");

        try {
            abd = new AdaptadorSQLBD(params);
            String sql;

            
            // Construimos cadena con fecha actual, incluidos segundos
            Fecha fecha = new Fecha();
            Date date = new Date();
            String fechaMovimiento = fecha.construirFechaCompleta(date);
            if (date.getSeconds() < 10) {
                fechaMovimiento += ":0" + date.getSeconds();
            } else {
                fechaMovimiento += ":" + date.getSeconds();
            }
            
             int sqlIncremento= 0;
             if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                 
                 sql="select SEQ_R_HISTORICO.nextval as COD from dual";
                 st = con.createStatement();
                    rs = st.executeQuery(sql);                   
                    if (rs.next()) {
                        sqlIncremento = rs.getInt("COD");
                    }
             }else
             {
                    sql = "SELECT MAX(CODIGO) AS COD FROM R_HISTORICO";
                    m_Log.debug(sql);
                    st = con.createStatement();
                    rs = st.executeQuery(sql);
                    int codigo = 0;
                    if (rs.next()) {
                        codigo = rs.getInt("COD");
                        m_Log.debug("Codigo recuperado de R_HISTORICO: " + codigo);
                    } else {
                        throw new SQLException("No se ha podido recuperar el siguiente codigo de movimiento");
                    }
                    codigo++;
                    sqlIncremento=codigo;
                 
             }
            sql = "INSERT INTO R_HISTORICO(CODIGO, USUARIO, FECHA, TIPOENTIDAD, " +
                    "CODENTIDAD, TIPOMOV, DETALLES)" + " VALUES ("+sqlIncremento+"," + 
                   
                    vo.getCodigoUsuario() + ", " +
                    // Fecha y hora actuales
                    abd.convertir("'" + fechaMovimiento + "'",
                                  AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,
                                  "DD/MM/YYYY HH24:MI:SS") + ", " +
                    "'" + vo.getTipoEntidad() + "', " +
                    "'" + vo.getCodigoEntidad() + "', " +
                    vo.getTipoMovimiento() + ", ?)";

            m_Log.debug(sql);

            ps = con.prepareStatement(sql);
            int j = 1;
            ps.setString(j, vo.getDetallesMovimiento());
            ps.executeUpdate();
            SigpGeneralOperations.closeStatement(ps);
            //Creamos una tabla intermedia para mostrar el listado de movimientos
            
            
             sql = "INSERT INTO R_HISTORICO_PREV(CODIGO, USUARIO, FECHA, TIPOENTIDAD, " +
                    "CODENTIDAD, TIPOMOV)" + " VALUES ("+sqlIncremento+"," +                    
                    vo.getCodigoUsuario() + ", " +
                    // Fecha y hora actuales
                    abd.convertir("'" + fechaMovimiento + "'",
                                  AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,
                                  "DD/MM/YYYY HH24:MI:SS") + ", " +
                    "'" + vo.getTipoEntidad() + "', " +
                    "'" + vo.getCodigoEntidad() + "', " +
                    vo.getTipoMovimiento() + ")";

            m_Log.debug(sql);
            ps = con.prepareStatement(sql);            
            ps.executeUpdate();
            SigpGeneralOperations.closeStatement(ps);
          
        } catch (SQLException e) {
            throw new TechnicalException("Problema técnico de JDBC: " + e.getMessage());
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeStatement(ps);
        }
    }
    
    
    /**
     * Devuelve el listado de movimientos para una anotación, con los datos
     * necesarios para mostrarlo ATENCION excepto la descripción del movimiento 
     * (tan sólo con su tipo numérico) pues depende del idioma del usuario.
     * 
     * @param codigoAnotacion El código de la anotacion en el formato D/U/T/A/N
     *        donde D = codigo departamento, U = codigo unidad organica, T = tipo
     *        de la anotación, A = ejercicio, N = numero
     * @param params Parametros de conexión a BD.
     * @return Un vector de HistoricoMovimientoValueObject
     * @throws es.altia.common.exception.TechnicalException
     */
    public Vector<HistoricoMovimientoValueObject> obtenerHistoricoAnotacion(String codigoAnotacion, String[] params) 
            throws TechnicalException {
        
        Vector<HistoricoMovimientoValueObject> movs = new Vector<HistoricoMovimientoValueObject>();        
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        m_Log.info("(NO ERROR)Traza control HistoricoMovimientoDAO->obtenerHistoricoAnotacion BEGIN");
        try {       
          abd = new AdaptadorSQLBD(params);        
          conexion = abd.getConnection();           
          
          String sql;
          sql = "SELECT CODIGO, USUARIO, USU_NOM, " 
                 + abd.convertir("FECHA", 
                                 AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, 
                                 "DD/MM/YYYY HH24:MI:SS") + " AS FECHAMOV, TIPOMOV " +
                "FROM R_HISTORICO_PREV " + 
                     "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_USU " +
                     "ON USUARIO = USU_COD " +
                "WHERE " +
                " CODENTIDAD = ? " +
                "ORDER BY FECHA ASC, TIPOMOV ASC";
          
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);

          ps = conexion.prepareStatement(sql);         
          ps.setString(1, codigoAnotacion);
          rs = ps.executeQuery();

          while(rs.next()){
              HistoricoMovimientoValueObject hvo = new HistoricoMovimientoValueObject();
              hvo.setCodigo(rs.getInt("CODIGO"));
              hvo.setCodigoUsuario(rs.getInt("USUARIO"));
              hvo.setNombreUsuario(rs.getString("USU_NOM"));
              hvo.setFecha(rs.getString("FECHAMOV"));
              hvo.setTipoMovimiento(rs.getInt("TIPOMOV"));
              movs.add(hvo);
          }
          
        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
        m_Log.info("(NO ERROR)Traza control HistoricoMovimientoDAO->obtenerHistoricoAnotacion END");
        return movs;
    }
    
    /**
     * Devuelve todos los datos de un movimiento incluyendo los detalles en formato
     * XML sin procesar (el procesamiento depende de la manera en que se vayan a mostrar).
     * @param codigoMovimiento Código identificativo del movimiento.
     * @param params Parámetros de conexión a BD.
     * @return Un HistoricoMovimientoValueObject con todos los datos del movimiento.
     * @throws es.altia.common.exception.TechnicalException
     */
    public HistoricoMovimientoValueObject obtenerMovimiento(int codigoMovimiento,
            String[] params) throws TechnicalException {
        
        HistoricoMovimientoValueObject hvo = new HistoricoMovimientoValueObject();        
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        m_Log.debug("HistoricoMovimientoDAO->obtenerMovimiento");
        try {       
          abd = new AdaptadorSQLBD(params);        
          conexion = abd.getConnection();           
          
          String sql;
          sql = "SELECT CODIGO, USUARIO, " 
                 + abd.convertir("FECHA", 
                                 AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, 
                                 "DD/MM/YYYY HH24:MI:SS") + " AS FECHA, " +
                       "TIPOENTIDAD, CODENTIDAD, TIPOMOV, DETALLES " +
                "FROM R_HISTORICO " +
                "WHERE CODIGO = " + codigoMovimiento;
          
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);

          ps = conexion.prepareStatement(sql);
          rs = ps.executeQuery();

          while(rs.next()){
              hvo.setCodigo(rs.getInt("CODIGO"));
              hvo.setCodigoUsuario(rs.getInt("USUARIO"));
              hvo.setFecha(rs.getString("FECHA"));
              hvo.setTipoEntidad(rs.getString("TIPOENTIDAD"));
              hvo.setCodigoEntidad(rs.getString("CODENTIDAD"));
              hvo.setTipoMovimiento(rs.getInt("TIPOMOV"));
              // rs.getString no funciona con el tipo de dato CLOB
              Clob detalles = rs.getClob("DETALLES");
              hvo.setDetallesMovimiento(detalles.getSubString(1,(int)detalles.length()));
          }
          
        } catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
        
        return hvo;
    }
}
