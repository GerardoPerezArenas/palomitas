// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.registro;

// PAQUETES IMPORTADOS
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;

public class DocumentoRegistroForm extends ActionForm {

    private String codigo;
    private FormFile fichero;
    private String docEscaneado;
    private String tituloFichero;
    private String tituloModificando; // Titulo del fichero que estamos modificando
    private String fechaDocumento;
    private boolean modificando;
    private String tipoFichero;
    private String entregado;
    private int modoAlta;
    private String tipoDocumental; 
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;

    }

    public FormFile getFichero() {
        return fichero;

    }

    public void setFichero(FormFile fichero) {
        this.fichero = fichero;
    }

    /**
     * Devuelve un String codificado en base64 con la imagen 
     * escaneada
     * @return Un String
     */
    public String getDocEscaneado() {
        return docEscaneado;
    }

    /**
     * Establece un String codificado en base64 con la imagen 
     * escaneada
     * @param Un String
     */
    public void setDocEscaneado(String docEscaneado) {
        this.docEscaneado = docEscaneado;
    }

    public String getTituloFichero() {
        return tituloFichero;
    }

    public void setTituloFichero(String titulo) {
        this.tituloFichero = titulo;
    }

    public String getTituloModificando() {
        return tituloModificando;
    }

    public void setTituloModificando(String tituloModificando) {
        this.tituloModificando = tituloModificando;
    }  
     public String getfechaDocumento() {
        return fechaDocumento;
    }

    public void setfechaDocumento(String fecha) {
        this.fechaDocumento = fecha;

    }

    public boolean isModificando() {
        return modificando;
    }

    public void setModificando(boolean modificando) {
        this.modificando = modificando;
    }

    public String getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(String fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }
 
    public String getTipoFichero() { 
        return tipoFichero;
    }

    public void setTipoFichero(String tipoFichero) {
        this.tipoFichero = tipoFichero;
    }

    public int getModoAlta() {
        return modoAlta;
    }

    public void setModoAlta(int modoAlta) {
        this.modoAlta = modoAlta;
    }

    public String getEntregado() {
        return entregado;
    }

    public void setEntregado(String entregado) {
        this.entregado = entregado;
    }

    public String getTipoDocumental() {
        return tipoDocumental;
    }

    public void setTipoDocumental(String tipoDocumental) {
        this.tipoDocumental = tipoDocumental;
    }
    
    
    
    
}