// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

// PAQUETES IMPORTADOS
import java.util.Calendar;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;

public class DocumentoCssForm extends ActionForm {

    private String codigo;
    private FormFile fichero;
    private String docEscaneado;
    private String tituloFichero;

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
}