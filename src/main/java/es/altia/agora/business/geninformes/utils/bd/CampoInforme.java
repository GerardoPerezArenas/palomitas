package es.altia.agora.business.geninformes.utils.bd;

import es.altia.agora.business.geninformes.utils.Utilidades;
import es.altia.util.conexion.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Vector;
import java.sql.ResultSet;

public final class CampoInforme {
    private java.sql.Connection con = null;
    private String[] _params = null;
    protected static Log m_Log =
                LogFactory.getLog(CampoInforme.class.getName());

    public CampoInforme(java.sql.Connection con) {
        this.con = con;
    }

    public es.altia.util.conexion.Cursor ConsultaCamposInforme(
        es.altia.util.HashtableWithNull usuario)
        throws Exception {

        es.altia.util.conexion.Cursor datosResultado = null;

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        boolean eraNull = false;
        if (con == null) {
            con = adapBD.getConnection();
            eraNull = true;
        }
        // Definir consulta.

        String consulta =
            "select CI.CODIGO AS CODIGO,CI.NOME AS NOME,CI.CAMPO AS CAMPO,CI.TIPO AS TIPO,CI.LONXITUDE AS LONXITUDE,CI.NOMEAS AS NOMEAS ";
        consulta
            += " FROM CAMPOINFORME CI,ENTIDADEINFORME EI, CAMPOENTIDADEINFORME CEI ";
        consulta += " WHERE EI.CODIGO  =" + usuario.get("COD_ENTIDADEINFORME");
        consulta += " AND EI.CODIGO=CEI.COD_ENTIDADEINFORME ";
        consulta += " AND CI.CODIGO=CEI.COD_CAMPOINFORME ";

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
        if (eraNull)
            adapBD.devolverConexion(con);

        return datosResultado;

    }

    public es.altia.util.conexion.Cursor ConsultaEntidadeInforme(
        es.altia.util.HashtableWithNull usuario)
        throws Exception {

        es.altia.util.conexion.Cursor datosResultado = null;

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        boolean eraNull = false;
        if (con == null) {
            con = adapBD.getConnection();
            eraNull = true;
        }
        // Definir consulta.

        String consulta =
            "select EI.CODIGO AS CODIGO,EI.NOME AS NOME,IX.NOMEVISTA AS NOMEVISTA ";
        consulta += " FROM ENTIDADEINFORME EI";
        consulta += " WHERE EI.CODIGO = " + usuario.get("COD_ENTIDADEINFORME");

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
        if (eraNull)
            adapBD.devolverConexion(con);

        return datosResultado;

    }

