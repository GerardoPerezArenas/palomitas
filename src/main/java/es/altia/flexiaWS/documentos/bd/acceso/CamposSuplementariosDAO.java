package es.altia.flexiaWS.documentos.bd.acceso;



import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.flexiaWS.documentos.bd.datos.ExpedienteVO;
import es.altia.flexiaWS.documentos.bd.datos.TramiteVO;
import es.altia.flexiaWS.documentos.bd.util.Campos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.cache.CacheDatosFactoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: marcos.baltar
 * Date: 16-ene-2006
 * Time: 13:44:05
 * To change this template use File | Settings | File Templates.
 */
public class CamposSuplementariosDAO {

   private static Log m_Log = LogFactory.getLog(CamposSuplementariosDAO.class.getName());
   private static CamposSuplementariosDAO instance = null;
    
    public static CamposSuplementariosDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized (CamposSuplementariosDAO.class) {
                if (instance == null) {
                    instance = new CamposSuplementariosDAO();
                }
            }
        }
        return instance;
    }

    public void leerXml (String campos)
        throws Exception{



    }



    /**
     * Busca el numero de expediente que le corresponde al expediente dado
     * @param expediente
     * @param con
     * @param oad
     */


    public void getNumeroExpediente (ExpedienteVO expediente, Connection con, AdaptadorSQLBD oad)
    throws TechnicalException, SQLException{

            String organizacion = expediente.getMunicipio();
            String numOrganizacion = "0";
            String ejercicio="";
            String numero="";
            String proc="";
            String uor="";


            Statement stmt = null;
            ResultSet rs = null;
            String sql="";
            boolean  res = false;

            String parametros[] = null;
            parametros[0] = (String) Campos.campos.getAtributo("numExpORG");
            parametros[1] = "1";

            try{
              sql = "SELECT  " +
              		oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, parametros)+ 
              		" AS " + Campos.campos.getAtributo("numExpORG") + ","
                     + oad.convertir(oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE,null)
                    		 		,AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,"YYYY")
                     + " AS ejercicio FROM A_ORG WHERE " + Campos.campos.getAtributo("codigoORG") 
                     + "=" + organizacion;

              m_Log.debug(sql);
              stmt = con.createStatement();
              rs = stmt.executeQuery(sql);
              if(rs.next()){
                numOrganizacion = rs.getString((String) Campos.campos.getAtributo("numExpORG"));
                ejercicio = rs.getString("ejercicio");
                if (numOrganizacion!=null) res = true;
              } else {
                numOrganizacion = "0";
                ejercicio = null;
              }
              rs.close();


              if (res){
                res = false;
                if ("0".equals(numOrganizacion) ) {
                  // Mostrará el número de expediente como: año/num_expediente
                  proc ="0";
                  uor ="0";
                }else if ("1".equals(numOrganizacion) ){
                  // Mostrará el número de expediente como: año/codigo_procedimiento/numero_expediente
                  proc =expediente.getProcedimiento();
                  uor ="0";
                } else if ( "2".equals(numOrganizacion) ) {
                  // Mostrará el número de expediente como: año/codigo_organización/numero_expediente
                  proc ="0";
                  uor =organizacion;
                }
                
                parametros[0] = (String) Campos.campos.getAtributo("numeroNEX");

                sql = "SELECT  " +
                		oad.funcionMatematica(AdaptadorSQLBD.FUNCIONMATEMATICA_MAX, parametros)+ 
                		" + 1 as num FROM E_NEX WHERE " 
                		+ Campos.campos.getAtributo("codMunicipioNEX") + "=" + organizacion
                		+ " AND " + Campos.campos.getAtributo("ejercicioNEX") +"="+ejercicio
                		+ " AND " + Campos.campos.getAtributo("codProcedimientoNEX") + "='"+ proc+"' "
                		+ " AND " + Campos.campos.getAtributo("uorNEX") + "="+ uor;

                 if (m_Log.isDebugEnabled()) m_Log.debug(sql);

                rs = stmt.executeQuery(sql);
                if(rs.next()) {
                    numero =  rs.getString("num");
                    if (numero!=null)
                    {
                      sql = "UPDATE E_NEX SET "+ Campos.campos.getAtributo("numeroNEX")  +"="+ numero + " "
                         + " WHERE " + Campos.campos.getAtributo("codMunicipioNEX") + "=" + organizacion
                         + " AND " + Campos.campos.getAtributo("ejercicioNEX") +"="+ejercicio
                         + " AND " + Campos.campos.getAtributo("codProcedimientoNEX") + "='"+ proc+"' "
                         + " AND " + Campos.campos.getAtributo("uorNEX") + "="+ uor;
                    } else {
                      numero = "1";
                      sql = "INSERT INTO E_NEX ("
                              + Campos.campos.getAtributo("codMunicipioNEX")+ ","
                              + Campos.campos.getAtributo("ejercicioNEX") +","
                              + Campos.campos.getAtributo("codProcedimientoNEX") +","
                              + Campos.campos.getAtributo("uorNEX") + ","
                              + Campos.campos.getAtributo("numeroNEX") +" ) "
                      + " VALUES (" + organizacion + "," +ejercicio +",'"+proc +"',"+ uor + "," +numero+")";
                    }
                    res = true;
                }

                rs.close();


                // Actualizar tabla.
                if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                int r = stmt.executeUpdate(sql);

               if (r>0)
                 res = true;
                 stmt.close();
              } else {
                 ejercicio="";
                 numero="";
                 proc="";
                 uor="";
              }

              stmt.close();
            } catch (Exception e) {
              e.printStackTrace();
              ejercicio="";
              numero="";
              proc="";
              uor="";
              throw new TechnicalException(e.getMessage());

            } finally {
                if (!res){
                      //rollBackTransaction(oad, con, null);
                    if(stmt!=null) stmt.close();
                    if(rs!=null) rs.close();
                    throw new TechnicalException("error");

                }
                expediente.setEjercicico(ejercicio);

                 if ( (numero.length()>0) && (numero.length()<6) )
                     numero= "000000".substring(0,6-numero.length()) +numero;
                 if ("0".equals(numOrganizacion) ) expediente.setNumero(ejercicio+"/"+numero);
                 else if ("1".equals(numOrganizacion) ) expediente.setNumero(ejercicio+"/"+proc+"/"+numero);
                 else if ( "2".equals(numOrganizacion) ) expediente.setNumero(ejercicio+"/"+uor+"/"+numero);
                 
                
                 
            }
     }

   

    
    public String getTipoCampo(String municipio, String procedimiento, String campo,Connection con)
        throws SQLException{
        
        Statement st = null;
        ResultSet rs = null;
        String retorno = null;
        
        try{
        String sql = "SELECT "+ Campos.campos.getAtributo("codTipoDatoPCA") +
                " FROM E_PCA "+
                " WHERE " + Campos.campos.getAtributo("codMunicipioPCA") +" = " + municipio+
                " AND " + Campos.campos.getAtributo("codProcedimientoPCA") +" = '" + procedimiento + "'"+
                " AND " + Campos.campos.getAtributo("codCampoPCA") +" = '" + campo +"'";

       if(m_Log.isDebugEnabled()) m_Log.debug(sql);
       st = con.createStatement();
       rs = st.executeQuery(sql);
       if (rs.next())
            retorno= rs.getString(Campos.campos.getAtributo("codTipoDatoPCA").toString());
       else
            retorno= "-1";
       
        }catch(Exception e)
        {
            m_Log.error(e.getMessage());
        }finally{
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }
        return retorno;

    }

   


    public int setDatoNumerico(String codMunicipio, String ejercicio, String numeroExpediente,
                               String codDato, String valorDato, Connection con)
        throws SQLException{

        Statement stmt = null;
        String sql = null;
        ResultSet rs = null;
        int resultadoInsertar = 0;
        
        try{
            String gestor = ConfigServiceHelper.getConfig("techserver").getString("CON.gestor");
            String parametros[] = {gestor};
            //parametros[0] = gestor;
            AdaptadorSQLBD abd = new AdaptadorSQLBD (parametros);

          

              sql = "DELETE FROM E_TNU "
                      + " WHERE " + Campos.campos.getAtributo("codMunicipioTNU") + "=" + codMunicipio
                      + " AND " + Campos.campos.getAtributo("ejercicioTNU") + "=" + ejercicio
                      + " AND " + Campos.campos.getAtributo("numeroExpedienteTNU") + "='" + numeroExpediente
                      + "' AND " + Campos.campos.getAtributo("codCampoTNU") + "='" + codDato + "'";

              if(m_Log.isDebugEnabled()) m_Log.debug(sql);

              stmt = con.createStatement();
              stmt.executeUpdate(sql);

              if(valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO E_TNU ( " + Campos.campos.getAtributo("codMunicipioTNU") + ","
                        + Campos.campos.getAtributo("ejercicioTNU") + ","
                        + Campos.campos.getAtributo("numeroExpedienteTNU") + ","
                        + Campos.campos.getAtributo("codCampoTNU") + ","
                        +  Campos.campos.getAtributo("valorTNU") + ") VALUES ("
                        + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                        + codDato + "'," +
                        abd.convertir("'"+valorDato+"'", AdaptadorSQLBD.CONVERTIR_COLUMNA_NUMERO, "9999999999D99") + ")";

                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                  m_Log.debug(sql);
                resultadoInsertar = stmt.executeUpdate(sql);
              } else {
                resultadoInsertar = 1;
              }
            stmt.close();
        }catch(Exception e){
             m_Log.error(e.getMessage());
        }finally{
            if(stmt!=null) stmt.close();
            if(rs!=null) rs.close();
        }

      return resultadoInsertar;
    }

    
    public int setDatoTexto(String codMunicipio, String ejercicio, String numeroExpediente,
                               String codDato, String valorDato, Connection con)
        throws SQLException{

        PreparedStatement ps  =null;
        Statement stmt = null;
        String sql = null;        
        int resultadoInsertar = 0;

        try{
            sql = "DELETE FROM E_TXT "
                    + " WHERE " + Campos.campos.getAtributo("codMunicipioTXT") + "=" + codMunicipio
                    + " AND " + Campos.campos.getAtributo("ejercicioTXT") + "=" + ejercicio
                    + " AND " + Campos.campos.getAtributo("numeroExpedienteTXT") + "='" + numeroExpediente +"'"
                    + " AND " + Campos.campos.getAtributo("codCampoTXT") + "='" + codDato + "'";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            stmt = con.createStatement();
            stmt.executeUpdate(sql);

            if(valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO E_TXT(TXT_MUN,TXT_EJE,TXT_NUM,TXT_COD,TXT_VALOR) VALUES(?,?,?,?,?)";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                ps = con.prepareStatement(sql);

                int i=1;
                ps.setInt(i++,Integer.parseInt(codMunicipio));
                ps.setInt(i++,Integer.parseInt(ejercicio));
                ps.setString(i++,numeroExpediente);
                ps.setString(i++,codDato);
                ps.setString(i++,valorDato);

                resultadoInsertar = ps.executeUpdate();
               
            } else {
              resultadoInsertar = 1;
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw e;            
        }finally{
            try{
                if(stmt!=null) stmt.close();
                if(ps!=null) ps.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return resultadoInsertar;
    }


    public int setDatoFecha(String codMunicipio, String ejercicio, String numeroExpediente,
                               String codDato, String valorDato,String mascara, Connection con)
        throws SQLException{

        Statement stmt = null;
        String sql = null;
        ResultSet rs = null;
        String gestor = ConfigServiceHelper.getConfig("techserver").getString("CON.gestor");
        String parametros[] = {gestor};
        //parametros[0] = gestor;
        AdaptadorSQLBD abd = new AdaptadorSQLBD (parametros);

        int resultadoInsertar = 0;

        try{
          sql = "DELETE FROM E_TFE "
                  + " WHERE " + Campos.campos.getAtributo("codMunicipioTFE") + "=" + codMunicipio
                  + " AND " + Campos.campos.getAtributo("ejercicioTFE") + "=" + ejercicio
                  + " AND " + Campos.campos.getAtributo("numeroExpedienteTFE") + "='" + numeroExpediente +"'"
                  + " AND " + Campos.campos.getAtributo("codCampoTFE") + "='" + codDato + "'";

          if(m_Log.isDebugEnabled()) m_Log.debug(sql);

          stmt = con.createStatement();
          stmt.executeUpdate(sql);

          if(valorDato != null && !"".equals(valorDato)) {
            if(mascara == null || "".equals(mascara)) {
              sql = "INSERT INTO E_TFE ( "+ Campos.campos.getAtributo("codMunicipioTFE") + ","
                    + Campos.campos.getAtributo("ejercicioTFE") + ","
                    + Campos.campos.getAtributo("numeroExpedienteTFE") + ","
                    + Campos.campos.getAtributo("codCampoTFE") + ","
                    + Campos.campos.getAtributo("valorTFE") + ") VALUES ("
                    + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                    + codDato + "'," +
                    abd.convertir("'" + valorDato + "'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") + ")";
            
            } else {
              sql = "INSERT INTO E_TFE ( "+ Campos.campos.getAtributo("codMunicipioTFE") + ","
                    + Campos.campos.getAtributo("ejercicioTFE") + ","
                    + Campos.campos.getAtributo("numeroExpedienteTFE") + ","
                    + Campos.campos.getAtributo("codCampoTFE") + ","
                    + Campos.campos.getAtributo("valorTFE") + ") VALUES ("
                    + codMunicipio + "," + ejercicio + ",'" + numeroExpediente + "','"
                    + codDato + "'," +
                    abd.convertir("'" + valorDato + "'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,mascara) + ")";
            }

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            resultadoInsertar = stmt.executeUpdate(sql);
          } else {
            resultadoInsertar = 1;
          }

      stmt.close();
      }catch(Exception e){
             m_Log.error(e.getMessage());
        }finally{
            if(stmt!=null) stmt.close();
            if(rs!=null) rs.close();
        }
        
      return resultadoInsertar;
    }

        
    
    public int setDatoTextoLargo (String codMunicipio, String ejercicio, String numeroExpediente,
                               String codDato, String valorDato, Connection con)
        throws SQLException {

        String sql = null;
        Statement stmt = null;
        PreparedStatement ps = null;
        int resultadoInsertar = 0;

        try{
            sql = "DELETE FROM E_TTL "
                    + " WHERE " + Campos.campos.getAtributo("codMunicipioTTL") + "=" + codMunicipio
                    + " AND " + Campos.campos.getAtributo("ejercicioTTL") + "=" + ejercicio
                    + " AND " + Campos.campos.getAtributo("numeroExpedienteTTL") + "='" + numeroExpediente +"'"
                    + " AND " + Campos.campos.getAtributo("codCampoTTL") + "='" + codDato + "'";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            m_Log.debug(sql);
            stmt = con.createStatement();
            stmt.executeUpdate(sql);

            if(valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO E_TTL (TTL_MUN,TTL_EJE,TTL_NUM,TTL_COD,TTL_VALOR) VALUES(?,?,?,?,?)";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                int i=1;
                ps = con.prepareStatement(sql);
                ps.setInt(i++,Integer.parseInt(codMunicipio));
                ps.setInt(i++,Integer.parseInt(ejercicio));
                ps.setString(i++,numeroExpediente);
                ps.setString(i++,codDato);
                java.io.StringReader cr = new java.io.StringReader(valorDato);
                ps.setCharacterStream(i++, cr, valorDato.length());              
                resultadoInsertar = ps.executeUpdate();
              
            } else {
              resultadoInsertar = 1;
            }
            
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
            
        }finally{
            try{
                if(ps!=null) ps.close();
                if(stmt!=null) stmt.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
      
      return resultadoInsertar;
    }


    public Vector cargaEstructuraDatosSuplementarios1(String codMunicipio, String codProcedimiento, AdaptadorSQLBD oad, Connection con) throws TechnicalException {
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector lista = new Vector();

        try{
        st = con.createStatement();


        from = "pca_cod , pca_des , pca_plt , plt_url , pca_tda , pca_tam , pca_mas , pca_obl , pca_nor , pca_rot , pca_activo";
        where = " pca_mun  = "+ codMunicipio + " AND  pca_pro  = '" + codProcedimiento + "'";
        String[] join = new String[5];
        join[0] = "E_PCA";
        join[1] = "INNER";
        join[2] = "e_plt";
        join[3] = "e_pca.pca_plt = e_plt.plt_cod";
        join[4] = "false";

        sql = oad.join(from,where,join);
        String parametros[] = {"9","9"};
        sql += oad.orderUnion(parametros);

        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        rs = st.executeQuery(sql);
        while(rs.next()){
          EstructuraCampo eC = new EstructuraCampo();
          String codCampo = rs.getString("pca_cod");
          eC.setCodCampo(codCampo);
          String descCampo = rs.getString("pca_des");
          eC.setDescCampo(descCampo);
          String codPlantilla = rs.getString("pca_plt");
          eC.setCodPlantilla(codPlantilla);
          String urlPlantilla = rs.getString("plt_url");
          eC.setURLPlantilla(urlPlantilla);
          String codTipoDato = rs.getString("pca_tda");
          eC.setCodTipoDato(codTipoDato);
          String tamano = rs.getString("pca_tam");
          eC.setTamano(tamano);
          String mascara = rs.getString("pca_mas");
          eC.setMascara(mascara);
          String obligatorio = rs.getString("pca_obl");
          eC.setObligatorio(obligatorio);
          String numeroOrden = rs.getString("pca_nor");
          eC.setNumeroOrden(numeroOrden);
          String rotulo = rs.getString("pca_rot");
          eC.setRotulo(rotulo);
          String activo = rs.getString("pca_activo");
          eC.setActivo(activo);
          String soloLectura = "false";
          eC.setSoloLectura(soloLectura);
          lista.addElement(eC);
        }
        rs.close();



      }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
      }finally {
      try{
          rs.close();
          st.close();
       //   oad.devolverConexion(con);
      }catch(Exception bde) {
        bde.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(bde.getMessage());
      }
      return lista;
      }
    }



    public Vector cargaEstructuraDatosSuplementarios2(String codMunicipio, String codProcedimiento, String numero, String tramite, AdaptadorSQL oad, Connection con) throws TechnicalException {        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector lista = new Vector();

        try{
        st = con.createStatement();

        from = "tca_cod, tca_des, tca_plt, plt_url, tca_tda, tca_rot" +
               ", tca_vis , tca_tra, tca_activo, tml_valor, cro_ocu";
        where = "tca_mun = " + codMunicipio + " AND tca_pro = '" + codProcedimiento + "' AND tca_tra="+tramite;
        String[] join1 = new String[8];
        join1[0] = "e_tml, e_cro, E_TCA";
        join1[1] = "INNER";
        join1[2] = "e_plt";
        join1[3] = "e_tca.tca_plt =e_plt.plt_cod";
        join1[4] = "INNER";
        join1[5] = "e_tra";
        join1[6] = "e_tca.tca_mun =e_tra.tra_mun AND " +
                             "e_tca.tca_pro =e_tra.tra_pro AND " +
                             "e_tca.tca_tra =e_tra.tra_cod AND e_tra.tra_fba IS NULL";
        // Para quitar la descripción del tramite
        //join1[7] = " and e_tca." + tca_mun + "=e_tml." + tml_mun + " AND e_tca." + tca_pro + "= e_tml." + tml_pro + " and e_tca." + tca_tra + "= e_tml." + tml_tra;
        join1[7] = "false";
        sql = oad.join(from,where,join1);

            sql = sql + " and e_tca.tca_mun =e_tml.tml_mun AND e_tca.tca_pro=e_tml.tml_pro and e_tca.tca_tra= e_tml.tml_tra";
            // Consulta modificada para que obtenga solo los campos de ese expediente y visibles
            //String ejercicio = (String)gVO.getAtributo("ejercicio");

            sql = sql + " AND e_tca.tca_vis = 'S'";
            sql = sql + " AND e_tca.tca_mun= e_cro.CRO_MUN AND e_tca.TCA_PRO  = e_cro.CRO_PRO AND e_tca.tca_TRA = e_cro.CRO_TRA";
            sql = sql + " AND e_cro.cro_num = '" + numero + "' ORDER BY  e_tca.TCA_TRA, e_tca.TCA_NOR, e_cro.CRO_OCU";
            // Fin consulta modificada
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        rs = st.executeQuery(sql);
        Config m_Conf = ConfigServiceHelper.getConfig("common");
        String campo="E_PLT.CodigoPlantillaFichero";
        String tipoDatoFichero = m_Conf.getString(campo);
        while(rs.next()){
          EstructuraCampo eC = new EstructuraCampo();
          String codCampo = rs.getString("tca_cod");
          eC.setCodCampo(codCampo);
          String descCampo = rs.getString("tca_des");
          eC.setDescCampo(descCampo);
          String codPlantilla = rs.getString("tca_plt");
          eC.setCodPlantilla(codPlantilla);
          String urlPlantilla = rs.getString("plt_url");
          eC.setURLPlantilla(urlPlantilla);
          String codTipoDato = rs.getString("tca_tda");
          eC.setCodTipoDato(codTipoDato);
          eC.setTamano("");
          eC.setMascara("");
          eC.setObligatorio("0");
          eC.setNumeroOrden("0");
          String rotulo = rs.getString("tca_rot");
          eC.setRotulo(rotulo);
          String soloLectura = "true";
          eC.setSoloLectura(soloLectura);
          String codTramite = rs.getString("tca_tra");
          eC.setCodTramite(codTramite);
          String activo = rs.getString("tca_activo");
          eC.setActivo(activo);
          String descripcionTramite = rs.getString("tml_valor");
          eC.setDescripcionTramite(descripcionTramite);
          String ocurrencia = rs.getString("cro_ocu");
          eC.setOcurrencia(ocurrencia);
          String visibleExpediente = rs.getString("tca_vis");
          if("S".equals(visibleExpediente)) {
            lista.addElement(eC);
          }
        }

      }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
      }finally {
      try{
          rs.close();
          st.close();
       //   oad.devolverConexion(con);
      }catch(Exception bde) {
        bde.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(bde.getMessage());
      }
      return lista;
      }
    }


    public int setDatoDesplegable(String codMunicipio, String ejercicio, String numeroExpediente,
            String codDato, String valorDato,Connection con)
        throws SQLException{

        PreparedStatement ps = null;
        String sql = null;

        int resultadoInsertar = 0;

        try{
          sql = "DELETE FROM E_TDE "
                  + " WHERE TDE_MUN=" + codMunicipio
                  + " AND TDE_EJE=" + ejercicio
                  + " AND TDE_NUM='" + numeroExpediente + "'"
                  + " AND TDE_COD='" + codDato+ "'";

          if(m_Log.isDebugEnabled()) m_Log.debug(sql);
          m_Log.debug(sql);
          ps = con.prepareStatement(sql);
          int rowsDeleted = ps.executeUpdate();

          m_Log.debug("Se ha eliminado: " + rowsDeleted);

          m_Log.debug("Codigo del dato: " + codDato);
          m_Log.debug("Valor del dato: " + valorDato);
          if(valorDato != null && !"".equals(valorDato)) {
            sql = "INSERT INTO E_TDE (TDE_MUN,TDE_EJE,TDE_NUM,TDE_COD,TDE_VALOR) " +
                  "VALUES(?,?,?,?,?)";

            ps = con.prepareStatement(sql);
            int i=1;
            ps.setInt(i++,Integer.parseInt(codMunicipio));
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numeroExpediente);
            ps.setString(i++,codDato);
            ps.setString(i++,valorDato);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resultadoInsertar = ps.executeUpdate();
            
          } else {
            resultadoInsertar = 1;
          }

        ps.close();
        }catch(Exception e){
             m_Log.error(e.getMessage());
        }finally{
            if(ps!=null) ps.close();
           
        }
      return resultadoInsertar;
    }
    
    
     public boolean esExpedienteValido(ExpedienteVO idExpedienteVO, Connection con) throws TechnicalException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean valido = false;
        String codPro = idExpedienteVO.getProcedimiento();
        String numExp = idExpedienteVO.getNumero();
        String ejercicio = idExpedienteVO.getEjercicio();
        //SERA VALIDO SI EXISTE EN E_EXP Y LA FECHA FIN NO EXISTE

        // Creamos la select con los parametros adecuados.
        String sql = "SELECT EXP_NUM FROM E_EXP WHERE  EXP_PRO = ? AND EXP_EJE = ? AND EXP_NUM = ? AND EXP_FEF IS NULL";
        try {
            m_Log.debug(sql);
            m_Log.debug("codPro " + codPro + "numExp " + numExp);

            ps = con.prepareStatement(sql);
            int i = 1;
           
            ps.setString(i++, codPro);
            ps.setInt(i++, Integer.parseInt(ejercicio));
            ps.setString(i++, numExp);
            
            rs = ps.executeQuery();
            while (rs.next()) {
                valido = true;
            }
        } catch (Exception e) {
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            m_Log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
         } finally {
             try {
                 if(ps!=null) ps.close();
                 if(rs!=null) rs.close();

                 //   oad.devolverConexion(con);
             } catch (Exception bde) {
                 bde.printStackTrace();
                 if (m_Log.isErrorEnabled()) {
                     m_Log.error(bde.getMessage());
                 }
             }
             return valido;
         }
     }
     
     
      public int estaOcurrenciaTramiteCerrada(ExpedienteVO idExpedienteVO, TramiteVO idTramiteVO,int codOrganizacion,Connection con)
            throws TechnicalException {
        int salida = -1;                              
        String sql = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        java.sql.Timestamp tFecFinalizacion = null;
        boolean existeTramite = false;
        
        try {
            
            sql = "SELECT CRO_FEF FROM E_CRO WHERE CRO_TRA=? AND CRO_EJE=? AND CRO_PRO=? AND "
                + "CRO_NUM=? AND CRO_OCU=? AND CRO_MUN=?";
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, Integer.parseInt(idTramiteVO.getCodTramite()));
            ps.setInt(i++, Integer.parseInt(idExpedienteVO.getEjercicio()));
            ps.setString(i++,idExpedienteVO.getProcedimiento());
            ps.setString(i++,idExpedienteVO.getNumero());
            ps.setInt(i++,Integer.parseInt(idTramiteVO.getOcurrenciaTramite()));
            ps.setInt(i++,codOrganizacion);
            
            rs = ps.executeQuery();
            while(rs.next()) {
                tFecFinalizacion = rs.getTimestamp("CRO_FEF");        
                existeTramite = true;
            }
            
            if(existeTramite){
                // La ocurrencia del trámite existe, se comprueba si está o no finalizado
                if(tFecFinalizacion!=null)
                    salida = 0; // Ocurrencia de trámite existe y está finalizada
                else
                    salida = 1; // Ocurrencia de trámite existe y está pendiente
            }else
                salida =-1; // No existe la ocurrencia del trámite
            
            
        } catch (Exception e) {
            m_Log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        } finally {
             try {
                 if(ps!=null) ps.close();
                 if(rs!=null) rs.close();

                 //   oad.devolverConexion(con);
             } catch (Exception bde) {
                 bde.printStackTrace();
                 if (m_Log.isErrorEnabled()) {
                     m_Log.error(bde.getMessage());
                 }
             }
        }
        return salida;
    }
      
      
       public boolean tieneDocumentoSinFirma(ExpedienteVO idExpedienteVO, TramiteVO idTramiteVO,int codOrganizacion,Connection con) throws TechnicalException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean tieneFirmasPendientes = false;

        try {
            String sql = "SELECT CRD_DOT FROM E_CRD WHERE CRD_PRO=? AND CRD_NUM=? AND CRD_TRA=? "
                    + "AND CRD_OCU=? AND CRD_EJE=? AND CRD_MUN=? AND (CRD_FIR_EST='E' OR CRD_FIR_EST='O')";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++,idExpedienteVO.getProcedimiento());
            ps.setString(i++,idExpedienteVO.getNumero());
            ps.setInt(i++, Integer.parseInt(idTramiteVO.getCodTramite()));
            ps.setInt(i++, Integer.parseInt(idTramiteVO.getOcurrenciaTramite()));
            ps.setInt(i++,  Integer.parseInt(idExpedienteVO.getEjercicio()));
            ps.setInt(i++, codOrganizacion);
            rs = ps.executeQuery();
            if (rs.next()) {
                tieneFirmasPendientes = true;
            }
            return tieneFirmasPendientes;

        } catch (Exception e) {
            m_Log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        } finally {
             try {
                 if(ps!=null) ps.close();
                 if(rs!=null) rs.close();

                 //   oad.devolverConexion(con);
             } catch (Exception bde) {
                 bde.printStackTrace();
                 if (m_Log.isErrorEnabled()) {
                     m_Log.error(bde.getMessage());
                 }
             }
        }
    }
       
       
       public boolean perteneceRelacion(ExpedienteVO idExpedienteVO,
            Connection con) throws TechnicalException {
       PreparedStatement ps = null;
        ResultSet rs = null;
        boolean perteneceRelacion = false;
        AdaptadorSQLBD oad = null;

        try {
           String sql = "select g_rel.rel_num from g_rel, g_exp where rel_ffi is null and g_rel.rel_num=g_exp.rel_num and exp_num=?";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            ps = con.prepareStatement(sql);
            int i = 1;
            
            ps.setString(i++,idExpedienteVO.getNumero());           
            
            rs = ps.executeQuery();
            if (rs.next()) {
               
                perteneceRelacion = true;
            }
            return perteneceRelacion;

        } catch (Exception e) {
            m_Log.error(e.getMessage());
           
            throw new TechnicalException(e.getMessage(), e);
        } finally {
             try {
                 if(ps!=null) ps.close();
                 if(rs!=null) rs.close();

                 //   oad.devolverConexion(con);
             } catch (Exception bde) {
                 bde.printStackTrace();
                 if (m_Log.isErrorEnabled()) {
                     m_Log.error(bde.getMessage());
                 }
             }
        }
    }
    

        public int comprobarInicioExpediente(int codOrganizacion,ExpedienteVO expedienteVO, Connection con) throws TechnicalException {
        
        String procedimiento = expedienteVO.getProcedimiento();
        String unidadInicio = expedienteVO.getUor() ;
        String unidadTramiteInicio = expedienteVO.getUorTramiteInicio();
        int salida = -1;
        try {
            boolean esProcValido = esProcedimientoValido(procedimiento, con);
            if(!esProcValido){
                salida = 1;
            }else{                                
                // si la unidad de inicio no es valida unidadDevuelta será -1
                int res = esUnidadInicioValida(codOrganizacion,procedimiento, Integer.parseInt(unidadInicio), con);
                if(res==1) 
                    salida = 2;
                else
                if(res==2) 
                    salida = 3; 
                else{
                    m_Log.debug("comprobarInicioExpediente UOR_TRAMITE inicio "+unidadTramiteInicio);
                    
                    res = esUnidadTramiteInicioValida(codOrganizacion,procedimiento, Integer.parseInt(unidadTramiteInicio), con);
                    if (res==1) salida=4;
                   
                }
            }
            
        } catch (Exception e) {
            m_Log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(), e);
        }
        
        return salida;
    }
        
        
        public boolean esProcedimientoValido(String procedimiento, Connection con) throws TechnicalException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultado = null;
        boolean valido = false;
        Timestamp ahora = new Timestamp(System.currentTimeMillis());

        String sql = "SELECT PRO_COD FROM E_PRO WHERE PRO_COD = ? AND PRO_FLD <=  ? AND "
                + "(PRO_FLH IS NULL OR PRO_FLH > ?) AND PRO_FBA IS NULL AND PRO_EST=1";
        try {
            m_Log.debug(sql);
            m_Log.debug("procedimiento " + procedimiento + ", fechaActual," + ahora);
            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, procedimiento);
            ps.setTimestamp(i++, ahora);
            ps.setTimestamp(i++, ahora);
            rs = ps.executeQuery();
            valido = rs.next();
            m_Log.debug("valido " + valido);
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error(e.getMessage());
            }
        } finally {
            try {
               if(ps!=null) ps.close();
                if(rs!=null) rs.close();
                //   oad.devolverConexion(con);
            } catch (Exception bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error(bde.getMessage());
                }
            }
          
        }
        return valido;
    }
        
    public int esUnidadInicioValida(int codOrganizacion, String codProcedimiento, int unidadInicio, Connection con) throws TechnicalException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int resultado = -1;
         String gestor = ConfigServiceHelper.getConfig("techserver").getString("CON.gestor");	
        String parametros[] = {gestor};

         UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(parametros[6],String.valueOf(unidadInicio));

             try {	
            if (uorDTO==null){
                resultado = 1;
            } else {
                // Se buscan las posibles unidades de inicio del expediente
                String sql = "SELECT PUI_COD FROM E_PUI WHERE PUI_PRO=? AND PUI_MUN=?";
                m_Log.debug(sql);
                m_Log.debug("+procedimiento " + codProcedimiento + " +unidadInicio " + unidadInicio + " codOrganizacion: " + codOrganizacion);
                ps = con.prepareStatement(sql);
                ps.setString(1, codProcedimiento);
                ps.setInt(2, codOrganizacion);
                rs = ps.executeQuery();

                ArrayList<Integer> unidades = new ArrayList<Integer>();
                while (rs.next()) {
                    unidades.add(rs.getInt("PUI_COD"));
                }

                if (unidades.size() == 0) {
                    // Si no hay unidades de inicio, es que la unidad de inicio puede ser cualquiera
                    resultado = 0;
                } else {
                    if (unidades.contains(unidadInicio)) {
                        resultado = 0;
                    } else {
                        resultado = 2;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error(e.getMessage());
            }
        } finally {
            try {
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
                //   oad.devolverConexion(con);
            } catch (Exception bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error(bde.getMessage());
                }
            }
            return resultado;
        }
    }
    
    
    
    public int esUnidadTramiteInicioValida(int codOrganizacion, String codProcedimiento, int unidadInicio, Connection con) throws TechnicalException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int resultado = -1;
        String gestor = ConfigServiceHelper.getConfig("techserver").getString("CON.gestor");
        String parametros[] = {gestor};
        boolean tieneOtraUnidadDeInicio = false;
        int codigoTramiteInicio = 0;


        UORDTO uorDTO = (UORDTO) CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(parametros[6], String.valueOf(unidadInicio));

        try {

            if (uorDTO == null) {

                resultado = 1;
            } else {
                // Se buscan las posibles unidades de tramitacion del trámite de inicio
                String sql = "SELECT TRA_COD,TRA_UTR FROM E_TRA,E_PRO WHERE TRA_PRO=? AND TRA_MUN=? AND TRA_PRO=PRO_COD AND TRA_MUN=PRO_MUN AND PRO_TRI=TRA_COD";
                m_Log.debug(sql);
                ps = con.prepareStatement(sql);
                ps.setString(1, codProcedimiento);
                ps.setInt(2, codOrganizacion);
                rs = ps.executeQuery();

                int uorTramiteInicio;
                if (rs.next()) {
                    uorTramiteInicio = rs.getInt("TRA_UTR");
                    codigoTramiteInicio = rs.getInt("TRA_COD");
                    m_Log.debug("codigoTramiteInicio pro " + codigoTramiteInicio);
                    if (uorTramiteInicio != 0) {
                        return 0;
                    } else {
                        tieneOtraUnidadDeInicio = true;
                    }
                }

                if (tieneOtraUnidadDeInicio) {
                    int numero = 1;
                    sql = "SELECT COUNT(*) as NUM FROM E_TRA_UTR WHERE TRA_PRO=? AND TRA_MUN=? AND TRA_COD=? AND TRA_UTR_COD=?";
                    m_Log.debug(sql);
                    ps = con.prepareStatement(sql);
                    ps.setString(1, codProcedimiento);
                    ps.setInt(2, codOrganizacion);
                    ps.setInt(3, codigoTramiteInicio);
                    ps.setInt(4, unidadInicio);
                    rs = ps.executeQuery();


                    while (rs.next()) {
                        numero = rs.getInt("NUM");
                    }
                    ps.close();
                    rs.close();
                    if (numero == 0) {
                        resultado = 1;
                    }

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error(e.getMessage());
            }
        } finally {
            try {
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
                //   oad.devolverConexion(con);
            } catch (Exception bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) {
                    m_Log.error(bde.getMessage());
                }
            }
            return resultado;
        }
    }
}
