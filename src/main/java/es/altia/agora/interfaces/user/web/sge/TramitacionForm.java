package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.administracion.mantenimiento.CriterioBusquedaPendientesVO;
import es.altia.agora.business.administracion.mantenimiento.ValoresCriterioBusquedaExpPendientesVO;
import es.altia.agora.business.registro.mantenimiento.MantClasifAsuntosValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.persistence.manual.TecnicoReferenciaDTO;
import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import java.util.Vector;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Clase utilizada para capturar o mostrar el estado de una Tramitacion
 */
public class TramitacionForm extends ActionForm {
    //Queremos usar el fichero de configuración technical

    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log
            = LogFactory.getLog(TramitacionForm.class.getName());

    private String fechaDesde;
    private String fechaHasta;

    private String fechaInicio;
    private String fechaFin;
    // Si las observaciones son obligatorias o no al rechazar
    private boolean obsObligatorias;
    private Vector camposListados;
    private boolean tramiteInicioConUnidadesTramitadoras;
    private int unidadTramiteInicioSeleccionada;
    private String leyenda;
    //Reutilizamos
    TramitacionValueObject tramVO = new TramitacionValueObject();

    private boolean tieneProcedimientoVistaExpedientesPropia = false;
    private Vector camposListadoProcedimiento;
    /**
     * ****************************
     */
    private ArrayList<CriterioBusquedaPendientesVO> criteriosBusquedaExpPendientes = null;
    private String codigoCriterioBusqueda;
    private String tipoCampoCriterioBusqueda;
    private String operadorCriterioBusqueda;
    private String valoresCriterioBusqueda;
    private String campoSuplementarioCriterioBusqueda;
    private String tipoCampoSuplementarioCriterioBusqueda;
    private String codigoDesplegable;
    private ValoresCriterioBusquedaExpPendientesVO valoresCriterioBusquedaExpPendientes;
    /**
     * ****************************
     */

    /**
     * * prueba ***
     */
    private boolean cargarNuevosCriteriosBuzonEntrada = false;
    private String nombreBuzonEntrada = null;
    private String primerApellidoBuzonEntrada = null;
    private String segundoApellidoBuzonEntrada = null;
    private Vector listaUnidadesDestinoBuzon = null;
    private Vector listaAsuntosCodificadosBuzon = null;
    private String codAsuntoBuzonEntrada = null;
    private String descAsuntoBuzonEntrada = null;
    private String codUnidadDestinoBuzonEntrada = null;
    private String descUnidadDestinoBuzonEntrada = null;
    private String documentoBuzonEntrada = null;
    private String codUnidadInternoDestinoBuzonEntrada = null;
    private String ejercicioBuzonEntrada = null;
    private String numAnotacionBuzonEntrada = null;
    private String codClasificacionAsuntos;
    private String descClasificacionAsuntos;
    private String unidadRegistroClasifAsuntoSeleccionado = null;
    private String codTecnicoReferencia;
    private String descTecnicoReferencia;
    private List<TecnicoReferenciaDTO> tecnicosReferencia;
    private String codRegPendCatalogacion;
    private String descRegPendCatalogacion;

    /**
     * * prueba ***
     */
    private String codAsuntoSeleccionado = null;
    private String unidadRegistroAsuntoSeleccionado = null;
    private String tipoRegistroAsuntoSeleccionado = null;

    /**
     * * historico ***
     */
    private boolean cargarNuevosCriteriosBuzonEntradaHistorico = false;
    private String nombreBuzonEntradaHistorico = null;
    private String primerApellidoBuzonEntradaHistorico = null;
    private String segundoApellidoBuzonEntradaHistorico = null;
    private String codAsuntoBuzonEntradaHistorico = null;
    private String descAsuntoBuzonEntradaHistorico = null;
    private String codUnidadDestinoBuzonEntradaHistorico = null;
    private String descUnidadDestinoBuzonEntradaHistorico = null;
    private String documentoBuzonEntradaHistorico = null;
    private String codUnidadInternoDestinoBuzonEntradaHistorico = null;

    private String fechaDesdeHistorico;
    private String fechaHastaHistorico;
    private String ejercicioBuzonEntradaHistorico = null;
    private String numAnotacionBuzonEntradaHistorico = null;

    private List<MantClasifAsuntosValueObject> listaClasificacionAsuntos;
    private Vector listaUnidadesDestinoBuzonHistorico = null;
    private Vector listaAsuntosCodificadosBuzonHistorico = null;
    private String codAsuntoSeleccionadoHistorico = null;
    private String unidadRegistroAsuntoSeleccionadoHistorico = null;
    private String tipoRegistroAsuntoSeleccionadoHistorico = null;

    private Boolean opcionPermanencia = false;
    private Boolean valorOpcionPermanencia = false;
    private String expedientes_eliminar = "";

    private String codUnidadOrganicaAnotacion = null;
    private String descUnidadOrganicaAnotacion = null;

