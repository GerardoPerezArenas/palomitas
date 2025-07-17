package es.altia.agora.webservice.tramitacion.servicios.tvg.bd.acceso;

import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.CROTramiteVO;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.util.Campos;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/**
 * Created by IntelliJ IDEA.
 * User: marcos.baltar
 * Date: 25-may-2006
 * Time: 10:58:09
 * To change this template use File | Settings | File Templates.
 */
public class TramiteDAO {
    protected static Config m_ConfigTechnical;


    protected static String cro_mun;
    protected static String cro_pro;
    protected static String cro_eje;
    protected static String cro_num;
    protected static String cro_tra;
    protected static String cro_fei;
    protected static String cro_fef;
    protected static String cro_utr;
    protected static String cro_usu;
    protected static String cro_fip;
    protected static String cro_fli;
    protected static String cro_ffp;
    protected static String cro_res;
    protected static String cro_obs;
    protected static String cro_ocu;

    protected static String patronFechaOracle = "DD/MM/YYYY HH24:MI:SS";
    protected static String patronFechaJava = "dd/MM/yyyy HH:mm:ss";

    private static Log m_Log = LogFactory.getLog(TramiteDAO.class.getName());

    public TramiteDAO() {
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        cro_mun = m_ConfigTechnical.getString("SQL.E_CRO.codMunicipio");
        cro_pro = m_ConfigTechnical.getString("SQL.E_CRO.codProcedimiento");
        cro_eje = m_ConfigTechnical.getString("SQL.E_CRO.ano");
        cro_num = m_ConfigTechnical.getString("SQL.E_CRO.numero");
        cro_tra = m_ConfigTechnical.getString("SQL.E_CRO.codTramite");
        cro_fei = m_ConfigTechnical.getString("SQL.E_CRO.fechaInicio");
        cro_fef = m_ConfigTechnical.getString("SQL.E_CRO.fechaFin");
        cro_utr = m_ConfigTechnical.getString("SQL.E_CRO.codUnidadTramitadora");
        cro_usu = m_ConfigTechnical.getString("SQL.E_CRO.codUsuario");
        cro_fip = m_ConfigTechnical.getString("SQL.E_CRO.fechaInicioPlazo");
        cro_fli = m_ConfigTechnical.getString("SQL.E_CRO.fechaLimite");
        cro_ffp = m_ConfigTechnical.getString("SQL.E_CRO.fechaFinPlazo");
        cro_res = m_ConfigTechnical.getString("SQL.E_CRO.resolucion");
        cro_obs = m_ConfigTechnical.getString("SQL.E_CRO.observaciones");
        cro_ocu = m_ConfigTechnical.getString("SQL.E_CRO.ocurrencia");

    }

