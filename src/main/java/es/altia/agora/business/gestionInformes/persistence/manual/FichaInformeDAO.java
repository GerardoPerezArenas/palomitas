package es.altia.agora.business.gestionInformes.persistence.manual;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.agora.business.gestionInformes.persistence.CampoValueObject;
import es.altia.agora.business.gestionInformes.*;
import es.altia.agora.business.gestionInformes.tareas.AsistenteCriterio;
import es.altia.agora.interfaces.user.web.gestionInformes.FachadaDatosInformes;
import es.altia.agora.interfaces.user.web.gestionInformes.FactoriaDatosInformes;
import es.altia.agora.interfaces.user.web.gestionInformes.exception.InstanciacionDatosInformesException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Vector;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author sin atribuir
 * @version 1.0
 */

public class FichaInformeDAO {
   //Para el fichero de configuracion tecnico.
   protected static Config conf;
   //Para informacion de logs.
   protected static Log m_Log =
            LogFactory.getLog(FichaInformeDAO.class.getName());


    protected static String tnu_mun;
    protected static String tnu_eje;
    protected static String tnu_num;
    protected static String tnu_cod;
    protected static String tnu_valor;

    protected static String tnut_mun;
    protected static String tnut_eje;
    protected static String tnut_num;
    protected static String tnut_cod;
    protected static String tnut_valor;
    protected static String tnut_tra;

    protected static String txt_mun;
    protected static String txt_eje;
    protected static String txt_num;
    protected static String txt_cod;
    protected static String txt_valor;

    protected static String txtt_mun;
    protected static String txtt_eje;
    protected static String txtt_num;
    protected static String txtt_cod;
    protected static String txtt_valor;
    protected static String txtt_tra;

    protected static String tfe_mun;
    protected static String tfe_eje;
    protected static String tfe_num;
    protected static String tfe_cod;
    protected static String tfe_valor;

    protected static String tfet_mun;
    protected static String tfet_eje;
    protected static String tfet_num;
    protected static String tfet_cod;
    protected static String tfet_valor;
    protected static String tfet_tra;

    protected static String ttl_mun;
    protected static String ttl_eje;
    protected static String ttl_num;
    protected static String ttl_cod;
    protected static String ttl_valor;

    protected static String ttlt_mun;
    protected static String ttlt_eje;
    protected static String ttlt_num;
    protected static String ttlt_cod;
    protected static String ttlt_valor;
    protected static String ttlt_tra;

    protected static String tde_mun;
    protected static String tde_eje;
    protected static String tde_num;
    protected static String tde_cod;
    protected static String tde_valor;

    protected static String tdet_mun;
    protected static String tdet_eje;
    protected static String tdet_num;
    protected static String tdet_cod;
    protected static String tdet_valor;
    protected static String tdet_tra;

    protected static String des_val_valor;
    protected static String des_val_nom;

    protected static String plant_inf_col_plantilla;
    protected static String plant_inf_col_idCampo;
    protected static String plant_inf_col_titulo;
    protected static String plant_inf_col_origen;
    protected static String plant_inf_col_posx;
    protected static String plant_inf_col_posy;
    protected static String plant_inf_col_align;
    protected static String plant_inf_col_tabla;
    protected static String plant_inf_col_ancho;
    protected static String plant_inf_col_elipsis;
    protected static String plant_inf_col_ord;

   private static FichaInformeDAO instance = null;
   private static int numeroCriterios = 4;

   protected FichaInformeDAO() {
	super();
	//Queremos usar el fichero de configuracion techserver
	conf = ConfigServiceHelper.getConfig("techserver");
       //Conexion

       tnu_mun = conf.getString("SQL.E_TNU.codMunicipio");
       tnu_eje = conf.getString("SQL.E_TNU.ejercicio");
       tnu_num = conf.getString("SQL.E_TNU.numeroExpediente");
       tnu_cod = conf.getString("SQL.E_TNU.codCampo");
       tnu_valor = conf.getString("SQL.E_TNU.valor");

       tnut_mun = conf.getString("SQL.E_TNUT.codMunicipio");
       tnut_eje = conf.getString("SQL.E_TNUT.ejercicio");
       tnut_num = conf.getString("SQL.E_TNUT.numeroExpediente");
       tnut_cod = conf.getString("SQL.E_TNUT.codCampo");
       tnut_valor = conf.getString("SQL.E_TNUT.valor");
       tnut_tra = conf.getString("SQL.E_TNUT.codTramite");

       txt_mun = conf.getString("SQL.E_TXT.codMunicipio");
       txt_eje = conf.getString("SQL.E_TXT.ejercicio");
       txt_num = conf.getString("SQL.E_TXT.numeroExpediente");
       txt_cod = conf.getString("SQL.E_TXT.codCampo");
       txt_valor = conf.getString("SQL.E_TXT.valor");

       txtt_mun = conf.getString("SQL.E_TXTT.codMunicipio");
       txtt_eje = conf.getString("SQL.E_TXTT.ejercicio");
       txtt_num = conf.getString("SQL.E_TXTT.numeroExpediente");
       txtt_cod = conf.getString("SQL.E_TXTT.codCampo");
       txtt_valor = conf.getString("SQL.E_TXTT.valor");
       txtt_tra = conf.getString("SQL.E_TXTT.codTramite");

       tfe_mun = conf.getString("SQL.E_TFE.codMunicipio");
       tfe_eje = conf.getString("SQL.E_TFE.ejercicio");
       tfe_num = conf.getString("SQL.E_TFE.numeroExpediente");
       tfe_cod = conf.getString("SQL.E_TFE.codCampo");
       tfe_valor = conf.getString("SQL.E_TFE.valor");

       tfet_mun = conf.getString("SQL.E_TFET.codMunicipio");
       tfet_eje = conf.getString("SQL.E_TFET.ejercicio");
       tfet_num = conf.getString("SQL.E_TFET.numeroExpediente");
       tfet_cod = conf.getString("SQL.E_TFET.codCampo");
       tfet_valor = conf.getString("SQL.E_TFET.valor");
       tfet_tra = conf.getString("SQL.E_TFET.codTramite");

       ttl_mun = conf.getString("SQL.E_TTL.codMunicipio");
       ttl_eje = conf.getString("SQL.E_TTL.ejercicio");
       ttl_num = conf.getString("SQL.E_TTL.numeroExpediente");
       ttl_cod = conf.getString("SQL.E_TTL.codCampo");
       ttl_valor = conf.getString("SQL.E_TTL.valor");

       ttlt_mun = conf.getString("SQL.E_TTLT.codMunicipio");
       ttlt_eje = conf.getString("SQL.E_TTLT.ejercicio");
       ttlt_num = conf.getString("SQL.E_TTLT.numeroExpediente");
       ttlt_cod = conf.getString("SQL.E_TTLT.codCampo");
       ttlt_valor = conf.getString("SQL.E_TTLT.valor");
       ttlt_tra = conf.getString("SQL.E_TTLT.codTramite");

       tde_mun = conf.getString("SQL.E_TDE.codMunicipio");
       tde_eje = conf.getString("SQL.E_TDE.ejercicio");
       tde_num = conf.getString("SQL.E_TDE.numeroExpediente");
       tde_cod = conf.getString("SQL.E_TDE.codCampo");
       tde_valor = conf.getString("SQL.E_TDE.valor");

       tdet_mun = conf.getString("SQL.E_TDET.codMunicipio");
       tdet_eje = conf.getString("SQL.E_TDET.ejercicio");
       tdet_num = conf.getString("SQL.E_TDET.numeroExpediente");
       tdet_cod = conf.getString("SQL.E_TDET.codCampo");
       tdet_valor = conf.getString("SQL.E_TDET.valor");
       tdet_tra = conf.getString("SQL.E_TDET.codTramite");

       des_val_valor = conf.getString("SQL.E_DES_VAL.codigoValor");
       des_val_nom = conf.getString("SQL.E_DES_VAL.nombreValor");

       plant_inf_col_plantilla = conf.getString("SQL.PLANT_INF_COL.plantilla");
       plant_inf_col_idCampo = conf.getString("SQL.PLANT_INF_COL.idCampo");
       plant_inf_col_titulo = conf.getString("SQL.PLANT_INF_COL.titulo");
       plant_inf_col_origen = conf.getString("SQL.PLANT_INF_COL.origen");
       plant_inf_col_posx = conf.getString("SQL.PLANT_INF_COL.posx");
       plant_inf_col_posy = conf.getString("SQL.PLANT_INF_COL.posy");
       plant_inf_col_align = conf.getString("SQL.PLANT_INF_COL.align");
       plant_inf_col_tabla = conf.getString("SQL.PLANT_INF_COL.tabla");
       plant_inf_col_ancho = conf.getString("SQL.PLANT_INF_COL.ancho");
       plant_inf_col_elipsis = conf.getString("SQL.PLANT_INF_COL.elipsis");
       plant_inf_col_ord = conf.getString("SQL.PLANT_INF_COL.ord");
   }

   public static FichaInformeDAO getInstance() {
	//si no hay ninguna instancia de esta clase tenemos que crear una.
	if (instance == null) {
	   // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
	   synchronized(FichaInformeDAO.class){
		if (instance == null)
		   instance = new FichaInformeDAO();
	   }
	}
	return instance;
   }

