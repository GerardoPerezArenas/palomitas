// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.mantenimiento.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.terceros.mantenimiento.MunicipioVO;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Vector;
import java.util.HashMap;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: MunicipiosDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */

public class MunicipiosDAO  {
  private static MunicipiosDAO instance = null;
  private static HashMap municipiosDescripciones = null;
  protected static Config campos; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(MunicipiosDAO.class.getName());


  protected MunicipiosDAO() {
    super();
    // Queremos usar el fichero de configuracion techserver
    campos = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static MunicipiosDAO getInstance() {
    // si no hay ninguna instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread)
      // Las invocaciones de este metodo
      synchronized (MunicipiosDAO.class) {
        if (instance == null) {
          instance = new MunicipiosDAO();
        }
      }
    }
    return instance;
  }

  public Vector eliminarMunicipio(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    AdaptadorSQLBD oad = null;
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    int cont = 0;
    int cont1 = 0;
    int cont2 = 0;
    int cont3 = 0;
    int cont4 = 0;
    int cont5 = 0;
    Vector resultado = new Vector();
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      stmt = conexion.createStatement();
      
      sql = "SELECT * FROM T_DNN WHERE " + 
            campos.getString("SQL.T_DNN.idPaisD")+"="+gVO.getAtributo("codPais")+" AND "+
		        campos.getString("SQL.T_DNN.idProvinciaD")+"="+gVO.getAtributo("codProvincia")+" AND "+
		        campos.getString("SQL.T_DNN.idMunicipioD")+"="+gVO.getAtributo("codMunicipio");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont++;
      }
      rs.close();
      
      sql = "SELECT * FROM T_DSU WHERE " + 
            campos.getString("SQL.T_DSU.pais")+"="+gVO.getAtributo("codPais")+" AND "+
		        campos.getString("SQL.T_DSU.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
		        campos.getString("SQL.T_DSU.municipio")+"="+gVO.getAtributo("codMunicipio");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont1++;
      }
      rs.close();
      
      sql = "SELECT * FROM T_ECO WHERE " + 
            campos.getString("SQL.T_ECO.pais")+"="+gVO.getAtributo("codPais")+" AND "+
		        campos.getString("SQL.T_ECO.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
		        campos.getString("SQL.T_ECO.municipio")+"="+gVO.getAtributo("codMunicipio");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont2++;
      }
      rs.close();
      
      sql = "SELECT * FROM T_ESI WHERE " + 
            campos.getString("SQL.T_ESI.pais")+"="+gVO.getAtributo("codPais")+" AND "+
		        campos.getString("SQL.T_ESI.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
		        campos.getString("SQL.T_ESI.municipio")+"="+gVO.getAtributo("codMunicipio");
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont3++;
      }
      rs.close();
      
      sql = "SELECT * FROM T_NUC WHERE " + 
            campos.getString("SQL.T_NUC.pais")+"="+gVO.getAtributo("codPais")+" AND "+
		        campos.getString("SQL.T_NUC.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
		        campos.getString("SQL.T_NUC.municipio")+"="+gVO.getAtributo("codMunicipio");    
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont4++;
      }
      rs.close();
      
