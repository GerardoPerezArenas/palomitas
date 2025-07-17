package es.altia.agora.business.sge;

import es.altia.agora.business.administracion.mantenimiento.ValoresCriterioBusquedaExpPendientesVO;
import es.altia.flexia.expedientes.relacionados.plugin.vo.ExpedienteRelacionadoVO;
import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

public class TramitacionValueObject implements Serializable, ValueObject {

    /** Construye un nuevo Tramitacion por defecto. */
    public TramitacionValueObject() {
        super();
    }

    public String getBloqUsuNom() {
        return bloqUsuNom;
    }

    public void setBloqUsuNom(String bloqUsuNom) {
        this.bloqUsuNom = bloqUsuNom;
    }

    public String getBloqUsuCod() {
        return bloqUsuCod;
    }

    public void setBloqUsuCod(String bloqUsuCod) {
        this.bloqUsuCod = bloqUsuCod;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getLocalizacion() {
      return localizacion;
    }
    public void setLocalizacion(String localizacion) {
      this.localizacion = localizacion;
    }
    public String getAsunto() {
      return asunto;
    }
    public void setAsunto(String asunto) {
      this.asunto = asunto;
    }
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCodDepartamento() {
      return codDepartamento;
    }
    public void setCodDepartamento(String codDepartamento) {
      this.codDepartamento = codDepartamento;
    }
    public String getCodUnidadRegistro() {
      return codUnidadRegistro;
    }
    public void setCodUnidadRegistro(String codUnidadRegistro) {
      this.codUnidadRegistro = codUnidadRegistro;
    }
    public String getEjerNum() {
      return ejerNum;
    }
    public void setEjerNum(String ejerNum) {
      this.ejerNum = ejerNum;
    }
    public String getFechaAnotacion() {
      return fechaAnotacion;
    }
    public void setFechaAnotacion(String fechaAnotacion) {
      this.fechaAnotacion = fechaAnotacion;
    }
    public String getFechaDocumento() {
      return fechaDoc;
    }
    public void setFechaDocumento(String fechaDoc) {
      this.fechaDoc = fechaDoc;
    }
    
    
    
    
    public String getRemitente() {
      return remitente;
    }
    public void setRemitente(String remitente) {
      this.remitente = remitente;
    }
    public String getNumTerceros() {
      return numTerceros;
    }
    public void setNumTerceros(String numTerceros) {
      this.numTerceros = numTerceros;
    }
    public String getTipoRegistro() {
      return tipoRegistro;
    }
    public void setTipoRegistro(String tipoRegistro) {
      this.tipoRegistro = tipoRegistro;
    }
    public String getNumLineasPaginaListado() {
      return numLineasPaginaListado;
    }
    public void setNumLineasPaginaListado(String numLineasPaginaListado) {
      this.numLineasPaginaListado = numLineasPaginaListado;
    }
    public String getPaginaListado() {
      return paginaListado;
    }
    public void setPaginaListado(String paginaListado) {
      this.paginaListado = paginaListado;
    }
    public String getNumLineasPaginaListadoE() {
      return numLineasPaginaListadoE;
    }
    public void setNumLineasPaginaListadoE(String numLineasPaginaListadoE) {
      this.numLineasPaginaListadoE = numLineasPaginaListadoE;
    }
    public String getPaginaListadoE() {
      return paginaListadoE;
    }
    public void setPaginaListadoE(String paginaListadoE) {
      this.paginaListadoE = paginaListadoE;
    }

    public String getNumeroExpediente() {
      return numeroExpediente;
    }
    public void setNumeroExpediente(String numeroExpediente) {
      this.numeroExpediente = numeroExpediente;
    }
    public String getNumeroExpedienteAntiguo() {
      return numeroExpedienteAntiguo;
    }
    public void setNumeroExpedienteAntiguo(String numeroExpedienteAntiguo) {
      this.numeroExpedienteAntiguo = numeroExpedienteAntiguo;
    }

  public Vector getListaUnidadesTramitadoras() {
    return listaUnidadesTramitadoras;
  }
  public void setListaUnidadesTramitadoras(Vector listaUnidadesTramitadoras) {
    this.listaUnidadesTramitadoras = listaUnidadesTramitadoras;
  }

    public String getListaUnidadesTramitadorasPendientes() {
        return listaUnidadesTramitadorasPendientes;
    }

    public void setListaUnidadesTramitadorasPendientes(String listaUnidadesTramitadorasPendientes) {
        this.listaUnidadesTramitadorasPendientes = listaUnidadesTramitadorasPendientes;
    }

  

  public String getCodMunicipio() {
    return codMunicipio;
  }
  public void setCodMunicipio(String codMunicipio) {
    this.codMunicipio = codMunicipio;
  }
  public String getCodProcedimiento() {
    return codProcedimiento;
  }
  public void setCodProcedimiento(String codProcedimiento) {
    this.codProcedimiento = codProcedimiento;
  }
  public String getEjercicio() {
    return ejercicio;
  }
  public void setEjercicio(String ejercicio) {
    this.ejercicio = ejercicio;
  }
  public String getNumero() {
    return numero;
  }
  public void setNumero(String numero) {
    this.numero = numero;
  }

  public Vector getListaProcedimientos() {
    return listaProcedimientos;
  }
  public void setListaProcedimientos(Vector listaProcedimientos) {
    this.listaProcedimientos = listaProcedimientos;
  }

  public String getFechaInicioExpediente() {
    return fechaInicioExpediente;
  }
  public void setFechaInicioExpediente(String fechaInicioExpediente) {
    this.fechaInicioExpediente = fechaInicioExpediente;
  }

    public String getFechaFinExpediente() {
        return fechaFinExpediente;
    }

    public void setFechaFinExpediente(String fechaFinExpediente) {
        this.fechaFinExpediente = fechaFinExpediente;
    }

  public String getTitular() {
    return titular;
  }
  public void setTitular(String titular) {
    this.titular = titular;
  }
  public String getDescProcedimiento() {
    return descProcedimiento;
  }
  public void setDescProcedimiento(String descProcedimiento) {
    this.descProcedimiento = descProcedimiento;
  }

  public String getRespOpcion() {
    return respOpcion;
  }
  public void setRespOpcion(String respOpcion) {
    this.respOpcion = respOpcion;
  }

  public String getEstado() {
    return estado;
  }
  public void setEstado(String estado) {
    this.estado = estado;
  }
  
    public String getFechaLimiteP() {
    return fechaLimteP;
  }
  public void setFechaLimiteP(String fechaLimteP) {
    this.fechaLimteP = fechaLimteP;
  }
  
    public String getPlazoCercaFin() {
    return plazoCercaFin;
  }
  public void setPlazoCercaFin(String plazoCercaFin) {
    this.plazoCercaFin = plazoCercaFin;
  }   

  public String getAlarmaVencida() {
    return alarmaVencida;
  }

  public void setAlarmaVencida(String alarmaVencida) {
    this.alarmaVencida = alarmaVencida;
  }

  public String getFueraDePlazo() {
    return fueraDePlazo;
  }
  public void setFueraDePlazo(String fueraDePlazo) {
    this.fueraDePlazo = fueraDePlazo;
  }
  
  public String getPendiente() {
    return pendiente;
  }
  public void setPendiente(String pendiente) {
    this.pendiente = pendiente;
  }

  public String getCodDomicilio() {
    return codDomicilio;
  }
  public void setCodDomicilio(String codDomicilio) {
    this.codDomicilio = codDomicilio;
  }
  public String getCodTercero() {
    return codTercero;
  }
  public void setCodTercero(String codTercero) {
    this.codTercero = codTercero;
  }
  public String getVersion() {
    return version;
  }
  public void setVersion(String version) {
    this.version = version;
  }
  
  public String getEjercicioRegistro() {
    return ejercicioRegistro;
  }
  public void setEjercicioRegistro(String ejercicioRegistro) {
    this.ejercicioRegistro = ejercicioRegistro;
  }
  
  public String getAsuntoExp() {
    return asuntoExp;
  }
  public void setAsuntoExp(String asuntoExp) {
    this.asuntoExp = asuntoExp;
  }

    public String getExpImportante() {
        return expImportante;
    }

    public void setExpImportante(String expImportante) {
        this.expImportante = expImportante;
    }

    public String getCodRangoTemporal() {
        return codRangoTemporal;
    }

    public void setCodRangoTemporal(String codRangoTemporal) {
        this.codRangoTemporal = codRangoTemporal;
    }

  public void setNumeroRelacion(String numeroRelacion) {
    this.numeroRelacion = numeroRelacion;
  }
  public String getNumeroRelacion() {
    return numeroRelacion;
  }

    public String getListaInteresados() {
        return listaInteresados;
    }

    public void setListaInteresados(String listaInteresados) {
        this.listaInteresados = listaInteresados;
    }

    public String getListaTramitesPendientes() {
        return listaTramitesPendientes;
    }

    public void setListaTramitesPendientes(String listaTramitesPendientes) {
        this.listaTramitesPendientes = listaTramitesPendientes;
    }

    public String getNombreUnidadInicio() {
        return nombreUnidadInicio;
    }

    public void setNombreUnidadInicio(String nombreUnidadInicio) {
        this.nombreUnidadInicio = nombreUnidadInicio;
    }

    public String getNombreUsuarioInicio() {
        return nombreUsuarioInicio;
    }

    public void setNombreUsuarioInicio(String nombreUsuarioInicio) {
        this.nombreUsuarioInicio = nombreUsuarioInicio;
    }

    /**
     *Atributo para indicar si su origen es SGE, SicalWin,....
     */
    public String getOrigen(){ return origen;}
    public void setOrigen(String origen){ this.origen = origen;}
public void setUnidadInicio(String unidadIni) {
    this.unidadIni = unidadIni;
  }
  public String getUnidadInicio() {
    return unidadIni;
  }
     public void setUsuarioIni(String usuario) {
    this.usuario = usuario;
  }
  public String getUsuarioIni() {
    return usuario;
  }

    public String getUnidadTramitadora(){ return uor;}
    public void setUnidadTramitadora(String uor){ this.uor = uor;}
    
     public String getProcedimiento(){ return proc;}
    public void setProcedimiento(String proc){ this.proc = proc;}
    
      public String getNumExpediente(){ return numeroexp;}
    public void setNumExpediente(String numeroexp){ this.numeroexp = numeroexp;}
    
    public String getUsuarioAlta(){ return usuarioAlta;}
    public void setUsuarioAlta(String usuarioAlta){ this.usuarioAlta = usuarioAlta;}
//parametros para realizar la ordenacion
    
  public void setColumna(String col) {
    this.col = col;
  }
  public String getColumna() {
    return col;
  }
  
 public void setTipoOrdenacion(String tipoOrden) {
    this.tipoOrden = tipoOrden;
  }
  public String getTipoOrdenacion() {
    return tipoOrden;
  }

    public String getCodigoRolDefectoInteresados() {
        return codigoRolDefectoInteresados;
    }

    public void setCodigoRolDefectoInteresados(String codigoRolDefectoInteresados) {
        this.codigoRolDefectoInteresados = codigoRolDefectoInteresados;
    }
 
    public String getNumeroSimple() {
        return numeroSimple;
    }

    public void setNumeroSimple(String numeroSimple) {
        this.numeroSimple = numeroSimple;
    }
  
    


    /**
     * Valida el estado de esta RegistroSaida
     * Puede ser invocado desde la capa cliente o desde la capa de negocio
     * @exception ValidationException si el estado no es válido
     */
    public void validate(String idioma) throws ValidationException {
        String sufijo = "";
        if ("euskera".equals(idioma)) sufijo="_eu";
    boolean correcto = true;
        Messages errors = new Messages();

        if (!errors.empty())
            throw new ValidationException(errors);
        isValid = true;
    }

    /** Devuelve un booleano que representa si el estado de este RegistroSaida es válido. */
    public boolean IsValid() { return isValid; }

    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;

    private String bloqUsuCod;
    private String bloqUsuNom;
    private int idUsuario;
    private String nombreUsuarioInicio;

    private String numeroRelacion;
    private String unidadIni;
    private String usuario;

    private String codDepartamento;
    private String codUnidadRegistro;
    private String nombreUnidadInicio;
    private String tipoRegistro;
    private String ejerNum;
    private String fechaAnotacion;
    private String fechaDoc;
    private String remitente;
    private String numTerceros;
    private String listaInteresados;
    private String asunto;
    private String codigoAsuntoCodificado;	
    private String descripcionAsuntoCodificado;
    private String observaciones = null;
    private String estado;
    private String codTercero;
    private String codDomicilio;
    private String version;
    private String ejercicioRegistro;
    private String localizacion;

    private String codMunicipio;
    private String codProcedimiento;
    private String descProcedimiento;
    private String ejercicio;
    private String numero;
    private String fechaInicioExpediente;
    private String fechaFinExpediente;
    private String titular;
    private String fueraDePlazo;
    private String fechaLimteP;
    private String plazoCercaFin;
    private String pendiente;
    private String asuntoExp;
    private String expImportante;   // atributo que indica si un expediente esta marcado como importante
    private String codRangoTemporal;

    private String numeroExpediente;
    private String numeroExpedienteAntiguo;
    private String numeroSimple;

    private String paginaListado;
    private String numLineasPaginaListado;
    private String paginaListadoE;
    private String numLineasPaginaListadoE;

    private Vector listaUnidadesTramitadoras;
    private String listaUnidadesTramitadorasPendientes;

    private Vector listaProcedimientos;

    private Vector listaTramitesAbiertos;
    private String listaTramitesPendientes;
    private Vector listaFechasInicioTramitesAbiertos;
    private Vector listaFechasFinTramitesAbiertos;

    private String col;
    private String tipoOrden;
    private String expedientesEliminar;
    
    // Lista de trámites cerrados
    private Vector listaTramitesFinalizados;

    //
    private boolean cercaPlazoExpediente;
    private boolean fueraPlazoExpediente;
    
    private boolean fechaVencidaExpediente;

    private Calendar fechaInicioAsCalendar;

    private String codigoRolDefectoInteresados;
    // ATRIBUTO QUE INDICA CUAL ES EL ORIGEN DEL EXPEDIENTE RELACIONADO
    private String origenExpedienteRelacionado;
    private String alarmaVencida;
    private String TipoInicio;
    private String tipoTransporte;
    
    private boolean expHistorico=false;

    private String usuarioTramitador;
    
    private String regPendCatalogacion = "";

    public String getUsuarioTramitador() {
        return usuarioTramitador;
    }

    public void setUsuarioTramitador(String usuarioTramitador) {
        this.usuarioTramitador = usuarioTramitador;
    }
    
    public boolean isExpHistorico() {
        return expHistorico;
    }

    public void setExpHistorico(boolean expHistorico) {
        this.expHistorico = expHistorico;
    }
           
    public Vector getListaTramitesFinalizados(){
        return this.listaTramitesFinalizados;
    }

    public Vector setListaTramitesFinalizados(Vector lista){
       return this.listaTramitesFinalizados = lista;
    }
    
    public Vector getListaTramitesAbiertos() {
        return listaTramitesAbiertos;
    }

    public void setListaTramitesAbiertos(Vector listaTramitesAbiertos) {
        this.listaTramitesAbiertos = listaTramitesAbiertos;
    }

    public Vector getListaFechasInicioTramitesAbiertos() {
        return listaFechasInicioTramitesAbiertos;
    }

    public void setListaFechasInicioTramitesAbiertos(Vector listaFechasInicioTramitesAbiertos) {
        this.listaFechasInicioTramitesAbiertos = listaFechasInicioTramitesAbiertos;
    }
    
    public Vector getListaFechasFinTramitesAbiertos() {
        return listaFechasFinTramitesAbiertos;
    }

    public void setListaFechasFinTramitesAbiertos(Vector listaFechasFinTramitesAbiertos) {
        this.listaFechasFinTramitesAbiertos = listaFechasFinTramitesAbiertos;
    }

    private String respOpcion;
    private String uor;
    private String proc;
    private String numeroexp;
    private String usuarioAlta;
    private String origen;// Atributo para indicar si su origen es SGE, SicalWin,....
    
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String docTercero;
    private int codigoUorSge;
    private String descripcionUorSge;
    private String codigoProcedimientoSge;

    private String ejercicioExpedienteRelacionadoExterno;
    private String municipioExpedienteRelacionadoExterno;
    private String codProcedimientoExpedienteRelacionadoExterno;
    private boolean utilizarDatosExpedienteRelacionadoExterno;

    private Hashtable<String,String> valoresCamposVistaPendientes = null;
    private ValoresCriterioBusquedaExpPendientesVO criterio = null;
    
    private boolean exportarConsultaSuperadosLimiteMaximo;
    private int numMaximoExpedientesRecuperados;
    private String tecnicoReferencia;
    
    private String registroTelematico="";
    
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
     * @return the apellido1
     */
    public String getApellido1() {
        return apellido1;
    }

    /**
     * @param apellido1 the apellido1 to set
     */
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    /**
     * @return the apellido2
     */
    public String getApellido2() {
        return apellido2;
    }

    /**
     * @param apellido2 the apellido2 to set
     */
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    /**
     * @return the docTercero 
     */
    public String getDocTercero() {
        return docTercero;
    }
    /**
     * @param docTercero the docTercero to set
     */
    public void setDocTercero(String docTercero) {
        this.docTercero = docTercero;
    }

    
    
    /**
     * @return the codigoUorSge
     */
    public int getCodigoUorSge() {
        return codigoUorSge;
    }

    /**
     * @param codigoUorSge the codigoUorSge to set
     */
    public void setCodigoUorSge(int codigoUorSge) {
        this.codigoUorSge = codigoUorSge;
    }

    /**
     * @return the descripcionUorSge
     */
    public String getDescripcionUorSge() {
        return descripcionUorSge;
    }

    /**
     * @param descripcionUorSge the descripcionUorSge to set
     */
    public void setDescripcionUorSge(String descripcionUorSge) {
        this.descripcionUorSge = descripcionUorSge;
    }

    /**
     * @return the codigoProcedimientoSge
     */
    public String getCodigoProcedimientoSge() {
        return codigoProcedimientoSge;
    }

    /**
     * @param codigoProcedimientoSge the codigoProcedimientoSge to set
     */
    public void setCodigoProcedimientoSge(String codigoProcedimientoSge) {
        this.codigoProcedimientoSge = codigoProcedimientoSge;
    }

    /**
     * @return the cercaPlazoExpediente
     */
    public boolean isCercaPlazoExpediente() {
        return cercaPlazoExpediente;
    }

    /**
     * @param cercaPlazoExpediente the cercaPlazoExpediente to set
     */
    public void setCercaPlazoExpediente(boolean cercaPlazoExpediente) {
        this.cercaPlazoExpediente = cercaPlazoExpediente;
    }

    /**
     * @return the fueraPlazoExpediente
     */
    public boolean isFueraPlazoExpediente() {
        return fueraPlazoExpediente;
    }

    /**
     * @param fueraPlazoExpediente the fueraPlazoExpediente to set
     */
    public void setFueraPlazoExpediente(boolean fueraPlazoExpediente) {
        this.fueraPlazoExpediente = fueraPlazoExpediente;
    }

    public boolean isFechaVencidaExpediente() {
        return fechaVencidaExpediente;
    }

    public void setFechaVencidaExpediente(boolean fechaVencidaExpediente) {
        this.fechaVencidaExpediente = fechaVencidaExpediente;
    }
    
    

    /**
     * @return the fechaInicioAsCalendar
     */
    public Calendar getFechaInicioAsCalendar() {
        return fechaInicioAsCalendar;
    }

    /**
     * @param fechaInicioAsCalendar the fechaInicioAsCalendar to set
     */
    public void setFechaInicioAsCalendar(Calendar fechaInicioAsCalendar) {
        this.fechaInicioAsCalendar = fechaInicioAsCalendar;
    }

    /**
     * @return the origenExpedienteRelacionado
     */
    public String getOrigenExpedienteRelacionado() {
        return origenExpedienteRelacionado;
    }

    /**
     * @param origenExpedienteRelacionado the origenExpedienteRelacionado to set
     */
    public void setOrigenExpedienteRelacionado(String origenExpedienteRelacionado) {
        this.origenExpedienteRelacionado = origenExpedienteRelacionado;
    }

    /**
     * @return the ejercicioExpedienteRelacionadoExterno
     */
    public String getEjercicioExpedienteRelacionadoExterno() {
        return ejercicioExpedienteRelacionadoExterno;
    }

    /**
     * @param ejercicioExpedienteRelacionadoExterno the ejercicioExpedienteRelacionadoExterno to set
     */
    public void setEjercicioExpedienteRelacionadoExterno(String ejercicioExpedienteRelacionadoExterno) {
        this.ejercicioExpedienteRelacionadoExterno = ejercicioExpedienteRelacionadoExterno;
    }

    /**
     * @return the municipioExpedienteRelacionadoExterno
     */
    public String getMunicipioExpedienteRelacionadoExterno() {
        return municipioExpedienteRelacionadoExterno;
    }

    /**
     * @param municipioExpedienteRelacionadoExterno the municipioExpedienteRelacionadoExterno to set
     */
    public void setMunicipioExpedienteRelacionadoExterno(String municipioExpedienteRelacionadoExterno) {
        this.municipioExpedienteRelacionadoExterno = municipioExpedienteRelacionadoExterno;
    }

    /**
     * @return the codProcedimientoExpedienteRelacionadoExterno
     */
    public String getCodProcedimientoExpedienteRelacionadoExterno() {
        return codProcedimientoExpedienteRelacionadoExterno;
    }

    /**
     * @param codProcedimientoExpedienteRelacionadoExterno the codProcedimientoExpedienteRelacionadoExterno to set
     */
    public void setCodProcedimientoExpedienteRelacionadoExterno(String codProcedimientoExpedienteRelacionadoExterno) {
        this.codProcedimientoExpedienteRelacionadoExterno = codProcedimientoExpedienteRelacionadoExterno;
    }

    /**
     * @return the utilizarDatosExpedienteRelacinoadoExterno
     */
    public boolean isUtilizarDatosExpedienteRelacionadoExterno() {
        return utilizarDatosExpedienteRelacionadoExterno;
    }

    /**
     * @param utilizarDatosExpedienteRelacinoadoExterno the utilizarDatosExpedienteRelacinoadoExterno to set
     */
    public void setUtilizarDatosExpedienteRelacionadoExterno(boolean utilizarDatosExpedienteRelacionadoExterno) {
        this.utilizarDatosExpedienteRelacionadoExterno = utilizarDatosExpedienteRelacionadoExterno;
    }

    /**
     * @return the valoresCamposVistaPendientes
     */
    public Hashtable<String, String> getValoresCamposVistaPendientes() {
        return valoresCamposVistaPendientes;
    }

    /**
     * @param valoresCamposVistaPendientes the valoresCamposVistaPendientes to set
     */
    public void setValoresCamposVistaPendientes(Hashtable<String, String> valoresCamposVistaPendientes) {
        this.valoresCamposVistaPendientes = valoresCamposVistaPendientes;
    }

    /**
     * @return the criterio
     */
    public ValoresCriterioBusquedaExpPendientesVO getCriterioBusquedaExpPendientes() {
        return criterio;
    }

    /**
     * @param criterio the criterio to set
     */
    public void setCriterioBusquedaExpPendientes(ValoresCriterioBusquedaExpPendientesVO criterio) {
        this.criterio = criterio;
    }

    /**
     * @return the TipoInicio
     */
    public String getTipoInicio() {
        return TipoInicio;
    }

    /**
     * @param TipoInicio the TipoInicio to set
     */
    public void setTipoInicio(String TipoInicio) {
        this.TipoInicio = TipoInicio;
    }

    private boolean soloContarExpedientesBuzonEntrada;
    
    
    public boolean isSoloContarExpedientesBuzonEntrada(){
        return this.soloContarExpedientesBuzonEntrada;
    }
    
    public void setSoloContarExpedientesBuzonEntrada(boolean flag){
        this.soloContarExpedientesBuzonEntrada = flag;
    }
    
    
    private Boolean dejarAnotacionBuzonEntrada = false;
    public Boolean getDejarAnotacionBuzonEntrada() {
        return dejarAnotacionBuzonEntrada;
    }//getDejarAnotacionBuzonEntrada
    public void setDejarAnotacionBuzonEntrada(Boolean dejarAnotacionBuzonEntrada) {
        this.dejarAnotacionBuzonEntrada = dejarAnotacionBuzonEntrada;
    }//setDejarAnotacionBuzonEntrada
    
    public ExpedienteRelacionadoVO expedienteRelacionado;
    public ExpedienteRelacionadoVO getExpedienteRelacionado() {
        return expedienteRelacionado;
    }//getExpedientesRelacionados
    public void setExpedientesRelacionados(ExpedienteRelacionadoVO expedienteRelacionado) {
        this.expedienteRelacionado = expedienteRelacionado;
    }//setExpedientesRelacionados
    
    private Boolean multiplesExpedientesAnotaciones = false;
    public Boolean getMultiplesExpedientesAnotaciones() {
        return multiplesExpedientesAnotaciones;
    }//getMultiplesAnotaciones
    public void setMultiplesExpedientesAnotaciones(Boolean multiplesAnotaciones) {
        this.multiplesExpedientesAnotaciones = multiplesAnotaciones;
    }//setMultiplesAnotaciones

    /**
     * @return the exportarConsultaSuperadosLimiteMaximo
     */
    public boolean isExportarConsultaSuperadosLimiteMaximo() {
        return exportarConsultaSuperadosLimiteMaximo;
    }

    /**
     * @param exportarConsultaSuperadosLimiteMaximo the exportarConsultaSuperadosLimiteMaximo to set
     */
    public void setExportarConsultaSuperadosLimiteMaximo(boolean exportarConsultaSuperadosLimiteMaximo) {
        this.exportarConsultaSuperadosLimiteMaximo = exportarConsultaSuperadosLimiteMaximo;
    }

    /**
     * @return the numMaximoExpedientesRecuperados
     */
    public int getNumMaximoExpedientesRecuperados() {
        return numMaximoExpedientesRecuperados;
    }

    /**
     * @param numMaximoExpedientesRecuperados the numMaximoExpedientesRecuperados to set
     */
    public void setNumMaximoExpedientesRecuperados(int numMaximoExpedientesRecuperados) {
        this.numMaximoExpedientesRecuperados = numMaximoExpedientesRecuperados;
    }

    /**
     * @return the expedientesEliminar
     */
    public String getexpedientesEliminar() {
        return expedientesEliminar;
    }

    /**
     * @param expedientesEliminar the expedientesEliminar to set
     */
    public void setexpedientesEliminar(String expedientesEliminar) {
        this.expedientesEliminar = expedientesEliminar;
    }

    public String getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(String tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public String getRegistroTelematico() {
        return registroTelematico;
    }

    public void setRegistroTelematico(String registroTelematico) {
        this.registroTelematico = registroTelematico;
    }

    public String getCodigoAsuntoCodificado() {
        return codigoAsuntoCodificado;
    }

    public void setCodigoAsuntoCodificado(String codigoAsuntoCodificado) {
        this.codigoAsuntoCodificado = codigoAsuntoCodificado;
    }

    public String getDescripcionAsuntoCodificado() {
        return descripcionAsuntoCodificado;
    }

    public void setDescripcionAsuntoCodificado(String descripcionAsuntoCodificado) {
        this.descripcionAsuntoCodificado = descripcionAsuntoCodificado;
    }

    public String getTecnicoReferencia() {
        return tecnicoReferencia;
    }

    public void setTecnicoReferencia(String tecnicoReferencia) {
        this.tecnicoReferencia = tecnicoReferencia;
    }
    
    public String getRegPendCatalogacion() {
        return regPendCatalogacion;
    }

    public void setRegPendCatalogacion(String regPendCatalogacion) {
        this.regPendCatalogacion = regPendCatalogacion;
    }

    
    
    
}//class