// NOMBRE DEL PAQUETE
package es.altia.agora.business.registro.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.registro.HistoricoMovimientoValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.ReservaOrdenValueObject;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.registro.persistence.HistoricoMovimientoManager;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.business.util.HistoricoAnotacionHelper;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.Fecha;
import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.*;

import java.sql.*;

import java.text.SimpleDateFormat;
import java.util.Vector;

public class ReservaOrdenDAO {

  private int numeroRegistrado=0;
  Vector numeros=null;
  Vector numero=null;
  private int ejerc;
  int cont;

  private static ReservaOrdenDAO instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
            LogFactory.getLog(ReservaOrdenDAO.class.getName());

  protected static String ejercicioRER;
  protected static String numeroRER;
  protected static String fechaRER;
  protected static String codDeptoRER;
  protected static String codUnidadRER;
  protected static String tipoRER;
  protected static String codUsuarioRER;
  protected static String codUsuarioUSU;
  protected static String nomUsuarioUSU;

  protected ReservaOrdenDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");

    ejercicioRER = m_ConfigTechnical.getString("SQL.R_RER.ejercicio");
    numeroRER = m_ConfigTechnical.getString("SQL.R_RER.numero");
    fechaRER = m_ConfigTechnical.getString("SQL.R_RER.fecha");
    tipoRER = m_ConfigTechnical.getString("SQL.R_RER.tipo");
    codDeptoRER = m_ConfigTechnical.getString("SQL.R_RER.dep_cod");
    codUnidadRER = m_ConfigTechnical.getString("SQL.R_RER.uor_cod");
    codUsuarioRER = m_ConfigTechnical.getString("SQL.R_RER.usu_cod");