    public List getTramitesExpediente(String expediente, Connection con, AdaptadorSQLBD bd)
            throws SQLException,BDException, ParseException {

        List salida = new ArrayList();

        String sql = "SELECT " + Campos.campos.getAtributo("codMunicipioCRO")
                + "," + Campos.campos.getAtributo("codProcedimientoCRO")
                + "," + Campos.campos.getAtributo("anoCRO")
                + "," + Campos.campos.getAtributo("numeroCRO")
                + "," + Campos.campos.getAtributo("codTramiteCRO")
                + "," + bd.convertir( (String) Campos.campos.getAtributo("fechaInicioCRO"),AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,patronFechaOracle) + " AS txtFechaInicioCRO "
                + "," + bd.convertir( (String) Campos.campos.getAtributo("fechaFinCRO"),AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,patronFechaOracle) + " AS txtFechaFinCRO "

                + "," + Campos.campos.getAtributo("codUnidadTramitadoraCRO")
                + "," + Campos.campos.getAtributo("codUsuarioCRO")
                + "," + bd.convertir( (String) Campos.campos.getAtributo("fechaInicioPlazoCRO"),AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,patronFechaOracle) + " AS txtFechaInicioPlazoCRO "
                + "," + bd.convertir( (String) Campos.campos.getAtributo("fechaLimiteCRO"),AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,patronFechaOracle) + " AS txtFechaLimiteCRO "
                + "," + bd.convertir( (String) Campos.campos.getAtributo("fechaFinPlazoCRO"),AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,patronFechaOracle) + " AS txtFechaFinPlazoCRO "
                + "," + Campos.campos.getAtributo("ocurrenciaCRO")
                + "," + Campos.campos.getAtributo("resolucionCRO")
                + "," + Campos.campos.getAtributo("observacionesCRO")
                + "," + Campos.campos.getAtributo("usuarioFinalizacionCRO")
                + "," + Campos.campos.getAtributo("valorTML")
                + "," + Campos.campos.getAtributo("nombreUOR")
                + "," + Campos.campos.getAtributo("visibleInternetTRA")

                + " FROM E_CRO, E_TML, A_UOR, E_TRA "
                + " WHERE " + Campos.campos.getAtributo("numeroCRO") + "='" + expediente + "'"
                + " AND " + Campos.campos.getAtributo("codMunicipioTML") + " = " + Campos.campos.getAtributo("codMunicipioCRO")
                + " AND " + Campos.campos.getAtributo("codProcedimientoTML") + " = " + Campos.campos.getAtributo("codProcedimientoCRO")
                + " AND " + Campos.campos.getAtributo("codTramiteTML") + " = " + Campos.campos.getAtributo("codTramiteCRO")
                + " AND " + Campos.campos.getAtributo("idiomaTML") + " = '"+m_ConfigTechnical.getString("idiomaDefecto")+"' "
                + " AND " + Campos.campos.getAtributo("codCampoMLTML") + " = 'NOM'"
                + " AND " + Campos.campos.getAtributo("codUnidadTramitadoraCRO") + " = " + Campos.campos.getAtributo("codUOR")
                + " AND " + Campos.campos.getAtributo("codMunicipioTRA") + " = " + Campos.campos.getAtributo("codMunicipioCRO")
                + " AND " + Campos.campos.getAtributo("codProcedimientoTRA") + " = " + Campos.campos.getAtributo("codProcedimientoCRO")
                + " AND " + Campos.campos.getAtributo("codTramiteTRA") + " = " + Campos.campos.getAtributo("codTramiteCRO")
                + " ORDER BY "+ Campos.campos.getAtributo("fechaInicioCRO") +" desc, " + Campos.campos.getAtributo("fechaFinCRO") + " desc";


        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        int i = 0;
        while (rs.next()) {
            CROTramiteVO traVO = new CROTramiteVO();
            traVO.setNombre(rs.getString(Campos.campos.getAtributo("valorTML").toString()));
            traVO.setAno(rs.getString(Campos.campos.getAtributo("anoCRO").toString()));
            traVO.setCodMunicipio(rs.getString(Campos.campos.getAtributo("codMunicipioCRO").toString()));
            traVO.setCodProcedimiento(rs.getString(Campos.campos.getAtributo("codProcedimientoCRO").toString()));
            traVO.setCodTramite(rs.getString(Campos.campos.getAtributo("codTramiteCRO").toString()));
            traVO.setCodUnidadTramitadora(rs.getString(Campos.campos.getAtributo("codUnidadTramitadoraCRO").toString()));
            traVO.setCodUsuario(rs.getString(Campos.campos.getAtributo("codUsuarioCRO").toString()));
            traVO.setFechaFin(toCalendar(rs.getString("txtfechaFinCRO")));
            traVO.setFechaFinPlazo(toCalendar(rs.getString("txtFechaFinPlazoCRO")));
            traVO.setFechaInicio(toCalendar(rs.getString("txtFechaInicioCRO")));
            traVO.setFechaLimite(toCalendar(rs.getString("txtFechaLimiteCRO")));
            traVO.setNumero(rs.getString(Campos.campos.getAtributo("numeroCRO").toString()));
            traVO.setObservaciones(rs.getString(Campos.campos.getAtributo("observacionesCRO").toString()));
            traVO.setOcurrencia(rs.getString(Campos.campos.getAtributo("ocurrenciaCRO").toString()));
            traVO.setResolucion(rs.getString(Campos.campos.getAtributo("resolucionCRO").toString()));
            traVO.setUsuarioFinalizacion(rs.getString(Campos.campos.getAtributo("usuarioFinalizacionCRO").toString()));
            traVO.setNombreUnidadTramitadora(rs.getString(Campos.campos.getAtributo("nombreUOR").toString()));
            String txtVisible = rs.getString(Campos.campos.getAtributo("visibleInternetTRA").toString());
            Boolean bVisible = ((txtVisible.equals("0"))?Boolean.FALSE:Boolean.TRUE);
            traVO.setVisibleInternet(bVisible);
            salida.add(traVO);
            i++;
        }
        if(rs != null) rs.close();
        if(st != null) st.close();
        return salida;
    }

