package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.editor.mantenimiento.persistence.manual.DocumentosAplicacionDAO;
import es.altia.agora.business.geninformes.GeneradorInformesMgr;
import es.altia.agora.business.geninformes.utils.UtilidadesXerador;
import es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades;
import es.altia.agora.business.geninformes.utils.XeracionInformes.NodoEntidad;
import es.altia.agora.business.sge.MetadatosDocumentoVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.FichaExpedienteManager;
import es.altia.agora.business.sge.persistence.TramitacionExpedientesManager;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoFirma;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoFirmaBBDD;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.business.terceros.persistence.manual.DatosSuplementariosTerceroManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.EstructuraCampo;
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
import es.altia.util.jdbc.JdbcOperations;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.regex.Pattern;


public class DocumentosExpedienteDAO
{
    //Para el fichero de configuracion tecnico.
    protected static Config conf;
    //Para informacion de logs.
    protected static Log m_Log =
            LogFactory.getLog(DocumentosExpedienteDAO.class.getName());

    private static DocumentosExpedienteDAO instance = null;

    protected DocumentosExpedienteDAO()
    {
        super();
        //Queremos usar el fichero de configuracion techserver
        conf = ConfigServiceHelper.getConfig("techserver");
    }

    public static DocumentosExpedienteDAO getInstance()
    {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        synchronized(DocumentosExpedienteDAO.class)
            {
                if (instance == null)
                    instance = new DocumentosExpedienteDAO();
            }
        return instance;
    }

    private void rollBackTransaction(AdaptadorSQLBD bd,Connection con,Exception e){
        try{
            bd.rollBack(con);
            bd.devolverConexion(con);
        }catch (Exception e1){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("SQLException haciendo rollBackTransaction en: " + getClass().getName());
        }
    }

    private void commitTransaction(AdaptadorSQLBD bd,Connection con){
        try{
            bd.finTransaccion(con);
            bd.devolverConexion(con);
        }catch (Exception ex){
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("SQLException haciendo commitTransaction en: " + getClass().getName());
        }
    }

    public int grabarDocumento(GeneralValueObject gvo,String[] params){
        int resultado=0;
        String codMunicipio     = (String)gvo.getAtributo("codMunicipio");
        String codAplicacion    = (String)gvo.getAtributo("codAplicacion");
        String codProcedimiento = (String)gvo.getAtributo("codProcedimiento");
        String ejercicio        = (String)gvo.getAtributo("ejercicio");
        String numeroExpediente = (String)gvo.getAtributo("numeroExpediente");
        String codTramite       = (String)gvo.getAtributo("codTramite");
        String ocurrenciaTramite= (String)gvo.getAtributo("ocurrenciaTramite");
        String codDocumento     = (String)gvo.getAtributo("codDocumento");
        String codUsuario       = (String)gvo.getAtributo("codUsuario");
        String nombreDocumento  = (String)gvo.getAtributo("nombreDocumento");
        String numeroDocumento  = (String)gvo.getAtributo("numeroDocumento");
        byte[] ficheroWord      = (byte[])gvo.getAtributo("ficheroWord");
        Boolean insertarMetadatoEnBBDD = (Boolean)gvo.getAtributoONulo("insertarMetadatosEnBBDD");
        MetadatosDocumentoVO metadatos = (MetadatosDocumentoVO)gvo.getAtributoONulo("metadatosDocumento");
        

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        DocumentoDAO documentoDAO = DocumentoDAO.getInstance();
        TramitacionExpedientesDAO tramitacionExpedientesDAO = TramitacionExpedientesDAO.getInstance();
         
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            String sql = "";
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
                resultSet.close();
                statement.close();
                if (tmp!=null) {
                    if (tmp.equalsIgnoreCase("O")) {
                        codEstadoFirma = "E";
                    } else {
                        codEstadoFirma = tmp.toUpperCase();
                    }//if
                }//if
            }//if
            /* ******************************************************** */