    codUsuarioUSU=m_ConfigTechnical.getString("SQL.A_USU.codigo");
    nomUsuarioUSU=m_ConfigTechnical.getString("SQL.A_USU.nombre");


  }

    public static ReservaOrdenDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        synchronized (ReservaOrdenDAO.class) {
            if (instance == null) {
                instance = new ReservaOrdenDAO();
            }
        }

        return instance;
    }

    private Vector<ReservaOrdenValueObject> load(Connection con, ReservaOrdenValueObject reservaVO)
            throws TechnicalException {
        
        String cant = reservaVO.getCantidad();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            
            String sql = "SELECT NRE_NUM FROM R_NRE " +
                    "WHERE NRE_DEP =" + reservaVO.getCodDepto() + " " +
                    "AND NRE_UOR = " + reservaVO.getCodUnidad() + " " +
                    "AND NRE_TIP = '" + reservaVO.getTipoReg() + "' " +
                    "AND NRE_EJE =" + reservaVO.getEjercicio();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            int ultimoNumRegistrado = 0;
            if (rs.next()) {
                ultimoNumRegistrado = rs.getInt("NRE_NUM");
            }
            
            Vector<ReservaOrdenValueObject> listaReservas = new Vector<ReservaOrdenValueObject>();
            for (int i = 0; i < Integer.parseInt(cant); i++) {
                ReservaOrdenValueObject res = new ReservaOrdenValueObject();
                res.setTxtNumRegistrado(++ultimoNumRegistrado);
                res.setEjercicio(reservaVO.getEjercicio());
                res.setCodDepto(reservaVO.getCodDepto());
                res.setCodUnidad(reservaVO.getCodUnidad());
                res.setTipoReg(reservaVO.getTipoReg());
                res.setFecha(reservaVO.getFecha());
                
                listaReservas.add(res);
            }
                
            return listaReservas;
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
        
    }

    private void insertNRE(Connection con, ReservaOrdenValueObject reservaVO) throws TechnicalException {

        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO R_NRE VALUES(" + reservaVO.getCodDepto() + ", " + reservaVO.getCodUnidad() +
                    ", '" + reservaVO.getTipoReg() + "', " + reservaVO.getEjercicio() + ", " + reservaVO.getTxtNumRegistrado() + ")";
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }
  
    private int modify(Connection con, ReservaOrdenValueObject reservaVO) throws TechnicalException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        int res;
        try {
            ReservaOrdenValueObject ultimaReserva = (ReservaOrdenValueObject)reservaVO.getNumeros().lastElement();
            int num = ultimaReserva.getTxtNumRegistrado();
            
            String sqlConsultaNRE="SELECT NRE_NUM FROM R_NRE WHERE NRE_NUM ='" + num + "' " +
                    "AND NRE_DEP =" + reservaVO.getCodDepto() + " " +
                    "AND NRE_UOR =" + reservaVO.getCodUnidad() + " " +
                    "AND NRE_TIP ='" + reservaVO.getTipoReg() + "' " +
                    "AND NRE_EJE =" + reservaVO.getEjercicio();              
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Sentencia SQL:" + sqlConsultaNRE);
            }
            ps = con.prepareStatement(sqlConsultaNRE);
            rs = ps.executeQuery();

            if (rs.next()) {
                res=0;
                return res;
            }
            String sql = "UPDATE R_NRE SET NRE_NUM ='" + num + "' " +
                    "WHERE NRE_DEP =" + reservaVO.getCodDepto() + " " +
                    "AND NRE_UOR =" + reservaVO.getCodUnidad() + " " +
                    "AND NRE_TIP ='" + reservaVO.getTipoReg() + "' " +
                    "AND NRE_EJE =" + reservaVO.getEjercicio();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            ps = con.prepareStatement(sql);
            res=ps.executeUpdate();
            return res;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage(), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    public void insert(ReservaOrdenValueObject reservaVO, String[] params) throws TechnicalException {
        
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection conexion = null;
        PreparedStatement ps = null;
        m_Log.debug("Entra en el insert del DAO");
        
        try {
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            
            Vector<ReservaOrdenValueObject> reservas = load(conexion, reservaVO);
            
            for (ReservaOrdenValueObject reserva: reservas) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String sql = "";
                sql = "INSERT INTO R_RER VALUES(?, ?, ?, ?, ?," +
                            abd.convertir("'" + sdf.format(reserva.getFecha()) + "'", 
                            AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, 
                            "DD/MM/YYYY HH24:MI") + ",?)";
                m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);
                int i = 1;
                ps.setInt(i++, reserva.getCodDepto());
                ps.setInt(i++, reserva.getCodUnidad());
                ps.setString(i++, reserva.getTipoReg());
                ps.setInt(i++, Integer.parseInt(reserva.getEjercicio()));
                ps.setInt(i++, reserva.getTxtNumRegistrado());
                ps.setInt(i++, reservaVO.getUsuario());
                
                ps.executeUpdate();
                
                SigpGeneralOperations.closeStatement(ps);
                if (reserva.getTxtNumRegistrado() == 1) insertNRE(conexion, reserva);
                                
                // Insertar reserva en el historico de movimientos
                HistoricoMovimientoValueObject hvo = new HistoricoMovimientoValueObject();
                hvo.setCodigoUsuario(reservaVO.getUsuario());
                hvo.setTipoEntidad(ConstantesDatos.HIST_ENTIDAD_ANOTACION);
                hvo.setCodigoEntidad(HistoricoAnotacionHelper.crearClaveHistorico(reserva));
                hvo.setTipoMovimiento(ConstantesDatos.HIST_ANOT_RESERVA);
                hvo.setDetallesMovimiento(HistoricoAnotacionHelper.crearXMLReserva(reserva));
                HistoricoMovimientoManager.getInstance().insertarMovimientoHistorico(hvo, conexion, params);                                
            }
            
            reservaVO.setNumeros(reservas);
            int resultado;            
            resultado=modify(conexion, reservaVO);
            if(resultado>0) abd.finTransaccion(conexion);
            else abd.rollBack(conexion);

        } catch (Exception e) {
            e.printStackTrace();
            SigpGeneralOperations.rollBack(abd, conexion); 
            throw new TechnicalException(e.getMessage(), e);
        }finally {
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, conexion);            
        }
    }


   public void loadRER(ReservaOrdenValueObject reservaVO,String[] params) throws TechnicalException,BDException {
       AdaptadorSQLBD abd = null;
       Connection conexion = null;
       Statement stmt = null;
       String sql= "";

       //Usar el JDBCWrapper es mas sencillo que usar JDBC directamente
       //JDBCWrapper sqlExec = new JDBCWrapper();
       try{
         m_Log.debug("A por el OAD");
         abd = new AdaptadorSQLBD(params);
         m_Log.debug("A por la conexion");
         conexion = abd.getConnection();
         sql = "SELECT " + ejercicioRER+","+numeroRER + "," +
                 abd.convertir(fechaRER, AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") + " AS "+
               fechaRER  + ","+codUsuarioRER+","+nomUsuarioUSU+ " FROM R_RER "+
               " LEFT JOIN "+GlobalNames.ESQUEMA_GENERICO+"A_USU ON "+codUsuarioRER+"="+codUsuarioUSU+
               " WHERE " +
               codDeptoRER + "=" + reservaVO.getCodDepto() + " AND " +
               codUnidadRER + "="+ reservaVO.getCodUnidad() + " AND " +
               tipoRER + "='"+ reservaVO.getTipoReg()+"'" + " ORDER BY " + ejercicioRER + ", " + numeroRER;
         if(m_Log.isDebugEnabled()) m_Log.debug(sql);

         PreparedStatement ps = conexion.prepareStatement(sql);
         ResultSet rs = ps.executeQuery();
         ReservaOrdenValueObject reserva;
         Vector codigos = new Vector();

         //sqlExec.execute(sql);
         while(rs.next()) {
           reserva= new ReservaOrdenValueObject();
           ejerc=rs.getInt(ejercicioRER);
           reserva.setEjercicio(Integer.toString(ejerc));
           numeroRegistrado=rs.getInt(numeroRER);
           reserva.setTxtNumRegistrado(numeroRegistrado);
           String fecha=rs.getString(fechaRER);
           reserva.setFec(fecha);
           reserva.setFecha(Fecha.obtenerDateCompleto2(fecha));
           reserva.setDia(fecha.substring(0,2));
           String mes = fecha.substring(3,5);
           reserva.setMes(mes);
           reserva.setAno(fecha.substring(6,10));
           reserva.setHora(fecha.substring(11,13));
           reserva.setMin(fecha.substring(14,16));
           reserva.setUsuario(rs.getInt(codUsuarioRER));
           reserva.setNombreUsuario(rs.getString(nomUsuarioUSU));
           codigos.addElement(reserva);
           m_Log.debug("Reserva #" + codigos.size() + ": " + ejerc + "/" + numeroRegistrado + " - " + fecha);
         }
         reservaVO.setCodigos(codigos);
         rs.close();
         ps.close();
         m_Log.debug("Total reservas recuperadas : " + codigos.size());

       } catch (Exception e) {
           e.printStackTrace();
           if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
       } finally {
           try{
               abd.devolverConexion(conexion);
           }catch (Exception e){
               e.printStackTrace();
               if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
           }
       }
  }
   
  /**
   * Recupera las reservas hasta cierta fecha.
   * @param reservaVO vo con los datos del registro y la fecha
   * @param params parametros de conexion a BD
   * @return vector con las reservas recuperadas
   * @throws Exception
   */
  public Vector<ReservaOrdenValueObject> cargarReservasPorFecha(
              ReservaOrdenValueObject reservaVO,String[] params) 
              throws Exception {
          
       AdaptadorSQLBD abd = null;
       Connection conexion = null;
       Statement stmt = null;
       String sql= "";
       Vector<ReservaOrdenValueObject> reservas = new Vector<ReservaOrdenValueObject>();
       
       try{
         abd = new AdaptadorSQLBD(params);
         conexion = abd.getConnection();
         stmt = conexion.createStatement();
         
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         String fechaLimite = sdf.format(reservaVO.getFecha());
         
         sql = "SELECT RER_EJE, RER_NUM," +
                 abd.convertir("RER_FEC", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") + " AS FECHA " +
               "FROM R_RER WHERE " +
               "RER_DEP=" + reservaVO.getCodDepto() + " AND " +
               "RER_UOR="+ reservaVO.getCodUnidad() + " AND " +
               "RER_TIP='"+ reservaVO.getTipoReg()+"' AND " + 
               abd.convertir("RER_FEC", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "YYYY-MM-DD") + 
               " <= '" + fechaLimite + "'";

         if(m_Log.isDebugEnabled()) m_Log.debug(sql);
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
           ReservaOrdenValueObject reserva= new ReservaOrdenValueObject();
           
           reserva.setTipoReg(reservaVO.getTipoReg());
           reserva.setCodDepto(reservaVO.getCodDepto());
           reserva.setCodUnidad(reservaVO.getCodUnidad());
           
           int ejercicio = rs.getInt("RER_EJE");
           reserva.setEjercicio(Integer.toString(ejercicio));
           int num = rs.getInt("RER_NUM");
           reserva.setTxtNumRegistrado(num);
           String fecha = rs.getString("FECHA");
           reserva.setFecha(Fecha.obtenerDateCompleto2(fecha));
           reservas.addElement(reserva);
           m_Log.debug("Reserva #" + reservas.size() + ": " + ejercicio + "/" + num + " - " + fecha);
         }
         rs.close();
         stmt.close();
         m_Log.debug("Total reservas recuperadas : " + reservas.size());

       } catch (Exception e) {
           e.printStackTrace();
           throw e;
       } finally {
           SigpGeneralOperations.closeStatement(stmt);
           SigpGeneralOperations.devolverConexion(abd, conexion);
       }
       
       return reservas;
  }
   
    /**
     * Anula las reservas del vector con la diligencia que se indica. Para cada una
     * se crea una anotación en R_RES con estado anulado y se anota la anulacion
     * en el histórico.
     * @param reservas reservas a anular
     * @param diligencia diligencia de anulación
     * @param usuario usuario que realiza la anulación
     * @param params parametros de conexion a BD
     * @throws java.lang.Exception
     */
    public void anularReservas(Vector<ReservaOrdenValueObject> reservas, String diligencia, UsuarioValueObject usuario, String[] params)
            throws Exception {
        m_Log.debug("ReservaOrdenDAO.anularReservas");
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        String sql = "";

        try {
            // Inicio de transaccion
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            stmt = conexion.createStatement();
            AnotacionRegistroManager registroMan = AnotacionRegistroManager.getInstance();
            HistoricoMovimientoManager histMan = HistoricoMovimientoManager.getInstance();

            // Obtenemos tipo de documento por defecto
            int tipoDoc = -1;
            sql = "SELECT TDO_IDE FROM R_TDO WHERE TDO_ACT = 'SI'";
            m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                tipoDoc = rs.getInt("TDO_IDE");
            }
                
            
            // RegistroValueObject con valores comunes a todas las reservas.
            RegistroValueObject registro = crearRegistroValueObjectAnular(usuario, diligencia, tipoDoc);

            // HistoricoMovimientoValueObject con valores comunes a todas las reservas.
            HistoricoMovimientoValueObject hvo = crearHistoricoValueObjectAnular(usuario, diligencia);

            // Se insertan las anotaciones anuladas, una por cada reserva.
            for (ReservaOrdenValueObject reserva : reservas) {

                m_Log.debug("====> ANULANDO RESERVA " + reserva);

                // Borrado de la reserva de R_RER
                sql = "DELETE FROM R_RER WHERE" +
                        " RER_DEP=" + reserva.getCodDepto() +
                        " AND RER_UOR=" + reserva.getCodUnidad() +
                        " AND RER_TIP='" + reserva.getTipoReg() + "'" +
                        " AND RER_EJE=" + reserva.getEjercicio() +
                        " AND RER_NUM=" + reserva.getTxtNumRegistrado();
                m_Log.debug(sql);
                stmt.executeUpdate(sql);

                // Inserción de la anotación
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String fecha = sdf.format(reserva.getFecha());
                registro.setFecEntrada(fecha);
                registro.setFecHoraDoc(fecha);
                registro.setIdentDepart(reserva.getCodDepto());
                registro.setUnidadOrgan(reserva.getCodUnidad());
                registro.setTipoReg(reserva.getTipoReg());
                registro.setAnoReg(Integer.parseInt(reserva.getEjercicio()));
                registro.setNumReg(new Long(reserva.getTxtNumRegistrado()));
                
                registroMan.insertarAnotacion(abd, conexion, registro);
                registroMan.insertarInteresados(conexion, registro);

                // Inserción en el historico
                hvo.setCodigoEntidad(HistoricoAnotacionHelper.crearClaveHistorico(reserva));
                histMan.insertarMovimientoHistorico(hvo, conexion, params);
            }

            // Fin de transaccion
            SigpGeneralOperations.commit(abd, conexion);

        } catch (Exception ex) {
            SigpGeneralOperations.rollBack(abd, conexion);
            ex.printStackTrace();
            throw ex;
        } finally {
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
    }
  
    /**
     * Crea un RegistroValueObject con los valores que son comunes a todas las
     * reservas que se van a anular. Solo falta el codigo de la anotacion.
     * @param usuario vo del usuario que realiza la operacion
     * @return vo con valores inicializados
     */
    private RegistroValueObject crearRegistroValueObjectAnular(UsuarioValueObject usuario, String diligencia, int tipoDoc) {

        RegistroValueObject registro = new RegistroValueObject();

        // Asunto
        int idioma = 1;
        if (usuario != null) {
            idioma = usuario.getIdioma();
        }
        TraductorAplicacionBean descriptor = new TraductorAplicacionBean();
        descriptor.setApl_cod(ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA);
        descriptor.setIdi_cod(idioma);
        registro.setAsunto(descriptor.getDescripcion("msjAsuntoAnular"));
        
        // Observaciones y diligencia de anulación (iguales)
        registro.setObservaciones(diligencia);
        registro.setDilAnulacion(diligencia);

        // Tipo de anotacion (ordinaria)
        registro.setTipoAnot(ConstantesDatos.REG_ANOT_TIPO_ORDINARIA);
        
        // Tipo de documento (ninguno)
        registro.setIdTipoDoc(tipoDoc);

        // Estado (anulada)
        registro.setEstAnotacion(ConstantesDatos.REG_ANOTACION_ESTADO_ANULADA);

        // Unidad tramitadora y tercero ficticios
        registro.setIdUndTramitad(ConstantesDatos.UOR_ANULAR_RES);

        int codigoTercero = ConstantesDatos.COD_TER_ANULAR_RES;
        int versionTercero = ConstantesDatos.VER_TER_ANULAR_RES;
        int domTercero = ConstantesDatos.DOM_TER_ANULAR_RES;
        int rolTercero = ConstantesDatos.ROL_TER_ANULAR_RES;

        registro.setCodInter(codigoTercero);
        registro.setDomicInter(domTercero);
        registro.setNumModInfInt(versionTercero);

        // Multiinteresado ficticio
        Vector codTer = new Vector();
        codTer.add(Integer.toString(codigoTercero));
        registro.setlistaCodTercero(codTer);

        Vector verTer = new Vector();
        verTer.add(Integer.toString(versionTercero));
        registro.setlistaVersionTercero(verTer);

        Vector domTer = new Vector();
        domTer.add(Integer.toString(domTercero));
        registro.setlistaCodDomicilio(domTer);

        Vector rolTer = new Vector();
        rolTer.add(Integer.toString(rolTercero));
        registro.setlistaRol(rolTer);

        // Usuario
        registro.setUsuarioQRegistra(Integer.toString(usuario.getIdUsuario()));
        registro.setDptoUsuarioQRegistra(Integer.toString(usuario.getDepCod()));
        registro.setUnidOrgUsuarioQRegistra(Integer.toString(usuario.getUnidadOrgCod()));
   
        //Necesitamos obtener el codigo de la oficina de Registro
       Integer codOficinaRegistro = UsuarioManager.getInstance().getCodOficinaRegistro(usuario.getIdUsuario(),usuario.getUnidadOrgCod(), usuario.getParamsCon());
       m_Log.debug("===========> CODOFICINAregistro: " + codOficinaRegistro);
       registro.setCodOficinaRegistro(codOficinaRegistro); 
       
    
        
        
        return registro;
    }

    /**
     * Crea un HistoricoMovimientoValueObject con los valores que son comunes a 
     * todas las reservas que se van a anular. Solo falta el codigo de la anotacion.
     * @param usuario vo del usuario que realiza la operacion
     * @param diligencia texto de la anulacion
     * @return vo con los valores inicializados
     */
    private HistoricoMovimientoValueObject crearHistoricoValueObjectAnular(UsuarioValueObject usuario, String diligencia) {
       
        HistoricoMovimientoValueObject hvo = new HistoricoMovimientoValueObject();
        hvo.setCodigoUsuario(usuario.getIdUsuario());
        hvo.setTipoEntidad(ConstantesDatos.HIST_ENTIDAD_ANOTACION);
        hvo.setTipoMovimiento(ConstantesDatos.HIST_ANOT_ANULAR_RES);
        hvo.setDetallesMovimiento(HistoricoAnotacionHelper.crearXMLAnularReserva(diligencia));
        
        return hvo;
    }
}



