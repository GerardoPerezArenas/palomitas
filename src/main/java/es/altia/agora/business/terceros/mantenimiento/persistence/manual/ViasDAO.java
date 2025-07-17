// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.mantenimiento.CondicionesBusquedaViaVO;
import es.altia.agora.business.terceros.mantenimiento.ViaEncontradaVO;
import es.altia.agora.business.terceros.mantenimiento.InfoTrameroViaVO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: ViasDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @version 1.0
 */

public class ViasDAO  {

    private static HashMap abreviaturasTiposVias = null;
    private static HashMap descripcionesTiposVias = null;

    private static ViasDAO instance = null;
    protected static Config campos; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Config m_Common;       // Para el fichero de opciones comunes de configuracion.
    protected static Log m_Log =
          LogFactory.getLog(ViasDAO.class.getName());

    protected ViasDAO() {
        super();
        // Queremos usar el fichero de configuracion techserver
        campos = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
        m_Common = ConfigServiceHelper.getConfig("common");
    }

    public static ViasDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread)
            // Las invocaciones de este metodo
            synchronized (ViasDAO.class) {
                if (instance == null) {
                    instance = new ViasDAO();
                }
            }
        }
        return instance;
    }

    public String existeAplicacion(String[] params,String codigo){
        String sql = "";
        String existe = "NO";
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
//            bd.inicioTransaccion(con);
            state = con.createStatement();
            sql = "SELECT A_APL.* " +
                  "FROM "+ GlobalNames.ESQUEMA_GENERICO+ "A_APL A_APL"+
                  " WHERE " +
                    campos.getString("SQL.A_APL.codigo")+"="+codigo;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            if (rs.next()) {
                existe= "SI";
            }
            rs.close();
            state.close();
        }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
//      commitTransaction(abd,conexion);
                bd.devolverConexion(con);
            }catch (BDException e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ViasDAO.getListaVias");
            }
        }
        return existe;
    }

    public Vector getListaNumeraciones(String[] params,GeneralValueObject gVO){
        String sql = "";
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
//            bd.inicioTransaccion(con);
            state = con.createStatement();
            sql =
                    "SELECT DISTINCT T_DSU.*,T_TRM.* FROM T_DSU,T_TRM"+
                            " WHERE " +
                            campos.getString("SQL.T_DSU.situacion")+"='A' AND " +
                            //campos.getString("SQL.T_DPO.suelo")+"="+campos.getString("SQL.T_DSU.identificador")+" AND "+
                            campos.getString("SQL.T_DSU.pais")+"=" + gVO.getAtributo("codPais")+ " AND " +
                            campos.getString("SQL.T_DSU.provincia")+"=" + gVO.getAtributo("codProvincia") + " AND " +
                            campos.getString("SQL.T_DSU.municipio")+"=" + gVO.getAtributo("codMunicipio") + " AND "+
                            campos.getString("SQL.T_DSU.vial")+"=" + gVO.getAtributo("idVia")+" AND " +
                            campos.getString("SQL.T_DSU.paisTRM")+"=" + campos.getString("SQL.T_TRM.pais")+ " AND " +
                            campos.getString("SQL.T_DSU.provinciaTRM")+"=" + campos.getString("SQL.T_TRM.provincia") + " AND " +
                            campos.getString("SQL.T_DSU.municipioTRM")+"=" + campos.getString("SQL.T_TRM.municipio") + " AND "+
                            campos.getString("SQL.T_DSU.vialTRM")+"=" + campos.getString("SQL.T_TRM.vial")+ " AND " +
                            campos.getString("SQL.T_DSU.codigoTRM")+"=" + campos.getString("SQL.T_TRM.codigo") + " AND " +
                            campos.getString("SQL.T_TRM.situacion")+"='A' AND "+
                            campos.getString("SQL.T_DSU.tipoNumeracionTRM")+"=" + campos.getString("SQL.T_TRM.tipoNumeracion");
            /*
           if (gVO.getAtributo("codESI")!= null)
               if (!"".equals(gVO.getAtributo("codESI")))
                   sql+=" AND " + campos.getString("SQL.T_TRM.e_singularNUC")+"=" + gVO.getAtributo("codESI");
           if (gVO.getAtributo("codNUC")!= null)
               if (!"".equals(gVO.getAtributo("codNUC")))
               sql +=  " AND " +campos.getString("SQL.T_TRM.nucleo")+"=" + gVO.getAtributo("codNUC");
         */
            sql += " ORDER BY "+campos.getString("SQL.T_DSU.numeroDesde")+","+
                    campos.getString("SQL.T_DSU.letraDesde")+","+campos.getString("SQL.T_DSU.numeroHasta")+","+
                    campos.getString("SQL.T_DSU.letraHasta");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while (rs.next()) {
                GeneralValueObject vo = new GeneralValueObject();
                //vo.setAtributo("idDPO", rs.getString(campos.getString("SQL.T_DPO.domicilio")));
                vo.setAtributo("codDSU", rs.getString(campos.getString("SQL.T_DSU.identificador")));
                vo.setAtributo("numDesde", rs.getString(campos.getString("SQL.T_DSU.numeroDesde")));
                vo.setAtributo("letraDesde", rs.getString(campos.getString("SQL.T_DSU.letraDesde")));
                vo.setAtributo("numHasta", rs.getString(campos.getString("SQL.T_DSU.numeroHasta")));
                vo.setAtributo("letraHasta", rs.getString(campos.getString("SQL.T_DSU.letraHasta")));
                // TRAMO
                vo.setAtributo("codTipoNumeracion",rs.getString(campos.getString("SQL.T_TRM.tipoNumeracion")));
                vo.setAtributo("codPais",rs.getString(campos.getString("SQL.T_TRM.pais")));
                vo.setAtributo("codProvincia",rs.getString(campos.getString("SQL.T_TRM.provincia")));
                vo.setAtributo("codMunicipio",rs.getString(campos.getString("SQL.T_TRM.municipio")));
                vo.setAtributo("idVia",rs.getString(campos.getString("SQL.T_TRM.vial")));
                vo.setAtributo("codTramo",rs.getString(campos.getString("SQL.T_TRM.codigo")));
                resultado.add(vo);
            }
            rs.close();
            state.close();
        }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
//      commitTransaction(abd,conexion);
                bd.devolverConexion(con);
            }catch (BDException e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ViasDAO.getListaNumeraciones");
            }
        }
        return resultado;
    }

    public Vector getListaTramos(String[] params,GeneralValueObject gVO){
        String sql = "";
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
//            bd.inicioTransaccion(con);
            state = con.createStatement();
            sql = "SELECT T_TRM.* FROM T_TRM WHERE ";
            // CONDICIONES
            sql+= campos.getString("SQL.T_TRM.situacion")+"='A' AND "+
                    campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("idVia");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while (rs.next()) {
                GeneralValueObject vo = new GeneralValueObject();
                // TRAMO
                vo.setAtributo("codTipoNumeracion",rs.getString(campos.getString("SQL.T_TRM.tipoNumeracion")));
                vo.setAtributo("codPais",rs.getString(campos.getString("SQL.T_TRM.pais")));
                vo.setAtributo("codProvincia",rs.getString(campos.getString("SQL.T_TRM.provincia")));
                vo.setAtributo("codMunicipio",rs.getString(campos.getString("SQL.T_TRM.municipio")));
                vo.setAtributo("idVia",rs.getString(campos.getString("SQL.T_TRM.vial")));
                vo.setAtributo("codTramo",rs.getString(campos.getString("SQL.T_TRM.codigo")));
                vo.setAtributo("codDistrito",rs.getString(campos.getString("SQL.T_TRM.distritoSeccion")));
                vo.setAtributo("codSeccion",rs.getString(campos.getString("SQL.T_TRM.seccion")));
                vo.setAtributo("letraSeccion",rs.getString(campos.getString("SQL.T_TRM.letraSeccion")));
                vo.setAtributo("numDesde",rs.getString(campos.getString("SQL.T_TRM.primerNumero")));
                vo.setAtributo("letraDesde",rs.getString(campos.getString("SQL.T_TRM.primeraLetra")));
                vo.setAtributo("numHasta",rs.getString(campos.getString("SQL.T_TRM.ultimoNumero")));
                vo.setAtributo("letraHasta",rs.getString(campos.getString("SQL.T_TRM.ultimaLetra")));
                resultado.add(vo);
            }
            rs.close();
            state.close();
        }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
//      commitTransaction(abd,conexion);
                bd.devolverConexion(con);
            }catch (BDException e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ViasDAO.getListaTramos");
            }
        }
        return resultado;
    }

    public boolean existeTramero(String[] params){
        String sql = "";
        boolean resultado = false;
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            state = con.createStatement();
            sql = "SELECT * FROM T_TRM";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            resultado = rs.next();
            rs.close();
            state.close();
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
                bd.devolverConexion(con);
            }catch (BDException e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ViasDAO.getListaTramos");
            }
        }
        return resultado;
    }

    public Vector eliminarVia(GeneralValueObject gVO, String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        String sql = "";
        Vector resultado = new Vector();
        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            stmt = conexion.createStatement();
            sql = "UPDATE T_VIA SET "+
                    campos.getString("SQL.T_VIA.situacion")+"='B',"+
                    campos.getString("SQL.T_VIA.fechaBaja")+"="+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
                    campos.getString("SQL.T_VIA.usuarioBaja")+"="+gVO.getAtributo("usuario")+
                    " WHERE "+
                    campos.getString("SQL.T_VIA.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_VIA.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_VIA.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_VIA.identificador")+"="+gVO.getAtributo("idVia");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
        }finally{
            commitTransaction(abd,conexion);
        }
        resultado = getListaVias1(params,gVO);
        return resultado;
    }

    public Vector getListaVias(String[] params,GeneralValueObject gVO){
        String sql = "";
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
//            bd.inicioTransaccion(con);
            state = con.createStatement();
            String codECO = (String)gVO.getAtributo("codECO");
            String codESI = (String)gVO.getAtributo("codESI");
            String codNUC = (String)gVO.getAtributo("codNUC");
            String numDesde = (String)gVO.getAtributo("numDesde");
            String tipoNumeracion = (String)gVO.getAtributo("tipoNumeracion");
            String distrito = (String)gVO.getAtributo("distrito");
            String seccion = (String)gVO.getAtributo("seccion");
            String letra = (String)gVO.getAtributo("letra");
            String fecha = (String)gVO.getAtributo("fecha");
            if(m_Log.isDebugEnabled()) m_Log.debug("la fecha es : " + fecha);

            String from = " DISTINCT T_VIA.*,T_TVI.*,T_ECO.*,T_ESI.*,T_NUC.*";

            String where = "";


            if((fecha == null)||("".equals(fecha))) {
                where +=  campos.getString("SQL.T_VIA.situacion")+"='A' AND ";
            }

            if(fecha != null && !"".equals(fecha)) {
                where += " AND " + campos.getString("SQL.T_VIA.fechaVigencia") + "<"+bd.convertir(fecha,AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+
                        " AND (" + campos.getString("SQL.T_VIA.fechaBaja") + ">"+bd.convertir(fecha,AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+
                        " OR " + campos.getString("SQL.T_VIA.fechaBaja") +
                        " IS NULL)";
            }
            if(numDesde!=null && !"".equals(numDesde)){
                where += " AND "+
                        campos.getString("SQL.T_TRM.tipoNumeracion")+"=" + tipoNumeracion+" AND "+
                        "("+numDesde +" BETWEEN "+campos.getString("SQL.T_TRM.primerNumero")+" AND " +campos.getString("SQL.T_TRM.ultimoNumero")+" )";
            }
            where = (!codESI.equals(""))?
                    where +" AND "+campos.getString("SQL.T_TRM.e_singularNUC")+"="+codESI: where;
            where = (!codNUC.equals(""))?
                    where +" AND "+campos.getString("SQL.T_TRM.nucleo")+"="+codNUC: where;

            where = ((distrito!=null)&&!distrito.equals(""))? where+" AND "+campos.getString("SQL.T_TRM.distritoSeccion")+"="+distrito+" AND "+
                    campos.getString("SQL.T_TRM.seccion")+"="+seccion+" AND "+
                    campos.getString("SQL.T_TRM.letraSeccion")+"="+bd.addString(letra):where;

            if((fecha == null)||("".equals(fecha))) {
                where +=  " AND ( "+campos.getString("SQL.T_TRM.situacion")+"='A' ) ";
            }

            where = (!codECO.equals(""))?
                    where+" AND "+campos.getString("SQL.T_ESI.eColectiva")+"="+codECO: where;

            ArrayList join = new ArrayList();
            join.add("T_VIA");
            join.add("LEFT");
            join.add("T_TRM");
            join.add(campos.getString("SQL.T_VIA.pais")+"=" + gVO.getAtributo("codPais")+ " AND " +
                     campos.getString("SQL.T_VIA.provincia")+"=" + gVO.getAtributo("codProvincia") + " AND " +
                     campos.getString("SQL.T_VIA.municipio")+"=" + gVO.getAtributo("codMunicipio") + " AND "+
                     campos.getString("SQL.T_VIA.pais")+"=" + campos.getString("SQL.T_TRM.pais")+ " AND " +
                     campos.getString("SQL.T_VIA.provincia")+"=" + campos.getString("SQL.T_TRM.provincia") + " AND " +
                     campos.getString("SQL.T_VIA.municipio")+"=" + campos.getString("SQL.T_TRM.municipio") + " AND "+
                     campos.getString("SQL.T_VIA.identificador")+"=" + campos.getString("SQL.T_TRM.vial"));
            join.add("LEFT");
            join.add("T_TVI");
            join.add(campos.getString("SQL.T_VIA.tipo")+"="+campos.getString("SQL.T_TVI.codigo"));
            join.add("LEFT");
            join.add("T_NUC");
            join.add(campos.getString("SQL.T_TRM.paisNUC")+"="+campos.getString("SQL.T_NUC.pais")+" AND "+
                     campos.getString("SQL.T_TRM.provinciaNUC")+"="+campos.getString("SQL.T_NUC.provincia")+" AND "+
                     campos.getString("SQL.T_TRM.municipioNUC")+"="+campos.getString("SQL.T_NUC.municipio")+" AND "+
                     campos.getString("SQL.T_TRM.e_singularNUC")+"="+campos.getString("SQL.T_NUC.eSingular")+" AND "+
                     campos.getString("SQL.T_TRM.nucleo")+"="+campos.getString("SQL.T_NUC.codigo"));
            join.add("LEFT");
            join.add("T_ESI");
            join.add(campos.getString("SQL.T_NUC.pais")+"="+campos.getString("SQL.T_ESI.pais")+" AND "+
                     campos.getString("SQL.T_NUC.provincia")+"="+campos.getString("SQL.T_ESI.provincia")+" AND "+
                     campos.getString("SQL.T_NUC.municipio")+"="+campos.getString("SQL.T_ESI.municipio")+" AND "+
                     campos.getString("SQL.T_NUC.eSingular")+"="+campos.getString("SQL.T_ESI.identificador"));
            join.add("LEFT");
            join.add("T_ECO");
            join.add(campos.getString("SQL.T_ESI.provincia")+"="+campos.getString("SQL.T_ECO.provincia")+" AND "+
                     campos.getString("SQL.T_ESI.municipio")+"="+campos.getString("SQL.T_ECO.municipio")+" AND "+
                     campos.getString("SQL.T_ESI.eColectiva")+"="+campos.getString("SQL.T_ECO.identificador"));
            join.add("false");

           sql = bd.join(from,where,(String[]) join.toArray(new String[]{}));

           sql = sql + " ORDER BY "+campos.getString("SQL.T_VIA.nombreVia") + "," +
                        campos.getString("SQL.T_ESI.nombre") + "," + campos.getString("SQL.T_NUC.nombre");

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while (rs.next()) {
                GeneralValueObject vo = new GeneralValueObject();
                vo.setAtributo("idVia", rs.getString(campos.getString("SQL.T_VIA.identificador")));
                vo.setAtributo("codVia", rs.getString(campos.getString("SQL.T_VIA.codVia")));
                vo.setAtributo("descVia", rs.getString(campos.getString("SQL.T_VIA.nombreVia")));
                vo.setAtributo("nombreCorto", rs.getString(campos.getString("SQL.T_VIA.nombreCorto")));
                vo.setAtributo("codTipoVia", rs.getString(campos.getString("SQL.T_TVI.codigo")));
                vo.setAtributo("descTipoVia", rs.getString(campos.getString("SQL.T_TVI.tipoVia")));
                vo.setAtributo("codECO",rs.getString(campos.getString("SQL.T_ECO.identificador")));
                vo.setAtributo("descECO",rs.getString(campos.getString("SQL.T_ECO.nombre")));
                vo.setAtributo("codESI",rs.getString(campos.getString("SQL.T_NUC.eSingular")));
                vo.setAtributo("descESI",rs.getString(campos.getString("SQL.T_ESI.nombre")));
                vo.setAtributo("codNUC",rs.getString(campos.getString("SQL.T_NUC.codigo")));
                vo.setAtributo("descNUC",rs.getString(campos.getString("SQL.T_NUC.nombre")));
                vo.setAtributo("idNUC",rs.getString(campos.getString("SQL.T_NUC.ine")));
                if(!vo.getAtributo("idNUC").equals("")){ // SI TIENE UN NUCLEO LE AÑADO EL NOMBRE
                    String nombreVia = vo.getAtributo("descVia")+" - "+vo.getAtributo("descNUC");
                    if((vo.getAtributo("idNUC").equals("99"))||(vo.getAtributo("idNUC").equals("0"))){
                        nombreVia = vo.getAtributo("descVia")+" - "+vo.getAtributo("descESI");
                    }
                    vo.setAtributo("descVia", nombreVia);
                }
                resultado.add(vo);
            }
            rs.close();
            state.close();
        }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
//      commitTransaction(abd,conexion);
                bd.devolverConexion(con);
            }catch (BDException e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ViasDAO.getListaVias");
            }
        }
        return resultado;
    }

   public Vector getListaVias1(String[] params,GeneralValueObject gVO){
        // VIAS SIN TRAMOS
        String sql = "";
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
//            bd.inicioTransaccion(con);
            state = con.createStatement();
            String codECO = (String)gVO.getAtributo("codECO");
            String codESI = (String)gVO.getAtributo("codESI");
            String codNUC = (String)gVO.getAtributo("codNUC");
            String numDesde = (String)gVO.getAtributo("numDesde");
            String tipoNumeracion = (String)gVO.getAtributo("tipoNumeracion");
            sql =
                    "SELECT DISTINCT T_VIA.*,T_TVI.* "+
                            "FROM T_VIA"
                    + " left join T_TVI on ("+campos.getString("SQL.T_VIA.tipo")+"="+campos.getString("SQL.T_TVI.codigo")+")"
                    + " WHERE " +
                            campos.getString("SQL.T_VIA.situacion")+"='A' AND " +
                            campos.getString("SQL.T_VIA.pais")+"=" + gVO.getAtributo("codPais")+ " AND " +
                            campos.getString("SQL.T_VIA.provincia")+"=" + gVO.getAtributo("codProvincia") + " AND " +
                            campos.getString("SQL.T_VIA.municipio")+"=" + gVO.getAtributo("codMunicipio") ;
            sql += " ORDER BY "//+campos.getString("SQL.T_VIA.codVia");
                    +campos.getString("SQL.T_VIA.nombreVia");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while (rs.next()) {
                GeneralValueObject vo = new GeneralValueObject();
                vo.setAtributo("idVia", rs.getString(campos.getString("SQL.T_VIA.identificador")));
                vo.setAtributo("codVia", rs.getString(campos.getString("SQL.T_VIA.codVia")));
                String descripcion = //rs.getString(campos.getString("SQL.T_TVI.tipoVia"))+ " " +
                        rs.getString(campos.getString("SQL.T_VIA.nombreVia"));
                vo.setAtributo("descVia", descripcion);
                vo.setAtributo("nombreCorto", rs.getString(campos.getString("SQL.T_VIA.nombreCorto")));
                vo.setAtributo("codTipoVia", rs.getString(campos.getString("SQL.T_TVI.codigo")));
                vo.setAtributo("descTipoVia", rs.getString(campos.getString("SQL.T_TVI.tipoVia")));
                resultado.add(vo);
            }
            rs.close();
            state.close();
        }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
