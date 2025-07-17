package es.altia.agora.business.sge;

import es.altia.agora.business.sge.firma.vo.FirmaCircuitoVO;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoVO;

/**
 *
 * @author oscar.rodriguez
 */
public class DocumentoTramiteVO {
    private String codigo;
    private String codTramite;
    private String nombre;
    private String codTipoDocumento;
    private String visibleInternet;
    private String firma;
    private String plantilla;
    private String codPlantilla;
    private String contPlantilla;
    private String interesado;
    private String docActivo;
    private String relacion;
    private String editorTexto;
    private String fechaModificacion;
    private String firmaDocumentoIdUsuario;
    private String firmaDocumentoLogUsuario;
    private String firmadocumentoDniUsuario;
    private String nombreUsuarioFirmante;
    private FirmaFlujoVO firmaFlujo;
    private FirmaCircuitoVO firmaCircuito;

    public String getFirmaDocumentoLogUsuario() {
        return firmaDocumentoLogUsuario;
    }

    public void setFirmaDocumentoLogUsuario(String firmaDocumentoUsuarioLog) {
        this.firmaDocumentoLogUsuario = firmaDocumentoUsuarioLog;
    }

    public String getFirmaDocumentoIdUsuario() {
        return firmaDocumentoIdUsuario;
    }

    public void setFirmaDocumentoIdUsuario(String firmaDocumentoUsuario) {
        this.firmaDocumentoIdUsuario = firmaDocumentoUsuario;
    }

    public FirmaFlujoVO getFirmaFlujo() {
        return firmaFlujo;
    }

    public void setFirmaFlujo(FirmaFlujoVO firmaFlujo) {
        this.firmaFlujo = firmaFlujo;
    }

    public FirmaCircuitoVO getFirmaCircuito() {
        return firmaCircuito;
    }

    public void setFirmaCircuito(FirmaCircuitoVO firmaCircuito) {
        this.firmaCircuito = firmaCircuito;
    }
    
    
    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the codTipoDocumento
     */
    public String getCodTipoDocumento() {
        return codTipoDocumento;
    }

    /**
     * @param codTipoDocumento the codTipoDocumento to set
     */
    public void setCodTipoDocumento(String codTipoDocumento) {
        this.codTipoDocumento = codTipoDocumento;
    }

    /**
     * @return the visibleInternet
     */
    public String getVisibleInternet() {
        return visibleInternet;
    }

    /**
     * @param visibleInternet the visibleInternet to set
     */
    public void setVisibleInternet(String visibleInternet) {
        this.visibleInternet = visibleInternet;
    }

    /**
     * @return the firma
     */
    public String getFirma() {
        return firma;
    }

    /**
     * @param firma the firma to set
     */
    public void setFirma(String firma) {
        this.firma = firma;
    }

    /**
     * @return the plantilla
     */
    public String getPlantilla() {
        return plantilla;
    }

    /**
     * @param plantilla the plantilla to set
     */
    public void setPlantilla(String plantilla) {
        this.plantilla = plantilla;
    }

    /**
     * @return the codPlantilla
     */
    public String getCodPlantilla() {
        return codPlantilla;
    }

    /**
     * @param codPlantilla the codPlantilla to set
     */
    public void setCodPlantilla(String codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    public String getContPlantilla() {
        return contPlantilla;
    }

    public void setContPlantilla(String contPlantilla) {
        this.contPlantilla = contPlantilla;
    }

    /**
     * @return the interesado
     */
    public String getInteresado() {
        return interesado;
    }

    /**
     * @param interesado the interesado to set
     */
    public void setInteresado(String interesado) {
        this.interesado = interesado;
    }

    /**
     * @return the docActivo
     */
    public String getDocActivo() {
        return docActivo;
    }

    /**
     * @param docActivo the docActivo to set
     */
    public void setDocActivo(String docActivo) {
        this.docActivo = docActivo;
    }

    /**
     * @return the relacion
     */
    public String getRelacion() {
        return relacion;
    }

    /**
     * @param relacion the relacion to set
     */
    public void setRelacion(String relacion) {
        this.relacion = relacion;
    }

    /**
     * @return the codTramite
     */
    public String getCodTramite() {
        return codTramite;
    }

    /**
     * @param codTramite the codTramite to set
     */
    public void setCodTramite(String codTramite) {
        this.codTramite = codTramite;
    }

    /**
     * @return the editorTexto
     */
    public String getEditorTexto() {
        return editorTexto;
    }

    /**
     * @param editorTexto the editorTexto to set
     */
    public void setEditorTexto(String editorTexto) {
        this.editorTexto = editorTexto;
    }
    
    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getFirmadocumentoDniUsuario() {
        return firmadocumentoDniUsuario;
    }

    public void setFirmadocumentoDniUsuario(String firmadocumentoDniUsuario) {
        this.firmadocumentoDniUsuario = firmadocumentoDniUsuario;
    }
    
    public String getNombreUsuarioFirmante() {
        return nombreUsuarioFirmante;
    }

    public void setNombreUsuarioFirmante(String nombreUsuarioFirmante) {
        this.nombreUsuarioFirmante = nombreUsuarioFirmante;
    }
}
