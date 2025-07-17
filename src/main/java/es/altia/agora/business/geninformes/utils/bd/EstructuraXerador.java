package es.altia.agora.business.geninformes.utils.bd;
import es.altia.agora.business.geninformes.utils.Utilidades;
import es.altia.util.conexion.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Vector;
import java.sql.ResultSet;

public final class EstructuraXerador {
    private java.sql.Connection con = null;
    private String[] _params = null;
    protected static Log m_Log =
            LogFactory.getLog(EstructuraXerador.class.getName());


    public EstructuraXerador(java.sql.Connection con) {
        this.con = con;
    }

    public es
        .altia
        .util
        .conexion
        .Cursor ConsultaHijosEstructuraInformeAdaptada(
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
            eraNull = true;
            con = adapBD.getConnection();
            m_Log.debug("con era null en consesthijosinf");

        }
        // Definir consulta.

        String consulta =
            "select ESTI.COD_ESTRUCTURA AS COD_ESTRUCTURA,ESTI.COD_ENTIDADEINFORME AS COD_ENTIDADEINFORME,ESTI.COD_PAI AS COD_PAI,ESTI.POSICION AS POSICION,EI.NOME AS NOME_ENT,A_DOC.DOC_NOM AS NOME  ";
        consulta += " FROM ESTRUCTURAINFORME ESTI,ENTIDADEINFORME EI ,A_DOC";
        consulta += " WHERE COD_PAI = " + usuario.get("COD_ESTRUCTURA");
        consulta += " AND ESTI.COD_ENTIDADEINFORME=EI.CODIGO ";
        consulta += " AND A_DOC.DOC_CEI=ESTI.COD_ESTRUCTURA ";
        consulta += " ORDER BY ESTI.POSICION ";

        m_Log.debug("Metodo ConsultaHijosEstructuraInformeAdaptada en la clase EstructuraXerador : " + consulta);
        pstmt = con.prepareStatement(consulta);

        // Ejecutar la consulta.

        rs = pstmt.executeQuery();

        // Procesar el resultado de la consulta.

        datosResultado =
            new es.altia.util.conexion.Cursor(
                rs,
                rs.getMetaData().getColumnCount());

        pstmt.close();
        if (eraNull) {
            adapBD.devolverConexion(con);
            con = null;
        }

