package es.altia.agora.business.geninformes.utils.bd;
import es.altia.util.conexion.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public final class ValidaXerador {
    protected static Log m_Log = LogFactory.getLog(UtilesXerador.class.getName()); // Para información de logs
    private java.sql.Connection con = null;
    private String[] _params=null;
    public void setParams(String[] par) { _params=par; }



    /**
     * Method ValidaXerador.
     * @param con
     */
    public ValidaXerador(java.sql.Connection con) {
        this.con = con;
    }

    /**
     * Method ExisteCodEntidadeEnENTIDADEINFORME.
     * @param entrada
     * @return Cursor
     * @throws Exception
     */
    public es.altia.util.conexion.Cursor ExisteCodEntidadeEnENTIDADEINFORME(
        es.altia.util.HashtableWithNull entrada)
        throws Exception {
        es.altia.util.conexion.Cursor datosResultado = null;

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        String[] _params = (String[]) entrada.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params!=null) adapBD=new AdaptadorSQLBD(_params);

        if (con==null) con = adapBD.getConnection();

        // Definir consulta.

        String consulta = "select 'x' ";
        consulta += "from ENTIDADEINFORME ";
        consulta += "where CODIGO = " + entrada.get("COD_ENTIDADEINFORME");

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

    /**
     * Method ExisteCodCampoEnCAMPOINFORME.
     * @param entrada
     * @return Cursor
     * @throws Exception
     */
    public es.altia.util.conexion.Cursor ExisteCodCampoEnCAMPOINFORME(
        es.altia.util.HashtableWithNull entrada)
        throws Exception {
        es.altia.util.conexion.Cursor datosResultado = null;

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        String[] _params = (String[]) entrada.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params!=null) adapBD=new AdaptadorSQLBD(_params);

        if (con==null) con = adapBD.getConnection();

        // Definir consulta.

        String consulta = "select 'x' ";
        consulta += "from CAMPOINFORME ";
        consulta += "where CODIGO = " + entrada.get("COD_CAMPOINFORME");

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

    /**
     * Method ExisteCampoEntidadeEnCAMPOENTIDADEINFORME.
     * @param entrada
     * @return Cursor
     * @throws Exception
     */
    public es
        .altia
        .util
        .conexion
        .Cursor ExisteCampoEntidadeEnCAMPOENTIDADEINFORME(
        es.altia.util.HashtableWithNull entrada)
        throws Exception {
        es.altia.util.conexion.Cursor datosResultado = null;

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        String[] _params = (String[]) entrada.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params!=null) adapBD=new AdaptadorSQLBD(_params);

        if (con==null) con = adapBD.getConnection();

        // Definir consulta.

        String consulta = "select 'x' ";
        consulta += "from CAMPOENTIDADEINFORME ";
        consulta += "where COD_ENTIDADEINFORME = "
            + entrada.get("COD_ENTIDADEINFORME");
        consulta += " AND COD_CAMPOINFORME = "
            + entrada.get("COD_CAMPOINFORME");
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

    public es.altia.util.conexion.Cursor ExisteCenCodNomeEnCAMPOINFORME(
        es.altia.util.HashtableWithNull entrada)
        throws Exception {
        es.altia.util.conexion.Cursor datosResultado = null;

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        String[] _params = (String[]) entrada.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params!=null) adapBD=new AdaptadorSQLBD(_params);

        if (con==null) con = adapBD.getConnection();

        // Definir consulta.

        String consulta = "select 'x' ";
        consulta += "from INFORMEXERADOR ";
        consulta += "where APL_COD = " + entrada.get("APL_COD");
        consulta += "and NOME= '" + entrada.get("NOME") + "' ";
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

    public es.altia.util.conexion.Cursor ExisteCodFonteEnFONTELETRA(
        es.altia.util.HashtableWithNull entrada)
        throws Exception {
        es.altia.util.conexion.Cursor datosResultado = null;

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        String[] _params = (String[]) entrada.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params!=null) adapBD=new AdaptadorSQLBD(_params);

        if (con==null) con = adapBD.getConnection();

        // Definir consulta.

        String consulta = "select 'x' ";
        consulta += "from FONTELETRA ";
        consulta += "where COD_FONTELETRA = " + entrada.get("COD_FONTELETRA");
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

    public es.altia.util.conexion.Cursor ExisteInformeVinculadoEtiqueta(
        es.altia.util.HashtableWithNull entrada)
        throws Exception {
        es.altia.util.conexion.Cursor datosResultado = null;

        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        String[] _params = (String[]) entrada.get("PARAMS");

        AdaptadorSQLBD adapBD = null;
        if (_params!=null) adapBD=new AdaptadorSQLBD(_params);

        if (con==null) con = adapBD.getConnection();


        // Definir consulta.

        String consulta = "select 'x' ";
        consulta += "from ETIQUETAXERADOR ";
        consulta += "where COD_ETIQUETA = " + entrada.get("CODIGO");
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

}