      sql = "SELECT * FROM T_SEC WHERE " + 
            campos.getString("SQL.T_SEC.pais")+"="+gVO.getAtributo("codPais")+" AND "+
		        campos.getString("SQL.T_SEC.provincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
		        campos.getString("SQL.T_SEC.municipio")+"="+gVO.getAtributo("codMunicipio");    
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      rs = stmt.executeQuery(sql);
      while(rs.next()){
      	cont5++;
      }
      rs.close();

      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      oad = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      con = oad.getConnection();
      stmt = con.createStatement();

      if(cont == 0 && cont1 == 0 && cont2 == 0 && cont3 == 0 && cont4 == 0 && cont5 == 0) {
	      sql = "DELETE FROM T_MUN WHERE " +
	        campos.getString("SQL.T_MUN.idPais")+"="+gVO.getAtributo("codPais")+" AND "+
	        campos.getString("SQL.T_MUN.idProvincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
	        campos.getString("SQL.T_MUN.idMunicipio")+"="+gVO.getAtributo("codMunicipio");
          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
	      stmt.executeUpdate(sql);
      }
      stmt.close();
    }catch (Exception e){
        e.printStackTrace();
    }finally{
        try {
            oad.devolverConexion(con);
            abd.devolverConexion(conexion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    if(cont == 0 && cont1 == 0 && cont2 == 0 && cont3 == 0 && cont4 == 0 && cont5 == 0) {
      resultado = getListaMunicipios(gVO,params);
    } else {
    	gVO.setAtributo("puedeEliminar","no");
    	resultado.addElement(gVO);
    }
    return resultado;
  }

  public Vector getListaMunicipios(GeneralValueObject parametros,String[] params){
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
      // Creamos la select con los parametros adecuados.
      sql = "SELECT T_MUN.* " +
            "FROM " + GlobalNames.ESQUEMA_GENERICO+ "T_MUN T_MUN WHERE ";

      // CONDICIONES
      sql+= campos.getString("SQL.T_MUN.idPais")+"="+parametros.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_MUN.idProvincia")+"="+parametros.getAtributo("codProvincia");

      String[] orden = {campos.getString("SQL.T_MUN.nombre"),"3"};
      sql += abd.orderUnion(orden);
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        GeneralValueObject gVO = new GeneralValueObject();
        // Municipio
        gVO.setAtributo("codPais",rs.getString(campos.getString("SQL.T_MUN.idPais")));
        gVO.setAtributo("codProvincia",rs.getString(campos.getString("SQL.T_MUN.idProvincia")));
        gVO.setAtributo("codMunicipio",rs.getString(campos.getString("SQL.T_MUN.idMunicipio")));
        gVO.setAtributo("partidoJudicial",rs.getString(campos.getString("SQL.T_MUN.partidoJudicial")));
        gVO.setAtributo("comarca",rs.getString(campos.getString("SQL.T_MUN.comarca")));
        gVO.setAtributo("nombreOficial",rs.getString(campos.getString("SQL.T_MUN.nombre")));
        gVO.setAtributo("nombreLargo",rs.getString(campos.getString("SQL.T_MUN.nombreLargo")));
        gVO.setAtributo("digitoControl",rs.getString(campos.getString("SQL.T_MUN.digitoControl")));
        gVO.setAtributo("superficie",rs.getString(campos.getString("SQL.T_MUN.superficie")));
        gVO.setAtributo("altitud",rs.getString(campos.getString("SQL.T_MUN.altitud")));
        gVO.setAtributo("kmtsACapital",rs.getString(campos.getString("SQL.T_MUN.kmtsCapital")));
        gVO.setAtributo("latitudNorte",rs.getString(campos.getString("SQL.T_MUN.latitudNorte")));
        gVO.setAtributo("latitudSur",rs.getString(campos.getString("SQL.T_MUN.latitudSur")));
        gVO.setAtributo("longitudEste",rs.getString(campos.getString("SQL.T_MUN.longitudEste")));
        gVO.setAtributo("longitudOeste",rs.getString(campos.getString("SQL.T_MUN.longitudOeste")));
        gVO.setAtributo("situacion",rs.getString(campos.getString("SQL.T_MUN.situacion")));
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
            if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en MunicipiosDAO.getListaMunicipios");
        }
    }
    return resultado;
  }

  public Vector modificarMunicipio(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    String sql = "";
    Vector resultado = new Vector();
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      sql = "UPDATE T_MUN SET " +
        campos.getString("SQL.T_MUN.idPais")+"="+gVO.getAtributo("codPais")+","+
        campos.getString("SQL.T_MUN.idProvincia")+"="+gVO.getAtributo("codProvincia")+","+
        campos.getString("SQL.T_MUN.idMunicipio")+"="+gVO.getAtributo("codMunicipio")+","+
        campos.getString("SQL.T_MUN.partidoJudicial")+"="+gVO.getAtributo("partidoJudicial")+","+
        campos.getString("SQL.T_MUN.comarca")+"="+gVO.getAtributo("comarca")+","+
        campos.getString("SQL.T_MUN.nombre")+"='"+gVO.getAtributo("nombreOficial")+"',"+
        campos.getString("SQL.T_MUN.nombreLargo")+"='"+gVO.getAtributo("nombreLargo")+"',"+
        campos.getString("SQL.T_MUN.digitoControl")+"='"+gVO.getAtributo("digitoControl")+"',"+
        campos.getString("SQL.T_MUN.superficie")+"="+gVO.getAtributo("superficie")+","+
        campos.getString("SQL.T_MUN.altitud")+"="+gVO.getAtributo("altitud")+","+
        campos.getString("SQL.T_MUN.kmtsCapital")+"="+gVO.getAtributo("kmtsACapital")+","+
        campos.getString("SQL.T_MUN.latitudNorte")+"="+gVO.getAtributo("latitudNorte")+","+
        campos.getString("SQL.T_MUN.latitudSur")+"="+gVO.getAtributo("latitudSur")+","+
        campos.getString("SQL.T_MUN.longitudEste")+"="+gVO.getAtributo("longitudEste")+","+
        campos.getString("SQL.T_MUN.longitudOeste")+"="+gVO.getAtributo("longitudOeste")+","+
        campos.getString("SQL.T_MUN.situacion")+"="+gVO.getAtributo("situacion")+
        " WHERE " +
        campos.getString("SQL.T_MUN.idPais")+"="+gVO.getAtributo("codPais")+" AND "+
        campos.getString("SQL.T_MUN.idProvincia")+"="+gVO.getAtributo("codProvincia")+" AND "+
        campos.getString("SQL.T_MUN.idMunicipio")+"="+gVO.getAtributo("codMunicipio");
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
    resultado = getListaMunicipios(gVO,params);
    return resultado;
  }

  public Vector altaMunicipio(GeneralValueObject gVO, String[] params){
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    Vector resultado = new Vector();
    try{
      //m_Log.debug("A por el OAD");
      String[] parametros = (String []) params.clone();
      parametros[6] = campos.getString("CON.jndi");
      abd = new AdaptadorSQLBD(parametros);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      abd.inicioTransaccion(conexion);
      sql = "INSERT INTO T_MUN("+
        campos.getString("SQL.T_MUN.idPais")+","+
        campos.getString("SQL.T_MUN.idProvincia")+","+
        campos.getString("SQL.T_MUN.idMunicipio")+","+
        campos.getString("SQL.T_MUN.partidoJudicial")+","+
        campos.getString("SQL.T_MUN.comarca")+","+
        campos.getString("SQL.T_MUN.nombre")+","+
        campos.getString("SQL.T_MUN.nombreLargo")+","+
        campos.getString("SQL.T_MUN.digitoControl")+","+
        campos.getString("SQL.T_MUN.superficie")+","+
        campos.getString("SQL.T_MUN.altitud")+","+
        campos.getString("SQL.T_MUN.kmtsCapital")+","+
        campos.getString("SQL.T_MUN.latitudNorte")+","+
        campos.getString("SQL.T_MUN.latitudSur")+","+
        campos.getString("SQL.T_MUN.longitudEste")+","+
        campos.getString("SQL.T_MUN.longitudOeste")+","+
        campos.getString("SQL.T_MUN.situacion")+
        ") VALUES (" +
        gVO.getAtributo("codPais")+","+
        gVO.getAtributo("codProvincia")+","+
        gVO.getAtributo("codMunicipio")+","+
        gVO.getAtributo("partidoJudicial")+","+
        gVO.getAtributo("comarca")+",'"+
        gVO.getAtributo("nombreOficial")+"','"+
        gVO.getAtributo("nombreLargo")+"','"+
        gVO.getAtributo("digitoControl")+"',"+
        gVO.getAtributo("superficie")+","+
        gVO.getAtributo("altitud")+","+
        gVO.getAtributo("kmtsACapital")+","+
        gVO.getAtributo("latitudNorte")+","+
        gVO.getAtributo("latitudSur")+","+
        gVO.getAtributo("longitudEste")+","+
        gVO.getAtributo("longitudOeste")+","+
        gVO.getAtributo("situacion")+")";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      stmt.executeUpdate(sql);
      //m_Log.debug("las filas afectadas en el insert son : " + res);
      stmt.close();
    }catch (Exception e){
      rollBackTransaction(abd,conexion,e);
    }finally{
      commitTransaction(abd,conexion);
    }
    resultado = getListaMunicipios(gVO,params);
    return resultado;
   }

   public HashMap getDescripcionesMunicipios(String[] params){

       if (municipiosDescripciones==null){
            AdaptadorSQLBD abd = null;
            Connection conexion = null;
            Statement stmt = null;
            ResultSet rs = null;
            String sql = "";
            HashMap municipios = new HashMap();
            try{
                abd = new AdaptadorSQLBD(params);
                conexion = abd.getConnection();
                stmt = conexion.createStatement();
                sql = "SELECT *" +
                      " FROM "+GlobalNames.ESQUEMA_GENERICO +"T_MUN";

                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs = stmt.executeQuery(sql);
                while(rs.next()){
                    municipios.put(rs.getString(campos.getString("SQL.T_MUN.idPais")) +rs.getString(campos.getString("SQL.T_MUN.idProvincia"))+rs.getString(campos.getString("SQL.T_MUN.idMunicipio")),
                                   rs.getString(campos.getString("SQL.T_MUN.nombre")));
                }

            }catch (Exception e){
                    e.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }finally{
                try {
                    abd.devolverConexion(conexion);
                }catch (BDException e) {
                    e.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("Imposible devolver la conexion en ProvinciasDAO.getListaProvincias");
                }
                municipiosDescripciones = municipios;
            }
        }

        return municipiosDescripciones;
  }

    public MunicipioVO getMunicipioByPaisAndProvAndDesc(int codPais, int codProvincia, String descMunicipio,
                                                               String params[]) throws BDException, SQLException {

        String sqlQuery = "SELECT MUN_COD, MUN_PJU, MUN_COM, MUN_NOM, MUN_NOL, MUN_DCO, MUN_SUP, MUN_ALT, MUN_KMC, " +
                "MUN_LTN, MUN_LTS, MUN_LES, MUN_LOE, MUN_SIT FROM " + GlobalNames.ESQUEMA_GENERICO + "T_MUN " +
                "WHERE MUN_PAI = ? AND MUN_PRV = ? AND (MUN_NOM = ? OR MUN_NOL = ?)";

        AdaptadorSQLBD dbAdapter = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbAdapter.getConnection();
            ps = con.prepareStatement(sqlQuery);
            m_Log.debug(sqlQuery);
            m_Log.debug("PARAMETRO 1 --> CODIGO DE PAIS: " + codPais);
            m_Log.debug("PARAMETRO 2 --> CODIGO DE PROVINCIA: " + codProvincia);
            m_Log.debug("PARAMETRO 2 --> NOMBRE DEL MUNICIPIO: " + descMunicipio);
            ps.setInt(1, codPais);
            ps.setInt(2, codProvincia);
            ps.setString(3, descMunicipio);
            ps.setString(4, descMunicipio);

            rs = ps.executeQuery();
            if (rs.next()) {
                int i = 1;
                MunicipioVO municipioRecuperado = new MunicipioVO();
                municipioRecuperado.setCodigoPais(codPais);
                municipioRecuperado.setCodigoProvincia(codProvincia);
                municipioRecuperado.setCodigoMunicipio(rs.getInt(i++));
                municipioRecuperado.setPartidoJudicial(rs.getInt(i++));
                municipioRecuperado.setComarca(rs.getInt(i++));
                municipioRecuperado.setNombreOficial(rs.getString(i++));
                municipioRecuperado.setNombreLargo(rs.getString(i++));
                municipioRecuperado.setDigitoControl(rs.getString(i++));
                municipioRecuperado.setSuperficie(rs.getFloat(i++));
                municipioRecuperado.setAltitud(rs.getInt(i++));
                municipioRecuperado.setKmsCapital(rs.getInt(i++));
                municipioRecuperado.setLatitudNorte(rs.getFloat(i++));
                municipioRecuperado.setLatitudSur(rs.getFloat(i++));
                municipioRecuperado.setLongitudEste(rs.getFloat(i++));
                municipioRecuperado.setLongitudOeste(rs.getFloat(i++));
                municipioRecuperado.setSituacion(rs.getString(i));

                return municipioRecuperado;
            }
            else throw new SQLException("NO SE HA ENCONTRADO NINGUN MUNICIPIO PARA ESE PAIS Y PROVINCIA CON ESA DESCRIPCION");

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            dbAdapter.devolverConexion(con);
        }
    }

