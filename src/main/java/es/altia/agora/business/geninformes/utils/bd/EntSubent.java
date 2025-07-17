package es.altia.agora.business.geninformes.utils.bd;
import es.altia.agora.business.geninformes.utils.Utilidades;
import es.altia.util.conexion.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Vector;

public final class EntSubent {
    private java.sql.Connection con = null;
    private String[] _params = null;
    protected static Log m_Log =
                LogFactory.getLog(EntSubent.class.getName());

    public EntSubent(java.sql.Connection con) {
        this.con = con;
    }

    public es.altia.util.conexion.Cursor ConsultaSubentidadesConCamposJoin(
        es.altia.util.HashtableWithNull usuario)
        throws Exception {

        es.altia.util.conexion.Cursor datosResultado = null;

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        if (con == null)
            con = adapBD.getConnection();

        // Definir consulta.

        String parteFrom =
            " ESE.COD_ENTSUBENT,ESE2.COD_ENTIDADEINFORME AS ENT,ESE.COD_ENTIDADEINFORME AS SUBENT,EI.NOME AS NOME,CJ.CAMPO_ENT AS CAMPO_ENT,CJ.CAMPO_SUBENT AS CAMPO_SUBENT,CJ.OUTER_JOIN AS OUTER_JOIN ";
        String parteWhere = "ESE2.COD_ENTIDADEINFORME = "
            + usuario.get("COD_ENTIDADEINFORME");

        ArrayList join = new ArrayList();
        join.add("ENTSUBENT ESE");
        join.add("INNER");
        join.add("ENTSUBENT ESE2");
        join.add("ESE.COD_PAI = ESE2.COD_ENTSUBENT");
        join.add("INNER");
        join.add("ENTIDADEINFORME EI");
        join.add("ESE.COD_ENTIDADEINFORME=EI.CODIGO");
        join.add("LEFT");
        join.add("CAMPOSJOIN CJ");
        join.add("ESE.COD_ENTSUBENT = CJ.COD_ESE");
        join.add("false");

        String consulta = adapBD.join(parteFrom, parteWhere, (String[]) join.toArray(new String[]{})) + " ORDER BY 3 ";
        // Ordeno por COD de Subent para que coincida con el order by de

        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);

        // Ejecutar la consulta.

        rs = pstmt.executeQuery();

        // Procesar el resultado de la consulta.

        datosResultado =
            new es.altia.util.conexion.Cursor(
                rs,
                rs.getMetaData().getColumnCount());

        pstmt.close();
        //adapBD.devolverConexion(con);

