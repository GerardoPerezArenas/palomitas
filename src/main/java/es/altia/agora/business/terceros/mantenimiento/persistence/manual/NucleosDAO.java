// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.util.GeneralValueObject;
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

/**
 * <p>Título: @gora</p>
 * <p>Descripción: NucleosDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @version 1.0
 */

public class NucleosDAO  {
    private static NucleosDAO instance = null;
    protected static Config campos; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log =
            LogFactory.getLog(NucleosDAO.class.getName());

    protected NucleosDAO() {
        super();
        // Queremos usar el fichero de configuracion techserver
        campos = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }

    public static NucleosDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread)
            // Las invocaciones de este metodo
            synchronized (NucleosDAO.class) {
                if (instance == null) {
                    instance = new NucleosDAO();
                }
            }
        }
        return instance;
    }

    public Vector eliminarNucleo(GeneralValueObject gVO, String[] params){
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        Vector resultado = new Vector();
        int cont = 0;
        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            stmt = conexion.createStatement();

            sql = "SELECT * FROM T_TRM WHERE " +
                    campos.getString("SQL.T_TRM.paisNUC")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_TRM.provinciaNUC")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_TRM.municipioNUC")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_TRM.e_singularNUC")+"="+gVO.getAtributo("codESI")+" AND "+
                    campos.getString("SQL.T_TRM.nucleo")+"="+gVO.getAtributo("codNUC");
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                cont++;
            }
            rs.close();

            if(cont == 0) {
                sql = "DELETE FROM T_NUC WHERE " +
                        campos.getString("SQL.T_NUC.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                        campos.getString("SQL.T_NUC.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                        campos.getString("SQL.T_NUC.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                        campos.getString("SQL.T_NUC.eSingular")+"="+gVO.getAtributo("codESI")+" AND "+
                        campos.getString("SQL.T_NUC.codigo")+"="+gVO.getAtributo("codNUC");
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
            }
            stmt.close();
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
        }finally{
            commitTransaction(abd,conexion);
        }
        if(cont == 0 ) {
            resultado = getListaNucleosTodos(gVO,params);
        } else {
            gVO.setAtributo("puedeEliminar","no");
            resultado.addElement(gVO);
        }
        return resultado;
    }

    public Vector getListaNucleos(GeneralValueObject parametros,String[] params){
        return getListaNucleos(parametros,params, false);
    }

    public Vector getListaNucleosTodos(GeneralValueObject parametros,String[] params){
        return getListaNucleos(parametros,params, true);
    }

    private Vector getListaNucleos(GeneralValueObject parametros,String[] params, boolean todos){
        Vector resultado = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String fecha = (String) parametros.getAtributo("fecha");
        String codESI = (String) parametros.getAtributo("codESI");
        String codECO = (String) parametros.getAtributo("codECO");
        String concatenar = (String) parametros.getAtributo("concatenar");
        try{
            //m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            //m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
//      abd.inicioTransaccion(conexion);
            // Creamos la select con los parametros adecuados.
            String select = "";
            String from = "";
            sql = "SELECT T_NUC.*,T_ESI.*,T_ECO.*" +
                    " FROM T_NUC,T_ESI,T_ECO";
            sql+= " WHERE ";
            // CONDICIONES
            sql+= campos.getString("SQL.T_NUC.pais")+"="+parametros.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_NUC.provincia")+"="+parametros.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_NUC.municipio")+"="+parametros.getAtributo("codMunicipio");
            if (codESI != null && !codESI.equals(""))
                sql+=" AND "+campos.getString("SQL.T_NUC.eSingular")+"="+parametros.getAtributo("codESI");
            if(m_Log.isDebugEnabled()) m_Log.debug("la fecha en el DAO es : " + fecha);
            if (!todos){
                if(fecha != null && !"".equals(fecha)) {
                    sql += " AND " + campos.getString("SQL.T_NUC.fVigencia") + "<"+abd.convertir(fecha, AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+
                            " AND (" + campos.getString("SQL.T_NUC.fBaja") + ">"+abd.convertir(fecha,AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+
                            " OR " + campos.getString("SQL.T_NUC.fBaja") +
                            " IS NULL)";
                } else {

                    sql += " AND " + campos.getString("SQL.T_NUC.fVigencia") + "<"+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+" AND ("
                            + campos.getString("SQL.T_NUC.fBaja") + "> "+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+" OR " + campos.getString("SQL.T_NUC.fBaja") +
                            " IS NULL)";
                }
            }
            sql+= " AND "+campos.getString("SQL.T_ESI.pais")+"="+parametros.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_ESI.provincia")+"="+parametros.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_ESI.municipio")+"="+parametros.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_NUC.eSingular")+"="+campos.getString("SQL.T_ESI.identificador")+" AND "+
                    campos.getString("SQL.T_ESI.pais")+"="+campos.getString("SQL.T_ECO.pais")+" AND "+
                    campos.getString("SQL.T_ESI.provincia")+"="+campos.getString("SQL.T_ECO.provincia")+" AND "+
                    campos.getString("SQL.T_ESI.municipio")+"="+campos.getString("SQL.T_ECO.municipio")+" AND "+
                    campos.getString("SQL.T_ESI.eColectiva")+"="+campos.getString("SQL.T_ECO.identificador");
            if (codECO != null && !codECO.equals(""))
                sql+= " AND "+campos.getString("SQL.T_ECO.identificador")+"="+parametros.getAtributo("codECO");


            String[] orden = {campos.getString("SQL.T_NUC.codigo"),"1"};
            sql += abd.orderUnion(orden);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                GeneralValueObject gVO = new GeneralValueObject();
                // NUCLEO
                gVO.setAtributo("codPais",rs.getString(campos.getString("SQL.T_NUC.pais")));
                gVO.setAtributo("codProvincia",rs.getString(campos.getString("SQL.T_NUC.provincia")));
                gVO.setAtributo("codMunicipio",rs.getString(campos.getString("SQL.T_NUC.municipio")));
                gVO.setAtributo("codECO",rs.getString(campos.getString("SQL.T_ECO.identificador")));
                gVO.setAtributo("descECO",rs.getString(campos.getString("SQL.T_ECO.nombre")));
                gVO.setAtributo("codESI",rs.getString(campos.getString("SQL.T_NUC.eSingular")));
                gVO.setAtributo("descESI",rs.getString(campos.getString("SQL.T_ESI.nombre")));
                gVO.setAtributo("codNUC",rs.getString(campos.getString("SQL.T_NUC.codigo")));
                gVO.setAtributo("descNUC",rs.getString(campos.getString("SQL.T_NUC.nombre")));
                gVO.setAtributo("nombreLargo",rs.getString(campos.getString("SQL.T_NUC.nLargo")));
                gVO.setAtributo("codINE",rs.getString(campos.getString("SQL.T_NUC.ine")));
                gVO.setAtributo("idObjetoGrafico",rs.getString(campos.getString("SQL.T_NUC.imagen")));
                gVO.setAtributo("situacion",rs.getString(campos.getString("SQL.T_NUC.situacion")));
                gVO.setAtributo("idNUC",rs.getString(campos.getString("SQL.T_NUC.ine")));
                String nombreNUC = (String)gVO.getAtributo("descNUC");
                if(concatenar==null){
                    if((gVO.getAtributo("idNUC").equals("99"))||(gVO.getAtributo("idNUC").equals("0"))){
                        nombreNUC = gVO.getAtributo("descNUC")+" - "+gVO.getAtributo("descESI");
                    }
                    gVO.setAtributo("descNUC", nombreNUC);
                }
                resultado.add(gVO);
            }
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
                if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en NucleosDAO.getListaNucleos");
            }
        }
        return resultado;
    }

    public Vector modificarNucleo(GeneralValueObject gVO, String[] params){
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
            sql = "UPDATE T_NUC SET " +
                    campos.getString("SQL.T_NUC.codigo")+"="+gVO.getAtributo("codNUC")+","+
                    campos.getString("SQL.T_NUC.nombre")+"='"+gVO.getAtributo("descNUC")+"',"+
                    campos.getString("SQL.T_NUC.nLargo")+"='"+gVO.getAtributo("nombreLargo")+"',"+
                    campos.getString("SQL.T_NUC.ine")+"="+gVO.getAtributo("codINE")+","+
                    campos.getString("SQL.T_NUC.imagen")+"='"+gVO.getAtributo("idObjetoGrafico")+"',"+
                    campos.getString("SQL.T_NUC.situacion")+"='"+gVO.getAtributo("situacion")+"'";
            if(gVO.getAtributo("situacion").equals("B"))
                sql+=","+campos.getString("SQL.T_NUC.fBaja")+"="+gVO.getAtributo("fechaBaja")+","+
                        campos.getString("SQL.T_NUC.uBaja")+"="+gVO.getAtributo("usuarioBaja");
            sql+= " WHERE " +
                    campos.getString("SQL.T_NUC.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_NUC.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_NUC.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_NUC.eSingular")+"="+gVO.getAtributo("codESI")+" AND "+
                    campos.getString("SQL.T_NUC.codigo")+"="+gVO.getAtributo("codNUCAntiguo");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            int res = stmt.executeUpdate(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("las filas afectadas en el update son : " + res);
            stmt.close();
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
        }finally{
            commitTransaction(abd,conexion);
        }
        resultado = getListaNucleosTodos(gVO,params);
        return resultado;
    }

    public Vector altaNucleo(GeneralValueObject gVO, String[] params){
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

            sql = "SELECT " +abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.T_NUC.codigo")})  + " AS CODNUC FROM T_NUC";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = stmt.executeQuery(sql);
            String codNuc = "";
            while(rs.next()) {
                codNuc = rs.getString("CODNUC");
            }
            rs.close();
            stmt.close();
            int codN = 0;
            if(codNuc != null && !"".equals(codNuc)) {
                codN = Integer.parseInt(codNuc);
            }
            codN = codN + 1;
            sql = "INSERT INTO T_NUC("+
                    campos.getString("SQL.T_NUC.pais")+","+
                    campos.getString("SQL.T_NUC.provincia")+","+
                    campos.getString("SQL.T_NUC.municipio")+","+
                    campos.getString("SQL.T_NUC.eSingular")+","+
                    campos.getString("SQL.T_NUC.codigo")+","+
                    campos.getString("SQL.T_NUC.nombre")+","+
                    campos.getString("SQL.T_NUC.nLargo")+","+
                    campos.getString("SQL.T_NUC.ine")+","+
                    campos.getString("SQL.T_NUC.imagen")+","+
                    campos.getString("SQL.T_NUC.situacion")+","+
                    campos.getString("SQL.T_NUC.fVigencia")+
                    ") VALUES (" +
                    gVO.getAtributo("codPais")+","+
                    gVO.getAtributo("codProvincia")+","+
                    gVO.getAtributo("codMunicipio")+","+
                    gVO.getAtributo("codESI")+","+
                    codN+",'"+
                    gVO.getAtributo("descNUC")+"','"+
                    gVO.getAtributo("nombreLargo")+"',"+
                    gVO.getAtributo("codINE")+",'"+
                    gVO.getAtributo("idObjetoGrafico")+"','"+
                    gVO.getAtributo("situacion")+"',"+
                    abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+")";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            //m_Log.debug("las filas afectadas en el insert son : " + res);
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
        }finally{
            commitTransaction(abd,conexion);
        }
        resultado = getListaNucleosTodos(gVO,params);
        return resultado;
    }

    public String modificarNucTerritorio(String[] params,GeneralValueObject gVO){
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
            stmt = conexion.createStatement();
            // ACTUALIZAR NUC
            actualizarNUC(conexion,gVO,abd);
            // ACTUALIZO EL TRAMERO
            actualizarTramero(conexion,gVO,abd);
            // ACTUALIZO LAS DIRECCIONES SUELO AFECTADAS
            actualizarDSUs(conexion,gVO,abd);
            // ACTUALIZO LAS DIRECCIONES POSTALES AFECTADAS
            actualizarDomicilios(stmt,gVO,abd);
            stmt.close();
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
            correcto = "NO";
        }finally{
            commitTransaction(abd,conexion);
        }
        return correcto;
    }

    private void actualizarNUC(Connection con,GeneralValueObject gVO,AdaptadorSQLBD abd)
            throws Exception {
        Statement stmt = con.createStatement();
        String sql = "";
        ResultSet rs = null;
        Vector nucleosModificados = new Vector();
        GeneralValueObject nuc = new GeneralValueObject();
        String codNUC = (String)gVO.getAtributo("codNUC");
        nuc.setAtributo("codNUC", codNUC);
        nuc.setAtributo("codESI",gVO.getAtributo("codESI"));
        nuc.setAtributo("codNuevaESI",gVO.getAtributo("codESI"));
        int idNuevoNUC = 0;
        sql = "SELECT " + abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.T_NUC.codigo")}) +
                " AS MAXIMO" +
                " FROM T_NUC WHERE " +
                campos.getString("SQL.T_NUC.pais") + "=" +
                gVO.getAtributo("codPais") + " AND " +
                campos.getString("SQL.T_NUC.provincia") + "=" +
                gVO.getAtributo("codProvincia") + " AND " +
                campos.getString("SQL.T_NUC.municipio") + "=" +
                gVO.getAtributo("codMunicipio") + " AND " +
                campos.getString("SQL.T_NUC.eSingular") + "=" +
                gVO.getAtributo("codESI");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            idNuevoNUC = rs.getInt("MAXIMO");
        }
        rs.close();
        idNuevoNUC++;
        sql = "INSERT INTO T_NUC( " +
                campos.getString("SQL.T_NUC.pais")+","+
                campos.getString("SQL.T_NUC.provincia") + "," +
                campos.getString("SQL.T_NUC.municipio") + "," +
                campos.getString("SQL.T_NUC.eSingular") + "," +
                campos.getString("SQL.T_NUC.codigo") + "," +
                campos.getString("SQL.T_NUC.nombre") + "," +
                campos.getString("SQL.T_NUC.nLargo") + "," +
                campos.getString("SQL.T_NUC.ine") + "," +
                campos.getString("SQL.T_NUC.imagen") + "," +
                campos.getString("SQL.T_NUC.situacion") + "," +
                campos.getString("SQL.T_NUC.fVigencia") +
                ") SELECT " +
                campos.getString("SQL.T_NUC.pais") + "," +
                campos.getString("SQL.T_NUC.provincia") + "," +
                campos.getString("SQL.T_NUC.municipio") + "," +
                campos.getString("SQL.T_NUC.eSingular")+","+
                idNuevoNUC + ",'" +
                gVO.getAtributo("descNUC") + "'," +
                campos.getString("SQL.T_NUC.nLargo") + "," +
                gVO.getAtributo("codINE") + "," +
                campos.getString("SQL.T_NUC.imagen") + "," +
                campos.getString("SQL.T_NUC.situacion") + "," +
                campos.getString("SQL.T_NUC.fVigencia") +
                " FROM T_NUC " +
                " WHERE " +
                campos.getString("SQL.T_NUC.pais") + "=" +
                gVO.getAtributo("codPais") + " AND " +
                campos.getString("SQL.T_NUC.provincia") + "=" +
                gVO.getAtributo("codProvincia") + " AND " +
                campos.getString("SQL.T_NUC.municipio") + "=" +
                gVO.getAtributo("codMunicipio") + " AND " +
                campos.getString("SQL.T_NUC.eSingular") + "=" + gVO.getAtributo("codESI")+" AND "+
                campos.getString("SQL.T_NUC.codigo") + "=" +codNUC;
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt.execute(sql);
        // DOY DE BAJA EL NUCLEO ANTIGUO
        sql = "UPDATE T_NUC SET " +
                campos.getString("SQL.T_NUC.situacion") + "='B'," +
                campos.getString("SQL.T_NUC.fBaja")+"="+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
                campos.getString("SQL.T_NUC.uBaja")+"="+gVO.getAtributo("usuario")+
                " WHERE " +
                campos.getString("SQL.T_NUC.pais") + "=" +
                gVO.getAtributo("codPais") + " AND " +
                campos.getString("SQL.T_NUC.provincia") + "=" +
                gVO.getAtributo("codProvincia") + " AND " +
                campos.getString("SQL.T_NUC.municipio") + "=" +
                gVO.getAtributo("codMunicipio") + " AND " +
                campos.getString("SQL.T_NUC.eSingular") + "=" + gVO.getAtributo("codESI")+" AND "+
                campos.getString("SQL.T_NUC.codigo") + "=" +codNUC;

        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt.execute(sql);
        nuc.setAtributo("codNuevoNUC",String.valueOf(idNuevoNUC));
        nucleosModificados.add(nuc);
        gVO.setAtributo("nucleosModificados", nucleosModificados);
        stmt.close();
    }

    private void actualizarTramero(Connection con,GeneralValueObject gVO, AdaptadorSQLBD abd)
            throws Exception {
        Statement stmt = con.createStatement();
        Statement stmt1 = con.createStatement();
        String sql = "";
        ResultSet rs = null;
        ResultSet rs1 = null;
        Vector nucleosModificados = (Vector)gVO.getAtributo("nucleosModificados");
        if (nucleosModificados==null) nucleosModificados= new Vector();
        Vector tramosModificados = new Vector();
        Vector tramosDestino = new Vector();
        int i=0;
        for(i=0;i<nucleosModificados.size();i++){
            GeneralValueObject nucleo = (GeneralValueObject) nucleosModificados.get(i);
            sql = "SELECT T_TRM.* FROM T_TRM " +
                    " WHERE " +
                    campos.getString("SQL.T_TRM.situacion") + "='A' AND " +
                    campos.getString("SQL.T_TRM.pais") + "=" +
                    gVO.getAtributo("codPais") + " AND " +
                    campos.getString("SQL.T_TRM.provincia") + "=" +
                    gVO.getAtributo("codProvincia") + " AND " +
                    campos.getString("SQL.T_TRM.municipio") + "=" +
                    gVO.getAtributo("codMunicipio") + " AND " +
                    campos.getString("SQL.T_TRM.e_singularNUC") + "=" +
                    nucleo.getAtributo("codESI") + " AND " +
                    campos.getString("SQL.T_TRM.nucleo") + "=" +
                    nucleo.getAtributo("codNUC");
            sql += " ORDER BY " + campos.getString("SQL.T_TRM.vial") + "," +
                    campos.getString("SQL.T_TRM.tipoNumeracion") + "," +
                    campos.getString("SQL.T_TRM.codigo");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                // ESTE ES EL TRAMO ORIGINAL
                GeneralValueObject tramo = new GeneralValueObject();
                tramo.setAtributo("codTramo",
                        rs.getString(campos.getString("SQL.T_TRM.codigo")));
                tramo.setAtributo("idVia",
                        rs.getString(campos.getString("SQL.T_TRM.vial")));
                tramo.setAtributo("tipoNumeracion",
                        rs.getString(campos.getString("SQL.T_TRM.tipoNumeracion")));
                tramo.setAtributo("distrito",
                        rs.getString(campos.getString("SQL.T_TRM.distritoSeccion")));
                tramo.setAtributo("seccion",
                        rs.getString(campos.getString("SQL.T_TRM.seccion")));
                tramo.setAtributo("letra",
                        rs.getString(campos.getString("SQL.T_TRM.letraSeccion")));
                tramo.setAtributo("numDesde",
                        rs.getString(campos.getString("SQL.T_TRM.primerNumero")));
                tramo.setAtributo("letraDesde",
                        rs.getString(campos.getString("SQL.T_TRM.primeraLetra")));
                tramo.setAtributo("numHasta",
                        rs.getString(campos.getString("SQL.T_TRM.ultimoNumero")));
                tramo.setAtributo("letraHasta",
                        rs.getString(campos.getString("SQL.T_TRM.ultimaLetra")));
                tramo.setAtributo("codESI",
                        rs.getString(campos.getString("SQL.T_TRM.e_singularNUC")));
                tramo.setAtributo("codNUC",
                        rs.getString(campos.getString("SQL.T_TRM.nucleo")));
                tramo.setAtributo("codESINueva",nucleo.getAtributo("codNuevaESI"));
                tramo.setAtributo("codNUCNueva",nucleo.getAtributo("codNuevoNUC"));

                // DOY DE BAJA EL TRAMO
                sql = "UPDATE T_TRM SET " +
                        campos.getString("SQL.T_TRM.situacion") + "='B'" +
                        " WHERE " +
                        campos.getString("SQL.T_TRM.pais") + "=" +
                        gVO.getAtributo("codPais") + " AND " +
                        campos.getString("SQL.T_TRM.provincia") + "=" +
                        gVO.getAtributo("codProvincia") + " AND " +
                        campos.getString("SQL.T_TRM.municipio") + "=" +
                        gVO.getAtributo("codMunicipio") + " AND " +
                        campos.getString("SQL.T_TRM.vial") + "=" +
                        rs.getString(campos.getString("SQL.T_TRM.vial")) + " AND " +
                        campos.getString("SQL.T_TRM.tipoNumeracion") + "=" +
                        rs.getString(campos.getString("SQL.T_TRM.tipoNumeracion")) +
                        " AND " +
                        campos.getString("SQL.T_TRM.codigo") + "=" +
                        rs.getString(campos.getString("SQL.T_TRM.codigo"));
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt1.executeUpdate(sql);
                // CREAR EL NUEVO TRAMO

                sql = "SELECT "+abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{
                        abd.convertir(campos.getString("SQL.T_TRM.codigo"),abd.CONVERTIR_COLUMNA_NUMERO,null)})+
                        " AS MAXIMO" +
                        " FROM T_TRM WHERE " +
                        campos.getString("SQL.T_TRM.pais") + "=" +
                        gVO.getAtributo("codPais") + " AND " +
                        campos.getString("SQL.T_TRM.provincia") + "=" +
                        gVO.getAtributo("codProvincia") + " AND " +
                        campos.getString("SQL.T_TRM.municipio") + "=" +
                        gVO.getAtributo("codMunicipio") + " AND " +
                        campos.getString("SQL.T_TRM.vial") + "=" +
                        rs.getString(campos.getString("SQL.T_TRM.vial")) + " AND " +
                        campos.getString("SQL.T_TRM.tipoNumeracion") + "=" +
                        rs.getString(campos.getString("SQL.T_TRM.tipoNumeracion"));
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs1 = stmt1.executeQuery(sql);
                int idNuevoTramo = 0;
                if (rs1.next()) {
                    idNuevoTramo = rs1.getInt("MAXIMO");
                }
                rs1.close();
                idNuevoTramo++;
                sql = "INSERT INTO T_TRM(" +
                        campos.getString("SQL.T_TRM.tipoNumeracion") + "," +
                        campos.getString("SQL.T_TRM.pais") + "," +
                        campos.getString("SQL.T_TRM.provincia") + "," +
                        campos.getString("SQL.T_TRM.municipio") + "," +
                        campos.getString("SQL.T_TRM.vial") + "," +
                        campos.getString("SQL.T_TRM.codigo") + "," +
                        campos.getString("SQL.T_TRM.paisSeccion") + "," +
                        campos.getString("SQL.T_TRM.provinciaSeccion") + "," +
                        campos.getString("SQL.T_TRM.municipioSeccion") + "," +
                        campos.getString("SQL.T_TRM.distritoSeccion") + "," +
                        campos.getString("SQL.T_TRM.seccion") + "," +
                        campos.getString("SQL.T_TRM.letraSeccion") + "," +
                        campos.getString("SQL.T_TRM.codigoPostal") + "," +
                        campos.getString("SQL.T_TRM.primerNumero") + "," +
                        campos.getString("SQL.T_TRM.primeraLetra") + "," +
                        campos.getString("SQL.T_TRM.ultimoNumero") + "," +
                        campos.getString("SQL.T_TRM.ultimaLetra") + "," +
                        campos.getString("SQL.T_TRM.paisNUC") + "," +
                        campos.getString("SQL.T_TRM.provinciaNUC") + "," +
                        campos.getString("SQL.T_TRM.municipioNUC") + "," +
                        campos.getString("SQL.T_TRM.e_singularNUC") + "," +
                        campos.getString("SQL.T_TRM.nucleo") +
                        ") SELECT " +
                        campos.getString("SQL.T_TRM.tipoNumeracion") + "," +
                        campos.getString("SQL.T_TRM.pais") + "," +
                        campos.getString("SQL.T_TRM.provincia") + "," +
                        campos.getString("SQL.T_TRM.municipio") + "," +
                        campos.getString("SQL.T_TRM.vial") + "," +
                        idNuevoTramo + "," +
                        campos.getString("SQL.T_TRM.paisSeccion") + "," +
                        campos.getString("SQL.T_TRM.provinciaSeccion") + "," +
                        campos.getString("SQL.T_TRM.municipioSeccion") + "," +
                        campos.getString("SQL.T_TRM.distritoSeccion") + "," +
                        campos.getString("SQL.T_TRM.seccion") + "," +
                        campos.getString("SQL.T_TRM.letraSeccion") + "," +
                        campos.getString("SQL.T_TRM.codigoPostal") + "," +
                        campos.getString("SQL.T_TRM.primerNumero") + "," +
                        campos.getString("SQL.T_TRM.primeraLetra") + "," +
                        campos.getString("SQL.T_TRM.ultimoNumero") + "," +
                        campos.getString("SQL.T_TRM.ultimaLetra") + "," +
                        campos.getString("SQL.T_TRM.paisNUC") + "," +
                        campos.getString("SQL.T_TRM.provinciaNUC") + "," +
                        campos.getString("SQL.T_TRM.municipioNUC") + "," +
                        nucleo.getAtributo("codNuevaESI") + "," +
                        nucleo.getAtributo("codNuevoNUC") +
                        " FROM T_TRM WHERE " +
                        campos.getString("SQL.T_TRM.pais") + "=" +
                        gVO.getAtributo("codPais") + " AND " +
                        campos.getString("SQL.T_TRM.provincia") + "=" +
                        gVO.getAtributo("codProvincia") + " AND " +
                        campos.getString("SQL.T_TRM.municipio") + "=" +
                        gVO.getAtributo("codMunicipio") + " AND " +
                        campos.getString("SQL.T_TRM.vial") + "=" +
                        rs.getString(campos.getString("SQL.T_TRM.vial")) + " AND " +
                        campos.getString("SQL.T_TRM.tipoNumeracion") + "=" +
                        rs.getString(campos.getString("SQL.T_TRM.tipoNumeracion")) +
                        " AND " +
                        campos.getString("SQL.T_TRM.codigo") + "=" +
                        rs.getString(campos.getString("SQL.T_TRM.codigo"));
                //m_Log.debug(idNuevoTramo);
                stmt1.executeUpdate(sql);
                tramo.setAtributo("codTramoNuevo",String.valueOf(idNuevoTramo));
                tramosModificados.add(tramo);
            }
            gVO.setAtributo("tramosModificados", tramosModificados);
            rs.close();
        }
        stmt.close();
        stmt1.close();
    }


    private void actualizarDSUs(Connection con,GeneralValueObject gVO,AdaptadorSQLBD abd)
            throws Exception {
        String sql = "";
        Statement stmt = con.createStatement();
        Statement stmt1 = con.createStatement();
        ResultSet rs = null;
        ResultSet rs1 = null;
        Vector tramosModificados = (Vector)gVO.getAtributo("tramosModificados");
        if (tramosModificados == null) tramosModificados=new Vector();
        Vector hojas = new Vector();
        int i=0;
        for(i=0;i<tramosModificados.size();i++){
            GeneralValueObject tramo = (GeneralValueObject)tramosModificados.get(i);
            String idVia = (String)gVO.getAtributo("idVia");
            String tipoNumeracion = (String)gVO.getAtributo("codTipoNumeracion");
            idVia = (String)tramo.getAtributo("idVia");
            tipoNumeracion = (String)tramo.getAtributo("tipoNumeracion");
            gVO.setAtributo("idVia", idVia);
            gVO.setAtributo("codTipoNumeracion", tipoNumeracion);
            gVO.setAtributo("codTramoNuevo", tramo.getAtributo("codTramoNuevo"));
            sql = "SELECT P_HOJ.*,T_DPO.*,T_DSU.*,T_TRM.* FROM P_HOJ,T_DPO,T_DSU,T_TRM " +
                    " WHERE "+
                    campos.getString("SQL.T_DSU.situacion")+"='A' AND " +
                    abd.joinRight(campos.getString("SQL.P_HOJ.domicilio"),campos.getString("SQL.T_DPO.domicilio"))+" AND "+
                    campos.getString("SQL.T_DPO.suelo")+"="+campos.getString("SQL.T_DSU.identificador")+" AND "+
                    campos.getString("SQL.T_DSU.pais")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_DSU.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_DSU.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_DSU.vial")+"="+idVia+" AND "+
                    campos.getString("SQL.T_DSU.paisTRM")+"="+gVO.getAtributo("codPais")+" AND "+
                    campos.getString("SQL.T_DSU.provinciaTRM")+"="+gVO.getAtributo("codProvincia")+" AND "+
                    campos.getString("SQL.T_DSU.municipioTRM")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                    campos.getString("SQL.T_DSU.tipoNumeracionTRM")+"="+tipoNumeracion+" AND "+
                    campos.getString("SQL.T_DSU.codigoTRM")+"="+tramo.getAtributo("codTramo")+" AND "+
                    campos.getString("SQL.T_DSU.vialTRM")+"="+idVia+" AND "+
                    campos.getString("SQL.T_DSU.paisTRM")+"=" + campos.getString("SQL.T_TRM.pais")+ " AND " +
                    campos.getString("SQL.T_DSU.provinciaTRM")+"=" + campos.getString("SQL.T_TRM.provincia") + " AND " +
                    campos.getString("SQL.T_DSU.municipioTRM")+"=" + campos.getString("SQL.T_TRM.municipio") + " AND "+
                    campos.getString("SQL.T_DSU.vialTRM")+"=" + campos.getString("SQL.T_TRM.vial")+ " AND " +
                    campos.getString("SQL.T_DSU.codigoTRM")+"=" + campos.getString("SQL.T_TRM.codigo") + " AND " +
                    //campos.getString("SQL.T_TRM.situacion")+"='A' AND "+
                    campos.getString("SQL.T_DSU.tipoNumeracionTRM")+"=" + campos.getString("SQL.T_TRM.tipoNumeracion");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = stmt.executeQuery(sql);
            String idDSUAnterior = "";
            while(rs.next()){
                String idDSU = rs.getString(campos.getString("SQL.T_DSU.identificador"));
                if(!idDSU.equals(idDSUAnterior)){
                    // INSERTAR LA NUEVA DSU
                    insertarT_DSU(con,rs,gVO,abd);
                    // DOY DE BAJA LA DIRECCION SUELO ANTIGUA
                    sql = "UPDATE T_DSU SET "+
                            campos.getString("SQL.T_DSU.situacion")+"='B',"+
                            campos.getString("SQL.T_DSU.fechaBaja")+"="+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
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
        int idNuevaDSU = 0;
        sql = "SELECT "+abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.T_DSU.identificador")})+
                " AS MAXIMO"+
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
                "'A',"+
                abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
                gVO.getAtributo("usuario")+","+
                "null,"+
                "null,"+
                abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
                campos.getString("SQL.T_DSU.tipoNumeracionTRM")+","+
                campos.getString("SQL.T_DSU.paisTRM")+","+
                campos.getString("SQL.T_DSU.provinciaTRM")+","+
                campos.getString("SQL.T_DSU.municipioTRM")+","+
                campos.getString("SQL.T_DSU.vialTRM")+","+
                gVO.getAtributo("codTramoNuevo")+
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
                    abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
                    gVO.getAtributo("usuario")+","+
                    "null,"+
                    "null,"+
                    abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
                    " FROM T_DPO WHERE "+
                    campos.getString("SQL.T_DPO.domicilio")+"="+idDPO;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            // DOY DE BAJA LA DIRECCION POSTAL ANTIGUA
            sql = "UPDATE T_DPO SET "+
                    campos.getString("SQL.T_DPO.situacion")+"='B',"+
                    campos.getString("SQL.T_DPO.fechaBaja")+"="+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
                    campos.getString("SQL.T_DPO.usuarioBaja")+"="+gVO.getAtributo("usuario")+
                    " WHERE "+
                    campos.getString("SQL.T_DPO.domicilio")+"="+idDPO;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            hoja.setAtributo("codDPO",String.valueOf(idNuevoDOM));
            stmt.close();
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
            m_Log.error(ex.getMessage());
        }
    }

}