    public es.altia.util.conexion.Cursor ConsultaInformeXeradorAdaptada(
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

        String consulta =
            "select IX.CODIGO AS CODIGO,IX.APL_COD AS APL_COD,IX.NOME AS NOME,IX.DESCRIPCION as DESCRIPCION,IX.FORMATO AS FORMATO,'CONSULTASQL',ESTI.COD_ENTIDADEINFORME AS COD_ENTIDADEINFORME,'COD_FUNCION' ";
        consulta += " FROM INFORMEXERADOR IX,ESTRUCTURAINFORME ESTI";
        consulta += " WHERE IX.CODIGO = " + usuario.get("COD_INFORMEXERADOR");
        consulta += " AND IX.COD_ESTRUCTURA=ESTI.COD_ESTRUCTURA ";
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
    public Long AltaCampoInformeEntidade(
        es.altia.util.HashtableWithNull usuario)
        throws Exception {

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        //con = adapBD.getConnection();

        Long salida = null;

        String nome =
            ((usuario.get("NOME") != null)
                && (!((((String) usuario.get("NOME")).equals("")))))
                ? (usuario.get("NOME").toString())
                : null;

        String campo =
            ((usuario.get("CAMPO") != null)
                && (!((((String) usuario.get("CAMPO")).equals("")))))
                ? (usuario.get("CAMPO").toString())
                : null;
        String nomeAs =
            ((usuario.get("NOMEAS") != null)
                && (!((((String) usuario.get("NOMEAS")).equals("")))))
                ? (usuario.get("NOMEAS").toString())
                : null;
        String tipo =
            ((usuario.get("TIPO") != null)
                && (!((((String) usuario.get("TIPO")).equals("")))))
                ? "'" + (usuario.get("TIPO").toString()) + "'"
                : "null";
        String lonxitude =
            ((usuario.get("LONXITUDE") != null)
                && (!(((usuario.get("LONXITUDE")).toString().equals("")))))
                ? (usuario.get("LONXITUDE").toString())
                : null;

        String nomeVista =
            (usuario.get("NOMEVISTA") != null)
                ? (usuario.get("NOMEVISTA").toString().trim())
                : "null";

        long temp = adapBD.devolverNextValSecuencia(_params, "CONTCAMPOINFORME");
        //
        // Al crear el informe siempre se mete 'N' en PLT_EDITADA
        // porque la plantilla se edita después
        //
        String consulta = "insert ";
        consulta
            += " into CAMPOINFORME ( CODIGO, NOME,CAMPO,TIPO,LONXITUDE,NOMEAS) ";
        consulta += " values (?,?,?," + tipo + "," + lonxitude + ",?)";

        m_Log.debug(
            "consulta:"
                + consulta
                + ".nome:"
                + nome
                + ".des:"
                + nomeVista
                + ".");
        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);
        pstmt.setLong(1, temp);
        pstmt.setString(2, nome);
        pstmt.setString(3, campo);
        pstmt.setString(4, nomeAs);
        // Ejecutar la consulta.
        pstmt.executeUpdate();

//		long temp = Utilidades.insertar(pstmt, "CONTCAMPOINFORME");

        salida = new Long(temp);

        pstmt.close();

        consulta =
            "insert into CAMPOENTIDADEINFORME (COD_ENTIDADEINFORME,COD_CAMPOINFORME) values ";
        consulta += "(" + usuario.get("COD_ENTIDADEINFORME") + "," + temp + ")";

        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);
        pstmt.executeUpdate();
        pstmt.close();

