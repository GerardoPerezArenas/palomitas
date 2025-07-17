package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DocumentoTramitacionModuloIntegracionVO {
    private int codMunicipio;
    private String codProcedimiento;
    private String numExpediente;
    private int codTramite;
    private int ocuTramite;
    private int numDocumento;
    private Calendar fechaAlta;
    private Calendar fechaModificacion;
    private Calendar fechaInforme;
    private int codUsuarioAlta;
    private int codUsuarioModificacion;
    private byte[] contenido;
    private String nombreDocumento;
    private String extensionDocumento;

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
     * @return the numExpediente
     */
    public String getNumExpediente() {
        return numExpediente;
    }

    /**
     * @param numExpediente the numExpediente to set
     */
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
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
     * @return the ocuTramite
     */
    public int getOcuTramite() {
        return ocuTramite;
    }

    /**
     * @param ocuTramite the ocuTramite to set
     */
    public void setOcuTramite(int ocuTramite) {
        this.ocuTramite = ocuTramite;
    }

    /**
     * @return the numDocumento
     */
    public int getNumDocumento() {
        return numDocumento;
    }

    /**
     * @param numDocumento the numDocumento to set
     */
    public void setNumDocumento(int numDocumento) {
        this.numDocumento = numDocumento;
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

    
    public String getFechaAltaAsString(){
        String salida = "";
        if(fechaAlta!=null){
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            salida = sf.format(fechaAlta.getTime());
        }
        return salida;
    }

    /**
     * @return the fechaModificacion
     */
    public Calendar getFechaModificacion() {
        return fechaModificacion;
    }

    /**
     * @param fechaModificacion the fechaModificacion to set
     */
    public void setFechaModificacion(Calendar fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getFechaModificacionAsString(){
        String salida = "";
        if(fechaModificacion!=null){
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            salida = sf.format(fechaModificacion.getTime());
        }

        return salida;
    }

    /**
     * @return the fechaInforme
     */
    public Calendar getFechaInforme() {
        return fechaInforme;
    }

    /**
     * @param fechaInforme the fechaInforme to set
     */
    public void setFechaInforme(Calendar fechaInforme) {
        this.fechaInforme = fechaInforme;
    }


    public String getFechaInformeAsString(){
        String salida = "";
        if(fechaInforme!=null){
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            salida = sf.format(fechaInforme.getTime());
        }
        return salida;
    }


    /**
     * @return the codUsuarioAlta
     */
    public int getCodUsuarioAlta() {
        return codUsuarioAlta;
    }

    /**
     * @param codUsuarioAlta the codUsuarioAlta to set
     */
    public void setCodUsuarioAlta(int codUsuarioAlta) {
        this.codUsuarioAlta = codUsuarioAlta;
    }

    /**
     * @return the codUsuarioModificacion
     */
    public int getCodUsuarioModificacion() {
        return codUsuarioModificacion;
    }

    /**
     * @param codUsuarioModificacion the codUsuarioModificacion to set
     */
    public void setCodUsuarioModificacion(int codUsuarioModificacion) {
        this.codUsuarioModificacion = codUsuarioModificacion;
    }

    /**
     * @return the contenido
     */
    public byte[] getContenido() {
        return contenido;
    }

    /**
     * @param contenido the contenido to set
     */
    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
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
    }

    /**
     * @return the extensionDocumento
     */
    public String getExtensionDocumento() {
        return extensionDocumento;
    }

    /**
     * @param extensionDocumento the extensionDocumento to set
     */
    public void setExtensionDocumento(String extensionDocumento) {
        this.extensionDocumento = extensionDocumento;
    }

}