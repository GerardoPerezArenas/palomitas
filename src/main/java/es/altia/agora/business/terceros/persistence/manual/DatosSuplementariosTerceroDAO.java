package es.altia.agora.business.terceros.persistence.manual;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.ByteArrayInputStream; 
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;


public class DatosSuplementariosTerceroDAO {

    private static DatosSuplementariosTerceroDAO instance = null;
    private Logger log = Logger.getLogger(DatosSuplementariosTerceroDAO.class);
    

    private DatosSuplementariosTerceroDAO(){
    }


    public static DatosSuplementariosTerceroDAO getInstance(){
        if(instance==null)
            instance = new DatosSuplementariosTerceroDAO();

        return instance;
    }


   /**
     * Recupera la estructura de los datos suplementarios definidos para los terceros de una organización
     * @param codMunicipio: Código del municipio
     * @param oad: AdaptadorSQLBD necesario para generar la consulta
     * @param con: Conexión a la BD
     * @return Vector>>
     * @throws es.altia.common.exception.TechnicalException
     */
    public Vector<EstructuraCampo> cargaEstructuraDatosSuplementariosTercero(String codMunicipio,  AdaptadorSQL oad, Connection con) throws TechnicalException {
      Statement st = null;
      Statement st2= null;
      ResultSet rs = null;
      ResultSet rs2 = null;
      String sql = "";
      String sql2 = "";
      String from = "";
      String where = "";
      Vector<EstructuraCampo> lista = new Vector<EstructuraCampo>();

      try{


        st = con.createStatement();
        from = "COD_CAMPO,COD_MUNICIPIO,DESCRIPCION,COD_PLANTILLA,PLT_URL,TIPO_DATO,TAMANO,MASCARA,OBLIGATORIO,ORDEN,ROTULO,ACTIVO,DESPLEGABLE";
        where = "COD_MUNICIPIO=" + codMunicipio;
        String[] join = new String[5];
        join[0] = "T_CAMPOS_EXTRA";
        join[1] = "INNER";
        join[2] = "T_PLT";
        join[3] = "T_CAMPOS_EXTRA.COD_PLANTILLA=T_PLT.PLT_COD";
        join[4] = "false";

        sql = oad.join(from,where,join);
        String parametros[] = {"10","10"};
        sql += oad.orderUnion(parametros);
        log.debug(sql);

        rs = st.executeQuery(sql);
        while(rs.next()){
            EstructuraCampo eC = new EstructuraCampo();
            String codCampo = rs.getString("COD_CAMPO");
            eC.setCodCampo(codCampo);
            String descCampo = rs.getString("DESCRIPCION");
            eC.setDescCampo(descCampo);
            String codPlantilla = rs.getString("COD_PLANTILLA");
            eC.setCodPlantilla(codPlantilla);
            String urlPlantilla = rs.getString("PLT_URL");
            eC.setURLPlantilla(urlPlantilla);
            String codTipoDato = rs.getString("TIPO_DATO");
            eC.setCodTipoDato(codTipoDato);
            String tamano = rs.getString("TAMANO");
            eC.setTamano(tamano);
            String mascara = rs.getString("MASCARA");
            eC.setMascara(mascara);
            String obligatorio = rs.getString("OBLIGATORIO");
            eC.setObligatorio(obligatorio);
            String numeroOrden = rs.getString("ORDEN");
            eC.setNumeroOrden(numeroOrden);
            String rotulo = rs.getString("ROTULO");
            eC.setRotulo(rotulo);
            String activo = rs.getString("ACTIVO");
            eC.setActivo(activo);
            String soloLectura = "false";
            eC.setSoloLectura(soloLectura);
            
            String desplegable = rs.getString("DESPLEGABLE");

            /*****/
            try {
                if (desplegable!=null && !"".equals(desplegable)) {
                    eC.setDesplegable(desplegable);
                }

                log.debug("DESPLEGABLE : " + desplegable);

                if (!desplegable.equals("")) {

                    Vector listaCod = new Vector();
                    Vector listaDesc = new Vector();
                    st2 = con.createStatement();

                    sql2 = "SELECT DES_VAL_COD,DES_NOM FROM E_DES_VAL WHERE " +
                           "DES_COD='"+ desplegable + "' ORDER BY DES_NOM";
                    rs2 = st2.executeQuery(sql2);
                    log.debug("cargaEstructuraDatosSuplementariosTercero sql: " + sql2);

                    while (rs2.next()) {
                        listaCod.addElement("'"+ rs2.getString("DES_VAL_COD")+"'");
                        listaDesc.addElement("'"+ rs2.getString("DES_NOM")+"'");
                    }

                    SigpGeneralOperations.closeStatement(st2);
                    SigpGeneralOperations.closeResultSet(rs2);
                    eC.setListaCodDesplegable(listaCod);
                    eC.setListaDescDesplegable(listaDesc);

                }// if
                
            } catch (NullPointerException e){
                
            }

            /******/
            lista.add(eC);

       }// while


    }catch (BDException e){
      e.printStackTrace();
      log.error(e.getMessage());
      throw new TechnicalException(e.getMessage(),e);
    }
    catch (SQLException e){
      e.printStackTrace();
      log.error(e.getMessage());
      throw new TechnicalException(e.getMessage(),e);
    } finally{
        SigpGeneralOperations.closeStatement(st);
        SigpGeneralOperations.closeResultSet(rs);
        SigpGeneralOperations.closeStatement(st2);
        SigpGeneralOperations.closeResultSet(rs2);
    }

    return lista;
  }



    /**
     * Verifica si una cadena de caracteres contiene un valor numérico
     * @param dato: String
     * @return boolean 
     */
    private boolean isInteger(String dato){
        boolean exito = false;
        try{
            Integer.parseInt(dato);
            exito = true;
        }catch(NumberFormatException e){
            exito = false;
        }
        return exito;
    }



