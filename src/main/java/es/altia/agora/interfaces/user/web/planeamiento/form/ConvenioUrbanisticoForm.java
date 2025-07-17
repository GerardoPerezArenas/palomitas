package es.altia.agora.interfaces.user.web.planeamiento.form;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;
import es.altia.agora.interfaces.user.web.planeamiento.form.mantenimiento.MantenimientoAmbitoForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.Iterator;
import java.util.Collection;

public class ConvenioUrbanisticoForm extends ActionForm {

    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(ConvenioUrbanisticoForm.class.getName());


    private String tipoRegistro;
    private String codigoSubseccion;
    private String subseccion;
    private String numero;
    private String anho;
    private String numeroRegistro;
    private String fechaAprobacion;
    private String fechaBaja;
    private String codigoAmbito;
    private String ambito;
    private String parcela;
    private String codigoOrganoAprobacion;
    private String organoAprobacion;
    private String objetoConvenio;
    private String fechaPublicacion;
    private String numeroPublicacion;
    private String observaciones;
    private String archivo;
    private Collection anotaciones;
    private Collection rectificaciones;
    private Collection subsecciones;
    private Collection ambitos;
    private Collection organosAprobacion;
    private String firmante;
    private Collection partesFirmantes;

    public ConvenioUrbanisticoForm() {
        reset();
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getCodigoSubseccion() {
        return codigoSubseccion;
    }

    public void setCodigoSubseccion(String codigoSubseccion) {
        this.codigoSubseccion = codigoSubseccion;
    }

    public String getSubseccion() {
        return subseccion;
    }

    public void setSubseccion(String subseccion) {
        this.subseccion = subseccion;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAnho() {
        return anho;
    }

    public void setAnho(String anho) {
        this.anho = anho;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public String getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(String fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(String fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getCodigoAmbito() {
        return codigoAmbito;
    }

    public void setCodigoAmbito(String codigoAmbito) {
        this.codigoAmbito = codigoAmbito;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getParcela() {
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public String getCodigoOrganoAprobacion() {
        return codigoOrganoAprobacion;
    }

    public void setCodigoOrganoAprobacion(String codigoOrganoAprobacion) {
        this.codigoOrganoAprobacion = codigoOrganoAprobacion;
    }

    public String getOrganoAprobacion() {
        return organoAprobacion;
    }

    public void setOrganoAprobacion(String organoAprobacion) {
        this.organoAprobacion = organoAprobacion;
    }

    public String getObjetoConvenio() {
        return objetoConvenio;
    }

    public void setObjetoConvenio(String objetoConvenio) {
        this.objetoConvenio = objetoConvenio;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getNumeroPublicacion() {
        return numeroPublicacion;
    }

    public void setNumeroPublicacion(String numeroPublicacion) {
        this.numeroPublicacion = numeroPublicacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Collection getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(Collection anotaciones) {
        this.anotaciones = anotaciones;
    }

    public Collection getRectificaciones() {
        return rectificaciones;
    }

    public void setRectificaciones(Collection rectificaciones) {
        this.rectificaciones = rectificaciones;
    }

    public Collection getSubsecciones() {
        return subsecciones;
    }

    public void setSubsecciones(Collection subsecciones) {
        this.subsecciones = subsecciones;
    }

    public Collection getAmbitos() {
        return ambitos;
    }

    public void setAmbitos(Collection ambitos) {
        this.ambitos = ambitos;
    }

    public Collection getOrganosAprobacion() {
        return organosAprobacion;
    }

    public void setOrganosAprobacion(Collection organosAprobacion) {
        this.organosAprobacion = organosAprobacion;
    }

    public String getFirmante() {
        return firmante;
    }

    public void setFirmante(String firmante) {
        this.firmante = firmante;
    }

    public Collection getPartesFirmantes() {
        return partesFirmantes;
    }

    public void setPartesFirmantes(Collection partesFirmantes) {
        this.partesFirmantes = partesFirmantes;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    public Object clone() {
        ConvenioUrbanisticoForm convenioForm = new ConvenioUrbanisticoForm();

        convenioForm.setTipoRegistro(tipoRegistro);
        convenioForm.setCodigoSubseccion(codigoSubseccion);
        convenioForm.setSubseccion(subseccion);
        convenioForm.setNumero(numero);
        convenioForm.setAnho(anho);
        convenioForm.setNumeroRegistro(numeroRegistro);
        convenioForm.setFechaAprobacion(fechaAprobacion);
        convenioForm.setFechaBaja(fechaBaja);
        convenioForm.setCodigoAmbito(codigoAmbito);
        convenioForm.setAmbito(ambito);
        convenioForm.setParcela(parcela);
        convenioForm.setCodigoOrganoAprobacion(codigoOrganoAprobacion);
        convenioForm.setOrganoAprobacion(organoAprobacion);
        convenioForm.setObjetoConvenio(objetoConvenio);
        convenioForm.setFechaPublicacion(fechaPublicacion);
        convenioForm.setNumeroPublicacion(numeroPublicacion);
        convenioForm.setObservaciones(observaciones);
        convenioForm.setAnotaciones(anotaciones);
        convenioForm.setRectificaciones(rectificaciones);
        convenioForm.setSubsecciones(subsecciones);
        convenioForm.setAmbitos(ambitos);
        convenioForm.setOrganosAprobacion(organosAprobacion);
        convenioForm.setFirmante(firmante);
        convenioForm.setPartesFirmantes(partesFirmantes);

        return convenioForm;

    }

    private void reset() {
        tipoRegistro = null;
        codigoSubseccion = null;
        subseccion = null;
        numero = null;
        anho = null;
        numeroRegistro = null;
        fechaAprobacion = null;
        fechaBaja = null;
        codigoAmbito = null;
        ambito = null;
        parcela = null;
        codigoOrganoAprobacion = null;
        organoAprobacion = null;
        objetoConvenio = null;
        fechaPublicacion = null;
        numeroPublicacion = null;
        observaciones = null;
        archivo = null;
        anotaciones = null;
        rectificaciones = null;
        subsecciones = null;
        ambitos = null;
        organosAprobacion = null;
        firmante = null;
        partesFirmantes = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        return errors;
    }

    /* Función que procesa los errores de validación a formato struts */
    private ActionErrors validationException(ValidationException ve,ActionErrors errors){
      Iterator iter = ve.getMessages().get();
      while (iter.hasNext()) {
        Message message = (Message)iter.next();
        errors.add(message.getProperty(), new ActionError(message.getMessageKey()));
      }
      return errors;
    }
}
