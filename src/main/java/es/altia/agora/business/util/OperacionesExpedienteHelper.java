/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.util;

import es.altia.agora.business.sge.ConsultaExpedientesValueObject;
import es.altia.agora.business.sge.OperacionExpedienteVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.agora.technical.EstructuraCampoAgrupado;
import es.altia.eni.conversoreni.StringUtils;
import es.altia.util.commons.DateOperations;
import java.text.SimpleDateFormat;
import java.util.Vector;

/**
 *
 * @author adrian.freixeiro
 */
public class OperacionesExpedienteHelper {

    protected void OperacionesExpedienteHelper() {
    }
        
    /*
     * Genera descripción con los datos del alta de un expediente
     * @param tercero Objeto que contiene información del expediente y del trámite inicial
     * @return String descripcionOperacion
     */
    public static String generarDescripcionAltaExpediente(GeneralValueObject infoExpTram, String fechaOper) {
        
        String codMunicipio = (String) infoExpTram.getAtributo("codMunicipio");
        String codProcedimiento = (String) infoExpTram.getAtributo("codProcedimiento");
        String ejercicio = (String) infoExpTram.getAtributo("ejercicio");
        String numero = (String) infoExpTram.getAtributo("numero");
        String usuario = (String) infoExpTram.getAtributo("usuario");
        String nomUsuario = (String) infoExpTram.getAtributo("nomUsuario");
        String descUOR = (String) infoExpTram.getAtributo("descUOR");
        String codUOR = (String) infoExpTram.getAtributo("codUOR");
        String asunto = (String) infoExpTram.getAtributo("asunto");
        String observaciones = (String) infoExpTram.getAtributo("observaciones");
        String localizacion = (String) infoExpTram.getAtributo("localizacion");
        String codLocalizacion = (String) infoExpTram.getAtributo("codLocalizacion");
        String tipoAlta = (String) infoExpTram.getAtributo("tipoAlta");
        String origenAlta =  "--";
        
        if (ConstantesDatos.TIPO_ALTA_EXP_NORMAL.equals(tipoAlta)) {
            origenAlta = "{eMovExpAltaNormal}";
        } else if (ConstantesDatos.TIPO_ALTA_EXP_ASIENTO.equals(tipoAlta)) {
            origenAlta = "{eMovExpAltaAsiento}";
        } else if (ConstantesDatos.TIPO_ALTA_EXP_COPIA.equals(tipoAlta)) {
            origenAlta = "{eMovExpAltaCopia}";
        } else if (ConstantesDatos.TIPO_ALTA_EXP_ASOCIADO.equals(tipoAlta)) {
            origenAlta = "{eMovExpAltaAsociado}";
        } else if (ConstantesDatos.TIPO_ALTA_EXP_WEBSERVICE_FLEXIA.equals(tipoAlta)) {
            origenAlta = "{eMovExpAltaWebservice}";
        }
        
        String codTramite = (String) infoExpTram.getAtributo("codTramite");
        String codTercero = (String) infoExpTram.getAtributo("codTercero");
        
        String descripcionOperacion = cab1("eMovExpAltaExp") + 
            linha("eMovExpNumExp",numero) +
            linha("eMovExpCodMun",codMunicipio) +
            linha("eMovExpCodProc",codProcedimiento) +
            linha("eMovExpEjercicio",ejercicio) +
            linha("eMovExpNomUOR",descUOR) + 
            linha("eMovExpCodUOR",codUOR) + 
            linha("eMovExpAsunto",asunto) + 
            linha("eMovExpObservac",observaciones) + 
            linha("eMovExpCodTramIni",codTramite) +
            linha("eMovExpLocalizac",localizacion) + 
            linha("eMovExpCodLocal",codLocalizacion) +
            linha("eMovExpTipoAlta",origenAlta);
        
        if (tipoAlta.equals(ConstantesDatos.TIPO_ALTA_EXP_COPIA) || 
                tipoAlta.equals(ConstantesDatos.TIPO_ALTA_EXP_ASOCIADO)){
            String numExpOriginal = (String) infoExpTram.getAtributo("numExpedienteOriginal");
            descripcionOperacion += linha("eMovExpNumExpOr",numExpOriginal);
        }
        
        if (tipoAlta.equals(ConstantesDatos.TIPO_ALTA_EXP_ASIENTO)){
            String procAsiento = (String) infoExpTram.getAtributo("procedimientoAsiento");
            String munProcAsiento = (String) infoExpTram.getAtributo("munProcedimiento");
            String tipoAsiento = (String) infoExpTram.getAtributo("tipoAsiento");
            String anoAsiento = (String) infoExpTram.getAtributo("ejercicioAsiento");
            String numAsiento = (String) infoExpTram.getAtributo("numeroAsiento");
            String uorAsiento = (String) infoExpTram.getAtributo("codUnidadRegistro");
            String depAsiento = (String) infoExpTram.getAtributo("codDepartamento");

            descripcionOperacion += linha("eMovExpProcAsiento",procAsiento) +
                linha("eMovExpMunAsiento",munProcAsiento) +
                linha("eMovExpTipoAsiento",tipoAsiento) +
                linha("eMovExpAnoAsiento",anoAsiento) +
                linha("eMovExpNumAsiento",numAsiento) +
                linha("eMovExpCodUORAsiento",uorAsiento) +
                linha("eMovExpCodDepAsiento",depAsiento);
        }
        
        if (informado(codTercero)){
            String codDomicilio = (String) infoExpTram.getAtributo("codDomicilio");
            String versionTercero = (String) infoExpTram.getAtributo("version");
            String codRolTercero = (String) infoExpTram.getAtributo("codRol");  // Es el valor del rol principal del procedimiento
            if ("".equals(codRolTercero)) codRolTercero = "1";

            descripcionOperacion += linha("eMovExpIntCodTer",codTercero) +
                linha("eMovExpIntCodDom",codDomicilio) +
                linha("eMovExpIntVerTer",versionTercero) +
                linha("eMovExpIntCodRol",codRolTercero);
        }
        
        descripcionOperacion += cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);
                