   /**
     * Se recuperalos valores de los campos suplementarios para un determinado tercero
     * @param codMunicipio: Código del municipio
     * @param codTercero: Código del tercero
     * @param con: Conexión a la BD
     * @param oad: AdaptadorSQLBD
     * @return CamposFormulario
     */
    public CamposFormulario getValoresNumericos(String codMunicipio,String codTercero,Connection con,AdaptadorSQLBD oad){
        PreparedStatement st = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try{

          
           
            String sql = "SELECT COD_CAMPO," + oad.convertir("VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null) + " AS VALOR FROM T_CAMPOS_NUMERICO WHERE "  +
                        " COD_MUNICIPIO=? AND COD_TERCERO=?";


            String cod="0";
            if(isInteger(codTercero)){
                cod=codTercero;
            }else
                cod="-1";

            log.debug(sql);
            st = con.prepareStatement(sql);
            
            st.setInt(1, Integer.parseInt(codMunicipio));
            st.setInt(2, Integer.parseInt(cod));
            
            rs = st.executeQuery();
            
            String entrar = "no";
            while(rs.next()){
                entrar = "si";
                String codCampo = rs.getString("COD_CAMPO");
                String valorCampo = rs.getString("VALOR");
                if (valorCampo.endsWith(",00")) valorCampo = valorCampo.substring(0, valorCampo.length()-3);
                campos.put(codCampo,valorCampo);
                cF = new CamposFormulario(campos);
            }

            if("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        }catch (Exception e){
            cF = null;
            e.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
        }
        finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return cF;
    }
    
    
     public CamposFormulario getValoresNumericos(String codMunicipio,String codTercero,String codCampo,Connection con,AdaptadorSQLBD oad){
        Statement st = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try{

            st = con.createStatement();
           
            String sql = "SELECT " + oad.convertir("VALOR", oad.CONVERTIR_COLUMNA_TEXTO, null) + " AS VALOR " + 
                         "FROM T_CAMPOS_NUMERICO WHERE "  +
                         "COD_MUNICIPIO=" + codMunicipio + " AND COD_CAMPO='" + codCampo + "'";

            if(isInteger(codTercero)){
                sql = sql + " AND COD_TERCERO=" + codTercero;
            }else
                sql = sql + " AND COD_TERCERO=-1";
            
            log.debug(sql);

            rs = st.executeQuery(sql);
            String entrar = "no";
            while(rs.next()){
                entrar = "si";                
                String valorCampo = rs.getString("VALOR");
                if (valorCampo.endsWith(",00")) valorCampo = valorCampo.substring(0, valorCampo.length()-3);
                campos.put(codCampo,valorCampo);
                cF = new CamposFormulario(campos);
            }

            if("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        }catch (Exception e){
            cF = null;
            e.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
        }
        finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return cF;
    }



    /**
     * Recupera los nombre de los ficheros de un campo suplementario de este tiop asociado a un determinado tercero
     * @param codMunicipio: Código del municipio
     * @param codTercero: Código del tercero
     * @param con: Conexión a la BD
     * @return CamposFormulario
     */
     public CamposFormulario getValoresFichero(String codMunicipio,String codTercero,Connection con){
         PreparedStatement st = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try{
                      
            String sql = "SELECT COD_CAMPO,NOMBRE_FICHERO FROM T_CAMPOS_FICHERO " +
                         "WHERE COD_MUNICIPIO=? AND COD_TERCERO=?";

            String cod="0";
            if(isInteger(codTercero)){
                cod=codTercero;
            }else
                cod="-1";

            log.debug(sql);
            st = con.prepareStatement(sql);
            
            st.setInt(1, Integer.parseInt(codMunicipio));
            st.setInt(2, Integer.parseInt(cod));
            
            rs = st.executeQuery();
            
            String entrar = "no";
            while(rs.next()){
                entrar = "si";
                String codCampo = rs.getString("COD_CAMPO");
                String valorCampo = rs.getString("NOMBRE_FICHERO");
                campos.put(codCampo,valorCampo);
                cF = new CamposFormulario(campos);
            }
            if("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        }catch (Exception e){
            cF = null;
            e.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
        }
        finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return cF;
    }
     
     
      public CamposFormulario getValoresFichero(String codMunicipio,String codTercero,String codCampo,Connection con){
        Statement st = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try{
            st = con.createStatement();
           
            String sql = "SELECT NOMBRE_FICHERO FROM T_CAMPOS_FICHERO " +
                         " WHERE COD_MUNICIPIO=" + codMunicipio + " AND COD_CAMPO='" + codCampo + "'";

            if(isInteger(codTercero)){
                sql = sql + " AND COD_TERCERO=" + codTercero;
            }else
                sql = sql + " AND COD_TERCERO=-1";

            log.debug(sql);
            rs = st.executeQuery(sql);
            String entrar = "no";
            while(rs.next()){
                entrar = "si";                
                String valorCampo = rs.getString("NOMBRE_FICHERO");
                campos.put(codCampo,valorCampo);
                cF = new CamposFormulario(campos);
            }
            if("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        }catch (Exception e){
            cF = null;
            e.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
        }
        finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return cF;
    } 


     /**
      * Recupera los valores de los campos de tipo texto largo para un determinado tercero
      * @param codMunicipio: Código del municipio
      * @param codTercero: Código del tercero
      * @param con: Conexión a la BBDD
      * @return CamposFormulario
      */
     public CamposFormulario getValoresTextoLargo(String codMunicipio,String codTercero,Connection con){
        PreparedStatement st = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = new CamposFormulario(campos);

        try{
           
            String sql = "SELECT COD_CAMPO,VALOR FROM T_CAMPOS_TEXTO_LARGO " +
                         "WHERE COD_MUNICIPIO=? AND COD_TERCERO=?";

             String cod="0";
            if(isInteger(codTercero)){
                cod=codTercero;
            }else
                cod="-1";
            
            log.debug(sql);            
            st = con.prepareStatement(sql);
            
            st.setInt(1, Integer.parseInt(codMunicipio));
            st.setInt(2, Integer.parseInt(cod));
            
            rs = st.executeQuery();
            String codCampo = null;
            String valorCampo = null;
            while(rs.next()){

                valorCampo = new String("");
                codCampo = rs.getString("COD_CAMPO");
                java.io.Reader cr = rs.getCharacterStream("VALOR");

                if(cr==null){
                    valorCampo = null;
                }
                else{

                    java.io.CharArrayWriter ot = new java.io.CharArrayWriter();
                    int c;
                    while ((c = cr.read())!= -1){
                        ot.write(c);
                    }
                    ot.flush();
                    valorCampo = ot.toString();
                    ot.close();
                    cr.close();
                }

                if("".equals(codCampo) || "".equals(valorCampo)){
                    cF = new CamposFormulario(campos);
                }else{
                    campos.put(codCampo,valorCampo);
                    cF = new CamposFormulario(campos);
                }
            }

        }catch (Exception e){
            cF = null;
            e.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return cF;
    }
     
     
     public CamposFormulario getValoresTextoLargo(String codMunicipio,String codTercero,String codCampo,Connection con){
        Statement st = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = new CamposFormulario(campos);

        try{
           
            String sql = "SELECT VALOR FROM T_CAMPOS_TEXTO_LARGO " +
                         "WHERE COD_MUNICIPIO=" + codMunicipio + " AND COD_CAMPO='" + codCampo + "'";

            if(isInteger(codTercero)){
                sql = sql + " AND COD_TERCERO=" + codTercero;
            }else
                sql = sql + " AND COD_TERCERO=-1";
            
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);            
            String valorCampo = null;
            while(rs.next()){

                valorCampo = new String("");                
                java.io.Reader cr = rs.getCharacterStream("VALOR");

                if(cr==null){
                    valorCampo = null;
                }
                else{

                    java.io.CharArrayWriter ot = new java.io.CharArrayWriter();
                    int c;
                    while ((c = cr.read())!= -1){
                        ot.write(c);
                    }
                    ot.flush();
                    valorCampo = ot.toString();
                    ot.close();
                    cr.close();
                }

                if("".equals(codCampo) || "".equals(valorCampo)){
                    cF = new CamposFormulario(campos);
                }else{
                    campos.put(codCampo,valorCampo);
                    cF = new CamposFormulario(campos);
                }
            }

        }catch (Exception e){
            cF = null;
            e.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return cF;
    } 



     /**
      * Recupera los valores de los campos de tipo fecha de un tercero
      * @param codMunicipio: Código del municipio
      * @param codTercero: Código del tercero
      * @param con: Conexión a la BD
      * @param oad: AdaptadorSQLBD
      * @param mascara: Máscara de la fecha
      * @return CamposFormulario
      */
     public CamposFormulario getValoresFecha(String codMunicipio,String codTercero,Connection con,AdaptadorSQLBD oad,String mascara){
        PreparedStatement st = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;
        String sql = "";

        try{           

            if(mascara == null || "".equals(mascara)) {
              
                sql = "SELECT COD_CAMPO," + oad.convertir("VALOR", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                        " AS VALOR" +
                        " FROM T_CAMPOS_FECHA WHERE COD_MUNICIPIO=? AND COD_TERCERO=?";

              

            } else {
                
                sql = "SELECT COD_CAMPO," + oad.convertir("VALOR", oad.CONVERTIR_COLUMNA_TEXTO, mascara) + " AS VALOR" +
                       " FROM T_CAMPOS_FECHA WHERE COD_MUNICIPIO=? AND COD_TERCERO=?";               
            }
            
            String cod="0";
            if(isInteger(codTercero)){
                cod=codTercero;
            }else
                cod="-1";

            log.debug(sql);
            st = con.prepareStatement(sql);
            
            st.setInt(1, Integer.parseInt(codMunicipio));
            st.setInt(2, Integer.parseInt(cod));
            
            rs = st.executeQuery();
            String entrar = "no";
            while(rs.next()){
                entrar = "si";
                String codCampo = rs.getString("COD_CAMPO");
                String valorCampo = rs.getString("VALOR");
                campos.put(codCampo,valorCampo);
                cF = new CamposFormulario(campos);
            }

            if("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        }catch (Exception e){
            cF = null;
            e.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return cF;
    }

     
     
   public CamposFormulario getValoresFecha(String codMunicipio,String codTercero,String codCampo,Connection con,AdaptadorSQLBD oad,String mascara){
        Statement st = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;
        String sql = "";

        try{
            st = con.createStatement();

            if(mascara == null || "".equals(mascara)) {
              
                sql = "SELECT " + oad.convertir("VALOR", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                        " AS VALOR" +
                        " FROM T_CAMPOS_FECHA WHERE COD_MUNICIPIO=" + codMunicipio + " AND COD_CAMPO='" + codCampo + "'";

               if(isInteger(codTercero)){
                    sql = sql + " AND COD_TERCERO=" + codTercero;
               }else
                    sql = sql + " AND COD_TERCERO=-1";

            } else {
                
                sql = "SELECT " + oad.convertir("VALOR", oad.CONVERTIR_COLUMNA_TEXTO, mascara) + " AS VALOR" +
                      " FROM T_CAMPOS_FECHA WHERE COD_MUNICIPIO=" + codMunicipio + " AND COD_CAMPO='" + codCampo + "'";

                if(isInteger(codTercero)){
                    sql = sql + " AND COD_TERCERO=" + codTercero;
                }else
                    sql = sql + " AND COD_TERCERO=-1";

            }

            log.debug(sql);
            rs = st.executeQuery(sql);
            String entrar = "no";
            while(rs.next()){
                entrar = "si";                
                String valorCampo = rs.getString("VALOR");
                campos.put(codCampo,valorCampo);
                cF = new CamposFormulario(campos);
            }

            if("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        }catch (Exception e){
            cF = null;
            e.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return cF;
    }
  
     
     

     /**
      * Recupera los valores de los campos de tipo desplegable de un tercero
      * @param codMunicipio: Código del municipio
      * @param codTercero: Código del tercero
      * @param con: Conexión a la BD
      * @return CamposFormulario
      */
    public CamposFormulario getValoresDesplegable(String codMunicipio,String codTercero,Connection con){
      PreparedStatement st = null;
      ResultSet rs = null;
      HashMap campos = new HashMap();
      CamposFormulario cF = null;

      try{ 
        

        String sql = "SELECT COD_CAMPO,VALOR FROM T_CAMPOS_DESPLEGABLE " +
                   "WHERE COD_MUNICIPIO=? AND COD_TERCERO=?";
      
        
        log.debug(sql);

        String cod="0";
        if(isInteger(codTercero)){
            cod=codTercero;
        }else
            cod="-1";

        log.debug(sql);
        st = con.prepareStatement(sql);

        st.setInt(1, Integer.parseInt(codMunicipio));
        st.setInt(2, Integer.parseInt(cod));

        rs = st.executeQuery();
            
        String entrar = "no";
        while(rs.next()){
            entrar = "si";
            String codCampo = rs.getString("COD_CAMPO");
            String valorCampo = rs.getString("VALOR");
            campos.put(codCampo,valorCampo);
            cF = new CamposFormulario(campos);
        }

        if("no".equals(entrar)) {
            cF = new CamposFormulario(campos);
        }
        
    }catch (Exception e){
      cF = null;
      e.printStackTrace();
      log.error("Excepcion capturada en: " + getClass().getName());
    }finally{

        try{
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeResultSet(rs);
        }catch(Exception ex){
            ex.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
        }
    }

    return cF;
  }
    
    
   public CamposFormulario getValoresDesplegable(String codMunicipio,String codTercero,String codCampo,Connection con){
      Statement st = null;
      ResultSet rs = null;
      HashMap campos = new HashMap();
      CamposFormulario cF = null;

      try{
        st = con.createStatement();

        String sql = "SELECT VALOR FROM T_CAMPOS_DESPLEGABLE " +
                   "WHERE COD_MUNICIPIO=" + codMunicipio + " AND COD_CAMPO='" + codCampo + "'";

        if(isInteger(codTercero)){
            sql = sql + " AND COD_TERCERO=" + codTercero;
        }else
            sql = sql + " AND COD_TERCERO=-1";
        
        log.debug(sql);

        rs = st.executeQuery(sql);
        String entrar = "no";
        while(rs.next()){
            entrar = "si";            
            String valorCampo = rs.getString("VALOR");
            campos.put(codCampo,valorCampo);
            cF = new CamposFormulario(campos);
        }

        if("no".equals(entrar)) {
            cF = new CamposFormulario(campos);
        }
        
    }catch (Exception e){
      cF = null;
      e.printStackTrace();
      log.error("Excepcion capturada en: " + getClass().getName());
    }finally{

        try{
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.closeResultSet(rs);
        }catch(Exception ex){
            ex.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
        }
    }

    return cF;
  }  



  public CamposFormulario getValoresTexto(String codMunicipio,String codTercero,Connection con){
       PreparedStatement st = null;
        ResultSet rs = null;
        CamposFormulario cF = null;

        try{
            
           
            String sql = "SELECT COD_CAMPO,VALOR FROM T_CAMPOS_TEXTO " +
                         "WHERE COD_MUNICIPIO=? AND COD_TERCERO=?";

            String cod="0";
            if(isInteger(codTercero)){
                cod=codTercero;
            }else
                cod="-1";

            log.debug(sql);
            st = con.prepareStatement(sql);
            
            st.setInt(1, Integer.parseInt(codMunicipio));
            st.setInt(2, Integer.parseInt(cod));
            
            rs = st.executeQuery();
            String entrar = "no";

            HashMap<String, String> campos = new HashMap<String, String>();
            while(rs.next()){
                entrar = "si";
                String codCampo = rs.getString("COD_CAMPO");
                String valorCampo = rs.getString("VALOR");
                campos.put(codCampo,valorCampo);
                cF = new CamposFormulario(campos);
            }

            if("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
            
        }catch (Exception e){
            cF = null;
            e.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }

        return cF;
    }
  
  
   public CamposFormulario getValoresTexto(String codMunicipio,String codTercero,String codCampo,Connection con){
        Statement st = null;
        ResultSet rs = null;
        CamposFormulario cF = null;

        try{
            st = con.createStatement();
           
            String sql = "SELECT VALOR FROM T_CAMPOS_TEXTO " +
                         "WHERE COD_MUNICIPIO=" + codMunicipio + " AND COD_CAMPO='" + codCampo + "'";

            if(isInteger(codTercero)){
                sql = sql + " AND COD_TERCERO=" + codTercero;
            }else
                sql = sql + " AND COD_TERCERO=-1";

            log.debug(sql);
            rs = st.executeQuery(sql);
            String entrar = "no";

            HashMap<String, String> campos = new HashMap<String, String>();
            while(rs.next()){
                entrar = "si";                
                String valorCampo = rs.getString("VALOR");
                campos.put(codCampo,valorCampo);
                cF = new CamposFormulario(campos);
            }

            if("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
            
        }catch (Exception e){
            cF = null;
            e.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }

        return cF;
    }



    /**
     * Recupera los valores de los datos suplementarios de un tercero
     * @param codOrganizacion: código de la organización
     * @param codTercero: Código del tercero
     * @param eCs: Estructura de los campos suplementarios de un tercero
     * @param con: Conexión a la BD
     * @param params: Parámetros de conexión a la BD
     * @return Vector<CamposFormulario>
     * @throws es.altia.common.exception.TechnicalException
     */
    public Vector cargaValoresDatosSuplementarios(String codOrganizacion,String codTercero,Vector eCs, Connection con,String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;        
        Vector lista = new Vector();
        CamposFormulario cF = null;

        try{
            oad = new AdaptadorSQLBD(params);
            
            for(int i=0;eCs!=null && i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String mascara = eC.getMascara();
                                
                if("1".equals(codTipoDato)) {                
                    cF = this.getValoresNumericos(codOrganizacion,codTercero, con, oad);
                }
                else if("2".equals(codTipoDato)) {

                    cF = this.getValoresTexto(codOrganizacion, codTercero, con);                    
                }
                else if("3".equals(codTipoDato)) {
                    cF = this.getValoresFecha(codOrganizacion,codTercero,con,oad,mascara);
                    
                } else if("4".equals(codTipoDato)) {                                        
                    cF = this.getValoresTextoLargo(codOrganizacion,codTercero, con);
                    
                } else if("5".equals(codTipoDato)) {
                    cF = this.getValoresFichero(codOrganizacion,codTercero, con);                    
                    
                } else if("6".equals(codTipoDato)) {                                        
                    cF = this.getValoresDesplegable(codOrganizacion,codTercero, con);                    
                }
                lista.addElement(cF);
            }

        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(),e);
        }        
        return lista;
    }
    
    
    
    
    public Vector cargaValoresDatosSuplementarios(String codOrganizacion,String codTercero,Vector eCs, Connection con,AdaptadorSQLBD oad) throws TechnicalException {          
        Vector lista = new Vector();
        CamposFormulario cF = null;

        try{
                        
            for(int i=0;eCs!=null && i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String mascara = eC.getMascara();
                                
                if("1".equals(codTipoDato)) {                
                    cF = this.getValoresNumericos(codOrganizacion,codTercero, con, oad);
                }
                else if("2".equals(codTipoDato)) {

                    cF = this.getValoresTexto(codOrganizacion, codTercero, con);                    
                }
                else if("3".equals(codTipoDato)) {
                    cF = this.getValoresFecha(codOrganizacion,codTercero,con,oad,mascara);
                    
                } else if("4".equals(codTipoDato)) {                                        
                    cF = this.getValoresTextoLargo(codOrganizacion,codTercero, con);
                    
                } else if("5".equals(codTipoDato)) {
                    cF = this.getValoresFichero(codOrganizacion,codTercero, con);                    
                    
                } else if("6".equals(codTipoDato)) {                                        
                    cF = this.getValoresDesplegable(codOrganizacion,codTercero, con);                    
                }
                lista.addElement(cF);
            }

        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(),e);
        }        
        return lista;
    }
    
    
    /**
     * Recupera los valores de los campos suplementarios que se corresponden con la estructura del parámetro eCs
     * @param codOrganizacion: Código de la organización
     * @param codTercero: Código del tercero
     * @param eCs: Vector con la estructura de los campos suplementarios
     * @param con: Conexión a la BBDD
     * @param params: Parámetros de conexión a la BBDD
     * @return Vector con los valores
     * @throws TechnicalException si ocurre algún error
     */
      public Vector cargaValoresDatosSuplementariosConCodigo(String codOrganizacion,String codTercero,Vector eCs, Connection con,String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;        
        Vector lista = new Vector();
        CamposFormulario cF = null;

        try{
            oad = new AdaptadorSQLBD(params);
            
            for(int i=0;eCs!=null && i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String mascara = eC.getMascara();
                String codCampo = eC.getCodCampo();
                                
                if("1".equals(codTipoDato)) {                
                    cF = this.getValoresNumericos(codOrganizacion,codTercero, codCampo,con, oad);
                }
                else if("2".equals(codTipoDato)) {

                    cF = this.getValoresTexto(codOrganizacion, codTercero, codCampo,con);                    
                }
                else if("3".equals(codTipoDato)) {
                    cF = this.getValoresFecha(codOrganizacion,codTercero,codCampo,con,oad,mascara);
                    
                } else if("4".equals(codTipoDato)) {                                        
                    cF = this.getValoresTextoLargo(codOrganizacion,codTercero,codCampo, con);
                    
                } else if("5".equals(codTipoDato)) {
                    cF = this.getValoresFichero(codOrganizacion,codTercero,codCampo, con);                    
                    
                } else if("6".equals(codTipoDato)) {                                        
                    cF = this.getValoresDesplegable(codOrganizacion,codTercero,codCampo, con);                    
                }
                lista.addElement(cF);
            }

        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(),e);
        }        
        return lista;
    }



    /**
     * Graba los datos suplementarios de un tercero
     * @param estructuraDatosSuplementarios: Estructrua de los campos suplementarios de tercero
     * @param valoresDatosSuplementarios: Valores de los campos suplementarios de un tercero
     * @param oad: AdaptadorSQLBD
     * @param con: Conexión a la BD
     * @return int
     * @throws es.altia.common.exception.TechnicalException
     */
     public int grabarDatosSuplementarios(String codOrganizacion,String codTercero,Vector estructuraDatosSuplementarios,Vector valoresDatosSuplementarios, AdaptadorSQLBD oad, Connection con) throws TechnicalException {

    	int res = 0;
        log.debug(this.getClass().getName() + ".grabarDatosSuplementarios ===================>");

    	try {
            log.debug(this.getClass().getName() + ".grabarDatosSuplementarios ==============>");
            if(estructuraDatosSuplementarios!=null){
                for(int i=0;i<estructuraDatosSuplementarios.size();i++) {
                    log.debug("I ... "+i);
                    res = 0;
                    EstructuraCampo eC = new EstructuraCampo();
                    eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                    log.debug("CODIGO ... "+eC.getCodCampo());
                    log.debug("NOMBRE ... "+eC.getDescCampo());
                    log.debug("TIPO DATO ... "+eC.getCodTipoDato());

                    String codTipoDato = eC.getCodTipoDato();
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO = (GeneralValueObject) valoresDatosSuplementarios.elementAt(i);
                    if("1".equals(codTipoDato)) {                    
                        res = setDatoNumerico(codOrganizacion,codTercero,eC,gVO,oad,con);
                        if(res<1) break;

                    } else if("2".equals(codTipoDato)) {                    
                        res = setDatoTexto(codOrganizacion,codTercero,eC,gVO,con);
                        if(res<1) break;

                    } else if("3".equals(codTipoDato)) {                    
                        res = setDatoFecha(codOrganizacion,codTercero,eC,gVO,oad,con);
                        if(res<1) break;

                    } else if("4".equals(codTipoDato)) {                    
                        res = setDatoTextoLargo(codOrganizacion,codTercero,eC,gVO,con);
                        if(res<1) break;

                    } else if("5".equals(codTipoDato)) {                    
                        log.debug(".....ha llegado el fichero");
                        res = setDatoFichero(codOrganizacion,codTercero,eC,gVO,con);
                        log.debug(".....ha pasado el fichero");
                        if(res<1) break;

                    } else if("6".equals(codTipoDato)) {                    
                        log.debug(".....ha llegado el fichero");
                        res = setDatoDesplegable(codOrganizacion,codTercero,eC,gVO,con);
                        log.debug(".....ha pasado el fichero");
                        if(res<1) break;
                    }
                }
            }

            log.debug("el resultado de grabar los valores de datos suplementarios de tercero es : " + res);
        } catch (TechnicalException e){
            e.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
            throw e;            
        }

        log.debug(this.getClass().getName() + ".grabarDatosSuplementarios <==============");
        return res;
    }



     /**
      * Graba un valor de un campo suplementario de tipo numérico para un determinado tercero
      * @param codMunicipio: Código del municipio
      * @param codTercero: Código del tercero
      * @param eC: EstructuraCampo con la estructura del campo suplementario
      * @param gVO: GeneralValueObject con los valores del campo suplementario a insertar
      * @param oad: AdaptadorSQLBD
      * @param con: Conexión a la BD
      * @return un int que si vale 1 indica que se ha dado de alta el valor del campo suplementario
      */
    public int setDatoNumerico(String codMunicipio,String codTercero,EstructuraCampo eC, GeneralValueObject gVO,AdaptadorSQL oad, Connection con) throws TechnicalException{
        Statement st = null;
        String sql = null;
        int resultadoInsertar = 0;       

        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());

        try {
          
            sql = "DELETE FROM T_CAMPOS_NUMERICO WHERE COD_MUNICIPIO=" + codMunicipio + " COD_TERCERO=" + codTercero +
                  " AND COD_CAMPO='"+ codDato + "'";

            log.debug(sql);
            st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
            st = null;
            st = con.createStatement();
            if(valorDato != null && !"".equals(valorDato)) {
          
                sql = "INSERT INTO T_CAMPOS_NUMERICO (COD_MUNICIPIO,COD_TERCERO,COD_CAMPO,VALOR) " +
                       "VALUES (" + codMunicipio + "," + codTercero + ",'" + codDato + "'," +
                        oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_NUMERO, "99999999D99") + ")";
                
                log.debug(sql);
                resultadoInsertar = st.executeUpdate(sql);
            } else {
                resultadoInsertar = 1;
            }

        } catch (Exception e) {
            resultadoInsertar =0;
            e.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
            throw new TechnicalException("Error al almacenar el valor de un campo de tipo numérico para un tercero",e);
        } finally {
            try {
                if (st!=null) {
                    st.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }


     /**
      * Da de alta el valor de un campo suplementario de tipo text largo para un determinado tercero
      * @param codMunicipio: Código del municipio
      * @param codTercero: Código del tercero
      * @param eC: EstructuraCampo con la estructura del campo suplementario
      * @param gVO: GeneralValueObject con los valores del campo suplementario a insertar      
      * @param con: Conexión a la BD
      * @return un int que si vale 1 indica que se ha dado de alta el valor del campo suplementario
      */
    public int setDatoTextoLargo(String codMunicipio,String codTercero,EstructuraCampo eC, GeneralValueObject gVO, Connection con) throws TechnicalException{

        PreparedStatement pstmt = null;
        String sql = null;
        int resultadoInsertar = 0;        
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());

        try {
        
            sql = "DELETE FROM T_CAMPOS_TEXTO_LARGO WHERE COD_MUNICIPIO=" + codMunicipio + " AND COD_TERCERO=" +
                   codTercero + " AND COD_CAMPO='" + codDato + "'";
            log.debug(sql);

            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();

            if(valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO T_CAMPOS_TEXTO_LARGO (COD_CAMPO,COD_MUNICIPIO,COD_TERCERO,VALOR) " +
                       "VALUES('" + codDato + "'," + codMunicipio + "," + codTercero + ",?)";
                log.debug(sql);

                pstmt.close();
                pstmt = con.prepareStatement(sql);
                java.io.StringReader cr = new java.io.StringReader(valorDato);
                pstmt.setCharacterStream(1,cr,valorDato.length());
                resultadoInsertar = pstmt.executeUpdate();
                cr.close();
            } else {
                resultadoInsertar = 1;
            }

        } catch (Exception e) {
            resultadoInsertar =0;            
            log.error("Excepcion capturada en: " + getClass().getName());
            throw new TechnicalException("Error al almacenar el valor de un campo de tipo texto largo para un tercero",e);
        } finally {
            try {
                if (pstmt!=null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }



    /**
      * Da de alta el valor de un campo suplementario de tipo desplegable para un determinado tercero
      * @param codMunicipio: Código del municipio
      * @param codTercero: Código del tercero
      * @param eC: EstructuraCampo con la estructura del campo suplementario
      * @param gVO: GeneralValueObject con los valores del campo suplementario a insertar      
      * @param con: Conexión a la BD
      * @return un int que si vale 1 indica que se ha dado de alta el valor del campo suplementario
      */
    public int setDatoDesplegable(String codMunicipio,String codTercero,EstructuraCampo eC, GeneralValueObject gVO,Connection con) throws TechnicalException{

        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;        
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());

        try {
            
            sql = "DELETE FROM T_CAMPOS_DESPLEGABLE WHERE COD_MUNICIPIO=" + codMunicipio + " AND COD_TERCERO=" + codTercero + " AND COD_CAMPO='" + codDato + "'";
            log.debug(sql);
            stmt = con.createStatement();
            stmt.executeUpdate(sql);

            if(valorDato != null && !"".equals(valorDato)) {

                sql = "INSERT INTO T_CAMPOS_DESPLEGABLE(COD_CAMPO,COD_MUNICIPIO,COD_TERCERO,VALOR) VALUES" +
                       "('" + codDato + "'," + codMunicipio + "," + codTercero + ",'" + valorDato + "')";
                log.debug(sql);
                resultadoInsertar = stmt.executeUpdate(sql);

          } else {
            resultadoInsertar = 1;
          }

        } catch (Exception e) {
            resultadoInsertar =0;
            e.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
            throw new TechnicalException("Error al almacenar el valor de un campo de tipo desplegable para un tercero",e);
        }

      return resultadoInsertar;
    }



    /**
      * Da de alta el valor de un campo suplementario de tipo fecha para un determinado tercero
      * @param codMunicipio: Código del municipio
      * @param codTercero: Código del tercero*
      * @param eC: EstructuraCampo con la estructura del campo suplementario
      * @param gVO: GeneralValueObject con los valores del campo suplementario a insertar
      * @param oad: AdaptadorSQLBD
      * @param con: Conexión a la BD
      * @return un int que si vale 1 indica que se ha dado de alta el valor del campo suplementario
      */
    public int setDatoFecha(String codMunicipio,String codTercero,EstructuraCampo eC, GeneralValueObject gVO,AdaptadorSQL oad, Connection con) throws TechnicalException{

        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 0;
        String codDato = eC.getCodCampo();
        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());
        String mascara = eC.getMascara();

        try {

            sql = "DELETE FROM T_CAMPOS_FECHA WHERE COD_CAMPO='" + codDato + "' AND COD_MUNICIPIO=" + codMunicipio + " AND COD_TERCERO=" + codTercero;
            log.debug(sql);

            stmt = con.createStatement();
            stmt.executeUpdate(sql);

            log.debug(" ====================> setDatoFecha codMunicipio: " + codMunicipio + ",codTercero : " + codTercero  + ",codDato: " + codDato + ",valorDato: " + valorDato);
            if(valorDato != null && !"".equals(valorDato) && !"null".equalsIgnoreCase(valorDato)) {
                if(mascara == null || "".equals(mascara)) {

                    sql = "INSERT INTO T_CAMPOS_FECHA (COD_CAMPO,COD_MUNICIPIO,COD_TERCERO,VALOR) " +
                           "VALUES('" + codDato + "'," + codMunicipio + "," + codTercero + "," + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY") + ")";

                } else {
                    
                    sql = "INSERT INTO T_CAMPOS_FECHA (COD_CAMPO,COD_MUNICIPIO,COD_TERCERO,VALOR) " +
                           "VALUES('" + codDato + "'," + codMunicipio + "," + codTercero + "," + oad.convertir("'" + valorDato + "'", oad.CONVERTIR_COLUMNA_FECHA, mascara) + ")";

                }

                log.debug(sql);
                resultadoInsertar = stmt.executeUpdate(sql);
            } else {
                resultadoInsertar = 1;
            }
        } catch (Exception e) {
            resultadoInsertar =0;            
            log.error("Excepcion capturada en: " + getClass().getName());
            throw new TechnicalException("Error al almacenar el valor de un campo de tipo fecha para un tercero",e);
        } finally {
            try {
                if (stmt!=null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }




    /**
      * Da de alta el valor de un campo suplementario de tipo desplegable para un determinado tercero
      * @param codMunicipio: Código del municipio
      * @param codTercero: Código del tercero
      * @param eC: EstructuraCampo con la estructura del campo suplementario
      * @param gVO: GeneralValueObject con los valores del campo suplementario a insertar      
      * @param con: Conexión a la BD
      * @return un int que si vale 1 indica que se ha dado de alta el valor del campo suplementario
      */
    public int setDatoFichero(String codMunicipio,String codTercero,EstructuraCampo eC, GeneralValueObject gVO, Connection con) throws TechnicalException{

        Statement stmt = null;
        String sql = null;
        int resultadoInsertar = 1;
        byte[] valorDato=null;
        String mime="";
        String nombre="";        
        String codDato = eC.getCodCampo();

        if (!gVO.getAtributo(eC.getCodCampo()).equals("")) {
            valorDato = (byte[])gVO.getAtributo(eC.getCodCampo());
            nombre = (String)gVO.getAtributo(eC.getCodCampo()+"_NOMBRE");
            mime = (String)gVO.getAtributo(eC.getCodCampo()+"_TIPO");
            log.debug("NOMBRE FICHERO ....."+nombre);
        }
        
        try {

            sql = "DELETE FROM T_CAMPOS_FICHERO WHERE COD_CAMPO='" + codDato + "' AND COD_MUNICIPIO=" + codMunicipio + " AND COD_TERCERO=" + codTercero;
            log.debug(sql);
            
            stmt = con.createStatement();
            stmt.executeUpdate(sql);

            if (valorDato!=null){
                if (!nombre.equals("")) {

                    sql = "INSERT INTO T_CAMPOS_FICHERO(COD_CAMPO,COD_MUNICIPIO,COD_TERCERO,TIPO_MIME,NOMBRE_FICHERO,VALOR) " +
                          "VALUES('" + codDato + "'," + codMunicipio + "," + codTercero + ",'" + mime + "','" + nombre + "',?)";
                    log.debug(sql);
                    stmt.close();
                    PreparedStatement ps = con.prepareStatement(sql);
                    log.debug(valorDato);

                    InputStream st = new ByteArrayInputStream(valorDato);
                    ps.setBinaryStream(1,st,valorDato.length);
                    resultadoInsertar =ps.executeUpdate();
                    ps.close();
                } else {
                    resultadoInsertar = 1;
                }

            }
        } catch (Exception e) {
            resultadoInsertar =0;            
            log.error("Excepcion capturada en: " + getClass().getName());
            throw new TechnicalException("Error al almacenar el valor de un campo de tipo fichero para un tercero",e);

        } finally {
            try {
                if (stmt!=null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoInsertar;
    }



    /**
      * @param codMunicipio: Código del municipio
      * @param codTercero: Código del tercero
      * Da de alta el valor de un campo suplementario de tipo texto para un determinado tercero
      * @param eC: EstructuraCampo con la estructura del campo suplementario
      * @param gVO: GeneralValueObject con los valores del campo suplementario a insertar      
      * @param con: Conexión a la BD
      * @return un int que si vale 1 indica que se ha dado de alta el valor del campo suplementario
      */
    public int setDatoTexto(String codMunicipio,String codTercero,EstructuraCampo eC, GeneralValueObject gVO,Connection con) throws TechnicalException{

        PreparedStatement ps = null;
        Statement st = null;
        int resultadoInsertar = 0;        
        String codDato = eC.getCodCampo();

        String valorDato = (String) gVO.getAtributo(eC.getCodCampo());

        try {

            String sql = "DELETE FROM T_CAMPOS_TEXTO WHERE COD_CAMPO='" + codDato + "' AND COD_MUNICIPIO=" + codMunicipio + " AND COD_TERCERO=" + codTercero;
            log.debug(sql);
            st = con.createStatement();
            st.executeUpdate(sql);
            st.close();

            if(valorDato != null && !"".equals(valorDato)) {
                sql = "INSERT INTO T_CAMPOS_TEXTO(COD_CAMPO,COD_MUNICIPIO,COD_TERCERO,VALOR) VALUES (?, ?, ?, ?)";
                log.debug(sql);

                ps = con.prepareStatement(sql);

                // Asignar los valores de la consulta al preparedStatement.
                int i = 1;
                ps.setString(i++,codDato);
                ps.setInt(i++, Integer.parseInt(codMunicipio));
                ps.setInt(i++, Integer.parseInt(codTercero));
                ps.setString(i++, valorDato);                
                resultadoInsertar = ps.executeUpdate();

            } else {
                resultadoInsertar = 1;
            }

        } catch (Exception e) {
            resultadoInsertar =0;            
            log.error("Excepcion capturada en: " + getClass().getName());
            throw new TechnicalException("Error al almacenar el valor de un campo de tipo texto para un tercero",e);
        } finally {
            try {
                if(st!=null) st.close();
                if(ps!=null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return resultadoInsertar;
    }




   /**
     * Recupera el contenido de un determinado campo suplementario de tipo fichero
     * @param codMunicipio: Código del municipio
     * @param codCampo: Código del campo
     * @param codTercero: Código del tercero
     * @param con: Conexión a la BD
     * @return Hashtable<String,Object>
     */
     public Hashtable<String,Object> getContenidoCampoFichero(String codMunicipio,String codCampo,String codTercero,Connection con){
        Hashtable<String,Object> salida = new Hashtable<String, Object>();
        Statement stmt = null;
        ResultSet rs = null;
        
        try{
            stmt = con.createStatement();

            String sql = "SELECT NOMBRE_FICHERO,VALOR,TIPO_MIME FROM T_CAMPOS_FICHERO " +
                         "WHERE COD_MUNICIPIO=" + codMunicipio + " AND COD_CAMPO='" + codCampo + "' AND COD_TERCERO=" +  codTercero;
            log.debug(sql);

            rs = stmt.executeQuery(sql);
            byte[] contenido = null;

            while(rs.next()){                
                java.io.InputStream st = rs.getBinaryStream("VALOR");                
                java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                int c;
                if (st != null) {
                     while ((c = st.read()) != -1) {
                         ot.write(c);
                     }

                    ot.flush();
                    contenido = ot.toByteArray();
                    ot.close();
                    st.close();
                }

                String nombre          = rs.getString("NOMBRE_FICHERO");
                String tipoMime        = rs.getString("TIPO_MIME");
                
                log.debug("Se ha recuperado un fichero de longitud: " + contenido.length);
                salida.put("NOMBRE_FICHERO",nombre);
                salida.put("TIPO_MIME",tipoMime);
                salida.put("CONTENIDO",contenido);
            }

        }catch (Exception e){            
            e.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
        }
        finally{
            try{
                SigpGeneralOperations.closeStatement(stmt);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        
        return salida;
    }

   /***************************/


   /**
      * Recupera el valor de un determinado campo suplementario de tipo fecha asociado un determinado
      * tercero
      * @param codMunicipio: Código del municipio
      * @param codTercero: Código del tercero
      * @param codCampo: Código del campo
      * @param con: Conexión a la BD
      * @return Calendar
      */
     public Calendar getFecha(String codMunicipio,String codTercero,String codCampo,Connection con){
        Calendar fecha = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";

        try{
            st = con.createStatement();

            sql = "SELECT VALOR " +
                  "FROM T_CAMPOS_FECHA " +
                  "WHERE COD_MUNICIPIO=" + codMunicipio + " AND COD_CAMPO='" + codCampo + "' " +
                  "AND COD_TERCERO=" + codTercero;
            log.debug(sql);          
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                java.sql.Timestamp tFecha = rs.getTimestamp("VALOR");
                if(tFecha!=null){
                    fecha = Calendar.getInstance();
                    fecha.clear();
                    fecha.setTimeInMillis(tFecha.getTime());
                }
            
            }

            
        }catch (Exception e){
            log.error("Excepcion capturada en: " + getClass().getName());
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return fecha;
    }



     /**
      * Recupera el valor de un determinado campo suplementario de tipo desplegable asociado un determinado
      * tercero
      * @param codMunicipio: Código del municipio
      * @param codTercero: Código del tercero
      * @param codCampo: Código del campo
      * @param con: Conexión a la BD
      * @return Calendar
      */
     public String getDesplegable(String codMunicipio,String codTercero,String codCampo,Connection con){
        String salida = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";

        try{
            st = con.createStatement();

            sql = "SELECT VALOR " +
                  "FROM T_CAMPOS_DESPLEGABLE " +
                  "WHERE COD_MUNICIPIO=" + codMunicipio + " AND COD_CAMPO='" + codCampo + "' " +
                  "AND COD_TERCERO=" + codTercero;
            log.debug(sql);

            rs = st.executeQuery(sql);
            while(rs.next()){
                salida = rs.getString("VALOR");                
            }


        }catch (Exception e){
            log.error("Excepcion capturada en: " + getClass().getName());
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return salida;
    }
     
     /**
      * 
      * @param codMunicipio
      * @param codCampo
      * @param codValor
      * @param con
      * @return
      * @throws Exception 
      */
     public String getValorDesplegable (String codMunicipio, String codCampo, String codValor, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValorDesplegable( codMunicipio = " + codMunicipio + " codCampo = " + codCampo 
                + " codValor = " + codValor + " ) : BEGIN"); 
        String valorDesplegable = "";
        if(esCampoDesplegable(codMunicipio, codCampo, con)){
            Statement st = null;
            ResultSet rs = null;
            String sql = "";
            try{
                st = con.createStatement();
                sql= "Select DES_NOM from T_CAMPOS_EXTRA campoExtra, E_DES_VAL valorDesplegable "
                    + "where campoextra.desplegable = valordesplegable.des_cod "
                    + "and campoextra.cod_campo = '" + codCampo + "' "
                    + "and valordesplegable.des_val_cod = '" + codValor + "'" ;
                
                rs = st.executeQuery(sql);
                while(rs.next()){
                    valorDesplegable = rs.getString("DES_NOM");
                }//while(rs.next())
            }catch (Exception e){
                log.error("Excepcion capturada en: " + getClass().getName());
            }finally{
                try{
                    SigpGeneralOperations.closeStatement(st);
                    SigpGeneralOperations.closeResultSet(rs);
                }catch(Exception ex){
                    ex.printStackTrace();
                    log.error("Excepcion capturada en: " + getClass().getName());
                }//try-catch
            }//try-catch
        }else{
            if(log.isDebugEnabled()) log.debug("No es un campo desplegable");
            throw new Exception();
        }//if(esCampoDesplegable(codMunicipio, codCampo, con))
        if(log.isDebugEnabled()) log.debug("getValorDesplegable() : END ");
        return valorDesplegable;
     }//getValorDesplegable
     
     /**
      * 
      * @param codMunicipio
      * @param codCampo
      * @param con
      * @return
      * @throws Exception 
      */
    private boolean esCampoDesplegable (String codMunicipio, String codCampo, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("esCampoDesplegable (codMunicipio = " + codMunicipio + " codCampo = " + codCampo 
                 + " ) : BEGIN" );
        Boolean esCampoDesplegable = false;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        try{
            st = con.createStatement();
            sql= "Select * from T_CAMPOS_EXTRA where COD_CAMPO = '" + codCampo + "' and TIPO_DATO = 6";
            rs = st.executeQuery(sql);
            while(rs.next()){
                esCampoDesplegable = true;
            }//while(rs.next())
        }catch (Exception e){
            log.error("Excepcion capturada en: " + getClass().getName());
            throw new Exception(e);
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
                throw new Exception(ex);
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("esCampoDesplegable() : END");
        return esCampoDesplegable;
     }//esCampoDesplegable

    //Métodos para recuperar los datos individuales de los campos suplementarios de tercero por codCampo, codTecero y codMunicipio.
    public String getValorDesplegableTabla (String codCampo, String codTercero, String codMunicipio, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValorDesplegableTabla() : BEGIN");
        String valor = "";
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        try{
            st = con.createStatement();
            sql = "Select VALOR from T_CAMPOS_DESPLEGABLE where COD_CAMPO = '" + codCampo + "' and COD_MUNICIPIO = '" + codMunicipio 
                    + "' and COD_TERCERO = '" + codTercero + "'";
            rs = st.executeQuery(sql);
            while(rs.next()){
                valor = rs.getString("VALOR");
            }//while(rs.next())
        }catch (Exception e){
            log.error("Excepcion capturada en: " + getClass().getName());
            throw new Exception(e);
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
                throw new Exception(ex);
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValorDesplegableTabla() : BEGIN");
        return valor;
    }//getValorDesplegableTabla
    
    public String getValorFecha (String codCampo, String codTercero, String codMunicipio, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValorFecha() : BEGIN");
        String valor = "";
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        try{
            st = con.createStatement();
            sql = "Select VALOR from T_CAMPOS_FECHA where COD_CAMPO = '" + codCampo + "' and COD_MUNICIPIO = '" + codMunicipio 
                    + "' and COD_TERCERO = '" + codTercero + "'";
            rs = st.executeQuery(sql);
            while(rs.next()){
                if(rs.getDate("VALOR") != null){
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(rs.getDate("VALOR"));
                    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
                    valor = formateador.format(cal.getTime());
                }//if(rs.getDate("VALOR") != null)
            }//while(rs.next())
        }catch (Exception e){
            log.error("Excepcion capturada en: " + getClass().getName());
            throw new Exception(e);
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
                throw new Exception(ex);
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValorFecha() : BEGIN");
        return valor;
    }//getValorFecha
        
    public String getValorTexto (String codCampo, String codTercero, String codMunicipio, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValorTexto() : BEGIN");
        String valor = "";
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        try{
            st = con.createStatement();
            sql = "Select VALOR from T_CAMPOS_TEXTO where COD_CAMPO = '" + codCampo + "' and COD_MUNICIPIO = '" + codMunicipio 
                    + "' and COD_TERCERO = '" + codTercero + "'";
            rs = st.executeQuery(sql);
            while(rs.next()){
                valor = rs.getString("VALOR");
            }//while(rs.next())
        }catch (Exception e){
            log.error("Excepcion capturada en: " + getClass().getName());
            throw new Exception(e);
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
                throw new Exception(ex);
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValorTexto() : BEGIN");
        return valor;
    }//getValorTexto
    
    public String getValorTextoLargo (String codCampo, String codTercero, String codMunicipio, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValorTextoLargo() : BEGIN");
        String valor = "";
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        try{
            st = con.createStatement();
            sql = "Select VALOR from T_CAMPOS_TEXTO_LARGO where COD_CAMPO = '" + codCampo + "' and COD_MUNICIPIO = '" + codMunicipio 
                    + "' and COD_TERCERO = '" + codTercero + "'";
            rs = st.executeQuery(sql);
            while(rs.next()){
                valor = rs.getString("VALOR");
            }//while(rs.next())
        }catch (Exception e){
            log.error("Excepcion capturada en: " + getClass().getName());
            throw new Exception(e);
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
                throw new Exception(ex);
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValorTextoLargo() : BEGIN");
        return valor;
    }//getValorTextoLargo
        
    public String getValorNumerico (String codCampo, String codTercero, String codMunicipio, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValorNumerico() : BEGIN");
        String valor = "";
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        try{
            st = con.createStatement();
            sql = "Select VALOR from T_CAMPOS_NUMERICO where COD_CAMPO = '" + codCampo + "' and COD_MUNICIPIO = '" + codMunicipio 
                    + "' and COD_TERCERO = '" + codTercero + "'";
            rs = st.executeQuery(sql);
            while(rs.next()){
                Integer valorNumerico = rs.getInt("VALOR");
                if(valorNumerico != null){
                    valor = String.valueOf(valorNumerico);
                }//if(valorNumerico != null)
            }//while(rs.next())
        }catch (Exception e){
            log.error("Excepcion capturada en: " + getClass().getName());
            throw new Exception(e);
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
                throw new Exception(ex);
            }//try-catch-finally
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValorNumerico() : BEGIN");
        return valor;
    }//getValorNumerico
    
    public Vector getValoresSuplementariosTerceroFromVector (Vector listaCampos, String codMunicipio, Connection con, AdaptadorSQL oad) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValoresSuplementariosTerceroFromVector() :  BEGIN");
        Vector<String> valoresSuplementariosTercero = new Vector<String>();
        Vector<EstructuraCampo> camposSuplementarios = new Vector<EstructuraCampo>();
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        try{
            camposSuplementarios = cargaEstructuraDatosSuplementariosTercero(codMunicipio, oad, con);
            if(camposSuplementarios.size()>0){
                if(log.isDebugEnabled()) log.debug("Existen campos suplementarios de terceros");
                if(listaCampos.size() > 0){
                    for(int i=0; i<listaCampos.size(); i++){
                        String nombreCampo = String.valueOf(listaCampos.get(i));
                        for(EstructuraCampo campoSuplementario : camposSuplementarios){
                            String nombreCampoSuplementario = campoSuplementario.getCodCampo();
                            if(nombreCampo.equalsIgnoreCase(nombreCampoSuplementario)){
                                valoresSuplementariosTercero.add(nombreCampo);
                                listaCampos.remove(i);
                                i-=1;
                                break;
                            }//if(nombreCampo.equalsIgnoreCase(nombreCampoSuplementario))
                        }//for(EstructuraCampo campoSuplementario : camposSuplementarios)
                    }//for(Object nombreCampo : listaCampos)
                }//if(listaCriterios.size() > 0)
            }//if(camposSuplementarios.size()>0)
        }catch (Exception e){
            log.error("Excepcion capturada en: " + getClass().getName());
            throw new Exception(e);
        }finally{
            try{
                SigpGeneralOperations.closeStatement(st);
                SigpGeneralOperations.closeResultSet(rs);
            }catch(Exception ex){
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
                throw new Exception(ex);
            }//try-catch-finally
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValoresSuplementariosTerceroFromVector() :  END");
        return valoresSuplementariosTercero;
    }//getValoresSuplementariosTerceroFromVector
    
    public String getValorCampoSuplementario(String codCampoSuplementario, String codMunicipio, String codTercero ,
            Connection con,  AdaptadorSQL oad) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValorCampoSuplementario() :  BEGIN");
        Vector<EstructuraCampo> camposSuplementarios = new Vector<EstructuraCampo>();
        String resultado = "";
        try{
            //Primero cargamos la estructura de datos suplementarios.
            camposSuplementarios = cargaEstructuraDatosSuplementariosTercero(codMunicipio, oad, con);
            if(camposSuplementarios.size()>0){
                if(log.isDebugEnabled()) log.debug("Existen campos suplementarios de terceros");
                    if(codCampoSuplementario != null && !codCampoSuplementario.equalsIgnoreCase("")){
                        for(EstructuraCampo campoSuplementario : camposSuplementarios){
                            String nombreCampoSuplementario = campoSuplementario.getCodCampo();
                            if(codCampoSuplementario.equalsIgnoreCase(nombreCampoSuplementario)){
                                String codTipoDato = campoSuplementario.getCodTipoDato();
                                if("1".equals(codTipoDato)) {                
                                    //Tipo Numerico
                                    resultado = getValorNumerico(codCampoSuplementario, codTercero, codMunicipio, con);
                                }else if("2".equals(codTipoDato)) {
                                    //Tipo texto
                                    resultado = getValorTexto(codCampoSuplementario, codTercero, codMunicipio, con);
                                }else if("3".equals(codTipoDato)) {
                                    //Tipo Fecha
                                    resultado = getValorFecha(codCampoSuplementario, codTercero, codMunicipio, con);
                                } else if("4".equals(codTipoDato)) {                                        
                                    //Tipo texto largo
                                    resultado = getValorTextoLargo(codCampoSuplementario, codTercero, codMunicipio, con);
                                } else if("5".equals(codTipoDato)) {
                                    //Tipo Fichero
                                } else if("6".equals(codTipoDato)) {                                        
                                    //Tipo Desplegable
                                    String valorAux = getValorDesplegableTabla(codCampoSuplementario, codTercero, codMunicipio, con);
                                    resultado = getValorDesplegable(codMunicipio, codCampoSuplementario, valorAux, con);
                                }//If tipos 
                                break;
                            }//if(codCampoSuplementario.equalsIgnoreCase(nombreCampoSuplementario))
                        }//for(EstructuraCampo campoSuplementario : camposSuplementarios)
                    }//if(codCampoSuplementario != null && !codCampoSuplementario.equalsIgnoreCase("")){
            }//if(camposSuplementarios.size()>0)
        }catch (Exception e){
            log.error("Excepcion capturada en: " + getClass().getName());
            throw new Exception(e);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValorCampoSuplementario() :  END");
        return resultado;
    }//getValorCampoSuplementario
    
}//class