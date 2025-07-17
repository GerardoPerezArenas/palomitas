
package es.altia.agora.interfaces.user.web.registro;
// PAQUETES IMPORTADOS
import org.apache.struts.action.*;
/**
 *
 * @author altia
 */
public class EntregadosAnteriorForm extends ActionForm{
    
    private String tipoDocumento;
    private String nombreDocumento;
    private String organo;
    private String fechaDocumento;
    private String nombreDocumentoMod;
    private String codigo;
    private boolean modificando=false;
    
    public String getNombreDocumentoMod() {
        return nombreDocumentoMod;
    }

    public void setNombreDocumentoMod(String nombreDocumentoMod) {
        this.nombreDocumentoMod = nombreDocumentoMod;
    }
    
    

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getOrgano() {
        return organo;
    }

    public void setOrgano(String organo) {
        this.organo = organo;
    }

    public String getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(String fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }
    
    
    public boolean isModificando(){
        return modificando;
    }
    public void setModificando(boolean modificando){
        this.modificando=modificando;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    
}