    public int altaInforme(FichaInformeValueObject fiVO, String[] params) {
        int resul = 0;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql = "";
        ResultSet rs = null;
        Statement stmt = null;
        try{
            int codigo=1;
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            sql="SELECT MAX(" + conf.getString("SQL.PLANT_INFORMES.cod") + ") FROM PLANT_INFORMES";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                codigo = rs.getInt(1)+1;
            }
            rs.close();
            stmt.close();
            sql ="INSERT INTO PLANT_INFORMES ("+ conf.getString("SQL.PLANT_INFORMES.cod") +", " +
                                                 conf.getString("SQL.PLANT_INFORMES.proced") + ", " +
                                                 conf.getString("SQL.PLANT_INFORMES.titulo") + ", " +
                                                 conf.getString("SQL.PLANT_INFORMES.pub") + ", " +
                                                 conf.getString("SQL.PLANT_INFORMES.origen") + ", " +
                                                 conf.getString("SQL.PLANT_INFORMES.fecha") +
                                             ") VALUES (" +
                                                codigo + ",'" +
                                                fiVO.getCodProcedimiento() + "','" +
                                                fiVO.getNombre()+
                                                "',0," +
                                                fiVO.getCodAmbito() + ", " +
                                                abd.funcionFecha(abd.FUNCIONFECHA_SYSDATE, null) + ")";
            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            fiVO.setCodPlantilla(String.valueOf(codigo));
        } catch (SQLException sqle) {
            rollBackTransaction(abd,conexion,sqle);
            m_Log.error("Error de SQL en altaInforme: " + sqle);
            resul=-1;
        } catch (BDException bde) {
            rollBackTransaction(abd,conexion,bde);
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo altaInforme: " + bde);
            resul=-1;
        } finally {
            commitTransaction(abd,conexion);
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }
            }
        }
        return resul;
    }

    
    public int eliminarInforme(String codigoPlantilla, String[] params) {
        int resul = 0;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql = "";
        ResultSet rs = null;
        Statement stmt = null;
        
        try{
        	abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            sql = "DELETE FROM PLANT_INFORMES WHERE "+ conf.getString("SQL.PLANT_INFORMES.cod")
            								  + "=" + codigoPlantilla ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            stmt.executeUpdate(sql);
            
        }catch(SQLException sqle) {
            rollBackTransaction(abd,conexion,sqle);
            m_Log.error("Error de SQL en eliminarInforme: " + sqle.toString());
            resul=-1;
        } catch (BDException bde) {
            rollBackTransaction(abd,conexion,bde);
            if(m_Log.isErrorEnabled()) m_Log.error("Error del OAD en el metodo eliminarInforme: " + bde.toString());
            resul=-1;
        } finally {
            commitTransaction(abd,conexion);
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde.toString());
                }
            }
        }       
        return resul;   
    }

    public int insertarInforme (InformeValueObject informeVO, String[] params) throws Exception {
        int resul = 0;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql;
        Statement stmt;
        int codigo = -1;
        ResultSet rs;

        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            sql="SELECT MAX(" + conf.getString("SQL.PLANT_INFORMES.cod") + ") FROM PLANT_INFORMES";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                codigo = rs.getInt(1)+1;
            }
            rs.close();
            stmt.close();

            informeVO.setCodPlantilla(String.valueOf(codigo));

            if (informeVO.getProcedimiento()==null) {
                informeVO.setProcedimiento("");
            }

            sql ="INSERT INTO PLANT_INFORMES ("+ conf.getString("SQL.PLANT_INFORMES.cod") +", " +
                                                 conf.getString("SQL.PLANT_INFORMES.proced") + ", " +
                                                 conf.getString("SQL.PLANT_INFORMES.titulo") + ", " +
                                                 conf.getString("SQL.PLANT_INFORMES.pub") + ", " +
                                                 conf.getString("SQL.PLANT_INFORMES.fecha") + ", " +
                                                 "PLANT_ORIGEN," +
                                                 conf.getString("SQL.PLANT_INFORMES.margenDer") + ", " +
                                                 conf.getString("SQL.PLANT_INFORMES.margenIzq") + ", " +
                                                 conf.getString("SQL.PLANT_INFORMES.margenSup") + ", " +
                                                 conf.getString("SQL.PLANT_INFORMES.margenInf") + ", " +
                                                 conf.getString("SQL.PLANT_INFORMES.papel") + ", " +
                                                 conf.getString("SQL.PLANT_INFORMES.orientacion") + " " +
                                             ") VALUES (" + informeVO.getCodPlantilla() + ",'" +
                                                informeVO.getProcedimiento() + "','" +
                                                informeVO.getTitulo() + "',0," +
                                                abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + "," + 
                                                informeVO.getAmbito() + ","+ informeVO.getMargenDerecho()+ ", "+
                                                informeVO.getMargenIzquierdo()+","+
                                                informeVO.getMargenInferior()+","+
                                                informeVO.getMargenSuperior()+",'"+
                                                informeVO.getPapel()+"','"+
                                                informeVO.getOrientacion()+"')";
            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();

            //Insertar los criterios informe
            Vector criterios = informeVO.getCriterios();
            CriteriosValueObject criterioVO;

            for (int i=0; i < criterios.size() ; i++) {
                criterioVO = (CriteriosValueObject) criterios.get(i);
                if (criterioVO.getValor1()==null) {
                    criterioVO.setValor1("");
                }
                if (criterioVO.getValor2()==null) {
                    criterioVO.setValor2("");
                }
                if (criterioVO.getTitulo()==null) {
                    criterioVO.setTitulo("");
                }
                if (criterioVO.getOrigen()==null) {
                    criterioVO.setOrigen("");
                }
                if (criterioVO.getTabla()==null) {
                    criterioVO.setTabla("");
                }
                sql ="INSERT INTO PLANT_INF_CRI (" + conf.getString("SQL.PLANT_INF_CRI.plantilla") + ", " +
                        conf.getString("SQL.PLANT_INF_CRI.id") + "," + conf.getString("SQL.PLANT_INF_CRI.campo") + ", " +
                        conf.getString("SQL.PLANT_INF_CRI.condicion") + "," + conf.getString("SQL.PLANT_INF_CRI.valor1") + "," +
                        conf.getString("SQL.PLANT_INF_CRI.valor2") + "," + conf.getString("SQL.PLANT_INF_CRI.titulo") + "," +
                        conf.getString("SQL.PLANT_INF_CRI.origen") + "," + conf.getString("SQL.PLANT_INF_CRI.tabla") +
                        ") VALUES (" + informeVO.getCodPlantilla() + "," + (i+1) + "," +
                        criterioVO.getCampo() + "," + criterioVO.getCondicion() + ",'" + criterioVO.getValor1() + "','" +
                        criterioVO.getValor2() + "','" + criterioVO.getTitulo() + "','" + criterioVO.getOrigen() + "','" +
                        criterioVO.getTabla() +  "')";
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }

            //Insertar la cabecera informe
            Vector vectorCabInforme = informeVO.getVectorCabInformeVO();
            CabInformeValueObject cabInformeVO;

            for (int i=0; i < vectorCabInforme.size() ; i++) {
                cabInformeVO = (CabInformeValueObject) vectorCabInforme.get(i);
                sql ="INSERT INTO PLANT_INF_CAB_I (" + conf.getString("SQL.PLANT_INF_CAB_I.plantilla")+", " +
                        conf.getString("SQL.PLANT_INF_CAB_I.idCampo") + "," + conf.getString("SQL.PLANT_INF_CAB_I.tipo") + "," +
                        conf.getString("SQL.PLANT_INF_CAB_I.posx") + "," + conf.getString("SQL.PLANT_INF_CAB_I.posy") +
                        ") VALUES (" + informeVO.getCodPlantilla() + "," + (i+1) + ",'" + cabInformeVO.getTipo() + "'," +
                        cabInformeVO.getPosX() + "," + cabInformeVO.getPosY() + ")";
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }

            //Insertar la cabecera pagina
            Vector vectorCabPaginaInforme = informeVO.getVectorCabPaginaInformeVO();
            CabPaginaInformeValueObject cabPaginaInformeVO;

            for (int i=0; i < vectorCabPaginaInforme.size() ; i++) {
                cabPaginaInformeVO = (CabPaginaInformeValueObject) vectorCabPaginaInforme.get(i);
                sql ="INSERT INTO PLANT_INF_CAB_P (" + conf.getString("SQL.PLANT_INF_CAB_P.plantilla")+", " +
                        conf.getString("SQL.PLANT_INF_CAB_P.idCampo") + "," + conf.getString("SQL.PLANT_INF_CAB_P.tipo") + "," +
                        conf.getString("SQL.PLANT_INF_CAB_P.posx") + "," + conf.getString("SQL.PLANT_INF_CAB_P.posy") +
                        ") VALUES (" + informeVO.getCodPlantilla() + "," + (i+1) + ",'" + cabPaginaInformeVO.getTipo() + "'," +
                        cabPaginaInformeVO.getPosX() + "," + cabPaginaInformeVO.getPosY() + ")";
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }

            //Insertar el pie pagina
            Vector vectorPiePaginaInforme = informeVO.getVectorPiePaginaInformeVO();
            PiePaginaInformeValueObject piePaginaInformeVO;

            for (int i=0; i < vectorPiePaginaInforme.size() ; i++) {
                piePaginaInformeVO = (PiePaginaInformeValueObject) vectorPiePaginaInforme.get(i);
                sql ="INSERT INTO PLANT_INF_PIE_P (" + conf.getString("SQL.PLANT_INF_PIE_P.plantilla")+", " +
                        conf.getString("SQL.PLANT_INF_PIE_P.idCampo") + "," + conf.getString("SQL.PLANT_INF_PIE_P.tipo") +
                        ") VALUES (" + informeVO.getCodPlantilla() + "," + (i+1) + "," + piePaginaInformeVO.getTipo() + ")";
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }

            //Insertar el pie informe
            Vector vectorPieInforme = informeVO.getVectorPieInformeVO();
            PieInformeValueObject pieInformeVO;

            for (int i=0; i < vectorPieInforme.size() ; i++) {
                pieInformeVO = (PieInformeValueObject) vectorPieInforme.get(i);
                sql ="INSERT INTO PLANT_INF_PIE_I (" + conf.getString("SQL.PLANT_INF_PIE_I.plantilla")+", " +
                        conf.getString("SQL.PLANT_INF_PIE_I.idCampo") + "," + conf.getString("SQL.PLANT_INF_PIE_I.tipo") +
                        ") VALUES (" + informeVO.getCodPlantilla() + "," + (i+1) + "," + pieInformeVO.getTipo() + ")";
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }

            //Insertar los campos asociados
            Vector vectorCuerpoInforme = informeVO.getVectorCuerpoInformeVO();
            CuerpoInformeValueObject cuerpoInformeVO;

            for (int i=0; i < vectorCuerpoInforme.size() ; i++) {
                cuerpoInformeVO = (CuerpoInformeValueObject) vectorCuerpoInforme.get(i);
                int elipsis = 0;
                if (cuerpoInformeVO.getElipsis()) {
                    elipsis = 1;
                }
                sql ="INSERT INTO PLANT_INF_COL (" + plant_inf_col_plantilla+", " +
                        plant_inf_col_idCampo + "," + plant_inf_col_titulo + "," +
                        plant_inf_col_origen + "," + plant_inf_col_posx + "," +
                        plant_inf_col_posy + "," + plant_inf_col_align + "," +
                        plant_inf_col_tabla + "," + plant_inf_col_ancho + "," +
                        plant_inf_col_elipsis + "," + plant_inf_col_ord +") VALUES (" + informeVO.getCodPlantilla() + "," + (i+1) + ",'" +
                        cuerpoInformeVO.getTitulo() + "','" + cuerpoInformeVO.getOrigen() + "'," + cuerpoInformeVO.getPosX() +
                        "," + cuerpoInformeVO.getPosY() + ",'" + cuerpoInformeVO.getAlign() + "','" + cuerpoInformeVO.getTabla() +
                        "'," + cuerpoInformeVO.getAncho() + ","+ elipsis + "," + cuerpoInformeVO.getOrden() +")";
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }

            Vector listaPermisos = informeVO.getVectorPermisos();
            for (int i=0; i < listaPermisos.size() ; i++) {
                String permiso = (String) listaPermisos.get(i);
                sql ="INSERT INTO PLANT_INF_UOR (PLANT_INF_UOR_PLANTILLA, PLANT_INF_UOR_ID) " +
                        "(select "+ informeVO.getCodPlantilla()+", uor_cod from a_uor where uor_cod_vis='"+permiso+"')";
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }

        } catch (SQLException sqle) {
            abd.rollBack(conexion);
            m_Log.error("Error de SQL en insertarInforme: ");
            sqle.printStackTrace();
            resul=-1;
            throw sqle;
        } catch (BDException bde) {
            abd.rollBack(conexion);
            if(m_Log.isErrorEnabled()) m_Log.error("Error del OAD en el metodo insertarInforme: ");
            bde.printStackTrace();
            resul=-1;
            throw bde;
        } finally {
            abd.finTransaccion(conexion);
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: ");
                    bde.printStackTrace();
                    throw bde;
                }
            }
        }
        return resul;
    }

    public int grabarInforme (FichaInformeValueObject fiVO, String[] params) {
        int resul = 0;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql = "";
        Statement stmt = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            sql ="UPDATE PLANT_INFORMES SET " + conf.getString("SQL.PLANT_INFORMES.papel") + "='" + fiVO.getPapel() + "', " +
                    conf.getString("SQL.PLANT_INFORMES.orientacion") + "='" + fiVO.getOrientacion() + "', " +
                    conf.getString("SQL.PLANT_INFORMES.margenSup") + "=" + fiVO.getMargenSup() + ", " +
                    conf.getString("SQL.PLANT_INFORMES.margenInf") + "=" + fiVO.getMargenInf() + ", " +
                    conf.getString("SQL.PLANT_INFORMES.margenDer") + "=" + fiVO.getMargenDer() + ", " +
                    conf.getString("SQL.PLANT_INFORMES.margenIzq") + "=" + fiVO.getMargenIzq() +
                    " WHERE " + conf.getString("SQL.PLANT_INFORMES.cod") +" = " + fiVO.getCodPlantilla() ;
            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();

            //Primero eliminar los criterios informe
            sql ="DELETE PLANT_INF_CRI WHERE " + conf.getString("SQL.PLANT_INF_CRI.plantilla") +" = " + fiVO.getCodPlantilla() ;
            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();

            //Insertar los criterios informe
            Vector listaCriInfCodigo = fiVO.getListaCriInfCodigo();
            Vector listaCriInfCondCri = fiVO.getListaCriInfCondCri();
            Vector listaCriInfValor1 = fiVO.getListaCriInfValor1();
            Vector listaCriInfValor2 = fiVO.getListaCriInfValor2();
            Vector listaCriInfTitulo = fiVO.getListaCriInfTitulo();
            Vector listaCriInfOrigen = fiVO.getListaCriInfOrigen();
            Vector listaCriInfTabla = fiVO.getListaCriInfTabla();

            for (int i=0; i < listaCriInfCodigo.size() ; i++) {
                if (listaCriInfCodigo.get(i).equals(String.valueOf(AsistenteCriterio.CRITERIO_ESTADO))) {
                    if (listaCriInfValor1.get(i).equals("1")) {
                        listaCriInfValor1.set(i,"9");
                    }
                }
                if (listaCriInfValor1.get(i).equals("null")) {
                    listaCriInfValor1.set(i,"");
                }
                if (listaCriInfValor2.get(i).equals("null")) {
                    listaCriInfValor2.set(i,"");
                }
                if (listaCriInfTitulo.get(i).equals("null")) {
                    listaCriInfTitulo.set(i,"");
                }
                if (listaCriInfOrigen.get(i).equals("null")) {
                    listaCriInfOrigen.set(i,"");
                }
                if (listaCriInfTabla.get(i).equals("null")) {
                    listaCriInfTabla.set(i,"");
                }
                sql ="INSERT INTO PLANT_INF_CRI (" + conf.getString("SQL.PLANT_INF_CRI.plantilla") + ", " +
                        conf.getString("SQL.PLANT_INF_CRI.id") + "," + conf.getString("SQL.PLANT_INF_CRI.campo") + ", " +
                        conf.getString("SQL.PLANT_INF_CRI.condicion") + "," + conf.getString("SQL.PLANT_INF_CRI.valor1") + "," +
                        conf.getString("SQL.PLANT_INF_CRI.valor2") + "," + conf.getString("SQL.PLANT_INF_CRI.titulo") + "," +
                        conf.getString("SQL.PLANT_INF_CRI.origen") + "," + conf.getString("SQL.PLANT_INF_CRI.tabla") +
                        ") VALUES (" + fiVO.getCodPlantilla() + "," + (i+1) + "," +
                        listaCriInfCodigo.get(i) + "," + listaCriInfCondCri.get(i) + ",'" + listaCriInfValor1.get(i) + "','" +
                        listaCriInfValor2.get(i) + "','" + listaCriInfTitulo.get(i) + "','" + listaCriInfOrigen.get(i) + "','" +
                        listaCriInfTabla.get(i) + "')";
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }

            //Primero eliminar la cabecera informe
            sql ="DELETE PLANT_INF_CAB_I WHERE " + conf.getString("SQL.PLANT_INF_CAB_I.plantilla") +" = " + fiVO.getCodPlantilla() ;
            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();

            //Insertar la cabecera informe
            Vector listaCabInfCodigo = fiVO.getListaCabInfCodigo();
            Vector listaCabInfPosx = fiVO.getListaCabInfPosx();
            Vector listaCabInfPosy = fiVO.getListaCabInfPosy();
            for (int i=0; i < listaCabInfCodigo.size() ; i++) {
                sql ="INSERT INTO PLANT_INF_CAB_I (" + conf.getString("SQL.PLANT_INF_CAB_I.plantilla")+", " +
                        conf.getString("SQL.PLANT_INF_CAB_I.idCampo") + "," + conf.getString("SQL.PLANT_INF_CAB_I.tipo") + "," +
                        conf.getString("SQL.PLANT_INF_CAB_I.posx") + "," + conf.getString("SQL.PLANT_INF_CAB_I.posy") +
                        ") VALUES (" + fiVO.getCodPlantilla() + "," + (i+1) + ",'" + listaCabInfCodigo.get(i) + "'," +
                        listaCabInfPosx.get(i) + "," + listaCabInfPosy.get(i) + ")";
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }

            //Primero eliminar el pie informe
            sql ="DELETE PLANT_INF_PIE_I WHERE " + conf.getString("SQL.PLANT_INF_PIE_I.plantilla") +" = " + fiVO.getCodPlantilla() ;
            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();

            //Insertar el pie informe
            Vector listaPieInfCodigo = fiVO.getListaPieInfCodigo();
            for (int i=0; i < listaPieInfCodigo.size() ; i++) {
                sql ="INSERT INTO PLANT_INF_PIE_I (" + conf.getString("SQL.PLANT_INF_PIE_I.plantilla")+", " +
                        conf.getString("SQL.PLANT_INF_PIE_I.idCampo") + "," + conf.getString("SQL.PLANT_INF_PIE_I.tipo") +
                        ") VALUES (" + fiVO.getCodPlantilla() + "," + (i+1) + "," + listaPieInfCodigo.get(i) + ")";
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }

            //Primero eliminar la cabecera pagina
            sql ="DELETE PLANT_INF_CAB_P WHERE " + conf.getString("SQL.PLANT_INF_CAB_P.plantilla") +" = " + fiVO.getCodPlantilla() ;
            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();

            //Insertar la cabecera pagina
            Vector listaCabPagCodigo = fiVO.getListaCabPagCodigo();
            Vector listaCabPagPosx = fiVO.getListaCabPagPosx();
            Vector listaCabPagPosy = fiVO.getListaCabPagPosy();
            for (int i=0; i < listaCabPagCodigo.size() ; i++) {
                sql ="INSERT INTO PLANT_INF_CAB_P (" + conf.getString("SQL.PLANT_INF_CAB_P.plantilla")+", " +
                        conf.getString("SQL.PLANT_INF_CAB_P.idCampo") + "," + conf.getString("SQL.PLANT_INF_CAB_P.tipo") + "," +
                        conf.getString("SQL.PLANT_INF_CAB_P.posx") + "," + conf.getString("SQL.PLANT_INF_CAB_P.posy") +
                        ") VALUES (" + fiVO.getCodPlantilla() + "," + (i+1) + ",'" + listaCabPagCodigo.get(i) + "'," +
                        listaCabPagPosx.get(i) + "," + listaCabPagPosy.get(i) + ")";
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }

            //Primero eliminar el pie pagina
            sql ="DELETE PLANT_INF_PIE_P WHERE " + conf.getString("SQL.PLANT_INF_PIE_P.plantilla") +" = " + fiVO.getCodPlantilla() ;
            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();

            //Insertar el pie pagina
            Vector listaPiePagCodigo = fiVO.getListaPiePagCodigo();
            for (int i=0; i < listaPiePagCodigo.size() ; i++) {
                sql ="INSERT INTO PLANT_INF_PIE_P (" + conf.getString("SQL.PLANT_INF_PIE_P.plantilla")+", " +
                        conf.getString("SQL.PLANT_INF_PIE_P.idCampo") + "," + conf.getString("SQL.PLANT_INF_PIE_P.tipo") +
                        ") VALUES (" + fiVO.getCodPlantilla() + "," + (i+1) + "," + listaPiePagCodigo.get(i) + ")";
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }

            //Primero eliminar los permisos
            sql ="DELETE PLANT_INF_UOR WHERE " + conf.getString("SQL.PLANT_INF_UOR.plantilla") +" = " + fiVO.getCodPlantilla() ;
            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();

            //Insertar el pie pagina
            Vector listaPermisosCodigo = fiVO.getListaUOR();
            for (int i=0; i < listaPermisosCodigo.size() ; i++) {
                sql ="INSERT INTO PLANT_INF_UOR (" + conf.getString("SQL.PLANT_INF_UOR.plantilla")+", " +
                        conf.getString("SQL.PLANT_INF_UOR.id") + ") VALUES (" + fiVO.getCodPlantilla() + "," + listaPermisosCodigo.get(i) + ")";
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }

            //Primero eliminar los campos asociados
            sql ="DELETE PLANT_INF_COL WHERE " + plant_inf_col_plantilla +" = " + fiVO.getCodPlantilla() ;
            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();

            //Insertar los campos asociados
            Vector listaCamposCodigo = fiVO.getListaCamposCodigo();
            Vector listaCamposNombre = fiVO.getListaCamposNombre();
            Vector listaCamposTabla = fiVO.getListaCamposTabla();
            Vector listaCamposPosx = fiVO.getListaCamposPosx();
            Vector listaCamposPosy = fiVO.getListaCamposPosy();
            Vector listaCamposAlign = fiVO.getListaCamposAlign();
            Vector listaCamposAncho = fiVO.getListaCamposAncho();
            Vector listaCamposElipsis = fiVO.getListaCamposElipsis();
            Vector listaCamposOrden = fiVO.getListaCamposOrden();
            for (int i=0; i < listaCamposCodigo.size() ; i++) {
                sql ="INSERT INTO PLANT_INF_COL (" + plant_inf_col_plantilla+", " +
                        plant_inf_col_idCampo + "," + plant_inf_col_titulo + "," +
                        plant_inf_col_origen + "," + plant_inf_col_posx + "," +
                        plant_inf_col_posy + "," + plant_inf_col_align + "," +
                        plant_inf_col_tabla + "," + plant_inf_col_ancho + "," +
                        plant_inf_col_elipsis +"," +plant_inf_col_ord +") VALUES (" + fiVO.getCodPlantilla() + "," + (i+1) + ",'" +
                        listaCamposNombre.get(i) + "','" + listaCamposCodigo.get(i) + "'," + listaCamposPosx.get(i) +
                        "," + listaCamposPosy.get(i) + ",'" + listaCamposAlign.get(i) + "','" + listaCamposTabla.get(i) +
                        "'," + listaCamposAncho.get(i) + ","+ listaCamposElipsis.get(i) + ","+ listaCamposOrden.get(i) + ")";
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            }
        } catch (SQLException sqle) {
            rollBackTransaction(abd,conexion,sqle);
            m_Log.error("Error de SQL en grabarInforme: " + sqle);
            resul=-1;
        } catch (BDException bde) {
            rollBackTransaction(abd,conexion,bde);
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo grabarInforme: " + bde);
            resul=-1;
        } finally {
            commitTransaction(abd,conexion);
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }
            }
        }
        return resul;
    }

    public int publicarInforme (String codInforme,String publicar, String[] params) {
        int resul = 0;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql = "";
        Statement stmt = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            sql ="UPDATE PLANT_INFORMES SET " + conf.getString("SQL.PLANT_INFORMES.pub") + " = " + publicar +
                    " WHERE " + conf.getString("SQL.PLANT_INFORMES.cod") +" = " + codInforme ;
            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException sqle) {
            rollBackTransaction(abd,conexion,sqle);
            m_Log.error("Error de SQL en publicarInforme: " + sqle);
            resul=-1;
        } catch (BDException bde) {
            rollBackTransaction(abd,conexion,bde);
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo publicarInforme: " + bde);
            resul=-1;
        } finally {
            commitTransaction(abd,conexion);
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }
            }
        }
        return resul;
    }

    public FichaInformeValueObject cargarInforme(String plantilla, String codMunicipio, String[] params) {
        int resul = 0;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql = "";
        ResultSet rs,rs2 = null;
        Statement stmt,stmt2 = null;
        FichaInformeValueObject fiVO = new FichaInformeValueObject();
        try{
            fiVO.setCodMunicipio(codMunicipio);
            int codigo=1;
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            sql ="SELECT " + conf.getString("SQL.PLANT_INFORMES.titulo") + "," + conf.getString("SQL.PLANT_INFORMES.proced") + "," +
                    conf.getString("SQL.E_PML.valor") + ", " + conf.getString("SQL.PLANT_INFORMES.pub") + ", " +
                    conf.getString("SQL.PLANT_INFORMES.papel") + ", "  + conf.getString("SQL.PLANT_INFORMES.orientacion") + ", " +
                    conf.getString("SQL.PLANT_INFORMES.margenSup") + ", " + conf.getString("SQL.PLANT_INFORMES.margenInf")  + ", " +
                    conf.getString("SQL.PLANT_INFORMES.margenDer") + ", "  + conf.getString("SQL.PLANT_INFORMES.margenIzq")  + ", " +
                    conf.getString("SQL.INF_ORIGEN.cod") + ", " + conf.getString("SQL.INF_ORIGEN.desc") + ", " +
                    conf.getString("SQL.INF_ORIGEN.mod") + ", " + conf.getString("SQL.INF_ORIGEN.tab") +
                    " FROM PLANT_INFORMES " +
                    " INNER JOIN INF_ORIGEN ON (" + conf.getString("SQL.INF_ORIGEN.cod") + " = " +
                                                    conf.getString("SQL.PLANT_INFORMES.origen") +  ")" +

                    " LEFT JOIN E_PML ON ( " + conf.getString("SQL.E_PML.codProcedimiento") +" = " + conf.getString("SQL.PLANT_INFORMES.proced") +
                    " AND " + conf.getString("SQL.E_PML.codCampoML") + " = 'NOM' AND " + conf.getString("SQL.E_PML.idioma") + " = " + conf.getString("idiomaDefecto") +
                    " AND " + conf.getString("SQL.E_PML.codMunicipio") +" = " + codMunicipio + ")" +
                    " WHERE " + conf.getString("SQL.PLANT_INFORMES.cod") +" = " + plantilla ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                fiVO.setCodPlantilla(plantilla);
                fiVO.setNombre(rs.getString(1));
                fiVO.setCodProcedimiento(rs.getString(2));
                fiVO.setDescProcedimiento(rs.getString(3));
                fiVO.setCodAmbito(rs.getString(11));
                fiVO.setDescAmbito(rs.getString(12));
                fiVO.setModAmbito(rs.getString(13));
                fiVO.setTabAmbito(rs.getString(14));
                fiVO.setPublicado(rs.getString(4));
                fiVO.setPapel(rs.getString(5));
                String orientacion = rs.getString(6);
                if (orientacion==null) orientacion="Horizontal";
                fiVO.setOrientacion(orientacion);
                String margenSup = rs.getString(7);
                if (margenSup==null) margenSup="20";
                fiVO.setMargenSup(margenSup);
                String margenInf = rs.getString(8);
                if (margenInf==null) margenInf="20";
                fiVO.setMargenInf(margenInf);
                String margenDer = rs.getString(9);
                if (margenDer==null) margenDer="20";
                fiVO.setMargenDer(margenDer);
                String margenIzq = rs.getString(10);
                if (margenIzq==null) margenIzq="20";
                fiVO.setMargenIzq(margenIzq);
            }
            rs.close();
            stmt.close();


            //Campos Disponibles
            FachadaDatosInformes fachadaDatos = FactoriaDatosInformes.getImpl(fiVO.getModAmbito());
            Vector listaCamposDisponibles = fachadaDatos.getListaCampos(fiVO, params);            
            
            //Campos seleccionados
            sql ="SELECT " + plant_inf_col_idCampo + "," +
                    plant_inf_col_titulo + ", " + plant_inf_col_origen + ", " +
                    plant_inf_col_posx + ", "  + plant_inf_col_posy + ", " +
                    plant_inf_col_align + ", " + plant_inf_col_tabla  + ", " +
                    plant_inf_col_ancho + ", "  + plant_inf_col_elipsis +", "  + plant_inf_col_ord +
                    " FROM PLANT_INF_COL " +
                    " WHERE " + plant_inf_col_plantilla +" = " + plantilla + " ORDER BY 1";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            Vector listaCamposSeleccionados = new Vector();
            while (rs.next()) {
                CampoValueObject campo = new CampoValueObject();
                campo.setIdCampo(rs.getString(1));
                campo.setTitulo(rs.getString(2));
                campo.setOrigen(rs.getString(3));
                campo.setPosx(rs.getString(4));
                campo.setPosy(rs.getString(5));
                campo.setAlign(rs.getString(6));
                campo.setTabla(rs.getString(7));
                campo.setAncho(rs.getString(8));
                campo.setElipsis(rs.getString(9));
                campo.setOrden(rs.getString(10));
                
                m_Log.debug("** campo"+ campo.getOrden());
                listaCamposSeleccionados.add(campo);
            }            


            /* Borramos los campos seleccionados de la lista de disponibles y a la vez les añadimos la
             * informacion que les falta (TituloOriginal) 
             */            
            for (int i=0; i<listaCamposSeleccionados.size(); i++) {
                CampoValueObject campoSeleccionado = (CampoValueObject) listaCamposSeleccionados.get(i);
                for (int j=0; j<listaCamposDisponibles.size(); j++) {
                    CampoValueObject campoDisponible = (CampoValueObject)listaCamposDisponibles.get(j);
                    if (campoSeleccionado.getOrigen().equals(campoDisponible.getOrigen())) {                    	
                    	campoSeleccionado.setTituloOriginal(campoDisponible.getTitulo());
                        listaCamposDisponibles.remove(campoDisponible);
                        break;
                    }
                }
            }

            //Ponerle un ID a cada campo para poder luego trabajar en JAVASCRIPT con sus propiedades
            // Disponibles
            for (int i=0;i<listaCamposDisponibles.size(); i++) {
                CampoValueObject campoDisponible = (CampoValueObject)listaCamposDisponibles.get(i);
                campoDisponible.setIdCampo(String.valueOf(i));
            }
            // Seleccionados
            for (int i=0;i<listaCamposSeleccionados.size(); i++) {
                CampoValueObject campoSeleccionado = (CampoValueObject)listaCamposSeleccionados.get(i);
                campoSeleccionado.setIdCampo(String.valueOf(i+listaCamposDisponibles.size()));
            }

            // Debug
            for (int i=0;i<listaCamposDisponibles.size();i++) {
                m_Log.debug("CAMPOS DISPONIBLES "+listaCamposDisponibles.get(i).toString());
            }

            for (int i=0;i<listaCamposSeleccionados.size();i++) {
                m_Log.debug("CAMPOS SELECCIONADOS "+ listaCamposSeleccionados.get(i).toString());
            }
            

            fiVO.setListaCamposDisponibles(listaCamposDisponibles);
            fiVO.setListaCampos(listaCamposSeleccionados);

                
            //Criterios disponibles
            Vector listaCriteriosDisponibles = fachadaDatos.getListaCriInforme(fiVO, params);          
            //Criterios seleccionados
            Vector listaCriteriosSeleccionados = fachadaDatos.getListaCriSeleccionados(fiVO, params);
     
       
            // Quitamos los criterios seleccionados de la lista de disponibles (en este momento están todos)
            for (int i=0; i<listaCriteriosSeleccionados.size(); i++) {
                CampoValueObject criInfSeleccionado = (CampoValueObject)listaCriteriosSeleccionados.get(i);
                for (int j=0; j<listaCriteriosDisponibles.size(); j++) {
                    CampoValueObject criInfDisponible = (CampoValueObject)listaCriteriosDisponibles.get(j);
                    if (criInfSeleccionado.getTitulo().equals(criInfDisponible.getTitulo())) {
                    	listaCriteriosDisponibles.remove(criInfDisponible);                    	
                        //Ponerle un ID a cada criterio seleccionado (el que tenia en la lista de disponibles)
                    	criInfSeleccionado.setIdCampo(criInfDisponible.getIdCampo());
                        break;
                    }
                }
            }

            fiVO.setListaCriInf(listaCriteriosSeleccionados);
            fiVO.setListaCriInfDisponibles(listaCriteriosDisponibles);

            m_Log.debug("\n");
            for (int i=0;i<listaCriteriosDisponibles.size();i++) {
                m_Log.debug("CRITERIOS DISPONIBLES "+listaCriteriosDisponibles.get(i).toString());
            }
            for (int i=0;i<listaCriteriosSeleccionados.size();i++) {
                m_Log.debug("CRITERIOS SELECCIONADOS "+listaCriteriosSeleccionados.get(i).toString());
            }

            //Cabecera informe seleccionados
            sql = "SELECT " + conf.getString("SQL.PLANT_INF_CAB_I.tipo") + ", " + conf.getString("SQL.TIPO_CAMPOS_INFORMES.titulo") +
                    "," + conf.getString("SQL.PLANT_INF_CAB_I.posx") +"," + conf.getString("SQL.PLANT_INF_CAB_I.posy") +
                    " FROM PLANT_INF_CAB_I " +
                    " INNER JOIN TIPO_CAMPOS_INFORMES ON (" + conf.getString("SQL.PLANT_INF_CAB_I.tipo") + " = " + conf.getString("SQL.TIPO_CAMPOS_INFORMES.id") + ")" +
                    " INNER JOIN TIPO_CAMPOS_MODULOS_INFORMES ON ("+conf.getString("SQL.TIPO_CAMPO_INF_MOD.campo")+"=" + conf.getString("SQL.PLANT_INF_CAB_I.tipo") +
                    " AND "+conf.getString("SQL.TIPO_CAMPO_INF_MOD.modulo")+"=1) " +
                    " WHERE " + conf.getString("SQL.PLANT_INF_CAB_I.plantilla") + "=" + plantilla +
                    " ORDER BY " + conf.getString("SQL.PLANT_INF_CAB_I.idCampo");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            Vector listaCabInf = new Vector();
            while (rs.next()) {
                CampoValueObject cabInf = new CampoValueObject();
                cabInf.setIdCampo(rs.getString(1));
                cabInf.setTitulo(rs.getString(2));
                cabInf.setPosx(rs.getString(3));
                cabInf.setPosy(rs.getString(4));
                listaCabInf.add(cabInf);
            }
            fiVO.setListaCabInf(listaCabInf);

            //Cabecera informe Disponibles
            Vector listaCabInfDisponibles = getListaCabInforme(params);
            for (int i=0; i<listaCabInf.size(); i++) {
                CampoValueObject cabInf = (CampoValueObject)listaCabInf.get(i);
                for (int j=0; j<listaCabInfDisponibles.size(); j++) {
                    CampoValueObject cabInfDisponible = (CampoValueObject)listaCabInfDisponibles.get(j);
                    if (cabInf.getIdCampo().equals(cabInfDisponible.getIdCampo())) {
                        m_Log.debug("CAMPO : "+cabInf.getIdCampo()+" ; "+cabInf.getTitulo());
                        listaCabInfDisponibles.remove(cabInfDisponible);
                        break;
                    }
                }
            }
            fiVO.setListaCabInfDisponibles(listaCabInfDisponibles);

            //Pie informe seleccionados
            sql = "SELECT " + conf.getString("SQL.PLANT_INF_PIE_I.tipo") + ", " + conf.getString("SQL.TIPO_CAMPOS_INFORMES.titulo") +
                    " FROM PLANT_INF_PIE_I " +
                    " INNER JOIN TIPO_CAMPOS_INFORMES ON (" + conf.getString("SQL.PLANT_INF_PIE_I.tipo") + " = " + conf.getString("SQL.TIPO_CAMPOS_INFORMES.id") + ")" +
                    " INNER JOIN TIPO_CAMPOS_MODULOS_INFORMES ON ("+conf.getString("SQL.TIPO_CAMPO_INF_MOD.campo")+"=" + conf.getString("SQL.PLANT_INF_PIE_I.tipo") +
                    " AND "+conf.getString("SQL.TIPO_CAMPO_INF_MOD.modulo")+"=2) " +
                    " WHERE " + conf.getString("SQL.PLANT_INF_PIE_I.plantilla") + "=" + plantilla + " " +
                    " ORDER BY " + conf.getString("SQL.PLANT_INF_PIE_I.idCampo");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            Vector listaPieInf = new Vector();
            while (rs.next()) {
                CampoValueObject pieInf = new CampoValueObject();
                pieInf.setIdCampo(rs.getString(1));
                pieInf.setTitulo(rs.getString(2));
                listaPieInf.add(pieInf);
            }
            fiVO.setListaPieInf(listaPieInf);

            //Pie informe Disponibles
            Vector listaPieInfDisponibles = getListaPieInforme(params);
            for (int i=0; i<listaPieInf.size(); i++) {
                CampoValueObject pieInf = (CampoValueObject)listaPieInf.get(i);
                for (int j=0; j<listaPieInfDisponibles.size(); j++) {
                    CampoValueObject pieInfDisponible = (CampoValueObject)listaPieInfDisponibles.get(j);
                    if (pieInf.getIdCampo().equals(pieInfDisponible.getIdCampo())) {
                        m_Log.debug("CAMPO : "+pieInf.getIdCampo()+" ; "+pieInf.getTitulo());
                        listaPieInfDisponibles.remove(pieInfDisponible);
                        break;
                    }
                }
            }
            fiVO.setListaPieInfDisponibles(listaPieInfDisponibles);

            //Cabecera pagina seleccionados
            sql = "SELECT " + conf.getString("SQL.PLANT_INF_CAB_P.tipo") + ", " + conf.getString("SQL.TIPO_CAMPOS_INFORMES.titulo") +
                    "," + conf.getString("SQL.PLANT_INF_CAB_P.posx") +"," + conf.getString("SQL.PLANT_INF_CAB_P.posy") +
                    " FROM PLANT_INF_CAB_P " +
                    " INNER JOIN TIPO_CAMPOS_INFORMES ON (" + conf.getString("SQL.PLANT_INF_CAB_P.tipo") + " = " + conf.getString("SQL.TIPO_CAMPOS_INFORMES.id") + ")" +
                    " INNER JOIN TIPO_CAMPOS_MODULOS_INFORMES ON ("+conf.getString("SQL.TIPO_CAMPO_INF_MOD.campo")+"=" + conf.getString("SQL.PLANT_INF_CAB_P.tipo") +
                    " AND "+conf.getString("SQL.TIPO_CAMPO_INF_MOD.modulo")+"=3) " +
                    " WHERE " + conf.getString("SQL.PLANT_INF_CAB_P.plantilla") + "=" + plantilla + " " +
                    " ORDER BY " + conf.getString("SQL.PLANT_INF_CAB_P.idCampo");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            Vector listaCabPag = new Vector();
            while (rs.next()) {
                CampoValueObject cabPag = new CampoValueObject();
                cabPag.setIdCampo(rs.getString(1));
                cabPag.setTitulo(rs.getString(2));
                cabPag.setPosx(rs.getString(3));
                cabPag.setPosy(rs.getString(4));
                listaCabPag.add(cabPag);
            }
            fiVO.setListaCabPag(listaCabPag);

            //Cabecera pagina Disponibles
            Vector listaCabPagDisponibles = getListaCabPagina(params);
            for (int i=0; i<listaCabPag.size(); i++) {
                CampoValueObject cabPag = (CampoValueObject)listaCabPag.get(i);
                for (int j=0; j<listaCabPagDisponibles.size(); j++) {
                    CampoValueObject cabPagDisponible = (CampoValueObject)listaCabPagDisponibles.get(j);
                    if (cabPag.getIdCampo().equals(cabPagDisponible.getIdCampo())) {
                        m_Log.debug("CAMPO : "+cabPag.getIdCampo()+" ; "+cabPag.getTitulo());
                        listaCabPagDisponibles.remove(cabPagDisponible);
                        break;
                    }
                }
            }
            fiVO.setListaCabPagDisponibles(listaCabPagDisponibles);

            //Pie pagina seleccionados
            sql = "SELECT " + conf.getString("SQL.PLANT_INF_PIE_P.tipo") + ", " + conf.getString("SQL.TIPO_CAMPOS_INFORMES.titulo") +
                    " FROM PLANT_INF_PIE_P " +
                    " INNER JOIN TIPO_CAMPOS_INFORMES ON (" + conf.getString("SQL.PLANT_INF_PIE_P.tipo") + " = " + conf.getString("SQL.TIPO_CAMPOS_INFORMES.id") + ")" +
                    " INNER JOIN TIPO_CAMPOS_MODULOS_INFORMES ON ("+conf.getString("SQL.TIPO_CAMPO_INF_MOD.campo")+"=" + conf.getString("SQL.PLANT_INF_PIE_P.tipo") +
                    " AND "+conf.getString("SQL.TIPO_CAMPO_INF_MOD.modulo")+"=4) " +
                    " WHERE " + conf.getString("SQL.PLANT_INF_PIE_P.plantilla") + "=" + plantilla + " " +
                    " ORDER BY " + conf.getString("SQL.PLANT_INF_PIE_P.idCampo");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            Vector listaPiePag = new Vector();
            while (rs.next()) {
                CampoValueObject piePag = new CampoValueObject();
                piePag.setIdCampo(rs.getString(1));
                piePag.setTitulo(rs.getString(2));
                listaPiePag.add(piePag);
            }
            fiVO.setListaPiePag(listaPiePag);

            //Pie pagina Disponibles
            Vector listaPiePagDisponibles = getListaPiePagina(params);
            for (int i=0; i<listaPiePag.size(); i++) {
                CampoValueObject piePag = (CampoValueObject)listaPiePag.get(i);
                for (int j=0; j<listaPiePagDisponibles.size(); j++) {
                    CampoValueObject piePagDisponible = (CampoValueObject)listaPiePagDisponibles.get(j);
                    if (piePag.getIdCampo().equals(piePagDisponible.getIdCampo())) {
                        m_Log.debug("CAMPO : "+piePag.getIdCampo()+" ; "+piePag.getTitulo());
                        listaPiePagDisponibles.remove(piePagDisponible);
                        break;
                    }
                }
            }
            fiVO.setListaPiePagDisponibles(listaPiePagDisponibles);


            //UORs seleccionadas
            sql = "SELECT " + conf.getString("SQL.PLANT_INF_UOR.id") + ", " + conf.getString("SQL.A_UOR.nombre") +
                    " FROM PLANT_INF_UOR " +
                    " INNER JOIN A_UOR ON ("+conf.getString("SQL.PLANT_INF_UOR.id")+"=" + conf.getString("SQL.A_UOR.codigo") + ")" +
                    " WHERE " + conf.getString("SQL.PLANT_INF_UOR.plantilla") + "=" + plantilla + " " +
                    " ORDER BY " + conf.getString("SQL.A_UOR.nombre");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            Vector listaUOR = new Vector();
            while (rs.next()) {
                CampoValueObject uor = new CampoValueObject();
                uor.setIdCampo(rs.getString(1));
                uor.setTitulo(rs.getString(2));
                listaUOR.add(uor);
            }
            fiVO.setListaUOR(listaUOR);

            //Pie pagina Disponibles
            Vector listaUORDisponibles = getListaUOR(params);
            for (int i=0; i<listaUOR.size(); i++) {
                CampoValueObject UOR = (CampoValueObject)listaUOR.get(i);
                for (int j=0; j<listaUORDisponibles.size(); j++) {
                    CampoValueObject UORDisponible = (CampoValueObject)listaUORDisponibles.get(j);
                    if (UOR.getIdCampo().equals(UORDisponible.getIdCampo())) {
                        m_Log.debug("CAMPO : "+UOR.getIdCampo()+" ; "+UOR.getTitulo());
                        listaUORDisponibles.remove(UORDisponible);
                        break;
                    }
                }
            }
            fiVO.setListaUORDisponibles(listaUORDisponibles);
        } catch (InstanciacionDatosInformesException ibte) {
            m_Log.error("NO SE HA PODIDO INSTANCIAR LA CLASE QUE IMPLEMENTA EL ÁMBITO " + fiVO.getDescAmbito());
        } catch (SQLException sqle) {
            m_Log.error("Error de SQL en altaInforme: " + sqle);
            resul=-1;
        } catch (BDException bde) {
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo altaInforme: " + bde);
            resul=-1;
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }
            }
        }
        return fiVO;
    }

    public Vector getListaCabInforme(String[] params) {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql = "select "+conf.getString("SQL.TIPO_CAMPOS_INFORMES.id")+","+conf.getString("SQL.TIPO_CAMPOS_INFORMES.titulo")+" " +
                    "from TIPO_CAMPOS_MODULOS_INFORMES " +
                    "INNER JOIN TIPO_CAMPOS_INFORMES ON ("+conf.getString("SQL.TIPO_CAMPOS_INFORMES.id")+"="+conf.getString("SQL.TIPO_CAMPO_INF_MOD.campo")+") " +
                    "WHERE " + conf.getString("SQL.TIPO_CAMPO_INF_MOD.modulo") + "= 1";
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                CampoValueObject campoVO = new CampoValueObject();
                campoVO.setIdCampo(rs.getString(1));
                campoVO.setTitulo(rs.getString(2));
                r.add(campoVO);
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqle) {
            m_Log.error("Error de SQL en getListaCampos: " + sqle);
            sqle.printStackTrace();
        } catch (BDException bde) {
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo getListaCampos: " + bde);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }
            }
        }
        return r;
    }

    public Vector getListaPieInforme(String[] params) {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql = "select "+conf.getString("SQL.TIPO_CAMPOS_INFORMES.id")+","+conf.getString("SQL.TIPO_CAMPOS_INFORMES.titulo")+" " +
                    "from TIPO_CAMPOS_MODULOS_INFORMES " +
                    "INNER JOIN TIPO_CAMPOS_INFORMES ON ("+conf.getString("SQL.TIPO_CAMPOS_INFORMES.id")+"="+conf.getString("SQL.TIPO_CAMPO_INF_MOD.campo")+") " +
                    "WHERE " + conf.getString("SQL.TIPO_CAMPO_INF_MOD.modulo") + "= 2";
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                CampoValueObject campoVO = new CampoValueObject();
                campoVO.setIdCampo(rs.getString(1));
                campoVO.setTitulo(rs.getString(2));
                r.add(campoVO);
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqle) {
            m_Log.error("Error de SQL en getListaCampos: " + sqle);
            sqle.printStackTrace();
        } catch (BDException bde) {
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo getListaCampos: " + bde);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }
            }
        }
        return r;
    }

    public Vector getListaCabPagina(String[] params) {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql = "select "+conf.getString("SQL.TIPO_CAMPOS_INFORMES.id")+","+conf.getString("SQL.TIPO_CAMPOS_INFORMES.titulo")+" " +
                    "from TIPO_CAMPOS_MODULOS_INFORMES " +
                    "INNER JOIN TIPO_CAMPOS_INFORMES ON ("+conf.getString("SQL.TIPO_CAMPOS_INFORMES.id")+"="+conf.getString("SQL.TIPO_CAMPO_INF_MOD.campo")+") " +
                    "WHERE " + conf.getString("SQL.TIPO_CAMPO_INF_MOD.modulo") + "= 3";
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                CampoValueObject campoVO = new CampoValueObject();
                campoVO.setIdCampo(rs.getString(1));
                campoVO.setTitulo(rs.getString(2));
                r.add(campoVO);
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqle) {
            m_Log.error("Error de SQL en getListaCampos: " + sqle);
            sqle.printStackTrace();
        } catch (BDException bde) {
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo getListaCampos: " + bde);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }
            }
        }
        return r;
    }

    public Vector getListaPiePagina(String[] params) {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql = "select "+conf.getString("SQL.TIPO_CAMPOS_INFORMES.id")+","+conf.getString("SQL.TIPO_CAMPOS_INFORMES.titulo")+" " +
                    "from TIPO_CAMPOS_MODULOS_INFORMES " +
                    "INNER JOIN TIPO_CAMPOS_INFORMES ON ("+conf.getString("SQL.TIPO_CAMPOS_INFORMES.id")+"="+conf.getString("SQL.TIPO_CAMPO_INF_MOD.campo")+") " +
                    "WHERE " + conf.getString("SQL.TIPO_CAMPO_INF_MOD.modulo") + "= 4";
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                CampoValueObject campoVO = new CampoValueObject();
                campoVO.setIdCampo(rs.getString(1));
                campoVO.setTitulo(rs.getString(2));
                r.add(campoVO);
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqle) {
            m_Log.error("Error de SQL en getListaCampos: " + sqle);
            sqle.printStackTrace();
        } catch (BDException bde) {
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo getListaCampos: " + bde);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }
            }
        }
        return r;
    }

    public Vector getListaUOR(String[] params) {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql = "select "+conf.getString("SQL.A_UOR.codigo")+","+conf.getString("SQL.A_UOR.nombre")+" " +
                    "from A_UOR " +
                    "order by 2";
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                CampoValueObject uor = new CampoValueObject();
                uor.setIdCampo(rs.getString(1));
                uor.setTitulo(rs.getString(2));
                r.add(uor);
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqle) {
            m_Log.error("Error de SQL en getListaUOR: " + sqle);
            sqle.printStackTrace();
        } catch (BDException bde) {
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo getListaUOR: " + bde);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }
            }
        }
        return r;
    }


    public Vector getListaCriterios(String plantilla, String[] params) {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql = "SELECT " + conf.getString("SQL.PLANT_INF_CRI.id") + "," + conf.getString("SQL.PLANT_INF_CRI.campo") + ", " +
                 conf.getString("SQL.PLANT_INF_CRI.condicion") + "," + conf.getString("SQL.PLANT_INF_CRI.valor1") + "," +
                 conf.getString("SQL.PLANT_INF_CRI.valor2") + "," + conf.getString("SQL.PLANT_INF_CRI.titulo") + "," +
                 conf.getString("SQL.PLANT_INF_CRI.origen") + "," + conf.getString("SQL.PLANT_INF_CRI.tabla") + "," +
                 conf.getString("SQL.INF_CAMPOS.tip") + 
                    " FROM PLANT_INF_CRI " +
                    " INNER JOIN INF_CAMPOS ON ( " + conf.getString("SQL.PLANT_INF_CRI.campo") + " = " + conf.getString("SQL.INF_CAMPOS.cod") + ")" +
                    " WHERE " + conf.getString("SQL.PLANT_INF_CRI.plantilla") + "=" + plantilla;
          
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql);
            int contador=0;
            while(rs.next()) {
                contador++;
                CriteriosValueObject criteriosVO;
                criteriosVO = new CriteriosValueObject();
                criteriosVO.setCodPlantilla(plantilla);
                criteriosVO.setId(rs.getString(1));
                criteriosVO.setCampo(rs.getString(2));
                criteriosVO.setCondicion(rs.getString(3)!=null?rs.getString(3):"");
                criteriosVO.setValor1(rs.getString(4)!=null?rs.getString(4):"");
                criteriosVO.setValor2(rs.getString(5)!=null?rs.getString(5):"");
                criteriosVO.setTitulo(rs.getString(6)!=null?rs.getString(6):"");
                criteriosVO.setOrigen(rs.getString(7)!=null?rs.getString(7):"");
                criteriosVO.setTabla(rs.getString(8)!=null?rs.getString(8):"");
                criteriosVO.setTipo(rs.getString(9)!=null?rs.getString(9):"");
                r.add(criteriosVO);
            }

            /** SE RECUPERAN EL RESTO DE CRITERIOS DE BÚSQUEDA DE LA SOLICITUD Y QUE NO SE CORRESPONDAN CON LOS RECUPERADOS ANTERIORMENTE */
            StringBuffer notInCampo = new StringBuffer();
            StringBuffer notInId = new StringBuffer();
            if(r.size()>0){
                notInId.append(" AND PLANT_INF_CRI_ID NOT IN(");
                notInCampo.append(" AND PLANT_INF_CRI_CAMPO NOT IN(");
                for(int z=0;z<r.size();z++)
                {
                    notInCampo.append(((CriteriosValueObject)r.get(z)).getCampo());
                    notInId.append(((CriteriosValueObject)r.get(z)).getId());
                    if((r.size()-z)>1){
                        notInCampo.append(",");
                        notInId.append(",");
                    }                
                }
                notInId.append(")");
                notInCampo.append(")");
            }// if


                     
                // No se han recuperado criterios al hacer el join entre la tabla PLANT_INF_CRI e INF_CAMPOS
                sql = "SELECT " + conf.getString("SQL.PLANT_INF_CRI.id") + "," + conf.getString("SQL.PLANT_INF_CRI.campo") + ", " +
                 conf.getString("SQL.PLANT_INF_CRI.condicion") + "," + conf.getString("SQL.PLANT_INF_CRI.valor1") + "," +
                 conf.getString("SQL.PLANT_INF_CRI.valor2") + "," + conf.getString("SQL.PLANT_INF_CRI.titulo") + "," +
             conf.getString("SQL.PLANT_INF_CRI.origen") + "," + conf.getString("SQL.PLANT_INF_CRI.tabla") + ",NULL" +
                    " FROM PLANT_INF_CRI " +                    
                    " WHERE " + conf.getString("SQL.PLANT_INF_CRI.plantilla") + "=" + plantilla;
            if(notInCampo.length()>0)
                sql += notInCampo.toString();

            if(notInId.length()>0)
                sql += notInId.toString();

                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs = stmt.executeQuery(sql);
                while(rs.next()) {
                    contador++;
                    CriteriosValueObject criteriosVO;
                    criteriosVO = new CriteriosValueObject();
                    criteriosVO.setCodPlantilla(plantilla);
                    criteriosVO.setId(rs.getString(1));
                    criteriosVO.setCampo(rs.getString(2));
                    criteriosVO.setCondicion(rs.getString(3)!=null?rs.getString(3):"");
                    criteriosVO.setValor1(rs.getString(4)!=null?rs.getString(4):"");
                    criteriosVO.setValor2(rs.getString(5)!=null?rs.getString(5):"");
                    criteriosVO.setTitulo(rs.getString(6)!=null?rs.getString(6):"");
                    criteriosVO.setOrigen(rs.getString(7)!=null?rs.getString(7):"");
                    criteriosVO.setTabla(rs.getString(8)!=null?rs.getString(8):"");
                    criteriosVO.setTipo(rs.getString(9)!=null?rs.getString(9):"");
                    r.add(criteriosVO);
                }
           

            rs.close();
            stmt.close();
        } catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error("Error en el metodo getListaCriterios: " + e);
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }
            }
        }
        return r;
    }

    public boolean yaExiste(String nombrePlantilla, String[] params) {
        boolean resultado = false;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql;
            sql = "select " + conf.getString("SQL.PLANT_INFORMES.titulo") + " from plant_informes where "+ conf.getString("SQL.PLANT_INFORMES.titulo") + "='" +
                    nombrePlantilla + "'";
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql);
            resultado = rs.next();
            rs.close();
            stmt.close();
        } catch (Exception e) {
            m_Log.error("Error en yaExiste: ");
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(Exception e) {
                    m_Log.error("No se pudo devolver la conexion: ");
                    e.printStackTrace();
                }
            }
        }
        return resultado;
    }

    public boolean hayDatosEnCuerpoInforme(String plantilla, String[] params) {
        boolean resultado = false;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql;
            sql = "select " + plant_inf_col_plantilla + " from plant_inf_col where " + plant_inf_col_plantilla + "=" + plantilla;
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql);
            resultado = rs.next();
            rs.close();
            stmt.close();
        } catch (Exception e) {
            m_Log.error("Error en hayDatosEnCuerpoInforme: ");
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(Exception e) {
                    m_Log.error("No se pudo devolver la conexion: ");
                    e.printStackTrace();
                }
            }
        }
        return resultado;
    }

    public Vector getListaCamposSeleccionadosInforme(String codPlantilla, String[] params) throws Exception{
        Vector listaCamposSeleccionados = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql;
        Statement stmt;
        ResultSet rs;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            sql ="SELECT " + plant_inf_col_idCampo + "," +
                    plant_inf_col_titulo + ", " + plant_inf_col_origen + ", " +
                    plant_inf_col_posx + ", "  + plant_inf_col_posy + ", " +
                    plant_inf_col_align + ", " + plant_inf_col_tabla  + ", " +
                    plant_inf_col_ancho + ", "  + plant_inf_col_elipsis +","  + plant_inf_col_ord +
                    " FROM PLANT_INF_COL " +
                    " WHERE " + plant_inf_col_plantilla +" = " + codPlantilla + " ORDER BY 1";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                CampoValueObject campo = new CampoValueObject();
                campo.setCodPlantilla(codPlantilla);
                campo.setTitulo(rs.getString(2));
                campo.setOrigen(rs.getString(3));
                campo.setPosx(rs.getString(4));
                campo.setPosy(rs.getString(5));
                campo.setAlign(rs.getString(6));
                campo.setTabla(rs.getString(7));
                campo.setAncho(rs.getString(8));
                campo.setElipsis(rs.getString(9));
                campo.setOrden(rs.getString(10));
                listaCamposSeleccionados.add(campo);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            m_Log.error("Error de SQL en getListaCamposSeleccionadosInforme: ");
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(Exception e) {
                    m_Log.error("No se pudo devolver la conexion: ");
                    e.printStackTrace();
                }
            }
        }
        return listaCamposSeleccionados;
    }

    public Vector getListaPermisosSeleccionadosInforme(String codPlantilla, String[] params) throws Exception{
        Vector listaPermisosSeleccionados = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        String sql;
        Statement stmt;
        ResultSet rs;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            sql ="SELECT UOR_COD_VIS FROM PLANT_INF_UOR INNER JOIN A_UOR ON (PLANT_INF_UOR_ID=UOR_COD)" +
                    " WHERE PLANT_INF_UOR_PLANTILLA = " + codPlantilla +" ORDER BY UOR_COD_VIS";
            if(m_Log.isDebugEnabled()) m_Log.debug("get lista permisos dao"+sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {                
                listaPermisosSeleccionados.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            m_Log.error("Error de SQL en getListaPermisosSeleccionadosInforme: ");
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(Exception e) {
                    m_Log.error("No se pudo devolver la conexion: ");
                    e.printStackTrace();
                }
            }
        }
        return listaPermisosSeleccionados;
    }

