package es.altia.agora.business.administracion.mantenimiento.persistence.manual;


import es.altia.agora.business.administracion.ParametrosBDVO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.manual.UsuarioDAO;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.util.commons.Pair;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.AdaptadorSQL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;


public class UORsDAO  {
    private static UORsDAO instance = null;
    protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log = LogFactory.getLog(UORsDAO.class.getName());
    // ctes de techserver.properties
    protected static String uor_cod;
    protected static String uor_pad;
    protected static String uor_nom;
    protected static String uor_tipo;
     protected static String uor_rex_xeral;
    protected static String uor_fecha_alta;
    protected static String uor_fecha_baja;
    protected static String uor_estado;
    protected static String uor_cod_vis;
    protected static String uor_email;
    protected static String uor_no_vis;
    protected static String res_uod;
    protected static String res_uor;
    protected static String res_uco;
    protected static String uor_cod_accede;
    protected static String uou_cod_uor;
    protected static String rel_uoi;
    protected static String rel_uof;
    protected static String tra_uin;
    protected static String tra_utr;
    protected static String pui_cod;
    protected static String exp_uor;
    protected static String cro_utr;
    protected static String plant_inf_uor_id;

    private int lastUnidadOrganizativaCreada;


    protected UORsDAO() {
        super();
        // Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

        // ctes de techserver.properties
        uor_cod = m_ConfigTechnical.getString("SQL.A_UOR.codigo");
        uor_pad = m_ConfigTechnical.getString("SQL.A_UOR.padre");
        uor_nom = m_ConfigTechnical.getString("SQL.A_UOR.nombre");
        uor_tipo = m_ConfigTechnical.getString("SQL.A_UOR.tipo");
        uor_rex_xeral = m_ConfigTechnical.getString("SQL.A_UOR.rex_xeral");
        //uor_rex_xeral = "";
        uor_fecha_alta = m_ConfigTechnical.getString("SQL.A_UOR.fechaAlta");
        uor_fecha_baja = m_ConfigTechnical.getString("SQL.A_UOR.fechaBaja");
        uor_estado = m_ConfigTechnical.getString("SQL.A_UOR.estado");
        uor_cod_vis = m_ConfigTechnical.getString("SQL.A_UOR.codigoVisible");
        uor_email = m_ConfigTechnical.getString("SQL.A_UOR.email");
        uor_no_vis = m_ConfigTechnical.getString("SQL.A_UOR.noVisRegistro");
        res_uod = m_ConfigTechnical.getString("SQL.R_RES.unidOrigDest");
        res_uor = m_ConfigTechnical.getString("SQL.R_RES.codUnidad");
        res_uco = m_ConfigTechnical.getString("SQL.R_RES.unidOrigAnot");
        uor_cod_accede = m_ConfigTechnical.getString("SQL.A_UOR.codigoAccede");
        uou_cod_uor = m_ConfigTechnical.getString("SQL.A_UOU.unidadOrg");
        rel_uoi = m_ConfigTechnical.getString("SQL.G_REL.uorInicio");
        rel_uof = m_ConfigTechnical.getString("SQL.G_REL.uorFin");
        tra_uin = m_ConfigTechnical.getString("SQL.E_TRA.unidadInicio");
        tra_utr = m_ConfigTechnical.getString("SQL.E_TRA.unidadTramite");
        pui_cod = m_ConfigTechnical.getString("SQL.E_PUI.codUnidadInicio");
        exp_uor = m_ConfigTechnical.getString("SQL.E_EXP.uor");
        cro_utr = m_ConfigTechnical.getString("SQL.E_CRO.codUnidadTramitadora");
        plant_inf_uor_id = m_ConfigTechnical.getString("SQL.PLANT_INF_UOR.id");
    }

    // singleton
    public static UORsDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        synchronized (UORsDAO.class) {
            if (instance == null) {
                instance = new UORsDAO();
            }
        }
        return instance;
    }

    
    
    public Vector getListaUORsPorNombre(boolean recuperarOcultas,String[] params) {
        Vector resultado = new Vector();

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            sql = "SELECT "+ uor_nom + "," + uor_pad + "," + uor_cod + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_alta + "," +
                    abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_baja + "," +
                    uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede + 
                    ",UOR_OCULTA" +
                    " FROM A_UOR ";
            if (!recuperarOcultas)
                sql += "WHERE UOR_OCULTA = 'N' ";

            sql += "ORDER BY " + uor_nom;


            m_Log.info("getListaUORsPorNombre: " + sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);

            // rellenar DTOs y meterlos en el ArrayList del resultado
            while(rs.next()) {
                UORDTO dto = new UORDTO();
                dto.setUor_nom(rs.getString(uor_nom));
                dto.setUor_cod(rs.getString(uor_cod));
                dto.setUor_cod_vis(rs.getString(uor_cod_vis));
                dto.setUor_cod_accede(rs.getString(uor_cod_accede));
                dto.setUor_estado(rs.getString(uor_estado));
                dto.setUor_fecha_alta(rs.getString(uor_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(uor_fecha_baja));
                dto.setUor_pad(rs.getString(uor_pad));
                dto.setUor_tipo(rs.getString(uor_tipo));
                dto.setUor_email(rs.getString(uor_email));
                dto.setUor_no_registro(rs.getString(uor_no_vis).charAt(0));
                dto.setUorOculta(rs.getString("UOR_OCULTA"));

                resultado.add(dto);
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

    
     public Vector getListaUORsPorNombrePermisoUsuario(UsuarioValueObject usuario, String[] params) {
        Vector resultado = new Vector();

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        
        int codUsu=usuario.getIdUsuario();
        int codOrg=usuario.getOrgCod();
        
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            sql = "SELECT "+ uor_nom + ",NULL as PADRE," + uor_cod + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_alta + "," +
                    abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_baja + "," +
                    uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede +
                    " FROM A_UOR, "+ GlobalNames.ESQUEMA_GENERICO + "A_UOU where UOR_OCULTA = 'N'"
                    + " AND UOU_UOR=UOR_COD AND UOU_ORG="+codOrg+" AND UOU_USU="+codUsu+  
                    " and (uor_pad is null or uor_pad not in (SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU  WHERE UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod()+" ))";
                 

            sql = sql+ "UNION SELECT "+ uor_nom + "," + uor_pad + " AS PADRE," + uor_cod + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_alta + "," +
                    abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_baja + "," +
                    uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede +
                    " FROM A_UOR, "+ GlobalNames.ESQUEMA_GENERICO + "A_UOU where UOR_OCULTA = 'N'"
                    + " AND UOU_UOR=UOR_COD AND UOU_ORG="+codOrg+" AND UOU_USU="+codUsu+
                    " and ( uor_pad  in (SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU  WHERE UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod()+" ))";
                   
            sql=sql+" ORDER BY UOR_NOM";

            m_Log.info("getListaUORsPorNombre: " + sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);

            // rellenar DTOs y meterlos en el ArrayList del resultado
            while(rs.next()) {
                UORDTO dto = new UORDTO();
                dto.setUor_nom(rs.getString(uor_nom));
                dto.setUor_cod(rs.getString(uor_cod));
                dto.setUor_cod_vis(rs.getString(uor_cod_vis));
                dto.setUor_cod_accede(rs.getString(uor_cod_accede));
                dto.setUor_estado(rs.getString(uor_estado));
                dto.setUor_fecha_alta(rs.getString(uor_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(uor_fecha_baja));
                dto.setUor_pad(rs.getString("PADRE"));
                dto.setUor_tipo(rs.getString(uor_tipo));
                dto.setUor_email(rs.getString(uor_email));
                dto.setUor_no_registro(rs.getString(uor_no_vis).charAt(0));

                resultado.add(dto);
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

    /**
     * Obtiene todos los registros del Unidades Organizativas en la BD
     * @param recuperarOcultas Indica si se recpueran las unidades orgánicas
     * @param params Parámetros de conexión
     * @return Lista de DTOs
     */
    public Vector getListaUORs(boolean recuperarOcultas, String[] params) throws TechnicalException {
        Vector resultado = new Vector();

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            resultado = getListaUORs(recuperarOcultas, conexion, abd);
            
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
        return resultado;
    }


     /**
     * Obtiene todos los registros del Unidades Organizativas en la BD que estan dadas de alta
     * @param params Parámetros de conexión
     * @return Lista de DTOs
     */
    public Vector getListaUORsDeAlta(String[] params) throws TechnicalException {
        Vector resultado = new Vector();

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            resultado = getListaUORsDeAlta(conexion, abd);

        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
        return resultado;
    }


    /**
     * Obtiene todos los registros del Unidades Organizativas en la BD
     * @param recuperarOcultas Indica si se recpueran las unidades orgánicas
     * @param params Parámetros de conexión
     * @return Lista de DTOs
     */
    public Vector getListaUORs(boolean recuperarOcultas, Connection con, AdaptadorSQLBD abd) 
            throws TechnicalException {
        Vector resultado = new Vector();
        SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(abd.getParametros()[6]);
        if (unidadesOrg!=null && !unidadesOrg.isEmpty()) {
            for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {
                UORDTO unidad = entry.getValue();
                if (recuperarOcultas || !"S".equals(unidad.getUorOculta()))
                    resultado.add(unidad);
            }
        } 
        return resultado;
    }
    
            /**
     * Obtiene todos los registros del Unidades Organizativas en la BD que estan dada de alta
     * @param params Parámetros de conexión
     * @return Lista de DTOs
     */
    public Vector getListaUORsDeAlta(Connection con, AdaptadorSQLBD abd) throws TechnicalException {
        Vector resultado = new Vector();
        SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(abd.getParametros()[6]);
        if (unidadesOrg!=null && !unidadesOrg.isEmpty()) {
            for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {
                UORDTO unidad = entry.getValue();
                if (unidad.getUor_fecha_baja()==null)
                    resultado.add(unidad);
            }
        } 
        return resultado;
    }



    public Vector getListaUOROrdenPorDesc(String[] params) {
        Vector resultado = new Vector();

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            
            resultado = getListaUOROrdenPorDesc(conexion, params);
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


    /**
     * Obtiene todos los registros del Unidades Organizativas en la BD con el código dado
     * @param codigo Código de unidad
     * @param params Parámetros de conexión
     * @return Lista de DTOs
     */
    public Vector getListaUORsPorCodigo(int codigo,String[] params){
        Vector resultado = new Vector();
        resultado.add((UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],String.valueOf(codigo)));
        return resultado;
    }
    
    
    
    public Vector getListaUORsPorCodigoPermisoUsuario(int codigo,UsuarioValueObject usuario,String[] params){
        Vector resultado = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection(); 
            sql = "SELECT "+ uor_cod + "," + uor_pad + "," + uor_nom + "," + uor_tipo + ","+ uor_cod_vis+                    
                    " FROM A_UOR,"+ GlobalNames.ESQUEMA_GENERICO + "A_UOU  WHERE " + uor_cod + "=" +
                    codigo + "  AND UOU_UOR=UOR_COD AND UOU_ORG="+usuario.getOrgCod()+" AND UOU_USU="+usuario.getIdUsuario()+" "
                    + " ORDER BY " + abd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_LPAD,
                    new String[]{uor_cod_vis,"6"});

            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                UORDTO dto = new UORDTO();
                dto.setUor_cod(rs.getString(uor_cod));
                dto.setUor_cod_vis(rs.getString(uor_cod_vis));         
                dto.setUor_nom(rs.getString(uor_nom));
                dto.setUor_pad(rs.getString(uor_pad));
             

                resultado.add(dto);
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

    /**
     * Obtiene todos los registros del Unidades Organizativas en la BD con el código dado
     * @param noVisible El código es visible ('0') o no ('1') en el registro
     * @param params Parámetros de conexión
     * @return Lista de DTOs
     */
    public Vector getListaUORsPorNoVisRegistro(char noVisible,String[] params){
        Vector resultado = new Vector();
        
        SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(params[6]);
        if (unidadesOrg!=null && !unidadesOrg.isEmpty()) {
            for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {
                UORDTO unidad = entry.getValue();
                if (unidad.getUor_no_registro()==noVisible && "A".equals(unidad.getUor_estado()))
                    resultado.add(unidad);
            }
        }
        return resultado;
    }
    
    
    
    /**
     * Obtiene todos los registros del Unidades Organizativas en la BD con el código dado
     * @param noVisible El código es visible ('0') o no ('1') en el registro. Obtiene las uors a las que tiene acceso el usuario
     * @param params Parámetros de conexión
     * @return Lista de DTOs
     */
    public Vector getListaUORsPorNoVisRegistroPermisoUsuario(char noVisible,UsuarioValueObject usuario,String[] params){
        Vector resultado = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        
        int codUsu=usuario.getIdUsuario();
        int codOrg=usuario.getOrgCod();
        
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();

            // Se comprueba si el usuario tiene la directiva de ver sólo las unidades sobre las que tiene permiso, 
            // sin incluir la unidad de registro
            boolean tieneDirectiva = UsuariosGruposDAO.getInstance().tienePermisoDirectiva(ConstantesDatos.REGISTRO_S_SOLO_UORS_USUARIO, usuario.getIdUsuario(), conexion);
            
            //Primero selecciono las que son numericas            
            sql = "SELECT "+ uor_cod + ",null as PADRE," + uor_nom + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_alta + "," +
                    abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_baja + "," +
                    uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede + "," + uor_rex_xeral +
                    " FROM A_UOR,"+ GlobalNames.ESQUEMA_GENERICO + "A_UOU WHERE " + uor_no_vis + "='" +
                    noVisible + "' AND UOR_ESTADO='A' AND " + abd.whereEsNumero(uor_cod_vis) +
                    " AND UOU_UOR=UOR_COD AND UOU_ORG="+codOrg+" AND UOU_USU="+codUsu+" "+
                    "and (uor_pad is null or uor_pad not in (SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU  WHERE UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod()+"))";
                    
            /** PRUEBA **/
            if(tieneDirectiva){
                sql = sql + " AND (UOR_TIP IS NULL OR UOR_TIP=0) ";                
            }
            /** PRUEBA **/
            
            sql= sql+" UNION SELECT "+ uor_cod + "," + uor_pad + " as PADRE," + uor_nom + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_alta + "," +
                    abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_baja + "," +
                    uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede + "," + uor_rex_xeral +
                    " FROM A_UOR,"+ GlobalNames.ESQUEMA_GENERICO + "A_UOU WHERE " + uor_no_vis + "='" +
                    noVisible + "' AND UOR_ESTADO='A' AND " + abd.whereEsNumero(uor_cod_vis) +
                    " AND UOU_UOR=UOR_COD AND UOU_ORG="+codOrg+" AND UOU_USU="+codUsu+" "+
                    " and (uor_pad in (SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU  WHERE UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod()+"))";
                    
            /** PRUEBA **/
            if(tieneDirectiva){
                sql = sql + " AND (UOR_TIP IS NULL OR UOR_TIP=0) ";                
            }
            /** PRUEBA **/
            
            sql= sql+" ORDER BY "+ uor_cod_vis;

            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                UORDTO dto = new UORDTO();
                dto.setUor_cod(rs.getString(uor_cod));
                dto.setUor_cod_vis(rs.getString(uor_cod_vis));
                dto.setUor_cod_accede(rs.getString(uor_cod_accede));
                dto.setUor_estado(rs.getString(uor_estado));
                dto.setUor_fecha_alta(rs.getString(uor_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(uor_fecha_baja));
                dto.setUor_nom(rs.getString(uor_nom));
                dto.setUor_pad(rs.getString("PADRE"));
                dto.setUor_tipo(rs.getString(uor_tipo));
                dto.setUor_email(rs.getString(uor_email));
                dto.setUor_no_registro(rs.getString(uor_no_vis).charAt(0));
                dto.setUor_rexistro_xeral(rs.getString(uor_rex_xeral));
                resultado.add(dto);
            }
            stmt.close();
            rs.close();
            
