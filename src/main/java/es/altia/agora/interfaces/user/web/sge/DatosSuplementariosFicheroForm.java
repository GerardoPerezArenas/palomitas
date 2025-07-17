// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.sge;

// PAQUETES IMPORTADOS
import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;

public class DatosSuplementariosFicheroForm extends ActionForm  {
    private String codigo;
    private String path;
    private FormFile fichero;
    private String docEscaneado;    // Documento escaneado codificado en Base64
    private String tituloFichero;   // Título del fichero

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo=codigo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path=path;
    }

    public FormFile getFichero() {
        return fichero;
    }
    public void setFichero(FormFile fichero) {
        this.fichero=fichero;
    }

    public String getDocEscaneado() {
        return docEscaneado;
    }

    public void setDocEscaneado(String docEscaneado) {
        this.docEscaneado = docEscaneado;
    }

    public String getTituloFichero() {
        return tituloFichero;
    }

    public void setTituloFichero(String tituloFichero) {
        this.tituloFichero = tituloFichero;
    }

}