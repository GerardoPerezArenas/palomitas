package es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas;

import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoFirmaVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoOtroFirmaVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoProcedimientoFirmaVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoRelacionFirmaVO;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.util.commons.DateOperations;
import es.altia.util.struts.DefaultActionForm;
import javax.servlet.http.HttpServletRequest;


public class DocumentoExternoNotificacionForm extends DefaultActionForm{
    
     /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    private int codAdjunto;
    private int codNotificacion;
    protected int pUsuario;
    private int pIdMunicipio;
    private String pIdProcedimiento;
    private int pIdEjercicio;
    private String pIdNumeroExpediente;
    private int pIdTramite;
    private int pIdOcurrenciaTramite;
    private int pIdNumeroDocumento;
    private int pUsuarioFirmante;
    private Integer pUsuarioDelegadoFirmante;
    protected String pNombreUsuarioFirmante;
    protected String pNombreUsuarioDelegadoFirmante;
    protected String pEstadoFirma;
    protected String pFirma;
    protected String pFechaFirma;
    protected String pObservaciones;
    protected String pNifUsuarioFirmante;
    protected int pIdNumFirma;
    protected int pTipoDocumento;    
    protected String firmaBase64;
    private String hashB64;
    private String tipoMime;

    /*_______Operations_____________________________________________*/
    protected void reset() {
        pUsuario=-1;
        pIdMunicipio=-1;
        pIdProcedimiento=null;
        pIdEjercicio=-1;
        pIdNumeroExpediente=null;
        pIdTramite=-1;
        pIdOcurrenciaTramite=-1;
        pIdNumeroDocumento=-1;
        pUsuarioFirmante=-1;
        pUsuarioDelegadoFirmante=null;
        pNombreUsuarioFirmante=null;
        pNombreUsuarioDelegadoFirmante=null;
        pEstadoFirma=null;
        pFirma=null;
        pFechaFirma=null;
        pObservaciones=null;
        pNifUsuarioFirmante = null;
        pTipoDocumento = 0;
        pIdNumFirma=0;
        firmaBase64=null;
        hashB64 = null;
        tipoMime = null;
    }//reset

    public int getUsuario() {
        return pUsuario;
    }
    public void setUsuario(int usuario) {
        pUsuario = usuario;
    }

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

    public String getNombreUsuarioFirmante() {
        return pNombreUsuarioFirmante;
    }
    public void setNombreUsuarioFirmante(String nombreUsuarioFirmante) {
        pNombreUsuarioFirmante = nombreUsuarioFirmante;
    }

    public String getEstadoFirma() {
        return pEstadoFirma;
    }
    public void setEstadoFirma(String estadoFirma) {
        pEstadoFirma = estadoFirma;
    }

    public String getFechaFirma() {
        return pFechaFirma;
    }
    public void setFechaFirma(String fechaFirma) {
        pFechaFirma = fechaFirma;
    }

    public String getObservaciones() {
        return pObservaciones;
    }
    public void setObservaciones(String observaciones) {
        pObservaciones = observaciones;
    }

    public String getFirma() {
        return pFirma;
    }
    public void setFirma(String firma) {
        pFirma = firma;
    }

    public String getNifUsuarioFirmante() {
        return pNifUsuarioFirmante;
    }
    public void setNifUsuarioFirmante(String nifUsuarioFirmante) {
        pNifUsuarioFirmante = nifUsuarioFirmante;
    }

    public Integer getUsuarioDelegadoFirmante() {
        return pUsuarioDelegadoFirmante;
    }
    public void setUsuarioDelegadoFirmante(Integer usuarioDelegadoFirmante) {
        pUsuarioDelegadoFirmante = usuarioDelegadoFirmante;
    }

    public String getNombreUsuarioDelegadoFirmante() {
        return pNombreUsuarioDelegadoFirmante;
    }
    public void setNombreUsuarioDelegadoFirmante(String nombreUsuarioDelegadoFirmante) {
        pNombreUsuarioDelegadoFirmante = nombreUsuarioDelegadoFirmante;
    }

    public int getTipoDocumento() {
        return pTipoDocumento;
    }