//Ahora selecciono las que no son numericas            
            sql = "SELECT "+ uor_cod + ",null AS PADRE," + uor_nom + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_alta + "," +
                    abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_baja + "," +
                    uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede + "," + uor_rex_xeral +
                    " FROM A_UOR,"+ GlobalNames.ESQUEMA_GENERICO + "A_UOU WHERE " + uor_no_vis + "='" +
                    noVisible + "' AND UOR_ESTADO='A' AND " + abd.whereNoEsNumero(uor_cod_vis)+
                    " AND UOU_UOR=UOR_COD AND UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod()+
                    " and (uor_pad is null or uor_pad not in (SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU  WHERE UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod()+"))";
            
            /** PRUEBA **/
            if(tieneDirectiva){
                sql = sql + " AND (UOR_TIP IS NULL OR UOR_TIP=0) ";                
            }
            /** PRUEBA **/
            
            sql=sql+ "UNION SELECT "+ uor_cod + "," + uor_pad + " as PADRE," + uor_nom + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_alta + "," +
                    abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_baja + "," +
                    uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede + "," + uor_rex_xeral +
                    " FROM A_UOR,"+ GlobalNames.ESQUEMA_GENERICO + "A_UOU WHERE " + uor_no_vis + "='" +
                    noVisible + "' AND UOR_ESTADO='A' AND " + abd.whereNoEsNumero(uor_cod_vis)+
                    " AND UOU_UOR=UOR_COD AND UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod()+
                    " and (uor_pad in (SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU  WHERE UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod()+"))";
             
            /** PRUEBA **/
            if(tieneDirectiva){
                sql = sql + " AND (UOR_TIP IS NULL OR UOR_TIP=0) ";                
            }
            /** PRUEBA **/
            
            sql=sql+ "ORDER BY "+uor_cod_vis;
                    

            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                UORDTO dto = new UORDTO();
                dto.setUor_cod(rs.getString(uor_cod));
                dto.setUor_cod_vis(rs.getString(uor_cod_vis));
                dto.setUor_cod_accede(rs.getString(uor_cod_accede));
                dto.setUor_estado(rs.getString(uor_estado));
                dto.setUor_fecha_alta(rs.getString(uor_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(uor_fecha_baja));
                dto.setUor_nom(rs.getString(uor_nom));
                dto.setUor_pad(rs.getString("PADRE"));
                dto.setUor_tipo(rs.getString(uor_tipo));
                dto.setUor_email(rs.getString(uor_email));
                dto.setUor_no_registro(rs.getString(uor_no_vis).charAt(0));
                dto.setUor_rexistro_xeral(rs.getString(uor_rex_xeral));
                resultado.add(dto);
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

    
    
    
        
    public Vector getListaUORsPorNombreNoVisRegistro(char noVisible,String[] params){
        Vector resultado = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            abd = new AdaptadorSQLBD(params); 
            conexion = abd.getConnection();
//Primero selecciono las que son numericas            
            sql = "SELECT "+ uor_cod + "," + uor_pad + "," + uor_nom + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_alta + "," +
                    abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_baja + "," +
                    uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede + "," + uor_rex_xeral +
                    " FROM A_UOR WHERE " + uor_no_vis + "='" +
                    noVisible + "' AND UOR_ESTADO='A' " +
                    " ORDER BY uor_nom" ;

            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                UORDTO dto = new UORDTO();
                dto.setUor_cod(rs.getString(uor_cod));
                dto.setUor_cod_vis(rs.getString(uor_cod_vis));
                dto.setUor_cod_accede(rs.getString(uor_cod_accede));
                dto.setUor_estado(rs.getString(uor_estado));
                dto.setUor_fecha_alta(rs.getString(uor_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(uor_fecha_baja));
                dto.setUor_nom(rs.getString(uor_nom));
                dto.setUor_pad(rs.getString(uor_pad));
                dto.setUor_tipo(rs.getString(uor_tipo));
                dto.setUor_email(rs.getString(uor_email));
                dto.setUor_no_registro(rs.getString(uor_no_vis).charAt(0));
                dto.setUor_rexistro_xeral(rs.getString(uor_rex_xeral));
                resultado.add(dto);
            }
            stmt.close();
            rs.close();
            
           
          
            
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
    
    
    public Vector getListaUORsPorNombreNoVisRegistroPermisoUsuario(char noVisible,UsuarioValueObject usuario,String[] params){
        Vector resultado = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        
        int codUsu=usuario.getIdUsuario();
        int codOrg=usuario.getOrgCod();
        
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
//Primero selecciono las que son numericas            
            sql = "SELECT "+ uor_cod + ",null as PADRE," + uor_nom + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_alta + "," +
                    abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_baja + "," +
                    uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede + "," + uor_rex_xeral +
                    " FROM A_UOR, "+ GlobalNames.ESQUEMA_GENERICO + "A_UOU WHERE " + uor_no_vis + "='" +
                    noVisible + "' AND UOR_ESTADO='A' " +
                    " AND UOU_UOR=UOR_COD AND UOU_ORG="+codOrg+" AND UOU_USU="+codUsu+" "+
                    " and (uor_pad is null or uor_pad not in (SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU  WHERE UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod()+"))";
        
            sql=sql+" UNION SELECT "+ uor_cod + ","+uor_pad+" as PADRE," + uor_nom + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_alta + "," +
                    abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_baja + "," +
                    uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede + "," + uor_rex_xeral +
                    " FROM A_UOR, "+ GlobalNames.ESQUEMA_GENERICO + "A_UOU WHERE " + uor_no_vis + "='" +
                    noVisible + "' AND UOR_ESTADO='A' " +
                    " AND UOU_UOR=UOR_COD AND UOU_ORG="+codOrg+" AND UOU_USU="+codUsu+" "+
                    " and (uor_pad  in (SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU  WHERE UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod()+"))";
        
            sql=sql+" ORDER BY 3";
                    

            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                UORDTO dto = new UORDTO();
                dto.setUor_cod(rs.getString(uor_cod));
                dto.setUor_cod_vis(rs.getString(uor_cod_vis));
                dto.setUor_cod_accede(rs.getString(uor_cod_accede));
                dto.setUor_estado(rs.getString(uor_estado));
                dto.setUor_fecha_alta(rs.getString(uor_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(uor_fecha_baja));
                dto.setUor_nom(rs.getString(uor_nom));
                dto.setUor_pad(rs.getString("PADRE"));
                dto.setUor_tipo(rs.getString(uor_tipo));
                dto.setUor_email(rs.getString(uor_email));
                dto.setUor_no_registro(rs.getString(uor_no_vis).charAt(0));
                dto.setUor_rexistro_xeral(rs.getString(uor_rex_xeral));
                resultado.add(dto);
            }
            stmt.close();
            rs.close();
            
           
          
            
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
    
    /**
     * Obtiene todos los registros del Unidades Organizativas en la BD que sean
     * de registro (RES_TIP = 1)
     * @param params Parámetros de conexión
     * @return Lista de DTOs
     */
    public Vector getListaUORsDeRegistro(String[] params){
        Vector resultado = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            sql = "SELECT "+ uor_cod + "," + uor_pad + "," + uor_nom + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_alta + "," +
                    abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_baja + "," +
                    uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede + "," + uor_rex_xeral +
                    " FROM A_UOR WHERE " + uor_tipo + " = '1' AND UOR_OCULTA = 'N'" +
                    " ORDER BY " + abd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_LPAD,new String[]{uor_nom,"6"});

            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                UORDTO dto = new UORDTO();
                dto.setUor_cod(rs.getString(uor_cod));
                dto.setUor_cod_vis(rs.getString(uor_cod_vis));
                dto.setUor_cod_accede(rs.getString(uor_cod_accede));
                dto.setUor_estado(rs.getString(uor_estado));
                dto.setUor_fecha_alta(rs.getString(uor_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(uor_fecha_baja));
                dto.setUor_nom(rs.getString(uor_nom));
                dto.setUor_pad(rs.getString(uor_pad));
                dto.setUor_tipo(rs.getString(uor_tipo));
                dto.setUor_email(rs.getString(uor_email));
                dto.setUor_no_registro(rs.getString(uor_no_vis).charAt(0));
                dto.setUor_rexistro_xeral(rs.getString(uor_rex_xeral));
                resultado.add(dto);
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

    /**
     * Obtiene todos los registros de Unidades Organizativas en la BD que sean
     * de registro (RES_TIP = 1)y que ademas esten asociadas al usuario usuario, pero solo devuelve codigo y descripcion.
     * @param params Parámetros de conexión
     * @param usuario Usuario
     * @return Lista de DTOs
     */
     public Vector getListaSimpleUORsDeRegistroUsuario(String[] params, UsuarioValueObject usuario)
        throws TechnicalException {
        Vector resultado = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        int totalUOR = 0;
        m_ConfigTechnical = ConfigServiceHelper.getConfig("Registro");
        String permitir_generar_salidas_sin_oficina="NO";
        int codigo_oficina_salidas_tramitacion_defecto=0;
        try{
            permitir_generar_salidas_sin_oficina = m_ConfigTechnical.getString(usuario.getOrgCod()+"/permitir_generar_salidas_sin_oficina");
            codigo_oficina_salidas_tramitacion_defecto = m_ConfigTechnical.getInt(usuario.getOrgCod()+"/codigo_oficina_salidas_tramitacion_defecto");   
        }catch(Exception e)
        { 
            m_Log.error("No existe la propiedad permitir_generar_salidas_sin_oficina o codigo_oficina_salidas_tramitacion_defecto");
        } 
        
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();           
            sql = "SELECT UOR_COD, UOR_NOM FROM A_UOR" +
                    " JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (UOR_COD = UOU_UOR) " +
                    "WHERE UOR_TIP = '1' AND UOR_OCULTA = 'N' AND UOU_USU = ? AND UOU_ORG = ? AND UOU_ENT = ? " +  
                    "ORDER BY  UOR_NOM";
            
            if(m_Log.isDebugEnabled()){
                m_Log.debug(sql);
                m_Log.debug("El codigo de usuario es :" + usuario.getIdUsuario());
                m_Log.debug("El codigo de organizacion es :" + usuario.getOrgCod());
                m_Log.debug("El codigo de entidad es :" + usuario.getEntCod());
            }          
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, usuario.getIdUsuario());
            ps.setInt(2, usuario.getOrgCod());
            ps.setInt(3, usuario.getEntCod());
            rs = ps.executeQuery(); 
            while(rs.next()){
               totalUOR++;
            }
                       
            if(totalUOR > 0 ){
                rs = ps.executeQuery();  
                while(rs.next()){
                    UORDTO dto = new UORDTO();
                    dto.setUor_cod(rs.getString(uor_cod));
                    dto.setUor_nom(rs.getString(uor_nom));
                    resultado.add(dto);
                }
            }
            else{
                if(permitir_generar_salidas_sin_oficina.equals("SI")){
                    UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],String.valueOf(codigo_oficina_salidas_tramitacion_defecto));
                    if (uorDTO!=null && "1".equals(uorDTO.getUor_tipo()) && "A".equals(uorDTO.getUor_estado()))
                        resultado.add(uorDTO);
                }   
            }
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
              
        return resultado;
    }


    /**
     * Obtiene la Unidad Organizativa con ese nombre que esta dada de alta
     * @param nombre Nombre de la unidad
     * @param params Parámetros de conexión
     * @return UORDTO, null si no existe
     */
