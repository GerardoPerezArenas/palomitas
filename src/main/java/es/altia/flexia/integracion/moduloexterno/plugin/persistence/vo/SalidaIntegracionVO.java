package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import java.util.ArrayList;


public class SalidaIntegracionVO {
    private int status;
    private String descStatus;
    private ArrayList<CampoDesplegableModuloIntegracionVO> camposDesplegables;
    private CampoDesplegableModuloIntegracionVO campoDesplegable;
    private CampoSuplementarioModuloIntegracionVO campoSuplementario;
    private DocumentoTramitacionModuloIntegracionVO documentoTramitacion;
    private ArrayList<DocumentoTramitacionModuloIntegracionVO> listaDocumentosTramitacion;
    private ArrayList<ExpedienteModuloIntegracionVO> expedientesRelacionados;
    private TramiteModuloIntegracionVO tramite;
    private ExpedienteModuloIntegracionVO expediente;
    private Documento documentoInicioExpediente;
    private ArrayList<CircuitoFirmaModuloIntegracionVO> circuitoFirmasDocumento;
    private ArrayList<FirmaDocumentoPresentadoModuloIntegracionVO> firmasDocumentoPresentado;
    private CampoSuplementarioTerceroModuloIntegracionVO campoSuplementarioTercero;
    private UORModuloIntegracionVO uorModuloIntegracionVO;
    private UsuarioModuloIntegracionVO usuarioModuloIntegracionVO;
    private ArrayList<UsuarioModuloIntegracionVO> listaUsuariosModuloIntegracionVO = new ArrayList<UsuarioModuloIntegracionVO>();
    private ArrayList<ExpedienteModuloIntegracionVO> expedientes;
    private ArrayList<TerceroModuloIntegracionVO> terceros; 
    //Documento Asociado a un expediente
    //Pensar si se deja así , o se elige un array como tipo de dato
    
    private DocumentoExternoModuloIntegracionVO documentoExternoModuloIntegracion; 
    
    /**
     * Devuelve el código resultado de realizar la operación
     */
    public int getStatus() {
        return status;
    }

    /**
     * Permite establecer el código resultado de realizar la operación
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Devuelve una descripción del resultado de la operación, en consonancia con el código de la operación
     * @return Descripción
     */
    public String getDescStatus() {
        return descStatus;
    }

    /**
     * Permite establecer la descripción del resultado de la operación, en consonancia con el código de la operación
     * @param Descripción
     */
    public void setDescStatus(String descStatus) {
        this.descStatus = descStatus;
    }

    /**
     * Permite recuperar una colección de campos desplegables
     * @return Colección de objetos CampoDesplegableVO
     */
    public ArrayList<CampoDesplegableModuloIntegracionVO> getCamposDesplegables() {
        return camposDesplegables;
    }

    /**
     * Permite establecer una colección de campos desplegables 
     * @param Colección de objetos CampoDesplegableVO
     */
    public void setCamposDesplegables(ArrayList<CampoDesplegableModuloIntegracionVO> camposDesplegables) {
        this.camposDesplegables = camposDesplegables;
    }

    /**
     * Devuelve un determinado campo desplegable
     * @return Objeto CampoDesplegableVO con el resultado con la información del campo
     */
    public CampoDesplegableModuloIntegracionVO getCampoDesplegable() {
        return campoDesplegable;
    }

    
    /**
     * Establece un determinado campo desplegable
     * @param Objeto CampoDesplegableVO con el resultado con la información del campo
     */
    public void setCampoDesplegable(CampoDesplegableModuloIntegracionVO campoDesplegable) {
        this.campoDesplegable = campoDesplegable;
    }

    /**
     * @return the campoSuplementario
     */
    public CampoSuplementarioModuloIntegracionVO getCampoSuplementario() {
        return campoSuplementario;
    }

