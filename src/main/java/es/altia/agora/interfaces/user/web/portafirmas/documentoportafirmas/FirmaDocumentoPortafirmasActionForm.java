/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas;

/**
 * @version $\Date$ $\Revision$
 */
public class FirmaDocumentoPortafirmasActionForm extends SearchDocumentoPortafirmasActionForm {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    protected int pIdMunicipio;
    protected String pIdProcedimiento;
    protected int pIdEjercicio;
    protected String pIdNumeroExpediente;
    protected int pIdTramite;
    protected int pIdOcurrenciaTramite;
    protected int pIdNumeroDocumento;
    protected int pUsuarioFirmante;
    protected String pNifUsuarioFirmante;
    protected String pFirma;
    protected String pObservaciones;
    protected String pDescDocumento;
    protected int pTipoDocumento;
    protected int pIdNumFirma;
    protected int pIdPresentado;
    protected String hashB64;
    protected String firmaBase64;
    /** Puede tomar los valores: FLEXIA, AFIRMA, ASF **/
    private String plataformaFirma;
    private String finalizaRechazo;
    private String editorTexto;
    private String tipoMime;
    
    /*_______Operations_____________________________________________*/
    protected void doReset() {
        super.doReset();
        pIdMunicipio=0;
        pIdProcedimiento=null;
        pIdEjercicio=0;
        pIdNumeroExpediente=null;
        pIdTramite=0;
        pIdOcurrenciaTramite=0;
        pIdNumeroDocumento=0;
        pUsuarioFirmante=0;
        pNifUsuarioFirmante = null;
        pFirma = null;
        pObservaciones = null;
        pDescDocumento = null;
        pTipoDocumento = 0;
        plataformaFirma = null;
        tipoMime = null;
    }//doReset

    public int getIdMunicipio() {
        return pIdMunicipio;
    }
    public void setIdMunicipio(int idMunicipio) {
        pIdMunicipio = idMunicipio;
    }

    public String getIdProcedimiento() {
        return pIdProcedimiento;
    }
    public void setIdProcedimiento(String idProcedimiento) {
        pIdProcedimiento = idProcedimiento;
    }

    public int getIdEjercicio() {
        return pIdEjercicio;
    }
    public void setIdEjercicio(int idEjercicio) {
        pIdEjercicio = idEjercicio;
    }

    public int getIdPresentado() {
        return pIdPresentado;
    }

    public void setIdPresentado(int pIdPresentado) {
        this.pIdPresentado = pIdPresentado;
    }
        

    public String getIdNumeroExpediente() {
        return pIdNumeroExpediente;
    }
    public void setIdNumeroExpediente(String idNumeroExpediente) {
        pIdNumeroExpediente = idNumeroExpediente;
    }

    public int getIdTramite() {
        return pIdTramite;
    }
    public void setIdTramite(int idTramite) {
        pIdTramite = idTramite;
    }

    public int getIdOcurrenciaTramite() {
        return pIdOcurrenciaTramite;
    }
    public void setIdOcurrenciaTramite(int idOcurrenciaTramite) {
        pIdOcurrenciaTramite = idOcurrenciaTramite;
    }

    public int getIdNumeroDocumento() {
        return pIdNumeroDocumento;
    }
    public void setIdNumeroDocumento(int idNumeroDocumento) {
        pIdNumeroDocumento = idNumeroDocumento;
    }

    public int getUsuarioFirmante() {
        return pUsuarioFirmante;
    }
    public void setUsuarioFirmante(int usuarioFirmante) {
        pUsuarioFirmante = usuarioFirmante;
    }

    public String getNifUsuarioFirmante() {
        return pNifUsuarioFirmante;
    }
    public void setNifUsuarioFirmante(String nifUsuarioFirmante) {
        pNifUsuarioFirmante = nifUsuarioFirmante;
    }

    public String getFirma() {
        return pFirma;
    }
    public void setFirma(String firma) {
        pFirma = firma;
    }

    public String getObservaciones() {
        return pObservaciones;
    }
    public void setObservaciones(String observaciones) {
        pObservaciones = observaciones;
    }

    public String getDescripcionDocumento() {
        return pDescDocumento;
    }
    public void setDescripcionDocumento(String descripcionDocumento) {
        pDescDocumento = descripcionDocumento;
    }

    public int getTipoDocumento() {
        return pTipoDocumento;
    }

    public void setTipoDocumento(int theTipoDocumento) {
        pTipoDocumento = theTipoDocumento;
    }

    public int getIdNumFirma() {
        return pIdNumFirma;
    }

    public void setIdNumFirma(int pIdFirma) {
        this.pIdNumFirma = pIdFirma;
    }

    public String getHashB64() {
        return hashB64;
    }

    public void setHashB64(String hashB64) {
        this.hashB64 = hashB64;
    }
    
    public String getFirmaBase64() {
        return firmaBase64;
    }

    public void setFirmaBase64(String firmaBase64) {
        this.firmaBase64 = firmaBase64;
    }

    public String getFinalizaRechazo() {
        return finalizaRechazo;
    }

    public void setFinalizaRechazo(String finalizaRechazo) {
        this.finalizaRechazo = finalizaRechazo;
    }

    public String getEditorTexto() {
        return editorTexto;
    }

    public void setEditorTexto(String editorTexto) {
        this.editorTexto = editorTexto;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }
    
    public String toString() {
        final StringBuffer buf = new StringBuffer(240);
        buf.append(super.toString()).append(" ::: ");
        buf.append("FirmaDocumentoPortafirmasActionForm");
        buf.append("{pIdMunicipio=").append(pIdMunicipio);
        buf.append(",pIdProcedimiento=").append(pIdProcedimiento);
        buf.append(",pIdEjercicio=").append(pIdEjercicio);
        buf.append(",pIdNumeroExpediente=").append(pIdNumeroExpediente);
        buf.append(",pIdTramite=").append(pIdTramite);
        buf.append(",pIdOcurrenciaTramite=").append(pIdOcurrenciaTramite);
        buf.append(",pIdNumeroDocumento=").append(pIdNumeroDocumento);
        buf.append(",pUsuarioFirmante=").append(pUsuarioFirmante);
        buf.append(",pNifUsuarioFirmante=").append(pNifUsuarioFirmante);
        buf.append(",pFirma=").append(pFirma);
        buf.append(",pObservaciones=").append(pObservaciones);
        buf.append(",pDescDocumento=").append(pDescDocumento);
        buf.append(",pTipoDocumento=").append(pTipoDocumento);
        buf.append(",pIdNumFirma=").append(pIdNumFirma);
        buf.append(",pIdPresentado=").append(pIdPresentado);
        buf.append(",pIdPresentado=").append(hashB64);
        buf.append(",firmaBase64=").append(firmaBase64);
        buf.append(",plataformaFirma=").append(plataformaFirma);
        buf.append(",finalizaRechazo").append(finalizaRechazo);
        buf.append(",tipoMime").append(tipoMime);
        buf.append('}');
        return buf.toString();
    }

    /**
     * @return the plataformaFirma
     */
    public String getPlataformaFirma() {
        return plataformaFirma;
    }

    /**
     * @param plataformaFirma the plataformaFirma to set
     */
    public void setPlataformaFirma(String plataformaFirma) {
        this.plataformaFirma = plataformaFirma;
    }
}//class
/*______________________________EOF_________________________________*/