//se usa en la aplicacion "catalogo Formularios"
    public UORDTO getUORPorNombre(String nombre,String[] params){
        UORDTO dto = null;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            sql = "SELECT "+ uor_cod + "," + uor_pad + "," + uor_nom + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") + " AS " + uor_fecha_alta +
                    "," + abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") + " AS " + uor_fecha_baja +
                    "," + uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede +
                    " FROM A_UOR WHERE " + uor_fecha_baja + " is null AND " + uor_nom + "='" +
                    nombre + "'";

            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                dto = new UORDTO();
                dto.setUor_cod(rs.getString(uor_cod));
                dto.setUor_cod_vis(rs.getString(uor_cod_vis));
                dto.setUor_cod_accede(rs.getString(uor_cod_accede));
                dto.setUor_estado(rs.getString(uor_estado));
                dto.setUor_fecha_alta(rs.getString(uor_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(uor_fecha_baja));
                dto.setUor_nom(rs.getString(uor_nom));
                dto.setUor_pad(rs.getString(uor_pad));
                dto.setUor_tipo(rs.getString(uor_tipo));
                dto.setUor_email(rs.getString(uor_email));
                dto.setUor_no_registro(rs.getString(uor_no_vis).charAt(0));
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

        return dto;
    }

   /**
     * Obtiene la Unidad Organizativa con ese codigo visible que esta dada de alta
     * @param codVis Codigo visible de la unidad
     * @param params Parámetros de conexión
     * @return UORDTO, null si no existe
     */
//se usa en la aplicacion "catalogo Formularios"
    public UORDTO getUORPorCodigoVisible(String codVis,String[] params){
        UORDTO dto = null;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            sql = "SELECT "+ uor_cod + "," + uor_pad + "," + uor_nom + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") + " AS " + uor_fecha_alta +
                    "," + abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") + " AS " + uor_fecha_baja +
                    "," + uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede +
                    " FROM A_UOR WHERE " + uor_fecha_baja + " is null AND " + uor_cod_vis + "='" +
                    codVis + "'";

            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                dto = new UORDTO();
                dto.setUor_cod(rs.getString(uor_cod));
                dto.setUor_cod_vis(rs.getString(uor_cod_vis));
                dto.setUor_cod_accede(rs.getString(uor_cod_accede));
                dto.setUor_estado(rs.getString(uor_estado));
                dto.setUor_fecha_alta(rs.getString(uor_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(uor_fecha_baja));
                dto.setUor_nom(rs.getString(uor_nom));
                dto.setUor_pad(rs.getString(uor_pad));
                dto.setUor_tipo(rs.getString(uor_tipo));
                dto.setUor_email(rs.getString(uor_email));
                dto.setUor_no_registro(rs.getString(uor_no_vis).charAt(0));
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

        return dto;
    }

    /**
     * Obtiene la Unidad Organizativa con ese codigo visible (de alta o baja)
     * @param codVis Codigo visible de la unidad
     * @param params Parámetros de conexión
     * @return UORDTO, null si no existe
     */
    public UORDTO getUORPorCodigoVisibleAll(String codVis,String[] params){
        UORDTO dto = null;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            sql = "SELECT "+ uor_cod + "," + uor_pad + "," + uor_nom + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") + " AS " + uor_fecha_alta +
                    "," + abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") + " AS " + uor_fecha_baja +
                    "," + uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede +
                    " FROM A_UOR WHERE " + uor_cod_vis + "='" +
                    codVis + "'";

            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                dto = new UORDTO();
                dto.setUor_cod(rs.getString(uor_cod));
                dto.setUor_cod_vis(rs.getString(uor_cod_vis));
                dto.setUor_cod_accede(rs.getString(uor_cod_accede));
                dto.setUor_estado(rs.getString(uor_estado));
                dto.setUor_fecha_alta(rs.getString(uor_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(uor_fecha_baja));
                dto.setUor_nom(rs.getString(uor_nom));
                dto.setUor_pad(rs.getString(uor_pad));
                dto.setUor_tipo(rs.getString(uor_tipo));
                dto.setUor_email(rs.getString(uor_email));
                dto.setUor_no_registro(rs.getString(uor_no_vis).charAt(0));
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

        return dto;
    }


    /**
     * Comprueba si la UOR cuyo codigo se pasa como argumento tiene tramites asociados
     * @param conexion Conexion a la BD (Connection)
     * @param codigo Codigo de la UOR  
     * @return int -1 si no tiene tramites, codigo UOR si tiene   
     */
    public int tieneTramitesUOR(Connection conexion, int codigo)
    throws Exception {	    
    	Statement stmt;
    	ResultSet rs;
    	int resultado;
    	String sql;
    	
	    // Comprobacion sobre la tabla G_REL de relaciones entre expedientes en el mismo tramite
	    sql = "SELECT COUNT(*) FROM G_REL WHERE (" + rel_uoi + " = " + codigo + " OR " + rel_uof + " = " + codigo + ") AND REL_FFI IS NULL";
	    if(m_Log.isDebugEnabled()) m_Log.debug("Método tieneTramitesUOR : " + sql);
	    stmt = conexion.createStatement();
	    rs = stmt.executeQuery(sql);
	    resultado = 0;
	    if(rs.next()) resultado = rs.getInt(1);            
	    rs.close();
	    stmt.close();	    
	    if (resultado > 0) {
	    	m_Log.debug("ALERTA: La UOR " + codigo + " tiene tramites asignados");
	    	return codigo; 
	    }	        
	    
	    // Comprobacion sobre la tabla E_TRA de tramites de un procedimiento
	    sql = "SELECT COUNT(*) FROM E_TRA WHERE (" + tra_uin + " = " + codigo + ") " +
	                                        "OR (" + tra_utr + " = " + codigo + ")";
	    if(m_Log.isDebugEnabled()) m_Log.debug("Método tieneTramitesUOR : " + sql);
	    stmt = conexion.createStatement();
	    rs = stmt.executeQuery(sql);
	    resultado = 0;
	    if(rs.next()) resultado = rs.getInt(1);            
	    rs.close();
	    stmt.close();	    
	    if (resultado > 0) {
	    	m_Log.debug("ALERTA: La UOR " + codigo + " tiene tramites asignados");
	    	return codigo; 
	    }
	    
	    // Comprobacion sobre la tabla E_PUI de procedimientos
	    sql = "SELECT COUNT(*) FROM E_PUI WHERE " + pui_cod + " = " + codigo;
	    if(m_Log.isDebugEnabled()) m_Log.debug("Método tieneTramitesUOR : " + sql);
	    stmt = conexion.createStatement();
	    rs = stmt.executeQuery(sql);
	    resultado = 0;
	    if(rs.next()) resultado = rs.getInt(1);            
	    rs.close();
	    stmt.close();	    
	    if (resultado > 0) {
	    	m_Log.debug("ALERTA: La UOR " + codigo + " tiene tramites asignados");
	    	return codigo; 
	    }
	    
	    // Comprobacion sobre la tabla E_EXP de expedientes
	    sql = "SELECT COUNT(*) FROM E_EXP WHERE " + exp_uor + " = " + codigo + " AND EXP_FEF IS NULL";
	    if(m_Log.isDebugEnabled()) m_Log.debug("Método tieneTramitesUOR : " + sql);
	    stmt = conexion.createStatement();
	    rs = stmt.executeQuery(sql);
	    resultado = 0;
	    if(rs.next()) resultado = rs.getInt(1);            
	    rs.close();
	    stmt.close();	    
	    if (resultado > 0) {
	    	m_Log.debug("ALERTA: La UOR " + codigo + " tiene tramites asignados");
	    	return codigo; 
	    }
	    
	    // Comprobacion sobre la tabla E_CRO de tramites de expedientes
	    sql = "SELECT COUNT(*) FROM E_CRO WHERE " + cro_utr + " = " + codigo + " AND CRO_FEF IS NULL";
	    if(m_Log.isDebugEnabled()) m_Log.debug("Método tieneTramitesUOR : " + sql);
	    stmt = conexion.createStatement();
	    rs = stmt.executeQuery(sql);
	    resultado = 0;
	    if(rs.next()) resultado = rs.getInt(1);            
	    rs.close();
	    stmt.close();	    
	    if (resultado > 0) {
	    	m_Log.debug("ALERTA: La UOR " + codigo + " tiene tramites asignados");
	    	return codigo; 
	    }
	    
	    return -1;
    }
    
    /**
     * Comprueba si la UOR cuyo codigo se pasa como argumento o alguna de sus hijas tiene tramites asociados
     * @param conexion Conexion a la BD (Connection)
     * @param codigo Codigo de la UOR  
     * @return int -1 si no tienen tramites, codigo de ALGUNA UOR con tramites asociados si tienen   
     */
    public int tieneTramitesUORRecursiva(Connection conexion, int codigo, String jndi)
    throws Exception {
	    
        // Primero comprobamos si tiene tramites asociados esta UOR, si los tiene se devuelve el codigo	  
        int resultado = tieneTramitesUOR(conexion, codigo);	  	    
        if (resultado > 0) return codigo; 	    

        // En el caso de que esta UOR no tenga tramites, pasamos a la parte recursiva
        Vector uor_cods = new Vector();
        
        SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(jndi);
        if (unidadesOrg!=null) {
            String codigoString = String.valueOf(codigo);
            for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {
                UORDTO unidad = entry.getValue();
                if (codigoString.equals(unidad.getUor_pad()))
                    uor_cods.add(Integer.parseInt(unidad.getUor_cod()));
            }
        } 
        if (!uor_cods.isEmpty()){
            int codigoUOR;
            for (int i=0;i<uor_cods.size();i++) {
                m_Log.debug("LLAMADA tieneTramitesUORRecursiva con: " + uor_cods.elementAt(i));
                codigoUOR = ((Integer) uor_cods.elementAt(i)).intValue();
                resultado = tieneTramitesUORRecursiva(conexion, codigoUOR,jndi);
                if (resultado > -1) {
                	m_Log.debug("ALERTA: La UOR " + resultado + " tiene tramites asignados");
                	return resultado; 
                }
            }
        } 
        return -1;
    }
    
    /**
     * Comprueba si la UOR cuyo codigo se pasa como argumento tiene usuarios asociados
     * @param conexion Conexion a la BD (Connection)
     * @param codigo Codigo de la UOR  
     * @return int -1 si no tiene usuarios, codigo UOR si tiene   
     */
    public int tieneUsuariosUOR(Connection conexion, int codigo, String organizacion)
    throws Exception {

	    // Primero comprobamos si tiene usuarios asociados esta UOR, si los tiene se devuelve el codigo
	    String sql = "SELECT COUNT(*) FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU WHERE " + uou_cod_uor + " = " + 
                codigo + " AND UOU_ORG=" + organizacion;
	    if(m_Log.isDebugEnabled())
	        m_Log.debug("Método tieneUsuariosUOR : " + sql);
	    Statement stmt = conexion.createStatement();
	    ResultSet rs = stmt.executeQuery(sql);
	    int resultado = 0;
	    if(rs.next()) resultado = rs.getInt(1);            
	    rs.close();
	    stmt.close();
	    
	    if (resultado > 0) {
	    	m_Log.debug("ALERTA: La UOR " + codigo + " tiene usuarios asignados");
	    	return codigo; 
	    }	        
	    return -1;
    }
    
    /**
     * Comprueba si la UOR cuyo codigo se pasa como argumento o alguna de sus hijas tiene usuarios asociados
     * @param conexion Conexion a la BD (Connection)
     * @param codigo Codigo de la UOR  
     * @return int -1 si no tienen usuarios, codigo de ALGUNA UOR con usuarios asociados si tienen   
     */
    public int tieneUsuariosUORRecursiva(Connection conexion, int codigo, String organizacion,String jndi)
    throws Exception {
	    
        // Primero comprobamos si tiene usuarios asociados esta UOR, si los tiene se devuelve el codigo	  
        int resultado = tieneUsuariosUOR(conexion, codigo, organizacion);
        if (resultado > 0) return codigo; 	    
	    
        // En el caso de que esta UOR no tenga usuarios, pasamos a la parte recursiva
        Vector uor_cods = new Vector();
        SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(jndi);
        if (unidadesOrg!=null) {
            String codigoString = String.valueOf(codigo);
            for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {
                UORDTO unidad = entry.getValue();
                if (codigoString.equals(unidad.getUor_pad()))
                    uor_cods.add(Integer.parseInt(unidad.getUor_cod()));
            }
        } 
        if (!uor_cods.isEmpty()){  // Tiene hijos
            int codigoUOR;
            for (int i=0;i<uor_cods.size();i++) {
                m_Log.debug("LLAMADA tieneUsuariosUORRecursiva con: " + uor_cods.elementAt(i));
                codigoUOR = ((Integer) uor_cods.elementAt(i)).intValue();
                resultado = tieneUsuariosUORRecursiva(conexion, codigoUOR, organizacion,jndi);
                if (resultado > -1) {
                	m_Log.debug("ALERTA: La UOR " + resultado + " tiene usuarios asignados");
                	return resultado; 
                }
            }
        } 
        return -1;
    }
           
    /**
     * Comprueba si la UOR cuyo codigo se pasa como argumento tiene registros asociados
     * @param conexion Conexion a la BD (Connection)
     * @param codigo Codigo de la UOR  
     * @return int -1 si no tiene registros, codigo UOR si tiene   
     */
    public int tieneRegistrosUOR(Connection conexion, int codigo)
    throws Exception {
	    
	    // Primero comprobamos si tiene registros asociados esta UOR, si los tiene se devuelve el codigo
	    String sql = "SELECT COUNT(*) FROM R_RES WHERE (" + res_uod + " = " + codigo + ") ";
	          sql += "OR (" + res_uor + " = " + codigo + ") OR (res_uco " + "=" + codigo + ")";
	    if(m_Log.isDebugEnabled())
	        m_Log.debug("Método tieneRegistrosUOR : " + sql);
	    Statement stmt = conexion.createStatement();
	    ResultSet rs = stmt.executeQuery(sql);
	    int resultado = 0;
	    if(rs.next()) resultado = rs.getInt(1);            
	    rs.close();
	    stmt.close();
	    
	    if (resultado > 0) {
	    	m_Log.debug("ALERTA: La UOR " + codigo + " tiene registros asignados");
	    	return codigo; 
	    }
	    // No hay registros asociados, se devuelve -1    
	    return -1;
    }
    
    /**
     * Comprueba si la UOR cuyo codigo se pasa como argumento o alguna de sus hijas tiene registros asociados
     * @param conexion Conexion a la BD (Connection)
     * @param codigo Codigo de la UOR  
     * @return int -1 si no tienen registros, codigo de ALGUNA UOR con registros asociados si tienen   
     */
    public int tieneRegistrosUORRecursiva(Connection conexion, int codigo,String jndi)
    throws Exception {
    
        // Primero comprobamos si tiene registros asociados esta UOR, si los tiene se devuelve el codigo
        int resultado = tieneRegistrosUOR(conexion,codigo);       
        if (resultado > 0) return codigo; 

        // En el caso de que esta UOR no tenga registros, pasamos a la parte recursiva
        Vector uor_cods = new Vector();
        SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(jndi);
        if (unidadesOrg!=null) {
            String codigoString = String.valueOf(codigo);
            for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {
                UORDTO unidad = entry.getValue();
                if (codigoString.equals(unidad.getUor_pad()))
                    uor_cods.add(Integer.parseInt(unidad.getUor_cod()));
            }
        } 
        if (!uor_cods.isEmpty()){  // Tiene hijos
        int codigoUOR;
        for (int i=0;i<uor_cods.size();i++) {
            m_Log.debug("LLAMADA tieneRegistrosUORRecursiva con: " + uor_cods.elementAt(i));
            codigoUOR = ((Integer) uor_cods.elementAt(i)).intValue();
            resultado = tieneRegistrosUORRecursiva(conexion, codigoUOR, jndi);
            if (resultado > -1) {
            	m_Log.debug("ALERTA: La UOR " + resultado + " tiene registros asociados");
            	return resultado; 
            }
        }
    } 
    return -1;
    }
    
    /**
     * Comprueba si la UOR cuyo codigo se pasa como argumento tiene permisos para 
     * algun informe asociados
     * @param conexion Conexion a la BD (Connection)
     * @param codigo Codigo de la UOR  
     * @return int -1 si no tiene informes, codigo UOR si tiene   
     */
    public int tieneInformesUOR(Connection conexion, int codigo)
    throws Exception {
	    
	    // Primero comprobamos si tiene informes asociados esta UOR, si los tiene se devuelve el codigo
	    String sql = "SELECT COUNT(*) FROM PLANT_INF_UOR WHERE " + plant_inf_uor_id + " = " + codigo;
	    if(m_Log.isDebugEnabled())
	        m_Log.debug("Método tieneInformesUOR : " + sql);
	    Statement stmt = conexion.createStatement();
	    ResultSet rs = stmt.executeQuery(sql);
	    int resultado = 0;
	    if(rs.next()) resultado = rs.getInt(1);            
	    rs.close();
	    stmt.close();
	    
	    if (resultado > 0) {
	    	m_Log.debug("ALERTA: La UOR " + codigo + " tiene informes asignados");
	    	return codigo; 
	    }
	    // No hay informes asociados, se devuelve -1    
	    return -1;
    }
    
    /**
     * Comprueba si la UOR cuyo codigo se pasa como argumento o alguna de sus hijas tiene permisos para 
     * algun informe asociados
     * @param conexion Conexion a la BD (Connection)
     * @param codigo Codigo de la UOR  
     * @return int -1 si no tienen informes, codigo de ALGUNA UOR con informes asociados si tienen   
     */
    public int tieneInformesUORRecursiva(Connection conexion, int codigo, String jndi)
    throws Exception {
    
        // Primero comprobamos si tiene informes asociados esta UOR, si los tiene se devuelve el codigo
        int resultado = tieneInformesUOR(conexion,codigo);       
        if (resultado > 0) return codigo; 

        // En el caso de que esta UOR no tenga informes, pasamos a la parte recursiva
        Vector uor_cods = new Vector();
        SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(jndi);
        if (unidadesOrg!=null) {
            String codigoString = String.valueOf(codigo);
            for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {
                UORDTO unidad = entry.getValue();
                if (codigoString.equals(unidad.getUor_pad()))
                    uor_cods.add(Integer.parseInt(unidad.getUor_cod()));
            }
        } 
        if (!uor_cods.isEmpty()){  // Tiene hijos
        int codigoUOR;
        for (int i=0;i<uor_cods.size();i++) {
            m_Log.debug("LLAMADA tieneInformesUORRecursiva con: " + uor_cods.elementAt(i));
            codigoUOR = ((Integer) uor_cods.elementAt(i)).intValue();
            resultado = tieneInformesUORRecursiva(conexion, codigoUOR,jndi);
            if (resultado > -1) {
            	m_Log.debug("ALERTA: La UOR " + resultado + " tiene informes asociados");
            	return resultado; 
            }
        }
    } 
    return -1;
    }
    
    /**
     * Elimina de la BD las Unidades Organizativas con el código dado, llama a BorrarDependencias para la recursión.
     * @param codigo Código de unidad
     * @param params Parámetros de conexión
     * @return Resultado de la consulta: >0 = OK, -2 = existen registros, -3 = existen usuarios,
     *         -4 = existen tramites, -5 = existen informes, otros = ERROR
     */
    public int eliminarUORPorCodigo(int codigo, String[] params, String organizacion){
        int resultado = 0;

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            // verificar que no hay registros asociados al código de unidad orgánica o alguna de sus hijas            
            int tieneRegistros = tieneRegistrosUORRecursiva(conexion, codigo,params[6]);             
            if(tieneRegistros > -1) {
                return -2;
            }

            // verificar que no hay usuarios asociados al código de unidad orgánica o alguna de sus hijas
            int tieneUsuarios = tieneUsuariosUORRecursiva(conexion, codigo, organizacion,params[6]);
            if (tieneUsuarios > -1) {
            	return -3;
            }
            
            // verificar que no hay tramites asociados al código de unidad orgánica o alguna de sus hijas
            int tieneTramites = tieneTramitesUORRecursiva(conexion,codigo,params[6]);
            if (tieneTramites > -1) {
            	return -4;
            }
            
            // verificar que no hay permisos para algun informe asociados al código de unidad orgánica o alguna de sus hijas
            int tieneInformes = tieneInformesUORRecursiva(conexion,codigo,params[6]);
            if (tieneInformes > -1) {
            	return -5;
            }
            
            // verificar que la UOR no está asignada como oficina de registro en alguna anotación de registro
            int tieneOfisRegistro = tieneAnotacionesRegistroConOficinaRegistro(conexion,codigo);
            if (tieneOfisRegistro > 0) {
            	return -6;
            }
            
            // si no hay registros asociados, continuamos con el borrado
            Vector uor_cods = new Vector();
            SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(abd.getParametros()[6]);
            if (unidadesOrg!=null) {
                for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {
                    UORDTO unidad = entry.getValue();
                    if (String.valueOf(codigo).equals(unidad.getUor_pad()))
                        uor_cods.add(unidad.getUor_cod());
                }
            } 
            for (int i=0;i<uor_cods.size();i++) {
                m_Log.debug("LLAMA A BORRARDEPENDENCIAS CON: "+uor_cods.elementAt(i));
                BorrarDependencias(conexion,params[6],(String)uor_cods.elementAt(i));
            }
            stmt = conexion.createStatement();
            sql = "DELETE FROM A_UOR WHERE " + uor_cod + "=" + codigo;
            if(m_Log.isDebugEnabled())
                m_Log.debug("Método eliminarUORPorCodigo: " + sql);
            resultado = stmt.executeUpdate(sql);
            if (resultado > 0)
                CacheDatosFactoria.getImplUnidadesOrganicas().eliminarDatoClaveUnica(params[6],String.valueOf(codigo));
        } catch(Exception e) {
            rollBackTransaction(abd,conexion,e);
        } finally {
            try {
                if(stmt != null)
                    stmt.close();
                commitTransaction(abd,conexion);
            } catch(SQLException e) {
                m_Log.error(e);
            }
        }
        return resultado;
    }

    public void BorrarDependencias(Connection con, String jndi, String codigo)
        throws Exception {
        Statement stmt = null;
        String consulta;
        ResultSet rs;
        Vector uor_cods = new Vector();
        SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(jndi);
        if (unidadesOrg!=null) {
            for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {
                UORDTO unidad = entry.getValue();
                if (String.valueOf(codigo).equals(unidad.getUor_pad()))
                    uor_cods.add(unidad.getUor_cod());
            }
        } 
        if (!uor_cods.isEmpty()) {
            for (int i=0;i<uor_cods.size();i++) {
                m_Log.debug("LLAMA A BORRARDEPENDENCIAS CON: "+uor_cods.elementAt(i));
                BorrarDependencias(con,jndi,(String)uor_cods.elementAt(i));
            }
            consulta = "delete from A_UOR where " + uor_cod + "=" + codigo;
            stmt = con.createStatement();
            m_Log.debug("Consulta en BorrarDependencias: "+consulta);
            int resultado = stmt.executeUpdate(consulta);
            stmt.close();
            if (resultado > 0)
                CacheDatosFactoria.getImplUnidadesOrganicas().eliminarDatoClaveUnica(jndi,codigo);
        } else {         // No tiene hijos
            consulta = "delete from A_UOR where " + uor_cod + "=" + codigo;
            stmt = con.createStatement();
            m_Log.debug("Consulta en BorrarDependencias: "+consulta);
            int resultado = stmt.executeUpdate(consulta);
            stmt.close();
            if (resultado > 0)
                CacheDatosFactoria.getImplUnidadesOrganicas().eliminarDatoClaveUnica(jndi,codigo);
        }
    }

    /**
     * Modifica una UOR presente en la BD.
     * @param dto DTO del tipo UOR
     * @param params Parámetros de conexión
     * @return Resultado de la consulta: >0 = OK, -2 = existen registros, -3 = existen usuarios, 
     *         -4 = existen tramites, -5 = existen informes, otros = ERROR
     */
    public int modificarUOR(UORDTO dto, String[] params, String organizacion) {
        // al menos el código debe estar definido
        if((dto.getUor_cod() == null)||(dto.getUor_cod_vis()== null)) {
            return -1;
        }

        int resultado = -1;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        String sql = "";
        String sql2="";

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
                       
           /* Comprobar si esta de alta y se pretende dar de baja, y en ese caso comprobar que no tenga
            * registros o usuarios asignados. */
            UORDTO uORActual = getUORPorCodigoVisible(dto.getUor_cod_vis(), params);
            String estadoActual = "A"; // por defecto asumimos esta de alta
            if (uORActual != null) estadoActual = uORActual.getUor_estado();
            String estadoNuevo = dto.getUor_estado();
            if ((estadoActual.equals("A")) && (estadoNuevo.equals("B"))) {
            	
            	int codigo = Integer.parseInt(dto.getUor_cod());   
               
                // verificar que no hay tramites asociados al código de unidad orgánica
                int tieneTramites = tieneTramitesUOR(conexion,codigo);
                if (tieneTramites > -1) {
                	return -4;
                }

            }
            //si rexistro xeral es =1 (esta activado) ponemos todos los que haya a cero pq solo uno puede estar activado
            //sino hacemos el update normal, sino compruebo si es null da error 
            if(dto.getUor_rexistro_xeral() != null) {
            if(dto.getUor_rexistro_xeral().compareTo("1")==0) {
                sql2="update A_UOR set " + uor_rex_xeral + "='0'";
                stmt = conexion.createStatement();
                resultado = stmt.executeUpdate(sql2);
                stmt.close();
                if (resultado>0) {
                    SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(abd.getParametros()[6]);
                    if (unidadesOrg!=null && !unidadesOrg.isEmpty()) {
                        for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {
                            UORDTO unidad = entry.getValue();
                            if (!"0".equals(unidad.getUor_rexistro_xeral())){
                                unidad.setUor_rexistro_xeral("0");
                                CacheDatosFactoria.getImplUnidadesOrganicas().actualizarDatoClaveUnica(unidad,abd.getParametros()[6],unidad.getUor_cod());
                            }
                        }
                    }
                }
            }
            }
            //-----------------------------------------------------
            
            sql = "update A_UOR set ";

            if (dto.getUor_pad() != null && "".equals(dto.getUor_pad())) 
                dto.setUor_pad(null);
            if(!"B".equals(dto.getUor_estado()))
                dto.setUor_fecha_baja(null);
            if(dto.getUor_tipo() == null) 
                dto.setUor_tipo("");
            if(dto.getUor_no_registro() == '\u0000') 
                dto.setUor_no_registro('0');

            if(dto.getUor_rexistro_xeral() != null) 
                dto.setUor_rexistro_xeral("1");
            else 
                dto.setUor_rexistro_xeral("0");
                        
            if(dto.getUor_cod_vis() != null) {
                sql += uor_cod_vis + "='" + dto.getUor_cod_vis() + "'";
            }
            if(dto.getUor_pad() != null) {
                sql += ", " + uor_pad + "=" + dto.getUor_pad();
            } else sql += ", " + uor_pad + "=NULL";
            if(dto.getUor_estado() != null) {
                sql += ", " + uor_estado + "='" + dto.getUor_estado() + "'";
                // Si el estado actual es ACTIVO se pone a null la fecha de baja
            }
            if(dto.getUor_fecha_alta() != null) {
                sql += ", " + uor_fecha_alta + "="+
                        abd.convertir("'" + dto.getUor_fecha_alta() + "'", 
                            AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") ;
            }

            if(dto.getUor_estado().equals("B")){
                if(dto.getUor_fecha_baja() != null) 
                    sql += ", " + uor_fecha_baja + "=" + abd.convertir("'" + dto.getUor_fecha_baja() + "'", 
                            AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") ;
                
                sql += ", UOR_OCULTA = '" + dto.getUorOculta() + "'";
            } else {
                sql += ", " + uor_fecha_baja + "=NULL";
                sql += ", UOR_OCULTA = 'N'";
            }
            
            if(dto.getUor_nom() != null) {
                sql += ", " + uor_nom + "='" + dto.getUor_nom() + "'";
            }
            // tipo es un caso especial, pq es un check box t no sabemos los posibles valores en BD
            //if(dto.getUor_tipo() != null) {
            // #234327: se añade la comprobación de que la propiedad no venga vacía
            if(dto.getUor_tipo() != null && !dto.getUor_tipo().equals("")) {
                sql += ", " + uor_tipo + "='1'";
            }
            else {
                sql += ", " + uor_tipo + "=''";
            }
            if(dto.getUor_email() != null) 
                sql += ", " + uor_email + "='" + dto.getUor_email() + "'";
                        
            sql += ", " + uor_no_vis + "='" + dto.getUor_no_registro() + "'";
            
            sql += ", " + uor_rex_xeral + "='"+dto.getUor_rexistro_xeral()+"'";
            
            /*** OFICINA REGISTRO ***
            if((dto.getUor_pad() != null)&&(!dto.getUor_pad().equals(""))) {
                // Si la UOR a dar de alta tiene un padre, se comprueba si éste es una unidad de tipo Registro
                boolean padreUnidadRegistro = this.esUnidadRegistro(Integer.parseInt(dto.getUor_pad()), conexion);
                boolean padreOficinaRegistro = this.esOficinaRegistro(Integer.parseInt(dto.getUor_pad()), conexion);
                if(padreUnidadRegistro || padreOficinaRegistro)
                    sql+=",OFICINA_REGISTRO=1";
                else
                    sql+=",OFICINA_REGISTRO=0";
            }else
                sql+=",OFICINA_REGISTRO=0";
            /*** OFICINA REGISTRO ***/
            
            if(dto.isOficinaRegistro())
                sql+=",OFICINA_REGISTRO=1";
            else
                sql+=",OFICINA_REGISTRO=0";
            
            sql += " where uor_cod = " + dto.getUor_cod();

            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);

            stmt = conexion.createStatement();
            resultado = stmt.executeUpdate(sql);
            if (resultado>0){
                dto.setJndi(params[6]);
                CacheDatosFactoria.getImplUnidadesOrganicas().actualizarDatoClaveUnica(dto,params[6],dto.getUor_cod());
            }
        }

        catch(Exception e) {
            rollBackTransaction(abd,conexion,e);
            m_Log.error(e);
        }
        finally {
            try {
                if(stmt != null)
                    stmt.close();
                commitTransaction(abd,conexion);
            } catch (Exception e) {m_Log.error(e);}
        }

        return resultado;
    }

    /**
     * Modifica solo el código accede si está habilitado.
     * @param dto DTO del tipo UOR
     * @param params Parámetros de conexión
     * @return Número de registros afectados o -1 si hubo algún problema
     */
    public int modificarUORCodAccede(UORDTO dto, String[] params) {
        // al menos el código debe estar definido
        if((dto.getUor_cod() == null)||(dto.getUor_cod_vis()== null)) {
            return -1;
        }

        int resultado = -1;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        boolean esNull = false;
        if ("".equals(dto.getUor_cod_accede()) || "undefined".equals(dto.getUor_cod_accede())) {
            esNull = true;
        }
        String sql = "";

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            if (esNull) {
                sql = "update A_UOR set " + uor_cod_accede + "= NULL";
                sql += " where uor_cod = " + dto.getUor_cod();
            } else {
                sql = "update A_UOR set " + uor_cod_accede + "='" + dto.getUor_cod_accede() + "'";
                sql += " where uor_cod = " + dto.getUor_cod();
            }
            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);

            stmt = conexion.createStatement();
            resultado = stmt.executeUpdate(sql);
            if (resultado > 0){
                UORDTO unidad = (UORDTO) CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],dto.getUor_cod());
                unidad.setUor_cod_accede(esNull?null:dto.getUor_cod_accede());
                CacheDatosFactoria.getImplUnidadesOrganicas().actualizarDatoClaveUnica(unidad,params[6],dto.getUor_cod());
            }
        }

        catch(Exception e) {
            rollBackTransaction(abd,conexion,e);
            m_Log.error(e);
        }
        finally {
            try {
                if(stmt != null)
                    stmt.close();
                commitTransaction(abd,conexion);
            } catch (Exception e) {m_Log.error(e);}
        }

        return resultado;
    }
    
    /**
     * Inserta una UOR en la BD con los datos del dto
     * @param dto DTO del tipo UOR
     * @param params Parámetros de conexión
     * @return Row count del INSERT o -1 si hay algún problema
     */
    public int altaUOR(UORDTO dto, String[] params){
        int resultado = -1;

        // comprobar campos obligatorios
        if((dto.getUor_cod_vis()== null)||(dto.getUor_estado()==null)||
                (dto.getUor_fecha_alta() == null)||(dto.getUor_nom() == null)) {
            m_Log.error("Algún campo necesario para el alta de UOR es nulo");
            return -1;
        }

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        String sql = "";
        ResultSet rs = null;

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            // para no depender de secuencias, obtener el último valor de la PK
            int codigo_obtenido = -1;

            SortedMap <String,SortedMap<String,UORDTO>> unidadesOrg = (SortedMap <String,SortedMap<String,UORDTO>>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatos();
            if (unidadesOrg!=null) 
                codigo_obtenido = Integer.parseInt(unidadesOrg.get(abd.getParametros()[6]).lastKey());
            
            // queremos usar el siguiente número disponible como código
            codigo_obtenido++;

            setLastUnidadOrganizativaCreada(codigo_obtenido);
            // si no hemos obtenido un código ha habido algún problema
            if(codigo_obtenido == -1) {
                resultado = -1;
            } else { // realizar la inserción en BD
                sql = "INSERT INTO A_UOR ( UOR_COD, UOR_PAD, UOR_NOM, UOR_TIP, UOR_COD_VIS, " +
                        "UOR_FECHA_ALTA, UOR_FECHA_BAJA, UOR_ESTADO, UOR_EMAIL, UOR_NO_VIS, UOR_COD_ACCEDE,OFICINA_REGISTRO) VALUES (";
                
                dto.setUor_cod(String.valueOf(codigo_obtenido));
                if (dto.getUor_pad() != null && dto.getUor_pad().equals(""))
                    dto.setUor_pad(null);
                if(dto.getUor_tipo() != null && dto.getUor_tipo().equals("")) 
                    dto.setUor_tipo(null);
                if(dto.getUor_fecha_alta() != null && dto.getUor_fecha_alta().equals(""))
                    dto.setUor_fecha_alta(null);
                if(dto.getUor_fecha_baja() != null && dto.getUor_fecha_baja().equals(""))
                    dto.setUor_fecha_baja(null);
                if(dto.getUor_email() != null && dto.getUor_email().equals("")) 
                    dto.setUor_email(null);
                if(dto.getUor_no_registro() != '1') 
                    dto.setUor_no_registro('0');
                    
                // código
                sql += codigo_obtenido + ",";
                // código del padre
                if(dto.getUor_pad() != null) {
                    sql += "'" + dto.getUor_pad() + "',";
                } else {
                    sql += "NULL,";
                }
                // nombre
                sql += "'" + dto.getUor_nom() + "',";
                // tipo
                if((dto.getUor_tipo() != null)&&(!dto.getUor_tipo().equals(""))) {
                    sql += "'" + dto.getUor_tipo() + "',";
                } else {
                    sql += "NULL,";
                }
                // cod visible
                sql += "'" + dto.getUor_cod_vis() + "',";
                // fecha alta
                if(dto.getUor_fecha_alta() != null) {
                    sql += abd.convertir("'" + dto.getUor_fecha_alta() + "'", 
                            AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") 
                            + ",";
                } else {
                    sql += "NULL,";
                }
                // fecha baja
                if(dto.getUor_fecha_baja() != null) {
                    sql += "'" + dto.getUor_fecha_baja() + "',";
                } else {
                    sql += "NULL,";
                }
                // estado
                sql += "'" + dto.getUor_estado() + "',";
                // email
                if(dto.getUor_email() != null) {
                    sql += "'" + dto.getUor_email() + "',";
                }else {
                    sql += "NULL,";
                }
                // no visible en el registro
                sql += "'" + dto.getUor_no_registro() + "'";
                // cod visible
                sql += ",'" + dto.getUor_cod_accede() + "'";
                
                /*** OFICINA REGISTRO ***
                if((dto.getUor_pad() != null)&&(!dto.getUor_pad().equals(""))) {
                    // Si la UOR a dar de alta tiene un padre, se comprueba si éste es una unidad de tipo Registro
                    boolean padreUnidadRegistro = this.esUnidadRegistro(Integer.parseInt(dto.getUor_pad()), conexion);
                    boolean padreOficinaRegistro = this.esOficinaRegistro(Integer.parseInt(dto.getUor_pad()), conexion);
                    if(padreUnidadRegistro || padreOficinaRegistro)
                        sql+=",1";
                    else
                        sql+=",0";
                }else
                    sql+=",0";
                /*** OFICINA REGISTRO ***/
                
                
                /*** OFICINA REGISTRO ***/
                if(dto.isOficinaRegistro())
                    sql+=",1";
                else
                    sql+=",0";
                
                /*** OFICINA REGISTRO ***/
                
                sql += ")";

                if(m_Log.isDebugEnabled())
                    m_Log.debug(sql);

                stmt = conexion.createStatement();
                resultado = stmt.executeUpdate(sql);
                if (resultado > 0){
                    dto.setJndi(params[6]);
                    CacheDatosFactoria.getImplUnidadesOrganicas().insertarDato(dto);
                }
            }

        }

        catch(Exception e) {
            rollBackTransaction(abd,conexion,e);
            m_Log.error(e);
        }
        finally {
            try {
                if(rs != null) {
                    rs.close();
                }
                if(stmt != null) {
                    stmt.close();
                }
                commitTransaction(abd,conexion);
            } catch (Exception e) {
                m_Log.error(e);
            }
        }

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

    
    /**
     * Establece el código de la última unidad organizativa creada
     * @param codigo
     */
    public void setLastUnidadOrganizativaCreada(int codigo)
    {
        this.lastUnidadOrganizativaCreada = codigo;
    }
    
    /**
     * Devuelve el código de la última unidad organizativa creada una vez que haya sido dada de alta
     * @return int
     */
    public int getLastUnidadOrganizativaCreada()
    {
        return this.lastUnidadOrganizativaCreada;
    }
    
    /**
     * Comprueba la existencia de una uor buscando por el código visible de la misma
     * @param codUor: Código de uor visible
     * @param con: Conexión a la base de datos
     * @return Código visible de uor
     */
    public boolean existeUorByCodigoVisible(String codUor,Connection con){
        boolean exito = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";        
        try{
            
            sql = "SELECT COUNT(*) AS NUM FROM A_UOR WHERE UOR_COD_VIS=?";
             m_Log.debug("existeUorByCodigoVisible: "  + sql);
            ps= con.prepareStatement(sql);
            int i=1;
            ps.setString(i++,codUor);
            rs = ps.executeQuery();
            while(rs.next()){
                int num = rs.getInt("NUM");
                if(num>=1) exito = true;
            }

           
            
        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
                if(rs!= null) rs.close();
                if(ps!= null) ps.close();
            } catch (Exception e) {m_Log.error(e);}
        }        
        return exito;
        
    }


 /**
     * Recupera el código visible de una uor a partir del código interno de la misma
     * @param codUor: Código de uor
     * @param con: Conexión a la base de datos
     * @return Código visible de uor o null si no se ha podido recuperar
     */
    public String getCodigoVisibleUorByCodUor(String codUor,String jndi){
        UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(jndi,codUor);

        return uorDTO.getUor_cod_vis();
    }


  /**
     * Recupera el nombre de una uor a partir del código de uor visible
     * @param codUor: Código de uor visible
     * @param con: Conexión a la base de datos
     * @return Nombre la uor o null si no existe
     */
    public String getNombreByCodUorVisible(String codUor,Connection con){
        String salida = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        try{

            sql = "SELECT UOR_NOM FROM A_UOR WHERE UOR_COD_VIS=?";
             m_Log.debug("getNombreByCodUorVisible: "  + sql);
            ps= con.prepareStatement(sql);
            int i=1;
            ps.setString(i++,codUor);
            rs = ps.executeQuery();
            while(rs.next()){
                salida = rs.getString("UOR_NOM");
            }

        }catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            try {
                if(rs!= null) rs.close();
                if(ps!= null) ps.close();
            } catch (Exception e) {m_Log.error(e);}
        }
        return salida;
    }


    /******* prueba *********/
    /**
     * Recuperal a lista de uors sobre las que tiene permiso un determinado usuario
     * @param usuario: Objeto de tipo UsuarioValueObject que contiene la información del usuario
     * @param con: Conexión a la BBDD
     * @param abd: AdaptadorSQLBD
     * @return Vector
     * @throws es.altia.common.exception.TechnicalException
     */
     public Vector getListaUORsPermisoUsuario(UsuarioValueObject usuario,Connection con, AdaptadorSQLBD abd) throws TechnicalException {
        Vector resultado = new Vector();

        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            sql = "SELECT "+ uor_cod + "," + uor_pad + "," + uor_nom + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_alta + "," +
                    abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_baja + "," +
                    uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede + "," + uor_rex_xeral +
                    " FROM A_UOR " +
                    "WHERE UOR_OCULTA = 'N' " +
                    "AND UOR_COD IN (SELECT UOU_UOR FROM " +
                                      GlobalNames.ESQUEMA_GENERICO +  "A_UOU  " +
                                      "WHERE UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod() + ") " + 
                    "ORDER BY " + abd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_LPAD,
                    new String[]{uor_cod_vis,"6"});


            m_Log.info("getListaUORsPermisoUsuario: " + sql);
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            // rellenar DTOs y meterlos en el ArrayList del resultado
            while(rs.next()) {
                UORDTO dto = new UORDTO();
                dto.setUor_cod(rs.getString(uor_cod));
                dto.setUor_cod_vis(rs.getString(uor_cod_vis));
                dto.setUor_cod_accede(rs.getString(uor_cod_accede));
                dto.setUor_estado(rs.getString(uor_estado));
                dto.setUor_fecha_alta(rs.getString(uor_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(uor_fecha_baja));
                dto.setUor_nom(rs.getString(uor_nom));
                dto.setUor_pad(rs.getString(uor_pad));
                dto.setUor_tipo(rs.getString(uor_tipo));
                dto.setUor_email(rs.getString(uor_email));
                dto.setUor_no_registro(rs.getString(uor_no_vis).charAt(0));
                dto.setUor_rexistro_xeral(rs.getString(uor_rex_xeral));

                resultado.add(dto);
            }

            return resultado;
        } catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            return resultado;
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        }
    }

     
     
      public Vector getListaUORsPermisoUsuarioNuevo(UsuarioValueObject usuario,Connection con, AdaptadorSQLBD abd) throws TechnicalException {
        Vector resultado = new Vector();

        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            sql = "SELECT "+ uor_cod + ",null as PADRE," + uor_nom + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_alta + "," +
                    abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_baja + "," +
                    uor_estado + "," + uor_cod_vis + " ," + uor_email + "," + uor_no_vis + "," + uor_cod_accede + "," + uor_rex_xeral +
                    " FROM A_UOR " +
                    "WHERE UOR_OCULTA = 'N' " +
                    "AND UOR_COD IN (SELECT UOU_UOR FROM " +
                                      GlobalNames.ESQUEMA_GENERICO +  "A_UOU  " +
                                      "WHERE UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod() + ") " + 
                    " and (uor_pad is null or uor_pad not in (SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU  WHERE UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod()+"))" + 
                    "AND (UOR_TIP IS NULL OR UOR_TIP=0)";
                   
            
            sql = sql+ " UNION SELECT "+ uor_cod + "," + uor_pad + " as PADRE," + uor_nom + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_alta + "," +
                    abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_baja + "," +
                    uor_estado + "," + uor_cod_vis + " ," + uor_email + "," + uor_no_vis + "," + uor_cod_accede + "," + uor_rex_xeral +
                    " FROM A_UOR " +
                    "WHERE UOR_OCULTA = 'N' " +
                    "AND UOR_COD IN (SELECT UOU_UOR FROM " +
                                      GlobalNames.ESQUEMA_GENERICO +  "A_UOU  " +
                                      "WHERE UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod() + ") " + 
                    " and (uor_pad  in (SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU  WHERE UOU_USU =" + usuario.getIdUsuario()  + " AND UOU_ORG =" + usuario.getOrgCod() + " AND UOU_ENT =" + usuario.getEntCod()+"))" + 
                    " AND (UOR_TIP IS NULL OR UOR_TIP=0)";
                   
            sql = sql+ " ORDER BY "+ uor_cod_vis;


            m_Log.info("getListaUORsPermisoUsuario: " + sql);
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            // rellenar DTOs y meterlos en el ArrayList del resultado
            while(rs.next()) {
                UORDTO dto = new UORDTO();
                dto.setUor_cod(rs.getString(uor_cod));
                dto.setUor_cod_vis(rs.getString(uor_cod_vis));
                dto.setUor_cod_accede(rs.getString(uor_cod_accede));
                dto.setUor_estado(rs.getString(uor_estado));
                dto.setUor_fecha_alta(rs.getString(uor_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(uor_fecha_baja));
                dto.setUor_nom(rs.getString(uor_nom));
                dto.setUor_pad(rs.getString("PADRE"));
                dto.setUor_tipo(rs.getString(uor_tipo));
                dto.setUor_email(rs.getString(uor_email));
                dto.setUor_no_registro(rs.getString(uor_no_vis).charAt(0));
                dto.setUor_rexistro_xeral(rs.getString(uor_rex_xeral));

                resultado.add(dto);
            }

            return resultado;
        } catch (Exception e){
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            return resultado;
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        }
    }

     
     
      public Vector getListaUOROrdenPorDesc(Connection conexion,String[] params) {
        Vector resultado = new Vector();

        AdaptadorSQLBD abd = null;        
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            abd = new AdaptadorSQLBD(params);
            
            sql = "SELECT "+ uor_cod + "," + uor_pad + "," + uor_nom + "," + uor_tipo + "," +
                    abd.convertir(uor_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_alta + "," +
                    abd.convertir(uor_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + uor_fecha_baja + "," +
                    uor_estado + "," + uor_cod_vis + "," + uor_email + "," + uor_no_vis + "," + uor_cod_accede + ",OFICINA_REGISTRO " + 
                    " FROM A_UOR WHERE UOR_OCULTA = 'N' ORDER BY " + uor_nom;


            m_Log.info("getListaUORs: " + sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);

            // rellenar DTOs y meterlos en el ArrayList del resultado
            while(rs.next()) {
                UORDTO dto = new UORDTO();
                dto.setUor_cod(rs.getString(uor_cod));
                dto.setUor_cod_vis(rs.getString(uor_cod_vis));
                dto.setUor_cod_accede(rs.getString(uor_cod_accede));
                dto.setUor_estado(rs.getString(uor_estado));
                dto.setUor_fecha_alta(rs.getString(uor_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(uor_fecha_baja));
                dto.setUor_nom(rs.getString(uor_nom));
                dto.setUor_pad(rs.getString(uor_pad));
                dto.setUor_tipo(rs.getString(uor_tipo));
                dto.setUor_email(rs.getString(uor_email));
                dto.setUor_no_registro(rs.getString(uor_no_vis).charAt(0));
                dto.setOficinaRegistro(false);
                int oficinaRegistro = rs.getInt("OFICINA_REGISTRO");
                if(oficinaRegistro==1)
                    dto.setOficinaRegistro(true);

                resultado.add(dto);
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
                
            } catch (Exception e) {m_Log.error(e);}
        }
        return resultado;
    }
     
   /**
     * Comprueba si hay anotaciones de registro que tengan como oficina de registro a una determinada UOR
     * @param conexion Conexion a la BD (Connection)
     * @param codigo Codigo de la UOR  
     * @return int -1 si no tiene anotaciones, en caso contrario, se devuelve el número de anotaciones que tiene a la uor como oficina de registro
     */
    public int tieneAnotacionesRegistroConOficinaRegistro(Connection conexion, int codigo)
    {    
        Statement st = null;
        ResultSet rs = null;
        int num = -1;
        
        try{
            String sql = "SELECT COUNT(*) AS NUM FROM R_RES WHERE RES_OFI=" + codigo;
            m_Log.debug(sql);
            m_Log.debug("param 1: " + codigo);
            
            st = conexion.createStatement();
            
            rs = st.executeQuery(sql);            
            while(rs.next()){
                num = rs.getInt("NUM");
            }
                        
        }catch(SQLException e){            
            e.printStackTrace();
            num =1; // En caso de error no se deja borrar la UOR
            
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return num;
   } 

    
    
   /**
     * Comprueba si los permisos que tiene un determinado usuario sobre la unidad de registro y sobre la oficina de registro son los correctos
     * @param codUorOficinaRegistro: Código de la oficina de registro
     * @param codUorRegistro;: Código de la unidad de tipo registro
     * @param codUsuario: Código del usuario
     * @param codOrganizacion: Código de la organización
     * @param codEntidad: Código de la entidad
     * @param conexion: Conexión a la BBDD
     * @return int:
     *        0 --> El usuario tiene permiso sobre la unidad de registro codUorRegistro y sobre una única oficina de registro hija que es codUorOficinaRegistro     
     *       -1 --> El usuario tiene permiso sobre más de una oficina de registro hija de la unidad de registro codUorRegistro.
     *       -2 --> El usuario no tiene permiso sobre ninguna oficina de registro hija de la unidad  de registro "codUorRegistro"
     
    public int comprobarPermisosUsuarioOficinaRegistro(int codUorOficinaRegistro,int codUorRegistro,int codUsuario,int codOrganizacion,int codEntidad,Connection conexion) throws SQLException
    {    
        Statement st = null;
        ResultSet rs = null;                
        int salida = -1;
        
        try{
            String sql = "SELECT UOR_COD " + 
                            "FROM A_UOR " +
                            "WHERE UOR_PAD=" + codUorRegistro + " AND OFICINA_REGISTRO=1 " + 
                            "AND UOR_COD IN (SELECT UOU_UOR FROM " +
                                GlobalNames.ESQUEMA_GENERICO +  "A_UOU  " +
                                "WHERE UOU_USU =" + codUsuario  + " AND UOU_ORG =" + codOrganizacion + " AND UOU_ENT =" + codEntidad + ") ";                
                                
            st = conexion.createStatement();
            rs = st.executeQuery(sql);                
            ArrayList<Integer> uors = new ArrayList<Integer>();
            
            while(rs.next()){
                uors.add(rs.getInt("UOR_COD"));
            }

            st.close();
            rs.close();

            if(uors.size()==1 && uors.contains(new Integer(codUorOficinaRegistro))){
                // El usuario tiene permiso sobre una única oficina de registro hija de la unidad de registro "codUorRegistro" y ésta 
                // la que se pasa por parámetro
                salida = 0;
            }else
            if(uors.size()>1){
                // El usuario tiene permiso sobre + de una oficina de registro hija de la unidad de registro "codUorRegistro" => ERROR
                salida  =-1;
            }else
            if(uors.size()==0)    {
                // El usuario no tiene permiso sobre ninguna oficina de registro hija de la unidad de registro "codUorRegistro"
                salida = -2;
                
            }                
                        
        }catch(SQLException e){            
            e.printStackTrace();            
            throw e;            
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return salida;
   } 
  */
    
    
   /**
     * Comprueba si los permisos que tiene un determinado usuario sobre la unidad de registro y sobre la oficina de registro son los correctos
     * @param codUorOficinaRegistro: Código de la oficina de registro
     * @param codUorRegistro;: Código de la unidad de tipo registro
     * @param codUsuario: Código del usuario
     * @param codOrganizacion: Código de la organización
     * @param codEntidad: Código de la entidad
     * @param conexion: Conexión a la BBDD
     * @return int:
     *        0 --> El usuario tiene permiso sobre la unidad de registro codUorRegistro y sobre una única oficina de registro hija que es codUorOficinaRegistro     
     *       -1 --> El usuario tiene permiso sobre más de una oficina de registro hija de la unidad de registro codUorRegistro.
     *       -2 --> El usuario no tiene permiso sobre ninguna oficina de registro hija de la unidad  de registro "codUorRegistro"
     */
    public int comprobarPermisosUsuarioOficinaRegistro(int codUorOficinaRegistro,int codUorRegistro,
            int codUsuario,int codOrganizacion,int codEntidad,Connection conexion, String jndi) 
            throws SQLException {    
        Statement st = null;
        ResultSet rs = null;                
        int salida = -1;
        
        try{
            String sql = "SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO +  "A_UOU,A_UOR " +
                         "WHERE UOU_USU =" + codUsuario  + " AND UOU_ORG =" + codOrganizacion + " AND UOU_ENT =" + codEntidad + " AND UOU_UOR=UOR_COD AND OFICINA_REGISTRO=1";                
            st = conexion.createStatement();
            rs = st.executeQuery(sql);                
            ArrayList<Integer> uors = new ArrayList<Integer>();
            
            while(rs.next()){
                uors.add(rs.getInt("UOU_UOR"));
            }

            st.close();
            rs.close();
            
            int contador = 0;
            for(int i=0;i<uors.size();i++){                
                UORDTO uor = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(jndi,String.valueOf(uors.get(i)));
                
                if(UsuarioDAO.getInstance().tieneUnidadAncestroTipoRegistroAsignado(uor,codUorRegistro,jndi))
                    contador++;                                
            }//for
            

            //if(uors.size()==1 && uors.contains(new Integer(codUorOficinaRegistro))){
            if(contador==1){                
                // El usuario tiene permiso sobre una única oficina de registro hija de la unidad de registro "codUorRegistro" y ésta 
                // la que se pasa por parámetro
                salida = 0;
            }else
            //if(uors.size()>1){
            if(contador>1){
                // El usuario tiene permiso sobre + de una oficina de registro hija de la unidad de registro "codUorRegistro" => ERROR
                salida  =-1;
            }else
            //if(uors.size()==0)    {
            if(contador==0){
                // El usuario no tiene permiso sobre ninguna oficina de registro hija de la unidad de registro "codUorRegistro"
                salida = -2;                
            }                
                        
        }catch(SQLException e){            
            e.printStackTrace();            
            throw e;            
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return salida;
   } 
    
     public ArrayList<UORDTO> getListaOficinasRegistro(String jndi) throws TechnicalException {
        
        ArrayList<UORDTO> resultado = new ArrayList<UORDTO>();
        
        SortedMap <ArrayList<String>,UORDTO> unidadesOrg = (SortedMap <ArrayList<String>,UORDTO>) CacheDatosFactoria.getImplUnidadesOrganicas().getDatosBD(jndi);
        if (unidadesOrg!=null) {
            for(Map.Entry<ArrayList<String>,UORDTO> entry : unidadesOrg.entrySet()) {
                UORDTO unidad = entry.getValue();
                if (!"S".equals(unidad.getUorOculta()) && unidad.isOficinaRegistro())
                    resultado.add(unidad);
            }
        } 
        return resultado;      
    }
     
    public  Pair<SortedMap,SortedMap> cargaCacheUnidadesOrganicas(){ 

        SortedMap unidadesOrganicasPorJndiCodVis = Collections.synchronizedSortedMap(new TreeMap<String,SortedMap>());
        SortedMap unidadesOrganicasPorJndiCodUor = Collections.synchronizedSortedMap(new TreeMap<String,SortedMap>());
        
        Comparator comparadorCodVisible = new Comparator<ArrayList<String>>() {
            public int compare (ArrayList<String> listaA, ArrayList<String> listaB) {
                if (!StringUtils.isNumericSpace(listaA.get(0)) && StringUtils.isNumericSpace(listaB.get(0)))
                    return 1;
                else if (StringUtils.isNumericSpace(listaA.get(0)) && !StringUtils.isNumericSpace(listaB.get(0)))
                    return -1;
                else {
                    String codVisibleCompA = String.format("%1$" + 8 + "s", listaA.get(0).trim());
                    String codVisibleCompB = String.format("%1$" + 8 + "s", listaB.get(0).trim());

                    if (codVisibleCompA.compareTo(codVisibleCompB) > 0)
                        return 1;
                    else if (codVisibleCompA.compareTo(codVisibleCompB) < 0)
                        return -1;
                    else if (listaA.get(1).compareTo(listaB.get(1)) > 0)
                        return 1;
                    else if (listaA.get(1).compareTo(listaB.get(1)) < 0)
                        return -1;
                    else if (listaA.get(2).compareTo(listaB.get(2)) > 0)
                        return 1;
                    else if (listaA.get(2).compareTo(listaB.get(2)) < 0)
                        return -1;
                    else
                        return 0;
                }
            }
        };

        Comparator comparadorCodUor = new Comparator<String>() {
            public int compare (String stringA, String stringB) {
                if (Integer.parseInt(stringA) > Integer.parseInt(stringB))
                    return 1;
                else if (Integer.parseInt(stringA) < Integer.parseInt(stringB))
                    return -1;
                else
                    return 0;
            }
        };
        
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        SortedMap<ArrayList<String>,ParametrosBDVO> listaParametrosBD = (SortedMap<ArrayList<String>,ParametrosBDVO>)CacheDatosFactoria.getImplParametrosBD().getDatos();

        ArrayList<String> listaBasesDatosDiferentes = new ArrayList<String>();
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        String gestorD = m_ConfigTechnical.getString("CON.gestor");
        
        try {
            if (listaParametrosBD!=null && !listaParametrosBD.isEmpty()) {
                for(Map.Entry<ArrayList<String>,ParametrosBDVO> entry : listaParametrosBD.entrySet()) {
                    ParametrosBDVO parametrosBD = entry.getValue();

                    if (!listaBasesDatosDiferentes.contains(parametrosBD.getJndi())) {
                        listaBasesDatosDiferentes.add(parametrosBD.getJndi());

                        SortedMap unidadesOrganicasJndiCodVis = Collections.synchronizedSortedMap(new TreeMap<ArrayList<String>,UORDTO>(comparadorCodVisible));
                        SortedMap unidadesOrganicasJndiCodUor = Collections.synchronizedSortedMap(new TreeMap<ArrayList<String>,UORDTO>(comparadorCodUor));

                        String params[] = {parametrosBD.getGestor() == null || "".equals(parametrosBD.getGestor().trim())?gestorD:parametrosBD.getGestor(),
                            parametrosBD.getDriver(),parametrosBD.getUrl(),parametrosBD.getUsuario(),
                            parametrosBD.getPassword(),parametrosBD.getFichlog(),parametrosBD.getJndi()};

                        oad = new AdaptadorSQLBD(params);
                        con = oad.getConnection();

                        String sql = "SELECT UOR_COD,UOR_PAD,UOR_NOM,UOR_TIP," +
                        oad.convertir("UOR_FECHA_ALTA",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                        " AS UOR_FECHA_ALTA," +
                        oad.convertir("UOR_FECHA_BAJA",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                        " AS UOR_FECHA_BAJA,UOR_ESTADO,UOR_COD_VIS,UOR_EMAIL,UOR_NO_VIS,UOR_COD_ACCEDE," + 
                        "UOR_REX_GENERAL,OFICINA_REGISTRO,UOR_OCULTA " +
                        " FROM A_UOR ORDER BY UOR_COD";

                        if(m_Log.isDebugEnabled()) m_Log.debug("CacheUnidadesOrganicas+++++, sql: " + sql);
                        stmt = con.prepareStatement(sql);
                        rs = stmt.executeQuery();
                        while (rs.next()) {
                            UORDTO uorDTO = new UORDTO();
                            uorDTO.setUor_cod(rs.getString("UOR_COD"));
                            uorDTO.setUor_cod_vis(rs.getString("UOR_COD_VIS"));
                            uorDTO.setUor_cod_accede(rs.getString("UOR_COD_ACCEDE"));
                            uorDTO.setUor_estado(rs.getString("UOR_ESTADO"));
                            uorDTO.setUor_fecha_alta(rs.getString("UOR_FECHA_ALTA"));
                            uorDTO.setUor_fecha_baja(rs.getString("UOR_FECHA_BAJA"));
                            uorDTO.setUor_nom(rs.getString("UOR_NOM"));
                            uorDTO.setUor_pad(rs.getString("UOR_PAD"));
                            uorDTO.setUor_tipo(rs.getString("UOR_TIP"));
                            uorDTO.setUor_email(rs.getString("UOR_EMAIL"));
                            uorDTO.setUor_no_registro(rs.getString("UOR_NO_VIS").charAt(0));
                            uorDTO.setUor_rexistro_xeral(rs.getString("UOR_REX_GENERAL"));
                            uorDTO.setUorOculta(rs.getString("UOR_OCULTA"));

                            uorDTO.setOficinaRegistro(false);
                            int oficinaRegistro = rs.getInt("OFICINA_REGISTRO");
                            if(oficinaRegistro==1)
                                uorDTO.setOficinaRegistro(true);

                            ArrayList <String> clave = new ArrayList<String>(3); 
                            clave.add(uorDTO.getUor_cod_vis());
                            clave.add(uorDTO.getUor_nom());
                            clave.add(uorDTO.getUor_estado());

                            unidadesOrganicasJndiCodVis.put(clave,uorDTO);
                            unidadesOrganicasJndiCodUor.put(uorDTO.getUor_cod(),uorDTO);
                        }
                        unidadesOrganicasPorJndiCodVis.put(parametrosBD.getJndi(), unidadesOrganicasJndiCodVis);
                        unidadesOrganicasPorJndiCodUor.put(parametrosBD.getJndi(), unidadesOrganicasJndiCodUor);
                        
                        SigpGeneralOperations.closeResultSet(rs);
                        SigpGeneralOperations.closeStatement(stmt);
                        SigpGeneralOperations.devolverConexion(oad, con);
                    }
                }
            }

        } catch (Exception e) {
            m_Log.error(e.getMessage());
        } finally {
            try {
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(stmt);
                SigpGeneralOperations.devolverConexion(oad, con);
            } catch (Exception e) {
            m_Log.error(e.getMessage());
            }
        }
        
        return new Pair(unidadesOrganicasPorJndiCodVis,unidadesOrganicasPorJndiCodUor);
    }
   
    /**
     * Recupera la descripción de una unidad organizativa buscando por su código
     * interno de uor
     *
     * @param codigo: Código interno de la uor
     * @param con: Conexión a la BBDD
     * @return Nombre
     */
    public String getDescripcionUOR(int codigo, Connection con) {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String nombre = null;
        try {

            sql = "SELECT UOR_NOM"
                    + " FROM A_UOR WHERE UOR_COD=" + codigo;

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("getNombreUOR: " + sql);
            }

            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                nombre = rs.getString("UOR_NOM");
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error(e.getMessage());
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
                m_Log.error(e);
            }
        }
        return nombre;
    }
    
     /**
    * Comprueba si una unidad orgánica permite digitalizar documentos
    *  @param codUorRegistro: Código de la unidad de tipo registro
    *  @param params: parametros de conexión a base de datos
    */ 
    public boolean comprobarUorPermiteDigitalizacion(int codUnidadRegistro, Connection con) throws SQLException{
        ResultSet rs = null;
        Statement st = null;
        boolean permiteDigitalizacion = false;
        int result=0;
        
        String sql = "";

	try {


            sql = "SELECT UOR_DIGITALIZACION FROM A_UOR WHERE UOR_COD = " + codUnidadRegistro;

            if (m_Log.isDebugEnabled()) m_Log.debug("query: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            if (rs.next()) {
                result=rs.getInt("UOR_DIGITALIZACION");
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            if(result==1){
                permiteDigitalizacion = true;
            } else {
                permiteDigitalizacion = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error al recuperar nombre del usuario: " + e.getMessage());
        } finally {
             try  {
                if(rs!=null) rs.close();
                if(st!=null) st.close();
            } catch (Exception e){
                m_Log.error("Error al liberar recursos de BBDD");
            }
            return permiteDigitalizacion;
        }
        
    }
     
     

}