    public MunicipioVO getMunicipioByPaisAndProvAndCodigo(int codPais, int codProvincia, int codMunicipio, String params[])
    throws BDException, SQLException {

        String sqlQuery = "SELECT MUN_PJU, MUN_COM, MUN_NOM, MUN_NOL, MUN_DCO, MUN_SUP, MUN_ALT, MUN_KMC, " +
                "MUN_LTN, MUN_LTS, MUN_LES, MUN_LOE, MUN_SIT FROM " + GlobalNames.ESQUEMA_GENERICO + "T_MUN " +
                "WHERE MUN_PAI = ? AND MUN_PRV = ? AND MUN_COD = ?";

        AdaptadorSQLBD dbAdapter = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dbAdapter.getConnection();
            ps = con.prepareStatement(sqlQuery);
            m_Log.debug(sqlQuery);
            m_Log.debug("PARAMETRO 1 --> CODIGO DE PAIS: " + codPais);
            m_Log.debug("PARAMETRO 2 --> CODIGO DE PROVINCIA: " + codProvincia);
            m_Log.debug("PARAMETRO 2 --> CODIGO DEL MUNICIPIO: " + codMunicipio);
            ps.setInt(1, codPais);
            ps.setInt(2, codProvincia);
            ps.setInt(3, codMunicipio);

            rs = ps.executeQuery();
            if (rs.next()) {
                int i = 1;
                MunicipioVO municipioRecuperado = new MunicipioVO();
                municipioRecuperado.setCodigoPais(codPais);
                municipioRecuperado.setCodigoProvincia(codProvincia);
                municipioRecuperado.setCodigoMunicipio(codMunicipio);
                municipioRecuperado.setPartidoJudicial(rs.getInt(i++));
                municipioRecuperado.setComarca(rs.getInt(i++));
                municipioRecuperado.setNombreOficial(rs.getString(i++));
                municipioRecuperado.setNombreLargo(rs.getString(i++));
                municipioRecuperado.setDigitoControl(rs.getString(i++));
                municipioRecuperado.setSuperficie(rs.getFloat(i++));
                municipioRecuperado.setAltitud(rs.getInt(i++));
                municipioRecuperado.setKmsCapital(rs.getInt(i++));
                municipioRecuperado.setLatitudNorte(rs.getFloat(i++));
                municipioRecuperado.setLatitudSur(rs.getFloat(i++));
                municipioRecuperado.setLongitudEste(rs.getFloat(i++));
                municipioRecuperado.setLongitudOeste(rs.getFloat(i++));
                municipioRecuperado.setSituacion(rs.getString(i));

                return municipioRecuperado;
            }
            else throw new SQLException("NO SE HA ENCONTRADO NINGUN MUNICIPIO PARA ESE PAIS Y PROVINCIA CON ESA DESCRIPCION");

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            dbAdapter.devolverConexion(con);
        }
    }

     /**
     * Devuelve la descripción de un determinado municipio
     * @param codPais: Código del país
     * @param codProvincia: Código de la provincia
     * @param codMunicipio: Código del municipio
     * @param con: Conexión a la BBDD
     * @return String con el nombre o String vacío sino se ha podido recuperar    
     */
    public String getDescripcionMunicipio(int codPais, int codProvincia, int codMunicipio,Connection con){

        PreparedStatement ps = null;
        ResultSet rs = null;
        String salida = "";
        
        try {
            
            String sql = "SELECT MUN_NOM FROM " + GlobalNames.ESQUEMA_GENERICO + "T_MUN " +
                     "WHERE MUN_PAI = ? AND MUN_PRV = ? AND MUN_COD=?";
            
            ps = con.prepareStatement(sql);
            
            int i=1;
            ps.setInt(i++, codPais);
            ps.setInt(i++, codProvincia);
            ps.setInt(i++, codMunicipio);

            rs = ps.executeQuery();
            while(rs.next()) {
                salida = rs.getString("MUN_NOM");
            }
            

        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            try{
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }            
        }
        
        return salida;
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
