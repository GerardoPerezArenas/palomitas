package es.altia.agora.interfaces.user.web.sge;

// PAQUETES IMPORTADOS
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author manuel.bahamonde
 */
public class FichaExpedienteDocumentoForm  extends ActionForm {

    private String codigo;
    private FormFile fichero;
    private String tituloFichero;
    private String fechaDocumento;
    private String tipoFichero;



    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(String fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public FormFile getFichero() {
        return fichero;
    }

    public void setFichero(FormFile fichero) {
        this.fichero = fichero;
    }

    public String getTipoFichero() {
        return tipoFichero;
    }

    public void setTipoFichero(String tipoFichero) {
        this.tipoFichero = tipoFichero;
    }

    public String getTituloFichero() {
        return tituloFichero;
    }

    public void setTituloFichero(String tituloFichero) {
        this.tituloFichero = tituloFichero;
    }



}
