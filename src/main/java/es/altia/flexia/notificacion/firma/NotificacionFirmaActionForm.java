/*______________________________BOF_________________________________*/
package es.altia.flexia.notificacion.firma;

import es.altia.util.struts.DefaultActionForm;
import es.altia.util.commons.DateOperations;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoFirmaVO;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoRelacionFirmaVO;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;

import javax.servlet.http.HttpServletRequest;

/**
 * @version $\Date$ $\Revision$
 */
public class NotificacionFirmaActionForm extends DefaultActionForm {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
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
    protected int pTipoDocumento;
    private int pCodigoNotificacion;
    private int pCodigoOrganizacion;
    private String pTipoMime;

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
        pCodigoNotificacion = -1;
        pCodigoOrganizacion=-1;
        pTipoMime = null;
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

    public int getCodigoNotificacion() {
        return pCodigoNotificacion;
    }

    public void setCodigoNotificacion(int codigoNotificacion) {
        pCodigoNotificacion = codigoNotificacion;
    }

     public int getCodigoOrganizacion() {
        return pCodigoOrganizacion;
    }

    public void setCodigoOrganizacion(int codigoOrganizacion) {
        pCodigoOrganizacion = codigoOrganizacion;
    }

    public String getTipoMime() {
        return pTipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.pTipoMime = tipoMime;
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
        buf.append(",pCodigoNotificacion=").append(pCodigoNotificacion);
        buf.append(",pTipoMime=").append(pTipoMime);
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
}//class
/*______________________________EOF_________________________________*/