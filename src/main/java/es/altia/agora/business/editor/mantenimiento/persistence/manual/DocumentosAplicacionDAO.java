package es.altia.agora.business.editor.mantenimiento.persistence.manual;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.jdbc.JdbcOperations;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;


public class DocumentosAplicacionDAO
{
    //Para el fichero de configuracion tecnico.
    protected static Config conf;
    //Para informacion de logs.
    protected static Log m_Log =
            LogFactory.getLog(DocumentosAplicacionDAO.class.getName());

    private static DocumentosAplicacionDAO instance = null;
    protected	static String dot_mun;
    protected	static String dot_pro;
    protected	static String dot_tra;
    protected	static String dot_cod;
    protected	static String dot_tdo;
    protected	static String dot_vis;
    protected	static String dot_frm;
    protected	static String dot_plt;
    protected	static String dot_activo;

    protected	static String dtml_mun;
    protected	static String dtml_pro;
    protected	static String dtml_tra;
    protected	static String dtml_dot;
    protected	static String dtml_cmp;
    protected	static String dtml_leng;
    protected	static String dtml_valor;

    protected static String aplt_cod;
    protected static String aplt_des;
    protected static String aplt_apl;
    protected static String aplt_doc;
    protected static String aplt_pro;
    protected static String aplt_tra;
    protected static String aplt_int;
    protected static String aplt_rel;

    protected DocumentosAplicacionDAO()
    {
        super();
        //Queremos usar el fichero de configuracion techserver
        conf = ConfigServiceHelper.getConfig("techserver");

        dot_mun	= conf.getString("SQL.E_DOT.codMunicipio");
        dot_pro	= conf.getString("SQL.E_DOT.codProcedimiento");
        dot_tra	= conf.getString("SQL.E_DOT.codTramite");
        dot_cod	= conf.getString("SQL.E_DOT.codDocumento");
        dot_tdo	= conf.getString("SQL.E_DOT.codTipoDocumento");
        dot_vis	= conf.getString("SQL.E_DOT.visibleInternet");
        dot_frm	= conf.getString("SQL.E_DOT.firma");
        dot_activo	= conf.getString("SQL.E_DOT.activo");
        dot_plt	= conf.getString("SQL.E_DOT.codPlantilla");

        aplt_cod = conf.getString("SQL.A_PLT.codigo");
        aplt_des = conf.getString("SQL.A_PLT.descripcion");
        aplt_apl = conf.getString("SQL.A_PLT.codigoApli");
        aplt_doc = conf.getString("SQL.A_PLT.doc");
        aplt_pro = conf.getString("SQL.A_PLT.procedimiento");
        aplt_tra = conf.getString("SQL.A_PLT.tramite");
        aplt_int = conf.getString("SQL.A_PLT.interesado");
        aplt_rel = conf.getString("SQL.A_PLT.relacion");

    }

