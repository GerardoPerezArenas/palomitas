package es.altia.agora.business.sge;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoVO;
import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class DefinicionProcedimientosValueObject implements Serializable, ValueObject {
    
    /** Construye un nuevo registroEntradaSalida por defecto. */
    public DefinicionProcedimientosValueObject() {
        super();
    }


 public String getCodTipoProcedimiento() {
   return codTipoProcedimiento;
 }
 public void setCodTipoProcedimiento(String codTipoProcedimiento) {
   this.codTipoProcedimiento = codTipoProcedimiento;
 }
 public String getCodUnidadInicio() {
   return codUnidadInicio;
 }
 public void setCodUnidadInicio(String codUnidadInicio) {
   this.codUnidadInicio = codUnidadInicio;
 }
 public String getCodVisibleUnidadInicio() {
   return codVisibleUnidadInicio;
 }
 public void setCodVisibleUnidadInicio(String codVisibleUnidadInicio) {
   this.codVisibleUnidadInicio = codVisibleUnidadInicio;
 }
 public String getDescTipoProcedimiento() {
   return descTipoProcedimiento;
 }
 public void setDescTipoProcedimiento(String descTipoProcedimiento) {
   this.descTipoProcedimiento = descTipoProcedimiento;
 }
 public String getDescUnidadInicio() {
   return descUnidadInicio;
 }
 public void setDescUnidadInicio(String descUnidadEncargInicio) {
   this.descUnidadInicio = descUnidadEncargInicio;
 }
 public String getDisponible() {
   return disponible;
 }
 public void setDisponible(String disponible) {
   this.disponible = disponible;
 }
 public String getTramitacionInternet() {
   return tramitacionInternet;
 }
 public void setTramitacionInternet(String tramitacionInternet) {
   this.tramitacionInternet = tramitacionInternet;
 }
 public String getPlazo() {
   return plazo;
 }
 public void setPlazo(String plazo) {
   this.plazo = plazo;
 }
 public String getTipoPlazo() {
   return tipoPlazo;
 }
 public void setTipoPlazo(String tipoPlazo) {
   this.tipoPlazo = tipoPlazo;
 }
 public String getTipoSilencio() {
   return tipoSilencio;
 }
 public void setTipoSilencio(String tipoSilencio) {
   this.tipoSilencio = tipoSilencio;
 }
 public String getTxtCodigo() {
   return txtCodigo;
 }
 public void setTxtCodigo(String txtCodigo) {
   this.txtCodigo = txtCodigo;
 }


    // Campo Descripcion Breve.
    public String getDescripcionBreve() {
        return descripcionBreve;
    }

    public void setDescripcionBreve(String descripcionBreve) {
        this.descripcionBreve = descripcionBreve;
    }


 public String getTxtDescripcion() {
   return txtDescripcion;
 }
 public void setTxtDescripcion(String txtDescripcion) {
   this.txtDescripcion = txtDescripcion;
 }
 public String getNormativa() {
  return normativa;
}
public void setNormativa(String normativa) {
  this.normativa = normativa;
  }
  public String getLocalizacion() {
    return localizacion;
  }
  public void setLocalizacion(String localizacion) {
    this.localizacion = localizacion;
  }

 public Vector getListaTiposProcedimientos() {
   return listaTiposProcedimientos;
 }
 public void setListaTiposProcedimientos(Vector listaTiposProcedimientos) {
   this.listaTiposProcedimientos = listaTiposProcedimientos;
 }
 public Vector getListaTiposDocumentos() {
   return listaTiposDocumentos;
 }
 public void setListaTiposDocumentos(Vector listaTiposDocumentos) {
   this.listaTiposDocumentos = listaTiposDocumentos;
 }
 public Vector getListaArea() {
  return listaArea;
}
public void setListaArea(Vector listaArea) {
  this.listaArea = listaArea;
}
public Vector getListaUnidadInicio() {
  return listaUnidadInicio;
}
public void setListaUnidadInicio(Vector listaUnidadInicio) {
  this.listaUnidadInicio = listaUnidadInicio;
  }

 public String getIdTipoProcedimiento() {
   return idTipoProcedimiento;
 }
 public void setIdTipoProcedimiento(String idTipoProcedimiento) {
   this.idTipoProcedimiento = idTipoProcedimiento;
 }
 public String getIdNaturaleza() {
   return idNaturaleza;
 }
 public void setIdNaturaleza(String idNaturaleza) {
   this.idNaturaleza = idNaturaleza;
 }

 public int getNoEncontrado() {
   return noEncontrado;
 }
 public void setNoEncontrado(int noEncontrado) {
   this.noEncontrado = noEncontrado;
 }

 public String getNumeroTramite() {
    return numeroTramite;
  }
 public void setNumeroTramite(String numeroTramite) {
    this.numeroTramite = numeroTramite;
 }
 public String getCodigoTramite() {
    return codigoTramite;
  }
 public void setCodigoTramite(String codigoTramite) {
    this.codigoTramite = codigoTramite;
 }
 public String getNombreTramite() {
   return nombreTramite;
 }
 public void setNombreTramite(String nombreTramite) {
   this.nombreTramite = nombreTramite;
 }
 public String getCodClasificacionTramite() {
   return codClasificacionTramite;
 }
 public void setCodClasificacionTramite(String codClasificacionTramite) {
   this.codClasificacionTramite = codClasificacionTramite;
 }
 public String getDescClasificacionTramite() {
   return descClasificacionTramite;
 }
 public void setDescClasificacionTramite(String nombreClasificacionTramite) {
   this.descClasificacionTramite = nombreClasificacionTramite;
 }

 public String getPosicionProcedimiento() {
   return posicionProcedimiento;
 }
 public void setPosicionProcedimiento(String posicionProcedimiento) {
   this.posicionProcedimiento = posicionProcedimiento;
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

 public Vector getTramites() {
   return tramites;
 }
 public void setTramites(Vector v) {
   this.tramites = v;
 }

 // Listas de la pestaña Documentos

 public Vector getListaCodigosDoc() {
    return listaCodigosDoc;
  }
  public void setListaCodigosDoc(Vector listaCodigosDoc) {
    this.listaCodigosDoc = listaCodigosDoc;
  }
  public Vector getListaCodTipoDoc() {
    return listaCodTipoDoc;
  }
  public void setListaCodTipoDoc(Vector listaCodTipoDoc) {
    this.listaCodTipoDoc = listaCodTipoDoc;
  }
  public Vector getListaCondicionDoc() {
    return listaCondicionDoc;
  }
  public void setListaCondicionDoc(Vector listaCondicionDoc) {
    this.listaCondicionDoc = listaCondicionDoc;
  }

    public String getRequiereFirma() {
        return requiereFirma;
    }

    public void setRequiereFirma(String requiereFirma) {
        this.requiereFirma = requiereFirma;
    }
  
  public Vector getListaNombresDoc() {
    return listaNombresDoc;
  }
  public void setListaNombresDoc(Vector listaNombresDoc) {
    this.listaNombresDoc = listaNombresDoc;
  }
  public Vector getListaObligatoriosDoc() {
    return listaObligatoriosDoc;
  }
  public void setListaObligatoriosDoc(Vector listaObligatoriosDoc) {
    this.listaObligatoriosDoc = listaObligatoriosDoc;
  }
  public String getCodArea() {
    return codArea;
  }
  public void setCodArea(String codArea) {
    this.codArea = codArea;
  }
  public String getCodEstado() {
    return codEstado;
  }
  public void setCodEstado(String codEstado) {
    this.codEstado = codEstado;
  }
    public String getCqUnidadInicio() {
      return cqUnidadInicio;
    }
    public void setCqUnidadInicio(String cqUnidadInicio) {
      this.cqUnidadInicio = cqUnidadInicio;
    }
  public String getDescArea() {
    return descArea;
  }
  public void setDescArea(String descArea) {
    this.descArea = descArea;
  }
  public String getDescEstado() {
    return descEstado;
  }
  public void setDescEstado(String descEstado) {
    this.descEstado = descEstado;
  }
  public String getFechaLimiteDesde() {
    return fechaLimiteDesde;
  }
  public void setFechaLimiteDesde(String fechaLimiteDesde) {
    this.fechaLimiteDesde = fechaLimiteDesde;
  }
  public String getFechaLimiteHasta() {
    return fechaLimiteHasta;
  }
  public void setFechaLimiteHasta(String fechaLimiteHasta) {
    this.fechaLimiteHasta = fechaLimiteHasta;
  }

  public String getCodigoDocumento() {
  return codigoDocumento;
}
public void setCodigoDocumento(String codigoDocumento) {
  this.codigoDocumento = codigoDocumento;
}
public String getCodTipoDocumento() {
  return codTipoDocumento;
}
public void setCodTipoDocumento(String codTipoDocumento) {
  this.codTipoDocumento = codTipoDocumento;
}
public String getDescTipoInicio() {
  return descTipoInicio;
}
public void setDescTipoInicio(String descTipoInicio) {
  this.descTipoInicio = descTipoInicio;
}
public String getCodTipoInicio() {
  return codTipoInicio;
}
public void setCodTipoInicio(String codTipoInicio) {
  this.codTipoInicio = codTipoInicio;
  }
public String getCondicion() {
  return condicion;
}
public void setCondicion(String condicion) {
  this.condicion = condicion;
}
public String getDescTipoDocumento() {
  return descTipoDocumento;
}
public void setDescTipoDocumento(String descTipoDocumento) {
  this.descTipoDocumento = descTipoDocumento;
}
public String getNombreDocumento() {
  return nombreDocumento;
}
public void setNombreDocumento(String nombreDocumento) {
  this.nombreDocumento = nombreDocumento;
}
public String getObligatorio() {
  return obligatorio;
}
public void setObligatorio(String obligatorio) {
  this.obligatorio = obligatorio;
  }
  public String getCodMunicipio() {
    return codMunicipio;
  }
  public void setCodMunicipio(String codMunicipio) {
    this.codMunicipio = codMunicipio;
  }
  public String getDescMunicipio() {
    return descMunicipio;
  }
  public void setDescMunicipio(String descMunicipio) {
    this.descMunicipio = descMunicipio;
  }
  public String getCodAplicacion() {
    return codAplicacion;
  }
  public void setCodAplicacion(String codAplicacion) {
    this.codAplicacion = codAplicacion;
  }
  public String getImportar() {
    return importar;
  }
  public void setImportar(String importar) {
    this.importar = importar;
  }

  public Vector getListasDoc() {
    return listasDoc;
  }
  public void setListasDoc(Vector listasDoc) {
    this.listasDoc = listasDoc;
  }

  public String getNoModificar() {
    return noModificar;
  }
  public void setNoModificar(String noModificar) {
    this.noModificar = noModificar;
  }
  public String getDeCatalogo() {
    return deCatalogo;
  }
  public void setDeCatalogo(String deCatalogo) {
    this.deCatalogo = deCatalogo;
  }
  public String getDeCatalogoDeProcedimiento() {
    return deCatalogoDeProcedimiento;
  }
  public void setDeCatalogoDeProcedimiento(String deCatalogoDeProcedimiento) {
    this.deCatalogoDeProcedimiento = deCatalogoDeProcedimiento;
  }

  public Vector getTablaUnidadInicio() {
    return tablaUnidadInicio;
  }
  public void setTablaUnidadInicio(Vector tablaUnidadInicio) {
    this.tablaUnidadInicio = tablaUnidadInicio;
  }

  public Vector getListaCodUnidadInicio() {
    return listaCodUnidadInicio;
  }
  public void setListaCodUnidadInicio(Vector listaCodUnidadInicio) {
    this.listaCodUnidadInicio = listaCodUnidadInicio;
  }
  public Vector getListaDescUnidadInicio() {
    return listaDescUnidadInicio;
  }
  public void setListaDescUnidadInicio(Vector listaDescUnidadInicio) {
    this.listaDescUnidadInicio = listaDescUnidadInicio;
  }

  public Vector getListaCampos() {
    return listaCampos;
  }
  public void setListaCampos(Vector listaCampos) {
    this.listaCampos = listaCampos;
  }

  public Vector getListaCodCampos() {
    return listaCodCampos;
  }
  public void setListaCodCampos(Vector listaCodCampos) {
    this.listaCodCampos = listaCodCampos;
  }
  public Vector getListaCodPlantilla() {
    return listaCodPlantilla;
  }
  public void setListaCodPlantilla(Vector listaCodPlantilla) {
    this.listaCodPlantilla = listaCodPlantilla;
  }
  public Vector getListaCodTipoDato() {
    return listaCodTipoDato;
  }
  public void setListaCodTipoDato(Vector listaCodTipoDato) {
    this.listaCodTipoDato = listaCodTipoDato;
  }
  public Vector getListaDescCampos() {
    return listaDescCampos;
  }
  public void setListaDescCampos(Vector listaDescCampos) {
    this.listaDescCampos = listaDescCampos;
  }
  public Vector getListaMascara() {
    return listaMascara;
  }
  public void setListaMascara(Vector listaMascara) {
    this.listaMascara = listaMascara;
  }
  public Vector getListaObligatorio() {
    return listaObligatorio;
  }
  public void setListaObligatorio(Vector listaObligatorio) {
    this.listaObligatorio = listaObligatorio;
  }
  public Vector getListaOrden() {
    return listaOrden;
  }
  public void setListaOrden(Vector listaOrden) {
    this.listaOrden = listaOrden;
  }
  public Vector getListaTamano() {
    return listaTamano;
  }
  public void setListaTamano(Vector listaTamano) {
    this.listaTamano = listaTamano;
  }

  public String getFueraLimite() {
    return fueraLimite;
  }
  public void setFueraLimite(String fueraLimite) {
    this.fueraLimite = fueraLimite;
  }

  public String getCodEnlace() {
    return codEnlace;
  }
  public void setCodEnlace(String codEnlace) {
    this.codEnlace = codEnlace;
  }
  public String getDescEnlace() {
    return descEnlace;
  }
  public void setDescEnlace(String descEnlace) {
    this.descEnlace = descEnlace;
  }
  public String getEstadoEnlace() {
    return estadoEnlace;
  }
  public void setEstadoEnlace(String estadoEnlace) {
    this.estadoEnlace = estadoEnlace;
  }
  public Vector getListaEnlaces() {
    return listaEnlaces;
  }
  public void setListaEnlaces(Vector listaEnlaces) {
    this.listaEnlaces = listaEnlaces;
  }
  public String getUrlEnlace() {
    return urlEnlace;
  }
  public void setUrlEnlace(String urlEnlace) {
    this.urlEnlace = urlEnlace;
  }
  public Vector getListaCodEnlaces() {
    return listaCodEnlaces;
  }
  public void setListaCodEnlaces(Vector listaCodEnlaces) {
    this.listaCodEnlaces = listaCodEnlaces;
  }
  public Vector getListaDescEnlaces() {
    return listaDescEnlaces;
  }
  public void setListaDescEnlaces(Vector listaDescEnlaces) {
    this.listaDescEnlaces = listaDescEnlaces;
  }
  public Vector getListaEstadoEnlaces() {
    return listaEstadoEnlaces;
  }
  public void setListaEstadoEnlaces(Vector listaEstadoEnlaces) {
    this.listaEstadoEnlaces = listaEstadoEnlaces;
  }
  public Vector getListaUrlEnlaces() {
    return listaUrlEnlaces;
  }
  public void setListaUrlEnlaces(Vector listaUrlEnlaces) {
    this.listaUrlEnlaces = listaUrlEnlaces;
  }

  public Vector getListaRotulo() {
    return listaRotulo;
  }
  public void setListaRotulo(Vector listaRotulo) {
    this.listaRotulo = listaRotulo;
  }

  public Vector getListaActivos() {
    return listaActivos;
  }
  public void setListaActivos(Vector listaActivo) {
    this.listaActivos = listaActivo;
  }

  public Vector getListaOcultos() {
    return listaOcultos;
  }
  public void setListaOcultos(Vector listaOculto) {
    this.listaOcultos = listaOculto;
  }

    public Vector getListaBloqueados() {
        return listaBloqueados;
    }

    public void setListaBloqueados(Vector listaBloqueados) {
        this.listaBloqueados = listaBloqueados;
    }

    public Vector getListaPlazoFecha() {
        return listaPlazoFecha;
    }

    public void setListaPlazoFecha(Vector listaPlazoFecha) {
        this.listaPlazoFecha = listaPlazoFecha;
    }

    public Vector getListaCheckPlazoFecha() {
        return listaCheckPlazoFecha;
    }

    public void setListaCheckPlazoFecha(Vector listaCheckPlazoFecha) {
        this.listaCheckPlazoFecha = listaCheckPlazoFecha;
    }
    


  public String getCodRol() {
    return codRol;
  }
  public void setCodRol(String codRol) {
    this.codRol = codRol;
  }
  public String getDescRol() {
    return descRol;
  }
  public void setDescRol(String descRol) {
    this.descRol = descRol;
  }
  public Vector getListaRoles() {
    return listaRoles;
  }
  public void setListaRoles(Vector listaRoles) {
    this.listaRoles = listaRoles;
  }
  public String getRolPorDefecto() {
    return rolPorDefecto;
  }
  public void setRolPorDefecto(String rolPorDefecto) {
    this.rolPorDefecto = rolPorDefecto;
  }

  public String getConsultaWebRol() {
    return consultaWebRol;
  }

  public void setConsultaWebRol(String consultaWebRol) {
    this.consultaWebRol = consultaWebRol;
  }

  public Vector getListaCodRoles() {
    return listaCodRoles;
  }
  public void setListaCodRoles(Vector listaCodRoles) {
    this.listaCodRoles = listaCodRoles;
  }
  public Vector getListaDescRoles() {
    return listaDescRoles;
  }
  public void setListaDescRoles(Vector listaDescRoles) {
    this.listaDescRoles = listaDescRoles;
  }
  public Vector getListaPorDefecto() {
    return listaPorDefecto;
  }
  public void setListaPorDefecto(Vector listaPorDefecto) {
    this.listaPorDefecto = listaPorDefecto;
  }

  public Vector getListaConsultaWebRol() {
    return listaConsultaWebRol;
  }

  public void setListaConsultaWebRol(Vector listaConsultaWebRol) {
    this.listaConsultaWebRol = listaConsultaWebRol;
  }

  public String getTramiteInicio() {
    return tramiteInicio;
  }
  public void setTramiteInicio(String tramiteInicio) {
    this.tramiteInicio = tramiteInicio;
  }

    public void copy(DefinicionProcedimientosValueObject other) {
      this.txtCodigo = other.txtCodigo;
      this.txtDescripcion = other.txtDescripcion;
        this.descripcionBreve = other.descripcionBreve;
      this.codTipoProcedimiento = other.codTipoProcedimiento;
      this.descTipoProcedimiento = other.descTipoProcedimiento;
      this.plazo = other.plazo;
      this.tipoPlazo = other.tipoPlazo;
      this.tipoSilencio = other.tipoSilencio;
      this.codUnidadInicio = other.codUnidadInicio;
      this.descUnidadInicio = other.descUnidadInicio;
      this.disponible = other.disponible;
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

    private String txtCodigo;
    private String txtDescripcion;
    private String codTipoProcedimiento;
    private String descTipoProcedimiento;
    private String plazo;
    private String normativa;
    private String localizacion;
    private String tipoPlazo;
    private String tipoSilencio;
    private String codUnidadInicio;
    private String codVisibleUnidadInicio;
    private String descUnidadInicio;
    private String disponible;
    private String tramitacionInternet;
    private String InteresadoOblig;
   //Este propiedad indica si e procedimiento sólo se puede iniciar mediante web Service.
    private String soloWS;
    private String codigoTramite;
    private String numeroTramite;
    private String nombreTramite;
    private String codClasificacionTramite;
    private String descClasificacionTramite;
    private String codArea;
    private String descArea;
    private Integer errorArea;
    private String codEstado;
    private String cqUnidadInicio;
    private String descEstado;
    private String codTipoInicio;
    private String descTipoInicio;
    private String tramiteInicio;

    private Vector listaTiposProcedimientos;
    private Vector listaTiposDocumentos;
    private Vector listaArea;
    private Vector listaUnidadInicio;
    private Vector tablaUnidadInicio;

    private String fechaLimiteHasta;
    private String fechaLimiteDesde;

    private String idTipoProcedimiento;
    private String idNaturaleza;

    private int noEncontrado;

    private String posicionProcedimiento;

    private String codMunicipio;
    private String descMunicipio;
    private String codAplicacion;
    private String importar;

    private String paginaListado;
    private String numLineasPaginaListado;

    private String descripcionBreve;    // Descripcion Breve.
    
    private String procedimientoDigit;

    Vector tramites = new Vector();

    // Listas de la pestaña Documentos
    Vector listaNombresDoc = new Vector();
    Vector listaCodigosDoc = new Vector();
    Vector listaObligatoriosDoc = new Vector();
    Vector listaCondicionDoc = new Vector();
    Vector listaCodTipoDoc = new Vector();

    private String codigoDocumento;
    private String nombreDocumento;
    private String obligatorio;
    private String condicion;
    private String codTipoDocumento;
    private String descTipoDocumento;

    Vector listaCodUnidadInicio = new Vector();
    Vector listaDescUnidadInicio = new Vector();

    Vector listasDoc = new Vector();

    private String noModificar;
    private String deCatalogo;
    private String deCatalogoDeProcedimiento;

    Vector listaCampos = new Vector();
    // Listas de la pestaña Campos
    Vector listaCodCampos = new Vector();
    Vector listaDescCampos = new Vector();
    Vector listaCodPlantilla = new Vector();
    Vector listaCodTipoDato = new Vector();
    Vector listaTamano = new Vector();
    Vector listaMascara = new Vector();
    Vector listaObligatorio = new Vector();
    Vector listaOrden = new Vector();
    Vector listaRotulo = new Vector();
    Vector listaActivos = new Vector();
    Vector listaOcultos = new Vector();
    Vector listaBloqueados = new Vector();
    private Vector listaValidacion = new Vector();
    private Vector listaOperacion = new Vector();
    Vector listaPlazoFecha = new Vector();
    Vector listaCheckPlazoFecha = new Vector();
    Vector listaAgrupacionesCampo = new Vector();
    Vector listaPosicionesX = new Vector();
    Vector listaPosicionesY = new Vector();
    
    Vector listaAgrupaciones = new Vector();
    Vector listaCodAgrupaciones = new Vector();
    Vector listaDescAgrupaciones = new Vector();
    Vector listaOrdenAgrupaciones = new Vector();
    Vector listaAgrupacionesActivas = new Vector();

    private String fueraLimite;

    private String codEnlace;
    private String descEnlace;
    private String urlEnlace;
    private String estadoEnlace;

    Vector listaEnlaces = new Vector();
    // Listas de la pestaña Enlaces
    Vector listaCodEnlaces = new Vector();
    Vector listaDescEnlaces = new Vector();
    Vector listaUrlEnlaces = new Vector();
    Vector listaEstadoEnlaces = new Vector();

    private String codRol;
    private String descRol;
    private String rolPorDefecto;
    private String consultaWebRol;

    Vector listaRoles = new Vector();
    Vector listaCodRoles = new Vector();
    Vector listaDescRoles = new Vector();
    Vector listaPorDefecto = new Vector();
    Vector listaConsultaWebRol = new Vector();
    private Hashtable<String,String> participantes = new Hashtable<String,String>();
    private String restringido = null;
    private String biblioteca = null;
    private String requiereFirma;
    // #303601: propiedad que almacena el valor de "CONFIGURAR_PROCEDIMIENTO_ANOEXPANOTACION" en common.properties
    private String propNumExpedientesAnoAsientoBuzon = null;
    // #303601: propiedad que indica si el procedimiento en concreto acepta que sus expedientes se numeren según el año de la anotacion
    private int numeracionExpedientesAnoAsiento = 0;


    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;

    /** Porcentaje de aviso con respecto a la fecha de fin de un expediente */
    private String porcentaje;
    
    private Vector<UORDTO> uorsTramiteInicio = new Vector<UORDTO>();
    
    private ArrayList<CampoListadoPendientesProcedimientoVO> camposPendientesSeleccionados;

    private String listaCodigosCamposPendientes;
    private String listaNombresCamposPendientes;
    private String listaTamanhoCamposPendientes;
    private String listaOrdenCamposPendientes;
    private String listaCampoSupCamposPendientes;

    private Vector coleccionCodigosCamposPendientes;
    private Vector coleccionNombresCamposPendientes;
    private Vector coleccionTamanhoCamposPendientes;
    private Vector coleccionOrdenCamposPendientes;
    private Vector coleccionCampoSupCamposPendientes;

    ArrayList<FirmasDocumentoProcedimientoVO> firmasDocumentosProcedimiento = new ArrayList<FirmasDocumentoProcedimientoVO>();

    
    private String descServicioFinalizacion;
    private String codServicioFinalizacion;
    private String implClassServicioFinalizacion;
    private String descClaseHist;
    private String codClaseHist;   
    private String ClaseBuzonEntradaHistorico;
	
	// Propiedad necesaria en la importacion/exportacion XPDL en la que se guardan los datos de flujos de firma
	private List<FirmaFlujoVO> listaFlujosFirma;
    
        

    public Vector<UORDTO> getUorsTramiteInicio() {
        return uorsTramiteInicio;
    }

    public void setUorsTramiteInicio(Vector<UORDTO> uorsTramiteInicio) {
        this.uorsTramiteInicio = uorsTramiteInicio;
    }

    /**
     * @return the porcentaje
     */
    public String getPorcentaje() {
        return porcentaje;
    }

    /**
     * @param porcentaje the porcentaje to set
     */
    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    /**
     * @return the participantes
     */
    public Hashtable<String,String> getParticipantes() {
        return participantes;
    }

    /**
     * @param participantes the participantes to set
     */
    public void setParticipantes(Hashtable<String,String> participantes) {
        this.participantes = participantes;
    }

    /**
     * @return the restringido
     */
    public String getRestringido() {
        return restringido;
    }

    /**
     * @param restringido the restringido to set
     */
    public void setRestringido(String restringido) {
        this.restringido = restringido;
    }
  
    /**
     * @return the camposPendientesSeleccionados
     */
    public ArrayList<CampoListadoPendientesProcedimientoVO> getCamposPendientesSeleccionados() {
        return camposPendientesSeleccionados;
    }

    /**
     * @param camposPendientesSeleccionados the camposPendientesSeleccionados to set
     */
    public void setCamposPendientesSeleccionados(ArrayList<CampoListadoPendientesProcedimientoVO> camposPendientesSeleccionados) {
        this.camposPendientesSeleccionados = camposPendientesSeleccionados;
    }
  

    /**
     * @return the listaCodigosCamposPendientes
     */
    public String getListaCodigosCamposPendientes() {
        return listaCodigosCamposPendientes;
    }

    /**
     * @param listaCodigosCamposPendientes the listaCodigosCamposPendientes to set
     */
    public void setListaCodigosCamposPendientes(String listaCodigosCamposPendientes) {
        this.listaCodigosCamposPendientes = listaCodigosCamposPendientes;
    }

    /**
     * @return the listaNombresCamposPendientes
     */
    public String getListaNombresCamposPendientes() {
        return listaNombresCamposPendientes;
    }

    /**
     * @param listaNombresCamposPendientes the listaNombresCamposPendientes to set
     */
    public void setListaNombresCamposPendientes(String listaNombresCamposPendientes) {
        this.listaNombresCamposPendientes = listaNombresCamposPendientes;
    }

    /**
     * @return the listaTamanhoCamposPendientes
     */
    public String getListaTamanhoCamposPendientes() {
        return listaTamanhoCamposPendientes;
    }

    /**
     * @param listaTamanhoCamposPendientes the listaTamanhoCamposPendientes to set
     */
    public void setListaTamanhoCamposPendientes(String listaTamanhoCamposPendientes) {
        this.listaTamanhoCamposPendientes = listaTamanhoCamposPendientes;
    }

    /**
     * @return the listaOrdenCamposPendientes
     */
    public String getListaOrdenCamposPendientes() {
        return listaOrdenCamposPendientes;
    }

    /**
     * @param listaOrdenCamposPendientes the listaOrdenCamposPendientes to set
     */
    public void setListaOrdenCamposPendientes(String listaOrdenCamposPendientes) {
        this.listaOrdenCamposPendientes = listaOrdenCamposPendientes;
    }

    /**
     * @return the listaCampoSupCamposPendientes
     */
    public String getListaCampoSupCamposPendientes() {
        return listaCampoSupCamposPendientes;
    }

    /**
     * @param listaCampoSupCamposPendientes the listaCampoSupCamposPendientes to set
     */
    public void setListaCampoSupCamposPendientes(String listaCampoSupCamposPendientes) {
        this.listaCampoSupCamposPendientes = listaCampoSupCamposPendientes;
    }

    /**
     * @return the coleccionCodigosCamposPendientes
     */
    public Vector getColeccionCodigosCamposPendientes() {
        return coleccionCodigosCamposPendientes;
    }

    /**
     * @param coleccionCodigosCamposPendientes the coleccionCodigosCamposPendientes to set
     */
    public void setColeccionCodigosCamposPendientes(Vector coleccionCodigosCamposPendientes) {
        this.coleccionCodigosCamposPendientes = coleccionCodigosCamposPendientes;
    }

    /**
     * @return the coleccionNombresCamposPendientes
     */
    public Vector getColeccionNombresCamposPendientes() {
        return coleccionNombresCamposPendientes;
    }

    /**
     * @param coleccionNombresCamposPendientes the coleccionNombresCamposPendientes to set
     */
    public void setColeccionNombresCamposPendientes(Vector coleccionNombresCamposPendientes) {
        this.coleccionNombresCamposPendientes = coleccionNombresCamposPendientes;
    }

    /**
     * @return the coleccionTamanhoCamposPendientes
     */
    public Vector getColeccionTamanhoCamposPendientes() {
        return coleccionTamanhoCamposPendientes;
    }

    /**
     * @param coleccionTamanhoCamposPendientes the coleccionTamanhoCamposPendientes to set
     */
    public void setColeccionTamanhoCamposPendientes(Vector coleccionTamanhoCamposPendientes) {
        this.coleccionTamanhoCamposPendientes = coleccionTamanhoCamposPendientes;
    }

    /**
     * @return the coleccionOrdenCamposPendientes
     */
    public Vector getColeccionOrdenCamposPendientes() {
        return coleccionOrdenCamposPendientes;
    }

    /**
     * @param coleccionOrdenCamposPendientes the coleccionOrdenCamposPendientes to set
     */
    public void setColeccionOrdenCamposPendientes(Vector coleccionOrdenCamposPendientes) {
        this.coleccionOrdenCamposPendientes = coleccionOrdenCamposPendientes;
    }

    /**
     * @return the coleccionCampoSupCamposPendientes
     */
    public Vector getColeccionCampoSupCamposPendientes() {
        return coleccionCampoSupCamposPendientes;
    }

    /**
     * @param coleccionCampoSupCamposPendientes the coleccionCampoSupCamposPendientes to set
     */
    public void setColeccionCampoSupCamposPendientes(Vector coleccionCampoSupCamposPendientes) {
        this.coleccionCampoSupCamposPendientes = coleccionCampoSupCamposPendientes;
    }

     public ArrayList<FirmasDocumentoProcedimientoVO> getFirmasDocumentosProcedimiento() {
        return firmasDocumentosProcedimiento;
    }

    /**
     * @param camposPendientesSeleccionados the camposPendientesSeleccionados to set
     */
    public void setFirmasDocumentosProcedimiento(ArrayList<FirmasDocumentoProcedimientoVO> firmasDocumentosProcedimiento) {
        this.firmasDocumentosProcedimiento = firmasDocumentosProcedimiento;
    }

    /**
     * @return the descServicioFinalizacion
     */
    public String getDescServicioFinalizacion() {
        return descServicioFinalizacion;
    }

    /**
     * @param descServicioFinalizacion the descServicioFinalizacion to set
     */
    public void setDescServicioFinalizacion(String descServicioFinalizacion) {
        this.descServicioFinalizacion = descServicioFinalizacion;
    }

    /**
     * @return the codServicioFinalizacion
     */
    public String getCodServicioFinalizacion() {
        return codServicioFinalizacion;
    }

    /**
     * @param codServicioFinalizacion the codServicioFinalizacion to set
     */
    public void setCodServicioFinalizacion(String codServicioFinalizacion) {
        this.codServicioFinalizacion = codServicioFinalizacion;
    }

    /**
     * @return the implClassServicioFinalizacion
     */
    public String getImplClassServicioFinalizacion() {
        return implClassServicioFinalizacion;
    }

    /**
     * @param implClassServicioFinalizacion the implClassServicioFinalizacion to set
     */
    public void setImplClassServicioFinalizacion(String implClassServicioFinalizacion) {
        this.implClassServicioFinalizacion = implClassServicioFinalizacion;
    }

    /**
     * @return the listaValidacion
     */
    public Vector getListaValidacion() {
        return listaValidacion;
    }

    /**
     * @param listaValidacion the listaValidacion to set
     */
    public void setListaValidacion(Vector listaValidacion) {
        this.listaValidacion = listaValidacion;
    }

    /**
     * @return the listaOperacion
     */
    public Vector getListaOperacion() {
        return listaOperacion;
    }

    /**
     * @param listaOperacion the listaOperacion to set
     */
    public void setListaOperacion(Vector listaOperacion) {
        this.listaOperacion = listaOperacion;
    }

    public Vector getListaAgrupaciones() {
        return listaAgrupaciones;
    }

    public void setListaAgrupaciones(Vector listaAgrupaciones) {
        this.listaAgrupaciones = listaAgrupaciones;
    }

    public Vector getListaAgrupacionesActivas() {
        return listaAgrupacionesActivas;
    }

    public void setListaAgrupacionesActivas(Vector listaAgrupacionesActivas) {
        this.listaAgrupacionesActivas = listaAgrupacionesActivas;
    }

    public Vector getListaAgrupacionesCampo() {
        return listaAgrupacionesCampo;
    }

    public void setListaAgrupacionesCampo(Vector listaAgrupacionesCampo) {
        this.listaAgrupacionesCampo = listaAgrupacionesCampo;
    }

    public Vector getListaCodAgrupaciones() {
        return listaCodAgrupaciones;
    }

    public void setListaCodAgrupaciones(Vector listaCodAgrupaciones) {
        this.listaCodAgrupaciones = listaCodAgrupaciones;
    }

    public Vector getListaDescAgrupaciones() {
        return listaDescAgrupaciones;
    }
    public void setListaDescAgrupaciones(Vector listaDescAgrupaciones) {
        this.listaDescAgrupaciones = listaDescAgrupaciones;
    }

    public Vector getListaOrdenAgrupaciones() {
        return listaOrdenAgrupaciones;
    }
    public void setListaOrdenAgrupaciones(Vector listaOrdenAgrupaciones) {
        this.listaOrdenAgrupaciones = listaOrdenAgrupaciones;
    }

    public Vector getListaPosicionesX() {
        return listaPosicionesX;
    }
    public void setListaPosicionesX(Vector listaPosicionesX) {
        this.listaPosicionesX = listaPosicionesX;
    }

    public Vector getListaPosicionesY() {
        return listaPosicionesY;
    }
    public void setListaPosicionesY(Vector listaPosicionesY) {
        this.listaPosicionesY = listaPosicionesY;
    }

    /**
     * @return the InteresadoOblig
     */
    public String getInteresadoOblig() {
        return InteresadoOblig;
    }

    /**
     * @param InteresadoOblig the InteresadoOblig to set
     */
    public void setInteresadoOblig(String InteresadoOblig) {
        this.InteresadoOblig = InteresadoOblig;
    }

    public String getSoloWS() {
        return this.soloWS;
    }
    public void setSoloWS(String soloWS) {
        this.soloWS = soloWS;
    }

    public String getClaseBuzonEntradaHistorico() {
        return ClaseBuzonEntradaHistorico;
    }
  
    public void setClaseBuzonEntradaHistorico(String ClaseBuzonEntradaHistorico) {
        this.ClaseBuzonEntradaHistorico = ClaseBuzonEntradaHistorico;
    }    

    /**
     * @return the descClaseHist
     */
    public String getDescClaseHist() {
        return descClaseHist;
    }

    /**
     * @param descClaseHist the descClaseHist to set
     */
    public void setDescClaseHist(String descClaseHist) {
        this.descClaseHist = descClaseHist;
    }

    /**
     * @return the codClaseHist
     */
    public String getCodClaseHist() {
        return codClaseHist;
    }

    /**
     * @param codClaseHist the codClaseHist to set
     */
    public void setCodClaseHist(String codClaseHist) {
        this.codClaseHist = codClaseHist;
    }

    /**
     * @return the biblioteca
     */
    public String getBiblioteca() {
        return biblioteca;
    }

    /**
     * @param biblioteca the biblioteca to set
     */
    public void setBiblioteca(String biblioteca) {
        this.biblioteca = biblioteca;
    }

    /**
     * @return the propNumExpedientesAnoAsientoBuzon
     */
    public String getPropNumExpedientesAnoAsientoBuzon(){
        return propNumExpedientesAnoAsientoBuzon;
    }

    /**
     * @param propNumExpedientesAnoAsientoBuzon the propNumExpedientesAnoAsientoBuzon to set
     */
    public void setPropNumExpedientesAnoAsientoBuzon(String propNumExpedientesAnoAsientoBuzon) {
        this.propNumExpedientesAnoAsientoBuzon = propNumExpedientesAnoAsientoBuzon;
    }

    /**
     * @return the numeracionExpedientesAnoAsiento
     */
    public int getNumeracionExpedientesAnoAsiento() {
        return numeracionExpedientesAnoAsiento;
    }

    /**
     * @param numeracionExpedientesAnoAsiento the numeracionExpedientesAnoAsiento to set
     */
    public void setNumeracionExpedientesAnoAsiento(int numeracionExpedientesAnoAsiento) {
        this.numeracionExpedientesAnoAsiento = numeracionExpedientesAnoAsiento;
    }

    public String getProcedimientoDigit() {
        return procedimientoDigit;
    }

    public void setProcedimientoDigit(String procedimientoDigit) {
        this.procedimientoDigit = procedimientoDigit;
    }

    public boolean isIsValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public List<FirmaFlujoVO> getListaFlujosFirma() {
        return listaFlujosFirma;
    }

    public void setListaFlujosFirma(List<FirmaFlujoVO> listaFlujosFirma) {
        this.listaFlujosFirma = listaFlujosFirma;
    } 

    public Integer getErrorArea() {
        return errorArea;
    }

    public void setErrorArea(Integer errorArea) {
        this.errorArea = errorArea;
    }  
}