//      commitTransaction(abd,conexion);
                bd.devolverConexion(con);
            }catch (BDException e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ViasDAO.getListaVias1");
            }
        }
        return resultado;
    }


    public Vector modificarVia(GeneralValueObject gVO, String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement ps;
        ResultSet rs;
        String sql;
        Vector resultado;
        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            sql = "UPDATE T_VIA SET " +
                    campos.getString("SQL.T_VIA.pais")+"="+gVO.getAtributo("codPais")+","+
                    campos.getString("SQL.T_VIA.provincia")+"="+gVO.getAtributo("codProvincia")+","+
                    campos.getString("SQL.T_VIA.municipio")+"="+gVO.getAtributo("codMunicipio")+","+
                    campos.getString("SQL.T_VIA.codVia")+"="+gVO.getAtributo("codVia")+","+
                    campos.getString("SQL.T_VIA.nombreVia")+"="+abd.addString((String)gVO.getAtributo("descVia"))+","+
                    campos.getString("SQL.T_VIA.nombreCorto")+"="+abd.addString((String)gVO.getAtributo("nombreCorto"))+","+
                    campos.getString("SQL.T_VIA.tipo")+"="+gVO.getAtributo("codTipoVia")+
                    " WHERE " +
                    campos.getString("SQL.T_VIA.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_VIA.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_VIA.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_VIA.identificador")+"="+gVO.getAtributo("idVia");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            int res = ps.executeUpdate();
            ps.close();

            sql = "UPDATE T_DNN SET " +
                  campos.getString("SQL.T_DNN.idTipoVia")+"=?" +
                  " WHERE " + campos.getString("SQL.T_DNN.idPaisD") + "=? AND " + campos.getString("SQL.T_DNN.idProvinciaD") +
                  "=? AND " + campos.getString("SQL.T_DNN.idMunicipioD") + "=? AND " + campos.getString("SQL.T_DNN.codigoVia") + "=?";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt((String)gVO.getAtributo("codTipoVia")));
            ps.setInt(2, Integer.parseInt((String)gVO.getAtributo("codPais")));
            ps.setInt(3, Integer.parseInt((String)gVO.getAtributo("codProvincia")));
            ps.setInt(4, Integer.parseInt((String)gVO.getAtributo("codMunicipio")));
            ps.setInt(5, Integer.parseInt((String)gVO.getAtributo("idVia")));
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("parametros:");
                m_Log.debug("DNN_TVI: " + gVO.getAtributo("codTipoVia"));
                m_Log.debug("DNN_PAI: " + gVO.getAtributo("codPais"));
                m_Log.debug("DNN_PRV: " +  gVO.getAtributo("codProvincia"));
                m_Log.debug("DNN_MUN: " + gVO.getAtributo("codMunicipio"));
                m_Log.debug("DNN_VIA: " + gVO.getAtributo("idVia"));
            }
            ps.executeUpdate();
            ps.close();

            sql = "SELECT " + campos.getString("SQL.T_DNN.idDomicilio") + " FROM T_DNN WHERE " + campos.getString("SQL.T_DNN.idPaisD") +
                  "=? AND " + campos.getString("SQL.T_DNN.idProvinciaD") + "=? AND " + campos.getString("SQL.T_DNN.idMunicipioD") + "=? AND " +
                  campos.getString("SQL.T_DNN.codigoVia") + "=?";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt((String)gVO.getAtributo("codPais")));
            ps.setInt(2, Integer.parseInt((String)gVO.getAtributo("codProvincia")));
            ps.setInt(3, Integer.parseInt((String)gVO.getAtributo("codMunicipio")));
            ps.setInt(4, Integer.parseInt((String)gVO.getAtributo("idVia")));
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("parametros:");
                m_Log.debug("DNN_PAI: " + gVO.getAtributo("codPais"));
                m_Log.debug("DNN_PRV: " +  gVO.getAtributo("codProvincia"));
                m_Log.debug("DNN_MUN: " + gVO.getAtributo("codMunicipio"));
                m_Log.debug("DNN_VIA: " + gVO.getAtributo("idVia"));
            }
            rs = ps.executeQuery();
            Vector cod_dom_modificados = new Vector();
            while (rs.next()) {
                cod_dom_modificados.add(rs.getString(1));
                m_Log.debug("DNN_DOM modificado " + rs.getString(1));
            }
            rs.close();
            rs.close();
            
            
            DomicilioSimpleValueObject domicilioSimpleVO;
            String codDomicilio,localizacion;
            ps.close();
            
            for (int i=0;i<cod_dom_modificados.size();i++) {
            	
                codDomicilio = (String)cod_dom_modificados.get(i);
                
                String from, where;
                from="DNN_TVI,TVI_DES,DNN_VIA,VIA_NOM,DNN_NUD,DNN_LED,DNN_NUH,DNN_LEH,DNN_BLQ,DNN_POR,DNN_ESC," +
                		"DNN_PLT,DNN_PTA,DNN_DMC,ECO_COD,ECO_NOM,ESI_COD,ESI_NOM ";
                where="DNN_DOM=? AND DNN_PAI=MUN_PAI AND DNN_PRV=MUN_PRV AND" +
                		" DNN_MUN=MUN_COD AND MUN_PAI=PAI_COD AND MUN_PAI=PRV_PAI AND MUN_PRV=PRV_COD";
                
                String []join= new String [14];
                
                join[0]= GlobalNames.ESQUEMA_GENERICO+ "T_PAI T_PAI,"+GlobalNames.ESQUEMA_GENERICO+ "T_PRV T_PRV,"+
                		 GlobalNames.ESQUEMA_GENERICO+ "T_MUN T_MUN, T_DNN";
                join[1]="LEFT";
                join[2]="T_VIA";
                join[3]="DNN_VIA=VIA_COD AND DNN_VPA=VIA_PAI AND DNN_VPR=VIA_PRV AND DNN_VMU=VIA_MUN";
                join[4]="LEFT";
                join[5]="T_ESI";
                join[6]="DNN_SPA=ESI_PAI AND DNN_SPR=ESI_PRV AND DNN_SMU=ESI_MUN AND DNN_ESI=ESI_COD";
                join[7]="LEFT";
                join[8]="T_ECO";
                join[9]="ESI_ECO=ECO_COD AND ESI_PAI=ECO_PAI AND ESI_PRV=ECO_PRV AND ESI_MUN=ECO_MUN";
                join[10]="LEFT";
                join[11]="T_TVI";
                join[12]="DNN_TVI=TVI_COD";
                join[13]="false";
                
                
                sql= abd.join(from, where, join);
                ps= conexion.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(codDomicilio));
                rs=ps.executeQuery();
                
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("DNN_DOM: " + codDomicilio);
                
                domicilioSimpleVO = new DomicilioSimpleValueObject();
                while (rs.next()) {
                    domicilioSimpleVO.setIdTipoVia(rs.getString(1));
                    domicilioSimpleVO.setTipoVia(rs.getString(2));
                    domicilioSimpleVO.setCodigoVia(rs.getString(3));
                    domicilioSimpleVO.setDescVia(rs.getString(4));
                    domicilioSimpleVO.setNumDesde(rs.getString(5));
                    domicilioSimpleVO.setLetraDesde(rs.getString(6));
                    domicilioSimpleVO.setNumHasta(rs.getString(7));
                    domicilioSimpleVO.setLetraHasta(rs.getString(8));
                    domicilioSimpleVO.setBloque(rs.getString(9));
                    domicilioSimpleVO.setPortal(rs.getString(10));
                    domicilioSimpleVO.setEscalera(rs.getString(11));
                    domicilioSimpleVO.setPlanta(rs.getString(12));
                    domicilioSimpleVO.setPuerta(rs.getString(13));
                    domicilioSimpleVO.setDomicilio(rs.getString(14));
                    domicilioSimpleVO.setCodECO(rs.getString(15));
                    domicilioSimpleVO.setDescECO(rs.getString(16));
                    domicilioSimpleVO.setCodESI(rs.getString(17));
                    domicilioSimpleVO.setDescESI(rs.getString(18));
                }
                ps.close();
                rs.close();

                localizacion = "";

                if (domicilioSimpleVO.getTipoVia()!=null && !(domicilioSimpleVO.getTipoVia().equals("")) &&
                    !(domicilioSimpleVO.getIdTipoVia().equals(m_Common.getString("T_TVI.CodigoSinTipoVia")))) {
                    localizacion = localizacion.concat(domicilioSimpleVO.getTipoVia()).concat(" ");
                }
                if (domicilioSimpleVO.getDescVia()!=null && !(domicilioSimpleVO.getDescVia().equals(""))) {
                    localizacion = localizacion.concat(domicilioSimpleVO.getDescVia());
                }
                localizacion = localizacion.concat(separadorComaDomicilio(localizacion));
                if (domicilioSimpleVO.getNumDesde()!=null && !(domicilioSimpleVO.getNumDesde().equals(""))) {
                    localizacion = localizacion.concat(domicilioSimpleVO.getNumDesde()).concat(" ");
                }
                if (domicilioSimpleVO.getLetraDesde()!=null && !(domicilioSimpleVO.getLetraDesde().equals(""))) {
                    localizacion = localizacion.concat(domicilioSimpleVO.getLetraDesde());
                }
                if (domicilioSimpleVO.getNumHasta()!=null && !(domicilioSimpleVO.getNumHasta().equals(""))) {
                    localizacion = localizacion.concat("-").concat(domicilioSimpleVO.getNumHasta()).concat(" ");
                }
                if (domicilioSimpleVO.getLetraHasta()!=null && !(domicilioSimpleVO.getLetraHasta().equals(""))) {
                    localizacion = localizacion.concat(domicilioSimpleVO.getLetraHasta());
                }
                if (domicilioSimpleVO.getBloque()!=null && !(domicilioSimpleVO.getBloque().equals(""))) {
                    localizacion = localizacion.concat(" BLq. ").concat(domicilioSimpleVO.getBloque());
                }
                if (domicilioSimpleVO.getPortal()!=null && !(domicilioSimpleVO.getPortal().equals(""))) {
                    localizacion = localizacion.concat(" Port. ").concat(domicilioSimpleVO.getPortal());
                }
                if (domicilioSimpleVO.getEscalera()!=null && !(domicilioSimpleVO.getEscalera().equals(""))) {
                    localizacion = localizacion.concat(" Esc. ").concat(domicilioSimpleVO.getEscalera());
                }
                if (domicilioSimpleVO.getPlanta()!=null && !(domicilioSimpleVO.getPlanta().equals(""))) {
                    localizacion = localizacion.concat(" Plta. ").concat(domicilioSimpleVO.getPlanta());
                }
                if (domicilioSimpleVO.getPuerta()!=null && !(domicilioSimpleVO.getPuerta().equals(""))) {
                    localizacion = localizacion.concat(" Pta. ").concat(domicilioSimpleVO.getPuerta());
                }
                if (domicilioSimpleVO.getDomicilio()!=null && !(domicilioSimpleVO.getDomicilio().equals(""))) {
                    localizacion = localizacion.concat(separadorComaDomicilio(localizacion));
                    localizacion = localizacion.concat(domicilioSimpleVO.getDomicilio());
                }
                if (domicilioSimpleVO.getDescESI()!=null && !(domicilioSimpleVO.getDescESI().equals(""))) {
                    localizacion = localizacion.concat(separadorComaDomicilio(localizacion));
                    localizacion = localizacion.concat(domicilioSimpleVO.getDescESI());
                }
                if (domicilioSimpleVO.getDescECO()!=null && !(domicilioSimpleVO.getDescECO().equals(""))) {
                    localizacion = localizacion.concat(separadorComaDomicilio(localizacion));
                    localizacion = localizacion.concat(domicilioSimpleVO.getDescECO());
                }
                m_Log.debug("---------------------> LOCALIZACION: "+localizacion);
                sql = "UPDATE E_EXP SET EXP_LOC=? WHERE EXP_CLO=?";
                ps = conexion.prepareStatement(sql);
                ps.setString(1, localizacion);
                ps.setInt(2, Integer.parseInt(codDomicilio));
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("EXP_LOC: " + localizacion);
                m_Log.debug("EXP_CLO: " + codDomicilio);
                ps.executeUpdate();
                ps.close();
            }


        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
        }finally{
            commitTransaction(abd,conexion);
        }
        resultado = getListaVias1(params,gVO);
        return resultado;
    }

    public Vector altaVia(GeneralValueObject gVO, String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        Vector resultado = new Vector();
        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            stmt = conexion.createStatement();
            int idNuevaVia = 0;
            sql = "SELECT "+abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.T_VIA.identificador")})+" AS MAXIMO"+
                    " FROM T_VIA";
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                idNuevaVia = rs.getInt("MAXIMO")+1;
            }
            rs.close();
            sql = "INSERT INTO T_VIA("+
                    campos.getString("SQL.T_VIA.pais")+","+
                    campos.getString("SQL.T_VIA.provincia")+","+
                    campos.getString("SQL.T_VIA.municipio")+","+
                    campos.getString("SQL.T_VIA.identificador")+","+
                    campos.getString("SQL.T_VIA.codVia")+","+
                    campos.getString("SQL.T_VIA.nombreVia")+","+
                    campos.getString("SQL.T_VIA.nombreCorto")+","+
                    campos.getString("SQL.T_VIA.tipo")+","+
                    campos.getString("SQL.T_VIA.situacion")+","+
                    campos.getString("SQL.T_VIA.fechaAlta")+","+
                    campos.getString("SQL.T_VIA.usuarioAlta")+","+
                    campos.getString("SQL.T_VIA.fechaBaja")+","+
                    campos.getString("SQL.T_VIA.usuarioBaja")+","+
                    campos.getString("SQL.T_VIA.fechaVigencia")+
                    ") VALUES (" +
                    gVO.getAtributo("codPais")+","+
                    gVO.getAtributo("codProvincia")+","+
                    gVO.getAtributo("codMunicipio")+","+
                    idNuevaVia+","+
                    gVO.getAtributo("codVia")+","+
                    abd.addString((String)gVO.getAtributo("descVia"))+","+
                    abd.addString((String)gVO.getAtributo("nombreCorto"))+","+
                    gVO.getAtributo("codTipoVia")+","+
                    "'A',"+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
                    gVO.getAtributo("usuario")+","+
                    "null,null,"+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+")";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();
            //m_Log.debug("las filas afectadas en el insert son : " + res);
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
        }finally{
            commitTransaction(abd,conexion);
        }
        resultado = getListaVias1(params,gVO);
        return resultado;
    }


    /** Marcos Baltar Vilar
     * Se encarga de insertar una via en la base de datos, pero  antes comprueba que no existe.
     * Devuelve como resultado el codigo de la via
     * @param gVO
     * @param params
     * @return el valor del codigo de la via
     */
    public int altaViaNoRepetido(GeneralValueObject gVO, String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int idNuevaVia = 0;
        Vector resultado = new Vector();
        if (existeVia(params,gVO)){
            return Integer.parseInt((String) gVO.getAtributo("identificacion"));
        }else{
            try{
                abd = new AdaptadorSQLBD(params);
                conexion = abd.getConnection();
                abd.inicioTransaccion(conexion);
                stmt = conexion.createStatement();
                sql = "SELECT "+abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.T_VIA.identificador")})+" AS MAXIMO"+
                        " FROM T_VIA";
                rs = stmt.executeQuery(sql);
                if(rs.next()){
                    idNuevaVia = rs.getInt("MAXIMO")+1;
                }
                rs.close();
                sql = "INSERT INTO T_VIA("+
                        campos.getString("SQL.T_VIA.pais")+","+
                        campos.getString("SQL.T_VIA.provincia")+","+
                        campos.getString("SQL.T_VIA.municipio")+","+
                        campos.getString("SQL.T_VIA.identificador")+","+
                        campos.getString("SQL.T_VIA.codVia")+","+
                        campos.getString("SQL.T_VIA.nombreVia")+","+
                        campos.getString("SQL.T_VIA.nombreCorto")+","+
                        campos.getString("SQL.T_VIA.tipo")+","+
                        campos.getString("SQL.T_VIA.situacion")+","+
                        campos.getString("SQL.T_VIA.fechaAlta")+","+
                        campos.getString("SQL.T_VIA.usuarioAlta")+","+
                        campos.getString("SQL.T_VIA.fechaBaja")+","+
                        campos.getString("SQL.T_VIA.usuarioBaja")+","+
                        campos.getString("SQL.T_VIA.fechaVigencia")+
                        ") VALUES (" +
                        gVO.getAtributo("codPais")+","+
                        gVO.getAtributo("codProvincia")+","+
                        gVO.getAtributo("codMunicipio")+","+
                        idNuevaVia+","+idNuevaVia+","+
                        abd.addString((String)gVO.getAtributo("descVia"))+","+
                        abd.addString((String)gVO.getAtributo("nombreCorto"))+","+
                        gVO.getAtributo("codTipoVia")+","+
                        "'A',"+abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+","+
                        gVO.getAtributo("usuario")+","+
                        "null,null,"+abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }catch (Exception e){
                rollBackTransaction(abd,conexion,e);
            }finally{
                commitTransaction(abd,conexion);
            }
            // resultado = getListaVias1(params,gVO);
            return idNuevaVia;
        }
    }
    /**Marcos Baltar Vilar
     * Se encarga de comprobar si existes una via dada en la base de datos.
     * Consideramos que una via existe en al base de datos si tienen los mismos datos
     * en: Pais, Provincia,Municipio, descripcion via, codigo tipo via y situacion = A
     */
    public boolean existeVia(String[] params, GeneralValueObject gVO)
    {
        boolean resultado = false;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
//            abd.inicioTransaccion(conexion);
            stmt = conexion.createStatement();
            sql = "SELECT "+ campos.getString("SQL.T_VIA.identificador")+" AS ID FROM T_VIA WHERE "+
                    campos.getString("SQL.T_VIA.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_VIA.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_VIA.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_VIA.nombreVia")+"="+abd.addString((String)gVO.getAtributo("descVia"))+" AND "+
                    campos.getString("SQL.T_VIA.tipo")+"="+gVO.getAtributo("codTipoVia")+" AND "+
                    campos.getString("SQL.T_VIA.situacion")+"= 'A'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = stmt.executeQuery(sql);
            resultado = rs.next();
            if(resultado)
                gVO.setAtributo("identificacion",rs.getString("ID"));
            rs.close();
            stmt.close();

        }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
