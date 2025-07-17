package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.ConsultaExpedientesValueObject;
import es.altia.common.service.config.*;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraCampoModuloIntegracionVO;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/**
 * Clase utilizada para capturar o mostrar el estado de un RegistroEntrada
 */
public class ConsultaExpedientesForm extends ActionForm {
    //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(ConsultaExpedientesForm.class.getName());

    //Reutilizamos
    ConsultaExpedientesValueObject consExpVO = new ConsultaExpedientesValueObject();

    public ConsultaExpedientesValueObject getConsultaExpedientes() {
        return consExpVO;
    }

    public void setConsultaExpedientes(ConsultaExpedientesValueObject consExpVO) {
        this.consExpVO = consExpVO;
    }

    private String[] expedientesSeleccionados;
    private Vector resultadoConsulta;
    private String modoConsultaExpRel;
    private String directivaNoRelacionarExp;
    private int numRelacionExpedientes;

    //Mai
    public String codRol;
    public String descRol;
    
    public String[] getExpedientesSeleccionados(){
        return this.expedientesSeleccionados;
    }

    public void setExpedientesSeleccionados(String[] exp){
        this.expedientesSeleccionados = exp;
    }

    public int getNumRelacionExpedientes() {
        return numRelacionExpedientes;
    }

    public void setNumRelacionExpedientes(int numRelacionExpedientes) {
        this.numRelacionExpedientes = numRelacionExpedientes;
    }
    
    
    public String getModoConsultaExpRel() {
        return modoConsultaExpRel;
    }
    
    public void setModoConsultaExpRel(String modoConsultaExpRel) {
        this.modoConsultaExpRel = modoConsultaExpRel;
    }

    public String getDirectivaNoRelacionarExp() {
        return directivaNoRelacionarExp;
    }

    public void setDirectivaNoRelacionarExp(String directivaNoRelacionarExp) {
        this.directivaNoRelacionarExp = directivaNoRelacionarExp;
    }


    /* Seccion donde metemos los metods get y set de los campos del formulario */

    public Vector getResultadoConsulta() {
        return resultadoConsulta;
    }

    public void setResultadoConsulta(Vector resultadoConsulta) {
        this.resultadoConsulta = resultadoConsulta;
    }

    public String getCodigoProcedimiento() {
        return consExpVO.getCodProcedimiento();
    }

    public void setCodigoProcedimiento(String codProcedimiento) {
        consExpVO.setCodProcedimiento(codProcedimiento);
    }
    
    public String getTipoBusqueda() {
        return consExpVO.getTipoBusqueda();
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        consExpVO.setTipoBusqueda(tipoBusqueda);
    }
    
     public HashMap getCamposSuplementarios() {
        return consExpVO.getCamposSuplementarios();
    }

    public void setCamposSuplementarios(HashMap CamposSuplementarios) {
        consExpVO.setCamposSuplementarios(CamposSuplementarios);
    }

    public String getDescProcedimiento() {
        return consExpVO.getDescProcedimiento();
    }

    public void setDescProcedimiento(String descProcedimiento) {
        consExpVO.setDescProcedimiento(descProcedimiento);
    }

    public String getFechaInicio() {
        return consExpVO.getFechaInicio();
    }

    public void setFechaInicio(String fechaInicio) {
        consExpVO.setFechaInicio(fechaInicio);
    }
    
     public String getEjercicioExpediente() {
        return consExpVO.getEjercicioExpediente();
    }

    public void setEjercicioExpediente(String ejercicioExpediente) {
        consExpVO.setEjercicioExpediente(ejercicioExpediente);
    }


    public String getNumeroExpediente() {
        return consExpVO.getNumeroExpediente();
    }

    public void setNumeroExpediente(String numeroExpediente) {
        consExpVO.setNumeroExpediente(numeroExpediente);
    }

    public String getTitular() {
        return consExpVO.getTitular();
    }

    public void setTitular(String titular) {
        consExpVO.setTitular(titular);
    }

    public String getDomicilio() {
        return consExpVO.getDomicilio();
    }

    public void setDomicilio(String domicilio) {
        consExpVO.setDomicilio(domicilio);
    }

    public String getTercero() {
        return consExpVO.getTercero();
    }

    public void setTercero(String tercero) {
        consExpVO.setTercero(tercero);
    }

