package es.altia.flexiaWS.tramitacion.bd.util;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.persistance.GeneralValueObject;

import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: marcos.baltar
 * Date: 11-ene-2006
 * Time: 17:52:21
 * To change this template use File | Settings | File Templates.
 */
public class Campos {

    private static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    public static final GeneralValueObject campos;


    static
    {

        campos =  new GeneralValueObject();


        // TABLA E_TRP
        campos.setAtributo("codMunicipioTRP",m_ConfigTechnical.getString("SQL.E_TRP.codMunicipio"));
        campos.setAtributo("ejercicioTRP",m_ConfigTechnical.getString("SQL.E_TRP.ejercicio"));
        campos.setAtributo("numeroExpedienteTRP",m_ConfigTechnical.getString("SQL.E_TRP.numeroExpediente"));
        campos.setAtributo("codProcedimientoTRP",m_ConfigTechnical.getString("SQL.E_TRP.codProcedimiento"));
        campos.setAtributo("codTramiteTRP",m_ConfigTechnical.getString("SQL.E_TRP.codTramite"));
        campos.setAtributo("codCondicionEntradaTRP",m_ConfigTechnical.getString("SQL.E_TRP.codCondicionEntrada"));
        campos.setAtributo("codTramiteCondicionEntradaTRP",m_ConfigTechnical.getString("SQL.E_TRP.codTramiteCondicionEntrada"));
        campos.setAtributo("estadoTRP",m_ConfigTechnical.getString("SQL.E_TRP.estado"));
        campos.setAtributo("fechaInicioTRP",m_ConfigTechnical.getString("SQL.E_TRP.fechaInicio"));
        campos.setAtributo("fechaFinTRP",m_ConfigTechnical.getString("SQL.E_TRP.fechaFin"));
        campos.setAtributo("codUnidadTramitadoraTRP",m_ConfigTechnical.getString("SQL.E_TRP.codUnidadTramitadora"));



        // TABLA E_CRO
        campos.setAtributo("codMunicipioCRO",m_ConfigTechnical.getString("SQL.E_CRO.codMunicipio"));
        campos.setAtributo("codProcedimientoCRO",m_ConfigTechnical.getString("SQL.E_CRO.codProcedimiento"));
        campos.setAtributo("anoCRO",m_ConfigTechnical.getString("SQL.E_CRO.ano"));
        campos.setAtributo("numeroCRO",m_ConfigTechnical.getString("SQL.E_CRO.numero"));
        campos.setAtributo("codTramiteCRO",m_ConfigTechnical.getString("SQL.E_CRO.codTramite"));
        campos.setAtributo("fechaInicioCRO",m_ConfigTechnical.getString("SQL.E_CRO.fechaInicio"));
        campos.setAtributo("fechaFinCRO",m_ConfigTechnical.getString("SQL.E_CRO.fechaFin"));
        campos.setAtributo("codUnidadTramitadoraCRO",m_ConfigTechnical.getString("SQL.E_CRO.codUnidadTramitadora"));
        campos.setAtributo("codUsuarioCRO",m_ConfigTechnical.getString("SQL.E_CRO.codUsuario"));
        campos.setAtributo("fechaInicioPlazoCRO",m_ConfigTechnical.getString("SQL.E_CRO.fechaInicioPlazo"));
        campos.setAtributo("fechaLimiteCRO",m_ConfigTechnical.getString("SQL.E_CRO.fechaLimite"));
        campos.setAtributo("fechaFinPlazoCRO",m_ConfigTechnical.getString("SQL.E_CRO.fechaFinPlazo"));
        campos.setAtributo("ocurrenciaCRO",m_ConfigTechnical.getString("SQL.E_CRO.ocurrencia"));
        campos.setAtributo("resolucionCRO",m_ConfigTechnical.getString("SQL.E_CRO.resolucion"));
        campos.setAtributo("observacionesCRO",m_ConfigTechnical.getString("SQL.E_CRO.observaciones"));
        campos.setAtributo("usuarioFinalizacionCRO",m_ConfigTechnical.getString("SQL.E_CRO.usuarioFinalizacion"));

       // TABLA A_UOU
        campos.setAtributo("unidadOrgUOU",m_ConfigTechnical.getString("SQL.A_UOU.unidadOrg"));
        campos.setAtributo("usuarioUOU",m_ConfigTechnical.getString("SQL.A_UOU.usuario"));
        campos.setAtributo("organizacionUOU",m_ConfigTechnical.getString("SQL.A_UOU.organizacion"));
        campos.setAtributo("entidadUOU",m_ConfigTechnical.getString("SQL.A_UOU.entidad"));

        // TABLA A_UOT
        campos.setAtributo("codUnidadOrganicaUOT",m_ConfigTechnical.getString("SQL.A_UOT.codUnidadOrganica"));
        campos.setAtributo("codUnidadTramitadoraUOT",m_ConfigTechnical.getString("SQL.A_UOT.codUnidadTramitadora"));

        // TABLA E_TRA
        campos.setAtributo("codMunicipioTRA",m_ConfigTechnical.getString("SQL.E_TRA.codMunicipio"));
        campos.setAtributo("codProcedimientoTRA",m_ConfigTechnical.getString("SQL.E_TRA.codProcedimiento"));
        campos.setAtributo("codTramiteTRA",m_ConfigTechnical.getString("SQL.E_TRA.codTramite"));
        campos.setAtributo("codTramiteUsuarioTRA",m_ConfigTechnical.getString("SQL.E_TRA.codTramiteUsuario"));
        campos.setAtributo("visibleInternetTRA",m_ConfigTechnical.getString("SQL.E_TRA.visibleInternet"));
        campos.setAtributo("unidadInicioTRA",m_ConfigTechnical.getString("SQL.E_TRA.unidadInicio"));
        campos.setAtributo("unidadTramiteTRA",m_ConfigTechnical.getString("SQL.E_TRA.unidadTramite"));
        campos.setAtributo("plazoTRA",m_ConfigTechnical.getString("SQL.E_TRA.plazo"));
        campos.setAtributo("unidadesPlazoTRA",m_ConfigTechnical.getString("SQL.E_TRA.unidadesPlazo"));
        campos.setAtributo("codAreaTRA",m_ConfigTechnical.getString("SQL.E_TRA.codArea"));
        campos.setAtributo("ocurrenciasTRA",m_ConfigTechnical.getString("SQL.E_TRA.ocurrencias"));
        campos.setAtributo("clasificacionTRA",m_ConfigTechnical.getString("SQL.E_TRA.clasificacion"));
        campos.setAtributo("fechaBajaTRA",m_ConfigTechnical.getString("SQL.E_TRA.fechaBaja"));
        campos.setAtributo("tramitePreguntaTRA",m_ConfigTechnical.getString("SQL.E_TRA.tramitePregunta"));
        campos.setAtributo("instruccionesTRA",m_ConfigTechnical.getString("SQL.E_TRA.instrucciones"));


        // TABLA E_EXP
        campos.setAtributo("codMunicipioEXP",m_ConfigTechnical.getString("SQL.E_EXP.codMunicipio"));
        campos.setAtributo("codProcedimientoEXP",m_ConfigTechnical.getString("SQL.E_EXP.codProcedimiento"));
        campos.setAtributo("anoEXP",m_ConfigTechnical.getString("SQL.E_EXP.ano"));
        campos.setAtributo("numeroEXP",m_ConfigTechnical.getString("SQL.E_EXP.numero"));
        campos.setAtributo("fechaInicioEXP",m_ConfigTechnical.getString("SQL.E_EXP.fechaInicio"));
        campos.setAtributo("fechaFinEXP",m_ConfigTechnical.getString("SQL.E_EXP.fechaFin"));
        campos.setAtributo("terceroEXP",m_ConfigTechnical.getString("SQL.E_EXP.tercero"));
        campos.setAtributo("versionEXP",m_ConfigTechnical.getString("SQL.E_EXP.version"));
        campos.setAtributo("estadoEXP",m_ConfigTechnical.getString("SQL.E_EXP.estado"));
        campos.setAtributo("usuarioEXP",m_ConfigTechnical.getString("SQL.E_EXP.usuario"));
        campos.setAtributo("uorEXP",m_ConfigTechnical.getString("SQL.E_EXP.uor"));
        campos.setAtributo("localizacionEXP",m_ConfigTechnical.getString("SQL.E_EXP.localizacion"));
        campos.setAtributo("codLocalizacionEXP",m_ConfigTechnical.getString("SQL.E_EXP.codLocalizacion"));
        campos.setAtributo("observacionesEXP",m_ConfigTechnical.getString("SQL.E_EXP.observaciones"));
        campos.setAtributo("asuntoEXP",m_ConfigTechnical.getString("SQL.E_EXP.asunto"));
        campos.setAtributo("codTramiteUltCerradoEXP",m_ConfigTechnical.getString("SQL.E_EXP.codTramiteUltCerrado"));
        campos.setAtributo("ocuTramiteUltCerradoEXP",m_ConfigTechnical.getString("SQL.E_EXP.ocuTramiteUltCerrado"));



        // TABLA E_TNUT
        campos.setAtributo("codMunicipioTNUT",m_ConfigTechnical.getString("SQL.E_TNUT.codMunicipio"));
        campos.setAtributo("codProcedimientoTNUT",m_ConfigTechnical.getString("SQL.E_TNUT.codProcedimiento"));
        campos.setAtributo("ejercicioTNUT",m_ConfigTechnical.getString("SQL.E_TNUT.ejercicio"));
        campos.setAtributo("numeroExpedienteTNUT",m_ConfigTechnical.getString("SQL.E_TNUT.numeroExpediente"));
        campos.setAtributo("codTramiteTNUT",m_ConfigTechnical.getString("SQL.E_TNUT.codTramite"));
        campos.setAtributo("ocurrenciaTNUT",m_ConfigTechnical.getString("SQL.E_TNUT.ocurrencia"));
        campos.setAtributo("codCampoTNUT",m_ConfigTechnical.getString("SQL.E_TNUT.codCampo"));
        campos.setAtributo("valorTNUT",m_ConfigTechnical.getString("SQL.E_TNUT.valor"));
        
        // TABLA E_TNUCT
        campos.setAtributo("codMunicipioTNUCT",m_ConfigTechnical.getString("SQL.E_TNUCT.codMunicipio"));
        campos.setAtributo("codProcedimientoTNUCT",m_ConfigTechnical.getString("SQL.E_TNUCT.codProcedimiento"));
        campos.setAtributo("ejercicioTNUCT",m_ConfigTechnical.getString("SQL.E_TNUCT.ejercicio"));
        campos.setAtributo("numeroExpedienteTNUCT",m_ConfigTechnical.getString("SQL.E_TNUCT.numeroExpediente"));
        campos.setAtributo("codTramiteTNUCT",m_ConfigTechnical.getString("SQL.E_TNUCT.codTramite"));
        campos.setAtributo("ocurrenciaTNUCT",m_ConfigTechnical.getString("SQL.E_TNUCT.ocurrencia"));
        campos.setAtributo("codCampoTNUCT",m_ConfigTechnical.getString("SQL.E_TNUCT.codCampo"));
        campos.setAtributo("valorTNUCT",m_ConfigTechnical.getString("SQL.E_TNUCT.valor"));

        // TABLA E_TXTT
        campos.setAtributo("codMunicipioTXTT",m_ConfigTechnical.getString("SQL.E_TXTT.codMunicipio"));
        campos.setAtributo("codProcedimientoTXTT",m_ConfigTechnical.getString("SQL.E_TXTT.codProcedimiento"));
        campos.setAtributo("ejercicioTXTT",m_ConfigTechnical.getString("SQL.E_TXTT.ejercicio"));
        campos.setAtributo("numeroExpedienteTXTT",m_ConfigTechnical.getString("SQL.E_TXTT.numeroExpediente"));
        campos.setAtributo("codTramiteTXTT",m_ConfigTechnical.getString("SQL.E_TXTT.codTramite"));
        campos.setAtributo("ocurrenciaTXTT",m_ConfigTechnical.getString("SQL.E_TXTT.ocurrencia"));
        campos.setAtributo("codCampoTXTT",m_ConfigTechnical.getString("SQL.E_TXTT.codCampo"));
        campos.setAtributo("valorTXTT",m_ConfigTechnical.getString("SQL.E_TXTT.valor"));


        // TABLA E_TFET
        campos.setAtributo("codMunicipioTFET",m_ConfigTechnical.getString("SQL.E_TFET.codMunicipio"));
        campos.setAtributo("codProcedimientoTFET",m_ConfigTechnical.getString("SQL.E_TFET.codProcedimiento"));
        campos.setAtributo("ejercicioTFET",m_ConfigTechnical.getString("SQL.E_TFET.ejercicio"));
        campos.setAtributo("numeroExpedienteTFET",m_ConfigTechnical.getString("SQL.E_TFET.numeroExpediente"));
        campos.setAtributo("codTramiteTFET",m_ConfigTechnical.getString("SQL.E_TFET.codTramite"));
        campos.setAtributo("ocurrenciaTFET",m_ConfigTechnical.getString("SQL.E_TFET.ocurrencia"));
        campos.setAtributo("codCampoTFET",m_ConfigTechnical.getString("SQL.E_TFET.codCampo"));
        campos.setAtributo("valorTFET",m_ConfigTechnical.getString("SQL.E_TFET.valor"));
        
        // TABLA E_TFECT
        campos.setAtributo("codMunicipioTFECT",m_ConfigTechnical.getString("SQL.E_TFECT.codMunicipio"));
        campos.setAtributo("codProcedimientoTFECT",m_ConfigTechnical.getString("SQL.E_TFECT.codProcedimiento"));
        campos.setAtributo("ejercicioTFECT",m_ConfigTechnical.getString("SQL.E_TFECT.ejercicio"));
        campos.setAtributo("numeroExpedienteTFECT",m_ConfigTechnical.getString("SQL.E_TFECT.numeroExpediente"));
        campos.setAtributo("codTramiteTFECT",m_ConfigTechnical.getString("SQL.E_TFECT.codTramite"));
        campos.setAtributo("ocurrenciaTFECT",m_ConfigTechnical.getString("SQL.E_TFECT.ocurrencia"));
        campos.setAtributo("codCampoTFECT",m_ConfigTechnical.getString("SQL.E_TFECT.codCampo"));
        campos.setAtributo("valorTFECT",m_ConfigTechnical.getString("SQL.E_TFECT.valor"));
        
        // TABLA E_TTLT
        campos.setAtributo("codMunicipioTTLT",m_ConfigTechnical.getString("SQL.E_TTLT.codMunicipio"));
        campos.setAtributo("codProcedimientoTTLT",m_ConfigTechnical.getString("SQL.E_TTLT.codProcedimiento"));
        campos.setAtributo("ejercicioTTLT",m_ConfigTechnical.getString("SQL.E_TTLT.ejercicio"));
        campos.setAtributo("numeroExpedienteTTLT",m_ConfigTechnical.getString("SQL.E_TTLT.numeroExpediente"));
        campos.setAtributo("codTramiteTTLT",m_ConfigTechnical.getString("SQL.E_TTLT.codTramite"));
        campos.setAtributo("ocurrenciaTTLT",m_ConfigTechnical.getString("SQL.E_TTLT.ocurrencia"));
        campos.setAtributo("codCampoTTLT",m_ConfigTechnical.getString("SQL.E_TTLT.codCampo"));
        campos.setAtributo("valorTTLT",m_ConfigTechnical.getString("SQL.E_TTLT.valor"));

        // TABLA E_ENT
        campos.setAtributo("codMunicipioENT", m_ConfigTechnical.getString("SQL.E_ENT.codMunicipio"));
        campos.setAtributo("codProcedimientoENT", m_ConfigTechnical.getString("SQL.E_ENT.codProcedimiento"));
        campos.setAtributo("codTramiteENT", m_ConfigTechnical.getString("SQL.E_ENT.codTramite"));
        campos.setAtributo("codCondicionENT", m_ConfigTechnical.getString("SQL.E_ENT.codCondicion"));
        campos.setAtributo("codTramiteCondENT", m_ConfigTechnical.getString("SQL.E_ENT.codTramiteCond"));
        campos.setAtributo("estadoTramiteCondENT", m_ConfigTechnical.getString("SQL.E_ENT.estadoTramiteCond"));

        // TABLA E_TML
        campos.setAtributo("codMunicipioTML", m_ConfigTechnical.getString("SQL.E_TML.codMunicipio"));
        campos.setAtributo("codProcedimientoTML", m_ConfigTechnical.getString("SQL.E_TML.codProcedimiento"));
        campos.setAtributo("codTramiteTML", m_ConfigTechnical.getString("SQL.E_TML.codTramite"));
        campos.setAtributo("codCampoMLTML", m_ConfigTechnical.getString("SQL.E_TML.codCampoML"));
        campos.setAtributo("idiomaTML", m_ConfigTechnical.getString("SQL.E_TML.idioma"));
        campos.setAtributo("valorTML", m_ConfigTechnical.getString("SQL.E_TML.valor"));


        // TABLA A_UOR
        campos.setAtributo("codUOR", m_ConfigTechnical.getString("SQL.A_UOR.codigo"));
        campos.setAtributo("unidadPadreUOR", m_ConfigTechnical.getString("SQL.A_UOR.padre"));
        campos.setAtributo("nombreUOR", m_ConfigTechnical.getString("SQL.A_UOR.nombre"));
        campos.setAtributo("tipoUOR", m_ConfigTechnical.getString("SQL.A_UOR.tipo"));
        campos.setAtributo("fechaAltaUOR", m_ConfigTechnical.getString("SQL.A_UOR.fechaAlta"));
        campos.setAtributo("fechaBajaUOR", m_ConfigTechnical.getString("SQL.A_UOR.fechaBaja"));
        campos.setAtributo("estadoUOR", m_ConfigTechnical.getString("SQL.A_UOR.estado"));
        campos.setAtributo("codigoVisibleUOR", m_ConfigTechnical.getString("SQL.A_UOR.codigoVisible"));
        campos.setAtributo("emailUOR", m_ConfigTechnical.getString("SQL.A_UOR.email"));


         // TABLA A_UTML
        campos.setAtributo("codUTML", m_ConfigTechnical.getString("SQL.A_UTML.codUnidadTramitadora"));
        campos.setAtributo("campoUTML", m_ConfigTechnical.getString("SQL.A_UTML.codCampoML"));
        campos.setAtributo("idiomaUTML", m_ConfigTechnical.getString("SQL.A_UTML.idioma"));
        campos.setAtributo("valorUTML", m_ConfigTechnical.getString("SQL.A_UTML.valor"));

        // TABLA E_TXT      
        campos.setAtributo("codMunicipioTXT",m_ConfigTechnical.getString("SQL.E_TXT.codMunicipio"));
        campos.setAtributo("ejercicioTXT",m_ConfigTechnical.getString("SQL.E_TXT.ejercicio"));
        campos.setAtributo("numeroExpedienteTXT",m_ConfigTechnical.getString("SQL.E_TXT.numeroExpediente"));
        campos.setAtributo("codCampoTXT",m_ConfigTechnical.getString("SQL.E_TXT.codCampo"));
        campos.setAtributo("valorTXT",m_ConfigTechnical.getString("SQL.E_TXT.valor"));

        // TABLA E_TFE
        campos.setAtributo("codMunicipioTFE",m_ConfigTechnical.getString("SQL.E_TFE.codMunicipio"));
        campos.setAtributo("ejercicioTFE",m_ConfigTechnical.getString("SQL.E_TFE.ejercicio"));
        campos.setAtributo("numeroExpedienteTFE",m_ConfigTechnical.getString("SQL.E_TFE.numeroExpediente"));
        campos.setAtributo("codCampoTFE",m_ConfigTechnical.getString("SQL.E_TFE.codCampo"));
        campos.setAtributo("valorTFE",m_ConfigTechnical.getString("SQL.E_TFE.valor"));
        
        // TABLA E_TFEC
        campos.setAtributo("codMunicipioTFEC",m_ConfigTechnical.getString("SQL.E_TFEC.codMunicipio"));
        campos.setAtributo("ejercicioTFEC",m_ConfigTechnical.getString("SQL.E_TFEC.ejercicio"));
        campos.setAtributo("numeroExpedienteTFEC",m_ConfigTechnical.getString("SQL.E_TFEC.numeroExpediente"));
        campos.setAtributo("codCampoTFEC",m_ConfigTechnical.getString("SQL.E_TFEC.codCampo"));
        campos.setAtributo("valorTFEC",m_ConfigTechnical.getString("SQL.E_TFEC.valor"));
        
        // TABLA E_TNU
        campos.setAtributo("codMunicipioTNU",m_ConfigTechnical.getString("SQL.E_TNU.codMunicipio"));
        campos.setAtributo("ejercicioTNU",m_ConfigTechnical.getString("SQL.E_TNU.ejercicio"));
        campos.setAtributo("numeroExpedienteTNU",m_ConfigTechnical.getString("SQL.E_TNU.numeroExpediente"));
        campos.setAtributo("codCampoTNU",m_ConfigTechnical.getString("SQL.E_TNU.codCampo"));
        campos.setAtributo("valorTNU",m_ConfigTechnical.getString("SQL.E_TNU.valor"));
        
        // TABLA E_TNUC
        campos.setAtributo("codMunicipioTNUC",m_ConfigTechnical.getString("SQL.E_TNUC.codMunicipio"));
        campos.setAtributo("ejercicioTNUC",m_ConfigTechnical.getString("SQL.E_TNUC.ejercicio"));
        campos.setAtributo("numeroExpedienteTNUC",m_ConfigTechnical.getString("SQL.E_TNUC.numeroExpediente"));
        campos.setAtributo("codCampoTNUC",m_ConfigTechnical.getString("SQL.E_TNUC.codCampo"));
        campos.setAtributo("valorTNUC",m_ConfigTechnical.getString("SQL.E_TNUC.valor"));
        
        // TABLA E_TTL
        campos.setAtributo("codMunicipioTTL",m_ConfigTechnical.getString("SQL.E_TTL.codMunicipio"));
        campos.setAtributo("ejercicioTTL",m_ConfigTechnical.getString("SQL.E_TTL.ejercicio"));
        campos.setAtributo("numeroExpedienteTTL",m_ConfigTechnical.getString("SQL.E_TTL.numeroExpediente"));
        campos.setAtributo("codCampoTTL",m_ConfigTechnical.getString("SQL.E_TTL.codCampo"));
        campos.setAtributo("valorTTL",m_ConfigTechnical.getString("SQL.E_TTL.valor"));


    }

   

}