//      commitTransaction(abd,conexion);
                abd.devolverConexion(conexion);
            }catch (BDException e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ViasDAO.getExisteVia");
            }
        }
        return resultado;
    }

    public String modificarViaTerritorio(String[] params,GeneralValueObject gVO){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        Statement stmt1 = null;
        ResultSet rs = null;
        String sql = "";
        String correcto = "SI";
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            //stmt = conexion.createStatement();
            String haCambiadoVial = (String)gVO.getAtributo("haCambiadoVial");
            if(m_Log.isDebugEnabled()) m_Log.debug("haCambiadoVial: "+haCambiadoVial);
            if(haCambiadoVial.equals("SI")){
                // ACTUALIZO LA VIA
                actualizarVia(abd,conexion,gVO);
                // ACTUALIZO EL TRAMERO
                actualizarTramero(conexion,gVO);
            }else{
                gVO.setAtributo("idNuevaVia",gVO.getAtributo("idVia"));
            }
            // ACTUALIZO LAS DIRECCIONES SUELO AFECTADAS
            actualizarDSUs(conexion,gVO,abd);
            // ACTUALIZO LAS DIRECCIONES POSTALES AFECTADAS
            actualizarDomicilios(stmt,gVO,abd);
            //stmt.close();
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
            if ("DSU_SIN_TRAM0".equals ((String) gVO.getAtributo("error")))
                correcto ="DSU_SIN_TRAM0";
            else correcto = "NO";

        }finally{
            commitTransaction(abd,conexion);
        }
        return correcto;
        
    }

    public Vector getListaViasBusqueda(String[] params,GeneralValueObject gVO){
        String sql = "";
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        TransformacionAtributoSelect transformador;
        try{
            bd = new AdaptadorSQLBD(params);
            transformador = new TransformacionAtributoSelect(bd);
            con = bd.getConnection();
//            bd.inicioTransaccion(con);
            state = con.createStatement();
            String nomVia = (String)gVO.getAtributo("nombreVia");
            String numero = (String) gVO.getAtributo("numero");
            String tipoNumeracion = "";
            if(numero != null && !"".equals(numero)) {
                int num = Integer.parseInt(numero);
                if(num%2 == 0) {
                    tipoNumeracion = "2";
                } else {
                    tipoNumeracion = "1";
                }
            } else {
                tipoNumeracion = "0";
            }
            String parteFrom =
                    " DISTINCT T_VIA.*,T_TVI.*,T_ECO.*,T_ESI.*,T_NUC.*";
            String parteWhere = campos.getString("SQL.T_VIA.situacion")+"='A' AND " +
                            campos.getString("SQL.T_VIA.pais")+"=" + gVO.getAtributo("codPais")+ " AND " +
                            campos.getString("SQL.T_VIA.provincia")+"=" + gVO.getAtributo("codProvincia") + " AND " +
                            campos.getString("SQL.T_VIA.municipio")+"=" + gVO.getAtributo("codMunicipio");
            if(nomVia != null && !"".equals(nomVia)) {
                String condicion = transformador.construirCondicionWhereConOperadores(campos.getString("SQL.T_VIA.nombreVia"), nomVia.trim(),false);
                if (!"".equals(condicion)) {
                    parteWhere += " AND " + condicion + " ";
                }
            }
            if(numero!=null && !"".equals(numero)){
                parteWhere+= " AND "+
                        campos.getString("SQL.T_TRM.tipoNumeracion")+"=" + tipoNumeracion+" AND "+
                        "("+numero +" BETWEEN "+campos.getString("SQL.T_TRM.primerNumero")+
                        " AND " +campos.getString("SQL.T_TRM.ultimoNumero")+" )";
            } else {
                parteWhere+= " AND "+
                        campos.getString("SQL.T_TRM.tipoNumeracion")+"=" + tipoNumeracion;
            }
            parteWhere+= " AND ( "+campos.getString("SQL.T_TRM.situacion")+"='A' )";

            ArrayList join = new ArrayList();
            join.add("T_VIA");
            join.add("LEFT");
            join.add("T_TRM");
            join.add(campos.getString("SQL.T_VIA.pais")+"=" + campos.getString("SQL.T_TRM.pais")+ " AND " +
                            campos.getString("SQL.T_VIA.provincia")+"=" + campos.getString("SQL.T_TRM.provincia") + " AND " +
                            campos.getString("SQL.T_VIA.municipio")+"=" + campos.getString("SQL.T_TRM.municipio") + " AND "+
                            campos.getString("SQL.T_VIA.identificador")+"=" + campos.getString("SQL.T_TRM.vial"));
            join.add("LEFT");
            join.add("T_TVI");
            join.add(campos.getString("SQL.T_VIA.tipo")+"="+campos.getString("SQL.T_TVI.codigo"));
            join.add("LEFT");
            join.add("T_NUC");
            join.add(campos.getString("SQL.T_TRM.paisNUC")+"="+campos.getString("SQL.T_NUC.pais")+" AND "+
                    campos.getString("SQL.T_TRM.provinciaNUC")+"="+campos.getString("SQL.T_NUC.provincia")+" AND "+
                    campos.getString("SQL.T_TRM.municipioNUC")+"="+campos.getString("SQL.T_NUC.municipio")+" AND "+
                    campos.getString("SQL.T_TRM.e_singularNUC")+"="+campos.getString("SQL.T_NUC.eSingular")+" AND "+
                    campos.getString("SQL.T_TRM.nucleo")+"="+campos.getString("SQL.T_NUC.codigo"));
            join.add("LEFT");
            join.add("T_ESI");
            join.add(campos.getString("SQL.T_NUC.pais")+"="+campos.getString("SQL.T_ESI.pais")+" AND "+
                    campos.getString("SQL.T_NUC.provincia")+"="+campos.getString("SQL.T_ESI.provincia")+" AND "+
                    campos.getString("SQL.T_NUC.municipio")+"="+campos.getString("SQL.T_ESI.municipio")+" AND "+
                    campos.getString("SQL.T_NUC.eSingular")+"="+campos.getString("SQL.T_ESI.identificador"));
            join.add("LEFT");
            join.add("T_ECO");
            join.add(campos.getString("SQL.T_ESI.provincia")+"="+campos.getString("SQL.T_ECO.provincia")+" AND "+
                    campos.getString("SQL.T_ESI.municipio")+"="+campos.getString("SQL.T_ECO.municipio")+" AND "+
                    campos.getString("SQL.T_ESI.eColectiva")+"="+campos.getString("SQL.T_ECO.identificador"));
            join.add("false");

            sql = bd.join(parteFrom, parteWhere, (String[]) join.toArray(new String[]{}));
            sql += " ORDER BY "+campos.getString("SQL.T_VIA.nombreVia") + "," +
                    campos.getString("SQL.T_ESI.nombre") + "," + campos.getString("SQL.T_NUC.nombre");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while (rs.next()) {
                GeneralValueObject vo = new GeneralValueObject();
                vo.setAtributo("idVia", rs.getString(campos.getString("SQL.T_VIA.identificador")));
                vo.setAtributo("codVia", rs.getString(campos.getString("SQL.T_VIA.codVia")));
                vo.setAtributo("descVia", rs.getString(campos.getString("SQL.T_VIA.nombreVia")));
                vo.setAtributo("nombreCorto", rs.getString(campos.getString("SQL.T_VIA.nombreCorto")));
                vo.setAtributo("codTipoVia", rs.getString(campos.getString("SQL.T_TVI.codigo")));
                vo.setAtributo("descTipoVia", rs.getString(campos.getString("SQL.T_TVI.tipoVia")));
                vo.setAtributo("codECO",rs.getString(campos.getString("SQL.T_ECO.identificador")));
                vo.setAtributo("descECO",rs.getString(campos.getString("SQL.T_ECO.nombre")));
                vo.setAtributo("codESI",rs.getString(campos.getString("SQL.T_NUC.eSingular")));
                vo.setAtributo("descESI",rs.getString(campos.getString("SQL.T_ESI.nombre")));
                vo.setAtributo("codNUC",rs.getString(campos.getString("SQL.T_NUC.codigo")));
                vo.setAtributo("descNUC",rs.getString(campos.getString("SQL.T_NUC.nombre")));
                vo.setAtributo("idNUC",rs.getString(campos.getString("SQL.T_NUC.ine")));
                if(!vo.getAtributo("idNUC").equals("")){ // SI TIENE UN NUCLEO LE AÑADO EL NOMBRE
                    String nombreVia = vo.getAtributo("descVia")+" - "+vo.getAtributo("descNUC");
                    if((vo.getAtributo("idNUC").equals("99"))||(vo.getAtributo("idNUC").equals("0"))){
                        nombreVia = vo.getAtributo("descVia")+" - "+vo.getAtributo("descESI");
                    }
                    vo.setAtributo("descVia", nombreVia);
                }
                resultado.add(vo);
            }
            rs.close();
            state.close();
        }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
//      commitTransaction(abd,conexion);
                bd.devolverConexion(con);
            }catch (BDException e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ViasDAO.getListaViasBusqueda");
            }
        }
        return resultado;
    }

    /* Viales 06/06/05 */
    /* Se diferencia del anterior en que si viene sin numero se muestran todas las vias,
      * sin numero y con numero.
      */
    public Vector getListaViasBusquedaGeneral(String[] params,GeneralValueObject gVO){

        String sql;
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs;
        Statement state;
        TransformacionAtributoSelect transformador;
        try{
            bd = new AdaptadorSQLBD(params);
            transformador = new TransformacionAtributoSelect(bd);
            con = bd.getConnection();
//            bd.inicioTransaccion(con);
            state = con.createStatement();
            String nomVia = (String)gVO.getAtributo("nombreVia");
            String numero = (String) gVO.getAtributo("numero");
            String numeroHasta = (String) gVO.getAtributo("numeroHasta");
            String codECO = (String) gVO.getAtributo("codECO");
            String codESI = (String) gVO.getAtributo("codESI");
            String codTipoVia = (String) gVO.getAtributo("codTipoVia");
            String tipoNumeracion;
            String sNum = (numero != null && !"".equals(numero.trim())?numero:numeroHasta);
            if(sNum != null && !"".equals(sNum)) {
                if(Integer.parseInt(sNum) % 2 == 0) {
                    tipoNumeracion = "2";  // PARES
                } else {
                    tipoNumeracion = "1";  // IMPARES
                }
            } else {
                tipoNumeracion = "0";   // SIN NUMERACION
            }
            String codProvincia = (String) gVO.getAtributo("codProvincia");
            if ((codProvincia == null) || (codProvincia.equals(""))) {
                codProvincia = "'%'";
            }
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            if ((codMunicipio == null) || (codMunicipio.equals(""))) {
                codMunicipio = "'%'";
            }

            String from =  "DISTINCT T_VIA.*,T_TVI.*,T_ECO.*,T_ESI.*,T_NUC.* "+
                    ", " + campos.getString("SQL.T_TRM.primerNumero") + ","  + campos.getString("SQL.T_TRM.primeraLetra") + "," +
                    campos.getString("SQL.T_TRM.ultimoNumero") + ","  + campos.getString("SQL.T_TRM.ultimaLetra") + ","  +
                    campos.getString("SQL.T_PRV.nombre") + ","  + campos.getString("SQL.T_MUN.nombre");

            String where = campos.getString("SQL.T_VIA.situacion")+"='A' AND " +
                            campos.getString("SQL.T_VIA.pais")+"=" + gVO.getAtributo("codPais")+ " AND " +
                            campos.getString("SQL.T_VIA.provincia")+" LIKE (" + codProvincia + ") AND " +
                            campos.getString("SQL.T_VIA.municipio")+" LIKE (" + codMunicipio + ") ";

            if(nomVia != null && !"".equals(nomVia)) {
                String condicion = transformador.construirCondicionWhereConOperadores(campos.getString("SQL.T_VIA.nombreVia"), nomVia.trim(),false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion + " ";
                }
            }
            if(codTipoVia != null && !"".equals(codTipoVia)){
                where+= " AND "+ campos.getString("SQL.T_VIA.tipo")+"=" + codTipoVia;
            }
            if(codESI != null && !"".equals(codESI)){
                where+= " AND "+ campos.getString("SQL.T_TRM.e_singularNUC")+"=" + codESI;
            }
            if(numero != null && !"".equals(numero)){
                where+= " AND "+ campos.getString("SQL.T_TRM.tipoNumeracion")+"=" + tipoNumeracion+" AND "+
                        "("+numero +" BETWEEN "+campos.getString("SQL.T_TRM.primerNumero")+
                        " AND " +campos.getString("SQL.T_TRM.ultimoNumero")+" )";
            }
            if(numeroHasta != null && !"".equals(numeroHasta)){
                where+= " AND "+ campos.getString("SQL.T_TRM.tipoNumeracion")+"=" + tipoNumeracion+" AND "+
                        "("+numeroHasta +" BETWEEN "+campos.getString("SQL.T_TRM.primerNumero")+
                        " AND " +campos.getString("SQL.T_TRM.ultimoNumero")+" )";
            }
            if(codECO != null && !"".equals(codECO)){
                where+= " AND "+ campos.getString("SQL.T_ECO.identificador")+"=" + codECO;
            }
            where += " AND  ( 'A' = "+campos.getString("SQL.T_TRM.situacion")+ " OR "+campos.getString("SQL.T_TRM.situacion")+" IS NULL )";

            String[] join = new String[23];

            join[0] = "T_VIA";
            join[1] = "LEFT";
            join[2] = "T_TRM";
            join[3] = campos.getString("SQL.T_VIA.pais")+"=" + campos.getString("SQL.T_TRM.pais")+ " AND " +
                      campos.getString("SQL.T_VIA.provincia")+"=" + campos.getString("SQL.T_TRM.provincia") + " AND " +
                      campos.getString("SQL.T_VIA.municipio")+"=" + campos.getString("SQL.T_TRM.municipio") + " AND "+
                      campos.getString("SQL.T_VIA.identificador")+"=" + campos.getString("SQL.T_TRM.vial");
            join[4] = "LEFT";
            join[5] = "T_TVI";
            join[6] = campos.getString("SQL.T_VIA.tipo")+"="+campos.getString("SQL.T_TVI.codigo");
            join[7] = "LEFT";
            join[8] = "T_NUC";
            join[9] = campos.getString("SQL.T_TRM.paisNUC")+"="+campos.getString("SQL.T_NUC.pais")+" AND "+
                      campos.getString("SQL.T_TRM.provinciaNUC")+"="+campos.getString("SQL.T_NUC.provincia")+" AND "+
                      campos.getString("SQL.T_TRM.municipioNUC")+"="+campos.getString("SQL.T_NUC.municipio")+" AND "+
                      campos.getString("SQL.T_TRM.e_singularNUC")+"="+campos.getString("SQL.T_NUC.eSingular")+" AND "+
                      campos.getString("SQL.T_TRM.nucleo")+"="+campos.getString("SQL.T_NUC.codigo");
            join[10] = "LEFT";
            join[11] = "T_ESI";
            join[12] = campos.getString("SQL.T_NUC.pais")+"="+campos.getString("SQL.T_ESI.pais")+" AND "+
                       campos.getString("SQL.T_NUC.provincia")+"="+campos.getString("SQL.T_ESI.provincia")+" AND "+
                       campos.getString("SQL.T_NUC.municipio")+"="+campos.getString("SQL.T_ESI.municipio")+" AND "+
                       campos.getString("SQL.T_NUC.eSingular")+"="+campos.getString("SQL.T_ESI.identificador");
            join[13] = "LEFT";
            join[14] = "T_ECO";
            join[15] = campos.getString("SQL.T_ESI.provincia")+"="+campos.getString("SQL.T_ECO.provincia")+" AND "+
                       campos.getString("SQL.T_ESI.municipio")+"="+campos.getString("SQL.T_ECO.municipio")+" AND "+
                       campos.getString("SQL.T_ESI.eColectiva")+"="+campos.getString("SQL.T_ECO.identificador");
            join[16] = "INNER";
            join[17] = GlobalNames.ESQUEMA_GENERICO + "T_PRV";
            join[18] = campos.getString("SQL.T_VIA.provincia")+"=" +campos.getString("SQL.T_PRV.idProvincia");
            join[19] = "INNER";
            join[20] = GlobalNames.ESQUEMA_GENERICO + "T_MUN";
            join[21] = campos.getString("SQL.T_PRV.idProvincia")+"="+campos.getString("SQL.T_MUN.idProvincia")+ " AND "+
                       campos.getString("SQL.T_VIA.municipio")+"="+campos.getString("SQL.T_MUN.idMunicipio");
            join[22] = "false";

            sql = bd.join(from,where,join);
            sql += " ORDER BY "+campos.getString("SQL.T_VIA.nombreVia") + "," +
                    campos.getString("SQL.T_ESI.nombre") + "," + campos.getString("SQL.T_NUC.nombre");


            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while (rs.next()) {
                GeneralValueObject vo = new GeneralValueObject();
                vo.setAtributo("codProvincia", rs.getString(campos.getString("SQL.T_VIA.provincia")));
                vo.setAtributo("descProvincia", rs.getString(campos.getString("SQL.T_PRV.nombre")));
                vo.setAtributo("codMunicipio", rs.getString(campos.getString("SQL.T_VIA.municipio")));
                vo.setAtributo("descMunicipio", rs.getString(campos.getString("SQL.T_MUN.nombre")));
                vo.setAtributo("idVia", rs.getString(campos.getString("SQL.T_VIA.identificador")));
                vo.setAtributo("codVia", rs.getString(campos.getString("SQL.T_VIA.codVia")));
                vo.setAtributo("descVia", rs.getString(campos.getString("SQL.T_VIA.nombreVia")));
                vo.setAtributo("nombreCorto", rs.getString(campos.getString("SQL.T_VIA.nombreCorto")));
                vo.setAtributo("codTipoVia", rs.getString(campos.getString("SQL.T_TVI.codigo")));
                vo.setAtributo("descTipoVia", rs.getString(campos.getString("SQL.T_TVI.tipoVia")));
                vo.setAtributo("codECO",rs.getString(campos.getString("SQL.T_ECO.identificador")));
                vo.setAtributo("descECO",rs.getString(campos.getString("SQL.T_ECO.nombre")));
                vo.setAtributo("codESI",rs.getString(campos.getString("SQL.T_NUC.eSingular")));
                vo.setAtributo("descESI",rs.getString(campos.getString("SQL.T_ESI.nombre")));
                vo.setAtributo("codNUC",rs.getString(campos.getString("SQL.T_NUC.codigo")));
                vo.setAtributo("descNUC",rs.getString(campos.getString("SQL.T_NUC.nombre")));
                vo.setAtributo("idNUC",rs.getString(campos.getString("SQL.T_NUC.ine")));
                vo.setAtributo("primerNumero", rs.getString(campos.getString("SQL.T_TRM.primerNumero")));
                vo.setAtributo("primeraLetra", rs.getString(campos.getString("SQL.T_TRM.primeraLetra")));
                vo.setAtributo("ultimoNumero", rs.getString(campos.getString("SQL.T_TRM.ultimoNumero")));
                vo.setAtributo("ultimaLetra", rs.getString(campos.getString("SQL.T_TRM.ultimaLetra")));
                resultado.add(vo);
            }
            rs.close();
            state.close();
        }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
//      commitTransaction(abd,conexion);
                bd.devolverConexion(con);
            }catch (BDException e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ViasDAO.getListaViasBusquedaGeneral");
            }
        }
        return resultado;
    }
    /* Fin viales 06/06/05 */


    /** Busqueda de Vias en la BB.DD. del SGE
     * @return Colección de ViaEncontradaVO con las vias que cumplen las condiciones.
     * @param condBusqueda Bean con las condiciones de busqueda de la via
     * @param params Array con los parametros de conexion a BB.DD.
     * @throws TechnicalException si hay algun error contra la BB.DD. **/

    public Collection<ViaEncontradaVO> getViasByCondiciones(CondicionesBusquedaViaVO condBusqueda, String[] params)
    throws TechnicalException {

        m_Log.debug("BUSQUEDA DE VIAS EN LA BBDD DEL SGE");
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TransformacionAtributoSelect trans = new TransformacionAtributoSelect(abd);

        StringBuffer sqlStrBuff = new StringBuffer();
        sqlStrBuff.append("SELECT VIA.VIA_COD, VIA.VIA_NOM, VIA.VIA_NOC, VIA.VIA_TVI, TVI.TVI_DES, VIA.VIA_PAI, ");
        sqlStrBuff.append("VIA.VIA_PRV, VIA.VIA_MUN, PAI.PAI_NOM, PRV.PRV_NOM, MUN.MUN_NOM, TRM.TRM_COD, TRM.TRM_PNU, ");
        sqlStrBuff.append("TRM.TRM_PLE, TRM.TRM_ULN, TRM.TRM_ULL, TRM.TRM_NUC, NUC.NUC_NOM, TRM.TRM_NES, ESI.ESI_NOM, ");
        sqlStrBuff.append("ECO.ECO_COD, ECO.ECO_NOM ");
        sqlStrBuff.append("FROM T_VIA VIA ");
        sqlStrBuff.append("JOIN T_TVI TVI ON (VIA.VIA_TVI = TVI.TVI_COD) ");
        sqlStrBuff.append("JOIN ").append(GlobalNames.ESQUEMA_GENERICO).append("T_PAI PAI ON (VIA.VIA_PAI = PAI.PAI_COD) ");
        sqlStrBuff.append("JOIN ").append(GlobalNames.ESQUEMA_GENERICO).append("T_PRV PRV ON ");
        sqlStrBuff.append("(VIA.VIA_PRV = PRV.PRV_COD AND VIA.VIA_PAI = PRV.PRV_PAI) ");
        sqlStrBuff.append("JOIN ").append(GlobalNames.ESQUEMA_GENERICO).append("T_MUN MUN ON ");
        sqlStrBuff.append("(VIA.VIA_MUN = MUN.MUN_COD AND VIA.VIA_PRV = MUN.MUN_PRV AND VIA.VIA_PAI = MUN.MUN_PAI) ");
        sqlStrBuff.append("LEFT JOIN T_TRM TRM ON ");
        sqlStrBuff.append("(VIA.VIA_COD = TRM.TRM_VIA AND VIA.VIA_MUN = TRM.TRM_MUN AND VIA.VIA_PRV = TRM.TRM_PRV ");
        sqlStrBuff.append("AND VIA.VIA_PAI = TRM.TRM_PAI)");
        sqlStrBuff.append("LEFT JOIN T_NUC NUC ON ");
        sqlStrBuff.append("(NUC.NUC_COD = TRM.TRM_NUC AND NUC.NUC_ESI = TRM.TRM_NES AND NUC.NUC_MUN = TRM.TRM_MUN ");
        sqlStrBuff.append("AND TRM.TRM_PRV = NUC.NUC_PRV AND NUC.NUC_PAI = TRM.TRM_PAI)");
        sqlStrBuff.append("LEFT JOIN T_ESI ESI ON ");
        sqlStrBuff.append("(ESI.ESI_COD = TRM.TRM_NES AND ESI.ESI_MUN = TRM.TRM_MUN AND ESI.ESI_PRV = TRM.TRM_PRV ");
        sqlStrBuff.append("AND ESI.ESI_PAI = TRM.TRM_PAI)");
        sqlStrBuff.append("LEFT JOIN T_ECO ECO ON ");
        sqlStrBuff.append("(ECO.ECO_COD = ESI.ESI_ECO AND ECO.ECO_MUN = ESI.ESI_MUN AND ECO.ECO_PRV = ESI.ESI_PRV ");
        sqlStrBuff.append("AND ECO.ECO_PAI = ESI.ESI_PAI)");

        ArrayList<String> whereConds = new ArrayList<String>();
        if (condBusqueda.getNombreVia() != null && !"".equals(condBusqueda.getNombreVia())) {
            String condicion = trans.construirCondicionWhereConOperadores("VIA.VIA_NOM", condBusqueda.getNombreVia(), false);
            whereConds.add(condicion);
        }
        if (condBusqueda.getCodPais() != -1) whereConds.add("PAI.PAI_COD = " + condBusqueda.getCodPais());
        if (condBusqueda.getCodProvincia() != -1) whereConds.add("PRV.PRV_COD = " + condBusqueda.getCodProvincia());
        if (condBusqueda.getCodMunicipio() != -1) whereConds.add("MUN.MUN_COD = " + condBusqueda.getCodMunicipio());        

        whereConds.add("VIA_SIT = 'A'");

        if (whereConds.size() > 0) {
            StringBuffer whereStrBuff = new StringBuffer();
            whereStrBuff.append("WHERE ");
            int numIndex = 1;
            for (String condicion: whereConds) {
                whereStrBuff.append(condicion);
                if (numIndex < whereConds.size()) {
                    numIndex++;
                    whereStrBuff.append(" AND ");
                }
            }
            whereStrBuff.append(" ");
            sqlStrBuff.append(whereStrBuff.toString());
        }
        sqlStrBuff.append(" ORDER BY TVI_DES, VIA_NOM");
        try {

            con = abd.getConnection();

            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA LA BUSQUEDA DE VIAS EN LA BBDD DEL SGE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlStrBuff.toString());
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1 - NOMBRE VIA: " + condBusqueda.getNombreVia());
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 2 - CODIGO PAIS: " + condBusqueda.getCodPais());
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 3 - CODIGO PROVINCIA: " + condBusqueda.getCodProvincia());
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 4 - CODIGO MUNICIPIO: " + condBusqueda.getCodMunicipio());

            ps = con.prepareStatement(sqlStrBuff.toString());
            rs = ps.executeQuery();

            Collection<ViaEncontradaVO> vias = new ArrayList<ViaEncontradaVO>();
            while (rs.next()) {
                int i = 1;
                ViaEncontradaVO via = new ViaEncontradaVO();
                via.setCodigoVia(rs.getInt(i++));
                via.setNombreVia(rs.getString(i++));
                via.setNombreCortoVia(rs.getString(i++));
                via.setCodigoTipoVia(rs.getInt(i++));
                via.setDescTipoVia(rs.getString(i++));
                via.setCodigoPais(rs.getInt(i++));
                via.setCodigoProvincia(rs.getInt(i++));
                via.setCodigoMunicipio(rs.getInt(i++));
                via.setDescPais(rs.getString(i++));
                via.setDescProvincia(rs.getString(i++));
                via.setDescMunicipio(rs.getString(i++));

                if (rs.getInt(i++) != 0) {
                    // Si el codigo de tramero es distinto de 0, recuperamos la informacion del mismo.
                    InfoTrameroViaVO infoTramero = new InfoTrameroViaVO();
                    infoTramero.setPrimerNumero(rs.getInt(i++));
                    infoTramero.setPrimeraLetra(rs.getString(i++));
                    infoTramero.setUltimoNumero(rs.getInt(i++));
                    infoTramero.setUltimaLetra(rs.getString(i++));
                    infoTramero.setCodigoNUC(rs.getInt(i++));
                    infoTramero.setDescNUC(rs.getString(i++));
                    infoTramero.setCodigoESI(rs.getInt(i++));
                    infoTramero.setDescESI(rs.getString(i++));
                    infoTramero.setCodigoECO(rs.getInt(i++));
                    infoTramero.setDescECO(rs.getString(i));

                    via.setInfoTramero(infoTramero);

                } else {
                    via.setInfoTramero(null);
                }
                vias.add(via);
            }
  
            return vias;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new TechnicalException("SE HA PRODUCIDO UN ERROR EN LAS CONSULTAS A BBDD", sqle);
        } catch (BDException bde) {
            throw new TechnicalException("SE HA PRODUCIDO UN ERROR AL INTENTAR CONECTARSE A LA BBDD", bde);
        } finally {
            cerrarStatement(ps);
            cerrarResultSet(rs);
            devolverConexion(abd, con);
        }
    }

    private void cerrarStatement(Statement stmt) throws TechnicalException {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException sqle) {
            throw new TechnicalException("SE HA PRODUCIDO UN ERROR AL CERRAR EL STATEMENT", sqle);
        }
    }

    private void cerrarResultSet(ResultSet rs) throws TechnicalException {
        try {
            if (rs != null) rs.close();
        } catch (SQLException sqle) {
            throw new TechnicalException("SE HA PRODUCIDO UN ERROR AL CERRAR EL RESULT SET", sqle);
        }
        }

    private void devolverConexion(AdaptadorSQLBD abd, Connection con) throws TechnicalException {
        try {
            if (con != null) abd.devolverConexion(con);
        } catch (BDException bde) {
            throw new TechnicalException("SE HA PRODUCIDO UN ERROR AL DEVOLVER LA CONEXION", bde);
        }
    }

