package es.altia.agora.business.sge.plugin.documentos.vo;

import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author oscar.rodriguez
 */
public class DocumentoGestor implements Documento{
    private int codUsuario;
    private int codTramite;
    private int codTramiteVisible;
    private int ocurrenciaTramite;
    private int ejercicio;
    private int codMunicipio;
    private String codProcedimiento;
    private String numeroExpediente;
    private String numeroDocumento;
    private String nombreDocumento;
    private String extension;
    private int codDocumento;
    private byte[] fichero;
    private String[] params;
    private boolean docRelacion;
    private String numeroRelacion;
    private String opcionGrabar;
    private String nombreProcedimiento;
    private String nombreTramite;
    private String nombreOrganizacion;
    private String urlGestor;
    private String usuarioGestor;
    private String paswordGestor;
    private String carpetaRaiz;
    private String GUION =  "-";
    private String BARRA =  "/";
    private String nuevoNombreFichero;
    private String implClassGestor;
    private String REL = "REL_";
    private String GUION_BAJO = "_";
    private int longitudNumeroDocumentoBD;
    private int longitudOcurrenciaTramiteInternoBD;
    private int longitudCodVisibleTramiteBD;
    private String nuevoNombreFicheroCompleto;
    private String nombreFicheroCompleto;
    private ArrayList<String> listaExpedientes;
    private String tipoMimeContenido;
    private String codificacionContenido;
    private String origen;
    private boolean eliminarSoloAdjunto;
    private boolean modificarAdjuntoDocExpediente;
    private ArrayList<String> listaCarpetas = null;
    private int longitudDocumento;  
    private String rutaDocumento;
    
    //parametro añadido para indicar la procedencia de la petición del documento 
    private boolean desdeNotificacion;
     
    private long idDocumento;
    // Atributos para documento de tipo registro
    private int codigoDepartamento;   
    private int codigoUnidadOrganica;     
    private int ejercicioAnotacion;            
    private long numeroRegistro;          
    private String tipoRegistro;         
    private String tipoDocumento;   
    private String fechaDocumento; 
    private String entregado;    
    private boolean documentoRegistro = false;
    private Integer estadoDocumentoRegistro;    
    private String codTipoDato;
    private String observaciones;
    private int idFirma;
    private boolean documentoProcedimientoSinValor;
    private boolean expHistorico;
    
    private String idDocGestor;
    
    //Metadatos de cotejo del documento
    private String versionNTIMetadatos;
    private Long idDocumentoMetadatos;
    private String organoMetadatos;
    private Calendar fechaCapturaMetadatos;
    private Integer origenMetadatos;
    private Integer estadoElaboracionMetadatos;
    private String nombreFormatoMetadatos;
    private Integer tipoDocumentalMetadatos;
    private Integer tipoFirmaMetadatos;
    
    // Metadatos de los documentos
    private Long idMetadatoDocumento;
    private Boolean insertarMetadatosEnBBDD;
    // Metadatos de los documentos: CSV
    private String metadatoDocumentoCsv;
    private String metadatoDocumentoCsvAplicacion;
    private String metadatoDocumentoCsvUri;

    public boolean isExpHistorico() {
        return expHistorico;
    }

    public void setExpHistorico(boolean expHistorico) {
        this.expHistorico = expHistorico;
    }
    
    
    public boolean esDocumentoProcedimientoSinValor(){
        return this.documentoProcedimientoSinValor;
    }
    
    public void setDocumentoProcedimientoSinValor(boolean documentoRegistroProcedimiento){
        this.documentoProcedimientoSinValor = documentoRegistroProcedimiento;
    }
        
    public int getIdFirma(){
        return this.idFirma;
    }
    
    public void setIdFirma(int idFirma){
        this.idFirma = idFirma;
    }
    
    
    public String getObservaciones(){
        return this.observaciones;
    }
    
