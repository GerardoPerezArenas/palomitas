/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.notificacion.vo;

import java.util.Calendar;

public class AdjuntoNotificacionVO {

    private String nombre;
    private byte[] contenido;
    private String firma;
    private String tipoDocumento;
    private int codigoMunicipio;
    private String codigoProcedimiento;
    private int ejercicio;
    private String numeroExpediente;
    private int codigoTramite;
    private int ocurrenciaTramite;
    private int numeroUnidad;
    private int codigoNotificacion;
    private String seleccionado;
    private String firmado;
    private boolean documentoTramitacion;
    private String contentType;
    private Calendar fechaAlta;
    private int codUsuarioFirmaOtro;
    private String estadoFirma;
    private int idDocExterno;
    private Calendar fechaFirma;    
    private String nombreProcedimiento;
    private String[] params;
    private String plataformaFirma;
    private Calendar fechaRechazo;
    private int codUsuarioRechazo;
    private String observacionesRechazo;
    private int tipoCertificadoFirma;
    
        
    public String getContentType(){
        return this.contentType;
    }
    
    public void setContentType(String contentType){
        this.contentType = contentType;
    }

//nombre: De tipo String. Representa el nombre del documento.
//contenido: De tipo byte[]. Representa el contenido del documento.
//firma: De tipo String. Representa la firma del documento en formato Base64
//tipoDocumento: De tipo String. Representa el tipo de documento.
//codigoMunicipio: De tipo int. Representa el código del municipio.
//codigoProcedimiento: De tipo String. Representa el código del procedimiento.
//ejercicio: De tipo int. Representa el ejercicio al que pertenece el expediente.
//numeroExpediente: Número del expediente
//codigoTramite: Código del trámite al que está asociado la notificación.
//ocurrenciaTramite: Ocurrencia del trámite al que está asociado la notificación.
//numeroUnidad: De tipo int. Representa el número de unidad del documento.
//codigoNotificacion: De tipo int. Código de la notificación.
//seleccionado: De tipo String. Si el adjunto está asociado a una notificación, entonces contiene el valor ?SI?, en caso contrario, contiene el valor ?NO?.


    public int getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(int codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public int getCodigoNotificacion() {
        return codigoNotificacion;
    }

    public void setCodigoNotificacion(int codigoNotificacion) {
        this.codigoNotificacion = codigoNotificacion;
    }

    public String getCodigoProcedimiento() {
        return codigoProcedimiento;
    }

    public void setCodigoProcedimiento(String codigoProcedimiento) {
        this.codigoProcedimiento = codigoProcedimiento;
    }

    public int getCodigoTramite() {
        return codigoTramite;
    }

    public void setCodigoTramite(int codigoTramite) {
        this.codigoTramite = codigoTramite;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    public int getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public int getNumeroUnidad() {
        return numeroUnidad;
    }

    public void setNumeroUnidad(int numeroUnidad) {
        this.numeroUnidad = numeroUnidad;
    }

    public int getOcurrenciaTramite() {
        return ocurrenciaTramite;
    }

    public void setOcurrenciaTramite(int ocurrenciaTramite) {
        this.ocurrenciaTramite = ocurrenciaTramite;
    }

    public String getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(String seleccionado) {
        this.seleccionado = seleccionado;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

     public String getFirmado() {
        return firmado;
    }

    public void setFirmado(String firmado) {
        this.firmado = firmado;
    }

    /**
     * @return the documentoTramitacion
     */
    public boolean isDocumentoTramitacion() {
        return documentoTramitacion;
    }

    /**
     * @param documentoTramitacion the documentoTramitacion to set
     */
    public void setDocumentoTramitacion(boolean documentoTramitacion) {
        this.documentoTramitacion = documentoTramitacion;
    }

    /**
     * @return the fechaAlta
     */
    public Calendar getFechaAlta() {
        return fechaAlta;
    }

    /**
     * @param fechaAlta the fechaAlta to set
     */
    public void setFechaAlta(Calendar fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    /**
     * @return the codUsuarioFirmaOtro
     */
    public int getCodUsuarioFirmaOtro() {
        return codUsuarioFirmaOtro;
    }

    /**
     * @param codUsuarioFirmaOtro the codUsuarioFirmaOtro to set
     */
    public void setCodUsuarioFirmaOtro(int codUsuarioFirmaOtro) {
        this.codUsuarioFirmaOtro = codUsuarioFirmaOtro;
    }

    /**
     * @return the estadoFirma
     */
    public String getEstadoFirma() {
        return estadoFirma;
    }

    /**
     * @param estadoFirma the estadoFirma to set
     */
    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
    }

    /**
     * @return the idDocExterno
     */
    public int getIdDocExterno() {
        return idDocExterno;
    }

    /**
     * @param idDocExterno the idDocExterno to set
     */
    public void setIdDocExterno(int idDocExterno) {
        this.idDocExterno = idDocExterno;
    }

    /**
     * @return the fechaFirma
     */
    public Calendar getFechaFirma() {
        return fechaFirma;
    }

    /**
     * @param fechaFirma the fechaFirma to set
     */
    public void setFechaFirma(Calendar fechaFirma) {
        this.fechaFirma = fechaFirma;
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
     * @return the plataformaFirma
     */
    public String getPlataformaFirma() {
        return plataformaFirma;
    }

    /**
     * @param plataformaFirma the plataformaFirma to set
     */
    public void setPlataformaFirma(String plataformaFirma) {
        this.plataformaFirma = plataformaFirma;
    }

    /**
     * @return the fechaRechazo
     */
    public Calendar getFechaRechazo() {
        return fechaRechazo;
    }

    /**
     * @param fechaRechazo the fechaRechazo to set
     */
    public void setFechaRechazo(Calendar fechaRechazo) {
        this.fechaRechazo = fechaRechazo;
    }

    /**
     * @return the codUsuarioRechazo
     */
    public int getCodUsuarioRechazo() {
        return codUsuarioRechazo;
    }

    /**
     * @param codUsuarioRechazo the codUsuarioRechazo to set
     */
    public void setCodUsuarioRechazo(int codUsuarioRechazo) {
        this.codUsuarioRechazo = codUsuarioRechazo;
    }

    /**
     * @return the observacionesRechazo
     */
    public String getObservacionesRechazo() {
        return observacionesRechazo;
    }

    /**
     * @param observacionesRechazo the observacionesRechazo to set
     */
    public void setObservacionesRechazo(String observacionesRechazo) {
        this.observacionesRechazo = observacionesRechazo;
    }

    /**
     * @return the tipoCertificadoFirma
     */
    public int getTipoCertificadoFirma() {
        return tipoCertificadoFirma;
    }

    /**
     * @param tipoCertificadoFirma the tipoCertificadoFirma to set
     */
    public void setTipoCertificadoFirma(int tipoCertificadoFirma) {
        this.tipoCertificadoFirma = tipoCertificadoFirma;
    }

   
    

}
