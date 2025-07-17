package es.altia.flexia.interfaces.user.web.documentospresentados.form;

import es.altia.agora.business.sge.FirmasDocumentoProcedimientoVO;
import java.util.ArrayList;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * Formulario para subir un adjunto a un documento de expediente
 * @author oscar.rodriguez
 */
public class DocumentoExpedienteForm extends ActionForm {

    private String codigo;
    private FormFile fichero;
    private String tituloFichero;
    private String fechaDocumento;
    private String tipoFichero;
    private String codDocumento;
    private String codMunicipio;
    private String ejercicio;
    private String numeroExpediente;
    private String codProcedimiento;
    private String modificar;
    private String nombreOriginalAdjunto;
    private String docEscaneado;
    private boolean firmasConfigurables;
    private String codUnidadOrganicaExp;
    
    
    private ArrayList<FirmasDocumentoProcedimientoVO> firmas;

    public ArrayList<FirmasDocumentoProcedimientoVO> getFirmas() {
        return firmas;
    }

    public void setFirmas(ArrayList<FirmasDocumentoProcedimientoVO> firmas) {
        this.firmas = firmas;
    }        

    public boolean isFirmasConfigurables() {
        return firmasConfigurables;
    }

    public void setFirmasConfigurables(boolean firmasConfigurables) {
        this.firmasConfigurables = firmasConfigurables;
    }    
    
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

    /**
     * @return the codDocumento
     */
    public String getCodDocumento() {
        return codDocumento;
    }

    /**
     * @param codDocumento the codDocumento to set
     */
    public void setCodDocumento(String codDocumento) {
        this.codDocumento = codDocumento;
    }

    /**
     * @return the codMunicipio
     */
    public String getCodMunicipio() {
        return codMunicipio;
    }

    /**
     * @param codMunicipio the codMunicipio to set
     */
    public void setCodMunicipio(String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    /**
     * @return the ejercicio
     */
    public String getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    /**
     * @return the numeroExpediente
     */
    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    /**
     * @param numeroExpediente the numeroExpediente to set
     */
    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
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
     * @return the modificar
     */
    public String getModificar() {
        return modificar;
    }

    /**
     * @param modificar the modificar to set
     */
    public void setModificar(String modificar) {
        this.modificar = modificar;
    }

    /**
     * @return the nombreOriginalAdjunto
     */
    public String getNombreOriginalAdjunto() {
        return nombreOriginalAdjunto;
    }

    /**
     * @param nombreOriginalAdjunto the nombreOriginalAdjunto to set
     */
    public void setNombreOriginalAdjunto(String nombreOriginalAdjunto) {
        this.nombreOriginalAdjunto = nombreOriginalAdjunto;
    }

    /**
     * @return the docEscaneado
     */
    public String getDocEscaneado() {
        return docEscaneado;
    }

    /**
     * @param docEscaneado the docEscaneado to set
     */
    public void setDocEscaneado(String docEscaneado) {
        this.docEscaneado = docEscaneado;
    }

    /**
     * @return the codUnidadOrganicaExpediente
     */
    public String getCodUnidadOrganicaExp() {
        return codUnidadOrganicaExp;
    }

    /**
     * @param codUnidadOrganicaExpediente the codUnidadOrganicaExpediente to set
     */
    public void setCodUnidadOrganicaExp(String codUnidadOrganicaExp) {
        this.codUnidadOrganicaExp = codUnidadOrganicaExp;
    }

}