            if(numeroDocumento!=null && !numeroDocumento.equals("")){
                sql = "UPDATE E_CRD "+
                        "SET CRD_FMO = " + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ", CRD_USM = ?, CRD_DES= ?, CRD_FIL = ? "+
                        "WHERE CRD_PRO = ? AND"+
                        "      CRD_EJE = ? AND"+
                        "      CRD_NUM = ? AND"+
                        "      CRD_TRA = ? AND"+
                        "      CRD_OCU = ? AND"+
                         "      CRD_NUD = ? AND"+
                        "      CRD_MUN = ? ";
            } else {
                /*
                sql = "INSERT INTO E_CRD(CRD_MUN,CRD_PRO,CRD_EJE,CRD_NUM,CRD_TRA,CRD_OCU,CRD_NUD,CRD_FAL,CRD_FMO," +
                        "CRD_USC,CRD_USM,CRD_FIL,CRD_DES,CRD_DOT,CRD_FIR_EST,CRD_EXP_FD,CRD_DOC_FD,CRD_FIR_FD) VALUES "+
                        "(?,?,?,?,?,?,?,"+ abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," +
                        abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ",?,?,?,?,?,?, NULL, NULL, NULL)";
                 */
                sql = "INSERT INTO E_CRD(CRD_MUN,CRD_PRO,CRD_EJE,CRD_NUM,CRD_TRA,CRD_OCU,CRD_NUD,CRD_FAL,CRD_FMO,CRD_FINF," +
                        "CRD_USC,CRD_USM,CRD_FIL,CRD_DES,CRD_DOT,CRD_FIR_EST,CRD_EXP_FD,CRD_DOC_FD,CRD_FIR_FD) VALUES "+
                        "(?,?,?,?,?,?,?,"+ abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," +
                        abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null)+  ",?,?,?,?,?,?, NULL, NULL, NULL)";

            }//if
            PreparedStatement stmt = conexion.prepareStatement(sql);
            if(numeroDocumento!=null && !numeroDocumento.equals("")){
                stmt.setString(1,codUsuario);
                stmt.setString(2,nombreDocumento);
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                stmt.setBinaryStream(3,st,ficheroWord.length);
                stmt.setString(4,codProcedimiento);
                stmt.setString(5,ejercicio);
                stmt.setString(6,numeroExpediente);
                stmt.setString(7,codTramite);
                stmt.setString(8,ocurrenciaTramite);
                stmt.setString(9,numeroDocumento);
                stmt.setString(10,codMunicipio);
            }else{
                stmt.setString(1,codMunicipio);
                stmt.setString(2,codProcedimiento);
                stmt.setString(3,ejercicio);
                stmt.setString(4,numeroExpediente);
                stmt.setString(5,codTramite);
                stmt.setString(6,ocurrenciaTramite);
                stmt.setString(7,this.obtenMaximo(abd, gvo,params));
                stmt.setString(8,codUsuario);
                stmt.setString(9,codUsuario);
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                stmt.setBinaryStream(10,st,ficheroWord.length);
                stmt.setString(11,nombreDocumento);
                stmt.setString(12,codDocumento);
                stmt.setString(13,codEstadoFirma);
            }
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            resultado = stmt.executeUpdate();
            stmt.close();
            
            // Insertar metadatos
            if (Boolean.TRUE.equals(insertarMetadatoEnBBDD)) {
                // Obtenemos el id del metadato del documento
                Long idMetadatoAntiguo = tramitacionExpedientesDAO.getDocumentoIdMetadato(
                        NumberUtils.createInteger(codDocumento),
                        NumberUtils.createInteger(ejercicio),
                        NumberUtils.createInteger(codMunicipio),
                        numeroExpediente,
                        NumberUtils.createInteger(codTramite),
                        NumberUtils.createInteger(ocurrenciaTramite),
                        codProcedimiento,
                        conexion);
                
                if (metadatos != null && StringUtils.isNotEmpty(metadatos.getCsv())) {
                    metadatos.setId(idMetadatoAntiguo);
                    
                    // Insertar el metadato CSV en la tabla de metadatos
                    Long idMetadatosNuevo = documentoDAO.insertarMetadatoCSV(metadatos, conexion, params);
                    
                    // Actualizar el CRD_ID_METADATO y el CRD_DOT de la tabla de los documentos de tramites
                    actualizarIdMetadato(
                        idMetadatosNuevo,
                        NumberUtils.createInteger(codDocumento),
                        NumberUtils.createInteger(ejercicio),
                        NumberUtils.createInteger(codMunicipio),
                        numeroExpediente,
                        NumberUtils.createInteger(codTramite),
                        NumberUtils.createInteger(ocurrenciaTramite),
                        codProcedimiento,
                        conexion);
                }
            }
            commitTransaction(abd,conexion);

        }catch (Exception sqle){
            rollBackTransaction(abd,conexion,sqle);
            resultado=0;
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("SQLException haciendo rollBackTransaction en: " + getClass().getName());
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
            if(m_Log.isDebugEnabled()) m_Log.debug("--> DATO: " + dato);
            if(m_Log.isDebugEnabled()) m_Log.debug("--> DATOTOKENIZADO: " + datoTokenizado);
        }
        return datoTokenizado;
    }


    private static String formatoFechaAlternativo(String codOrganizacion){
        String salida = null;
        try{
            ResourceBundle config = ResourceBundle.getBundle("documentos");
            salida = config.getString(codOrganizacion + ConstantesDatos.FORMATO_ALTERNATIVO_FECHAS_DOC_TRAMITACION);

        }catch(Exception e){
            m_Log.error("No se ha podido encontrar la propiedad: " + codOrganizacion + ConstantesDatos.FORMATO_ALTERNATIVO_FECHAS_DOC_TRAMITACION
                    + " en fichero de configuración documentos.properties");
            salida = null;
        }
        return salida;
    }
    /**
     * Este método se utilizaba en el antiguo proceso de creación de plantillas WORD y OOFFICE para preparar el xml con los pares etiqueta-valor para sustituir las etiquetas al generar
     * un documento de la plantilla 'No por interesado'
     * @param gvo
     * @param params
     * @return 
     */
    public String consultaXML(GeneralValueObject gvo,String[] params){

        String xml = "";
        String raiz = obtenEstructuraRaiz((String)gvo.getAtributo("codAplicacion"),params);
        if(m_Log.isDebugEnabled()) m_Log.debug("--> RAIZ en obten estructura2 : " + raiz);
        EstructuraEntidades eeRaiz;
        Vector listaIdiomas = (Vector) gvo.getAtributo("listaIdiomas");

        Vector<String> parametros = new Vector<String>();
        parametros.add((String)gvo.getAtributo("codProcedimiento"));
        parametros.add((String)gvo.getAtributo("numeroExpediente"));
        parametros.add((String)gvo.getAtributo("codTramite"));
        parametros.add((String)gvo.getAtributo("ocurrenciaTramite"));
        parametros.add((String)gvo.getAtributo("idioma"));
        
        String codOrganizacion=(String)gvo.getAtributo("codMunicipio");
        String codTramite=(String)gvo.getAtributo("codTramite");
        String ocuTramite=(String)gvo.getAtributo("ocurrenciaTramite"); 
                
        if(m_Log.isDebugEnabled()) m_Log.debug("codOrganizacion:"+codOrganizacion);
        if(m_Log.isDebugEnabled()) m_Log.debug("codTRAMITE:"+codTramite);
        if(m_Log.isDebugEnabled()) m_Log.debug("ocuTRAMITE:"+ocuTramite);
        
        if(m_Log.isDebugEnabled()) m_Log.debug("COD_PROC::"+gvo.getAtributo("codProcedimiento"));
        if(m_Log.isDebugEnabled()) m_Log.debug("NUM_EXP::"+gvo.getAtributo("numeroExpediente"));
        if(m_Log.isDebugEnabled()) m_Log.debug("COD_TRA::"+gvo.getAtributo("codTramite"));
        if(m_Log.isDebugEnabled()) m_Log.debug("OCU_TRA::"+gvo.getAtributo("ocurrenciaTramite"));
        if(m_Log.isDebugEnabled()) m_Log.debug("IDIOMA ::"+gvo.getAtributo("idioma"));
        if(m_Log.isDebugEnabled()) m_Log.debug("EJERCICIO ::"+gvo.getAtributo("ejercicio"));
           
       

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
        String numExpediente = (String)gvo.getAtributo("numeroExpediente");
        String ejercicio = (String) gvo.getAtributo("ejercicio");
        
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
            eeRaiz = UtilidadesXerador.construyeEstructuraEntidadesInformeDocumento(params,"",raiz,parametros);
            //eeRaiz = UtilidadesXerador.construyeEstructuraEntidadesInforme(params,"",raiz);
            eeRaiz.setValoresParametrosConsulta(parametros);
            UtilidadesXerador.construyeEstructuraDatos(params,eeRaiz,etiquetasFecha);
            TercerosManager tercerosManager = TercerosManager.getInstance();
            DatosSuplementariosTerceroManager datosSuplementariosManager = DatosSuplementariosTerceroManager.getInstance();

            Vector listas = getValoresDatosSuplementarios(gvo,params);
            Vector estructuraDatosSuplementarios = (Vector) listas.firstElement();
            Vector valoresDatosSuplementarios = (Vector) listas.lastElement();
            Vector listaEstructuraInteresados = getEstructuraInteresados(gvo,params);

            Collection col = eeRaiz.getListaInstancias();

            boolean etiquetasRegistro = false;
            for (Object aCol : col) {
                NodoEntidad n = (NodoEntidad) aCol;                
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
                // Fecha actual en euskera
                //fecha = n.getCampo("FECACTEU");
                if (fecha != null) {
                    n.remove("FECACTEU");
                    n.addCampo("FECACTEU", DateOperations.traducirFechaEuskera(fecha));
                }
                
                String fechaEntradaRegistro = n.getCampo("FECENTREGISTRO");                
                if(fechaEntradaRegistro!=null && !"".equals(fechaEntradaRegistro) && formatoFechaAlternativo){                     
                    // Si hay fecha de entrada en registro y está activado el formato de fecha alternativo
                    n.addCampo("FECENTREGISTRO" + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA, DateOperations.formatoAlternativoCampoFecha(fechaEntradaRegistro,FORMATO_FECHA_ALTERNATIVO));                    
                }

                // Cargos 
                Vector listaCargos = getListaCargos(params, (String) gvo.getAtributo("codAplicacion"));
                for (int i = 0; i < listaCargos.size(); i++) {
                    GeneralValueObject g = (GeneralValueObject) listaCargos.elementAt(i);
                    String cargo = (String) g.getAtributo("cargo");
                    String nombre = (String) g.getAtributo("nombre");

                    String cargoTokenizado = eliminarCaracteresProhibidos(cargo);
                    n.remove(cargoTokenizado);
                    n.addCampo(cargoTokenizado, nombre);
                }
                
                // Interesados                
                Vector listaInteresados = (Vector) campos.get("INTERESADO");
                if (listaInteresados != null) {
                    
                    ArrayList<TercerosValueObject> terceros = new ArrayList<TercerosValueObject>();
                    terceros = tercerosManager.getInteresadosExpediente(codMunicipio, numExpediente, codProcedimiento, ejercicio, params);
                    
                    //Estructura datos suplementarios
                    Vector<EstructuraCampo> datosSuplementarios = 
                    datosSuplementariosManager.cargaEstructuraDatosSuplementariosTercero(codMunicipio, params);
                    
                    for(int i = 0; i < listaInteresados.size(); i++){
                        NodoEntidad nInt = (NodoEntidad) listaInteresados.elementAt(i);
                           m_Log.debug("_________________________________________________________________nInt " +i+"-"+ nInt);
                        for(int j = 0; j < listaEstructuraInteresados.size(); j++){
                            EstructuraCampo eC = (EstructuraCampo) listaEstructuraInteresados.elementAt(j);


                            m_Log.debug("*******************************************************************nInt " +j+"-"+eC);
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
                            String mailInt = nInt.getCampo("MAILINTERESADO");
                            String codPostalInt = nInt.getCampo("CODPOSTALINTERESADO");

                            String descPrv = nInt.getCampo("PROVINTERESADO");                           

                            String descNombreInt = "Nombre" + descRolInterTokenizado;
                            String descApel1Int = "Apel1" + descRolInterTokenizado;
                            String descApel2Int = "Apel2" + descRolInterTokenizado;
                            String descDocInt = "Doc" + descRolInterTokenizado;
                            String descDomInt = "Dom" + descRolInterTokenizado;
                            //Para el rol del interesado
                            String descRol = "Rol" + descRolInterTokenizado;
                            //Para la poblacion del interesado
                            String descPobInt = "Pob" + descRolInterTokenizado;
                            String descProInt = "Provincia" + descRolInterTokenizado;
                            String descTlfnoInt = "Tlfno" + descRolInterTokenizado;
                            String descMail = "Mail"+descRolInterTokenizado;
                            String descCodPostalInt = "CodPostal" + descRolInterTokenizado;
                            String poblacionInteresado = nInt.getCampo("POBLACIONINTERESADO");
                            nInt.remove("COSPOSTALINTERESADO");
                            if(rolInteresado.equals(descRolInter)){
                                //Para el nombre del interesado
                                nInt.addCampo(descRolInterTokenizado, nombreCompletoInt);
                                nInt.addCampo(descNombreInt, nombreInt);
                                nInt.addCampo(descApel1Int, apel1Int);
                                nInt.addCampo(descApel2Int, apel2Int);
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
                                nInt.addCampo(descMail, mailInt);
                                nInt.addCampo(descTlfnoInt, tlfnoInt);
                                nInt.addCampo(descCodPostalInt, codPostalInt);
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
                                            
                                            datoSuplementario.getCodPlantilla();
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
                                                            
                                                            // nuevo ===>
                                                            if(dato==null) dato = "";
                                                            // nuevo <====
                                                            nInt.addCampo(nombreCampo+descRolInterTokenizado, dato);
                                                            
                                                            // nuevo ===>
                                                           if(dato!=null &&formatoFechaAlternativo && codPlantillaTercero!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(codPlantillaTercero)){
                                                                String nuevoCampoTokenizado = eliminarCaracteresProhibidos(nombreCampo + descRolInterTokenizado + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                                                nInt.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(dato,FORMATO_FECHA_ALTERNATIVO));
                                                            }
                                                            // nuevo <====
                                                            
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
                                nInt.addCampo(descDomInt, "");
                                nInt.addCampo(descRol, "");
                                nInt.addCampo(descPobInt, "");
                                nInt.addCampo(descTlfnoInt, "");
                                nInt.addCampo(descCodPostalInt,"");
                                nInt.addCampo(descProInt, "");
                                nInt.addCampo(descMail, "");
                                for(EstructuraCampo datoSuplementario : datosSuplementarios){
                                    String nombreCampo = datoSuplementario.getCodCampo();
                                    nInt.addCampo(nombreCampo+descRolInterTokenizado, "");
                                }//for(EstructuraCampo datoSuplementario : datosSuplementarios)
                            }//if(rolInteresado.equals(descRolInter))
                        }//for(int j = 0; j < listaEstructuraInteresados.size(); j++)
                    }//for(int i = 0; i < listaInteresados.size(); i++)
                }/*else{
                     listaInteresados = rellenarListaInteresadosVacia(listaEstructuraInteresados);
                     campos.put(ConstantesDatos.NODO_INTERESADO,listaInteresados);
                }*/
                
                // Datos Suplementarios
                for (int i = 0; i < valoresDatosSuplementarios.size(); i++) {
                    CamposFormulario cF = (CamposFormulario) valoresDatosSuplementarios.elementAt(i);
                    EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                    String valorCampo = cF.getString(eC.getCodCampo());
                    if (eC.getCodTipoDato().equals("6")) {	
                        valorCampo = getDescripcionValorDesplegable(eC, valorCampo);	
                        if (listaIdiomas.size()>1){	
                            if(valorCampo != null && !"".equals(valorCampo)){	
                                String campoTokenizado = eliminarCaracteresProhibidos (eC.getCodCampo());	
                                String [] valorCampoArray = valorCampo.split(Pattern.quote("|"));	
                                String valorIdiomaAlternativo = "";	
                                if(valorCampoArray.length>1){	
                                    valorIdiomaAlternativo = valorCampoArray[1];	
                                    valorCampo = valorCampoArray[0];	
                                }	
                                GeneralValueObject idioma = (GeneralValueObject) listaIdiomas.get(1);	
                                n.addCampo(campoTokenizado+"_"+((String) idioma.getAtributo("descripcion")).toUpperCase(), valorIdiomaAlternativo);	
                            }	
                        }	
                    }
                    if (valorCampo != null && !"".equals(valorCampo)) {
                        valorCampo = formatoValorNumerico(valorCampo);
                        String campoTokenizado = eliminarCaracteresProhibidos(eC.getCodCampo());
                        n.addCampo(campoTokenizado, valorCampo);

                        
                        // Si el campo suplementario es de tipo fecha y está habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                        // otro nombre y con el mismo valor que la original sólo que con el nuevo formato de fecha
                        if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                            String nuevoCampoTokenizado = eliminarCaracteresProhibidos(eC.getCodCampo() + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                            n.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                        }
                        
                    }



                    String idCampoConPrefijo = "T" + gvo.getAtributo("codTramite") + eC.getCodCampo();
                    valorCampo = cF.getString(idCampoConPrefijo);
                    if (eC.getCodTipoDato().equals("6")) {	
                        valorCampo = getDescripcionValorDesplegable(eC, valorCampo);	
                        if(listaIdiomas.size()>1){	
                            if (valorCampo != null && !"".equals(valorCampo)) {	
                                 String campoTokenizado = eliminarCaracteresProhibidos(idCampoConPrefijo);	
                                  String [] valorCampoArray = valorCampo.split(Pattern.quote("|"));	
                                  String valorIdiomaAlternativo = "";	
                                  if (valorCampoArray.length > 1) {	
                                       valorIdiomaAlternativo = valorCampoArray[1];	
                                       valorCampo = valorCampoArray[0];	
                                  }	
                                  GeneralValueObject idioma = (GeneralValueObject) listaIdiomas.get(1);	
                                  n.addCampo(campoTokenizado+"_"+((String) idioma.getAtributo("descripcion")).toUpperCase(), valorIdiomaAlternativo);	
                            }	
                        }	
                    }                                                                                                
                    if (valorCampo != null && !"".equals(valorCampo)) {
                        valorCampo = formatoValorNumerico(valorCampo);
                        String campoTokenizado = eliminarCaracteresProhibidos(idCampoConPrefijo);
                        n.addCampo(campoTokenizado, valorCampo);

                        
                        // Si el campo suplementario es de tipo fecha y está habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                        // otro nombre y con el mismo valor que la original sólo que con el nuevo formato de fecha
                        if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                                String nuevoCampoTokenizado = eliminarCaracteresProhibidos(idCampoConPrefijo + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                            n.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                        }
                        
                    }
                }
          
            
            
            //Etiquetas que vienen de los módulos externos
            //Plantilla Interesado -> N
            //Plantilla Relación -> N    
             ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasModulosExternos= getEtiquetasModulosExternos(codOrganizacion,codProcedimiento,"N", "N", params);
              for (int i = 0; i < etiquetasModulosExternos.size(); i++) {
                 EstructuraEtiquetaModuloIntegracionVO etiqueta  = (EstructuraEtiquetaModuloIntegracionVO) etiquetasModulosExternos.get(i);
                
                 //Ahora tenemos que recuperar el valor de la etiqueta, y anhadirla..
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es el numExpediente pasado: "+ numExpediente);
                 String valor=ModuloIntegracionExternoManager.getInstance().getValorDeEtiqueta(params, etiqueta, numExpediente,codTramite, ocuTramite);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es numExpediente: "+numExpediente);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es codTramite: "+codTramite);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es codTramite: "+ocuTramite);
                 n.addCampo(etiqueta.getNombreEtiqueta(),valor);
                
              }
              
              //n.addCampo("IMPORTE","11");
              if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es n"+n);
           }//Fin del While   
            /////////////////////
           
            xml = new GeneradorInformesMgr(params).generaXMLResultado(eeRaiz);

        }catch(TechnicalException te){
            te.printStackTrace();
            m_Log.debug("**************** Exception capturada en: " + getClass().getName() + ": " + te.getMessage());
            if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName() + ": " + te.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            m_Log.debug("**************** Exception capturada en: " + getClass().getName() + ": " + e.getMessage());
            if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName() + ": " + e.getMessage());
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("XML ::"+xml);
        return xml;
    }


    
    /**
     * Este método se utilizaba en el antiguo proceso de creación de plantillas WORD y OOFFICE para preparar el xml con los pares etiqueta-valor para sustituir las etiquetas al generar
     * * un documento de la plantilla 'Por interesado'
     * @param gvo
     * @param params
     * @return 
     */
    public String consultaXML2(GeneralValueObject gvo,String[] params){
        String xml = "";
        String raiz = obtenEstructuraRaiz2((String)gvo.getAtributo("codAplicacion"),params);
        if(m_Log.isDebugEnabled()) m_Log.debug("--> RAIZ en obten estructura2 : " + raiz);
        EstructuraEntidades eeRaiz = null;
        Vector listaIdiomas = (Vector) gvo.getAtributo(("listaIdiomas"));
        Vector listaCodInteresados = listaCodInteresados = (Vector) gvo.getAtributo("listaCodInteresados");
        Vector listaVersInteresados = listaVersInteresados = (Vector) gvo.getAtributo("listaVersInteresados");
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

        String codOrganizacionM=(String)gvo.getAtributo("codMunicipio");
        String codTramite=(String)gvo.getAtributo("codTramite");
        String ocuTramite=(String)gvo.getAtributo("ocurrenciaTramite"); 
                
        if(m_Log.isDebugEnabled()) m_Log.debug("codOrganizacion:"+codOrganizacionM);
        if(m_Log.isDebugEnabled()) m_Log.debug("codTRAMITE:"+codTramite);
        if(m_Log.isDebugEnabled()) m_Log.debug("ocuTRAMITE:"+ocuTramite);
        
        String numExpediente=(String)gvo.getAtributo("numeroExpediente");
         if(m_Log.isDebugEnabled()) m_Log.debug("NumeroExpediente:"+numExpediente);
        try{
            eeRaiz = UtilidadesXerador.construyeEstructuraEntidadesInforme(params,"",raiz);
            for(int m=0;m<listaCodInteresados.size();m++) {
                Vector parametros = new Vector();
                parametros.add((String)gvo.getAtributo("codProcedimiento"));
                parametros.add((String)gvo.getAtributo("numeroExpediente"));
                parametros.add((String)gvo.getAtributo("codTramite"));
                parametros.add((String)gvo.getAtributo("ocurrenciaTramite"));
                parametros.add((String)gvo.getAtributo("idioma"));
                parametros.add((String) listaCodInteresados.elementAt(m));
                parametros.add((String) listaVersInteresados.elementAt(m));
                if(m_Log.isDebugEnabled()) m_Log.debug("COD_PROC::"+gvo.getAtributo("codProcedimiento"));
                if(m_Log.isDebugEnabled()) m_Log.debug("NUM_EXP::"+gvo.getAtributo("numeroExpediente"));
                if(m_Log.isDebugEnabled()) m_Log.debug("COD_TRA::"+gvo.getAtributo("codTramite"));
                if(m_Log.isDebugEnabled()) m_Log.debug("OCU_TRA::"+gvo.getAtributo("ocurrenciaTramite"));
                if(m_Log.isDebugEnabled()) m_Log.debug("IDIOMA ::"+gvo.getAtributo("idioma"));
                if(m_Log.isDebugEnabled()) m_Log.debug("CODINTERESADO :: " + listaCodInteresados.elementAt(m));
                if(m_Log.isDebugEnabled()) m_Log.debug("VERSINTERESADO :: " + listaVersInteresados.elementAt(m));

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
                while (iterInst.hasNext()) {
                    NodoEntidad n = (NodoEntidad) iterInst.next();

                    String diaActual = n.getCampo("DIAACTUAL");
                    String mesActual = n.getCampo("MESACTUAL");
                    String anoActual = n.getCampo("ANOACTUAL");
               
                    String fecha = n.getCampo("FECACTGAL");
                    if (!"".equals(fecha) && fechaFormato(fecha,"dd/MM/yyyy")) {
                        n.remove("FECACTGAL");
                        n.addCampo("FECACTGAL",traducirFecha(fecha,"es","GL"));
                    }//if (!"".equals(fecha) && fechaFormato(fecha,"dd/MM/yyyy"))
                    fecha = n.getCampo("FECACTESP");
                    
                    if (!"".equals(fecha) && fechaFormato(fecha,"dd/MM/yyyy")) {
                        n.remove("FECACTESP");
                        n.addCampo("FECACTESP",traducirFecha(fecha,"es","ES"));
                    }//if (!"".equals(fecha) && fechaFormato(fecha,"dd/MM/yyyy")) 
                   
                    if (fecha != null && fechaFormato(fecha,"dd/MM/yyyy")) {
                        n.remove("FECACTEU");
                        n.addCampo("FECACTEU", DateOperations.traducirFechaEuskera(fecha));
                    }//if (fecha != null && fechaFormato(fecha,"dd/MM/yyyy")) 
              
                    if(m_Log.isDebugEnabled()) m_Log.debug("-->Cargamos lista de cargos:");
                    Vector listaCargos = new Vector();
                    listaCargos = getListaCargos(params,(String)gvo.getAtributo("codAplicacion"));
                    for(int i=0;i<listaCargos.size();i++) {
                        GeneralValueObject g = new GeneralValueObject();
                        g = (GeneralValueObject) listaCargos.elementAt(i);
                        String cargo = (String) g.getAtributo("cargo");
                        String nombre = (String) g.getAtributo("nombre");
                        //Esto es lo que he añadido
                        StringTokenizer valores = null;
                        String cargoTokenizado = eliminarCaracteresProhibidos(cargo);
                        n.remove(cargoTokenizado);
                        n.addCampo(cargoTokenizado,nombre);
                    }//for(int i=0;i<listaCargos.size();i++)
                    String domicilio = n.getCampo("DOMICILIOINTERESADO");
                    String poblacion = n.getCampo("POBLACIONINTERESADO");
                    String domicilioNoNormalizado = n.getCampo("DOMICILIONONORMALIZADO");
                    String poblacionNoNormalizado = n.getCampo("POBLACIONNONORMALIZADO");
                    String normalizado = n.getCampo("NORMALIZADO");
                    if(normalizado != null && normalizado.equals("2")){
                        n.remove("DOMICILIOINTERESADO");
                        n.addCampo("DOMICILIOINTERESADO",domicilioNoNormalizado);
                        n.remove("POBLACIONINTERESADO");
                        n.addCampo("POBLACIONINTERESADO", poblacionNoNormalizado);
                    }//if(normalizado != null && normalizado.equals("2")) 
                    
                    //Datos de terceros
                    TercerosManager tercerosManager = TercerosManager.getInstance();
                    DatosSuplementariosTerceroManager datosSuplementariosManager = DatosSuplementariosTerceroManager.getInstance();
                    TercerosValueObject tercero = tercerosManager.getDatosTercero((String)listaCodInteresados.get(m), params);
                    String codTercero = tercero.getIdentificador();
                    String codOrganizacion = (String) gvo.getAtributo("codMunicipio");
                    
                    if(!iterInst.hasNext()){
                        //Estructura datos suplementarios
                        Vector<EstructuraCampo> datosSuplementarios = 
                                datosSuplementariosManager.cargaEstructuraDatosSuplementariosTercero(codOrganizacion, params);

                        //CamposFormulario
                        Vector camposFormulario = 
                            datosSuplementariosManager.cargaValoresDatosSuplementariosConCodigo(codOrganizacion, codTercero, datosSuplementarios, params);

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
                                            valor = datosSuplementariosManager.getValorDesplegable(codOrganizacion, nombreCampo, (String) entry.getValue(), params);
                                        }else{
                                            valor = (String) entry.getValue();
                                        }//if(tipoDato.equalsIgnoreCase("6"))
                                        
                                        if(valor==null) valor="";
                                        n.addCampo(nombreCampo,valor);
                                        
                                        /** prueba ***/
                                        if(formatoFechaAlternativo && codPlantillaTercero!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(codPlantillaTercero)){
                                            String nuevoCampoTokenizado = eliminarCaracteresProhibidos(nombreCampo + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                            n.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valor,FORMATO_FECHA_ALTERNATIVO));
                                        }
                                        /** prueba ***/
                                        
                                    }//if(nombreCampo.equalsIgnoreCase(key))
                                }//while(itCampos.hasNext())
                            }//for(int z=0; z<camposFormulario.size(); z++)
                        }//for(EstructuraCampos datoSuplementario : datosSuplementarios)
                    }//if(!iterInst.hasNext())

                    // Datos Suplementarios
                    for (int i = 0; i < valoresDatosSuplementarios.size(); i++) {
                        CamposFormulario cF = (CamposFormulario) valoresDatosSuplementarios.elementAt(i);
                        EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                        String valorCampo = cF.getString(eC.getCodCampo());
                        if (eC.getCodTipoDato().equals("6")) {	
                             valorCampo = getDescripcionValorDesplegable(eC, valorCampo);	
                              if (listaIdiomas.size()>1){	
                                  if (valorCampo != null && !"".equals(valorCampo)) {	
                                      String campoTokenizado = eliminarCaracteresProhibidos(eC.getCodCampo());	
                                      String [] valorCampoArray = valorCampo.split(Pattern.quote("|"));	
                                       String valorIdiomaAlternativo = "";	
                                        if (valorCampoArray.length > 1) {	
                                            valorIdiomaAlternativo = valorCampoArray[1];	
                                            valorCampo = valorCampoArray[0];	
                                        }	
                                        GeneralValueObject idioma = (GeneralValueObject) listaIdiomas.get(1);	
                                        n.addCampo(campoTokenizado+"_"+((String) idioma.getAtributo("descripcion")).toUpperCase(), valorIdiomaAlternativo);	
                                  }	
                              }	
                        }
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
                        if (eC.getCodTipoDato().equals("6")) {	
                            valorCampo = getDescripcionValorDesplegable(eC, valorCampo);	
                            if (listaIdiomas.size()>1){	
                                 if (valorCampo != null && !"".equals(valorCampo)) {	
                                     String campoTokenizado = eliminarCaracteresProhibidos(idCampoConPrefijo);	
                                     String [] valorCampoArray = valorCampo.split(Pattern.quote("|"));	
                                     String valorIdiomaAlternativo = "";	
                                      if (valorCampoArray.length > 1) {	
                                          valorIdiomaAlternativo = valorCampoArray[1];	
                                          valorCampo = valorCampoArray[0];	
                                      }	
                                      GeneralValueObject idioma = (GeneralValueObject) listaIdiomas.get(1);	
                                       n.addCampo(campoTokenizado+"_"+((String) idioma.getAtributo("descripcion")).toUpperCase(), valorIdiomaAlternativo);	
                                 }	
                            }	
                        } 
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
                    }//for (int i = 0; i < valoresDatosSuplementarios.size(); i++) 
                
                    
            //Etiquetas que vienen de los módulos externos
             //Plantilla Interesado-> Si
             //Plantilla Relación-> NO       
             ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasModulosExternos= getEtiquetasModulosExternos(codOrganizacion,codProcedimiento,"S", "N", params);
              for (int i = 0; i < etiquetasModulosExternos.size(); i++) {
                 EstructuraEtiquetaModuloIntegracionVO etiqueta  = (EstructuraEtiquetaModuloIntegracionVO) etiquetasModulosExternos.get(i);
                
                 //Ahora tenemos que recuperar el valor de la etiqueta, y anhadirla..
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es el numExpediente pasado: "+ numExpediente);
                 String valor=ModuloIntegracionExternoManager.getInstance().getValorDeEtiqueta(params, etiqueta, numExpediente,codTramite, ocuTramite);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es numExpediente: "+numExpediente);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es codTramite: "+codTramite);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es codTramite: "+ocuTramite);
                 n.addCampo(etiqueta.getNombreEtiqueta(),valor);
                
              }
              
             }//while (iterInst.hasNext()) 
            }// for(int m=0;m<listaCodInteresados.size();m++) 
            xml = new GeneradorInformesMgr(params).generaXMLResultado2(eeRaiz);
         }catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("XML ::"+xml);
        return xml;
    }//consultaXML2

        
    
    
    public HashMap consultaHashEtiquetasValor(GeneralValueObject gvo,String[] params){

        HashMap resultado=new HashMap();
        List<Map> interesados = new ArrayList<Map>();
        String raiz = obtenEstructuraRaiz((String)gvo.getAtributo("codAplicacion"),params);
        if(m_Log.isDebugEnabled()) m_Log.debug("--> RAIZ en obten estructura2 : " + raiz);
        EstructuraEntidades eeRaiz;

        Vector<String> parametros = new Vector<String>();
        parametros.add((String)gvo.getAtributo("codProcedimiento"));
        parametros.add((String)gvo.getAtributo("numeroExpediente"));
        parametros.add((String)gvo.getAtributo("codTramite"));
        parametros.add((String)gvo.getAtributo("ocurrenciaTramite"));
        parametros.add((String)gvo.getAtributo("idioma"));
        
        String codOrganizacion=(String)gvo.getAtributo("codMunicipio");
        String codTramite=(String)gvo.getAtributo("codTramite");
        String ocuTramite=(String)gvo.getAtributo("ocurrenciaTramite"); 
                
        if(m_Log.isDebugEnabled()) m_Log.debug("codOrganizacion:"+codOrganizacion);
        if(m_Log.isDebugEnabled()) m_Log.debug("codTRAMITE:"+codTramite);
        if(m_Log.isDebugEnabled()) m_Log.debug("ocuTRAMITE:"+ocuTramite);
        
        if(m_Log.isDebugEnabled()) m_Log.debug("COD_PROC::"+gvo.getAtributo("codProcedimiento"));
        if(m_Log.isDebugEnabled()) m_Log.debug("NUM_EXP::"+gvo.getAtributo("numeroExpediente"));
        if(m_Log.isDebugEnabled()) m_Log.debug("COD_TRA::"+gvo.getAtributo("codTramite"));
        if(m_Log.isDebugEnabled()) m_Log.debug("OCU_TRA::"+gvo.getAtributo("ocurrenciaTramite"));
        if(m_Log.isDebugEnabled()) m_Log.debug("IDIOMA ::"+gvo.getAtributo("idioma"));
        if(m_Log.isDebugEnabled()) m_Log.debug("EJERCICIO ::"+gvo.getAtributo("ejercicio"));
           
       

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
        String numExpediente = (String)gvo.getAtributo("numeroExpediente");
        String ejercicio = (String) gvo.getAtributo("ejercicio");
        
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
            eeRaiz = UtilidadesXerador.construyeEstructuraEntidadesInformeDocumento(params,"",raiz,parametros);
            //eeRaiz = UtilidadesXerador.construyeEstructuraEntidadesInforme(params,"",raiz);
            eeRaiz.setValoresParametrosConsulta(parametros);
            UtilidadesXerador.construyeEstructuraDatos(params,eeRaiz,etiquetasFecha);
            TercerosManager tercerosManager = TercerosManager.getInstance();
            DatosSuplementariosTerceroManager datosSuplementariosManager = DatosSuplementariosTerceroManager.getInstance();

            Vector listas = getValoresDatosSuplementarios(gvo,params);
            Vector estructuraDatosSuplementarios = (Vector) listas.firstElement();
            Vector valoresDatosSuplementarios = (Vector) listas.lastElement();
            Vector listaEstructuraInteresados = getEstructuraInteresados(gvo,params);

            Collection col = eeRaiz.getListaInstancias();
            
             NodoEntidad n=new NodoEntidad();

            boolean etiquetasRegistro = false;
            for (Object aCol : col) {
                n = (NodoEntidad) aCol;                
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
                // Fecha actual en euskera
                //fecha = n.getCampo("FECACTEU");
                if (fecha != null) {
                    n.remove("FECACTEU");
                    n.addCampo("FECACTEU", DateOperations.traducirFechaEuskera(fecha));
                }
                
                String fechaEntradaRegistro = n.getCampo("FECENTREGISTRO");                
                if(fechaEntradaRegistro!=null && !"".equals(fechaEntradaRegistro) && formatoFechaAlternativo){                     
                    // Si hay fecha de entrada en registro y est\E1 activado el formato de fecha alternativo
                    n.addCampo("FECENTREGISTRO" + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA, DateOperations.formatoAlternativoCampoFecha(fechaEntradaRegistro,FORMATO_FECHA_ALTERNATIVO));                    
                }

                // Cargos 
                Vector listaCargos = getListaCargos(params, (String) gvo.getAtributo("codAplicacion"));
                for (int i = 0; i < listaCargos.size(); i++) {
                    GeneralValueObject g = (GeneralValueObject) listaCargos.elementAt(i);
                    String cargo = (String) g.getAtributo("cargo");
                    String nombre = (String) g.getAtributo("nombre");

                    String cargoTokenizado = eliminarCaracteresProhibidos(cargo);
                    n.remove(cargoTokenizado);
                    n.addCampo(cargoTokenizado, nombre);
                }
                
                // Interesados                
                Vector listaInteresados = (Vector) campos.get("INTERESADO");
                if (listaInteresados != null) {
                    
                    ArrayList<TercerosValueObject> terceros = new ArrayList<TercerosValueObject>();
                    terceros = tercerosManager.getInteresadosExpediente(codMunicipio, numExpediente, codProcedimiento, ejercicio, params);
                    
                    //Estructura datos suplementarios
                    Vector<EstructuraCampo> datosSuplementarios = 
                    datosSuplementariosManager.cargaEstructuraDatosSuplementariosTercero(codMunicipio, params);
                    
                    
                    for(int i = 0; i < listaInteresados.size(); i++){
                        NodoEntidad nInt = (NodoEntidad) listaInteresados.elementAt(i);
                        Map camposInteresado=new HashMap();
                           m_Log.debug("_________________________________________________________________nInt " +i+"-"+ nInt);
                        for(int j = 0; j < listaEstructuraInteresados.size(); j++){
                            EstructuraCampo eC = (EstructuraCampo) listaEstructuraInteresados.elementAt(j);


                            m_Log.debug("*******************************************************************nInt " +j+"-"+eC);
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
                            String mailInt = nInt.getCampo("MAILINTERESADO");
                            String codPostalInt = nInt.getCampo("CODPOSTALINTERESADO");

                            String descPrv = nInt.getCampo("PROVINTERESADO");                           

                            String descNombreInt = "Nombre" + descRolInterTokenizado;
                            String descApel1Int = "Apel1" + descRolInterTokenizado;
                            String descApel2Int = "Apel2" + descRolInterTokenizado;
                            String descDocInt = "Doc" + descRolInterTokenizado;
                            String descDomInt = "Dom" + descRolInterTokenizado;
                            //Para el rol del interesado
                            String descRol = "Rol" + descRolInterTokenizado;
                            //Para la poblacion del interesado
                            String descPobInt = "Pob" + descRolInterTokenizado;
                            String descProInt = "Provincia" + descRolInterTokenizado;
                            String descTlfnoInt = "Tlfno" + descRolInterTokenizado;
                            String descMail = "Mail"+descRolInterTokenizado;
                            String descCodPostalInt = "CodPostal" + descRolInterTokenizado;
                            String poblacionInteresado = nInt.getCampo("POBLACIONINTERESADO");
                            nInt.remove("COSPOSTALINTERESADO");
                            if(rolInteresado.equals(descRolInter)){
                                //Para el nombre del interesado
                                nInt.addCampo(descRolInterTokenizado, nombreCompletoInt);
                                nInt.addCampo(descNombreInt, nombreInt);
                                nInt.addCampo(descApel1Int, apel1Int);
                                nInt.addCampo(descApel2Int, apel2Int);
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
                                nInt.addCampo(descMail, mailInt);
                                nInt.addCampo(descTlfnoInt, tlfnoInt);
                                nInt.addCampo(descCodPostalInt, codPostalInt);
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
                                            
                                            datoSuplementario.getCodPlantilla();
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
                                                            
                                                            // nuevo ===>
                                                            if(dato==null) dato = "";
                                                            // nuevo <====
                                                            nInt.addCampo(nombreCampo+descRolInterTokenizado, dato);
                                                            
                                                            // nuevo ===>
                                                           if(dato!=null &&formatoFechaAlternativo && codPlantillaTercero!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(codPlantillaTercero)){
                                                                String nuevoCampoTokenizado = eliminarCaracteresProhibidos(nombreCampo + descRolInterTokenizado + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                                                nInt.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(dato,FORMATO_FECHA_ALTERNATIVO));
                                                            }
                                                            // nuevo <====
                                                            
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
                                nInt.addCampo(descDomInt, "");
                                nInt.addCampo(descRol, "");
                                nInt.addCampo(descPobInt, "");
                                nInt.addCampo(descTlfnoInt, "");
                                nInt.addCampo(descCodPostalInt,"");
                                nInt.addCampo(descProInt, "");
                                nInt.addCampo(descMail, "");
                                for(EstructuraCampo datoSuplementario : datosSuplementarios){
                                    String nombreCampo = datoSuplementario.getCodCampo();
                                    nInt.addCampo(nombreCampo+descRolInterTokenizado, "");
                                }//for(EstructuraCampo datoSuplementario : datosSuplementarios)
                            }//if(rolInteresado.equals(descRolInter))
                        }//for(int j = 0; j < listaEstructuraInteresados.size(); j++)
                        
                        camposInteresado=nInt.getCampos();
                        interesados.add(i, camposInteresado);
                        
                    }//for(int i = 0; i < listaInteresados.size(); i++)
                }/*else{
                     listaInteresados = rellenarListaInteresadosVacia(listaEstructuraInteresados);
                     campos.put(ConstantesDatos.NODO_INTERESADO,listaInteresados);
                }*/
                
                // Datos Suplementarios
                for (int i = 0; i < valoresDatosSuplementarios.size(); i++) {
                    CamposFormulario cF = (CamposFormulario) valoresDatosSuplementarios.elementAt(i);
                    EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
                    if(cF != null){
                        String valorCampo = cF.getString(eC.getCodCampo());
                        if (eC.getCodTipoDato().equals("6")) valorCampo = getDescripcionValorDesplegable(eC, valorCampo);
                        if (valorCampo != null && !"".equals(valorCampo)) {
                        if (eC.getCodTipoDato().equals("1") || eC.getCodTipoDato().equals("8")){
                            valorCampo = formatoValorNumerico(valorCampo);
                        }
                            String campoTokenizado = eliminarCaracteresProhibidos(eC.getCodCampo());
                            n.addCampo(campoTokenizado, valorCampo);


                            // Si el campo suplementario es de tipo fecha y est\E1 habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                            // otro nombre y con el mismo valor que la original s\F3lo que con el nuevo formato de fecha
                            if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                                String nuevoCampoTokenizado = eliminarCaracteresProhibidos(eC.getCodCampo() + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                n.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                            }

                        }
                        else{
                             valorCampo = "";
                            String campoTokenizado = eliminarCaracteresProhibidos(eC.getCodCampo());
                            n.addCampo(campoTokenizado, valorCampo);


                            // Si el campo suplementario es de tipo fecha y est\E1 habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                            // otro nombre y con el mismo valor que la original s\F3lo que con el nuevo formato de fecha
                            if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                                String nuevoCampoTokenizado = eliminarCaracteresProhibidos(eC.getCodCampo() + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                n.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                            }
                        }



                    String idCampoConPrefijo = "T" + gvo.getAtributo("codTramite") + eC.getCodCampo();
                    valorCampo = cF.getString(idCampoConPrefijo);
                    if (eC.getCodTipoDato().equals("6")) valorCampo = getDescripcionValorDesplegable(eC, valorCampo);                                                                                                
                    if (valorCampo != null && !"".equals(valorCampo)) {
                        if (eC.getCodTipoDato().equals("1") || eC.getCodTipoDato().equals("8")){
                            valorCampo = formatoValorNumerico(valorCampo);
                        }
                        String campoTokenizado = eliminarCaracteresProhibidos(idCampoConPrefijo);
                        n.addCampo(campoTokenizado, valorCampo);


                            // Si el campo suplementario es de tipo fecha y est\E1 habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                            // otro nombre y con el mismo valor que la original s\F3lo que con el nuevo formato de fecha
                            if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                                    String nuevoCampoTokenizado = eliminarCaracteresProhibidos(idCampoConPrefijo + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                n.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                            }

                        }else{
                            valorCampo = "";
                            String campoTokenizado = eliminarCaracteresProhibidos(idCampoConPrefijo);
                            n.addCampo(campoTokenizado, valorCampo);


                            // Si el campo suplementario es de tipo fecha y est\E1 habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                            // otro nombre y con el mismo valor que la original s\F3lo que con el nuevo formato de fecha
                            if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                                    String nuevoCampoTokenizado = eliminarCaracteresProhibidos(idCampoConPrefijo + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                n.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                            }
                        }
                    }
                }
          
            
            
            //Etiquetas que vienen de los m\F3dulos externos
            //Plantilla Interesado -> N
            //Plantilla Relaci\F3n -> N    
             ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasModulosExternos= getEtiquetasModulosExternos(codOrganizacion,codProcedimiento,"N", "N", params);
              for (int i = 0; i < etiquetasModulosExternos.size(); i++) {
                 EstructuraEtiquetaModuloIntegracionVO etiqueta  = (EstructuraEtiquetaModuloIntegracionVO) etiquetasModulosExternos.get(i);
                
                 //Ahora tenemos que recuperar el valor de la etiqueta, y anhadirla..
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es el numExpediente pasado: "+ numExpediente);
                 String valor=ModuloIntegracionExternoManager.getInstance().getValorDeEtiqueta(params, etiqueta, numExpediente,codTramite, ocuTramite);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es numExpediente: "+numExpediente);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es codTramite: "+codTramite);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es codTramite: "+ocuTramite);
                 n.addCampo(etiqueta.getNombreEtiqueta(),valor);
                
              }
              
              //n.addCampo("IMPORTE","11");
              if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es n"+n);
           }//Fin del While   
            /////////////////////
           
           resultado=n.getCampos();
           resultado.put("LISTAINTERESADOS", interesados);

        }catch(TechnicalException te){
            te.printStackTrace();
            m_Log.debug("**************** Exception capturada en: " + getClass().getName() + ": " + te.getMessage());
            if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName() + ": " + te.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            m_Log.debug("**************** Exception capturada en: " + getClass().getName() + ": " + e.getMessage());
            if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName() + ": " + e.getMessage());
        }
      
        return resultado;
    }
    