    public static DocumentosAplicacionDAO getInstance()
    {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null)
        {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(DocumentosAplicacionDAO.class)
            {
                if (instance == null)
                    instance = new DocumentosAplicacionDAO();
            }
        }
        return instance;
    }


    public Vector loadAplicaciones(String[] params,String codigoAplicacionDocumentos)
    {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try
        {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            // Creamos la select con los parametros adecuados.
            String sql = "SELECT ";
            sql = sql +conf.getString("SQL.A_APL.codigo")+","+conf.getString("SQL.A_APL.nombre")
                    +" FROM " + GlobalNames.ESQUEMA_GENERICO + "A_APL A_APL WHERE "+conf.getString("SQL.A_APL.codigo")+" <> "+codigoAplicacionDocumentos;

            //Para que nos devuelva ordenados por SQL.A_APL.codApli
            //deberemos llamar a la funcion OrderUnion del abd, a esta funcion
            //le pasaremos un Array de cadenas donde insertaremos para cada campo
            //por el que queremos ordenar:
            //    - en la posicion el nombre del campo por el que queremos ordenar
            //    - en la posicion siguiente, la posicion que ocupará dicho campo en
            //        el SELECT.
            String[] orden = {conf.getString("SQL.A_APL.codigo"),"1"};
            //La condicion de ordenación irá al final, luego lo añadimos a la sentencia
            //SQL.
            sql += abd.orderUnion(orden);
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int i=0;
            while(rs.next())
            {
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codigoAplicacion", rs.getString(conf.getString("SQL.A_APL.codigo")));
                gVO.setAtributo("nombreAplicacion", rs.getString(conf.getString("SQL.A_APL.nombre")));
                r.add(gVO);
                i++;
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException sqle)
        {
            m_Log.error("Error de SQL en loadAplicaciones: " + sqle.toString());
        }
        catch (BDException bde)
        {
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo loadAplicaciones: " + bde.toString());
        }
        finally
        {
            if (conexion != null)
            {
                try
                {
                    abd.devolverConexion(conexion);
                }
                catch(BDException bde)
                {
                    m_Log.error("No se pudo devolver la conexion: " + bde.toString());
                }
            }
        }
        return r;
    }


    public Vector loadDocumentos(GeneralValueObject gvo,String[] params)
    {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try
        {
            if (m_Log.isDebugEnabled()){
                m_Log.debug(" Aplicacion :: "+gvo.getAtributo("codigoAplicacion"));
                m_Log.debug(" Procedimientocion :: "+gvo.getAtributo("codigoAplicacion"));
            }
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            // Creamos la select con los parametros adecuados.
            String sql = "";

           sql = "SELECT PLT_COD, PLT_DES, PLT_INT, PLT_REL, PLT_EDITOR_TEXTO, PLT_VIS_EXT";

            String docActivo = " AND E_DOT.DOT_ACTIVO = 'SI'";
            if (gvo.getAtributo("codActivo") != null && !gvo.getAtributo("codActivo").equals("")) {
                if (gvo.getAtributo("codActivo").equals("N")) {
                    docActivo = " AND E_DOT.DOT_ACTIVO = 'NO'";
                }
            }
            else {
                docActivo = "";
            }
            if(gvo.getAtributo("codigoAplicacion").equals("4") && !gvo.getAtributo("codProcedimiento").equals("")) {
                sql += ", e_dot.dot_activo, E_DOT.DOT_COD as CODVIS, DOT_TRA, TML_VALOR, E_DOT.DOT_FRM as TIPO_FIRMA " +
                    " FROM A_PLT, E_DOT, E_TML WHERE "+conf.getString("SQL.A_PLT.codigoApli")+"="+gvo.getAtributoONulo("codigoAplicacion") + 
                    " and a_plt.plt_pro ='" + gvo.getAtributo("codProcedimiento")+"'";
                if (!gvo.getAtributo("codTramite").equals(""))
                    sql += " and a_plt.plt_tra =" + gvo.getAtributo("codTramite");
                
                sql += " and e_dot.dot_pro ='" + gvo.getAtributo("codProcedimiento") +	
                        "' and e_dot.dot_plt = a_plt.plt_cod" +
                        " and a_plt.plt_pro = e_dot.dot_pro and a_plt.plt_tra = e_dot.dot_tra " + 
                        "AND TML_PRO = DOT_PRO AND TML_TRA = DOT_TRA AND TML_LENG = '"+ conf.getString("idiomaDefecto") + "'" +
                        docActivo +
                        " order by DOT_TRA,PLT_COD ";
            } else { 
                sql+=" FROM A_PLT WHERE "+conf.getString("SQL.A_PLT.codigoApli")+"="+gvo.getAtributoONulo("codigoAplicacion");
                String[] orden = {conf.getString("SQL.A_PLT.codigo"),"1"};
                //La condicion de ordenación irá al final, luego lo añadimos a la sentencia
                //SQL.
                sql += abd.orderUnion(orden);
            }

            if(m_Log.isDebugEnabled()) m_Log.debug("SQL de loadDocumentos: "+sql);
            ResultSet rs = null;

            if (gvo.getAtributo("codigoAplicacion").equals("4") && gvo.getAtributo("codProcedimiento").equals(""))
                rs = null;
            else {
                Statement stmt = conexion.createStatement();

                rs = stmt.executeQuery(sql);

                int i=0;

                while(rs.next())
                {
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codigo", rs.getString(conf.getString("SQL.A_PLT.codigo"))); 
                    if(gvo.getAtributo("codigoAplicacion").equals("4") && !gvo.getAtributo("codProcedimiento").equals("")) { 
                        gVO.setAtributo("codigoVisible", rs.getString("CODVIS"));
                        gVO.setAtributo("codigoTramite", rs.getString("DOT_TRA"));
                        gVO.setAtributo("nomeTramite", rs.getString("TML_VALOR"));
                        gVO.setAtributo("docActivo", rs.getString(dot_activo));
                        gVO.setAtributo("firma", rs.getString("TIPO_FIRMA"));
                    } else 
                        gVO.setAtributo("codigoVisible", "");
                    gVO.setAtributo("descripcion", rs.getString(conf.getString("SQL.A_PLT.descripcion")));
                    gVO.setAtributo("interesado", rs.getString(conf.getString("SQL.A_PLT.interesado")));
                    if(gvo.getAtributo("codigoAplicacion").equals("4") && !gvo.getAtributo("codProcedimiento").equals("")) {
                        gVO.setAtributo("docActivo", rs.getString(dot_activo));
                    } 
                    gVO.setAtributo("relacion", rs.getString(conf.getString("SQL.A_PLT.relacion")));
                    gVO.setAtributo("editorTexto", rs.getString("PLT_EDITOR_TEXTO"));
                    gVO.setAtributo("visibleExt", rs.getString("PLT_VIS_EXT"));
                    r.add(gVO);
                    i++;
                }
                rs.close();
                stmt.close();
            }
        }
        catch (SQLException sqle)
        {
            m_Log.error("Error de SQL en loadDocumentos: " + sqle.toString());
        }
        catch (BDException bde)
        {
            m_Log.error("error del OAD en el metodo loadDocumentos: " + bde.toString());
        }
        finally
        {
            if (conexion != null)
            {
                try
                {
                    abd.devolverConexion(conexion);
                }
                catch(BDException bde)
                {
                    m_Log.error("No se pudo devolver la conexion: " + bde.toString());
                }
            }
        }
        return r;
    }

    public Vector loadDocumentosDesdeDefinicion(GeneralValueObject gvo,String[] params)
    {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String from = "";
        String where = "";
        String sql = "";
        try
        {
            if (m_Log.isDebugEnabled()){
                m_Log.debug(" Aplicacion :: "+gvo.getAtributo("codigoAplicacion"));
                m_Log.debug(" Procedimientocion :: "+gvo.getAtributo("codigoAplicacion"));
            }
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            // Creamos la select con los parametros adecuados.
            from = dot_cod + "," + aplt_des	+ " AS nombreDocumento," + dot_tdo + "," + dot_vis + "," +
                    dot_frm + ","	+ aplt_des + " AS plantilla, " + dot_plt + "," + aplt_int + "," + aplt_rel + "," + 
                     dot_activo + ",PLT_EDITOR_TEXTO, PLT_VIS_EXT";
            where	= dot_pro	+ "='" + gvo.getAtributo("codProcedimiento") +	"' AND " +
                    dot_tra +	"=" +	gvo.getAtributo("codTramite");
            String[] join2 = new String[5];
            join2[0] = "E_DOT";
            join2[1] = "INNER";
            join2[2] = "a_plt";
            join2[3] = "e_dot." + dot_plt	+ "=a_plt." + aplt_cod + " AND " +
                    "e_dot." + dot_pro	+ "=a_plt." + aplt_pro + " AND " +
                    "e_dot." + dot_tra	+ "=a_plt." + aplt_tra;
            join2[4] = "false";

            sql =	abd.join(from,where,join2);
            String[] orden = {dot_cod,"1"};
            //La condicion de ordenación irá al final, luego lo añadimos a la sentencia
            //SQL.
            sql += abd.orderUnion(orden);
            if(m_Log.isDebugEnabled()) m_Log.debug("SQL de loadDocumentosDesdeDefinicion: "+sql);
            ResultSet rs = null;

            if(gvo.getAtributo("codigoAplicacion").equals("4") && (gvo.getAtributo("codProcedimiento").equals("")
                    ||gvo.getAtributo("codTramite").equals(""))) {
                rs = null;
            } else {
                Statement stmt = conexion.createStatement();

                rs = stmt.executeQuery(sql);

                int i=0;

                while(rs.next())
                {
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codigo", rs.getString(dot_cod));
                    gVO.setAtributo("descripcion", rs.getString("nombreDocumento"));
                    gVO.setAtributo("visibleInternet", rs.getString(dot_vis));
                    gVO.setAtributo("plantilla",rs.getString("plantilla"));
                    gVO.setAtributo("codPlantilla",rs.getString(dot_plt));
                    gVO.setAtributo("interesado", rs.getString(aplt_int));
                    gVO.setAtributo("docActivo", rs.getString(dot_activo));
                    gVO.setAtributo("firma", rs.getString(dot_frm));
                    gVO.setAtributo("relacion", rs.getString(aplt_rel));
                    gVO.setAtributo("editorTexto", rs.getString("PLT_EDITOR_TEXTO"));
                     gVO.setAtributo("visibleExt", rs.getString("PLT_VIS_EXT"));
                    r.add(gVO);
                    i++;
                }
                rs.close();
                stmt.close();
            }
        }
        catch (SQLException sqle)
        {
            m_Log.error("Error de SQL en loadDocumentos: " + sqle.toString());
        }
        catch (BDException bde)
        {
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo loadDocumentos: " + bde.toString());
        }
        finally
        {
            if (conexion != null)
            {
                try
                {
                    abd.devolverConexion(conexion);
                }
                catch(BDException bde)
                {
                    m_Log.error("No se pudo devolver la conexion: " + bde.toString());
                }
            }
        }
        return r;
    }


    public int eliminaDocumento(GeneralValueObject gvo,String[] params)
    {
// Se realizara una eliminación LOGICA, no FISICA, campo ACTIVO = "NO"
// Se utiliza este metodo también para activarlo, despues de una DESACTIVACION
        int resultadoEliminacion=0;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String codMunicipio    = (String)gvo.getAtributo("codMunicipio");
        String codAplicacion    = (String)gvo.getAtributo("codigoAplicacion");
        String codProcedimiento = (String)gvo.getAtributo("codProcedimiento");
        String codTramite       = (String)gvo.getAtributo("codTramite");
        String codDocumento     = (String)gvo.getAtributo("codDocumento");
        String documentoActivo = (String) gvo.getAtributo("docActivo");
        try
        {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            String sql = "SELECT DOT_COD FROM E_DOT WHERE DOT_MUN=? AND DOT_PRO=? AND DOT_TRA=? AND DOT_PLT=?";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            Vector listaCodDoc = new Vector();
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(codMunicipio));
            stmt.setString(2, codProcedimiento);
            if (codTramite!=null && !codTramite.equals("")) {
                stmt.setInt(3, Integer.parseInt(codTramite));
            } else {
                stmt.setString(3, null);
            }
            if (codDocumento!=null && !codDocumento.equals("")) {
                stmt.setInt(4, Integer.parseInt(codDocumento));
            } else {
                stmt.setString(4, null);
            }
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                String codDoc= rs.getString("DOT_COD");
                listaCodDoc.addElement(codDoc);
            }
            if (m_Log.isDebugEnabled()) m_Log.debug("el tamaño de la lista de documento es : " + listaCodDoc.size());
            rs.close();
            stmt.close();
            sql = "UPDATE E_DOT set DOT_ACTIVO =? WHERE DOT_MUN=? AND DOT_PRO=? AND DOT_TRA=? AND DOT_PLT=?";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            stmt.setString(1, documentoActivo);
            stmt.setInt(2, Integer.parseInt(codMunicipio));
            stmt.setString(3, codProcedimiento);
            if (codTramite!=null && !codTramite.equals("")) {
                stmt.setInt(4, Integer.parseInt(codTramite));
            } else {
                stmt.setString(4, null);
            }
            if (codDocumento!=null && !codDocumento.equals("")) {
                stmt.setInt(5, Integer.parseInt(codDocumento));
            } else {
                stmt.setString(5, null);
            }
            resultadoEliminacion = stmt.executeUpdate();
            resultadoEliminacion=1;
            stmt.close();
        }
        catch (Exception sqle)
        {
            rollBackTransaction(abd,conexion,sqle);
            resultadoEliminacion=0;

        }
        finally
        {
            commitTransaction(abd,conexion);
        }
        return resultadoEliminacion;
    }
    public int eliminaDocumentoAplicacion(GeneralValueObject gvo,String[] params)
    {
// Se realizara una eliminación LOGICA, no FISICA, campo ACTIVO = "NO"
// Se utiliza este metodo también para activarlo, despues de una DESACTIVACION
        int resultadoEliminacion=0;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String codMunicipio    = (String)gvo.getAtributo("codMunicipio");
        String codAplicacion    = (String)gvo.getAtributo("codigoAplicacion");
        String codProcedimiento = (String)gvo.getAtributo("codProcedimiento");
        String codTramite       = (String)gvo.getAtributo("codTramite");
        String codDocumento     = (String)gvo.getAtributo("codDocumento");
        String documentoActivo = (String) gvo.getAtributo("docActivo");
        try
        {
          /*  DELETE FROM Store_Information
WHERE store_name = "Los Angeles"*/
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            String sql = "DELETE FROM A_PLT WHERE PLT_APL=? AND PLT_COD=?";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(codAplicacion));
            if (codDocumento!=null && !codDocumento.equals("")) {
                stmt.setInt(2, Integer.parseInt(codDocumento));
            } else {
                stmt.setString(2, null);
            }
            resultadoEliminacion = stmt.executeUpdate();
            resultadoEliminacion=1;
            stmt.close();
        }
        catch (Exception sqle)
        {
            rollBackTransaction(abd,conexion,sqle);
            resultadoEliminacion=0;

        }
        finally
        {
            commitTransaction(abd,conexion);
        }
        return resultadoEliminacion;
    }
    
    
    
    
    public int documentoVisibleExterior(GeneralValueObject gVO, String[] params){
        int result=0;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String codAplicacion    = (String)gVO.getAtributo("codigoAplicacion");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String codTramite       = (String)gVO.getAtributo("codTramite");
        String codDocumento     = (String)gVO.getAtributo("codDocumento");
        String visibleExterior = (String) gVO.getAtributo("visibleExt");
        m_Log.debug("visibleExterior"+visibleExterior);
        m_Log.debug("codDocumento"+codDocumento);
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            String sql="UPDATE A_PLT SET PLT_VIS_EXT=? WHERE PLT_COD=?";
            m_Log.debug(sql);
            PreparedStatement stmt=conexion.prepareStatement(sql);
            stmt.setString(1, visibleExterior);
            stmt.setInt(2, Integer.parseInt(codDocumento));
            result = stmt.executeUpdate();
            m_Log.debug("result:"+result);
        }catch(Exception sqle){
            rollBackTransaction(abd, conexion,sqle);
        }finally{
            commitTransaction(abd, conexion);
        }
        
        return result;
    }
    
    
    private void rollBackTransaction(AdaptadorSQLBD bd,Connection con,Exception e)
    {
        try
        {
            bd.rollBack(con);
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        finally
        {
            e.printStackTrace();
            m_Log.error(e.getMessage());
        }
    }

    private void commitTransaction(AdaptadorSQLBD bd,Connection con)
    {
        try
        {
            bd.finTransaccion(con);
            bd.devolverConexion(con);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
        }
    }


    public Vector loadProcedimientos(String[] params)
    {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try
        {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            // Creamos la select con los parametros adecuados.
            StringBuffer sql = new StringBuffer();

            sql.append("SELECT ")
                    .append(conf.getString("SQL.E_PML.codProcedimiento"))
                    .append(",")
                    .append(conf.getString("SQL.E_PML.valor"))
                    .append(" FROM E_PML, E_PRO WHERE ")
                    .append(conf.getString("SQL.E_PML.codCampoML"))
                    .append("='NOM' and ")
                    .append(conf.getString("SQL.E_PML.idioma")) // Parametrizarlo
                    .append("='" + conf.getString("idiomaDefecto")+"' and ")
                    .append(" PRO_COD = PML_COD and ")
                    .append(" ("+abd.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL,new String[]{"PRO_FLD",abd.funcionFecha(
                            AdaptadorSQLBD.FUNCIONFECHA_SYSDATE,null)}) +"<= "+abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE,null)
                            + " AND "+abd.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL,new String[]{"PRO_FLH",abd.funcionFecha(
                            AdaptadorSQLBD.FUNCIONFECHA_SYSDATE,null)})+" >= "+abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE,null) +") ")
                    .append(" ORDER BY PML_COD");

            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql.toString());
            int i=0;
            while(rs.next())
            {
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codigoProc", rs.getString(conf.getString("SQL.E_PML.codProcedimiento")));
                gVO.setAtributo("nombreProc", rs.getString(conf.getString("SQL.E_PML.valor")));
                r.add(gVO);
                i++;
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException sqle)
        {
            m_Log.error("Error de SQL en loadProcedimientos: " + sqle.toString());
        }
        catch (BDException bde)
        {
            if (m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo loadProcedimientos: " + bde.toString());
        }
        finally
        {
            if (conexion != null)
            {
                try
                {
                    abd.devolverConexion(conexion);
                }
                catch(BDException bde)
                {
                    m_Log.error("No se pudo devolver la conexion: " + bde.toString());
                }
            }
        }
        return r;
    }

    public Vector loadTramites(String[] params)
    {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try
        {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            // Creamos la select con los parametros adecuados.
            String sql = "";

            sql = "SELECT TRA_COU," + conf.getString("SQL.E_TRA.codProcedimiento") + "," +
                    conf.getString("SQL.E_TRA.codTramite") + "," +
                    conf.getString("SQL.E_TML.valor")  + " FROM E_TRA,E_TML WHERE " +
                    conf.getString("SQL.E_TRA.codProcedimiento")+ "=" + conf.getString("SQL.E_TML.codProcedimiento") +
                    " AND " + conf.getString("SQL.E_TRA.codTramite") + "=" + conf.getString("SQL.E_TML.codTramite") +
                    " AND " + conf.getString("SQL.E_TML.codCampoML") + "='NOM' AND " +
                    conf.getString("SQL.E_TML.idioma") + "='"+ conf.getString("idiomaDefecto")+"' AND TRA_FBA IS NULL ORDER BY TRA_PRO, TRA_COU";

            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql);
            int i=0;
            while(rs.next())
            {
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codigoProc", rs.getString(conf.getString("SQL.E_TRA.codProcedimiento")));
                gVO.setAtributo("codigoTramite", rs.getString(conf.getString("SQL.E_TRA.codTramite")));
                gVO.setAtributo("nombreTramite", rs.getString(conf.getString("SQL.E_TML.valor")));
                gVO.setAtributo("codigoTramiteVisible", rs.getString("TRA_COU"));
                r.add(gVO);
                i++;
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException sqle)
        {
            m_Log.error("Error de SQL en loadTramites: " + sqle.toString());
        }
        catch (BDException bde)
        {
            m_Log.error("error del OAD en el metodo loadTramites: " + bde.toString());
        }
        finally
        {
            if (conexion != null)
            {
                try
                {
                    abd.devolverConexion(conexion);
                }
                catch(BDException bde)
                {
                    m_Log.error("No se pudo devolver la conexion: " + bde.toString());
                }
            }
        }
        return r;
    }

    public Vector loadEtiquetas(GeneralValueObject gvo,String[] params) throws BDException {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        int posicion = 0;
        Config common = ConfigServiceHelper.getConfig("common");
        String sufijoEtiquetaFechaAlternativa = null;
        Vector listaIdiomas = (Vector) gvo.getAtributo("listaIdiomas");
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String codAplicacion = "";
            codAplicacion = (String) gvo.getAtributo("codigoAplicacion");
            m_Log.debug("--> Codigo de la aplicacion : " + codAplicacion);  
            String interesado = (String) gvo.getAtributo("interesado");
            m_Log.debug("--> Interesado : " + interesado); 
            String relacion = (String) gvo.getAtributo("relacion");
            m_Log.debug("--> Relacion : " + relacion);            
            sufijoEtiquetaFechaAlternativa = (String)gvo.getAtributo("sufijoEtiquetaFechaAlternativa");
            m_Log.debug("--> sufijoEtiquetaFechaAlternativa : " + sufijoEtiquetaFechaAlternativa);

            // Creamos la select con los parametros adecuados.
            StringBuffer sql = new StringBuffer();

            if(codAplicacion.equals("4")) {
              if(relacion.equals("N")) {
                 if(interesado.equals("N")) {

                     sql.append(" select " +
                            abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_'","CI.NOMEAS"}) +
                            " AS CODIGO,CI.NOME AS NOME ")
                            // prueba
                            .append("," + abd.convertir("CI.TIPO",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM CAMPOINFORME CI, CAMPOSELECCIONINFORME CSI, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" A_DOC.DOC_CEI = CSI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                            .append(" CSI.COD_CAMPOINFORME = CI.CODIGO")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_TITULOCARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_NOMBRECARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_DES AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ").append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                            // Nuevo
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_TRATAMIENTOCARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_TRA AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                            //
                            .append(" UNION SELECT "+ abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_'","PCA_COD"})  +" AS CODIGO,PCA_ROT AS NOME ")
                            // prueba

                            .append("," + abd.convertir("PCA_PLT",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_PCA ")
                            .append(" WHERE PCA_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" PCA_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" PCA_ACTIVO= 'SI'").append(" AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                            .append(" E_PCA.PCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"))
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_T'",abd.convertir("TCA_TRA", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null),"TCA_COD"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'E'","TCA_ROT"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("TCA_PLT",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_TCA ")
                            .append(" WHERE TCA_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" TCA_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" TCA_ACTIVO= 'SI'").append(" AND ")
                            .append(" ((TCA_VIS = 'S' AND TCA_TRA <> ").append(gvo.getAtributo("codTramite")).append(") OR (TCA_TRA = ")
                            .append(gvo.getAtributo("codTramite")).append("))").append(" AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                            .append(" E_TCA.TCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"))
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Nombre'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'NombreInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            // Nuevo
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'NombreCompleto'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Apel1'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Apellido1Interesado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Apel2'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Apellido2Interesado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            //
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Dom'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'DomicilioInteresado'","ROL_DES"})+"AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            //
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Mail'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Email'","ROL_DES"})+"AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            // 02/11/2007 - Se va a añadir el codigo postal como nuevo campo disponible a la lista de
                            // campos de las plantillas.
                            .append(" UNION SELECT ").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_CodPostal'","ROL_DES"})).append(" AS CODIGO,").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'CodigoPostal'","ROL_DES"})).append("AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Rol'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'RolInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Doc'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'DocumInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Pob'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'PoblacionInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")

                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Tlfno'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'TelefonoInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Provincia'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'ProvinciaInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                             
                            .append("UNION ")
                             // ORIGINAL
                            //.append("SELECT NVL('EXPEDIENTE_','') || NVL(EI.NOME,'') ||  NVL('_','') ||  NVL(COD_CAMPO,'') || NVL(ROL_DES,'') AS CODIGO,  ROTULO || NVL(ROL_DES,'') AS NOME ,")
                            //.append("NVL(TO_CHAR(COD_PLANTILLA),'')  AS TIPO ")
                            .append("SELECT " + abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_'","COD_CAMPO","ROL_DES"}) + " AS CODIGO," + abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"ROTULO","ROL_DES"}))
                            .append("," + abd.convertir("COD_PLANTILLA",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO ")
                            .append("FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, T_CAMPOS_EXTRA,E_ROL ")
                            .append("WHERE ROL_MUN=").append(gvo.getAtributo("codMunicipio")).append(" AND ROL_PRO='").append(gvo.getAtributo("codProcedimiento")).append("' ")
                            .append(" AND T_CAMPOS_EXTRA.COD_MUNICIPIO=" + (String)gvo.getAtributo("codMunicipio") + " AND  T_CAMPOS_EXTRA.ACTIVO = 'SI' AND  A_DOC.DOC_APL =").append(gvo.getAtributo("codigoAplicacion"))
                            .append(" AND  A_DOC.DOC_CEI = 2 AND  A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND  ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                            .append(" AND  T_CAMPOS_EXTRA.COD_PLANTILLA<>5")                             
                            .append(" ORDER BY 2");

                } else if(interesado.equals("S")) {

                    /*** Plantilla por interesado **/
                    sql.append(" select "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_'","CI.NOMEAS"})+" AS CODIGO,CI.NOME AS NOME ")
                            // prueba
                             .append("," + abd.convertir("CI.TIPO",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM CAMPOINFORME CI, CAMPOSELECCIONINFORME CSI, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" A_DOC.DOC_CEI = CSI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                            .append(" CSI.COD_CAMPOINFORME = CI.CODIGO")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_TITULOCARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_NOMBRECARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_DES AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                  			// Nuevo
                  			.append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_TRATAMIENTO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                            //
                  			.append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_'","PCA_COD"})+" AS CODIGO,PCA_ROT AS NOME ")
                            // prueba
                            .append("," + abd.convertir("PCA_PLT",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_PCA ")
                            .append(" WHERE PCA_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" PCA_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" PCA_ACTIVO = 'SI'").append(" AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                            .append(" E_PCA.PCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"))
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_T'",abd.convertir("TCA_TRA", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null),"TCA_COD"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'E'","TCA_ROT"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("TCA_PLT",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_TCA ")
                            .append(" WHERE TCA_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" TCA_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" TCA_ACTIVO = 'SI'").append(" AND ")
                            .append(" ((TCA_VIS = 'S' AND TCA_TRA <> ").append(gvo.getAtributo("codTramite")).append(") OR (TCA_TRA = ")
                            .append(gvo.getAtributo("codTramite")).append("))").append(" AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                            .append(" E_TCA.TCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"))
                                                        
                            .append("UNION ")
                            //.append("SELECT  NVL(EI.NOME,'') ||  NVL('_','') ||  NVL(COD_CAMPO,'') AS CODIGO, ROTULO AS NOME , ")
                            //.append("NVL(TO_CHAR(COD_PLANTILLA),'')  AS TIPO ")                                                        
                            .append(" SELECT " + abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_'","COD_CAMPO"}) + " AS CODIGO,ROTULO AS NOME, ")
                            .append(abd.convertir("COD_PLANTILLA",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO ")
                            .append("FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, T_CAMPOS_EXTRA ")
                            .append("WHERE T_CAMPOS_EXTRA.COD_MUNICIPIO =" + (String)gvo.getAtributo("codMunicipio") + " AND  T_CAMPOS_EXTRA.ACTIVO = 'SI' AND  A_DOC.DOC_APL =" + (String)gvo.getAtributo("codigoAplicacion") + " AND  A_DOC.DOC_CEI = 5 AND  A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND  ESI.COD_ENTIDADEINFORME = EI.CODIGO AND  T_CAMPOS_EXTRA.COD_PLANTILLA<>5 ")
                            .append(" ORDER BY 2");
                    
                }
              } else {

                    /**** plantilla para relación entre expedientes ******/
                   sql.append(" select " +
                          abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_'","CI.NOMEAS"}) +
                          " AS CODIGO,CI.NOME AS NOME ")
                          // prueba
                          .append("," + abd.convertir("CI.TIPO",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                          // prueba
                          .append(" FROM CAMPOINFORME CI, CAMPOSELECCIONINFORME CSI, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                          .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                          .append(" A_DOC.DOC_CEI = 7 AND ")
                          .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                          .append(" A_DOC.DOC_CEI = CSI.COD_ESTRUCTURA AND ")
                          .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                          .append(" CSI.COD_CAMPOINFORME = CI.CODIGO ")
                          .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_TITULOCARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
                           // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                           // prueba
                          .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                          .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                          .append(" A_DOC.DOC_CEI = 7 AND ")
                          .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                          .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                          .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_NOMBRECARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_DES AS NOME ")
                          // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                           // prueba
                          .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                          .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                          .append(" A_DOC.DOC_CEI = 7 AND ")
                          .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                          .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                  			.append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_TRATAMIENTO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                           // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 7 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")

                          .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_T'",abd.convertir("TCA_TRA", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null),"TCA_COD"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'E'","TCA_ROT"})+" AS NOME ")
                          // prueba
                            .append("," + abd.convertir("TCA_PLT",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                           // prueba
                          .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_TCA ")
                          .append(" WHERE TCA_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                          .append(" TCA_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                          .append(" TCA_ACTIVO = 'SI'").append(" AND ")
                          .append(" ((TCA_VIS = 'S' AND TCA_TRA <> ").append(gvo.getAtributo("codTramite")).append(") OR (TCA_TRA = ")
                          .append(gvo.getAtributo("codTramite")).append("))").append(" AND ")
                          .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                          .append(" A_DOC.DOC_CEI = 7 AND ")
                          .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                          .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                          .append(" E_TCA.TCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"))
                           
                          .append(" UNION")
                          //.append(" SELECT NVL(EI.NOME,'') || NVL('_','') || NVL(COD_CAMPO,'') AS CODIGO, ROTULO AS NOME, NVL(TO_CHAR(COD_PLANTILLA),'') AS TIPO")                          
                          .append(" SELECT " + abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_'","COD_CAMPO"}) + " AS CODIGO,ROTULO AS NOME, ")
                          .append(abd.convertir("COD_PLANTILLA",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO ")
                          .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, T_CAMPOS_EXTRA,  E_ROL") 
                          .append(" WHERE ROL_MUN=").append(gvo.getAtributo("codMunicipio")).append(" AND ROL_PRO='").append(gvo.getAtributo("codProcedimiento")).append("' ")
                          .append(" AND T_CAMPOS_EXTRA.COD_MUNICIPIO=" + gvo.getAtributo("codMunicipio") + " AND  T_CAMPOS_EXTRA.ACTIVO = 'SI' AND  A_DOC.DOC_APL =").append(gvo.getAtributo("codigoAplicacion"))
                          .append(" AND  A_DOC.DOC_CEI = 7 AND  A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND  ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                          .append(" AND  T_CAMPOS_EXTRA.COD_PLANTILLA<>5")
                           
                          .append(" ORDER BY 2");
              }
            } else {
                sql.append(" select "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_'","CI.NOMEAS"})+" AS CODIGO,CI.NOME AS NOME ")
                         // prueba
                         .append("," + abd.convertir("CI.TIPO",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                          // prueba
                        .append(" FROM CAMPOINFORME CI, CAMPOSELECCIONINFORME CSI, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                        .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                        .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                        .append(" A_DOC.DOC_CEI = CSI.COD_ESTRUCTURA AND ")
                        .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                        .append(" CSI.COD_CAMPOINFORME = CI.CODIGO ")
                        .append(" ORDER BY 2");
            }

            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql.toString());


            while(rs.next())
            {
                GeneralValueObject gVO = new GeneralValueObject();
                String codigo = rs.getString("CODIGO");
                String cod = "";
                StringTokenizer valores = null;
                if (codigo != null)
                {
                    valores = new StringTokenizer(codigo," ()/ºª@%&",false);
                    while (valores.hasMoreTokens())
                    {
                        String valor = valores.nextToken();
                        cod += valor;
                    }

                    // Eliminar digitos del principio.
                    posicion = 	cod.indexOf("_");
                    boolean digPrincipio=true;
                    while (digPrincipio && cod.length()>0)
                    {
                        if (cod.charAt(posicion+1)>='0' && cod.charAt(posicion+1)<='9' )
                            cod = cod.substring(0,posicion+1)+cod.substring(posicion+2);
                        else digPrincipio = false;
                    }
                    if (m_Log.isDebugEnabled()) m_Log.debug("--> " + cod);
                }
                gVO.setAtributo("codigo", cod);
                if (m_Log.isDebugEnabled()) m_Log.debug("CODIGO: " + gVO.getAtributo("codigo"));
                gVO.setAtributo("nombre", rs.getString("NOME"));
                if (m_Log.isDebugEnabled()) m_Log.debug("NOMBRE: " + gVO.getAtributo("nombre"));




                /** prueba ***/                
                gVO.setAtributo("tipoCampo",rs.getString("TIPO"));
                if (m_Log.isDebugEnabled()) m_Log.debug("TIPO: " + gVO.getAtributo("tipoCampo"));
                
                /** prueba **/


                r.add(gVO);
                if("6".equals(gVO.getAtributo("tipoCampo")) && listaIdiomas.size()>1){	
                    GeneralValueObject desplegableTraducido = new GeneralValueObject();	
                    GeneralValueObject idioma = (GeneralValueObject) listaIdiomas.get(1);	
                    desplegableTraducido.setAtributo("codigo", cod + "_" + ((String) idioma.getAtributo("descripcion")).toUpperCase());	
                    desplegableTraducido.setAtributo("nombre", rs.getString("NOME") + " " + ((String) idioma.getAtributo("descripcion")).toUpperCase());	
                    desplegableTraducido.setAtributo("tipoCampo",rs.getString("TIPO"));	
                    r.add(desplegableTraducido);	
                    if (m_Log.isDebugEnabled()) m_Log.debug("CODIGO: " + desplegableTraducido.getAtributo("codigo"));	
                    if (m_Log.isDebugEnabled()) m_Log.debug("NOMBRE: " + desplegableTraducido.getAtributo("nombre"));	
                    if (m_Log.isDebugEnabled()) m_Log.debug("TIPO: " + desplegableTraducido.getAtributo("tipoCampo"));	
                }
            }
            /*  if(codAplicacion.equals("4") && interesado.equals("N")) {
               GeneralValueObject gVO = new GeneralValueObject();
               gVO.setAtributo("codigo", "EXPEDIENTE_FecActEsp");
            gVO.setAtributo("nombre", "FECHA ACTUAL ESPAÑOL");
            r.add(gVO);
            gVO = new GeneralValueObject();
            gVO.setAtributo("codigo", "EXPEDIENTE_FecActGal");
            gVO.setAtributo("nombre", "FECHA ACTUAL GALLEGO");
            r.add(gVO);
            */
            // }
            /*if(codAplicacion.equals("4") && interesado.equals("S")) {
               GeneralValueObject gVO = new GeneralValueObject();
               gVO.setAtributo("codigo", "EXPEDIENTEINTERESADO_FecActEsp");
            gVO.setAtributo("nombre", "FECHA ACTUAL ESPAÑOL");
            r.add(gVO);
            gVO = new GeneralValueObject();
            gVO.setAtributo("codigo", "EXPEDIENTEINTERESADO_FecActGal");
            gVO.setAtributo("nombre", "FECHA ACTUAL GALLEGO");
            r.add(gVO);
            */
            //  }
            rs.close();
            stmt.close();
        } catch (SQLException sqle) {
            m_Log.error("Error de SQL en loadEtiquetas: " + sqle.toString());
            throw new BDException(sqle.toString());
        } finally {
            if (conexion != null) abd.devolverConexion(conexion);
        }


        boolean formatoFechaAlternativo = false;
        try{
            ResourceBundle config = ResourceBundle.getBundle("documentos");
            String formatoFechas = config.getString(((String)gvo.getAtributo("codMunicipio")) + ConstantesDatos.FORMATO_ALTERNATIVO_FECHAS_DOC_TRAMITACION);
            if(formatoFechas!=null && !"".equals(formatoFechas))
                formatoFechaAlternativo = true;

        }catch(Exception e){
            e.printStackTrace();
        }

        Vector aux = new Vector();

        if(formatoFechaAlternativo){
            for(int i=0;r!=null && i<r.size();i++){
                GeneralValueObject gVO = (GeneralValueObject)r.get(i);
                String codigo = (String)gVO.getAtributo("codigo");
                String nombre = (String)gVO.getAtributo("nombre");
                String tipoCampo = (String)gVO.getAtributo("tipoCampo");
                if(tipoCampo!=null && (tipoCampo.equalsIgnoreCase(ConstantesDatos.TIPO_DATO_FECHAS_ETIQUETAS_DOCS_TRAMITACION) ||
                        tipoCampo.equalsIgnoreCase(ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA)) && !"EXPEDIENTE_FECACTESP".equalsIgnoreCase(codigo)
                        && !"EXPEDIENTE_FECACTGAL".equalsIgnoreCase(codigo) && !"RELACION_FECACTESP".equalsIgnoreCase(codigo) && !"RELACION_FECACTGAL".equalsIgnoreCase(codigo)
                        && !"EXPEDIENTEINTERESADO_FECACTESP".equalsIgnoreCase(codigo) && !"EXPEDIENTEINTERESADO_FECACTGAL".equalsIgnoreCase(codigo)
                        && !"EXPEDIENTE_FECACTEU".equalsIgnoreCase(codigo) && !"EXPEDIENTEINTERESADO_FECACTEU".equalsIgnoreCase(codigo)
                        && !"RELACION_FECACTEU".equalsIgnoreCase(codigo)){
                    
                    // Se trata de una etiqueta fecha o de un campo suplementario de tipo fecha
                    GeneralValueObject nuevo = new GeneralValueObject();                    
                    nuevo.setAtributo("codigo",codigo + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                    nuevo.setAtributo("nombre",nombre + sufijoEtiquetaFechaAlternativa);
                    nuevo.setAtributo("tipoCampo",tipoCampo);
                    aux.add(nuevo);
                }

                aux.add(gVO);
            }// for
            r.clear();
            r = aux;
        }// if


        return r;
    }

     public Vector loadEtiquetasPlantillaODT(GeneralValueObject gvo,String[] params) throws BDException {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        int posicion = 0;
        Config common = ConfigServiceHelper.getConfig("common");
        String sufijoEtiquetaFechaAlternativa = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String codAplicacion = "";
            codAplicacion = (String) gvo.getAtributo("codigoAplicacion");
            m_Log.debug("--> Codigo de la aplicacion : " + codAplicacion);  
            String interesado = (String) gvo.getAtributo("interesado");
            m_Log.debug("--> Interesado : " + interesado); 
            String relacion = (String) gvo.getAtributo("relacion");
            m_Log.debug("--> Relacion : " + relacion);            
            sufijoEtiquetaFechaAlternativa = (String)gvo.getAtributo("sufijoEtiquetaFechaAlternativa");
            m_Log.debug("--> sufijoEtiquetaFechaAlternativa : " + sufijoEtiquetaFechaAlternativa);

            // Creamos la select con los parametros adecuados.
            StringBuffer sql = new StringBuffer();

            if(codAplicacion.equals("4")) {
              if(relacion.equals("N")) {
                 if(interesado.equals("N")) {

                     sql.append(" select " +
                            abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"CI.NOMEAS"}) +
                            " AS CODIGO,CI.NOME AS NOME ")
                            // prueba
                            .append("," + abd.convertir("CI.TIPO",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM CAMPOINFORME CI, CAMPOSELECCIONINFORME CSI, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" A_DOC.DOC_CEI = CSI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                            .append(" CSI.COD_CAMPOINFORME = CI.CODIGO")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'TITULOCARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'NOMBRECARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_DES AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ").append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                            // Nuevo
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'TRATAMIENTOCARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_TRA AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                            //
                            .append(" UNION SELECT "+ abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"PCA_COD"})  +" AS CODIGO,PCA_ROT AS NOME ")
                            // prueba

                            .append("," + abd.convertir("PCA_PLT",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_PCA ")
                            .append(" WHERE PCA_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" PCA_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" PCA_ACTIVO= 'SI'").append(" AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                            .append(" E_PCA.PCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"))
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'T'",abd.convertir("TCA_TRA", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null),"TCA_COD"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'E'","TCA_ROT"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("TCA_PLT",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_TCA ")
                            .append(" WHERE TCA_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" TCA_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" TCA_ACTIVO= 'SI'").append(" AND ")
                            .append(" ((TCA_VIS = 'S' AND TCA_TRA <> ").append(gvo.getAtributo("codTramite")).append(") OR (TCA_TRA = ")
                            .append(gvo.getAtributo("codTramite")).append("))").append(" AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                            .append(" E_TCA.TCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"))
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'item.'","'Nombre'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'NombreInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            // Nuevo
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'item.'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'NombreCompleto'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'item.'","'Apel1'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Apellido1Interesado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'item.'","'Apel2'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Apellido2Interesado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            //
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'item.'","'Dom'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'DomicilioInteresado'","ROL_DES"})+"AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            //
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'item.'","'Mail'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Email'","ROL_DES"})+"AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            // 02/11/2007 - Se va a añadir el codigo postal como nuevo campo disponible a la lista de
                            // campos de las plantillas.
                            .append(" UNION SELECT ").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'item.'","'CodPostal'","ROL_DES"})).append(" AS CODIGO,").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'CodigoPostal'","ROL_DES"})).append("AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'item.'","'Rol'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'RolInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'item.'","'Doc'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'DocumInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'item.'","'Pob'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'PoblacionInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")

                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'item.'","'Tlfno'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'TelefonoInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'item.'","'Provincia'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'ProvinciaInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                             
                            .append("UNION ")
                             // ORIGINAL
                            //.append("SELECT NVL('EXPEDIENTE_','') || NVL(EI.NOME,'') ||  NVL('_','') ||  NVL(COD_CAMPO,'') || NVL(ROL_DES,'') AS CODIGO,  ROTULO || NVL(ROL_DES,'') AS NOME ,")
                            //.append("NVL(TO_CHAR(COD_PLANTILLA),'')  AS TIPO ")
                            .append("SELECT " + abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"COD_CAMPO","ROL_DES"}) + " AS CODIGO," + abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"ROTULO","ROL_DES"}))
                            .append("," + abd.convertir("COD_PLANTILLA",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO ")
                            .append("FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, T_CAMPOS_EXTRA,E_ROL ")
                            .append("WHERE ROL_MUN=").append(gvo.getAtributo("codMunicipio")).append(" AND ROL_PRO='").append(gvo.getAtributo("codProcedimiento")).append("' ")
                            .append(" AND T_CAMPOS_EXTRA.COD_MUNICIPIO=" + (String)gvo.getAtributo("codMunicipio") + " AND  T_CAMPOS_EXTRA.ACTIVO = 'SI' AND  A_DOC.DOC_APL =").append(gvo.getAtributo("codigoAplicacion"))
                            .append(" AND  A_DOC.DOC_CEI = 2 AND  A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND  ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                            .append(" AND  T_CAMPOS_EXTRA.COD_PLANTILLA<>5")                             
                            .append(" ORDER BY 2");

                } else if(interesado.equals("S")) {

                    /*** Plantilla por interesado **/
                    sql.append(" select "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"CI.NOMEAS"})+" AS CODIGO,CI.NOME AS NOME ")
                            // prueba
                             .append("," + abd.convertir("CI.TIPO",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM CAMPOINFORME CI, CAMPOSELECCIONINFORME CSI, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" A_DOC.DOC_CEI = CSI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                            .append(" CSI.COD_CAMPOINFORME = CI.CODIGO")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'TITULOCARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'NOMBRECARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_DES AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                  			// Nuevo
                  			.append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'TRATAMIENTO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                            //
                  			.append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"PCA_COD"})+" AS CODIGO,PCA_ROT AS NOME ")
                            // prueba
                            .append("," + abd.convertir("PCA_PLT",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_PCA ")
                            .append(" WHERE PCA_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" PCA_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" PCA_ACTIVO = 'SI'").append(" AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                            .append(" E_PCA.PCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"))
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'T'",abd.convertir("TCA_TRA", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null),"TCA_COD"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'E'","TCA_ROT"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("TCA_PLT",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_TCA ")
                            .append(" WHERE TCA_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" TCA_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" TCA_ACTIVO = 'SI'").append(" AND ")
                            .append(" ((TCA_VIS = 'S' AND TCA_TRA <> ").append(gvo.getAtributo("codTramite")).append(") OR (TCA_TRA = ")
                            .append(gvo.getAtributo("codTramite")).append("))").append(" AND ")
                            .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                            .append(" E_TCA.TCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"))
                                                        
                            .append("UNION ")
                            //.append("SELECT  NVL(EI.NOME,'') ||  NVL('_','') ||  NVL(COD_CAMPO,'') AS CODIGO, ROTULO AS NOME , ")
                            //.append("NVL(TO_CHAR(COD_PLANTILLA),'')  AS TIPO ")                                                        
                            .append(" SELECT " + abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"COD_CAMPO"}) + " AS CODIGO,ROTULO AS NOME, ")
                            .append(abd.convertir("COD_PLANTILLA",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO ")
                            .append("FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, T_CAMPOS_EXTRA ")
                            .append("WHERE T_CAMPOS_EXTRA.COD_MUNICIPIO =" + (String)gvo.getAtributo("codMunicipio") + " AND  T_CAMPOS_EXTRA.ACTIVO = 'SI' AND  A_DOC.DOC_APL =" + (String)gvo.getAtributo("codigoAplicacion") + " AND  A_DOC.DOC_CEI = 5 AND  A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND  ESI.COD_ENTIDADEINFORME = EI.CODIGO AND  T_CAMPOS_EXTRA.COD_PLANTILLA<>5 ")
                            .append(" ORDER BY 2");
                    
                }
              } else {

                    /**** plantilla para relación entre expedientes ******/
                   sql.append(" select " +
                          abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"CI.NOMEAS"}) +
                          " AS CODIGO,CI.NOME AS NOME ")
                          // prueba
                          .append("," + abd.convertir("CI.TIPO",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                          // prueba
                          .append(" FROM CAMPOINFORME CI, CAMPOSELECCIONINFORME CSI, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                          .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                          .append(" A_DOC.DOC_CEI = 7 AND ")
                          .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                          .append(" A_DOC.DOC_CEI = CSI.COD_ESTRUCTURA AND ")
                          .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                          .append(" CSI.COD_CAMPOINFORME = CI.CODIGO ")
                          .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'TITULOCARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
                           // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                           // prueba
                          .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                          .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                          .append(" A_DOC.DOC_CEI = 7 AND ")
                          .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                          .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                          .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'NOMBRECARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_DES AS NOME ")
                          // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                           // prueba
                          .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                          .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                          .append(" A_DOC.DOC_CEI = 7 AND ")
                          .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                          .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                  			.append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'TRATAMIENTO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                           // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 7 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")

                          .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'T'",abd.convertir("TCA_TRA", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null),"TCA_COD"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'E'","TCA_ROT"})+" AS NOME ")
                          // prueba
                            .append("," + abd.convertir("TCA_PLT",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                           // prueba
                          .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_TCA ")
                          .append(" WHERE TCA_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                          .append(" TCA_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                          .append(" TCA_ACTIVO = 'SI'").append(" AND ")
                          .append(" ((TCA_VIS = 'S' AND TCA_TRA <> ").append(gvo.getAtributo("codTramite")).append(") OR (TCA_TRA = ")
                          .append(gvo.getAtributo("codTramite")).append("))").append(" AND ")
                          .append(" A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                          .append(" A_DOC.DOC_CEI = 7 AND ")
                          .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                          .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                          .append(" E_TCA.TCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"))
                           
                          .append(" UNION")
                          //.append(" SELECT NVL(EI.NOME,'') || NVL('_','') || NVL(COD_CAMPO,'') AS CODIGO, ROTULO AS NOME, NVL(TO_CHAR(COD_PLANTILLA),'') AS TIPO")                          
                          .append(" SELECT " + abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"COD_CAMPO"}) + " AS CODIGO,ROTULO AS NOME, ")
                          .append(abd.convertir("COD_PLANTILLA",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO ")
                          .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, T_CAMPOS_EXTRA,  E_ROL") 
                          .append(" WHERE ROL_MUN=").append(gvo.getAtributo("codMunicipio")).append(" AND ROL_PRO='").append(gvo.getAtributo("codProcedimiento")).append("' ")
                          .append(" AND T_CAMPOS_EXTRA.COD_MUNICIPIO=" + gvo.getAtributo("codMunicipio") + " AND  T_CAMPOS_EXTRA.ACTIVO = 'SI' AND  A_DOC.DOC_APL =").append(gvo.getAtributo("codigoAplicacion"))
                          .append(" AND  A_DOC.DOC_CEI = 7 AND  A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND  ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                          .append(" AND  T_CAMPOS_EXTRA.COD_PLANTILLA<>5")
                           
                          .append(" ORDER BY 2");
              }
            } else {
                sql.append(" select "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"CI.NOMEAS"})+" AS CODIGO,CI.NOME AS NOME ")
                         // prueba
                         .append("," + abd.convertir("CI.TIPO",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                          // prueba
                        .append(" FROM CAMPOINFORME CI, CAMPOSELECCIONINFORME CSI, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                        .append(" WHERE A_DOC.DOC_APL = ").append(gvo.getAtributo("codigoAplicacion")).append(" AND ")
                        .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                        .append(" A_DOC.DOC_CEI = CSI.COD_ESTRUCTURA AND ")
                        .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                        .append(" CSI.COD_CAMPOINFORME = CI.CODIGO ")
                        .append(" ORDER BY 1");
            }

            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql.toString());


            while(rs.next())
            {
                GeneralValueObject gVO = new GeneralValueObject();
                String codigo = rs.getString("CODIGO");
                String cod = "";
                StringTokenizer valores = null;
                if (codigo != null)
                {
                    valores = new StringTokenizer(codigo," ()/ºª@%&",false);
                    while (valores.hasMoreTokens())
                    {
                        String valor = valores.nextToken();
                        cod += valor;
                    }

                    // Eliminar digitos del principio.
                    posicion = 	cod.indexOf("_");
                    boolean digPrincipio=true;
                    while (digPrincipio && cod.length()>0)
                    {
                        if (cod.charAt(posicion+1)>='0' && cod.charAt(posicion+1)<='9' )
                            cod = cod.substring(0,posicion+1)+cod.substring(posicion+2);
                        else digPrincipio = false;
                    }
                    if (m_Log.isDebugEnabled()) m_Log.debug("--> " + cod);
                }
                gVO.setAtributo("codigo", cod);
                if (m_Log.isDebugEnabled()) m_Log.debug("CODIGO: " + gVO.getAtributo("codigo"));
                gVO.setAtributo("nombre", rs.getString("NOME"));
                if (m_Log.isDebugEnabled()) m_Log.debug("NOMBRE: " + gVO.getAtributo("nombre"));




                /** prueba ***/                
                gVO.setAtributo("tipoCampo",rs.getString("TIPO"));
                if (m_Log.isDebugEnabled()) m_Log.debug("TIPO: " + gVO.getAtributo("tipoCampo"));
                
                /** prueba **/


                r.add(gVO);
            }
            /*  if(codAplicacion.equals("4") && interesado.equals("N")) {
               GeneralValueObject gVO = new GeneralValueObject();
               gVO.setAtributo("codigo", "EXPEDIENTE_FecActEsp");
            gVO.setAtributo("nombre", "FECHA ACTUAL ESPAÑOL");
            r.add(gVO);
            gVO = new GeneralValueObject();
            gVO.setAtributo("codigo", "EXPEDIENTE_FecActGal");
            gVO.setAtributo("nombre", "FECHA ACTUAL GALLEGO");
            r.add(gVO);
            */
            // }
            /*if(codAplicacion.equals("4") && interesado.equals("S")) {
               GeneralValueObject gVO = new GeneralValueObject();
               gVO.setAtributo("codigo", "EXPEDIENTEINTERESADO_FecActEsp");
            gVO.setAtributo("nombre", "FECHA ACTUAL ESPAÑOL");
            r.add(gVO);
            gVO = new GeneralValueObject();
            gVO.setAtributo("codigo", "EXPEDIENTEINTERESADO_FecActGal");
            gVO.setAtributo("nombre", "FECHA ACTUAL GALLEGO");
            r.add(gVO);
            */
            //  }
            rs.close();
            stmt.close();
        } catch (SQLException sqle) {
            m_Log.error("Error de SQL en loadEtiquetas: " + sqle.toString());
            throw new BDException(sqle.toString());
        } finally {
            if (conexion != null) abd.devolverConexion(conexion);
        }


        boolean formatoFechaAlternativo = false;
        try{
            ResourceBundle config = ResourceBundle.getBundle("documentos");
            String formatoFechas = config.getString(((String)gvo.getAtributo("codMunicipio")) + ConstantesDatos.FORMATO_ALTERNATIVO_FECHAS_DOC_TRAMITACION);
            if(formatoFechas!=null && !"".equals(formatoFechas))
                formatoFechaAlternativo = true;

        }catch(Exception e){
            e.printStackTrace();
        }

        Vector aux = new Vector();

        if(formatoFechaAlternativo){
            for(int i=0;r!=null && i<r.size();i++){
                GeneralValueObject gVO = (GeneralValueObject)r.get(i);
                String codigo = (String)gVO.getAtributo("codigo");
                String nombre = (String)gVO.getAtributo("nombre");
                String tipoCampo = (String)gVO.getAtributo("tipoCampo");
                if(tipoCampo!=null && (tipoCampo.equalsIgnoreCase(ConstantesDatos.TIPO_DATO_FECHAS_ETIQUETAS_DOCS_TRAMITACION) ||
                        tipoCampo.equalsIgnoreCase(ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA)) && !"EXPEDIENTE_FECACTESP".equalsIgnoreCase(codigo)
                        && !"EXPEDIENTE_FECACTGAL".equalsIgnoreCase(codigo) && !"RELACION_FECACTESP".equalsIgnoreCase(codigo) && !"RELACION_FECACTGAL".equalsIgnoreCase(codigo)
                        && !"EXPEDIENTEINTERESADO_FECACTESP".equalsIgnoreCase(codigo) && !"EXPEDIENTEINTERESADO_FECACTGAL".equalsIgnoreCase(codigo)
                        && !"EXPEDIENTE_FECACTEU".equalsIgnoreCase(codigo) && !"EXPEDIENTEINTERESADO_FECACTEU".equalsIgnoreCase(codigo)
                        && !"RELACION_FECACTEU".equalsIgnoreCase(codigo)){
                    
                    // Se trata de una etiqueta fecha o de un campo suplementario de tipo fecha
                    GeneralValueObject nuevo = new GeneralValueObject();                    
                    nuevo.setAtributo("codigo",codigo + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                    nuevo.setAtributo("nombre",nombre + sufijoEtiquetaFechaAlternativa);
                    nuevo.setAtributo("tipoCampo",tipoCampo);
                    aux.add(nuevo);
                }

                aux.add(gVO);
            }// for
            r.clear();
            r = aux;
        }// if


        return r;
    }

    public int grabarDocumento(GeneralValueObject gvo,String[] params){
        int resultado=0;
        if (m_Log.isDebugEnabled()){
            m_Log.debug("Entramos en Grabar Documentos");
            m_Log.debug("Aplicacion ::"+gvo.getAtributo("codigoAplicacion"));
            m_Log.debug("Procedimiento ::"+gvo.getAtributo("codProcedimiento"));
            m_Log.debug("Tramite ::"+gvo.getAtributo("codTramite"));
            m_Log.debug("codDocumento ::"+gvo.getAtributo("codDocumento"));
             m_Log.debug("Fichero bytes::"+(byte[])gvo.getAtributo("ficheroWord"));
            m_Log.debug("Nombre ::"+gvo.getAtributo("nombreDocumento"));
            m_Log.debug("Interesado ::"+gvo.getAtributo("interesado"));
            m_Log.debug("Relacion ::"+gvo.getAtributo("relacion"));
            m_Log.debug("Documento Activo? ::"+gvo.getAtributo("docActivo"));
        }
        String codMunicipio    = (String)gvo.getAtributo("codMunicipio");
        String codAplicacion    = (String)gvo.getAtributo("codigoAplicacion");
        String codProcedimiento = (String)gvo.getAtributo("codProcedimiento");
        String codTramite       = (String)gvo.getAtributo("codTramite");
        String codDocumento     = (String)gvo.getAtributo("codDocumento");
        String nombreDocumento  = (String)gvo.getAtributo("nombreDocumento");
        String interesado = (String) gvo.getAtributo("interesado");
        String relacion = (String) gvo.getAtributo("relacion");
        String documentoActivo = (String) gvo.getAtributo("docActivo");
        byte[] ficheroWord      = (byte[])gvo.getAtributo("ficheroWord");
        String editorTexto = (String) gvo.getAtributo("editorTexto");
        if(("".equals(editorTexto))|| (editorTexto==null)) editorTexto="WORD";

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt;
        ResultSet rs;

        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            String sql;
            String plt_cod = "", cod_dot = "";

            if(codDocumento!=null && !codDocumento.equals("")){
                sql = "UPDATE A_PLT SET PLT_DES = ?, PLT_DOC = ? WHERE PLT_COD = ?";
            }else{
                sql = "select " + abd.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, new String[] {abd.funcionMatematica(
                        AdaptadorSQLBD.FUNCIONMATEMATICA_MAX, new String[] {aplt_cod}) + "+1", "1"}) + " as " +
                        aplt_cod + " from A_PLT";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt = conexion.prepareStatement(sql);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    plt_cod = rs.getString(aplt_cod);
                }
                rs.close();
                stmt.close();
                sql = "INSERT INTO A_PLT (PLT_COD,PLT_DES,PLT_APL,PLT_DOC,PLT_PRO,PLT_TRA,PLT_INT,PLT_REL,PLT_EDITOR_TEXTO) VALUES (" +
                        Integer.parseInt(plt_cod) + ",?,?,?,?,?,?,?,?)";
            }
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            if(codDocumento!=null && !codDocumento.equals("")){
                stmt.setString(1,nombreDocumento);
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                stmt.setBinaryStream(2,st,ficheroWord.length);
                stmt.setString(3,codDocumento);
            }else{
                stmt.setString(1,nombreDocumento);
                stmt.setInt(2,Integer.parseInt(codAplicacion));
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                stmt.setBinaryStream(3,st,ficheroWord.length);
                stmt.setString(4,codProcedimiento);
                if (codTramite!=null && !codTramite.equals("")) {
                    stmt.setInt(5,Integer.parseInt(codTramite));
                }else {
                    stmt.setString(5, null);
                }
                stmt.setString(6,interesado);
                stmt.setString(7,relacion);
                stmt.setString(8,editorTexto);
            }
            resultado = stmt.executeUpdate();
            stmt.close();

            if (codAplicacion.equalsIgnoreCase("4")) {
                if(codDocumento!=null && !codDocumento.equals("")){
                    // Habrá que actualizar la ACTIVACION o NO del DOCUMENTO, desde la APLICACION DE DOCUMENTOS
                    sql = "UPDATE E_DOT set DOT_ACTIVO = '" + documentoActivo + "' WHERE DOT_MUN = " +codMunicipio+
                            " and DOT_PRO = '" +codProcedimiento+ "' and DOT_TRA = "+codTramite+ " and DOT_PLT = " + codDocumento;
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt = conexion.prepareStatement(sql);
                    resultado = stmt.executeUpdate();
                    // Fin de actualizacion de ACTIVACION o NO del DOCUMENTO
                }else{
                    sql = "select " + abd.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, new String[] {abd.funcionMatematica(
                            AdaptadorSQLBD.FUNCIONMATEMATICA_MAX, new String[] {dot_cod}) + "+1", "1"}) + " as " +
                            dot_cod + " from E_DOT where dot_pro='"+codProcedimiento+"' and dot_tra="+codTramite;
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt = conexion.prepareStatement(sql);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        cod_dot = rs.getString(dot_cod);
                    }
                    rs.close();
                    stmt.close();
                    sql = "select " + abd.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, new String[] {abd.funcionMatematica(
                            AdaptadorSQLBD.FUNCIONMATEMATICA_MAX, new String[] {aplt_cod}), "1"}) + " as " +
                            aplt_cod + " from A_PLT";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt = conexion.prepareStatement(sql);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        plt_cod = rs.getString(aplt_cod);
                    }
                    rs.close();
                    stmt.close();
                    sql = "INSERT INTO E_DOT (DOT_MUN,DOT_PRO,DOT_TRA,DOT_COD,DOT_TDO,DOT_VIS,DOT_FRM" +
                            ",DOT_PLT, DOT_ACTIVO) VALUES (" + codMunicipio + ",'" + codProcedimiento + "'," +
                            codTramite + "," + cod_dot + ",NULL,'S',NULL," +
                            plt_cod + ", '" + documentoActivo + "')";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt = conexion.prepareStatement(sql);
                    resultado = stmt.executeUpdate();
                }
                stmt.close();
            }
        }catch (Exception sqle){
            rollBackTransaction(abd,conexion,sqle);
            resultado=0;
        }finally{
            m_Log.debug("SE REALIZA UN COMMIT");
            commitTransaction(abd,conexion);
        }
        return resultado;
    }

    public int modificarInteresado(GeneralValueObject gvo,String[] params){
        int resultado=0;
        String interesado = (String)gvo.getAtributo("interesado");
        String codAplicacion = (String) gvo.getAtributo("codigoAplicacion");
        String codProcedimiento = (String) gvo.getAtributo("codProcedimiento");
        String codTramite = (String) gvo.getAtributo("codTramite");
        int codDoc = 0;

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            String sql = "";
            sql = "select PLT_COD FROM A_PLT";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                codDoc = rs.getInt("PLT_COD");
            }
            codDoc = codDoc + 1;
            rs.close();
            stmt.close();

            sql = "INSERT INTO A_PLT VALUES (" + codDoc + ",null," +
                    codAplicacion + ",null,'" + codProcedimiento + "'," + codTramite +
                    ",'" + interesado + "')";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.prepareStatement(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
        }catch (Exception sqle){
            rollBackTransaction(abd,conexion,sqle);
            resultado=0;
        }finally{
            commitTransaction(abd,conexion);
        }
        return codDoc;
    }

    public GeneralValueObject loadDocumento(GeneralValueObject gvo,String[] params)
    {
        GeneralValueObject r = new GeneralValueObject();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            // Creamos la select con los parametros adecuados.
            StringBuffer sql = new StringBuffer();
            sql.append(" select PLT_DES, PLT_DOC ")
                    .append(" FROM A_PLT ")
                    .append(" WHERE PLT_COD = ").append(gvo.getAtributo("codDocumento"));

            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql.toString());
            while(rs.next())
            {
                r.setAtributo("nombre",rs.getString("PLT_DES"));
                java.io.InputStream st = rs.getBinaryStream("PLT_DOC");
                java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                int c;
                while ((c = st.read())!= -1){
                    ot.write(c);
                }
                ot.flush();
                r.setAtributo("fichero",ot.toByteArray());
                ot.close();
                st.close();
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException sqle)
        {
            m_Log.error("Error de SQL en loadDocumento: " + sqle.toString());
        }
        catch (Exception bde)
        {
            if (m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo loadDocumento: " + bde.toString());
            bde.printStackTrace();
        }
        finally
        {
            if (conexion != null)
            {
                try
                {
                    abd.devolverConexion(conexion);
                }
                catch(BDException bde)
                {
                    m_Log.error("No se pudo devolver la conexion: " + bde.toString());
                }
            }
        }
        return r;
    }


    /************************************************************************************/

    /**
     * Comprueba si existe una determinada etiqueta para una determinada aplicación
     * @param codAplicacion: Código de la aplicación
     * @param etiqueta: Nombre de la etiqueta
     * @param con: Conexión a la base de datos
     * @return Boolean
     */
     public boolean existeEtiqueta(int codAplicacion,String etiqueta, Connection con)
    {
        boolean exito = false;
        Statement st = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();        
        try{
            sql.append("SELECT COUNT(DISTINCT(CI.NOMEAS)) AS NUM ");
            sql.append("FROM CAMPOINFORME CI, CAMPOSELECCIONINFORME CSI, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC  ");
            sql.append("WHERE A_DOC.DOC_APL =);");
            sql.append(codAplicacion);
            sql.append(" ");
            sql.append("AND  (A_DOC.DOC_CEI = 1 OR A_DOC.DOC_CEI = 2) ");
            sql.append("AND  A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND  A_DOC.DOC_CEI = CSI.COD_ESTRUCTURA ");
            sql.append("AND  ESI.COD_ENTIDADEINFORME = EI.CODIGO AND  CSI.COD_CAMPOINFORME = CI.CODIGO ");
            sql.append("AND (CI.NOMEAS='");
            sql.append(etiqueta);
            sql.append("')");

            m_Log.debug(this.getClass().getName() + ".existeEtiqueta sql: " + sql.toString());
            st = con.createStatement();
            rs = st.executeQuery(sql.toString());
            int num = 0;
            while(rs.next()){
                num = rs.getInt("NUM");
            }// while

            if(num==1) exito = true;
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
            m_Log.error("Error de SQL en loadDocumento: " + sqle.toString());
        }
        finally
        {
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
                m_Log.error("Error de SQL en loadDocumento: " + e.toString());
            }
        }
        return exito;
    }



   /************** PRUEBA ****************************/
    /**
      * Recupera las etiquetas de tipo fecha que se pueden cargar en un documento de tramitación. Se excluyen los campos suplementarios
     * porque se tratan por separado
      * @param gvo
      * @param params
      * @return
      * @throws es.altia.util.conexion.BDException
      */
   public Vector loadEtiquetasFecha(GeneralValueObject gvo,String[] params) throws BDException {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        int posicion = 0;
        Config common = ConfigServiceHelper.getConfig("common");
        String sufijoEtiquetaFechaAlternativa = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String codAplicacion = "";
            codAplicacion = (String) gvo.getAtributo("codAplicacion");
            m_Log.debug("--> Codigo de la aplicacion : " + codAplicacion);
            String interesado = (String) gvo.getAtributo("interesado");
            m_Log.debug("--> Interesado : " + interesado);
            String relacion = (String) gvo.getAtributo("relacion");
            m_Log.debug("--> Relacion : " + relacion);
            sufijoEtiquetaFechaAlternativa = (String)gvo.getAtributo("sufijoEtiquetaFechaAlternativa");
            m_Log.debug("--> sufijoEtiquetaFechaAlternativa : " + sufijoEtiquetaFechaAlternativa);

            // Creamos la select con los parametros adecuados.
            StringBuffer sql = new StringBuffer();

            if(codAplicacion.equals("4")) {
              if(relacion.equals("N")) {
                 if(interesado.equals("N")) {

                     sql.append(" select " +
                            abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_'","CI.NOMEAS"}) +
                            " AS CODIGO,CI.NOME AS NOME ")
                            // prueba
                            .append("," + abd.convertir("CI.TIPO",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM CAMPOINFORME CI, CAMPOSELECCIONINFORME CSI, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" A_DOC.DOC_CEI = CSI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                            .append(" CSI.COD_CAMPOINFORME = CI.CODIGO")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_TITULOCARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_NOMBRECARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_DES AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
		                 	.append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                  			// Nuevo
                  			.append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_TRATAMIENTOCARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_TRA AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 1 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")                            
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Nombre'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'NombreInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            // Nuevo
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'NombreCompleto'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Apel1'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Apellido1Interesado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Apel2'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Apellido2Interesado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            //
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Dom'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'DomicilioInteresado'","ROL_DES"})+"AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            //
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Mail'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Email'","ROL_DES"})+"AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            // 02/11/2007 - Se va a añadir el codigo postal como nuevo campo disponible a la lista de
                            // campos de las plantillas.
                            .append(" UNION SELECT ").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_CodPostal'","ROL_DES"})).append(" AS CODIGO,").append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'CodigoPostal'","ROL_DES"})).append("AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Rol'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'RolInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Doc'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'DocumInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Pob'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'PoblacionInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")

                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Tlfno'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'TelefonoInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'EXPEDIENTE_'","EI.NOME","'_Provincia'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'ProvinciaInteresado'","ROL_DES"})+" AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                            .append(" WHERE ROL_MUN = ").append(gvo.getAtributo("codMunicipio")).append(" AND ")
                            .append(" ROL_PRO = '").append(gvo.getAtributo("codProcedimiento")).append("' AND ")
                            .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 2 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO ")
                            .append(" ORDER BY 2");

                } else if(interesado.equals("S")) {

                    /*** Plantilla por interesado **/
                    sql.append(" select "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_'","CI.NOMEAS"})+" AS CODIGO,CI.NOME AS NOME ")
                            // prueba
                             .append("," + abd.convertir("CI.TIPO",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM CAMPOINFORME CI, CAMPOSELECCIONINFORME CSI, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" A_DOC.DOC_CEI = CSI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                            .append(" CSI.COD_CAMPOINFORME = CI.CODIGO")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_TITULOCARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                            .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_NOMBRECARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_DES AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                  			// Nuevo
                  			.append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_TRATAMIENTO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                            // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 5 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")                            
                            .append(" ORDER BY 2");
                }
              } else {

                    /**** plantilla para relación entre expedientes ******/
                   sql.append(" select " +
                          abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_'","CI.NOMEAS"}) +
                          " AS CODIGO,CI.NOME AS NOME ")
                          // prueba
                          .append("," + abd.convertir("CI.TIPO",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                          // prueba
                          .append(" FROM CAMPOINFORME CI, CAMPOSELECCIONINFORME CSI, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                          .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                          .append(" A_DOC.DOC_CEI = 7 AND ")
                          .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                          .append(" A_DOC.DOC_CEI = CSI.COD_ESTRUCTURA AND ")
                          .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                          .append(" CSI.COD_CAMPOINFORME = CI.CODIGO ")
                          .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_TITULOCARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
                           // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                           // prueba
                          .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                          .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                          .append(" A_DOC.DOC_CEI = 7 AND ")
                          .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                          .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                          .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_NOMBRECARGO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_DES AS NOME ")
                          // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                           // prueba
                          .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                          .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                          .append(" A_DOC.DOC_CEI = 7 AND ")
                          .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                          .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                  			.append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_TRATAMIENTO'",abd.convertir("E_CAR.CAR_COD",abd.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
                            // prueba
                            .append("," + abd.convertir("'-1'",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                           // prueba
                            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                            .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                            .append(" A_DOC.DOC_CEI = 7 AND ")
                            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
                          .append(" ORDER BY 2");
              }
            } else {
                sql.append(" select "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"EI.NOME","'_'","CI.NOMEAS"})+" AS CODIGO,CI.NOME AS NOME ")
                         // prueba
                         .append("," + abd.convertir("CI.TIPO",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) + " AS TIPO")
                          // prueba
                        .append(" FROM CAMPOINFORME CI, CAMPOSELECCIONINFORME CSI, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
                        .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                        .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                        .append(" A_DOC.DOC_CEI = CSI.COD_ESTRUCTURA AND ")
                        .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO AND ")
                        .append(" CSI.COD_CAMPOINFORME = CI.CODIGO ")
                        .append(" ORDER BY 2");
            }

            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql.toString());

            while(rs.next())
            {
                GeneralValueObject gVO = new GeneralValueObject();
                String codigo = rs.getString("CODIGO");
                String cod = "";
                StringTokenizer valores = null;
                if (codigo != null)
                {
                    valores = new StringTokenizer(codigo," ()/ºª@%&",false);
                    while (valores.hasMoreTokens())
                    {
                        String valor = valores.nextToken();
                        cod += valor;
                    }

                    // Eliminar digitos del principio.
                    posicion = 	cod.indexOf("_");
                    boolean digPrincipio=true;
                    while (digPrincipio && cod.length()>0)
                    {
                        if (cod.charAt(posicion+1)>='0' && cod.charAt(posicion+1)<='9' )
                            cod = cod.substring(0,posicion+1)+cod.substring(posicion+2);
                        else digPrincipio = false;
                    }
                    if (m_Log.isDebugEnabled()) m_Log.debug("--> " + cod);
                }
                gVO.setAtributo("codigo", cod);
                if (m_Log.isDebugEnabled()) m_Log.debug("CODIGO: " + gVO.getAtributo("codigo"));
                gVO.setAtributo("nombre", rs.getString("NOME"));
                if (m_Log.isDebugEnabled()) m_Log.debug("NOMBRE: " + gVO.getAtributo("nombre"));

                /** prueba ***/
                gVO.setAtributo("tipoCampo",rs.getString("TIPO"));
                if (m_Log.isDebugEnabled()) m_Log.debug("TIPO: " + gVO.getAtributo("tipoCampo"));

                String tipoCampo = rs.getString("TIPO");
                if(tipoCampo!=null && tipoCampo.equalsIgnoreCase(ConstantesDatos.TIPO_DATO_FECHAS_ETIQUETAS_DOCS_TRAMITACION)){
                    cod = cod.substring(cod.indexOf("_")+1, cod.length());
                    r.add(cod);
                }
            }// 
         
            rs.close();
            stmt.close();
        } catch (SQLException sqle) {
            m_Log.error("Error de SQL en loadEtiquetas: " + sqle.toString());
            throw new BDException(sqle.toString());
        } finally {
            if (conexion != null) abd.devolverConexion(conexion);
        }
        return r;
    }



     /**
     * Comprueba si una etiqueta es de tipo fecha
     * @param nombreEtiqueta: Nombre de la etiqueta
     * @param params: Parámetros de conexión a la BBDD
     * @return Un boolean
     */
    public Hashtable<String,String> getPlantillaDocumento(String codPlantilla,String codProcedimiento,String[] params){
        Hashtable<String,String> dato = new Hashtable<String,String>();        
        Statement st = null;
        ResultSet rs = null;
        AdaptadorSQLBD adapt = null;
        Connection con  = null;
        try{

            adapt= new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            String sql ="SELECT PLT_TRA,PLT_DES,PLT_INT,PLT_REL FROM A_PLT WHERE PLT_COD=" + codPlantilla + " AND PLT_PRO='" + codProcedimiento + "'";
            m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                dato.put("COD_TRAMITE",rs.getString("PLT_TRA"));
                dato.put("DESC_TRAMITE",rs.getString("PLT_DES"));
                dato.put("POR_INTERESADO",rs.getString("PLT_INT"));
                dato.put("POR_RELACION",rs.getString("PLT_REL"));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                adapt.devolverConexion(con);
            }catch(SQLException e){
                e.printStackTrace();
            }catch(BDException e){
                e.printStackTrace();
            }
        }
        return dato;
    }
    /*************** PRUEBA ***************************/
    
     /**
     * Recupera el editor de texto
     * @param nombreEtiqueta: Nombre de la etiqueta
     * @param params: Parámetros de conexión a la BBDD
     * @return Un String
     */
    public String getEditorTexto(int codMunicipio, String numExpediente, int codTramite, int ocurrencia, 
            int codDocumento, boolean perteneceRelacion, String[] params){
        PreparedStatement ps = null;
        ResultSet rs = null;
        AdaptadorSQLBD adapt = null;
        String editorTexto = "";
        Connection con = null;
        int ejercicio = Integer.parseInt(numExpediente.split("/")[0]);
        String procedimiento = numExpediente.split("/")[1];
        try{
            adapt= new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            String sql;
            if (perteneceRelacion)
                sql ="SELECT PLT_EDITOR_TEXTO FROM A_PLT,E_DOT,G_CRD "; 
            else
                sql ="SELECT PLT_EDITOR_TEXTO FROM A_PLT,E_DOT,E_CRD "; 
                
            sql += "WHERE CRD_MUN = ? AND CRD_PRO = ? AND CRD_EJE = ? AND CRD_NUM = ? " + 
                        "AND CRD_TRA = ? AND CRD_OCU = ? AND CRD_NUD = ? "+
                        "AND DOT_MUN = CRD_MUN AND DOT_PRO = CRD_PRO " + 
                        "AND DOT_TRA = CRD_TRA AND DOT_COD = CRD_DOT " +
                        "AND PLT_COD = DOT_PLT ";

            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, procedimiento);
            ps.setInt(i++, ejercicio);
            ps.setString(i++, numExpediente);
            ps.setInt(i++, codTramite);
            ps.setInt(i++, ocurrencia);
            ps.setInt(i++, codDocumento);
            rs = ps.executeQuery();
            
            while(rs.next()){
                editorTexto = rs.getString("PLT_EDITOR_TEXTO");
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
                adapt.devolverConexion(con);
            }catch(SQLException e){
                e.printStackTrace();
            }catch(BDException e){
                e.printStackTrace();
            } 
        }
        return editorTexto;
    }
    
     public String getEditorTextoAlta(int codMunicipio, String numExpediente, int codTramite, int codDocumento, String[] params){
        PreparedStatement ps = null;
        ResultSet rs = null;
        AdaptadorSQLBD adapt = null;
        String editorTexto = "";
        Connection con = null;
        int ejercicio = Integer.parseInt(numExpediente.split("/")[0]);
        String procedimiento = numExpediente.split("/")[1];
        try{
            adapt= new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            String sql;
          
           
            sql ="SELECT PLT_EDITOR_TEXTO FROM A_PLT, E_DOT "; 
                
            sql += "WHERE PLT_PRO = ? AND PLT_TRA = ? AND DOT_COD= ?   AND PLT_COD = DOT_PLT ";

            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            int i = 1;
           
            ps.setString(i++, procedimiento);           
            ps.setInt(i++, codTramite);
            ps.setInt(i++, codDocumento);
          
            rs = ps.executeQuery();
            
            while(rs.next()){
                editorTexto = rs.getString("PLT_EDITOR_TEXTO");
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
                adapt.devolverConexion(con);
            }catch(SQLException e){
                e.printStackTrace();
            }catch(BDException e){
                e.printStackTrace();
            }
        }
        return editorTexto;
    }

    /**
     * Recupera el nombre del documento
     * 
     * @param codMunicipio
     * @param numExpediente
     * @param codTramite
     * @param ocurrencia
     * @param codDocumento
     * @param perteneceRelacion
     * @param params
     * @return 
     */
    public String getNombreDocumento(int codMunicipio, String numExpediente, int codTramite, int ocurrencia, 
            int codDocumento, boolean perteneceRelacion, String[] params){
        PreparedStatement ps = null;
        ResultSet rs = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        String nombreDocumento = "";
        
        int ejercicio = Integer.parseInt(numExpediente.split("/")[0]);
        String procedimiento = numExpediente.split("/")[1];
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            StringBuilder sql = new StringBuilder();
            
            sql.append("SELECT CRD_DES ");
            
            if (perteneceRelacion) {
                sql.append("FROM G_CRD ");
            } else {
                sql.append("FROM E_CRD ");
            }

            sql.append("WHERE CRD_MUN = ? ")
               .append("AND CRD_PRO = ? ")
               .append("AND CRD_EJE = ? ")
               .append("AND CRD_NUM = ? ")
               .append("AND CRD_TRA = ? ")
               .append("AND CRD_OCU = ? ")
               .append("AND CRD_NUD = ? ");
            
            m_Log.debug(sql);
            ps = con.prepareStatement(sql.toString());
            int index = 1;
            JdbcOperations.setValues(ps, index,
                    codMunicipio,
                    procedimiento,
                    ejercicio,
                    numExpediente,
                    codTramite,
                    ocurrencia,
                    codDocumento);
            
            rs = ps.executeQuery();

            if (rs.next()) {
                nombreDocumento = rs.getString("CRD_DES");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                adapt.devolverConexion(con);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (BDException e) {
                e.printStackTrace();
            }
        }
        return nombreDocumento;
    }
    
    /**
     * Comprueba si la plantilla del documento se ha creado para relación y devuelve true o false.
     * 
     * @param codMunicipio
     * @param codTramite
     * @param codDocumento
     * @param con
     * @return 
     */
    public boolean esDocumentoParaRelacion(int codMunicipio, String codProcedimiento, int codTramite, int codDocumento, Connection con){
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql;
        boolean paraRelacion = false;
        
        try {
            sql = "SELECT PLT_REL FROM A_PLT JOIN E_DOT ON (PLT_COD = DOT_PLT) "
                    + "WHERE DOT_MUN = ? AND DOT_PRO = ? AND DOT_TRA = ? AND DOT_COD = ?";
            
            m_Log.debug(sql);
            ps = con.prepareStatement(sql.toString());
            int index = 1;
            JdbcOperations.setValues(ps, index,
                    codMunicipio,
                    codProcedimiento,
                    codTramite,
                    codDocumento);
            
            rs = ps.executeQuery();

            if (rs.next()) {
                if(rs.getString("PLT_REL").equals("S")) {
                    paraRelacion = true;
                }
            }
        } catch (Exception e) {
            m_Log.error("Ha ocurrido un error al comprobar si el documento se ha creado para una relación.");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } 
        }
        return paraRelacion;
    }

    public Vector loadDespPlantillaActiva(String []params, String nombreCampo)
    {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try
        {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            // Creamos la select con los parametros adecuados.
            String sql = "SELECT DES_VAL_COD, DES_NOM FROM E_DES_VAL WHERE DES_COD = '" + nombreCampo + "'";

            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int i=0;
            while(rs.next()) {
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codigoActivo", rs.getString("DES_VAL_COD"));
                gVO.setAtributo("nombreActivo", rs.getString("DES_NOM"));
                r.add(gVO);
                i++;
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException sqle)
        {
            m_Log.error("Error de SQL en loadDespPlantillaActiva: " + sqle.toString());
        }
        catch (BDException bde)
        {
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo loadDespPlantillaActiva: " + bde.toString());
        }
        finally
        {
            if (conexion != null)
            {
                try
                {
                    abd.devolverConexion(conexion);
                }
                catch(BDException bde)
                {
                    m_Log.error("No se pudo devolver la conexion: " + bde.toString());
                }
            }
        }
        return r;
    }
}