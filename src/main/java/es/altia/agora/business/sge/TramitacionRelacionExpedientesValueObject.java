package es.altia.agora.business.sge;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import es.altia.agora.business.util.GeneralValueObject;

import java.io.Serializable;
import java.util.Vector;

public class TramitacionRelacionExpedientesValueObject implements Serializable, ValueObject {

    /** Construye un nuevo TramitacionExpedientes por defecto. */
    public TramitacionRelacionExpedientesValueObject() {
        super();
    }

    public String getClasificacionTramite() {
  return clasificacionTramite;
}

public String getOpcionGrabar() {
    return opcionGrabar;
}

public void setOpcionGrabar(String opcionGrabar) {
    this.opcionGrabar = opcionGrabar;
}

public String getNumeroRelacion() {
  return numeroExpediente;
}
public void setNumeroRelacion(String numeroRelacion) {
  this.numeroExpediente = numeroExpediente;
}

public String getNumeroRelacionMostrar() {
  return numeroExpedienteMostrar;
}
public void setNumeroRelacionMostrar(String numeroExpedienteMostrar) {
  this.numeroExpedienteMostrar = numeroExpedienteMostrar;
}

public void setClasificacionTramite(String clasificacionTramite) {
  this.clasificacionTramite = clasificacionTramite;
}
public void setFechaFin(String fechaFin) {
  this.fechaFin = fechaFin;
}
public String getFechaFin() {
  return fechaFin;
}
public String getFechaFinPlazo() {
  return fechaFinPlazo;
}
public void setFechaFinPlazo(String fechaFinPlazo) {
  this.fechaFinPlazo = fechaFinPlazo;
}
public String getFechaInicio() {
  return fechaInicio;
}
public void setFechaInicio(String fechaInicio) {
  this.fechaInicio = fechaInicio;
}
public String getFechaInicioPlazo() {
  return fechaInicioPlazo;
}
public void setFechaInicioPlazo(String fechaInicioPlazo) {
  this.fechaInicioPlazo = fechaInicioPlazo;
}
public String getFechaLimite() {
  return fechaLimite;
}
public void setFechaLimite(String fechaLimite) {
  this.fechaLimite = fechaLimite;
}
public String getNumeroExpediente() {
  return numeroExpediente;
}
public void setNumeroExpediente(String numeroExpediente) {
  this.numeroExpediente = numeroExpediente;
}
public String getPlazo() {
  return plazo;
}
public void setPlazo(String plazo) {
  this.plazo = plazo;
}
public String getProcedimiento() {
  return procedimiento;
}
public void setProcedimiento(String procedimiento) {
  this.procedimiento = procedimiento;
}
public String getTipoPlazo() {
  return tipoPlazo;
}
public void setTipoPlazo(String tipoPlazo) {
  this.tipoPlazo = tipoPlazo;
}
public String getTitular() {
  return titular;
}
public void setTitular(String titular) {
  this.titular = titular;
}
public String getTramite() {
  return tramite;
}
public void setTramite(String tramite) {
  this.tramite = tramite;
}
public String getUnidadInicio() {
  return unidadInicio;
}
public void setUnidadInicio(String unidadInicio) {
  this.unidadInicio = unidadInicio;
}
public String getUnidadTramitadora() {
  return unidadTramitadora;
}
public void setUnidadTramitadora(String unidadTramitadora) {
  this.unidadTramitadora = unidadTramitadora;
}
public String getUnidadTramitadoraTramiteIniciado() {
  return unidadTramitadoraTramiteIniciado;
}
public void setUnidadTramitadoraTramiteIniciado(String unidadTramitadoraTramiteIniciado) {
  this.unidadTramitadoraTramiteIniciado = unidadTramitadoraTramiteIniciado;
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
public String getCodTramite() {
  return codTramite;
}
public void setCodTramite(String codTramite) {
  this.codTramite = codTramite;
}

public String getOcurrenciaTramite() {
  return this.ocurrenciaTramite;
}
public void setOcurrenciaTramite(String ocurrencia) {
  this.ocurrenciaTramite = ocurrencia;
}

public String getDescDocumento() {
  return descDocumento;
}
public void setDescDocumento(String descDocumento) {
  this.descDocumento = descDocumento;
}
public String getFechaCreacion() {
  return fechaCreacion;
}
public void setFechaCreacion(String fechaCreacion) {
  this.fechaCreacion = fechaCreacion;
}
public String getFechaModificacion() {
  return fechaModificacion;
}
public void setFechaModificacion(String fechaModificacion) {
  this.fechaModificacion = fechaModificacion;
}
public Vector getListaDocumentos() {
  return listaDocumentos;
}
public void setListaDocumentos(Vector listaDocumentos) {
  this.listaDocumentos = listaDocumentos;
}
public String getInstrucciones() {
  return instrucciones;
}
public void setInstrucciones(String instrucciones) {
  this.instrucciones = instrucciones;
  }
public String getUsuario() {
  return usuario;
}
public void setUsuario(String usuario) {
  this.usuario = usuario;
  }
  public String getCodDocumento() {
    return codDocumento;
  }
  public void setCodDocumento(String codDocumento) {
    this.codDocumento = codDocumento;
  }
  public Vector getListaCodDocumentosTramite() {
    return listaCodDocumentosTramite;
  }
  public void setListaCodDocumentosTramite(Vector listaCodDocumentosTramite) {
    this.listaCodDocumentosTramite = listaCodDocumentosTramite;
  }

  public String getCodigoDocumento() {
  return codigoDocumento;
}
public void setCodigoDocumento(String codigoDocumento) {
  this.codigoDocumento = codigoDocumento;
}
public String getDescripcionDocumento() {
  return descripcionDocumento;
}
public void setDescripcionDocumento(String descripcionDocumento) {
  this.descripcionDocumento = descripcionDocumento;
}
public Vector getListaDocumentosTramite() {
  return listaDocumentosTramite;
}
public void setListaDocumentosTramite(Vector listaDocumentosTramite) {
  this.listaDocumentosTramite = listaDocumentosTramite;
  }

public String getRespOpcion(){
    return this.respOpcion;
}
public void setRespOpcion(String respuesta) {
    respOpcion=respuesta;
}

public String getNotificacionRealizada(){
    return notificacionRealizada;
}
public void setNotificacionRealizada(String notificacionRealizada) {
    this.notificacionRealizada=notificacionRealizada;
}

public String getAccion() {
  return accion;
}
public void setAccion(String accion) {
  this.accion = accion;
}
public String getAccionAfirmativa() {
  return accionAfirmativa;
}
public void setAccionAfirmativa(String accionAfirmativa) {
  this.accionAfirmativa = accionAfirmativa;
}
public String getAccionNegativa() {
  return accionNegativa;
}
public void setAccionNegativa(String accionNegativa) {
  this.accionNegativa = accionNegativa;
}
public String getCodigoTramiteFlujoSalida() {
  return codigoTramiteFlujoSalida;
}
public void setCodigoTramiteFlujoSalida(String codigoTramiteFlujoSalida) {
  this.codigoTramiteFlujoSalida = codigoTramiteFlujoSalida;
}
public String getCodigoVisibleTramiteFlujoSalida() {
  return codigoVisibleTramiteFlujoSalida;
}
public void setCodigoVisibleTramiteFlujoSalida(String codigoVisibleTramiteFlujoSalida) {
  this.codigoVisibleTramiteFlujoSalida = codigoVisibleTramiteFlujoSalida;
}
public String getDescripcionTramiteFlujoSalida() {
  return descripcionTramiteFlujoSalida;
}
public void setDescripcionTramiteFlujoSalida(String descripcionTramiteFlujoSalida) {
  this.descripcionTramiteFlujoSalida = descripcionTramiteFlujoSalida;
}
public Vector getListaTramitesFavorables() {
  return listaTramitesFavorables;
}
public void setListaTramitesFavorables(Vector listaTramitesFavorables) {
  this.listaTramitesFavorables = listaTramitesFavorables;
}
public Vector getListaTramitesNoFavorables() {
  return listaTramitesNoFavorables;
}
public void setListaTramitesNoFavorables(Vector listaTramitesNoFavorables) {
  this.listaTramitesNoFavorables = listaTramitesNoFavorables;
}
public String getNumeroSecuencia() {
  return numeroSecuencia;
}
public void setNumeroSecuencia(String numeroSecuencia) {
  this.numeroSecuencia = numeroSecuencia;
}
public String getObligatorio() {
  return obligatorio;
}
public void setObligatorio(String obligatorio) {
  this.obligatorio = obligatorio;
}
public String getObligatorioDesf() {
  return obligatorioDesf;
}
public void setObligatorioDesf(String obligatorioDesf) {
  this.obligatorioDesf = obligatorioDesf;
}
public String getPregunta() {
  return pregunta;
}
public void setPregunta(String pregunta) {
  this.pregunta = pregunta;
  }

public Vector getListaTramitesIniciar() {
return listaTramitesIniciar;
}
public void setListaTramitesIniciar(Vector listaTramitesIniciar) {
this.listaTramitesIniciar = listaTramitesIniciar;
}

public Vector getListaTramitesExpediente() {
return listaTramitesExpediente;
}
public void setListaTramitesExpediente(Vector listaTramitesExpediente) {
this.listaTramitesExpediente = listaTramitesExpediente;
}

public String getCodUOR() {
  return codUOR;
}
public void setCodUOR(String codUOR) {
  this.codUOR = codUOR;
}
public String getCodUsuario() {
  return codUsuario;
}
public void setCodUsuario(String codUsuario) {
  this.codUsuario = codUsuario;
  }
public String getPermiso() {
  return permiso;
}
public void setPermiso(String permiso) {
  this.permiso = permiso;
}

public String getBloqueo() {
  return bloqueo;
}
public void setBloqueo(String bloqueo) {
  this.bloqueo = bloqueo;
}

public String getLocalizacion() {
  return localizacion;
}
public void setLocalizacion(String localizacion) {
  this.localizacion = localizacion;
}
public String getPoseeLocalizacion() {
  return poseeLocalizacion;
}
public void setPoseeLocalizacion(String poseeLocalizacion) {
  this.poseeLocalizacion = poseeLocalizacion;
  }

public String getCodProvincia() {
  return codProvincia;
}
public void setCodProvincia(String codProvincia) {
  this.codProvincia = codProvincia;
}
public String getCodTVia() {
  return codTVia;
}
public void setCodTVia(String codTVia) {
  this.codTVia = codTVia;
}
public String getDescMunicipio() {
  return descMunicipio;
}
public void setDescMunicipio(String descMunicipio) {
  this.descMunicipio = descMunicipio;
}
public String getDescPostal() {
  return descPostal;
}
public void setDescPostal(String descPostal) {
  this.descPostal = descPostal;
}
public String getDescProvincia() {
  return descProvincia;
}
public void setDescProvincia(String descProvincia) {
  this.descProvincia = descProvincia;
}
public String getDescTVia() {
  return descTVia;
}
public void setDescTVia(String descTVia) {
  this.descTVia = descTVia;
}
public String getTxtBloque() {
  return txtBloque;
}
public void setTxtBloque(String txtBloque) {
  this.txtBloque = txtBloque;
}
public String getTxtDomicilio() {
  return txtDomicilio;
}
public void setTxtDomicilio(String txtDomicilio) {
  this.txtDomicilio = txtDomicilio;
}
public String getTxtEsc() {
  return txtEsc;
}
public void setTxtEsc(String txtEsc) {
  this.txtEsc = txtEsc;
}
public String getTxtLetraDesde() {
  return txtLetraDesde;
}
public void setTxtLetraDesde(String txtLetraDesde) {
  this.txtLetraDesde = txtLetraDesde;
}
public String getTxtLetraHasta() {
  return txtLetraHasta;
}
public void setTxtLetraHasta(String txtLetraHasta) {
  this.txtLetraHasta = txtLetraHasta;
}
public String getTxtNumDesde() {
  return txtNumDesde;
}
public void setTxtNumDesde(String txtNumDesde) {
  this.txtNumDesde = txtNumDesde;
}
public String getTxtNumHasta() {
  return txtNumHasta;
}
public void setTxtNumHasta(String txtNumHasta) {
  this.txtNumHasta = txtNumHasta;
}
public String getTxtPlta() {
  return txtPlta;
}
public void setTxtPlta(String txtPlta) {
  this.txtPlta = txtPlta;
}
public String getTxtPortal() {
  return txtPortal;
}
public void setTxtPortal(String txtPortal) {
  this.txtPortal = txtPortal;
}
public String getTxtPta() {
  return txtPta;
}
public void setTxtPta(String txtPta) {
  this.txtPta = txtPta;
}
public String getTxtRefCatastral() {
  return txtRefCatastral;
}
public void setTxtRefCatastral(String txtRefCatastral) {
  this.txtRefCatastral = txtRefCatastral;
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
public String getIdDomicilio() {
  return idDomicilio;
}
public void setIdDomicilio(String idDomicilio) {
  this.idDomicilio = idDomicilio;
}

public Vector getListaTramitesPendientes() {
  return listaTramitesPendientes;
}
public void setListaTramitesPendientes(Vector listaTramitesPendientes) {
  this.listaTramitesPendientes = listaTramitesPendientes;
}

public String getModoConsulta() {
  return modoConsulta;
}
public void setModoConsulta(String modoConsulta) {
  this.modoConsulta = modoConsulta;
}
public String getExpRelacionado() {
  return expRelacionado;
}
public void setExpRelacionado(String expRelacionado) {
  this.expRelacionado = expRelacionado;
}

public String getCodMunicipioIni() {
  return codMunicipioIni;
}
public void setCodMunicipioIni(String codMunicipioIni) {
  this.codMunicipioIni = codMunicipioIni;
}
public String getEjercicioIni() {
  return ejercicioIni;
}
public void setEjercicioIni(String ejercicioIni) {
  this.ejercicioIni = ejercicioIni;
}
public String getNumeroIni() {
  return numeroIni;
}
public void setNumeroIni(String numeroIni) {
  this.numeroIni = numeroIni;
}

public String getCodLocalizacion() {
  return codLocalizacion;
}
public void setCodLocalizacion(String codLocalizacion) {
  this.codLocalizacion = codLocalizacion;
}

public String getObservaciones() {
  return observaciones;
}
public void setObservaciones(String observaciones) {
  this.observaciones = observaciones;
}

public String getCodUnidadOrganicaExp() {
  return codUnidadOrganicaExp;
}
public void setCodUnidadOrganicaExp(String codUnidadOrganicaExp) {
  this.codUnidadOrganicaExp = codUnidadOrganicaExp;
}
public String getCodUnidadTramitadoraTram() {
  return codUnidadTramitadoraTram;
}
public void setCodUnidadTramitadoraTram(String codUnidadTramitadoraTram) {
  this.codUnidadTramitadoraTram = codUnidadTramitadoraTram;
}
public String getCodUnidadInicioTram() {
  return codUnidadInicioTram;
}
public void setCodUnidadInicioTram(String codUnidadInicioTram) {
  this.codUnidadInicioTram = codUnidadInicioTram;
}
public String getCodUnidadTramitadoraUsu() {
  return codUnidadTramitadoraUsu;
}
public void setCodUnidadTramitadoraUsu(String codUnidadTramitadoraUsu) {
  this.codUnidadTramitadoraUsu = codUnidadTramitadoraUsu;
}

public String getCodEntidad() {
  return codEntidad;
}
public void setCodEntidad(String codEntidad) {
  this.codEntidad = codEntidad;
}
public String getCodOrganizacion() {
  return codOrganizacion;
}
public void setCodOrganizacion(String codOrganizacion) {
  this.codOrganizacion = codOrganizacion;
}
public String getCodPlantilla() {
  return codPlantilla;
}
public void setCodPlantilla(String codPlantilla) {
  this.codPlantilla = codPlantilla;
}
public String getTipoPlantilla() {
  return tipoPlantilla;
}
public void setTipoPlantilla(String tipoPlantilla) {
  this.tipoPlantilla = tipoPlantilla;
}

public String getInteresado() {
  return interesado;
}
public void setInteresado(String interesado) {
  this.interesado = interesado;
}
public String getEditorTexto() {
    return editorTexto;
}
public void setEditorTexto(String editorTexto) {
    this.editorTexto = editorTexto;
}
public String getDescEnlace() {
  return descEnlace;
}
public void setDescEnlace(String descEnlace) {
  this.descEnlace = descEnlace;
}
public Vector getListaEnlaces() {
  return listaEnlaces;
}
public void setListaEnlaces(Vector listaEnlaces) {
  this.listaEnlaces = listaEnlaces;
}
public String getUrl() {
  return url;
}
public void setUrl(String url) {
  this.url = url;
}

public Vector getEstructuraDatosSuplementarios() {
  return estructuraDatosSuplementarios;
}
public void setEstructuraDatosSuplementarios(Vector estructuraDatosSuplementarios) {
  this.estructuraDatosSuplementarios = estructuraDatosSuplementarios;
}
public Vector getValoresDatosSuplementarios() {
  return valoresDatosSuplementarios;
}
public void setValoresDatosSuplementarios(Vector valoresDatosSuplementarios) {
  this.valoresDatosSuplementarios = valoresDatosSuplementarios;
}

public Vector getListaInteresados() {
  return listaInteresados;
}
public void setListaInteresados(Vector listaInteresados) {
  this.listaInteresados = listaInteresados;
}
public Vector getListaEMailsAlIniciar() {
  return listaEMailsAlIniciar;
}
public void setListaEMailsAlIniciar(Vector listaEMailsAlIniciar) {
  this.listaEMailsAlIniciar = listaEMailsAlIniciar;
}

public Vector getListaEMailsAlFinalizar() {
  return listaEMailsAlFinalizar;
}
public void setListaEMailsAlFinalizar(Vector listaEMailsAlFinalizar) {
  this.listaEMailsAlFinalizar = listaEMailsAlFinalizar;
}

public Vector getListaRoles() {
  return listaRoles;
}
public void setListaRoles(Vector listaRoles) {
  this.listaRoles = listaRoles;
}

public void setListaCodInteresados(String listaCodInteresados) {
  this.listaCodInteresados = listaCodInteresados;	
}
public void setVectorCodInteresados(Vector vectorCodInteresados) {
  this.vectorCodInteresados = vectorCodInteresados;
}
public void setListaVersInteresados(String listaVersInteresados) {
  this.listaVersInteresados = listaVersInteresados;	
}
public String getListaCodInteresados() {
  return listaCodInteresados;
}
public Vector getVectorCodInteresados() {
  return vectorCodInteresados;
}
public String getListaVersInteresados() {
  return listaVersInteresados;	
}

public String getDesdeInformesGestion() {
  return desdeInformesGestion;	
}
public void setDesdeInformesGestion(String desdeInformesGestion) {
  this.desdeInformesGestion = desdeInformesGestion;	
}


public String getTodos() {
  return todos;	
}
public void setTodos(String todos) {
  this.todos = todos;	
}


public GeneralValueObject getListaFicheros(){ return listaFicheros; }
public void setListaFicheros(GeneralValueObject listaFicheros){ this.listaFicheros=listaFicheros; }

public GeneralValueObject getListaTiposFicheros(){ return listaTiposFicheros; }
public void setListaTiposFicheros(GeneralValueObject listaTiposFicheros){ this.listaTiposFicheros=listaTiposFicheros; }


public String getMensajeSW() {
	return mensajeSW;
}
public void setMensajeSW(String mensajeSW) {
	this.mensajeSW = mensajeSW;
}


	/**
     * Valida el estado de esta RegistroSaida
     * Puede ser invocado desde la capa cliente o desde la capa de negocio
     * @exception es.altia.technical.ValidationException si el estado no es válido
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

    private String numeroRelacion;

    private String opcionGrabar;

    private String numeroExpediente;
    private String numeroExpedienteMostrar;
    private String procedimiento;
    private String titular;
    private String tramite;
    private String fechaInicio;
    private String fechaFin;
    private String unidadInicio;
    private String unidadTramitadora;
    private String unidadTramitadoraTramiteIniciado;
    private String clasificacionTramite;
    private String plazo;
    private String tipoPlazo;
    private String fechaInicioPlazo;
    private String fechaLimite;
    private String fechaFinPlazo;
    private String localizacion;
    private String poseeLocalizacion;
    private String observaciones;
    private String instrucciones;

    private String codMunicipio;
    private String codProcedimiento;
    private String ejercicio;
    private String numero;
    private String codTramite;
    private String ocurrenciaTramite;

    private String descDocumento;
    private String fechaCreacion;
    private String fechaModificacion;
    private String usuario;
    private String codDocumento;
    private String codPlantilla;
    private String tipoPlantilla;
    private String interesado;
    private String editorTexto;
    private Vector listaCodDocumentosTramite;

    private Vector listaDocumentos;

    private String codigoDocumento;
    private String descripcionDocumento;
    private Vector listaDocumentosTramite;

    private String respOpcion;
    private String notificacionRealizada;

    private String accion;
    private String accionAfirmativa;
    private String accionNegativa;
    private String obligatorio;
    private String obligatorioDesf;
    private String pregunta;
    private String codigoTramiteFlujoSalida;
    private String numeroSecuencia;
    private String codigoVisibleTramiteFlujoSalida;
    private String descripcionTramiteFlujoSalida;
    private Vector listaTramitesFavorables;
    private Vector listaTramitesNoFavorables;

    private Vector listaTramitesIniciar;
    private Vector listaTramitesExpediente;

    private String codUsuario;
    private String codEntidad;
    private String codOrganizacion;
    private String codUOR;

    private String permiso;

    private String bloqueo;

    private String codProvincia;
    private String descProvincia;
    private String descMunicipio;
    private String codTVia;
    private String descTVia;
    private String descPostal;
    private String txtNumDesde;
    private String txtLetraDesde;
    private String txtNumHasta;
    private String txtLetraHasta;
    private String txtBloque;
    private String txtPortal;
    private String txtEsc;
    private String txtPlta;
    private String txtPta;
    private String txtDomicilio;
    private String txtRefCatastral;

    private String paginaListado;
    private String numLineasPaginaListado;

    private String idDomicilio;

    private Vector listaTramitesPendientes;

    private String modoConsulta;
    private String expRelacionado;
    private String codMunicipioIni;
    private String ejercicioIni;
    private String numeroIni;

    private String codLocalizacion;

    private String codUnidadOrganicaExp;
    private String codUnidadTramitadoraTram;
    private String codUnidadInicioTram;
    private String codUnidadTramitadoraUsu;

    private String descEnlace;
    private String url;
    private Vector listaEnlaces;

    private Vector estructuraDatosSuplementarios;
    private Vector valoresDatosSuplementarios;
    
    private Vector listaInteresados;
    private Vector listaEMailsAlIniciar;
    private Vector listaEMailsAlFinalizar;
    private Vector listaRoles;
    
    private String listaCodInteresados;
    private Vector vectorCodInteresados;
    private String listaVersInteresados;
    
    private String desdeInformesGestion;
    private String todos;

    private GeneralValueObject listaFicheros = new GeneralValueObject();
    private GeneralValueObject listaTiposFicheros = new GeneralValueObject();

	private String mensajeSW;

	protected String pEstadoFirma = null;
    public String getEstadoFirma() {
        return pEstadoFirma;
    }
    public void setEstadoFirma(String estadoFirma) {
        pEstadoFirma = estadoFirma;
    }
    /* ******************************************************** */
}