public HashMap consultaHashEtiquetasValor2(GeneralValueObject gvo,String[] params){
        HashMap resultado=new HashMap();
        List<Map> interesados = new ArrayList<Map>();
        String xml = "";
        String raiz = obtenEstructuraRaiz2((String)gvo.getAtributo("codAplicacion"),params);
        if(m_Log.isDebugEnabled()) m_Log.debug("--> RAIZ en obten estructura2 : " + raiz);
        EstructuraEntidades eeRaiz = null;
        Vector listaCodInteresados = listaCodInteresados = (Vector) gvo.getAtributo("listaCodInteresados");
        Vector listaVersInteresados = listaVersInteresados = (Vector) gvo.getAtributo("listaVersInteresados");
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

        String codOrganizacionM=(String)gvo.getAtributo("codMunicipio");
        String codTramite=(String)gvo.getAtributo("codTramite");
        String ocuTramite=(String)gvo.getAtributo("ocurrenciaTramite"); 
                
        if(m_Log.isDebugEnabled()) m_Log.debug("codOrganizacion:"+codOrganizacionM);
        if(m_Log.isDebugEnabled()) m_Log.debug("codTRAMITE:"+codTramite);
        if(m_Log.isDebugEnabled()) m_Log.debug("ocuTRAMITE:"+ocuTramite);
        
        String numExpediente=(String)gvo.getAtributo("numeroExpediente");
         if(m_Log.isDebugEnabled()) m_Log.debug("NumeroExpediente:"+numExpediente);
        try{
            eeRaiz = UtilidadesXerador.construyeEstructuraEntidadesInforme(params,"",raiz);
            for(int m=0;m<listaCodInteresados.size();m++) {
                  if(m_Log.isDebugEnabled()) m_Log.debug("ENTRANDO EN EL INTERESADO ::"+m);
                Map camposInteresado=new HashMap();
                Vector parametros = new Vector();
                parametros.add((String)gvo.getAtributo("codProcedimiento"));
                parametros.add((String)gvo.getAtributo("numeroExpediente"));
                parametros.add((String)gvo.getAtributo("codTramite"));
                parametros.add((String)gvo.getAtributo("ocurrenciaTramite"));
                parametros.add((String)gvo.getAtributo("idioma"));
                parametros.add((String) listaCodInteresados.elementAt(m));
                parametros.add((String) listaVersInteresados.elementAt(m));
                if(m_Log.isDebugEnabled()) m_Log.debug("COD_PROC::"+gvo.getAtributo("codProcedimiento"));
                if(m_Log.isDebugEnabled()) m_Log.debug("NUM_EXP::"+gvo.getAtributo("numeroExpediente"));
                if(m_Log.isDebugEnabled()) m_Log.debug("COD_TRA::"+gvo.getAtributo("codTramite"));
                if(m_Log.isDebugEnabled()) m_Log.debug("OCU_TRA::"+gvo.getAtributo("ocurrenciaTramite"));
                if(m_Log.isDebugEnabled()) m_Log.debug("IDIOMA ::"+gvo.getAtributo("idioma"));
                if(m_Log.isDebugEnabled()) m_Log.debug("CODINTERESADO :: " + listaCodInteresados.elementAt(m));
                if(m_Log.isDebugEnabled()) m_Log.debug("VERSINTERESADO :: " + listaVersInteresados.elementAt(m));

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
                while (iterInst.hasNext()) {
                     if(m_Log.isDebugEnabled()) m_Log.debug("ENTRANDO EN la iteraccion ");
                    NodoEntidad n = (NodoEntidad) iterInst.next();
 
                    String diaActual = n.getCampo("DIAACTUAL");
                    String mesActual = n.getCampo("MESACTUAL");
                    String anoActual = n.getCampo("ANOACTUAL");
               
                    String fecha = n.getCampo("FECACTGAL");
                    if (!"".equals(fecha) && fechaFormato(fecha,"dd/MM/yyyy")) {
                        n.remove("FECACTGAL");
                        n.addCampo("FECACTGAL",traducirFecha(fecha,"es","GL"));
                    }//if (!"".equals(fecha) && fechaFormato(fecha,"dd/MM/yyyy"))
                    fecha = n.getCampo("FECACTESP");
                    
                    if (!"".equals(fecha) && fechaFormato(fecha,"dd/MM/yyyy")) {
                        n.remove("FECACTESP");
                        n.addCampo("FECACTESP",traducirFecha(fecha,"es","ES"));
                    }//if (!"".equals(fecha) && fechaFormato(fecha,"dd/MM/yyyy")) 
                   
                    if (fecha != null && fechaFormato(fecha,"dd/MM/yyyy")) {
                        n.remove("FECACTEU");
                        n.addCampo("FECACTEU", DateOperations.traducirFechaEuskera(fecha));
                    }//if (fecha != null && fechaFormato(fecha,"dd/MM/yyyy")) 
              
                    if(m_Log.isDebugEnabled()) m_Log.debug("-->Cargamos lista de cargos:");
                    Vector listaCargos = new Vector();
                    listaCargos = getListaCargos(params,(String)gvo.getAtributo("codAplicacion"));
                    for(int i=0;i<listaCargos.size();i++) {
                        GeneralValueObject g = new GeneralValueObject();
                        g = (GeneralValueObject) listaCargos.elementAt(i);
                        String cargo = (String) g.getAtributo("cargo");
                        String nombre = (String) g.getAtributo("nombre");
                        //Esto es lo que he añadido
                        StringTokenizer valores = null;
                        String cargoTokenizado = eliminarCaracteresProhibidos(cargo);
                        n.remove(cargoTokenizado);
                        n.addCampo(cargoTokenizado,nombre);
                    }//for(int i=0;i<listaCargos.size();i++)
                    String domicilio = n.getCampo("DOMICILIOINTERESADO");
                    String poblacion = n.getCampo("POBLACIONINTERESADO");
                    String domicilioNoNormalizado = n.getCampo("DOMICILIONONORMALIZADO");
                    String poblacionNoNormalizado = n.getCampo("POBLACIONNONORMALIZADO");
                    String normalizado = n.getCampo("NORMALIZADO");
                    if(normalizado != null && normalizado.equals("2")){
                        n.remove("DOMICILIOINTERESADO");
                        n.addCampo("DOMICILIOINTERESADO",domicilioNoNormalizado);
                        n.remove("POBLACIONINTERESADO");
                        n.addCampo("POBLACIONINTERESADO", poblacionNoNormalizado);
                    }//if(normalizado != null && normalizado.equals("2")) 
                    
                    //Datos de terceros
                    TercerosManager tercerosManager = TercerosManager.getInstance();
                    DatosSuplementariosTerceroManager datosSuplementariosManager = DatosSuplementariosTerceroManager.getInstance();
                    TercerosValueObject tercero = tercerosManager.getDatosTercero((String)listaCodInteresados.get(m), params);
                    String codTercero = tercero.getIdentificador();
                    String codOrganizacion = (String) gvo.getAtributo("codMunicipio");
                    
                    if(!iterInst.hasNext()){
                        //Estructura datos suplementarios
                        Vector<EstructuraCampo> datosSuplementarios = 
                                datosSuplementariosManager.cargaEstructuraDatosSuplementariosTercero(codOrganizacion, params);

                        //CamposFormulario
                        Vector camposFormulario = 
                            datosSuplementariosManager.cargaValoresDatosSuplementariosConCodigo(codOrganizacion, codTercero, datosSuplementarios, params);

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
                                            valor = datosSuplementariosManager.getValorDesplegable(codOrganizacion, nombreCampo, (String) entry.getValue(), params);
                                        }else{
                                            valor = (String) entry.getValue();
                                        }//if(tipoDato.equalsIgnoreCase("6"))
                                        
                                        if(valor==null) valor="";
                                        n.addCampo(nombreCampo,valor);
                                        
                                        /** prueba ***/
                                        if(formatoFechaAlternativo && codPlantillaTercero!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(codPlantillaTercero)){
                                            String nuevoCampoTokenizado = eliminarCaracteresProhibidos(nombreCampo + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                            n.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valor,FORMATO_FECHA_ALTERNATIVO));
                                        }
                                        /** prueba ***/
                                        
                                    }//if(nombreCampo.equalsIgnoreCase(key))
                                }//while(itCampos.hasNext())
                            }//for(int z=0; z<camposFormulario.size(); z++)
                        }//for(EstructuraCampos datoSuplementario : datosSuplementarios)
                    }//if(!iterInst.hasNext())

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
                        else{
                             String campoTokenizado = "";
                            n.addCampo(campoTokenizado, valorCampo);
                            // Si el campo suplementario es de tipo fecha y está habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                            // otro nombre y con el mismo valor que la original sólo que con el nuevo formato de fecha
                            if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                                String nuevoCampoTokenizado = eliminarCaracteresProhibidos(campoTokenizado + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                n.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                            }
                        }

                        String idCampoConPrefijo = "T" + gvo.getAtributo("codTramite") + eC.getCodCampo();
                        valorCampo = cF.getString(idCampoConPrefijo);
                        if (eC.getCodTipoDato().equals("6")) valorCampo = getDescripcionValorDesplegable(eC, valorCampo);
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
                        else{
                            String campoTokenizado = "";
                            n.addCampo(campoTokenizado, valorCampo);
                            // Si el campo suplementario es de tipo fecha y está habilitado el formato alternativo de fecha, se duplica la etiqueta pero con
                            // otro nombre y con el mismo valor que la original sólo que con el nuevo formato de fecha
                            if(formatoFechaAlternativo && eC.getCodPlantilla()!=null && ConstantesDatos.TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA.equals(eC.getCodPlantilla())){
                                String nuevoCampoTokenizado = eliminarCaracteresProhibidos(campoTokenizado + ConstantesDatos.SUFIJO_ETIQUETA_FEC_ALTERNATIVA);
                                n.addCampo(nuevoCampoTokenizado, DateOperations.formatoAlternativoCampoFecha(valorCampo,FORMATO_FECHA_ALTERNATIVO));
                            }
                        }
                    }//for (int i = 0; i < valoresDatosSuplementarios.size(); i++) 
                
                    
            //Etiquetas que vienen de los módulos externos
             //Plantilla Interesado-> Si
             //Plantilla Relación-> NO       
             ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasModulosExternos= getEtiquetasModulosExternos(codOrganizacion,codProcedimiento,"S", "N", params);
              for (int i = 0; i < etiquetasModulosExternos.size(); i++) {
                 EstructuraEtiquetaModuloIntegracionVO etiqueta  = (EstructuraEtiquetaModuloIntegracionVO) etiquetasModulosExternos.get(i);
                
                 //Ahora tenemos que recuperar el valor de la etiqueta, y anhadirla..
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es el numExpediente pasado: "+ numExpediente);
                 String valor=ModuloIntegracionExternoManager.getInstance().getValorDeEtiqueta(params, etiqueta, numExpediente,codTramite, ocuTramite);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es numExpediente: "+numExpediente);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es codTramite: "+codTramite);
                 if(m_Log.isDebugEnabled()) m_Log.debug("Veamos quien es codTramite: "+ocuTramite);
                 n.addCampo(etiqueta.getNombreEtiqueta(),valor);
                
              }
               camposInteresado=n.getCampos();
              
             }//while (iterInst.hasNext()) 
              interesados.add(m, camposInteresado);   
                        
            }// for(int m=0;m<listaCodInteresados.size();m++) 
            resultado.put("LISTAINTERESADOS", interesados);
         }catch(Exception e){ 
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("XML ::"+xml);
        return resultado;
    }//consultaXML2
    

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

    public Vector getValoresDatosSuplementarios(GeneralValueObject gvo, String[] params) {

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

        // Campos del tramite
        Vector estructuraDatosSuplementarios = TramitacionExpedientesManager.getInstance().cargaEstructuraDatosSuplementarios(t, params);
        Vector valoresDatosSuplementarios = TramitacionExpedientesManager.getInstance().cargaValoresDatosSuplEtiquetas(t, estructuraDatosSuplementarios, params);
        // Campos del expediente
        Vector estructuraDatosSuplementarios1 = FichaExpedienteManager.getInstance().cargaEstructuraDatosSuplementarios(g, params);
        Vector valoresDatosSuplementarios1 = FichaExpedienteManager.getInstance().cargaValoresDatosSuplEtiquetas(g, estructuraDatosSuplementarios1, params);

        for (int i = 0; i < valoresDatosSuplementarios1.size(); i++) {
            valoresDatosSuplementarios.addElement(valoresDatosSuplementarios1.elementAt(i));
            estructuraDatosSuplementarios.addElement(estructuraDatosSuplementarios1.elementAt(i));
        }
        if (m_Log.isDebugEnabled())
            m_Log.debug("el tamaño de la lista de valores de datos suplementarios es : " + valoresDatosSuplementarios.size());
        Vector listas = new Vector();
        listas.addElement(estructuraDatosSuplementarios);
        listas.addElement(valoresDatosSuplementarios);
        return listas;
    }

      //Recuperamos las etiquetas de los módulos externos, se mira para todos los módulo
      public ArrayList<EstructuraEtiquetaModuloIntegracionVO> getEtiquetasModulosExternos(String codOrganizacion, String codigoProcedimiento, String interesado, String relacion, String[] params) {
          
      if (m_Log.isDebugEnabled()) m_Log.debug("getEtiquetasModulosExternos.BEGIN");
          ArrayList<ModuloIntegracionExterno> modulos= new ArrayList<ModuloIntegracionExterno>();
          ArrayList<EstructuraEtiquetaModuloIntegracionVO> etiquetasModulo=new ArrayList<EstructuraEtiquetaModuloIntegracionVO>();
          int codOrganizacionInt=Integer.parseInt(codOrganizacion);
          modulos=
                  ModuloIntegracionExternoFactoria.getInstance().getImplClassModuloConEtiquetas(codOrganizacionInt,
                  codigoProcedimiento, params,interesado,
                  relacion);
          //Imprimimos para debug los modulos..
            for (int i = 0; i < modulos.size(); i++) {
                ModuloIntegracionExterno modulo=modulos.get(i); 
                 etiquetasModulo.addAll(modulo.getEtiquetas());
               if (m_Log.isDebugEnabled()) m_Log.debug("Nombre Modulo:"+modulo.getNombreModulo());  
               if (m_Log.isDebugEnabled()) m_Log.debug("Descripcion Modulo:"+modulo.getDescripcionModulo());

            }
          
          if (m_Log.isDebugEnabled()) m_Log.debug("Lista de las etiquetas:"+etiquetasModulo.toString());
        return etiquetasModulo;

         
}   
    

 private boolean fechaFormato(String fecha, String formato){
         boolean exito = false;
        try{
            SimpleDateFormat s = new SimpleDateFormat(formato);
            java.util.Date d = s.parse(fecha);
            Calendar calendario = Calendar.getInstance();
            calendario.setTime(d);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            int mes = calendario.get(Calendar.MONTH);
            int ano = calendario.get(Calendar.YEAR);
            exito = true;
        }catch(Exception e){

            e.printStackTrace();
        }
        return exito;
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
            }
        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
        }finally {
            try{
                if (rs!=null) rs.close();
                if (st!=null) st.close();
                abd.devolverConexion(con);
            }catch(Exception bde) {
                bde.getMessage();
                if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
            }
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

    public byte[] loadDocumento(GeneralValueObject gvo,String[] params)
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
            if ("true".equals((String)gvo.getAtributo("expHistorico")))
                sql.append(" select CRD_FIL ")
                    .append(" FROM HIST_E_CRD ")
                    .append(" WHERE CRD_NUM=? AND CRD_TRA=? AND CRD_OCU=? AND CRD_NUD=?");
            else
                sql.append(" select CRD_FIL ")
                    .append(" FROM E_CRD ")
                    .append(" WHERE CRD_NUM=? AND CRD_TRA=? AND CRD_OCU=? AND CRD_NUD=?");


            stmt = conexion.prepareStatement(sql.toString());
            if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
            stmt.setString(1,abd.js_unescape((String)gvo.getAtributo("numeroExpediente")));
            stmt.setInt(2,new Integer((String)gvo.getAtributo("codTramite")));
            stmt.setInt(3,new Integer((String)gvo.getAtributo("ocurrenciaTramite")));
            stmt.setInt(4, new Integer((String) gvo.getAtributo("numeroDocumento")));
            rs = stmt.executeQuery();

            while(rs.next()) {
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
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            }catch(Exception bde){
                bde.getMessage();
                if(m_Log.isErrorEnabled()) m_Log.error("No se pudo devolver la conexion: " + bde.toString());
            }
        }
        return r;
    }

    private String obtenEstructuraRaiz(String codAplicacion,String[] params){
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
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            }catch(Exception bde){
                bde.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("No se pudo devolver la conexion: " + bde.toString());
            }
        }
        return raiz;
    }

    private String obtenEstructuraRaiz2(String codAplicacion,String[] params){
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
                    .append(codAplicacion).append(" AND DOC_CEI=5");  //LO CAMBIE : 5 POR 1

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
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            }catch(Exception bde){
                bde.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
            }
        }
        return raiz;
    }

    private String obtenMaximo(AdaptadorSQLBD oad, GeneralValueObject gvo,String[] params){
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
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
                abd.devolverConexion(conexion);
            }catch(Exception bde){
                bde.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
            }
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("MAXIMO:::" + maximo);
        return maximo;
    }

    private String obtenMaximo(String codProc, int ejerc, String numExp, int codTra, int ocuTra, AdaptadorSQLBD oad, Connection conexion){
        String maximo = "";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try{
            // Creamos la select con los parametros adecuados.
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
            stmt.setString(1,codProc);
            stmt.setInt(2,ejerc);
            stmt.setString(3,numExp);
            stmt.setInt(4,codTra);
            stmt.setInt(5,ocuTra);

            rs = stmt.executeQuery();
            while(rs.next()){
                maximo = rs.getString(1);
            }
        }catch (Exception sqle){
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Error de SQL en obtenMaximo: " + sqle.toString());
        }
        finally{
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();
            }catch(Exception bde){
                bde.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
            }
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("MAXIMO:::" + maximo);
        return maximo;
    }



    /**
     * Actualiza la fecha del informe de un documento de un determinado trámite
     * @param gvo: GeneralValueObject con los datos del documento a actualizar
     * @param con: Conexión a la BD
     * @return 
     */
    public void actualizarFechaInforme(GeneralValueObject gvo,Connection con) throws SQLException{
        int resultado=0;
        String codMunicipio     = (String)gvo.getAtributo("codMunicipio");        
        String codProcedimiento = (String)gvo.getAtributo("codProcedimiento");
        String ejercicio        = (String)gvo.getAtributo("ejercicio");
        String numeroExpediente = (String)gvo.getAtributo("numeroExpediente");
        String codTramite       = (String)gvo.getAtributo("codTramite");
        String ocurrenciaTramite= (String)gvo.getAtributo("ocurrenciaTramite");
        String codDocumento     = (String)gvo.getAtributo("codDocumento");
        String fechaInforme    = (String)gvo.getAtributo("fechaInforme");

        AdaptadorSQLBD abd = null;        
        String sql ="";
        PreparedStatement ps = null;
        
        try{

              sql = "UPDATE E_CRD "+
                        "SET CRD_FINF=? " +
                        "WHERE CRD_MUN= " + codMunicipio +  " " +
                        "AND CRD_PRO ='" + codProcedimiento + "' " +
                        "AND CRD_EJE = "+ ejercicio + " " +
                        "AND CRD_NUM ='" + numeroExpediente + "' " +
                        "AND CRD_TRA ="+ codTramite + " " +
                        "AND CRD_OCU =" + ocurrenciaTramite + " " +
                        "AND CRD_NUD = " + codDocumento;

              m_Log.debug(this.getClass().getName() + ".actualizarFechaInforme sql: " + sql);
              int i=1;
              ps = con.prepareStatement(sql);
              ps.setTimestamp(i++,DateOperations.toTimestamp(fechaInforme));
              /*
              ps.setInt(i++,Integer.parseInt(codMunicipio));
              ps.setString(i++,codProcedimiento);
              ps.setInt(i++,Integer.parseInt(ejercicio));
              ps.setString(i++,numeroExpediente);
              ps.setInt(i++,Integer.parseInt(codTramite));
              ps.setInt(i++,Integer.parseInt(ocurrenciaTramite));
              ps.setInt(i++,Integer.parseInt(codDocumento)); */

              int rowsUpdated = ps.executeUpdate();
              m_Log.debug(" rowsUpdated: " + rowsUpdated);
        }
        catch (SQLException e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("SQLException haciendo rollBackTransaction en: " + getClass().getName());
            throw e;
        }finally{
            if(ps!=null) ps.close();            
        }
        
    }