    public void setTipoDocumento(int tipoDocumento) {
        pTipoDocumento = tipoDocumento;
    }

    public int getIdNumFirma() {
        return pIdNumFirma;
    }

    public void setIdNumFirma(int pIdNumFirma) {
        this.pIdNumFirma = pIdNumFirma;
    }

    public String getFirmaBase64() {
        return firmaBase64;
    }

    public void setFirmaBase64(String firmaBase64) {
        this.firmaBase64 = firmaBase64;
    }
    
    
    

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append(super.toString()).append(" ::: ");
        buf.append("DocumentoFirmaActionForm");
        buf.append("{pUsuario=").append(pUsuario);
        buf.append(",pIdMunicipio=").append(pIdMunicipio);
        buf.append(",pIdProcedimiento=").append(pIdProcedimiento);
        buf.append(",pIdEjercicio=").append(pIdEjercicio);
        buf.append(",pIdNumeroExpediente=").append(pIdNumeroExpediente);
        buf.append(",pIdTramite=").append(pIdTramite);
        buf.append(",pIdOcurrenciaTramite=").append(pIdOcurrenciaTramite);
        buf.append(",pIdNumeroDocumento=").append(pIdNumeroDocumento);
        buf.append(",pUsuarioFirmante=").append(pUsuarioFirmante);
        buf.append(",pUsuarioDelegadoFirmante=").append(pUsuarioDelegadoFirmante);
        buf.append(",pEstadoFirma=").append(pEstadoFirma);
        buf.append(",pFirma=").append(pFirma);
        buf.append(",pFechaFirma=").append(pFechaFirma);
        buf.append(",pObservaciones=").append(pObservaciones);
        buf.append(",pNifUsuarioFirmante=").append(pNifUsuarioFirmante);
        buf.append(",pTipoDocumento=").append(pTipoDocumento);
        buf.append(",firmaBase64=").append(firmaBase64);
        buf.append(",tipoMime=").append(tipoMime);
        buf.append('}');
        return buf.toString();
    }

    public void setDocumentoFirmaVO(DocumentoFirmaVO vo, HttpServletRequest request) {
        pIdMunicipio=vo.getIdMunicipio();
        pIdProcedimiento=vo.getIdProcedimiento();
        pIdEjercicio=vo.getIdEjercicio();
        pIdNumeroExpediente=vo.getIdNumeroExpediente();
        pIdTramite=vo.getIdTramite();
        pIdOcurrenciaTramite=vo.getIdOcurrenciaTramite();
        pIdNumeroDocumento=vo.getIdNumeroDocumento();
        pUsuarioFirmante=vo.getUsuarioFirmante();
        pUsuarioDelegadoFirmante=vo.getIdUsuarioDelegadoFirmante();
        pEstadoFirma=vo.getEstadoFirma();
        pFirma=(vo.getFirma()!=null)?("..."):("");
        pFechaFirma=DateOperations.toString(vo.getFechaFirma(),GlobalNames.TIMESTAMP_FORMAT);
        pObservaciones=vo.getObservaciones();
    }

    public void setDocumentoFirmaVO(DocumentoRelacionFirmaVO vo, HttpServletRequest request) {
        pIdMunicipio=vo.getIdMunicipio();
        pIdProcedimiento=vo.getIdProcedimiento();
        pIdEjercicio=vo.getIdEjercicio();
        pIdNumeroExpediente=vo.getIdNumeroExpediente();
        pIdTramite=vo.getIdTramite();
        pIdOcurrenciaTramite=vo.getIdOcurrenciaTramite();
        pIdNumeroDocumento=vo.getIdNumeroDocumento();
        pUsuarioFirmante=vo.getUsuarioFirmante();
        pUsuarioDelegadoFirmante=vo.getIdUsuarioDelegadoFirmante();
        pEstadoFirma=vo.getEstadoFirma();
        pFirma=(vo.getFirma()!=null)?("..."):("");
        pFechaFirma=DateOperations.toString(vo.getFechaFirma(),GlobalNames.TIMESTAMP_FORMAT);
        pObservaciones=vo.getObservaciones();
    }
    
    public void setDocumentoFirmaVO(DocumentoProcedimientoFirmaVO vo, HttpServletRequest request) {                
        pIdNumeroExpediente=vo.getIdNumeroExpediente();
        pIdTramite=0;
        pIdOcurrenciaTramite=0;
        pIdNumeroDocumento=vo.getIdPresentado();
        pUsuarioFirmante=Integer.valueOf(vo.getCodigoUsuarioFirma());
        pUsuarioDelegadoFirmante=null;
        pEstadoFirma=vo.getEstadoFirma();
        pFirma=(vo.getFirma()!=null)?("..."):("");
        pFechaFirma=DateOperations.toString(vo.getFechaFirma(),GlobalNames.TIMESTAMP_FORMAT);
        pIdNumFirma = vo.getIdNumFirma();
        pObservaciones=vo.getObservaciones();
    }
    
    public void setDocumentoFirmaVO(DocumentoOtroFirmaVO vo, HttpServletRequest request) {                
        pIdNumeroExpediente=null;
        pIdTramite=0;
        pIdOcurrenciaTramite=0;
        pIdNumeroDocumento=vo.getCodigoDocumento();
        pUsuarioFirmante=Integer.valueOf(vo.getCodigoUsuarioFirma());
        pUsuarioDelegadoFirmante=null;
        pEstadoFirma=vo.getEstadoFirma();
        pIdNumFirma = vo.getCodigoDocumento();
        pFirma=(vo.getFirma()!=null)?("..."):("");
        pFechaFirma=DateOperations.toString(vo.getFechaFirma(),GlobalNames.TIMESTAMP_FORMAT);
        pObservaciones=vo.getObservaciones();
    }
    
    public void setDocumentoFirmaVO(AdjuntoNotificacionVO vo, HttpServletRequest request) {                
        pIdNumeroExpediente=null;
        pIdTramite=vo.getCodigoTramite();
        pIdOcurrenciaTramite=vo.getOcurrenciaTramite();
        pIdNumeroExpediente=vo.getNumeroExpediente();
        pIdNumeroDocumento=vo.getIdDocExterno();
                
        pEstadoFirma=vo.getEstadoFirma();        
        if(vo.getEstadoFirma()!=null && "F".equalsIgnoreCase(vo.getEstadoFirma())){
            pUsuarioDelegadoFirmante=vo.getCodUsuarioFirmaOtro();
            pUsuarioFirmante=vo.getCodUsuarioFirmaOtro();
            pFechaFirma=DateOperations.toString(vo.getFechaFirma(),GlobalNames.TIMESTAMP_FORMAT);        
        }else
        if(vo.getEstadoFirma()!=null && "R".equalsIgnoreCase(vo.getEstadoFirma())){
            pUsuarioDelegadoFirmante=vo.getCodUsuarioRechazo();
            pUsuarioFirmante=vo.getCodUsuarioRechazo();
            pFechaFirma=DateOperations.toString(vo.getFechaRechazo(),GlobalNames.TIMESTAMP_FORMAT);        
        }                
            
        pFirma=(vo.getFirma()!=null)?("..."):("");        
        
    }

    /**
     * @return the hashB64
     */
    public String getHashB64() {
        return hashB64;
    }

    /**
     * @param hashB64 the hashB64 to set
     */
    public void setHashB64(String hashB64) {
        this.hashB64 = hashB64;
    }

    /**
     * @return the codAdjunto
     */
    public int getCodAdjunto() {
        return codAdjunto;
    }

    /**
     * @param codAdjunto the codAdjunto to set
     */
    public void setCodAdjunto(int codAdjunto) {
        this.codAdjunto = codAdjunto;
    }

    /**
     * @return the codNotificacion
     */
    public int getCodNotificacion() {
        return codNotificacion;
    }

    /**
     * @param codNotificacion the codNotificacion to set
     */
    public void setCodNotificacion(int codNotificacion) {
        this.codNotificacion = codNotificacion;
    }
       
    public String getTipoMime() {
        return tipoMime;
    }
    
    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }
}