    public String getVersionTercero() {
        return consExpVO.getVersionTercero();
    }

    public void setVersionTercero(String versionTercero) {
        consExpVO.setVersionTercero(versionTercero);
    }

    public Vector getListaProcedimientos() {
        return consExpVO.getListaProcedimientos();
    }

    public void setListaProcedimientos(Vector listaProcedimientos) {
        consExpVO.setListaProcedimientos(listaProcedimientos);
    }

    public String getNumLineasPaginaListado() {
        return consExpVO.getNumLineasPaginaListado();
    }

    public void setNumLineasPaginaListado(String numLineasPaginaListado) {
        consExpVO.setNumLineasPaginaListado(numLineasPaginaListado);
    }

    public String getPaginaListado() {
        return consExpVO.getPaginaListado();
    }

    public void setPaginaListado(String paginaListado) {
        consExpVO.setPaginaListado(paginaListado);
    }

    public String getCodMunicipio() {
        return consExpVO.getCodMunicipio();
    }

    public void setCodMunicipio(String codMunicipio) {
        consExpVO.setCodMunicipio(codMunicipio);
    }

    public String getEjercicio() {
        return consExpVO.getEjercicio();
    }

    public void setEjercicio(String ejercicio) {
        consExpVO.setEjercicio(ejercicio);
    }

    public String getFechaFin() {
        return consExpVO.getFechaFin();
    }

    public void setFechaFin(String fechaFin) {
        consExpVO.setFechaFin(fechaFin);
    }

    public Vector getListaExpedientesRel() {
        return consExpVO.getListaExpedientesRel();
    }

    public void setListaExpedientesRel(Vector listaExpedientesRel) {
        consExpVO.setListaExpedientesRel(listaExpedientesRel);
    }

    public String getExpRelacionado() {
        return consExpVO.getExpRelacionado();
    }

    public void setExpRelacionado(String expRelacionado) {
        consExpVO.setExpRelacionado(expRelacionado);
    }

    public String getCodMunicipioRel() {
        return consExpVO.getCodMunicipioRel();
    }

    public void setCodMunicipioRel(String codMunicipioRel) {
        consExpVO.setCodMunicipioRel(codMunicipioRel);
    }

    public String getEjercicioRel() {
        return consExpVO.getEjercicioRel();
    }

    public void setEjercicioRel(String ejercicioRel) {
        consExpVO.setEjercicioRel(ejercicioRel);
    }

    public String getNumeroExpedienteRel() {
        return consExpVO.getNumeroExpedienteRel();
    }

    public void setNumeroExpedienteRel(String numeroExpedienteRel) {
        consExpVO.setNumeroExpedienteRel(numeroExpedienteRel);
    }

    public String getCodMunicipioIni() {
        return consExpVO.getCodMunicipioIni();
    }

    public void setCodMunicipioIni(String codMunicipioIni) {
        consExpVO.setCodMunicipioIni(codMunicipioIni);
    }

    public String getEjercicioIni() {
        return consExpVO.getEjercicioIni();
    }

    public void setEjercicioIni(String ejercicioIni) {
        consExpVO.setEjercicioIni(ejercicioIni);
    }

    public String getNumeroExpedienteIni() {
        return consExpVO.getNumeroExpedienteIni();
    }

    public void setNumeroExpedienteIni(String numeroExpedienteIni) {
        consExpVO.setNumeroExpedienteIni(numeroExpedienteIni);
    }

    public String getRespOpcion() {
        return consExpVO.getRespOpcion();
    }

    public void setRespOpcion(String respOpcion) {
        consExpVO.setRespOpcion(respOpcion);
    }

    public String getModoConsulta() {
        return consExpVO.getModoConsulta();
    }

    public void setModoConsulta(String modoConsulta) {
        consExpVO.setModoConsulta(modoConsulta);
    }

    public String getEstado() {
        return consExpVO.getEstado();
    }

    public void setEstado(String estado) {
        consExpVO.setEstado(estado);
    }

    public String getLocalizacion() {
        return consExpVO.getLocalizacion();
    }

    public void setLocalizacion(String localizacion) {
        consExpVO.setLocalizacion(localizacion);
    }

    public String getCodClasifTramite() {
        return consExpVO.getCodClasifTramite();
    }

    public void setCodClasifTramite(String codClasifTramite) {
        consExpVO.setCodClasifTramite(codClasifTramite);
    }

