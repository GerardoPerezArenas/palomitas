package es.altia.agora.business.administracion.mantenimiento.persistence.manual;


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
import java.sql.SQLException;
import java.util.Vector;


public class CargosDAO  {
    private static CargosDAO instance = null;
    protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log = LogFactory.getLog(CargosDAO.class.getName());
    // ctes de techserver.properties
    protected static String car_cod;
    protected static String car_pad;
    protected static String car_nom;
    protected static String car_fecha_alta;
    protected static String car_fecha_baja;
    protected static String car_estado;
    protected static String car_cod_vis;
    protected static String res_uod;



    protected CargosDAO() {
        super();
        // Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

        // ctes de techserver.properties
        car_cod = m_ConfigTechnical.getString("SQL.A_CAR.codigo");
        car_pad = m_ConfigTechnical.getString("SQL.A_CAR.padre");
        car_nom = m_ConfigTechnical.getString("SQL.A_CAR.nombre");
        car_fecha_alta = m_ConfigTechnical.getString("SQL.A_CAR.fechaAlta");
        car_fecha_baja = m_ConfigTechnical.getString("SQL.A_CAR.fechaBaja");
        car_estado = m_ConfigTechnical.getString("SQL.A_CAR.estado");
        car_cod_vis = m_ConfigTechnical.getString("SQL.A_CAR.codigoVisible");
        res_uod = m_ConfigTechnical.getString("SQL.R_RES.unidOrigDest");
    }

