package es.altia.flexia.integracion.moduloexterno.plugin.persistence.manual;

import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.*;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import org.apache.log4j.Logger;


public class TramiteModuloIntegracionExternoDAO {
    private static TramiteModuloIntegracionExternoDAO instance = null;
    private Logger log = Logger.getLogger(TramiteModuloIntegracionExternoDAO.class);
    private static final String TIPO_PLAZO_DIAS_HABILES = "H";
    private static final String TIPO_PLAZO_DIAS_NATURALES = "N";
    private static final String TIPO_PLAZO_MESES = "M";
    private TramiteModuloIntegracionExternoDAO(){
    }
    
    public static TramiteModuloIntegracionExternoDAO getInstance(){
        if(instance==null)
            instance = new TramiteModuloIntegracionExternoDAO();

        return instance;
    }

 /**
     * Inicia el tramite en un expediente, es decir lo crea en e_cro. La unidad tramitadora se saca del
     * tramite. Si en el tramite no contiene unidad tramitadora,
     * entonces coge la del expediente
     * @param ExpedienteVO: Datos del expediente
     * @param TramiteModuloIntegracionVO: Identificador del tramite
     * @param utr: Unidad organica que inicia el tramite
     * @param con: Conexion a la base de datos
     * @throws TechnicalException si ocurre algo grave
     */
    public void crearTramite(ExpedienteModuloIntegracionVO expedienteVO, TramiteModuloIntegracionVO tramite,
            Integer unidadInicioTramite,int codOrg, Connection con, AdaptadorSQLBD oad)
            throws TechnicalException, SQLException {
       
        log.debug("crearTramite. BEGIN: NumExp: " + expedienteVO.getNumExpediente() + ",CodTramite: " + 
                tramite.getCodTramite() + ",UnidadInicioTramite:  " + unidadInicioTramite );
        String sql;
        PreparedStatement ps = null;

        String proc = expedienteVO.getCodProcedimiento();
        int ejercicio = expedienteVO.getEjercicio();
        String numExp = expedienteVO.getNumExpediente();
        int codTra = tramite.getCodTramite();

        // Se suma un segundo a la fecha y hora actual para que no haya coincidencia
        //entre la fecha de inicio de los tramites de un expediente
       
       
  
        int usuarioInicio = expedienteVO.getCodUsuarioIniciaExpediente();
        Calendar fechaLimite = fechaLimiteAutomatica(tramite, expedienteVO, con, oad);
        //como es tramite de inicio ocurrencia=1
       try {

            int ocurrencia = getOcurrenciaTramite(con, expedienteVO,  oad, codTra, codOrg);
            ocurrencia++;

            sql = "INSERT INTO E_CRO (CRO_MUN,CRO_PRO,CRO_EJE,CRO_NUM,CRO_TRA,CRO_OCU,CRO_FEI,CRO_FEF,CRO_USU,CRO_UTR,CRO_FIP,CRO_FLI)"
                     + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            if (log.isDebugEnabled()) {
                log.debug(sql);
            }
            ps = con.prepareStatement(sql);
            
            Timestamp ahora = new Timestamp((new Date()).getTime());
            int i = 1;

            ps.setInt(i++, codOrg);// CRO_MUN
            ps.setString(i++, proc);// CRO_PRO
            ps.setInt(i++, ejercicio);// CRO_EJE
            ps.setString(i++, numExp);// CRO_NUM
            ps.setInt(i++, codTra);// CRO_TRA
            ps.setInt(i++, ocurrencia);// CRO_OCU
            ps.setTimestamp(i++, ahora); // CRO_FEI
           
            ps.setNull(i++, Types.TIMESTAMP); // CRO_FEF
            ps.setInt(i++, usuarioInicio);// CRO_USU
            ps.setInt(i++, unidadInicioTramite);// CRO_UTR
            ps.setTimestamp(i++, ahora); // CRO_FIP
            if (fechaLimite != null) {
                ps.setTimestamp(i++, new Timestamp(fechaLimite.getTimeInMillis())); // CRO_FLI
            } else {
                ps.setNull(i++, Types.TIMESTAMP);
            }
            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            if (ps!=null) ps.close();
            
        }
    }

