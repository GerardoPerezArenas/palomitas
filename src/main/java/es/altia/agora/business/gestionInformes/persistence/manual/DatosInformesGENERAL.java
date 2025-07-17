package es.altia.agora.business.gestionInformes.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Vector;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.altia.agora.business.gestionInformes.FichaInformeValueObject;
import es.altia.agora.business.gestionInformes.SolicitudInformeValueObject;
import es.altia.agora.business.gestionInformes.CriteriosSolicitudValueObject;
import es.altia.agora.business.gestionInformes.CriteriosValueObject;
import es.altia.agora.business.gestionInformes.tareas.AsistenteCriterio;
import es.altia.agora.business.gestionInformes.persistence.CampoValueObject;
import es.altia.agora.business.terceros.persistence.manual.DatosSuplementariosTerceroManager;
import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.gestionInformes.FachadaDatosInformes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.cache.CacheDatosFactoria;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;

public class DatosInformesGENERAL implements FachadaDatosInformes {
    protected static Log m_Log =
             LogFactory.getLog(DatosInformesGENERAL.class.getName());

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
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            StringBuffer sql = new StringBuffer();
            sql.append(" select NOMEAS AS CODIGO,NOME AS NOME, '" + fiVO.getTabAmbito() + "' AS TABLA")
                .append(" FROM INF_CAMPOS ")
                .append(" WHERE ORIGEN = " + fiVO.getCodAmbito())
                .append(" ORDER BY 2");
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
                r.add(campoVO);
            }//while(rs.next())
            rs.close();
            stmt.close();
            //Recuperamos los datos para los campos suplementarios de los terceros.
            getCamposSuplementariosTercero(r, conexion,fiVO.getCodMunicipio());
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

    private void getCamposSuplementariosTercero(Vector listaCampos, Connection conexion, String codMunicipio) 
            throws SQLException {
        if(m_Log.isDebugEnabled()) m_Log.debug("getCamposSuplementariosTercero() : BEGIN");
        try{
            StringBuffer sql = new StringBuffer();
            sql.append("Select COD_CAMPO, ROTULO, TIPO_DATO from T_CAMPOS_EXTRA")
                .append(" where COD_MUNICIPIO = ").append(codMunicipio)
                .append(" and ACTIVO = 'SI'");

            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            ResultSet rs = stmt.executeQuery(sql.toString());

            while(rs.next()){
                CampoValueObject campoVO = new CampoValueObject();
                String codigo = rs.getString("COD_CAMPO");
                String rotulo = rs.getString("ROTULO");
                String tipoDato = rs.getString("TIPO_DATO");
                campoVO.setOrigen(codigo);
                campoVO.setTitulo(rotulo);
                campoVO.setTituloOriginal(rotulo);
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
            }//while(rs.next())
        } catch (SQLException sqle){
            m_Log.error("Error de SQL en getCamposSuplementariosTercero: " + sqle);
            throw new SQLException(sqle.getMessage());
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("getCamposSuplementariosTercero() : END");
    }//getCamposSuplementariosTercero
    
    public Vector getListaCriteriosDisponibles(FichaInformeValueObject fiVO, String[] params) {
        if(m_Log.isDebugEnabled()) m_Log.debug("getListaCriteriosDisponibles() : BEGIN");
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Config common = ConfigServiceHelper.getConfig("common");
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            StringBuffer sql = new StringBuffer();
            sql.append(" select campo AS CODIGO,NOME AS NOME, '" + fiVO.getTabAmbito() + "' AS TABLA")
                .append(" FROM INF_CAMPOS ")
                .append(" WHERE ORIGEN = " + fiVO.getCodAmbito())
                .append(" ORDER BY 2");
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
                r.add(campoVO);
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

    /**
     * Carga de la lista de campos de informe disponibles para este tipo de informe los campos para que puedan ser seleccionados como criterio
     * al crear la plantilla
     * @param fiVO
     * @param params
     * @return 
     */
    public Vector getListaCriInforme(FichaInformeValueObject fiVO, String[] params) {
        if(m_Log.isDebugEnabled()) m_Log.debug("getListaCriInforme() : BEGIN");
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        int criterio = 0;
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql = "select CODIGO, NOME, '" + fiVO.getTabAmbito() + "', TIPO FROM INF_CAMPOS WHERE ORIGEN = " + fiVO.getCodAmbito() + " and CRITERIO=1 ORDER BY 2";
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                CampoValueObject campoVO = new CampoValueObject();
                if (criterio < rs.getInt(1)) criterio = rs.getInt(1);
                campoVO.setIdCampo(rs.getString(1));
                campoVO.setTitulo(rs.getString(2));
                campoVO.setTabla(rs.getString(3));
                campoVO.setTipo(rs.getString(4));
                m_Log.debug("COD_PROCEDIMIENTO " + fiVO.getCodProcedimiento());
                m_Log.debug("COD_CRITERIO " + rs.getString(1));
                r.add(campoVO);
            }//while(rs.next())
            rs.close();
            stmt.close();
            //Criterios de campos adicionales de tercero.
            getCriteriosSuplementariosTercero(r, conexion, fiVO.getCodProcedimiento(), fiVO.getCodMunicipio(),criterio);
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

    /**
     * Devuelve una lista con los campos suplementarios del tercero y sus tablas y tipos para añadirlos a la lista de criterios
     * seleccionables para una plantilla.
     * @param listaCampos
     * @param conexion
     * @param codProcedimiento
     * @param codMunicipio
     * @param criterio
     * @throws SQLException 
     */
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

    /**
     * Recupera la lista de criterios seleccionados para una plantilla
     * @param fiVO
     * @param params
     * @return 
     */
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
                criInf.setTipo(rs.getString(9));
                r.add(criInf);
            }//while (rs.next())
        } catch (SQLException sqle) {
            m_Log.error("Error de SQL en getListaCriSeleccionados: " + sqle);
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

    /*
    * ENTRADA:  gvo (datos de entrada)
    *           params (datos de conexión)
    *
    *           gvo.codPlantilla : codigo de la plantilla
    *           gvo.codProcedimiento : codigo del procedimiento
    *           gvo.fechaInicio : fecha inicial del expediente (fecha inicio)
    *           gvo.fechaFInal : fecha final del expediente (fecha inicio)
    *           gvo.codMunicipio : codigo del municipio
    *
    * SALIDA:   Vector de vectores
    *           Cada componente del vector de salida estará compuesto a su vez por otro vector con los datos del informe
    *
    */
    public Vector getDatosExpedientes(SolicitudInformeValueObject siVO, String codOrganizacion, String[] params) throws Exception {
        if(m_Log.isDebugEnabled()) m_Log.debug("execute getDatosExpedientes() BEGIN");
        Vector r = new Vector();
        Vector resul = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        TransformacionAtributoSelect transformador;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            transformador = new TransformacionAtributoSelect(abd);
            String sql;
            String codProcedimiento = siVO.getProcedimiento();

            //Campos Genéricos
            if(m_Log.isDebugEnabled()) m_Log.debug("recupera campos generales plantilla");
            Vector camposGenericos = getCamposGeneralesPlantilla(siVO.getCodPlantilla(),params);
            String listaCamposGenericos = "";
            for (int i=0; i< camposGenericos.size(); i++) {
                if (i+1== camposGenericos.size())
                    listaCamposGenericos += "'"+ camposGenericos.get(i)+"'";
                else
                    listaCamposGenericos += "'"+ camposGenericos.get(i)+"',";
            }//for (int i=0; i< camposGenericos.size(); i++) 
            if(m_Log.isDebugEnabled()) m_Log.debug("FIN recupera campos generales plantilla");
            
            //Filtramos de la lista de campos genericos los campos suplementarios de terceros
            DatosSuplementariosTerceroManager datosSuplementariosTerceroManager = DatosSuplementariosTerceroManager.getInstance();
            Vector camposSuplementariosTerceros = 
                datosSuplementariosTerceroManager.getValoresSuplementariosTerceroFromVector(camposGenericos, codOrganizacion, params);

            CriteriosSolicitudValueObject criteriosVO;
            Vector criterios = new Vector();
            String codSolicitud = siVO.getCodSolicitud();
            //Recuperamos los criterios de los campos normales
            sql = "SELECT COD_CRITERIO," +
                  "INF_CRITERIOS.COD_TIPO," +
                  "INF_CRITERIOS.CONDICION," +
                  "INF_CRITERIOS.VALOR1," +
                  "INF_CRITERIOS.VALOR2," +
                  "INF_CRITERIOS.TITULO," +
                  "INF_CAMPOS.CAMPO," +
                  "INF_CRITERIOS.TABLA," +  
                  "INF_CAMPOS.TIPO" +
                  " FROM INF_CRITERIOS" +
                  " INNER JOIN INF_CAMPOS ON ( INF_CRITERIOS.COD_TIPO = INF_CAMPOS.CODIGO )" +
                  " WHERE COD_SOLICITUD = " + codSolicitud +
                  " AND TABLA NOT IN ('T_CAMPOS_DESPLEGABLE','T_CAMPOS_TEXTO','T_CAMPOS_TEXTO_LARGO','T_CAMPOS_FECHA','T_CAMPOS_ALFANUMERICO')" ;
            if(m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
            
            if(m_Log.isDebugEnabled()) m_Log.debug("Inicio criterios");
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("FIN criterios");
            
            if(m_Log.isDebugEnabled()) m_Log.debug("Inicio resultset");
            while(rs.next()) {
                criteriosVO = new CriteriosSolicitudValueObject();
                criteriosVO.setCodSolicitud(codSolicitud);
                criteriosVO.setCodCriterio(rs.getString(1));
                criteriosVO.setCampo(rs.getString(2));
                criteriosVO.setTitulo(rs.getString(6));
                criteriosVO.setOrigen(rs.getString(7));
                criteriosVO.setTabla(rs.getString(8));
                criteriosVO.setTipo(rs.getString(9));
                Statement stmt2 = conexion.createStatement();
                String sql2= " SELECT COD_CONDICION, DESC_CONDICION FROM CONDICION_CRITERIOS_INFORMES WHERE COD_CONDICION = " + rs.getString(3);
                ResultSet rs2 = stmt2.executeQuery(sql2);
                while (rs2.next()) {
                    criteriosVO.setCondicion(rs2.getString(2));
                }//while (rs2.next()) 
                rs2.close();
                stmt2.close();
                if (rs.getString(4)!=null && !rs.getString(4).equals("null")) {
                   criteriosVO.setValor1(rs.getString(4));
                } else {
                   criteriosVO.setValor1("");
                }//if (rs.getString(4)!=null && !rs.getString(4).equals("null"))
                if (rs.getString(5)!=null && !rs.getString(5).equals("null")) {
                   criteriosVO.setValor2(rs.getString(5));
                } else {
                   criteriosVO.setValor2("");
                }//if (rs.getString(5)!=null && !rs.getString(5).equals("null")) 
                criterios.addElement(criteriosVO);
            }//while(rs.next()) 
            rs.close();
            stmt.close();
            if(m_Log.isDebugEnabled()) m_Log.debug("FIN resultset");
            
            //Recuperamos los criterios de los campos adicionales de tercero.
            if(m_Log.isDebugEnabled()) m_Log.debug("Inicio criterios");
            stmt = conexion.createStatement();
            sql = "SELECT COD_CRITERIO, INF_CRITERIOS.COD_TIPO, INF_CRITERIOS.CONDICION, INF_CRITERIOS.VALOR1, INF_CRITERIOS.VALOR2," +
                    "INF_CRITERIOS.TITULO, INF_CRITERIOS.TABLA, INF_CRITERIOS.ORIGEN FROM INF_CRITERIOS WHERE COD_SOLICITUD = " + codSolicitud +
                    " AND TABLA IN ('T_CAMPOS_DESPLEGABLE','T_CAMPOS_TEXTO','T_CAMPOS_TEXTO_LARGO','T_CAMPOS_FECHA','T_CAMPOS_ALFANUMERICO')" ;
            if(m_Log.isDebugEnabled()) m_Log.debug("sql criterios adicionales tercero = " + sql);
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                criteriosVO = new CriteriosSolicitudValueObject();
                criteriosVO.setCodSolicitud(codSolicitud);
                criteriosVO.setCodCriterio(rs.getString(1));
                criteriosVO.setCampo(rs.getString(2));
                criteriosVO.setTitulo(rs.getString(6));
                criteriosVO.setTabla(rs.getString(7));
                criteriosVO.setOrigen(rs.getString(8));
                Statement stmt2 = conexion.createStatement();
                String sql2= " SELECT COD_CONDICION, DESC_CONDICION FROM CONDICION_CRITERIOS_INFORMES WHERE COD_CONDICION = " + rs.getString(3);
                ResultSet rs2 = stmt2.executeQuery(sql2);
                while (rs2.next()) {
                    criteriosVO.setCondicion(rs2.getString(2));
                }//while (rs2.next()) 
                rs2.close();
                stmt2.close();
                if (rs.getString(4)!=null && !rs.getString(4).equals("null")) {
                   criteriosVO.setValor1(rs.getString(4));
                } else {
                   criteriosVO.setValor1("");
                }//if (rs.getString(4)!=null && !rs.getString(4).equals("null"))
                if (rs.getString(5)!=null && !rs.getString(5).equals("null")) {
                   criteriosVO.setValor2(rs.getString(5));
                } else {
                   criteriosVO.setValor2("");
                }//if (rs.getString(5)!=null && !rs.getString(5).equals("null")) 
                criterios.addElement(criteriosVO);
            }//while(rs.next()) 
            rs.close();
            stmt.close();
            if(m_Log.isDebugEnabled()) m_Log.debug("Fin criterios");
            
            String tabla_origen = "";
            String id_origen = "";
            if(m_Log.isDebugEnabled()) m_Log.debug("Inicio origen");
            stmt = conexion.createStatement();
            sql = " select tab_origen, id_origen from inf_origen, plant_informes, inf_solicitud " +
                  " where plant_informes.plant_origen = inf_origen.id_origen and inf_solicitud.cod_plantilla = plant_informes.plant_plantilla " +
                  " and inf_solicitud.cod_solicitud = " + codSolicitud;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                tabla_origen = rs.getString(1);
                id_origen = rs.getString(2);
            }//if (rs.next())
            rs.close();
            stmt.close();
            if(m_Log.isDebugEnabled()) m_Log.debug("Fin origen"); 
            
            Vector campos_origen = new Vector();
            Vector orden_campos=new Vector();
            String campos_cadena = "";
            if(m_Log.isDebugEnabled()) m_Log.debug("Inicio campos");
            stmt = conexion.createStatement();            
            
            sql = "select inf_campos.campo,plant_inf_col.PLANT_INF_COL_ORD  "
                    + "from inf_campos "
                    + "inner join plant_inf_col on (inf_campos.NOMEAS = plant_inf_col.PLANT_INF_COL_ORIGEN) "
                    + "inner join inf_origen on (plant_inf_col.plant_inf_col_tabla=inf_origen.tab_origen and inf_origen.id_origen=inf_campos.ORIGEN)"
                    + "inner join inf_solicitud on (inf_solicitud.cod_plantilla = plant_inf_col.PLANT_INF_COL_PLANTILLA)"
                    + "where inf_solicitud.cod_solicitud = " + codSolicitud;
            
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                campos_origen.add(rs.getString(1));
                campos_cadena += rs.getString(1) + ",";
                orden_campos.add(rs.getString(2));
            }//while (rs.next())
            rs.close();
            stmt.close();
            if(m_Log.isDebugEnabled()) m_Log.debug("Fin campos");
            
            //Añadimos siempre el campo RES_TER para el código del tercero.
            campos_origen.add("RES_TER");
            campos_cadena += "RES_TER,";
            orden_campos.add("0");
            campos_cadena = campos_cadena.substring(0, campos_cadena.length()-1);

            //Expedientes
            String estado;
            boolean hayWhere = false;
            sql = "select DISTINCT " + campos_cadena + " from " + tabla_origen;
            if (codProcedimiento!=null && !codProcedimiento.equals("")) {
                sql = sql + " where pro_cod='"+codProcedimiento+"'"; // tratado criterio tipo procedimiento
                hayWhere = true;
            }//if (codProcedimiento!=null && !codProcedimiento.equals("")) 
            for (int i=0;i<criterios.size();i++) {  // tratado criterios fecha inicio y fin, estado y procedimiento
                criteriosVO = (CriteriosSolicitudValueObject) criterios.get(i);
                if(m_Log.isDebugEnabled()) m_Log.debug("criteriosVO "+criteriosVO);
                //Si la tabla es igual que la tabla origen (la vista o lo que sea)
                //Si la tabla no es igual que la tabla origen hay unos criterios para cada tabla
                //Las tablas adicionales son (T_CAMPOS_DESPLEGABLE, T_CAMPOS_TEXTO, T_CAMPOS_TEXTO_LARGO, T_CAMPOS_NUMERICO, T_CAMPOS_FECHA)
                //que son las de los datos suplementarios de tercero.
                if (criteriosVO.getTabla().equalsIgnoreCase(tabla_origen)) {
                    if (hayWhere) {
                        sql = sql + " and ";
                    } else {
                        sql = sql + " where ";
                        hayWhere = true;
                    }//if (hayWhere) 

                    if (criteriosVO.getTipo().equalsIgnoreCase("F")) {
                        if (criteriosVO.getCondicion().equalsIgnoreCase("entre")) {
                            sql = sql + "(" + (String)criteriosVO.getOrigen()+
                                " between " + abd.convertir("'"+criteriosVO.getValor1()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") +
                                " and " + abd.convertir("'"+criteriosVO.getValor2()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") + ")";
                        } else if (criteriosVO.getCondicion().equalsIgnoreCase("IS NULL")){
                            sql = sql + " " + criteriosVO.getOrigen() + " IS NULL ";
                        } else if (criteriosVO.getCondicion().equalsIgnoreCase("IS NOT NULL")){    
                            sql = sql + " " + criteriosVO.getOrigen() + " IS NOT NULL ";
                        } else {
                            sql = sql + "(" + (String)criteriosVO.getOrigen() +
                                criteriosVO.getCondicion() + abd.convertir("'"+criteriosVO.getValor1()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") + ")";
                        }//if
                    } else {
                        criteriosVO.setValor1(criteriosVO.getValor1().replace('*','%'));
                        if (criteriosVO.getCondicion().equals("<>")) {
                            sql = sql + "(" + criteriosVO.getOrigen() + " NOT LIKE " + "'" + criteriosVO.getValor1() + "'" +
                                " OR " + criteriosVO.getOrigen() + " IS NULL) ";
                        } else if (criteriosVO.getCondicion().equalsIgnoreCase("IS NULL")){
                            sql = sql + " " + criteriosVO.getOrigen() + " IS NULL ";
                        } else if (criteriosVO.getCondicion().equalsIgnoreCase("IS NOT NULL")){
                            sql = sql + " " + criteriosVO.getOrigen() + " IS NOT NULL ";
                        } else {
                            sql = sql + criteriosVO.getOrigen() + " LIKE " + "'" + criteriosVO.getValor1() + "'";
                        }//if
                    }//if (criteriosVO.getTipo().equalsIgnoreCase("F"))
                }else if(criteriosVO.getTabla().equalsIgnoreCase("T_CAMPOS_DESPLEGABLE")){
                    if(m_Log.isDebugEnabled()) m_Log.debug("Criterio campo adicional tercero desplegable");
                    if (hayWhere) {
                        sql = sql + " and ";
                    } else {
                        sql = sql + " where ";
                        hayWhere = true;
                    }//if (hayWhere) 
                    sql += "RES_TER in ";
                    StringBuffer sbSql = new StringBuffer();
                        if(criteriosVO.getCondicion().equalsIgnoreCase("=") || criteriosVO.getCondicion().equalsIgnoreCase("<>")){
                            sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_DESPLEGABLE Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR " 
                                + criteriosVO.getCondicion() + " '" + criteriosVO.getValor1() + "'");
                            sbSql.append(")");
                        }else if(criteriosVO.getCondicion().equalsIgnoreCase("IS NULL")){
                            sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_DESPLEGABLE Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR IS NULL ");
                            sbSql.append(")");
                        }else if(criteriosVO.getCondicion().equalsIgnoreCase("IS NOT NULL")){    
                            sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_DESPLEGABLE Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR IS NOT NULL ");
                            sbSql.append(")");
                        }//if condiciones
                        sql += sbSql.toString();
                }else if(criteriosVO.getTabla().equalsIgnoreCase("T_CAMPOS_TEXTO")){
                    if(m_Log.isDebugEnabled()) m_Log.debug("Criterio campo adicional tercero texto");
                    if (hayWhere) {
                        sql = sql + " and ";
                    } else {
                        sql = sql + " where ";
                        hayWhere = true;
                    }//if (hayWhere) 
                    sql += "RES_TER in ";
                    StringBuffer sbSql = new StringBuffer();
                    if(criteriosVO.getCondicion().equalsIgnoreCase("=") || criteriosVO.getCondicion().equalsIgnoreCase("<>")){
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_TEXTO Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR " 
                                + criteriosVO.getCondicion() + " '" + criteriosVO.getValor1() + "'");
                            sbSql.append(")");
                    }else if(criteriosVO.getCondicion().equalsIgnoreCase("LIKE")){
                        String valor = criteriosVO.getValor1().replace('*','%');
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_TEXTO Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR LIKE '" 
                                    + valor + "'");
                        sbSql.append(")");
                    }else if(criteriosVO.getCondicion().equalsIgnoreCase("IS NULL")){
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_TEXTO Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR IS NULL ");
                        sbSql.append(")");
                    }else if(criteriosVO.getCondicion().equalsIgnoreCase("IS NOT NULL")){
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_TEXTO Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR IS NOT NULL ");
                        sbSql.append(")");
                    }//if condiciones
                    sql += sbSql.toString();
                }else if(criteriosVO.getTabla().equalsIgnoreCase("T_CAMPOS_TEXTO_LARGO")){
                    if(m_Log.isDebugEnabled()) m_Log.debug("Criterio campo adicional tercero texto largo");
                    if (hayWhere) {
                        sql = sql + " and ";
                    } else {
                        sql = sql + " where ";
                        hayWhere = true;
                    }//if (hayWhere) 
                    sql += "RES_TER in ";
                    StringBuffer sbSql = new StringBuffer();
                    if(criteriosVO.getCondicion().equalsIgnoreCase("=") || criteriosVO.getCondicion().equalsIgnoreCase("<>")){
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_TEXTO_LARGO Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR " 
                                + criteriosVO.getCondicion() + " '" + criteriosVO.getValor1() + "'");
                            sbSql.append(")");
                    }else if(criteriosVO.getCondicion().equalsIgnoreCase("LIKE")){
                        String valor = criteriosVO.getValor1().replace('*','%');
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_TEXTO_LARGO Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR LIKE '" 
                                    + valor + "'");
                        sbSql.append(")");
                    }else if(criteriosVO.getCondicion().equalsIgnoreCase("IS NULL")){
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_TEXTO_LARGO Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR IS NULL ");
                        sbSql.append(")");
                    }else if(criteriosVO.getCondicion().equalsIgnoreCase("IS NOT NULL")){
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_TEXTO_LARGO Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR IS NOT NULL ");
                        sbSql.append(")");
                    }//if condiciones
                    sql += sbSql.toString();
                }else if(criteriosVO.getTabla().equalsIgnoreCase("T_CAMPOS_FECHA")){
                    if(m_Log.isDebugEnabled()) m_Log.debug("Criterio campo adicional tercero fecha");
                    if (hayWhere) {
                        sql = sql + " and ";
                    } else {
                        sql = sql + " where ";
                        hayWhere = true;
                    }//if (hayWhere) 
                    sql += "RES_TER in ";
                    StringBuffer sbSql = new StringBuffer();
                    if(criteriosVO.getCondicion().equalsIgnoreCase("=") || criteriosVO.getCondicion().equalsIgnoreCase("<>") 
                            ||criteriosVO.getCondicion().equalsIgnoreCase(">") || criteriosVO.getCondicion().equalsIgnoreCase(">=")
                            ||criteriosVO.getCondicion().equalsIgnoreCase("<") || criteriosVO.getCondicion().equalsIgnoreCase("<=")){
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_FECHA Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR " + criteriosVO.getCondicion());
                            sbSql.append(" ");
                            sbSql.append(abd.convertir("'"+criteriosVO.getValor1()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY"));
                        sbSql.append(")");
                    }else if(criteriosVO.getCondicion().equalsIgnoreCase("ENTRE")){
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_FECHA Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR BETWEEN ");
                            sbSql.append(" ");
                            sbSql.append(abd.convertir("'"+criteriosVO.getValor1()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY"));
                            sbSql.append(" AND ");
                            sbSql.append(abd.convertir("'"+criteriosVO.getValor2()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY"));
                        sbSql.append(")");
                    }else if(criteriosVO.getCondicion().equalsIgnoreCase("IS NULL")){
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_FECHA Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR IS NULL ");
                        sbSql.append(")");
                    }else if(criteriosVO.getCondicion().equalsIgnoreCase("IS NOT NULL")){
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_FECHA Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR IS NOT NULL ");
                        sbSql.append(")");
                    }//if condicion
                    sql += sbSql.toString();
                }else if(criteriosVO.getTabla().equalsIgnoreCase("T_CAMPOS_NUMERICO")){
                    if(m_Log.isDebugEnabled()) m_Log.debug("Criterio campo adicional tercero numerico");
                    if (hayWhere) {
                        sql = sql + " and ";
                    } else {
                        sql = sql + " where ";
                        hayWhere = true;
                    }//if (hayWhere) 
                    sql += "RES_TER in ";
                    StringBuffer sbSql = new StringBuffer();
                    if(criteriosVO.getCondicion().equalsIgnoreCase("=") || criteriosVO.getCondicion().equalsIgnoreCase("<>") 
                            ||criteriosVO.getCondicion().equalsIgnoreCase(">") || criteriosVO.getCondicion().equalsIgnoreCase(">=")
                            ||criteriosVO.getCondicion().equalsIgnoreCase("<") || criteriosVO.getCondicion().equalsIgnoreCase("<=")){
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_NUMERICO Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR ");
                            sbSql.append(criteriosVO.getCondicion() + " ");
                            sbSql.append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+criteriosVO.getValor1()+"'"}));
                        sbSql.append(")");
                    }else if(criteriosVO.getCondicion().equalsIgnoreCase("<>")){
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_NUMERICO Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR ");
                            sbSql.append(" <> ");
                            sbSql.append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+criteriosVO.getValor1()+"'"}));
                        sbSql.append(")");
                    }else if(criteriosVO.getCondicion().equalsIgnoreCase("ENTRE")){
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_NUMERICO Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR ");
                            sbSql.append(" BETWEEN ");
                            sbSql.append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+criteriosVO.getValor1()+"'"}));
                            sbSql.append(" AND ");
                            sbSql.append(abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, new String[]{"'"+criteriosVO.getValor2()+"'"}));
                        sbSql.append(")");
                    }else if(criteriosVO.getCondicion().equalsIgnoreCase("IS NULL")){
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_NUMERICO Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR IS NULL");
                        sbSql.append(")");
                    }else if(criteriosVO.getCondicion().equalsIgnoreCase("IS NOT NULL")){
                        sbSql.append("(");
                            sbSql.append("Select COD_TERCERO From T_CAMPOS_NUMERICO Where ");
                            sbSql.append(" COD_CAMPO = '" + criteriosVO.getOrigen() + "' and VALOR IS NOT NULL");
                        sbSql.append(")");
                    }//if
                    sql += sbSql.toString();
                }//if tablas
            }//for (int i=0;i<criterios.size();i++) 
            
            //tengo los campos en campos_cadena y su ordenacion en orden_campos
            // hacemos el order by del campo si orden_campos es 1= asc, si 2=desc 
            // si 0 nada.
            //recorro es la varible que me indica donde debo enpezar en el siguiente bucle
            if(orden_campos.contains("1") || orden_campos.contains("2")){
                int z=0,entro=0,recorro=0;
                for(z=0;z<orden_campos.size();z++){
                    if(entro==0){
                        if(orden_campos.elementAt(z).equals("1")){
                                sql = sql + " order by " +campos_origen.elementAt(z)+ " asc";  
                                entro=1;
                                recorro=z+1;
                        }else if(orden_campos.elementAt(z).equals("2")){
                                sql = sql + " order by " +campos_origen.elementAt(z)+ " desc";  
                                entro=1;
                                recorro=z+1;
                        }else if(orden_campos.elementAt(z).equals("0")) {
                                entro=0;
                                recorro=0;
                        }//if
                    }//if(entro==0)
                }//for(z=0;z<orden_campos.size();z++)
                int x=0;
                //SI HAY MAS DE UN CAMPO POR EL QUE ORDENAR CONCATENO AL ORDE BY ANTERIOR LO QUE TOQUE
                for(x=recorro;x<orden_campos.size();x++){
                    if(orden_campos.elementAt(x).equals("1")){
                        sql = sql + " , " +campos_origen.elementAt(x)+ " asc";  
                    }else if(orden_campos.elementAt(x).equals("2")){
                        sql = sql + " , " +campos_origen.elementAt(x)+ " desc";  
                    }//if
                }//for(x=recorro;x<orden_campos.size();x++)
            }//if(orden_campos.contains("1") || orden_campos.contains("2"))
            
            if(m_Log.isDebugEnabled()) m_Log.debug("Inicio consulta datos");
            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug("sqlOrder = " + sql);
            rs = stmt.executeQuery(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug("Fin consulta datos");
            if(m_Log.isDebugEnabled()) m_Log.debug("Inicio resultset");
            GeneralValueObject gVO;
            Vector resultado = new Vector();
            while(rs.next()) {
                gVO = new GeneralValueObject();
                for (int w=0; w< campos_origen.size(); w++) {
                    String campo = (String)campos_origen.get(w);
                    gVO.setAtributo(campo,rs.getString(campo));
                }//for (int w=0; w< campos_origen.size(); w++) 
                resultado.add(gVO);
            }//while(rs.next()) 
            rs.close();
            stmt.close();
            if(m_Log.isDebugEnabled()) m_Log.debug("Fin resultset");
            if(m_Log.isDebugEnabled()) m_Log.debug("resultado.size = " + resultado.size());
            
            if(m_Log.isDebugEnabled()) m_Log.debug("Recorremos resultado inicio");
            for (int i=0;i<resultado.size();i++) {
                gVO = (GeneralValueObject) resultado.get(i);
                Statement stmt2 = conexion.createStatement();
                String sql2= " SELECT NOMEAS AS CODIGO,NOME AS NOME,CAMPO AS CAMPO FROM INF_CAMPOS  WHERE ORIGEN = " 
                        + id_origen + " AND NOMEAS IN (" + listaCamposGenericos + ")";
                ResultSet rs2 = stmt2.executeQuery(sql2);
                GeneralValueObject aux = new GeneralValueObject();
                while (rs2.next()){
                    String codigo= rs2.getString("codigo");
                    String campo= rs2.getString("campo");
                    aux.setAtributo(codigo,gVO.getAtributo(campo));
                }//while (rs2.next())
                rs2.close();
                stmt2.close();

                //CAMPOS INTERESADOS
                sql2 = "SELECT int_rol, int_nom, int_domnn, int_doc, int_pob, int_tlf FROM v_int WHERE int_eje=" 
                        + gVO.getAtributo("CRO_EJE")+" AND int_num='" + gVO.getAtributo("CRO_NUM") + "' ORDER BY 1";
                stmt2 = conexion.createStatement();
                rs2 = stmt2.executeQuery(sql2);
                while(rs2.next()) {
                    aux.setAtributo("Rol"+rs2.getString(1),rs2.getString(1));
                    if (rs2.getString(1)!= null && rs2.getString(2)!= null) aux.setAtributo(rs2.getString(1),rs2.getString(2));
                    aux.setAtributo("Dom"+rs2.getString(1),rs2.getString(3));
                    aux.setAtributo("Doc"+rs2.getString(1),rs2.getString(4));
                    aux.setAtributo("Pob"+rs2.getString(1),rs2.getString(5));
                    aux.setAtributo("Tlfno"+rs2.getString(1),rs2.getString(6));
                }//while(rs2.next()) 

                rs2.close();
                stmt2.close();

                //Campos suplementarios tercero.
                String codTercero = (String) gVO.getAtributo("RES_TER");
                DatosSuplementariosTerceroManager datosSuplementarioTerceroManager = DatosSuplementariosTerceroManager.getInstance();
                for(Object objectCampoSuplementario : camposSuplementariosTerceros){
                    String campoSuplementario = String.valueOf(objectCampoSuplementario);
                    String valor = datosSuplementarioTerceroManager.getValorCampoSuplementario(campoSuplementario, codOrganizacion, codTercero, params);
                    aux.setAtributo(campoSuplementario, valor);
                }//for(String campoSuplementario : camposSuplementariosTerceros)

                //AÑADIR OBJECTO GENERALVALUE CON LOS DATOS EN UNA LISTA TEMPORAL
                r.add(aux);
            }//for (int i=0;i<resultado.size();i++) 
            if(m_Log.isDebugEnabled()) m_Log.debug("Recorremos resultado fin");
            
            //ORDEN Y CAMPOS DEL INFORME
            Vector campos = new Vector();
            sql="select PLANT_INF_COL_ORIGEN from PLANT_INF_COL where PLANT_INF_COL_PLANTILLA = " + siVO.getCodPlantilla() +" order by PLANT_INF_COL_IDCAMPO ";
            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug("ORDEN Y CAMPOS DEL INFORME = " + sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                campos.add(rs.getString(1));
            }//while (rs.next()) 
            rs.close();
            stmt.close();

            //RECORRER VECTOR TEMPORAL CON LOS DATOS
            if(m_Log.isDebugEnabled()) m_Log.debug("Recorrer bucle temporal con los datos");
            for (int i=0; i < r.size(); i++) {
                GeneralValueObject exp = (GeneralValueObject) r.get(i);
                Vector aux = new Vector();
                for (int j=0; j < campos.size(); j++) {
                    String campo = (String)campos.get(j);
                    aux.add(exp.getAtributo(campo));
                }//for (int j=0; j < campos.size(); j++)
                resul.add(aux);
            }//for (int i=0; i < r.size(); i++) 
            if(m_Log.isDebugEnabled()) m_Log.debug("Fin Recorrer bucle temporal con los datos");
        } catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error("Error en el metodo getDatosExpedientes: " + e);
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }//try-catch
            }//if (conexion != null) 
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("execute getDatosExpedientes() END");
        return resul;
    }//getDatosExpedientes

    private Vector getCamposGeneralesPlantilla(String plantilla, String[] params) throws Exception {
        if(m_Log.isDebugEnabled()) m_Log.debug("getCamposGeneralesPlantilla() : BEGIN");
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            String sql;
            sql = "select PLANT_INF_COL_ORIGEN from plant_inf_col where  PLANT_INF_COL_PLANTILLA = " + plantilla;
            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                r.add(rs.getString(1));
            }//while(rs.next())
            rs.close();
            stmt.close();
        } catch (Exception e) {
            m_Log.error("Error en getCamposGeneralesPlantilla: " + e);
            throw new Exception(e);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
                } catch(BDException bde) {
                    m_Log.error("No se pudo devolver la conexion: " + bde);
                }//try-catch
            }//if (conexion != null) 
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("getCamposGeneralesPlantilla() : END");
        return r;
    }//getCamposGeneralesPlantilla

    public Vector getListaCriFinal(FichaInformeValueObject fiVO, String[] params) {
        if(m_Log.isDebugEnabled()) m_Log.debug("getListaCriFinal() : BEGIN");
        Vector r = new Vector();
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();

            String sql = "SELECT PLANT_INF_CRI_ID, PLANT_INF_CRI_CAMPO, " +
                "PLANT_INF_CRI_CONDICION, PLANT_INF_CRI_VALOR1," +
                "PLANT_INF_CRI_VALOR2, PLANT_INF_CRI_TITULO," +
                "PLANT_INF_CRI_ORIGEN, PLANT_INF_CRI_TABLA, TIPO FROM PLANT_INF_CRI p,INF_CAMPOS ic " +
                " WHERE p.PLANT_INF_CRI_CAMPO = ic.CODIGO " +
                " AND PLANT_INF_CRI_PLANTILLA=" + fiVO.getCodPlantilla() +
                " AND PLANT_INF_CRI_TABLA NOT IN ('T_CAMPOS_DESPLEGABLE', 'T_CAMPOS_TEXTO', 'T_CAMPOS_TEXTO_LARGO', 'T_CAMPOS_FECHA', 'T_CAMPOS_NUMERICO')";

            Statement stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                CriteriosValueObject criteriosVO;
                criteriosVO = new CriteriosValueObject();
                criteriosVO.setCodPlantilla(fiVO.getCodPlantilla());
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
            }//while(rs.next())

            String sqlCamposAdicionalesTercero = "SELECT PLANT_INF_CRI_CAMPO, " +
                "PLANT_INF_CRI_CONDICION, PLANT_INF_CRI_VALOR1," +
                "PLANT_INF_CRI_VALOR2, PLANT_INF_CRI_TITULO," +
                "PLANT_INF_CRI_ORIGEN, PLANT_INF_CRI_TABLA FROM PLANT_INF_CRI p " +
                "WHERE PLANT_INF_CRI_PLANTILLA=" + fiVO.getCodPlantilla() + " " +
                "AND PLANT_INF_CRI_TABLA IN ('T_CAMPOS_DESPLEGABLE', 'T_CAMPOS_TEXTO', 'T_CAMPOS_TEXTO_LARGO', 'T_CAMPOS_FECHA', 'T_CAMPOS_NUMERICO')";
            
            if(m_Log.isDebugEnabled()) m_Log.debug("sqlCamposAdicionalesTercero = " + sqlCamposAdicionalesTercero);
            rs = stmt.executeQuery(sqlCamposAdicionalesTercero);
            while(rs.next()){
                CriteriosValueObject criteriosVO = new CriteriosValueObject();
                criteriosVO.setCodPlantilla(fiVO.getCodPlantilla());
                criteriosVO.setId(rs.getString(1));
                criteriosVO.setCampo(rs.getString(1));
                criteriosVO.setCondicion(rs.getString(2)!=null?rs.getString(2):"");
                criteriosVO.setValor1(rs.getString(3)!=null?rs.getString(3):"");
                criteriosVO.setValor2(rs.getString(4)!=null?rs.getString(4):"");
                criteriosVO.setTitulo(rs.getString(5)!=null?rs.getString(5):"");
                criteriosVO.setOrigen(rs.getString(6)!=null?rs.getString(6):"");
                String tabla = rs.getString(7);
                criteriosVO.setTabla(tabla);
                if(tabla.equalsIgnoreCase("T_CAMPOS_TEXTO") || tabla.equalsIgnoreCase("T_CAMPOS_TEXTO_LARGO")){
                    criteriosVO.setTipo("A");
                }else if(tabla.equalsIgnoreCase("T_CAMPOS_FECHA")){
                    criteriosVO.setTipo("F");
                }else if(tabla.equalsIgnoreCase("T_CAMPOS_DESPLEGABLE")){
                    criteriosVO.setTipo("D");
                }else if(tabla.equalsIgnoreCase("T_CAMPOS_NUMERICO")){
                    criteriosVO.setTipo("N");
                }//if
                r.add(criteriosVO);
            }//while(rs.next())
            rs.close();
            stmt.close();
        } catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error("Error en el metodo getListaCriterios: " + e);
        } finally {
            if (conexion != null) {
                try {
                    abd.devolverConexion(conexion);
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
}//class