    /**
     * Permite establecer un determinado campo suplementario
     * @param campoSuplementario: Objeto de la clase CampoSuplementarioModuloIntegracionVO con la información del campo suplementario
     */
    public void setCampoSuplementario(CampoSuplementarioModuloIntegracionVO campoSuplementario) {
        this.campoSuplementario = campoSuplementario;
    }

    /**
     * Devuelve la información de un documento de tramitación que previamente ha sido recuperado, junto con el contenido binario del fichero
     * @return documentoTramitacion: Objeto de la clase DocumentoTramitacionModuloIntegracionVO
     */
    public DocumentoTramitacionModuloIntegracionVO getDocumentoTramitacion() {
        return documentoTramitacion;
    }

    /**
     * Permite establecer un documento de tramitación que ha sido recuperado previamente
     * @param documentoTramitacion: Objeto de la clase DocumentoTramitacionModuloIntegracionVO
     */
    public void setDocumentoTramitacion(DocumentoTramitacionModuloIntegracionVO documentoTramitacion) {
        this.documentoTramitacion = documentoTramitacion;
    }


    /**
     * Devuelve una lista de documentos de tramitación. No se devuelve el contenido binario de cada uno de ellos
     * @return the listaDocumentosTramitacion
     */
    public ArrayList<DocumentoTramitacionModuloIntegracionVO> getListaDocumentosTramitacion() {
        return listaDocumentosTramitacion;
    }


    /**
     * Establece una lista de documentos de tramitación. Estos documentos de tramitación no incluyen el contenido binario de cada documento
     * @return the listaDocumentosTramitacion
     */
    public void setListaDocumentosTramitacion(ArrayList<DocumentoTramitacionModuloIntegracionVO> listaDocumentosTramitacion) {
        this.listaDocumentosTramitacion = listaDocumentosTramitacion;
    }

    /**
     * Devuelve una lista de expedientes relacionados a uno dado
     * @return expedientesRelacionados: ArrayList<ExpedienteModuloIntegracionVO> que alberga la colección con los expedientes relacionados a uno dado
     */
    public ArrayList<ExpedienteModuloIntegracionVO> getExpedientesRelacionados() {
        return expedientesRelacionados;
    }

    /**
     * Permite establecer la lista de documentos relacionados con un determinado expediente
     * @param expedientesRelacionados: Colección de expedientes relacionados. Es un objeto instancia de ArrayList<ExpedienteModuloIntegracionVO>
     */
    public void setExpedientesRelacionados(ArrayList<ExpedienteModuloIntegracionVO> expedientesRelacionados) {
        this.expedientesRelacionados = expedientesRelacionados;
    }

    /**
     * Devuelve un determinado trámite que ha sido recuperado previamente
     * @return Objeto de la clase TramiteModuloIntegracionVO
     */
    public TramiteModuloIntegracionVO getTramite() {
        return tramite;
    }

    /**
     * Permite establecer un determinado trámite que ha sido recuperado previamente
     * @param tramite: Objeto de la clase TramiteModuloIntegracionVO
     */
    public void setTramite(TramiteModuloIntegracionVO tramite) {
        this.tramite = tramite;
    }

    /**
     * Devuelve un determinado expediente que ha sido recuperado previamente
     * @return Objeto de la clase ExpedienteModuoIntegracionVO con la información del expediente recuperado
     */
    public ExpedienteModuloIntegracionVO getExpediente() {
        return expediente;
    }

    /**
     * Permite establecer un expediente que ha sido recuperado previamente
     * @return expediente: Objeto de la clase ExpedienteModuoIntegracionVO con la información del expediente recuperado
     */
    public void setExpediente(ExpedienteModuloIntegracionVO expediente) {
        this.expediente = expediente;
    }
    
    /**
     * 
     * @return 
     */
    public Documento getDocumentoInicioExpediente() {
        return documentoInicioExpediente;
    }

    /**
     * 
     * @param documentoInicioExpediente 
     */
    public void setDocumentoInicioExpediente(Documento documentoInicioExpediente) {
        this.documentoInicioExpediente = documentoInicioExpediente;
    }