        return descripcionOperacion;
    }
	
	public static String generarDescripcionOperacionExpediente(OperacionExpedienteVO operacion){
		// TODO Es prerrequisito que ninguno de estos campos venga a null?
		String fechaOper = DateOperations.extraerFechayHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
		String codMunicipio = Integer.toString(operacion.getCodMunicipio());
		String ejercicio = Integer.toString(operacion.getEjercicio());
		String numeroExpediente = operacion.getNumExpediente();
		String tipoOperacion = Integer.toString(operacion.getTipoOperacion());
		String codUsuario = Integer.toString(operacion.getCodUsuario());
		
		StringBuilder sb = new StringBuilder();
		if (operacion.getTipoOperacion() == ConstantesDatos.TIPO_MOV_EXPORTAR_EXPEDIENTE){
			sb.append(cabb1("exportacionExp"));
		}else if (operacion.getTipoOperacion() == ConstantesDatos.TIPO_MOV_IMPORTAR_EXPEDIENTE) {
			sb.append(cabb1("importacionExp"));
		}
		sb.append(linea("codMunicipio", codMunicipio));
		sb.append(linea("ejercicio", ejercicio));
		sb.append(linea("numeroExpediente",numeroExpediente));
		sb.append(linea("tipoOperacion",tipoOperacion));
		sb.append(linea("fecha", fechaOper));
		sb.append(linea("codUsuario",codUsuario));
		
		return sb.toString();
	}
        
    /*
     * Genera descripción con los datos de grabación de un expediente
     * @param tercero Objeto que contiene información del expediente y del trámite inicial
     * @return String descripcionOperacion
     */
    public static String generarDescripcionGrabarExpediente(GeneralValueObject infoExpTram, String fechaOper) {
        
        String codMunicipio = (String) infoExpTram.getAtributo("codMunicipio");
        String codProcedimiento = (String) infoExpTram.getAtributo("codProcedimiento");
        String ejercicio = (String) infoExpTram.getAtributo("ejercicio");
        String numero = (String) infoExpTram.getAtributo("numero");
        String usuario = (String) infoExpTram.getAtributo("usuario");
        String nomUsuario = (String) infoExpTram.getAtributo("nomUsuario");
        String asunto = (String) infoExpTram.getAtributo("asunto");
        String observaciones = (String) infoExpTram.getAtributo("observaciones");
        String localizacion = (String) infoExpTram.getAtributo("localizacion");
        String codLocalizacion = (String) infoExpTram.getAtributo("codLocalizacion");
        
        String descripcionOperacion = cab1("eMovExpGrabarExp") + 
            linha("eMovExpNumExp",numero) +
            linha("eMovExpCodMun",codMunicipio) +
            linha("eMovExpCodProc",codProcedimiento) +
            linha("eMovExpEjercicio",ejercicio) +
            linha("eMovExpAsunto",asunto) + 
            linha("eMovExpObservac",observaciones) + 
            linha("eMovExpLocalizac",localizacion) + 
            linha("eMovExpCodLocal",codLocalizacion);
        
        descripcionOperacion += incluirDatosSuplementarios(infoExpTram,false);
        
        descripcionOperacion += cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);
                
        return descripcionOperacion;
    }
        
    /*
     * Genera descripción con los datos de reapertura de un expediente
     * @param tercero Objeto que contiene información del expediente y del trámite inicial
     * @return String descripcionOperacion
     */
    public static String generarDescripcionReabrirExpediente(GeneralValueObject infoReabrirExp, String fechaOper) {
        
        String codMunicipio = (String) infoReabrirExp.getAtributo("codMunicipio");
        String codProcedimiento = (String) infoReabrirExp.getAtributo("codProcedimiento");
        String ejercicio = (String) infoReabrirExp.getAtributo("ejercicio");
        String numero = (String) infoReabrirExp.getAtributo("numero");
        String usuario = (String) infoReabrirExp.getAtributo("usuario");
        String nomUsuario = (String) infoReabrirExp.getAtributo("nomUsuario");
        String estadoExpediente = (String) infoReabrirExp.getAtributo("estadoExpediente");
        
        String descripcionOperacion = cab1("eMovExpReabrirExp") + 
            linha("eMovExpNumExp",numero) +
            linha("eMovExpCodMun",codMunicipio) +
            linha("eMovExpCodProc",codProcedimiento) +
            linha("eMovExpEjercicio",ejercicio) +
            linha("eMovExpEstado",estadoExpediente) + 
            cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);
        
        return descripcionOperacion;
    }
        
    /*
     * Genera descripción con los datos de finalización de un expediente
     * @param tercero Objeto que contiene información del expediente y del trámite inicial
     * @return String descripcionOperacion
     */
    public static String generarDescripcionFinalizarExpediente(TramitacionExpedientesValueObject infoFinExp, String fechaOper) {
        
        String codMunicipio = infoFinExp.getCodMunicipio();
        String codProcedimiento = infoFinExp.getCodProcedimiento();
        String ejercicio = infoFinExp.getEjercicio();
        String numero = (infoFinExp.getNumero()!=null && !"".equals(infoFinExp.getNumero().trim()))?infoFinExp.getNumero():infoFinExp.getNumeroExpediente();
        String usuario = infoFinExp.getCodUsuario();
        String nomUsuario = infoFinExp.getNombreUsuario();
        
        String descripcionOperacion = cab1("eMovExpFinalizaExp") + 
            linha("eMovExpNumExp",numero) + 
            linha("eMovExpCodMun",codMunicipio) +
            linha("eMovExpCodProc",codProcedimiento) +
            linha("eMovExpEjercicio",ejercicio);

        descripcionOperacion += incluirDatosSuplementarios(infoFinExp.getCamposSuplementarios(),false);

        descripcionOperacion += cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);
        
        return descripcionOperacion;
    }
        
    /*
     * Genera descripción con los datos de anulación de un expediente
     * @param tercero Objeto que contiene información del expediente y del trámite inicial
     * @return String descripcionOperacion
     */
    public static String generarDescripcionAnularExpediente(TramitacionExpedientesValueObject infoFinExp, String fechaOper) {
        
        String codMunicipio = infoFinExp.getCodMunicipio();
        String codProcedimiento = infoFinExp.getCodProcedimiento();
        String ejercicio = infoFinExp.getEjercicio();
        String numero = infoFinExp.getNumero();
        String usuario = infoFinExp.getCodUsuario();
        String nomUsuario = infoFinExp.getNombreUsuario();
        String personaAutoriza = infoFinExp.getPersonaAutoriza();
        String justificacion = infoFinExp.getJustificacion();
        
        
        String descripcionOperacion = cab1("eMovExpAnulaExp") + 
            linha("eMovExpNumExp",numero) +
            linha("eMovExpCodMun",codMunicipio) +
            linha("eMovExpCodProc",codProcedimiento) +
            linha("eMovExpEjercicio",ejercicio)  +
            linha("eMovExpMotivo",justificacion)  +
            linha("eMovExpPersonaAutoriza",personaAutoriza)  +
            cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);
        
        return descripcionOperacion;
    }
        
    /*
     * Genera descripción con los datos de grabación de un trámite
     * @param tercero Objeto que contiene información del expediente y del trámite
     * @return String descripcionOperacion
     */
    public static String generarDescripcionGrabarTramite(TramitacionExpedientesValueObject infoTramite, String fechaOper) {
        
        String codMunicipio = infoTramite.getCodMunicipio();
        String codProcedimiento = infoTramite.getCodProcedimiento();
        String ejercicio = infoTramite.getEjercicio();
        String numero = (infoTramite.getNumero()!=null && !"".equals(infoTramite.getNumero().trim()))?infoTramite.getNumero():infoTramite.getNumeroExpediente();
        String codTramite = infoTramite.getCodTramite();
        String nomTramite = infoTramite.getTramite();
        String ocurrTramite = infoTramite.getOcurrenciaTramite();
        String usuario = infoTramite.getCodUsuario();
        String nomUsuario = infoTramite.getNombreUsuario();
        String fechaFin = infoTramite.getFechaFin();
        String fechaInicioPlazo = infoTramite.getFechaInicioPlazo();
        String fechaFinPlazo = infoTramite.getFechaFinPlazo();
        String fechaLimite = infoTramite.getFechaLimite();
        String observaciones = infoTramite.getObservaciones();
        String bloqueo = infoTramite.getBloqueo();
        
        String descripcionOperacion = cab1("eMovExpGrabarTram") + 
            linha("eMovExpNumExp",numero) +
            linha("eMovExpCodMun",codMunicipio) +
            linha("eMovExpCodProc",codProcedimiento) +
            linha("eMovExpEjercicio",ejercicio) +
            linha("eMovExpNomTramite",nomTramite) +
            linha("eMovExpCodTramite",codTramite) +
            linha("eMovExpOcurrTramite",ocurrTramite) + 
            linha("eMovExpObservac",observaciones) + 
            linha("eMovExpBloqueo",bloqueo);
        
        if (informado(fechaFin)) 
            descripcionOperacion += linha("eMovExpFecFin",fechaFin);
        if (informado(fechaInicioPlazo)) 
            descripcionOperacion += linha("eMovExpFecIniPlz",fechaInicioPlazo);
        if (informado(fechaFinPlazo)) 
            descripcionOperacion += linha("eMovExpFecFinPlz",fechaFinPlazo);
        if (informado(fechaLimite)) 
            descripcionOperacion += linha("eMovExpFecLimite",fechaLimite);
        
        descripcionOperacion += incluirDatosSuplementarios(infoTramite.getCamposSuplementarios(),true);
                
        descripcionOperacion += cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);

        return descripcionOperacion;
    }

        /*
     * Genera descripción con los datos de finalización de un trámite 
     * @param tercero Objeto que contiene información del expediente y del trámite
     * @return String descripcionOperacion
     */
    public static String generarDescripcionFinalizarTramite(TramitacionExpedientesValueObject infoTramite, 
            Boolean desfavorable, String fechaOper) {
        
        String codMunicipio = infoTramite.getCodMunicipio();
        String codProcedimiento = infoTramite.getCodProcedimiento();
        String ejercicio = infoTramite.getEjercicio();
        String numero = (infoTramite.getNumero()!=null && !"".equals(infoTramite.getNumero().trim()))?infoTramite.getNumero():infoTramite.getNumeroExpediente();
        String codTramite = infoTramite.getCodTramite();
        String nomTramite = infoTramite.getTramite();
        String ocurrTramite = infoTramite.getOcurrenciaTramite();
        String usuario = infoTramite.getCodUsuario();
        String nomUsuario = infoTramite.getNombreUsuario();
        String fechaFin = infoTramite.getFechaFin();
        String fechaInicioPlazo = infoTramite.getFechaInicioPlazo();
        String fechaFinPlazo = infoTramite.getFechaFinPlazo();
        String fechaLimite = infoTramite.getFechaLimite();
        String bloqueo = infoTramite.getBloqueo();
        
        String descripcionOperacion = cab1("eMovExpFinalizarTram") + 
            linha("eMovExpNumExp",numero) +
            linha("eMovExpCodMun",codMunicipio) +
            linha("eMovExpCodProc",codProcedimiento) +
            linha("eMovExpEjercicio",ejercicio) +
            linha("eMovExpNomTramite",nomTramite) +
            linha("eMovExpCodTramite",codTramite) +
            linha("eMovExpOcurrTramite",ocurrTramite);
        
        if (informado(fechaFin)) 
            descripcionOperacion += linha("eMovExpFecFin",fechaFin);
        if (informado(fechaInicioPlazo)) 
            descripcionOperacion += linha("eMovExpFecIniPlz",fechaInicioPlazo);
        if (informado(fechaFinPlazo)) 
            descripcionOperacion += linha("eMovExpFecFinPlz",fechaFinPlazo);
        if (informado(fechaLimite)) 
            descripcionOperacion += linha("eMovExpFecLimite",fechaLimite);
        if (desfavorable!=null) 
            descripcionOperacion += linha("eMovExpResol",(desfavorable?"{etiq_no}":"{etiq_si}"));
        if (informado(bloqueo)) 
            descripcionOperacion += linha("eMovExpBloqueo",bloqueo);
        
        descripcionOperacion += incluirDatosSuplementarios(infoTramite.getCamposSuplementarios(),true);
        
        descripcionOperacion += cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);
                
        return descripcionOperacion;
    }
    
    /*
     * Genera descripción con los datos de inicio de un trámite 
     * @param infoTramite Objeto que contiene información del expediente y del trámite
     * @param codMunicipio Indica se se trata de un tramite iniciado de forma manual o automatica
     * @return String descripcionOperacion
     */
    public static String generarDescripcionIniciarTramite(GeneralValueObject infoTramite, 
            boolean manual, String fechaOper) {                    
        String codMunicipio = (String) infoTramite.getAtributo("codMunicipio");
        String codProcedimiento = (String) infoTramite.getAtributo("codProcedimiento");
        String ejercicio = (String) infoTramite.getAtributo("ejercicio");
        String numero = (String) infoTramite.getAtributo("numero");
        String codTramite = (String) infoTramite.getAtributo("codTramite");
        String nomTramite = (String) infoTramite.getAtributo("nomTramite");
        String ocurrTramite = (String) infoTramite.getAtributo("ocurrTramite");
        String usuario = (String) infoTramite.getAtributo("usuario");
        String nomUsuario = (String) infoTramite.getAtributo("nombreUsuario");
        String fechaInicio = (String) infoTramite.getAtributo("fechaInicioTramite");
        
        String tituloOperacion = null;
        if (manual) {
            tituloOperacion = cab1("eMovExpIniciarTramManual");
        } else {
            tituloOperacion = cab1("eMovExpIniciarTram");
        }
        
        String descripcionOperacion = tituloOperacion + 
            linha("eMovExpNumExp",numero) +
            linha("eMovExpCodMun",codMunicipio) +
            linha("eMovExpCodProc",codProcedimiento) +
            linha("eMovExpEjercicio",ejercicio) +
            linha("eMovExpNomTramite",nomTramite) +
            linha("eMovExpCodTramite",codTramite) +
            linha("eMovExpOcurrTramite",ocurrTramite) +
            linha("eMovExpFecIni",fechaInicio); 

        descripcionOperacion += cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);
                
        return descripcionOperacion;
    }
        
    /*
     * Genera descripción con los datos del retroceso de un trámite
     * @param tercero Objeto que contiene información del expediente y del trámite
     * @return String descripcionOperacion
     */
    public static String generarDescripcionRetrocederTramite(GeneralValueObject infoTramite, String fechaOper) {
        
        String codMunicipio = (String) infoTramite.getAtributo("codMunicipio");
        String codProcedimiento = (String) infoTramite.getAtributo("codProcedimiento");
        String ejercicio = (String) infoTramite.getAtributo("ejercicio");
        String numero = (String) infoTramite.getAtributo("numero");
        String usuario = (String) infoTramite.getAtributo("usuario");
        String nomUsuario = (String) infoTramite.getAtributo("nomUsuario");
        String nomTramite = (String) infoTramite.getAtributo("tramiteRetroceder");
        String codTramite = (String) infoTramite.getAtributo("codTramiteRetroceder");
        String ocurrTramite = (String) infoTramite.getAtributo("ocurrenciaTramiteRetroceder");
        String fechaInicio = (String) infoTramite.getAtributo("fechaInicioTramiteRetroceder");

        String descripcionOperacion = cab1("eMovExpRetrocTram") + 
            linha("eMovExpNumExp",numero) +
            linha("eMovExpCodMun",codMunicipio) +
            linha("eMovExpCodProc",codProcedimiento) +
            linha("eMovExpEjercicio",ejercicio) +
            linha("eMovExpNomTramite",nomTramite) +
            linha("eMovExpCodTramite",codTramite) +
            linha("eMovExpOcurrTramite",ocurrTramite) +
            linha("eMovExpFecIni",fechaInicio) +       
            cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);

            return descripcionOperacion;
    }
    
    /*
     * Genera descripción con los datos del retroceso de un trámite de origen
     * @param tercero Objeto que contiene información del expediente y del trámite
     * @return String descripcionOperacion
     */
    public static String generarDescripcionRetrocederTramiteOrigen(GeneralValueObject infoTramite, String fechaOper) {
        
        String codMunicipio = (String) infoTramite.getAtributo("codMunicipio");
        String codProcedimiento = (String) infoTramite.getAtributo("codProcedimiento");
        String ejercicio = (String) infoTramite.getAtributo("ejercicio");
        String numero = (String) infoTramite.getAtributo("numero");
        String usuario = (String) infoTramite.getAtributo("usuario");
        String nomUsuario = (String) infoTramite.getAtributo("nomUsuario");
        String nomTramite = (String) infoTramite.getAtributo("nomTramiteOrigenReabrir");
        String codTramite = (String) infoTramite.getAtributo("codTramiteOrigenReabrir");
        String ocurrTramite = (String) infoTramite.getAtributo("ocurrenciaTramiteOrigenReabrir");
        String fechaInicio = (String) infoTramite.getAtributo("fecIniTramiteOrigenReabrir");            
 
        String descripcionOperacion = cab1("eMovExpRetrocTramOrigen") +
            linha("eMovExpNumExp",numero) +
            linha("eMovExpCodMun",codMunicipio) +
            linha("eMovExpCodProc",codProcedimiento) +
            linha("eMovExpEjercicio",ejercicio) +
            linha("eMovExpNomTramite",nomTramite) +
            linha("eMovExpCodTramite",codTramite) +
            linha("eMovExpOcurrTramite",ocurrTramite) +
            linha("eMovExpFecIni",fechaInicio) +       
            cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);

            return descripcionOperacion;
    }
    
    
    public static String generarDescripcionReabrirTramite(GeneralValueObject infoTramite, String fechaOper) {
        
        String codMunicipio = (String) infoTramite.getAtributo("codMunicipio");
        String codProcedimiento = (String) infoTramite.getAtributo("codProcedimiento");
        String ejercicio = (String) infoTramite.getAtributo("ejercicio");
        String numero = (String) infoTramite.getAtributo("numero");
        String usuario = (String) infoTramite.getAtributo("usuario");
        String nomUsuario = (String) infoTramite.getAtributo("nomUsuario");
        String nomTramite = (String) infoTramite.getAtributo("tramiteRetroceder");
        String codTramite = (String) infoTramite.getAtributo("codTramiteRetroceder");
        String ocurrTramite = (String) infoTramite.getAtributo("ocurrenciaTramiteRetroceder");
        String fechaInicio = (String) infoTramite.getAtributo("fechaInicioTramiteRetroceder");            
 
        String descripcionOperacion = cab1("eMovExpReabrirTramOrigen") +
            linha("eMovExpNumExp",numero) +
            linha("eMovExpCodMun",codMunicipio) +
            linha("eMovExpCodProc",codProcedimiento) +
            linha("eMovExpEjercicio",ejercicio) +
            linha("eMovExpNomTramite",nomTramite) +
            linha("eMovExpCodTramite",codTramite) +
            linha("eMovExpOcurrTramite",ocurrTramite) +
            linha("eMovExpFecIni",fechaInicio) +       
            cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);

            return descripcionOperacion;
    }
    
    /*
     * Genera descripción con los datos del alta de un interesado
     * @param tercero Objeto que contiene información del tercero
     * @return String descripcionOperacion
     */
    public static String generarDescripcionAltaInteresado(TercerosValueObject tercero, int usuario, 
            String nomUsuario, String fechaOper) {
            
        String descripcionOperacion = cab1("eMovExpIntAlt") + 
            linha("eMovExpIntCodTer",tercero.getIdentificador()) +
            linha("eMovExpIntVerTer",tercero.getVersion()) +
            linha("eMovExpIntCodDom",tercero.getIdDomicilio()) +
            linha("eMovExpIntCambVer",tercero.getCodRol().toString()) +
            linha("eMovExpIntNotifElec",tercero.getNotificacionElectronica()) +
            linha("eMovExpIntNom",tercero.getNombre()) +
            linha("eMovExpIntApel1",tercero.getApellido1()) +
            linha("eMovExpIntApel2",tercero.getApellido2()) +
            linha("eMovExpIntTipoDoc",tercero.getTipoDocumento()) +
            linha("eMovExpIntDoc",tercero.getDocumento()) +
            linha("eMovExpIntTel",tercero.getTelefono()) +
            linha("eMovExpIntEmail",tercero.getEmail()); 
                
        if (tercero.getDomicilios() != null && tercero.getDomicilios().size()>0){
            DomicilioSimpleValueObject domicilio = (DomicilioSimpleValueObject) tercero.getDomicilios().elementAt(0);
            if (domicilio != null){
                descripcionOperacion += linha("eMovExpIntTipoVia",domicilio.getTipoVia()) +
                    linha("eMovExpIntNomVia",domicilio.getDescVia()) +
                    linha("eMovExpIntEmp",domicilio.getDomicilio()) +
                    linha("eMovExpIntNumDesde",domicilio.getNumDesde()) +
                    linha("eMovExpIntNumHasta",domicilio.getNumHasta()) +
                    linha("eMovExpIntLetDesde",domicilio.getLetraDesde()) +
                    linha("eMovExpIntletHasta",domicilio.getLetraHasta()) +
                    linha("eMovExpIntBloque",domicilio.getBloque()) +
                    linha("eMovExpIntPortal",domicilio.getPortal()) +
                    linha("eMovExpIntEsc",domicilio.getEscalera()) +
                    linha("eMovExpIntPlanta",domicilio.getPlanta()) +
                    linha("eMovExpIntPuerta",domicilio.getPuerta()) +
                    linha("eMovExpIntCodPos",domicilio.getCodigoPostal()) +
                    linha("eMovExpIntMun",domicilio.getMunicipio()) +
                    linha("eMovExpIntPro",domicilio.getProvincia());
            }
        }
        
        descripcionOperacion += cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);

        return descripcionOperacion;
    }
        
    /*
     * Genera descripción con los datos de la eliminación de un interesado
     * @param tercero Objeto que contiene información del tercero
     * @return String descripcionOperacion
     */
    public static String generarDescripcionEliminacionInteresado(TercerosValueObject tercero, int usuario, 
            String nomUsuario, String fechaOper) {
        
        String descripcionOperacion = cab1("eMovExpIntEliminar") + 
            linha("eMovExpIntCodTer",tercero.getIdentificador()) + 
            linha("eMovExpIntVerTer",tercero.getVersion()) +
            linha("eMovExpIntDoc",tercero.getDocumento()) +
            linha("eMovExpIntNom",tercero.getNombreCompleto()) + 
            cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);
                    
        return descripcionOperacion;
    }
        
    /*
     * Genera descripción con los datos de la modificacion de un interesado
     * @param interesadoExpedienteVO Objeto que contiene información de interesado del expediente
     * @param
     * @param
     * @param
     * @return String descripcionOperacion
     */
    public static String generarDescripcionModificacionInteresado(GeneralValueObject gVO, boolean cambioVersion, 
            boolean cambioRol, boolean cambioDomicilio, boolean cambioNotifElec, boolean cambioDatos, String fechaOper) {
            
        //Recupera los valores a mostrar
        String codigo = (String) gVO.getAtributo("Codigo");
        String versionA = (String) gVO.getAtributo("VersionA");
        String versionN = (String) gVO.getAtributo("VersionN");        
        String rolA = (String) gVO.getAtributo("RolA");        
        String rolN = (String) gVO.getAtributo("RolN");        
        String descRolA = (String) gVO.getAtributo("DescRolA");        
        String descRolN = (String) gVO.getAtributo("DescRolN");        
        String codDomicilioA = (String) gVO.getAtributo("CodDomicilioA");                
        String codDomicilioN = (String) gVO.getAtributo("CodDomicilioN");        
        String notifElectA = (String) gVO.getAtributo("NotifElectA");   
        String notifElectN = (String) gVO.getAtributo("NotifElectN");    
        String nombreA = (String) gVO.getAtributo("NombreA");
        String nombreN= (String) gVO.getAtributo("NombreN");
        String apellido1A = (String) gVO.getAtributo("Apellido1A");
        String apellido1N = (String) gVO.getAtributo("Apellido1N");
        String apellido2A = (String) gVO.getAtributo("Apellido2A");
        String apellido2N = (String) gVO.getAtributo("Apellido2N");
        String tipoDocumentoA = (String) gVO.getAtributo("TipoDocumentoA");        
        String tipoDocumentoN = (String) gVO.getAtributo("TipoDocumentoN");
        String documentoA = (String) gVO.getAtributo("DocumentoA");
        String documentoN = (String) gVO.getAtributo("DocumentoN");
        String telefonoA = (String) gVO.getAtributo("TelefonoA");
        String telefonoN = (String) gVO.getAtributo("TelefonoN");
        String emailA = (String) gVO.getAtributo("EmailA");
        String emailN = (String) gVO.getAtributo("EmailN");  
        String tipoViaA = (String) gVO.getAtributo("TipoViaA");  
        String descViaA = (String) gVO.getAtributo("DescViaA");          
        String emplazamientoA = (String) gVO.getAtributo("EmplazamientoA");          
        String portalA = (String) gVO.getAtributo("PortalA");          
        String plantaA = (String) gVO.getAtributo("PlantaA");          
        String puertaA = (String) gVO.getAtributo("PuertaA");          
        String escaleraA = (String) gVO.getAtributo("EscaleraA");          
        String bloqueA = (String) gVO.getAtributo("BloqueA");          
        String letraDesdeA = (String) gVO.getAtributo("LetraDesdeA");          
        String letraHastaA = (String) gVO.getAtributo("LetraHastaA");          
        String numDesdeA = (String) gVO.getAtributo("NumDesdeA");          
        String numHastaA = (String) gVO.getAtributo("NumHastaA");          
        String codigoPostalA = (String) gVO.getAtributo("CodigoPostalA");          
        String municipioA = (String) gVO.getAtributo("MunicipioA");          
        String provinciaA = (String) gVO.getAtributo("ProvinciaA");          
        String tipoViaN = (String) gVO.getAtributo("TipoViaN");  
        String descViaN = (String) gVO.getAtributo("DescViaN");          
        String emplazamientoN = (String) gVO.getAtributo("EmplazamientoN");          
        String portalN = (String) gVO.getAtributo("PortalN");          
        String plantaN = (String) gVO.getAtributo("PlantaN");          
        String puertaN = (String) gVO.getAtributo("PuertaN");          
        String escaleraN = (String) gVO.getAtributo("EscaleraN");          
        String bloqueN = (String) gVO.getAtributo("BloqueN");          
        String letraDesdeN = (String) gVO.getAtributo("LetraDesdeN");          
        String letraHastaN = (String) gVO.getAtributo("LetraHastaN");          
        String numDesdeN = (String) gVO.getAtributo("NumDesdeN");          
        String numHastaN = (String) gVO.getAtributo("NumHastaN");          
        String codigoPostalN = (String) gVO.getAtributo("CodigoPostalN");          
        String municipioN = (String) gVO.getAtributo("MunicipioN");          
        String provinciaN = (String) gVO.getAtributo("ProvinciaN");  
        String usuario = (String) gVO.getAtributo("usuario");  
        String nomUsuario = (String) gVO.getAtributo("nomUsuario");  

        String descripcionOperacion = cab1("eMovExpIntModificar") + 
            linha("eMovExpIntCodTer",codigo);
        
        if (cambioVersion){
            descripcionOperacion += cab2("eMovExpIntCambVer") + 
                linha("eMovExpIntVerA",versionA) +
                linha("eMovExpIntVerN",versionN);
        }        
        if (cambioRol){
            descripcionOperacion += cab2("eMovExpIntCambRol") + 
                linha("eMovExpIntCodRolA",rolA) +
                linha("eMovExpIntDesRolA",descRolA) +
                linha("eMovExpIntCodRolN",rolN) +
                linha("eMovExpIntDesRolN",descRolN);
        }
        if (cambioDomicilio){
            descripcionOperacion +=cab2("eMovExpIntCambDom") + 
                cab3("eMovExpIntCambDomA") +
                linha("eMovExpIntCodDom",codDomicilioA) +
                linha("eMovExpIntTipoVia",tipoViaA) +
                linha("eMovExpIntNomVia",descViaA) +
                linha("eMovExpIntEmp",emplazamientoA) +
                linha("eMovExpIntPortal",portalA) +
                linha("eMovExpIntPlanta",plantaA) +
                linha("eMovExpIntPuerta",puertaA) +
                linha("eMovExpIntEsc",escaleraA) +
                linha("eMovExpIntBloque",bloqueA) +
                linha("eMovExpIntLetDesde",letraDesdeA) +
                linha("eMovExpIntletHasta",letraHastaA) +
                linha("eMovExpIntNumDesde",numDesdeA) +
                linha("eMovExpIntNumHasta",numHastaA) +
                linha("eMovExpIntCodPos",codigoPostalA) +
                linha("eMovExpIntMun",municipioA) +
                linha("eMovExpIntPro",provinciaA)+ 
                cab3("eMovExpIntCambDomN") +
                linha("eMovExpIntCodDom",codDomicilioN) +
                linha("eMovExpIntTipoVia",tipoViaN) +
                linha("eMovExpIntNomVia",descViaN) +
                linha("eMovExpIntEmp",emplazamientoN) +
                linha("eMovExpIntPortal",portalN) +
                linha("eMovExpIntPlanta",plantaN) +
                linha("eMovExpIntPuerta",puertaN) +
                linha("eMovExpIntEsc",escaleraN) +
                linha("eMovExpIntBloque",bloqueN) +
                linha("eMovExpIntLetDesde",letraDesdeN) +
                linha("eMovExpIntletHasta",letraHastaN) +
                linha("eMovExpIntNumDesde",numDesdeN) +
                linha("eMovExpIntNumHasta",numHastaN) +
                linha("eMovExpIntCodPos",codigoPostalN) +
                linha("eMovExpIntMun",municipioN) +
                linha("eMovExpIntPro",provinciaN); 
        }
        if (cambioNotifElec){
            descripcionOperacion += cab2("eMovExpIntCambNotElect") + 
                linha("eMovExpIntNotElectA",notifElectA) +
                linha("eMovExpIntNotElectN",notifElectN);
        }
        if (cambioDatos){
            descripcionOperacion += cab2("eMovExpIntCambDatPer") + 
                cab3("eMovExpIntCambDatPerA") +
                linha("eMovExpIntNomA",nombreA) +
                linha("eMovExpIntApel1A",apellido1A) +
                linha("eMovExpIntApel2A",apellido2A) +
                linha("eMovExpIntTipoDocA",tipoDocumentoA) +
                linha("eMovExpIntDocA",documentoA) +
                linha("eMovExpIntTelA",telefonoA) +
                linha("eMovExpIntEmailA",emailA) + 
                cab3("eMovExpIntCambDatPerN") +
                linha("eMovExpIntNomN",nombreN) +
                linha("eMovExpIntApel1N",apellido1N) +
                linha("eMovExpIntApel2N",apellido2N) +
                linha("eMovExpIntTipoDocN",tipoDocumentoN) +
                linha("eMovExpIntDocN",documentoN) +
                linha("eMovExpIntTelN",telefonoN) +
                linha("eMovExpIntEmailN",emailN);
        }
        
        descripcionOperacion += cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);

        return descripcionOperacion;
    }
        
    /*
     * Genera descripción con los datos de añadir relación
     * @param tercero Objeto que contiene información del expediente y del trámite inicial
     * @return String descripcionOperacion
     */
    public static String generarDescripcionAnhadirRelacion(ConsultaExpedientesValueObject infoRelExp, 
            String fechaOper) {
        
        String codMunicipio = infoRelExp.getCodMunicipioIni();
        String ejercicio = infoRelExp.getEjercicioIni();
        String numero = infoRelExp.getNumeroExpedienteIni();
        String codMunicipioRel = infoRelExp.getCodMunicipio();
        String ejercicioRel = infoRelExp.getEjercicio();
        String numeroRel = infoRelExp.getNumeroExpediente();
        String usuario = infoRelExp.getUsuario();
        String nomUsuario = infoRelExp.getNombreUsuario();
        
        String descripcionOperacion = cab1("eMovExpAnhadirRel") + 
            cab2("eMovExpExpIni") + 
            linha("eMovExpCodMun",codMunicipio) +
            linha("eMovExpEjercicio",ejercicio) +
            linha("eMovExpNumExp",numero) + 
            cab2("eMovExpExpRel") + 
            linha("eMovExpCodMun",codMunicipioRel) +
            linha("eMovExpEjercicio",ejercicioRel) +
            linha("eMovExpNumExp",numeroRel) + 
            cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);
        
        return descripcionOperacion;
    }
    
    /*
     * Genera descripción con los datos de añadir relación
     * @param tercero Objeto que contiene información del expediente y del trámite inicial
     * @return String descripcionOperacion
     */
    public static String generarDescripcionEliminarRelacion(ConsultaExpedientesValueObject infoRelExp, 
            String fechaOper) {
        
        String codMunicipio = infoRelExp.getCodMunicipioIni();
        String ejercicio = infoRelExp.getEjercicioIni();
        String numero = infoRelExp.getNumeroExpedienteIni();
        String codMunicipioRel = infoRelExp.getCodMunicipio();
        String ejercicioRel = infoRelExp.getEjercicio();
        String numeroRel = infoRelExp.getNumeroExpediente();
        String usuario = infoRelExp.getUsuario();
        String nomUsuario = infoRelExp.getNombreUsuario();
        
        String descripcionOperacion = cab1("eMovExpEliminarRel") + 
            cab2("eMovExpExpIni") + 
            linha("eMovExpCodMun",codMunicipio) +
            linha("eMovExpEjercicio",ejercicio) +
            linha("eMovExpNumExp",numero) + 
            cab2("eMovExpExpRel") + 
            linha("eMovExpCodMun",codMunicipioRel) +
            linha("eMovExpEjercicio",ejercicioRel) +
            linha("eMovExpNumExp",numeroRel) + 
            cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",usuario) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);
        
        return descripcionOperacion;
    }
        
    /*
     * Genera descripción con los datos de añadir documento de expediente
     * @param tercero Objeto que contiene información del documento
     * @return String descripcionOperacion
     */
    public static String generarDescripcionAltaDocumentoExpediente(Documento doc, 
            String nomUsuario, boolean externo, String fechaOper) {
        
        String descripcionOperacion = "";
        
        if (externo)
            descripcionOperacion = cab1("eMovExpAltDocExt");
        else
            descripcionOperacion = cab1("eMovExpAltDocExp");
        
        descripcionOperacion += linha("eMovExpCodMun",doc.getCodMunicipio()) +
            linha("eMovExpEjercicio",doc.getEjercicio()) +
            linha("eMovExpNumExp",doc.getNumeroExpediente()) + 
            linha("eMovExpCodDoc",doc.getCodDocumento()) +
            linha("eMovExpNomDoc",doc.getNombreDocumento()) +
            linha("eMovExpExtDoc",doc.getExtension()) + 
            linha("eMovExpMimeDoc",doc.getTipoMimeContenido()) + 
            linha("eMovExpOriDoc",doc.getOrigen()) + 
            cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",doc.getCodUsuario()) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);
        
        return descripcionOperacion;
    }
        
    /*
     * Genera descripción con los datos de eliminar documento de expediente
     * @param tercero Objeto que contiene información del documento
     * @return String descripcionOperacion
     */
    public static String generarDescripcionEliminacionDocumentoExpediente(Documento doc, 
            String nomUsuario, boolean externo, String fechaOper) {
        
        String descripcionOperacion = "";
        
        if (externo)
            descripcionOperacion = cab1("eMovExpElimDocExt");
        else
            descripcionOperacion = cab1("eMovExpElimDocExp");
        
        descripcionOperacion += linha("eMovExpCodMun",doc.getCodMunicipio()) +
            linha("eMovExpEjercicio",doc.getEjercicio()) +
            linha("eMovExpNumExp",doc.getNumeroExpediente()) +
            linha("eMovExpCodDoc",doc.getCodDocumento()) +
            linha("eMovExpNomDoc",doc.getNombreDocumento()) +
            linha("eMovExpExtDoc",doc.getExtension()) +
            linha("eMovExpMimeDoc",doc.getTipoMimeContenido()) +
            linha("eMovExpOriDoc",doc.getOrigen()) +
            cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",doc.getCodUsuario()) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);
        
        return descripcionOperacion;
    }
        
    /*
     * Genera descripción con los datos de añadir documento de tramitación
     * @param tercero Objeto que contiene información del documento
     * @return String descripcionOperacion
     */
    public static String generarDescripcionAltaDocumentoTramite(Documento doc, 
            String nomUsuario, String fechaOper) {
        
        String descripcionOperacion = cab1("eMovExpAltDocTra") + 
            linha("eMovExpCodMun",doc.getCodMunicipio()) +
            linha("eMovExpEjercicio",doc.getEjercicio()) +
            linha("eMovExpNumExp",doc.getNumeroExpediente()) +
            linha("eMovExpCodTramite",doc.getCodTramite()) +
            linha("eMovExpOcurrTramite",doc.getOcurrenciaTramite()) + 
            linha("eMovExpCodDoc",doc.getCodDocumento()) +
            linha("eMovExpNomDoc",doc.getNombreDocumento()) +
            linha("eMovExpExtDoc",doc.getExtension()) +
            linha("eMovExpMimeDoc",doc.getTipoMimeContenido()) +
            linha("eMovExpOriDoc",doc.getOrigen()) +
            cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",doc.getCodUsuario()) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);
        
        return descripcionOperacion;
    }
        
    /*
     * Genera descripción con los datos de eliminar documento de tramitación
     * @param tercero Objeto que contiene información del documento
     * @return String descripcionOperacion
     */
    public static String generarDescripcionEliminacionDocumentoTramite(Documento doc, 
            String nomUsuario, String fechaOper) {
        
        String descripcionOperacion = cab1("eMovExpElimDocTra") + 
            linha("eMovExpCodMun",doc.getCodMunicipio()) +
            linha("eMovExpEjercicio",doc.getEjercicio()) +
            linha("eMovExpNumExp",doc.getNumeroExpediente()) +
            linha("eMovExpCodTramite",doc.getCodTramite()) +
            linha("eMovExpOcurrTramite",doc.getOcurrenciaTramite()) + 
            linha("eMovExpCodDoc",doc.getCodDocumento()) +
            linha("eMovExpNomDoc",doc.getNombreDocumento()) +
            linha("eMovExpExtDoc",doc.getExtension()) +
            linha("eMovExpMimeDoc",doc.getTipoMimeContenido()) +
            linha("eMovExpOriDoc",doc.getOrigen()) +
            cab2("eMovExpUsuFec") + 
            linha("eMovExpUsuario",doc.getCodUsuario()) +
            linha("eMovExpNomUsuario",nomUsuario) + 
            linha("gEtiqFecOpe",fechaOper);
        
        return descripcionOperacion;
    }

    /*
     * Genera descripción con los datos del bloqueo de un trámite
     * @param tercero Objeto que contiene información del expediente y del trámite
     * @return String descripcionOperacion
     */
    public static String generarDescripcionBloquearTramite(GeneralValueObject infoTramite, String fechaOper) {
        String usuario = (String) infoTramite.getAtributo("usuario");
        String nomUsuario = (String) infoTramite.getAtributo("nombreUsuario");

        StringBuilder descripcionOperacion = new StringBuilder();
        descripcionOperacion.append(getDatosBasicosTramites("eMovExpBloquearTram", infoTramite))
                .append(getDatosUsuarioMovimiento(usuario, nomUsuario, fechaOper));

        return descripcionOperacion.toString();
    }
    
        /*
     * Genera descripción con los datos del desbloqueo de un trámite
     * @param tercero Objeto que contiene información del expediente y del trámite
     * @return String descripcionOperacion
     */
    public static String generarDescripcionDesbloquearTramite(GeneralValueObject infoTramite, String fechaOper) {
        String usuario = (String) infoTramite.getAtributo("usuario");
        String nomUsuario = (String) infoTramite.getAtributo("nombreUsuario");

        StringBuilder descripcionOperacion = new StringBuilder();
        descripcionOperacion.append(getDatosBasicosTramites("eMovExpDesbloquearTram", infoTramite))
                .append(getDatosUsuarioMovimiento(usuario, nomUsuario, fechaOper));

        return descripcionOperacion.toString();
    }
    
    private static String getDatosBasicosTramites(String etiquetaTitulo, GeneralValueObject infoTramite) {
        StringBuilder descripcionOperacion = new StringBuilder();

        String codMunicipio = (String) infoTramite.getAtributo("codMunicipio");
        String codProcedimiento = (String) infoTramite.getAtributo("codProcedimiento");
        String ejercicio = (String) infoTramite.getAtributo("ejercicio");
        String numero = (String) infoTramite.getAtributo("numero");
        String codTramite = (String) infoTramite.getAtributo("codTramite");
        String nomTramite = (String) infoTramite.getAtributo("nomTramite");
        String ocurrTramite = (String) infoTramite.getAtributo("ocurrTramite");
        String fechaInicio = (String) infoTramite.getAtributo("fechaInicioTramite");

        descripcionOperacion.append(cab1(etiquetaTitulo))
                .append(linha("eMovExpNumExp", numero))
                .append(linha("eMovExpCodMun", codMunicipio))
                .append(linha("eMovExpCodProc", codProcedimiento))
                .append(linha("eMovExpEjercicio", ejercicio))
                .append(linha("eMovExpNomTramite", nomTramite))
                .append(linha("eMovExpCodTramite", codTramite))
                .append(linha("eMovExpOcurrTramite", ocurrTramite))
                .append(linha("eMovExpFecIni", fechaInicio));

        return descripcionOperacion.toString();
    }
    
    private static String getDatosUsuarioMovimiento(String idUsuario, String nomUsuario, String fechaOper) {
        StringBuilder descUsuarioMovimiento = new StringBuilder();
        descUsuarioMovimiento.append(cab2("eMovExpUsuFec"))
            .append(linha("eMovExpUsuario", idUsuario))
            .append(linha("eMovExpNomUsuario", nomUsuario))
            .append(linha("gEtiqFecOpe", fechaOper));

        return descUsuarioMovimiento.toString();
    }
    
    private static String incluirDatosSuplementarios(GeneralValueObject infoCamposSupl, boolean esTramite) {
        
        Vector estDatosSuplementarios = null;
        String texto = "";
        
        if (infoCamposSupl != null) {
            estDatosSuplementarios = (Vector) infoCamposSupl.getAtributo("estDatosSuplementarios");

            if (estDatosSuplementarios!=null && estDatosSuplementarios.size() > 0) {
                boolean senTitulo = true;
                if (estDatosSuplementarios.get(0) instanceof EstructuraCampoAgrupado) {
                    for (int i = 0; i < estDatosSuplementarios.size(); i++) {
                        EstructuraCampoAgrupado eC = (EstructuraCampoAgrupado) estDatosSuplementarios.get(i);
                        if (eC.getCodTramite()==null || "".equals(eC.getCodTramite()) || esTramite){
                            if (senTitulo){
                                texto += cab2(esTramite?"eMovExpDatosSuplTr":"eMovExpDatosSupl");
                                senTitulo = false;
                            }
                            
                            String codCampo = eC.getCodCampo();
                            String etiquetaCampo = eC.getDescCampo();

                            if (ConstantesDatos.TIPO_CAMPO_FICHERO == Integer.parseInt(eC.getCodTipoDato())) {
                                String nombreFichero = (String) infoCamposSupl.getAtributo(String.format("%s%s", codCampo, "_NOMBRE"));
                                String tipoMime = (String) infoCamposSupl.getAtributo(String.format("%s%s", codCampo, "_TIPO"));
                                        
                                texto += linhaCampoSuplFich(etiquetaCampo, nombreFichero, tipoMime);
                            } else {
                                texto += linhaCampoSupl(etiquetaCampo,(String) infoCamposSupl.getAtributo(codCampo));
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < estDatosSuplementarios.size(); i++) {
                        EstructuraCampo eC = (EstructuraCampo) estDatosSuplementarios.get(i);
                        if (eC.getCodTramite()==null || "".equals(eC.getCodTramite()) || esTramite){
                            if (senTitulo){
                                texto += cab2(esTramite?"eMovExpDatosSuplTr":"eMovExpDatosSupl");
                                senTitulo = false;
                            }
                            
                            String etiquetaCampo = eC.getDescCampo();
                            
                            if (ConstantesDatos.TIPO_CAMPO_FICHERO == Integer.parseInt(eC.getCodTipoDato())) {
                                String nombreFichero = (String) infoCamposSupl.getAtributo(String.format("%s%s", etiquetaCampo, "_NOMBRE"));
                                String tipoMime = (String) infoCamposSupl.getAtributo(String.format("%s%s", etiquetaCampo, "_TIPO"));
                                        
                                texto += linhaCampoSuplFich(etiquetaCampo, nombreFichero, tipoMime);
                            } else {
                                texto += linhaCampoSupl(etiquetaCampo,(String) infoCamposSupl.getAtributo(eC.getCodCampo()));
                            }
                        }
                    }
                }
            }
        }
        
        return texto;
    }

    private static boolean informado(String valor) {
        
        boolean estaInformado = true;
        
        if (valor == null || "".equals(valor.trim()) || "null".equalsIgnoreCase(valor.trim()))
            estaInformado = false;

        return estaInformado;
    }
	
	/**
	 * Metodo para guardar el movimiento con una determinada clase css para que se muestren los datos como cabecera principal
	 * @param etiqueta
	 * @return 
	 */
    private static String cab1(String etiqueta) {
        String linha = "<div class=\"movExpC1\">{" + etiqueta + "}</div>";
        return linha;    
    }
	
	/**
	 * Metodo para guardar el movimiento con una determinada clase css para que se muestren los datos como cabecera principal.
	 * Se diferencia del anterior en que la cadena se construye con String.format()
	 * @param etiqueta
	 * @return 
	 */
	private static String cabb1(String etiqueta){
		return String.format("<div class=\"movExpC1\">{%s}</div>", etiqueta);
	}
	
	/**
	 * Metodo para guardar el movimiento con una determinada clase css para que se muestren los datos como cabecera secundaria
	 * @param etiqueta
	 * @return 
	 */
    private static String cab2(String etiqueta) {
        
        String linha = "<div class=\"movExpC2\">{" + etiqueta + "}</div>";
        
        return linha;
    }

    private static String cab3(String etiqueta) {
        
        String linha = "<div class=\"movExpC3\">{" + etiqueta + "}</div>";
        
        return linha;
    }

    private static String linha(String etiqueta,String valor) {
        
        if (valor == null || "".equals(valor.trim()) || "null".equalsIgnoreCase(valor.trim()))
            valor = "--";

        String linha = "<div class=\"movExpLin\"><div class=\"movExpEtiq\">{" + etiqueta + 
                "}:</div><div class=\"movExpVal\">" + valor + "</div></div>";
        
        return linha;
    }
	
	private static String linea(String etiqueta, String valor){
		if (StringUtils.isNullOrEmpty(valor)){
			valor = "--";
		}
		return String.format("<div class=\"movExpLin\"><div class=\"movExpEtiq\">{%s}:</div><div class=\"movExpVal\">%s</div></div>", etiqueta,valor);
				
				
	}

    private static String linhaCampoSupl(String etiqueta,String valor) {
        
        if (valor == null || "".equals(valor.trim()) || "null".equalsIgnoreCase(valor.trim()))
            valor = "--";

        String linha = "<div class=\"movExpLin\"><div class=\"movExpEtiq\">" + etiqueta + 
                ":</div><div class=\"movExpVal\">" + valor + "</div></div>";
        
        return linha;
    }

    private static String linhaCampoSuplFich(String etiqueta, String valorNombre, String valorTipoMime) {
        
        if (valorNombre == null || "".equals(valorNombre.trim()) || "null".equalsIgnoreCase(valorNombre.trim())) {
            valorNombre = "--";
            valorTipoMime = "";
        } else if (valorTipoMime != null && !"".equals(valorTipoMime.trim()) && !"null".equalsIgnoreCase(valorTipoMime.trim())) {
            valorTipoMime = String.format(" (%s)", valorTipoMime);
        }
        
        String linha = "<div class=\"movExpLin\"><div class=\"movExpEtiq\">" + etiqueta + 
                ":</div><div class=\"movExpVal\">" + valorNombre + valorTipoMime + "</div></div>";
        
        return linha;
    }
    
    private static String linha(String etiqueta,int valorNum) {
        String linha = "<div class=\"movExpLin\"><div class=\"movExpEtiq\">{" + etiqueta + 
                "}:</div><div class=\"movExpVal\">" + valorNum + "</div></div>";
        
        return linha;
    }
}
