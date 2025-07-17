package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.editor.mantenimiento.persistence.manual.DocumentosAplicacionDAO;
import es.altia.agora.business.geninformes.GeneradorInformesMgr;
import es.altia.agora.business.geninformes.utils.UtilidadesXerador;
import es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades;
import es.altia.agora.business.geninformes.utils.XeracionInformes.NodoEntidad;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.FichaExpedienteManager;
import es.altia.agora.business.sge.persistence.TramitacionExpedientesManager;
import es.altia.agora.business.sge.persistence.FichaRelacionExpedientesManager;
import es.altia.agora.business.sge.persistence.DocumentosExpedienteManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.InteresadoExpedienteVO;
import es.altia.agora.business.sge.persistence.TramitesExpedientesManager;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.business.terceros.persistence.manual.DatosSuplementariosTerceroManager;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.ModuloIntegracionExternoManager;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraEtiquetaModuloIntegracionVO;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.sql.SQLException;


public class DocumentosRelacionExpedientesDAO
{
    
    protected static Config m_ConfigCommon; 
    protected static Config conf;
    protected static Log m_Log =
            LogFactory.getLog(DocumentosRelacionExpedientesDAO.class.getName());

    private static DocumentosRelacionExpedientesDAO instance = null;

    protected DocumentosRelacionExpedientesDAO()
    {
        super();
        conf = ConfigServiceHelper.getConfig("techserver");
        m_ConfigCommon = ConfigServiceHelper.getConfig("common");
    }

    public static DocumentosRelacionExpedientesDAO getInstance()
    {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null)
        {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized(DocumentosRelacionExpedientesDAO.class)
            {
                if (instance == null)
                    instance = new DocumentosRelacionExpedientesDAO();
            }
        }
        return instance;
    }

    public int grabarDocumento(GeneralValueObject gvo,String[] params) throws TechnicalException{
        int resultado;
        String numDoc = "", numDocExp;
        String codMunicipio     = (String)gvo.getAtributo("codMunicipio");
        String codProcedimiento = (String)gvo.getAtributo("codProcedimiento");
        String ejercicio        = (String)gvo.getAtributo("ejercicio");
        String numeroRelacion   = (String)gvo.getAtributo("numeroRelacion");
        String codTramite       = (String)gvo.getAtributo("codTramite");
        String ocurrenciaTramite= (String)gvo.getAtributo("ocurrenciaTramite");
        String codDocumento     = (String)gvo.getAtributo("codDocumento");
        String codUsuario       = (String)gvo.getAtributo("codUsuario");
        String nombreDocumento  = (String)gvo.getAtributo("nombreDocumento");
        String numeroDocumento  = (String)gvo.getAtributo("numeroDocumento");
        String opcionGrabar     = (String)gvo.getAtributo("opcionGrabar");
        byte[] ficheroWord      = (byte[])gvo.getAtributo("ficheroWord");

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        ResultSet rs;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            String sql;
            String codEstadoFirma = null;
            if ( ! (numeroDocumento!=null && !numeroDocumento.equals("")) ) {
                StringBuffer query = new StringBuffer("SELECT DOT_FRM FROM E_DOT WHERE ");
                query.append("(DOT_MUN=").append(codMunicipio).append(") AND ");
                query.append("(DOT_PRO='").append(codProcedimiento).append("') AND ");
                query.append("(DOT_TRA=").append(codTramite).append(") AND ");
                query.append("(DOT_COD=").append(codDocumento).append(")");
                Statement statement = conexion.createStatement();
                ResultSet resultSet = statement.executeQuery(query.toString());
                String tmp = null;
                if (resultSet.next()) {
                    tmp = resultSet.getString(1);
                }
                SigpGeneralOperations.closeResultSet(resultSet);
                SigpGeneralOperations.closeStatement(statement);
                if (tmp!=null) {
                    if (tmp.equalsIgnoreCase("O")) {
                        codEstadoFirma = "E";
                    } else {
                        codEstadoFirma = tmp.toUpperCase();
                    }
                }
            }            

            if(numeroDocumento!=null && !numeroDocumento.equals("")){
                sql = "UPDATE G_CRD "+
                        "SET CRD_FMO = " + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ", CRD_USM = ?, CRD_DES= ?, CRD_FIL = ? "+
                        "WHERE CRD_PRO = ? AND"+
                        "      CRD_EJE = ? AND"+
                        "      CRD_NUM = ? AND"+
                        "      CRD_TRA = ? AND"+
                        "      CRD_OCU = ? AND"+
                        "      CRD_NUD = ? ";
            } else {
                sql = "INSERT INTO G_CRD VALUES "+
                        "(?,?,?,?,?,?,?,"+ abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," +
                        abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ",?,?,?,?,?,?, NULL, NULL, NULL, ?)";
            }
            PreparedStatement stmt = conexion.prepareStatement(sql);
            if(numeroDocumento!=null && !numeroDocumento.equals("")){
                stmt.setString(1,codUsuario);
                stmt.setString(2,nombreDocumento);
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                stmt.setBinaryStream(3,st,ficheroWord.length);
                stmt.setString(4,codProcedimiento);
                stmt.setString(5,ejercicio);
                stmt.setString(6,numeroRelacion);
                stmt.setString(7,codTramite);
                stmt.setString(8,ocurrenciaTramite);
                stmt.setString(9,numeroDocumento);
            }else{
                numDoc = this.obtenMaximo(abd, gvo,params);                
                sql = "INSERT INTO G_CRD(CRD_MUN,CRD_PRO,CRD_EJE,CRD_NUM,CRD_TRA,CRD_OCU,CRD_NUD,CRD_FAL,CRD_FMO," +
                        "CRD_USM,CRD_USC,CRD_FIL,CRD_DES,CRD_DOT,CRD_EXP,CRD_FIR_EST,CRD_EXP_FD,CRD_DOC_FD,CRD_FIR_FD)VALUES "+
                        "(" + codMunicipio + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numeroRelacion + "'," + codTramite + "," + ocurrenciaTramite +
                        "," + numDoc + "," + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) +
                        "," + codUsuario + "," + codUsuario + ",?," + "'" + nombreDocumento + "',"  + codDocumento + ",'" +opcionGrabar + "',?,NULL,NULL,NULL)";

                m_Log.debug("==========> sql:  " + sql);
                stmt.close();
                stmt = conexion.prepareStatement(sql);                      
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                stmt.setBinaryStream(1,st,ficheroWord.length);

                if(codEstadoFirma!=null)
                    stmt.setString(2, codEstadoFirma);
                else
                    stmt.setNull(2, java.sql.Types.VARCHAR);               
            }
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resultado = stmt.executeUpdate();
            SigpGeneralOperations.closeStatement(stmt);

            if(numeroDocumento!=null && !numeroDocumento.equals("")){ 
                if(m_Log.isDebugEnabled()) m_Log.debug("MODIFICAR.  OPCION GRABAR ... " + opcionGrabar);
                if(opcionGrabar.equals("1")) {  //Todos los expedientes
                    //Recupero los expedientes
                    Vector<GeneralValueObject> exps = new Vector<GeneralValueObject>();
                    sql = "SELECT EXP_CRD_EJE, EXP_CRD_MUN, EXP_CRD_NUD, EXP_CRD_NUM, EXP_CRD_OCU, EXP_CRD_PRO, EXP_CRD_TRA" +
                            " FROM G_CRD_EXP WHERE REL_CRD_MUN = " + codMunicipio + " AND REL_CRD_PRO='" +
                            codProcedimiento + "' AND REL_CRD_EJE =" + ejercicio + " AND REL_CRD_NUM ='" +
                            numeroRelacion + "' AND REL_CRD_TRA =" + codTramite + " AND REL_CRD_OCU = " +
                            ocurrenciaTramite + " AND REL_CRD_NUD = " + numeroDocumento;
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    Statement stmt2 = conexion.createStatement();
                    rs = stmt2.executeQuery(sql);
                    while ( rs.next() ) {
                        GeneralValueObject exp = new GeneralValueObject();
                        exp.setAtributo("ejercicio", rs.getString("EXP_CRD_EJE"));
                        exp.setAtributo("municipio", rs.getString("EXP_CRD_MUN"));
                        exp.setAtributo("numDoc", rs.getString("EXP_CRD_NUD"));
                        exp.setAtributo("numExp", rs.getString("EXP_CRD_NUM"));
                        exp.setAtributo("ocurrencia", rs.getString("EXP_CRD_OCU"));
                        exp.setAtributo("procedimiento", rs.getString("EXP_CRD_PRO"));
                        exp.setAtributo("tramite", rs.getString("EXP_CRD_TRA"));
                        exps.add(exp);
                    }
                    SigpGeneralOperations.closeResultSet(rs);
                    SigpGeneralOperations.closeStatement(stmt2);

                    for (GeneralValueObject exp : exps) {
                        sql = "UPDATE E_CRD " +
                                "SET CRD_FMO = " + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ", CRD_USM = ?, CRD_DES= ?, CRD_FIL = ? " +
                                "WHERE CRD_PRO = ? AND" +
                                "      CRD_EJE = ? AND" +
                                "      CRD_NUM = ? AND" +
                                "      CRD_TRA = ? AND" +
                                "      CRD_OCU = ? AND" +
                                "      CRD_NUD = ? ";
                        stmt = conexion.prepareStatement(sql);
                        stmt.setString(1, codUsuario);
                        stmt.setString(2, nombreDocumento);
                        InputStream st = new ByteArrayInputStream(ficheroWord);
                        stmt.setBinaryStream(3, st, ficheroWord.length);
                        stmt.setString(4, (String) exp.getAtributo("procedimiento"));
                        stmt.setString(5, (String) exp.getAtributo("ejercicio"));
                        stmt.setString(6, (String) exp.getAtributo("numExp"));
                        stmt.setString(7, (String) exp.getAtributo("tramite"));
                        stmt.setString(8, (String) exp.getAtributo("ocurrencia"));
                        stmt.setString(9, (String) exp.getAtributo("numDoc"));
                        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                        resultado = stmt.executeUpdate();
                        SigpGeneralOperations.closeStatement(stmt2);
                    }
                }
            }else{
                if(opcionGrabar.equals("1")) { 

                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio",codMunicipio);
                    gVO.setAtributo("codProcedimiento",codProcedimiento);
                    gVO.setAtributo("ejercicio",ejercicio);
                    gVO.setAtributo("numero",numeroRelacion);
                    Vector expedientes = FichaRelacionExpedientesManager.getInstance().cargaListaExpedientes(gVO, params);
                    for (Object objExpediente : expedientes) {
                        GeneralValueObject expediente = (GeneralValueObject) objExpediente;
                        String numExp = (String) expediente.getAtributo("numExp");
                        m_Log.debug("EXPEDIENTE .... " + numExp);
                        gvo.setAtributo("numeroExpediente", numExp);
                        numDocExp = this.obtenMaximoExpediente(abd, gvo, params);
                        DocumentosExpedienteManager.getInstance().grabarDocumento(gvo, params);
                        sql = "INSERT INTO G_CRD_EXP VALUES " +
                                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        stmt = conexion.prepareStatement(sql);
                        stmt.setString(1, codMunicipio);
                        stmt.setString(2, codProcedimiento);
                        stmt.setString(3, ejercicio);
                        stmt.setString(4, numeroRelacion);
                        stmt.setString(5, codTramite);
                        stmt.setString(6, ocurrenciaTramite);
                        stmt.setString(7, numDoc);
                        stmt.setString(8, codMunicipio);
                        stmt.setString(9, codProcedimiento);
                        stmt.setString(10, ejercicio);
                        stmt.setString(11, numExp);
                        stmt.setString(12, codTramite);
                        stmt.setString(13, ocurrenciaTramite);
                        stmt.setString(14, numDocExp);
                        resultado = stmt.executeUpdate();
                        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                        SigpGeneralOperations.closeStatement(stmt);
                    }
                }
            }

            SigpGeneralOperations.commit(abd, conexion);

        }catch (BDException sqle){
            SigpGeneralOperations.rollBack(abd, conexion);
            resultado=0;
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("SQLException haciendo rollBackTransaction en: " + getClass().getName());
        }catch (SQLException sqle){
            SigpGeneralOperations.rollBack(abd, conexion);
            resultado=0;
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("SQLException haciendo rollBackTransaction en: " + getClass().getName());
        } catch (WSException sqle){
            SigpGeneralOperations.rollBack(abd, conexion);
            resultado=0;
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("SQLException haciendo rollBackTransaction en: " + getClass().getName());
        } finally {
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
        return resultado;
    }

    /* Elimina del string Dato los siguientes caracteres: ()/ºª@%&
* y no permite que dicho string comience por un numerico */
    private String eliminarCaracteresProhibidos(String dato)
    {
        StringTokenizer valores = null;
        String datoTokenizado = "";
        if (dato != null)
        {
            valores = new StringTokenizer(dato," ()/ºª@%&",false);
            while (valores.hasMoreTokens())
            {
                String valor = valores.nextToken();
                datoTokenizado += valor;
            }

            // Eliminar digitos del principio.

            boolean digPrincipio = true;
            while (digPrincipio && datoTokenizado.length()>0)
            {
                if (datoTokenizado.charAt(0)>='0' && datoTokenizado.charAt(0)<='9' )
                    datoTokenizado = datoTokenizado.substring(1);
                else digPrincipio = false;
            }
            if(m_Log.isDebugEnabled()) m_Log.debug("--> " + datoTokenizado);
        }
        return datoTokenizado;
    }
