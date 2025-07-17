package es.altia.agora.business.registro;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: miguel.coladas
 * Date: 29-sep-2006
 * Time: 11:42:33
 * To change this template use File | Settings | File Templates.
 */
public class DocumentoValueObject implements Serializable {

    private String codigo;
    private String extension;
    private String nombre;
    private byte[] fichero;
    private String fecha;
    // #291976
    private String catalogado; // SI -> Es un documento catalogado con tipo documental y metadados / NO -> No es un documento catalogado
    // Necesitamos saber los documentos digitalizados para comprobar si debe estar catalogado. Si digitalizado = NO no importa el valor de catalogado
    private String digitalizado; // Necesitamos saber los documentos digitalizados para comprobar si debe estar catalogadoSI -> Es un documento digitalizado / NO -> No es un documento digitalizado
    // necesitamos saber la uor del documento
    private int unidadOrg;
    private String tipoDocumental; 
    private long idDocumento;
    private String observDoc;
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getFichero() {
        return fichero;
    }

    public void setFichero(byte[] fichero) {
        this.fichero = fichero;
    }
        
    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the catalogado
     */
    public String getCatalogado() {
        return catalogado;
    }

    /**
     * @param catalogado the catalogado to set
     */
    public void setCatalogado(String catalogado) {
        this.catalogado = catalogado;
    }

    /**
     * @return the digitalizado
     */
    public String getDigitalizado() {
        return digitalizado;
    }

    /**
     * @param digitalizado the digitalizado to set
     */
    public void setDigitalizado(String digitalizado) {
        this.digitalizado = digitalizado;
    }

    /**
     * @return the unidadOrg
     */
    public int getUnidadOrg() {
        return unidadOrg;
    }

    /**
     * @param unidadOrg the unidadOrg to set
     */
    public void setUnidadOrg(int unidadOrg) {
        this.unidadOrg = unidadOrg;
    }

    public String getTipoDocumental() {
        return tipoDocumental;
    }

    public void setTipoDocumental(String tipoDocumental) {
        this.tipoDocumental = tipoDocumental;
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

    public String getObservDoc() {
        return observDoc;
    }

    public void setObservDoc(String observDoc) {
        this.observDoc = observDoc;
    }
    
    
    
    
}
