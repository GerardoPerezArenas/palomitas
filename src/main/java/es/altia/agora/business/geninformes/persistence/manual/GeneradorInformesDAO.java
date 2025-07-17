// NOMBRE DEL PAQUETE
package es.altia.agora.business.geninformes.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.geninformes.utils.bd.EstructuraXerador;
import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.*;
import java.sql.*;
import java.util.Vector;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DefinicionProcedimientosDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class GeneradorInformesDAO {

    private static GeneradorInformesDAO instance = null;
    protected static Config conf; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log = LogFactory.getLog(GeneradorInformesDAO.class.getName()); // Para información de logs


    protected GeneradorInformesDAO() {
        super();
        // Queremos usar el fichero de configuracion techserver
        conf = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
        // Queremos usar el servicio de log
    }

    public static GeneradorInformesDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread)
            // Las invocaciones de este metodo
            synchronized(GeneradorInformesDAO.class) {
                if (instance == null) {
                    instance = new GeneradorInformesDAO();
                }
            }
        }
        return instance;
    }

    public Vector getListaEntidades(String codAplicacion,String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.info("getListaEntidades");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        AdaptadorSQLBD oad = null;
        String sql = "";
        Vector list = new Vector();
        ElementoListaValueObject elemListVO;
        int orden = 0;

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            sql = "SELECT " + conf.getString("SQL.ENTIDADEAPLICACION.codEntidad") + ","+
                    conf.getString("SQL.ENTIDADEINFORME.nombre") +
                    " FROM  ENTIDADEAPLICACION,ENTIDADEINFORME WHERE " +
                    conf.getString("SQL.ENTIDADEAPLICACION.codAplicacion") + "=" + codAplicacion +
                    " AND " + conf.getString("SQL.ENTIDADEAPLICACION.codEntidad") + "=" +
                    conf.getString("SQL.ENTIDADEINFORME.codigo");
            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("GeneradorInformesDAO, getListaEntidades: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                String codEntidad = rs.getString(conf.getString("SQL.ENTIDADEAPLICACION.codEntidad"));
                String nombreEntidad = rs.getString(conf.getString("SQL.ENTIDADEINFORME.nombre"));
                if("4".equals(codAplicacion) && ("4".equals(codEntidad) || "10011".equals(codEntidad) || "10015".equals(codEntidad))) {

                } else {
                    elemListVO = new ElementoListaValueObject(codEntidad,nombreEntidad,orden++);
                    list.addElement(elemListVO);
                }
            }
            m_Log.debug("GeneradorInformesDAO, getListaEntidades: Lista de entidades cargada");
            rs.close();
            st.close();
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.GeneradorInformesDAO.getListaEntidades"), e);
        }finally {
            try{
                oad.devolverConexion(con);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        return list;
    }

    public Vector getListaCampos(String codEntidad,String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.info("getListaCampos");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        AdaptadorSQLBD oad = null;
        String sql = "";
        Vector list = new Vector();

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            sql = "SELECT " + conf.getString("SQL.CAMPOINFORME.codigo") + ","+
                    conf.getString("SQL.CAMPOINFORME.nombre") + "," +
                    conf.getString("SQL.CAMPOINFORME.tipo") + "," +
                    conf.getString("SQL.CAMPOINFORME.longitud") + "," +
                    conf.getString("SQL.CAMPOINFORME.campo") + "," +
                    conf.getString("SQL.CAMPOINFORME.nombreAs") +
                    " FROM CAMPOINFORME,CAMPOENTIDADEINFORME WHERE " +
                    conf.getString("SQL.CAMPOENTIDADEINFORME.codCampo") + "=" +
                    conf.getString("SQL.CAMPOINFORME.codigo") + " AND " +
                    conf.getString("SQL.CAMPOENTIDADEINFORME.codEntidad") + "=" + codEntidad;

            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug("GeneradorInformesDAO, getListaEntidades: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                GeneralValueObject gVO = new GeneralValueObject();
                String codCampo = rs.getString(conf.getString("SQL.CAMPOINFORME.codigo"));
                gVO.setAtributo("codCampo",codCampo);
                String nombreCampo = rs.getString(conf.getString("SQL.CAMPOINFORME.nombre"));
                gVO.setAtributo("nombreCampo",nombreCampo);
                String tipo = rs.getString(conf.getString("SQL.CAMPOINFORME.tipo"));
                gVO.setAtributo("tipo",tipo);
                String longitud = rs.getString(conf.getString("SQL.CAMPOINFORME.longitud"));
                gVO.setAtributo("longitud",longitud);
                String campo = rs.getString(conf.getString("SQL.CAMPOINFORME.campo"));
                gVO.setAtributo("campo",campo);
                String nombreAs = rs.getString(conf.getString("SQL.CAMPOINFORME.nombreAs"));
                gVO.setAtributo("nombreAs",nombreAs);
                list.addElement(gVO);
            }
            m_Log.debug("GeneradorInformesDAO, getListaCampos: Lista de campos cargada");
            rs.close();
            st.close();
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.GeneradorInformesDAO.getListaCampos"), e);
        }finally {
            try{
                oad.devolverConexion(con);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        return list;
    }

    public String getNombreVista(String codEntidad,String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getNombreVista");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        AdaptadorSQLBD oad = null;
        String sql = "";
        String nombreVista = "";

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();
            sql = "SELECT " + conf.getString("SQL.ENTIDADEINFORME.nombreVista") +
                    " FROM ENTIDADEINFORME WHERE " +
                    conf.getString("SQL.ENTIDADEINFORME.codigo") + "=" + codEntidad;
            if(m_Log.isDebugEnabled()) m_Log.debug("GeneradorInformesDAO, getNombreVista: Sentencia SQL:" + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                nombreVista = rs.getString(conf.getString("SQL.ENTIDADEINFORME.nombreVista"));
            }
            rs.close();
            st.close();
        }catch (Exception e) {
            nombreVista = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.GeneradorInformesDAO.getNombreVista"), e);
        }finally {
            try{
                oad.devolverConexion(con);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        return nombreVista;
    }

    public int altaInforme(GeneralValueObject gVO,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException,SQLException{
        String sql= "";
        int res = 0;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;


        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            int resultado1 = insertarEstructuraInforme(gVO,conexion,params);
            if(resultado1 >0) {
                res = resultado1;
                int codEst = resultado1;
                String codEstructura = Integer.toString(codEst);
                gVO.setAtributo("codEstructura",codEstructura);
                int resultado2 = insertarCampoSeleccionInforme(gVO,conexion,params);
                res = resultado2;
                int resultado3 = insertarCampoCondicionInforme(gVO,conexion,params);
                res = resultado3;
                int resultado4 = insertarCampoOrdenInforme(gVO,conexion,params);
                res = resultado4;
                int resultado5 = insertarInformeGenerador(gVO,conexion,params);
                String codInforme = Integer.toString(resultado5);
                gVO.setAtributo("codInforme",codInforme);
                res = resultado5;
                int resultado6 = insertarEtiqPlt(gVO,conexion,params);
                res = resultado6;
            }

            abd.finTransaccion(conexion);

        } catch (SQLException ex) {
            conexion.rollback();
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
        } catch (BDException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + e.getMessage());
        } finally {
            try{
                abd.devolverConexion(conexion);
             } catch(Exception ex) {
                ex.printStackTrace();
                 if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
             }
        }

        return res;
    }

    public int insertarEstructuraInforme(GeneralValueObject gVO,Connection conexion,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException,SQLException{
        String sql= "";
        int res = 0;
        ResultSet rs = null;
        PreparedStatement pst = null;

        String codEntidad = (String) gVO.getAtributo("codEntidad");
        String consultaSQL = (String) gVO.getAtributo("consultaSQL");
        long codEst = 0;

        try{

/*            sql = "SELECT CONTESTRUCTURAINFORME.nextval from dual";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            pst = conexion.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                codEst = rs.getInt(1);
            }
            codEst++;*/
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            codEst = oad.devolverNextValSecuencia(params, "CONTESTRUCTURAINFORME");
            String codEstructura = Long.toString(codEst);

            sql = "INSERT INTO ESTRUCTURAINFORME (" + conf.getString("SQL.ESTRUCTURAINFORME.codEstructura") +
                    "," + conf.getString("SQL.ESTRUCTURAINFORME.codEntidad") + "," +
                    conf.getString("SQL.ESTRUCTURAINFORME.codPadre") + "," +
                    conf.getString("SQL.ESTRUCTURAINFORME.consultaSQL") + "," +
                    conf.getString("SQL.ESTRUCTURAINFORME.posicion") + ") VALUES (" + codEstructura +
                    "," + codEntidad + ",null,?,0)";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            pst = conexion.prepareStatement(sql);
            char[] consulta =	(consultaSQL != null)? (consultaSQL).toCharArray(): null;
            if (consulta == null) {
                pst.setNull(1, java.sql.Types.LONGVARCHAR);
            } else {
                java.io.CharArrayReader cr = new java.io.CharArrayReader(consulta);
                pst.setCharacterStream(1, cr, consulta.length);
            }
            res = pst.executeUpdate();

            pst.close();

        } catch (SQLException ex) {
            conexion.rollback();
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }
        if(res >0) {
            res = new Long(codEst).intValue();
        }
        return res;
    }

    public int insertarCampoSeleccionInforme(GeneralValueObject gVO,Connection conexion,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException,SQLException{
        String sql= "";
        int res = 0;
        ResultSet rs = null;
        Statement st = null;

        String codEstructura = (String) gVO.getAtributo("codEstructura");
        Vector listaCodCamposElegidos = new Vector();
        listaCodCamposElegidos = (Vector) gVO.getAtributo("listaCodCamposElegidos");

        try{

            for(int i=0;i<listaCodCamposElegidos.size();i++) {
                sql = "INSERT INTO CAMPOSELECCIONINFORME (" + conf.getString("SQL.CAMPOSELECCIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOSELECCIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOSELECCIONINFORME.codCampoInforme") + "," +
                        conf.getString("SQL.CAMPOSELECCIONINFORME.ancho") + ") VALUES (" +
                        codEstructura + "," + i + "," + listaCodCamposElegidos.elementAt(i) + ",null)";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(listaCodCamposElegidos.size() == 0) {
                res =1;
            }



        } catch (SQLException ex) {
            conexion.rollback();
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }

        return res;
    }

    public int insertarCampoCondicionInforme(GeneralValueObject gVO,Connection conexion,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException,SQLException{
        String sql= "";
        int res = 0;
        ResultSet rs = null;
        Statement st = null;

        String codEstructura = (String) gVO.getAtributo("codEstructura");
        String codCampo1 = (String) gVO.getAtributo("codCampo1");
        String codCampo2 = (String) gVO.getAtributo("codCampo2");
        String codCampo3 = (String) gVO.getAtributo("codCampo3");
        String codCampo4 = (String) gVO.getAtributo("codCampo4");
        String codCampo5 = (String) gVO.getAtributo("codCampo5");
        String codCampo6 = (String) gVO.getAtributo("codCampo6");
        String codCampo7 = (String) gVO.getAtributo("codCampo7");
        String codCondicion1 = (String) gVO.getAtributo("codCondicion1");
        String codCondicion2 = (String) gVO.getAtributo("codCondicion2");
        String codCondicion3 = (String) gVO.getAtributo("codCondicion3");
        String codCondicion4 = (String) gVO.getAtributo("codCondicion4");
        String codCondicion5 = (String) gVO.getAtributo("codCondicion5");
        String codCondicion6 = (String) gVO.getAtributo("codCondicion6");
        String codCondicion7 = (String) gVO.getAtributo("codCondicion7");
        String valor1 = (String) gVO.getAtributo("valor1");
        String valor2 = (String) gVO.getAtributo("valor2");
        String valor3 = (String) gVO.getAtributo("valor3");
        String valor4 = (String) gVO.getAtributo("valor4");
        String valor5 = (String) gVO.getAtributo("valor5");
        String valor6 = (String) gVO.getAtributo("valor6");
        String valor7 = (String) gVO.getAtributo("valor7");
        String codOperador1 = (String) gVO.getAtributo("codOperador1");
        String codOperador2 = (String) gVO.getAtributo("codOperador2");
        String codOperador3 = (String) gVO.getAtributo("codOperador3");
        String codOperador4 = (String) gVO.getAtributo("codOperador4");
        String codOperador5 = (String) gVO.getAtributo("codOperador5");
        String codOperador6 = (String) gVO.getAtributo("codOperador6");

        try{

            if(!"".equals(codCampo1)) {
                sql = "INSERT INTO CAMPOCONDICIONINFORME (" + conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOCONDICIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.clausula") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.operador") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.valor") + ") VALUES (" + codEstructura +
                        ",0,null," + codCampo1 + ",'" + codCondicion1 + "',";
                if(valor1 == null) {
                    sql += valor1 + ")";
                } else {
                    sql += "'" + valor1 + "')";
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codOperador1)) {
                sql = "INSERT INTO CAMPOCONDICIONINFORME (" + conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOCONDICIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.clausula") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.operador") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.valor") + ") VALUES (" + codEstructura +
                        ",1,'" + codOperador1 + "'," + codCampo2 + ",'" + codCondicion2 + "',";
                if(valor2 == null) {
                    sql += valor2 + ")";
                } else {
                    sql += "'" + valor2 + "')";
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codOperador2)) {
                sql = "INSERT INTO CAMPOCONDICIONINFORME (" + conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOCONDICIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.clausula") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.operador") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.valor") + ") VALUES (" + codEstructura +
                        ",2,'" + codOperador2 + "'," + codCampo3 + ",'" + codCondicion3 + "',";
                if(valor3 == null) {
                    sql += valor3 + ")";
                } else {
                    sql += "'" + valor3 + "')";
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codOperador3)) {
                sql = "INSERT INTO CAMPOCONDICIONINFORME (" + conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOCONDICIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.clausula") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.operador") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.valor") + ") VALUES (" + codEstructura +
                        ",3,'" + codOperador3 + "'," + codCampo4 + ",'" + codCondicion4 + "',";
                if(valor4 == null) {
                    sql += valor4 + ")";
                } else {
                    sql += "'" + valor4 + "')";
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codOperador4)) {
                sql = "INSERT INTO CAMPOCONDICIONINFORME (" + conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOCONDICIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.clausula") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.operador") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.valor") + ") VALUES (" + codEstructura +
                        ",4,'" + codOperador4 + "'," + codCampo5+ ",'" + codCondicion5 + "',";
                if(valor5 == null) {
                    sql += valor5 + ")";
                } else {
                    sql += "'" + valor5 + "')";
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codOperador5)) {
                sql = "INSERT INTO CAMPOCONDICIONINFORME (" + conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOCONDICIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.clausula") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.operador") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.valor") + ") VALUES (" + codEstructura +
                        ",5,'" + codOperador5 + "'," + codCampo6 + ",'" + codCondicion6 + "',";
                if(valor6 == null) {
                    sql += valor6 + ")";
                } else {
                    sql += "'" + valor6 + "')";
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codOperador6)) {
                sql = "INSERT INTO CAMPOCONDICIONINFORME (" + conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOCONDICIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.clausula") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.operador") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.valor") + ") VALUES (" + codEstructura +
                        ",6,'" + codOperador6 + "'," + codCampo7 + ",'" + codCondicion7 + "',";
                if(valor7 == null) {
                    sql += valor7 + ")";
                } else {
                    sql += "'" + valor7 + "')";
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
        } catch (SQLException ex) {
            conexion.rollback();
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }

        return res;
    }

    public int insertarCampoOrdenInforme(GeneralValueObject gVO,Connection conexion,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException,SQLException{
        String sql= "";
        int res = 0;
        ResultSet rs = null;
        Statement st = null;

        String codEstructura = (String) gVO.getAtributo("codEstructura");
        String codCampoOrdenacion1 = (String) gVO.getAtributo("codCampoOrdenacion1");
        String codCampoOrdenacion2 = (String) gVO.getAtributo("codCampoOrdenacion2");
        String codCampoOrdenacion3 = (String) gVO.getAtributo("codCampoOrdenacion3");
        String codCampoOrdenacion4 = (String) gVO.getAtributo("codCampoOrdenacion4");
        String codCampoOrdenacion5 = (String) gVO.getAtributo("codCampoOrdenacion5");
        String codSentidoOrdenacion1 = (String) gVO.getAtributo("codSentidoOrdenacion1");
        String codSentidoOrdenacion2 = (String) gVO.getAtributo("codSentidoOrdenacion2");
        String codSentidoOrdenacion3 = (String) gVO.getAtributo("codSentidoOrdenacion3");
        String codSentidoOrdenacion4 = (String) gVO.getAtributo("codSentidoOrdenacion4");
        String codSentidoOrdenacion5 = (String) gVO.getAtributo("codSentidoOrdenacion5");
        String codSentido1 = "";
        String codSentido2 = "";
        String codSentido3 = "";
        String codSentido4 = "";
        String codSentido5 = "";
        if(codSentidoOrdenacion1 != null && !"".equals(codSentidoOrdenacion1)) {
            if("ASC".equals(codSentidoOrdenacion1)) {
                codSentido1 = "A";
            } else if("DESC".equals(codSentidoOrdenacion1)) {
                codSentido1 = "D";
            }
        }
        if(codSentidoOrdenacion2 != null && !"".equals(codSentidoOrdenacion2)) {
            if("ASC".equals(codSentidoOrdenacion2)) {
                codSentido2 = "A";
            } else if("DESC".equals(codSentidoOrdenacion2)) {
                codSentido2 = "D";
            }
        }
        if(codSentidoOrdenacion3 != null && !"".equals(codSentidoOrdenacion3)) {
            if("ASC".equals(codSentidoOrdenacion3)) {
                codSentido3 = "A";
            } else if("DESC".equals(codSentidoOrdenacion3)) {
                codSentido3 = "D";
            }
        }
        if(codSentidoOrdenacion4 != null && !"".equals(codSentidoOrdenacion4)) {
            if("ASC".equals(codSentidoOrdenacion4)) {
                codSentido4 = "A";
            } else if("DESC".equals(codSentidoOrdenacion4)) {
                codSentido4 = "D";
            }
        }
        if(codSentidoOrdenacion5 != null && !"".equals(codSentidoOrdenacion5)) {
            if("ASC".equals(codSentidoOrdenacion5)) {
                codSentido5 = "A";
            } else if("DESC".equals(codSentidoOrdenacion5)) {
                codSentido5 = "D";
            }
        }

        try{
            if(!"".equals(codCampoOrdenacion1)) {
                sql = "INSERT INTO CAMPOORDEINFORME (" + conf.getString("SQL.CAMPOORDEINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOORDEINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.tipoOrden") + ") VALUES (" + codEstructura +
                        ",0," + codCampoOrdenacion1 + ",'" + codSentido1 + "')";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codCampoOrdenacion2)) {
                sql = "INSERT INTO CAMPOORDEINFORME (" + conf.getString("SQL.CAMPOORDEINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOORDEINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.tipoOrden") + ") VALUES (" + codEstructura +
                        ",1," + codCampoOrdenacion2 + ",'" + codSentido2 + "')";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codCampoOrdenacion3)) {
                sql = "INSERT INTO CAMPOORDEINFORME (" + conf.getString("SQL.CAMPOORDEINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOORDEINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.tipoOrden") + ") VALUES (" + codEstructura +
                        ",2," + codCampoOrdenacion3 + ",'" + codSentido3 + "')";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codCampoOrdenacion4)) {
                sql = "INSERT INTO CAMPOORDEINFORME (" + conf.getString("SQL.CAMPOORDEINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOORDEINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.tipoOrden") + ") VALUES (" + codEstructura +
                        ",3," + codCampoOrdenacion4 + ",'" + codSentido4 + "')";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codCampoOrdenacion5)) {
                sql = "INSERT INTO CAMPOORDEINFORME (" + conf.getString("SQL.CAMPOORDEINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOORDEINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.tipoOrden") + ") VALUES (" + codEstructura +
                        ",4," + codCampoOrdenacion5 + ",'" + codSentido5 + "')";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }

            if(st != null )
                st.close();


        } catch (SQLException ex) {
            conexion.rollback();
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }

        return res;
    }

    public int insertarInformeGenerador(GeneralValueObject gVO,Connection conexion,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException,SQLException{
        String sql= "";
        int res = 0;
        ResultSet rs = null;
        PreparedStatement pst = null;

        String codAplicacion = (String) gVO.getAtributo("codAplicacion");
        String nombreInforme = (String) gVO.getAtributo("nombreInforme");
        String descInforme = (String) gVO.getAtributo("descInforme");
        String codEstructura = (String) gVO.getAtributo("codEstructura");
        long codInf = 0;

        try{
/*            sql = "SELECT CONTINFORMEXERADOR.nextval from dual";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            pst = conexion.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                codInf = rs.getInt(1);
            }
            codInf++;*/
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            codInf = oad.devolverNextValSecuencia(params, "CONTINFORMEXERADOR");
            String codInforme = Long.toString(codInf);

            sql = "INSERT INTO INFORMEXERADOR (" + conf.getString("SQL.INFORMEXERADOR.codigo") +
                    "," + conf.getString("SQL.INFORMEXERADOR.codAplicacion") + "," +
                    conf.getString("SQL.INFORMEXERADOR.codPais") + "," +
                    conf.getString("SQL.INFORMEXERADOR.codProvincia") + "," +
                    conf.getString("SQL.INFORMEXERADOR.codMunicipio") + "," +
                    conf.getString("SQL.INFORMEXERADOR.nombre") + "," +
                    conf.getString("SQL.INFORMEXERADOR.descripcion") + "," +
                    conf.getString("SQL.INFORMEXERADOR.formato") + "," +
                    conf.getString("SQL.INFORMEXERADOR.codEstructura") + "," +
                    conf.getString("SQL.INFORMEXERADOR.contenido") + "," +
                    conf.getString("SQL.INFORMEXERADOR.fichero") + "," +
                    conf.getString("SQL.INFORMEXERADOR.editada") + ") VALUES (" + codInforme +
                    "," + codAplicacion + ",null,null,null,'" + nombreInforme + "','" +
                    descInforme + "','L'," + codEstructura + ",null,null,'N')";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            pst = conexion.prepareStatement(sql);
            //pst.setNull(1, java.sql.Types.BLOB);
            res = pst.executeUpdate();

            pst.close();

        } catch (SQLException ex) {
            conexion.rollback();
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }
        if(res >0) {
            res = new Long(codInf).intValue();
        }
        return res;
    }

    public int insertarEtiqPlt(GeneralValueObject gVO,Connection conexion,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException,SQLException{
        String sql= "";
        int res = 0;
        ResultSet rs = null;
        Statement st = null;

        String codInforme = (String) gVO.getAtributo("codInforme");
        String descEntidad = (String) gVO.getAtributo("descEntidad");
        Vector listaCodCamposElegidos = new Vector();
        listaCodCamposElegidos = (Vector) gVO.getAtributo("listaCodCamposElegidos");

        try{
            for(int i=0;i<listaCodCamposElegidos.size();i++) {
                sql = "INSERT INTO ETIQ_PLT (" + conf.getString("SQL.ETIQ_PLT.codCampo") +
                        "," + conf.getString("SQL.ETIQ_PLT.codInforme") + "," +
                        conf.getString("SQL.ETIQ_PLT.prefijo") + ") VALUES (" +
                        listaCodCamposElegidos.elementAt(i) + "," + codInforme + ",'" +
                        descEntidad + "')";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(listaCodCamposElegidos.size() == 0) {
                res =1;
            }


        } catch (SQLException ex) {
            conexion.rollback();
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }
        return res;
    }

    public GeneralValueObject obtenerInforme(GeneralValueObject gVO,String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("obtenerInforme");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        AdaptadorSQLBD oad = null;
        String sql = "";
        String codAplicacion = (String) gVO.getAtributo("codAplicacion");
        String codInforme = (String) gVO.getAtributo("codInforme");

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            // PESTAÑA DE DATOS INFORME
            sql = "SELECT " + conf.getString("SQL.INFORMEXERADOR.nombre") + ","+
                    conf.getString("SQL.INFORMEXERADOR.descripcion") + "," +
                    conf.getString("SQL.INFORMEXERADOR.codEstructura") + "," +
                    conf.getString("SQL.INFORMEXERADOR.editada") +
                    " FROM INFORMEXERADOR WHERE " +
                    conf.getString("SQL.INFORMEXERADOR.codigo") + "=" + codInforme;

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            String codEstructura = "";
            while(rs.next()){
                String nombreInforme = rs.getString(conf.getString("SQL.INFORMEXERADOR.nombre"));
                gVO.setAtributo("nombreInforme",nombreInforme);
                String descInforme = rs.getString(conf.getString("SQL.INFORMEXERADOR.descripcion"));
                gVO.setAtributo("descInforme",descInforme);
                codEstructura = rs.getString(conf.getString("SQL.INFORMEXERADOR.codEstructura"));
                gVO.setAtributo("codEstructura",codEstructura);
                String editada = rs.getString(conf.getString("SQL.INFORMEXERADOR.editada"));
                gVO.setAtributo("editada",editada);
            }
            rs.close();

            sql = "SELECT " + conf.getString("SQL.ESTRUCTURAINFORME.codEntidad") +
                    " FROM ESTRUCTURAINFORME WHERE " +
                    conf.getString("SQL.ESTRUCTURAINFORME.codEstructura") + "=" + codEstructura;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            String codEntidad = "";
            while(rs.next()){
                codEntidad = rs.getString(conf.getString("SQL.ESTRUCTURAINFORME.codEntidad"));
                gVO.setAtributo("codEntidad",codEntidad);
            }
            rs.close();

            sql = "SELECT " + conf.getString("SQL.ENTIDADEINFORME.nombre") + "," +
                    conf.getString("SQL.ENTIDADEINFORME.nombreVista") +
                    " FROM ENTIDADEINFORME WHERE " +
                    conf.getString("SQL.ENTIDADEINFORME.codigo") + "=" + codEntidad;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                String descEntidad = rs.getString(conf.getString("SQL.ENTIDADEINFORME.nombre"));
                gVO.setAtributo("descEntidad",descEntidad);
                String nombreVista = rs.getString(conf.getString("SQL.ENTIDADEINFORME.nombreVista"));
                gVO.setAtributo("nombreVista",nombreVista);
            }
            rs.close();

            // PESTAÑA CAMPOS
            sql = "SELECT " + conf.getString("SQL.CAMPOINFORME.codigo") + ","+
                    conf.getString("SQL.CAMPOINFORME.nombre") + "," +
                    conf.getString("SQL.CAMPOINFORME.tipo") + "," +
                    conf.getString("SQL.CAMPOINFORME.longitud") + "," +
                    conf.getString("SQL.CAMPOINFORME.campo") + "," +
                    conf.getString("SQL.CAMPOINFORME.nombreAs") +
                    " FROM CAMPOINFORME,CAMPOENTIDADEINFORME WHERE " +
                    conf.getString("SQL.CAMPOENTIDADEINFORME.codCampo") + "=" +
                    conf.getString("SQL.CAMPOINFORME.codigo") + " AND " +
                    conf.getString("SQL.CAMPOENTIDADEINFORME.codEntidad") + "=" + codEntidad;
            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            Vector listaCamposDisponibles = new Vector();
            Vector listaCamposDisponiblesEntera = new Vector();
            while(rs.next()){
                GeneralValueObject g = new GeneralValueObject();
                String codCampo = rs.getString(conf.getString("SQL.CAMPOINFORME.codigo"));
                g.setAtributo("codCampo",codCampo);
                String nombreCampo = rs.getString(conf.getString("SQL.CAMPOINFORME.nombre"));
                g.setAtributo("nombreCampo",nombreCampo);
                String tipo = rs.getString(conf.getString("SQL.CAMPOINFORME.tipo"));
                g.setAtributo("tipo",tipo);
                String longitud = rs.getString(conf.getString("SQL.CAMPOINFORME.longitud"));
                g.setAtributo("longitud",longitud);
                String campo = rs.getString(conf.getString("SQL.CAMPOINFORME.campo"));
                g.setAtributo("campo",campo);
                String nombreAs = rs.getString(conf.getString("SQL.CAMPOINFORME.nombreAs"));
                g.setAtributo("nombreAs",nombreAs);
                listaCamposDisponibles.addElement(g);
                listaCamposDisponiblesEntera.addElement(g);
            }
            gVO.setAtributo("listaCamposDisponiblesEntera",listaCamposDisponiblesEntera);
            rs.close();

            sql = "SELECT " + conf.getString("SQL.CAMPOINFORME.codigo") + ","+
                    conf.getString("SQL.CAMPOINFORME.nombre") + "," +
                    conf.getString("SQL.CAMPOINFORME.tipo") + "," +
                    conf.getString("SQL.CAMPOINFORME.longitud") + "," +
                    conf.getString("SQL.CAMPOINFORME.campo") + "," +
                    conf.getString("SQL.CAMPOINFORME.nombreAs") +
                    " FROM CAMPOINFORME,ETIQ_PLT WHERE " +
                    conf.getString("SQL.ETIQ_PLT.codCampo") + "=" +
                    conf.getString("SQL.CAMPOINFORME.codigo") + " AND " +
                    conf.getString("SQL.ETIQ_PLT.codInforme") + "=" + codInforme;
            String parametros1[] = {"2","2"};
            sql += oad.orderUnion(parametros1);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            Vector listaCamposElegidos = new Vector();
            while(rs.next()){
                GeneralValueObject g = new GeneralValueObject();
                String codCampo = rs.getString(conf.getString("SQL.CAMPOINFORME.codigo"));
                g.setAtributo("codCampo",codCampo);
                String nombreCampo = rs.getString(conf.getString("SQL.CAMPOINFORME.nombre"));
                g.setAtributo("nombreCampo",nombreCampo);
                String tipo = rs.getString(conf.getString("SQL.CAMPOINFORME.tipo"));
                g.setAtributo("tipo",tipo);
                String longitud = rs.getString(conf.getString("SQL.CAMPOINFORME.longitud"));
                g.setAtributo("longitud",longitud);
                String campo = rs.getString(conf.getString("SQL.CAMPOINFORME.campo"));
                g.setAtributo("campo",campo);
                String nombreAs = rs.getString(conf.getString("SQL.CAMPOINFORME.nombreAs"));
                g.setAtributo("nombreAs",nombreAs);
                listaCamposElegidos.addElement(g);
            }
            rs.close();

            for(int i=0;i<listaCamposElegidos.size();i++) {
                GeneralValueObject g = (GeneralValueObject) listaCamposElegidos.elementAt(i);
                String cCampoElegido = (String) g.getAtributo("codCampo");
                for(int j=0;j<listaCamposDisponibles.size();j++) {
                    GeneralValueObject g2 = (GeneralValueObject) listaCamposDisponibles.elementAt(j);
                    String cCampoDisponible = (String) g2.getAtributo("codCampo");
                    if(cCampoElegido.equals(cCampoDisponible)) {
                        listaCamposDisponibles.removeElementAt(j);
                        break;
                    }
                }
            }
            gVO.setAtributo("listaCamposDisponibles",listaCamposDisponibles);
            gVO.setAtributo("listaCamposElegidos",listaCamposElegidos);

            // PESTAÑA DE ORDENACION
            sql = "SELECT " + conf.getString("SQL.CAMPOORDEINFORME.posicion") + ","+
                    conf.getString("SQL.CAMPOORDEINFORME.codCampo") + "," +
                    conf.getString("SQL.CAMPOORDEINFORME.tipoOrden") +
                    " FROM CAMPOORDEINFORME WHERE " +
                    conf.getString("SQL.CAMPOORDEINFORME.codEstructura") + "=" + codEstructura;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                int posicion = rs.getInt(conf.getString("SQL.CAMPOORDEINFORME.posicion"));
                if(posicion == 0) {
                    String codCampoOrdenacion1 = rs.getString(conf.getString("SQL.CAMPOORDEINFORME.codCampo"));
                    gVO.setAtributo("codCampoOrdenacion1",codCampoOrdenacion1);
                    String codSentidoOrdenacion1 = rs.getString(conf.getString("SQL.CAMPOORDEINFORME.tipoOrden"));
                    if("A".equals(codSentidoOrdenacion1)) {
                        gVO.setAtributo("codSentidoOrdenacion1","ASC");
                    } else {
                        gVO.setAtributo("codSentidoOrdenacion1","DESC");
                    }
                }
                if(posicion == 1) {
                    String codCampoOrdenacion2 = rs.getString(conf.getString("SQL.CAMPOORDEINFORME.codCampo"));
                    gVO.setAtributo("codCampoOrdenacion2",codCampoOrdenacion2);
                    String codSentidoOrdenacion2 = rs.getString(conf.getString("SQL.CAMPOORDEINFORME.tipoOrden"));
                    if("A".equals(codSentidoOrdenacion2)) {
                        gVO.setAtributo("codSentidoOrdenacion2","ASC");
                    } else {
                        gVO.setAtributo("codSentidoOrdenacion2","DESC");
                    }
                }
                if(posicion == 2) {
                    String codCampoOrdenacion3 = rs.getString(conf.getString("SQL.CAMPOORDEINFORME.codCampo"));
                    gVO.setAtributo("codCampoOrdenacion3",codCampoOrdenacion3);
                    String codSentidoOrdenacion3 = rs.getString(conf.getString("SQL.CAMPOORDEINFORME.tipoOrden"));
                    if("A".equals(codSentidoOrdenacion3)) {
                        gVO.setAtributo("codSentidoOrdenacion3","ASC");
                    } else {
                        gVO.setAtributo("codSentidoOrdenacion3","DESC");
                    }
                }
                if(posicion == 3) {
                    String codCampoOrdenacion4 = rs.getString(conf.getString("SQL.CAMPOORDEINFORME.codCampo"));
                    gVO.setAtributo("codCampoOrdenacion4",codCampoOrdenacion4);
                    String codSentidoOrdenacion4 = rs.getString(conf.getString("SQL.CAMPOORDEINFORME.tipoOrden"));
                    if("A".equals(codSentidoOrdenacion4)) {
                        gVO.setAtributo("codSentidoOrdenacion4","ASC");
                    } else {
                        gVO.setAtributo("codSentidoOrdenacion4","DESC");
                    }
                }
                if(posicion == 4) {
                    String codCampoOrdenacion5 = rs.getString(conf.getString("SQL.CAMPOORDEINFORME.codCampo"));
                    gVO.setAtributo("codCampoOrdenacion5",codCampoOrdenacion5);
                    String codSentidoOrdenacion5 = rs.getString(conf.getString("SQL.CAMPOORDEINFORME.tipoOrden"));
                    if("A".equals(codSentidoOrdenacion5)) {
                        gVO.setAtributo("codSentidoOrdenacion5","ASC");
                    } else {
                        gVO.setAtributo("codSentidoOrdenacion5","DESC");
                    }
                }
            }
            rs.close();

            // PESTAÑA DE FILTRO
            sql = "SELECT " + conf.getString("SQL.CAMPOCONDICIONINFORME.posicion") + ","+
                    conf.getString("SQL.CAMPOCONDICIONINFORME.clausula") + "," +
                    conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo") + "," +
                    conf.getString("SQL.CAMPOCONDICIONINFORME.operador") + "," +
                    conf.getString("SQL.CAMPOCONDICIONINFORME.valor") +
                    " FROM CAMPOCONDICIONINFORME WHERE " +
                    conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") + "=" + codEstructura;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                int posicion = rs.getInt(conf.getString("SQL.CAMPOCONDICIONINFORME.posicion"));
                if(posicion == 0) {
                    String codCampo1 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo"));
                    gVO.setAtributo("codCampo1",codCampo1);
                    String codCondicion1 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.operador"));
                    gVO.setAtributo("codCondicion1",codCondicion1);
                    String valor1 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.valor"));
                    gVO.setAtributo("valor1",valor1);
                }
                if(posicion == 1) {
                    String codCampo2 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo"));
                    gVO.setAtributo("codCampo2",codCampo2);
                    String codCondicion2 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.operador"));
                    gVO.setAtributo("codCondicion2",codCondicion2);
                    String valor2 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.valor"));
                    gVO.setAtributo("valor2",valor2);
                    String codOperador1 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.clausula"));
                    gVO.setAtributo("codOperador1",codOperador1);
                }
                if(posicion == 2) {
                    String codCampo3 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo"));
                    gVO.setAtributo("codCampo3",codCampo3);
                    String codCondicion3 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.operador"));
                    gVO.setAtributo("codCondicion3",codCondicion3);
                    String valor3 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.valor"));
                    gVO.setAtributo("valor3",valor3);
                    String codOperador2 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.clausula"));
                    gVO.setAtributo("codOperador2",codOperador2);
                }
                if(posicion == 3) {
                    String codCampo4 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo"));
                    gVO.setAtributo("codCampo4",codCampo4);
                    String codCondicion4 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.operador"));
                    gVO.setAtributo("codCondicion4",codCondicion4);
                    String valor4 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.valor"));
                    gVO.setAtributo("valor4",valor4);
                    String codOperador3 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.clausula"));
                    gVO.setAtributo("codOperador3",codOperador3);

                }
                if(posicion == 4) {
                    String codCampo5 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo"));
                    gVO.setAtributo("codCampo5",codCampo5);
                    String codCondicion5 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.operador"));
                    gVO.setAtributo("codCondicion5",codCondicion5);
                    String valor5 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.valor"));
                    gVO.setAtributo("valor5",valor5);
                    String codOperador4 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.clausula"));
                    gVO.setAtributo("codOperador4",codOperador4);
                }
                if(posicion == 5) {
                    String codCampo6 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo"));
                    gVO.setAtributo("codCampo6",codCampo6);
                    String codCondicion6 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.operador"));
                    gVO.setAtributo("codCondicion6",codCondicion6);
                    String valor6 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.valor"));
                    gVO.setAtributo("valor6",valor6);
                    String codOperador5 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.clausula"));
                    gVO.setAtributo("codOperador5",codOperador5);
                }
                if(posicion == 6) {
                    String codCampo7 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo"));
                    gVO.setAtributo("codCampo7",codCampo7);
                    String codCondicion7 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.operador"));
                    gVO.setAtributo("codCondicion7",codCondicion7);
                    String valor7 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.valor"));
                    gVO.setAtributo("valor7",valor7);
                    String codOperador6 = rs.getString(conf.getString("SQL.CAMPOCONDICIONINFORME.clausula"));
                    gVO.setAtributo("codOperador6",codOperador6);
                }
            }

            //m_Log.info("GeneradorInformesDAO, obtenerInforme");
            rs.close();
            st.close();
        }catch (Exception e) {
            gVO = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.GeneradorInformesDAO.obtenerInforme"), e);
        }finally {
            try{
                oad.devolverConexion(con);
             } catch(Exception ex) {
                ex.printStackTrace();
                 if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage());
             }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        return gVO;
    }

    public int modificarInforme(GeneralValueObject gVO,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException,SQLException{
        String sql= "";
        int res = 0;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;


        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            int resultado1 = modificarEstructuraInforme(gVO,conexion,params);
            if(resultado1 >0) {
                res = resultado1;
                int resultado2 = modificarCampoSeleccionInforme(gVO,conexion,params);
                res = resultado2;
                int resultado3 = modificarCampoCondicionInforme(gVO,conexion,params);
                res = resultado3;
                int resultado4 = modificarCampoOrdenInforme(gVO,conexion,params);
                res = resultado4;
                int resultado5 = modificarInformeGenerador(gVO,conexion,params);
                res = resultado5;
                int resultado6 = modificarEtiqPlt(gVO,conexion,params);
                res = resultado6;
            }

            abd.finTransaccion(conexion);

        } catch (SQLException ex) {
            conexion.rollback();
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        } catch (BDException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                abd.devolverConexion(conexion);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }

        return res;
    }

    public int modificarEstructuraInforme(GeneralValueObject gVO,Connection conexion,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException,SQLException{
        String sql= "";
        int res = 0;
        ResultSet rs = null;
        PreparedStatement pst = null;

        String codEntidad = (String) gVO.getAtributo("codEntidad");
        String consultaSQL = (String) gVO.getAtributo("consultaSQL");
        String codEstructura = (String) gVO.getAtributo("codEstructura");
        Vector cods_estructura = new Vector();
        EstructuraXerador estructuraXerador = new EstructuraXerador(conexion);
        try{

            /* NO FUNCIONA EL UPDATE DEL CLOB !!!! NO SÉ POR QUE?
         sql = "UPDATE ESTRUCTURAINFORME SET " +
                     conf.getString("SQL.ESTRUCTURAINFORME.consultaSQL") + "= ? WHERE " +
                     conf.getString("SQL.ESTRUCTURAINFORME.codEstructura") + "=" + codEstructura;
               if(m_Log.isDebugEnabled()) m_Log.debug(sql);
               pst = conexion.prepareStatement(sql);
               char[] consulta =	(consultaSQL != null)? (consultaSQL).toCharArray(): null;
               if (consulta == null) {
                   pst.setNull(1, java.sql.Types.LONGVARCHAR);
               } else {
                   java.io.CharArrayReader cr = new java.io.CharArrayReader(consulta);
                   pst.setCharacterStream(1, cr, consulta.length);
               }
               res = pst.executeUpdate();
			
               pst.close();
           */
            sql = "SELECT " + conf.getString("SQL.ESTRUCTURAINFORME.codEntidad") + "," +
                    conf.getString("SQL.ESTRUCTURAINFORME.codPadre") + "," +
                    conf.getString("SQL.ESTRUCTURAINFORME.posicion") +
                    " FROM ESTRUCTURAINFORME WHERE " +
                    conf.getString("SQL.ESTRUCTURAINFORME.codEstructura") + "=" + codEstructura;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            Statement st = conexion.createStatement();
            rs = st.executeQuery(sql);
            String c1 = "";
            String c2 = "";
            String c3 = "";
            while(rs.next()){
                c1 = rs.getString(conf.getString("SQL.ESTRUCTURAINFORME.codEntidad"));
                c2 = rs.getString(conf.getString("SQL.ESTRUCTURAINFORME.codPadre"));
                c3 = rs.getString(conf.getString("SQL.ESTRUCTURAINFORME.posicion"));
            }
            rs.close();
            st.close();
            
            sql = "select cod_estructura from ESTRUCTURAINFORME where cod_pai=" + codEstructura;
            st = conexion.createStatement();
            m_Log.debug("consulta en modificarEstructuraInforme: "+sql);
            rs = st.executeQuery(sql);
            while (rs.next()) {
                cods_estructura.add(rs.getString("cod_estructura"));
            }
            rs.close();
            st.close();
            for(int i=0;i<cods_estructura.size();i++) {
                estructuraXerador.BorrarDependencias((String) cods_estructura.elementAt(i));
            }
            sql = "DELETE FROM ESTRUCTURAINFORME WHERE " +
                    conf.getString("SQL.ESTRUCTURAINFORME.codEstructura") + "=" + codEstructura;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = conexion.createStatement();
            res = st.executeUpdate(sql);
            st.close();

            sql = "INSERT INTO ESTRUCTURAINFORME (" + conf.getString("SQL.ESTRUCTURAINFORME.codEstructura") +
                    "," + conf.getString("SQL.ESTRUCTURAINFORME.codEntidad") + "," +
                    conf.getString("SQL.ESTRUCTURAINFORME.codPadre") + "," +
                    conf.getString("SQL.ESTRUCTURAINFORME.consultaSQL") + "," +
                    conf.getString("SQL.ESTRUCTURAINFORME.posicion") + ") VALUES (" + codEstructura +
                    "," + c1 + ","+ c2 + ",?,"+c3+")";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            pst = conexion.prepareStatement(sql);
            char[] consulta =	(consultaSQL != null)? (consultaSQL).toCharArray(): null;
            if (consulta == null) {
                pst.setNull(1, java.sql.Types.LONGVARCHAR);
            } else {
                java.io.CharArrayReader cr = new java.io.CharArrayReader(consulta);
                pst.setCharacterStream(1, cr, consulta.length);
            }
            res = pst.executeUpdate();

            pst.close();


        } catch (Exception ex) {
            conexion.rollback();
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }
        return res;
    }

    public int modificarCampoSeleccionInforme(GeneralValueObject gVO,Connection conexion,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException,SQLException{
        String sql= "";
        int res = 0;
        int resEliminar = 0;
        ResultSet rs = null;
        Statement st = null;

        String codEstructura = (String) gVO.getAtributo("codEstructura");
        Vector listaCodCamposElegidos = new Vector();
        listaCodCamposElegidos = (Vector) gVO.getAtributo("listaCodCamposElegidos");

        try{

            sql = "DELETE FROM CAMPOSELECCIONINFORME WHERE " +
                    conf.getString("SQL.CAMPOSELECCIONINFORME.codEstructura") + "=" + codEstructura;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = conexion.createStatement();
            resEliminar = st.executeUpdate(sql);
            st.close();
            for(int i=0;i<listaCodCamposElegidos.size();i++) {
                sql = "INSERT INTO CAMPOSELECCIONINFORME (" + conf.getString("SQL.CAMPOSELECCIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOSELECCIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOSELECCIONINFORME.codCampoInforme") + "," +
                        conf.getString("SQL.CAMPOSELECCIONINFORME.ancho") + ") VALUES (" +
                        codEstructura + "," + i + "," + listaCodCamposElegidos.elementAt(i) + ",null)";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(listaCodCamposElegidos.size() == 0) {
                res =1;
            }


        } catch (SQLException ex) {
            conexion.rollback();
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }

        return res;
    }

    public int modificarCampoCondicionInforme(GeneralValueObject gVO,Connection conexion,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException,SQLException{
        String sql= "";
        int res = 0;
        int resEliminar = 0;
        ResultSet rs = null;
        Statement st = null;

        String codEstructura = (String) gVO.getAtributo("codEstructura");
        String codCampo1 = (String) gVO.getAtributo("codCampo1");
        String codCampo2 = (String) gVO.getAtributo("codCampo2");
        String codCampo3 = (String) gVO.getAtributo("codCampo3");
        String codCampo4 = (String) gVO.getAtributo("codCampo4");
        String codCampo5 = (String) gVO.getAtributo("codCampo5");
        String codCampo6 = (String) gVO.getAtributo("codCampo6");
        String codCampo7 = (String) gVO.getAtributo("codCampo7");
        String codCondicion1 = (String) gVO.getAtributo("codCondicion1");
        String codCondicion2 = (String) gVO.getAtributo("codCondicion2");
        String codCondicion3 = (String) gVO.getAtributo("codCondicion3");
        String codCondicion4 = (String) gVO.getAtributo("codCondicion4");
        String codCondicion5 = (String) gVO.getAtributo("codCondicion5");
        String codCondicion6 = (String) gVO.getAtributo("codCondicion6");
        String codCondicion7 = (String) gVO.getAtributo("codCondicion7");
        String valor1 = (String) gVO.getAtributo("valor1");
        String valor2 = (String) gVO.getAtributo("valor2");
        String valor3 = (String) gVO.getAtributo("valor3");
        String valor4 = (String) gVO.getAtributo("valor4");
        String valor5 = (String) gVO.getAtributo("valor5");
        String valor6 = (String) gVO.getAtributo("valor6");
        String valor7 = (String) gVO.getAtributo("valor7");
        String codOperador1 = (String) gVO.getAtributo("codOperador1");
        String codOperador2 = (String) gVO.getAtributo("codOperador2");
        String codOperador3 = (String) gVO.getAtributo("codOperador3");
        String codOperador4 = (String) gVO.getAtributo("codOperador4");
        String codOperador5 = (String) gVO.getAtributo("codOperador5");
        String codOperador6 = (String) gVO.getAtributo("codOperador6");

        try{

            sql = "DELETE FROM CAMPOCONDICIONINFORME WHERE " +
                    conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") + "=" + codEstructura;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = conexion.createStatement();
            resEliminar = st.executeUpdate(sql);
            st.close();
            if(!"".equals(codCampo1)) {
                sql = "INSERT INTO CAMPOCONDICIONINFORME (" + conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOCONDICIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.clausula") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.operador") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.valor") + ") VALUES (" + codEstructura +
                        ",0,null," + codCampo1 + ",'" + codCondicion1 + "',";
                if(valor1 == null) {
                    sql += valor1 + ")";
                } else {
                    sql += "'" + valor1 + "')";
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codOperador1)) {
                sql = "INSERT INTO CAMPOCONDICIONINFORME (" + conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOCONDICIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.clausula") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.operador") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.valor") + ") VALUES (" + codEstructura +
                        ",1,'" + codOperador1 + "'," + codCampo2 + ",'" + codCondicion2 + "',";
                if(valor2 == null) {
                    sql += valor2 + ")";
                } else {
                    sql += "'" + valor2 + "')";
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codOperador2)) {
                sql = "INSERT INTO CAMPOCONDICIONINFORME (" + conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOCONDICIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.clausula") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.operador") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.valor") + ") VALUES (" + codEstructura +
                        ",2,'" + codOperador2 + "'," + codCampo3 + ",'" + codCondicion3 + "',";
                if(valor3 == null) {
                    sql += valor3 + ")";
                } else {
                    sql += "'" + valor3 + "')";
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codOperador3)) {
                sql = "INSERT INTO CAMPOCONDICIONINFORME (" + conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOCONDICIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.clausula") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.operador") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.valor") + ") VALUES (" + codEstructura +
                        ",3,'" + codOperador3 + "'," + codCampo4 + ",'" + codCondicion4 + "',";
                if(valor4 == null) {
                    sql += valor4 + ")";
                } else {
                    sql += "'" + valor4 + "')";
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codOperador4)) {
                sql = "INSERT INTO CAMPOCONDICIONINFORME (" + conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOCONDICIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.clausula") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.operador") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.valor") + ") VALUES (" + codEstructura +
                        ",4,'" + codOperador4 + "'," + codCampo5+ ",'" + codCondicion5 + "',";
                if(valor5 == null) {
                    sql += valor5 + ")";
                } else {
                    sql += "'" + valor5 + "')";
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codOperador5)) {
                sql = "INSERT INTO CAMPOCONDICIONINFORME (" + conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOCONDICIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.clausula") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.operador") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.valor") + ") VALUES (" + codEstructura +
                        ",5,'" + codOperador5 + "'," + codCampo6 + ",'" + codCondicion6 + "',";
                if(valor6 == null) {
                    sql += valor6 + ")";
                } else {
                    sql += "'" + valor6 + "')";
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codOperador6)) {
                sql = "INSERT INTO CAMPOCONDICIONINFORME (" + conf.getString("SQL.CAMPOCONDICIONINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOCONDICIONINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.clausula") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.operador") + "," +
                        conf.getString("SQL.CAMPOCONDICIONINFORME.valor") + ") VALUES (" + codEstructura +
                        ",6,'" + codOperador6 + "'," + codCampo7 + ",'" + codCondicion7 + "',";
                if(valor7 == null) {
                    sql += valor7 + ")";
                } else {
                    sql += "'" + valor7 + "')";
                }
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }

        } catch (SQLException ex) {
            conexion.rollback();
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }

        return res;
    }

    public int modificarCampoOrdenInforme(GeneralValueObject gVO,Connection conexion,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException,SQLException{
        String sql= "";
        int res = 0;
        int resEliminar = 0;
        ResultSet rs = null;
        Statement st = null;

        String codEstructura = (String) gVO.getAtributo("codEstructura");
        String codCampoOrdenacion1 = (String) gVO.getAtributo("codCampoOrdenacion1");
        String codCampoOrdenacion2 = (String) gVO.getAtributo("codCampoOrdenacion2");
        String codCampoOrdenacion3 = (String) gVO.getAtributo("codCampoOrdenacion3");
        String codCampoOrdenacion4 = (String) gVO.getAtributo("codCampoOrdenacion4");
        String codCampoOrdenacion5 = (String) gVO.getAtributo("codCampoOrdenacion5");
        String codSentidoOrdenacion1 = (String) gVO.getAtributo("codSentidoOrdenacion1");
        String codSentidoOrdenacion2 = (String) gVO.getAtributo("codSentidoOrdenacion2");
        String codSentidoOrdenacion3 = (String) gVO.getAtributo("codSentidoOrdenacion3");
        String codSentidoOrdenacion4 = (String) gVO.getAtributo("codSentidoOrdenacion4");
        String codSentidoOrdenacion5 = (String) gVO.getAtributo("codSentidoOrdenacion5");
        String codSentido1 = "";
        String codSentido2 = "";
        String codSentido3 = "";
        String codSentido4 = "";
        String codSentido5 = "";
        if(codSentidoOrdenacion1 != null && !"".equals(codSentidoOrdenacion1)) {
            if("ASC".equals(codSentidoOrdenacion1)) {
                codSentido1 = "A";
            } else if("DESC".equals(codSentidoOrdenacion1)) {
                codSentido1 = "D";
            }
        }
        if(codSentidoOrdenacion2 != null && !"".equals(codSentidoOrdenacion2)) {
            if("ASC".equals(codSentidoOrdenacion2)) {
                codSentido2 = "A";
            } else if("DESC".equals(codSentidoOrdenacion2)) {
                codSentido2 = "D";
            }
        }
        if(codSentidoOrdenacion3 != null && !"".equals(codSentidoOrdenacion3)) {
            if("ASC".equals(codSentidoOrdenacion3)) {
                codSentido3 = "A";
            } else if("DESC".equals(codSentidoOrdenacion3)) {
                codSentido3 = "D";
            }
        }
        if(codSentidoOrdenacion4 != null && !"".equals(codSentidoOrdenacion4)) {
            if("ASC".equals(codSentidoOrdenacion4)) {
                codSentido4 = "A";
            } else if("DESC".equals(codSentidoOrdenacion4)) {
                codSentido4 = "D";
            }
        }
        if(codSentidoOrdenacion5 != null && !"".equals(codSentidoOrdenacion5)) {
            if("ASC".equals(codSentidoOrdenacion5)) {
                codSentido5 = "A";
            } else if("DESC".equals(codSentidoOrdenacion5)) {
                codSentido5 = "D";
            }
        }

        try{
            sql = "DELETE FROM CAMPOORDEINFORME WHERE " +
                    conf.getString("SQL.CAMPOORDEINFORME.codEstructura") + "=" + codEstructura;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = conexion.createStatement();
            resEliminar = st.executeUpdate(sql);
            st.close();

            if(!"".equals(codCampoOrdenacion1)) {
                sql = "INSERT INTO CAMPOORDEINFORME (" + conf.getString("SQL.CAMPOORDEINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOORDEINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.tipoOrden") + ") VALUES (" + codEstructura +
                        ",0," + codCampoOrdenacion1 + ",'" + codSentido1 + "')";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codCampoOrdenacion2)) {
                sql = "INSERT INTO CAMPOORDEINFORME (" + conf.getString("SQL.CAMPOORDEINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOORDEINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.tipoOrden") + ") VALUES (" + codEstructura +
                        ",1," + codCampoOrdenacion2 + ",'" + codSentido2 + "')";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codCampoOrdenacion3)) {
                sql = "INSERT INTO CAMPOORDEINFORME (" + conf.getString("SQL.CAMPOORDEINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOORDEINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.tipoOrden") + ") VALUES (" + codEstructura +
                        ",2," + codCampoOrdenacion3 + ",'" + codSentido3 + "')";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codCampoOrdenacion4)) {
                sql = "INSERT INTO CAMPOORDEINFORME (" + conf.getString("SQL.CAMPOORDEINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOORDEINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.tipoOrden") + ") VALUES (" + codEstructura +
                        ",3," + codCampoOrdenacion4 + ",'" + codSentido4 + "')";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(!"".equals(codCampoOrdenacion5)) {
                sql = "INSERT INTO CAMPOORDEINFORME (" + conf.getString("SQL.CAMPOORDEINFORME.codEstructura") +
                        "," + conf.getString("SQL.CAMPOORDEINFORME.posicion") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.codCampo") + "," +
                        conf.getString("SQL.CAMPOORDEINFORME.tipoOrden") + ") VALUES (" + codEstructura +
                        ",4," + codCampoOrdenacion5 + ",'" + codSentido5 + "')";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }

        } catch (SQLException ex) {
            conexion.rollback();
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }

        return res;
    }

    public int modificarInformeGenerador(GeneralValueObject gVO,Connection conexion,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException,SQLException{
        String sql= "";
        int res = 0;
        ResultSet rs = null;
        PreparedStatement pst = null;

        String codAplicacion = (String) gVO.getAtributo("codAplicacion");
        String nombreInforme = (String) gVO.getAtributo("nombreInforme");
        String descInforme = (String) gVO.getAtributo("descInforme");
        String codEstructura = (String) gVO.getAtributo("codEstructura");
        String codInforme = (String) gVO.getAtributo("codInforme");

        try{

            sql = "UPDATE INFORMEXERADOR SET " +
                    conf.getString("SQL.INFORMEXERADOR.nombre") + "='" + nombreInforme + "'," +
                    conf.getString("SQL.INFORMEXERADOR.descripcion") + "='" + descInforme + "'" +
                    " WHERE " + conf.getString("SQL.INFORMEXERADOR.codigo") + "=" + codInforme;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            pst = conexion.prepareStatement(sql);
            res = pst.executeUpdate();

            pst.close();

        } catch (SQLException ex) {
            conexion.rollback();
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }
        return res;
    }

    public int modificarEtiqPlt(GeneralValueObject gVO,Connection conexion,String[] params)
            throws TechnicalException,BDException,AnotacionRegistroException,SQLException{
        String sql= "";
        int res = 0;
        int resEliminar = 0;
        ResultSet rs = null;
        Statement st = null;

        String codInforme = (String) gVO.getAtributo("codInforme");
        String descEntidad = (String) gVO.getAtributo("descEntidad");
        Vector listaCodCamposElegidos = new Vector();
        listaCodCamposElegidos = (Vector) gVO.getAtributo("listaCodCamposElegidos");

        try{
            sql = "DELETE FROM ETIQ_PLT WHERE " +
                    conf.getString("SQL.ETIQ_PLT.codInforme") + "=" + codInforme;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = conexion.createStatement();
            resEliminar = st.executeUpdate(sql);
            st.close();

            for(int i=0;i<listaCodCamposElegidos.size();i++) {
                sql = "INSERT INTO ETIQ_PLT (" + conf.getString("SQL.ETIQ_PLT.codCampo") +
                        "," + conf.getString("SQL.ETIQ_PLT.codInforme") + "," +
                        conf.getString("SQL.ETIQ_PLT.prefijo") + ") VALUES (" +
                        listaCodCamposElegidos.elementAt(i) + "," + codInforme + ",'" +
                        descEntidad + "')";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                st = conexion.createStatement();
                res = st.executeUpdate(sql);
                st.close();
            }
            if(listaCodCamposElegidos.size() == 0) {
                res =1;
            }
        } catch (SQLException ex) {
            conexion.rollback();
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
        }

        return res;
    }

    public Vector listaCamposDisponiblesEnteros(String codEntidad,String[] params)
            throws AnotacionRegistroException, TechnicalException{

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.info("listaCamposDisponiblesEnteros");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        AdaptadorSQLBD oad = null;
        String sql = "";
        Vector listaCamposDisponiblesEntera = new Vector();
        int orden = 0;

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            sql = "SELECT " + conf.getString("SQL.CAMPOINFORME.codigo") + ","+
                    conf.getString("SQL.CAMPOINFORME.nombre") + "," +
                    conf.getString("SQL.CAMPOINFORME.tipo") + "," +
                    conf.getString("SQL.CAMPOINFORME.longitud") + "," +
                    conf.getString("SQL.CAMPOINFORME.campo") + "," +
                    conf.getString("SQL.CAMPOINFORME.nombreAs") +
                    " FROM CAMPOINFORME,CAMPOENTIDADEINFORME WHERE " +
                    conf.getString("SQL.CAMPOENTIDADEINFORME.codCampo") + "=" +
                    conf.getString("SQL.CAMPOINFORME.codigo") + " AND " +
                    conf.getString("SQL.CAMPOENTIDADEINFORME.codEntidad") + "=" + codEntidad;
            String parametros[] = {"2","2"};
            sql += oad.orderUnion(parametros);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                GeneralValueObject g = new GeneralValueObject();
                String codCampo = rs.getString(conf.getString("SQL.CAMPOINFORME.codigo"));
                g.setAtributo("codCampo",codCampo);
                String nombreCampo = rs.getString(conf.getString("SQL.CAMPOINFORME.nombre"));
                g.setAtributo("nombreCampo",nombreCampo);
                String tipo = rs.getString(conf.getString("SQL.CAMPOINFORME.tipo"));
                g.setAtributo("tipo",tipo);
                String longitud = rs.getString(conf.getString("SQL.CAMPOINFORME.longitud"));
                g.setAtributo("longitud",longitud);
                String campo = rs.getString(conf.getString("SQL.CAMPOINFORME.campo"));
                g.setAtributo("campo",campo);
                String nombreAs = rs.getString(conf.getString("SQL.CAMPOINFORME.nombreAs"));
                g.setAtributo("nombreAs",nombreAs);
                listaCamposDisponiblesEntera.addElement(g);
            }
            rs.close();
            st.close();
        }catch (Exception e) {
            listaCamposDisponiblesEntera = null;
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.GeneradorInformesDAO.listaCamposDisponiblesEnteros"), e);
        }finally {
            try{
                oad.devolverConexion(con);
            } catch(Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Exception: " + ex.getMessage()) ;
            }
        }
        //Queremos estar informados de cuando este metodo ha finalizado
        return listaCamposDisponiblesEntera;
    }

}