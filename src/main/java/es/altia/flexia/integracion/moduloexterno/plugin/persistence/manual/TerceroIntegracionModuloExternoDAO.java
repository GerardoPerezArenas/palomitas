package es.altia.flexia.integracion.moduloexterno.plugin.persistence.manual;

import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.TransformacionAtributoSelect;import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DomicilioInteresadoModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.TerceroModuloIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Vector;
import org.apache.log4j.Logger;


public class TerceroIntegracionModuloExternoDAO {
    private static TerceroIntegracionModuloExternoDAO instance = null;
    private Logger log = Logger.getLogger(TerceroIntegracionModuloExternoDAO.class);
  

   public static TerceroIntegracionModuloExternoDAO getInstance(){
        if(instance==null)
            instance = new TerceroIntegracionModuloExternoDAO();

        return instance;
    }
    
    protected TerceroIntegracionModuloExternoDAO() {}
    
    /**
     * 
     * @param codOrg
     * @param documento
     * @param nombre
     * @param apelido1
     * @param apelido2
     * @param con
     * @return
     * @throws TechnicalException
     * @throws SQLException 
     */
   public ArrayList<TerceroModuloIntegracionVO> getTerceros(String codOrg,int tipoDocumento, String documento, String nombre, String apelido1, String apelido2, Connection con ) throws TechnicalException, SQLException{
      
       log.debug("getTerceros. BEGIN");
        String sql= "";
        ArrayList<TerceroModuloIntegracionVO> resultado=new ArrayList<TerceroModuloIntegracionVO>();
        AdaptadorSQLBD bd = null;
        ResultSet rs=null;
        Statement st=null;
        
        
        try{
         
            String clausulaAnd="";
            //Construimos a clausula AND, para filtrar
            if(tipoDocumento>0){
            
                clausulaAnd += " AND  TER_TID  = " +tipoDocumento;
            
            }
            
            
            if (documento != null){
                if (!"".equals(documento.trim())) {
                      
                         clausulaAnd += " AND  TER_DOC  = '"+ documento+"'";
                       
                }
            }
            if (nombre!= null){
                  if (!"".equals(nombre.trim())){
                    
                          clausulaAnd += " AND TER_NOM = '"+ nombre +"'"; 
                       }
                    
              }
            if (apelido1!= null ){
                    if (!"".equals(apelido1.trim())){
                        clausulaAnd += " AND TER_AP1 = '"+ apelido1 +"'"; 
                    }
                  }
            
            if (apelido2!= null){
                     if (!"".equals(apelido2.trim())){
                        clausulaAnd += " AND TER_AP2 = '"+ apelido2 +"'";
                       }
            }
            
                sql = " SELECT T_TER.* "
                    + " FROM T_TER "
                    + " WHERE TER_SIT ='A'" + clausulaAnd 
                    + " ORDER BY TER_DOC DESC, TER_NVE DESC";
              
                st = con.createStatement();
                rs = st.executeQuery(sql);
                if(log.isDebugEnabled()) log.debug("getTerceros. Consulta: " + sql);
                TerceroModuloIntegracionVO tercero = null;
                while(rs.next()){
                    tercero = new TerceroModuloIntegracionVO(
                          
                            rs.getString("TER_COD"),
                            rs.getString("TER_NVE"),
                            rs.getString("TER_TID"),
                            rs.getString("TER_DOC"),
                            rs.getString("TER_NOM"),
                            rs.getString("TER_AP1"),
                            rs.getString("TER_AP2"),
                            rs.getString("TER_NML"),
                            rs.getString("TER_TLF"), 
                            rs.getString("TER_DCE"),
                            rs.getString("TER_SIT").charAt(0),
                            rs.getString("TER_FAL"),
                            rs.getString("TER_UAL"),
                            rs.getString("TER_APL"),
                            rs.getString("TER_FBJ"),
                            rs.getString("TER_UBJ"),
                            rs.getInt("TER_DOM")
                         );
                    tercero.setDomicilios(new ArrayList<DomicilioInteresadoModuloIntegracionVO>());
                    resultado.add(tercero);
                }
                 if (rs!=null) rs.close();
                 if (st!=null) st.close();
                 //if(con!=null) con.close();
           
        }catch (Exception e){
            if(log.isDebugEnabled()) log.error(e.getMessage());
            e.printStackTrace();
        }finally{
            if (rs!=null) rs.close();
            if (st!=null) st.close();;
        
        log.debug("getTerceros. END");    
        return resultado;
    }
    
   }
    
   
   
