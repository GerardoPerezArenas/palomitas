/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.notificacion.persistence;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;

import java.sql.*;
import java.util.ArrayList;


import es.altia.flexia.notificacion.vo.*;
import java.util.ResourceBundle;


public class AutorizadoNotificacionDAO {


   private static AutorizadoNotificacionDAO instance =	null;
    protected   static Config m_CommonProperties; // Para el fichero de contantes
    protected static Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log =
            LogFactory.getLog(AutorizadoNotificacionDAO.class.getName());

    protected static Config m_ct; // Para el fichero de configuracion técnico
    protected static GeneralValueObject campos = null;
    protected static String idPais;
    protected static String idProvincia;
    protected static String idMunicipio;
    protected static String pais;
    protected static String provincia;
    protected static String municipio;



     protected AutorizadoNotificacionDAO() {
                m_CommonProperties = ConfigServiceHelper.getConfig("common");
		// Queremos usar el	fichero de configuración technical
		m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
		// Queremos tener acceso a los mensajes de error localizados
		m_ConfigError	= ConfigServiceHelper.getConfig("error");

        m_ct = ConfigServiceHelper.getConfig("techserver");

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

    public static AutorizadoNotificacionDAO getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (AutorizadoNotificacionDAO.class) {
            if (instance == null) {
                instance = new AutorizadoNotificacionDAO();
            }

        }
        return instance;
    }


    //Da de alta un registro en la tabla AUTORIZADO_NOTIFICACION
    public boolean insertarAutorizado(AutorizadoNotificacionVO autorizado,Connection con) throws TechnicalException {

    int resultado = 0;    
    Statement st = null;
    String sql = "";
    
    try{
      
      st = con.createStatement();
      String numeroExpediente=autorizado.getNumeroExpediente();
      int ejercicio=autorizado.getEjercicio();
      int codMunicipio=autorizado.getCodigoMunicipio();
      int codTercero=autorizado.getCodigoTercero();
      int verTercero=autorizado.getNumeroVersionTercero();
      int codigo=autorizado.getCodigoNotificacion();

      sql = "INSERT INTO AUTORIZADO_NOTIFICACION(CODIGO_NOTIFICACION,COD_MUNICIPIO, EJERCICIO," +
              "NUM_EXPEDIENTE,COD_TERCERO,VER_TERCERO)" +
              " VALUES ("+codigo+","+codMunicipio+","+ejercicio+
              ",'"+numeroExpediente+"',"+codTercero+","+verTercero+
              ")";

      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      resultado = st.executeUpdate(sql);

    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
        return false;
    }finally{
        try{
            if (st!=null) st.close();            
        }catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
            return false;
        }
    }

    if (resultado==0) return false;
    else return true;

   }

public boolean eliminarAutorizado(AutorizadoNotificacionVO autorizado,String[] params) throws TechnicalException {

      AdaptadorSQLBD obd = null;
      Connection con = null;
      Statement st = null;
      String sql = "";

      try{

      obd = new AdaptadorSQLBD(params);
      con = obd.getConnection();
      obd.inicioTransaccion(con);
      st = con.createStatement();

      int codNotificacion = autorizado.getCodigoNotificacion();
      int codigoMunicipio=autorizado.getCodigoMunicipio();
      int ejercicio=autorizado.getEjercicio();
      String numeroExpediente =autorizado.getNumeroExpediente();
      int codTercero=autorizado.getCodigoTercero();
      int verTercero=autorizado.getNumeroVersionTercero();
      
      sql ="DELETE FROM AUTORIZADO_NOTIFICACION " +
          " WHERE CODIGO_NOTIFICACION = " + codNotificacion + " AND COD_MUNICIPIO="+codigoMunicipio+" AND EJERCICIO = " + ejercicio + " AND NUM_EXPEDIENTE = '" + numeroExpediente + "' AND " +
          " COD_TERCERO = '" + codTercero + "' AND VER_TERCERO= " + verTercero;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);

      st.executeUpdate(sql);
      obd.finTransaccion(con);


      }catch (Exception e){
      try{
            obd.rollBack(con);
      }catch(Exception ex){
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());

      }
      e.printStackTrace();
      if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
      return false;

    }finally{
      try{
            if (st!=null) st.close();
            obd.devolverConexion(con);
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
            return false;
        }
    }

    return true;
  }