/* Fin viales 04 10 2005 */


/* Cambio combo viales */

    public Vector getListaViasESIs(String[] params,GeneralValueObject gVO){
        String sql;
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs;
        Statement state;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
//          bd.inicioTransaccion(con);
            state = con.createStatement();
            String codECO = (String)gVO.getAtributo("codECO");
            String codESI = (String)gVO.getAtributo("codESI");
            String codNUC = (String)gVO.getAtributo("codNUC");
            String distrito = (String)gVO.getAtributo("distrito");
            String seccion = (String)gVO.getAtributo("seccion");
            String letra = (String)gVO.getAtributo("letra");
            String fecha = (String)gVO.getAtributo("fecha");

            if(m_Log.isDebugEnabled()) m_Log.debug("la fecha es : " + fecha);
            if(m_Log.isDebugEnabled()) m_Log.debug("La fecha para la consulta es " + fecha);


            String from = "DISTINCT " + campos.getString("SQL.T_VIA.identificador")
                    +","+ campos.getString("SQL.T_VIA.codVia") +","+ campos.getString("SQL.T_VIA.nombreVia")
                    +","+ campos.getString("SQL.T_VIA.nombreCorto") +","+ campos.getString("SQL.T_TVI.codigo")
                    +","+ campos.getString("SQL.T_TVI.tipoVia") +","+ campos.getString("SQL.T_ECO.identificador")
                    +","+ campos.getString("SQL.T_ECO.nombre") +","+ campos.getString("SQL.T_NUC.eSingular");

            String where = "";
            if ((fecha == null)||("".equals(fecha))) {
                where += campos.getString("SQL.T_VIA.situacion")+"='A' AND ";
            }

            if(fecha != null && !"".equals(fecha)) {
                where += " AND " + campos.getString("SQL.T_VIA.fechaVigencia") + "<"+bd.convertir(fecha,AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+
                        " AND (" + campos.getString("SQL.T_VIA.fechaBaja") + ">" +bd.convertir(fecha,AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+
                        " OR " + campos.getString("SQL.T_VIA.fechaBaja") +
                        " IS NULL)";
            }

            where = (!codESI.equals(""))?
                    where+" AND "+campos.getString("SQL.T_TRM.e_singularNUC")+"="+codESI: where;
            where = (!codNUC.equals(""))?
                    where+" AND "+campos.getString("SQL.T_TRM.nucleo")+"="+codNUC: where;

            where = ((distrito!=null)&&!distrito.equals(""))?
                    where+" AND "+campos.getString("SQL.T_TRM.distritoSeccion")+"="+distrito+" AND "+
                            campos.getString("SQL.T_TRM.seccion")+"="+seccion+" AND "+
                            campos.getString("SQL.T_TRM.letraSeccion")+"="+bd.addString(letra):where;
            if((fecha == null)&&("".equals(fecha))) {
                where += " AND ( "+campos.getString("SQL.T_TRM.situacion")+"='A' ) ";
            }

            where = (!codECO.equals(""))?
                    where+" AND "+campos.getString("SQL.T_ESI.eColectiva")+"="+codECO: where;

            ArrayList<String> join = new ArrayList<String>();
            join.add("T_VIA");
            join.add("LEFT");
            join.add("T_TRM");
            join.add(campos.getString("SQL.T_VIA.pais")+"=" + gVO.getAtributo("codPais")+ " AND " +
                     campos.getString("SQL.T_VIA.provincia")+"=" + gVO.getAtributo("codProvincia") + " AND " +
                     campos.getString("SQL.T_VIA.municipio")+"=" + gVO.getAtributo("codMunicipio") + " AND "+
                     campos.getString("SQL.T_VIA.pais")+"=" + campos.getString("SQL.T_TRM.pais")+ " AND " +
                     campos.getString("SQL.T_VIA.provincia")+"=" + campos.getString("SQL.T_TRM.provincia") + " AND " +
                     campos.getString("SQL.T_VIA.municipio")+"=" + campos.getString("SQL.T_TRM.municipio") + " AND "+
                     campos.getString("SQL.T_VIA.identificador")+"=" + campos.getString("SQL.T_TRM.vial"));
            join.add("LEFT");
            join.add("T_TVI");
            join.add(campos.getString("SQL.T_VIA.tipo")+"="+campos.getString("SQL.T_TVI.codigo"));
            join.add("LEFT");
            join.add("T_NUC");
            join.add(campos.getString("SQL.T_TRM.paisNUC")+"="+campos.getString("SQL.T_NUC.pais")+" AND "+
                     campos.getString("SQL.T_TRM.provinciaNUC")+"="+campos.getString("SQL.T_NUC.provincia")+" AND "+
                     campos.getString("SQL.T_TRM.municipioNUC")+"="+campos.getString("SQL.T_NUC.municipio")+" AND "+
                     campos.getString("SQL.T_TRM.e_singularNUC")+"="+campos.getString("SQL.T_NUC.eSingular")+" AND "+
                     campos.getString("SQL.T_TRM.nucleo")+"="+campos.getString("SQL.T_NUC.codigo"));
            join.add("LEFT");
            join.add("T_ESI");
            join.add(campos.getString("SQL.T_NUC.pais")+"="+campos.getString("SQL.T_ESI.pais")+" AND "+
                     campos.getString("SQL.T_NUC.provincia")+"="+campos.getString("SQL.T_ESI.provincia")+" AND "+
                     campos.getString("SQL.T_NUC.municipio")+"="+campos.getString("SQL.T_ESI.municipio")+" AND "+
                     campos.getString("SQL.T_NUC.eSingular")+"="+campos.getString("SQL.T_ESI.identificador"));
            join.add("LEFT");
            join.add("T_ECO");
            join.add(campos.getString("SQL.T_ESI.provincia")+"="+campos.getString("SQL.T_ECO.provincia")+" AND "+
                     campos.getString("SQL.T_ESI.municipio")+"="+campos.getString("SQL.T_ECO.municipio")+" AND "+
                     campos.getString("SQL.T_ESI.eColectiva")+"="+campos.getString("SQL.T_ECO.identificador"));
            join.add("false");

            sql = bd.join(from,where,(String[]) join.toArray(new String[]{})) +
                " ORDER BY "+ campos.getString("SQL.T_VIA.nombreVia") +","+ campos.getString("SQL.T_ESI.nombre");

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            rs = state.executeQuery(sql);
            while (rs.next()) {
                GeneralValueObject vo = new GeneralValueObject();
                vo.setAtributo("idVia", rs.getString(campos.getString("SQL.T_VIA.identificador")));
                vo.setAtributo("codVia", rs.getString(campos.getString("SQL.T_VIA.codVia")));
                vo.setAtributo("descVia", rs.getString(campos.getString("SQL.T_VIA.nombreVia")));
                vo.setAtributo("nombreCorto", rs.getString(campos.getString("SQL.T_VIA.nombreCorto")));
                vo.setAtributo("codTipoVia", rs.getString(campos.getString("SQL.T_TVI.codigo")));
                vo.setAtributo("descTipoVia", rs.getString(campos.getString("SQL.T_TVI.tipoVia")));
                vo.setAtributo("codECO",rs.getString(campos.getString("SQL.T_ECO.identificador")));
                vo.setAtributo("descECO",rs.getString(campos.getString("SQL.T_ECO.nombre")));
                vo.setAtributo("codESI",rs.getString(campos.getString("SQL.T_NUC.eSingular")));
                vo.setAtributo("descESI",rs.getString(campos.getString("SQL.T_ESI.nombre")));
                String tipoVia = (String) vo.getAtributo("descTipoVia");
                if ("SIN TIPO DE VIA".equals(tipoVia))
                    tipoVia="";
                else tipoVia = tipoVia + " ";
                String nombreVia = vo.getAtributo("descVia")+ " (" + tipoVia + ") - "+vo.getAtributo("descESI");
                vo.setAtributo("descVia", nombreVia);
                resultado.add(vo);
            }
            rs.close();
            state.close();
        }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
//      commitTransaction(abd,conexion);
                bd.devolverConexion(con);
            }catch (BDException e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ViasDAO.getListaViasESIs");
            }
        }
        return resultado;
    }

    public Vector getListaViasSolas(String[] params,GeneralValueObject gVO){
        String sql = "";
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
//            bd.inicioTransaccion(con);
            state = con.createStatement();
            String codECO = (String)gVO.getAtributo("codECO");
            String codESI = (String)gVO.getAtributo("codESI");
            String codNUC = (String)gVO.getAtributo("codNUC");
            String numDesde = (String)gVO.getAtributo("numDesde");
            String tipoNumeracion = (String)gVO.getAtributo("tipoNumeracion");
            String distrito = (String)gVO.getAtributo("distrito");
            String seccion = (String)gVO.getAtributo("seccion");
            String letra = (String)gVO.getAtributo("letra");
            String fecha = (String)gVO.getAtributo("fecha");

            if(m_Log.isDebugEnabled()) m_Log.debug("la fecha es : " + fecha);
            if(m_Log.isDebugEnabled()) m_Log.debug("La fecha para la consulta es " + fecha);


            String from =  "DISTINCT " +
                           campos.getString("SQL.T_VIA.identificador") + "," +
                           campos.getString("SQL.T_VIA.codVia") + "," +
                           campos.getString("SQL.T_VIA.nombreVia") + "," +
                           campos.getString("SQL.T_VIA.nombreCorto") + "," +
                           campos.getString("SQL.T_TVI.codigo") + "," +
                           campos.getString("SQL.T_TVI.tipoVia");

            String where = campos.getString("SQL.T_VIA.pais")+"=" + gVO.getAtributo("codPais")+ " AND " +
                           campos.getString("SQL.T_VIA.provincia")+"=" + gVO.getAtributo("codProvincia") + " AND " +
                           campos.getString("SQL.T_VIA.municipio")+"=" + gVO.getAtributo("codMunicipio") + " AND ";
            if ((fecha == null)||("".equals(fecha))) {
                where += campos.getString("SQL.T_VIA.situacion")+"='A' AND ";
                where += "( "+campos.getString("SQL.T_TRM.situacion")+"='A' OR " +
                        campos.getString("SQL.T_TRM.situacion") + " IS NULL)";
            }else{
                where += campos.getString("SQL.T_VIA.fechaVigencia") + "<"+bd.convertir(fecha,AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                        " AND (" + campos.getString("SQL.T_VIA.fechaBaja") + ">"+ bd.convertir(fecha,AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                        " OR " + campos.getString("SQL.T_VIA.fechaBaja") +
                        " IS NULL)";
            }

            where = (codESI!=null&&!"".equals(codESI))?
                    where+" AND "+campos.getString("SQL.T_TRM.e_singularNUC")+"="+codESI: where;

            where = (codNUC!=null&&!"".equals(codNUC))?
                    where+" AND "+campos.getString("SQL.T_TRM.nucleo")+"="+codNUC: where;

            where = ((distrito!=null)&&!"".equals(distrito))?
                    where+" AND "+campos.getString("SQL.T_TRM.distritoSeccion")+"="+distrito+" AND "+
                            campos.getString("SQL.T_TRM.seccion")+"="+seccion+" AND "+
                            campos.getString("SQL.T_TRM.letraSeccion")+"="+bd.addString(letra):where;
            where = (codECO!=null && !"".equals(codECO))?
                    where +" AND "+campos.getString("SQL.T_ESI.eColectiva")+"="+codECO: where;

            String[] join = new String[17];

           join[0] = "T_VIA";
           join[1] = "LEFT";
           join[2] = "T_TRM";
           join[3] = campos.getString("SQL.T_VIA.pais") + "=" + campos.getString("SQL.T_TRM.pais") + " AND " +
                     campos.getString("SQL.T_VIA.provincia") + "=" + campos.getString("SQL.T_TRM.provincia") + " AND " +
                     campos.getString("SQL.T_VIA.municipio") + "=" + campos.getString("SQL.T_TRM.municipio") + " AND " +
                     campos.getString("SQL.T_VIA.tipo") + "=" + campos.getString("SQL.T_TRM.vial");
           join[4] = "LEFT";
           join[5] = "T_TVI";
           join[6] = campos.getString("SQL.T_VIA.tipo") + "=" + campos.getString("SQL.T_TVI.codigo");

           join[7] = "LEFT";
           join[8] = "T_NUC";
           join[9] = campos.getString("SQL.T_TRM.paisNUC")  + "=" +  campos.getString("SQL.T_NUC.pais")  + " AND " +
                     campos.getString("SQL.T_TRM.provinciaNUC") + "=" + campos.getString("SQL.T_NUC.provincia")  + " AND " +
                     campos.getString("SQL.T_TRM.municipioNUC") + "=" + campos.getString("SQL.T_NUC.municipio")  + " AND " +
                     campos.getString("SQL.T_TRM.e_singularNUC") + "=" + campos.getString("SQL.T_NUC.eSingular")  + " AND " +
                     campos.getString("SQL.T_TRM.nucleo") + "=" + campos.getString("SQL.T_NUC.codigo");
           join[10] = "LEFT";
           join[11] = "T_ESI";
           join[12] = campos.getString("SQL.T_NUC.pais") + "=" + campos.getString("SQL.T_ESI.pais") + " AND " +
                      campos.getString("SQL.T_NUC.provincia") + "=" + campos.getString("SQL.T_ESI.provincia") + " AND " +
                      campos.getString("SQL.T_NUC.municipio") + "=" + campos.getString("SQL.T_ESI.municipio") + " AND " +
                      campos.getString("SQL.T_NUC.eSingular") + "=" + campos.getString("SQL.T_ESI.identificador");
           join[13] = "LEFT";
           join[14] = "T_ECO";
           join[15] = campos.getString("SQL.T_ESI.provincia") + "=" + campos.getString("SQL.T_ECO.provincia") + " AND " +
                      campos.getString("SQL.T_ESI.municipio") + "=" + campos.getString("SQL.T_ECO.municipio") + " AND " +
                      campos.getString("SQL.T_ESI.eColectiva") + "=" + campos.getString("SQL.T_ECO.identificador");

            join[16] = "false";

            sql = bd.join(from,where,join) + " ORDER BY " + campos.getString("SQL.T_VIA.nombreVia")
                    +"," +campos.getString("SQL.T_TVI.tipoVia");

//            sql = "SELECT DISTINCT " + campos.getString("SQL.T_VIA.identificador")
//                    +","+ campos.getString("SQL.T_VIA.codVia") +","+ campos.getString("SQL.T_VIA.nombreVia")
//                    +","+ campos.getString("SQL.T_VIA.nombreCorto") +","+ campos.getString("SQL.T_TVI.codigo")
//                    +","+ campos.getString("SQL.T_TVI.tipoVia")
//                    + " FROM T_VIA,T_TRM,T_TVI,T_ECO,T_ESI,T_NUC WHERE ";
//            if (fecha == null) {
//                sql += campos.getString("SQL.T_VIA.situacion")+"='A' AND ";
//            } else {
//                if ("".equals(fecha)) {
//                    sql += campos.getString("SQL.T_VIA.situacion")+"='A' AND ";
//                }
//            }
//            sql += campos.getString("SQL.T_VIA.pais")+"=" + gVO.getAtributo("codPais")+ " AND " +
//                    campos.getString("SQL.T_VIA.provincia")+"=" + gVO.getAtributo("codProvincia") + " AND " +
//                    campos.getString("SQL.T_VIA.municipio")+"=" + gVO.getAtributo("codMunicipio") + " AND "+
//                     bd.joinLeft(new String[]{campos.getString("SQL.T_VIA.pais"),
//                                              campos.getString("SQL.T_VIA.provincia"),
//                                              campos.getString("SQL.T_VIA.municipio"),
//                                              campos.getString("SQL.T_VIA.identificador"),
//                                              campos.getString("SQL.T_VIA.tipo")},
//                                 new String[]{campos.getString("SQL.T_TRM.pais"),
//                                              campos.getString("SQL.T_TRM.provincia"),
//                                              campos.getString("SQL.T_TRM.municipio"),
//                                              campos.getString("SQL.T_TRM.vial"),
//                                              campos.getString("SQL.T_TVI.codigo")});
//            if(fecha != null && !"".equals(fecha)) {
//
//
//                sql += " AND " + campos.getString("SQL.T_VIA.fechaVigencia") + "<"+bd.fechaHora(fecha,bd."DD/MM/YYYY") +
//                        " AND (" + campos.getString("SQL.T_VIA.fechaBaja") + ">"+ bd.fechaHora(fecha,bd."DD/MM/YYYY") +
//                        " OR " + campos.getString("SQL.T_VIA.fechaBaja") +
//                        " IS NULL)";
//            }
//            /* No se para que sirve
//                  if(numDesde!=null && !"".equals(numDesde)){
//                    sql+= " AND "+
//                      campos.getString("SQL.T_TRM.tipoNumeracion")+"=" + tipoNumeracion+" AND "+
//                      "("+numDesde +" BETWEEN "+campos.getString("SQL.T_TRM.primerNumero")+" AND " +campos.getString("SQL.T_TRM.ultimoNumero")+" )";
//                  }
//                  */
//            sql = (codESI!=null&&!"".equals(codESI))?
//                    sql+" AND "+campos.getString("SQL.T_TRM.e_singularNUC")+"="+codESI: sql;
//            sql = (codNUC!=null&&!"".equals(codNUC))?
//                    sql+" AND "+campos.getString("SQL.T_TRM.nucleo")+"="+codNUC: sql;
//
//            sql = ((distrito!=null)&&!"".equals(distrito))?
//                    sql+" AND "+campos.getString("SQL.T_TRM.distritoSeccion")+"="+distrito+" AND "+
//                            campos.getString("SQL.T_TRM.seccion")+"="+seccion+" AND "+
//                            campos.getString("SQL.T_TRM.letraSeccion")+"="+bd.addString(letra):sql;
//            if(fecha == null) {
//                sql += " AND ( "+campos.getString("SQL.T_TRM.situacion")+"='A' OR " +
//                        campos.getString("SQL.T_TRM.situacion") + " IS NULL) AND ";
//            } else{
//                if ("".equals(fecha)) {
//                    sql += " AND ( "+campos.getString("SQL.T_TRM.situacion")+"='A' OR " +
//                            campos.getString("SQL.T_TRM.situacion") + " IS NULL) AND ";
//                } else sql +=" AND ";
//            }
//
//           sql += bd.joinLeft(new String[]{campos.getString("SQL.T_TRM.paisNUC"),
//                                           campos.getString("SQL.T_TRM.provinciaNUC"),
//                                           campos.getString("SQL.T_TRM.municipioNUC"),
//                                           campos.getString("SQL.T_TRM.e_singularNUC"),
//                                           campos.getString("SQL.T_TRM.nucleo"),
//                                           campos.getString("SQL.T_NUC.pais"),
//                                           campos.getString("SQL.T_NUC.provincia"),
//                                           campos.getString("SQL.T_NUC.municipio"),
//                                           campos.getString("SQL.T_NUC.eSingular"),
//                                           campos.getString("SQL.T_ESI.provincia"),
//                                           campos.getString("SQL.T_ESI.municipio"),
//                                           campos.getString("SQL.T_ESI.eColectiva")},
//                               new String[]{campos.getString("SQL.T_NUC.pais"),
//                                           campos.getString("SQL.T_NUC.provincia"),
//                                           campos.getString("SQL.T_NUC.municipio"),
//                                           campos.getString("SQL.T_NUC.eSingular"),
//                                           campos.getString("SQL.T_NUC.codigo"),
//                                           campos.getString("SQL.T_ESI.pais"),
//                                           campos.getString("SQL.T_ESI.provincia"),
//                                           campos.getString("SQL.T_ESI.municipio"),
//                                           campos.getString("SQL.T_ESI.identificador"),
//                                           campos.getString("SQL.T_ECO.provincia"),
//                                           campos.getString("SQL.T_ECO.municipio"),
//                                           campos.getString("SQL.T_ECO.identificador")});
//            sql = (codECO!=null && !"".equals(codECO))?
//                    sql+" AND "+campos.getString("SQL.T_ESI.eColectiva")+"="+codECO: sql;
//            sql += " ORDER BY " + campos.getString("SQL.T_VIA.nombreVia")
//                    +"," +campos.getString("SQL.T_TVI.tipoVia") ;


            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            //m_Log.debug(sql);

            rs = state.executeQuery(sql);
            while (rs.next()) {
                GeneralValueObject vo = new GeneralValueObject();
                vo.setAtributo("idVia", rs.getString(campos.getString("SQL.T_VIA.identificador")));
                vo.setAtributo("codVia", rs.getString(campos.getString("SQL.T_VIA.codVia")));
                vo.setAtributo("descVia", rs.getString(campos.getString("SQL.T_VIA.nombreVia")));
                vo.setAtributo("nombreCorto", rs.getString(campos.getString("SQL.T_VIA.nombreCorto")));
                vo.setAtributo("codTipoVia", rs.getString(campos.getString("SQL.T_TVI.codigo")));
                vo.setAtributo("descTipoVia", rs.getString(campos.getString("SQL.T_TVI.tipoVia")));
                String tipoVia = (String) vo.getAtributo("descTipoVia");
                if ("SIN TIPO DE VIA".equals(tipoVia))
                    tipoVia="";
                else tipoVia = tipoVia + " ";
                String nombreVia = vo.getAtributo("descVia") +" (" + tipoVia + ")";
                vo.setAtributo("descVia", nombreVia);
                resultado.add(vo);
            }
            rs.close();
            state.close();
        }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
//      commitTransaction(abd,conexion);
                bd.devolverConexion(con);
            }catch (BDException e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ViasDAO.getListaViasSolas");
            }
        }
        return resultado;
    }

