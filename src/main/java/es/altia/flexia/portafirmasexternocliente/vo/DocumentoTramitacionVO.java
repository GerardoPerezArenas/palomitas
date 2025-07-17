package es.altia.flexia.portafirmasexternocliente.vo;

/**
 * @author david.caamano
 * @version 04/03/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 04/03/2013 * #53275 Edición inicial</li>
 * </ol> 
 */
public class DocumentoTramitacionVO {
    
    private String codMunicipio;
    private String codProcedimiento;
    private String ejercicio;
    private String numExpediente;
    private String codTramite;
    private String codOcurrencia;
    private byte[] fichero;
    private String descripcion;
    private String codDocumento;
    private String nifUsuarioFirmante;
    private String nombreUsuarioFirmante;
    private String estadoFirma;
    private String clientePortafirmasExterno;
    private String idSolicitudPortafirmasExterno;
    private String buzonFirma;
    private String idDocumentoEnGestor;

    public String getCodMunicipio() {
        return codMunicipio;
    }//getCodMunicipio
    public void setCodMunicipio(String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }//setCodMunicipio
    
    public String getCodProcedimiento() {
        return codProcedimiento;
    }//getCodProcedimiento
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }//setCodProcedimiento
    
    public String getEjercicio() {
        return ejercicio;
    }//getEjercicio
    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }//setEjercicio
    
    public String getNumExpediente() {
        return numExpediente;
    }//getNumExpediente
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }//setNumExpediente
    
    public String getCodTramite() {
        return codTramite;
    }//getCodTramite
    public void setCodTramite(String codTramite) {
        this.codTramite = codTramite;
    }//setCodTramite

    public String getCodOcurrencia() {
        return codOcurrencia;
    }//getCodOcurrencia
    public void setCodOcurrencia(String codOcurrencia) {
        this.codOcurrencia = codOcurrencia;
    }//setCodOcurrencia

    public byte[] getFichero() {
        return fichero;
    }//getFichero
    public void setFichero(byte[] fichero) {
        this.fichero = fichero;
    }//setFichero

    public String getDescripcion() {
        return descripcion;
    }//getDescripcion
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }//setDescripcion

    public String getCodDocumento() {
        return codDocumento;
    }//getCodDocumento
    public void setCodDocumento(String codDocumento) {
        this.codDocumento = codDocumento;
    }//setCodDocumento

    public String getNifUsuarioFirmante() {
        return nifUsuarioFirmante;
    }//getNifUsuarioFirmante
    public void setNifUsuarioFirmante(String nifUsuarioFirmante) {
        this.nifUsuarioFirmante = nifUsuarioFirmante;
    }//setNifUsuarioFirmante
    
    public String getEstadoFirma() {
        return estadoFirma;
    }//getEstadoFirma
    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
    }//setEstadoFirma

    public String getClientePortafirmasExterno() {
        return clientePortafirmasExterno;
    }//getClientePortafirmasExterno
    public void setClientePortafirmasExterno(String clientePortafirmasExterno) {
        this.clientePortafirmasExterno = clientePortafirmasExterno;
    }//setClientePortafirmasExterno

    public String getIdSolicitudPortafirmasExterno() {
        return idSolicitudPortafirmasExterno;
    }//getIdSolicitudPortafirmasExterno
    public void setIdSolicitudPortafirmasExterno(String idSolicitudPortafirmasExterno) {
        this.idSolicitudPortafirmasExterno = idSolicitudPortafirmasExterno;
    }//setIdSolicitudPortafirmasExterno   
    
    public String getBuzonFirma() {
        return buzonFirma;
    }//getBuzonFirma
    public void setBuzonFirma(String buzonFirma) {
        this.buzonFirma = buzonFirma;
    }//setBuzonFirma
    
    public String getNombreUsuarioFirmante() {
        return nombreUsuarioFirmante;
    }//getNombreUsuarioFirmante
    public void setNombreUsuarioFirmante(String nombreUsuarioFirmante) {
        this.nombreUsuarioFirmante = nombreUsuarioFirmante;
    }//setNombreUsuarioFirmante

    /**
     * @return the idDocumentoEnGestor
     */
    public String getIdDocumentoEnGestor() {
        return idDocumentoEnGestor;
    }

    /**
     * @param idDocumentoEnGestor the idDocumentoEnGestor to set
     */
    public void setIdDocumentoEnGestor(String idDocumentoEnGestor) {
        this.idDocumentoEnGestor = idDocumentoEnGestor;
    }
    
}//class