public ArrayList<AutorizadoNotificacionVO> getDetalleInteresadosExpediente(String numExpediente,int codigoNotificacion, boolean expedienteHistorico,Connection con) throws TechnicalException {
     
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList<AutorizadoNotificacionVO> arrayAux=new ArrayList<AutorizadoNotificacionVO>();
        ArrayList<AutorizadoNotificacionVO> arrayRetorno=new ArrayList<AutorizadoNotificacionVO>();

        try{

           String sql = "";
           
           if(!expedienteHistorico) {
                /** Se recuperan los datos de los terceros que están asociados a la notificación. Estos terceros son o pueden haber sido interesados del expediente */
                sql= "SELECT HTE_TER,HTE_NVR,HTE_NOC,HTE_NOM,HTE_DOC,HTE_DCE,HTE_AP1,HTE_AP2,HTE_TLF " +
                     "FROM T_HTE,AUTORIZADO_NOTIFICACION " +
                     "WHERE HTE_TER=COD_TERCERO AND HTE_NVR = VER_TERCERO AND CODIGO_NOTIFICACION=?" +
                     "AND NUM_EXPEDIENTE=?";
           } else {
                /** Se recuperan los datos de los terceros que están asociados a la notificación. Estos terceros son o pueden haber sido interesados del expediente */
                sql= "SELECT HTE_TER,HTE_NVR,HTE_NOC,HTE_NOM,HTE_DOC,HTE_DCE,HTE_AP1,HTE_AP2,HTE_TLF " +
                     "FROM T_HTE,HIST_AUTORIZADO_NOTIFICACION " +
                     "WHERE HTE_TER=COD_TERCERO AND HTE_NVR = VER_TERCERO AND CODIGO_NOTIFICACION=?" +
                     "AND NUM_EXPEDIENTE=?";               
           }
           if(m_Log.isDebugEnabled()) m_Log.debug(sql);

           ps = con.prepareStatement(sql);
           ps.setInt(1,codigoNotificacion);
           ps.setString(2,numExpediente);
           
           rs = ps.executeQuery();
           
           int i=0;
           while(rs.next()){
               
                AutorizadoNotificacionVO autorizadoNotifAux=new AutorizadoNotificacionVO();
                autorizadoNotifAux.setCodigoTercero(rs.getInt("HTE_TER"));
                autorizadoNotifAux.setNumeroVersionTercero(rs.getInt("HTE_NVR"));
                autorizadoNotifAux.setNombre(rs.getString("HTE_NOM"));
                autorizadoNotifAux.setNif(rs.getString("HTE_DOC"));
                autorizadoNotifAux.setEmail(rs.getString("HTE_DCE"));
                autorizadoNotifAux.setNumeroExpediente(numExpediente);                                
                autorizadoNotifAux.setApellido1(rs.getString("HTE_AP1"));
                autorizadoNotifAux.setApellido2(rs.getString("HTE_AP2"));

                String nombreCompleto = "";
//                if(autorizadoNotifAux.getApellido2()==null || "".equals(autorizadoNotifAux.getApellido2()) || "null".equalsIgnoreCase(autorizadoNotifAux.getApellido2())){
//                    nombreCompleto = autorizadoNotifAux.getApellido1() + " " + autorizadoNotifAux.getNombre();
//                }else
//                    nombreCompleto = autorizadoNotifAux.getApellido1() + " " + autorizadoNotifAux.getApellido2() + ", " + autorizadoNotifAux.getNombre();
                if(autorizadoNotifAux.getApellido1()!=null && !"".equals(autorizadoNotifAux.getApellido1()) && !"null".equalsIgnoreCase(autorizadoNotifAux.getApellido1())){
                    nombreCompleto=autorizadoNotifAux.getApellido1();
                }
                if(autorizadoNotifAux.getApellido2()!=null && !"".equals(autorizadoNotifAux.getApellido2()) && !"null".equalsIgnoreCase(autorizadoNotifAux.getApellido2())){
                    nombreCompleto += (!nombreCompleto.isEmpty() ? " " + autorizadoNotifAux.getApellido2(): autorizadoNotifAux.getApellido2());
                }
                if(autorizadoNotifAux.getNombre()!=null && !"".equals(autorizadoNotifAux.getNombre()) && !"null".equalsIgnoreCase(autorizadoNotifAux.getNombre())){
                    nombreCompleto += (!nombreCompleto.isEmpty() ? ", " + autorizadoNotifAux.getNombre() : autorizadoNotifAux.getNombre());
                }
               

                autorizadoNotifAux.setNombreCompleto(nombreCompleto);
                arrayAux.add(i, autorizadoNotifAux);
                i++;
            }

            rs.close();

            for(int j=0;j<arrayAux.size();j++)
            {
               AutorizadoNotificacionVO autorizadoNotifVORetorno=new AutorizadoNotificacionVO();
               autorizadoNotifVORetorno=arrayAux.get(j);
               if(estaAutorizadoEnNotificacion(autorizadoNotifVORetorno,codigoNotificacion,con))
               {
                   autorizadoNotifVORetorno.setSeleccionado("SI");
                   autorizadoNotifVORetorno.setCodigoNotificacion(codigoNotificacion);
               }

               arrayRetorno.add(autorizadoNotifVORetorno);

            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(ps!=null) ps.close();                
                if(rs!=null) rs.close();                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return arrayRetorno;

} 









//Recupera todos aquellos interesados de un determinado expediente que admiten notificaciones electrónicas. Además comprueba si alguno de estos interesados está asociado a una determinada notificación.
public ArrayList<AutorizadoNotificacionVO> getInteresadosExpediente(String numExpediente,int codigoNotificacion, String gestor,Connection con) throws TechnicalException {

        ResultSet rs = null;
        Statement st = null;
        ArrayList<AutorizadoNotificacionVO> arrayAux=new ArrayList<AutorizadoNotificacionVO>();
        ArrayList<AutorizadoNotificacionVO> arrayRetorno=new ArrayList<AutorizadoNotificacionVO>();

        try{
           String sqlQuery = "SELECT EXT_ROL,EXT_DOT,ROL_DES,ROL_PDE,HTE_TID,TID_DES,HTE_TER,HTE_NVR,HTE_DOC,HTE_NOM,HTE_AP1,HTE_AP2,HTE_TLF,DNN_PAI,HTE_DCE,HTE_NOC," +
				      "PAI_NOM,DNN_PRV,	PRV_COD,PRV_NOM,DNN_MUN,MUN_COD,MUN_NOM,TVI_DES,VIA_COD,VIA_NOM,DNN_DMC,DNN_NUD,DNN_LED,DNN_NUH," +
					  "DNN_LEH,DNN_BLQ,DNN_POR,DNN_ESC,DNN_PLT,DNN_PTA,DNN_CPO, EXT_MUN,EXT_EJE " +
					  "FROM E_EXT, E_ROL, T_HTE, T_DNN, T_TVI, T_VIA, T_TID, " + GlobalNames.ESQUEMA_GENERICO + "T_PAI, " +
					  GlobalNames.ESQUEMA_GENERICO + "T_PRV, " + GlobalNames.ESQUEMA_GENERICO + "T_MUN " +
					  "WHERE EXT_MUN=ROL_MUN " +
					  "AND EXT_PRO=ROL_PRO " +
					  "AND EXT_ROL= ROL_COD  " +
					  "AND EXT_TER=HTE_TER " +
					  "AND EXT_NVR=HTE_NVR " +
					  "AND EXT_DOT=DNN_DOM ";

                if("ORACLE".equalsIgnoreCase(gestor)){
	                sqlQuery = sqlQuery + "AND DNN_TVI=TVI_COD(+) " +
	                "AND DNN_VIA=VIA_COD(+) " +
	                "AND DNN_PAI=VIA_PAI(+) " +
	                "AND DNN_PRV=VIA_PRV(+) " +
	                "AND DNN_MUN=VIA_MUN(+) ";
                }else{
                	sqlQuery = sqlQuery + "AND DNN_TVI*=TVI_COD " +
	                "AND DNN_VIA*=VIA_COD " +
	                "AND DNN_PAI*=VIA_PAI " +
	                "AND DNN_PRV*=VIA_PRV " +
	                "AND DNN_MUN*=VIA_MUN ";
                }

	            sqlQuery = sqlQuery +
                "AND HTE_TID=TID_COD " +
                "AND DNN_PAI=PAI_COD " +
                "AND DNN_PRV=PRV_COD " +
                "AND DNN_PAI=PRV_PAI " +
                "AND DNN_MUN=MUN_COD " +
                "AND DNN_PRV=MUN_PRV " +
                "AND DNN_PAI=MUN_PAI " +
                "AND DNN_MUN=MUN_COD " +
                "AND EXT_NUM='"  + numExpediente + "' " +
                "AND EXT_NOTIFICACION_ELECTRONICA='1' " + 
                "ORDER BY ROL_PDE DESC, EXT_ROL ASC";

           if(m_Log.isDebugEnabled()) m_Log.debug(sqlQuery);

            st = con.createStatement();
            rs = st.executeQuery(sqlQuery);

            int i=0;
            ResourceBundle bundle = ResourceBundle.getBundle("notificaciones");
            String codPaisRT = bundle.getString("COUNTRY");
            while(rs.next()){
                AutorizadoNotificacionVO autorizadoNotifAux=new AutorizadoNotificacionVO();

                autorizadoNotifAux.setCodigoTercero(rs.getInt("HTE_TER"));
                autorizadoNotifAux.setNumeroVersionTercero(rs.getInt("HTE_NVR"));
                autorizadoNotifAux.setNombre(rs.getString("HTE_NOM"));

                autorizadoNotifAux.setNif(rs.getString("HTE_DOC"));
                autorizadoNotifAux.setTipoDocumento(rs.getString("HTE_TID"));
                autorizadoNotifAux.setEmail(rs.getString("HTE_DCE"));
                autorizadoNotifAux.setNumeroExpediente(numExpediente);
                autorizadoNotifAux.setCodigoMunicipio(rs.getInt("EXT_MUN"));
                autorizadoNotifAux.setEjercicio(rs.getInt("EXT_EJE"));
                autorizadoNotifAux.setSeleccionado("NO");
                autorizadoNotifAux.setApellido1(rs.getString("HTE_AP1"));
                autorizadoNotifAux.setApellido2(rs.getString("HTE_AP2"));
                autorizadoNotifAux.setTelefono(rs.getString("HTE_TLF"));

                String nombreCompleto = "";

//                if(autorizadoNotifAux.getApellido2()==null || "".equals(autorizadoNotifAux.getApellido2()) || "null".equalsIgnoreCase(autorizadoNotifAux.getApellido2())){
//                    nombreCompleto = autorizadoNotifAux.getApellido1() + " " + autorizadoNotifAux.getNombre();
//                }else
//                    nombreCompleto = autorizadoNotifAux.getApellido1() + " " + autorizadoNotifAux.getApellido2() + ", " + autorizadoNotifAux.getNombre();

                if(autorizadoNotifAux.getApellido1()!=null && !"".equals(autorizadoNotifAux.getApellido1()) && !"null".equalsIgnoreCase(autorizadoNotifAux.getApellido1())){
                    nombreCompleto=autorizadoNotifAux.getApellido1();
                }
                if(autorizadoNotifAux.getApellido2()!=null && !"".equals(autorizadoNotifAux.getApellido2()) && !"null".equalsIgnoreCase(autorizadoNotifAux.getApellido2())){
                    nombreCompleto += (!nombreCompleto.isEmpty() ? " " + autorizadoNotifAux.getApellido2(): autorizadoNotifAux.getApellido2());
                }
                if(autorizadoNotifAux.getNombre()!=null && !"".equals(autorizadoNotifAux.getNombre()) && !"null".equalsIgnoreCase(autorizadoNotifAux.getNombre())){
                    nombreCompleto += (!nombreCompleto.isEmpty() ? ", " + autorizadoNotifAux.getNombre() : autorizadoNotifAux.getNombre());
                }

                autorizadoNotifAux.setNombreCompleto(nombreCompleto);

                autorizadoNotifAux.setCodPais(codPaisRT);
                autorizadoNotifAux.setCodProvincia(rs.getString("PRV_COD"));
                autorizadoNotifAux.setDescProvincia(rs.getString("PRV_NOM"));
                autorizadoNotifAux.setDescMunicipio(rs.getString("MUN_NOM"));
                autorizadoNotifAux.setCodVia(rs.getString("VIA_COD"));
                autorizadoNotifAux.setDescVia(rs.getString("VIA_NOM"));
                String tipoVia = rs.getString("TVI_DES");
                String nombreVia = rs.getString("VIA_NOM");
                String numDesde = rs.getString("DNN_NUD");
                String letraDesde = rs.getString("DNN_LED");
                String numHasta = rs.getString("DNN_NUH");
                String letraHasta = rs.getString("DNN_LEH");
                String bloque = rs.getString("DNN_BLQ");
                String portal = rs.getString("DNN_POR");
                String escalera = rs.getString("DNN_ESC");
                String planta = rs.getString("DNN_PLT");
                String puerta = rs.getString("DNN_PTA");
                autorizadoNotifAux.setDireccion(construirDireccion(tipoVia, nombreVia, numDesde, letraDesde, numHasta,
                        letraHasta, bloque, portal, escalera, planta, puerta));
                autorizadoNotifAux.setCodPostal(rs.getString("DNN_CPO"));
                autorizadoNotifAux.setCodDomicilio(rs.getInt("EXT_DOT"));
                autorizadoNotifAux.setRol(rs.getInt("EXT_ROL"));
                
                arrayAux.add(autorizadoNotifAux);
            }

            rs.close();

            for(int j=0;j<arrayAux.size();j++)
            {
               AutorizadoNotificacionVO autorizadoNotifVORetorno=new AutorizadoNotificacionVO();
               autorizadoNotifVORetorno=arrayAux.get(j);
               if(estaAutorizadoEnNotificacion(autorizadoNotifVORetorno,codigoNotificacion,con))
               {
                   autorizadoNotifVORetorno.setSeleccionado("SI");
                   autorizadoNotifVORetorno.setCodigoNotificacion(codigoNotificacion);
               }

               arrayRetorno.add(autorizadoNotifVORetorno);

            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return arrayRetorno;
}



    private String construirDireccion(String tipoVia, String nombreVia, String numDesde, String letraDesde,
                                      String numHasta, String letraHasta, String bloque, String portal, String escalera,
                                      String planta, String puerta) {

        StringBuffer dirBuffer = new StringBuffer();
        if (tipoVia != null && !"".equals(tipoVia.trim())) dirBuffer.append(tipoVia).append(" ");
        dirBuffer.append(nombreVia).append(" ");
        if (numDesde != null && !numDesde.trim().equals("")) dirBuffer.append(numDesde).append(" ");
        if (letraDesde != null && !letraDesde.trim().equals("")) dirBuffer.append(letraDesde).append(" ");
        if ((numHasta != null && !numHasta.trim().equals("")) || (letraHasta != null && !letraHasta.trim().equals(""))) {
            dirBuffer.append(" - ");
            if (numHasta != null && !numHasta.trim().equals("")) dirBuffer.append(numHasta).append(" ");
            if (letraHasta != null && !letraHasta.trim().equals("")) dirBuffer.append(letraHasta).append(" ");
        }
        if (bloque != null && !bloque.trim().equals("")) dirBuffer.append("BLQ. ").append(bloque).append(" ");
        if (portal != null && !portal.trim().equals("")) dirBuffer.append("POR. ").append(portal).append(" ");
        if (escalera != null && !escalera.trim().equals("")) dirBuffer.append("ESC. ").append(escalera).append(" ");
        if (planta != null && !planta.trim().equals("")) dirBuffer.append(planta).append("º ");
        if (puerta != null && !puerta.trim().equals("")) dirBuffer.append(puerta).append(" ");

        return dirBuffer.toString().trim();

    }








//Comprueba si un interesado de un expediente está asociado a una notificación
private boolean estaAutorizadoEnNotificacion(AutorizadoNotificacionVO autorizado, int codNotificacion, Connection con) throws TechnicalException {

    boolean retorno=false;
    ResultSet rs = null;
    Statement st = null;

    try{
        int codigoMunicipio=autorizado.getCodigoMunicipio();
        int ejercicio=autorizado.getEjercicio();
        String numeroExpediente =autorizado.getNumeroExpediente();
        int codTercero=autorizado.getCodigoTercero();
        int verTercero=autorizado.getNumeroVersionTercero();



        String sql = "SELECT CODIGO_NOTIFICACION FROM AUTORIZADO_NOTIFICACION "+
                " WHERE CODIGO_NOTIFICACION = " + codNotificacion + " AND COD_MUNICIPIO="+codigoMunicipio+" AND EJERCICIO = " + ejercicio + " AND NUM_EXPEDIENTE = '" + numeroExpediente + "'" +
        "  AND COD_TERCERO= " + codTercero+" AND VER_TERCERO="+verTercero;

        if(m_Log.isDebugEnabled()) m_Log.debug(sql);

        st = con.createStatement();
        rs = st.executeQuery(sql);

        if(rs.next()){
            retorno=true;
        }

    }catch(Exception e){
        e.printStackTrace();
    }finally{
        try{
            if(st!=null) st.close();
            if(rs!=null) rs.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    return retorno;
}


    /**
     * Comprueba si un determinado tercero está como autorizado en una notificación electrónica
     * @param autorizado: Objeto de la clase AutorizadoNotificacionVO con los datos del interesado
     * @param codNotificacion: Código de la notificación
     * @param expedienteHistorico: True si el expediente está en el histórico, y false en caso contrario
     * @param con: Conexión a la BBDD
     * @return true si el tercero está autorizado a recibir la notificación o false en caso contrario
     * @throws TechnicalException 
     */
    private boolean estaAutorizadoEnNotificacion(AutorizadoNotificacionVO autorizado,int codNotificacion,boolean expedienteHistorico,Connection con) throws TechnicalException {
        boolean retorno=false;
        ResultSet rs = null;
        PreparedStatement ps = null;

        try{
            int codigoMunicipio=autorizado.getCodigoMunicipio();
            int ejercicio=autorizado.getEjercicio();
            String numeroExpediente =autorizado.getNumeroExpediente();
            int codTercero=autorizado.getCodigoTercero();
            int verTercero=autorizado.getNumeroVersionTercero();
            String sql = "";
            
            if(!expedienteHistorico) {
                sql = "SELECT CODIGO_NOTIFICACION FROM AUTORIZADO_NOTIFICACION "+
                      "WHERE CODIGO_NOTIFICACION=? AND COD_MUNICIPIO=? AND EJERCICIO=? AND NUM_EXPEDIENTE=? " +
                      "AND COD_TERCERO=? AND VER_TERCERO=?";
            } else {
                sql = "SELECT CODIGO_NOTIFICACION FROM HIST_AUTORIZADO_NOTIFICACION "+
                      "WHERE CODIGO_NOTIFICACION=? AND COD_MUNICIPIO=? AND EJERCICIO=? AND NUM_EXPEDIENTE=? " +
                      "AND COD_TERCERO=? AND VER_TERCERO=?";                
            }

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            ps.setInt(1,codNotificacion);
            ps.setInt(2,codigoMunicipio);
            ps.setInt(3,ejercicio);
            ps.setString(4,numeroExpediente);
            ps.setInt(5,codTercero);
            ps.setInt(6,verTercero);
            rs = ps.executeQuery();

            if(rs.next()){
                retorno=true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return retorno;
    }



   //Comprueba si hay interesados asociados a un determinado expediente que admiten notificaciones electrónicas
    public boolean existenInteresadosNotificacionElectronica (NotificacionVO notificacion,String[] params) throws TechnicalException {

        AdaptadorSQLBD obd = null;
        Connection con = null;
        boolean retorno=false;
        ResultSet rs = null;
        Statement st = null;

       try{

            obd = new AdaptadorSQLBD(params);
            con = obd.getConnection();

            String numeroExpediente =notificacion.getNumExpediente();

            String sql = "SELECT EXT_TER FROM E_EXT WHERE EXT_NUM='"+numeroExpediente+"' AND EXT_NOTIFICACION_ELECTRONICA='1'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);


            if(rs.next()){

            retorno=true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                obd.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return retorno;
    }
    
    
        
    
    /**
     * Actualiza los interesados que van a recibir la notificación electrónica
     * @param codigo: Código de la notificación
     * @param codMunicipìo: Código del municipio
     * @param autorizados: ArrayList<AutorizadoNotificacionVO>
     * @param con: Conexión a la BBDD
     * @return Un boolean 
     */
    public boolean actualizarAutorizado(int codigo,int codMunicipio,String numeroExpediente,ArrayList<AutorizadoNotificacionVO> autorizados,Connection con) throws TechnicalException {

        boolean exito = false;
        Statement st = null;        
        String sql = "";
    
        try{

            sql = "DELETE FROM AUTORIZADO_NOTIFICACION WHERE CODIGO_NOTIFICACION=" + codigo + " AND COD_MUNICIPIO=" + codMunicipio + " AND NUM_EXPEDIENTE='" + numeroExpediente + "'";
            m_Log.debug(sql);
            st = con.createStatement();
            int rowsDeleted = st.executeUpdate(sql);
            
            int contador = 0;
            for(int i=0;autorizados!=null && i<autorizados.size();i++){
                
                AutorizadoNotificacionVO autorizado = autorizados.get(i);
                if(autorizado.getSeleccionado().equals("SI")){
                
                    int ejercicio=autorizado.getEjercicio();
                    int codTercero=autorizado.getCodigoTercero();
                    int verTercero=autorizado.getNumeroVersionTercero();

                    m_Log.debug(" AutorizadoNotificacionDAO.actualizarAutorizado() rowsDeleted: " + rowsDeleted);
                    sql = "INSERT INTO AUTORIZADO_NOTIFICACION(CODIGO_NOTIFICACION,COD_MUNICIPIO, EJERCICIO," +
                            "NUM_EXPEDIENTE,COD_TERCERO,VER_TERCERO)" +
                            " VALUES ("+codigo+","+codMunicipio+","+ejercicio+
                            ",'"+numeroExpediente+"',"+codTercero+","+verTercero+")";

                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    int resultado = st.executeUpdate(sql);
                    if(resultado==1) contador++;
                }
            }
            
            exito = true;

        }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
            exito = false;
        }finally{
            
            try{
                if (st!=null) st.close();            
            }catch(Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());                
            }
        }
        return exito;
    }



}
