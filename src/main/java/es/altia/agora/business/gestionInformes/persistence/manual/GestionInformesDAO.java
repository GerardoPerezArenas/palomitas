package es.altia.agora.business.gestionInformes.persistence.manual;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.gestionInformes.*;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Vector;
import java.util.HashMap;
import java.sql.*;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author sin atribuir
 * @version 1.0
 */

public class GestionInformesDAO {
   //Para el fichero de configuracion tecnico.
   protected static Config conf;
   //Para informacion de logs.
   protected static Log m_Log =
            LogFactory.getLog(GestionInformesDAO.class.getName());


   private static GestionInformesDAO instance = null;

   protected GestionInformesDAO() {
	super();
	//Queremos usar el fichero de configuracion techserver
       conf = ConfigServiceHelper.getConfig("techserver");
          //Conexion
   }

   public static GestionInformesDAO getInstance() {
	//si no hay ninguna instancia de esta clase tenemos que crear una.
	if (instance == null) {
	   // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
	   synchronized(GestionInformesDAO.class){
		if (instance == null)
		   instance = new GestionInformesDAO();
	   }
	}
	return instance;
   }

   public Vector getListaInformes(String[] params){
	Vector r = new Vector();
	AdaptadorSQLBD abd = null;
	Connection conexion = null;
	try{
	   abd = new AdaptadorSQLBD(params);
	   conexion = abd.getConnection();
	   String sql = "SELECT " + conf.getString("SQL.PLANT_INFORMES.cod") + "," +
                                conf.getString("SQL.PLANT_INFORMES.titulo") + "," +
	                            conf.getString("SQL.PLANT_INFORMES.pub") + "," +
                                conf.getString("SQL.PLANT_INFORMES.proced") + "," +
                                conf.getString("SQL.INF_ORIGEN.desc") +
       " FROM PLANT_INFORMES, INF_ORIGEN " +
       " WHERE " + conf.getString("SQL.PLANT_INFORMES.origen") + " = " + conf.getString("SQL.INF_ORIGEN.cod");
	   if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
	   Statement stmt = conexion.createStatement();
	   ResultSet rs = stmt.executeQuery(sql);
	   while(rs.next()){
		m_Log.debug("Entro en el bucle");
		InformeListaValueObject giVO = new InformeListaValueObject();
		giVO.setCodigo(rs.getString(1));
		giVO.setTitulo(rs.getString(2));
        if (rs.getString(3).equals("1")) {
             giVO.setPublicado("SI");
        } else {
             giVO.setPublicado("NO");
        }
        if (rs.getString(4) != null)
            giVO.setCodProcedimiento(rs.getString(4));
        else
             giVO.setCodProcedimiento("");
        giVO.setOrigen(rs.getString(5));
        r.add(giVO);
	   }
	   rs.close();
	   stmt.close();
	}
	catch (SQLException sqle){
	   m_Log.error("Error de SQL en getListaInformes: " + sqle.toString());
	}
	catch (BDException bde){
	   m_Log.error("error del OAD en el metodo getListaInformes: " +
				    bde.toString());
	}
	finally {
	   if (conexion != null){
		try{
		   abd.devolverConexion(conexion);
		}
		catch(BDException bde){
		   m_Log.error("No se pudo devolver la conexion: " + bde.toString());
		}
	   }
	}
	return r;
   }

