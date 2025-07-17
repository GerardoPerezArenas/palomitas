// NOMBRE DEL PAQUETE
package es.altia.agora.business.registro.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.Vector;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class InformesDAO {
   private static InformesDAO instance = null;
   protected static Config m_ct; // Para el fichero de configuracion técnico
   protected static Config m_ConfigError; // Para los mensajes de error localizados
   protected static Log m_Log =
           LogFactory.getLog(InformesDAO.class.getName());

   protected static GeneralValueObject columns;

   protected InformesDAO() {
  super();
  // Queremos usar el fichero de configuracion techserver
  m_ct = ConfigServiceHelper.getConfig("techserver");
  // Queremos tener acceso a los mensajes de error localizados
  m_ConfigError = ConfigServiceHelper.getConfig("error");

  columns = new GeneralValueObject();
  columns.setAtributo("",m_ct.getString("SQL.T_PAI.nombre"));
   }

   public static InformesDAO getInstance() {
  // si no hay ninguna instancia de esta clase tenemos que crear una
  if (instance == null) {
     // Necesitamos sincronizacion para serializar (no multithread)
     // Las invocaciones de este metodo
     synchronized(InformesDAO.class) {
    if (instance == null) {
       instance = new InformesDAO();
    }
     }
  }
  return instance;
   }

   public Vector getDias(String[] params,
     GeneralValueObject parametrosConsultaVO, int t)
     throws TechnicalException{
  AdaptadorSQLBD aod = new AdaptadorSQLBD(params);
  Vector v = new Vector();
  String tipo = (String)parametrosConsultaVO.getAtributo("tipo");
  String fechaInicio = (String)parametrosConsultaVO.getAtributo("fechaInicio");
  String fechaFin = (String)parametrosConsultaVO.getAtributo("fechaFin");
  String unidadOrg = (String)parametrosConsultaVO.getAtributo("unidadOrg");
  String depto = (String)parametrosConsultaVO.getAtributo("depto");
  String numDesde = (String)parametrosConsultaVO.getAtributo("numDesde");
  String departamento = (String)parametrosConsultaVO.getAtributo("departamento");
  String unidad = (String)parametrosConsultaVO.getAtributo("unidad");
        String regDesde = (String) parametrosConsultaVO.getAtributo("regInicio");
        String regHasta = (String) parametrosConsultaVO.getAtributo("regFinal");
  String sql = "";
//  String sqlIncluirFechaFin = "";
  switch(t){
     case 0:
    sql = "SELECT DISTINCT " + aod.convertir("res_fec", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
            " AS FECHAN, " + aod.convertir(aod.convertir("res_fec", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"),
            AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " AS FE" +
      "CHA FROM R_RES WHERE res_dep = " + depto + " AND res_uor = "
      + unidadOrg + " AND res_tip = '" + tipo + "' AND res_num >= "
      + numDesde + " AND ((res_fec BETWEEN " + aod.convertir("'" + fechaInicio + "'",
            AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " AND " + aod.convertir("'" + fechaFin + "'",
            AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ") OR (" +
            aod.convertir(aod.convertir("res_fec", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"),
            AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "=" +
      aod.convertir("'" + fechaFin + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ")) ORDER BY FECHA";
/*	sqlIncluirFechaFin = "SELECT DISTINCT TO_CHAR(res_fec, 'DD/MM/YYYY') AS FECHAN, " +
		  "TO_DATE(TO_CHAR(res_fec, 'DD/MM/YYYY'),'DD/MM/YYYY') AS FE" +
		  "CHA FROM R_RES WHERE res_dep = " + depto + " AND res_uor = "
		  + unidadOrg + " AND res_tip = '" + tipo + "' AND res_num >= "
		  + numDesde + 
		  " AND (TO_DATE(TO_CHAR( res_fec,'DD/MM/YYYY'),'DD/MM/YYYY') = TO_DATE('"+ fechaFin + "','DD/MM/YYYY')) "
		  + " ORDER BY FECHA";      
*/
    break;
     case 1:
    sql = "SELECT DISTINCT " + aod.convertir("res_fec", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
            " AS FECHAN, " + aod.convertir(aod.convertir("res_fec", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY"),
            AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " AS FE" +
      "CHA FROM R_RES WHERE res_dep = " + depto + " AND res_uor = "
      + unidadOrg + " AND res_tip = '" + tipo + "' AND ((res_fec BETWEEN " + aod.convertir("'" + fechaInicio + "'",
            AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " AND " + aod.convertir("'" + fechaFin + "'",
            AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ") OR (" +
            aod.convertir(aod.convertir("res_fec", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"),
            AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "=" +
      aod.convertir("'" + fechaFin + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ")) ";
    if(!"".equals(unidad))
       sql += " AND res_uod = " + unidad;
    sql += " ORDER BY FECHA";
/*	sqlIncluirFechaFin = "SELECT DISTINCT TO_CHAR(res_fec, 'DD/MM/YYYY') AS FECHAN, " +
	"TO_DATE(TO_CHAR(res_fec, 'DD/MM/YYYY'),'DD/MM/YYYY') AS FE" +
	"CHA FROM R_RES WHERE res_dep = " + depto + " AND res_uor = "
	+ unidadOrg + " AND res_tip = '" + tipo + "' "
	+ " AND (TO_DATE(TO_CHAR( res_fec,'DD/MM/YYYY'),'DD/MM/YYYY') = TO_DATE('"+ fechaFin + "','DD/MM/YYYY')) ";
  	if(!"".equals(unidad))
  		sqlIncluirFechaFin  += " AND res_uod = " + unidad;
  	sqlIncluirFechaFin  += " ORDER BY FECHA";*/
    break;

            case 2:
                try {
                    sql = "SELECT DISTINCT " + aod.convertir("res_fec", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHAN, " +
                           aod.convertir(aod.convertir("res_fec", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), 
                                         AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " AS FECHA " +
                            "FROM R_RES " +
                            "WHERE " +
                            " res_uor = " + unidadOrg +
                            " AND res_tip = '" + tipo + "' " +
                            " AND (res_num BETWEEN " + regDesde + " AND " + regHasta + ")" +
                            " AND " + fechaInicio.substring(6) + " = " + "res_eje ";
                    sql += " ORDER BY FECHA";
                }
                catch (Exception bde) {
                    bde.printStackTrace();
                    m_Log.error("Error del AdaptadorSQL en el método getDias de InformesDAO");
                }

                break;

  }
  if(m_Log.isDebugEnabled()) m_Log.debug(sql);
//  m_Log.debug(sqlIncluirFechaFin);
  Connection con = null;
  Statement stmt = null;
  ResultSet rs = null;
  try{
     con = aod.getConnection();
     stmt = con.createStatement();
     rs = stmt.executeQuery(sql);
     while(rs.next())
    	v.add(rs.getString("FECHAN"));
     rs.close();
     stmt.close();
/*	rs = stmt.executeQuery(sqlIncluirFechaFin);
	while(rs.next())
		v.add(rs.getString("FECHAN"));
	rs.close();*/
  }
  catch(BDException bde){
     bde.printStackTrace();
     m_Log.error("Error del AdaptadorSQL en el método getDias de InformesDAO");
  }
  catch(SQLException sqle){
     sqle.printStackTrace();
     m_Log.error("Error de SQL en el método getDias de InformesDAO");
  }
  catch(Exception e){
     e.printStackTrace();
     m_Log.error("Error inesperado en el método getDias de InformesDAO");
  }
  finally{
     try{
        aod.devolverConexion(con);
     }
     catch(Exception e){
    e.printStackTrace();
    m_Log.error("Error en el finally del método getDias de Inf" +
           "ormesDAO");
     }
  }
  return v;
   }

	/* INFORME DE GESTION. */
	   
	public Vector getEstadisticas(String[] params, UsuarioValueObject usuario) throws TechnicalException{
  		Vector estadisticas = new Vector();
		String ano="";
		String mes="";
		String cuenta="";
		String tipoAnot="";
  		String anoViejo="";
		String mesViejo="";
  		String tipoAnotViejo="";
  		GeneralValueObject  gvo = new GeneralValueObject();
		GeneralValueObject  tavo = new GeneralValueObject();
		GeneralValueObject  mvo = new GeneralValueObject();
  		AdaptadorSQLBD aod = new AdaptadorSQLBD(params);  		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT " + aod.convertir(m_ct.getString("SQL.R_RES.fechaAnotacion"),
                AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "YYYY") + " AS ano,"
		+ aod.convertir(m_ct.getString("SQL.R_RES.fechaAnotacion"),
                AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "MM") + " AS mes, "
		+ m_ct.getString("SQL.R_RES.tipoReg")+" AS tipoAnotacion, count(*) as cuenta "
		+" FROM R_RES WHERE " + m_ct.getString("SQL.R_RES.codDpto") + "="+ usuario.getDepCod()
		+ " AND " + m_ct.getString("SQL.R_RES.codUnidad") +"=" + usuario.getUnidadOrgCod() 
		+" GROUP BY " + aod.convertir(m_ct.getString("SQL.R_RES.fechaAnotacion"),
                AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "YYYY") + ","
		+ aod.convertir(m_ct.getString("SQL.R_RES.fechaAnotacion"),
                AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "MM") + ","
		+ m_ct.getString("SQL.R_RES.tipoReg")  
		+" ORDER BY ano, tipoAnotacion, mes ";

		try{
			con = aod.getConnection();
			stmt = con.createStatement();
			if(m_Log.isDebugEnabled()) m_Log.debug("InformesDAO.getEstadísticas. " + sql);
			rs = stmt.executeQuery(sql);
			 
			while(rs.next()) {
				
				ano = rs.getString("ano");
				mes = rs.getString("mes");
				tipoAnot= rs.getString("tipoAnotacion");
				cuenta= rs.getString("cuenta");
				 
				if (ano.equals(anoViejo)) {
					if (tipoAnot.equals(tipoAnotViejo)) {
						tavo.setAtributo(mes, cuenta)		;										
					} else {
						tavo = new GeneralValueObject();
						tavo.setAtributo("tipoAnotacion", tipoAnot);
						tavo.setAtributo(mes, cuenta)	;					
						gvo.setAtributo(tipoAnot, tavo);						
						tipoAnotViejo = tipoAnot;
					}
					
				} else {				
					gvo = new GeneralValueObject();
					gvo.setAtributo("ano", ano);
					tavo = new GeneralValueObject();
					tavo.setAtributo("tipoAnotacion", tipoAnot);
					tavo.setAtributo(mes, cuenta)	;																	
					gvo.setAtributo(tipoAnot, tavo);
					tipoAnotViejo = tipoAnot;
					anoViejo = ano;
					estadisticas.add(gvo);					
				}						
			}
			rs.close();	

			 
		} catch(BDException bde){
			bde.printStackTrace();
			m_Log.debug("InformesDAO. Error del AdaptadorSQL en el método getEstadísticas");
	 	} catch(SQLException sqle){
			sqle.printStackTrace();
			m_Log.debug("InformesDAO. Error de SQL en el método getEstadisticas");
	 	} catch(Exception e){
			e.printStackTrace();
			m_Log.debug("InformesDAO. Error inesperado en el método getEstadisticas" );
	 	} finally{
			try{
	   			if (rs!=null) rs.close();
	   			if (stmt!=null) stmt.close();
	   			aod.devolverConexion(con);
			} catch(Exception e){
	   			e.printStackTrace();
				m_Log.debug("InformesDAO. Error en el finally del método getEstadisticas");
			}		
  			return estadisticas;
		}
	}

    public Vector getTodasUnidades(String[] params,  GeneralValueObject parametrosConsultaVO) throws TechnicalException{

        Vector unidades = new Vector();
        AdaptadorSQLBD aod = new AdaptadorSQLBD(params);
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

          String tipo = (String)parametrosConsultaVO.getAtributo("tipo");
          String fechaInicio = (String)parametrosConsultaVO.getAtributo("fechaInicio");
          String fechaFin = (String)parametrosConsultaVO.getAtributo("fechaFin");
          String depto = (String)parametrosConsultaVO.getAtributo("depto");
          String numDesde = (String)parametrosConsultaVO.getAtributo("numDesde");


          String directivaUsuPermisoUor=(String)parametrosConsultaVO.getAtributo("directivaUsuPermisoUor");
          int codigoUsuario=(Integer)parametrosConsultaVO.getAtributo("codigoUsuario");
          int organizacionUsuario=(Integer)parametrosConsultaVO.getAtributo("organizacionUsuario");
          int entidadUsuario=(Integer)parametrosConsultaVO.getAtributo("entidadUsuario");
          



//      String sql = "SELECT UOR_COD FROM A_UOR WHERE UOR_COD > 0";

String sql = "  SELECT UOR_COD "+
             "  FROM A_UOR "+
             "  WHERE UOR_COD > 0 "+
             "  AND ( " +
		     "     0 < (" +
		     "         SELECT count(*) as total " +
		     "         FROM R_RES " +
		     "         WHERE res_dep ="+depto +
		     "         AND res_uod = A_UOR.UOR_COD "+
		     "         AND res_tip = '"+tipo + "'"+
		     "         AND ((res_fec BETWEEN " + aod.convertir("'" + fechaInicio + "'",
            AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " AND " + aod.convertir("'" + fechaFin + "'",
            AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ") OR (" +
            aod.convertir(aod.convertir("res_fec", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"),
            AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "=" +
      aod.convertir("'" + fechaFin + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "))))";

    if(("SI".equals(directivaUsuPermisoUor))&&("S".equals(tipo)))
    {
       sql=sql+ " AND UOR_COD in (SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU WHERE UOU_USU="+codigoUsuario+" AND UOU_ORG="+organizacionUsuario+" AND UOU_ENT="+entidadUsuario+")" ;
    }
/*
* Modificacion de la consulta SQL para la obtencion de las unidades (optimizacion)
*/
/*             sql +=  ")"+
		     "      OR  "+
		     "      0 < ( "+
		     "          SELECT COUNT(*) AS TOTAL " +
			 "          FROM R_RES "+
			 "          WHERE res_dep = "+ depto +
			 "          AND res_uod = a_uor.UOR_COD "+
			 "          AND res_tip = '"+ tipo + "'"+
			 "          AND (TO_DATE(TO_CHAR( res_fec,'DD/MM/YYYY'),'DD/MM/YYYY') = TO_DATE('" + fechaFin + "','DD/MM/YYYY'))";
            sql += ")"+
	         "      )";  */

        m_Log.debug(sql);
            try{
                con = aod.getConnection();
                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);

                while(rs.next()) {
                    unidades.add(rs.getString("UOR_COD"));
                }
                rs.close();
                stmt.close();

            } catch(BDException bde){
                bde.printStackTrace();
                m_Log.debug("InformesDAO. Error del AdaptadorSQL en el método getTodasUnidades");
             } catch(SQLException sqle){
                sqle.printStackTrace();
                m_Log.debug("InformesDAO. Error de SQL en el método getTodasUnidades");
             } catch(Exception e){
                e.printStackTrace();
                m_Log.debug("InformesDAO. Error inesperado en el método getTodasUnidades" );
             } finally{
                try{
                       aod.devolverConexion(con);
                } catch(Exception e){
                       e.printStackTrace();
                       m_Log.debug("InformesDAO. Error en el finally del método getTodasUnidades");
                }
            }
        return unidades;
        }


    /**
     * Obtiene los codigos de las unidades organicas externas
     * @param parametrosConsultaVO parametros necesarios para filtrar las consultas.
     * @param codOrganizacion Codigo de la organizacion externa.
     * @param codUnidadOrganica Codigo de la unidad organica externa. (0: todas las unidades de una organizacion externa).
     * @param params Parametros de conexion a la BD.
     * @return lista con los codigos de las unidades organicas externas.
     */
    public Vector getUnidadesExternas(GeneralValueObject parametrosConsultaVO, String codOrganizacion, String codUnidadOrganica, String[] params) {
        Vector unidades = new Vector();
        AdaptadorSQLBD aod = new AdaptadorSQLBD(params);
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        String tipo = (String) parametrosConsultaVO.getAtributo("tipo");    // Informe para entrada o salida
        String fechaInicio = (String) parametrosConsultaVO.getAtributo("fechaInicio");
        String fechaFin = (String) parametrosConsultaVO.getAtributo("fechaFin");
        String depto = (String) parametrosConsultaVO.getAtributo("depto");        
        String sql = "";

        if (codUnidadOrganica.equals("0")) {
            // obtenemos todas las uors de una organizacion
            sql += "SELECT UOREX_COD  FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOREX A_UOREX WHERE UOREX_COD > 0  AND UOREX_ORG ="  + codOrganizacion + " "
                    + "AND ( 0 < ( SELECT count(*) as total FROM R_RES WHERE res_dep =" + depto;

            if (tipo.equals("E") || tipo.equals("S")) {  // cuando es un informe del registro de entrada tenemos que obtener las uors de destino de otras organizaciones asignadas al algun registro
                sql += " AND res_ocd = A_UOREX.UOREX_ORG "
                        + " AND res_ucd = A_UOREX.UOREX_COD  ";

            } 

            sql += " AND res_tip = '" + tipo + "' "
                    + " AND ((res_fec BETWEEN " + aod.convertir("'" + fechaInicio + "'",
                    AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " AND " + aod.convertir("'" + fechaFin + "'",
                    AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ") OR ("
                    + aod.convertir(aod.convertir("res_fec", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"),
                    AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "="
                    + aod.convertir("'" + fechaFin + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "))))";

        } else {
            // obtenemos una unica uor
            sql = "SELECT UOREX_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOREX A_UOREX WHERE UOREX_ORG = " + codOrganizacion + " AND UOREX_COD=" + codUnidadOrganica;
        }

        try {
            con = aod.getConnection();
            stmt = con.createStatement();
            m_Log.debug(sql);
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                unidades.add(rs.getString("UOREX_COD"));
            }
            rs.close();
            stmt.close();

        } catch (BDException bde) {
            bde.printStackTrace();
            m_Log.debug("InformesDAO. Error del AdaptadorSQL en el método getUnidadesExternas");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            m_Log.debug("InformesDAO. Error de SQL en el método getUnidadesExternas");
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.debug("InformesDAO. Error inesperado en el método getUnidadesExternas");
        } finally {
            try {
                aod.devolverConexion(con);
            } catch (Exception e) {
                e.printStackTrace();
                m_Log.debug("InformesDAO. Error en el finally del método getUnidadesExternas");
            }
        }
        return unidades;
    }


    /**
     * Devuelve los codigos de las organizaciones externas
     * @param parametrosConsultaVO parametros necesarios para filtrar las consultas.
     * @param codOrganizacion Codigo de la organizacion externa.(0: todas las organizaciones externas).
     * @param params Parametros de conexion a la BD.
     * @return lista con los codigos de las organizaciones externas.
     */
    public Vector getOrganizacionesExternas(GeneralValueObject parametrosConsultaVO, String codOrganizacion, String[] params) {
        Vector organizaciones = new Vector();
        AdaptadorSQLBD aod = new AdaptadorSQLBD(params);
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        String tipo = (String) parametrosConsultaVO.getAtributo("tipo");    // Informe para entrada o salida
        String fechaInicio = (String) parametrosConsultaVO.getAtributo("fechaInicio");
        String fechaFin = (String) parametrosConsultaVO.getAtributo("fechaFin");
        String depto = (String) parametrosConsultaVO.getAtributo("depto");
        String sql = "";

        if (codOrganizacion.equals("0")) {
            // obtenemos todas las uors de una organizacion

            sql += "SELECT ORGEX_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "A_ORGEX A_ORGEX   WHERE ORGEX_COD > 0 "
                    + "AND ( 0 < ( SELECT count(*) as total FROM R_RES WHERE res_dep =" + depto;

            if (tipo.equals("E") || tipo.equals("S")) {  //  obtener las organizaciones de destino externas
                sql += " AND res_ocd = A_ORGEX.ORGEX_COD ";

            }

            sql += " AND res_tip = '" + tipo + "' "
                    + " AND ((res_fec BETWEEN " + aod.convertir("'" + fechaInicio + "'",
                    AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + " AND " + aod.convertir("'" + fechaFin + "'",
                    AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ") OR ("
                    + aod.convertir(aod.convertir("res_fec", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"),
                    AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "="
                    + aod.convertir("'" + fechaFin + "'", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "))))";

        } else {
            // obtenemos una unica organizacion
            sql = "SELECT ORGEX_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "A_ORGEX A_ORGEX WHERE ORGEX_COD = " + codOrganizacion + " ";
        }

        try {
            con = aod.getConnection();
            stmt = con.createStatement();
            m_Log.debug(sql);
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                organizaciones.add(rs.getString("ORGEX_COD"));
            }
            rs.close();
            stmt.close();

        } catch (BDException bde) {
            bde.printStackTrace();
            m_Log.debug("InformesDAO. Error del AdaptadorSQL en el método getOrganizacionesExternas");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            m_Log.debug("InformesDAO. Error de SQL en el método getOrganizacionesExternas");
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.debug("InformesDAO. Error inesperado en el método getOrganizacionesExternas");
        } finally {
            try {
                aod.devolverConexion(con);
            } catch (Exception e) {
                e.printStackTrace();
                m_Log.debug("InformesDAO. Error en el finally del método getOrganizacionesExternas");
            }
        }
        return organizaciones;
    }


    public String getUORPorCodigoVisual(String codigoVisual,String[] params){
        String resultado = null;
        
        SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(params[6]);
        for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {
            UORDTO unidad = entry.getValue();
            if (codigoVisual.equals(unidad.getUor_cod_vis()))
                resultado = unidad.getUor_cod();
        }

        return resultado;
    }      
    
      public String getClaveIdioma(String idioma,String[] params){
        String resultado = null;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            sql = "SELECT IDI_CLAVE FROM "+ GlobalNames.ESQUEMA_GENERICO +"A_IDI WHERE IDI_COD ="+idioma;

            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()){
                resultado = rs.getString("IDI_CLAVE");            
            }

        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
                if(rs != null)
                    rs.close();
                if(stmt != null)
                    stmt.close();
                abd.devolverConexion(conexion);
            } catch (Exception e) {m_Log.error(e);}
        }

        return resultado;
    }   
      
      
      public Vector getListadoInformeRegistro(String sql, String[] params){
                AdaptadorSQLBD abd = null;
                Connection con=null;
                ResultSet rs=null;
                PreparedStatement stmt = null;
                Vector<RegistroValueObject> listado = new Vector<RegistroValueObject>(); 
                try{
                    abd= new AdaptadorSQLBD(params);
                    con = abd.getConnection();
                    abd.inicioTransaccion(con);
                    m_Log.debug("CONSULTA "+sql);
                    stmt = con.prepareStatement(sql);
                    rs = stmt.executeQuery();
                    while(rs.next()){
                        RegistroValueObject registroVO = new RegistroValueObject(); 
                        registroVO.setNumReg(Long.valueOf(rs.getString("NUM")).longValue());
                        registroVO.setFecEntrada(rs.getString("FECHA"));
                        registroVO.setAsunto(rs.getString("ASUNTO"));
                        registroVO.setNombreInteresado(rs.getString("NOMBRE"));
                        registroVO.setApellido1Interesado(rs.getString("APELLIDO1"));
                        registroVO.setApellido2Interesado(rs.getString("APELLIDO2"));
                        registroVO.setOrganizacionOrigen(rs.getString("DESTINO"));
                        listado.addElement(registroVO);
                    }
                    
                }catch(Exception e){
                    e.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
                }finally{
                    try{
                        if(rs != null)
                        rs.close();
                        if(stmt != null)
                        stmt.close();
                        abd.devolverConexion(con);
                    } catch (Exception e){
                        m_Log.error(e);
                    }
                }
        return listado; 
        
    }
        

}