        return datosResultado;

    }

    public es.altia.util.conexion.Cursor ConsultaHijosEstructuraInforme(
        es.altia.util.HashtableWithNull usuario)
        throws Exception {

        es.altia.util.conexion.Cursor datosResultado = null;

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;
        AdaptadorSQLBD adapBD = null;

        try{
        _params = (String[]) usuario.get("PARAMS");

       
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        boolean eraNull = false;
        if (con == null) {
            eraNull = true;
            con = adapBD.getConnection();
            m_Log.debug("con era null en consesthijosinf");

        }
        // Definir consulta.

        String consulta =
            "select ESTI.COD_ESTRUCTURA AS COD_ESTRUCTURA,ESTI.COD_ENTIDADEINFORME AS COD_ENTIDADEINFORME,ESTI.COD_PAI AS COD_PAI,ESTI.CONSULTASQL AS CONSULTASQL,ESTI.POSICION AS POSICION,EI.NOME AS NOME,EI.CLAUSULAFROM AS CLAUSULAFROM,EI.CLAUSULAWHERE AS CLAUSULAWHERE ,EI.NOMEVISTA AS NOMEVISTA  ";
        consulta += " FROM ESTRUCTURAINFORME ESTI,ENTIDADEINFORME EI ";
        consulta += " WHERE COD_PAI = " + usuario.get("COD_ESTRUCTURA");
        consulta += " AND ESTI.COD_ENTIDADEINFORME=EI.CODIGO ";
        consulta += " ORDER BY ESTI.POSICION ";

        m_Log.debug("Metodo ConsultaHijosEstructuraInforme en la clase EstructuraXerador : " + consulta);
        pstmt = con.prepareStatement(consulta);

        // Ejecutar la consulta.

        rs = pstmt.executeQuery();

        // Procesar el resultado de la consulta.

        datosResultado =
            new es.altia.util.conexion.Cursor(
                rs,
                rs.getMetaData().getColumnCount());

        pstmt.close();
        if (eraNull) {
            adapBD.devolverConexion(con);
            con = null;
        }
        
        }catch(Exception e)
        {
            m_Log.error ("Excepción: " +e.getMessage());
        }finally{
            adapBD.devolverConexion(con);
        }

        return datosResultado;

    }

    public es.altia.util.conexion.Cursor ConsultaEstructuraEntidadesInforme(
        es.altia.util.HashtableWithNull usuario)
        throws Exception {

        es.altia.util.conexion.Cursor datosResultado = null;

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        
        try{
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        boolean eraNull = false;
        if (con == null) {
            eraNull = true;
            con = adapBD.getConnection();
        }
        String consulta =
            "select ESTRUCTURAINFORME.COD_ESTRUCTURA AS COD_ESTRUCTURA,ESTRUCTURAINFORME.COD_ENTIDADEINFORME AS COD_ENTIDADEINFORME,ESTRUCTURAINFORME.COD_PAI AS COD_PAI,ESTRUCTURAINFORME.CONSULTASQL AS CONSULTASQL,ESTRUCTURAINFORME.POSICION AS POSICION,EI.NOME AS NOME,EI.CLAUSULAFROM AS CLAUSULAFROM,EI.CLAUSULAWHERE AS CLAUSULAWHERE ,EI.NOMEVISTA AS NOMEVISTA ";
        consulta += " FROM ESTRUCTURAINFORME,ENTIDADEINFORME EI ";
        consulta += " WHERE COD_ESTRUCTURA = " + usuario.get("COD_ESTRUCTURA");
        consulta += " AND ESTRUCTURAINFORME.COD_ENTIDADEINFORME=EI.CODIGO ";
        consulta += " ORDER BY ESTRUCTURAINFORME.POSICION ";
        m_Log.debug("Metodo ConsultaEstructuraEntidadesInforme en la clase EstructuraXerador : " + consulta);
        pstmt = con.prepareStatement(consulta);

        // Ejecutar la consulta.

        rs = pstmt.executeQuery();

        // Procesar el resultado de la consulta.

        datosResultado = new es.altia.util.conexion.Cursor(rs,rs.getMetaData().getColumnCount());

        pstmt.close();
        if (eraNull) {
            adapBD.devolverConexion(con);
            con = null;
        }
        }catch(Exception e)
        {
            m_Log.error ("Excepción: " +e.getMessage());
        }finally{
            try {
                adapBD.devolverConexion(con);
            } catch (Exception e) {               
                e.printStackTrace();
            }
        }
        return datosResultado;

    }

    public es
        .altia
        .util
        .conexion
        .Cursor ConsultaEstructuraEntidadesInformeAdaptada(
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
            eraNull = true;
            con = adapBD.getConnection();
        }
        String consulta =
            "select ESTRUCTURAINFORME.COD_ESTRUCTURA AS COD_ESTRUCTURA,A_DOC.DOC_APL AS APL_DOC,A_DOC.DOC_NOM AS NOME,'-','L','CONSULTASQL',ESTRUCTURAINFORME.COD_ENTIDADEINFORME AS COD_ENTIDADEINFORME,'MNUCOD'  AS COD_FUNCION ";
        consulta += " FROM ESTRUCTURAINFORME,ENTIDADEINFORME EI ,A_DOC ";
        consulta += " WHERE ESTRUCTURAINFORME.COD_ESTRUCTURA = "
            + usuario.get("COD_ESTRUCTURA");
        consulta += " AND ESTRUCTURAINFORME.COD_ENTIDADEINFORME=EI.CODIGO ";
        consulta += " AND A_DOC.DOC_CEI=ESTRUCTURAINFORME.COD_ESTRUCTURA ";
        consulta += " ORDER BY ESTRUCTURAINFORME.POSICION ";
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
        if (eraNull) {
            adapBD.devolverConexion(con);
            con = null;
        }
        return datosResultado;

    }

    public es.altia.util.conexion.Cursor ConsultaEstructurasInforme(
        es.altia.util.HashtableWithNull usuario)
        throws Exception {

        es.altia.util.conexion.Cursor datosResultado = null;

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        try{
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        boolean eraNull = false;
        if (con == null) {
            eraNull = true;
            con = adapBD.getConnection();
        }
        String consulta =
            "select ESTRUCTURAINFORME.COD_ESTRUCTURA AS COD_ESTRUCTURA,A_DOC.DOC_APL AS APL_DOC,A_DOC.DOC_NOM AS NOME,ESTRUCTURAINFORME.COD_ENTIDADEINFORME AS COD_ENTIDADEINFORME,EI.NOME AS NOME_ENT ";
        consulta += " FROM ESTRUCTURAINFORME,ENTIDADEINFORME EI ,A_DOC ";
        consulta += " WHERE A_DOC.DOC_APL = " + usuario.get("APL_COD");
        consulta += " AND ESTRUCTURAINFORME.COD_ENTIDADEINFORME=EI.CODIGO ";
        consulta += " AND A_DOC.DOC_CEI=ESTRUCTURAINFORME.COD_ESTRUCTURA ";
        consulta += " ORDER BY A_DOC.DOC_NOM  ";
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
        if (eraNull) {
            adapBD.devolverConexion(con);
            con = null;
        }
        }catch(Exception e)
        {
            m_Log.error ("Excepción: " +e.getMessage());           
        }finally{
            try {
                adapBD.devolverConexion(con);
            } catch (Exception e) {               
                e.printStackTrace();
            }
           
        }
        return datosResultado;

    }

    public Long AltaEstructuraXerador(es.altia.util.HashtableWithNull usuario)
        throws Exception {

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        if (con == null)
            con = adapBD.getConnection();

        Long salida = null;

        String codEntidadeInforme =
            ((usuario.get("COD_ENTIDADEINFORME") != null)
                && (!((((String) usuario.get("COD_ENTIDADEINFORME"))
                    .equals("")))))
                ? usuario.get("COD_ENTIDADEINFORME").toString()
                : "null";

        String codPai =
            ((usuario.get("COD_PAI") != null)
                && (!((((String) usuario.get("COD_PAI")).equals("")))))
                ? usuario.get("COD_PAI").toString()
                : "null";

        String posicion =
            ((usuario.get("POSICION") != null)
                && (!(((usuario.get("POSICION")).toString().equals("")))))
                ? usuario.get("POSICION").toString()
                : "null";

        // Definir consulta.

        long temp = adapBD.devolverNextValSecuencia(_params, "CONTESTRUCTURAINFORME");

        String consulta = "insert ";
        consulta
            += " into ESTRUCTURAINFORME ( COD_ESTRUCTURA, COD_ENTIDADEINFORME,COD_PAI,CONSULTASQL,POSICION) ";
        consulta += " values (?,"
            + codEntidadeInforme
            + ","
            + codPai
            + ",?,"
            + posicion
            + " ) ";

        m_Log.debug("EstructuraXerador:AltaEstructuraXerador: " + consulta);
        pstmt = con.prepareStatement(consulta);
        m_Log.debug("EstructuraXerador:AltaEstructuraXerador: " + usuario.get("CONSULTASQL"));

        char[] plt_txt =
            (usuario.get("CONSULTASQL") != null)
                ? ((String) usuario.get("CONSULTASQL")).toCharArray()
                : null;

        pstmt.setLong(1, temp);
        if (plt_txt == null) {
            pstmt.setNull(2, java.sql.Types.LONGVARCHAR);

        } else {
            java.io.CharArrayReader cr = new java.io.CharArrayReader(plt_txt);
            pstmt.setCharacterStream(2, cr, plt_txt.length);
        }

        // Ejecutar la consulta.
        pstmt.executeUpdate();

//		long temp = Utilidades.insertar(pstmt, "CONTESTRUCTURAINFORME");

        salida = new Long(temp);

        pstmt.close();
        //adapBD.devolverConexion(con);
        return salida;

    }

    public void BorraEstructuraXerador(es.altia.util.HashtableWithNull usuario)
        throws Exception {

        m_Log.debug("En BorraEstructuraXerador de EstructuraXerador");
        java.sql.PreparedStatement pstmt = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        if (con == null)
            con = adapBD.getConnection();
        String consulta;
        Vector cod_estructura = new Vector();
        ResultSet rs;

        consulta = "select cod_estructura from ESTRUCTURAINFORME where cod_pai=" + usuario.get("COD_ESTRUCTURA");
        pstmt = con.prepareStatement(consulta);
        m_Log.debug("consulta en BorraEstructuraXerador: "+consulta);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            cod_estructura.add(rs.getString("cod_estructura"));
        }
        rs.close();
        pstmt.close();
        for(int i=0;i<cod_estructura.size();i++) {
            BorrarDependencias((String) cod_estructura.elementAt(i));
        }
        consulta = "delete from ESTRUCTURAINFORME WHERE ESTRUCTURAINFORME.COD_ESTRUCTURA=" + usuario.get("COD_ESTRUCTURA");
        m_Log.debug("consulta BorraEstructuraXerador: " + consulta);

        pstmt = con.prepareStatement(consulta);
        int r = pstmt.executeUpdate();
        m_Log.debug("el resultado de borrar es : " + r);
        pstmt.close();

    }

    public void BorrarDependencias(String cod_estructura)
        throws Exception {
        m_Log.debug("En BorrarDependencias con cod_estructura: "+cod_estructura);
        java.sql.PreparedStatement pstmt = null;
        String consulta;
        ResultSet rs;
        Vector cods_estructura = new Vector();

        consulta = "select cod_estructura from ESTRUCTURAINFORME where cod_pai=" + cod_estructura;
        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta en BorrarDependencias: "+consulta);
        rs = pstmt.executeQuery();
            if (rs.next()) {
                cods_estructura.add(rs.getString("cod_estructura"));
                while (rs.next()) {
                    cods_estructura.add(rs.getString("cod_estructura"));
                }
                rs.close();
                pstmt.close();
                for (int i=0;i<cods_estructura.size();i++) {
                    m_Log.debug("LLAMA A BORRARDEPENDENCIAS CON: "+cods_estructura.elementAt(i));
                    BorrarDependencias((String)cods_estructura.elementAt(i));
                }
                consulta = "delete from ESTRUCTURAINFORME where cod_estructura=" + cod_estructura;
                pstmt = con.prepareStatement(consulta);
                m_Log.debug("Consulta en BorrarDependencias: "+consulta);
                pstmt.executeUpdate();
                pstmt.close();
            } else {
                rs.close();
                pstmt.close();
                consulta = "delete from ESTRUCTURAINFORME where cod_estructura=" + cod_estructura;
                pstmt = con.prepareStatement(consulta);
                m_Log.debug("Consulta en BorrarDependencias: "+consulta);
                pstmt.executeUpdate();
                pstmt.close();
            }
    }

    public void AltaEstructuraAplicacion(
        es.altia.util.HashtableWithNull usuario)
        throws Exception {

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        if (con == null)
            con = adapBD.getConnection();

        Long salida = null;

        String codEstructuraInforme =
            ((usuario.get("COD_ESTRUCTURA") != null)
                && (!(((usuario.get("COD_ESTRUCTURA"))
                    .toString()
                    .trim()
                    .equals("")))))
                ? usuario.get("COD_ESTRUCTURA").toString()
                : "null";

        String aplCod =
            ((usuario.get("APL_COD") != null)
                && (!(((usuario.get("APL_COD")).toString().trim().equals("")))))
                ? usuario.get("APL_COD").toString()
                : "null";

        String docNom =
            ((usuario.get("DOC_NOM") != null)
                && (!(((usuario.get("DOC_NOM")).toString().equals("")))))
                ? "'" + usuario.get("DOC_NOM").toString() + "'"
                : "null";

        // Definir consulta.

        String consulta = "insert ";
        consulta += " into A_DOC ( DOC_CEI, DOC_APL, DOC_NOM) ";
        consulta += " values ("
            + codEstructuraInforme
            + ","
            + aplCod
            + ","
            + docNom
            + " ) ";

        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);

        // Ejecutar la consulta.

        pstmt.executeUpdate();

        //salida = new Long(temp);

        pstmt.close();
        //adapBD.devolverConexion(con);
        //return salida;

    }