/**
     * Actualiza el id del metadato del documento de un determinado tramite
     * 
     * @param idMetadato
     * @param codDocumento
     * @param ejercicio
     * @param municipio
     * @param numeroExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param procedimiento
     * @param con
     * @param con
     * @throws SQLException 
     */
    public void actualizarIdMetadato(Long idMetadato, Integer codDocumento, Integer ejercicio,
            Integer municipio, String numeroExpediente, Integer codTramite,
            Integer ocurrenciaTramite, String procedimiento, Connection con)
            throws SQLException {

        StringBuilder sql = new StringBuilder();
        PreparedStatement ps = null;

        m_Log.debug("actualizarIdMetadato");
        
        try {
            sql.append(" UPDATE E_CRD ")
               .append(" SET CRD_ID_METADATO = ? ")             
               .append(" WHERE CRD_PRO = ? ")
               .append(" AND CRD_MUN = ? ")
               .append(" AND CRD_EJE = ? ")
               .append(" AND CRD_NUM = ? ")
               .append(" AND CRD_TRA = ? ")
               .append(" AND CRD_OCU = ? ")
               .append(" AND CRD_NUD = ? ");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(String.format("SQL: %s", sql.toString()));
                m_Log.debug("Params:");
                m_Log.debug(String.format("CRD_ID_METADATO = %d", idMetadato));
                m_Log.debug(String.format("CRD_PRO = %s", procedimiento));
                m_Log.debug(String.format("CRD_MUN = %d", municipio));
                m_Log.debug(String.format("CRD_EJE = %d", ejercicio));
                m_Log.debug(String.format("CRD_NUM = %s", numeroExpediente));
                m_Log.debug(String.format("CRD_TRA = %d", codTramite));
                m_Log.debug(String.format("CRD_OCU = %d", ocurrenciaTramite));
                m_Log.debug(String.format("CRD_NUD = %d", codDocumento));
            }
            
            ps = con.prepareStatement(sql.toString());

            JdbcOperations.setValues(ps, 1,
                    idMetadato,
                    procedimiento,
                    municipio,
                    ejercicio,
                    numeroExpediente,
                    codTramite,
                    ocurrenciaTramite,
                    codDocumento);
            
            int rowsUpdated = ps.executeUpdate();
            m_Log.debug(" rowsUpdated: " + rowsUpdated);
        } catch (SQLException e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error("SQLException haciendo rollBackTransaction en: " + getClass().getName());
            }
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }

    }

   /**
     * Graba un documento en base de datos pero en el contenido no se graba el contenido del documento, puesto que éste se almacena en un gestor documental
     * @param gVO: Objeto que contiene los datos del objeto a dar de alta
     * @param params: Parámetros de conexión a base de datos
     * @return Número de documento asignado al documento
     */
    public int grabarDocumentoGestor(GeneralValueObject gvo,String[] params){
        int resultado=0;

        String codMunicipio     = (String)gvo.getAtributo("codMunicipio");        
        String codProcedimiento = (String)gvo.getAtributo("codProcedimiento");
        String ejercicio        = (String)gvo.getAtributo("ejercicio");
        String numeroExpediente = (String)gvo.getAtributo("numeroExpediente");
        String codTramite       = (String)gvo.getAtributo("codTramite");
        String ocurrenciaTramite= (String)gvo.getAtributo("ocurrenciaTramite");
        String codDocumento     = (String)gvo.getAtributo("codDocumento");
        String codUsuario       = (String)gvo.getAtributo("codUsuario");
        String nombreDocumento  = (String)gvo.getAtributo("nombreDocumento");
        String numeroDocumento  = (String)gvo.getAtributo("numeroDocumento");
        //byte[] ficheroWord      = (byte[])gvo.getAtributo("ficheroWord");
        String contenidoFichero = "DOCGESTOR";
        byte[] ficheroWord = contenidoFichero.getBytes();

        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            String sql = "";
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
                resultSet.close();
                statement.close();
                if (tmp!=null) {
                    if (tmp.equalsIgnoreCase("O")) {
                        codEstadoFirma = "E";
                    } else {
                        codEstadoFirma = tmp.toUpperCase();
                    }//if
                }//if
            }//if
            /* ******************************************************** */

            if(numeroDocumento!=null && !numeroDocumento.equals("")){
                sql = "UPDATE E_CRD "+
                        "SET CRD_FMO = " + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ", CRD_USM = ?, CRD_DES= ?, CRD_FIL = ? "+
                        "WHERE CRD_PRO = ? AND"+
                        "      CRD_EJE = ? AND"+
                        "      CRD_NUM = ? AND"+
                        "      CRD_TRA = ? AND"+
                        "      CRD_OCU = ? AND"+
                        "      CRD_NUD = ? ";
            } else {
              
                sql = "INSERT INTO E_CRD(CRD_MUN,CRD_PRO,CRD_EJE,CRD_NUM,CRD_TRA,CRD_OCU,CRD_NUD,CRD_FAL,CRD_FMO,CRD_FINF," +
                        "CRD_USC,CRD_USM,CRD_FIL,CRD_DES,CRD_DOT,CRD_FIR_EST,CRD_EXP_FD,CRD_DOC_FD,CRD_FIR_FD) VALUES "+
                        "(?,?,?,?,?,?,?,"+ abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," +
                        abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null)+  ",?,?,?,?,?,?, NULL, NULL, NULL)";
            }//if

            PreparedStatement stmt = conexion.prepareStatement(sql);
            if(numeroDocumento!=null && !numeroDocumento.equals("")){
                stmt.setString(1,codUsuario);
                stmt.setString(2,nombreDocumento);
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                stmt.setBinaryStream(3,st,ficheroWord.length);
                stmt.setString(4,codProcedimiento);
                stmt.setString(5,ejercicio);
                stmt.setString(6,numeroExpediente);
                stmt.setString(7,codTramite);
                stmt.setString(8,ocurrenciaTramite);
                stmt.setString(9,numeroDocumento);
            }else{

                String sNumeroDocumento = this.obtenMaximo(abd, gvo,params);
                resultado = Integer.parseInt(sNumeroDocumento);

                stmt.setString(1,codMunicipio);
                stmt.setString(2,codProcedimiento);
                stmt.setString(3,ejercicio);
                stmt.setString(4,numeroExpediente);
                stmt.setString(5,codTramite);
                stmt.setString(6,ocurrenciaTramite);
                //stmt.setString(7,this.obtenMaximo(abd, gvo,params));
                stmt.setString(7,sNumeroDocumento);
                stmt.setString(8,codUsuario);
                stmt.setString(9,codUsuario);
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                stmt.setBinaryStream(10,st,ficheroWord.length);
                stmt.setString(11,nombreDocumento);
                stmt.setString(12,codDocumento);
                stmt.setString(13,codEstadoFirma);
            }

            if(numeroDocumento!=null && !numeroDocumento.equals("")){
                resultado = Integer.parseInt(numeroDocumento);
            }

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate();
            stmt.close();
            commitTransaction(abd,conexion);

        }catch (Exception sqle){
            rollBackTransaction(abd,conexion,sqle);
            resultado=0;
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("SQLException haciendo rollBackTransaction en: " + getClass().getName());
        }
        return resultado;
    }



    public String getNombreDocumentoGestor(GeneralValueObject gVO, Connection con){
        // Comienzo del metodo.
        m_Log.info("DocumentosExpedienteManager.getNombreDocumentoGestor");
        String resultado="";
        Statement st = null;
        ResultSet rs = null;

        try{

            String codMunicipio = (String)gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String)gVO.getAtributo("codProcedimiento");
            String ejercicio = (String)gVO.getAtributo("ejercicio");
            String codTramite = (String)gVO.getAtributo("codTramite");
            String ocurrenciaTramite = (String)gVO.getAtributo("ocurrenciaTramite");
            String numeroExpediente = (String)gVO.getAtributo("numeroExpediente");
            String numeroDocumento = (String)gVO.getAtributo("numeroDocumento");

            String sql;
            
            if ("true".equals((String)gVO.getAtributo("expHistorico")))
                sql = "SELECT CRD_DES FROM HIST_E_CRD WHERE ";
            else
                sql = "SELECT CRD_DES FROM E_CRD WHERE ";
                
                sql = sql + " CRD_MUN= " + codMunicipio + " AND CRD_PRO='" + codProcedimiento + "' AND CRD_EJE=" + ejercicio +
                             " AND CRD_TRA=" + codTramite + " AND CRD_OCU=" + ocurrenciaTramite + " AND CRD_NUM='" + numeroExpediente + "' AND " +
                             " CRD_NUD=" + numeroDocumento;

           st = con.createStatement();
           rs = st.executeQuery(sql);
           while(rs.next()){
               resultado = rs.getString("CRD_DES");
           }
            
        }catch(SQLException e){
            e.printStackTrace();
            m_Log.error(e.getMessage());
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return resultado;
    }



     /**
     * Graba un documento en base de datos junto con el contenido y devuelve el número de documento asignado
     * @param gVO: Objeto que contiene los datos del objeto a dar de alta
     * @param params: Parámetros de conexión a base de datos
     * @return Número de documento asignado al documento
     */
    public int grabarDocumentoGestorContenido(GeneralValueObject gvo,String[] params){
        int resultado=0;

        String codMunicipio     = (String)gvo.getAtributo("codMunicipio");
        String codProcedimiento = (String)gvo.getAtributo("codProcedimiento");
        String ejercicio        = (String)gvo.getAtributo("ejercicio");
        String numeroExpediente = (String)gvo.getAtributo("numeroExpediente");
        String codTramite       = (String)gvo.getAtributo("codTramite");
        String ocurrenciaTramite= (String)gvo.getAtributo("ocurrenciaTramite");
        String codDocumento     = (String)gvo.getAtributo("codDocumento");
        String codUsuario       = (String)gvo.getAtributo("codUsuario");
        String nombreDocumento  = (String)gvo.getAtributo("nombreDocumento");
        String numeroDocumento  = (String)gvo.getAtributo("numeroDocumento");
        byte[] ficheroWord      = (byte[])gvo.getAtributo("ficheroWord");


        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);
            String sql = "";
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
                resultSet.close();
                statement.close();
                if (tmp!=null) {
                    if (tmp.equalsIgnoreCase("O")) {
                        codEstadoFirma = "E";
                    } else {
                        codEstadoFirma = tmp.toUpperCase();
                    }//if
                }//if
            }//if
            /* ******************************************************** */

            if(numeroDocumento!=null && !numeroDocumento.equals("")){
                sql = "UPDATE E_CRD "+
                        "SET CRD_FMO = " + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ", CRD_USM = ?, CRD_DES= ?, CRD_FIL = ? "+
                        "WHERE CRD_PRO = ? AND"+
                        "      CRD_EJE = ? AND"+
                        "      CRD_NUM = ? AND"+
                        "      CRD_TRA = ? AND"+
                        "      CRD_OCU = ? AND"+
                        "      CRD_NUD = ? ";
            } else {

                sql = "INSERT INTO E_CRD(CRD_MUN,CRD_PRO,CRD_EJE,CRD_NUM,CRD_TRA,CRD_OCU,CRD_NUD,CRD_FAL,CRD_FMO,CRD_FINF," +
                        "CRD_USC,CRD_USM,CRD_FIL,CRD_DES,CRD_DOT,CRD_FIR_EST,CRD_EXP_FD,CRD_DOC_FD,CRD_FIR_FD) VALUES "+
                        "(?,?,?,?,?,?,?,"+ abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," +
                        abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null)+  ",?,?,?,?,?,?, NULL, NULL, NULL)";
            }//if

            PreparedStatement stmt = conexion.prepareStatement(sql);
            if(numeroDocumento!=null && !numeroDocumento.equals("")){
                stmt.setString(1,codUsuario);
                stmt.setString(2,nombreDocumento);
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                stmt.setBinaryStream(3,st,ficheroWord.length);
                stmt.setString(4,codProcedimiento);
                stmt.setString(5,ejercicio);
                stmt.setString(6,numeroExpediente);
                stmt.setString(7,codTramite);
                stmt.setString(8,ocurrenciaTramite);
                stmt.setString(9,numeroDocumento);
            }else{

                String sNumeroDocumento = this.obtenMaximo(abd, gvo,params);
                resultado = Integer.parseInt(sNumeroDocumento);

                stmt.setString(1,codMunicipio);
                stmt.setString(2,codProcedimiento);
                stmt.setString(3,ejercicio);
                stmt.setString(4,numeroExpediente);
                stmt.setString(5,codTramite);
                stmt.setString(6,ocurrenciaTramite);
                //stmt.setString(7,this.obtenMaximo(abd, gvo,params));
                stmt.setString(7,sNumeroDocumento);
                stmt.setString(8,codUsuario);
                stmt.setString(9,codUsuario);
                java.io.InputStream st = new java.io.ByteArrayInputStream(ficheroWord);
                stmt.setBinaryStream(10,st,ficheroWord.length);
                stmt.setString(11,nombreDocumento);
                stmt.setString(12,codDocumento);
                stmt.setString(13,codEstadoFirma);
            }

            if(numeroDocumento!=null && !numeroDocumento.equals("")){
                resultado = Integer.parseInt(numeroDocumento);
            }

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt.executeUpdate();
            stmt.close();
            commitTransaction(abd,conexion);

        }catch (Exception sqle){
            rollBackTransaction(abd,conexion,sqle);
            resultado=0;
            sqle.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("SQLException haciendo rollBackTransaction en: " + getClass().getName());
        }
        return resultado;
    }

    
    
    /**
     * Recupera el contenido binario de un documento de tramitación de la BBDD
     * @param gvo: Objeto instancia de la clase GeneralValueObject con la información 
     * necesaria para recuperar el documento
     * @param con: Conexión a la BBDD
     * @return byte[] con el contenido binario del documento
     */
    public byte[] loadDocumento(GeneralValueObject gvo,Connection con)
    {
        byte[] r = null;        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        
            // Creamos la select con los parametros adecuados.
            StringBuffer sql = new StringBuffer();
            sql.append(" select CRD_FIL ")
                    .append(" FROM E_CRD ")
                    .append(" WHERE CRD_NUM=? AND CRD_TRA=? AND CRD_OCU=? AND CRD_NUD=?");

            stmt = con.prepareStatement(sql.toString());
            if(m_Log.isDebugEnabled()) m_Log.debug(sql.toString());
            stmt.setString(1,(String)gvo.getAtributo("numeroExpediente"));
            stmt.setInt(2,new Integer((String)gvo.getAtributo("codTramite")));
            stmt.setInt(3,new Integer((String)gvo.getAtributo("ocurrenciaTramite")));
            stmt.setInt(4, new Integer((String) gvo.getAtributo("numeroDocumento")));
            rs = stmt.executeQuery();

            while(rs.next()) {
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
            try{
                if (rs!=null) rs.close();
                if (stmt!=null) stmt.close();                
            }catch(Exception bde){
                bde.getMessage();
                if(m_Log.isErrorEnabled()) m_Log.error("No se pudo devolver la conexion: " + bde.toString());
            }
        }
        return r;
    }
    
     public String formatoValorNumerico(String valor){
        DecimalFormatSymbols separador = new DecimalFormatSymbols();
        DecimalFormat formateador = new DecimalFormat("###,###.##",separador);
        Double resultado = null;
        
        try{
            resultado = new Double(valor.replace(',', '.')); 
        } catch (NumberFormatException nfe){
            return valor;
        }
        return formateador.format (resultado);
    }

     public void getDocumentoTramitacionConFirma(DocumentoFirma doc, Connection con) throws SQLException{
         m_Log.info("DocumentosExpedienteDAO.getDocumentoTramitacionConFirma");
         PreparedStatement ps = null;
         ResultSet rs = null;
         String query;
         
         try {
             query = "SELECT CRD_FAL,CRD_FMO,CRD_USC,USUC.USU_NOM AS UC,CRD_USM,USUM.USU_NOM AS UM,CRD_FIL,CRD_DES,CRD_DOT,CRD_FIR_EST,"
                     + " CRD_EXP_FD,CRD_DOC_FD,CRD_FIR_FD,CRD_FINF,CRD_ID_METADATO,PLT_INT,PLT_REL,PLT_EDITOR_TEXTO"
                     + " FROM E_CRD "
                     + " JOIN E_DOT ON E_DOT.DOT_MUN=E_CRD.CRD_MUN AND E_DOT.DOT_PRO=E_CRD.CRD_PRO AND E_DOT.DOT_TRA=E_CRD.CRD_TRA AND E_DOT.DOT_COD=E_CRD.CRD_DOT "
                     + " JOIN A_PLT ON A_PLT.PLT_COD=E_DOT.DOT_PLT"
                     + " JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_USU USUC ON USUC.USU_COD=E_CRD.CRD_USC"
                     + " JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_USU USUM ON USUM.USU_COD=E_CRD.CRD_USM"
                     + " WHERE CRD_PRO = ? AND CRD_EJE = ? AND CRD_NUM = ? AND CRD_TRA = ? AND CRD_OCU = ? AND CRD_NUD = ? AND CRD_MUN = ?";
             if(m_Log.isDebugEnabled()) m_Log.debug("Query = " + query);
             
             ps = con.prepareStatement(query);
             int contbd = 1;
             ps.setString(contbd++, doc.getCodProcedimiento());
             ps.setInt(contbd++, doc.getEjercicio());
             ps.setString(contbd++, doc.getNumExpediente());
             ps.setInt(contbd++, doc.getCodTramite());
             ps.setInt(contbd++, doc.getOcurrenciaTramite());
             ps.setInt(contbd++, doc.getCodDocumento());
             ps.setInt(contbd++, doc.getCodMunicipio());
             
             rs = ps.executeQuery();
             while(rs.next()) {
                 /*
                 Como los tipos LONG y LONG RAW están básicamente obsoletos, el leer esos valores correctamente requiere mucho cuidado. 
                 El problema es que, aunque no sea aparente a primera vista, cuando una consulta incluye columnas de ese tipo, Oracle abre un stream o flujo especial para esos tipos. 
                 Pero para poder leerlos sin problemas, debes asegurarte de 2 cosas:
                 -  Que leas las columnas LONG/LONG RAW en tu bucle antes de leer las demás columnas.
                 -  Que leas las columnas LONG/LONG RAW una sola vez en el bucle.
                 */
                 byte[] contenidoFichero = rs.getBytes("CRD_FIL");
                 if(contenidoFichero != null) {
                     doc.setFichero(contenidoFichero);
                 }
                 
                 java.sql.Date fecAltaSql = rs.getDate("CRD_FAL");
                 java.sql.Date fecModSql = rs.getDate("CRD_FMO");
                 java.sql.Date fecInfSql = rs.getDate("CRD_FINF");
                 long idMetadatos = rs.getLong("CRD_ID_METADATO");
                 int firFd = rs.getInt("CRD_FIR_FD");
                 if(fecAltaSql != null)
                    doc.setFechaAltaDocumento(new Date(fecAltaSql.getTime()));
                 else
                     doc.setFechaAltaDocumento(null);
                 if(fecModSql != null)
                    doc.setFechaModDocumento(new Date(fecModSql.getTime()));
                 else
                     doc.setFechaModDocumento(null);
                 doc.setCodUsuarioAltaDoc(rs.getInt("CRD_USC"));
                 doc.setCodUsuarioModDoc(rs.getInt("CRD_USM"));
                 doc.setNombreDocumento(rs.getString("CRD_DES"));
                 doc.setNumeroDocumento(rs.getInt("CRD_DOT"));
                 doc.setEstadoFirma(rs.getString("CRD_FIR_EST"));
                 doc.setExpFd(rs.getString("CRD_EXP_FD"));
                 doc.setDocFd(rs.getString("CRD_DOC_FD"));
                 if(rs.wasNull())
                     doc.setFirFd(-1);
                 else
                     doc.setFirFd(firFd);
                 if(fecInfSql != null)
                    doc.setFechaInforme(new Date(fecInfSql.getTime()));
                 else
                     doc.setFechaInforme(null);
                 if(rs.wasNull())
                     doc.setIdMetadatos(-1L);
                 else
                     doc.setIdMetadatos(idMetadatos);
             }
             
         } catch(SQLException sqle) {
             m_Log.error("Ha ocurrido un error al recuperar los datos del documento");
             throw sqle;
         } finally {
             try {
                 if(rs!=null) rs.close();
                 if(ps!=null) ps.close();
             } catch (SQLException ex){
                 m_Log.error("Ha ocurrido un error al liberar recursos de BBDD.");
             }
         }
     }
     
     public void revertEstadoDuplicadoDocumentoTramitacionConFirma(
             DocumentoFirma doc, Connection con)
             throws SQLException, BDException {
         m_Log.info("DocumentosExpedienteDAO.revertEstadoDuplicadoDocumentoTramitacionConFirma");
         PreparedStatement ps = null;
         int rowsUpdated = 0;
         
         try {
             int codMunicipio = doc.getCodMunicipio();
             String codProcedimiento = doc.getCodProcedimiento();
             int ejercicio = doc.getEjercicio();
             String numExpediente = doc.getNumExpediente();
             int codTramite = doc.getCodTramite();
             int ocuTramite = doc.getOcurrenciaTramite();
             int codDocumento = doc.getCodDocumento();
             String estadoFirma = doc.getEstadoFirma();
             
             if (ConstantesDatos.ESTADO_FIRMA_TIPO_FLUJO_ORIGINAL.equals(estadoFirma)
                     || ConstantesDatos.ESTADO_FIRMA_TIPO_USUARIO_ORIGINAL.equals(estadoFirma)) {
                
                StringBuilder query = new StringBuilder();
                query.append("UPDATE E_CRD ")
                     .append("SET CRD_FIR_EST = ? ")
                     .append("WHERE CRD_MUN = ? ")
                     .append("  AND CRD_PRO = ? ")
                     .append("  AND CRD_EJE = ? ")
                     .append("  AND CRD_NUM = ? ")
                     .append("  AND CRD_TRA = ? ")
                     .append("  AND CRD_OCU = ? ")
                     .append("  AND CRD_NUD = ? ");

                // Cambiamos el estado de original a normal
                if (ConstantesDatos.ESTADO_FIRMA_TIPO_FLUJO_ORIGINAL.equals(estadoFirma)) {
                    estadoFirma = ConstantesDatos.ESTADO_FIRMA_TIPO_FLUJO;
                } else if (ConstantesDatos.ESTADO_FIRMA_TIPO_USUARIO_ORIGINAL.equals(estadoFirma)) {
                    estadoFirma = ConstantesDatos.ESTADO_FIRMA_TIPO_USUARIO;
                }
                
                if(m_Log.isDebugEnabled()) {
                    m_Log.debug(String.format("Query = %s", query));
                    m_Log.debug(String.format("CRD_FIR_EST = %s", estadoFirma));
                    m_Log.debug(String.format("CRD_MUN = %s", codMunicipio));
                    m_Log.debug(String.format("CRD_PRO = %s", codProcedimiento));
                    m_Log.debug(String.format("CRD_EJE = %s", ejercicio));
                    m_Log.debug(String.format("CRD_NUM = %s", numExpediente));
                    m_Log.debug(String.format("CRD_TRA = %s", codTramite));
                    m_Log.debug(String.format("CRD_OCU = %s", ocuTramite));
                    m_Log.debug(String.format("CRD_NUD = %s", codDocumento));
                }

                ps = con.prepareStatement(query.toString());

                JdbcOperations.setValues(ps, 1, 
                        estadoFirma,
                        codMunicipio,
                        codProcedimiento,
                        ejercicio,
                        numExpediente,
                        codTramite,
                        ocuTramite,
                        codDocumento);

                rowsUpdated = ps.executeUpdate();

                if (rowsUpdated != 1) {
                    m_Log.error("No se ha actualizado el numero correcto de filas en BBDD: %d");
                    throw new BDException(String.format(
                            "No se ha actualizado el numero correcto de filas en BBDD: %d",
                            rowsUpdated));
                }
             }
         } catch(SQLException sqle) {
             m_Log.error("Ha ocurrido un error al  los datos del documento");
             throw sqle;
         } finally {
             try {
                 if(ps!=null) ps.close();
             } catch (SQLException ex){
                 m_Log.error("Ha ocurrido un error al liberar recursos de BBDD.");
             }
         }
     }
    
     public int setDocumentoTramitacionConFirma(DocumentoFirma doc, AdaptadorSQLBD adapt, Connection con) throws SQLException{
         m_Log.info("DocumentosExpedienteDAO.setDocumentoTramitacionConFirma");
         PreparedStatement ps = null;
         int insertado = 0;
         String codDocNuevo;
         int contbd = 1;
         byte[] contenidoFichero = doc.getFichero();
         String codProcedimiento = doc.getCodProcedimiento();
         int ejercicio = doc.getEjercicio();
         String numExpediente = doc.getNumExpediente();
         int codTramite = doc.getCodTramite();
         int ocuTramite = doc.getOcurrenciaTramite();
         Date fecAlta = doc.getFechaAltaDocumento();
         Date fecMod = doc.getFechaModDocumento();
         Date fecInf = doc.getFechaInforme();
         int firFd = doc.getFirFd();
         long idMetadatos = doc.getIdMetadatos();
         String estFirmaOriginal = doc.getEstadoFirma();
         String estFirmaFinal = null;
         String query;
         
         try {
             if("U".equals(estFirmaOriginal)) { // Estado de firma del documento duplicado de documento con estado U
                 estFirmaFinal = "V";
             } else if("L".equals(estFirmaOriginal)) { // Estado de firma del documento duplicado de documento con estado L
                 estFirmaFinal = "M";
             } else {
                 estFirmaFinal = estFirmaOriginal;
             }
             
             codDocNuevo = this.obtenMaximo(codProcedimiento, ejercicio, numExpediente, codTramite, ocuTramite, adapt, con);
             doc.setCodDocumento(Integer.parseInt(codDocNuevo));
             doc.setEstadoFirma(estFirmaFinal);
            
             doc.setFechaAltaDocumentoAsString(DateOperations.toString(fecAlta, DateOperations.LATIN_DATE_FORMAT));
             doc.setFechaInformeAsString(DateOperations.toString(fecInf, DateOperations.LATIN_DATE_FORMAT));
             doc.setFechaModDocumentoAsString(DateOperations.toString(fecMod, DateOperations.LATIN_DATE_FORMAT));
             
             query = "INSERT INTO E_CRD (CRD_MUN,CRD_PRO,CRD_EJE,CRD_NUM,CRD_TRA,CRD_OCU,CRD_NUD,CRD_FAL,CRD_FMO,CRD_USC,CRD_USM,CRD_FIL,CRD_DES,CRD_DOT,"
                     + "CRD_FIR_EST,CRD_EXP_FD,CRD_DOC_FD,CRD_FIR_FD,CRD_FINF,CRD_ID_METADATO) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
             if(m_Log.isDebugEnabled()) m_Log.debug("Query = " + query);
             
             ps = con.prepareStatement(query);
             
             ps.setInt(contbd++, doc.getCodMunicipio());
             ps.setString(contbd++, codProcedimiento);
             ps.setInt(contbd++, ejercicio);
             ps.setString(contbd++, numExpediente);
             ps.setInt(contbd++, codTramite);
             ps.setInt(contbd++, ocuTramite);
             ps.setString(contbd++, codDocNuevo);
             if(fecAlta != null) {
                 ps.setDate(contbd++, new java.sql.Date(fecAlta.getTime()));
             } else {
                 ps.setNull(contbd++, java.sql.Types.DATE);
             }
             if(fecAlta != null) {
                 ps.setDate(contbd++, new java.sql.Date(fecMod.getTime()));
             } else {
                 ps.setNull(contbd++, java.sql.Types.DATE);
             }
             ps.setInt(contbd++, doc.getCodUsuarioAltaDoc());
             ps.setInt(contbd++, doc.getCodUsuarioModDoc());
             if(contenidoFichero != null) { 
                java.io.InputStream st = new java.io.ByteArrayInputStream(contenidoFichero);
                ps.setBinaryStream(contbd++,st,contenidoFichero.length);
             }
             ps.setString(contbd++, doc.getNombreDocumento());
             ps.setInt(contbd++, doc.getNumeroDocumento());
             ps.setString(contbd++, estFirmaFinal); 
             ps.setString(contbd++, doc.getExpFd());
             ps.setString(contbd++, doc.getDocFd());
             if(firFd != -1)
                 ps.setInt(contbd++, firFd);
             else
                 ps.setNull(contbd++, java.sql.Types.INTEGER);
             if(fecInf != null)
                 ps.setDate(contbd++, new java.sql.Date(fecInf.getTime()));
             else
                 ps.setNull(contbd++, java.sql.Types.DATE);
             if(idMetadatos !=  -1L)
                 ps.setLong(contbd++, idMetadatos);
             else
                 ps.setNull(contbd++, java.sql.Types.INTEGER);
             
             insertado = ps.executeUpdate();
             
         } catch(SQLException sqle) {
             m_Log.error("Ha ocurrido un error al  los datos del documento");
             throw sqle;
         } finally {
             try {
                 if(ps!=null) ps.close();
             } catch (SQLException ex){
                 m_Log.error("Ha ocurrido un error al liberar recursos de BBDD.");
             }
         }
         
         if(insertado > 0) {
             return doc.getCodDocumento();
         }
          return -1;
     }
     
      public void eliminarDocumentoTramitacionDuplicado(DocumentoFirmaBBDD doc, AdaptadorSQLBD adapt, Connection con) throws SQLException{
         m_Log.info("DocumentoExpedienteDAO.eliminarDocumentoTramitacionDuplicado");
         
         PreparedStatement ps = null;
         
         String query;
         int contbd = 1;
         
         String codProcedimiento = doc.getCodProcedimiento();
         int ejercicio = doc.getEjercicio();
         String numExpediente = doc.getNumExpediente();
         int codTramite = doc.getCodTramite();
         int ocuTramite = doc.getOcurrenciaTramite();
         int codDocumento = doc.getCodDocumento();
         int codMunicipio = doc.getCodMunicipio();
         try{
             query = "DELETE FROM E_CRD WHERE "
                     + "CRD_MUN = ? AND CRD_PRO = ? AND CRD_EJE = ? AND CRD_NUM = ? AND CRD_TRA = ? AND CRD_OCU = ? AND CRD_NUD = ?";
             
             ps = con.prepareStatement(query);
             
            ps.setInt(contbd++, codMunicipio);
            ps.setString(contbd++, codProcedimiento);
            ps.setInt(contbd++, ejercicio);
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codTramite);
            ps.setInt(contbd++, ocuTramite);
            ps.setInt(contbd++, codDocumento);
            
           if(m_Log.isDebugEnabled()) m_Log.debug(query);

            ps.executeUpdate();
            ps.close();
         } catch (SQLException sqle) {
             m_Log.error("Ha ocurrido un error al eliminar el documento duplicado");
             throw sqle;
         } finally{
             try {
                 if(ps!=null) ps.close();
             } catch (SQLException ex){
                 m_Log.error("Ha ocurrido un error al liberar recursos de BBDD.");
             }
         }
     }
      
      public void eliminarTodosDocumentosTramitacion(Documento doc, Connection con) throws SQLException{
         m_Log.info("DocumentoExpedienteDAO.eliminarDocumentoTramitacionDuplicado BEGIN");
         
         PreparedStatement ps = null;
         
         String query;
         int contbd = 1;
         
         String codProcedimiento = doc.getCodProcedimiento();
         int ejercicio = doc.getEjercicio();
         String numExpediente = doc.getNumeroExpediente();
         int codTramite = doc.getCodTramite();
         int ocuTramite = doc.getOcurrenciaTramite();
         int codMunicipio = doc.getCodMunicipio();
         try{
             query = "DELETE FROM E_CRD WHERE "
                     + "CRD_MUN = ? AND CRD_PRO = ? AND CRD_EJE = ? AND CRD_NUM = ? AND CRD_TRA = ? AND CRD_OCU = ? ";
             
             ps = con.prepareStatement(query);
             
            ps.setInt(contbd++, codMunicipio);
            ps.setString(contbd++, codProcedimiento);
            ps.setInt(contbd++, ejercicio);
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codTramite);
            ps.setInt(contbd++, ocuTramite);
            
           if(m_Log.isDebugEnabled()) m_Log.debug(query);

            ps.executeUpdate();
            ps.close();
         } catch (SQLException sqle) {
             m_Log.error("Ha ocurrido un error al eliminar el documento duplicado");
             throw sqle;
         } finally{
             try {
                 if(ps!=null) ps.close();
             } catch (SQLException ex){
                 m_Log.error("Ha ocurrido un error al liberar recursos de BBDD.");
                 throw ex;
             }
         }
        m_Log.info("DocumentoExpedienteDAO.eliminarDocumentoTramitacionDuplicado END");
     }
}