        return datosResultado;

    }

    public es.altia.util.conexion.Cursor ConsultaCamposJoinEntSubent(
        es.altia.util.HashtableWithNull usuario)
        throws Exception {

        m_Log.debug("En ConsultaCamposJoinEntSubent");
        es.altia.util.conexion.Cursor datosResultado = null;

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        if (con == null)
            con = adapBD.getConnection();

        // Definir consulta.

        String consulta =
            "select ESE.COD_ENTSUBENT,ESE.COD_PAI AS ENT,ESE.COD_ENTIDADEINFORME AS SUBENT,CJ.CAMPO_ENT AS CAMPO_ENT,CJ.CAMPO_SUBENT AS CAMPO_SUBENT,CJ.OUTER_JOIN AS OUTER_JOIN ";
        consulta += " FROM ENTSUBENT ESE,CAMPOSJOIN CJ,ENTSUBENT ESE2";
        consulta += " WHERE ESE.COD_PAI = ESE2.COD_ENTSUBENT ";
        consulta += " AND ESE2.COD_ENTIDADEINFORME = " + usuario.get("ENT");
        consulta += " AND ESE.COD_ENTIDADEINFORME = " + usuario.get("SUBENT");
        consulta += " AND ESE.COD_ENTSUBENT = CJ.COD_ESE ";

        //consulta += " ORDER BY 3 "; // Ordeno por COD de Subent para que coincida con el order by de

        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);

        // Ejecutar la consulta.

        rs = pstmt.executeQuery();

        // Procesar el resultado de la consulta.

        datosResultado =
            new es.altia.util.conexion.Cursor(
                rs,
                rs.getMetaData().getColumnCount());

        pstmt.close();

        if (con!=null) adapBD.devolverConexion(con);

        return datosResultado;

    }

    public Long AltaEntSubent(es.altia.util.HashtableWithNull usuario)
        throws Exception {

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        boolean eraNull = false;
        if (con == null) {
            eraNull = true;
            con = adapBD.getConnection();
        }
        Long salida = null;

        String codEntidadeInforme =
            ((usuario.get("COD_ENTIDADEINFORME") != null)
                && (!(((usuario.get("COD_ENTIDADEINFORME"))
                    .toString()
                    .equals("")))))
                ? usuario.get("COD_ENTIDADEINFORME").toString()
                : "null";

        String codPai =
            ((usuario.get("COD_PAI") != null)
                && (!(((usuario.get("COD_PAI")).toString().equals("")))))
                ? usuario.get("COD_PAI").toString()
                : "null";

        String txtCondJoin =
            ((usuario.get("TXTCONDJOIN") != null)
                && (!(((usuario.get("TXTCONDJOIN")).toString().equals("")))))
                ? usuario.get("TXTCONDJOIN").toString()
                : null;

        String codEse = null;

        // Definir consulta.
        String consultaPrevia =
            "SELECT COD_ENTSUBENT FROM ENTSUBENT WHERE COD_ENTIDADEINFORME="
                + codEntidadeInforme
                + " AND COD_PAI IS NULL";
                //Aquí consulto la ESE que he creado anteriormente
        m_Log.debug("consultaPrevia: "+consultaPrevia);
        pstmt = con.prepareStatement(consultaPrevia);
        rs = pstmt.executeQuery();
        if (rs.next()) {
            codEse = rs.getString("COD_ENTSUBENT");
            m_Log.debug("codEse que devuelve la consulta: "+codEse);
            salida = new Long(codEse);
        }
        pstmt.close();

        String consulta = null;
        if (codEse == null) {
            long temp = adapBD.devolverNextValSecuencia(_params, "CONTENTSUBENT");
            consulta = "insert ";
            consulta
                += " into ENTSUBENT (COD_ENTSUBENT,COD_ENTIDADEINFORME, COD_PAI,TXT_CONDJOIN) ";
            consulta += " values (?,"
                + codEntidadeInforme
                + ","
                + codPai
                + ",? ) ";

            pstmt = con.prepareStatement(consulta);
            m_Log.debug("consulta: "+consulta);
            m_Log.debug("nuevo COD_ENTSUBENT: "+temp);

            pstmt.setLong(1, temp);
            if (txtCondJoin != null) {
                pstmt.setString(2, txtCondJoin);
            } else
                pstmt.setNull(2, java.sql.Types.VARCHAR);

            pstmt.executeUpdate();
            // Ejecutar la consulta.

//			long temp = Utilidades.insertar(pstmt, "CONTENTSUBENT");

            salida = new Long(temp);
            pstmt.close();
        } // Añadido 26/01/2004
        else {
            consulta = "update ";
            consulta += " ENTSUBENT set COD_PAI= " + codPai;
            consulta += " where COD_ENTSUBENT= " + codEse;

            pstmt = con.prepareStatement(consulta);
            m_Log.debug("consulta: "+consulta);

            // Ejecutar la consulta.

            //long temp = Utilidades.insertar(pstmt, "CONTENTSUBENT");
            pstmt.executeUpdate();

            //salida = new Long(temp);
            pstmt.close();
        } // Fin Añadido 26/01/2004

        if (eraNull)
            adapBD.devolverConexion(con);
        return salida;

    }

    public void AltaCampoJoinEntSubent(es.altia.util.HashtableWithNull usuario)
        throws Exception {

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        boolean eraNull = false;
        if (con == null) {
            eraNull = true;
            con = adapBD.getConnection();
        }
        Long salida = null;

        String campoEnt =
            ((usuario.get("CAMPO_ENT") != null)
                && (!(((usuario.get("CAMPO_ENT")).toString().equals("")))))
                ? "'" + usuario.get("CAMPO_ENT").toString() + "'"
                : "null";

        String codEse =
            ((usuario.get("COD_ESE") != null)
                && (!(((usuario.get("COD_ESE")).toString().equals("")))))
                ? usuario.get("COD_ESE").toString()
                : "null";

        String campoSubent =
            ((usuario.get("CAMPO_SUBENT") != null)
                && (!(((usuario.get("CAMPO_SUBENT")).toString().equals("")))))
                ? "'" + usuario.get("CAMPO_SUBENT").toString() + "'"
                : "null";

        String outerJoin =
            ((usuario.get("OUTER_JOIN") != null)
                && (!(((usuario.get("OUTER_JOIN")).toString().equals("")))))
                ? usuario.get("OUTER_JOIN").toString()
                : null;

        // Definir consulta.

        String consulta = "insert ";
        consulta
            += " into CAMPOSJOIN (COD_ESE,CAMPO_ENT,CAMPO_SUBENT,OUTER_JOIN) ";
        consulta += " values ("
            + codEse
            + ","
            + campoEnt
            + ","
            + campoSubent
            + ",?) ";

        m_Log.debug("outerJoin es:" + outerJoin);

        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);
        if (outerJoin == null)
            pstmt.setNull(1, java.sql.Types.VARCHAR);
        else
            pstmt.setString(1, outerJoin);
        // Ejecutar la consulta.

        pstmt.executeUpdate();

        pstmt.close();
        if (eraNull)
            adapBD.devolverConexion(con);

    }

    public void BorraEntSubentHijas(es.altia.util.HashtableWithNull usuario)
        throws Exception {

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;
        Vector cods_entsubent = new Vector();
        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        boolean eraNull = false;
        if (con == null) {
            eraNull = true;
            con = adapBD.getConnection();
        }
        EntidadeInforme entidadeInforme = new EntidadeInforme(con);

        String codEse =
            ((usuario.get("COD_ESE") != null)
                && (!(((usuario.get("COD_ESE")).toString().equals("")))))
                ? usuario.get("COD_ESE").toString()
                : "null";

        // Definir consulta.
        String consulta = "select cod_entsubent from ENTSUBENT where cod_pai=" + codEse;
        m_Log.debug("Consulta en BorraEntSubentHijas: "+consulta);
        pstmt = con.prepareStatement(consulta);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            cods_entsubent.add(rs.getString("cod_entsubent"));
        }
        rs.close();
        pstmt.close();
        for (int i=0;i<cods_entsubent.size();i++) {
            entidadeInforme.BorrarDependencias((String)cods_entsubent.elementAt(i));
        }
        consulta = "delete from ENTSUBENT where COD_PAI=" + codEse;
        m_Log.debug("Consulta en BorraEntSubentHijas: "+consulta);
        pstmt = con.prepareStatement(consulta);
        pstmt.executeUpdate();
        pstmt.close();
        if (eraNull)
            adapBD.devolverConexion(con);

    }

    public void BorraCamposJoinEse(es.altia.util.HashtableWithNull usuario)
        throws Exception {

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        boolean eraNull = false;
        if (con == null) {
            eraNull = true;
            con = adapBD.getConnection();
        }

        String codEse =
            ((usuario.get("COD_ESE") != null)
                && (!(((usuario.get("COD_ESE")).toString().equals("")))))
                ? usuario.get("COD_ESE").toString()
                : "null";

        // Definir consulta.

        String consulta = "delete from  ";
        consulta += " CAMPOSJOIN where ";
        consulta += " COD_ESE=" + codEse;

        m_Log.debug("Consulta:"+consulta);

        pstmt = con.prepareStatement(consulta);

        // Ejecutar la consulta.

        pstmt.executeUpdate();

        pstmt.close();
        if (eraNull)
            adapBD.devolverConexion(con);

    }

}