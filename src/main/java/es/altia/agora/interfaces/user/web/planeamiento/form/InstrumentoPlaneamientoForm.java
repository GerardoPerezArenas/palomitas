package es.altia.agora.interfaces.user.web.planeamiento.form;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

public class InstrumentoPlaneamientoForm extends ActionForm {

    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(InstrumentoPlaneamientoForm.class.getName());

    private String tipoRegistro;
    private String codigoSubseccion;
    private String subseccion;
    private String codigoTipo;
    private String tipo;
    private String numero;
    private String anho;
    private String numeroRegistro;
    private String fechaAprobacion;
    private String fechaVigencia;
    private String fechaBaja;
    private String codigoProcedimiento;
    private String numeroProcedimiento;
    private String procedimiento;
    private String codigoAmbito;
    private String ambito;
    private String parcela;
    private String promotor;
    private String codigoOrganoAprobacion;
    private String organoAprobacion;
    private String fechaPublicacion;
    private String numeroPublicacion;
    private String observaciones;
    private String archivo;
    private Collection anotaciones;
    private Collection rectificaciones;
    private Collection subsecciones;
    private Collection tipos;
    private Collection procedimientos;
    private Collection ambitos;
    private Collection promotores;
    private Collection organosAprobacion;
    private Collection codigosPromotor;

    public InstrumentoPlaneamientoForm() {
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

    public String getCodigoTipo() {
        return codigoTipo;
    }

    public void setCodigoTipo(String codigoTipo) {
        this.codigoTipo = codigoTipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public String getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(String fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public String getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(String fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getCodigoProcedimiento() {
        return codigoProcedimiento;
    }

    public void setCodigoProcedimiento(String codigoProcedimiento) {
        this.codigoProcedimiento = codigoProcedimiento;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getNumeroProcedimiento() {
        return numeroProcedimiento;
    }

    public void setNumeroProcedimiento(String numeroProcedimiento) {
        this.numeroProcedimiento = numeroProcedimiento;
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

    public String getPromotor() {
        return promotor;
    }

    public void setPromotor(String promotor) {
        this.promotor = promotor;
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

    public Collection getTipos() {
        return tipos;
    }

    public void setTipos(Collection tipos) {
        this.tipos = tipos;
    }

    public Collection getProcedimientos() {
        return procedimientos;
    }

    public void setProcedimientos(Collection procedimientos) {
        this.procedimientos = procedimientos;
    }

    public Collection getAmbitos() {
        return ambitos;
    }

    public void setAmbitos(Collection ambitos) {
        this.ambitos = ambitos;
    }

    public Collection getPromotores() {
        return promotores;
    }

    public void setPromotores(Collection promotores) {
        this.promotores = promotores;
    }

    public Collection getOrganosAprobacion() {
        return organosAprobacion;
    }

    public void setOrganosAprobacion(Collection organosAprobacion) {
        this.organosAprobacion = organosAprobacion;
    }

    public Collection getCodigosPromotor() {
        return codigosPromotor;
    }

    public void setCodigosPromotor(Collection codigosPromotor) {
        this.codigosPromotor = codigosPromotor;
    }

    public Object clone() {
        InstrumentoPlaneamientoForm instrumentoForm = new InstrumentoPlaneamientoForm();

        instrumentoForm.setTipoRegistro(tipoRegistro);
        instrumentoForm.setCodigoSubseccion(codigoSubseccion);
        instrumentoForm.setSubseccion(subseccion);
        instrumentoForm.setCodigoTipo(codigoTipo);
        instrumentoForm.setTipo(tipo);
        instrumentoForm.setNumero(numero);
        instrumentoForm.setAnho(anho);
        instrumentoForm.setNumeroRegistro(numeroRegistro);
        instrumentoForm.setFechaAprobacion(fechaAprobacion);
        instrumentoForm.setFechaVigencia(fechaVigencia);
        instrumentoForm.setFechaBaja(fechaBaja);
        instrumentoForm.setCodigoProcedimiento(codigoProcedimiento);
        instrumentoForm.setProcedimiento(procedimiento);
        instrumentoForm.setNumeroProcedimiento(numeroProcedimiento);
        instrumentoForm.setCodigoAmbito(codigoAmbito);
        instrumentoForm.setAmbito(ambito);
        instrumentoForm.setParcela(parcela);
        instrumentoForm.setPromotor(promotor);
        instrumentoForm.setCodigoOrganoAprobacion(codigoOrganoAprobacion);
        instrumentoForm.setOrganoAprobacion(organoAprobacion);
        instrumentoForm.setFechaPublicacion(fechaPublicacion);
        instrumentoForm.setNumeroPublicacion(numeroPublicacion);
        instrumentoForm.setObservaciones(observaciones);
        instrumentoForm.setArchivo(archivo);
        instrumentoForm.setAnotaciones(anotaciones);
        instrumentoForm.setRectificaciones(rectificaciones);
        instrumentoForm.setSubsecciones(subsecciones);
        instrumentoForm.setTipos(tipos);
        instrumentoForm.setProcedimientos(procedimientos);
        instrumentoForm.setAmbitos(ambitos);
        instrumentoForm.setPromotores(promotores);
        instrumentoForm.setOrganosAprobacion(organosAprobacion);
        instrumentoForm.setCodigosPromotor(codigosPromotor);

        return instrumentoForm;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    private void reset() {
        tipoRegistro = null;
        codigoSubseccion = null;
        subseccion = null;
        codigoTipo = null;
        tipo = null;
        numero = null;
        anho = null;
        numeroRegistro = null;
        fechaAprobacion = null;
        fechaVigencia = null;
        fechaBaja = null;
        codigoProcedimiento = null;
        procedimiento = null;
        numeroProcedimiento = null;
        codigoAmbito = null;
        ambito = null;
        parcela = null;
        promotor = null;
        codigoOrganoAprobacion = null;
        organoAprobacion = null;
        fechaPublicacion = null;
        numeroPublicacion = null;
        observaciones = null;
        archivo = null;
        anotaciones = null;
        rectificaciones = null;
        subsecciones = null;
        tipos = null;
        procedimientos = null;
        ambitos = null;
        promotores = null;
        organosAprobacion = null;
        codigosPromotor = null;
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
