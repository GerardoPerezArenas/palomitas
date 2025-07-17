package es.altia.agora.business.sge;

import java.util.Calendar;

/**
 *
 * @author oscar
 */
public class DocumentoAnotacionRegistroVO {

    private int codDepartamento;
    private int codigoUorRegistro;
    private int ejercicio;
    private int numeroAnotacion;
    private String tipoEntrada;
    private String nombreDocumento;
    private String tipoDocumento;
    private byte[] contenido;
    private Calendar fechaAlta;
    private String entidad;
    // #
    private String idDocGestor;
    private boolean cotejado; //1:si; 0:no
	private boolean compulsado; //1:si; 0:no

    /**
     * @return the codDepartamento
     */
    public int getCodDepartamento() {
        return codDepartamento;
    }

    /**
     * @param codDepartamento the codDepartamento to set
     */
    public void setCodDepartamento(int codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    /**
     * @return the codigoUorRegistro
     */
    public int getCodigoUorRegistro() {
        return codigoUorRegistro;
    }

    /**
     * @param codigoUorRegistro the codigoUorRegistro to set
     */
    public void setCodigoUorRegistro(int codigoUorRegistro) {
        this.codigoUorRegistro = codigoUorRegistro;
    }

    /**
     * @return the ejercicio
     */
    public int getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

   

    /**
     * @return the tipoEntrada
     */
    public String getTipoEntrada() {
        return tipoEntrada;
    }

    /**
     * @param tipoEntrada the tipoEntrada to set
     */
    public void setTipoEntrada(String tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
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
     * @return the entidad
     */
    public String getEntidad() {
        return entidad;
    }

    /**
     * @param entidad the entidad to set
     */
    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    /**
     * @return the numeroAnotacion
     */
    public int getNumeroAnotacion() {
        return numeroAnotacion;
    }

    /**
     * @param numeroAnotacion the numeroAnotacion to set
     */
    public void setNumeroAnotacion(int numeroAnotacion) {
        this.numeroAnotacion = numeroAnotacion;
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
     * @return the cotejado
     */
    public boolean isCotejado() {
        return cotejado;
    }

    /**
     * @param cotejado the cotejado to set
     */
    public void setCotejado(boolean cotejado) {
        this.cotejado = cotejado;
    }

	/**
	 * @return the compulsado
	 */
	public boolean isCompulsado() {
		return compulsado;
	}

	/**
	 * @param compulsado the compulsado to set
	 */
	public void setCompulsado(boolean compulsado) {
		this.compulsado = compulsado;
	}
    
}