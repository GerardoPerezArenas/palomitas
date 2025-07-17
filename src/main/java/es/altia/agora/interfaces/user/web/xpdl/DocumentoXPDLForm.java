package es.altia.agora.interfaces.user.web.xpdl;

import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author oscar.rodriguez
 */
public class DocumentoXPDLForm extends ActionForm{
    
    private String codigo;
    private FormFile fichero;
    private String tituloFichero;
    private String tituloModificando;
    private DefinicionProcedimientosValueObject defProcVO;
    private String listaCodigosUorsImportacion = null;
    private String listaCodigosVisibleUorsImportacion = null;

    /*** CAMPOS PARA SELECCION DE UNIDADES TRAMITADORAS DE UN TRÁMITE */
    private String listaCodigosUorsTramitadorasTramite = null;    
    /** CAMPO CON EL CODIGO DE UOR DE LA UNIDAD DE INICIO MANUAL DEL TRÁMITE */
    private String codigoUorInicioManualTramite = null;
    /** CAMPO CON EL CÓDIGO DEL TRÁMITE **/
    private String codigoTramite;
    

    public DocumentoXPDLForm() {
        reset();
    }  

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

   

    public String getTituloFichero() {
        return tituloFichero;
    }

    public void setTituloFichero(String titulo) {
        this.tituloFichero = titulo;
    }

    public String getTituloModificando() {
        return tituloModificando;
    }

    public void setTituloModificando(String tituloModificando) {
        this.tituloModificando = tituloModificando;
    }
    public DefinicionProcedimientosValueObject getProcedimiento(){
        return defProcVO;
    }
    
    public void setProcedimiento(DefinicionProcedimientosValueObject dpVO){
        this.defProcVO = dpVO;
    }
 
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }
    
     private void reset() {
        codigo="";
        fichero=null;
        tituloFichero="";
        tituloModificando="";
        defProcVO=null;
        listaCodigosUorsImportacion = null;
        listaCodigosVisibleUorsImportacion = null;
    }

    /**
     * @return the listaCodigosUorsImportacion
     */
    public String getListaCodigosUorsImportacion() {
        return listaCodigosUorsImportacion;
    }

    /**
     * @param listaCodigosUorsImportacion the listaCodigosUorsImportacion to set
     */
    public void setListaCodigosUorsImportacion(String listaCodigosUorsImportacion) {
        this.listaCodigosUorsImportacion = listaCodigosUorsImportacion;
    }

    /**
     * @return the listaCodigosVisibleUorsImportacion
     */
    public String getListaCodigosVisibleUorsImportacion() {
        return listaCodigosVisibleUorsImportacion;
    }

    /**
     * @param listaCodigosVisibleUorsImportacion the listaCodigosVisibleUorsImportacion to set
     */
    public void setListaCodigosVisibleUorsImportacion(String listaCodigosVisibleUorsImportacion) {
        this.listaCodigosVisibleUorsImportacion = listaCodigosVisibleUorsImportacion;
    }

    /**
     * @return the listaCodigosUorsTramitadorasTramite
     */
    public String getListaCodigosUorsTramitadorasTramite() {
        return listaCodigosUorsTramitadorasTramite;
    }

    /**
     * @param listaCodigosUorsTramitadorasTramite the listaCodigosUorsTramitadorasTramite to set
     */
    public void setListaCodigosUorsTramitadorasTramite(String listaCodigosUorsTramitadorasTramite) {
        this.listaCodigosUorsTramitadorasTramite = listaCodigosUorsTramitadorasTramite;
    }
    
    /**
     * @return the codigoUorInicioManualTramite
     */
    public String getCodigoUorInicioManualTramite() {
        return codigoUorInicioManualTramite;
    }

    /**
     * @param codigoUorInicioManualTramite the codigoUorInicioManualTramite to set
     */
    public void setCodigoUorInicioManualTramite(String codigoUorInicioManualTramite) {
        this.codigoUorInicioManualTramite = codigoUorInicioManualTramite;
    }

    /**
     * @return the codigoTramite
     */
    public String getCodigoTramite() {
        return codigoTramite;
    }

    /**
     * @param codigoTramite the codigoTramite to set
     */
    public void setCodigoTramite(String codigoTramite) {
        this.codigoTramite = codigoTramite;
    }
    
}