    public void setObservaciones(String observaciones){
        this.observaciones = observaciones;
    }

    
    public String getRutaDocumento(){
        return this.rutaDocumento;
    }
       
    
    public void setRutaDocumento(String rutaDocumento){
        this.rutaDocumento = rutaDocumento;
    }

    public ArrayList<String> getListaCarpetas(){
        return this.listaCarpetas;
    }

    public void setListaCarpetas(ArrayList<String> carpetas){
        this.listaCarpetas = carpetas;
    }
    
    /**
     * @return the codUsuario
     */
    public int getCodUsuario() {
        return codUsuario;
    }

    /**
     * @param codUsuario the codUsuario to set
     */
    public void setCodUsuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }

    /**
     * @return the ocurrenciaTramite
     */
    public int getOcurrenciaTramite() {
        return ocurrenciaTramite;
    }

    /**
     * @param ocurrenciaTramite the ocurrenciaTramite to set
     */
    public void setOcurrenciaTramite(int ocurrenciaTramite) {
        this.ocurrenciaTramite = ocurrenciaTramite;
    }

    /**
     * @return the ejercicicio
     */
    public int getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicicio the ejercicicio to set
     */
    public void setEjercicio(int ejercicicio) {
        this.ejercicio = ejercicicio;
    }

    /**
     * @return the codMunicipio
     */
    public int getCodMunicipio() {
        return codMunicipio;
    }

    /**
     * @param codMunicipio the codMunicipio to set
     */
    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    /**
     * @return the codProcedimiento
     */
    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    /**
     * @param codProcedimiento the codProcedimiento to set
     */
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    /**
     * @return the numeroExpediente
     */
    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    /**
     * @param numeroExpediente the numeroExpediente to set
     */
    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    /**
     * @return the nombreDocumento
     */
    public String getNombreDocumento() {
        return nombreDocumento;
    }

    /**
     * @param nombreDocumento the nombreDocumento to set
     */
    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
        setNuevoNombreFichero(nombreDocumento);
    }

    /**
     * @return the codDocumento
     */
    public int getCodDocumento() {
        return codDocumento;
    }

    /**
     * @param codDocumento the codDocumento to set
     */
    public void setCodDocumento(int codDocumento) {
        this.codDocumento = codDocumento;
    }

    /**
     * @return the fichero
     */
    public byte[] getFichero() {
        return fichero;
    }

    /**
     * @param fichero the fichero to set
     */
    public void setFichero(byte[] fichero) {
        this.fichero = fichero;
    }

    /**
     * @return the params
     */
    public String[] getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(String[] params) {
        this.params = params;
    }

    /**
     * @return the numeroDocumento
     */
    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * @param numeroDocumento the numeroDocumento to set
     */
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    /**
     * @return the docRelacion
     */
    public boolean isDocRelacion() {
        return docRelacion;
    }

    /**
     * @param docRelacion the docRelacion to set
     */
    public void setDocRelacion(boolean docRelacion) {
        this.docRelacion = docRelacion;
    }

    /**
     * @return the numeroRelacion
     */
    public String getNumeroRelacion() {
        return numeroRelacion;
    }

    /**
     * @param numeroRelacion the numeroRelacion to set
     */
    public void setNumeroRelacion(String numeroRelacion) {
        this.numeroRelacion = numeroRelacion;
    }

    /**
     * @return the opcionGrabar
     */
    public String getOpcionGrabar() {
        return opcionGrabar;
    }

    /**
     * @param opcionGrabar the opcionGrabar to set
     */
    public void setOpcionGrabar(String opcionGrabar) {
        this.opcionGrabar = opcionGrabar;
    }

    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * @return the nombreProcedimiento
     */
    public String getNombreProcedimiento() {
        return nombreProcedimiento;
    }

    /**
     * @param nombreProcedimiento the nombreProcedimiento to set
     */
    public void setNombreProcedimiento(String nombreProcedimiento) {
        this.nombreProcedimiento = nombreProcedimiento;
    }

    /**
     * @return the nombreTramite
     */
    public String getNombreTramite() {
        return nombreTramite;
    }

    /**
     * @param nombreTramite the nombreTramite to set
     */
    public void setNombreTramite(String nombreTramite) {
        this.nombreTramite = nombreTramite;
    }


    /**
     * @return the nombreOrganizacion
     */
    public String getNombreOrganizacion() {
        return nombreOrganizacion;
    }

    /**
     * @param nombreOrganizacion the nombreOrganizacion to set
     */
    public void setNombreOrganizacion(String nombreOrganizacion) {
        this.nombreOrganizacion = nombreOrganizacion;
    }


    public String getNombreCarpetaOrganizacion(){
        String salida = "";
        if(nombreOrganizacion!=null && codMunicipio>=0){
            return codMunicipio + GUION + nombreOrganizacion;
        }
        return salida;
    }


    public String getNombreCarpetaProcedimiento(){
        String salida = "";
        if(codProcedimiento!=null && nombreProcedimiento!=null){
            return codProcedimiento + GUION + nombreProcedimiento;
        }
        return salida;
    }


    public String getNombreCarpetaExpediente(){
        if(!this.isDocRelacion()) // Si el documento no pertenece a una relación
            return numeroExpediente.replaceAll(BARRA,GUION);
        else // Si el documento pertenece a una relación
            return REL + numeroRelacion.replaceAll(BARRA,GUION);

    }


   public String getNombreFicheroCompleto(){
        return this.nombreFicheroCompleto;
    }

    /**
     * @return the urlGestor
     */
    public String getUrlGestor() {
        return urlGestor;
    }

    /**
     * @param urlGestor the urlGestor to set
     */
    public void setUrlGestor(String urlGestor) {
        this.urlGestor = urlGestor;
    }

    /**
     * @return the usuarioGestor
     */
    public String getUsuarioGestor() {
        return usuarioGestor;
    }

    /**
     * @param usuarioGestor the usuarioGestor to set
     */
    public void setUsuarioGestor(String usuarioGestor) {
        this.usuarioGestor = usuarioGestor;
    }

    /**
     * @return the paswordGestor
     */
    public String getPaswordGestor() {
        return paswordGestor;
    }

    /**
     * @param paswordGestor the paswordGestor to set
     */
    public void setPaswordGestor(String paswordGestor) {
        this.paswordGestor = paswordGestor;
    }

    /**
     * @return the carpetaRaiz
     */
    public String getCarpetaRaiz() {
        return carpetaRaiz;
    }

    /**
     * @param carpetaRaiz the carpetaRaiz to set
     */
    public void setCarpetaRaiz(String carpetaRaiz) {
        this.carpetaRaiz = carpetaRaiz;
    }

    /**
     * @return the nuevoNombreFichero
     */
    public String getNuevoNombreFichero() {
        return nuevoNombreFichero;
    }

    /**
     * @param nuevoNombreFichero the nuevoNombreFichero to set
     */
    public void setNuevoNombreFichero(String nuevoNombreFichero) {
        this.nuevoNombreFichero = nuevoNombreFichero;
    }

    public String getNuevoNombreFicheroCompleto(){
        return this.nuevoNombreFicheroCompleto;
    }

    /**
     * @return the implClassGestor
     */
    public String getImplClassGestor() {
        return implClassGestor;
    }

    /**
     * @param implClassGestor the implClassGestor to set
     */
    public void setImplClassGestor(String implClassGestor) {
        this.implClassGestor = implClassGestor;
    }

    /**
     * @return the longitudNumeroDocumentoBD
     */
    public int getLongitudNumeroDocumentoBD() {
        return longitudNumeroDocumentoBD;
    }

    /**
     * @param longitudNumeroDocumentoBD the longitudNumeroDocumentoBD to set
     */
    public void setLongitudNumeroDocumentoBD(int longitudNumeroDocumentoBD) {
        this.longitudNumeroDocumentoBD = longitudNumeroDocumentoBD;
    }

    /**
     * @return the longitudOcurrenciaTramiteInternoBD
     */
    public int getLongitudOcurrenciaTramiteInternoBD() {
        return longitudOcurrenciaTramiteInternoBD;
    }

    /**
     * @param longitudOcurrenciaTramiteInternoBD the longitudOcurrenciaTramiteInternoBD to set
     */
    public void setLongitudOcurrenciaTramiteInternoBD(int longitudOcurrenciaTramiteInternoBD) {
        this.longitudOcurrenciaTramiteInternoBD = longitudOcurrenciaTramiteInternoBD;
    }

    /**
     * @return the longitudCodVisibleTramiteBD
     */
    public int getLongitudCodVisibleTramiteBD() {
        return longitudCodVisibleTramiteBD;
    }

    /**
     * @param longitudCodVisibleTramiteBD the longitudCodVisibleTramiteBD to set
     */
    public void setLongitudCodVisibleTramiteBD(int longitudCodVisibleTramiteBD) {
        this.longitudCodVisibleTramiteBD = longitudCodVisibleTramiteBD;
    }

    /**
     * @return the codTramiteVisible
     */
    public int getCodTramiteVisible() {
        return codTramiteVisible;
    }

    /**
     * @param codTramiteVisible the codTramiteVisible to set
     */
    public void setCodTramiteVisible(int codTramiteVisible) {
        this.codTramiteVisible = codTramiteVisible;
    }

    /**
     * @return the codTramite
     */
    public int getCodTramite() {
        return codTramite;
    }

    /**
     * @param codTramite the codTramite to set
     */
    public void setCodTramite(int codTramite) {
        this.codTramite = codTramite;
    }

    /**
     * @param nuevoNombreFicheroCompleto the nuevoNombreFicheroCompleto to set
     */
    public void setNuevoNombreFicheroCompleto(String nuevoNombreFicheroCompleto) {
        this.nuevoNombreFicheroCompleto = nuevoNombreFicheroCompleto;
    }

    /**
     * @param nombreFicheroCompleto the nombreFicheroCompleto to set
     */
    public void setNombreFicheroCompleto(String nombreFicheroCompleto) {
        this.nombreFicheroCompleto = nombreFicheroCompleto;
    }

    /**
     * @return the listaExpedientes
     */
    public ArrayList<String> getListaExpedientes() {
        return listaExpedientes;
    }

    /**
     * @param listaExpedientes the listaExpedientes to set
     */
    public void setListaExpedientes(ArrayList<String> listaExpedientes) {
        this.listaExpedientes = listaExpedientes;
    }

    /**
     * @return the tipoMimeContenido
     */
    public String getTipoMimeContenido() {
        return tipoMimeContenido;
    }

    /**
     * @param tipoMimeContenido the tipoMimeContenido to set
     */
    public void setTipoMimeContenido(String tipoMimeContenido) {
        this.tipoMimeContenido = tipoMimeContenido;
    }

    /**
     * @return the codificacionContenido
     */
    public String getCodificacionContenido() {
        return codificacionContenido;
    }

    /**
     * @param codificacionContenido the codificacionContenido to set
     */
    public void setCodificacionContenido(String codificacionContenido) {
        this.codificacionContenido = codificacionContenido;
    }

    /**
     * @return the origen
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * @param origen the origen to set
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public boolean isEliminarSoloAdjunto(){
        return eliminarSoloAdjunto;
    }

    public void setEliminarSoloAdjunto(boolean flag){
        this.eliminarSoloAdjunto = flag;
    }

    public boolean isModificarAdjuntoDocExpediente(){
        return modificarAdjuntoDocExpediente;
    }

    public void setModificarAdjuntoDocExpediente(boolean flag){
        this.modificarAdjuntoDocExpediente = flag;
    }

    /**
     * @return the codigoDepartamento
     */
    public int getCodigoDepartamento() {
        return codigoDepartamento;
    }

    /**
     * @param codigoDepartamento the codigoDepartamento to set
     */
    public void setCodigoDepartamento(int codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    /**
     * @return the codigoUnidadOrganica
     */
    public int getCodigoUnidadOrganica() {
        return codigoUnidadOrganica;
    }

    /**
     * @param codigoUnidadOrganica the codigoUnidadOrganica to set
     */
    public void setCodigoUnidadOrganica(int codigoUnidadOrganica) {
        this.codigoUnidadOrganica = codigoUnidadOrganica;
    }

    /**
     * @return the ejercicioAnotacion
     */
    public int getEjercicioAnotacion() {
        return ejercicioAnotacion;
    }

    /**
     * @param ejercicioAnotacion the ejercicioAnotacion to set
     */
    public void setEjercicioAnotacion(int ejercicioAnotacion) {
        this.ejercicioAnotacion = ejercicioAnotacion;
    }

    /**
     * @return the numeroRegistro
     */
    public long getNumeroRegistro() {
        return numeroRegistro;
    }

    /**
     * @param numeroRegistro the numeroRegistro to set
     */
    public void setNumeroRegistro(long numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    /**
     * @return the tipoRegistro
     */
    public String getTipoRegistro() {
        return tipoRegistro;
    }

    /**
     * @param tipoRegistro the tipoRegistro to set
     */
    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    /**
     * @return the tipoDocumento
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param tipoDocumento the tipoDocumento to set
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @return the fechaDocumento
     */
    public String getFechaDocumento() {
        return fechaDocumento;
    }

    /**
     * @param fechaDocumento the fechaDocumento to set
     */
    public void setFechaDocumento(String fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    /**
     * @return the entregado
     */
    public String getEntregado() {
        return entregado;
    }

    /**
     * @param entregado the entregado to set
     */
    public void setEntregado(String entregado) {
        this.entregado = entregado;
    }

    /**
     * @return the documentoRegistro
     */
    public boolean isDocumentoRegistro() {
        return documentoRegistro;
    }

    /**
     * @param documentoRegistro the documentoRegistro to set
     */
    public void setDocumentoRegistro(boolean documentoRegistro) {
        this.documentoRegistro = documentoRegistro;
    }
 
    
     public Integer getEstadoDocumentoRegistro(){
        return this.estadoDocumentoRegistro;
    }
    
    public void setEstadoDocumentoRegistro(Integer estadoDocumentoRegistro){
        this.estadoDocumentoRegistro = estadoDocumentoRegistro;
    }
    
    /*
    public String getRutaDocumentoRegistro(){
        return this.rutaDocumentoRegistro;
    }
    
    public void setRutaDocumentoRegistro(String rutaDocumentoRegistro){
        this.rutaDocumentoRegistro = rutaDocumentoRegistro;
    }**/
    
    public int getLongitudDocumento(){
        return this.longitudDocumento;
    }
    
    public void setLongitudDocumento(int longitud){
        this.longitudDocumento = longitud;
    }
    
    public String getCodTipoDato() {
        return codTipoDato;
    }

    public void setCodTipoDato(String codTipoDato) {
        this.codTipoDato = codTipoDato;
    }

    public boolean isDesdeNotificacion() {
        return desdeNotificacion; 
    }

    public void setDesdeNotificacion(boolean desdeNotificacion) {
        this.desdeNotificacion = desdeNotificacion;
    }
    
    public String getVersionNTIMetadatos() {
        return versionNTIMetadatos;
    }

    public void setVersionNTIMetadatos(String versionNTIMetadatos) {
        this.versionNTIMetadatos = versionNTIMetadatos;
    }

    public Long getIdDocumentoMetadatos() {
        return idDocumentoMetadatos;
    }

    public void setIdDocumentoMetadatos(Long idDocumentoMetadatos) {
        this.idDocumentoMetadatos = idDocumentoMetadatos;
    }

    public String getOrganoMetadatos() {
        return organoMetadatos;
    }

    public void setOrganoMetadatos(String organoMetadatos) {
        this.organoMetadatos = organoMetadatos;
    }

    public Calendar getFechaCapturaMetadatos() {
        return fechaCapturaMetadatos;
    }

    public void setFechaCapturaMetadatos(Calendar fechaCapturaMetadatos) {
        this.fechaCapturaMetadatos = fechaCapturaMetadatos;
    }

    public Integer getOrigenMetadatos() {
        return origenMetadatos;
    }

    public void setOrigenMetadatos(Integer origenMetadatos) {
        this.origenMetadatos = origenMetadatos;
    }

    public Integer getEstadoElaboracionMetadatos() {
        return estadoElaboracionMetadatos;
    }
    
    public void setEstadoElaboracionMetadatos(Integer estadoElaboracionMetadatos) {
        this.estadoElaboracionMetadatos = estadoElaboracionMetadatos;
    }

    public String getNombreFormatoMetadatos() {
        return nombreFormatoMetadatos;
    }

    public void setNombreFormatoMetadatos(String nombreFormatoMetadatos) {
        this.nombreFormatoMetadatos = nombreFormatoMetadatos;
    }

    public Integer getTipoDocumentalMetadatos() {
        return tipoDocumentalMetadatos;
    }

    public void setTipoDocumentalMetadatos(Integer tipoDocumentalMetadatos) {
        this.tipoDocumentalMetadatos = tipoDocumentalMetadatos;
    }

    public Integer getTipoFirmaMetadatos() {
        return tipoFirmaMetadatos;
    }

    public void setTipoFirmaMetadatos(Integer tipoFirmaMetadatos) {
        this.tipoFirmaMetadatos = tipoFirmaMetadatos;
    }
    
    public Long getIdMetadatoDocumento() {
        return idMetadatoDocumento;
    }

    public void setIdMetadatoDocumento(Long idMetadatoDocumento) {
        this.idMetadatoDocumento = idMetadatoDocumento;
    }

    public String getMetadatoDocumentoCsv() {
        return metadatoDocumentoCsv;
    }

    public void setMetadatoDocumentoCsv(String metadatoDocumentoCsv) {
        this.metadatoDocumentoCsv = metadatoDocumentoCsv;
    }

    public String getMetadatoDocumentoCsvAplicacion() {
        return metadatoDocumentoCsvAplicacion;
    }

    public void setMetadatoDocumentoCsvAplicacion(String metadatoDocumentoCsvAplicacion) {
        this.metadatoDocumentoCsvAplicacion = metadatoDocumentoCsvAplicacion;
    }

    public String getMetadatoDocumentoCsvUri() {
        return metadatoDocumentoCsvUri;
    }

    public void setMetadatoDocumentoCsvUri(String metadatoDocumentoCsvUri) {
        this.metadatoDocumentoCsvUri = metadatoDocumentoCsvUri;
    }
    
    public Boolean isInsertarMetadatosEnBBDD() {
        return insertarMetadatosEnBBDD;
    }

    public void setInsertarMetadatosEnBBDD(Boolean insertarMetadatosEnBBDD) {
        this.insertarMetadatosEnBBDD = insertarMetadatosEnBBDD;
    }
	
	/**
     * @return the idDocGestor
     */
    public String getIdDocGestor() {
        return idDocGestor;
    }

    /**
     * @param idDocGestor the idDocGestor to set
     */
    public void setIdDocGestor(String idDocGestor) {
        this.idDocGestor = idDocGestor;
    }

    /**
     * @return the idDocumento
     */
    public long getIdDocumento() {
        return idDocumento;
    }

    /**
     * @param idDocumento the idDocumento to set
     */
    public void setIdDocumento(long idDocumento) {
        this.idDocumento = idDocumento;
    }
}