public void ModificaEstructuraAplicacion(
        es.altia.util.HashtableWithNull usuario)
        throws Exception {

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        if (con == null)
            con = adapBD.getConnection();

        Long salida = null;

        String codEstructuraInforme =
            ((usuario.get("COD_ESTRUCTURA") != null)
                && (!(((usuario.get("COD_ESTRUCTURA"))
                    .toString()
                    .trim()
                    .equals("")))))
                ? usuario.get("COD_ESTRUCTURA").toString()
                : "null";

        String aplCod =
            ((usuario.get("APL_COD") != null)
                && (!(((usuario.get("APL_COD")).toString().trim().equals("")))))
                ? usuario.get("APL_COD").toString()
                : "null";

        String docNom =
            ((usuario.get("DOC_NOM") != null)
                && (!(((usuario.get("DOC_NOM")).toString().equals("")))))
                ?  usuario.get("DOC_NOM").toString()
                : null;

        // Definir consulta.

        String consulta = " update ";
        consulta += " A_DOC ";
        consulta+=" set DOC_APL="+aplCod;
        consulta+=", DOC_NOM=? ";
        consulta += " WHERE DOC_CEI="+codEstructuraInforme;


        m_Log.debug("Update de A_DOC:"+consulta+".");

        pstmt = con.prepareStatement(consulta);

        pstmt.setString(1,docNom);
        // Ejecutar la consulta.

        pstmt.executeUpdate();

        //salida = new Long(temp);

        pstmt.close();
        //adapBD.devolverConexion(con);
        //return salida;

    }


    public void ModificarEstructuraXerador(
        es.altia.util.HashtableWithNull usuario)
        throws Exception {

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");
        m_Log.debug("los parametros en modificarEstructuraXerador son : " + _params);

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        m_Log.debug("la conexion es : " + con);

        if (con == null)
            con = adapBD.getConnection();

        Long salida = null;

        String codEntidadeInforme =
            ((usuario.get("COD_ENTIDADEINFORME") != null)
                && (!((((String) usuario.get("COD_ENTIDADEINFORME"))
                    .equals("")))))
                ? usuario.get("COD_ENTIDADEINFORME").toString()
                : "null";

        String codPai =
            ((usuario.get("COD_PAI") != null)
                && (!((((String) usuario.get("COD_PAI")).equals("")))))
                ? usuario.get("COD_PAI").toString()
                : "null";

        String posicion =
            ((usuario.get("POSICION") != null)
                && (!(((usuario.get("POSICION")).toString().equals("")))))
                ? usuario.get("POSICION").toString()
                : "null";

        m_Log.debug("En modificar estructura xerador ******************************** " ) ;

        // Definir consulta.

        String consulta = "update ";
        consulta += "  ESTRUCTURAINFORME set ";
        consulta += " COD_ENTIDADEINFORME="
            + usuario.get("COD_ENTIDADEINFORME");
        consulta += ",COD_PAI=" + codPai;
        consulta += ",CONSULTASQL=?";
        consulta += ",POSICION=" + posicion;
        consulta += " where COD_ESTRUCTURA =" + usuario.get("COD_ESTRUCTURA");

        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);

        char[] plt_txt =
            (usuario.get("CONSULTASQL") != null)
                ? ((String) usuario.get("CONSULTASQL")).toCharArray()
                : null;

        m_Log.debug("Grabando consultasql:"+usuario.get("CONSULTASQL")+".");
        m_Log.debug("el plt_txt es : " + plt_txt);
        if (plt_txt == null) {
            pstmt.setNull(1, java.sql.Types.LONGVARCHAR);

        } else {
            m_Log.debug("dentro del else donde meto el array *************************************** ");
            java.io.CharArrayReader cr = new java.io.CharArrayReader(plt_txt);
            pstmt.setCharacterStream(1, cr, plt_txt.length);
        }

        // Ejecutar la consulta.

        pstmt.executeUpdate();

        //		salida = new Long(temp);

        pstmt.close();
        //adapBD.devolverConexion(con);
        //		return salida;

    }

    public void ModificarPaiEstructuraXerador(
        es.altia.util.HashtableWithNull usuario)
        throws Exception {

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        if (con == null)
            con = adapBD.getConnection();

        Long salida = null;

        String codPai =
            ((usuario.get("COD_PAI") != null)
                && (!(((usuario.get("COD_PAI")).toString().trim().equals("")))))
                ? usuario.get("COD_PAI").toString()
                : "null";

        String posicion =
            ((usuario.get("POSICION") != null)
                && (!(((usuario.get("POSICION")).toString().equals("")))))
                ? usuario.get("POSICION").toString()
                : "null";

        // Definir consulta.

        String consulta = "update ";
        consulta += "  ESTRUCTURAINFORME set ";
        consulta += "COD_PAI=" + codPai;
        consulta += ",POSICION=" + posicion;
        consulta += " where COD_ESTRUCTURA = " + usuario.get("COD_ESTRUCTURA");

        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);

        // Ejecutar la consulta.

        pstmt.executeUpdate();

        //		salida = new Long(temp);

        pstmt.close();
        //adapBD.devolverConexion(con);
        //		return salida;

    }

    public void BorraSubEstructurasEstructura(
        es.altia.util.HashtableWithNull usuario)
        throws Exception {

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        _params = (String[]) usuario.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params != null)
            adapBD = new AdaptadorSQLBD(_params);

        if (con == null)
            con = adapBD.getConnection();


        // Definir consulta.

        String consulta = "update ";
        consulta += "  ESTRUCTURAINFORME set ";
        consulta += " COD_PAI=NULL ";
        consulta += " ,POSICION=0 ";
        consulta += " where COD_PAI = " + usuario.get("COD_ESTRUCTURA");

        pstmt = con.prepareStatement(consulta);
        m_Log.debug("Consulta: "+consulta);

        // Ejecutar la consulta.

        pstmt.executeUpdate();

        //		salida = new Long(temp);

        pstmt.close();
        //adapBD.devolverConexion(con);
        //		return salida;

    }

}