        //adapBD.devolverConexion(con);
        return salida;

    }

    public void AltaEntidadeAplicacion(es.altia.util.HashtableWithNull usuario)
        throws Exception {

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        boolean eraNull = true;
        if (con == null) {
            eraNull = true;
            con = adapBD.getConnection();
        }

        Long salida = null;

        String aplCod =
            ((usuario.get("APL_COD") != null)
                && (!(((usuario.get("APL_COD")).toString().equals("")))))
                ? (usuario.get("APL_COD").toString())
                : "null";
        String codEntidadeInforme =
            (usuario.get("COD_ENTIDADEINFORME") != null)
                ? (usuario.get("COD_ENTIDADEINFORME").toString().trim())
                : "null";

        //
        // Al crear el informe siempre se mete 'N' en PLT_EDITADA
        // porque la plantilla se edita después
        //
        String consulta = "insert ";
        consulta += " into ENTIDADEINFORME ( APL_COD, COD_ENTIDADEINFORME) ";
        consulta += " values (" + aplCod + "," + codEntidadeInforme + ")";

        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);
        // Ejecutar la consulta.

        pstmt.executeUpdate();

        pstmt.close();
        if (eraNull)
            adapBD.devolverConexion(con);

    }

    public void BorraEntidadeInforme(es.altia.util.HashtableWithNull usuario)
        throws Exception {

        java.sql.PreparedStatement pstmt = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);
        //con = adapBD.getConnection();

        // Definir consulta.
        String consulta = "delete ";
        consulta += "  from   ENTIDADEINFORME ";
        consulta += " WHERE ENTIDADEINFORME.CODIGO = "
            + usuario.get("COD_ENTIDADEINFORME");

        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);

        // Ejecutar la consulta.

        pstmt.executeUpdate();

        // Procesar el resultado de la consulta.

        pstmt.close();
        //adapBD.devolverConexion(con);

    }

    public void ModificaCampoInforme(es.altia.util.HashtableWithNull usuario)
        throws Exception {
        java.sql.PreparedStatement pstmt = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        //con = adapBD.getConnection();

        String codigo =
            ((usuario.get("CODIGO") != null)
                && (!((((String) usuario.get("CODIGO")).equals("")))))
                ? usuario.get("CODIGO").toString()
                : "null";

        String nome =
            ((usuario.get("NOME") != null)
                && (!((((String) usuario.get("NOME")).equals("")))))
                ? (usuario.get("NOME").toString())
                : null;

        String campo =
            (usuario.get("CAMPO") != null)
                ? (usuario.get("CAMPO").toString())
                : null;
        String tipo =
            (usuario.get("TIPO") != null)
                ? (usuario.get("TIPO").toString())
                : null;
        String lonxitude =
            (usuario.get("LONXITUDE") != null)
                ? (usuario.get("LONXITUDE").toString())
                : null;

        String nomeAs =
            (usuario.get("NOMEAS") != null)
                ? (usuario.get("NOMEAS").toString())
                : null;

        // Definir consulta.
        String consulta = "update CAMPOINFORME set NOME= '"+nome+"'," + "CAMPO = '"+campo+"',";
        consulta += " TIPO = '"+tipo+"',";
        consulta += " LONXITUDE = "+lonxitude+",";
        consulta += " NOMEAS = '"+nomeAs+"'";
        consulta += " WHERE CODIGO = " + codigo;

        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);
        // Ejecutar la consulta.

        pstmt.executeUpdate();

        // Procesar el resultado de la consulta.

        pstmt.close();
        //adapBD.devolverConexion(con);

    }

    public void EliminaCamposInforme(Long codEntidade)
        throws Exception {
        java.sql.PreparedStatement pstmt = null;

        Vector cods_campoInforme = new Vector();

        String consulta = "select cod_campoinforme from CAMPOENTIDADEINFORME where cod_entidadeinforme="+codEntidade;
        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            cods_campoInforme.add(rs.getString("cod_campoinforme"));
        }
        rs.close();
        pstmt.close();

        for (int i=0;i<cods_campoInforme.size();i++) {
            consulta = "delete from CAMPOINFORME where codigo="+cods_campoInforme.elementAt(i);
            pstmt = con.prepareStatement(consulta);
            m_Log.debug("Consulta: "+consulta);
            pstmt.executeUpdate();
            pstmt.close();
        }
        consulta = "delete from CAMPOENTIDADEINFORME where cod_entidadeinforme="+codEntidade;
        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void ModificaInformeXerador(es.altia.util.HashtableWithNull usuario)
        throws Exception {
        java.sql.PreparedStatement pstmt = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        //con = adapBD.getConnection();

        String codigo =
            ((usuario.get("COD_ENTIDADEINFORME") != null)
                && (!((((String) usuario.get("COD_ENTIDADEINFORME"))
                    .equals("")))))
                ? usuario.get("COD_ENTIDADEINFORME").toString()
                : "null";

        String nome =
            ((usuario.get("NOME") != null)
                && (!((((String) usuario.get("NOME")).equals("")))))
                ? (usuario.get("NOME").toString())
                : "null";
        String nomeVista =
            (usuario.get("NOMEVISTA") != null)
                ? (usuario.get("NOMEVISTA").toString())
                : "null";

        // Definir consulta.
        String consulta = "update ENTIDADEINFORME ";
        consulta += "   set NOME= ?,";
        consulta += "       NOMEVISTA = ? ";
        consulta += " WHERE CODIGO = " + codigo;

        pstmt = con.prepareStatement(consulta);
        pstmt.setString(1, nome);
        pstmt.setString(2, nomeVista);

        // Ejecutar la consulta.

        pstmt.executeUpdate();

        // Procesar el resultado de la consulta.

        pstmt.close();
        //adapBD.devolverConexion(con);

    }

}