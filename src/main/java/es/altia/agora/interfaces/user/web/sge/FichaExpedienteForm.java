package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.administracion.mantenimiento.TipoDocumentoVO;
import es.altia.agora.business.sge.AsientoFichaExpedienteVO;
import es.altia.agora.business.sge.ExpedienteOtroDocumentoVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.sge.FicheroVO;
import es.altia.agora.business.sge.InteresadoExpedienteVO;
import es.altia.agora.business.sge.RolVO;
import es.altia.agora.business.sge.OperacionExpedienteVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.business.comunicaciones.vo.ComunicacionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.notificacion.vo.NotificacionVO;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;


/** Clase utilizada para capturar o mostrar el estado del BuzonEntradaSGE */
public class FichaExpedienteForm extends ActionForm {
    //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(FichaExpedienteForm.class.getName());
    private String numExpediente;
    private String procedimiento;
    private String titular;
    private String documentoTitular;
    private String localizacion;
    private String fechaInicio;
    private String fechaFin;
    private String usuario;
    private String codUnidadOrganicaExp;
    private String descUnidadOrganicaExp;
    private String observaciones;
    private String asunto;
    private String respOpcion;
    private String poseeLocalizacion;
    private String codLocalizacion;
    private Vector permisosTramites;
    private Vector tramites;
    private ArrayList<AsientoFichaExpedienteVO> asientos;    
    private Vector documentos;
    private Vector enlaces;
    private Vector estructuraDatosSuplementarios;
    private Vector valoresDatosSuplementarios;
    private Vector tramitesDisponibles;
    //private Vector<InteresadoExpedienteVO> terceros;
    private ArrayList<InteresadoExpedienteVO> terceros;
    private Vector listaUnidadesUsuario;
    private Vector listaTiposDocumentos = new Vector();
    private Collection formularios;
    private GeneralValueObject listaFicheros = new GeneralValueObject();
    private GeneralValueObject listaTiposFicheros = new GeneralValueObject();
    private GeneralValueObject listaNombreFicheros = new GeneralValueObject();
    private GeneralValueObject listaEstadoFicheros = new GeneralValueObject();    
    private GeneralValueObject listaRutaFicherosDisco = new GeneralValueObject();    
    private GeneralValueObject listaLongitudFicherosDisco = new GeneralValueObject();    
    private GeneralValueObject listaMetadatosFicheros = new GeneralValueObject();
    
    private String mensajeSW;
    private GeneralValueObject expedienteVO = new GeneralValueObject();
    
    private Vector<FicheroVO> ficherosExpediente;
    private Vector<FicheroVO> ficherosRegistroEntrada;
    private Vector<FicheroVO> ficherosRegistroSalida;
    private Vector<FicheroVO> ficherosTramites;
    private HashMap<String,FicheroVO> relacionFicheros;
    private ArrayList<RolVO> listaRoles;
    private ArrayList<ExpedienteOtroDocumentoVO> documentosExternosExpediente;

    // Nuevo
    private String codDocInteresado;
    private String documentoInteresado;
    private String lineasPagina;
    private String pagina;
    // Para unidades tramitadoras de un trámite
    private String listaCodTramites;
    private String listaModoTramites;
    private String listaUtrTramites;
    private String listaNotificacionesElectronica;
    
    //Lista del número de firmas para cada documento 
    private List<Integer> listaFirmasDocumentos;
    private Boolean firmaValida;
    private Boolean firmanteNombre;
    private Boolean firmanteNIF;
    private Boolean firmanteCertificado;
    
    
    private String nombreFirmante;
    private String nifFirmante;
    private String emisorCertificado;
    private String validezCertificado;
    private String datosCertificado;
    private Boolean verificacionFirmaComunicacion;

    // Lista con los trámites abiertos  por uno dado.
    private ArrayList<TramitacionExpedientesValueObject> tramitesDestino = new ArrayList<TramitacionExpedientesValueObject>();

    private ArrayList<ModuloIntegracionExterno> modulosExternos;

    private ArrayList<String> operacionesJavascript = null;
    
    //#72851 INICIO
    private List<ComunicacionVO> listaComunicaciones = new Vector<ComunicacionVO>();
    private Long idComunicacion;
    private ComunicacionVO comunicacionVO;