    public Vector getListaInformesPublicados(UsuarioValueObject usuario, String origen){
        Vector r = new Vector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conexion = null;
        String[] params = usuario.getParamsCon();
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        try{
            conexion = abd.getConnection();
            String sql = "SELECT DISTINCT PLANT_PLANTILLA, PLANT_TITULO, PLANT_PUBLICADO, PLANT_PROCED, DESC_ORIGEN " +
                        " FROM "+ GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU " +
                        " INNER JOIN PLANT_INF_UOR ON (PLANT_INF_UOR.PLANT_INF_UOR_ID = A_UOU.UOU_UOR) " +
                        " INNER JOIN PLANT_INFORMES ON (PLANT_INF_UOR.PLANT_INF_UOR_PLANTILLA = PLANT_INFORMES.PLANT_PLANTILLA) " +
                        " INNER JOIN INF_ORIGEN ON (PLANT_INFORMES.PLANT_ORIGEN = INF_ORIGEN.ID_ORIGEN) " +
                        " WHERE PLANT_INFORMES.PLANT_PUBLICADO=1 AND A_UOU.UOU_USU = ?";
            if (origen != null && !origen.equals("")) sql += " AND INF_ORIGEN.ID_ORIGEN = " + origen;
            if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, usuario.getIdUsuario());
            rs = ps.executeQuery();
            while(rs.next()){
                 m_Log.debug("Entro en el bucle");
                 InformeListaValueObject giVO = new InformeListaValueObject();
                 giVO.setCodigo(rs.getString(1));
                 giVO.setTitulo(rs.getString(2));
                 if (rs.getString(3).equals("1")) {
                     giVO.setPublicado("SI");
                 } else {
                     giVO.setPublicado("NO");
                 }
                 giVO.setCodProcedimiento(rs.getString(4));
                 giVO.setOrigen(rs.getString(5));
                 r.add(giVO);
            }
            rs.close();
            ps.close();
     }
     catch (SQLException sqle){
        m_Log.error("Error de SQL en getListaInformesPublicados: " + sqle.toString());
     }
     catch (BDException bde){
        m_Log.error("error del OAD en el metodo getListaInformesPublicados: " +
                     bde.toString());
     }
     finally {
        if (conexion != null){
         try{
            abd.devolverConexion(conexion);
         }
         catch(BDException bde){
            m_Log.error("No se pudo devolver la conexion: " + bde.toString());
         }
        }
     }
     return r;
    }

    public Vector getListaInformes(GeneralValueObject gVO, String[] params){
     Vector r = new Vector();
     AdaptadorSQLBD abd = null;
     Connection conexion = null;
     try{
        abd = new AdaptadorSQLBD(params);
        conexion = abd.getConnection();
        String sql = "SELECT " + conf.getString("SQL.PLANT_INFORMES.cod") + "," +
                                 conf.getString("SQL.PLANT_INFORMES.titulo") + "," +
                                 conf.getString("SQL.PLANT_INFORMES.pub") + "," +
                                 conf.getString("SQL.PLANT_INFORMES.proced") +
                     " FROM PLANT_INFORMES " +
                     " WHERE 1=1 ";
        String proc = (String)gVO.getAtributo("proc");

        if (!proc.equals("")) sql += " AND " + conf.getString("SQL.PLANT_INFORMES.proced") +" = '" + proc + "'";
        String nombre = (String)gVO.getAtributo("nombre");
        if (!nombre.equals("")) sql += " AND " + conf.getString("SQL.PLANT_INFORMES.titulo") +" like '%" + nombre + "%'";
        String pub = (String)gVO.getAtributo("pub");
        String noPub = (String)gVO.getAtributo("noPub");
        if (!pub.equals("") && noPub.equals("")) sql += " AND " + conf.getString("SQL.PLANT_INFORMES.pub") +" = 1";
        else if (pub.equals("") && !noPub.equals("")) sql += " AND " + conf.getString("SQL.PLANT_INFORMES.pub") +" = 0";
        else if (pub.equals("") && noPub.equals("")) sql += " AND " + conf.getString("SQL.PLANT_INFORMES.pub") +" = -1"; // No salirá nada
        if(m_Log.isDebugEnabled()) m_Log.debug("proc: " + proc);
        if(m_Log.isDebugEnabled()) m_Log.debug("nombre: " + nombre);
        if(m_Log.isDebugEnabled()) m_Log.debug("pub: " + pub);
        if(m_Log.isDebugEnabled()) m_Log.debug("noPub: " + noPub);
        if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
         m_Log.debug("Entro en el bucle");
         InformeListaValueObject giVO = new InformeListaValueObject();
         giVO.setCodigo(rs.getString(1));
         giVO.setTitulo(rs.getString(2));
         if (rs.getString(3).equals("1")) {
             giVO.setPublicado("SI");
         } else {
             giVO.setPublicado("NO");
         }
         giVO.setCodProcedimiento(rs.getString(4));
         r.add(giVO);
        }
        rs.close();
        stmt.close();
     }
     catch (SQLException sqle){
        m_Log.error("Error de SQL en getListaInformes: " + sqle.toString());
     }
     catch (BDException bde){
        m_Log.error("error del OAD en el metodo getListaInformes: " +
                     bde.toString());
     }
     finally {
        if (conexion != null){
         try{
            abd.devolverConexion(conexion);
         }
         catch(BDException bde){
            m_Log.error("No se pudo devolver la conexion: " + bde.toString());
         }
        }
     }
     return r;
    }

    public Vector getTipoCamposInformes(String[] params){

    Vector listaTiposCampos = new Vector();
    HashMap hashMap;
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    try{
        abd = new AdaptadorSQLBD(params);
        conexion = abd.getConnection();
        String sql = "SELECT " + conf.getString("SQL.TIPO_CAMPOS_INFORMES.id") + "," + conf.getString("SQL.TIPO_CAMPOS_INFORMES.desc") +
                     " FROM TIPO_CAMPOS_INFORMES";
        if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            hashMap = new HashMap();
            hashMap.put("tipo",rs.getString(1));
            hashMap.put("descripcion",rs.getString(2));
            m_Log.debug("tipo " + hashMap.get("tipo") + " descripcion " + hashMap.get("descripcion"));
            listaTiposCampos.addElement(hashMap);
        }
        rs.close();
        stmt.close();
    } catch (SQLException sqle){
        m_Log.error("Error de SQL en getTipoCamposInformes: " + sqle.toString());
    } catch (BDException bde){
        m_Log.error("error del OAD en el metodo getTipoCamposInformes: " + bde.toString());
    } finally {
        if (conexion != null){
         try{
            abd.devolverConexion(conexion);
         }
         catch(BDException bde){
            m_Log.error("No se pudo devolver la conexion: " + bde.toString());
         }
        }
    }
    return listaTiposCampos;
    }

    public Vector getListaAmbitos(String[] params) {
        Vector listaAmbitos = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql = "SELECT " + conf.getString("SQL.INF_ORIGEN.cod") + "," + conf.getString("SQL.INF_ORIGEN.desc") +
                         ","+ conf.getString("SQL.INF_ORIGEN.tab") +","+ conf.getString("SQL.INF_ORIGEN.mod") +
                         " FROM INF_ORIGEN ORDER BY "+conf.getString("SQL.INF_ORIGEN.cod") + " ASC";
            if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                ElementoListaValueObject elvo = new ElementoListaValueObject();
                elvo.setCodigo(rs.getString(1));
                elvo.setDescripcion(rs.getString(2));
                elvo.setTab(rs.getString(3));
                elvo.setModo(rs.getString(4));
                listaAmbitos.addElement(elvo);
            }
            rs.close();
            stmt.close();

        } catch (SQLException sqle){
            m_Log.error("Error de SQL en getListaAmbitos: " + sqle.toString());
        } catch (BDException bde){
            m_Log.error("error del OAD en el metodo getListaAmbitos: " + bde.toString());
        } finally {
            if (conexion != null){
             try{
                abd.devolverConexion(conexion);
             }
             catch(BDException bde){
                m_Log.error("No se pudo devolver la conexion: " + bde.toString());
             }
            }
        }
        return listaAmbitos;
    }
    
    public Vector getListaModoAmbitos(String[] params) {
        Vector listaModoAmbitos = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql = "SELECT ID_MODOORIGEN,DESC_MODOORIGEN FROM INF_MODOORIGEN";
            if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
            	ElementoListaValueObject elvo = new ElementoListaValueObject();
                elvo.setCodigo(rs.getString(1));
                elvo.setDescripcion(rs.getString(2));
                listaModoAmbitos.addElement(elvo);
            }
            rs.close();
            stmt.close();

        } catch (SQLException sqle){
            m_Log.error("Error de SQL en getListaAmbitos: " + sqle.toString());
        } catch (BDException bde){
            m_Log.error("error del OAD en el metodo getListaAmbitos: " + bde.toString());
        } finally {
            if (conexion != null){
             try{
                abd.devolverConexion(conexion);
             }
             catch(BDException bde){
                m_Log.error("No se pudo devolver la conexion: " + bde.toString());
             }
            }
        }
        return listaModoAmbitos;
    }
    public Vector getListaCampos(String codigoAmbito, String[] params) {
        Vector listaCampos = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        int origen;
        String cri;
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql = "SELECT * FROM INF_CAMPOS WHERE ORIGEN="+codigoAmbito;
            if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
            	AmbitoListaValueObject elvo = new AmbitoListaValueObject();
                elvo.setCodigo(rs.getInt(1));
                elvo.setNome(rs.getString(2));
                elvo.setCampo(rs.getString(3));
                elvo.setTipo(rs.getString(4));
                elvo.setLonxitude(rs.getInt(5));
                //tengo el campo origen en el 6 saco la descripcion al que pertenece:
                origen=rs.getInt(6);
                String sql1 = "SELECT DESC_ORIGEN FROM INF_ORIGEN WHERE ID_ORIGEN="+origen;
                if(m_Log.isDebugEnabled()) m_Log.debug("sql1: " + sql1);
                Statement stmt1 = conexion.createStatement();
                ResultSet rs1 = stmt1.executeQuery(sql1);
                if(rs1.next()){
                	String DESC_ORIGEN=rs1.getString("DESC_ORIGEN");
                	// m_Log.debug("*******: " + DESC_ORIGEN);
                	 elvo.setDescOrigen(DESC_ORIGEN);
                }
                rs1.close();
                stmt1.close();
                elvo.setOrigen(origen);
                elvo.setNomeas(rs.getString(7));
                if(rs.getInt(8)==1){
                	cri="SI";
                }else{
                	cri="NO";
                }
                elvo.setCriterio(rs.getInt(8));
                elvo.setCri(cri);
                listaCampos.addElement(elvo);
               
            }
            
            rs.close();
            stmt.close();

        } catch (SQLException sqle){
            m_Log.error("Error de SQL en getListaAmbitos: " + sqle.toString());
        } catch (BDException bde){
            m_Log.error("error del OAD en el metodo getListaAmbitos: " + bde.toString());
        } finally {
            if (conexion != null){
             try{
                abd.devolverConexion(conexion);
             }
             catch(BDException bde){
                m_Log.error("No se pudo devolver la conexion: " + bde.toString());
             }
            }
        }
        return listaCampos;
    }

    public String getOrigenPlantilla(String[] params, String idPlantilla) {
        String origen = "";
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql = " select mod_origen " +
                         " from plant_informes " +
                         " inner join inf_origen on (id_origen = plant_origen) " +
                         " where plant_plantilla = " + idPlantilla;
            if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                origen = rs.getString(1);
            }
            rs.close();
            stmt.close();

        } catch (SQLException sqle){
            m_Log.error("Error de SQL en getOrigenPlantilla: " + sqle.toString());
        } catch (BDException bde){
            m_Log.error("error del OAD en el metodo getOrigenPlantilla: " + bde.toString());
        } finally {
            if (conexion != null){
             try{
                abd.devolverConexion(conexion);
             }
             catch(BDException bde){
                m_Log.error("No se pudo devolver la conexion: " + bde.toString());
             }
            }
        }
        return origen;
    }
    
    public boolean hayPermiso (String codPlantilla, String[] params){
        Vector r = new Vector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conexion = null;        
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        boolean hay = false;
        try{
            conexion = abd.getConnection();
            String sql = "SELECT DISTINCT PLANT_PLANTILLA, PLANT_TITULO, PLANT_PUBLICADO, PLANT_PROCED, DESC_ORIGEN " +
                        " FROM "+ GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU " +
                        " INNER JOIN PLANT_INF_UOR ON (PLANT_INF_UOR.PLANT_INF_UOR_ID = A_UOU.UOU_UOR) " +
                        " INNER JOIN PLANT_INFORMES ON (PLANT_INF_UOR.PLANT_INF_UOR_PLANTILLA = PLANT_INFORMES.PLANT_PLANTILLA) " +
                        " INNER JOIN INF_ORIGEN ON (PLANT_INFORMES.PLANT_ORIGEN = INF_ORIGEN.ID_ORIGEN) " +
                        " WHERE PLANT_INFORMES.PLANT_PLANTILLA = ?";
            
            if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + sql);
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(codPlantilla));
            rs = ps.executeQuery();
            hay = rs.next();
            rs.close();
            ps.close();
            
     }
     catch (SQLException sqle){
        m_Log.error("Error de SQL en hayPermiso: " + sqle.toString());
     }
     catch (BDException bde){
        m_Log.error("error del OAD en el metodo hayPermiso: " +
                     bde.toString());
     }
     finally {
        if (conexion != null){
         try{
            abd.devolverConexion(conexion);
         }
         catch(BDException bde){
            m_Log.error("No se pudo devolver la conexion: " + bde.toString());
         }
        }
     }
    return hay;
    }
}