/////////////////////////////////////////////////////////////////////////
    
    public String consultaXML(GeneralValueObject gvo,String[] params) throws TechnicalException {

        m_Log.info("DocumentosRelacionExpedientesDAO. consultaXML. BEGIN:");

        //Codigo de la aplicacion
        String codAplicacion = (String)gvo.getAtributo("codAplicacion");

        String xml = "";
        String raiz = obtenEstructuraRaizRelacion(codAplicacion, params);
        EstructuraEntidades eeRaiz;

        String codOrganizacionM=(String)gvo.getAtributo("codMunicipio");
        String codTramite=(String)gvo.getAtributo("codTramite");
        String ocuTramite=(String)gvo.getAtributo("ocurrenciaTramite"); 
        String numExpedienteRelacion= (String) gvo.getAtributo("numeroRelacion");
                
        if(m_Log.isDebugEnabled()) m_Log.debug("codOrganizacionM:"+codOrganizacionM);
        if(m_Log.isDebugEnabled()) m_Log.debug("codTRAMITE:"+codTramite);
        if(m_Log.isDebugEnabled()) m_Log.debug("ocuTRAMITE:"+ocuTramite);
        if(m_Log.isDebugEnabled()) m_Log.debug("Numero de expediente de la relacion:"+numExpedienteRelacion);
       
        
        
        Vector<String> parametros = new Vector<String>();
        parametros.add((String)gvo.getAtributo("codProcedimiento"));
        parametros.add((String)gvo.getAtributo("numeroExpediente"));
        parametros.add((String)gvo.getAtributo("codTramite"));
        parametros.add((String)gvo.getAtributo("ocurrenciaTramite"));
        parametros.add((String)gvo.getAtributo("idioma"));

        String numeroRelacion = (String) gvo.getAtributo("numeroRelacion");


          /****** prueba ****/
         boolean formatoFechaAlternativo = false;
        String FORMATO_FECHA_ALTERNATIVO = null;
        try{
            ResourceBundle config = ResourceBundle.getBundle("documentos");
            FORMATO_FECHA_ALTERNATIVO = config.getString(((String)gvo.getAtributo("codMunicipio")) + ConstantesDatos.FORMATO_ALTERNATIVO_FECHAS_DOC_TRAMITACION);
            if(FORMATO_FECHA_ALTERNATIVO!=null && !"".equals(FORMATO_FECHA_ALTERNATIVO)){
                formatoFechaAlternativo = true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        DocumentosAplicacionDAO docDAO = DocumentosAplicacionDAO.getInstance();
        String codProcedimiento = (String)gvo.getAtributo("codProcedimiento");
        String codPlantilla     = (String)gvo.getAtributo("codPlantilla");
        String codMunicipio     = (String)gvo.getAtributo("codMunicipio");

        Vector etiquetasFecha = null;
        if(FORMATO_FECHA_ALTERNATIVO!=null && !"".equals(FORMATO_FECHA_ALTERNATIVO)){
            formatoFechaAlternativo = true;
            Hashtable<String,String> datosPlantilla = docDAO.getPlantillaDocumento(codPlantilla, codProcedimiento, params);            
            String porInteresado = datosPlantilla.get("POR_INTERESADO");
            String porRelacion   = datosPlantilla.get("POR_RELACION");

            gvo.setAtributo("relacion", porRelacion);
            gvo.setAtributo("interesado", porInteresado);
            try{
                etiquetasFecha = docDAO.loadEtiquetasFecha(gvo, params);
            }catch(Exception e){
                e.printStackTrace();
            }

        }// if

        /****** prueba ****/

        try{
            eeRaiz = UtilidadesXerador.construyeEstructuraEntidadesInforme(params, "", raiz);
            eeRaiz.setValoresParametrosConsulta(parametros);
            UtilidadesXerador.construyeEstructuraDatos(params, eeRaiz,etiquetasFecha);

            Vector listas = getValoresDatosSuplementarios(gvo,params);
            Vector estructuraDatosSuplementarios = (Vector) listas.firstElement();
            Vector valoresDatosSuplementarios = (Vector) listas.lastElement();
            Vector listaEstructuraInteresados = getEstructuraInteresados(gvo,params);
            java.util.Collection col = eeRaiz.getListaInstancias();

            for (Object objNodo : col) {
                NodoEntidad n = (NodoEntidad) objNodo;
                HashMap campos = n.getHijosPorTipo();
                String fecha = n.getCampo("FECACTGAL");
                if (fecha != null) {
                    n.remove("FECACTGAL");
                    n.addCampo("FECACTGAL", traducirFecha(fecha, "es", "GL"));
                }
                fecha = n.getCampo("FECACTESP");
                if (fecha != null) {
                    n.remove("FECACTESP");
                    n.addCampo("FECACTESP", traducirFecha(fecha, "es", "ES"));
                }

                //String fechaEu = n.getCampo("FECACTEU");
                if (fecha != null) {
                    n.remove("FECACTEU");
                    n.addCampo("FECACTEU", DateOperations.traducirFechaEuskera(fecha));
                }

                GeneralValueObject temp = new GeneralValueObject();
                temp.setAtributo("codProcedimiento", gvo.getAtributo("codProcedimiento"));
                temp.setAtributo("codMunicipio", gvo.getAtributo("codMunicipio"));
                temp.setAtributo("numero", numeroRelacion);
                temp.setAtributo("ejercicio", gvo.getAtributo("ejercicio"));
                if (m_Log.isDebugEnabled()) m_Log.debug("RELACION");
                if (m_Log.isDebugEnabled()) m_Log.debug("NUM_REL::" + temp.getAtributo("numero"));
                if (m_Log.isDebugEnabled()) m_Log.debug("COD_PRO::" + temp.getAtributo("codProcedimiento"));
                if (m_Log.isDebugEnabled()) m_Log.debug("COD_MUN::" + temp.getAtributo("codMunicipio"));
                if (m_Log.isDebugEnabled()) m_Log.debug("EJERCICIO ::" + temp.getAtributo("ejercicio"));
                m_Log.debug("CARGAR RELACION EXPEDIENTES --> INI cargaListaExpedientes");

                //EXPEDIENTES
                Vector expedientes = FichaRelacionExpedientesManager.getInstance().cargaListaExpedientes(temp, params);
                m_Log.debug("EXPEDIENTES : " + expedientes);
                m_Log.debug("CARGAR RELACION EXPEDIENTES --> FIN cargaListaExpedientes");
                int w = 187;
                char punto = (char) w;
                int x = 13;
                char newline = (char) x;
                int y = 45;
                char guion = (char) y;
                int z = 9;
                char tabulador = (char) z;
                String t = "";
                for (Object objExpediente : expedientes) {
                    GeneralValueObject temp2 = (GeneralValueObject) objExpediente;
                    String numExp = (String) temp2.getAtributo("numExp");
                    t += (punto + " " + numExp + newline);
                }
                n.remove("LISTADOEXPEDIENTES");
                n.addCampo("LISTADOEXPEDIENTES", t);


                // INTERESADOS
                Vector interesados = FichaRelacionExpedientesManager.getInstance().cargaListaInteresados(temp, params);
                m_Log.debug("INTERESADOS ____________________________________________________________________" + interesados);
                String t2 = "";
                for (int m = 0; m < interesados.size(); m++) {
                    GeneralValueObject inter = (GeneralValueObject) interesados.get(m);
                    m_Log.debug("INTERESADO " + m + " : " + inter.getAtributo("titular"));
                    t2 += (punto + " " + inter.getAtributo("titular") + newline);
                }
                n.remove("LISTADOINTERESADOS");
                n.addCampo("LISTADOINTERESADOS", t2);

                // INTERESADOS CON DNI
                Vector interesadosDNI = FichaRelacionExpedientesManager.getInstance().cargaListaInteresados(temp, params);
                m_Log.debug("INTERESADOS CON DNI ____________________________________________________________________" + interesadosDNI);
                String t4 = "";
                for (int m = 0; m < interesadosDNI.size(); m++) {
                    GeneralValueObject inter = (GeneralValueObject) interesadosDNI.get(m);
                    m_Log.debug("INTERESADO CON DNI " + m + " : " + inter.getAtributo("titular"));
                    t4 += (punto + " " + inter.getAtributo("documento") + " " + tabulador + " " + inter.getAtributo("titular") + newline);
                }
                n.remove("LISTADOINTERESADOSCONDNI");
                n.addCampo("LISTADOINTERESADOSCONDNI", t4);
                
                //Recuperamos en función de los ids de tercero recuperados en el vector interesados DNI la información adicional de los terceros
                TercerosManager tercerosManager = TercerosManager.getInstance();
                DatosSuplementariosTerceroManager datosSuplementariosManager = DatosSuplementariosTerceroManager.getInstance();
                
                
                String codOrganizacion = (String) gvo.getAtributo("codMunicipio");
                
                //Estructura datos suplementarios
                Vector<EstructuraCampo> datosSuplementarios = 
                        datosSuplementariosManager.cargaEstructuraDatosSuplementariosTercero(codOrganizacion, params);
                
                
                /***************** DATOS SUPLEMENTARIOS DE TERCERO ************************/                
                
                for (int m = 0; m < interesados.size(); m++) {
                    GeneralValueObject inter = (GeneralValueObject) interesados.get(m);
                    
                    String codTercero = (String)inter.getAtributo("idTercero");
                    m_Log.debug("****** idTercero: " + codTercero);
                    
                    Vector camposFormulario = datosSuplementariosManager.cargaValoresDatosSuplementariosConCodigo(codMunicipio,codTercero, datosSuplementarios, params);

                    for(EstructuraCampo datoSuplementario : datosSuplementarios){
                        String nombreCampo = datoSuplementario.getCodCampo();
                        String tipoDato = datoSuplementario.getCodTipoDato();
                        String codPlantillaTercero = datoSuplementario.getCodPlantilla();

                        for(int c=0; c<camposFormulario.size(); c++){
                            CamposFormulario campo = (CamposFormulario) camposFormulario.get(c);
                            HashMap campos2 =  campo.getCampos();

                            
                            Iterator itCampos = campos2.entrySet().iterator();
                            while(itCampos.hasNext()){
                                Map.Entry entry = (Map.Entry) itCampos.next();
                                String key = (String) entry.getKey();
                                if(nombreCampo.equalsIgnoreCase(key))
                                {  
                                   
                                    String valor = "";
                                    String valorOriginal = "";
                                    if(tipoDato.equalsIgnoreCase("6")){
                                        valor = datosSuplementariosManager.getValorDesplegable(codMunicipio, nombreCampo, (String)entry.getValue(), params);
                                    }else{
                                        valor = (String)entry.getValue();                                        
                                    }
                                    
                                    if(valor==null) valor ="";
                                    valorOriginal = valor;

                                    String valorDatoExistente = (String)n.getCampo(nombreCampo);
                                    if(valorDatoExistente!=null) valor = punto + valorDatoExistente + " " + punto + valor + newline;
                                    n.addCampo(nombreCampo,valor);

                                    if(formatoFechaAlternativo && codPlantillaTercero!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(codPlantillaTercero)){
                                        String nuevoCampoTokenizado = eliminarCaracteresProhibidos(nombreCampo + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                        String dato = (String)n.getCampo(nuevoCampoTokenizado);
                                        String salida = "";
                                        if(dato!=null) salida = punto + dato + " " + punto + DateOperations.formatoAlternativoCampoFecha(valorOriginal,FORMATO_FECHA_ALTERNATIVO) + newline;
                                        else
                                            salida = DateOperations.formatoAlternativoCampoFecha(valorOriginal,FORMATO_FECHA_ALTERNATIVO) + newline;

                                        n.addCampo(nuevoCampoTokenizado, salida);
                                    }
                                    break;
                                }
                            }
                        }//for camposformulario
                    }//for EstructuraCampo
                   
                }// for                                
                
               /***************** DATOS SUPLEMENTARIOS DE TERCERO ************************/                
             
                
                // INTERESADOS X EXPEDIENTE
                Vector interesadosxexpediente = FichaRelacionExpedientesManager.getInstance().cargaListaInteresadosxExpediente(temp, params);
                m_Log.debug("INTERESADOS x expediente ____________________________________________________________________" + interesadosxexpediente);
                String t3 = "";
                for(int m = 0; m < interesadosxexpediente.size(); m++){
                    GeneralValueObject tmp = (GeneralValueObject) interesadosxexpediente.get(m);
                    String exp = (String) tmp.getAtributo("expediente");
                    m_Log.debug("Expediente " + m + " : " + exp);
                    t3 += (punto + " " + exp + newline);
                    Vector listaInteresados = (Vector) tmp.getAtributo("interesados");
                    for(Object listaInteresado : listaInteresados){
                        t3 += tabulador + " " + tabulador + " " + tabulador + " " + tabulador + guion + " " + listaInteresado + newline;
                    }//for(Object listaInteresado : listaInteresados)
                }//for(int m = 0; m < interesadosxexpediente.size(); m++)
                n.remove("LISTADOINTERESADOSXEXPEDIENTE");
                n.addCampo("LISTADOINTERESADOSXEXPEDIENTE", t3);

                // Asunto
                GeneralValueObject relac = FichaRelacionExpedientesManager.getInstance().cargaRelacionExpedientes(temp, params);
                m_Log.debug("ASUNTO RELACIÓN " + relac.getAtributo("asuntoUnescape"));
                n.remove("ASUNTOEXPED");
                n.addCampo("ASUNTOEXPED", (String) relac.getAtributo("asuntoUnescape"));

                // Cargos
                Vector listaCargos = getListaCargos2(params, codAplicacion);
                for(int i = 0; i < listaCargos.size(); i++){
                    GeneralValueObject g = (GeneralValueObject) listaCargos.elementAt(i);
                    String cargo = (String) g.getAtributo("cargo");
                    String nombre = (String) g.getAtributo("nombre");
                    String cargoTokenizado = eliminarCaracteresProhibidos(cargo);
                    n.remove(cargoTokenizado);
                    n.addCampo(cargoTokenizado, nombre);
                }//for(int i = 0; i < listaCargos.size(); i++)
                
                // Interesados
                Vector listaInteresados = (Vector) campos.get("INTERESADO");
                if(listaInteresados != null){
                    for(int i = 0; i < listaInteresados.size(); i++){
                        NodoEntidad nInt = (NodoEntidad) listaInteresados.elementAt(i);
                        for(int j = 0; j < listaEstructuraInteresados.size(); j++){
                            EstructuraCampo eC = (EstructuraCampo) listaEstructuraInteresados.elementAt(j);
                            String descRolInter = eC.getCodCampo();
                            String descRolInterTokenizado = eliminarCaracteresProhibidos(descRolInter);

                            String rolInteresado = nInt.getCampo("ROLINTERESADO");
                            //Para el nombre del interesado
                            String nombreInt = nInt.getCampo("NOMBREINTERESADO");
                            //Para el documento del interesado
                            String documentoInt = nInt.getCampo("DOCUMENTO");
                            //Para el domicilio del interesado
                            String domicilio = nInt.getCampo("DOMICILIOINTERESADO");
                            String domicilioNoNormalizado = nInt.getCampo("DOMICILIONONORMALIZADO");
                            String normalizado = nInt.getCampo("NORMALIZADO");
                            String mailInt = nInt.getCampo("MAILINTERESADO");
                            String tlfnoInt = nInt.getCampo("TELEFONOINTERESADO");
                            String codPostalInt = nInt.getCampo("CODPOSTALINTERESADO");

                            String descDocInt = "Doc" + descRolInterTokenizado;
                            String descDomInt = "Dom" + descRolInterTokenizado;
                            //Para el rol del interesado
                            String descRol = "Rol" + descRolInterTokenizado;
                            //Para la poblacion del interesado
                            String descPobInt = "Pob" + descRolInterTokenizado;
                            String descTlfnoInt = "Tlfno" + descRolInterTokenizado;
                            String descMail = "Mail"+descRolInterTokenizado;
                            String descCodPostalInt = "CodPostal" + descRolInterTokenizado;

                            String poblacionInteresado = nInt.getCampo("POBLACIONINTERESADO");
                            if(rolInteresado.equals(descRolInter)){
                                //Para el nombre del interesado
                                nInt.addCampo(descRolInterTokenizado, nombreInt);
                                //Para el documento del interesado
                                if (m_Log.isDebugEnabled())
                                    m_Log.debug("DocumentosRelacionExpedientesDAO. documentoInt " + documentoInt);
                                nInt.addCampo(descDocInt, documentoInt);
                                //Para el domicilio del interesado
                                if(normalizado != null && normalizado.equals("2")){
                                    nInt.addCampo(descDomInt, domicilioNoNormalizado);
                                }else if (normalizado != null && normalizado.equals("1")){
                                    nInt.addCampo(descDomInt, domicilio);
                                }//if
                                //Para el rol del interesado
                                nInt.addCampo(descRol, rolInteresado);
                                //Para la poblacion del interesado
                                nInt.addCampo(descPobInt, poblacionInteresado);
                                nInt.addCampo(descTlfnoInt, tlfnoInt);
                                nInt.addCampo(descCodPostalInt, codPostalInt);
                                nInt.addCampo(descMail, mailInt);
                            }else{
                                nInt.addCampo(descRolInterTokenizado, "");
                                nInt.addCampo(descDocInt, "");
                                nInt.addCampo(descDomInt, "");
                                nInt.addCampo(descRol, "");
                                nInt.addCampo(descPobInt, "");
                                nInt.addCampo(descTlfnoInt, "");
                                nInt.addCampo(descCodPostalInt, "");
                                nInt.addCampo(descMail, "");
                            }//if(rolInteresado.equals(descRolInter))
                        }//for(int j = 0; j < listaEstructuraInteresados.size(); j++)
                    }//for(int i = 0; i < listaInteresados.size(); i++)
                }//if(listaInteresados != null)
                
                // Datos Suplementarios
                for (int i = 0; i < valoresDatosSuplementarios.size(); i++) {
                    CamposFormulario cF = (CamposFormulario) valoresDatosSuplementarios.elementAt(i);
                    EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                    String valorCampo = cF.getString(eC.getCodCampo());
                    if (eC.getCodTipoDato().equals("6")) valorCampo = getDescripcionValorDesplegable(eC, valorCampo);
                    if (valorCampo != null && !"".equals(valorCampo)) {
                        String campoTokenizado = eliminarCaracteresProhibidos(eC.getCodCampo());
                        n.addCampo(campoTokenizado, valorCampo);
                        // Si el campo suplementario es de tipo fecha y está habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                        // otro nombre y con el mismo valor que la original sólo que con el nuevo formato de fecha
                        if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                            String nuevoCampoTokenizado = eliminarCaracteresProhibidos(campoTokenizado + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                            n.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                        }//if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla()))
                    }//if (valorCampo != null && !"".equals(valorCampo)) 

                    String idCampoConPrefijo = "T" + gvo.getAtributo("codTramite") + eC.getCodCampo();
                    valorCampo = cF.getString(idCampoConPrefijo);
                    if (valorCampo != null && !"".equals(valorCampo)) {
                        String campoTokenizado = eliminarCaracteresProhibidos(idCampoConPrefijo);
                        n.addCampo(campoTokenizado, valorCampo);
                        // Si el campo suplementario es de tipo fecha y está habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                        // otro nombre y con el mismo valor que la original sólo que con el nuevo formato de fecha
                        if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                            String nuevoCampoTokenizado = eliminarCaracteresProhibidos(campoTokenizado + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                            n.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                        }//if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                    }//if (valorCampo != null && !"".equals(valorCampo)) 
                }//for (int i = 0; i < valoresDatosSuplementarios.size(); i++) 
                
              
              
              //Indicamos plantilla de Relacion->S
              ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasModulosExternos= 
                      getEtiquetasModulosExternos(codOrganizacionM,codProcedimiento,"N", "S", params);
              for (int i = 0; i < etiquetasModulosExternos.size(); i++) {
                 EstructuraEtiquetaModuloIntegracionVO etiqueta  = (EstructuraEtiquetaModuloIntegracionVO) 
                                                                        etiquetasModulosExternos.get(i);
                 String valor=ModuloIntegracionExternoManager.
                         getInstance().getValorDeEtiqueta(params, etiqueta,numExpedienteRelacion,codTramite, ocuTramite);
                 n.addCampo(etiqueta.getNombreEtiqueta(),valor);
                
              }
                
            
            }//Fin del While
            xml = new GeneradorInformesMgr(params).generaXMLResultado(eeRaiz);
        }catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("XML ::"+xml);
        return xml;
    }



   /////////////////////////////////////////////////////////////////////////////////////////  

    public String consultaXMLRelacion(GeneralValueObject gvo,String[] params){
       
        
        
        m_Log.info("DocumentosRelacionExpedientesDAO. consultaXMLRelacion. BEGIN:");
        String xml = "";
        String codAplicacion = (String)gvo.getAtributo("codAplicacion");
        // TODO: Comprobar la selección de la estructura raíz.
        //String raiz = obtenEstructuraRaiz(codAplicacion, params);
        //String raiz = "8";
        String raiz = "7";

        EstructuraEntidades eeRaiz;
        String codOrganizacionM=(String)gvo.getAtributo("codMunicipio");
        String codTramite=(String)gvo.getAtributo("codTramite");
        String ocuTramite=(String)gvo.getAtributo("ocurrenciaTramite"); 
       
        String codProcedimiento= (String) gvo.getAtributo("codProcedimiento");
        
        if(m_Log.isDebugEnabled()) m_Log.debug("codOrganizacionM:"+codOrganizacionM);
        if(m_Log.isDebugEnabled()) m_Log.debug("codTRAMITE:"+codTramite);
        if(m_Log.isDebugEnabled()) m_Log.debug("ocuTRAMITE:"+ocuTramite);
       if(m_Log.isDebugEnabled()) m_Log.debug("codProcedimiento:"+codProcedimiento);
        
        
        
        
        
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codProcedimiento", gvo.getAtributo("codProcedimiento"));
        gVO.setAtributo("codMunicipio", gvo.getAtributo("codMunicipio"));
        gVO.setAtributo("numero", gvo.getAtributo("numeroRelacion"));
        gVO.setAtributo("ejercicio", gvo.getAtributo("ejercicio"));

        Vector expedientes = FichaRelacionExpedientesManager.getInstance().cargaListaExpedientes(gVO, params);
        try {
            eeRaiz = UtilidadesXerador.construyeEstructuraEntidadesInforme(params, "", raiz);
            String fecActGal = "", fecActEsp = "", fechaActEu="";
            for (Object expediente : expedientes) {
                GeneralValueObject temp = (GeneralValueObject) expediente;
                String numExp = (String) temp.getAtributo("numExp");
                gvo.setAtributo("numeroExpediente", numExp);

                Vector<String> parametros = new Vector<String>();
                parametros.add((String) gvo.getAtributo("codProcedimiento"));
                parametros.add(numExp);
                parametros.add((String) gvo.getAtributo("codTramite"));
                parametros.add((String) gvo.getAtributo("ocurrenciaTramite"));
                parametros.add((String) gvo.getAtributo("idioma"));

                eeRaiz.setValoresParametrosConsulta(parametros);
                eeRaiz.setConsultaEjecutada(false);
                UtilidadesXerador.construyeEstructuraDatosRelacion(params, eeRaiz,null);
            }

            Collection colNodos = eeRaiz.getListaInstancias();
            for (Object objNodo : colNodos) {

                NodoEntidad nodo = (NodoEntidad) objNodo;
                m_Log.debug(nodo);

                // Añadimos información de la fecha actual tanto en gallego, castellano como en Euskera
                if (fecActGal.equals("")) {
                    fecActGal = nodo.getCampo("FECACTGAL");
                    fecActGal = traducirFecha(fecActGal, "es", "GL");
                }
                nodo.remove("FECACTGAL");
                nodo.addCampo("FECACTGAL", fecActGal);

                String fechaActual = fecActEsp;
                if (fecActEsp.equals("")) {
                    fecActEsp = nodo.getCampo("FECACTESP");
                    fecActEsp = traducirFecha(fecActEsp, "es", "ES");
                }
                nodo.remove("FECACTESP");
                nodo.addCampo("FECACTESP", fecActEsp);

                /*** euskera **/
                if (fechaActual.equals("")) {
                    fechaActEu = DateOperations.traducirFechaEuskera(fechaActual);
                }   
                nodo.remove("FECACTEU");
                nodo.addCampo("FECACTEU", fechaActEu);
                /*** euskera **/

                // Añadimos los cargos definidos.
                Vector listaCargos = getListaCargos2(params, codAplicacion);
                for (int i = 0; i < listaCargos.size(); i++) {
                    GeneralValueObject g = (GeneralValueObject) listaCargos.elementAt(i);
                    String cargo = (String) g.getAtributo("cargo");
                    String nombre = (String) g.getAtributo("nombre");

                    String cargoTokenizado = eliminarCaracteresProhibidos(cargo);
                    nodo.remove(cargoTokenizado);
                    nodo.addCampo(cargoTokenizado, nombre);
                }

                String numExpediente = nodo.getCampo("NUMEXPEDIENTE");
                m_Log.debug("ESTAMOS RELLENADO LA INFORMACIÓN DEL EXPEDIENTE: " + numExpediente);
                gvo.setAtributo("numeroExpediente", numExpediente);

                // Añadimos los Interesados
                Vector listaEstructuraInteresados = getEstructuraInteresados(gvo, params);
                HashMap campos = nodo.getHijosPorTipo();
                Vector listaInteresados = (Vector) campos.get("INTERESADO");

                if (listaInteresados != null) {
                    for (int i = 0; i < listaInteresados.size(); i++) {
                        NodoEntidad nInt = (NodoEntidad) listaInteresados.elementAt(i);
                        for (int j = 0; j < listaEstructuraInteresados.size(); j++) {
                            EstructuraCampo eC = (EstructuraCampo) listaEstructuraInteresados.elementAt(j);
                            String descRolInter = eC.getCodCampo();
                            String descRolInterTokenizado = eliminarCaracteresProhibidos(descRolInter);

                            String rolInteresado = nInt.getCampo("ROLINTERESADO");
                            //Para el nombre del interesado
                            String nombreInt = nInt.getCampo("NOMBREINTERESADO");
                            //Para el nombre del interesado
                            String nombreCompletoInt = nInt.getCampo("NOMBRECOMPLETO");
                            //Para el nombre del interesado
                            String apel1Int = nInt.getCampo("APELLIDO1INTERESADO");
                            //Para el nombre del interesado
                            String apel2Int = nInt.getCampo("APELLIDO2INTERESADO");
                            //Para el documento del interesado
                            String documentoInt = nInt.getCampo("DOCUMENTO");
                            //Para el domicilio del interesado
                            String domicilio = nInt.getCampo("DOMICILIOINTERESADO");
                            String domicilioNoNormalizado = nInt.getCampo("DOMICILIONONORMALIZADO");
                            String normalizado = nInt.getCampo("NORMALIZADO");
                            String tlfnoInt = nInt.getCampo("TELEFONOINTERESADO");
                            String codPostalInt = nInt.getCampo("CODPOSTALINTERESADO");
                            String mailInt = nInt.getCampo("MAILINTERESADO");

                            String descNombreInt = "Nombre" + descRolInterTokenizado;
                            String descApel1Int = "Apel1" + descRolInterTokenizado;
                            String descApel2Int = "Apel2" + descRolInterTokenizado;
                            String descDocInt = "Doc" + descRolInterTokenizado;
                            String descDomInt = "Dom" + descRolInterTokenizado;
                            //Para el rol del interesado
                            String descRol = "Rol" + descRolInterTokenizado;
                            //Para la poblacion del interesado
                            String descPobInt = "Pob" + descRolInterTokenizado;
                            String descTlfnoInt = "Tlfno" + descRolInterTokenizado;
                            String descCodPostal = "CodPostal" + descRolInterTokenizado;
                            String descMail = "Mail"+descRolInterTokenizado;

                            String poblacionInteresado = nInt.getCampo("POBLACIONINTERESADO");
                            if (rolInteresado.equals(descRolInter)) {
                                //Para el nombre del interesado
                                nInt.addCampo(descRolInterTokenizado, nombreCompletoInt);
                                nInt.addCampo(descNombreInt, nombreInt);
                                nInt.addCampo(descApel1Int, apel1Int);
                                nInt.addCampo(descApel2Int, apel2Int);
                                nInt.addCampo(descMail, mailInt);
                                //Para el documento del interesado
                                if (m_Log.isDebugEnabled())
                                    m_Log.debug("DocumentosExpedienteDAO. documentoInt " + documentoInt);
                                nInt.addCampo(descDocInt, documentoInt);
                                //Para el domicilio del interesado
                                if (normalizado != null && normalizado.equals("2")) {
                                    nInt.addCampo(descDomInt, domicilioNoNormalizado);
                                } else if (normalizado != null && normalizado.equals("1")) {
                                    nInt.addCampo(descDomInt, domicilio);
                                }
                                //Para el rol del interesado
                                nInt.addCampo(descRol, rolInteresado);
                                //Para la poblacion del interesado
                                nInt.addCampo(descPobInt, poblacionInteresado);
                                nInt.addCampo(descTlfnoInt, tlfnoInt);
                                nInt.addCampo(descCodPostal, codPostalInt);
                            } else {
                                nInt.addCampo(descRolInterTokenizado, "");
                                nInt.addCampo(descMail, "");
                                nInt.addCampo(descNombreInt, "");
                                nInt.addCampo(descApel1Int, "");
                                nInt.addCampo(descApel2Int, "");
                                nInt.addCampo(descDocInt, "");
                                nInt.addCampo(descDomInt, "");
                                nInt.addCampo(descRol, "");
                                nInt.addCampo(descPobInt, "");
                                nInt.addCampo(descTlfnoInt, "");
                                nInt.addCampo(descCodPostal, "");
                            }
                        }
                    }
                }

                // Datos Suplementarios
                Vector listas = getValoresDatosSuplementarios(gvo, params);
                Vector estructuraDatosSuplementarios = (Vector) listas.firstElement();
                Vector valoresDatosSuplementarios = (Vector) listas.lastElement();

                for (int i = 0; i < valoresDatosSuplementarios.size(); i++) {
                    CamposFormulario cF = (CamposFormulario) valoresDatosSuplementarios.elementAt(i);
                    EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);

                    String valorCampo = cF.getString(eC.getCodCampo());
                    if (eC.getCodTipoDato().equals("6")) valorCampo = getDescripcionValorDesplegable(eC, valorCampo);

                    if (valorCampo != null && !"".equals(valorCampo)) {
                        String campoTokenizado = eliminarCaracteresProhibidos(eC.getCodCampo());
                        nodo.addCampo(campoTokenizado, valorCampo);
                    }

                    String idCampoConPrefijo = "T" + gvo.getAtributo("codTramite") + eC.getCodCampo();
                    valorCampo = cF.getString(idCampoConPrefijo);
                    if (eC.getCodTipoDato().equals("6")) valorCampo = getDescripcionValorDesplegable(eC, valorCampo);

                    if (valorCampo != null && !"".equals(valorCampo)) {
                        String campoTokenizado = eliminarCaracteresProhibidos(idCampoConPrefijo);
                        nodo.addCampo(campoTokenizado, valorCampo);
                    }
                }
                
                
                   
                //Etiquetas que vienen de los módulos externos
                //Indicamos plantilla Relación N
                //Plantilla Interesado N
              ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasModulosExternos= getEtiquetasModulosExternos(codOrganizacionM,codProcedimiento,"N", "N", params);
              for (int i = 0; i < etiquetasModulosExternos.size(); i++) {
                 EstructuraEtiquetaModuloIntegracionVO etiqueta  = (EstructuraEtiquetaModuloIntegracionVO) etiquetasModulosExternos.get(i);
                
                 //Ahora tenemos que recuperar el valor de la etiqueta, y anhadirla..
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es el numExpediente pasado: "+ numExpediente);
                 String valor=ModuloIntegracionExternoManager.getInstance().getValorDeEtiqueta(params, etiqueta, numExpediente,codTramite, ocuTramite);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es numExpediente: "+numExpediente);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es codTramite: "+codTramite);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es codTramite: "+ocuTramite);
                 nodo.addCampo(etiqueta.getNombreEtiqueta(),valor);
                
              }
                
               if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es nodo"+nodo); 
            }

            //xml = new GeneradorInformesMgr(params).generaXMLResultado2(eeRaiz);
            xml = new GeneradorInformesMgr(params).generaXMLResultadoPorExpediente(eeRaiz);

        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
        }
        m_Log.debug("XML ::" + xml);
        return xml;
    }



    /**
     * Se recuperan los interesados de un expediente
     * @param numExpediente: Número de expediente
     * @param params: Parámetros de conexión a la base de datos
     * @return Vector que contiene objetos de la clase NodoEntidad
     */
    public Vector getConsultaInteresadosExpediente(String numExpediente,String[] params) throws TechnicalException{

        Vector interesados = new Vector();
        String sql = "SELECT v_int.int_dce as mailinteresado,v_int.int_nom AS nombreinteresado, v_int.int_noc AS nombrecompleto, "
                + "v_int.int_ap1 AS apellido1interesado, v_int.int_ap2 AS apellido2interesado, v_int.int_num AS numeroexpediente,"
                + "v_int.int_domn AS domiciliointeresado, v_int.int_rol AS rolinteresado,v_int.int_nor AS normalizado,"
                + "v_int.int_domnn AS domiciliononormalizado,v_int.int_pob AS poblacioninteresado, v_int.int_doc AS documento,"
                + "v_int.int_tlf AS telefonointeresado, v_int.int_rca AS refcatastral,v_int.int_prv AS provinteresado, "
                + "v_int.int_cpo as codpostalinteresado FROM v_int " 
                + "WHERE INT_NUM='" + numExpediente + "'";
        Statement st = null;
        ResultSet rs = null;
        Connection con = null;
        AdaptadorSQLBD adaptador = new AdaptadorSQLBD(params);
         try{            
            con = adaptador.getConnection();
            if(con!=null){
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next()){
                    NodoEntidad nodo = new NodoEntidad();
                    String rol = rs.getString("rolinteresado");
                    String nombre = rs.getString("nombreinteresado");
                    String nombreCompleto = rs.getString("nombrecompleto");
                    String apellido1 = rs.getString("apellido1interesado");
                    String apellido2 = rs.getString("apellido2interesado");
                    String documento = rs.getString("documento");
                    String domicilio  = rs.getString("domiciliointeresado");
                    String domicilioNoNormalizado = rs.getString("domiciliononormalizado");
                    String normalizado = rs.getString("normalizado");
                    String telefono = rs.getString("telefonointeresado");
                    String codPostal = rs.getString("codpostalinteresado");
                    String provincia = rs.getString("provinteresado");
                    String poblacion = rs.getString("poblacioninteresado");
                    String catastral = rs.getString("refcatastral");
                    String mail = rs.getString("mailinteresado");

                    if (mail!=null)
                        nodo.addCampo("MAILINTERESADO", mail);
                    else
                        nodo.addCampo("MAILINTERESADO", "");
                    if(rol!=null)
                        nodo.addCampo("ROLINTERESADO",rol);
                    else
                        nodo.addCampo("ROLINTERESADO","");
                    if(nombre!=null)
                        nodo.addCampo("NOMBREINTERESADO",nombre);
                    else
                        nodo.addCampo("NOMBREINTERESADO","");
                    if(nombreCompleto!=null)
                        nodo.addCampo("NOMBRECOMPLETO",nombreCompleto);
                    else
                        nodo.addCampo("NOMBRECOMPLETO","");
                    if(apellido1!=null)
                        nodo.addCampo("APELLIDO1INTERESADO",apellido1);
                    else
                        nodo.addCampo("APELLIDO1INTERESADO","");
                    if(apellido2!=null)
                        nodo.addCampo("APELLIDO2INTERESADO",apellido2);
                    else
                        nodo.addCampo("APELLIDO2INTERESADO","");
                    if(documento!=null)
                        nodo.addCampo("DOCUMENTO",documento);
                    else
                        nodo.addCampo("DOCUMENTO","");
                    if(domicilio!=null)
                        nodo.addCampo("DOMICILIOINTERESADO",domicilio);
                    else
                        nodo.addCampo("DOMICILIOINTERESADO","");
                    if(domicilioNoNormalizado!=null)
                        nodo.addCampo("DOMICILIONONORMALIZADO",domicilioNoNormalizado);
                    else
                        nodo.addCampo("DOMICILIONONORMALIZADO","");
                    if(normalizado!=null)
                        nodo.addCampo("NORMALIZADO",normalizado);
                    else
                        nodo.addCampo("NORMALIZADO","");
                    if(telefono!=null)
                        nodo.addCampo("TELEFONOINTERESADO",telefono);
                    else
                        nodo.addCampo("TELEFONOINTERESADO","");
                    if(codPostal!=null)
                        nodo.addCampo("CODPOSTALINTERESADO",codPostal);
                    else
                        nodo.addCampo("CODPOSTALINTERESADO","");
                    if(provincia!=null)
                        nodo.addCampo("PROVINTERESADO",provincia);
                    else
                        nodo.addCampo("PROVINTERESADO","");

                    nodo.addCampo("NUMEROEXPEDIENTE",numExpediente);

                    if(poblacion!=null)
                        nodo.addCampo("POBLACIONINTERESADO",poblacion);
                    else
                        nodo.addCampo("POBLACIONINTERESADO","");
                    if(catastral!=null)
                        nodo.addCampo("REFCATASTRAL",catastral);
                    else
                        nodo.addCampo("REFCATASTRAL","");
                    
                    nodo.setNomeEntidade("INTERESADO");
                    interesados.add(nodo);
                         
                }// while
            }

        }catch(Exception e){
            m_Log.error("getConsultaDatosExpediente " + e.getMessage());
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
               SigpGeneralOperations.closeStatement(st);
               SigpGeneralOperations.devolverConexion(adaptador, con);
        }

        return interesados;
    }

    
     private Vector getListaCargos(String[] params, String codAplicacion) throws TechnicalException {
        AdaptadorSQLBD abd = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        Vector lista = new Vector();

        try{
            abd = new AdaptadorSQLBD(params);
            con = abd.getConnection();
            st = con.createStatement();

            sql.append("SELECT "+abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'TITULOCARGO'",
                    abd.convertir("E_CAR.CAR_COD",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null)}))
            .append(" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
            .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
            .append(" A_DOC.DOC_CEI = 1 AND ")
            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
            .append(" UNION SELECT "+abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'NOMBRECARGO'",
                    abd.convertir("E_CAR.CAR_COD",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_DES AS NOME ")
            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
            .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
            .append(" A_DOC.DOC_CEI = 1 AND ")
            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
            .append(" UNION SELECT "+abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'TRATAMIENTOCARGO'",
                    abd.convertir("E_CAR.CAR_COD",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_TRA AS NOME ")
            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
            .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
            .append(" A_DOC.DOC_CEI = 1 AND ")
            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO");

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql.toString());
            while(rs.next()){
                GeneralValueObject g = new GeneralValueObject();
                String cargo = rs.getString("CODIGO");
                g.setAtributo("cargo",cargo);
                String nombre = rs.getString("NOME");
                g.setAtributo("nombre",nombre);
                lista.addElement(g);
                m_Log.debug("CARGO "+cargo);
                m_Log.debug("NOMBRE "+nombre);
            }
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
        }finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(abd, con);
            return lista;
        }
    }

   

    /**
     * Genera el xml para dar de alta un documento normal en una relación
     * @param gvo: GeneralValueObject
     * @param params: Parámetros de conexión a la base de datos
     * @return String con el xml resultado de la consulta.   */
     public String consultaXMLRelacionDocumentoNormal(GeneralValueObject gvo,String[] params){
        m_Log.info("DocumentosRelacionExpedientesDAO. consultaXMLRelacionDocumentoNormal. BEGIN:");
        String xml = "";
        String codAplicacion = (String)gvo.getAtributo("codAplicacion");        
        String raiz = "7";

        TercerosManager tercerosManager = TercerosManager.getInstance();
        DatosSuplementariosTerceroManager datosSuplementariosManager = DatosSuplementariosTerceroManager.getInstance();
        EstructuraEntidades eeRaiz;

        String codOrganizacionM=(String)gvo.getAtributo("codMunicipio");
        String codTramiteM=(String)gvo.getAtributo("codTramite");
        String ocuTramiteM=(String)gvo.getAtributo("ocurrenciaTramite"); 
        String codProcedimientoM= (String) gvo.getAtributo("codProcedimiento");
        
        if(m_Log.isDebugEnabled()) m_Log.debug("codOrganizacionM:"+codOrganizacionM);
        if(m_Log.isDebugEnabled()) m_Log.debug("codTRAMITE:"+codTramiteM);
        if(m_Log.isDebugEnabled()) m_Log.debug("ocuTRAMITE:"+ocuTramiteM);
        if(m_Log.isDebugEnabled()) m_Log.debug("codProcedimiento:"+codProcedimientoM);
        
        
        
        
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codProcedimiento", gvo.getAtributo("codProcedimiento"));
        gVO.setAtributo("codMunicipio", gvo.getAtributo("codMunicipio"));
        gVO.setAtributo("numero", gvo.getAtributo("numeroRelacion"));
        gVO.setAtributo("ejercicio", gvo.getAtributo("ejercicio"));

        boolean formatoFechaAlternativo = false;
        String FORMATO_FECHA_ALTERNATIVO = null;
        try{
            ResourceBundle config = ResourceBundle.getBundle("documentos");
            FORMATO_FECHA_ALTERNATIVO = config.getString(((String)gvo.getAtributo("codMunicipio")) + ConstantesDatos.FORMATO_ALTERNATIVO_FECHAS_DOC_TRAMITACION);
            if(FORMATO_FECHA_ALTERNATIVO!=null && !"".equals(FORMATO_FECHA_ALTERNATIVO)){
                formatoFechaAlternativo = true;
            }//if(FORMATO_FECHA_ALTERNATIVO!=null && !"".equals(FORMATO_FECHA_ALTERNATIVO))
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch

        DocumentosAplicacionDAO docDAO = DocumentosAplicacionDAO.getInstance();
        String codProcedimiento = (String)gvo.getAtributo("codProcedimiento");
        String codPlantilla     = (String)gvo.getAtributo("codPlantilla");
        String codMunicipio     = (String)gvo.getAtributo("codMunicipio");

        Vector etiquetasFecha = null;
        if(FORMATO_FECHA_ALTERNATIVO!=null && !"".equals(FORMATO_FECHA_ALTERNATIVO)){
            formatoFechaAlternativo = true;
            Hashtable<String,String> datosPlantilla = docDAO.getPlantillaDocumento(codPlantilla, codProcedimiento, params);
            String codTramite    = datosPlantilla.get("COD_TRAMITE");
            String porInteresado = datosPlantilla.get("POR_INTERESADO");
            String porRelacion   = datosPlantilla.get("POR_RELACION");

            gvo.setAtributo("relacion", porRelacion);
            gvo.setAtributo("interesado", porInteresado);
            try{
                etiquetasFecha = docDAO.loadEtiquetasFecha(gvo, params);
            }catch(Exception e){
                e.printStackTrace();
            }//try-catch
        }// if

        Vector expedientes = FichaRelacionExpedientesManager.getInstance().cargaListaExpedientes(gVO, params);
        try {
            eeRaiz = UtilidadesXerador.construyeEstructuraEntidadesInforme(params, "", raiz);
            String fecActGal = "", fecActEsp = "", fechaActEu="";
            for (Object expediente : expedientes) {
                GeneralValueObject temp = (GeneralValueObject) expediente;
                String numExp = (String) temp.getAtributo("numExp");
                gvo.setAtributo("numeroExpediente", numExp);

                Vector<String> parametros = new Vector<String>();
                parametros.add((String) gvo.getAtributo("codProcedimiento"));
                parametros.add(numExp);
                parametros.add((String) gvo.getAtributo("codTramite"));
                parametros.add((String) gvo.getAtributo("ocurrenciaTramite"));
                parametros.add((String) gvo.getAtributo("idioma"));

                eeRaiz.setValoresParametrosConsulta(parametros);
                eeRaiz.setConsultaEjecutada(false);
                UtilidadesXerador.construyeEstructuraDatosRelacion(params, eeRaiz,etiquetasFecha);
            }//for (Object expediente : expedientes) 

            Collection colNodos = eeRaiz.getListaInstancias();
            String fechaActual = "";
            for (Object objNodo : colNodos) {

                NodoEntidad nodo = (NodoEntidad) objNodo;
                m_Log.debug(nodo);

                // Añadimos información de la fecha actual tanto en gallego como en castellano.
                if (fecActGal.equals("")) {
                    fecActGal = nodo.getCampo("FECACTGAL");
                    fecActGal = traducirFecha(fecActGal, "es", "GL");
                }//if (fecActGal.equals("")) 
                nodo.remove("FECACTGAL");
                nodo.addCampo("FECACTGAL", fecActGal);

                
                if (fecActEsp.equals("")) {                    
                    fecActEsp = nodo.getCampo("FECACTESP");
                    fechaActual = fecActEsp;
                    fecActEsp = traducirFecha(fecActEsp, "es", "ES");
                }//if (fecActEsp.equals(""))
                nodo.remove("FECACTESP");
                nodo.addCampo("FECACTESP", fecActEsp);

                /** euskera **/
                if (!fechaActual.equals("")) {
                    fechaActEu = DateOperations.traducirFechaEuskera(fechaActual);
                }//if (!fechaActual.equals(""))
                nodo.remove("FECACTEU");
                nodo.addCampo("FECACTEU", fechaActEu);
                /** euskera **/

                // Añadimos los cargos definidos.
                Vector listaCargos = getListaCargos2(params, codAplicacion);
                for (int i = 0; i < listaCargos.size(); i++) {
                    GeneralValueObject g = (GeneralValueObject) listaCargos.elementAt(i);
                    String cargo = (String) g.getAtributo("cargo");
                    String nombre = (String) g.getAtributo("nombre");

                    String cargoTokenizado = eliminarCaracteresProhibidos(cargo);
                    nodo.remove(cargoTokenizado);
                    nodo.addCampo(cargoTokenizado, nombre);
                }//for (int i = 0; i < listaCargos.size(); i++) 

                String numExpediente = nodo.getCampo("NUMEXPEDIENTE");
                m_Log.debug("ESTAMOS RELLENADO LA INFORMACIÓN DEL EXPEDIENTE: " + numExpediente);
                gvo.setAtributo("numeroExpediente", numExpediente);

                // Añadimos los Interesados
                Vector listaEstructuraInteresados = getEstructuraInteresados(gvo, params);
                HashMap campos = nodo.getHijosPorTipo();
                //Vector listaInteresados = (Vector) campos.get("INTERESADO");
                
                campos.put("INTERESADO",this.getConsultaInteresadosExpediente(numExpediente, params));
                nodo.setHijosPorTipo(campos);
                campos = nodo.getHijosPorTipo();
                
                ArrayList<TercerosValueObject> terceros = new ArrayList<TercerosValueObject>();
                String[] propsNumExp = numExpediente.split("/");
                String ejercicio = propsNumExp[0];
                terceros = tercerosManager.getInteresadosExpediente(codMunicipio, numExpediente, codProcedimiento, ejercicio, params);
                
                //Estructura datos suplementarios
                Vector<EstructuraCampo> datosSuplementarios = 
                datosSuplementariosManager.cargaEstructuraDatosSuplementariosTercero(codMunicipio, params);
                
                Vector listaInteresados = (Vector)campos.get("INTERESADO");
                if (listaInteresados != null) {
                    for (int i = 0; i < listaInteresados.size(); i++) {
                        NodoEntidad nInt = (NodoEntidad) listaInteresados.elementAt(i);
                        for (int j = 0; j < listaEstructuraInteresados.size(); j++) {
                            EstructuraCampo eC = (EstructuraCampo) listaEstructuraInteresados.elementAt(j);
                            String descRolInter = eC.getCodCampo();
                            String descRolInterTokenizado = eliminarCaracteresProhibidos(descRolInter);

                            String rolInteresado = nInt.getCampo("ROLINTERESADO");
                            //Para el nombre del interesado
                            String nombreInt = nInt.getCampo("NOMBREINTERESADO");
                            //Para el nombre del interesado
                            String nombreCompletoInt = nInt.getCampo("NOMBRECOMPLETO");
                            //Para el nombre del interesado
                            String apel1Int = nInt.getCampo("APELLIDO1INTERESADO");
                            //Para el nombre del interesado
                            String apel2Int = nInt.getCampo("APELLIDO2INTERESADO");
                            //Para el documento del interesado
                            String documentoInt = nInt.getCampo("DOCUMENTO");
                            //Para el domicilio del interesado
                            String domicilio = nInt.getCampo("DOMICILIOINTERESADO");
                            String domicilioNoNormalizado = nInt.getCampo("DOMICILIONONORMALIZADO");
                            String normalizado = nInt.getCampo("NORMALIZADO");
                            String tlfnoInt = nInt.getCampo("TELEFONOINTERESADO");
                            String codPostalInt = nInt.getCampo("CODPOSTALINTERESADO");
                            String descPrv = nInt.getCampo("PROVINTERESADO");
                            String mailInt = nInt.getCampo("MAILINTERESADO");                            

                            String descNombreInt = "Nombre" + descRolInterTokenizado;
                            String descApel1Int = "Apel1" + descRolInterTokenizado;
                            String descApel2Int = "Apel2" + descRolInterTokenizado;
                            String descDocInt = "Doc" + descRolInterTokenizado;
                            String descDomInt = "Dom" + descRolInterTokenizado;
                            String descMail = "Mail"+descRolInterTokenizado;
                            //Para el rol del interesado
                            String descRol = "Rol" + descRolInterTokenizado;
                            //Para la poblacion del interesado
                            String descPobInt = "Pob" + descRolInterTokenizado;
                            String descTlfnoInt = "Tlfno" + descRolInterTokenizado;
                            String descCodPostal = "CodPostal" + descRolInterTokenizado;
                            String descProInt = "Provincia" + descRolInterTokenizado;
                            
                            String poblacionInteresado = nInt.getCampo("POBLACIONINTERESADO");
                            if (rolInteresado.equals(descRolInter)) {
                                //Para el nombre del interesado
                                nInt.addCampo(descRolInterTokenizado, nombreCompletoInt);
                                nInt.addCampo(descNombreInt, nombreInt);
                                nInt.addCampo(descApel1Int, apel1Int);
                                nInt.addCampo(descApel2Int, apel2Int);
                                nInt.addCampo(descMail, mailInt);
                                //Para el documento del interesado
                                if (m_Log.isDebugEnabled())
                                    m_Log.debug("DocumentosExpedienteDAO. documentoInt " + documentoInt);
                                nInt.addCampo(descDocInt, documentoInt);
                                
                                //Para el domicilio del interesado
                                if (normalizado != null && normalizado.equals("2")) {
                                    nInt.addCampo(descDomInt, domicilioNoNormalizado);
                                } else if (normalizado != null && normalizado.equals("1")) {
                                    nInt.addCampo(descDomInt, domicilio);
                                }//if
                                
                                //Para el rol del interesado
                                nInt.addCampo(descRol, rolInteresado);
                                //Para la poblacion del interesado
                                nInt.addCampo(descPobInt, poblacionInteresado);
                                nInt.addCampo(descTlfnoInt, tlfnoInt);
                                nInt.addCampo(descCodPostal, codPostalInt);
                                nInt.addCampo(descProInt, descPrv);
                                
                                for(TercerosValueObject tercero : terceros){
                                    if(tercero.getDocumento().equalsIgnoreCase(documentoInt)){
                                        //CamposFormulario
                                        Vector camposFormulario = datosSuplementariosManager.
                                                        cargaValoresDatosSuplementariosConCodigo(codMunicipio, tercero.getIdentificador(), 
                                                        datosSuplementarios, params);

                                        for(EstructuraCampo datoSuplementario : datosSuplementarios){
                                            String nombreCampo = datoSuplementario.getCodCampo();
                                            String tipoDato = datoSuplementario.getCodTipoDato();
                                            String codPlantillaTercero = datoSuplementario.getCodPlantilla();

                                            for(int z=0; z<camposFormulario.size(); z++){
                                                    CamposFormulario campo = (CamposFormulario) camposFormulario.get(z);
                                                    HashMap camposSuplementarios =  campo.getCampos();
                                                    Iterator itCampos = camposSuplementarios.entrySet().iterator();
                                                    
                                                    while(itCampos.hasNext()){
                                                        Map.Entry entry = (Map.Entry) itCampos.next();
                                                        String key = (String) entry.getKey();
                                                        String dato = "";
                                                        if(key.equalsIgnoreCase(nombreCampo)){
                                                            
                                                            if(tipoDato.equalsIgnoreCase("6")){
                                                                dato = datosSuplementariosManager.getValorDesplegable(codMunicipio, nombreCampo, (String) entry.getValue(), params);
                                                            }else{
                                                                dato = (String) entry.getValue();
                                                            }//if(tipoDato.equalsIgnoreCase("6"))
                                                            
                                                            if(dato==null) dato="";
                                                            nInt.addCampo(nombreCampo+descRolInterTokenizado, dato);
                                                            
                                                            /****** FECHA ALTERNATIVA PARA DATOS SUPLEMENTARIOS DE TIPO FECHA ***/
                                                            if(formatoFechaAlternativo && codPlantillaTercero!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(codPlantillaTercero)){
                                                                String nuevoCampoTokenizado = eliminarCaracteresProhibidos(nombreCampo+descRolInterTokenizado + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                                                nInt.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(dato,FORMATO_FECHA_ALTERNATIVO));
                                                            }                                                            
                                                            
                                                            /****** FECHA ALTERNATIVA PARA DATOS SUPLEMENTARIOS DE TIPO FECHA ***/
                                                            break;
                                                        }//if(key.equalsIgnoreCase(nombreCampo))
                                                    }//while(itCampos.hasNext())
                                                   
                                                    
                                            }//for(int z=0; z<camposFormulario.size(); z++)
                                        }//for(EstructuraCampo datoSuplementario : datosSuplementarios)
                                    }//if(tercero.getDocumento().equalsIgnoreCase(documentoInt))
                                }//for(TercerosValueObject tercero : terceros)
                            } else {
                                nInt.addCampo(descRolInterTokenizado, "");
                                nInt.addCampo(descNombreInt, "");
                                nInt.addCampo(descApel1Int, "");
                                nInt.addCampo(descApel2Int, "");
                                nInt.addCampo(descDocInt, "");
                                nInt.addCampo(descMail, "");
                                nInt.addCampo(descDomInt, "");
                                nInt.addCampo(descRol, "");
                                nInt.addCampo(descPobInt, "");
                                nInt.addCampo(descTlfnoInt, "");
                                nInt.addCampo(descCodPostal, "");
                                nInt.addCampo(descProInt, "");
                                for(EstructuraCampo datoSuplementario : datosSuplementarios){
                                    String nombreCampo = datoSuplementario.getCodCampo();
                                    nInt.addCampo(nombreCampo+descRolInterTokenizado, "");
                                }//for(EstructuraCampo datoSuplementario : datosSuplementarios)
                            }//if (rolInteresado.equals(descRolInter))
                        }//for (int j = 0; j < listaEstructuraInteresados.size(); j++)
                    }//for (int i = 0; i < listaInteresados.size(); i++) 
                }//if (listaInteresados != null) 

                // DATOS SUPLEMENTARIOS DE LOS TRÁMITES
                ArrayList<GeneralValueObject> tramitesExpediente = TramitesExpedientesManager.getInstance().getOcurrenciaTramiteExpediente(gvo, params);
                m_Log.debug(" ===========> Nº de tramites del expediente: " + tramitesExpediente.size());

                for(int h=0;tramitesExpediente!=null && h<tramitesExpediente.size();h++){
                    GeneralValueObject tramiteActual = (GeneralValueObject)tramitesExpediente.get(h);
                    Integer codTramiteActual = (Integer)tramiteActual.getAtributo("codTramite");
                    Integer ocuTramiteActual = (Integer)tramiteActual.getAtributo("ocurrenciaTramite");

                    gvo.setAtributo("codTramite",codTramiteActual.toString());
                    gvo.setAtributo("ocurrenciaTramite",ocuTramiteActual.toString());

                    // ORIGINAL
                    //Vector listas = getValoresDatosSuplementarios(gvo, params);
                    Vector listas = getValoresDatosSuplementariosTramiteSinFiltrar(gvo, params);
                    Vector estructuraDatosSuplementarios = (Vector) listas.firstElement();
                    Vector valoresDatosSuplementarios = (Vector) listas.lastElement();

                    for (int i = 0; i < valoresDatosSuplementarios.size(); i++) {
                        CamposFormulario cF = (CamposFormulario) valoresDatosSuplementarios.elementAt(i);
                        EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                        String valorCampo = cF.getString(eC.getCodCampo());
                        if (eC.getCodTipoDato().equals("6")) valorCampo = getDescripcionValorDesplegable(eC, valorCampo);
                        if (valorCampo != null && !"".equals(valorCampo)){
                            String campoTokenizado = eliminarCaracteresProhibidos(eC.getCodCampo());
                            nodo.addCampo(campoTokenizado, valorCampo);
                            // Si el campo suplementario es de tipo fecha y está habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                            // otro nombre y con el mismo valor que la original sólo que con el nuevo formato de fecha
                            if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                                String nuevoCampoTokenizado = eliminarCaracteresProhibidos(campoTokenizado + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                nodo.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                            }//if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla()))
                        }//if (valorCampo != null && !"".equals(valorCampo)) 

                        String idCampoConPrefijo = "T" + gvo.getAtributo("codTramite") + eC.getCodCampo();
                        valorCampo = cF.getString(idCampoConPrefijo);
                        if (eC.getCodTipoDato().equals("6")) valorCampo = getDescripcionValorDesplegable(eC, valorCampo);
                        if (valorCampo != null && !"".equals(valorCampo)) {
                            String campoTokenizado = eliminarCaracteresProhibidos(idCampoConPrefijo);
                            nodo.addCampo(campoTokenizado, valorCampo);
                            // Si el campo suplementario es de tipo fecha y está habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                            // otro nombre y con el mismo valor que la original sólo que con el nuevo formato de fecha
                            if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                                String nuevoCampoTokenizado = eliminarCaracteresProhibidos(campoTokenizado + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                nodo.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                            }//if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla()))
                            /** prueba ***/
                        }//if (valorCampo != null && !"".equals(valorCampo)) 
                    }//for (int i = 0; i < valoresDatosSuplementarios.size(); i++)
                    m_Log.debug(nodo);
                }//for(int h=0;tramitesExpediente!=null && h<tramitesExpediente.size();h++)

                
                // CAMPOS SUPLEMENTARIOS A NIVEL DE PROCEDIMIENTO                
                Vector listas = getValoresDatosSuplementariosProcedimientoSinFiltrar(gvo, params);
                Vector estructuraDatosSuplementarios = (Vector) listas.firstElement();
                Vector valoresDatosSuplementarios = (Vector) listas.lastElement();

                for (int i = 0; i < valoresDatosSuplementarios.size(); i++) {
                    CamposFormulario cF = (CamposFormulario) valoresDatosSuplementarios.elementAt(i);
                    EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);

                    String valorCampo = cF.getString(eC.getCodCampo());
                    if (eC.getCodTipoDato().equals("6")) valorCampo = getDescripcionValorDesplegable(eC, valorCampo);

                    if (valorCampo != null && !"".equals(valorCampo)){
                        String campoTokenizado = eliminarCaracteresProhibidos(eC.getCodCampo());
                        nodo.addCampo(campoTokenizado, valorCampo);
                        // Si el campo suplementario es de tipo fecha y está habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                        // otro nombre y con el mismo valor que la original sólo que con el nuevo formato de fecha
                        if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                            String nuevoCampoTokenizado = eliminarCaracteresProhibidos(campoTokenizado + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                            nodo.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                        }//if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla()))
                    }//if (valorCampo != null && !"".equals(valorCampo))

                    String idCampoConPrefijo = "T" + gvo.getAtributo("codTramite") + eC.getCodCampo();
                    valorCampo = cF.getString(idCampoConPrefijo);
                    if (eC.getCodTipoDato().equals("6")) valorCampo = getDescripcionValorDesplegable(eC, valorCampo);

                    if (valorCampo != null && !"".equals(valorCampo)) {
                        String campoTokenizado = eliminarCaracteresProhibidos(idCampoConPrefijo);
                        nodo.addCampo(campoTokenizado, valorCampo);
                        /** prueba ***/
                        // Si el campo suplementario es de tipo fecha y está habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                        // otro nombre y con el mismo valor que la original sólo que con el nuevo formato de fecha
                        if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                            String nuevoCampoTokenizado = eliminarCaracteresProhibidos(campoTokenizado + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                            nodo.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                        }//if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla()))
                        /** prueba ***/
                    }//if (valorCampo != null && !"".equals(valorCampo))
                }//for (int i = 0; i < valoresDatosSuplementarios.size(); i++)
                         
              //Etiquetas que vienen de los módulos externos
              //Indicamos plantillaInteresados->NO
              //Indicamos plantillaRelacion->NO
              ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasModulosExternos= getEtiquetasModulosExternos(codOrganizacionM,codProcedimiento,"N", "N", params);
              for (int i = 0; i < etiquetasModulosExternos.size(); i++) {
                 EstructuraEtiquetaModuloIntegracionVO etiqueta  = (EstructuraEtiquetaModuloIntegracionVO) etiquetasModulosExternos.get(i);
                
                 //Ahora tenemos que recuperar el valor de la etiqueta, y anhadirla..
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es el numExpediente pasado: "+ numExpediente);
                 String valor=ModuloIntegracionExternoManager.getInstance().getValorDeEtiqueta(params, etiqueta, numExpediente,codTramiteM, ocuTramiteM);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es numExpediente: "+numExpediente);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es codTramite: "+codTramiteM);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es codTramite: "+ocuTramiteM);
                 nodo.addCampo(etiqueta.getNombreEtiqueta(),valor);
                
              }//for de Módulos Externos
                m_Log.debug(nodo);
            }//for (Object objNodo : colNodos)
            //xml = new GeneradorInformesMgr(params).generaXMLResultado2(eeRaiz);
            xml = new GeneradorInformesMgr(params).generaXMLResultadoPorExpediente(eeRaiz);
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
        }//try-catch
        m_Log.debug("XML ::" + xml);
        return xml;
    }//consultaXMLRelacionDocumentoNormal


    private String getDescripcionValorDesplegable(EstructuraCampo eC, String valorCampo) {
        // El campo suplementario es de tipo desplegable, así que el valor que tenemos es el codigo
        // del combo. Vamos a recuperar la descripcion de ese codigo.
        String descValorCampo = valorCampo;
        for (int j = 0; j < eC.getListaCodDesplegable().size(); j++) {
            String codigoCampo = (String) eC.getListaCodDesplegable().elementAt(j);
            if (codigoCampo.equals("'" + descValorCampo + "'")) {
                String descCampo = (String) eC.getListaDescDesplegable().elementAt(j);
                descValorCampo = descCampo.substring(1, descCampo.length() - 1);
                break;
            }
        }
        return descValorCampo;
    }

    
    //Se ejecuta cuando queremos dar de alta un documento por interesado.
    public String consultaXML2Relacion(GeneralValueObject gvo,String[] params) throws TechnicalException{
        m_Log.info("DocumentosRelacionExpedientesDAO. consultaXML2Relacion. BEGIN:");
        String xml = "";
        String raiz = obtenEstructuraRaiz2((String)gvo.getAtributo("codAplicacion"),params);
        
        String CodAPLICACION = (String)gvo.getAtributo("codAplicacion");
        EstructuraEntidades eeRaiz;
        Hashtable<String,Boolean> conjuntoInteresadoExpediente = new Hashtable<String,Boolean>();
        //Hashtable<String,Boolean> conjuntoInteresadoExpedienteMai = new Hashtable<String,Boolean>();
        

        
        String codOrganizacionM=(String)gvo.getAtributo("codMunicipio");
        String codTramiteM=(String)gvo.getAtributo("codTramite");
        String ocuTramiteM=(String)gvo.getAtributo("ocurrenciaTramite"); 
        
        
        if(m_Log.isDebugEnabled()) m_Log.debug("codOrganizacionM:"+codOrganizacionM);
        if(m_Log.isDebugEnabled()) m_Log.debug("codTRAMITE:"+codTramiteM);
        if(m_Log.isDebugEnabled()) m_Log.debug("ocuTRAMITE:"+ocuTramiteM);
      
     
        
        
        boolean formatoFechaAlternativo = false;
        String FORMATO_FECHA_ALTERNATIVO = null;
        try{
            ResourceBundle config = ResourceBundle.getBundle("documentos");
            FORMATO_FECHA_ALTERNATIVO = config.getString(((String)gvo.getAtributo("codMunicipio")) + ConstantesDatos.FORMATO_ALTERNATIVO_FECHAS_DOC_TRAMITACION);
            if(FORMATO_FECHA_ALTERNATIVO!=null && !"".equals(FORMATO_FECHA_ALTERNATIVO)){
                formatoFechaAlternativo = true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch

        DocumentosAplicacionDAO docDAO = DocumentosAplicacionDAO.getInstance();
        String codProcedimiento = (String)gvo.getAtributo("codProcedimiento");
        String codPlantilla     = (String)gvo.getAtributo("codPlantilla");
        String codMunicipio     = (String)gvo.getAtributo("codMunicipio");
        
        

        Vector etiquetasFecha = null;
        if(FORMATO_FECHA_ALTERNATIVO!=null && !"".equals(FORMATO_FECHA_ALTERNATIVO)){
            formatoFechaAlternativo = true;
            Hashtable<String,String> datosPlantilla = docDAO.getPlantillaDocumento(codPlantilla, codProcedimiento, params);
            String codTramite    = datosPlantilla.get("COD_TRAMITE");
            String porInteresado = datosPlantilla.get("POR_INTERESADO");
            String porRelacion   = datosPlantilla.get("POR_RELACION");

            gvo.setAtributo("relacion", porRelacion);
            gvo.setAtributo("interesado", porInteresado);
            try{
                etiquetasFecha = docDAO.loadEtiquetasFecha(gvo, params);
            }catch(Exception e){
                e.printStackTrace();
            }//try-catch
        }//if(FORMATO_FECHA_ALTERNATIVO!=null && !"".equals(FORMATO_FECHA_ALTERNATIVO))
        try{
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codProcedimiento", gvo.getAtributo("codProcedimiento"));
            gVO.setAtributo("codMunicipio", gvo.getAtributo("codMunicipio"));
            gVO.setAtributo("numero", gvo.getAtributo("numeroRelacion"));
            gVO.setAtributo("ejercicio", gvo.getAtributo("ejercicio"));
            if(m_Log.isDebugEnabled()) m_Log.debug("RELACION");
            if(m_Log.isDebugEnabled()) m_Log.debug("NUM_REL::"+gVO.getAtributo("numero"));
            if(m_Log.isDebugEnabled()) m_Log.debug("COD_PRO::"+gVO.getAtributo("codProcedimiento"));
            if(m_Log.isDebugEnabled()) m_Log.debug("COD_MUN::"+gVO.getAtributo("codMunicipio"));
            if(m_Log.isDebugEnabled()) m_Log.debug("EJERCICIO ::"+gVO.getAtributo("ejercicio"));
            m_Log.debug("CARGAR RELACION EXPEDIENTES --> INI cargaListaExpedientes");
            //Necesitamos guardar para cada expediente sus interesados
            HashMap mapa=new HashMap();
            mapa=(HashMap)gvo.getAtributo("mapa");
           
           
            Vector expedientes = new Vector();
            expedientes = FichaRelacionExpedientesManager.getInstance().cargaListaExpedientes(gVO, params);
            m_Log.debug("EXPEDIENTES : " + expedientes);
            m_Log.debug("CARGAR RELACION EXPEDIENTES --> FIN cargaListaExpedientes");

            eeRaiz = UtilidadesXerador.construyeEstructuraEntidadesInforme(params,"",raiz);
            m_Log.debug("##################################### RAIZ " + eeRaiz);
            m_Log.debug("........EXPEDIENTES : " + expedientes.size());
            String fecActEsp = "",fecActGal = "",fechaActEu = "";
            ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasModulosExternos= getEtiquetasModulosExternos(codOrganizacionM,codProcedimiento,"S", "N", params);
            m_Log.debug("Numero de etiquetas que nos vienen de los modulos externos"+etiquetasModulosExternos.size());
            for(int j=0;j<expedientes.size();j++) {
                GeneralValueObject temp = (GeneralValueObject)expedientes.get(j);
                String numExp = (String)temp.getAtributo("numExp");
                gvo.setAtributo("numeroExpediente",numExp);
                GeneralValueObject temp2 = new GeneralValueObject();
                temp2.setAtributo("codMunicipio",(String)gvo.getAtributo("codMunicipio"));
                temp2.setAtributo("codProcedimiento",(String)gvo.getAtributo("codProcedimiento"));
                temp2.setAtributo("numeroExpediente", numExp);
                temp2.setAtributo("ejercicio",(String)gvo.getAtributo("ejercicio"));
                temp2.setAtributo("codRol","");
                m_Log.debug("........EXPEDIENTE : " + numExp);
             
                Vector listaInteresados = (Vector)gvo.getAtributo("listaCodInteresados");
              
                Vector listaVersInteresados = (Vector)gvo.getAtributo("listaVersInteresados");
                
                String numeroExpedienteTratado = (String)gvo.getAtributo("numeroExpediente");
                m_Log.debug("........INTERESADOS : " + listaInteresados.size());
                for(int m=0;m<listaInteresados.size()&& m<listaVersInteresados.size();m++) {
                 
                    String codInteresado = (String)listaInteresados.get(m);
                    String versInteresado = (String) listaVersInteresados.get(m);

                    String clave = codInteresado + "-" + numeroExpedienteTratado;
                    // Si el interesado ya ha sido tratado para el expediente actual => No se vuelve a tratar y no se incluye de nuevo en el xml
                    if(!conjuntoInteresadoExpediente.containsKey(clave)){
                        conjuntoInteresadoExpediente.put(clave,true);
                   
                        Vector parametros = new Vector();
                        parametros.add((String)gvo.getAtributo("codProcedimiento"));
                        parametros.add((String)gvo.getAtributo("numeroExpediente"));
                        parametros.add((String)gvo.getAtributo("codTramite"));
                        parametros.add((String)gvo.getAtributo("ocurrenciaTramite"));
                        parametros.add((String)gvo.getAtributo("idioma"));
                        parametros.add(codInteresado);
                        parametros.add(versInteresado);
                        if(m_Log.isDebugEnabled()) m_Log.debug("COD_PROC::"+gvo.getAtributo("codProcedimiento"));
                        if(m_Log.isDebugEnabled()) m_Log.debug("NUM_EXP::"+gvo.getAtributo("numeroExpediente"));
                        if(m_Log.isDebugEnabled()) m_Log.debug("COD_TRA::"+gvo.getAtributo("codTramite"));
                        if(m_Log.isDebugEnabled()) m_Log.debug("OCU_TRA::"+gvo.getAtributo("ocurrenciaTramite"));
                        if(m_Log.isDebugEnabled()) m_Log.debug("IDIOMA ::"+gvo.getAtributo("idioma"));
                        if(m_Log.isDebugEnabled()) m_Log.debug("CODINTERESADO :: " + codInteresado);
                        if(m_Log.isDebugEnabled()) m_Log.debug("VERSINTERESADO :: " + versInteresado);

                        eeRaiz.setValoresParametrosConsulta(parametros);
                        eeRaiz.setConsultaEjecutada(false);
                        UtilidadesXerador.construyeEstructuraDatos(params,eeRaiz,etiquetasFecha);
                        Vector valoresDatosSuplementarios = new Vector();
                        Vector estructuraDatosSuplementarios = new Vector();
                        Vector listas = new Vector();
                        listas = getValoresDatosSuplementarios(gvo,params);
                        estructuraDatosSuplementarios = (Vector) listas.firstElement();
                        valoresDatosSuplementarios = (Vector) listas.lastElement();
                        java.util.Collection col = eeRaiz.getListaInstancias();
                        java.util.Iterator iterInst = col.iterator();
                        m_Log.debug("tamanaho de la coleccion: "+col.size());
                        String fechaActual = "";
                        while (iterInst.hasNext()) {
                           
                            NodoEntidad n = (NodoEntidad) iterInst.next();
                           
                           //Etiquetas que vienen de los módulos externos
                           //Indicamos plantillaInteresados->SI
                           //Indicamos plantillaRelacion->NO
                           
                            String numeroExpedienteAAnalizar=dameExpedienteDeInteresado(mapa,codInteresado);
                            //String claveMai = codInteresado + "-" + numeroExpedienteTratado;
                            // if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es CLAVEmAI: .."+claveMai);
                            // if(!conjuntoInteresadoExpedienteMai.containsKey(claveMai)){
                                // if(m_Log.isDebugEnabled()) m_Log.debug("No contiene claveMai: .."+claveMai);  
                                //conjuntoInteresadoExpedienteMai.put(claveMai,true); 
                            for (int i = 0; i < etiquetasModulosExternos.size(); i++) {
                               EstructuraEtiquetaModuloIntegracionVO etiqueta  = (EstructuraEtiquetaModuloIntegracionVO) etiquetasModulosExternos.get(i);
                               String valor=ModuloIntegracionExternoManager.getInstance().getValorDeEtiqueta(params, etiqueta, numeroExpedienteAAnalizar,codTramiteM, ocuTramiteM);
                               if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es el interesado a analizar: .."+codInteresado);
                               if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es el numExpediente a analizar: .."+numeroExpedienteTratado);
                               if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es la etiqueta "+etiqueta.getNombreEtiqueta());
                               if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es el valor: "+valor);
                               n.addCampo(etiqueta.getNombreEtiqueta(),valor);
                           }//for de Módulos Externos
                            
                           if (fecActGal.equals("")) {
                                fecActGal = n.getCampo("FECACTGAL");
                                if(m_Log.isDebugEnabled()) m_Log.debug("FECHA GALEGO :: " + fecActGal);
                                fecActGal = traducirFecha(fecActGal,"es","GL");
                            }//if (fecActGal.equals("")) 
                            n.remove("FECACTGAL");
                            n.addCampo("FECACTGAL",fecActGal);

                            if (fecActEsp.equals("")) {
                                fecActEsp = n.getCampo("FECACTESP");
                                fechaActual = fecActEsp;
                                if(m_Log.isDebugEnabled()) m_Log.debug("FECHA ESPAÑOL :: " + fecActEsp);
                                fecActEsp = traducirFecha(fecActEsp,"es","ES");
                            }//if (fecActEsp.equals("")) 
                            n.remove("FECACTESP");
                            n.addCampo("FECACTESP",fecActEsp);

                            if (!fechaActual.equals("")) {
                                fechaActEu = DateOperations.traducirFechaEuskera(fechaActual);
                            }//if (!fechaActual.equals("")) 
                            n.remove("FECACTEU");
                            n.addCampo("FECACTEU",fechaActEu);

                            Vector listaCargos = new Vector();
                            listaCargos = getListaCargos2(params,CodAPLICACION);
                            for(int i=0;i<listaCargos.size();i++) {
                                GeneralValueObject g = new GeneralValueObject();
                                g = (GeneralValueObject) listaCargos.elementAt(i);
                                String cargo = (String) g.getAtributo("cargo");
                                String nombre = (String) g.getAtributo("nombre");
                                StringTokenizer valores = null;
                                String cargoTokenizado = "";
                                if (cargo != null) {
                                    valores = new StringTokenizer(cargo," ",false);
                                    while (valores.hasMoreTokens()) {
                                        String valor = valores.nextToken();
                                        cargoTokenizado += valor;
                                        if(m_Log.isDebugEnabled()) m_Log.debug("--> " + cargoTokenizado);
                                    }//while (valores.hasMoreTokens()) 
                                }//if (cargo != null) 
                                n.remove(cargoTokenizado);
                                n.addCampo(cargoTokenizado,nombre);
                            }//for(int i=0;i<listaCargos.size();i++) 

                            String domicilio = n.getCampo("DOMICILIOINTERESADO");
                            String poblacion = n.getCampo("POBLACIONINTERESADO");
                            String domicilioNoNormalizado = n.getCampo("DOMICILIONONORMALIZADO");
                            String poblacionNoNormalizado = n.getCampo("POBLACIONNONORMALIZADO");
                            String normalizado = n.getCampo("NORMALIZADO");
                            if(normalizado != null && normalizado.equals("2")) {
                                n.remove("DOMICILIOINTERESADO");
                                n.addCampo("DOMICILIOINTERESADO",domicilioNoNormalizado);
                                n.remove("POBLACIONINTERESADO");
                                n.addCampo("POBLACIONINTERESADO", poblacionNoNormalizado);
                            }//if(normalizado != null && normalizado.equals("2")) 

                            if(!iterInst.hasNext()){                                
                                DatosSuplementariosTerceroManager datosSuplementariosManager = DatosSuplementariosTerceroManager.getInstance();

                                //Estructura datos suplementarios
                                Vector<EstructuraCampo> datosSuplementarios = 
                                        datosSuplementariosManager.cargaEstructuraDatosSuplementariosTercero(codMunicipio, params);

                                //CamposFormulario
                                Vector camposFormulario = 
                                    datosSuplementariosManager.cargaValoresDatosSuplementariosConCodigo(codMunicipio, codInteresado, datosSuplementarios, params);

                                for(EstructuraCampo datoSuplementario : datosSuplementarios){
                                    String nombreCampo = datoSuplementario.getCodCampo();
                                    String tipoDato = datoSuplementario.getCodTipoDato();
                                    String codPlantillaTercero = datoSuplementario.getCodPlantilla();
                                    
                                    for(int z=0; z<camposFormulario.size(); z++){
                                        CamposFormulario campo = (CamposFormulario) camposFormulario.get(z);
                                        HashMap campos =  campo.getCampos();
                                        Iterator itCampos = campos.entrySet().iterator();
                                        while(itCampos.hasNext()){
                                            Map.Entry entry = (Map.Entry) itCampos.next();
                                            String key = (String) entry.getKey();
                                            if(nombreCampo.equalsIgnoreCase(key)){
                                                String valor = "";
                                                if(tipoDato.equalsIgnoreCase("6")){
                                                    valor = datosSuplementariosManager.getValorDesplegable(codMunicipio, nombreCampo, (String) entry.getValue(), params);
                                                }else{
                                                    valor = (String) entry.getValue();
                                                }//if(tipoDato.equalsIgnoreCase("6"))
                                                
                                                if(valor==null) valor ="";
                                                n.addCampo(nombreCampo,valor);

                                                
                                                if(formatoFechaAlternativo && codPlantillaTercero!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(codPlantillaTercero)){
                                                    String nuevoCampoTokenizado = eliminarCaracteresProhibidos(nombreCampo + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                                    n.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valor,FORMATO_FECHA_ALTERNATIVO));
                                                }
                                                
                                                break;
                                            }//if(nombreCampo.equalsIgnoreCase(key))
                                        }//while(itCampos.hasNext())
                                    }//for(int z=0; z<camposFormulario.size(); z++)
                                }//for(EstructuraCampos datoSuplementario : datosSuplementarios)
                            }//if(!iterInst.hasNext())

                            for(int i=0;i<valoresDatosSuplementarios.size();i++) {
                                CamposFormulario cF = (CamposFormulario) valoresDatosSuplementarios.elementAt(i);
                                EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);

                                String valorCampo = cF.getString(eC.getCodCampo());
                                if (eC.getCodTipoDato().equals("6")) valorCampo = getDescripcionValorDesplegable(eC, valorCampo);
                                if(valorCampo != null && !"".equals(valorCampo)) {
                                    n.addCampo(eC.getCodCampo(),valorCampo);
                                    // Si el campo suplementario es de tipo fecha y está habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                                    // otro nombre y con el mismo valor que la original sólo que con el nuevo formato de fecha
                                    if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                                        String nuevoCampoTokenizado = eliminarCaracteresProhibidos(eC.getCodCampo() + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                        n.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                                    }//if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla()))
                                }//if(valorCampo != null && !"".equals(valorCampo)) 

                                String idCampoConPrefijo = "T" + gvo.getAtributo("codTramite") + eC.getCodCampo();
                                if (eC.getCodTipoDato().equals("6")) valorCampo = getDescripcionValorDesplegable(eC, valorCampo);
                                valorCampo = cF.getString(idCampoConPrefijo);
                                if (valorCampo != null && !"".equals(valorCampo)) {
                                    String campoTokenizado = eliminarCaracteresProhibidos(idCampoConPrefijo);
                                    n.addCampo(campoTokenizado, valorCampo);
                                    // Si el campo suplementario es de tipo fecha y está habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                                    // otro nombre y con el mismo valor que la original sólo que con el nuevo formato de fecha
                                    if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                                        String nuevoCampoTokenizado = eliminarCaracteresProhibidos(campoTokenizado + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                        n.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                                    }//if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla()))
                                }//if (valorCampo != null && !"".equals(valorCampo)) 
                            }//for(int i=0;i<valoresDatosSuplementarios.size();i++) 
                     
                            
                        
         
                        }//while (iterInst.hasNext()) 
                        //}
                    
                    }//if(!conjuntoInteresadoExpediente.containsKey(clave))
                }//for(int m=0;m<listaInteresados.size()&& m<listaVersInteresados.size();m++) 
            }//for(int j=0;j<expedientes.size();j++) 
            xml = new GeneradorInformesMgr(params).generaXMLResultado2(eeRaiz);
        }catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
        }//try-catch
        m_Log.debug("El XML generado en consultaXML2Relacion es:"+xml);
        m_Log.debug("El XML generado en consultaXMLRelacion es:"+xml);
        return xml;
    }//consultaXML2Relacion

 


    public Vector getValoresDatosSuplementarios(GeneralValueObject gvo,String[] params){
        TramitacionExpedientesValueObject t = new TramitacionExpedientesValueObject();
        t.setCodMunicipio((String) gvo.getAtributo("codMunicipio"));
        t.setCodProcedimiento((String) gvo.getAtributo("codProcedimiento"));
        t.setEjercicio(((String) gvo.getAtributo("numeroExpediente")).substring(0, 4));
        t.setNumeroExpediente((String) gvo.getAtributo("numeroExpediente"));
        t.setCodTramite((String) gvo.getAtributo("codTramite"));
        t.setOcurrenciaTramite((String) gvo.getAtributo("ocurrenciaTramite"));
        gvo.setAtributo("numero", gvo.getAtributo("numeroExpediente"));
        GeneralValueObject g = new GeneralValueObject();
        g.setAtributo("codMunicipio", gvo.getAtributo("codMunicipio"));
        g.setAtributo("codProcedimiento", gvo.getAtributo("codProcedimiento"));
        g.setAtributo("ejercicio",((String) gvo.getAtributo("numeroExpediente")).substring(0, 4));
        g.setAtributo("numero", gvo.getAtributo("numeroExpediente"));
        g.setAtributo("codTramite", gvo.getAtributo("codTramite"));

        m_Log.debug("NUMERO DE EXPEDIENTE: " + gvo.getAtributo("numeroExpediente"));
        // Campos del tramite
        Vector estructuraDatosSuplementarios = TramitacionExpedientesManager.getInstance().
                cargaEstructuraDatosSuplementarios(t, params);
        estructuraDatosSuplementarios = FichaRelacionExpedientesManager.getInstance().
                filtrarEstructuraDatosRelaciones(estructuraDatosSuplementarios, (String) gvo.getAtributo("numeroRelacion"), params);
        Vector valoresDatosSuplementarios = TramitacionExpedientesManager.getInstance().cargaValoresDatosSuplEtiquetas(t,estructuraDatosSuplementarios, params);
        valoresDatosSuplementarios = FichaRelacionExpedientesManager.getInstance().
                filtrarDatosSuplementariosRelacion(estructuraDatosSuplementarios, valoresDatosSuplementarios, (String) gvo.getAtributo("numeroRelacion"), params);


        if(m_Log.isDebugEnabled()) m_Log.debug("el tamaño de la lista de valores de datos suplementarios es : " + valoresDatosSuplementarios.size());
        Vector<Vector> listas = new Vector<Vector>();
        listas.addElement(estructuraDatosSuplementarios);
        listas.addElement(valoresDatosSuplementarios);
        return listas;
    }
   

      /**
       * Recupera los valores de los datos suplementarios de un trámite de una relación pero sin realizar el filtrado por trámite
       * @param gvo: Datos de la relación
       * @param params: Parámetros de conexión a la base de datos
       * @return Vector
       */
    public Vector getValoresDatosSuplementariosTramiteSinFiltrar(GeneralValueObject gvo,String[] params){
        TramitacionExpedientesValueObject t = new TramitacionExpedientesValueObject();
        t.setCodMunicipio((String) gvo.getAtributo("codMunicipio"));
        t.setCodProcedimiento((String) gvo.getAtributo("codProcedimiento"));
        t.setEjercicio((String) gvo.getAtributo("ejercicio"));
        t.setNumeroExpediente((String) gvo.getAtributo("numeroExpediente"));
        t.setCodTramite((String) gvo.getAtributo("codTramite"));
        t.setOcurrenciaTramite((String) gvo.getAtributo("ocurrenciaTramite"));
        gvo.setAtributo("numero", gvo.getAtributo("numeroExpediente"));
        GeneralValueObject g = new GeneralValueObject();
        g.setAtributo("codMunicipio", gvo.getAtributo("codMunicipio"));
        g.setAtributo("codProcedimiento", gvo.getAtributo("codProcedimiento"));
        g.setAtributo("ejercicio", gvo.getAtributo("ejercicio"));
        g.setAtributo("numero", gvo.getAtributo("numeroExpediente"));
        g.setAtributo("codTramite", gvo.getAtributo("codTramite"));

        m_Log.debug("NUMERO DE EXPEDIENTE: " + gvo.getAtributo("numeroExpediente"));
        // Campos del tramite
        Vector estructuraDatosSuplementarios = TramitacionExpedientesManager.getInstance().cargaEstructuraDatosSuplementarios(t, params);
        // ORIGINAL
        //estructuraDatosSuplementarios = FichaRelacionExpedientesManager.getInstance().filtrarEstructuraDatosRelaciones(estructuraDatosSuplementarios, (String) gvo.getAtributo("numeroRelacion"), params);
        Vector valoresDatosSuplementarios = TramitacionExpedientesManager.getInstance().cargaValoresDatosSuplEtiquetas(t,estructuraDatosSuplementarios, params);
        valoresDatosSuplementarios = FichaRelacionExpedientesManager.getInstance().
                filtrarDatosSuplementariosRelacion(estructuraDatosSuplementarios, valoresDatosSuplementarios, (String) gvo.getAtributo("numeroRelacion"), params);

        if(m_Log.isDebugEnabled()) m_Log.debug("el tamaño de la lista de valores de datos suplementarios es : " + valoresDatosSuplementarios.size());
        Vector<Vector> listas = new Vector<Vector>();
        listas.addElement(estructuraDatosSuplementarios);
        listas.addElement(valoresDatosSuplementarios);
        return listas;
    }


      /**
       * Recupera los valores de los datos suplementarios de un expediente
       * @param gvo: Datos de la relación
       * @param params: Parámetros de conexión a la base de datos
       * @return Vector
       */
    public Vector getValoresDatosSuplementariosProcedimientoSinFiltrar(GeneralValueObject gvo,String[] params){
        TramitacionExpedientesValueObject t = new TramitacionExpedientesValueObject();
        t.setCodMunicipio((String) gvo.getAtributo("codMunicipio"));
        t.setCodProcedimiento((String) gvo.getAtributo("codProcedimiento"));
        t.setEjercicio((String) gvo.getAtributo("ejercicio"));
        t.setNumeroExpediente((String) gvo.getAtributo("numeroExpediente"));
        t.setCodTramite((String) gvo.getAtributo("codTramite"));
        t.setOcurrenciaTramite((String) gvo.getAtributo("ocurrenciaTramite"));
        gvo.setAtributo("numero", gvo.getAtributo("numeroExpediente"));
        GeneralValueObject g = new GeneralValueObject();
        g.setAtributo("codMunicipio", gvo.getAtributo("codMunicipio"));
        g.setAtributo("codProcedimiento", gvo.getAtributo("codProcedimiento"));
        g.setAtributo("ejercicio", gvo.getAtributo("ejercicio"));
        g.setAtributo("numero", gvo.getAtributo("numeroExpediente"));
        g.setAtributo("codTramite", gvo.getAtributo("codTramite"));

        m_Log.debug("NUMERO DE EXPEDIENTE: " + gvo.getAtributo("numeroExpediente"));

        Vector estructuraDatosSuplementarios = new Vector();
        Vector valoresDatosSuplementarios = new Vector();

        Vector estructuraDatosSuplementarios1 = FichaExpedienteManager.getInstance().cargaEstructuraDatosSuplementariosProcedimiento(g, params);
        Vector valoresDatosSuplementarios1 = FichaExpedienteManager.getInstance().cargaValoresDatosSuplementarios(g,estructuraDatosSuplementarios1, params);
        for(int i=0;estructuraDatosSuplementarios1!=null && valoresDatosSuplementarios1!=null && i<valoresDatosSuplementarios1.size();i++) {            
            valoresDatosSuplementarios.addElement(valoresDatosSuplementarios1.elementAt(i));
            estructuraDatosSuplementarios.addElement(estructuraDatosSuplementarios1.elementAt(i));
        }
        
        if(m_Log.isDebugEnabled()) m_Log.debug("el tamaño de la lista de valores de datos suplementarios es : " + valoresDatosSuplementarios.size());
        Vector<Vector> listas = new Vector<Vector>();
        listas.addElement(estructuraDatosSuplementarios);
        listas.addElement(valoresDatosSuplementarios);
        return listas;
    }


    private String traducirFecha(String fecha, String pais, String idioma){
        try{
            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date d = s.parse(fecha);
            Calendar calendario = Calendar.getInstance();
            calendario.setTime(d);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            int mes = calendario.get(Calendar.MONTH);
            int ano = calendario.get(Calendar.YEAR);
            return dia +" de " +es.altia.util.commons.DateOperations.traducirMes(mes,pais,idioma)+ " de " + ano;
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    
    
    private Vector getListaCargos2(String[] params , String codAplicacion) throws TechnicalException {
        AdaptadorSQLBD abd = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        Vector lista = new Vector();

        try{
            abd = new AdaptadorSQLBD(params);
            con = abd.getConnection();
            st = con.createStatement();
              
            sql.append("SELECT "+abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'TITULOCARGO'",
                    abd.convertir("E_CAR.CAR_COD",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null)}))
            .append(" AS CODIGO,E_CAR.CAR_CAR AS NOME ")
            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
            .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
            .append(" A_DOC.DOC_CEI = 7 AND ")
            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO")
            .append(" UNION SELECT "+abd.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,new String[]{"'NOMBRECARGO'",
                    abd.convertir("E_CAR.CAR_COD",AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,null)})+" AS CODIGO,E_CAR.CAR_DES AS NOME ")
            .append(" FROM E_CAR, ENTIDADEINFORME EI, ESTRUCTURAINFORME ESI, A_DOC ")
            .append(" WHERE A_DOC.DOC_APL = ").append(codAplicacion).append(" AND ")
            .append(" A_DOC.DOC_CEI = 7 AND ")
            .append(" A_DOC.DOC_CEI = ESI.COD_ESTRUCTURA AND ")
            .append(" ESI.COD_ENTIDADEINFORME = EI.CODIGO");

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql.toString());
            while(rs.next()){
                GeneralValueObject g = new GeneralValueObject();
                String cargo = rs.getString("CODIGO");
                g.setAtributo("cargo",cargo);
                String nombre = rs.getString("NOME");
                g.setAtributo("nombre",nombre);
                lista.addElement(g);
                m_Log.debug("CARGO "+cargo);
                m_Log.debug("NOMBRE "+nombre);
            }
            
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
        }finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(abd, con);
            return lista;
        }
    }
    
    private Vector getListaCargos(String[] params) throws TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        Vector lista = new Vector();

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            sql = "SELECT CAR_DES,CAR_CAR FROM E_CAR";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                GeneralValueObject g = new GeneralValueObject();
                String cargo = rs.getString("CAR_CAR");
                g.setAtributo("cargo",cargo);
                String nombre = rs.getString("CAR_DES");
                g.setAtributo("nombre",nombre);
                lista.addElement(g);
            }
            
       
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
        }finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
            return lista;
        }
    }


    public Vector getEstructuraInteresados(GeneralValueObject gvo,String[] params){
        gvo.setAtributo("numero",(String) gvo.getAtributo("numeroExpediente"));
        GeneralValueObject g = new GeneralValueObject();
        g.setAtributo("codMunicipio",(String) gvo.getAtributo("codMunicipio"));
        g.setAtributo("codProcedimiento",(String) gvo.getAtributo("codProcedimiento"));
        g.setAtributo("ejercicio",(String) gvo.getAtributo("ejercicio"));
        g.setAtributo("numero",(String) gvo.getAtributo("numeroExpediente"));
        Vector estructuraInteresados = new Vector();
        estructuraInteresados = FichaExpedienteManager.getInstance().cargaEstructuraInteresados(g, params);
        return estructuraInteresados;
    }

    public byte[] loadDocumento(GeneralValueObject gvo,String[] params) throws TechnicalException
    {
        byte[] r = null;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            // Creamos la select con los parametros adecuados.
            StringBuffer sql = new StringBuffer();
            sql.append(" select CRD_FIL ")
                    .append(" FROM G_CRD ")
                    .append(" WHERE CRD_NUM=? AND CRD_TRA=? AND CRD_OCU=? AND CRD_NUD=?");

            stmt = conexion.prepareStatement(sql.toString());
            if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
            stmt.setString(1,abd.js_unescape((String)gvo.getAtributo("numeroRelacion")));
            stmt.setInt(2,new Integer((String)gvo.getAtributo("codTramite")));
            stmt.setInt(3,new Integer((String)gvo.getAtributo("ocurrenciaTramite")));
            stmt.setInt(4, new Integer((String) gvo.getAtributo("numeroDocumento")));
            rs = stmt.executeQuery();
            while(rs.next())
            {
                java.io.InputStream st = rs.getBinaryStream("CRD_FIL");
                java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                int c;
                while ((c = st.read())!= -1){
                    ot.write(c);
                }
                ot.flush();
                r = ot.toByteArray();
                ot.close();
                st.close();
            }
        }catch (Exception sqle){
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Error de SQL en loadDocumento: " + sqle.toString());
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
        return r;
    }

    private String obtenEstructuraRaiz(String codAplicacion,String[] params) throws TechnicalException{
        String raiz = "";
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            // Creamos la select con los parametros adecuados.
            StringBuffer sql = new StringBuffer();
            sql.append(" select DOC_CEI FROM A_DOC WHERE DOC_APL = ")
                    .append(codAplicacion).append(" AND DOC_CEI=1");

            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
            rs = stmt.executeQuery(sql.toString());
            while(rs.next()){
                raiz = rs.getString("DOC_CEI");
            }
        }
        catch (Exception sqle){
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Error en obtenEstructuraRaiz: " + sqle.toString());
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
        return raiz;
    }

    private String obtenEstructuraRaiz2(String codAplicacion,String[] params) throws TechnicalException{
        String raiz = "";
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet  rs = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            // Creamos la select con los parametros adecuados.
            StringBuffer sql = new StringBuffer();
            sql.append(" select DOC_CEI FROM A_DOC WHERE DOC_APL = ")
                    .append(codAplicacion).append(" AND DOC_CEI=5");

            stmt = conexion.createStatement();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
            rs = stmt.executeQuery(sql.toString());
            while(rs.next()){
                raiz = rs.getString("DOC_CEI");
            }
        }catch (Exception sqle){
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Error en obtenEstructuraRaiz: " + sqle.toString());
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
        return raiz;
    }

    private String obtenEstructuraRaizRelacion(String codAplicacion, String[] params) throws TechnicalException {

        m_Log.info("OBTENEMOS LA ESTRUCTURA RAIZ DE LA RELACION :: obtenEstructuraRaizRelacion() BEGIN");
        String raiz = "";
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            StringBuffer sql = new StringBuffer();
            sql.append(" select DOC_CEI FROM A_DOC WHERE DOC_APL = ").append(codAplicacion).append(" AND DOC_CEI=7");

            ps = conexion.prepareStatement(sql.toString());
            if (m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                raiz = rs.getString("DOC_CEI");
            }
        } catch (Exception sqle) {
            sqle.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Error en obtenEstructuraRaiz: " + sqle.toString());
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
        return raiz;
    }

    private String obtenMaximo(AdaptadorSQLBD oad, GeneralValueObject gvo,String[] params) throws TechnicalException{
        String maximo = "";
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            // Creamos la select con los parametros adecuados.
            StringBuffer sql = new StringBuffer();
            sql.append(" select " + oad.funcionSistema(oad.FUNCIONSISTEMA_NVL,
                new String[]{oad.funcionMatematica(oad.FUNCIONMATEMATICA_MAX, new String[]{"CRD_NUD"}) + "+1", "1"}))
                    .append(" FROM G_CRD ")
                    .append(" WHERE ")
                    .append(" CRD_PRO= ? AND ")
                    .append(" CRD_EJE= ? AND ")
                    .append(" CRD_NUM= ? AND ")
                    .append(" CRD_TRA= ? AND ")
                    .append(" CRD_OCU= ? ");

            stmt = conexion.prepareStatement(sql.toString());
            stmt.setString(1,(String)gvo.getAtributo("codProcedimiento"));
            stmt.setString(2,(String)gvo.getAtributo("ejercicio"));
            stmt.setString(3,(String)gvo.getAtributo("numeroRelacion"));
            stmt.setString(4,(String)gvo.getAtributo("codTramite"));
            stmt.setString(5,(String)gvo.getAtributo("ocurrenciaTramite"));

            rs = stmt.executeQuery();
            while(rs.next()){
                maximo = rs.getString(1);
            }
        }catch (Exception sqle){
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Error de SQL en obtenMaximo: " + sqle.toString());
        }
        finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.devolverConexion(oad, conexion);
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("MAXIMO:::" + maximo);
        return maximo;
    }


    private String obtenMaximoExpediente(AdaptadorSQLBD oad, GeneralValueObject gvo,String[] params) throws TechnicalException{
        String maximo = "";
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            StringBuffer sql = new StringBuffer();
            sql.append(" select " + oad.funcionSistema(oad.FUNCIONSISTEMA_NVL,
                new String[]{oad.funcionMatematica(oad.FUNCIONMATEMATICA_MAX, new String[]{"CRD_NUD"}) + "+1", "1"}))
                    .append(" FROM E_CRD ")
                    .append(" WHERE ")
                    .append(" CRD_PRO= ? AND ")
                    .append(" CRD_EJE= ? AND ")
                    .append(" CRD_NUM= ? AND ")
                    .append(" CRD_TRA= ? AND ")
                    .append(" CRD_OCU= ? ");

            stmt = conexion.prepareStatement(sql.toString());
            stmt.setString(1,(String)gvo.getAtributo("codProcedimiento"));
            stmt.setString(2,(String)gvo.getAtributo("ejercicio"));
            stmt.setString(3,(String)gvo.getAtributo("numeroExpediente"));
            stmt.setString(4,(String)gvo.getAtributo("codTramite"));
            stmt.setString(5,(String)gvo.getAtributo("ocurrenciaTramite"));

            rs = stmt.executeQuery();
            while(rs.next()){
                maximo = rs.getString(1);
            }
        }catch (Exception sqle){
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Error de SQL en obtenMaximo: " + sqle.toString());
        }
        finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.devolverConexion(oad, conexion);
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("MAXIMO:::" + maximo);
        return maximo;
    }

    public TramitacionExpedientesValueObject cargarDocumentos(TramitacionExpedientesValueObject tEVO,String[] params)
            throws AnotacionRegistroException,TechnicalException {

        m_Log.debug("cargarDocumentos");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        AdaptadorSQLBD oad = null;
        String sql = "";
        String from = "";
        String where = "";
        Vector listaDocumentos = new Vector();
        Vector listaCodDocumentosTramite = new Vector();

        try{
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            // PESTAÑA DE DOCUMENTOS
            from = "CRD_NUD ,CRD_DES," + oad.convertir("CRD_FAL", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                    " AS CRD_FAL," + oad.convertir("CRD_FMO", oad.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") +
                    " AS CRD_FMO, usu.USU_NOM AS usuarioCreacion,usu1.USU_NOM AS usuarioModificacion," +
                    "PLT_INT,PLT_EDITOR_TEXTO " ;

            where = "CRD_MUN = " + tEVO.getCodMunicipio() + " AND CRD_PRO ='" + tEVO.getCodProcedimiento() +
                    "' AND CRD_TRA =" + tEVO.getCodTramite() + " AND CRD_NUM='" +
                    tEVO.getNumeroRelacion() + "' AND CRD_OCU = " + tEVO.getOcurrenciaTramite() + " ";

            // Comprobamos si esta activado el servicio Web de firmadoc.
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
                from += ", CRD_FIR_FD, CRD_EXP";
                where += "AND CRD_EXP_FD IS NOT NULL " +
                        "AND CRD_DOC_FD IS NOT NULL ";
            } else {
                from += ", CRD_FIR_EST, CRD_EXP";
                where += "AND CRD_EXP_FD IS NULL " +
                        "AND CRD_DOC_FD IS NULL " +
                        "AND CRD_FIR_FD IS NULL";
            }
            String[] join1 = new String[14];
            join1[0] = "G_CRD";
            join1[1] = "INNER";
            join1[2] = GlobalNames.ESQUEMA_GENERICO + "a_usu usu";
            join1[3] = "g_crd.CRD_USC = usu.USU_COD";
            join1[4] = "LEFT";
            join1[5] = GlobalNames.ESQUEMA_GENERICO + "a_usu usu1";
            join1[6] = "g_crd.CRD_USM = usu1.USU_COD";
            join1[7] = "INNER";
            join1[8] = "e_dot";
            join1[9] = "g_crd.CRD_PRO = e_dot.DOT_PRO AND " +
                    "g_crd.CRD_TRA = e_dot.DOT_TRA AND " +
                    "g_crd.CRD_DOT = e_dot.DOT_COD";
            join1[10] = "INNER";
            join1[11] = "a_plt";
            join1[12] = "e_dot.DOT_PLT = a_plt.PLT_COD";
            join1[13] = "false";
            sql = oad.join(from,where,join1);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            String entrar = "no";
            while ( rs.next() ) {
                entrar = "si";
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String codDocumento = rs.getString("CRD_NUD");
                tramExpVO.setCodDocumento(codDocumento);
                String descripcion = rs.getString("CRD_DES");
                tramExpVO.setDescDocumento(descripcion);
                String fechaCreacion = rs.getString("CRD_FAL");
                tramExpVO.setFechaCreacion(fechaCreacion);
                String fechaModificacion = rs.getString("CRD_FMO");
                if(fechaModificacion != null) {
                    if(!fechaModificacion.equals("")) {
                        tramExpVO.setFechaModificacion(fechaModificacion);
                    } else tramExpVO.setFechaModificacion("");
                } else tramExpVO.setFechaModificacion("");
                String usuarioCreacion = rs.getString("usuarioCreacion");
                String usuarioModificacion = rs.getString("usuarioModificacion");
                if(usuarioModificacion == null || usuarioModificacion.equals("")) {
                    tramExpVO.setUsuario(usuarioCreacion);
                } else tramExpVO.setUsuario(usuarioModificacion);
                String interesado = rs.getString("PLT_INT");
                tramExpVO.setInteresado(interesado);
                tramExpVO.setEditorTexto(rs.getString("PLT_EDITOR_TEXTO"));
                String estadoFirma;
                if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
                    estadoFirma = rs.getString("CRD_FIR_FD");
                } else {
                    estadoFirma = rs.getString("CRD_FIR_EST");
                }
                tramExpVO.setEstadoFirma(estadoFirma);
                String opcionGrabar = rs.getString("CRD_EXP");
                tramExpVO.setOpcionGrabar(opcionGrabar);
                listaDocumentos.addElement(tramExpVO);
                tEVO.setListaDocumentos(listaDocumentos);
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            
            if(entrar.equals("no")) {
                tEVO.setListaDocumentos(listaDocumentos);
            }
            
            sql = "SELECT DOT_COD FROM E_DOT WHERE DOT_MUN=" + tEVO.getCodMunicipio() +
                    " AND DOT_PRO='" + tEVO.getCodProcedimiento() + "' AND DOT_TRA=" +
                    tEVO.getCodTramite();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            String entrar1 = "no";
            while(rs.next()) {
                entrar1 ="si";
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
                String codDocumento = rs.getString("DOT_COD");
                tramExpVO.setCodDocumento(codDocumento);
                listaCodDocumentosTramite.addElement(tramExpVO);
                tEVO.setListaCodDocumentosTramite(listaCodDocumentosTramite);
            }
            if(entrar1.equals("no")) {
                tEVO.setListaCodDocumentosTramite(listaCodDocumentosTramite);
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException("Error. TramitacionExpedientesDAO.cargarDatos", e);
        } finally {            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }

        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("cargarDocumentos");
        return tEVO;
    }
    
    /**
     * Graba un documento en una relación entre expedientes pero el contenido del fichero no se guarda puesto que se guarda en un gestor documental
     * @param gvo: Datos del documento a dar de
     * @param params: Parámetros de conexión a la base de datos
     * @return Nº del documento
     */
     public int grabarDocumentoGestor(GeneralValueObject gvo,String[] params) throws TechnicalException{
        int resultado;
        String numDoc = "", numDocExp;
        String codMunicipio     = (String)gvo.getAtributo("codMunicipio");
        String codProcedimiento = (String)gvo.getAtributo("codProcedimiento");
        String ejercicio        = (String)gvo.getAtributo("ejercicio");
        String numeroRelacion   = (String)gvo.getAtributo("numeroRelacion");
        String codTramite       = (String)gvo.getAtributo("codTramite");
        String ocurrenciaTramite= (String)gvo.getAtributo("ocurrenciaTramite");
        String codDocumento     = (String)gvo.getAtributo("codDocumento");
        String codUsuario       = (String)gvo.getAtributo("codUsuario");
        String nombreDocumento  = (String)gvo.getAtributo("nombreDocumento");
        String numeroDocumento  = (String)gvo.getAtributo("numeroDocumento");
        String opcionGrabar     = (String)gvo.getAtributo("opcionGrabar");
        String contenidoFichero = "DOCGESTOR";
        byte[] ficheroWord = contenidoFichero.getBytes();

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        ResultSet rs;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            String sql;
            String codEstadoFirma = null;
            if ( ! (numeroDocumento!=null && !numeroDocumento.equals("")) ) {
                StringBuffer query = new StringBuffer("SELECT DOT_FRM FROM E_DOT WHERE ");
                query.append("(DOT_MUN=").append(codMunicipio).append(") AND ");
                query.append("(DOT_PRO='").append(codProcedimiento).append("') AND ");
                query.append("(DOT_TRA=").append(codTramite).append(") AND ");
                query.append("(DOT_COD=").append(codDocumento).append(")");
                Statement statement = conexion.createStatement();
                ResultSet resultSet = statement.executeQuery(query.toString());
                String tmp = null;
                if (resultSet.next()) {
                    tmp = resultSet.getString(1);
                }//if
                SigpGeneralOperations.closeResultSet(resultSet);
                SigpGeneralOperations.closeStatement(statement);
                if (tmp!=null) {
                    if (tmp.equalsIgnoreCase("O")) {
                        codEstadoFirma = "E";
                    } else {
                        codEstadoFirma = tmp.toUpperCase();
                    }//if
                }//if
            }//if

            if(numeroDocumento!=null && !numeroDocumento.equals("")){
                sql = "UPDATE G_CRD "+
                        "SET CRD_FMO = " + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ", CRD_USM = ?, CRD_DES= ?, CRD_FIL = ? "+
                        "WHERE CRD_PRO = ? AND"+
                        "      CRD_EJE = ? AND"+
                        "      CRD_NUM = ? AND"+
                        "      CRD_TRA = ? AND"+
                        "      CRD_OCU = ? AND"+
                        "      CRD_NUD = ? ";
            } else {
                sql = "INSERT INTO G_CRD VALUES "+
                        "(?,?,?,?,?,?,?,"+ abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," +
                        abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ",?,?,?,?,?,?, NULL, NULL, NULL, ?)";
            }//if
            PreparedStatement stmt = conexion.prepareStatement(sql);
            if(numeroDocumento!=null && !numeroDocumento.equals("")){
                resultado = Integer.parseInt(numeroDocumento);
                stmt.setString(1,codUsuario);
                stmt.setString(2,nombreDocumento);
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                stmt.setBinaryStream(3,st,ficheroWord.length);
                stmt.setString(4,codProcedimiento);
                stmt.setString(5,ejercicio);
                stmt.setString(6,numeroRelacion);
                stmt.setString(7,codTramite);
                stmt.setString(8,ocurrenciaTramite);
                stmt.setString(9,numeroDocumento);
            }else{
                /***/
                numDoc = this.obtenMaximo(abd, gvo,params);
                resultado = Integer.parseInt(numDoc);
               
                sql = "INSERT INTO G_CRD(CRD_MUN,CRD_PRO,CRD_EJE,CRD_NUM,CRD_TRA,CRD_OCU,CRD_NUD,CRD_FAL,CRD_FMO," +
                        "CRD_USM,CRD_USC,CRD_FIL,CRD_DES,CRD_DOT,CRD_EXP,CRD_FIR_EST,CRD_EXP_FD,CRD_DOC_FD,CRD_FIR_FD)VALUES "+
                        "(" + codMunicipio + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numeroRelacion + "'," + codTramite + "," + ocurrenciaTramite +
                        "," + numDoc + "," + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) +
                        "," + codUsuario + "," + codUsuario + ",?," + "'" + nombreDocumento + "',"  + codDocumento + ",'" +opcionGrabar + "',?,NULL,NULL,NULL)";


                m_Log.debug("==========> sql:  " + sql);
                stmt.close();
                stmt = conexion.prepareStatement(sql);                
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                stmt.setBinaryStream(1,st,ficheroWord.length);
                if(codEstadoFirma!=null)
                    stmt.setString(2, codEstadoFirma);
                else
                    stmt.setNull(2,java.sql.Types.VARCHAR);
                
            }
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate();
            SigpGeneralOperations.closeStatement(stmt);
            

            if(numeroDocumento!=null && !numeroDocumento.equals("")){  //Modificar
                if(m_Log.isDebugEnabled()) m_Log.debug("MODIFICAR.  OPCION GRABAR ... " + opcionGrabar);
                if(opcionGrabar.equals("1")) {                      
                    Vector<GeneralValueObject> exps = new Vector<GeneralValueObject>();
                    sql = "SELECT EXP_CRD_EJE, EXP_CRD_MUN, EXP_CRD_NUD, EXP_CRD_NUM, EXP_CRD_OCU, EXP_CRD_PRO, EXP_CRD_TRA" +
                            " FROM G_CRD_EXP WHERE REL_CRD_MUN = " + codMunicipio + " AND REL_CRD_PRO='" +
                            codProcedimiento + "' AND REL_CRD_EJE =" + ejercicio + " AND REL_CRD_NUM ='" +
                            numeroRelacion + "' AND REL_CRD_TRA =" + codTramite + " AND REL_CRD_OCU = " +
                            ocurrenciaTramite + " AND REL_CRD_NUD = " + numeroDocumento;
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    Statement stmt2 = conexion.createStatement();
                    rs = stmt2.executeQuery(sql);
                    while ( rs.next() ) {
                        GeneralValueObject exp = new GeneralValueObject();
                        exp.setAtributo("ejercicio", rs.getString("EXP_CRD_EJE"));
                        exp.setAtributo("municipio", rs.getString("EXP_CRD_MUN"));
                        exp.setAtributo("numDoc", rs.getString("EXP_CRD_NUD"));
                        exp.setAtributo("numExp", rs.getString("EXP_CRD_NUM"));
                        exp.setAtributo("ocurrencia", rs.getString("EXP_CRD_OCU"));
                        exp.setAtributo("procedimiento", rs.getString("EXP_CRD_PRO"));
                        exp.setAtributo("tramite", rs.getString("EXP_CRD_TRA"));
                        exps.add(exp);
                    }
                    SigpGeneralOperations.closeResultSet(rs);
                    SigpGeneralOperations.closeStatement(stmt2);

                    for (GeneralValueObject exp : exps) {
                        sql = "UPDATE E_CRD " +
                                "SET CRD_FMO = " + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ", CRD_USM = ?, CRD_DES= ?, CRD_FIL = ? " +
                                "WHERE CRD_PRO = ? AND" +
                                "      CRD_EJE = ? AND" +
                                "      CRD_NUM = ? AND" +
                                "      CRD_TRA = ? AND" +
                                "      CRD_OCU = ? AND" +
                                "      CRD_NUD = ? ";
                        stmt = conexion.prepareStatement(sql);
                        stmt.setString(1, codUsuario);
                        stmt.setString(2, nombreDocumento);
                        InputStream st = new ByteArrayInputStream(ficheroWord);
                        stmt.setBinaryStream(3, st, ficheroWord.length);
                        stmt.setString(4, (String) exp.getAtributo("procedimiento"));
                        stmt.setString(5, (String) exp.getAtributo("ejercicio"));
                        stmt.setString(6, (String) exp.getAtributo("numExp"));
                        stmt.setString(7, (String) exp.getAtributo("tramite"));
                        stmt.setString(8, (String) exp.getAtributo("ocurrencia"));
                        stmt.setString(9, (String) exp.getAtributo("numDoc"));
                        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                        stmt.executeUpdate();
                        SigpGeneralOperations.closeStatement(stmt);
                    }
                }
            }else{ // Insertar
                if(opcionGrabar.equals("1")) { //Todos

                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio",codMunicipio);
                    gVO.setAtributo("codProcedimiento",codProcedimiento);
                    gVO.setAtributo("ejercicio",ejercicio);
                    gVO.setAtributo("numero",numeroRelacion);
                    Vector expedientes = FichaRelacionExpedientesManager.getInstance().cargaListaExpedientes(gVO, params);
                    for (Object objExpediente : expedientes) {
                        GeneralValueObject expediente = (GeneralValueObject) objExpediente;
                        String numExp = (String) expediente.getAtributo("numExp");
                        m_Log.debug("EXPEDIENTE .... " + numExp);
                        gvo.setAtributo("numeroExpediente", numExp);
                        numDocExp = this.obtenMaximoExpediente(abd, gvo, params);
                        DocumentosExpedienteManager.getInstance().grabarDocumento(gvo, params);
                        sql = "INSERT INTO G_CRD_EXP VALUES " +
                                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        stmt = conexion.prepareStatement(sql);
                        stmt.setString(1, codMunicipio);
                        stmt.setString(2, codProcedimiento);
                        stmt.setString(3, ejercicio);
                        stmt.setString(4, numeroRelacion);
                        stmt.setString(5, codTramite);
                        stmt.setString(6, ocurrenciaTramite);
                        stmt.setString(7, numDoc);
                        stmt.setString(8, codMunicipio);
                        stmt.setString(9, codProcedimiento);
                        stmt.setString(10, ejercicio);
                        stmt.setString(11, numExp);
                        stmt.setString(12, codTramite);
                        stmt.setString(13, ocurrenciaTramite);
                        stmt.setString(14, numDocExp);
                        stmt.executeUpdate();
                        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                        SigpGeneralOperations.closeStatement(stmt);
                    }
                }
            }

            SigpGeneralOperations.commit(abd, conexion);

        }catch (Exception sqle){
            SigpGeneralOperations.rollBack(abd, conexion);
            resultado=0;
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("SQLException haciendo rollBackTransaction en: " + getClass().getName());
        } finally {
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
        return resultado;
    }

     /**
      * Recupera el nombre original de un documento asociado a un trámite de una relación entre expedientes
      * @param gVO
      * @param con
      * @return
      */
    public String getNombreDocumentoRelacionGestor(GeneralValueObject gVO,Connection con) throws TechnicalException{

        String nombreDocumento = "";
        String codMunicipio = (String)gVO.getAtributo("codMunicipio");
        String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
        String ejercicio = (String)gVO.getAtributo("ejercicio");
        String codTramite = (String)gVO.getAtributo("codTramite");
        String ocurrenciaTramite = (String)gVO.getAtributo("ocurrenciaTramite");
        String numeroRelacion = (String)gVO.getAtributo("numeroRelacion");
        String numeroDocumento = (String)gVO.getAtributo("numeroDocumento");

        Statement st = null;
        ResultSet rs = null;
        try{
            String sql ="SELECT CRD_DES FROM G_CRD WHERE CRD_NUM='" + numeroRelacion + "' AND CRD_MUN=" + codMunicipio + " AND CRD_PRO='" + codProcedimiento + "' AND " +
                             " CRD_EJE = " + ejercicio + " AND CRD_TRA=" + codTramite + " AND CRD_OCU=" + ocurrenciaTramite + " AND CRD_NUD=" + numeroDocumento;

            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                nombreDocumento = rs.getString("CRD_DES");
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

        }catch(SQLException e){
            e.printStackTrace();
            m_Log.error(e.getMessage());
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
        }

        return nombreDocumento;
    }




  /**
     *  Utilizado por el nuevo plugin de integración con el WS de NextRet para grabar un documento
     * @param gVO: GeneralValueObject
     * @param params: Parámetros de conexión a la BBDD
     */
     public int grabarDocumentoContenido(GeneralValueObject gvo,String[] params) throws TechnicalException{
        int resultado;
        String numDoc = "", numDocExp;
        String codMunicipio     = (String)gvo.getAtributo("codMunicipio");
        String codProcedimiento = (String)gvo.getAtributo("codProcedimiento");
        String ejercicio        = (String)gvo.getAtributo("ejercicio");
        String numeroRelacion   = (String)gvo.getAtributo("numeroRelacion");
        String codTramite       = (String)gvo.getAtributo("codTramite");
        String ocurrenciaTramite= (String)gvo.getAtributo("ocurrenciaTramite");
        String codDocumento     = (String)gvo.getAtributo("codDocumento");
        String codUsuario       = (String)gvo.getAtributo("codUsuario");
        String nombreDocumento  = (String)gvo.getAtributo("nombreDocumento");
        String numeroDocumento  = (String)gvo.getAtributo("numeroDocumento");
        String opcionGrabar     = (String)gvo.getAtributo("opcionGrabar");
        byte[] ficheroWord      = (byte[])gvo.getAtributo("ficheroWord");

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        ResultSet rs;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            String sql;
            String codEstadoFirma = null;
            if ( ! (numeroDocumento!=null && !numeroDocumento.equals("")) ) {
                StringBuffer query = new StringBuffer("SELECT DOT_FRM FROM E_DOT WHERE ");
                query.append("(DOT_MUN=").append(codMunicipio).append(") AND ");
                query.append("(DOT_PRO='").append(codProcedimiento).append("') AND ");
                query.append("(DOT_TRA=").append(codTramite).append(") AND ");
                query.append("(DOT_COD=").append(codDocumento).append(")");
                Statement statement = conexion.createStatement();
                ResultSet resultSet = statement.executeQuery(query.toString());
                String tmp = null;
                if (resultSet.next()) {
                    tmp = resultSet.getString(1);
                }
                SigpGeneralOperations.closeResultSet(resultSet);
                SigpGeneralOperations.closeStatement(statement);
                if (tmp!=null) {
                    if (tmp.equalsIgnoreCase("O")) {
                        codEstadoFirma = "E";
                    } else {
                        codEstadoFirma = tmp.toUpperCase();
                    }
                }
            }

            if(numeroDocumento!=null && !numeroDocumento.equals("")){
                sql = "UPDATE G_CRD "+
                        "SET CRD_FMO = " + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ", CRD_USM = ?, CRD_DES= ?, CRD_FIL = ? "+
                        "WHERE CRD_PRO = ? AND"+
                        "      CRD_EJE = ? AND"+
                        "      CRD_NUM = ? AND"+
                        "      CRD_TRA = ? AND"+
                        "      CRD_OCU = ? AND"+
                        "      CRD_NUD = ? ";
            } else {
                sql = "INSERT INTO G_CRD VALUES "+
                        "(?,?,?,?,?,?,?,"+ abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," +
                        abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ",?,?,?,?,?,?, NULL, NULL, NULL, ?)";
            }
            PreparedStatement stmt = conexion.prepareStatement(sql);
            if(numeroDocumento!=null && !numeroDocumento.equals("")){
                numDoc = numeroDocumento;
                stmt.setString(1,codUsuario);
                stmt.setString(2,nombreDocumento);
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                stmt.setBinaryStream(3,st,ficheroWord.length);
                stmt.setString(4,codProcedimiento);
                stmt.setString(5,ejercicio);
                stmt.setString(6,numeroRelacion);
                stmt.setString(7,codTramite);
                stmt.setString(8,ocurrenciaTramite);
                stmt.setString(9,numeroDocumento);
            }else{                
                numDoc = this.obtenMaximo(abd, gvo,params);                
                sql = "INSERT INTO G_CRD(CRD_MUN,CRD_PRO,CRD_EJE,CRD_NUM,CRD_TRA,CRD_OCU,CRD_NUD,CRD_FAL,CRD_FMO," +
                        "CRD_USM,CRD_USC,CRD_FIL,CRD_DES,CRD_DOT,CRD_EXP,CRD_FIR_EST,CRD_EXP_FD,CRD_DOC_FD,CRD_FIR_FD)VALUES "+
                        "(" + codMunicipio + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numeroRelacion + "'," + codTramite + "," + ocurrenciaTramite +
                        "," + numDoc + "," + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) +
                        "," + codUsuario + "," + codUsuario + ",?," + "'" + nombreDocumento + "',"  + codDocumento + ",'" +opcionGrabar + "',?,NULL,NULL,NULL)";

                m_Log.debug("==========> sql:  " + sql);
                stmt.close();
                stmt = conexion.prepareStatement(sql);                       
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                stmt.setBinaryStream(1,st,ficheroWord.length);

                if(codEstadoFirma!=null)
                    stmt.setString(2, codEstadoFirma);
                else
                    stmt.setNull(2, java.sql.Types.VARCHAR);
            }
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resultado = stmt.executeUpdate();
            SigpGeneralOperations.closeStatement(stmt);

            if(numeroDocumento!=null && !numeroDocumento.equals("")){  //Modificar
                if(m_Log.isDebugEnabled()) m_Log.debug("MODIFICAR.  OPCION GRABAR ... " + opcionGrabar);
                if(opcionGrabar.equals("1")) {  //Todos los expedientes
                    //Recupero los expedientes
                    Vector<GeneralValueObject> exps = new Vector<GeneralValueObject>();
                    sql = "SELECT EXP_CRD_EJE, EXP_CRD_MUN, EXP_CRD_NUD, EXP_CRD_NUM, EXP_CRD_OCU, EXP_CRD_PRO, EXP_CRD_TRA" +
                            " FROM G_CRD_EXP WHERE REL_CRD_MUN = " + codMunicipio + " AND REL_CRD_PRO='" +
                            codProcedimiento + "' AND REL_CRD_EJE =" + ejercicio + " AND REL_CRD_NUM ='" +
                            numeroRelacion + "' AND REL_CRD_TRA =" + codTramite + " AND REL_CRD_OCU = " +
                            ocurrenciaTramite + " AND REL_CRD_NUD = " + numeroDocumento;
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    Statement stmt2 = conexion.createStatement();
                    rs = stmt2.executeQuery(sql);
                    while ( rs.next() ) {
                        GeneralValueObject exp = new GeneralValueObject();
                        exp.setAtributo("ejercicio", rs.getString("EXP_CRD_EJE"));
                        exp.setAtributo("municipio", rs.getString("EXP_CRD_MUN"));
                        exp.setAtributo("numDoc", rs.getString("EXP_CRD_NUD"));
                        exp.setAtributo("numExp", rs.getString("EXP_CRD_NUM"));
                        exp.setAtributo("ocurrencia", rs.getString("EXP_CRD_OCU"));
                        exp.setAtributo("procedimiento", rs.getString("EXP_CRD_PRO"));
                        exp.setAtributo("tramite", rs.getString("EXP_CRD_TRA"));
                        exps.add(exp);
                    }
                    SigpGeneralOperations.closeResultSet(rs);
                    SigpGeneralOperations.closeStatement(stmt2);

                    for (GeneralValueObject exp : exps) {
                        sql = "UPDATE E_CRD " +
                                "SET CRD_FMO = " + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ", CRD_USM = ?, CRD_DES= ?, CRD_FIL = ? " +
                                "WHERE CRD_PRO = ? AND" +
                                "      CRD_EJE = ? AND" +
                                "      CRD_NUM = ? AND" +
                                "      CRD_TRA = ? AND" +
                                "      CRD_OCU = ? AND" +
                                "      CRD_NUD = ? ";
                        stmt = conexion.prepareStatement(sql);
                        stmt.setString(1, codUsuario);
                        stmt.setString(2, nombreDocumento);
                        InputStream st = new ByteArrayInputStream(ficheroWord);
                        stmt.setBinaryStream(3, st, ficheroWord.length);
                        stmt.setString(4, (String) exp.getAtributo("procedimiento"));
                        stmt.setString(5, (String) exp.getAtributo("ejercicio"));
                        stmt.setString(6, (String) exp.getAtributo("numExp"));
                        stmt.setString(7, (String) exp.getAtributo("tramite"));
                        stmt.setString(8, (String) exp.getAtributo("ocurrencia"));
                        stmt.setString(9, (String) exp.getAtributo("numDoc"));
                        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                        resultado = stmt.executeUpdate();
                        SigpGeneralOperations.closeStatement(stmt);
                    }
                }
            }else{ // Insertar
                if(opcionGrabar.equals("1")) { //Todos

                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio",codMunicipio);
                    gVO.setAtributo("codProcedimiento",codProcedimiento);
                    gVO.setAtributo("ejercicio",ejercicio);
                    gVO.setAtributo("numero",numeroRelacion);
                    Vector expedientes = FichaRelacionExpedientesManager.getInstance().cargaListaExpedientes(gVO, params);
                    for (Object objExpediente : expedientes) {
                        GeneralValueObject expediente = (GeneralValueObject) objExpediente;
                        String numExp = (String) expediente.getAtributo("numExp");
                        m_Log.debug("EXPEDIENTE .... " + numExp);
                        gvo.setAtributo("numeroExpediente", numExp);
                        numDocExp = this.obtenMaximoExpediente(abd, gvo, params);
                        DocumentosExpedienteManager.getInstance().grabarDocumento(gvo, params);
                        sql = "INSERT INTO G_CRD_EXP VALUES " +
                                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        stmt = conexion.prepareStatement(sql);
                        stmt.setString(1, codMunicipio);
                        stmt.setString(2, codProcedimiento);
                        stmt.setString(3, ejercicio);
                        stmt.setString(4, numeroRelacion);
                        stmt.setString(5, codTramite);
                        stmt.setString(6, ocurrenciaTramite);
                        stmt.setString(7, numDoc);
                        stmt.setString(8, codMunicipio);
                        stmt.setString(9, codProcedimiento);
                        stmt.setString(10, ejercicio);
                        stmt.setString(11, numExp);
                        stmt.setString(12, codTramite);
                        stmt.setString(13, ocurrenciaTramite);
                        stmt.setString(14, numDocExp);
                        resultado = stmt.executeUpdate();
                        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                        SigpGeneralOperations.closeStatement(stmt);
                    }
                }
            }
            SigpGeneralOperations.commit(abd, conexion);

        }catch (Exception sqle){
            SigpGeneralOperations.rollBack(abd, conexion);
            resultado=0;
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("SQLException haciendo rollBackTransaction en: " + getClass().getName());
        } finally {
            SigpGeneralOperations.devolverConexion(abd, conexion);
            if (numDoc != null && !"".equals(numDoc)) {
                return Integer.parseInt(numDoc);
            } else {
                return 1;
            }
        }        
    }

     
     //Recuperamos las etiquetas de los módulos externos, se mira para todos los módulos
     //activos que etiquetas tienen.
      public ArrayList<EstructuraEtiquetaModuloIntegracionVO> getEtiquetasModulosExternos(String codOrganizacion, String codigoProcedimiento, String interesado, String relacion, String[] params) {
          
      if (m_Log.isDebugEnabled()) m_Log.debug("getEtiquetasModulosExternos.BEGIN");
          ArrayList<ModuloIntegracionExterno> modulos= new ArrayList<ModuloIntegracionExterno>();
          ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasModulo=new ArrayList<EstructuraEtiquetaModuloIntegracionVO>();
          int codOrganizacionInt=Integer.parseInt(codOrganizacion);
          modulos=
                  ModuloIntegracionExternoFactoria.getInstance().getImplClassModuloConEtiquetas(codOrganizacionInt,
                  codigoProcedimiento, params,interesado,
                  relacion);
          
           if (m_Log.isDebugEnabled()) m_Log.debug("getEtiquetasModulosExternos:Tamanho de la lista devuelta:"+modulos.size());  
          //Imprimimos para debug los modulos..
            for (int i = 0; i < modulos.size(); i++) {
                ModuloIntegracionExterno modulo=modulos.get(i); 
                if (m_Log.isDebugEnabled()) m_Log.debug("getEtiquetasModulosExternos:Modulo:"+modulo.getNombreModulo());
                if (m_Log.isDebugEnabled()) m_Log.debug("getEtiquetasModulosExternos: Descripcion del Modulo:"+modulo.getDescripcionModulo());
                if (m_Log.isDebugEnabled()) m_Log.debug("getEtiquetasModulosExternos:Etiquetas del modulo:"+modulo.getEtiquetas());
                etiquetasModulo.addAll(modulo.getEtiquetas());
              

            }
          
          if (m_Log.isDebugEnabled()) m_Log.debug("Lista de las etiquetas:"+etiquetasModulo.toString());
        return etiquetasModulo;

         
}     

    /**
     * Método auxiliar, se utiliza para dado un codInteresado, nos diga
     * a que expediente pertenece.
     * @param mapa con expediente como clave, y códigos de interesados como value
     * @param codInteresado codigo del interesado del que queremos conocer el numExpediente al que pertenece
     * @return 
     */  
    private String dameExpedienteDeInteresado(HashMap mapa,String codInteresado){
      
      if (m_Log.isDebugEnabled()) m_Log.debug("dameExpedienteDeInteresado.BEGIN.El interesado es:"+codInteresado);     
      
      Set claves=null;
      if(mapa!=null){
      claves= mapa.keySet();
      }
      Object[] objetos=null;
      if(claves!=null){
        objetos= claves.toArray();
        if (m_Log.isDebugEnabled()) m_Log.debug("Tamanho de objetos es : "+ objetos.length);  
      }
      String numExpediente="";
      if (objetos!=null){
        for(int i=0; i<objetos.length; i++){
          numExpediente= (String) objetos[i];  
         if (m_Log.isDebugEnabled()) m_Log.debug("El expediente, dentro de dameExpedienteDeInteresado es:"+numExpediente);  
      
         Vector interesados=(Vector) mapa.get(numExpediente);
         
          
          if(interesados!=null){
             if (m_Log.isDebugEnabled()) m_Log.debug("El tamanho de la lista de interesados es:"+interesados.size());    
             for(int j=0; j<interesados.size(); j++){
               InteresadoExpedienteVO interesadoAAnalizar=(InteresadoExpedienteVO) interesados.elementAt(j);
               if (m_Log.isDebugEnabled()) m_Log.debug("El interesadoAAnalizar es:"+interesadoAAnalizar.getCodTercero());
               String codInteresadoS=String.valueOf((interesadoAAnalizar.getCodTercero()));
               if (codInteresado.equals(codInteresadoS)){
                   if (m_Log.isDebugEnabled()) m_Log.debug("dameExpedienteDeInteresado.END.El numeroExpediente es:"+numExpediente);     
                   return numExpediente;
               }

            }
          } 
        }
      }
      if (m_Log.isDebugEnabled()) m_Log.debug("dameExpedienteDeInteresado.END.El numeroExpediente es:"+numExpediente);     
      return numExpediente;
    }
    
 
    
    /**
     * Recupera el contenido binario de un documento de tramitación perteneciente a una relación
     * entre expediente:
     * @param gvo: Instancia de la clase GeneralValueObject con los datos del documento a recuperar
     * @param con: Conexión a la BBDD
     * @return byte[] con el contenido del documento o null en caso contrario
     * @throws TechnicalException: Si ocurre algún error.
     */
    public byte[] loadDocumento(GeneralValueObject gvo,Connection con) throws TechnicalException{
        
        byte[] r = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try{
            
            // Creamos la select con los parametros adecuados.
            StringBuffer sql = new StringBuffer();
            sql.append(" select CRD_FIL ")
                    .append(" FROM G_CRD ")
                    .append(" WHERE CRD_NUM=? AND CRD_TRA=? AND CRD_OCU=? AND CRD_NUD=?");

            stmt = con.prepareStatement(sql.toString());
            if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
            stmt.setString(1,(String)gvo.getAtributo("numeroRelacion"));
            stmt.setInt(2,new Integer((String)gvo.getAtributo("codTramite")));
            stmt.setInt(3,new Integer((String)gvo.getAtributo("ocurrenciaTramite")));
            stmt.setInt(4, new Integer((String) gvo.getAtributo("numeroDocumento")));
            rs = stmt.executeQuery();
            while(rs.next())
            {
                java.io.InputStream st = rs.getBinaryStream("CRD_FIL");
                java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                int c;
                while ((c = st.read())!= -1){
                    ot.write(c);
                }
                ot.flush();
                r = ot.toByteArray();
                ot.close();
                st.close();
            }
        }catch (Exception sqle){
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Error de SQL en loadDocumento: " + sqle.toString());
        }finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);            
        }
        return r;
    }
    
    
     
}