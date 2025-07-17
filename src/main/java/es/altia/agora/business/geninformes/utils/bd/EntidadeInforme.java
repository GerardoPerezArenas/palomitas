package es.altia.agora.business.geninformes.utils.bd;
import es.altia.agora.business.geninformes.utils.Utilidades;
import es.altia.agora.business.util.GlobalNames;
import es.altia.util.conexion.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.ResultSet;
import java.util.Vector;

public final class EntidadeInforme {
    private java.sql.Connection con = null;
    private String[] _params = null;
    protected static Log m_Log =
                LogFactory.getLog(EntidadeInforme.class.getName());

    public EntidadeInforme(java.sql.Connection con) {
        this.con = con;
    }

    public es.altia.util.conexion.Cursor ConsultaEntidadesInforme(
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
            "select EI.CODIGO AS CODIGO,EI.NOME AS NOME,EI.NOMEVISTA AS NOMEVISTA ";
        consulta += " FROM ENTIDADEINFORME EI";

        pstmt = con.prepareStatement(consulta);

        // Ejecutar la consulta.

        m_Log.debug("Consulta: "+consulta);
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
            "select EI.CODIGO AS CODIGO,EI.NOME AS NOME,EI.NOMEVISTA AS NOMEVISTA ";
        consulta += " FROM ENTIDADEINFORME EI";
        consulta += " WHERE EI.CODIGO = " + usuario.get("COD_ENTIDADEINFORME");

        pstmt = con.prepareStatement(consulta);

        // Ejecutar la consulta.

        m_Log.debug("Consulta: "+consulta);
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

        // Ejecutar la consulta.

        m_Log.debug("Consulta: "+consulta);
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

    public Long AltaEntidadeInforme(es.altia.util.HashtableWithNull usuario)
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
                : "null";
        String nomeVista =
            (usuario.get("NOMEVISTA") != null)
                ? (usuario.get("NOMEVISTA").toString().trim())
                : "null";

        long temp = adapBD.devolverNextValSecuencia(_params, "CONTENTIDADEINFORME");
        //
        // Al crear el informe siempre se mete 'N' en PLT_EDITADA
        // porque la plantilla se edita después
        //
        String consulta = "insert ";
        consulta += " into ENTIDADEINFORME ( CODIGO, NOME,NOMEVISTA) ";
        consulta += " values (?,?,?)";

        m_Log.debug(
            "consulta:"
                + consulta
                + ".nome:"
                + nome
                + ".des:"
                + nomeVista
                + ".");
        pstmt = con.prepareStatement(consulta);
        pstmt.setLong(1, temp);
        pstmt.setString(2, nome);
        pstmt.setString(3, nomeVista);
        // Ejecutar la consulta.
        m_Log.debug("Consulta: "+consulta);
        pstmt.executeUpdate();

//		long temp = Utilidades.insertar(pstmt, "CONTENTIDADEINFORME");

        salida = new Long(temp);

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

        boolean eraNull = false;
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
        consulta += " into ENTIDADEAPLICACION ( APL_COD, COD_ENTIDADEINFORME) ";
        consulta += " values (" + aplCod + "," + codEntidadeInforme + ")";

        pstmt = con.prepareStatement(consulta);
        // Ejecutar la consulta.

        m_Log.debug("Consulta: "+consulta);
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
        String consulta;
        Vector cod_entsubent = new Vector();
        ResultSet rs;

        consulta = "select cod_entsubent from entsubent where cod_entidadeinforme=" + usuario.get("COD_ENTIDADEINFORME");
        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta en BorraEntidadeInforme: "+consulta);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            cod_entsubent.add(rs.getString("cod_entsubent"));
        }
        rs.close();
        pstmt.close();
        for(int i=0;i<cod_entsubent.size();i++) {
            BorrarDependencias((String) cod_entsubent.elementAt(i));
        }
        consulta = "delete from ENTIDADEINFORME WHERE CODIGO = " + usuario.get("COD_ENTIDADEINFORME");
        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta en BorraEntidadeInforme: "+consulta);
        pstmt.executeUpdate();
        pstmt.close();

    }

    public void BorrarDependencias(String cod_entsubent)
        throws Exception {
        m_Log.debug("En BorrarDependencias con cod_entsubent: "+cod_entsubent);
        java.sql.PreparedStatement pstmt = null;
        String consulta;
        ResultSet rs;
        Vector codigos = new Vector();

        consulta = "select cod_entsubent from entsubent where cod_pai=" + cod_entsubent;
        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta en BorrarDependencias: "+consulta);
        rs = pstmt.executeQuery();
            if (rs.next()) {
                codigos.add(rs.getString("cod_entsubent"));
                while (rs.next()) {
                    codigos.add(rs.getString("cod_entsubent"));
                }
                rs.close();
                pstmt.close();
                for (int i=0;i<codigos.size();i++) {
                    BorrarDependencias((String)codigos.elementAt(i));
                }
                consulta = "delete from entsubent where cod_entsubent=" + cod_entsubent;
                pstmt = con.prepareStatement(consulta);
                m_Log.debug("Consulta en BorrarDependencias: "+consulta);
                pstmt.executeUpdate();
                pstmt.close();
            } else {
                rs.close();
                pstmt.close();
                consulta = "delete from entsubent where cod_entsubent=" + cod_entsubent;
                pstmt = con.prepareStatement(consulta);
                m_Log.debug("Consulta en BorrarDependencias: "+consulta);
                pstmt.executeUpdate();
                pstmt.close();
            }
    }

    public void BorraEntidadeAplicacion(
        es.altia.util.HashtableWithNull usuario)
        throws Exception {

        java.sql.PreparedStatement pstmt = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);
        //con = adapBD.getConnection();

        String cod = usuario.get("COD_ENTIDADEINFORME").toString();

        // Definir consulta.
        String consulta = "delete";
        consulta += " from ENTIDADEAPLICACION";
        consulta += " WHERE COD_ENTIDADEINFORME = " + cod;

        if (m_Log.isDebugEnabled()) m_Log.debug("Consulta en BorraEntidadeAplicacion: "+consulta);
        pstmt = con.prepareStatement(consulta);

        // Ejecutar la consulta.

        pstmt.executeUpdate();

        // Procesar el resultado de la consulta.

        pstmt.close();

        //adapBD.devolverConexion(con);

    }

    public void ModificaEntidadeInforme(
        es.altia.util.HashtableWithNull usuario)
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

        m_Log.debug("Consulta: "+consulta);
        pstmt.executeUpdate();

        // Procesar el resultado de la consulta.

        pstmt.close();
        //adapBD.devolverConexion(con);

    }

    public es.altia.util.conexion.Cursor ConsultaEntidadeAplicacion(
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
            "select EA.COD_ENTIDADEINFORME as COD_ENTIDADEINFORME,EA.APL_COD AS APL_COD ,A_APL.APL_NOM AS APL_NOM";
        consulta += " FROM ENTIDADEAPLICACION EA," + GlobalNames.ESQUEMA_GENERICO + "A_APL A_APL";
        consulta += " WHERE EA.COD_ENTIDADEINFORME = "
            + usuario.get("COD_ENTIDADEINFORME");
        consulta += " AND A_APL.APL_COD=EA.APL_COD ";
        pstmt = con.prepareStatement(consulta);

        // Ejecutar la consulta.

        m_Log.debug("Consulta: "+consulta);
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

}
