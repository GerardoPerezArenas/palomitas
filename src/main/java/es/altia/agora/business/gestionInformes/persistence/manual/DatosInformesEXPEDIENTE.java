package es.altia.agora.business.gestionInformes.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.gestionInformes.FichaInformeValueObject;
import es.altia.agora.business.gestionInformes.SolicitudInformeValueObject;
import es.altia.agora.business.gestionInformes.CriteriosValueObject;
import es.altia.agora.business.gestionInformes.tareas.AsistenteCriterio;
import es.altia.agora.business.gestionInformes.persistence.CampoValueObject;
import es.altia.agora.business.sge.InteresadoExpedienteVO;
import es.altia.agora.business.sge.persistence.manual.InteresadosDAO;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.business.terceros.persistence.manual.DatosSuplementariosTerceroManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.gestionInformes.FachadaDatosInformes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.BDException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.commons.DateOperations;
import es.altia.util.cache.CacheDatosFactoria;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Vector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;	
import java.util.SortedMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DatosInformesEXPEDIENTE implements FachadaDatosInformes {
    protected static Log m_Log = LogFactory.getLog(DatosInformesEXPEDIENTE.class.getName());
    private boolean tieneTramiteCuerpo = false;
    private String origen;
    public String getOrigen() {
        return origen;
    }//getOrigen
    public void setOrigen(String origen) {
        this.origen = origen;
    }//setOrigen

    public Vector getListaCampos(FichaInformeValueObject fiVO, String[] params) {
        if(m_Log.isDebugEnabled()) m_Log.debug("getListaCampos() : BEGIN");
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Config common = ConfigServiceHelper.getConfig("common");
        try{
             abd = new AdaptadorSQLBD(params);
             conexion = abd.getConnection();
             String codAplicacion = "4";
             StringBuffer sql = new StringBuffer();
             sql.append(" select NOMEAS AS CODIGO,NOME AS NOME, 'V_EXP_INF' AS TABLA, TIPO AS TIPO")
                .append(" FROM INF_CAMPOS ")
                .append(" WHERE ORIGEN = 1");
             if (fiVO.getCodProcedimiento() != null) {
                 sql.append(" UNION SELECT PCA_COD AS CODIGO,PCA_ROT AS NOME, TDA_TAB AS TABLA, null AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_PCA, E_TDA ")
                    .append(" WHERE E_PCA.PCA_TDA = E_TDA.TDA_COD AND ")
                    .append(" PCA_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" PCA_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" PCA_ACTIVO = 'SI' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 1 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                    .append(" E_PCA.PCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"))
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'T_'",abd.convertir("TCA_TRA", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null),"'_'","TCA_COD"})+" AS CODIGO, TCA_ROT AS NOME,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"TDA_TAB","'T'"})+" AS TABLA, null AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_TCA, E_TDA ")
                    .append(" WHERE E_TCA.TCA_TDA = E_TDA.TDA_COD AND ")
                    .append(" TCA_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" TCA_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" TCA_VIS = 'S' AND ")
                    .append(" TCA_ACTIVO = 'SI' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 1 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                    .append(" E_TCA.TCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"))
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Nom'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'NombreInteresado'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, null AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT ROL_DES AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'NombreCompletoInteresado'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, null AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Ap1'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Apellido1Interesado'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, null AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Ap2'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Apellido2Interesado'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, null AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Dom'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'DomicilioInteresado'","ROL_DES"})+"AS NOME, 'INT' AS TABLA, 'A' AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Rol'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'RolInteresado'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, 'A' AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Tid'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'TipoDocumInteresado'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, 'A' AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Doc'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'DocumInteresado'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, 'A' AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Pob'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'PoblacionInteresado'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, 'A' AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Tlfno'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'TelefonoInteresado'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, 'A' AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'CodPostal'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'CodPostal'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, 'A' AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")                                              
                     /*** EMAIL ***/
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Email'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Email'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, 'A' AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ");    
                     /*** EMAIL ****/     
                         
            }//if (fiVO.getCodProcedimiento() != null) 
            sql.append(" ORDER BY 2");
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ResultSet rs = stmt.executeQuery(sql.toString());
            Statement stmt2;
            ResultSet rs2;
            String sql2;
            while(rs.next()){
                CampoValueObject campoVO = new CampoValueObject();
                String codigo = rs.getString("CODIGO");
                campoVO.setOrigen(codigo);
                campoVO.setTitulo(rs.getString("NOME"));
                campoVO.setTituloOriginal(rs.getString("NOME"));
                campoVO.setTabla(rs.getString("TABLA"));
                if (rs.getString("TIPO") == null) {
                    if (campoVO.getTabla().equals("E_TDE") || campoVO.getTabla().equals("E_TDET") || 
                            campoVO.getTabla().equals("E_TDEX") || campoVO.getTabla().equals("E_TDEXT")) { //Desplegable
                        campoVO.setTipo("D"); 
                    } else if (campoVO.getTabla().equals("E_TFE") || campoVO.getTabla().equals("E_TFET") || 
                            campoVO.getTabla().equals("E_TFEC") || campoVO.getTabla().equals("E_TFECT")) { //Fecha
                        campoVO.setTipo("F");
                    } else if (campoVO.getTabla().equals("E_TNU") || campoVO.getTabla().equals("E_TNUC") || 
                            campoVO.getTabla().equals("E_TNUT") || campoVO.getTabla().equals("E_TNUCT")) { //Numerico
                        campoVO.setTipo("N");
                    } else if (campoVO.getTabla().equals("E_TXT") || campoVO.getTabla().equals("E_TXTT")) { //Texto
                        campoVO.setTipo("A");
                    }//if campoVO.getTabla
                }else{
                    campoVO.setTipo(rs.getString("TIPO"));
                    if ("I".equals(campoVO.getTipo())) continue;
                }//if (rs.getString("TIPO") == null)
                if (!(campoVO.getTabla().equals("E_AGR") || campoVO.getTabla().equals("E_AGRT"))) {
                    r.add(campoVO);
                }//if (!(campoVO.getTabla().equals("E_AGR") || campoVO.getTabla().equals("E_AGRT")))
            }//while(rs.next())
            rs.close();
            stmt.close();
            //Añadimos los campos suplementarios de tercero por expediente
            if (fiVO.getCodProcedimiento() != null) {
                getCamposSuplementariosTercero(r, conexion, fiVO.getCodProcedimiento(), fiVO.getCodMunicipio());
            }//if (fiVO.getCodProcedimiento() != null) 
        }catch (SQLException sqle){
            m_Log.error("Error de SQL en getListaCampos: " + sqle);
            sqle.printStackTrace();
        }catch (BDException bde){
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo getListaCampos: " + bde);
        }finally{
            if (conexion != null){
                try{
                    abd.devolverConexion(conexion);
                }catch(BDException bde){
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }//try-catch
            }//if (conexion != null)
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("getListaCampos() : END");
        return r;
    }//getListaCampos
    
    private void getCamposSuplementariosTercero(Vector listaCampos, Connection conexion, String codProcedimiento,String codMunicipio) 
            throws SQLException {
        if(m_Log.isDebugEnabled()) m_Log.debug("getCamposSuplementariosTercero() : BEGIN");
        try{
            //Recuperamos los roles del procedimiento.
            if(m_Log.isDebugEnabled()) m_Log.debug("recuperamos los roles del procedimiento");
            ArrayList<String> rolesProcedimiento = new ArrayList<String>();
                rolesProcedimiento = getRolesProcedimiento(conexion, codProcedimiento, codMunicipio);
            
            StringBuffer sql = new StringBuffer();
            sql.append("Select COD_CAMPO, ROTULO, TIPO_DATO from T_CAMPOS_EXTRA")
               .append(" where COD_MUNICIPIO = ").append(codMunicipio)
               .append(" and ACTIVO = 'SI'");
            
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            ResultSet rs = stmt.executeQuery(sql.toString());
            while(rs.next()){
                for(String descRol : rolesProcedimiento){
                    CampoValueObject campoVO = new CampoValueObject();
                    String codigo = rs.getString("COD_CAMPO");
                    String rotulo = rs.getString("ROTULO");
                    String tipoDato = rs.getString("TIPO_DATO");
                    
                    campoVO.setOrigen(codigo+descRol);
                    campoVO.setTitulo(rotulo+descRol);
                    campoVO.setTituloOriginal(rotulo+descRol);
                    if(tipoDato.equalsIgnoreCase("1")){
                        //Numerico
                        campoVO.setTipo("N");
                        campoVO.setTabla("T_CAMPOS_NUMERICO");
                    }else if(tipoDato.equalsIgnoreCase("2")){
                        //Texto
                        campoVO.setTipo("A");
                        campoVO.setTabla("T_CAMPOS_TEXTO");
                    }else if(tipoDato.equalsIgnoreCase("3")){
                        //Fecha
                        campoVO.setTipo("F");
                        campoVO.setTabla("T_CAMPOS_FECHA");
                    }else if(tipoDato.equalsIgnoreCase("4")){
                        //Texto largo
                        campoVO.setTipo("A");
                        campoVO.setTabla("T_CAMPOS_TEXTO_LARGO");
                    }else if(tipoDato.equalsIgnoreCase("6")){
                        //Desplegable
                        campoVO.setTipo("D");
                        campoVO.setTabla("T_CAMPOS_DESPLEGABLE");
                    }//if tipoCampo
                    listaCampos.add(campoVO);
                }//for(String descRol : rolesProcedimiento)
            }//while(rs.next())
        } catch (SQLException sqle){
            m_Log.error("Error de SQL en getCamposSuplementariosTercero: " + sqle);
            throw new SQLException(sqle.getMessage());
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("getCamposSuplementariosTercero() : END");
    }//getCamposSuplementariosTercero
    
    private ArrayList<String> getRolesProcedimiento (Connection conexion, String codProcedimiento, String codMunicipio) 
            throws SQLException{
        if(m_Log.isDebugEnabled()) m_Log.debug("getRolesProcedimiento() : BEGIN");
        ArrayList<String> listaDescripcionesRoles = new ArrayList<String>();
        try{
            StringBuffer sql = new StringBuffer();
            sql.append("Select ROL_DES from E_ROL")
               .append(" where ROL_PRO = '").append(codProcedimiento).append("'")
               .append(" and ROL_MUN = ").append(codMunicipio);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql.toString());
            while(rs.next()){
                listaDescripcionesRoles.add(rs.getString("ROL_DES"));
            }//while(rs.next())
        } catch (SQLException sqle){
            m_Log.error("Error de SQL en getRolesProcedimiento: " + sqle);
            throw new SQLException(sqle.getMessage());
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("getRolesProcedimiento() : END");
        return listaDescripcionesRoles;
    }//getRolesProcedimiento

    public Vector getListaCriteriosDisponibles(FichaInformeValueObject fiVO, String[] params) {
        if(m_Log.isDebugEnabled()) m_Log.debug("getListaCriteriosDisponibles() : BEGIN");
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Config common = ConfigServiceHelper.getConfig("common");
        try{
             abd = new AdaptadorSQLBD(params);
             conexion = abd.getConnection();
             String codAplicacion = "4";
             StringBuffer sql = new StringBuffer();
             sql.append(" select CAMPO AS CODIGO,NOME AS NOME, 'V_EXP_INF' AS TABLA, TIPO AS TIPO")
                .append(" FROM INF_CAMPOS ")
                .append(" WHERE ORIGEN = 1");
             if (fiVO.getCodProcedimiento() != null) {
                 sql.append(" UNION SELECT PCA_COD AS CODIGO,PCA_ROT AS NOME, TDA_TAB AS TABLA, null AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_PCA, E_TDA ")
                    .append(" WHERE E_PCA.PCA_TDA = E_TDA.TDA_COD AND ")
                    .append(" PCA_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" PCA_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" PCA_ACTIVO = 'SI' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 1 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                    .append(" E_PCA.PCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"))
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'T_'",abd.convertir("TCA_TRA", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null),"'_'","TCA_COD"})+" AS CODIGO, TCA_ROT AS NOME,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"TDA_TAB","'T'"})+" AS TABLA, null AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_TCA, E_TDA ")
                    .append(" WHERE E_TCA.TCA_TDA = E_TDA.TDA_COD AND ")
                    .append(" TCA_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" TCA_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" TCA_VIS = 'S' AND ")
                    .append(" TCA_ACTIVO = 'SI' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 1 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                    .append(" E_TCA.TCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"))
                    .append(" UNION SELECT ROL_DES AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'NombreInteresado'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, null AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Dom'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'DomicilioInteresado'","ROL_DES"})+"AS NOME, 'INT' AS TABLA, 'A' AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Rol'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'RolInteresado'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, 'A' AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Doc'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'DocumInteresado'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, 'A' AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Pob'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'PoblacionInteresado'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, 'A' AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ")
                    /* Kr */
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'Tlfno'","ROL_DES"})+" AS CODIGO,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'TelefonoInteresado'","ROL_DES"})+" AS NOME, 'INT' AS TABLA, 'A' AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_ROL ")
                    .append(" WHERE ROL_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" ROL_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 2 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA ");
            }//if (fiVO.getCodProcedimiento() != null) 
            sql.append(" ORDER BY 2");
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            ResultSet rs = stmt.executeQuery(sql.toString());
            Statement stmt2;
            ResultSet rs2;
            String sql2;
            while(rs.next()){
                CampoValueObject campoVO = new CampoValueObject();
                String codigo = rs.getString("CODIGO");
                campoVO.setOrigen(codigo);
                campoVO.setTitulo(rs.getString("NOME"));
                campoVO.setTituloOriginal(rs.getString("NOME"));
                campoVO.setTabla(rs.getString("TABLA"));
                if (rs.getString("TIPO") == null) {
                    if (campoVO.getTabla().equals("E_TDE") || campoVO.getTabla().equals("E_TDET") ||
                            campoVO.getTabla().equals("E_TDEX") || campoVO.getTabla().equals("E_TDEXT")) { //Desplegable
                        campoVO.setTipo("D");
                    } else if (campoVO.getTabla().equals("E_TFE") || campoVO.getTabla().equals("E_TFET") || 
                            campoVO.getTabla().equals("E_TFEC") || campoVO.getTabla().equals("E_TFECT")) { //Fecha
                        campoVO.setTipo("F");
                    } else if (campoVO.getTabla().equals("E_TNU") || campoVO.getTabla().equals("E_TNUC") || 
                            campoVO.getTabla().equals("E_TNUT") || campoVO.getTabla().equals("E_TNUCT")) { //Numerico
                        campoVO.setTipo("N");
                    } else if (campoVO.getTabla().equals("E_TXT") || campoVO.getTabla().equals("E_TXTT")) { //Texto
                        campoVO.setTipo("A");
                    }//if campoVO.getTabla
                } else {
                    campoVO.setTipo(rs.getString("TIPO"));
                }//if (rs.getString("TIPO") == null) 
                if (!(campoVO.getTabla().equals("E_AGR") || campoVO.getTabla().equals("E_AGRT"))) {
                    r.add(campoVO);
                }//if (!(campoVO.getTabla().equals("E_AGR") || campoVO.getTabla().equals("E_AGRT"))) 
            }//while(rs.next())
            rs.close();
            stmt.close();
        }catch (SQLException sqle){
            m_Log.error("Error de SQL en getListaCampos: " + sqle);
            sqle.printStackTrace();
        }catch (BDException bde){
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo getListaCampos: " + bde);
        }finally{
            if (conexion != null){
                try{
                    abd.devolverConexion(conexion);
                }catch(BDException bde){
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }//try-catch
            }//if (conexion != null)
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("getListaCriteriosDisponibles() : END");
        return r;
    }//getListaCriteriosDisponibles

    public Vector getListaCriInforme(FichaInformeValueObject fiVO, String[] params) {
        if(m_Log.isDebugEnabled()) m_Log.debug("getListaCriInforme() : BEGIN");
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Config common = ConfigServiceHelper.getConfig("common");
        int criterio = 0;
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String codAplicacion = "4";
            StringBuffer sql = new StringBuffer();
            sql.append(" select CODIGO AS CODIGO,NOME AS NOME, 'V_EXP_INF' AS TABLA, TIPO AS TIPO, CAMPO")
               .append(" FROM INF_CAMPOS ")
               .append(" WHERE ORIGEN = 1 AND CRITERIO = 1 ")
               .append(" ORDER BY 2");
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            ResultSet rs = stmt.executeQuery(sql.toString());
            while(rs.next()){
                CampoValueObject campoVO = new CampoValueObject();
                if (criterio < rs.getInt(1)) criterio = rs.getInt(1);
                campoVO.setIdCampo(rs.getString(1));
                campoVO.setTitulo(rs.getString(2));
                campoVO.setTabla(rs.getString(3));
                campoVO.setTipo(rs.getString(4));
                campoVO.setOrigen(rs.getString(5));
                if(m_Log.isDebugEnabled()){
                    m_Log.debug("COD_PROCEDIMIENTO " + fiVO.getCodProcedimiento());
                    m_Log.debug("COD_CRITERIO " + rs.getString(1));
                }//if(m_Log.isDebugEnabled())
                r.add(campoVO);
            }//while(rs.next())
            rs.close();
            stmt.close();

            //Añadimos los campos suplementarios
            if (fiVO.getCodProcedimiento() != null) {
                StringBuffer sql2 = new StringBuffer();
                sql2.append(" SELECT PCA_COD AS CODIGO,PCA_ROT AS NOME, TDA_TAB AS TABLA, 'P' AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_PCA, E_TDA ")
                    .append(" WHERE E_PCA.PCA_TDA = E_TDA.TDA_COD AND ")
                    .append(" PCA_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" PCA_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" PCA_ACTIVO = 'SI' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 1 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                    .append(" E_PCA.PCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"))
                    .append(" UNION SELECT "+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"'T_'",abd.convertir("TCA_TRA", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null),"'_'","TCA_COD"})+" AS CODIGO, TCA_ROT AS NOME,"+abd.funcionCadena(abd.FUNCIONCADENA_CONCAT,new String[]{"TDA_TAB","'T'"})+" AS TABLA, 'P' AS TIPO ")
                    .append(" FROM ESTRUCTURAINFORME ESI, A_DOC, E_TCA, E_TDA ")
                    .append(" WHERE E_TCA.TCA_TDA = E_TDA.TDA_COD AND ")
                    .append(" TCA_MUN = ").append(fiVO.getCodMunicipio()).append(" AND ")
                    .append(" TCA_PRO = '").append(fiVO.getCodProcedimiento()).append("' AND ")
                    .append(" TCA_VIS = 'S' AND ")
                    .append(" TCA_ACTIVO = 'SI' AND ")
                    .append(" A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
                    .append(" A_DOC.DOC_CEI = 1 AND ")
                    .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
                    .append(" E_TCA.TCA_PLT<>" + common.getString("E_PLT.CodigoPlantillaFichero"));
                sql2.append(" ORDER BY 2");
                stmt = conexion.createStatement();
                if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql2);
                rs = stmt.executeQuery(sql2.toString());
                while(rs.next()){
                    String codigo = rs.getString("CODIGO");
                    CampoValueObject campoVO = new CampoValueObject();
                    campoVO.setIdCampo(String.valueOf(++criterio));
                    //campoVO.setIdCampo(rs.getString(1));
                    campoVO.setTitulo(rs.getString(2));
                    campoVO.setTabla(rs.getString(3));
                    if (rs.getString(4).trim().equals("P")) {
                        String tabla = rs.getString(3);
                        if (tabla.equals("E_TFE") || tabla.equals("E_TFET") || tabla.equals("E_TFEC") || tabla.equals("E_TFECT")) {
                            campoVO.setTipo("F");
                        } else if (tabla.equals("E_TXT") || tabla.equals("E_TXTT")) {
                            campoVO.setTipo("A");
                        } else if (tabla.equals("E_TNU") || tabla.equals("E_TNUC") || tabla.equals("E_TNUT") || tabla.equals("E_TNUCT")) {
                            campoVO.setTipo("N");
                        } else if (tabla.equals("E_TDE") || tabla.equals("E_TDET") || tabla.equals("E_TDEX") || tabla.equals("E_TDEXT")) {
                            campoVO.setTipo("D");
                            //Recuperacion del codigo de campo desplegable
                            Statement _stmt;
                            ResultSet _rs;
                            String _sql;
                            if (campoVO.getTabla().equals("E_TDE") || campoVO.getTabla().equals("E_TDEX")) {
                                _sql = " SELECT PCA_DESPLEGABLE FROM E_PCA WHERE PCA_ROT='" + campoVO.getTitulo() + "' AND PCA_PRO='" + fiVO.getCodProcedimiento() +
                                      "' AND PCA_MUN=" + fiVO.getCodMunicipio();
                                if(m_Log.isDebugEnabled()) m_Log.debug("_sql = " + _sql);
                                _stmt = conexion.createStatement();
                                _rs = _stmt.executeQuery(_sql);
                                while(_rs.next()){
                                    campoVO.setValor2Criterio(_rs.getString("PCA_DESPLEGABLE"));
                                }//while(_rs.next())
                                _rs.close();
                                _stmt.close();
                            } else if (campoVO.getTabla().equals("E_TDET") || campoVO.getTabla().equals("E_TDEXT")) {
                                _sql = " SELECT TCA_DESPLEGABLE FROM E_TCA WHERE TCA_ROT='" + campoVO.getTitulo() + "' AND TCA_PRO='" + fiVO.getCodProcedimiento() +
                                      "' AND TCA_MUN=" + fiVO.getCodMunicipio();
                                if(m_Log.isDebugEnabled()) m_Log.debug("_sql = " + _sql);
                                _stmt = conexion.createStatement();
                                _rs = _stmt.executeQuery(_sql);
                                while(_rs.next()){
                                    campoVO.setValor2Criterio(_rs.getString("TCA_DESPLEGABLE"));
                                }//while(_rs.next())
                                _rs.close();
                                _stmt.close();
                            }
                        }//campoVO.getTabla()
                    } else campoVO.setTipo(rs.getString(4));
                    campoVO.setOrigen(rs.getString(1));
                    m_Log.debug("COD_PROCEDIMIENTO " + fiVO.getCodProcedimiento());
                    m_Log.debug("COD_CRITERIO " + rs.getString(1));
                    r.add(campoVO);
                }//while(rs.next())
                rs.close();
                stmt.close();
                
                //Añadimos los campos suplementarios de tercero por expediente
                if(fiVO.getCodProcedimiento() != null){
                    getCriteriosSuplementariosTercero(r, conexion, fiVO.getCodProcedimiento(), fiVO.getCodMunicipio(),criterio);
                }//if(fiVO.getCodProcedimiento() != null)
            }//if (fiVO.getCodProcedimiento() != null) 
        } catch (SQLException sqle) {
            m_Log.error("Error de SQL en getListaCriInforme: " + sqle);
            sqle.printStackTrace();
        } catch (BDException bde) {
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo getListaCriInforme: " + bde);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }//try-catch
            }//if (conexion != null) 
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("getListaCriInforme() : END");
        return r;
    }//getListaCriInforme
    
    private void getCriteriosSuplementariosTercero(Vector listaCampos, Connection conexion, String codProcedimiento,String codMunicipio,
           int criterio) throws SQLException {
        if(m_Log.isDebugEnabled()) m_Log.debug("getCriteriosSuplementariosTercero() : BEGIN");
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("Select COD_CAMPO, ROTULO, TIPO_DATO, DESPLEGABLE from T_CAMPOS_EXTRA")
               .append(" where COD_MUNICIPIO = ").append(codMunicipio)
               .append(" and ACTIVO = 'SI'");
            
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            ResultSet rs = stmt.executeQuery(sql.toString());
            while(rs.next()){
                CampoValueObject campoVO = new CampoValueObject();
                String tipoDato = rs.getString("TIPO_DATO");
                campoVO.setIdCampo(String.valueOf(criterio++));
                campoVO.setTitulo(rs.getString("ROTULO"));
                campoVO.setOrigen(rs.getString("COD_CAMPO"));
                if(tipoDato.equalsIgnoreCase("1")){
                    //Numerico
                    campoVO.setTipo("N");
                    campoVO.setTabla("T_CAMPOS_NUMERICO");
                }else if(tipoDato.equalsIgnoreCase("2")){
                    //Texto
                    campoVO.setTipo("A");
                    campoVO.setTabla("T_CAMPOS_TEXTO");
                }else if(tipoDato.equalsIgnoreCase("3")){
                    //Fecha
                    campoVO.setTipo("F");
                    campoVO.setTabla("T_CAMPOS_FECHA");
                }else if(tipoDato.equalsIgnoreCase("4")){
                    //Texto largo
                    campoVO.setTipo("A");
                    campoVO.setTabla("T_CAMPOS_TEXTO_LARGO");
                }else if(tipoDato.equalsIgnoreCase("6")){
                    //Desplegable
                    campoVO.setTipo("D");
                    campoVO.setTabla("T_CAMPOS_DESPLEGABLE");
                    campoVO.setValor2Criterio(rs.getString("DESPLEGABLE"));
                }//if tipoCampo
                listaCampos.add(campoVO);
            }//while(rs.next())
        } catch (SQLException sqle){
            m_Log.error("Error de SQL en getCamposSuplementariosTercero: " + sqle);
            throw new SQLException(sqle.getMessage());
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("getCriteriosSuplementariosTercero() : END");
    }//getCriteriosSuplementariosTercero

    public Vector getListaCriSeleccionados(FichaInformeValueObject fiVO, String[] params) {
        if(m_Log.isDebugEnabled()) m_Log.debug("getListaCriSeleccionados() : BEGIN");
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            // Buscamos criterios seleccionados
            String sql = "SELECT PLANT_INF_CRI_PLANTILLA, PLANT_INF_CRI_CAMPO, PLANT_INF_CRI_CONDICION, PLANT_INF_CRI_VALOR1, PLANT_INF_CRI_VALOR2, " +
                    " PLANT_INF_CRI_TITULO, PLANT_INF_CRI_ORIGEN, PLANT_INF_CRI_TABLA, TIPO" +
                    " FROM PLANT_INF_CRI " +
                    " LEFT JOIN INF_CAMPOS ON ( PLANT_INF_CRI_CAMPO = CODIGO ) AND ORIGEN = " + fiVO.getCodAmbito() +
                    " WHERE PLANT_INF_CRI_PLANTILLA = " + fiVO.getCodPlantilla() + " " +
                    " ORDER BY PLANT_INF_CRI_ID";
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                CampoValueObject criInf = new CampoValueObject();
                criInf.setCodPlantilla(rs.getString(1));
                criInf.setIdCampo(rs.getString(2));
                criInf.setCondicionCriterio(rs.getString(3));
                if (criInf.getIdCampo().equals(String.valueOf(AsistenteCriterio.CRITERIO_ESTADO))) {
                    if (rs.getString(4)!=null && rs.getString(4).equals("9")) {
                        criInf.setValor1Criterio("1");
                    } else {
                        criInf.setValor1Criterio(rs.getString(4));
                    }//if (rs.getString(4)!=null && rs.getString(4).equals("9")) 
                } else {
                    criInf.setValor1Criterio(rs.getString(4));
                }//if (criInf.getIdCampo().equals(String.valueOf(AsistenteCriterio.CRITERIO_ESTADO))) 
                criInf.setValor2Criterio(rs.getString(5));
                criInf.setTitulo(rs.getString(6));
                criInf.setOrigen(rs.getString(7));
                criInf.setTabla(rs.getString(8));
                if ( rs.getString(9) == null) {
                    String tabla = rs.getString(8);
                    if (tabla.equals("E_TFE") || tabla.equals("E_TFET") || tabla.equals("E_TFEC") || tabla.equals("E_TFECT") || 
                            tabla.equals("T_CAMPOS_FECHA")) {
                        criInf.setTipo("F");
                    } else if (tabla.equals("E_TXT") || tabla.equals("E_TXTT") || tabla.equals("T_CAMPOS_TEXTO") 
                            || tabla.equals("T_CAMPOS_TEXTO_LARGO")) {
                        criInf.setTipo("A");
                    } else if (tabla.equals("E_TNU") || tabla.equals("E_TNUC") || tabla.equals("E_TNUT") || 
                            tabla.equals("E_TNUCT") || tabla.equals("T_CAMPOS_NUMERICO")){
                        criInf.setTipo("N");
                    } else if (tabla.equals("E_TDE") || tabla.equals("E_TDET") || tabla.equals("E_TDEX") || 
                            tabla.equals("E_TDEXT") || tabla.equals("T_CAMPOS_DESPLEGABLE")) {
                        criInf.setTipo("D");
                    }// if tabla.equals
                } else criInf.setTipo(rs.getString(9));
                // Recuperacion del codigo de campo desplegable
                Statement _stmt;
                ResultSet _rs;
                String _sql;
                if (criInf.getTabla().equals("E_TDE") || criInf.getTabla().equals("E_TDEX")) {
                    _sql = " SELECT PCA_DESPLEGABLE FROM E_PCA WHERE PCA_ROT='" + criInf.getTitulo() + "' AND PCA_PRO='" + fiVO.getCodProcedimiento() +
                          "' AND PCA_MUN=" + fiVO.getCodMunicipio();
                    if(m_Log.isDebugEnabled()) m_Log.debug("_sql = " + _sql);
                    _stmt = conexion.createStatement();
                    _rs = _stmt.executeQuery(_sql);
                    while (_rs.next()) {
                    	criInf.setValor2Criterio(_rs.getString("PCA_DESPLEGABLE"));
                    }//while (_rs.next()) 
                    _rs.close();
                    _stmt.close();
                } else if (criInf.getTabla().equals("E_TDET") || criInf.getTabla().equals("E_TDEXT")) {
                    _sql = " SELECT TCA_DESPLEGABLE FROM E_TCA WHERE TCA_ROT='" + criInf.getTitulo() + "' AND TCA_PRO='" + fiVO.getCodProcedimiento() +
                          "' AND TCA_MUN=" + fiVO.getCodMunicipio();
                    if(m_Log.isDebugEnabled()) m_Log.debug("_sql = " + _sql);
                    _stmt = conexion.createStatement();
                    _rs = _stmt.executeQuery(_sql);
                    while (_rs.next()) {
                    	criInf.setValor2Criterio(_rs.getString("TCA_DESPLEGABLE"));
                    }//while (_rs.next()) 
                    _rs.close();
                    _stmt.close();
                }//if
                r.add(criInf);
            }//while (rs.next()) 
            rs.close();
            stmt.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (BDException bde) {
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo getListaCriSeleccionados: " + bde);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }//try-catch
            }//if (conexion != null) 
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("getListaCriSeleccionados() : END");
        return r;
    }//getListaCriSeleccionados

    public Vector getListaCriFinal(FichaInformeValueObject fiVO, String[] params) {
        if(m_Log.isDebugEnabled()) m_Log.debug("getListaCriFinal() : BEGIN");
        Vector r = new Vector();
        AdaptadorSQLBD oad = null;
        Connection conexion = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            oad = new AdaptadorSQLBD(params);
            conexion = oad.getConnection();
            String from = "PLANT_INF_CRI_PLANTILLA, PLANT_INF_CRI_CAMPO, PLANT_INF_CRI_CONDICION, PLANT_INF_CRI_VALOR1, " +
                          "PLANT_INF_CRI_VALOR2, PLANT_INF_CRI_TITULO, PLANT_INF_CRI_ORIGEN, PLANT_INF_CRI_TABLA, TIPO, " +
                          "PLANT_INF_CRI_ID";

            ArrayList<String> join = new ArrayList<String>();

            join.add("PLANT_INF_CRI");
            join.add("LEFT");
            join.add("(SELECT * FROM INF_CAMPOS WHERE ORIGEN = " + fiVO.getCodAmbito()  + " ) INF_CAMPOS");
            join.add("PLANT_INF_CRI.PLANT_INF_CRI_CAMPO = INF_CAMPOS.CODIGO");
            join.add("false");

            String where = "PLANT_INF_CRI_PLANTILLA = " + fiVO.getCodPlantilla();

            String sql = oad.join(from, where, join.toArray(new String[]{}));
            sql = "SELECT * FROM (" + sql + ") CONSULTA ORDER BY PLANT_INF_CRI_ID";

            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                CriteriosValueObject criteriosVO = new CriteriosValueObject();
                criteriosVO.setCodPlantilla(fiVO.getCodPlantilla());
                criteriosVO.setId(rs.getString(10));
                criteriosVO.setCampo(rs.getString(2));
                criteriosVO.setCondicion(rs.getString(3)!=null?rs.getString(3):"");
                criteriosVO.setValor1(rs.getString(4)!=null?rs.getString(4):"");
                criteriosVO.setValor2(rs.getString(5)!=null?rs.getString(5):"");
                criteriosVO.setTitulo(rs.getString(6)!=null?rs.getString(6):"");
                criteriosVO.setOrigen(rs.getString(7)!=null?rs.getString(7):"");
                criteriosVO.setTabla(rs.getString(8)!=null?rs.getString(8):"");
                if ( rs.getString(9) == null) {
                    String tabla = rs.getString(8);
                    if (tabla.equals("E_TFE") || tabla.equals("E_TFET") || tabla.equals("E_TFEC") || tabla.equals("E_TFECT") || 
                            tabla.equals("T_CAMPOS_FECHA")) {
                        criteriosVO.setTipo("F");
                    } else if (tabla.equals("E_TXT") || tabla.equals("E_TXTT") || tabla.equals("T_CAMPOS_TEXTO")
                            || tabla.equals("T_CAMPOS_TEXTO_LARGO")) {
                        criteriosVO.setTipo("A");
                    } else if (tabla.equals("E_TNU") || tabla.equals("E_TNUC") || tabla.equals("E_TNUT") || 
                            tabla.equals("E_TNUCT") ||  tabla.equals("T_CAMPOS_NUMERICO")) {
                        criteriosVO.setTipo("N");
                    } else if (tabla.equals("E_TDE") || tabla.equals("E_TDET") || tabla.equals("E_TDEX") || 
                            tabla.equals("E_TDEXT") || tabla.equals("T_CAMPOS_DESPLEGABLE")) {
                        criteriosVO.setTipo("D");
                    }//if
                } else criteriosVO.setTipo(rs.getString(9));
                // Recuperacion del codigo de campo desplegable
                Statement _stmt;
                ResultSet _rs;
                String _sql;
                if (criteriosVO.getTabla().equals("E_TDE") || criteriosVO.getTabla().equals("E_TDEX")) {
                    _sql = " SELECT PCA_DESPLEGABLE FROM E_PCA WHERE PCA_ROT='" + criteriosVO.getTitulo() + "' AND PCA_PRO='" + fiVO.getCodProcedimiento() +
                          "' AND PCA_MUN=" + fiVO.getCodMunicipio();
                    if(m_Log.isDebugEnabled()) m_Log.debug("_sql = " + _sql);
                    _stmt = conexion.createStatement();
                    _rs = _stmt.executeQuery(_sql);
                    while (_rs.next()) {
                    	criteriosVO.setValor2(_rs.getString("PCA_DESPLEGABLE"));
                    }//while (_rs.next())
                    _rs.close();
                    _stmt.close();
                } else if (criteriosVO.getTabla().equals("E_TDET") || criteriosVO.getTabla().equals("E_TDEXT")) {
                    _sql = " SELECT TCA_DESPLEGABLE FROM E_TCA WHERE TCA_ROT='" + criteriosVO.getTitulo() + "' AND TCA_PRO='" + fiVO.getCodProcedimiento() +
                          "' AND TCA_MUN=" + fiVO.getCodMunicipio();
                    if(m_Log.isDebugEnabled()) m_Log.debug("_sql = " + _sql);
                    _stmt = conexion.createStatement();
                    _rs = _stmt.executeQuery(_sql);
                    while (_rs.next()) {
                    	criteriosVO.setValor2(_rs.getString("TCA_DESPLEGABLE"));
                    }//while (_rs.next())
                    _rs.close();
                    _stmt.close();
                }//if
                r.add(criteriosVO);
            }//while (rs.next()) 
        } catch (SQLException sqle) {
            m_Log.error("Error de SQL en getListaCriSeleccionados: " + sqle);
            sqle.printStackTrace();
        } catch (BDException bde) {
            if(m_Log.isErrorEnabled()) m_Log.error("error del OAD en el metodo getListaCriSeleccionados: " + bde);
        } finally {
            if (conexion != null) {
                try {
                    oad.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }//try-catch
            }//if (conexion != null) 
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("getListaCriFinal() : END");
        return r;
    }//getListaCriFinal

   public Vector getListaPermisos(String[] params) {
        if(m_Log.isDebugEnabled()) m_Log.debug("getListaPermisos() : BEGIN");
        Vector listaPermisos = new Vector();
         SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(params[6]);	
        for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {
            UORDTO unidad = entry.getValue();
            if (unidad.getUor_fecha_baja()==null)
                listaPermisos.add(unidad.getUor_cod_vis());
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("getListaPermisos() : END");
        return listaPermisos;
    }//getListaPermisos

    public Vector getDatosExpedientes(SolicitudInformeValueObject siVO, String codOrganizacion, String[] params) throws Exception {
        if(m_Log.isDebugEnabled()) m_Log.debug("getDatosExpedientes() : BEGIN");
        if(m_Log.isDebugEnabled()) m_Log.debug("ENTRADA EN NUEVO MÉTODO DE OBTENCIÓN DE DATOS");
        /*
         * Pasos a seguir para la construcción del informe. Debemos diferenciar si en dicho informe van datos de trámite (datos
         que acarrearían que salga una fila por cada trámite y expediente), o si el informe tendrá una fila por expediente.
         * Dichos datos de trámite conflictivos son:
         * - Cod. Uor trámite.
         * - Cod. trámite.
         * - Fecha de inicio de trámite.
         * - Fecha de fin de trámite.
         * - Nombre de UOR de trámite
         * - Nombre trámite.
         * Observaciones trámite.
         */

        /*
         * CONSTRUCCIÓN DE INFORME SIN DATOS DE TRÁMITE (CASO GENERAL).
         * 1. Se construye una tabla temporal global cuyas columnas serán los distintos campos que irán en el informe.
         *    El nombre de esta tabla será CODPLANTILLA_CODUSUARIO_TIMESTAMP. La primary key de la tabla será el
         *    nº de expediente.
         * 2. Mediante consultas generadas automáticamente se rellenarán la columna nº de expediente con aquellos que
         *    cumplan los criterios del informe.
         * 3. Mediante llamadas a funciones específicas, se cubrirán los datos de la tabla:
         *    - Una función para cubrir datos generales del expediente/trámite.
         *    - Una función para rellenar el interesado.
         *    - Una función para rellenar datos suplementarios según tipo y ubicación.
         * 4. Una vez completada la información de la tabla se construye el expediente con una SELECT simple sobre dicha tabla.
         */

         /*
         * CONSTRUCCIÓN DE INFORME CON DATOS DE TRÁMITE (DUPLICADOS).
         * 1. Se construye una tabla temporal global cuyas columnas serán los distintos campos que irán en el informe.
         *    El nombre de esta tabla será CODPLANTILLA_CODUSUARIO_TIMESTAMP. La primary key de la tabla será el
         *    nº de expediente.
         * 2. Mediante consultas generadas automáticamente se rellenarán la columna nº de expediente con aquellos que
         *    cumplan los criterios del informe.
         * 3. Mediante llamadas a funciones específicas, se cubrirán los datos de la tabla:
         *    - Una función para cubrir datos generales del expediente/trámite.
         *    - Una función para rellenar el interesado.
         *    - Una función para rellenar datos suplementarios según tipo y ubicación.
         * 4. Una vez completada la información de la tabla se construye el expediente con una JOIN entre dicha tabla
         *    y la vista V_EXP_INF.
         */

        /*
         * Las tablas temporales trabajan a través de sesiones activas, por lo que no podemos realizar varias conexiones
         * y debemos pasar la consxion como parámetro a cada uno de los métodos usados.
         */
        Vector informe = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        GeneralValueObject gVO = new GeneralValueObject();
        tieneTramiteCuerpo = false;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            if(m_Log.isDebugEnabled()) m_Log.debug("Iniciamos la transaccion");
            abd.inicioTransaccion(conexion);
            if(m_Log.isDebugEnabled()) m_Log.debug("Construimos la tabla temporal para el informe");
            gVO = construirTablaTemporalInforme(siVO, conexion, abd, params[0].toUpperCase());
            if(m_Log.isDebugEnabled()) m_Log.debug("Obtenemos la consulta tiene expedientes informe");
            String queryExpedientes = obtieneConsultaExpedientesInforme ((String)gVO.getAtributo("nomTabla"), siVO, conexion, abd);
            if(m_Log.isDebugEnabled()) m_Log.debug("Rellenamos con expedientes la tabla temporal");
            GeneralValueObject lista = rellenaExpedientesTablaTemporal2 (queryExpedientes, (String)gVO.getAtributo("nomTabla"), conexion, abd);
            
            Vector expedientes = (Vector)lista.getAtributo("expedientes");
            Vector<Integer> tramites = (Vector<Integer>)lista.getAtributo("tramites");
            
            if(m_Log.isDebugEnabled()) m_Log.debug("Completamos la tabla temporal");
            completarTablaTemporal2 (gVO, siVO.getProcedimiento(), conexion, abd, params,codOrganizacion,expedientes,tramites);
            if(m_Log.isDebugEnabled()) m_Log.debug("Finalizamos la transaccion");
            abd.finTransaccion(conexion);
            if(m_Log.isDebugEnabled()) m_Log.debug("Recuperamos los datos para el informe");
            informe = recuperarDatosInforme(siVO, (String)gVO.getAtributo("nomTabla"), conexion, abd);
        }catch(Exception ex){
            throw ex;
        }finally{
            if(m_Log.isDebugEnabled()) m_Log.debug("Eliminamos la tabla temporal");
            eliminarTablaTemporal((String)gVO.getAtributo("nomTabla"), conexion, abd);
            if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos la conexion");
            abd.devolverConexion(conexion);
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("getDatosExpedientes() : END");
        return informe;
    }//getDatosExpedientes

    private GeneralValueObject construirTablaTemporalInforme(SolicitudInformeValueObject siVO, Connection conexion, AdaptadorSQLBD abd, String gestor) throws BDException{
        if(m_Log.isDebugEnabled()) m_Log.debug("construirTablaTemporalInforme() : BEGIN");
        PreparedStatement pstmt = null;
        String sql = "";
        ResultSet rs = null;
        GeneralValueObject gVO = new GeneralValueObject();
        Vector <String> campos = new Vector<String>();
        Vector <String> tablas = new Vector<String>();
        Vector <String> columnas = new Vector<String>();
        Vector <String> tipos = new Vector<String>();
        
        /*
         * En el generalValueObject irán 4 datos:
         * - String nomTabla: Nombre de la tabla temporal creada.
         * - Vector campos: Campos que dan nombre a las columnas y que se serán el cuerpo.
         * - Vector tablas: Nombres de las tablas en las que encontrar cada dato.
         * - Vector columnas: Columnas de las anteriores tablas en las que encontrar el dato.
         * - Vector tipos: Tipo de dato.
        */

        java.util.Date utilDate = new java.util.Date();
        long lnMilisegundos = utilDate.getTime();
        String nomTabla = "";
        
        if (gestor.equals("ORACLE")){ 
            nomTabla = "GI_"+siVO.getCodSolicitud()+"_"+siVO.getCodUsuario()+"_"+lnMilisegundos;
            gVO.setAtributo("nomTabla", nomTabla);
        }else{ 
            nomTabla = "##GI_"+siVO.getCodSolicitud()+"_"+siVO.getCodUsuario()+"_"+lnMilisegundos;
            gVO.setAtributo("nomTabla", nomTabla);
        }//if

        if (gestor.equals("ORACLE")){
            sql += "CREATE GLOBAL TEMPORARY TABLE " + nomTabla +"(SIGP_TEMP_EXP VARCHAR2(30),";
        }else{
            sql += "CREATE TABLE " + nomTabla +"(SIGP_TEMP_EXP VARCHAR(30) NOT NULL,";
        }//if

        /*
         * Con esta consulta obtenemos los datos que llevará el informe y el tipo de dato que es:
         * A: Alfanumérico
         * N: Número
         * F: Fecha
         * D: Desplegable
         * En cualquier otro caso se interpreta como alfanumérico.
        */
        String sqlColumnas="SELECT campo as columna,plant_inf_col_tabla as tabla," +
            abd.funcionCadena(abd.FUNCIONCADENA_REPLACE, new String[]{"plant_inf_col_origen","' '","''"}) +
            " AS campo, " +
            "tipo FROM plant_inf_col INNER JOIN " +
            "inf_campos ON (nomeas = plant_inf_col_origen) WHERE plant_inf_col_plantilla = ? UNION " +
            "SELECT  " +
            abd.funcionCadena(abd.FUNCIONCADENA_SUBSTR, new String[]{"plant_inf_col_tabla","3",
            abd.funcionCadena(abd.FUNCIONCADENA_LENGTH, new String[]{"plant_inf_col_tabla"})}) +
            abd.getSymbolConcat() +
            "'_VALOR' as columna,plant_inf_col_tabla as tabla," +
            abd.funcionCadena(abd.FUNCIONCADENA_REPLACE, new String[]{"plant_inf_col_origen","' '","''"}) +
            " AS campo, (CASE WHEN plant_inf_col_tabla IN ('E_TNU','E_TNUC','E_TNUT','E_TNUCT') THEN 'N' " + 
            "WHEN plant_inf_col_tabla IN ('E_TFET','E_TFE','E_TFECT','E_TFEC') THEN 'F' " + 
            "WHEN plant_inf_col_tabla = 'E_TXT' OR " +
            "plant_inf_col_tabla = 'E_TXTT' THEN 'A' " + 
            "WHEN plant_inf_col_tabla IN ('E_TDE','E_TDET','E_TDEX','E_TDEXT') THEN 'D' ELSE 'A' END) AS tipo " + 
            "FROM plant_inf_col WHERE " +
            "plant_inf_col_plantilla = ? AND NOT EXISTS  (SELECT *    FROM inf_campos    " +
            "WHERE nomeas = plant_inf_col_origen)";

        try {
            if(m_Log.isDebugEnabled()) m_Log.debug("Consulta de columnas de tabla temporal = " + sqlColumnas);
            pstmt = conexion.prepareStatement(sqlColumnas);
            pstmt.setString(1, siVO.getCodPlantilla());
            pstmt.setString(2, siVO.getCodPlantilla());
            rs = pstmt.executeQuery();

            while (rs.next()){
                sql+= " "+rs.getString("campo");
                campos.add(rs.getString("campo"));
                if (gestor.equals("ORACLE")){
                    if (rs.getString("tipo").equals("A")) sql+=" VARCHAR2 (4000 BYTE) ,";
                    if (rs.getString("tipo").equals("F")) sql+=" DATE ,";
                    if (rs.getString("tipo").equals("D")) sql+=" VARCHAR2 (4000 BYTE) ,";
                    if (rs.getString("tipo").equals("N")) sql+=" NUMBER(10,2) ,";
                } else{
                    if (rs.getString("tipo").equals("A")) sql+=" VARCHAR (4000) ,";
                    if (rs.getString("tipo").equals("F")) sql+=" DATETIME ,";
                    if (rs.getString("tipo").equals("D")) sql+=" VARCHAR (4000) ,";
                    if (rs.getString("tipo").equals("N")) sql+=" DECIMAL(10,2) ,";
                }//if (gestor.equals("ORACLE"))

                tablas.add(rs.getString("TABLA"));
                columnas.add(rs.getString("COLUMNA"));
                tipos.add(rs.getString("TIPO"));
                if (rs.getString("COLUMNA").equals("TRA_FEI") || rs.getString("COLUMNA").equals("TRA_FEF") || rs.getString("COLUMNA").equals("TRA_NOM") ||
                        rs.getString("COLUMNA").equals("TRA_COD") || rs.getString("COLUMNA").equals("TRA_OBS") || rs.getString("COLUMNA").equals("TRA_UOR_COD") ||
                        rs.getString("COLUMNA").equals("TRA_UOR_NOM")) tieneTramiteCuerpo = true;
            }//while (rs.next())
            
            rs.close();
            pstmt.close();
            if (gestor.equals("SQLSERVER")){
                sql += "SIGP_TEMP_TRA DECIMAL (6,0) NOT NULL, SIGP_TEMP_OCU DECIMAL (6,0) NOT NULL)";
            }else if (gestor.equals("ORACLE")){
                sql += "SIGP_TEMP_TRA NUMBER (6,0) NOT NULL, SIGP_TEMP_OCU NUMBER (6,0) NOT NULL)";
            }//if

            if (gestor.equals("ORACLE")){
                sql += "ON COMMIT PRESERVE ROWS";
            }//if
            
            //if (gestor.equals("ORACLE")) sql += "ON COMMIT DELETE ROWS";
            gVO.setAtributo("campos", campos);
            gVO.setAtributo("tablas", tablas);
            gVO.setAtributo("columnas", columnas);

            if(m_Log.isDebugEnabled()) m_Log.debug("Consulta para creación de tabla temporal="+sql);
            pstmt = conexion.prepareStatement(sql);
            pstmt.executeUpdate();
            rs.close();
            pstmt.close();
            /*
             * Debemos tener en cuenta,a la hora de fijar la clave principal que, si el cuerpo del informe contiene
             * datos de támites (fecha de inicio/fin, usuarios y unidades orgánicas, nombres), la clave será el código
             * del trámite y el nº de expediente. En caso contrario será solo el nº de expediente.
             */
            /*String key = "";
            if (gestor.equals("ORACLE") && tieneTramiteCuerpo) key = "";
            else if (gestor.equals("ORACLE") && !tieneTramiteCuerpo) key = "";
            else if (gestor.equals("SQLSERVER") && tieneTramiteCuerpo)
                key = "ALTER TABLE dbo." + nomTabla + " ADD CONSTRAINT PK_" + nomTabla + " PRIMARY KEY CLUSTERED (SIGP_TEMP_EXP)";
            else if (gestor.equals("SQLSERVER") && !tieneTramiteCuerpo)
                key = "ALTER TABLE dbo." + nomTabla + " ADD CONSTRAINT PK_" + nomTabla + " PRIMARY KEY CLUSTERED (SIGP_TEMP_EXP,SIGP_TEMP_TRA)";
            pstmt = conexion.prepareStatement(key);
            resultado = pstmt.executeUpdate();*/
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR CONSTRUYENDO TABLA TEMPORAL. construirTablaTemporalInforme ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("construirTablaTemporalInforme() : END");
        return gVO;
    }//construirTablaTemporalInforme

    private String obtieneConsultaExpedientesInforme (String nomTabla, SolicitudInformeValueObject siVO, Connection conexion, AdaptadorSQLBD abd) throws BDException{
        if(m_Log.isDebugEnabled()) m_Log.debug("obtieneConsultaExpedientesInforme() : BEGIN");
        if(m_Log.isDebugEnabled()) m_Log.debug("Función para almacenar los números de expediente que cumplen los criterios del informe");

        /*
         * En esta función se almacenan en la tabla temporal los nº de expediente que cumplen los criterios definidos en
         * la plantilla del informe.
         */
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector criterios = new Vector<String>();

        /*
         * Consulto los criterios de la plantilla para trabajar en base a ellos.
         */
        String query = "";
        if (tieneTramiteCuerpo){
            query = "SELECT CRO_NUM AS EXPEDIENTE, CRO_TRA AS TRAMITE FROM E_CRO CRONO";
        }else{
            query = "SELECT DISTINCT(CRO_NUM) AS EXPEDIENTE FROM E_CRO CRONO";
        }//if

        String sqlCriteriosInforme = "SELECT CAMPO, (CASE WHEN NOMEAS='INTERESADOEXP' THEN 'V_EXP_INT_INF' ELSE (CASE WHEN NOMEAS='NUMERO_REGISTRO' or NOMEAS='FECHA_PRESENTACION' THEN 'V_REG_INICIO_EXP' ELSE TABLA END)  END) AS TABLA," +
                " VALOR1, VALOR2, CONDICION FROM INF_CRITERIOS  INNER JOIN INF_CAMPOS ON (COD_TIPO=CODIGO) WHERE COD_SOLICITUD=? " +
                " AND INF_campos.ORIGEN=1 UNION SELECT ORIGEN " +
                "AS CAMPO, TABLA, VALOR1, VALOR2, CONDICION FROM INF_CRITERIOS CRI WHERE NOT EXISTS (SELECT * FROM" +
                " INF_CAMPOS WHERE CRI.COD_TIPO=CODIGO and origen=1) AND COD_SOLICITUD = ?";
        if(m_Log.isDebugEnabled()) m_Log.debug ("sqlCriteriosInforme = " + sqlCriteriosInforme);

        try {
            pstmt = conexion.prepareStatement(sqlCriteriosInforme);
            pstmt.setString(1, siVO.getCodSolicitud());
            pstmt.setString(2, siVO.getCodSolicitud());
            rs = pstmt.executeQuery();
            String aux = "";

            while (rs.next()){
                criterios.add(tratarCriterioSolicitud(abd, rs.getString("CAMPO"), rs.getString("TABLA"), rs.getString("VALOR1"),
                        rs.getString("VALOR2"), rs.getInt("CONDICION"), siVO.getProcedimiento()));
            }//while (rs.next())
            
            rs.close();
            pstmt.close();
            
            if (criterios.size()!=0){
                query += " WHERE EXISTS ";
                for (int i=0; i<criterios.size(); i++){
                    query += criterios.elementAt(i);
                    if (i<criterios.size()-1) query += " AND EXISTS ";
                }//for (int i=0; i<criterios.size(); i++)
                if (!"".equals(siVO.getProcedimiento()) && siVO.getProcedimiento()!=null) {
                    query += " AND CRO_PRO='" + siVO.getProcedimiento()+"'";
                }//if (!"".equals(siVO.getProcedimiento()) && siVO.getProcedimiento()!=null) 
            }else  if (!"".equals(siVO.getProcedimiento()) && siVO.getProcedimiento()!=null) {
                query += " WHERE CRO_PRO='" + siVO.getProcedimiento()+"'";
            }//if
            
            if(m_Log.isDebugEnabled()) m_Log.debug("Consulta para obtención de expedientes que cumplen los criterios: " + query);
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            if(m_Log.isDebugEnabled()) m_Log.error("ERROR OBTENIENDO CONSULTA EXPEDIENTES. obtieneConsultaExpedientesInforme ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("obtieneConsultaExpedientesInforme() : END");
        return query;
    }//obtieneConsultaExpedientesInforme

    private String tratarCriterioSolicitud (AdaptadorSQLBD abd, String campo, String tabla, String valor1,
            String valor2, int codCondicion, String procedimiento){
        if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : BEGIN");
        String sqlGeneral = "(SELECT EXP_NUM FROM V_EXP_INF WHERE  V_EXP_INF.EXP_PRO_COD='"+procedimiento+"' AND CRONO.CRO_NUM=EXP_NUM AND ";
        String sqlTER = "(SELECT EXP_NUM FROM V_EXP_INT_INF INNER JOIN T_HTE ON EXT_TER=HTE_TER WHERE V_EXP_INT_INF.EXP_PRO_COD='"+procedimiento+"'  AND EXP_NUM=CRONO.CRO_NUM AND HTE_TER ";
        String sqlReg = "(SELECT NUM_EXPEDIENTE FROM V_REG_INICIO_EXP WHERE V_REG_INICIO_EXP.NUM_EXPEDIENTE=CRONO.CRO_NUM AND ";
        String nulo = " IS NULL ";
        String noNulo = " IS NOT NULL ";
        String condicion = "";
        String campoTramite = "";
        String codTramite ="";

        try{
            codTramite = campo.split("_")[1];
            campoTramite = campo.split("_")[2];
        } catch (ArrayIndexOutOfBoundsException aibe){
            campoTramite = "";
        }//try-catch

        if (codCondicion==0 || codCondicion==1 || codCondicion==2 || codCondicion==3 || codCondicion==5){
            if (codCondicion == 0) condicion = ">";
            if (codCondicion == 1) condicion = ">=";
            if (codCondicion == 2) condicion = "<";
            if (codCondicion == 3) condicion = "<=";
            if (codCondicion == 5) condicion = "=";
            if (tabla.equals("E_TXT")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                        "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                        "E_TXT WHERE TXT_COD=PCA_COD AND TXT_NUM=CRO_NUM AND " +
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"TXT_VALOR"}) +
                        condicion + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) +
                        "))";
            }//if (tabla.equals("E_TXT"))
            if (tabla.equals("E_TXTT")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                        "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TXTT WHERE TXTT_COD=TCA_COD AND TXTT_NUM=CRO_NUM AND " +
                        "TXTT_TRA=CRO_TRA AND TXTT_TRA=" + codTramite +" AND " +
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"TXTT_VALOR"}) +
                        condicion + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) +
                        ")) AND CRONO.CRO_NUM=CRO_NUM)";
            }//if (tabla.equals("E_TXTT"))
            if (tabla.equals("E_TNU")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                        "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                        "E_TNU WHERE TNU_COD=PCA_COD AND TNU_NUM=CRO_NUM AND TNU_VALOR" + condicion + valor1 + "))";
            }//if (tabla.equals("E_TNU"))
            if (tabla.equals("E_TNUC")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                        "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                        "E_TNUC WHERE TNUC_COD=PCA_COD AND TNUC_NUM=CRO_NUM AND TNUC_VALOR" + condicion + valor1 + "))";
            }//if (tabla.equals("E_TNUC"))
            if (tabla.equals("E_TNUT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                        "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TNUT WHERE TNUT_COD=TCA_COD AND TNUT_NUM=CRO_NUM AND " +
                        "TNUT_TRA=CRO_TRA  AND TNUT_TRA=" + codTramite +" AND TNUT_VALOR" + condicion + valor1 + "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if (tabla.equals("E_TNUT"))
            if (tabla.equals("E_TNUCT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                        "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TNUCT WHERE TNUCT_COD=TCA_COD AND TNUCT_NUM=CRO_NUM AND " +
                        "TNUCT_TRA=CRO_TRA  AND TNUCT_TRA=" + codTramite +" AND TNUCT_VALOR" + condicion + valor1 + "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if (tabla.equals("E_TNUCT"))
            if (tabla.equals("E_TFE")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                        "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                        "E_TFE WHERE TFE_COD=PCA_COD AND TFE_NUM=CRO_NUM AND TFE_VALOR" +
                        condicion + abd.convertir("'"+valor1+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") +
                        "))";
            }//if (tabla.equals("E_TFE"))
            if (tabla.equals("E_TFET")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                        "' AND TCA_COD='" + campoTramite + "' AND ( EXISTS (SELECT * FROM E_TFET WHERE TFET_COD=TCA_COD AND TFET_NUM=CRO_NUM AND " +
                        "TFET_TRA=CRO_TRA AND TFET_TRA=" + codTramite +" AND TFET_VALOR" +
                        condicion + abd.convertir("'"+valor1+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") +
                        "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if (tabla.equals("E_TFET"))
            if (tabla.equals("E_TFEC")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                        "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                        "E_TFEC WHERE TFEC_COD=PCA_COD AND TFEC_NUM=CRO_NUM AND TFEC_VALOR" +
                        condicion + abd.convertir("'"+valor1+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") +
                        "))";
            }//if (tabla.equals("E_TFEC"))
            if (tabla.equals("E_TFECT")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                        "' AND TCA_COD='" + campoTramite + "' AND ( EXISTS (SELECT * FROM E_TFECT WHERE TFECT_COD=TCA_COD AND TFECT_NUM=CRO_NUM AND " +
                        "TFECT_TRA=CRO_TRA AND TFECT_TRA=" + codTramite +" AND TFECT_VALOR" +
                        condicion + abd.convertir("'"+valor1+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") +
                        "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if (tabla.equals("E_TFECT"))
            if (tabla.equals("E_TDE")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                        "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND  EXISTS (SELECT * FROM " +
                        "E_TDE WHERE TDE_COD=PCA_COD AND TDE_NUM=CRO_NUM AND " +
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"TDE_VALOR"}) +
                        condicion + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) +
                        "))";
            }//if (tabla.equals("E_TDE"))
            if (tabla.equals("E_TDET")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END"); 
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                        "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TDET WHERE TDET_COD=TCA_COD AND TDET_NUM=CRO_NUM AND " +
                        "TDET_TRA=CRO_TRA AND TDET_TRA=" + codTramite + " AND " +
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"TDET_VALOR"}) +
                        condicion + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) +
                        "))AND CRONO.CRO_NUM=CRO_NUM AND CRONO.CRO_TRA=CRO_TRA)";
            }//if (tabla.equals("E_TDET"))
            if (tabla.equals("E_TDEX")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                        "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND  EXISTS (SELECT * FROM " +
                        "E_TDEX WHERE TDEX_COD=PCA_COD AND TDEX_NUM=CRO_NUM AND " +
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"TDEX_VALOR"}) +
                        condicion + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) +
                        "))";
            }//if (tabla.equals("E_TDEX"))
            if (tabla.equals("E_TDEXT")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END"); 
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                        "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TDEXT WHERE TDEXT_COD=TCA_COD AND TDEXT_NUM=CRO_NUM AND " +
                        "TDEXT_TRA=CRO_TRA AND TDEXT_TRA=" + codTramite + " AND " +
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"TDEXT_VALOR"}) +
                        condicion + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) +
                        "))AND CRONO.CRO_NUM=CRO_NUM AND CRONO.CRO_TRA=CRO_TRA)";
            }//if (tabla.equals("E_TDEXT"))
            if (tabla.equals("V_EXP_INF")){
                if (campo.equals("EXP_FEI") || campo.equals("EXP_FEF"))
                    return sqlGeneral + "(" + campo + condicion +
                        abd.convertir("'" + valor1 + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "))";
                if (campo.startsWith("TRA_")) return sqlGeneral + " CRONO.CRO_TRA=TRA_COD AND (" + campo + condicion +
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) + "))";
                    return sqlGeneral + "(" + campo + condicion +
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) + "))";
            }//if (tabla.equals("V_EXP_INF"))
            if (tabla.equals("V_EXP_INT_INF")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sqlTER + condicion + valor2 + ")";
            }//if (tabla.equals("V_EXP_INT_INF"))
            
             if (tabla.equals("V_REG_INICIO_EXP")){
                 if (campo.equals("FECHA_PRESENTACION")){
                    if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                     return sqlReg + "(" + campo + condicion +
                        abd.convertir("'" + valor1 + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + "))";
                 }else{ 
                     if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                     return sqlReg + campo + condicion +"'"+ valor1 + "')";
                 }//if
            }//if (tabla.equals("V_REG_INICIO_EXP"))
             
            if(tabla.equals("T_CAMPOS_DESPLEGABLE")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                    .append(" From E_EXT,T_CAMPOS_DESPLEGABLE")
                    .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                    .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                    .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                    .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                    .append(" and E_EXT.EXT_TER = T_CAMPOS_DESPLEGABLE.COD_TERCERO")
                    .append(" and T_CAMPOS_DESPLEGABLE.COD_CAMPO = '" + campo + "'")
                    .append(" and T_CAMPOS_DESPLEGABLE.COD_MUNICIPIO = CRONO.CRO_MUN")
                    .append(" and T_CAMPOS_DESPLEGABLE.VALOR " + condicion + " '" + valor1 + "')");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_DESPLEGABLE"))
            if(tabla.equals("T_CAMPOS_FECHA")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                    .append(" From E_EXT,T_CAMPOS_FECHA")
                    .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                    .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                    .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                    .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                    .append(" and E_EXT.EXT_TER = T_CAMPOS_FECHA.COD_TERCERO")
                    .append(" and T_CAMPOS_FECHA.COD_CAMPO = '" + campo + "'")
                    .append(" and T_CAMPOS_FECHA.COD_MUNICIPIO = CRONO.CRO_MUN")
                    .append(" and T_CAMPOS_FECHA.VALOR " + condicion + " " + 
                        abd.convertir("'"+valor1+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") + ")");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_FECHA"))
            if(tabla.equals("T_CAMPOS_TEXTO")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                    .append(" From E_EXT,T_CAMPOS_TEXTO")
                    .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                    .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                    .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                    .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                    .append(" and E_EXT.EXT_TER = T_CAMPOS_TEXTO.COD_TERCERO")
                    .append(" and T_CAMPOS_TEXTO.COD_CAMPO = '" + campo + "'")
                    .append(" and T_CAMPOS_TEXTO.COD_MUNICIPIO = CRONO.CRO_MUN")
                    .append(" and " + 
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"T_CAMPOS_TEXTO.VALOR"}) +
                        condicion + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) + ")");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_TEXTO"))
            if(tabla.equals("T_CAMPOS_TEXTO_LARGO")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                    .append(" From E_EXT,T_CAMPOS_TEXTO_LARGO")
                    .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                    .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                    .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                    .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                    .append(" and E_EXT.EXT_TER = T_CAMPOS_TEXTO_LARGO.COD_TERCERO")
                    .append(" and T_CAMPOS_TEXTO_LARGO.COD_CAMPO = '" + campo + "'")
                    .append(" and T_CAMPOS_TEXTO_LARGO.COD_MUNICIPIO = CRONO.CRO_MUN")
                    .append(" and " + 
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"T_CAMPOS_TEXTO_LARGO.VALOR"}) +
                        condicion + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) + ")");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_TEXTO_LARGO"))
            if(tabla.equals("T_CAMPOS_NUMERICO")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                    .append(" From E_EXT,T_CAMPOS_NUMERICO")
                    .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                    .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                    .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                    .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                    .append(" and E_EXT.EXT_TER = T_CAMPOS_NUMERICO.COD_TERCERO")
                    .append(" and T_CAMPOS_NUMERICO.COD_CAMPO = '" + campo + "'")
                    .append(" and T_CAMPOS_NUMERICO.COD_MUNICIPIO = CRONO.CRO_MUN")
                    .append(" and T_CAMPOS_NUMERICO.VALOR " + condicion + " " + valor1 + ")");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_NUMERICO"))
        } else if (codCondicion ==4){
            if (tabla.equals("E_TNU")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                        "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                        "E_TNU WHERE TNU_COD=PCA_COD AND TNU_NUM=CRO_NUM AND TNU_VALOR BETWEEN " +
                        valor1 + " AND " + valor2 + "))";
            }//if (tabla.equals("E_TNU"))
            if (tabla.equals("E_TNUC")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                        "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                        "E_TNUC WHERE TNUC_COD=PCA_COD AND TNUC_NUM=CRO_NUM AND TNUC_VALOR BETWEEN " +
                        valor1 + " AND " + valor2 + "))";
            }//if (tabla.equals("E_TNUC"))
            if (tabla.equals("E_TNUT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                        "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TNUT WHERE TNUT_COD=TCA_COD AND TNUT_NUM=CRO_NUM AND " +
                        "TNUT_TRA=CRO_TRA AND TNUT_TRA=" + codTramite + " AND TNUT_VALOR BETWEEN "  + valor1 + " AND " + valor2 +
                        "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if (tabla.equals("E_TNUCT"))
            if (tabla.equals("E_TNUCT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                        "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TNUCT WHERE TNUCT_COD=TCA_COD AND TNUCT_NUM=CRO_NUM AND " +
                        "TNUCT_TRA=CRO_TRA AND TNUCT_TRA=" + codTramite + " AND TNUCT_VALOR BETWEEN "  + valor1 + " AND " + valor2 +
                        "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if (tabla.equals("E_TNUCT"))
            if (tabla.equals("E_TFE")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                        "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                        "E_TFE WHERE TFE_COD=PCA_COD AND TFE_NUM=CRO_NUM AND TFE_VALOR BETWEEN " +
                        abd.convertir("'"+valor1+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                        abd.convertir("'"+valor2+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") +
                        "))";
            }//if (tabla.equals("E_TFE"))
            if (tabla.equals("E_TFET")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                        "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TFET WHERE TFET_COD=TCA_COD AND TFET_NUM=CRO_NUM AND " +
                        "TFET_TRA=CRO_TRA AND TFET_TRA=" + codTramite + " AND TFET_VALOR BETWEEN" +
                        abd.convertir("'"+valor1+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                        abd.convertir("'"+valor2+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") +
                        "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if (tabla.equals("E_TFET"))
            if (tabla.equals("E_TFEC")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                        "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                        "E_TFEC WHERE TFEC_COD=PCA_COD AND TFEC_NUM=CRO_NUM AND TFEC_VALOR BETWEEN " +
                        abd.convertir("'"+valor1+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                        abd.convertir("'"+valor2+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") +
                        "))";
            }//if (tabla.equals("E_TFEC"))
            if (tabla.equals("E_TFECT")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                        "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TFECT WHERE TFECT_COD=TCA_COD AND TFECT_NUM=CRO_NUM AND " +
                        "TFECT_TRA=CRO_TRA AND TFECT_TRA=" + codTramite + " AND TFECT_VALOR BETWEEN" +
                        abd.convertir("'"+valor1+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                        abd.convertir("'"+valor2+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") +
                        "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if (tabla.equals("E_TFECT"))
            if (tabla.equals("V_EXP_INF")){
                if (campo.startsWith("TRA_")){
                    if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                    return sqlGeneral + " CRONO.CRO_TRA=TRA_COD AND " + campo + " BETWEEN " + "'" + valor1 + "' AND '" + valor2+ "')";
                }//if
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sqlGeneral + campo + " BETWEEN " + "'" + valor1 + "' AND '" + valor2+ "')";
            }//if (tabla.equals("V_EXP_INF"))
            
            if (tabla.equals("V_REG_INICIO_EXP")){     
                if (campo.equals("FECHA_PRESENTACION")){
                    if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                    return sqlReg +  campo + " BETWEEN " +
                        abd.convertir("'"+valor1+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                        abd.convertir("'"+valor2+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") + ")" ;
                }else{
                    if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                    return sqlReg +  campo + " BETWEEN " + "'" + valor1 + "' AND '" + valor2+ "')";                   
                }//if
             }//if
            
            if(tabla.equals("T_CAMPOS_FECHA")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_FECHA")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_FECHA.COD_TERCERO")
                   .append(" and T_CAMPOS_FECHA.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_FECHA.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and T_CAMPOS_FECHA.VALOR BETWEEN " + 
                        abd.convertir("'"+valor1+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                        abd.convertir("'"+valor2+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") +")");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_FECHA"))
            if(tabla.equals("T_CAMPOS_NUMERICO")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_NUMERICO")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_NUMERICO.COD_TERCERO")
                   .append(" and T_CAMPOS_NUMERICO.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_NUMERICO.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and T_CAMPOS_NUMERICO.VALOR BETWEEN " + 
                        abd.convertir("'"+valor1+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                        abd.convertir("'"+valor2+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") +")");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_NUMERICO"))
        } else if (codCondicion == 6){
            /*
             * Cuando el criterio es <>, las consultas de datos suplementarios se vuelven algo mas complejas, debido a que se
             * debe consultar que el valor sea diferente o que, simplemente, ese valor no exista.
             */
            if (tabla.equals("E_TXT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRO_PRO='"+ procedimiento + "' AND " +
                       "PCA_COD='" + campo + "' AND (NOT EXISTS (SELECT * FROM E_TXT WHERE TXT_COD=PCA_COD AND TXT_NUM=CRO_NUM ) " +
                       "OR EXISTS(SELECT * FROM E_TXT WHERE TXT_COD=PCA_COD AND TXT_NUM=CRO_NUM AND " +
                       abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"TXT_VALOR"}) +
                       "<>" +abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'" + valor1 + "'"}) +
                       "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if (tabla.equals("E_TXT"))
            if (tabla.equals("E_TXTT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO WHERE CRO_TRA=TCA_TRA AND CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (NOT EXISTS (SELECT * FROM E_TXTT WHERE TXTT_COD=TCA_COD AND TXTT_NUM=CRO_NUM AND " +
                       "TXTT_TRA=CRO_TRA) OR EXISTS (SELECT * FROM E_TXTT WHERE TXTT_COD=TCA_COD AND TXTT_NUM=CRO_NUM AND TXTT_TRA=CRO_TRA AND " +
                       abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"TXTT_VALOR"})  + "<>" +
                       abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) + "))AND CRONO.CRO_NUM=CRO_NUM)" +
                       "OR NOT EXISTS (SELECT * FROM E_CRO WHERE CRO_NUM=CRONO.CRO_NUM AND CRO_TRA=" + codTramite + ")";
            }//if (tabla.equals("E_TXTT"))
            if (tabla.equals("E_TNU")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE  CRO_PRO='"+ procedimiento + "' AND " +
                       "PCA_COD='" + campo + "' AND (NOT EXISTS (SELECT * FROM E_TNU WHERE TNU_COD=PCA_COD AND TNU_NUM=CRO_NUM ) " +
                       "OR EXISTS(SELECT * FROM E_TNU WHERE TNU_COD=PCA_COD AND TNU_NUM=CRO_NUM AND TNU_VALOR<>" +
                       abd.convertir(valor1,AdaptadorSQLBD.CONVERTIR_COLUMNA_NUMERO, null)+
                       "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if (tabla.equals("E_TNU"))
            if (tabla.equals("E_TNUT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO WHERE CRO_TRA=TCA_TRA AND CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (NOT EXISTS (SELECT * FROM E_TNUT WHERE TNUT_COD=TCA_COD AND TNUT_NUM=CRO_NUM AND " +
                       "TNUT_TRA=CRO_TRA) OR EXISTS (SELECT * FROM E_TNUT WHERE TNUT_COD=TCA_COD AND TNUT_NUM=CRO_NUM AND TNUT_TRA=CRO_TRA AND TNUT_VALOR<>" +
                       abd.convertir(valor1,AdaptadorSQLBD.CONVERTIR_COLUMNA_NUMERO, null) + "))AND CRONO.CRO_NUM=CRO_NUM) " +
                       "OR NOT EXISTS (SELECT * FROM E_CRO WHERE CRO_NUM=CRONO.CRO_NUM AND CRO_TRA=" + codTramite + ")";
            }//if (tabla.equals("E_TNUT"))
            if (tabla.equals("E_TNUC")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE  CRO_PRO='"+ procedimiento + "' AND " +
                       "PCA_COD='" + campo + "' AND (NOT EXISTS (SELECT * FROM E_TNUC WHERE TNUC_COD=PCA_COD AND TNUC_NUM=CRO_NUM ) " +
                       "OR EXISTS(SELECT * FROM E_TNUC WHERE TNUC_COD=PCA_COD AND TNUC_NUM=CRO_NUM AND TNUC_VALOR<>" +
                       abd.convertir(valor1,AdaptadorSQLBD.CONVERTIR_COLUMNA_NUMERO, null)+
                       "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if (tabla.equals("E_TNUC"))
            if (tabla.equals("E_TNUCT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO WHERE CRO_TRA=TCA_TRA AND CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (NOT EXISTS (SELECT * FROM E_TNUCT WHERE TNUCT_COD=TCA_COD AND TNUCT_NUM=CRO_NUM AND " +
                       "TNUCT_TRA=CRO_TRA) OR EXISTS (SELECT * FROM E_TNUCT WHERE TNUCT_COD=TCA_COD AND TNUCT_NUM=CRO_NUM AND TNUCT_TRA=CRO_TRA AND TNUCT_VALOR<>" +
                       abd.convertir(valor1,AdaptadorSQLBD.CONVERTIR_COLUMNA_NUMERO, null) + "))AND CRONO.CRO_NUM=CRO_NUM) " +
                       "OR NOT EXISTS (SELECT * FROM E_CRO WHERE CRO_NUM=CRONO.CRO_NUM AND CRO_TRA=" + codTramite + ")";
            }//if (tabla.equals("E_TNUCT"))
            if (tabla.equals("E_TDE")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRO_PRO='"+ procedimiento + "' AND " +
                       "PCA_COD='" + campo + "' AND (NOT EXISTS (SELECT * FROM E_TDE WHERE TDE_COD=PCA_COD AND TDE_NUM=CRO_NUM ) " +
                       "OR EXISTS(SELECT * FROM E_TDE WHERE TDE_COD=PCA_COD AND TDE_NUM=CRO_NUM AND " +
                       abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"TDE_VALOR"}) +
                       "<>" +abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'" + valor1 + "'"}) +
                       "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if (tabla.equals("E_TDE"))
            if (tabla.equals("E_TDET")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO WHERE CRO_TRA=TCA_TRA AND CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (NOT EXISTS (SELECT * FROM E_TDET WHERE TDET_COD=TCA_COD AND TDET_NUM=CRO_NUM AND " +
                       "TDET_TRA=CRO_TRA) OR EXISTS (SELECT * FROM E_TDET WHERE TDET_COD=TCA_COD AND TDET_NUM=CRO_NUM AND TDET_TRA=CRO_TRA AND " +
                       abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"TDET_VALOR"}) + "<>" +
                       abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"})+ "))" +
                       "AND CRONO.CRO_NUM=CRO_NUM AND CRONO.CRO_TRA=CRO_TRA)";
            }//if (tabla.equals("E_TDET"))
            if (tabla.equals("E_TDEX")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRO_PRO='"+ procedimiento + "' AND " +
                       "PCA_COD='" + campo + "' AND (NOT EXISTS (SELECT * FROM E_TDEX WHERE TDEX_COD=PCA_COD AND TDEX_NUM=CRO_NUM ) " +
                       "OR EXISTS(SELECT * FROM E_TDEX WHERE TDEX_COD=PCA_COD AND TDEX_NUM=CRO_NUM AND " +
                       abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"TDEX_VALOR"}) +
                       "<>" +abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'" + valor1 + "'"}) +
                       "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if (tabla.equals("E_TDEX"))
            if (tabla.equals("E_TDEXT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO WHERE CRO_TRA=TCA_TRA AND CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (NOT EXISTS (SELECT * FROM E_TDEXT WHERE TDEXT_COD=TCA_COD AND TDEXT_NUM=CRO_NUM AND " +
                       "TDEXT_TRA=CRO_TRA) OR EXISTS (SELECT * FROM E_TDEXT WHERE TDEXT_COD=TCA_COD AND TDEXT_NUM=CRO_NUM AND TDEXT_TRA=CRO_TRA AND " +
                       abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"TDEXT_VALOR"}) + "<>" +
                       abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"})+ "))" +
                       "AND CRONO.CRO_NUM=CRO_NUM AND CRONO.CRO_TRA=CRO_TRA)";
            }//if (tabla.equals("E_TDEXT"))
            if (tabla.equals("V_EXP_INF")){
                if (campo.startsWith("TRA_")){
                    if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                    return sqlGeneral + " CRONO.CRO_TRA=TRA_COD AND(" + campo + " IS NULL OR " + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{campo}) +
                        "<>" + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) + "))";
                }//if (campo.startsWith("TRA_"))
                return sqlGeneral + "(" + campo + "<>" + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) + "))";
            }//if (tabla.equals("V_EXP_INF"))
            if (tabla.equals("V_EXP_INT_INF")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT INT_NUM FROM V_INT INNER JOIN T_HTE ON INT_TER=HTE_TER WHERE INT_NUM=CRONO.CRO_NUM AND " +
                       "INT_NOC<>'" + valor1 + "' OR NOT EXISTS(SELECT INT_TER FROM V_INT INT2 WHERE INT_NUM=CRONO.CRO_NUM))";
            }//if (tabla.equals("V_EXP_INT_INF"))
          
             if (tabla.equals("V_REG_INICIO_EXP")){
              
                  return sqlReg +  campo + " IS NULL OR " + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{campo}) +
                        "<>" + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) + ")";
                                   
            }//if (tabla.equals("V_REG_INICIO_EXP"))
            if(tabla.equals("T_CAMPOS_DESPLEGABLE")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_DESPLEGABLE")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_DESPLEGABLE.COD_TERCERO")
                   .append(" and T_CAMPOS_DESPLEGABLE.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_DESPLEGABLE.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and " +
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"T_CAMPOS_DESPLEGABLE.VALOR"}) +
                        " <> " + 
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"})
                        + ")");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_DESPLEGABLE"))
            if(tabla.equals("T_CAMPOS_TEXTO")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_TEXTO")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_TEXTO.COD_TERCERO")
                   .append(" and T_CAMPOS_TEXTO.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_TEXTO.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and " +
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"T_CAMPOS_TEXTO.VALOR"}) +
                        " <> " + 
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"})
                        + ")");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_TEXTO"))
            if(tabla.equals("T_CAMPOS_TEXTO_LARGO")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_TEXTO_LARGO")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_TEXTO_LARGO.COD_TERCERO")
                   .append(" and T_CAMPOS_TEXTO_LARGO.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_TEXTO_LARGO.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and " +
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"T_CAMPOS_TEXTO_LARGO.VALOR"}) +
                        " <> " + 
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"})
                        + ")");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_TEXTO_LARGO"))
            if(tabla.equals("T_CAMPOS_NUMERICO")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_NUMERICO")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_NUMERICO.COD_TERCERO")
                   .append(" and T_CAMPOS_NUMERICO.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_NUMERICO.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and " +
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"T_CAMPOS_NUMERICO.VALOR"}) +
                        " <> " + 
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"})
                        + ")");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_TEXTO_LARGO"))
        } else if (codCondicion == 7){
            valor1=valor1.replace('*','%');
            if (tabla.equals("E_TXT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                       "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                       "E_TXT WHERE TXT_COD=PCA_COD AND TXT_NUM=CRO_NUM AND " +
                       abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"TXT_VALOR"}) +
                       " LIKE " + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) +
                       "))";
            }//if
            if (tabla.equals("E_TXTT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TXTT WHERE TXTT_COD=TCA_COD AND " +
                       "TXTT_NUM=CRO_NUM AND TXTT_TRA=CRO_TRA AND TXTT_TRA=" + codTramite + " AND " +
                       abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"TXTT_VALOR"}) +
                       " LIKE " + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) +
                       ")) AND CRONO.CRO_NUM=CRO_NUM)";
            }//if
            if (tabla.equals("V_EXP_INF")){
                if (campo.startsWith("TRA_")){
                    if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                    return sqlGeneral + " CRONO.CRO_TRA=TRA_COD AND " + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{campo}) +
                        " LIKE " + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) + ")";
                
                }else{
                    if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                    return sqlGeneral + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{campo}) +
                        " LIKE " + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) + ")";
            
                }//if (campo.startsWith("TRA_"))
            }//if (tabla.equals("V_EXP_INF"))
            
            if (tabla.equals("V_REG_INICIO_EXP")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sqlReg + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{campo}) +
                    " LIKE " + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"}) + ")";
            }//if
            
            if(tabla.equals("T_CAMPOS_TEXTO")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_TEXTO")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_TEXTO.COD_TERCERO")
                   .append(" and T_CAMPOS_TEXTO.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_TEXTO.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and " +
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"T_CAMPOS_TEXTO.VALOR"}) +
                        " LIKE " + 
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"})
                        + ")");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_TEXTO"))
            if(tabla.equals("T_CAMPOS_TEXTO_LARGO")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_TEXTO_LARGO")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_TEXTO_LARGO.COD_TERCERO")
                   .append(" and T_CAMPOS_TEXTO_LARGO.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_TEXTO_LARGO.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and " +
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"T_CAMPOS_TEXTO_LARGO.VALOR"}) +
                        " LIKE " + 
                        abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+valor1+"'"})
                        + ")");
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return sql.toString();
            }//if(tabla.equals("T_CAMPOS_TEXTO_LARGO"))
        } else if (codCondicion == 8){
            if (tabla.equals("E_TXT")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                      "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND NOT EXISTS (SELECT * FROM " +
                      "E_TXT WHERE TXT_COD=PCA_COD AND TXT_NUM=CRO_NUM ))";
            }//if
            if (tabla.equals("E_TXTT")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                      "' AND TCA_COD='" + campoTramite + "' AND (NOT EXISTS (SELECT * FROM E_TXTT WHERE TXTT_COD=TCA_COD AND TXTT_NUM=CRO_NUM AND " +
                      "TXTT_TRA=CRO_TRA)) AND CRONO.CRO_NUM=CRO_NUM) OR NOT EXISTS (SELECT * FROM E_CRO WHERE CRO_NUM=CRONO.CRO_NUM " +
                      "AND CRO_TRA=" + codTramite + ")";
            }//if
            if (tabla.equals("E_TNU")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                      "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND NOT EXISTS (SELECT * FROM " +
                      "E_TNU WHERE TNU_COD=PCA_COD AND TNU_NUM=CRO_NUM ))";
            }//if
            if (tabla.equals("E_TNUT")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                      "' AND TCA_COD='" + campoTramite + "' AND (NOT EXISTS (SELECT * FROM E_TNUT WHERE TNUT_COD=TCA_COD AND TNUT_NUM=CRO_NUM AND " +
                      "TNUT_TRA=CRO_TRA))AND CRONO.CRO_NUM=CRO_NUM) OR NOT EXISTS (SELECT * FROM E_CRO WHERE CRO_NUM=CRONO.CRO_NUM " +
                      "AND CRO_TRA=" + codTramite + ")";
            }//if
            if (tabla.equals("E_TNUC")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                      "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND NOT EXISTS (SELECT * FROM " +
                      "E_TNUC WHERE TNUC_COD=PCA_COD AND TNUC_NUM=CRO_NUM ))";
            }//if
            if (tabla.equals("E_TNUCT")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                      "' AND TCA_COD='" + campoTramite + "' AND (NOT EXISTS (SELECT * FROM E_TNUCT WHERE TNUCT_COD=TCA_COD AND TNUCT_NUM=CRO_NUM AND " +
                      "TNUCT_TRA=CRO_TRA))AND CRONO.CRO_NUM=CRO_NUM) OR NOT EXISTS (SELECT * FROM E_CRO WHERE CRO_NUM=CRONO.CRO_NUM " +
                      "AND CRO_TRA=" + codTramite + ")";
            }//if
            if (tabla.equals("E_TFE")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                      "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND NOT EXISTS (SELECT * FROM " +
                      "E_TFE WHERE TFE_COD=PCA_COD AND TFE_NUM=CRO_NUM ))";
            }//if
            if (tabla.equals("E_TFET")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (NOT EXISTS (SELECT * FROM E_TFET WHERE TFET_COD=TCA_COD AND TFET_NUM=CRO_NUM AND " +
                       "TFET_TRA=CRO_TRA))AND CRONO.CRO_NUM=CRO_NUM) OR NOT EXISTS (SELECT * FROM E_CRO WHERE CRO_NUM=CRONO.CRO_NUM " +
                       "AND CRO_TRA=" + codTramite + ")";
            }//if
            if (tabla.equals("E_TFEC")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                      "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND NOT EXISTS (SELECT * FROM " +
                      "E_TFEC WHERE TFEC_COD=PCA_COD AND TFEC_NUM=CRO_NUM ))";
            }//if
            if (tabla.equals("E_TFECT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (NOT EXISTS (SELECT * FROM E_TFECT WHERE TFECT_COD=TCA_COD AND TFECT_NUM=CRO_NUM AND " +
                       "TFECT_TRA=CRO_TRA))AND CRONO.CRO_NUM=CRO_NUM) OR NOT EXISTS (SELECT * FROM E_CRO WHERE CRO_NUM=CRONO.CRO_NUM " +
                       "AND CRO_TRA=" + codTramite + ")";
            }//if
            if (tabla.equals("E_TDE")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                       "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND NOT EXISTS (SELECT * FROM " +
                       "E_TDE WHERE TDE_COD=PCA_COD AND TDE_NUM=CRO_NUM ))";
            }//if
            if (tabla.equals("E_TDET")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (NOT EXISTS (SELECT * FROM E_TDET WHERE TDET_COD=TCA_COD AND TDET_NUM=CRO_NUM AND " +
                       "TDET_TRA=CRO_TRA))AND CRONO.CRO_NUM=CRO_NUM AND CRONO.CRO_TRA=CRO_TRA) OR NOT EXISTS (SELECT * FROM E_CRO WHERE CRO_NUM=CRONO.CRO_NUM " +
                       "AND CRO_TRA=" + codTramite + ")";
            }//if
            if (tabla.equals("E_TDEX")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                       "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND NOT EXISTS (SELECT * FROM " +
                       "E_TDEX WHERE TDEX_COD=PCA_COD AND TDEX_NUM=CRO_NUM ))";
            }//if
            if (tabla.equals("E_TDEXT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (NOT EXISTS (SELECT * FROM E_TDEXT WHERE TDEXT_COD=TCA_COD AND TDEXT_NUM=CRO_NUM AND " +
                       "TDEXT_TRA=CRO_TRA))AND CRONO.CRO_NUM=CRO_NUM AND CRONO.CRO_TRA=CRO_TRA) OR NOT EXISTS (SELECT * FROM E_CRO WHERE CRO_NUM=CRONO.CRO_NUM " +
                       "AND CRO_TRA=" + codTramite + ")";
            }//if
            if (tabla.equals("V_EXP_INF")){
                if (campo.startsWith("TRA_"))
                    return sqlGeneral + " CRONO.CRO_TRA=TRA_COD AND " + campo + nulo + ")";
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sqlGeneral + campo + nulo + ")";
            }//if
            if (tabla.equals("V_EXP_INT_INF")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT EXP_NUM FROM V_EXP_INT_INF WHERE  NOT EXISTS(SELECT INT_TER FROM V_INT WHERE INT_NUM=CRONO.CRO_NUM))";
            }//if
            if (tabla.equals("V_REG_INICIO_EXP")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sqlReg + campo + nulo + ")";
            }//if (tabla.equals("V_REG_INICIO_EXP"))
            
            if(tabla.equals("T_CAMPOS_DESPLEGABLE")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_DESPLEGABLE")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_DESPLEGABLE.COD_TERCERO")
                   .append(" and T_CAMPOS_DESPLEGABLE.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_DESPLEGABLE.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and T_CAMPOS_DESPLEGABLE.VALOR IS NULL) ");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_DESPLEGABLE"))
            if(tabla.equals("T_CAMPOS_FECHA")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_FECHA")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_FECHA.COD_TERCERO")
                   .append(" and T_CAMPOS_FECHA.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_FECHA.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and T_CAMPOS_FECHA.VALOR IS NULL) ");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_FECHA"))
            if(tabla.equals("T_CAMPOS_TEXTO")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_TEXTO")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_TEXTO.COD_TERCERO")
                   .append(" and T_CAMPOS_TEXTO.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_TEXTO.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and T_CAMPOS_TEXTO.VALOR IS NULL) ");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_TEXTO"))
            if(tabla.equals("T_CAMPOS_TEXTO_LARGO")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_TEXTO_LARGO")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_TEXTO_LARGO.COD_TERCERO")
                   .append(" and T_CAMPOS_TEXTO_LARGO.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_TEXTO_LARGO.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and T_CAMPOS_TEXTO_LARGO.VALOR IS NULL) ");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_TEXTO_LARGO"))
            if(tabla.equals("T_CAMPOS_NUMERICO")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_NUMERICO")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_NUMERICO.COD_TERCERO")
                   .append(" and T_CAMPOS_NUMERICO.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_NUMERICO.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and T_CAMPOS_NUMERICO.VALOR IS NULL) ");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_NUMERICO"))
        }else if (codCondicion == 9){
           if (tabla.equals("E_TXT")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                       "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                       "E_TXT WHERE TXT_COD=PCA_COD AND TXT_NUM=CRO_NUM ))";
            }//if
            if (tabla.equals("E_TXTT")){
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TXTT WHERE TXTT_COD=TCA_COD AND " +
                       "TXTT_NUM=CRO_NUM AND TXTT_TRA=CRO_TRA AND TXTT_TRA=" + codTramite + ")) AND CRONO.CRO_NUM=CRO_NUM)";
            }//if
            if (tabla.equals("E_TNU")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                       "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                       "E_TNU WHERE TNU_COD=PCA_COD AND TNU_NUM=CRO_NUM ))";
            }//if
            if (tabla.equals("E_TNUT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TNUT WHERE TNUT_COD=TCA_COD AND TNUT_NUM=CRO_NUM AND " +
                       "TNUT_TRA=CRO_TRA AND TNUT_TRA=" + codTramite + "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if
            if (tabla.equals("E_TNUC")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                       "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                       "E_TNUC WHERE TNUC_COD=PCA_COD AND TNUC_NUM=CRO_NUM ))";
            }//if
            if (tabla.equals("E_TNUCT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TNUCT WHERE TNUCT_COD=TCA_COD AND TNUCT_NUM=CRO_NUM AND " +
                       "TNUCT_TRA=CRO_TRA AND TNUCT_TRA=" + codTramite + "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if
            if (tabla.equals("E_TFE")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                       "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                       "E_TFE WHERE TFE_COD=PCA_COD AND TFE_NUM=CRO_NUM ))";
            }//if
            if (tabla.equals("E_TFET")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TFET WHERE TFET_COD=TCA_COD AND TFET_NUM=CRO_NUM AND " +
                       "TFET_TRA=CRO_TRA  AND TFET_TRA=" + codTramite + "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if
            if (tabla.equals("E_TFEC")){
               if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
               return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                       "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                       "E_TFEC WHERE TFEC_COD=PCA_COD AND TFEC_NUM=CRO_NUM ))";
            }//if
            if (tabla.equals("E_TFECT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TFECT WHERE TFECT_COD=TCA_COD AND TFECT_NUM=CRO_NUM AND " +
                       "TFECT_TRA=CRO_TRA  AND TFECT_TRA=" + codTramite + "))AND CRONO.CRO_NUM=CRO_NUM)";
            }//if
            if (tabla.equals("E_TDE")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                       "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                       "E_TDE WHERE TDE_COD=PCA_COD AND TDE_NUM=CRO_NUM ))";
            }//if
            if (tabla.equals("E_TDET")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TDET WHERE TDET_COD=TCA_COD AND TDET_NUM=CRO_NUM AND " +
                       "TDET_TRA=CRO_TRA AND TDET_TRA=" + codTramite + "))AND CRONO.CRO_NUM=CRO_NUM AND CRONO.CRO_TRA=CRO_TRA)";
            }//if
            if (tabla.equals("E_TDEX")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_PCA ON PCA_PRO=CRO_PRO WHERE CRONO.CRO_NUM=CRO_NUM and " +
                       "CRO_PRO='" + procedimiento + "'  AND PCA_COD='" + campo + "' AND EXISTS (SELECT * FROM " +
                       "E_TDEX WHERE TDEX_COD=PCA_COD AND TDEX_NUM=CRO_NUM ))";
            }//if
            if (tabla.equals("E_TDEXT")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT CRO_NUM FROM E_CRO INNER JOIN E_TCA ON TCA_PRO=CRO_PRO AND CRO_TRA=TCA_TRA WHERE CRO_PRO='" + procedimiento +
                       "' AND TCA_COD='" + campoTramite + "' AND (EXISTS (SELECT * FROM E_TDEXT WHERE TDEXT_COD=TCA_COD AND TDEXT_NUM=CRO_NUM AND " +
                       "TDEXT_TRA=CRO_TRA AND TDEXT_TRA=" + codTramite + "))AND CRONO.CRO_NUM=CRO_NUM AND CRONO.CRO_TRA=CRO_TRA)";
            }//if
            if (tabla.equals("V_EXP_INF")){
                if (campo.startsWith("TRA_"))
                    return sqlGeneral + " CRONO.CRO_TRA=TRA_COD AND " + campo + noNulo + ")";
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sqlGeneral + campo + noNulo + ")";
            }//if
            if (tabla.equals("V_EXP_INT_INF")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return "(SELECT EXP_NUM FROM V_EXP_INT_INF WHERE EXP_NUM=CRONO.CRO_NUM AND EXT_TER IS NOT NULL)";
            }//if
            if (tabla.equals("V_REG_INICIO_EXP")){
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sqlReg + campo + noNulo + ")";
            }//if
             
            if(tabla.equals("T_CAMPOS_DESPLEGABLE")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_DESPLEGABLE")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_DESPLEGABLE.COD_TERCERO")
                   .append(" and T_CAMPOS_DESPLEGABLE.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_DESPLEGABLE.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and T_CAMPOS_DESPLEGABLE.VALOR IS NOT NULL) ");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_DESPLEGABLE"))
            if(tabla.equals("T_CAMPOS_FECHA")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                    .append(" From E_EXT,T_CAMPOS_FECHA")
                    .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                    .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                    .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                    .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                    .append(" and E_EXT.EXT_TER = T_CAMPOS_FECHA.COD_TERCERO")
                    .append(" and T_CAMPOS_FECHA.COD_CAMPO = '" + campo + "'")
                    .append(" and T_CAMPOS_FECHA.COD_MUNICIPIO = CRONO.CRO_MUN")
                    .append(" and T_CAMPOS_FECHA.VALOR IS NOT NULL) ");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_FECHA"))
            if(tabla.equals("T_CAMPOS_TEXTO")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_TEXTO")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_TEXTO.COD_TERCERO")
                   .append(" and T_CAMPOS_TEXTO.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_TEXTO.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and T_CAMPOS_TEXTO.VALOR IS NOT NULL) ");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_TEXTO"))
            if(tabla.equals("T_CAMPOS_TEXTO_LARGO")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_TEXTO_LARGO")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_TEXTO_LARGO.COD_TERCERO")
                   .append(" and T_CAMPOS_TEXTO_LARGO.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_TEXTO_LARGO.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and T_CAMPOS_TEXTO_LARGO.VALOR IS NOT NULL) ");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_TEXTO_LARGO"))
            if(tabla.equals("T_CAMPOS_NUMERICO")){
                StringBuffer sql = new StringBuffer();
                sql.append("(Select distinct (CRONO.CRO_NUM)")
                   .append(" From E_EXT,T_CAMPOS_NUMERICO")
                   .append(" where CRONO.CRO_MUN = E_EXT.EXT_MUN")
                   .append(" and CRONO.CRO_EJE = E_EXT.EXT_EJE")
                   .append(" and CRONO.CRO_NUM = E_EXT.EXT_NUM")
                   .append(" and CRONO.CRO_PRO ='" + procedimiento +"'")
                   .append(" and E_EXT.EXT_TER = T_CAMPOS_NUMERICO.COD_TERCERO")
                   .append(" and T_CAMPOS_NUMERICO.COD_CAMPO = '" + campo + "'")
                   .append(" and T_CAMPOS_NUMERICO.COD_MUNICIPIO = CRONO.CRO_MUN")
                   .append(" and T_CAMPOS_NUMERICO.VALOR IS NOT NULL) ");
                if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
                return sql.toString();
            }//if(tabla.equals("T_CAMPOS_NUMERICO"))
        }//if conCondicion
        if(m_Log.isDebugEnabled()) m_Log.debug("tratarCriterioSolicitud() : END");
        return "";
    }//tratarCriterioSolicitud

     private GeneralValueObject rellenaExpedientesTablaTemporal2(String sql, String tabla, Connection conexion, AdaptadorSQLBD abd) throws BDException{
        if(m_Log.isDebugEnabled()) m_Log.debug("rellenaExpedientesTablaTemporal2() : BEGIN");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector expedientes = new Vector<String>();
        Vector<Integer> tramites = new Vector<Integer>();
        GeneralValueObject salida = new GeneralValueObject();
        
        try {
            if(m_Log.isDebugEnabled()) m_Log.debug ("Tiene en el cuerpo datos específicos de trámite? "+ tieneTramiteCuerpo);
            if(m_Log.isDebugEnabled()) m_Log.debug("Consulta de columnas de tabla temporal = " + sql);
            pstmt = conexion.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (tieneTramiteCuerpo){
                while (rs.next()){
                    expedientes.add(rs.getString("EXPEDIENTE"));
                    tramites.add(rs.getInt("TRAMITE"));
                }//while (rs.next())
                rs.close();
                pstmt.close();
                sql = "INSERT INTO " + tabla + " (SIGP_TEMP_EXP, SIGP_TEMP_TRA, SIGP_TEMP_OCU) VALUES (?,?,0)";
                if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
                pstmt = conexion.prepareStatement(sql);
                for (int i = 0; i < expedientes.size(); i++){
                    pstmt.setString(1, (String) expedientes.get(i));
                    pstmt.setInt(2, tramites.get(i));
                    pstmt.executeUpdate();
                }//for (int i = 0; i < expedientes.size(); i++)
                pstmt.close();
            }else{
                while (rs.next()){
                    expedientes.add(rs.getString("EXPEDIENTE"));
                }//while (rs.next())
                rs.close();
                pstmt.close();
                sql = "INSERT INTO " + tabla + " (SIGP_TEMP_EXP, SIGP_TEMP_TRA, SIGP_TEMP_OCU) VALUES (?,0,0)";
                if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
                pstmt = conexion.prepareStatement(sql);
                for (int i=0; i<expedientes.size(); i++){
                    pstmt.setString(1, (String) expedientes.get(i));
                    pstmt.executeUpdate();
                }//for (int i=0; i<expedientes.size(); i++)
                pstmt.close();
            }//if (tieneTramiteCuerpo)
            if(m_Log.isDebugEnabled()) m_Log.debug("rellenaExpedientesTablaTemporal2() : END");
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR RELLENANDO TABLA TEMPORAL. rellenaExpedientesTablaTemporal2 ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }//try-catch
        // Se devuelve la lista de expedientes y de trámites
        salida.setAtributo("expedientes",expedientes);
        salida.setAtributo("tramites",tramites);
        return salida;
    }//rellenaExpedientesTablaTemporal
     
    private void completarTablaTemporal2 (GeneralValueObject gVO, String procedimiento, Connection conexion,
            AdaptadorSQLBD abd, String[] params, String codOrganizacion,Vector<String> expedientes,Vector<Integer> tramites) throws BDException{
        if(m_Log.isDebugEnabled()) m_Log.debug("completarTablaTemporal() : BEGIN");
        Vector<String> campos = new Vector<String>();
        Vector<String> tablas = new Vector<String>();
        Vector<String> columnas = new Vector<String>();
        campos = (Vector) gVO.getAtributo("campos");
        tablas = (Vector) gVO.getAtributo("tablas");
        columnas = (Vector) gVO.getAtributo("columnas");
        String nomTabla = (String) gVO.getAtributo("nomTabla");
        /*Dependiendo de la tabla podremos obtener los datos e insertarlos en la tabla llamando a la función adecuada.*/
        for (int i=0; i<campos.size();i++){
            if (tablas.elementAt(i).equals("E_TXT")) insertarDatoTexto2 (nomTabla, campos.elementAt(i), conexion, abd,expedientes);
            if (tablas.elementAt(i).equals("E_TXTT")) insertarDatoTextoTramite2 (nomTabla, campos.elementAt(i), conexion, abd,expedientes,campos);
            if (tablas.elementAt(i).equals("E_TNU")) insertarDatoNumero2 (nomTabla, campos.elementAt(i), conexion, abd,expedientes);
            if (tablas.elementAt(i).equals("E_TNUT")) insertarDatoNumeroTramite2 (nomTabla, campos.elementAt(i), conexion, abd,expedientes,campos);
            if (tablas.elementAt(i).equals("E_TNUC")) insertarDatoNumeroCalculado (nomTabla, campos.elementAt(i), conexion, abd,expedientes);
            if (tablas.elementAt(i).equals("E_TNUCT")) insertarDatoNumeroCalculadoTramite (nomTabla, campos.elementAt(i), conexion, abd,expedientes,campos);
            if (tablas.elementAt(i).equals("E_TFE")) insertarDatoFecha2 (nomTabla, campos.elementAt(i), conexion, abd,expedientes);
            if (tablas.elementAt(i).equals("E_TFET")) insertarDatoFechaTramite2 (nomTabla, campos.elementAt(i), conexion, abd,expedientes,campos);
            if (tablas.elementAt(i).equals("E_TFEC")) insertarDatoFechaCalculada (nomTabla, campos.elementAt(i), conexion, abd,expedientes);
            if (tablas.elementAt(i).equals("E_TFECT")) insertarDatoFechaCalculadaTramite (nomTabla, campos.elementAt(i), conexion, abd,expedientes,campos);
            if (tablas.elementAt(i).equals("E_TDE")) insertarDatoDesplegable2 (nomTabla, campos.elementAt(i), procedimiento, conexion, abd,expedientes);
            if (tablas.elementAt(i).equals("E_TDET")) insertarDatoDesplegableTramite2 (nomTabla, campos.elementAt(i), conexion, abd,expedientes,campos);
            if (tablas.elementAt(i).equals("E_TDEX")) insertarDatoDesplegableExterno (nomTabla, campos.elementAt(i), conexion, abd,expedientes);
            if (tablas.elementAt(i).equals("E_TDEXT")) insertarDatoDesplegableExternoTramite (nomTabla, campos.elementAt(i), conexion, abd,expedientes,campos);
            if (tablas.elementAt(i).equals("E_TTL")) insertarDatoTextoLargo2 (nomTabla, campos.elementAt(i), conexion, abd,expedientes);
            if (tablas.elementAt(i).equals("E_TTLT")) insertarDatoTextoLargoTramite2 (nomTabla, campos.elementAt(i), conexion, abd,expedientes,campos);
            if (tablas.elementAt(i).equals("INT")) insertarDatoInteresado2 (nomTabla, tablas.elementAt(i), campos.elementAt(i), columnas.elementAt(i),  conexion, abd,expedientes);            
            
            if(campos.elementAt(i).equalsIgnoreCase(("NUMERO_REGISTRO"))){
                insertarNumeroRegistroInicioExpediente2(nomTabla, campos.elementAt(i), columnas.elementAt(i), conexion, abd,expedientes);
                break;
            }//if
            
            if(campos.elementAt(i).equalsIgnoreCase(("FECHA_PRESENTACION"))){
                insertarFechaPresentacionRegistroInicioExpediente2(nomTabla, campos.elementAt(i), columnas.elementAt(i), conexion, abd,expedientes);
                break;
            }//if
            
            if (tablas.elementAt(i).equals("V_EXP_INF") && tieneTramiteCuerpo) insertarDatoGeneralTramite2(nomTabla, tablas.elementAt(i), campos.elementAt(i), columnas.elementAt(i), conexion, abd,expedientes,tramites);
            if (tablas.elementAt(i).equals("V_EXP_INF") && !tieneTramiteCuerpo) insertarDatoGeneral2(nomTabla, tablas.elementAt(i), campos.elementAt(i), columnas.elementAt(i), conexion, abd,expedientes);
            if (tablas.elementAt(i).equals("T_CAMPOS_NUMERICO")) insertarDatoNumericoAdicionalInteresado(nomTabla, tablas.elementAt(i), campos.elementAt(i), nomTabla, conexion, abd);
            if (tablas.elementAt(i).equals("T_CAMPOS_DESPLEGABLE"))insertarDatoDesplegableAdicionalInteresado2(nomTabla, tablas.elementAt(i), campos.elementAt(i), nomTabla, conexion, abd, params, codOrganizacion,procedimiento,expedientes);
            if (tablas.elementAt(i).equals("T_CAMPOS_TEXTO"))insertarDatoTextoAdicionalInteresado2(nomTabla, tablas.elementAt(i), campos.elementAt(i), nomTabla, conexion, abd, params, codOrganizacion,procedimiento,expedientes);
            if (tablas.elementAt(i).equals("T_CAMPOS_TEXTO_LARGO"))insertarDatoTextoLargoAdicionalInteresado2(nomTabla, tablas.elementAt(i), campos.elementAt(i), nomTabla, conexion, abd, params, codOrganizacion,procedimiento,expedientes);
            if (tablas.elementAt(i).equals("T_CAMPOS_FECHA"))insertarDatoFechaAdicionalInteresado2(nomTabla, tablas.elementAt(i), campos.elementAt(i), nomTabla, conexion, abd, params, codOrganizacion,procedimiento,expedientes);
        }//for (int i=0; i<campos.size();i++)
        if(m_Log.isDebugEnabled()) m_Log.debug("completarTablaTemporal() : END");
    }//completarTablaTemporal
    
    private void insertarDatoNumericoAdicionalInteresado(String nomTabla, String tabla, String campo, String columna, 
            Connection conexion, AdaptadorSQLBD abd) throws BDException{
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoNumericoAdicionalInteresado() : BEGIN");
        //Tabla temporal con los expedientes
        String consultaExpedientes = "SELECT SIGP_TEMP_EXP AS EXPEDIENTE FROM " + nomTabla;
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoNumericoAdicionalInteresado() : END");
    }//insertarDatoNumericoAdicionalInteresado
    
    private void insertarDatoTextoAdicionalInteresado2(String nomTabla, String tabla, String campo, String columna, 
            Connection conexion, AdaptadorSQLBD abd, String[] params, String codOrganizacion, String procedimiento,Vector<String> expedientes) 
            throws BDException{
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoTextoAdicionalInteresado() : BEGIN");
        PreparedStatement pstmt = null;        
        try{  
            //Recuperamos los terceros del expediente
            InteresadosDAO interesadosDao = InteresadosDAO.getInstance();
            DatosSuplementariosTerceroManager datosSuplementariosTerceroManager = DatosSuplementariosTerceroManager.getInstance();
            for(String expediente : expedientes){
                String[] splitExp = expediente.split("/");
                String ejercicio = splitExp[0];
                String numExp = splitExp[2];

                GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", codOrganizacion);
                    gVO.setAtributo("codProcedimiento", procedimiento);
                    gVO.setAtributo("ejercicio", ejercicio);
                    gVO.setAtributo("numero", expediente);
                        
                Vector<InteresadoExpedienteVO> interesados = interesadosDao.getListaInteresados(gVO, params);
                String valorActualizar = "";
                for(InteresadoExpedienteVO interesado : interesados){
                    //Extraemos el rol del nombre del campo desplegable.
                    String rolInteresado = eliminaEspaciosEnBlanco(interesado.getDescRol().trim());
                    if(campo.endsWith(rolInteresado)){
                        if(m_Log.isDebugEnabled()) m_Log.debug("Rol = " + interesado.getDescRol());
                        //Si coincide el rol del interesado con el de la columna extraemos el nombre de la columna para saber
                        //que dato adicional tenemos que recuperar.
                        String nombreCampoARecuperar = campo.substring(0,campo.length()-rolInteresado.length());
                        if(m_Log.isDebugEnabled()) m_Log.debug("Campo = " + nombreCampoARecuperar);
                        String valor = datosSuplementariosTerceroManager.
                            getValorTexto(nombreCampoARecuperar,String.valueOf(interesado.getCodTercero()), codOrganizacion, 
                            params);
                        if(m_Log.isDebugEnabled()) m_Log.debug("Valor = " + valor);
                        if(valor != null && !valor.equalsIgnoreCase("")){
                            if(valorActualizar.length() > 0){
                                valorActualizar += ";";
                            }//if(valorActualizar.length() > 0)
                            valorActualizar +=valor;
                        }//if(valor != null && valor != "")
                    }//if(campo.endsWith(interesado.getDescRol()))
                }//for(InteresadoExpedienteVO interesado : interesados)
                if(valorActualizar.length() > 0){
                    String sqlUpdate = "UPDATE " + nomTabla + " SET " + campo + "= '" 
                        + valorActualizar + "' WHERE SIGP_TEMP_EXP = '" + expediente + "'";
                    pstmt = conexion.prepareStatement(sqlUpdate);               
                    pstmt.executeUpdate();
                }//if(valorActualizar.length() > 0)
            }//for(String expediente : expedientes)
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoInteresado ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        } catch (Exception exc){
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, exc);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoInteresado ", exc);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }//try-catch
        finally{
            try{
               if(pstmt!=null) pstmt.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoTextoAdicionalInteresado() : END");
    }//insertarDatoTextoAdicionalInteresado
    
    private void insertarDatoTextoLargoAdicionalInteresado2(String nomTabla, String tabla, String campo, String columna, 
            Connection conexion, AdaptadorSQLBD abd, String[] params, String codOrganizacion, String procedimiento,Vector<String> expedientes) 
            throws BDException{
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoTextoLargoAdicionalInteresado() : BEGIN");
        PreparedStatement pstmt = null;
        try{
            //Recuperamos los terceros del expediente
            InteresadosDAO interesadosDao = InteresadosDAO.getInstance();
            DatosSuplementariosTerceroManager datosSuplementariosTerceroManager = DatosSuplementariosTerceroManager.getInstance();
            for(String expediente : expedientes){
                String[] splitExp = expediente.split("/");
                String ejercicio = splitExp[0];
                String numExp = splitExp[2];

                GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", codOrganizacion);
                    gVO.setAtributo("codProcedimiento", procedimiento);
                    gVO.setAtributo("ejercicio", ejercicio);
                    gVO.setAtributo("numero", expediente);
                        
                Vector<InteresadoExpedienteVO> interesados = interesadosDao.getListaInteresados(gVO, params);
                String valorActualizar = "";
                for(InteresadoExpedienteVO interesado : interesados){
                    //Extraemos el rol del nombre del campo desplegable.
                    String rolInteresado = eliminaEspaciosEnBlanco(interesado.getDescRol().trim());
                    if(campo.endsWith(rolInteresado)){
                        if(m_Log.isDebugEnabled()) m_Log.debug("Rol = " + interesado.getDescRol());
                        //Si coincide el rol del interesado con el de la columna extraemos el nombre de la columna para saber
                        //que dato adicional tenemos que recuperar.
                        String nombreCampoARecuperar = campo.substring(0,campo.length()-rolInteresado.length());
                        if(m_Log.isDebugEnabled()) m_Log.debug("Campo = " + nombreCampoARecuperar);
                        String valor = datosSuplementariosTerceroManager.
                                getValorTextoLargo(nombreCampoARecuperar,String.valueOf(interesado.getCodTercero()), codOrganizacion, 
                                params);
                        if(m_Log.isDebugEnabled()) m_Log.debug("Valor = " + valor);
                        if(valor != null && !valor.equalsIgnoreCase("")){
                                if(valorActualizar.length() > 0){
                                    valorActualizar += ";";
                                }//if(valorActualizar.length() > 0)
                                valorActualizar +=valor;
                        }//if(valor != null && valor != "")
                    }//if(campo.endsWith(interesado.getDescRol()))
                }//for(InteresadoExpedienteVO interesado : interesados)
                if(valorActualizar.length() > 0){
                    String sqlUpdate = "UPDATE " + nomTabla + " SET " + campo + "= '" 
                        + valorActualizar + "' WHERE SIGP_TEMP_EXP = '" + expediente + "'";
                    pstmt = conexion.prepareStatement(sqlUpdate);               
                    pstmt.executeUpdate();
                }//if(valorActualizar.length() > 0)
            }//for(String expediente : expedientes)
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoInteresado ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        } catch (Exception exc){
            //
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(pstmt!=null) pstmt.close();
            }catch(SQLException e){                
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoTextoLargoAdicionalInteresado() : END");
    }//insertarDatoTextoLargoAdicionalInteresado
    
    private void insertarDatoDesplegableAdicionalInteresado2(String nomTabla, String tabla, String campo, String columna, 
            Connection conexion, AdaptadorSQLBD abd, String[] params, String codOrganizacion, String procedimiento,Vector<String> expedientes) 
            throws BDException{
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoDesplegableAdicionalInteresado() : BEGIN");
        PreparedStatement pstmt = null; 
        try{
            //Recuperamos los terceros del expediente
            InteresadosDAO interesadosDao = InteresadosDAO.getInstance();
            DatosSuplementariosTerceroManager datosSuplementariosTerceroManager = DatosSuplementariosTerceroManager.getInstance();
            for(String expediente : expedientes){
                String[] splitExp = expediente.split("/");
                String ejercicio = splitExp[0];
                String numExp = splitExp[2];

                GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", codOrganizacion);
                    gVO.setAtributo("codProcedimiento", procedimiento);
                    gVO.setAtributo("ejercicio", ejercicio);
                    gVO.setAtributo("numero", expediente);
                        
                Vector<InteresadoExpedienteVO> interesados = interesadosDao.getListaInteresados(gVO, params);
                String valorActualizar = "";
                for(InteresadoExpedienteVO interesado : interesados){
                    //Extraemos el rol del nombre del campo desplegable.
                    String rolInteresado = eliminaEspaciosEnBlanco(interesado.getDescRol().trim());
                    if(campo.endsWith(rolInteresado)){
                        if(m_Log.isDebugEnabled()) m_Log.debug("Rol = " + interesado.getDescRol());
                        //Si coincide el rol del interesado con el de la columna extraemos el nombre de la columna para saber
                        //que dato adicional tenemos que recuperar.
                        String nombreCampoARecuperar = campo.substring(0,campo.length()-rolInteresado.length());
                        if(m_Log.isDebugEnabled()) m_Log.debug("Campo = " + nombreCampoARecuperar);
                        String valor = datosSuplementariosTerceroManager.
                                getValorDesplegableTabla(nombreCampoARecuperar,String.valueOf(interesado.getCodTercero()), codOrganizacion, 
                                params);
                        if(m_Log.isDebugEnabled()) m_Log.debug("Valor = " + valor);
                        if(valor != null && !valor.equalsIgnoreCase("")){
                            String valorDesplegable = datosSuplementariosTerceroManager.
                                    getValorDesplegable(codOrganizacion, nombreCampoARecuperar, valor, params);
                            if(m_Log.isDebugEnabled()) m_Log.debug("valorDesplegable = " + valorDesplegable);
                            if(valorDesplegable != null && !valorDesplegable.equalsIgnoreCase("")){
                                //Lo añadimos a los posibles valores existentes de otros terceros
                                if(valorActualizar.length() > 0){
                                    valorActualizar += ";";
                                }//if(valorActualizar.length() > 0)
                                valorActualizar +=valorDesplegable;
                            }//if(valorDesplegable != null && !valorDesplegable.equalsIgnoreCase(""))
                        }//if(valor != null && valor != "")
                    }//if(campo.endsWith(interesado.getDescRol()))
                }//for(InteresadoExpedienteVO interesado : interesados)
                if(valorActualizar.length() > 0){
                    String sqlUpdate = "UPDATE " + nomTabla + " SET " + campo + "= '" 
                        + valorActualizar + "' WHERE SIGP_TEMP_EXP = '" + expediente + "'";
                    pstmt = conexion.prepareStatement(sqlUpdate);               
                    pstmt.executeUpdate();
                }//if(valorActualizar.length() > 0)
            }//for(String expediente : expedientes)
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoInteresado ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        } catch (Exception exc){
            //
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(pstmt!=null) pstmt.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoDesplegableAdicionalInteresado() : END");
    }//insertarDatoDesplegableAdicionalInteresado
        
     private void insertarDatoFechaAdicionalInteresado2(String nomTabla, String tabla, String campo, String columna, 
            Connection conexion, AdaptadorSQLBD abd, String[] params, String codOrganizacion, String procedimiento,Vector<String> expedientes) 
            throws BDException{
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoFechaAdicionalInteresado() : BEGIN");
        PreparedStatement pstmt = null; 
        try{
            //Recuperamos los terceros del expediente
            InteresadosDAO interesadosDao = InteresadosDAO.getInstance();
            DatosSuplementariosTerceroManager datosSuplementariosTerceroManager = DatosSuplementariosTerceroManager.getInstance();
            for(String expediente : expedientes){
                String[] splitExp = expediente.split("/");
                String ejercicio = splitExp[0];
                String numExp = splitExp[2];

                GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", codOrganizacion);
                    gVO.setAtributo("codProcedimiento", procedimiento);
                    gVO.setAtributo("ejercicio", ejercicio);
                    gVO.setAtributo("numero", expediente);
                        
                Vector<InteresadoExpedienteVO> interesados = interesadosDao.getListaInteresados(gVO, params);
                String valorActualizar = "";
                for(InteresadoExpedienteVO interesado : interesados){
                    //Extraemos el rol del nombre del campo desplegable.
                    String rolInteresado = eliminaEspaciosEnBlanco(interesado.getDescRol().trim());
                    if(campo.endsWith(rolInteresado)){
                        if(m_Log.isDebugEnabled()) m_Log.debug("Rol = " + interesado.getDescRol());
                        //Si coincide el rol del interesado con el de la columna extraemos el nombre de la columna para saber
                        //que dato adicional tenemos que recuperar.
                        String nombreCampoARecuperar = campo.substring(0,campo.length()-rolInteresado.length());
                        if(m_Log.isDebugEnabled()) m_Log.debug("Campo = " + nombreCampoARecuperar);
                        String valor = datosSuplementariosTerceroManager.
                                getValorFecha(nombreCampoARecuperar,String.valueOf(interesado.getCodTercero()), codOrganizacion, 
                                params);
                        if(m_Log.isDebugEnabled()) m_Log.debug("Valor = " + valor);
                        if(valor != null && !valor.equalsIgnoreCase("")){
                                if(valorActualizar.length() > 0){
                                    valorActualizar += ";";
                                }//if(valorActualizar.length() > 0)
                                valorActualizar +=valor;
                        }//if(valor != null && valor != "")
                    }//if(campo.endsWith(interesado.getDescRol()))
                }//for(InteresadoExpedienteVO interesado : interesados)
                if(valorActualizar.length() > 0){
                    String sqlUpdate = "UPDATE " + nomTabla + " SET " + campo + "= '" 
                        + valorActualizar + "' WHERE SIGP_TEMP_EXP = '" + expediente + "'";
                    pstmt = conexion.prepareStatement(sqlUpdate);               
                    pstmt.executeUpdate();
                }//if(valorActualizar.length() > 0)
            }//for(String expediente : expedientes)
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoInteresado ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        } catch (Exception exc){
            //
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(pstmt!=null) pstmt.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoFechaAdicionalInteresado() : END");
    }//insertarDatoFechaAdicionalInteresado
    
    private String eliminaEspaciosEnBlanco (String cadena){
        String cadenaSinEspacios = "";
        StringTokenizer tok = new StringTokenizer(cadena);
        while(tok.hasMoreElements()){
             cadenaSinEspacios += tok.nextElement();
        }//while(tok.hasMoreElements())
        return cadenaSinEspacios;
    }//eliminaEspaciosEnBlanco

    private void insertarDatoGeneralTramite2(String nomTabla, String tabla, String campo, String columna, Connection conexion, AdaptadorSQLBD abd,Vector<String> expedientes,Vector<Integer> tramites) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoGeneralTramite() : BEGIN");
        PreparedStatement pstmt = null;        
        //Vector<Integer> tramites = new Vector<Integer>();
        //String consultaExpedientes = "SELECT SIGP_TEMP_EXP AS EXPEDIENTE, SIGP_TEMP_TRA AS TRAMITE FROM " + nomTabla;
        String sql = "UPDATE " + nomTabla + " SET " + campo + "= (SELECT MAX("+ columna + ") FROM " + tabla +
                " WHERE EXP_NUM=? AND TRA_COD=?) WHERE SIGP_TEMP_EXP=?  AND SIGP_TEMP_TRA=?";
        if (columna.equals("EXP_FEI") || columna.equals("EXP_FEF") || columna.equals("TRA_FEI") || columna.equals("TRA_FEF"))
            sql= "UPDATE " + nomTabla + " SET " + campo + "= (SELECT " +
                    abd.convertir("MAX("+ columna + ")",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " FROM " + tabla +
                " WHERE EXP_NUM=? AND TRA_COD=?) WHERE SIGP_TEMP_EXP=? AND SIGP_TEMP_TRA=?";
        try {      
            pstmt = conexion.prepareStatement(sql);
            for (int i = 0; i < expedientes.size(); i++){
                pstmt.setString(1, expedientes.elementAt(i));
                pstmt.setInt(2, tramites.elementAt(i));
                pstmt.setString(3, expedientes.elementAt(i));
                pstmt.setInt(4, tramites.elementAt(i));
                pstmt.executeUpdate();
            }//for
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoGeneralTramite ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(pstmt!=null) pstmt.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoGeneralTramite() : END");
    }//insertarDatoGeneralTramite

    private void insertarDatoGeneral2(String nomTabla, String tabla, String campo, String columna, Connection conexion, AdaptadorSQLBD abd,Vector<String> expedientes) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoGeneral() : BEGIN");
        PreparedStatement pstmt = null;
        
        String sql = "UPDATE " + nomTabla + " SET " + campo + "= (SELECT MAX("+ columna + ") FROM " + tabla +
                " WHERE EXP_NUM=?) WHERE SIGP_TEMP_EXP=?";
        if (columna.equals("EXP_FEI") || columna.equals("EXP_FEF"))
            sql= "UPDATE " + nomTabla + " SET " + campo + "= (SELECT " +
                    abd.convertir( "MAX("+ columna + ")",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " FROM " + tabla +
                " WHERE EXP_NUM=?) WHERE SIGP_TEMP_EXP=?";
        try {
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            pstmt = conexion.prepareStatement(sql);
            for (int i = 0; i < expedientes.size(); i++) {
                pstmt.setString(1, expedientes.elementAt(i));
                pstmt.setString(2, expedientes.elementAt(i));                
                pstmt.executeUpdate();
            }//for (int i = 0; i < expedientes.size(); i++) 
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoGeneral ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(pstmt!=null) pstmt.close();                
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoGeneral() : END");
    }//insertarDatoGeneral
    
    private void insertarNumeroRegistroInicioExpediente2(String nomTabla, String campo, String columna, Connection conexion, AdaptadorSQLBD abd,Vector<String> expedientes) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoGeneral() : BEGIN");
        PreparedStatement pstmt = null;
              
        String sql = "UPDATE " + nomTabla + " SET " + campo + "= (SELECT max("+ columna + ") FROM V_REG_INICIO_EXP" +
                " WHERE NUM_EXPEDIENTE=?) WHERE SIGP_TEMP_EXP=?";
        
        try {
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            pstmt = conexion.prepareStatement(sql);
            for (int i = 0; i < expedientes.size(); i++) {
                pstmt.setString(1, expedientes.elementAt(i));
                pstmt.setString(2, expedientes.elementAt(i));                
                pstmt.executeUpdate();
            }//for
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarNumeroRegistroInicioExpediente ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(pstmt!=null) pstmt.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoGeneral() : END");
    }//insertarNumeroRegistroInicioExpediente  
     
    private void insertarFechaPresentacionRegistroInicioExpediente2(String nomTabla, String campo, String columna, Connection conexion, AdaptadorSQLBD abd, Vector<String> expedientes) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarFechaPresentacionRegistroInicioExpediente() : BEGIN");
        PreparedStatement pstmt = null;
        String sql = "UPDATE " + nomTabla + " SET " + campo + "= (SELECT max("+ columna + ") FROM V_REG_INICIO_EXP" +
                " WHERE NUM_EXPEDIENTE=?) WHERE SIGP_TEMP_EXP=?";
        
        try {            
            pstmt = conexion.prepareStatement(sql);
            for (int i = 0; i < expedientes.size(); i++) {
                pstmt.setString(1, expedientes.elementAt(i));
                pstmt.setString(2, expedientes.elementAt(i));                
                pstmt.executeUpdate();
            }//for
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarFechaPresentacionRegistroInicioExpediente ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(pstmt!=null) pstmt.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarFechaPresentacionRegistroInicioExpediente() : END");
    }//insertarFechaPresentacionRegistroInicioExpediente
    
    private void insertarDatoTexto2 (String nomTabla, String campo, Connection conexion, AdaptadorSQLBD abd,Vector<String> expedientes) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoTexto() : BEGIN");
        PreparedStatement pstmt = null;
        ResultSet rs = null;        
        //String consultaExpedientes = "SELECT SIGP_TEMP_EXP AS EXPEDIENTE FROM " + nomTabla;
        String sql = "UPDATE " + nomTabla + " SET " + campo + "= (SELECT TXT_VALOR FROM E_TXT" +
                " WHERE TXT_NUM=? AND TXT_COD='" + campo + "') WHERE SIGP_TEMP_EXP=?";
        try {           
            pstmt = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            for (int i = 0; i < expedientes.size(); i++) {
                pstmt.setString(1,expedientes.get(i));
                pstmt.setString(2,expedientes.get(i));
                pstmt.executeUpdate();
            }//for
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoTexto ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(pstmt!=null) pstmt.close();
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el resultset");
                if(rs!=null) rs.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoTexto() : END");
    }//insertarDatoTexto

    private void insertarDatoTextoTramite2 (String nomTabla, String campo, Connection conexion, 
            AdaptadorSQLBD abd,Vector<String> expedientes,Vector<String> campos) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoTextoTramite() : BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        int tramite = Integer.valueOf(campo.split("_")[1]);
        String codCampo = campo.split("_")[2];        
        String sql = "SELECT TXTT_VALOR, TXTT_OCU FROM E_TXTT DATOS " +
                " WHERE TXTT_NUM=? AND TXTT_TRA=? AND TXTT_COD=?";
        try {
            ps = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            for (int i = 0; i < expedientes.size(); i++) {
                ps.setString(1, expedientes.elementAt(i));
                ps.setInt(2, tramite);
                ps.setString(3, codCampo);
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    String valor = rs.getString("TXTT_VALOR");
                    int ocurrencia = rs.getInt("TXTT_OCU");

                    insertarDato((String) expedientes.elementAt(i), tramite, ocurrencia, nomTabla, campo, valor, campos, conexion);
                }
            }//for
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoTextoTramite ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(ps!=null) ps.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoTextoTramite() : END");
    }//insertarDatoTextoTramite2

    private void insertarDatoNumero2(String nomTabla,  String campo, Connection conexion, AdaptadorSQLBD abd,Vector<String> expedientes) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoNumero() : BEGIN");
        PreparedStatement pstmt = null;
                
        String sql = "UPDATE " + nomTabla + " SET " + campo + "= (SELECT " +
                abd.convertir("TNU_VALOR",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) +
                " FROM E_TNU" +
                " WHERE TNU_NUM=? AND TNU_COD='" + campo + "') WHERE SIGP_TEMP_EXP=?";
        try {
            pstmt = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            for (int i = 0; i < expedientes.size(); i++) {
                pstmt.setString(1, expedientes.elementAt(i));
                pstmt.setString(2, expedientes.elementAt(i));                
                pstmt.executeUpdate();
            }//for (int i = 0; i < expedientes.size(); i++) 
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoNumero ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(pstmt!=null) pstmt.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoNumero() : END");
    }//insertarDatoNumero
    
    private void insertarDatoNumeroTramite2 (String nomTabla, String campo, Connection conexion, 
            AdaptadorSQLBD abd,Vector<String> expedientes,Vector<String> campos) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoNumeroTramite() : BEGIN");
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        int tramite = Integer.valueOf(campo.split("_")[1]);
        String codCampo = campo.split("_")[2];
        
        String sql = "SELECT " + abd.convertir("TNUT_VALOR",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) +
                " AS VALOR, TNUT_OCU" + 
                " FROM E_TNUT" +
                " WHERE TNUT_NUM = ? AND TNUT_TRA = ? AND TNUT_COD = ?";
        try {
            ps = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            for (int i = 0; i < expedientes.size(); i++) {
                ps.setString(1, expedientes.elementAt(i));
                ps.setInt(2, tramite);
                ps.setString(3, codCampo);
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    String valor = rs.getString("VALOR");
                    int ocurrencia = rs.getInt("TNUT_OCU");

                    insertarDato((String) expedientes.elementAt(i), tramite, ocurrencia, nomTabla, campo, valor, campos, conexion);
                }
            }//for
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoNumeroTramite ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(ps!=null) ps.close(); 
            }catch(SQLException e){
                //
            }//try-catch       
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoNumeroTramite() : END");
    }//insertarDatoNumeroTramite2

    private void insertarDatoNumeroCalculado(String nomTabla,  String campo, Connection conexion, AdaptadorSQLBD abd,Vector<String> expedientes) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoNumeroCalculado() : BEGIN");
        PreparedStatement pstmt = null;
                
        String sql = "UPDATE " + nomTabla + " SET " + campo + "= (SELECT " +
                abd.convertir("TNUC_VALOR",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) +
                " FROM E_TNUC" +
                " WHERE TNUC_NUM=? AND TNUC_COD='" + campo + "') WHERE SIGP_TEMP_EXP=?";
        try {
            pstmt = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            for (int i = 0; i < expedientes.size(); i++) {
                pstmt.setString(1, expedientes.elementAt(i));
                pstmt.setString(2, expedientes.elementAt(i));                
                pstmt.executeUpdate();
            }//for (int i = 0; i < expedientes.size(); i++) 
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoNumero ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(pstmt!=null) pstmt.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoNumeroCalculado() : END");
    }//insertarDatoNumeroCalculado
    
    private void insertarDatoNumeroCalculadoTramite (String nomTabla, String campo, Connection conexion, 
            AdaptadorSQLBD abd,Vector<String> expedientes,Vector<String> campos) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoNumeroCalculadoTramite() : BEGIN");
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        int tramite = Integer.valueOf(campo.split("_")[1]);
        String codCampo = campo.split("_")[2];
        
        String sql = "SELECT " + abd.convertir("TNUCT_VALOR",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null) +
                " AS VALOR, TNUCT_OCU" + 
                " FROM E_TNUCT" +
                " WHERE TNUCT_NUM = ? AND TNUCT_TRA = ? AND TNUCT_COD = ?";
        try {
            ps = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            for (int i = 0; i < expedientes.size(); i++) {
                ps.setString(1, expedientes.elementAt(i));
                ps.setInt(2, tramite);
                ps.setString(3, codCampo);
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    String valor = rs.getString("VALOR");
                    int ocurrencia = rs.getInt("TNUCT_OCU");

                    insertarDato((String) expedientes.elementAt(i), tramite, ocurrencia, nomTabla, campo, valor, campos, conexion);
                }
            }//for
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoNumeroCalculadoTramite ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(ps!=null) ps.close(); 
            }catch(SQLException e){
                //
            }//try-catch       
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoNumeroCalculadoTramite() : END");
    }//insertarDatoNumeroCalculadoTramite

    private void insertarDatoFecha2 (String nomTabla, String campo, Connection conexion, AdaptadorSQLBD abd,Vector<String> expedientes) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoFecha() : BEGIN");
        PreparedStatement pstmt = null;        
        String sql = "UPDATE " + nomTabla + " SET " + campo + "= (SELECT " +
                abd.convertir("TFE_VALOR",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+ " FROM E_TFE" +
                " WHERE TFE_NUM=? AND TFE_COD='" + campo + "') WHERE SIGP_TEMP_EXP=?";
        try {
            pstmt = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            for (int i = 0; i < expedientes.size(); i++) {
                pstmt.setString(1, expedientes.elementAt(i));
                pstmt.setString(2, expedientes.elementAt(i));                
                pstmt.executeUpdate();
            }//for
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoFecha ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(pstmt!=null) pstmt.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoFecha() : END");
    }//insertarDatoFecha

    private void insertarDatoFechaTramite2 (String nomTabla,String campo, Connection conexion, 
            AdaptadorSQLBD abd,Vector<String> expedientes,Vector<String> campos) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoFechaTramite() : BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        int tramite = Integer.valueOf(campo.split("_")[1]);
        String codCampo = campo.split("_")[2];
        
        try {
            String sql = "SELECT " + abd.convertir("TFET_VALOR",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                " AS VALOR, TFET_OCU " +
                " FROM E_TFET" +
                " WHERE TFET_NUM = ? AND TFET_TRA = ? AND TFET_COD = ?" +
                " ORDER BY TFET_OCU";
            
            ps = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            
            for (int i = 0; i < expedientes.size(); i++) {
                ps = conexion.prepareStatement(sql);
                ps.setString(1, expedientes.elementAt(i));
                ps.setInt(2, tramite);
                ps.setString(3, codCampo);
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    String valor = rs.getString("VALOR");
                    int ocurrencia = rs.getInt("TFET_OCU");

                    insertarDato((String) expedientes.elementAt(i), tramite, ocurrencia, nomTabla, campo, valor, campos, conexion);
                }
            }//for
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoFechaTramite ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(ps!=null) ps.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoFechaTramite() : END");
    }//insertarDatoFechaTramite

    private void insertarDatoFechaCalculada (String nomTabla, String campo, Connection conexion, AdaptadorSQLBD abd,Vector<String> expedientes) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoFechaCalculada() : BEGIN");
        PreparedStatement pstmt = null;        
        String sql = "UPDATE " + nomTabla + " SET " + campo + "= (SELECT " +
                abd.convertir("TFEC_VALOR",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY")+ " FROM E_TFEC" +
                " WHERE TFEC_NUM=? AND TFEC_COD='" + campo + "') WHERE SIGP_TEMP_EXP=?";
        try {
            pstmt = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            for (int i = 0; i < expedientes.size(); i++) {
                pstmt.setString(1, expedientes.elementAt(i));
                pstmt.setString(2, expedientes.elementAt(i));                
                pstmt.executeUpdate();
            }//for
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoFechaCalculada ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(pstmt!=null) pstmt.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoFechaCalculada() : END");
    }//insertarDatoFechaCalculada

    private void insertarDatoFechaCalculadaTramite (String nomTabla,String campo, Connection conexion, 
            AdaptadorSQLBD abd,Vector<String> expedientes,Vector<String> campos) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoFechaCalculadaTramite() : BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        int tramite = Integer.valueOf(campo.split("_")[1]);
        String codCampo = campo.split("_")[2];
        
        try {
            String sql = "SELECT " + abd.convertir("TFECT_VALOR",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                " AS VALOR, TFECT_OCU " +
                " FROM E_TFECT" +
                " WHERE TFECT_NUM = ? AND TFECT_TRA = ? AND TFECT_COD = ?" +
                " ORDER BY TFECT_OCU";
            
            ps = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            
            for (int i = 0; i < expedientes.size(); i++) {               
                ps.setString(1, expedientes.elementAt(i));
                ps.setInt(2, tramite);
                ps.setString(3, codCampo);
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    String valor = rs.getString("VALOR");
                    int ocurrencia = rs.getInt("TFECT_OCU");

                    insertarDato((String) expedientes.elementAt(i), tramite, ocurrencia, nomTabla, campo, valor, campos, conexion);
                }
            }//for
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoFechaCalculadaTramite ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(ps!=null) ps.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoFechaCalculadaTramite() : END");
    }//insertarDatoFechaCalculadaTramite
 
    private void insertarDatoDesplegable2 (String nomTabla, String campo, String procedimiento, Connection conexion, AdaptadorSQLBD abd,Vector<String> expedientes) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoDesplegable() : BEGIN");
        PreparedStatement pstmt = null;
                
       /* String sql = "UPDATE " + nomTabla + " SET " + campo + "=(SELECT VALORES.DES_NOM FROM E_DES_VAL VALORES INNER " +
                "JOIN E_DES ON E_DES.DES_COD=VALORES.DES_COD " +
                "AND DES_VAL_COD=(SELECT TDE_VALOR FROM E_TDE INNER JOIN E_PCA ON PCA_COD=TDE_COD AND PCA_PRO=? " +
                " INNER JOIN E_EXP ON EXP_NUM=TDE_NUM WHERE  " +
                "TDE_COD=? AND TDE_NUM=? AND PCA_DESPLEGABLE=E_DES.DES_COD)) WHERE SIGP_TEMP_EXP=?";*/
         
        String sql = "UPDATE " + nomTabla + " SET " + campo + "=(SELECT E_DES_VAL.DES_NOM AS VALOR" + 
                " FROM E_DES_VAL " + 
                " INNER JOIN E_DES ON E_DES.DES_COD = E_DES_VAL.DES_COD" +
                " INNER JOIN E_TDE ON TDE_VALOR = DES_VAL_COD" + 
                " INNER JOIN E_PCA ON  " + 
                "  PCA_COD=TDE_COD AND PCA_DESPLEGABLE=E_DES.DES_COD" + 
                " WHERE PCA_PRO=? AND TDE_COD=? AND TDE_NUM=? ) WHERE SIGP_TEMP_EXP=?";
        
        
        try {
            pstmt = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            for (int i = 0; i < expedientes.size(); i++) {
                pstmt.setString(1, procedimiento);
                pstmt.setString(2, campo);
                pstmt.setString(3, expedientes.elementAt(i));                
                pstmt.setString(4, expedientes.elementAt(i));                
                pstmt.executeUpdate();
            }//for
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoDesplegable ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(pstmt!=null) pstmt.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoDesplegable() : END");
    }//insertarDatoDesplegable

    private void insertarDatoDesplegableTramite2 (String nomTabla, String campo, Connection conexion, 
            AdaptadorSQLBD abd,Vector<String> expedientes,Vector<String> campos) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoDesplegableTramite() : BEGIN");
        PreparedStatement ps = null;    
        ResultSet rs = null;
        int tramite = Integer.valueOf(campo.split("_")[1]);
        String codCampo = campo.split("_")[2];
        
        String sql = "SELECT E_DES_VAL.DES_NOM AS VALOR, TDET_OCU" + 
                " FROM E_DES_VAL " + 
                " INNER JOIN E_DES ON E_DES.DES_COD = E_DES_VAL.DES_COD" +
                " INNER JOIN E_TDET ON TDET_VALOR = DES_VAL_COD" + 
                " INNER JOIN E_TCA ON TCA_PRO=TDET_PRO AND TCA_TRA=TDET_TRA" + 
                " AND TCA_COD=TDET_COD AND TCA_DESPLEGABLE=E_DES.DES_COD" + 
                " WHERE TDET_COD=? AND TDET_NUM=? ";

        try {        
            ps = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            for (int i = 0; i < expedientes.size(); i++) {
                ps.setString(1, codCampo);
                ps.setString(2, expedientes.elementAt(i));                
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    String valor = rs.getString("VALOR");
                    int ocurrencia = rs.getInt("TDET_OCU");

                    insertarDato((String) expedientes.elementAt(i), tramite, ocurrencia, nomTabla, campo, valor, campos, conexion);
                }
            }//for
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoDesplegableTramite ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(ps!=null) ps.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoDesplegableTramite() : END");
    }//insertarDatoDesplegableTramite

    private void insertarDatoDesplegableExterno (String nomTabla, String campo, Connection conexion, AdaptadorSQLBD abd,Vector<String> expedientes) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoDesplegableExterno() : BEGIN");
        PreparedStatement pstmt = null;        
        String sql = "UPDATE " + nomTabla + " SET " + campo + "= (SELECT TDEX_VALOR" + 
                " FROM E_TDEX" +
                " WHERE TDEX_NUM=? AND TDEX_COD='" + campo + "') WHERE SIGP_TEMP_EXP=?";
        try {
            pstmt = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            for (int i = 0; i < expedientes.size(); i++) {
                pstmt.setString(1, expedientes.elementAt(i));
                pstmt.setString(2, expedientes.elementAt(i));                
                pstmt.executeUpdate();
            }//for
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoDesplegableExterno ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(pstmt!=null) pstmt.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoDesplegableExterno() : END");
    }//insertarDatoDesplegableExterno

    private void insertarDatoDesplegableExternoTramite (String nomTabla,String campo, Connection conexion, 
            AdaptadorSQLBD abd,Vector<String> expedientes,Vector<String> campos) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoDesplegableExternoTramite() : BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        int tramite = Integer.valueOf(campo.split("_")[1]);
        String codCampo = campo.split("_")[2];
        
        try {
            String sql = "SELECT TDEXT_VALOR" +
                " AS VALOR, TDEXT_OCU " +
                " FROM E_TDEXT" +
                " WHERE TDEXT_NUM = ? AND TDEXT_TRA = ? AND TDEXT_COD = ?" +
                " ORDER BY TDEXT_OCU";
            
            ps = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            
            for (int i = 0; i < expedientes.size(); i++) {              
                ps.setString(1, expedientes.elementAt(i));
                ps.setInt(2, tramite);
                ps.setString(3, codCampo);
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    String valor = rs.getString("VALOR");
                    int ocurrencia = rs.getInt("TDEXT_OCU");

                    insertarDato((String) expedientes.elementAt(i), tramite, ocurrencia, nomTabla, campo, valor, campos, conexion);
                }
            }//for
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoDesplegableExternoTramite ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(ps!=null) ps.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoDesplegableExternoTramite() : END");
    }//insertarDatoDesplegableExternoTramite

    private void insertarDatoTextoLargo2 (String nomTabla, String campo, Connection conexion, AdaptadorSQLBD abd,Vector<String> expedientes) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoTextoLargo() : BEGIN");
        PreparedStatement pstmt = null;
        ResultSet rs = null;        
        Vector<String> datos = new Vector<String>();
        String sql = "UPDATE " + nomTabla + " SET " + campo + "=? WHERE SIGP_TEMP_EXP=?";
        String sqlDatoLargo = "SELECT TTL_VALOR FROM E_TTL WHERE TTL_NUM=? AND TTL_COD='" + campo + "'";

        try {
            pstmt = conexion.prepareStatement(sqlDatoLargo);
            if(m_Log.isDebugEnabled()) m_Log.debug("sqlDatoLargo = " + sqlDatoLargo);
            for (int i = 0; i < expedientes.size(); i++) {
                pstmt.setString(1, expedientes.elementAt(i));                
                rs = pstmt.executeQuery();
                if (rs.next()){
                    java.io.Reader cr = rs.getCharacterStream("TTL_VALOR");
                    if (cr != null) {
                        java.io.CharArrayWriter ot = new java.io.CharArrayWriter();
                        int c;
                        while ((c = cr.read()) != -1) {
                            ot.write(c);
                        }//while ((c = cr.read()) != -1) 
                        ot.flush();
                        datos.add(ot.toString());
                        ot.close();
                        cr.close();
                    }//if (cr != null)
                }//if (rs.next())
                else datos.add("");
            }//for (int i = 0; i < expedientes.size(); i++)

            pstmt = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            for (int i = 0; i < expedientes.size(); i++) {
                pstmt.setString(1, datos.elementAt(i));
                pstmt.setString(2, expedientes.elementAt(i));
                pstmt.executeUpdate();
            }//for
            pstmt.close();
        } catch (IOException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR ENTRADA SALIDA. insertarDatoTextoLargo ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoTextoLargo ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(pstmt!=null) pstmt.close();
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el resultset");
                if(rs!=null) rs.close();
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoTextoLargo() : END");
    }//insertarDatoTextoLargo
    
    private void insertarDatoTextoLargoTramite2 (String nomTabla, String campo, Connection conexion, 
            AdaptadorSQLBD abd,Vector<String> expedientes,Vector<String> campos) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoTextoLargoTramite() : BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;        
        int tramite = Integer.valueOf(campo.split("_")[1]);
        String codCampo = campo.split("_")[2];
        
        String sqlDatoLargo = "SELECT TTLT_OCU, TTLT_VALOR FROM E_TTLT DATOS " +
                " WHERE TTLT_NUM=? AND TTLT_TRA=? AND TTLT_COD=?";
        try {            
            ps = conexion.prepareStatement(sqlDatoLargo);
            if(m_Log.isDebugEnabled()) m_Log.debug("sqlDatoLargo = " + sqlDatoLargo);
            for (int i = 0; i < expedientes.size(); i++) {
                ps.setString(1, expedientes.elementAt(i));
                ps.setInt(2, tramite);
                ps.setString(3, codCampo);                
                rs = ps.executeQuery();
                while (rs.next()){
                    int ocurrencia = rs.getInt("TTLT_OCU");
                    String valor = "";
                    java.io.Reader cr = rs.getCharacterStream("TTLT_VALOR");
                    if (cr != null) {
                        java.io.CharArrayWriter ot = new java.io.CharArrayWriter();
                        int c;
                        while ((c = cr.read()) != -1) {
                            ot.write(c);
                        }//while ((c = cr.read()) != -1) 
                        ot.flush();
                        valor = ot.toString();
                        ot.close();
                        cr.close();
                    }//if (cr != null)
                    insertarDato((String) expedientes.elementAt(i), tramite, ocurrencia, nomTabla, campo, valor, campos, conexion);
                }//if (rs.next())
            }//for (int i = 0; i < expedientes.size(); i++) 
        } catch (IOException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR ENTRADA SALIDA. insertarDatoTextoLargoTramite ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoTextoLargoTramite ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerrramos el prepared statement");
                if(ps!=null) ps.close();
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerrramos el resultset");
                if(rs!=null) rs.close();
            }catch(SQLException e){                
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoTextoLargoTramite() : END");
    }//insertarDatoTextoLargoTramite

    private void insertarDatoInteresado2 (String nomTabla, String tabla, String campo, String columna, Connection conexion, AdaptadorSQLBD abd,Vector<String> expedientes) throws BDException{
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoInteresado() : BEGIN");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector<String> datos = new Vector<String>();
        
        String sql = "UPDATE " + nomTabla + " SET " + campo + "=? WHERE SIGP_TEMP_EXP=?";
        String compruebaRol = "SELECT * FROM E_ROL WHERE " + abd.funcionCadena(abd.FUNCIONCADENA_REPLACE, new String[]
                                {"ROL_DES","' '","''"}) + "='" + campo + "'";
        String campoVistaConsultar = "";
        String interesados = "";
        String datoInsercion ="";
        String rol = "";

        try {
            //OPCIONES DEL CAMPO = ROL, RolROL, DocROL, DomROL, CodPostalROL
            pstmt = conexion.prepareStatement(compruebaRol);
            if(m_Log.isDebugEnabled()) m_Log.debug("compruebaRol = " + compruebaRol);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                campoVistaConsultar = "INT_NOC";
                rol = campo;
            }// if (rs.next())
            rs.close();
            pstmt.close();
            if (campoVistaConsultar.equals("")){
                rol = campo.substring(3,campo.length());
                if (campo.startsWith("Nom")) {
                    campoVistaConsultar = "INT_NOM";
                }//if (campo.startsWith("Nom"))
                if (campo.startsWith("Ap1")) {
                    campoVistaConsultar = "INT_AP1";
                }//if (campo.startsWith("Ap1")) 
                if (campo.startsWith("Ap2")) {
                    campoVistaConsultar = "INT_AP2";
                }//if (campo.startsWith("Ap2")) 
                if (campo.startsWith("Tid")) {
                    campoVistaConsultar = "INT_TID";
                }//if (campo.startsWith("Tid")) 
                if (campo.startsWith("Doc")) {
                    campoVistaConsultar = "INT_DOC";
                }//if (campo.startsWith("Doc")) 
                if (campo.startsWith("Dom")) {
                    campoVistaConsultar = "INT_DOMNN";
                }//if (campo.startsWith("Dom")) 
                if (campo.startsWith("Rol")) {
                    campoVistaConsultar = "INT_ROL";
                }//if (campo.startsWith("Rol")) 
                if (campo.startsWith("Pob")) {
                    campoVistaConsultar = "INT_POB";
                }//if (campo.startsWith("Pob")) 
                if (campo.startsWith("Tlfno")) {
                    campoVistaConsultar = "INT_TLF";
                    rol = campo.substring(5,campo.length());
                }//if (campo.startsWith("Tlfno")) 
                if (campo.startsWith("Rol")) {
                    campoVistaConsultar = "INT_ROL";
                }//if (campo.startsWith("Rol")) 
                if (campo.startsWith("CodPostal")) {
                    campoVistaConsultar = "INT_CPO";
                    rol = campo.substring(9,campo.length());
                }//if (campo.startsWith("CodPostal"))
                /*** PRUEBA ***/
                if (campo.startsWith("Email")) {
                    campoVistaConsultar = "INT_DCE";
                    rol = campo.substring(5,campo.length());
                }//if (campo.startsWith("CodPostal"))
                /*** PRUEBA ***/
            }//if (campoVistaConsultar.equals("")
           
            interesados = "SELECT " + campoVistaConsultar + " FROM V_INT WHERE INT_NUM=? AND " +
                    abd.funcionCadena(abd.FUNCIONCADENA_REPLACE, new String[]{"INT_ROL","' '","''"}) +
                    "='" + rol + "'";
            
            if(m_Log.isDebugEnabled()) m_Log.debug("interesados = " + interesados);
            if(m_Log.isDebugEnabled()) m_Log.debug("interesados = " + expedientes);

            for (int i = 0; i < expedientes.size(); i++) {
                pstmt = conexion.prepareStatement(interesados);
                datoInsercion = "";
                pstmt.setString(1, expedientes.elementAt(i));
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    String resultado=rs.getString(campoVistaConsultar);
                    if(resultado==null) resultado=" ";
                    datoInsercion += resultado + " -- ";
                }//while (rs.next()) 
                rs.close();
                pstmt.close();
                if (datoInsercion.length()>1) datoInsercion = datoInsercion.substring(0, datoInsercion.length()-4);
                datos.add(datoInsercion);
            }// for (int i = 0; i < expedientes.size(); i++) 

            pstmt = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            for (int i = 0; i < expedientes.size(); i++) {
                pstmt.setString(1, datos.elementAt(i));
                pstmt.setString(2, expedientes.elementAt(i));                
                pstmt.executeUpdate();
            }//for (int i = 0; i < expedientes.size(); i++)
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR INSERTANDO DATO. insertarDatoInteresado ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el prepared statement");
                if(pstmt!=null) pstmt.close();        
            }catch(SQLException e){
                //
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDatoInteresado() : END");
    }//insertarDatoInteresado
    
    private Vector recuperarDatosInforme (SolicitudInformeValueObject siVO, String nomTabla, Connection conexion, AdaptadorSQLBD abd) throws BDException {
        if(m_Log.isDebugEnabled()) m_Log.debug("recuperarDatosInforme() : BEGIN");
        /*
         * En esta función recuperaremos todos los datos que deben ir en el informe y con ellos construiremos la salida
         * del informe.
         */
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector<String> columnas = new Vector <String>();
        Vector orden = new Vector <String>();
        Vector<String> tipo = new Vector <String>();
        Vector informe = new Vector();
        String consultaColumnas = "SELECT " +
            abd.funcionCadena(abd.FUNCIONCADENA_REPLACE, new String[]{"plant_inf_col_origen","' '","''"}) +
            " AS campo, PLANT_INF_COL_ORD AS ORDEN, " +
            "tipo, plant_inf_col_idcampo FROM plant_inf_col INNER JOIN " +
            "inf_campos ON (nomeas = plant_inf_col_origen) WHERE plant_inf_col_plantilla = ? UNION " +
            "SELECT  " +
            abd.funcionCadena(abd.FUNCIONCADENA_REPLACE, new String[]{"plant_inf_col_origen","' '","''"}) +
            " AS campo, PLANT_INF_COL_ORD AS ORDEN, " + 
            "(CASE WHEN plant_inf_col_tabla IN ('E_TNU','E_TNUT','E_TNUC','E_TNUCT') THEN 'N' " + 
            "WHEN plant_inf_col_tabla IN ('E_TFET','E_TFE','E_TFECT','E_TFEC') THEN 'F' " + 
            "WHEN plant_inf_col_tabla = 'E_TXT' OR plant_inf_col_tabla = 'E_TXTT' THEN 'A' " + 
            "WHEN plant_inf_col_tabla IN ('E_TDE','E_TDET','E_TDEX','E_TDEXT') THEN 'D' ELSE 'A' END) AS tipo, " + 
            "plant_inf_col_idcampo" +
            " FROM plant_inf_col WHERE " +
            "plant_inf_col_plantilla = ? AND NOT EXISTS  (SELECT *    FROM inf_campos    " +
            "WHERE nomeas = plant_inf_col_origen) order by plant_inf_col_idcampo";

        String sqlOrden = "";
        try {
            pstmt = conexion.prepareStatement(consultaColumnas);
            pstmt.setInt(1, Integer.valueOf(siVO.getCodPlantilla()));
            pstmt.setInt(2, Integer.valueOf(siVO.getCodPlantilla()));
            if(m_Log.isDebugEnabled()) m_Log.debug("Consulta de campos para la plantilla " + siVO.getCodPlantilla() + " : " + consultaColumnas);
            rs = pstmt.executeQuery();
            while (rs.next()){
                columnas.add(rs.getString("CAMPO"));
                orden.add(rs.getString("ORDEN"));
                tipo.add(rs.getString("TIPO"));
            }//while (rs.next())
            rs.close();
            pstmt.close();
            String sqlInforme = "SELECT DISTINCT ";
            for (int i = 0; i < columnas.size(); i++) {
                sqlInforme += columnas.elementAt(i) + ",";
            }//for (int i = 0; i < columnas.size(); i++) 
            sqlInforme = sqlInforme.substring(0, sqlInforme.length()-1) + " FROM " + nomTabla;

            /* Con los datos del vector columnas y del vector orden podemos
             * hacer la select que nos devuelva todos los datos del informe.
             */
            for (int i=0; i<orden.size(); i++){
                if (!sqlOrden.equals("") && sqlOrden.length()>0 && !orden.elementAt(i).equals("0")) sqlOrden += ",";
                if (orden.elementAt(i).equals("1")) sqlOrden += " " + columnas.elementAt(i) + " ASC";
                if (orden.elementAt(i).equals("2")) sqlOrden += " " + columnas.elementAt(i) + " DESC";
            }//for (int i=0; i<orden.size(); i++)
            if (!sqlOrden.equals("")) sqlOrden = " ORDER BY " + sqlOrden;
            sqlInforme += sqlOrden;
            if(m_Log.isDebugEnabled()) m_Log.debug("sqlInforme = " + sqlInforme);
            pstmt = conexion.prepareStatement(sqlInforme);
            rs = pstmt.executeQuery();

            /*
             * Una vez ejecutada la consulta debemos, simplemente, construir un vector por cada expediente, y
             * devolver un vector que contenga a todos esos vectores.
             */
            Vector expediente = new Vector <String>();
            while (rs.next()){
                //Debemos recorrer el vector de columnas para saber que dato coger.
                expediente = new Vector <String>();
                for (int i=0; i<columnas.size(); i++){
                    // Si el tipo de la columna es una fecha
                    if (tipo.elementAt(i).equals("F")){   
                        java.sql.Timestamp valor = rs.getTimestamp((String)columnas.elementAt(i));
                        if(valor!=null)
                            expediente.add(DateOperations.extraerFechaTimeStamp(valor));
                        else
                            expediente.add("");
                    }else
                        expediente.add(rs.getString((String)columnas.elementAt(i)));
                }//for (int i=0; i<columnas.size(); i++)
                informe.add(expediente);
            }//while (rs.next())
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR RECUPERANDO DATOS. recuperarDatosInforme ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion);
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("recuperarDatosInforme() : END");
        return informe;
    }//recuperarDatosInforme

    private void eliminarTablaTemporal (String nomTabla, Connection conexion, AdaptadorSQLBD abd) throws BDException{
        if(m_Log.isDebugEnabled()) m_Log.debug("eliminarTablaTemporal() : BEGIN");
        try {
            String sql = "TRUNCATE TABLE " + nomTabla;
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            pstmt.executeUpdate();
            pstmt.close();
            sql ="DROP TABLE " + nomTabla;
            pstmt = conexion.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatosInformesEXPEDIENTE.class.getName()).log(Level.SEVERE, null, ex);
            m_Log.error("ERROR ELIMINANDO TABLA TEMPORAL. eliminarTablaTemporal ", ex);
            abd.rollBack(conexion);
            abd.devolverConexion(conexion); 
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("eliminarTablaTemporal() : END");
    }//eliminarTablaTemporal
    
    private void insertarDato (String numExpediente, int tramite, int ocurrencia, String nomTabla, String campo, 
            String valor, Vector <String> campos, Connection conexion) throws SQLException, BDException{
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDato() : BEGIN");

        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            boolean existe = true;

            if (ocurrencia > 1){
                String sql = "SELECT  SIGP_TEMP_EXP" + 
                        " FROM " + nomTabla + 
                        " WHERE SIGP_TEMP_EXP = ? AND SIGP_TEMP_TRA = ? AND SIGP_TEMP_OCU = ?";

                ps = conexion.prepareStatement(sql);
                int i = 1;
                ps.setString(i++, numExpediente);
                ps.setInt(i++, tramite);
                ps.setInt(i++, ocurrencia);
                rs = ps.executeQuery();

                if (!rs.next())
                    existe = false;
            } else {
                if (!tieneTramiteCuerpo)
                    tramite = 0;
                ocurrencia = 0;
            }
            
            if (existe){ 
                String sql = "UPDATE " + nomTabla + " SET " + campo + "= ?" + 
                        " WHERE SIGP_TEMP_EXP = ? AND SIGP_TEMP_TRA = ? AND SIGP_TEMP_OCU = ?";
                ps = conexion.prepareStatement(sql);
                int i = 1;

                ps.setString(i++, valor);
                ps.setString(i++, numExpediente);
                ps.setInt(i++, tramite);
                ps.setInt(i++, ocurrencia);                
                ps.executeUpdate();
            } else {
                StringBuilder camposCopiar = new StringBuilder("SIGP_TEMP_EXP");
                
                for (int i=0;i <campos.size();i++){
                    if (!campo.equals(campos.get(i)))
                        camposCopiar.append("," + campos.get(i));
                }
                                
                String sql = "INSERT INTO " + nomTabla + " (" + camposCopiar.toString() + "," + campo + 
                        ", SIGP_TEMP_TRA, SIGP_TEMP_OCU)" + 
                        " SELECT " + camposCopiar.toString() + ",?, ?, ?" + 
                        " FROM " + nomTabla + 
                        " WHERE SIGP_TEMP_EXP = ? AND SIGP_TEMP_TRA = ? AND SIGP_TEMP_OCU = ?";

                ps = conexion.prepareStatement(sql);
                int i = 1;
                ps.setString(i++, (String) valor);
                ps.setInt(i++, tramite);
                ps.setInt(i++, ocurrencia);
                ps.setString(i++, numExpediente);
                ps.setInt(i++, tieneTramiteCuerpo?tramite:0);
                ps.setInt(i++, 0);
                ps.executeUpdate();
            }                   
        } catch (SQLException ex) {
            m_Log.error("ERROR INSERTANDO DATO. insertarDato ", ex);
            throw ex;
        } finally {
            if(ps!=null) ps.close();
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("insertarDato() : END");
    }
    
}//class