    public String getDescClasifTramite() {
        return consExpVO.getDescClasifTramite();
    }

    public void setDescClasifTramite(String descClasifTramite) {
        consExpVO.setDescClasifTramite(descClasifTramite);
    }

    public Vector getListaClasifTramite() {
        return consExpVO.getListaClasifTramite();
    }

    public void setListaClasifTramite(Vector listaClasifTramite) {
        consExpVO.setListaClasifTramite(listaClasifTramite);
    }

    public String getDeAdjuntar() {
        return consExpVO.getDeAdjuntar();
    }

    public void setDeAdjuntar(String deAdjuntar) {
        consExpVO.setDeAdjuntar(deAdjuntar);
    }
    
    public String getNumeroAnotacion() {
        return consExpVO.getNumeroAnotacion();
    }

    public void setNumeroAnotacion(String numeroAnotacion) {
        consExpVO.setNumeroAnotacion(numeroAnotacion);
    }
    
    public String getEjercicioAnotacion() {
        return consExpVO.getEjercicioAnotacion();
    }

    public void setEjercicioAnotacion(String ejercicioAnotacion) {
        consExpVO.setEjercicioAnotacion(ejercicioAnotacion);
    }    
    
    public String getCodTipoAnotacion() {
        return consExpVO.getCodTipoAnotacion();
    }

    public void setCodTipoAnotacion(String tipoAnotacion) {
        consExpVO.setCodTipoAnotacion(tipoAnotacion);
    }

    public String getDescTipoAnotacion() {
        return consExpVO.getDescTipoAnotacion();
    }

    public void setDescTipoAnotacion(String descTipoAnotacion) {
        consExpVO.setDescTipoAnotacion(descTipoAnotacion);
    }

       public Vector getCamposListados() {
        return consExpVO.getCamposListados();
    }

    public void setCamposListados(Vector lista) {
        consExpVO.setCamposListados(lista);
    }

    public String getAsuntoConsulta() {
        return consExpVO.getAsunto();
    }

    public void setAsuntoConsulta(String asunto) {
        consExpVO.setAsunto(asunto);
    }
    public String getObservaciones() {
        return consExpVO.getObservaciones();
    }

    public void setObservaciones(String observaciones) {
        consExpVO.setObservaciones(observaciones);
    }
    
   public ArrayList<EstructuraCampoModuloIntegracionVO> getCamposConsulta() {
        return  consExpVO.getCamposConsultaModuloExterno();
    }

    public void setCamposConsulta(ArrayList<EstructuraCampoModuloIntegracionVO> camposConsulta) {
        consExpVO.setCamposConsultaModuloExterno(camposConsulta);
    }
    
    
    public String getTipoDocumentoTercero(String tipoDocumento){
        return consExpVO.getTipoDocumentoTercero();        
    }
    
    public void setTipoDocumentoTercero(String tipoDocumento){
        consExpVO.setTipoDocumentoTercero(tipoDocumento);        
    }
    
    
    public String getDocumentoTercero(String documento){
        return consExpVO.getDocumentoTercero();        
    }
    
    public void setDocumentoTercero(String documento){
        consExpVO.setDocumentoTercero(documento);        
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            consExpVO.validate(idioma);
        } catch (ValidationException ve) {
            //Hay errores...
            //Tenemos que traducirlos a formato struts
            errors = validationException(ve, errors);
        }
        return errors;
    }

    /* Función que procesa los errores de validación a formato struts */
    private ActionErrors validationException(ValidationException ve, ActionErrors errors) {
        Iterator iter = ve.getMessages().get();
        while (iter.hasNext()) {
            Message message = (Message) iter.next();
            errors.add(message.getProperty(), new ActionError(message.getMessageKey()));
        }
        return errors;
    }

    
    //Pruebas de Mai
    //Si no pongo esto, me falla el html:property del desplegable de Rol
     public String getCodRol() {
        return consExpVO.getCodigoRol();
    }

    public void setCodRol(String codRol){
     consExpVO.setCodigoRol(codRol);
    }
    
     public String getDescRol() {
        return consExpVO.getCodigoRol();
    }

    public void setDescRol(String descRol){
       consExpVO.setDescripcionRol(descRol);
    }
    //Neceario este trozo de codigo para qrue no falle el desplegable de Rol
}