    /**
     * @return the circuitoFirmasDocumento
     */
    public ArrayList<CircuitoFirmaModuloIntegracionVO> getCircuitoFirmasDocumento() {
        return circuitoFirmasDocumento;
    }

    /**
     * @param circuitoFirmasDocumento the circuitoFirmasDocumento to set
     */
    public void setCircuitoFirmasDocumento(ArrayList<CircuitoFirmaModuloIntegracionVO> circuitoFirmasDocumento) {
        this.circuitoFirmasDocumento = circuitoFirmasDocumento;
    }

    /**
     * @return the firmasDocumentoPresentado
     */
    public ArrayList<FirmaDocumentoPresentadoModuloIntegracionVO> getFirmasDocumentoPresentado() {
        return firmasDocumentoPresentado;
    }

    /**
     * @param firmasDocumentoPresentado the firmasDocumentoPresentado to set
     */
    public void setFirmasDocumentoPresentado(ArrayList<FirmaDocumentoPresentadoModuloIntegracionVO> firmasDocumentoPresentado) {
        this.firmasDocumentoPresentado = firmasDocumentoPresentado;
    }

    public CampoSuplementarioTerceroModuloIntegracionVO getCampoSuplementarioTercero() {
        return campoSuplementarioTercero;
    }//getCampoSuplementarioTercero
    public void setCampoSuplementarioTercero(CampoSuplementarioTerceroModuloIntegracionVO campoSuplementarioTercero) {
        this.campoSuplementarioTercero = campoSuplementarioTercero;
    }//setCampoSuplementarioTercero

    public UORModuloIntegracionVO getUorModuloIntegracionVO() {
        return uorModuloIntegracionVO;
    }//getUorModuloIntegracionVO
    public void setUorModuloIntegracionVO(UORModuloIntegracionVO uorModuloIntegracionVO) {
        this.uorModuloIntegracionVO = uorModuloIntegracionVO;
    }//setUorModuloIntegracionVO

    public UsuarioModuloIntegracionVO getUsuarioModuloIntegracionVO() {
        return usuarioModuloIntegracionVO;
    }//getUsuarioModuloIntegracionVO
    public void setUsuarioModuloIntegracionVO(UsuarioModuloIntegracionVO usuarioModuloIntegracionVO) {
        this.usuarioModuloIntegracionVO = usuarioModuloIntegracionVO;
    }//setUsuarioModuloIntegracionVO

    public ArrayList<UsuarioModuloIntegracionVO> getListaUsuariosModuloIntegracionVO() {
        return listaUsuariosModuloIntegracionVO;
    }//getListaUsuariosModuloIntegracionVO
    public void setListaUsuariosModuloIntegracionVO(ArrayList<UsuarioModuloIntegracionVO> listaUsuariosModuloIntegracionVO) {
        this.listaUsuariosModuloIntegracionVO = listaUsuariosModuloIntegracionVO;
    }//setListaUsuariosModuloIntegracionVO
    
    public ArrayList<ExpedienteModuloIntegracionVO> getExpedientes() {
        return expedientes;
    }//getExpedientes
    public void setExpedientes(ArrayList<ExpedienteModuloIntegracionVO> expedientes) {
        this.expedientes = expedientes;
    }//setExpedientes

    public ArrayList<TerceroModuloIntegracionVO> getTerceros() {
        return terceros;
    }

    public void setTerceros(ArrayList<TerceroModuloIntegracionVO> terceros) {
        this.terceros = terceros;
    }

    
    public DocumentoExternoModuloIntegracionVO getDocumentoExternoModuloIntegracion() {
        return this.documentoExternoModuloIntegracion;
    }

    public void setDocumentoExternoModuloIntegracion(DocumentoExternoModuloIntegracionVO documento) {
        this.documentoExternoModuloIntegracion = documento;
    }
    
    
}//class