    private ArrayList<NotificacionVO> notificaciones = null;
    private Vector listaAgrupacionesCampos = new Vector();
    private Boolean cargarVista = false;
    
    private String jsonEstructuraCamposSuplementarios;

    //check de notificaciones de solo lectura o no
    private boolean readOnlyCheck = false;
	
	//check de notificaciones activo por defecto o no
    private boolean checkNotifActivo = false;
    
    private boolean expHistorico;
    
	private ArrayList <OperacionExpedienteVO> listaOperacionesExpediente;

    private ArrayList<String> codigosUorsPermisoUsuario = null;
    
    private ArrayList<TipoDocumentoVO> listaTiposDocumentosTerceros;
    
    // #253692: indica si se va a mostrar la ubicación física de la documentación
    private boolean ubicacionDocumentacionVis;
    private String ubicacionDoc;
    

    public boolean isExpHistorico() {
        return expHistorico;
    }

    public void setExpHistorico(boolean expHistorico) {
        this.expHistorico = expHistorico;
    }

    public ArrayList<OperacionExpedienteVO> getListaOperacionesExpediente() {
        return listaOperacionesExpediente;
    }

    public void setListaOperacionesExpediente(ArrayList<OperacionExpedienteVO> listaOperacionesExpediente) {
        this.listaOperacionesExpediente = listaOperacionesExpediente;
    }

    public ComunicacionVO getComunicacionVO() {
        return comunicacionVO;
    }

    public void setComunicacionVO(ComunicacionVO comunicacionVO) {
        this.comunicacionVO = comunicacionVO;
    }
        
    public Long getIdComunicacion() {
        return idComunicacion;
    }

    public void setIdComunicacion(Long idComunicacion) {
        this.idComunicacion = idComunicacion;
    }

    public List<ComunicacionVO> getListaComunicaciones() {
        return listaComunicaciones;
    }

    public void setListaComunicaciones(List<ComunicacionVO> listaComunicaciones) {
        this.listaComunicaciones = listaComunicaciones;
    }

    //#72851 FIN
    
    public ArrayList<ModuloIntegracionExterno> getModulosExternos(){
        return this.modulosExternos;
    }

    public void setModulosExternos(ArrayList<ModuloIntegracionExterno> modulos){
        this.modulosExternos = modulos;
    }

    public ArrayList<RolVO> getListaRoles(){
        return listaRoles;
    }

    public void setListaRoles(ArrayList<RolVO> lista){
        this.listaRoles = lista;
    }

    public String getRefCatastral() {
        return (String) expedienteVO.getAtributo("refCatastral");
    }

    public void setRefCatastral(String valor) {
        expedienteVO.setAtributo("refCatastral", valor);
    }

    public String getNumExpediente() {
        return (String) expedienteVO.getAtributo("numero");
    }

    public void setNumExpediente(String numExpediente) {
        expedienteVO.setAtributo("numero", numExpediente);
    }

    public String getProcedimiento() {
        return (String) expedienteVO.getAtributo("nombreProcedimiento");
    }

    public void setProcedimiento(String procedimiento) {
        expedienteVO.setAtributo("nombreProcedimiento", procedimiento);
    }

    public String getTitular() {
        return (String) expedienteVO.getAtributo("titular");
    }

    public void setTitular(String titular) {
        expedienteVO.setAtributo("titular", titular);
    }
    
       public String getDocumentoTitular() {
        return (String) expedienteVO.getAtributo("documentoTitular");
    }

    public void setDocumentoTitular(String titular) {
        expedienteVO.setAtributo("documentoTitular", titular);
    }

    public String getLocalizacion() {
        return (String) expedienteVO.getAtributo("localizacion");
    }
    
    public String getDomicilio() {
        return (String) expedienteVO.getAtributo("domicilio");
    }

    public void setLocalizacion(String localizacion) {
        expedienteVO.setAtributo("localizacion", localizacion);
    }
    
    public void setDomicilio(String domicilio) {
        expedienteVO.setAtributo("domicilio", domicilio);
    }

    public String getFechaInicio() {
        return (String) expedienteVO.getAtributo("fechaInicioExpediente");
    }

    public void setFechaInicio(String fechaInicio) {
        expedienteVO.setAtributo("fechaInicioExpediente", fechaInicio);
    }

