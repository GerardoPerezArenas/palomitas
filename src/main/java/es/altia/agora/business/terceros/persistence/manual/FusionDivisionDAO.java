// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.technical.Fecha;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.conexion.AdaptadorSQL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import java.util.ArrayList;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase FusionDivisionDAO </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @version 1.0
 */

public class FusionDivisionDAO  {
    private static FusionDivisionDAO instance = null;
    protected static Config campos; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log =
            LogFactory.getLog(FusionDivisionDAO.class.getName());

    protected FusionDivisionDAO() {
        super();
        // Queremos usar el fichero de configuracion techserver
        campos = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }

    public static FusionDivisionDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread)
            // Las invocaciones de este metodo
            synchronized (FusionDivisionDAO.class) {
                if (instance == null) {
                    instance = new FusionDivisionDAO();
                }
            }
        }
        return instance;
    }

    private String buscarSiIniciado(String codProceso,Connection con)
            throws Exception{
        String iniciado = "NO";
        Statement stmt = con.createStatement();
        ResultSet rs = null;
        String sql = "";
        sql = "SELECT P_FDS.* FROM P_FDS WHERE "+
                campos.getString("SQL.P_FDS.codigo")+"="+codProceso+" AND "+
                "("+campos.getString("SQL.P_FDS.codigo")+" IN (SELECT OPE_FDS FROM P_OPE))";
        //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        rs = stmt.executeQuery(sql);
        if(rs.next()){
            iniciado = "SI";
        }
        rs.close();
        stmt.close();
        return iniciado;
    }

    public Vector getListaProcesos(String[] params,GeneralValueObject paramsVO){
        Vector resultado = new Vector();
        String sql = "";
        AdaptadorSQLBD bd = null;
        Connection con = null;
        ResultSet rs = null;
        Statement state = null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
//      bd.inicioTransaccion(con);
            state = con.createStatement();
            String pais = (String)paramsVO.getAtributo("codPais");
            String provincia = (String)paramsVO.getAtributo("codProvincia");
            String municipio = (String)paramsVO.getAtributo("codMunicipio");
            String tipoOperacion = (String)paramsVO.getAtributo("tipoOperacion");
            String fechaDesde = (String)paramsVO.getAtributo("fechaDesde");
            String fechaHasta = (String)paramsVO.getAtributo("fechaHasta");
            String usuario = (String)paramsVO.getAtributo("usuario");
            String estado = (String)paramsVO.getAtributo("estado");
            sql = "SELECT P_FDS.*,A_USU.*,"+
                    bd.convertir(campos.getString("SQL.P_FDS.fechaProceso"),bd.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS FECHA,"+
                    "DISTRITOOR."+campos.getString("SQL.T_DIS.codigo")+" AS CODDISOR,"+
                    "DISTRITOOR."+campos.getString("SQL.T_DIS.nOficial")+" AS NOMDISOR,"+
                    "SECCIONOR."+campos.getString("SQL.T_SEC.codigo")+" AS CODSECOR,"+
                    "SECCIONOR."+campos.getString("SQL.T_SEC.letra")+" AS LETSECOR,"+
                    "SECCIONOR."+campos.getString("SQL.T_SEC.nOficial")+" AS NOMSECOR,"+
                    "DISTRITODE."+campos.getString("SQL.T_DIS.codigo")+" AS CODDISDE,"+
                    "DISTRITODE."+campos.getString("SQL.T_DIS.nOficial")+" AS NOMDISDE,"+
                    "SECCIONDE."+campos.getString("SQL.T_SEC.codigo")+" AS CODSECDE,"+
                    "SECCIONDE."+campos.getString("SQL.T_SEC.letra")+" AS LETSECDE,"+
                    "SECCIONDE."+campos.getString("SQL.T_SEC.nOficial")+" AS NOMSECDE "+
                    "FROM P_FDS,"+GlobalNames.ESQUEMA_GENERICO+"A_USU A_USU,T_DIS DISTRITOOR,T_DIS DISTRITODE,T_SEC SECCIONOR,"+
                    "T_SEC SECCIONDE WHERE "+
                    campos.getString("SQL.P_FDS.tipoOperacion")+"='"+tipoOperacion+"'";
            String fecha = campos.getString("SQL.P_FDS.fechaProceso");
            if(!"".equals(fechaDesde)&&!"".equals(fechaHasta)){
                fechaDesde = bd.convertir(fechaDesde, AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY");
                fechaHasta = bd.convertir(fechaHasta,AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY");
                sql += " AND ("+fecha+" BETWEEN "+fechaDesde+" AND "+fechaHasta+")";
            }else if(!"".equals(fechaDesde)&&"".equals(fechaHasta)){
                fechaDesde = bd.convertir(fechaDesde,AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY");
                sql += " AND "+fecha+" >= "+fechaDesde;
            }else if("".equals(fechaDesde)&&!"".equals(fechaHasta)){
                fechaHasta = bd.convertir(fechaHasta,AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY");
                sql += " AND "+fecha+" <= "+fechaHasta;
            }
            sql = ("".equals(usuario))?sql:
                    sql+" AND "+campos.getString("SQL.P_FDS.usuario")+"="+usuario;
            sql = ("".equals(estado))?sql:
                    sql+" AND "+campos.getString("SQL.P_FDS.estado")+"='"+estado+"'";
            sql += " AND "+
                    campos.getString("SQL.P_FDS.paisOrigen")+"= "+pais+" AND "+
                    campos.getString("SQL.P_FDS.provinciaOrigen")+"= "+provincia+" AND "+
                    campos.getString("SQL.P_FDS.municipioOrigen")+"= "+municipio+" AND "+
                    campos.getString("SQL.P_FDS.paisDestino")+"= "+pais+" AND "+
                    campos.getString("SQL.P_FDS.provinciaDestino")+"= "+provincia+" AND "+
                    campos.getString("SQL.P_FDS.municipioDestino")+"= "+municipio+" AND "+
                    campos.getString("SQL.P_FDS.usuario")+"="+campos.getString("SQL.A_USU.codigo")+" AND "+
                    campos.getString("SQL.P_FDS.paisOrigen")+"= DISTRITOOR."+campos.getString("SQL.T_DIS.pais")+" AND "+
                    campos.getString("SQL.P_FDS.provinciaOrigen")+"= DISTRITOOR."+campos.getString("SQL.T_DIS.provincia")+" AND "+
                    campos.getString("SQL.P_FDS.municipioOrigen")+"= DISTRITOOR."+campos.getString("SQL.T_DIS.municipio")+" AND "+
                    campos.getString("SQL.P_FDS.distritoOrigen")+"= DISTRITOOR."+campos.getString("SQL.T_DIS.codigo")+" AND "+
                    campos.getString("SQL.P_FDS.paisOrigen")+"= SECCIONOR."+campos.getString("SQL.T_SEC.pais")+" AND "+
                    campos.getString("SQL.P_FDS.provinciaOrigen")+"= SECCIONOR."+campos.getString("SQL.T_SEC.provincia")+" AND "+
                    campos.getString("SQL.P_FDS.municipioOrigen")+"= SECCIONOR."+campos.getString("SQL.T_SEC.municipio")+" AND "+
                    campos.getString("SQL.P_FDS.distritoOrigen")+"= SECCIONOR."+campos.getString("SQL.T_SEC.distrito")+" AND "+
                    campos.getString("SQL.P_FDS.seccionOrigen")+"= SECCIONOR."+campos.getString("SQL.T_SEC.codigo")+" AND "+
                    campos.getString("SQL.P_FDS.letraOrigen")+"= SECCIONOR."+campos.getString("SQL.T_SEC.letra")+" AND "+
                    campos.getString("SQL.P_FDS.paisDestino")+"= DISTRITODE."+campos.getString("SQL.T_DIS.pais")+" AND "+
                    campos.getString("SQL.P_FDS.provinciaDestino")+"= DISTRITODE."+campos.getString("SQL.T_DIS.provincia")+" AND "+
                    campos.getString("SQL.P_FDS.municipioDestino")+"= DISTRITODE."+campos.getString("SQL.T_DIS.municipio")+" AND "+
                    campos.getString("SQL.P_FDS.distritoDestino")+"= DISTRITODE."+campos.getString("SQL.T_DIS.codigo")+" AND "+
                    campos.getString("SQL.P_FDS.paisDestino")+"= SECCIONDE."+campos.getString("SQL.T_SEC.pais")+" AND "+
                    campos.getString("SQL.P_FDS.provinciaDestino")+"= SECCIONDE."+campos.getString("SQL.T_SEC.provincia")+" AND "+
                    campos.getString("SQL.P_FDS.municipioDestino")+"= SECCIONDE."+campos.getString("SQL.T_SEC.municipio")+" AND "+
                    campos.getString("SQL.P_FDS.distritoDestino")+"= SECCIONDE."+campos.getString("SQL.T_SEC.distrito")+" AND "+
                    campos.getString("SQL.P_FDS.seccionDestino")+"= SECCIONDE."+campos.getString("SQL.T_SEC.codigo")+" AND "+
                    campos.getString("SQL.P_FDS.letraDestino")+"= SECCIONDE."+campos.getString("SQL.T_SEC.letra");
            sql += " ORDER BY " + campos.getString("SQL.P_FDS.fechaProceso") + " DESC ";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            //m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while(rs.next()){
                GeneralValueObject gVO = new GeneralValueObject();
                String codProceso = rs.getString(campos.getString("SQL.P_FDS.codigo"));
                gVO.setAtributo("codProceso",codProceso);
                gVO.setAtributo("descripcion",rs.getString(campos.getString("SQL.P_FDS.descripcion")));
                gVO.setAtributo("fechaProceso",rs.getString("FECHA"));
                gVO.setAtributo("codUsuario",rs.getString(campos.getString("SQL.A_USU.codigo")));
                gVO.setAtributo("descUsuario",rs.getString(campos.getString("SQL.A_USU.nombre")));
                gVO.setAtributo("estado",rs.getString(campos.getString("SQL.P_FDS.estado")));
                gVO.setAtributo("codDistritoOrigen",rs.getString("CODDISOR"));
                gVO.setAtributo("descDistritoOrigen",rs.getString("NOMDISOR"));
                gVO.setAtributo("codSeccionOrigen",rs.getString("CODSECOR"));
                gVO.setAtributo("descSeccionOrigen",rs.getString("NOMSECOR"));
                gVO.setAtributo("letraOrigen",rs.getString("LETSECOR"));
                gVO.setAtributo("codDistritoDestino",rs.getString("CODDISDE"));
                gVO.setAtributo("descDistritoDestino",rs.getString("NOMDISDE"));
                gVO.setAtributo("codSeccionDestino",rs.getString("CODSECDE"));
                gVO.setAtributo("descSeccionDestino",rs.getString("NOMSECDE"));
                gVO.setAtributo("letraDestino",rs.getString("LETSECDE"));
                gVO.setAtributo("iniciado",buscarSiIniciado(codProceso,con));
                resultado.add(gVO);
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
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en FusionDivisionDAO.getListaProcesos");
            }
        }
        return resultado;
    }

    public String retrocederProceso(String[] params,GeneralValueObject gVO){
        String correcto = "SI";
        String sql = "";
        AdaptadorSQLBD bd = null;
        Connection con = null;
        ResultSet rs = null;
        Statement state = null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();
            String codProceso = (String)gVO.getAtributo("codProceso");
            sql = "SELECT P_OPE.*, "+
                    bd.convertir("P_OPE."+campos.getString("SQL.P_OPE.fechaOperacion"),bd.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS FECHA,"+
                    "FROM P_OPE WHERE "+
                    campos.getString("SQL.P_OPE.proceso")+"="+codProceso; // Solo son RD, segun la fusion
            /*
            sql = "SELECT P_HAB.*,P_OPE.*,P_HOJ.*,T_DPO.*,T_DSU.* "
              +", TO_CHAR("+campos.getString("SQL.P_OPE.fechaOperacion")+",'DD/MM/YYYY') AS FECHA "
            + "FROM P_HAB,P_OPE,P_HOJ,T_DPO,T_DSU WHERE "+
            campos.getString("SQL.P_OPE.proceso")+"="+codProceso+" AND "+
            campos.getString("SQL.P_OPE.habitante")+"="+campos.getString("SQL.P_HAB.codigo")+" AND "+
            campos.getString("SQL.P_OPE.pais")+"="+campos.getString("SQL.P_HOJ.pais")+" AND "+
            campos.getString("SQL.P_OPE.provincia")+"="+campos.getString("SQL.P_HOJ.provincia")+" AND "+
            campos.getString("SQL.P_OPE.municipio")+"="+campos.getString("SQL.P_HOJ.municipio")+" AND "+
            campos.getString("SQL.P_OPE.distrito")+"="+campos.getString("SQL.P_HOJ.distrito")+" AND "+
            campos.getString("SQL.P_OPE.seccion")+"="+campos.getString("SQL.P_HOJ.seccion")+" AND "+
            campos.getString("SQL.P_OPE.letra")+"="+campos.getString("SQL.P_HOJ.letra")+" AND "+
            campos.getString("SQL.P_OPE.hoja")+"="+campos.getString("SQL.P_HOJ.numero")+" AND "+
            campos.getString("SQL.P_OPE.familia")+"="+campos.getString("SQL.P_HOJ.familia")+" AND "+
            campos.getString("SQL.P_OPE.version")+"="+campos.getString("SQL.P_HOJ.version")+" AND "+
            campos.getString("SQL.P_HOJ.domicilio")+"="+campos.getString("SQL.T_DPO.domicilio")+" AND "+
            campos.getString("SQL.T_DPO.suelo")+"="+campos.getString("SQL.T_DSU.identificador");
            */
            //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            Vector hojas = new Vector();
            while(rs.next()){
                gVO.setAtributo("codHabitante",rs.getString(campos.getString("SQL.P_OPE.habitante")));
                gVO.setAtributo("codOperacion",rs.getString(campos.getString("SQL.P_OPE.numero")));
                gVO.setAtributo("fechaOperacion",rs.getString("FECHA"));
                eliminarOperacionRD(con,gVO,bd);
                hojas.add(gVO.getAtributo("hoja"));
            }
            int i = 0;
            for(i=0;i<hojas.size();i++){
                GeneralValueObject hoja = (GeneralValueObject)hojas.get(i);
                eliminarHoja(con,hoja);
            }
            sql= "DELETE P_FDD WHERE "+
                    campos.getString("SQL.P_FDD.proceso")+"="+codProceso;
            //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state.executeUpdate(sql);
            sql= "DELETE P_FDS WHERE "+
                    campos.getString("SQL.P_FDS.codigo")+"="+codProceso;
            //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state.executeUpdate(sql);
        }catch(Exception e){
            rollBackTransaction(bd,con,e);
            correcto = "NO";
        }finally{
            commitTransaction(bd,con);
        }
        return correcto;
    }

    private void eliminarOperacionRD(Connection con,GeneralValueObject gVO,AdaptadorSQLBD bd)
            throws Exception {
        // Solo traigo en gVO un RD asociado al proceso
        // Seleccionamos todas las operaciones de ese hab. ordenadas por fecha.
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        Statement stmt1 = con.createStatement();
        ResultSet rs = null;
        String sql = "SELECT P_HAB.*,P_OPE.*,P_HOJ.*,T_DPO.*,T_DSU.*, "+
                bd.convertir("P_OPE."+campos.getString("SQL.P_OPE.fechaOperacion"),bd.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS FECHAOPERACION "
                + "FROM P_HAB,P_OPE,P_HOJ,T_DPO,T_DSU WHERE ";
        sql+= campos.getString("SQL.P_OPE.habitante")+"="+gVO.getAtributo("codHabitante")+" AND "+
                campos.getString("SQL.P_OPE.habitante")+"="+campos.getString("SQL.P_HAB.codigo")+" AND "+
                campos.getString("SQL.P_OPE.pais")+"="+campos.getString("SQL.P_HOJ.pais")+" AND "+
                campos.getString("SQL.P_OPE.provincia")+"="+campos.getString("SQL.P_HOJ.provincia")+" AND "+
                campos.getString("SQL.P_OPE.municipio")+"="+campos.getString("SQL.P_HOJ.municipio")+" AND "+
                campos.getString("SQL.P_OPE.distrito")+"="+campos.getString("SQL.P_HOJ.distrito")+" AND "+
                campos.getString("SQL.P_OPE.seccion")+"="+campos.getString("SQL.P_HOJ.seccion")+" AND "+
                campos.getString("SQL.P_OPE.letra")+"="+campos.getString("SQL.P_HOJ.letra")+" AND "+
                campos.getString("SQL.P_OPE.hoja")+"="+campos.getString("SQL.P_HOJ.numero")+" AND "+
                campos.getString("SQL.P_OPE.familia")+"="+campos.getString("SQL.P_HOJ.familia")+" AND "+
                campos.getString("SQL.P_OPE.version")+"="+campos.getString("SQL.P_HOJ.version")+" AND "+
                campos.getString("SQL.P_HOJ.domicilio")+"="+campos.getString("SQL.T_DPO.domicilio")+" AND "+
                campos.getString("SQL.T_DPO.suelo")+"="+campos.getString("SQL.T_DSU.identificador")+
                " ORDER BY "+campos.getString("SQL.P_OPE.fechaOperacion");
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        rs = stmt.executeQuery(sql);
        //rs.setFetchDirection(rs.FETCH_UNKNOWN);
        boolean siguiente = false;
        boolean primeraVez = true;
        int codOpeEliminar = Integer.parseInt((String)gVO.getAtributo("codOperacion"));
        //Date fechaOperacion = (Date)gVO.getAtributo("fechaOperacion");
        Date fechaOperacion = Fecha.obtenerDate((String) gVO.getAtributo("fechaOperacion"));
        String codPais = "";
        String codProvincia = "";
        String codMunicipio = "";
        String distrito = "";
        String seccion = "";
        String letraSeccion = "";
        String numHoja = "";
        String familia = "";
        String version = "";
        String codigoNuevaOperacion = "";
        String codDPO = "";
        String codDSU = "";
        String tipoNumeracion = "";
        String codVia = "";
        String codTramo = "";
        GeneralValueObject hoja = new GeneralValueObject();
        boolean finActualizar = false;
        while(rs.next()){
            int codigoOperacion = rs.getInt(campos.getString("SQL.P_OPE.numero"));
            if(codigoOperacion==codOpeEliminar){
                // HE LLEGADO A LA OPERACION QUE HAY QUE ELIMINAR
                rs.previous();
                // Datos de la hoja antes del RD
                codPais = rs.getString(campos.getString("SQL.P_OPE.pais"));
                codProvincia = rs.getString(campos.getString("SQL.P_OPE.provincia"));
                codMunicipio = rs.getString(campos.getString("SQL.P_OPE.municipio"));
                distrito = rs.getString(campos.getString("SQL.P_OPE.distrito"));
                seccion = rs.getString(campos.getString("SQL.P_OPE.seccion"));
                letraSeccion = rs.getString(campos.getString("SQL.P_OPE.letra"));
                numHoja = rs.getString(campos.getString("SQL.P_OPE.hoja"));
                familia = rs.getString(campos.getString("SQL.P_OPE.familia"));
                version = rs.getString(campos.getString("SQL.P_OPE.version"));
                codigoNuevaOperacion = rs.getString(campos.getString("SQL.P_OPE.numero"));
                codDPO = rs.getString(campos.getString("SQL.T_DPO.domicilio"));
                codDSU = rs.getString(campos.getString("SQL.T_DSU.identificador"));
                tipoNumeracion = rs.getString(campos.getString("SQL.T_DSU.tipoNumeracion"));
                codVia = rs.getString(campos.getString("SQL.T_DSU.vial"));
                codTramo = rs.getString(campos.getString("SQL.T_DSU.codigoTRM"));
                rs.next();
                String codOperacion = rs.getString(campos.getString("SQL.P_OPE.numero"));
                String codOpeHab = rs.getString(campos.getString("SQL.P_HAB.operacion"));
                String codHab = rs.getString(campos.getString("SQL.P_HAB.codigo"));
                if(codOpeHab.equals(codOperacion)){ // ES LA ULTIMA OPERACION TENGO QUE ACTUALIZAR EL HABITANTE
                    sql = "UPDATE P_HAB SET "+
                            campos.getString("SQL.P_HAB.distrito")+"="+distrito+","+
                            campos.getString("SQL.P_HAB.seccion")+"="+seccion+","+
                            campos.getString("SQL.P_HAB.letra")+"='"+letraSeccion+"',"+
                            campos.getString("SQL.P_HAB.hoja")+"="+numHoja+","+
                            campos.getString("SQL.P_HAB.familia")+"="+familia+","+
                            campos.getString("SQL.P_HAB.version")+"="+version+","+
                            campos.getString("SQL.P_HAB.operacion")+"="+codigoNuevaOperacion+
                            " WHERE "+
                            campos.getString("SQL.P_HAB.codigo")+"="+codHab;
                    //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt1.executeUpdate(sql);
                    // VUELVO A DAR DE ALTA LA HOJA
                    sql = "UPDATE P_HOJ SET "+
                            campos.getString("SQL.P_HOJ.situacion")+"='A'"+
                            " WHERE ";
                    sql+= campos.getString("SQL.P_HOJ.pais")+"="+codPais+" AND "+
                            campos.getString("SQL.P_HOJ.provincia")+"="+codProvincia+" AND "+
                            campos.getString("SQL.P_HOJ.municipio")+"="+codMunicipio+" AND "+
                            campos.getString("SQL.P_HOJ.distrito")+"="+distrito+" AND "+
                            campos.getString("SQL.P_HOJ.seccion")+"="+seccion+" AND "+
                            campos.getString("SQL.P_HOJ.letra")+"='"+letraSeccion+"' AND "+
                            campos.getString("SQL.P_HOJ.numero")+"="+numHoja+" AND "+
                            campos.getString("SQL.P_HOJ.familia")+"="+familia+" AND "+
                            campos.getString("SQL.P_HOJ.version")+"="+version;
                    //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt1.executeUpdate(sql);
                    // VUELVO A DAR DE ALTA LA DPO
                    sql = "UPDATE T_DPO SET "+
                            campos.getString("SQL.T_DPO.situacion")+"='A'"+
                            " WHERE "+
                            campos.getString("SQL.T_DPO.domicilio")+"="+codDPO;
                    //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt1.executeUpdate(sql);
                    // VUELVO A DAR DE ALTA LA DSU
                    sql = "UPDATE T_DSU SET "+
                            campos.getString("SQL.T_DSU.situacion")+"='A'"+
                            " WHERE "+
                            campos.getString("SQL.T_DSU.paisTRM")+"="+codPais+" AND "+
                            campos.getString("SQL.T_DSU.provinciaTRM")+"="+codProvincia+" AND "+
                            campos.getString("SQL.T_DSU.municipioTRM")+"="+codMunicipio+" AND "+
                            campos.getString("SQL.T_DSU.tipoNumeracionTRM")+"="+tipoNumeracion+" AND "+
                            campos.getString("SQL.T_DSU.vialTRM")+"="+codVia+" AND "+
                            campos.getString("SQL.T_DSU.codigoTRM")+"="+codTramo;
                    //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt1.executeUpdate(sql);
                    // VUELVO A DAR DE ALTA EL TRAMO
                    sql = "UPDATE T_TRM SET " +
                            campos.getString("SQL.T_TRM.situacion")+"='A'"+
                            " WHERE " +
                            campos.getString("SQL.T_TRM.pais")+"="+codPais+" AND "+
                            campos.getString("SQL.T_TRM.provincia")+"="+codProvincia+" AND "+
                            campos.getString("SQL.T_TRM.municipio")+"="+codMunicipio+" AND "+
                            campos.getString("SQL.T_TRM.vial")+"="+codVia+" AND "+
                            campos.getString("SQL.T_TRM.tipoNumeracion")+"="+tipoNumeracion+" AND "+
                            campos.getString("SQL.T_TRM.codigo")+"="+codTramo;
                    //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt1.executeUpdate(sql);
                }
                // BORRO LA OPERACION
                /* lo he sacado
                sql = "DELETE P_OPE WHERE "+
                  campos.getString("SQL.P_OPE.numero") + "=" + codigoOperacion;
                //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt1.executeUpdate(sql);
                */
                siguiente=true;
                hoja.setAtributo("codPais",rs.getString(campos.getString("SQL.P_OPE.pais")));
                hoja.setAtributo("codProvincia",rs.getString(campos.getString("SQL.P_OPE.provincia")));
                hoja.setAtributo("codMunicipio",rs.getString(campos.getString("SQL.P_OPE.municipio")));
                hoja.setAtributo("distrito",rs.getString(campos.getString("SQL.P_OPE.distrito")));
                hoja.setAtributo("seccion",rs.getString(campos.getString("SQL.P_OPE.seccion")));
                hoja.setAtributo("letra",rs.getString(campos.getString("SQL.P_OPE.letra")));
                hoja.setAtributo("numHoja",rs.getString(campos.getString("SQL.P_OPE.hoja")));
                hoja.setAtributo("familia",rs.getString(campos.getString("SQL.P_OPE.familia")));
                hoja.setAtributo("version",rs.getString(campos.getString("SQL.P_OPE.version")));
                hoja.setAtributo("codDPO",rs.getString(campos.getString("SQL.T_DPO.domicilio")));
                hoja.setAtributo("codDSU",rs.getString(campos.getString("SQL.T_DSU.identificador")));
                hoja.setAtributo("tipoNumeracion",rs.getString(campos.getString("SQL.T_DSU.tipoNumeracion")));
                hoja.setAtributo("codVia",rs.getString(campos.getString("SQL.T_DSU.vial")));
                hoja.setAtributo("codTramo",rs.getString(campos.getString("SQL.T_DSU.codigoTRM")));
                gVO.setAtributo("hoja",hoja);
            }else{
                Date fechaOpe = rs.getDate(campos.getString("SQL.P_OPE.fechaOperacion"));
                if(fechaOpe.after(fechaOperacion)){ // ACTUALIZO LAS OPERACIONES POSTERIORES
                    String cav = rs.getString(campos.getString("SQL.P_OPE.causaVariacion"));
                    if(cav.equals("RD")){ //||cav.equals("CD")||cav.equals("CR")){
                        finActualizar = true;
                    }
                    if(!finActualizar){
                        sql = "UPDATE P_OPE SET " +
                                campos.getString("SQL.P_OPE.pais") + "=" + codPais + "," +
                                campos.getString("SQL.P_OPE.provincia") + "=" + codProvincia + "," +
                                campos.getString("SQL.P_OPE.municipio") + "=" + codMunicipio + "," +
                                campos.getString("SQL.P_OPE.distrito") + "=" + distrito + "," +
                                campos.getString("SQL.P_OPE.seccion") + "=" + seccion + "," +
                                campos.getString("SQL.P_OPE.letra") + "='" + letraSeccion + "'," +
                                campos.getString("SQL.P_OPE.hoja") + "=" + numHoja + "," +
                                campos.getString("SQL.P_OPE.familia") + "=" + familia + "," +
                                campos.getString("SQL.P_OPE.version") + "=" + version;
                        sql+= " WHERE " + campos.getString("SQL.P_OPE.numero") + " = " +codigoOperacion;
                        stmt1.executeUpdate(sql);
                    }
                    if(siguiente){
                        sql = "UPDATE P_OPE SET "
                                + campos.getString("SQL.P_OPE.operacionAnt") + "=" + codigoNuevaOperacion;
                        sql+= " WHERE " + campos.getString("SQL.P_OPE.numero") + " = " +codigoOperacion;
                        stmt1.executeUpdate(sql);
                        siguiente = false;

                        // BORRO LA OPERACION
                        sql = "DELETE P_OPE WHERE "+
                                campos.getString("SQL.P_OPE.numero") + "=" + codOpeEliminar;
                        //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                        stmt1.executeUpdate(sql);
                    }

                }
            }
        }
    }

    private void eliminarHoja(Connection con,GeneralValueObject gVO) throws Exception{
        Statement stmt = con.createStatement();
        ResultSet rs1 = null;
        String codPais = (String)gVO.getAtributo("codPais");
        String codProvincia = (String)gVO.getAtributo("codProvincia");
        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String distrito = (String)gVO.getAtributo("distrito");
        String seccion = (String)gVO.getAtributo("seccion");
        String letraSeccion = (String)gVO.getAtributo("letra");
        String numHoja = (String)gVO.getAtributo("numHoja");
        String familia = (String)gVO.getAtributo("familia");
        String version = (String)gVO.getAtributo("version");
        String codDPO = (String)gVO.getAtributo("codDPO");
        String codDSU = (String)gVO.getAtributo("codDSU");
        String tipoNumeracion = (String)gVO.getAtributo("tipoNumeracion");
        String codVia = (String)gVO.getAtributo("codVia");
        String codTramo = (String)gVO.getAtributo("codTramo");
        String sql = "";

        // BORRO LA HOJA
        sql = "DELETE P_HOJ WHERE " +
                campos.getString("SQL.P_HOJ.pais") + "=" + codPais + " AND " +
                campos.getString("SQL.P_HOJ.provincia") + "=" + codProvincia + " AND " +
                campos.getString("SQL.P_HOJ.municipio") + "=" + codMunicipio + " AND " +
                campos.getString("SQL.P_HOJ.distrito") + "=" + distrito + " AND " +
                campos.getString("SQL.P_HOJ.seccion") + "=" + seccion + " AND " +
                campos.getString("SQL.P_HOJ.letra") + "='" + letraSeccion + "' AND " +
                campos.getString("SQL.P_HOJ.numero") + "=" + numHoja + " AND " +
                campos.getString("SQL.P_HOJ.familia") + "=" + familia + " AND " +
                campos.getString("SQL.P_HOJ.version") + "=" + version;
        //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt.executeUpdate(sql);
        // BORRO LA DPO
        sql = "DELETE T_DPO WHERE "+
                campos.getString("SQL.T_DPO.domicilio") + "=" + codDPO;
        //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt.executeUpdate(sql);
        // BORRO EL DOM
        sql = "DELETE T_DOM WHERE "+
                campos.getString("SQL.T_DOM.idDomicilio") + "=" + codDPO;
        //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt.executeUpdate(sql);
        // BORRO LAS DSU DEL TRAMO
        sql = "DELETE T_DSU WHERE "+
                campos.getString("SQL.T_DSU.paisTRM")+"="+codPais+" AND "+
                campos.getString("SQL.T_DSU.provinciaTRM")+"="+codProvincia+" AND "+
                campos.getString("SQL.T_DSU.municipioTRM")+"="+codMunicipio+" AND "+
                campos.getString("SQL.T_DSU.tipoNumeracionTRM")+"="+tipoNumeracion+" AND "+
                campos.getString("SQL.T_DSU.vialTRM")+"="+codVia+" AND "+
                campos.getString("SQL.T_DSU.codigoTRM")+"="+codTramo;
        //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt.executeUpdate(sql);
        // BORRO EL TRAMO
        sql = "DELETE T_TRM WHERE "+
                campos.getString("SQL.T_TRM.pais") + "=" + codPais + " AND " +
                campos.getString("SQL.T_TRM.provincia") + "=" + codProvincia + " AND " +
                campos.getString("SQL.T_TRM.municipio") + "=" + codMunicipio + " AND " +
                campos.getString("SQL.T_TRM.vial") + "=" + codVia + " AND " +
                campos.getString("SQL.T_TRM.tipoNumeracion") + "=" + tipoNumeracion + " AND " +
                campos.getString("SQL.T_TRM.codigo") + "=" + codTramo;
        //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt.executeUpdate(sql);
    }

    public String insertarProceso(String[] params,GeneralValueObject paramsVO){
        String correcto = "SI";
        String sql = "";
        AdaptadorSQLBD bd = null;
        Connection con = null;
        ResultSet rs = null;
        Statement state = null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();
            String pais = (String)paramsVO.getAtributo("codPais");
            String provincia = (String)paramsVO.getAtributo("codProvincia");
            String municipio = (String)paramsVO.getAtributo("codMunicipio");
            String tipoOperacion = (String)paramsVO.getAtributo("tipoOperacion");
            String fecha = bd.convertir((String)paramsVO.getAtributo("fecha"),AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY");
            String descripcion = (String)paramsVO.getAtributo("descripcion");
            String usuario = (String)paramsVO.getAtributo("usuario");
            String estado = "P";
            if(m_Log.isDebugEnabled()) m_Log.debug("Tipo operacion: " + tipoOperacion);
            if(tipoOperacion.equals("D")){
                correcto = comprobarNoHojasEnSeccionDestino(params,paramsVO);
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("Correcto: " + correcto);
            if(correcto.equals("SI")){
                int codigo = 1;
                sql = "SELECT " + bd.funcionMatematica(bd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.P_FDS.codigo")}) +
                        " AS MAXIMO" +
                        " FROM P_FDS";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs = state.executeQuery(sql);
                if (rs.next()) {
                    codigo = rs.getInt("MAXIMO") + 1;
                }
                sql = "INSERT INTO P_FDS (" +
                        campos.getString("SQL.P_FDS.codigo") + "," +
                        campos.getString("SQL.P_FDS.descripcion") + "," +
                        campos.getString("SQL.P_FDS.tipoOperacion") + "," +
                        campos.getString("SQL.P_FDS.estado") + "," +
                        campos.getString("SQL.P_FDS.fechaProceso") + "," +
                        campos.getString("SQL.P_FDS.usuario") + "," +
                        campos.getString("SQL.P_FDS.paisOrigen") + "," +
                        campos.getString("SQL.P_FDS.provinciaOrigen") + "," +
                        campos.getString("SQL.P_FDS.municipioOrigen") + "," +
                        campos.getString("SQL.P_FDS.distritoOrigen") + "," +
                        campos.getString("SQL.P_FDS.seccionOrigen") + "," +
                        campos.getString("SQL.P_FDS.letraOrigen") + "," +
                        campos.getString("SQL.P_FDS.paisDestino") + "," +
                        campos.getString("SQL.P_FDS.provinciaDestino") + "," +
                        campos.getString("SQL.P_FDS.municipioDestino") + "," +
                        campos.getString("SQL.P_FDS.distritoDestino") + "," +
                        campos.getString("SQL.P_FDS.seccionDestino") + "," +
                        campos.getString("SQL.P_FDS.letraDestino") +
                        ") VALUES (" +
                        codigo + ",'" +
                        descripcion + "','" +
                        tipoOperacion + "','" +
                        estado + "'," +
                        fecha + "," +
                        usuario + "," +
                        pais + "," +
                        provincia + "," +
                        municipio + "," +
                        paramsVO.getAtributo("distritoOrigen") + "," +
                        paramsVO.getAtributo("seccionOrigen") + ",'" +
                        paramsVO.getAtributo("letraOrigen") + "'," +
                        pais + "," +
                        provincia + "," +
                        municipio + "," +
                        paramsVO.getAtributo("distritoDestino") + "," +
                        paramsVO.getAtributo("seccionDestino") + ",'" +
                        paramsVO.getAtributo("letraDestino") + "')";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state.executeUpdate(sql);
            }
            rs.close();
            state.close();
        }catch(Exception e){
            e.printStackTrace();
            rollBackTransaction(bd,con,e);
            correcto = "NO";
        }finally{
            commitTransaction(bd,con);
        }
        return correcto;
    }

    private String comprobarNoHojasEnSeccionDestino(String[] params,GeneralValueObject paramsVO){
        String correcto = "SI";
        String sql = "";
        AdaptadorSQLBD bd = null;
        Connection con = null;
        ResultSet rs = null;
        Statement state = null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
//      bd.inicioTransaccion(con);
            state = con.createStatement();
            sql = "SELECT P_HOJ.* FROM P_HOJ WHERE "+
                    campos.getString("SQL.P_HOJ.pais")+"="+paramsVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.P_HOJ.provincia")+"="+paramsVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.P_HOJ.municipio")+"="+paramsVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.P_HOJ.distrito")+"="+paramsVO.getAtributo("distritoDestino")+" AND "+
                    campos.getString("SQL.P_HOJ.seccion")+"="+paramsVO.getAtributo("seccionDestino")+" AND "+
                    campos.getString("SQL.P_HOJ.letra")+"='"+paramsVO.getAtributo("letraDestino")+"'";
            //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            if(rs.next())
                correcto = "EXISTEHOJA";
            rs.close();
            state.close();
        }catch (Exception e){
//      rollBackTransaction(abd,conexion,e);
            correcto = "NO";
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
//      commitTransaction(abd,conexion);
                bd.devolverConexion(con);
            }catch (BDException e) {
                e.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en FusionDivisionDAO.comprobarNoHojasEnSeccionDestino");
            }
        }
        return correcto;
    }

    public String eliminarProceso(String[] params,GeneralValueObject paramsVO){
        String correcto = "SI";
        String sql = "";
        AdaptadorSQLBD bd = null;
        Connection con = null;
        ResultSet rs = null;
        Statement state = null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();
            sql = "DELETE P_FDS WHERE "+
                    campos.getString("SQL.P_FDS.codigo")+"="+paramsVO.getAtributo("codProceso");
            //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state.executeUpdate(sql);
            state.close();
        }catch(Exception e){
            rollBackTransaction(bd,con,e);
            correcto = "NO";
        }finally{
            commitTransaction(bd,con);
        }
        return correcto;
    }

    public String finalizarProceso(String[] params,GeneralValueObject paramsVO){
        String correcto = "SI";
        String sql = "";
        AdaptadorSQLBD bd = null;
        Connection con = null;
        ResultSet rs = null;
        Statement state = null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();
            sql = "UPDATE P_FDS SET "+
                    campos.getString("SQL.P_FDS.estado")+"='F'"+
                    " WHERE "+
                    campos.getString("SQL.P_FDS.codigo")+"="+paramsVO.getAtributo("codProceso");
            //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state.executeUpdate(sql);
            state.close();
        }catch(Exception e){
            rollBackTransaction(bd,con,e);
            correcto = "NO";
        }finally{
            commitTransaction(bd,con);
        }
        return correcto;
    }

    public GeneralValueObject verDomicilios(String[] params,GeneralValueObject gVO) {
        String sql = "";
        GeneralValueObject resultado = new GeneralValueObject();
        AdaptadorSQLBD bd = null;
        Connection con = null;
        ResultSet rs = null;
        Statement state = null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
//      bd.inicioTransaccion(con);
            state = con.createStatement();
            int i = 0;
            String parteFrom = " P_HOJ.*,T_DPO.*,T_DSU.*,T_VIA.*,P_HAB.*,P_OPE.*,T_TID.*,T_ECO.*,T_ESI.*,T_NUC.* ";
            String parteWhere = campos.getString("SQL.T_DSU.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_DSU.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_DSU.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_DSU.vial")+"="+gVO.getAtributo("idVia")+" AND "+
                    "((("+campos.getString("SQL.T_DSU.numeroDesde")+">="+gVO.getAtributo("numDesde")+") AND "+
                    "("+campos.getString("SQL.T_DSU.numeroDesde")+"<="+gVO.getAtributo("numHasta")+")) OR ("+
                    campos.getString("SQL.T_DSU.numeroDesde")+" IS NULL)) AND "+
                    "(("+campos.getString("SQL.T_DSU.numeroHasta")+"<="+gVO.getAtributo("numHasta")+") OR ("+
                    campos.getString("SQL.T_DSU.numeroHasta")+" IS NULL)) AND "+
                    campos.getString("SQL.T_DSU.tipoNumeracionTRM")+"="+gVO.getAtributo("codTipoNumeracion")+" AND "+
                    campos.getString("SQL.T_DSU.paisTRM")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_DSU.provinciaTRM")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_DSU.municipioTRM")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_DSU.vialTRM")+"="+gVO.getAtributo("idVia")+" AND "+
                    campos.getString("SQL.T_DSU.codigoTRM")+"="+gVO.getAtributo("codTramo") + " AND " +
                    campos.getString("SQL.T_TRM.situacion")+"='A' AND " + campos.getString("SQL.T_DSU.situacion")+"='A'";

            ArrayList join = new ArrayList();
            join.add("T_DSU");
            join.add("LEFT");
            join.add("T_TRM");
            join.add(campos.getString("SQL.T_DSU.paisTRM")+"="+campos.getString("SQL.T_TRM.pais")+" AND "+
                    campos.getString("SQL.T_DSU.provinciaTRM")+"="+campos.getString("SQL.T_TRM.provincia")+" AND "+
                    campos.getString("SQL.T_DSU.municipioTRM")+"="+campos.getString("SQL.T_TRM.municipio")+" AND "+
                    campos.getString("SQL.T_DSU.tipoNumeracionTRM")+"="+campos.getString("SQL.T_TRM.tipoNumeracion")+" AND "+
                    campos.getString("SQL.T_DSU.vialTRM")+"="+campos.getString("SQL.T_TRM.vial")+" AND "+
                    campos.getString("SQL.T_DSU.codigoTRM")+"="+campos.getString("SQL.T_TRM.codigo"));
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
            join.add(campos.getString("SQL.T_ESI.eColectiva")+"="+campos.getString("SQL.T_ECO.identificador")+" AND "+
                    campos.getString("SQL.T_ESI.pais")+"="+campos.getString("SQL.T_ECO.pais")+" AND "+
                    campos.getString("SQL.T_ESI.provincia")+"="+campos.getString("SQL.T_ECO.provincia")+" AND "+
                    campos.getString("SQL.T_ESI.municipio")+"="+campos.getString("SQL.T_ECO.municipio"));
            join.add("INNER");
            join.add("T_VIA");
            join.add(campos.getString("SQL.T_DSU.pais")+"="+campos.getString("SQL.T_VIA.pais")+" AND "+
                    campos.getString("SQL.T_DSU.provincia")+"="+campos.getString("SQL.T_VIA.provincia")+" AND "+
                    campos.getString("SQL.T_DSU.municipio")+"="+campos.getString("SQL.T_VIA.municipio")+" AND "+
                    campos.getString("SQL.T_DSU.vial")+"="+campos.getString("SQL.T_VIA.identificador"));
            join.add("INNER");
            join.add("T_DPO");
            join.add(campos.getString("SQL.T_DSU.identificador")+"="+campos.getString("SQL.T_DPO.suelo"));
            join.add("LEFT");
            join.add("P_HOJ");
            join.add(campos.getString("SQL.T_DPO.domicilio")+"="+campos.getString("SQL.P_HOJ.domicilio"));
            join.add("LEFT");
            join.add("P_HAB");
            join.add(campos.getString("SQL.P_HOJ.pais")+"="+campos.getString("SQL.P_HAB.pais")+" AND "+
                    campos.getString("SQL.P_HOJ.provincia")+"="+campos.getString("SQL.P_HAB.provincia")+" AND "+
                    campos.getString("SQL.P_HOJ.municipio")+"="+campos.getString("SQL.P_HAB.municipio")+" AND "+
                    campos.getString("SQL.P_HOJ.distrito")+"="+campos.getString("SQL.P_HAB.distrito")+" AND "+
                    campos.getString("SQL.P_HOJ.seccion")+"="+campos.getString("SQL.P_HAB.seccion")+" AND "+
                    campos.getString("SQL.P_HOJ.letra")+"="+campos.getString("SQL.P_HAB.letra")+" AND "+
                    campos.getString("SQL.P_HOJ.numero")+"="+campos.getString("SQL.P_HAB.hoja")+" AND "+
                    campos.getString("SQL.P_HOJ.familia")+"="+campos.getString("SQL.P_HAB.familia")+" AND "+
                    campos.getString("SQL.P_HOJ.version")+"="+campos.getString("SQL.P_HAB.version"));
            join.add("LEFT");
            join.add("P_OPE");
            join.add(campos.getString("SQL.P_HAB.operacion")+"="+campos.getString("SQL.P_OPE.numero")+" AND "+
                    campos.getString("SQL.P_HAB.codigo")+"="+campos.getString("SQL.P_OPE.habitante"));
            join.add("LEFT");
            join.add("T_TID");
            join.add(campos.getString("SQL.P_HAB.tipoDocumento")+"="+campos.getString("SQL.T_TID.codigo"));
            join.add("false");

            sql = bd.join(parteFrom, parteWhere, (String[]) join.toArray(new String[]{}));
            sql+= " ORDER BY "+campos.getString("SQL.T_VIA.codVia")+","+campos.getString("SQL.T_DSU.numeroDesde")+","+
                    campos.getString("SQL.T_DSU.letraDesde")+","+campos.getString("SQL.T_DSU.bloque")+","+
                    campos.getString("SQL.T_DSU.portal")+","+campos.getString("SQL.T_DPO.escalera")+","+
                    campos.getString("SQL.T_DPO.planta")+","+campos.getString("SQL.T_DPO.puerta");
            //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            Vector hojas = new Vector();
            Vector habitantes = null;
            GeneralValueObject hoja = null;
            GeneralValueObject ultimaHoja = null;
            String codHojaAnterior = "";

            while (rs.next()) {
                String codHoja = rs.getString(campos.getString("SQL.P_HOJ.numero"));
                if(codHoja!=null && codHoja.equals(codHojaAnterior)){
                    // SIGO EN LA MISMA HOJA, SOLO AÑADO HABITANTES
                    GeneralValueObject habitante = new GeneralValueObject();
                    // HABITANTE
                    habitante.setAtributo("idTercero", rs.getString(campos.getString("SQL.P_HAB.codigo")));
                    habitante.setAtributo("nombre", rs.getString(campos.getString("SQL.P_HAB.nombre")));
                    habitante.setAtributo("apellido1", rs.getString(campos.getString("SQL.P_HAB.apellido1")));
                    habitante.setAtributo("apellido2", rs.getString(campos.getString("SQL.P_HAB.apellido2")));
                    habitante.setAtributo("particula1", rs.getString(campos.getString("SQL.P_HAB.pApellido1")));
                    habitante.setAtributo("particula2", rs.getString(campos.getString("SQL.P_HAB.pApellido2")));
                    habitante.setAtributo("codTipoDoc", rs.getString(campos.getString("SQL.T_TID.codigo")));
                    habitante.setAtributo("descTipoDoc", rs.getString(campos.getString("SQL.T_TID.nombre")));
                    habitante.setAtributo("documento", rs.getString(campos.getString("SQL.P_HAB.documento")));
                    habitante.setAtributo("orden", rs.getString(campos.getString("SQL.P_HAB.orden")));
                    habitante.setAtributo("telefono", rs.getString(campos.getString("SQL.P_HAB.telefono")));
                    habitante.setAtributo("sexo", rs.getString(campos.getString("SQL.P_HAB.sexo")));
                    habitante.setAtributo("nia", rs.getString(campos.getString("SQL.P_HAB.nia")));
                    habitante.setAtributo("familiaReal", rs.getString(campos.getString("SQL.P_HAB.familiaReal")));
                    habitante.setAtributo("operacion", rs.getString(campos.getString("SQL.P_HAB.operacion")));
                    habitante.setAtributo("codigoOperacion",rs.getString(campos.getString("SQL.P_OPE.codigoVariacion")));
                    habitante.setAtributo("causaVariacion",rs.getString(campos.getString("SQL.P_OPE.causaVariacion")));
                    habitante.setAtributo("situacion", rs.getString(campos.getString("SQL.P_HAB.situacion")));
                    habitantes.add(habitante);
                }else{
                    // HE CAMBIADO DE HOJA Y POR TANTO DE DOMICILIO
                    if(ultimaHoja!=null){
                        ultimaHoja.setAtributo("habitantes",habitantes);
                        hojas.add(ultimaHoja);
                    }
                    hoja = new GeneralValueObject();
                    hoja.setAtributo("codDSU",rs.getString(campos.getString("SQL.T_DSU.identificador")));
                    hoja.setAtributo("codDPO",rs.getString(campos.getString("SQL.T_DPO.domicilio")));
                    hoja.setAtributo("codPais",rs.getString(campos.getString("SQL.P_HOJ.pais")));
                    hoja.setAtributo("codProvincia",rs.getString(campos.getString("SQL.P_HOJ.provincia")));
                    hoja.setAtributo("codMunicipio",rs.getString(campos.getString("SQL.P_HOJ.municipio")));
                    hoja.setAtributo("distrito",rs.getString(campos.getString("SQL.P_HOJ.distrito")));
                    hoja.setAtributo("seccion",rs.getString(campos.getString("SQL.P_HOJ.seccion")));
                    hoja.setAtributo("letra",rs.getString(campos.getString("SQL.P_HOJ.letra")));
                    hoja.setAtributo("hoja",rs.getString(campos.getString("SQL.P_HOJ.numero")));
                    hoja.setAtributo("familia",rs.getString(campos.getString("SQL.P_HOJ.familia")));
                    hoja.setAtributo("version",rs.getString(campos.getString("SQL.P_HOJ.version")));
                    hoja.setAtributo("urbanizacion",rs.getString(campos.getString("SQL.P_HOJ.urbanizacion")));
                    hoja.setAtributo("contador",rs.getString(campos.getString("SQL.P_HOJ.contador")));
                    hoja.setAtributo("situacion",rs.getString(campos.getString("SQL.P_HOJ.situacion")));
                    hoja.setAtributo("codDomicilio", rs.getString(campos.getString("SQL.T_VIA.codVia")));
                    hoja.setAtributo("descDomicilio", rs.getString(campos.getString("SQL.T_VIA.nombreVia")));
                    hoja.setAtributo("numDesde", rs.getString(campos.getString("SQL.T_DSU.numeroDesde")));
                    hoja.setAtributo("letraDesde", rs.getString(campos.getString("SQL.T_DSU.letraDesde")));
                    hoja.setAtributo("numHasta", rs.getString(campos.getString("SQL.T_DSU.numeroHasta")));
                    hoja.setAtributo("letraHasta", rs.getString(campos.getString("SQL.T_DSU.letraDesde")));
                    hoja.setAtributo("bloque", rs.getString(campos.getString("SQL.T_DSU.bloque")));
                    hoja.setAtributo("portal", rs.getString(campos.getString("SQL.T_DSU.portal")));
                    hoja.setAtributo("escalera", rs.getString(campos.getString("SQL.T_DPO.escalera")));
                    hoja.setAtributo("planta", rs.getString(campos.getString("SQL.T_DPO.planta")));
                    hoja.setAtributo("puerta", rs.getString(campos.getString("SQL.T_DPO.puerta")));
                    hoja.setAtributo("km", rs.getString(campos.getString("SQL.T_DSU.kilometro")));
                    hoja.setAtributo("hm", rs.getString(campos.getString("SQL.T_DSU.hectometro")));
                    habitantes = new Vector();
                    GeneralValueObject habitante = new GeneralValueObject();
                    // HABITANTE
                    habitante.setAtributo("idTercero", rs.getString(campos.getString("SQL.P_HAB.codigo")));
                    habitante.setAtributo("nombre", rs.getString(campos.getString("SQL.P_HAB.nombre")));
                    habitante.setAtributo("apellido1", rs.getString(campos.getString("SQL.P_HAB.apellido1")));
                    habitante.setAtributo("apellido2", rs.getString(campos.getString("SQL.P_HAB.apellido2")));
                    habitante.setAtributo("particula1", rs.getString(campos.getString("SQL.P_HAB.pApellido1")));
                    habitante.setAtributo("particula2", rs.getString(campos.getString("SQL.P_HAB.pApellido2")));
                    habitante.setAtributo("codTipoDoc", rs.getString(campos.getString("SQL.T_TID.codigo")));
                    habitante.setAtributo("descTipoDoc", rs.getString(campos.getString("SQL.T_TID.nombre")));
                    habitante.setAtributo("documento", rs.getString(campos.getString("SQL.P_HAB.documento")));
                    habitante.setAtributo("orden", rs.getString(campos.getString("SQL.P_HAB.orden")));
                    habitante.setAtributo("telefono", rs.getString(campos.getString("SQL.P_HAB.telefono")));
                    habitante.setAtributo("sexo", rs.getString(campos.getString("SQL.P_HAB.sexo")));
                    habitante.setAtributo("nia", rs.getString(campos.getString("SQL.P_HAB.nia")));
                    habitante.setAtributo("familiaReal", rs.getString(campos.getString("SQL.P_HAB.familiaReal")));
                    habitante.setAtributo("operacion", rs.getString(campos.getString("SQL.P_HAB.operacion")));
                    habitante.setAtributo("codigoOperacion",rs.getString(campos.getString("SQL.P_OPE.codigoVariacion")));
                    habitante.setAtributo("causaVariacion",rs.getString(campos.getString("SQL.P_OPE.causaVariacion")));
                    habitante.setAtributo("situacion", rs.getString(campos.getString("SQL.P_HAB.situacion")));
                    habitantes.add(habitante);
                    ultimaHoja = hoja;
                    codHojaAnterior = codHoja;
                }
            }
            if(ultimaHoja!=null){
                ultimaHoja.setAtributo("habitantes",habitantes);
                hojas.add(ultimaHoja);
            }
            resultado.setAtributo("hojas",hojas);
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
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en FusionDivisionDAO.verDomicilios");
            }
        }
        return resultado;
    }

    public GeneralValueObject verDetalle(String[] params,GeneralValueObject gVO) {
        String sql = "";
        GeneralValueObject resultado = new GeneralValueObject();
        AdaptadorSQLBD bd = null;
        Connection con = null;
        ResultSet rs = null;
        Statement state = null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
//      bd.inicioTransaccion(con);
            state = con.createStatement();
            int i = 0;

            // KR<- sql = "SELECT P_FDD.*,T_VIA.* FROM P_FDD,T_VIA "+
            // KR -> añadir esi a la descripcion de via
            String parteFrom = " P_FDD.*,T_VIA.*"
                    +","+campos.getString("SQL.T_TVI.tipoVia")
                    +",T_ECO.*,T_ESI.* ";
            String parteWhere = campos.getString("SQL.P_FDD.proceso")+"="+gVO.getAtributo("codProceso")+" AND " +
                    campos.getString("SQL.T_TRM.situacion")+"='A' ";

            ArrayList join = new ArrayList();
            join.add("P_FDD");
            join.add("INNER");
            join.add("T_TRM");
            join.add(campos.getString("SQL.P_FDD.pais")+"="+campos.getString("SQL.T_TRM.pais")+" AND "+
                    campos.getString("SQL.P_FDD.provincia")+"="+campos.getString("SQL.T_TRM.provincia")+" AND "+
                    campos.getString("SQL.P_FDD.municipio")+"="+campos.getString("SQL.T_TRM.municipio")+" AND "+
                    campos.getString("SQL.P_FDD.vial")+"="+campos.getString("SQL.T_TRM.vial")
                    + " AND "+ campos.getString("SQL.P_FDD.tipoNumeracion") +"=" + campos.getString("SQL.T_TRM.tipoNumeracion")
                    +" AND "+ campos.getString("SQL.P_FDD.numeroDesde") +" = "+campos.getString("SQL.T_TRM.primerNumero")
                    +" AND "+ campos.getString("SQL.P_FDD.numeroHasta") +" = "+campos.getString("SQL.T_TRM.ultimoNumero"));
            join.add("INNER");
            join.add("T_VIA");
            join.add(campos.getString("SQL.T_TRM.vial") +"="+campos.getString("SQL.T_VIA.identificador"));
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
            join.add("INNER");
            join.add("T_TVI");
            join.add(campos.getString("SQL.T_VIA.tipo")+"="+campos.getString("SQL.T_TVI.codigo"));
            join.add("false");

            sql = bd.join(parteFrom, parteWhere, (String[]) join.toArray(new String[]{})) +
                    " ORDER BY "+campos.getString("SQL.T_VIA.tipo")+ "," + campos.getString("SQL.P_FDD.vial")
                    + "," + campos.getString("SQL.T_TRM.e_singularNUC")
                    +","+campos.getString("SQL.P_FDD.tipoNumeracion");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            Vector vias = new Vector();
            Vector numeraciones = new Vector();
            Vector tramos = new Vector();
            GeneralValueObject via = null;
            GeneralValueObject ultimaVia = null;
            GeneralValueObject numeracion = null;
            GeneralValueObject ultimaNumeracion = null;
            String codViaAnterior = "";
            String numeracionAnterior = "";
            // KR -> añadir esi a la descripcion de via
            String codESIAnt ="";

            while (rs.next()) {
                String codVia = rs.getString(campos.getString("SQL.P_FDD.vial"));
                // KR -> añadir eco, esi y nucleo a la descripcion de via
                String codESI = rs.getString(campos.getString("SQL.T_ESI.identificador"));

                // KR <- if(codVia.equals(codViaAnterior) ){
                // KR -> añadir esi a la descripcion de via
                if(codVia.equals(codViaAnterior) && codESI.equals(codESIAnt) ){

                    String tipoNumeracion = rs.getString(campos.getString("SQL.P_FDD.tipoNumeracion"));
                    if(tipoNumeracion.equals(numeracionAnterior)){
                        GeneralValueObject tramo = new GeneralValueObject();
                        // TRAMO
                        tramo.setAtributo("numDesde", rs.getString(campos.getString("SQL.P_FDD.numeroDesde")));
                        tramo.setAtributo("letraDesde", rs.getString(campos.getString("SQL.P_FDD.letraDesde")));
                        tramo.setAtributo("numHasta", rs.getString(campos.getString("SQL.P_FDD.numeroHasta")));
                        tramo.setAtributo("letraHasta", rs.getString(campos.getString("SQL.P_FDD.letraHasta")));
                        tramo.setAtributo("habitantes", rs.getString(campos.getString("SQL.P_FDD.habitantes")));
                        tramos.add(tramo);
                    }else{
                        // HA CAMBIADO EL TIPO DE NUMERACION
                        if(ultimaNumeracion!=null){
                            ultimaNumeracion.setAtributo("tramos",tramos);
                            numeraciones.add(ultimaNumeracion);
                        }
                        numeracion = new GeneralValueObject();
                        //m_Log.debug("Tipo Numeracion: "+tipoNumeracion);
                        numeracion.setAtributo("tipoNumeracion",tipoNumeracion);
                        tramos = new Vector();
                        GeneralValueObject tramo = new GeneralValueObject();
                        // TRAMO
                        tramo.setAtributo("numDesde", rs.getString(campos.getString("SQL.P_FDD.numeroDesde")));
                        tramo.setAtributo("letraDesde", rs.getString(campos.getString("SQL.P_FDD.letraDesde")));
                        tramo.setAtributo("numHasta", rs.getString(campos.getString("SQL.P_FDD.numeroHasta")));
                        tramo.setAtributo("letraHasta", rs.getString(campos.getString("SQL.P_FDD.letraHasta")));
                        tramo.setAtributo("habitantes", rs.getString(campos.getString("SQL.P_FDD.habitantes")));
                        tramos.add(tramo);
                        ultimaNumeracion = numeracion;
                        numeracionAnterior = tipoNumeracion;
                    }
                }else{
                    // HE CAMBIADO DE VIA
                    if(ultimaVia!=null){
                        if(ultimaNumeracion!=null){
                            ultimaNumeracion.setAtributo("tramos",tramos);
                            numeraciones.add(ultimaNumeracion);
                        }
                        ultimaVia.setAtributo("numeraciones",numeraciones);
                        vias.add(ultimaVia);
                        ultimaNumeracion=null;
                        numeracionAnterior="";
                    }
                    via = new GeneralValueObject();
                    //m_Log.debug("Via: "+codVia);
                    via.setAtributo("codVia",codVia);
                    via.setAtributo("cinVia",rs.getString(campos.getString("SQL.T_VIA.codVia")));
                    via.setAtributo("descVia",rs.getString(campos.getString("SQL.T_VIA.nombreVia")));
                    String tipoVia = rs.getString(campos.getString("SQL.T_TVI.tipoVia"));
                    via.setAtributo("tipo",tipoVia);
                    // KR -> añadir (eco, ....)
                    via.setAtributo("codECO",rs.getString(campos.getString("SQL.T_ECO.identificador")));
                    via.setAtributo("descECO",rs.getString(campos.getString("SQL.T_ECO.nombre")));
                    via.setAtributo("codESI",rs.getString(campos.getString("SQL.T_ESI.identificador")));
                    via.setAtributo("descESI",rs.getString(campos.getString("SQL.T_ESI.nombre")));
                    /* Cambio combo vial *
                 via.setAtributo("codNUC",rs.getString(campos.getString("SQL.T_NUC.codigo")));
                 via.setAtributo("descNUC",rs.getString(campos.getString("SQL.T_NUC.nombre")));
                 via.setAtributo("idNUC",rs.getString(campos.getString("SQL.T_NUC.ine")));
                 if(!via.getAtributo("idNUC").equals("")){ // SI TIENE UN NUCLEO LE AÑADO EL NOMBRE
                   String nombreVia = via.getAtributo("descVia")+" - "+via.getAtributo("descNUC");
                   if((via.getAtributo("idNUC").equals("99"))||(via.getAtributo("idNUC").equals("0"))){
                     nombreVia = via.getAtributo("descVia")+" - "+via.getAtributo("descESI");
                   }
                   */
                    if ("SIN TIPO DE VIA".equals(tipoVia))
                        tipoVia="";
                    else tipoVia = tipoVia + " ";
                    String nombreVia = tipoVia + via.getAtributo("descVia")+" - "+via.getAtributo("descESI");
                    /* Fin cambio combo vial */
                    via.setAtributo("descVia", nombreVia);

                    numeraciones = new Vector();
                    String tipoNumeracion = rs.getString(campos.getString("SQL.P_FDD.tipoNumeracion"));
                    if(tipoNumeracion.equals(numeracionAnterior)){
                        GeneralValueObject tramo = new GeneralValueObject();
                        // TRAMO
                        tramo.setAtributo("numDesde", rs.getString(campos.getString("SQL.P_FDD.numeroDesde")));
                        tramo.setAtributo("letraDesde", rs.getString(campos.getString("SQL.P_FDD.letraDesde")));
                        tramo.setAtributo("numHasta", rs.getString(campos.getString("SQL.P_FDD.numeroHasta")));
                        tramo.setAtributo("letraHasta", rs.getString(campos.getString("SQL.P_FDD.letraHasta")));
                        tramo.setAtributo("habitantes", rs.getString(campos.getString("SQL.P_FDD.habitantes")));
                        tramos.add(tramo);
                    }else{
                        // HA CAMBIADO EL TIPO DE NUMERACION
                        if(ultimaNumeracion!=null){
                            ultimaNumeracion.setAtributo("tramos",tramos);
                            numeraciones.add(ultimaNumeracion);
                        }
                        numeracion = new GeneralValueObject();
                        //m_Log.debug("Tipo Numeracion: "+tipoNumeracion);
                        numeracion.setAtributo("tipoNumeracion",tipoNumeracion);
                        tramos = new Vector();
                        GeneralValueObject tramo = new GeneralValueObject();
                        // TRAMO
                        tramo.setAtributo("numDesde", rs.getString(campos.getString("SQL.P_FDD.numeroDesde")));
                        tramo.setAtributo("letraDesde", rs.getString(campos.getString("SQL.P_FDD.letraDesde")));
                        tramo.setAtributo("numHasta", rs.getString(campos.getString("SQL.P_FDD.numeroHasta")));
                        tramo.setAtributo("letraHasta", rs.getString(campos.getString("SQL.P_FDD.letraHasta")));
                        tramo.setAtributo("habitantes", rs.getString(campos.getString("SQL.P_FDD.habitantes")));
                        tramos.add(tramo);
                        ultimaNumeracion = numeracion;
                        numeracionAnterior = tipoNumeracion;
                    }
                    ultimaVia = via;
                    codViaAnterior = codVia;
                    codESIAnt = codESI;

                }
            }
            rs.close();
            if(ultimaVia!=null){
                if(ultimaNumeracion!=null){
                    ultimaNumeracion.setAtributo("tramos",tramos);
                    numeraciones.add(ultimaNumeracion);
                }
                ultimaVia.setAtributo("numeraciones",numeraciones);
                vias.add(ultimaVia);
            }
            resultado.setAtributo("vias",vias);
            sql = "SELECT P_FDS.*,A_USU.*,"+
                    bd.convertir("P_OPE."+campos.getString("SQL.P_FDS.fechaProceso"),bd.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+" AS FECHA,"+
                    "DISTRITOOR."+campos.getString("SQL.T_DIS.codigo")+" AS CODDISOR,"+
                    "DISTRITOOR."+campos.getString("SQL.T_DIS.nOficial")+" AS NOMDISOR,"+
                    "SECCIONOR."+campos.getString("SQL.T_SEC.codigo")+" AS CODSECOR,"+
                    "SECCIONOR."+campos.getString("SQL.T_SEC.letra")+" AS LETSECOR,"+
                    "SECCIONOR."+campos.getString("SQL.T_SEC.nOficial")+" AS NOMSECOR,"+
                    "DISTRITODE."+campos.getString("SQL.T_DIS.codigo")+" AS CODDISDE,"+
                    "DISTRITODE."+campos.getString("SQL.T_DIS.nOficial")+" AS NOMDISDE,"+
                    "SECCIONDE."+campos.getString("SQL.T_SEC.codigo")+" AS CODSECDE,"+
                    "SECCIONDE."+campos.getString("SQL.T_SEC.letra")+" AS LETSECDE,"+
                    "SECCIONDE."+campos.getString("SQL.T_SEC.nOficial")+" AS NOMSECDE "+
                    "FROM P_FDS,P_FDD,"+GlobalNames.ESQUEMA_GENERICO+"A_USU A_USU,T_DIS DISTRITOOR,T_DIS DISTRITODE,T_SEC SECCIONOR,"+
                    "T_SEC SECCIONDE WHERE "+
                    campos.getString("SQL.P_FDS.usuario")+"="+campos.getString("SQL.A_USU.codigo")+" AND "+
                    campos.getString("SQL.P_FDS.paisOrigen")+"= DISTRITOOR."+campos.getString("SQL.T_DIS.pais")+" AND "+
                    campos.getString("SQL.P_FDS.provinciaOrigen")+"= DISTRITOOR."+campos.getString("SQL.T_DIS.provincia")+" AND "+
                    campos.getString("SQL.P_FDS.municipioOrigen")+"= DISTRITOOR."+campos.getString("SQL.T_DIS.municipio")+" AND "+
                    campos.getString("SQL.P_FDS.distritoOrigen")+"= DISTRITOOR."+campos.getString("SQL.T_DIS.codigo")+" AND "+
                    campos.getString("SQL.P_FDS.paisOrigen")+"= SECCIONOR."+campos.getString("SQL.T_SEC.pais")+" AND "+
                    campos.getString("SQL.P_FDS.provinciaOrigen")+"= SECCIONOR."+campos.getString("SQL.T_SEC.provincia")+" AND "+
                    campos.getString("SQL.P_FDS.municipioOrigen")+"= SECCIONOR."+campos.getString("SQL.T_SEC.municipio")+" AND "+
                    campos.getString("SQL.P_FDS.distritoOrigen")+"= SECCIONOR."+campos.getString("SQL.T_SEC.distrito")+" AND "+
                    campos.getString("SQL.P_FDS.seccionOrigen")+"= SECCIONOR."+campos.getString("SQL.T_SEC.codigo")+" AND "+
                    campos.getString("SQL.P_FDS.letraOrigen")+"= SECCIONOR."+campos.getString("SQL.T_SEC.letra")+" AND "+
                    campos.getString("SQL.P_FDS.paisDestino")+"= DISTRITODE."+campos.getString("SQL.T_DIS.pais")+" AND "+
                    campos.getString("SQL.P_FDS.provinciaDestino")+"= DISTRITODE."+campos.getString("SQL.T_DIS.provincia")+" AND "+
                    campos.getString("SQL.P_FDS.municipioDestino")+"= DISTRITODE."+campos.getString("SQL.T_DIS.municipio")+" AND "+
                    campos.getString("SQL.P_FDS.distritoDestino")+"= DISTRITODE."+campos.getString("SQL.T_DIS.codigo")+" AND "+
                    campos.getString("SQL.P_FDS.paisDestino")+"= SECCIONDE."+campos.getString("SQL.T_SEC.pais")+" AND "+
                    campos.getString("SQL.P_FDS.provinciaDestino")+"= SECCIONDE."+campos.getString("SQL.T_SEC.provincia")+" AND "+
                    campos.getString("SQL.P_FDS.municipioDestino")+"= SECCIONDE."+campos.getString("SQL.T_SEC.municipio")+" AND "+
                    campos.getString("SQL.P_FDS.distritoDestino")+"= SECCIONDE."+campos.getString("SQL.T_SEC.distrito")+" AND "+
                    campos.getString("SQL.P_FDS.seccionDestino")+"= SECCIONDE."+campos.getString("SQL.T_SEC.codigo")+" AND "+
                    campos.getString("SQL.P_FDS.letraDestino")+"= SECCIONDE."+campos.getString("SQL.T_SEC.letra")+" AND "+
                    campos.getString("SQL.P_FDD.proceso")+"="+gVO.getAtributo("codProceso")+" AND "+
                    campos.getString("SQL.P_FDD.proceso")+"="+campos.getString("SQL.P_FDS.codigo");
            //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            if(rs.next()){
                String codProceso = rs.getString(campos.getString("SQL.P_FDS.codigo"));
                resultado.setAtributo("codProceso",codProceso);
                resultado.setAtributo("descripcion",rs.getString(campos.getString("SQL.P_FDS.descripcion")));
                resultado.setAtributo("fechaProceso",rs.getString("FECHA"));
                resultado.setAtributo("codUsuario",rs.getString(campos.getString("SQL.A_USU.codigo")));
                resultado.setAtributo("descUsuario",rs.getString(campos.getString("SQL.A_USU.nombre")));
                resultado.setAtributo("estado",rs.getString(campos.getString("SQL.P_FDS.estado")));
                resultado.setAtributo("codDistritoOrigen",rs.getString("CODDISOR"));
                resultado.setAtributo("descDistritoOrigen",rs.getString("NOMDISOR"));
                resultado.setAtributo("codSeccionOrigen",rs.getString("CODSECOR"));
                resultado.setAtributo("descSeccionOrigen",rs.getString("NOMSECOR"));
                resultado.setAtributo("letraOrigen",rs.getString("LETSECOR"));
                resultado.setAtributo("codDistritoDestino",rs.getString("CODDISDE"));
                resultado.setAtributo("descDistritoDestino",rs.getString("NOMDISDE"));
                resultado.setAtributo("codSeccionDestino",rs.getString("CODSECDE"));
                resultado.setAtributo("descSeccionDestino",rs.getString("NOMSECDE"));
                resultado.setAtributo("letraDestino",rs.getString("LETSECDE"));

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
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en FusionDivisionDAO.verDetalle");
            }
        }
        return resultado;
    }

    private void insertarTramoOrigen(Connection con,GeneralValueObject gVO,GeneralValueObject tramo,AdaptadorSQLBD abd) throws Exception{
        String sql = "";
        Statement stmt = con.createStatement();
        ResultSet rs = null;

        sql = "SELECT "+abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{
                abd.convertir(campos.getString("SQL.T_TRM.codigo"),abd.CONVERTIR_COLUMNA_NUMERO,null)})+" AS MAXIMO"+
                " FROM T_TRM WHERE "+
                campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("idVia")+" AND "+
                campos.getString("SQL.T_TRM.tipoNumeracion")+"="+gVO.getAtributo("codTipoNumeracion");
        rs=stmt.executeQuery(sql);
        int idNuevoTramo = 0;
        if(rs.next()){
            idNuevoTramo = rs.getInt("MAXIMO");
        }
        rs.close();
        idNuevoTramo++;
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
                campos.getString("SQL.T_TRM.vial")+","+
                idNuevoTramo+","+
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
                tramo.getAtributo("distrito")+","+
                tramo.getAtributo("seccion")+",'"+
                tramo.getAtributo("letra")+"',"+
                campos.getString("SQL.T_TRM.paisSubseccion")+","+
                campos.getString("SQL.T_TRM.provinciaSubseccion")+","+
                campos.getString("SQL.T_TRM.municipioSubseccion")+","+
                campos.getString("SQL.T_TRM.distritoSubseccion")+","+
                campos.getString("SQL.T_TRM.seccionSubseccion")+","+
                campos.getString("SQL.T_TRM.letraSubseccion")+","+
                campos.getString("SQL.T_TRM.subseccion")+","+
                campos.getString("SQL.T_TRM.codigoPostal")+","+
                tramo.getAtributo("numDesde")+",'"+
                tramo.getAtributo("letraDesde")+"',"+
                tramo.getAtributo("numHasta")+",'"+
                tramo.getAtributo("letraHasta")+"',"+
                campos.getString("SQL.T_TRM.paisNUC")+","+
                campos.getString("SQL.T_TRM.provinciaNUC")+","+
                campos.getString("SQL.T_TRM.municipioNUC")+","+
                campos.getString("SQL.T_TRM.e_singularNUC")+","+
                campos.getString("SQL.T_TRM.nucleo")+
                " FROM T_TRM WHERE "+
                campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("idVia")+" AND "+
                campos.getString("SQL.T_TRM.tipoNumeracion")+"="+gVO.getAtributo("codTipoNumeracion")+" AND "+
                campos.getString("SQL.T_TRM.codigo")+"="+tramo.getAtributo("codTramo");
        //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt.executeUpdate(sql);
        tramo.setAtributo("codTramo",String.valueOf(idNuevoTramo));
        stmt.close();
    }

    private void insertarTramoDestino(Connection con,GeneralValueObject gVO,GeneralValueObject tramo,AdaptadorSQLBD abd) throws Exception{
        String sql = "";
        Statement stmt = con.createStatement();
        ResultSet rs = null;
        sql = "SELECT "+abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{
                abd.convertir(campos.getString("SQL.T_TRM.codigo"),abd.CONVERTIR_COLUMNA_NUMERO,null)})+" AS MAXIMO"+
                " FROM T_TRM WHERE "+
                campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("idVia")+" AND "+
                campos.getString("SQL.T_TRM.tipoNumeracion")+"="+gVO.getAtributo("codTipoNumeracion");
        rs=stmt.executeQuery(sql);
        int idNuevoTramo = 0;
        if(rs.next()){
            idNuevoTramo = rs.getInt("MAXIMO");
        }
        rs.close();
        idNuevoTramo++;
        sql = "INSERT INTO T_TRM("+
                campos.getString("SQL.T_TRM.tipoNumeracion")+","+
                campos.getString("SQL.T_TRM.pais")+","+
                campos.getString("SQL.T_TRM.provincia")+","+
                campos.getString("SQL.T_TRM.municipio")+","+
                campos.getString("SQL.T_TRM.vial")+","+
                campos.getString("SQL.T_TRM.codigo")+","+
                campos.getString("SQL.T_TRM.paisSeccion")+","+
                campos.getString("SQL.T_TRM.provinciaSeccion")+","+
                campos.getString("SQL.T_TRM.municipioSeccion")+","+
                campos.getString("SQL.T_TRM.distritoSeccion")+","+
                campos.getString("SQL.T_TRM.seccion")+","+
                campos.getString("SQL.T_TRM.letraSeccion")+","+
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
                campos.getString("SQL.T_TRM.vial")+","+
                idNuevoTramo+","+
                campos.getString("SQL.T_TRM.paisSeccion")+","+
                campos.getString("SQL.T_TRM.provinciaSeccion")+","+
                campos.getString("SQL.T_TRM.municipioSeccion")+","+
                tramo.getAtributo("distrito")+","+
                tramo.getAtributo("seccion")+",'"+
                tramo.getAtributo("letra")+"',"+
                campos.getString("SQL.T_TRM.codigoPostal")+","+
                tramo.getAtributo("numDesde")+",'"+
                tramo.getAtributo("letraDesde")+"',"+
                tramo.getAtributo("numHasta")+",'"+
                tramo.getAtributo("letraHasta")+"',"+
                campos.getString("SQL.T_TRM.paisNUC")+","+
                campos.getString("SQL.T_TRM.provinciaNUC")+","+
                campos.getString("SQL.T_TRM.municipioNUC")+","+
                campos.getString("SQL.T_TRM.e_singularNUC")+","+
                campos.getString("SQL.T_TRM.nucleo")+
                " FROM T_TRM WHERE "+
                campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("idVia")+" AND "+
                campos.getString("SQL.T_TRM.tipoNumeracion")+"="+gVO.getAtributo("codTipoNumeracion")+" AND "+
                campos.getString("SQL.T_TRM.codigo")+"="+tramo.getAtributo("codTramo");
        //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt.executeUpdate(sql);
        tramo.setAtributo("codTramo",String.valueOf(idNuevoTramo));
        stmt.close();
    }


    public String procesarProceso(String[] params,GeneralValueObject gVO){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        String correcto = "SI";
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            stmt = conexion.createStatement();
            // ACTUALIZO EL TRAMERO
            String todos = (String)gVO.getAtributo("traspasarTodos");
            String pasarVial = (String)gVO.getAtributo("traspasarVial");

            if(todos.equals("SI")||pasarVial.equals("SI")){
                //m_Log.debug("FusionDivisionDAO:fusion completa");
                if(m_Log.isDebugEnabled()) m_Log.debug("FusionDivisionDAO:fusion completa");
                actualizarSeccionCompleta(conexion,gVO,abd);
            }else{
                if(m_Log.isDebugEnabled()) m_Log.debug("FusionDivisionDAO:actualizar tramero");
                actualizarTramero(conexion,gVO,abd);
            }
            // ACTUALIZO LAS DIRECCIONES SUELO AFECTADAS
            actualizarDSUs(conexion,gVO,abd);
            // ACTUALIZO LAS DIRECCIONES POSTALES AFECTADAS
            actualizarDomicilios(stmt,gVO,abd);
            stmt.close();
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
            if ("DSU_SIN_TRAM0".equals ((String) gVO.getAtributo("error")))
                correcto ="DSU_SIN_TRAM0";
            else correcto = "NO";
        }finally{
            commitTransaction(abd,conexion);
            //rollBackTransaction(abd,conexion,new Exception(" NUESTRA PRUEBA..............."));
        }
        return correcto;
    }

    private void insertarP_FDD(Connection con,GeneralValueObject gVO,
                               GeneralValueObject tramo,AdaptadorSQLBD abd) throws Exception{
        String sql = "";
        Statement stmt = con.createStatement();
        ResultSet rs = null;
        sql = "SELECT "+abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.P_FDD.codigo")})+" AS MAXIMO"+
                " FROM P_FDD WHERE "+
                campos.getString("SQL.P_FDD.proceso")+"="+gVO.getAtributo("codProceso");
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        rs=stmt.executeQuery(sql);
        int idNuevoFDD = 0;
        if(rs.next()){
            idNuevoFDD = rs.getInt("MAXIMO");
        }
        rs.close();
        idNuevoFDD++;
        sql = "INSERT INTO P_FDD("+
                campos.getString("SQL.P_FDD.proceso") + "," +
                campos.getString("SQL.P_FDD.codigo") + "," +
                campos.getString("SQL.P_FDD.pais") + "," +
                campos.getString("SQL.P_FDD.provincia") + "," +
                campos.getString("SQL.P_FDD.municipio") + "," +
                campos.getString("SQL.P_FDD.vial") + "," +
                campos.getString("SQL.P_FDD.tipoNumeracion") + "," +
                campos.getString("SQL.P_FDD.numeroDesde") + "," +
                campos.getString("SQL.P_FDD.letraDesde") + "," +
                campos.getString("SQL.P_FDD.numeroHasta") + "," +
                campos.getString("SQL.P_FDD.letraHasta") + "," +
                campos.getString("SQL.P_FDD.habitantes") +
                ") VALUES (" +
                gVO.getAtributo("codProceso")+","+
                idNuevoFDD+","+
                gVO.getAtributo("codPais")+","+
                gVO.getAtributo("codProvincia")+","+
                gVO.getAtributo("codMunicipio")+",";
        String via = gVO.getAtributo("idVia")+","+
                gVO.getAtributo("codTipoNumeracion")+",";
        String todos = (String)gVO.getAtributo("traspasarTodos");
        String pasarVial = (String)gVO.getAtributo("traspasarVial");
        if(todos.equals("SI")||pasarVial.equals("SI")){
            via = tramo.getAtributo("idVia")+","+
                    tramo.getAtributo("tipoNumeracion")+",";
        }
        sql+= via+
                tramo.getAtributo("numDesde")+",'"+
                tramo.getAtributo("letraDesde")+"',"+
                tramo.getAtributo("numHasta")+",'"+
                tramo.getAtributo("letraHasta")+"',0)";
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt.executeUpdate(sql);
        tramo.setAtributo("codFDD",String.valueOf(idNuevoFDD));
        stmt.close();
    }


    private void actualizarTramero(Connection con,GeneralValueObject gVO,AdaptadorSQLBD abd)
            throws Exception {
        String sql = "";
        Statement stmt = con.createStatement();
        ResultSet rs = null;
        Vector tramosOrigen = (Vector)gVO.getAtributo("tramosOrigen");
        Vector tramosDestino = (Vector)gVO.getAtributo("tramosDestino");
        Vector tramosModificados = new Vector();
        int i=0;
        for(i=0;i<tramosOrigen.size();i++){
            GeneralValueObject tramo = (GeneralValueObject)tramosOrigen.get(i);
            String accion = (String)tramo.getAtributo("accion");
            if(accion.equals("E")){
                // DAR DE BAJA DICHO TRAMO
                tramosModificados.add(tramo);
                sql = "UPDATE T_TRM SET " +
                        campos.getString("SQL.T_TRM.situacion")+"='B'"+
                        " WHERE " +
                        campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                        campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                        campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                        campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("idVia")+" AND "+
                        campos.getString("SQL.T_TRM.tipoNumeracion")+"="+gVO.getAtributo("codTipoNumeracion")+" AND "+
                        campos.getString("SQL.T_TRM.codigo")+"="+tramo.getAtributo("codTramo");
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
            }else if(accion.equals("C")){
                // CREAR DICHO TRAMO
                insertarTramoOrigen(con,gVO,tramo,abd);
            }
        } // Fin tramos del origen

        gVO.setAtributo("tramosModificados",tramosModificados);
        for(i=0;i<tramosDestino.size();i++){
            GeneralValueObject tramo = (GeneralValueObject)tramosDestino.get(i);
            String accion = (String)tramo.getAtributo("accion");
            if(accion.equals("C")){
                // CREAR DICHO TRAMO
                insertarTramoDestino(con,gVO,tramo,abd);
                // INSERTO EN LA TABLA DE PROCESOS
                insertarP_FDD(con,gVO,tramo,abd);
            }
        }// FIN TRAMOS DESTINO

        // AÑADO LOS TRAMOS DEL ORIGEN CREADOS EN EL VECTOR DE TRAMOS DESTINO PARA LA
        // POSTERIOR BUSQUEDA DE TRAMOS
        for(i=0;i<tramosOrigen.size();i++){
            GeneralValueObject tramo = (GeneralValueObject)tramosOrigen.get(i);
            String accion = (String)tramo.getAtributo("accion");
            if(accion.equals("C")){
                tramosDestino.add(tramo);
            }
        }
        stmt.close();
    }

    private void actualizarSeccionCompleta(Connection con,GeneralValueObject gVO,AdaptadorSQLBD abd)
            throws Exception {
        Statement stmt = con.createStatement();
        Statement stmt1 = con.createStatement();
        String sql = "";
        ResultSet rs = null;
        ResultSet rs1 = null;
        Vector tramosModificados = new Vector();
        Vector tramosDestino = new Vector();
        int i=0;
        sql = "SELECT T_TRM.* FROM T_TRM " +
                " WHERE " +
                campos.getString("SQL.T_TRM.situacion")+"='A' AND " +
                campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_TRM.distritoSeccion")+"="+gVO.getAtributo("distritoOrigen")+" AND "+
                campos.getString("SQL.T_TRM.seccion")+"="+gVO.getAtributo("seccionOrigen")+" AND "+
                campos.getString("SQL.T_TRM.letraSeccion")+"='"+gVO.getAtributo("letraOrigen")+"'";
        if(gVO.getAtributo("traspasarVial").equals("SI")&& !gVO.getAtributo("traspasarTodos").equals("SI")){
            sql+= " AND "+campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("idVia");
            /* Cambiar combo vial *
       sql+=" AND " +
           campos.getString("SQL.T_TRM.e_singularNUC")+"=" + gVO.getAtributo("codESI")+ " AND " +
           campos.getString("SQL.T_TRM.nucleo")+"=" + gVO.getAtributo("codNUC");
       * Fin cambiar combo vial */
        }
        sql+=  " ORDER BY "+campos.getString("SQL.T_TRM.vial")+","+campos.getString("SQL.T_TRM.tipoNumeracion")+","+
                campos.getString("SQL.T_TRM.codigo");
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        rs = stmt.executeQuery(sql);
        while(rs.next()){
            // ESTE ES EL TRAMO ORIGINAL
            GeneralValueObject tramo = new GeneralValueObject();
            tramo.setAtributo("codTramo",rs.getString(campos.getString("SQL.T_TRM.codigo")));
            tramo.setAtributo("idVia",rs.getString(campos.getString("SQL.T_TRM.vial")));
            tramo.setAtributo("tipoNumeracion",rs.getString(campos.getString("SQL.T_TRM.tipoNumeracion")));
            tramo.setAtributo("distrito",rs.getString(campos.getString("SQL.T_TRM.distritoSeccion")));
            tramo.setAtributo("seccion",rs.getString(campos.getString("SQL.T_TRM.seccion")));
            tramo.setAtributo("letra",rs.getString(campos.getString("SQL.T_TRM.letraSeccion")));
            tramo.setAtributo("numDesde",rs.getString(campos.getString("SQL.T_TRM.primerNumero")));
            tramo.setAtributo("letraDesde",rs.getString(campos.getString("SQL.T_TRM.primeraLetra")));
            tramo.setAtributo("numHasta",rs.getString(campos.getString("SQL.T_TRM.ultimoNumero")));
            tramo.setAtributo("letraHasta",rs.getString(campos.getString("SQL.T_TRM.ultimaLetra")));
            tramo.setAtributo("codESI",rs.getString(campos.getString("SQL.T_TRM.e_singularNUC")));
            tramo.setAtributo("codNUC",rs.getString(campos.getString("SQL.T_TRM.nucleo")));

            //tramo.setAtributo("accion",temp.substring(0,temp.length()-1));
            tramosModificados.add(tramo);

            // ESTE ES EL TRAMO DE DESTINO
            GeneralValueObject tramoDestino = new GeneralValueObject();
            tramoDestino.setAtributo("codTramo",rs.getString(campos.getString("SQL.T_TRM.codigo")));
            tramoDestino.setAtributo("idVia",rs.getString(campos.getString("SQL.T_TRM.vial")));
            tramoDestino.setAtributo("tipoNumeracion",rs.getString(campos.getString("SQL.T_TRM.tipoNumeracion")));
            tramoDestino.setAtributo("distrito",rs.getString(campos.getString("SQL.T_TRM.distritoSeccion")));
            tramoDestino.setAtributo("seccion",rs.getString(campos.getString("SQL.T_TRM.seccion")));
            tramoDestino.setAtributo("letra",rs.getString(campos.getString("SQL.T_TRM.letraSeccion")));
            tramoDestino.setAtributo("numDesde",rs.getString(campos.getString("SQL.T_TRM.primerNumero")));
            tramoDestino.setAtributo("letraDesde",rs.getString(campos.getString("SQL.T_TRM.primeraLetra")));
            tramoDestino.setAtributo("numHasta",rs.getString(campos.getString("SQL.T_TRM.ultimoNumero")));
            tramoDestino.setAtributo("letraHasta",rs.getString(campos.getString("SQL.T_TRM.ultimaLetra")));
            // DOY DE BAJA EL TRAMO
            sql = "UPDATE T_TRM SET " +
                    campos.getString("SQL.T_TRM.situacion")+"='B'"+
                    " WHERE " +
                    campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_TRM.vial")+"="+rs.getString(campos.getString("SQL.T_TRM.vial"))+" AND "+
                    campos.getString("SQL.T_TRM.tipoNumeracion")+"="+rs.getString(campos.getString("SQL.T_TRM.tipoNumeracion"))+" AND "+
                    campos.getString("SQL.T_TRM.codigo")+"="+rs.getString(campos.getString("SQL.T_TRM.codigo"));
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt1.executeUpdate(sql);
            // CREAR EL NUEVO TRAMO
            sql = "SELECT "+abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{
                    abd.convertir(campos.getString("SQL.T_TRM.codigo"),abd.CONVERTIR_COLUMNA_NUMERO,null)})+" AS MAXIMO"+
                    " FROM T_TRM WHERE "+
                    campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_TRM.vial")+"="+rs.getString(campos.getString("SQL.T_TRM.vial"))+" AND "+
                    campos.getString("SQL.T_TRM.tipoNumeracion")+"="+rs.getString(campos.getString("SQL.T_TRM.tipoNumeracion"));
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs1=stmt1.executeQuery(sql);
            int idNuevoTramo = 0;
            if(rs1.next()){
                idNuevoTramo = rs1.getInt("MAXIMO");
            }
            rs1.close();
            idNuevoTramo++;
            sql = "INSERT INTO T_TRM("+
                    campos.getString("SQL.T_TRM.tipoNumeracion")+","+
                    campos.getString("SQL.T_TRM.pais")+","+
                    campos.getString("SQL.T_TRM.provincia")+","+
                    campos.getString("SQL.T_TRM.municipio")+","+
                    campos.getString("SQL.T_TRM.vial")+","+
                    campos.getString("SQL.T_TRM.codigo")+","+
                    campos.getString("SQL.T_TRM.paisSeccion")+","+
                    campos.getString("SQL.T_TRM.provinciaSeccion")+","+
                    campos.getString("SQL.T_TRM.municipioSeccion")+","+
                    campos.getString("SQL.T_TRM.distritoSeccion")+","+
                    campos.getString("SQL.T_TRM.seccion")+","+
                    campos.getString("SQL.T_TRM.letraSeccion")+","+
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
                    campos.getString("SQL.T_TRM.vial")+","+
                    idNuevoTramo+","+
                    campos.getString("SQL.T_TRM.paisSeccion")+","+
                    campos.getString("SQL.T_TRM.provinciaSeccion")+","+
                    campos.getString("SQL.T_TRM.municipioSeccion")+","+
                    gVO.getAtributo("distritoDestino")+","+
                    gVO.getAtributo("seccionDestino")+",'"+
                    gVO.getAtributo("letraDestino")+"',"+
                    campos.getString("SQL.T_TRM.codigoPostal")+","+
                    tramo.getAtributo("numDesde")+",'"+
                    tramo.getAtributo("letraDesde")+"',"+
                    tramo.getAtributo("numHasta")+",'"+
                    tramo.getAtributo("letraHasta")+"',"+
                    campos.getString("SQL.T_TRM.paisNUC")+","+
                    campos.getString("SQL.T_TRM.provinciaNUC")+","+
                    campos.getString("SQL.T_TRM.municipioNUC")+","+
                    campos.getString("SQL.T_TRM.e_singularNUC")+","+
                    campos.getString("SQL.T_TRM.nucleo")+
                    " FROM T_TRM WHERE "+
                    campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_TRM.vial")+"="+rs.getString(campos.getString("SQL.T_TRM.vial"))+" AND "+
                    campos.getString("SQL.T_TRM.tipoNumeracion")+"="+rs.getString(campos.getString("SQL.T_TRM.tipoNumeracion"))+" AND "+
                    campos.getString("SQL.T_TRM.codigo")+"="+rs.getString(campos.getString("SQL.T_TRM.codigo"));
            //m_Log.debug(idNuevoTramo);
            stmt1.executeUpdate(sql);
            // INSERTO EN LA TABLA DE PROCESOS
            insertarP_FDD(con,gVO,tramoDestino,abd);
            tramoDestino.setAtributo("codTramo",String.valueOf(idNuevoTramo));
            tramosDestino.add(tramoDestino);
        }
        gVO.setAtributo("tramosModificados",tramosModificados);
        gVO.setAtributo("tramosDestino",tramosDestino);
        rs.close();
        rs1.close();
        stmt.close();
        stmt1.close();
    }

    private String[] buscarNuevoTramo(Statement stmt,ResultSet rs,GeneralValueObject gVO)
            throws Exception {
        String[] nuevoTramo = new String[3];
        ResultSet rs1 = null;
        String todos = (String)gVO.getAtributo("traspasarTodos");
        String sql = "";
        sql = "SELECT T_TRM.* FROM T_TRM WHERE "+
                campos.getString("SQL.T_TRM.situacion")+"='A' AND "+
                campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("idVia")+" AND "+
                campos.getString("SQL.T_TRM.tipoNumeracion")+"="+gVO.getAtributo("codTipoNumeracion");
        sql+=" AND " +
                campos.getString("SQL.T_TRM.e_singularNUC")+"=" + gVO.getAtributo("codESI");
        /* Cambio combo vial
                + " AND " + campos.getString("SQL.T_TRM.nucleo")+"=" + gVO.getAtributo("codNUC");
            *	Fin cambio combo vial */
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        rs1 = stmt.executeQuery(sql);
        int distritoDSU = rs.getInt(campos.getString("SQL.T_TRM.distritoSeccion"));
        int seccionDSU = rs.getInt(campos.getString("SQL.T_TRM.seccion"));
        String letraDSU = rs.getString(campos.getString("SQL.T_TRM.letraSeccion"));
        letraDSU = (letraDSU==null)?"":letraDSU;
        int numDesdeDSU = rs.getInt(campos.getString("SQL.T_DSU.numeroDesde"));
        String numHastaDSU1 = rs.getString(campos.getString("SQL.T_DSU.numeroHasta"));
        while(rs1.next()){
            int distrito = rs1.getInt(campos.getString("SQL.T_TRM.distritoSeccion"));
            int seccion = rs1.getInt(campos.getString("SQL.T_TRM.seccion"));
            String letra = rs1.getString(campos.getString("SQL.T_TRM.letraSeccion"));
            letra= (letra==null)?"":letra;
            int numDesde = rs1.getInt(campos.getString("SQL.T_TRM.primerNumero"));
            int numHasta = rs1.getInt(campos.getString("SQL.T_TRM.ultimoNumero"));
            String codTramo = rs1.getString(campos.getString("SQL.T_TRM.codigo"));
            if(numHastaDSU1!=null){
                int numHastaDSU = Integer.parseInt(numHastaDSU1);
                if((numDesdeDSU>=numDesde)&&(numDesdeDSU<=numHasta) &&
                        (numHastaDSU>=numDesde)&&(numHastaDSU<=numHasta)){
                    nuevoTramo[0] = codTramo;
                    nuevoTramo[1] = "NO";
                    if((distrito!=distritoDSU)||(seccion!=seccionDSU)||(!letra.equals(letraDSU))){
                        nuevoTramo[1] = "SI";
                    }
                    // BUSCO EL CODIGO DEL PROCESO ASOCIADO
                    Vector tramosDestino = (Vector)gVO.getAtributo("tramosDestino");
                    int i=0;
                    for(i=0;i<tramosDestino.size();i++){
                        GeneralValueObject tramo = (GeneralValueObject)tramosDestino.get(i);
                        if(m_Log.isDebugEnabled()) m_Log.debug("codTramo : "+codTramo+","+tramo.getAtributo("codTramo"));
                        if(codTramo.equals(tramo.getAtributo("codTramo"))){
                            nuevoTramo[2] = (String)tramo.getAtributo("codFDD");
                            break;
                        }
                    }
                    break;
                }
            }else{
                if((numDesdeDSU>=numDesde)&&(numDesdeDSU<=numHasta)){
                    nuevoTramo[0] = codTramo;
                    nuevoTramo[1] = "NO";
                    if((distrito!=distritoDSU)||(seccion!=seccionDSU)||(!letra.equals(letraDSU))){
                        nuevoTramo[1] = "SI";
                    }
                    // BUSCO EL CODIGO DEL PROCESO ASOCIADO
                    Vector tramosDestino = (Vector)gVO.getAtributo("tramosDestino");
                    int i=0;
                    for(i=0;i<tramosDestino.size();i++){
                        GeneralValueObject tramo = (GeneralValueObject)tramosDestino.get(i);
                        if(m_Log.isDebugEnabled()) m_Log.debug("codTramo : "+codTramo+","+tramo.getAtributo("codTramo"));
                        if(codTramo.equals(tramo.getAtributo("codTramo"))){
                            nuevoTramo[2] = (String)tramo.getAtributo("codFDD");
                            break;
                        }
                    }
                    break;
                }
            }
        }
        rs1.close();
        //m_Log.debug(nuevoTramo[2]);
        return nuevoTramo;
    }

    private void actualizarDSUs(Connection con,GeneralValueObject gVO,AdaptadorSQLBD abd)
            throws Exception {
        String sql = "";
        Statement stmt = con.createStatement();
        Statement stmt1 = con.createStatement();
        ResultSet rs = null;
        ResultSet rs1 = null;
        String todos = (String)gVO.getAtributo("traspasarTodos");
        String pasarVial = (String)gVO.getAtributo("traspasarVial");
        Vector tramosModificados = (Vector)gVO.getAtributo("tramosModificados");
        Vector hojas = new Vector();
        int i=0;
        for(i=0;i<tramosModificados.size();i++){
            GeneralValueObject tramo = (GeneralValueObject)tramosModificados.get(i);
            String idVia = (String)gVO.getAtributo("idVia");
            String tipoNumeracion = (String)gVO.getAtributo("codTipoNumeracion");
            if(todos.equals("SI")||pasarVial.equals("SI")){
                idVia = (String)tramo.getAtributo("idVia");
                tipoNumeracion = (String)tramo.getAtributo("tipoNumeracion");
                String codESI = (String)tramo.getAtributo("codESI");
                String codNUC = (String)tramo.getAtributo("codNUC");
                gVO.setAtributo("idVia", idVia);
                gVO.setAtributo("codTipoNumeracion", tipoNumeracion);
                gVO.setAtributo("codESI", codESI);
                gVO.setAtributo("codNUC", codNUC);
            }
            String parteFrom = " P_HOJ.*,T_DPO.*,T_DSU.*,T_TRM.*";
            String parteWhere = campos.getString("SQL.T_DSU.situacion")+"='A' AND " +
                    campos.getString("SQL.T_DSU.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_DSU.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_DSU.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_DSU.vial")+"="+idVia+" AND "+
                    campos.getString("SQL.T_DSU.paisTRM")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_DSU.provinciaTRM")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_DSU.municipioTRM")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_DSU.tipoNumeracionTRM")+"="+tipoNumeracion+" AND "+
                    campos.getString("SQL.T_DSU.codigoTRM")+"="+tramo.getAtributo("codTramo")+" AND "+
                    campos.getString("SQL.T_DSU.vialTRM")+"="+idVia;
            ArrayList join = new ArrayList();
            join.add("P_HOJ");
            join.add("RIGHT");
            join.add("T_DPO");
            join.add(campos.getString("SQL.P_HOJ.domicilio")+"="+campos.getString("SQL.T_DPO.domicilio"));
            join.add("INNER");
            join.add("T_DSU");
            join.add(campos.getString("SQL.T_DPO.suelo")+"="+campos.getString("SQL.T_DSU.identificador"));
            join.add("INNER");
            join.add("T_TRM");
            join.add(campos.getString("SQL.T_DSU.paisTRM")+"=" + campos.getString("SQL.T_TRM.pais")+ " AND " +
                    campos.getString("SQL.T_DSU.provinciaTRM")+"=" + campos.getString("SQL.T_TRM.provincia") + " AND " +
                    campos.getString("SQL.T_DSU.municipioTRM")+"=" + campos.getString("SQL.T_TRM.municipio") + " AND "+
                    campos.getString("SQL.T_DSU.vialTRM")+"=" + campos.getString("SQL.T_TRM.vial")+ " AND " +
                    campos.getString("SQL.T_DSU.codigoTRM")+"=" + campos.getString("SQL.T_TRM.codigo") + " AND " +
                    campos.getString("SQL.T_DSU.tipoNumeracionTRM")+"=" + campos.getString("SQL.T_TRM.tipoNumeracion"));
            join.add("false");
            if(!todos.equals("SI")){
                parteWhere+=" AND " +
                        campos.getString("SQL.T_TRM.e_singularNUC")+"=" + gVO.getAtributo("codESI");
                /* Cambio combo vial
              + " AND " + campos.getString("SQL.T_TRM.nucleo")+"=" + gVO.getAtributo("codNUC");
              * Fin cambio combo vial */
            }
            sql = abd.join(parteFrom, parteWhere, (String[]) join.toArray(new String[]{}));
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = stmt.executeQuery(sql);
            String idDSUAnterior = "";
            while(rs.next()){
                String idDSU = rs.getString(campos.getString("SQL.T_DSU.identificador"));
                String[] nuevoTramo = buscarNuevoTramo(stmt1,rs,gVO);
                if (nuevoTramo == null) {
                    gVO.setAtributo("error", "DSU_SIN_TRAM0");
                    throw new Exception("DSU_SIN_TRAM0");
                }
                if(!idDSU.equals(idDSUAnterior)){
                    // INSERTAR LA NUEVA DSU
                    gVO.setAtributo("nuevoTramo",nuevoTramo);
                    insertarT_DSU(con,rs,gVO,abd);
                    // DOY DE BAJA LA DIRECCION SUELO ANTIGUA
                    sql = "UPDATE T_DSU SET "+
                            campos.getString("SQL.T_DSU.situacion")+"='B',"+
                            campos.getString("SQL.T_DSU.fechaBaja")+"="+abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null)+","+
                            campos.getString("SQL.T_DSU.usuarioBaja")+"="+gVO.getAtributo("usuario")+
                            " WHERE "+
                            campos.getString("SQL.T_DSU.identificador")+"="+idDSU;
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    stmt1.executeUpdate(sql);
                    idDSUAnterior = idDSU;
                }
                GeneralValueObject hoja = new GeneralValueObject();
                String distrito = rs.getString(campos.getString("SQL.P_HOJ.distrito"));
                String seccion = rs.getString(campos.getString("SQL.P_HOJ.seccion"));
                String letra = rs.getString(campos.getString("SQL.P_HOJ.letra"));
                String numeroHoja = rs.getString(campos.getString("SQL.P_HOJ.numero"));
                String version = rs.getString(campos.getString("SQL.P_HOJ.version"));
                String contador = rs.getString(campos.getString("SQL.P_HOJ.contador"));
                hoja.setAtributo("haCambiadoDistrito",nuevoTramo[1]);
                hoja.setAtributo("codFDD",nuevoTramo[2]);
                hoja.setAtributo("codDSU",gVO.getAtributo("idNuevaDSU"));
                hoja.setAtributo("codDPO",rs.getString(campos.getString("SQL.T_DPO.domicilio")));
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
            }
            rs.close();
        }
        gVO.setAtributo("hojas",hojas);
        stmt1.close();
        stmt.close();
    }

    private void insertarT_DSU(Connection con,ResultSet rs,
                               GeneralValueObject gVO,AdaptadorSQLBD abd) throws Exception{
        String sql = "";
        Statement stmt1 = con.createStatement();
        ResultSet rs1 = null;
        String idDSU = rs.getString(campos.getString("SQL.T_DSU.identificador"));
        String[] nuevoTramo = (String[])gVO.getAtributo("nuevoTramo");
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
                campos.getString("SQL.T_DSU.vial")+","+
                idNuevaDSU+","+
                campos.getString("SQL.T_DSU.tipoNumeracion")+","+
                campos.getString("SQL.T_DSU.hoja")+","+
                campos.getString("SQL.T_DSU.parcela")+","+
                campos.getString("SQL.T_DSU.cpoPais")+","+
                campos.getString("SQL.T_DSU.cpoProvincia")+","+
                campos.getString("SQL.T_DSU.cpoMunicipio")+","+
                campos.getString("SQL.T_DSU.codigoPostal")+","+
                campos.getString("SQL.T_DSU.distritoPais")+","+
                campos.getString("SQL.T_DSU.distritoProvincia")+","+
                campos.getString("SQL.T_DSU.distritoMunicipio")+","+
                gVO.getAtributo("distritoDestino")+","+
                gVO.getAtributo("seccionDestino")+",'"+
                gVO.getAtributo("letraDestino")+"',"+
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
                "'A',"+
                abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null)+ ","+
                gVO.getAtributo("usuario")+","+
                "null,"+
                "null,"+
                abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null)+ ","+
                campos.getString("SQL.T_DSU.tipoNumeracionTRM")+","+
                campos.getString("SQL.T_DSU.paisTRM")+","+
                campos.getString("SQL.T_DSU.provinciaTRM")+","+
                campos.getString("SQL.T_DSU.municipioTRM")+","+
                campos.getString("SQL.T_DSU.vialTRM")+","+
                nuevoTramo[0]+
                " FROM T_DSU WHERE "+
                campos.getString("SQL.T_DSU.identificador")+"="+idDSU;
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt1.executeUpdate(sql);
        gVO.setAtributo("idNuevaDSU",String.valueOf(idNuevaDSU));
        rs1.close();
        stmt1.close();
    }

    private void actualizarDomicilios(Statement stmt,GeneralValueObject gVO,AdaptadorSQLBD abd)
            throws Exception {
        String sql = "";
        ResultSet rs = null;
        Vector hojas = (Vector)gVO.getAtributo("hojas");
        int numDSUs = hojas.size();
        int i=0;
        for(i=0;i<numDSUs;i++){
            GeneralValueObject hoja = (GeneralValueObject)hojas.get(i);
            String idNuevaDSU = (String)hoja.getAtributo("codDSU");
            String idDPO = (String)hoja.getAtributo("codDPO");
            int idNuevoDOM = 0;
            sql = "SELECT "+abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.T_DOM.idDomicilio")})+" AS MAXIMO"+
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
                    abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null)+ ","+
                    gVO.getAtributo("usuario")+","+
                    "null,"+
                    "null,"+
                    abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null)+
                    " FROM T_DPO WHERE "+
                    campos.getString("SQL.T_DPO.domicilio")+"="+idDPO;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            // DOY DE BAJA LA DIRECCION POSTAL ANTIGUA
            sql = "UPDATE T_DPO SET "+
                    campos.getString("SQL.T_DPO.situacion")+"='B',"+
                    campos.getString("SQL.T_DPO.fechaBaja")+"="+abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null)+ ","+
                    campos.getString("SQL.T_DPO.usuarioBaja")+"="+gVO.getAtributo("usuario")+
                    " WHERE "+
                    campos.getString("SQL.T_DPO.domicilio")+"="+idDPO;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            hoja.setAtributo("codDPO",String.valueOf(idNuevoDOM));
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
            //m_Log.error("SQLException: " + ex.getMessage());
        }
    }
}