    private Calendar toCalendar(String txtfecha)
        throws ParseException {
        if ((txtfecha != null) && (txtfecha.length() > 0)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(patronFechaJava);
            Calendar date = Calendar.getInstance();
            date.clear();
            date.setTime(dateFormat.parse(txtfecha));
            return date;
        }else{
            return null;
        }
    }

    public String getExisteTermiteAbierto(String num_expediente, String procedimiento, String municipio,
                                          String ejercicio, String tramite, String[] params)
            throws SQLException, BDException {

        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);

        Connection con = oad.getConnection();

        String sql = "SELECT " + cro_ocu
                + " FROM E_CRO "
                + " WHERE " + cro_mun + "=" + municipio
                + " AND " + cro_pro + "='" + procedimiento + "'"
                + " AND " + cro_eje + "=" + ejercicio
                + " AND " + cro_num + "='" + num_expediente + "'"
                + " AND " + cro_tra + "=" + tramite
                + " AND " + cro_fef + "  is null";

        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(sql);

        if (rs.next()) {
            String resul = rs.getString(cro_ocu);
            rs.close();
            stm.close();
            con.close();
            return resul;
        }
        rs.close();
        stm.close();
        con.close();
        return null;


    }

    public void cambioEstado(String estado, String num_expediente, String procedimiento, String municipio,
                             String ejercicio, String tramite, String observaciones, String ocurrencia,
                             String[] params)
            throws SQLException, BDException {


        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);

        Connection con = oad.getConnection();

        String sql = "UPDATE  e_cro "
                + " SET " + cro_obs + "= '" + observaciones + "'";
        if (estado.equals("C"))
            sql += " , " + cro_fef + " = "+oad.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null);


        sql += " WHERE " + cro_mun + "=" + municipio
                + " AND " + cro_pro + "='" + procedimiento + "'"
                + " AND " + cro_eje + "=" + ejercicio
                + " AND " + cro_num + "='" + num_expediente + "'"
                + " AND " + cro_tra + "=" + tramite
                + " AND " + cro_ocu + "=" + ocurrencia
                + " AND " + cro_fef + "  is null";

        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        Statement stm = con.createStatement();
        stm.executeUpdate(sql);
        stm.close();
        con.close();


    }

    public int setDatoNumerico(String codMunicipio, String ejercicio, String numeroExpediente, String tramite,
                               String ocurrencia, String procedimiento, String codDato, String valorDato,Connection con)
        throws SQLException{

        Statement stmt = null;
        String sql = null;
        ResultSet rs = null;
        String gestor = ConfigServiceHelper.getConfig("techserver").getString("CON.gestor");
        String parametros[] = {gestor};
        //parametros[0] = gestor;
        AdaptadorSQLBD abd = new AdaptadorSQLBD (parametros);
        int resultadoInsertar = 0;

          sql = "DELETE FROM E_TNUT "
                  + " WHERE " + Campos.campos.getAtributo("codMunicipioTNUT") + "=" + codMunicipio
                  + " AND " + Campos.campos.getAtributo("codProcedimientoTNUT") + "='" + procedimiento +"'"
                  + " AND " + Campos.campos.getAtributo("ejercicioTNUT") + "='" + ejercicio+ "'"
                  + " AND " + Campos.campos.getAtributo("numeroExpedienteTNUT") + "='" + numeroExpediente + "'"
                  + " AND " + Campos.campos.getAtributo("codTramiteTNUT") + "='" + tramite+"'"
                  + " AND " + Campos.campos.getAtributo("ocurrenciaTNUT") + "=" + ocurrencia
                  + " AND " + Campos.campos.getAtributo("codCampoTNUT") + "='" + codDato+"'";
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);

          stmt = con.createStatement();
          stmt.executeUpdate(sql);

          if(valorDato != null && !"".equals(valorDato)) {
            sql = "INSERT INTO E_TNUT ( " + Campos.campos.getAtributo("codMunicipioTNUT") + ","
                    + Campos.campos.getAtributo("codProcedimientoTNUT") + ","
                    + Campos.campos.getAtributo("ejercicioTNUT") + ","
                    + Campos.campos.getAtributo("numeroExpedienteTNUT") + ","
                    +  Campos.campos.getAtributo("codTramiteTNUT")+ ","
                    +  Campos.campos.getAtributo("codCampoTNUT")+ ","
                    +  Campos.campos.getAtributo("ocurrenciaTNUT")+ ","
                    +  Campos.campos.getAtributo("valorTNUT")+ ") VALUES ("
                    + codMunicipio + ",'" + procedimiento + "'," + ejercicio + ",'" + numeroExpediente + "','"
                    + tramite + "','" + codDato + "'," + ocurrencia +"," +
                    abd.convertir("'"+valorDato+"'", AdaptadorSQLBD.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
              m_Log.debug(sql);
            resultadoInsertar = stmt.executeUpdate(sql);
          } else {
            resultadoInsertar = 1;
          }
        stmt.close();

      return resultadoInsertar;
    }

    public int setDatoTexto(String codMunicipio, String ejercicio, String numeroExpediente, String tramite,
                            String ocurrencia, String procedimiento, String codDato, String valorDato,Connection con)
        throws SQLException{

        Statement stmt = null;
        String sql = null;

        int resultadoInsertar = 0;

          sql = "DELETE FROM E_TXTT "
                  + " WHERE " + Campos.campos.getAtributo("codMunicipioTXTT") + "=" + codMunicipio
                  + " AND " + Campos.campos.getAtributo("codProcedimientoTXTT") + "='" + procedimiento+"'"
                  + " AND " + Campos.campos.getAtributo("ejercicioTXTT") + "=" + ejercicio
                  + " AND " + Campos.campos.getAtributo("numeroExpedienteTXTT") + "='" + numeroExpediente + "'"
                  + " AND " + Campos.campos.getAtributo("codTramiteTXTT") + "='" + tramite+"'"
                  + " AND " + Campos.campos.getAtributo("ocurrenciaTXTT") + "=" + ocurrencia
                  + " AND " + Campos.campos.getAtributo("codCampoTXTT") + "='" + codDato+"'";
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          m_Log.debug(sql);
          stmt = con.createStatement();
          stmt.executeUpdate(sql);

          if(valorDato != null && !"".equals(valorDato)) {
            sql = "INSERT INTO E_TXTT ( " + Campos.campos.getAtributo("codMunicipioTXTT") + ","
                    + Campos.campos.getAtributo("codProcedimientoTXTT") + ","
                    + Campos.campos.getAtributo("ejercicioTXTT") + ","
                    + Campos.campos.getAtributo("numeroExpedienteTXTT") + ","
                    +  Campos.campos.getAtributo("codTramiteTXTT")+ ","
                    +  Campos.campos.getAtributo("codCampoTXTT") + ","
                    +  Campos.campos.getAtributo("ocurrenciaTXTT")+ ","
                    +  Campos.campos.getAtributo("valorTXTT")+ ") VALUES ("
                    + codMunicipio + ",'" + procedimiento + "'," + ejercicio + ",'" + numeroExpediente + "','"
                    + tramite + "','" + codDato + "'," + ocurrencia +",'" + valorDato + "')";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
              m_Log.debug(sql);
            resultadoInsertar = stmt.executeUpdate(sql);
          } else {
            resultadoInsertar = 1;
          }
        stmt.close();
      return resultadoInsertar;
    }


    public int setDatoFecha(String codMunicipio, String ejercicio, String numeroExpediente, String tramite,
                            String ocurrencia, String procedimiento, String codDato, String valorDato,String mascara,
                            Connection con)
        throws SQLException{

        Statement stmt = null;
        String sql = null;
        ResultSet rs = null;
        String gestor = ConfigServiceHelper.getConfig("techserver").getString("CON.gestor");        
        String parametros[] = {gestor};
        AdaptadorSQLBD abd = new AdaptadorSQLBD (parametros);
        int resultadoInsertar = 0;

          sql = "DELETE FROM E_TFET "
                  + " WHERE " + Campos.campos.getAtributo("codMunicipioTFET") + "=" + codMunicipio
                  + " AND " + Campos.campos.getAtributo("codTramiteTFET") + "=" + tramite
                  + " AND " + Campos.campos.getAtributo("ejercicioTFET") + "=" + ejercicio
                  + " AND " + Campos.campos.getAtributo("numeroExpedienteTFET") + "='" + numeroExpediente + "'"
                  + " AND " + Campos.campos.getAtributo("codProcedimientoTFET") + "='" + procedimiento+"'"
                  + " AND " + Campos.campos.getAtributo("ocurrenciaTFET") + "=" + ocurrencia
                  + " AND " + Campos.campos.getAtributo("codCampoTFET") + "='" + codDato+"'";

          if(m_Log.isDebugEnabled()) m_Log.debug(sql);

          stmt = con.createStatement();
          stmt.executeUpdate(sql);

          if(valorDato != null && !"".equals(valorDato)) {
            if(mascara == null || "".equals(mascara)) {
              sql = "INSERT INTO E_TFET ( "+ Campos.campos.getAtributo("codMunicipioTFET") + ","
                    + Campos.campos.getAtributo("codProcedimientoTFET") + ","
                    + Campos.campos.getAtributo("ejercicioTFET") + ","
                    + Campos.campos.getAtributo("numeroExpedienteTFET") + ","
                    +  Campos.campos.getAtributo("codTramiteTFET")+ ","
                    +  Campos.campos.getAtributo("codCampoTFET")+ ","
                    +  Campos.campos.getAtributo("ocurrenciaTFET")+ ","
                    +  Campos.campos.getAtributo("valorTFET")+ ") VALUES ("
                    + codMunicipio + ",'" + procedimiento + "'," + ejercicio + ",'" + numeroExpediente + "','"
                    + tramite + "','" + codDato + "'," + ocurrencia +", " +
                    abd.convertir("'"+valorDato+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") + ")";
            } else {
              sql = "INSERT INTO E_TFET ( "+ Campos.campos.getAtributo("codMunicipioTFET") + ","
                    + Campos.campos.getAtributo("codProcedimientoTFET") + ","
                    + Campos.campos.getAtributo("ejercicioTFET") + ","
                    + Campos.campos.getAtributo("numeroExpedienteTFET") + ","
                    +  Campos.campos.getAtributo("codTramiteTFET")+ ","
                    +  Campos.campos.getAtributo("codCampoTFET")+ ","
                    +  Campos.campos.getAtributo("ocurrenciaTFET")+ ","
                    +  Campos.campos.getAtributo("valorTFET")+ ") VALUES ("
                    + codMunicipio + ",'" + procedimiento + "'," + ejercicio + ",'" + numeroExpediente + "','"
                    + tramite + "'," + codDato + ","+ ocurrencia + "'," +
                    abd.convertir("'"+valorDato+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,mascara) + ")";
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("Dato fecha de tramite grabado"+sql);
              m_Log.debug(sql);
            resultadoInsertar = stmt.executeUpdate(sql);
          } else {
            resultadoInsertar = 1;
          }

      stmt.close();
      return resultadoInsertar;
    }

    public int setDatoTextoLargo (String codMunicipio, String ejercicio, String numeroExpediente, String tramite,
                                  String ocurrencia, String procedimiento, String codDato, String valorDato,Connection con)
        throws SQLException {

        String sql = null;
        Statement stmt;
        int resultadoInsertar = 0;

          sql = "DELETE FROM E_TTLT "
                  + " WHERE " + Campos.campos.getAtributo("codMunicipioTTLT") + "=" + codMunicipio
                  + " AND " + Campos.campos.getAtributo("codProcedimientoTTLT") + "='" + procedimiento + "'"
                  + " AND " + Campos.campos.getAtributo("ejercicioTTLT") + "=" + ejercicio
                  + " AND " + Campos.campos.getAtributo("numeroExpedienteTTLT") + "='" + numeroExpediente + "'"
                  + " AND " + Campos.campos.getAtributo("codTramiteTTLT") + "='" + tramite+"'"
                  + " AND " + Campos.campos.getAtributo("ocurrenciaTTLT") + "=" + ocurrencia
                  + " AND " + Campos.campos.getAtributo("codCampoTTLT") + "='" + codDato+"'";

          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        m_Log.debug(sql);
          stmt = con.createStatement();
          stmt.executeUpdate(sql);

          if(valorDato != null && !"".equals(valorDato)) {
            sql = "INSERT INTO E_TTLT ( "+ Campos.campos.getAtributo("codMunicipioTTLT") + ","
                  	+ Campos.campos.getAtributo("codProcedimientoTTLT") + ","
                    + Campos.campos.getAtributo("ejercicioTTLT") + ","
                    + Campos.campos.getAtributo("numeroExpedienteTTLT") + ","
                    +  Campos.campos.getAtributo("codTramiteTTLT") + ","
                    +  Campos.campos.getAtributo("codCampoTTLT")+ ","
                    +  Campos.campos.getAtributo("ocurrenciaTTLT") + ","
                    +  Campos.campos.getAtributo("valorTTLT")+ ") VALUES ("
                    + codMunicipio + ",'" + procedimiento + "'," + ejercicio + ",'" + numeroExpediente + "','"
                    + tramite + "','" + codDato + "'," + ocurrencia +",'" + valorDato + "')";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
              m_Log.debug(sql);
            stmt = con.createStatement();
            resultadoInsertar = stmt.executeUpdate(sql);
          } else {
            resultadoInsertar = 1;
          }
      stmt.close();
      return resultadoInsertar;
    }


    /**
     *  Grabar un campo suplementario de tipo desplegable asociado a un trámite
     * @param codMunicipio :Código del municipio
     * @param ejercicio: Ejercicio
     * @param numeroExpediente: Nº de expediente
     * @param tramite: Código del trámite
     * @param ocurrencia: Ocurrencia
     * @param procedimiento: Código procedimiento
     * @param codDato: Código del campo suplementario
     * @param valorDato: Valor del campo suplementario
     * @param con: Conexión a la base de datos
     * @return Un int
     * @throws java.sql.SQLException
     */
     public int setDatoDesplegable(String codMunicipio, String ejercicio, String numeroExpediente, String tramite,
                            String ocurrencia, String procedimiento, String codDato, String valorDato,Connection con)
        throws SQLException{

        PreparedStatement ps = null;
        String sql = null;

        int resultadoInsertar = 0;

          sql = "DELETE FROM E_TDET "
                  + " WHERE TDET_MUN=" + codMunicipio
                  + " AND TDET_PRO='" + procedimiento + "'"
                  + " AND TDET_EJE=" + ejercicio
                  + " AND TDET_NUM='" + numeroExpediente + "'"
                  + " AND TDET_TRA=" + tramite
                  + " AND TDET_OCU=" + ocurrencia
                  + " AND TDET_COD='" + codDato+ "'";

          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          m_Log.debug(sql);
          ps = con.prepareStatement(sql);
          int rowsDeleted = ps.executeUpdate();

          m_Log.debug("Se ha eliminado: " + rowsDeleted);

          m_Log.debug("Codigo del dato: " + codDato);
          m_Log.debug("Valor del dato: " + valorDato);
          if(valorDato != null && !"".equals(valorDato)) {
            sql = "INSERT INTO E_TDET (TDET_MUN,TDET_PRO,TDET_EJE,TDET_NUM,TDET_TRA,TDET_OCU,TDET_COD,TDET_VALOR) " +
                  "VALUES(?,?,?,?,?,?,?,?)";
            
            ps = con.prepareStatement(sql);
            int i=1;
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setString(i++,procedimiento);
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numeroExpediente);
            ps.setInt(i++,Integer.parseInt(tramite));            
            ps.setInt(i++,Integer.parseInt(ocurrencia));
            ps.setString(i++,codDato);
            ps.setString(i++,valorDato);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);            
            resultadoInsertar = ps.executeUpdate();
          } else {
            resultadoInsertar = 1;
          }

        ps.close();
      return resultadoInsertar;
    }



}