/*
* ENTRADA:  plantilla (codigo de la plantilla)
*           params (datos de conexión)
*
* SALIDA:   Vector con los títulos de los campos asociados a la plantilla
*
*/
    public Vector getCamposOrdenadosInforme(String plantilla, String[] params) throws Exception {
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql = "select " + plant_inf_col_titulo + " from PLANT_INF_COL where " + plant_inf_col_plantilla + " = " + plantilla + " order by " + plant_inf_col_idCampo;
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                r.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            m_Log.error("Error de SQL en getCamposOrdenadosInforme: " + e);
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }
            }
        }
        return r;
    }

    public InformeValueObject getInforme(String[] params,String codPlantilla) throws Exception{
     AdaptadorSQLBD abd = null;
     Connection conexion = null;
     InformeValueObject informeVO = new InformeValueObject();

     try{
        abd = new AdaptadorSQLBD(params);
        conexion = abd.getConnection();
        CabInformeValueObject cabInformeVO;
        CabPaginaInformeValueObject cabPaginaInformeVO;
        CuerpoInformeValueObject cuerpoInformeVO;
        PiePaginaInformeValueObject piePaginaInformeVO;
        PieInformeValueObject pieInformeVO;
        Vector vectorCabInformeVO = new Vector();
        Vector vectorCabPaginaInformeVO = new Vector();
        Vector vectorCuerpoInformeVO = new Vector();
        Vector vectorPiePaginaInformeVO = new Vector();
        Vector vectorPieInformeVO = new Vector();

        String sql = "SELECT PLANT_PAPEL ,PLANT_TITULO,PLANT_PROCED," +
                "PLANT_ORIENTACION, PLANT_MARGEN_SUP, PLANT_MARGEN_INF, PLANT_MARGEN_IZQ, PLANT_MARGEN_DER, PLANT_ORIGEN" +
            " FROM PLANT_INFORMES WHERE " + conf.getString("SQL.PLANT_INFORMES.cod") + "=" + codPlantilla;
        if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            informeVO.setCodPlantilla(codPlantilla);
            int i = 1;
            informeVO.setPapel(rs.getString(i++));
            informeVO.setTitulo(rs.getString(i++));
            informeVO.setProcedimiento(rs.getString(i++));
            informeVO.setOrientacion(rs.getString(i++));
            informeVO.setMargenSuperior(rs.getString(i++));
            informeVO.setMargenInferior(rs.getString(i++));
            informeVO.setMargenIzquierdo(rs.getString(i++));
            informeVO.setMargenDerecho(rs.getString(i++));
            informeVO.setAmbito(rs.getString(i++));
        }
        rs.close();
        stmt.close();

         sql = "SELECT " + conf.getString("SQL.PLANT_INF_CAB_I.idCampo") + "," +
                  conf.getString("SQL.PLANT_INF_CAB_I.posx") + "," + conf.getString("SQL.PLANT_INF_CAB_I.posy") + "," +
                  conf.getString("SQL.PLANT_INF_CAB_I.tipo") + "," + conf.getString("SQL.PLANT_INF_CAB_I.informacion") +
                  " FROM PLANT_INF_CAB_I WHERE " + conf.getString("SQL.PLANT_INF_CAB_I.plantilla") + "=" + codPlantilla;
          if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
          stmt = conexion.createStatement();
          rs = stmt.executeQuery(sql);
          while(rs.next()){
              cabInformeVO = new CabInformeValueObject();
              cabInformeVO.setCodPlantilla(codPlantilla);
              cabInformeVO.setIdCampo(rs.getString(1));
              cabInformeVO.setPosX(rs.getString(2));
              cabInformeVO.setPosY(rs.getString(3));
              cabInformeVO.setTipo(rs.getString(4));
              cabInformeVO.setInformacion(rs.getString(5));
              vectorCabInformeVO.addElement(cabInformeVO);
          }
          rs.close();
          stmt.close();

         sql = "SELECT " + conf.getString("SQL.PLANT_INF_CAB_P.idCampo") + "," +
                 conf.getString("SQL.PLANT_INF_CAB_P.posx") + "," + conf.getString("SQL.PLANT_INF_CAB_P.posy") + "," +
                 conf.getString("SQL.PLANT_INF_CAB_P.tipo") + "," + conf.getString("SQL.PLANT_INF_CAB_P.informacion") +
                 " FROM PLANT_INF_CAB_P WHERE " + conf.getString("SQL.PLANT_INF_CAB_P.plantilla") + "=" + codPlantilla;
         if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
         stmt = conexion.createStatement();
         rs = stmt.executeQuery(sql);
         while(rs.next()){
             cabPaginaInformeVO = new CabPaginaInformeValueObject();
             cabPaginaInformeVO.setCodPlantilla(codPlantilla);
             cabPaginaInformeVO.setIdCampo(rs.getString(1));
             cabPaginaInformeVO.setPosX(rs.getString(2));
             cabPaginaInformeVO.setPosY(rs.getString(3));
             cabPaginaInformeVO.setTipo(rs.getString(4));
             cabPaginaInformeVO.setInformacion(rs.getString(5));
             vectorCabPaginaInformeVO.addElement(cabPaginaInformeVO);
         }
         rs.close();
         stmt.close();

         sql = "SELECT " + plant_inf_col_titulo + "," +
                 plant_inf_col_posx + "," + plant_inf_col_posy + "," +
                 plant_inf_col_origen + "," + plant_inf_col_align + "," +
                 plant_inf_col_tabla + "," + plant_inf_col_ancho + "," +
                 plant_inf_col_elipsis + "," + plant_inf_col_idCampo +"," + plant_inf_col_ord +
                 " FROM PLANT_INF_COL WHERE " + plant_inf_col_plantilla + "=" + codPlantilla +
                 " ORDER BY " + plant_inf_col_idCampo;
         if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
         stmt = conexion.createStatement();
         rs = stmt.executeQuery(sql);
         while(rs.next()){
             cuerpoInformeVO = new CuerpoInformeValueObject();
             cuerpoInformeVO.setCodPlantilla(codPlantilla);
             cuerpoInformeVO.setTitulo(rs.getString(1));
             cuerpoInformeVO.setPosX(rs.getString(2));
             cuerpoInformeVO.setPosY(rs.getString(3));
             cuerpoInformeVO.setOrigen(rs.getString(4));
             cuerpoInformeVO.setAlign(rs.getString(5));
             cuerpoInformeVO.setTabla(rs.getString(6));
             cuerpoInformeVO.setAncho(rs.getString(7));
             cuerpoInformeVO.setElipsis(rs.getString(8).equals("1"));
             cuerpoInformeVO.setIdCampo(rs.getString(9));
             cuerpoInformeVO.setOrden(rs.getString(10));
             vectorCuerpoInformeVO.addElement(cuerpoInformeVO);
         }
         rs.close();
         stmt.close();

         sql = "SELECT " + conf.getString("SQL.PLANT_INF_PIE_P.idCampo") + "," +
               conf.getString("SQL.PLANT_INF_PIE_P.tipo") + "," +
               conf.getString("SQL.PLANT_INF_PIE_P.informacion") +
               " FROM PLANT_INF_PIE_P WHERE " + conf.getString("SQL.PLANT_INF_PIE_P.plantilla") + "=" + codPlantilla;
         if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
         stmt = conexion.createStatement();
         rs = stmt.executeQuery(sql);
         while(rs.next()){
             piePaginaInformeVO = new PiePaginaInformeValueObject();
             piePaginaInformeVO.setCodPlantilla(codPlantilla);
             piePaginaInformeVO.setIdCampo(rs.getString(1));
             piePaginaInformeVO.setTipo(rs.getString(2));
             piePaginaInformeVO.setInformacion(rs.getString(3));
             vectorPiePaginaInformeVO.addElement(piePaginaInformeVO);
         }
         rs.close();
         stmt.close();

         sql = "SELECT " + conf.getString("SQL.PLANT_INF_PIE_I.idCampo") + "," +
               conf.getString("SQL.PLANT_INF_PIE_I.tipo") + "," +
               conf.getString("SQL.PLANT_INF_PIE_I.informacion") +
               " FROM PLANT_INF_PIE_I WHERE " + conf.getString("SQL.PLANT_INF_PIE_I.plantilla") + "=" + codPlantilla;
         if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
         stmt = conexion.createStatement();
         rs = stmt.executeQuery(sql);
         while(rs.next()){
             pieInformeVO = new PieInformeValueObject();
             pieInformeVO.setCodPlantilla(codPlantilla);
             pieInformeVO.setIdCampo(rs.getString(1));
             pieInformeVO.setTipo(rs.getString(2));
             pieInformeVO.setInformacion(rs.getString(3));
             vectorPieInformeVO.addElement(pieInformeVO);
         }
         rs.close();
         stmt.close();

         CriteriosValueObject criteriosVO;
         Vector criterios = new Vector();

         sql = "SELECT " + conf.getString("SQL.PLANT_INF_CRI.id") + "," +
                 conf.getString("SQL.PLANT_INF_CRI.campo") + "," +
                 conf.getString("SQL.PLANT_INF_CRI.condicion") + "," +
                 conf.getString("SQL.PLANT_INF_CRI.valor1") + "," +
                 conf.getString("SQL.PLANT_INF_CRI.valor2") + "," +
                 conf.getString("SQL.PLANT_INF_CRI.titulo") + "," +
                 conf.getString("SQL.PLANT_INF_CRI.origen") + "," +
                 conf.getString("SQL.PLANT_INF_CRI.tabla") +
               " FROM PLANT_INF_CRI WHERE " + conf.getString("SQL.PLANT_INF_CRI.plantilla") + "=" + codPlantilla;
         if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
         stmt = conexion.createStatement();
         rs = stmt.executeQuery(sql);
         while(rs.next()){
             criteriosVO = new CriteriosValueObject();
             criteriosVO.setCodPlantilla(codPlantilla);
             criteriosVO.setId(rs.getString(1));
             criteriosVO.setCampo(rs.getString(2));
             criteriosVO.setCondicion(rs.getString(3));
             criteriosVO.setValor1(rs.getString(4));
             criteriosVO.setValor2(rs.getString(5));
             criteriosVO.setTitulo(rs.getString(6));
             criteriosVO.setOrigen(rs.getString(7));
             criteriosVO.setTabla(rs.getString(8));
             criterios.addElement(criteriosVO);
         }
         rs.close();
         stmt.close();

         Vector listaPermisos = new Vector();
         sql = "SELECT UOR_COD_VIS FROM PLANT_INF_UOR JOIN A_UOR ON (PLANT_INF_UOR_ID=UOR_COD) " +
                 "WHERE PLANT_INF_UOR_PLANTILLA=" + codPlantilla;
         if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
         stmt = conexion.createStatement();
         rs = stmt.executeQuery(sql);
         while(rs.next()){
             listaPermisos.add(rs.getString(1));
         }
         rs.close();
         stmt.close();

         informeVO.setCriterios(criterios);
         informeVO.setVectorCabInformeVO(vectorCabInformeVO);
         informeVO.setVectorCabPaginaInformeVO(vectorCabPaginaInformeVO);
         informeVO.setVectorCuerpoInformeVO(vectorCuerpoInformeVO);
         informeVO.setVectorPiePaginaInformeVO(vectorPiePaginaInformeVO);
         informeVO.setVectorPieInformeVO(vectorPieInformeVO);
         informeVO.setVectorPermisos(listaPermisos);
     }
     catch (SQLException sqle){
         m_Log.error("Error de SQL en getInforme: ");
         sqle.printStackTrace();
         throw sqle;
     }
     catch (BDException bde){
         m_Log.error("Error del OAD en el metodo getInforme: ");
         bde.printStackTrace();
         throw bde;
     }
     finally {
        if (conexion != null){
         try{
            abd.devolverConexion(conexion);
         }
         catch(BDException bde){
             m_Log.error("No se pudo devolver la conexion: ");
             bde.printStackTrace();
             throw bde;
         }
        }
        m_Log.debug("INFORME VALUE OBJECT DEVUELTO: "+informeVO);
     }
    return informeVO;
    }

    public InformeValueObject getEstructuraInforme(String[] params,String codPlantilla, String codSolicitud){
     AdaptadorSQLBD abd = null;
     Connection conexion = null;
     InformeValueObject informeVO = new InformeValueObject();
     
     m_Log.debug("Entrando en FichaInformeDAO.getEstructuraInforme");
     try{
        abd = new AdaptadorSQLBD(params);
        conexion = abd.getConnection();
        CabInformeValueObject cabInformeVO;
        CabPaginaInformeValueObject cabPaginaInformeVO;
        CuerpoInformeValueObject cuerpoInformeVO;
        PiePaginaInformeValueObject piePaginaInformeVO;
        PieInformeValueObject pieInformeVO;
        Vector vectorCabInformeVO = new Vector();
        Vector vectorCabPaginaInformeVO = new Vector();
        Vector vectorCuerpoInformeVO = new Vector();
        Vector vectorPiePaginaInformeVO = new Vector();
        Vector vectorPieInformeVO = new Vector();

        String sql = "SELECT " + conf.getString("SQL.PLANT_INFORMES.papel") + "," +
            conf.getString("SQL.PLANT_INFORMES.titulo") + "," +
            conf.getString("SQL.PLANT_INFORMES.orientacion") + "," +
            conf.getString("SQL.PLANT_INFORMES.margenSup") + "," +
            conf.getString("SQL.PLANT_INFORMES.margenInf") + "," +
            conf.getString("SQL.PLANT_INFORMES.margenIzq") + "," +
            conf.getString("SQL.PLANT_INFORMES.margenDer") +
            " FROM PLANT_INFORMES WHERE " + conf.getString("SQL.PLANT_INFORMES.cod") + "=" + codPlantilla;
        if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            informeVO.setCodPlantilla(codPlantilla);
            informeVO.setPapel(rs.getString(1));
            informeVO.setTitulo(rs.getString(2));
            informeVO.setOrientacion(rs.getString(3));
            informeVO.setMargenSuperior(rs.getString(4));
            informeVO.setMargenInferior(rs.getString(5));
            informeVO.setMargenIzquierdo(rs.getString(6));
            informeVO.setMargenDerecho(rs.getString(7));
        }
        rs.close();
        stmt.close();

         sql = "SELECT " + conf.getString("SQL.PLANT_INF_CAB_I.idCampo") + "," +
                  conf.getString("SQL.PLANT_INF_CAB_I.posx") + "," + conf.getString("SQL.PLANT_INF_CAB_I.posy") + "," +
                  conf.getString("SQL.PLANT_INF_CAB_I.tipo") + "," + conf.getString("SQL.PLANT_INF_CAB_I.informacion") +
                  " FROM PLANT_INF_CAB_I WHERE " + conf.getString("SQL.PLANT_INF_CAB_I.plantilla") + "=" + codPlantilla;
          if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
          stmt = conexion.createStatement();
          rs = stmt.executeQuery(sql);
          while(rs.next()){
              cabInformeVO = new CabInformeValueObject();
              cabInformeVO.setCodPlantilla(codPlantilla);
              cabInformeVO.setIdCampo(rs.getString(1));
              cabInformeVO.setPosX(rs.getString(2));
              cabInformeVO.setPosY(rs.getString(3));
              cabInformeVO.setTipo(rs.getString(4));
              cabInformeVO.setInformacion(rs.getString(5));
              vectorCabInformeVO.addElement(cabInformeVO);
          }
          rs.close();
          stmt.close();

         sql = "SELECT " + conf.getString("SQL.PLANT_INF_CAB_P.idCampo") + "," +
                 conf.getString("SQL.PLANT_INF_CAB_P.posx") + "," + conf.getString("SQL.PLANT_INF_CAB_P.posy") + "," +
                 conf.getString("SQL.PLANT_INF_CAB_P.tipo") + "," + conf.getString("SQL.PLANT_INF_CAB_P.informacion") +
                 " FROM PLANT_INF_CAB_P WHERE " + conf.getString("SQL.PLANT_INF_CAB_P.plantilla") + "=" + codPlantilla;
         if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
         stmt = conexion.createStatement();
         rs = stmt.executeQuery(sql);
         while(rs.next()){
             cabPaginaInformeVO = new CabPaginaInformeValueObject();
             cabPaginaInformeVO.setCodPlantilla(codPlantilla);
             cabPaginaInformeVO.setIdCampo(rs.getString(1));
             cabPaginaInformeVO.setPosX(rs.getString(2));
             cabPaginaInformeVO.setPosY(rs.getString(3));
             cabPaginaInformeVO.setTipo(rs.getString(4));
             cabPaginaInformeVO.setInformacion(rs.getString(5));
             vectorCabPaginaInformeVO.addElement(cabPaginaInformeVO);
         }
         rs.close();
         stmt.close();

         sql = "SELECT " + plant_inf_col_titulo + "," +
                 plant_inf_col_posx + "," + plant_inf_col_posy + "," +
                 plant_inf_col_origen + "," + plant_inf_col_align + "," +
                 plant_inf_col_tabla + "," + plant_inf_col_ancho + "," +
                 plant_inf_col_elipsis + "," + plant_inf_col_idCampo +"," + plant_inf_col_ord +
                 " FROM PLANT_INF_COL WHERE " + plant_inf_col_plantilla + "=" + codPlantilla +
                 " ORDER BY " + plant_inf_col_idCampo;
         if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
         stmt = conexion.createStatement();
         rs = stmt.executeQuery(sql);
         while(rs.next()){
             cuerpoInformeVO = new CuerpoInformeValueObject();
             cuerpoInformeVO.setCodPlantilla(codPlantilla);
             cuerpoInformeVO.setTitulo(rs.getString(1));
             cuerpoInformeVO.setPosX(rs.getString(2));
             cuerpoInformeVO.setPosY(rs.getString(3));
             cuerpoInformeVO.setOrigen(rs.getString(4));
             cuerpoInformeVO.setAlign(rs.getString(5));
             cuerpoInformeVO.setTabla(rs.getString(6));
             cuerpoInformeVO.setAncho(rs.getString(7));
             cuerpoInformeVO.setElipsis(rs.getString(8).equals("0"));
             cuerpoInformeVO.setIdCampo(rs.getString(9));
             cuerpoInformeVO.setOrden(rs.getString(10));
             vectorCuerpoInformeVO.addElement(cuerpoInformeVO);
         }
         rs.close();
         stmt.close();

         sql = "SELECT " + conf.getString("SQL.PLANT_INF_PIE_P.idCampo") + "," +
               conf.getString("SQL.PLANT_INF_PIE_P.tipo") + "," +
               conf.getString("SQL.PLANT_INF_PIE_P.informacion") +
               " FROM PLANT_INF_PIE_P WHERE " + conf.getString("SQL.PLANT_INF_PIE_P.plantilla") + "=" + codPlantilla;
         if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
         stmt = conexion.createStatement();
         rs = stmt.executeQuery(sql);
         while(rs.next()){
             piePaginaInformeVO = new PiePaginaInformeValueObject();
             piePaginaInformeVO.setCodPlantilla(codPlantilla);
             piePaginaInformeVO.setIdCampo(rs.getString(1));
             piePaginaInformeVO.setTipo(rs.getString(2));
             piePaginaInformeVO.setInformacion(rs.getString(3));
             vectorPiePaginaInformeVO.addElement(piePaginaInformeVO);
         }
         rs.close();
         stmt.close();

         sql = "SELECT " + conf.getString("SQL.PLANT_INF_PIE_I.idCampo") + "," +
               conf.getString("SQL.PLANT_INF_PIE_I.tipo") + "," +
               conf.getString("SQL.PLANT_INF_PIE_I.informacion") +
               " FROM PLANT_INF_PIE_I WHERE " + conf.getString("SQL.PLANT_INF_PIE_I.plantilla") + "=" + codPlantilla;
         if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
         stmt = conexion.createStatement();
         rs = stmt.executeQuery(sql);
         while(rs.next()){
             pieInformeVO = new PieInformeValueObject();
             pieInformeVO.setCodPlantilla(codPlantilla);
             pieInformeVO.setIdCampo(rs.getString(1));
             pieInformeVO.setTipo(rs.getString(2));
             pieInformeVO.setInformacion(rs.getString(3));
             vectorPieInformeVO.addElement(pieInformeVO);
         }
         rs.close();
         stmt.close();

         CriteriosSolicitudValueObject criteriosSolicitudVO;
         Vector criterios = new Vector();

         sql = "SELECT " + conf.getString("SQL.INF_CRITERIOS.criterio") + "," +
                 conf.getString("SQL.INF_CRITERIOS.tipo") + "," +
                 conf.getString("SQL.INF_CRITERIOS.condicion") + "," +
                 conf.getString("SQL.INF_CRITERIOS.valor1") + "," +
                 conf.getString("SQL.INF_CRITERIOS.valor2") +
               " FROM INF_CRITERIOS WHERE " + conf.getString("SQL.INF_CRITERIOS.solicitud") + "=" + codSolicitud;
         if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
         stmt = conexion.createStatement();
         rs = stmt.executeQuery(sql);
         while(rs.next()){
             criteriosSolicitudVO = new CriteriosSolicitudValueObject();
             criteriosSolicitudVO.setCodSolicitud(codSolicitud);
             criteriosSolicitudVO.setCodCriterio(rs.getString(1));
             criteriosSolicitudVO.setTipo(rs.getString(2));
             Statement stmt2 = conexion.createStatement();
             String sql2= " SELECT " + conf.getString("SQL.CONDICION_CRITERIOS_INFORMES.cod") + "," +
                 conf.getString("SQL.CONDICION_CRITERIOS_INFORMES.desc") +
                 " FROM CONDICION_CRITERIOS_INFORMES WHERE " + conf.getString("SQL.CONDICION_CRITERIOS_INFORMES.cod") +
                 "=" + rs.getString(3);
             if(m_Log.isDebugEnabled()) m_Log.debug(sql2);
             ResultSet rs2 = stmt2.executeQuery(sql2);
             while (rs2.next()) {
                 criteriosSolicitudVO.setCondicion(rs2.getString(2));
             }
             rs2.close();
             stmt2.close();
             if (rs.getString(4)!=null && !rs.getString(4).equals("null")) {
                if (rs.getString(2).equals(AsistenteCriterio.CRITERIO_ESTADO)) {
                    if (rs.getString(4).equals("0")) {
                        criteriosSolicitudVO.setValor1("Abierto");
                    } else {
                        criteriosSolicitudVO.setValor1("Cerrado");
                    }
                } else {
                criteriosSolicitudVO.setValor1(rs.getString(4));
                }
             } else {
                criteriosSolicitudVO.setValor1("");
             }
             if (rs.getString(5)!=null && !rs.getString(5).equals("null")) {
                criteriosSolicitudVO.setValor2(rs.getString(5));
             } else {
                criteriosSolicitudVO.setValor2("");
             }
             criterios.addElement(criteriosSolicitudVO);
         }
         rs.close();
         stmt.close();

         Vector vectorCriterios = new Vector();

         for (int i=0;i<criterios.size();i++) {
            criteriosSolicitudVO = (CriteriosSolicitudValueObject) criterios.get(i);
            
            /* NOTA : el siguiente codigo comentado se usaba para recuperar los nombres de los
             * criterios pero daba nombres erroneos para los criterios basados en campos suplementarios
             * pues en la tabla INF_CAMPOS no se almacenan los campos suplementarios.
             * Se ha sustituido por una consulta similar a la tabla INF_CRITERIOS
             */
            //sql = "SELECT " + conf.getString("SQL.INF_CAMPOS.cod") + "," +
            //     conf.getString("SQL.INF_CAMPOS.nom") +
            //   " FROM INF_CAMPOS WHERE " +
            //     conf.getString("SQL.INF_CAMPOS.cod") + "=" + criteriosSolicitudVO.getTipo();
            
            sql = "SELECT DISTINCT " + conf.getString("SQL.INF_CRITERIOS.tipo") + "," +
                 conf.getString("SQL.INF_CRITERIOS.titulo") +
                 " FROM INF_CRITERIOS WHERE " +
                 conf.getString("SQL.INF_CRITERIOS.tipo") + "=" + criteriosSolicitudVO.getTipo()+"and cod_solicitud=" +criteriosSolicitudVO.getCodSolicitud();
            
            if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                vectorCriterios.add(rs.getString(2) + " " + criteriosSolicitudVO.getCondicion() + " " +
                criteriosSolicitudVO.getValor1() + " " + criteriosSolicitudVO.getValor2());
            }
            rs.close();
            stmt.close();
         }
         informeVO.setCriterios(vectorCriterios);
         informeVO.setVectorCabInformeVO(vectorCabInformeVO);
         informeVO.setVectorCabPaginaInformeVO(vectorCabPaginaInformeVO);
         informeVO.setVectorCuerpoInformeVO(vectorCuerpoInformeVO);
         informeVO.setVectorPiePaginaInformeVO(vectorPiePaginaInformeVO);
         informeVO.setVectorPieInformeVO(vectorPieInformeVO);
     }
     catch (SQLException sqle){
         m_Log.error("Error de SQL en getEstructuraInforme: ");
         sqle.printStackTrace();
     }
     catch (BDException bde){
         m_Log.error("Error del OAD en el metodo getEstructuraInforme: ");
         bde.printStackTrace();
     }
     finally {
        if (conexion != null){
         try{
            abd.devolverConexion(conexion);
         }
         catch(BDException bde){
             m_Log.error("No se pudo devolver la conexion: ");
             bde.printStackTrace();
         }
        }
        m_Log.debug("INFORME VALUE OBJECT DEVUELTO: "+informeVO);
     }
    return informeVO;
    }

    private void rollBackTransaction(AdaptadorSQLBD bd,Connection con,Exception e){
     try{
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