    private int getOcurrenciaTramite(Connection con, ExpedienteModuloIntegracionVO expedienteVO, AdaptadorSQLBD oad,int codTramite,int codOrganizacion)
            throws SQLException, TechnicalException {
        Statement st = null;
        String sql = null;

        String codProcedimiento = expedienteVO.getCodProcedimiento();
        int ejercicio = expedienteVO.getEjercicio();
        String numero = expedienteVO.getNumExpediente();
       
        //String codTramite = (String) gVO.getAtributo("codTramite");
        int ocurrencia = 0;

        st = con.createStatement();
        sql = " SELECT " + oad.funcionMatematica(oad.FUNCIONMATEMATICA_MAX, new String[]{"CRO_OCU"})
                + " AS OCURRENCIA FROM E_CRO "
                + " WHERE CRO_MUN=" + codOrganizacion
                + " AND CRO_PRO='" + codProcedimiento + "'"
                + " AND CRO_EJE=" + ejercicio
                + " AND CRO_NUM='" + numero + "'"
                + " AND CRO_TRA=" + codTramite;

        log.debug(" ******* codTramite: " + codTramite);
        if (log.isDebugEnabled()) {
            log.debug("getOcurrenciaTramite.SQL:"+sql);
        }
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            ocurrencia = rs.getInt("OCURRENCIA");

        }
        rs.close();
        st.close();
        return ocurrencia;
    }

   
     private Calendar getFechaLimitePlazo(String tipoPlazo, int unidadesPlazo, Connection con, AdaptadorSQLBD oad) throws TechnicalException {


        Calendar fechaLimitePlazo = Calendar.getInstance();
        fechaLimitePlazo.setTimeInMillis(System.currentTimeMillis());
        if (tipoPlazo.equals(TIPO_PLAZO_DIAS_NATURALES)) {
            fechaLimitePlazo.add(Calendar.DATE, unidadesPlazo);
        } else if (tipoPlazo.equals(TIPO_PLAZO_MESES)) {
            fechaLimitePlazo.add(Calendar.MONTH, unidadesPlazo);
        } else if (tipoPlazo.equals(TIPO_PLAZO_DIAS_HABILES)) {
            Vector diasFestivos = obtenerFestivosPorAno(
                    Integer.toString(fechaLimitePlazo.get(Calendar.YEAR)), con, oad);
            int contadorDias = unidadesPlazo;
            while (contadorDias > 0) {
                fechaLimitePlazo.add(Calendar.DATE, 1);
                boolean incremento = true;
                if (fechaLimitePlazo.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    incremento = false;
                } else {
                    for (Object objDiaFestivo : diasFestivos) {
                        String diaFestivo = (String) objDiaFestivo;
                        java.util.Date dateFestivo = obtenerDate(diaFestivo);
                        if (dateFestivo.equals(fechaLimitePlazo.getTime())) {
                            incremento = false;
                        }
                    }
                }
                if (incremento) {
                    contadorDias--;
                }
            }
        }
        return fechaLimitePlazo;

    }
    
     
      public Vector obtenerFestivosPorAno(String ano, Connection con, AdaptadorSQLBD oad) throws TechnicalException {

        Statement st = null;
        ResultSet rs = null;
        Vector festivos = new Vector();

        try {
            st = con.createStatement();

            String sql = "SELECT " + oad.convertir("GEN_CAL_DIA",
                    AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY")
                    + " AS GEN_CAL_DIA "
                    + " FROM GEN_CALENDARIO WHERE GEN_CAL_ANO "
                    + "=" + ano;
            if (log.isDebugEnabled()) {
                log.debug("obtenerFestivosPorAno:"+sql);
            }

            rs = st.executeQuery(sql);

            while (rs.next()) {
                festivos.addElement(rs.getString("GEN_CAL_DIA"));
            }

            rs.close();
            st.close();

            log.debug("Obtenidos los dias festivos");

        } catch (Exception e) {
            //Si la eliminacion va mal tenemos que lanzar y loggear la excepcion
            log.error(e.getMessage());
            throw new TechnicalException("Error obteniendo d?as festivos", e);
        }

        //Queremos estar informados de cuando este metodo ha finalizado
       log.debug("Obtenidos los dias festivos");
        return festivos;
    }

       public static java.util.Date obtenerDate(String s) {
        int dia, mes, anho;
        try {
           
            dia = Integer.parseInt(s.substring(0, 2));
            mes = Integer.parseInt(s.substring(3, 5)) - 1;
            anho = Integer.parseInt(s.substring(6, 10)) - 1900;
            return new Date(anho, mes, dia);
        } catch (Exception ex) {
            return null;
        }
    }
      
    // funcion que devuelve la fecha limite del tramite en caso que el su definicion este marcada la notificacion y bloqueo
    // de plazos automatica
    private Calendar fechaLimiteAutomatica(TramiteModuloIntegracionVO tramite, ExpedienteModuloIntegracionVO expediente, Connection con, AdaptadorSQLBD oad) throws TechnicalException {
        String sql = "SELECT TRA_PLZ, TRA_UND, TRA_GENERARPLZ FROM E_TRA WHERE TRA_COD = ? AND TRA_PRO = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String tipoPlazo = "";
        int unidadesPlazo = 0;
        int generarPlazo = -1;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, tramite.getCodTramite());
            ps.setString(2, expediente.getCodProcedimiento());
            rs = ps.executeQuery();
            if (rs.next()) {
                unidadesPlazo = rs.getInt(1);
                tipoPlazo = rs.getString(2);
                generarPlazo = rs.getInt(3);
            }
        } catch (SQLException ex) {
            log.error("Salta excepcion");
            ex.printStackTrace();
            return null;
        }
        if (generarPlazo == 1 && unidadesPlazo > 0) {
            return getFechaLimitePlazo(tipoPlazo, unidadesPlazo, con, oad);
        } else {
            return null;
        }
    }

      public TramiteModuloIntegracionVO getTramitesInicio(ExpedienteModuloIntegracionVO expedienteVO, Connection con)
            throws TechnicalException, SQLException, SQLException {
        
        log.debug("getTramitesInicio.BEGIN:");  
        String sql;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String proc = expedienteVO.getCodProcedimiento();
        TramiteModuloIntegracionVO tramite= new TramiteModuloIntegracionVO();
        try {

            sql = "SELECT PRO_TRI FROM E_PRO WHERE PRO_COD=?";
            if (log.isDebugEnabled()) {
                log.debug("getTramitesInicio:" + sql);
            }
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, proc);
            rs = ps.executeQuery();
            if (rs.next()) {
                tramite.setCodTramite(rs.getInt("PRO_TRI"));
            }else //No hay trámite de inicio, así que ponemos el codigo de un tramite inexistente
            { tramite.setCodTramite(-55555);}
            rs.close();
            ps.close();
            log.debug("getTramitesInicio.END"); 
            return tramite;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            if (ps!=null) ps.close();
            if (rs!=null) rs.close();
            //if (con!=null) con.close();
            
        }
      }
        
        /**
         * Dame tipo de unidad de inicio del trámite
         * (La del expediente, la del trámite anterior, cq, otras, todas)
         * @param codOrg
         * @param codTra
         * @return
         * @throws TechnicalException
         * @throws SQLException
         * @throws SQLException 
         */
        public int dameTipoUnidadInicio(int codOrg, int codTra, String codPro,Connection con)
            throws TechnicalException, SQLException, SQLException {

        log.debug("DameTipoUnidadInicio.BEGIN:");
        log.debug("DameTipoUnidadInicio.codOrg: "+ codOrg);
        log.debug("DameTipoUnidadInicio.codTramite: "+ codTra);
        log.debug("DameTipoUnidadInicio.codProcedimiento: "+ codPro);
        
        String sql;
        Statement st = null;
        ResultSet rs = null;
        int tipo=-1; 
        try {
            

            sql =   " SELECT TRA_UTR FROM E_TRA"
                  + " WHERE TRA_MUN= "+ codOrg
                  + " AND TRA_PRO= '"+ codPro + "'"
                  + " AND TRA_COD= "+ codTra;
            
            
            if (log.isDebugEnabled()) {
                log.debug("dameTipoUnidadInicio. Consulta:" + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
               tipo= rs.getInt("TRA_UTR"); 
            }
            rs.close();
            st.close();
            log.debug("El tipo devuelto es:" + tipo);
            return tipo;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            if (st!=null) st.close();
            if (rs!=null) rs.close();
            //if (con!=null) con.close();
            
        }

        
        
    }
        
        
        /**
         *  Me da las unidades de inicio del tramite,
         *  cuando el tipo de unidad de inicio del
         *  trámite es "otras"
         * @param codOrg código de la organizacion (codMun)
         * @param codTra código del trámite
         * @param codPro código del procedimiento
         * @return
         * @throws TechnicalException
         * @throws SQLException
         * @throws SQLException 
         */
        public ArrayList<Integer> dameUnidadesInicio(int codOrg, int codTra, String codPro,Connection con)
            throws TechnicalException, SQLException, SQLException {

        log.debug("dameUnidadesInicio.BEGIN. codOrg: " + codOrg);  
        log.debug("dameUnidadesInicio.BEGIN. codTra: " + codTra);    
        log.debug("dameUnidadesInicio.BEGIN. codPro: " + codPro);    
        String sql;
        Statement st = null;
        ResultSet rs = null;
        Integer resultado=null; 
        ArrayList<Integer> unidadesInicioTramite= new ArrayList<Integer>();
        try {
            

            sql = " SELECT TRA_UTR_COD "
                + " FROM E_TRA_UTR "
                + " WHERE TRA_PRO='"+ codPro+"'"
                + " AND TRA_MUN= " + codOrg
                + " AND TRA_COD= " + codTra;
            
            
            if (log.isDebugEnabled()) {
                log.debug("dameUnidadesInicio. Consulta:" + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                resultado = rs.getInt("TRA_UTR_COD");
                log.debug("La unidad de inicio puede ser:  " + resultado);
                unidadesInicioTramite.add(resultado);
            }
            
            rs.close();
            st.close();
            log.debug("dameUnidadesInicio.END. El tamanho de la lista de unidades es: "+ unidadesInicioTramite.size());    
            return unidadesInicioTramite;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            if (st!=null) st.close();
            if (rs!=null) rs.close();
            //if (con!=null) con.close();
            
        }

        
        
    }    
       
       
}//class