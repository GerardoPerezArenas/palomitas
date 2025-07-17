// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.*;

import java.sql.*;

import java.util.Vector;
import java.util.ArrayList;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: DomiciliosDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class DomiciliosDAO  {
  private static DomiciliosDAO instance = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(DomiciliosDAO.class.getName());

  protected DomiciliosDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static DomiciliosDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (DomiciliosDAO.class) {
        if (instance == null) {
          instance = new DomiciliosDAO();
        }
      }
    }
    return instance;
  }

  public GeneralValueObject eliminarDomicilio(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    String correcto = "SI";
    GeneralValueObject resultado = new GeneralValueObject();
    try{
      abd = new AdaptadorSQLBD(params);
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
      sql = "UPDATE T_DPO SET " +
        campos.getString("SQL.T_DPO.situacion")+"='B',"+
        campos.getString("SQL.T_DPO.fechaBaja")+"="+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
        campos.getString("SQL.T_DPO.usuarioBaja")+"="+gVO.getAtributo("usuario")+
        " WHERE "+
        campos.getString("SQL.T_DPO.domicilio")+"="+gVO.getAtributo("codDPO");
      //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      sql = "UPDATE T_DSU SET " +
        campos.getString("SQL.T_DSU.situacion")+"='B',"+
        campos.getString("SQL.T_DSU.fechaBaja")+"="+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
        campos.getString("SQL.T_DSU.usuarioBaja")+"="+gVO.getAtributo("usuario")+
        " WHERE "+
        campos.getString("SQL.T_DSU.identificador")+"="+gVO.getAtributo("codDSU");
      //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      int terceros = Integer.parseInt((String)gVO.getAtributo("tieneTerceros"));
      //m_Log.debug("TercerosImpl: "+terceros);
      if(terceros>0){
        sql = "UPDATE T_DOT SET " +
          campos.getString("SQL.T_DOT.situacion")+"='B',"+
          campos.getString("SQL.T_DOT.fecha")+"="+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
          campos.getString("SQL.T_DOT.usuario")+"="+gVO.getAtributo("usuario")+
          " WHERE "+
          campos.getString("SQL.T_DOT.idDomicilio")+"="+gVO.getAtributo("codDPO");
        //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        stmt.executeUpdate(sql);
      }
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
      correcto = "NO";
    }finally{
      commitTransaction(abd,conexion);
    }
    resultado.setAtributo("operacion",correcto);
    resultado.setAtributo("domicilios",getListaDomicilios(gVO,params));
    return resultado;
  }

  private String[] tieneHabitantes(Connection conexion,String codDPO) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String[] res = new String[2];
    String sql = "SELECT COUNT(HAB_COD) AS HABITANTES "+
      "FROM P_HAB,P_HOJ "+
      "WHERE "+
      campos.getString("SQL.P_HAB.hoja")+"="+campos.getString("SQL.P_HOJ.numero")+" AND "+
      campos.getString("SQL.P_HOJ.domicilio")+"="+codDPO;
    stmt = conexion.createStatement();
    rs = stmt.executeQuery(sql);
    if(rs.next()){
      res[0] = rs.getString("HABITANTES");
    }
    sql = "SELECT COUNT(DOT_TER) AS TERCEROS FROM T_DOT "+
      " WHERE "+
      campos.getString("SQL.T_DOT.idDomicilio")+"="+codDPO;
    rs = stmt.executeQuery(sql);
    if(rs.next()){
      res[1] = rs.getString("TERCEROS");
    }
    rs.close();
    stmt.close();
    return res;
  }

  public Vector getListaDomicilios(GeneralValueObject parametros,String[] params){
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

      String from = "T_DPO.*,T_DSU.*,T_TRM.*,T_ESC.*,T_PLT.*,T_TNU.*,T_TVV.* ";

      String where = "";
      if(parametros.getAtributo("idVia").equals("131")){
        where = campos.getString("SQL.T_TRM.e_singularNUC")+"=" + parametros.getAtributo("codESI")+ " AND " +
                campos.getString("SQL.T_TRM.nucleo")+"=" + parametros.getAtributo("codNUC");
      }

      ArrayList join = new ArrayList();

      join.add("T_DSU");
      join.add("INNER");
      join.add("T_DPO");
      join.add(campos.getString("SQL.T_DSU.situacion")+"='A' AND "+
        campos.getString("SQL.T_DSU.pais")+"="+parametros.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_DSU.provincia")+"="+parametros.getAtributo("codProvincia")+" AND "+
        campos.getString("SQL.T_DSU.municipio")+"="+parametros.getAtributo("codMunicipio")+" AND "+
        campos.getString("SQL.T_DSU.vial")+"="+parametros.getAtributo("idVia")+ " AND " +
        campos.getString("SQL.T_DPO.suelo")+"="+campos.getString("SQL.T_DSU.identificador"));
      join.add("LEFT");
      join.add("T_TVV");
      join.add(campos.getString("SQL.T_DPO.tipoVivienda")+"="+campos.getString("SQL.T_TVV.codigo"));
      join.add("LEFT");
      join.add(GlobalNames.ESQUEMA_GENERICO+"T_ESC T_ESC");
      join.add(campos.getString("SQL.T_DPO.escalera")+"="+campos.getString("SQL.T_ESC.codigo"));
      join.add("LEFT");
      join.add(GlobalNames.ESQUEMA_GENERICO+"T_PLT T_PLT");
      join.add(campos.getString("SQL.T_DPO.planta")+"="+campos.getString("SQL.T_PLT.codigo"));
      join.add("LEFT");
      join.add("T_TNU");
      join.add(campos.getString("SQL.T_DSU.tipoNumeracion")+"="+campos.getString("SQL.T_TNU.codigo"));
      join.add("INNER");
      join.add("T_TRM");
      join.add(campos.getString("SQL.T_DSU.paisTRM")+"=" + campos.getString("SQL.T_TRM.pais")+ " AND " +
               campos.getString("SQL.T_DSU.provinciaTRM")+"=" + campos.getString("SQL.T_TRM.provincia") + " AND " +
               campos.getString("SQL.T_DSU.municipioTRM")+"=" + campos.getString("SQL.T_TRM.municipio") + " AND "+
               campos.getString("SQL.T_DSU.vialTRM")+"=" + campos.getString("SQL.T_TRM.vial")+ " AND " +
               campos.getString("SQL.T_DSU.codigoTRM")+"=" + campos.getString("SQL.T_TRM.codigo") + " AND " +
               campos.getString("SQL.T_DSU.tipoNumeracionTRM")+"=" + campos.getString("SQL.T_TRM.tipoNumeracion"));
      join.add("false");

      sql = abd.join(from,where, (String[]) join.toArray(new String[]{})) +
            " ORDER BY "+campos.getString("SQL.T_DSU.numeroDesde")+","+
                         campos.getString("SQL.T_DSU.letraDesde")+","+campos.getString("SQL.T_DSU.bloque")+","+
                         campos.getString("SQL.T_DSU.portal")+","+campos.getString("SQL.T_DPO.escalera")+","+
                         campos.getString("SQL.T_DPO.planta")+","+campos.getString("SQL.T_DPO.puerta");

//      // Creamos la select con los parametros adecuados.
//      sql = "SELECT T_DPO.*,T_DSU.*,T_TRM.*,T_ESC.*,T_PLT.*,T_TNU.*,T_TVV.* "+
//        " FROM T_DPO,T_DSU,T_TRM,"+ GlobalNames.ESQUEMA_GENERICO+"T_ESC T_ESC,"+
//              GlobalNames.ESQUEMA_GENERICO+"T_PLT T_PLT,T_TNU,T_TVV WHERE ";
//      // CONDICIONES
//      sql+= campos.getString("SQL.T_DSU.situacion")+"='A' AND "+
//        campos.getString("SQL.T_DSU.pais")+"="+parametros.getAtributo("codPais")+" AND "+
//        campos.getString("SQL.T_DSU.provincia")+"="+parametros.getAtributo("codProvincia")+" AND "+
//        campos.getString("SQL.T_DSU.municipio")+"="+parametros.getAtributo("codMunicipio")+" AND "+
//        campos.getString("SQL.T_DSU.vial")+"="+parametros.getAtributo("idVia")+ " AND " +
//        campos.getString("SQL.T_DPO.suelo")+"="+campos.getString("SQL.T_DSU.identificador")+" AND "+
//        campos.getString("SQL.T_DPO.tipoVivienda")+"="+campos.getString("SQL.T_TVV.codigo")+"(+) AND "+
//        campos.getString("SQL.T_DPO.escalera")+"="+campos.getString("SQL.T_ESC.codigo")+"(+) AND "+
//        campos.getString("SQL.T_DPO.planta")+"="+campos.getString("SQL.T_PLT.codigo")+"(+) AND "+
//        campos.getString("SQL.T_DSU.tipoNumeracion")+"="+campos.getString("SQL.T_TNU.codigo")+"(+) AND "+
//        campos.getString("SQL.T_DSU.paisTRM")+"=" + campos.getString("SQL.T_TRM.pais")+ " AND " +
//        campos.getString("SQL.T_DSU.provinciaTRM")+"=" + campos.getString("SQL.T_TRM.provincia") + " AND " +
//        campos.getString("SQL.T_DSU.municipioTRM")+"=" + campos.getString("SQL.T_TRM.municipio") + " AND "+
//        campos.getString("SQL.T_DSU.vialTRM")+"=" + campos.getString("SQL.T_TRM.vial")+ " AND " +
//        campos.getString("SQL.T_DSU.codigoTRM")+"=" + campos.getString("SQL.T_TRM.codigo") + " AND " +
//        campos.getString("SQL.T_DSU.tipoNumeracionTRM")+"=" + campos.getString("SQL.T_TRM.tipoNumeracion");
//
//      if(parametros.getAtributo("idVia").equals("131")){
//        sql+=" AND " +
//          campos.getString("SQL.T_TRM.e_singularNUC")+"=" + parametros.getAtributo("codESI")+ " AND " +
//          campos.getString("SQL.T_TRM.nucleo")+"=" + parametros.getAtributo("codNUC");
//      }
//      sql+= " ORDER BY "+campos.getString("SQL.T_DSU.numeroDesde")+","+
//        campos.getString("SQL.T_DSU.letraDesde")+","+campos.getString("SQL.T_DSU.bloque")+","+
//        campos.getString("SQL.T_DSU.portal")+","+campos.getString("SQL.T_DPO.escalera")+","+
//        campos.getString("SQL.T_DPO.planta")+","+campos.getString("SQL.T_DPO.puerta");

      if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        // TRAMO
        String codDPO = rs.getString(campos.getString("SQL.T_DPO.domicilio"));
        gVO.setAtributo("codDPO",codDPO);
        gVO.setAtributo("codDSU",rs.getString(campos.getString("SQL.T_DPO.suelo")));
        gVO.setAtributo("codEscalera",rs.getString(campos.getString("SQL.T_DPO.escalera")));
        gVO.setAtributo("descEscalera",rs.getString(campos.getString("SQL.T_ESC.descripcion")));
        gVO.setAtributo("codPlanta",rs.getString(campos.getString("SQL.T_DPO.planta")));
        gVO.setAtributo("descPlanta",rs.getString(campos.getString("SQL.T_PLT.descripcion")));
        gVO.setAtributo("puerta",rs.getString(campos.getString("SQL.T_DPO.puerta")));
        gVO.setAtributo("observaciones",rs.getString(campos.getString("SQL.T_DPO.observaciones")));
        gVO.setAtributo("situacion",rs.getString(campos.getString("SQL.T_DPO.situacion")));
        gVO.setAtributo("codTipoVivienda",rs.getString(campos.getString("SQL.T_DPO.tipoVivienda")));
        gVO.setAtributo("descTipoVivienda",rs.getString(campos.getString("SQL.T_TVV.descripcion")));
        gVO.setAtributo("codTipoNumeracion",rs.getString(campos.getString("SQL.T_DSU.tipoNumeracion")));
        gVO.setAtributo("descTipoNumeracion",rs.getString(campos.getString("SQL.T_TNU.descripcion")));
        gVO.setAtributo("codPostal",rs.getString(campos.getString("SQL.T_DSU.codigoPostal")));
        gVO.setAtributo("numDesde",rs.getString(campos.getString("SQL.T_DSU.numeroDesde")));
        gVO.setAtributo("letraDesde",rs.getString(campos.getString("SQL.T_DSU.letraDesde")));
        gVO.setAtributo("numHasta",rs.getString(campos.getString("SQL.T_DSU.numeroHasta")));
        gVO.setAtributo("letraHasta",rs.getString(campos.getString("SQL.T_DSU.letraHasta")));
        gVO.setAtributo("bloque",rs.getString(campos.getString("SQL.T_DSU.bloque")));
        gVO.setAtributo("portal",rs.getString(campos.getString("SQL.T_DSU.portal")));
        gVO.setAtributo("km",rs.getString(campos.getString("SQL.T_DSU.kilometro")));
        gVO.setAtributo("hm",rs.getString(campos.getString("SQL.T_DSU.hectometro")));
        gVO.setAtributo("distrito",rs.getString(campos.getString("SQL.T_TRM.distritoSeccion")));
        gVO.setAtributo("seccion",rs.getString(campos.getString("SQL.T_TRM.seccion")));
        gVO.setAtributo("letra",rs.getString(campos.getString("SQL.T_TRM.letraSeccion")));
        gVO.setAtributo("codTramo",rs.getString(campos.getString("SQL.T_DSU.codigoTRM")));
        String[] res = tieneHabitantes(conexion,codDPO);
        gVO.setAtributo("tieneHabitantes",res[0]);
        gVO.setAtributo("tieneTerceros",res[1]);
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
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en DomiciliosDAO.getListaDomicilios");
        }
    }
    return resultado;
  }

  public GeneralValueObject modificarDomicilio(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    Statement stmt1 = null;
    ResultSet rs = null;
    String sql = "";
    String correcto = "SI";
    GeneralValueObject resultado = new GeneralValueObject();
    try{
      abd = new AdaptadorSQLBD(params);
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
      stmt1 = conexion.createStatement();
      // ACTUALIZO LAS DIRECCIONES SUELO AFECTADAS
      actualizarDSUs(stmt,stmt1,gVO,abd);
      // ACTUALIZO LAS DIRECCIONES POSTALES AFECTADAS
      actualizarDomicilios(stmt,gVO,abd);
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
      correcto = "NO";
    }finally{
      commitTransaction(abd,conexion);
    }
    resultado.setAtributo("operacion",correcto);
    resultado.setAtributo("domicilios",getListaDomicilios(gVO,params));
    return resultado;
  }

  private String[] buscarNuevoTramo(Statement stmt,ResultSet rs,GeneralValueObject gVO)
    throws Exception {
    String[] nuevoTramo = new String[2];
    ResultSet rs1 = null;
    String sql = "";
    sql = "SELECT T_TRM.* FROM T_TRM WHERE "+
      campos.getString("SQL.T_TRM.situacion")+"='A' AND "+
      campos.getString("SQL.T_TRM.pais")+"="+gVO.getAtributo("codPais")+" AND "+
      campos.getString("SQL.T_TRM.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
      campos.getString("SQL.T_TRM.municipio")+"="+gVO.getAtributo("codMunicipio")+" AND "+
      campos.getString("SQL.T_TRM.vial")+"="+gVO.getAtributo("idVia")+" AND "+
      campos.getString("SQL.T_TRM.tipoNumeracion")+"="+gVO.getAtributo("codTipoNumeracion");
    //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
    rs1 = stmt.executeQuery(sql);
    int distritoDSU = rs.getInt(campos.getString("SQL.P_HOJ.distrito"));
    int seccionDSU = rs.getInt(campos.getString("SQL.P_HOJ.seccion"));
    String letraDSU = rs.getString(campos.getString("SQL.P_HOJ.letra"));
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
          break;
        }
      }else{
        if((numDesdeDSU>=numDesde)&&(numDesdeDSU<=numHasta)){
          nuevoTramo[0] = codTramo;
          nuevoTramo[1] = "NO";
          if((distrito!=distritoDSU)||(seccion!=seccionDSU)||(!letra.equals(letraDSU))){
            nuevoTramo[1] = "SI";
          }
          break;
        }
      }
    }
    stmt.close();
    return nuevoTramo;
  }

  private void actualizarDSUs(Statement stmt,Statement stmt1,GeneralValueObject gVO,AdaptadorSQLBD abd)
    throws Exception {
    String sql = "";
    ResultSet rs = null;
    ResultSet rs1 = null;
    Vector hojas = new Vector();
    sql = "SELECT P_HOJ.*,T_DPO.*,T_DSU.* FROM P_HOJ,T_DPO,T_DSU " +
      " WHERE "+
      campos.getString("SQL.T_DSU.situacion")+"='A' AND " +
      campos.getString("SQL.T_DSU.identificador")+"="+gVO.getAtributo("codDSU")+" AND "+
      campos.getString("SQL.T_DPO.suelo")+"="+campos.getString("SQL.T_DSU.identificador")+" AND "+
      abd.joinRight(campos.getString("SQL.P_HOJ.domicilio"),campos.getString("SQL.T_DPO.domicilio"));     

    sql+= " ORDER BY "+campos.getString("SQL.T_DSU.numeroDesde")+","+
      campos.getString("SQL.T_DSU.letraDesde")+","+campos.getString("SQL.T_DSU.bloque")+","+
      campos.getString("SQL.T_DSU.portal")+","+campos.getString("SQL.T_DPO.escalera")+","+
      campos.getString("SQL.T_DPO.planta")+","+campos.getString("SQL.T_DPO.puerta");
    //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
    rs = stmt.executeQuery(sql);
    while(rs.next()){
      String idDSU = rs.getString(campos.getString("SQL.T_DSU.identificador"));
      String haCambiadoNumeracion = (String)gVO.getAtributo("haCambiadoNumeracion");
      String haCambiadoDistrito = (String)gVO.getAtributo("haCambiadoDistrito");
      int idNuevaDSU = 0;
      sql = "SELECT "+ abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.T_DSU.identificador")}) + " AS MAXIMO" +
            " FROM T_DSU";
      rs1 = stmt1.executeQuery(sql);
      if(rs1.next()){
        idNuevaDSU = rs1.getInt("MAXIMO");
      }
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
        gVO.getAtributo("codTipoNumeracion")+","+
        campos.getString("SQL.T_DSU.hoja")+","+
        campos.getString("SQL.T_DSU.parcela")+","+
        campos.getString("SQL.T_DSU.cpoPais")+","+
        campos.getString("SQL.T_DSU.cpoProvincia")+","+
        campos.getString("SQL.T_DSU.cpoMunicipio")+","+
        gVO.getAtributo("codigoPostal")+","+
        campos.getString("SQL.T_DSU.distritoPais")+","+
        campos.getString("SQL.T_DSU.distritoProvincia")+","+
        campos.getString("SQL.T_DSU.distritoMunicipio")+","+
        gVO.getAtributo("distrito")+","+
        gVO.getAtributo("seccion")+",'"+
        gVO.getAtributo("letra")+"',"+
        campos.getString("SQL.T_DSU.paisESI")+","+
        campos.getString("SQL.T_DSU.provinciaESI")+","+
        campos.getString("SQL.T_DSU.municipioESI")+","+
        campos.getString("SQL.T_DSU.e_singular")+","+
        gVO.getAtributo("numDesde")+",'"+
        gVO.getAtributo("letraDesde")+"','"+
        gVO.getAtributo("numHasta")+"','"+
        gVO.getAtributo("letraHasta")+"','"+
        gVO.getAtributo("bloque")+"','"+
        gVO.getAtributo("portal")+"',"+
        campos.getString("SQL.T_DSU.caserio")+","+
        campos.getString("SQL.T_DSU.origenX")+","+
        campos.getString("SQL.T_DSU.origenY")+","+
        campos.getString("SQL.T_DSU.finX")+","+
        campos.getString("SQL.T_DSU.finY")+","+
        campos.getString("SQL.T_DSU.verificacion")+",'"+
        gVO.getAtributo("km")+"','"+
        gVO.getAtributo("hm")+"',"+
        campos.getString("SQL.T_DSU.imagen")+","+
        "'A',"+
        abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
        gVO.getAtributo("usuario")+","+
        "null,"+
        "null,"+
        abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
        gVO.getAtributo("codTipoNumeracion")+","+
        campos.getString("SQL.T_DSU.paisTRM")+","+
        campos.getString("SQL.T_DSU.provinciaTRM")+","+
        campos.getString("SQL.T_DSU.municipioTRM")+","+
        campos.getString("SQL.T_DSU.vialTRM")+","+
        gVO.getAtributo("codTramo")+
        " FROM T_DSU WHERE "+
        campos.getString("SQL.T_DSU.identificador")+"="+idDSU;
      //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt1.executeUpdate(sql);
      // DOY DE BAJA LA DIRECCION SUELO ANTIGUA
      sql = "UPDATE T_DSU SET "+
        campos.getString("SQL.T_DSU.situacion")+"='B',"+
        campos.getString("SQL.T_DSU.fechaBaja")+"="+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
        campos.getString("SQL.T_DSU.usuarioBaja")+"="+gVO.getAtributo("usuario")+
        " WHERE "+
        campos.getString("SQL.T_DSU.identificador")+"="+idDSU;
      //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt1.executeUpdate(sql);
      GeneralValueObject hoja = new GeneralValueObject();
      String distrito = rs.getString(campos.getString("SQL.P_HOJ.distrito"));
      String seccion = rs.getString(campos.getString("SQL.P_HOJ.seccion"));
      String letra = rs.getString(campos.getString("SQL.P_HOJ.letra"));
      String numeroHoja = rs.getString(campos.getString("SQL.P_HOJ.numero"));
      String version = rs.getString(campos.getString("SQL.P_HOJ.version"));
      String contador = rs.getString(campos.getString("SQL.P_HOJ.contador"));
      hoja.setAtributo("codDSU",String.valueOf(idNuevaDSU));
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
    gVO.setAtributo("hojas",hojas);
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
      idNuevoDOM++;
      sql = "INSERT INTO T_DOM("+
        campos.getString("SQL.T_DOM.idDomicilio")+","+
        campos.getString("SQL.T_DOM.normalizado")+
        ") VALUES ("+
        idNuevoDOM+","+
        "1)";
      //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
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
        idNuevaDSU+",'"+
        gVO.getAtributo("escalera")+"','"+
        gVO.getAtributo("planta")+"','"+
        gVO.getAtributo("codTipoVivienda")+"','"+
        gVO.getAtributo("puerta")+"',"+
        campos.getString("SQL.T_DPO.observaciones")+","+
        "'A',"+
        abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
        gVO.getAtributo("usuario")+","+
        "null,"+
        "null,"+
        abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
        " FROM T_DPO WHERE "+
        campos.getString("SQL.T_DPO.domicilio")+"="+idDPO;
      //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      // DOY DE BAJA LA DIRECCION POSTAL ANTIGUA
      sql = "UPDATE T_DPO SET "+
        campos.getString("SQL.T_DPO.situacion")+"='B',"+
        campos.getString("SQL.T_DPO.fechaBaja")+"="+abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
        campos.getString("SQL.T_DPO.usuarioBaja")+"="+gVO.getAtributo("usuario")+
        " WHERE "+
        campos.getString("SQL.T_DPO.domicilio")+"="+idDPO;
      //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      hoja.setAtributo("codDPO",String.valueOf(idNuevoDOM));
    }
  }

  public GeneralValueObject altaDomicilio(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String correcto = "SI";
    GeneralValueObject resultado = new GeneralValueObject();
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      stmt = conexion.createStatement();
      int idNuevaDSU = 0;
      sql = "SELECT "+abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.T_DSU.identificador")})+ " AS MAXIMO"+
        " FROM T_DSU";
      rs = stmt.executeQuery(sql);
      if(rs.next()){
        idNuevaDSU = rs.getInt("MAXIMO");
      }
      idNuevaDSU++;
      // INSERTO LAS NUEVAS DIRECCIONES SUELO
      sql = "INSERT INTO T_DSU("+
        campos.getString("SQL.T_DSU.pais")+","+
        campos.getString("SQL.T_DSU.provincia")+","+
        campos.getString("SQL.T_DSU.municipio")+","+
        campos.getString("SQL.T_DSU.vial")+","+
        campos.getString("SQL.T_DSU.identificador")+","+
        campos.getString("SQL.T_DSU.tipoNumeracion")+","+
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
        campos.getString("SQL.T_DSU.kilometro")+","+
        campos.getString("SQL.T_DSU.hectometro")+","+
        campos.getString("SQL.T_DSU.situacion")+","+
        campos.getString("SQL.T_DSU.fechaAlta")+","+
        campos.getString("SQL.T_DSU.usuarioAlta")+","+
        campos.getString("SQL.T_DSU.fechaVigencia")+","+
        campos.getString("SQL.T_DSU.tipoNumeracionTRM")+","+
        campos.getString("SQL.T_DSU.paisTRM")+","+
        campos.getString("SQL.T_DSU.provinciaTRM")+","+
        campos.getString("SQL.T_DSU.municipioTRM")+","+
        campos.getString("SQL.T_DSU.vialTRM")+","+
        campos.getString("SQL.T_DSU.codigoTRM")+
        ") VALUES (" +
        gVO.getAtributo("codPais")+","+
        gVO.getAtributo("codProvincia")+","+
        gVO.getAtributo("codMunicipio")+","+
        gVO.getAtributo("idVia")+","+
        idNuevaDSU+","+
        gVO.getAtributo("codTipoNumeracion")+","+
        gVO.getAtributo("codPais")+","+
        gVO.getAtributo("codProvincia")+","+
        gVO.getAtributo("codMunicipio")+",'"+
        gVO.getAtributo("codigoPostal")+"',"+
        gVO.getAtributo("codPais")+","+
        gVO.getAtributo("codProvincia")+","+
        gVO.getAtributo("codMunicipio")+","+
        gVO.getAtributo("distrito")+","+
        gVO.getAtributo("seccion")+",'"+
        gVO.getAtributo("letra")+"',"+
        gVO.getAtributo("codPais")+","+
        gVO.getAtributo("codProvincia")+","+
        gVO.getAtributo("codMunicipio")+","+
        gVO.getAtributo("codESI")+","+
        gVO.getAtributo("numDesde")+",'"+
        gVO.getAtributo("letraDesde")+"','"+
        gVO.getAtributo("numHasta")+"','"+
        gVO.getAtributo("letraHasta")+"','"+
        gVO.getAtributo("bloque")+"','"+
        gVO.getAtributo("portal")+"','"+
        gVO.getAtributo("km")+"','"+
        gVO.getAtributo("hm")+"',"+
        "'A',"+
         abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
        gVO.getAtributo("usuario")+","+
        abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
        gVO.getAtributo("codTipoNumeracion")+","+
        gVO.getAtributo("codPais")+","+
        gVO.getAtributo("codProvincia")+","+
        gVO.getAtributo("codMunicipio")+","+
        gVO.getAtributo("idVia")+","+
        gVO.getAtributo("codTramo")+")";
      //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      int idNuevoDOM = 0;
      sql = "SELECT "+abd.funcionMatematica(abd.FUNCIONMATEMATICA_MAX,new String[]{campos.getString("SQL.T_DOM.idDomicilio")})+" AS MAXIMO"+
      " FROM T_DOM";
      rs = stmt.executeQuery(sql);
      if(rs.next()){
        idNuevoDOM = rs.getInt("MAXIMO");
      }
      idNuevoDOM++;
      sql = "INSERT INTO T_DOM("+
        campos.getString("SQL.T_DOM.idDomicilio")+","+
        campos.getString("SQL.T_DOM.normalizado")+
        ") VALUES ("+
        idNuevoDOM+","+
        "1)";
      //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
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
        ") VALUES (" +
        idNuevoDOM+","+
        idNuevaDSU+",'"+
        gVO.getAtributo("escalera")+"','"+
        gVO.getAtributo("planta")+"','"+
        gVO.getAtributo("codTipoVivienda")+"','"+
        gVO.getAtributo("puerta")+"','"+
        gVO.getAtributo("observaciones")+"',"+
        "'A',"+
        abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+","+
        gVO.getAtributo("usuario")+","+
        "null,"+
        "null,"+
        abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null)+")";
      //if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt.executeUpdate(sql);
      //m_Log.debug("las filas afectadas en el insert son : " + res);
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
      correcto = "NO";
    }finally{
      commitTransaction(abd,conexion);
    }
    resultado.setAtributo("operacion",correcto);
    resultado.setAtributo("domicilios",getListaDomicilios(gVO,params));
    return resultado;
   }
  
  
  
  
   /**
    * Comprueba si existe un determinado domicilio
    * @param domVO: Objeto con los datos del domicilio
    * @param con: Conexión a la BBDD
    * @return id del domicilio
    * @throws TechnicalException si ocurre algún error
    */
   public int existeDomicilio(DomicilioSimpleValueObject domVO,Connection con) throws TechnicalException
   {        
        ResultSet rs = null;
        Statement state = null;
        int resultado = 0;
        String sql="";

        try{
            state = con.createStatement();
            
            sql = "SELECT DNN_DOM FROM T_DNN WHERE "+
                  "DNN_PAI=" + domVO.getIdPais() + " AND ";
            
            if (domVO.getIdTipoVia()!=null && !"".equals(domVO.getIdTipoVia()))
                sql += " DNN_TVI="+ domVO.getIdTipoVia() +  " AND  ";            
            else sql += " DNN_TVI IS NULL AND ";
            
            if(!(domVO.getIdProvincia().equals("")))
                sql += "DNN_PRV=" + domVO.getIdProvincia() + " AND ";
            else sql += "DNN_PRV IS NULL AND ";
            
            if (!(domVO.getIdMunicipio()).equals(""))
                sql += "DNN_MUN="+ domVO.getIdMunicipio() + " AND ";
            else sql += "DNN_MUN IS NULL AND ";
                        
            if(!(domVO.getIdPaisVia().equals("")))
                sql += "DNN_VPA=" + domVO.getIdPaisVia() + " AND ";
            else sql += "DNN_VPA IS NULL and  ";
            
            if(!(domVO.getIdProvinciaVia().equals("")))
                sql += "DNN_VPR=" +domVO.getIdProvinciaVia() + " AND ";
            else sql += "DNN_VPR IS NULL AND ";
            
            if(!( domVO.getIdMunicipioVia().equals("")))
                sql += "DNN_VMU="+ domVO.getIdMunicipioVia() + " AND ";
            else sql += "DNN_VMU IS NULL AND ";
            
            if(!(domVO.getIdVia().equals("")))
                sql += "DNN_VIA="+ domVO.getIdVia() + " AND ";
            else sql += "DNN_VIA IS NULL AND ";
            
            if(!(domVO.getNumDesde()).equals(""))
                sql += "DNN_NUD="+ domVO.getNumDesde() + " AND ";
            else sql += "DNN_NUD= IS NULL AND ";
            
            if(!(domVO.getLetraDesde().equals("")))
                sql += "DNN_LED='"+ domVO.getLetraDesde() + "' AND ";
            else sql += "DNN_LED IS NULL AND ";
            
            if(!(domVO.getNumHasta().equals("")))
                sql += "DNN_NUH=" +  domVO.getNumHasta() + " AND ";
            else sql += "DNN_NUH IS NULL AND ";
            
            if(!(domVO.getLetraHasta().equals("")))
                sql += "DNN_LEH='"+ domVO.getLetraHasta() + "' AND ";
            else sql += "DNN_LEH IS NULL AND ";
            
            if(!(domVO.getBloque().equals("")))
                sql += "DNN_BLQ='"+ domVO.getBloque() + "' AND ";
            else sql += "DNN_BLQ IS NULL AND ";
            
            if(!(domVO.getPortal().equals("")))
                sql += "DNN_POR='" + domVO.getPortal() + "' AND ";
            else sql += "DNN_POR IS NULL AND ";
                        
            if(!(domVO.getEscalera().equals("")))
                sql += "DNN_ESC='" + domVO.getEscalera() + "' AND ";
            else sql += "DNN_ESC IS NULL AND ";
            
            if (!(domVO.getPlanta().equals(""))) 
                sql += "DNN_PLT='" + domVO.getPlanta() + "' AND ";
            else sql += "DNN_PLT IS NULL AND ";
                        
            if(!( domVO.getPuerta().equals("")))
                sql += "DNN_PTA='" + domVO.getPuerta() + "' AND ";
            else sql += "DNN_PTA IS NULL AND ";
            
            if(!(domVO.getDomicilio().equals("")))
                sql += "DNN_DMC='"+ domVO.getDomicilio() + "' AND ";
            else sql += "DNN_DMC IS NULL AND";
            
            if(!(domVO.getCodigoPostal().equals("")))
                sql += "DNN_DMC='" + domVO.getCodigoPostal() + "' AND ";
            else sql += "DNN_DMC IS NULL AND ";
                        
            if(!(domVO.getBarriada().equals("")))
                sql += "DNN_LUG='" + domVO.getBarriada() + "' AND ";
            else sql += "DNN_LUG IS NULL AND  ";            
            
            sql += " DNN_SIT='A'";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs=state.executeQuery(sql);
            boolean seguir = rs.next();

            if(seguir) resultado = rs.getInt("DNN_DOM");

        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new TechnicalException("Error al ejecutar el metodo existeDomicilio");
            
        }finally{     
            
            try{
                if(state!=null) state.close();
                if(rs!=null) rs.close();
                
            }catch(SQLException e){
                m_Log.error("Error al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }
        }
        return resultado;
    }
   
  
  
  
  
  

     /**
     * Inserta un domicilio nuevo
     * @param dir
     * @param con
     * @return El codigo del nuevo domicilio
     * @throws java.sql.SQLException
     */
    public int altaDomicilio(DomicilioSimpleValueObject domicilio, String usuario,String[] params)throws Exception {

        String sql;
        int codigoDomicilio = 1;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        AdaptadorSQLBD abd = null;
        Connection conexion = null; 
        int tipoVia = -1;
        
        if (m_Log.isDebugEnabled()) m_Log.debug("DomicilioDAO.altaDomicilio");

        try {
            
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            
            int codPais = Integer.valueOf(domicilio.getIdPais());
            int codProv = Integer.valueOf(domicilio.getIdProvincia());
            int codMun = Integer.valueOf(domicilio.getIdMunicipio());            
            ViasDAO viasDAO = new ViasDAO();    
            if (domicilio.getTipoVia()!=null && domicilio.getTipoVia().length()>0){
                tipoVia = Integer.valueOf(domicilio.getTipoVia()); 
            }    
            
            int codigoVia = -1;
            //Busca si existe la via
            if (domicilio.getDescVia() != null && domicilio.getDescVia().length()>0){
                codigoVia = viasDAO.buscarVia(codPais, codProv, codMun, tipoVia, domicilio.getDescVia(), conexion);
            }
            //Sino existe la via la da de alta con tipo de via (SIN VIA)
            if (codigoVia == -1 && domicilio.getDescVia() != null && domicilio.getDescVia().length()>0) {
                codigoVia = viasDAO.altaVia(codPais, codProv, codMun, ConstantesDatos.TIPO_VIA_SINVIA, domicilio.getDescVia(),usuario, conexion);
                if (m_Log.isDebugEnabled()) m_Log.debug("No existe la via. Creada nueva via con codigo " + codigoVia);
            } else {
                if (m_Log.isDebugEnabled()) m_Log.debug("Existe la via con codigo " + codigoVia);
            }

            // Insertamos el codigo postal (no se inserta si ya existe)
            CodPostalesDAO codPostalesDAO = new CodPostalesDAO();
            if (domicilio.getCodigoPostal()!=null && domicilio.getCodigoPostal().length()>0){
                codPostalesDAO.altaCodPostal(codPais, codProv, codMun, domicilio.getCodigoPostal(), conexion);
            }
            
            
            
            
            
            
            
            // Codigo para el nuevo domicilio
            sql = "SELECT MAX(DOM_COD) FROM T_DOM";
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = conexion.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                codigoDomicilio = rs.getInt(1);
                codigoDomicilio++;
            }
            rs.close();
            if (m_Log.isDebugEnabled()) m_Log.debug("Codigo para el nuevo domicilio: " + codigoDomicilio);

            // Insercion en T_DOM
            sql = "INSERT INTO T_DOM (DOM_COD, DOM_NML) VALUES (" + codigoDomicilio + ", 2)";
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            st.executeUpdate(sql);
            st.close();

            // Insercion en T_DNN
            int usuarioAlta = Integer.valueOf(usuario);
            sql = 
                "INSERT INTO T_DNN(DNN_DOM, DNN_TVI, DNN_PAI, DNN_PRV, DNN_MUN, DNN_VPA, " +
                                  "DNN_VPR, DNN_VMU, DNN_VIA, DNN_NUD, DNN_LED, DNN_NUH, " +  
                                  "DNN_LEH, DNN_BLQ, DNN_POR, DNN_ESC, DNN_PLT ,DNN_PTA, " + 
                                  "DNN_DMC, DNN_CPO, DNN_SIT, DNN_FAL, DNN_UAL)" +
                "VALUES (" + codigoDomicilio + ",?," +
                    codPais + ", " + codProv + ", " + codMun + ", " +
                    codPais + ", " + codProv + ", " + codMun + ",? " +
                    ", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'A', ?, " +
                    usuarioAlta + ")";

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = conexion.prepareStatement(sql);

            int i = 1;
            //DNN_TVI
            if (noNulo(domicilio.getTipoVia())) {ps.setInt(i++, Integer.valueOf(domicilio.getTipoVia()));}
            else {ps.setNull(i++, Types.INTEGER);}

            //DNN_VIA
           if (codigoVia!= -1) {ps.setInt(i++, codigoVia);}
            else {ps.setNull(i++, Types.INTEGER);}

            // DNN_NUD
            if (noNulo(domicilio.getNumDesde())) {ps.setInt(i++, Integer.valueOf(domicilio.getNumDesde()));}
            else {ps.setNull(i++, Types.INTEGER);}
            // DNN_LED
            if (noNulo(domicilio.getLetraDesde())) {ps.setString(i++, domicilio.getLetraDesde());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_NUH
            if (noNulo(domicilio.getNumHasta())) {ps.setInt(i++, Integer.valueOf(domicilio.getNumHasta()));}
            else {ps.setNull(i++, Types.INTEGER);}
            // DNN_LEH
            if (noNulo(domicilio.getLetraHasta())) {ps.setString(i++, domicilio.getLetraHasta());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_BLQ
            if (noNulo(domicilio.getBloque())) {ps.setString(i++, domicilio.getBloque());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_POR
            if (noNulo(domicilio.getPortal())) {ps.setString(i++, domicilio.getPortal());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_ESC
            if (noNulo(domicilio.getEscalera())) {ps.setString(i++, domicilio.getEscalera());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_PLT
            if (noNulo(domicilio.getPlanta())) {ps.setString(i++, domicilio.getPlanta());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_PTA
            if (noNulo(domicilio.getPuerta())) {ps.setString(i++, domicilio.getPuerta());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_DMC
            if (noNulo(domicilio.getSituacion())) {ps.setString(i++, domicilio.getSituacion());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_CPO
            if (noNulo(domicilio.getCodigoPostal())) {ps.setString(i++, domicilio.getCodigoPostal());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_FAL
            Timestamp ahora = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(i++, ahora);
            
            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            rollBackTransaction(abd,conexion,e);
            throw e;
        } finally {
            commitTransaction(abd,conexion);
            if (st != null) st.close();
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
        m_Log.debug("codigoDomicilio:"+codigoDomicilio);
        return codigoDomicilio;
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

      /**
     * Comprueba si un string no es nulo o vacio.
     * @param str
     * @return true si el string no es nulo o vacio, false en otro caso
     */
    private boolean noNulo(String str) {
        if (str != null && !"".equals(str)) return true;
        else return false;
    }
 
    
    /********************* NUEVO *****************************/
    
      /**
     * Busca el domicilio cuyos datos se indican en el VO
     * @param dir
     * @param con
     * @return El codigo de domicilio si se encuentra, -1 en otro caso
     * @throws java.sql.SQLException
     */
    public int buscarDireccion(DomicilioSimpleValueObject dir, Connection con)
            throws SQLException {

        String sql;
        int codigoDomicilio = -1;
        PreparedStatement ps = null;
        ResultSet rs = null;
        if (m_Log.isDebugEnabled()) m_Log.debug("->DireccionDAO.buscarDireccion");

        try {
            // Comprobar si existe la via en T_VIA           
            int codigoVia = -1;
            
            if (dir.getDescVia()!=null && !"".equals(dir.getDescVia())){
                codigoVia = this.buscarVia(Integer.parseInt(dir.getIdPais()), Integer.parseInt(dir.getIdProvincia()),
                        Integer.parseInt(dir.getIdMunicipio()), dir.getDescVia(), con);
            }
             
            if (codigoVia == -1) {
                if (m_Log.isDebugEnabled()) m_Log.debug("No existe la via");
                return -1;
            } else {
                if (m_Log.isDebugEnabled()) m_Log.debug("Existe la via con codigo " + codigoVia);
            }

            // Buscar domicilio
            sql = "SELECT DNN_DOM " +
                  "FROM T_DNN " +
                  "WHERE DNN_PAI = " + dir.getIdPais() +
                  " AND DNN_PRV = " + dir.getIdProvincia() +
                  " AND DNN_MUN = " + dir.getIdMunicipio();

           
             m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() codigoVia: " + codigoVia);
             m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getDomicilio(): " + dir.getDomicilio());
             m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getNumDesde(): " + dir.getNumDesde());
             
             if (codigoVia!= -1 && noNulo(dir.getDomicilio())){
                sql += " AND (DNN_VIA = " +codigoVia + " OR DNN_DMC='" + dir.getDomicilio() + "') ";
             }
             else
             if (codigoVia== -1 && !noNulo(dir.getDomicilio())){
                 sql += " AND DNN_DMC='" + dir.getDomicilio() + "'";                 
             }    
             else
             if (codigoVia!= -1 && !noNulo(dir.getDomicilio())){
                 sql += " AND DNN_VIA=" + codigoVia;
             }        
                 
             
             
                 
              

              /**
            if (dir.getIdTipoVia()!=null && !"".equals(dir.getIdTipoVia()))
                sql += " AND DNN_TVI = " + dir.getIdTipoVia();
            else
                sql += " AND DNN_TVI IS NULL";
                */

            if (dir.getNumDesde()!=null && !"".equals(dir.getNumDesde()) && Integer.parseInt(dir.getNumDesde())!=-1 && Integer.parseInt(dir.getNumDesde())!=0)
                sql += " AND DNN_NUD = " + dir.getNumDesde();
            else
                sql += " AND DNN_NUD IS NULL";

            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getNumHasta(): " + dir.getNumHasta());
            if (dir.getNumHasta()!=null && !"".equals(dir.getNumHasta()) && Integer.parseInt(dir.getNumHasta())!= -1 && Integer.parseInt(dir.getNumHasta())!= 0)
                sql += " AND DNN_NUH = " + dir.getNumHasta();
            else
                sql += " AND DNN_NUH IS NULL";

            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getLetraDesde(): " + dir.getLetraDesde());
            if (noNulo(dir.getLetraDesde()))
                sql += " AND DNN_LED = ?";
            else
                sql += " AND DNN_LED IS NULL";

            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getLetraHasta(): " + dir.getLetraHasta());
            if (noNulo(dir.getLetraHasta()))
                sql += " AND DNN_LEH = ?";
            else
                sql += " AND DNN_LEH IS NULL";

            
            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getBloque(): " + dir.getBloque());
            if (noNulo(dir.getBloque()))
                sql += " AND DNN_BLQ = ?";
            else
                sql += " AND DNN_BLQ IS NULL";

            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getPortal(): " + dir.getPortal());
            if (noNulo(dir.getPortal()))
                sql += " AND DNN_POR = ?";
            else
                sql += " AND DNN_POR IS NULL";

            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getEscalera(): " + dir.getEscalera());
            if (noNulo(dir.getEscalera()))
                sql += " AND DNN_ESC = ?";
            else
                sql += " AND DNN_ESC IS NULL";

            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getPlanta(): " + dir.getPlanta());
            if (noNulo(dir.getPlanta()))
                sql += " AND DNN_PLT = ?";
            else
                sql += " AND DNN_PLT IS NULL";

            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getPuerta(): " + dir.getPuerta());
            if (noNulo(dir.getPuerta()))
                sql += " AND DNN_PTA = ?";
            else
                sql += " AND DNN_PTA IS NULL";

            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getCodigoPostal(): " + dir.getCodigoPostal());
            if(noNulo(dir.getCodigoPostal())){
                sql += " AND DNN_CPO = ?";                
            }else
                sql += " AND DNN_CPO IS NULL";                
            
            sql += " AND DNN_SIT = 'A'";
            
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = con.prepareStatement(sql);

            int i = 1;
            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getLetraDesde(): " + dir.getLetraDesde());
            if (noNulo(dir.getLetraDesde())) ps.setString(i++, dir.getLetraDesde());
            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getLetraHasta(): " + dir.getLetraHasta());
            if (noNulo(dir.getLetraHasta())) ps.setString(i++, dir.getLetraHasta());
            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getBloque(): " + dir.getBloque());
            if (noNulo(dir.getBloque())) ps.setString(i++, dir.getBloque());
            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getPortal(): " + dir.getPortal());
            if (noNulo(dir.getPortal())) ps.setString(i++, dir.getPortal());
            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getEscalera(): " + dir.getEscalera());
            if (noNulo(dir.getEscalera())) ps.setString(i++, dir.getEscalera());
            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getPlanta(): " + dir.getPlanta());
            if (noNulo(dir.getPlanta())) ps.setString(i++, dir.getPlanta());
            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getPuerta(): " + dir.getPuerta());
            if (noNulo(dir.getPuerta())) ps.setString(i++, dir.getPuerta());
            m_Log.debug("*****************> DomiciliosDAO.buscarDireccion() dir.getCodigoPostal(): " + dir.getCodigoPostal());
            if (noNulo(dir.getCodigoPostal())) ps.setString(i++, dir.getCodigoPostal());

            rs = ps.executeQuery();

            // Obtener resultado
            if (rs.next()) {
                codigoDomicilio = rs.getInt("DNN_DOM");
                if (m_Log.isDebugEnabled()) m_Log.debug("Domicilio encontrado: " + codigoDomicilio);
            } else {
                if (m_Log.isDebugEnabled()) m_Log.debug("Domicilio no encontrado");
            }
            
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
        return codigoDomicilio;
    }
    
    
    
    public int buscarVia(int codPais, int codProvincia, int codMunicipio,
            String nombreVia, Connection con) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        int codigoVia = -1;
        if (m_Log.isDebugEnabled()) m_Log.debug("->Domicilios.buscarVia");
        
        try {

          // Comprobamos si existe
          sql = "SELECT VIA_COD FROM T_VIA " +
                "WHERE (VIA_NOM LIKE ? OR VIA_NOC LIKE ?) " +
                "AND VIA_PAI = " + codPais +
                "AND VIA_PRV = " + codProvincia +
                "AND VIA_MUN = " + codMunicipio +
                "AND VIA_SIT = 'A'";

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
     * Inserta un domicilio nuevo
     * @param dir: DomicilioSimpleValueObject
     * @param con: Conexión a la BBDD
     * @return El codigo del nuevo domicilio
     * @throws java.sql.SQLException
     */
    public int altaDomicilio(DomicilioSimpleValueObject domicilio, String usuario,Connection con)throws Exception {
        String sql;
        int codigoDomicilio = 1;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        int tipoVia = -1;
        
        if (m_Log.isDebugEnabled()) m_Log.debug("DomicilioDAO.altaDomicilio");

        try {
            
            int codPais = Integer.valueOf(domicilio.getIdPais());
            int codProv = Integer.valueOf(domicilio.getIdProvincia());
            int codMun = Integer.valueOf(domicilio.getIdMunicipio());            
            ViasDAO viasDAO = new ViasDAO();    
            if (domicilio.getTipoVia()!=null && domicilio.getTipoVia().length()>0){
                tipoVia = Integer.valueOf(domicilio.getTipoVia()); 
            }    
            
            int codigoVia = -1;
            //Busca si existe la via
            if (domicilio.getDescVia() != null && domicilio.getDescVia().length()>0){
                codigoVia = this.buscarVia(codPais, codProv, codMun, domicilio.getDescVia(), con);                
            }
            
            //Sino existe la via la da de alta con tipo de via (SIN VIA)
            if (codigoVia == -1 && domicilio.getDescVia() != null && domicilio.getDescVia().length()>0) {
                codigoVia = viasDAO.altaVia(codPais, codProv, codMun, ConstantesDatos.TIPO_VIA_SINVIA, domicilio.getDescVia(),usuario, con);
                if (m_Log.isDebugEnabled()) m_Log.debug("No existe la via. Creada nueva via con codigo " + codigoVia);
            } else {
                if (m_Log.isDebugEnabled()) m_Log.debug("Existe la via con codigo " + codigoVia);
            }

            // Insertamos el codigo postal (no se inserta si ya existe)
            CodPostalesDAO codPostalesDAO = new CodPostalesDAO();
            if (domicilio.getCodigoPostal()!=null && domicilio.getCodigoPostal().length()>0){
                codPostalesDAO.altaCodPostal(codPais, codProv, codMun, domicilio.getCodigoPostal(), con);
            }
                        
            
            // Codigo para el nuevo domicilio
            sql = "SELECT MAX(DOM_COD) FROM T_DOM";
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                codigoDomicilio = rs.getInt(1);
                codigoDomicilio++;
            }
            rs.close();
            if (m_Log.isDebugEnabled()) m_Log.debug("Codigo para el nuevo domicilio: " + codigoDomicilio);

            // Insercion en T_DOM
            sql = "INSERT INTO T_DOM (DOM_COD, DOM_NML) VALUES (" + codigoDomicilio + ", 2)";
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            st.executeUpdate(sql);
            st.close();

            // Insercion en T_DNN
            int usuarioAlta = Integer.valueOf(usuario);
            sql = 
                "INSERT INTO T_DNN(DNN_DOM, DNN_TVI, DNN_PAI, DNN_PRV, DNN_MUN, DNN_VPA, " +
                                  "DNN_VPR, DNN_VMU, DNN_VIA, DNN_NUD, DNN_LED, DNN_NUH, " +  
                                  "DNN_LEH, DNN_BLQ, DNN_POR, DNN_ESC, DNN_PLT ,DNN_PTA, " + 
                                  "DNN_DMC, DNN_CPO, DNN_SIT, DNN_FAL, DNN_UAL)" +
                "VALUES (" + codigoDomicilio + ",?," +
                    codPais + ", " + codProv + ", " + codMun + ", " +
                    codPais + ", " + codProv + ", " + codMun + ",? " +
                    ", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'A', ?, " +
                    usuarioAlta + ")";

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = con.prepareStatement(sql);

            int i = 1;
            
            /**
            //DNN_TVI
            if (noNulo(domicilio.getTipoVia())) {ps.setInt(i++, Integer.valueOf(domicilio.getTipoVia()));}
            else {ps.setNull(i++, Types.INTEGER);}

            //DNN_VIA
           if (codigoVia!= -1) {ps.setInt(i++, codigoVia);}
            else {ps.setNull(i++, Types.INTEGER);}

            // DNN_NUD
            if (noNulo(domicilio.getNumDesde())) {ps.setInt(i++, Integer.valueOf(domicilio.getNumDesde()));}
            else {ps.setNull(i++, Types.INTEGER);}
            // DNN_LED
            if (noNulo(domicilio.getLetraDesde())) {ps.setString(i++, domicilio.getLetraDesde());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_NUH
            if (noNulo(domicilio.getNumHasta())) {ps.setInt(i++, Integer.valueOf(domicilio.getNumHasta()));}
            else {ps.setNull(i++, Types.INTEGER);}
            // DNN_LEH
            if (noNulo(domicilio.getLetraHasta())) {ps.setString(i++, domicilio.getLetraHasta());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_BLQ
            if (noNulo(domicilio.getBloque())) {ps.setString(i++, domicilio.getBloque());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_POR
            if (noNulo(domicilio.getPortal())) {ps.setString(i++, domicilio.getPortal());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_ESC
            if (noNulo(domicilio.getEscalera())) {ps.setString(i++, domicilio.getEscalera());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_PLT
            if (noNulo(domicilio.getPlanta())) {ps.setString(i++, domicilio.getPlanta());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_PTA
            if (noNulo(domicilio.getPuerta())) {ps.setString(i++, domicilio.getPuerta());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_DMC
            if (noNulo(domicilio.getSituacion())) {ps.setString(i++, domicilio.getSituacion());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_CPO
            if (noNulo(domicilio.getCodigoPostal())) {ps.setString(i++, domicilio.getCodigoPostal());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_FAL
            Timestamp ahora = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(i++, ahora);
            */
            
            //DNN_TVI
            if (noNulo(domicilio.getTipoVia())) {ps.setInt(i++, Integer.valueOf(domicilio.getTipoVia()));}
            else {ps.setNull(i++, Types.INTEGER);}

            //DNN_VIA
           if (codigoVia!= -1) {ps.setInt(i++, codigoVia);}
            else {ps.setNull(i++, Types.INTEGER);}

            // DNN_NUD
            if (noNulo(domicilio.getNumDesde())) {ps.setInt(i++, Integer.valueOf(domicilio.getNumDesde()));}
            else {ps.setNull(i++, Types.INTEGER);}
            // DNN_LED
            if (noNulo(domicilio.getLetraDesde())) {ps.setString(i++, domicilio.getLetraDesde());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_NUH
            if (noNulo(domicilio.getNumHasta())) {ps.setInt(i++, Integer.valueOf(domicilio.getNumHasta()));}
            else {ps.setNull(i++, Types.INTEGER);}
            // DNN_LEH
            if (noNulo(domicilio.getLetraHasta())) {ps.setString(i++, domicilio.getLetraHasta());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_BLQ
            if (noNulo(domicilio.getBloque())) {ps.setString(i++, domicilio.getBloque());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_POR
            if (noNulo(domicilio.getPortal())) {ps.setString(i++, domicilio.getPortal());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_ESC
            if (noNulo(domicilio.getEscalera())) {ps.setString(i++, domicilio.getEscalera());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_PLT
            if (noNulo(domicilio.getPlanta())) {ps.setString(i++, domicilio.getPlanta());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_PTA
            if (noNulo(domicilio.getPuerta())) {ps.setString(i++, domicilio.getPuerta());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_DMC
            if (noNulo(domicilio.getSituacion())) {ps.setString(i++, domicilio.getSituacion());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_CPO
            if (noNulo(domicilio.getCodigoPostal())) {ps.setString(i++, domicilio.getCodigoPostal());}
            else {ps.setNull(i++, Types.VARCHAR);}
            // DNN_FAL
            Timestamp ahora = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(i++, ahora);
            
            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {            
            throw e;
        } finally {
            
            if (st != null) st.close();
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
        m_Log.debug("codigoDomicilio:"+codigoDomicilio);
        return codigoDomicilio;
    }
    
    
}