    public String getFechaFin() {
        return (String) expedienteVO.getAtributo("fechaFinExpediente");
    }

    public void setFechaFin(String fechaFin) {
        expedienteVO.setAtributo("fechaFinExpediente", fechaFin);
    }

    public String getUsuario() {
        return (String) expedienteVO.getAtributo("usuarioExpediente");
    }

    public void setUsuario(String usuario) {
        expedienteVO.setAtributo("usuarioExpediente", usuario);
    }

    public String getCodUnidadOrganicaExp() {
        return (String) expedienteVO.getAtributo("codUnidadOrganicaExp");
    }

    public void setCodUnidadOrganicaExp(String codUnidadOrganicaExp) {
        expedienteVO.setAtributo("codUnidadOrganicaExp", codUnidadOrganicaExp);
    }

    public String getDescUnidadOrganicaExp() {
        return (String) expedienteVO.getAtributo("descUnidadOrganicaExp");
    }

    public void setDescUnidadOrganicaExp(String descUnidadOrganicaExp) {
        expedienteVO.setAtributo("descUnidadOrganicaExp", descUnidadOrganicaExp);
    }

    public String getObservaciones() {
        return (String) expedienteVO.getAtributo("observaciones");
    }

    public void setObservaciones(String observaciones) {
        expedienteVO.setAtributo("observaciones", observaciones);
    }

    public String getAsunto() {
        return (String) expedienteVO.getAtributo("asunto");
    }

    public void setAsunto(String asunto) {
        expedienteVO.setAtributo("asunto", asunto);
    }

    public String getRespOpcion() {
        return (String) expedienteVO.getAtributo("respOpcion");
    }

    public void setRespOpcion(String respOpcion) {
        expedienteVO.setAtributo("respOpcion", respOpcion);
    }

    public String getPoseeLocalizacion() {
        return (String) expedienteVO.getAtributo("poseeLocalizacion");
    }

    public void setPoseeLocalizacion(String poseeLocalizacion) {
        expedienteVO.setAtributo("poseeLocalizacion", poseeLocalizacion);
    }

    public String getCodLocalizacion() {
        return (String) expedienteVO.getAtributo("codLocalizacion");
    }

    public void setCodLocalizacion(String codLocalizacion) {
        expedienteVO.setAtributo("codLocalizacion", codLocalizacion);
    }

    public Vector getTramites() {
        return tramites;
    }

    public void setTramites(Vector tramites) {
        this.tramites = tramites;
    }

    public ArrayList<AsientoFichaExpedienteVO> getAsientos() {
        return asientos;
    }

    public void setAsientos(ArrayList<AsientoFichaExpedienteVO> asientos) {
        this.asientos = asientos;
    }

