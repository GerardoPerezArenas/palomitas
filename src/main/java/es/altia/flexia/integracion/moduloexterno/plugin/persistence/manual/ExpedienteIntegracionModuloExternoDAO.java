package es.altia.flexia.integracion.moduloexterno.plugin.persistence.manual;

import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.manual.TercerosDAO;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoDesplegableModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.ModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.*;
import es.altia.flexia.integracion.moduloexterno.plugin.util.UtilitiesModuloIntegracion;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.axis.encoding.Base64;

import org.apache.log4j.Logger;


public class ExpedienteIntegracionModuloExternoDAO {
    private static ExpedienteIntegracionModuloExternoDAO instance = null;
    private Logger log = Logger.getLogger(ExpedienteIntegracionModuloExternoDAO.class);

    private ExpedienteIntegracionModuloExternoDAO(){
    }
    
    public static ExpedienteIntegracionModuloExternoDAO getInstance(){
        if(instance==null)
            instance = new ExpedienteIntegracionModuloExternoDAO();

        return instance;
    }


    /**
    * Se comprueba si el código visible de la unidad de inicio es válida.
    * 
    **/

    public int esUnidadInicioValida(String procedimiento, int unidadInicio, Connection con) 
            throws TechnicalException, SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        int resultado;
        int devuelvo = -1;
        Vector<Integer> uor = new Vector<Integer>();
        String sql = "SELECT PUI_COD FROM E_PUI WHERE PUI_PRO= ?";
        try {
            log.debug(" Consulta a ejecutar en es UnidadInicioValida: " + sql);
            log.debug("Pocedimiento: " + procedimiento + " UnidadInicio: " + unidadInicio);
            ps = con.prepareStatement(sql);
            ps.setString(1, procedimiento);
            rs = ps.executeQuery();
            /*
             * SI DEVUELVE ALGO TENGO QUE COMPROBAR QUE EL COD DE LA UNIDA DE INICIO
             * QUE PASAN ES IGUAL A ALGUNO DE LOS QUE DEVUELVE
             * SI ES VACIO ENTOCES ES CORRECTO PQ SIGINIFICA QUE UNIDAD DE INCIO ES
             * CUALQUIERA
             */
            while (rs.next()) {
                resultado = rs.getInt("PUI_COD");
                log.debug("La unidad de inicio puede ser:  " + resultado);
                uor.add(resultado);
            }
            if (uor.size() > 0) {
                for (int i = 0; i < uor.size(); i++) {
                    if (uor.elementAt(i) == unidadInicio) {
                        devuelvo = unidadInicio;
                    }
                }
            } else {
                //la unidad de incio es cualquiera
                devuelvo = unidadInicio;
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(sql);
            }
            log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            if (ps!=null){ ps.close();}
            if (rs!=null) { rs.close();}
           // if( con!=null) { con.close();}
        }
        return devuelvo;
    }

   /**
    * Se comprueba si el procedimiento para el que se desea iniciar el expediente
    * es válido. Si no lo es, se cancela la operacion y se devuelve el código
    * de error correspondiente.
    * Para que un procedimiento sexa válido, entre outras cousas ten que existir, non
    * pode estar caducado, etc..
    * 
    **/
    public boolean esProcedimientoValido(String procedimiento, Connection con) throws TechnicalException, SQLException {

        log.debug("ExpedienteIntegracionModuloExternoDAO.esProcedimientoValido.Procedimiento: "+ procedimiento);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultado = null;
        boolean valido = false;
        Timestamp ahora = new Timestamp(System.currentTimeMillis());

        String sql = "SELECT PRO_COD FROM E_PRO WHERE PRO_COD = ? AND PRO_FLD <=  ? AND "
                + "(PRO_FLH IS NULL OR PRO_FLH > ?) AND PRO_FBA IS NULL AND PRO_EST=1";
        try {
            log.debug("Consulta en esProcedimientoValido: "+ sql);
            log.debug("esProcedimientoValido. Procedimiento " + procedimiento + ", FechaActual," + ahora);
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, procedimiento);
            ps.setTimestamp(i++, ahora);
            ps.setTimestamp(i++, ahora);
            rs = ps.executeQuery();
            valido = rs.next();
            log.debug("valido " + valido);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(sql);
            }
            log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        } finally {
           if (ps!=null) ps.close();
           if (rs!=null) rs.close();
          // if (con!=null) con.close();
        }
        return valido;
    }

    /**
     * Se crea el numero expediente en la tabla E_NEX para el procedimiento y ejercicio
     * del mismo
     * @param procedimiento
     * @param codOrganizacion
     * @param ejercicio
     * @param con
     * @return ExpedienteModuloIntegracionVO con los campos ejercicio, numero y codProcedimiento cubiertos
     * 
     * @throws TechnicalException 
     */
     public int generarNumeroExpediente(String procedimiento, String codOrganizacion, int ejercicio, Connection con) throws TechnicalException, SQLException {

        log.info("ExpedienteModuloIntegracionModuloExternoDAO.generarNumeroExpediente.Procedimiento: "+ ejercicio+"/"+procedimiento); 
        PreparedStatement ps = null;
        ResultSet rs = null;
        String proc;
        int eje;
        String num = "";
        int numExp = -1;
        

        String sql = "SELECT NEX_PRO,NEX_EJE,NEX_NUM FROM E_NEX WHERE NEX_PRO= ? AND NEX_EJE=? ORDER BY NEX_NUM desc";
		log.info("Se ejecuta la select para obtener el próximo número de expediente");
        //  String sql = "SELECT EXP_PRO,EXP_EJE,EXP_NUM FROM E_EXP WHERE EXP_PRO= ? ORDER BY EXP_NUM desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, procedimiento);
            ps.setInt(2, ejercicio);
            rs = ps.executeQuery();
            /*Sacamos el primero que devuelve que sera el de numero mas alto*/
            boolean insertarContador = false;
            if (rs.next()) {
                
                num = rs.getString("NEX_NUM");
                log.info("Se obtiene el número: " + num);
                numExp = Integer.parseInt(num);
                numExp = numExp + 1;
                log.debug("numExp " + numExp);

            } else {
                // Si no existe el contador de expedientes para el procedimiento, es que no hay expedientes,
                // por tanto, el contador empieza en 1
               
                numExp = 1;
                insertarContador = true;
            }

            rs.close();
            ps.close();

            if (!insertarContador) {
                String sql2 = "UPDATE E_NEX SET NEX_NUM=? where NEX_PRO=? and NEX_EJE=?";
                ps = con.prepareStatement(sql2);
                int i = 1;
                num = Integer.toString(numExp);
                ps.setString(i++, num);
                ps.setString(i++, procedimiento);
                ps.setInt(i++, ejercicio);
                ps.executeUpdate();
                ps.close();
            } else {
                String sql2 = "INSERT INTO E_NEX(NEX_PRO,NEX_EJE,NEX_NUM,NEX_MUN,NEX_UOR) "
                        + " VALUES(?,?,?,?,?)";
                ps = con.prepareStatement(sql2);
                int i = 1;
                num = Integer.toString(numExp);
                ps.setString(i++, procedimiento);
                ps.setInt(i++, Calendar.getInstance().get(Calendar.YEAR));
                ps.setString(i++, Integer.toString(numExp));
                ps.setInt(i++, Integer.parseInt(codOrganizacion));
                ps.setInt(i++, 0);
                ps.executeUpdate();
                ps.close();
            }
			log.info("Se inserta/actualiza E_NEX con el valor " + numExp);



        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(sql);
            }
            log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            if (ps!=null) { ps.close();}
            if (rs!=null) { rs.close(); }
            //if (con!=null) { con.close();}
        }
        return numExp;
    }

   /*
    *
    */  
    public void crearExpediente(ExpedienteModuloIntegracionVO expedienteVO,int codOrganizacion, Connection con, AdaptadorSQLBD oad) throws TechnicalException, SQLException {
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        String sql;

        String codProcedimiento = expedienteVO.getCodProcedimiento();
        int ejercicio = expedienteVO.getEjercicio();
        String numero = expedienteVO.getNumExpediente();
        log.debug("**crearExpediente.BEGIN: " + numero);
        int usuario = expedienteVO.getCodUsuarioIniciaExpediente();
        String uorVisible =expedienteVO.getCodigoUorVisibleInicioExpediente();
        int codUor=dameCodigoUorInterno(con,uorVisible);
        
        //necesitamos recupe
        String asunto = expedienteVO.getAsunto();
        String observaciones = (String) expedienteVO.getObservaciones();
        Timestamp ahora = new Timestamp(System.currentTimeMillis());

        sql = "INSERT INTO E_EXP (EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_FEI, EXP_FEF, EXP_EST, EXP_USU, EXP_UOR, "
                + "EXP_ASU,EXP_OBS) VALUES (?,?,?,?," + oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ",?,?,?,?,?,?)";

        try {
            // Insercion en E_EXP
            ps = con.prepareStatement(sql);

            int i = 1;
            ps.setInt(i++, codOrganizacion); // EXP_MUN
            ps.setString(i++, codProcedimiento); // EXP_PRO
            ps.setInt(i++, ejercicio);  // EXP_EJE
            ps.setString(i++, numero);  // EXP_NUM
            //ps.setTimestamp(i++, ahora); // EXP_FEI
            ps.setNull(i++, Types.TIMESTAMP); // EXP_FEF
            ps.setInt(i++, 0);  // EXP_EST
            ps.setInt(i++, usuario); // EXP_USU
            ps.setInt(i++, codUor); // EXP_UOR
            ps.setString(i++, asunto); // EXP_ASU
            ps.setString(i++, observaciones); // EXP_OBS
            ps.executeUpdate();
            log.debug("**crearExpediente.END: " + numero);
        } catch (Exception e) {
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            if (rs!=null) {rs.close();}
            if (st!=null) {st.close();}   
           // if (con!=null){ con.close();}
        }
    } 
    
    
   public int dameCodigoUorInterno(Connection con, String codVisible ){
        
        log.debug("DameCodigoUorInterno.BEGIN. CodVisible es: "+codVisible);
        
        Statement st = null;
        String sql;
        ResultSet rs = null;
        int uorCod=-9997;
        try {       
            sql = " SELECT UOR_COD "
                + " FROM A_UOR "
                + " WHERE UOR_COD_VIS= '" + codVisible + "'" ;

            
            log.debug("Consulta en dameCodigoUorInterno: "+sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next())
                uorCod = rs.getInt("UOR_COD");
                        
        } catch (Exception e) {
            e.printStackTrace();
           
        } finally {
            try {
                if (con != null) {
                    rs.close();
                    st.close();   
                   // con.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (log.isDebugEnabled()) log.error(ex.getMessage());
            }

        }  
        log.debug("DameCodigoUorInterno.END. Codigo interno es: "+uorCod);
        return uorCod; 
    
    }
    
    
    /**
    * Se comprueba si el usuario que va a iniciar el expediente
    * tiene permiso sobre la unidad de inicio del expediente
    * 
    **/
    public boolean tienePermiso(int usuario, int unidadInicio, int codOrg, Connection con) throws TechnicalException, SQLException {

        log.debug("Tiene Permiso.BEGIN.");
        log.debug("Tiene Permiso.Usuario: "+ usuario);
        log.debug("Tiene Permiso.UnidadInicio: "+ unidadInicio);
        log.debug("Tiene Permiso.CodOrganizacion: "+ codOrg);
       
        Statement st = null;
        ResultSet rs = null;
       
        boolean valido = false;
       

        String sql = "SELECT UOU_UOR FROM " 
               +  GlobalNames.ESQUEMA_GENERICO + "A_UOU  " 
               + "WHERE UOU_USU =" + usuario  + " AND UOU_ORG =" + codOrg + " AND UOU_UOR =" + unidadInicio ;
        
        log.debug("tienePermiso. SQL:"+ sql);
        
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next())
               valido=true;
                        
           } catch (Exception e) {
            e.printStackTrace();
            if (log.isDebugEnabled()) {
                log.debug(sql);
            }
            log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        } finally {
           if (st!=null) st.close();
           if (rs!=null) rs.close();
          // if (con!=null) con.close();
        }
        return valido;
    }

    
     
     /** Esta operación se utiliza a la hora
     * de dar de alta un interesado en un expediente,
     * para verificar que el expediente está ok.
     * (Existe y el estado es 0-> pendiente)
     * Comprueba que existe un expediente y está
     * en estado pediente
     * @param numExp numero del expediente
     * @param eje ejercicio del expediente
     * @param codProc procedimiento del expediente
     * @param con
     * @return Boolean (true si se encuentra al tercero, false en caso contrario)
     * @throws java.sql.SQLException
     */
    public boolean existeExpediente(String numExp, String codProc, int eje, Connection con) throws SQLException {

        String sql;
        Statement st = null;
        ResultSet rs = null;
        Boolean resultado= false;
        if (log.isDebugEnabled()) log.debug("->ExpedienteIntegracionModuloExternoDAO.existeExpediente");
        if (log.isDebugEnabled()) log.debug("->ExpedienteIntegracionModuloExternoDAO.NumExp:" + numExp);
        if (log.isDebugEnabled()) log.debug("->ExpedienteIntegracionModuloExternoDAO.codProc: "+ codProc );
        if (log.isDebugEnabled()) log.debug("->ExpedienteIntegracionModuloExternoDAO.Ejercicio: "+ eje);

        try {
            sql = " SELECT EXP_NUM " +
                  " FROM E_EXP " +
                  " WHERE EXP_NUM ='" + numExp+ "'" +
                  " AND EXP_PRO =  '" + codProc + "'"+
                  " AND EXP_EJE = " + eje +
                  " AND EXP_EST= 0 ";
                        

            if (log.isDebugEnabled()) log.debug("ExpedienteIntegracionModuloExternoDAO. existeExpediente.Consulta:"+ sql);
           
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
        if (log.isDebugEnabled()) log.debug("ExpedienteIntegracionModuloExternoDAO. existeTercero. Resultado devuelto:"+ resultado);
        return resultado;
    }
    
    
    /** Esta operación asocia un documento
     * externo a un expediente
     * @param DocumentoModuloIntegracionVO documento
     * @param con
     * @return int codigo del documento insertado o -1
     * @throws java.sql.SQLException
     */
    
      public int setDocumentoExterno(DocumentoExternoModuloIntegracionVO documentoExterno,int codOrg,
            Connection con) throws TechnicalException, SQLException {

        if (log.isDebugEnabled()) log.debug("ExpedienteIntegracionModuloExternoDAO.setDocumentoExterno.BEGIN"); 
        PreparedStatement ps=null;  
        int resultado=-1;
        try {
            String sql = "INSERT INTO E_DOC_EXT(DOC_EXT_MUN, DOC_EXT_EJE, DOC_EXT_NUM, DOC_EXT_COD, DOC_EXT_NOM,"
                    + " DOC_EXT_FAL, DOC_EXT_FIL, DOC_EXT_TIP, DOC_EXT_EXT) VALUES (?,?,?,?,?,?,?,?,?)";


            int codigoDocumento = getCodigoMaximoDocExterno(con);
            ps = con.prepareStatement(sql);
            ps.setInt(1, codOrg);
            ps.setInt(2, documentoExterno.getEjercicio());
            ps.setString(3, documentoExterno.getNumExp());
            ps.setString(4, String.valueOf(codigoDocumento));
            ps.setString(5, documentoExterno.getNombreDocumento());
            ps.setTimestamp(6, DateOperations.toSQLTimestamp(Calendar.getInstance()));
            if (documentoExterno.getContenido() != null ) {
                byte[] contenido =documentoExterno.getContenido();
                java.io.InputStream st = new java.io.ByteArrayInputStream(contenido);
                ps.setBinaryStream(7, st, contenido.length);
            } else {
                // Si no hay fichero se guarda un null en el campo doc_ext_fil. Esto sólo se debe dar cuando se ha configurado FLEXIA para que almacene los documentos adjuntos en un gestor documental,
                // de modo que se guarda en base de datos una referencia al documento pero su contenido estará en el gestor.
                ps.setNull(7, java.sql.Types.BINARY);
            }

            if (log.isDebugEnabled()) {
                log.debug("setDocumentoExterno. Consulta: "+sql);
            }

            ps.setString(8, documentoExterno.getMimetipe());
            ps.setString(9, documentoExterno.getExtensionDocumento());
            int rowsInserted = ps.executeUpdate();
            resultado=codigoDocumento;
            log.debug("Nº de documentos externos insertados: " + rowsInserted);


        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Se ha producido un error insertando el documento presentado " + e.getMessage());
            
        } finally {
            if(ps!=null) ps.close();
        }
       if (log.isDebugEnabled()) log.debug("ExpedienteIntegracionModuloExternoDAO.setDocumentoExterno.END. Resultado: "+ resultado);  
       return resultado; 
    }

  /**
   * Método auxiliar para el método setDocumentoExterno
   * Nos da el código que toca para insertar el documento.
   * @param con
   * @return
   * @throws TechnicalException
   * @throws SQLException 
   */    
      
 private int getCodigoMaximoDocExterno(Connection con) throws TechnicalException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        int salida = 1;
       log.debug("getCodigoMaximoDocExterno.BEGIN");
        try {
            String sql = "SELECT MAX(DOC_EXT_COD) AS NUM FROM E_DOC_EXT";
            if (log.isDebugEnabled()) {
                log.debug("getCodigoMaximoDocExterno:"+ sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                salida = rs.getInt("NUM") + 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Se ha producido un error recuperando el código máximo del documento externo " + e.getMessage());
            throw new TechnicalException("Error en el get del codigo maximo");
        } finally {
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }
        log.debug("getCodigoMaximoDocExterno.END. El resultado devuelto es: "+ salida);
        return salida;
    }
      /**
     * Recupera el contenido de un determinado documento externo que pertenece a un determinado expediente
     * @param DocumentoExternoModuloIntegracionVO: Objeto de la clase DocumentoExternoModuloIntegracionVO
     * @param codOrg codigo Organizacion
     * @param con: Conexion a la BBDD
     * @return byte[] si se ha podido recuperar el contenido del documento
     * @throws es.altia.common.exception.TechnicalException
     */
    public byte[] getDocumentoExterno(DocumentoExternoModuloIntegracionVO documento, int codOrg ,Connection con) throws TechnicalException, SQLException {
        Statement st = null;
        ResultSet rs = null;
        byte[] contenido = null;
        try {
            String sql = "SELECT DOC_EXT_FIL FROM E_DOC_EXT "
                    + "WHERE DOC_EXT_COD=" + documento.getCodDocumento() + " AND DOC_EXT_NUM='" + documento.getNumExp() + "' AND "
                    + "DOC_EXT_MUN=" + codOrg + " AND DOC_EXT_EJE=" + documento.getEjercicio();

            log.debug("getDocumentoExterno:Consulta:"+sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                contenido = rs.getBytes("DOC_EXT_FIL");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Se ha producido un error recuperando el documento externo " + e.getMessage());
            throw new TechnicalException("Error al recuperar el contenido del documento externo", e);
        } finally {
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }
        return contenido;
    }
      
      
}//class