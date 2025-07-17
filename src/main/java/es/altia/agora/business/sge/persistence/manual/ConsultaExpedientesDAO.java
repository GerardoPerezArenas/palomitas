// NOMBRE DEL PAQUETE
package es.altia.agora.business.sge.persistence.manual;

// PAQUETES IMPORTADOS
 
import es.altia.agora.business.administracion.mantenimiento.CamposListadosParametrizablesVO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.ConfInformeValueObject;
import es.altia.agora.business.sge.ConsultaExpedientesValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.ConfInformeManager;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.interfaces.user.web.util.FormateadorTercero;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraCampoModuloIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase DefinicionProcedimientosDAO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 *
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */

public class ConsultaExpedientesDAO {

    private static ConsultaExpedientesDAO instance = null;
    protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log = LogFactory.getLog(ConsultaExpedientesDAO.class.getName());

    private static final String  COLUMNA_ORDEN_INTERESADO="19";
    public static final int CONSULTA_EXPEDIENTES_CAMPLIST_COD_ORDEN_EXP_NUM = 5;	
    public static final String CONSULTA_EXPEDIENTES_CAMPLIST_NOM_ORDEN_EXP_NUM = "EXP_NUM";
    boolean consultaInsensitiva=false;

    protected ConsultaExpedientesDAO() {
        super();
        // Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

    }

    public static ConsultaExpedientesDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        synchronized (ConsultaExpedientesDAO.class) {
            if (instance == null) {
                instance = new ConsultaExpedientesDAO();
            }
        }
        return instance;
    }


    private String obtenerSentenciaConFiltroCampoSuplementarios(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                                                TransformacionAtributoSelect transformador, String[] params, UsuarioValueObject uVO)
            throws BDException {
        
        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;

        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");

            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }
        
        String from = "  DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                //"PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom,E_EXR.EXR_TOP"; // Para el order
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom"; // Para el order

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) from += ", DNN_RCA";

        ArrayList<String> join = new ArrayList<String>();


        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
         join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");

        if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
            join.add("LEFT");
            join.add("E_EXT");
            join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");
            
            /*** SE BUSCAN LOS EXPEDIENTES QUE TIENEN COMO INTERESADO ALGÚN TERCERO CON UN DETERMINADO DOCUMENTO ***/
            if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                join.add("LEFT");
                join.add("T_HTE");
                join.add("EXT_TER = T_HTE.HTE_TER AND EXT_NVR = T_HTE.HTE_NVR");                
            }            
        }
                
        /**
            join.add("LEFT");
            join.add("E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");        
        **/
        
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }

        HashMap datosConsulta = consExpVO.getCamposSuplementarios();
        int z = 0;
        int tamaño = 0;
        if(datosConsulta!=null && datosConsulta.get("TAMAÑO")!=null){
            tamaño = (Integer) datosConsulta.get("TAMAÑO");
        }
        datosConsulta.remove("TAMAÑO");
        EstructuraCampo estructura;
        String clave;
        String valor;
        String tipo;
        String tramite;
        while (z < tamaño) {
            estructura = (EstructuraCampo) datosConsulta.get(String.valueOf(z));
            clave = estructura.getCodCampo();
            valor = estructura.getValorConsulta();
            tipo = estructura.getURLPlantilla();
            tramite = estructura.getCodTramite();
            if (m_Log.isDebugEnabled()) m_Log.debug("CampoSuplementario: Existe Tramite= " + tramite);
            if (tramite == null) { // Se trata de campos suplementarios de expediente
                 if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEX_" + z;
                    join.add("E_TDEX " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEX_NUM AND " + tabla + ".TDEX_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEX_VALOR", valor, false));
                 } else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDE_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDE_VALOR";
                    join.add("E_TDE " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = "'"+valor+"'";
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDE_NUM AND " + tabla + ".TDE_COD = '" + clave + "' AND " + filtroDesplegable +")");
                } else if (tipo.indexOf("Texto") != -1) {
                    consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXT_" + z;
                    join.add("E_TXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXT_NUM AND " + tabla + ".TXT_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXT_VALOR", valor, false));                
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUC_" + z;
                    join.add("E_TNUC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUC_NUM AND " + tabla + ".TNUC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUC_VALOR", valor));
                 } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNU_" + z;
                    join.add("E_TNU " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNU_NUM AND " + tabla + ".TNU_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNU_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFEC_" + z;
                    join.add("E_TFEC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFEC_NUM AND " + tabla + ".TFEC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFEC_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFE_" + z;
                    join.add("E_TFE " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFE_NUM AND " + tabla + ".TFE_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFE_VALOR", valor));    
                }
            } else { // Se trata de campos suplementarios de tramites visibles a nivel de expediente
                if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEXT_" + z;
                    join.add("E_TDEXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEXT_NUM AND " + tabla + ".TDEXT_COD = '" + clave + "' AND " + tabla + ".TDEXT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEXT_VALOR", valor, false));            
                }else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDET_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDET_VALOR";
                    join.add("E_TDET " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = "'"+valor+"'";
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDET_NUM AND " + tabla + ".TDET_COD = '" + clave + "' AND " + tabla + ".TDET_TRA = " + tramite +
                            " AND " + filtroDesplegable + ")");
                } else if (tipo.indexOf("Texto") != -1) {
                    consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXTT_" + z;
                    join.add("E_TXTT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXTT_NUM AND " + tabla + ".TXTT_COD = '" + clave + "' AND " + tabla + ".TXTT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXTT_VALOR", valor, false));
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUCT_" + z;
                    join.add("E_TNUCT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUCT_NUM AND " + tabla + ".TNUCT_COD = '" + clave + "' AND " + tabla + ".TNUCT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUCT_VALOR", valor));
                } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUT_" + z;
                    join.add("E_TNUT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUT_NUM AND " + tabla + ".TNUT_COD = '" + clave + "' AND " + tabla + ".TNUT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUT_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFECT_" + z;
                    join.add("E_TFECT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFECT_NUM AND " + tabla + ".TFECT_COD = '" + clave + "' AND " + tabla + ".TFECT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFECT_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFET_" + z;
                    join.add("E_TFET " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFET_NUM AND " + tabla + ".TFET_COD = '" + clave + "' AND " + tabla + ".TFET_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFET_VALOR", valor));
                }
            }
            z = z + 1;
        }
        // Fin Parte de Consulta  para campos Suplementarios
        join.add("false");

        String where = "PML_CMP = 'NOM' AND PML_LENG = '"+m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";

        where += "(PRO_TIN = 0 OR (PRO_TIN = 1 AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = TRA_UIN)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))))";


        if (consExpVO.getCodProcedimiento() != null) {
            String[] parametros = new String[2];
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_NUM", consExpVO.getNumeroExpediente().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }

        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) condicion = "0)";
                else if ("cerrado".equals(consExpVO.getEstado())) condicion = "9 OR EXP_EST =9)";
                else if ("anulado".equals(consExpVO.getEstado())) condicion = "1 OR EXP_EST =1)";
                where += " AND (EXP_EST = " + condicion;
            }
        }
        
        if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
            if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero()) &&
                    consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                String condicion = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_TID", consExpVO.getTipoDocumentoTercero().trim(), false);
                String condicion1 = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_DOC", consExpVO.getDocumentoTercero().trim(), false);
                if (!"".equals(condicion) && !"".equals(condicion1)) {
                    where += " AND " + condicion + " AND " + condicion1;
                }
            }
        } else {
            if (consExpVO.getTitular() != null) {
                if (!"".equals(consExpVO.getTitular())) {
                    String condicion = transformador.construirCondicionWhereConOperadores("TER.EXT_TER", consExpVO.getTercero().trim(), false);
                    String condicion1 = transformador.construirCondicionWhereConOperadores("TER.EXT_NVR", consExpVO.getVersionTercero().trim(), false);
                    if (!"".equals(condicion) && !"".equals(condicion1)) {
                        where += " AND " + condicion + " AND " + condicion1;
                    }
                }
            }
        }

        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("TRA_CLS", consExpVO.getCodClasifTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_UOR", consExpVO.getCodUnidadTram().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_UOR = " + cUT;
                    else where += " OR EXP_UOR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("CRO_TRA", consExpVO.getCodTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }
        
        
        /** ORIGINAL
         if (consExpVO.getTipoInicio()!= null) {
            if (!"Todos".equals(consExpVO.getTipoInicio())) {
                if ("Instancia".equals(consExpVO.getTipoInicio())) {
                    where+=" AND E_EXR.EXR_TOP= 0 ";
                }
                else if ("Oficio".equals(consExpVO.getTipoInicio())) {                    
                    where += "AND (E_EXR.EXR_TOP <> 0 OR E_EXR.EXR_TOP IS NULL)";
                }                
            }
        }
        ***/
        if (consExpVO.getTipoInicio()!= null && consExpVO.getTipoInicio().equals("Instancia")) {
            where+= " AND EXP_NUM IN (SELECT EXR_NUM FROM E_EXR WHERE EXR_NUM=EXP_NUM AND EXR_MUN=EXP_MUN AND EXR_PRO=EXP_PRO AND EXR_TOP=0)";
        }else if(consExpVO.getTipoInicio()!= null && consExpVO.getTipoInicio().equals("Oficio")) {
            where+= " AND EXP_NUM NOT IN (SELECT EXR_NUM FROM E_EXR WHERE EXR_NUM=EXP_NUM AND EXR_MUN=EXP_MUN AND EXR_PRO=EXP_PRO AND EXR_TOP=0)";
        }//
        
        where = where + " AND (PRO_RESTRINGIDO=0 OR (PRO_RESTRINGIDO=1 AND E_PRO.PRO_COD IN (SELECT PRO_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE USU_COD="
                + uVO.getIdUsuario() + " AND ORG_COD=" + uVO.getOrgCod() + ")))";
                                
        
        ////MAI
      
        
        ArrayList<EstructuraCampoModuloIntegracionVO> camposConsultaModuloExterno = consExpVO.getCamposConsultaModuloExterno();
        
        if(camposConsultaModuloExterno!=null){
            int tamanho=camposConsultaModuloExterno.size();
            if (m_Log.isDebugEnabled()) m_Log.debug("Tamanho de la lista de campos de consulta de modulos externos = " + tamanho);
            int h=0;
            String consultaSQL="";
            while (h < tamanho) { 
                //Hai que construir las sentencias, enchufar los valores que vienen
                //de la vista, en las interrogaciones.
                EstructuraCampoModuloIntegracionVO campoConsultaMEVO =  camposConsultaModuloExterno.get(h);
                if (m_Log.isDebugEnabled()) m_Log.debug("CampoConsultaMEVO. Valor del campo Consulta = " +campoConsultaMEVO.getValorConsulta());
                // Si el campo de la vista no está cubierto, NO filtramos por él.
                if (campoConsultaMEVO.getValorConsulta()!=null){
                   
                    consultaSQL= campoConsultaMEVO.getConsultaSql();
                    if (m_Log.isDebugEnabled()) m_Log.debug("CampoConsultaMEVO. La consulta recuperada del properties = " + consultaSQL);
                    //Miramos si la consulta tiene interrogación (que siempre debe de tener)
                    if (consultaSQL.contains("?")){
                        //Agora sustituir o que hai na posicion do interrogante, polo valor do campo
                        //Así funciona para campos, numéricos e de texto 
                        consultaSQL=consultaSQL.replace("?", "'"+campoConsultaMEVO.getValorConsulta()+"'");
                    
                    }
                    if (m_Log.isDebugEnabled()) m_Log.debug("CampoConsultaMEVO, consulta despues de enchufarle el valor:"+consultaSQL);
        
                    where=where + " AND EXP_NUM IN ( "+ consultaSQL+ ")";
                    
                 }
                 h=h+1; 
            } 
            
        }  
        
        //MAI
        if (m_Log.isDebugEnabled()) m_Log.debug("ConsultaExpedienteDAO.obtenerSentenciaConFiltroCampoSuplementarios.END");
        return oad.join(from, where, join.toArray(new String[]{}));
    }

    
    
     private String obtenerSentenciaConFiltroCampoSuplementariosImprimir(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                                                TransformacionAtributoSelect transformador, String[] params, UsuarioValueObject uVO)
            throws BDException {

        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;

        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");

            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACI?N " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }

        String from = "  DISTINCT  EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                //"PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom,E_EXR.EXR_TOP"; // Para el order
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom,EXT_TER,EXT_ROL, "+
                 oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1"})+ " AS ap1, "+
                oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA2",  "T_HTE.HTE_AP2"})+ " AS ap2, "
                + " HTE_NOM AS nombre";

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) from += ", DNN_RCA";

        ArrayList<String> join = new ArrayList<String>();


        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
         join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");
        join.add("LEFT");
        join.add("E_EXT");
        join.add("EXP_NUM=EXT_NUM");
        join.add("LEFT");
        join.add("T_HTE");
        
        
        if(consExpVO.getCodigoRol()!=null && !"".equals(consExpVO.getCodigoRol().trim())) { 
             join.add("EXT_TER =HTE_TER and EXT_NVR = HTE_NVR ");           
        }
        else  if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
             join.add("EXT_TER =HTE_TER and EXT_NVR = HTE_NVR ");
        }else
        {
             join.add("EXT_TER =HTE_TER and EXT_NVR = HTE_NVR AND 1=MOSTRAR");
        }
        

        if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
            join.add("LEFT");
            join.add("E_EXT");
            join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");
        }
                
        /**
            join.add("LEFT");
            join.add("E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");        
        **/
        
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }

        HashMap datosConsulta = consExpVO.getCamposSuplementarios();
        int z = 0;
        int tamaño = 0;
        if(datosConsulta!=null && datosConsulta.get("TAMAÑO")!=null){
            tamaño = (Integer) datosConsulta.get("TAMAÑO");
        }
        datosConsulta.remove("TAMAÑO");
        EstructuraCampo estructura;
        String clave;
        String valor;
        String tipo;
        String tramite;
        while (z < tamaño) {
            estructura = (EstructuraCampo) datosConsulta.get(String.valueOf(z));
            clave = estructura.getCodCampo();
            valor = estructura.getValorConsulta();
            tipo = estructura.getURLPlantilla();
            tramite = estructura.getCodTramite();
            if (m_Log.isDebugEnabled()) m_Log.debug("CampoSuplementario: Existe Tramite= " + tramite);
            if (tramite == null) { // Se trata de campos suplementarios de expediente
                 if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEX_" + z;
                    join.add("E_TDEX " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEX_NUM AND " + tabla + ".TDEX_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEX_VALOR", valor, false));
                 } else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDE_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDE_VALOR";
                    join.add("E_TDE " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = "'"+valor+"'";
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDE_NUM AND " + tabla + ".TDE_COD = '" + clave + "' AND " + filtroDesplegable +")");
                } else if (tipo.indexOf("Texto") != -1) {
                    boolean consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXT_" + z;
                    join.add("E_TXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXT_NUM AND " + tabla + ".TXT_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXT_VALOR", valor, false));                
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUC_" + z;
                    join.add("E_TNUC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUC_NUM AND " + tabla + ".TNUC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUC_VALOR", valor));
                 } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNU_" + z;
                    join.add("E_TNU " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNU_NUM AND " + tabla + ".TNU_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNU_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFEC_" + z;
                    join.add("E_TFEC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFEC_NUM AND " + tabla + ".TFEC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFEC_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFE_" + z;
                    join.add("E_TFE " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFE_NUM AND " + tabla + ".TFE_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFE_VALOR", valor));    
                }
            } else { // Se trata de campos suplementarios de tramites visibles a nivel de expediente
                if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEXT_" + z;
                    join.add("E_TDEXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEXT_NUM AND " + tabla + ".TDEXT_COD = '" + clave + "' AND " + tabla + ".TDEXT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEXT_VALOR", valor, false));            
                }else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDET_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDET_VALOR";
                    join.add("E_TDET " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = "'"+valor+"'";
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDET_NUM AND " + tabla + ".TDET_COD = '" + clave + "' AND " + tabla + ".TDET_TRA = " + tramite +
                            " AND " + filtroDesplegable + ")");
                } else if (tipo.indexOf("Texto") != -1) {
                    boolean consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXTT_" + z;
                    join.add("E_TXTT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXTT_NUM AND " + tabla + ".TXTT_COD = '" + clave + "' AND " + tabla + ".TXTT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXTT_VALOR", valor, false));
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUCT_" + z;
                    join.add("E_TNUCT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUCT_NUM AND " + tabla + ".TNUCT_COD = '" + clave + "' AND " + tabla + ".TNUCT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUCT_VALOR", valor));
                } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUT_" + z;
                    join.add("E_TNUT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUT_NUM AND " + tabla + ".TNUT_COD = '" + clave + "' AND " + tabla + ".TNUT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUT_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFECT_" + z;
                    join.add("E_TFECT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFECT_NUM AND " + tabla + ".TFECT_COD = '" + clave + "' AND " + tabla + ".TFECT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFECT_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFET_" + z;
                    join.add("E_TFET " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFET_NUM AND " + tabla + ".TFET_COD = '" + clave + "' AND " + tabla + ".TFET_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFET_VALOR", valor));
                }
            }
            z = z + 1;
        }
        // Fin Parte de Consulta  para campos Suplementarios
        join.add("false");

         String where =
                 " (EXT_ROL IN (select ROL_COD from E_ROL where ROL_MUN = " + uVO.getOrgCod() + " and ROL_PRO = EXP_PRO) or EXT_ROL is null)";
        
         where = where + " and PML_CMP = 'NOM' AND PML_LENG = '" + m_ConfigTechnical.getString("idiomaDefecto") + "' AND ";


        where += "(PRO_TIN = 0 OR (PRO_TIN = 1 AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = TRA_UIN)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))))";


        if (consExpVO.getCodProcedimiento() != null) {
            String[] parametros = new String[2];
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_NUM", consExpVO.getNumeroExpediente().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }

        if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
            if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero()) &&
                    consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                String condicion = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_TID", consExpVO.getTipoDocumentoTercero().trim(), false);
                String condicion1 = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_DOC", consExpVO.getDocumentoTercero().trim(), false);
                if (!"".equals(condicion) && !"".equals(condicion1)) {
                    where += " AND " + condicion + " AND " + condicion1;
                }
            }
        } else {
            if (consExpVO.getTitular() != null) {
                if (!"".equals(consExpVO.getTitular())) {
                    String condicion = transformador.construirCondicionWhereConOperadores("TER.EXT_TER", consExpVO.getTercero().trim(), false);
                    String condicion1 = transformador.construirCondicionWhereConOperadores("TER.EXT_NVR", consExpVO.getVersionTercero().trim(), false);
                    if (!"".equals(condicion) && !"".equals(condicion1)) {
                        where += " AND " + condicion + " AND " + condicion1;
                    }
                }
            }
        }
        
        if (consExpVO.getTitular() != null) {
            if (!"".equals(consExpVO.getTitular())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXT_TER", consExpVO.getTercero().trim(), false);
                String condicion1 = transformador.construirCondicionWhereConOperadores("EXT_NVR", consExpVO.getVersionTercero().trim(), false);
                if (!"".equals(condicion) && !"".equals(condicion1)) {
                    where += " AND " + condicion + " AND " + condicion1;
                }
            }
        }
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("TRA_CLS", consExpVO.getCodClasifTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_UOR", consExpVO.getCodUnidadTram().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_UOR = " + cUT;
                    else where += " OR EXP_UOR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("CRO_TRA", consExpVO.getCodTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }
        
        
        /** ORIGINAL
         if (consExpVO.getTipoInicio()!= null) {
            if (!"Todos".equals(consExpVO.getTipoInicio())) {
                if ("Instancia".equals(consExpVO.getTipoInicio())) {
                    where+=" AND E_EXR.EXR_TOP= 0 ";
                }
                else if ("Oficio".equals(consExpVO.getTipoInicio())) {                    
                    where += "AND (E_EXR.EXR_TOP <> 0 OR E_EXR.EXR_TOP IS NULL)";
                }                
            }
        }
        ***/
        if (consExpVO.getTipoInicio()!= null && consExpVO.getTipoInicio().equals("Instancia")) {
            where+= " AND EXP_NUM IN (SELECT EXR_NUM FROM E_EXR WHERE EXR_NUM=EXP_NUM AND EXR_MUN=EXP_MUN AND EXR_PRO=EXP_PRO AND EXR_TOP=0)";
        }else if(consExpVO.getTipoInicio()!= null && consExpVO.getTipoInicio().equals("Oficio")) {
            where+= " AND EXP_NUM NOT IN (SELECT EXR_NUM FROM E_EXR WHERE EXR_NUM=EXP_NUM AND EXR_MUN=EXP_MUN AND EXR_PRO=EXP_PRO AND EXR_TOP=0)";
        }//
        
        where = where + " AND (PRO_RESTRINGIDO=0 OR (PRO_RESTRINGIDO=1 AND E_PRO.PRO_COD IN (SELECT PRO_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE USU_COD="
                + uVO.getIdUsuario() + " AND ORG_COD=" + uVO.getOrgCod() + ")))";
                                
        
        ////MAI
      
        
        ArrayList<EstructuraCampoModuloIntegracionVO> camposConsultaModuloExterno = consExpVO.getCamposConsultaModuloExterno();
        
        if(camposConsultaModuloExterno!=null){
            int tamanho=camposConsultaModuloExterno.size();
            if (m_Log.isDebugEnabled()) m_Log.debug("Tamanho de la lista de campos de consulta de modulos externos = " + tamanho);
            int h=0;
            String consultaSQL="";
            while (h < tamanho) { 
                //Hai que construir las sentencias, enchufar los valores que vienen
                //de la vista, en las interrogaciones.
                EstructuraCampoModuloIntegracionVO campoConsultaMEVO =  camposConsultaModuloExterno.get(h);
                if (m_Log.isDebugEnabled()) m_Log.debug("CampoConsultaMEVO. Valor del campo Consulta = " +campoConsultaMEVO.getValorConsulta());
                // Si el campo de la vista no está cubierto, NO filtramos por él.
                if (campoConsultaMEVO.getValorConsulta()!=null){
                   
                    consultaSQL= campoConsultaMEVO.getConsultaSql();
                    if (m_Log.isDebugEnabled()) m_Log.debug("CampoConsultaMEVO. La consulta recuperada del properties = " + consultaSQL);
                    //Miramos si la consulta tiene interrogación (que siempre debe de tener)
                    if (consultaSQL.contains("?")){
                        //Agora sustituir o que hai na posicion do interrogante, polo valor do campo
                        //Así funciona para campos, numéricos e de texto 
                        consultaSQL=consultaSQL.replace("?", "'"+campoConsultaMEVO.getValorConsulta()+"'");
                    
                    }
                    if (m_Log.isDebugEnabled()) m_Log.debug("CampoConsultaMEVO, consulta despues de enchufarle el valor:"+consultaSQL);
        
                    where=where + " AND EXP_NUM IN ( "+ consultaSQL+ ")";
                    
                 }
                 h=h+1; 
            } 
            
        }  
        
        //MAI
        if (m_Log.isDebugEnabled()) m_Log.debug("ConsultaExpedienteDAO.obtenerSentenciaConFiltroCampoSuplementarios.END");
        return oad.join(from, where, join.toArray(new String[]{}));
    }


    private String obtenerSentenciaConFiltroCampoSuplementariosByInteresado(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                                                TransformacionAtributoSelect transformador, String[] params, UsuarioValueObject uVO)
            throws BDException {


        m_Log.debug("*********** ConsultaExpedientesDAO.obtenerSentenciaConFiltroCampoSuplementariosByInteresado entrando");
        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");

        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;

        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");

            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACI?N " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }

        String from = "  DISTINCT  EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom"; // Para el order

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) from += ", DNN_RCA";

        from=from+", "+"T_HTE.HTE_TER, " +
                 oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1", "T_HTE.HTE_PA2",  "T_HTE.HTE_AP2",  "T_HTE.HTE_NOM"})+ " AS NOMBRE";


        ArrayList<String> join = new ArrayList<String>();


        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
         join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");


       
        join.add("LEFT");
        join.add("E_EXT TER");
        join.add("EXP_MUN = TER.EXT_MUN AND EXP_EJE = TER.EXT_EJE AND EXP_NUM = TER.EXT_NUM");


        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }

        //Los joins para ordenar por interesado

             join.add("LEFT");
             join.add("E_EXT A");
             join.add("E_EXP.EXP_EJE=A.EXT_EJE AND E_EXP.EXP_MUN=A.EXT_MUN AND E_EXP.EXP_NUM=A.EXT_NUM");
             join.add("LEFT");
             join.add("T_HTE");
             join.add("A.EXT_TER=T_HTE.HTE_TER AND A.EXT_NVR=T_HTE.HTE_NVR");

        HashMap datosConsulta = consExpVO.getCamposSuplementarios();
        int z = 0;
        //int tamaño = (Integer) datosConsulta.get("TAMAÑO");
        
        int tamaño = 0;
        if(datosConsulta!=null && datosConsulta.get("TAMAÑO")!=null){
            tamaño = (Integer) datosConsulta.get("TAMAÑO");
        }

        datosConsulta.remove("TAMAÑO");
        EstructuraCampo estructura;
        String clave;
        String valor;
        String tipo;
        String tramite;
        while (z < tamaño) {
            estructura = (EstructuraCampo) datosConsulta.get(String.valueOf(z));
            clave = estructura.getCodCampo();
            valor = estructura.getValorConsulta();
            tipo = estructura.getURLPlantilla();
            tramite = estructura.getCodTramite();
            if (m_Log.isDebugEnabled()) m_Log.debug("CampoSuplementario: Existe Tramite= " + tramite);
            if (tramite == null) { // Se trata de campos suplementarios de expediente
                if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEX_" + z;
                    join.add("E_TDEX " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEX_NUM AND " + tabla + ".TDEX_COD = '" + clave + "'" + 
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEX_VALOR", valor, false));
                 } else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDE_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDE_VALOR";
                    join.add("E_TDE " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = valor;
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDE_NUM AND " + tabla + ".TDE_COD = '" + clave + "' AND " + filtroDesplegable +")");
                } else if (tipo.indexOf("Texto") != -1) {
                    boolean consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXT_" + z;
                    join.add("E_TXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXT_NUM AND " + tabla + ".TXT_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXT_VALOR", valor, false));
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUC_" + z;
                    join.add("E_TNUC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUC_NUM AND " + tabla + ".TNUC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUC_VALOR", valor));
                } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNU_" + z;
                    join.add("E_TNU " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNU_NUM AND " + tabla + ".TNU_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNU_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFEC_" + z;
                    join.add("E_TFEC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFEC_NUM AND " + tabla + ".TFEC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFEC_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFE_" + z;
                    join.add("E_TFE " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFE_NUM AND " + tabla + ".TFE_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFE_VALOR", valor));
                }
            } else { // Se trata de campos suplementarios de tramites visibles a nivel de expediente
                if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEXT_" + z;
                    join.add("E_TDEXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEXT_NUM AND " + tabla + ".TDEXT_COD = '" + clave + "' AND " + tabla + ".TDEXT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEXT_VALOR", valor, false));            
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUCT_" + z;
                    join.add("E_TNUCT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUCT_NUM AND " + tabla + ".TNUCT_COD = '" + clave + "' AND " + tabla + ".TNUCT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUCT_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFECT_" + z;
                    join.add("E_TFECT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFECT_NUM AND " + tabla + ".TFECT_COD = '" + clave + "' AND " + tabla + ".TFECT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFECT_VALOR", valor));
                } else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDET_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDET_VALOR";
                    join.add("E_TDET " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = valor;
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDET_NUM AND " + tabla + ".TDET_COD = '" + clave + "' AND " + tabla + ".TDET_TRA = " + tramite +
                            " AND " + filtroDesplegable + ")");
                } else if (tipo.indexOf("Texto") != -1) {
                    boolean consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXTT_" + z;
                    join.add("E_TXTT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXTT_NUM AND " + tabla + ".TXTT_COD = '" + clave + "' AND " + tabla + ".TXTT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXTT_VALOR", valor, false));
                } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUT_" + z;
                    join.add("E_TNUT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUT_NUM AND " + tabla + ".TNUT_COD = '" + clave + "' AND " + tabla + ".TNUT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUT_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFET_" + z;
                    join.add("E_TFET " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFET_NUM AND " + tabla + ".TFET_COD = '" + clave + "' AND " + tabla + ".TFET_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFET_VALOR", valor));
                }
            }
            z = z + 1;
        }
        // Fin Parte de Consulta  para campos Suplementarios
        join.add("false");

        String where = "(A.EXT_TER IS NULL OR A.EXT_TER=(SELECT MIN(EXT_TER) FROM E_EXT WHERE EXT_NUM=EXP_NUM AND MOSTRAR=1)) AND PML_CMP = 'NOM' AND PML_LENG = '"+
                m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";

        
        where += "(PRO_TIN = 0 OR (PRO_TIN = 1 AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = TRA_UIN)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))))"+ " ";
                

        if (consExpVO.getCodProcedimiento() != null) {
            String[] parametros = new String[2];
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_NUM", consExpVO.getNumeroExpediente().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) condicion = "0)";
                else if ("cerrado".equals(consExpVO.getEstado())) condicion = "9 OR EXP_EST =9)";
                else if ("anulado".equals(consExpVO.getEstado())) condicion = "1 OR EXP_EST =1)";
                 where += " AND (EXP_EST = " + condicion ;
            }
        }

        if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
            if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero()) &&
                    consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                String condicion = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_TID", consExpVO.getTipoDocumentoTercero().trim(), false);
                String condicion1 = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_DOC", consExpVO.getDocumentoTercero().trim(), false);
                if (!"".equals(condicion) && !"".equals(condicion1)) {
                    where += " AND " + condicion + " AND " + condicion1;
                }
            }
        } else {
            if (consExpVO.getTitular() != null) {
                if (!"".equals(consExpVO.getTitular())) {
                    String condicion = transformador.construirCondicionWhereConOperadores("TER.EXT_TER", consExpVO.getTercero().trim(), false);
                    String condicion1 = transformador.construirCondicionWhereConOperadores("TER.EXT_NVR", consExpVO.getVersionTercero().trim(), false);
                    if (!"".equals(condicion) && !"".equals(condicion1)) {
                        where += " AND " + condicion + " AND " + condicion1;
                    }
                }
            }
        }
        
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("TRA_CLS", consExpVO.getCodClasifTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_UOR", consExpVO.getCodUnidadTram().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_UOR = " + cUT;
                    else where += " OR EXP_UOR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("CRO_TRA", consExpVO.getCodTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }

        return oad.join(from, where, join.toArray(new String[]{}));
    }
    
    
    
    
    
     private String obtenerSentenciaConFiltroCampoSuplementariosByInteresadoImprimir(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                                                TransformacionAtributoSelect transformador, String[] params, UsuarioValueObject uVO)
            throws BDException {


        m_Log.debug("*********** ConsultaExpedientesDAO.obtenerSentenciaConFiltroCampoSuplementariosByInteresado entrando");
        m_Log.debug("*********** ConsultaExpedientesDAO.obtenerSentenciaConFiltroCampoSuplementariosByInteresado entrando");
        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }

        String from = "  DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom,EXT_TER,EXT_ROL, "+
                oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1"})+ " AS ap1, "+
                oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA2",  "T_HTE.HTE_AP2"})+ " AS ap2, "
                + " HTE_NOM AS nombre";

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) from += ", DNN_RCA";

        from=from+", "+"T_HTE.HTE_TER, " +
                 oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1", "T_HTE.HTE_PA2",  "T_HTE.HTE_AP2",  "T_HTE.HTE_NOM"})+ " AS NOMBRE";


        ArrayList<String> join = new ArrayList<String>();


        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
         join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");


       
        join.add("LEFT");
        join.add("E_EXT TER");
        join.add("EXP_MUN = TER.EXT_MUN AND EXP_EJE = TER.EXT_EJE AND EXP_NUM = TER.EXT_NUM");


        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }

        //Los joins para ordenar por interesado

             join.add("LEFT");
             join.add("E_EXT A");
             join.add("E_EXP.EXP_EJE=A.EXT_EJE AND E_EXP.EXP_MUN=A.EXT_MUN AND E_EXP.EXP_NUM=A.EXT_NUM");
             join.add("LEFT");
             join.add("T_HTE");
             join.add("A.EXT_TER=T_HTE.HTE_TER AND A.EXT_NVR=T_HTE.HTE_NVR");

        HashMap datosConsulta = consExpVO.getCamposSuplementarios();
        int z = 0;
        //int tamaño = (Integer) datosConsulta.get("TAMAÑO");
        
        int tamaño = 0;
        if(datosConsulta!=null && datosConsulta.get("TAMAÑO")!=null){
            tamaño = (Integer) datosConsulta.get("TAMAÑO");
        }

        datosConsulta.remove("TAMAÑO");
        EstructuraCampo estructura;
        String clave;
        String valor;
        String tipo;
        String tramite;
        while (z < tamaño) {
            estructura = (EstructuraCampo) datosConsulta.get(String.valueOf(z));
            clave = estructura.getCodCampo();
            valor = estructura.getValorConsulta();
            tipo = estructura.getURLPlantilla();
            tramite = estructura.getCodTramite();
            if (m_Log.isDebugEnabled()) m_Log.debug("CampoSuplementario: Existe Tramite= " + tramite);
            if (tramite == null) { // Se trata de campos suplementarios de expediente
                if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEX_" + z;
                    join.add("E_TDEX " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEX_NUM AND " + tabla + ".TDEX_COD = '" + clave + "'" + 
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEX_VALOR", valor, false));
                 } else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDE_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDE_VALOR";
                    join.add("E_TDE " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = valor;
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDE_NUM AND " + tabla + ".TDE_COD = '" + clave + "' AND " + filtroDesplegable +")");
                } else if (tipo.indexOf("Texto") != -1) {
                    boolean consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXT_" + z;
                    join.add("E_TXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXT_NUM AND " + tabla + ".TXT_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXT_VALOR", valor, false));
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUC_" + z;
                    join.add("E_TNUC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUC_NUM AND " + tabla + ".TNUC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUC_VALOR", valor));
                } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNU_" + z;
                    join.add("E_TNU " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNU_NUM AND " + tabla + ".TNU_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNU_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFEC_" + z;
                    join.add("E_TFEC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFEC_NUM AND " + tabla + ".TFEC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFEC_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFE_" + z;
                    join.add("E_TFE " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFE_NUM AND " + tabla + ".TFE_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFE_VALOR", valor));
                }
            } else { // Se trata de campos suplementarios de tramites visibles a nivel de expediente
                if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEXT_" + z;
                    join.add("E_TDEXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEXT_NUM AND " + tabla + ".TDEXT_COD = '" + clave + "' AND " + tabla + ".TDEXT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEXT_VALOR", valor, false));            
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUCT_" + z;
                    join.add("E_TNUCT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUCT_NUM AND " + tabla + ".TNUCT_COD = '" + clave + "' AND " + tabla + ".TNUCT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUCT_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFECT_" + z;
                    join.add("E_TFECT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFECT_NUM AND " + tabla + ".TFECT_COD = '" + clave + "' AND " + tabla + ".TFECT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFECT_VALOR", valor));
                } else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDET_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDET_VALOR";
                    join.add("E_TDET " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = valor;
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDET_NUM AND " + tabla + ".TDET_COD = '" + clave + "' AND " + tabla + ".TDET_TRA = " + tramite +
                            " AND " + filtroDesplegable + ")");
                } else if (tipo.indexOf("Texto") != -1) {
                    boolean consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXTT_" + z;
                    join.add("E_TXTT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXTT_NUM AND " + tabla + ".TXTT_COD = '" + clave + "' AND " + tabla + ".TXTT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXTT_VALOR", valor, false));
                } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUT_" + z;
                    join.add("E_TNUT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUT_NUM AND " + tabla + ".TNUT_COD = '" + clave + "' AND " + tabla + ".TNUT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUT_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFET_" + z;
                    join.add("E_TFET " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFET_NUM AND " + tabla + ".TFET_COD = '" + clave + "' AND " + tabla + ".TFET_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFET_VALOR", valor));
                }
            }
            z = z + 1;
        }
        // Fin Parte de Consulta  para campos Suplementarios
        join.add("false");

        String where = "(A.EXT_TER IS NULL OR A.EXT_TER=(SELECT MIN(EXT_TER) FROM E_EXT WHERE EXT_NUM=EXP_NUM AND MOSTRAR=1)) AND PML_CMP = 'NOM' AND PML_LENG = '"+
                m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";

        
        where += "(PRO_TIN = 0 OR (PRO_TIN = 1 AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = TRA_UIN)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))))"+ " ";
                 

        if (consExpVO.getCodProcedimiento() != null) {
            String[] parametros = new String[2];
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion; 
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_NUM", consExpVO.getNumeroExpediente().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) condicion = "0)";
                else if ("cerrado".equals(consExpVO.getEstado())) condicion = "9 OR EXP_EST =9)";
                else if ("anulado".equals(consExpVO.getEstado())) condicion = "1 OR EXP_EST =1)";
                 where += " AND (EXP_EST = " + condicion ;
            }
        }

        if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
            if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero()) &&
                    consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                String condicion = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_TID", consExpVO.getTipoDocumentoTercero().trim(), false);
                String condicion1 = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_DOC", consExpVO.getDocumentoTercero().trim(), false);
                if (!"".equals(condicion) && !"".equals(condicion1)) {
                    where += " AND " + condicion + " AND " + condicion1;
                }
            }
        } else {
            if (consExpVO.getTitular() != null) {
                if (!"".equals(consExpVO.getTitular())) {
                    String condicion = transformador.construirCondicionWhereConOperadores("TER.EXT_TER", consExpVO.getTercero().trim(), false);
                    String condicion1 = transformador.construirCondicionWhereConOperadores("TER.EXT_NVR", consExpVO.getVersionTercero().trim(), false);
                    if (!"".equals(condicion) && !"".equals(condicion1)) {
                        where += " AND " + condicion + " AND " + condicion1;
                    }
                }
            }
        }

        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("TRA_CLS", consExpVO.getCodClasifTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_UOR", consExpVO.getCodUnidadTram().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_UOR = " + cUT;
                    else where += " OR EXP_UOR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("CRO_TRA", consExpVO.getCodTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }

        return oad.join(from, where, join.toArray(new String[]{}));
    }
    
    
    /**
     * Recupera el tipo de inicio de un expediente determinado
     * @param numExpediente
     * @param codProcedimiento
     * @param codMunicipio
     * @param ejercicio
     * @return String con el tipo de inicio, 0 si es de instancia de parte y 1 si es de oficio
     */
    public boolean isExpedienteIniciadoInstanciaParte(String numExpediente,String codProcedimiento,String codMunicipio,String ejercicio,Connection con){
        String sql = null;
        ResultSet rs = null;
        Statement st = null;
        boolean salida  = false;
        
        try{
            sql = "SELECT COUNT(*) AS NUM FROM E_EXR WHERE EXR_NUM='" + numExpediente + "' AND EXR_PRO='" + codProcedimiento + "' " + 
                  "AND EXR_MUN=" + codMunicipio + " AND EXR_EJE=" + ejercicio + " AND EXR_TOP=0";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;
            while(rs.next()){
                num = rs.getInt("NUM");                
            }
            
            if(num>=1) salida = true;
            
        }catch(SQLException e){
            m_Log.error("Se ha producido un error durante la ejecución de la consulta: " + e.getMessage());
            e.printStackTrace();            
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                m_Log.error("Se ha producido un error al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }
        }
        
        return salida;
    }
    

    private String obtenerSentenciaConFiltro(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO, boolean buscarInicioProc)
        throws BDException {
        
        
        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }
        
        
        m_Log.debug("eMPIEZO A obtenerSentenciaConFiltro");
        String from = "  DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom, 'N' AS EXHISTORICO ";

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            from += ", DNN_RCA," + GlobalNames.ESQUEMA_GENERICO + "A_USU,";
        }

        ArrayList<String> join = new ArrayList<String>();

        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
        join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");

        if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
            join.add("LEFT");
            join.add("E_EXT");
            join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");
            
            /*** SE BUSCAN LOS EXPEDIENTES QUE TIENEN COMO INTERESADO ALGÚN TERCERO CON UN DETERMINADO DOCUMENTO ***/
            if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                join.add("LEFT");
                join.add("T_HTE");
                join.add("EXT_TER = T_HTE.HTE_TER AND EXT_NVR = T_HTE.HTE_NVR");                
            }            
            
        }else{ //NO hay titular pero hay Rol 
       
            if(consExpVO.getCodigoRol()!=null && !"".equals(consExpVO.getCodigoRol().trim())) {  
              join.add("INNER");   
              join.add("E_EXT");
              join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");            
           }
        }

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }
  
        join.add("false");
        String where = "PML_CMP = 'NOM' AND PML_LENG = '"+m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";        
        
        
        if ((consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) ||
            (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) ||
            (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
            
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                 where += " EXP_NUM IN (SELECT EXR_NUM FROM E_EXR WHERE " +
                                  "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND " + 
                                  "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO ";
            }else
            {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM E_EXR WHERE " +
                                  "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND " + 
                                  "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO ";
            }
           
            
            if(consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())){
                where += " AND EXR_TIP='" + consExpVO.getCodTipoAnotacion() + "'";
            }
            
            if(consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())){
                where+= " AND EXR_NRE=" + consExpVO.getNumeroAnotacion();
            }
            
            if((consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))){
                where += " AND EXR_EJR=" + consExpVO.getEjercicioAnotacion();
            }
               
            if (hayDatosRegistroExterno(consExpVO, uVO, oad, params)) {
                where = where + "UNION "
                        + "SELECT EXREXT_MUN,EXREXT_PRO,EXREXT_NUM FROM E_EXREXT WHERE "
                        + "EXREXT_NUM=EXP_NUM AND EXREXT_MUN=EXP_MUN AND EXP_PRO=EXREXT_PRO ";

                if (consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) {
                    where += " AND EXREXT_TIP='" + consExpVO.getCodTipoAnotacion() + "'";
                }

                if (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) {
                    where += " AND EXREXT_NRE=" + consExpVO.getNumeroAnotacion();
                }

                if ((consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
                    where += " AND EXREXT_EJR=" + consExpVO.getEjercicioAnotacion();
                }
            }
            where = where + ") AND ";
        }//if

        
        
        /***  SI SE FILTRA POR TIPO DE INICIO ****/
        if((consExpVO.getTipoInicio()!=null && !"".equals(consExpVO.getTipoInicio())) && "Instancia".equalsIgnoreCase(consExpVO.getTipoInicio())){            
            where += " EXP_NUM IN (SELECT EXR_NUM FROM E_EXR WHERE " +
                                      "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND " + 
                                      "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO AND EXR_TOP=0) AND ";                            
            
        }else
        if((consExpVO.getTipoInicio()!=null && !"".equals(consExpVO.getTipoInicio())) && "Oficio".equalsIgnoreCase(consExpVO.getTipoInicio())){            
            where += " EXP_NUM NOT IN (SELECT EXR_NUM FROM E_EXR WHERE " +
                                           "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND " + 
                                           "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO AND EXR_TOP=0) AND ";                                                    
        }// if    
        
        /***  SI SE FILTRA POR EXPEDIENTE INICIADO DESDE REGISTRO TELEMATICO ****/
        if((consExpVO.getBusqTelematicos()!=null && !"".equals(consExpVO.getBusqTelematicos())) && "1".equals(consExpVO.getBusqTelematicos())){ 
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                where += " EXP_NUM IN (SELECT EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            } else {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            }
        }
        
        where += " (PRO_TIN = 0 OR (PRO_TIN = 1 AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = TRA_UIN))";

        if (buscarInicioProc) {
                m_Log.debug("***  buscarInicioProc");
            where += " OR (exists (SELECT DISTINCT UOU_UOR " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI " +
                    "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " " +
                    "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))";
        }

        where += "))";

        String[] parametros = new String[2];
 m_Log.debug("*** ANTES  getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getCodProcedimiento() != null) {
             m_Log.debug("***  getCodProcedimiento"+consExpVO.getCodProcedimiento());
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
      m_Log.debug("*** DESPUES getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                parametros[0] = "'"+consExpVO.getNumeroExpediente().trim()+"'";
                Config m_ConfigExp = ConfigServiceHelper.getConfig("Expediente");
                Boolean usarMascaraBusquedaGeneral=false;
                try{
                    usarMascaraBusquedaGeneral=m_ConfigExp.getString(Integer.toString(uVO.getOrgCod())  + "/usarMascaraBusquedaGeneral").toUpperCase().equals("SI");
                   
                }catch(Exception e)
                {
                    usarMascaraBusquedaGeneral=false;
                }
                String condicion;
                if (usarMascaraBusquedaGeneral){
                    int  posAst = parametros[0].indexOf("*");
                    if (posAst != -1)
                        condicion = "EXP_NUM LIKE "+ parametros[0].replace("*", "%");
                    else
                        condicion = "EXP_NUM="+ parametros[0];
                }else
                    condicion = "EXP_NUM="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        if (consExpVO.getEjercicioExpediente() != null) {
            if (!"".equals(consExpVO.getEjercicioExpediente())) {
               
                where += " AND EXP_EJE=" + consExpVO.getEjercicioExpediente();
                
            }
            
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        
        
        // Recuperar expedientes 
        if (consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion())
                && consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion())
                && consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==1){
                
            String condicion = "CRO_FEI BETWEEN " + oad.convertir("'"+ consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()+"'",oad.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                    oad.convertir("'"+consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY");

            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }            
        }        
        
        if (consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion())
                && consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion())
                && consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==2){
            
            String condicion = "CRO_FEF BETWEEN " + oad.convertir("'"+ consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()+"'",oad.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                    oad.convertir("'"+consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY");
            
            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }            
        }
        
         if((consExpVO.getTiempo()!=null) &&(!"".equals(consExpVO.getTiempo()))&& consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==1)
        {
            String tiempo=consExpVO.getTiempo();
            String condicion="";

            if ("1".equals(tiempo))  condicion = "CRO_FEI  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -7) ";
            if ("2".equals(tiempo))  condicion = "CRO_FEI  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -30) ";
            if ("3".equals(tiempo))  condicion = "CRO_FEI  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -90) ";
           
            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }   
        }
        if((consExpVO.getTiempo()!=null) &&(!"".equals(consExpVO.getTiempo()))&& consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==2)
        {
            String tiempo=consExpVO.getTiempo();
            String condicion="";

            if ("1".equals(tiempo))  condicion = "CRO_FEF  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -7) ";
            if ("2".equals(tiempo))  condicion = "CRO_FEF  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -30) ";
            if ("3".equals(tiempo))  condicion = "CRO_FEF  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -90) ";
           
            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }   
        }
                
        
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) {
                    condicion = "0";
                    where+=" AND EXP_EST = 0 ";
                }
                else if ("cerrado".equals(consExpVO.getEstado())) {
                    //condicion = "9";
                    where += " AND (EXP_EST=9 )";
                }
                else if ("anulado".equals(consExpVO.getEstado())) {
                    //condicion = "1";
                    where += " AND (EXP_EST=1 )";
                }
            }
        }
        
        
        if (consExpVO.getTercero() != null) {
            
            if(!CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
                if (!"".equals(consExpVO.getTercero())) {
                    where += " AND EXT_TER = " + consExpVO.getTercero().trim();                
                }                 
            }else{
                // SE CONSULTA POR DOCUMENTO DEL TERCERO Y NO POR CÓDIGO DE TERCERO
                if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                   
                    where += " AND HTE_DOC='" + consExpVO.getDocumentoTercero()+ "'" ;
                }                
            }   
        }
        
        
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = "TRA_CLS="+consExpVO.getCodClasifTramite();
                if (!"".equals(condicion)) {
                    where += " AND " + condicion+" AND CRO_FEF IS NULL";
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }

        String condFechaFinTramite;
        if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = " AND CRO_FEF IS NULL";
        else condFechaFinTramite = " AND CRO_FEF IS NOT NULL";

        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
               
                    where += " AND CRO_UTR=" + consExpVO.getCodUnidadTram().trim();

            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_UTR = " + cUT;
                    else where += " OR CRO_UTR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += condFechaFinTramite + ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
              
                where += " AND CRO_TRA=" + consExpVO.getCodTramite().trim();

            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }
        
        
        // Si tenemos asunto
            if (consExpVO.getAsunto() != null) {
                if (!"".equals(consExpVO.getAsunto().trim())) {
                    boolean consultaInsensitiva=true;
                    String condicion = "";
                        condicion = transformador.construirCondicionWhereConOperadores("EXP_ASU",consExpVO.getAsunto().trim(), false);
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
        // Si tenemos observaciones
            if (consExpVO.getObservaciones() != null) {
                if (!"".equals(consExpVO.getObservaciones().trim())) {
                    boolean consultaInsensitiva=true;
                    String condicion = "";
                        condicion = transformador.construirCondicionWhereConOperadores("EXP_OBS",consExpVO.getObservaciones().trim(), false);
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
            
            
         
         if (consExpVO.getCodigoRol() != null) {
                if (!"".equals(consExpVO.getCodigoRol().trim())) {
                        where += " AND  EXT_ROL= "+ consExpVO.getCodigoRol() ;}
         }   


        // óscar
        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientes".equals(consExpVO.getTipo())){
              m_Log.debug("\n Consulta expedientes--> expedientesPendientes");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND CRO_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientesVolumen".equals(consExpVO.getTipo())){
            m_Log.debug("\n Consulta expedientes--> expedientesPendientesVolumen");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND CRO_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesHistoricos".equals(consExpVO.getTipo())){
              m_Log.debug("\n Consulta expedientes--> expedientesHistoricos");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
             where += " AND CRO_FEF IS NOT NULL";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesCerrados".equals(consExpVO.getTipo())){
              m_Log.debug("\n Consulta expedientes--> expedientesHistoricos");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL AND (EXP_EST=1 or EXP_EST=9)";
        }
        
        // óscar

        // Se tiene en cuenta los procedimientos restringidos. Sólo se recuperan los exp. de los procedimientos no restringidos o, en el caso de que estén restringidos, se
        // recuperan también siempre y cuando el usuario tenga permiso sobre el procedimiento
        where += " AND (PRO_RESTRINGIDO=0 OR (PRO_RESTRINGIDO=1 AND E_PRO.PRO_COD IN (SELECT PRO_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE USU_COD="  +
                 uVO.getIdUsuario() + " AND ORG_COD=" +  uVO.getOrgCod() + ")))";


        return oad.join(from, where, join.toArray(new String[]{}));
    }
    
    
    
    private String obtenerSentenciaConFiltro2(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO,boolean buscarInicioProc)
        throws BDException {
        
        
        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }
        
        
        m_Log.debug("eMPIEZO A obtenerSentenciaConFiltro");
        String from = "  DISTINCT  EXP_MUN, EXP_NUM";
       

        ArrayList<String> join = new ArrayList<String>();

        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
        join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");

        if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
            join.add("LEFT");
            join.add("E_EXT");
            join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");
            
            /*** SE BUSCAN LOS EXPEDIENTES QUE TIENEN COMO INTERESADO ALGÚN TERCERO CON UN DETERMINADO DOCUMENTO ***/
            if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                join.add("LEFT");
                join.add("T_HTE");
                join.add("EXT_TER = T_HTE.HTE_TER AND EXT_NVR = T_HTE.HTE_NVR");                
            }            
            
        }else{ //NO hay titular pero hay Rol 
       
            if(consExpVO.getCodigoRol()!=null && !"".equals(consExpVO.getCodigoRol().trim())) {  
              join.add("INNER");   
              join.add("E_EXT");
              join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");            
           }
        }

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }
  
        join.add("false");
        String where = "PML_CMP = 'NOM' AND PML_LENG = '"+m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";        
        
        
        if ((consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) ||
            (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) ||
            (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
            
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                 where += " EXP_NUM IN (SELECT EXR_NUM FROM E_EXR WHERE " +
                                  "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND " + 
                                  "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO ";
            }else
            {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM E_EXR WHERE " +
                                  "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND " + 
                                  "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO ";
            }
           
            
            if(consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())){
                where += " AND EXR_TIP='" + consExpVO.getCodTipoAnotacion() + "'";
            }
            
            if(consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())){
                where+= " AND EXR_NRE=" + consExpVO.getNumeroAnotacion();
            }
            
            if((consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))){
                where += " AND EXR_EJR=" + consExpVO.getEjercicioAnotacion();
            }
               
            if (hayDatosRegistroExterno(consExpVO, uVO, oad, params)) {
                where = where + "UNION "
                        + "SELECT EXREXT_MUN,EXREXT_PRO,EXREXT_NUM FROM E_EXREXT WHERE "
                        + "EXREXT_NUM=EXP_NUM AND EXREXT_MUN=EXP_MUN AND EXP_PRO=EXREXT_PRO ";

                if (consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) {
                    where += " AND EXREXT_TIP='" + consExpVO.getCodTipoAnotacion() + "'";
                }

                if (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) {
                    where += " AND EXREXT_NRE=" + consExpVO.getNumeroAnotacion();
                }

                if ((consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
                    where += " AND EXREXT_EJR=" + consExpVO.getEjercicioAnotacion();
                }
            }
            where = where + ") AND ";
        }//if

        
        
        /***  SI SE FILTRA POR TIPO DE INICIO ****/
        if((consExpVO.getTipoInicio()!=null && !"".equals(consExpVO.getTipoInicio())) && "Instancia".equalsIgnoreCase(consExpVO.getTipoInicio())){            
            where += " EXP_NUM IN (SELECT EXR_NUM FROM E_EXR WHERE " +
                                      "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND " + 
                                      "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO AND EXR_TOP=0) AND ";                            
            
        }else
        if((consExpVO.getTipoInicio()!=null && !"".equals(consExpVO.getTipoInicio())) && "Oficio".equalsIgnoreCase(consExpVO.getTipoInicio())){            
            where += " EXP_NUM NOT IN (SELECT EXR_NUM FROM E_EXR WHERE " +
                                           "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND " + 
                                           "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO AND EXR_TOP=0) AND ";                                                    
        }// if    
        
        /***  SI SE FILTRA POR EXPEDIENTE INICIADO DESDE REGISTRO TELEMATICO ****/
        if((consExpVO.getBusqTelematicos()!=null && !"".equals(consExpVO.getBusqTelematicos())) && "1".equals(consExpVO.getBusqTelematicos())){ 
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                where += " EXP_NUM IN (SELECT EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            } else {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            }
        }
        
        where += " (PRO_TIN = 0 OR (PRO_TIN = 1 AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = TRA_UIN))";

        if (buscarInicioProc) {
                m_Log.debug("***  buscarInicioProc");
            where += " OR (exists (SELECT DISTINCT UOU_UOR " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI " +
                    "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " " +
                    "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))";
        }

        where += "))";

        String[] parametros = new String[2];
 m_Log.debug("*** ANTES  getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getCodProcedimiento() != null) {
             m_Log.debug("***  getCodProcedimiento"+consExpVO.getCodProcedimiento());
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
      m_Log.debug("*** DESPUES getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                parametros[0] = "'"+consExpVO.getNumeroExpediente().trim()+"'";
                Config m_ConfigExp = ConfigServiceHelper.getConfig("Expediente");
                Boolean usarMascaraBusquedaGeneral=false;
                try{
                    usarMascaraBusquedaGeneral=m_ConfigExp.getString(Integer.toString(uVO.getOrgCod())  + "/usarMascaraBusquedaGeneral").toUpperCase().equals("SI");
                   
                }catch(Exception e)
                {
                    usarMascaraBusquedaGeneral=false;
                }
                String condicion;
                if (usarMascaraBusquedaGeneral){
                    int  posAst = parametros[0].indexOf("*");
                    if (posAst != -1)
                        condicion = "EXP_NUM LIKE "+ parametros[0].replace("*", "%");
                    else
                        condicion = "EXP_NUM="+ parametros[0];
                }else
                    condicion = "EXP_NUM="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        if (consExpVO.getEjercicioExpediente() != null) {
            if (!"".equals(consExpVO.getEjercicioExpediente())) {
               
                where += " AND EXP_EJE=" + consExpVO.getEjercicioExpediente();
                
            }
            
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        
        
        // Recuperar expedientes 
        if (consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion())
                && consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion())
                && consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==1){
                
            String condicion = "CRO_FEI BETWEEN " + oad.convertir("'"+ consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()+"'",oad.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                    oad.convertir("'"+consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY");

            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }            
        }        
        
        if (consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion())
                && consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion())
                && consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==2){
            
            String condicion = "CRO_FEF BETWEEN " + oad.convertir("'"+ consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()+"'",oad.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                    oad.convertir("'"+consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY");
            
            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }            
        }
        
        if((consExpVO.getTiempo()!=null) &&(!"".equals(consExpVO.getTiempo()))&& consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==1)
        {
            String tiempo=consExpVO.getTiempo();
            String condicion="";

            if ("1".equals(tiempo))  condicion = "CRO_FEI  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -7) ";
            if ("2".equals(tiempo))  condicion = "CRO_FEI  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -30) ";
            if ("3".equals(tiempo))  condicion = "CRO_FEI  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -90) ";
           
            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }   
        }
        if((consExpVO.getTiempo()!=null) &&(!"".equals(consExpVO.getTiempo()))&& consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==2)
        {
            String tiempo=consExpVO.getTiempo();
            String condicion="";

            if ("1".equals(tiempo))  condicion = "CRO_FEF  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -7) ";
            if ("2".equals(tiempo))  condicion = "CRO_FEF  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -30) ";
            if ("3".equals(tiempo))  condicion = "CRO_FEF  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -90) ";
           
            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }   
        }
        
                
        
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) {
                    condicion = "0";
                    where+=" AND EXP_EST = 0 ";
                }
                else if ("cerrado".equals(consExpVO.getEstado())) {
                    //condicion = "9";
                    where += " AND (EXP_EST=9 )";
                }
                else if ("anulado".equals(consExpVO.getEstado())) {
                    //condicion = "1";
                    where += " AND (EXP_EST=1 )";
                }
            }
        }
        
        
        if (consExpVO.getTercero() != null) {
            
            if(!CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
                if (!"".equals(consExpVO.getTercero())) {
                    where += " AND EXT_TER = " + consExpVO.getTercero().trim();                
                }                 
            }else{
                // SE CONSULTA POR DOCUMENTO DEL TERCERO Y NO POR CÓDIGO DE TERCERO
                if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                   
                    where += " AND HTE_DOC='" + consExpVO.getDocumentoTercero()+ "'" ;
                }                
            }   
        }
        
        
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = "TRA_CLS="+consExpVO.getCodClasifTramite();
                if (!"".equals(condicion)) {
                    where += " AND " + condicion+" AND CRO_FEF IS NULL";
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }

        String condFechaFinTramite;
        if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = " AND CRO_FEF IS NULL";
        else condFechaFinTramite = " AND CRO_FEF IS NOT NULL";

        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
               
                    where += " AND CRO_UTR=" + consExpVO.getCodUnidadTram().trim();

            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_UTR = " + cUT;
                    else where += " OR CRO_UTR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += condFechaFinTramite + ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
              
                where += " AND CRO_TRA=" + consExpVO.getCodTramite().trim();

            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }
        
        
        // Si tenemos asunto
            if (consExpVO.getAsunto() != null) {
                if (!"".equals(consExpVO.getAsunto().trim())) {
                    boolean consultaInsensitiva=true;
                    String condicion = "";
                        condicion = transformador.construirCondicionWhereConOperadores("EXP_ASU",consExpVO.getAsunto().trim(), false);
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
        // Si tenemos observaciones
            if (consExpVO.getObservaciones() != null) {
                if (!"".equals(consExpVO.getObservaciones().trim())) {
                    boolean consultaInsensitiva=true;
                    String condicion = "";
                        condicion = transformador.construirCondicionWhereConOperadores("EXP_OBS",consExpVO.getObservaciones().trim(), false);
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
            
            
         
         if (consExpVO.getCodigoRol() != null) {
                if (!"".equals(consExpVO.getCodigoRol().trim())) {
                        where += " AND  EXT_ROL= "+ consExpVO.getCodigoRol() ;}
         }   


        // óscar
        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientes".equals(consExpVO.getTipo())){
              m_Log.debug("\n Consulta expedientes--> expedientesPendientes");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND CRO_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientesVolumen".equals(consExpVO.getTipo())){
            m_Log.debug("\n Consulta expedientes--> expedientesPendientesVolumen");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND CRO_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesHistoricos".equals(consExpVO.getTipo())){
              m_Log.debug("\n Consulta expedientes--> expedientesHistoricos");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
             where += " AND CRO_FEF IS NOT NULL";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesCerrados".equals(consExpVO.getTipo())){
              m_Log.debug("\n Consulta expedientes--> expedientesHistoricos");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL AND (EXP_EST=1 or EXP_EST=9)";
        }
        
        // óscar

        // Se tiene en cuenta los procedimientos restringidos. Sólo se recuperan los exp. de los procedimientos no restringidos o, en el caso de que estén restringidos, se
        // recuperan también siempre y cuando el usuario tenga permiso sobre el procedimiento
        where += " AND (PRO_RESTRINGIDO=0 OR (PRO_RESTRINGIDO=1 AND E_PRO.PRO_COD IN (SELECT PRO_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE USU_COD="  +
                 uVO.getIdUsuario() + " AND ORG_COD=" +  uVO.getOrgCod() + ")))";


        return oad.join(from, where, join.toArray(new String[]{}));
    }
    
    
    private String obtenerSentenciaConFiltroActivosHistorico(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO,boolean buscarInicioProc)
        throws BDException {

        m_Log.debug("eMPIEZO A obtenerSentenciaConFiltroUsuarioTramitadorHistorico");
        String queryActivos = obtenerSentenciaConFiltro(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
        String queryHistorico = obtenerSentenciaConFiltroHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
        String resultado = queryActivos + " UNION ALL " + queryHistorico;
        
        return resultado;
     }
     
private String obtenerSentenciaConFiltroHistorico(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO,  boolean buscarInicioProc)
        throws BDException {
        
        
        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }
        
        
        m_Log.debug("eMPIEZO A obtenerSentenciaConFiltroHistorico");
        String from = "  DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom, 'S' AS EXHISTORICO ";

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            from += ", DNN_RCA," + GlobalNames.ESQUEMA_GENERICO + "A_USU,";
        }

        ArrayList<String> join = new ArrayList<String>();

        join.add("HIST_E_EXP");
        join.add("LEFT");
        join.add("HIST_E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
        join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");

        if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
            join.add("LEFT");
            join.add("HIST_E_EXT");
            join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");
            
            /*** SE BUSCAN LOS EXPEDIENTES QUE TIENEN COMO INTERESADO ALGÚN TERCERO CON UN DETERMINADO DOCUMENTO ***/
            if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                join.add("LEFT");
                join.add("T_HTE");
                join.add("EXT_TER = T_HTE.HTE_TER AND EXT_NVR = T_HTE.HTE_NVR");                
            }            
            
        }else{ //NO hay titular pero hay Rol 
       
            if(consExpVO.getCodigoRol()!=null && !"".equals(consExpVO.getCodigoRol().trim())) {  
              join.add("INNER");   
              join.add("HIST_E_EXT");
              join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");            
           }
        }

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }
  
        join.add("false");
        String where = "PML_CMP = 'NOM' AND PML_LENG = '"+m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";        
        
        
        if ((consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) ||
            (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) ||
            (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
            
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                 where += " EXP_NUM IN (SELECT EXR_NUM FROM HIST_E_EXR WHERE " +
                                  "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND " + 
                                  "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO ";
            }else
            {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM HIST_E_EXR WHERE " +
                                  "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND " + 
                                  "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO ";
            }
           
            
            if(consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())){
                where += " AND EXR_TIP='" + consExpVO.getCodTipoAnotacion() + "'";
            }
            
            if(consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())){
                where+= " AND EXR_NRE=" + consExpVO.getNumeroAnotacion();
            }
            
            if((consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))){
                where += " AND EXR_EJR=" + consExpVO.getEjercicioAnotacion();
            }
               
            if (hayDatosRegistroExterno(consExpVO, uVO, oad, params)) {
                where = where + "UNION "
                        + "SELECT EXREXT_MUN,EXREXT_PRO,EXREXT_NUM FROM HIST_E_EXREXT WHERE "
                        + "EXREXT_NUM=EXP_NUM AND EXREXT_MUN=EXP_MUN AND EXP_PRO=EXREXT_PRO ";

                if (consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) {
                    where += " AND EXREXT_TIP='" + consExpVO.getCodTipoAnotacion() + "'";
                }

                if (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) {
                    where += " AND EXREXT_NRE=" + consExpVO.getNumeroAnotacion();
                }

                if ((consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
                    where += " AND EXREXT_EJR=" + consExpVO.getEjercicioAnotacion();
                }
            }
            where = where + ") AND ";
        }//if

        
        
        /***  SI SE FILTRA POR TIPO DE INICIO ****/
        if((consExpVO.getTipoInicio()!=null && !"".equals(consExpVO.getTipoInicio())) && "Instancia".equalsIgnoreCase(consExpVO.getTipoInicio())){            
            where += " EXP_NUM IN (SELECT EXR_NUM FROM HIST_E_EXR WHERE " +
                                      "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND " + 
                                      "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO AND EXR_TOP=0) AND ";                            
            
        }else
        if((consExpVO.getTipoInicio()!=null && !"".equals(consExpVO.getTipoInicio())) && "Oficio".equalsIgnoreCase(consExpVO.getTipoInicio())){            
            where += " EXP_NUM NOT IN (SELECT EXR_NUM FROM HIST_E_EXR WHERE " +
                                           "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND " + 
                                           "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO AND EXR_TOP=0) AND ";                                                    
        }// if    
        
        /***  SI SE FILTRA POR EXPEDIENTE INICIADO DESDE REGISTRO TELEMATICO ****/
        if((consExpVO.getBusqTelematicos()!=null && !"".equals(consExpVO.getBusqTelematicos())) && "1".equals(consExpVO.getBusqTelematicos())){ 
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                where += " EXP_NUM IN (SELECT EXR_NUM FROM HIST_E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            } else {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM HIST_E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            }
        }
        
        where += " (PRO_TIN = 0 OR (PRO_TIN = 1 AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = TRA_UIN))";

        if (buscarInicioProc) {
                m_Log.debug("***  buscarInicioProc");
            where += " OR (exists (SELECT DISTINCT UOU_UOR " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI " +
                    "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " " +
                    "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))";
        }

        where += "))";

        String[] parametros = new String[2];
 m_Log.debug("*** ANTES  getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getCodProcedimiento() != null) {
             m_Log.debug("***  getCodProcedimiento"+consExpVO.getCodProcedimiento());
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
      m_Log.debug("*** DESPUES getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                parametros[0] = "'"+consExpVO.getNumeroExpediente().trim()+"'";
                Config m_ConfigExp = ConfigServiceHelper.getConfig("Expediente");
                Boolean usarMascaraBusquedaGeneral=false;
                try{
                    usarMascaraBusquedaGeneral=m_ConfigExp.getString(Integer.toString(uVO.getOrgCod())  + "/usarMascaraBusquedaGeneral").toUpperCase().equals("SI");
                   
                }catch(Exception e)
                {
                    usarMascaraBusquedaGeneral=false;
                }
                String condicion;
                if (usarMascaraBusquedaGeneral){
                    int  posAst = parametros[0].indexOf("*");
                    if (posAst != -1)
                        condicion = "EXP_NUM LIKE "+ parametros[0].replace("*", "%");
                    else
                        condicion = "EXP_NUM="+ parametros[0];
                }else
                    condicion = "EXP_NUM="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        if (consExpVO.getEjercicioExpediente() != null) {
            if (!"".equals(consExpVO.getEjercicioExpediente())) {
               
                where += " AND EXP_EJE=" + consExpVO.getEjercicioExpediente();
                
            }
            
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                } 
            }
        }
        
        
        
        // Recuperar expedientes 
        if (consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion())
                && consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion())
                && consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==1){
                
            String condicion = "CRO_FEI BETWEEN " + oad.convertir("'"+ consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()+"'",oad.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                    oad.convertir("'"+consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY");

            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }            
        }        
        
        if (consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion())
                && consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion())
                && consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==2){
            
            String condicion = "CRO_FEF BETWEEN " + oad.convertir("'"+ consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()+"'",oad.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                    oad.convertir("'"+consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY");
            
            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }            
        }
        
         if((consExpVO.getTiempo()!=null) &&(!"".equals(consExpVO.getTiempo()))&& consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==1)
        {
            String tiempo=consExpVO.getTiempo();
            String condicion="";

            if ("1".equals(tiempo))  condicion = "CRO_FEI  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -7) ";
            if ("2".equals(tiempo))  condicion = "CRO_FEI  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -30) ";
            if ("3".equals(tiempo))  condicion = "CRO_FEI  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -90) ";
           
            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }   
        }
        if((consExpVO.getTiempo()!=null) &&(!"".equals(consExpVO.getTiempo()))&& consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==2)
        {
            String tiempo=consExpVO.getTiempo();
            String condicion="";

            if ("1".equals(tiempo))  condicion = "CRO_FEF  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -7) ";
            if ("2".equals(tiempo))  condicion = "CRO_FEF  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -30) ";
            if ("3".equals(tiempo))  condicion = "CRO_FEF  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -90) ";
           
            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }   
        }
                
        
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) {
                    condicion = "0";
                    where+=" AND EXP_EST = 0 ";
                }
                else if ("cerrado".equals(consExpVO.getEstado())) {
                    //condicion = "9";
                    where += " AND (EXP_EST=9 )";
                }
                else if ("anulado".equals(consExpVO.getEstado())) {
                    //condicion = "1";
                    where += " AND (EXP_EST=1 )";
                }
            }
        }
        
        
        if (consExpVO.getTercero() != null) {
            
            if(!CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
                if (!"".equals(consExpVO.getTercero())) {
                    where += " AND EXT_TER = " + consExpVO.getTercero().trim();                
                }                 
            }else{
                // SE CONSULTA POR DOCUMENTO DEL TERCERO Y NO POR CÓDIGO DE TERCERO
                if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                   
                    where += " AND HTE_DOC='" + consExpVO.getDocumentoTercero()+ "'" ;
                }                
            }   
        }
        
        
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = "TRA_CLS="+consExpVO.getCodClasifTramite();
                if (!"".equals(condicion)) {
                    where += " AND " + condicion+" AND CRO_FEF IS NULL";
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }

        String condFechaFinTramite;
        if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = " AND CRO_FEF IS NULL";
        else condFechaFinTramite = " AND CRO_FEF IS NOT NULL";

        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
               
                    where += " AND CRO_UTR=" + consExpVO.getCodUnidadTram().trim();

            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_UTR = " + cUT;
                    else where += " OR CRO_UTR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += condFechaFinTramite + ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
              
                where += " AND CRO_TRA=" + consExpVO.getCodTramite().trim();

            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }
        
        
        // Si tenemos asunto
            if (consExpVO.getAsunto() != null) {
                if (!"".equals(consExpVO.getAsunto().trim())) {
                    boolean consultaInsensitiva=true;
                    String condicion = "";
                        condicion = transformador.construirCondicionWhereConOperadores("EXP_ASU",consExpVO.getAsunto().trim(), false);
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
        // Si tenemos observaciones
            if (consExpVO.getObservaciones() != null) {
                if (!"".equals(consExpVO.getObservaciones().trim())) {
                    boolean consultaInsensitiva=true;
                    String condicion = "";
                        condicion = transformador.construirCondicionWhereConOperadores("EXP_OBS",consExpVO.getObservaciones().trim(), false);
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
            
            
         
         if (consExpVO.getCodigoRol() != null) {
                if (!"".equals(consExpVO.getCodigoRol().trim())) {
                        where += " AND  EXT_ROL= "+ consExpVO.getCodigoRol() ;}
         }   


        // óscar
        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientes".equals(consExpVO.getTipo())){
              m_Log.debug("\n Consulta expedientes--> expedientesPendientes");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND CRO_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientesVolumen".equals(consExpVO.getTipo())){
            m_Log.debug("\n Consulta expedientes--> expedientesPendientesVolumen");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND CRO_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesHistoricos".equals(consExpVO.getTipo())){
              m_Log.debug("\n Consulta expedientes--> expedientesHistoricos");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
             where += " AND CRO_FEF IS NOT NULL";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesCerrados".equals(consExpVO.getTipo())){
              m_Log.debug("\n Consulta expedientes--> expedientesHistoricos");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL AND (EXP_EST=1 or EXP_EST=9)";
        }
        
        // óscar

        // Se tiene en cuenta los procedimientos restringidos. Sólo se recuperan los exp. de los procedimientos no restringidos o, en el caso de que estén restringidos, se
        // recuperan también siempre y cuando el usuario tenga permiso sobre el procedimiento
        where += " AND (PRO_RESTRINGIDO=0 OR (PRO_RESTRINGIDO=1 AND E_PRO.PRO_COD IN (SELECT PRO_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE USU_COD="  +
                 uVO.getIdUsuario() + " AND ORG_COD=" +  uVO.getOrgCod() + ")))";


        return oad.join(from, where, join.toArray(new String[]{}));
    }
    
  private String obtenerSentenciaConFiltroImprimir(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO, boolean buscarInicioProc)
        throws BDException {
        
        
        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }
        
        
        m_Log.debug("eMPIEZO A obtenerSentenciaConFiltroImprimir");
        String from = "  DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom ";


        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            from += ", DNN_RCA," + GlobalNames.ESQUEMA_GENERICO + "A_USU,";
        }

        ArrayList<String> join = new ArrayList<String>();

        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
        join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");        
        join.add("LEFT");
        join.add("E_EXT");
        join.add("EXP_NUM=EXT_NUM"); 
        join.add("LEFT");
        join.add("T_HTE");
        
        if(consExpVO.getCodigoRol()!=null && !"".equals(consExpVO.getCodigoRol().trim())) { 
             join.add("EXT_TER =HTE_TER and EXT_NVR = HTE_NVR ");           
        }
        else  if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
             join.add("EXT_TER =HTE_TER and EXT_NVR = HTE_NVR ");
        }else
        {
             join.add("EXT_TER =HTE_TER and EXT_NVR = HTE_NVR AND 1=MOSTRAR");
        }
        
               
      
  
        if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
            join.add("LEFT");
            join.add("E_EXT");
            join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");
            
            /*** SE BUSCAN LOS EXPEDIENTES QUE TIENEN COMO INTERESADO ALGÚN TERCERO CON UN DETERMINADO DOCUMENTO ***/
            if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                join.add("LEFT");
                join.add("T_HTE");
                join.add("EXT_TER = T_HTE.HTE_TER AND EXT_NVR = T_HTE.HTE_NVR");                
            }            
            
        }else{ //NO hay titular pero hay Rol 
       
            if(consExpVO.getCodigoRol()!=null && !"".equals(consExpVO.getCodigoRol().trim())) {  
              join.add("INNER");   
              join.add("E_EXT");
              join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");            
           }
        }

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }
   
        join.add("false");
        m_Log.debug("consExpVO.getTercero() ==  "+consExpVO.getTercero());
         String where=
        " (EXT_ROL IN (select ROL_COD from E_ROL where ROL_MUN = "+ uVO.getOrgCod()+" and ROL_PRO = EXP_PRO) or EXT_ROL is null)";
         
        where= where + " and PML_CMP = 'NOM' AND PML_LENG = '"+m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";        
       
        if ((consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) ||
            (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) ||
            (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
         
             if (params[0] != null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))) {
                where += " EXP_NUM IN (EXR_NUM FROM E_EXR WHERE "
                        + "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND "
                        + "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO ";
            } else {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM E_EXR WHERE "
                        + "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND "
                        + "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO ";
            }
            
            if(consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())){
                where += " AND EXR_TIP='" + consExpVO.getCodTipoAnotacion() + "'";
            }
            
            if(consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())){
                where+= " AND EXR_NRE=" + consExpVO.getNumeroAnotacion();
            }
            
            if((consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))){
                where += " AND EXR_EJR=" + consExpVO.getEjercicioAnotacion();
            }
              
            if (hayDatosRegistroExterno(consExpVO,uVO, oad, params)){
                where = where + "UNION " +
                                 "SELECT EXREXT_MUN,EXREXT_PRO,EXREXT_NUM FROM E_EXREXT WHERE " +
                                 "EXREXT_NUM=EXP_NUM AND EXREXT_MUN=EXP_MUN AND EXP_PRO=EXREXT_PRO ";

                if(consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())){
                    where += " AND EXREXT_TIP='" + consExpVO.getCodTipoAnotacion() + "'";
                }

                if(consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())){
                    where+= " AND EXREXT_NRE=" + consExpVO.getNumeroAnotacion();
                }

                if((consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))){
                    where += " AND EXREXT_EJR=" + consExpVO.getEjercicioAnotacion();
                }
            }
            where = where + ") AND ";
        }//if

        
        
        /***  SI SE FILTRA POR TIPO DE INICIO ****/
        if((consExpVO.getTipoInicio()!=null && !"".equals(consExpVO.getTipoInicio())) && "Instancia".equalsIgnoreCase(consExpVO.getTipoInicio())){            
            where += " EXP_NUM IN (SELECT EXR_NUM FROM E_EXR WHERE " +
                                      "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND " + 
                                      "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO AND EXR_TOP=0) AND ";                            
            
        }else
        if((consExpVO.getTipoInicio()!=null && !"".equals(consExpVO.getTipoInicio())) && "Oficio".equalsIgnoreCase(consExpVO.getTipoInicio())){            
            where += " EXP_NUM NOT IN (SELECT EXR_NUM FROM E_EXR WHERE " +
                                           "EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND " + 
                                           "EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO AND EXR_TOP=0) AND ";                                                    
        }// if    
        
        /***  SI SE FILTRA POR EXPEDIENTE INICIADO DESDE REGISTRO TELEMATICO ****/
        if((consExpVO.getBusqTelematicos()!=null && !"".equals(consExpVO.getBusqTelematicos())) && "1".equals(consExpVO.getBusqTelematicos())){ 
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                where += " EXP_NUM IN (SELECT EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            } else {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            }
        }
        
        where += " (PRO_TIN = 0 OR (PRO_TIN = 1 AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = TRA_UIN))";

        if (buscarInicioProc) {
                m_Log.debug("***  buscarInicioProc");
            where += " OR (exists (SELECT DISTINCT UOU_UOR " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI " +
                    "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " " +
                    "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))";
        }

        where += "))";

        String[] parametros = new String[2];
 m_Log.debug("*** ANTES  getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getCodProcedimiento() != null) {
             m_Log.debug("***  getCodProcedimiento"+consExpVO.getCodProcedimiento());
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
      m_Log.debug("*** DESPUES getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                parametros[0] = "'"+consExpVO.getNumeroExpediente().trim()+"'";
                String condicion = "EXP_NUM="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        if (consExpVO.getEjercicioExpediente() != null) {
            if (!"".equals(consExpVO.getEjercicioExpediente())) {
               
                where += " AND EXP_EJE=" + consExpVO.getEjercicioExpediente();
                
            }
            
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        
        
        // Recuperar expedientes 
        if (consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion())
                && consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion())
                && consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==1){
                
            String condicion = "CRO_FEI BETWEEN " + oad.convertir("'"+ consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()+"'",oad.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                    oad.convertir("'"+consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY");

            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }            
        }        
        
        if (consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion())
                && consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()!= null && !"".equals(consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion())
                && consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==2){
            
            String condicion = "CRO_FEF BETWEEN " + oad.convertir("'"+ consExpVO.getFechaInicioTramiteBusquedaDesdeInformesGestion()+"'",oad.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY")+ " AND " +
                    oad.convertir("'"+consExpVO.getFechaFinTramiteBusquedaDesdeInformesGestion()+"'",AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY");
            
            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }            
        }
        
         if((consExpVO.getTiempo()!=null) &&(!"".equals(consExpVO.getTiempo()))&& consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==1)
        {
            String tiempo=consExpVO.getTiempo();
            String condicion="";

            if ("1".equals(tiempo))  condicion = "CRO_FEI  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -7) ";
            if ("2".equals(tiempo))  condicion = "CRO_FEI  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -30) ";
            if ("3".equals(tiempo))  condicion = "CRO_FEI  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -90) ";
           
            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }   
        }
        if((consExpVO.getTiempo()!=null) &&(!"".equals(consExpVO.getTiempo()))&& consExpVO.isDesdeInformesGestion() && consExpVO.getEstadoTramiteBusquedaDesdeInformesGestion()==2)
        {
            String tiempo=consExpVO.getTiempo();
            String condicion="";

            if ("1".equals(tiempo))  condicion = "CRO_FEF  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -7) ";
            if ("2".equals(tiempo))  condicion = "CRO_FEF  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -30) ";
            if ("3".equals(tiempo))  condicion = "CRO_FEF  > ( "+oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+" -90) ";
           
            if (!"".equals(condicion)) {
                where += " AND (" + condicion + ")";
            }   
        }
                
        
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) {
                    condicion = "0";
                    where+=" AND EXP_EST = 0 ";
                }
                else if ("cerrado".equals(consExpVO.getEstado())) {
                    //condicion = "9";
                    where += " AND (EXP_EST=9 )";
                }
                else if ("anulado".equals(consExpVO.getEstado())) {
                    //condicion = "1";
                    where += " AND (EXP_EST=1 )";
                }
            }
        }
        
        
        if (consExpVO.getTercero() != null) {
            
            if(!CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
                if (!"".equals(consExpVO.getTercero())) {
                    where += " AND EXT_TER = " + consExpVO.getTercero().trim();                
                }                 
            }else{
                // SE CONSULTA POR DOCUMENTO DEL TERCERO Y NO POR CÓDIGO DE TERCERO
                if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                   
                    where += " AND HTE_DOC='" + consExpVO.getDocumentoTercero()+ "'" ;
                }                
            }   
        }
        
        
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = "TRA_CLS="+consExpVO.getCodClasifTramite();
                if (!"".equals(condicion)) {
                    where += " AND " + condicion+" AND CRO_FEF IS NULL";
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }

        String condFechaFinTramite;
        if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = " AND CRO_FEF IS NULL";
        else condFechaFinTramite = " AND CRO_FEF IS NOT NULL";

        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
               
                    where += " AND CRO_UTR=" + consExpVO.getCodUnidadTram().trim();

            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_UTR = " + cUT;
                    else where += " OR CRO_UTR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += condFechaFinTramite + ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
              
                where += " AND CRO_TRA=" + consExpVO.getCodTramite().trim();

            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }
        
        
        // Si tenemos asunto
            if (consExpVO.getAsunto() != null) {
                if (!"".equals(consExpVO.getAsunto().trim())) {
                    boolean consultaInsensitiva=true;
                    String condicion = "";
                        condicion = transformador.construirCondicionWhereConOperadores("EXP_ASU",consExpVO.getAsunto().trim(), false);
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
        // Si tenemos observaciones
            if (consExpVO.getObservaciones() != null) {
                if (!"".equals(consExpVO.getObservaciones().trim())) {
                    boolean consultaInsensitiva=true;
                    String condicion = "";
                        condicion = transformador.construirCondicionWhereConOperadores("EXP_OBS",consExpVO.getObservaciones().trim(), false);
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
            
            
         
         if (consExpVO.getCodigoRol() != null) {
                if (!"".equals(consExpVO.getCodigoRol().trim())) {
                        where += " AND  EXT_ROL= "+ consExpVO.getCodigoRol() ;}
         }   


        // óscar
        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientes".equals(consExpVO.getTipo())){
              m_Log.debug("\n Consulta expedientes--> expedientesPendientes");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND CRO_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientesVolumen".equals(consExpVO.getTipo())){
            m_Log.debug("\n Consulta expedientes--> expedientesPendientesVolumen");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND CRO_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesHistoricos".equals(consExpVO.getTipo())){
              m_Log.debug("\n Consulta expedientes--> expedientesHistoricos");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
             where += " AND CRO_FEF IS NOT NULL";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesCerrados".equals(consExpVO.getTipo())){
              m_Log.debug("\n Consulta expedientes--> expedientesHistoricos");
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL AND (EXP_EST=1 or EXP_EST=9)";
        }
        
        // óscar

        // Se tiene en cuenta los procedimientos restringidos. Sólo se recuperan los exp. de los procedimientos no restringidos o, en el caso de que estén restringidos, se
        // recuperan también siempre y cuando el usuario tenga permiso sobre el procedimiento
        where += " AND (PRO_RESTRINGIDO=0 OR (PRO_RESTRINGIDO=1 AND E_PRO.PRO_COD IN (SELECT PRO_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE USU_COD="  +
                 uVO.getIdUsuario() + " AND ORG_COD=" +  uVO.getOrgCod() + ")))";


        return oad.join(from, where, join.toArray(new String[]{}));
    }
    
    
    
    
    private String obtenerSentenciaConFiltroByInteresado(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO, boolean buscarInicioProc,String tipoOrden)
            throws BDException {

        m_Log.debug("eMPIEZO A obtenerSentenciaConFiltroInteresado");
        
        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }
        
        String from = "  DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
               "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom, 'N' AS EXHISTORICO";

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            from += ", DNN_RCA," + GlobalNames.ESQUEMA_GENERICO + "A_USU,";
        }

        /* ORIGINAL
        from=from+", "+"T_HTE.HTE_TER, " +
                 oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1", "T_HTE.HTE_PA2",  "T_HTE.HTE_AP2",  "T_HTE.HTE_NOM"})+ " AS NOMBRE";
        */
        
        
        // #225966: ordena por interesado sin duplicar expedientes
        String funcionAgregado;
        if(tipoOrden.equals("DESC")) funcionAgregado = "MAX"; 
        else funcionAgregado = "MIN"; 
        
        
        StringBuilder campoNombre = new StringBuilder("SELECT ");
        campoNombre.append(funcionAgregado).append("( UPPER(")
                .append(oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1", "T_HTE.HTE_PA2",  "T_HTE.HTE_AP2",  "T_HTE.HTE_NOM"}))
                .append(")) FROM E_EXT TER,T_HTE WHERE EXP_MUN = TER.EXT_MUN AND EXP_EJE = TER.EXT_EJE AND EXP_NUM = TER.EXT_NUM")
                .append(" AND TER.EXT_TER=T_HTE.HTE_TER AND TER.EXT_NVR=T_HTE.HTE_NVR");
        
        from += ", (" + campoNombre.toString() + ") AS NOMBRE";

        ArrayList<String> join = new ArrayList<String>();
        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
         join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
        join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");

       
        /* ORIGINAL
        join.add("LEFT");
        join.add("E_EXT TER");
        join.add("EXP_MUN = TER.EXT_MUN AND EXP_EJE = TER.EXT_EJE AND EXP_NUM = TER.EXT_NUM");            
       */
        // #225966: SOLO HACE EL JOIN CON LAS TABLAS E_EXT Y E_THE SI SE FILTRA POR TITUTAR (POR E_EXT TAMBIÉN SI SE FILTRA POR ROL)
        if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
            join.add("LEFT");
            join.add("E_EXT");
            join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");
            
            /*** SE BUSCAN LOS EXPEDIENTES QUE TIENEN COMO INTERESADO ALGÚN TERCERO CON UN DETERMINADO DOCUMENTO ***/
            if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                join.add("LEFT");
                join.add("T_HTE");
                join.add("EXT_TER = T_HTE.HTE_TER AND EXT_NVR = T_HTE.HTE_NVR");                
            }            
            
        }else{ //NO hay titular pero hay Rol 
       
            if(consExpVO.getCodigoRol()!=null && !"".equals(consExpVO.getCodigoRol().trim())) {  
              join.add("INNER");   
              join.add("E_EXT");
              join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");            
           }
        }            
       

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }

        // Caso de que tengamos ejercicio, numero o tipo de anotacion.
        if ((consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) ||
            (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) ||
            (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
            join.add("LEFT");
            join.add("E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");
            join.add("LEFT");
            join.add("E_EXREXT");
            join.add("EXP_MUN = EXREXT_MUN AND EXP_NUM = EXREXT_NUM");
        }

        //Los joins para ordenar por interesado
        /** ORIGINAL
         join.add("LEFT");
         join.add("E_EXT A");
         join.add("E_EXP.EXP_EJE=A.EXT_EJE AND E_EXP.EXP_MUN=A.EXT_MUN AND E_EXP.EXP_NUM=A.EXT_NUM");
         join.add("LEFT");
         join.add("T_HTE");
         join.add("A.EXT_TER=T_HTE.HTE_TER AND A.EXT_NVR=T_HTE.HTE_NVR");
         ***/
        
        /*** OSCAR ***/
        //join.add("LEFT");
        //join.add("E_EXT A");
        //join.add("E_EXP.EXP_EJE=A.EXT_EJE AND E_EXP.EXP_MUN=A.EXT_MUN AND E_EXP.EXP_NUM=A.EXT_NUM");
        /*
        join.add("LEFT");
        join.add("T_HTE");
        join.add("TER.EXT_TER=T_HTE.HTE_TER AND TER.EXT_NVR=T_HTE.HTE_NVR");
        */
        /*** OSCAR ***/

        join.add("false");

        /** original
        String where = "(A.EXT_TER IS NULL OR A.EXT_TER=(SELECT MIN(EXT_TER) FROM E_EXT WHERE EXT_NUM=EXP_NUM AND MOSTRAR=1)) AND PML_CMP = 'NOM' AND PML_LENG = '"+
                m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";
                ***/
        /** OSCAR **/
        String where = " PML_CMP = 'NOM' AND PML_LENG = '"+
                m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";
        /** OSCAR **/
        
        /***  SI SE FILTRA POR EXPEDIENTE INICIADO DESDE REGISTRO TELEMATICO ****/
        if((consExpVO.getBusqTelematicos()!=null && !"".equals(consExpVO.getBusqTelematicos())) && "1".equals(consExpVO.getBusqTelematicos())){ 
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                where += " EXP_NUM IN (SELECT EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            } else {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            }
        }
        
        where += "(PRO_TIN = 0 OR (PRO_TIN = 1 AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = TRA_UIN))"+ " ";
                

        if (buscarInicioProc) {
                m_Log.debug("***  buscarInicioProc");
            where += " OR (exists (SELECT DISTINCT UOU_UOR " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI " +
                    "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " " +
                    "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))";
        }

        where += "))";

        String[] parametros = new String[2];
 m_Log.debug("*** ANTES  getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getCodProcedimiento() != null) {
             m_Log.debug("***  getCodProcedimiento"+consExpVO.getCodProcedimiento());
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
      m_Log.debug("*** DESPUES getCodProcedimiento"+consExpVO.getCodProcedimiento());
       if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                parametros[0] = "'" + consExpVO.getNumeroExpediente().trim() + "'";
                Config m_ConfigExp = ConfigServiceHelper.getConfig("Expediente");
                Boolean usarMascaraBusquedaGeneral = false;
                try {
                    usarMascaraBusquedaGeneral=m_ConfigExp.getString(Integer.toString(uVO.getOrgCod())  + "/usarMascaraBusquedaGeneral").toUpperCase().equals("SI");
                   
                } catch (Exception e) {
                    usarMascaraBusquedaGeneral = false;
                }
                String condicion;
                if (usarMascaraBusquedaGeneral){
                    int  posAst = parametros[0].indexOf("*");
                    if (posAst != -1)
                        condicion = "EXP_NUM LIKE "+ parametros[0].replace("*", "%");
                    else
                        condicion = "EXP_NUM="+ parametros[0];
                }else
                    condicion = "EXP_NUM="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        if (consExpVO.getEjercicioExpediente() != null) {
            if (!"".equals(consExpVO.getEjercicioExpediente())) {
               
                where += " AND EXP_EJE=" + consExpVO.getEjercicioExpediente();
                
            }
            
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) {
                    condicion = "0";
                    where+=" AND EXP_EST = 0 ";
                }
                else if ("cerrado".equals(consExpVO.getEstado())) {
                    //condicion = "9";
                    where += " AND (EXP_EST=9 OR EXP_EST=1 )";

                }

            }
        }
        
        
          /*** SE BUSCAN LOS EXPEDIENTES QUE TIENEN COMO INTERESADO ALGÚN TERCERO CON UN DETERMINADO DOCUMENTO ***/
        if(!CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
            if (!"".equals(consExpVO.getTitular())) {
                where += " AND TER.EXT_TER = " + consExpVO.getTercero().trim();
            }
        }else
        if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){           
            where += " AND T_HTE.HTE_TID=" + consExpVO.getTipoDocumentoTercero() + " AND T_HTE.HTE_DOC='" + consExpVO.getDocumentoTercero() + "'";
            
        }    
        
        
        /*** ORIGINAL
        if (consExpVO.getTitular() != null) {
            if (!"".equals(consExpVO.getTitular())) {
                where += " AND TER.EXT_TER = " + consExpVO.getTercero().trim();
            }
        }
        */
            
            
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = "TRA_CLS="+consExpVO.getCodClasifTramite();
                if (!"".equals(condicion)) {
                    where += " AND " + condicion+" AND CRO_FEF IS NULL";
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }

        String condFechaFinTramite;
        if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = " AND CRO_FEF IS NULL";
        else condFechaFinTramite = " AND CRO_FEF IS NOT NULL";

        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
              
                    where += " AND CRO_UTR=" + consExpVO.getCodUnidadTram().trim();

            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_UTR = " + cUT;
                    else where += " OR CRO_UTR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += condFechaFinTramite + ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
             
                where += " AND CRO_TRA=" + consExpVO.getCodTramite().trim();

            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }


        // Si tenemos ejercicio de anotacion
        if (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion())) {
            where += " AND (EXR_EJR = " + consExpVO.getEjercicioAnotacion().trim() +
                       " OR EXREXT_EJR = " + consExpVO.getEjercicioAnotacion().trim() + ")";
        }

        // Si tenemos numero de anotacion
        if (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) {
           where += " AND (EXR_NRE = " + consExpVO.getNumeroAnotacion().trim() +
                      " OR EXREXT_NRE = " + consExpVO.getNumeroAnotacion().trim() + ")";
        }

        // Si tenemos tipo de anotacion ('E' o 'S')
        if (consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) {
            where += " AND (EXR_TIP = '" + consExpVO.getCodTipoAnotacion().trim() +
                      "' OR EXREXT_TIP = '" + consExpVO.getCodTipoAnotacion().trim() + "')";
        }

        // Si tenemos asunto
            if (consExpVO.getAsunto() != null) {
                if (!"".equals(consExpVO.getAsunto().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_ASU COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getAsunto().trim() + "%')";
                    else condicion="((EXP_ASU LIKE '%"+consExpVO.getAsunto().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
        // Si tenemos observaciones
            if (consExpVO.getObservaciones() != null) {
                if (!"".equals(consExpVO.getObservaciones().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_OBS COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getObservaciones().trim() + "%')";
                    else condicion="((EXP_OBS LIKE '%"+consExpVO.getObservaciones().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
            
        // #225966: al filtrar por rol y ordenar por interesado se perdía el filtro por rol
        if (consExpVO.getCodigoRol() != null) {
            if (!"".equals(consExpVO.getCodigoRol().trim())) {
                where += " AND  EXT_ROL= "+ consExpVO.getCodigoRol() ;}
        }

        // óscar
        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientes".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND CRO_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientesVolumen".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND EXP_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesHistoricos".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesCerrados".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL AND (EXP_EST=1 or EXP_EST=9)";
        }

        // óscar

        // Se tiene en cuenta los procedimientos restringidos. Sólo se recuperan los exp. de los procedimientos no restringidos o, en el caso de que estén restringidos, se
        // recuperan también siempre y cuando el usuario tenga permiso sobre el procedimiento
        where += " AND (PRO_RESTRINGIDO=0 OR (PRO_RESTRINGIDO=1 AND E_PRO.PRO_COD IN (SELECT PRO_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE USU_COD="  +
                 uVO.getIdUsuario() + " AND ORG_COD=" +  uVO.getOrgCod() + ")))";
        
        return oad.join(from, where, join.toArray(new String[]{}));
    }
    
    
     private String obtenerSentenciaConFiltroByInteresadoActivosHistorico(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO, boolean buscarInicioProc, String tipoOrden)
            throws BDException {

        m_Log.debug("eMPIEZO A obtenerSentenciaConFiltroUsuarioTramitadorHistorico");
        String queryActivos = obtenerSentenciaConFiltroByInteresado(consExpVO, oad, transformador, params, uVO, buscarInicioProc, tipoOrden);
        String queryHistorico = obtenerSentenciaConFiltroByInteresadoHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc, tipoOrden);
        String resultado = queryActivos + " UNION ALL " + queryHistorico;
        
        return resultado;
     }
     
    private String obtenerSentenciaConFiltroByInteresadoHistorico(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO, boolean buscarInicioProc, String tipoOrden)
            throws BDException {

        m_Log.debug("eMPIEZO A obtenerSentenciaConFiltroInteresadoHistorico");
        
        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }
        
        String from = "  DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom, 'S' AS EXHISTORICO";

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            from += ", DNN_RCA," + GlobalNames.ESQUEMA_GENERICO + "A_USU,";
        }

        /* ORIGINAL
        from=from+", "+"T_HTE.HTE_TER, " +
                 oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1", "T_HTE.HTE_PA2",  "T_HTE.HTE_AP2",  "T_HTE.HTE_NOM"})+ " AS NOMBRE";
        */
        
        // #225966: ordena por interesado sin duplicar expedientes
        String funcionAgregado;
        if(tipoOrden.equals("DESC")) funcionAgregado = "MAX"; 
        else funcionAgregado = "MIN"; 
        
        
        StringBuilder campoNombre = new StringBuilder("SELECT ");
        campoNombre.append(funcionAgregado).append("( UPPER(")
                .append(oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1", "T_HTE.HTE_PA2",  "T_HTE.HTE_AP2",  "T_HTE.HTE_NOM"}))
                .append(")) FROM HIST_E_EXT TER,T_HTE WHERE EXP_MUN = TER.EXT_MUN AND EXP_EJE = TER.EXT_EJE AND EXP_NUM = TER.EXT_NUM")
                .append(" AND TER.EXT_TER=T_HTE.HTE_TER AND TER.EXT_NVR=T_HTE.HTE_NVR");
        
        from += ", (" + campoNombre.toString() + ") AS NOMBRE";

        ArrayList<String> join = new ArrayList<String>();
        join.add("HIST_E_EXP");
        join.add("LEFT");
        join.add("HIST_E_CRO");
         join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
        join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");

       
        /* ORIGINAL
        join.add("LEFT");
        join.add("HIST_E_EXT TER");
        join.add("EXP_MUN = TER.EXT_MUN AND EXP_EJE = TER.EXT_EJE AND EXP_NUM = TER.EXT_NUM");            
        */
        // #225966: SOLO HACE EL JOIN CON LAS TABLAS E_EXT Y E_THE SI SE FILTRA POR TITUTAR (POR E_EXT TAMBIÉN SI SE FILTRA POR ROL)
        if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
            join.add("LEFT");
            join.add("HIST_E_EXT");
            join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");
            
            /*** SE BUSCAN LOS EXPEDIENTES QUE TIENEN COMO INTERESADO ALGÚN TERCERO CON UN DETERMINADO DOCUMENTO ***/
            if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                join.add("LEFT");
                join.add("T_HTE");
                join.add("EXT_TER = T_HTE.HTE_TER AND EXT_NVR = T_HTE.HTE_NVR");                
            }            
            
        }else{ //NO hay titular pero hay Rol 
       
            if(consExpVO.getCodigoRol()!=null && !"".equals(consExpVO.getCodigoRol().trim())) {  
              join.add("INNER");   
              join.add("HIST_E_EXT");
              join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");            
           }
        }           
       

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }

        // Caso de que tengamos ejercicio, numero o tipo de anotacion.
        if ((consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) ||
            (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) ||
            (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
            join.add("LEFT");
            join.add("HIST_E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");
            join.add("LEFT");
            join.add("HIST_E_EXREXT");
            join.add("EXP_MUN = EXREXT_MUN AND EXP_NUM = EXREXT_NUM");
        }

        //Los joins para ordenar por interesado
        /** ORIGINAL
         join.add("LEFT");
         join.add("HIST_E_EXT A");
         join.add("HIST_E_EXP.EXP_EJE=A.EXT_EJE AND HIST_E_EXP.EXP_MUN=A.EXT_MUN AND HIST_E_EXP.EXP_NUM=A.EXT_NUM");
         join.add("LEFT");
         join.add("T_HTE");
         join.add("A.EXT_TER=T_HTE.HTE_TER AND A.EXT_NVR=T_HTE.HTE_NVR");
         ***/
        
        /*** OSCAR ***/
        //join.add("LEFT");
        //join.add("HIST_E_EXT A");
        //join.add("HIST_E_EXP.EXP_EJE=A.EXT_EJE AND HIST_E_EXP.EXP_MUN=A.EXT_MUN AND HIST_E_EXP.EXP_NUM=A.EXT_NUM");
        /*
        join.add("LEFT");
        join.add("T_HTE");
        join.add("TER.EXT_TER=T_HTE.HTE_TER AND TER.EXT_NVR=T_HTE.HTE_NVR");
        */
        /*** OSCAR ***/

        join.add("false");

        /** original
        String where = "(A.EXT_TER IS NULL OR A.EXT_TER=(SELECT MIN(EXT_TER) FROM HIST_E_EXT WHERE EXT_NUM=EXP_NUM AND MOSTRAR=1)) AND PML_CMP = 'NOM' AND PML_LENG = '"+
                m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";
                ***/
        /** OSCAR **/
        String where = " PML_CMP = 'NOM' AND PML_LENG = '"+
                m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";
        /** OSCAR **/
        
        /***  SI SE FILTRA POR EXPEDIENTE INICIADO DESDE REGISTRO TELEMATICO ****/
        if((consExpVO.getBusqTelematicos()!=null && !"".equals(consExpVO.getBusqTelematicos())) && "1".equals(consExpVO.getBusqTelematicos())){ 
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                where += " EXP_NUM IN (SELECT EXR_NUM FROM HIST_E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            } else {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM HIST_E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            }
        }
        
        where += "(PRO_TIN = 0 OR (PRO_TIN = 1 AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = TRA_UIN))"+ " ";
                

        if (buscarInicioProc) {
                m_Log.debug("***  buscarInicioProc");
            where += " OR (exists (SELECT DISTINCT UOU_UOR " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI " +
                    "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " " +
                    "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))";
        }

        where += "))";

        String[] parametros = new String[2];
 m_Log.debug("*** ANTES  getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getCodProcedimiento() != null) {
             m_Log.debug("***  getCodProcedimiento"+consExpVO.getCodProcedimiento());
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
      m_Log.debug("*** DESPUES getCodProcedimiento"+consExpVO.getCodProcedimiento());
       if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                parametros[0] = "'" + consExpVO.getNumeroExpediente().trim() + "'";
                Config m_ConfigExp = ConfigServiceHelper.getConfig("Expediente");
                Boolean usarMascaraBusquedaGeneral = false;
                try {
                    usarMascaraBusquedaGeneral=m_ConfigExp.getString(Integer.toString(uVO.getOrgCod())  + "/usarMascaraBusquedaGeneral").toUpperCase().equals("SI");
                   
                } catch (Exception e) {
                    usarMascaraBusquedaGeneral = false;
                }
                String condicion;
                if (usarMascaraBusquedaGeneral){
                    int  posAst = parametros[0].indexOf("*");
                    if (posAst != -1)
                        condicion = "EXP_NUM LIKE "+ parametros[0].replace("*", "%");
                    else
                        condicion = "EXP_NUM="+ parametros[0];
                }else
                    condicion = "EXP_NUM="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        if (consExpVO.getEjercicioExpediente() != null) {
            if (!"".equals(consExpVO.getEjercicioExpediente())) {
               
                where += " AND EXP_EJE=" + consExpVO.getEjercicioExpediente();
                
            }
            
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) {
                    condicion = "0";
                    where+=" AND EXP_EST = 0 ";
                }
                else if ("cerrado".equals(consExpVO.getEstado())) {
                    //condicion = "9";
                    where += " AND (EXP_EST=9 OR EXP_EST=1 )";

                }

            }
        }
        
        
          /*** SE BUSCAN LOS EXPEDIENTES QUE TIENEN COMO INTERESADO ALGÚN TERCERO CON UN DETERMINADO DOCUMENTO ***/
        if(!CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
            if (!"".equals(consExpVO.getTitular())) {
                where += " AND TER.EXT_TER = " + consExpVO.getTercero().trim();
            }
        }else
        if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){           
            where += " AND T_HTE.HTE_TID=" + consExpVO.getTipoDocumentoTercero() + " AND T_HTE.HTE_DOC='" + consExpVO.getDocumentoTercero() + "'";
            
        }    
        
        
        /*** ORIGINAL
        if (consExpVO.getTitular() != null) {
            if (!"".equals(consExpVO.getTitular())) {
                where += " AND TER.EXT_TER = " + consExpVO.getTercero().trim();
            }
        }
        */
            
            
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = "TRA_CLS="+consExpVO.getCodClasifTramite();
                if (!"".equals(condicion)) {
                    where += " AND " + condicion+" AND CRO_FEF IS NULL";
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }

        String condFechaFinTramite;
        if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = " AND CRO_FEF IS NULL";
        else condFechaFinTramite = " AND CRO_FEF IS NOT NULL";

        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
              
                    where += " AND CRO_UTR=" + consExpVO.getCodUnidadTram().trim();

            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_UTR = " + cUT;
                    else where += " OR CRO_UTR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += condFechaFinTramite + ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
             
                where += " AND CRO_TRA=" + consExpVO.getCodTramite().trim();

            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }


        // Si tenemos ejercicio de anotacion
        if (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion())) {
            where += " AND (EXR_EJR = " + consExpVO.getEjercicioAnotacion().trim() +
                       " OR EXREXT_EJR = " + consExpVO.getEjercicioAnotacion().trim() + ")";
        }

        // Si tenemos numero de anotacion
        if (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) {
           where += " AND (EXR_NRE = " + consExpVO.getNumeroAnotacion().trim() +
                      " OR EXREXT_NRE = " + consExpVO.getNumeroAnotacion().trim() + ")";
        }

        // Si tenemos tipo de anotacion ('E' o 'S')
        if (consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) {
            where += " AND (EXR_TIP = '" + consExpVO.getCodTipoAnotacion().trim() +
                      "' OR EXREXT_TIP = '" + consExpVO.getCodTipoAnotacion().trim() + "')";
        }

        // Si tenemos asunto
            if (consExpVO.getAsunto() != null) {
                if (!"".equals(consExpVO.getAsunto().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_ASU COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getAsunto().trim() + "%')";
                    else condicion="((EXP_ASU LIKE '%"+consExpVO.getAsunto().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
        // Si tenemos observaciones
            if (consExpVO.getObservaciones() != null) {
                if (!"".equals(consExpVO.getObservaciones().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_OBS COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getObservaciones().trim() + "%')";
                    else condicion="((EXP_OBS LIKE '%"+consExpVO.getObservaciones().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
            
            // #225966: al filtrar por rol y ordenar por interesado se perdía el filtro por rol
            if (consExpVO.getCodigoRol() != null) {
                if (!"".equals(consExpVO.getCodigoRol().trim())) {
                    where += " AND  EXT_ROL= "+ consExpVO.getCodigoRol() ;}
            }   

        // óscar
        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientes".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND CRO_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientesVolumen".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND EXP_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesHistoricos".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesCerrados".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL AND (EXP_EST=1 or EXP_EST=9)";
        }

        // óscar

        // Se tiene en cuenta los procedimientos restringidos. Sólo se recuperan los exp. de los procedimientos no restringidos o, en el caso de que estén restringidos, se
        // recuperan también siempre y cuando el usuario tenga permiso sobre el procedimiento
        where += " AND (PRO_RESTRINGIDO=0 OR (PRO_RESTRINGIDO=1 AND E_PRO.PRO_COD IN (SELECT PRO_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE USU_COD="  +
                 uVO.getIdUsuario() + " AND ORG_COD=" +  uVO.getOrgCod() + ")))";
        
        return oad.join(from, where, join.toArray(new String[]{}));
    }
    
    
    
    
    
    private String obtenerSentenciaConFiltroByInteresadoImprimir(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO, boolean buscarInicioProc)
            throws BDException {

        m_Log.debug("eMPIEZO A obtenerSentenciaConFiltroByInteresadoImprimir");
        
        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }
        
        String from = "  DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom";


        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            from += ", DNN_RCA," + GlobalNames.ESQUEMA_GENERICO + "A_USU,";
        }

        from=from+", "+"T_HTE.HTE_TER, " +
                 oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1", "T_HTE.HTE_PA2",  "T_HTE.HTE_AP2",  "T_HTE.HTE_NOM"})+ " AS NOMBRE";

        ArrayList<String> join = new ArrayList<String>();
        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
        join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");

       
        join.add("LEFT");
        join.add("E_EXT TER");
        join.add("EXP_MUN = TER.EXT_MUN AND EXP_EJE = TER.EXT_EJE AND EXP_NUM = TER.EXT_NUM");            
      

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }

        // Caso de que tengamos ejercicio, numero o tipo de anotacion.
        if ((consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) ||
            (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) ||
            (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
            join.add("LEFT");
            join.add("E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");
            join.add("LEFT");
            join.add("E_EXREXT");
            join.add("EXP_MUN = EXREXT_MUN AND EXP_NUM = EXREXT_NUM");
        }

        //Los joins para ordenar por interesado
        /** ORIGINAL
         join.add("LEFT");
         join.add("E_EXT A");
         join.add("E_EXP.EXP_EJE=A.EXT_EJE AND E_EXP.EXP_MUN=A.EXT_MUN AND E_EXP.EXP_NUM=A.EXT_NUM");
         join.add("LEFT");
         join.add("T_HTE");
         join.add("A.EXT_TER=T_HTE.HTE_TER AND A.EXT_NVR=T_HTE.HTE_NVR");
         ***/
        
        /*** OSCAR ***/
        //join.add("LEFT");
        //join.add("E_EXT A");
        //join.add("E_EXP.EXP_EJE=A.EXT_EJE AND E_EXP.EXP_MUN=A.EXT_MUN AND E_EXP.EXP_NUM=A.EXT_NUM");
        join.add("LEFT");
        join.add("T_HTE");
        join.add("TER.EXT_TER=T_HTE.HTE_TER AND TER.EXT_NVR=T_HTE.HTE_NVR");
        /*** OSCAR ***/

        join.add("false");

        /** original
        String where = "(A.EXT_TER IS NULL OR A.EXT_TER=(SELECT MIN(EXT_TER) FROM E_EXT WHERE EXT_NUM=EXP_NUM AND MOSTRAR=1)) AND PML_CMP = 'NOM' AND PML_LENG = '"+
                m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";
                ***/
        /** OSCAR **/
        String where = " PML_CMP = 'NOM' AND PML_LENG = '"+
                m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";
        /** OSCAR **/
        
        /***  SI SE FILTRA POR EXPEDIENTE INICIADO DESDE REGISTRO TELEMATICO ****/
        if((consExpVO.getBusqTelematicos()!=null && !"".equals(consExpVO.getBusqTelematicos())) && "1".equals(consExpVO.getBusqTelematicos())){ 
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                where += " EXP_NUM IN (SELECT EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            } else {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            }
        }
        
        where += "(PRO_TIN = 0 OR (PRO_TIN = 1 AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)) OR "
                + "(exists (SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = TRA_UIN))"+ " ";
                

        if (buscarInicioProc) {
                m_Log.debug("***  buscarInicioProc");
            where += " OR (exists (SELECT DISTINCT UOU_UOR " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI " +
                    "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " " +
                    "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))";
        }

        where += "))";

        String[] parametros = new String[2];
 m_Log.debug("*** ANTES  getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getCodProcedimiento() != null) {
             m_Log.debug("***  getCodProcedimiento"+consExpVO.getCodProcedimiento());
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
      m_Log.debug("*** DESPUES getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                parametros[0] = "'"+consExpVO.getNumeroExpediente().trim()+"'";
                String condicion = "EXP_NUM="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        if (consExpVO.getEjercicioExpediente() != null) {
            if (!"".equals(consExpVO.getEjercicioExpediente())) {
               
                where += " AND EXP_EJE=" + consExpVO.getEjercicioExpediente();
                
            }
            
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) {
                    condicion = "0";
                    where+=" AND EXP_EST = 0 ";
                }
                else if ("cerrado".equals(consExpVO.getEstado())) {
                    //condicion = "9";
                    where += " AND (EXP_EST=9 OR EXP_EST=1 )";

                }

            }
        }
        
        
          /*** SE BUSCAN LOS EXPEDIENTES QUE TIENEN COMO INTERESADO ALGÚN TERCERO CON UN DETERMINADO DOCUMENTO ***/
        if(!CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
            if (!"".equals(consExpVO.getTitular())) {
                where += " AND TER.EXT_TER = " + consExpVO.getTercero().trim();
            }
        }else
        if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){           
            where += " AND T_HTE.HTE_TID=" + consExpVO.getTipoDocumentoTercero() + " AND T_HTE.HTE_DOC='" + consExpVO.getDocumentoTercero() + "'";
            
        }    
        
        
        /*** ORIGINAL
        if (consExpVO.getTitular() != null) {
            if (!"".equals(consExpVO.getTitular())) {
                where += " AND TER.EXT_TER = " + consExpVO.getTercero().trim();
            }
        }
        */
            
            
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = "TRA_CLS="+consExpVO.getCodClasifTramite();
                if (!"".equals(condicion)) {
                    where += " AND " + condicion+" AND CRO_FEF IS NULL";
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }

        String condFechaFinTramite;
        if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = " AND CRO_FEF IS NULL";
        else condFechaFinTramite = " AND CRO_FEF IS NOT NULL";

        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
              
                    where += " AND CRO_UTR=" + consExpVO.getCodUnidadTram().trim();

            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_UTR = " + cUT;
                    else where += " OR CRO_UTR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += condFechaFinTramite + ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
             
                where += " AND CRO_TRA=" + consExpVO.getCodTramite().trim();

            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }


        // Si tenemos ejercicio de anotacion
        if (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion())) {
            where += " AND (EXR_EJR = " + consExpVO.getEjercicioAnotacion().trim() +
                       " OR EXREXT_EJR = " + consExpVO.getEjercicioAnotacion().trim() + ")";
        }

        // Si tenemos numero de anotacion
        if (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) {
           where += " AND (EXR_NRE = " + consExpVO.getNumeroAnotacion().trim() +
                      " OR EXREXT_NRE = " + consExpVO.getNumeroAnotacion().trim() + ")";
        }

        // Si tenemos tipo de anotacion ('E' o 'S')
        if (consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) {
            where += " AND (EXR_TIP = '" + consExpVO.getCodTipoAnotacion().trim() +
                      "' OR EXREXT_TIP = '" + consExpVO.getCodTipoAnotacion().trim() + "')";
        }

        // Si tenemos asunto
            if (consExpVO.getAsunto() != null) {
                if (!"".equals(consExpVO.getAsunto().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_ASU COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getAsunto().trim() + "%')";
                    else condicion="((EXP_ASU LIKE '%"+consExpVO.getAsunto().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
        // Si tenemos observaciones
            if (consExpVO.getObservaciones() != null) {
                if (!"".equals(consExpVO.getObservaciones().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_OBS COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getObservaciones().trim() + "%')";
                    else condicion="((EXP_OBS LIKE '%"+consExpVO.getObservaciones().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }


        // óscar
        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientes".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND CRO_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientesVolumen".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND EXP_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesHistoricos".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesCerrados".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL AND (EXP_EST=1 or EXP_EST=9)";
        }

        // óscar

        // Se tiene en cuenta los procedimientos restringidos. Sólo se recuperan los exp. de los procedimientos no restringidos o, en el caso de que estén restringidos, se
        // recuperan también siempre y cuando el usuario tenga permiso sobre el procedimiento
        where += " AND (PRO_RESTRINGIDO=0 OR (PRO_RESTRINGIDO=1 AND E_PRO.PRO_COD IN (SELECT PRO_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE USU_COD="  +
                 uVO.getIdUsuario() + " AND ORG_COD=" +  uVO.getOrgCod() + ")))";
        
        return oad.join(from, where, join.toArray(new String[]{}));
    }
    /**
     * Metodo para realizar consultas sobre el conjunto de expedientes de la aplicación y recuperar aquellos que cumplen
     * ciertos criterios generales de un procedimiento.
     * @param uVO UsuarioValueObject con la información del usuario.
     * @param consExpVO ConsultaExpedientesValueObject con los criterios de consulta del expediente.
     * @param params String[] con los parametros de conexión a BB.DD.
     * @param desdeGestion boolean indicando si la consulta se ha hecho desde gestión de expedientes.
     * @param buscarInicioProc boolean indicando si se quiere que la consulta busque aquellos expedientes que puede
     * tramitar el usuario o aquellos expedientes de procedimiento que puede iniciar el usuario.
     * @return Vector con los expedientes recuperados.
     * @throws AnotacionRegistroException En caso de error.
     * @throws TechnicalException En caso de error.
     * @throws TramitacionException En caso de error.
     */
    public Vector consultar(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params,
                            boolean desdeGestion, boolean buscarInicioProc,String columna,String tipoOrden,boolean adjuntarExpediente)
    throws AnotacionRegistroException, TechnicalException, TramitacionException {

        // Comienzo del metodo.
        m_Log.debug("Comienzo de ConsultaExpedientesDAO.consultar()");

        // Obtener la conexion.
        Connection con = null;
        Statement st;
        ResultSet rs;
        String sql;
        Vector<TramitacionValueObject> lista = new Vector<TramitacionValueObject>();
        Vector<TramitacionValueObject> listaFinal = new Vector<TramitacionValueObject>();
        Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(uVO, params);
        TransformacionAtributoSelect transformador;
        boolean superadoLimiteExpedientesImpresion = false;
        int NUM_EXPEDIENTES_MAXIMO_CONSULTA = 10000;                
        String codigoColOrdInteresado=COLUMNA_ORDEN_INTERESADO; //Columna de ordenación por tercero

        if (lUT.size() > 0) {
            // Tiene unidades tramitadoras.

            try {
                AdaptadorSQLBD oad = new AdaptadorSQLBD(params);               
                con = oad.getConnection();
                st = con.createStatement();
                transformador = new TransformacionAtributoSelect(oad,con);

                /* Creacion de la consulta SQL.
                 Queremos que se nos muestren para un usuario concreto solo los expedientes tramitados por las
                 unidades organicas a las que el pertenece. Se hace la consulta con filtro.
                */
                // String sqlSinFiltro = obtenerSentenciaSinFiltro(consExpVO,oad,transformador,params);

                /* Comprobar datos del usuario */
                m_Log.debug("--> Codigo Unidad organizativa : " + uVO.getUnidadOrgCod());
                m_Log.debug("--> Unidad organizativa : " + uVO.getUnidadOrg());
                m_Log.debug("--> Codigo departamento(String) : " + uVO.getDep());
                m_Log.debug("--> Codigo departamento(int) : " + uVO.getDepCod());
                m_Log.debug("--> Codigo Ent : " + uVO.getEntCod());
                m_Log.debug("--> Nombre de usuario : " + uVO.getNombreUsu());
                m_Log.debug("--> Identificador de usuario : " + uVO.getIdUsuario());
                m_Log.debug("--> Org (string) : " + uVO.getOrg());
                m_Log.debug("--> Org (int) : " + uVO.getOrgCod());
                
                String sqlConFiltro;
                if(codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                {
                    m_Log.debug("obtenerSentenciaConFiltroByInteresado");
                                    
                    if  ("2".equals(consExpVO.getTipoBusqueda()))      
                        sqlConFiltro = obtenerSentenciaConFiltroByInteresadoHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc, tipoOrden);
                    else if ("1".equals(consExpVO.getTipoBusqueda())) 
                        sqlConFiltro = obtenerSentenciaConFiltroByInteresadoActivosHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc, tipoOrden);
                    else 
                        sqlConFiltro = obtenerSentenciaConFiltroByInteresado(consExpVO, oad, transformador, params, uVO, buscarInicioProc, tipoOrden);
                }
                else {
                      m_Log.debug("obtenerSentenciaConFiltro");
                    if  ("2".equals(consExpVO.getTipoBusqueda()))      
                        sqlConFiltro = obtenerSentenciaConFiltroHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                    else if ("1".equals(consExpVO.getTipoBusqueda())) 
                        sqlConFiltro = obtenerSentenciaConFiltroActivosHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                    else 
                        sqlConFiltro = obtenerSentenciaConFiltro(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                }

                int posE = 0;
                int numPaginaE = 0;
                int pagActual = -1;
                if (!"".equals(consExpVO.getPaginaListado())&&consExpVO.getPaginaListado()!=null) {
                    pagActual = Integer.parseInt(consExpVO.getPaginaListado());
                }
                int lineas = -1;
                if (!"".equals(consExpVO.getNumLineasPaginaListado())&&consExpVO.getNumLineasPaginaListado()!=null) {
                    lineas = Integer.parseInt(consExpVO.getNumLineasPaginaListado());
                }
                
                 if ((consExpVO.isImprimiendo())||(consExpVO.isReaperturaExpediente())||((pagActual==-1)&&(lineas==-1))) {
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY  5 ASC ";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY NOMBRE " + tipoOrden;
                    } else {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY " + columna + " " + tipoOrden;
                    }
                } else  if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                      

                    
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by 5 asc) RN  FROM(" + sqlConFiltro + " ORDER BY 5 ASC) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select  /*+first_rows(" + lineas + ")*/  *  FROM (SELECT consulta.*, ROW_NUMBER() over ( order by " + columna + " " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by " + columna + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                }
                else  //compatible con sqlserver
                {
                    // Obtenemos el nombre de la columna para ordenarlo en sqlserver	
                    String nombreColumnaOrdenar = null;	
                    if (!codigoColOrdInteresado.equals(columna)) {	
                        nombreColumnaOrdenar = getNombreColumnaOrdenTabla(columna, params);	
                        // Para que no falle, si no tiene especificado columna en A_CAMPLIST, elegimos por defecto la de numero de expediente	
                        if (StringUtils.isEmpty(nombreColumnaOrdenar)) {	
                            nombreColumnaOrdenar = CONSULTA_EXPEDIENTES_CAMPLIST_NOM_ORDEN_EXP_NUM;	
                        }	
                    }
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by (exp_num) asc) RN  FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by exp_num asc) CONSULTA )  resultado where rn between ? and ? order by rn";                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + ") sqlconfiltro order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select resultado.*  FROM (SELECT consulta.*, ROW_NUMBER() over ( order by (" + nombreColumnaOrdenar + ") " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by " + nombreColumnaOrdenar + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } 
                }
                m_Log.debug(sql);
                
                String alter1 = null;
                String alter2 = null;
                
                
                
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    if(consultaInsensitiva){
                        st.executeQuery(alter1);
                        st.executeQuery(alter2);
                    }
                }
                 if ((consExpVO.isImprimiendo())||(consExpVO.isReaperturaExpediente())||((pagActual==-1)&&(lineas==-1))) {
                    rs = st.executeQuery(sql);
                } else {
                    //rs = st.executeQuery(sql);
                    PreparedStatement ps = con.prepareStatement(sql);

                    m_Log.debug("resultado de " + ((lineas * (pagActual - 1)) + 1) + " a " + pagActual * lineas);

                    ps.setInt(1, (lineas * (pagActual - 1)) + 1);
                    ps.setInt(2, pagActual * lineas);

                    rs = ps.executeQuery();
                }
                /*******************************************************************************************************************/
                /*******************************************************************************************************************/
                /************************** SE RECUPERA EL NÚMERO MÁXIMO DE EXPEDIENTES A RECUPERAR AL EXPORTAR A PDF/CSV **********/
                /*******************************************************************************************************************/
                /*******************************************************************************************************************/
                
                try{
                    
                    ResourceBundle config = ResourceBundle.getBundle("Expediente");
                    String S_NUM_EXPEDIENTES_MAXIMO_CONSULTA =  config.getString(uVO.getOrgCod() + "/numero_maximo_expedientes_recuperados_exportar_consulta_expedientes");
                    if(S_NUM_EXPEDIENTES_MAXIMO_CONSULTA!=null && !"".equals(S_NUM_EXPEDIENTES_MAXIMO_CONSULTA)){
                        NUM_EXPEDIENTES_MAXIMO_CONSULTA = Integer.parseInt(S_NUM_EXPEDIENTES_MAXIMO_CONSULTA);
                    }
                    
                }catch(Exception e){
                    m_Log.debug("  ERROR AL RECUPERAR LA PROPIEDAD CON EL NÚMERO MAXIMO DE EXPEDIENTES A RECUPERAR EN CONSULTA AL EXPORTAR A PDF/CSV: " + e.getMessage());
                }
                
                int contador = 0;        
                while(rs.next()) {
                    
                    if (consExpVO.isImprimiendo()) {
                        // Se está imprimiendo, entonces se cuenta el número de expedientes.                        
                        if(contador>=NUM_EXPEDIENTES_MAXIMO_CONSULTA){
                            superadoLimiteExpedientesImpresion = true;
                            break;
                        }                        
                    }
                    
                    
                    TramitacionValueObject tvo1 = new TramitacionValueObject();
                     //GUARDAMOS EL USUARIO DE INICIO PARA TENERLO EN LA BUSQUEDA DE EXPEDIENTES
                    tvo1.setUsuarioIni(rs.getString("usu_nom"));
                    tvo1.setLocalizacion(rs.getString("EXP_LOC"));
                    tvo1.setEstado(rs.getString("EXP_EST"));
                    tvo1.setCodMunicipio(rs.getString("EXP_MUN"));
                    tvo1.setCodProcedimiento(rs.getString("EXP_PRO"));
                    tvo1.setDescProcedimiento(rs.getString("PML_VALOR"));
                    tvo1.setEjercicio(rs.getString("EXP_EJE"));
                    tvo1.setNumero(rs.getString("EXP_NUM"));
                    tvo1.setFechaInicioExpediente(rs.getString("fIni"));
                    tvo1.setFechaFinExpediente(rs.getString("fFin"));
                    String asuntoExp = rs.getString("EXP_ASU");
                    if (asuntoExp != null) tvo1.setAsuntoExp(AdaptadorSQLBD.js_escape(asuntoExp));
                    else tvo1.setAsuntoExp("");
                    String unidadIni = rs.getString("UOR_NOM");
                    tvo1.setUnidadInicio(unidadIni);
                    
                    /** original
                    String tipoInicio = rs.getString("EXR_TOP");
                    if("0".equals(tipoInicio)) tvo1.setTipoInicio("Instancia");
                    else tvo1.setTipoInicio("Oficio");
                    **/
                    if(this.isExpedienteIniciadoInstanciaParte(tvo1.getNumero(),tvo1.getCodProcedimiento(),tvo1.getCodMunicipio(),tvo1.getEjercicio(), con)){
                        tvo1.setTipoInicio("Instancia");
                    }else
                        tvo1.setTipoInicio("Oficio");
                    
                    tvo1.setExpHistorico("S".equals(rs.getString("EXHISTORICO")));
                    
                    lista.addElement(tvo1);
                    posE++;
                    contador++;
               }// while
                
                
                if (m_Log.isDebugEnabled()) m_Log.debug("Se han recuperado " + lista.size() + " expedientes.");
                rs.close();

                // Completamos con los datos del interesado.
                for (int i = 0; i < lista.size(); i++) {
                    TramitacionValueObject tvo1 = lista.elementAt(i);
                        
                     String from = "EXT_ROL, " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA1", "''", "HTE_AP1"}) + " AS ap1, " +
                            oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA2", "''", "HTE_AP2"}) + " AS ap2, HTE_NOM AS nombre, " + 
                            "ROL_COD, ROL_DES ";

                    String where = "EXT_MUN = " + tvo1.getCodMunicipio() + " AND EXT_EJE = " + tvo1.getEjercicio() +
                            " AND EXT_NUM = '" + tvo1.getNumero() + "' ";
                    
                    String[] join = new String[8];

                    if (tvo1.isExpHistorico())
                        join[0] = "HIST_E_EXT";
                    else
                        join[0] = "E_EXT";

                    join[1] = "LEFT";
                    join[2] = "T_HTE";
                    join[3] = "EXT_TER = HTE_TER AND EXT_NVR = HTE_NVR";
                    join[4] = "LEFT";
                    join[5] = "E_ROL";
                    join[6] = "ROL_MUN = EXT_MUN AND ROL_PRO = EXT_PRO AND ROL_COD = EXT_ROL";
                    join[7] = "false";

                    sql = oad.join(from, where, join);

                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);

                    String titular = "";
                    while (rs.next()) {
                        if (!"".equals(titular))
                            titular += "<br/>";
                        //formato del interesado
                         String ap1=rs.getString("ap1");
                         String ap2=rs.getString("ap2");
                         String nombre=rs.getString("nombre");  
                         titular+=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);
                         
                         int codRol = rs.getInt("ROL_COD");
                         String desRol = rs.getString("ROL_DES");
                         titular += "<br/>(" + codRol + " - " + desRol + ")";
                    }
                    tvo1.setTitular(titular);
                    rs.close();

                    // Como estamos en consulta, buscaremos los datos referentes a los
                    // tramites abiertos de cada expediente.
                    String condFechaFinTramite;
                    if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = "IS NULL";
                    else condFechaFinTramite = "IS NOT NULL";
                    sql = "SELECT " +
                            oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FINITRAMITE, " +
                            oad.convertir("CRO_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FFINTRAMITE, " +
                            "TML_VALOR " +
                            "FROM E_CRO, E_TML " +
                            "WHERE CRO_MUN = TML_MUN AND CRO_PRO = TML_PRO AND CRO_TRA = TML_TRA " +
                            "AND CRO_NUM = '" + tvo1.getNumero() + "' AND CRO_FEF " + condFechaFinTramite;

                    if (consExpVO.getCodTramite() != null && !"".equals(consExpVO.getCodTramite())&& !"todos".equals(consExpVO.getCodTramite()))
                        sql += " AND CRO_TRA = " + consExpVO.getCodTramite();
                    
                    /*if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);*/
                    //ELIMINAMOS LA EJECUCIÓN DE ESTA CONSULTA. EL EN LISTADO NO NECESITAMOS ESTA INFO. SOLO PARA IMPRIMIR Y SE USA OTRO METODO

                    Vector<String> tramitesIniciados = new Vector<String>();
                    Vector<String> fechasInicioTramites = new Vector<String>();
                    Vector<String> fechasFinTramites = new Vector<String>();
                    /*while (rs.next()) {
                        tramitesIniciados.add(rs.getString("TML_VALOR"));
                        fechasInicioTramites.add(rs.getString("FINITRAMITE"));
                        fechasFinTramites.add(rs.getString("FFINTRAMITE"));
                    }*/
                   
                    if (!desdeGestion || tramitesIniciados.size() > 0) {
                        tvo1.setListaTramitesAbiertos(tramitesIniciados);
                        tvo1.setListaFechasInicioTramitesAbiertos(fechasInicioTramites);
                        tvo1.setListaFechasFinTramitesAbiertos(fechasFinTramites);
                        listaFinal.addElement(tvo1);
                    }

                    //rs.close();

                    /***/
                     // Como estamos en consulta, buscaremos los datos referentes a los
                    // tramites cerrados
                    sql = "SELECT " +
                            oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FINITRAMITE, " +
                            oad.convertir("CRO_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FFINTRAMITE, " +
                            "TML_VALOR " +
                            "FROM E_CRO, E_TML " +
                            "WHERE CRO_MUN = TML_MUN AND CRO_PRO = TML_PRO AND CRO_TRA = TML_TRA " +
                            "AND CRO_NUM = '" + tvo1.getNumero() + "' AND CRO_FEF IS NOT NULL";

                    if (consExpVO.getCodTramite() != null && !"".equals(consExpVO.getCodTramite())&& !"todos".equals(consExpVO.getCodTramite()))
                        sql += " AND CRO_TRA = " + consExpVO.getCodTramite();

                   /* if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);*/
                    //ELIMINAMOS LA EJECUCIÓN DE ESTA CONSULTA. EL EN LISTADO NO NECESITAMOS ESTA INFO. SOLO PARA IMPRIMIR Y SE USA OTRO METODO

                    Vector<String> tramitesFinalizados = new Vector<String>();
                    /*while (rs.next()) {
                        tramitesFinalizados.add(rs.getString("TML_VALOR"));
                    }*/
                    
                    if (!desdeGestion || tramitesFinalizados.size() > 0) {
                        tvo1.setListaTramitesFinalizados(tramitesFinalizados);
                    } 
                    //rs.close();
                    /******/

                    sql="SELECT G_REL.REL_NUM FROM G_EXP, G_REL WHERE G_REL.REL_NUM=G_EXP.REL_NUM AND REL_EST=0 AND EXP_NUM='"+
                            tvo1.getNumero()+"' ";
                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);
                    if (rs.next()) tvo1.setNumeroRelacion(rs.getString("REL_NUM"));
                }

                st.close();

            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
            }
            finally {
                try {
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    Statement st_alter;
                    String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                    String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                    if(consultaInsensitiva){
                        st_alter = con.createStatement();
                        st_alter.executeQuery(alter1);
                        st_alter.executeQuery(alter2);
                    
                        st_alter.close();
                    }
                    }

                    con.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }
        
        
        if(superadoLimiteExpedientesImpresion){
            // Si se ha superado el límite maximo de expedientes a recuperar al exportar a PDF/CSV
            listaFinal.get(0).setExportarConsultaSuperadosLimiteMaximo(true);
            listaFinal.get(0).setNumMaximoExpedientesRecuperados(NUM_EXPEDIENTES_MAXIMO_CONSULTA);
        }
        
        return listaFinal;
    }
    
    
    
    
     public Vector consultarImprimir(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params,
                            boolean desdeGestion, boolean buscarInicioProc,String columna,String tipoOrden,boolean paraCSV)
    throws AnotacionRegistroException, TechnicalException, TramitacionException {

        // Comienzo del metodo.
        m_Log.debug("Comienzo de ConsultaExpedientesDAO.consultar()");

        // Obtener la conexion.
        Connection con = null;
        Statement st;
        ResultSet rs;
        String sql;
        Vector<TramitacionValueObject> lista = new Vector<TramitacionValueObject>();
        Vector<TramitacionValueObject> listaFinal = new Vector<TramitacionValueObject>();
        Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(uVO, params);
        TransformacionAtributoSelect transformador;
        boolean superadoLimiteExpedientesImpresion = false;
        int NUM_EXPEDIENTES_MAXIMO_CONSULTA = 10000;                
        String codigoColOrdInteresado=COLUMNA_ORDEN_INTERESADO; //Columna de ordenación por tercero

        if (lUT.size() > 0) {
            // Tiene unidades tramitadoras.

            try {
                AdaptadorSQLBD oad = new AdaptadorSQLBD(params);               
                con = oad.getConnection();
                st = con.createStatement();
                transformador = new TransformacionAtributoSelect(oad,con); 

                /* Creacion de la consulta SQL.
                 Queremos que se nos muestren para un usuario concreto solo los expedientes tramitados por las
                 unidades organicas a las que el pertenece. Se hace la consulta con filtro.
                */
                // String sqlSinFiltro = obtenerSentenciaSinFiltro(consExpVO,oad,transformador,params);

                /* Comprobar datos del usuario */
                m_Log.debug("--> Codigo Unidad organizativa : " + uVO.getUnidadOrgCod());
                m_Log.debug("--> Unidad organizativa : " + uVO.getUnidadOrg());
                m_Log.debug("--> Codigo departamento(String) : " + uVO.getDep());
                m_Log.debug("--> Codigo departamento(int) : " + uVO.getDepCod());
                m_Log.debug("--> Codigo Ent : " + uVO.getEntCod());
                m_Log.debug("--> Nombre de usuario : " + uVO.getNombreUsu());
                m_Log.debug("--> Identificador de usuario : " + uVO.getIdUsuario());
                m_Log.debug("--> Org (string) : " + uVO.getOrg());
                m_Log.debug("--> Org (int) : " + uVO.getOrgCod());
                
                String sqlConFiltro;
                if(codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                {
                    m_Log.debug("obtenerSentenciaConFiltroByInteresadoImprimir");
                     sqlConFiltro  = obtenerSentenciaConFiltroByInteresadoImprimir(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                }
                else {
                    m_Log.debug("obtenerSentenciaConFiltroImprimir");
                     sqlConFiltro = obtenerSentenciaConFiltroImprimir(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                }


                int posE = 0;
                int numPaginaE = 0;
                int pagActual = -1;
                if (!"".equals(consExpVO.getPaginaListado())&&consExpVO.getPaginaListado()!=null) {
                    pagActual = Integer.parseInt(consExpVO.getPaginaListado());
                }
                int lineas = -1;
                if (!"".equals(consExpVO.getNumLineasPaginaListado())&&consExpVO.getNumLineasPaginaListado()!=null) {
                    lineas = Integer.parseInt(consExpVO.getNumLineasPaginaListado());
                }

                if (consExpVO.isImprimiendo()) {
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY  5 ASC ";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY NOMBRE " + tipoOrden;
                    } else {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY " + columna + " " + tipoOrden;
                    }
                } else if (params[0] != null && (params[0].equals("oracle") || params[0].equals("ORACLE"))) {



                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by 5 asc) RN  FROM(" + sqlConFiltro + " ORDER BY 5 ASC) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * FROM (SELECT consulta.*, ROW_NUMBER() over ( order by " + columna + " " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by " + columna + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                } else //compatible con sqlserver
                {
                    // Obtenemos el nombre de la columna para ordenarlo en sqlserver	
                    String nombreColumnaOrdenar = null;	
                    if (!codigoColOrdInteresado.equals(columna)) {	
                        nombreColumnaOrdenar = getNombreColumnaOrdenTabla(columna, params);	
                        // Para que no falle, si no tiene especificado columna en A_CAMPLIST, elegimos por defecto la de numero de expediente	
                        if (StringUtils.isEmpty(nombreColumnaOrdenar)) {	
                            nombreColumnaOrdenar = CONSULTA_EXPEDIENTES_CAMPLIST_NOM_ORDEN_EXP_NUM;	
                        }	
                    }
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by (exp_num) asc) RN  FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by exp_num asc) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + ") sqlconfiltro order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select resultado.* FROM  (SELECT consulta.*, ROW_NUMBER() over ( order by (" + nombreColumnaOrdenar + ") " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by " + nombreColumnaOrdenar + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                }
                
                m_Log.debug(sql);
                
                String alter1 = null;
                String alter2 = null;
                
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    if(consultaInsensitiva){
                    st.executeQuery(alter1);
                    st.executeQuery(alter2);
                    }
                }
                
                if (consExpVO.isImprimiendo()) {
                    rs = st.executeQuery(sql);
                } else {
                    //rs = st.executeQuery(sql);
                    PreparedStatement ps = con.prepareStatement(sql);

                    m_Log.debug("resultado de " + ((lineas * (pagActual - 1)) + 1) + " a " + pagActual * lineas);

                    ps.setInt(1, (lineas * (pagActual - 1)) + 1);
                    ps.setInt(2, pagActual * lineas);

                    rs = ps.executeQuery();
                }
                
                /*******************************************************************************************************************/
                /*******************************************************************************************************************/
                /************************** SE RECUPERA EL NÚMERO MÁXIMO DE EXPEDIENTES A RECUPERAR AL EXPORTAR A PDF/CSV **********/
                /*******************************************************************************************************************/
                /*******************************************************************************************************************/
                
                try{
                    
                    ResourceBundle config = ResourceBundle.getBundle("Expediente");
                    String S_NUM_EXPEDIENTES_MAXIMO_CONSULTA =  config.getString(uVO.getOrgCod() + "/numero_maximo_expedientes_recuperados_exportar_consulta_expedientes");
                    if(S_NUM_EXPEDIENTES_MAXIMO_CONSULTA!=null && !"".equals(S_NUM_EXPEDIENTES_MAXIMO_CONSULTA)){
                        NUM_EXPEDIENTES_MAXIMO_CONSULTA = Integer.parseInt(S_NUM_EXPEDIENTES_MAXIMO_CONSULTA);
                    }
                    
                }catch(Exception e){
                    m_Log.debug("  ERROR AL RECUPERAR LA PROPIEDAD CON EL NÚMERO MAXIMO DE EXPEDIENTES A RECUPERAR EN CONSULTA AL EXPORTAR A PDF/CSV: " + e.getMessage());
                }
                
                int contador = 0;  
                  m_Log.debug("entramos a recorrer los resultados");
                while(rs.next()) {
                    
                    if (consExpVO.isImprimiendo()) {
                        // Se está imprimiendo, entonces se cuenta el número de expedientes.                        
                        if(contador>=NUM_EXPEDIENTES_MAXIMO_CONSULTA){
                            superadoLimiteExpedientesImpresion = true;
                            break;
                        }                        
                    }
                    
                    
                    TramitacionValueObject tvo1 = new TramitacionValueObject();
                   
                     //GUARDAMOS EL USUARIO DE INICIO PARA TENERLO EN LA BUSQUEDA DE EXPEDIENTES
                    tvo1.setUsuarioIni(rs.getString("usu_nom"));
                    tvo1.setLocalizacion(rs.getString("EXP_LOC"));
                    tvo1.setEstado(rs.getString("EXP_EST"));
                    tvo1.setCodMunicipio(rs.getString("EXP_MUN"));
                    tvo1.setCodProcedimiento(rs.getString("EXP_PRO"));
                    tvo1.setDescProcedimiento(rs.getString("PML_VALOR"));
                    tvo1.setEjercicio(rs.getString("EXP_EJE"));
                    tvo1.setNumero(rs.getString("EXP_NUM"));
                    tvo1.setFechaInicioExpediente(rs.getString("fIni"));
                    tvo1.setFechaFinExpediente(rs.getString("fFin"));
                    String asuntoExp = rs.getString("EXP_ASU");
                    if (asuntoExp != null) tvo1.setAsuntoExp(AdaptadorSQLBD.js_escape(asuntoExp));
                    else tvo1.setAsuntoExp("");
                    String unidadIni = rs.getString("UOR_NOM");
                    tvo1.setUnidadInicio(unidadIni);

                    /** original
                    String tipoInicio = rs.getString("EXR_TOP");
                    if("0".equals(tipoInicio)) tvo1.setTipoInicio("Instancia");
                    else tvo1.setTipoInicio("Oficio");
                    **/
                    if(this.isExpedienteIniciadoInstanciaParte(tvo1.getNumero(),tvo1.getCodProcedimiento(),tvo1.getCodMunicipio(),tvo1.getEjercicio(), con)){
                        tvo1.setTipoInicio("Instancia");
                    }else
                        tvo1.setTipoInicio("Oficio");
                    
                    
                    
                   

                    lista.addElement(tvo1);
                    posE++;
                    contador++;  
               }// while
                
                
                if (m_Log.isDebugEnabled()) m_Log.debug("Se han recuperado " + lista.size() + " expedientes.");
                rs.close();
                
                for (int i = 0; i < lista.size(); i++) {
                    TramitacionValueObject tvo1 = lista.elementAt(i);
                  
                    
                    String from = "EXT_ROL, " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA1", "''", "HTE_AP1"}) + " AS ap1, " +
                            oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA2", "''", "HTE_AP2"}) + " AS ap2, HTE_NOM AS nombre, " + 
                            "ROL_COD, ROL_DES ";

                    String where = "EXT_MUN = " + tvo1.getCodMunicipio() + " AND EXT_EJE = " + tvo1.getEjercicio() +
                            " AND EXT_NUM = '" + tvo1.getNumero() + "' ";

                    String[] join = new String[8];

                    if (tvo1.isExpHistorico())
                        join[0] = "HIST_E_EXT";
                    else
                        join[0] = "E_EXT";

                    join[1] = "LEFT";
                    join[2] = "T_HTE";
                    join[3] = "EXT_TER = HTE_TER AND EXT_NVR = HTE_NVR";
                    join[4] = "LEFT";
                    join[5] = "E_ROL";
                    join[6] = "ROL_MUN = EXT_MUN AND ROL_PRO = EXT_PRO AND ROL_COD = EXT_ROL";
                    join[7] = "false";

                    sql = oad.join(from, where, join);

                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);

                    String titular;
                    if (paraCSV) {
                        titular = "\"";
                        while (rs.next()) {
                            if (!"\"".equals(titular))
                                titular += "\r";
                            //formato del interesado
                             String ap1=rs.getString("ap1");
                             String ap2=rs.getString("ap2");
                             String nombre=rs.getString("nombre");  
                             titular+=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);

                             int codRol = rs.getInt("ROL_COD");
                             String desRol = rs.getString("ROL_DES");
                             titular += " (" + codRol + " - " + desRol + ")";
                        }
                        titular += "\"";
                    } else {
                        titular = "";
                        while (rs.next()) {
                            if (!"".equals(titular))
                                titular += ">>";
                            //formato del interesado
                             String ap1=rs.getString("ap1");
                             String ap2=rs.getString("ap2");
                             String nombre=rs.getString("nombre");
                             titular+=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);

                             int codRol = rs.getInt("ROL_COD");
                             String desRol = rs.getString("ROL_DES");
                             titular += ">>(" + codRol + " - " + desRol + ")";
                        }
                    }
                    tvo1.setTitular(titular);
                    rs.close();

                    
                    // Como estamos en consulta, buscaremos los datos referentes a los
                    // tramites abiertos de cada expediente.
                    String condFechaFinTramite;
                    if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = "IS NULL";
                    else condFechaFinTramite = "IS NOT NULL";
                    sql = "SELECT " +
                            oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FINITRAMITE, " +
                            oad.convertir("CRO_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FFINTRAMITE, " +
                            "TML_VALOR " +
                            "FROM E_CRO, E_TML " +
                            "WHERE CRO_MUN = TML_MUN AND CRO_PRO = TML_PRO AND CRO_TRA = TML_TRA " +
                            "AND CRO_NUM = '" + tvo1.getNumero() + "' AND CRO_FEF " + condFechaFinTramite;

                    if (consExpVO.getCodTramite() != null && !"".equals(consExpVO.getCodTramite())&& !"todos".equals(consExpVO.getCodTramite()))
                        sql += " AND CRO_TRA = " + consExpVO.getCodTramite();
                    
                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);

                    Vector<String> tramitesIniciados = new Vector<String>();
                    Vector<String> fechasInicioTramites = new Vector<String>();
                    Vector<String> fechasFinTramites = new Vector<String>();
                    while (rs.next()) {
                        tramitesIniciados.add(rs.getString("TML_VALOR"));
                        fechasInicioTramites.add(rs.getString("FINITRAMITE"));
                        fechasFinTramites.add(rs.getString("FFINTRAMITE"));
                    }

                    if (!desdeGestion || tramitesIniciados.size() > 0) {
                        tvo1.setListaTramitesAbiertos(tramitesIniciados);
                        tvo1.setListaFechasInicioTramitesAbiertos(fechasInicioTramites);
                        tvo1.setListaFechasFinTramitesAbiertos(fechasFinTramites);
                        listaFinal.addElement(tvo1);
                    }

                    rs.close();

                   
                }


                st.close();

            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
            }
            finally {
                try {                    
                   
                    if(consultaInsensitiva){
                        if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                            Statement st_alter;
                            String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                            String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                            st_alter = con.createStatement();
                            st_alter.executeQuery(alter1);
                            st_alter.executeQuery(alter2);

                            st_alter.close();
                        }
                    }
                    con.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }
        
        
        if(superadoLimiteExpedientesImpresion){
            // Si se ha superado el límite maximo de expedientes a recuperar al exportar a PDF/CSV
            lista.get(0).setExportarConsultaSuperadosLimiteMaximo(true);
            lista.get(0).setNumMaximoExpedientesRecuperados(NUM_EXPEDIENTES_MAXIMO_CONSULTA);
        }
        
        return listaFinal;
    }
     
  
     
          public Vector consultarImprimirOptimo(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params,
                            boolean desdeGestion, boolean buscarInicioProc,String columna,String tipoOrden,boolean paraCSV)
    throws AnotacionRegistroException, TechnicalException, TramitacionException {

        // Comienzo del metodo.
        m_Log.debug("Comienzo de ConsultaExpedientesDAO.consultar()");

        // Obtener la conexion.
        Connection con = null;
        Statement st;
        ResultSet rs;
        String sql="";
        Vector<TramitacionValueObject> lista = new Vector<TramitacionValueObject>();
        Vector<TramitacionValueObject> listaFinal = new Vector<TramitacionValueObject>();
        Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(uVO, params);
        TransformacionAtributoSelect transformador;
        boolean superadoLimiteExpedientesImpresion = false;
        int NUM_EXPEDIENTES_MAXIMO_CONSULTA = 10000;                
        String codigoColOrdInteresado=COLUMNA_ORDEN_INTERESADO; //Columna de ordenación por tercero

        if (lUT.size() > 0) {
            // Tiene unidades tramitadoras.

            try {
                AdaptadorSQLBD oad = new AdaptadorSQLBD(params);               
                con = oad.getConnection();
                st = con.createStatement();
                transformador = new TransformacionAtributoSelect(oad,con); 

                /* Creacion de la consulta SQL.
                 Queremos que se nos muestren para un usuario concreto solo los expedientes tramitados por las
                 unidades organicas a las que el pertenece. Se hace la consulta con filtro.
                */
                // String sqlSinFiltro = obtenerSentenciaSinFiltro(consExpVO,oad,transformador,params);

                /* Comprobar datos del usuario */
                m_Log.debug("--> Codigo Unidad organizativa : " + uVO.getUnidadOrgCod());
                m_Log.debug("--> Unidad organizativa : " + uVO.getUnidadOrg());
                m_Log.debug("--> Codigo departamento(String) : " + uVO.getDep());
                m_Log.debug("--> Codigo departamento(int) : " + uVO.getDepCod());
                m_Log.debug("--> Codigo Ent : " + uVO.getEntCod());
                m_Log.debug("--> Nombre de usuario : " + uVO.getNombreUsu());
                m_Log.debug("--> Identificador de usuario : " + uVO.getIdUsuario());
                m_Log.debug("--> Org (string) : " + uVO.getOrg());
                m_Log.debug("--> Org (int) : " + uVO.getOrgCod());
                
                String sqlConFiltro;
                if(codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                {
                    System.out.println("\n obtenerSentenciaConFiltroByInteresadoImprimir");
                     sqlConFiltro  = obtenerSentenciaConFiltroByInteresadoImprimir(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                }
                else {
                    System.out.println("\n obtenerSentenciaConFiltroImprimir");
                     sqlConFiltro = obtenerSentenciaConFiltroImprimir(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                }


                int posE = 0;
                int numPaginaE = 0;
                int pagActual = -1;
                if (!"".equals(consExpVO.getPaginaListado())&&consExpVO.getPaginaListado()!=null) {
                    pagActual = Integer.parseInt(consExpVO.getPaginaListado());
                }
                int lineas = -1;
                if (!"".equals(consExpVO.getNumLineasPaginaListado())&&consExpVO.getNumLineasPaginaListado()!=null) {
                    lineas = Integer.parseInt(consExpVO.getNumLineasPaginaListado());
                }

                if (consExpVO.isImprimiendo()) {
                    if (params[0] != null && (params[0].equals("oracle") || params[0].equals("ORACLE"))) {
                        
                        //TODO
//                        if (tvo1.isExpHistorico())
//                        join[0] = "HIST_E_EXT";
//                    else
//                        join[0] = "E_EXT";
                        if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                            sql = "SELECT CONSULTA.*,(SELECT COUNT(*) AS NUM FROM E_EXR WHERE EXR_NUM=EXP_NUM AND EXR_PRO=EXP_PRO AND EXR_MUN=EXP_MUN AND exr_eje=EXP_EJE AND EXR_TOP=0)AS CUENTA, "+
                                    "(SELECT rtrim (xmlagg (xmlelement (e,  EXT_ROL||'#'|| NVL(HTE_PA1, '') || NVL('', '') || NVL(HTE_AP1, '') ||'#'||"+
                                   "NVL(HTE_PA2, '') || NVL('', '') || NVL(HTE_AP2, '') ||'#'||HTE_NOM ||'#'||ROL_COD ||'#'||ROL_DES  || '$')).extract ('//text()'), '$') as valor FROM E_EXT, "+
                                    "T_HTE,E_ROL WHERE EXT_NUM = EXP_NUM AND EXT_TER = HTE_TER(+) AND EXT_NVR = HTE_NVR(+) AND ROL_MUN = EXT_MUN(+) AND ROL_PRO = EXT_PRO(+) AND ROL_COD = EXT_ROL(+)) as interesados, "+
                                    "(SELECT rtrim (xmlagg (xmlelement (e,  '<FECHA_INICIO>'||NVL(TO_CHAR(CRO_FEI, 'DD/MM/YYYY'), '') ||'</FECHA_INICIO><NOMBRE>'|| TML_VALOR||'</NOMBRE>' || '$')).extract ('//text()'), '$') as valor "+
                                    " FROM E_CRO,E_TML WHERE CRO_MUN = TML_MUN AND CRO_PRO = TML_PRO AND CRO_TRA = TML_TRA AND CRO_NUM =EXP_NUM and cro_fef is null) as tramites "+
                                    "FROM(" + sqlConFiltro + " ORDER BY  5 ASC) CONSULTA  ";
                        } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                        {
                            sql = "SELECT CONSULTA.*,(SELECT COUNT(*) AS NUM FROM E_EXR WHERE EXR_NUM=EXP_NUM AND EXR_PRO=EXP_PRO AND EXR_MUN=EXP_MUN AND exr_eje=EXP_EJE AND EXR_TOP=0)AS CUENTA, "+
                                    "(SELECT rtrim (xmlagg (xmlelement (e,  EXT_ROL||'#'|| NVL(HTE_PA1, '') || NVL('', '') || NVL(HTE_AP1, '') ||'#'||"+
                              "NVL(HTE_PA2, '') || NVL('', '') || NVL(HTE_AP2, '') ||'#'||HTE_NOM ||'#'||ROL_COD ||'#'||ROL_DES  || '$')).extract ('//text()'), '$') as valor FROM E_EXT, "+
                                    "T_HTE,E_ROL WHERE EXT_NUM = EXP_NUM AND EXT_TER = HTE_TER(+) AND EXT_NVR = HTE_NVR(+) AND ROL_MUN = EXT_MUN(+) AND ROL_PRO = EXT_PRO(+) AND ROL_COD = EXT_ROL(+)) as interesados, "+
                                   "(SELECT rtrim (xmlagg (xmlelement (e,  '<FECHA_INICIO>'||NVL(TO_CHAR(CRO_FEI, 'DD/MM/YYYY'), '') ||'</FECHA_INICIO><NOMBRE>'|| TML_VALOR||'</NOMBRE>' || '$')).extract ('//text()'), '$') as valor "+
                                  " FROM E_CRO,E_TML WHERE CRO_MUN = TML_MUN AND CRO_PRO = TML_PRO AND CRO_TRA = TML_TRA AND CRO_NUM =EXP_NUM and cro_fef is null) as tramites "+
                                  " FROM(" + sqlConFiltro + " ORDER BY NOMBRE " + tipoOrden+") CONSULTA" ;
                        } else {
                            
                            sql = "SELECT CONSULTA.*,(SELECT COUNT(*) AS NUM FROM E_EXR WHERE EXR_NUM=EXP_NUM AND EXR_PRO=EXP_PRO AND EXR_MUN=EXP_MUN AND exr_eje=EXP_EJE AND EXR_TOP=0)AS CUENTA,"+
                                    "(SELECT rtrim (xmlagg (xmlelement (e,  EXT_ROL||'#'|| NVL(HTE_PA1, '') || NVL('', '') || NVL(HTE_AP1, '') ||'#'||"+
                                   "NVL(HTE_PA2, '') || NVL('', '') || NVL(HTE_AP2, '') ||'#'||HTE_NOM ||'#'||ROL_COD ||'#'||ROL_DES  || '$')).extract ('//text()'), '$') as valor FROM E_EXT, "+
                                    "T_HTE,E_ROL WHERE EXT_NUM = EXP_NUM AND EXT_TER = HTE_TER(+) AND EXT_NVR = HTE_NVR(+) AND ROL_MUN = EXT_MUN(+) AND ROL_PRO = EXT_PRO(+) AND ROL_COD = EXT_ROL(+)) as interesados, "+
                                    "(SELECT rtrim (xmlagg (xmlelement (e,  '<FECHA_INICIO>'||NVL(TO_CHAR(CRO_FEI, 'DD/MM/YYYY'), '') ||'</FECHA_INICIO><NOMBRE>'|| TML_VALOR||'</NOMBRE>' || '$')).extract ('//text()'), '$') as valor "+
                                    " FROM E_CRO,E_TML WHERE CRO_MUN = TML_MUN AND CRO_PRO = TML_PRO AND CRO_TRA = TML_TRA AND CRO_NUM =EXP_NUM and cro_fef is null) as tramites "+
                                    "FROM(" + sqlConFiltro + "ORDER BY  " + columna + " " + tipoOrden+") CONSULTA ";

                        }
                        
                    }else{
                        if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                            sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY  5 ASC ";
                        } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                        {
                            sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY NOMBRE " + tipoOrden;
                        } else {
                            sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY " + columna + " " + tipoOrden;
                        }
                    } 
                }
//                else if (params[0] != null && (params[0].equals("oracle") || params[0].equals("ORACLE"))) {
//
//
//
//                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
//                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by 5 asc) RN  FROM(" + sqlConFiltro + " ORDER BY 5 ASC) CONSULTA )  resultado where rn between ? and ? order by rn";
//                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
//                    {
//                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
//                    } else {
//                        sql = "select  /*+first_rows(" + lineas + ")*/  * FROM (SELECT consulta.*, ROW_NUMBER() over ( order by " + columna + " " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by " + columna + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
//                    }
//                } else //compatible con sqlserver
//                {
//                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
//                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by (select 5) asc) RN  FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by 5 asc) CONSULTA )  resultado where rn between ? and ? order by rn";
//                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
//                    {
//                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + ") sqlconfiltro order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
//                    } else {
//                        sql = "select resultado.* FROM  (SELECT consulta.*, ROW_NUMBER() over ( order by (select " + columna + ") " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by " + columna + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
//                    }
//                }
                
                m_Log.debug(sql);
                
                String alter1 = null;
                String alter2 = null;
                
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    if(consultaInsensitiva){
                        st.executeQuery(alter1);
                        st.executeQuery(alter2);
                    }
                }
               
               
                    rs = st.executeQuery(sql);
                    
                
                /*******************************************************************************************************************/
                /*******************************************************************************************************************/
                /************************** SE RECUPERA EL NÚMERO MÁXIMO DE EXPEDIENTES A RECUPERAR AL EXPORTAR A PDF/CSV **********/
                /*******************************************************************************************************************/
                /*******************************************************************************************************************/
                
                try{
                    
                    ResourceBundle config = ResourceBundle.getBundle("Expediente");
                    String S_NUM_EXPEDIENTES_MAXIMO_CONSULTA =  config.getString(uVO.getOrgCod() + "/numero_maximo_expedientes_recuperados_exportar_consulta_expedientes");
                    if(S_NUM_EXPEDIENTES_MAXIMO_CONSULTA!=null && !"".equals(S_NUM_EXPEDIENTES_MAXIMO_CONSULTA)){
                        NUM_EXPEDIENTES_MAXIMO_CONSULTA = Integer.parseInt(S_NUM_EXPEDIENTES_MAXIMO_CONSULTA);
                    }
                    
                }catch(Exception e){
                    m_Log.debug("  ERROR AL RECUPERAR LA PROPIEDAD CON EL NÚMERO MAXIMO DE EXPEDIENTES A RECUPERAR EN CONSULTA AL EXPORTAR A PDF/CSV: " + e.getMessage());
                }
                
                int contador = 0;  
                  m_Log.debug("entramos a recorrer los resultados");
                while(rs.next()) {
                    
                    if (consExpVO.isImprimiendo()) {
                        // Se está imprimiendo, entonces se cuenta el número de expedientes.                        
                        if(contador>=NUM_EXPEDIENTES_MAXIMO_CONSULTA){
                            superadoLimiteExpedientesImpresion = true;
                            break;
                        }                        
                    }
                    

                    
                    TramitacionValueObject tvo1 = new TramitacionValueObject();
                   
                     //GUARDAMOS EL USUARIO DE INICIO PARA TENERLO EN LA BUSQUEDA DE EXPEDIENTES
                    tvo1.setUsuarioIni(rs.getString("usu_nom"));
                    tvo1.setLocalizacion(rs.getString("EXP_LOC"));
                    tvo1.setEstado(rs.getString("EXP_EST"));
                    tvo1.setCodMunicipio(rs.getString("EXP_MUN"));
                    tvo1.setCodProcedimiento(rs.getString("EXP_PRO"));
                    tvo1.setDescProcedimiento(rs.getString("PML_VALOR"));
                    tvo1.setEjercicio(rs.getString("EXP_EJE"));
                    tvo1.setNumero(rs.getString("EXP_NUM"));
                    tvo1.setFechaInicioExpediente(rs.getString("fIni"));
                    tvo1.setFechaFinExpediente(rs.getString("fFin"));
                    String asuntoExp = rs.getString("EXP_ASU");
                    if (asuntoExp != null) tvo1.setAsuntoExp(AdaptadorSQLBD.js_escape(asuntoExp));
                    else tvo1.setAsuntoExp("");
                    String unidadIni = rs.getString("UOR_NOM");
                    tvo1.setUnidadInicio(unidadIni);

                   
                    if (params[0] != null && (params[0].equals("oracle") || params[0].equals("ORACLE"))) {
                        int cuenta = rs.getInt("CUENTA");
                        if (cuenta >= 1) {
                            tvo1.setTipoInicio("Instancia");
                        } else {
                            tvo1.setTipoInicio("Oficio");
                        }

                    } else {
                        if (this.isExpedienteIniciadoInstanciaParte(tvo1.getNumero(), tvo1.getCodProcedimiento(), tvo1.getCodMunicipio(), tvo1.getEjercicio(), con)) {
                            tvo1.setTipoInicio("Instancia");
                        } else {
                            tvo1.setTipoInicio("Oficio");
                        }
                    }

                            
                    
                    if (params[0] != null && (params[0].equals("oracle") || params[0].equals("ORACLE"))) {
                        //Recuperar los campos nuevos
                        
                       
                        if (codigoColOrdInteresado.equals(columna)){                            
                            String nombreCompleto = rs.getString("NOMBRE");
                            tvo1.setTitular(funcionTransformaInteresados(rs.getString("interesados"), paraCSV,true,nombreCompleto));
                        }else{

                            tvo1.setTitular(funcionTransformaInteresados(rs.getString("interesados"), paraCSV,false,""));
                        }
   
                        String tramites = rs.getString("tramites");
                        tvo1.setListaTramitesPendientes(tramites);

                    }

                    lista.addElement(tvo1);
                    posE++;
                    contador++;
                    
                    
                }// while


                 if (params[0] != null && (params[0].equals("oracle") || params[0].equals("ORACLE"))) {
                    if (m_Log.isDebugEnabled()) m_Log.debug("Se han recuperado " + lista.size() + " expedientes.");
                    rs.close(); 
                    return lista;
                 }else
                 { //sqlserver 
                       

                        for (int i = 0; i < lista.size(); i++) {
                            TramitacionValueObject tvo1 = lista.elementAt(i);


                            String from = "EXT_ROL, " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA1", "''", "HTE_AP1"}) + " AS ap1, " +
                                    oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA2", "''", "HTE_AP2"}) + " AS ap2, HTE_NOM AS nombre, " + 
                                    "ROL_COD, ROL_DES ";

                            String where = "EXT_MUN = " + tvo1.getCodMunicipio() + " AND EXT_EJE = " + tvo1.getEjercicio() +
                                    " AND EXT_NUM = '" + tvo1.getNumero() + "' ";

                            String[] join = new String[8];

                            if (tvo1.isExpHistorico())
                                join[0] = "HIST_E_EXT";
                            else
                                join[0] = "E_EXT";

                            join[1] = "LEFT";
                            join[2] = "T_HTE";
                            join[3] = "EXT_TER = HTE_TER AND EXT_NVR = HTE_NVR";
                            join[4] = "LEFT";
                            join[5] = "E_ROL";
                            join[6] = "ROL_MUN = EXT_MUN AND ROL_PRO = EXT_PRO AND ROL_COD = EXT_ROL";
                            join[7] = "false";

                            sql = oad.join(from, where, join);

                            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                            rs = st.executeQuery(sql);

                            String titular;
                            if (paraCSV) {
                                titular = "\"";
                                while (rs.next()) {
                                    if (!"\"".equals(titular))
                                        titular += "\r";
                                    //formato del interesado
                                     String ap1=rs.getString("ap1");
                                     String ap2=rs.getString("ap2");
                                     String nombre=rs.getString("nombre");  
                                     titular+=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);

                                     int codRol = rs.getInt("ROL_COD");
                                     String desRol = rs.getString("ROL_DES");
                                     titular += " (" + codRol + " - " + desRol + ")";
                                }
                                titular += "\"";
                            } else {
                                titular = "";
                                while (rs.next()) {
                                    if (!"".equals(titular))
                                        titular += ">>";
                                    //formato del interesado
                                     String ap1=rs.getString("ap1");
                                     String ap2=rs.getString("ap2");
                                     String nombre=rs.getString("nombre");
                                     titular+=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);

                                     int codRol = rs.getInt("ROL_COD");
                                     String desRol = rs.getString("ROL_DES");
                                     titular += ">>(" + codRol + " - " + desRol + ")";
                                }
                            }
                            tvo1.setTitular(titular);
                            rs.close();


                            // Como estamos en consulta, buscaremos los datos referentes a los
                            // tramites abiertos de cada expediente.
                            String condFechaFinTramite;
                            if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = "IS NULL";
                            else condFechaFinTramite = "IS NOT NULL";
                            sql = "SELECT " +
                                    oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FINITRAMITE, " +
                                    oad.convertir("CRO_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FFINTRAMITE, " +
                                    "TML_VALOR " +
                                    "FROM E_CRO, E_TML " +
                                    "WHERE CRO_MUN = TML_MUN AND CRO_PRO = TML_PRO AND CRO_TRA = TML_TRA " +
                                    "AND CRO_NUM = '" + tvo1.getNumero() + "' AND CRO_FEF " + condFechaFinTramite;

                            if (consExpVO.getCodTramite() != null && !"".equals(consExpVO.getCodTramite())&& !"todos".equals(consExpVO.getCodTramite()))
                                sql += " AND CRO_TRA = " + consExpVO.getCodTramite();

                            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                            rs = st.executeQuery(sql);

                            Vector<String> tramitesIniciados = new Vector<String>();
                            Vector<String> fechasInicioTramites = new Vector<String>();
                            Vector<String> fechasFinTramites = new Vector<String>();
                            while (rs.next()) {
                                tramitesIniciados.add(rs.getString("TML_VALOR"));
                                fechasInicioTramites.add(rs.getString("FINITRAMITE"));
                                fechasFinTramites.add(rs.getString("FFINTRAMITE"));
                            }

                            if (!desdeGestion || tramitesIniciados.size() > 0) {
                                tvo1.setListaTramitesAbiertos(tramitesIniciados);
                                tvo1.setListaFechasInicioTramitesAbiertos(fechasInicioTramites);
                                tvo1.setListaFechasFinTramitesAbiertos(fechasFinTramites);
                                listaFinal.addElement(tvo1);
                            }

                            rs.close();


                        }
                 }


                st.close();

            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
            }
            finally {
                try {
                   
                   if(consultaInsensitiva){
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                         Statement st_alter;
                         String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                         String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                         st_alter = con.createStatement();
                         st_alter.executeQuery(alter1);
                         st_alter.executeQuery(alter2);

                         st_alter.close();
                     }
                   }
                    con.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }
        
        
        if(superadoLimiteExpedientesImpresion){
            // Si se ha superado el límite maximo de expedientes a recuperar al exportar a PDF/CSV
            lista.get(0).setExportarConsultaSuperadosLimiteMaximo(true);
            lista.get(0).setNumMaximoExpedientesRecuperados(NUM_EXPEDIENTES_MAXIMO_CONSULTA);
        }
        
        return listaFinal;
    }
          
          
    
          
    private String funcionTransformaInteresados(String interesados, boolean paraCSV, boolean ordenados, String nombreCompleto) {

        String titular = "";
        if (paraCSV) {
            titular = "\"";
            if ((!"".equals(interesados)) &&  (interesados != null)) {

                StringTokenizer st = new StringTokenizer(interesados, "$");
                while (st.hasMoreTokens()) {
                    String interesado = st.nextToken();


                    if (!"\"".equals(titular)) { 
                        titular += "\r";
                    }

                   String[] tokens = interesado.split("#");
                   
                        String rolExp = tokens[0];
                        String ap1 = tokens[1];
                        String ap2 = tokens[2];
                        String nombre = tokens[3];
                        String completo=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);
                        
                       
                        if(((ordenados)&&((completo.replace(" ","").replace(",","")).equals((nombreCompleto.replace(" ","")))))||(!ordenados)){
                            titular += completo;

                            String codRol = tokens[4];
                            String desRol = tokens[5];
                            titular += " (" + codRol + " - " + desRol + ")";
                        }
                    
                }
                titular += "\"";
            }
        } else {
            titular = "";
            if ((!"".equals(interesados)) && (interesados != null)) {
                StringTokenizer st = new StringTokenizer(interesados, "$");
                while (st.hasMoreTokens()) {
                    String interesado = st.nextToken();
                    if (!"".equals(titular)) {
                        titular += ">>";
                    }
                    //formato del interesado

                    String[] tokens = interesado.split("#");
                   
                        String rolExp = tokens[0];
                        String ap1 = tokens[1];
                        String ap2 = tokens[2];
                        String nombre = tokens[3];
                        
                        String completo=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);
                       
                         if(((ordenados)&&((completo.replace(" ","").replace(",","")).equals((nombreCompleto.replace(" ","")))))||(!ordenados)){
                            titular += completo;

                            String codRol = tokens[4];
                            String desRol = tokens[5];
                            titular += ">>(" + codRol + " - " + desRol + ")";
                        }
                        
                     
                    
                }
            }
        }


        return titular;
    }
    
    
    
    
    
    
    /*private Vector<String> funcionTransformaTramitesAbiertos(String tramites) {

        Vector<String> tramitesIniciados = new Vector();

        if ((!"".equals(tramites)) &&  (tramites != null)) {

            StringTokenizer st = new StringTokenizer(tramites, "$");
            while (st.hasMoreTokens()) {
                String tramite = st.nextToken();
                
                 String[] tokens = tramite.split("#");
               
                 String fechaInicio = tokens[0];                    
                 String nombre = tokens[1];                
                 tramitesIniciados.add(nombre);
           }

        }

        return tramitesIniciados;

    }
    
    
     private Vector<String> funcionTransformaFechaTramitesAbiertos(String tramites) {

        Vector<String> fechasTramitesIniciados = new Vector();

        if ((!"".equals(tramites)) &&  (tramites != null)) {

            StringTokenizer st = new StringTokenizer(tramites, "$");
            while (st.hasMoreTokens()) {
                String tramite = st.nextToken();
                String[] tokens = tramite.split("#");
               
                 String fechaInicio = tokens[0];                    
                 String nombre = tokens[1];
                 m_Log.debug(nombre); m_Log.debug(fechaInicio);
                 fechasTramitesIniciados.add(fechaInicio);

            }

        }

        return fechasTramitesIniciados;

    }
*/

     
    public int contarExpedientes(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params,
                            boolean desdeGestion, boolean buscarInicioProc)
    throws AnotacionRegistroException, TechnicalException, TramitacionException {
        
        // Comienzo del metodo.
        m_Log.debug("Comienzo de ConsultaExpedientesDAO.consultar()");

        // Obtener la conexion.
        Connection con = null;
        Statement st;        
        ResultSet rs;
        String sql;
        Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(uVO, params);
        TransformacionAtributoSelect transformador;
        int numRelacionExpedientes = 0;
        if (lUT.size() > 0) {
            // Tiene unidades tramitadoras.

            try {
                AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
                con = oad.getConnection();
                st = con.createStatement();
                
                transformador = new TransformacionAtributoSelect(oad,con);

                /* Comprobar datos del usuario */
                m_Log.debug("--> Codigo Unidad organizativa : " + uVO.getUnidadOrgCod());
                m_Log.debug("--> Unidad organizativa : " + uVO.getUnidadOrg());
                m_Log.debug("--> Codigo departamento(String) : " + uVO.getDep());
                m_Log.debug("--> Codigo departamento(int) : " + uVO.getDepCod());
                m_Log.debug("--> Codigo Ent : " + uVO.getEntCod());
                m_Log.debug("--> Nombre de usuario : " + uVO.getNombreUsu());
                m_Log.debug("--> Identificador de usuario : " + uVO.getIdUsuario());
                m_Log.debug("--> Org (string) : " + uVO.getOrg());
                m_Log.debug("--> Org (int) : " + uVO.getOrgCod());

                
                String sqlConFiltro = ""; 
                        
                if  ("2".equals(consExpVO.getTipoBusqueda()))      
                    sqlConFiltro = obtenerSentenciaConFiltroHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                else if ("1".equals(consExpVO.getTipoBusqueda())) 
                    sqlConFiltro = obtenerSentenciaConFiltroActivosHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                else 
                    sqlConFiltro = obtenerSentenciaConFiltro2(consExpVO, oad, transformador, params, uVO,  buscarInicioProc);


                sql = "SELECT count(*) FROM(" + sqlConFiltro + ") CONSULTA ";

                m_Log.debug(sql);

                String alter1 = null;
                String alter2 = null;

                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    if(consultaInsensitiva){
                        st.executeQuery(alter1);
                        st.executeQuery(alter2);
                    }
                }

                rs = st.executeQuery(sql);                
                if (rs.next()) numRelacionExpedientes = rs.getInt(1);
                // Obtener los resultados.
                
                if (m_Log.isDebugEnabled()) m_Log.debug("Se han recuperado " + numRelacionExpedientes + " expedientes.");
                rs.close();


            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
            }
            finally {
                try {
                    
                    if(consultaInsensitiva){
                        if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                            Statement st_alter;
                            String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                            String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                            st_alter = con.createStatement();
                            st_alter.executeQuery(alter1);
                            st_alter.executeQuery(alter2);

                            st_alter.close();
                        }
                    }
                    
                    con.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }

        return numRelacionExpedientes;
    }
    public Vector consultarPorCampoSuplementario(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params,String columna,String tipoOrden)
            throws AnotacionRegistroException, TechnicalException, TramitacionException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("consultar");

        Connection con = null;
        Statement st;
        ResultSet rs;
        String sql;

        Vector<TramitacionValueObject> listaExpedientes = new Vector<TramitacionValueObject>();
        int cont = 0;
        Vector<TramitacionValueObject> lista = new Vector<TramitacionValueObject>();
        Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(uVO, params);
        TransformacionAtributoSelect transformador;

        String codigoColOrdInteresado=COLUMNA_ORDEN_INTERESADO; //Columna de ordenación por tercero

        if (lUT.size() > 0) // Tiene unidades tramitadoras
        {

            try {
                AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
                transformador = new TransformacionAtributoSelect(oad);
                con = oad.getConnection();

                st = con.createStatement();


                String sqlConFiltro;
                if(codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                {
                      sqlConFiltro = obtenerSentenciaConFiltroCampoSuplementariosByInteresado(consExpVO, oad, transformador, params, uVO);
                }
                else {
                      sqlConFiltro = obtenerSentenciaConFiltroCampoSuplementarios(consExpVO, oad, transformador, params, uVO);
                }

                

                int posE = 0;
                int numPaginaE = 0;
                int pagActual = -1;
                if (!"".equals(consExpVO.getPaginaListado())&&consExpVO.getPaginaListado()!=null) {
                    pagActual = Integer.parseInt(consExpVO.getPaginaListado());
                }
                int lineas = -1;
                if (!"".equals(consExpVO.getNumLineasPaginaListado())&&consExpVO.getNumLineasPaginaListado()!=null) {
                    lineas = Integer.parseInt(consExpVO.getNumLineasPaginaListado());
                }

                if (consExpVO.isImprimiendo()) {
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY  5 ASC ";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY NOMBRE " + tipoOrden;
                    } else {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY " + columna + " " + tipoOrden;
                    }
                } else if (params[0] != null && (params[0].equals("oracle") || params[0].equals("ORACLE"))) {



                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by 5 asc) RN  FROM(" + sqlConFiltro + " ORDER BY 5 ASC) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * FROM (SELECT consulta.*, ROW_NUMBER() over ( order by " + columna + " " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by " + columna + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                } else //compatible con sqlserver
                {
                    // Obtenemos el nombre de la columna para ordenarlo en sqlserver	
                    String nombreColumnaOrdenar = null;	
                    if (!codigoColOrdInteresado.equals(columna)) {	
                        nombreColumnaOrdenar = getNombreColumnaOrdenTabla(columna, params);	
                        // Para que no falle, si no tiene especificado columna en A_CAMPLIST, elegimos por defecto la de numero de expediente	
                        if (StringUtils.isEmpty(nombreColumnaOrdenar)) {	
                            nombreColumnaOrdenar = CONSULTA_EXPEDIENTES_CAMPLIST_NOM_ORDEN_EXP_NUM;	
                        }	
                    }
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by (exp_num) asc) RN  FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by exp_num asc) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + ") sqlconfiltro order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                       sql = "select resultado.* FROM (SELECT consulta.*, ROW_NUMBER() over ( order by (" + nombreColumnaOrdenar + ") " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by " + nombreColumnaOrdenar + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                }
                
                m_Log.debug(sql);
                String alter1 = null;
                String alter2 = null;
                
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    if(consultaInsensitiva){
                        alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                        alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                        st.executeQuery(alter1);
                        st.executeQuery(alter2);
                    }
                   
                }

                if (consExpVO.isImprimiendo()) {
                    rs = st.executeQuery(sql);
                } else {
                    //rs = st.executeQuery(sql);
                    PreparedStatement ps = con.prepareStatement(sql);

                    m_Log.debug("resultado de " + ((lineas * (pagActual - 1)) + 1) + " a " + pagActual * lineas);

                    ps.setInt(1, (lineas * (pagActual - 1)) + 1);
                    ps.setInt(2, pagActual * lineas);

                    rs = ps.executeQuery();
                }

                
                while(rs.next()) {
                    
                    cont++;
                    TramitacionValueObject tvo1 = new TramitacionValueObject();
                    tvo1.setUsuarioIni(rs.getString("usu_nom"));
                    tvo1.setLocalizacion(rs.getString("EXP_LOC"));
                    tvo1.setEstado(rs.getString("EXP_EST"));
                    tvo1.setCodMunicipio(rs.getString("EXP_MUN"));
                    tvo1.setCodProcedimiento(rs.getString("EXP_PRO"));
                    tvo1.setDescProcedimiento(rs.getString("PML_VALOR"));
                    tvo1.setEjercicio(rs.getString("EXP_EJE"));
                    tvo1.setNumero(rs.getString("EXP_NUM"));
                    tvo1.setFechaInicioExpediente(rs.getString("fIni"));
                    tvo1.setFechaFinExpediente(rs.getString("fFin"));
                    String asuntoExp = rs.getString("EXP_ASU");

                    if (asuntoExp != null) tvo1.setAsuntoExp(AdaptadorSQLBD.js_escape(asuntoExp));
                    else tvo1.setAsuntoExp("");

                    String unidadIni = rs.getString("UOR_NOM");
                    tvo1.setUnidadInicio(unidadIni);
                    
                    if(this.isExpedienteIniciadoInstanciaParte(tvo1.getNumero(),tvo1.getCodProcedimiento(),tvo1.getCodMunicipio(),tvo1.getEjercicio(), con)){
                        tvo1.setTipoInicio("Instancia");
                    }else{
                        tvo1.setTipoInicio("Oficio");
                    }
                    
                    /**
                    String TipoInicio = rs.getString("EXR_TOP");
                    if ("0".equals(TipoInicio)) tvo1.setTipoInicio("Instancia");
                    else tvo1.setTipoInicio("Oficio");
                    **/                         
                    lista.addElement(tvo1);
                    posE++;
                }
                if (m_Log.isDebugEnabled()) m_Log.debug("las filas afectados en el select de consultar son : " + cont);
                rs.close();

                for (int i = 0; i < lista.size(); i++) {
                    TramitacionValueObject tvo1 = lista.elementAt(i);
                    String from = "EXT_ROL, " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA1", "''", "HTE_AP1"}) + " AS ap1, " +
                            oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA2", "''", "HTE_AP2"}) + " AS ap2, HTE_NOM AS nombre, " + 
                            "ROL_COD, ROL_DES ";

                    String where = "EXT_MUN = " + tvo1.getCodMunicipio() + " AND EXT_EJE = " + tvo1.getEjercicio() +
                            " AND EXT_NUM = '" + tvo1.getNumero() + "' ";

                    String[] join = new String[8];

                    if (tvo1.isExpHistorico())
                        join[0] = "HIST_E_EXT";
                    else
                        join[0] = "E_EXT";

                    join[1] = "LEFT";
                    join[2] = "T_HTE";
                    join[3] = "EXT_TER = HTE_TER AND EXT_NVR = HTE_NVR";
                    join[4] = "LEFT";
                    join[5] = "E_ROL";
                    join[6] = "ROL_MUN = EXT_MUN AND ROL_PRO = EXT_PRO AND ROL_COD = EXT_ROL";
                    join[7] = "false";

                    sql = oad.join(from, where, join);

                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);

                    String titular = "";
                    while (rs.next()) {
                        if (!"".equals(titular))
                            titular += "<br/>";
                        //formato del interesado
                         String ap1=rs.getString("ap1");
                         String ap2=rs.getString("ap2");
                         String nombre=rs.getString("nombre");  
                         titular+=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);
                         
                         int codRol = rs.getInt("ROL_COD");
                         String desRol = rs.getString("ROL_DES");
                         titular += "<br/>(" + codRol + " - " + desRol + ")";
                    }
                    tvo1.setTitular(titular);
                    rs.close();
                    
                    
                    // Como estamos en consulta, buscaremos los datos referentes a los
                    // tramites abiertos de cada expediente.
                    sql = "SELECT " +
                            oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FINITRAMITE, " +
                            oad.convertir("CRO_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FFINTRAMITE, " +
                            "TML_VALOR " +
                            "FROM E_CRO, E_TML " +
                            "WHERE CRO_MUN = TML_MUN AND CRO_PRO = TML_PRO AND CRO_TRA = TML_TRA " +
                            "AND CRO_NUM = '" + tvo1.getNumero() + "' AND CRO_FEF IS NULL";

                    /*if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);*/
                    //ELIMINAMOS LA EJECUCIÓN DE ESTA CONSULTA. EL EN LISTADO NO NECESITAMOS ESTA INFO. SOLO PARA IMPRIMIR Y SE USA OTRO METODO

                    Vector<String> tramitesIniciados = new Vector<String>();
                    Vector<String> fechasInicioTramites = new Vector<String>();
                    Vector<String> fechasFinTramites = new Vector<String>();

                    /*while (rs.next()) {
                        tramitesIniciados.add(rs.getString("TML_VALOR"));
                        fechasInicioTramites.add(rs.getString("FINITRAMITE"));
                        fechasFinTramites.add(rs.getString("FFINTRAMITE"));
                    }*/
                    //ELIMINAMOS LA EJECUCIÓN DE ESTA CONSULTA. EL EN LISTADO NO NECESITAMOS ESTA INFO. SOLO PARA IMPRIMIR Y SE USA OTRO METODO
                    

                    tvo1.setListaTramitesAbiertos(tramitesIniciados);
                    tvo1.setListaFechasInicioTramitesAbiertos(fechasInicioTramites);
                    tvo1.setListaFechasFinTramitesAbiertos(fechasFinTramites);

                    listaExpedientes.addElement(tvo1);
                    //rs.close();
                    
                     sql="SELECT G_REL.REL_NUM FROM G_EXP, G_REL WHERE G_REL.REL_NUM=G_EXP.REL_NUM AND REL_EST=0 AND EXP_NUM='"+
                            tvo1.getNumero()+"' ";
                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);
                    if (rs.next()) tvo1.setNumeroRelacion(rs.getString("REL_NUM"));

                }

                st.close();

            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
            }
            finally {
                try {
                     if(consultaInsensitiva){
                        if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                           Statement st_alter;
                           String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                           String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                           st_alter = con.createStatement();
                           st_alter.executeQuery(alter1);
                           st_alter.executeQuery(alter2);

                           st_alter.close();
                       }
                     }
                    
                    if (con != null) con.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }

        return listaExpedientes;
    }
    
    
    
    public Vector consultarPorCampoSuplementarioImprimir(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params,String columna,String tipoOrden, boolean paraCSV)
            throws AnotacionRegistroException, TechnicalException, TramitacionException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("consultar");

        Connection con = null;
        Statement st;
        ResultSet rs;
        String sql;

        Vector<TramitacionValueObject> listaExpedientes = new Vector<TramitacionValueObject>();
        int cont = 0;
        Vector<TramitacionValueObject> lista = new Vector<TramitacionValueObject>();
        Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(uVO, params);
        TransformacionAtributoSelect transformador;

        String codigoColOrdInteresado=COLUMNA_ORDEN_INTERESADO; //Columna de ordenación por tercero

        if (lUT.size() > 0) // Tiene unidades tramitadoras
        {

            try {
                AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
                transformador = new TransformacionAtributoSelect(oad);
                con = oad.getConnection();

                st = con.createStatement();


                String sqlConFiltro;
                if(codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                {
                      sqlConFiltro = obtenerSentenciaConFiltroCampoSuplementariosByInteresadoImprimir(consExpVO, oad, transformador, params, uVO);
                }
                else {
                      sqlConFiltro = obtenerSentenciaConFiltroCampoSuplementariosImprimir(consExpVO, oad, transformador, params, uVO);
                }

                

                 int posE = 0;
                int numPaginaE = 0;
                int pagActual = -1;
                if (!"".equals(consExpVO.getPaginaListado())&&consExpVO.getPaginaListado()!=null) {
                    pagActual = Integer.parseInt(consExpVO.getPaginaListado());
                }
                int lineas = -1;
                if (!"".equals(consExpVO.getNumLineasPaginaListado())&&consExpVO.getNumLineasPaginaListado()!=null) {
                    lineas = Integer.parseInt(consExpVO.getNumLineasPaginaListado());
                }

                if (consExpVO.isImprimiendo()) {
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY  5 ASC ";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY NOMBRE " + tipoOrden;
                    } else {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY " + columna + " " + tipoOrden;
                    }
                } else if (params[0] != null && (params[0].equals("oracle") || params[0].equals("ORACLE"))) {



                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by 5 asc) RN  FROM(" + sqlConFiltro + " ORDER BY 5 ASC) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select  from /*+first_rows(" + lineas + ")*/  * (SELECT consulta.*, ROW_NUMBER() over ( order by " + columna + " " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by " + columna + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                } else //compatible con sqlserver
                {
                    	
                    String nombreColumnaOrdenar = null;	
                    if (!codigoColOrdInteresado.equals(columna)) {	
                        nombreColumnaOrdenar = getNombreColumnaOrdenTabla(columna, params);	
                        // Para que no falle, si no tiene especificado columna en A_CAMPLIST, elegimos por defecto la de numero de expediente	
                        if (StringUtils.isEmpty(nombreColumnaOrdenar)) {	
                            nombreColumnaOrdenar = CONSULTA_EXPEDIENTES_CAMPLIST_NOM_ORDEN_EXP_NUM;	
                        }	
                    }
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by (exp_num) asc) RN  FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by exp_num asc) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + ") sqlconfiltro order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                       sql = "select resultado.* FROM (SELECT consulta.*, ROW_NUMBER() over ( order by (" + nombreColumnaOrdenar + ") " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by " + nombreColumnaOrdenar + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                }
                m_Log.debug(sql);
                String alter1 = null;
                String alter2 = null;
                
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                     if(consultaInsensitiva){
                        st.executeQuery(alter1);
                        st.executeQuery(alter2);
                     }
                }
                
                 if (consExpVO.isImprimiendo()) {
                    rs = st.executeQuery(sql);
                } else {
                    //rs = st.executeQuery(sql);
                    PreparedStatement ps = con.prepareStatement(sql);

                    m_Log.debug("resultado de " + ((lineas * (pagActual - 1)) + 1) + " a " + pagActual * lineas);

                    ps.setInt(1, (lineas * (pagActual - 1)) + 1);
                    ps.setInt(2, pagActual * lineas);

                    rs = ps.executeQuery();
                }

                
                while(rs.next()) {
                    
                    cont++;
                    TramitacionValueObject tvo1 = new TramitacionValueObject();
                    Vector<String> tramitesIniciados = new Vector<String>();
                    Vector<String> fechasInicioTramites = new Vector<String>();
                    Vector<String> fechasFinTramites = new Vector<String>();
                    tvo1.setUsuarioIni(rs.getString("usu_nom"));
                    tvo1.setLocalizacion(rs.getString("EXP_LOC"));
                    tvo1.setEstado(rs.getString("EXP_EST"));
                    tvo1.setCodMunicipio(rs.getString("EXP_MUN"));
                    tvo1.setCodProcedimiento(rs.getString("EXP_PRO"));
                    tvo1.setDescProcedimiento(rs.getString("PML_VALOR"));
                    tvo1.setEjercicio(rs.getString("EXP_EJE"));
                    tvo1.setNumero(rs.getString("EXP_NUM"));
                    tvo1.setFechaInicioExpediente(rs.getString("fIni"));
                    tvo1.setFechaFinExpediente(rs.getString("fFin"));
                    String asuntoExp = rs.getString("EXP_ASU");

                    if (asuntoExp != null) tvo1.setAsuntoExp(AdaptadorSQLBD.js_escape(asuntoExp));
                    else tvo1.setAsuntoExp("");

                    String unidadIni = rs.getString("UOR_NOM");
                    tvo1.setUnidadInicio(unidadIni);
                    
                    if(this.isExpedienteIniciadoInstanciaParte(tvo1.getNumero(),tvo1.getCodProcedimiento(),tvo1.getCodMunicipio(),tvo1.getEjercicio(), con)){
                        tvo1.setTipoInicio("Instancia");
                    }else{
                        tvo1.setTipoInicio("Oficio");
                    }
                    
                    
                    
                 

                    lista.addElement(tvo1);
                    posE++;
                   
                }
                if (m_Log.isDebugEnabled()) m_Log.debug("las filas afectados en el select de consultar son : " + cont);
                
                for (int i = 0; i < lista.size(); i++) {
                    TramitacionValueObject tvo1 = lista.elementAt(i);
                  
                    String from = "EXT_ROL, " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA1", "''", "HTE_AP1"}) + " AS ap1, " +
                            oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA2", "''", "HTE_AP2"}) + " AS ap2, HTE_NOM AS nombre, " + 
                            "ROL_COD, ROL_DES ";

                    String where = "EXT_MUN = " + tvo1.getCodMunicipio() + " AND EXT_EJE = " + tvo1.getEjercicio() +
                            " AND EXT_NUM = '" + tvo1.getNumero() + "' AND MOSTRAR = 1";

                    String[] join = new String[8];

                    if (tvo1.isExpHistorico())
                        join[0] = "HIST_E_EXT";
                    else
                        join[0] = "E_EXT";

                    join[1] = "LEFT";
                    join[2] = "T_HTE";
                    join[3] = "EXT_TER = HTE_TER AND EXT_NVR = HTE_NVR";
                    join[4] = "LEFT";
                    join[5] = "E_ROL";
                    join[6] = "ROL_MUN = EXT_MUN AND ROL_PRO = EXT_PRO AND ROL_COD = EXT_ROL";
                    join[7] = "false";

                    sql = oad.join(from, where, join);

                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);

                    String titular;
                    if (paraCSV) {
                        titular = "\"";
                        while (rs.next()) {
                            if (!"\"".equals(titular))
                                titular += "\r";
                            //formato del interesado
                             String ap1=rs.getString("ap1");
                             String ap2=rs.getString("ap2");
                             String nombre=rs.getString("nombre");  
                             titular+=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);

                             int codRol = rs.getInt("ROL_COD");
                             String desRol = rs.getString("ROL_DES");
                             titular += " (" + codRol + " - " + desRol + ")";
                        }
                        titular += "\"";
                    } else {
                        titular = "";
                        while (rs.next()) {
                            if (!"".equals(titular))
                                titular += ">>";
                            //formato del interesado
                             String ap1=rs.getString("ap1");
                             String ap2=rs.getString("ap2");
                             String nombre=rs.getString("nombre");
                             titular+=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);

                             int codRol = rs.getInt("ROL_COD");
                             String desRol = rs.getString("ROL_DES");
                             titular += ">>(" + codRol + " - " + desRol + ")";
                        }
                    }
                    tvo1.setTitular(titular);
                    rs.close();
                    
                    // Como estamos en consulta, buscaremos los datos referentes a los
                    // tramites abiertos de cada expediente.
                    String condFechaFinTramite;
                    if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = "IS NULL";
                    else condFechaFinTramite = "IS NOT NULL";
                    sql = "SELECT " +
                            oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FINITRAMITE, " +
                            oad.convertir("CRO_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FFINTRAMITE, " +
                            "TML_VALOR " +
                            "FROM E_CRO, E_TML " +
                            "WHERE CRO_MUN = TML_MUN AND CRO_PRO = TML_PRO AND CRO_TRA = TML_TRA " +
                            "AND CRO_NUM = '" + tvo1.getNumero() + "' AND CRO_FEF " + condFechaFinTramite;

                    if (consExpVO.getCodTramite() != null && !"".equals(consExpVO.getCodTramite())&& !"todos".equals(consExpVO.getCodTramite()))
                        sql += " AND CRO_TRA = " + consExpVO.getCodTramite();
                    
                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);

                    Vector<String> tramitesIniciados = new Vector<String>();
                    Vector<String> fechasInicioTramites = new Vector<String>();
                    Vector<String> fechasFinTramites = new Vector<String>();
                    while (rs.next()) {
                        tramitesIniciados.add(rs.getString("TML_VALOR"));
                        fechasInicioTramites.add(rs.getString("FINITRAMITE"));
                        fechasFinTramites.add(rs.getString("FFINTRAMITE"));
                    }

                    if (  tramitesIniciados.size() > 0) {
                        tvo1.setListaTramitesAbiertos(tramitesIniciados);
                        tvo1.setListaFechasInicioTramitesAbiertos(fechasInicioTramites);
                        tvo1.setListaFechasFinTramitesAbiertos(fechasFinTramites);
                        
                    }
                    listaExpedientes.addElement(tvo1);

                    rs.close();

                    
                }
                
                rs.close();
                st.close();

            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
            }
            finally {
                try {
                     if(consultaInsensitiva){
                        if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                           Statement st_alter;
                           String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                           String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                           st_alter = con.createStatement();
                           st_alter.executeQuery(alter1);
                           st_alter.executeQuery(alter2);

                           st_alter.close();
                       }
                     }
                    if (con != null) con.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }

        return listaExpedientes;
    }
    
    
    public int contarPorCampoSuplementario(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params,String columna,String tipoOrden)
            throws AnotacionRegistroException, TechnicalException, TramitacionException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("contar");

        Connection con = null;
        Statement st;
        ResultSet rs;
        String sql;
        int numRelacionExpedientes = 0;
        Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(uVO, params);
        TransformacionAtributoSelect transformador;

        if (lUT.size() > 0) // Tiene unidades tramitadoras
        {

            try {
                AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
                transformador = new TransformacionAtributoSelect(oad);
                con = oad.getConnection();

                st = con.createStatement();

                String sqlConFiltro = obtenerSentenciaConFiltroCampoSuplementarios(consExpVO, oad, transformador, params, uVO);
                sql = "SELECT count(*) FROM(" + sqlConFiltro + ") CONSULTA ";

                if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                String alter1 = null;
                String alter2 = null;

                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    if(consultaInsensitiva){
                    st.executeQuery(alter1);
                    st.executeQuery(alter2);
                    }
                }
                rs = st.executeQuery(sql);
                if (rs.next()) numRelacionExpedientes = rs.getInt(1);
                // Obtener los resultados.

                if (m_Log.isDebugEnabled()) m_Log.debug("Se han recuperado " + numRelacionExpedientes + " expedientes.");
                rs.close();

                st.close();

            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
            }
            finally {
                try {
                    if(consultaInsensitiva){
                        if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                            Statement st_alter;
                            String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                            String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                            st_alter = con.createStatement();
                            st_alter.executeQuery(alter1);
                            st_alter.executeQuery(alter2);

                            st_alter.close();
                        }
                    }
                    
                    if (con != null) con.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }

        return numRelacionExpedientes;
    }

     public Vector getExpedientesRelacionados(ConsultaExpedientesValueObject consExpVO, String[] params)
            throws AnotacionRegistroException, TechnicalException, TramitacionException {
        
        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getExpedientesRelacionados");

        Connection con = null;
        Statement st;
        ResultSet rs;
        String sql;
        int cont = 0;
        Vector<ConsultaExpedientesValueObject> listaExpRel = new Vector<ConsultaExpedientesValueObject>();
        Vector<ConsultaExpedientesValueObject> resultado = new Vector<ConsultaExpedientesValueObject>();
        
        try {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            sql = "SELECT REX_MUNR, REX_EJER, REX_NUMR " +
                    "FROM E_REX " +
                    "WHERE REX_MUN = " + consExpVO.getCodMunicipio() + " AND REX_EJE = " + consExpVO.getEjercicio() + " " +
                    "AND REX_NUM = '" + consExpVO.getNumeroExpediente() + "' ";

            if (m_Log.isDebugEnabled()) m_Log.debug("getExpedientesRelacionados : " + sql);
            rs = st.executeQuery(sql);
            while (rs.next()) {
                cont++;
                ConsultaExpedientesValueObject cEVO = new ConsultaExpedientesValueObject();
                String codMunicipioRel = rs.getString("REX_MUNR");
                cEVO.setCodMunicipio(codMunicipioRel);
                String ejercicioRel = rs.getString("REX_EJER");
                cEVO.setEjercicio(ejercicioRel);
                String numeroExpedienteRel = rs.getString("REX_NUMR");
                cEVO.setNumeroExpediente(numeroExpedienteRel);
                listaExpRel.addElement(cEVO);
            }
            rs.close();
            if (m_Log.isDebugEnabled())
                m_Log.debug("las filas afectados en el select de getExpedientesRelacionados son : " + cont);
            
            resultado.addAll(getExpedientesRelacionadosDif(consExpVO,listaExpRel,true,params));
            resultado.addAll(getExpedientesRelacionadosDif(consExpVO,listaExpRel,false,params));
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.ConsultaExpedientesDAO.getExpedientesRelacionados"), e);
        }
        finally {
            try {
                if (con != null) con.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }
            
        return resultado;
        
    }

    public Vector getExpedientesRelacionadosDif(ConsultaExpedientesValueObject consExpVO, 
            Vector<ConsultaExpedientesValueObject> listaExpRel, boolean expHistorico, String[] params)
            throws AnotacionRegistroException, TechnicalException, TramitacionException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getExpedientesRelacionados");

        Connection con = null;
        Statement st;
        ResultSet rs = null;
        String sql;
        String from;
        String where;
        Vector<ConsultaExpedientesValueObject> lista = new Vector<ConsultaExpedientesValueObject>();
        Vector<ConsultaExpedientesValueObject> listaFinal = new Vector<ConsultaExpedientesValueObject>();

        try {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            from = "EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, " +
                    oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni " + "," +
                    oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                    "PML_VALOR";
            String[] join = new String[5];
            if (expHistorico)
                join[0] = "HIST_E_EXP";
            else
                join[0] = "E_EXP";
            join[1] = "INNER";
            join[2] = "e_pml";
            join[3] = "EXP_MUN = PML_MUN AND EXP_PRO = PML_COD";
            join[4] = "false";

            for (int i = 0; i < listaExpRel.size(); i++) {
                ConsultaExpedientesValueObject cEVO = listaExpRel.elementAt(i);
                where = "PML_CMP = 'NOM' AND PML_LENG = '"+m_ConfigTechnical.getString("idiomaDefecto")+
                        "' AND EXP_MUN = " + cEVO.getCodMunicipio() + " " +
                        "AND EXP_EJE = " + cEVO.getEjercicio() + " AND EXP_NUM = '" + cEVO.getNumeroExpediente() + "'";
                sql = oad.join(from, where, join);
                String parametros[] = {"5", "5"};
                sql += oad.orderUnion(parametros);
                if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs = st.executeQuery(sql);
                while (rs.next()) {
                    ConsultaExpedientesValueObject ceVO = new ConsultaExpedientesValueObject();
                    String codMunicipioRel = rs.getString("EXP_MUN");
                    ceVO.setCodMunicipioRel(codMunicipioRel);
                    String codProcedimiento = rs.getString("EXP_PRO");
                    ceVO.setCodProcedimiento(codProcedimiento);
                    String descProcedimiento = rs.getString("PML_VALOR");
                    ceVO.setDescProcedimiento(descProcedimiento);
                    String ejercicioRel = rs.getString("EXP_EJE");
                    ceVO.setEjercicioRel(ejercicioRel);
                    String numeroExpedienteRel = rs.getString("EXP_NUM");
                    ceVO.setNumeroExpedienteRel(numeroExpedienteRel);
                    String fechaInicio = rs.getString("fIni");
                    ceVO.setFechaInicio(fechaInicio);
                    String fechaFin = rs.getString("fFin");
                    ceVO.setFechaFin(fechaFin);
                    ceVO.setCodMunicipio(consExpVO.getCodMunicipio());
                    ceVO.setEjercicio(consExpVO.getEjercicio());
                    ceVO.setNumeroExpediente(consExpVO.getNumeroExpediente());
                    ceVO.setExpHistorico(expHistorico);
                    lista.addElement(ceVO);
                }
            }

            for (int i = 0; i < lista.size(); i++) {
                ConsultaExpedientesValueObject ceVO = lista.elementAt(i);

                int codRolPD = -1;
                sql = "SELECT ROL_COD FROM E_ROL WHERE ROL_MUN = " + ceVO.getCodMunicipioRel() +
                        " AND ROL_PRO = '" + ceVO.getCodProcedimiento() + "' AND ROL_PDE = 1";

                rs = st.executeQuery(sql);
                while (rs.next()) {
                    codRolPD = rs.getInt("ROL_COD");
                }

                from = "EXT_ROL, " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                        new String[]{"HTE_PA1", "' '", "HTE_AP1", "' '", "HTE_PA2", "' '", "HTE_AP2",
                                " (CASE WHEN HTE_PA1 IS NOT NULL OR HTE_AP1 IS NOT NULL OR HTE_PA2 IS NOT NULL " +
                                        "OR HTE_AP2 IS NOT NULL THEN ',' ELSE '' END)", "HTE_NOM"}) + " AS titular";

                join = new String[5];
                if (expHistorico)
                    join[0] = "HIST_E_EXT";
                else
                    join[0] = "E_EXT";
                join[1] = "LEFT";
                join[2] = "T_HTE";
                join[3] = "EXT_TER = HTE_TER AND EXT_NVR = HTE_NVR";
                join[4] = "false";

                where = "EXT_MUN = " + ceVO.getCodMunicipioRel() + " AND EXT_EJE = " + ceVO.getEjercicioRel() + " " +
                        "AND EXT_NUM = '" + ceVO.getNumeroExpedienteRel() + "'";

                sql = oad.join(from, where, join);

                if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs = st.executeQuery(sql);


                String entrar = "no";
                while (rs.next()) {
                    int rol = rs.getInt("EXT_ROL");
                    if (rol == codRolPD) {
                        if (entrar.equals("no")) {
                            ceVO.setTitular(rs.getString("titular"));
                            entrar = "si";
                        }
                    } else {
                        if (entrar.equals("no")) {
                            String titular = rs.getString("titular");
                            if ("  , ".equals(titular)) ceVO.setTitular("");
                            else ceVO.setTitular(titular);
                        }
                    }
                }
                listaFinal.addElement(ceVO);
            }

            if (rs!=null)
                rs.close();
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.ConsultaExpedientesDAO.getExpedientesRelacionados"), e);
        }
        finally {
            try {
                if (con != null) con.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }

        return listaFinal;
    }

    public int insertExpedientesRelacionados(ConsultaExpedientesValueObject consExpVO, String[] params)
            throws TechnicalException, BDException, AnotacionRegistroException {

        int res = 0;
        int cont = 0;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        ResultSet rs;
        Statement st;

        try {

            if (m_Log.isDebugEnabled()) m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            if (m_Log.isDebugEnabled()) m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            st = conexion.createStatement();
            String grabar = "si";

            if (consExpVO.getCodMunicipioIni().equals(consExpVO.getCodMunicipio()) &&
                    consExpVO.getEjercicioIni().equals(consExpVO.getEjercicio()) &&
                    consExpVO.getNumeroExpedienteIni().equals(consExpVO.getNumeroExpediente())) {
                grabar = "no";
            }

            String sql = "SELECT REX_NUM " +
                    "FROM E_REX " +
                    "WHERE REX_MUN = " + consExpVO.getCodMunicipioIni() + " " +
                    "AND REX_EJE = " + consExpVO.getEjercicioIni() + " " +
                    "AND REX_NUM = '" + consExpVO.getNumeroExpedienteIni() + "' " +
                    "AND REX_MUNR = " + consExpVO.getCodMunicipio() + " " +
                    "AND REX_EJER = " + consExpVO.getEjercicio() + " " +
                    "AND REX_NUMR = '" + consExpVO.getNumeroExpediente() + "'";
            rs = st.executeQuery(sql);
            while (rs.next()) {
                cont++;
            }
            rs.close();
            st.close();
            if (cont > 0) {
                grabar = "no";
            }

            if ("si".equals(grabar)) {
                sql = "INSERT INTO E_REX (REX_MUN, REX_EJE, REX_NUM, REX_MUNR, REX_EJER, REX_NUMR) " +
                        "VALUES(" + consExpVO.getCodMunicipioIni() + ", " + consExpVO.getEjercicioIni() + ", " +
                        "'" + consExpVO.getNumeroExpedienteIni() + "', " + consExpVO.getCodMunicipio() + ", " +
                        consExpVO.getEjercicio() + ", '" + consExpVO.getNumeroExpediente() + "')";

                if (m_Log.isDebugEnabled()) m_Log.debug(sql);

                PreparedStatement ps = conexion.prepareStatement(sql);
                res = ps.executeUpdate();
                if (m_Log.isDebugEnabled())
                    m_Log.debug("las filas afectadas en el insert  de E_REX son :::::::::::::: : " + res);

                ps.close();

                sql = "INSERT INTO E_REX (REX_MUN, REX_EJE, REX_NUM, REX_MUNR, REX_EJER, REX_NUMR) " +
                        "VALUES(" + consExpVO.getCodMunicipio() + ", " + consExpVO.getEjercicio() + ", " +
                        "'" + consExpVO.getNumeroExpediente() + "', " + consExpVO.getCodMunicipioIni() + ", " +
                        consExpVO.getEjercicioIni() + ", '" + consExpVO.getNumeroExpedienteIni() + "')";

                if (m_Log.isDebugEnabled()) m_Log.debug(sql);

                ps = conexion.prepareStatement(sql);
                res = ps.executeUpdate();
                if (m_Log.isDebugEnabled())
                    m_Log.debug("las filas afectadas en el insert  de E_REX son (reciproca) :::::::::::::: : " + res);
                ps.close();


            } else {
                res = 1;
            }

            abd.finTransaccion(conexion);

        } catch (Exception ex) {
            abd.rollBack(conexion);
            ex.printStackTrace();
            m_Log.error("error al conectar en el metodo insert");
        } finally {
            try {
                abd.devolverConexion(conexion);
            } catch (Exception ex) {
                ex.printStackTrace();
                m_Log.error("Exception: " + ex.getMessage());
            }
        }
        return res;
    }

    public int eliminarExpedientesRelacionados(ConsultaExpedientesValueObject consExpVO, String[] params)
            throws TechnicalException, BDException, AnotacionRegistroException {

        int res = 0;
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection conexion = null;

        try {

            if (m_Log.isDebugEnabled()) m_Log.debug("A por la conexion");
            conexion = abd.getConnection();
            abd.inicioTransaccion(conexion);

            String sql = "DELETE FROM E_REX " +
                    "WHERE REX_MUN = " + consExpVO.getCodMunicipioIni() + " " +
                    "AND REX_EJE = " + consExpVO.getEjercicioIni() + " " +
                    "AND REX_NUM = '" + consExpVO.getNumeroExpedienteIni() + "' " +
                    "AND REX_MUNR = " + consExpVO.getCodMunicipio() + " " +
                    "AND REX_EJER = " + consExpVO.getEjercicio() + " " +
                    "AND REX_NUMR = '" + consExpVO.getNumeroExpediente() + "'";
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);

            PreparedStatement ps = conexion.prepareStatement(sql);
            res = ps.executeUpdate();
            if (m_Log.isDebugEnabled())
                m_Log.debug("las filas afectadas en el eliminar  de E_REX son :::::::::::::: : " + res);

            ps.close();

            sql = "DELETE FROM E_REX " +
                    "WHERE REX_MUN = " + consExpVO.getCodMunicipio() + " " +
                    "AND REX_EJE = " + consExpVO.getEjercicio() + " " +
                    "AND REX_NUM = '" + consExpVO.getNumeroExpediente() + "' " +
                    "AND REX_MUNR = " + consExpVO.getCodMunicipioIni() + " " +
                    "AND REX_EJER = " + consExpVO.getEjercicioIni() + " " +
                    "AND REX_NUMR = '" + consExpVO.getNumeroExpedienteIni() + "'";

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps = conexion.prepareStatement(sql);
            res = ps.executeUpdate();
            if (m_Log.isDebugEnabled())
                m_Log.debug("las filas afectadas en el eliminar  de E_REX son (reciproca) :::::::::::::: : " + res);
            ps.close();

            abd.finTransaccion(conexion);

        } catch (Exception ex) {
            abd.rollBack(conexion);
            ex.printStackTrace();
            m_Log.error("error al conectar en el metodo insert");
        } finally {
            if (conexion != null)
                try {
                    abd.devolverConexion(conexion);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    m_Log.error("Exception: " + ex.getMessage());
                }
        }
        return res;
    }

    public boolean esPendienteParaUsuario(UsuarioValueObject usuario, ConsultaExpedientesValueObject consExpVO,String[] params)
             throws TechnicalException, BDException, AnotacionRegistroException, TramitacionException {
        boolean esPendiente = false;
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection conexion = null;
        //Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(usuario, params);
         String sql;
        if (consExpVO.isExpHistorico())
            sql = "SELECT /*+ index(HIST_E_EXP) index(HIST_E_CRO) index(e_pro) index(e_pml) index(a_uor)*/ HIST_E_EXP.EXP_NUM FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU, A_UOR,HIST_E_EXP " +
            "LEFT JOIN HIST_E_CRO ON (HIST_E_EXP.EXP_MUN = CRO_MUN AND HIST_E_EXP.EXP_EJE = CRO_EJE AND HIST_E_EXP.EXP_NUM = CRO_NUM AND CRO_FEF IS NULL)"  +
            "INNER JOIN E_PML ON (HIST_E_EXP.EXP_MUN = PML_MUN AND HIST_E_EXP.EXP_PRO = PML_COD)" +
            "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (UOU_UOR = CRO_UTR AND UOU_USU = ? AND UOU_ORG = ? AND UOU_ENT = ?)"  +
            "WHERE EXP_NUM = ? AND USU_COD= ? AND UOR_COD=EXP_UOR AND PML_CMP='NOM'  AND PML_LENG='"+m_ConfigTechnical.getString("idiomaDefecto")+"'  AND EXP_EST = 0  " +
            "AND ((exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                            "WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=EXP_UOR)) OR " +
                 "(exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                            "WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=CRO_UTR))  AND CRO_FEF IS NULL)" +
           "GROUP BY HIST_E_EXP.EXP_MUN, HIST_E_EXP.EXP_PRO, HIST_E_EXP.EXP_EJE, HIST_E_EXP.EXP_NUM, PML_VALOR, HIST_E_EXP.EXP_FEI, HIST_E_EXP.EXP_FEF, HIST_E_EXP.EXP_EST,UOR_NOM,USU_NOM";
        else
            sql = "SELECT /*+ index(e_exp) index(e_cro) index(e_pro) index(e_pml) index(a_uor)*/ E_EXP.EXP_NUM FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU, A_UOR,E_EXP " +
            "LEFT JOIN E_CRO ON (E_EXP.EXP_MUN = CRO_MUN AND E_EXP.EXP_EJE = CRO_EJE AND E_EXP.EXP_NUM = CRO_NUM AND CRO_FEF IS NULL)"  +
            "INNER JOIN E_PML ON (E_EXP.EXP_MUN = PML_MUN AND E_EXP.EXP_PRO = PML_COD)" +
            "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (UOU_UOR = CRO_UTR AND UOU_USU = ? AND UOU_ORG = ? AND UOU_ENT = ?)"  +
            "WHERE EXP_NUM = ? AND USU_COD= ? AND UOR_COD=EXP_UOR AND PML_CMP='NOM'  AND PML_LENG='"+m_ConfigTechnical.getString("idiomaDefecto")+"'  AND EXP_EST = 0  " +
            "AND ((exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                            "WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=EXP_UOR)) OR " +
                 "(exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                            "WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=CRO_UTR))  AND CRO_FEF IS NULL)" +
           "GROUP BY E_EXP.EXP_MUN, E_EXP.EXP_PRO, E_EXP.EXP_EJE, E_EXP.EXP_NUM, PML_VALOR, E_EXP.EXP_FEI, E_EXP.EXP_FEF, E_EXP.EXP_EST,UOR_NOM,USU_NOM";
        
        m_Log.debug("esPendienteParaUsuario: " + sql);
        try {
            conexion = abd.getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            m_Log.debug("Numero de expediente: " + consExpVO.getNumeroExpediente());
            m_Log.debug("Codigo de Usuario: " + usuario.getIdUsuario());
            m_Log.debug("Organizacion de Usuario: " + usuario.getOrgCod());
            m_Log.debug("Entidad de Usuario: " + usuario.getEntCod());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());
            ps.setString(i++, consExpVO.getNumeroExpediente());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());

            ResultSet rs = ps.executeQuery();
            esPendiente = rs.next();
        } catch (Exception ex) {
            abd.rollBack(conexion);
            ex.printStackTrace();            
        } finally {
            if (conexion != null)
                try {
                    abd.devolverConexion(conexion);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    m_Log.error("Exception: " + ex.getMessage());
                }
        }
        
        return esPendiente;
    }


 public boolean permiteModificarObservacionesUsuario (UsuarioValueObject usuario, ConsultaExpedientesValueObject consExpVO,Connection con)
             throws TechnicalException, BDException, AnotacionRegistroException, TramitacionException {
        
       boolean permiteModificarObservaciones = false;
       PreparedStatement ps = null;
       ResultSet rs = null;
       String sql;
       
       if (consExpVO.isExpHistorico()) {           
           /** ORIGINAL
            sql = "SELECT /*+ index(HIST_E_EXP) index(HIST_E_CRO) index(e_pro) index(e_pml) index(a_uor)* HIST_E_EXP.EXP_NUM FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU, A_UOR,HIST_E_EXP " +
                "LEFT JOIN HIST_E_CRO ON (HIST_E_EXP.EXP_MUN = CRO_MUN AND HIST_E_EXP.EXP_EJE = CRO_EJE AND HIST_E_EXP.EXP_NUM = CRO_NUM AND CRO_FEF IS NULL)"  +
                "INNER JOIN E_PML ON (HIST_E_EXP.EXP_MUN = PML_MUN AND HIST_E_EXP.EXP_PRO = PML_COD)" +
                "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (UOU_UOR = CRO_UTR AND UOU_USU = ? AND UOU_ORG = ? AND UOU_ENT = ?)"  +
                "WHERE EXP_NUM = ? AND USU_COD= ? AND UOR_COD=EXP_UOR AND PML_CMP='NOM'  AND PML_LENG='"+m_ConfigTechnical.getString("idiomaDefecto")+"'  AND (EXP_EST = 9 OR EXP_EST=1)  " +
                "AND ((exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                                "WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=EXP_UOR)) OR " +
                     "(exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                                "WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=CRO_UTR))  AND CRO_FEF IS NULL)" +
               "GROUP BY HIST_E_EXP.EXP_MUN, HIST_E_EXP.EXP_PRO, HIST_E_EXP.EXP_EJE, HIST_E_EXP.EXP_NUM, PML_VALOR, HIST_E_EXP.EXP_FEI, HIST_E_EXP.EXP_FEF, HIST_E_EXP.EXP_EST,UOR_NOM,USU_NOM";
           */
           sql ="SELECT COUNT(*) AS NUM FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU, A_UOR,HIST_E_EXP " + 
                "LEFT JOIN HIST_E_CRO ON (HIST_E_EXP.EXP_MUN = CRO_MUN AND HIST_E_EXP.EXP_EJE = CRO_EJE AND HIST_E_EXP.EXP_NUM = CRO_NUM AND CRO_FEF IS NULL) " + 
                "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (UOU_UOR = CRO_UTR AND UOU_USU = ? AND UOU_ORG = ? AND UOU_ENT = ?) " + 
                "WHERE EXP_NUM=? AND USU_COD= ? AND UOR_COD=EXP_UOR AND (EXP_EST = 9 OR EXP_EST=1) " + 
                "AND ((exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " + 
                "WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=EXP_UOR)) OR " + 
                "(exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " + 
                "WHERE UOU_USU=? AND UOU_ORG=? AND UOU_ENT=? AND UOU_UOR=CRO_UTR)) AND CRO_FEF IS NULL)";
       }
       else {
           
           /*
            sql = "SELECT /*+ index(e_exp) index(e_cro) index(e_pro) index(e_pml) index(a_uor)* E_EXP.EXP_NUM FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU, A_UOR,E_EXP " +
                "LEFT JOIN E_CRO ON (E_EXP.EXP_MUN = CRO_MUN AND E_EXP.EXP_EJE = CRO_EJE AND E_EXP.EXP_NUM = CRO_NUM AND CRO_FEF IS NULL)"  +
                "INNER JOIN E_PML ON (E_EXP.EXP_MUN = PML_MUN AND E_EXP.EXP_PRO = PML_COD)" +
                "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (UOU_UOR = CRO_UTR AND UOU_USU = ? AND UOU_ORG = ? AND UOU_ENT = ?)"  +
                "WHERE EXP_NUM = ? AND USU_COD= ? AND UOR_COD=EXP_UOR AND PML_CMP='NOM'  AND PML_LENG='"+m_ConfigTechnical.getString("idiomaDefecto")+"'  AND (EXP_EST = 9 OR EXP_EST=1)  " +
                "AND ((exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                                "WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=EXP_UOR)) OR " +
                     "(exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                                "WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=CRO_UTR))  AND CRO_FEF IS NULL)" +
               "GROUP BY E_EXP.EXP_MUN, E_EXP.EXP_PRO, E_EXP.EXP_EJE, E_EXP.EXP_NUM, PML_VALOR, E_EXP.EXP_FEI, E_EXP.EXP_FEF, E_EXP.EXP_EST,UOR_NOM,USU_NOM";
               **/           
           sql ="SELECT COUNT(*) AS NUM FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU, A_UOR,E_EXP " + 
                "LEFT JOIN E_CRO ON (E_EXP.EXP_MUN = CRO_MUN AND E_EXP.EXP_EJE = CRO_EJE AND E_EXP.EXP_NUM = CRO_NUM AND CRO_FEF IS NULL) " + 
                "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (UOU_UOR = CRO_UTR AND UOU_USU = ? AND UOU_ORG = ? AND UOU_ENT = ?) " + 
                "WHERE EXP_NUM=? AND USU_COD= ? AND UOR_COD=EXP_UOR AND (EXP_EST = 9 OR EXP_EST=1) " + 
                "AND ((exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " + 
                "WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=EXP_UOR)) OR " + 
                "(exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " + 
                "WHERE UOU_USU=? AND UOU_ORG=? AND UOU_ENT=? AND UOU_UOR=CRO_UTR)) AND CRO_FEF IS NULL)";
       }
        
        m_Log.debug("permiteModificarObservacionesUsiario: " + sql);
        
        try {            
            ps = con.prepareStatement(sql);
            int i = 1;
            m_Log.debug("Numero de expediente: " + consExpVO.getNumeroExpediente());
            m_Log.debug("Codigo de Usuario: " + usuario.getIdUsuario());
            m_Log.debug("Organizacion de Usuario: " + usuario.getOrgCod());
            m_Log.debug("Entidad de Usuario: " + usuario.getEntCod());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());
            ps.setString(i++, consExpVO.getNumeroExpediente());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());

            rs = ps.executeQuery();
            permiteModificarObservaciones = rs.next();
            
        } catch (Exception ex) {            
            ex.printStackTrace();
        } finally {            
            try {
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                m_Log.error("Exception: " + ex.getMessage());
            }
        }

        return permiteModificarObservaciones;
    }
    


    
    public Integer estadoExpModifObservacionesUsuario (UsuarioValueObject usuario, ConsultaExpedientesValueObject consExpVO,String[] params)
             throws TechnicalException, BDException, AnotacionRegistroException, TramitacionException {
        
        Integer estadoExp = null;
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection conexion = null;
        Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(usuario, params);
        String sql = "SELECT E_EXP.EXP_EST FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU, A_UOR,E_EXP " +
        "LEFT JOIN E_CRO ON (E_EXP.EXP_MUN = CRO_MUN AND E_EXP.EXP_EJE = CRO_EJE AND E_EXP.EXP_NUM = CRO_NUM AND CRO_FEF IS NULL)"  +
        "INNER JOIN E_PML ON (E_EXP.EXP_MUN = PML_MUN AND E_EXP.EXP_PRO = PML_COD)" +
        "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (UOU_UOR = CRO_UTR AND UOU_USU = ? AND UOU_ORG = ? AND UOU_ENT = ?)"  +
        "WHERE EXP_NUM = ? AND USU_COD= ? AND UOR_COD=EXP_UOR AND PML_CMP='NOM'  AND PML_LENG='"+m_ConfigTechnical.getString("idiomaDefecto")+"'  AND (EXP_EST=0 OR EXP_EST=1 OR EXP_EST=9)  " +
	"AND ((exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
			"WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=EXP_UOR)) OR " +
             "(exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
			"WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=CRO_UTR))  AND CRO_FEF IS NULL)" +
       "GROUP BY E_EXP.EXP_MUN, E_EXP.EXP_PRO, E_EXP.EXP_EJE, E_EXP.EXP_NUM, PML_VALOR, E_EXP.EXP_FEI, E_EXP.EXP_FEF, E_EXP.EXP_EST,UOR_NOM,USU_NOM";
        m_Log.debug("permiteModificarObservacionesUsiario: " + sql);
        try {
            conexion = abd.getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql);
            int i = 1;
            m_Log.debug("Numero de expediente: " + consExpVO.getNumeroExpediente());
            m_Log.debug("Codigo de Usuario: " + usuario.getIdUsuario());
            m_Log.debug("Organizacion de Usuario: " + usuario.getOrgCod());
            m_Log.debug("Entidad de Usuario: " + usuario.getEntCod());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());
            ps.setString(i++, consExpVO.getNumeroExpediente());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());

            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                estadoExp = rs.getInt("EXP_EST");
            }
            
        } catch (Exception ex) {
            abd.rollBack(conexion);
            ex.printStackTrace();
        } finally {
            if (conexion != null)
                try {
                    abd.devolverConexion(conexion);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    m_Log.error("Exception: " + ex.getMessage());
                }
        }

        return estadoExp;
    }
    
    
    
    /**
     * Cuenta el número de expedientes que tienen la misma localización
     * @param localizacion: Localización
     * @param numExpediente: Nº del expediente
     * @param params: Parámetros de conexión a la base de datos
     * @return int
     * @throws es.altia.util.conexion.BDException
     */
    public int getNumExpedientesConLocalizacion(String localizacion,String numExpediente, String[] params)
            throws BDException {

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int num = 0;

        m_Log.debug("==================> ConsultaExpedientesDAO init ");
        try{
            String[] datosExpediente = numExpediente.split("/");
            if(datosExpediente!=null && datosExpediente.length==3){
                AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
                con = adapt.getConnection();
                String sql = "SELECT COUNT(*) AS NUM FROM E_EXP WHERE ";

                TransformacionAtributoSelect transform = new TransformacionAtributoSelect(adapt);
                if(localizacion!=null){
                   boolean consultaInsensitiva=true;
                   String condicion = transform.construirCondicionWhereConOperadores("EXP_LOC", localizacion, false);
                    if (!"".equals(condicion)) {
                       sql += condicion;
                    }
                }
                
                sql += " AND EXP_NUM<>'" + numExpediente + "'";

                m_Log.debug("sql: " + sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);
                rs.next();
                num = rs.getInt("NUM");

            }// if
        }catch(SQLException e){
            e.printStackTrace();
            m_Log.error("SQLException e: " + e.getMessage());
            throw new BDException(e.getMessage());
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
                m_Log.error("SQLException " + e.getMessage());
                throw new BDException(e.getMessage());
            }
        }

        return num;

    }// getNumExpedientesConLocalizacion


      /**
     * Metodo para realizar consultas sobre el conjunto de expedientes de la aplicación y recuperar aquellos que cumplen
     * ciertos criterios generales de un procedimiento. Se excluye un determinado número de expediente.
     * @param uVO UsuarioValueObject con la información del usuario.
     * @param consExpVO ConsultaExpedientesValueObject con los criterios de consulta del expediente.
     * @param params String[] con los parametros de conexión a BB.DD.
     * @param desdeGestion boolean indicando si la consulta se ha hecho desde gestión de expedientes.
     * @param buscarInicioProc boolean indicando si se quiere que la consulta busque aquellos expedientes que puede
     * tramitar el usuario o aquellos expedientes de procedimiento que puede iniciar el usuario.
     * @param numExpediente: Número del expediente
     * @return Vector con los expedientes recuperados.
     * @throws AnotacionRegistroException En caso de error.
     * @throws TechnicalException En caso de error.
     * @throws TramitacionException En caso de error.
     */
    public Vector getExpedientesConCriterioExcepto(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params,
                            boolean desdeGestion, boolean buscarInicioProc,String columna,String tipoOrden,boolean adjuntarExpediente,String numExpediente)
    throws AnotacionRegistroException, TechnicalException, TramitacionException {
  // Comienzo del metodo.
        m_Log.debug("Comienzo de ConsultaExpedientesDAO.consultar()");

        // Obtener la conexion.
        Connection con = null;
        Statement st;
        ResultSet rs;
        String sql;
        Vector<TramitacionValueObject> lista = new Vector<TramitacionValueObject>();        
        Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(uVO, params);
        TransformacionAtributoSelect transformador;
        
        String codigoColOrdInteresado=COLUMNA_ORDEN_INTERESADO; //Columna de ordenación por tercero

        if (lUT.size() > 0) {
            // Tiene unidades tramitadoras.

            try {
                AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
                con = oad.getConnection();
                st = con.createStatement();
                transformador = new TransformacionAtributoSelect(oad,con);

                /* Creacion de la consulta SQL.
                 Queremos que se nos muestren para un usuario concreto solo los expedientes tramitados por las
                 unidades organicas a las que el pertenece. Se hace la consulta con filtro.
                */
                // String sqlSinFiltro = obtenerSentenciaSinFiltro(consExpVO,oad,transformador,params);

                /* Comprobar datos del usuario */
                m_Log.debug("--> Codigo Unidad organizativa : " + uVO.getUnidadOrgCod());
                m_Log.debug("--> Unidad organizativa : " + uVO.getUnidadOrg());
                m_Log.debug("--> Codigo departamento(String) : " + uVO.getDep());
                m_Log.debug("--> Codigo departamento(int) : " + uVO.getDepCod());
                m_Log.debug("--> Codigo Ent : " + uVO.getEntCod());
                m_Log.debug("--> Nombre de usuario : " + uVO.getNombreUsu());
                m_Log.debug("--> Identificador de usuario : " + uVO.getIdUsuario());
                m_Log.debug("--> Org (string) : " + uVO.getOrg());
                m_Log.debug("--> Org (int) : " + uVO.getOrgCod());

                 String sqlConFiltro;
                if(codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                {
                    sqlConFiltro  = obtenerSentenciaConFiltroByInteresado(consExpVO, oad, transformador, params, uVO, buscarInicioProc,tipoOrden);
                }
                else {
                      sqlConFiltro = obtenerSentenciaConFiltro(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                }

                int posE = 0;
                int numPaginaE = 0;
                int pagActual = -1;
                if (!"".equals(consExpVO.getPaginaListado())&&consExpVO.getPaginaListado()!=null) {
                    pagActual = Integer.parseInt(consExpVO.getPaginaListado());
                }
                int lineas = -1;
                if (!"".equals(consExpVO.getNumLineasPaginaListado())&&consExpVO.getNumLineasPaginaListado()!=null) {
                    lineas = Integer.parseInt(consExpVO.getNumLineasPaginaListado());
                }
                
                
                if (numExpediente != null && numExpediente.length() > 0) {
                    sqlConFiltro += " AND EXP_NUM<>'" + numExpediente + "' ";
                }

                if (consExpVO.isImprimiendo()) {
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY  5 ASC ";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY NOMBRE " + tipoOrden;
                    } else {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY " + columna + " " + tipoOrden;
                    }
                } else if (params[0] != null && (params[0].equals("oracle") || params[0].equals("ORACLE"))) {



                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by 5 asc) RN  FROM(" + sqlConFiltro + " ORDER BY 5 ASC) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select   /*+first_rows(" + lineas + ")*/  * FROM (SELECT consulta.*, ROW_NUMBER() over ( order by " + columna + " " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by " + columna + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                } else //compatible con sqlserver
                {
                    // Obtenemos el nombre de la columna para ordenarlo en sqlserver	
                    String nombreColumnaOrdenar = null;	
                    if (!codigoColOrdInteresado.equals(columna)) {	
                        nombreColumnaOrdenar = getNombreColumnaOrdenTabla(columna, params);	
                        // Para que no falle, si no tiene especificado columna en A_CAMPLIST, elegimos por defecto la de numero de expediente	
                        if (StringUtils.isEmpty(nombreColumnaOrdenar)) {	
                            nombreColumnaOrdenar = CONSULTA_EXPEDIENTES_CAMPLIST_NOM_ORDEN_EXP_NUM;	
                        }	
                    }
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by (exp_num) asc) RN  FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by exp_num asc) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + ") sqlconfiltro order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select resultado.* FROM (SELECT consulta.*, ROW_NUMBER() over ( order by (" + nombreColumnaOrdenar + ") " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by " + nombreColumnaOrdenar + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                }
                m_Log.debug(sql);

                String alter1 = null;
                String alter2 = null;
                
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    if(consultaInsensitiva){
                    st.executeQuery(alter1);
                    st.executeQuery(alter2);
                    }
                }
                
                
                if (consExpVO.isImprimiendo()) {
                    rs = st.executeQuery(sql);
                } else {
                    //rs = st.executeQuery(sql);
                    PreparedStatement ps = con.prepareStatement(sql);

                    m_Log.debug("resultado de " + ((lineas * (pagActual - 1)) + 1) + " a " + pagActual * lineas);

                    ps.setInt(1, (lineas * (pagActual - 1)) + 1);
                    ps.setInt(2, pagActual * lineas);

                    rs = ps.executeQuery();
                }

                
                while(rs.next()) {
                  
                    TramitacionValueObject tvo1 = new TramitacionValueObject();
                     //GUARDAMOS EL USUARIO DE INICIO PARA TENERLO EN LA BUSQUEDA DE EXPEDIENTES
                     tvo1.setUsuarioIni(rs.getString("usu_nom"));
                    tvo1.setLocalizacion(rs.getString("EXP_LOC"));
                    tvo1.setEstado(rs.getString("EXP_EST"));
                    tvo1.setCodMunicipio(rs.getString("EXP_MUN"));
                    tvo1.setCodProcedimiento(rs.getString("EXP_PRO"));
                    tvo1.setDescProcedimiento(rs.getString("PML_VALOR"));
                    tvo1.setEjercicio(rs.getString("EXP_EJE"));
                    tvo1.setNumero(rs.getString("EXP_NUM"));
                    tvo1.setFechaInicioExpediente(rs.getString("fIni"));
                    tvo1.setFechaFinExpediente(rs.getString("fFin"));
                    String asuntoExp = rs.getString("EXP_ASU");
                    if (asuntoExp != null) tvo1.setAsuntoExp(AdaptadorSQLBD.js_escape(asuntoExp));
                    else tvo1.setAsuntoExp("");
                    String unidadIni = rs.getString("UOR_NOM");
                    tvo1.setUnidadInicio(unidadIni);
                    lista.addElement(tvo1);
                    posE++;
                }
                if (m_Log.isDebugEnabled()) m_Log.debug("Se han recuperado " + lista.size() + " expedientes.");
                rs.close();

                // Completamos con los datos del interesado.
                for (int i = 0; i < lista.size(); i++) {
                    TramitacionValueObject tvo1 = lista.elementAt(i);
                    tvo1.setTitular(" ");
                    int codRolPD = -1;
                    sql = "SELECT ROL_COD FROM E_ROL WHERE ROL_MUN = " + tvo1.getCodMunicipio() +
                            " AND ROL_PRO = '" + tvo1.getCodProcedimiento() + "' AND ROL_PDE = 1";
                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);

                    while (rs.next()) {
                        codRolPD = rs.getInt("ROL_COD");
                    }
                    rs.close();

                    String from = "EXT_ROL, " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA1", "''", "HTE_AP1"}) + " AS ap1, " +
                            oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA2", "''", "HTE_AP2"}) + " AS ap2, HTE_NOM AS nombre ";

                    String where = "EXT_MUN = " + tvo1.getCodMunicipio() + " AND EXT_EJE = " + tvo1.getEjercicio() +
                            " AND EXT_NUM = '" + tvo1.getNumero() + "' AND MOSTRAR = 1";

                    String[] join = new String[5];

                    join[0] = "E_EXT";
                    join[1] = "LEFT";
                    join[2] = "T_HTE";
                    join[3] = "EXT_TER = HTE_TER AND EXT_NVR = HTE_NVR";
                    join[4] = "false";

                    sql = oad.join(from, where, join);

                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);

                    String entrar = "no";
                    if (rs.next()) {
                        //formato del interesado
                         String ap1=rs.getString("ap1");
                         String ap2=rs.getString("ap2");
                         String nombre=rs.getString("nombre");
                         String titular=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);
                         tvo1.setTitular(titular);
                    }
                    rs.close();
                  
                    sql="SELECT G_REL.REL_NUM FROM G_EXP, G_REL WHERE G_REL.REL_NUM=G_EXP.REL_NUM AND REL_EST=0 AND EXP_NUM='"+
                            tvo1.getNumero()+"' ";
                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);
                    if (rs.next()) tvo1.setNumeroRelacion(rs.getString("REL_NUM"));
                }
                
                st.close();

            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
            }
            finally {
                try {
                    if(consultaInsensitiva){
                        if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                            Statement st_alter;
                            String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                            String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                            st_alter = con.createStatement();
                            st_alter.executeQuery(alter1);
                            st_alter.executeQuery(alter2);

                            st_alter.close();
                        }
                    }
                    con.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }

        return lista;
       
    }


    /**
     * Recupera los expedientes relacionados a uno dado que tenga una determinada localización
     * @param numExpediente: Número del expediente
     * @param codMunicipio: Código del municipio
     * @param localizacion: Localización
     * @param params: Parámetros de conexión
     * @return
     * @throws es.altia.agora.business.registro.exception.AnotacionRegistroException
     * @throws es.altia.common.exception.TechnicalException
     * @throws es.altia.agora.business.sge.exception.TramitacionException     */
     public ArrayList<ConsultaExpedientesValueObject> getExpRelacionados(String numExpediente,int codMunicipio,String localizacion, String[] params)
            throws AnotacionRegistroException, TechnicalException, TramitacionException {

         String BARRA = "/";
        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getExpRelacionados");

        Connection con = null;
        Statement st=null;
        ResultSet rs=null;
        String sql;        
        int cont = 0;
        ArrayList<ConsultaExpedientesValueObject> listaExpRel = new ArrayList<ConsultaExpedientesValueObject>();
        String[] datosExpediente = numExpediente.split(BARRA);
        int ejercicio = -1;
        if(datosExpediente!=null && datosExpediente.length==3){
            ejercicio = Integer.parseInt(datosExpediente[0]);
        }
        
        try {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

            sql = "SELECT REX_MUNR, REX_EJER, REX_NUMR " +
                    "FROM E_REX,E_EXP " +
                    "WHERE REX_MUN = " + codMunicipio + " AND REX_EJE = " + ejercicio + " " +
                    "AND REX_NUM = '" + numExpediente + "' " +
                    "AND REX_MUNR=EXP_MUN AND REX_NUMR=EXP_NUM AND REX_EJER = EXP_EJE AND ";

            TransformacionAtributoSelect transformador = new TransformacionAtributoSelect(oad);
            String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", localizacion.trim(), false);
            sql += condicion;

            if (m_Log.isDebugEnabled()) m_Log.debug("getExpRelacionados : " + sql);
            rs = st.executeQuery(sql);
            while (rs.next()) {
                cont++;
                ConsultaExpedientesValueObject cEVO = new ConsultaExpedientesValueObject();
                String codMunicipioRel = rs.getString("REX_MUNR");
                cEVO.setCodMunicipio(codMunicipioRel);
                String ejercicioRel = rs.getString("REX_EJER");
                cEVO.setEjercicio(ejercicioRel);
                String numeroExpedienteRel = rs.getString("REX_NUMR");
                cEVO.setNumeroExpediente(numeroExpedienteRel);
                listaExpRel.add(cEVO);
            }           
            if (m_Log.isDebugEnabled())
                m_Log.debug("las filas afectados en el select de getExpRelacionados son : " + cont);

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.ConsultaExpedientesDAO.getExpRelacionados"), e);
        }
        finally {
            try {
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if (con != null) con.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }

        return listaExpRel;
    } 


    /**
     * Recupera los expedientes relacionados a uno dado que tenga una determinada localización
     * @param numExpediente: Número del expediente
     * @param codMunicipio: Código del municipio
     * @param localizacion: Localización
     * @param params: Parámetros de conexión
     * @return
     * @throws es.altia.agora.business.registro.exception.AnotacionRegistroException
     * @throws es.altia.common.exception.TechnicalException
     * @throws es.altia.agora.business.sge.exception.TramitacionException
     */
     public ArrayList<ConsultaExpedientesValueObject> getExpRelacionados(String numExpediente,int codMunicipio,String localizacion,Connection con,String[] params)
            throws AnotacionRegistroException, TechnicalException, TramitacionException {

         String BARRA = "/";
        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getExpRelacionados");

        Statement st=null;
        ResultSet rs=null;
        String sql;
        int cont = 0;
        ArrayList<ConsultaExpedientesValueObject> listaExpRel = new ArrayList<ConsultaExpedientesValueObject>();
        String[] datosExpediente = numExpediente.split(BARRA);
        int ejercicio = -1;
        if(datosExpediente!=null && datosExpediente.length==3){
            ejercicio = Integer.parseInt(datosExpediente[0]);
        }

        try {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            st = con.createStatement();

            sql = "SELECT REX_MUNR, REX_EJER, REX_NUMR " +
                    "FROM E_REX,E_EXP " +
                    "WHERE REX_MUN = " + codMunicipio + " AND REX_EJE = " + ejercicio + " " +
                    "AND REX_NUM = '" + numExpediente + "' " +
                    "AND REX_MUNR=EXP_MUN AND REX_NUMR=EXP_NUM AND REX_EJER = EXP_EJE AND ";

            TransformacionAtributoSelect transformador = new TransformacionAtributoSelect(oad);
            String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", localizacion.trim(), false);
            sql += condicion;

            if (m_Log.isDebugEnabled()) m_Log.debug("getExpRelacionados : " + sql);
            rs = st.executeQuery(sql);
            while (rs.next()) {
                cont++;
                ConsultaExpedientesValueObject cEVO = new ConsultaExpedientesValueObject();
                String codMunicipioRel = rs.getString("REX_MUNR");
                cEVO.setCodMunicipio(codMunicipioRel);
                String ejercicioRel = rs.getString("REX_EJER");
                cEVO.setEjercicio(ejercicioRel);
                String numeroExpedienteRel = rs.getString("REX_NUMR");
                cEVO.setNumeroExpediente(numeroExpedienteRel);
                listaExpRel.add(cEVO);
            }
            
            if (m_Log.isDebugEnabled())
                m_Log.debug("las filas afectados en el select de getExpRelacionados son : " + cont);

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.ConsultaExpedientesDAO.getExpRelacionados"), e);
        }
        finally {
            try {
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
            }
        }

        return listaExpRel;
    }


    /**
     * Cuenta el nº de expedientes que verifican un determinado criterio de filtrado y además que los expedientes hayan sido
     * tramitados por un determinado usuario
     * @param uVO: Objeto con los datos del usuario
     * @param consExpVO: Objeto con el criterio de filtrado a realizar
     * @param params: Parámetros de conexión a la BD
     * @param desdeGestion
     * @param buscarInicioProc
     * @return
     * @throws es.altia.agora.business.registro.exception.AnotacionRegistroException
     * @throws es.altia.common.exception.TechnicalException
     * @throws es.altia.agora.business.sge.exception.TramitacionException
     */
    public int contarExpedientesUsuarioTramitador(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params,
                            boolean desdeGestion, boolean buscarInicioProc)
    throws AnotacionRegistroException, TechnicalException, TramitacionException {

        // Comienzo del metodo.
        m_Log.debug("Comienzo de ConsultaExpedientesDAO.contarExpedientesUsuarioTramitador()");

        // Obtener la conexion.
        Connection con = null;
        Statement st;
        ResultSet rs;
        String sql;
        Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(uVO, params);
        TransformacionAtributoSelect transformador;
        int numRelacionExpedientes = 0;
        if (lUT.size() > 0) {
            // Tiene unidades tramitadoras.

            try {
                AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
                con = oad.getConnection();
                st = con.createStatement();
                transformador = new TransformacionAtributoSelect(oad,con);

                /* Comprobar datos del usuario */
                m_Log.debug("--> Codigo Unidad organizativa : " + uVO.getUnidadOrgCod());
                m_Log.debug("--> Unidad organizativa : " + uVO.getUnidadOrg());
                m_Log.debug("--> Codigo departamento(String) : " + uVO.getDep());
                m_Log.debug("--> Codigo departamento(int) : " + uVO.getDepCod());
                m_Log.debug("--> Codigo Ent : " + uVO.getEntCod());
                m_Log.debug("--> Nombre de usuario : " + uVO.getNombreUsu());
                m_Log.debug("--> Identificador de usuario : " + uVO.getIdUsuario());
                m_Log.debug("--> Org (string) : " + uVO.getOrg());
                m_Log.debug("--> Org (int) : " + uVO.getOrgCod());

                String sqlConFiltro = ""; 
                        
                if  ("2".equals(consExpVO.getTipoBusqueda()))      
                    sqlConFiltro = obtenerSentenciaConFiltroUsuarioTramitadorHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                else if ("1".equals(consExpVO.getTipoBusqueda())) 
                    sqlConFiltro = obtenerSentenciaConFiltroUsuarioTramitadorActivosHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                else 
                    sqlConFiltro = obtenerSentenciaConFiltroUsuarioTramitador(consExpVO, oad, transformador, params, uVO, buscarInicioProc);

                sql = "SELECT count(*) FROM(" + sqlConFiltro + ") CONSULTA ";

                m_Log.debug(sql);

                String alter1 = null;
                String alter2 = null;

                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    if(consultaInsensitiva){
                        st.executeQuery(alter1);
                        st.executeQuery(alter2);
                    }
                }

                rs = st.executeQuery(sql);
                if (rs.next()) numRelacionExpedientes = rs.getInt(1);
                // Obtener los resultados.

                if (m_Log.isDebugEnabled()) m_Log.debug("Se han recuperado " + numRelacionExpedientes + " expedientes.");
                rs.close();


            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
            }
            finally {
                try {
                    
                    if(consultaInsensitiva){
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                        Statement st_alter;
                        String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                        String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                        st_alter = con.createStatement();
                        st_alter.executeQuery(alter1);
                        st_alter.executeQuery(alter2);

                        st_alter.close();
                    }
                    }
                    con.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }

        return numRelacionExpedientes;
    }



  

    /**
     * Obtiene una consulta de expedientes que cumplen unos determinados criterios de filtrado pero que han sido tramitados
     * por un determinado usuario
     * @param consExpVO: Objeto con los criterios de filtrado
     * @param oad: AdaptadorSQLBD
     * @param transformador: TransformacionAtributoSelect
     * @param params: Parámetros de conexión a la base de datos
     * @param uVO: Objeto con los datos del usuario
     * @param buscarInicioProc
     * @return
     * @throws es.altia.util.conexion.BDException
     */
     private String obtenerSentenciaConFiltroUsuarioTramitador(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO, boolean buscarInicioProc)
            throws BDException {


            m_Log.debug("eMPIEZO A obtenerSentenciaConFiltroExpedientesTramitadosUsuario");

        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }
            
            
            
        String from = " DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
               "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom,E_EXR.EXR_TOP, 'N' AS EXHISTORICO";

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            from += ", DNN_RCA," + GlobalNames.ESQUEMA_GENERICO + "A_USU,";
        }

        ArrayList<String> join = new ArrayList<String>();

        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
         join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");

        if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
            join.add("LEFT");
            join.add("E_EXT");
            join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");
            
            /*** SE BUSCAN LOS INTERESADOS QUE TIENEN COMO INTERESADO ALGUNO CON UN DETERMINADO DOCUMENTO Y TIPO DE DOCUMENTO ***/
            if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                join.add("LEFT");
                join.add("T_HTE");
                join.add("EXT_TER = T_HTE.HTE_TER AND EXT_NVR = T_HTE.HTE_NVR");                
            }                        
        }else{ //NO hay titular pero hay Rol 
       
            if(consExpVO.getCodigoRol()!=null && !"".equals(consExpVO.getCodigoRol().trim())) {  
              join.add("INNER");   
              join.add("E_EXT");
              join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");            
           }
        }

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }

        // Caso de que tengamos ejercicio, numero o tipo de anotacion.
        if ((consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) ||
            (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) ||
            (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
            join.add("LEFT");
            join.add("E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");
            join.add("LEFT");
            join.add("E_EXREXT");
            join.add("EXP_MUN = EXREXT_MUN AND EXP_NUM = EXREXT_NUM");
        }
         //else if (consExpVO.getTipoInicio() != null && !"Todos".equals(consExpVO.getTipoInicio()))
        else
        {
            join.add("LEFT");
            join.add("E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");
        }

        join.add("false");

        String where = "PML_CMP = 'NOM' AND PML_LENG = '"+m_ConfigTechnical.getString("idiomaDefecto")+"'  AND ";
        
        /***  SI SE FILTRA POR EXPEDIENTE INICIADO DESDE REGISTRO TELEMATICO ****/
        if((consExpVO.getBusqTelematicos()!=null && !"".equals(consExpVO.getBusqTelematicos())) && "1".equals(consExpVO.getBusqTelematicos())){ 
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                where += " EXP_NUM IN (SELECT EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            } else {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            }
        }

        where += "(EXP_USU=" + uVO.getIdUsuario() + " AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) ";

        where += " OR ( CRO_USF=" + uVO.getIdUsuario() + " AND (exists ("
                  + "SELECT DISTINCT UOU_UOR "
                  + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                  + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                  + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR))) ";

     
        if (buscarInicioProc) {
                m_Log.debug("***  buscarInicioProc");
            where += " OR (exists (SELECT DISTINCT UOU_UOR " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI " +
                    "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " " +
                    "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))";
        }

        //where += "))";
        where += ")";

       
        String[] parametros = new String[2];
 m_Log.debug("*** ANTES  getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getCodProcedimiento() != null) {
             m_Log.debug("***  getCodProcedimiento"+consExpVO.getCodProcedimiento());
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
      m_Log.debug("*** DESPUES getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                parametros[0] = "'"+consExpVO.getNumeroExpediente().trim()+"'";
                 String condicion = "EXP_NUM="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        if (consExpVO.getEjercicioExpediente() != null) {
            if (!"".equals(consExpVO.getEjercicioExpediente())) {
               
                where += " AND EXP_EJE=" + consExpVO.getEjercicioExpediente();
                
            }
            
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) {
                    condicion = "0";
                    where+=" AND EXP_EST = 0 ";
                }
                else if ("cerrado".equals(consExpVO.getEstado())) {
                    //condicion = "9";
                    where += " AND (EXP_EST=9 )";
                }
                else if ("anulado".equals(consExpVO.getEstado())) {
                    //condicion = "1";
                    where += " AND (EXP_EST=1 )";
                }
            }
        }
        
        
        if (consExpVO.getTitular() != null) {
            
            if(!CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
                if (!"".equals(consExpVO.getTercero())) {
                    where += " AND EXT_TER = " + consExpVO.getTercero().trim();                
                }                 
            }else{
                // SE CONSULTA POR DOCUMENTO DEL TERCERO Y NO POR CÓDIGO DE TERCERO
                if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                   
                    where += " AND HTE_DOC='" + consExpVO.getDocumentoTercero()+ "'" ;
                }                
            }   
        }
        
        
        /*** ORIGINAL
        if (consExpVO.getTitular() != null) {
            if (!"".equals(consExpVO.getTitular()) && !"".equals(consExpVO.getTercero())) {
                where += " AND EXT_TER = " + consExpVO.getTercero().trim();
            }
        } **/
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = "TRA_CLS="+consExpVO.getCodClasifTramite();
                if (!"".equals(condicion)) {
                   where += " AND " + condicion+" AND CRO_FEF IS NULL";
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }

        String condFechaFinTramite;
        if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = " AND CRO_FEF IS NULL";
        else condFechaFinTramite = " AND CRO_FEF IS NOT NULL";

        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
              
                    where += " AND CRO_UTR=" + consExpVO.getCodUnidadTram().trim();

            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_UTR = " + cUT;
                    else where += " OR CRO_UTR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += condFechaFinTramite + ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
             
                where += " AND CRO_TRA=" + consExpVO.getCodTramite().trim();

            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }


        // Si tenemos ejercicio de anotacion
        if (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion())) {
            where += " AND (EXR_EJR = " + consExpVO.getEjercicioAnotacion().trim() +
                       " OR EXREXT_EJR = " + consExpVO.getEjercicioAnotacion().trim() + ")";
        }

        // Si tenemos numero de anotacion
        if (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) {
           where += " AND (EXR_NRE = " + consExpVO.getNumeroAnotacion().trim() +
                      " OR EXREXT_NRE = " + consExpVO.getNumeroAnotacion().trim() + ")";
        }

        // Si tenemos tipo de anotacion ('E' o 'S')
        if (consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) {
            where += " AND (EXR_TIP = '" + consExpVO.getCodTipoAnotacion().trim() +
                      "' OR EXREXT_TIP = '" + consExpVO.getCodTipoAnotacion().trim() + "')";
        }

        // Si tenemos asunto
            if (consExpVO.getAsunto() != null) {
                if (!"".equals(consExpVO.getAsunto().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_ASU COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getAsunto().trim() + "%')";
                    else condicion="((EXP_ASU LIKE '%"+consExpVO.getAsunto().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
        // Si tenemos observaciones
            if (consExpVO.getObservaciones() != null) {
                if (!"".equals(consExpVO.getObservaciones().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_OBS COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getObservaciones().trim() + "%')";
                    else condicion="((EXP_OBS LIKE '%"+consExpVO.getObservaciones().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }


        // óscar
        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientes".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND EXP_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientesVolumen".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND EXP_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesHistoricos".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            //where += " AND EXP_FEF IS NOT NULL";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesCerrados".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND EXP_FEF IS NOT NULL AND (EXP_EST=1 or EXP_EST=9)";
        }
                
        if (consExpVO.getTipoInicio()!= null) {
            if (!"Todos".equals(consExpVO.getTipoInicio())) {
                if ("Instancia".equals(consExpVO.getTipoInicio())) {
                    where+=" AND E_EXR.EXR_TOP= 0 ";
                }
                else if ("Oficio".equals(consExpVO.getTipoInicio())) {                    
                    where += "AND (E_EXR.EXR_TOP <> 0 OR E_EXR.EXR_TOP IS NULL)";
                }                
            }
        }
        
        if (consExpVO.getCodigoRol() != null) {
            if (!"".equals(consExpVO.getCodigoRol().trim())) {
                    where += " AND  EXT_ROL= "+ consExpVO.getCodigoRol() ;}
        }   

        where = where + " AND (PRO_RESTRINGIDO=0 OR (PRO_RESTRINGIDO=1 AND E_PRO.PRO_COD IN (SELECT PRO_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE USU_COD=" + uVO.getIdUsuario() + " AND ORG_COD=" + uVO.getOrgCod() + ")))";

        return oad.join(from, where, join.toArray(new String[]{}));
    }
     
     
     /**
     * Obtiene una consulta de expedientes activos y de histórico que cumplen unos determinados criterios de filtrado 
     * pero que han sido tramitados por un determinado usuario
     * @param consExpVO: Objeto con los criterios de filtrado
     * @param oad: AdaptadorSQLBD
     * @param transformador: TransformacionAtributoSelect
     * @param params: Parámetros de conexión a la base de datos
     * @param uVO: Objeto con los datos del usuario
     * @param buscarInicioProc
     * @return
     * @throws es.altia.util.conexion.BDException
     */
     private String obtenerSentenciaConFiltroUsuarioTramitadorActivosHistorico(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO, boolean buscarInicioProc)
            throws BDException {

        m_Log.debug("eMPIEZO A obtenerSentenciaConFiltroUsuarioTramitadorHistorico");
        String queryActivos = obtenerSentenciaConFiltroUsuarioTramitador(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
        String queryHistorico = obtenerSentenciaConFiltroUsuarioTramitadorHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
        String resultado = queryActivos + " UNION ALL " + queryHistorico;
        
        return resultado;
     }
     
     
    /**
     * Obtiene una consulta de expedientes de histórico que cumplen unos determinados criterios de filtrado 
     * pero que han sido tramitados por un determinado usuario
     * @param consExpVO: Objeto con los criterios de filtrado
     * @param oad: AdaptadorSQLBD
     * @param transformador: TransformacionAtributoSelect
     * @param params: Parámetros de conexión a la base de datos
     * @param uVO: Objeto con los datos del usuario
     * @param buscarInicioProc
     * @return
     * @throws es.altia.util.conexion.BDException
     */
     private String obtenerSentenciaConFiltroUsuarioTramitadorHistorico(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO, boolean buscarInicioProc)
            throws BDException {


        m_Log.debug("eMPIEZO A obtenerSentenciaConFiltroUsuarioTramitadorHistorico");

        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }
            
            
            
        String from = " DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom,HIST_E_EXR.EXR_TOP, 'S' AS EXHISTORICO";

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            from += ", DNN_RCA," + GlobalNames.ESQUEMA_GENERICO + "A_USU,";
        }

        ArrayList<String> join = new ArrayList<String>();

        join.add("HIST_E_EXP");
        join.add("LEFT");
        join.add("HIST_E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
         join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");

        if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
            join.add("LEFT");
            join.add("HIST_E_EXT");
            join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");
            
            /*** SE BUSCAN LOS INTERESADOS QUE TIENEN COMO INTERESADO ALGUNO CON UN DETERMINADO DOCUMENTO Y TIPO DE DOCUMENTO ***/
            if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                join.add("LEFT");
                join.add("T_HTE");
                join.add("EXT_TER = T_HTE.HTE_TER AND EXT_NVR = T_HTE.HTE_NVR");                
            }                        
        }else{ //NO hay titular pero hay Rol 
       
            if(consExpVO.getCodigoRol()!=null && !"".equals(consExpVO.getCodigoRol().trim())) {  
              join.add("INNER");   
              join.add("HIST_E_EXT");
              join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");            
           }
        }

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }

        // Caso de que tengamos ejercicio, numero o tipo de anotacion.
        if ((consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) ||
            (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) ||
            (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
            join.add("LEFT");
            join.add("HIST_E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");
            join.add("LEFT");
            join.add("HIST_E_EXREXT");
            join.add("EXP_MUN = EXREXT_MUN AND EXP_NUM = EXREXT_NUM");
        }
         //else if (consExpVO.getTipoInicio() != null && !"Todos".equals(consExpVO.getTipoInicio()))
        else
        {
            join.add("LEFT");
            join.add("HIST_E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");
        }

        join.add("false");

        String where = "PML_CMP = 'NOM' AND PML_LENG = '"+m_ConfigTechnical.getString("idiomaDefecto")+"'  AND ";
        
         /***  SI SE FILTRA POR EXPEDIENTE INICIADO DESDE REGISTRO TELEMATICO ****/
        if((consExpVO.getBusqTelematicos()!=null && !"".equals(consExpVO.getBusqTelematicos())) && "1".equals(consExpVO.getBusqTelematicos())){ 
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                where += " EXP_NUM IN (SELECT EXR_NUM FROM HIST_E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            } else {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM HIST_E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            }
        }

        where += "(EXP_USU=" + uVO.getIdUsuario() + " AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) ";

        where += " OR ( CRO_USF=" + uVO.getIdUsuario() + " AND (exists ("
                  + "SELECT DISTINCT UOU_UOR "
                  + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                  + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                  + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR))) ";

     
        if (buscarInicioProc) {
                m_Log.debug("***  buscarInicioProc");
            where += " OR (exists (SELECT DISTINCT UOU_UOR " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI " +
                    "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " " +
                    "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))";
        }

        //where += "))";
        where += ")";

       
        String[] parametros = new String[2];
 m_Log.debug("*** ANTES  getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getCodProcedimiento() != null) {
             m_Log.debug("***  getCodProcedimiento"+consExpVO.getCodProcedimiento());
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
      m_Log.debug("*** DESPUES getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                parametros[0] = "'"+consExpVO.getNumeroExpediente().trim()+"'";
                 String condicion = "EXP_NUM="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        if (consExpVO.getEjercicioExpediente() != null) {
            if (!"".equals(consExpVO.getEjercicioExpediente())) {
               
                where += " AND EXP_EJE=" + consExpVO.getEjercicioExpediente();
                
            }
            
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) {
                    condicion = "0";
                    where+=" AND EXP_EST = 0 ";
                }
                else if ("cerrado".equals(consExpVO.getEstado())) {
                    //condicion = "9";
                    where += " AND (EXP_EST=9 )";
                }
                else if ("anulado".equals(consExpVO.getEstado())) {
                    //condicion = "1";
                    where += " AND (EXP_EST=1 )";
                }
            }
        }
        
        
        if (consExpVO.getTitular() != null) {
            
            if(!CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
                if (!"".equals(consExpVO.getTercero())) {
                    where += " AND EXT_TER = " + consExpVO.getTercero().trim();                
                }                 
            }else{
                // SE CONSULTA POR DOCUMENTO DEL TERCERO Y NO POR CÓDIGO DE TERCERO
                if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                   
                    where += " AND HTE_DOC='" + consExpVO.getDocumentoTercero()+ "'" ;
                }                
            }   
        }
        
        
        /*** ORIGINAL
        if (consExpVO.getTitular() != null) {
            if (!"".equals(consExpVO.getTitular()) && !"".equals(consExpVO.getTercero())) {
                where += " AND EXT_TER = " + consExpVO.getTercero().trim();
            }
        } **/
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = "TRA_CLS="+consExpVO.getCodClasifTramite();
                if (!"".equals(condicion)) {
                   where += " AND " + condicion+" AND CRO_FEF IS NULL";
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }

        String condFechaFinTramite;
        if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = " AND CRO_FEF IS NULL";
        else condFechaFinTramite = " AND CRO_FEF IS NOT NULL";

        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
              
                    where += " AND CRO_UTR=" + consExpVO.getCodUnidadTram().trim();

            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_UTR = " + cUT;
                    else where += " OR CRO_UTR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += condFechaFinTramite + ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
             
                where += " AND CRO_TRA=" + consExpVO.getCodTramite().trim();

            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }


        // Si tenemos ejercicio de anotacion
        if (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion())) {
            where += " AND (EXR_EJR = " + consExpVO.getEjercicioAnotacion().trim() +
                       " OR EXREXT_EJR = " + consExpVO.getEjercicioAnotacion().trim() + ")";
        }

        // Si tenemos numero de anotacion
        if (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) {
           where += " AND (EXR_NRE = " + consExpVO.getNumeroAnotacion().trim() +
                      " OR EXREXT_NRE = " + consExpVO.getNumeroAnotacion().trim() + ")";
        }

        // Si tenemos tipo de anotacion ('E' o 'S')
        if (consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) {
            where += " AND (EXR_TIP = '" + consExpVO.getCodTipoAnotacion().trim() +
                      "' OR EXREXT_TIP = '" + consExpVO.getCodTipoAnotacion().trim() + "')";
        }

        // Si tenemos asunto
            if (consExpVO.getAsunto() != null) {
                if (!"".equals(consExpVO.getAsunto().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_ASU COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getAsunto().trim() + "%')";
                    else condicion="((EXP_ASU LIKE '%"+consExpVO.getAsunto().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
        // Si tenemos observaciones
            if (consExpVO.getObservaciones() != null) {
                if (!"".equals(consExpVO.getObservaciones().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_OBS COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getObservaciones().trim() + "%')";
                    else condicion="((EXP_OBS LIKE '%"+consExpVO.getObservaciones().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }


        // óscar
        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientes".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND EXP_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientesVolumen".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND EXP_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesHistoricos".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND EXP_FEF IS NOT NULL";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesCerrados".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND EXP_FEF IS NOT NULL AND (EXP_EST=1 or EXP_EST=9)";
        }
                
        if (consExpVO.getTipoInicio()!= null) {
            if (!"Todos".equals(consExpVO.getTipoInicio())) {
                if ("Instancia".equals(consExpVO.getTipoInicio())) {
                    where+=" AND HIST_E_EXR.EXR_TOP= 0 ";
                }
                else if ("Oficio".equals(consExpVO.getTipoInicio())) {                    
                    where += "AND (HIST_E_EXR.EXR_TOP <> 0 OR HIST_E_EXR.EXR_TOP IS NULL)";
                }                
            }
        }
        
        if (consExpVO.getCodigoRol() != null) {
            if (!"".equals(consExpVO.getCodigoRol().trim())) {
                    where += " AND  EXT_ROL= "+ consExpVO.getCodigoRol() ;}
        }   

        where = where + " AND (PRO_RESTRINGIDO=0 OR (PRO_RESTRINGIDO=1 AND E_PRO.PRO_COD IN (SELECT PRO_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE USU_COD=" + uVO.getIdUsuario() + " AND ORG_COD=" + uVO.getOrgCod() + ")))";

        return oad.join(from, where, join.toArray(new String[]{}));
    }

   
      private String obtenerSentenciaConFiltroUsuarioTramitadorImprimir(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO, boolean buscarInicioProc)
            throws BDException {


            m_Log.debug("eMPIEZO A obtenerSentenciaConFiltroExpedientesTramitadosUsuario");

        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }
            
            
            
        String from = " DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom,E_EXR.EXR_TOP,EXT_TER,EXT_ROL,"+
                 oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1"})+ " AS ap1, "+
                oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA2",  "T_HTE.HTE_AP2"})+ " AS ap2, "
                + " HTE_NOM AS nombre";


        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            from += ", DNN_RCA," + GlobalNames.ESQUEMA_GENERICO + "A_USU,";
        }

        ArrayList<String> join = new ArrayList<String>();

        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
         join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");         
        join.add("LEFT");
        join.add("E_EXT");
        join.add("EXP_NUM=EXT_NUM");
        join.add("LEFT");
        join.add("T_HTE");
        
        if(consExpVO.getCodigoRol()!=null && !"".equals(consExpVO.getCodigoRol().trim())) { 
             join.add("EXT_TER =HTE_TER and EXT_NVR = HTE_NVR ");           
        }
        else  if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
             join.add("EXT_TER =HTE_TER and EXT_NVR = HTE_NVR ");
        }else
        {
             join.add("EXT_TER =HTE_TER and EXT_NVR = HTE_NVR AND 1=MOSTRAR");
        }
        

        if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
            join.add("LEFT");
            join.add("E_EXT");
            join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");
            
            /*** SE BUSCAN LOS INTERESADOS QUE TIENEN COMO INTERESADO ALGUNO CON UN DETERMINADO DOCUMENTO Y TIPO DE DOCUMENTO ***/
            if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                join.add("LEFT");
                join.add("T_HTE");
                join.add("EXT_TER = T_HTE.HTE_TER AND EXT_NVR = T_HTE.HTE_NVR");                
            }                        
        }else{ //NO hay titular pero hay Rol 
       
            if(consExpVO.getCodigoRol()!=null && !"".equals(consExpVO.getCodigoRol().trim())) {  
              join.add("INNER");   
              join.add("E_EXT");
              join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");            
           }
        }

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }

        // Caso de que tengamos ejercicio, numero o tipo de anotacion.
        if ((consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) ||
            (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) ||
            (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
            join.add("LEFT");
            join.add("E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");
            join.add("LEFT");
            join.add("E_EXREXT");
            join.add("EXP_MUN = EXREXT_MUN AND EXP_NUM = EXREXT_NUM");
        }
         //else if (consExpVO.getTipoInicio() != null && !"Todos".equals(consExpVO.getTipoInicio()))
        else
        {
            join.add("LEFT");
            join.add("E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");
        }

        join.add("false");

        String where=
        " (EXT_ROL IN (select ROL_COD from E_ROL where ROL_MUN = "+ uVO.getOrgCod()+" and ROL_PRO = EXP_PRO) or EXT_ROL is null)";
        
        where= where + " and PML_CMP = 'NOM' AND PML_LENG = '"+m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";   
        
        /***  SI SE FILTRA POR EXPEDIENTE INICIADO DESDE REGISTRO TELEMATICO ****/
        if((consExpVO.getBusqTelematicos()!=null && !"".equals(consExpVO.getBusqTelematicos())) && "1".equals(consExpVO.getBusqTelematicos())){ 
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                where += " EXP_NUM IN (SELECT EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            } else {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            }
        }
        

        where += "(EXP_USU=" + uVO.getIdUsuario() + " AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) ";

        where += " OR ( CRO_USF=" + uVO.getIdUsuario() + " AND (exists ("
                  + "SELECT DISTINCT UOU_UOR "
                  + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                  + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                  + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR))) ";

     
        if (buscarInicioProc) {
                m_Log.debug("***  buscarInicioProc");
            where += " OR (exists (SELECT DISTINCT UOU_UOR " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI " +
                    "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " " +
                    "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))";
        }

        //where += "))";
        where += ")";

       
        String[] parametros = new String[2];
 m_Log.debug("*** ANTES  getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getCodProcedimiento() != null) {
             m_Log.debug("***  getCodProcedimiento"+consExpVO.getCodProcedimiento());
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
      m_Log.debug("*** DESPUES getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                parametros[0] = "'"+consExpVO.getNumeroExpediente().trim()+"'";
                String condicion = "EXP_NUM="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        if (consExpVO.getEjercicioExpediente() != null) {
            if (!"".equals(consExpVO.getEjercicioExpediente())) {
               
                where += " AND EXP_EJE=" + consExpVO.getEjercicioExpediente();
                
            }
            
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) {
                    condicion = "0";
                    where+=" AND EXP_EST = 0 ";
                }
                else if ("cerrado".equals(consExpVO.getEstado())) {
                    //condicion = "9";
                    where += " AND (EXP_EST=9 )";
                }
                else if ("anulado".equals(consExpVO.getEstado())) {
                    //condicion = "1";
                    where += " AND (EXP_EST=1 )";
                }
            }
        }
        
        
        if (consExpVO.getTitular() != null) {
            
            if(!CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
                if (!"".equals(consExpVO.getTercero())) {
                    where += " AND EXT_TER = " + consExpVO.getTercero().trim();                
                }                 
            }else{
                // SE CONSULTA POR DOCUMENTO DEL TERCERO Y NO POR CÓDIGO DE TERCERO
                if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                   
                    where += " AND HTE_DOC='" + consExpVO.getDocumentoTercero()+ "'" ;
                }                
            }   
        }
        
        
        /*** ORIGINAL
        if (consExpVO.getTitular() != null) {
            if (!"".equals(consExpVO.getTitular()) && !"".equals(consExpVO.getTercero())) {
                where += " AND EXT_TER = " + consExpVO.getTercero().trim();
            }
        } **/
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = "TRA_CLS="+consExpVO.getCodClasifTramite();
                if (!"".equals(condicion)) {
                   where += " AND " + condicion+" AND CRO_FEF IS NULL";
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }

        String condFechaFinTramite;
        if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = " AND CRO_FEF IS NULL";
        else condFechaFinTramite = " AND CRO_FEF IS NOT NULL";

        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
              
                    where += " AND CRO_UTR=" + consExpVO.getCodUnidadTram().trim();

            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_UTR = " + cUT;
                    else where += " OR CRO_UTR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += condFechaFinTramite + ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
             
                where += " AND CRO_TRA=" + consExpVO.getCodTramite().trim();

            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }


        // Si tenemos ejercicio de anotacion
        if (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion())) {
            where += " AND (EXR_EJR = " + consExpVO.getEjercicioAnotacion().trim() +
                       " OR EXREXT_EJR = " + consExpVO.getEjercicioAnotacion().trim() + ")";
        }

        // Si tenemos numero de anotacion
        if (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) {
           where += " AND (EXR_NRE = " + consExpVO.getNumeroAnotacion().trim() +
                      " OR EXREXT_NRE = " + consExpVO.getNumeroAnotacion().trim() + ")";
        }

        // Si tenemos tipo de anotacion ('E' o 'S')
        if (consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) {
            where += " AND (EXR_TIP = '" + consExpVO.getCodTipoAnotacion().trim() +
                      "' OR EXREXT_TIP = '" + consExpVO.getCodTipoAnotacion().trim() + "')";
        }

        // Si tenemos asunto
            if (consExpVO.getAsunto() != null) {
                if (!"".equals(consExpVO.getAsunto().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_ASU COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getAsunto().trim() + "%')";
                    else condicion="((EXP_ASU LIKE '%"+consExpVO.getAsunto().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
        // Si tenemos observaciones
            if (consExpVO.getObservaciones() != null) {
                if (!"".equals(consExpVO.getObservaciones().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_OBS COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getObservaciones().trim() + "%')";
                    else condicion="((EXP_OBS LIKE '%"+consExpVO.getObservaciones().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }


        // óscar
        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientes".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND EXP_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientesVolumen".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND EXP_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesHistoricos".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND EXP_FEF IS NOT NULL";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesCerrados".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND EXP_FEF IS NOT NULL AND (EXP_EST=1 or EXP_EST=9)";
        }
                
        if (consExpVO.getTipoInicio()!= null) {
            if (!"Todos".equals(consExpVO.getTipoInicio())) {
                if ("Instancia".equals(consExpVO.getTipoInicio())) {
                    where+=" AND E_EXR.EXR_TOP= 0 ";
                }
                else if ("Oficio".equals(consExpVO.getTipoInicio())) {                    
                    where += "AND (E_EXR.EXR_TOP <> 0 OR E_EXR.EXR_TOP IS NULL)";
                }                
            }
        }
        
        if (consExpVO.getCodigoRol() != null) {
            if (!"".equals(consExpVO.getCodigoRol().trim())) {
                    where += " AND  EXT_ROL= "+ consExpVO.getCodigoRol() ;}
        }   

        where = where + " AND (PRO_RESTRINGIDO=0 OR (PRO_RESTRINGIDO=1 AND E_PRO.PRO_COD IN (SELECT PRO_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE USU_COD=" + uVO.getIdUsuario() + " AND ORG_COD=" + uVO.getOrgCod() + ")))";

        return oad.join(from, where, join.toArray(new String[]{}));
    }



       /**
     * Metodo para realizar consultas sobre el conjunto de expedientes de la aplicación y recuperar aquellos que cumplen
     * ciertos criterios generales de un procedimiento. Pero sólo aquellos expedientes tramitados por un determinado usuario
     * @param uVO UsuarioValueObject con la información del usuario.
     * @param consExpVO ConsultaExpedientesValueObject con los criterios de consulta del expediente.
     * @param params String[] con los parametros de conexión a BB.DD.
     * @param desdeGestion boolean indicando si la consulta se ha hecho desde gestión de expedientes.
     * @param buscarInicioProc boolean indicando si se quiere que la consulta busque aquellos expedientes que puede
     * tramitar el usuario o aquellos expedientes de procedimiento que puede iniciar el usuario.
     * @return Vector con los expedientes recuperados.
     * @throws AnotacionRegistroException En caso de error.
     * @throws TechnicalException En caso de error.
     * @throws TramitacionException En caso de error.
     */
    public Vector consultarUsuarioTramitador(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params,
                            boolean desdeGestion, boolean buscarInicioProc,String columna,String tipoOrden,boolean adjuntarExpediente)
    throws AnotacionRegistroException, TechnicalException, TramitacionException {

        // Comienzo del metodo.
        m_Log.debug("Comienzo de ConsultaExpedientesDAO.consultar()");

        // Obtener la conexion.
        Connection con = null;
        Statement st;
        ResultSet rs;
        String sql;
        Vector<TramitacionValueObject> lista = new Vector<TramitacionValueObject>();
        Vector<TramitacionValueObject> listaFinal = new Vector<TramitacionValueObject>();
        Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(uVO, params);
        TransformacionAtributoSelect transformador;

        String codigoColOrdInteresado=COLUMNA_ORDEN_INTERESADO; //Columna de ordenación por tercero

        if (lUT.size() > 0) {
            // Tiene unidades tramitadoras.

            try {
                AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
                con = oad.getConnection();
                st = con.createStatement();
                transformador = new TransformacionAtributoSelect(oad,con);

                /* Creacion de la consulta SQL.
                 Queremos que se nos muestren para un usuario concreto solo los expedientes tramitados por las
                 unidades organicas a las que el pertenece. Se hace la consulta con filtro.
                */
                // String sqlSinFiltro = obtenerSentenciaSinFiltro(consExpVO,oad,transformador,params);

                /* Comprobar datos del usuario */
                m_Log.debug("--> Codigo Unidad organizativa : " + uVO.getUnidadOrgCod());
                m_Log.debug("--> Unidad organizativa : " + uVO.getUnidadOrg());
                m_Log.debug("--> Codigo departamento(String) : " + uVO.getDep());
                m_Log.debug("--> Codigo departamento(int) : " + uVO.getDepCod());
                m_Log.debug("--> Codigo Ent : " + uVO.getEntCod());
                m_Log.debug("--> Nombre de usuario : " + uVO.getNombreUsu());
                m_Log.debug("--> Identificador de usuario : " + uVO.getIdUsuario());
                m_Log.debug("--> Org (string) : " + uVO.getOrg());
                m_Log.debug("--> Org (int) : " + uVO.getOrgCod());

                String sqlConFiltro;
                if(codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                {
                    // TODO: Comprobar si hay que filtrar aquí también por USUARIO TRAMITADOR
                    if  ("2".equals(consExpVO.getTipoBusqueda()))      
                        sqlConFiltro = obtenerSentenciaConFiltroByInteresadoUsuarioTramitadorHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc, tipoOrden);
                    else if ("1".equals(consExpVO.getTipoBusqueda())) 
                        sqlConFiltro = obtenerSentenciaConFiltroByInteresadoUsuarioTramitadorActivosHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc, tipoOrden);
                    else 
                        sqlConFiltro  = obtenerSentenciaConFiltroByInteresadoUsuarioTramitador(consExpVO, oad, transformador, params, uVO, buscarInicioProc, tipoOrden);
                }
                else {
                    // TODO: Comprobar si hay que filtrar aquí también por USUARIO TRAMITADOR
                    if  ("2".equals(consExpVO.getTipoBusqueda()))      
                        sqlConFiltro = obtenerSentenciaConFiltroUsuarioTramitadorHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                    else if ("1".equals(consExpVO.getTipoBusqueda())) 
                        sqlConFiltro = obtenerSentenciaConFiltroUsuarioTramitadorActivosHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                    else 
                        sqlConFiltro  = obtenerSentenciaConFiltroUsuarioTramitador(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                }

                int posE = 0;
                int numPaginaE = 0;
                int pagActual = -1;
                if (!"".equals(consExpVO.getPaginaListado())&&consExpVO.getPaginaListado()!=null) {
                    pagActual = Integer.parseInt(consExpVO.getPaginaListado());
                }
                int lineas = -1;
                if (!"".equals(consExpVO.getNumLineasPaginaListado())&&consExpVO.getNumLineasPaginaListado()!=null) {
                    lineas = Integer.parseInt(consExpVO.getNumLineasPaginaListado());
                }


                 if (consExpVO.isImprimiendo()) {
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY  5 ASC ";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY NOMBRE " + tipoOrden;
                    } else {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY " + columna + " " + tipoOrden;
                    }
                } else if (params[0] != null && (params[0].equals("oracle") || params[0].equals("ORACLE"))) {



                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by 5 asc) RN  FROM(" + sqlConFiltro + " ORDER BY 5 ASC) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select   /*+first_rows(" + lineas + ")*/  * FROM (SELECT consulta.*, ROW_NUMBER() over ( order by " + columna + " " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by " + columna + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                } else //compatible con sqlserver
                {
                    // Obtenemos el nombre de la columna para ordenarlo en sqlserver	
                    String nombreColumnaOrdenar = null;	
                    if (!codigoColOrdInteresado.equals(columna)) {	
                        nombreColumnaOrdenar = getNombreColumnaOrdenTabla(columna, params);	
                        // Para que no falle, si no tiene especificado columna en A_CAMPLIST, elegimos por defecto la de numero de expediente	
                        if (StringUtils.isEmpty(nombreColumnaOrdenar)) {	
                            nombreColumnaOrdenar = CONSULTA_EXPEDIENTES_CAMPLIST_NOM_ORDEN_EXP_NUM;	
                        }	
                    }
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by (exp_num) asc) RN  FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by exp_num asc) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + ") sqlconfiltro order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select resultado.* FROM (SELECT consulta.*, ROW_NUMBER() over ( order by (" + nombreColumnaOrdenar + ") " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by " + nombreColumnaOrdenar + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                }

                m_Log.debug(sql);

                String alter1 = null;
                String alter2 = null;
                
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                     if(consultaInsensitiva){
                        st.executeQuery(alter1);
                        st.executeQuery(alter2);
                     }
                }
                if (consExpVO.isImprimiendo()) {
                    rs = st.executeQuery(sql);
                } else {
                    //rs = st.executeQuery(sql);
                    PreparedStatement ps = con.prepareStatement(sql);

                    m_Log.debug("resultado de " + ((lineas * (pagActual - 1)) + 1) + " a " + pagActual * lineas);

                    ps.setInt(1, (lineas * (pagActual - 1)) + 1);
                    ps.setInt(2, pagActual * lineas);

                    rs = ps.executeQuery();
                }
                
                while(rs.next()) {
                    
                    TramitacionValueObject tvo1 = new TramitacionValueObject();
                     //GUARDAMOS EL USUARIO DE INICIO PARA TENERLO EN LA BUSQUEDA DE EXPEDIENTES
                     tvo1.setUsuarioIni(rs.getString("usu_nom"));
                    tvo1.setLocalizacion(rs.getString("EXP_LOC"));
                    tvo1.setEstado(rs.getString("EXP_EST"));
                    tvo1.setCodMunicipio(rs.getString("EXP_MUN"));
                    tvo1.setCodProcedimiento(rs.getString("EXP_PRO"));
                    tvo1.setDescProcedimiento(rs.getString("PML_VALOR"));
                    tvo1.setEjercicio(rs.getString("EXP_EJE"));
                    tvo1.setNumero(rs.getString("EXP_NUM"));
                    tvo1.setFechaInicioExpediente(rs.getString("fIni"));
                    tvo1.setFechaFinExpediente(rs.getString("fFin"));
                    String asuntoExp = rs.getString("EXP_ASU");
                    if (asuntoExp != null) tvo1.setAsuntoExp(AdaptadorSQLBD.js_escape(asuntoExp));
                    else tvo1.setAsuntoExp("");
                    String unidadIni = rs.getString("UOR_NOM");
                    tvo1.setUnidadInicio(unidadIni);
                    tvo1.setExpHistorico("S".equals(rs.getString("EXHISTORICO")));
                    lista.addElement(tvo1);
                    posE++;
                }
                if (m_Log.isDebugEnabled()) m_Log.debug("Se han recuperado " + lista.size() + " expedientes.");
                rs.close();

                // Completamos con los datos del interesado.
                for (int i = 0; i < lista.size(); i++) {
                    TramitacionValueObject tvo1 = lista.elementAt(i);
                    String from = "EXT_ROL, " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA1", "''", "HTE_AP1"}) + " AS ap1, " +
                            oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA2", "''", "HTE_AP2"}) + " AS ap2, HTE_NOM AS nombre, " + 
                            "ROL_COD, ROL_DES ";

                    String where = "EXT_MUN = " + tvo1.getCodMunicipio() + " AND EXT_EJE = " + tvo1.getEjercicio() +
                            " AND EXT_NUM = '" + tvo1.getNumero() + "' ";

                    String[] join = new String[8];

                    if (tvo1.isExpHistorico())
                        join[0] = "HIST_E_EXT";
                    else
                        join[0] = "E_EXT";

                    join[1] = "LEFT";
                    join[2] = "T_HTE";
                    join[3] = "EXT_TER = HTE_TER AND EXT_NVR = HTE_NVR";
                    join[4] = "LEFT";
                    join[5] = "E_ROL";
                    join[6] = "ROL_MUN = EXT_MUN AND ROL_PRO = EXT_PRO AND ROL_COD = EXT_ROL";
                    join[7] = "false";

                    sql = oad.join(from, where, join);

                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);

                    String titular = "";
                    while (rs.next()) {
                        if (!"".equals(titular))
                            titular += "<br/>";

                        //formato del interesado
                         String ap1=rs.getString("ap1");
                         String ap2=rs.getString("ap2");
                         String nombre=rs.getString("nombre");  
                         titular+=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);
                         
                         int codRol = rs.getInt("ROL_COD");
                         String desRol = rs.getString("ROL_DES");
                         titular += "<br/>(" + codRol + " - " + desRol + ")";
                    }
                    tvo1.setTitular(titular);
                    rs.close();
                  
                    /***/
                     // Como estamos en consulta, buscaremos los datos referentes a los
                    // tramites cerrados
                    sql = "SELECT " +
                            oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FINITRAMITE, " +
                            oad.convertir("CRO_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FFINTRAMITE, " +
                            "TML_VALOR " +
                            "FROM E_CRO, E_TML " +
                            "WHERE CRO_MUN = TML_MUN AND CRO_PRO = TML_PRO AND CRO_TRA = TML_TRA " +
                            "AND CRO_NUM = '" + tvo1.getNumero() + "' AND CRO_FEF IS NOT NULL";

                    if (consExpVO.getCodTramite() != null && !"".equals(consExpVO.getCodTramite())&& !"todos".equals(consExpVO.getCodTramite()))
                        sql += " AND CRO_TRA = " + consExpVO.getCodTramite();

                    /*if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);*/
                   
                    Vector<String> tramitesFinalizados = new Vector<String>();
                    /*while (rs.next()) {
                        tramitesFinalizados.add(rs.getString("TML_VALOR"));
                    }*/
                   
                    if (!desdeGestion || tramitesFinalizados.size() > 0) {
                        tvo1.setListaTramitesFinalizados(tramitesFinalizados);
                        // #238255: se rellena listaFinal con los datos recuperados de la consulta
                        listaFinal.addElement(tvo1);
                    }
                    //rs.close();
                    /******/

                    sql="SELECT G_REL.REL_NUM FROM G_EXP, G_REL WHERE G_REL.REL_NUM=G_EXP.REL_NUM AND REL_EST=0 AND EXP_NUM='"+
                            tvo1.getNumero()+"' ";
                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);
                    if (rs.next()) tvo1.setNumeroRelacion(rs.getString("REL_NUM"));
                }

                st.close();

            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
            }
            finally {
                try {
                    if(consultaInsensitiva){
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                        Statement st_alter;
                        String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                        String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                        st_alter = con.createStatement();
                        st_alter.executeQuery(alter1);
                        st_alter.executeQuery(alter2);

                        st_alter.close();
                    }
                    }
                    con.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }

        return listaFinal;
    }
    

    
    
    
    public Vector consultarUsuarioTramitadorImprimir(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params,
                            boolean desdeGestion, boolean buscarInicioProc,String columna,String tipoOrden,boolean paraCSV)
    throws AnotacionRegistroException, TechnicalException, TramitacionException {

        // Comienzo del metodo.
        m_Log.debug("Comienzo de ConsultaExpedientesDAO.consultar()");

        // Obtener la conexion.
        Connection con = null;
        Statement st;
        ResultSet rs;
        String sql;
        Vector<TramitacionValueObject> lista = new Vector<TramitacionValueObject>();
        Vector<TramitacionValueObject> listaFinal = new Vector<TramitacionValueObject>();
        Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(uVO, params);
        TransformacionAtributoSelect transformador;

        String codigoColOrdInteresado=COLUMNA_ORDEN_INTERESADO; //Columna de ordenación por tercero

        if (lUT.size() > 0) {
            // Tiene unidades tramitadoras.

            try {
                AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
                con = oad.getConnection();
                st = con.createStatement();
                transformador = new TransformacionAtributoSelect(oad,con);

                /* Creacion de la consulta SQL.
                 Queremos que se nos muestren para un usuario concreto solo los expedientes tramitados por las
                 unidades organicas a las que el pertenece. Se hace la consulta con filtro.
                */
                // String sqlSinFiltro = obtenerSentenciaSinFiltro(consExpVO,oad,transformador,params);

                /* Comprobar datos del usuario */
                m_Log.debug("--> Codigo Unidad organizativa : " + uVO.getUnidadOrgCod());
                m_Log.debug("--> Unidad organizativa : " + uVO.getUnidadOrg());
                m_Log.debug("--> Codigo departamento(String) : " + uVO.getDep());
                m_Log.debug("--> Codigo departamento(int) : " + uVO.getDepCod());
                m_Log.debug("--> Codigo Ent : " + uVO.getEntCod());
                m_Log.debug("--> Nombre de usuario : " + uVO.getNombreUsu());
                m_Log.debug("--> Identificador de usuario : " + uVO.getIdUsuario());
                m_Log.debug("--> Org (string) : " + uVO.getOrg());
                m_Log.debug("--> Org (int) : " + uVO.getOrgCod());

                String sqlConFiltro;
                if(codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                {
                    // TODO: Comprobar si hay que filtrar aquí también por USUARIO TRAMITADOR
                     sqlConFiltro  = obtenerSentenciaConFiltroByInteresadoUsuarioTramitadorImprimir(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                }
                else {
                     sqlConFiltro = obtenerSentenciaConFiltroUsuarioTramitadorImprimir(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                     //sqlConFiltro = obtenerSentenciaConFiltroUsuarioInicioExpediente(consExpVO, oad, transformador, params, uVO, buscarInicioProc);
                }
                
                int posE = 0;
                int numPaginaE = 0;
                int pagActual = -1;
                if (!"".equals(consExpVO.getPaginaListado())&&consExpVO.getPaginaListado()!=null) {
                    pagActual = Integer.parseInt(consExpVO.getPaginaListado());
                }
                int lineas = -1;
                if (!"".equals(consExpVO.getNumLineasPaginaListado())&&consExpVO.getNumLineasPaginaListado()!=null) {
                    lineas = Integer.parseInt(consExpVO.getNumLineasPaginaListado());
                }


                if (consExpVO.isImprimiendo()) {
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY  5 ASC ";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY NOMBRE " + tipoOrden;
                    } else {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY " + columna + " " + tipoOrden;
                    }
                } else if (params[0] != null && (params[0].equals("oracle") || params[0].equals("ORACLE"))) {



                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by 5 asc) RN  FROM(" + sqlConFiltro + " ORDER BY 5 ASC) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select   /*+first_rows(" + lineas + ")*/  * FROM  (SELECT consulta.*, ROW_NUMBER() over ( order by " + columna + " " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by " + columna + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                } else //compatible con sqlserver
                {
                    // Obtenemos el nombre de la columna para ordenarlo en sqlserver	
                    String nombreColumnaOrdenar = null;	
                    if (!codigoColOrdInteresado.equals(columna)) {	
                        nombreColumnaOrdenar = getNombreColumnaOrdenTabla(columna, params);	
                        // Para que no falle, si no tiene especificado columna en A_CAMPLIST, elegimos por defecto la de numero de expediente	
                        if (StringUtils.isEmpty(nombreColumnaOrdenar)) {	
                            nombreColumnaOrdenar = CONSULTA_EXPEDIENTES_CAMPLIST_NOM_ORDEN_EXP_NUM;	
                        }	
                    }
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by (exp_num) asc) RN  FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by exp_num asc) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + ") sqlconfiltro order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select resultado.*  FROM (SELECT consulta.*, ROW_NUMBER() over ( order by (" + nombreColumnaOrdenar + ") " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by " + nombreColumnaOrdenar + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                }
                
                m_Log.debug(sql);

                String alter1 = null;
                String alter2 = null;

                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                     if(consultaInsensitiva){
                         st.executeQuery(alter1);
                         st.executeQuery(alter2);
                     }
                    
                }
                if (consExpVO.isImprimiendo()) {
                    rs = st.executeQuery(sql);
                } else {
                    //rs = st.executeQuery(sql);
                    PreparedStatement ps = con.prepareStatement(sql);

                    m_Log.debug("resultado de " + ((lineas * (pagActual - 1)) + 1) + " a " + pagActual * lineas);

                    ps.setInt(1, (lineas * (pagActual - 1)) + 1);
                    ps.setInt(2, pagActual * lineas);

                    rs = ps.executeQuery();
                }
                
                
                
                while(rs.next()) {
                    
                    TramitacionValueObject tvo1 = new TramitacionValueObject();
                     //GUARDAMOS EL USUARIO DE INICIO PARA TENERLO EN LA BUSQUEDA DE EXPEDIENTES
                  
                    
                     tvo1.setUsuarioIni(rs.getString("usu_nom"));
                    tvo1.setLocalizacion(rs.getString("EXP_LOC"));
                    tvo1.setEstado(rs.getString("EXP_EST"));
                    tvo1.setCodMunicipio(rs.getString("EXP_MUN"));
                    tvo1.setCodProcedimiento(rs.getString("EXP_PRO"));
                    tvo1.setDescProcedimiento(rs.getString("PML_VALOR"));
                    tvo1.setEjercicio(rs.getString("EXP_EJE"));
                    tvo1.setNumero(rs.getString("EXP_NUM"));
                    tvo1.setFechaInicioExpediente(rs.getString("fIni"));
                    tvo1.setFechaFinExpediente(rs.getString("fFin"));
                    String asuntoExp = rs.getString("EXP_ASU");
                    if (asuntoExp != null) tvo1.setAsuntoExp(AdaptadorSQLBD.js_escape(asuntoExp));
                    else tvo1.setAsuntoExp("");
                    String unidadIni = rs.getString("UOR_NOM");
                    tvo1.setUnidadInicio(unidadIni);
                    
                  
                   
 
                    lista.addElement(tvo1);
                    posE++;
                }
                if (m_Log.isDebugEnabled()) m_Log.debug("Se han recuperado " + lista.size() + " expedientes.");
                rs.close();
                
                 for (int i = 0; i < lista.size(); i++) {
                    TramitacionValueObject tvo1 = lista.elementAt(i);
                  
                    String from = "EXT_ROL, " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA1", "''", "HTE_AP1"}) + " AS ap1, " +
                            oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA2", "''", "HTE_AP2"}) + " AS ap2, HTE_NOM AS nombre, " + 
                            "ROL_COD, ROL_DES ";

                    String where = "EXT_MUN = " + tvo1.getCodMunicipio() + " AND EXT_EJE = " + tvo1.getEjercicio() +
                            " AND EXT_NUM = '" + tvo1.getNumero() + "' AND MOSTRAR = 1";

                    String[] join = new String[8];

                    if (tvo1.isExpHistorico())
                        join[0] = "HIST_E_EXT";
                    else
                        join[0] = "E_EXT";

                    join[1] = "LEFT";
                    join[2] = "T_HTE";
                    join[3] = "EXT_TER = HTE_TER AND EXT_NVR = HTE_NVR";
                    join[4] = "LEFT";
                    join[5] = "E_ROL";
                    join[6] = "ROL_MUN = EXT_MUN AND ROL_PRO = EXT_PRO AND ROL_COD = EXT_ROL";
                    join[7] = "false";

                    sql = oad.join(from, where, join);

                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);

                    String titular;
                    if (paraCSV) {
                        titular = "\"";
                        while (rs.next()) {
                            if (!"\"".equals(titular))
                                titular += "\r";
                            //formato del interesado
                             String ap1=rs.getString("ap1");
                             String ap2=rs.getString("ap2");
                             String nombre=rs.getString("nombre");  
                             titular+=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);

                             int codRol = rs.getInt("ROL_COD");
                             String desRol = rs.getString("ROL_DES");
                             titular += " (" + codRol + " - " + desRol + ")";
                        }
                        titular += "\"";
                    } else {
                        titular = "";
                        while (rs.next()) {
                            if (!"".equals(titular))
                                titular += ">>";
                            //formato del interesado
                             String ap1=rs.getString("ap1");
                             String ap2=rs.getString("ap2");
                             String nombre=rs.getString("nombre");
                             titular+=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);

                             int codRol = rs.getInt("ROL_COD");
                             String desRol = rs.getString("ROL_DES");
                             titular += ">>(" + codRol + " - " + desRol + ")";
                        }
                    }
                    tvo1.setTitular(titular);
                    rs.close();
                    
                    
                    // Como estamos en consulta, buscaremos los datos referentes a los
                    // tramites abiertos de cada expediente.
                    String condFechaFinTramite;
                    if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = "IS NULL";
                    else condFechaFinTramite = "IS NOT NULL";
                    sql = "SELECT " +
                            oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FINITRAMITE, " +
                            oad.convertir("CRO_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FFINTRAMITE, " +
                            "TML_VALOR " +
                            "FROM E_CRO, E_TML " +
                            "WHERE CRO_MUN = TML_MUN AND CRO_PRO = TML_PRO AND CRO_TRA = TML_TRA " +
                            "AND CRO_NUM = '" + tvo1.getNumero() + "' AND CRO_FEF " + condFechaFinTramite;

                    if (consExpVO.getCodTramite() != null && !"".equals(consExpVO.getCodTramite())&& !"todos".equals(consExpVO.getCodTramite()))
                        sql += " AND CRO_TRA = " + consExpVO.getCodTramite();
                    
                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);

                    Vector<String> tramitesIniciados = new Vector<String>();
                    Vector<String> fechasInicioTramites = new Vector<String>();
                    Vector<String> fechasFinTramites = new Vector<String>();
                    while (rs.next()) {
                        tramitesIniciados.add(rs.getString("TML_VALOR"));
                        fechasInicioTramites.add(rs.getString("FINITRAMITE"));
                        fechasFinTramites.add(rs.getString("FFINTRAMITE"));
                    }

                    if (!desdeGestion || tramitesIniciados.size() > 0) {
                        tvo1.setListaTramitesAbiertos(tramitesIniciados);
                        tvo1.setListaFechasInicioTramitesAbiertos(fechasInicioTramites);
                        tvo1.setListaFechasFinTramitesAbiertos(fechasFinTramites);
                        listaFinal.addElement(tvo1);
                    }

                    rs.close();

                   
                }
                
                st.close();

            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
            }
            finally {
                try {
                    if(consultaInsensitiva){
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                        Statement st_alter;
                        String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                        String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                        st_alter = con.createStatement();
                        st_alter.executeQuery(alter1);
                        st_alter.executeQuery(alter2);

                        st_alter.close();
                    }
                    }
                    con.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }

        return lista;
    }



    /**
     * Cuenta los expedientes que ha tramitado o iniciado un determinado usuario.
     * Se realiza un filtrado por los campos suplementarios del mismo si fuese necesario.
     * @param uVO
     * @param consExpVO
     * @param params
     * @param columna
     * @param tipoOrden
     * @return
     * @throws es.altia.agora.business.registro.exception.AnotacionRegistroException
     * @throws es.altia.common.exception.TechnicalException
     * @throws es.altia.agora.business.sge.exception.TramitacionException
     */
     public int contarPorCampoSuplementarioUsuarioTramitador(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params,String columna,String tipoOrden,int tam)
            throws AnotacionRegistroException, TechnicalException, TramitacionException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("contarPorCampoSuplementarioUsuarioTramitador");

        Connection con = null;
        Statement st;
        ResultSet rs;
        String sql;
        int numRelacionExpedientes = 0;
        Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(uVO, params);
        TransformacionAtributoSelect transformador;

        if (lUT.size() > 0) // Tiene unidades tramitadoras
        {

            try {
                AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
                transformador = new TransformacionAtributoSelect(oad);
                con = oad.getConnection();

                st = con.createStatement();

                String sqlConFiltro = obtenerSentenciaConFiltroCampoSuplementariosUsuarioTramitador(consExpVO, oad, transformador, params, uVO,tam);
                sql = "SELECT count(*) FROM(" + sqlConFiltro + ") CONSULTA ";

                if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                String alter1 = null;
                String alter2 = null;

                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                     if(consultaInsensitiva){
                         st.executeQuery(alter1);
                         st.executeQuery(alter2);
                     }
                    
                }
                
                rs = st.executeQuery(sql);
                if (rs.next()) numRelacionExpedientes = rs.getInt(1);
                // Obtener los resultados.

                if (m_Log.isDebugEnabled()) m_Log.debug("Se han recuperado " + numRelacionExpedientes + " expedientes.");
                rs.close();

                st.close();

            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
            }
            finally {
                try {
                    if(consultaInsensitiva){
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                        Statement st_alter;
                        String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                        String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                        st_alter = con.createStatement();
                        st_alter.executeQuery(alter1);
                        st_alter.executeQuery(alter2);

                        st_alter.close();
                    }
                    }
                    
                    if (con != null) con.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }

        return numRelacionExpedientes;
    }





     /**
      *
      * @param consExpVO
      * @param oad
      * @param transformador
      * @param params
      * @param uVO
      * @return
      * @throws es.altia.util.conexion.BDException
      */
     private String obtenerSentenciaConFiltroCampoSuplementariosUsuarioTramitador(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                                                TransformacionAtributoSelect transformador, String[] params, UsuarioValueObject uVO,int tam)
            throws BDException {

        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }
        
        String from = " DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom"; // Para el order

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) from += ", DNN_RCA";

        ArrayList<String> join = new ArrayList<String>();


        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
         join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");


        if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
            join.add("LEFT");
            join.add("E_EXT");
            join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");

            /*** SE BUSCAN LOS EXPEDIENTES QUE TIENEN COMO INTERESADO ALGÚN TERCERO CON UN DETERMINADO DOCUMENTO ***/
            if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                join.add("LEFT");
                join.add("T_HTE");
                join.add("EXT_TER = T_HTE.HTE_TER AND EXT_NVR = T_HTE.HTE_NVR");                
            }            
        }

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }

        /**
            join.add("LEFT");
            join.add("E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");
        **/
        
        HashMap datosConsulta = consExpVO.getCamposSuplementarios();
        if(datosConsulta!=null && datosConsulta.get("TAMAÑO")!=null){
            m_Log.debug("************* ConsultaExpedientesDAO.obtenerSentenciaConFiltroCampoSuplementariosUsuarioTramitador datosConsulta.TAMAÑO!=null");
        }
        else
            m_Log.debug("************* ConsultaExpedientesDAO.obtenerSentenciaConFiltroCampoSuplementariosUsuarioTramitador datosConsulta==null");
        
        int z = 0;
        int tamaño =tam;


        datosConsulta.remove("TAMAÑO");
        EstructuraCampo estructura;
        String clave;
        String valor;
        String tipo;
        String tramite;
        while (z < tamaño) {
            estructura = (EstructuraCampo) datosConsulta.get(String.valueOf(z));
            clave = estructura.getCodCampo();
            valor = estructura.getValorConsulta();
            tipo = estructura.getURLPlantilla();
            tramite = estructura.getCodTramite();
            if (m_Log.isDebugEnabled()) m_Log.debug("CampoSuplementario: Existe Tramite= " + tramite);
            if (tramite == null) { // Se trata de campos suplementarios de expediente
                if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEX_" + z;
                    join.add("E_TDEX " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEX_NUM AND " + tabla + ".TDEX_COD = '" + clave + "'"+
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEX_VALOR", valor, false));                 
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUC_" + z;
                    join.add("E_TNUC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUC_NUM AND " + tabla + ".TNUC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUC_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFEC_" + z;
                    join.add("E_TFEC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFEC_NUM AND " + tabla + ".TFEC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFEC_VALOR", valor));
                } else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDE_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDE_VALOR";
                    join.add("E_TDE " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = "'"+valor+"'";
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDE_NUM AND " + tabla + ".TDE_COD = '" + clave + "' AND " + filtroDesplegable +")");
                } else if (tipo.indexOf("Texto") != -1) {
                    boolean consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXT_" + z;
                    join.add("E_TXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXT_NUM AND " + tabla + ".TXT_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXT_VALOR", valor, false));
                } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNU_" + z;
                    join.add("E_TNU " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNU_NUM AND " + tabla + ".TNU_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNU_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFE_" + z;
                    join.add("E_TFE " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFE_NUM AND " + tabla + ".TFE_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFE_VALOR", valor));
                }
            } else { // Se trata de campos suplementarios de tramites visibles a nivel de expediente
                if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEXT_" + z;
                    join.add("E_TDEXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEXT_NUM AND " + tabla + ".TDEXT_COD = '" + clave + "' AND " + tabla + ".TDEXT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEXT_VALOR", valor, false));            
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUCT_" + z;
                    join.add("E_TNUCT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUCT_NUM AND " + tabla + ".TNUCT_COD = '" + clave + "' AND " + tabla + ".TNUCT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUCT_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFECT_" + z;
                    join.add("E_TFECT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFECT_NUM AND " + tabla + ".TFECT_COD = '" + clave + "' AND " + tabla + ".TFECT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFECT_VALOR", valor));
                } else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDET_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDET_VALOR";
                    join.add("E_TDET " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = "'"+valor+"'";
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDET_NUM AND " + tabla + ".TDET_COD = '" + clave + "' AND " + tabla + ".TDET_TRA = " + tramite +
                            " AND " + filtroDesplegable + ")");
                } else if (tipo.indexOf("Texto") != -1) {
                    boolean consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXTT_" + z;
                    join.add("E_TXTT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXTT_NUM AND " + tabla + ".TXTT_COD = '" + clave + "' AND " + tabla + ".TXTT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXTT_VALOR", valor, false));
                } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUT_" + z;
                    join.add("E_TNUT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUT_NUM AND " + tabla + ".TNUT_COD = '" + clave + "' AND " + tabla + ".TNUT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUT_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFET_" + z;
                    join.add("E_TFET " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFET_NUM AND " + tabla + ".TFET_COD = '" + clave + "' AND " + tabla + ".TFET_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFET_VALOR", valor));
                }
            }
            z = z + 1;
        }
        // Fin Parte de Consulta  para campos Suplementarios
        join.add("false");

        String where = "PML_CMP = 'NOM' AND PML_LENG = '"+m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";
        
        where += "(EXP_USU=" + uVO.getIdUsuario() + " AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) ";

        where += " OR ( CRO_USF=" + uVO.getIdUsuario() + " AND (exists ("
                  + "SELECT DISTINCT UOU_UOR "
                  + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                  + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                  + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)))) ";

        if (consExpVO.getCodProcedimiento() != null) {
            String[] parametros = new String[2];
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_NUM", consExpVO.getNumeroExpediente().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) condicion = "0)";
                else if ("cerrado".equals(consExpVO.getEstado())) condicion = "9 OR EXP_EST =1)";
                where += " AND (EXP_EST = " + condicion ;
            }
        }
        
        if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
            if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero()) &&
                    consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                String condicion = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_TID", consExpVO.getTipoDocumentoTercero().trim(), false);
                String condicion1 = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_DOC", consExpVO.getDocumentoTercero().trim(), false);
                if (!"".equals(condicion) && !"".equals(condicion1)) {
                    where += " AND " + condicion + " AND " + condicion1;
                }
            }
        } else {
            if (consExpVO.getTitular() != null) {
                if (!"".equals(consExpVO.getTitular())) {
                    String condicion = transformador.construirCondicionWhereConOperadores("TER.EXT_TER", consExpVO.getTercero().trim(), false);
                    String condicion1 = transformador.construirCondicionWhereConOperadores("TER.EXT_NVR", consExpVO.getVersionTercero().trim(), false);
                    if (!"".equals(condicion) && !"".equals(condicion1)) {
                        where += " AND " + condicion + " AND " + condicion1;
                    }
                }
            }
        }

        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("TRA_CLS", consExpVO.getCodClasifTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_UOR", consExpVO.getCodUnidadTram().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_UOR = " + cUT;
                    else where += " OR EXP_UOR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("CRO_TRA", consExpVO.getCodTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }
        
        
        /***
        if (consExpVO.getTipoInicio()!= null) {
            if (!"Todos".equals(consExpVO.getTipoInicio())) {
                if ("Instancia".equals(consExpVO.getTipoInicio())) {
                    where+=" AND E_EXR.EXR_TOP= 0 ";
                }
                else if ("Oficio".equals(consExpVO.getTipoInicio())) {                    
                    where += "AND (E_EXR.EXR_TOP <> 0 OR E_EXR.EXR_TOP IS NULL)";
                }                
            }
        }
        ***/
        if (consExpVO.getTipoInicio()!= null && "Instancia".equals(consExpVO.getTipoInicio())) {
            where+=" AND EXP_NUM IN (SELECT EXR_NUM FROM E_EXR WHERE EXR_NUM=EXP_NUM AND EXR_MUN=EXP_MUN AND EXR_PRO=EXP_PRO AND EXR_TOP=0)";                    
        }
        else if ("Oficio".equals(consExpVO.getTipoInicio())) {                    
            where+=" AND EXP_NUM NOT IN (SELECT EXR_NUM FROM E_EXR WHERE EXR_NUM=EXP_NUM AND EXR_MUN=EXP_MUN AND EXR_PRO=EXP_PRO AND EXR_TOP=0)";                    
        }
        
        
        where = where + " AND (PRO_RESTRINGIDO=0 OR (PRO_RESTRINGIDO=1 AND E_PRO.PRO_COD IN (SELECT PRO_COD FROM " + GlobalNames.ESQUEMA_GENERICO+ "USUARIO_PROC_RESTRINGIDO WHERE USU_COD=" + uVO.getIdUsuario() +" AND ORG_COD=" + uVO.getOrgCod()+ ")))";                
        return oad.join(from, where, join.toArray(new String[]{}));
    }
     
      private String obtenerSentenciaConFiltroCampoSuplementariosUsuarioTramitadorImprimir(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                                                TransformacionAtributoSelect transformador, String[] params, UsuarioValueObject uVO,int tam)
            throws BDException {

        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }
        
        String from = " DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom,EXT_TER,EXT_ROL,"+
                 oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1"})+ " AS ap1, "+
                oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA2",  "T_HTE.HTE_AP2"})+ " AS ap2, "
                + " HTE_NOM AS nombre";
    

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) from += ", DNN_RCA";

        ArrayList<String> join = new ArrayList<String>();


        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
         join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");
        join.add("LEFT");
        join.add("E_EXT");
        join.add("EXP_NUM=EXT_NUM");
        join.add("LEFT");
        join.add("T_HTE");
        
        if(consExpVO.getCodigoRol()!=null && !"".equals(consExpVO.getCodigoRol().trim())) { 
             join.add("EXT_TER =HTE_TER and EXT_NVR = HTE_NVR ");           
        }
        else  if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
             join.add("EXT_TER =HTE_TER and EXT_NVR = HTE_NVR ");
        }else
        {
             join.add("EXT_TER =HTE_TER and EXT_NVR = HTE_NVR AND 1=MOSTRAR");
        }
        
               

        if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
            join.add("LEFT");
            join.add("E_EXT");
            join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");
        }

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }

        /**
            join.add("LEFT");
            join.add("E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");
        **/
        
        HashMap datosConsulta = consExpVO.getCamposSuplementarios();
        if(datosConsulta!=null && datosConsulta.get("TAMAÑO")!=null){
            m_Log.debug("************* ConsultaExpedientesDAO.obtenerSentenciaConFiltroCampoSuplementariosUsuarioTramitador datosConsulta.TAMAÑO!=null");
        }
        else
            m_Log.debug("************* ConsultaExpedientesDAO.obtenerSentenciaConFiltroCampoSuplementariosUsuarioTramitador datosConsulta==null");
        
        int z = 0;
        int tamaño =tam;


        datosConsulta.remove("TAMAÑO");
        EstructuraCampo estructura;
        String clave;
        String valor;
        String tipo;
        String tramite;
        while (z < tamaño) {
            estructura = (EstructuraCampo) datosConsulta.get(String.valueOf(z));
            clave = estructura.getCodCampo();
            valor = estructura.getValorConsulta();
            tipo = estructura.getURLPlantilla();
            tramite = estructura.getCodTramite();
            if (m_Log.isDebugEnabled()) m_Log.debug("CampoSuplementario: Existe Tramite= " + tramite);
            if (tramite == null) { // Se trata de campos suplementarios de expediente
                if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEX_" + z;
                    join.add("E_TDEX " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEX_NUM AND " + tabla + ".TDEX_COD = '" + clave + "'"+
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEX_VALOR", valor, false));                 
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUC_" + z;
                    join.add("E_TNUC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUC_NUM AND " + tabla + ".TNUC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUC_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFEC_" + z;
                    join.add("E_TFEC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFEC_NUM AND " + tabla + ".TFEC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFEC_VALOR", valor));
                } else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDE_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDE_VALOR";
                    join.add("E_TDE " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = "'"+valor+"'";
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDE_NUM AND " + tabla + ".TDE_COD = '" + clave + "' AND " + filtroDesplegable +")");
                } else if (tipo.indexOf("Texto") != -1) {
                    boolean consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXT_" + z;
                    join.add("E_TXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXT_NUM AND " + tabla + ".TXT_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXT_VALOR", valor, false));
                } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNU_" + z;
                    join.add("E_TNU " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNU_NUM AND " + tabla + ".TNU_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNU_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFE_" + z;
                    join.add("E_TFE " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFE_NUM AND " + tabla + ".TFE_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFE_VALOR", valor));
                }
            } else { // Se trata de campos suplementarios de tramites visibles a nivel de expediente
                if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEXT_" + z;
                    join.add("E_TDEXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEXT_NUM AND " + tabla + ".TDEXT_COD = '" + clave + "' AND " + tabla + ".TDEXT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEXT_VALOR", valor, false));            
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUCT_" + z;
                    join.add("E_TNUCT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUCT_NUM AND " + tabla + ".TNUCT_COD = '" + clave + "' AND " + tabla + ".TNUCT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUCT_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFECT_" + z;
                    join.add("E_TFECT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFECT_NUM AND " + tabla + ".TFECT_COD = '" + clave + "' AND " + tabla + ".TFECT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFECT_VALOR", valor));
                } else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDET_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDET_VALOR";
                    join.add("E_TDET " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = "'"+valor+"'";
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDET_NUM AND " + tabla + ".TDET_COD = '" + clave + "' AND " + tabla + ".TDET_TRA = " + tramite +
                            " AND " + filtroDesplegable + ")");
                } else if (tipo.indexOf("Texto") != -1) {
                    boolean consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXTT_" + z;
                    join.add("E_TXTT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXTT_NUM AND " + tabla + ".TXTT_COD = '" + clave + "' AND " + tabla + ".TXTT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXTT_VALOR", valor, false));
                } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUT_" + z;
                    join.add("E_TNUT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUT_NUM AND " + tabla + ".TNUT_COD = '" + clave + "' AND " + tabla + ".TNUT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUT_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFET_" + z;
                    join.add("E_TFET " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFET_NUM AND " + tabla + ".TFET_COD = '" + clave + "' AND " + tabla + ".TFET_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFET_VALOR", valor));
                }
            }
            z = z + 1;
        }
        // Fin Parte de Consulta  para campos Suplementarios
        join.add("false");

         String where=
        " (EXT_ROL IN (select ROL_COD from E_ROL where ROL_MUN = "+ uVO.getOrgCod()+" and ROL_PRO = EXP_PRO) or EXT_ROL is null)";
          where= where + " and PML_CMP = 'NOM' AND PML_LENG = '"+m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";        
        
        
        where += "(EXP_USU=" + uVO.getIdUsuario() + " AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) ";

        where += " OR ( CRO_USF=" + uVO.getIdUsuario() + " AND (exists ("
                  + "SELECT DISTINCT UOU_UOR "
                  + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                  + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                  + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)))) ";

        if (consExpVO.getCodProcedimiento() != null) {
            String[] parametros = new String[2];
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_NUM", consExpVO.getNumeroExpediente().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) condicion = "0)";
                else if ("cerrado".equals(consExpVO.getEstado())) condicion = "9 OR EXP_EST =1)";
                where += " AND (EXP_EST = " + condicion ;
            }
        }
        if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
            if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero()) &&
                    consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                String condicion = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_TID", consExpVO.getTipoDocumentoTercero().trim(), false);
                String condicion1 = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_DOC", consExpVO.getDocumentoTercero().trim(), false);
                if (!"".equals(condicion) && !"".equals(condicion1)) {
                    where += " AND " + condicion + " AND " + condicion1;
                }
            }
        } else {
            if (consExpVO.getTitular() != null) {
                if (!"".equals(consExpVO.getTitular())) {
                    String condicion = transformador.construirCondicionWhereConOperadores("TER.EXT_TER", consExpVO.getTercero().trim(), false);
                    String condicion1 = transformador.construirCondicionWhereConOperadores("TER.EXT_NVR", consExpVO.getVersionTercero().trim(), false);
                    if (!"".equals(condicion) && !"".equals(condicion1)) {
                        where += " AND " + condicion + " AND " + condicion1;
                    }
                }
            }
        }
        
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("TRA_CLS", consExpVO.getCodClasifTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_UOR", consExpVO.getCodUnidadTram().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_UOR = " + cUT;
                    else where += " OR EXP_UOR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("CRO_TRA", consExpVO.getCodTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }
        
        
        /***
        if (consExpVO.getTipoInicio()!= null) {
            if (!"Todos".equals(consExpVO.getTipoInicio())) {
                if ("Instancia".equals(consExpVO.getTipoInicio())) {
                    where+=" AND E_EXR.EXR_TOP= 0 ";
                }
                else if ("Oficio".equals(consExpVO.getTipoInicio())) {                    
                    where += "AND (E_EXR.EXR_TOP <> 0 OR E_EXR.EXR_TOP IS NULL)";
                }                
            }
        }
        ***/
        if (consExpVO.getTipoInicio()!= null && "Instancia".equals(consExpVO.getTipoInicio())) {
            where+=" AND EXP_NUM IN (SELECT EXR_NUM FROM E_EXR WHERE EXR_NUM=EXP_NUM AND EXR_MUN=EXP_MUN AND EXR_PRO=EXP_PRO AND EXR_TOP=0)";                    
        }
        else if ("Oficio".equals(consExpVO.getTipoInicio())) {                    
            where+=" AND EXP_NUM NOT IN (SELECT EXR_NUM FROM E_EXR WHERE EXR_NUM=EXP_NUM AND EXR_MUN=EXP_MUN AND EXR_PRO=EXP_PRO AND EXR_TOP=0)";                    
        }
        
        
        where = where + " AND (PRO_RESTRINGIDO=0 OR (PRO_RESTRINGIDO=1 AND E_PRO.PRO_COD IN (SELECT PRO_COD FROM " + GlobalNames.ESQUEMA_GENERICO+ "USUARIO_PROC_RESTRINGIDO WHERE USU_COD=" + uVO.getIdUsuario() +" AND ORG_COD=" + uVO.getOrgCod()+ ")))";                
        return oad.join(from, where, join.toArray(new String[]{}));
    }




     /**
      * Recupera los expedientes que ha tramitado o iniciado un determinado usuario.
      * Se realiza, en caso de ser necesario, un filtrado por los campos suplementarios
      * @param uVO: Objeto con los datos del usuario
      * @param consExpVO: Objeto con los criterios de filtrado de la consulta
      * @param params: Parámetros de conexión a la base de datos
      * @param columna
      * @param tipoOrden
      * @return Vector
      * @throws es.altia.agora.business.registro.exception.AnotacionRegistroException si ocurre un error
      * @throws es.altia.common.exception.TechnicalException si ocurre un error
      * @throws es.altia.agora.business.sge.exception.TramitacionException si ocurre un error
      */
     public Vector consultarPorCampoSuplementarioUsuarioTramitador(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params,String columna,String tipoOrden,int tam)
            throws AnotacionRegistroException, TechnicalException, TramitacionException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("consultar");

        Connection con = null;
        Statement st;
        ResultSet rs;
        String sql;

        Vector<TramitacionValueObject> listaExpedientes = new Vector<TramitacionValueObject>();
        int cont = 0;
        Vector<TramitacionValueObject> lista = new Vector<TramitacionValueObject>();
        Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(uVO, params);
        TransformacionAtributoSelect transformador;

        String codigoColOrdInteresado=COLUMNA_ORDEN_INTERESADO; //Columna de ordenación por tercero

        if (lUT.size() > 0) // Tiene unidades tramitadoras
        {

            try {
                AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
                transformador = new TransformacionAtributoSelect(oad);
                con = oad.getConnection();

                st = con.createStatement();

                String sqlConFiltro;
                if(codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                {
                      sqlConFiltro = obtenerSentenciaConFiltroCampoSuplementariosByInteresadoUsuarioTramitador(consExpVO, oad, transformador, params, uVO,tam);
                }
                else {
                      sqlConFiltro = obtenerSentenciaConFiltroCampoSuplementariosUsuarioTramitador(consExpVO, oad, transformador, params, uVO,tam);
                }

                 int posE = 0;
                int numPaginaE = 0;
                int pagActual = -1;
                if (!"".equals(consExpVO.getPaginaListado())&&consExpVO.getPaginaListado()!=null) {
                    pagActual = Integer.parseInt(consExpVO.getPaginaListado());
                }
                int lineas = -1;
                if (!"".equals(consExpVO.getNumLineasPaginaListado())&&consExpVO.getNumLineasPaginaListado()!=null) {
                    lineas = Integer.parseInt(consExpVO.getNumLineasPaginaListado());
                }

                 if (consExpVO.isImprimiendo()) {
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY  5 ASC ";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY NOMBRE " + tipoOrden;
                    } else {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY " + columna + " " + tipoOrden;
                    }
                } else if (params[0] != null && (params[0].equals("oracle") || params[0].equals("ORACLE"))) {



                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by 5 asc) RN  FROM(" + sqlConFiltro + " ORDER BY 5 ASC) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select   /*+first_rows(" + lineas + ")*/  * FROM (SELECT consulta.*, ROW_NUMBER() over ( order by " + columna + " " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by " + columna + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                } else //compatible con sqlserver
                {
                    // Obtenemos el nombre de la columna para ordenarlo en sqlserver	
                    String nombreColumnaOrdenar = null;	
                    if (!codigoColOrdInteresado.equals(columna)) {	
                        nombreColumnaOrdenar = getNombreColumnaOrdenTabla(columna, params);	
                        // Para que no falle, si no tiene especificado columna en A_CAMPLIST, elegimos por defecto la de numero de expediente	
                        if (StringUtils.isEmpty(nombreColumnaOrdenar)) {	
                            nombreColumnaOrdenar = CONSULTA_EXPEDIENTES_CAMPLIST_NOM_ORDEN_EXP_NUM;	
                        }	
                    }
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                         sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by (exp_num) asc) RN  FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by exp_num asc) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + ") sqlconfiltro order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select resultado.* FROM (SELECT consulta.*, ROW_NUMBER() over ( order by (" + nombreColumnaOrdenar + ") " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by " + nombreColumnaOrdenar + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                }
                m_Log.debug(sql);
                
                String alter1 = null;
                String alter2 = null;

                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    if(consultaInsensitiva){
                        st.executeQuery(alter1);
                        st.executeQuery(alter2);
                    }
                    
                }
                
                if (consExpVO.isImprimiendo()) {
                    rs = st.executeQuery(sql);
                } else {
                    //rs = st.executeQuery(sql);
                    PreparedStatement ps = con.prepareStatement(sql);

                    m_Log.debug("resultado de " + ((lineas * (pagActual - 1)) + 1) + " a " + pagActual * lineas);

                    ps.setInt(1, (lineas * (pagActual - 1)) + 1);
                    ps.setInt(2, pagActual * lineas);

                    rs = ps.executeQuery();
                }

               
                while(rs.next()) {
                    
                    cont++;
                    TramitacionValueObject tvo1 = new TramitacionValueObject();
                    tvo1.setUsuarioIni(rs.getString("usu_nom"));
                    tvo1.setLocalizacion(rs.getString("EXP_LOC"));
                    tvo1.setEstado(rs.getString("EXP_EST"));
                    tvo1.setCodMunicipio(rs.getString("EXP_MUN"));
                    tvo1.setCodProcedimiento(rs.getString("EXP_PRO"));
                    tvo1.setDescProcedimiento(rs.getString("PML_VALOR"));
                    tvo1.setEjercicio(rs.getString("EXP_EJE"));
                    tvo1.setNumero(rs.getString("EXP_NUM"));
                    tvo1.setFechaInicioExpediente(rs.getString("fIni"));
                     tvo1.setFechaFinExpediente(rs.getString("fFin"));
                    String asuntoExp = rs.getString("EXP_ASU");
                    if(this.isExpedienteIniciadoInstanciaParte(tvo1.getNumero(),tvo1.getCodProcedimiento(),tvo1.getCodMunicipio(),tvo1.getEjercicio(), con)){
                        tvo1.setTipoInicio("Instancia");
                    }else{
                        tvo1.setTipoInicio("Oficio");
                    }

                    if (asuntoExp != null) tvo1.setAsuntoExp(AdaptadorSQLBD.js_escape(asuntoExp));
                    else tvo1.setAsuntoExp("");

                    String unidadIni = rs.getString("UOR_NOM");
                    tvo1.setUnidadInicio(unidadIni);
                    lista.addElement(tvo1);
                    posE++;
                }
                if (m_Log.isDebugEnabled()) m_Log.debug("las filas afectados en el select de consultar son : " + cont);
                rs.close();

                for (int i = 0; i < lista.size(); i++) {
                    TramitacionValueObject tvo1 = lista.elementAt(i);
                    String from = "EXT_ROL, " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA1", "''", "HTE_AP1"}) + " AS ap1, " +
                            oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA2", "''", "HTE_AP2"}) + " AS ap2, HTE_NOM AS nombre, " + 
                            "ROL_COD, ROL_DES ";

                    String where = "EXT_MUN = " + tvo1.getCodMunicipio() + " AND EXT_EJE = " + tvo1.getEjercicio() +
                            " AND EXT_NUM = '" + tvo1.getNumero() + "' ";

                    String[] join = new String[8];

                    if (tvo1.isExpHistorico())
                        join[0] = "HIST_E_EXT";
                    else
                        join[0] = "E_EXT";

                    join[1] = "LEFT";
                    join[2] = "T_HTE";
                    join[3] = "EXT_TER = HTE_TER AND EXT_NVR = HTE_NVR";
                    join[4] = "LEFT";
                    join[5] = "E_ROL";
                    join[6] = "ROL_MUN = EXT_MUN AND ROL_PRO = EXT_PRO AND ROL_COD = EXT_ROL";
                    join[7] = "false";

                    sql = oad.join(from, where, join);

                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);

                    String titular = "";
                    while (rs.next()) {
                        if (!"".equals(titular))
                            titular += "<br/>";
                        //formato del interesado
                         String ap1=rs.getString("ap1");
                         String ap2=rs.getString("ap2");
                         String nombre=rs.getString("nombre");  
                         titular+=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);
                         
                         int codRol = rs.getInt("ROL_COD");
                         String desRol = rs.getString("ROL_DES");
                         titular += "<br/>(" + codRol + " - " + desRol + ")";
                    }
                    tvo1.setTitular(titular);
                    rs.close();
                    
                    // Como estamos en consulta, buscaremos los datos referentes a los
                    // tramites abiertos de cada expediente.
                    sql = "SELECT " +
                            oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FINITRAMITE, " +
                            oad.convertir("CRO_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FFINTRAMITE, " +
                            "TML_VALOR " +
                            "FROM E_CRO, E_TML " +
                            "WHERE CRO_MUN = TML_MUN AND CRO_PRO = TML_PRO AND CRO_TRA = TML_TRA " +
                            "AND CRO_NUM = '" + tvo1.getNumero() + "' AND CRO_FEF IS NULL";

                    //if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    //rs = st.executeQuery(sql);
                    //ELIMINAMOS LA EJECUCIÓN DE ESTA CONSULTA. EL EN LISTADO NO NECESITAMOS ESTA INFO. SOLO PARA IMPRIMIR Y SE USA OTRO METODO

                    Vector<String> tramitesIniciados = new Vector<String>();
                    Vector<String> fechasInicioTramites = new Vector<String>();
                    Vector<String> fechasFinTramites = new Vector<String>();

                   /* while (rs.next()) {
                        tramitesIniciados.add(rs.getString("TML_VALOR"));
                        fechasInicioTramites.add(rs.getString("FINITRAMITE"));
                        fechasFinTramites.add(rs.getString("FFINTRAMITE"));
                    }*/

                    tvo1.setListaTramitesAbiertos(tramitesIniciados);
                    tvo1.setListaFechasInicioTramitesAbiertos(fechasInicioTramites);
                    tvo1.setListaFechasFinTramitesAbiertos(fechasFinTramites);

                    listaExpedientes.addElement(tvo1);
                   // rs.close();
                }

                st.close();

            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
            }
            finally {
                try {
                    if(consultaInsensitiva){
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                        Statement st_alter;
                        String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                        String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                        st_alter = con.createStatement();
                        st_alter.executeQuery(alter1);
                        st_alter.executeQuery(alter2);

                        st_alter.close();
                    }
                    }
                    if (con != null) con.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }

        return listaExpedientes;
    }


     
     
     public Vector consultarPorCampoSuplementarioUsuarioTramitadorImprimir(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params,String columna,String tipoOrden,int tam,boolean paraCSV)
            throws AnotacionRegistroException, TechnicalException, TramitacionException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("consultar");

        Connection con = null;
        Statement st;
        ResultSet rs;
        String sql;

        Vector<TramitacionValueObject> listaExpedientes = new Vector<TramitacionValueObject>();
        int cont = 0;
        Vector<TramitacionValueObject> lista = new Vector<TramitacionValueObject>();
        Vector lUT = TramitacionManager.getInstance().getListaUnidadesTramitadorasUsuario(uVO, params);
        TransformacionAtributoSelect transformador;

        String codigoColOrdInteresado=COLUMNA_ORDEN_INTERESADO; //Columna de ordenación por tercero

        if (lUT.size() > 0) // Tiene unidades tramitadoras
        {

            try {
                AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
                transformador = new TransformacionAtributoSelect(oad);
                con = oad.getConnection();

                st = con.createStatement();

                String sqlConFiltro;
                if(codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                {
                      sqlConFiltro = obtenerSentenciaConFiltroCampoSuplementariosByInteresadoUsuarioTramitadorImprimir(consExpVO, oad, transformador, params, uVO,tam);
                }
                else {
                      sqlConFiltro = obtenerSentenciaConFiltroCampoSuplementariosUsuarioTramitadorImprimir(consExpVO, oad, transformador, params, uVO,tam);
                }

                int posE = 0;
                int numPaginaE = 0;
                int pagActual = -1;
                if (!"".equals(consExpVO.getPaginaListado())&&consExpVO.getPaginaListado()!=null) {
                    pagActual = Integer.parseInt(consExpVO.getPaginaListado());
                }
                int lineas = -1;
                if (!"".equals(consExpVO.getNumLineasPaginaListado())&&consExpVO.getNumLineasPaginaListado()!=null) {
                    lineas = Integer.parseInt(consExpVO.getNumLineasPaginaListado());
                }

                 if (consExpVO.isImprimiendo()) {
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY  5 ASC ";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY NOMBRE " + tipoOrden;
                    } else {
                        sql = "SELECT * FROM(" + sqlConFiltro + ") CONSULTA ORDER BY " + columna + " " + tipoOrden;
                    }
                } else  if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                     

                    
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by 5 asc) RN  FROM(" + sqlConFiltro + " ORDER BY 5 ASC) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select  /*+first_rows(" + lineas + ")*/  * FROM (SELECT consulta.*, ROW_NUMBER() over ( order by " + columna + " " + tipoOrden + " ) RN FROM (" + sqlConFiltro + " order by " + columna + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    }
                }
                else  //compatible con sqlserver
                {
                    // Obtenemos el nombre de la columna para ordenarlo en sqlserver	
                    String nombreColumnaOrdenar = null;	
                    if (!codigoColOrdInteresado.equals(columna)) {	
                        nombreColumnaOrdenar = getNombreColumnaOrdenTabla(columna, params);	
                        // Para que no falle, si no tiene especificado columna en A_CAMPLIST, elegimos por defecto la de numero de expediente	
                        if (StringUtils.isEmpty(nombreColumnaOrdenar)) {	
                            nombreColumnaOrdenar = CONSULTA_EXPEDIENTES_CAMPLIST_NOM_ORDEN_EXP_NUM;	
                        }	
                    }
                    if (("".equals(columna)) || ("0".equals(columna)) || (columna == null)) {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by (exp_num) asc) RN  FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by exp_num asc) CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else if (codigoColOrdInteresado.equals(columna))//Ordenacion por interesado
                    {
                        sql = "select resultado.* from (SELECT consulta.*, ROW_NUMBER() over ( order by NOMBRE " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + ") sqlconfiltro order by NOMBRE " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } else {
                        sql = "select resultado.* FROM (SELECT consulta.*, ROW_NUMBER() over ( order by (" + nombreColumnaOrdenar + ") " + tipoOrden + " ) RN FROM ( select top 100 percent  sqlconfiltro.* from (" + sqlConFiltro + " ) sqlconfiltro order by " + nombreColumnaOrdenar + " " + tipoOrden + ") CONSULTA )  resultado where rn between ? and ? order by rn";
                    } 
                }
                m_Log.debug(sql);
                
                String alter1 = null;
                String alter2 = null;

                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    if(consultaInsensitiva){
                    st.executeQuery(alter1);
                    st.executeQuery(alter2);
                    }
                }
                
                 if (consExpVO.isImprimiendo()) {
                    rs = st.executeQuery(sql);
                } else {
                    //rs = st.executeQuery(sql);
                    PreparedStatement ps = con.prepareStatement(sql);

                    m_Log.debug("resultado de " + ((lineas * (pagActual - 1)) + 1) + " a " + pagActual * lineas);

                    ps.setInt(1, (lineas * (pagActual - 1)) + 1);
                    ps.setInt(2, pagActual * lineas);

                    rs = ps.executeQuery();
                }

                
                while(rs.next()) {
                    
                    cont++;
                    TramitacionValueObject tvo1 = new TramitacionValueObject();
                    Vector<String> tramitesIniciados = new Vector<String>();
                    Vector<String> fechasInicioTramites = new Vector<String>();
                    Vector<String> fechasFinTramites = new Vector<String>();
                    tvo1.setUsuarioIni(rs.getString("usu_nom"));
                    tvo1.setLocalizacion(rs.getString("EXP_LOC"));
                    tvo1.setEstado(rs.getString("EXP_EST"));
                    tvo1.setCodMunicipio(rs.getString("EXP_MUN"));
                    tvo1.setCodProcedimiento(rs.getString("EXP_PRO"));
                    tvo1.setDescProcedimiento(rs.getString("PML_VALOR"));
                    tvo1.setEjercicio(rs.getString("EXP_EJE"));
                    tvo1.setNumero(rs.getString("EXP_NUM"));
                    tvo1.setFechaInicioExpediente(rs.getString("fIni"));
                     tvo1.setFechaFinExpediente(rs.getString("fFin"));
                    String asuntoExp = rs.getString("EXP_ASU");
                

                    if (asuntoExp != null) tvo1.setAsuntoExp(AdaptadorSQLBD.js_escape(asuntoExp));
                    else tvo1.setAsuntoExp("");
                    
                    if(this.isExpedienteIniciadoInstanciaParte(tvo1.getNumero(),tvo1.getCodProcedimiento(),tvo1.getCodMunicipio(),tvo1.getEjercicio(), con)){
                        tvo1.setTipoInicio("Instancia");
                    }else{
                        tvo1.setTipoInicio("Oficio");
                    }

                    

                    lista.addElement(tvo1);
                    posE++;
                }
                if (m_Log.isDebugEnabled()) m_Log.debug("las filas afectados en el select de consultar son : " + cont);
                rs.close();

                for (int i = 0; i < lista.size(); i++) {
                    TramitacionValueObject tvo1 = lista.elementAt(i);
                  
                    String from = "EXT_ROL, " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA1", "''", "HTE_AP1"}) + " AS ap1, " +
                            oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"HTE_PA2", "''", "HTE_AP2"}) + " AS ap2, HTE_NOM AS nombre, " + 
                            "ROL_COD, ROL_DES ";

                    String where = "EXT_MUN = " + tvo1.getCodMunicipio() + " AND EXT_EJE = " + tvo1.getEjercicio() +
                            " AND EXT_NUM = '" + tvo1.getNumero() + "' AND MOSTRAR = 1";

                    String[] join = new String[8];

                    if (tvo1.isExpHistorico())
                        join[0] = "HIST_E_EXT";
                    else
                        join[0] = "E_EXT";

                    join[1] = "LEFT";
                    join[2] = "T_HTE";
                    join[3] = "EXT_TER = HTE_TER AND EXT_NVR = HTE_NVR";
                    join[4] = "LEFT";
                    join[5] = "E_ROL";
                    join[6] = "ROL_MUN = EXT_MUN AND ROL_PRO = EXT_PRO AND ROL_COD = EXT_ROL";
                    join[7] = "false";

                    sql = oad.join(from, where, join);

                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);

                    String titular;
                    if (paraCSV) {
                        titular = "\"";
                        while (rs.next()) {
                            if (!"\"".equals(titular))
                                titular += "\r";
                            //formato del interesado
                             String ap1=rs.getString("ap1");
                             String ap2=rs.getString("ap2");
                             String nombre=rs.getString("nombre");  
                             titular+=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);

                             int codRol = rs.getInt("ROL_COD");
                             String desRol = rs.getString("ROL_DES");
                             titular += " (" + codRol + " - " + desRol + ")";
                        }
                        titular += "\"";
                    } else {
                        titular = "";
                        while (rs.next()) {
                            if (!"".equals(titular))
                                titular += ">>";
                            //formato del interesado
                             String ap1=rs.getString("ap1");
                             String ap2=rs.getString("ap2");
                             String nombre=rs.getString("nombre");
                             titular+=FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);

                             int codRol = rs.getInt("ROL_COD");
                             String desRol = rs.getString("ROL_DES");
                             titular += ">>(" + codRol + " - " + desRol + ")";
                        }
                    }
                    tvo1.setTitular(titular);
                    rs.close();
                    // Como estamos en consulta, buscaremos los datos referentes a los
                    // tramites abiertos de cada expediente.
                    String condFechaFinTramite;
                    if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = "IS NULL";
                    else condFechaFinTramite = "IS NOT NULL";
                    sql = "SELECT " +
                            oad.convertir("CRO_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FINITRAMITE, " +
                            oad.convertir("CRO_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FFINTRAMITE, " +
                            "TML_VALOR " +
                            "FROM E_CRO, E_TML " +
                            "WHERE CRO_MUN = TML_MUN AND CRO_PRO = TML_PRO AND CRO_TRA = TML_TRA " +
                            "AND CRO_NUM = '" + tvo1.getNumero() + "' AND CRO_FEF " + condFechaFinTramite;

                    if (consExpVO.getCodTramite() != null && !"".equals(consExpVO.getCodTramite())&& !"todos".equals(consExpVO.getCodTramite()))
                        sql += " AND CRO_TRA = " + consExpVO.getCodTramite();
                    
                    if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                    rs = st.executeQuery(sql);

                    Vector<String> tramitesIniciados = new Vector<String>();
                    Vector<String> fechasInicioTramites = new Vector<String>();
                    Vector<String> fechasFinTramites = new Vector<String>();
                    while (rs.next()) {
                        tramitesIniciados.add(rs.getString("TML_VALOR"));
                        fechasInicioTramites.add(rs.getString("FINITRAMITE"));
                        fechasFinTramites.add(rs.getString("FFINTRAMITE"));
                    }

                    if ( tramitesIniciados.size() > 0) {
                        tvo1.setListaTramitesAbiertos(tramitesIniciados);
                        tvo1.setListaFechasInicioTramitesAbiertos(fechasInicioTramites);
                        tvo1.setListaFechasFinTramitesAbiertos(fechasFinTramites);
                        listaExpedientes.addElement(tvo1);
                    }

                    rs.close();

                   
                }
                st.close();

            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                throw new AnotacionRegistroException(m_ConfigError.getString("Error.DefinicionProcedimientosDAO.buscar"), e);
            }
            finally {
                try {
                    if(consultaInsensitiva){
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                        Statement st_alter;
                        String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                        String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                        st_alter = con.createStatement();
                        st_alter.executeQuery(alter1);
                        st_alter.executeQuery(alter2);

                        st_alter.close();
                    }
                    }
                    if (con != null) con.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
        }

        return lista;
    }



     /**
      * Devuelve la consulta SQL en la que se recuperan los expedientes que ha iniciado o a tramitado un determinado usuario pero ordenando
      * por la columna de interesado.
      * @param consExpVO: Objeto con los criterios de filtrado d ela consulta
      * @param oad: AdaptadorSQLBD
      * @param transformador
      * @param params: Parámetros de conexión a la base de datos
      * @param uVO: Objeto con los datos del usuario
      * @param buscarInicioProc
      * @return String con la consulta SQL
      * @throws es.altia.util.conexion.BDException si ocurre algún error
      */
     private String obtenerSentenciaConFiltroByInteresadoUsuarioTramitador(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO, boolean buscarInicioProc, String tipoOrden)
            throws BDException {


        m_Log.debug("eMPIEZO A obtenerSentenciaConFiltroByInteresadoUsuarioTramitador");
        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }    
            
        String from = " DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                 "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom, 'N' AS EXHISTORICO";

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            from += ", DNN_RCA," + GlobalNames.ESQUEMA_GENERICO + "A_USU,";
        }

        /*
        from=from+", "+"T_HTE.HTE_TER, " +
                 oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1", "T_HTE.HTE_PA2",  "T_HTE.HTE_AP2",  "T_HTE.HTE_NOM"})+ " AS NOMBRE";
        */
        
        // #225966: ordena por interesado sin duplicar expedientes
        String funcionAgregado;
        if(tipoOrden.equals("DESC")) funcionAgregado = "MAX"; 
        else funcionAgregado = "MIN"; 
        
        
        StringBuilder campoNombre = new StringBuilder("SELECT ");
        campoNombre.append(funcionAgregado).append("( UPPER(")
                .append(oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1", "T_HTE.HTE_PA2",  "T_HTE.HTE_AP2",  "T_HTE.HTE_NOM"}))
                .append(")) FROM E_EXT TER,T_HTE WHERE EXP_MUN = TER.EXT_MUN AND EXP_EJE = TER.EXT_EJE AND EXP_NUM = TER.EXT_NUM")
                .append(" AND TER.EXT_TER=T_HTE.HTE_TER AND TER.EXT_NVR=T_HTE.HTE_NVR");
        
        from += ", (" + campoNombre.toString() + ") AS NOMBRE";


        ArrayList<String> join = new ArrayList<String>();



        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
         join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");

       
        /*
        join.add("LEFT");
        join.add("E_EXT TER");
        join.add("EXP_MUN = TER.EXT_MUN AND EXP_EJE = TER.EXT_EJE AND EXP_NUM = TER.EXT_NUM");
       */
        // #225966: SOLO HACE EL JOIN CON LAS TABLAS E_EXT Y E_THE SI SE FILTRA POR TITUTAR (POR E_EXT TAMBIÉN SI SE FILTRA POR ROL)
        if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
            join.add("LEFT");
            join.add("E_EXT");
            join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");
            
            /*** SE BUSCAN LOS EXPEDIENTES QUE TIENEN COMO INTERESADO ALGÚN TERCERO CON UN DETERMINADO DOCUMENTO ***/
            if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                join.add("LEFT");
                join.add("T_HTE");
                join.add("EXT_TER = T_HTE.HTE_TER AND EXT_NVR = T_HTE.HTE_NVR");                
            }            
            
        }else{ //NO hay titular pero hay Rol 
       
            if(consExpVO.getCodigoRol()!=null && !"".equals(consExpVO.getCodigoRol().trim())) {  
              join.add("INNER");   
              join.add("E_EXT");
              join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");            
           }
        }

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }

        // Caso de que tengamos ejercicio, numero o tipo de anotacion.
        if ((consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) ||
            (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) ||
            (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
            join.add("LEFT");
            join.add("E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");
            join.add("LEFT");
            join.add("E_EXREXT");
            join.add("EXP_MUN = EXREXT_MUN AND EXP_NUM = EXREXT_NUM");
        }

        //Los joins para ordenar por interesado

        
            /***  ORIGINAL
             join.add("LEFT");
             join.add("E_EXT A");
             join.add("E_EXP.EXP_EJE=A.EXT_EJE AND E_EXP.EXP_MUN=A.EXT_MUN AND E_EXP.EXP_NUM=A.EXT_NUM");
             join.add("LEFT");
             join.add("T_HTE");
             join.add("A.EXT_TER=T_HTE.HTE_TER AND A.EXT_NVR=T_HTE.HTE_NVR");
             ***/
        
             /*** OSCAR ***/
        /*
             join.add("LEFT");
             join.add("T_HTE");
             join.add("TER.EXT_TER=T_HTE.HTE_TER AND TER.EXT_NVR=T_HTE.HTE_NVR");
        */
            /*** OSCAR ***/


        join.add("false");

        /** ORIGINAL
        String where = "(A.EXT_TER IS NULL OR A.EXT_TER=(SELECT MIN(EXT_TER) FROM E_EXT WHERE EXT_NUM=EXP_NUM AND MOSTRAR=1)) AND PML_CMP = 'NOM' AND PML_LENG = '"+
                m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";
        ***/
        
        /*** OSCAR ***/
        String where = " PML_CMP = 'NOM' AND PML_LENG = '"+
                m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";
        /*** OSCAR ***/
        
        /***  SI SE FILTRA POR EXPEDIENTE INICIADO DESDE REGISTRO TELEMATICO ****/
        if((consExpVO.getBusqTelematicos()!=null && !"".equals(consExpVO.getBusqTelematicos())) && "1".equals(consExpVO.getBusqTelematicos())){ 
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                where += " EXP_NUM IN (SELECT EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            } else {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            }
        }

        // Se comprueba que el usuario sea el de inicio del expediente y además que tenga permiso sobre la uor de inicio del expediente
        where += "(EXP_USU=" + uVO.getIdUsuario() + " AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) ";
        
        // Se comprueba que el usuario de fin del trámite sea el usuario y que tenga
        // permiso sobre la unidad tramitadora del trámite finalizado del expediente
        where += " OR ( CRO_USF=" + uVO.getIdUsuario() + " AND (exists ("
                  + "SELECT DISTINCT UOU_UOR "
                  + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                  + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                  + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)) ";


        if (buscarInicioProc) {
                m_Log.debug("***  buscarInicioProc");
            where += " OR (exists (SELECT DISTINCT UOU_UOR " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI " +
                    "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " " +
                    "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))";
        }

        where += "))";

        String[] parametros = new String[2];
 m_Log.debug("*** ANTES  getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getCodProcedimiento() != null) {
             m_Log.debug("***  getCodProcedimiento"+consExpVO.getCodProcedimiento());
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
      m_Log.debug("*** DESPUES getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                parametros[0] = "'"+consExpVO.getNumeroExpediente().trim()+"'";
                String condicion = "EXP_NUM="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        if (consExpVO.getEjercicioExpediente() != null) {
            if (!"".equals(consExpVO.getEjercicioExpediente())) {
               
                where += " AND EXP_EJE=" + consExpVO.getEjercicioExpediente();
                
            }
            
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) {
                    condicion = "0";
                    where+=" AND EXP_EST = 0 ";
                }
                else if ("cerrado".equals(consExpVO.getEstado())) {
                    //condicion = "9";
                    where += " AND (EXP_EST=9 OR EXP_EST=1 )";

                }

            }
        }
        
        // #238255: Se añaden comprobaciones de distinto de vacío para añadir las condiciones del where
        if (consExpVO.getTitular() != null) {
            if (!CONSULTAR_EXPEDIENTES_POR_DOCUMENTO) {
                if (!"".equals(consExpVO.getTercero())) {
                    where += " AND TER.EXT_TER = " + consExpVO.getTercero().trim();
                }
            }else {    
                // SE CONSULTA POR DOCUMENTO DEL TERCERO Y NO POR CÓDIGO DE TERCERO
                if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                    where += " AND T_HTE.HTE_DOC='" + consExpVO.getDocumentoTercero() + "' AND T_HTE.HTE_TID=" + consExpVO.getTipoDocumentoTercero() + " ";
                }
            }
        }
        
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = "TRA_CLS="+consExpVO.getCodClasifTramite();
                if (!"".equals(condicion)) {
                    where += " AND " + condicion+" AND CRO_FEF IS NULL";
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }

        String condFechaFinTramite;
        if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = " AND CRO_FEF IS NULL";
        else condFechaFinTramite = " AND CRO_FEF IS NOT NULL";

        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
              
                    where += " AND CRO_UTR=" + consExpVO.getCodUnidadTram().trim();

            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_UTR = " + cUT;
                    else where += " OR CRO_UTR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += condFechaFinTramite + ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
              
                where += " AND CRO_TRA=" + consExpVO.getCodTramite().trim();

            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }


        // Si tenemos ejercicio de anotacion
        if (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion())) {
            where += " AND (EXR_EJR = " + consExpVO.getEjercicioAnotacion().trim() +
                       " OR EXREXT_EJR = " + consExpVO.getEjercicioAnotacion().trim() + ")";
        }

        // Si tenemos numero de anotacion
        if (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) {
           where += " AND (EXR_NRE = " + consExpVO.getNumeroAnotacion().trim() +
                      " OR EXREXT_NRE = " + consExpVO.getNumeroAnotacion().trim() + ")";
        }

        // Si tenemos tipo de anotacion ('E' o 'S')
        if (consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) {
            where += " AND (EXR_TIP = '" + consExpVO.getCodTipoAnotacion().trim() +
                      "' OR EXREXT_TIP = '" + consExpVO.getCodTipoAnotacion().trim() + "')";
        }

        // Si tenemos asunto
            if (consExpVO.getAsunto() != null) {
                if (!"".equals(consExpVO.getAsunto().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_ASU COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getAsunto().trim() + "%')";
                    else condicion="((EXP_ASU LIKE '%"+consExpVO.getAsunto().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
        // Si tenemos observaciones
            if (consExpVO.getObservaciones() != null) {
                if (!"".equals(consExpVO.getObservaciones().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_OBS COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getObservaciones().trim() + "%')";
                    else condicion="((EXP_OBS LIKE '%"+consExpVO.getObservaciones().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }

        // #225966: al filtrar por rol y ordenar por interesado se perdía el filtro por rol
        if (consExpVO.getCodigoRol() != null) {
            if (!"".equals(consExpVO.getCodigoRol().trim())) {
                where += " AND  EXT_ROL= "+ consExpVO.getCodigoRol() ;}
        }

        // óscar
        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientes".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND CRO_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientesVolumen".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND EXP_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesHistoricos".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesCerrados".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL AND (EXP_EST=1 or EXP_EST=9)";
        }

        // óscar

        return oad.join(from, where, join.toArray(new String[]{}));
    }

/**
     * Devuelve la consulta SQL en la que se recuperan los expedientes activos y de histórico que ha  
     * iniciado o ha tramitado un determinado usuario pero ordenando por la columna de interesado.
     * @param consExpVO: Objeto con los criterios de filtrado
     * @param oad: AdaptadorSQLBD
     * @param transformador: TransformacionAtributoSelect
     * @param params: Parámetros de conexión a la base de datos
     * @param uVO: Objeto con los datos del usuario
     * @param buscarInicioProc
     * @return
     * @throws es.altia.util.conexion.BDException
     */
     private String obtenerSentenciaConFiltroByInteresadoUsuarioTramitadorActivosHistorico(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO, boolean buscarInicioProc, String tipoOrden)
            throws BDException {

        m_Log.debug("eMPIEZO A obtenerSentenciaConFiltroUsuarioTramitadorHistorico");
        String queryActivos = obtenerSentenciaConFiltroByInteresadoUsuarioTramitador(consExpVO, oad, transformador, params, uVO, buscarInicioProc, tipoOrden);
        String queryHistorico = obtenerSentenciaConFiltroByInteresadoUsuarioTramitadorHistorico(consExpVO, oad, transformador, params, uVO, buscarInicioProc, tipoOrden);
        String resultado = queryActivos + " UNION ALL " + queryHistorico;
        
        return resultado;
     }
     
     /**
      * Devuelve la consulta SQL en la que se recuperan los expedientes de histórico que ha iniciado o ha 
      * tramitado un determinado usuario pero ordenando por la columna de interesado.
      * @param consExpVO: Objeto con los criterios de filtrado d ela consulta
      * @param oad: AdaptadorSQLBD
      * @param transformador
      * @param params: Parámetros de conexión a la base de datos
      * @param uVO: Objeto con los datos del usuario
      * @param buscarInicioProc
      * @return String con la consulta SQL
      * @throws es.altia.util.conexion.BDException si ocurre algún error
      */
     private String obtenerSentenciaConFiltroByInteresadoUsuarioTramitadorHistorico(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO, boolean buscarInicioProc, String tipoOrden)
            throws BDException {


        m_Log.debug("eMPIEZO A obtenerSentenciaConFiltroByInteresadoUsuarioTramitador");
        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }    
            
        String from = " DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom, 'S' AS EXHISTORICO";

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            from += ", DNN_RCA," + GlobalNames.ESQUEMA_GENERICO + "A_USU,";
        }

        /*
        from=from+", "+"T_HTE.HTE_TER, " +
                 oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1", "T_HTE.HTE_PA2",  "T_HTE.HTE_AP2",  "T_HTE.HTE_NOM"})+ " AS NOMBRE";
        */
        
        // #225966: ordena por interesado sin duplicar expedientes
        String funcionAgregado;
        if(tipoOrden.equals("DESC")) funcionAgregado = "MAX"; 
        else funcionAgregado = "MIN"; 
        
        
        StringBuilder campoNombre = new StringBuilder("SELECT ");
        campoNombre.append(funcionAgregado).append("( UPPER(")
                .append(oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1", "T_HTE.HTE_PA2",  "T_HTE.HTE_AP2",  "T_HTE.HTE_NOM"}))
                .append(")) FROM HIST_E_EXT TER,T_HTE WHERE EXP_MUN = TER.EXT_MUN AND EXP_EJE = TER.EXT_EJE AND EXP_NUM = TER.EXT_NUM")
                .append(" AND TER.EXT_TER=T_HTE.HTE_TER AND TER.EXT_NVR=T_HTE.HTE_NVR");
        
        from += ", (" + campoNombre.toString() + ") AS NOMBRE";


        ArrayList<String> join = new ArrayList<String>();



        join.add("HIST_E_EXP");
        join.add("LEFT");
        join.add("HIST_E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
         join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");

       
        /*
        join.add("LEFT");
        join.add("HIST_E_EXT TER");
        join.add("EXP_MUN = TER.EXT_MUN AND EXP_EJE = TER.EXT_EJE AND EXP_NUM = TER.EXT_NUM");
        */
        // #225966: SOLO HACE EL JOIN CON LAS TABLAS E_EXT Y E_THE SI SE FILTRA POR TITUTAR (POR E_EXT TAMBIÉN SI SE FILTRA POR ROL)
        if (consExpVO.getTitular() != null && !"".equals(consExpVO.getTitular())) {
            join.add("LEFT");
            join.add("HIST_E_EXT");
            join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");
            
            /*** SE BUSCAN LOS EXPEDIENTES QUE TIENEN COMO INTERESADO ALGÚN TERCERO CON UN DETERMINADO DOCUMENTO ***/
            if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero())
                    && consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                join.add("LEFT");
                join.add("T_HTE");
                join.add("EXT_TER = T_HTE.HTE_TER AND EXT_NVR = T_HTE.HTE_NVR");                
            }            
            
        }else{ //NO hay titular pero hay Rol 
       
            if(consExpVO.getCodigoRol()!=null && !"".equals(consExpVO.getCodigoRol().trim())) {  
              join.add("INNER");   
              join.add("HIST_E_EXT");
              join.add("EXP_MUN = EXT_MUN AND EXP_EJE = EXT_EJE AND EXP_NUM = EXT_NUM");            
           }
        }

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }

        // Caso de que tengamos ejercicio, numero o tipo de anotacion.
        if ((consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) ||
            (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) ||
            (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
            join.add("LEFT");
            join.add("HIST_E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");
            join.add("LEFT");
            join.add("HIST_E_EXREXT");
            join.add("EXP_MUN = EXREXT_MUN AND EXP_NUM = EXREXT_NUM");
        }

        //Los joins para ordenar por interesado

        
            /***  ORIGINAL
             join.add("LEFT");
             join.add("HIST_E_EXT A");
             join.add("HIST_E_EXP.EXP_EJE=A.EXT_EJE AND HIST_E_EXP.EXP_MUN=A.EXT_MUN AND HIST_E_EXP.EXP_NUM=A.EXT_NUM");
             join.add("LEFT");
             join.add("T_HTE");
             join.add("A.EXT_TER=T_HTE.HTE_TER AND A.EXT_NVR=T_HTE.HTE_NVR");
             ***/
        
             /*** OSCAR ***/     
        /*
             join.add("LEFT");
             join.add("T_HTE");
             join.add("TER.EXT_TER=T_HTE.HTE_TER AND TER.EXT_NVR=T_HTE.HTE_NVR");
        */
            /*** OSCAR ***/


        join.add("false");

        /** ORIGINAL
        String where = "(A.EXT_TER IS NULL OR A.EXT_TER=(SELECT MIN(EXT_TER) FROM HIST_E_EXT WHERE EXT_NUM=EXP_NUM AND MOSTRAR=1)) AND PML_CMP = 'NOM' AND PML_LENG = '"+
                m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";
        ***/
        
        /*** OSCAR ***/
        String where = " PML_CMP = 'NOM' AND PML_LENG = '"+
                m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";
        /*** OSCAR ***/
        
        /***  SI SE FILTRA POR EXPEDIENTE INICIADO DESDE REGISTRO TELEMATICO ****/
        if((consExpVO.getBusqTelematicos()!=null && !"".equals(consExpVO.getBusqTelematicos())) && "1".equals(consExpVO.getBusqTelematicos())){ 
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                where += " EXP_NUM IN (SELECT EXR_NUM FROM HIST_E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            } else {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM HIST_E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            }
        }

        // Se comprueba que el usuario sea el de inicio del expediente y además que tenga permiso sobre la uor de inicio del expediente
        where += "(EXP_USU=" + uVO.getIdUsuario() + " AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) ";
        
        // Se comprueba que el usuario de fin del trámite sea el usuario y que tenga
        // permiso sobre la unidad tramitadora del trámite finalizado del expediente
        where += " OR ( CRO_USF=" + uVO.getIdUsuario() + " AND (exists ("
                  + "SELECT DISTINCT UOU_UOR "
                  + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                  + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                  + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)) ";


        if (buscarInicioProc) {
                m_Log.debug("***  buscarInicioProc");
            where += " OR (exists (SELECT DISTINCT UOU_UOR " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI " +
                    "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " " +
                    "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))";
        }

        where += "))";

        String[] parametros = new String[2];
 m_Log.debug("*** ANTES  getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getCodProcedimiento() != null) {
             m_Log.debug("***  getCodProcedimiento"+consExpVO.getCodProcedimiento());
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
      m_Log.debug("*** DESPUES getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                parametros[0] = "'"+consExpVO.getNumeroExpediente().trim()+"'";
                String condicion = "EXP_NUM="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        if (consExpVO.getEjercicioExpediente() != null) {
            if (!"".equals(consExpVO.getEjercicioExpediente())) {
               
                where += " AND EXP_EJE=" + consExpVO.getEjercicioExpediente();
                
            }
            
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) {
                    condicion = "0";
                    where+=" AND EXP_EST = 0 ";
                }
                else if ("cerrado".equals(consExpVO.getEstado())) {
                    //condicion = "9";
                    where += " AND (EXP_EST=9 OR EXP_EST=1 )";

                }

            }
        }
        
        if (consExpVO.getTitular() != null) {
            if (!CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && !"".equals(consExpVO.getTitular()) && !"".equals(consExpVO.getTercero())) {
                where += " AND TER.EXT_TER = " + consExpVO.getTercero().trim();
            }else
            if (CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getTipoDocumentoTercero()!=null && consExpVO.getDocumentoTercero()!=null){    
                where += " AND T_HTE.HTE_DOC='" + consExpVO.getDocumentoTercero() + "' AND T_HTE.HTE_TID=" + consExpVO.getTipoDocumentoTercero() + " ";
            }
        }
        
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = "TRA_CLS="+consExpVO.getCodClasifTramite();
                if (!"".equals(condicion)) {
                    where += " AND " + condicion+" AND CRO_FEF IS NULL";
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }

        String condFechaFinTramite;
        if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = " AND CRO_FEF IS NULL";
        else condFechaFinTramite = " AND CRO_FEF IS NOT NULL";

        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
              
                    where += " AND CRO_UTR=" + consExpVO.getCodUnidadTram().trim();

            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_UTR = " + cUT;
                    else where += " OR CRO_UTR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += condFechaFinTramite + ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
              
                where += " AND CRO_TRA=" + consExpVO.getCodTramite().trim();

            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }


        // Si tenemos ejercicio de anotacion
        if (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion())) {
            where += " AND (EXR_EJR = " + consExpVO.getEjercicioAnotacion().trim() +
                       " OR EXREXT_EJR = " + consExpVO.getEjercicioAnotacion().trim() + ")";
        }

        // Si tenemos numero de anotacion
        if (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) {
           where += " AND (EXR_NRE = " + consExpVO.getNumeroAnotacion().trim() +
                      " OR EXREXT_NRE = " + consExpVO.getNumeroAnotacion().trim() + ")";
        }

        // Si tenemos tipo de anotacion ('E' o 'S')
        if (consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) {
            where += " AND (EXR_TIP = '" + consExpVO.getCodTipoAnotacion().trim() +
                      "' OR EXREXT_TIP = '" + consExpVO.getCodTipoAnotacion().trim() + "')";
        }

        // Si tenemos asunto
            if (consExpVO.getAsunto() != null) {
                if (!"".equals(consExpVO.getAsunto().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_ASU COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getAsunto().trim() + "%')";
                    else condicion="((EXP_ASU LIKE '%"+consExpVO.getAsunto().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
        // Si tenemos observaciones
            if (consExpVO.getObservaciones() != null) {
                if (!"".equals(consExpVO.getObservaciones().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_OBS COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getObservaciones().trim() + "%')";
                    else condicion="((EXP_OBS LIKE '%"+consExpVO.getObservaciones().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }

        // #225966: al filtrar por rol y ordenar por interesado se perdía el filtro por rol
        if (consExpVO.getCodigoRol() != null) {
            if (!"".equals(consExpVO.getCodigoRol().trim())) {
                where += " AND  EXT_ROL= "+ consExpVO.getCodigoRol() ;}
        }

        // óscar
        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientes".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND CRO_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientesVolumen".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND EXP_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesHistoricos".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesCerrados".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL AND (EXP_EST=1 or EXP_EST=9)";
        }

        // óscar

        return oad.join(from, where, join.toArray(new String[]{}));
    }



      private String obtenerSentenciaConFiltroByInteresadoUsuarioTramitadorImprimir(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                             TransformacionAtributoSelect transformador, String[] params,
                                             UsuarioValueObject uVO, boolean buscarInicioProc)
            throws BDException {


        m_Log.debug("eMPIEZO A obtenerSentenciaConFiltroByInteresadoUsuarioTramitador");
        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }    
            
        String from = " DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom, EXT_TER,EXT_ROL,"+
                 oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1"})+ " AS ap1, "+
                oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA2",  "T_HTE.HTE_AP2"})+ " AS ap2, "
                + " HTE_NOM AS nombre";


        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            from += ", DNN_RCA," + GlobalNames.ESQUEMA_GENERICO + "A_USU,";
        }

        from=from+", "+"T_HTE.HTE_TER, " +
                 oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1", "T_HTE.HTE_PA2",  "T_HTE.HTE_AP2",  "T_HTE.HTE_NOM"})+ " AS NOMBRE";



        ArrayList<String> join = new ArrayList<String>();



          join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
         join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");

        
        join.add("LEFT");
        join.add("E_EXT TER");
        join.add("EXP_MUN = TER.EXT_MUN AND EXP_EJE = TER.EXT_EJE AND EXP_NUM = TER.EXT_NUM");
       

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }

        // Caso de que tengamos ejercicio, numero o tipo de anotacion.
        if ((consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) ||
            (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) ||
            (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
            join.add("LEFT");
            join.add("E_EXR");
            join.add("EXP_MUN = EXR_MUN AND EXP_EJE = EXR_EJE AND EXP_NUM = EXR_NUM");
            join.add("LEFT");
            join.add("E_EXREXT");
            join.add("EXP_MUN = EXREXT_MUN AND EXP_NUM = EXREXT_NUM");
        }

        //Los joins para ordenar por interesado

        
            /***  ORIGINAL
             join.add("LEFT");
             join.add("E_EXT A");
             join.add("E_EXP.EXP_EJE=A.EXT_EJE AND E_EXP.EXP_MUN=A.EXT_MUN AND E_EXP.EXP_NUM=A.EXT_NUM");
             join.add("LEFT");
             join.add("T_HTE");
             join.add("A.EXT_TER=T_HTE.HTE_TER AND A.EXT_NVR=T_HTE.HTE_NVR");
             ***/
        
             /*** OSCAR ***/        
             join.add("LEFT");
             join.add("T_HTE");
             join.add("TER.EXT_TER=T_HTE.HTE_TER AND TER.EXT_NVR=T_HTE.HTE_NVR");
            /*** OSCAR ***/


        join.add("false");

        /** ORIGINAL
        String where = "(A.EXT_TER IS NULL OR A.EXT_TER=(SELECT MIN(EXT_TER) FROM E_EXT WHERE EXT_NUM=EXP_NUM AND MOSTRAR=1)) AND PML_CMP = 'NOM' AND PML_LENG = '"+
                m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";
        ***/
        
        /*** OSCAR ***/
        String where = " PML_CMP = 'NOM' AND PML_LENG = '"+
                m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";
        /*** OSCAR ***/
        
        /***  SI SE FILTRA POR EXPEDIENTE INICIADO DESDE REGISTRO TELEMATICO ****/
        if((consExpVO.getBusqTelematicos()!=null && !"".equals(consExpVO.getBusqTelematicos())) && "1".equals(consExpVO.getBusqTelematicos())){ 
            if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver"))){
                where += " EXP_NUM IN (SELECT EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            } else {
                where += " (EXP_MUN,EXP_PRO,EXP_NUM) IN (SELECT EXR_MUN,EXR_PRO,EXR_NUM FROM E_EXR JOIN R_RES ON EXR_UOR=RES_UOR AND EXR_TIP=RES_TIP AND EXR_EJR=RES_EJE AND EXR_NRE=RES_NUM"
                    + " WHERE EXP_NUM = EXR_NUM AND EXP_EJE = EXR_EJE AND EXP_MUN = EXR_MUN AND EXP_PRO=EXR_PRO"
                    + " AND REGISTRO_TELEMATICO=1) AND ";         
            }
        }

        // Se comprueba que el usuario sea el de inicio del expediente y además que tenga permiso sobre la uor de inicio del expediente
        where += "(EXP_USU=" + uVO.getIdUsuario() + " AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) ";
        
        // Se comprueba que el usuario de fin del trámite sea el usuario y que tenga
        // permiso sobre la unidad tramitadora del trámite finalizado del expediente
        where += " OR ( CRO_USF=" + uVO.getIdUsuario() + " AND (exists ("
                  + "SELECT DISTINCT UOU_UOR "
                  + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                  + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                  + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)) ";


        if (buscarInicioProc) {
                m_Log.debug("***  buscarInicioProc");
            where += " OR (exists (SELECT DISTINCT UOU_UOR " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU, E_PUI " +
                    "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " " +
                    "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = PUI_COD AND PUI_MUN = EXP_MUN AND PUI_PRO = EXP_PRO))";
        }

        where += "))";

        String[] parametros = new String[2];
 m_Log.debug("*** ANTES  getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getCodProcedimiento() != null) {
             m_Log.debug("***  getCodProcedimiento"+consExpVO.getCodProcedimiento());
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
      m_Log.debug("*** DESPUES getCodProcedimiento"+consExpVO.getCodProcedimiento());
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                parametros[0] = "'"+consExpVO.getNumeroExpediente().trim()+"'";
                String condicion = "EXP_NUM="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        
        if (consExpVO.getEjercicioExpediente() != null) {
            if (!"".equals(consExpVO.getEjercicioExpediente())) {
               
                where += " AND EXP_EJE=" + consExpVO.getEjercicioExpediente();
                
            }
            
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) {
                    condicion = "0";
                    where+=" AND EXP_EST = 0 ";
                }
                else if ("cerrado".equals(consExpVO.getEstado())) {
                    //condicion = "9";
                    where += " AND (EXP_EST=9 OR EXP_EST=1 )";

                }

            }
        }
        
        if (consExpVO.getTitular() != null) {
            if (!CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && !"".equals(consExpVO.getTitular()) && !"".equals(consExpVO.getTercero())) {
                where += " AND TER.EXT_TER = " + consExpVO.getTercero().trim();
            }else
            if (CONSULTAR_EXPEDIENTES_POR_DOCUMENTO && consExpVO.getTipoDocumentoTercero()!=null && consExpVO.getDocumentoTercero()!=null){    
                where += " AND T_HTE.HTE_DOC='" + consExpVO.getDocumentoTercero() + "' AND T_HTE.HTE_TID=" + consExpVO.getTipoDocumentoTercero() + " ";
            }
        }
        
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = "TRA_CLS="+consExpVO.getCodClasifTramite();
                if (!"".equals(condicion)) {
                    where += " AND " + condicion+" AND CRO_FEF IS NULL";
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }

        String condFechaFinTramite;
        if (consExpVO.isVerTramitesAbiertos()) condFechaFinTramite = " AND CRO_FEF IS NULL";
        else condFechaFinTramite = " AND CRO_FEF IS NOT NULL";

        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
              
                    where += " AND CRO_UTR=" + consExpVO.getCodUnidadTram().trim();

            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_UTR = " + cUT;
                    else where += " OR CRO_UTR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += condFechaFinTramite + ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
              
                where += " AND CRO_TRA=" + consExpVO.getCodTramite().trim();

            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }


        // Si tenemos ejercicio de anotacion
        if (consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion())) {
            where += " AND (EXR_EJR = " + consExpVO.getEjercicioAnotacion().trim() +
                       " OR EXREXT_EJR = " + consExpVO.getEjercicioAnotacion().trim() + ")";
        }

        // Si tenemos numero de anotacion
        if (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) {
           where += " AND (EXR_NRE = " + consExpVO.getNumeroAnotacion().trim() +
                      " OR EXREXT_NRE = " + consExpVO.getNumeroAnotacion().trim() + ")";
        }

        // Si tenemos tipo de anotacion ('E' o 'S')
        if (consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) {
            where += " AND (EXR_TIP = '" + consExpVO.getCodTipoAnotacion().trim() +
                      "' OR EXREXT_TIP = '" + consExpVO.getCodTipoAnotacion().trim() + "')";
        }

        // Si tenemos asunto
            if (consExpVO.getAsunto() != null) {
                if (!"".equals(consExpVO.getAsunto().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_ASU COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getAsunto().trim() + "%')";
                    else condicion="((EXP_ASU LIKE '%"+consExpVO.getAsunto().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }
        // Si tenemos observaciones
            if (consExpVO.getObservaciones() != null) {
                if (!"".equals(consExpVO.getObservaciones().trim())) {
                    String condicion = "";
                    if(params[0]!=null && (params[0].equals("SQLSERVER") || params[0].equals("sqlserver")))
                        condicion = "(EXP_OBS COLLATE Latin1_General_CI_AI LIKE '%" + consExpVO.getObservaciones().trim() + "%')";
                    else condicion="((EXP_OBS LIKE '%"+consExpVO.getObservaciones().trim()+"%'))";
                    if (!"".equals(condicion)) {
                        where += " AND " +condicion ;
                    }
                }
            }


        // óscar
        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientes".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND CRO_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesPendientesVolumen".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites pendientes.
            // A mayores se comprueba que el estado del expediente no esté finalizado
            where += " AND EXP_FEF IS NULL AND (EXP_EST!=1 AND EXP_EST!=9)";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesHistoricos".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL";
        }

        if(consExpVO.isDesdeInformesGestion() && "expedientesCerrados".equals(consExpVO.getTipo())){
            // Si la consulta se hace desde informes de gestión y se pretenden recuperar expedientes con trámites cerrados
            where += " AND CRO_FEF IS NOT NULL AND (EXP_EST=1 or EXP_EST=9)";
        }

        // óscar

        return oad.join(from, where, join.toArray(new String[]{}));
    }





     /**
      * Recupera la sentencia SQL que recupera los expedientes tramitados o finalizados por un determinado usuario.
      * Ordena por campos suplementarios si hay criterios de filtrado
      * @param consExpVO: Objeto con los criterios de filtrado
      * @param oad: AdaptadorSQLBD
      * @param transformador
      * @param params: Parámetros de conexión a la base de datos
      * @param uVO: Objeto con los datos del usuario
      * @return String con el SQL
      * @throws es.altia.util.conexion.BDException
      */
     private String obtenerSentenciaConFiltroCampoSuplementariosByInteresadoUsuarioTramitador(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                                                TransformacionAtributoSelect transformador, String[] params, UsuarioValueObject uVO,int tam)
            throws BDException {

        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }
        
        String from = "  DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom"; // Para el orden 

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) from += ", DNN_RCA";

        from=from+", "+"T_HTE.HTE_TER, " +
                 oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1", "T_HTE.HTE_PA2",  "T_HTE.HTE_AP2",  "T_HTE.HTE_NOM"})+ " AS NOMBRE";


        ArrayList<String> join = new ArrayList<String>();


        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
        join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");


        
        join.add("LEFT");
        join.add("E_EXT TER");
        join.add("EXP_MUN = TER.EXT_MUN AND EXP_EJE = TER.EXT_EJE AND EXP_NUM = TER.EXT_NUM");
      

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }
        
        /**
            join.add("LEFT");
            join.add("E_EXREXT");
            join.add("EXP_MUN = EXREXT_MUN AND EXP_NUM = EXREXT_NUM"); **/
        //Los joins para ordenar por interesado

             join.add("LEFT");
             join.add("E_EXT A");
             join.add("E_EXP.EXP_EJE=A.EXT_EJE AND E_EXP.EXP_MUN=A.EXT_MUN AND E_EXP.EXP_NUM=A.EXT_NUM");
             join.add("LEFT");
             join.add("T_HTE");
             join.add("A.EXT_TER=T_HTE.HTE_TER AND A.EXT_NVR=T_HTE.HTE_NVR");

        HashMap datosConsulta = consExpVO.getCamposSuplementarios();
        int z = 0;
        int tamaño=tam;        
        datosConsulta.remove("TAMAÑO");
        EstructuraCampo estructura;
        String clave;
        String valor;
        String tipo;
        String tramite;
        while (z < tamaño) {
            estructura = (EstructuraCampo) datosConsulta.get(String.valueOf(z));
            clave = estructura.getCodCampo();
            valor = estructura.getValorConsulta();
            tipo = estructura.getURLPlantilla();
            tramite = estructura.getCodTramite();
            if (m_Log.isDebugEnabled()) m_Log.debug("CampoSuplementario: Existe Tramite= " + tramite);
            if (tramite == null) { // Se trata de campos suplementarios de expediente
                if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEX_" + z;
                    join.add("E_TDEX " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEX_NUM AND " + tabla + ".TDEX_COD = '" + clave + "'" +
                                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEX_VALOR", valor, false));
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUC_" + z;
                    join.add("E_TNUC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUC_NUM AND " + tabla + ".TNUC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUC_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFEC_" + z;
                    join.add("E_TFEC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFEC_NUM AND " + tabla + ".TFEC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFEC_VALOR", valor));
                } else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDE_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDE_VALOR";
                    join.add("E_TDE " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = valor;
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDE_NUM AND " + tabla + ".TDE_COD = '" + clave + "' AND " + filtroDesplegable +")");
                } else if (tipo.indexOf("Texto") != -1) {
                    boolean consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXT_" + z;
                    join.add("E_TXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXT_NUM AND " + tabla + ".TXT_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXT_VALOR", valor, false));
                } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNU_" + z;
                    join.add("E_TNU " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNU_NUM AND " + tabla + ".TNU_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNU_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFE_" + z;
                    join.add("E_TFE " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFE_NUM AND " + tabla + ".TFE_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFE_VALOR", valor));
                }
            } else { // Se trata de campos suplementarios de tramites visibles a nivel de expediente
                if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEXT_" + z;
                    join.add("E_TDEXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEXT_NUM AND " + tabla + ".TDEXT_COD = '" + clave + "' AND " + tabla + ".TDEXT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEXT_VALOR", valor, false));            
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUCT_" + z;
                    join.add("E_TNUCT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUCT_NUM AND " + tabla + ".TNUCT_COD = '" + clave + "' AND " + tabla + ".TNUCT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUCT_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFECT_" + z;
                    join.add("E_TFECT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFECT_NUM AND " + tabla + ".TFECT_COD = '" + clave + "' AND " + tabla + ".TFECT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFECT_VALOR", valor));
                } else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDET_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDET_VALOR";
                    join.add("E_TDET " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = valor;
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDET_NUM AND " + tabla + ".TDET_COD = '" + clave + "' AND " + tabla + ".TDET_TRA = " + tramite +
                            " AND " + filtroDesplegable + ")");
                } else if (tipo.indexOf("Texto") != -1) {
                    boolean consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXTT_" + z;
                    join.add("E_TXTT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXTT_NUM AND " + tabla + ".TXTT_COD = '" + clave + "' AND " + tabla + ".TXTT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXTT_VALOR", valor, false));
                } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUT_" + z;
                    join.add("E_TNUT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUT_NUM AND " + tabla + ".TNUT_COD = '" + clave + "' AND " + tabla + ".TNUT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUT_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFET_" + z;
                    join.add("E_TFET " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFET_NUM AND " + tabla + ".TFET_COD = '" + clave + "' AND " + tabla + ".TFET_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFET_VALOR", valor));
                }
            }
            z = z + 1;
        }
        // Fin Parte de Consulta  para campos Suplementarios
        join.add("false");

        String where = "(A.EXT_TER IS NULL OR A.EXT_TER=(SELECT MIN(EXT_TER) FROM E_EXT WHERE EXT_NUM=EXP_NUM AND MOSTRAR=1)) AND PML_CMP = 'NOM' AND PML_LENG = '"+m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";


          // Se comprueba que el usuario sea el de inicio del expediente y además que tenga permiso sobre la uor de inicio del expediente
        where += "(EXP_USU=" + uVO.getIdUsuario() + " AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) ";

        // Se comprueba que el usuario de fin del trámite sea el usuario y que tenga
        // permiso sobre la unidad tramitadora del trámite finalizado del expediente
        where += " OR ( CRO_USF=" + uVO.getIdUsuario() + " AND (exists ("
                  + "SELECT DISTINCT UOU_UOR "
                  + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                  + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                  + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)))) ";



        if (consExpVO.getCodProcedimiento() != null) {
            String[] parametros = new String[2];
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_NUM", consExpVO.getNumeroExpediente().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) condicion = "0)";
                else if ("cerrado".equals(consExpVO.getEstado())) condicion = "9 OR EXP_EST =1)";
                where += " AND (EXP_EST = " + condicion ;
            }
        }
		
        if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
            if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero()) &&
                    consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                String condicion = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_TID", consExpVO.getTipoDocumentoTercero().trim(), false);
                String condicion1 = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_DOC", consExpVO.getDocumentoTercero().trim(), false);
                if (!"".equals(condicion) && !"".equals(condicion1)) {
                    where += " AND " + condicion + " AND " + condicion1;
                }
            }
        } else {
            if (consExpVO.getTitular() != null) {
                if (!"".equals(consExpVO.getTitular())) {
                    String condicion = transformador.construirCondicionWhereConOperadores("TER.EXT_TER", consExpVO.getTercero().trim(), false);
                    String condicion1 = transformador.construirCondicionWhereConOperadores("TER.EXT_NVR", consExpVO.getVersionTercero().trim(), false);
                    if (!"".equals(condicion) && !"".equals(condicion1)) {
                        where += " AND " + condicion + " AND " + condicion1;
                    }
                }
            }
        }
		
        if (consExpVO.getTitular() != null) {
            if (!"".equals(consExpVO.getTitular())) {
                String condicion = transformador.construirCondicionWhereConOperadores("TER.EXT_TER", consExpVO.getTercero().trim(), false);
                String condicion1 = transformador.construirCondicionWhereConOperadores("TER.EXT_NVR", consExpVO.getVersionTercero().trim(), false);
                if (!"".equals(condicion) && !"".equals(condicion1)) {
                    where += " AND " + condicion + " AND " + condicion1;
                }
            }
        }
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("TRA_CLS", consExpVO.getCodClasifTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_UOR", consExpVO.getCodUnidadTram().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_UOR = " + cUT;
                    else where += " OR EXP_UOR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("CRO_TRA", consExpVO.getCodTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        
        
        /***
        if (consExpVO.getTipoInicio()!= null) {
            if (!"Todos".equals(consExpVO.getTipoInicio())) {
                if ("Instancia".equals(consExpVO.getTipoInicio())) {
                    where+=" AND E_EXR.EXR_TOP= 0 ";
                }
                else if ("Oficio".equals(consExpVO.getTipoInicio())) {                    
                    where += "AND (E_EXR.EXR_TOP <> 0 OR E_EXR.EXR_TOP IS NULL)";
                }                
            }
        }
        ***/
        if (consExpVO.getTipoInicio()!= null && "Instancia".equals(consExpVO.getTipoInicio())) {            
            where+=" AND EXP_NUM IN (SELECT EXR_NUM FROM E_EXR WHERE EXR_NUM=EXP_NUM AND EXR_PRO=EXP_PRO AND EXR_MUN=EXR_MUN AND EXR_TOP=0)";
        }else if ("Oficio".equals(consExpVO.getTipoInicio())) {                    
             where+=" AND EXP_NUM NOT IN (SELECT EXR_NUM FROM E_EXR WHERE EXR_NUM=EXP_NUM AND EXR_PRO=EXP_PRO AND EXR_MUN=EXR_MUN AND EXR_TOP=0)";         
        }
        
        
        
        
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }

        return oad.join(from, where, join.toArray(new String[]{}));
    }

     
     
      private String obtenerSentenciaConFiltroCampoSuplementariosByInteresadoUsuarioTramitadorImprimir(ConsultaExpedientesValueObject consExpVO, AdaptadorSQLBD oad,
                                                                TransformacionAtributoSelect transformador, String[] params, UsuarioValueObject uVO,int tam)
            throws BDException {

        ResourceBundle CONFIG_TERCERO = ResourceBundle.getBundle("Expediente");
        boolean CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        try{
            String valor = CONFIG_TERCERO.getString(uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO");
            if(valor!=null && "SI".equalsIgnoreCase(valor)){
                CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = true;
            }
        }catch(Exception e){
            m_Log.debug(" NO EXISTE LA PROPIEDAD DE CONFIGURACIÓN " + uVO.getOrgCod() + "/CONSULTA_EXPEDIENTES_TERCERO_DOCUMENTO EN Expediente.properties");
            CONSULTAR_EXPEDIENTES_POR_DOCUMENTO = false;
        }
        
        String from = " DISTINCT EXP_LOC, EXP_MUN, EXP_PRO, EXP_EJE, EXP_NUM, EXP_EST, " +
                oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fIni, " +
                oad.convertir("EXP_FEF", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS fFin, " +
                  oad.convertir("EXP_FEI", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS fIniOrd, " +
                "EXP_FEF, " +
                "PML_VALOR, EXP_FEI, EXP_ASU,UOR_NOM,usu_nom,EXT_TER,EXT_ROL, "+
                 oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1"})+ " AS ap1, "+
                oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA2",  "T_HTE.HTE_AP2"})+ " AS ap2, "
                + " HTE_NOM AS nombre";
    

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) from += ", DNN_RCA";

        from=from+", "+"T_HTE.HTE_TER, " +
                 oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1",  "T_HTE.HTE_AP1", "T_HTE.HTE_PA2",  "T_HTE.HTE_AP2",  "T_HTE.HTE_NOM"})+ " AS NOMBRE";


        ArrayList<String> join = new ArrayList<String>();


        join.add("E_EXP");
        join.add("LEFT");
        join.add("E_CRO");
        join.add("EXP_MUN = CRO_MUN AND EXP_PRO=CRO_PRO AND EXP_EJE = CRO_EJE AND EXP_NUM = CRO_NUM ");
        join.add("LEFT");
        join.add("E_TRA");
        join.add("CRO_MUN = TRA_MUN AND CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD");
        join.add("LEFT");
        join.add("E_PRO");
        join.add("EXP_MUN = PRO_MUN AND EXP_PRO = PRO_COD");
        join.add("LEFT");
        join.add("E_PML");
        join.add("EXP_MUN = PML_MUN AND EXP_PRO = PML_COD");
        join.add("LEFT");
        join.add("A_UOR");
        join.add("UOR_COD = EXP_UOR");
        join.add("INNER");
        join.add(GlobalNames.ESQUEMA_GENERICO + "A_USU");
        join.add("USU_COD=EXP_USU");


       
        join.add("LEFT");
        join.add("E_EXT TER");
        join.add("EXP_MUN = TER.EXT_MUN AND EXP_EJE = TER.EXT_EJE AND EXP_NUM = TER.EXT_NUM");
        

        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            join.add("LEFT");
            join.add("T_DNN");
            join.add("EXP_CLO = DNN_DOM");
        }
        
        /**
            join.add("LEFT");
            join.add("E_EXREXT");
            join.add("EXP_MUN = EXREXT_MUN AND EXP_NUM = EXREXT_NUM"); **/
        //Los joins para ordenar por interesado

             join.add("LEFT");
             join.add("E_EXT A");
             join.add("E_EXP.EXP_EJE=A.EXT_EJE AND E_EXP.EXP_MUN=A.EXT_MUN AND E_EXP.EXP_NUM=A.EXT_NUM");
             join.add("LEFT");
             join.add("T_HTE");
             join.add("A.EXT_TER=T_HTE.HTE_TER AND A.EXT_NVR=T_HTE.HTE_NVR");

        HashMap datosConsulta = consExpVO.getCamposSuplementarios();
        int z = 0;
        int tamaño=tam;        
        datosConsulta.remove("TAMAÑO");
        EstructuraCampo estructura;
        String clave;
        String valor;
        String tipo;
        String tramite;
        while (z < tamaño) {
            estructura = (EstructuraCampo) datosConsulta.get(String.valueOf(z));
            clave = estructura.getCodCampo();
            valor = estructura.getValorConsulta();
            tipo = estructura.getURLPlantilla();
            tramite = estructura.getCodTramite();
            if (m_Log.isDebugEnabled()) m_Log.debug("CampoSuplementario: Existe Tramite= " + tramite);
            if (tramite == null) { // Se trata de campos suplementarios de expediente
                if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEX_" + z;
                    join.add("E_TDEX " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEX_NUM AND " + tabla + ".TDEX_COD = '" + clave + "'" +
                                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEX_VALOR", valor, false));
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUC_" + z;
                    join.add("E_TNUC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUC_NUM AND " + tabla + ".TNUC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUC_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFEC_" + z;
                    join.add("E_TFEC " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFEC_NUM AND " + tabla + ".TFEC_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFEC_VALOR", valor));
                } else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDE_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDE_VALOR";
                    join.add("E_TDE " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = valor;
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDE_NUM AND " + tabla + ".TDE_COD = '" + clave + "' AND " + filtroDesplegable +")");
                } else if (tipo.indexOf("Texto") != -1) {
                    boolean consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXT_" + z;
                    join.add("E_TXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXT_NUM AND " + tabla + ".TXT_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXT_VALOR", valor, false));
                } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNU_" + z;
                    join.add("E_TNU " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNU_NUM AND " + tabla + ".TNU_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNU_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFE_" + z;
                    join.add("E_TFE " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFE_NUM AND " + tabla + ".TFE_COD = '" + clave + "'" +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFE_VALOR", valor));
                }
            } else { // Se trata de campos suplementarios de tramites visibles a nivel de expediente
                if (tipo.indexOf("DesplegableExterno") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDEXT_" + z;
                    join.add("E_TDEXT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TDEXT_NUM AND " + tabla + ".TDEXT_COD = '" + clave + "' AND " + tabla + ".TDEXT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TDEXT_VALOR", valor, false));            
                } else if (tipo.indexOf("NumericoCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUCT_" + z;
                    join.add("E_TNUCT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUCT_NUM AND " + tabla + ".TNUCT_COD = '" + clave + "' AND " + tabla + ".TNUCT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUCT_VALOR", valor));
                } else if (tipo.indexOf("FechaCal") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFECT_" + z;
                    join.add("E_TFECT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFECT_NUM AND " + tabla + ".TFECT_COD = '" + clave + "' AND " + tabla + ".TFECT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFECT_VALOR", valor));
                } else if (tipo.indexOf("Desplegable") != -1) {
                    join.add("INNER");
                    String tabla = "E_TDET_" + z;
                    String[] parametros = new String[1];
                    parametros[0]=tabla+ ".TDET_VALOR";
                    join.add("E_TDET " + tabla);
                    String filtroDesplegable ="("+ oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER,parametros);
                    parametros[0] = valor;
                    filtroDesplegable+= "= " + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_UPPER, parametros);
                    join.add("EXP_NUM = " + tabla + ".TDET_NUM AND " + tabla + ".TDET_COD = '" + clave + "' AND " + tabla + ".TDET_TRA = " + tramite +
                            " AND " + filtroDesplegable + ")");
                } else if (tipo.indexOf("Texto") != -1) {
                    boolean consultaInsensitiva=true;
                    join.add("INNER");
                    String tabla = "E_TXTT_" + z;
                    join.add("E_TXTT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TXTT_NUM AND " + tabla + ".TXTT_COD = '" + clave + "' AND " + tabla + ".TXTT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadores(tabla + ".TXTT_VALOR", valor, false));
                } else if (tipo.indexOf("Numerico") != -1) {
                    join.add("INNER");
                    String tabla = "E_TNUT_" + z;
                    join.add("E_TNUT " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TNUT_NUM AND " + tabla + ".TNUT_COD = '" + clave + "' AND " + tabla + ".TNUT_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoNumerico(tabla + ".TNUT_VALOR", valor));
                } else if (tipo.indexOf("Fecha") != -1) {
                    join.add("INNER");
                    String tabla = "E_TFET_" + z;
                    join.add("E_TFET " + tabla);
                    join.add("EXP_NUM = " + tabla + ".TFET_NUM AND " + tabla + ".TFET_COD = '" + clave + "' AND " + tabla + ".TFET_TRA = " + tramite +
                            " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha(tabla + ".TFET_VALOR", valor));
                }
            }
            z = z + 1;
        }
        // Fin Parte de Consulta  para campos Suplementarios
        join.add("false");

        String where = "(A.EXT_TER IS NULL OR A.EXT_TER=(SELECT MIN(EXT_TER) FROM E_EXT WHERE EXT_NUM=EXP_NUM AND MOSTRAR=1)) AND PML_CMP = 'NOM' AND PML_LENG = '"+m_ConfigTechnical.getString("idiomaDefecto")+"' AND ";


          // Se comprueba que el usuario sea el de inicio del expediente y además que tenga permiso sobre la uor de inicio del expediente
        where += "(EXP_USU=" + uVO.getIdUsuario() + " AND (exists ("
                + "SELECT DISTINCT UOU_UOR "
                + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = EXP_UOR)) ";

        // Se comprueba que el usuario de fin del trámite sea el usuario y que tenga
        // permiso sobre la unidad tramitadora del trámite finalizado del expediente
        where += " OR ( CRO_USF=" + uVO.getIdUsuario() + " AND (exists ("
                  + "SELECT DISTINCT UOU_UOR "
                  + "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU "
                  + "WHERE UOU_USU = " + uVO.getIdUsuario() + " AND UOU_ORG = " + uVO.getOrgCod() + " "
                  + "AND UOU_ENT = " + uVO.getEntCod() + " AND UOU_UOR = CRO_UTR)))) ";



        if (consExpVO.getCodProcedimiento() != null) {
            String[] parametros = new String[2];
            if (!"".equals(consExpVO.getCodProcedimiento()) && !"todos".equals(consExpVO.getCodProcedimiento())) {
                parametros[0] = "'"+consExpVO.getCodProcedimiento().trim()+"'";
                String condicion = "EXP_PRO="+ parametros[0];
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
                
            } else if ("todos".equals(consExpVO.getCodProcedimiento())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "PRO", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaProc.elementAt(j);
                    String cP = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_PRO = '" + cP + "'";
                    else where += " OR EXP_PRO = '" + cP + "'";

                }
                if (listaProc.size() != 0) {
                    where += ")";
                }
            }
        }
        if (consExpVO.getNumeroExpediente() != null) {
            if (!"".equals(consExpVO.getNumeroExpediente())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_NUM", consExpVO.getNumeroExpediente().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaInicio() != null) {
            if (!"".equals(consExpVO.getFechaInicio())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEI", consExpVO.getFechaInicio());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getFechaFin() != null) {
            if (!"".equals(consExpVO.getFechaFin())) {
                String condicion = transformador.construirCondicionWhereConOperadoresCampoFecha("EXP_FEF", consExpVO.getFechaFin());
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getEstado() != null) {
            if (!"sinEstado".equals(consExpVO.getEstado())) {
                String condicion = "";
                if ("abierto".equals(consExpVO.getEstado())) condicion = "0)";
                else if ("cerrado".equals(consExpVO.getEstado())) condicion = "9 OR EXP_EST =1)";
                where += " AND (EXP_EST = " + condicion ;
            }
        }


        if (consExpVO.getTitular() != null) {
            if (!"".equals(consExpVO.getTitular())) {
                String condicion = transformador.construirCondicionWhereConOperadores("TER.EXT_TER", consExpVO.getTercero().trim(), false);
                String condicion1 = transformador.construirCondicionWhereConOperadores("TER.EXT_NVR", consExpVO.getVersionTercero().trim(), false);
                if (!"".equals(condicion) && !"".equals(condicion1)) {
                    where += " AND " + condicion + " AND " + condicion1;
                }
            }
        }
		
        if(CONSULTAR_EXPEDIENTES_POR_DOCUMENTO){
            if(consExpVO.getDocumentoTercero()!=null && !"".equals(consExpVO.getDocumentoTercero()) &&
                    consExpVO.getTipoDocumentoTercero()!=null && !"".equals(consExpVO.getTipoDocumentoTercero())){
                String condicion = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_TID", consExpVO.getTipoDocumentoTercero().trim(), false);
                String condicion1 = transformador.construirCondicionWhereConOperadores("T_HTE.HTE_DOC", consExpVO.getDocumentoTercero().trim(), false);
                if (!"".equals(condicion) && !"".equals(condicion1)) {
                    where += " AND " + condicion + " AND " + condicion1;
                }
            }
        } else {
            if (consExpVO.getTitular() != null) {
                if (!"".equals(consExpVO.getTitular())) {
                    String condicion = transformador.construirCondicionWhereConOperadores("TER.EXT_TER", consExpVO.getTercero().trim(), false);
                    String condicion1 = transformador.construirCondicionWhereConOperadores("TER.EXT_NVR", consExpVO.getVersionTercero().trim(), false);
                    if (!"".equals(condicion) && !"".equals(condicion1)) {
                        where += " AND " + condicion + " AND " + condicion1;
                    }
                }
            }
        }
		
        if (consExpVO.getLocalizacion() != null) {
            if (!"".equals(consExpVO.getLocalizacion())) {
                boolean consultaInsensitiva=true;
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_LOC", consExpVO.getLocalizacion().trim(), false);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            }
        }
        if (consExpVO.getCodClasifTramite() != null) {
            if (!"".equals(consExpVO.getCodClasifTramite()) && !"todos".equals(consExpVO.getCodClasifTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("TRA_CLS", consExpVO.getCodClasifTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodClasifTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "CLS", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaClasTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaClasTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaClasTram.elementAt(j);
                    String cCT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (TRA_CLS = " + cCT;
                    else where += " OR TRA_CLS = " + cCT;
                }
                if (listaClasTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodUnidadTram() != null) {
            if (!"".equals(consExpVO.getCodUnidadTram()) && !"todos".equals(consExpVO.getCodUnidadTram())) {
                String condicion = transformador.construirCondicionWhereConOperadores("EXP_UOR", consExpVO.getCodUnidadTram().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodUnidadTram())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "UTR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaUnidTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaUnidTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaUnidTram.elementAt(j);
                    String cUT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (EXP_UOR = " + cUT;
                    else where += " OR EXP_UOR = " + cUT;

                }
                if (listaUnidTram.size() != 0) where += ")";
            }
        }
        if (consExpVO.getCodTramite() != null) {
            if (!"".equals(consExpVO.getCodTramite()) && !"todos".equals(consExpVO.getCodTramite())) {
                String condicion = transformador.construirCondicionWhereConOperadores("CRO_TRA", consExpVO.getCodTramite().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTramite())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TRA", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaCodTram = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaCodTram.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaCodTram.elementAt(j);
                    String cT = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (CRO_TRA = " + cT;
                    else where += " OR CRO_TRA = " + cT;

                }
                if (listaCodTram.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodArea() != null) {
            if (!"".equals(consExpVO.getCodArea()) && !"todos".equals(consExpVO.getCodArea())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_ARE", consExpVO.getCodArea().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodArea())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "ARE", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaArea = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaArea.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaArea.elementAt(j);
                    String cA = cIVO.getTipoProcedimiento();

                    if (j == 0) where += " AND (PRO_ARE = " + cA;
                    else where += " OR PRO_ARE =" + cA;

                }
                if (listaArea.size() != 0) where += ")";

            }
        }
        if (consExpVO.getCodTipoProced() != null) {
            if (!"".equals(consExpVO.getCodTipoProced()) && !"todos".equals(consExpVO.getCodTipoProced())) {
                String condicion = transformador.construirCondicionWhereConOperadores("PRO_TIP", consExpVO.getCodTipoProced().trim(), true);
                if (!"".equals(condicion)) {
                    where += " AND " + condicion;
                }
            } else if ("todos".equals(consExpVO.getCodTipoProced())) {
                ConfInformeValueObject confInformeVO = new ConfInformeValueObject(
                        "TPR", "", "", "", "", "", "", "", "", "", "", "", "");
                Vector listaTipoProc = ConfInformeManager.getInstance().cargarCombos(confInformeVO, params);
                for (int j = 0; j < listaTipoProc.size(); j++) {
                    ConfInformeValueObject cIVO = (ConfInformeValueObject) listaTipoProc.elementAt(j);
                    String tP = cIVO.getTipoProcedimiento();
                    if (j == 0) where += " AND (PRO_TIP = " + tP;
                    else where += " OR PRO_TIP = " + tP;

                }
                if (listaTipoProc.size() != 0) where += ")";
            }
        }
        
        
        /***
        if (consExpVO.getTipoInicio()!= null) {
            if (!"Todos".equals(consExpVO.getTipoInicio())) {
                if ("Instancia".equals(consExpVO.getTipoInicio())) {
                    where+=" AND E_EXR.EXR_TOP= 0 ";
                }
                else if ("Oficio".equals(consExpVO.getTipoInicio())) {                    
                    where += "AND (E_EXR.EXR_TOP <> 0 OR E_EXR.EXR_TOP IS NULL)";
                }                
            }
        }
        ***/
        if (consExpVO.getTipoInicio()!= null && "Instancia".equals(consExpVO.getTipoInicio())) {            
            where+=" AND EXP_NUM IN (SELECT EXR_NUM FROM E_EXR WHERE EXR_NUM=EXP_NUM AND EXR_PRO=EXP_PRO AND EXR_MUN=EXR_MUN AND EXR_TOP=0)";
        }else if ("Oficio".equals(consExpVO.getTipoInicio())) {                    
             where+=" AND EXP_NUM NOT IN (SELECT EXR_NUM FROM E_EXR WHERE EXR_NUM=EXP_NUM AND EXR_PRO=EXP_PRO AND EXR_MUN=EXR_MUN AND EXR_TOP=0)";         
        }
        
        
        
        
        if (consExpVO.getRefCatastral() != null && !"".equals(consExpVO.getRefCatastral())) {
            where += " AND " + transformador.construirCondicionWhereConOperadores("DNN_RCA", consExpVO.getRefCatastral().trim(), false);
        }

        return oad.join(from, where, join.toArray(new String[]{}));
    }


      private boolean hayDatosRegistroExterno (ConsultaExpedientesValueObject consExpVO, UsuarioValueObject uVO,AdaptadorSQLBD oad,String[] params){
          
          boolean retorno=false;
          Connection con = null;
          Statement st;
          ResultSet rs;
          
          try {
          
         
          con = oad.getConnection(); 
          st = con.createStatement();

          String sql = "SELECT EXREXT_MUN,EXREXT_PRO,EXREXT_NUM FROM E_EXREXT WHERE EXREXT_MUN= " + uVO.getOrgCod();


          if (consExpVO.getCodTipoAnotacion() != null && !"".equals(consExpVO.getCodTipoAnotacion())) {
              sql += " AND EXREXT_TIP='" + consExpVO.getCodTipoAnotacion() + "'";
          }

          if (consExpVO.getNumeroAnotacion() != null && !"".equals(consExpVO.getNumeroAnotacion())) {
              sql += " AND EXREXT_NRE=" + consExpVO.getNumeroAnotacion();
          }

          if ((consExpVO.getEjercicioAnotacion() != null && !"".equals(consExpVO.getEjercicioAnotacion()))) {
              sql += " AND EXREXT_EJR=" + consExpVO.getEjercicioAnotacion();
          }
          
          m_Log.debug(sql);
              
           rs = st.executeQuery(sql);
           
           if(rs.next()) {
             retorno=true;  
           }
           
           rs.close();
           st.close();
           
          } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                
            }
            finally {
                try {
                    if (con != null) con.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    if (m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " + sqle.getMessage());
                }
            }
          

          return retorno;
      }
      
      
      
    public boolean esPendienteParaUsuario(UsuarioValueObject usuario, ConsultaExpedientesValueObject consExpVO, Connection con) throws TechnicalException, BDException, AnotacionRegistroException, TramitacionException {
        PreparedStatement ps = null;
        ResultSet rs = null;                
        boolean esPendiente = false;        
        String sql;
        
        if (consExpVO.isExpHistorico())
            
            /*** ORIGINAL
            sql = "SELECT /*+ index(HIST_E_EXP) index(HIST_E_CRO) index(e_pro) index(e_pml) index(a_uor)* HIST_E_EXP.EXP_NUM FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU, A_UOR,HIST_E_EXP " +
            "LEFT JOIN HIST_E_CRO ON (HIST_E_EXP.EXP_MUN = CRO_MUN AND HIST_E_EXP.EXP_EJE = CRO_EJE AND HIST_E_EXP.EXP_NUM = CRO_NUM AND CRO_FEF IS NULL)"  +
            "INNER JOIN E_PML ON (HIST_E_EXP.EXP_MUN = PML_MUN AND HIST_E_EXP.EXP_PRO = PML_COD)" +
            "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (UOU_UOR = CRO_UTR AND UOU_USU = ? AND UOU_ORG = ? AND UOU_ENT = ?)"  +
            "WHERE EXP_NUM = ? AND USU_COD= ? AND UOR_COD=EXP_UOR AND PML_CMP='NOM'  AND PML_LENG='"+m_ConfigTechnical.getString("idiomaDefecto")+"'  AND EXP_EST = 0  " +
            "AND ((exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                            "WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=EXP_UOR)) OR " +
                 "(exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                            "WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=CRO_UTR))  AND CRO_FEF IS NULL)" +
           "GROUP BY HIST_E_EXP.EXP_MUN, HIST_E_EXP.EXP_PRO, HIST_E_EXP.EXP_EJE, HIST_E_EXP.EXP_NUM, PML_VALOR, HIST_E_EXP.EXP_FEI, HIST_E_EXP.EXP_FEF, HIST_E_EXP.EXP_EST,UOR_NOM,USU_NOM";
           **/
            sql = "SELECT COUNT(*) AS NUM FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU, A_UOR,HIST_E_EXP " + 
                  "LEFT JOIN HIST_E_CRO ON (HIST_E_EXP.EXP_MUN = CRO_MUN AND HIST_E_EXP.EXP_EJE = CRO_EJE AND HIST_E_EXP.EXP_NUM = CRO_NUM AND CRO_FEF IS NULL) " +          
                  "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (UOU_UOR = CRO_UTR AND UOU_USU=? AND UOU_ORG=? AND UOU_ENT=?) " + 
                  "WHERE EXP_NUM=? AND USU_COD=? AND UOR_COD=EXP_UOR AND EXP_EST=0 " +  
                  "AND ((exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " + 
                  "WHERE UOU_USU=? AND UOU_ORG =? AND UOU_ENT=? AND UOU_UOR=EXP_UOR)) " + 
                  "OR (exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " + 
                  "WHERE UOU_USU=? AND UOU_ORG=? AND UOU_ENT=? AND UOU_UOR=CRO_UTR))  AND CRO_FEF IS NULL)";
            
        else
            
            sql = "SELECT COUNT(*) AS NUM FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU, A_UOR,E_EXP " + 
                  "LEFT JOIN E_CRO ON (E_EXP.EXP_MUN = CRO_MUN AND E_EXP.EXP_EJE = CRO_EJE AND E_EXP.EXP_NUM = CRO_NUM AND CRO_FEF IS NULL) " +          
                  "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (UOU_UOR = CRO_UTR AND UOU_USU=? AND UOU_ORG=? AND UOU_ENT=?) " + 
                  "WHERE EXP_NUM=? AND USU_COD=? AND UOR_COD=EXP_UOR AND EXP_EST=0 " +  
                  "AND ((exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " + 
                  "WHERE UOU_USU=? AND UOU_ORG =? AND UOU_ENT=? AND UOU_UOR=EXP_UOR)) " + 
                  "OR (exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " + 
                  "WHERE UOU_USU=? AND UOU_ORG=? AND UOU_ENT=? AND UOU_UOR=CRO_UTR))  AND CRO_FEF IS NULL)";
            /*  ORIGINAL
            sql = "SELECT /*+ index(e_exp) index(e_cro) index(e_pro) index(e_pml) index(a_uor)* E_EXP.EXP_NUM FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU, A_UOR,E_EXP " +
            "LEFT JOIN E_CRO ON (E_EXP.EXP_MUN = CRO_MUN AND E_EXP.EXP_EJE = CRO_EJE AND E_EXP.EXP_NUM = CRO_NUM AND CRO_FEF IS NULL)"  +
            "INNER JOIN E_PML ON (E_EXP.EXP_MUN = PML_MUN AND E_EXP.EXP_PRO = PML_COD)" +
            "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (UOU_UOR = CRO_UTR AND UOU_USU = ? AND UOU_ORG = ? AND UOU_ENT = ?)"  +
            "WHERE EXP_NUM = ? AND USU_COD= ? AND UOR_COD=EXP_UOR AND PML_CMP='NOM'  AND PML_LENG='"+m_ConfigTechnical.getString("idiomaDefecto")+"'  AND EXP_EST = 0  " +
            "AND ((exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                            "WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=EXP_UOR)) OR " +
                 "(exists (SELECT DISTINCT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                            "WHERE UOU_USU= ? AND UOU_ORG = ? AND UOU_ENT=? AND UOU_UOR=CRO_UTR))  AND CRO_FEF IS NULL)" +
           "GROUP BY E_EXP.EXP_MUN, E_EXP.EXP_PRO, E_EXP.EXP_EJE, E_EXP.EXP_NUM, PML_VALOR, E_EXP.EXP_FEI, E_EXP.EXP_FEF, E_EXP.EXP_EST,UOR_NOM,USU_NOM";
            **/
        m_Log.debug("esPendienteParaUsuario: " + sql);
        try {
          
            ps = con.prepareStatement(sql);
            int i = 1;
            m_Log.debug("Numero de expediente: " + consExpVO.getNumeroExpediente());
            m_Log.debug("Codigo de Usuario: " + usuario.getIdUsuario());
            m_Log.debug("Organizacion de Usuario: " + usuario.getOrgCod());
            m_Log.debug("Entidad de Usuario: " + usuario.getEntCod());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());
            ps.setString(i++, consExpVO.getNumeroExpediente());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());
            ps.setInt(i++, usuario.getIdUsuario());
            ps.setInt(i++, usuario.getOrgCod());
            ps.setInt(i++, usuario.getEntCod());

            rs = ps.executeQuery();
            
            int num = 0;
            while(rs.next()) {
                num = rs.getInt("NUM");                
            }
            
            if(num>=1) esPendiente = true;
            
        } catch (Exception ex) {            
            ex.printStackTrace();            
            
        } finally {
            
            try {
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                m_Log.error("Exception: " + ex.getMessage());
            }
        }
        
        return esPendiente;
    }
    
        /**

     * Obtiene el nombre de la columna a partir del codigo de ordenacion de la misma
     *
     * @param codOrdenColumna
     * @param params
     * @return
     * @throws TramitacionException 
     */

    private String getNombreColumnaOrdenTabla(String codOrdenColumna, String[] params) throws TramitacionException {

        String nomColumna = null;
        Integer ordenColumna = null;
        	
        if (StringUtils.isEmpty(codOrdenColumna) || ("0".equals(codOrdenColumna))) {	
            ordenColumna = CONSULTA_EXPEDIENTES_CAMPLIST_COD_ORDEN_EXP_NUM;	
        } else {	
            ordenColumna = NumberUtils.createInteger(codOrdenColumna);	
        }	
        	
        Vector<CamposListadosParametrizablesVO> listaCamposListados	
                = listaCamposListados = TramitacionManager.getInstance().getCamposListado(1, params);		
        for (CamposListadosParametrizablesVO campo : listaCamposListados) {	
            if (ordenColumna.equals(campo.getOrdenCampo())) {	
                nomColumna = campo.getColumnaCampo();	
                break;	
            }	
        }	
	
        return nomColumna;
	
    }

}