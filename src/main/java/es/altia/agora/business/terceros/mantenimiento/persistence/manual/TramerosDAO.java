// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import java.util.ArrayList;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: TramerosDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @version 1.0
 */

public class TramerosDAO  {
  private static TramerosDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(TramerosDAO.class.getName());

  protected TramerosDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static TramerosDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (TramerosDAO.class) {
        if (instance == null) {
          instance = new TramerosDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarTramero(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    int cont = 0;
    Vector resultado = new Vector();
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();

      sql = "SELECT * FROM T_DSU WHERE " +
            campos.getString("SQL.T_DSU.paisTRM")+"="+gVO.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_DSU.provinciaTRM")+"="+gVO.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_DSU.municipioTRM")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_DSU.vialTRM")+"="+gVO.getAtributo("codVia")+" AND "+
                campos.getString("SQL.T_DSU.tipoNumeracionTRM")+"="+gVO.getAtributo("tipoNumeracion")+" AND "+
                campos.getString("SQL.T_DSU.codigoTRM")+"="+gVO.getAtributo("codTramo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
          cont++;
      }
      rs.close();

      if(cont == 0) {
          sql = "DELETE FROM T_TRM WHERE " +
            campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
            campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
            campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
            campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("codVia")+" AND "+
            campos.getString("SQL.T_TRM.tipoNumeracion")+"="+gVO.getAtributo("tipoNumeracion")+" AND "+
            campos.getString("SQL.T_TRM.codigo")+"="+gVO.getAtributo("codTramo");
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          stmt.executeUpdate(sql);
      }
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    if(cont == 0) {
      resultado = getListaTrameros(gVO,params);
    } else {
        gVO.setAtributo("puedeEliminar","no");
        resultado.addElement(gVO);
    }
    return resultado;
  }

  public Vector getListaTrameros(GeneralValueObject parametros,String[] params){
    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
//      abd.inicioTransaccion(conexion);

      //String codESI = (String)parametros.getAtributo("codESI");
      //String codNUC = (String)parametros.getAtributo("codNUC");
      String codTipoNumeracion = (String)parametros.getAtributo("codTipoNumeracion");
      String idNucleo = (String)parametros.getAtributo("idNucleo");


String from = " T_TRM.*,T_VIA.*,T_TNU.*,T_PAI.*,T_PRV.*,T_MUN.*,T_ECO.*,T_ESI.*,T_NUC.*, "+
        "T_DIS.*,T_SEC.*,T_SSC.*,T_MZI.*";


String where = "";
where = (!codTipoNumeracion.equals(""))?
where+campos.getString("SQL.T_TRM.tipoNumeracion")+"="+codTipoNumeracion+" AND ": where;
if(idNucleo != null && !idNucleo.equals("") && !idNucleo.equals("null")) {
where += campos.getString("SQL.T_TRM.paisNUC")+"="+parametros.getAtributo("codPais")+" AND " +
       campos.getString("SQL.T_TRM.provinciaNUC")+"="+parametros.getAtributo("codProvincia")+" AND " +
       campos.getString("SQL.T_TRM.municipioNUC")+"="+parametros.getAtributo("codMunicipio")+" AND " +
       campos.getString("SQL.T_TRM.e_singularNUC")+"="+parametros.getAtributo("idESI")+" AND " +
       campos.getString("SQL.T_TRM.nucleo")+"="+idNucleo+" AND ";
}
where =  campos.getString("SQL.T_TRM.pais")+"="+parametros.getAtributo("codPais")+" AND "+
                campos.getString("SQL.T_TRM.provincia")+"="+parametros.getAtributo("codProvincia")+" AND "+
                campos.getString("SQL.T_TRM.municipio")+"="+parametros.getAtributo("codMunicipio")+" AND "+
                campos.getString("SQL.T_TRM.vial")+"="+parametros.getAtributo("idVia");


ArrayList join = new ArrayList();
join.add("T_TRM");
join.add("INNER");
join.add("T_VIA");
join.add(campos.getString("SQL.T_TRM.pais")+"="+campos.getString("SQL.T_VIA.pais")+" AND "+
        campos.getString("SQL.T_TRM.provincia")+"="+campos.getString("SQL.T_VIA.provincia")+" AND "+
        campos.getString("SQL.T_TRM.municipio")+"="+campos.getString("SQL.T_VIA.municipio")+" AND "+
        campos.getString("SQL.T_TRM.vial")+"="+campos.getString("SQL.T_VIA.identificador"));
join.add("INNER");
join.add("T_TNU");
join.add(campos.getString("SQL.T_TRM.tipoNumeracion")+"="+campos.getString("SQL.T_TNU.codigo"));
join.add("INNER");
join.add("T_SEC");
join.add(campos.getString("SQL.T_TRM.paisSeccion")+"="+campos.getString("SQL.T_SEC.pais")+" AND "+
         campos.getString("SQL.T_TRM.provinciaSeccion")+"="+campos.getString("SQL.T_SEC.provincia")+" AND "+
         campos.getString("SQL.T_TRM.municipioSeccion")+"="+campos.getString("SQL.T_SEC.municipio")+" AND "+
         campos.getString("SQL.T_TRM.distritoSeccion")+"="+campos.getString("SQL.T_SEC.distrito")+" AND "+
         campos.getString("SQL.T_TRM.seccion")+"="+campos.getString("SQL.T_SEC.codigo")+" AND "+
         campos.getString("SQL.T_TRM.letraSeccion")+"="+campos.getString("SQL.T_SEC.letra"));
join.add("INNER");
join.add("T_DIS");
join.add(campos.getString("SQL.T_TRM.paisSeccion")+"="+campos.getString("SQL.T_DIS.pais")+" AND "+
         campos.getString("SQL.T_TRM.provinciaSeccion")+"="+campos.getString("SQL.T_DIS.provincia")+" AND "+
         campos.getString("SQL.T_TRM.municipioSeccion")+"="+campos.getString("SQL.T_DIS.municipio")+" AND "+
         campos.getString("SQL.T_TRM.distritoSeccion")+"="+campos.getString("SQL.T_DIS.codigo"));
join.add("INNER");
join.add(GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN");
join.add(campos.getString("SQL.T_TRM.municipio")+"="+campos.getString("SQL.T_MUN.idMunicipio")+" AND "+
         campos.getString("SQL.T_TRM.pais")+"="+campos.getString("SQL.T_MUN.idPais")+" AND "+
         campos.getString("SQL.T_TRM.provincia")+"="+campos.getString("SQL.T_MUN.idProvincia"));
join.add("INNER");
join.add(GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV");
join.add(campos.getString("SQL.T_MUN.idProvincia")+"="+campos.getString("SQL.T_PRV.idProvincia")+" AND "+
         campos.getString("SQL.T_MUN.idPais")+"="+campos.getString("SQL.T_PRV.idPais"));
join.add("INNER");
join.add(GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI");
join.add(campos.getString("SQL.T_PRV.idPais")+"="+campos.getString("SQL.T_PAI.idPais"));
join.add("LEFT");
join.add("T_SSC");
join.add(campos.getString("SQL.T_TRM.paisSubseccion")+"="+campos.getString("SQL.T_SSC.pais")+" AND "+
         campos.getString("SQL.T_TRM.provinciaSubseccion")+"="+campos.getString("SQL.T_SSC.provincia")+" AND "+
         campos.getString("SQL.T_TRM.municipioSubseccion")+"="+campos.getString("SQL.T_SSC.municipio")+" AND "+
         campos.getString("SQL.T_TRM.distritoSubseccion")+"="+campos.getString("SQL.T_SSC.distrito")+" AND "+
         campos.getString("SQL.T_TRM.seccionSubseccion")+"="+campos.getString("SQL.T_SSC.seccion")+" AND "+
         campos.getString("SQL.T_TRM.letraSubseccion")+"="+campos.getString("SQL.T_SSC.letraSeccion"));
join.add("LEFT");
join.add("T_MZI");
join.add(campos.getString("SQL.T_TRM.paisManzana")+"="+campos.getString("SQL.T_MZI.pais")+" AND "+
         campos.getString("SQL.T_TRM.provinciaManzana")+"="+campos.getString("SQL.T_MZI.provincia")+" AND "+
         campos.getString("SQL.T_TRM.municipioManzana")+"="+campos.getString("SQL.T_MZI.municipio")+" AND "+
         campos.getString("SQL.T_TRM.distritoManzana")+"="+campos.getString("SQL.T_MZI.distrito")+" AND "+
         campos.getString("SQL.T_TRM.seccionManzana")+"="+campos.getString("SQL.T_MZI.seccion")+" AND "+
         campos.getString("SQL.T_TRM.letraManzana")+"="+campos.getString("SQL.T_MZI.letraSeccion")+" AND "+
         campos.getString("SQL.T_TRM.manzana")+"="+campos.getString("SQL.T_MZI.codigo"));
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
join.add(campos.getString("SQL.T_ESI.pais")+"="+campos.getString("SQL.T_ECO.pais")+" AND "+
         campos.getString("SQL.T_ESI.provincia")+"="+campos.getString("SQL.T_ECO.provincia")+" AND "+
         campos.getString("SQL.T_ESI.municipio")+"="+campos.getString("SQL.T_ECO.municipio")+" AND "+
         campos.getString("SQL.T_ESI.eColectiva")+"="+campos.getString("SQL.T_ECO.identificador"));
join.add("false");
sql = abd.join(from,where,(String[]) join.toArray(new String[]{}));



      String[] orden = {campos.getString("SQL.T_TRM.codigo"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        // TRAMO
        gVO.setAtributo("codTipoNumeracion",rs.getString(campos.getString("SQL.T_TRM.tipoNumeracion")));
        gVO.setAtributo("descTipoNumeracion",rs.getString(campos.getString("SQL.T_TNU.descripcion")));
        gVO.setAtributo("codPais",rs.getString(campos.getString("SQL.T_TRM.pais")));
        gVO.setAtributo("descPais",rs.getString(campos.getString("SQL.T_PAI.nombre")));
        gVO.setAtributo("codProvincia",rs.getString(campos.getString("SQL.T_TRM.provincia")));
        gVO.setAtributo("descProvincia",rs.getString(campos.getString("SQL.T_PRV.nombre")));
        gVO.setAtributo("codMunicipio",rs.getString(campos.getString("SQL.T_TRM.municipio")));
        gVO.setAtributo("descMunicipio",rs.getString(campos.getString("SQL.T_MUN.nombre")));
        gVO.setAtributo("idVia",rs.getString(campos.getString("SQL.T_TRM.vial")));
        gVO.setAtributo("codVia",rs.getString(campos.getString("SQL.T_VIA.codVia")));
        gVO.setAtributo("descVia",rs.getString(campos.getString("SQL.T_VIA.nombreVia")));
        gVO.setAtributo("codTramo",rs.getString(campos.getString("SQL.T_TRM.codigo")));
        gVO.setAtributo("codDistrito",rs.getString(campos.getString("SQL.T_TRM.distritoSeccion")));
        gVO.setAtributo("descDistrito",rs.getString(campos.getString("SQL.T_DIS.nOficial")));
        gVO.setAtributo("codSeccion",rs.getString(campos.getString("SQL.T_SEC.codigo")));
        gVO.setAtributo("letraSeccion",rs.getString(campos.getString("SQL.T_TRM.letraSeccion")));
        gVO.setAtributo("descSeccion",rs.getString(campos.getString("SQL.T_SEC.nOficial")));
        gVO.setAtributo("codSubSeccion",rs.getString(campos.getString("SQL.T_TRM.subseccion")));
        gVO.setAtributo("descSubSeccion",rs.getString(campos.getString("SQL.T_SSC.nOficial")));
        gVO.setAtributo("codManzana",rs.getString(campos.getString("SQL.T_TRM.manzana")));
        gVO.setAtributo("descManzana",rs.getString(campos.getString("SQL.T_MZI.nombre")));
        gVO.setAtributo("codECO",rs.getString(campos.getString("SQL.T_ECO.identificador")));
        gVO.setAtributo("descECO",rs.getString(campos.getString("SQL.T_ECO.nombre")));
        gVO.setAtributo("codESI",rs.getString(campos.getString("SQL.T_ESI.identificador")));
        gVO.setAtributo("descESI",rs.getString(campos.getString("SQL.T_ESI.nombre")));
        gVO.setAtributo("codNUC",rs.getString(campos.getString("SQL.T_NUC.codigo")));
        gVO.setAtributo("descNUC",rs.getString(campos.getString("SQL.T_NUC.nombre")));
        gVO.setAtributo("codPostal",rs.getString(campos.getString("SQL.T_TRM.codigoPostal")));
        gVO.setAtributo("numDesde",rs.getString(campos.getString("SQL.T_TRM.primerNumero")));
        gVO.setAtributo("letraDesde",rs.getString(campos.getString("SQL.T_TRM.primeraLetra")));
        gVO.setAtributo("numHasta",rs.getString(campos.getString("SQL.T_TRM.ultimoNumero")));
        gVO.setAtributo("letraHasta",rs.getString(campos.getString("SQL.T_TRM.ultimaLetra")));
        gVO.setAtributo("situacion",rs.getString(campos.getString("SQL.T_TRM.situacion")));
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
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en TramerosDAO.getListaTrameros");
        }
    }
    return resultado;
  }

  public GeneralValueObject buscarTramosProceso(String[] params,GeneralValueObject parametros){
    GeneralValueObject resultado = new GeneralValueObject();
    Vector tramosOrigen = new Vector();
    Vector tramosDestino = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
//      abd.inicioTransaccion(conexion);
      // Creamos la select con los parametros adecuados.
      sql = "SELECT T_TRM.* FROM T_TRM WHERE ";
      // CONDICIONES
      sql+= campos.getString("SQL.T_TRM.situacion")+"='A' AND "+
        campos.getString("SQL.T_TRM.pais")+"="+parametros.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_TRM.provincia")+"="+parametros.getAtributo("codProvincia")+" AND "+
        campos.getString("SQL.T_TRM.municipio")+"="+parametros.getAtributo("codMunicipio")+" AND "+
        campos.getString("SQL.T_TRM.vial")+"="+parametros.getAtributo("idVia")+" AND ";

      String tipoNumeracion = (String)parametros.getAtributo("codTipoNumeracion");
      sql = (!tipoNumeracion.equals(""))?
        sql+campos.getString("SQL.T_TRM.tipoNumeracion")+"="+tipoNumeracion+" AND ":sql;
      // JOINS
      sql+= campos.getString("SQL.T_TRM.distritoSeccion")+"= ? AND "+
        campos.getString("SQL.T_TRM.seccion")+"= ? AND "+
        campos.getString("SQL.T_TRM.letraSeccion")+"= ? "
        /* Cambio combo vial. */
        + " AND "+campos.getString("SQL.T_TRM.e_singularNUC")+"="+parametros.getAtributo("codESI");
        /* +" AND "+campos.getString("SQL.T_TRM.nucleo")+"="+parametros.getAtributo("codNUC");
        * Fin cambio combo vial */

      String[] orden = {campos.getString("SQL.T_TRM.codigo"),"1"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.prepareStatement(sql);
      stmt.setString(1,(String)parametros.getAtributo("distritoOrigen"));
      stmt.setString(2,(String)parametros.getAtributo("seccionOrigen"));
      stmt.setString(3,(String)parametros.getAtributo("letraOrigen"));
      rs = stmt.executeQuery();
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        // TRAMO
        gVO.setAtributo("codTramo",rs.getString(campos.getString("SQL.T_TRM.codigo")));
        gVO.setAtributo("codTipoNumeracion",rs.getString(campos.getString("SQL.T_TRM.tipoNumeracion")));
        gVO.setAtributo("distrito",rs.getString(campos.getString("SQL.T_TRM.distritoSeccion")));
        gVO.setAtributo("seccion",rs.getString(campos.getString("SQL.T_TRM.seccion")));
        gVO.setAtributo("letra",rs.getString(campos.getString("SQL.T_TRM.letraSeccion")));
        gVO.setAtributo("numDesde",rs.getString(campos.getString("SQL.T_TRM.primerNumero")));
        gVO.setAtributo("letraDesde",rs.getString(campos.getString("SQL.T_TRM.primeraLetra")));
        gVO.setAtributo("numHasta",rs.getString(campos.getString("SQL.T_TRM.ultimoNumero")));
        gVO.setAtributo("letraHasta",rs.getString(campos.getString("SQL.T_TRM.ultimaLetra")));
        tramosOrigen.add(gVO);
      }
      rs.close();
      stmt.setString(1,(String)parametros.getAtributo("distritoDestino"));
      stmt.setString(2,(String)parametros.getAtributo("seccionDestino"));
      stmt.setString(3,(String)parametros.getAtributo("letraDestino"));
      rs = stmt.executeQuery();
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        // TRAMO
        gVO.setAtributo("codTramo",rs.getString(campos.getString("SQL.T_TRM.codigo")));
        gVO.setAtributo("codTipoNumeracion",rs.getString(campos.getString("SQL.T_TRM.tipoNumeracion")));
        gVO.setAtributo("distrito",rs.getString(campos.getString("SQL.T_TRM.distritoSeccion")));
        gVO.setAtributo("seccion",rs.getString(campos.getString("SQL.T_TRM.seccion")));
        gVO.setAtributo("letra",rs.getString(campos.getString("SQL.T_TRM.letraSeccion")));
        gVO.setAtributo("numDesde",rs.getString(campos.getString("SQL.T_TRM.primerNumero")));
        gVO.setAtributo("letraDesde",rs.getString(campos.getString("SQL.T_TRM.primeraLetra")));
        gVO.setAtributo("numHasta",rs.getString(campos.getString("SQL.T_TRM.ultimoNumero")));
        gVO.setAtributo("letraHasta",rs.getString(campos.getString("SQL.T_TRM.ultimaLetra")));
        tramosDestino.add(gVO);
      }
      rs.close();
      stmt.close();
      resultado.setAtributo("tramosOrigen",tramosOrigen);
      resultado.setAtributo("tramosDestino",tramosDestino);
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
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en TramerosDAO.getListaTrameros");
        }
    }
    return resultado;
  }

  public Vector modificarTramero(GeneralValueObject gVO, String[] params){
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
      sql = "UPDATE T_TRM SET " +
        campos.getString("SQL.T_TRM.tipoNumeracion")+"="+gVO.getAtributo("tipoNumeracion")+","+
        campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+","+
        campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+","+
        campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+","+
        campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("codVia")+","+
        campos.getString("SQL.T_TRM.codigo")+"="+gVO.getAtributo("codTramo")+",";
        if (gVO.getAtributo("codPaisManzana")!=null)
            if (!"".equals(gVO.getAtributo("codPaisManzana")))
                sql += campos.getString("SQL.T_TRM.paisManzana")+"="+gVO.getAtributo("codPaisManzana")+",";
        if (gVO.getAtributo("codProvinciaManzana")!=null)
            if (!"".equals(gVO.getAtributo("codProvinciaManzana")))
                sql += campos.getString("SQL.T_TRM.provinciaManzana")+"="+gVO.getAtributo("codProvinciaManzana")+",";
        if (gVO.getAtributo("codMunicipioManzana")!=null)
                    if (!"".equals(gVO.getAtributo("codMunicipioManzana")))
                        sql += campos.getString("SQL.T_TRM.municipioManzana")+"="+gVO.getAtributo("codMunicipioManzana")+",";
        if (gVO.getAtributo("codDistritoManzana")!=null)
            if (!"".equals(gVO.getAtributo("codDistritoManzana")))
                sql += campos.getString("SQL.T_TRM.distritoManzana")+"="+gVO.getAtributo("codDistritoManzana")+",";
        if (gVO.getAtributo("codSeccionManzana")!=null)
            if (!"".equals(gVO.getAtributo("codSeccionManzana")))
                sql += campos.getString("SQL.T_TRM.seccionManzana")+"="+gVO.getAtributo("codSeccionManzana")+",";
        if (gVO.getAtributo("codLetraManzana")!=null)
            if (!"".equals(gVO.getAtributo("codLetraManzana")))
                sql += campos.getString("SQL.T_TRM.letraManzana")+"='"+gVO.getAtributo("codLetraManzana")+"',";
        if (gVO.getAtributo("codManzana")!=null)
            if (!"".equals(gVO.getAtributo("codManzana")))
                sql += campos.getString("SQL.T_TRM.manzana")+"='"+gVO.getAtributo("codManzana")+"',";
        sql += campos.getString("SQL.T_TRM.paisSeccion")+"="+gVO.getAtributo("codPaisSeccion")+","+
        campos.getString("SQL.T_TRM.provinciaSeccion")+"="+gVO.getAtributo("codProvinciaSeccion")+","+
        campos.getString("SQL.T_TRM.municipioSeccion")+"="+gVO.getAtributo("codMunicipioSeccion")+","+
        campos.getString("SQL.T_TRM.distritoSeccion")+"="+gVO.getAtributo("codDistritoSeccion")+","+
        campos.getString("SQL.T_TRM.seccion")+"="+gVO.getAtributo("codSeccion")+","+
        campos.getString("SQL.T_TRM.letraSeccion")+"='"+gVO.getAtributo("letraSeccion")+"',"+
        campos.getString("SQL.T_TRM.paisSubseccion")+"='"+gVO.getAtributo("codPaisSubSeccion")+"',"+
        campos.getString("SQL.T_TRM.provinciaSubseccion")+"='"+gVO.getAtributo("codProvinciaSubSeccion")+"',"+
        campos.getString("SQL.T_TRM.municipioSubseccion")+"='"+gVO.getAtributo("codMunicipioSubSeccion")+"',"+
        campos.getString("SQL.T_TRM.distritoSubseccion")+"='"+gVO.getAtributo("codDistritoSubSeccion")+"',"+
        campos.getString("SQL.T_TRM.seccionSubseccion")+"='"+gVO.getAtributo("codSeccionSubSeccion")+"',"+
        campos.getString("SQL.T_TRM.letraSubseccion")+"='"+gVO.getAtributo("codLetraSubSeccion")+"',"+
        campos.getString("SQL.T_TRM.subseccion")+"='"+gVO.getAtributo("codSubSeccion")+"',"+
        campos.getString("SQL.T_TRM.codigoPostal")+"='"+gVO.getAtributo("codPostal")+"',"+
        campos.getString("SQL.T_TRM.primerNumero")+"=";
        if (gVO.getAtributo("numDesde")!= null){
            if (!"".equals((String) gVO.getAtributo("numDesde")) )
                sql += gVO.getAtributo("numDesde")+",";
            else sql+= "null,";
        }else sql+= "null,";
        sql +=campos.getString("SQL.T_TRM.primeraLetra")+"='";
        if ("0".equals((String) gVO.getAtributo("codTipoNumeracion")))
            sql += "S',";
        else sql +=gVO.getAtributo("letraDesde")+"',";
        sql+=campos.getString("SQL.T_TRM.ultimoNumero")+"=";
        if (gVO.getAtributo("numHasta")!= null){
            if (!"".equals((String) gVO.getAtributo("numHasta")) )
                sql += gVO.getAtributo("numHasta")+",";
            else sql += "null,";
        } else sql+= "null,";
        sql += campos.getString("SQL.T_TRM.ultimaLetra")+"='"+gVO.getAtributo("letraHasta")+"',"+
        campos.getString("SQL.T_TRM.e_singularNUC")+"="+gVO.getAtributo("codESITramo")+","+
        campos.getString("SQL.T_TRM.nucleo")+"="+gVO.getAtributo("codNUCTramo")+
        " WHERE " +
        campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
        campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
        campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("codVia")+" AND "+
        campos.getString("SQL.T_TRM.tipoNumeracion")+"="+gVO.getAtributo("tipoNumeracion")+" AND "+
        campos.getString("SQL.T_TRM.codigo")+"="+gVO.getAtributo("codTramo");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      int res = stmt.executeUpdate(sql);
      //m_Log.debug("las filas afectadas en el update son : " + res);
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    //gVO.setAtributo("idVia", (String) gVO.getAtributo("codVia"));
    resultado = getListaTrameros(gVO,params);
    return resultado;
  }

  public Vector altaTramero(GeneralValueObject gVO, String[] params){
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

      sql = "SELECT " + abd.funcionSistema(abd.FUNCIONSISTEMA_NVL,
                                    new String[]{abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.T_TRM.codigo")})+"+1",
                                            "1"}) +" AS MAXIMO" +
                       " FROM T_TRM WHERE "+
      campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
      campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
      campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
      campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("codVia")+" AND "+
      campos.getString("SQL.T_TRM.tipoNumeracion")+"="+gVO.getAtributo("tipoNumeracion");

      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      int max = 1;
      if(rs.next()){
        max = rs.getInt("MAXIMO");
      }
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
        // NUCLEO
        campos.getString("SQL.T_TRM.paisNUC")+","+
        campos.getString("SQL.T_TRM.provinciaNUC")+","+
        campos.getString("SQL.T_TRM.municipioNUC")+","+
        campos.getString("SQL.T_TRM.e_singularNUC")+","+
        campos.getString("SQL.T_TRM.nucleo") +","+
        campos.getString("SQL.T_TRM.situacion") +
        ") VALUES (" +
        gVO.getAtributo("tipoNumeracion")+","+
        gVO.getAtributo("codPais")+","+
        gVO.getAtributo("codProvincia")+","+
        gVO.getAtributo("codMunicipio")+","+
        gVO.getAtributo("codVia")+","+
        //¡CORREGIR!
        max+","+
        gVO.getAtributoONulo("codPaisManzana")+","+
        gVO.getAtributoONulo("codProvinciaManzana")+","+
        gVO.getAtributoONulo("codMunicipioManzana")+","+
        gVO.getAtributoONulo("codDistritoManzana")+","+
        gVO.getAtributoONulo("codSeccionManzana")+",'"+
        gVO.getAtributo("codLetraManzana")+"','"+
        gVO.getAtributo("codManzana")+"',"+
        gVO.getAtributo("codPaisSeccion")+","+
        gVO.getAtributo("codProvinciaSeccion")+","+
        gVO.getAtributo("codMunicipioSeccion")+","+
        gVO.getAtributo("codDistritoSeccion")+","+
        gVO.getAtributo("codSeccion")+",'"+
        gVO.getAtributo("letraSeccion")+"',";
        String subSecc = (String) gVO.getAtributo("codSubSeccion");
        if (subSecc != null)
            if (!"".equals(subSecc))
                sql += gVO.getAtributo("codPaisSubSeccion")+","
                        +gVO.getAtributo("codProvinciaSubSeccion")+","
                        +gVO.getAtributo("codMunicipioSubSeccion")+","
                        +gVO.getAtributo("codDistritoSubSeccion")+","
                        +gVO.getAtributo("codSeccionSubSeccion")+","
                        +"'"+gVO.getAtributo("codLetraSubSeccion")+","
                        +"'"+gVO.getAtributo("codSubSeccion")+"',";
            else sql += "null,null,null,null,null,null,null,";
        else  sql += "null,null,null,null,null,null,null,";
        sql += "'" + gVO.getAtributo("codPostal")+"',";
        if (gVO.getAtributo("numDesde")!= null){
            if (!"".equals(gVO.getAtributo("numDesde"))){
                sql += gVO.getAtributo("numDesde")+",'";
            } else sql+= "null,'";
        }else sql+= "null,'";
        if ("0".equals((String) gVO.getAtributo("tipoNumeracion")))
            sql += "S',";
        else sql +=gVO.getAtributo("letraDesde")+"',";
        if (gVO.getAtributo("numHasta")!= null){
            if (!"".equals((String) gVO.getAtributo("numHasta"))){
                sql += gVO.getAtributo("numHasta")+",'";
            }else sql+= "null,'";
        } else sql+= "null,'";
        sql += gVO.getAtributo("letraHasta")+"', " +
        gVO.getAtributo("codPais")+","+
        gVO.getAtributo("codProvincia")+","+
        gVO.getAtributo("codMunicipio")+",";
        if (gVO.getAtributo("codESI")!= null) {
            if (!"".equals( (String) gVO.getAtributo("codESI") ) ){
                sql += gVO.getAtributo("codESI")+","+
                gVO.getAtributo("codNUC");
            } else {
                sql += gVO.getAtributo("codESITramo")+","+
                gVO.getAtributo("codNUCTramo");
            }
        } else {
            sql += gVO.getAtributo("codESITramo")+","+
            gVO.getAtributo("codNUCTramo");
        }
        sql += ",'A'" + " )";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      //m_Log.debug("las filas afectadas en el insert son : " + res);
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    //gVO.setAtributo("idVia", (String) gVO.getAtributo("codVia"));
    resultado = getListaTrameros(gVO,params);
    return resultado;
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