   /**
     * Inserta un nuevo tercero en la BD. No es necesario indicar su domicilio principal.
     * @param ter
     * @param con
     * @return int Codigo del nuevo tercero insertado.
     * @throws java.sql.SQLException
     */
    public int altaTercero(TerceroModuloIntegracionVO ter, Connection con) throws SQLException {

        String sql;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        int nuevoCodigo = 1;
        if (log.isDebugEnabled()) log.debug("->TerceroIntegracionModuloExternoDAO.altaTercero");

        try {

            // Obtener codigo para el nuevo tercero
            sql = "SELECT MAX(TER_COD) AS MAXIMO FROM T_TER";

            st = con.createStatement();
            rs = st.executeQuery(sql);

            if (rs.next()) {
                nuevoCodigo = rs.getInt("MAXIMO");
                nuevoCodigo++;
            }
            rs.close();
            st.close();
            if (log.isDebugEnabled()) log.debug("Nuevo codigo de tercero: " + nuevoCodigo);

            // Insercion en T_TER
            String usuarioAlta =ter.getUsuarioAlta();
            int usuarioAltaInt=Integer.parseInt(usuarioAlta);
            if (log.isDebugEnabled()) log.debug("Usuario Alta: " + usuarioAltaInt);
            
            
            sql = "INSERT INTO T_TER( TER_COD, TER_TID, TER_DOC, TER_NOM, TER_AP1,  " +
                                    " TER_AP2,  TER_NOC, TER_NML, TER_TLF, TER_DCE, " +
                                    " TER_SIT, TER_NVE, TER_FAL, TER_UAL, TER_APL, "+
                                    "  TER_FBJ, TER_UBJ, TER_DOM) " +
                  "VALUES (  ?, ?,?, ?, ?,  " +
                           " ?, ?, ?, ?, ?," +
                           " ?, ?, ?, ?, ?, "+
                           " ?, ?, ? )";

            if (log.isDebugEnabled()) log.debug(sql);
            ps = con.prepareStatement(sql);

            int i = 1;
            
            ps.setInt(i++,nuevoCodigo); //ter_cod
            ps.setInt(i++, Integer.parseInt(ter.getTipoDocumentoTercero())); // TER_TID
            ps.setString(i++, ter.getDocumentoTercero()); // TER_DOC
            ps.setString(i++, ter.getNombreTercero());  // TER_NOM
            ps.setString(i++, ter.getApellido1Tercero());  // TER_AP1
            
            ps.setString(i++, ter.getApellido2Tercero()); // TER_AP2
            ps.setString(i++, ter.getNombreTercero() + " " + ter.getApellido1Tercero() + " " + ter.getApellido2Tercero()); // TER_NOC
            ps.setInt(i++,2); //TER_NML            
            ps.setString(i++, ter.getTelefonoTercero()); // TER_TLF
            ps.setString(i++, ter.getEmail()); // TER_DCE
            
            ps.setString(i++,"A"); // ter_sit
            ps.setInt(i++,1); // ter_nve            
            Timestamp ahora = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(i++, ahora); // TER_FAL
            ps.setInt(i++,usuarioAltaInt); // ter_ual
            ps.setInt(i++,5); //TER_APL
            
                    
            ps.setNull(i++, Types.TIMESTAMP); // TER_FBJ
            ps.setNull(i++, Types.INTEGER); // TER_UBJ
            ps.setNull(i++, Types.INTEGER); // TER_DOM          
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (ps != null) ps.close();
        }
        
        if (log.isDebugEnabled()) log.debug("->TerceroIntegracionModuloExternoDAO.altaTercero. El codigo del nuevo tercero es: "+nuevoCodigo);
        return nuevoCodigo;
    }
    
    
  /**
   * 
   * @param mostrar
   * @param codOrg
   * @param codProcedimiento
   * @param ejercicio
   * @param numExp
   * @param rol rol del tercero
   * @param codTercero codigo del tercero asociado al expediente
   * @param numVersTercero
   * @param codDireccion domicilio del tercero 
   * @param con
   * @throws TechnicalException 
   */  
 public void altaInteresado(int mostrar, String codOrg, String codProcedimiento, 
         int ejercicio, String numExp,int rol,int codTercero,int numVersTercero,int codDireccion,Connection con)
          throws TechnicalException, SQLException{

        log.debug("altaInteresado. BEGIN. ");
        String sql;
        PreparedStatement ps = null;
    
        try {
            sql = "INSERT INTO E_EXT (EXT_MUN, EXT_EJE, EXT_NUM, EXT_TER, EXT_NVR, EXT_DOT, EXT_ROL, EXT_PRO, MOSTRAR) " +
                            "VALUES (?,?,?,?,?,?,?,?,?)";
           
            
            if (log.isDebugEnabled()) log.debug("AltaInteresado.CONSULTA: " +sql);
            ps = con.prepareStatement(sql);
            int contbd = 1;
            ps.setInt(contbd++, Integer.parseInt(codOrg));
            ps.setInt(contbd++, ejercicio);
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, codTercero);
            ps.setInt(contbd++, numVersTercero);
            ps.setInt(contbd++, codDireccion);
            ps.setInt(contbd++, rol);
            ps.setString(contbd++, codProcedimiento);
            ps.setInt(contbd++, mostrar);


            ps.executeUpdate();
            ps.close();
         }catch (Exception e){
            log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            
            if (ps != null) ps.close();
        }

  }
   
  /**
   * 
   * @param mostrar
   * @param idExpedienteVO
   * @param rol
   * @param codTercero
   * @param numVersTercero
   * @param codDireccion
   * @param con
   * @throws TechnicalException 
   */  
 
 
   public boolean existeInteresado(Connection con, String documento, String numExp ) throws Exception{
        if(log.isDebugEnabled()) log.debug("TerceroIntegracionModuloExternoDAO.existeInteresado() : BEGIN ");
      
        boolean resultado=false;
        Statement st = null;
        ResultSet rs = null;
          
       try{
        
              String sql = " SELECT T_TER.* " +
                           " FROM T_TER,E_EXT " +
                           " WHERE TER_SIT='A' "+
                           " AND TER_DOC= '" + documento + "'"+ 
                           " AND TER_COD=E_EXT.EXT_TER " + 
                           " AND E_EXT.EXT_NUM = '" + numExp+ "'" ;
           
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
             
           
            if((rs!=null) && (rs.next())){
                //se hai algun resultado devolvemos true
                resultado= true;
            }else resultado= false;
       
            }catch (SQLException e) {
                log.error("Se ha producido un error recuperando el/los interesados de flexia", e);
                throw e;
            }finally{
                try{
                    if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                }catch(Exception e){
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);                
                }//try-catch
            }//try-catch-finally    
        if(log.isDebugEnabled()) log.debug("existeInteresado() : END"+ resultado);   
        return resultado;
    }
      
 
  /**
     * Inserta una nueva version en el historico de terceros con los datos y
     * domicilios nuevos. Actualiza T_TER con el nuevo numero de version y nuevo
     * domicilio principal.
     * @param ter
     * @param codTercero
     * @param domPrincipal
     * @param con
     * @return Nuevo numero de version del tercero.
     * @throws java.sql.SQLException
     */
    public int actualizarHistoricoTercero(TerceroModuloIntegracionVO ter, int codTercero,
            int domPrincipal, int usuarioAlta, int moduloAlta, Connection con) throws SQLException {

        String sql;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        int nuevaVersion = 1;

        if (log.isDebugEnabled()) log.debug("->TerceroIntegracionModuloExternoDAO.actualizarHistoricoTercero, tercero= " + codTercero +
                ", domicilio principal=" + domPrincipal);

        try {

            // Primero obtenemos el nuevo numero de version
            sql = "SELECT MAX(HTE_NVR) FROM T_HTE " +
                  "WHERE HTE_TER = " + codTercero;
            if (log.isDebugEnabled()) log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                nuevaVersion = rs.getInt(1);
                nuevaVersion++;
            }

            rs.close();
            if (log.isDebugEnabled()) log.debug("Nueva version del tercero: " + nuevaVersion);

            // Insertamos en el historico
           
            sql =
                "INSERT INTO T_HTE(HTE_TER, HTE_NVR, HTE_DOT, HTE_TID, HTE_DOC, " +
                                  "HTE_NOM, HTE_AP1, HTE_PA1, HTE_AP2, HTE_PA2, " +
                                  "HTE_NOC, HTE_NML, HTE_TLF, HTE_DCE, HTE_FOP, HTE_USU, HTE_APL) " +
                "VALUES (" + codTercero + ", " + nuevaVersion + ", " + 
                     domPrincipal + ", ?, ?, ?, ?, ?, ?, ?, ?, 2, ?, ?, ?, " +
                     usuarioAlta + ", " + moduloAlta + ")";
            if (log.isDebugEnabled()) log.debug(sql);

            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,1); // HTE_TID
            ps.setString(i++, ter.getDocumentoTercero()); // HTE_DOC
            ps.setString(i++, ter.getNombreTercero()); // HTE_NOM
            ps.setString(i++, ter.getApellido1Tercero()); // HTE_AP1
            ps.setNull(i++, Types.VARCHAR); // HTE_PA1
            ps.setString(i++, ter.getApellido2Tercero()); // HTE_AP2
            ps.setNull(i++, Types.VARCHAR); // HTE_PA2
            ps.setString(i++, ter.getNombreTercero()+ " " + ter.getApellido1Tercero() + " " + ter.getApellido2Tercero()); // HTE_NOC
            ps.setString(i++, ter.getTelefonoTercero()); // HTE_TLF
            ps.setString(i++, ter.getEmail()); // HTE_DCE
            Timestamp ahora = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(i++, ahora); // HTE_FOP

            ps.executeUpdate();
            ps.close();

            // Actualizamos numero de version y domicilio principal en T_TER
            sql = "UPDATE T_TER " +
                  "SET TER_NVE= " + nuevaVersion + ", TER_DOM= " + domPrincipal +
                  " WHERE TER_COD = " + codTercero;
            if (log.isDebugEnabled()) log.debug(sql);
            st.executeUpdate(sql);
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (ps != null) ps.close();
        }

        return nuevaVersion;
    }

    
     /**
     * Inserta la relacion entre tercero y domicilio en la tabla T_DOT, si no
     * existe ya. Si existe pero esta de baja la cambia a alta.
     * @param codTercero
     * @param codDomicilio
     * @param con
     * @throws java.sql.SQLException
     */
    public boolean altaDomicilioTercero(int codTercero, int codDomicilio, Integer usuarioAlta, Connection con)
            throws SQLException {

        String sql;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        boolean actualizacionDomicillioRealizada = true;
        if (log.isDebugEnabled()) log.debug("->TerceroIntegracionModuloExternoDAO.altaDomicilioTercero, tercero =" +
                codTercero + ", domicilio=" + codDomicilio);

        try {

            // Se comprueba si ya existe la relacion
            sql = "SELECT DOT_SIT FROM T_DOT " +
                  "WHERE DOT_DOM = " + codDomicilio + " AND DOT_TER = " + codTercero;

            if (log.isDebugEnabled()) log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);

            String situacion = null;
            if (rs.next()) {
                situacion = rs.getString(1);
            }

            rs.close();

            // Caso de que no exista la relacion
            if (situacion == null) {

                if (log.isDebugEnabled()) log.debug("No existe relacion");
               
                sql = "INSERT INTO T_DOT(DOT_DOM, DOT_TER, DOT_TOC, DOT_SIT, " +
                                        "DOT_FEC, DOT_USU, DOT_DPA) " +
                      "VALUES (?, ?, ?, 'A', ?, " + usuarioAlta + ", 0)";

                if (log.isDebugEnabled()) log.debug(sql);

                ps = con.prepareStatement(sql);
                int i = 1;
                ps.setInt(i++, codDomicilio);
                ps.setInt(i++, codTercero);
                ps.setNull(i++, Types.INTEGER); // DOT_TOC
                Timestamp ahora = new Timestamp(System.currentTimeMillis());
                ps.setTimestamp(i++, ahora); // DOT_FEC

                ps.executeUpdate();
                ps.close();

            // Caso de que la relacion exista pero este de baja
            } else if ("B".equals(situacion)) {

                if (log.isDebugEnabled()) log.debug("La relacion esta de baja");

                sql = "UPDATE T_DOT SET DOT_SIT = 'A' " +
                      "WHERE DOT_DOM = " + codDomicilio +
                      "AND DOT_TER = " + codTercero;

                if (log.isDebugEnabled()) log.debug(sql);

                st.executeUpdate(sql);

            // La relacion ya existe y esta de alta
            } else {
                if (log.isDebugEnabled()) log.debug("El domicilio ya esta relacionado con el tercero");
                actualizacionDomicillioRealizada = false;
            }

            st.close();
            return actualizacionDomicillioRealizada;

        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (ps != null) ps.close();
        }
    }

 
    
     /**
     * Busca un tercero en BD
     * @param tipoDoc tipo del Documento del tercero
     * @param documento Documento del tercero
     * @param con
     * @return Boolean (true si se encuentra al tercero, false en caso contrario)
     * @throws java.sql.SQLException
     */
    public boolean existeTercero(int codTercero,
            int tipoDoc, String documento, int numVersion, Connection con) throws SQLException {

        String sql;
        Statement st = null;
        ResultSet rs = null;
        Boolean resultado= false;
        if (log.isDebugEnabled()) log.debug("->TerceroIntegracionModuloExternoDAO.existeTercero");
        if (log.isDebugEnabled()) log.debug("->TerceroIntegracionModuloExternoDAO.CodTercero:" + codTercero);
        if (log.isDebugEnabled()) log.debug("->TerceroIntegracionModuloExternoDAO.DocumentoTercero: "+ documento );

        try {
            sql = " SELECT TER_COD " +
                  " FROM T_TER " +
                  " WHERE TER_SIT='A' " +
                  " AND TER_DOC =  '" + documento + "'"+
                  " AND TER_TID = " + tipoDoc +
                  " AND TER_NVE= " + numVersion +
                  " AND TER_COD= "+codTercero;         

            if (log.isDebugEnabled()) log.debug("TerceroIntegracionModuloExternoDAO. existeTercero.Consulta:"+ sql);
           
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            if (rs.next()) {
                resultado= true;
            } else {
                resultado= false;
            }

            rs.close();
            st.close();

        
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
        }     
        if (log.isDebugEnabled()) log.debug("TerceroIntegracionModuloExternoDAO. existeTercero. Resultado devuelto:"+ resultado);
        return resultado;
    }

    
  
}//class