    public Vector getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Vector documentos) {
        this.documentos = documentos;
    }

    public Vector getEnlaces() {
        return enlaces;
    }

    public void setEnlaces(Vector enlaces) {
        this.enlaces = enlaces;
    }

    public Vector getEstructuraDatosSuplementarios() {
        return estructuraDatosSuplementarios;
    }

    public void setEstructuraDatosSuplementarios(Vector estructuraDatosSuplementarios) {
        this.estructuraDatosSuplementarios = estructuraDatosSuplementarios;
    }

    public Vector getValoresDatosSuplementarios() {
        return valoresDatosSuplementarios;
    }

    public void setValoresDatosSuplementarios(Vector valoresDatosSuplementarios) {
        this.valoresDatosSuplementarios = valoresDatosSuplementarios;
    }

    public Vector getTramitesDisponibles() {
        return tramitesDisponibles;
    }

    public void setTramitesDisponibles(Vector tramitesDisponibles) {
        this.tramitesDisponibles = tramitesDisponibles;
    }
    
    public ArrayList<InteresadoExpedienteVO> getTerceros() {
        return terceros;
    }

    public void setTerceros(ArrayList<InteresadoExpedienteVO> lista_terceros) {
        this.terceros = lista_terceros;
    }

    public String getCodMunicipio() {
        return (String) expedienteVO.getAtributo("codMunicipio");
    }

    public void setCodMunicipio(String codMunicipio) {
        expedienteVO.setAtributo("codMunicipio", codMunicipio);
    }

    public String getCodProcedimiento() {
        return (String) expedienteVO.getAtributo("codProcedimiento");
    }

    public void setCodProcedimiento(String codProcedimiento) {
        expedienteVO.setAtributo("codProcedimiento", codProcedimiento);
    }

    public String getCodTramite() {
        return (String) expedienteVO.getAtributo("codTramite");
    }

    public void setCodTramite(String codTramite) {
        expedienteVO.setAtributo("codTramite", codTramite);
    }

    public String getEjercicio() {
        return (String) expedienteVO.getAtributo("ejercicio");
    }

    public void setEjercicio(String ejercicio) {
        expedienteVO.setAtributo("ejercicio", ejercicio);
    }

    public String getNumero() {
        return (String) expedienteVO.getAtributo("numero");
    }

    public void setNumero(String numero) {
        expedienteVO.setAtributo("numero", numero);
    }

    public String getCodMunExpIni() {
        return (String) expedienteVO.getAtributo("codMunExpIni");
    }

    public void setCodMunExpIni(String codMunExpIni) {
        expedienteVO.setAtributo("codMunExpIni", codMunExpIni);
    }

    public String getEjercicioExpIni() {
        return (String) expedienteVO.getAtributo("ejercicioExpIni");
    }

    public void setEjercicioExpIni(String ejercicioExpIni) {
        expedienteVO.setAtributo("ejercicioExpIni", ejercicioExpIni);
    }

    public String getNumeroExpIni() {
        return (String) expedienteVO.getAtributo("numeroExpIni");
    }

    public void setNumeroExpIni(String numeroExpIni) {
        expedienteVO.setAtributo("numeroExpIni", numeroExpIni);
    }

    public String getNotificacionRealizada() {
        return (String) expedienteVO.getAtributo("notificacionRealizada");
    }

    public void setNotificacionRealizada(String notificacionRealizada) {
        expedienteVO.setAtributo("notificacionRealizada", notificacionRealizada);
    }

    public Vector getListaUnidadesUsuario() {
        return listaUnidadesUsuario;
    }

    public void setListaUnidadesUsuario(Vector listaUnidadesUsuario) {
        this.listaUnidadesUsuario = listaUnidadesUsuario;
    }

    public Collection getFormularios() {
        return formularios;
    }

    public void setFormularios(Collection formularios) {
        this.formularios = formularios;
    }

    public GeneralValueObject getListaFicheros() {
        return listaFicheros;
    }

    public void setListaFicheros(GeneralValueObject listaFicheros) {
        this.listaFicheros = listaFicheros;
    }

    public GeneralValueObject getListaTiposFicheros() {
        return listaTiposFicheros;
    }

    public void setListaTiposFicheros(GeneralValueObject listaTiposFicheros) {
        this.listaTiposFicheros = listaTiposFicheros;
    }

    public GeneralValueObject getExpedienteVO() {
        return expedienteVO;
    }

    public void setExpedienteVO(GeneralValueObject expedienteVO) {
        this.expedienteVO = expedienteVO;
    }

    public Vector getPermisosTramites() {
        return permisosTramites;
    }

    public void setPermisosTramites(Vector permisosTramites) {
        this.permisosTramites = permisosTramites;
    }

    public String getMensajeSW() {
        return mensajeSW;
    }

    public void setMensajeSW(String mensajeSW) {
        this.mensajeSW = mensajeSW;
    }

    public GeneralValueObject getListaNombreFicheros() {
        return listaNombreFicheros;
    }

    public void setListaNombreFicheros(GeneralValueObject listaNombreFicheros) {
        this.listaNombreFicheros = listaNombreFicheros;
    }

    public GeneralValueObject getListaEstadoFicheros() {
        return listaEstadoFicheros;
    }

    public void setListaEstadoFicheros(GeneralValueObject listaEstadoFicheros) {
        this.listaEstadoFicheros = listaEstadoFicheros;
    }
    
    public Vector<FicheroVO> getFicherosExpediente() {
        return ficherosExpediente;
    }

    public void setFicherosExpediente(Vector<FicheroVO> ficherosExpediente) {
        this.ficherosExpediente = ficherosExpediente;
    }

    public Vector<FicheroVO> getFicherosRegistroEntrada() {
        return ficherosRegistroEntrada;
    }

    public void setFicherosRegistroEntrada(Vector<FicheroVO> ficherosRegistroEntrada) {
        this.ficherosRegistroEntrada = ficherosRegistroEntrada;
    }

    public Vector<FicheroVO> getFicherosRegistroSalida() {
        return ficherosRegistroSalida;
    }

    public void setFicherosRegistroSalida(Vector<FicheroVO> ficherosRegistroSalida) {
        this.ficherosRegistroSalida = ficherosRegistroSalida;
    }

    public Vector<FicheroVO> getFicherosTramites() {
        return ficherosTramites;
    }

    public void setFicherosTramites(Vector<FicheroVO> ficherosTramites) {
        this.ficherosTramites = ficherosTramites;
    }

    public HashMap<String, FicheroVO> getRelacionFicheros() {
        return relacionFicheros;
    }

    public void setRelacionFicheros(HashMap<String, FicheroVO> relacionFicheros) {
        this.relacionFicheros = relacionFicheros;
    }

    /**
     * @return the codDocInteresado
     */
    public String getCodDocInteresado() {
        return codDocInteresado;
    }

    /**
     * @param codDocInteresado the codDocInteresado to set
     */
    public void setCodDocInteresado(String codDocInteresado) {
        this.codDocInteresado = codDocInteresado;
    }

    /**
     * @return the documentoInteresado
     */
    public String getDocumentoInteresado() {
        return documentoInteresado;
    }

    /**
     * @param documentoInteresado the documentoInteresado to set
     */
    public void setDocumentoInteresado(String documentoInteresado) {
        this.documentoInteresado = documentoInteresado;
    }

    /**
     * @return the listaTiposDocumentos
     */
    public Vector getListaTiposDocumentos() {
        return listaTiposDocumentos;
    }

    /**
     * @param listaTiposDocumentos the listaTiposDocumentos to set
     */
    public void setListaTiposDocumentos(Vector listaTiposDocumentos) {
        this.listaTiposDocumentos = listaTiposDocumentos;
    }

    /**
     * @return the lineasPagina
     */
    public String getLineasPagina() {
        return lineasPagina;
    }

    /**
     * @param lineasPagina the lineasPagina to set
     */
    public void setLineasPagina(String lineasPagina) {
        this.lineasPagina = lineasPagina;
    }

    /**
     * @return the pagina
     */
    public String getPagina() {
        return pagina;
    }

    /**
     * @param pagina the pagina to set
     */
    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    /**
     * @return the listaCodTramites
     */
    public String getListaCodTramites() {
        return listaCodTramites;
    }

    /**
     * @param listaCodTramites the listaCodTramites to set
     */
    public void setListaCodTramites(String listaCodTramites) {
        this.listaCodTramites = listaCodTramites;
    }

    /**
     * @return the listaModoTramites
     */
    public String getListaModoTramites() {
        return listaModoTramites;
    }

    /**
     * @param listaModoTramites the listaModoTramites to set
     */
    public void setListaModoTramites(String listaModoTramites) {
        this.listaModoTramites = listaModoTramites;
    }

    /**
     * @return the listaUtrTramites
     */
    public String getListaUtrTramites() {
        return listaUtrTramites;
    }

    /**
     * @param listaUtrTramites the listaUtrTramites to set
     */
    public void setListaUtrTramites(String listaUtrTramites) {
        this.listaUtrTramites = listaUtrTramites;
    }

    /**
     * @return the tramitesAbiertos
     */
    public ArrayList<TramitacionExpedientesValueObject> getTramitesDestino() {
        return tramitesDestino;
    }

    /**
     * @param tramitesAbiertos the tramitesAbiertos to set
     */
    public void setTramitesDestino(ArrayList<TramitacionExpedientesValueObject> tramitesAbiertos) {
        this.tramitesDestino = tramitesAbiertos;
    }

    /**
     * @return the listaNotificacionesElectronica
     */
    public String getListaNotificacionesElectronica() {
        return listaNotificacionesElectronica;
    }

    /**
     * @param listaNotificacionesElectronica the listaNotificacionesElectronica to set
     */
    public void setListaNotificacionesElectronica(String listaNotificacionesElectronica) {
        this.listaNotificacionesElectronica = listaNotificacionesElectronica;
    }

    /**
     * @return the operacionesJavascript
     */
    public ArrayList<String> getOperacionesJavascript() {
        return operacionesJavascript;
    }

    /**
     * @param operacionesJavascript the operacionesJavascript to set
     */
    public void setOperacionesJavascript(ArrayList<String> operacionesJavascript) {
        this.operacionesJavascript = operacionesJavascript;
    }

    public List<Integer> getListaFirmasDocumentos() {
        return listaFirmasDocumentos;
    }

    public void setListaFirmasDocumentos(List<Integer> listaFirmasDocumentos) {
        this.listaFirmasDocumentos = listaFirmasDocumentos;
    }

    public Boolean getFirmaValida() {
        return firmaValida;
    }

    public void setFirmaValida(Boolean firmaValida) {
        this.firmaValida = firmaValida;
    }

    public Boolean getFirmanteCertificado() {
        return firmanteCertificado;
    }

    public void setFirmanteCertificado(Boolean firmanteCertificado) {
        this.firmanteCertificado = firmanteCertificado;
    }

    public Boolean getFirmanteNIF() {
        return firmanteNIF;
    }

    public void setFirmanteNIF(Boolean firmanteNIF) {
        this.firmanteNIF = firmanteNIF;
    }

    public Boolean getFirmanteNombre() {
        return firmanteNombre;
    }

    public void setFirmanteNombre(Boolean firmanteNombre) {
        this.firmanteNombre = firmanteNombre;
    }

    public String getDatosCertificado() {
        return datosCertificado;
    }

    public void setDatosCertificado(String datosCertificado) {
        this.datosCertificado = datosCertificado;
    }

    public String getEmisorCertificado() {
        return emisorCertificado;
    }

    public void setEmisorCertificado(String emisorCertificado) {
        this.emisorCertificado = emisorCertificado;
    }

    public String getNifFirmante() {
        return nifFirmante;
    }

    public void setNifFirmante(String nifFirmante) {
        this.nifFirmante = nifFirmante;
    }

    public String getNombreFirmante() {
        return nombreFirmante;
    }

    public void setNombreFirmante(String nombreFirmante) {
        this.nombreFirmante = nombreFirmante;
    }

    public String getValidezCertificado() {
        return validezCertificado;
    }

    public void setValidezCertificado(String validezCertificado) {
        this.validezCertificado = validezCertificado;
    }
    
 
    public Boolean getVerificacionFirmaComunicacion(){
        return this.verificacionFirmaComunicacion;
    }
    
    
    public void setVerificacionFirmaComunicacion(Boolean flag){
        this.verificacionFirmaComunicacion = flag;
    }

    /**
     * @return the notificaciones
     */
    public ArrayList<NotificacionVO> getNotificaciones() {
        return notificaciones;
    }

    /**
     * @param notificaciones the notificaciones to set
     */
    public void setNotificaciones(ArrayList<NotificacionVO> notificaciones) {
        this.notificaciones = notificaciones;
    }
    
    public Vector getListaAgrupacionesCampos() {
        return listaAgrupacionesCampos;
    }
    public void setListaAgrupacionesCampos(Vector listaAgrupacionesCampos) {
        this.listaAgrupacionesCampos = listaAgrupacionesCampos;
    }

    public Boolean getCargarVista() {
        return cargarVista;
    }
    public void setCargarVista(Boolean cargarVista) {
        this.cargarVista = cargarVista;
    }

    /**
     * @return the documentosExternosExpediente
     */
    public ArrayList<ExpedienteOtroDocumentoVO> getDocumentosExternosExpediente() {
        return documentosExternosExpediente;
    }

    /**
     * @param documentosExternosExpediente the documentosExternosExpediente to set
     */
    public void setDocumentosExternosExpediente(ArrayList<ExpedienteOtroDocumentoVO> documentosExternosExpediente) {
        this.documentosExternosExpediente = documentosExternosExpediente;
    }
    
    
    public boolean isInteresadoObligatorio(){
        boolean exito = false;
        
        try{
            String interesadoObligatorio = (String)this.expedienteVO.getAtributo("interesadoObligatorio");
            if(interesadoObligatorio!=null && "SI".equalsIgnoreCase(interesadoObligatorio)){
                exito = true;
            }
        }catch(Exception e){
            exito = false;
        }
        
        return exito;
    }

    /**
     * @return the listaRutaFicherosDisco
     */
    public GeneralValueObject getListaRutaFicherosDisco() {
        return listaRutaFicherosDisco;
    }

    /**
     * @param listaRutaFicherosDisco the listaRutaFicherosDisco to set
     */
    public void setListaRutaFicherosDisco(GeneralValueObject listaRutaFicherosDisco) {
        this.listaRutaFicherosDisco = listaRutaFicherosDisco;
    }

    /**
     * @return the listaLongitudFicherosDisco
     */
    public GeneralValueObject getListaLongitudFicherosDisco() {
        return listaLongitudFicherosDisco;
    }

    /**
     * @param listaLongitudFicherosDisco the listaLongitudFicherosDisco to set
     */
    public void setListaLongitudFicherosDisco(GeneralValueObject listaLongitudFicherosDisco) {
        this.listaLongitudFicherosDisco = listaLongitudFicherosDisco;
    }
   
    public GeneralValueObject getListaMetadatosFicheros() {
        return listaMetadatosFicheros;
    }

    public void setListaMetadatosFicheros(GeneralValueObject listaMetadatosFicheros) {
        this.listaMetadatosFicheros = listaMetadatosFicheros;
    }
    
    public String getJsonEstructuraCamposSuplementarios() {
        return jsonEstructuraCamposSuplementarios;
    }

    public void setJsonEstructuraCamposSuplementarios(String jsonEstructuraCamposSuplementarios) {
        this.jsonEstructuraCamposSuplementarios = jsonEstructuraCamposSuplementarios;
    }
    
    
   /**
     * @return the readOnlyCheck
     */
    public boolean isReadOnlyCheck() {
        return readOnlyCheck;
    }

    /**
     * @param readOnlyCheck the readOnlyCheck to set
     */
    public void setReadOnlyCheck(boolean readOnlyCheck) {
        this.readOnlyCheck = readOnlyCheck;
    }

    /**
     * @return the codigosUorsPermisoUsuario
     */
    public ArrayList<String> getCodigosUorsPermisoUsuario() {
        return codigosUorsPermisoUsuario;
    }

    /**
     * @param codigosUorsPermisoUsuario the codigosUorsPermisoUsuario to set
     */
    public void setCodigosUorsPermisoUsuario(ArrayList<String> codigosUorsPermisoUsuario) {
        this.codigosUorsPermisoUsuario = codigosUorsPermisoUsuario;
    }

    /**
     * @return the listaTiposDocumentosTerceros
     */
    public ArrayList<TipoDocumentoVO> getListaTiposDocumentosTerceros() {
        return listaTiposDocumentosTerceros;
    }

    /**
     * @param listaTiposDocumentosTerceros the listaTiposDocumentosTerceros to set
     */
    public void setListaTiposDocumentosTerceros(ArrayList<TipoDocumentoVO> listaTiposDocumentosTerceros) {
        this.listaTiposDocumentosTerceros = listaTiposDocumentosTerceros;
    }

    /**
     * @return the ubicacionDocumentacionVis
     */
    public boolean isUbicacionDocumentacionVis() {
        return ubicacionDocumentacionVis;
    }

    /**
     * @param ubicacionDocumentacionVis the ubicacionDocumentacionVis to set
     */
    public void setUbicacionDocumentacionVis(boolean ubicacionDocumentacionVis) {
        this.ubicacionDocumentacionVis = ubicacionDocumentacionVis;
    }

    /**
     * @return the ubicacionDoc
     */
    public String getUbicacionDoc() {
        return ubicacionDoc;
    }

    /**
     * @param ubicacionDoc the ubicacionDocumentacion to set
     */
    public void setUbicacionDoc(String ubicacionDoc) {
        this.ubicacionDoc = ubicacionDoc;
    }

	/**
	 * @return the checkNotifActivo
	 */
	public boolean isCheckNotifActivo() {
		return checkNotifActivo;
	}

	/**
	 * @param checkNotifActivo the checkNotifActivo to set
	 */
	public void setCheckNotifActivo(boolean checkNotifActivo) {
		this.checkNotifActivo = checkNotifActivo;
	}
}