    // singleton
    public static CargosDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread)
            // Las invocaciones de este metodo
            synchronized (CargosDAO.class) {
                if (instance == null) {
                    instance = new CargosDAO();
                }
            }
        }
        return instance;
    }



    /**
     * Obtiene todos los registros del Unidades Organizativas en la BD
     * @param params Parámetros de conexión
     * @return Lista de DTOs
     */
    public Vector getListaUORs(String[] params) {
        Vector resultado = new Vector();

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            sql = "SELECT "+ car_cod + "," + car_pad + "," + car_nom + "," +
                    abd.convertir(car_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + car_fecha_alta + "," +
                    abd.convertir(car_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + car_fecha_baja + "," +
                    car_estado + "," + car_cod_vis + " FROM A_CAR ORDER BY " + abd.funcionCadena(
                        AdaptadorSQLBD.FUNCIONCADENA_LPAD, new String [] {car_cod_vis, "6"});


            m_Log.debug("getListaCARs: " + sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);

            // rellenar DTOs y meterlos en el ArrayList del resultado
            while(rs.next()) {
                UORDTO dto = new UORDTO();
                dto.setUor_cod(rs.getString(car_cod));
                dto.setUor_cod_vis(rs.getString(car_cod_vis));
                dto.setUor_estado(rs.getString(car_estado));
                dto.setUor_fecha_alta(rs.getString(car_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(car_fecha_baja));
                dto.setUor_nom(rs.getString(car_nom));
                dto.setUor_pad(rs.getString(car_pad));
                resultado.add(dto);
            }
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
        }finally{
            commitTransaction(abd,conexion);
            try {
                if(rs != null)
                    rs.close();
                if(stmt != null)
                    stmt.close();
            } catch (SQLException e) {m_Log.error(e);}
        }
        return resultado;
    }

    /**
     * Obtiene todos los registros del Unidades Organizativas en la BD
     * @param params Parámetros de conexión
     * @return Lista de DTOs
     */
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
            abd.inicioTransaccion(conexion);
            sql = "SELECT "+ car_cod + "," + car_pad + "," + car_nom + "," +
                    abd.convertir(car_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + car_fecha_alta + "," +
                    abd.convertir(car_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + car_fecha_baja + "," +
                    car_estado + "," + car_cod_vis + " FROM A_CAR ORDER BY " + car_nom;


            m_Log.debug("getListaCARs: " + sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);

            // rellenar DTOs y meterlos en el ArrayList del resultado
            while(rs.next()) {
                UORDTO dto = new UORDTO();
                dto.setUor_cod(rs.getString(car_cod));
                dto.setUor_cod_vis(rs.getString(car_cod_vis));
                dto.setUor_estado(rs.getString(car_estado));
                dto.setUor_fecha_alta(rs.getString(car_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(car_fecha_baja));
                dto.setUor_nom(rs.getString(car_nom));
                dto.setUor_pad(rs.getString(car_pad));
                resultado.add(dto);
            }
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
        }finally{
            commitTransaction(abd,conexion);
            try {
                if(rs != null)
                    rs.close();
                if(stmt != null)
                    stmt.close();
            } catch (SQLException e) {m_Log.error(e);}
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
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            sql = "SELECT "+ car_cod + "," + car_pad + "," + car_nom + "," +
                    abd.convertir(car_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + car_fecha_alta + "," +
                    abd.convertir(car_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + car_fecha_baja + "," +
                     car_estado + "," + car_cod_vis + " FROM A_CAR WHERE " + car_cod + "=" +
                    codigo + " ORDER BY " + abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_LPAD, new String [] {car_cod_vis, "6"});

            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                UORDTO dto = new UORDTO();
                dto.setUor_cod(rs.getString(car_cod));
                dto.setUor_cod_vis(rs.getString(car_cod_vis));
                dto.setUor_estado(rs.getString(car_estado));
                dto.setUor_fecha_alta(rs.getString(car_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(car_fecha_baja));
                dto.setUor_nom(rs.getString(car_nom));
                dto.setUor_pad(rs.getString(car_pad));
                resultado.add(dto);
            }
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
        }finally{
            commitTransaction(abd,conexion);
            try {
                if(rs != null)
                    rs.close();
                if(stmt != null)
                    stmt.close();
            } catch (SQLException e) {m_Log.error(e);}
        }

        return resultado;
    }

    /**
     * Obtiene el cargo no dado de baja con el codigo vivible dado
     * @param codigoVisible Código visible del cargo
     * @param params Parámetros de conexión
     * @return UORDTO, null si no existe o esta de baja
     */

    //(Se usa en la aplicación "catalogo Formularios")

    public UORDTO getCargoPorCodigoVisible(String codigoVisible,String[] params){
        UORDTO dto = null;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            sql = "SELECT "+ car_cod + "," + car_pad + "," + car_nom + "," +
                    abd.convertir(car_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") + " AS " + car_fecha_alta +
                    "," + abd.convertir(car_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") + " AS " + car_fecha_baja +
                    "," + car_estado + "," + car_cod_vis + " FROM A_CAR WHERE " + car_fecha_baja +
                    " is null AND " + car_cod_vis + "='" + codigoVisible + "'";

            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                dto = new UORDTO();
                dto.setUor_cod(rs.getString(car_cod));
                dto.setUor_cod_vis(rs.getString(car_cod_vis));
                dto.setUor_estado(rs.getString(car_estado));
                dto.setUor_fecha_alta(rs.getString(car_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(car_fecha_baja));
                dto.setUor_nom(rs.getString(car_nom));
                dto.setUor_pad(rs.getString(car_pad));
            }
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
        }finally{
            commitTransaction(abd,conexion);
            try {
                if(rs != null)
                    rs.close();
                if(stmt != null)
                    stmt.close();
            } catch (SQLException e) {m_Log.error(e);}
        }

        return dto;
    }

    /**
     * Obtiene todos los registros del Unidades Organizativas en la BD con el código dado
     * @param noVisible El código es visible ('0') o no ('1') en el registro
     * @param params Parámetros de conexión
     * @return Lista de DTOs
     */
    public Vector getListaUORsPorNoVisRegistro(char noVisible,String[] params){
        Vector resultado = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            sql = "SELECT "+ car_cod + "," + car_pad + "," + car_nom + "," +
                    abd.convertir(car_fecha_alta,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + car_fecha_alta + "," +
                    abd.convertir(car_fecha_baja,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY") +
                    " AS " + car_fecha_baja + "," +
                    car_estado + "," + car_cod_vis + " FROM A_CAR ORDER BY lpad(" + car_cod_vis + ",6)";

            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                UORDTO dto = new UORDTO();
                dto.setUor_cod(rs.getString(car_cod));
                dto.setUor_cod_vis(rs.getString(car_cod_vis));
                dto.setUor_estado(rs.getString(car_estado));
                dto.setUor_fecha_alta(rs.getString(car_fecha_alta));
                dto.setUor_fecha_baja(rs.getString(car_fecha_baja));
                dto.setUor_nom(rs.getString(car_nom));
                dto.setUor_pad(rs.getString(car_pad));
                resultado.add(dto);
            }
        }catch (Exception e){
            rollBackTransaction(abd,conexion,e);
        }finally{
            commitTransaction(abd,conexion);
            try {
                if(rs != null)
                    rs.close();
                if(stmt != null)
                    stmt.close();
            } catch (SQLException e) {m_Log.error(e);}
        }

        return resultado;
    }

    /**
     * Elimina de la BD las Unidades Organizativas con el código dado
     * @param codigo Código de unidad
     * @param params Parámetros de conexión
     * @return Resultado de la consulta: >1 = OK, -1 = existen registros, otros = ERROR
     */
    public int eliminarUORPorCodigo(int codigo, String[] params){
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

            // verificar q no hay tramites asociados al código de cargo
            sql = "SELECT COUNT(*) FROM E_TRA WHERE TRA_CAR = " + codigo;
            if(m_Log.isDebugEnabled())
                m_Log.debug("Método CargosDAO.eliminarUORPorCodigo (primera consulta): " + sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            int numero_registros = 0;
            if(rs.next()) {
                numero_registros = rs.getInt(1);
            }

            rs.close();
            stmt.close();
            // si hay registros, salir
            if(numero_registros > 0) {
                return -1;
            }


            // si no hay registros asociados, continuamos con el borrado
            stmt = conexion.createStatement();
            sql = "DELETE FROM A_CAR WHERE " + car_cod + "=" + codigo;
            if(m_Log.isDebugEnabled())
                m_Log.debug("Método CargosDAO.eliminarUORPorCodigo: " + sql);
            resultado = stmt.executeUpdate(sql);
        } catch(Exception e) {
            rollBackTransaction(abd,conexion,e);
        } finally {
            try {
                commitTransaction(abd,conexion);
                if(stmt != null)
                    stmt.close();
            } catch(SQLException e) {
                m_Log.error(e);
            }
        }
        return resultado;
    }

    /**
     * Chequea que una Unidad Organizativa existe en la BD
     * @param codigo Código del Cargo
     * @param params Parámetros de conexión
     * @return Si existe o no
     */
    private boolean existeUOR(int codigo, String[] params) {
        boolean existe = false;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        String sql = "";
        ResultSet rs = null;

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();

            sql = "SELECT "+ car_cod +" FROM A_CAR WHERE " + car_cod + "=" + codigo;
            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);

            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                if (rs.getString(car_cod)!= null)
                    existe = true;
            }
        }

        catch(BDException e) {
            m_Log.error("existeCAR()", e);
        }
        catch (SQLException e) {
            m_Log.error("existeCAR()", e);
        }
        finally {
            try {
                if(rs != null)
                    rs.close();
                if(stmt != null)
                    stmt.close();
            } catch(SQLException e) {m_Log.error(e);}
        }

        return existe;
    }

    /**
     * Modifica un cargo presente en la BD.
     * @param dto DTO del tipo Cargo
     * @param params Parámetros de conexión
     * @return Número de registros afectados o -1 si hubo algún problema
     */
    public int modificarUOR(UORDTO dto, String[] params) {
        // al menos el código debe estar definido
        if((dto.getUor_cod() == null)||(dto.getUor_cod_vis()== null)) {
            return -1;
        }

        int resultado = -1;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        String sql = "";
        ResultSet rs = null;

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            sql = "update A_CAR set ";

            if(dto.getUor_cod_vis() != null) {
                sql += car_cod_vis + "='" + dto.getUor_cod_vis() + "'";
            }
            if ((dto.getUor_pad() != null) && (dto.getUor_pad() != "")) {
                sql += ", " + car_pad + "='" + dto.getUor_pad() + "'";
            }
            if(dto.getUor_estado() != null) {
                sql += ", " + car_estado + "='" + dto.getUor_estado() + "'";
            }
            if(dto.getUor_fecha_alta() != null) {
                sql += ", " + car_fecha_alta + "='" + dto.getUor_fecha_alta() + "'";
            }
            if(dto.getUor_fecha_baja() != null) {
                sql += ", " + car_fecha_baja + "='" + dto.getUor_fecha_baja() + "'";
            }
            if(dto.getUor_nom() != null) {
                sql += ", " + car_nom + "='" + dto.getUor_nom() + "'";
            }

            sql += " where " + car_cod + " = " + dto.getUor_cod();



            if(m_Log.isDebugEnabled())
                m_Log.debug(sql);

            stmt = conexion.createStatement();
            resultado = stmt.executeUpdate(sql);       
        }

        catch(Exception e) {
            rollBackTransaction(abd,conexion,e);
            m_Log.error(e);
        }
        finally {
            commitTransaction(abd,conexion);
            try {
                if(rs != null)
                    rs.close();
                if(stmt != null)
                    stmt.close();
            } catch (SQLException e) {m_Log.error(e);}
        }

        return resultado;
    }

    /**
     * Inserta un cargo en la BD con los datos del dto
     * @param dto DTO del tipo Cargo
     * @param params Parámetros de conexión
     * @return Row count del INSERT o -1 si hay algún problema
     */
    public int altaUOR(UORDTO dto, String[] params){
        int resultado = -1;

        // comprobar campos obligatorios
        if((dto.getUor_cod_vis()== null)||(dto.getUor_estado()==null)||
                (dto.getUor_fecha_alta() == null)||(dto.getUor_nom() == null)) {
            m_Log.error("Algún campo necesario para el alta de CARGO es nulo");
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
            sql = "SELECT "+abd.funcionMatematica(AdaptadorSQLBD.FUNCIONMATEMATICA_MAX,new String[]{car_cod})+ " FROM A_CAR";
            if(m_Log.isDebugEnabled())
                    m_Log.debug(sql);
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                codigo_obtenido = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            // queremos usar el siguiente número disponible como código
            codigo_obtenido++;

            // si no hemos obtenido un código ha habido algún problema
            if(codigo_obtenido == -1) {
                resultado = -1;
            }
            // realizar la inserción en BD
            else {
                sql = "INSERT INTO A_CAR ( CAR_COD, CAR_PAD, CAR_NOM, CAR_COD_VIS, " +
                        "CAR_FECHA_ALTA, CAR_FECHA_BAJA, CAR_ESTADO ) VALUES (";

                // código
                sql += codigo_obtenido + ",";
                // código del padre
                if((dto.getUor_pad() != null)&&(!dto.getUor_pad().equals(""))) {
                    sql += "'" + dto.getUor_pad() + "',";
                }
                else {
                    sql += "NULL,";
                }
                // nombre
                sql += "'" + dto.getUor_nom() + "',";
                // cod visible
                sql += "'" + dto.getUor_cod_vis() + "',";
                // fecha alta
                if((dto.getUor_fecha_alta() != null)&&(!dto.getUor_fecha_alta().equals(""))) {
                    sql += "'" + dto.getUor_fecha_alta() + "',";
                }
                else {
                    sql += "NULL,";
                }
                // fecha baja
                if((dto.getUor_fecha_baja() != null)&&(!dto.getUor_fecha_baja().equals(""))) {
                    sql += "'" + dto.getUor_fecha_baja() + "',";
                }
                else {
                    sql += "NULL,";
                }
                // estado
                sql += "'" + dto.getUor_estado() + "'";
                sql += ")";

                if(m_Log.isDebugEnabled())
                    m_Log.debug(sql);

                stmt = conexion.createStatement();
                resultado = stmt.executeUpdate(sql);
            }

        }

        catch(Exception e) {
            rollBackTransaction(abd,conexion,e);
            m_Log.error(e);
        }
        finally {
            commitTransaction(abd,conexion);
            try {
                if(rs != null) {
                    rs.close();
                }
                if(stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                m_Log.error(e);
            }
        }
    return resultado;
    }
    
    /**
     * Determina si exsiten usuarios en el catálogo de formularios con el
     * cargo que se va a a eliminar/modificar. Si existe alguno, ese cargo no 
     * se puede borrar.
     * */
    public int usuariosCatalogoAfectados (String[] params, int codCargo, String estado,String codVisible){
    	
    	PreparedStatement ps=null;
    	ResultSet rs=null;
    	int resultado=0;
    	Connection con = null;
    	AdaptadorSQLBD abd = new AdaptadorSQLBD (params);
    	String sql="SELECT COUNT(*) AS CONT FROM USUARIO_TEMP WHERE PER_COD IN" +
    			"(SELECT CAR_COD_VIS FROM A_CAR WHERE CAR_COD=" + String.valueOf(codCargo) +")";
    	m_Log.debug("usuariosCatalogoAfectados --> " + sql);
    	
    	try {
    		con = abd.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			rs.next();
			resultado = rs.getInt("CONT");
			
			if (resultado!=0){
				//Si existe algun usuario, debemos comprobar que el campo car_cod_vis no varia,
				//y que no se da de baja, que es lo que afecta al catalogo.			
				sql="SELECT CAR_COD_VIS FROM A_CAR WHERE CAR_COD=" + String.valueOf(codCargo);
				m_Log.debug(sql);
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				rs.next();
				if (codVisible.equals(rs.getString("CAR_COD_VIS"))) resultado=0;			
				if (estado.equals("B")) return -1;
			}
			
			
		} catch (SQLException e) {
			m_Log.debug("Excepción SQL en usuariosCatalogoAfectados.");
			e.printStackTrace();
		} catch (BDException e) {
			m_Log.debug("Excepción BD en usuariosCatalogoAfectados.");
			e.printStackTrace();
		}
        finally {
            try {
                if(rs != null)
                    rs.close();
                if(ps != null)
                    ps.close();
            } catch(SQLException e) {m_Log.error(e);}
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


}