/* Fin cambio combo viales */
    private void actualizarVia  (AdaptadorSQLBD adpt, Connection con,GeneralValueObject gVO)
            throws Exception {
        //AdaptadorSQLBD bd = adpt/*null*/;
        String sql = "";
        String fechaOperacion= (String) gVO.getAtributo("fechaOperacion");
        fechaOperacion="'"+fechaOperacion+"'";
        Vector resultado= new Vector();
        
        //con=adpt.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = null;
        int idNuevaVia = 0;
        sql = "SELECT "+adpt.funcionMatematica(adpt.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.T_VIA.identificador")})+" AS MAXIMO"+
                " FROM T_VIA";
        rs = stmt.executeQuery(sql);
        if(rs.next()){
            idNuevaVia = rs.getInt("MAXIMO")+1;
        }
        rs.close();
        stmt.close();
        
        sql = "INSERT INTO T_VIA("+
                campos.getString("SQL.T_VIA.pais")+","+
                campos.getString("SQL.T_VIA.provincia")+","+
                campos.getString("SQL.T_VIA.municipio")+","+
                campos.getString("SQL.T_VIA.identificador")+","+
                campos.getString("SQL.T_VIA.tipo")+","+
                campos.getString("SQL.T_VIA.causaBaja")+","+
                campos.getString("SQL.T_VIA.codVia")+","+
                campos.getString("SQL.T_VIA.nombreVia")+","+
                campos.getString("SQL.T_VIA.nombreCorto")+","+
                campos.getString("SQL.T_VIA.nombreAntiguo")+","+
                campos.getString("SQL.T_VIA.catalogacion")+","+
                campos.getString("SQL.T_VIA.fechaAprobacion")+","+
                campos.getString("SQL.T_VIA.ExpRel")+","+
                campos.getString("SQL.T_VIA.longitud")+","+
                campos.getString("SQL.T_VIA.anchoMax")+","+
                campos.getString("SQL.T_VIA.anchoMin")+","+
                campos.getString("SQL.T_VIA.trafico")+","+
                campos.getString("SQL.T_VIA.sentido")+","+
                campos.getString("SQL.T_VIA.imagen")+","+
                campos.getString("SQL.T_VIA.situacion")+","+
                campos.getString("SQL.T_VIA.fechaAlta")+","+
                campos.getString("SQL.T_VIA.usuarioAlta")+","+
                campos.getString("SQL.T_VIA.fechaBaja")+","+
                campos.getString("SQL.T_VIA.usuarioBaja")+","+
                campos.getString("SQL.T_VIA.fechaVigencia")+
                ") SELECT " +
                campos.getString("SQL.T_VIA.pais")+","+
                campos.getString("SQL.T_VIA.provincia")+","+
                campos.getString("SQL.T_VIA.municipio")+","+
                idNuevaVia+","+
                gVO.getAtributo("codTipoVia")+","+
                campos.getString("SQL.T_VIA.causaBaja")+","+
                gVO.getAtributo("codVia")+","+
                adpt.addString((String)gVO.getAtributo("descVia"))+","+
                adpt.addString((String)gVO.getAtributo("descVia"))+","+
                campos.getString("SQL.T_VIA.nombreVia")+","+
                campos.getString("SQL.T_VIA.catalogacion")+","+
                campos.getString("SQL.T_VIA.fechaAprobacion")+","+
                campos.getString("SQL.T_VIA.ExpRel")+","+
                campos.getString("SQL.T_VIA.longitud")+","+
                campos.getString("SQL.T_VIA.anchoMax")+","+
                campos.getString("SQL.T_VIA.anchoMin")+","+
                campos.getString("SQL.T_VIA.trafico")+","+
                campos.getString("SQL.T_VIA.sentido")+","+
                campos.getString("SQL.T_VIA.imagen")+","+
                campos.getString("SQL.T_VIA.situacion")+","+
                adpt.convertir(fechaOperacion,AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+","+
                campos.getString("SQL.T_VIA.usuarioAlta")+","+
                campos.getString("SQL.T_VIA.fechaBaja")+","+
                campos.getString("SQL.T_VIA.usuarioBaja")+","+
                adpt.convertir(fechaOperacion,AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+
                " FROM T_VIA WHERE "+
                campos.getString("SQL.T_VIA.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_VIA.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_VIA.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_VIA.identificador")+"="+gVO.getAtributo("idVia");
                
        
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = con.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        // DOY DE BAJA LA VIA ANTIGUA
        sql = "UPDATE T_VIA SET "+
                campos.getString("SQL.T_VIA.situacion")+"='B',"+
                //campos.getString("SQL.T_VIA.fechaBaja")+"=SYSDATE,"+
                campos.getString("SQL.T_VIA.fechaBaja")+"=" + adpt.convertir(fechaOperacion,AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ ","+
                campos.getString("SQL.T_VIA.usuarioBaja")+"="+gVO.getAtributo("usuario")+
                " WHERE "+
                campos.getString("SQL.T_VIA.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_VIA.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_VIA.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_VIA.identificador")+"="+gVO.getAtributo("idVia");
        
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = con.createStatement();
        stmt.executeUpdate(sql);
        gVO.setAtributo("idNuevaVia",String.valueOf(idNuevaVia));
        stmt.close();
    }

    private void actualizarTramero(Connection con,GeneralValueObject gVO)
            throws Exception {
        String sql = "";
        Statement stmt = con.createStatement();
        // INSERTO LAS NUEVAS DIRECCIONES SUELO
        sql = "INSERT INTO T_TRM("+
                campos.getString("SQL.T_TRM.tipoNumeracion")+","+
                campos.getString("SQL.T_TRM.pais")+","+
                campos.getString("SQL.T_TRM.provincia")+","+
                campos.getString("SQL.T_TRM.municipio")+","+
                campos.getString("SQL.T_TRM.vial")+","+
                campos.getString("SQL.T_TRM.codigo")+","+
                campos.getString("SQL.T_TRM.paisManzana")+","+
                campos.getString("SQL.T_TRM.provinciaManzana")+","+
                campos.getString("SQL.T_TRM.municipioManzana")+","+
                campos.getString("SQL.T_TRM.distritoManzana")+","+
                campos.getString("SQL.T_TRM.seccionManzana")+","+
                campos.getString("SQL.T_TRM.letraManzana")+","+
                campos.getString("SQL.T_TRM.manzana")+","+
                campos.getString("SQL.T_TRM.paisSeccion")+","+
                campos.getString("SQL.T_TRM.provinciaSeccion")+","+
                campos.getString("SQL.T_TRM.municipioSeccion")+","+
                campos.getString("SQL.T_TRM.distritoSeccion")+","+
                campos.getString("SQL.T_TRM.seccion")+","+
                campos.getString("SQL.T_TRM.letraSeccion")+","+
                campos.getString("SQL.T_TRM.paisSubseccion")+","+
                campos.getString("SQL.T_TRM.provinciaSubseccion")+","+
                campos.getString("SQL.T_TRM.municipioSubseccion")+","+
                campos.getString("SQL.T_TRM.distritoSubseccion")+","+
                campos.getString("SQL.T_TRM.seccionSubseccion")+","+
                campos.getString("SQL.T_TRM.letraSubseccion")+","+
                campos.getString("SQL.T_TRM.subseccion")+","+
                campos.getString("SQL.T_TRM.codigoPostal")+","+
                campos.getString("SQL.T_TRM.primerNumero")+","+
                campos.getString("SQL.T_TRM.primeraLetra")+","+
                campos.getString("SQL.T_TRM.ultimoNumero")+","+
                campos.getString("SQL.T_TRM.ultimaLetra")+","+
                campos.getString("SQL.T_TRM.paisNUC")+","+
                campos.getString("SQL.T_TRM.provinciaNUC")+","+
                campos.getString("SQL.T_TRM.municipioNUC")+","+
                campos.getString("SQL.T_TRM.e_singularNUC")+","+
                campos.getString("SQL.T_TRM.nucleo")+
                ") SELECT " +
                campos.getString("SQL.T_TRM.tipoNumeracion")+","+
                campos.getString("SQL.T_TRM.pais")+","+
                campos.getString("SQL.T_TRM.provincia")+","+
                campos.getString("SQL.T_TRM.municipio")+","+
                gVO.getAtributo("idNuevaVia")+","+
                campos.getString("SQL.T_TRM.codigo")+","+
                campos.getString("SQL.T_TRM.paisManzana")+","+
                campos.getString("SQL.T_TRM.provinciaManzana")+","+
                campos.getString("SQL.T_TRM.municipioManzana")+","+
                campos.getString("SQL.T_TRM.distritoManzana")+","+
                campos.getString("SQL.T_TRM.seccionManzana")+","+
                campos.getString("SQL.T_TRM.letraManzana")+","+
                campos.getString("SQL.T_TRM.manzana")+","+
                campos.getString("SQL.T_TRM.paisSeccion")+","+
                campos.getString("SQL.T_TRM.provinciaSeccion")+","+
                campos.getString("SQL.T_TRM.municipioSeccion")+","+
                campos.getString("SQL.T_TRM.distritoSeccion")+","+
                campos.getString("SQL.T_TRM.seccion")+","+
                campos.getString("SQL.T_TRM.letraSeccion")+","+
                campos.getString("SQL.T_TRM.paisSubseccion")+","+
                campos.getString("SQL.T_TRM.provinciaSubseccion")+","+
                campos.getString("SQL.T_TRM.municipioSubseccion")+","+
                campos.getString("SQL.T_TRM.distritoSubseccion")+","+
                campos.getString("SQL.T_TRM.seccionSubseccion")+","+
                campos.getString("SQL.T_TRM.letraSubseccion")+","+
                campos.getString("SQL.T_TRM.subseccion")+","+
                campos.getString("SQL.T_TRM.codigoPostal")+","+
                campos.getString("SQL.T_TRM.primerNumero")+","+
                campos.getString("SQL.T_TRM.primeraLetra")+","+
                campos.getString("SQL.T_TRM.ultimoNumero")+","+
                campos.getString("SQL.T_TRM.ultimaLetra")+","+
                campos.getString("SQL.T_TRM.paisNUC")+","+
                campos.getString("SQL.T_TRM.provinciaNUC")+","+
                campos.getString("SQL.T_TRM.municipioNUC")+","+
                campos.getString("SQL.T_TRM.e_singularNUC")+","+
                campos.getString("SQL.T_TRM.nucleo")+
                " FROM T_TRM WHERE "+
                campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("idVia");
        /* Cambio combo viales *
    +" AND "+
   campos.getString("SQL.T_TRM.e_singularNUC")+"="+gVO.getAtributo("codESI")+" AND "+
   campos.getString("SQL.T_TRM.nucleo")+"="+gVO.getAtributo("codNUC");
   * Fin cambio combo viales */

        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt.executeUpdate(sql);
        stmt.close();
        // DOY DE BAJA LOS TRAMOS ANTIGUOS
        sql = "UPDATE T_TRM SET " +
                campos.getString("SQL.T_TRM.situacion")+"='B'"+
                " WHERE " +
                campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("idVia");
        /*+" AND "+
        campos.getString("SQL.T_TRM.e_singularNUC")+"="+gVO.getAtributo("codESI")+" AND "+
        campos.getString("SQL.T_TRM.nucleo")+"="+gVO.getAtributo("codNUC");
        */
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt = con.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }

    private boolean esNumeroPar(int numero){
        return ((numero%2)==0);
    }

    private boolean buscarNuevoTramo(Statement stmt,ResultSet rs,GeneralValueObject gVO)
            throws Exception {
        ResultSet rs1 = null;
        boolean encontrado = false;
        String tipoNumeracion = (String)gVO.getAtributo("codTipoNumeracion");
        String sql = "";
        sql = "SELECT T_TRM.* FROM T_TRM WHERE "+
                campos.getString("SQL.T_TRM.situacion")+"='A' AND "+
                campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("idVia");
        //+" AND "+
        //  campos.getString("SQL.T_TRM.tipoNumeracion")+"="+gVO.getAtributo("codTipoNumeracion");
        sql+=" AND " +
                campos.getString("SQL.T_TRM.e_singularNUC")+"=" + gVO.getAtributo("codESI")+ " AND " +
                campos.getString("SQL.T_TRM.nucleo")+"=" + gVO.getAtributo("codNUC");

        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        rs1 = stmt.executeQuery(sql);
        int distritoDSU = rs.getInt(campos.getString("SQL.T_TRM.distritoSeccion"));
        int seccionDSU = rs.getInt(campos.getString("SQL.T_TRM.seccion"));
        String letraDSU = rs.getString(campos.getString("SQL.T_TRM.letraSeccion"));
        if(m_Log.isDebugEnabled()) m_Log.debug("NumDesde: "+gVO.getAtributo("numDesde"));
        int numDesdeDSU = 0;
        if(!tipoNumeracion.equals("0")){
            numDesdeDSU = Integer.parseInt( (String) gVO.getAtributo("numDesde"));
        }
        String numHastaDSU1 = (String)gVO.getAtributo("numHasta");
        while(rs1.next()){
            int distrito = rs1.getInt(campos.getString("SQL.T_TRM.distritoSeccion"));
            int seccion = rs1.getInt(campos.getString("SQL.T_TRM.seccion"));
            String letra = rs1.getString(campos.getString("SQL.T_TRM.letraSeccion"));
            String codTramo = rs1.getString(campos.getString("SQL.T_TRM.codigo"));
            String tnuRs1 = rs1.getString(campos.getString("SQL.T_TRM.tipoNumeracion"));
            if(!tipoNumeracion.equals("0")){
                int numDesde = rs1.getInt(campos.getString("SQL.T_TRM.primerNumero"));
                int numHasta = rs1.getInt(campos.getString("SQL.T_TRM.ultimoNumero"));
                if (!numHastaDSU1.equals("")) {
                    int numHastaDSU = Integer.parseInt(numHastaDSU1);
                    if ( (numDesdeDSU >= numDesde) && (numDesdeDSU <= numHasta) &&
                            (numHastaDSU >= numDesde) && (numHastaDSU <= numHasta)) {
                        if(((esNumeroPar(numDesde))&&(tnuRs1.equals("2")))||
                                ((!esNumeroPar(numDesde))&&(tnuRs1.equals("1")))){
                            encontrado = true;
                            gVO.setAtributo("codTramo", codTramo);
                            gVO.setAtributo("distrito",
                                    rs.getString(campos.getString("SQL.T_TRM.distritoSeccion")));
                            gVO.setAtributo("seccion",
                                    rs.getString(campos.getString("SQL.T_TRM.seccion")));
                            gVO.setAtributo("letra",
                                    rs.getString(campos.getString("SQL.T_TRM.letraSeccion")));
                            gVO.setAtributo("haCambiadoDistrito", "NO");
                            if ( (distrito != distritoDSU) || (seccion != seccionDSU) ||
                                    (!letra.equals(letraDSU))) {
                                gVO.setAtributo("distrito",
                                        rs1.getString(campos.getString("SQL.T_TRM.distritoSeccion")));
                                gVO.setAtributo("seccion",
                                        rs1.getString(campos.getString("SQL.T_TRM.seccion")));
                                gVO.setAtributo("letra",
                                        rs1.getString(campos.getString("SQL.T_TRM.letraSeccion")));
                                gVO.setAtributo("haCambiadoDistrito", "SI");
                            }
                            gVO.setAtributo("codTipoNumeracion",
                                    rs1.getString(campos.getString("SQL.T_TRM.tipoNumeracion")));
                            break;
                        }
                    }
                }
                else {
                    if ( (numDesdeDSU >= numDesde) && (numDesdeDSU <= numHasta)) {
                        if(((esNumeroPar(numDesde))&&(tnuRs1.equals("2")))||
                                ((!esNumeroPar(numDesde))&&(tnuRs1.equals("1")))){
                            encontrado = true;
                            gVO.setAtributo("codTramo", codTramo);
                            gVO.setAtributo("distrito",
                                    rs.getString(campos.getString("SQL.T_TRM.distritoSeccion")));
                            gVO.setAtributo("seccion",
                                    rs.getString(campos.getString("SQL.T_TRM.seccion")));
                            gVO.setAtributo("letra",
                                    rs.getString(campos.getString("SQL.T_TRM.letraSeccion")));
                            gVO.setAtributo("haCambiadoDistrito", "NO");
                            if ( (distrito != distritoDSU) || (seccion != seccionDSU) ||
                                    (!letra.equals(letraDSU))) {
                                gVO.setAtributo("distrito",
                                        rs1.getString(campos.getString("SQL.T_TRM.distritoSeccion")));
                                gVO.setAtributo("seccion",
                                        rs1.getString(campos.getString("SQL.T_TRM.seccion")));
                                gVO.setAtributo("letra",
                                        rs1.getString(campos.getString("SQL.T_TRM.letraSeccion")));
                                gVO.setAtributo("haCambiadoDistrito", "SI");
                            }
                            gVO.setAtributo("codTipoNumeracion",
                                    rs1.getString(campos.getString("SQL.T_TRM.tipoNumeracion")));
                            break;
                        }
                    }
                }
            }else{
                if(tnuRs1.equals("0")){
                    gVO.setAtributo("codTramo", codTramo);
                    gVO.setAtributo("distrito",
                            rs.getString(campos.getString("SQL.T_TRM.distritoSeccion")));
                    gVO.setAtributo("seccion",
                            rs.getString(campos.getString("SQL.T_TRM.seccion")));
                    gVO.setAtributo("letra",
                            rs.getString(campos.getString("SQL.T_TRM.letraSeccion")));
                    gVO.setAtributo("haCambiadoDistrito", "NO");
                    if ( (distrito != distritoDSU) || (seccion != seccionDSU) ||
                            (!letra.equals(letraDSU))) {
                        gVO.setAtributo("distrito",
                                rs1.getString(campos.getString("SQL.T_TRM.distritoSeccion")));
                        gVO.setAtributo("seccion",
                                rs1.getString(campos.getString("SQL.T_TRM.seccion")));
                        gVO.setAtributo("letra",
                                rs1.getString(campos.getString("SQL.T_TRM.letraSeccion")));
                        gVO.setAtributo("haCambiadoDistrito", "SI");
                    }
                    encontrado = true;
                    break;
                }
            }
        }
        rs1.close();
        return encontrado;
    }

    private void actualizarDSUs(Connection con,GeneralValueObject gVO,AdaptadorSQLBD abd)
            throws Exception {
        String sql = "";
        Statement stmt = con.createStatement();
        //Statement stmt1 = con.createStatement();
        ResultSet rs = null;
        //ResultSet rs1 = null;
        //Vector dsus = new Vector();
        //Vector dpos = new Vector();
        Vector hojas = new Vector();
        String haCambiadoVial = (String)gVO.getAtributo("haCambiadoVial");
        Vector nuevasNumeraciones = (Vector)gVO.getAtributo("nuevaNumeracion");
        gVO.setAtributo("nuevasNumeraciones",nuevasNumeraciones);

        String from =  "P_HOJ.*,T_DPO.*,T_DSU.*,T_TRM.*";/*FROM P_HOJ,T_DPO,T_DSU,T_TRM*/
      ArrayList join = new ArrayList();
      
        join.add("P_HOJ");
        join.add("LEFT");
        join.add("T_DPO");
        join.add(campos.getString("SQL.T_DPO.domicilio")+"="+campos.getString("SQL.P_HOJ.domicilio"));
        join.add("INNER");
        join.add("T_DSU");
        join.add(campos.getString("SQL.T_DPO.suelo")+"="+campos.getString("SQL.T_DSU.identificador")+" AND "+
                 campos.getString("SQL.T_DSU.pais")+"=" + gVO.getAtributo("codPais")+ " AND " +
                 campos.getString("SQL.T_DSU.provincia")+"=" + gVO.getAtributo("codProvincia") + " AND " +
                 campos.getString("SQL.T_DSU.municipio")+"=" + gVO.getAtributo("codMunicipio") + " AND "+
                 campos.getString("SQL.T_DSU.vial")+"=" + gVO.getAtributo("idVia"));
        join.add("");
        join.add("T_TRM");
        join.add(campos.getString("SQL.T_DSU.paisTRM")+"=" + campos.getString("SQL.T_TRM.pais")+ " AND " +
                 campos.getString("SQL.T_DSU.provinciaTRM")+"=" + campos.getString("SQL.T_TRM.provincia") + " AND " +
                 campos.getString("SQL.T_DSU.municipioTRM")+"=" + campos.getString("SQL.T_TRM.municipio") + " AND "+
                 campos.getString("SQL.T_DSU.vialTRM")+"=" + campos.getString("SQL.T_TRM.vial")+ " AND " +
                 campos.getString("SQL.T_DSU.codigoTRM")+"=" + campos.getString("SQL.T_TRM.codigo") + " AND " +
                 campos.getString("SQL.T_DSU.tipoNumeracionTRM")+"=" + campos.getString("SQL.T_TRM.tipoNumeracion"));
        join.add("false");
        
        sql = abd.join(from,null,(String[]) join.toArray(new String[]{})) +
        " ORDER BY "+campos.getString("SQL.T_DSU.numeroDesde")+","+
                     campos.getString("SQL.T_DSU.letraDesde")+","+
                     campos.getString("SQL.T_DSU.numeroHasta")+","+
                     campos.getString("SQL.T_DSU.letraHasta");

        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        //m_Log.debug(sql);
        rs = stmt.executeQuery(sql);
        int numsLength = nuevasNumeraciones.size();
        String idDSUAnterior = "";
        while(rs.next()){
            String idDSU = rs.getString(campos.getString("SQL.T_DSU.identificador"));
            String esiAqui = rs.getString(campos.getString("SQL.T_TRM.e_singularNUC"));
            String nucAqui = rs.getString(campos.getString("SQL.T_TRM.nucleo"));
            if(m_Log.isDebugEnabled()) m_Log.debug("idDSUAnterior = "+idDSUAnterior+" idDSU = "+idDSU);
            // BUSCAMOS LA DSU EN EL VECTOR DE NUMERACIONES, SI NO LA ENCONTRAMOS
            // SE TRATA DE UNA DSU QUE SE AÑADIÓ CON POSTERIORIDAD A CUANDO REALIZAMOS
            // LA CONSULTA DE DSUs DE LA VIA
            int i = 0;
            GeneralValueObject numeracion = new GeneralValueObject();
            for(i=0;i<numsLength;i++){
                numeracion = (GeneralValueObject) nuevasNumeraciones.get(i);
                String idDSUNume = (String)numeracion.getAtributo("codDSU");
                if(idDSU.equals(idDSUNume)){
                    // HEMOS ENCONTRADO LA DSU, NOS SALIMOS DEL BUCLE
                    break;
                }
            }
            if(i==numsLength){
                // NO LO HEMOS ENCONTRADO, Y LE DAMOS VALORES A numeracion
                numeracion.setAtributo("codDSU", rs.getString(campos.getString("SQL.T_DSU.identificador")));
                numeracion.setAtributo("numDesde", rs.getString(campos.getString("SQL.T_DSU.numeroDesde")));
                numeracion.setAtributo("letraDesde", rs.getString(campos.getString("SQL.T_DSU.letraDesde")));
                numeracion.setAtributo("numHasta", rs.getString(campos.getString("SQL.T_DSU.numeroHasta")));
                numeracion.setAtributo("letraHasta", rs.getString(campos.getString("SQL.T_DSU.letraHasta")));
                // TRAMO
                numeracion.setAtributo("codTipoNumeracion",rs.getString(campos.getString("SQL.T_TRM.tipoNumeracion")));
                numeracion.setAtributo("codPais",rs.getString(campos.getString("SQL.T_TRM.pais")));
                numeracion.setAtributo("codProvincia",rs.getString(campos.getString("SQL.T_TRM.provincia")));
                numeracion.setAtributo("codMunicipio",rs.getString(campos.getString("SQL.T_TRM.municipio")));
                numeracion.setAtributo("idVia",rs.getString(campos.getString("SQL.T_TRM.vial")));
                numeracion.setAtributo("codTramo",rs.getString(campos.getString("SQL.T_TRM.codigo")));
                numeracion.setAtributo("haCambiadoNumeracion","NO");
            }
            numeracion.setAtributo("idVia",gVO.getAtributo("idNuevaVia"));
            /* cambio combo vial *
       numeracion.setAtributo("codESI",gVO.getAtributo("codESI"));
       numeracion.setAtributo("codNUC",gVO.getAtributo("codNUC"));
       * fin cambio combo vial */
            numeracion.setAtributo("codESI",esiAqui);
            numeracion.setAtributo("codNUC",nucAqui);

            if(!buscarNuevoTramo(stmt/*stmt1*/,rs,numeracion)){
                gVO.setAtributo("error", "DSU_SIN_TRAM0");
                throw new Exception("DSU_SIN_TRAM0");
            }
            String haCambiadoNumeracion = (String)numeracion.getAtributo("haCambiadoNumeracion");
            String haCambiadoDistrito = (String)numeracion.getAtributo("haCambiadoDistrito");


            if((haCambiadoVial.equals("SI"))||
                    (haCambiadoNumeracion.equals("SI"))){


                if(!idDSU.equals(idDSUAnterior)){

                    numeracion.setAtributo("usuario",gVO.getAtributo("usuario"));
                    // INSERTO LAS NUEVAS DIRECCIONES SUELO
                    insertarT_DSU(con,rs,numeracion,abd);
                    // DOY DE BAJA LA DIRECCION SUELO ANTIGUA
                    sql = "UPDATE T_DSU SET "+
                            campos.getString("SQL.T_DSU.situacion")+"='B',"+
                            campos.getString("SQL.T_DSU.fechaBaja")+"="+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
                            campos.getString("SQL.T_DSU.usuarioBaja")+"="+gVO.getAtributo("usuario")+
                            " WHERE "+
                            campos.getString("SQL.T_DSU.identificador")+"="+rs.getString(campos.getString("SQL.T_DSU.identificador"));
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    //m_Log.debug(sql);
                    stmt.executeUpdate(sql);
                    //stmt1.executeUpdate(sql);

                }

                // GUARDO LOS VALORES DE LA HOJA CORRESPONDIENTE PARA SU POSTERIOR
                // PROCESAMIENTO
                GeneralValueObject hoja = new GeneralValueObject();
                String distrito = rs.getString(campos.getString("SQL.P_HOJ.distrito"));
                String seccion = rs.getString(campos.getString("SQL.P_HOJ.seccion"));
                String letra = rs.getString(campos.getString("SQL.P_HOJ.letra"));
                String numeroHoja = rs.getString(campos.getString("SQL.P_HOJ.numero"));
                String version = rs.getString(campos.getString("SQL.P_HOJ.version"));
                String contador = rs.getString(campos.getString("SQL.P_HOJ.contador"));
                hoja.setAtributo("codDSU",numeracion.getAtributo("idNuevaDSU"));
                hoja.setAtributo("codDPO",rs.getString(campos.getString("SQL.T_DPO.domicilio")));
                hoja.setAtributo("numeracion",numeracion);
                hoja.setAtributo("haCambiadoNumeracion",haCambiadoNumeracion);
                hoja.setAtributo("haCambiadoDistrito",haCambiadoDistrito);
                hoja.setAtributo("codPais",rs.getString(campos.getString("SQL.P_HOJ.pais")));
                hoja.setAtributo("codProvincia",rs.getString(campos.getString("SQL.P_HOJ.provincia")));
                hoja.setAtributo("codMunicipio",rs.getString(campos.getString("SQL.P_HOJ.municipio")));
                hoja.setAtributo("distrito",distrito);
                hoja.setAtributo("seccion",seccion);
                hoja.setAtributo("letra",letra);
                hoja.setAtributo("hoja",numeroHoja);
                hoja.setAtributo("familia",rs.getString(campos.getString("SQL.P_HOJ.familia")));
                hoja.setAtributo("version",version);
                hoja.setAtributo("urbanizacion",rs.getString(campos.getString("SQL.P_HOJ.urbanizacion")));
                hoja.setAtributo("contador",contador);
                hoja.setAtributo("situacion",rs.getString(campos.getString("SQL.P_HOJ.situacion")));
                hojas.add(hoja);

                idDSUAnterior = idDSU;
            }
        }
        gVO.setAtributo("hojas",hojas);
        stmt.close();
        //stmt1.close();
    }

    private void insertarT_DSU(Connection con,ResultSet rs,
                               GeneralValueObject numeracion,AdaptadorSQLBD abd) throws Exception{
        AdaptadorSQLBD bd = null;
        String sql = "";
        Statement stmt1 = con.createStatement();
        ResultSet rs1 = null;
        String idDSU = rs.getString(campos.getString("SQL.T_DSU.identificador"));
        String haCambiadoNumeracion = (String)numeracion.getAtributo("haCambiadoNumeracion");
        int idNuevaDSU = 0;
        sql = "SELECT "+abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.T_DSU.identificador")})+" AS MAXIMO"+
                " FROM T_DSU";
        rs1 = stmt1.executeQuery(sql);
        if(rs1.next()){
            idNuevaDSU = rs1.getInt("MAXIMO");
        }
        rs1.close();
        idNuevaDSU++;
        // INSERTO LAS NUEVAS DIRECCIONES SUELO
        sql = "INSERT INTO T_DSU("+
                campos.getString("SQL.T_DSU.pais")+","+
                campos.getString("SQL.T_DSU.provincia")+","+
                campos.getString("SQL.T_DSU.municipio")+","+
                campos.getString("SQL.T_DSU.vial")+","+
                campos.getString("SQL.T_DSU.identificador")+","+
                campos.getString("SQL.T_DSU.tipoNumeracion")+","+
                campos.getString("SQL.T_DSU.hoja")+","+
                //campos.getString("SQL.T_DSU.manzana")+","+
                campos.getString("SQL.T_DSU.parcela")+","+
                campos.getString("SQL.T_DSU.cpoPais")+","+
                campos.getString("SQL.T_DSU.cpoProvincia")+","+
                campos.getString("SQL.T_DSU.cpoMunicipio")+","+
                campos.getString("SQL.T_DSU.codigoPostal")+","+
                campos.getString("SQL.T_DSU.distritoPais")+","+
                campos.getString("SQL.T_DSU.distritoProvincia")+","+
                campos.getString("SQL.T_DSU.distritoMunicipio")+","+
                campos.getString("SQL.T_DSU.distrito")+","+
                campos.getString("SQL.T_DSU.seccion")+","+
                campos.getString("SQL.T_DSU.letra")+","+
                //campos.getString("SQL.T_DSU.manzanaINE")+","+
                campos.getString("SQL.T_DSU.paisESI")+","+
                campos.getString("SQL.T_DSU.provinciaESI")+","+
                campos.getString("SQL.T_DSU.municipioESI")+","+
                campos.getString("SQL.T_DSU.e_singular")+","+
                campos.getString("SQL.T_DSU.numeroDesde")+","+
                campos.getString("SQL.T_DSU.letraDesde")+","+
                campos.getString("SQL.T_DSU.numeroHasta")+","+
                campos.getString("SQL.T_DSU.letraHasta")+","+
                campos.getString("SQL.T_DSU.bloque")+","+
                campos.getString("SQL.T_DSU.portal")+","+
                campos.getString("SQL.T_DSU.caserio")+","+
                campos.getString("SQL.T_DSU.origenX")+","+
                campos.getString("SQL.T_DSU.origenY")+","+
                campos.getString("SQL.T_DSU.finX")+","+
                campos.getString("SQL.T_DSU.finY")+","+
                campos.getString("SQL.T_DSU.verificacion")+","+
                campos.getString("SQL.T_DSU.kilometro")+","+
                campos.getString("SQL.T_DSU.hectometro")+","+
                campos.getString("SQL.T_DSU.imagen")+","+
                campos.getString("SQL.T_DSU.situacion")+","+
                campos.getString("SQL.T_DSU.fechaAlta")+","+
                campos.getString("SQL.T_DSU.usuarioAlta")+","+
                campos.getString("SQL.T_DSU.fechaBaja")+","+
                campos.getString("SQL.T_DSU.usuarioBaja")+","+
                campos.getString("SQL.T_DSU.fechaVigencia")+","+
                campos.getString("SQL.T_DSU.tipoNumeracionTRM")+","+
                campos.getString("SQL.T_DSU.paisTRM")+","+
                campos.getString("SQL.T_DSU.provinciaTRM")+","+
                campos.getString("SQL.T_DSU.municipioTRM")+","+
                campos.getString("SQL.T_DSU.vialTRM")+","+
                campos.getString("SQL.T_DSU.codigoTRM")+
                ") SELECT " +
                campos.getString("SQL.T_DSU.pais")+","+
                campos.getString("SQL.T_DSU.provincia")+","+
                campos.getString("SQL.T_DSU.municipio")+","+
                numeracion.getAtributo("idVia")+","+
                idNuevaDSU+","+
                numeracion.getAtributo("codTipoNumeracion")+","+
                campos.getString("SQL.T_DSU.hoja")+","+
                //        campos.getString("SQL.T_DSU.manzana")+","+
                campos.getString("SQL.T_DSU.parcela")+","+
                campos.getString("SQL.T_DSU.cpoPais")+","+
                campos.getString("SQL.T_DSU.cpoProvincia")+","+
                campos.getString("SQL.T_DSU.cpoMunicipio")+","+
                campos.getString("SQL.T_DSU.codigoPostal")+","+
                campos.getString("SQL.T_DSU.distritoPais")+","+
                campos.getString("SQL.T_DSU.distritoProvincia")+","+
                campos.getString("SQL.T_DSU.distritoMunicipio")+","+
                numeracion.getAtributo("distrito")+","+
                numeracion.getAtributo("seccion")+","+
                bd.addString((String)numeracion.getAtributo("letra"))+","+
                campos.getString("SQL.T_DSU.paisESI")+","+
                campos.getString("SQL.T_DSU.provinciaESI")+","+
                campos.getString("SQL.T_DSU.municipioESI")+","+
                campos.getString("SQL.T_DSU.e_singular")+",";
        String numeracion1=
                campos.getString("SQL.T_DSU.numeroDesde")+","+
                        campos.getString("SQL.T_DSU.letraDesde")+","+
                        campos.getString("SQL.T_DSU.numeroHasta")+","+
                        campos.getString("SQL.T_DSU.letraHasta")+",";
        if(haCambiadoNumeracion.equals("SI")){
            numeracion1= numeracion.getAtributo("numDesde")+","+
                    bd.addString((String)numeracion.getAtributo("letraDesde"))+","+
                    bd.addString((String)numeracion.getAtributo("numHasta"))+","+
                    bd.addString((String)numeracion.getAtributo("letraHasta"))+",";
        }
        sql+=numeracion1+campos.getString("SQL.T_DSU.bloque")+","+
                campos.getString("SQL.T_DSU.portal")+","+
                campos.getString("SQL.T_DSU.caserio")+","+
                campos.getString("SQL.T_DSU.origenX")+","+
                campos.getString("SQL.T_DSU.origenY")+","+
                campos.getString("SQL.T_DSU.finX")+","+
                campos.getString("SQL.T_DSU.finY")+","+
                campos.getString("SQL.T_DSU.verificacion")+","+
                campos.getString("SQL.T_DSU.kilometro")+","+
                campos.getString("SQL.T_DSU.hectometro")+","+
                campos.getString("SQL.T_DSU.imagen")+","+
                "'A',"+
                abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
                numeracion.getAtributo("usuario")+","+
                "null,"+
                "null,"+
                abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
                numeracion.getAtributo("codTipoNumeracion")+","+
                campos.getString("SQL.T_DSU.paisTRM")+","+
                campos.getString("SQL.T_DSU.provinciaTRM")+","+
                campos.getString("SQL.T_DSU.municipioTRM")+","+
                numeracion.getAtributo("idVia")+","+
                numeracion.getAtributo("codTramo")+
                " FROM T_DSU WHERE "+
                campos.getString("SQL.T_DSU.identificador")+"="+idDSU;
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        //m_Log.debug(sql);
        stmt1.executeUpdate(sql);
        numeracion.setAtributo("idNuevaDSU",String.valueOf(idNuevaDSU));
        rs1.close();
        stmt1.close();
    }


    private void actualizarDomicilios(Statement stmt,GeneralValueObject gVO,AdaptadorSQLBD abd)
            throws Exception {
        String sql = "";
        ResultSet rs = null;
        Vector hojas = (Vector) gVO.getAtributo("hojas");
        int numDSUs = hojas.size();
        int i=0;
        for(i=0;i<numDSUs;i++){
            GeneralValueObject hoja = (GeneralValueObject) hojas.get(i);
            String idNuevaDSU = (String) hoja.getAtributo("codDSU");
            String idDPO = (String) hoja.getAtributo("codDPO");
            int idNuevoDOM = 0;
            sql = "SELECT " + abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.T_DOM.idDomicilio")}) +
                    " AS MAXIMO" +
                    " FROM T_DOM";
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                idNuevoDOM = rs.getInt("MAXIMO");
            }
            rs.close();
            idNuevoDOM++;
            sql = "INSERT INTO T_DOM("+
                    campos.getString("SQL.T_DOM.idDomicilio")+","+
                    campos.getString("SQL.T_DOM.normalizado")+
                    ") VALUES ("+
                    idNuevoDOM+","+
                    "1)";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            //m_Log.debug(sql);
            stmt.executeUpdate(sql);
            // INSERTO LAS NUEVAS DIRECCIONES POSTALES
            sql = "INSERT INTO T_DPO("+
                    campos.getString("SQL.T_DPO.domicilio")+","+
                    campos.getString("SQL.T_DPO.suelo")+","+
                    campos.getString("SQL.T_DPO.escalera")+","+
                    campos.getString("SQL.T_DPO.planta")+","+
                    campos.getString("SQL.T_DPO.tipoVivienda")+","+
                    campos.getString("SQL.T_DPO.puerta")+","+
                    campos.getString("SQL.T_DPO.observaciones")+","+
                    campos.getString("SQL.T_DPO.situacion")+","+
                    campos.getString("SQL.T_DPO.fechaAlta")+","+
                    campos.getString("SQL.T_DPO.usuarioAlta")+","+
                    campos.getString("SQL.T_DPO.fechaBaja")+","+
                    campos.getString("SQL.T_DPO.usuarioBaja")+","+
                    campos.getString("SQL.T_DPO.fechaVigencia")+
                    ") SELECT " +
                    idNuevoDOM+","+
                    idNuevaDSU+","+
                    campos.getString("SQL.T_DPO.escalera")+","+
                    campos.getString("SQL.T_DPO.planta")+","+
                    campos.getString("SQL.T_DPO.tipoVivienda")+","+
                    campos.getString("SQL.T_DPO.puerta")+","+
                    campos.getString("SQL.T_DPO.observaciones")+","+
                    "'A',"+
                    abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
                    gVO.getAtributo("usuario")+","+
                    "null,"+
                    "null,"+
                    abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+
                    " FROM T_DPO WHERE "+
                    campos.getString("SQL.T_DPO.domicilio")+"="+idDPO;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            //m_Log.debug(sql);
            stmt.executeUpdate(sql);
            // ACTUALIZAR TABLAS DEPENDIENTES DE T_DPO
            sql = "UPDATE T_DOT SET " + campos.getString("SQL.T_DOT.idDomicilio")+"="+idNuevoDOM+" "
                    + " WHERE " + campos.getString("SQL.T_DOT.idDomicilio") +"=" +idDPO;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            //m_Log.debug(sql);
            stmt.executeUpdate(sql);

            // DOY DE BAJA LA DIRECCION POSTAL ANTIGUA
            sql = "UPDATE T_DPO SET "+
                    campos.getString("SQL.T_DPO.situacion")+"='B',"+
                    campos.getString("SQL.T_DPO.fechaBaja")+"="+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
                    campos.getString("SQL.T_DPO.usuarioBaja") + "=" +
                    gVO.getAtributo("usuario") +
                    " WHERE "+
                    campos.getString("SQL.T_DPO.domicilio")+"="+idDPO;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            //m_Log.debug(sql);
            stmt.executeUpdate(sql);
            hoja.setAtributo("codDPO", String.valueOf(idNuevoDOM));

        }

    }

    private boolean noInsertar(ResultSet rs,GeneralValueObject hoja) throws Exception{
        String cov = rs.getString(campos.getString("SQL.P_OPE.codigoVariacion"));
        String cav = rs.getString(campos.getString("SQL.P_OPE.causaVariacion"));
        if(!relacionada(rs,hoja)){
            rs.previous();
            if(relacionada(rs,hoja)&&cav.equals("CD")){
                rs.next();
                return false;
            }
            rs.next();
            return true;
        }else{
            if(cov.equals("B"))
                return false;
            if(cav.equals("RD")||cav.equals("CD")||cav.equals("CR")){
                return true;
            }
        }
        return false;
    }


    private boolean relacionada(ResultSet rs,GeneralValueObject hoja) throws Exception{
        String codPais        = (String)hoja.getAtributo("codPais");
        String codProvincia   = (String)hoja.getAtributo("codProvincia");
        String codMunicipio   = (String)hoja.getAtributo("codMunicipio");
        String distrito       = (String)hoja.getAtributo("distrito");
        String seccion        = (String)hoja.getAtributo("seccion");
        String letraSeccion   = (String)hoja.getAtributo("letra");
        String numHoja        = (String)hoja.getAtributo("hoja");
        String familia        = (String)hoja.getAtributo("familia");
        String version        = (String)hoja.getAtributo("version");
        // DATOS RS
        String codPaisRS        = rs.getString(campos.getString("SQL.P_OPE.pais"));
        String codProvinciaRS   = rs.getString(campos.getString("SQL.P_OPE.provincia"));
        String codMunicipioRS   = rs.getString(campos.getString("SQL.P_OPE.municipio"));
        String distritoRS       = rs.getString(campos.getString("SQL.P_OPE.distrito"));
        String seccionRS        = rs.getString(campos.getString("SQL.P_OPE.seccion"));
        String letraSeccionRS   = rs.getString(campos.getString("SQL.P_OPE.letra"));
        String numHojaRS        = rs.getString(campos.getString("SQL.P_OPE.hoja"));
        String familiaRS        = rs.getString(campos.getString("SQL.P_OPE.familia"));
        String versionRS        = rs.getString(campos.getString("SQL.P_OPE.version"));
        return ((codPais.equals(codPaisRS))&&(codProvincia.equals(codProvinciaRS))&&
                (codMunicipio.equals(codMunicipioRS))&&(distrito.equals(distritoRS))&&
                (seccion.equals(seccionRS))&&(letraSeccion.equals(letraSeccionRS))&&
                (numHoja.equals(numHojaRS))&&(familia.equals(familiaRS))&&
                (version.equals(versionRS)));
    }

    public boolean insertVia(GeneralValueObject gVO, String[] params){

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        boolean resultado = false;
        try{

            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            stmt = conexion.createStatement();
            int idNuevaVia = 0;

            sql = "SELECT " + abd.funcionSistema(abd.FUNCIONSISTEMA_NVL,new String[]{
                    abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.T_VIA.identificador")})
                    ,"0"})+ "+1 AS MAXIMO" +
                  " FROM T_VIA WHERE " + campos.getString("SQL.T_VIA.provincia")+"="+gVO.getAtributo("codProvincia")
                  + " AND " + campos.getString("SQL.T_VIA.municipio")+"="+gVO.getAtributo("codMunicipio");

            rs = stmt.executeQuery(sql);
            if(rs.next()){
                idNuevaVia = rs.getInt("MAXIMO");
            }
            rs.close();
            sql = "INSERT INTO T_VIA("+
                    campos.getString("SQL.T_VIA.pais")+","+
                    campos.getString("SQL.T_VIA.provincia")+","+
                    campos.getString("SQL.T_VIA.municipio")+","+
                    campos.getString("SQL.T_VIA.identificador")+","+
                    campos.getString("SQL.T_VIA.codVia")+","+
                    campos.getString("SQL.T_VIA.nombreVia")+","+
                    campos.getString("SQL.T_VIA.nombreCorto")+","+
                    campos.getString("SQL.T_VIA.tipo")+","+
                    campos.getString("SQL.T_VIA.situacion")+","+
                    campos.getString("SQL.T_VIA.fechaAlta")+","+
                    campos.getString("SQL.T_VIA.usuarioAlta")+","+
                    campos.getString("SQL.T_VIA.fechaBaja")+","+
                    campos.getString("SQL.T_VIA.usuarioBaja")+","+
                    campos.getString("SQL.T_VIA.fechaVigencia")+
                    ") VALUES (" +
                    gVO.getAtributo("codPais")+","+
                    gVO.getAtributo("codProvincia")+","+
                    gVO.getAtributo("codMunicipio")+","+
                    idNuevaVia+","+
                    idNuevaVia+","+
                    abd.addString((String)gVO.getAtributo("descVia"))+","+
                    abd.addString((String)gVO.getAtributo("nombreCorto"))+","+
                    gVO.getAtributo("codTipoVia")+","+
                    "'A',"+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
                    gVO.getAtributo("usuario")+","+
                    "null,null,"+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+")";

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            if (stmt.executeUpdate(sql)>0) resultado=true;
        }catch (Exception e){
            resultado=false;
            rollBackTransaction(abd,conexion,e);
        }finally{
            try {
             if (stmt!=null) stmt.close();
            commitTransaction(abd,conexion);
        }catch (Exception e){
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Excepcion e finally de ViasDAO.insertVia()");
            }
        }
        return resultado;
    }

      public HashMap getDescripcionesTiposVias(String[] params){

        if (descripcionesTiposVias==null){
            AdaptadorSQLBD abd = null;
            Connection conexion = null;
            Statement stmt = null;
            ResultSet rs = null;
            String sql = "";
            HashMap descripciones = new HashMap();
            try{
                abd = new AdaptadorSQLBD(params);
                conexion = abd.getConnection();
                stmt = conexion.createStatement();

                sql = "SELECT * FROM T_TVI";

                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs = stmt.executeQuery(sql);
                while(rs.next()){
                    descripciones.put(rs.getString(campos.getString("SQL.T_TVI.codigo")),
                                      rs.getString(campos.getString("SQL.T_TVI.tipoVia")));
                }
                rs.close();
                stmt.close();

            }catch (Exception e){
                    e.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }finally{
                try {
                    abd.devolverConexion(conexion);
                }catch (BDException e) {
                    e.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ViasDAO.getListaVias");
                }
                descripcionesTiposVias = descripciones;
            }
        }
        return descripcionesTiposVias;
  }

     public HashMap getAbreviaturasTiposVias(String[] params){

        if (abreviaturasTiposVias==null){
            AdaptadorSQLBD abd = null;
            Connection conexion = null;
            Statement stmt = null;
            ResultSet rs = null;
            String sql = "";
            HashMap descripciones = new HashMap();
            try{
                abd = new AdaptadorSQLBD(params);
                conexion = abd.getConnection();
                stmt = conexion.createStatement();

                sql = "SELECT * FROM T_TVI";

                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs = stmt.executeQuery(sql);
                while(rs.next()){
                    descripciones.put(rs.getString(campos.getString("SQL.T_TVI.codigo")),
                                      rs.getString(campos.getString("SQL.T_TVI.abreviatura")));
                }
                rs.close();
                stmt.close();

            }catch (Exception e){
                    e.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }finally{
                try {
                    abd.devolverConexion(conexion);
                }catch (BDException e) {
                    e.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ViasDAO.getListaVias");
                }
                abreviaturasTiposVias = descripciones;
            }
        }
        return abreviaturasTiposVias;
  }

    public Vector getViasByDescAndProvinciaAndMunicipioGrouped(String[] params,GeneralValueObject gVO)
            throws TechnicalException {
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        PreparedStatement ps=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            String codProvincia = (String) gVO.getAtributo("codProvincia");
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String descVia = (String) gVO.getAtributo("descVia");
 
            TransformacionAtributoSelect transformador = new TransformacionAtributoSelect(bd, con);
            String condicion = transformador.construirCondicionWhereConOperadores(
                    bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"VIA_NOM"}), descVia.trim(),false);


            String parteFromInterna = bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM,
                    new String[]{"VIA_NOM"}) + " AS VIA_NOM,  COUNT(*) AS NUM";
            String parteWhereInterna = "PRV_COD=? AND MUN_COD=? AND " + condicion;
            ArrayList join = new ArrayList();
            join.add("T_VIA");
            join.add("INNER");
            join.add("T_TVI");
            join.add("VIA_TVI=TVI_COD");
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV");
            join.add("VIA_PAI=PRV_PAI AND VIA_PRV=PRV_COD");
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN");
            join.add("VIA_PAI=MUN_PAI AND VIA_PRV=MUN_PRV AND VIA_MUN=MUN_COD AND VIA_SIT='A'");
            join.add("false");

            String sqlInterna = bd.join(parteFromInterna, parteWhereInterna, (String[]) join.toArray(new String[]{})) +
                    " GROUP BY " + bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"VIA_NOM"});

            String parteFrom = " TVI_COD, TVI_DES, VIA_COD, T_VIA.VIA_NOM,PRV_COD, PRV_NOM, MUN_COD, MUN_NOM";
            String parteWhere = "NUM>1 AND PRV_COD=? AND MUN_COD=? AND VIA_SIT='A'";
            join = new ArrayList();
            join.add("T_VIA T_VIA");
            join.add("INNER");
            join.add("T_TVI");
            join.add("VIA_TVI=TVI_COD");
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV");
            join.add("VIA_PAI=PRV_PAI AND VIA_PRV=PRV_COD");
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN");
            join.add("VIA_PAI=MUN_PAI AND VIA_PRV=MUN_PRV AND VIA_MUN=MUN_COD AND VIA_SIT='A'");
            join.add("INNER");
            join.add("(" + sqlInterna + ") a");
            join.add(bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"T_VIA.VIA_NOM"}) + "= a.VIA_NOM");
            join.add("false");

            String sql = bd.join(parteFrom, parteWhere, (String[]) join.toArray(new String[]{})) +
                    " ORDER BY T_VIA.VIA_NOM";

            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(codProvincia));
            ps.setInt(2, Integer.parseInt(codMunicipio));
            ps.setInt(3, Integer.parseInt(codProvincia));
            ps.setInt(4, Integer.parseInt(codMunicipio));

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("PRV_COD: " +  codProvincia);
                m_Log.debug("MUN_COD: " + codMunicipio);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                GeneralValueObject tupla = new GeneralValueObject();
                tupla.setAtributo("T_TVI.TVI_COD", Integer.toString(rs.getInt(1)));
                tupla.setAtributo("T_TVI.TVI_DES", rs.getString(2));
                tupla.setAtributo("T_VIA.VIA_COD", Integer.toString(rs.getInt(3)));
                tupla.setAtributo("T_VIA.VIA_NOM", rs.getString(4));
                tupla.setAtributo("T_PRV.PRV_COD", Integer.toString(rs.getInt(5)));
                tupla.setAtributo("T_PRV.PRV_NOM", rs.getString(6));
                tupla.setAtributo("T_MUN.MUN_COD", Integer.toString(rs.getInt(7)));
                tupla.setAtributo("T_MUN.MUN_NOM", rs.getString(8));
                resultado.add(tupla);
            }
            rs.close();
            ps.close();
        }catch (Exception e){
            try{
                e.printStackTrace();
                bd.devolverConexion(con);
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }catch (Exception ex){
                ex.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            }
        }finally{
            try{
                bd.devolverConexion(con);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
        return resultado;
    }

    public Vector getViasByDescAndProvinciaAndMunicipio(String[] params,GeneralValueObject gVO)
            throws TechnicalException {
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        PreparedStatement ps=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            String codProvincia = (String) gVO.getAtributo("codProvincia");
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String descVia = (String) gVO.getAtributo("descVia");

            String parteFrom = "TVI_COD, TVI_DES, VIA_COD, VIA_NOM, VIA_NOC, PRV_PAI,PRV_COD, PRV_NOM, MUN_COD, MUN_NOM";
            TransformacionAtributoSelect transformador = new TransformacionAtributoSelect(bd, con);
            String condicion = transformador.construirCondicionWhereConOperadores(
                    bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"VIA_NOM"}), descVia.trim(),false);
            String parteWhere =  condicion + " AND PRV_COD=? AND MUN_COD=? AND VIA_SIT='A'";
            ArrayList join = new ArrayList();
            join.add("T_VIA");
            join.add("INNER");
            join.add("T_TVI");
            join.add("VIA_TVI=TVI_COD");
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV");
            join.add("VIA_PAI=PRV_PAI AND VIA_PRV=PRV_COD");
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN");
            join.add("VIA_PAI=MUN_PAI AND VIA_PRV=MUN_PRV AND VIA_MUN=MUN_COD");
            join.add("false");

            String sql = bd.join(parteFrom, parteWhere, (String[]) join.toArray(new String[]{}));

            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, Integer.parseInt(codProvincia));
            ps.setInt(i++, Integer.parseInt(codMunicipio));

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("VIA_NOM: " + descVia);
                m_Log.debug("PRV_COD: " +  codProvincia);
                m_Log.debug("MUN_COD: " + codMunicipio);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                GeneralValueObject tupla = new GeneralValueObject();
                tupla.setAtributo("T_TVI.TVI_COD", Integer.toString(rs.getInt(1)));
                tupla.setAtributo("T_TVI.TVI_DES", rs.getString(2));
                tupla.setAtributo("T_VIA.VIA_COD", Integer.toString(rs.getInt(3)));
                tupla.setAtributo("T_VIA.VIA_NOM", rs.getString(4));
                tupla.setAtributo("T_VIA.VIA_NOC", rs.getString(5));
                tupla.setAtributo("T_PRV.PRV_PAI", Integer.toString(rs.getInt(6)));
                tupla.setAtributo("T_PRV.PRV_COD", Integer.toString(rs.getInt(7)));
                tupla.setAtributo("T_PRV.PRV_NOM", rs.getString(8));
                tupla.setAtributo("T_MUN.MUN_COD", Integer.toString(rs.getInt(9)));
                tupla.setAtributo("T_MUN.MUN_NOM", rs.getString(10));
                resultado.add(tupla);
            }
            rs.close();
            ps.close();
        }catch (Exception e){
            try{
                e.printStackTrace();
                bd.devolverConexion(con);
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }catch (Exception ex){
                ex.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            }
        }finally{
            try{
                bd.devolverConexion(con);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
        return resultado;
    }

    public void depurarVias(String[] params, GeneralValueObject gVO, String[] codigosVias) throws BDException {
        AdaptadorSQLBD bd = null;
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs = null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            String codPais = (String) gVO.getAtributo("codPais");
            String codProvincia = (String) gVO.getAtributo("codProvincia");
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codVia = (String) gVO.getAtributo("codVia");

            String vias = "";
            for (int i=0;i<codigosVias.length-1;i++) {
                vias = vias + ", ?";
            }
            vias = vias + ")";

            String sql = "UPDATE P_FDD SET FDD_VIA=? WHERE FDD_PAI=? AND FDD_PRV=? AND FDD_MUN=? AND FDD_VIA IN (?" + vias;
            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(codVia));
            ps.setInt(2, Integer.parseInt(codPais));
            ps.setInt(3, Integer.parseInt(codProvincia));
            ps.setInt(4, Integer.parseInt(codMunicipio));
            for (int i=0;i<codigosVias.length;i++) {
                ps.setInt(i+5, Integer.parseInt(codigosVias[i]));
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("FDD_VIA: " + codVia);
                m_Log.debug("FDD_PAI: " + codPais);
                m_Log.debug("FDD_PRV: " +  codProvincia);
                m_Log.debug("FDD_MUN: " + codMunicipio);
                String viasDebug = "";
                for (int i=0;i<codigosVias.length;i++) {
                    viasDebug = viasDebug + ",";
                }
                m_Log.debug("FDD_VIA: " + viasDebug.substring(0, viasDebug.length()-1));
            }
            ps.executeUpdate();
            ps.close();

            sql = "UPDATE T_CVE SET CVE_VIA=? WHERE CVE_PAI=? AND CVE_PRV=? AND CVE_MUN=? AND CVE_VIA IN (?" + vias;
            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(codVia));
            ps.setInt(2, Integer.parseInt(codPais));
            ps.setInt(3, Integer.parseInt(codProvincia));
            ps.setInt(4, Integer.parseInt(codMunicipio));
            for (int i=0;i<codigosVias.length;i++) {
                ps.setInt(i+5, Integer.parseInt(codigosVias[i]));
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("CVE_VIA: " + codVia);
                m_Log.debug("CVE_PAI: " + codPais);
                m_Log.debug("CVE_PRV: " +  codProvincia);
                m_Log.debug("CVE_MUN: " + codMunicipio);
                String viasDebug = "";
                for (int i=0;i<codigosVias.length;i++) {
                    viasDebug = viasDebug + ",";
                }
                m_Log.debug("CVE_VIA: " + viasDebug.substring(0, viasDebug.length()-1));
            }
            ps.executeUpdate();
            ps.close();

            sql = "SELECT DNN_DOM FROM T_DNN WHERE DNN_PAI=? AND DNN_PRV=? AND DNN_MUN=? AND DNN_VIA IN (?" + vias;
            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(codPais));
            ps.setInt(2, Integer.parseInt(codProvincia));
            ps.setInt(3, Integer.parseInt(codMunicipio));
            for (int i=0;i<codigosVias.length;i++) {
                ps.setInt(i+4, Integer.parseInt(codigosVias[i]));
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("DNN_PAI: " + codPais);
                m_Log.debug("DNN_PRV: " +  codProvincia);
                m_Log.debug("DNN_MUN: " + codMunicipio);
                String viasDebug = "";
                for (int i=0;i<codigosVias.length;i++) {
                    viasDebug = viasDebug + ",";
                }
                m_Log.debug("DNN_VIA IN " + viasDebug.substring(0, viasDebug.length()-1));
            }
            rs = ps.executeQuery();
            Vector cod_dom_modificados = new Vector();
            while (rs.next()) {
                cod_dom_modificados.add(rs.getString(1));
                m_Log.debug("DNN_DOM modificado " + rs.getString(1));
            }
            ps.close();
            rs.close();

            sql = "SELECT VIA_TVI FROM T_VIA WHERE VIA_COD=? AND VIA_PAI=? AND VIA_PRV=? AND VIA_MUN=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(codVia));
            ps.setInt(2, Integer.parseInt(codPais));
            ps.setInt(3, Integer.parseInt(codProvincia));
            ps.setInt(4, Integer.parseInt(codMunicipio));
            rs = ps.executeQuery();
            String via_tvi = "";
            while (rs.next()) {
               via_tvi = rs.getString("VIA_TVI");
            }
            ps.close();
            rs.close();

            sql = "UPDATE T_DNN SET DNN_TVI=?,DNN_VIA=? WHERE DNN_PAI=? AND DNN_PRV=? AND DNN_MUN=? AND DNN_VIA IN (?" + vias;
            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(via_tvi));
            ps.setInt(2, Integer.parseInt(codVia));
            ps.setInt(3, Integer.parseInt(codPais));
            ps.setInt(4, Integer.parseInt(codProvincia));
            ps.setInt(5, Integer.parseInt(codMunicipio));
            for (int i=0;i<codigosVias.length;i++) {
                ps.setInt(i+6, Integer.parseInt(codigosVias[i]));
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("DNN_VIA: " + codVia);
                m_Log.debug("DNN_PAI: " + codPais);
                m_Log.debug("DNN_PRV: " +  codProvincia);
                m_Log.debug("DNN_MUN: " + codMunicipio);
                String viasDebug = "";
                for (int i=0;i<codigosVias.length;i++) {
                    viasDebug = viasDebug + ",";
                }
                m_Log.debug("DNN_VIA: " + viasDebug.substring(0, viasDebug.length()-1));
            }
            ps.executeUpdate();
            ps.close();

            DomicilioSimpleValueObject domicilioSimpleVO;
            String codDomicilio,localizacion;
            rs.close();

            for (int i=0;i<cod_dom_modificados.size();i++) {

                codDomicilio = (String)cod_dom_modificados.get(i);
                String from="DNN_TVI, TVI_DES, DNN_VIA, VIA_NOM, DNN_NUD, DNN_LED, DNN_NUH, DNN_LEH, DNN_BLQ," +
                		"DNN_POR, DNN_ESC, DNN_PLT, DNN_PTA, DNN_DMC, ECO_COD, ECO_NOM, ESI_COD, ESI_NOM";
                String where="DNN_DOM=? AND DNN_PAI=MUN_PAI AND DNN_PRV=MUN_PRV AND DNN_MUN=MUN_COD AND " +
                		"MUN_PAI=PAI_COD AND MUN_PAI=PRV_PAI AND MUN_PRV=PRV_COD";
                
                String []join= new String [14];
                join[0]=GlobalNames.ESQUEMA_GENERICO+"T_PAI T_PAI,"+GlobalNames.ESQUEMA_GENERICO+"T_PRV T_PRV,"+
                		GlobalNames.ESQUEMA_GENERICO+"T_MUN T_MUN,T_DNN";
                join[1]="LEFT";
                join[2]="T_VIA";
                join[3]="DNN_VIA=VIA_COD AND DNN_VPA=VIA_PAI AND DNN_VPR=VIA_PRV AND DNN_MUN=VIA_MUN";
                join[4]="LEFT";
                join[5]="T_ESI";
                join[6]="DNN_SPA=ESI_PAI AND DNN_SPR=ESI_PRV AND DNN_SMU=ESI_MUN AND DNN_ESI=ESI_COD";
                join[7]="LEFT";
                join[8]="T_ECO";
                join[9]="ESI_ECO=ECO_COD AND ESI_PAI=ECO_PAI AND ESI_PRV=ECO_PRV AND ESI_MUN=ECO_MUN";
                join[10]="LEFT";
                join[11]="T_TVI";
                join[12]="DNN_TVI=TVI_COD";
                join[13]="false";     
                
                sql=bd.join(from, where, join);
                ps=con.prepareStatement (sql);
                ps.setInt(1, Integer.parseInt(codDomicilio));
                rs=ps.executeQuery();
              
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("DNN_DOM: " + codDomicilio);

                domicilioSimpleVO = new DomicilioSimpleValueObject();
                while (rs.next()) {
                    domicilioSimpleVO.setIdTipoVia(rs.getString(1));
                    domicilioSimpleVO.setTipoVia(rs.getString(2));
                    domicilioSimpleVO.setCodigoVia(rs.getString(3));
                    domicilioSimpleVO.setDescVia(rs.getString(4));
                    domicilioSimpleVO.setNumDesde(rs.getString(5));
                    domicilioSimpleVO.setLetraDesde(rs.getString(6));
                    domicilioSimpleVO.setNumHasta(rs.getString(7));
                    domicilioSimpleVO.setLetraHasta(rs.getString(8));
                    domicilioSimpleVO.setBloque(rs.getString(9));
                    domicilioSimpleVO.setPortal(rs.getString(10));
                    domicilioSimpleVO.setEscalera(rs.getString(11));
                    domicilioSimpleVO.setPlanta(rs.getString(12));
                    domicilioSimpleVO.setPuerta(rs.getString(13));
                    domicilioSimpleVO.setDomicilio(rs.getString(14));
                    domicilioSimpleVO.setCodECO(rs.getString(15));
                    domicilioSimpleVO.setDescECO(rs.getString(16));
                    domicilioSimpleVO.setCodESI(rs.getString(17));
                    domicilioSimpleVO.setDescESI(rs.getString(18));
                }
                ps.close();
                rs.close();

                localizacion = "";

                if (domicilioSimpleVO.getTipoVia()!=null && !(domicilioSimpleVO.getTipoVia().equals("")) &&
                    !(domicilioSimpleVO.getIdTipoVia().equals(m_Common.getString("T_TVI.CodigoSinTipoVia")))) {
                    localizacion = localizacion.concat(domicilioSimpleVO.getTipoVia()).concat(" ");
                }
                if (domicilioSimpleVO.getDescVia()!=null && !(domicilioSimpleVO.getDescVia().equals(""))) {
                    localizacion = localizacion.concat(domicilioSimpleVO.getDescVia());
                }
                localizacion = localizacion.concat(separadorComaDomicilio(localizacion));
                if (domicilioSimpleVO.getNumDesde()!=null && !(domicilioSimpleVO.getNumDesde().equals(""))) {
                    localizacion = localizacion.concat(domicilioSimpleVO.getNumDesde()).concat(" ");
                }
                if (domicilioSimpleVO.getLetraDesde()!=null && !(domicilioSimpleVO.getLetraDesde().equals(""))) {
                    localizacion = localizacion.concat(domicilioSimpleVO.getLetraDesde());
                }
                if (domicilioSimpleVO.getNumHasta()!=null && !(domicilioSimpleVO.getNumHasta().equals(""))) {
                    localizacion = localizacion.concat("-").concat(domicilioSimpleVO.getNumHasta()).concat(" ");
                }
                if (domicilioSimpleVO.getLetraHasta()!=null && !(domicilioSimpleVO.getLetraHasta().equals(""))) {
                    localizacion = localizacion.concat(domicilioSimpleVO.getLetraHasta());
                }
                if (domicilioSimpleVO.getBloque()!=null && !(domicilioSimpleVO.getBloque().equals(""))) {
                    localizacion = localizacion.concat(" BLq. ").concat(domicilioSimpleVO.getBloque());
                }
                if (domicilioSimpleVO.getPortal()!=null && !(domicilioSimpleVO.getPortal().equals(""))) {
                    localizacion = localizacion.concat(" Port. ").concat(domicilioSimpleVO.getPortal());
                }
                if (domicilioSimpleVO.getEscalera()!=null && !(domicilioSimpleVO.getEscalera().equals(""))) {
                    localizacion = localizacion.concat(" Esc. ").concat(domicilioSimpleVO.getEscalera());
                }
                if (domicilioSimpleVO.getPlanta()!=null && !(domicilioSimpleVO.getPlanta().equals(""))) {
                    localizacion = localizacion.concat(" Plta. ").concat(domicilioSimpleVO.getPlanta());
                }
                if (domicilioSimpleVO.getPuerta()!=null && !(domicilioSimpleVO.getPuerta().equals(""))) {
                    localizacion = localizacion.concat(" Pta. ").concat(domicilioSimpleVO.getPuerta());
                }
                if (domicilioSimpleVO.getDomicilio()!=null && !(domicilioSimpleVO.getDomicilio().equals(""))) {
                    localizacion = localizacion.concat(separadorComaDomicilio(localizacion));
                    localizacion = localizacion.concat(domicilioSimpleVO.getDomicilio());
                }
                if (domicilioSimpleVO.getDescESI()!=null && !(domicilioSimpleVO.getDescESI().equals(""))) {
                    localizacion = localizacion.concat(separadorComaDomicilio(localizacion));
                    localizacion = localizacion.concat(domicilioSimpleVO.getDescESI());
                }
                if (domicilioSimpleVO.getDescECO()!=null && !(domicilioSimpleVO.getDescECO().equals(""))) {
                    localizacion = localizacion.concat(separadorComaDomicilio(localizacion));
                    localizacion = localizacion.concat(domicilioSimpleVO.getDescECO());
                }
                m_Log.debug("---------------------> LOCALIZACION: "+localizacion);
                sql = "UPDATE E_EXP SET EXP_LOC=? WHERE EXP_CLO=?";
                ps = con.prepareStatement(sql);
                ps.setString(1, localizacion);
                ps.setInt(2, Integer.parseInt(codDomicilio));
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("EXP_LOC: " + localizacion);
                m_Log.debug("EXP_CLO: " + codDomicilio);
                ps.executeUpdate();
                ps.close();
            }

            sql = "UPDATE T_DSU SET DSU_VIA=? WHERE DSU_PAI=? AND DSU_PRV=? AND DSU_MUN=? AND DSU_VIA IN (?" + vias;
            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(codVia));
            ps.setInt(2, Integer.parseInt(codPais));
            ps.setInt(3, Integer.parseInt(codProvincia));
            ps.setInt(4, Integer.parseInt(codMunicipio));
            for (int i=0;i<codigosVias.length;i++) {
                ps.setInt(i+5, Integer.parseInt(codigosVias[i]));
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("DSU_VIA: " + codVia);
                m_Log.debug("DSU_PAI: " + codPais);
                m_Log.debug("DSU_PRV: " +  codProvincia);
                m_Log.debug("DSU_MUN: " + codMunicipio);
                String viasDebug = "";
                for (int i=0;i<codigosVias.length;i++) {
                    viasDebug = viasDebug + ",";
                }
                m_Log.debug("DSU_VIA: " + viasDebug.substring(0, viasDebug.length()-1));
            }
            ps.executeUpdate();
            ps.close();

            sql = "DELETE FROM T_TRM WHERE TRM_PAI=? AND TRM_PRV=? AND TRM_MUN=? AND TRM_VIA IN (?" + vias;
            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(codPais));
            ps.setInt(2, Integer.parseInt(codProvincia));
            ps.setInt(3, Integer.parseInt(codMunicipio));
            for (int i=0;i<codigosVias.length;i++) {
                ps.setInt(i+4, Integer.parseInt(codigosVias[i]));
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("TRM_PAI: " + codPais);
                m_Log.debug("TRM_PRV: " +  codProvincia);
                m_Log.debug("TRM_MUN: " + codMunicipio);
                String viasDebug = "";
                for (int i=0;i<codigosVias.length;i++) {
                    viasDebug = viasDebug + ",";
                }
                m_Log.debug("TRM_VIA: " + viasDebug.substring(0, viasDebug.length()-1));
            }
            ps.executeUpdate();
            ps.close();

            sql = "DELETE FROM T_VIA WHERE VIA_PAI=? AND VIA_PRV=? AND VIA_MUN=? AND VIA_COD IN (?" + vias;
            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(codPais));
            ps.setInt(2, Integer.parseInt(codProvincia));
            ps.setInt(3, Integer.parseInt(codMunicipio));
            for (int i=0;i<codigosVias.length;i++) {
                ps.setInt(i+4, Integer.parseInt(codigosVias[i]));
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("VIA_PAI: " + codPais);
                m_Log.debug("VIA_PRV: " +  codProvincia);
                m_Log.debug("VIA_MUN: " + codMunicipio);
                String viasDebug = "";
                for (int i=0;i<codigosVias.length;i++) {
                    viasDebug = viasDebug + ",";
                }
                m_Log.debug("VIA_COD: " + viasDebug.substring(0, viasDebug.length()-1));
            }
            ps.executeUpdate();
            ps.close();

            bd.finTransaccion(con);
        }catch (Exception e){
            bd.rollBack(con);
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new BDException(e.getMessage());
        }finally{
            try{
                bd.devolverConexion(con);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
    }

    private void rollBackTransaction(AdaptadorSQLBD bd,Connection con,Exception e){
        try {
            bd.rollBack(con);
        }catch (Exception e1) {
            e1.printStackTrace();
        }finally {
            e.printStackTrace();
            m_Log.error(e.getMessage());
        }
    }

    private void commitTransaction(AdaptadorSQLBD bd,Connection con){
        try{
            bd.finTransaccion(con);
            bd.devolverConexion(con);
        }catch (Exception ex) {
            ex.printStackTrace();
            m_Log.error(ex.getMessage());
        }
    }

   private String convertirMAYUS(String valor)
    {
        if (("".equals(valor)) || (valor == null))
            return valor;
        else
            return valor.toUpperCase();
    }

    private String separadorComaDomicilio(String localizacion){
        int longitud = localizacion.length();
        if (longitud>=2){
            if (localizacion.substring(longitud-2,longitud).equals(", "))
                return "";
            else return ", ";
        } else {
            return "";
        }
    }



    /**********************************************/


   /** 
     * Se encarga de insertar una via en la base de datos, pero  antes comprueba que no existe.
     * Devuelve como resultado el codigo de la via
     * @param gVO: Objeto que contiene los datos de la vía a dar de alta
     * @param abd: objeto de la clase AdaptadorSQLBD necesario para generar la consulta SQL
     * @param con: Conexión a la BD ya inicializada
     * @return el valor del codigo de la via
     */
    public int altaViaNoRepetido(GeneralValueObject gVO, AdaptadorSQLBD abd,Connection con) throws TechnicalException{
        
        
        if (existeVia(gVO,abd,con)) {
            return Integer.parseInt((String) gVO.getAtributo("identificacion"));
        } else {
			int idNuevaVia = 0;
            Statement stmt = null;
			ResultSet rs = null;
			try {
                stmt = con.createStatement();
                String sql = "SELECT "+abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.T_VIA.identificador")})+" AS MAXIMO"+
                        " FROM T_VIA";
                rs = stmt.executeQuery(sql);
                if(rs.next()){
                    idNuevaVia = rs.getInt("MAXIMO")+1;
                }
                rs.close();
                sql = "INSERT INTO T_VIA("+
                        campos.getString("SQL.T_VIA.pais")+","+
                        campos.getString("SQL.T_VIA.provincia")+","+
                        campos.getString("SQL.T_VIA.municipio")+","+
                        campos.getString("SQL.T_VIA.identificador")+","+
                        campos.getString("SQL.T_VIA.codVia")+","+
                        campos.getString("SQL.T_VIA.nombreVia")+","+
                        campos.getString("SQL.T_VIA.nombreCorto")+","+
                        campos.getString("SQL.T_VIA.tipo")+","+
                        campos.getString("SQL.T_VIA.situacion")+","+
                        campos.getString("SQL.T_VIA.fechaAlta")+","+
                        campos.getString("SQL.T_VIA.usuarioAlta")+","+
                        campos.getString("SQL.T_VIA.fechaBaja")+","+
                        campos.getString("SQL.T_VIA.usuarioBaja")+","+
                        campos.getString("SQL.T_VIA.fechaVigencia")+
                        ") VALUES (" +
                        gVO.getAtributo("codPais")+","+
                        gVO.getAtributo("codProvincia")+","+
                        gVO.getAtributo("codMunicipio")+","+
                        idNuevaVia+","+idNuevaVia+","+
                        abd.addString((String)gVO.getAtributo("descVia"))+","+
                        abd.addString((String)gVO.getAtributo("nombreCorto"))+","+
                        gVO.getAtributo("codTipoVia")+","+
                        "'A',"+abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+","+
                        gVO.getAtributo("usuario")+","+
                        "null,null,"+abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
                
            } catch (Exception e) {
                throw new TechnicalException("Error al dar de alta una vía no repetida: " + e.getMessage());
            } finally {
                cerrarStatement(stmt);
                cerrarResultSet(rs);
            }
            
            return idNuevaVia;
        }
    }


   /**
     * Se encarga de comprobar si existe una via dada en la base de datos.
     * Consideramos que una via existe en al base de datos si tienen los mismos datos
     * en: Pais, Provincia,Municipio, descripcion via, codigo tipo via y situacion = A
     * @param gVO: Objeto con los datos de la vía
     * @param abd: AdaptadorSQLBD
     * @param conexion: Conexión a la BD
     * @return True si existe la vía y false en caso contrario
     */
    public boolean existeVia(GeneralValueObject gVO,AdaptadorSQLBD abd,Connection conexion) throws TechnicalException
    {
        boolean resultado = false;        
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";

        try{            
            stmt = conexion.createStatement();
            sql = "SELECT "+ campos.getString("SQL.T_VIA.identificador")+" AS ID FROM T_VIA WHERE "+
                    campos.getString("SQL.T_VIA.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_VIA.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_VIA.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_VIA.nombreVia")+"="+abd.addString((String)gVO.getAtributo("descVia"))+" AND "+
                    campos.getString("SQL.T_VIA.tipo")+"="+gVO.getAtributo("codTipoVia")+" AND "+
                    campos.getString("SQL.T_VIA.situacion")+"= 'A'";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            rs = stmt.executeQuery(sql);
            resultado = rs.next();
            if(resultado) gVO.setAtributo("identificacion",rs.getString("ID"));
            
        }catch (Exception e){            
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new TechnicalException("Error al comprobar si existe la vía: " + e.getMessage());
        }finally{
            cerrarStatement(stmt);
            cerrarResultSet(rs);            
        }        
        return resultado;
    }


    /**
     * Busca una vía por su pais, provincia, municipio, tipo (numerico) y nombre.
     * @param codPais
     * @param codProvincia
     * @param codMunicipio
     * @param tipoVia
     * @param nombreVia
     * @param con
     * @return Si existe la vía devuelve su código, en otro caso devuelve -1.
     * @throws java.sql.SQLException
     */
    public int buscarVia(int codPais, int codProvincia, int codMunicipio, int tipoVia, String nombreVia, Connection con) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        int codigoVia = -1;
        if (m_Log.isDebugEnabled()) m_Log.debug("ViaDAO.buscarVia");
        
        try {

          // Comprobamos si existe
          sql = "SELECT VIA_COD FROM T_VIA " +
                "WHERE (VIA_NOM LIKE ? OR VIA_NOC LIKE ?) " +
                "AND VIA_PAI = " + codPais +
                " AND VIA_PRV = " + codProvincia +
                " AND VIA_MUN = " + codMunicipio +
                " AND VIA_SIT = 'A'";

          if (tipoVia != -1){
              sql = sql + "AND VIA_TVI = " + tipoVia;
          }
          if (m_Log.isDebugEnabled()) m_Log.debug(sql);

          ps = con.prepareStatement(sql);
          int i = 1;
          ps.setString(i++, nombreVia.toUpperCase());
          ps.setString(i++, nombreVia.toUpperCase());

          rs = ps.executeQuery();
          if (rs.next()) {
              codigoVia = rs.getInt("VIA_COD");
          }
          rs.close();
          ps.close();
          
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }

        return codigoVia;
    }
    
    
        /**
     * Da de alta una via.
     * @param codPais
     * @param codProvincia
     * @param codMunicipio
     * @param tipoVia
     * @param nombreVia
     * @param con
     * @return El código de la nueva vía.
     * @throws java.sql.SQLException
     */
    public int altaVia(int codPais, int codProvincia, int codMunicipio, int tipoVia, String nombreVia, String usuario, Connection con) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        int nuevoCodigoVia = 1;
        if (m_Log.isDebugEnabled()) m_Log.debug("ViaDAO.altaVia");

        try {
            // Primero obtenemos el nuevo codigo de via.
            sql = "SELECT MAX(VIA_COD) FROM T_VIA " +
                  "WHERE VIA_PAI = " + codPais +
                  " AND VIA_PRV = " + codProvincia +
                  " AND VIA_MUN = " + codMunicipio;

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                nuevoCodigoVia = rs.getInt(1) + 1;
            }
            rs.close();
            ps.close();

            // Una vez obtenido el nuevo codigo de via, insertamos.
            String nombreCortoVia = (nombreVia.length() > 25) ?
                                    nombreVia.substring(0, 25) :
                                    nombreVia;

            int usuarioAlta = Integer.parseInt(usuario);
            sql = "INSERT INTO T_VIA (VIA_PAI, VIA_PRV, VIA_MUN, VIA_COD, VIA_CIN, VIA_NOM, " +
                      "VIA_NOC, VIA_TVI, VIA_SIT, VIA_FAL, VIA_UAL, VIA_FBJ, VIA_UBJ, VIA_FIV" +
                    ") VALUES (" +
                    codPais + ", " + codProvincia + ", " + codMunicipio + ", " +
                    nuevoCodigoVia + ", " + nuevoCodigoVia + ", ?, ?, " + tipoVia + ", " +
                    "'A', ?, " +usuarioAlta + ", ?, ?, ?)";

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps = con.prepareStatement(sql);
            int i = 1;
            Timestamp ahora = new Timestamp(System.currentTimeMillis());
            ps.setString(i++, nombreVia.toUpperCase()); // VIA_NOM
            ps.setString(i++, nombreCortoVia.toUpperCase()); // VIA_NOC
            ps.setTimestamp(i++, ahora); // VIA_FAL
            ps.setNull(i++, Types.DATE); // VIA_FBJ
            ps.setNull(i++, Types.INTEGER); // VIA_UBJ
            ps.setTimestamp(i++, ahora); // VIA_FIV

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }

        return nuevoCodigoVia;
    }
    
}