    private String codUnidadOrganicaAnotacionHistorico = null;
    private String descUnidadOrganicaAnotacionHistorico = null;
    private String codUnidadInternoAnotacionHistorico = null;

    private String codRegistroTelematico;
    private String descRegistroTelematico;

    private String usuarioTramitador;

    public String getUsuarioTramitador() {
        return usuarioTramitador;
    }

    public void setUsuarioTramitador(String usuarioTramitador) {
        this.usuarioTramitador = usuarioTramitador;
    }

    public String getCodRegistroTelematico() {
        return codRegistroTelematico;
    }

    public void setCodRegistroTelematico(String codRegistroTelematico) {
        this.codRegistroTelematico = codRegistroTelematico;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * * historico ***
     */
    public boolean getCargarNuevosCriteriosBuzonEntrada() {
        return this.cargarNuevosCriteriosBuzonEntrada;
    }

    public void setCargarNuevosCriteriosBuzonEntrada(boolean cargar) {
        this.cargarNuevosCriteriosBuzonEntrada = cargar;
    }

    public String getLeyenda() {
        return leyenda;
    }

    public void setLeyenda(String leyenda) {
        this.leyenda = leyenda;
    }

    public int getUnidadTramiteInicioSeleccionada() {
        return this.unidadTramiteInicioSeleccionada;
    }

    public void setUnidadTramiteInicioSeleccionada(int unidadTramiteInicioSeleccionada) {
        this.unidadTramiteInicioSeleccionada = unidadTramiteInicioSeleccionada;
    }

    public String getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public String getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public boolean isObsObligatorias() {
        return obsObligatorias;
    }

    public void setObsObligatorias(boolean obsObligatorias) {
        this.obsObligatorias = obsObligatorias;
    }

    public Vector getCamposListados() {
        return camposListados;
    }

    public void setCamposListados(Vector camposListados) {
        this.camposListados = camposListados;
    }

    public TramitacionValueObject getTramitacion() {
        return tramVO;
    }

    public void setTramitacion(TramitacionValueObject tramVO) {
        this.tramVO = tramVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */
    public String getAsunto() {
        return tramVO.getAsunto();
    }

    public void setAsunto(String asunto) {
        tramVO.setAsunto(asunto);
    }

    public String getCodDepartamento() {
        return tramVO.getCodDepartamento();
    }

    public void setCodDepartamento(String codDepartamento) {
        tramVO.setCodDepartamento(codDepartamento);
    }

    public String getCodUnidadRegistro() {
        return tramVO.getCodUnidadRegistro();
    }

    public void setCodUnidadRegistro(String codUnidadRegistro) {
        tramVO.setCodUnidadRegistro(codUnidadRegistro);
    }

    public String getEjerNum() {
        return tramVO.getEjerNum();
    }

    public void setEjerNum(String ejerNum) {
        tramVO.setEjerNum(ejerNum);
    }

    public String getFechaAnotacion() {
        return tramVO.getFechaAnotacion();
    }

    public void setFechaAnotacion(String fechaAnotacion) {
        tramVO.setFechaAnotacion(fechaAnotacion);
    }

    public String getRemitente() {
        return tramVO.getRemitente();
    }

    public void setRemitente(String remitente) {
        tramVO.setRemitente(remitente);
    }

    public String getTipoRegistro() {
        return tramVO.getTipoRegistro();
    }

    public void setTipoRegistro(String tipoRegistro) {
        tramVO.setTipoRegistro(tipoRegistro);
    }

    public String getNumeroExpediente() {
        return tramVO.getNumeroExpediente();
    }

    public void setNumeroExpediente(String numeroExpediente) {
        tramVO.setNumeroExpediente(numeroExpediente);
    }

    public String getNumeroExpedienteAntiguo() {
        return tramVO.getNumeroExpedienteAntiguo();
    }

    public void setNumeroExpedienteAntiguo(String numeroExpedienteAntiguo) {
        tramVO.setNumeroExpedienteAntiguo(numeroExpedienteAntiguo);
    }

    public String getNumLineasPaginaListado() {
        return tramVO.getNumLineasPaginaListado();
    }

    public void setNumLineasPaginaListado(String numLineasPaginaListado) {
        tramVO.setNumLineasPaginaListado(numLineasPaginaListado);
    }

    public String getPaginaListado() {
        return tramVO.getPaginaListado();
    }

    public void setPaginaListado(String paginaListado) {
        tramVO.setPaginaListado(paginaListado);
    }

    public String getNumLineasPaginaListadoE() {
        return tramVO.getNumLineasPaginaListadoE();
    }

    public void setNumLineasPaginaListadoE(String numLineasPaginaListadoE) {
        tramVO.setNumLineasPaginaListadoE(numLineasPaginaListadoE);
    }

    public String getPaginaListadoE() {
        return tramVO.getPaginaListadoE();
    }

    public void setPaginaListadoE(String paginaListadoE) {
        tramVO.setPaginaListadoE(paginaListadoE);
    }

    public String getCodMunicipio() {
        return tramVO.getCodMunicipio();
    }

    public void setCodMunicipio(String codMunicipio) {
        tramVO.setCodMunicipio(codMunicipio);
    }

    public String getCodProcedimiento() {
        return tramVO.getCodProcedimiento();
    }

    public void setCodProcedimiento(String codProcedimiento) {
        tramVO.setCodProcedimiento(codProcedimiento);
    }

    public String getEjercicio() {
        return tramVO.getEjercicio();
    }

    public void setEjercicio(String ejercicio) {
        tramVO.setEjercicio(ejercicio);
    }

    public String getNumero() {
        return tramVO.getNumero();
    }

    public void setNumero(String numero) {
        tramVO.setNumero(numero);
    }

    public Vector getListaProcedimientos() {
        return tramVO.getListaProcedimientos();
    }

    public void setListaProcedimientos(Vector listaProcedimientos) {
        tramVO.setListaProcedimientos(listaProcedimientos);
    }

    public String getFechaInicioExpediente() {
        return tramVO.getFechaInicioExpediente();
    }

    public void setFechaInicioExpediente(String fechaInicioExpediente) {
        tramVO.setFechaInicioExpediente(fechaInicioExpediente);
    }

    public String getTitular() {
        return tramVO.getTitular();
    }

    public void setTitular(String titular) {
        tramVO.setTitular(titular);
    }

    public String getDescProcedimiento() {
        return tramVO.getDescProcedimiento();
    }

    public void setDescProcedimiento(String descProcedimiento) {
        tramVO.setDescProcedimiento(descProcedimiento);
    }

    public String getRespOpcion() {
        return tramVO.getRespOpcion();
    }

    public void setRespOpcion(String respOpcion) {
        tramVO.setRespOpcion(respOpcion);
    }

    public ActionErrors validate(
            ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date desde = simpleDateFormat.parse(fechaDesde);
            Date hasta = simpleDateFormat.parse(fechaHasta);
            if (desde.after(hasta)) {
                errors.add("fechaHasta", new ActionError("Sge.TramitacionForm.fechas"));
            }
        } catch (ParseException pe) {
            errors = validationException(pe, errors);
        }
        return errors;

    }

    /* Función que procesa los errores de validación a formato struts */

    private ActionErrors validationException(ParseException pe, ActionErrors errors) {
        String message = pe.getMessage();
        errors.add(message, new ActionError(message));
        return errors;
    }

    public String getOrigenServicio() {
        return tramVO.getOrigen();
    }

    public void setOrigenServicio(String origenServicio) {
        tramVO.setOrigen(origenServicio);
    }

    public void setObservaciones(String observaciones) {
        tramVO.setObservaciones(observaciones);
    }

    public String getObservaciones() {
        return tramVO.getObservaciones();
    }

    /**
     * @return the tramiteInicioConUnidadesTramitadoras
     */
    public boolean isTramiteInicioConUnidadesTramitadoras() {
        return tramiteInicioConUnidadesTramitadoras;
    }

    /**
     * @param tramiteInicioConUnidadesTramitadoras the
     * tramiteInicioConUnidadesTramitadoras to set
     */
    public void setTramiteInicioConUnidadesTramitadoras(boolean tramiteInicioConUnidadesTramitadoras) {
        this.tramiteInicioConUnidadesTramitadoras = tramiteInicioConUnidadesTramitadoras;
    }

    /**
     * Indica si el procedimiento tiene vista de expedientes propia
     *
     * @return Un boolean
     */
    public boolean getTieneProcedimientoVistaExpedientesPropia() {
        return this.tieneProcedimientoVistaExpedientesPropia;
    }

    /**
     * Permite indica si un procedimiento tiene vista de expedientes propia o si
     * por el contrario, se carga la vista general del mismo
     *
     * @param flag: boolean
     */
    public void setTieneProcedimientoVistaExpedientesPropia(boolean flag) {
        this.tieneProcedimientoVistaExpedientesPropia = flag;
    }

    /**
     * @return the camposListadoProcedimiento
     */
    public Vector getCamposListadoProcedimiento() {
        return camposListadoProcedimiento;
    }

    /**
     * @param camposListadoProcedimiento the camposListadoProcedimiento to set
     */
    public void setCamposListadoProcedimiento(Vector camposListadoProcedimiento) {
        this.camposListadoProcedimiento = camposListadoProcedimiento;
    }

    /**
     * @return the criteriosBusquedaExpPendientes
     */
    public ArrayList<CriterioBusquedaPendientesVO> getCriteriosBusquedaExpPendientes() {
        return criteriosBusquedaExpPendientes;
    }

    /**
     * @param criteriosBusquedaExpPendientes the criteriosBusquedaExpPendientes
     * to set
     */
    public void setCriteriosBusquedaExpPendientes(ArrayList<CriterioBusquedaPendientesVO> criteriosBusquedaExpPendientes) {
        this.criteriosBusquedaExpPendientes = criteriosBusquedaExpPendientes;
    }

    /**
     * @return the codigoCriterioBusqueda
     */
    public String getCodigoCriterioBusqueda() {
        return codigoCriterioBusqueda;
    }

    /**
     * @param codigoCriterioBusqueda the codigoCriterioBusqueda to set
     */
    public void setCodigoCriterioBusqueda(String codigoCriterioBusqueda) {
        this.codigoCriterioBusqueda = codigoCriterioBusqueda;
    }

    /**
     * @return the tipoCampoCriterioBusqueda
     */
    public String getTipoCampoCriterioBusqueda() {
        return tipoCampoCriterioBusqueda;
    }

    /**
     * @param tipoCampoCriterioBusqueda the tipoCampoCriterioBusqueda to set
     */
    public void setTipoCampoCriterioBusqueda(String tipoCampoCriterioBusqueda) {
        this.tipoCampoCriterioBusqueda = tipoCampoCriterioBusqueda;
    }

    /**
     * @return the operadorCriterioBusqueda
     */
    public String getOperadorCriterioBusqueda() {
        return operadorCriterioBusqueda;
    }

    /**
     * @param operadorCriterioBusqueda the operadorCriterioBusqueda to set
     */
    public void setOperadorCriterioBusqueda(String operadorCriterioBusqueda) {
        this.operadorCriterioBusqueda = operadorCriterioBusqueda;
    }

    /**
     * @return the valoresCriterioBusqueda
     */
    public String getValoresCriterioBusqueda() {
        return valoresCriterioBusqueda;
    }

    /**
     * @param valoresCriterioBusqueda the valoresCriterioBusqueda to set
     */
    public void setValoresCriterioBusqueda(String valoresCriterioBusqueda) {
        this.valoresCriterioBusqueda = valoresCriterioBusqueda;
    }

    /**
     * @return the campoSuplementarioCriterioBusqueda
     */
    public String getCampoSuplementarioCriterioBusqueda() {
        return campoSuplementarioCriterioBusqueda;
    }

    /**
     * @param campoSuplementarioCriterioBusqueda the
     * campoSuplementarioCriterioBusqueda to set
     */
    public void setCampoSuplementarioCriterioBusqueda(String campoSuplementarioCriterioBusqueda) {
        this.campoSuplementarioCriterioBusqueda = campoSuplementarioCriterioBusqueda;
    }

    /**
     * @return the tipoCampoSuplementarioCriterioBusqueda
     */
    public String getTipoCampoSuplementarioCriterioBusqueda() {
        return tipoCampoSuplementarioCriterioBusqueda;
    }

    /**
     * @param tipoCampoSuplementarioCriterioBusqueda the
     * tipoCampoSuplementarioCriterioBusqueda to set
     */
    public void setTipoCampoSuplementarioCriterioBusqueda(String tipoCampoSuplementarioCriterioBusqueda) {
        this.tipoCampoSuplementarioCriterioBusqueda = tipoCampoSuplementarioCriterioBusqueda;
    }

    /**
     * @return the codigoDesplegable
     */
    public String getCodigoDesplegable() {
        return codigoDesplegable;
    }

    /**
     * @param codigoDesplegable the codigoDesplegable to set
     */
    public void setCodigoDesplegable(String codigoDesplegable) {
        this.codigoDesplegable = codigoDesplegable;
    }

    /**
     * @return the valoresCriterioBusquedaExpPendientes
     */
    public ValoresCriterioBusquedaExpPendientesVO getValoresCriterioBusquedaExpPendientes() {
        return valoresCriterioBusquedaExpPendientes;
    }

    /**
     * @param valoresCriterioBusquedaExpPendientes the
     * valoresCriterioBusquedaExpPendientes to set
     */
    public void setValoresCriterioBusquedaExpPendientes(ValoresCriterioBusquedaExpPendientesVO valoresCriterioBusquedaExpPendientes) {
        this.valoresCriterioBusquedaExpPendientes = valoresCriterioBusquedaExpPendientes;
    }

    /**
     * @return the nombreBuzonEntrada
     */
    public String getNombreBuzonEntrada() {
        return nombreBuzonEntrada;
    }

    /**
     * @param nombreBuzonEntrada the nombreBuzonEntrada to set
     */
    public void setNombreBuzonEntrada(String nombreBuzonEntrada) {
        this.nombreBuzonEntrada = nombreBuzonEntrada;
    }

    /**
     * @return the primerApellidoBuzonEntrada
     */
    public String getPrimerApellidoBuzonEntrada() {
        return primerApellidoBuzonEntrada;
    }

    /**
     * @param primerApellidoBuzonEntrada the primerApellidoBuzonEntrada to set
     */
    public void setPrimerApellidoBuzonEntrada(String primerApellidoBuzonEntrada) {
        this.primerApellidoBuzonEntrada = primerApellidoBuzonEntrada;
    }

    /**
     * @return the segundoApellidoBuzonEntrada
     */
    public String getSegundoApellidoBuzonEntrada() {
        return segundoApellidoBuzonEntrada;
    }

    /**
     * @param segundoApellidoBuzonEntrada the segundoApellidoBuzonEntrada to set
     */
    public void setSegundoApellidoBuzonEntrada(String segundoApellidoBuzonEntrada) {
        this.segundoApellidoBuzonEntrada = segundoApellidoBuzonEntrada;
    }

    /**
     * @return the listaUnidadesDestinoBuzon
     */
    public Vector getListaUnidadesDestinoBuzon() {
        return listaUnidadesDestinoBuzon;
    }

    /**
     * @param listaUnidadesDestinoBuzon the listaUnidadesDestinoBuzon to set
     */
    public void setListaUnidadesDestinoBuzon(Vector listaUnidadesDestinoBuzon) {
        this.listaUnidadesDestinoBuzon = listaUnidadesDestinoBuzon;
    }

    /**
     * @return the codAsuntoBuzonEntrada
     */
    public String getCodAsuntoBuzonEntrada() {
        return codAsuntoBuzonEntrada;
    }

    /**
     * @param codAsuntoBuzonEntrada the codAsuntoBuzonEntrada to set
     */
    public void setCodAsuntoBuzonEntrada(String codAsuntoBuzonEntrada) {
        this.codAsuntoBuzonEntrada = codAsuntoBuzonEntrada;
    }

    /**
     * @return the descAsuntoBuzonEntrada
     */
    public String getDescAsuntoBuzonEntrada() {
        return descAsuntoBuzonEntrada;
    }

    /**
     * @param descAsuntoBuzonEntrada the descAsuntoBuzonEntrada to set
     */
    public void setDescAsuntoBuzonEntrada(String descAsuntoBuzonEntrada) {
        this.descAsuntoBuzonEntrada = descAsuntoBuzonEntrada;
    }

    /**
     * @return the listaAsuntosCodificadosBuzon
     */
    public Vector getListaAsuntosCodificadosBuzon() {
        return listaAsuntosCodificadosBuzon;
    }

    /**
     * @param listaAsuntosCodificadosBuzon the listaAsuntosCodificadosBuzon to
     * set
     */
    public void setListaAsuntosCodificadosBuzon(Vector listaAsuntosCodificadosBuzon) {
        this.listaAsuntosCodificadosBuzon = listaAsuntosCodificadosBuzon;
    }

    /**
     * @return the codUnidadDestinoBuzonEntrada
     */
    public String getCodUnidadDestinoBuzonEntrada() {
        return codUnidadDestinoBuzonEntrada;
    }

    /**
     * @param codUnidadDestinoBuzonEntrada the codUnidadDestinoBuzonEntrada to
     * set
     */
    public void setCodUnidadDestinoBuzonEntrada(String codUnidadDestinoBuzonEntrada) {
        this.codUnidadDestinoBuzonEntrada = codUnidadDestinoBuzonEntrada;
    }

    /**
     * @return the descUnidadDestinoBuzonEntrada
     */
    public String getDescUnidadDestinoBuzonEntrada() {
        return descUnidadDestinoBuzonEntrada;
    }

    /**
     * @param descUnidadDestinoBuzonEntrada the descUnidadDestinoBuzonEntrada to
     * set
     */
    public void setDescUnidadDestinoBuzonEntrada(String descUnidadDestinoBuzonEntrada) {
        this.descUnidadDestinoBuzonEntrada = descUnidadDestinoBuzonEntrada;
    }

    /**
     * @return the documentoBuzonEntrada
     */
    public String getDocumentoBuzonEntrada() {
        return documentoBuzonEntrada;
    }

    /**
     * @param documentoBuzonEntrada the documentoBuzonEntrada to set
     */
    public void setDocumentoBuzonEntrada(String documentoBuzonEntrada) {
        this.documentoBuzonEntrada = documentoBuzonEntrada;
    }

    /**
     * @return the codUnidadInternoDestinoBuzonEntrada
     */
    public String getCodUnidadInternoDestinoBuzonEntrada() {
        return codUnidadInternoDestinoBuzonEntrada;
    }

    /**
     * @param codUnidadInternoDestinoBuzonEntrada the
     * codUnidadInternoDestinoBuzonEntrada to set
     */
    public void setCodUnidadInternoDestinoBuzonEntrada(String codUnidadInternoDestinoBuzonEntrada) {
        this.codUnidadInternoDestinoBuzonEntrada = codUnidadInternoDestinoBuzonEntrada;
    }

    /**
     * @return the codAsuntoSeleccionado
     */
    public String getCodAsuntoSeleccionado() {
        return codAsuntoSeleccionado;
    }

    /**
     * @param codAsuntoSeleccionado the codAsuntoSeleccionado to set
     */
    public void setCodAsuntoSeleccionado(String codAsuntoSeleccionado) {
        this.codAsuntoSeleccionado = codAsuntoSeleccionado;
    }

    /**
     * @return the unidadRegistroAsuntoSeleccionado
     */
    public String getUnidadRegistroAsuntoSeleccionado() {
        return unidadRegistroAsuntoSeleccionado;
    }

    /**
     * @param unidadRegistroAsuntoSeleccionado the
     * unidadRegistroAsuntoSeleccionado to set
     */
    public void setUnidadRegistroAsuntoSeleccionado(String unidadRegistroAsuntoSeleccionado) {
        this.unidadRegistroAsuntoSeleccionado = unidadRegistroAsuntoSeleccionado;
    }

    /**
     * @return the tipoRegistroAsuntoSeleccionado
     */
    public String getTipoRegistroAsuntoSeleccionado() {
        return tipoRegistroAsuntoSeleccionado;
    }

    /**
     * @param tipoRegistroAsuntoSeleccionado the tipoRegistroAsuntoSeleccionado
     * to set
     */
    public void setTipoRegistroAsuntoSeleccionado(String tipoRegistroAsuntoSeleccionado) {
        this.tipoRegistroAsuntoSeleccionado = tipoRegistroAsuntoSeleccionado;
    }

    public String getEjercicioBuzonEntrada() {
        return ejercicioBuzonEntrada;
    }

    public void setEjercicioBuzonEntrada(String ejercicioBuzonEntrada) {
        this.ejercicioBuzonEntrada = ejercicioBuzonEntrada;
    }

    public String getNumAnotacionBuzonEntrada() {
        return numAnotacionBuzonEntrada;
    }

    public void setNumAnotacionBuzonEntrada(String numAnotacionBuzonEntrada) {
        this.numAnotacionBuzonEntrada = numAnotacionBuzonEntrada;
    }

    public boolean isCargarNuevosCriteriosBuzonEntradaHistorico() {
        return cargarNuevosCriteriosBuzonEntradaHistorico;
    }

    public void setCargarNuevosCriteriosBuzonEntradaHistorico(boolean cargar) {
        this.cargarNuevosCriteriosBuzonEntradaHistorico = cargar;
    }

    public String getCodAsuntoBuzonEntradaHistorico() {
        return codAsuntoBuzonEntradaHistorico;
    }

    public void setCodAsuntoBuzonEntradaHistorico(String codAsuntoBuzonEntradaHistorico) {
        this.codAsuntoBuzonEntradaHistorico = codAsuntoBuzonEntradaHistorico;
    }

    public String getCodAsuntoSeleccionadoHistorico() {
        return codAsuntoSeleccionadoHistorico;
    }

    public void setCodAsuntoSeleccionadoHistorico(String codAsuntoSeleccionadoHistorico) {
        this.codAsuntoSeleccionadoHistorico = codAsuntoSeleccionadoHistorico;
    }

    public String getCodUnidadDestinoBuzonEntradaHistorico() {
        return codUnidadDestinoBuzonEntradaHistorico;
    }

    public void setCodUnidadDestinoBuzonEntradaHistorico(String codUnidadDestinoBuzonEntradaHistorico) {
        this.codUnidadDestinoBuzonEntradaHistorico = codUnidadDestinoBuzonEntradaHistorico;
    }

    public String getCodUnidadInternoDestinoBuzonEntradaHistorico() {
        return codUnidadInternoDestinoBuzonEntradaHistorico;
    }

    public void setCodUnidadInternoDestinoBuzonEntradaHistorico(String codUnidadInternoDestinoBuzonEntradaHistorico) {
        this.codUnidadInternoDestinoBuzonEntradaHistorico = codUnidadInternoDestinoBuzonEntradaHistorico;
    }

    public String getDescAsuntoBuzonEntradaHistorico() {
        return descAsuntoBuzonEntradaHistorico;
    }

    public void setDescAsuntoBuzonEntradaHistorico(String descAsuntoBuzonEntradaHistorico) {
        this.descAsuntoBuzonEntradaHistorico = descAsuntoBuzonEntradaHistorico;
    }

    public String getDescUnidadDestinoBuzonEntradaHistorico() {
        return descUnidadDestinoBuzonEntradaHistorico;
    }

    public void setDescUnidadDestinoBuzonEntradaHistorico(String descUnidadDestinoBuzonEntradaHistorico) {
        this.descUnidadDestinoBuzonEntradaHistorico = descUnidadDestinoBuzonEntradaHistorico;
    }

    public String getDocumentoBuzonEntradaHistorico() {
        return documentoBuzonEntradaHistorico;
    }

    public void setDocumentoBuzonEntradaHistorico(String documentoBuzonEntradaHistorico) {
        this.documentoBuzonEntradaHistorico = documentoBuzonEntradaHistorico;
    }

    public String getEjercicioBuzonEntradaHistorico() {
        return ejercicioBuzonEntradaHistorico;
    }

    public void setEjercicioBuzonEntradaHistorico(String ejercicioBuzonEntradaHistorico) {
        this.ejercicioBuzonEntradaHistorico = ejercicioBuzonEntradaHistorico;
    }

    public String getFechaDesdeHistorico() {
        return fechaDesdeHistorico;
    }

    public void setFechaDesdeHistorico(String fechaDesdeHistorico) {
        this.fechaDesdeHistorico = fechaDesdeHistorico;
    }

    public String getFechaHastaHistorico() {
        return fechaHastaHistorico;
    }

    public void setFechaHastaHistorico(String fechaHastaHistorico) {
        this.fechaHastaHistorico = fechaHastaHistorico;
    }

    public String getNombreBuzonEntradaHistorico() {
        return nombreBuzonEntradaHistorico;
    }

    public void setNombreBuzonEntradaHistorico(String nombreBuzonEntradaHistorico) {
        this.nombreBuzonEntradaHistorico = nombreBuzonEntradaHistorico;
    }

    public String getNumAnotacionBuzonEntradaHistorico() {
        return numAnotacionBuzonEntradaHistorico;
    }

    public void setNumAnotacionBuzonEntradaHistorico(String numAnotacionBuzonEntradaHistorico) {
        this.numAnotacionBuzonEntradaHistorico = numAnotacionBuzonEntradaHistorico;
    }

    public String getPrimerApellidoBuzonEntradaHistorico() {
        return primerApellidoBuzonEntradaHistorico;
    }

    public void setPrimerApellidoBuzonEntradaHistorico(String primerApellidoBuzonEntradaHistorico) {
        this.primerApellidoBuzonEntradaHistorico = primerApellidoBuzonEntradaHistorico;
    }

    public String getSegundoApellidoBuzonEntradaHistorico() {
        return segundoApellidoBuzonEntradaHistorico;
    }

    public void setSegundoApellidoBuzonEntradaHistorico(String segundoApellidoBuzonEntradaHistorico) {
        this.segundoApellidoBuzonEntradaHistorico = segundoApellidoBuzonEntradaHistorico;
    }

    public String getTipoRegistroAsuntoSeleccionadoHistorico() {
        return tipoRegistroAsuntoSeleccionadoHistorico;
    }

    public void setTipoRegistroAsuntoSeleccionadoHistorico(String tipoRegistroAsuntoSeleccionadoHistorico) {
        this.tipoRegistroAsuntoSeleccionadoHistorico = tipoRegistroAsuntoSeleccionadoHistorico;
    }

    public String getUnidadRegistroAsuntoSeleccionadoHistorico() {
        return unidadRegistroAsuntoSeleccionadoHistorico;
    }

    public void setUnidadRegistroAsuntoSeleccionadoHistorico(String unidadRegistroAsuntoSeleccionadoHistorico) {
        this.unidadRegistroAsuntoSeleccionadoHistorico = unidadRegistroAsuntoSeleccionadoHistorico;
    }

    public Vector getListaAsuntosCodificadosBuzonHistorico() {
        return listaAsuntosCodificadosBuzonHistorico;
    }

    public void setListaAsuntosCodificadosBuzonHistorico(Vector listaAsuntosCodificadosBuzonHistorico) {
        this.listaAsuntosCodificadosBuzonHistorico = listaAsuntosCodificadosBuzonHistorico;
    }

    public Vector getListaUnidadesDestinoBuzonHistorico() {
        return listaUnidadesDestinoBuzonHistorico;
    }

    public void setListaUnidadesDestinoBuzonHistorico(Vector listaUnidadesDestinoBuzonHistorico) {
        this.listaUnidadesDestinoBuzonHistorico = listaUnidadesDestinoBuzonHistorico;
    }

    public Boolean getOpcionPermanencia() {
        return opcionPermanencia;
    }//getOpcionPermanencia

    public void setOpcionPermanencia(Boolean opcionPermanencia) {
        this.opcionPermanencia = opcionPermanencia;
    }//setOpcionPermanencia

    public Boolean getValorOpcionPermanencia() {
        return valorOpcionPermanencia;
    }//getValorOpcionPermanencia

    public void setValorOpcionPermanencia(Boolean valorOpcionPermanencia) {
        this.valorOpcionPermanencia = valorOpcionPermanencia;
    }//setValorOpcionPermanencia

    /**
     * @return the expedientes_eliminar
     */
    public String getexpedientes_eliminar() {
        return expedientes_eliminar;
    }

    /**
     * @param expedientes_eliminar the expedientes_eliminar to set
     */
    public void setexpedientes_eliminar(String expedientes_eliminar) {
        this.expedientes_eliminar = expedientes_eliminar;
    }

    public String getCodUnidadOrganicaAnotacion() {
        return codUnidadOrganicaAnotacion;
    }

    public void setCodUnidadOrganicaAnotacion(String codUnidadOrganicaAnotacion) {
        this.codUnidadOrganicaAnotacion = codUnidadOrganicaAnotacion;
    }

    public String getDescUnidadOrganicaAnotacion() {
        return descUnidadOrganicaAnotacion;
    }

    public void setDescUnidadOrganicaAnotacion(String descUnidadOrganicaAnotacion) {
        this.descUnidadOrganicaAnotacion = descUnidadOrganicaAnotacion;
    }

    public String getCodUnidadOrganicaAnotacionHistorico() {
        return codUnidadOrganicaAnotacionHistorico;
    }

    public void setCodUnidadOrganicaAnotacionHistorico(String codUnidadOrganicaAnotacionHistorico) {
        this.codUnidadOrganicaAnotacionHistorico = codUnidadOrganicaAnotacionHistorico;
    }

    public String getDescUnidadOrganicaAnotacionHistorico() {
        return descUnidadOrganicaAnotacionHistorico;
    }

    public void setDescUnidadOrganicaAnotacionHistorico(String descUnidadOrganicaAnotacionHistorico) {
        this.descUnidadOrganicaAnotacionHistorico = descUnidadOrganicaAnotacionHistorico;
    }

    public String getCodUnidadInternoAnotacionHistorico() {
        return codUnidadInternoAnotacionHistorico;
    }

    public void setCodUnidadInternoAnotacionHistorico(String codUnidadInternoAnotacionHistorico) {
        this.codUnidadInternoAnotacionHistorico = codUnidadInternoAnotacionHistorico;
    }

    public String getCodClasificacionAsuntos() {
        return codClasificacionAsuntos;
    }

    public void setCodClasificacionAsuntos(String codClasificacionAsuntos) {
        this.codClasificacionAsuntos = codClasificacionAsuntos;
    }

    public String getDescClasificacionAsuntos() {
        return descClasificacionAsuntos;
    }

    public void setDescClasificacionAsuntos(String descClasificacionAsuntos) {
        this.descClasificacionAsuntos = descClasificacionAsuntos;
    }

    public List<MantClasifAsuntosValueObject> getListaClasificacionAsuntos() {
        return listaClasificacionAsuntos;
    }

    public void setListaClasificacionAsuntos(List<MantClasifAsuntosValueObject> listaClasificacionAsuntos) {
        this.listaClasificacionAsuntos = listaClasificacionAsuntos;
    }

    public String getUnidadRegistroClasifAsuntoSeleccionado() {
        return unidadRegistroClasifAsuntoSeleccionado;
    }

    public void setUnidadRegistroClasifAsuntoSeleccionado(String unidadRegistroClasifAsuntoSeleccionado) {
        this.unidadRegistroClasifAsuntoSeleccionado = unidadRegistroClasifAsuntoSeleccionado;
    }

    public List<TecnicoReferenciaDTO> getTecnicosReferencia() {
        return tecnicosReferencia;
    }

    public void setTecnicosReferencia(List<TecnicoReferenciaDTO> tecnicosReferencia) {
        this.tecnicosReferencia = tecnicosReferencia;
    }

    public String getCodTecnicoReferencia() {
        return codTecnicoReferencia;
    }

    public void setCodTecnicoReferencia(String codTecnicoReferencia) {
        this.codTecnicoReferencia = codTecnicoReferencia;
    }

    public String getDescTecnicoReferencia() {
        return descTecnicoReferencia;
    }

    public void setDescTecnicoReferencia(String descTecnicoReferencia) {
        this.descTecnicoReferencia = descTecnicoReferencia;
    }
    
    /**
     * @return the descRegistroTelematico
     */
    public String getDescRegistroTelematico() {
        return descRegistroTelematico;
    }

    /**
     * @param descRegistroTelematico the descRegistroTelematico to set
     */
    public void setDescRegistroTelematico(String descRegistroTelematico) {
        this.descRegistroTelematico = descRegistroTelematico;
    }

    public String getCodRegPendCatalogacion() {
        return codRegPendCatalogacion;
    }

    public void setCodRegPendCatalogacion(String codRegPendCatalogacion) {
        this.codRegPendCatalogacion = codRegPendCatalogacion;
    }

    /**
     * @return the descRegPendCatalogacion
     */
    public String getDescRegPendCatalogacion() {
        return descRegPendCatalogacion;
    }

    /**
     * @param descRegPendCatalogacion the descRegPendCatalogacion to set
     */
    public void setDescRegPendCatalogacion(String descRegPendCatalogacion) {
        this.descRegPendCatalogacion = descRegPendCatalogacion;
    }

}//class
