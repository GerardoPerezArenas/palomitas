// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.persistence.manual;

// PAQUETES IMPORTADOS
import es.altia.agora.business.sge.ConsultaExpedientesValueObject;
import es.altia.agora.business.sge.persistence.ConsultaExpedientesManager;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.agora.webservice.tercero.exception.DemasiadosResultadosBusquedaTerceroException;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.ResourceBundle;
import org.apache.commons.lang.StringUtils;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase TercerosDAO </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class TercerosDAO {

    private static TercerosDAO instance = null;
    protected static Config m_ct; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Config m_Common;       // Para el fichero de opciones comunes de configuracion.
    protected static Log m_Log =
            LogFactory.getLog(TercerosDAO.class.getName());

    protected static GeneralValueObject campos;

    protected static String idPais;
    protected static String idProvincia;
    protected static String idMunicipio;
    protected static String pais;
    protected static String provincia;
    protected static String municipio;
    boolean consultaInsensitiva=false;

    protected TercerosDAO() {
        super();
        // Queremos usar el fichero de configuracion techserver
        m_ct = ConfigServiceHelper.getConfig("techserver");
        m_Common = ConfigServiceHelper.getConfig("common");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

        campos = new GeneralValueObject();

        campos.setAtributo("codigoPais",m_ct.getString("SQL.CODIGOPAIS"));
        // TABLA T_TID
        campos.setAtributo("codTID",m_ct.getString("SQL.T_TID.codigo"));
        campos.setAtributo("descTID",m_ct.getString("SQL.T_TID.nombre"));
        campos.setAtributo("perFJTID",m_ct.getString("SQL.T_TID.fisJur"));
        // TABLA T_TER
        campos.setAtributo("idTerceroTER",m_ct.getString("SQL.T_TER.identificador"));
        campos.setAtributo("versionTER",m_ct.getString("SQL.T_TER.version"));
        campos.setAtributo("tipoDocTER",m_ct.getString("SQL.T_TER.tipoDocumento"));
        campos.setAtributo("documentoTER",m_ct.getString("SQL.T_TER.documento"));
        campos.setAtributo("nombreTER",m_ct.getString("SQL.T_TER.nombre"));
        campos.setAtributo("apellido1TER",m_ct.getString("SQL.T_TER.apellido1"));
        campos.setAtributo("pApellido1TER",m_ct.getString("SQL.T_TER.partApellido1"));
        campos.setAtributo("apellido2TER",m_ct.getString("SQL.T_TER.apellido2"));
        campos.setAtributo("pApellido2TER",m_ct.getString("SQL.T_TER.partApellido2"));
        campos.setAtributo("nombreLargoTER",m_ct.getString("SQL.T_TER.nombreCompleto"));
        campos.setAtributo("normalizadoTER",m_ct.getString("SQL.T_TER.normalizado"));
        campos.setAtributo("telefonoTER",m_ct.getString("SQL.T_TER.telefono"));
        campos.setAtributo("emailTER",m_ct.getString("SQL.T_TER.email"));
        campos.setAtributo("situacionTER",m_ct.getString("SQL.T_TER.situacion"));
        campos.setAtributo("fechaAltaTER",m_ct.getString("SQL.T_TER.fechaAlta"));
        campos.setAtributo("usuarioAltaTER",m_ct.getString("SQL.T_TER.usuarioAlta"));
        campos.setAtributo("moduloAltaTER",m_ct.getString("SQL.T_TER.moduloAlta"));
        campos.setAtributo("fechaBajaTER",m_ct.getString("SQL.T_TER.fechaBaja"));
        campos.setAtributo("usuarioBajaTER",m_ct.getString("SQL.T_TER.usuarioBaja"));
        // TABLA T_HTE
        campos.setAtributo("idTerceroHTE",m_ct.getString("SQL.T_HTE.identificador"));
        campos.setAtributo("versionHTE",m_ct.getString("SQL.T_HTE.version"));
        campos.setAtributo("idDomicilioHTE",m_ct.getString("SQL.T_HTE.idDomicilio"));
        campos.setAtributo("tipoDocHTE",m_ct.getString("SQL.T_HTE.tipoDocumento"));
        campos.setAtributo("documentoHTE",m_ct.getString("SQL.T_HTE.documento"));
        campos.setAtributo("nombreHTE",m_ct.getString("SQL.T_HTE.nombre"));
        campos.setAtributo("apellido1HTE",m_ct.getString("SQL.T_HTE.apellido1"));
        campos.setAtributo("pApellido1HTE",m_ct.getString("SQL.T_HTE.partApellido1"));
        campos.setAtributo("apellido2HTE",m_ct.getString("SQL.T_HTE.apellido2"));
        campos.setAtributo("pApellido2HTE",m_ct.getString("SQL.T_HTE.partApellido2"));
        campos.setAtributo("nombreLargoHTE",m_ct.getString("SQL.T_HTE.nombreCompleto"));
        campos.setAtributo("normalizadoHTE",m_ct.getString("SQL.T_HTE.normalizado"));
        campos.setAtributo("telefonoHTE",m_ct.getString("SQL.T_HTE.telefono"));
        campos.setAtributo("emailHTE",m_ct.getString("SQL.T_HTE.email"));
        campos.setAtributo("fechaOperHTE",m_ct.getString("SQL.T_HTE.fecha"));
        campos.setAtributo("usuarioOperHTE",m_ct.getString("SQL.T_HTE.usuario"));
        campos.setAtributo("moduloOperHTE",m_ct.getString("SQL.T_HTE.aplicacion"));
        // TABLA T_DOM
        campos.setAtributo("idDomicilioDOM",m_ct.getString("SQL.T_DOM.idDomicilio"));
        campos.setAtributo("normalizadoDOM",m_ct.getString("SQL.T_DOM.normalizado"));
        // TABLA T_DOT
        campos.setAtributo("idDomicilioDOT",m_ct.getString("SQL.T_DOT.idDomicilio"));
        campos.setAtributo("idTerceroDOT",m_ct.getString("SQL.T_DOT.idTercero"));
        campos.setAtributo("tipoOcupacionDOT",m_ct.getString("SQL.T_DOT.tipoUso"));
        campos.setAtributo("situacionDOT",m_ct.getString("SQL.T_DOT.situacion"));
        campos.setAtributo("fechaRelacionDOT",m_ct.getString("SQL.T_DOT.fecha"));
        campos.setAtributo("usuarioRelacionDOT",m_ct.getString("SQL.T_DOT.usuario"));
        campos.setAtributo("enPadronDOT",m_ct.getString("SQL.T_DOT.padron"));
        // TABLA T_TOC
        campos.setAtributo("codTOC",m_ct.getString("SQL.T_TOC.codigoUso"));
        campos.setAtributo("descTOC",m_ct.getString("SQL.T_TOC.descUso"));
        // TABLA T_PAI
        campos.setAtributo("codPAI",m_ct.getString("SQL.T_PAI.idPais"));
        campos.setAtributo("descPAI",m_ct.getString("SQL.T_PAI.nombre"));
        // TABLA T_PRV
        campos.setAtributo("codPRV",m_ct.getString("SQL.T_PRV.idProvincia"));
        campos.setAtributo("descPRV",m_ct.getString("SQL.T_PRV.nombre"));
        campos.setAtributo("codPaisPRV",m_ct.getString("SQL.T_PRV.idPais"));
        // TABLA T_MUN
        campos.setAtributo("codMUN",m_ct.getString("SQL.T_MUN.idMunicipio"));
        campos.setAtributo("descMUN",m_ct.getString("SQL.T_MUN.nombre"));
        campos.setAtributo("codPaisMUN",m_ct.getString("SQL.T_MUN.idPais"));
        campos.setAtributo("codProvMUN",m_ct.getString("SQL.T_MUN.idProvincia"));
        // TABLA T_ECO
        campos.setAtributo("codECO",m_ct.getString("SQL.T_ECO.identificador"));
        campos.setAtributo("descECO",m_ct.getString("SQL.T_ECO.nombre"));
        campos.setAtributo("codPaisECO",m_ct.getString("SQL.T_ECO.pais"));
        campos.setAtributo("codProvECO",m_ct.getString("SQL.T_ECO.provincia"));
        campos.setAtributo("codMunECO",m_ct.getString("SQL.T_ECO.municipio"));
        campos.setAtributo("situacionECO",m_ct.getString("SQL.T_ECO.situacion"));
        // TABLA T_ESI
        campos.setAtributo("codESI",m_ct.getString("SQL.T_ESI.identificador"));
        campos.setAtributo("descESI",m_ct.getString("SQL.T_ESI.nombre"));
        campos.setAtributo("codPaisESI",m_ct.getString("SQL.T_ESI.pais"));
        campos.setAtributo("codProvESI",m_ct.getString("SQL.T_ESI.provincia"));
        campos.setAtributo("codMunESI",m_ct.getString("SQL.T_ESI.municipio"));
        campos.setAtributo("codECOESI",m_ct.getString("SQL.T_ESI.eColectiva"));
        campos.setAtributo("situacionESI",m_ct.getString("SQL.T_ESI.situacion"));
        // TABLA T_NUC
        campos.setAtributo("codNUC",m_ct.getString("SQL.T_NUC.codigo"));
        campos.setAtributo("descNUC",m_ct.getString("SQL.T_NUC.nombre"));
        campos.setAtributo("codPaisNUC",m_ct.getString("SQL.T_NUC.pais"));
        campos.setAtributo("codProvNUC",m_ct.getString("SQL.T_NUC.provincia"));
        campos.setAtributo("codMunNUC",m_ct.getString("SQL.T_NUC.municipio"));
        campos.setAtributo("codESINUC",m_ct.getString("SQL.T_NUC.eSingular"));
        // TABLA T_DNN
        campos.setAtributo("idDomicilioDNN",m_ct.getString("SQL.T_DNN.idDomicilio"));
        campos.setAtributo("idTipoViaDNN",m_ct.getString("SQL.T_DNN.idTipoVia"));
        campos.setAtributo("codPaisDNN",m_ct.getString("SQL.T_DNN.idPaisD"));
        campos.setAtributo("codProvDNN",m_ct.getString("SQL.T_DNN.idProvinciaD"));
        campos.setAtributo("codMunDNN",m_ct.getString("SQL.T_DNN.idMunicipioD"));
        campos.setAtributo("codPaisViaDNN",m_ct.getString("SQL.T_DNN.idPaisDVia"));
        campos.setAtributo("codProvViaDNN",m_ct.getString("SQL.T_DNN.idProvinciaDVia"));
        campos.setAtributo("codMunViaDNN",m_ct.getString("SQL.T_DNN.idMunicipioDVia"));
        campos.setAtributo("codViaDNN",m_ct.getString("SQL.T_DNN.codigoVia"));
        campos.setAtributo("numDesdeDNN",m_ct.getString("SQL.T_DNN.numDesde"));
        campos.setAtributo("letraDesdeDNN",m_ct.getString("SQL.T_DNN.letraDesde"));
        campos.setAtributo("numHastaDNN",m_ct.getString("SQL.T_DNN.numHasta"));
        campos.setAtributo("letraHastaDNN",m_ct.getString("SQL.T_DNN.letraHasta"));
        campos.setAtributo("bloqueDNN",m_ct.getString("SQL.T_DNN.bloque"));
        campos.setAtributo("portalDNN",m_ct.getString("SQL.T_DNN.portal"));
        campos.setAtributo("escaleraDNN",m_ct.getString("SQL.T_DNN.escalera"));
        campos.setAtributo("plantaDNN",m_ct.getString("SQL.T_DNN.planta"));
        campos.setAtributo("puertaDNN",m_ct.getString("SQL.T_DNN.puerta"));
        campos.setAtributo("domicilioDNN",m_ct.getString("SQL.T_DNN.domicilio"));
        campos.setAtributo("codPostalDNN",m_ct.getString("SQL.T_DNN.codigoPostal"));
        campos.setAtributo("barriadaDNN",m_ct.getString("SQL.T_DNN.barriada"));
        campos.setAtributo("situacionDNN",m_ct.getString("SQL.T_DNN.situacion"));
        campos.setAtributo("fechaAltaDNN",m_ct.getString("SQL.T_DNN.fechaAlta"));
        campos.setAtributo("usuarioAltaDNN",m_ct.getString("SQL.T_DNN.usuarioAlta"));
        campos.setAtributo("fechaBajaDNN",m_ct.getString("SQL.T_DNN.fechaBaja"));
        campos.setAtributo("usuarioBajaDNN",m_ct.getString("SQL.T_DNN.usuarioBaja"));
        campos.setAtributo("codPaisESIDNN",m_ct.getString("SQL.T_DNN.paisESI"));
        campos.setAtributo("codProvESIDNN",m_ct.getString("SQL.T_DNN.provinciaESI"));
        campos.setAtributo("codMunESIDNN",m_ct.getString("SQL.T_DNN.municipioESI"));
        campos.setAtributo("codESIDNN",m_ct.getString("SQL.T_DNN.codigoESI"));
        campos.setAtributo("refCatastralDNN",m_ct.getString("SQL.T_DNN.referenciaCatastral"));
        // TABLA T_DPO
        campos.setAtributo("idDomicilioDPO",m_ct.getString("SQL.T_DPO.domicilio"));
        campos.setAtributo("dirSueloDPO",m_ct.getString("SQL.T_DPO.suelo"));
        campos.setAtributo("escaleraDPO",m_ct.getString("SQL.T_DPO.escalera"));
        campos.setAtributo("plantaDPO",m_ct.getString("SQL.T_DPO.planta"));
        campos.setAtributo("puertaDPO",m_ct.getString("SQL.T_DPO.puerta"));
        campos.setAtributo("observDPO",m_ct.getString("SQL.T_DPO.observaciones"));
        campos.setAtributo("tipoViviendaDPO",m_ct.getString("SQL.T_DPO.tipoVivienda"));
        campos.setAtributo("situacionDPO",m_ct.getString("SQL.T_DPO.situacion"));
        campos.setAtributo("fechaAltaDPO",m_ct.getString("SQL.T_DPO.fechaAlta"));
        campos.setAtributo("usuarioAltaDPO",m_ct.getString("SQL.T_DPO.usuarioAlta"));
        campos.setAtributo("fechaBajaDPO",m_ct.getString("SQL.T_DPO.fechaBaja"));
        campos.setAtributo("usuarioBajaDPO",m_ct.getString("SQL.T_DPO.usuarioBaja"));
        campos.setAtributo("fechaVigenciaDPO",m_ct.getString("SQL.T_DPO.fechaVigencia"));
        // TABLA T_DSU
        campos.setAtributo("codDirSueloDSU",m_ct.getString("SQL.T_DSU.identificador"));
        campos.setAtributo("codPaisDSU",m_ct.getString("SQL.T_DSU.pais"));
        campos.setAtributo("codProvDSU",m_ct.getString("SQL.T_DSU.provincia"));
        campos.setAtributo("codMunDSU",m_ct.getString("SQL.T_DSU.municipio"));
        campos.setAtributo("codViaDSU",m_ct.getString("SQL.T_DSU.vial"));
        campos.setAtributo("numDesdeDSU",m_ct.getString("SQL.T_DSU.numeroDesde"));
        campos.setAtributo("letraDesdeDSU",m_ct.getString("SQL.T_DSU.letraDesde"));
        campos.setAtributo("numHastaDSU",m_ct.getString("SQL.T_DSU.numeroHasta"));
        campos.setAtributo("letraHastaDSU",m_ct.getString("SQL.T_DSU.letraHasta"));
        campos.setAtributo("bloqueDSU",m_ct.getString("SQL.T_DSU.bloque"));
        campos.setAtributo("portalDSU",m_ct.getString("SQL.T_DSU.portal"));
        campos.setAtributo("kmDSU",m_ct.getString("SQL.T_DSU.kilometro"));
        campos.setAtributo("hmDSU",m_ct.getString("SQL.T_DSU.hectometro"));
        campos.setAtributo("barriadaDSU",m_ct.getString("SQL.T_DSU.caserio"));
        campos.setAtributo("codPostalDSU",m_ct.getString("SQL.T_DSU.codigoPostal"));
        campos.setAtributo("situacionDSU",m_ct.getString("SQL.T_DSU.situacion"));
        campos.setAtributo("fechaAltaDSU",m_ct.getString("SQL.T_DSU.fechaAlta"));
        campos.setAtributo("usuarioAltaDSU",m_ct.getString("SQL.T_DSU.usuarioAlta"));
        campos.setAtributo("fechaBajaDSU",m_ct.getString("SQL.T_DSU.fechaBaja"));
        campos.setAtributo("usuarioBajaDSU",m_ct.getString("SQL.T_DSU.usuarioBaja"));
        campos.setAtributo("tipoNumeracionTRMDSU",m_ct.getString("SQL.T_DSU.tipoNumeracionTRM"));
        campos.setAtributo("codPaisTRMDSU",m_ct.getString("SQL.T_DSU.paisTRM"));
        campos.setAtributo("codProvTRMDSU",m_ct.getString("SQL.T_DSU.provinciaTRM"));
        campos.setAtributo("codMunTRMDSU",m_ct.getString("SQL.T_DSU.municipioTRM"));
        campos.setAtributo("vialTRMDSU",m_ct.getString("SQL.T_DSU.vialTRM"));
        campos.setAtributo("codigoTRMDSU",m_ct.getString("SQL.T_DSU.codigoTRM"));
        campos.setAtributo("codigoESI",m_ct.getString("SQL.T_DSU.e_singular"));
        // TABLA T_TVI
        campos.setAtributo("codTVI",m_ct.getString("SQL.T_TVI.codigo"));
        campos.setAtributo("descTVI",m_ct.getString("SQL.T_TVI.tipoVia"));
        // TABLA T_VIA
        campos.setAtributo("codPaisVIA",m_ct.getString("SQL.T_VIA.pais"));
        campos.setAtributo("codProvVIA",m_ct.getString("SQL.T_VIA.provincia"));
        campos.setAtributo("codMunVIA",m_ct.getString("SQL.T_VIA.municipio"));
        campos.setAtributo("idVIA",m_ct.getString("SQL.T_VIA.identificador"));
        campos.setAtributo("situacionVIA",m_ct.getString("SQL.T_VIA.situacion"));
        campos.setAtributo("tipoVIA",m_ct.getString("SQL.T_VIA.tipo"));
        campos.setAtributo("codVIA",m_ct.getString("SQL.T_VIA.codVia"));
        campos.setAtributo("descVIA",m_ct.getString("SQL.T_VIA.nombreVia"));
        campos.setAtributo("nomCorto",m_ct.getString("SQL.T_VIA.nombreCorto"));
        campos.setAtributo("fechaAlta",m_ct.getString("SQL.T_VIA.fechaAlta"));
        campos.setAtributo("fechaBaja",m_ct.getString("SQL.T_VIA.fechaBaja"));
        campos.setAtributo("usuarioBaja",m_ct.getString("SQL.T_VIA.usuarioBaja"));
        campos.setAtributo("usuarioAlta",m_ct.getString("SQL.T_VIA.usuarioAlta"));
        campos.setAtributo("fechaVigencia",m_ct.getString("SQL.T_VIA.fechaVigencia"));
        // TABLA T_TRM
        campos.setAtributo("codPaisTRM",m_ct.getString("SQL.T_TRM.pais"));
        campos.setAtributo("codProvTRM",m_ct.getString("SQL.T_TRM.provincia"));
        campos.setAtributo("codMunTRM",m_ct.getString("SQL.T_TRM.municipio"));
        campos.setAtributo("viaTRM",m_ct.getString("SQL.T_TRM.vial"));
        campos.setAtributo("codigoTRM",m_ct.getString("SQL.T_TRM.codigo"));
        campos.setAtributo("codPaisNUCTRM",m_ct.getString("SQL.T_TRM.paisNUC"));
        campos.setAtributo("codProvNUCTRM",m_ct.getString("SQL.T_TRM.provinciaNUC"));
        campos.setAtributo("codMunNUCTRM",m_ct.getString("SQL.T_TRM.municipioNUC"));
        campos.setAtributo("codESINUCTRM",m_ct.getString("SQL.T_TRM.e_singularNUC"));
        campos.setAtributo("codNUCTRM",m_ct.getString("SQL.T_TRM.nucleo"));
        campos.setAtributo("tipoNumeracionTRM",m_ct.getString("SQL.T_TRM.tipoNumeracion"));
        campos.setAtributo("primerNumeroTRM",m_ct.getString("SQL.T_TRM.primerNumero"));
        campos.setAtributo("primeraLetraTRM",m_ct.getString("SQL.T_TRM.primeraLetra"));
        campos.setAtributo("ultimoNumeroTRM",m_ct.getString("SQL.T_TRM.ultimoNumero"));
        campos.setAtributo("ultimaLetraTRM",m_ct.getString("SQL.T_TRM.ultimaLetra"));
        campos.setAtributo("situacionTRM",m_ct.getString("SQL.T_TRM.situacion"));
        // TABLA T_CPO
        campos.setAtributo("codPaisCPO",m_ct.getString("SQL.T_CPO.codPais"));
        campos.setAtributo("codProvCPO",m_ct.getString("SQL.T_CPO.codProvincia"));
        campos.setAtributo("codMunCPO",m_ct.getString("SQL.T_CPO.codMunicipio"));
        campos.setAtributo("codigoCPO",m_ct.getString("SQL.T_CPO.codigo"));

        campos.setAtributo("codTerceroEXT", m_ct.getString("SQL.E_EXT.codTercero"));
        campos.setAtributo("numVersionEXT", m_ct.getString("SQL.E_EXT.verTercero"));
        campos.setAtributo("codDomicilioEXT", m_ct.getString("SQL.E_EXT.codDomicilio"));

        campos.setAtributo("numeroExpedienteEXT", m_ct.getString("SQL.E_EXT.numero"));
        campos.setAtributo("codMunicipioEXT", m_ct.getString("SQL.E_EXT.codMunicipio"));
        campos.setAtributo("codEjercicioEXT", m_ct.getString("SQL.E_EXT.ano"));


        //tabla e_cro
        campos.setAtributo("numeroExpedienteCRO", m_ct.getString("SQL.E_CRO.numero"));
        campos.setAtributo("UtrCRO", m_ct.getString("SQL.E_CRO.codUnidadTramitadora"));
        campos.setAtributo("codMunicipioCRO", m_ct.getString("SQL.E_CRO.codMunicipio"));
        campos.setAtributo("codEjercicioCRO", m_ct.getString("SQL.E_CRO.ano"));

        //tabla a_uou
         campos.setAtributo("UorUOU", m_ct.getString("SQL.A_UOU.unidadOrg"));

          //tabla E_EXP
         campos.setAtributo("UorEXP", m_ct.getString("SQL.E_EXP.uor"));
         campos.setAtributo("numeroExpedienteEXP", m_ct.getString("SQL.E_EXP.numero"));
         campos.setAtributo("codMunicipioEXP", m_ct.getString("SQL.E_EXP.codMunicipio"));
         campos.setAtributo("codEjercicioEXP", m_ct.getString("SQL.E_EXP.ano"));


        idPais = m_ct.getString("SQL.T_PAI.idPais");
        pais = m_ct.getString("SQL.T_PAI.nombre");
        idProvincia = m_ct.getString("SQL.T_PRV.idProvincia");
        provincia = m_ct.getString("SQL.T_PRV.nombre");
        idMunicipio = m_ct.getString("SQL.T_MUN.idMunicipio");
        municipio = m_ct.getString("SQL.T_MUN.nombre");
    }

    public static TercerosDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread)
            // Las invocaciones de este metodo
            synchronized (TercerosDAO.class) {
                if (instance == null) {
                    instance = new TercerosDAO();
                }
            }
        }
        return instance;
    }

    public Vector getListaVias(String[] params,GeneralValueObject gVO) throws TechnicalException{

        int codigo=0;
        Vector<GeneralValueObject> resultado = new Vector<GeneralValueObject>();
        AdaptadorSQLBD bd = new AdaptadorSQLBD(params);
        Connection con=null;
        ResultSet rs =null;
        Statement state=null;

        try{

            con = bd.getConnection();
            state = con.createStatement();

            String from = " DISTINCT "+
                    campos.getAtributo("idVIA")+" AS IDVIA,"+
                    campos.getAtributo("codVIA")+" AS COD,"+
                    campos.getAtributo("codESI")+" AS CODESI,"+
                    campos.getAtributo("descVIA")+" AS NOMBRE,"+
                    campos.getAtributo("codTVI")+" AS TVIACOD,"+
                    campos.getAtributo("descTVI")+" AS TVIADES,"+
                    campos.getAtributo("descECO")+" AS LUGAR ";

            String where =  campos.getAtributo("situacionVIA")+"='A' ";
            if (gVO.getSize() == 4) {
                codigo = Integer.parseInt((String)gVO.getAtributo("codVia"));
                where += " AND " + campos.getAtributo("codVIA")+"=" + codigo;
            }

            where += " AND " + campos.getAtributo("codProvVIA")+"=" + gVO.getAtributo("codProvincia") + " AND " +
                    campos.getAtributo("codMunVIA")+"=" + gVO.getAtributo("codMunicipio") + " AND "+
                    campos.getAtributo("codProvTRM")+"=" + gVO.getAtributo("codProvincia") + " AND " +
                    campos.getAtributo("codMunTRM")+"=" + gVO.getAtributo("codMunicipio") + " AND " +
                    campos.getAtributo("codPaisVIA")+"=" + campos.getAtributo("codigoPais")+ " AND " +
                    campos.getAtributo("codPaisTRM")+"=" + campos.getAtributo("codigoPais");

            String[] join = new String[14];
            join[0] = "T_VIA";
            join[1] = "INNER";
            join[2] = "T_TRM";
            join[3] = campos.getAtributo("viaTRM")+"=" + campos.getAtributo("idVIA");
            join[4] = "LEFT";
            join[5] = "T_TVI";
            join[6] =  campos.getAtributo("tipoVIA") + "=" + campos.getAtributo("codTVI");
            join[7] = "LEFT";
            join[8] = "T_ESI";
            join[9] = campos.getAtributo("codESINUCTRM") + "=" + campos.getAtributo("codESI") + " AND " +
                    campos.getAtributo("codPaisNUCTRM") + "=" + campos.getAtributo("codPaisESI") + " AND " +
                    campos.getAtributo("codProvNUCTRM") + "=" + campos.getAtributo("codProvESI") + " AND " +
                    campos.getAtributo("codMunNUCTRM") + "=" + campos.getAtributo("codMunESI");
            join[10] = "LEFT";
            join[11] = "T_ECO";
            join[12] = campos.getAtributo("codECOESI") + "=" + campos.getAtributo("codECO") + " AND " +
                    campos.getAtributo("codPaisESI") + "=" + campos.getAtributo("codPaisECO") + " AND " +
                    campos.getAtributo("codProvESI") + "=" + campos.getAtributo("codProvECO") + " AND " +
                    campos.getAtributo("codMunESI") + "=" + campos.getAtributo("codMunECO");
            join[13] = "false";

            String sql = bd.join(from,where,join);
            sql += "ORDER BY "+campos.getAtributo("codVIA");

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while (rs.next()) {
                if((codigo>0)&&(codigo==rs.getInt("COD"))){
                    GeneralValueObject vo = new GeneralValueObject();
                    vo.setAtributo("idVia", rs.getString("IDVIA"));
                    vo.setAtributo("codVia", rs.getString("COD"));
                    vo.setAtributo("esiCod", rs.getString("CODESI"));
                    vo.setAtributo("descVia", rs.getString("NOMBRE"));
                    vo.setAtributo("codTipoVia", rs.getString("TVIACOD"));
                    vo.setAtributo("descTipoVia", rs.getString("TVIADES"));
                    vo.setAtributo("lugar", rs.getString("LUGAR"));
                    resultado.add(vo);
                    break;
                }else if(codigo==0){
                    GeneralValueObject vo = new GeneralValueObject();
                    vo.setAtributo("idVia", rs.getString("IDVIA"));
                    vo.setAtributo("codVia", rs.getString("COD"));
                    vo.setAtributo("esiCod", rs.getString("CODESI"));
                    vo.setAtributo("descVia", rs.getString("NOMBRE"));
                    vo.setAtributo("codTipoVia", rs.getString("TVIACOD"));
                    vo.setAtributo("descTipoVia", rs.getString("TVIADES"));
                    vo.setAtributo("lugar", rs.getString("LUGAR"));
                    resultado.add(vo);
                }
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            m_Log.debug("------------------------------------------------");
            sql = "SELECT 0 AS IDVIA,0 AS COD,"+
                    campos.getAtributo("codESI")+" AS CODESI,"+
                    campos.getAtributo("descESI")+" AS NOMBRE,0 AS TVIACOD,'' AS TVIADES,"+
                    campos.getAtributo("descECO")+" AS LUGAR " +
                    "FROM T_ESI"
                    +" left join T_ECO on ("+campos.getAtributo("codECOESI")+"="+campos.getAtributo("codECO")+
                    " AND "+ campos.getAtributo("codPaisESI")+"="+campos.getAtributo("codPaisECO")+
                    " AND "+ campos.getAtributo("codProvESI")+"="+campos.getAtributo("codProvECO")+
                    " AND "+ campos.getAtributo("codMunESI")+"="+campos.getAtributo("codMunECO")+")"
                    + " WHERE " +
                    campos.getAtributo("codPaisESI")+"=" + campos.getAtributo("codigoPais")+ " AND " +
                    campos.getAtributo("codProvESI")+"=" + gVO.getAtributo("codProvincia") + " AND " +
                    campos.getAtributo("codMunESI")+"=" + gVO.getAtributo("codMunicipio");
            String sqlMinus =
                    "SELECT 0,0,"+
                            campos.getAtributo("codESI")+","+
                            campos.getAtributo("descESI")+",0,'',"+
                            campos.getAtributo("descECO")+
                            " FROM T_TRM "+
                    " left join T_ESI on ("+campos.getAtributo("codESINUCTRM")+"=" + gVO.getAtributo("codESI")+
                    " AND "+ campos.getAtributo("codPaisNUCTRM")+"="+campos.getAtributo("codPaisESI")+
                    " AND "+ campos.getAtributo("codProvNUCTRM")+"="+campos.getAtributo("codProvESI")+
                    " AND "+ campos.getAtributo("codMunNUCTRM")+"="+campos.getAtributo("codMunESI")+ ") "+
                    " left join T_ECO on ("+campos.getAtributo("codECOESI")+"=" + gVO.getAtributo("codECO")+
                    " AND "+ campos.getAtributo("codPaisESI")+"="+campos.getAtributo("codPaisECO")+
                    " AND "+ campos.getAtributo("codProvESI")+"="+campos.getAtributo("codProvECO")+
                    " AND "+ campos.getAtributo("codMunESI")+"="+campos.getAtributo("codMunECO")
                    +")"
                    
                    + " WHERE " +
                            campos.getAtributo("codPaisTRM")+"=" + campos.getAtributo("codigoPais")+ " AND " +
                            campos.getAtributo("codProvTRM")+"=" + gVO.getAtributo("codProvincia") + " AND " +
                            campos.getAtributo("codMunTRM")+"=" + gVO.getAtributo("codMunicipio");

            sql = sql + bd.minus(sql,sqlMinus);

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state =  con.createStatement();
            rs = state.executeQuery(sql);
            while (rs.next()) {
                if((codigo>0)&&(codigo==rs.getInt("COD"))){
                    GeneralValueObject vo = new GeneralValueObject();
                    vo.setAtributo("idVia", rs.getString("IDVIA"));
                    vo.setAtributo("codVia", rs.getString("COD"));
                    vo.setAtributo("esiCod", rs.getString("CODESI"));
                    vo.setAtributo("descVia", rs.getString("NOMBRE"));
                    vo.setAtributo("codTipoVia", rs.getString("TVIACOD"));
                    vo.setAtributo("descTipoVia", rs.getString("TVIADES"));
                    vo.setAtributo("lugar", rs.getString("LUGAR"));
                    resultado.add(vo);
                    break;
                }else if(codigo==0){
                    GeneralValueObject vo = new GeneralValueObject();
                    vo.setAtributo("idVia", rs.getString("IDVIA"));
                    vo.setAtributo("codVia", rs.getString("COD"));
                    vo.setAtributo("esiCod", rs.getString("CODESI"));
                    vo.setAtributo("descVia", rs.getString("NOMBRE"));
                    vo.setAtributo("codTipoVia", rs.getString("TVIACOD"));
                    vo.setAtributo("descTipoVia", rs.getString("TVIADES"));
                    vo.setAtributo("lugar", rs.getString("LUGAR"));
                    resultado.add(vo);
                }
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return resultado;
    }


    public Vector getListaDomicilios(String[] params,GeneralValueObject gVO)
            throws TechnicalException{
        String sql = "";
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            state = con.createStatement();
            String codPais=(String)campos.getAtributo("codigoPais");
            String codProvincia=(String)gVO.getAtributo("codProvincia");
            String codMunicipio=(String)gVO.getAtributo("codMunicipio");
            String codigoVia=(String)gVO.getAtributo("codVia");
            String codTipoVia = (String)gVO.getAtributo("codTipoVia");
            String numDesde = (String)gVO.getAtributo("numDesde");
            String letraDesde = (String)gVO.getAtributo("letraDesde");
            String numHasta = (String)gVO.getAtributo("numHasta");
            String letraHasta = (String)gVO.getAtributo("letraHasta");
            String bloque = (String)gVO.getAtributo("bloque");
            String portal = (String)gVO.getAtributo("portal");
            String escalera = (String)gVO.getAtributo("escalera");
            String planta = (String)gVO.getAtributo("planta");
            String puerta = (String)gVO.getAtributo("puerta");
            String km = (String)gVO.getAtributo("km");
            String hm = (String)gVO.getAtributo("hm");
            /* anadir ECO/ESI */
            String codECO = (String)gVO.getAtributo("codECO");
            String codESI = (String)gVO.getAtributo("codESI");
            /* fin anadir ECO/ESI */

            sql = "SELECT DISTINCT T_DSU.*,T_DPO.*,T_VIA.*,T_TVI.*,T_PAI.*,T_PRV.*,T_MUN.*,T_TOC.*, " +
                    "("+  bd.funcionCadena(bd.FUNCIONCADENA_CONCAT,new String[]{(String) campos.getAtributo("descESI"),"' - '",(String) campos.getAtributo("descECO")}) +") AS LUGAR, "
                    + campos.getAtributo("codECO")+","+campos.getAtributo("descECO")+","
                    + campos.getAtributo("codESI")+","+ campos.getAtributo("descESI")
                    + " FROM "+
                    GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"+
                    GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,"+
                    GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN, "+
					 " T_DOT " +
				        "Right join T_DPO on ("+campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDPO")+")"+
					"left join T_TOC on ("+campos.getAtributo("tipoOcupacionDOT")+"="+campos.getAtributo("codTOC")+"),"+
					" T_DSU "+
					" left join T_VIA on ("+campos.getAtributo("codViaDSU")+"="+campos.getAtributo("idVIA")+
					" AND "+campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisVIA") +
					" AND "+campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvVIA") +
					" AND "+campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMunVIA") +
					" left join T_TVI on ("+campos.getAtributo("tipoVIA")+"="+campos.getAtributo("codTVI")+
					" left join T_TRM on ("+campos.getAtributo("codPaisTRMDSU")+"="+campos.getAtributo("codPaisTRM")+
					" AND "+campos.getAtributo("codProvTRMDSU")+"="+campos.getAtributo("codProvTRM") +
					" AND "+campos.getAtributo("codMunTRMDSU")+"="+campos.getAtributo("codMunTRM") +
					" AND "+campos.getAtributo("tipoNumeracionTRMDSU")+"="+campos.getAtributo("tipoNumeracionTRM") +
					" AND "+campos.getAtributo("vialTRMDSU")+"="+campos.getAtributo("viaTRM") +
					" AND "+campos.getAtributo("codigoTRMDSU")+"="+campos.getAtributo("codigoTRM") +")"+
					" left join T_ESI on ("+campos.getAtributo("codPaisNUCTRM")+"="+campos.getAtributo("codPaisESI")+
					" AND "+campos.getAtributo("codProvNUCTRM")+"="+campos.getAtributo("codProvESI") +
					" AND "+campos.getAtributo("codMunNUCTRM")+"="+campos.getAtributo("codMunESI") +
					" AND "+campos.getAtributo("codESINUCTRM")+"="+campos.getAtributo("codESI") +")"+
					" left join T_ECO on ("+campos.getAtributo("codECOESI")+"="+campos.getAtributo("codECO")+
					" AND "+campos.getAtributo("codPaisESI")+"="+campos.getAtributo("codPaisECO") +
					" AND "+campos.getAtributo("codProvESI")+"="+campos.getAtributo("codProvECO") +
					" AND "+campos.getAtributo("codMunESI")+"="+campos.getAtributo("codMunECO") +")"
                   			+" WHERE "+
                    campos.getAtributo("situacionDSU")+"='A' AND " + campos.getAtributo("situacionDPO")+"='A' AND " +
                    campos.getAtributo("situacionVIA")+"='A' AND " + campos.getAtributo("situacionTRM")+"='A' AND " +
                    campos.getAtributo("situacionESI")+"='A' AND " + campos.getAtributo("situacionECO")+"='A' AND " +                                      
                    campos.getAtributo("dirSueloDPO")+"="+ campos.getAtributo("codDirSueloDSU")+" AND "+
                    campos.getAtributo("codPaisDSU")+"=" + codPais + " AND "+
                    campos.getAtributo("codProvDSU")+"=" + codProvincia + " AND " +
                    campos.getAtributo("codMunDSU")+"=" + codMunicipio + " AND " +
                    campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMUN")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV");

	    sql = (codigoVia.equals("")) ? sql:sql+" AND "+campos.getAtributo("codVIA")+"=" + codigoVia;
	    sql = (codTipoVia.equals("")) ? sql:sql+" AND "+campos.getAtributo("tipoVIA")+"="+ codTipoVia;
            sql = (numDesde.equals("")) ? sql:sql+" AND "+campos.getAtributo("numDesdeDSU")+"="+numDesde;
            sql = (letraDesde.equals("")) ? sql:sql+" AND "+campos.getAtributo("letraDesdeDSU")+"="+bd.addString(letraDesde);
            sql = (numHasta.equals("")) ? sql:sql+" AND "+campos.getAtributo("numHastaDSU")+"="+numHasta;
            sql = (letraHasta.equals("")) ? sql:sql+" AND "+campos.getAtributo("letraHastaDSU")+"="+bd.addString(letraHasta);
            sql = (bloque.equals("")) ? sql:sql+" AND "+campos.getAtributo("bloqueDSU")+"="+bd.addString(bloque);
            sql = (portal.equals("")) ? sql:sql+" AND "+campos.getAtributo("portalDSU")+"="+bd.addString(portal);
            sql = (escalera.equals("")) ? sql:sql+" AND "+campos.getAtributo("escaleraDPO")+"="+bd.addString(escalera);
            sql = (planta.equals("")) ? sql:sql+" AND "+campos.getAtributo("plantaDPO")+"="+bd.addString(planta);
            sql = (puerta.equals("")) ? sql:sql+" AND "+campos.getAtributo("puertaDPO")+"="+bd.addString(puerta);
            sql = (km.equals("")) ? sql:sql+" AND "+campos.getAtributo("kmDSU")+"="+km;
            sql = (hm.equals("")) ? sql:sql+" AND "+campos.getAtributo("hmDSU")+"="+hm;
            if (codECO != null){
                if (!"".equals(codECO)){
                    sql += " AND " + campos.getAtributo("codECO")+"="+ codECO;
                }
            }
            if (codESI != null){
                if (!"".equals(codESI)){
                    sql += " AND " + campos.getAtributo("codESI")+"="+ codESI;
                }
            }
            sql+= " ORDER BY "+campos.getAtributo("numDesdeDSU")+","+
                    campos.getAtributo("letraDesdeDSU")+","+campos.getAtributo("numHastaDSU")+","+
                    campos.getAtributo("letraHastaDSU")+","+campos.getAtributo("bloqueDSU")+","+
                    campos.getAtributo("portalDSU")+","+campos.getAtributo("escaleraDPO")+","+
                    campos.getAtributo("plantaDPO")+","+campos.getAtributo("puertaDPO");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while(rs.next()){
                GeneralValueObject domicilioVO = new GeneralValueObject();
                numDesde = rs.getString((String)campos.getAtributo("numDesdeDSU"));
                letraDesde = rs.getString((String)campos.getAtributo("letraDesdeDSU"));
                numHasta = rs.getString((String)campos.getAtributo("numHastaDSU"));
                letraHasta = rs.getString((String)campos.getAtributo("letraHastaDSU"));
                bloque = rs.getString((String)campos.getAtributo("bloqueDSU"));
                portal = rs.getString((String)campos.getAtributo("portalDSU"));
                escalera = rs.getString((String)campos.getAtributo("escaleraDPO"));
                planta = rs.getString((String)campos.getAtributo("plantaDPO"));
                puerta = rs.getString((String)campos.getAtributo("puertaDPO"));
                km = rs.getString((String)campos.getAtributo("kmDSU"));
                hm = rs.getString((String)campos.getAtributo("hmDSU"));
                domicilioVO.setAtributo("normalizado","si");
                domicilioVO.setAtributo("idDomicilio",rs.getString((String)campos.getAtributo("idDomicilioDPO")));
                domicilioVO.setAtributo("codPais",rs.getString((String)campos.getAtributo("codPAI")));
                domicilioVO.setAtributo("codProvincia",rs.getString((String)campos.getAtributo("codPRV")));
                domicilioVO.setAtributo("codMunicipio",rs.getString((String)campos.getAtributo("codMUN")));
                domicilioVO.setAtributo("descPais",rs.getString((String)campos.getAtributo("descPAI")));
                domicilioVO.setAtributo("descProvincia",rs.getString((String)campos.getAtributo("descPRV")));
                domicilioVO.setAtributo("descMunicipio",rs.getString((String)campos.getAtributo("descMUN")));
                domicilioVO.setAtributo("idVia",rs.getString((String)campos.getAtributo("idVIA")));
                domicilioVO.setAtributo("codVia",rs.getString((String)campos.getAtributo("codVIA")));
                domicilioVO.setAtributo("descVia",rs.getString((String)campos.getAtributo("descVIA")));
                domicilioVO.setAtributo("codTipoVia",rs.getString((String)campos.getAtributo("codTVI")));
                domicilioVO.setAtributo("descTipoVia",rs.getString((String)campos.getAtributo("descTVI")));
                domicilioVO.setAtributo("numDesde",numDesde);
                domicilioVO.setAtributo("letraDesde",letraDesde);
                domicilioVO.setAtributo("numHasta",numHasta);
                domicilioVO.setAtributo("letraHasta",letraHasta);
                domicilioVO.setAtributo("bloque",bloque);
                domicilioVO.setAtributo("portal",portal);
                domicilioVO.setAtributo("escalera",escalera);
                domicilioVO.setAtributo("planta",planta);
                domicilioVO.setAtributo("puerta",puerta);
                domicilioVO.setAtributo("km",km);
                domicilioVO.setAtributo("hm",hm);
                domicilioVO.setAtributo("lugar",rs.getString("LUGAR"));
                domicilioVO.setAtributo("codPostal",rs.getString((String)campos.getAtributo("codPostalDSU")));
                domicilioVO.setAtributo("codUso",rs.getString((String)campos.getAtributo("codTOC")));
                domicilioVO.setAtributo("descUso",rs.getString((String)campos.getAtributo("descTOC")));
                domicilioVO.setAtributo("codECO",rs.getString((String)campos.getAtributo("codECO")));
                domicilioVO.setAtributo("descECO",rs.getString((String)campos.getAtributo("descECO")));
                domicilioVO.setAtributo("codESI",rs.getString((String)campos.getAtributo("codESI")));
                domicilioVO.setAtributo("descESI",rs.getString((String)campos.getAtributo("descESI")));
                resultado.add(domicilioVO);
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return resultado;
    }

    public Vector getListaDomiciliosNoNormalizados(String[] params,GeneralValueObject gVO)
            throws TechnicalException{
        String sql = "";
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        TransformacionAtributoSelect transformador;
        try{
            bd = new AdaptadorSQLBD(params);
            transformador = new TransformacionAtributoSelect(bd);
            con = bd.getConnection();
            state = con.createStatement();
            String codPais=(String)campos.getAtributo("codigoPais");
            String codProvincia=(String)gVO.getAtributo("codProvincia");
            String codMunicipio=(String)gVO.getAtributo("codMunicipio");
            String codigoVia=(String)gVO.getAtributo("codVia");
            if(m_Log.isDebugEnabled()) m_Log.debug("TercerosDAO: codVia " + gVO.getAtributo("codVia"));
            String codTipoVia = (String)gVO.getAtributo("codTipoVia");
            String numDesde = (String)gVO.getAtributo("numDesde");
            String letraDesde = (String)gVO.getAtributo("letraDesde");
            String numHasta = (String)gVO.getAtributo("numHasta");
            String letraHasta = (String)gVO.getAtributo("letraHasta");
            String bloque = (String)gVO.getAtributo("bloque");
            String portal = (String)gVO.getAtributo("portal");
            String escalera = (String)gVO.getAtributo("escalera");
            String planta = (String)gVO.getAtributo("planta");
            String puerta = (String)gVO.getAtributo("puerta");
            String domicilio = (String)gVO.getAtributo("domicilio");
            String poblacion = (String)gVO.getAtributo("poblacion");
            String codPostal = (String)gVO.getAtributo("codPostal");
            /* anadir ECO/ESI */
            String codECO = (String)gVO.getAtributo("codECO");
            String codESI = (String)gVO.getAtributo("codESI");
            /* fin anadir ECO/ESI */

            sql = "SELECT T_DNN.*,T_VIA.*,T_TVI.*,T_PAI.*,T_PRV.*,T_MUN.*, "
                    + campos.getAtributo("codECO") + "," + campos.getAtributo("descECO") + ","
                    + campos.getAtributo("codESI") + "," + campos.getAtributo("descESI")
                    + " FROM T_DNN "
                    + " left join T_VIA on (" + campos.getAtributo("codViaDNN") + "=" + campos.getAtributo("idVIA")
                    + " AND " + campos.getAtributo("codPaisViaDNN") + "=" + campos.getAtributo("codPaisVIA")
                    + " AND " + campos.getAtributo("codProvViaDNN") + "=" + campos.getAtributo("codProvVIA")
                    + " AND " + campos.getAtributo("codMunViaDNN") + "=" + campos.getAtributo("codMunVIA") + ")"
                    + " left join T_TVI on (" + campos.getAtributo("tipoVIA") + "=" + campos.getAtributo("codTVI") + ")"
                    + " left join T_ESI on (" + campos.getAtributo("codPaisESIDNN") + "=" + campos.getAtributo("codPaisESI")
                    + " AND " + campos.getAtributo("codProvESIDNN") + "=" + campos.getAtributo("codProvESI")
                    + " AND " + campos.getAtributo("codMunESIDNN") + "=" + campos.getAtributo("codMunESI")
                    + " AND " + campos.getAtributo("codESIDNN") + "=" + campos.getAtributo("codESI") + ")"
                    + " left join T_ECO on (" + campos.getAtributo("codECOESI") + "=" + campos.getAtributo("codECO")
                    + " AND " + campos.getAtributo("codPaisESI") + "=" + campos.getAtributo("codPaisECO")
                    + " AND " + campos.getAtributo("codProvESI") + "=" + campos.getAtributo("codProvECO")
                    + " AND " + campos.getAtributo("codMunESI") + "=" + campos.getAtributo("codMunECO") + ")"
                    + ","
                    + GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"
                    + GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,"
                    + GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN "
                    + "WHERE "
                    + campos.getAtributo("codPaisDNN") + "=" + codPais + " AND "
                    + campos.getAtributo("codProvDNN") + "=" + codProvincia + " AND "
                    + campos.getAtributo("codMunDNN") + "=" + codMunicipio + " AND "
                    + campos.getAtributo("situacionDNN") + "='A' AND "
                    + campos.getAtributo("codPaisDNN") + "=" + campos.getAtributo("codPaisMUN") + " AND "
                    + campos.getAtributo("codProvDNN") + "=" + campos.getAtributo("codProvMUN") + " AND "
                    + campos.getAtributo("codMunDNN") + "=" + campos.getAtributo("codMUN") + " AND "
                    + campos.getAtributo("codPaisMUN") + "=" + campos.getAtributo("codPAI") + " AND "
                    + campos.getAtributo("codPaisMUN") + "=" + campos.getAtributo("codPaisPRV") + " AND "
                    + campos.getAtributo("codProvMUN") + "=" + campos.getAtributo("codPRV") ;
            sql = (codigoVia.equals("")) ? sql : sql + " AND " + campos.getAtributo("codViaDNN") + "=" + codigoVia + "  ";
            sql = (codTipoVia.equals("")) ? sql : sql + " AND " + campos.getAtributo("tipoVIA") + "=" + codTipoVia;
            sql = (numDesde.equals("")) ? sql : sql + " AND " + campos.getAtributo("numDesdeDNN") + "=" + numDesde;
            sql = (letraDesde.equals("")) ? sql : sql + " AND " + campos.getAtributo("letraDesdeDNN") + "=" + bd.addString(letraDesde);
            sql = (numHasta.equals("")) ? sql : sql + " AND " + campos.getAtributo("numHastaDNN") + "=" + numHasta;
            sql = (letraHasta.equals("")) ? sql : sql + " AND " + campos.getAtributo("letraHastaDNN") + "=" + bd.addString(letraHasta);
            sql = (bloque.equals("")) ? sql : sql + " AND " + campos.getAtributo("bloqueDNN") + "=" + bd.addString(bloque);
            sql = (portal.equals("")) ? sql : sql + " AND " + campos.getAtributo("portalDNN") + "=" + bd.addString(portal);
            sql = (escalera.equals("")) ? sql : sql + " AND " + campos.getAtributo("escaleraDNN") + "=" + bd.addString(escalera);
            sql = (planta.equals("")) ? sql : sql + " AND " + campos.getAtributo("plantaDNN") + "=" + bd.addString(planta);
            sql = (puerta.equals("")) ? sql : sql + " AND " + campos.getAtributo("puertaDNN") + "=" + bd.addString(puerta);
            sql = (codPostal.equals("")) ? sql : sql + " AND " + campos.getAtributo("codPostalDNN") + "=" + bd.addString(codPostal);
            String condicion = transformador.construirCondicionWhereConOperadores((String) campos.getAtributo("domicilioDNN"), domicilio.trim(), false);
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("la condicion del domicilio en el DAO es : " + condicion);
            }
            if ((!domicilio.equals("")&&(domicilio!=null))) consultaInsensitiva=true;
            
            sql = (domicilio.equals("")) ? sql : sql + " AND " + condicion;
            String condicion1 = transformador.construirCondicionWhereConOperadores((String) campos.getAtributo("barriadaDNN"), poblacion.trim(), false);
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("la condicion de la poblacion en el DAO es : " + condicion1);
            }
            sql = (poblacion.equals("")) ? sql : sql + " AND " + condicion1;
            /* anadir ECO/ESI */
            if (codECO != null) {
                if (!"".equals(codECO)) {
                    sql += " AND " + campos.getAtributo("codECO") + "=" + codECO;
                }
            }
            if (codESI != null) {
                if (!"".equals(codESI)) {
                    sql += " AND " + campos.getAtributo("codESI") + "=" + codESI;
                }
            }
            /* fin anadir ECO/ESI */
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            if(consultaInsensitiva){
                String alter1=null;
                String alter2=null;
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    m_Log.debug("ALTER SESSION SET LINGUISTIC/GENERICBASELETTER");

                    state.executeQuery(alter1);
                    state.executeQuery(alter2);

                } 
            }
            
            rs = state.executeQuery(sql);
            while(rs.next()){
                GeneralValueObject domicilioVO = new GeneralValueObject();
                numDesde = rs.getString((String)campos.getAtributo("numDesdeDNN"));
                letraDesde = rs.getString((String)campos.getAtributo("letraDesdeDNN"));
                numHasta = rs.getString((String)campos.getAtributo("numHastaDNN"));
                letraHasta = rs.getString((String)campos.getAtributo("letraHastaDNN"));
                bloque = rs.getString((String)campos.getAtributo("bloqueDNN"));
                portal = rs.getString((String)campos.getAtributo("portalDNN"));
                escalera = rs.getString((String)campos.getAtributo("escaleraDNN"));
                planta = rs.getString((String)campos.getAtributo("plantaDNN"));
                puerta = rs.getString((String)campos.getAtributo("puertaDNN"));
                domicilioVO.setAtributo("normalizado","no");
                domicilioVO.setAtributo("idDomicilio",rs.getString((String)campos.getAtributo("idDomicilioDNN")));
                domicilioVO.setAtributo("codPais",rs.getString((String)campos.getAtributo("codPAI")));
                domicilioVO.setAtributo("codProvincia",rs.getString((String)campos.getAtributo("codPRV")));
                domicilioVO.setAtributo("codMunicipio",rs.getString((String)campos.getAtributo("codMUN")));
                domicilioVO.setAtributo("descPais",rs.getString((String)campos.getAtributo("descPAI")));
                domicilioVO.setAtributo("descProvincia",rs.getString((String)campos.getAtributo("descPRV")));
                domicilioVO.setAtributo("descMunicipio",rs.getString((String)campos.getAtributo("descMUN")));
                domicilioVO.setAtributo("idVia",rs.getString((String)campos.getAtributo("idVIA")));
                domicilioVO.setAtributo("codVia",rs.getString((String)campos.getAtributo("codVIA")));
                domicilioVO.setAtributo("descVia",rs.getString((String)campos.getAtributo("descVIA")));
                domicilioVO.setAtributo("codTipoVia",rs.getString((String)campos.getAtributo("codTVI")));
                domicilioVO.setAtributo("descTipoVia",rs.getString((String)campos.getAtributo("descTVI")));
                domicilioVO.setAtributo("numDesde",numDesde);
                domicilioVO.setAtributo("letraDesde",letraDesde);
                domicilioVO.setAtributo("numHasta",numHasta);
                domicilioVO.setAtributo("letraHasta",letraHasta);
                domicilioVO.setAtributo("bloque",bloque);
                domicilioVO.setAtributo("portal",portal);
                domicilioVO.setAtributo("escalera",escalera);
                domicilioVO.setAtributo("planta",planta);
                domicilioVO.setAtributo("puerta",puerta);
                domicilioVO.setAtributo("codPostal",rs.getString((String)campos.getAtributo("codPostalDNN")));
                domicilioVO.setAtributo("domicilio",rs.getString((String)campos.getAtributo("domicilioDNN")));
                domicilioVO.setAtributo("poblacion",rs.getString((String)campos.getAtributo("barriadaDNN")));
                domicilioVO.setAtributo("codECO",rs.getString((String)campos.getAtributo("codECO")));
                domicilioVO.setAtributo("descECO",rs.getString((String)campos.getAtributo("descECO")));
                domicilioVO.setAtributo("codESI",rs.getString((String)campos.getAtributo("codESI")));
                domicilioVO.setAtributo("descESI",rs.getString((String)campos.getAtributo("descESI")));
                domicilioVO.setAtributo("refCatastral",rs.getString((String)campos.getAtributo("refCatastralDNN")));
                resultado.add(domicilioVO);
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
        }catch (Exception e){
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
           
            try{
                if (consultaInsensitiva){
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                       String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                       String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                       m_Log.debug("ALTER SESSION SET BINARY/SPANISH");

                       state=con.createStatement();
                       state.executeQuery(alter1);
                       SigpGeneralOperations.closeStatement(state);
                       state=con.createStatement();
                       state.executeQuery(alter2);
                       SigpGeneralOperations.closeStatement(state);
                   } 
                }
            }catch (SQLException se){
                if(m_Log.isErrorEnabled()) m_Log.error(se.getMessage());
            }
            
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return resultado;
    }

    public Vector getDomiciliosById(String[] params,GeneralValueObject gVO)
            throws TechnicalException{
        String sql = "";
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            state = con.createStatement();
            String codDomicilio=(String)gVO.getAtributo("codDomicilio");

            sql = "SELECT " + campos.getAtributo("normalizadoDOM") + " FROM T_DOM WHERE " +
                    campos.getAtributo("idDomicilioDOM")+"=" + codDomicilio;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            String normalizado = "";
            while(rs.next()){
                int n = rs.getInt((String)campos.getAtributo("normalizadoDOM"));
                if(n ==1) normalizado = "si";
                else normalizado = "no";
            }
            
            String parteFrom;
            String parteWhere;
            ArrayList join = new ArrayList();
            if("si".equals(normalizado)) {
                parteFrom = " T_DSU.*,T_DPO.*,T_VIA.*,T_TVI.*,T_PAI.*,T_PRV.*,T_MUN.*,T_TOC.*, " +
                        "("+  bd.funcionCadena(bd.FUNCIONCADENA_CONCAT,new String[]{(String) campos.getAtributo("descESI"),"' - '",(String) campos.getAtributo("descECO")}) +") AS LUGAR, "
                        + campos.getAtributo("codECO")+","+campos.getAtributo("descECO")+","
                        + campos.getAtributo("codESI")+","+campos.getAtributo("descESI");

                parteWhere = null;

                join.add("T_TOC, T_DSU");
                join.add("INNER");
                join.add("T_DPO");
                join.add(campos.getAtributo("idDomicilioDPO")+"=" + codDomicilio + " AND "+
                        campos.getAtributo("dirSueloDPO")+"="+ campos.getAtributo("codDirSueloDSU"));
                join.add("INNER");
                join.add(GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN");
                join.add(campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                        campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvMUN")+" AND "+
                        campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMUN"));
                join.add("INNER");
                join.add(GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI");
                join.add(campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI"));
                join.add("INNER");
                join.add(GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV");
                join.add(campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                        campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV"));
                join.add("LEFT");
                join.add("T_VIA");
                join.add((String) campos.getAtributo("codViaDSU") + "=" + (String) campos.getAtributo("idVIA") + " AND " +
                        (String) campos.getAtributo("codPaisDSU") + "=" + (String) campos.getAtributo("codPaisVIA") + " AND " +
                        (String) campos.getAtributo("codProvDSU") + "=" + (String) campos.getAtributo("codProvVIA") + " AND " +
                        (String) campos.getAtributo("codMunDSU") + "=" + (String) campos.getAtributo("codMunVIA"));
                join.add("LEFT");
                join.add("T_TRM");
                join.add((String) campos.getAtributo("codPaisTRMDSU") + "=" + (String) campos.getAtributo("codPaisTRM") + " AND " +
                        (String) campos.getAtributo("codProvTRMDSU") + "=" + (String) campos.getAtributo("codProvTRM") + " AND " +
                        (String) campos.getAtributo("codMunTRMDSU") + "=" + (String) campos.getAtributo("codMunTRM") + " AND " +
                        (String) campos.getAtributo("tipoNumeracionTRMDSU") + "=" + (String) campos.getAtributo("tipoNumeracionTRM") + " AND " +
                        (String) campos.getAtributo("vialTRMDSU") + "=" + (String) campos.getAtributo("viaTRM") + " AND " +
                        (String) campos.getAtributo("codigoTRMDSU") + "=" + (String) campos.getAtributo("codigoTRM"));
                join.add("LEFT");
                join.add("T_ESI");
                join.add((String) campos.getAtributo("codPaisNUCTRM") + "=" + (String) campos.getAtributo("codPaisESI") + " AND " +
                        (String) campos.getAtributo("codProvNUCTRM") + "=" + (String) campos.getAtributo("codProvESI") + " AND " +
                        (String) campos.getAtributo("codMunNUCTRM") + "=" + (String) campos.getAtributo("codMunESI") + " AND " +
                        (String) campos.getAtributo("codESINUCTRM") + "=" + (String) campos.getAtributo("codESI"));
                join.add("LEFT");
                join.add("T_ECO");
                join.add((String) campos.getAtributo("codECOESI") + "=" + (String) campos.getAtributo("codECO") + " AND "+
                        (String) campos.getAtributo("codPaisESI") + "=" + (String) campos.getAtributo("codPaisECO") + " AND " +
                        (String) campos.getAtributo("codProvESI") + "=" + (String) campos.getAtributo("codProvECO") + " AND "+
                        (String) campos.getAtributo("codMunESI") + "=" + (String) campos.getAtributo("codMunECO"));
                join.add("LEFT");
                join.add("T_TVI");
                join.add((String) campos.getAtributo("tipoVIA") + "=" + (String) campos.getAtributo("codTVI"));
                join.add("false");
            } else {
                parteFrom = " T_DNN.*,T_VIA.*,T_TVI.*,T_PAI.*,T_PRV.*,T_MUN.*,T_TOC.*, "
                        + campos.getAtributo("codECO")+","+campos.getAtributo("descECO")+","
                        + campos.getAtributo("codESI")+","+campos.getAtributo("descESI");

                parteWhere = campos.getAtributo("idDomicilioDNN")+"=" + codDomicilio;

                join.add("T_TOC, T_DNN");
                join.add("INNER");
                join.add(GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN");
                join.add(campos.getAtributo("codPaisDNN")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                        campos.getAtributo("codProvDNN")+"="+campos.getAtributo("codProvMUN")+" AND "+
                        campos.getAtributo("codMunDNN")+"="+campos.getAtributo("codMUN"));
                join.add("INNER");
                join.add(GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI");
                join.add(campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI"));
                join.add("INNER");
                join.add(GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV");
                join.add(campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                        campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV"));
                join.add("LEFT");
                join.add("T_VIA");
                join.add((String) campos.getAtributo("codViaDNN") + "=" + (String) campos.getAtributo("idVIA") + " AND " +
                        (String) campos.getAtributo("codPaisViaDNN") + "=" + (String) campos.getAtributo("codPaisVIA") + " AND " +
                        (String) campos.getAtributo("codProvViaDNN") + "=" + (String) campos.getAtributo("codProvVIA") + " AND "+
                        (String) campos.getAtributo("codMunViaDNN") + "=" + (String) campos.getAtributo("codMunVIA"));
                join.add("LEFT");
                join.add("T_ESI");
                join.add((String) campos.getAtributo("codPaisESIDNN") + "=" + (String) campos.getAtributo("codPaisESI") +  " AND " +
                        (String) campos.getAtributo("codProvESIDNN") + "=" + (String) campos.getAtributo("codProvESI") + " AND "+
                        (String) campos.getAtributo("codMunESIDNN") + "=" + (String) campos.getAtributo("codMunESI") + " AND " +
                        (String) campos.getAtributo("codESIDNN") + "=" + (String) campos.getAtributo("codESI"));
                join.add("LEFT");
                join.add("T_ECO");
                join.add((String) campos.getAtributo("codECOESI") + "=" + (String) campos.getAtributo("codECO") + " AND " +
                        (String) campos.getAtributo("codPaisESI") + "=" + (String) campos.getAtributo("codPaisECO") + " AND "+
                        (String) campos.getAtributo("codProvESI") + "=" + (String) campos.getAtributo("codProvECO") + " AND " +
                        (String) campos.getAtributo("codMunESI") + "=" + (String) campos.getAtributo("codMunECO"));
                join.add("LEFT");
                join.add("T_TVI");
                join.add((String) campos.getAtributo("idTipoViaDNN") + "=" + (String) campos.getAtributo("codTVI"));
                join.add("false");

            }

            sql = bd.join(parteFrom, parteWhere, (String[]) join.toArray(new String[]{}));
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while(rs.next()){
                GeneralValueObject domicilioVO = new GeneralValueObject();
                if("si".equals(normalizado)) {
                    String numDesde = rs.getString((String)campos.getAtributo("numDesdeDSU"));
                    String letraDesde = rs.getString((String)campos.getAtributo("letraDesdeDSU"));
                    String numHasta = rs.getString((String)campos.getAtributo("numHastaDSU"));
                    String letraHasta = rs.getString((String)campos.getAtributo("letraHastaDSU"));
                    String bloque = rs.getString((String)campos.getAtributo("bloqueDSU"));
                    String portal = rs.getString((String)campos.getAtributo("portalDSU"));
                    String escalera = rs.getString((String)campos.getAtributo("escaleraDPO"));
                    String planta = rs.getString((String)campos.getAtributo("plantaDPO"));
                    String puerta = rs.getString((String)campos.getAtributo("puertaDPO"));
                    String km =rs.getString((String)campos.getAtributo("kmDSU"));
                    String hm = rs.getString((String)campos.getAtributo("hmDSU"));
                    domicilioVO.setAtributo("normalizado","si");
                    domicilioVO.setAtributo("idDomicilio",rs.getString((String)campos.getAtributo("idDomicilioDPO")));
                    domicilioVO.setAtributo("codPais",rs.getString((String)campos.getAtributo("codPAI")));
                    domicilioVO.setAtributo("codProvincia",rs.getString((String)campos.getAtributo("codPRV")));
                    domicilioVO.setAtributo("codMunicipio",rs.getString((String)campos.getAtributo("codMUN")));
                    domicilioVO.setAtributo("descPais",rs.getString((String)campos.getAtributo("descPAI")));
                    domicilioVO.setAtributo("descProvincia",rs.getString((String)campos.getAtributo("descPRV")));
                    domicilioVO.setAtributo("descMunicipio",rs.getString((String)campos.getAtributo("descMUN")));
                    domicilioVO.setAtributo("idVia",rs.getString((String)campos.getAtributo("idVIA")));
                    domicilioVO.setAtributo("codVia",rs.getString((String)campos.getAtributo("codVIA")));
                    domicilioVO.setAtributo("descVia",rs.getString((String)campos.getAtributo("descVIA")));
                    domicilioVO.setAtributo("codTipoVia",rs.getString((String)campos.getAtributo("codTVI")));
                    domicilioVO.setAtributo("descTipoVia",rs.getString((String)campos.getAtributo("descTVI")));
                    domicilioVO.setAtributo("numDesde",numDesde);
                    domicilioVO.setAtributo("letraDesde",letraDesde);
                    domicilioVO.setAtributo("numHasta",numHasta);
                    domicilioVO.setAtributo("letraHasta",letraHasta);
                    domicilioVO.setAtributo("bloque",bloque);
                    domicilioVO.setAtributo("portal",portal);
                    domicilioVO.setAtributo("escalera",escalera);
                    domicilioVO.setAtributo("planta",planta);
                    domicilioVO.setAtributo("puerta",puerta);
                    domicilioVO.setAtributo("km",km);
                    domicilioVO.setAtributo("hm",hm);
                    domicilioVO.setAtributo("lugar",rs.getString("LUGAR"));
                    domicilioVO.setAtributo("codPostal",rs.getString((String)campos.getAtributo("codPostalDSU")));
                    domicilioVO.setAtributo("codUso",rs.getString((String)campos.getAtributo("codTOC")));
                    domicilioVO.setAtributo("descUso",rs.getString((String)campos.getAtributo("descTOC")));
                    /* anadir ECO/ESI */
                    domicilioVO.setAtributo("codECO",rs.getString((String)campos.getAtributo("codECO")));
                    domicilioVO.setAtributo("descECO",rs.getString((String)campos.getAtributo("descECO")));
                    domicilioVO.setAtributo("codESI",rs.getString((String)campos.getAtributo("codESI")));
                    domicilioVO.setAtributo("descESI",rs.getString((String)campos.getAtributo("descESI")));
                    /* fin anadir ECO/ESI */
                    resultado.add(domicilioVO);
                } else {
                    String numDesde = rs.getString((String)campos.getAtributo("numDesdeDNN"));
                    String letraDesde = rs.getString((String)campos.getAtributo("letraDesdeDNN"));
                    String numHasta = rs.getString((String)campos.getAtributo("numHastaDNN"));
                    String letraHasta = rs.getString((String)campos.getAtributo("letraHastaDNN"));
                    String bloque = rs.getString((String)campos.getAtributo("bloqueDNN"));
                    String portal = rs.getString((String)campos.getAtributo("portalDNN"));
                    String escalera = rs.getString((String)campos.getAtributo("escaleraDNN"));
                    String planta = rs.getString((String)campos.getAtributo("plantaDNN"));
                    String puerta = rs.getString((String)campos.getAtributo("puertaDNN"));
                    domicilioVO.setAtributo("normalizado","no");
                    domicilioVO.setAtributo("idDomicilio",rs.getString((String)campos.getAtributo("idDomicilioDNN")));
                    domicilioVO.setAtributo("codPais",rs.getString((String)campos.getAtributo("codPAI")));
                    domicilioVO.setAtributo("codProvincia",rs.getString((String)campos.getAtributo("codPRV")));
                    domicilioVO.setAtributo("codMunicipio",rs.getString((String)campos.getAtributo("codMUN")));
                    domicilioVO.setAtributo("descPais",rs.getString((String)campos.getAtributo("descPAI")));
                    domicilioVO.setAtributo("descProvincia",rs.getString((String)campos.getAtributo("descPRV")));
                    domicilioVO.setAtributo("descMunicipio",rs.getString((String)campos.getAtributo("descMUN")));
                    domicilioVO.setAtributo("idVia",rs.getString((String)campos.getAtributo("idVIA")));
                    domicilioVO.setAtributo("codVia",rs.getString((String)campos.getAtributo("codVIA")));
                    domicilioVO.setAtributo("descVia",rs.getString((String)campos.getAtributo("descVIA")));
                    domicilioVO.setAtributo("codTipoVia",rs.getString((String)campos.getAtributo("codTVI")));
                    domicilioVO.setAtributo("descTipoVia",rs.getString((String)campos.getAtributo("descTVI")));
                    domicilioVO.setAtributo("numDesde",numDesde);
                    domicilioVO.setAtributo("letraDesde",letraDesde);
                    domicilioVO.setAtributo("numHasta",numHasta);
                    domicilioVO.setAtributo("letraHasta",letraHasta);
                    domicilioVO.setAtributo("bloque",bloque);
                    domicilioVO.setAtributo("portal",portal);
                    domicilioVO.setAtributo("escalera",escalera);
                    domicilioVO.setAtributo("planta",planta);
                    domicilioVO.setAtributo("puerta",puerta);
                    domicilioVO.setAtributo("codPostal",rs.getString((String)campos.getAtributo("codPostalDNN")));
                    domicilioVO.setAtributo("codUso",rs.getString((String)campos.getAtributo("codTOC")));
                    domicilioVO.setAtributo("descUso",rs.getString((String)campos.getAtributo("descTOC")));
                    domicilioVO.setAtributo("domicilio",rs.getString((String)campos.getAtributo("domicilioDNN")));
                    domicilioVO.setAtributo("poblacion",rs.getString((String)campos.getAtributo("barriadaDNN")));
                    /* anadir ECO/ESI */
                    domicilioVO.setAtributo("codECO",rs.getString((String)campos.getAtributo("codECO")));
                    domicilioVO.setAtributo("descECO",rs.getString((String)campos.getAtributo("descECO")));
                    domicilioVO.setAtributo("codESI",rs.getString((String)campos.getAtributo("codESI")));
                    domicilioVO.setAtributo("descESI",rs.getString((String)campos.getAtributo("descESI")));
                    domicilioVO.setAtributo("refCatastral",rs.getString((String)campos.getAtributo("refCatastralDNN")));
                    /* fin anadir ECO/ESI */
                    resultado.add(domicilioVO);
                }
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return resultado;
    }


    public Vector getByDocumento(TercerosValueObject terVO, String[] params)
            throws TechnicalException{
        String sql= "";
        Vector resultado=new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            state = con.createStatement();

            String from = "T_TER.*,T_DOM.*,"+
                    campos.getAtributo("codTVI")+" AS CODTIPOVIA,"+
                    campos.getAtributo("descTVI")+" AS DESCTIPOVIA," +
                    campos.getAtributo("idVIA")+" AS IDVIA,"+
                    campos.getAtributo("codVIA")+" AS CODVIA,"+
                    campos.getAtributo("descVIA")+" AS DESCVIA,"+
                    campos.getAtributo("domicilioDNN")+" AS DESCDMC," +
                    campos.getAtributo("numDesdeDNN")+" AS NUD,"+
                    campos.getAtributo("letraDesdeDNN")+" AS LED,"+
                    campos.getAtributo("numHastaDNN")+" AS NUH,"+
                    campos.getAtributo("letraHastaDNN")+" AS LEH," +
                    campos.getAtributo("bloqueDNN")+" AS BLQ,"+
                    campos.getAtributo("portalDNN")+" AS POR,"+
                    campos.getAtributo("escaleraDNN")+" AS ESC,"+
                    campos.getAtributo("plantaDNN")+" AS PLT," +
                    campos.getAtributo("puertaDNN")+" AS PTA,"+
                    campos.getAtributo("codPostalDNN")+" AS CPO,"+
                    campos.getAtributo("barriadaDNN")+" AS CAS,"+
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA")+","+campos.getAtributo("codECO") + "," +
                    campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");

            ArrayList join = new ArrayList();
            join.add("T_TER");
            join.add("INNER");
            join.add("T_DOT");
            join.add(campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT")+" AND "+
                    campos.getAtributo("tipoDocTER")+"="+terVO.getTipoDocumento()+" AND "+
                    campos.getAtributo("documentoTER")+"="+bd.addString(terVO.getDocumento()) + " AND " +
                    campos.getAtributo("situacionTER")+"='A'");
            join.add("INNER");
            join.add("T_DOM");
            join.add(campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM")+" AND "+
                    campos.getAtributo("normalizadoDOM")+"=2 AND "+
                    campos.getAtributo("situacionDOT")+"='A'");
            join.add("INNER");
            join.add("T_DNN");
            join.add(campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDNN"));
            join.add("LEFT");
            join.add("T_TOC");
            join.add(campos.getAtributo("tipoOcupacionDOT")+"="+campos.getAtributo("codTOC"));
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO+"T_MUN T_MUN");
            join.add(campos.getAtributo("codPaisDNN")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDNN")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDNN")+"="+campos.getAtributo("codMUN"));
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO+"T_PAI T_PAI");
            join.add(campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI"));
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO+"T_PRV T_PRV");
            join.add(campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV"));
            join.add("LEFT");
            join.add("T_VIA");
            join.add(campos.getAtributo("codViaDNN")+"="+campos.getAtributo("idVIA")+ " AND " +
                    campos.getAtributo("codProvViaDNN")+"="+campos.getAtributo("codPaisVIA")+" AND " +
                    campos.getAtributo("codProvViaDNN")+"="+campos.getAtributo("codProvVIA")+" AND " +
                    campos.getAtributo("codMunViaDNN")+"="+campos.getAtributo("codMunVIA"));
            join.add("LEFT");
            join.add("T_TVI");
            join.add(campos.getAtributo("idTipoViaDNN")+"="+campos.getAtributo("codTVI"));
            join.add("LEFT");
            join.add("T_ESI");
            join.add(campos.getAtributo("codESIDNN")+"="+campos.getAtributo("codESI"));
            join.add("LEFT");
            join.add("T_ECO");
            join.add(campos.getAtributo("codECOESI")+"="+campos.getAtributo("codECO"));
            join.add("false");
            sql = bd.join(from,null,(String[])join.toArray(new String[]{}));

            from =  "T_TER.*,T_DOM.*,"+
                    campos.getAtributo("codTVI")+","+
                    campos.getAtributo("descTVI")+"," +
                    campos.getAtributo("idVIA")+","+
                    campos.getAtributo("codVIA")+","+
                    campos.getAtributo("descVIA")+","+
                    campos.getAtributo("descVIA")+"," +
                    campos.getAtributo("numDesdeDSU")+","+
                    campos.getAtributo("letraDesdeDSU")+","+
                    campos.getAtributo("numHastaDSU")+","+
                    campos.getAtributo("letraHastaDSU")+"," +
                    campos.getAtributo("bloqueDSU")+","+
                    campos.getAtributo("portalDSU")+","+
                    campos.getAtributo("escaleraDPO")+","+
                    campos.getAtributo("plantaDPO")+"," +
                    campos.getAtributo("puertaDPO")+","+
                    campos.getAtributo("codPostalDSU")+","+
                    campos.getAtributo("barriadaDSU")+","+
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA")+ ","+campos.getAtributo("codECO") + "," +
                    campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");

            join = new ArrayList();
            join.add("T_TER");
            join.add("INNER");
            join.add("T_DOT");
            join.add(campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT")+" AND "+
                    campos.getAtributo("tipoDocTER")+"="+terVO.getTipoDocumento()+" AND "+
                    campos.getAtributo("documentoTER")+"="+bd.addString(terVO.getDocumento()) + " AND " +
                    campos.getAtributo("situacionTER")+"='A'");
            join.add("INNER");
            join.add("T_DOM");
            join.add(campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM")+" AND "+
                    campos.getAtributo("normalizadoDOM")+"=1 AND "+
                    campos.getAtributo("situacionDOT")+"='A'");
            join.add("INNER");
            join.add("T_DPO");
            join.add(campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDPO"));
            join.add("INNER");
            join.add("T_DSU");
            join.add(campos.getAtributo("dirSueloDPO")+"="+ campos.getAtributo("codDirSueloDSU"));
            join.add("LEFT");
            join.add("T_TOC");
            join.add(campos.getAtributo("tipoOcupacionDOT")+"="+campos.getAtributo("codTOC"));
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO+"T_MUN T_MUN");
            join.add(campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMUN"));
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO+"T_PAI T_PAI");
            join.add(campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI"));
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO+"T_PRV T_PRV");
            join.add(campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV"));
            join.add("INNER");
            join.add("T_VIA");
            join.add(campos.getAtributo("codViaDSU")+"="+campos.getAtributo("idVIA")+" AND "+
                    campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisVIA")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvVIA")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMunVIA"));
            join.add("INNER");
            join.add("T_TVI");
            join.add(campos.getAtributo("tipoVIA")+"="+campos.getAtributo("codTVI"));
            join.add("LEFT");
            join.add("T_ESI");
            join.add(campos.getAtributo("codigoESI")+"="+campos.getAtributo("codESI"));
            join.add("LEFT");
            join.add("T_ECO");
            join.add(campos.getAtributo("codECOESI")+"="+campos.getAtributo("codECO"));
            join.add("false");

            sql = sql + " UNION " + bd.join(from,null,(String[])join.toArray(new String[]{}));

            if(m_Log.isDebugEnabled()) m_Log.debug("EN getByDocumento");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            int actual;
            int ultimo=-1;
            DomicilioSimpleValueObject nuevo;
            Vector domicilios = null;
            TercerosValueObject terceroActual = null;
            TercerosValueObject terceroUltimo = null;
            while(rs.next()){
                actual=rs.getInt((String)campos.getAtributo("idTerceroTER"));
                if (actual==ultimo){
                    nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                            rs.getString((String)campos.getAtributo("codPAI")),
                            rs.getString((String)campos.getAtributo("codPRV")),
                            rs.getString((String)campos.getAtributo("codMUN")),
                            rs.getString((String)campos.getAtributo("descPAI")),
                            rs.getString((String)campos.getAtributo("descPRV")),
                            rs.getString((String)campos.getAtributo("descMUN")),
                            TransformacionAtributoSelect.replace(rs.getString("DESCDMC"),"\"","\\\""),rs.getString("CPO"));
                    nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                    nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                    nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                    nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                    nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                    if(rs.getString("CODVIA")!=null){
                        nuevo.setDescVia(rs.getString("DESCVIA"));
                        nuevo.setCodigoVia(rs.getString("CODVIA"));
                        nuevo.setIdVia(rs.getString("IDVIA"));
                    }
                    nuevo.setNumDesde(rs.getString("NUD"));
                    nuevo.setLetraDesde(rs.getString("LED"));
                    nuevo.setNumHasta(rs.getString("NUH"));
                    nuevo.setLetraHasta(rs.getString("LEH"));
                    nuevo.setBloque(rs.getString("BLQ"));
                    nuevo.setPortal(rs.getString("POR"));
                    nuevo.setEscalera(rs.getString("ESC"));
                    nuevo.setPlanta(rs.getString("PLT"));
                    nuevo.setPuerta(rs.getString("PTA"));
                    nuevo.setBarriada(rs.getString("CAS"));
                    nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                    nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                    nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                    /* anadir ECOESI */
                    nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                    nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                    nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                    /* fin anadir ECOESI */
                    domicilios.add(nuevo);
                }else{
                    if (terceroUltimo!=null){
                        terceroUltimo.setDomicilios(domicilios);
                        resultado.add(terceroUltimo);
                    }
                    terceroActual = new TercerosValueObject(
                            String.valueOf(actual),
                            rs.getString((String)campos.getAtributo("versionTER")),
                            rs.getString((String)campos.getAtributo("tipoDocTER")),
                            "",
                            rs.getString((String)campos.getAtributo("documentoTER")),
                            rs.getString((String)campos.getAtributo("nombreTER")),
                            rs.getString((String)campos.getAtributo("apellido1TER")),
                            rs.getString((String)campos.getAtributo("pApellido1TER")),
                            rs.getString((String)campos.getAtributo("apellido2TER")),
                            rs.getString((String)campos.getAtributo("pApellido2TER")),
                            rs.getString((String)campos.getAtributo("normalizadoTER")),
                            rs.getString((String)campos.getAtributo("telefonoTER")),
                            rs.getString((String)campos.getAtributo("emailTER")),
                            rs.getString((String)campos.getAtributo("situacionTER")).charAt(0),
                            rs.getString((String)campos.getAtributo("fechaAltaTER")),
                            rs.getString((String)campos.getAtributo("usuarioAltaTER")),
                            rs.getString((String)campos.getAtributo("moduloAltaTER")),
                            rs.getString((String)campos.getAtributo("fechaBajaTER")),
                            rs.getString((String)campos.getAtributo("usuarioBajaTER")));
                    terceroActual.setSituacion(rs.getString((String)campos.getAtributo("situacionTER")).charAt(0));
                    domicilios = new Vector();
                    if (rs.getInt((String)campos.getAtributo("idDomicilioDOM"))>0){
                        nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                                rs.getString((String)campos.getAtributo("codPAI")),
                                rs.getString((String)campos.getAtributo("codPRV")),
                                rs.getString((String)campos.getAtributo("codMUN")),
                                rs.getString((String)campos.getAtributo("descPAI")),
                                rs.getString((String)campos.getAtributo("descPRV")),
                                rs.getString((String)campos.getAtributo("descMUN")),
                                TransformacionAtributoSelect.replace(rs.getString("DESCDMC"),"\"","\\\""),rs.getString("CPO"));
                        nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                        nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                        nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                        nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                        nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                        if(rs.getString("CODVIA")!=null){
                            nuevo.setDescVia(rs.getString("DESCVIA"));
                            nuevo.setCodigoVia(rs.getString("CODVIA"));
                            nuevo.setIdVia(rs.getString("IDVIA"));
                        }
                        nuevo.setNumDesde(rs.getString("NUD"));
                        nuevo.setLetraDesde(rs.getString("LED"));
                        nuevo.setNumHasta(rs.getString("NUH"));
                        nuevo.setLetraHasta(rs.getString("LEH"));
                        nuevo.setBloque(rs.getString("BLQ"));
                        nuevo.setPortal(rs.getString("POR"));
                        nuevo.setEscalera(rs.getString("ESC"));
                        nuevo.setPlanta(rs.getString("PLT"));
                        nuevo.setPuerta(rs.getString("PTA"));
                        nuevo.setBarriada(rs.getString("CAS"));
                        nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                        nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                        nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                        /* anadir ECOESI */
                        nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                        nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                        nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                        /* fin anadir ECOESI */
                        domicilios.add(nuevo);
                    }
                    terceroUltimo=terceroActual;
                    ultimo=actual;
                }
            }
            
            if (terceroUltimo!=null){
                terceroUltimo.setDomicilios(domicilios);
                resultado.add(terceroUltimo);
            }else{
                // No hay domicilios solo devuelvo los datos del tercero
                sql = "SELECT T_TER.* FROM T_TER WHERE " +
                        campos.getAtributo("tipoDocTER")+"="+terVO.getTipoDocumento()+" AND "+
                        campos.getAtributo("documentoTER")+"="+bd.addString(terVO.getDocumento()) + " AND " +
                        campos.getAtributo("situacionTER")+"='A'";
                if(m_Log.isDebugEnabled()) m_Log.debug("No hay domicilios");
                rs = state.executeQuery(sql);
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                TercerosValueObject tercero = null;
                while(rs.next()){
                    tercero = new TercerosValueObject(
                            rs.getString((String)campos.getAtributo("idTerceroTER")),
                            rs.getString((String)campos.getAtributo("versionTER")),
                            rs.getString((String)campos.getAtributo("tipoDocTER")),
                            "",
                            rs.getString((String)campos.getAtributo("documentoTER")),
                            rs.getString((String)campos.getAtributo("nombreTER")),
                            rs.getString((String)campos.getAtributo("apellido1TER")),
                            rs.getString((String)campos.getAtributo("pApellido1TER")),
                            rs.getString((String)campos.getAtributo("apellido2TER")),
                            rs.getString((String)campos.getAtributo("pApellido2TER")),
                            rs.getString((String)campos.getAtributo("normalizadoTER")),
                            rs.getString((String)campos.getAtributo("telefonoTER")),
                            rs.getString((String)campos.getAtributo("emailTER")),
                            rs.getString((String)campos.getAtributo("situacionTER")).charAt(0),
                            rs.getString((String)campos.getAtributo("fechaAltaTER")),
                            rs.getString((String)campos.getAtributo("usuarioAltaTER")),
                            rs.getString((String)campos.getAtributo("moduloAltaTER")),
                            rs.getString((String)campos.getAtributo("fechaBajaTER")),
                            rs.getString((String)campos.getAtributo("usuarioBajaTER")));
                    tercero.setSituacion(rs.getString((String)campos.getAtributo("situacionTER")).charAt(0));
                    tercero.setDomicilios(new Vector());
                    resultado.add(tercero);
                }
            cerrarResultSet(rs);
            cerrarStatement(state);
            }
        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return resultado;
    }

    public Vector getByIdTercero(TercerosValueObject terVO, String[] params)
            throws TechnicalException{
        String sql= "";
        Vector resultado=new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            state = con.createStatement();
            String from = "T_TER.*,T_DOM.*,"+
            campos.getAtributo("descTID")+","+
            campos.getAtributo("codTVI")+" AS CODTIPOVIA,"+
            campos.getAtributo("descTVI")+" AS DESCTIPOVIA," +
            campos.getAtributo("idVIA")+" AS IDVIA,"+
            campos.getAtributo("codVIA")+" AS CODVIA,"+
            campos.getAtributo("descVIA")+" AS DESCVIA,"+
            campos.getAtributo("domicilioDNN")+" AS DESCDMC," +
            campos.getAtributo("numDesdeDNN")+" AS NUD,"+
            campos.getAtributo("letraDesdeDNN")+" AS LED,"+
            campos.getAtributo("numHastaDNN")+" AS NUH,"+
            campos.getAtributo("letraHastaDNN")+" AS LEH," +
            campos.getAtributo("bloqueDNN")+" AS BLQ,"+
            campos.getAtributo("portalDNN")+" AS POR,"+
            campos.getAtributo("escaleraDNN")+" AS ESC,"+
            campos.getAtributo("plantaDNN")+" AS PLT," +
            campos.getAtributo("puertaDNN")+" AS PTA,"+
            campos.getAtributo("codPostalDNN")+" AS CPO,"+
            campos.getAtributo("barriadaDNN")+" AS CAS,"+
            campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
            campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
            campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
            campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
            campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
            campos.getAtributo("codMunVIA")+
            /* anadir ECOESI */
            ","+campos.getAtributo("codECO") + "," + campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");
            /* fin anadir ECOESI */
            String where = campos.getAtributo("idTerceroTER")+"="+terVO.getIdentificador()+" AND "+
            campos.getAtributo("situacionTER")+"='A' AND "+
            campos.getAtributo("normalizadoDOM")+"=2 AND "+
            campos.getAtributo("situacionDOT")+"='A'";
            ArrayList join = new ArrayList();
            join.add("T_TER");
            join.add("INNER");
            join.add("T_DOT");
            join.add(campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT"));
            join.add("INNER");
            join.add("T_TID");
            join.add(campos.getAtributo("codTID")+"="+campos.getAtributo("tipoDocTER"));
            join.add("INNER");
            join.add("T_DOM");
            join.add(campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM"));
            join.add("INNER");
            join.add("T_DNN");
            join.add(campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDNN"));
            join.add("LEFT");
            join.add("T_TOC");
            join.add(campos.getAtributo("tipoOcupacionDOT")+"="+campos.getAtributo("codTOC"));
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO+"T_MUN T_MUN");
            join.add(campos.getAtributo("codPaisDNN")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDNN")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDNN")+"="+campos.getAtributo("codMUN"));
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO+"T_PAI T_PAI");
            join.add(campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI"));
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO+"T_PRV T_PRV");
            join.add(campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV"));
            join.add("LEFT");
            join.add("T_VIA");
            join.add(campos.getAtributo("codViaDNN")+"="+campos.getAtributo("idVIA")+" AND " +
            		campos.getAtributo("codPaisViaDNN")+"="+campos.getAtributo("codPaisVIA") + " AND " +
            		campos.getAtributo("codProvViaDNN") + " = " + campos.getAtributo("codProvVIA") + " AND " +
            		campos.getAtributo("codMunViaDNN") + " = " + campos.getAtributo("codMunVIA"));
            join.add("LEFT");
            join.add("T_TVI");
            join.add(campos.getAtributo("idTipoViaDNN") + " = " + campos.getAtributo("codTVI"));
            join.add("LEFT");
            join.add("T_ESI");
            join.add(campos.getAtributo("codESIDNN") + " = " + campos.getAtributo("codESI"));
            join.add("LEFT");
            join.add("T_ECO");
            join.add(campos.getAtributo("codECOESI") + " = " + campos.getAtributo("codECO"));
            join.add("false");

            String fromDer = "T_TER.*,T_DOM.*,"+
            campos.getAtributo("descTID")+","+
            campos.getAtributo("codTVI")+","+
            campos.getAtributo("descTVI")+"," +
            campos.getAtributo("idVIA")+","+
            campos.getAtributo("codVIA")+","+
            campos.getAtributo("descVIA")+","+
            campos.getAtributo("descVIA")+"," +
            campos.getAtributo("numDesdeDSU")+","+
            campos.getAtributo("letraDesdeDSU")+","+
            campos.getAtributo("numHastaDSU")+","+
            campos.getAtributo("letraHastaDSU")+"," +
            campos.getAtributo("bloqueDSU")+","+
            campos.getAtributo("portalDSU")+","+
            campos.getAtributo("escaleraDPO")+","+
            campos.getAtributo("plantaDPO")+"," +
            campos.getAtributo("puertaDPO")+","+
            campos.getAtributo("codPostalDSU")+","+
            campos.getAtributo("barriadaDSU")+","+
            campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
            campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
            campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
            campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
            campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
            campos.getAtributo("codMunVIA")+","+campos.getAtributo("codECO") + "," +
            campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");

            String whereDer = campos.getAtributo("idTerceroTER")+"="+terVO.getIdentificador()+" AND "+
            				campos.getAtributo("situacionTER")+"='A' AND "+
            				campos.getAtributo("normalizadoDOM")+"=1 AND "+
                            campos.getAtributo("situacionDOT")+"='A'";

            ArrayList joinDer = new ArrayList();
            joinDer.add("T_TER");
            joinDer.add("INNER");
            joinDer.add("T_DOT");
            joinDer.add(campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT"));
            joinDer.add("INNER");
            joinDer.add("T_TID");
            joinDer.add(campos.getAtributo("codTID")+"="+campos.getAtributo("tipoDocTER"));
            joinDer.add("INNER");
            joinDer.add("T_DOM");
            joinDer.add(campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM"));
            joinDer.add("INNER");
            joinDer.add("T_DPO");
            joinDer.add(campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDPO"));
            joinDer.add("INNER");
            joinDer.add("T_DSU");
            joinDer.add(campos.getAtributo("dirSueloDPO")+"="+ campos.getAtributo("codDirSueloDSU"));
            joinDer.add("LEFT");
            joinDer.add("T_TOC");
            joinDer.add(campos.getAtributo("tipoOcupacionDOT")+"="+campos.getAtributo("codTOC"));
            joinDer.add("INNER");
            joinDer.add(GlobalNames.ESQUEMA_GENERICO+"T_MUN T_MUN");
            joinDer.add(campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMUN"));
            joinDer.add("INNER");
            joinDer.add(GlobalNames.ESQUEMA_GENERICO+"T_PAI T_PAI");
            joinDer.add(campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI"));
            joinDer.add("INNER");
            joinDer.add(GlobalNames.ESQUEMA_GENERICO+"T_PRV T_PRV");
            joinDer.add(campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV"));
            joinDer.add("INNER");
            joinDer.add("T_VIA");
            joinDer.add(campos.getAtributo("codViaDSU")+"="+campos.getAtributo("idVIA")+" AND " +
            		campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisVIA") + " AND " +
            		campos.getAtributo("codProvDSU") + " = " + campos.getAtributo("codProvVIA") + " AND " +
            		campos.getAtributo("codMunDSU") + " = " + campos.getAtributo("codMunVIA"));
            joinDer.add("INNER");
            joinDer.add("T_TVI");
            joinDer.add(campos.getAtributo("tipoVIA") + " = " + campos.getAtributo("codTVI"));
            joinDer.add("LEFT");
            joinDer.add("T_ESI");
            joinDer.add(campos.getAtributo("codigoESI") + " = " + campos.getAtributo("codESI"));
            joinDer.add("LEFT");
            joinDer.add("T_ECO");
            joinDer.add(campos.getAtributo("codECOESI") + " = " + campos.getAtributo("codECO"));
            joinDer.add("false");

            sql = bd.join(from, where, (String[])join.toArray(new String[] {}));
            String sqlDer = bd.join(fromDer, whereDer, (String[])joinDer.toArray(new String[] {}));
            sql = sql + " UNION " + sqlDer;
            if(m_Log.isDebugEnabled()) m_Log.debug("EN getByIdTercero");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            int actual;
            int ultimo=-1;
            DomicilioSimpleValueObject nuevo;
            Vector domicilios = null;
            TercerosValueObject terceroActual = null;
            TercerosValueObject terceroUltimo = null;
            while(rs.next()){
                actual=rs.getInt((String)campos.getAtributo("idTerceroTER"));
                if (actual==ultimo){
                    nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                            rs.getString((String)campos.getAtributo("codPAI")),
                            rs.getString((String)campos.getAtributo("codPRV")),
                            rs.getString((String)campos.getAtributo("codMUN")),
                            rs.getString((String)campos.getAtributo("descPAI")),
                            rs.getString((String)campos.getAtributo("descPRV")),
                            rs.getString((String)campos.getAtributo("descMUN")),
                            TransformacionAtributoSelect.replace(rs.getString("DESCDMC"),"\"","\\\""),rs.getString("CPO"));
                    nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                    nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                    nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                    nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                    nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                    if(rs.getString("CODVIA")!=null){
                        nuevo.setDescVia(rs.getString("DESCVIA"));
                        nuevo.setCodigoVia(rs.getString("CODVIA"));
                        nuevo.setIdVia(rs.getString("IDVIA"));
                    }
                    nuevo.setNumDesde(rs.getString("NUD"));
                    nuevo.setLetraDesde(rs.getString("LED"));
                    nuevo.setNumHasta(rs.getString("NUH"));
                    nuevo.setLetraHasta(rs.getString("LEH"));
                    nuevo.setBloque(rs.getString("BLQ"));
                    nuevo.setPortal(rs.getString("POR"));
                    nuevo.setEscalera(rs.getString("ESC"));
                    nuevo.setPlanta(rs.getString("PLT"));
                    nuevo.setPuerta(rs.getString("PTA"));
                    nuevo.setBarriada(rs.getString("CAS"));
                    nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                    nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                    nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                    /* anadir ECOESI */
                    nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                    nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                    nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                    /* fin anadir ECOESI */
                    domicilios.add(nuevo);
                }else{
                    if (terceroUltimo!=null){
                        terceroUltimo.setDomicilios(domicilios);
                        resultado.add(terceroUltimo);
                    }
                    terceroActual = new TercerosValueObject(
                            String.valueOf(actual),
                            rs.getString((String)campos.getAtributo("versionTER")),
                            rs.getString((String)campos.getAtributo("tipoDocTER")),
                            rs.getString((String)campos.getAtributo("descTID")),
                            rs.getString((String)campos.getAtributo("documentoTER")),
                            rs.getString((String)campos.getAtributo("nombreTER")),
                            rs.getString((String)campos.getAtributo("apellido1TER")),
                            rs.getString((String)campos.getAtributo("pApellido1TER")),
                            rs.getString((String)campos.getAtributo("apellido2TER")),
                            rs.getString((String)campos.getAtributo("pApellido2TER")),
                            rs.getString((String)campos.getAtributo("normalizadoTER")),
                            rs.getString((String)campos.getAtributo("telefonoTER")),
                            rs.getString((String)campos.getAtributo("emailTER")),
                            rs.getString((String)campos.getAtributo("situacionTER")).charAt(0),
                            rs.getString((String)campos.getAtributo("fechaAltaTER")),
                            rs.getString((String)campos.getAtributo("usuarioAltaTER")),
                            rs.getString((String)campos.getAtributo("moduloAltaTER")),
                            rs.getString((String)campos.getAtributo("fechaBajaTER")),
                            rs.getString((String)campos.getAtributo("usuarioBajaTER")));
                    terceroActual.setSituacion(rs.getString((String)campos.getAtributo("situacionTER")).charAt(0));
                    terceroActual.setDomPrincipal(rs.getString("TER_DOM"));
                    domicilios = new Vector();
                    if (rs.getInt((String)campos.getAtributo("idDomicilioDOM"))>0){
                        nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                                rs.getString((String)campos.getAtributo("codPAI")),
                                rs.getString((String)campos.getAtributo("codPRV")),
                                rs.getString((String)campos.getAtributo("codMUN")),
                                rs.getString((String)campos.getAtributo("descPAI")),
                                rs.getString((String)campos.getAtributo("descPRV")),
                                rs.getString((String)campos.getAtributo("descMUN")),
                                TransformacionAtributoSelect.replace(rs.getString("DESCDMC"),"\"","\\\""),rs.getString("CPO"));
                        nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                        nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                        nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                        nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                        nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                        if(rs.getString("CODVIA")!=null){
                            nuevo.setDescVia(rs.getString("DESCVIA"));
                            nuevo.setCodigoVia(rs.getString("CODVIA"));
                            nuevo.setIdVia(rs.getString("IDVIA"));
                        }
                        nuevo.setNumDesde(rs.getString("NUD"));
                        nuevo.setLetraDesde(rs.getString("LED"));
                        nuevo.setNumHasta(rs.getString("NUH"));
                        nuevo.setLetraHasta(rs.getString("LEH"));
                        nuevo.setBloque(rs.getString("BLQ"));
                        nuevo.setPortal(rs.getString("POR"));
                        nuevo.setEscalera(rs.getString("ESC"));
                        nuevo.setPlanta(rs.getString("PLT"));
                        nuevo.setPuerta(rs.getString("PTA"));
                        nuevo.setBarriada(rs.getString("CAS"));
                        nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                        nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                        nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                        /* anadir ECOESI */
                        nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                        nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                        nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                        /* fin anadir ECOESI */
                        domicilios.add(nuevo);
                    }
                    terceroUltimo=terceroActual;
                    ultimo=actual;
                }
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            if (terceroUltimo!=null){
                terceroUltimo.setDomicilios(domicilios);
                resultado.add(terceroUltimo);
            }else{
                // No hay domicilios solo devuelvo los datos del tercero
                sql = "SELECT T_TER.*,TID_DES FROM T_TER,T_TID WHERE " +
                        campos.getAtributo("idTerceroTER")+"="+terVO.getIdentificador()+" AND "+
                        campos.getAtributo("situacionTER")+"='A'"+" AND "+campos.getAtributo("codTID")+"="+campos.getAtributo("tipoDocTER");
                if(m_Log.isDebugEnabled()) m_Log.debug("No hay domicilios");
                state = con.createStatement();
                rs = state.executeQuery(sql);
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                TercerosValueObject tercero = null;
                while(rs.next()){
                    tercero = new TercerosValueObject(
                            rs.getString((String)campos.getAtributo("idTerceroTER")),
                            rs.getString((String)campos.getAtributo("versionTER")),
                            rs.getString((String)campos.getAtributo("tipoDocTER")),
                            rs.getString((String)campos.getAtributo("descTID")),
                            rs.getString((String)campos.getAtributo("documentoTER")),
                            rs.getString((String)campos.getAtributo("nombreTER")),
                            rs.getString((String)campos.getAtributo("apellido1TER")),
                            rs.getString((String)campos.getAtributo("pApellido1TER")),
                            rs.getString((String)campos.getAtributo("apellido2TER")),
                            rs.getString((String)campos.getAtributo("pApellido2TER")),
                            rs.getString((String)campos.getAtributo("normalizadoTER")),
                            rs.getString((String)campos.getAtributo("telefonoTER")),
                            rs.getString((String)campos.getAtributo("emailTER")),
                            rs.getString((String)campos.getAtributo("situacionTER")).charAt(0),
                            rs.getString((String)campos.getAtributo("fechaAltaTER")),
                            rs.getString((String)campos.getAtributo("usuarioAltaTER")),
                            rs.getString((String)campos.getAtributo("moduloAltaTER")),
                            rs.getString((String)campos.getAtributo("fechaBajaTER")),
                            rs.getString((String)campos.getAtributo("usuarioBajaTER")));
                    tercero.setSituacion(rs.getString((String)campos.getAtributo("situacionTER")).charAt(0));
                    tercero.setDomicilios(new Vector());
                    resultado.add(tercero);
                }
                cerrarResultSet(rs);
                cerrarStatement(state);
            }
        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return resultado;
    }

    /**
     * Unica funcion de este DAO que devuelve un TercerosValueObject,
     * con los datos que aparecen en la ventana de mantenimiento de terceros.
     * @param codigo tercero
     * @param params
     * @return TercerosValueObject
     * @throws TechnicalException
     */
    public TercerosValueObject getDatosTercero(String idTercero, String[] params)
        throws TechnicalException{

    	AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs = null;
        PreparedStatement state = null;
        TercerosValueObject terVO = new TercerosValueObject();
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();

            String sql = "SELECT " +  campos.getAtributo("versionTER") + "," + campos.getAtributo("tipoDocTER") + ","
                                   +  campos.getAtributo("documentoTER") + "," + campos.getAtributo("nombreTER") + ","
                                   +  campos.getAtributo("apellido1TER") + "," + campos.getAtributo("apellido2TER") + ","
                                   +  campos.getAtributo("pApellido1TER") + "," + campos.getAtributo("pApellido2TER") + ","
                                   +  campos.getAtributo("telefonoTER") + "," + campos.getAtributo("emailTER") + ","
                                   +  campos.getAtributo("normalizadoTER") + "," + campos.getAtributo("situacionTER") + ","
                                   +  campos.getAtributo("fechaAltaTER") + "," + campos.getAtributo("usuarioAltaTER") + ","
                                   +  campos.getAtributo("moduloAltaTER") + "," + campos.getAtributo("fechaBajaTER") + ","
                                   +  campos.getAtributo("usuarioBajaTER")
                  + " FROM T_TER WHERE " + campos.getAtributo("idTerceroTER") + "=?";

            state = con.prepareStatement(sql);
            int i = 1;
            state.setInt(i++, Integer.parseInt(idTercero));
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery();

            terVO.setIdentificador(idTercero);
            if (rs.next()) {
                i = 1;
                terVO.setVersion(rs.getString(i++));
                terVO.setTipoDocumento(rs.getString(i++));
                terVO.setDocumento(rs.getString(i++));
                terVO.setNombre(rs.getString(i++));
                terVO.setApellido1(rs.getString(i++));
                terVO.setApellido2(rs.getString(i++));
                terVO.setPartApellido1(rs.getString(i++));
                terVO.setPartApellido2(rs.getString(i++));
                terVO.setTelefono(rs.getString(i++));
                terVO.setEmail(rs.getString(i++));
                terVO.setNormalizado(rs.getString(i++));
                terVO.setSituacion(rs.getString(i++).charAt(0));
                terVO.setFechaAlta(rs.getString(i++));
                terVO.setUsuarioAlta(rs.getString(i++));
                terVO.setModuloAlta(rs.getString(i++));
                terVO.setFechaBaja(rs.getString(i++));
                terVO.setUsuarioBaja(rs.getString(i++));
            }
                cerrarResultSet(rs);
                cerrarStatement(state);

            }catch (Exception e){
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }finally{
                cerrarResultSet(rs);
                cerrarStatement(state);
                devolverConexion(bd, con);
            }
        return terVO;
    }
    
    public Boolean esTerceroFormatoNifValido (String codTercero,String[] params) throws TechnicalException {
        
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs = null;
        PreparedStatement state = null;
        Boolean valido = Boolean.FALSE;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();

            String sql = "SELECT COUNT(*) AS NUM FROM T_TER WHERE TER_COD = ? AND TER_AP1 IS NULL";

            state = con.prepareStatement(sql);
            int i = 1;
            state.setInt(i++, Integer.parseInt(codTercero));
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery();
            rs.next();
            valido = rs.getInt("NUM") > 0 ? Boolean.FALSE : Boolean.TRUE; 
            cerrarResultSet(rs);
            cerrarStatement(state);

            }catch (Exception e){
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }finally{
                cerrarResultSet(rs);
                cerrarStatement(state);
                devolverConexion(bd, con);
            }   
        
        return valido;
    }


    public void updateMayusculasTercero( String[] params) throws TechnicalException{
        String query;
        query= "UPDATE T_TER SET TER_AP1=UPPER(TER_AP1), TER_AP2=UPPER(TER_AP2), TER_NOM=UPPER(TER_NOM) ";
        AdaptadorSQLBD bd = null;
        Connection con=null;
        Statement state= null;
        TransformacionAtributoSelect transformador=null;

        try{
            bd = new AdaptadorSQLBD(params);
            transformador = new TransformacionAtributoSelect(bd);
            con = bd.getConnection();
            state = con.createStatement();
            state.executeUpdate(query);
            cerrarStatement(state);
            commitTransaction(bd, con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            rollBackTransaction(bd, con, e);
        }finally{
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
    }



    public Vector getTercero(CondicionesBusquedaTerceroVO condsBusqueda, String[] params) throws TechnicalException,DemasiadosResultadosBusquedaTerceroException{
        String sql;
        Vector<TercerosValueObject> resultado = new Vector<TercerosValueObject>();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs =null;
        Statement state = null;
        boolean superadoLimite=false;
        TransformacionAtributoSelect transformador;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            transformador = new TransformacionAtributoSelect(bd,con);
            int tipoDoc = condsBusqueda.getTipoDocumento();
            String docu = condsBusqueda.getDocumento();
            String name = condsBusqueda.getNombre();
            String apell1 = condsBusqueda.getApellido1();
            String apell2 = condsBusqueda.getApellido2();
            String telefono = condsBusqueda.getTelefono();
            String email = condsBusqueda.getEmail();
            String codPais = (String)campos.getAtributo("codigoPais");
            int codProvincia = condsBusqueda.getCodigoProvincia();
            int codMunicipio = condsBusqueda.getCodigoMunicipio();
            if (codProvincia == -1 && codMunicipio == -1) codPais = "";
            int codVia = condsBusqueda.getCodigoVia();
            String descVia = condsBusqueda.getNombreVia();
            int numDesde = condsBusqueda.getNumeroDesde();
            String letraDesde = condsBusqueda.getLetraDesde();
            int numHasta = condsBusqueda.getNumeroHasta();
            String letraHasta = condsBusqueda.getLetraHasta();
            String bloque = condsBusqueda.getBloque();
            String portal = condsBusqueda.getPortal();
            String escalera = condsBusqueda.getEscalera();
            String planta = condsBusqueda.getPlanta();
            String puerta = condsBusqueda.getPuerta();
            String codPostal = condsBusqueda.getCodPostal();
            String domicilio = condsBusqueda.getDomicilio();
            String lugar = condsBusqueda.getLugar();
            int codEsi = condsBusqueda.getCodigoEsi();
            int codEco = condsBusqueda.getCodigoEco();


            // PRIMERA PARTE DE LA UNION (DOMICILIOS NO NORMALIZADOS)
            String from = "TER_COD,TER_TID,TER_DOC,TER_NOM,TER_AP1,TER_PA1,TER_AP2,TER_PA2,TER_NOC,TER_NML,TER_TLF,TER_DCE,TER_SIT,TER_NVE,TER_FAL,TER_UAL,TER_APL,TER_FBJ,TER_UBJ,TER_DOM,T_DOM.*," +
                    campos.getAtributo("codTVI")+" AS CODTIPOVIA,"+
                    campos.getAtributo("descTVI")+" AS DESCTIPOVIA," +
                    campos.getAtributo("descESI") +" AS LUGAR," +
                    campos.getAtributo("idVIA")+" AS IDVIA,"+
                    campos.getAtributo("codVIA")+" AS CODVIA,"+
                    campos.getAtributo("descVIA")+" AS DESCVIA,"+
                    campos.getAtributo("domicilioDNN")+" AS DESCDMC," +
                    campos.getAtributo("numDesdeDNN")+" AS NUD,"+
                    campos.getAtributo("letraDesdeDNN")+" AS LED,"+
                    campos.getAtributo("numHastaDNN")+" AS NUH,"+
                    campos.getAtributo("letraHastaDNN")+" AS LEH," +
                    campos.getAtributo("bloqueDNN")+" AS BLQ,"+
                    campos.getAtributo("portalDNN")+" AS POR,"+
                    campos.getAtributo("escaleraDNN")+" AS ESC,"+
                    campos.getAtributo("plantaDNN")+" AS PLT," +
                    campos.getAtributo("puertaDNN")+" AS PTA,"+
                    campos.getAtributo("codPostalDNN")+" AS CPO,"+
                    campos.getAtributo("barriadaDNN")+" AS CAS,"+
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA")+","+campos.getAtributo("codECO") + "," +
                    campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");

            String where = "";
            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    where += condicion +" AND ";
                }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        where += condicion + " AND ";
                    }*/
                     where += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }
            if (name != null) {
                if (!"".equals(name.trim())) {
                   
                   String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                   String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                where += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                where += "TER_DCE = '" + email + "' AND ";
            }

            String[] join = new String[38];

            join[0] = "T_TER";
            join[1] = "INNER";
            join[2] = "T_TID";
            join[3] = campos.getAtributo("tipoDocTER")+"="+campos.getAtributo("codTID");
            join[4] = "INNER";
            join[5] = "T_DOT";
            join[6] = campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT");
            join[7] = "INNER";
            join[8] = "T_DOM";
            join[9] = campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM");
            join[10] = "INNER";
            join[11] = "T_DNN";
            join[12] = campos.getAtributo("idDomicilioDOM")+"="+campos.getAtributo("idDomicilioDNN");
            join[13] = "LEFT";
            join[14] = "T_TOC";
            join[15] = campos.getAtributo("tipoOcupacionDOT")+"="+campos.getAtributo("codTOC");

            where = (codPais.equals("")) ? where
                    :where+campos.getAtributo("codPaisDNN")+"="+codPais+ " AND ";
            where = (codProvincia == -1) ? where
                    :where+campos.getAtributo("codProvDNN")+"="+codProvincia+ " AND ";
            where = (codMunicipio == -1) ? where
                    :where+campos.getAtributo("codMunDNN")+"="+codMunicipio+ " AND ";
            where = (numDesde == -1) ? where
                    :where+campos.getAtributo("numDesdeDNN")+"="+numDesde+ " AND ";
            where = (letraDesde == null || "".equals(letraDesde)) ? where
                    :where+campos.getAtributo("letraDesdeDNN")+"="+bd.addString(letraDesde)+" AND ";
            where = (numHasta == -1) ? where
                    :where+campos.getAtributo("numHastaDNN")+"="+numHasta+ " AND ";
            where = (letraDesde == null || "".equals(letraHasta)) ? where
                    :where+campos.getAtributo("letraHastaDNN")+"="+bd.addString(letraHasta)+" AND ";

            if (bloque != null && !bloque.equals("")) {
                where += "DNN_BLQ = '" + bloque + "' AND ";
            }

            if (portal != null && !portal.equals("")) {
                where += "DNN_POR = '" + portal + "' AND ";
            }

            if (escalera != null && !escalera.equals("")) {
                where += "DNN_ESC = '" + escalera + "' AND ";
            }

            if (planta != null && !planta.equals("")) {
                where += "DNN_PLT = '" + planta + "' AND ";
            }

            if (puerta != null && !puerta.equals("")) {
                where += "DNN_PTA = '" + puerta + "' AND ";
            }

            if (codPostal != null && !codPostal.equals("")) {
                where += "DNN_CPO = '" + codPostal + "' AND ";
            }

            if (domicilio != null && !domicilio.equals("")) {
                where += "DNN_DMC = '" + domicilio + "' AND ";
            }

            if (lugar != null && !lugar.equals("")) {
                where += "DNN_LUG = '" + lugar + "' AND ";
            }

            join[16] = "INNER";
            join[17] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
            join[18] = campos.getAtributo("codPaisDNN")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDNN")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDNN")+"="+campos.getAtributo("codMUN");
            join[19] = "INNER";
            join[20] = GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI";
            join[21] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI");
            join[22] = "INNER";
            join[23] = GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV";
            join[24] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV");
            join[25] = "LEFT";
            join[26] = "T_VIA";
            join[27] = campos.getAtributo("codViaDNN")+"="+campos.getAtributo("idVIA")+" AND "+
                    campos.getAtributo("codPaisViaDNN")+"="+campos.getAtributo("codPaisVIA")+" AND "+
                    campos.getAtributo("codProvViaDNN")+"="+campos.getAtributo("codProvVIA")+" AND "+
                    campos.getAtributo("codMunViaDNN")+"="+campos.getAtributo("codMunVIA");

            where = (codVia == -1) ? where:where+campos.getAtributo("codVIA")+"="+codVia+ " AND ";
            where = (descVia == null || "".equals(descVia)) ? where:where+campos.getAtributo("descVIA")+" LIKE '%"+TransformacionAtributoSelect.replace(descVia,"'","''")+ "%' AND ";
            where = (codEco == -1) ? where:where+campos.getAtributo("codECO")+"="+codEco+ " AND ";
            where = (codEsi == -1) ? where:where+campos.getAtributo("codESI")+"="+codEsi+ " AND ";

            join[28] = "LEFT";
            join[29] = "T_TVI";
            join[30] = campos.getAtributo("idTipoViaDNN")+"="+campos.getAtributo("codTVI");
            join[31] = "LEFT";
            join[32] = "T_ESI";
            join[33] = campos.getAtributo("codESIDNN")+"="+campos.getAtributo("codESI");
            join[34] = "LEFT";
            join[35] = "T_ECO";
            join[36] = campos.getAtributo("codECOESI")+"="+campos.getAtributo("codECO");
            join[37] = "false";

            where += campos.getAtributo("situacionTER")+"='A' AND "+
                    campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("normalizadoDOM")+"=2 ";

            sql = bd.join(from,where,join);

            // SEGUNDA PARTE DE LA UNION (DOMICILIOS NORMALIZADOS)
            join = new String[41];

            from =  "TER_COD,TER_TID,TER_DOC,TER_NOM,TER_AP1,TER_PA1,TER_AP2,TER_PA2,TER_NOC,TER_NML,TER_TLF,TER_DCE,TER_SIT,TER_NVE,TER_FAL,TER_UAL,TER_APL,TER_FBJ,TER_UBJ,TER_DOM,T_DOM.*,"+
                    campos.getAtributo("codTVI")+","+
                    campos.getAtributo("descTVI")+"," +
                    campos.getAtributo("descESI")+"," +
                    campos.getAtributo("idVIA")+","+
                    campos.getAtributo("codVIA")+","+
                    campos.getAtributo("descVIA")+","+
                    campos.getAtributo("descVIA")+"," +
                    campos.getAtributo("numDesdeDSU")+","+
                    campos.getAtributo("letraDesdeDSU")+","+
                    campos.getAtributo("numHastaDSU")+","+
                    campos.getAtributo("letraHastaDSU")+"," +
                    campos.getAtributo("bloqueDSU")+","+
                    campos.getAtributo("portalDSU")+","+
                    campos.getAtributo("escaleraDPO")+","+
                    campos.getAtributo("plantaDPO")+"," +
                    campos.getAtributo("puertaDPO")+","+
                    campos.getAtributo("codPostalDSU")+","+
                    "("+  bd.funcionCadena(bd.FUNCIONCADENA_CONCAT,new String[]{(String) campos.getAtributo("descESI"),"' - '",(String) campos.getAtributo("descECO")}) +"), " +
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA")+
                    ","+campos.getAtributo("codECO") + "," + campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");

            where = "";

            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    where += condicion +" AND ";
                }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        where += condicion + " AND ";
                    }*/
                     where += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }

            if (name != null) {
                if (!"".equals(name.trim())) {
                    String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                    String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                where += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                where += "TER_DCE = '" + email + "' AND ";
            }

            where+= campos.getAtributo("situacionTER")+"='A' AND "+
                    campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("normalizadoDOM")+"=1 AND ";

            join[0] = "T_TER";
            join[1] = "INNER";
            join[2] = "T_TID";
            join[3] = campos.getAtributo("tipoDocTER")+"="+campos.getAtributo("codTID");
            join[4] = "INNER";
            join[5] = "T_DOT";
            join[6] = campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT");
            join[7] = "INNER";
            join[8] = "T_DOM";
            join[9] = campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM");
            join[10] = "INNER";
            join[11] = "T_DPO";
            join[12] = campos.getAtributo("idDomicilioDOM")+"="+campos.getAtributo("idDomicilioDPO");
            join[13] = "INNER";
            join[14] = "T_DSU";
            join[15] = campos.getAtributo("dirSueloDPO")+"="+campos.getAtributo("codDirSueloDSU");
            join[16] = "LEFT";
            join[17] = "T_TOC";
            join[18] = campos.getAtributo("tipoOcupacionDOT") + " = " + campos.getAtributo("codTOC");

            where = (codPais.equals("")) ? where
                    :where+campos.getAtributo("codPaisDSU")+"="+codPais+ " AND ";
            where = (codProvincia == -1) ? where
                    :where+campos.getAtributo("codProvDSU")+"="+codProvincia+ " AND ";
            where = (codMunicipio == -1) ? where
                    :where+campos.getAtributo("codMunDSU")+"="+codMunicipio+ " AND ";
            where = (numDesde == -1) ? where
                    :where+campos.getAtributo("numDesdeDSU")+"="+numDesde+ " AND ";
            where = (letraDesde ==  null || "".equals(letraDesde)) ? where
                    :where+campos.getAtributo("letraDesdeDSU")+"="+bd.addString(letraDesde)+" AND ";
            where = (numHasta == -1) ? where
                    :where+campos.getAtributo("numHastaDSU")+"="+numHasta+ " AND ";
            where = (letraHasta == null || "".equals(letraHasta)) ? where
                    :where+campos.getAtributo("letraHastaDSU")+"="+bd.addString(letraHasta)+" AND ";

            if (bloque != null && !bloque.equals("")) {
                where += "DSU_BLQ = '" + bloque + "' AND ";
            }

            if (portal != null && !portal.equals("")) {
                where += "DSU_POR = '" + portal + "' AND ";
            }

            if (lugar != null && !lugar.equals("")) {
                where += "DSU_LUG = '" + lugar + "' AND ";
            }

            if (codPostal != null && !codPostal.equals("")) {
                where += "DSU_CPO = '" + codPostal + "' AND ";
            }

            join[19] = "INNER";
            join[20] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
            join[21] = campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMUN");
            join[22] = "INNER";
            join[23] = GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI";
            join[24] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI");
            join[25] = "INNER";
            join[26] = GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV";
            join[27] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV");
            join[28] = "INNER";
            join[29] = "T_VIA";
            join[30] = campos.getAtributo("codViaDSU")+"="+campos.getAtributo("idVIA")+" AND "+
                    campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisVIA")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvVIA")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMunVIA");
            where = (codVia == -1) ? where:where+campos.getAtributo("codVIA")+"="+codVia+ " AND ";
            where = (descVia == null || "".equals(descVia)) ? where:where+campos.getAtributo("descVIA")+" LIKE '%"+TransformacionAtributoSelect.replace(descVia,"'","''")+ "%' AND ";

            where = (codEco == -1) ? where: where+campos.getAtributo("codECO")+"="+codEco+ " AND ";
            where = (codEsi == -1) ? where: where+campos.getAtributo("codESI")+"="+codEsi+ " AND ";

            where += campos.getAtributo("situacionDSU")+"='A'";
            join[31] = "INNER";
            join[32] = "T_TVI";
            join[33] = campos.getAtributo("tipoVIA")+"="+campos.getAtributo("codTVI");
            join[34] = "LEFT";
            join[35] = "T_ESI";
            join[36] = campos.getAtributo("codigoESI")+"="+campos.getAtributo("codESI");
            join[37] = "LEFT";
            join[38] = "T_ECO";
            join[39] = campos.getAtributo("codECOESI")+"="+campos.getAtributo("codECO");
            join[40] =  "false";
            sql = sql + " UNION " + bd.join(from,where,join);
       

            
            //Limitamos la consulta
            ResourceBundle config = ResourceBundle.getBundle("Terceros");
            String S_NUM_TERCEROS_MAXIMO_CONSULTA="";
            int  NUM_TERCEROS_MAXIMO_CONSULTA=1000;
            
            try{
                
                 S_NUM_TERCEROS_MAXIMO_CONSULTA =  config.getString(condsBusqueda.getCodOrganizacion()+ "/limiteResultadosFlexia");
                 NUM_TERCEROS_MAXIMO_CONSULTA = Integer.parseInt(S_NUM_TERCEROS_MAXIMO_CONSULTA);
            }catch (Exception e)
            {
                NUM_TERCEROS_MAXIMO_CONSULTA=1000;
            }
            
            
            
            if (params[0] != null && (params[0].equals("oracle") || params[0].equals("ORACLE"))) {

                sql = sql + " ORDER BY TER_AP1, TER_COD";

                sql = "SELECT * FROM (" + sql + ") WHERE ROWNUM<=" + NUM_TERCEROS_MAXIMO_CONSULTA;

            } else {

                sql = "SELECT top " + NUM_TERCEROS_MAXIMO_CONSULTA + " * FROM (" + sql + ") consulta ORDER BY consulta.TER_AP1, consulta.TER_COD";
            }
            
                        if(m_Log.isDebugEnabled()) m_Log.debug("CONSULTA DE BUSQUEDA DE TERCEROS"+sql);

            long tiempoInicio = System.currentTimeMillis();

            state = con.createStatement();
            
            
            if(consultaInsensitiva){
                String alter1=null;
                String alter2=null;
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    m_Log.debug("ALTER SESSION SET LINGUISTIC/GENERICBASELETTER");

                    state.executeQuery(alter1);
                    state.executeQuery(alter2);

                } 
            }
            
            rs = state.executeQuery(sql);          
            long totalTiempo = System.currentTimeMillis() - tiempoInicio;
m_Log.debug("El tiempo de demora es :" + totalTiempo + " miliseg");
            int actual;
            int ultimo=-1;
            DomicilioSimpleValueObject nuevo;
            Vector<DomicilioSimpleValueObject> domicilios = null;
            TercerosValueObject terceroActual;
            TercerosValueObject terceroUltimo = null;

            DatosSuplementariosTerceroDAO datosSupDAO = DatosSuplementariosTerceroDAO.getInstance();
            
            // OBTENER RESULTADOS DE LA CONSULTA
            int contador=0;
          
            
            
            while(rs.next()){
                contador++;
                if(contador>=NUM_TERCEROS_MAXIMO_CONSULTA){
                      
                          superadoLimite=true;                           
                }   
                
                actual=rs.getInt((String)campos.getAtributo("idTerceroTER"));
                if (actual==ultimo){
                    String descdmc = rs.getString("DESCDMC");
                    if (descdmc== null) descdmc ="";
                    nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                            rs.getString((String)campos.getAtributo("codPAI")),
                            rs.getString((String)campos.getAtributo("codPRV")),
                            rs.getString((String)campos.getAtributo("codMUN")),
                            rs.getString((String)campos.getAtributo("descPAI")),
                            rs.getString((String)campos.getAtributo("descPRV")),
                            rs.getString((String)campos.getAtributo("descMUN")),
                            TransformacionAtributoSelect.replace(descdmc,"\"","\\\""),rs.getString("CPO"));
                    nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                    nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                    nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                    nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                    nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                    if(rs.getString("CODVIA")!=null){
                        nuevo.setDescVia(rs.getString("DESCVIA"));
                        nuevo.setCodigoVia(rs.getString("CODVIA"));
                        nuevo.setIdVia(rs.getString("IDVIA"));
                        if(rs.getString("CODVIA").equals("0")) {
                        }
                    }
                    nuevo.setNumDesde(rs.getString("NUD"));
                    nuevo.setLetraDesde(rs.getString("LED"));
                    nuevo.setNumHasta(rs.getString("NUH"));
                    nuevo.setLetraHasta(rs.getString("LEH"));
                    nuevo.setBloque(rs.getString("BLQ"));
                    nuevo.setPortal(rs.getString("POR"));
                    nuevo.setEscalera(rs.getString("ESC"));
                    nuevo.setPlanta(rs.getString("PLT"));
                    nuevo.setPuerta(rs.getString("PTA"));
                    nuevo.setBarriada(rs.getString("CAS"));
                    nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                    nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                    nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                    /* anadir ECOESI */
                    nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                    nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                    nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                    /* fin anadir ECOESI */
                    domicilios.add(nuevo);
                }else{
                    if (terceroUltimo!=null){
                        terceroUltimo.setDomicilios(domicilios);
                        resultado.add(terceroUltimo);
                    }
                    terceroActual = new TercerosValueObject(
                            String.valueOf(actual),
                            rs.getString((String)campos.getAtributo("versionTER")),
                            rs.getString((String)campos.getAtributo("tipoDocTER")),
                            "",
                            rs.getString((String)campos.getAtributo("documentoTER")),
                            rs.getString((String)campos.getAtributo("nombreTER")),
                            rs.getString((String)campos.getAtributo("apellido1TER")),
                            rs.getString((String)campos.getAtributo("pApellido1TER")),
                            rs.getString((String)campos.getAtributo("apellido2TER")),
                            rs.getString((String)campos.getAtributo("pApellido2TER")),
                            rs.getString((String)campos.getAtributo("normalizadoTER")),
                            rs.getString((String)campos.getAtributo("telefonoTER")),
                            rs.getString((String)campos.getAtributo("emailTER")),
                            rs.getString((String)campos.getAtributo("situacionTER")).charAt(0),
                            rs.getString((String)campos.getAtributo("fechaAltaTER")),
                            rs.getString((String)campos.getAtributo("usuarioAltaTER")),
                            rs.getString((String)campos.getAtributo("moduloAltaTER")),
                            rs.getString((String)campos.getAtributo("fechaBajaTER")),
                            rs.getString((String)campos.getAtributo("usuarioBajaTER")));

                    terceroActual.setSituacion(rs.getString((String)campos.getAtributo("situacionTER")).charAt(0));
                    terceroActual.setDomPrincipal(rs.getString("TER_DOM"));
                    domicilios = new Vector<DomicilioSimpleValueObject>();
                    if (rs.getInt((String)campos.getAtributo("idDomicilioDOM"))>0){
                        nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                                rs.getString((String)campos.getAtributo("codPAI")),
                                rs.getString((String)campos.getAtributo("codPRV")),
                                rs.getString((String)campos.getAtributo("codMUN")),
                                rs.getString((String)campos.getAtributo("descPAI")),
                                rs.getString((String)campos.getAtributo("descPRV")),
                                rs.getString((String)campos.getAtributo("descMUN")),
                                TransformacionAtributoSelect.replace(rs.getString("DESCDMC"),"\"","\\\""),rs.getString("CPO"));
                        nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                        nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                        nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                        nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                        nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                        if(rs.getString("CODVIA")!=null){
                            nuevo.setDescVia(rs.getString("DESCVIA"));
                            nuevo.setCodigoVia(rs.getString("CODVIA"));
                            nuevo.setIdVia(rs.getString("IDVIA"));
                            if(rs.getString("CODVIA").equals("0")) {
                            }
                        }
                        nuevo.setNumDesde(rs.getString("NUD"));
                        nuevo.setLetraDesde(rs.getString("LED"));
                        nuevo.setNumHasta(rs.getString("NUH"));
                        nuevo.setLetraHasta(rs.getString("LEH"));
                        nuevo.setBloque(rs.getString("BLQ"));
                        nuevo.setPortal(rs.getString("POR"));
                        nuevo.setEscalera(rs.getString("ESC"));
                        nuevo.setPlanta(rs.getString("PLT"));
                        nuevo.setPuerta(rs.getString("PTA"));
                        nuevo.setBarriada(rs.getString("CAS"));
                        nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                        nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                        nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                        /* anadir ECOESI */
                        nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                        nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                        nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                        /* fin anadir ECOESI */

                        domicilios.add(nuevo);
                    }
                    terceroUltimo=terceroActual;
                    ultimo=actual;
                }
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            if (terceroUltimo!=null){
                terceroUltimo.setDomicilios(domicilios);
                resultado.add(terceroUltimo);
            }


            // COMPARACIÓN Q NO VA NINGÚN PARÁMETRO DE CONSULTA DE DOMICILIO

            m_Log.debug("PROVINCIA ................  "+codProvincia);
            m_Log.debug("MUNICIPIO ................  "+codMunicipio);
            m_Log.debug("NUM DESDE ................  "+numDesde);
            m_Log.debug("LETRA DESDE ..............  "+letraDesde);
            m_Log.debug("NUM HASTA ................  "+numHasta);
            m_Log.debug("LETRA HASTA...............  "+letraHasta);
            m_Log.debug("DESC VIA .................  "+descVia);
            m_Log.debug("COD VIA ..................  "+codVia);
            m_Log.debug("COD ECO ..................  "+codEco);
            m_Log.debug("COD ESI ..................  "+codEsi);

            if (codProvincia == -1 && codMunicipio == -1 && numDesde == -1 && (letraDesde == null || "".equals(letraDesde)) &&
                    numHasta == -1 && (letraHasta == null || "".equals(letraHasta)) && codVia == -1 && codEco == -1 &&
                    codEsi == -1) {

            // Terceros q no tienen domicilio SÓLO SE AÑADEN A LOS ANTERIORES cuando la consulta no tiene ningún parámetro
            sql = "SELECT T_TER.* FROM T_TER WHERE "+ campos.getAtributo("situacionTER")+"='A' AND ";

            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    sql += condicion +" AND ";
                }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        sql += condicion + " AND ";
                    }*/
                    sql += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }
            if (name != null) {
                if (!"".equals(name.trim())) {
                    String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sql += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sql += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                   String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sql += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                sql += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                sql += "TER_DCE = '" + email + "' AND ";
            }

            int longitud =sql.length()-5;
            if(sql.endsWith(" AND "))
                sql = sql.substring(0,longitud);
            if(sql.endsWith(" WHERE "))
                sql = sql.substring(0,sql.length()-7);

            String sqlMinus = "";

            sqlMinus += "SELECT T_TER.* FROM T_TER,T_DOT WHERE ";
            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    sqlMinus += condicion +" AND ";
                }
             if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        sqlMinus += condicion + " AND ";
                    }*/
                    sqlMinus += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }
            if (name != null) {
                if (!"".equals(name.trim())) {
                    String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sqlMinus += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sqlMinus += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                    String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sqlMinus += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                sqlMinus += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                sqlMinus += "TER_DCE = '" + email + "' AND ";
            }

            sqlMinus += campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT");

            sql = sql + bd.minus(sql,sqlMinus);

            if(m_Log.isDebugEnabled()) {
                m_Log.debug("No tienen domicilio: solo se añaden cuando en la consutla no va ningún parámetro");
                m_Log.debug(sql);
            }
            state = con.createStatement();
            
            if(consultaInsensitiva){
                String alter1=null;
                String alter2=null;
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    m_Log.debug("ALTER SESSION SET LINGUISTIC/GENERICBASELETTER");

                    state.executeQuery(alter1);
                    state.executeQuery(alter2);

                } 
            }
            
            rs = state.executeQuery(sql);
            TercerosValueObject tercero = null;
            
            while(rs.next()){
                String codTercero  = rs.getString((String)campos.getAtributo("idTerceroTER"));
                tercero = new TercerosValueObject(
                        codTercero,
                        rs.getString((String)campos.getAtributo("versionTER")),
                        rs.getString((String)campos.getAtributo("tipoDocTER")),
                        "",
                        rs.getString((String)campos.getAtributo("documentoTER")),
                        rs.getString((String)campos.getAtributo("nombreTER")),
                        rs.getString((String)campos.getAtributo("apellido1TER")),
                        rs.getString((String)campos.getAtributo("pApellido1TER")),
                        rs.getString((String)campos.getAtributo("apellido2TER")),
                        rs.getString((String)campos.getAtributo("pApellido2TER")),
                        rs.getString((String)campos.getAtributo("normalizadoTER")),
                        rs.getString((String)campos.getAtributo("telefonoTER")),
                        rs.getString((String)campos.getAtributo("emailTER")),
                        rs.getString((String)campos.getAtributo("situacionTER")).charAt(0),
                        rs.getString((String)campos.getAtributo("fechaAltaTER")),
                        rs.getString((String)campos.getAtributo("usuarioAltaTER")),
                        rs.getString((String)campos.getAtributo("moduloAltaTER")),
                        rs.getString((String)campos.getAtributo("fechaBajaTER")),
                        rs.getString((String)campos.getAtributo("usuarioBajaTER")));
                tercero.setSituacion(rs.getString((String)campos.getAtributo("situacionTER")).charAt(0));
                tercero.setDomicilios(new Vector());

                /*** SE RECUPERA LOS VALORES DE L TERCERO *****/
                resultado.add(tercero);
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
         }


        /***************** PARA LOS TERCEROS RECUPERADOS, SE RECUPERAN LOS VALORES DE SUS CAMPOS SUPLEMENTARIOS ***********/
         DatosSuplementariosTerceroManager camposManager = DatosSuplementariosTerceroManager.getInstance();

        String codOrganizacion = Integer.toString(condsBusqueda.getCodOrganizacion());
        try{
            Vector<EstructuraCampo> estructura = camposManager.cargaEstructuraDatosSuplementariosTercero(codOrganizacion, params);

            Vector aux = new Vector();
            for(int i=0;resultado!=null && i<resultado.size();i++){
                TercerosValueObject tercero = (TercerosValueObject)resultado.get(i);
                tercero.setValoresCamposSuplementarios(camposManager.cargaValoresDatosSuplementarios(codOrganizacion,tercero.getIdentificador(),estructura, params));
                aux.add(tercero);
            }
            
            resultado = null;
            resultado = aux;

        }catch(Exception e){
            e.printStackTrace();
        }
        /******************************************************************************************************************/

            // fin COMPARACIÓN Q NO VA NINGÚN PARÁMETRO DE CONSULTA DE DOMICILIO
        } catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
             try{
                if (consultaInsensitiva){
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                       String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                       String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                       m_Log.debug("ALTER SESSION SET BINARY/SPANISH");

                       state=con.createStatement();
                       state.executeQuery(alter1);
                       SigpGeneralOperations.closeStatement(state);
                       state=con.createStatement();
                       state.executeQuery(alter2);
                       SigpGeneralOperations.closeStatement(state);
                   } 
                }
            }catch (SQLException se){
                if(m_Log.isErrorEnabled()) m_Log.error(se.getMessage());
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        
        if  (superadoLimite==true){
            resultado=null;
             throw new DemasiadosResultadosBusquedaTerceroException("DEMASIADOS VALORES", null);
        }
        return resultado;
    }

    
    /***********************************************************/
    
    /**
     * Realiza una búsqueda de terceros que cumplen unas determinadas condiciones de búsqueda
     * @param condsBusqueda: Objeto de tipo CondicionesBusquedaTerceroVO
     * @param bd: Objeto de tipo AdaptadorSQLBD
     * @param con: Objeto de tipo Connection
     * @return Vector
     * @throws TechnicalException RE
     */
    public Vector getTercero(CondicionesBusquedaTerceroVO condsBusqueda, AdaptadorSQLBD bd,Connection con,String[] params) throws TechnicalException{
        String sql;
        Vector<TercerosValueObject> resultado = new Vector<TercerosValueObject>();        
        ResultSet rs =null;
        Statement state = null;
        TransformacionAtributoSelect transformador;
        
        try{
        
        
            transformador = new TransformacionAtributoSelect(bd,con);
            int tipoDoc = condsBusqueda.getTipoDocumento();
            String docu = condsBusqueda.getDocumento();
            String name = condsBusqueda.getNombre();
            String apell1 = condsBusqueda.getApellido1();
            String apell2 = condsBusqueda.getApellido2();
            String telefono = condsBusqueda.getTelefono();
            String email = condsBusqueda.getEmail();
            String codPais = (String)campos.getAtributo("codigoPais");
            int codProvincia = condsBusqueda.getCodigoProvincia();
            int codMunicipio = condsBusqueda.getCodigoMunicipio();
            if (codProvincia == -1 && codMunicipio == -1) codPais = "";
            int codVia = condsBusqueda.getCodigoVia();
            String descVia = condsBusqueda.getNombreVia();
            int numDesde = condsBusqueda.getNumeroDesde();
            String letraDesde = condsBusqueda.getLetraDesde();
            int numHasta = condsBusqueda.getNumeroHasta();
            String letraHasta = condsBusqueda.getLetraHasta();
            String bloque = condsBusqueda.getBloque();
            String portal = condsBusqueda.getPortal();
            String escalera = condsBusqueda.getEscalera();
            String planta = condsBusqueda.getPlanta();
            String puerta = condsBusqueda.getPuerta();
            String codPostal = condsBusqueda.getCodPostal();
            String domicilio = condsBusqueda.getDomicilio();
            String lugar = condsBusqueda.getLugar();
            int codEsi = condsBusqueda.getCodigoEsi();
            int codEco = condsBusqueda.getCodigoEco();


            // PRIMERA PARTE DE LA UNION (DOMICILIOS NO NORMALIZADOS)
            String from = "TER_COD,TER_TID,TER_DOC,TER_NOM,TER_AP1,TER_PA1,TER_AP2,TER_PA2,TER_NOC,TER_NML,TER_TLF,TER_DCE,TER_SIT,TER_NVE,TER_FAL,TER_UAL,TER_APL,TER_FBJ,TER_UBJ,TER_DOM,T_DOM.*," +
                    campos.getAtributo("codTVI")+" AS CODTIPOVIA,"+
                    campos.getAtributo("descTVI")+" AS DESCTIPOVIA," +
                    campos.getAtributo("descESI") +" AS LUGAR," +
                    campos.getAtributo("idVIA")+" AS IDVIA,"+
                    campos.getAtributo("codVIA")+" AS CODVIA,"+
                    campos.getAtributo("descVIA")+" AS DESCVIA,"+
                    campos.getAtributo("domicilioDNN")+" AS DESCDMC," +
                    campos.getAtributo("numDesdeDNN")+" AS NUD,"+
                    campos.getAtributo("letraDesdeDNN")+" AS LED,"+
                    campos.getAtributo("numHastaDNN")+" AS NUH,"+
                    campos.getAtributo("letraHastaDNN")+" AS LEH," +
                    campos.getAtributo("bloqueDNN")+" AS BLQ,"+
                    campos.getAtributo("portalDNN")+" AS POR,"+
                    campos.getAtributo("escaleraDNN")+" AS ESC,"+
                    campos.getAtributo("plantaDNN")+" AS PLT," +
                    campos.getAtributo("puertaDNN")+" AS PTA,"+
                    campos.getAtributo("codPostalDNN")+" AS CPO,"+
                    campos.getAtributo("barriadaDNN")+" AS CAS,"+
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA")+","+campos.getAtributo("codECO") + "," +
                    campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");

            String where = "";
            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    where += condicion +" AND ";
                }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        where += condicion + " AND ";
                    }*/
                     where += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }
            if (name != null) {
                if (!"".equals(name.trim())) {
                   String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                   String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                where += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                where += "TER_DCE = '" + email + "' AND ";
            }

            String[] join = new String[38];

            join[0] = "T_TER";
            join[1] = "INNER";
            join[2] = "T_TID";
            join[3] = campos.getAtributo("tipoDocTER")+"="+campos.getAtributo("codTID");
            join[4] = "INNER";
            join[5] = "T_DOT";
            join[6] = campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT");
            join[7] = "INNER";
            join[8] = "T_DOM";
            join[9] = campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM");
            join[10] = "INNER";
            join[11] = "T_DNN";
            join[12] = campos.getAtributo("idDomicilioDOM")+"="+campos.getAtributo("idDomicilioDNN");
            join[13] = "LEFT";
            join[14] = "T_TOC";
            join[15] = campos.getAtributo("tipoOcupacionDOT")+"="+campos.getAtributo("codTOC");

            where = (codPais.equals("")) ? where
                    :where+campos.getAtributo("codPaisDNN")+"="+codPais+ " AND ";
            where = (codProvincia == -1) ? where
                    :where+campos.getAtributo("codProvDNN")+"="+codProvincia+ " AND ";
            where = (codMunicipio == -1) ? where
                    :where+campos.getAtributo("codMunDNN")+"="+codMunicipio+ " AND ";
            where = (numDesde == -1) ? where
                    :where+campos.getAtributo("numDesdeDNN")+"="+numDesde+ " AND ";
            where = (letraDesde == null || "".equals(letraDesde)) ? where
                    :where+campos.getAtributo("letraDesdeDNN")+"="+bd.addString(letraDesde)+" AND ";
            where = (numHasta == -1) ? where
                    :where+campos.getAtributo("numHastaDNN")+"="+numHasta+ " AND ";
            where = (letraDesde == null || "".equals(letraHasta)) ? where
                    :where+campos.getAtributo("letraHastaDNN")+"="+bd.addString(letraHasta)+" AND ";

            if (bloque != null && !bloque.equals("")) {
                where += "DNN_BLQ = '" + bloque + "' AND ";
            }

            if (portal != null && !portal.equals("")) {
                where += "DNN_POR = '" + portal + "' AND ";
            }

            if (escalera != null && !escalera.equals("")) {
                where += "DNN_ESC = '" + escalera + "' AND ";
            }

            if (planta != null && !planta.equals("")) {
                where += "DNN_PLT = '" + planta + "' AND ";
            }

            if (puerta != null && !puerta.equals("")) {
                where += "DNN_PTA = '" + puerta + "' AND ";
            }

            if (codPostal != null && !codPostal.equals("")) {
                where += "DNN_CPO = '" + codPostal + "' AND ";
            }

            if (domicilio != null && !domicilio.equals("")) {
                where += "DNN_DMC = '" + domicilio + "' AND ";
            }

            if (lugar != null && !lugar.equals("")) {
                where += "DNN_LUG = '" + lugar + "' AND ";
            }

            join[16] = "INNER";
            join[17] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
            join[18] = campos.getAtributo("codPaisDNN")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDNN")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDNN")+"="+campos.getAtributo("codMUN");
            join[19] = "INNER";
            join[20] = GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI";
            join[21] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI");
            join[22] = "INNER";
            join[23] = GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV";
            join[24] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV");
            join[25] = "LEFT";
            join[26] = "T_VIA";
            join[27] = campos.getAtributo("codViaDNN")+"="+campos.getAtributo("idVIA")+" AND "+
                    campos.getAtributo("codPaisViaDNN")+"="+campos.getAtributo("codPaisVIA")+" AND "+
                    campos.getAtributo("codProvViaDNN")+"="+campos.getAtributo("codProvVIA")+" AND "+
                    campos.getAtributo("codMunViaDNN")+"="+campos.getAtributo("codMunVIA");

            where = (codVia == -1) ? where:where+campos.getAtributo("codVIA")+"="+codVia+ " AND ";
            where = (descVia == null || "".equals(descVia)) ? where:where+campos.getAtributo("descVIA")+" LIKE '%"+TransformacionAtributoSelect.replace(descVia,"'","''")+ "%' AND ";
            where = (codEco == -1) ? where:where+campos.getAtributo("codECO")+"="+codEco+ " AND ";
            where = (codEsi == -1) ? where:where+campos.getAtributo("codESI")+"="+codEsi+ " AND ";

            join[28] = "LEFT";
            join[29] = "T_TVI";
            join[30] = campos.getAtributo("idTipoViaDNN")+"="+campos.getAtributo("codTVI");
            join[31] = "LEFT";
            join[32] = "T_ESI";
            join[33] = campos.getAtributo("codESIDNN")+"="+campos.getAtributo("codESI");
            join[34] = "LEFT";
            join[35] = "T_ECO";
            join[36] = campos.getAtributo("codECOESI")+"="+campos.getAtributo("codECO");
            join[37] = "false";

            where += campos.getAtributo("situacionTER")+"='A' AND "+
                    campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("normalizadoDOM")+"=2 ";

            sql = bd.join(from,where,join);

            // SEGUNDA PARTE DE LA UNION (DOMICILIOS NORMALIZADOS)
            join = new String[41];

            from =  "TER_COD,TER_TID,TER_DOC,TER_NOM,TER_AP1,TER_PA1,TER_AP2,TER_PA2,TER_NOC,TER_NML,TER_TLF,TER_DCE,TER_SIT,TER_NVE,TER_FAL,TER_UAL,TER_APL,TER_FBJ,TER_UBJ,TER_DOM,T_DOM.*,"+
                    campos.getAtributo("codTVI")+","+
                    campos.getAtributo("descTVI")+"," +
                    campos.getAtributo("descESI")+"," +
                    campos.getAtributo("idVIA")+","+
                    campos.getAtributo("codVIA")+","+
                    campos.getAtributo("descVIA")+","+
                    campos.getAtributo("descVIA")+"," +
                    campos.getAtributo("numDesdeDSU")+","+
                    campos.getAtributo("letraDesdeDSU")+","+
                    campos.getAtributo("numHastaDSU")+","+
                    campos.getAtributo("letraHastaDSU")+"," +
                    campos.getAtributo("bloqueDSU")+","+
                    campos.getAtributo("portalDSU")+","+
                    campos.getAtributo("escaleraDPO")+","+
                    campos.getAtributo("plantaDPO")+"," +
                    campos.getAtributo("puertaDPO")+","+
                    campos.getAtributo("codPostalDSU")+","+
                    "("+  bd.funcionCadena(bd.FUNCIONCADENA_CONCAT,new String[]{(String) campos.getAtributo("descESI"),"' - '",(String) campos.getAtributo("descECO")}) +"), " +
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA")+
                    ","+campos.getAtributo("codECO") + "," + campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");

            where = "";

            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    where += condicion +" AND ";
                }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        where += condicion + " AND ";
                    }*/
                     where += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }

            if (name != null) {
                if (!"".equals(name.trim())) {
                    String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                    String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                where += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                where += "TER_DCE = '" + email + "' AND ";
            }

            where+= campos.getAtributo("situacionTER")+"='A' AND "+
                    campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("normalizadoDOM")+"=1 AND ";

            join[0] = "T_TER";
            join[1] = "INNER";
            join[2] = "T_TID";
            join[3] = campos.getAtributo("tipoDocTER")+"="+campos.getAtributo("codTID");
            join[4] = "INNER";
            join[5] = "T_DOT";
            join[6] = campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT");
            join[7] = "INNER";
            join[8] = "T_DOM";
            join[9] = campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM");
            join[10] = "INNER";
            join[11] = "T_DPO";
            join[12] = campos.getAtributo("idDomicilioDOM")+"="+campos.getAtributo("idDomicilioDPO");
            join[13] = "INNER";
            join[14] = "T_DSU";
            join[15] = campos.getAtributo("dirSueloDPO")+"="+campos.getAtributo("codDirSueloDSU");
            join[16] = "LEFT";
            join[17] = "T_TOC";
            join[18] = campos.getAtributo("tipoOcupacionDOT") + " = " + campos.getAtributo("codTOC");

            where = (codPais.equals("")) ? where
                    :where+campos.getAtributo("codPaisDSU")+"="+codPais+ " AND ";
            where = (codProvincia == -1) ? where
                    :where+campos.getAtributo("codProvDSU")+"="+codProvincia+ " AND ";
            where = (codMunicipio == -1) ? where
                    :where+campos.getAtributo("codMunDSU")+"="+codMunicipio+ " AND ";
            where = (numDesde == -1) ? where
                    :where+campos.getAtributo("numDesdeDSU")+"="+numDesde+ " AND ";
            where = (letraDesde ==  null || "".equals(letraDesde)) ? where
                    :where+campos.getAtributo("letraDesdeDSU")+"="+bd.addString(letraDesde)+" AND ";
            where = (numHasta == -1) ? where
                    :where+campos.getAtributo("numHastaDSU")+"="+numHasta+ " AND ";
            where = (letraHasta == null || "".equals(letraHasta)) ? where
                    :where+campos.getAtributo("letraHastaDSU")+"="+bd.addString(letraHasta)+" AND ";

            if (bloque != null && !bloque.equals("")) {
                where += "DSU_BLQ = '" + bloque + "' AND ";
            }

            if (portal != null && !portal.equals("")) {
                where += "DSU_POR = '" + portal + "' AND ";
            }

            if (lugar != null && !lugar.equals("")) {
                where += "DSU_LUG = '" + lugar + "' AND ";
            }

            if (codPostal != null && !codPostal.equals("")) {
                where += "DSU_CPO = '" + codPostal + "' AND ";
            }

            join[19] = "INNER";
            join[20] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
            join[21] = campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMUN");
            join[22] = "INNER";
            join[23] = GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI";
            join[24] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI");
            join[25] = "INNER";
            join[26] = GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV";
            join[27] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV");
            join[28] = "INNER";
            join[29] = "T_VIA";
            join[30] = campos.getAtributo("codViaDSU")+"="+campos.getAtributo("idVIA")+" AND "+
                    campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisVIA")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvVIA")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMunVIA");
            where = (codVia == -1) ? where:where+campos.getAtributo("codVIA")+"="+codVia+ " AND ";
            where = (descVia == null || "".equals(descVia)) ? where:where+campos.getAtributo("descVIA")+" LIKE '%"+TransformacionAtributoSelect.replace(descVia,"'","''")+ "%' AND ";

            where = (codEco == -1) ? where: where+campos.getAtributo("codECO")+"="+codEco+ " AND ";
            where = (codEsi == -1) ? where: where+campos.getAtributo("codESI")+"="+codEsi+ " AND ";

            where += campos.getAtributo("situacionDSU")+"='A'";
            join[31] = "INNER";
            join[32] = "T_TVI";
            join[33] = campos.getAtributo("tipoVIA")+"="+campos.getAtributo("codTVI");
            join[34] = "LEFT";
            join[35] = "T_ESI";
            join[36] = campos.getAtributo("codigoESI")+"="+campos.getAtributo("codESI");
            join[37] = "LEFT";
            join[38] = "T_ECO";
            join[39] = campos.getAtributo("codECOESI")+"="+campos.getAtributo("codECO");
            join[40] =  "false";
            sql = sql + " UNION " + bd.join(from,where,join);
            sql = sql + "ORDER BY TER_AP1, TER_COD";
            if(m_Log.isDebugEnabled()) m_Log.debug("CONSULTA DE BUSQUEDA DE TERCEROS"+sql);

            long tiempoInicio = System.currentTimeMillis();

            state = con.createStatement();
            
            
            if(consultaInsensitiva){
                String alter1=null;
                String alter2=null;
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    m_Log.debug("ALTER SESSION SET LINGUISTIC/GENERICBASELETTER");

                    state.executeQuery(alter1);
                    state.executeQuery(alter2);

                } 
            }
            
            
            rs = state.executeQuery(sql);
            long totalTiempo = System.currentTimeMillis() - tiempoInicio;
            m_Log.debug("El tiempo de demora es :" + totalTiempo + " miliseg");
            int actual;
            int ultimo=-1;
            DomicilioSimpleValueObject nuevo;
            Vector<DomicilioSimpleValueObject> domicilios = null;
            TercerosValueObject terceroActual;
            TercerosValueObject terceroUltimo = null;

            DatosSuplementariosTerceroDAO datosSupDAO = DatosSuplementariosTerceroDAO.getInstance();
            
            // OBTENER RESULTADOS DE LA CONSULTA
            while(rs.next()){
                actual=rs.getInt((String)campos.getAtributo("idTerceroTER"));
                if (actual==ultimo){
                    String descdmc = rs.getString("DESCDMC");
                    if (descdmc== null) descdmc ="";
                    nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                            rs.getString((String)campos.getAtributo("codPAI")),
                            rs.getString((String)campos.getAtributo("codPRV")),
                            rs.getString((String)campos.getAtributo("codMUN")),
                            rs.getString((String)campos.getAtributo("descPAI")),
                            rs.getString((String)campos.getAtributo("descPRV")),
                            rs.getString((String)campos.getAtributo("descMUN")),
                            TransformacionAtributoSelect.replace(descdmc,"\"","\\\""),rs.getString("CPO"));
                    nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                    nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                    nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                    nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                    nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                    if(rs.getString("CODVIA")!=null){
                        nuevo.setDescVia(rs.getString("DESCVIA"));
                        nuevo.setCodigoVia(rs.getString("CODVIA"));
                        nuevo.setIdVia(rs.getString("IDVIA"));
                        if(rs.getString("CODVIA").equals("0")) {
                        }
                    }
                    nuevo.setNumDesde(rs.getString("NUD"));
                    nuevo.setLetraDesde(rs.getString("LED"));
                    nuevo.setNumHasta(rs.getString("NUH"));
                    nuevo.setLetraHasta(rs.getString("LEH"));
                    nuevo.setBloque(rs.getString("BLQ"));
                    nuevo.setPortal(rs.getString("POR"));
                    nuevo.setEscalera(rs.getString("ESC"));
                    nuevo.setPlanta(rs.getString("PLT"));
                    nuevo.setPuerta(rs.getString("PTA"));
                    nuevo.setBarriada(rs.getString("CAS"));
                    nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                    nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                    nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                    /* anadir ECOESI */
                    nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                    nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                    nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                    /* fin anadir ECOESI */
                    domicilios.add(nuevo);
                }else{
                    if (terceroUltimo!=null){
                        terceroUltimo.setDomicilios(domicilios);
                        resultado.add(terceroUltimo);
                    }
                    terceroActual = new TercerosValueObject(
                            String.valueOf(actual),
                            rs.getString((String)campos.getAtributo("versionTER")),
                            rs.getString((String)campos.getAtributo("tipoDocTER")),
                            "",
                            rs.getString((String)campos.getAtributo("documentoTER")),
                            rs.getString((String)campos.getAtributo("nombreTER")),
                            rs.getString((String)campos.getAtributo("apellido1TER")),
                            rs.getString((String)campos.getAtributo("pApellido1TER")),
                            rs.getString((String)campos.getAtributo("apellido2TER")),
                            rs.getString((String)campos.getAtributo("pApellido2TER")),
                            rs.getString((String)campos.getAtributo("normalizadoTER")),
                            rs.getString((String)campos.getAtributo("telefonoTER")),
                            rs.getString((String)campos.getAtributo("emailTER")),
                            rs.getString((String)campos.getAtributo("situacionTER")).charAt(0),
                            rs.getString((String)campos.getAtributo("fechaAltaTER")),
                            rs.getString((String)campos.getAtributo("usuarioAltaTER")),
                            rs.getString((String)campos.getAtributo("moduloAltaTER")),
                            rs.getString((String)campos.getAtributo("fechaBajaTER")),
                            rs.getString((String)campos.getAtributo("usuarioBajaTER")));

                    terceroActual.setSituacion(rs.getString((String)campos.getAtributo("situacionTER")).charAt(0));
                    terceroActual.setDomPrincipal(rs.getString("TER_DOM"));
                    domicilios = new Vector<DomicilioSimpleValueObject>();
                    if (rs.getInt((String)campos.getAtributo("idDomicilioDOM"))>0){
                        nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                                rs.getString((String)campos.getAtributo("codPAI")),
                                rs.getString((String)campos.getAtributo("codPRV")),
                                rs.getString((String)campos.getAtributo("codMUN")),
                                rs.getString((String)campos.getAtributo("descPAI")),
                                rs.getString((String)campos.getAtributo("descPRV")),
                                rs.getString((String)campos.getAtributo("descMUN")),
                                TransformacionAtributoSelect.replace(rs.getString("DESCDMC"),"\"","\\\""),rs.getString("CPO"));
                        nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                        nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                        nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                        nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                        nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                        if(rs.getString("CODVIA")!=null){
                            nuevo.setDescVia(rs.getString("DESCVIA"));
                            nuevo.setCodigoVia(rs.getString("CODVIA"));
                            nuevo.setIdVia(rs.getString("IDVIA"));
                            if(rs.getString("CODVIA").equals("0")) {
                            }
                        }
                        nuevo.setNumDesde(rs.getString("NUD"));
                        nuevo.setLetraDesde(rs.getString("LED"));
                        nuevo.setNumHasta(rs.getString("NUH"));
                        nuevo.setLetraHasta(rs.getString("LEH"));
                        nuevo.setBloque(rs.getString("BLQ"));
                        nuevo.setPortal(rs.getString("POR"));
                        nuevo.setEscalera(rs.getString("ESC"));
                        nuevo.setPlanta(rs.getString("PLT"));
                        nuevo.setPuerta(rs.getString("PTA"));
                        nuevo.setBarriada(rs.getString("CAS"));
                        nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                        nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                        nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                        /* anadir ECOESI */
                        nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                        nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                        nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                        /* fin anadir ECOESI */

                        domicilios.add(nuevo);
                    }
                    terceroUltimo=terceroActual;
                    ultimo=actual;
                }
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            if (terceroUltimo!=null){
                terceroUltimo.setDomicilios(domicilios);
                resultado.add(terceroUltimo);
            }


            // COMPARACIÓN Q NO VA NINGÚN PARÁMETRO DE CONSULTA DE DOMICILIO

            m_Log.debug("PROVINCIA ................  "+codProvincia);
            m_Log.debug("MUNICIPIO ................  "+codMunicipio);
            m_Log.debug("NUM DESDE ................  "+numDesde);
            m_Log.debug("LETRA DESDE ..............  "+letraDesde);
            m_Log.debug("NUM HASTA ................  "+numHasta);
            m_Log.debug("LETRA HASTA...............  "+letraHasta);
            m_Log.debug("DESC VIA .................  "+descVia);
            m_Log.debug("COD VIA ..................  "+codVia);
            m_Log.debug("COD ECO ..................  "+codEco);
            m_Log.debug("COD ESI ..................  "+codEsi);

            if (codProvincia == -1 && codMunicipio == -1 && numDesde == -1 && (letraDesde == null || "".equals(letraDesde)) &&
                    numHasta == -1 && (letraHasta == null || "".equals(letraHasta)) && codVia == -1 && codEco == -1 &&
                    codEsi == -1) {

            // Terceros q no tienen domicilio SÓLO SE AÑADEN A LOS ANTERIORES cuando la consulta no tiene ningún parámetro
            sql = "SELECT T_TER.* FROM T_TER WHERE "+ campos.getAtributo("situacionTER")+"='A' AND ";

            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    sql += condicion +" AND ";
                }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        sql += condicion + " AND ";
                    }*/
                    sql += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }
            if (name != null) {
                if (!"".equals(name.trim())) {
                    String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sql += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sql += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                   String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sql += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                sql += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                sql += "TER_DCE = '" + email + "' AND ";
            }

            int longitud =sql.length()-5;
            if(sql.endsWith(" AND "))
                sql = sql.substring(0,longitud);
            if(sql.endsWith(" WHERE "))
                sql = sql.substring(0,sql.length()-7);

            String sqlMinus = "";

            sqlMinus += "SELECT T_TER.* FROM T_TER,T_DOT WHERE ";
            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    sqlMinus += condicion +" AND ";
                }
             if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        sqlMinus += condicion + " AND ";
                    }*/
                    sqlMinus += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }
            if (name != null) {
                if (!"".equals(name.trim())) {
                    String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sqlMinus += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sqlMinus += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                    String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sqlMinus += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                sqlMinus += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                sqlMinus += "TER_DCE = '" + email + "' AND ";
            }

            sqlMinus += campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT");

            sql = sql + bd.minus(sql,sqlMinus);

            if(m_Log.isDebugEnabled()) {
                m_Log.debug("No tienen domicilio: solo se añaden cuando en la consutla no va ningún parámetro");
                m_Log.debug(sql);
            }
            state = con.createStatement();
            
            if(consultaInsensitiva){
                String alter1=null;
                String alter2=null;
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    m_Log.debug("ALTER SESSION SET LINGUISTIC/GENERICBASELETTER");

                    state.executeQuery(alter1);
                    state.executeQuery(alter2);

                } 
            }
            
            rs = state.executeQuery(sql);
            TercerosValueObject tercero = null;
            
            while(rs.next()){
                String codTercero  = rs.getString((String)campos.getAtributo("idTerceroTER"));
                tercero = new TercerosValueObject(
                        codTercero,
                        rs.getString((String)campos.getAtributo("versionTER")),
                        rs.getString((String)campos.getAtributo("tipoDocTER")),
                        "",
                        rs.getString((String)campos.getAtributo("documentoTER")),
                        rs.getString((String)campos.getAtributo("nombreTER")),
                        rs.getString((String)campos.getAtributo("apellido1TER")),
                        rs.getString((String)campos.getAtributo("pApellido1TER")),
                        rs.getString((String)campos.getAtributo("apellido2TER")),
                        rs.getString((String)campos.getAtributo("pApellido2TER")),
                        rs.getString((String)campos.getAtributo("normalizadoTER")),
                        rs.getString((String)campos.getAtributo("telefonoTER")),
                        rs.getString((String)campos.getAtributo("emailTER")),
                        rs.getString((String)campos.getAtributo("situacionTER")).charAt(0),
                        rs.getString((String)campos.getAtributo("fechaAltaTER")),
                        rs.getString((String)campos.getAtributo("usuarioAltaTER")),
                        rs.getString((String)campos.getAtributo("moduloAltaTER")),
                        rs.getString((String)campos.getAtributo("fechaBajaTER")),
                        rs.getString((String)campos.getAtributo("usuarioBajaTER")));
                tercero.setSituacion(rs.getString((String)campos.getAtributo("situacionTER")).charAt(0));
                tercero.setDomicilios(new Vector());

                /*** SE RECUPERA LOS VALORES DE L TERCERO *****/
                resultado.add(tercero);
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
         }


        /***************** PARA LOS TERCEROS RECUPERADOS, SE RECUPERAN LOS VALORES DE SUS CAMPOS SUPLEMENTARIOS ***********/
         DatosSuplementariosTerceroManager camposManager = DatosSuplementariosTerceroManager.getInstance();

        String codOrganizacion = Integer.toString(condsBusqueda.getCodOrganizacion());
        try{
           // Vector<EstructuraCampo> estructura = camposManager.cargaEstructuraDatosSuplementariosTercero(codOrganizacion, params);
            
            Vector<EstructuraCampo> estructura = DatosSuplementariosTerceroDAO.getInstance().cargaEstructuraDatosSuplementariosTercero(codOrganizacion,bd,con);

            Vector aux = new Vector();
            for(int i=0;resultado!=null && i<resultado.size();i++){
                TercerosValueObject tercero = (TercerosValueObject)resultado.get(i);
                //tercero.setValoresCamposSuplementarios(camposManager.cargaValoresDatosSuplementarios(codOrganizacion,tercero.getIdentificador(),estructura, params));                
                //tercero.setValoresCamposSuplementarios(DatosSuplementariosTerceroDAO.getInstance().cargaValoresDatosSuplementarios(codOrganizacion,tercero.getIdentificador(),estructura,con,params));                                
                tercero.setValoresCamposSuplementarios(DatosSuplementariosTerceroDAO.getInstance().cargaValoresDatosSuplementarios(codOrganizacion,tercero.getIdentificador(),estructura,con,bd));                
                aux.add(tercero);
            }
            
            resultado = null;
            resultado = aux;

        }catch(Exception e){
            e.printStackTrace();
        }
        /******************************************************************************************************************/

        // fin COMPARACIÓN Q NO VA NINGÚN PARÁMETRO DE CONSULTA DE DOMICILIO
        
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            
            try{
                if (consultaInsensitiva){
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                       String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                       String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                       m_Log.debug("ALTER SESSION SET BINARY/SPANISH");

                       state=con.createStatement();
                       state.executeQuery(alter1);
                       SigpGeneralOperations.closeStatement(state);
                       state=con.createStatement();
                       state.executeQuery(alter2);
                       SigpGeneralOperations.closeStatement(state);
                   } 
                }
            }catch (SQLException se){
                if(m_Log.isErrorEnabled()) m_Log.error(se.getMessage());
            }
            
            cerrarResultSet(rs);
            cerrarStatement(state);        
        }
        return resultado;
    }

    
    
    
    
    /**********************************************************/
    
    
    public Vector getTerceroExpMiUnidad(CondicionesBusquedaTerceroVO condsBusqueda, String[] params) throws TechnicalException{
        String sql;
        Vector<TercerosValueObject> resultado = new Vector<TercerosValueObject>();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs = null;
        Statement state = null;
        TransformacionAtributoSelect transformador;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            transformador = new TransformacionAtributoSelect(bd,con);
            state = con.createStatement();
            int tipoDoc = condsBusqueda.getTipoDocumento();
            String docu = condsBusqueda.getDocumento();
            String name = condsBusqueda.getNombre();
            String apell1 = condsBusqueda.getApellido1();
            String apell2 = condsBusqueda.getApellido2();
            String telefono = condsBusqueda.getTelefono();
            String email = condsBusqueda.getEmail();
            String codPais = (String)campos.getAtributo("codigoPais");
            int codProvincia = condsBusqueda.getCodigoProvincia();
            int codMunicipio = condsBusqueda.getCodigoMunicipio();
            if (codProvincia == -1 && codMunicipio == -1) codPais = "";
            int codVia = condsBusqueda.getCodigoVia();
            String descVia = condsBusqueda.getNombreVia();
            int numDesde = condsBusqueda.getNumeroDesde();
            String letraDesde = condsBusqueda.getLetraDesde();
            int numHasta = condsBusqueda.getNumeroHasta();
            String letraHasta = condsBusqueda.getLetraHasta();
            String bloque = condsBusqueda.getBloque();
            String portal = condsBusqueda.getPortal();
            String escalera = condsBusqueda.getEscalera();
            String planta = condsBusqueda.getPlanta();
            String puerta = condsBusqueda.getPuerta();
            String codPostal = condsBusqueda.getCodPostal();
            String domicilio = condsBusqueda.getDomicilio();
            String lugar = condsBusqueda.getLugar();
            int codEsi = condsBusqueda.getCodigoEsi();
            int codEco = condsBusqueda.getCodigoEco();
            int codUsuario=condsBusqueda.getIdUsuario();
            int codEntidad=condsBusqueda.getCodEnt();
            int codOrganizacion=condsBusqueda.getCodOrganizacion();
            
            // PRIMERA PARTE DE LA UNION (DOMICILIOS NO NORMALIZADOS)  (DETERMINAR LOS TERCEROS DE EXPEDIENTES CON UOR QUE LOS TRAMITAN IGUAL QUE LA DEL USUARIO)

            String from = "DISTINCT TER_COD,TER_TID,TER_DOC,TER_NOM,TER_AP1,TER_PA1,TER_AP2,TER_PA2,TER_NOC,TER_NML,TER_TLF,TER_DCE,TER_SIT,TER_NVE,TER_FAL,TER_UAL,TER_APL,TER_FBJ,TER_UBJ,TER_DOM,T_DOM.*," +
                    campos.getAtributo("codTVI")+" AS CODTIPOVIA,"+
                    campos.getAtributo("descTVI")+" AS DESCTIPOVIA," +
                    campos.getAtributo("descESI") +" AS LUGAR," +
                    campos.getAtributo("idVIA")+" AS IDVIA,"+
                    campos.getAtributo("codVIA")+" AS CODVIA,"+
                    campos.getAtributo("descVIA")+" AS DESCVIA,"+
                    campos.getAtributo("domicilioDNN")+" AS DESCDMC," +
                    campos.getAtributo("numDesdeDNN")+" AS NUD,"+
                    campos.getAtributo("letraDesdeDNN")+" AS LED,"+
                    campos.getAtributo("numHastaDNN")+" AS NUH,"+
                    campos.getAtributo("letraHastaDNN")+" AS LEH," +
                    campos.getAtributo("bloqueDNN")+" AS BLQ,"+
                    campos.getAtributo("portalDNN")+" AS POR,"+
                    campos.getAtributo("escaleraDNN")+" AS ESC,"+
                    campos.getAtributo("plantaDNN")+" AS PLT," +
                    campos.getAtributo("puertaDNN")+" AS PTA,"+
                    campos.getAtributo("codPostalDNN")+" AS CPO,"+
                    campos.getAtributo("barriadaDNN")+" AS CAS,"+
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA")+","+campos.getAtributo("codECO") + "," +
                    campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");

            String where = "";
            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    where += condicion +" AND ";
                }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        where += condicion + " AND ";
                    }*/
                     where += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }
            if (name != null) {
                if (!"".equals(name.trim())) {
                   String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                   String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                where += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                where += "TER_DCE = '" +email + "' AND ";
            }
            if (codUsuario != -1) {
                where += "UOU_USU = " + codUsuario + " AND ";
            }
             if (codEntidad != -1) {
                where += "UOU_ENT = " + codEntidad + " AND ";
            }
            if (codOrganizacion != -1) {
                where += "UOU_ORG = " + codOrganizacion + " AND ";
            }

            String[] join = new String[47];

            join[0] = "T_TER";
            join[1] = "INNER";
            join[2] = "T_TID";
            join[3] = campos.getAtributo("tipoDocTER")+"="+campos.getAtributo("codTID");
            join[4] = "INNER";
            join[5] = "T_DOT";
            join[6] = campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT");
            join[7] = "INNER";
            join[8] = "T_DOM";
            join[9] = campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM");
            join[10] = "INNER";
            join[11] = "T_DNN";
            join[12] = campos.getAtributo("idDomicilioDOM")+"="+campos.getAtributo("idDomicilioDNN");
            join[13] = "LEFT";
            join[14] = "T_TOC";
            join[15] = campos.getAtributo("tipoOcupacionDOT")+"="+campos.getAtributo("codTOC");
            join[16] = "INNER";
            join[17] = "E_EXT";
            join[18] = campos.getAtributo("codTerceroEXT")+"="+campos.getAtributo("idTerceroTER")+" AND "+
                       campos.getAtributo("codTerceroEXT")+"="+campos.getAtributo("idTerceroDOT");
            join[19] = "INNER";
            join[20] = "E_CRO";
            join[21] = campos.getAtributo("numeroExpedienteEXT")+"="+campos.getAtributo("numeroExpedienteCRO")+" AND "+
                       campos.getAtributo("codMunicipioEXT")+"="+campos.getAtributo("codMunicipioCRO")+" AND "+
                       campos.getAtributo("codEjercicioEXT")+"="+campos.getAtributo("codEjercicioCRO");


            where = (codPais.equals("")) ? where
                    :where+campos.getAtributo("codPaisDNN")+"="+codPais+ " AND ";
            where = (codProvincia == -1) ? where
                    :where+campos.getAtributo("codProvDNN")+"="+codProvincia+ " AND ";
            where = (codMunicipio == -1) ? where
                    :where+campos.getAtributo("codMunDNN")+"="+codMunicipio+ " AND ";
            where = (numDesde == -1) ? where
                    :where+campos.getAtributo("numDesdeDNN")+"="+numDesde+ " AND ";
            where = (letraDesde == null || "".equals(letraDesde)) ? where
                    :where+campos.getAtributo("letraDesdeDNN")+"="+bd.addString(letraDesde)+" AND ";
            where = (numHasta == -1) ? where
                    :where+campos.getAtributo("numHastaDNN")+"="+numHasta+ " AND ";
            where = (letraDesde == null || "".equals(letraHasta)) ? where
                    :where+campos.getAtributo("letraHastaDNN")+"="+bd.addString(letraHasta)+" AND ";

            if (bloque != null && !bloque.equals("")) {
                where += "DNN_BLQ = '" + bloque + "' AND ";
            }

            if (portal != null && !portal.equals("")) {
                where += "DNN_POR = '" + portal + "' AND ";
            }

            if (escalera != null && !escalera.equals("")) {
                where += "DNN_ESC = '" + escalera + "' AND ";
            }

            if (planta != null && !planta.equals("")) {
                where += "DNN_PLT = '" + planta + "' AND ";
            }

            if (puerta != null && !puerta.equals("")) {
                where += "DNN_PTA = '" + puerta + "' AND ";
            }

            if (codPostal != null && !codPostal.equals("")) {
                where += "DNN_CPO = '" + codPostal + "' AND ";
            }

            if (domicilio != null && !domicilio.equals("")) {
                where += "DNN_DMC = '" + domicilio + "' AND ";
            }

            if (lugar != null && !lugar.equals("")) {
                where += "DNN_LUG = '" + lugar + "' AND ";
            }

            join[22] = "INNER";
            join[23] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
            join[24] = campos.getAtributo("codPaisDNN")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDNN")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDNN")+"="+campos.getAtributo("codMUN");
            join[25] = "INNER";
            join[26] = GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI";
            join[27] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI");
            join[28] = "INNER";
            join[29] = GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV";
            join[30] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV");
            join[31] = "LEFT";
            join[32] = "T_VIA";
            join[33] = campos.getAtributo("codViaDNN")+"="+campos.getAtributo("idVIA")+" AND "+
                    campos.getAtributo("codPaisViaDNN")+"="+campos.getAtributo("codPaisVIA")+" AND "+
                    campos.getAtributo("codProvViaDNN")+"="+campos.getAtributo("codProvVIA")+" AND "+
                    campos.getAtributo("codMunViaDNN")+"="+campos.getAtributo("codMunVIA");
            join[34] = "INNER";
            join[35] = GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU";
            join[36] = campos.getAtributo("UorUOU")+"="+campos.getAtributo("UtrCRO");

            where = (codVia == -1) ? where:where+campos.getAtributo("codVIA")+"="+codVia+ " AND ";
            where = (descVia == null || "".equals(descVia)) ? where:where+campos.getAtributo("descVIA")+" LIKE '%"+TransformacionAtributoSelect.replace(descVia,"'","''")+ "%' AND ";
            where = (codEco == -1) ? where:where+campos.getAtributo("codECO")+"="+codEco+ " AND ";
            where = (codEsi == -1) ? where:where+campos.getAtributo("codESI")+"="+codEsi+ " AND ";

            join[37] = "LEFT";
            join[38] = "T_TVI";
            join[39] = campos.getAtributo("idTipoViaDNN")+"="+campos.getAtributo("codTVI");
            join[40] = "LEFT";
            join[41] = "T_ESI";
            join[42] = campos.getAtributo("codESIDNN")+"="+campos.getAtributo("codESI");
            join[43] = "LEFT";
            join[44] = "T_ECO";
            join[45] = campos.getAtributo("codECOESI")+"="+campos.getAtributo("codECO");
            join[46] = "false";

            where += campos.getAtributo("situacionTER")+"='A' AND "+
                    campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("normalizadoDOM")+"=2 ";

            sql = bd.join(from,where,join);




            // SEGUNDA PARTE DE LA UNION (DOMICILIOS NO NORMALIZADOS)  (DETERMINAR LOS TERCEROS DE EXPEDIENTES CON UORS DE INICIO IGUAL QUE LA DEL USUARIO)
            join = new String[47];

            from = "DISTINCT TER_COD,TER_TID,TER_DOC,TER_NOM,TER_AP1,TER_PA1,TER_AP2,TER_PA2,TER_NOC,TER_NML,TER_TLF,TER_DCE,TER_SIT,TER_NVE,TER_FAL,TER_UAL,TER_APL,TER_FBJ,TER_UBJ,TER_DOM,T_DOM.*," +
                    campos.getAtributo("codTVI")+" AS CODTIPOVIA,"+
                    campos.getAtributo("descTVI")+" AS DESCTIPOVIA," +
                    campos.getAtributo("descESI") +" AS LUGAR," +
                    campos.getAtributo("idVIA")+" AS IDVIA,"+
                    campos.getAtributo("codVIA")+" AS CODVIA,"+
                    campos.getAtributo("descVIA")+" AS DESCVIA,"+
                    campos.getAtributo("domicilioDNN")+" AS DESCDMC," +
                    campos.getAtributo("numDesdeDNN")+" AS NUD,"+
                    campos.getAtributo("letraDesdeDNN")+" AS LED,"+
                    campos.getAtributo("numHastaDNN")+" AS NUH,"+
                    campos.getAtributo("letraHastaDNN")+" AS LEH," +
                    campos.getAtributo("bloqueDNN")+" AS BLQ,"+
                    campos.getAtributo("portalDNN")+" AS POR,"+
                    campos.getAtributo("escaleraDNN")+" AS ESC,"+
                    campos.getAtributo("plantaDNN")+" AS PLT," +
                    campos.getAtributo("puertaDNN")+" AS PTA,"+
                    campos.getAtributo("codPostalDNN")+" AS CPO,"+
                    campos.getAtributo("barriadaDNN")+" AS CAS,"+
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA")+","+campos.getAtributo("codECO") + "," +
                    campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");

             where = "";
            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    where += condicion +" AND ";
                }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        where += condicion + " AND ";
                    }*/
                     where += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }
            if (name != null) {
                if (!"".equals(name.trim())) {
                   String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                   String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                where += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                where += "TER_DCE = '" + email + "' AND ";
            }
             if (codUsuario != -1) {
                where += "UOU_USU = " + codUsuario + " AND ";
            }
             if (codEntidad != -1) {
                where += "UOU_ENT = " + codEntidad + " AND ";
            }
            if (codOrganizacion != -1) {
                where += "UOU_ORG = " + codOrganizacion + " AND ";
            }



            join[0] = "T_TER";
            join[1] = "INNER";
            join[2] = "T_TID";
            join[3] = campos.getAtributo("tipoDocTER")+"="+campos.getAtributo("codTID");
            join[4] = "INNER";
            join[5] = "T_DOT";
            join[6] = campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT");
            join[7] = "INNER";
            join[8] = "T_DOM";
            join[9] = campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM");
            join[10] = "INNER";
            join[11] = "T_DNN";
            join[12] = campos.getAtributo("idDomicilioDOM")+"="+campos.getAtributo("idDomicilioDNN");
            join[13] = "LEFT";
            join[14] = "T_TOC";
            join[15] = campos.getAtributo("tipoOcupacionDOT")+"="+campos.getAtributo("codTOC");
            join[16] = "INNER";
            join[17] = "E_EXT";
            join[18] = campos.getAtributo("codTerceroEXT")+"="+campos.getAtributo("idTerceroTER")+" AND "+
                       campos.getAtributo("codTerceroEXT")+"="+campos.getAtributo("idTerceroDOT");
            join[19] = "INNER";
            join[20] = "E_EXP";
            join[21] = campos.getAtributo("numeroExpedienteEXT")+"="+campos.getAtributo("numeroExpedienteEXP")+" AND "+
                       campos.getAtributo("codMunicipioEXT")+"="+campos.getAtributo("codMunicipioEXP")+" AND "+
                       campos.getAtributo("codEjercicioEXT")+"="+campos.getAtributo("codEjercicioEXP");


            where = (codPais.equals("")) ? where
                    :where+campos.getAtributo("codPaisDNN")+"="+codPais+ " AND ";
            where = (codProvincia == -1) ? where
                    :where+campos.getAtributo("codProvDNN")+"="+codProvincia+ " AND ";
            where = (codMunicipio == -1) ? where
                    :where+campos.getAtributo("codMunDNN")+"="+codMunicipio+ " AND ";
            where = (numDesde == -1) ? where
                    :where+campos.getAtributo("numDesdeDNN")+"="+numDesde+ " AND ";
            where = (letraDesde == null || "".equals(letraDesde)) ? where
                    :where+campos.getAtributo("letraDesdeDNN")+"="+bd.addString(letraDesde)+" AND ";
            where = (numHasta == -1) ? where
                    :where+campos.getAtributo("numHastaDNN")+"="+numHasta+ " AND ";
            where = (letraDesde == null || "".equals(letraHasta)) ? where
                    :where+campos.getAtributo("letraHastaDNN")+"="+bd.addString(letraHasta)+" AND ";

            if (bloque != null && !bloque.equals("")) {
                where += "DNN_BLQ = '" + bloque + "' AND ";
            }

            if (portal != null && !portal.equals("")) {
                where += "DNN_POR = '" + portal + "' AND ";
            }

            if (escalera != null && !escalera.equals("")) {
                where += "DNN_ESC = '" + escalera + "' AND ";
            }

            if (planta != null && !planta.equals("")) {
                where += "DNN_PLT = '" + planta + "' AND ";
            }

            if (puerta != null && !puerta.equals("")) {
                where += "DNN_PTA = '" + puerta + "' AND ";
            }

            if (codPostal != null && !codPostal.equals("")) {
                where += "DNN_CPO = '" + codPostal + "' AND ";
            }

            if (domicilio != null && !domicilio.equals("")) {
                where += "DNN_DMC = '" + domicilio + "' AND ";
            }

            if (lugar != null && !lugar.equals("")) {
                where += "DNN_LUG = '" + lugar + "' AND ";
            }

            join[22] = "INNER";
            join[23] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
            join[24] = campos.getAtributo("codPaisDNN")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDNN")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDNN")+"="+campos.getAtributo("codMUN");
            join[25] = "INNER";
            join[26] = GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI";
            join[27] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI");
            join[28] = "INNER";
            join[29] = GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV";
            join[30] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV");
            join[31] = "LEFT";
            join[32] = "T_VIA";
            join[33] = campos.getAtributo("codViaDNN")+"="+campos.getAtributo("idVIA")+" AND "+
                    campos.getAtributo("codPaisViaDNN")+"="+campos.getAtributo("codPaisVIA")+" AND "+
                    campos.getAtributo("codProvViaDNN")+"="+campos.getAtributo("codProvVIA")+" AND "+
                    campos.getAtributo("codMunViaDNN")+"="+campos.getAtributo("codMunVIA");
             join[34] = "INNER";
            join[35] = GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU";
            join[36] = campos.getAtributo("UorUOU")+"="+campos.getAtributo("UorEXP");

            where = (codVia == -1) ? where:where+campos.getAtributo("codVIA")+"="+codVia+ " AND ";
            where = (descVia == null || "".equals(descVia)) ? where:where+campos.getAtributo("descVIA")+" LIKE '%"+TransformacionAtributoSelect.replace(descVia,"'","''")+ "%' AND ";
            where = (codEco == -1) ? where:where+campos.getAtributo("codECO")+"="+codEco+ " AND ";
            where = (codEsi == -1) ? where:where+campos.getAtributo("codESI")+"="+codEsi+ " AND ";

            join[37] = "LEFT";
            join[38] = "T_TVI";
            join[39] = campos.getAtributo("idTipoViaDNN")+"="+campos.getAtributo("codTVI");
            join[40] = "LEFT";
            join[41] = "T_ESI";
            join[42] = campos.getAtributo("codESIDNN")+"="+campos.getAtributo("codESI");
            join[43] = "LEFT";
            join[44] = "T_ECO";
            join[45] = campos.getAtributo("codECOESI")+"="+campos.getAtributo("codECO");
            join[46] = "false";

            where += campos.getAtributo("situacionTER")+"='A' AND "+
                    campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("normalizadoDOM")+"=2 ";

             sql = sql + " UNION " + bd.join(from,where,join);





            // TERCERA PARTE DE LA UNION (DOMICILIOS NORMALIZADOS) TERCEROS DE EXPEDIENTES CON UORS QUE TRAMITAN EL EXPEDIENTE IGUAL DEL USUARIO
            join = new String[50];

            from =  "DISTINCT TER_COD,TER_TID,TER_DOC,TER_NOM,TER_AP1,TER_PA1,TER_AP2,TER_PA2,TER_NOC,TER_NML,TER_TLF,TER_DCE,TER_SIT,TER_NVE,TER_FAL,TER_UAL,TER_APL,TER_FBJ,TER_UBJ,TER_DOM,T_DOM.*,"+
                    campos.getAtributo("codTVI")+","+
                    campos.getAtributo("descTVI")+"," +
                    campos.getAtributo("descESI")+"," +
                    campos.getAtributo("idVIA")+","+
                    campos.getAtributo("codVIA")+","+
                    campos.getAtributo("descVIA")+","+
                    campos.getAtributo("descVIA")+"," +
                    campos.getAtributo("numDesdeDSU")+","+
                    campos.getAtributo("letraDesdeDSU")+","+
                    campos.getAtributo("numHastaDSU")+","+
                    campos.getAtributo("letraHastaDSU")+"," +
                    campos.getAtributo("bloqueDSU")+","+
                    campos.getAtributo("portalDSU")+","+
                    campos.getAtributo("escaleraDPO")+","+
                    campos.getAtributo("plantaDPO")+"," +
                    campos.getAtributo("puertaDPO")+","+
                    campos.getAtributo("codPostalDSU")+","+
                    "("+  bd.funcionCadena(bd.FUNCIONCADENA_CONCAT,new String[]{(String) campos.getAtributo("descESI"),"' - '",(String) campos.getAtributo("descECO")}) +"), " +
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA")+
                    ","+campos.getAtributo("codECO") + "," + campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");

            where = "";

            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    where += condicion +" AND ";
                }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        where += condicion + " AND ";
                    }*/
                     where += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }

            if (name != null) {
                if (!"".equals(name.trim())) {
                    String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                    String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                where += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                where += "TER_DCE = '" + email + "' AND ";
            }
             if (codUsuario != -1) {
                where += "UOU_USU = " + codUsuario + " AND ";
            }
             if (codEntidad != -1) {
                where += "UOU_ENT = " + codEntidad + " AND ";
            }
            if (codOrganizacion != -1) {
                where += "UOU_ORG = " + codOrganizacion + " AND ";
            }

            where+= campos.getAtributo("situacionTER")+"='A' AND "+
                    campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("normalizadoDOM")+"=1 AND ";

            join[0] = "T_TER";
            join[1] = "INNER";
            join[2] = "T_TID";
            join[3] = campos.getAtributo("tipoDocTER")+"="+campos.getAtributo("codTID");
            join[4] = "INNER";
            join[5] = "T_DOT";
            join[6] = campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT");
            join[7] = "INNER";
            join[8] = "T_DOM";
            join[9] = campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM");
            join[10] = "INNER";
            join[11] = "T_DPO";
            join[12] = campos.getAtributo("idDomicilioDOM")+"="+campos.getAtributo("idDomicilioDPO");
            join[13] = "INNER";
            join[14] = "T_DSU";
            join[15] = campos.getAtributo("dirSueloDPO")+"="+campos.getAtributo("codDirSueloDSU");
            join[16] = "LEFT";
            join[17] = "T_TOC";
            join[18] = campos.getAtributo("tipoOcupacionDOT") + " = " + campos.getAtributo("codTOC");
			join[19] = "INNER";
			join[20] = "E_EXT";
            join[21] = campos.getAtributo("codTerceroEXT")+"="+campos.getAtributo("idTerceroTER")+" AND "+
                       campos.getAtributo("codTerceroEXT")+"="+campos.getAtributo("idTerceroDOT");
            join[22] = "INNER";
            join[23] = "E_CRO";
            join[24] = campos.getAtributo("numeroExpedienteEXT")+"="+campos.getAtributo("numeroExpedienteCRO")+" AND "+
                       campos.getAtributo("codMunicipioEXT")+"="+campos.getAtributo("codMunicipioCRO")+" AND "+
                       campos.getAtributo("codEjercicioEXT")+"="+campos.getAtributo("codEjercicioCRO");

            where = (codPais.equals("")) ? where
                    :where+campos.getAtributo("codPaisDSU")+"="+codPais+ " AND ";
            where = (codProvincia == -1) ? where
                    :where+campos.getAtributo("codProvDSU")+"="+codProvincia+ " AND ";
            where = (codMunicipio == -1) ? where
                    :where+campos.getAtributo("codMunDSU")+"="+codMunicipio+ " AND ";
            where = (numDesde == -1) ? where
                    :where+campos.getAtributo("numDesdeDSU")+"="+numDesde+ " AND ";
            where = (letraDesde ==  null || "".equals(letraDesde)) ? where
                    :where+campos.getAtributo("letraDesdeDSU")+"="+bd.addString(letraDesde)+" AND ";
            where = (numHasta == -1) ? where
                    :where+campos.getAtributo("numHastaDSU")+"="+numHasta+ " AND ";
            where = (letraHasta == null || "".equals(letraHasta)) ? where
                    :where+campos.getAtributo("letraHastaDSU")+"="+bd.addString(letraHasta)+" AND ";

            if (bloque != null && !bloque.equals("")) {
                where += "DSU_BLQ = '" + bloque + "' AND ";
            }

            if (portal != null && !portal.equals("")) {
                where += "DSU_POR = '" + portal + "' AND ";
            }

            if (lugar != null && !lugar.equals("")) {
                where += "DSU_LUG = '" + lugar + "' AND ";
            }

            if (codPostal != null && !codPostal.equals("")) {
                where += "DSU_CPO = '" + codPostal + "' AND ";
            }

            join[25] = "INNER";
            join[26] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
            join[27] = campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMUN");
            join[28] = "INNER";
            join[29] = GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI";
            join[30] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI");
            join[31] = "INNER";
            join[32] = GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV";
            join[33] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV");
            join[34] = "INNER";
            join[35] = "T_VIA";
            join[36] = campos.getAtributo("codViaDSU")+"="+campos.getAtributo("idVIA")+" AND "+
                    campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisVIA")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvVIA")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMunVIA");
			join[37] = "INNER";
            join[38] = GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU";
            join[39] = campos.getAtributo("UorUOU")+"="+campos.getAtributo("UtrCRO");
            where = (codVia == -1) ? where:where+campos.getAtributo("codVIA")+"="+codVia+ " AND ";
            where = (descVia == null || "".equals(descVia)) ? where:where+campos.getAtributo("descVIA")+" LIKE '%"+TransformacionAtributoSelect.replace(descVia,"'","''")+ "%' AND ";

            where = (codEco == -1) ? where: where+campos.getAtributo("codECO")+"="+codEco+ " AND ";
            where = (codEsi == -1) ? where: where+campos.getAtributo("codESI")+"="+codEsi+ " AND ";

            where += campos.getAtributo("situacionDSU")+"='A'";
            join[40] = "INNER";
            join[41] = "T_TVI";
            join[42] = campos.getAtributo("tipoVIA")+"="+campos.getAtributo("codTVI");
            join[43] = "LEFT";
            join[44] = "T_ESI";
            join[45] = campos.getAtributo("codigoESI")+"="+campos.getAtributo("codESI");
            join[46] = "LEFT";
            join[47] = "T_ECO";
            join[48] = campos.getAtributo("codECOESI")+"="+campos.getAtributo("codECO");
            join[49] =  "false";
            sql = sql + " UNION " + bd.join(from,where,join);





            // CUARTA PARTE DE LA UNION (DOMICILIOS NORMALIZADOS) TERCEROS DE EXPEDIENTES CON UOR DE INCIO IGUAL A LA DEL USUARIO
            join = new String[50];

            from =  "DISTINCT TER_COD,TER_TID,TER_DOC,TER_NOM,TER_AP1,TER_PA1,TER_AP2,TER_PA2,TER_NOC,TER_NML,TER_TLF,TER_DCE,TER_SIT,TER_NVE,TER_FAL,TER_UAL,TER_APL,TER_FBJ,TER_UBJ,TER_DOM,T_DOM.*,"+
                    campos.getAtributo("codTVI")+","+
                    campos.getAtributo("descTVI")+"," +
                    campos.getAtributo("descESI")+"," +
                    campos.getAtributo("idVIA")+","+
                    campos.getAtributo("codVIA")+","+
                    campos.getAtributo("descVIA")+","+
                    campos.getAtributo("descVIA")+"," +
                    campos.getAtributo("numDesdeDSU")+","+
                    campos.getAtributo("letraDesdeDSU")+","+
                    campos.getAtributo("numHastaDSU")+","+
                    campos.getAtributo("letraHastaDSU")+"," +
                    campos.getAtributo("bloqueDSU")+","+
                    campos.getAtributo("portalDSU")+","+
                    campos.getAtributo("escaleraDPO")+","+
                    campos.getAtributo("plantaDPO")+"," +
                    campos.getAtributo("puertaDPO")+","+
                    campos.getAtributo("codPostalDSU")+","+
                    "("+  bd.funcionCadena(bd.FUNCIONCADENA_CONCAT,new String[]{(String) campos.getAtributo("descESI"),"' - '",(String) campos.getAtributo("descECO")}) +"), " +
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA")+
                    ","+campos.getAtributo("codECO") + "," + campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");

            where = "";

            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    where += condicion +" AND ";
                }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        where += condicion + " AND ";
                    }*/
                     where += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }

            if (name != null) {
                if (!"".equals(name.trim())) {
                    String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                    String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                where += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                where += "TER_DCE = '" + email + "' AND ";
            }
            if (codUsuario != -1) {
                where += "UOU_USU = " + codUsuario + " AND ";
            }
             if (codEntidad != -1) {
                where += "UOU_ENT = " + codEntidad + " AND ";
            }
            if (codOrganizacion != -1) {
                where += "UOU_ORG = " + codOrganizacion + " AND ";
            }

            where+= campos.getAtributo("situacionTER")+"='A' AND "+
                    campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("normalizadoDOM")+"=1 AND ";

            join[0] = "T_TER";
            join[1] = "INNER";
            join[2] = "T_TID";
            join[3] = campos.getAtributo("tipoDocTER")+"="+campos.getAtributo("codTID");
            join[4] = "INNER";
            join[5] = "T_DOT";
            join[6] = campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT");
            join[7] = "INNER";
            join[8] = "T_DOM";
            join[9] = campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM");
            join[10] = "INNER";
            join[11] = "T_DPO";
            join[12] = campos.getAtributo("idDomicilioDOM")+"="+campos.getAtributo("idDomicilioDPO");
            join[13] = "INNER";
            join[14] = "T_DSU";
            join[15] = campos.getAtributo("dirSueloDPO")+"="+campos.getAtributo("codDirSueloDSU");
            join[16] = "LEFT";
            join[17] = "T_TOC";
            join[18] = campos.getAtributo("tipoOcupacionDOT") + " = " + campos.getAtributo("codTOC");
			join[19] = "INNER";
            join[20] = "E_EXT";
            join[21] = campos.getAtributo("codTerceroEXT")+"="+campos.getAtributo("idTerceroTER")+" AND "+
                       campos.getAtributo("codTerceroEXT")+"="+campos.getAtributo("idTerceroDOT");
            join[22] = "INNER";
            join[23] = "E_EXP";
            join[24] = campos.getAtributo("numeroExpedienteEXT")+"="+campos.getAtributo("numeroExpedienteEXP")+" AND "+
                       campos.getAtributo("codMunicipioEXT")+"="+campos.getAtributo("codMunicipioEXP")+" AND "+
                       campos.getAtributo("codEjercicioEXT")+"="+campos.getAtributo("codEjercicioEXP");

            where = (codPais.equals("")) ? where
                    :where+campos.getAtributo("codPaisDSU")+"="+codPais+ " AND ";
            where = (codProvincia == -1) ? where
                    :where+campos.getAtributo("codProvDSU")+"="+codProvincia+ " AND ";
            where = (codMunicipio == -1) ? where
                    :where+campos.getAtributo("codMunDSU")+"="+codMunicipio+ " AND ";
            where = (numDesde == -1) ? where
                    :where+campos.getAtributo("numDesdeDSU")+"="+numDesde+ " AND ";
            where = (letraDesde ==  null || "".equals(letraDesde)) ? where
                    :where+campos.getAtributo("letraDesdeDSU")+"="+bd.addString(letraDesde)+" AND ";
            where = (numHasta == -1) ? where
                    :where+campos.getAtributo("numHastaDSU")+"="+numHasta+ " AND ";
            where = (letraHasta == null || "".equals(letraHasta)) ? where
                    :where+campos.getAtributo("letraHastaDSU")+"="+bd.addString(letraHasta)+" AND ";

            if (bloque != null && !bloque.equals("")) {
                where += "DSU_BLQ = '" + bloque + "' AND ";
            }

            if (portal != null && !portal.equals("")) {
                where += "DSU_POR = '" + portal + "' AND ";
            }

            if (lugar != null && !lugar.equals("")) {
                where += "DSU_LUG = '" + lugar + "' AND ";
            }

            if (codPostal != null && !codPostal.equals("")) {
                where += "DSU_CPO = '" + codPostal + "' AND ";
            }

            join[25] = "INNER";
            join[26] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
            join[27] = campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMUN");
            join[28] = "INNER";
            join[29] = GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI";
            join[30] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI");
            join[31] = "INNER";
            join[32] = GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV";
            join[33] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV");
            join[34] = "INNER";
            join[35] = "T_VIA";
            join[36] = campos.getAtributo("codViaDSU")+"="+campos.getAtributo("idVIA")+" AND "+
                    campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisVIA")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvVIA")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMunVIA");
			join[37] = "INNER";
            join[38] = GlobalNames.ESQUEMA_GENERICO + "A_UOU A_UOU";
            join[39] = campos.getAtributo("UorUOU")+"="+campos.getAtributo("UorEXP");
            where = (codVia == -1) ? where:where+campos.getAtributo("codVIA")+"="+codVia+ " AND ";
            where = (descVia == null || "".equals(descVia)) ? where:where+campos.getAtributo("descVIA")+" LIKE '%"+TransformacionAtributoSelect.replace(descVia,"'","''")+ "%' AND ";

            where = (codEco == -1) ? where: where+campos.getAtributo("codECO")+"="+codEco+ " AND ";
            where = (codEsi == -1) ? where: where+campos.getAtributo("codESI")+"="+codEsi+ " AND ";

            where += campos.getAtributo("situacionDSU")+"='A'";
            join[40] = "INNER";
            join[41] = "T_TVI";
            join[42] = campos.getAtributo("tipoVIA")+"="+campos.getAtributo("codTVI");
            join[43] = "LEFT";
            join[44] = "T_ESI";
            join[45] = campos.getAtributo("codigoESI")+"="+campos.getAtributo("codESI");
            join[46] = "LEFT";
            join[47] = "T_ECO";
            join[48] = campos.getAtributo("codECOESI")+"="+campos.getAtributo("codECO");
            join[49] =  "false";
            sql = sql + " UNION " + bd.join(from,where,join);
            sql = sql + "ORDER BY TER_AP1, TER_COD";

            if(m_Log.isDebugEnabled()) m_Log.debug("CONSULTA DE BUSQUEDA DE TERCEROS"+sql);




            long tiempoInicio = System.currentTimeMillis();

             if(consultaInsensitiva){
                String alter1=null;
                String alter2=null;
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    m_Log.debug("ALTER SESSION SET LINGUISTIC/GENERICBASELETTER");

                    state.executeQuery(alter1);
                    state.executeQuery(alter2);

                } 
            }

            rs = state.executeQuery(sql);
            long totalTiempo = System.currentTimeMillis() - tiempoInicio;
m_Log.debug("El tiempo de demora es :" + totalTiempo + " miliseg");
            int actual;
            int ultimo=-1;
            DomicilioSimpleValueObject nuevo;
            Vector<DomicilioSimpleValueObject> domicilios = null;
            TercerosValueObject terceroActual;
            TercerosValueObject terceroUltimo = null;

            // OBTENER RESULTADOS DE LA CONSULTA
            while(rs.next()){
                actual=rs.getInt((String)campos.getAtributo("idTerceroTER"));
                if (actual==ultimo){
                    String descdmc = rs.getString("DESCDMC");
                    if (descdmc== null) descdmc ="";
                    nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                            rs.getString((String)campos.getAtributo("codPAI")),
                            rs.getString((String)campos.getAtributo("codPRV")),
                            rs.getString((String)campos.getAtributo("codMUN")),
                            rs.getString((String)campos.getAtributo("descPAI")),
                            rs.getString((String)campos.getAtributo("descPRV")),
                            rs.getString((String)campos.getAtributo("descMUN")),
                            TransformacionAtributoSelect.replace(descdmc,"\"","\\\""),rs.getString("CPO"));
                    nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                    nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                    nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                    nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                    nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                    if(rs.getString("CODVIA")!=null){
                        nuevo.setDescVia(rs.getString("DESCVIA"));
                        nuevo.setCodigoVia(rs.getString("CODVIA"));
                        nuevo.setIdVia(rs.getString("IDVIA"));
                        if(rs.getString("CODVIA").equals("0")) {
                        }
                    }
                    nuevo.setNumDesde(rs.getString("NUD"));
                    nuevo.setLetraDesde(rs.getString("LED"));
                    nuevo.setNumHasta(rs.getString("NUH"));
                    nuevo.setLetraHasta(rs.getString("LEH"));
                    nuevo.setBloque(rs.getString("BLQ"));
                    nuevo.setPortal(rs.getString("POR"));
                    nuevo.setEscalera(rs.getString("ESC"));
                    nuevo.setPlanta(rs.getString("PLT"));
                    nuevo.setPuerta(rs.getString("PTA"));
                    nuevo.setBarriada(rs.getString("CAS"));
                    nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                    nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                    nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                    /* anadir ECOESI */
                    nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                    nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                    nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                    /* fin anadir ECOESI */
                    domicilios.add(nuevo);
                }else{
                    if (terceroUltimo!=null){
                        terceroUltimo.setDomicilios(domicilios);
                        resultado.add(terceroUltimo);
                    }
                    terceroActual = new TercerosValueObject(
                            String.valueOf(actual),
                            rs.getString((String)campos.getAtributo("versionTER")),
                            rs.getString((String)campos.getAtributo("tipoDocTER")),
                            "",
                            rs.getString((String)campos.getAtributo("documentoTER")),
                            rs.getString((String)campos.getAtributo("nombreTER")),
                            rs.getString((String)campos.getAtributo("apellido1TER")),
                            rs.getString((String)campos.getAtributo("pApellido1TER")),
                            rs.getString((String)campos.getAtributo("apellido2TER")),
                            rs.getString((String)campos.getAtributo("pApellido2TER")),
                            rs.getString((String)campos.getAtributo("normalizadoTER")),
                            rs.getString((String)campos.getAtributo("telefonoTER")),
                            rs.getString((String)campos.getAtributo("emailTER")),
                            rs.getString((String)campos.getAtributo("situacionTER")).charAt(0),
                            rs.getString((String)campos.getAtributo("fechaAltaTER")),
                            rs.getString((String)campos.getAtributo("usuarioAltaTER")),
                            rs.getString((String)campos.getAtributo("moduloAltaTER")),
                            rs.getString((String)campos.getAtributo("fechaBajaTER")),
                            rs.getString((String)campos.getAtributo("usuarioBajaTER")));

                    terceroActual.setSituacion(rs.getString((String)campos.getAtributo("situacionTER")).charAt(0));
                    terceroActual.setDomPrincipal(rs.getString("TER_DOM"));
                    domicilios = new Vector<DomicilioSimpleValueObject>();
                    if (rs.getInt((String)campos.getAtributo("idDomicilioDOM"))>0){
                        nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                                rs.getString((String)campos.getAtributo("codPAI")),
                                rs.getString((String)campos.getAtributo("codPRV")),
                                rs.getString((String)campos.getAtributo("codMUN")),
                                rs.getString((String)campos.getAtributo("descPAI")),
                                rs.getString((String)campos.getAtributo("descPRV")),
                                rs.getString((String)campos.getAtributo("descMUN")),
                                TransformacionAtributoSelect.replace(rs.getString("DESCDMC"),"\"","\\\""),rs.getString("CPO"));
                        nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                        nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                        nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                        nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                        nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                        if(rs.getString("CODVIA")!=null){
                            nuevo.setDescVia(rs.getString("DESCVIA"));
                            nuevo.setCodigoVia(rs.getString("CODVIA"));
                            nuevo.setIdVia(rs.getString("IDVIA"));
                            if(rs.getString("CODVIA").equals("0")) {
                            }
                        }
                        nuevo.setNumDesde(rs.getString("NUD"));
                        nuevo.setLetraDesde(rs.getString("LED"));
                        nuevo.setNumHasta(rs.getString("NUH"));
                        nuevo.setLetraHasta(rs.getString("LEH"));
                        nuevo.setBloque(rs.getString("BLQ"));
                        nuevo.setPortal(rs.getString("POR"));
                        nuevo.setEscalera(rs.getString("ESC"));
                        nuevo.setPlanta(rs.getString("PLT"));
                        nuevo.setPuerta(rs.getString("PTA"));
                        nuevo.setBarriada(rs.getString("CAS"));
                        nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                        nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                        nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                        /* anadir ECOESI */
                        nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                        nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                        nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                        /* fin anadir ECOESI */

                        domicilios.add(nuevo);
                    }
                    terceroUltimo=terceroActual;
                    ultimo=actual;
                }
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            if (terceroUltimo!=null){
                terceroUltimo.setDomicilios(domicilios);
                resultado.add(terceroUltimo);
            }


            // COMPARACIÓN Q NO VA NINGÚN PARÁMETRO DE CONSULTA DE DOMICILIO

            m_Log.debug("PROVINCIA ................  "+codProvincia);
            m_Log.debug("MUNICIPIO ................  "+codMunicipio);
            m_Log.debug("NUM DESDE ................  "+numDesde);
            m_Log.debug("LETRA DESDE ..............  "+letraDesde);
            m_Log.debug("NUM HASTA ................  "+numHasta);
            m_Log.debug("LETRA HASTA...............  "+letraHasta);
            m_Log.debug("DESC VIA .................  "+descVia);
            m_Log.debug("COD VIA ..................  "+codVia);
            m_Log.debug("COD ECO ..................  "+codEco);
            m_Log.debug("COD ESI ..................  "+codEsi);

            if (codProvincia == -1 && codMunicipio == -1 && numDesde == -1 && (letraDesde == null || "".equals(letraDesde)) &&
                    numHasta == -1 && (letraHasta == null || "".equals(letraHasta)) && codVia == -1 && codEco == -1 &&
                    codEsi == -1) {

            // Terceros q no tienen domicilio SÓLO SE AÑADEN A LOS ANTERIORES cuando la consulta no tiene ningún parámetro
            sql = "SELECT T_TER.* FROM T_TER WHERE "+ campos.getAtributo("situacionTER")+"='A' AND ";

            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    sql += condicion +" AND ";
                }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        sql += condicion + " AND ";
                    }*/
                    sql += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }
            if (name != null) {
                if (!"".equals(name.trim())) {
                    String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sql += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sql += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                   String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sql += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                sql += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                sql += "TER_DCE = '" + email + "' AND ";
            }

            int longitud =sql.length()-5;
            if(sql.endsWith(" AND "))
                sql = sql.substring(0,longitud);
            if(sql.endsWith(" WHERE "))
                sql = sql.substring(0,sql.length()-7);

            String sqlMinus = "";

            sqlMinus += "SELECT T_TER.* FROM T_TER,T_DOT WHERE ";
            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    sqlMinus += condicion +" AND ";
                }
             if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        sqlMinus += condicion + " AND ";
                    }*/
                    sqlMinus += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }
            if (name != null) {
                if (!"".equals(name.trim())) {
                    String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sqlMinus += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sqlMinus += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                    String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sqlMinus += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                sqlMinus += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                sqlMinus += "TER_DCE = '" + email + "' AND ";
            }

            sqlMinus += campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT");

            sql = sql + bd.minus(sql,sqlMinus);

            if(m_Log.isDebugEnabled()) {
                m_Log.debug("No tienen domicilio: solo se añaden cuando en la consutla no va ningún parámetro");
                m_Log.debug(sql);
            }
            state = con.createStatement();
            
            if(consultaInsensitiva){
                String alter1=null;
                String alter2=null;
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    m_Log.debug("ALTER SESSION SET LINGUISTIC/GENERICBASELETTER");

                    state.executeQuery(alter1);
                    state.executeQuery(alter2);

                } 
            }

            rs = state.executeQuery(sql);
            TercerosValueObject tercero = null;
            while(rs.next()){
                tercero = new TercerosValueObject(
                        rs.getString((String)campos.getAtributo("idTerceroTER")),
                        rs.getString((String)campos.getAtributo("versionTER")),
                        rs.getString((String)campos.getAtributo("tipoDocTER")),
                        "",
                        rs.getString((String)campos.getAtributo("documentoTER")),
                        rs.getString((String)campos.getAtributo("nombreTER")),
                        rs.getString((String)campos.getAtributo("apellido1TER")),
                        rs.getString((String)campos.getAtributo("pApellido1TER")),
                        rs.getString((String)campos.getAtributo("apellido2TER")),
                        rs.getString((String)campos.getAtributo("pApellido2TER")),
                        rs.getString((String)campos.getAtributo("normalizadoTER")),
                        rs.getString((String)campos.getAtributo("telefonoTER")),
                        rs.getString((String)campos.getAtributo("emailTER")),
                        rs.getString((String)campos.getAtributo("situacionTER")).charAt(0),
                        rs.getString((String)campos.getAtributo("fechaAltaTER")),
                        rs.getString((String)campos.getAtributo("usuarioAltaTER")),
                        rs.getString((String)campos.getAtributo("moduloAltaTER")),
                        rs.getString((String)campos.getAtributo("fechaBajaTER")),
                        rs.getString((String)campos.getAtributo("usuarioBajaTER")));
                tercero.setSituacion(rs.getString((String)campos.getAtributo("situacionTER")).charAt(0));
                tercero.setDomicilios(new Vector());
                resultado.add(tercero);
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
         }


            // fin COMPARACIÓN Q NO VA NINGÚN PARÁMETRO DE CONSULTA DE DOMICILIO
        } catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            
            try{
                if (consultaInsensitiva){
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                       String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                       String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                       m_Log.debug("ALTER SESSION SET BINARY/SPANISH");

                       state=con.createStatement();
                       state.executeQuery(alter1);
                       SigpGeneralOperations.closeStatement(state);
                       state=con.createStatement();
                       state.executeQuery(alter2);
                       SigpGeneralOperations.closeStatement(state);
                   } 
                }
            }catch (SQLException se){
                if(m_Log.isErrorEnabled()) m_Log.error(se.getMessage());
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return resultado;
    }


    public Vector getTerceroUOR(CondicionesBusquedaTerceroVO condsBusqueda, String[] params) throws TechnicalException{
        String sql;
        Vector<TercerosValueObject> resultado = new Vector<TercerosValueObject>();
        Vector<DomicilioSimpleValueObject> domicilios = new Vector<DomicilioSimpleValueObject>();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs = null;
        Statement state = null;
        TransformacionAtributoSelect transformador;
        try{
            bd = new AdaptadorSQLBD(params);
            transformador = new TransformacionAtributoSelect(bd);
            con = bd.getConnection();
            state = con.createStatement();

            String tipoDoc = String.valueOf(condsBusqueda.getTipoDocumento());
            String docu = condsBusqueda.getDocumento();
            String name = condsBusqueda.getNombre();
              m_Log.debug("NAME "+name);

            if ((name==null || name.equals("")) && !tipoDoc.equals("7")) return resultado;

                sql = "select UOR_COD_VIS, UOR_NOM, UOR_EMAIL from A_UOR where  UOR_ESTADO='A' ";

                if (docu != null) {
                    if (!docu.equals("")) sql += "AND UOR_COD_VIS like '" + docu + "' ";
                }
                if (name != null) {
                    if (!"".equals(name.trim())) {
                        String condicion = transformador.construirCondicionWhereConOperadores("UOR_NOM", name.trim(),false);
                        sql += "AND" + condicion;
                    }
                }
                 sql += " ORDER BY UOR_NOM,UOR_COD_VIS";
                m_Log.debug(sql);
                rs = state.executeQuery(sql);
                TercerosValueObject tercero = null;
                while (rs.next()) {
                    tercero = new TercerosValueObject("0", "1", m_Common.getString("tercero.codUOR"),
                    "", rs.getString(1), rs.getString(2), "", "", "", "", "2", "", "", 'A', null,
                    "5", "1", null, null);
                    tercero.setOrigen("UOR");
                    tercero.setEmail(rs.getString(3));
                DomicilioSimpleValueObject nuevo = new DomicilioSimpleValueObject("1", "108","99","999", "ESPAÑA", "DESCONOCIDA", "DESCONOCIDO", null,null);

                nuevo.setIdTipoVia("18");
                nuevo.setTipoVia("CALLE");
                nuevo.setIdPaisVia("108");
                nuevo.setIdProvinciaVia("99");
                nuevo.setIdMunicipioVia("999");
                nuevo.setDescVia("POR DEFECTO");
                nuevo.setCodigoVia("18");
                nuevo.setIdVia("18");

                nuevo.setOrigen("UOR");
                domicilios.add(nuevo);
                    tercero.setDomicilios(domicilios);
                    resultado.add(tercero);
                }
                cerrarResultSet(rs);
                cerrarStatement(state);
        } catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return resultado;
    }

    public Vector getTerceroHistorico(GeneralValueObject terVO, String[] params)
            throws TechnicalException{
        String sql= "";
        String sqlInterno="";
        Vector resultado=new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs = null;
        Statement state = null;
        TransformacionAtributoSelect transformador;
        try{
            bd = new AdaptadorSQLBD(params);
            transformador = new TransformacionAtributoSelect(bd);
            con = bd.getConnection();
            state = con.createStatement();
            String tipoDoc = (String)terVO.getAtributo("tipoDocumento");
            String docu = (String)terVO.getAtributo("documento");
            String name = (String)terVO.getAtributo("nombre");
            String apell1 = (String)terVO.getAtributo("apellido1");
            String apell2 = (String)terVO.getAtributo("apellido2");
            String codPais = (String)campos.getAtributo("codigoPais");
            String codProvincia = (String)terVO.getAtributo("codProvincia");
            String codMunicipio = (String)terVO.getAtributo("codMunicipio");
            String codVia = (String)terVO.getAtributo("codVia");
            String idVia = (String)terVO.getAtributo("idVia");
            if (terVO.getAtributo("idVia")==null)
                idVia = "";
            String casa = (String)terVO.getAtributo("domicilio");
            String numDesde = (String)terVO.getAtributo("numDesde");
            String letraDesde = (String)terVO.getAtributo("letraDesde");
            String numHasta = (String)terVO.getAtributo("numHasta");
            String letraHasta = (String)terVO.getAtributo("letraHasta");
            String codTerc = (String) terVO.getAtributo("codTerc");
            String versTerc = (String) terVO.getAtributo("versTerc");
            String codDom = (String) terVO.getAtributo("codDom");
            String codEsi = (String) terVO.getAtributo("codEsi");
            String codEco = (String) terVO.getAtributo("codEco");
            String from ="";
            String where="";


            String []joinInterno = new String [5];
            joinInterno[0]="T_DOT";
            joinInterno[1]="LEFT";
            joinInterno[2]="T_TOC";
            joinInterno[3]="DOT_TOC=TOC_COD";
            joinInterno[4]="true";

            sqlInterno = bd.join("","",joinInterno);

            from = "T_HTE.*, T_DOM.*, " + campos.getAtributo("codTVI") + " AS CODTIPOVIA, " +
                    campos.getAtributo("descTVI")+" AS DESCTIPOVIA," +
                    campos.getAtributo("descESI") +" AS LUGAR," +
                    campos.getAtributo("idVIA")+" AS IDVIA,"+
                    campos.getAtributo("codVIA")+" AS CODVIA,"+
                    campos.getAtributo("descVIA")+" AS DESCVIA,"+
                    campos.getAtributo("domicilioDNN")+" AS DESCDMC," +
                    campos.getAtributo("numDesdeDNN")+" AS NUD,"+
                    campos.getAtributo("letraDesdeDNN")+" AS LED,"+
                    campos.getAtributo("numHastaDNN")+" AS NUH,"+
                    campos.getAtributo("letraHastaDNN")+" AS LEH," +
                    campos.getAtributo("bloqueDNN")+" AS BLQ,"+
                    campos.getAtributo("portalDNN")+" AS POR,"+
                    campos.getAtributo("escaleraDNN")+" AS ESC,"+
                    campos.getAtributo("plantaDNN")+" AS PLT," +
                    campos.getAtributo("puertaDNN")+" AS PTA,"+
                    campos.getAtributo("codPostalDNN")+" AS CPO,"+
                    campos.getAtributo("barriadaDNN")+" AS CAS,"+
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
            campos.getAtributo("codMunVIA") + ","+campos.getAtributo("codECO") + "," + campos.getAtributo("codESI")+ ","
            + campos.getAtributo("descESI");

            String []join= new String [14];
            join[0]= sqlInterno + " , T_HTE, T_DOM, T_TID, "+GlobalNames.ESQUEMA_GENERICO+ "T_PAI T_PAI,"+GlobalNames.ESQUEMA_GENERICO+
            	"T_PRV T_PRV,"+ GlobalNames.ESQUEMA_GENERICO+ "T_MUN T_MUN, T_DNN ";
            join[1]="LEFT";
            join[2]="T_VIA";
            join[3]="DNN_VIA=VIA_COD AND DNN_VPA=VIA_PAI AND DNN_VPR=VIA_PRV AND DNN_VMU=VIA_MUN";
            join[4]="LEFT";
            join[5]="T_TVI";
            join[6]="DNN_TVI=TVI_COD";
            join[7]="LEFT";
            join[8]="T_ESI";
            join[9]="DNN_ESI=ESI_COD";
            join[10]="LEFT";
            join[11]="T_ECO";
            join[12]="ESI_ECO=ECO_COD";
            join[13]="false";

            where = (codTerc != null && "".equals(codTerc)) ? where:where+campos.getAtributo("idTerceroHTE") + " = " + codTerc+" AND ";
            where = (versTerc != null && "".equals(versTerc)) ?where:where+campos.getAtributo("versionHTE") + " = " + versTerc+" AND ";
            where = (codDom != null && "".equals(codDom)) ? where:where+campos.getAtributo("idDomicilioDOT") + " = " + codDom+" AND ";
            if (tipoDoc != null) {
                if (!"".equals(tipoDoc.trim())){
                    String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocHTE"), tipoDoc.trim());
                    where += condicion +" AND ";
                }
            }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                    String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoHTE"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        where += condicion + " AND ";
                    }
                }
            }
            if (name != null) {
                if (!"".equals(name.trim())) {
                    String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("nombreHTE"), name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("apellido1HTE"), apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                    String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("apellido2HTE"), apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            where+= campos.getAtributo("tipoDocHTE")+"="+campos.getAtributo("codTID")+" AND "+
                    campos.getAtributo("idTerceroHTE")+"="+campos.getAtributo("idTerceroDOT")+" AND "+
                    campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM")+" AND "+
                    campos.getAtributo("normalizadoDOM")+"=2 AND "+
                    campos.getAtributo("idDomicilioDOM")+"="+campos.getAtributo("idDomicilioDNN")+" AND ";

            where = (codPais.equals("")) ? where
                    :where+campos.getAtributo("codPaisDNN")+"="+codPais+ " AND ";
            where = (codProvincia.equals("")) ? where
                    :where+campos.getAtributo("codProvDNN")+"="+codProvincia+ " AND ";
            where = (codMunicipio.equals("")) ? where
                    :where+campos.getAtributo("codMunDNN")+"="+codMunicipio+ " AND ";
            where = (numDesde.equals("")) ? where
                    :where+campos.getAtributo("numDesdeDNN")+"="+numDesde+ " AND ";
            where = (letraDesde.equals("")) ? where
                    :where+campos.getAtributo("letraDesdeDNN")+"="+bd.addString(letraDesde)+" AND ";
            where = (numHasta.equals("")) ? where
                    :where+campos.getAtributo("numHastaDNN")+"="+numHasta+ " AND ";
            where = (letraHasta.equals("")) ? where
                    :where+campos.getAtributo("letraHastaDNN")+"="+bd.addString(letraHasta)+" AND ";
            where = (casa.equals("")||!codVia.equals("")) ? where
                    :where+campos.getAtributo("domicilioDNN")+" LIKE '"+TransformacionAtributoSelect.replace(casa,"'","''")+ "%' AND ";
            where+= campos.getAtributo("codPaisDNN")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDNN")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDNN")+"="+campos.getAtributo("codMUN")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV");
            where = (codVia.equals("")) ? where:where+ " AND "+campos.getAtributo("codVIA")+"="+codVia;
            where = (idVia.equals("")) ? where:where+ " AND "+campos.getAtributo("idVIA")+"="+idVia+ " AND ";
            where = (codEco == null || "".equals(codEco)) ? where:where + " AND "+ campos.getAtributo("codECO")+"="+codEco+ " AND ";
            where = (codEsi == null || "".equals(codEsi)) ? where:where + " AND "+ campos.getAtributo("codESI")+"="+codEsi;

            sql=bd.join (from, where, join);

            from= where = "";

            from =  " T_HTE.*,T_DOM.*,"+
                    campos.getAtributo("codTVI")+","+
                    campos.getAtributo("descTVI")+"," +
                    campos.getAtributo("descESI")+"," +
                    campos.getAtributo("idVIA")+","+
                    campos.getAtributo("codVIA")+","+
                    campos.getAtributo("descVIA")+","+
                    campos.getAtributo("descVIA")+"," +
                    campos.getAtributo("numDesdeDSU")+","+
                    campos.getAtributo("letraDesdeDSU")+","+
                    campos.getAtributo("numHastaDSU")+","+
                    campos.getAtributo("letraHastaDSU")+"," +
                    campos.getAtributo("bloqueDSU")+","+
                    campos.getAtributo("portalDSU")+","+
                    campos.getAtributo("escaleraDPO")+","+
                    campos.getAtributo("plantaDPO")+"," +
                    campos.getAtributo("puertaDPO")+","+
                    campos.getAtributo("codPostalDSU")+","+
                    "("+  bd.funcionCadena(bd.FUNCIONCADENA_CONCAT,new String[]{(String) campos.getAtributo("descESI"),"' - '",(String) campos.getAtributo("descECO")}) +"), "+
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA")+
                    /* anadir ECOESI */
                    ","+campos.getAtributo("codECO") + "," + campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");



            String []aux = new String [8];
            aux[0]= sqlInterno + ", " + " T_HTE, "+GlobalNames.ESQUEMA_GENERICO+ "T_PAI T_PAI,"+GlobalNames.ESQUEMA_GENERICO+
            "T_PRV T_PRV,"+ GlobalNames.ESQUEMA_GENERICO+ "T_MUN T_MUN, T_DPO, T_TID, T_VIA, T_DOM, T_TVI, T_ESI ";
            aux[1]="LEFT";
            aux[2]="T_ECO";
            aux[3]="ECO_COD=ESI_ECO";
            aux[4]="LEFT";
            aux[5]="T_DSU";
            aux[6]="ESI_COD=DSU_ESI";
            aux[7]="false";

	        where = "";

            where = (codTerc !=null && "".equals(codTerc)) ? where:where+campos.getAtributo("idTerceroHTE") + " = " + codTerc + " AND ";
            where = (versTerc != null && "".equals(versTerc)) ? where:where+campos.getAtributo("versionHTE") + " = " + versTerc + " AND ";
            where = (codDom != null && codDom.equals("")) ? where:where+campos.getAtributo("idDomicilioDOM") + " = " + codDom + " AND ";
            if (tipoDoc != null) {
                if (!"".equals(tipoDoc.trim())){
                    String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), tipoDoc.trim());
                    where += condicion +" AND ";
                }
            }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                    String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoHTE"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                    	where += condicion + " AND ";
                    }
                }
            }
            if (name != null) {
                if (!"".equals(name.trim())) {
                    String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("nombreHTE"), name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                    	where += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("apellido1HTE"), apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                    	where += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                    consultaInsensitiva=true;
                    String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("apellido2HTE"), apell2.trim(),false);
                    if (!"".equals(condicion)) {
                    	where += condicion + " AND ";
                    }
                }
            }
            where+= campos.getAtributo("tipoDocHTE")+"="+campos.getAtributo("codTID")+" AND "+
                    campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("idTerceroHTE")+"="+campos.getAtributo("idTerceroDOT")+" AND "+
                    campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM")+" AND "+
                    campos.getAtributo("normalizadoDOM")+"=1 AND "+
                    campos.getAtributo("idDomicilioDOM")+"="+campos.getAtributo("idDomicilioDPO")+" AND " +
                    campos.getAtributo("dirSueloDPO")+"="+ campos.getAtributo("codDirSueloDSU") + " AND ";

                    where = (codPais.equals("")) ? where
                    :where+campos.getAtributo("codPaisDSU")+"="+codPais+ " AND ";

                    where = (codProvincia.equals("")) ? where
                    :where+campos.getAtributo("codProvDSU")+"="+codProvincia+ " AND ";

                    where = (codMunicipio.equals("")) ? where
                    :where+campos.getAtributo("codMunDSU")+"="+codMunicipio+ " AND ";

                    where = (numDesde.equals("")) ? where
                    :where+campos.getAtributo("numDesdeDSU")+"="+numDesde+ " AND ";

                    where = (letraDesde.equals("")) ? where
                    :where+campos.getAtributo("letraDesdeDSU")+"="+bd.addString(letraDesde)+" AND ";

                    where = (numHasta.equals("")) ? where
                    :where+campos.getAtributo("numHastaDSU")+"="+numHasta+ " AND ";

                    where = (letraHasta.equals("")) ? where
                    :where+campos.getAtributo("letraHastaDSU")+"="+bd.addString(letraHasta)+" AND ";

                    where+= campos.getAtributo("situacionDSU")+"='A' AND " +
                    campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMUN")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV")+" AND "+
                    campos.getAtributo("codViaDSU")+"="+campos.getAtributo("idVIA")+" AND "+
                    campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisVIA")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvVIA")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMunVIA")+" AND ";
                    where = (codVia.equals("")) ? where:where+campos.getAtributo("codVIA")+"="+codVia+ " AND ";
                    where = (idVia.equals("")) ? where:where+campos.getAtributo("idVIA")+"="+idVia+ " AND ";

                    where = ("".equals(codEco)) ? where
                    :where+campos.getAtributo("codECO")+"="+codEco+ " AND ";
                    where = ("".equals(codEsi)) ? where
                    :where+campos.getAtributo("codESI")+"="+codEsi+ " AND ";
                    where+= campos.getAtributo("tipoVIA")+"="+campos.getAtributo("codTVI");

                    sql = sql + " UNION " + bd.join(from, where, aux);


            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            if(consultaInsensitiva){
                String alter1=null;
                String alter2=null;
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    m_Log.debug("ALTER SESSION SET LINGUISTIC/GENERICBASELETTER");

                    state.executeQuery(alter1);
                    state.executeQuery(alter2);

                } 
            }
            
            rs = state.executeQuery(sql);
            int actual;
            int ultimo=-1;
            DomicilioSimpleValueObject nuevo;
            Vector domicilios = null;
            TercerosValueObject terceroActual = null;
            TercerosValueObject terceroUltimo = null;
            while(rs.next()){
                actual=rs.getInt((String)campos.getAtributo("idTerceroHTE"));
                if (actual==ultimo){
                    String descdmc = rs.getString("DESCDMC");
                    if (descdmc== null) descdmc ="";
                    nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                            rs.getString((String)campos.getAtributo("codPAI")),
                            rs.getString((String)campos.getAtributo("codPRV")),
                            rs.getString((String)campos.getAtributo("codMUN")),
                            rs.getString((String)campos.getAtributo("descPAI")),
                            rs.getString((String)campos.getAtributo("descPRV")),
                            rs.getString((String)campos.getAtributo("descMUN")),
                            TransformacionAtributoSelect.replace(descdmc,"\"","\\\""),rs.getString("CPO"));
                    nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                    nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                    nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                    nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                    nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                    if(rs.getString("CODVIA")!=null){
                        nuevo.setDescVia(rs.getString("DESCVIA"));
                        nuevo.setCodigoVia(rs.getString("CODVIA"));
                        nuevo.setIdVia(rs.getString("IDVIA"));
                        if(rs.getString("CODVIA").equals("0")) {
                        }
                    }
                    nuevo.setNumDesde(rs.getString("NUD"));
                    nuevo.setLetraDesde(rs.getString("LED"));
                    nuevo.setNumHasta(rs.getString("NUH"));
                    nuevo.setLetraHasta(rs.getString("LEH"));
                    nuevo.setBloque(rs.getString("BLQ"));
                    nuevo.setPortal(rs.getString("POR"));
                    nuevo.setEscalera(rs.getString("ESC"));
                    nuevo.setPlanta(rs.getString("PLT"));
                    nuevo.setPuerta(rs.getString("PTA"));
                    nuevo.setBarriada(rs.getString("CAS"));
                    nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                    nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                    nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                    /* anadir ECOESI */
                    nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                    nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                    nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                    /* fin anadir ECOESI */
                    domicilios.add(nuevo);
                }else{
                    if (terceroUltimo!=null){
                        terceroUltimo.setDomicilios(domicilios);
                        resultado.add(terceroUltimo);
                    }
                    terceroActual = new TercerosValueObject(
                            String.valueOf(actual),
                            rs.getString((String)campos.getAtributo("versionHTE")),
                            rs.getString((String)campos.getAtributo("tipoDocHTE")),
                            "",
                            rs.getString((String)campos.getAtributo("documentoHTE")),
                            rs.getString((String)campos.getAtributo("nombreHTE")),
                            rs.getString((String)campos.getAtributo("apellido1HTE")),
                            rs.getString((String)campos.getAtributo("pApellido1HTE")),
                            rs.getString((String)campos.getAtributo("apellido2HTE")),
                            rs.getString((String)campos.getAtributo("pApellido2HTE")),
                            rs.getString((String)campos.getAtributo("normalizadoHTE")),
                            rs.getString((String)campos.getAtributo("telefonoHTE")),
                            rs.getString((String)campos.getAtributo("emailHTE")),
                            'A',
                            rs.getString((String)campos.getAtributo("fechaOperHTE")),
                            rs.getString((String)campos.getAtributo("usuarioOperHTE")),
                            rs.getString((String)campos.getAtributo("moduloOperHTE")),
                            rs.getString((String)campos.getAtributo("fechaOperHTE")),
                            rs.getString((String)campos.getAtributo("usuarioOperHTE")));
                    domicilios = new Vector();
                    if (rs.getInt((String)campos.getAtributo("idDomicilioDOM"))>0){
                        nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                                rs.getString((String)campos.getAtributo("codPAI")),
                                rs.getString((String)campos.getAtributo("codPRV")),
                                rs.getString((String)campos.getAtributo("codMUN")),
                                rs.getString((String)campos.getAtributo("descPAI")),
                                rs.getString((String)campos.getAtributo("descPRV")),
                                rs.getString((String)campos.getAtributo("descMUN")),
                                TransformacionAtributoSelect.replace(rs.getString("DESCDMC"),"\"","\\\""),rs.getString("CPO"));
                        nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                        nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                        nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                        nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                        nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                        if(rs.getString("CODVIA")!=null){
                            nuevo.setDescVia(rs.getString("DESCVIA"));
                            nuevo.setCodigoVia(rs.getString("CODVIA"));
                            nuevo.setIdVia(rs.getString("IDVIA"));
                            if(rs.getString("CODVIA").equals("0")) {
                            }
                        }
                        nuevo.setNumDesde(rs.getString("NUD"));
                        nuevo.setLetraDesde(rs.getString("LED"));
                        nuevo.setNumHasta(rs.getString("NUH"));
                        nuevo.setLetraHasta(rs.getString("LEH"));
                        nuevo.setBloque(rs.getString("BLQ"));
                        nuevo.setPortal(rs.getString("POR"));
                        nuevo.setEscalera(rs.getString("ESC"));
                        nuevo.setPlanta(rs.getString("PLT"));
                        nuevo.setPuerta(rs.getString("PTA"));
                        nuevo.setBarriada(rs.getString("CAS"));
                        nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                        nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                        nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                        /* anadir ECOESI */
                        nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                        nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                        nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                        /* fin anadir ECOESI */
                        domicilios.add(nuevo);
                    }
                    terceroUltimo=terceroActual;
                    ultimo=actual;
                }
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            if (terceroUltimo!=null){
                terceroUltimo.setDomicilios(domicilios);
                resultado.add(terceroUltimo);
            } else {


                // ini COMPARACIÓN Q NO VA NINGÚN PARÁMETRO DE CONSULTA DE DOMICILIO

                m_Log.debug("PROVINCIA ................  "+codProvincia);
                m_Log.debug("MUNICIPIO ................  "+codMunicipio);
                m_Log.debug("NUM DESDE ................  "+numDesde);
                m_Log.debug("LETRA DESDE ..............  "+letraDesde);
                m_Log.debug("NUM HASTA ................  "+numHasta);
                m_Log.debug("LETRA HASTA...............  "+letraHasta);
                m_Log.debug("CASA .....................  "+casa);
                m_Log.debug("COD VIA ..................  "+codVia);
                m_Log.debug("ID VIA ...................  "+idVia);
                m_Log.debug("COD ECO ..................  "+codEco);
                m_Log.debug("COD ESI ..................  "+codEsi);
                m_Log.debug("RESULTADO ................  "+(codProvincia.equals("") && codMunicipio.equals("") && numDesde.equals("") &&
                   letraDesde.equals("") && numHasta.equals("") && letraHasta.equals("") && casa.equals("") &&
                   codVia.equals("") && idVia.equals("") && "".equals(codEco) && "".equals(codEsi)));

                if (codProvincia.equals("") && codMunicipio.equals("") && numDesde.equals("") &&
                   letraDesde.equals("") && numHasta.equals("") && letraHasta.equals("") && casa.equals("") &&
                   codVia.equals("") && idVia.equals("") && "".equals(codEco) && "".equals(codEsi)) {



                // Terceros q no tienen domicilio SÓLO SE AÑADEN A LOS ANTERIORES cuando la consulta no tiene ningún parámetro
                sql = "SELECT T_HTE.* FROM T_HTE WHERE ";

                sql = (codTerc.equals("")) ?
                        sql:sql+campos.getAtributo("idTerceroHTE")+"="+codTerc+" AND ";
                sql = (versTerc.equals("")) ?
                        sql:sql+campos.getAtributo("versionHTE")+"="+versTerc+" AND ";
                if (tipoDoc != null) {
                    if (!"".equals(tipoDoc.trim())){
                        String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocHTE"), tipoDoc.trim());
                        sql += condicion +" AND ";
                    }
                }
                if (docu != null) {
                    if (!"".equals(docu.trim())) {
                        String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoHTE"), docu.trim(),false);
                        if (!"".equals(condicion)) {
                            sql += condicion + " AND ";
                        }
                    }
                }
                if (name != null) {
                    if (!"".equals(name.trim())) {
                        String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("nombreHTE"), name.trim(),false);
                        if (!"".equals(condicion)) {
                            consultaInsensitiva=true;
                            sql += condicion + " AND ";
                        }
                    }
                }
                if (apell1 != null) {
                    if (!"".equals(apell1.trim())) {
                        String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("apellido1HTE"), apell1.trim(),false);
                        if (!"".equals(condicion)) {
                            consultaInsensitiva=true;
                            sql += condicion + " AND ";
                        }
                    }
                }
                if (apell2 != null) {
                    if (!"".equals(apell2.trim())) {
                        String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("apellido2HTE"), apell2.trim(),false);
                        if (!"".equals(condicion)) {
                            consultaInsensitiva=true;
                            sql += condicion + " AND ";
                        }
                    }
                }
                int longitud =sql.length()-5;
                if(sql.endsWith(" AND "))
                    sql = sql.substring(0,longitud);
                if(sql.endsWith(" WHERE "))
                    sql = sql.substring(0,sql.length()-7);

                String sqlMinus = "SELECT T_HTE.* FROM T_HTE,T_DOT WHERE ";
                if (tipoDoc != null) {
                    if (!"".equals(tipoDoc.trim())){
                        String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocHTE"), tipoDoc.trim());
                        sqlMinus += condicion +" AND ";
                    }
                }
                if (docu != null) {
                    if (!"".equals(docu.trim())) {
                        String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoHTE"), docu.trim(),false);
                        if (!"".equals(condicion)) {
                            sqlMinus += condicion + " AND ";
                        }
                    }
                }
                if (name != null) {
                    if (!"".equals(name.trim())) {
                        String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("nombreHTE"), name.trim(),false);
                        if (!"".equals(condicion)) {
                            consultaInsensitiva=true;
                            sqlMinus += condicion + " AND ";
                        }
                    }
                }
                if (apell1 != null) {
                    if (!"".equals(apell1.trim())) {
                        String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("apellido1HTE"), apell1.trim(),false);
                        if (!"".equals(condicion)) {
                            consultaInsensitiva=true;
                            sqlMinus += condicion + " AND ";
                        }
                    }
                }
                if (apell2 != null) {
                    if (!"".equals(apell2.trim())) {
                        String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("apellido2HTE"), apell2.trim(),false);
                        if (!"".equals(condicion)) {
                            consultaInsensitiva=true;
                            sqlMinus += condicion + " AND ";
                        }
                    }
                }
                sqlMinus += campos.getAtributo("situacionDOT")+"='A' AND " +
                        campos.getAtributo("idTerceroHTE")+"="+campos.getAtributo("idTerceroDOT");

                sql = sql + bd.minus(sql,sqlMinus);

                if(m_Log.isDebugEnabled()) {
                    m_Log.debug("No hay domicilios");
                    m_Log.debug(sql);
                }
                state = con.createStatement();
                
                if(consultaInsensitiva){
                String alter1=null;
                String alter2=null;
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    m_Log.debug("ALTER SESSION SET LINGUISTIC/GENERICBASELETTER");

                    state.executeQuery(alter1);
                    state.executeQuery(alter2);

                } 
            }
                
                rs = state.executeQuery(sql);
                TercerosValueObject tercero = null;
                while(rs.next()){
                    tercero = new TercerosValueObject(
                            rs.getString((String)campos.getAtributo("idTerceroHTE")),
                            rs.getString((String)campos.getAtributo("versionHTE")),
                            rs.getString((String)campos.getAtributo("tipoDocHTE")),
                        "",
                            rs.getString((String)campos.getAtributo("documentoHTE")),
                            rs.getString((String)campos.getAtributo("nombreHTE")),
                            rs.getString((String)campos.getAtributo("apellido1HTE")),
                            rs.getString((String)campos.getAtributo("pApellido1HTE")),
                            rs.getString((String)campos.getAtributo("apellido2HTE")),
                            rs.getString((String)campos.getAtributo("pApellido2HTE")),
                            rs.getString((String)campos.getAtributo("normalizadoHTE")),
                            rs.getString((String)campos.getAtributo("telefonoHTE")),
                            rs.getString((String)campos.getAtributo("emailHTE")),
                            'A',
                            rs.getString((String)campos.getAtributo("fechaOperHTE")),
                            rs.getString((String)campos.getAtributo("usuarioOperHTE")),
                            rs.getString((String)campos.getAtributo("moduloOperHTE")),
                            rs.getString((String)campos.getAtributo("fechaOperHTE")),
                            rs.getString((String)campos.getAtributo("usuarioOperHTE")));
                    tercero.setDomicilios(new Vector());
                    resultado.add(tercero);
                }
                cerrarResultSet(rs);
                cerrarStatement(state);
            }
            }
        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            
            try{
                if (consultaInsensitiva){
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                       String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                       String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                       m_Log.debug("ALTER SESSION SET BINARY/SPANISH");

                       state=con.createStatement();
                       state.executeQuery(alter1);
                       SigpGeneralOperations.closeStatement(state);
                       state=con.createStatement();
                       state.executeQuery(alter2);
                       SigpGeneralOperations.closeStatement(state);
                   } 
                }
            }catch (SQLException se){
                if(m_Log.isErrorEnabled()) m_Log.error(se.getMessage());
            }
            
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return resultado;
    }
    public Vector getListaInteresadosRegistro(GeneralValueObject gVO,String[] params){
    	Vector resultado=new Vector();
          String ejercicio = (String)gVO.getAtributo("ejercicio");
          String numero = (String)gVO.getAtributo("numero");
          String codtip = (String)gVO.getAtributo("codTip");
          String codOur = (String)gVO.getAtributo("codOur");
          m_Log.debug("*** recojo: " +ejercicio+numero+codOur+codtip);
    	return resultado;
    }
    public Vector getByHistorico(TercerosValueObject terVO,String[] params)
            throws TechnicalException{
        String sql= "";
        Vector resultado=new Vector();
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs = null;
        Statement state = null;
        boolean encontrado=false;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            state = con.createStatement();
            sql = "SELECT T_HTE.*,T_DOM.*,"+
                    campos.getAtributo("codTVI")+" AS CODTIPOVIA,"+
                    campos.getAtributo("descTVI")+" AS DESCTIPOVIA," +
                    campos.getAtributo("idVIA")+" AS IDVIA,"+
                    campos.getAtributo("codVIA")+" AS CODVIA,"+
                    campos.getAtributo("descVIA")+" AS DESCVIA,"+
                    campos.getAtributo("domicilioDNN")+" AS DESCDMC," +
                    campos.getAtributo("numDesdeDNN")+" AS NUD,"+
                    campos.getAtributo("letraDesdeDNN")+" AS LED,"+
                    campos.getAtributo("numHastaDNN")+" AS NUH,"+
                    campos.getAtributo("letraHastaDNN")+" AS LEH," +
                    campos.getAtributo("bloqueDNN")+" AS BLQ,"+
                    campos.getAtributo("portalDNN")+" AS POR,"+
                    campos.getAtributo("escaleraDNN")+" AS ESC,"+
                    campos.getAtributo("plantaDNN")+" AS PLT," +
                    campos.getAtributo("puertaDNN")+" AS PTA,"+
                    campos.getAtributo("codPostalDNN")+" AS CPO,"+
                    campos.getAtributo("barriadaDNN")+" AS CAS,"+
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA") +
                    " FROM T_HTE,"+ GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"+
                    GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,"+
                    GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN,"+
                    //"T_DNN,T_DOT,T_VIA,T_DOM,T_TVI,T_TOC WHERE "+
                    "T_DOM,T_DNN "
                    + " left join T_VIA on (DNN_VIA = VIA_COD AND DNN_VPA = VIA_PAI AND DNN_VPR = VIA_PRV AND DNN_VMU = VIA_MUN)"
                    + " left join T_TVI on (DNN_TVI = TVI_COD ),"
                    + "T_DOT "
                    + "left join T_TOC on (DOT_TOC = TOC_COD) "
                    + "WHERE "+

                    campos.getAtributo("idTerceroHTE")+"=" + terVO.getIdentificador() + " AND " +
                    campos.getAtributo("versionHTE")+"=" + terVO.getVersion() + " AND " +
                    terVO.getIdDomicilio() + "="+campos.getAtributo("idDomicilioDOM")+" AND "+
                    campos.getAtributo("idTerceroHTE")+"="+campos.getAtributo("idTerceroDOT")+" AND "+
                    campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM")+" AND "+
                    campos.getAtributo("normalizadoDOM")+"=2 AND "+
                    campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDNN")+" AND " +                   
                    campos.getAtributo("codPaisDNN")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDNN")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDNN")+"="+campos.getAtributo("codMUN")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV")+                    
                    " UNION "+
                    "SELECT T_HTE.*,T_DOM.*,"+
                    campos.getAtributo("codTVI")+","+
                    campos.getAtributo("descTVI")+"," +
                    campos.getAtributo("idVIA")+","+
                    campos.getAtributo("codVIA")+","+
                    campos.getAtributo("descVIA")+","+
                    campos.getAtributo("descVIA")+"," +
                    campos.getAtributo("numDesdeDSU")+","+
                    campos.getAtributo("letraDesdeDSU")+","+
                    campos.getAtributo("numHastaDSU")+","+
                    campos.getAtributo("letraHastaDSU")+"," +
                    campos.getAtributo("bloqueDSU")+","+
                    campos.getAtributo("portalDSU")+","+
                    campos.getAtributo("escaleraDPO")+","+
                    campos.getAtributo("plantaDPO")+"," +
                    campos.getAtributo("puertaDPO")+","+
                    campos.getAtributo("codPostalDSU")+","+
                    campos.getAtributo("barriadaDSU")+","+
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA")+
                    " FROM T_HTE,"+ GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"+
                    GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,"+
                    GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN,"+
                    "T_DSU,T_DPO,T_VIA,T_DOM,T_TVI,T_DOT"+
                    " left join T_TOC on (DOT_TOC = TOC_COD) "
                    + " WHERE "+
                    campos.getAtributo("idTerceroHTE")+"=" + terVO.getIdentificador() + " AND " +
                    campos.getAtributo("versionHTE")+"=" + terVO.getVersion() + " AND " +
                    terVO.getIdDomicilio() + "="+campos.getAtributo("idDomicilioDOM")+" AND "+
                    campos.getAtributo("idTerceroHTE")+"="+campos.getAtributo("idTerceroDOT")+" AND "+
                    campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM")+" AND "+
                    campos.getAtributo("normalizadoDOM")+"=1 AND "+
                    campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDPO")+" AND " +
                    campos.getAtributo("dirSueloDPO")+"="+ campos.getAtributo("codDirSueloDSU")+" AND "+                    
                    campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMUN")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV")+" AND "+
                    campos.getAtributo("codViaDSU")+"="+campos.getAtributo("idVIA")+" AND "+
                    campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisVIA")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvVIA")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMunVIA")+" AND "+
                    campos.getAtributo("tipoVIA")+"="+campos.getAtributo("codTVI");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            DomicilioSimpleValueObject nuevo;
            Vector domicilios = new Vector();
            while (rs.next()) {
                encontrado=true;
                terVO.setTipoDocumento(rs.getString((String)campos.getAtributo("tipoDocHTE")));
                terVO.setDocumento(rs.getString((String)campos.getAtributo("documentoHTE")));
                terVO.setNombre(rs.getString((String)campos.getAtributo("nombreHTE")));
                terVO.setApellido1(rs.getString((String)campos.getAtributo("apellido1HTE")));
                terVO.setPartApellido1(rs.getString((String)campos.getAtributo("pApellido1HTE")));
                terVO.setApellido2(rs.getString((String)campos.getAtributo("apellido2HTE")));
                terVO.setPartApellido2(rs.getString((String)campos.getAtributo("pApellido2HTE")));
                terVO.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoHTE")));
                terVO.setTelefono(rs.getString((String)campos.getAtributo("telefonoHTE")));
                terVO.setEmail(rs.getString((String)campos.getAtributo("emailHTE")));
                domicilios = new Vector();
                if (rs.getInt((String)campos.getAtributo("idDomicilioDOM"))>0){
                    nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                            rs.getString((String)campos.getAtributo("codPAI")),
                            rs.getString((String)campos.getAtributo("codPRV")),
                            rs.getString((String)campos.getAtributo("codMUN")),
                            rs.getString((String)campos.getAtributo("descPAI")),
                            rs.getString((String)campos.getAtributo("descPRV")),
                            rs.getString((String)campos.getAtributo("descMUN")),
                            TransformacionAtributoSelect.replace(rs.getString("DESCDMC"),"\"","\\\""),rs.getString("CPO"));
                    nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                    nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                    nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                    nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                    nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                    if(rs.getString("CODVIA")!=null){
                        nuevo.setDescVia(rs.getString("DESCVIA"));
                        nuevo.setCodigoVia(rs.getString("CODVIA"));
                        nuevo.setIdVia(rs.getString("IDVIA"));
                    }
                    nuevo.setNumDesde(rs.getString("NUD"));
                    nuevo.setLetraDesde(rs.getString("LED"));
                    nuevo.setNumHasta(rs.getString("NUH"));
                    nuevo.setLetraHasta(rs.getString("LEH"));
                    nuevo.setBloque(rs.getString("BLQ"));
                    nuevo.setPortal(rs.getString("POR"));
                    nuevo.setEscalera(rs.getString("ESC"));
                    nuevo.setPlanta(rs.getString("PLT"));
                    nuevo.setPuerta(rs.getString("PTA"));
                    nuevo.setBarriada(rs.getString("CAS"));
                    nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                    nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                    nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                    domicilios.add(nuevo);
                }
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            if (encontrado) {
                terVO.setDomicilios(domicilios);
            }
            resultado.add(terVO);
        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return resultado;
    }
    
    
    public Vector getByHistorico(TercerosValueObject terVO,Connection con,String[] params)throws TechnicalException{
        
        String sql= "";
        Vector resultado=new Vector();
        AdaptadorSQLBD bd=null;
        ResultSet rs = null;
        Statement state = null;
        boolean encontrado=false;
        try{
            bd = new AdaptadorSQLBD(params);
            state = con.createStatement();
            sql = "SELECT T_HTE.*,T_DOM.*,"+
                    campos.getAtributo("codTVI")+" AS CODTIPOVIA,"+
                    campos.getAtributo("descTVI")+" AS DESCTIPOVIA," +
                    campos.getAtributo("idVIA")+" AS IDVIA,"+
                    campos.getAtributo("codVIA")+" AS CODVIA,"+
                    campos.getAtributo("descVIA")+" AS DESCVIA,"+
                    campos.getAtributo("domicilioDNN")+" AS DESCDMC," +
                    campos.getAtributo("numDesdeDNN")+" AS NUD,"+
                    campos.getAtributo("letraDesdeDNN")+" AS LED,"+
                    campos.getAtributo("numHastaDNN")+" AS NUH,"+
                    campos.getAtributo("letraHastaDNN")+" AS LEH," +
                    campos.getAtributo("bloqueDNN")+" AS BLQ,"+
                    campos.getAtributo("portalDNN")+" AS POR,"+
                    campos.getAtributo("escaleraDNN")+" AS ESC,"+
                    campos.getAtributo("plantaDNN")+" AS PLT," +
                    campos.getAtributo("puertaDNN")+" AS PTA,"+
                    campos.getAtributo("codPostalDNN")+" AS CPO,"+
                    campos.getAtributo("barriadaDNN")+" AS CAS,"+
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA") +
                    " FROM T_HTE,"+ GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"+
                    GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,"+
                    GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN,"+
                    "T_DNN,T_DOT,T_VIA,T_DOM,T_TVI,T_TOC WHERE "+

                    campos.getAtributo("idTerceroHTE")+"=" + terVO.getIdentificador() + " AND " +
                    campos.getAtributo("versionHTE")+"=" + terVO.getVersion() + " AND " +
                    terVO.getIdDomicilio() + "="+campos.getAtributo("idDomicilioDOM")+" AND "+
                    campos.getAtributo("idTerceroHTE")+"="+campos.getAtributo("idTerceroDOT")+" AND "+
                    campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM")+" AND "+
                    campos.getAtributo("normalizadoDOM")+"=2 AND "+
                    campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDNN")+" AND " +
                    bd.joinLeft((String) campos.getAtributo("tipoOcupacionDOT"),(String)campos.getAtributo("codTOC"))+ " AND " +
                    campos.getAtributo("codPaisDNN")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDNN")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDNN")+"="+campos.getAtributo("codMUN")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV")+" AND "+
                    bd.joinLeft(new String[]{(String) campos.getAtributo("codViaDNN"),
                            (String) campos.getAtributo("codPaisViaDNN"),
                            (String) campos.getAtributo("codProvViaDNN"),
                            (String) campos.getAtributo("codMunViaDNN"),
                            (String) campos.getAtributo("idTipoViaDNN")},
                            new String[]{(String) campos.getAtributo("idVIA"),
                                    (String) campos.getAtributo("codPaisVIA"),
                                    (String) campos.getAtributo("codProvVIA"),
                                    (String) campos.getAtributo("codMunVIA"),
                                    (String) campos.getAtributo("codTVI")}) +
                    " UNION "+
                    "SELECT T_HTE.*,T_DOM.*,"+
                    campos.getAtributo("codTVI")+","+
                    campos.getAtributo("descTVI")+"," +
                    campos.getAtributo("idVIA")+","+
                    campos.getAtributo("codVIA")+","+
                    campos.getAtributo("descVIA")+","+
                    campos.getAtributo("descVIA")+"," +
                    campos.getAtributo("numDesdeDSU")+","+
                    campos.getAtributo("letraDesdeDSU")+","+
                    campos.getAtributo("numHastaDSU")+","+
                    campos.getAtributo("letraHastaDSU")+"," +
                    campos.getAtributo("bloqueDSU")+","+
                    campos.getAtributo("portalDSU")+","+
                    campos.getAtributo("escaleraDPO")+","+
                    campos.getAtributo("plantaDPO")+"," +
                    campos.getAtributo("puertaDPO")+","+
                    campos.getAtributo("codPostalDSU")+","+
                    campos.getAtributo("barriadaDSU")+","+
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA")+
                    " FROM T_HTE,"+ GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"+
                    GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV,"+
                    GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN,"+
                    "T_DSU,T_DPO,T_DOT,T_VIA,T_DOM,T_TVI,T_TOC WHERE "+
                    campos.getAtributo("idTerceroHTE")+"=" + terVO.getIdentificador() + " AND " +
                    campos.getAtributo("versionHTE")+"=" + terVO.getVersion() + " AND " +
                    terVO.getIdDomicilio() + "="+campos.getAtributo("idDomicilioDOM")+" AND "+
                    campos.getAtributo("idTerceroHTE")+"="+campos.getAtributo("idTerceroDOT")+" AND "+
                    campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM")+" AND "+
                    campos.getAtributo("normalizadoDOM")+"=1 AND "+
                    campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDPO")+" AND " +
                    campos.getAtributo("dirSueloDPO")+"="+ campos.getAtributo("codDirSueloDSU")+" AND "+
                    bd.joinLeft((String) campos.getAtributo("tipoOcupacionDOT"),
                            (String) campos.getAtributo("codTOC")) + " AND " +
                    campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMUN")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV")+" AND "+
                    campos.getAtributo("codViaDSU")+"="+campos.getAtributo("idVIA")+" AND "+
                    campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisVIA")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvVIA")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMunVIA")+" AND "+
                    campos.getAtributo("tipoVIA")+"="+campos.getAtributo("codTVI");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            DomicilioSimpleValueObject nuevo;
            Vector domicilios = new Vector();
            while (rs.next()) {
                encontrado=true;
                terVO.setTipoDocumento(rs.getString((String)campos.getAtributo("tipoDocHTE")));
                terVO.setDocumento(rs.getString((String)campos.getAtributo("documentoHTE")));
                terVO.setNombre(rs.getString((String)campos.getAtributo("nombreHTE")));
                terVO.setApellido1(rs.getString((String)campos.getAtributo("apellido1HTE")));
                terVO.setPartApellido1(rs.getString((String)campos.getAtributo("pApellido1HTE")));
                terVO.setApellido2(rs.getString((String)campos.getAtributo("apellido2HTE")));
                terVO.setPartApellido2(rs.getString((String)campos.getAtributo("pApellido2HTE")));
                terVO.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoHTE")));
                terVO.setTelefono(rs.getString((String)campos.getAtributo("telefonoHTE")));
                terVO.setEmail(rs.getString((String)campos.getAtributo("emailHTE")));
                domicilios = new Vector();
                if (rs.getInt((String)campos.getAtributo("idDomicilioDOM"))>0){
                    nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                            rs.getString((String)campos.getAtributo("codPAI")),
                            rs.getString((String)campos.getAtributo("codPRV")),
                            rs.getString((String)campos.getAtributo("codMUN")),
                            rs.getString((String)campos.getAtributo("descPAI")),
                            rs.getString((String)campos.getAtributo("descPRV")),
                            rs.getString((String)campos.getAtributo("descMUN")),
                            TransformacionAtributoSelect.replace(rs.getString("DESCDMC"),"\"","\\\""),rs.getString("CPO"));
                    nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                    nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                    nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                    nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                    nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                    if(rs.getString("CODVIA")!=null){
                        nuevo.setDescVia(rs.getString("DESCVIA"));
                        nuevo.setCodigoVia(rs.getString("CODVIA"));
                        nuevo.setIdVia(rs.getString("IDVIA"));
                    }
                    nuevo.setNumDesde(rs.getString("NUD"));
                    nuevo.setLetraDesde(rs.getString("LED"));
                    nuevo.setNumHasta(rs.getString("NUH"));
                    nuevo.setLetraHasta(rs.getString("LEH"));
                    nuevo.setBloque(rs.getString("BLQ"));
                    nuevo.setPortal(rs.getString("POR"));
                    nuevo.setEscalera(rs.getString("ESC"));
                    nuevo.setPlanta(rs.getString("PLT"));
                    nuevo.setPuerta(rs.getString("PTA"));
                    nuevo.setBarriada(rs.getString("CAS"));
                    nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                    nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                    nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                    domicilios.add(nuevo);
                }
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            if (encontrado) {
                terVO.setDomicilios(domicilios);
            }
            resultado.add(terVO);
        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new TechnicalException(e.getMessage(),e);
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
        }
        return resultado;
    }
    
    

    public boolean existeTercero(TercerosValueObject terVO,String[] params)throws TechnicalException{
        String sql = "";
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs = null;
        Statement state = null;
        boolean res = false;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            state = con.createStatement();
            
            sql = "SELECT T_TER.* FROM T_TER WHERE (" + campos.getAtributo("situacionTER")+"='A' AND ";
            if(terVO.getTipoDocumento().equals("0")) {
                sql += campos.getAtributo("tipoDocTER") + "="+terVO.getTipoDocumento()+" AND " +
                        campos.getAtributo("nombreTER") + "=" + bd.addString(terVO.getNombre());
                if ((terVO.getApellido1() != null) && (!terVO.getApellido1().equals(""))) {
                    sql += " AND " + campos.getAtributo("apellido1TER") + "=" + bd.addString(terVO.getApellido1());
                } else {
                    sql += " AND " + campos.getAtributo("apellido1TER") + " IS NULL";
                }
                if ((terVO.getApellido2() != null) && (!terVO.getApellido2().equals(""))) {
                    sql += " AND " + campos.getAtributo("apellido2TER") + "=" + bd.addString(terVO.getApellido2());
                } else {
                    sql += " AND " + campos.getAtributo("apellido2TER") + " IS NULL";
                }
                sql += ")";
            } else {
                sql += campos.getAtributo("tipoDocTER")+"="+terVO.getTipoDocumento()+" AND " +
                        campos.getAtributo("documentoTER")+"=" + bd.addString(terVO.getDocumento())+")";
            }
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            if (rs.next()) {
                res=true;
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return res;
    }


    public int setTercero(TercerosValueObject terVO,String[] params)throws TechnicalException{
        String sql = "";
        int max = 0;
        int ver = 1;
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();

            String nombre = terVO.getNombreCompleto();
            sql = "SELECT " +bd.funcionMatematica(bd.FUNCIONMATEMATICA_MAX,new String[]{(String)campos.getAtributo("idTerceroTER")}) + " AS MAXIMO FROM T_TER";
            rs = state.executeQuery(sql);
            rs.next();
            max = rs.getInt("MAXIMO");
            cerrarResultSet(rs);
            cerrarStatement(state);
            if (max<0)max=1;
            else max++;

            String codigoExterno;
            if (terVO.getCodTerceroOrigen() != null && terVO.getIdentificador().equals("0") 
					&& !terVO.getCodTerceroOrigen().isEmpty()) {
				codigoExterno=  terVO.getCodTerceroOrigen();
			} else {
                codigoExterno="null";
            }


            m_Log.debug(String.format("Codigo de tercero a insertar: %d", max));
            //max++;
            sql = "INSERT INTO T_TER("+
                    campos.getAtributo("idTerceroTER")+","+
                    campos.getAtributo("tipoDocTER")+","+
                    campos.getAtributo("documentoTER")+","+
                    campos.getAtributo("nombreTER")+","+
                    campos.getAtributo("apellido1TER")+","+
                    campos.getAtributo("pApellido1TER")+","+
                    campos.getAtributo("apellido2TER")+","+
                    campos.getAtributo("pApellido2TER")+","+
                    campos.getAtributo("nombreLargoTER")+","+
                    campos.getAtributo("normalizadoTER")+","+
                    campos.getAtributo("telefonoTER")+","+
                    campos.getAtributo("emailTER")+","+
                    campos.getAtributo("situacionTER")+","+
                    campos.getAtributo("versionTER")+","+
                    campos.getAtributo("fechaAltaTER")+","+
                    campos.getAtributo("usuarioAltaTER")+","+
                    campos.getAtributo("moduloAltaTER")+","+
                    campos.getAtributo("fechaBajaTER")+","+
                    campos.getAtributo("usuarioBajaTER")+
                    ",EXTERNAL_CODE"+
                    ") VALUES(" + max + "," + bd.addString(terVO.getTipoDocumento()) +
                    "," + bd.addString(terVO.getDocumento()) + "," + bd.addString(terVO.getNombre()) + "," +
                    bd.addString(terVO.getApellido1()) + "," + bd.addString(terVO.getPartApellido1()) + "," +
                    bd.addString(terVO.getApellido2()) + "," + bd.addString(terVO.getPartApellido2()) + "," +
                    bd.addString(nombre) + "," +
                    terVO.getNormalizado() + "," + bd.addString(terVO.getTelefono()) + "," +
                    bd.addString(terVO.getEmail()) + ",'" + terVO.getSituacion() + "'," +
                    ver +","+ bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null) + "," +
                    bd.addString(terVO.getUsuarioAlta()) +
                    "," + bd.addString(terVO.getModuloAlta()) + ",null,null,"+codigoExterno+")";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();
            state.executeUpdate(sql);
            cerrarStatement(state);
            sql = "INSERT INTO T_HTE("+
                    campos.getAtributo("idTerceroHTE")+","+
                    campos.getAtributo("versionHTE")+","+
                    campos.getAtributo("tipoDocHTE")+","+
                    campos.getAtributo("documentoHTE")+","+
                    campos.getAtributo("nombreHTE")+","+
                    campos.getAtributo("apellido1HTE")+","+
                    campos.getAtributo("pApellido1HTE")+","+
                    campos.getAtributo("apellido2HTE")+","+
                    campos.getAtributo("pApellido2HTE")+","+  
                    campos.getAtributo("nombreLargoHTE")+","+
                    campos.getAtributo("normalizadoHTE")+","+
                    campos.getAtributo("telefonoHTE")+","+
                    campos.getAtributo("emailHTE")+","+
                    campos.getAtributo("fechaOperHTE")+","+
                    campos.getAtributo("usuarioOperHTE")+","+
                    campos.getAtributo("moduloOperHTE")+
                     ",EXTERNAL_CODE"+ 
                    ") VALUES(" + max + ",'" + ver + "'," +
                    terVO.getTipoDocumento() + "," + bd.addString(terVO.getDocumento()) + "," +
                    bd.addString(terVO.getNombre()) + "," + bd.addString(terVO.getApellido1()) + "," +
                    bd.addString(terVO.getPartApellido1()) + "," + bd.addString(terVO.getApellido2()) + "," +
                    bd.addString(terVO.getPartApellido2()) + "," + bd.addString(nombre) + "," +
                    terVO.getNormalizado() + "," +
                    bd.addString(terVO.getTelefono()) + "," + bd.addString(terVO.getEmail()) + "," +
                    bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null) + "," +
                    bd.addString(terVO.getUsuarioAlta()) +
                    "," + bd.addString(terVO.getModuloAlta()) + ","+codigoExterno+")";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();
            state.executeUpdate(sql);
            cerrarStatement(state);
            commitTransaction(bd, con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            rollBackTransaction(bd, con, e);
            max=0;
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return max;
    }

    public int setTerceroDuplicado(TercerosValueObject terVO,String[] params)throws TechnicalException{
        String sql = "";
        int max = 0;
        int ver = 1;
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();

            String nombre = terVO.getNombreCompleto();
            sql = "SELECT " +bd.funcionMatematica(bd.FUNCIONMATEMATICA_MAX,new String[]{(String)campos.getAtributo("idTerceroTER")})  + " AS MAXIMO FROM T_TER";
            rs = state.executeQuery(sql);
            rs.next();
            max = rs.getInt("MAXIMO");
            m_Log.debug(new Integer(max));
            max++;
            cerrarResultSet(rs);
            cerrarStatement(state);

            sql = "INSERT INTO T_TER("+
                    campos.getAtributo("idTerceroTER")+","+
                    campos.getAtributo("tipoDocTER")+","+
                    campos.getAtributo("documentoTER")+","+
                    campos.getAtributo("nombreTER")+","+
                    campos.getAtributo("apellido1TER")+","+
                    campos.getAtributo("pApellido1TER")+","+
                    campos.getAtributo("apellido2TER")+","+
                    campos.getAtributo("pApellido2TER")+","+
                    campos.getAtributo("nombreLargoTER")+","+
                    campos.getAtributo("normalizadoTER")+","+
                    campos.getAtributo("telefonoTER")+","+
                    campos.getAtributo("emailTER")+","+
                    campos.getAtributo("situacionTER")+","+
                    campos.getAtributo("versionTER")+","+
                    campos.getAtributo("fechaAltaTER")+","+
                    campos.getAtributo("usuarioAltaTER")+","+
                    campos.getAtributo("moduloAltaTER")+","+
                    campos.getAtributo("fechaBajaTER")+","+
                    campos.getAtributo("usuarioBajaTER")+
                    ") VALUES(" + max + "," + bd.addString(terVO.getTipoDocumento()) +
                    "," + bd.addString(terVO.getDocumento()) + "," + bd.addString(terVO.getNombre()) + "," +
                    bd.addString(terVO.getApellido1()) + "," + bd.addString(terVO.getPartApellido1()) + "," +
                    bd.addString(terVO.getApellido2()) + "," + bd.addString(terVO.getPartApellido2()) + "," +
                    bd.addString(nombre) + "," +
                    terVO.getNormalizado() + "," + bd.addString(terVO.getTelefono()) + "," +
                    bd.addString(terVO.getEmail()) + ",'" + terVO.getSituacion() + "'," +
                    ver +","+bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null)+","+
                    bd.addString(terVO.getUsuarioAlta()) +
                    "," + bd.addString(terVO.getModuloAlta()) + ",'','')";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();
            state.executeUpdate(sql);
            cerrarStatement(state);
            sql = "INSERT INTO T_HTE("+
                    campos.getAtributo("idTerceroHTE")+","+
                    campos.getAtributo("versionHTE")+","+
                    campos.getAtributo("tipoDocHTE")+","+
                    campos.getAtributo("documentoHTE")+","+
                    campos.getAtributo("nombreHTE")+","+
                    campos.getAtributo("apellido1HTE")+","+
                    campos.getAtributo("pApellido1HTE")+","+
                    campos.getAtributo("apellido2HTE")+","+
                    campos.getAtributo("pApellido2HTE")+","+
                    campos.getAtributo("nombreLargoHTE")+","+
                    campos.getAtributo("normalizadoHTE")+","+
                    campos.getAtributo("telefonoHTE")+","+
                    campos.getAtributo("emailHTE")+","+
                    campos.getAtributo("fechaOperHTE")+","+
                    campos.getAtributo("usuarioOperHTE")+","+
                    campos.getAtributo("moduloOperHTE")+
                    ") VALUES(" + max + ",'" + ver + "'," +
                    terVO.getTipoDocumento() + "," + bd.addString(terVO.getDocumento()) + "," +
                    bd.addString(terVO.getNombre()) + "," + bd.addString(terVO.getApellido1()) + "," +
                    bd.addString(terVO.getPartApellido1()) + "," + bd.addString(terVO.getApellido2()) + "," +
                    bd.addString(terVO.getPartApellido2()) + "," + bd.addString(nombre) + "," +
                    terVO.getNormalizado() + "," +
                    bd.addString(terVO.getTelefono()) + "," + bd.addString(terVO.getEmail()) + "," +
                    bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null)+","+
                    bd.addString(terVO.getUsuarioAlta()) +
                    "," + bd.addString(terVO.getModuloAlta()) + ")";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();
            state.executeUpdate(sql);
            cerrarStatement(state);
            commitTransaction(bd, con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            rollBackTransaction(bd, con, e);
            max=0;
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return max;
    }

    /**
     * Devuelve un tercero
     * @param codigo tercero
     * @param params
     * @return String con el nombre
     * @throws TechnicalException
     */
    public String getNombreTercero  (int codigo, String[] params)throws TechnicalException{
        String sql = "";
        String nombre = "";
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs=null;
        PreparedStatement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();

            sql = "SELECT " +  campos.getAtributo("nombreTER") + "," + campos.getAtributo("apellido1TER") + "," +
                    campos.getAtributo("apellido2TER") + " FROM T_TER WHERE " + campos.getAtributo("idTerceroTER") + "=?";

            state = con.prepareStatement(sql);
            int i = 1;
            state.setInt(i++, codigo);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery();

            if (rs.next()) {
                i = 1;
                String nom = rs.getString(i++);
                String ap1 = rs.getString(i++);
                String ap2 = rs.getString(i++);
                if (ap1!=null) {
                    nombre = nombre + ap1;
                }
                if (ap2!=null) {
                    nombre = nombre + " " + ap2;
                }
                if (nom!=null) {
                    nombre = nombre + ", " + nom;
                }
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return nombre;
    }


    public int updateTercero(TercerosValueObject terVO,String[] params)throws TechnicalException{
        String sql = "";
        int ver = Integer.parseInt(terVO.getVersion())+1;
        int id = 0;
        AdaptadorSQLBD bd=null;
        Connection con=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();

            String nombre = terVO.getNombreCompleto();
            id = Integer.parseInt(terVO.getIdentificador());
            sql = "UPDATE T_TER SET " +
                    campos.getAtributo("versionTER") + "='" + ver + "'," +
                    campos.getAtributo("tipoDocTER") + "=" + bd.addString(terVO.getTipoDocumento()) + "," +
                    campos.getAtributo("documentoTER") + "=" + bd.addString(terVO.getDocumento()) + "," +
                    campos.getAtributo("nombreTER") + "=" + bd.addString(terVO.getNombre()) + "," +
                    campos.getAtributo("apellido1TER") + "=" + bd.addString(terVO.getApellido1()) + "," +
                    campos.getAtributo("pApellido1TER") + "=" + bd.addString(terVO.getPartApellido1()) + "," +
                    campos.getAtributo("apellido2TER") + "=" + bd.addString(terVO.getApellido2()) + "," +
                    campos.getAtributo("pApellido2TER") + "=" + bd.addString(terVO.getPartApellido2()) + "," +
                    campos.getAtributo("nombreLargoTER") + "=" + bd.addString(nombre) + "," +
                    campos.getAtributo("telefonoTER") + "=" + bd.addString(terVO.getTelefono()) + "," +
                    campos.getAtributo("emailTER") + "=" + bd.addString(terVO.getEmail());
                    if (!terVO.getDomPrincipal().equals("")) {
                        sql += ", TER_DOM = " + terVO.getDomPrincipal();
                    }
                    sql += " WHERE " + campos.getAtributo("idTerceroTER") + "=" + terVO.getIdentificador();

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state.executeUpdate(sql);
            cerrarStatement(state);

            sql = "INSERT INTO T_HTE("+ 
                    campos.getAtributo("idTerceroHTE")+","+
                    campos.getAtributo("versionHTE")+","+
                    campos.getAtributo("tipoDocHTE")+","+
                    campos.getAtributo("documentoHTE")+","+
                    campos.getAtributo("nombreHTE")+","+
                    campos.getAtributo("apellido1HTE")+","+
                    campos.getAtributo("pApellido1HTE")+","+
                    campos.getAtributo("apellido2HTE")+","+
                    campos.getAtributo("pApellido2HTE")+","+
                    campos.getAtributo("nombreLargoHTE")+","+
                    campos.getAtributo("normalizadoHTE")+","+
                    campos.getAtributo("telefonoHTE")+","+
                    campos.getAtributo("emailHTE")+","+
                    campos.getAtributo("fechaOperHTE")+","+
                    campos.getAtributo("usuarioOperHTE")+","+
                    campos.getAtributo("moduloOperHTE")+
                    ") VALUES(" + id + ",'" + ver + "'," +
                    terVO.getTipoDocumento() + "," + bd.addString(terVO.getDocumento()) + "," +
                    bd.addString(terVO.getNombre()) + "," + bd.addString(terVO.getApellido1()) + "," +
                    bd.addString(terVO.getPartApellido1()) + "," + bd.addString(terVO.getApellido2()) + "," +
                    bd.addString(terVO.getPartApellido2()) + "," + bd.addString(nombre) + "," +
                    terVO.getNormalizado() + "," +
                    bd.addString(terVO.getTelefono()) + "," + bd.addString(terVO.getEmail()) + "," +
                    bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null)+","+
                    bd.addString(terVO.getUsuarioAlta()) +
                    "," + bd.addString(terVO.getModuloAlta()) + ")";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state =  con.createStatement();
            state.executeUpdate(sql);
            cerrarStatement(state);
            commitTransaction(bd, con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            rollBackTransaction(bd, con, e);
            id=0;
        }finally{
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return id;
    }

    public int updateTerceroDesdePadron(TercerosValueObject terVO,String[] params)throws TechnicalException{
        String sql = "";
        int ver = Integer.parseInt(terVO.getVersion())+1;
        int id = 0;
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();

            String nombre = terVO.getNombreCompleto();
            id = Integer.parseInt(terVO.getIdentificador());
            sql = "UPDATE T_TER SET " +
                    campos.getAtributo("versionTER") + "='" + ver + "'," +
                    campos.getAtributo("tipoDocTER") + "=" + bd.addString(terVO.getTipoDocumento()) + "," +
                    campos.getAtributo("documentoTER") + "=" + bd.addString(terVO.getDocumento()) + "," +
                    campos.getAtributo("nombreTER") + "=" + bd.addString(terVO.getNombre()) + "," +
                    campos.getAtributo("apellido1TER") + "=" + bd.addString(terVO.getApellido1()) + "," +
                    campos.getAtributo("pApellido1TER") + "=" + bd.addString(terVO.getPartApellido1()) + "," +
                    campos.getAtributo("apellido2TER") + "=" + bd.addString(terVO.getApellido2()) + "," +
                    campos.getAtributo("pApellido2TER") + "=" + bd.addString(terVO.getPartApellido2()) + "," +
                    campos.getAtributo("nombreLargoTER") + "=" + bd.addString(nombre) + "," +
                    campos.getAtributo("telefonoTER") + "=" + bd.addString(terVO.getTelefono()) + "," +
                    campos.getAtributo("emailTER") + "=" + terVO.getEmail() +
                    " WHERE " +
                    campos.getAtributo("idTerceroTER") + "=" + terVO.getIdentificador();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state.executeUpdate(sql);
            cerrarStatement(state);

            sql = "SELECT * FROM T_HTE WHERE " +
                    campos.getAtributo("idTerceroHTE")+"="+id + " AND " +
                    campos.getAtributo("versionHTE") + "=" + ver;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();
            rs = state.executeQuery(sql);
            String existe = "no";
            while (rs.next()) {
                existe = "si";
            }
            cerrarResultSet(rs);
            cerrarStatement(state);

            sql = "INSERT INTO T_HTE("+
                    campos.getAtributo("idTerceroHTE")+","+
                    campos.getAtributo("versionHTE")+","+
                    campos.getAtributo("tipoDocHTE")+","+
                    campos.getAtributo("documentoHTE")+","+
                    campos.getAtributo("nombreHTE")+","+
                    campos.getAtributo("apellido1HTE")+","+
                    campos.getAtributo("pApellido1HTE")+","+
                    campos.getAtributo("apellido2HTE")+","+
                    campos.getAtributo("pApellido2HTE")+","+
                    campos.getAtributo("nombreLargoHTE")+","+
                    campos.getAtributo("normalizadoHTE")+","+
                    campos.getAtributo("telefonoHTE")+","+
                    campos.getAtributo("emailHTE")+","+
                    campos.getAtributo("fechaOperHTE")+","+
                    campos.getAtributo("usuarioOperHTE")+","+
                    campos.getAtributo("moduloOperHTE")+
                    ") VALUES(" + id + ",'" + ver + "'," +
                    terVO.getTipoDocumento() + "," + bd.addString(terVO.getDocumento()) + "," +
                    bd.addString(terVO.getNombre()) + "," + bd.addString(terVO.getApellido1()) + "," +
                    bd.addString(terVO.getPartApellido1()) + "," + bd.addString(terVO.getApellido2()) + "," +
                    bd.addString(terVO.getPartApellido2()) + "," + bd.addString(nombre) + "," +
                    terVO.getNormalizado() + "," +
                    bd.addString(terVO.getTelefono()) + "," + bd.addString(terVO.getEmail()) + "," +
                    bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null)+","+
                    bd.addString(terVO.getUsuarioAlta()) +
                    "," + bd.addString(terVO.getModuloAlta()) + ")";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();
            state.executeUpdate(sql);
            cerrarStatement(state);
            commitTransaction(bd, con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            rollBackTransaction(bd, con, e);
            id=0;
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return id;
    }

    public Vector getIdTercero(TercerosValueObject terVO,String[] params,String mod)throws TechnicalException{
        String sql = "";
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        String codTercero = "";
        Vector listaCodTercero = new Vector();
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            state = con.createStatement();

            if(mod.equals("noModificar")) {
                sql = "SELECT T_TER.* FROM T_TER WHERE " + campos.getAtributo("situacionTER")+"='A' AND "+
                        "(("+campos.getAtributo("tipoDocTER")+"=0 AND " +
                        campos.getAtributo("nombreTER")+"="+bd.addString(terVO.getNombre())+" AND " +
                        campos.getAtributo("apellido1TER") + "=" + bd.addString(terVO.getApellido1());
                if(terVO.getPartApellido1() != null && !terVO.getPartApellido1().equals("")) {
                    sql += " AND " + campos.getAtributo("pApellido1TER") + "=" + bd.addString(terVO.getPartApellido1());
                }
                if(terVO.getApellido2() != null && !terVO.getApellido2().equals("")) {
                    sql += " AND " + campos.getAtributo("apellido2TER") + "=" + bd.addString(terVO.getApellido2());
                }
                if(terVO.getPartApellido2() != null && !terVO.getPartApellido2().equals("")) {
                    sql += " AND " + campos.getAtributo("pApellido2TER") + "=" + bd.addString(terVO.getPartApellido2());
                }
                sql += ") OR (" + campos.getAtributo("tipoDocTER")+"<>0 AND " +
                        campos.getAtributo("documentoTER")+"="+bd.addString(terVO.getDocumento())+" AND " +
                        campos.getAtributo("nombreTER")+"="+bd.addString(terVO.getNombre())+" AND " +
                        campos.getAtributo("apellido1TER") + "=" + bd.addString(terVO.getApellido1());
                if(terVO.getPartApellido1() != null && !terVO.getPartApellido1().equals("")) {
                    sql += " AND " + campos.getAtributo("pApellido1TER") + "=" + bd.addString(terVO.getPartApellido1());
                }
                if(terVO.getApellido2() != null && !terVO.getApellido2().equals("")) {
                    sql += " AND " + campos.getAtributo("apellido2TER") + "=" + bd.addString(terVO.getApellido2());
                }
                if(terVO.getPartApellido2() != null && !terVO.getPartApellido2().equals("")) {
                    sql += " AND " + campos.getAtributo("pApellido2TER") + "=" + bd.addString(terVO.getPartApellido2());
                }
                sql += "))";
            } else if(mod.equals("modificar")) {

                String td = null;
                if (terVO.getTipoDocumentoAntiguo() != null)
                    if (!"".equals(terVO.getTipoDocumentoAntiguo()))
                        td = terVO.getTipoDocumentoAntiguo();
                    else td="0";
                else td = "0";
                sql = "SELECT T_TER.* FROM T_TER WHERE " + campos.getAtributo("situacionTER")+"='A' AND "+
                        "("+campos.getAtributo("tipoDocTER")+"= " + td + " AND "  ;
                if (!"0".equals(td))
                    sql += 	campos.getAtributo("documentoTER")+"="+bd.addString(terVO.getDocumentoAntiguo())+" AND ";

                sql += campos.getAtributo("nombreTER")+"="+bd.addString(terVO.getNombreAntiguo())+" AND " +
                        campos.getAtributo("apellido1TER") + "=" + bd.addString(terVO.getApellido1Antiguo());
                if(terVO.getPartApellido1Antiguo() != null && !terVO.getPartApellido1Antiguo().equals("")) {
                    sql += " AND " + campos.getAtributo("pApellido1TER") + "=" + bd.addString(terVO.getPartApellido1Antiguo());
                }
                if(terVO.getApellido2Antiguo() != null && !terVO.getApellido2Antiguo().equals("")) {
                    sql += " AND " + campos.getAtributo("apellido2TER") + "=" + bd.addString(terVO.getApellido2Antiguo());
                }
                if(terVO.getPartApellido2Antiguo() != null && !terVO.getPartApellido2Antiguo().equals("")) {
                    sql += " AND " + campos.getAtributo("pApellido2TER") + "=" + bd.addString(terVO.getPartApellido2Antiguo());
                }
                sql += ")";
            }
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);

            while (rs.next()) {
                codTercero = rs.getString((String)campos.getAtributo("idTerceroTER"));
                String version = rs.getString((String)campos.getAtributo("versionTER"));
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codTercero",codTercero);
                gVO.setAtributo("versionTercero",version);
                listaCodTercero.addElement(gVO);
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
        }catch (Exception e){
            listaCodTercero=null;
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return listaCodTercero;
    }

    public void cambiaSituacionTercero(TercerosValueObject terVO,String[] params)throws TechnicalException{
        char situ = terVO.getSituacion();
        String sql = "";
        AdaptadorSQLBD bd=null;
        Connection con=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();

            sql = "UPDATE T_TER SET ";
            if(situ=='B'){
                sql+=campos.getAtributo("fechaBajaTER") + "="+bd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+"," +
                                campos.getAtributo("usuarioBajaTER") + "=" + bd.addString(terVO.getUsuarioBaja()) + ",";
            }
            sql+=campos.getAtributo("situacionTER") + "='" + terVO.getSituacion() + "' "+
                            "WHERE " + campos.getAtributo("idTerceroTER") + "=" + terVO.getIdentificador();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state.executeUpdate(sql);
            cerrarStatement(state);
            commitTransaction(bd,con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            rollBackTransaction(bd, con, e);
        }finally{
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
    }

    public void eliminarTercerosSimilares(String codTerDepurado, String codDomPrincipal, String[] codTerAEliminar,
                                          int codUsuario, String[] params) throws TechnicalException {
        String sql;
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs = null;
        PreparedStatement ps=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);

            String where = " WHERE ";
            for (int i=0;i<codTerAEliminar.length;i++) {
                if (i!=codTerAEliminar.length-1) {
                    where = where.concat(campos.getAtributo("idTerceroTER") + "=" + codTerAEliminar[i] + " OR ");
                } else {
                    where = where.concat(campos.getAtributo("idTerceroTER") + "=" + codTerAEliminar[i]);
                }

            }

            sql = "UPDATE T_TER SET " +
                campos.getAtributo("fechaBajaTER") + "="+bd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+"," +
                campos.getAtributo("usuarioBajaTER") + "=" + codUsuario + "," +
                campos.getAtributo("situacionTER") + "='B' "+ where;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = con.prepareStatement(sql);

            ps.executeUpdate();
            cerrarStatement(ps);

            // Una vez dado de baja el tercero, tendremos que actualizar las referencias a él en expedientes.
            // Para ello es necesario que obtengamos el numero de version del tercero.
            sql = "SELECT " + bd.funcionMatematica(
                    AdaptadorSQLBD.FUNCIONMATEMATICA_MAX,
                    new String[]{(String)campos.getAtributo("versionTER")}) + " AS MAX_NVE " +
                    "FROM T_TER " +
                    "WHERE " + campos.getAtributo("idTerceroTER") + " = ?";
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(codTerDepurado));

            rs = ps.executeQuery();

            int numVersion = 0;
            if (rs.next()) {
                numVersion = rs.getInt(1);
            }
            if (numVersion == 0) throw new TechnicalException("ERROR, NO SE HA HALLADO NUMERO DE VERSION PARA EL " +
                    "TERCERO A DEPURAR");

            cerrarResultSet(rs);
            cerrarStatement(ps);

            where = " WHERE ";
            for (int i = 0; i < codTerAEliminar.length; i++) {
                if (i != codTerAEliminar.length - 1) {
                    where = where.concat(campos.getAtributo("codTerceroEXT") + "=" + codTerAEliminar[i] + " OR ");
                } else {
                    where = where.concat(campos.getAtributo("codTerceroEXT") + "=" + codTerAEliminar[i]);
                }

            }
            sql = "UPDATE E_EXT " +
                    "SET " + campos.getAtributo("codTerceroEXT") + " = ? " +
                    ", " + campos.getAtributo("numVersionEXT") + " = ? " +
                    ", " + campos.getAtributo("codDomicilioEXT") + " = ? " + where;
            if (m_Log.isDebugEnabled()) m_Log.debug(sql);

            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, Integer.parseInt(codTerDepurado));
            ps.setInt(i++, numVersion);
            ps.setInt(i, Integer.parseInt(codDomPrincipal));
            ps.executeUpdate();
            cerrarStatement(ps);
            commitTransaction(bd,con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            rollBackTransaction(bd, con, e);
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(ps);
            devolverConexion(bd, con);
        }
    }

    public int setDomicilioTercero(TercerosValueObject terVO,String[] params)throws TechnicalException{
        DomicilioSimpleValueObject domVO =
                (DomicilioSimpleValueObject)terVO.getDomicilios().get(0);
        int Normalizado=Integer.parseInt(domVO.getNormalizado());
        String sql = "";
        int max = 0;
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs = null;
        Statement state = null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            // Caso domicilio no normalizado. Se inserta en la tabla T_DOM que siempre tiene valor 2 para
            // el campo normalizadoDOM, y el otro campo es el id.
            // Además se inserta en la tabla de domicilios T_DNN (Se crea un domicilio nuevo).
            if(Normalizado==2){
                sql = "SELECT "+bd.funcionMatematica(bd.FUNCIONMATEMATICA_MAX,new String[]{(String)campos.getAtributo("idDomicilioDOM")})+" AS MAXIMO FROM T_DOM";
                state = con.createStatement();
                rs=state.executeQuery(sql);
                rs.next();
                max = rs.getInt("MAXIMO");
                cerrarResultSet(rs);
                cerrarStatement(state);
                m_Log.debug(new Integer(max));
                max++;
                sql = "INSERT INTO T_DOM("+
                        campos.getAtributo("idDomicilioDOM")+"," +
                        campos.getAtributo("normalizadoDOM")+
                        ") VALUES("+max+","+ domVO.getNormalizado() +")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                state = con.createStatement();
                state.executeUpdate(sql);
                cerrarStatement(state);
                if(((!domVO.getCodigoVia().equals(""))&&
                        (!domVO.getCodigoVia().equals("0")))||
                        ((domVO.getCodigoVia().equals("0"))&&
                                (domVO.getDomicilio().equals("TRAMO SIN VIA")))){

                    domVO.setCodigoVia(domVO.getIdVia());
                }else{
                    domVO.setCodigoVia("");
                }

                if (domVO.getIdTipoVia().equalsIgnoreCase("") || domVO.getIdTipoVia()==null)
                    domVO.setIdTipoVia("null");

                sql = "INSERT INTO T_DNN("+
                        campos.getAtributo("idDomicilioDNN")+","+
                        campos.getAtributo("idTipoViaDNN")+","+
                        campos.getAtributo("codPaisDNN")+","+
                        campos.getAtributo("codProvDNN")+","+
                        campos.getAtributo("codMunDNN")+","+
                        campos.getAtributo("codPaisViaDNN")+","+
                        campos.getAtributo("codProvViaDNN")+","+
                        campos.getAtributo("codMunViaDNN")+","+
                        campos.getAtributo("codViaDNN")+","+
                        campos.getAtributo("numDesdeDNN")+","+
                        campos.getAtributo("letraDesdeDNN")+","+
                        campos.getAtributo("numHastaDNN")+","+
                        campos.getAtributo("letraHastaDNN")+","+
                        campos.getAtributo("bloqueDNN")+","+
                        campos.getAtributo("portalDNN")+","+
                        campos.getAtributo("escaleraDNN")+","+
                        campos.getAtributo("plantaDNN")+","+
                        campos.getAtributo("puertaDNN")+","+
                        campos.getAtributo("domicilioDNN")+","+
                        campos.getAtributo("codPostalDNN")+","+
                        campos.getAtributo("barriadaDNN")+","+
                        campos.getAtributo("situacionDNN")+","+
                        campos.getAtributo("fechaAltaDNN")+","+
                        campos.getAtributo("usuarioAltaDNN")+ ","+
                        campos.getAtributo("codPaisESIDNN")+ "," +
                        campos.getAtributo("codProvESIDNN")+ "," +
                        campos.getAtributo("codMunESIDNN")+ "," +
                        campos.getAtributo("codESIDNN")+
                        ") VALUES(" + max + "," + domVO.getIdTipoVia() + "," +
                        campos.getAtributo("codigoPais") + "," + domVO.getIdProvincia() + "," +
                        domVO.getIdMunicipio()+ ",";

                  if (domVO.getIdPaisVia()  == null ||domVO.getIdPaisVia() .equals("")) {
                    sql += "null,";
                } else {
                    sql += domVO.getIdPaisVia() + ",";
                }

                 if (domVO.getIdProvinciaVia()  == null ||domVO.getIdProvinciaVia() .equals("")) {
                    sql += "null,";
                } else {
                    sql += domVO.getIdProvinciaVia()  + ",";
                }

                if (domVO.getIdMunicipioVia() == null ||domVO.getIdMunicipioVia().equals("")) {
                    sql += "null,";
                } else {
                    sql +=domVO.getIdMunicipioVia() + ",";
                }
                if (domVO.getIdVia() == null || domVO.getIdVia().equals("")) {
                    sql += "null,";
                } else {
                    sql +=domVO.getIdVia() + ",";
                }

                if (domVO.getNumDesde() == null || domVO.getNumDesde().equals("")) {
                    sql += "null,";
                } else {
                    sql += domVO.getNumDesde() + ",";
                }
                sql += bd.addString(domVO.getLetraDesde());
                if (domVO.getNumHasta() == null || domVO.getNumHasta().equals("")) {
                    sql += ",null,";
                } else {
                    sql +=","+domVO.getNumHasta() + ",";
                }
                sql += bd.addString(domVO.getLetraHasta()) + "," + bd.addString(domVO.getBloque()) + "," +


                        bd.addString(domVO.getPortal()) + "," + bd.addString(domVO.getEscalera()) + "," +
                        bd.addString(domVO.getPlanta()) + "," + bd.addString(domVO.getPuerta()) + "," +
                        bd.addString(domVO.getDomicilio()) + "," + bd.addString(domVO.getCodigoPostal()) + "," +
                        bd.addString(domVO.getBarriada()) + ",'A'," +
                        bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null)+","+ terVO.getUsuarioAlta() + ",";
                if (domVO.getCodESI()!=null){
                    if (!"".equals((String) domVO.getCodESI()) ){
                        sql +=  campos.getAtributo("codigoPais") + ","	+ domVO.getIdProvincia()
                                + "," + domVO.getIdMunicipio() + "," + domVO.getCodESI();
                    } else {
                        sql += "null,null,null,null ";
                    }
                } else {
                    sql += "null,null,null,null ";
                }

                sql += ")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state=con.createStatement();
                state.executeUpdate(sql);
                cerrarStatement(state);
            }
            // Caso domicilio normalizado (Normalizado != 2)
            else{
                max=Integer.parseInt(terVO.getIdDomicilio());
            }

            // Ahora viene el código que se ejecuta sea normalizado o no el domicilio.
            // Primero se comprueba si existe la relación entre domicilio y tercero.
            String existe = "no";
            String situacionExiste ="A";
            sql = "SELECT * FROM T_DOT WHERE " + campos.getAtributo("idDomicilioDOT") + "=" +
                    max + " AND " + campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();
            rs=state.executeQuery(sql);
            if(rs.next()) {
                existe = "si";
                situacionExiste = rs.getString((String) campos.getAtributo("situacionDOT"));
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            // Si no existe registro para la relación entre el domicilio y el tercero se crea nueva.
            state = con.createStatement();
            if("no".equals(existe)) {
                sql = "INSERT INTO T_DOT("+
                        campos.getAtributo("idDomicilioDOT")+","+
                        campos.getAtributo("idTerceroDOT")+","+
                        campos.getAtributo("tipoOcupacionDOT")+","+
                        campos.getAtributo("situacionDOT")+","+
                        campos.getAtributo("fechaRelacionDOT")+","+
                        campos.getAtributo("usuarioRelacionDOT")+","+
                        campos.getAtributo("enPadronDOT")+
                        ") VALUES(" + max + "," +
                        terVO.getIdentificador() + "," + bd.addString(domVO.getCodTipoUso()) + ",'A'," +
                        bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null) + "," + terVO.getUsuarioAlta() +
                        "," + domVO.getEnPadron() + ")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state.executeUpdate(sql);
            }
            // Si la relación existe se comprueba si esta de baja y en ese caso se dá de alta.
            else {
                if("B".equals(situacionExiste)){
                    //Lo recuperamos (damos de alta)
                    sql = "UPDATE T_DOT SET " +
                            campos.getAtributo("situacionDOT") + "='A' WHERE " +
                            campos.getAtributo("idDomicilioDOT") + "=" + max + " AND " +
                            campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                   state.executeUpdate(sql);
                }

            }
            cerrarStatement(state);
            // Por ultimo, comprobamos si el domicilio se ha marcado como principal del
            // tercero. En ese caso anotamos su id en la columna TER_DOM de T_TER.
            state = con.createStatement();
            if (domVO.getEsDomPrincipal().equals("true")) {
                sql = "UPDATE T_TER SET TER_DOM = " + max +
                     " WHERE TER_COD = " + terVO.getIdentificador();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state.executeUpdate(sql);
            }else {
                sql = "UPDATE T_TER SET TER_DOM = " + terVO.getDomPrincipal() +
                     " WHERE TER_COD = " + terVO.getIdentificador();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state.executeUpdate(sql);
            }
            cerrarStatement(state);
            commitTransaction(bd,con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            rollBackTransaction(bd, con, e);
            max=0;
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return max;
    }
  public int setDomicilioTerceroRapido(TercerosValueObject terVO,String[] params)throws TechnicalException{
        DomicilioSimpleValueObject domVO =
                (DomicilioSimpleValueObject)terVO.getDomicilios().get(0);
        int Normalizado=Integer.parseInt(domVO.getNormalizado());
        String sql = "";
        int max = 0;
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
           m_Log.debug("******  DENTRO DE INSERTAR DOMICILIO   ******");
         m_Log.debug("******   nORMALIZADO"+domVO.getNormalizado());
         m_Log.debug("******   PAIS"+domVO.getIdPais());
          m_Log.debug("******   PROVINCIA"+domVO.getProvincia());
          m_Log.debug("******   MUNICIPIO"+ domVO.getMunicipio());
           m_Log.debug("******   DOMICILIO"+ domVO.getDomicilio());
             m_Log.debug("******   TERCERO"+  terVO.getUsuarioAlta());



        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);

            // Caso domicilio no normalizado. Se inserta en la tabla T_DOM que siempre tiene valor 2 para
            // el campo normalizadoDOM, y el otro campo es el id.
            // Además se inserta en la tabla de domicilios T_DNN (Se crea un domicilio nuevo).
            if(Normalizado==2){
                sql = "SELECT "+bd.funcionMatematica(bd.FUNCIONMATEMATICA_MAX,new String[]{(String)campos.getAtributo("idDomicilioDOM")})+" AS MAXIMO FROM T_DOM";
                state = con.createStatement();
                rs=state.executeQuery(sql);
                rs.next();
                max = rs.getInt("MAXIMO");
                cerrarResultSet(rs);
                cerrarStatement(state);
                m_Log.debug(new Integer(max));
                max++;

                state = con.createStatement();
                sql = "INSERT INTO T_DOM("+
                        campos.getAtributo("idDomicilioDOM")+"," +
                        campos.getAtributo("normalizadoDOM")+
                        ") VALUES("+max+","+bd.addString(domVO.getNormalizado())+")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state.executeUpdate(sql);
                cerrarStatement(state);

                state = con.createStatement();
                sql = "INSERT INTO T_DNN("+
                        campos.getAtributo("idDomicilioDNN")+","+
                        campos.getAtributo("codPaisDNN")+","+
                        campos.getAtributo("codProvDNN")+","+
                        campos.getAtributo("codMunDNN")+","+
                        campos.getAtributo("domicilioDNN")+","+
                        campos.getAtributo("situacionDNN")+","+
                        campos.getAtributo("fechaAltaDNN")+","+
                        campos.getAtributo("usuarioAltaDNN")+
                        ") VALUES(" + max + "," + domVO.getIdPais() + "," + domVO.getIdProvincia() + "," +
                        domVO.getIdMunicipio() + "," + bd.addString(domVO.getDomicilio()) + ",'A'," +
                        bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null)+","+ bd.addString(terVO.getUsuarioAlta())+")";

                if(m_Log.isDebugEnabled()) m_Log.debug("  SQL INSERTAR DOMICILIO : "+sql);
                state.executeUpdate(sql);
                cerrarStatement(state);
            }
            // Caso domicilio normalizado (Normalizado != 2)
            else{
                max=Integer.parseInt(terVO.getIdDomicilio());
            }

            // Ahora viene el código que se ejecuta sea normalizado o no el domicilio.
            // Primero se comprueba si existe la relación entre domicilio y tercero.
            String existe = "no";
            String situacionExiste ="A";
            sql = "SELECT * FROM T_DOT WHERE " + campos.getAtributo("idDomicilioDOT") + "=" +
                    max + " AND " + campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();
            rs=state.executeQuery(sql);

            if(rs.next()) {
                existe = "si";
                situacionExiste = rs.getString((String) campos.getAtributo("situacionDOT"));
            }
            cerrarResultSet(rs);
            cerrarStatement(state);

            // Si no existe registro para la relación entre el domicilio y el tercero se crea nueva.
            state = con.createStatement();
            if("no".equals(existe)) {
                sql = "INSERT INTO T_DOT("+
                        campos.getAtributo("idDomicilioDOT")+","+
                        campos.getAtributo("idTerceroDOT")+","+
                        campos.getAtributo("tipoOcupacionDOT")+","+
                        campos.getAtributo("situacionDOT")+","+
                        campos.getAtributo("fechaRelacionDOT")+","+
                        campos.getAtributo("usuarioRelacionDOT")+","+
                        campos.getAtributo("enPadronDOT")+
                        ") VALUES(" + max + "," +
                        terVO.getIdentificador() + "," + bd.addString(domVO.getCodTipoUso()) + ",'A'," +
                        bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null) + "," + bd.addString(terVO.getUsuarioAlta()) +
                        "," + bd.addString(domVO.getEnPadron()) + ")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state.executeUpdate(sql);
            }
            // Si la relación existe se comprueba si esta de baja y en ese caso se dá de alta.
            else {
                if("B".equals(situacionExiste)){
                    //Lo recuperamos (damos de alta)
                    sql = "UPDATE T_DOT SET " +
                            campos.getAtributo("situacionDOT") + "='A' WHERE " +
                            campos.getAtributo("idDomicilioDOT") + "=" + max + " AND " +
                            campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    state.executeUpdate(sql);
                }

            }
            cerrarStatement(state);

            commitTransaction(bd,con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            rollBackTransaction(bd, con, e);
            max=0;
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return max;
    }
    /**
     * comprueba si existe un domicilio en la base de datos. Al permitir
     * la recuperación de domicilio de otras bases de datos se tiene que comprobar
     * a la hora de insertar un domicilio si este ya existe en la base de datos.
     * @return int
     */
    public int existeDomicilio(TercerosValueObject terVO,String[] params) throws TechnicalException
    {
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs = null;
        Statement state = null;
        int resultado = 0;
        String sql="";

        try{

        bd = new AdaptadorSQLBD(params);
        con = bd.getConnection();
        state = con.createStatement();

        DomicilioSimpleValueObject domVO = (DomicilioSimpleValueObject)terVO.getDomicilios().get(0);

        sql = "SELECT "+ campos.getAtributo("idDomicilioDNN") + " FROM T_DNN WHERE "+
                campos.getAtributo("codPaisDNN")+" = "+ campos.getAtributo("codigoPais") + " and ";
        if (!(domVO.getIdTipoVia().equals("")))
            sql += campos.getAtributo("idTipoViaDNN")+" = "+ domVO.getIdTipoVia() + " and  ";
        else sql += campos.getAtributo("idTipoViaDNN")+" IS NULL and  ";
        if(!(domVO.getIdProvincia().equals("")))
            sql += campos.getAtributo("codProvDNN")+" = "+ domVO.getIdProvincia() + " and ";
        else sql += campos.getAtributo("codProvDNN")+" IS NULL and  ";
        if (!(domVO.getIdMunicipio()).equals(""))
            sql += campos.getAtributo("codMunDNN")+" = "+ domVO.getIdMunicipio() + " and ";
        else sql += campos.getAtributo("codMunDNN")+" IS NULL and  ";
        if(!(domVO.getIdPaisVia().equals("")))
            sql += campos.getAtributo("codPaisViaDNN")+" = " + domVO.getIdPaisVia() + " and ";
        else sql += campos.getAtributo("codPaisViaDNN")+" IS NULL and  ";
        if(!(domVO.getIdProvinciaVia().equals("")))
            sql += campos.getAtributo("codProvViaDNN")+" = " +domVO.getIdProvinciaVia() + " and ";
        else sql += campos.getAtributo("codProvViaDNN")+" IS NULL and  ";
        if(!( domVO.getIdMunicipioVia().equals("")))
            sql += campos.getAtributo("codMunViaDNN")+" = "+ domVO.getIdMunicipioVia() + " and ";
        else sql += campos.getAtributo("codMunViaDNN")+" IS NULL and  ";
        if(!(domVO.getIdVia().equals("")))
            sql += campos.getAtributo("codViaDNN")+" = "+ domVO.getIdVia() + " and ";
        else sql += campos.getAtributo("codViaDNN")+" IS NULL and  ";
        if(!(domVO.getNumDesde()).equals(""))
            sql += campos.getAtributo("numDesdeDNN")+" = "+ domVO.getNumDesde() + " and ";
        else sql += campos.getAtributo("numDesdeDNN")+" IS NULL and  ";
        if(!(domVO.getLetraDesde().equals("")))
            sql += campos.getAtributo("letraDesdeDNN")+" = "+ bd.addString(domVO.getLetraDesde()) + " and ";
        else sql += campos.getAtributo("letraDesdeDNN")+" IS NULL and  ";
        if(!(domVO.getNumHasta().equals("")))
            sql += campos.getAtributo("numHastaDNN")+" = "+ domVO.getNumHasta() + " and ";
        else sql += campos.getAtributo("numHastaDNN")+" IS NULL and  ";
        if(!(domVO.getLetraHasta().equals("")))
            sql += campos.getAtributo("letraHastaDNN")+" = "+ bd.addString(domVO.getLetraHasta()) + " and ";
        else sql += campos.getAtributo("numHastaDNN")+" IS NULL and  ";
        if(!(domVO.getBloque().equals("")))
            sql += campos.getAtributo("bloqueDNN")+" = "+ bd.addString(domVO.getBloque()) + " and ";
        else sql += campos.getAtributo("bloqueDNN")+" IS NULL and  ";
        if(!(domVO.getPortal().equals("")))
            sql += campos.getAtributo("portalDNN")+" = "+ bd.addString(domVO.getPortal()) + " and ";
        else sql += campos.getAtributo("portalDNN")+" IS NULL and  ";
        if(!(domVO.getEscalera().equals("")))
            sql += campos.getAtributo("escaleraDNN")+" = "+ bd.addString(domVO.getEscalera()) + " and ";
        else sql += campos.getAtributo("escaleraDNN")+" IS NULL and  ";
        if (!(domVO.getPlanta().equals("")))
            sql += campos.getAtributo("plantaDNN")+" = "+ bd.addString(domVO.getPlanta()) + " and ";
        else sql += campos.getAtributo("plantaDNN")+" IS NULL and  ";
        if(!( domVO.getPuerta().equals("")))
            sql += campos.getAtributo("puertaDNN")+" = "+ bd.addString(domVO.getPuerta()) + " and ";
        else sql += campos.getAtributo("puertaDNN")+" IS NULL and  ";
        if(!(domVO.getDomicilio().equals("")))
            sql += campos.getAtributo("domicilioDNN")+" = "+ bd.addString(domVO.getDomicilio()) + " and ";
        else sql += campos.getAtributo("domicilioDNN")+" IS NULL and  ";
        if(!(domVO.getCodigoPostal().equals("")))
            sql += campos.getAtributo("codPostalDNN")+" = "+ bd.addString(domVO.getCodigoPostal()) + " and ";
        else sql += campos.getAtributo("codPostalDNN")+" IS NULL and  ";
        if(!(domVO.getBarriada().equals("")))
            sql += campos.getAtributo("barriadaDNN")+" = "+domVO.getBarriada() + " and  ";
        else sql += campos.getAtributo("barriadaDNN")+" IS NULL and  ";
        sql += campos.getAtributo("situacionDNN")+" = 'A'";

        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs=state.executeQuery(sql);
        boolean seguir = rs.next();

            if(seguir)
                resultado = rs.getInt((String) campos.getAtributo("idDomicilioDNN"));

            cerrarResultSet(rs);
            cerrarStatement(state);

        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
			devolverConexion(bd, con);
        }
        return resultado;
    }

    public void updateDomicilioTercero(TercerosValueObject terVO,String[] params)throws TechnicalException{
        DomicilioSimpleValueObject domVO =
                (DomicilioSimpleValueObject)terVO.getDomicilios().get(0);
        int Normalizado=Integer.parseInt(domVO.getNormalizado());
        String sql = "";
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            if(Normalizado==2){
                if(((!domVO.getCodigoVia().equals("")))||((domVO.getCodigoVia().equals("0"))&&
                                (domVO.getDomicilio().equals("TRAMO SIN VIA")))){
                    domVO.setCodigoVia(domVO.getIdVia());
                }else{
                    domVO.setCodigoVia("");
                }
                sql = "UPDATE T_DNN SET " +
                        campos.getAtributo("idTipoViaDNN") + "=" + bd.addString(domVO.getIdTipoVia()) + "," +
                        campos.getAtributo("codPaisDNN") + "=" + campos.getAtributo("codigoPais") + "," +
                        campos.getAtributo("codProvDNN") + "=" + domVO.getIdProvincia() + "," +
                        campos.getAtributo("codMunDNN") + "=" + domVO.getIdMunicipio() + "," +
                        campos.getAtributo("codPaisViaDNN") + "=" + bd.addString(domVO.getIdPaisVia()) + "," +
                        campos.getAtributo("codProvViaDNN") + "=" + bd.addString(domVO.getIdProvinciaVia()) + "," +
                        campos.getAtributo("codMunViaDNN") + "=" + bd.addString(domVO.getIdMunicipioVia()) + "," +
                        campos.getAtributo("codViaDNN") + "=" + bd.addString(domVO.getCodigoVia()) + "," +
                        campos.getAtributo("numDesdeDNN") + "=" + bd.addString(domVO.getNumDesde()) + "," +
                        campos.getAtributo("letraDesdeDNN") + "=" + bd.addString(domVO.getLetraDesde()) + ", " +
                        campos.getAtributo("numHastaDNN") + "=" + bd.addString(domVO.getNumHasta()) + "," +
                        campos.getAtributo("letraHastaDNN") + "=" + bd.addString(domVO.getLetraHasta()) + "," +
                        campos.getAtributo("bloqueDNN") + "=" + bd.addString(domVO.getBloque()) + "," +
                        campos.getAtributo("portalDNN") + "=" + bd.addString(domVO.getPortal()) + "," +
                        campos.getAtributo("escaleraDNN") + "=" + bd.addString(domVO.getEscalera()) + "," +
                        campos.getAtributo("plantaDNN") + "=" + bd.addString(domVO.getPlanta()) + "," +
                        campos.getAtributo("puertaDNN") + "=" + bd.addString(domVO.getPuerta()) + "," +
                        campos.getAtributo("domicilioDNN") + "=" + bd.addString(domVO.getDomicilio()) + "," +
                        campos.getAtributo("codPostalDNN") + "=" + bd.addString(domVO.getCodigoPostal()) + "," +
                        campos.getAtributo("barriadaDNN") + "=" + bd.addString(domVO.getBarriada()) + " ";
                if (domVO.getCodESI()!=null){
                    if (!"".equals((String) domVO.getCodESI()) ){
                        sql += "," + campos.getAtributo("codPaisESIDNN") + "=" + campos.getAtributo("codigoPais") + ","
                                + campos.getAtributo("codProvESIDNN")+ "=" + domVO.getIdProvincia() + ","
                                + campos.getAtributo("codMunESIDNN")+ "=" + domVO.getIdMunicipio() + ","
                                + campos.getAtributo("codESIDNN")+ "=" + domVO.getCodESI();
                    } else {
                        sql += "," + campos.getAtributo("codPaisESIDNN") + "= null,"
                                + campos.getAtributo("codProvESIDNN")+ "= null,"
                                + campos.getAtributo("codMunESIDNN")+ "= null,"
                                + campos.getAtributo("codESIDNN")+ "= null ";
                    }
                } else {
                    sql += "," + campos.getAtributo("codPaisESIDNN") + "= null,"
                            + campos.getAtributo("codProvESIDNN")+ "= null,"
                            + campos.getAtributo("codMunESIDNN")+ "= null,"
                            + campos.getAtributo("codESIDNN")+ "= null ";
                }
                sql += " WHERE " + campos.getAtributo("idDomicilioDNN") + "=" + domVO.getIdDomicilio();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state = con.createStatement();
                state.executeUpdate(sql);
                cerrarStatement(state);
            }
            state = con.createStatement();
            sql = "UPDATE T_DOT SET " +
                    campos.getAtributo("tipoOcupacionDOT") + "=" + bd.addString(domVO.getCodTipoUso()) +
                    " WHERE " +
                    campos.getAtributo("idDomicilioDOT") + "=" + domVO.getIdDomicilio() + " AND " +
                    campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state.executeUpdate(sql);
            cerrarStatement(state);
            domVO.setModificado("SI");
            commitTransaction(bd,con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            rollBackTransaction(bd, con, e);
            domVO.setModificado("NO");
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
    }

    public void cambiarDomicilioTercero(TercerosValueObject terVO,String[] params)throws TechnicalException{
        DomicilioSimpleValueObject domVO =
                (DomicilioSimpleValueObject)terVO.getDomicilios().get(0);
        String sql = "";
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();
            sql = "SELECT * FROM T_DOT WHERE " +
                    campos.getAtributo("idDomicilioDOT") + "=" + domVO.getIdDomicilio() + " AND " +
                    campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            String existe = "no";
            boolean bajaDomicilio = false;
            while(rs.next()) {
                existe = "si";
                String sitDom = rs.getString((String) campos.getAtributo("situacionDOT"));
                if ("B".equals(sitDom)){
                    bajaDomicilio=true;
                }
            }
            cerrarResultSet(rs);
            cerrarStatement(state);

            state = con.createStatement();
            if(existe.equals("no")) {
                sql = "INSERT INTO T_DOT("+
                        campos.getAtributo("idDomicilioDOT")+","+
                        campos.getAtributo("idTerceroDOT")+","+
                        campos.getAtributo("tipoOcupacionDOT")+","+
                        campos.getAtributo("situacionDOT")+","+
                        campos.getAtributo("fechaRelacionDOT")+","+
                        campos.getAtributo("usuarioRelacionDOT")+","+
                        campos.getAtributo("enPadronDOT")+
                        ") VALUES(" + domVO.getIdDomicilio() + "," +
                        terVO.getIdentificador() + ",null,'A'," +
                        bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null) + bd.addString(terVO.getUsuarioAlta()) +
                        ",1)";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state.executeUpdate(sql);
            } else {
                if (bajaDomicilio){ // Lo recuperamos
                    sql = "UPDATE T_DOT SET " +
                            campos.getAtributo("situacionDOT") +"='A'," +
                            campos.getAtributo("fechaRelacionDOT") +"="+bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null) + "," +
                            campos.getAtributo("usuarioRelacionDOT") +"=" +bd.addString(terVO.getUsuarioAlta()) +
                            " WHERE " +
                            campos.getAtributo("idDomicilioDOT") + "=" + domVO.getIdDomicilio() + " AND " +
                            campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    state.executeUpdate(sql);
                }
            }
            cerrarStatement(state);
            commitTransaction(bd,con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            rollBackTransaction(bd, con, e);
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
    }

    public void deleteDomicilioTercero(TercerosValueObject terVO,String[] params)throws TechnicalException{
        String sql = "";
        AdaptadorSQLBD bd=null;
        Connection con=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();
            sql = "UPDATE T_DOT SET " +
                    campos.getAtributo("situacionDOT") +"='B'," +
                    campos.getAtributo("fechaRelacionDOT") +"="+bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null) + "," +
                    campos.getAtributo("usuarioRelacionDOT") +"=" + bd.addString(terVO.getUsuarioBaja()) +
                    " WHERE " +
                    campos.getAtributo("idDomicilioDOT") + "=" + terVO.getIdDomicilio() + " AND " +
                    campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state.executeUpdate(sql);
            cerrarStatement(state);

            // Comprobamos si se ha indicado un nuevo domicilio principal del
            // tercero. En ese caso anotamos su id en la columna TER_DOM de T_TER.
            state = con.createStatement();
            if (terVO.getDomPrincipal()!=null && !terVO.getDomPrincipal().equals("")) {
                sql = "UPDATE T_TER SET TER_DOM = " + terVO.getDomPrincipal() +
                     " WHERE TER_COD = " + terVO.getIdentificador();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state.executeUpdate(sql);
            }
            cerrarStatement(state);
            commitTransaction(bd,con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            rollBackTransaction(bd, con, e);
        }finally{
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
    }

    public void depurarDomicilios(String[] params,GeneralValueObject gVO,String[] codDomAEliminar,int codUsuario) throws TechnicalException{
        String sql = "";
        AdaptadorSQLBD bd=null;
        Connection con=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            String codTercero = (String) gVO.getAtributo("codTercero");
            String codDomPrincipal = (String) gVO.getAtributo("codDomPrincipal");

            String where = "(";
            for (int i=0;i<codDomAEliminar.length;i++) {
                if (i!=codDomAEliminar.length-1) {
                    where = where.concat(m_ct.getString("SQL.E_EXT.codDomicilio") + "=" + codDomAEliminar[i] + " OR ");
                } else {
                    where = where.concat(m_ct.getString("SQL.E_EXT.codDomicilio") + "=" + codDomAEliminar[i]) + ")";
                }

            }
            sql = "UPDATE E_EXT SET " +
                  m_ct.getString("SQL.E_EXT.codDomicilio") +"=" + codDomPrincipal +
                  " WHERE " + where + " AND " + m_ct.getString("SQL.E_EXT.codTercero") + "=" + codTercero;
            state = con.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state.executeUpdate(sql);
            cerrarStatement(state);
            where = "(";
            for (int i=0;i<codDomAEliminar.length;i++) {
                if (i!=codDomAEliminar.length-1) {
                    where = where.concat(m_ct.getString("SQL.R_RES.domicTercero") + "=" + codDomAEliminar[i] + " OR ");
                } else {
                    where = where.concat(m_ct.getString("SQL.R_RES.domicTercero") + "=" + codDomAEliminar[i]) + ")";
                }

            }
            sql = "UPDATE R_RES SET " +
                m_ct.getString("SQL.R_RES.domicTercero") +"=" + codDomPrincipal + " WHERE " + where + " AND " +
                m_ct.getString("SQL.R_RES.codTercero") + "=" + codTercero;
            state = con.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state.executeUpdate(sql);
            cerrarStatement(state);

            where = "(";
            for (int i=0;i<codDomAEliminar.length;i++) {
                if (i!=codDomAEliminar.length-1) {
                    where = where.concat(campos.getAtributo("idDomicilioDOT") + "=" + codDomAEliminar[i] + " OR ");
                } else {
                    where = where.concat(campos.getAtributo("idDomicilioDOT") + "=" + codDomAEliminar[i]) + ")";
                }

            }
            sql = "UPDATE T_DOT SET " +
                campos.getAtributo("situacionDOT") +"='B'," +
                campos.getAtributo("fechaRelacionDOT") +"="+bd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + "," +
                campos.getAtributo("usuarioRelacionDOT") +"=" + codUsuario + " WHERE " + where + " AND " +
                campos.getAtributo("idTerceroDOT") + "=" + codTercero;
            state = con.prepareStatement(sql);
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state.executeUpdate(sql);
            cerrarStatement(state);

            commitTransaction(bd,con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            rollBackTransaction(bd, con, e);
        }finally{
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
    }

    public int grabarDomiciliosNoNormalizados(GeneralValueObject gVO,String[] params)throws TechnicalException{
        String sql = "";
        int resultado = 0;
        int resultado1 = 0;
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        int codMaximoDomicilio = 0;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();

            campos.setAtributo("idDomicilioDOM",m_ct.getString("SQL.T_DOM.idDomicilio"));
            sql = "SELECT " + bd.funcionMatematica(AdaptadorSQLBD.FUNCIONMATEMATICA_MAX,new String[]{(String)campos.getAtributo("idDomicilioDOM")}) + " AS maximo FROM T_DOM";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            if (rs.next()) {
                codMaximoDomicilio = rs.getInt("maximo");
            }
            cerrarResultSet(rs);
            cerrarStatement(state);

            if (codMaximoDomicilio > 0) {
                String codVia = (String) gVO.getAtributo("codVia");
                if (codVia.equals("0")) {
                    sql = "SELECT "+ campos.getAtributo("idVIA")+" AS ID FROM T_VIA WHERE "+
                            campos.getAtributo("codPaisVIA")+"="+gVO.getAtributo("codPais")+" AND "+
                            campos.getAtributo("codProvVIA")+"="+gVO.getAtributo("codProvincia")+" AND "+
                            campos.getAtributo("codMunVIA")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                            campos.getAtributo("descVIA")+"="+bd.addString((String)gVO.getAtributo("descVia"))+" AND "+
                            campos.getAtributo("tipoVIA")+"="+gVO.getAtributo("codTipoVia")+" AND "+
                            campos.getAtributo("situacionVIA")+"= 'A'";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    state = con.createStatement();
                    rs = state.executeQuery(sql);
                    boolean existeVia = rs.next();
                    cerrarResultSet(rs);
                    cerrarStatement(state);
                    if(!existeVia) {
                        int idNuevaVia = 0;
                        sql = "SELECT " + bd.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL,new String[]{"MAX("+campos.getAtributo("idVIA")+")","0"})+ "+1 AS MAXIMO" +
                            " FROM T_VIA WHERE " + campos.getAtributo("codProvVIA")+"="+gVO.getAtributo("codProvincia")
                            + " AND " + campos.getAtributo("codMunVIA")+"="+gVO.getAtributo("codMunicipio");

                        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                        state = con.createStatement();
                        rs = state.executeQuery(sql);
                        if(rs.next()){
                            idNuevaVia = rs.getInt("MAXIMO");
                            codVia = Integer.toString(idNuevaVia);
                        }
                        cerrarResultSet(rs);
                        cerrarStatement(state);
                        sql = "INSERT INTO T_VIA("+
                            campos.getAtributo("codPaisVIA")+","+
                            campos.getAtributo("codProvVIA")+","+
                            campos.getAtributo("codMunVIA")+","+
                            campos.getAtributo("idVIA")+","+
                            campos.getAtributo("codVIA")+","+
                            campos.getAtributo("descVIA")+","+
                            campos.getAtributo("nomCorto")+","+
                            campos.getAtributo("tipoVIA")+","+
                            campos.getAtributo("situacionVIA")+","+
                            campos.getAtributo("fechaAlta")+","+
                            campos.getAtributo("usuarioAlta")+","+
                            campos.getAtributo("fechaBaja")+","+
                            campos.getAtributo("usuarioBaja")+","+
                            campos.getAtributo("fechaVigencia")+
                            ") VALUES (" +
                            gVO.getAtributo("codPais")+","+
                            gVO.getAtributo("codProvincia")+","+
                            gVO.getAtributo("codMunicipio")+","+
                            idNuevaVia+","+
                            idNuevaVia+","+
                            bd.addString((String)gVO.getAtributo("descVia"))+","+
                            bd.addString((String)gVO.getAtributo("descVia"))+","+
                            gVO.getAtributo("codTipoVia")+","+
                            "'A',"+bd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+","+
                            gVO.getAtributo("codUsuario")+","+
                            "null,null,"+bd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+")";

                        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                        state = con.createStatement();
                        state.executeUpdate(sql);
                        cerrarStatement(state);
                    } else {
                        codVia = rs.getString("ID");
                    }
                }
                codMaximoDomicilio++;
                sql = "INSERT INTO T_DOM("+
                        campos.getAtributo("idDomicilioDOM")+","+
                        campos.getAtributo("normalizadoDOM")+
                        ") VALUES(" + codMaximoDomicilio + ",2)";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state = con.createStatement();
                resultado = state.executeUpdate(sql);
                cerrarStatement(state);
                if(resultado>0) {
                    sql = "INSERT INTO T_DNN("+
                            campos.getAtributo("idDomicilioDNN")+","+campos.getAtributo("idTipoViaDNN")+","+
                            campos.getAtributo("codPaisDNN")+","+campos.getAtributo("codProvDNN")+","+
                            campos.getAtributo("codMunDNN")+","+campos.getAtributo("codPaisViaDNN")+","+
                            campos.getAtributo("codProvViaDNN")+","+campos.getAtributo("codMunViaDNN")+","+
                            campos.getAtributo("codViaDNN")+","+campos.getAtributo("numDesdeDNN")+","+
                            campos.getAtributo("letraDesdeDNN")+","+campos.getAtributo("numHastaDNN")+","+
                            campos.getAtributo("letraHastaDNN")+","+campos.getAtributo("bloqueDNN")+","+
                            campos.getAtributo("portalDNN")+","+campos.getAtributo("escaleraDNN")+","+
                            campos.getAtributo("plantaDNN")+","+campos.getAtributo("puertaDNN")+","+
                            campos.getAtributo("domicilioDNN")+","+campos.getAtributo("codPostalDNN")+","+
                            campos.getAtributo("situacionDNN")+","+
                            campos.getAtributo("fechaAltaDNN")+","+campos.getAtributo("usuarioAltaDNN")+","+
                            campos.getAtributo("codPaisESIDNN")+ "," + campos.getAtributo("codProvESIDNN")+ ","+
                            campos.getAtributo("codMunESIDNN")+","+ campos.getAtributo("codESIDNN")+ " ," +
                            campos.getAtributo("refCatastralDNN")+
                            ") VALUES (" + codMaximoDomicilio + "," + bd.addString((String)gVO.getAtributo("codTipoVia")) + "," +
                            gVO.getAtributo("codPais") + "," + gVO.getAtributo("codProvincia") + "," +
                            gVO.getAtributo("codMunicipio") + "," + bd.addString((String)gVO.getAtributo("codPais")) + "," +
                            bd.addString((String)gVO.getAtributo("codProvincia")) + "," + bd.addString((String)gVO.getAtributo("codMunicipio")) + "," +
                            bd.addString(codVia) + "," + bd.addString((String)gVO.getAtributo("numDesde")) + "," +
                            bd.addString((String)gVO.getAtributo("letraDesde")) + "," + bd.addString((String)gVO.getAtributo("numHasta")) + "," +
                            bd.addString((String)gVO.getAtributo("letraHasta")) + "," + bd.addString((String)gVO.getAtributo("bloque")) + "," +
                            bd.addString((String)gVO.getAtributo("portal")) + "," + bd.addString((String)gVO.getAtributo("escalera")) + "," +
                            bd.addString((String)gVO.getAtributo("planta")) + "," + bd.addString((String)gVO.getAtributo("puerta")) + "," +
                            bd.addString((String)gVO.getAtributo("domicilio")) + "," + bd.addString((String)gVO.getAtributo("codPostal")) + ", 'A', " +
                            bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null)+ "," +gVO.getAtributo("codUsuario") + ",";

                    if (gVO.getAtributo("codESI")!=null){
                        if (!"".equals( gVO.getAtributo("codESI") ) ){
                            sql += gVO.getAtributo("codPais") + "," +gVO.getAtributo("codProvincia") + "," +
                                    gVO.getAtributo("codMunicipio") + ","  + gVO.getAtributo("codESI");
                        } else {
                            sql += "null,null,null,null ";
                        }
                    } else {
                        sql += "null,null,null,null ";
                    }
                    sql += ", " + bd.addString((String)gVO.getAtributo("refCatastral")) + ")";

                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    state = con.createStatement();
                    resultado1 = state.executeUpdate(sql);
                    cerrarStatement(state);
                }
            }
            commitTransaction(bd, con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            resultado1=0;
            rollBackTransaction(bd, con, e);
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return codMaximoDomicilio;
    }


    /**
     * Graba el domicilio correspondiente a una localización y graba la localización del mismo en el expediente
     * @param gVO
     * @param params
     * @return
     * @throws es.altia.common.exception.TechnicalException
     */
      public int grabarDomiciliosNoNormalizadosLocalizacion(GeneralValueObject gVO,String[] params)throws TechnicalException{
        String sql = "";
        int resultado3=-1;
        int resultado = 0;
        int resultado1 = 0;
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        int codMaximoDomicilio = 0;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();

            campos.setAtributo("idDomicilioDOM",m_ct.getString("SQL.T_DOM.idDomicilio"));
            sql = "SELECT " + bd.funcionMatematica(AdaptadorSQLBD.FUNCIONMATEMATICA_MAX,new String[]{(String)campos.getAtributo("idDomicilioDOM")}) + " AS maximo FROM T_DOM";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            if (rs.next()) {
                codMaximoDomicilio = rs.getInt("maximo");
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            if (codMaximoDomicilio > 0)
            {
                String codVia = (String) gVO.getAtributo("codVia");
                if (codVia.equals("0")) {
                    sql = "SELECT "+ campos.getAtributo("idVIA")+" AS ID FROM T_VIA WHERE "+
                            campos.getAtributo("codPaisVIA")+"="+gVO.getAtributo("codPais")+" AND "+
                            campos.getAtributo("codProvVIA")+"="+gVO.getAtributo("codProvincia")+" AND "+
                            campos.getAtributo("codMunVIA")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                            campos.getAtributo("descVIA")+"="+bd.addString((String)gVO.getAtributo("descVia"))+" AND "+
                            campos.getAtributo("tipoVIA")+"="+gVO.getAtributo("codTipoVia")+" AND "+
                            campos.getAtributo("situacionVIA")+"= 'A'";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    state = con.createStatement();
                    rs = state.executeQuery(sql);
                    boolean existeVia = rs.next();
                    cerrarResultSet(rs);
                    cerrarStatement(state);
                    if(!existeVia) {
                        int idNuevaVia = 0;
                        sql = "SELECT " + bd.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL,new String[]{"MAX("+campos.getAtributo("idVIA")+")","0"})+ "+1 AS MAXIMO" +
                            " FROM T_VIA WHERE " + campos.getAtributo("codProvVIA")+"="+gVO.getAtributo("codProvincia")
                            + " AND " + campos.getAtributo("codMunVIA")+"="+gVO.getAtributo("codMunicipio");

                        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                        state = con.createStatement();
                        rs = state.executeQuery(sql);
                        if(rs.next()){
                            idNuevaVia = rs.getInt("MAXIMO");
                            codVia = Integer.toString(idNuevaVia);
                        }
                        cerrarResultSet(rs);
                        cerrarStatement(state);
                        sql = "INSERT INTO T_VIA("+
                            campos.getAtributo("codPaisVIA")+","+
                            campos.getAtributo("codProvVIA")+","+
                            campos.getAtributo("codMunVIA")+","+
                            campos.getAtributo("idVIA")+","+
                            campos.getAtributo("codVIA")+","+
                            campos.getAtributo("descVIA")+","+
                            campos.getAtributo("nomCorto")+","+
                            campos.getAtributo("tipoVIA")+","+
                            campos.getAtributo("situacionVIA")+","+
                            campos.getAtributo("fechaAlta")+","+
                            campos.getAtributo("usuarioAlta")+","+
                            campos.getAtributo("fechaBaja")+","+
                            campos.getAtributo("usuarioBaja")+","+
                            campos.getAtributo("fechaVigencia")+
                            ") VALUES (" +
                            gVO.getAtributo("codPais")+","+
                            gVO.getAtributo("codProvincia")+","+
                            gVO.getAtributo("codMunicipio")+","+
                            idNuevaVia+","+
                            idNuevaVia+","+
                            bd.addString((String)gVO.getAtributo("descVia"))+","+
                            bd.addString((String)gVO.getAtributo("descVia"))+","+
                            gVO.getAtributo("codTipoVia")+","+
                            "'A',"+bd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+","+
                            gVO.getAtributo("codUsuario")+","+
                            "null,null,"+bd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+")";

                        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                        state = con.createStatement();
                        state.executeUpdate(sql);
                        cerrarStatement(state);
                    } else {
                        codVia = rs.getString("ID");
                    }
                }
                codMaximoDomicilio++;
                sql = "INSERT INTO T_DOM("+
                        campos.getAtributo("idDomicilioDOM")+","+
                        campos.getAtributo("normalizadoDOM")+
                        ") VALUES(" + codMaximoDomicilio + ",2)";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state = con.createStatement();
                resultado = state.executeUpdate(sql);
                cerrarStatement(state);
                if(resultado>0)
                {
                    sql = "INSERT INTO T_DNN("+
                            campos.getAtributo("idDomicilioDNN")+","+campos.getAtributo("idTipoViaDNN")+","+
                            campos.getAtributo("codPaisDNN")+","+campos.getAtributo("codProvDNN")+","+
                            campos.getAtributo("codMunDNN")+","+campos.getAtributo("codPaisViaDNN")+","+
                            campos.getAtributo("codProvViaDNN")+","+campos.getAtributo("codMunViaDNN")+","+
                            campos.getAtributo("codViaDNN")+","+campos.getAtributo("numDesdeDNN")+","+
                            campos.getAtributo("letraDesdeDNN")+","+campos.getAtributo("numHastaDNN")+","+
                            campos.getAtributo("letraHastaDNN")+","+campos.getAtributo("bloqueDNN")+","+
                            campos.getAtributo("portalDNN")+","+campos.getAtributo("escaleraDNN")+","+
                            campos.getAtributo("plantaDNN")+","+campos.getAtributo("puertaDNN")+","+
                            campos.getAtributo("domicilioDNN")+","+campos.getAtributo("codPostalDNN")+","+
                            campos.getAtributo("situacionDNN")+","+
                            campos.getAtributo("fechaAltaDNN")+","+campos.getAtributo("usuarioAltaDNN")+","+
                            campos.getAtributo("codPaisESIDNN")+ "," + campos.getAtributo("codProvESIDNN")+ ","+
                            campos.getAtributo("codMunESIDNN")+","+ campos.getAtributo("codESIDNN")+ " ," +
                            campos.getAtributo("refCatastralDNN")+
                            ") VALUES (" + codMaximoDomicilio + "," + bd.addString((String)gVO.getAtributo("codTipoVia")) + "," +
                            gVO.getAtributo("codPais") + "," + gVO.getAtributo("codProvincia") + "," +
                            gVO.getAtributo("codMunicipio") + "," + bd.addString((String)gVO.getAtributo("codPais")) + "," +
                            bd.addString((String)gVO.getAtributo("codProvincia")) + "," + bd.addString((String)gVO.getAtributo("codMunicipio")) + "," +
                            bd.addString(codVia) + "," + bd.addString((String)gVO.getAtributo("numDesde")) + "," +
                            bd.addString((String)gVO.getAtributo("letraDesde")) + "," + bd.addString((String)gVO.getAtributo("numHasta")) + "," +
                            bd.addString((String)gVO.getAtributo("letraHasta")) + "," + bd.addString((String)gVO.getAtributo("bloque")) + "," +
                            bd.addString((String)gVO.getAtributo("portal")) + "," + bd.addString((String)gVO.getAtributo("escalera")) + "," +
                            bd.addString((String)gVO.getAtributo("planta")) + "," + bd.addString((String)gVO.getAtributo("puerta")) + "," +
                            bd.addString((String)gVO.getAtributo("domicilio")) + "," + bd.addString((String)gVO.getAtributo("codPostal")) + ", 'A', " +
                            bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null)+ "," +gVO.getAtributo("codUsuario") + ",";

                    if (gVO.getAtributo("codESI")!=null){
                        if (!"".equals( gVO.getAtributo("codESI") ) ){
                            sql += gVO.getAtributo("codPais") + "," +gVO.getAtributo("codProvincia") + "," +
                                    gVO.getAtributo("codMunicipio") + ","  + gVO.getAtributo("codESI");
                        } else {
                            sql += "null,null,null,null ";
                        }
                    } else {
                        sql += "null,null,null,null ";
                    }
                    sql += ", " + bd.addString((String)gVO.getAtributo("refCatastral")) + ")";

                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    state = con.createStatement();
                    resultado1 = state.executeUpdate(sql);
                    cerrarStatement(state);

                    if(resultado1==1){
                        // Se graba la localización asociada al expediente
                        //String codMunicipio = (String) gVO.getAtributo("codMunicipio");
                        String ejercicio = (String) gVO.getAtributo("ejercicio");
                        String numeroExpediente = (String) gVO.getAtributo("numeroExpediente");
                        String localizacion = (String) gVO.getAtributo("localizacion");
                        //String referencia = (String) gVO.getAtributo("referencia");
                        String codMunExp=(String) gVO.getAtributo("codMunExp");

                        state = con.createStatement();

                         String sqlExp = "UPDATE E_EXP SET EXP_LOC='" + localizacion + "',EXP_CLO='" + codMaximoDomicilio + "' WHERE EXP_MUN=" + codMunExp + " AND EXP_EJE=" + ejercicio + " AND EXP_NUM='" + numeroExpediente + "'";
                         if(m_Log.isDebugEnabled()) m_Log.debug(sqlExp);
                         resultado3 = state.executeUpdate(sqlExp);
                         cerrarStatement(state);
                    }
                }
            }
            commitTransaction(bd, con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            resultado1=0;
            rollBackTransaction(bd, con, e);
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }

        if(resultado3==1)
            return codMaximoDomicilio;
        else return -1;
    }



    public boolean eliminarDomicilioNoNormalizado(GeneralValueObject gVO,String[] params)throws TechnicalException{
        boolean eliminado = false;
        String sql = "";
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();
            boolean utilizado = false;

            /* Comprobar que no se usa */
            sql = "SELECT * FROM R_RES WHERE " +
                    m_ct.getString("SQL.R_RES.domicTercero") + "=" + gVO.getAtributo("codLocalizacion");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while(rs.next()) {
                utilizado = true;
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            state = con.createStatement();
            sql = "SELECT * FROM E_EXT WHERE " +
                    m_ct.getString("SQL.E_EXT.codDomicilio") + "=" + gVO.getAtributo("codLocalizacion");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while(rs.next()) {
                utilizado = true;
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            state = con.createStatement();

            sql = "SELECT * FROM T_HTE WHERE " +
                    m_ct.getString("SQL.T_HTE.idDomicilio") + "=" + gVO.getAtributo("codLocalizacion") ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while(rs.next()) {
                utilizado = true;
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            state = con.createStatement();

            sql = "SELECT * FROM T_DOT WHERE " +
                    campos.getAtributo("idDomicilioDOT") + "=" + gVO.getAtributo("codLocalizacion");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while(rs.next()) {
                utilizado = true;
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            state = con.createStatement();

            sql = "SELECT * FROM E_EXP WHERE " +
                    m_ct.getString("SQL.E_EXP.codLocalizacion") + "=" + gVO.getAtributo("codLocalizacion") ;

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while(rs.next()) {
                utilizado = true;
            }
            cerrarResultSet(rs);
            cerrarStatement(state);

            if (!utilizado) {
                state = con.createStatement();
                sql = "DELETE FROM T_DNN " +
                        " WHERE " + m_ct.getString("SQL.T_DNN.idDomicilio") + "=" + gVO.getAtributo("codLocalizacion");
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                int res = state.executeUpdate(sql);
                cerrarStatement(state);
                if (res > 0) {
                    state = con.createStatement();
                    sql = "DELETE FROM T_DOM " +
                            " WHERE " + campos.getAtributo("idDomicilioDOM") + "=" + gVO.getAtributo("codLocalizacion");
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    state.executeUpdate(sql);
                    cerrarStatement(state);
                }
                eliminado = true;
            }
            commitTransaction(bd,con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            eliminado=false;
            rollBackTransaction(bd, con, e);
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return eliminado;
    }

    public boolean eliminarDomicilioTerceroPadron(TercerosValueObject terVO,String[] params)throws TechnicalException{
        boolean eliminado = false;
        String sql = "";
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();
            boolean utilizado = false;

            /* Comprobar que no se usa */
            sql = "SELECT * FROM R_RES WHERE " +
                    m_ct.getString("SQL.R_RES.codTercero") + "=" + terVO.getIdentificador() + " AND " +
                    m_ct.getString("SQL.R_RES.domicTercero") + "=" + terVO.getIdDomicilio();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while(rs.next()) {
                utilizado = true;
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            state = con.createStatement();

            sql = "SELECT * FROM E_EXT WHERE " +
                    m_ct.getString("SQL.E_EXT.codTercero") + "=" + terVO.getIdentificador() + " AND " +
                    m_ct.getString("SQL.E_EXT.codDomicilio") + "=" + terVO.getIdDomicilio()+ " AND " +
                    m_ct.getString("SQL.E_EXT.verTercero") + "=" + terVO.getVersion() ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while(rs.next()) {
                utilizado = true;
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            state = con.createStatement();


            sql = "SELECT * FROM T_HTE WHERE " +
                    m_ct.getString("SQL.T_HTE.identificador") + "=" + terVO.getIdentificador() + " AND " +
                    m_ct.getString("SQL.T_HTE.version") + "=" + terVO.getIdDomicilio()+ " AND " +
                    m_ct.getString("SQL.T_HTE.idDomicilio") + "=" + terVO.getVersion() ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while(rs.next()) {
                utilizado = true;
            }
            cerrarResultSet(rs);
            cerrarStatement(state);


            if (!utilizado) {
                state = con.createStatement();
                sql = "DELETE FROM T_DOT " +
                        " WHERE " + campos.getAtributo("idDomicilioDOT") + "=" + terVO.getIdDomicilio() + " AND " +
                        campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state.executeUpdate(sql);
                /* Fin dos soluciones.*/


                eliminado = true;
                cerrarStatement(state);
            }
            commitTransaction(bd,con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            eliminado=false;
            rollBackTransaction(bd, con, e);
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return eliminado;
    }

    public boolean eliminarTerceroPadron(TercerosValueObject terVO,String[] params)throws TechnicalException{
        // Si no tiene domicilios de alta se elimina, no tenemos en cuenta la version
        // si se ha utilizado con alguna se manteine toda la informacion
        boolean eliminado = false;
        String sql = "";
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        boolean utilizado = false;

        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();

            /* Utilizacion de ese hab-dom */

            sql = "SELECT * FROM R_RES WHERE " +
                    m_ct.getString("SQL.R_RES.codTercero") + "=" + terVO.getIdentificador()
                    ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while(rs.next()) {
                utilizado = true;
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            state = con.createStatement();

            sql = "SELECT * FROM E_EXT WHERE " +
                    m_ct.getString("SQL.E_EXT.codTercero") + "=" + terVO.getIdentificador()
                    ;
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            while(rs.next()) {
                utilizado = true;
            }
            cerrarResultSet(rs);
            cerrarStatement(state);

            if (!utilizado) {
                boolean masDomicilios = false;
                state = con.createStatement();
                /* Comprobar que no tiene mas domicilios */
                sql = "SELECT * FROM T_DOT WHERE " +
                        campos.getAtributo("situacionDOT") +"='A' AND " +
                        campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador()
                        +" AND " + campos.getAtributo("idDomicilioDOT") + "<>" + terVO.getIdDomicilio();// + domicilios q ese

                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs = state.executeQuery(sql);
                while(rs.next()) {
                    masDomicilios = true;
                }
                cerrarResultSet(rs);
                cerrarStatement(state);

                if (!masDomicilios) {
                    state = con.createStatement();
                    sql = "DELETE FROM T_HTE WHERE " +
                            m_ct.getString("SQL.T_HTE.identificador") + "=" + terVO.getIdentificador();
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    state.executeUpdate(sql);
                cerrarStatement(state);
                state = con.createStatement();
                    sql = "DELETE FROM T_TER WHERE " +	campos.getAtributo("idTerceroTER")+ "=" + terVO.getIdentificador();
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    state.executeUpdate(sql);
                    eliminado = true;
                cerrarStatement(state);
                state = con.createStatement();
                    sql = "DELETE FROM T_DOT " +
                            " WHERE " + campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    state.executeUpdate(sql);
                    /* Fin dos soluciones. */
                cerrarStatement(state);
                }
            }
            commitTransaction(bd,con);
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            eliminado=false;
            rollBackTransaction(bd, con, e);
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
            devolverConexion(bd, con);
        }
        return eliminado;
    }

    public Vector getTercerosRepByFec(String[] params,GeneralValueObject gVO)
            throws TechnicalException {
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        PreparedStatement ps=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.consultaSinAcentos(con, params);
            String fechaInicio = (String) gVO.getAtributo("fechaInicio");
            String fechaFin = (String) gVO.getAtributo("fechaFin");

            String parteFromInterna = "TER_NOC";
            String parteWhereInterna = "HTE_FOP BETWEEN " + bd.convertir("?", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) +
                    " AND " + bd.convertir("?", AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) + " AND TER_SIT='A'";
            ArrayList join = new ArrayList();
            join.add("T_HTE");
            join.add("INNER");
            join.add("T_TER");
            join.add("HTE_TER=TER_COD AND HTE_NVR=TER_NVE");
            join.add("false");

            String sqlInterna = bd.join(parteFromInterna, parteWhereInterna, (String[]) join.toArray(new String[]{}));

            sqlInterna = "SELECT " + bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM,new String[]{"TER_NOC"}) + " AS TER_NOC, COUNT(*) AS NUM FROM (" + sqlInterna + ") a GROUP BY "+
                    bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"TER_NOC"});

            String parteFrom = "TID_DES, TER_DOC, TER_COD, TER_NOM, TER_AP1, TER_AP2";
            String parteWhere = "NUM>1 AND TER_SIT='A'";
            join = new ArrayList();
            join.add("T_TER");
            join.add("INNER");
            join.add("T_TID");
            join.add("TER_TID=TID_COD");
            join.add("INNER");
            join.add("(" + sqlInterna + ") a");
            join.add(bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM,new String[]{"T_TER.TER_NOC"}) + "= a.TER_NOC");
            join.add("false");

            String sql = bd.join(parteFrom, parteWhere, (String[]) join.toArray(new String[]{})) +
                    " ORDER BY T_TER.TER_NOC";

            ps = con.prepareStatement(sql);
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("FECHA_INICIO: " +  fechaInicio);
                m_Log.debug("FECHA_FIN: " + fechaFin);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                GeneralValueObject tupla = new GeneralValueObject();
                tupla.setAtributo("T_TID.TID_DES", rs.getString(1));
                tupla.setAtributo("T_TER.TER_DOC", rs.getString(2));
                tupla.setAtributo("T_TER.TER_COD", Integer.toString(rs.getInt(3)));
                tupla.setAtributo("T_TER.TER_NOM", rs.getString(4));
                tupla.setAtributo("T_TER.TER_AP1", rs.getString(5));
                tupla.setAtributo("T_TER.TER_AP2", rs.getString(6));
                resultado.add(tupla);
            }
            cerrarResultSet(rs);
            cerrarStatement(ps);
        }catch (Exception e){
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(ps);
            devolverConexion(bd, con);
        }
        return resultado;
    }

    public Vector getTercerosRepByNomCompleto(String[] params,GeneralValueObject gVO)
            throws TechnicalException {
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        PreparedStatement ps=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            String nombreCompleto = (String) gVO.getAtributo("nombreCompleto");
            String tipoDocumento = (String) gVO.getAtributo("codTipoDocumento");
            String documento = (String) gVO.getAtributo("documento");

           
            TransformacionAtributoSelect transformador = new TransformacionAtributoSelect(bd,con);
            String condicionNombre = transformador.construirCondicionWhereConOperadores(
                    bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"TER_NOC"}), nombreCompleto.trim(),false);
            String condicionTipoDoc = transformador.construirCondicionWhereConOperadores("TID_COD", tipoDocumento.trim(),false);
            String condicionDoc = transformador.construirCondicionWhereConOperadores("TER_DOC", documento.trim(),false);
            String condicionDocNull = "TER_DOC IS NULL";

            String parteFromInterna = bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"TER_NOC"}) + " AS TER_NOC, COUNT(*) AS NUM";
            String parteWhereInterna = "";
            if (documento.equals("*")) {
                parteWhereInterna = "TER_SIT='A' AND " + condicionNombre + " AND " + condicionTipoDoc + " AND (" + condicionDoc + " OR " + condicionDocNull + ")";
            } else {
                parteWhereInterna = "TER_SIT='A' AND " + condicionNombre + " AND " + condicionTipoDoc + " AND " + condicionDoc;
            }
            ArrayList join = new ArrayList();
            join.add("T_TER");
            join.add("INNER");
            join.add("T_TID");
            join.add("TER_TID=TID_COD");
            join.add("false");
            String sqlInterna = bd.join(parteFromInterna, parteWhereInterna, (String[]) join.toArray(new String[]{})) +
                    " GROUP BY " + bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"TER_NOC"});

            String parteFrom = "TID_DES, TER_DOC, TER_COD, TER_NOM, TER_AP1, TER_AP2";
            String parteWhere = "";
            if (documento.equals("*")) {
                parteWhere = "NUM>1 AND TER_SIT='A' AND " + condicionTipoDoc + " AND (" + condicionDoc + " OR " + condicionDocNull + ")";
            } else {
                parteWhere = "NUM>1 AND TER_SIT='A' AND " + condicionTipoDoc + " AND " + condicionDoc;
            }
            join = new ArrayList();
            join.add("T_TER");
            join.add("INNER");
            join.add("T_TID");
            join.add("TER_TID=TID_COD");
            join.add("INNER");
            join.add("(" + sqlInterna + ") a");
            join.add(bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"T_TER.TER_NOC"})+ " = a.TER_NOC");
            join.add("false");

            String sql = bd.join(parteFrom, parteWhere, (String[]) join.toArray(new String[]{}));
            sql = sql + " ORDER BY T_TER.TER_NOC";
            ps = con.prepareStatement(sql);

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("NOMBRE COMPLETO: " +  nombreCompleto);
                m_Log.debug("TIPO DOCUMENTO: " +  tipoDocumento);
                m_Log.debug("DOCUMENTO: " +  documento);
            }

           
             if(params[0]!=null && (params[0].equalsIgnoreCase("oracle"))){
                String alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                String alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";                
                m_Log.debug("ALTER SESSION SET LINGUISTIC/GENERICBASELETTER");

                Statement st =con.createStatement();
                st.executeQuery(alter1);
                st=con.prepareStatement(alter2);
                st.executeQuery(alter2);
                st.close();
            }
            
            rs = ps.executeQuery();
            while (rs.next()) {
                GeneralValueObject tupla = new GeneralValueObject();
                tupla.setAtributo("T_TID.TID_DES", rs.getString(1));
                tupla.setAtributo("T_TER.TER_DOC", rs.getString(2));
                tupla.setAtributo("T_TER.TER_COD", Integer.toString(rs.getInt(3)));
                tupla.setAtributo("T_TER.TER_NOM", rs.getString(4));
                tupla.setAtributo("T_TER.TER_AP1", rs.getString(5));
                tupla.setAtributo("T_TER.TER_AP2", rs.getString(6));
                resultado.add(tupla);
            }
            cerrarResultSet(rs);
            cerrarStatement(ps);
            return resultado;

        } catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            return new Vector();
        } finally {
            
            try{
               
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                        Statement st_alter;
                        String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                        String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                        m_Log.debug("ALTER SESSION SET BINARY/SPANISH");

                        st_alter = con.createStatement();
                        st_alter.executeQuery(alter1);
                        st_alter.executeQuery(alter2);

                        st_alter.close();
                    }
                
            }catch (SQLException se){
                if(m_Log.isErrorEnabled()) m_Log.error(se.getMessage());
            }
            cerrarResultSet(rs);
            cerrarStatement(ps);
            devolverConexion(bd, con);
        }

    }

    public Vector getTercerosRepByNomAndDoc(String[] params,GeneralValueObject gVO)
            throws TechnicalException {
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        PreparedStatement ps=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            String nombre = (String) gVO.getAtributo("nombre");
            String apellido1 = (String) gVO.getAtributo("apellido1");
            String apellido2 = (String) gVO.getAtributo("apellido2");
            String tipoDocumento = (String) gVO.getAtributo("codTipoDocumento");
            String documento = (String) gVO.getAtributo("documento");

            
            TransformacionAtributoSelect transformador = new TransformacionAtributoSelect(bd, con);
            String condicionNombre = transformador.construirCondicionWhereConOperadores(
                    bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"TER_NOM"}), nombre.trim(),false);

            String condicionApellido1 = transformador.construirCondicionWhereConOperadores(
                    bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"TER_AP1"}), apellido1.trim(),false);
            String condicionApellido2 = transformador.construirCondicionWhereConOperadores(
                    bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"TER_AP2"}), apellido2.trim(),false);
            String condicionTipoDoc = transformador.construirCondicionWhereConOperadores("TID_COD", tipoDocumento.trim(),false);
            String condicionDoc = transformador.construirCondicionWhereConOperadores("TER_DOC", documento.trim(),false);
            String condicionDocNull = "TER_DOC IS NULL";
            String condicionNomNull = "TER_NOM IS NULL";
            String condicionAp1Null = "TER_AP1 IS NULL";
            String condicionAp2Null = "TER_AP2 IS NULL";

            String parteFromInterna = bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"TER_NOM"}) + " AS TER_NOM," +
                                      bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"TER_AP1"}) + " AS TER_AP1," +
                                      bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"TER_AP2"}) + " AS TER_AP2" +
                    ", COUNT(*) AS NUM";

            String parteWhereInterna = "";

            if (nombre.equals("*")) {
                condicionNombre = condicionNombre.concat(" OR " + condicionNomNull);
            }
            if (apellido1.equals("*")) {
                condicionApellido1 = condicionApellido1.concat(" OR " + condicionAp1Null);
            }
            if (apellido2.equals("*")) {
                condicionApellido2 = condicionApellido2.concat(" OR " + condicionAp2Null);
            }
            if (documento.equals("*")) {
                parteWhereInterna = "TER_SIT='A' AND (" + condicionNombre + ") AND (" + condicionApellido1 + ") AND (" + condicionApellido2
                        + ") AND " + condicionTipoDoc + " AND (" + condicionDoc + " OR " + condicionDocNull + ")";
            } else {
                parteWhereInterna = "TER_SIT='A' AND " + condicionNombre + " AND " + condicionApellido1 + " AND " + condicionApellido2 + " AND "
                        + condicionTipoDoc + " AND " + condicionDoc;
            }
            ArrayList join = new ArrayList();
            join.add("T_TER");
            join.add("INNER");
            join.add("T_TID");
            join.add("TER_TID=TID_COD");
            join.add("false");
            String sqlInterna = bd.join(parteFromInterna, parteWhereInterna, (String[]) join.toArray(new String[]{})) +
                    " GROUP BY " + bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"TER_NOM"}) + "," +
                    bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"TER_AP1"}) + "," +
                    bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"TER_AP2"});

            String parteFrom = "TER_COD, TID_DES, TER_DOC, T_TER.TER_NOM, T_TER.TER_AP1, T_TER.TER_AP2";
            String parteWhere = "";
            if (documento.equals("*")) {
                //parteWhere = "NUM>1 AND TER_SIT='A' AND " + condicionTipoDoc + " AND (" + condicionDoc + " OR " + condicionDocNull + ")";
                parteWhere = "TER_SIT='A' AND " + condicionTipoDoc + " AND (" + condicionDoc + " OR " + condicionDocNull + ")";
            } else {
                //parteWhere = "NUM>1 AND TER_SIT='A' AND " + condicionTipoDoc + " AND " + condicionDoc;
                parteWhere = "TER_SIT='A' AND " + condicionTipoDoc + " AND " + condicionDoc;
            }
            String condicionBusqueda = "";
            if (nombre.equals("*")) {
                condicionBusqueda = condicionBusqueda.concat("(" + bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM,
                        new String[]{"T_TER.TER_NOM"}) + " = a.TER_NOM OR (T_TER.TER_NOM IS NULL AND a.TER_NOM IS NULL))");
            } else {
                condicionBusqueda = condicionBusqueda.concat(bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"T_TER.TER_NOM"}) + " = a.TER_NOM");
            }
            if (apellido1.equals("*")) {
                condicionBusqueda = condicionBusqueda.concat(" AND (" + bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"T_TER.TER_AP1"}) +
                    " = a.TER_AP1 OR (T_TER.TER_AP1 IS NULL AND a.TER_AP1 IS NULL))");
            } else {
                condicionBusqueda = condicionBusqueda.concat(" AND " + bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"T_TER.TER_AP1"}) + " = a.TER_AP1");
            }
            if (apellido2.equals("*")) {
                condicionBusqueda = condicionBusqueda.concat(" AND (" + bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"T_TER.TER_AP2"}) +
                    " = a.TER_AP2 OR (T_TER.TER_AP2 IS NULL AND a.TER_AP2 IS NULL))");
            } else {
                condicionBusqueda = condicionBusqueda.concat(" AND " + bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"T_TER.TER_AP2"}) + " = a.TER_AP2");
            }
            join = new ArrayList();
            join.add("T_TER");
            join.add("INNER");
            join.add("T_TID");
            join.add("TER_TID=TID_COD");
            join.add("INNER");
            join.add("(" + sqlInterna + ") a");
            join.add(condicionBusqueda);
            join.add("false");

            String sql = bd.join(parteFrom, parteWhere, (String[]) join.toArray(new String[]{})) +
                    " ORDER BY T_TER.TER_NOM, T_TER.TER_AP1, T_TER.TER_AP2";

            ps = con.prepareStatement(sql);

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("NOMBRE: " +  nombre);
                m_Log.debug("APELLIDO1: " +  apellido1);
                m_Log.debug("APELLIDO2: " +  apellido2);
                m_Log.debug("TIPO DOCUMENTO: " +  tipoDocumento);
                m_Log.debug("DOCUMENTO: " +  documento);
            }

            if(params[0]!=null && (params[0].equalsIgnoreCase("oracle"))){
                String alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                String alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";                
                m_Log.debug("ALTER SESSION SET LINGUISTIC/GENERICBASELETTER");

                Statement st =con.createStatement();
                st.executeQuery(alter1);
                st=con.prepareStatement(alter2);
                st.executeQuery(alter2);
                st.close();
            }
            
            rs = ps.executeQuery();
            while (rs.next()) {
                GeneralValueObject tupla = new GeneralValueObject();
                tupla.setAtributo("T_TER.TER_COD", Integer.toString(rs.getInt(1)));
                tupla.setAtributo("T_TID.TID_DES", rs.getString(2));
                tupla.setAtributo("T_TER.TER_DOC", rs.getString(3));
                tupla.setAtributo("T_TER.TER_NOM", rs.getString(4));
                tupla.setAtributo("T_TER.TER_AP1", rs.getString(5));
                tupla.setAtributo("T_TER.TER_AP2", rs.getString(6));
                resultado.add(tupla);
            }
            cerrarResultSet(rs);
            cerrarStatement(ps);
        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            
             try{
               
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                        Statement st_alter;
                        String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                        String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                        m_Log.debug("ALTER SESSION SET BINARY/SPANISH");

                        st_alter = con.createStatement();
                        st_alter.executeQuery(alter1);
                        st_alter.executeQuery(alter2);

                        st_alter.close();
                    }
                
            }catch (SQLException se){
                if(m_Log.isErrorEnabled()) m_Log.error(se.getMessage());
            }
            cerrarResultSet(rs);
            cerrarStatement(ps);
            devolverConexion(bd, con);
        }
        return resultado;
    }

    /**
     * Devuelve el número de terceros que comparten un domicilio.
     * @param domVO : domicilio
     * @param params
     * @return int : numero de terceros que comparten domVO
     * @throws TechnicalException
     */
    public int getNumeroTercerosByDomicilio(DomicilioSimpleValueObject domVO, String[] params)
    		throws TechnicalException{

    	int resultado = 0;
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs=null;
        PreparedStatement ps=null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            // Sentencia SQL
            String sql = "SELECT COUNT(*) FROM T_DOT WHERE " + campos.getAtributo("idDomicilioDOT") +
                         "=" + domVO.getIdDomicilio();
            ps = con.prepareStatement(sql);
            if (m_Log.isDebugEnabled()) m_Log.debug("SQL: " + sql);
            // Ejecucion
            rs = ps.executeQuery();
            rs.next();
            resultado = rs.getInt(1);
            cerrarResultSet(rs);
            cerrarStatement(ps);
        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(ps);
            devolverConexion(bd, con);
        }
    	return resultado;
    }

    public Vector getIdsTercerosSimilares(String[] params,GeneralValueObject gVO)
            throws TechnicalException{
        Vector resultado = new Vector();
        AdaptadorSQLBD bd = null;
        Connection con=null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.consultaSinAcentos(con, params);
            String codTercero = (String) gVO.getAtributo("codTercero");

            String sqlInterna = "SELECT TER_NOM, TER_AP1, TER_AP2, TER_NOC FROM T_TER WHERE TER_COD=?";
            String condJoin = "(("+
                bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"T_TER.TER_NOC"}) +
                " = " +
                bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"a.TER_NOC"}) +
                " AND a.TER_NOC IS NOT NULL) OR " +
                "(a.TER_NOC IS NULL AND " +
                bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"T_TER.TER_NOM"}) +
                " = " +
                bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"a.TER_NOM"}) +
                "AND (" + bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"T_TER.TER_AP1"}) +
                " = " +
                bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"a.TER_AP1"}) +
                " OR (T_TER.TER_AP1 IS NULL AND a.TER_AP1 IS NULL)) " +
                "AND (" + bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"T_TER.TER_AP2"}) +
                " = " +
                bd.funcionCadena(AdaptadorSQL.FUNCIONCADENA_TRIM, new String[]{"a.TER_AP2"}) +
                " OR (T_TER.TER_AP2 IS NULL AND a.TER_AP2 IS NULL))))";

            String parteFrom = "TER_COD";
            String parteWhere = "TER_SIT='A'";
            ArrayList join = new ArrayList();
            join.add("T_TER");
            join.add("INNER");
            join.add("(" + sqlInterna + ") a");
            join.add(condJoin);
            join.add("false");

            String sql = bd.join(parteFrom, parteWhere, (String[]) join.toArray(new String[]{}));

            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(codTercero));

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SQL: " + sql);
                m_Log.debug("parametros:");
                m_Log.debug("CODIGO TERCERO: " +  codTercero);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add(new Integer(rs.getInt(1)));
            }
            cerrarResultSet(rs);
            cerrarStatement(ps);
        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(ps);
            devolverConexion(bd, con);
        }
        return resultado;
    }

    public void copiarDomiciliosATercero(Vector codDoms, String codTercero, String codUsuario, String[] params)
    throws TechnicalException, SQLException, BDException {

        // Generamos la conexion a la Base de Datos.
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        boolean yaExisten = true;

        try {
            con = abd.getConnection();
            abd.inicioTransaccion(con);
            // Creamos las consultas en SQL.
            String sqlQuery = "SELECT " + campos.getAtributo("situacionDOT") +
                    " FROM T_DOT " +
                    "WHERE " + campos.getAtributo("idTerceroDOT") + " = ? " +
                    "AND " + campos.getAtributo("idDomicilioDOT")  + " = ?";
            String sqlInsert = "INSERT INTO T_DOT VALUES (?, ?, NULL, 'A', " + abd.convertir(abd.convertir(abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null),
                    AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") + ", ?, 0)";
            String sqlUpdate = "UPDATE T_DOT SET DOT_SIT='A',DOT_FEC=" + abd.convertir(abd.convertir(abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null),
                    AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO,"DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA,"DD/MM/YYYY") +
                    ",DOT_USU=? WHERE DOT_DOM=? AND DOT_TER=?";

            Iterator itCodDoms = codDoms.iterator();
            while (itCodDoms.hasNext()) {
                String codDom = (String)itCodDoms.next();
                m_Log.debug("cod domicilio a copiar "+codDom);
                ps = con.prepareStatement(sqlQuery);
                int i = 1;
                ps.setInt(i++, Integer.parseInt(codTercero));
                ps.setInt(i, Integer.parseInt(codDom));
                m_Log.debug(sqlQuery);
                m_Log.debug("PARAMETROS:");
                m_Log.debug("codTercero "+codTercero);
                m_Log.debug("codDom "+codDom);

                rs = ps.executeQuery();

                if (!rs.next()) {
                    yaExisten = false;
                    cerrarResultSet(rs);
                    cerrarStatement(ps);
                    ps = con.prepareStatement(sqlInsert);
                    i = 1;
                    ps.setInt(i++, Integer.parseInt(codDom));
                    ps.setInt(i++, Integer.parseInt(codTercero));
                    ps.setInt(i, Integer.parseInt(codUsuario));
                    m_Log.debug(sqlInsert);
                    m_Log.debug("PARAMETROS:");
                    m_Log.debug("codDom "+codDom);
                    m_Log.debug("codTercero "+codTercero);
                    m_Log.debug("codUsuario "+codUsuario);
                    ps.executeUpdate();
                    cerrarStatement(ps);

                } else {
                    if (rs.getString(1).equals("B")) {
                        yaExisten = false;
                        cerrarResultSet(rs);
                        cerrarStatement(ps);
                        ps = con.prepareStatement(sqlUpdate);
                        i = 1;
                        ps.setInt(i++, Integer.parseInt(codUsuario));
                        ps.setInt(i++, Integer.parseInt(codDom));
                        ps.setInt(i, Integer.parseInt(codTercero));
                        m_Log.debug(sqlUpdate);
                        m_Log.debug("PARAMETROS");
                        m_Log.debug("codUsuario "+codUsuario);
                        m_Log.debug("codDom "+codDom);
                        m_Log.debug("codTercero "+codTercero);
                        ps.executeUpdate();
                        cerrarStatement(ps);
                    }
                }
            }

            if (yaExisten) {
                m_Log.debug("YA EXISTEN TODOS LOS DOMICILIOS");
                throw new TechnicalException("yaExisten");
            }

            commitTransaction(abd, con);

        } catch (BDException bde) {
            bde.printStackTrace();
            rollBackTransaction(abd, con, bde);
            throw new TechnicalException(bde.getMensaje());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            rollBackTransaction(abd, con, sqle);
            throw new TechnicalException(sqle.getMessage());
        } catch (Exception e) {
            if (e.getMessage().equals("yaExisten")) {
                throw new TechnicalException(e.getMessage());
            } else {
                rollBackTransaction(abd, con, e);
                e.printStackTrace();
                throw new TechnicalException(e.getMessage());
            }
        } finally {
            cerrarResultSet(rs);
            cerrarStatement(ps);
            devolverConexion(abd, con);
        }
    }

    private void rollBackTransaction(AdaptadorSQLBD bd,Connection con,Exception e){
        try {
            bd.rollBack(con);
            bd.devolverConexion(con);
        }
        catch (Exception e1) {
            try{
                bd.devolverConexion(con);
            } catch(Exception ex){
                ex.printStackTrace();
                m_Log.error(ex.getMessage());
            }
            e1.printStackTrace();
            m_Log.error(e1.getMessage());
        }
        finally {
            e.printStackTrace();
            m_Log.error(e.getMessage());
        }
    }

    private void commitTransaction(AdaptadorSQLBD bd,Connection con){
        try{
            bd.finTransaccion(con);
            bd.devolverConexion(con);
        }
        catch (Exception ex) {
            try{
                bd.devolverConexion(con);
            } catch(Exception e){
                e.printStackTrace();
                m_Log.error(e.getMessage());
            }
            ex.printStackTrace();
            m_Log.error(ex.getMessage());
        }
    }


    /**
     * Recupera el email de un tercero de la tabla histórico de terceros
     * @param idTercero: Identificador del tercero
     * @param params: Parámetros de conexión a la base de datos
     * @return Un String
     */
    public String getEmailTercero(String idTercero,String[] params) throws TechnicalException
    {
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        Statement st = null;
        ResultSet rs = null;
        String email = null;
        try
        {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();

             // Se recupera del histórico los datos del tercero más reciente
            String sql = "SELECT HTE_DCE FROM T_HTE WHERE HTE_TER=" + idTercero + " AND HTE_FOP="
                       + "(SELECT MAX(HTE_FOP) FROM T_HTE WHERE HTE_TER=" + idTercero + ")";

            m_Log.debug("SQL: " + sql);
            st = conexion.createStatement();
            m_Log.debug("************ TercerosDAO.getEmailTercero sql nativo: " + conexion.nativeSQL(sql) );
            rs = st.executeQuery(sql);
            m_Log.debug("****************** TercerosDAO.getEmailTercero después de ejecutar consulta");
            while(rs.next()){
                email = rs.getString("HTE_DCE");
                m_Log.debug("TercerosDAO.getEmailTercero email del tercero: " + idTercero + " es:" + email);
            }
         cerrarResultSet(rs);
         cerrarStatement(st);
         devolverConexion(abd, conexion);
        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(st);
            devolverConexion(abd, conexion);
        }
        return email;
    }



    /**
     * Graba el domicilio correspondiente a una localización y graba la localización del mismo en el expediente. Adicionalmente graba los expedientes
     * indicados en el parámetro expedientes como relacionados al expediente cuya información se encuentra en el parámetro gVO
     * @param gVO: Objeto con los datos del expediente
     * @param expediente: Array de String que contiene el número de los expedientes que se van a relacionar con el expediente a tratar
     * @param params: Parámetros de conexión a la base de datos
     * @return int
     * @throws es.altia.common.exception.TechnicalException
     */
      public int grabarDomiciliosNoNormalizadosLocalizacionExpedientesRelacionados(GeneralValueObject gVO,String[] expedientes,String[] params)throws TechnicalException{
        String sql = "";
        int resultado3=-1;
        int resultado = 0;
        int resultado1 = 0;
        AdaptadorSQLBD bd=null;
        Connection con=null;
        ResultSet rs=null;
        Statement state=null;
        int codMaximoDomicilio = 0;
        PreparedStatement ps = null;
        String BARRA_INVERTIDA = "/";

        try{
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            state = con.createStatement();

            campos.setAtributo("idDomicilioDOM",m_ct.getString("SQL.T_DOM.idDomicilio"));
            sql = "SELECT " + bd.funcionMatematica(AdaptadorSQLBD.FUNCIONMATEMATICA_MAX,new String[]{(String)campos.getAtributo("idDomicilioDOM")}) + " AS maximo FROM T_DOM";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            if (rs.next()) {
                codMaximoDomicilio = rs.getInt("maximo");
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            if (codMaximoDomicilio > 0)
            {
                String codVia = (String) gVO.getAtributo("codVia");
                if (codVia.equals("0")) {
                    sql = "SELECT "+ campos.getAtributo("idVIA")+" AS ID FROM T_VIA WHERE "+
                            campos.getAtributo("codPaisVIA")+"="+gVO.getAtributo("codPais")+" AND "+
                            campos.getAtributo("codProvVIA")+"="+gVO.getAtributo("codProvincia")+" AND "+
                            campos.getAtributo("codMunVIA")+"="+gVO.getAtributo("codMunicipio")+" AND "+
                            campos.getAtributo("descVIA")+"="+bd.addString((String)gVO.getAtributo("descVia"))+" AND "+
                            campos.getAtributo("tipoVIA")+"="+gVO.getAtributo("codTipoVia")+" AND "+
                            campos.getAtributo("situacionVIA")+"= 'A'";
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    state = con.createStatement();
                    rs = state.executeQuery(sql);
                    boolean existeVia = rs.next();
                    cerrarResultSet(rs);
                    cerrarStatement(state);
                    if(!existeVia) {
                        int idNuevaVia = 0;
                        sql = "SELECT " + bd.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL,new String[]{"MAX("+campos.getAtributo("idVIA")+")","0"})+ "+1 AS MAXIMO" +
                            " FROM T_VIA WHERE " + campos.getAtributo("codProvVIA")+"="+gVO.getAtributo("codProvincia")
                            + " AND " + campos.getAtributo("codMunVIA")+"="+gVO.getAtributo("codMunicipio");

                        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                        state = con.createStatement();
                        rs = state.executeQuery(sql);
                        if(rs.next()){
                            idNuevaVia = rs.getInt("MAXIMO");
                            codVia = Integer.toString(idNuevaVia);
                        }
                        cerrarResultSet(rs);
                        cerrarStatement(state);
                        sql = "INSERT INTO T_VIA("+
                            campos.getAtributo("codPaisVIA")+","+
                            campos.getAtributo("codProvVIA")+","+
                            campos.getAtributo("codMunVIA")+","+
                            campos.getAtributo("idVIA")+","+
                            campos.getAtributo("codVIA")+","+
                            campos.getAtributo("descVIA")+","+
                            campos.getAtributo("nomCorto")+","+
                            campos.getAtributo("tipoVIA")+","+
                            campos.getAtributo("situacionVIA")+","+
                            campos.getAtributo("fechaAlta")+","+
                            campos.getAtributo("usuarioAlta")+","+
                            campos.getAtributo("fechaBaja")+","+
                            campos.getAtributo("usuarioBaja")+","+
                            campos.getAtributo("fechaVigencia")+
                            ") VALUES (" +
                            gVO.getAtributo("codPais")+","+
                            gVO.getAtributo("codProvincia")+","+
                            gVO.getAtributo("codMunicipio")+","+
                            idNuevaVia+","+
                            idNuevaVia+","+
                            bd.addString((String)gVO.getAtributo("descVia"))+","+
                            bd.addString((String)gVO.getAtributo("descVia"))+","+
                            gVO.getAtributo("codTipoVia")+","+
                            "'A',"+bd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+","+
                            gVO.getAtributo("codUsuario")+","+
                            "null,null,"+bd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null)+")";

                        if (m_Log.isDebugEnabled()) m_Log.debug(sql);
                        state = con.createStatement();
                        state.executeUpdate(sql);
                        cerrarStatement(state);
                    } else {
                        codVia = rs.getString("ID");
                    }
                }
                codMaximoDomicilio++;
                sql = "INSERT INTO T_DOM("+
                        campos.getAtributo("idDomicilioDOM")+","+
                        campos.getAtributo("normalizadoDOM")+
                        ") VALUES(" + codMaximoDomicilio + ",2)";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state = con.createStatement();
                resultado = state.executeUpdate(sql);
                cerrarStatement(state);
                if(resultado>0)
                {
                    sql = "INSERT INTO T_DNN("+
                            campos.getAtributo("idDomicilioDNN")+","+campos.getAtributo("idTipoViaDNN")+","+
                            campos.getAtributo("codPaisDNN")+","+campos.getAtributo("codProvDNN")+","+
                            campos.getAtributo("codMunDNN")+","+campos.getAtributo("codPaisViaDNN")+","+
                            campos.getAtributo("codProvViaDNN")+","+campos.getAtributo("codMunViaDNN")+","+
                            campos.getAtributo("codViaDNN")+","+campos.getAtributo("numDesdeDNN")+","+
                            campos.getAtributo("letraDesdeDNN")+","+campos.getAtributo("numHastaDNN")+","+
                            campos.getAtributo("letraHastaDNN")+","+campos.getAtributo("bloqueDNN")+","+
                            campos.getAtributo("portalDNN")+","+campos.getAtributo("escaleraDNN")+","+
                            campos.getAtributo("plantaDNN")+","+campos.getAtributo("puertaDNN")+","+
                            campos.getAtributo("domicilioDNN")+","+campos.getAtributo("codPostalDNN")+","+
                            campos.getAtributo("situacionDNN")+","+
                            campos.getAtributo("fechaAltaDNN")+","+campos.getAtributo("usuarioAltaDNN")+","+
                            campos.getAtributo("codPaisESIDNN")+ "," + campos.getAtributo("codProvESIDNN")+ ","+
                            campos.getAtributo("codMunESIDNN")+","+ campos.getAtributo("codESIDNN")+ " ," +
                            campos.getAtributo("refCatastralDNN")+
                            ") VALUES (" + codMaximoDomicilio + "," + bd.addString((String)gVO.getAtributo("codTipoVia")) + "," +
                            gVO.getAtributo("codPais") + "," + gVO.getAtributo("codProvincia") + "," +
                            gVO.getAtributo("codMunicipio") + "," + bd.addString((String)gVO.getAtributo("codPais")) + "," +
                            bd.addString((String)gVO.getAtributo("codProvincia")) + "," + bd.addString((String)gVO.getAtributo("codMunicipio")) + "," +
                            bd.addString(codVia) + "," + bd.addString((String)gVO.getAtributo("numDesde")) + "," +
                            bd.addString((String)gVO.getAtributo("letraDesde")) + "," + bd.addString((String)gVO.getAtributo("numHasta")) + "," +
                            bd.addString((String)gVO.getAtributo("letraHasta")) + "," + bd.addString((String)gVO.getAtributo("bloque")) + "," +
                            bd.addString((String)gVO.getAtributo("portal")) + "," + bd.addString((String)gVO.getAtributo("escalera")) + "," +
                            bd.addString((String)gVO.getAtributo("planta")) + "," + bd.addString((String)gVO.getAtributo("puerta")) + "," +
                            bd.addString((String)gVO.getAtributo("domicilio")) + "," + bd.addString((String)gVO.getAtributo("codPostal")) + ", 'A', " +
                            bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null)+ "," +gVO.getAtributo("codUsuario") + ",";

                    if (gVO.getAtributo("codESI")!=null){
                        if (!"".equals( gVO.getAtributo("codESI") ) ){
                            sql += gVO.getAtributo("codPais") + "," +gVO.getAtributo("codProvincia") + "," +
                                    gVO.getAtributo("codMunicipio") + ","  + gVO.getAtributo("codESI");
                        } else {
                            sql += "null,null,null,null ";
                        }
                    } else {
                        sql += "null,null,null,null ";
                    }
                    sql += ", " + bd.addString((String)gVO.getAtributo("refCatastral")) + ")";

                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    state = con.createStatement();
                    resultado1 = state.executeUpdate(sql);
                    cerrarStatement(state);

                    if(resultado1==1){
                        // Se graba la localización asociada al expediente
                        //String codMunicipio = (String) gVO.getAtributo("codMunicipio");
                        String ejercicio = (String) gVO.getAtributo("ejercicio");
                        String numeroExpediente = (String) gVO.getAtributo("numeroExpediente");
                        String localizacion = (String) gVO.getAtributo("localizacion");
                        //String referencia = (String) gVO.getAtributo("referencia");
                        String codMunExp=(String) gVO.getAtributo("codMunExp");

                        state = con.createStatement();

                         String sqlExp = "UPDATE E_EXP SET EXP_LOC='" + localizacion + "',EXP_CLO='" + codMaximoDomicilio + "' WHERE EXP_MUN=" + codMunExp + " AND EXP_EJE=" + ejercicio + " AND EXP_NUM='" + numeroExpediente + "'";
                         if(m_Log.isDebugEnabled()) m_Log.debug(sqlExp);
                         resultado3 = state.executeUpdate(sqlExp);
                         cerrarStatement(state);
                    }
                }

                /******  Se comprueba si entre los expedientes relacionados con la misma localización al actual, si no están entre los seleccionados, hay que eliminarlos. ******/
                ArrayList<ConsultaExpedientesValueObject> expRelacionadosAnteriormente = ConsultaExpedientesManager.getInstance().getExpRelacionados((String) gVO.getAtributo("numeroExpediente"),Integer.parseInt((String) gVO.getAtributo("codMunExp")), (String) gVO.getAtributo("localizacion"),con,params);

                String cancelar = (String)gVO.getAtributo("cancelarExpRelacionadosLocalizacion");
                m_Log.debug(" ==================> cancelarExpRelacionados: " + cancelar);
                if(cancelar!=null && !"si".equals(cancelar))
                {
                    for(int h=0;h<expRelacionadosAnteriormente.size();h++){
                       if(!contieneExpediente(expRelacionadosAnteriormente.get(h).getNumeroExpediente(),expedientes)){
                           // Un expediente relacionado con el actual y con la misma localización deja de estarlo ahora => Se elimina
                           // el expediente relacionado.
                           this.eliminarExpedienteRelacionado((String) gVO.getAtributo("numeroExpediente"), expRelacionadosAnteriormente.get(h).getNumeroExpediente(), con);
                       }// if
                    }// for



                    // Se insertan los expedientes seleccionados
                    String numExpedienteOrigen = (String) gVO.getAtributo("numeroExpediente");
                    // Código del municipio de origen que será el mismo que el del expediente relacionado
                    String codMunicipioOrigen = (String) gVO.getAtributo("codMunExp");

                    String[] datosExpOrigen = numExpedienteOrigen.split(BARRA_INVERTIDA);
                    String ejercicioOrigen = datosExpOrigen[0];

                    for(int i=0;expedientes!=null && i<expedientes.length;i++){

                        if(!expedientes[i].equals(""))
                        {
                            String[] auxExp = expedientes[i].split(BARRA_INVERTIDA);
                            if(auxExp!=null && auxExp.length==3)
                            {
                                String numExpedienteRelacionado = expedientes[i];
                                String[] datosExpRelacionado = numExpedienteRelacionado.split(BARRA_INVERTIDA);
                                String ejercicioExpRelacionado = datosExpRelacionado[0];

                                if(!existeExpedienteRelacionado(numExpedienteOrigen, numExpedienteRelacionado, con)){

                                    String sqlExpRelacionado = "INSERT INTO E_REX(REX_MUN,REX_EJE,REX_NUM,REX_MUNR,REX_EJER,REX_NUMR) VALUES(?,?,?,?,?,?)";
                                    ps = con.prepareStatement(sqlExpRelacionado);

                                    int j=1;
                                    ps.setInt(j++,Integer.parseInt(codMunicipioOrigen));
                                    ps.setInt(j++,Integer.parseInt(ejercicioOrigen));
                                    ps.setString(j++,numExpedienteOrigen);
                                    ps.setInt(j++,Integer.parseInt(codMunicipioOrigen));
                                    ps.setInt(j++,Integer.parseInt(ejercicioExpRelacionado));
                                    ps.setString(j++,numExpedienteRelacionado);
                                    int rowsInserted = ps.executeUpdate();
                                    m_Log.debug("Se ha insertado " + rowsInserted + " expediente/s relacionado/s al expediente " + numExpedienteOrigen);
                                    cerrarStatement(ps);

                                    sqlExpRelacionado = "INSERT INTO E_REX(REX_MUN,REX_EJE,REX_NUM,REX_MUNR,REX_EJER,REX_NUMR) VALUES(?,?,?,?,?,?)";
                                    ps = con.prepareStatement(sqlExpRelacionado);
                                    j=1;
                                    ps.setInt(j++,Integer.parseInt(codMunicipioOrigen));
                                    ps.setInt(j++,Integer.parseInt(ejercicioExpRelacionado));
                                    ps.setString(j++,numExpedienteRelacionado);
                                    ps.setInt(j++,Integer.parseInt(codMunicipioOrigen));
                                    ps.setInt(j++,Integer.parseInt(ejercicioOrigen));
                                    ps.setString(j++,numExpedienteOrigen);

                                    rowsInserted = ps.executeUpdate();
                                    m_Log.debug("Se ha insertado " + rowsInserted + " expediente/s relacionado/s al expediente " + numExpedienteRelacionado);
                                    cerrarStatement(ps);
                                }
                            }
                        }
                    } // for
                }// if cancelar

            }
            commitTransaction(bd, con);
        }catch (Exception e) {
            rollBackTransaction(bd, con, e);
            resultado1=0;
        }finally{
            cerrarStatement(ps);
            cerrarStatement(state);
            cerrarResultSet(rs);
            devolverConexion(bd, con);
        }

        if(resultado3==1)
            return codMaximoDomicilio;
        else return -1;
    }


      /**
       * Comprueba si en un array de String existe un determinado número de expediente
       * @param numExpediente: Número de expediente
       * @param expedientes: Array de String con número de expedientes
       * @return boolean
       */
      private boolean contieneExpediente(String numExpediente,String[] expedientes){
          boolean exito = false;
          for(int i=0;i<expedientes.length;i++){
              if(expedientes[i]==numExpediente){
                  exito = true;
                  break;
              }
          }
          return exito;
      }


         /**
       * Elimina las dos relaciones que hay entre expedientes relacionados
       * @param numExpedienteOriginal: Número del expediente original
       * @param numExpedienteRelacionado: Número del expediente reliacionado
       * @param con: Conexión a la bd
       * @return boolean
       */
    private boolean eliminarExpedienteRelacionado(String numExpedienteOriginal,String numExpedienteRelacionado,Connection con) throws TechnicalException {
        boolean exito = false;
        PreparedStatement ps = null;

        try{
            String sql = "DELETE FROM E_REX WHERE REX_NUMR=? AND REX_NUM=?";
            ps = con.prepareStatement(sql);
            int i=1;
            ps.setString(i++,numExpedienteRelacionado);
            ps.setString(i++,numExpedienteOriginal);
            int rowsUpdated = ps.executeUpdate();
            m_Log.debug("Relación la relación del expediente " + numExpedienteOriginal + "  con el expediente " + numExpedienteRelacionado);
            cerrarStatement(ps);

            i=1;
            ps = con.prepareStatement(sql);
            ps.setString(i++,numExpedienteOriginal);
            ps.setString(i++,numExpedienteRelacionado);
            rowsUpdated = ps.executeUpdate();
            m_Log.debug("Relación la relación del expediente " + numExpedienteRelacionado + "  con el expediente " + numExpedienteOriginal);
            cerrarStatement(ps);
            if(rowsUpdated==1)
                exito = true;

        }catch(Exception e){
            e.printStackTrace();
            m_Log.error("eliminarExpedienteRelacionado: "  + e.getMessage());
        }finally{
            cerrarStatement(ps);
        }
        return exito;
    }




      /**
       * Elimina las dos relaciones que hay entre expedientes relacionados
       * @param numExpedienteOriginal: Número del expediente original
       * @param numExpedienteRelacionado: Número del expediente reliacionado
       * @param con: Conexión a la bd
       * @return boolean
       */
    private boolean existeExpedienteRelacionado(String numExpedienteOriginal,String numExpedienteRelacionado,Connection con) throws TechnicalException {
        boolean exito = false;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            String sql = "SELECT COUNT(*) AS NUM FROM E_REX WHERE REX_NUMR=? AND REX_NUM=?";
            ps = con.prepareStatement(sql);

            int i=1;
            ps.setString(i++,numExpedienteRelacionado);
            ps.setString(i++,numExpedienteOriginal);
            rs = ps.executeQuery();
            rs.next();
            int num = rs.getInt("NUM");
            if(num>=1)
                exito = true;

        }catch(Exception e){
            e.printStackTrace();
            m_Log.error("eliminarExpedienteRelacionado: "  + e.getMessage());
        }finally{
            cerrarStatement(ps);
            cerrarResultSet(rs);
        }

        return exito;
    }

      private void devolverConexion(AdaptadorSQLBD abd, Connection con) throws TechnicalException {
        try {
            if (con != null) abd.devolverConexion(con);
        } catch (BDException bde) {
            m_Log.error("ERROR AL DEVOLVER LA CONEXION A LA BASE DE DATOS", bde);
            throw new TechnicalException(bde.getMensaje(), bde);
        }
    }

    private void cerrarStatement(Statement statement) throws TechnicalException {
        try {
            if (statement != null) statement.close();
        } catch(SQLException sqle){
            m_Log.error("ERROR AL CERRAR EL STATEMENT DE LA BASE DE DATOS", sqle);
            throw new TechnicalException(sqle.getMessage(), sqle);
        }
    }

    private void cerrarResultSet(ResultSet resultSet) throws TechnicalException {
        try {
            if (resultSet != null) resultSet.close();
        } catch(SQLException sqle){
            m_Log.error("ERROR AL CERRAR EL STATEMENT DE LA BASE DE DATOS", sqle);
            throw new TechnicalException(sqle.getMessage(), sqle);
        }
    }


    /**
     * Recupera un tercero y sus domicilios en base al identificador del mismo
     * @param terVO: TercerosValueObject
     * @param con: Conexión a la BBDD
     * @param params: Parámetros de conexión a la base de datos
     * @return ArrayList<TercerosValueObject>
     * @throws es.altia.common.exception.TechnicalException
     */
     public ArrayList<TercerosValueObject> getByIdTercero(TercerosValueObject terVO, Connection con,String[] params)
            throws TechnicalException{
        String sql= "";        
        AdaptadorSQLBD bd = null;        
        ResultSet rs=null;
        Statement state=null;

        ArrayList<TercerosValueObject> resultado = new ArrayList<TercerosValueObject>();
        try{
            
            bd = new AdaptadorSQLBD(params);            
            state = con.createStatement();
            String from = "T_TER.*,T_DOM.*,"+
            campos.getAtributo("descTID")+","+
            campos.getAtributo("codTVI")+" AS CODTIPOVIA,"+
            campos.getAtributo("descTVI")+" AS DESCTIPOVIA," +
            campos.getAtributo("idVIA")+" AS IDVIA,"+
            campos.getAtributo("codVIA")+" AS CODVIA,"+
            campos.getAtributo("descVIA")+" AS DESCVIA,"+
            campos.getAtributo("domicilioDNN")+" AS DESCDMC," +
            campos.getAtributo("numDesdeDNN")+" AS NUD,"+
            campos.getAtributo("letraDesdeDNN")+" AS LED,"+
            campos.getAtributo("numHastaDNN")+" AS NUH,"+
            campos.getAtributo("letraHastaDNN")+" AS LEH," +
            campos.getAtributo("bloqueDNN")+" AS BLQ,"+
            campos.getAtributo("portalDNN")+" AS POR,"+
            campos.getAtributo("escaleraDNN")+" AS ESC,"+
            campos.getAtributo("plantaDNN")+" AS PLT," +
            campos.getAtributo("puertaDNN")+" AS PTA,"+
            campos.getAtributo("codPostalDNN")+" AS CPO,"+
            campos.getAtributo("barriadaDNN")+" AS CAS,"+
            campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
            campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
            campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
            campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
            campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
            campos.getAtributo("codMunVIA")+
            /* anadir ECOESI */
            ","+campos.getAtributo("codECO") + "," + campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");
            /* fin anadir ECOESI */
            String where = campos.getAtributo("idTerceroTER")+"="+terVO.getIdentificador()+" AND "+
            campos.getAtributo("situacionTER")+"='A' AND "+
            campos.getAtributo("normalizadoDOM")+"=2 AND "+
            campos.getAtributo("situacionDOT")+"='A'";
            ArrayList join = new ArrayList();
            join.add("T_TER");
            join.add("INNER");
            join.add("T_DOT");
            join.add(campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT"));
            join.add("INNER");
            join.add("T_TID");
            join.add(campos.getAtributo("codTID")+"="+campos.getAtributo("tipoDocTER"));
            join.add("INNER");
            join.add("T_DOM");
            join.add(campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM"));
            join.add("INNER");
            join.add("T_DNN");
            join.add(campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDNN"));
            join.add("LEFT");
            join.add("T_TOC");
            join.add(campos.getAtributo("tipoOcupacionDOT")+"="+campos.getAtributo("codTOC"));
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO+"T_MUN T_MUN");
            join.add(campos.getAtributo("codPaisDNN")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDNN")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDNN")+"="+campos.getAtributo("codMUN"));
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO+"T_PAI T_PAI");
            join.add(campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI"));
            join.add("INNER");
            join.add(GlobalNames.ESQUEMA_GENERICO+"T_PRV T_PRV");
            join.add(campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV"));
            join.add("LEFT");
            join.add("T_VIA");
            join.add(campos.getAtributo("codViaDNN")+"="+campos.getAtributo("idVIA")+" AND " +
            		campos.getAtributo("codPaisViaDNN")+"="+campos.getAtributo("codPaisVIA") + " AND " +
            		campos.getAtributo("codProvViaDNN") + " = " + campos.getAtributo("codProvVIA") + " AND " +
            		campos.getAtributo("codMunViaDNN") + " = " + campos.getAtributo("codMunVIA"));
            join.add("LEFT");
            join.add("T_TVI");
            join.add(campos.getAtributo("idTipoViaDNN") + " = " + campos.getAtributo("codTVI"));
            join.add("LEFT");
            join.add("T_ESI");
            join.add(campos.getAtributo("codESIDNN") + " = " + campos.getAtributo("codESI"));
            join.add("LEFT");
            join.add("T_ECO");
            join.add(campos.getAtributo("codECOESI") + " = " + campos.getAtributo("codECO"));
            join.add("false");

            String fromDer = "T_TER.*,T_DOM.*,"+
            campos.getAtributo("descTID")+","+
            campos.getAtributo("codTVI")+","+
            campos.getAtributo("descTVI")+"," +
            campos.getAtributo("idVIA")+","+
            campos.getAtributo("codVIA")+","+
            campos.getAtributo("descVIA")+","+
            campos.getAtributo("descVIA")+"," +
            campos.getAtributo("numDesdeDSU")+","+
            campos.getAtributo("letraDesdeDSU")+","+
            campos.getAtributo("numHastaDSU")+","+
            campos.getAtributo("letraHastaDSU")+"," +
            campos.getAtributo("bloqueDSU")+","+
            campos.getAtributo("portalDSU")+","+
            campos.getAtributo("escaleraDPO")+","+
            campos.getAtributo("plantaDPO")+"," +
            campos.getAtributo("puertaDPO")+","+
            campos.getAtributo("codPostalDSU")+","+
            campos.getAtributo("barriadaDSU")+","+
            campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
            campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
            campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
            campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
            campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
            campos.getAtributo("codMunVIA")+","+campos.getAtributo("codECO") + "," +
            campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");

            String whereDer = campos.getAtributo("idTerceroTER")+"="+terVO.getIdentificador()+" AND "+
            				campos.getAtributo("situacionTER")+"='A' AND "+
            				campos.getAtributo("normalizadoDOM")+"=1 AND "+
                            campos.getAtributo("situacionDOT")+"='A'";

            ArrayList joinDer = new ArrayList();
            joinDer.add("T_TER");
            joinDer.add("INNER");
            joinDer.add("T_DOT");
            joinDer.add(campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT"));
            joinDer.add("INNER");
            joinDer.add("T_TID");
            joinDer.add(campos.getAtributo("codTID")+"="+campos.getAtributo("tipoDocTER"));
            joinDer.add("INNER");
            joinDer.add("T_DOM");
            joinDer.add(campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM"));
            joinDer.add("INNER");
            joinDer.add("T_DPO");
            joinDer.add(campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDPO"));
            joinDer.add("INNER");
            joinDer.add("T_DSU");
            joinDer.add(campos.getAtributo("dirSueloDPO")+"="+ campos.getAtributo("codDirSueloDSU"));
            joinDer.add("LEFT");
            joinDer.add("T_TOC");
            joinDer.add(campos.getAtributo("tipoOcupacionDOT")+"="+campos.getAtributo("codTOC"));
            joinDer.add("INNER");
            joinDer.add(GlobalNames.ESQUEMA_GENERICO+"T_MUN T_MUN");
            joinDer.add(campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMUN"));
            joinDer.add("INNER");
            joinDer.add(GlobalNames.ESQUEMA_GENERICO+"T_PAI T_PAI");
            joinDer.add(campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI"));
            joinDer.add("INNER");
            joinDer.add(GlobalNames.ESQUEMA_GENERICO+"T_PRV T_PRV");
            joinDer.add(campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV")+" AND "+
                    campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV"));
            joinDer.add("INNER");
            joinDer.add("T_VIA");
            joinDer.add(campos.getAtributo("codViaDSU")+"="+campos.getAtributo("idVIA")+" AND " +
            		campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisVIA") + " AND " +
            		campos.getAtributo("codProvDSU") + " = " + campos.getAtributo("codProvVIA") + " AND " +
            		campos.getAtributo("codMunDSU") + " = " + campos.getAtributo("codMunVIA"));
            joinDer.add("INNER");
            joinDer.add("T_TVI");
            joinDer.add(campos.getAtributo("tipoVIA") + " = " + campos.getAtributo("codTVI"));
            joinDer.add("LEFT");
            joinDer.add("T_ESI");
            joinDer.add(campos.getAtributo("codigoESI") + " = " + campos.getAtributo("codESI"));
            joinDer.add("LEFT");
            joinDer.add("T_ECO");
            joinDer.add(campos.getAtributo("codECOESI") + " = " + campos.getAtributo("codECO"));
            joinDer.add("false");

            sql = bd.join(from, where, (String[])join.toArray(new String[] {}));
            String sqlDer = bd.join(fromDer, whereDer, (String[])joinDer.toArray(new String[] {}));
            sql = sql + " UNION " + sqlDer;
            if(m_Log.isDebugEnabled()) m_Log.debug("EN getByIdTercero");
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = state.executeQuery(sql);
            int actual;
            int ultimo=-1;
            DomicilioSimpleValueObject nuevo;
            Vector domicilios = null;
            TercerosValueObject terceroActual = null;
            TercerosValueObject terceroUltimo = null;
            while(rs.next()){
                actual=rs.getInt((String)campos.getAtributo("idTerceroTER"));
                if (actual==ultimo){
                    nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                            rs.getString((String)campos.getAtributo("codPAI")),
                            rs.getString((String)campos.getAtributo("codPRV")),
                            rs.getString((String)campos.getAtributo("codMUN")),
                            rs.getString((String)campos.getAtributo("descPAI")),
                            rs.getString((String)campos.getAtributo("descPRV")),
                            rs.getString((String)campos.getAtributo("descMUN")),
                            TransformacionAtributoSelect.replace(rs.getString("DESCDMC"),"\"","\\\""),rs.getString("CPO"));
                    nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                    nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                    nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                    nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                    nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                    if(rs.getString("CODVIA")!=null){
                        nuevo.setDescVia(rs.getString("DESCVIA"));
                        nuevo.setCodigoVia(rs.getString("CODVIA"));
                        nuevo.setIdVia(rs.getString("IDVIA"));
                    }
                    nuevo.setNumDesde(rs.getString("NUD"));
                    nuevo.setLetraDesde(rs.getString("LED"));
                    nuevo.setNumHasta(rs.getString("NUH"));
                    nuevo.setLetraHasta(rs.getString("LEH"));
                    nuevo.setBloque(rs.getString("BLQ"));
                    nuevo.setPortal(rs.getString("POR"));
                    nuevo.setEscalera(rs.getString("ESC"));
                    nuevo.setPlanta(rs.getString("PLT"));
                    nuevo.setPuerta(rs.getString("PTA"));
                    nuevo.setBarriada(rs.getString("CAS"));
                    nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                    nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                    nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                    /* anadir ECOESI */
                    nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                    nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                    nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                    /* fin anadir ECOESI */
                    domicilios.add(nuevo);
                }else{
                    if (terceroUltimo!=null){
                        terceroUltimo.setDomicilios(domicilios);
                        resultado.add(terceroUltimo);
                    }
                    terceroActual = new TercerosValueObject(
                            String.valueOf(actual),
                            rs.getString((String)campos.getAtributo("versionTER")),
                            rs.getString((String)campos.getAtributo("tipoDocTER")),
                            rs.getString((String)campos.getAtributo("descTID")),
                            rs.getString((String)campos.getAtributo("documentoTER")),
                            rs.getString((String)campos.getAtributo("nombreTER")),
                            rs.getString((String)campos.getAtributo("apellido1TER")),
                            rs.getString((String)campos.getAtributo("pApellido1TER")),
                            rs.getString((String)campos.getAtributo("apellido2TER")),
                            rs.getString((String)campos.getAtributo("pApellido2TER")),
                            rs.getString((String)campos.getAtributo("normalizadoTER")),
                            rs.getString((String)campos.getAtributo("telefonoTER")),
                            rs.getString((String)campos.getAtributo("emailTER")),
                            rs.getString((String)campos.getAtributo("situacionTER")).charAt(0),
                            rs.getString((String)campos.getAtributo("fechaAltaTER")),
                            rs.getString((String)campos.getAtributo("usuarioAltaTER")),
                            rs.getString((String)campos.getAtributo("moduloAltaTER")),
                            rs.getString((String)campos.getAtributo("fechaBajaTER")),
                            rs.getString((String)campos.getAtributo("usuarioBajaTER")));
                    terceroActual.setSituacion(rs.getString((String)campos.getAtributo("situacionTER")).charAt(0));
                    terceroActual.setDomPrincipal(rs.getString("TER_DOM"));
                    domicilios = new Vector();
                    if (rs.getInt((String)campos.getAtributo("idDomicilioDOM"))>0){
                        nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                                rs.getString((String)campos.getAtributo("codPAI")),
                                rs.getString((String)campos.getAtributo("codPRV")),
                                rs.getString((String)campos.getAtributo("codMUN")),
                                rs.getString((String)campos.getAtributo("descPAI")),
                                rs.getString((String)campos.getAtributo("descPRV")),
                                rs.getString((String)campos.getAtributo("descMUN")),
                                TransformacionAtributoSelect.replace(rs.getString("DESCDMC"),"\"","\\\""),rs.getString("CPO"));
                        nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                        nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                        nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                        nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                        nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                        if(rs.getString("CODVIA")!=null){
                            nuevo.setDescVia(rs.getString("DESCVIA"));
                            nuevo.setCodigoVia(rs.getString("CODVIA"));
                            nuevo.setIdVia(rs.getString("IDVIA"));
                        }
                        nuevo.setNumDesde(rs.getString("NUD"));
                        nuevo.setLetraDesde(rs.getString("LED"));
                        nuevo.setNumHasta(rs.getString("NUH"));
                        nuevo.setLetraHasta(rs.getString("LEH"));
                        nuevo.setBloque(rs.getString("BLQ"));
                        nuevo.setPortal(rs.getString("POR"));
                        nuevo.setEscalera(rs.getString("ESC"));
                        nuevo.setPlanta(rs.getString("PLT"));
                        nuevo.setPuerta(rs.getString("PTA"));
                        nuevo.setBarriada(rs.getString("CAS"));
                        nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                        nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                        nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                        /* anadir ECOESI */
                        nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                        nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                        nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                        /* fin anadir ECOESI */
                        domicilios.add(nuevo);
                    }
                    terceroUltimo=terceroActual;
                    ultimo=actual;
                }
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            if (terceroUltimo!=null){
                terceroUltimo.setDomicilios(domicilios);
                resultado.add(terceroUltimo);
            }else{
                // No hay domicilios solo devuelvo los datos del tercero
                sql = "SELECT T_TER.*,TID_DES FROM T_TER,T_TID WHERE " +
                        campos.getAtributo("idTerceroTER")+"="+terVO.getIdentificador()+" AND "+
                        campos.getAtributo("situacionTER")+"='A'"+" AND "+campos.getAtributo("codTID")+"="+campos.getAtributo("tipoDocTER");
                if(m_Log.isDebugEnabled()) m_Log.debug("No hay domicilios");
                state = con.createStatement();
                rs = state.executeQuery(sql);
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                TercerosValueObject tercero = null;
                while(rs.next()){
                    tercero = new TercerosValueObject(
                            rs.getString((String)campos.getAtributo("idTerceroTER")),
                            rs.getString((String)campos.getAtributo("versionTER")),
                            rs.getString((String)campos.getAtributo("tipoDocTER")),
                            rs.getString((String)campos.getAtributo("descTID")),
                            rs.getString((String)campos.getAtributo("documentoTER")),
                            rs.getString((String)campos.getAtributo("nombreTER")),
                            rs.getString((String)campos.getAtributo("apellido1TER")),
                            rs.getString((String)campos.getAtributo("pApellido1TER")),
                            rs.getString((String)campos.getAtributo("apellido2TER")),
                            rs.getString((String)campos.getAtributo("pApellido2TER")),
                            rs.getString((String)campos.getAtributo("normalizadoTER")),
                            rs.getString((String)campos.getAtributo("telefonoTER")),
                            rs.getString((String)campos.getAtributo("emailTER")),
                            rs.getString((String)campos.getAtributo("situacionTER")).charAt(0),
                            rs.getString((String)campos.getAtributo("fechaAltaTER")),
                            rs.getString((String)campos.getAtributo("usuarioAltaTER")),
                            rs.getString((String)campos.getAtributo("moduloAltaTER")),
                            rs.getString((String)campos.getAtributo("fechaBajaTER")),
                            rs.getString((String)campos.getAtributo("usuarioBajaTER")));
                    tercero.setSituacion(rs.getString((String)campos.getAtributo("situacionTER")).charAt(0));
                    tercero.setDomicilios(new Vector());
                    resultado.add(tercero);
                }
                cerrarResultSet(rs);
                cerrarStatement(state);
            }
            
        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);
        }
        return resultado;
    }



   /*************/
    public int setTercero(TercerosValueObject terVO,AdaptadorSQLBD bd,Connection con) throws TechnicalException{
        String sql = "";
        int max = 0;
        int ver = 1;
        ResultSet rs=null;
        Statement state=null;
        try{

            state = con.createStatement();

            String nombre = terVO.getNombreCompleto();
            sql = "SELECT " +bd.funcionMatematica(bd.FUNCIONMATEMATICA_MAX,new String[]{(String)campos.getAtributo("idTerceroTER")}) + " AS MAXIMO FROM T_TER";
            rs = state.executeQuery(sql);
            rs.next();
            max = rs.getInt("MAXIMO");
            cerrarResultSet(rs);
            cerrarStatement(state);
            if (max<0)max=1;
            else max++;

            String codigoExterno="";
            if(((terVO.getIdentificador()).equals("0"))&&(!"".equals(terVO.getCodTerceroOrigen()))&&((terVO.getCodTerceroOrigen())!=null))
            {
              codigoExterno=  terVO.getCodTerceroOrigen();
            }
            else{
                codigoExterno="null";
            }

            m_Log.debug(new Integer(max));
            sql = "INSERT INTO T_TER("+
                    campos.getAtributo("idTerceroTER")+","+
                    campos.getAtributo("tipoDocTER")+","+
                    campos.getAtributo("documentoTER")+","+
                    campos.getAtributo("nombreTER")+","+
                    campos.getAtributo("apellido1TER")+","+
                    campos.getAtributo("pApellido1TER")+","+
                    campos.getAtributo("apellido2TER")+","+
                    campos.getAtributo("pApellido2TER")+","+
                    campos.getAtributo("nombreLargoTER")+","+
                    campos.getAtributo("normalizadoTER")+","+
                    campos.getAtributo("telefonoTER")+","+
                    campos.getAtributo("emailTER")+","+
                    campos.getAtributo("situacionTER")+","+
                    campos.getAtributo("versionTER")+","+
                    campos.getAtributo("fechaAltaTER")+","+
                    campos.getAtributo("usuarioAltaTER")+","+
                    campos.getAtributo("moduloAltaTER")+","+
                    campos.getAtributo("fechaBajaTER")+","+
                    campos.getAtributo("usuarioBajaTER")+
                    ",EXTERNAL_CODE"+
                    ") VALUES(" + max + "," + bd.addString(terVO.getTipoDocumento()) +
                    "," + bd.addString(terVO.getDocumento()) + "," + bd.addString(terVO.getNombre()) + "," +
                    bd.addString(terVO.getApellido1()) + "," + bd.addString(terVO.getPartApellido1()) + "," +
                    bd.addString(terVO.getApellido2()) + "," + bd.addString(terVO.getPartApellido2()) + "," +
                    bd.addString(nombre) + "," +
                    terVO.getNormalizado() + "," + bd.addString(terVO.getTelefono()) + "," +
                    bd.addString(terVO.getEmail()) + ",'" + terVO.getSituacion() + "'," +
                    ver +","+ bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null) + "," +
                    bd.addString(terVO.getUsuarioAlta()) +
                    "," + bd.addString(terVO.getModuloAlta()) + ",null,null,"+codigoExterno+")";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();
            state.executeUpdate(sql);
            cerrarStatement(state);
            sql = "INSERT INTO T_HTE("+
                    campos.getAtributo("idTerceroHTE")+","+
                    campos.getAtributo("versionHTE")+","+
                    campos.getAtributo("tipoDocHTE")+","+
                    campos.getAtributo("documentoHTE")+","+
                    campos.getAtributo("nombreHTE")+","+
                    campos.getAtributo("apellido1HTE")+","+
                    campos.getAtributo("pApellido1HTE")+","+
                    campos.getAtributo("apellido2HTE")+","+
                    campos.getAtributo("pApellido2HTE")+","+
                    campos.getAtributo("nombreLargoHTE")+","+
                    campos.getAtributo("normalizadoHTE")+","+
                    campos.getAtributo("telefonoHTE")+","+
                    campos.getAtributo("emailHTE")+","+
                    campos.getAtributo("fechaOperHTE")+","+
                    campos.getAtributo("usuarioOperHTE")+","+
                    campos.getAtributo("moduloOperHTE")+
                     ",EXTERNAL_CODE"+
                    ") VALUES(" + max + ",'" + ver + "'," +
                    terVO.getTipoDocumento() + "," + bd.addString(terVO.getDocumento()) + "," +
                    bd.addString(terVO.getNombre()) + "," + bd.addString(terVO.getApellido1()) + "," +
                    bd.addString(terVO.getPartApellido1()) + "," + bd.addString(terVO.getApellido2()) + "," +
                    bd.addString(terVO.getPartApellido2()) + "," + bd.addString(nombre) + "," +
                    terVO.getNormalizado() + "," +
                    bd.addString(terVO.getTelefono()) + "," + bd.addString(terVO.getEmail()) + "," +
                    bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null) + "," +
                    bd.addString(terVO.getUsuarioAlta()) +
                    "," + bd.addString(terVO.getModuloAlta()) + ","+codigoExterno+")";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();
            state.executeUpdate(sql);
            cerrarStatement(state);            
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());            
            max=0;
            throw new TechnicalException("Error al dar de alta el tercero: " + e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);            
        }
        return max;
    }


   /**
     * comprueba si existe un domicilio en la base de datos. Al permitir
     * la recuperación de domicilio de otras bases de datos se tiene que comprobar
     * a la hora de insertar un domicilio si este ya existe en la base de datos.
     * @param terVO: TercerosValueObject
     * @param bd: AdaptadorSQLBD
     * @param con: Conexión a la BD ya inicializada
     * @return int: Identificador del domicilio o cero sino se ha podido recuperar
     */
    public int existeDomicilio(TercerosValueObject terVO,AdaptadorSQLBD bd,Connection con) throws TechnicalException
    {        
        ResultSet rs = null;
        Statement state = null;
        int resultado = 0;
        String sql="";

        try{
            state = con.createStatement();
            DomicilioSimpleValueObject domVO = (DomicilioSimpleValueObject)terVO.getDomicilios().get(0);

            sql = "SELECT "+ campos.getAtributo("idDomicilioDNN") + " FROM T_DNN WHERE "+
                    campos.getAtributo("codPaisDNN")+" = "+ campos.getAtributo("codigoPais") + " and ";
            if (!(StringUtils.isEmpty(domVO.getIdTipoVia())))
                sql += campos.getAtributo("idTipoViaDNN")+" = "+ domVO.getIdTipoVia() + " and  ";
            else sql += campos.getAtributo("idTipoViaDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getIdProvincia())))
                sql += campos.getAtributo("codProvDNN")+" = "+ domVO.getIdProvincia() + " and ";
            else sql += campos.getAtributo("codProvDNN")+" IS NULL and  ";
            if (!(StringUtils.isEmpty(domVO.getIdMunicipio())))
                sql += campos.getAtributo("codMunDNN")+" = "+ domVO.getIdMunicipio() + " and ";
            else sql += campos.getAtributo("codMunDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getIdPaisVia())))
                sql += campos.getAtributo("codPaisViaDNN")+" = " + domVO.getIdPaisVia() + " and ";
            else sql += campos.getAtributo("codPaisViaDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getIdProvinciaVia())))
                sql += campos.getAtributo("codProvViaDNN")+" = " +domVO.getIdProvinciaVia() + " and ";
            else sql += campos.getAtributo("codProvViaDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getIdMunicipioVia())))
                sql += campos.getAtributo("codMunViaDNN")+" = "+ domVO.getIdMunicipioVia() + " and ";
            else sql += campos.getAtributo("codMunViaDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getIdVia())))
                sql += campos.getAtributo("codViaDNN")+" = "+ domVO.getIdVia() + " and ";
            else sql += campos.getAtributo("codViaDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getNumDesde())))
                sql += campos.getAtributo("numDesdeDNN")+" = "+ domVO.getNumDesde() + " and ";
            else sql += campos.getAtributo("numDesdeDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getLetraDesde())))
                sql += campos.getAtributo("letraDesdeDNN")+" = "+ bd.addString(domVO.getLetraDesde()) + " and ";
            else sql += campos.getAtributo("letraDesdeDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getNumHasta())))
                sql += campos.getAtributo("numHastaDNN")+" = "+ domVO.getNumHasta() + " and ";
            else sql += campos.getAtributo("numHastaDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getLetraHasta())))
                sql += campos.getAtributo("letraHastaDNN")+" = "+ bd.addString(domVO.getLetraHasta()) + " and ";
            else sql += campos.getAtributo("numHastaDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getBloque())))
                sql += campos.getAtributo("bloqueDNN")+" = "+ bd.addString(domVO.getBloque()) + " and ";
            else sql += campos.getAtributo("bloqueDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getPortal())))
                sql += campos.getAtributo("portalDNN")+" = "+ bd.addString(domVO.getPortal()) + " and ";
            else sql += campos.getAtributo("portalDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getEscalera())))
                sql += campos.getAtributo("escaleraDNN")+" = "+ bd.addString(domVO.getEscalera()) + " and ";
            else sql += campos.getAtributo("escaleraDNN")+" IS NULL and  ";
            if (!(StringUtils.isEmpty(domVO.getPlanta()))) 
                sql += campos.getAtributo("plantaDNN")+" = "+ bd.addString(domVO.getPlanta()) + " and ";
            else sql += campos.getAtributo("plantaDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getPuerta())))
                sql += campos.getAtributo("puertaDNN")+" = "+ bd.addString(domVO.getPuerta()) + " and ";
            else sql += campos.getAtributo("puertaDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getDomicilio())))
                sql += campos.getAtributo("domicilioDNN")+" = "+ bd.addString(domVO.getDomicilio()) + " and ";
            else sql += campos.getAtributo("domicilioDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getCodigoPostal())))
                sql += campos.getAtributo("codPostalDNN")+" = "+ bd.addString(domVO.getCodigoPostal()) + " and ";
            else sql += campos.getAtributo("codPostalDNN")+" IS NULL and  ";
            if(!(StringUtils.isEmpty(domVO.getBarriada())))
                sql += campos.getAtributo("barriadaDNN")+" = "+domVO.getBarriada() + " and  ";
            else sql += campos.getAtributo("barriadaDNN")+" IS NULL and  ";
            sql += campos.getAtributo("situacionDNN")+" = 'A'";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                rs=state.executeQuery(sql);
            boolean seguir = rs.next();

            if(seguir) resultado = rs.getInt((String) campos.getAtributo("idDomicilioDNN"));

        }catch (Exception e){
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            throw new TechnicalException("Error al ejecutar el metodo existeDomicilio");
            
        }finally{            
            cerrarResultSet(rs);
            cerrarStatement(state);
        }
        return resultado;
    }




   /**
     * Da de alta el domicilio de un tercero
     * @param terVO; Datos del tercero y de su domicilio
     * @param bd: AdaptadorSQLBD necesario para crear las consultas SQL
     * @param con: Conexión a la BD
     * @return int con el identificador del domicilio
     * @throws es.altia.common.exception.TechnicalException
     */
    public int setDomicilioTercero(TercerosValueObject terVO,AdaptadorSQLBD bd,Connection con) throws TechnicalException{
        DomicilioSimpleValueObject domVO =
                (DomicilioSimpleValueObject)terVO.getDomicilios().get(0);
        int Normalizado=Integer.parseInt(domVO.getNormalizado());
        String sql = "";
        int max = 0;        
        ResultSet rs = null;
        Statement state = null;
        
        try{            
            // Caso domicilio no normalizado. Se inserta en la tabla T_DOM que siempre tiene valor 2 para
            // el campo normalizadoDOM, y el otro campo es el id.
            // Además se inserta en la tabla de domicilios T_DNN (Se crea un domicilio nuevo).
            if(Normalizado==2){
                sql = "SELECT "+bd.funcionMatematica(bd.FUNCIONMATEMATICA_MAX,new String[]{(String)campos.getAtributo("idDomicilioDOM")})+" AS MAXIMO FROM T_DOM";
                state = con.createStatement();
                rs=state.executeQuery(sql);
                rs.next();
                max = rs.getInt("MAXIMO");
                cerrarResultSet(rs);
                cerrarStatement(state);
                m_Log.debug(new Integer(max));
                max++;
                sql = "INSERT INTO T_DOM("+
                        campos.getAtributo("idDomicilioDOM")+"," +
                        campos.getAtributo("normalizadoDOM")+
                        ") VALUES("+max+","+ domVO.getNormalizado() +")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                state = con.createStatement();
                state.executeUpdate(sql);
                cerrarStatement(state);
                if(((!domVO.getCodigoVia().equals(""))&&
                        (!domVO.getCodigoVia().equals("0")))||
                        ((domVO.getCodigoVia().equals("0"))&&
                                (domVO.getDomicilio().equals("TRAMO SIN VIA")))){

                    domVO.setCodigoVia(domVO.getIdVia());
                }else{
                    domVO.setCodigoVia("");
                }

                if (domVO.getIdTipoVia().equalsIgnoreCase("") || domVO.getIdTipoVia()==null)
                    domVO.setIdTipoVia("null");

                sql = "INSERT INTO T_DNN("+
                        campos.getAtributo("idDomicilioDNN")+","+
                        campos.getAtributo("idTipoViaDNN")+","+
                        campos.getAtributo("codPaisDNN")+","+
                        campos.getAtributo("codProvDNN")+","+
                        campos.getAtributo("codMunDNN")+","+
                        campos.getAtributo("codPaisViaDNN")+","+
                        campos.getAtributo("codProvViaDNN")+","+
                        campos.getAtributo("codMunViaDNN")+","+
                        campos.getAtributo("codViaDNN")+","+
                        campos.getAtributo("numDesdeDNN")+","+
                        campos.getAtributo("letraDesdeDNN")+","+
                        campos.getAtributo("numHastaDNN")+","+
                        campos.getAtributo("letraHastaDNN")+","+
                        campos.getAtributo("bloqueDNN")+","+
                        campos.getAtributo("portalDNN")+","+
                        campos.getAtributo("escaleraDNN")+","+
                        campos.getAtributo("plantaDNN")+","+
                        campos.getAtributo("puertaDNN")+","+
                        campos.getAtributo("domicilioDNN")+","+
                        campos.getAtributo("codPostalDNN")+","+
                        campos.getAtributo("barriadaDNN")+","+
                        campos.getAtributo("situacionDNN")+","+
                        campos.getAtributo("fechaAltaDNN")+","+
                        campos.getAtributo("usuarioAltaDNN")+ ","+
                        campos.getAtributo("codPaisESIDNN")+ "," +
                        campos.getAtributo("codProvESIDNN")+ "," +
                        campos.getAtributo("codMunESIDNN")+ "," +
                        campos.getAtributo("codESIDNN")+
                        ") VALUES(" + max + "," + domVO.getIdTipoVia() + "," +
                        campos.getAtributo("codigoPais") + "," + domVO.getIdProvincia() + "," +
                        domVO.getIdMunicipio()+ ",";

                  if (domVO.getIdPaisVia()  == null ||domVO.getIdPaisVia() .equals("")) {
                    sql += "null,";
                } else {
                    sql += domVO.getIdPaisVia() + ",";
                }

                 if (domVO.getIdProvinciaVia()  == null ||domVO.getIdProvinciaVia() .equals("")) {
                    sql += "null,";
                } else {
                    sql += domVO.getIdProvinciaVia()  + ",";
                }

                if (domVO.getIdMunicipioVia() == null ||domVO.getIdMunicipioVia().equals("")) {
                    sql += "null,";
                } else {
                    sql +=domVO.getIdMunicipioVia() + ",";
                }
                if (domVO.getIdVia() == null || domVO.getIdVia().equals("")) {
                    sql += "null,";
                } else {
                    sql +=domVO.getIdVia() + ",";
                }

                if (domVO.getNumDesde() == null || domVO.getNumDesde().equals("")) {
                    sql += "null,";
                } else {
                    sql += domVO.getNumDesde() + ",";
                }
                sql += bd.addString(domVO.getLetraDesde());
                if (domVO.getNumHasta() == null || domVO.getNumHasta().equals("")) {
                    sql += ",null,";
                } else {
                    sql +=","+domVO.getNumHasta() + ",";
                }
                
                sql +=  bd.addString(domVO.getLetraHasta()) + "," + bd.addString(domVO.getBloque()) + "," +
                        bd.addString(domVO.getPortal()) + "," + bd.addString(domVO.getEscalera()) + "," +
                        bd.addString(domVO.getPlanta()) + "," + bd.addString(domVO.getPuerta()) + "," +
                        bd.addString(domVO.getDomicilio()) + "," + bd.addString(domVO.getCodigoPostal()) + "," +
                        bd.addString(domVO.getBarriada()) + ",'A'," +
                        bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null)+","+ terVO.getUsuarioAlta() + ",";
                if (domVO.getCodESI()!=null){
                    if (!"".equals((String) domVO.getCodESI()) ){
                        sql +=  campos.getAtributo("codigoPais") + ","	+ domVO.getIdProvincia()
                                + "," + domVO.getIdMunicipio() + "," + domVO.getCodESI();
                    } else {
                        sql += "null,null,null,null ";
                    }
                } else {
                    sql += "null,null,null,null ";
                }

                sql += ")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state=con.createStatement();
                state.executeUpdate(sql);
                cerrarStatement(state);
            }
            // Caso domicilio normalizado (Normalizado != 2)
            else{
                max=Integer.parseInt(terVO.getIdDomicilio());
            }

            // Ahora viene el código que se ejecuta sea normalizado o no el domicilio.
            // Primero se comprueba si existe la relación entre domicilio y tercero.
            String existe = "no";
            String situacionExiste ="A";
            sql = "SELECT * FROM T_DOT WHERE " + campos.getAtributo("idDomicilioDOT") + "=" +
                    max + " AND " + campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();
            rs=state.executeQuery(sql);
            if(rs.next()) {
                existe = "si";
                situacionExiste = rs.getString((String) campos.getAtributo("situacionDOT"));
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            // Si no existe registro para la relación entre el domicilio y el tercero se crea nueva.
            state = con.createStatement();
            if("no".equals(existe)) {
                sql = "INSERT INTO T_DOT("+
                        campos.getAtributo("idDomicilioDOT")+","+
                        campos.getAtributo("idTerceroDOT")+","+
                        campos.getAtributo("tipoOcupacionDOT")+","+
                        campos.getAtributo("situacionDOT")+","+
                        campos.getAtributo("fechaRelacionDOT")+","+
                        campos.getAtributo("usuarioRelacionDOT")+","+
                        campos.getAtributo("enPadronDOT")+
                        ") VALUES(" + max + "," +
                        terVO.getIdentificador() + "," + bd.addString(domVO.getCodTipoUso()) + ",'A'," +
                        bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null) + "," + terVO.getUsuarioAlta() +
                        "," + domVO.getEnPadron() + ")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state.executeUpdate(sql);
            }
            // Si la relación existe se comprueba si esta de baja y en ese caso se dá de alta.
            else {
                if("B".equals(situacionExiste)){
                    //Lo recuperamos (damos de alta)
                    sql = "UPDATE T_DOT SET " +
                            campos.getAtributo("situacionDOT") + "='A' WHERE " +
                            campos.getAtributo("idDomicilioDOT") + "=" + max + " AND " +
                            campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                   state.executeUpdate(sql);
                }
            }
            
            cerrarStatement(state);
            // Por ultimo, comprobamos si el domicilio se ha marcado como principal del
            // tercero. En ese caso anotamos su id en la columna TER_DOM de T_TER.
            state = con.createStatement();
            if (domVO.getEsDomPrincipal().equals("true")) {
                sql = "UPDATE T_TER SET TER_DOM = " + max +
                     " WHERE TER_COD = " + terVO.getIdentificador();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state.executeUpdate(sql);
            }else {
                sql = "UPDATE T_TER SET TER_DOM = " + terVO.getDomPrincipal() +
                     " WHERE TER_COD = " + terVO.getIdentificador();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state.executeUpdate(sql);
            }
            cerrarStatement(state);

        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());            
            max=0;
            throw new TechnicalException("Error al dar de alta el domicilio del tercero: " + e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);            
        }
        return max;
    }


    
    
    public int setDomicilioTercero2(TercerosValueObject terVO,AdaptadorSQLBD bd,Connection con) throws TechnicalException{
        DomicilioSimpleValueObject domVO =
                (DomicilioSimpleValueObject)terVO.getDomicilios().get(0);
        
        String sql = "";
        int max = 0;        
        ResultSet rs = null;
        Statement state = null;
        
        try{     
            
             if(m_Log.isDebugEnabled()) m_Log.debug("setDomicilioTercero2");
            // Caso domicilio no normalizado. Se inserta en la tabla T_DOM que siempre tiene valor 2 para
            // el campo normalizadoDOM, y el otro campo es el id.
            // Además se inserta en la tabla de domicilios T_DNN (Se crea un domicilio nuevo).
          
                sql = "SELECT "+bd.funcionMatematica(bd.FUNCIONMATEMATICA_MAX,new String[]{(String)campos.getAtributo("idDomicilioDOM")})+" AS MAXIMO FROM T_DOM";
                state = con.createStatement();
                rs=state.executeQuery(sql);
                rs.next();
                max = rs.getInt("MAXIMO");
                cerrarResultSet(rs);
                cerrarStatement(state);
                m_Log.debug(new Integer(max));
                max++;
                sql = "INSERT INTO T_DOM("+
                        campos.getAtributo("idDomicilioDOM")+"," +
                        campos.getAtributo("normalizadoDOM")+
                        ") VALUES("+max+",2)";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                state = con.createStatement();
                state.executeUpdate(sql);
                cerrarStatement(state);
                if(((!domVO.getCodigoVia().equals(""))&&
                        (!domVO.getCodigoVia().equals("0")))||
                        ((domVO.getCodigoVia().equals("0"))&&
                                (domVO.getDomicilio().equals("TRAMO SIN VIA")))){

                    domVO.setCodigoVia(domVO.getIdVia());
                }else{
                    domVO.setCodigoVia("");
                }

                if (domVO.getIdTipoVia().equalsIgnoreCase("") || domVO.getIdTipoVia()==null)
                    domVO.setIdTipoVia("null");

                sql = "INSERT INTO T_DNN("+
                        campos.getAtributo("idDomicilioDNN")+","+
                        campos.getAtributo("idTipoViaDNN")+","+
                        campos.getAtributo("codPaisDNN")+","+
                        campos.getAtributo("codProvDNN")+","+
                        campos.getAtributo("codMunDNN")+","+
                        campos.getAtributo("codPaisViaDNN")+","+
                        campos.getAtributo("codProvViaDNN")+","+
                        campos.getAtributo("codMunViaDNN")+","+
                        campos.getAtributo("codViaDNN")+","+
                        campos.getAtributo("numDesdeDNN")+","+
                        campos.getAtributo("letraDesdeDNN")+","+
                        campos.getAtributo("numHastaDNN")+","+
                        campos.getAtributo("letraHastaDNN")+","+
                        campos.getAtributo("bloqueDNN")+","+
                        campos.getAtributo("portalDNN")+","+
                        campos.getAtributo("escaleraDNN")+","+
                        campos.getAtributo("plantaDNN")+","+
                        campos.getAtributo("puertaDNN")+","+
                        campos.getAtributo("domicilioDNN")+","+
                        campos.getAtributo("codPostalDNN")+","+
                        campos.getAtributo("barriadaDNN")+","+
                        campos.getAtributo("situacionDNN")+","+
                        campos.getAtributo("fechaAltaDNN")+","+
                        campos.getAtributo("usuarioAltaDNN")+ ","+
                        campos.getAtributo("codPaisESIDNN")+ "," +
                        campos.getAtributo("codProvESIDNN")+ "," +
                        campos.getAtributo("codMunESIDNN")+ "," +
                        campos.getAtributo("codESIDNN")+
                        ") VALUES(" + max + "," + domVO.getIdTipoVia() + "," +
                        campos.getAtributo("codigoPais") + "," + domVO.getIdProvincia() + "," +
                        domVO.getIdMunicipio()+ ","; 

                  if (domVO.getIdPaisVia()  == null ||domVO.getIdPaisVia() .equals("")) {
                    sql += "null,";
                } else {
                    sql += domVO.getIdPaisVia() + ",";
                }

                 if (domVO.getIdProvinciaVia()  == null ||domVO.getIdProvinciaVia() .equals("")) {
                    sql += "null,";
                } else {
                    sql += domVO.getIdProvinciaVia()  + ",";
                }

                if (domVO.getIdMunicipioVia() == null ||domVO.getIdMunicipioVia().equals("")) {
                    sql += "null,";
                } else {
                    sql +=domVO.getIdMunicipioVia() + ",";
                }
                if (domVO.getIdVia() == null || domVO.getIdVia().equals("")) {
                    sql += "null,";
                } else {
                    sql +=domVO.getIdVia() + ",";
                }

                if (domVO.getNumDesde() == null || domVO.getNumDesde().equals("")) {
                    sql += "null,";
                } else {
                    sql += domVO.getNumDesde() + ",";
                }
                sql += bd.addString(domVO.getLetraDesde());
                if (domVO.getNumHasta() == null || domVO.getNumHasta().equals("")) {
                    sql += ",null,";
                } else {
                    sql +=","+domVO.getNumHasta() + ",";
                }
                
                sql +=  bd.addString(domVO.getLetraHasta()) + "," + bd.addString(domVO.getBloque()) + "," +
                        bd.addString(domVO.getPortal()) + "," + bd.addString(domVO.getEscalera()) + "," +
                        bd.addString(domVO.getPlanta()) + "," + bd.addString(domVO.getPuerta()) + "," +
                        bd.addString(domVO.getDomicilio()) + "," + bd.addString(domVO.getCodigoPostal()) + "," +
                        bd.addString(domVO.getBarriada()) + ",'A'," +
                        bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null)+","+ terVO.getUsuarioAlta() + ",";
                if (domVO.getCodESI()!=null){
                    if (!"".equals((String) domVO.getCodESI()) ){
                        sql +=  campos.getAtributo("codigoPais") + ","	+ domVO.getIdProvincia()
                                + "," + domVO.getIdMunicipio() + "," + domVO.getCodESI();
                    } else {
                        sql += "null,null,null,null ";
                    }
                } else {
                    sql += "null,null,null,null ";
                }

                sql += ")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state=con.createStatement();
                state.executeUpdate(sql);
                cerrarStatement(state);
           

            // Ahora viene el código que se ejecuta sea normalizado o no el domicilio.
            // Primero se comprueba si existe la relación entre domicilio y tercero.
            String existe = "no";
            String situacionExiste ="A";
            sql = "SELECT * FROM T_DOT WHERE " + campos.getAtributo("idDomicilioDOT") + "=" +
                    max + " AND " + campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();
            rs=state.executeQuery(sql);
            if(rs.next()) {
                existe = "si";
                situacionExiste = rs.getString((String) campos.getAtributo("situacionDOT"));
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            // Si no existe registro para la relación entre el domicilio y el tercero se crea nueva.
            state = con.createStatement();
            if("no".equals(existe)) {
                sql = "INSERT INTO T_DOT("+
                        campos.getAtributo("idDomicilioDOT")+","+
                        campos.getAtributo("idTerceroDOT")+","+
                        campos.getAtributo("tipoOcupacionDOT")+","+
                        campos.getAtributo("situacionDOT")+","+
                        campos.getAtributo("fechaRelacionDOT")+","+
                        campos.getAtributo("usuarioRelacionDOT")+","+
                        campos.getAtributo("enPadronDOT")+
                        ") VALUES(" + max + "," +
                        terVO.getIdentificador() + "," + bd.addString(domVO.getCodTipoUso()) + ",'A'," +
                        bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null) + "," + terVO.getUsuarioAlta() +
                        ",0)";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state.executeUpdate(sql);
            }
            // Si la relación existe se comprueba si esta de baja y en ese caso se dá de alta.
            else {
                if("B".equals(situacionExiste)){
                    //Lo recuperamos (damos de alta)
                    sql = "UPDATE T_DOT SET " +
                            campos.getAtributo("situacionDOT") + "='A' WHERE " +
                            campos.getAtributo("idDomicilioDOT") + "=" + max + " AND " +
                            campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                   state.executeUpdate(sql);
                }
            }
            
            cerrarStatement(state);
            // Por ultimo, comprobamos si el domicilio se ha marcado como principal del
            // tercero. En ese caso anotamos su id en la columna TER_DOM de T_TER.
            state = con.createStatement();
            if (domVO.getEsDomPrincipal().equals("true")) {
                sql = "UPDATE T_TER SET TER_DOM = " + max +
                     " WHERE TER_COD = " + terVO.getIdentificador();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state.executeUpdate(sql);
            }
            cerrarStatement(state);

        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());            
            max=0;
            throw new TechnicalException("Error al dar de alta el domicilio del tercero: " + e.getMessage());
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state); 
            if(m_Log.isDebugEnabled()) m_Log.debug("fin setDomicilioTercero2");
        }
        return max;
    }


    /******************************** PRUEBA *****************************************/

    /**
     * Actualiza la información de un tercero
     * @param terVO: Objeto que contiene la información del tercero
     * @param bd :AdaptadorSQLBD necesario para tratar la fecha de modificación, debe estar inicializado
     * @param con: Conexión a la BBDD ya inicializada en una transacción
     * @return id del tercero
     * @throws es.altia.common.exception.TechnicalException si ocurre algún error durante la ejecución del operación
     */
    public int updateTercero(TercerosValueObject terVO,AdaptadorSQLBD bd,Connection con) throws TechnicalException{
        String sql = "";
        int ver = Integer.parseInt(terVO.getVersion())+1;
        int id = 0;        
        Statement state=null;
        
        try{            
            state = con.createStatement();

            String nombre = terVO.getNombreCompleto();
            id = Integer.parseInt(terVO.getIdentificador());
            sql = "UPDATE T_TER SET " +
                    campos.getAtributo("versionTER") + "='" + ver + "'," +
                    campos.getAtributo("tipoDocTER") + "=" + bd.addString(terVO.getTipoDocumento()) + "," +
                    campos.getAtributo("documentoTER") + "=" + bd.addString(terVO.getDocumento()) + "," +
                    campos.getAtributo("nombreTER") + "=" + bd.addString(terVO.getNombre()) + "," +
                    campos.getAtributo("apellido1TER") + "=" + bd.addString(terVO.getApellido1()) + "," +
                    campos.getAtributo("pApellido1TER") + "=" +bd.addString( terVO.getPartApellido1()) + "," +
                    campos.getAtributo("apellido2TER") + "=" + bd.addString(terVO.getApellido2()) + "," +
                    campos.getAtributo("pApellido2TER") + "=" + bd.addString(terVO.getPartApellido2()) + "," +
                    campos.getAtributo("nombreLargoTER") + "=" + bd.addString(nombre) + "," +
                    campos.getAtributo("telefonoTER") + "='" + terVO.getTelefono() + "'," +
                    campos.getAtributo("emailTER") + "='" + terVO.getEmail() + "'";
                    if (!terVO.getDomPrincipal().equals("")) {
                        sql += ", TER_DOM = " + terVO.getDomPrincipal();
                    }
                    sql += " WHERE " + campos.getAtributo("idTerceroTER") + "=" + terVO.getIdentificador();

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state.executeUpdate(sql);
            state.close();

            sql = "INSERT INTO T_HTE("+
                    campos.getAtributo("idTerceroHTE")+","+
                    campos.getAtributo("versionHTE")+","+
                    campos.getAtributo("tipoDocHTE")+","+
                    campos.getAtributo("documentoHTE")+","+
                    campos.getAtributo("nombreHTE")+","+
                    campos.getAtributo("apellido1HTE")+","+
                    campos.getAtributo("pApellido1HTE")+","+
                    campos.getAtributo("apellido2HTE")+","+
                    campos.getAtributo("pApellido2HTE")+","+
                    campos.getAtributo("nombreLargoHTE")+","+
                    campos.getAtributo("normalizadoHTE")+","+
                    campos.getAtributo("telefonoHTE")+","+
                    campos.getAtributo("emailHTE")+","+
                    campos.getAtributo("fechaOperHTE")+","+
                    campos.getAtributo("usuarioOperHTE")+","+
                    campos.getAtributo("moduloOperHTE")+
                    ") VALUES(" + id + ",'" + ver + "'," +
                    terVO.getTipoDocumento() + ",'" + terVO.getDocumento() + "'," +
                    bd.addString(terVO.getNombre()) + "," + bd.addString(terVO.getApellido1()) + "," +
                    bd.addString(terVO.getPartApellido1()) + "," + bd.addString(terVO.getApellido2()) + "," +
                    bd.addString(terVO.getPartApellido2()) + "," + bd.addString(nombre )+ "," +
                    terVO.getNormalizado() + ",'" +
                    terVO.getTelefono() + "','" + terVO.getEmail() + "'," +
                    bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null)+","+
                    terVO.getUsuarioAlta() +
                    "," + terVO.getModuloAlta() + ")";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state =  con.createStatement();
            state.execute(sql);
            state.close();
            
        }catch (Exception e) {
            m_Log.error(e.getMessage());
            id=0;
            throw new TechnicalException("Error al actualizar el tercero",e);
        }finally{
            try{
                if(state!=null) state.close();
            }catch(SQLException e){
                m_Log.error("Error en claúsula finally al cerrar el Statement " + e.getMessage());
            }
        }
        return id;
    }


    /****************************************************/

    /**
     * Actualiza la info de un domicilio de un tercero
     * @param terVO: Objeto con contiene la información del tercero y del domicilio
     * @param bd: AdaptadorSQLBD ya inicializado
     * @param con: Conexión a la BD ya inicializada en una transacción
     * @throws es.altia.common.exception.TechnicalException
     */
    public void updateDomicilioTercero(TercerosValueObject terVO,AdaptadorSQLBD bd,Connection con)throws TechnicalException{

        DomicilioSimpleValueObject domVO = (DomicilioSimpleValueObject)terVO.getDomicilios().get(0);
        int Normalizado=Integer.parseInt(domVO.getNormalizado());
        String sql = "";        
        ResultSet rs=null;
        Statement state=null;

        try{
            
            if(Normalizado==2){
                if(((!domVO.getCodigoVia().equals("")))||((domVO.getCodigoVia().equals("0"))&&
                                (domVO.getDomicilio().equals("TRAMO SIN VIA")))){
                    domVO.setCodigoVia(domVO.getIdVia());
                }else{
                    domVO.setCodigoVia("");
                }

                sql = "UPDATE T_DNN SET " +
                        campos.getAtributo("idTipoViaDNN") + "=" + bd.addString(domVO.getIdTipoVia()) + "," +
                        campos.getAtributo("codPaisDNN") + "=" + campos.getAtributo("codigoPais") + "," +
                        campos.getAtributo("codProvDNN") + "=" + domVO.getIdProvincia() + "," +
                        campos.getAtributo("codMunDNN") + "=" + domVO.getIdMunicipio() + "," +
                        campos.getAtributo("codPaisViaDNN") + "=" + bd.addString(domVO.getIdPaisVia()) + "," +
                        campos.getAtributo("codProvViaDNN") + "=" + bd.addString(domVO.getIdProvinciaVia()) + "," +
                        campos.getAtributo("codMunViaDNN") + "=" + bd.addString(domVO.getIdMunicipioVia()) + "," +
                        campos.getAtributo("codViaDNN") + "=" + bd.addString(domVO.getCodigoVia()) + "," +
                        campos.getAtributo("numDesdeDNN") + "=" + bd.addString(domVO.getNumDesde()) + "," +
                        campos.getAtributo("letraDesdeDNN") + "=" + bd.addString(domVO.getLetraDesde()) + ", " +
                        campos.getAtributo("numHastaDNN") + "=" + bd.addString(domVO.getNumHasta()) + "," +
                        campos.getAtributo("letraHastaDNN") + "=" + bd.addString(domVO.getLetraHasta()) + "," +
                        campos.getAtributo("bloqueDNN") + "=" + bd.addString(domVO.getBloque()) + "," +
                        campos.getAtributo("portalDNN") + "=" + bd.addString(domVO.getPortal()) + "," +
                        campos.getAtributo("escaleraDNN") + "=" + bd.addString(domVO.getEscalera()) + "," +
                        campos.getAtributo("plantaDNN") + "=" + bd.addString(domVO.getPlanta()) + "," +
                        campos.getAtributo("puertaDNN") + "=" + bd.addString(domVO.getPuerta()) + "," +
                        campos.getAtributo("domicilioDNN") + "=" + bd.addString(domVO.getDomicilio()) + "," +
                        campos.getAtributo("codPostalDNN") + "=" + bd.addString(domVO.getCodigoPostal()) + "," +
                        campos.getAtributo("barriadaDNN") + "=" + bd.addString(domVO.getBarriada()) + " ";

                if (domVO.getCodESI()!=null){
                    if (!"".equals((String) domVO.getCodESI()) ){
                        sql += "," + campos.getAtributo("codPaisESIDNN") + "=" + campos.getAtributo("codigoPais") + ","
                                + campos.getAtributo("codProvESIDNN")+ "=" + domVO.getIdProvincia() + ","
                                + campos.getAtributo("codMunESIDNN")+ "=" + domVO.getIdMunicipio() + ","
                                + campos.getAtributo("codESIDNN")+ "=" + domVO.getCodESI();
                    } else {
                        sql += "," + campos.getAtributo("codPaisESIDNN") + "= null,"
                                + campos.getAtributo("codProvESIDNN")+ "= null,"
                                + campos.getAtributo("codMunESIDNN")+ "= null,"
                                + campos.getAtributo("codESIDNN")+ "= null ";
                    }
                } else {
                    sql += "," + campos.getAtributo("codPaisESIDNN") + "= null,"
                            + campos.getAtributo("codProvESIDNN")+ "= null,"
                            + campos.getAtributo("codMunESIDNN")+ "= null,"
                            + campos.getAtributo("codESIDNN")+ "= null ";
                }

                sql += " WHERE " + campos.getAtributo("idDomicilioDNN") + "=" + domVO.getIdDomicilio();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state = con.createStatement();
                state.executeUpdate(sql);
                cerrarStatement(state);
            }

            state = con.createStatement();
            sql = "UPDATE T_DOT SET " +
                    campos.getAtributo("tipoOcupacionDOT") + "=" + bd.addString(domVO.getCodTipoUso()) +
                    " WHERE " +
                    campos.getAtributo("idDomicilioDOT") + "=" + domVO.getIdDomicilio() + " AND " +
                    campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state.executeUpdate(sql);
            cerrarStatement(state);
            domVO.setModificado("SI");
            
        }catch (Exception e) {
            m_Log.error(e.getMessage());
            domVO.setModificado("NO");
            throw new TechnicalException("Error al actualizar el domicilio de " + terVO.getIdentificador(),e);

        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);            
        }

    }



    /**
     * Actualiza el EXTERNAL_CODE de un tercero, tanto en la tabla T_TER como en T_HTE. Si se pasa el documento, también
     * se actualiza este dato del tercero
     * @param codTercero: Código del tercero
     * @param externalCode: Código externo de tercero
     * @param documento: Documento del tercero
     * @param con: Conexión a la BBDD
     * @return boolean
     */
    public boolean actualizarCodigoExternoTercero(String codTercero,String externalCode,String documento,Connection con){
        Statement st = null;
        ResultSet rs = null;
        boolean exito = false;

        try{
            String sql = "";
            // Se actualiza T_TER
            String parte1     = "UPDATE T_TER SET EXTERNAL_CODE='" + externalCode + "' ";
            String parte2     = ",TER_DOC='" + documento + "' ";
            String parteWhere = "WHERE TER_COD=" + codTercero + " AND TER_NVE=1";

            sql = parte1 + parteWhere;
            if(documento!=null && !"".equals(documento)) // Si hay documento del tercero para actualizar
                sql = parte1 + parte2 + parteWhere;

            st = con.createStatement();
            m_Log.debug(sql);
            int rowsUpdated = st.executeUpdate(sql);
            st.close();

            m_Log.debug("rowsUpdated: " + rowsUpdated);

            
            parte1      = "UPDATE T_HTE SET EXTERNAL_CODE='" + externalCode + "' ";
            parte2      = ",HTE_DOC='" + documento + "' ";
            parteWhere  = " WHERE HTE_TER=" + codTercero + " AND HTE_NVR=1";
            
            sql = parte1 + parteWhere;
            m_Log.debug(sql);
            if(documento!=null && !"".equals(documento)) // Si hay documento del tercero para actualizar
                sql = parte1 + parte2 + parteWhere;

            st = con.createStatement();
            rowsUpdated = st.executeUpdate(sql);
            m_Log.debug("Num filas T_HTE actualizadas: " + rowsUpdated);
            exito = true;
            
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return exito;
    }

    public TercerosValueObject existeTerceroExterno(String codigoExterno,String documento, Connection con) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        TercerosValueObject terExtVO= null;
        
        try {            
            String sql = "SELECT TER_COD, MAX(TER_NVE) FROM T_TER WHERE EXTERNAL_CODE = "+ codigoExterno + " AND TER_FBJ IS NULL";
            if (documento!=null) sql=sql+" and TER_DOC='"+documento+"'";
            sql=sql+" group by TER_COD";
            m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                terExtVO = new TercerosValueObject();
                terExtVO.setIdentificador(rs.getString(1));
                terExtVO.setVersion(rs.getString(2));
            }
        } catch (SQLException sQLException) {
            terExtVO = null;
            throw sQLException;
        } finally { 
            st.close();
            rs.close();

        }
        return terExtVO;
    }
    
    public ArrayList<TercerosValueObject> getInteresadosExpediente(String codMunicipio,String numExpediente,String codProcedimiento,String ejercicio,Connection con, String[] params){
        if(m_Log.isDebugEnabled()) m_Log.debug("getInteresadosExpediente() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        ArrayList<TercerosValueObject> interesados = new ArrayList<TercerosValueObject>();
        try{
            String sql = "SELECT HTE_TER " + 
                         "FROM E_EXT,T_HTE,T_TID,E_ROL " +
                         "WHERE EXT_MUN=" + codMunicipio + " AND EXT_NUM='" + numExpediente + "' AND EXT_EJE=" + ejercicio +
                         " AND EXT_TER=HTE_TER AND EXT_NVR=HTE_NVR " +
                         " AND HTE_TID=TID_COD" +
                         " AND EXT_ROL=ROL_COD AND ROL_MUN=" + codMunicipio + " AND ROL_PRO='" + codProcedimiento + "'";
            
            m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                String codTercero = rs.getString("HTE_TER");

                TercerosValueObject tVO = new TercerosValueObject();
                tVO.setIdentificador(codTercero);
              
                ArrayList<TercerosValueObject> terceros = getByIdTercero(tVO,con,params);
                for(TercerosValueObject tercero : terceros){
                    if(tercero.getIdentificador().equalsIgnoreCase(codTercero)){
                        interesados.add(tercero);  
                    }
                }//for(TercerosValueObject tercero : terceros)
            }// while
        }catch(SQLException e){
            e.printStackTrace();
        }catch(TechnicalException e){
            e.printStackTrace();
        }
        finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("getInteresadosExpediente() : END");
        return interesados;
    }//getInteresadosExpediente
    
    
    
    /**
     * Comprueba si un domicilio está asignado a más de un tercero
     * @param codDomicilio: Código del domicilio
     * @param con: Conexión a la BBDD
     * @return Un boolean
     * @throws SQLException  si ocurre algún error
     */
    
    public boolean estaAsignadoDomicilioVariosTerceros(String codDomicilio,Connection con) throws SQLException {
        boolean exito = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            String sql = "SELECT COUNT(*) AS NUM FROM T_DOT WHERE DOT_DOM=?";
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++,Integer.parseInt(codDomicilio));
            rs = ps.executeQuery();
            int num = 0;
            
            while(rs.next()){                
                num = rs.getInt("NUM");
            }
            
            if(num>1) exito = true;
            
        }catch(SQLException e){
            exito = false;
            throw e;            
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
                
            }catch(SQLException e){
                m_Log.error("Error al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }
        }
        
        return exito;
    }
 
    /**
     * Comprueba si un domicilio está asignado a un registro o expediente
     * @param codDomicilio: Código del domicilio
     * @param con: Conexión a la BBDD
     * @return Un boolean
     * @throws SQLException  si ocurre algún error
     */
    
    public boolean existeDomEnRegistroExpediente(String codDomicilio,Connection con) throws SQLException {
        boolean exito = false;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        String sql;
        int i = 1;
        
        try{
            sql = "SELECT COUNT(*) AS NUM FROM R_EXT WHERE EXT_DOT=?";
            m_Log.debug("query: " + sql);
            
            ps = con.prepareStatement(sql);
            ps.setInt(i,Integer.parseInt(codDomicilio));
            rs = ps.executeQuery();
            int num = 0;
            
            while(rs.next()){                
                num = rs.getInt("NUM");
            }
            
            if(num>0) exito = true;
            else {
                sql = "SELECT COUNT(*) AS NUM FROM E_EXT WHERE EXT_DOT=?";
                m_Log.debug("query: " + sql);
                
                ps2 = con.prepareStatement(sql);
                ps2.setInt(i,Integer.parseInt(codDomicilio));
                rs2 = ps2.executeQuery();
                num = 0;

                while(rs2.next()){                
                    num = rs2.getInt("NUM");
                }

                if(num>0) exito = true;
            }
            
        }catch(SQLException e){
            m_Log.debug("Error al comprobar si el domicilio está asociado a un registro o expediente.");
            e.printStackTrace();
            throw e;            
        }finally{
            try{
                if(ps!=null) ps.close();
                if(ps2!=null) ps2.close();
                if(rs!=null) rs.close();
                if(rs2!=null) rs2.close();
                
            }catch(SQLException e){
                m_Log.error("Error al cerrar recursos de la BBDD");
            }
        }
        return exito;
    }
    
    /***** PRUEBA ******/
    
    /**
     *  Este método se debe llamar cuando se pretende modificar un domicilio de un tercero, que a su vez, 
     * es domicilio de otros terceros. Esto implica que para que la modificación del domicilio no afecte
     * a los otros terceros, entonces, se da de alta un nuevo domicilio, se le asocia al tercero y se 
     * da de baja el anterior.
     * @param terVO: Datos del tercero
     * @param codDomicilioModificado: Código del domicilio que se modifica. Se utiliza para darlo de baja
     * @param con: Conexión a la BBDD
     * @return se devuelve un int con el código de domicilio generado
     * @throws TechnicalException si ocurre algún error al acceder a la BBDD
     */
    public int modificarDomicilioCreandoNuevo(TercerosValueObject terVO,String codDomicilioModificado, Connection con,AdaptadorSQLBD bd) throws TechnicalException{
        DomicilioSimpleValueObject domVO =
                (DomicilioSimpleValueObject)terVO.getDomicilios().get(0);
        int Normalizado=Integer.parseInt(domVO.getNormalizado());
        String sql = "";
        int max = 0;                
        ResultSet rs = null;
        Statement state = null;        
                
        try{            
                    
            if(Normalizado==2){
                
                sql = "SELECT MAX("+ campos.getAtributo("idDomicilioDOM") + ") AS MAXIMO FROM T_DOM";
                state = con.createStatement();
                rs=state.executeQuery(sql);
                rs.next();
                max = rs.getInt("MAXIMO");
                cerrarResultSet(rs);
                cerrarStatement(state);
                m_Log.debug(new Integer(max));
                max++;
                sql = "INSERT INTO T_DOM("+
                        campos.getAtributo("idDomicilioDOM")+"," +
                        campos.getAtributo("normalizadoDOM")+
                        ") VALUES("+max+","+ domVO.getNormalizado() +")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);

                state = con.createStatement();
                state.executeUpdate(sql);
                cerrarStatement(state);
                if(((!domVO.getCodigoVia().equals(""))&&
                        (!domVO.getCodigoVia().equals("0")))||
                        ((domVO.getCodigoVia().equals("0"))&&
                                (domVO.getDomicilio().equals("TRAMO SIN VIA")))){

                    domVO.setCodigoVia(domVO.getIdVia());
                }else{
                    domVO.setCodigoVia("");
                }

                if (domVO.getIdTipoVia().equalsIgnoreCase("") || domVO.getIdTipoVia()==null)
                    domVO.setIdTipoVia("null");

                
                /**** NUEVO *****/
                sql = "INSERT INTO T_DNN("+
                        campos.getAtributo("idDomicilioDNN")+","+
                        campos.getAtributo("idTipoViaDNN")+","+
                        campos.getAtributo("codPaisDNN")+","+
                        campos.getAtributo("codProvDNN")+","+
                        campos.getAtributo("codMunDNN")+","+
                        campos.getAtributo("codPaisViaDNN")+","+
                        campos.getAtributo("codProvViaDNN")+","+
                        campos.getAtributo("codMunViaDNN")+","+
                        campos.getAtributo("codViaDNN")+","+
                        campos.getAtributo("numDesdeDNN")+","+
                        campos.getAtributo("letraDesdeDNN")+","+
                        campos.getAtributo("numHastaDNN")+","+
                        campos.getAtributo("letraHastaDNN")+","+
                        campos.getAtributo("bloqueDNN")+","+
                        campos.getAtributo("portalDNN")+","+
                        campos.getAtributo("escaleraDNN")+","+
                        campos.getAtributo("plantaDNN")+","+
                        campos.getAtributo("puertaDNN")+","+
                        campos.getAtributo("domicilioDNN")+","+
                        campos.getAtributo("codPostalDNN")+","+
                        campos.getAtributo("barriadaDNN")+","+
                        campos.getAtributo("situacionDNN")+","+
                        campos.getAtributo("fechaAltaDNN")+","+
                        campos.getAtributo("usuarioAltaDNN")+ ","+
                        campos.getAtributo("codPaisESIDNN")+ "," +
                        campos.getAtributo("codProvESIDNN")+ "," +
                        campos.getAtributo("codMunESIDNN")+ "," +
                        campos.getAtributo("codESIDNN")+
                        ") VALUES(" + max + "," + domVO.getIdTipoVia() + "," +
                        campos.getAtributo("codigoPais") + "," + domVO.getIdProvincia() + "," +
                        domVO.getIdMunicipio()+ ",";

                  if (domVO.getIdPaisVia()  == null ||domVO.getIdPaisVia() .equals("")) {
                    sql += "null,";
                } else {
                    sql += domVO.getIdPaisVia() + ",";
                }

                 if (domVO.getIdProvinciaVia()  == null ||domVO.getIdProvinciaVia() .equals("")) {
                    sql += "null,";
                } else {
                    sql += domVO.getIdProvinciaVia()  + ",";
                }

                if (domVO.getIdMunicipioVia() == null ||domVO.getIdMunicipioVia().equals("")) {
                    sql += "null,";
                } else {
                    sql +=domVO.getIdMunicipioVia() + ",";
                }
                if (domVO.getIdVia() == null || domVO.getIdVia().equals("")) {
                    sql += "null,";
                } else {
                    sql +=domVO.getIdVia() + ",";
                }

                if (domVO.getNumDesde() == null || domVO.getNumDesde().equals("")) {
                    sql += "null,";
                } else {
                    sql += domVO.getNumDesde() + ",";
                }
                sql += bd.addString(domVO.getLetraDesde());
                if (domVO.getNumHasta() == null || domVO.getNumHasta().equals("")) {
                    sql += ",null,";
                } else {
                    sql +=","+domVO.getNumHasta() + ",";
                }
                
                sql +=  bd.addString(domVO.getLetraHasta()) + "," + bd.addString(domVO.getBloque()) + "," +
                        bd.addString(domVO.getPortal()) + "," + bd.addString(domVO.getEscalera()) + "," +
                        bd.addString(domVO.getPlanta()) + "," + bd.addString(domVO.getPuerta()) + "," +
                        bd.addString(domVO.getDomicilio()) + "," + bd.addString(domVO.getCodigoPostal()) + "," +
                        bd.addString(domVO.getBarriada()) + ",'A'," +
                        bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null)+","+ terVO.getUsuarioAlta() + ",";
                if (domVO.getCodESI()!=null){
                    if (!"".equals((String) domVO.getCodESI()) ){
                        sql +=  campos.getAtributo("codigoPais") + ","	+ domVO.getIdProvincia()
                                + "," + domVO.getIdMunicipio() + "," + domVO.getCodESI();
                    } else {
                        sql += "null,null,null,null ";
                    }
                } else {
                    sql += "null,null,null,null ";
                }

                sql += ")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state=con.createStatement();
                state.executeUpdate(sql);
                cerrarStatement(state);
                /**** NUEVO *****/
            }
            // Caso domicilio normalizado (Normalizado != 2)
            else{
                max=Integer.parseInt(terVO.getIdDomicilio());
            }

            // Ahora viene el código que se ejecuta sea normalizado o no el domicilio.
            // Primero se comprueba si existe la relación entre domicilio y tercero.
            String existe = "no";
            String situacionExiste ="A";
            sql = "SELECT * FROM T_DOT WHERE " + campos.getAtributo("idDomicilioDOT") + "=" +
                    max + " AND " + campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();
            rs=state.executeQuery(sql);
            if(rs.next()) {
                existe = "si";
                situacionExiste = rs.getString((String) campos.getAtributo("situacionDOT"));
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            // Si no existe registro para la relación entre el domicilio y el tercero se crea nueva.            
            if("no".equals(existe)) {
                sql = "INSERT INTO T_DOT("+
                        campos.getAtributo("idDomicilioDOT")+","+
                        campos.getAtributo("idTerceroDOT")+","+
                        campos.getAtributo("tipoOcupacionDOT")+","+
                        campos.getAtributo("situacionDOT")+","+
                        campos.getAtributo("fechaRelacionDOT")+","+
                        campos.getAtributo("usuarioRelacionDOT")+","+
                        campos.getAtributo("enPadronDOT")+
                        ") VALUES(" + max + "," +
                        terVO.getIdentificador() + ",null,'A'," +  bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null) + "," +
                        terVO.getUsuarioAlta() + "," + domVO.getEnPadron() + ")";
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state = con.createStatement();
                state.executeUpdate(sql);
                cerrarStatement(state);
            }
            // Si la relación existe se comprueba si esta de baja y en ese caso se dá de alta.
            else {
                if("B".equals(situacionExiste)){
                    //Lo recuperamos (damos de alta)
                    sql = "UPDATE T_DOT SET " +
                            campos.getAtributo("situacionDOT") + "='A' WHERE " +
                            campos.getAtributo("idDomicilioDOT") + "=" + max + " AND " +
                            campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    state = con.createStatement();
                    state.executeUpdate(sql);
                    cerrarStatement(state);
                }
            }
            
            // Por ultimo, comprobamos si el domicilio se ha marcado como principal del
            // tercero. En ese caso anotamos su id en la columna TER_DOM de T_TER.
                        
            // Si el domicilio a modificar era el actual, entonces ahora el nuevo será el principal
            // Sino lo es, esto quiere decir que el usuario tiene al menos otro domicilio marcado como 
            // principal, entonces, no se hace nada
            /*if (domVO.getEsDomPrincipal().equals("true")) {
                sql = "UPDATE T_TER SET TER_DOM = " + max +
                     " WHERE TER_COD = " + terVO.getIdentificador();
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state = con.createStatement();
                state.executeUpdate(sql);
                cerrarStatement(state);
            }*/
            
            //#129378 Hacemos el update de T_TER independientemente de si es domicilio principla.
            //Se hace el update si en la tabla t_ter esta el domicilio que se va a dar de baja.
            
             sql = "UPDATE T_TER SET TER_DOM = " + max +
                     " WHERE TER_COD = " + terVO.getIdentificador() +" AND TER_DOM="+codDomicilioModificado;
                if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                state = con.createStatement();
                state.executeUpdate(sql);
                cerrarStatement(state);
            
            // Se marca como de baja el domicilio antiguo en la tabla T_DOT, que contiene la relación
            // los domicilios que están asociados a un tercero
             sql = "UPDATE T_DOT SET " +
                    campos.getAtributo("situacionDOT") +"='B'," +
                    campos.getAtributo("fechaRelacionDOT") +"=" + bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null) + "," + campos.getAtributo("usuarioRelacionDOT") +"=" + terVO.getUsuarioBaja() + 
                    " WHERE " + campos.getAtributo("idDomicilioDOT") + "=" + codDomicilioModificado + 
                    " AND " + campos.getAtributo("idTerceroDOT") + "=" + terVO.getIdentificador();
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();            
            state.executeUpdate(sql);
            cerrarStatement(state);
            
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());            
            max=0;
            throw new TechnicalException(e.getMessage());
            
        }finally{
            try{
                if(state!=null) state.close();                
                if(rs!=null) rs.close();                
            }catch(SQLException e){
                m_Log.error("Eror al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }
            
        }
        return max;
    }
    
    /***** PRUEBA ******/
    
    /**
     * Inserta la relacion entre tercero y domicilio en la tabla T_DOT, si no
     * existe ya. Si existe pero esta de baja la cambia a alta.
     * @param codTercero
     * @param codDomicilio
     * @param con
     * @throws java.sql.Exception
     */
    public boolean altaDomicilioTercero(int codTercero, int codDomicilio, String usuario, String[] params)throws Exception {

        String sql;
        PreparedStatement ps = null;
        AdaptadorSQLBD bd=null;
        Connection con=null;        
        Statement st = null;
        ResultSet rs = null;
        boolean actualizacionDomicillioRealizada = true;
        if (m_Log.isDebugEnabled()) m_Log.debug("TercerosDAO.altaDomicilioTercero, tercero =" +
                codTercero + ", domicilio=" + codDomicilio);

        try {

            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            bd.inicioTransaccion(con);
            
            // Se comprueba si ya existe la relacion
            sql = "SELECT DOT_SIT FROM T_DOT " +
                  "WHERE DOT_DOM = " + codDomicilio + " AND DOT_TER = " + codTercero;

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);

            String situacion = null;
            if (rs.next()) {
                situacion = rs.getString(1);
            }

            rs.close();

            // Caso de que no exista la relacion
            if (situacion == null) {

                if (m_Log.isDebugEnabled()) m_Log.debug("No existe relacion");
                int usuarioAlta = Integer.parseInt(usuario);
                sql = "INSERT INTO T_DOT(DOT_DOM, DOT_TER, DOT_TOC, DOT_SIT, " +
                                        "DOT_FEC, DOT_USU, DOT_DPA) " +
                      "VALUES (?, ?, ?, 'A', ?, " + usuarioAlta + ", 0)";

                if (m_Log.isDebugEnabled()) m_Log.debug(sql);

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

                if (m_Log.isDebugEnabled()) m_Log.debug("La relacion esta de baja");

                sql = "UPDATE T_DOT SET DOT_SIT = 'A' " +
                      "WHERE DOT_DOM = " + codDomicilio +
                      "AND DOT_TER = " + codTercero;

                if (m_Log.isDebugEnabled()) m_Log.debug(sql);

                st.executeUpdate(sql);

            // La relacion ya existe y esta de alta
            } else {
                if (m_Log.isDebugEnabled()) m_Log.debug("El domicilio ya esta relacionado con el tercero");
                actualizacionDomicillioRealizada = false;
            }

            cerrarStatement(st);
            commitTransaction(bd, con);
            
            return actualizacionDomicillioRealizada;

        } catch (Exception e) {
            rollBackTransaction(bd, con, e);
            throw e;
        } finally {
            cerrarResultSet(rs);
            cerrarStatement(st);
            devolverConexion(bd, con);
        }
    }
    
    
    
 
    /************************** nuevo ******************************/
     public int setTercero(TercerosValueObject terVO,String[] params,Connection con)throws TechnicalException{
        String sql = "";
        int max = 0;
        int ver = 1;
        AdaptadorSQLBD bd=null;        
        ResultSet rs=null;
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);
            
            state = con.createStatement();

            String nombre = terVO.getNombreCompleto();
            sql = "SELECT " +bd.funcionMatematica(bd.FUNCIONMATEMATICA_MAX,new String[]{(String)campos.getAtributo("idTerceroTER")}) + " AS MAXIMO FROM T_TER";
            rs = state.executeQuery(sql);
            rs.next();
            max = rs.getInt("MAXIMO");
            cerrarResultSet(rs);
            cerrarStatement(state);
            if (max<0)max=1;
            else max++;

            String codigoExterno="";
            if(((terVO.getIdentificador()).equals("0"))&&(!"".equals(terVO.getCodTerceroOrigen()))&&((terVO.getCodTerceroOrigen())!=null))
            {
              codigoExterno=  terVO.getCodTerceroOrigen();
            }
            else
            {
                codigoExterno="null";
            }


            m_Log.debug(new Integer(max));
            //max++;
            sql = "INSERT INTO T_TER("+
                    campos.getAtributo("idTerceroTER")+","+
                    campos.getAtributo("tipoDocTER")+","+
                    campos.getAtributo("documentoTER")+","+
                    campos.getAtributo("nombreTER")+","+
                    campos.getAtributo("apellido1TER")+","+
                    campos.getAtributo("pApellido1TER")+","+
                    campos.getAtributo("apellido2TER")+","+
                    campos.getAtributo("pApellido2TER")+","+
                    campos.getAtributo("nombreLargoTER")+","+
                    campos.getAtributo("normalizadoTER")+","+
                    campos.getAtributo("telefonoTER")+","+
                    campos.getAtributo("emailTER")+","+
                    campos.getAtributo("situacionTER")+","+
                    campos.getAtributo("versionTER")+","+
                    campos.getAtributo("fechaAltaTER")+","+
                    campos.getAtributo("usuarioAltaTER")+","+
                    campos.getAtributo("moduloAltaTER")+","+
                    campos.getAtributo("fechaBajaTER")+","+
                    campos.getAtributo("usuarioBajaTER")+
                    ",EXTERNAL_CODE"+
                    ") VALUES(" + max + "," + bd.addString(terVO.getTipoDocumento()) +
                    "," + bd.addString(terVO.getDocumento()) + "," + bd.addString(terVO.getNombre()) + "," +
                    bd.addString(terVO.getApellido1()) + "," + bd.addString(terVO.getPartApellido1()) + "," +
                    bd.addString(terVO.getApellido2()) + "," + bd.addString(terVO.getPartApellido2()) + "," +
                    bd.addString(nombre) + "," +
                    terVO.getNormalizado() + "," + bd.addString(terVO.getTelefono()) + "," +
                    bd.addString(terVO.getEmail()) + ",'" + terVO.getSituacion() + "'," +
                    ver +","+ bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null) + "," +
                    bd.addString(terVO.getUsuarioAlta()) +
                    "," + bd.addString(terVO.getModuloAlta()) + ",null,null,"+codigoExterno+")";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();
            state.executeUpdate(sql);
            cerrarStatement(state);
            sql = "INSERT INTO T_HTE("+
                    campos.getAtributo("idTerceroHTE")+","+
                    campos.getAtributo("versionHTE")+","+
                    campos.getAtributo("tipoDocHTE")+","+
                    campos.getAtributo("documentoHTE")+","+
                    campos.getAtributo("nombreHTE")+","+
                    campos.getAtributo("apellido1HTE")+","+
                    campos.getAtributo("pApellido1HTE")+","+
                    campos.getAtributo("apellido2HTE")+","+
                    campos.getAtributo("pApellido2HTE")+","+  
                    campos.getAtributo("nombreLargoHTE")+","+
                    campos.getAtributo("normalizadoHTE")+","+
                    campos.getAtributo("telefonoHTE")+","+
                    campos.getAtributo("emailHTE")+","+
                    campos.getAtributo("fechaOperHTE")+","+
                    campos.getAtributo("usuarioOperHTE")+","+
                    campos.getAtributo("moduloOperHTE")+
                     ",EXTERNAL_CODE"+ 
                    ") VALUES(" + max + ",'" + ver + "'," +
                    terVO.getTipoDocumento() + "," + bd.addString(terVO.getDocumento()) + "," +
                    bd.addString(terVO.getNombre()) + "," + bd.addString(terVO.getApellido1()) + "," +
                    bd.addString(terVO.getPartApellido1()) + "," + bd.addString(terVO.getApellido2()) + "," +
                    bd.addString(terVO.getPartApellido2()) + "," + bd.addString(nombre) + "," +
                    terVO.getNormalizado() + "," +
                    bd.addString(terVO.getTelefono()) + "," + bd.addString(terVO.getEmail()) + "," +
                    bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null) + "," +
                    bd.addString(terVO.getUsuarioAlta()) +
                    "," + bd.addString(terVO.getModuloAlta()) + ","+codigoExterno+")";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state = con.createStatement();
            state.executeUpdate(sql);
            cerrarStatement(state);
            
        }catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());            
            max=0;
        }finally{
            cerrarResultSet(rs);
            cerrarStatement(state);            
        }
        return max;
    }
    
 
     
     
   public Vector getTercero(CondicionesBusquedaTerceroVO condsBusqueda, String[] params,Connection con) throws TechnicalException{
        String sql;
        Vector<TercerosValueObject> resultado = new Vector<TercerosValueObject>();
        AdaptadorSQLBD adapt = null;
        
        ResultSet rs =null;
        Statement state = null;
        TransformacionAtributoSelect transformador;
        
        try{
            
            adapt = new AdaptadorSQLBD(params);
            
            transformador = new TransformacionAtributoSelect(adapt,con);
            int tipoDoc = condsBusqueda.getTipoDocumento();
            String docu = condsBusqueda.getDocumento();
            String name = condsBusqueda.getNombre();
            String apell1 = condsBusqueda.getApellido1();
            String apell2 = condsBusqueda.getApellido2();
            String telefono = condsBusqueda.getTelefono();
            String email = condsBusqueda.getEmail();
            String codPais = (String)campos.getAtributo("codigoPais");
            int codProvincia = condsBusqueda.getCodigoProvincia();
            int codMunicipio = condsBusqueda.getCodigoMunicipio();
            if (codProvincia == -1 && codMunicipio == -1) codPais = "";
            int codVia = condsBusqueda.getCodigoVia();
            String descVia = condsBusqueda.getNombreVia();
            int numDesde = condsBusqueda.getNumeroDesde();
            String letraDesde = condsBusqueda.getLetraDesde();
            int numHasta = condsBusqueda.getNumeroHasta();
            String letraHasta = condsBusqueda.getLetraHasta();
            String bloque = condsBusqueda.getBloque();
            String portal = condsBusqueda.getPortal();
            String escalera = condsBusqueda.getEscalera();
            String planta = condsBusqueda.getPlanta();
            String puerta = condsBusqueda.getPuerta();
            String codPostal = condsBusqueda.getCodPostal();
            String domicilio = condsBusqueda.getDomicilio();
            String lugar = condsBusqueda.getLugar();
            int codEsi = condsBusqueda.getCodigoEsi();
            int codEco = condsBusqueda.getCodigoEco();


            // PRIMERA PARTE DE LA UNION (DOMICILIOS NO NORMALIZADOS)
            String from = "TER_COD,TER_TID,TER_DOC,TER_NOM,TER_AP1,TER_PA1,TER_AP2,TER_PA2,TER_NOC,TER_NML,TER_TLF,TER_DCE,TER_SIT,TER_NVE,TER_FAL,TER_UAL,TER_APL,TER_FBJ,TER_UBJ,TER_DOM,T_DOM.*," +
                    campos.getAtributo("codTVI")+" AS CODTIPOVIA,"+
                    campos.getAtributo("descTVI")+" AS DESCTIPOVIA," +
                    campos.getAtributo("descESI") +" AS LUGAR," +
                    campos.getAtributo("idVIA")+" AS IDVIA,"+
                    campos.getAtributo("codVIA")+" AS CODVIA,"+
                    campos.getAtributo("descVIA")+" AS DESCVIA,"+
                    campos.getAtributo("domicilioDNN")+" AS DESCDMC," +
                    campos.getAtributo("numDesdeDNN")+" AS NUD,"+
                    campos.getAtributo("letraDesdeDNN")+" AS LED,"+
                    campos.getAtributo("numHastaDNN")+" AS NUH,"+
                    campos.getAtributo("letraHastaDNN")+" AS LEH," +
                    campos.getAtributo("bloqueDNN")+" AS BLQ,"+
                    campos.getAtributo("portalDNN")+" AS POR,"+
                    campos.getAtributo("escaleraDNN")+" AS ESC,"+
                    campos.getAtributo("plantaDNN")+" AS PLT," +
                    campos.getAtributo("puertaDNN")+" AS PTA,"+
                    campos.getAtributo("codPostalDNN")+" AS CPO,"+
                    campos.getAtributo("barriadaDNN")+" AS CAS,"+
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA")+","+campos.getAtributo("codECO") + "," +
                    campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");

            String where = "";
            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    where += condicion +" AND ";
                }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        where += condicion + " AND ";
                    }*/
                     where += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }
            if (name != null) {
                if (!"".equals(name.trim())) {
                   String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                   String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                where += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                where += "TER_DCE = '" + email + "' AND ";
            }

            String[] join = new String[38];

            join[0] = "T_TER";
            join[1] = "INNER";
            join[2] = "T_TID";
            join[3] = campos.getAtributo("tipoDocTER")+"="+campos.getAtributo("codTID");
            join[4] = "INNER";
            join[5] = "T_DOT";
            join[6] = campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT");
            join[7] = "INNER";
            join[8] = "T_DOM";
            join[9] = campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM");
            join[10] = "INNER";
            join[11] = "T_DNN";
            join[12] = campos.getAtributo("idDomicilioDOM")+"="+campos.getAtributo("idDomicilioDNN");
            join[13] = "LEFT";
            join[14] = "T_TOC";
            join[15] = campos.getAtributo("tipoOcupacionDOT")+"="+campos.getAtributo("codTOC");

            where = (codPais.equals("")) ? where
                    :where+campos.getAtributo("codPaisDNN")+"="+codPais+ " AND ";
            where = (codProvincia == -1) ? where
                    :where+campos.getAtributo("codProvDNN")+"="+codProvincia+ " AND ";
            where = (codMunicipio == -1) ? where
                    :where+campos.getAtributo("codMunDNN")+"="+codMunicipio+ " AND ";
            where = (numDesde == -1) ? where
                    :where+campos.getAtributo("numDesdeDNN")+"="+numDesde+ " AND ";
            where = (letraDesde == null || "".equals(letraDesde)) ? where
                    :where+campos.getAtributo("letraDesdeDNN")+"="+adapt.addString(letraDesde)+" AND ";
            where = (numHasta == -1) ? where
                    :where+campos.getAtributo("numHastaDNN")+"="+numHasta+ " AND ";
            where = (letraDesde == null || "".equals(letraHasta)) ? where
                    :where+campos.getAtributo("letraHastaDNN")+"="+adapt.addString(letraHasta)+" AND ";

            if (bloque != null && !bloque.equals("")) {
                where += "DNN_BLQ = '" + bloque + "' AND ";
            }

            if (portal != null && !portal.equals("")) {
                where += "DNN_POR = '" + portal + "' AND ";
            }

            if (escalera != null && !escalera.equals("")) {
                where += "DNN_ESC = '" + escalera + "' AND ";
            }

            if (planta != null && !planta.equals("")) {
                where += "DNN_PLT = '" + planta + "' AND ";
            }

            if (puerta != null && !puerta.equals("")) {
                where += "DNN_PTA = '" + puerta + "' AND ";
            }

            if (codPostal != null && !codPostal.equals("")) {
                where += "DNN_CPO = '" + codPostal + "' AND ";
            }

            if (domicilio != null && !domicilio.equals("")) {
                where += "DNN_DMC = '" + domicilio + "' AND ";
            }

            if (lugar != null && !lugar.equals("")) {
                where += "DNN_LUG = '" + lugar + "' AND ";
            }

            join[16] = "INNER";
            join[17] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
            join[18] = campos.getAtributo("codPaisDNN")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDNN")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDNN")+"="+campos.getAtributo("codMUN");
            join[19] = "INNER";
            join[20] = GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI";
            join[21] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI");
            join[22] = "INNER";
            join[23] = GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV";
            join[24] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV");
            join[25] = "LEFT";
            join[26] = "T_VIA";
            join[27] = campos.getAtributo("codViaDNN")+"="+campos.getAtributo("idVIA")+" AND "+
                    campos.getAtributo("codPaisViaDNN")+"="+campos.getAtributo("codPaisVIA")+" AND "+
                    campos.getAtributo("codProvViaDNN")+"="+campos.getAtributo("codProvVIA")+" AND "+
                    campos.getAtributo("codMunViaDNN")+"="+campos.getAtributo("codMunVIA");

            where = (codVia == -1) ? where:where+campos.getAtributo("codVIA")+"="+codVia+ " AND ";
            where = (descVia == null || "".equals(descVia)) ? where:where+campos.getAtributo("descVIA")+" LIKE '%"+TransformacionAtributoSelect.replace(descVia,"'","''")+ "%' AND ";
            where = (codEco == -1) ? where:where+campos.getAtributo("codECO")+"="+codEco+ " AND ";
            where = (codEsi == -1) ? where:where+campos.getAtributo("codESI")+"="+codEsi+ " AND ";

            join[28] = "LEFT";
            join[29] = "T_TVI";
            join[30] = campos.getAtributo("idTipoViaDNN")+"="+campos.getAtributo("codTVI");
            join[31] = "LEFT";
            join[32] = "T_ESI";
            join[33] = campos.getAtributo("codESIDNN")+"="+campos.getAtributo("codESI");
            join[34] = "LEFT";
            join[35] = "T_ECO";
            join[36] = campos.getAtributo("codECOESI")+"="+campos.getAtributo("codECO");
            join[37] = "false";

            where += campos.getAtributo("situacionTER")+"='A' AND "+
                    campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("normalizadoDOM")+"=2 ";

            sql = adapt.join(from,where,join);

            // SEGUNDA PARTE DE LA UNION (DOMICILIOS NORMALIZADOS)
            join = new String[41];

            from =  "TER_COD,TER_TID,TER_DOC,TER_NOM,TER_AP1,TER_PA1,TER_AP2,TER_PA2,TER_NOC,TER_NML,TER_TLF,TER_DCE,TER_SIT,TER_NVE,TER_FAL,TER_UAL,TER_APL,TER_FBJ,TER_UBJ,TER_DOM,T_DOM.*,"+
                    campos.getAtributo("codTVI")+","+
                    campos.getAtributo("descTVI")+"," +
                    campos.getAtributo("descESI")+"," +
                    campos.getAtributo("idVIA")+","+
                    campos.getAtributo("codVIA")+","+
                    campos.getAtributo("descVIA")+","+
                    campos.getAtributo("descVIA")+"," +
                    campos.getAtributo("numDesdeDSU")+","+
                    campos.getAtributo("letraDesdeDSU")+","+
                    campos.getAtributo("numHastaDSU")+","+
                    campos.getAtributo("letraHastaDSU")+"," +
                    campos.getAtributo("bloqueDSU")+","+
                    campos.getAtributo("portalDSU")+","+
                    campos.getAtributo("escaleraDPO")+","+
                    campos.getAtributo("plantaDPO")+"," +
                    campos.getAtributo("puertaDPO")+","+
                    campos.getAtributo("codPostalDSU")+","+
                    "("+  adapt.funcionCadena(adapt.FUNCIONCADENA_CONCAT,new String[]{(String) campos.getAtributo("descESI"),"' - '",(String) campos.getAtributo("descECO")}) +"), " +
                    campos.getAtributo("codTOC")+","+campos.getAtributo("descTOC")+"," +
                    campos.getAtributo("codPAI")+","+campos.getAtributo("descPAI")+","+
                    campos.getAtributo("codPRV")+","+campos.getAtributo("descPRV")+","+
                    campos.getAtributo("codMUN")+","+campos.getAtributo("descMUN")+","+
                    campos.getAtributo("codPaisVIA")+","+campos.getAtributo("codProvVIA")+","+
                    campos.getAtributo("codMunVIA")+
                    ","+campos.getAtributo("codECO") + "," + campos.getAtributo("codESI")+ "," + campos.getAtributo("descESI");

            where = "";

            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    where += condicion +" AND ";
                }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        where += condicion + " AND ";
                    }*/
                     where += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }

            if (name != null) {
                if (!"".equals(name.trim())) {
                    String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                    String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        where += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                where += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                where += "TER_DCE = '" + email + "' AND ";
            }

            where+= campos.getAtributo("situacionTER")+"='A' AND "+
                    campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("normalizadoDOM")+"=1 AND ";

            join[0] = "T_TER";
            join[1] = "INNER";
            join[2] = "T_TID";
            join[3] = campos.getAtributo("tipoDocTER")+"="+campos.getAtributo("codTID");
            join[4] = "INNER";
            join[5] = "T_DOT";
            join[6] = campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT");
            join[7] = "INNER";
            join[8] = "T_DOM";
            join[9] = campos.getAtributo("idDomicilioDOT")+"="+campos.getAtributo("idDomicilioDOM");
            join[10] = "INNER";
            join[11] = "T_DPO";
            join[12] = campos.getAtributo("idDomicilioDOM")+"="+campos.getAtributo("idDomicilioDPO");
            join[13] = "INNER";
            join[14] = "T_DSU";
            join[15] = campos.getAtributo("dirSueloDPO")+"="+campos.getAtributo("codDirSueloDSU");
            join[16] = "LEFT";
            join[17] = "T_TOC";
            join[18] = campos.getAtributo("tipoOcupacionDOT") + " = " + campos.getAtributo("codTOC");

            where = (codPais.equals("")) ? where
                    :where+campos.getAtributo("codPaisDSU")+"="+codPais+ " AND ";
            where = (codProvincia == -1) ? where
                    :where+campos.getAtributo("codProvDSU")+"="+codProvincia+ " AND ";
            where = (codMunicipio == -1) ? where
                    :where+campos.getAtributo("codMunDSU")+"="+codMunicipio+ " AND ";
            where = (numDesde == -1) ? where
                    :where+campos.getAtributo("numDesdeDSU")+"="+numDesde+ " AND ";
            where = (letraDesde ==  null || "".equals(letraDesde)) ? where
                    :where+campos.getAtributo("letraDesdeDSU")+"="+adapt.addString(letraDesde)+" AND ";
            where = (numHasta == -1) ? where
                    :where+campos.getAtributo("numHastaDSU")+"="+numHasta+ " AND ";
            where = (letraHasta == null || "".equals(letraHasta)) ? where
                    :where+campos.getAtributo("letraHastaDSU")+"="+adapt.addString(letraHasta)+" AND ";

            if (bloque != null && !bloque.equals("")) {
                where += "DSU_BLQ = '" + bloque + "' AND ";
            }

            if (portal != null && !portal.equals("")) {
                where += "DSU_POR = '" + portal + "' AND ";
            }

            if (lugar != null && !lugar.equals("")) {
                where += "DSU_LUG = '" + lugar + "' AND ";
            }

            if (codPostal != null && !codPostal.equals("")) {
                where += "DSU_CPO = '" + codPostal + "' AND ";
            }

            join[19] = "INNER";
            join[20] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
            join[21] = campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisMUN")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvMUN")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMUN");
            join[22] = "INNER";
            join[23] = GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI";
            join[24] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPAI");
            join[25] = "INNER";
            join[26] = GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV";
            join[27] = campos.getAtributo("codPaisMUN")+"="+campos.getAtributo("codPaisPRV")+" AND "+
                    campos.getAtributo("codProvMUN")+"="+campos.getAtributo("codPRV");
            join[28] = "INNER";
            join[29] = "T_VIA";
            join[30] = campos.getAtributo("codViaDSU")+"="+campos.getAtributo("idVIA")+" AND "+
                    campos.getAtributo("codPaisDSU")+"="+campos.getAtributo("codPaisVIA")+" AND "+
                    campos.getAtributo("codProvDSU")+"="+campos.getAtributo("codProvVIA")+" AND "+
                    campos.getAtributo("codMunDSU")+"="+campos.getAtributo("codMunVIA");
            where = (codVia == -1) ? where:where+campos.getAtributo("codVIA")+"="+codVia+ " AND ";
            where = (descVia == null || "".equals(descVia)) ? where:where+campos.getAtributo("descVIA")+" LIKE '%"+TransformacionAtributoSelect.replace(descVia,"'","''")+ "%' AND ";

            where = (codEco == -1) ? where: where+campos.getAtributo("codECO")+"="+codEco+ " AND ";
            where = (codEsi == -1) ? where: where+campos.getAtributo("codESI")+"="+codEsi+ " AND ";

            where += campos.getAtributo("situacionDSU")+"='A'";
            join[31] = "INNER";
            join[32] = "T_TVI";
            join[33] = campos.getAtributo("tipoVIA")+"="+campos.getAtributo("codTVI");
            join[34] = "LEFT";
            join[35] = "T_ESI";
            join[36] = campos.getAtributo("codigoESI")+"="+campos.getAtributo("codESI");
            join[37] = "LEFT";
            join[38] = "T_ECO";
            join[39] = campos.getAtributo("codECOESI")+"="+campos.getAtributo("codECO");
            join[40] =  "false";
            sql = sql + " UNION " + adapt.join(from,where,join);
            sql = sql + "ORDER BY TER_AP1, TER_COD";
            if(m_Log.isDebugEnabled()) m_Log.debug("CONSULTA DE BUSQUEDA DE TERCEROS"+sql);

            long tiempoInicio = System.currentTimeMillis();

            state = con.createStatement();
            
             if(consultaInsensitiva){
                String alter1=null;
                String alter2=null;
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    m_Log.debug("ALTER SESSION SET LINGUISTIC/GENERICBASELETTER");

                    state.executeQuery(alter1);
                    state.executeQuery(alter2);

                } 
            }
            
            rs = state.executeQuery(sql);
            long totalTiempo = System.currentTimeMillis() - tiempoInicio;
m_Log.debug("El tiempo de demora es :" + totalTiempo + " miliseg");
            int actual;
            int ultimo=-1;
            DomicilioSimpleValueObject nuevo;
            Vector<DomicilioSimpleValueObject> domicilios = null;
            TercerosValueObject terceroActual;
            TercerosValueObject terceroUltimo = null;

            DatosSuplementariosTerceroDAO datosSupDAO = DatosSuplementariosTerceroDAO.getInstance();
            
            // OBTENER RESULTADOS DE LA CONSULTA
            while(rs.next()){
                actual=rs.getInt((String)campos.getAtributo("idTerceroTER"));
                if (actual==ultimo){
                    String descdmc = rs.getString("DESCDMC");
                    if (descdmc== null) descdmc ="";
                    nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                            rs.getString((String)campos.getAtributo("codPAI")),
                            rs.getString((String)campos.getAtributo("codPRV")),
                            rs.getString((String)campos.getAtributo("codMUN")),
                            rs.getString((String)campos.getAtributo("descPAI")),
                            rs.getString((String)campos.getAtributo("descPRV")),
                            rs.getString((String)campos.getAtributo("descMUN")),
                            TransformacionAtributoSelect.replace(descdmc,"\"","\\\""),rs.getString("CPO"));
                    nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                    nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                    nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                    nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                    nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                    if(rs.getString("CODVIA")!=null){
                        nuevo.setDescVia(rs.getString("DESCVIA"));
                        nuevo.setCodigoVia(rs.getString("CODVIA"));
                        nuevo.setIdVia(rs.getString("IDVIA"));
                        if(rs.getString("CODVIA").equals("0")) {
                        }
                    }
                    nuevo.setNumDesde(rs.getString("NUD"));
                    nuevo.setLetraDesde(rs.getString("LED"));
                    nuevo.setNumHasta(rs.getString("NUH"));
                    nuevo.setLetraHasta(rs.getString("LEH"));
                    nuevo.setBloque(rs.getString("BLQ"));
                    nuevo.setPortal(rs.getString("POR"));
                    nuevo.setEscalera(rs.getString("ESC"));
                    nuevo.setPlanta(rs.getString("PLT"));
                    nuevo.setPuerta(rs.getString("PTA"));
                    nuevo.setBarriada(rs.getString("CAS"));
                    nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                    nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                    nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                    /* anadir ECOESI */
                    nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                    nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                    nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                    /* fin anadir ECOESI */
                    domicilios.add(nuevo);
                }else{
                    if (terceroUltimo!=null){
                        terceroUltimo.setDomicilios(domicilios);
                        resultado.add(terceroUltimo);
                    }
                    terceroActual = new TercerosValueObject(
                            String.valueOf(actual),
                            rs.getString((String)campos.getAtributo("versionTER")),
                            rs.getString((String)campos.getAtributo("tipoDocTER")),
                            "",
                            rs.getString((String)campos.getAtributo("documentoTER")),
                            rs.getString((String)campos.getAtributo("nombreTER")),
                            rs.getString((String)campos.getAtributo("apellido1TER")),
                            rs.getString((String)campos.getAtributo("pApellido1TER")),
                            rs.getString((String)campos.getAtributo("apellido2TER")),
                            rs.getString((String)campos.getAtributo("pApellido2TER")),
                            rs.getString((String)campos.getAtributo("normalizadoTER")),
                            rs.getString((String)campos.getAtributo("telefonoTER")),
                            rs.getString((String)campos.getAtributo("emailTER")),
                            rs.getString((String)campos.getAtributo("situacionTER")).charAt(0),
                            rs.getString((String)campos.getAtributo("fechaAltaTER")),
                            rs.getString((String)campos.getAtributo("usuarioAltaTER")),
                            rs.getString((String)campos.getAtributo("moduloAltaTER")),
                            rs.getString((String)campos.getAtributo("fechaBajaTER")),
                            rs.getString((String)campos.getAtributo("usuarioBajaTER")));

                    terceroActual.setSituacion(rs.getString((String)campos.getAtributo("situacionTER")).charAt(0));
                    terceroActual.setDomPrincipal(rs.getString("TER_DOM"));
                    domicilios = new Vector<DomicilioSimpleValueObject>();
                    if (rs.getInt((String)campos.getAtributo("idDomicilioDOM"))>0){
                        nuevo = new DomicilioSimpleValueObject(rs.getString((String)campos.getAtributo("idDomicilioDOM")),
                                rs.getString((String)campos.getAtributo("codPAI")),
                                rs.getString((String)campos.getAtributo("codPRV")),
                                rs.getString((String)campos.getAtributo("codMUN")),
                                rs.getString((String)campos.getAtributo("descPAI")),
                                rs.getString((String)campos.getAtributo("descPRV")),
                                rs.getString((String)campos.getAtributo("descMUN")),
                                TransformacionAtributoSelect.replace(rs.getString("DESCDMC"),"\"","\\\""),rs.getString("CPO"));
                        nuevo.setIdTipoVia(rs.getString("CODTIPOVIA"));
                        nuevo.setTipoVia(rs.getString("DESCTIPOVIA"));
                        nuevo.setIdPaisVia(rs.getString("VIA_PAI"));
                        nuevo.setIdProvinciaVia(rs.getString("VIA_PRV"));
                        nuevo.setIdMunicipioVia(rs.getString("VIA_MUN"));
                        if(rs.getString("CODVIA")!=null){
                            nuevo.setDescVia(rs.getString("DESCVIA"));
                            nuevo.setCodigoVia(rs.getString("CODVIA"));
                            nuevo.setIdVia(rs.getString("IDVIA"));
                            if(rs.getString("CODVIA").equals("0")) {
                            }
                        }
                        nuevo.setNumDesde(rs.getString("NUD"));
                        nuevo.setLetraDesde(rs.getString("LED"));
                        nuevo.setNumHasta(rs.getString("NUH"));
                        nuevo.setLetraHasta(rs.getString("LEH"));
                        nuevo.setBloque(rs.getString("BLQ"));
                        nuevo.setPortal(rs.getString("POR"));
                        nuevo.setEscalera(rs.getString("ESC"));
                        nuevo.setPlanta(rs.getString("PLT"));
                        nuevo.setPuerta(rs.getString("PTA"));
                        nuevo.setBarriada(rs.getString("CAS"));
                        nuevo.setCodTipoUso(rs.getString((String)campos.getAtributo("codTOC")));
                        nuevo.setDescTipoUso(rs.getString((String)campos.getAtributo("descTOC")));
                        nuevo.setNormalizado(rs.getString((String)campos.getAtributo("normalizadoDOM")));
                        /* anadir ECOESI */
                        nuevo.setCodECO(rs.getString((String)campos.getAtributo("codECO")));
                        nuevo.setCodESI(rs.getString((String)campos.getAtributo("codESI")));
                        nuevo.setDescESI(rs.getString((String)campos.getAtributo("descESI")));
                        /* fin anadir ECOESI */

                        domicilios.add(nuevo);
                    }
                    terceroUltimo=terceroActual;
                    ultimo=actual;
                }
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
            if (terceroUltimo!=null){
                terceroUltimo.setDomicilios(domicilios);
                resultado.add(terceroUltimo);
            }


            // COMPARACIÓN Q NO VA NINGÚN PARÁMETRO DE CONSULTA DE DOMICILIO

            m_Log.debug("PROVINCIA ................  "+codProvincia);
            m_Log.debug("MUNICIPIO ................  "+codMunicipio);
            m_Log.debug("NUM DESDE ................  "+numDesde);
            m_Log.debug("LETRA DESDE ..............  "+letraDesde);
            m_Log.debug("NUM HASTA ................  "+numHasta);
            m_Log.debug("LETRA HASTA...............  "+letraHasta);
            m_Log.debug("DESC VIA .................  "+descVia);
            m_Log.debug("COD VIA ..................  "+codVia);
            m_Log.debug("COD ECO ..................  "+codEco);
            m_Log.debug("COD ESI ..................  "+codEsi);

            if (codProvincia == -1 && codMunicipio == -1 && numDesde == -1 && (letraDesde == null || "".equals(letraDesde)) &&
                    numHasta == -1 && (letraHasta == null || "".equals(letraHasta)) && codVia == -1 && codEco == -1 &&
                    codEsi == -1) {

            // Terceros q no tienen domicilio SÓLO SE AÑADEN A LOS ANTERIORES cuando la consulta no tiene ningún parámetro
            sql = "SELECT T_TER.* FROM T_TER WHERE "+ campos.getAtributo("situacionTER")+"='A' AND ";

            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    sql += condicion +" AND ";
                }
            if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        sql += condicion + " AND ";
                    }*/
                    sql += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }
            if (name != null) {
                if (!"".equals(name.trim())) {
                    String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sql += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sql += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                   String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sql += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                sql += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                sql += "TER_DCE = '" + email + "' AND ";
            }

            int longitud =sql.length()-5;
            if(sql.endsWith(" AND "))
                sql = sql.substring(0,longitud);
            if(sql.endsWith(" WHERE "))
                sql = sql.substring(0,sql.length()-7);

            String sqlMinus = "";

            sqlMinus += "SELECT T_TER.* FROM T_TER,T_DOT WHERE ";
            if (tipoDoc != -1) {
                String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("tipoDocTER"), Integer.toString(tipoDoc));
                    sqlMinus += condicion +" AND ";
                }
             if (docu != null) {
                if (!"".equals(docu.trim())) {
                 /*   String condicion = transformador.construirCondicionWhereConOperadores((String)campos.getAtributo("documentoTER"), docu.trim(),false);
                    if (!"".equals(condicion)) {
                        sqlMinus += condicion + " AND ";
                    }*/
                    sqlMinus += campos.getAtributo("documentoTER")+"='"+docu.trim()+ "' AND ";
                }
            }
            if (name != null) {
                if (!"".equals(name.trim())) {
                    String nomb=(String)campos.getAtributo("nombreTER");
                    String condicion = transformador.construirCondicionWhereConOperadores(nomb, name.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sqlMinus += condicion + " AND ";
                    }
                }
            }
            if (apell1 != null) {
                if (!"".equals(apell1.trim())) {
                    String ap1=(String)campos.getAtributo("apellido1TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap1, apell1.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sqlMinus += condicion + " AND ";
                    }
                }
            }
            if (apell2 != null) {
                if (!"".equals(apell2.trim())) {
                    String ap2=(String)campos.getAtributo("apellido2TER");
                    String condicion = transformador.construirCondicionWhereConOperadores(ap2, apell2.trim(),false);
                    if (!"".equals(condicion)) {
                        consultaInsensitiva=true;
                        sqlMinus += condicion + " AND ";
                    }
                }
            }
            if (telefono != null && !telefono.equals("")) {
                sqlMinus += "TER_TLF = '" + telefono + "' AND ";
            }
            if (email != null && !email.equals("")) {
                sqlMinus += "TER_DCE = '" + email + "' AND ";
            }

            sqlMinus += campos.getAtributo("situacionDOT")+"='A' AND " +
                    campos.getAtributo("idTerceroTER")+"="+campos.getAtributo("idTerceroDOT");

            sql = sql + adapt.minus(sql,sqlMinus);

            if(m_Log.isDebugEnabled()) {
                m_Log.debug("No tienen domicilio: solo se añaden cuando en la consutla no va ningún parámetro");
                m_Log.debug(sql);
            }
            state = con.createStatement();
            
            if(consultaInsensitiva){
                String alter1=null;
                String alter2=null;
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    m_Log.debug("ALTER SESSION SET LINGUISTIC/GENERICBASELETTER");

                    state.executeQuery(alter1);
                    state.executeQuery(alter2);

                } 
            }
            
            rs = state.executeQuery(sql);
            TercerosValueObject tercero = null;
            
            while(rs.next()){
                String codTercero  = rs.getString((String)campos.getAtributo("idTerceroTER"));
                tercero = new TercerosValueObject(
                        codTercero,
                        rs.getString((String)campos.getAtributo("versionTER")),
                        rs.getString((String)campos.getAtributo("tipoDocTER")),
                        "",
                        rs.getString((String)campos.getAtributo("documentoTER")),
                        rs.getString((String)campos.getAtributo("nombreTER")),
                        rs.getString((String)campos.getAtributo("apellido1TER")),
                        rs.getString((String)campos.getAtributo("pApellido1TER")),
                        rs.getString((String)campos.getAtributo("apellido2TER")),
                        rs.getString((String)campos.getAtributo("pApellido2TER")),
                        rs.getString((String)campos.getAtributo("normalizadoTER")),
                        rs.getString((String)campos.getAtributo("telefonoTER")),
                        rs.getString((String)campos.getAtributo("emailTER")),
                        rs.getString((String)campos.getAtributo("situacionTER")).charAt(0),
                        rs.getString((String)campos.getAtributo("fechaAltaTER")),
                        rs.getString((String)campos.getAtributo("usuarioAltaTER")),
                        rs.getString((String)campos.getAtributo("moduloAltaTER")),
                        rs.getString((String)campos.getAtributo("fechaBajaTER")),
                        rs.getString((String)campos.getAtributo("usuarioBajaTER")));
                tercero.setSituacion(rs.getString((String)campos.getAtributo("situacionTER")).charAt(0));
                tercero.setDomicilios(new Vector());

                /*** SE RECUPERA LOS VALORES DE L TERCERO *****/
                resultado.add(tercero);
            }
            cerrarResultSet(rs);
            cerrarStatement(state);
         }


        /***************** PARA LOS TERCEROS RECUPERADOS, SE RECUPERAN LOS VALORES DE SUS CAMPOS SUPLEMENTARIOS ***********/

        String codOrganizacion = Integer.toString(condsBusqueda.getCodOrganizacion());
        try{                        
            Vector<EstructuraCampo> estructura = DatosSuplementariosTerceroDAO.getInstance().cargaEstructuraDatosSuplementariosTercero(Integer.toString(codMunicipio), adapt, con);
            Vector aux = new Vector();
            
            for(int i=0;resultado!=null && i<resultado.size();i++){
                TercerosValueObject tercero = (TercerosValueObject)resultado.get(i);
                //tercero.setValoresCamposSuplementarios(camposManager.cargaValoresDatosSuplementarios(codOrganizacion,tercero.getIdentificador(),estructura, params));
                tercero.setValoresCamposSuplementarios(DatosSuplementariosTerceroDAO.getInstance().cargaValoresDatosSuplementarios(codOrganizacion,tercero.getIdentificador(),estructura,con,params));                                
                aux.add(tercero);
            }
            
            resultado = null;
            resultado = aux;

        }catch(Exception e){
            e.printStackTrace();
            
            if(m_Log.isErrorEnabled()) m_Log.error("ERROR en getTercero():"  + e.getMessage());
        }
            /******************************************************************************************************************/

            // fin COMPARACIÓN Q NO VA NINGÚN PARÁMETRO DE CONSULTA DE DOMICILIO        
        } catch (Exception e) {
            if(m_Log.isErrorEnabled()) m_Log.error("ERROR en getTercero():"  + e.getMessage());
        }finally{
            
            try{
                if (consultaInsensitiva){
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                       String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                       String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                       m_Log.debug("ALTER SESSION SET BINARY/SPANISH");

                       state=con.createStatement();
                       state.executeQuery(alter1);
                       SigpGeneralOperations.closeStatement(state);
                       state=con.createStatement();
                       state.executeQuery(alter2);
                       SigpGeneralOperations.closeStatement(state);
                   } 
                }
            }catch (SQLException se){
                if(m_Log.isErrorEnabled()) m_Log.error(se.getMessage());
            }
            
            cerrarResultSet(rs);
            cerrarStatement(state);        
        }
                
        m_Log.debug(" ==================> getTercero() devolviendo : "  + resultado);
        return resultado;
    }

   
   
   
   
    public boolean altaDomicilioTercero(int codTercero, int codDomicilio, String usuario, Connection con)throws Exception {

        String sql;
        PreparedStatement ps = null;               
        Statement st = null;
        ResultSet rs = null;
        boolean actualizacionDomicillioRealizada = true;
        if (m_Log.isDebugEnabled()) m_Log.debug("TercerosDAO.altaDomicilioTercero, tercero =" +
                codTercero + ", domicilio=" + codDomicilio);

        try {
            
            
            // Se comprueba si ya existe la relacion
            sql = "SELECT DOT_SIT FROM T_DOT " +
                  "WHERE DOT_DOM = " + codDomicilio + " AND DOT_TER = " + codTercero;

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);

            String situacion = null;
            if (rs.next()) {
                situacion = rs.getString(1);
            }

            rs.close();

            // Caso de que no exista la relacion
            if (situacion == null) {

                if (m_Log.isDebugEnabled()) m_Log.debug("No existe relacion");
                int usuarioAlta = Integer.parseInt(usuario);
                sql = "INSERT INTO T_DOT(DOT_DOM, DOT_TER, DOT_TOC, DOT_SIT, " +
                                        "DOT_FEC, DOT_USU, DOT_DPA) " +
                      "VALUES (?, ?, ?, 'A', ?, " + usuarioAlta + ", 0)";

                if (m_Log.isDebugEnabled()) m_Log.debug(sql);

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

                if (m_Log.isDebugEnabled()) m_Log.debug("La relacion esta de baja");

                sql = "UPDATE T_DOT SET DOT_SIT = 'A' " +
                      "WHERE DOT_DOM = " + codDomicilio +
                      "AND DOT_TER = " + codTercero;

                if (m_Log.isDebugEnabled()) m_Log.debug(sql);

                st.executeUpdate(sql);

            // La relacion ya existe y esta de alta
            } else {
                if (m_Log.isDebugEnabled()) m_Log.debug("El domicilio ya esta relacionado con el tercero");
                actualizacionDomicillioRealizada = false;
            }

            cerrarStatement(st);            
            return actualizacionDomicillioRealizada;

        } catch (Exception e) {            
            throw e;
        } finally {
            cerrarResultSet(rs);
            cerrarStatement(st);            
        }
    }
    
 
    
     public int updateTercero(TercerosValueObject terVO,Connection con,String[] params)throws TechnicalException{
        String sql = "";
        int ver = Integer.parseInt(terVO.getVersion())+1;
        int id = 0;
        AdaptadorSQLBD bd=null;        
        Statement state=null;
        try{
            bd = new AdaptadorSQLBD(params);            
            state = con.createStatement();

            m_Log.debug("============> TercerosDAO.updateTercero id: " + terVO.getIdentificador());
            String nombre = terVO.getNombreCompleto();
            id = Integer.parseInt(terVO.getIdentificador());
           
            
           
            sql = "UPDATE T_TER SET TER_NVE=" + ver + ",TER_TID=" + terVO.getTipoDocumento() + ",TER_DOC='" + terVO.getDocumento() + "',TER_NOM='" + terVO.getNombre() + "'," 
                + "TER_AP1='" + terVO.getApellido1() + "',TER_PA1='" + terVO.getPartApellido1() + "'," +
                "TER_AP2='" + terVO.getApellido2() + "',TER_PA2='" + terVO.getPartApellido2() + "'," +
                "TER_NOC='" + nombre + "',TER_TLF='" + terVO.getTelefono() + "',TER_DCE='" + terVO.getEmail() + "',TER_DOM=" +                    
                terVO.getDomPrincipal();
            sql += " WHERE TER_COD=" + terVO.getIdentificador();
           
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state.executeUpdate(sql);
            cerrarStatement(state);

            sql = "INSERT INTO T_HTE("+ 
                    campos.getAtributo("idTerceroHTE")+","+
                    campos.getAtributo("versionHTE")+","+
                    campos.getAtributo("tipoDocHTE")+","+
                    campos.getAtributo("documentoHTE")+","+
                    campos.getAtributo("nombreHTE")+","+
                    campos.getAtributo("apellido1HTE")+","+
                    campos.getAtributo("pApellido1HTE")+","+
                    campos.getAtributo("apellido2HTE")+","+
                    campos.getAtributo("pApellido2HTE")+","+
                    campos.getAtributo("nombreLargoHTE")+","+
                    campos.getAtributo("normalizadoHTE")+","+
                    campos.getAtributo("telefonoHTE")+","+
                    campos.getAtributo("emailHTE")+","+
                    campos.getAtributo("fechaOperHTE")+","+
                    campos.getAtributo("usuarioOperHTE")+","+
                    campos.getAtributo("moduloOperHTE")+
                    ") VALUES(" + id + ",'" + ver + "'," +
                    terVO.getTipoDocumento() + "," + bd.addString(terVO.getDocumento()) + "," +
                    bd.addString(terVO.getNombre()) + "," + bd.addString(terVO.getApellido1()) + "," +
                    bd.addString(terVO.getPartApellido1()) + "," + bd.addString(terVO.getApellido2()) + "," +
                    bd.addString(terVO.getPartApellido2()) + "," + bd.addString(nombre) + "," +
                    terVO.getNormalizado() + "," +
                    bd.addString(terVO.getTelefono()) + "," + bd.addString(terVO.getEmail()) + "," +
                    bd.funcionFecha(bd.FUNCIONFECHA_SYSDATE, null)+","+
                    bd.addString(terVO.getUsuarioAlta()) +
                    "," + bd.addString(terVO.getModuloAlta()) + ")";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            state =  con.createStatement();
            state.executeUpdate(sql);
            cerrarStatement(state);            
        }catch (Exception e) {
            e.printStackTrace();
            if(m_Log.isDebugEnabled()) m_Log.debug(" ERROR ====================>: " + e.getMessage());            
            id=0;
        }finally{
            cerrarStatement(state);            
        }
        return id;
    }
     
     
   
    
}
