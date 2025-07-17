package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import java.sql.*;
import java.util.HashMap;
import java.util.Vector;
 
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.persistence.InteresadosManager;
import es.altia.agora.business.sge.persistence.OperacionesExpedienteManager;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.manual.TercerosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.util.commons.DateOperations;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExpedientesDAO {


  protected static Config m_ConfigTechnical;
  protected static Log m_Log =
          LogFactory.getLog(ExpedientesDAO.class.getName());
    private static ResourceBundle properties = ResourceBundle.getBundle("Terceros");  

  protected static String exp_mun;
  protected static String exp_eje;
  protected static String exp_num;
  protected static String exp_pro;
  protected static String exp_fei;
  protected static String exp_fef;
  protected static String exp_est;
  protected static String exp_usuario;
  protected static String exp_uor;
  protected static String exp_asu;

  //Interesados expediente
  protected static String ext_mun;
  protected static String ext_pro;
  protected static String ext_eje;
  protected static String ext_num;
  protected static String ext_ter;
  protected static String ext_nvr;
  protected static String ext_dot;
  protected static String ext_rol;


  //Interesados registro
  protected static String r_ext_coduor;
  protected static String r_ext_tipo;
  protected static String r_ext_ano;
  protected static String r_ext_numero;
  protected static String r_ext_dep;
  protected static String r_ext_ter;
  protected static String r_ext_nvr;
  protected static String r_ext_dot;
  protected static String r_ext_rol;
  
  // Roles procedimiento
  protected static String rol_mun;
  protected static String rol_pro;
  protected static String rol_cod;
  protected static String rol_des;
  protected static String rol_pde;
  
  // Roles asiento
  protected static String  rol_gruporoles;
  protected static String  rol_codigo;
  protected static String  rol_nombre;
  protected static String  rol_defecto; 
  protected static String grupoPorDefecto;

  private static ExpedientesDAO instance = null;

  protected ExpedientesDAO() {
    super();
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");

    exp_mun = m_ConfigTechnical.getString("SQL.E_EXP.codMunicipio");
    exp_eje = m_ConfigTechnical.getString("SQL.E_EXP.ano");
    exp_num = m_ConfigTechnical.getString("SQL.E_EXP.numero");
    exp_pro = m_ConfigTechnical.getString("SQL.E_EXP.codProcedimiento");
    exp_fei = m_ConfigTechnical.getString("SQL.E_EXP.fechaInicio");
    exp_fef = m_ConfigTechnical.getString("SQL.E_EXP.fechaFin");
    exp_est = m_ConfigTechnical.getString("SQL.E_EXP.estado");
    exp_usuario = m_ConfigTechnical.getString( "SQL.E_EXP.usuario");
    exp_uor = m_ConfigTechnical.getString( "SQL.E_EXP.uor");
    exp_asu = m_ConfigTechnical.getString( "SQL.E_EXP.asunto");

    //Interesados expediente
    ext_mun = m_ConfigTechnical.getString( "SQL.E_EXT.codMunicipio");
    ext_pro = m_ConfigTechnical.getString( "SQL.E_EXT.codProcedimiento");
    ext_eje = m_ConfigTechnical.getString( "SQL.E_EXT.ano");
    ext_num = m_ConfigTechnical.getString( "SQL.E_EXT.numero");
    ext_ter = m_ConfigTechnical.getString( "SQL.E_EXT.codTercero");
    ext_nvr = m_ConfigTechnical.getString( "SQL.E_EXT.verTercero");
    ext_dot = m_ConfigTechnical.getString( "SQL.E_EXT.codDomicilio");
    ext_rol = m_ConfigTechnical.getString( "SQL.E_EXT.rolTercero");

  
    // Interesados registro
    r_ext_coduor = m_ConfigTechnical.getString ("SQL.R_EXT.codUor");
    r_ext_tipo   = m_ConfigTechnical.getString ("SQL.R_EXT.tipo");
    r_ext_ano    = m_ConfigTechnical.getString ("SQL.R_EXT.ano");
    r_ext_numero = m_ConfigTechnical.getString ("SQL.R_EXT.numero");
    r_ext_dep    = m_ConfigTechnical.getString ("SQL.R_EXT.coddepartamento");
    r_ext_ter    = m_ConfigTechnical.getString ("SQL.R_EXT.codTercero");
    r_ext_nvr    = m_ConfigTechnical.getString ("SQL.R_EXT.verTercero");
    r_ext_dot    = m_ConfigTechnical.getString ("SQL.R_EXT.codDomicilio");
    r_ext_rol    = m_ConfigTechnical.getString ("SQL.R_EXT.rolTercero");
    
    // Roles procedimiento
    rol_mun = m_ConfigTechnical.getString("SQL.E_ROL.codMunicipio");
    rol_pro = m_ConfigTechnical.getString("SQL.E_ROL.codProcedimiento");
    rol_cod = m_ConfigTechnical.getString("SQL.E_ROL.codRol");
    rol_des = m_ConfigTechnical.getString("SQL.E_ROL.descripcion");
    rol_pde = m_ConfigTechnical.getString("SQL.E_ROL.porDefecto");
    
    // Roles asiento
    rol_gruporoles  = m_ConfigTechnical.getString("SQL.R_ROL.grupo");
    rol_codigo      = m_ConfigTechnical.getString("SQL.R_ROL.codigo");
    rol_nombre      = m_ConfigTechnical.getString("SQL.R_ROL.descripcion");
    rol_defecto     = m_ConfigTechnical.getString("SQL.R_ROL.activo"); 
    grupoPorDefecto = m_ConfigTechnical.getString("SQL.R_ROL.grupoPorDefecto");
  }

  public static ExpedientesDAO getInstance() {
    //si no hay ninguna instancia de esta clase tenemos que crear una.
    if (instance == null) {
      // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
      synchronized(ExpedientesDAO.class){
        if (instance == null)
          instance = new ExpedientesDAO();
      }
    }
    return instance;
  }


    public int iniciarExpediente(GeneralValueObject gVO, String[] params) throws TechnicalException {
        if(m_Log.isDebugEnabled()) m_Log.debug("iniciarExpediente() : BEGIN");
        AdaptadorSQLBD oad = null;
        Connection con = null;
        int resultado=0;
        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);

            UsuarioValueObject uVO = new UsuarioValueObject();
            uVO.setOrgCod(Integer.parseInt((String) gVO.getAtributo("codMunicipio")));
            uVO.setParamsCon(params);
            if(gVO.getAtributo("numero") == null || "".equals(gVO.getAtributo("numero"))){
                TramitacionValueObject tVO = new TramitacionValueObject();
                tVO.setCodProcedimiento((String) gVO.getAtributo("codProcedimiento"));
                TramitacionDAO.getInstance().getNuevoExpediente(uVO, tVO, con);
                String numeroExpediente = tVO.getNumero();
                StringTokenizer tokenizer = new StringTokenizer(numeroExpediente, "/");
                gVO.setAtributo("ejercicio", tokenizer.nextToken());
                gVO.setAtributo("numero", numeroExpediente);
            }//if(gVO.getAtributo("numero") == null || "".equals(gVO.getAtributo("numero")))
            gVO.setAtributo("params", params);
            resultado = insertarExpediente(oad, con, gVO);
            
            if(resultado >=0){
                resultado = TramitesExpedienteDAO.getInstance().iniciarTramiteInicio(oad,con, gVO);
            }//if(resultado >=0)
        }catch (Exception e){
            SigpGeneralOperations.rollBack(oad, con);
            resultado = -1;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion en iniciarExpediente " + e.getMessage());
        }finally{
            if (resultado >= 0)
                SigpGeneralOperations.commit(oad, con);
            else
                SigpGeneralOperations.rollBack(oad, con);
            SigpGeneralOperations.devolverConexion(oad, con);      
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("iniciarExpediente() : END");

        if(resultado >=0){
            TramitesExpedienteDAO.getInstance().ejecutarOperacionesAlIniciarTramiteInicio(gVO, params);
        }//if(resultado >=0)
        
        return resultado;
    }//iniciarExpediente
  
    /**
     * Inserta un nuevo expediente y copia los interesados del asiento al procedimiento, conservando sus
     * roles si el procedimiento asignado al asiento es el mismo que el del nuevo expediente.
     * No abre transacción ni captura excepciones porque recibe la conexión desde iniciarExpedienteAsiento
     * de FichaExpedienteDAO, donde ya se controla la transacción y las excepciones.
     */
    public int insertarExpediente(AdaptadorSQL oad, Connection con, GeneralValueObject gVO) 
        throws SQLException, TechnicalException {

        if (m_Log.isDebugEnabled()) m_Log.debug("ExpedientesDAO->insertarExpediente"); 
        
        Statement st = con.createStatement();;
        ResultSet rs;
        String sql;
        int resultado;
        int resultado1;
        int resultadoFinal = 0;

        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numero = (String) gVO.getAtributo("numero");
        String usuario = (String) gVO.getAtributo("usuario");
        String nomUsuario = (String) gVO.getAtributo("nomUsuario");
        String uor = (String) gVO.getAtributo("codUOR");
        String codTercero = (String) gVO.getAtributo("codTercero");
        String codDomicilio = (String) gVO.getAtributo("codDomicilio");
        String version = (String) gVO.getAtributo("version");
        String codRol = (String) gVO.getAtributo("codRol");  // Es el valor del rol principal del procedimiento
        if (codRol == null || "".equals(codRol)) codRol = "1";
        String asunto = (String) gVO.getAtributo("asunto");
        String observaciones = (String) gVO.getAtributo("observaciones");
        String[] params = (String[]) gVO.getAtributo("params");
                
        String procAsiento = (String) gVO.getAtributo("procedimientoAsiento");
        String munProcAsiento = (String) gVO.getAtributo("munProcedimiento");
        String tipoAsiento = (String) gVO.getAtributo("tipoAsiento");
        String anoAsiento = (String) gVO.getAtributo("ejercicioAsiento");
        String numAsiento = (String) gVO.getAtributo("numeroAsiento");
        String uorAsiento = (String) gVO.getAtributo("codUnidadRegistro");
        String depAsiento = (String) gVO.getAtributo("codDepartamento");
        String desdeAsiento = (String) gVO.getAtributo("desdeAsiento");

        sql = "INSERT INTO E_EXP (EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_FEI, EXP_FEF, EXP_EST, EXP_USU, EXP_UOR, " +
                "EXP_ASU, EXP_OBS) VALUES (" + codMunicipio + ", '" + codProcedimiento + "', " + ejercicio + ", '" +
                numero + "'" + ", " + oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", null, 0, " +
                usuario + ", " + uor + ",";

        // Se indica cual es el asunto
        if (asunto != null && !"".equals(asunto) && !"null".equals(asunto))
            sql += "'" + TransformacionAtributoSelect.replace(asunto, "'", "''") + "',";
        else sql += "null,";

        // Se indican cuales son las observaciones
        if (observaciones != null && !"".equals(observaciones) && !"null".equals(observaciones))
            sql += "'" + TransformacionAtributoSelect.replace(observaciones, "'", "''") + "')";
        else sql += "null)";

        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
        resultado = st.executeUpdate(sql);
        SigpGeneralOperations.closeStatement(st);
        
        // Inicio de expediente desde asiento
        if (desdeAsiento != null && !desdeAsiento.equals("")) {
            if (m_Log.isDebugEnabled()) m_Log.debug("Se inicia desde asiento.");

            // Comprobamos si el procedimiento del asiento y el del nuevo expediente coinciden
            boolean mismoProcedimiento = codProcedimiento.equals(procAsiento);
            if (mismoProcedimiento) {
                if (m_Log.isDebugEnabled()) m_Log.debug("El procedimiento es el mismo que el del asiento"); 
            } else {
                if (m_Log.isDebugEnabled()) m_Log.debug("El procedimiento es distinto que el del asiento"); 
            }                     
            
            // Obtenemos la lista de terceros asociados al asiento
            sql = "SELECT EXT_TER, EXT_NVR, EXT_DOT, EXT_ROL, COUNT(RES_TER) MOSTRAR "+
                 "FROM R_EXT LEFT JOIN R_RES " +
                 "ON(EXT_UOR = RES_UOR AND EXT_DEP = RES_DEP AND EXT_TIP = RES_TIP AND EXT_EJE = RES_EJE AND EXT_NUM = RES_NUM AND EXT_TER = RES_TER)" + 
                 " WHERE " + r_ext_coduor + "=" + uorAsiento +
                 " AND " + r_ext_dep + "=" + depAsiento +
                 " AND " + r_ext_tipo + "='" + tipoAsiento + "'" + 
                 " AND " + r_ext_ano + "=" + anoAsiento +
                 " AND " + r_ext_numero + "=" + numAsiento + " " +
                 "GROUP BY EXT_TER, EXT_NVR, EXT_DOT, EXT_ROL";
            
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);       
            st = con.createStatement();
            rs = st.executeQuery(sql);                       
            
            // Se prepara un statement para insertar los terceros en el expediente
            sql = "INSERT INTO E_EXT ( " + ext_mun + "," + ext_eje + "," + ext_num + "," + ext_ter + "," +
                                           ext_nvr + "," + ext_dot + "," + ext_rol + "," + ext_pro + ", MOSTRAR) " +
                  "VALUES ( " + codMunicipio + "," + ejercicio + ",'" + numero + "',?,?,?,?,'" + codProcedimiento + "', ?)";
            
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);       
            PreparedStatement ps = con.prepareStatement(sql);
            
            int j;
            // Rol por defecto del procedimiento
            int rolPrincipalProc = Integer.parseInt(codRol);
            // Si el procedimiento iniciado no es el del asiento obtenemos
            // un mapeo de los roles del asiento los roles del procedimiento en el
            // que se casaran los roles del mismo nombre.
            HashMap<String,String> mapeoRoles = new HashMap<String,String>();
            if (!mismoProcedimiento) {
                /*
                mapeoRoles =
                   obtenerMapeoRoles(codProcedimiento, codMunicipio, codRol,
                            procAsiento, munProcAsiento, (String[]) gVO.getAtributo("params"));
                */
                obtenerMapeoRoles(codProcedimiento, codMunicipio, codRol,
                            procAsiento, codMunicipio, (String[]) gVO.getAtributo("params"));
            }
            while (rs.next()) {
                j = 1;
                ps.setInt(j++, rs.getInt(r_ext_ter));
                ps.setInt(j++, rs.getInt(r_ext_nvr));            
                ps.setInt(j++, rs.getInt(r_ext_dot));
                if (mismoProcedimiento) 
                    // Se conservan los roles
                    ps.setInt(j++, rs.getInt(r_ext_rol));  
                else { 
                    // Se usa el mapeo
                    String cod = mapeoRoles.get(rs.getString(r_ext_rol));
                    if (cod == null) cod = codRol;
                    ps.setInt(j++, Integer.parseInt(cod)); // Se usa el mapeo
                }
                ps.setBoolean(j++, rs.getBoolean("MOSTRAR"));
                ps.executeUpdate();
            }
    
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            resultado1=1;
        
         // Inicio de expediente desde servicio web o sin asiento (codigo original)
        } else {              
            if (m_Log.isDebugEnabled()) m_Log.debug("No se inicia desde asiento.");
            
            if (codTercero != null && !"".equals(codTercero)) {
                st = con.createStatement();
                
                sql = "INSERT INTO E_EXT ( " + ext_mun + "," + ext_eje + "," + ext_num + "," + ext_ter + "," +
                        ext_nvr + "," + ext_dot + "," + ext_rol + "," + ext_pro + ", MOSTRAR) VALUES ( " +
                        codMunicipio + "," + ejercicio + ",'" + numero + "'," + codTercero + "," +
                        version + "," + codDomicilio + "," + codRol + ",'" + codProcedimiento + "', 1)";
                
                if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                resultado1 = st.executeUpdate(sql);
                SigpGeneralOperations.closeStatement(st);
            } else {
                resultado1 = 1;
            }
        }

         try{
            //Registra el cambio en el historico de movimientos de un expediente
            TercerosValueObject tercero = new TercerosValueObject();
            tercero.setIdentificador(codTercero);
            tercero.setVersion(version);
            tercero.setIdDomicilio(codDomicilio);
            Vector terceros = TercerosDAO.getInstance().getByHistorico(tercero, con, params);
            tercero = (TercerosValueObject) terceros.elementAt(0);
            tercero.setCodRol(Integer.valueOf(codRol));
            tercero.setNotificacionElectronica("0"); 
            
            OperacionesExpedienteManager.getInstance().registrarAltaInteresado(Integer.valueOf(codMunicipio).intValue(),
                    numero, Integer.valueOf(usuario).intValue(), nomUsuario, tercero, con);  
        }catch (TechnicalException te){
            if (m_Log.isErrorEnabled()) {m_Log.error(te.getMessage());}            
        }
         
        if (resultado > 0 && resultado1 > 0) resultadoFinal = 1;                
        return resultadoFinal;
    }


  public int reabrirExpediente(AdaptadorSQL oad, Connection con, GeneralValueObject gVO)
  throws Exception  {
    gVO.setAtributo("estadoExpediente","0");    
    gVO.setAtributo("fechaFinExpediente","");    
    return actualizarExpediente(oad, con, gVO, false);
  }

    public int finalizarExpediente(AdaptadorSQL oad, Connection con, GeneralValueObject gVO) throws TechnicalException {
        gVO.setAtributo("estadoExpediente", "9");
        return actualizarExpediente(oad, con, gVO, true);
    }
     public int finalizarExpedienteNoConvencional(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO) throws TechnicalException {
         GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
         gVO.setAtributo("estadoExpediente", "1");
        return actualizarExpediente(oad, con, gVO, true);
    }


    public int finalizarExpediente(AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO)
            throws TechnicalException {

        GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
        return finalizarExpediente(oad, con, gVO);
    }

  private GeneralValueObject tramitacionExpedientesVO (TramitacionExpedientesValueObject teVO) {
    GeneralValueObject gVO = new GeneralValueObject();
    gVO.setAtributo("codMunicipio", teVO.getCodMunicipio());
    gVO.setAtributo("codProcedimiento",teVO.getCodProcedimiento());
    gVO.setAtributo("ejercicio", teVO.getEjercicio());
    gVO.setAtributo("numero", teVO.getNumero());
    gVO.setAtributo("usuario", teVO.getUsuario());
    gVO.setAtributo("codUOR", teVO.getUnidadInicio());  
    return gVO;
  }


    private int actualizarExpediente(AdaptadorSQL oad, Connection con, GeneralValueObject gVO,
                                     boolean finalizarOReabrir) throws TechnicalException {

        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String numero = (String) gVO.getAtributo("numero");
        String fechaFinExpediente = (String) gVO.getAtributo("fechaFinExpediente");
        String estado = (String) gVO.getAtributo("estadoExpediente");

        String valorFechaFin;
        if (finalizarOReabrir) valorFechaFin = oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null);
        else if ("".equals(fechaFinExpediente)){
            valorFechaFin = "null";
        } else {
            valorFechaFin = "'" + fechaFinExpediente + "'";
        }

        String sqlUpdateExp = "UPDATE E_EXP SET " + exp_fef + " = " + valorFechaFin + ", " + exp_est + " = ? WHERE " +
                exp_mun + " = ? AND " + exp_num + " = ?";
        

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sqlUpdateExp);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA FINALIZAR O REABRIR UN EXPEDIENTE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlUpdateExp);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 1 --> Fecha Fin Expediente: " + fechaFinExpediente);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 2 --> Estado Expediente: " + estado);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 3 --> Cod Municipio: " + codMunicipio);
            if (m_Log.isDebugEnabled()) m_Log.debug("Parametro 4 --> Numero Expediente: " + numero);

            int i = 1;
            ps.setInt(i++, Integer.parseInt(estado));
            ps.setInt(i++, Integer.parseInt(codMunicipio));
            ps.setString(i, numero);

            int updatedRows = ps.executeUpdate();
            if (updatedRows != 1) throw new SQLException("EL RESULTADO DEVUELTO POR LA CONSULTA A LA BASE DE DATOS" +
                    " NO ES VALIDO");

            SigpGeneralOperations.closeStatement(ps);

           

            return updatedRows;

        } catch(SQLException sqle) {
            sqle.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR EN LA CONSULTA PARA ACTUALIZAR EL ESTADO DE UN EXPEDIENTE");
            throw new TechnicalException("ERROR EN LA CONSULTA PARA ACTUALIZAR EL ESTADO DE UN EXPEDIENTE", sqle);
        }
    }
    
    /**
     * Crea un mapeo de los roles del asiento a los del procedimiento, intentando casar los
     * codigos de los roles del asiento con los del proc. cuando tienen el mismo 
     * nombre. Si hay algun rol del asiento con distinto nombre que cualquiera
     * del procedimiento se mapea al rol por defecto del procedimiento.
     * 
     * Los resultados del mapeo se devuelven en un HashMap. 
     * 
     * @param codProcedimiento codigo del procedimiento.
     * @param codMunicipio     codigo del municipio.
     * @param rolDefectoProc   codigo del rol por defecto del proc.
     * @param procAsiento      codigo del procedimiento del asiento.
     * @param munProcAsiento   codigo del municipio del asiento.
     * @param params           parametros de conexion a BD.
     */
    private HashMap<String,String> obtenerMapeoRoles(String codProcedimiento, 
            String codMunicipio, String rolDefectoProc, String procAsiento, 
            String munProcAsiento, String[] params) {

        // Obtenemos roles del procedimiento y del asiento
        Vector<GeneralValueObject> rolesProc = 
                obtenerRoles(codProcedimiento, codMunicipio, params);
        Vector<GeneralValueObject> rolesAsiento = 
                obtenerRoles(procAsiento, munProcAsiento, params);
        
        m_Log.debug("ROLES DEL ASIENTO:");
        for (GeneralValueObject rol : rolesAsiento) {
            m_Log.debug(rol.getAtributo("codRol") + "-" + rol.getAtributo("descRol") + "-" + rol.getAtributo("porDefecto"));
        }
        m_Log.debug("ROLES DEL PROCEDIMIENTO:");
        for (GeneralValueObject rol : rolesProc) {
            m_Log.debug(rol.getAtributo("codRol") + "-" + rol.getAtributo("descRol") + "-" + rol.getAtributo("porDefecto"));
        }
        m_Log.debug("Rol por defecto del procedimiento: " + rolDefectoProc);
        
        // Hacemos el mapeo
        HashMap<String,String> mapeo = new HashMap<String,String>();
        // Para cada rol del asiento
        for (GeneralValueObject rolAsiento : rolesAsiento) {
            String descAsiento = (String) rolAsiento.getAtributo("descRol");
            String codAsiento = (String) rolAsiento.getAtributo("codRol");
            
            // Buscamos un rol del procedimiento del mismo nombre
            boolean encontrado = false;
            for (GeneralValueObject rolProc : rolesProc) {
                String descProc = (String) rolProc.getAtributo("descRol");
                if (descAsiento.equals(descProc)) {
                    mapeo.put(codAsiento, (String) rolProc.getAtributo("codRol"));
                    encontrado = true;
                    break;
                }
            }
            // Si no se ha encontrado un rol del mismo nombre, mapeo al rol por defecto
            if (!encontrado) {
                 mapeo.put(codAsiento, rolDefectoProc);
            }
        }
        
        m_Log.debug("MAPEO DE ROLES : " + mapeo);
        
        return mapeo;
    }
    
    public int getUorExpediente(int codOrganizacion, int ejercicio, String numExpediente, Connection con) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        int codUor = -1;
        
        try {
            query = "SELECT EXP_UOR FROM E_EXP WHERE EXP_MUN = ? AND EXP_EJE = ? AND EXP_NUM = ?";
            m_Log.debug("Query para recuperar la UOR del expediente: " + query);
            m_Log.debug(String.format("Parámetros de la query: %d - %d - %s", codOrganizacion, ejercicio, numExpediente));
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, codOrganizacion);
            ps.setInt(contbd++, ejercicio);
            ps.setString(contbd++, numExpediente);
            rs = ps.executeQuery();

            if(rs.next()){
                codUor = rs.getInt("EXP_UOR");                    
            }
        } catch (SQLException sqlex) {
            m_Log.error("Ha ocurrido un error al obtener la UOR del expediente " + numExpediente, sqlex);
            throw  sqlex;
        } finally {
            try{
                if (rs != null){
                    rs.close();
                }
                if (ps != null){
                    ps.close();
                }
            } catch (SQLException e){
                m_Log.error("Ha ocurrido un error al liberar recursos de base de datos.", e);        
                throw  e;
            }
        }
        
        return codUor;
    }
    
    /**
     * Obtiene la lista de roles correspondientes al código de procedimiento
     * y código de municipio pasados. Si el codigo de procedimiento es nulo
     * o vacío devuelve los roles por defecto.     
     */
    private Vector<GeneralValueObject> obtenerRoles(String proc, String mun, String[] params) {

        Vector<GeneralValueObject> roles = new Vector<GeneralValueObject>();
        if (proc != null && !proc.equals("") && !proc.equals("null")) {
            GeneralValueObject procGVO = new GeneralValueObject();
            procGVO.setAtributo("codMunicipio", mun);
            procGVO.setAtributo("codProcedimiento", proc);
            roles = InteresadosManager.getInstance().getListaRoles(procGVO, params);
        } else {
            roles = InteresadosManager.getInstance().getListaRolesRegistro(params);
        }        
        return roles;
    }

    
    /**
     * Dado un expediente, comprueba si los documentos de expediente asociados que necesitan firmas
     * tienen dicho circuito finalizado. De no estarlo no se podrá finalizar el expediente.
     */
    public boolean firmasPendientesExpediente (AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO) throws TechnicalException{
        
        boolean posible=true;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector<String> documentosConFirmas = new Vector <String>();
        
        String sql = "SELECT distinct(id_doc_presentado) FROM E_DOCS_PRESENTADOS RIGHT JOIN E_DOCS_FIRMAS ON "
                + "(PRESENTADO_COD=ID_DOC_PRESENTADO) WHERE PRESENTADO_MUN=? AND PRESENTADO_PRO=?"
                + " AND PRESENTADO_NUM=?";
        try {
            ps = con.prepareStatement(sql);
            int i=1;
            m_Log.debug(sql);
            
            ps.setInt(i++, Integer.parseInt(teVO.getCodOrganizacion()));
            ps.setString(i++, teVO.getCodProcedimiento());
            ps.setString(i++, teVO.getNumeroExpediente());
            rs = ps.executeQuery();
            
            while (rs.next()){
                documentosConFirmas.add(rs.getString("id_doc_presentado"));                
            }
            
            //Para cada documento con firmas se mirará su estado. En cuanto uno no esté firmado, dará error.
            
            sql = "SELECT doc_firma_estado FROM E_DOCS_PRESENTADOS RIGHT JOIN E_DOCS_FIRMAS ON "
                + "(PRESENTADO_COD=ID_DOC_PRESENTADO) WHERE PRESENTADO_MUN=? AND PRESENTADO_PRO=?"
                + " AND PRESENTADO_NUM=? and id_doc_presentado=?";
            
            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            ArrayList<String> estados = new ArrayList<String>();
            int j=0;
            
              while (posible && j < documentosConFirmas.size()) {
                estados = new ArrayList<String>();
                i = 1;
                ps.setInt(i++, Integer.parseInt(teVO.getCodOrganizacion()));
                ps.setString(i++, teVO.getCodProcedimiento());
                ps.setString(i++, teVO.getNumeroExpediente());
                ps.setInt(i++, Integer.valueOf(documentosConFirmas.elementAt(j)));
                rs = ps.executeQuery();
                j++;

                while (rs.next()) {
                    estados.add(rs.getString("DOC_FIRMA_ESTADO"));
                }

                posible = !"O".equals(FichaExpedienteDAO.getInstance().devolverEstadoFirma(estados));
                m_Log.debug("documento firmado? " + posible);
            }




        } catch (SQLException sqle) {
            sqle.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR EN LA CONSULTA PARA CONSULTAR LAS FIRMAS DE DOCS DE EXPEDIENTE");
            throw new TechnicalException("ERROR EN LA CONSULTA DE FIRMAS DE DOCUMENTOSDE UN EXPEDIENTE", sqle);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }

        return posible;
    }
    
    public int guardarSitucionFirmasDocumentos (AdaptadorSQL oad, Connection con, TramitacionExpedientesValueObject teVO,
            String filtroEstado, String nuevoEstado) throws TechnicalException{
     
        int resultado = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector<String> firmasPendientes = new Vector <String>();
        
        String sql = "SELECT id_doc_firma FROM E_DOCS_PRESENTADOS RIGHT JOIN E_DOCS_FIRMAS ON "
                + "(PRESENTADO_COD=ID_DOC_PRESENTADO) WHERE PRESENTADO_MUN=? AND PRESENTADO_PRO=?"
                + " AND PRESENTADO_NUM=? AND DOC_FIRMA_ESTADO='" + filtroEstado + "'";
        try {
            ps = con.prepareStatement(sql);
            int i=1;
            m_Log.debug(sql);
            
            ps.setInt(i++, Integer.parseInt(teVO.getCodOrganizacion()));
            ps.setString(i++, teVO.getCodProcedimiento());
            ps.setString(i++, teVO.getNumero());
            rs = ps.executeQuery();
            
            while (rs.next()){
                firmasPendientes.add(rs.getString("id_doc_firma"));                   
            }
            m_Log.debug("firmas pendientes = " + firmasPendientes);
            
            //Para cada documento con firmas se mirará su estado. En cuanto uno no esté firmado, se actualizará
            // al estado de firma no requerida.
            
            sql = "UPDATE E_DOCS_FIRMAS SET DOC_FIRMA_ESTADO='" + nuevoEstado + "' WHERE ID_DOC_FIRMA=? ";
            
            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            int j=0;
            
            while (resultado>=0 && j<firmasPendientes.size()){
                ps.setInt(1, Integer.valueOf(firmasPendientes.elementAt(j)));
                resultado = ps.executeUpdate();
                j++;
            }
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR EN LA CONSULTA PARA CONSULTAR LAS FIRMAS DE DOCS DE EXPEDIENTE");
            throw new TechnicalException("ERROR EN LA CONSULTA DE FIRMAS DE DOCUMENTOSDE UN EXPEDIENTE", sqle);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
        return resultado;
        
    }
    
    /**
     * Actualizar la fecha de vencimiento de la primera alarma del expediente
     * 
     * @param alarma
     * @param con
     * @return
     * @throws Exception 
     */
    public void actualizarFechaPrimeraAlarma(int codMunicipio, int ejercicio, String numExp, Calendar fecAlarma, 
            Connection con) throws SQLException {
        
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE E_EXP SET EXP_FPA = ? " +
                "WHERE EXP_MUN = ? AND EXP_EJE = ? AND EXP_NUM = ?";
            
           if (m_Log.isErrorEnabled()) m_Log.debug(" (ExpedientesDAO.actualizarFechaPrimeraAlarma(): " + sql);
            ps = con.prepareStatement(sql);
            
            int i = 1;
            ps.setTimestamp(i++,DateOperations.toTimestamp(fecAlarma));
            ps.setInt(i++,codMunicipio);
            ps.setInt(i++,ejercicio);
            ps.setString(i++,numExp);
            
            ps.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Se ha producido un error en el metodo ExpedientesDAO.actualizarFechaPrimeraAlarma(): " + ex.getMessage() , ex);
            throw new SQLException(ex);
        } finally {
            if(ps != null)
                ps.close();
        }
    }
    
    
}