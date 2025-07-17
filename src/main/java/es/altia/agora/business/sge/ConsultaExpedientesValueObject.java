package es.altia.agora.business.sge;

import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraCampoModuloIntegracionVO;
import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Hashtable;
import java.util.HashMap;

public class ConsultaExpedientesValueObject implements Serializable, ValueObject {

    /** Construye un nuevo ConsultaExpedientes por defecto. */
    public ConsultaExpedientesValueObject() {
        super();
    }

public String getCodProcedimiento() {
  return codProcedimiento;
}
public void setCodProcedimiento(String codProcedimiento) {
  this.codProcedimiento = codProcedimiento;
}
public String getDescProcedimiento() {
  return descProcedimiento;
}
public void setDescProcedimiento(String descProcedimiento) {
  this.descProcedimiento = descProcedimiento;
}
public String getFechaInicio() {
  return fechaInicio;
}
public void setFechaInicio(String fechaInicio) {
  this.fechaInicio = fechaInicio;
}
public String getNumeroExpediente() {
  return numeroExpediente;
}
public void setNumeroExpediente(String numeroExpediente) {
  this.numeroExpediente = numeroExpediente;
}
public String getTitular() {
  return titular;
}
public void setTitular(String titular) {
  this.titular = titular;
}
public String getDomicilio() {
  return domicilio;
}
public void setDomicilio(String domicilio) {
  this.domicilio = domicilio;
}
public String getTercero() {
  return tercero;
}
public void setTercero(String tercero) {
  this.tercero = tercero;
}
public String getVersionTercero() {
  return versionTercero;
}
public void setVersionTercero(String versionTercero) {
  this.versionTercero = versionTercero;
}

public Vector getListaProcedimientos() {
  return listaProcedimientos;
}
public void setListaProcedimientos(Vector listaProcedimientos) {
  this.listaProcedimientos = listaProcedimientos;
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
public String getCodMunicipio() {
  return codMunicipio;
}
public void setCodMunicipio(String codMunicipio) {
  this.codMunicipio = codMunicipio;
}
public String getEjercicio() {
  return ejercicio;
}
public void setEjercicio(String ejercicio) {
  this.ejercicio = ejercicio;
}
public String getFechaFin() {
  return fechaFin;
}
public void setFechaFin(String fechaFin) {
  this.fechaFin = fechaFin;
}

public Vector getListaExpedientesRel() {
  return listaExpedientesRel;
}
public void setListaExpedientesRel(Vector listaExpedientesRel) {
  this.listaExpedientesRel = listaExpedientesRel;
}

public String getExpRelacionado() {
  return expRelacionado;
}
public void setExpRelacionado(String expRelacionado) {
  this.expRelacionado = expRelacionado;
}
public String getCodMunicipioRel() {
  return codMunicipioRel;
}
public void setCodMunicipioRel(String codMunicipioRel) {
  this.codMunicipioRel = codMunicipioRel;
}
public String getEjercicioRel() {
  return ejercicioRel;
}
public void setEjercicioRel(String ejercicioRel) {
  this.ejercicioRel = ejercicioRel;
}
public String getNumeroExpedienteRel() {
  return numeroExpedienteRel;
}
public void setNumeroExpedienteRel(String numeroExpedienteRel) {
  this.numeroExpedienteRel = numeroExpedienteRel;
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

public String getEjercicioExpediente() {
    return ejercicioExpediente;
}

public void setEjercicioExpediente(String ejercicioExpediente) {
    this.ejercicioExpediente = ejercicioExpediente;
}


public String getNumeroExpedienteIni() {
  return numeroExpedienteIni;
}
public void setNumeroExpedienteIni(String numeroExpedienteIni) {
  this.numeroExpedienteIni = numeroExpedienteIni;
}

public String getCodArea() {
  return codArea;
}
public void setCodArea(String codArea) {
  this.codArea = codArea;
}
public String getCodTramite() {
  return codTramite;
}
public void setCodTramite(String codTramite) {
  this.codTramite = codTramite;
}
public String getCodUnidadTram() {
  return codUnidadTram;
}
public void setCodUnidadTram(String codUnidadTram) {
  this.codUnidadTram = codUnidadTram;
}
public String getCodTipoProced() {
  return codTipoProced;
}
public void setCodTipoProced(String codTipoProced) {
  this.codTipoProced = codTipoProced;
}

public String getRespOpcion() {
  return respOpcion;
}
public void setRespOpcion(String respOpcion) {
  this.respOpcion = respOpcion;
}

public String getModoConsulta() {
  return modoConsulta;
}
public void setModoConsulta(String modoConsulta) {
  this.modoConsulta = modoConsulta;
}

public String getEstado() {
  return estado;
}
public void setEstado(String estado) {
  this.estado = estado;
}
public String getLocalizacion() {
  return localizacion;
}
public void setLocalizacion(String localizacion) {
  this.localizacion = localizacion;
}
public String getCodClasifTramite() {
  return codClasifTramite;
}
public void setCodClasifTramite(String codClasifTramite) {
  this.codClasifTramite = codClasifTramite;
}
public String getDescClasifTramite() {
  return descClasifTramite;
}
public void setDescClasifTramite(String descClasifTramite) {
  this.descClasifTramite = descClasifTramite;
}
public Vector getListaClasifTramite() {
  return listaClasifTramite;
}
public void setListaClasifTramite(Vector listaClasifTramite) {
  this.listaClasifTramite = listaClasifTramite;
}
public String getDeAdjuntar() {
  return deAdjuntar;
}
public void setDeAdjuntar(String deAdjuntar) {
  this.deAdjuntar = deAdjuntar;
}

public String getTipo() {
  return tipo;
}
public void setTipo(String tipo) {
  this.tipo = tipo;
}

public String getTiempo() {
  return tiempo;
}
public void setTiempo(String tiempo) {
  this.tiempo = tiempo;
}

public String getRefCatastral() {
    return refCatastral;
}

public void setRefCatastral(String refCatastral) {
    this.refCatastral = refCatastral;
}

public Hashtable getCriterios(){
    return this.criterios;
}
public void setCriterios(Hashtable hs){
    this.criterios = hs;
}

public HashMap getCamposSuplementarios(){
    return this.camposSuplementarios;
}
public void setCamposSuplementarios(HashMap hm){
    this.camposSuplementarios = hm;
}

//MAI
public ArrayList<EstructuraCampoModuloIntegracionVO> getCamposConsultaModuloExterno(){
    return this.camposConsultaModuloExterno;
}
public void setCamposConsultaModuloExterno(ArrayList<EstructuraCampoModuloIntegracionVO> camposConsultaModuloExterno){
    this.camposConsultaModuloExterno = camposConsultaModuloExterno;
}

//mAI
public String getNumeroAnotacion() {
    return this.numeroAnotacion;
}

public void setNumeroAnotacion(String numeroAnotacion) {
    this.numeroAnotacion = numeroAnotacion;
}

public String getEjercicioAnotacion() {
    return this.ejercicioAnotacion;
}

public void setEjercicioAnotacion(String ejercicioAnotacion) {
    this.ejercicioAnotacion = ejercicioAnotacion;
}

public String getCodTipoAnotacion() {
    return this.codTipoAnotacion;
}

public void setCodTipoAnotacion(String codTipoAnotacion) {
    this.codTipoAnotacion = codTipoAnotacion;
}

public String getDescTipoAnotacion() {
    return this.descTipoAnotacion;
}

public void setDescTipoAnotacion(String descTipoAnotacion) {
    this.descTipoAnotacion = descTipoAnotacion;
}

    public boolean isVerTramitesAbiertos() {
        return verTramitesAbiertos;
    }

    public void setVerTramitesAbiertos(boolean verTramitesAbiertos) {
        this.verTramitesAbiertos = verTramitesAbiertos;
    }

    //para recuperar los campos que aparecn en el listado
     public Vector getCamposListados() {
        return camposListados;
    }

    public void setCamposListados(Vector newLista) {
        camposListados = newLista;
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
    public String getTipoInicio() {
        return TipoInicio;
    }
    public void setTipoInicio(String T_Inicio) {
        this.TipoInicio = T_Inicio;
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
    private String ejercicioExpediente;
    private String numeroExpediente;
    private String codProcedimiento;
    private String descProcedimiento;
    private String fechaInicio;
    private String fechaFin;
    private String titular;
    private String tercero;
    private String versionTercero;
    private String tipoDocumentoTercero;
    private String documentoTercero;
    private String domicilio;
    private String estado;
    private String localizacion;
    private String codClasifTramite;
    private String descClasifTramite;
    private String codArea;
    private String codTramite;
    private String codUnidadTram;
    private String codTipoProced;
    private String tipo;
    private String tiempo;
    private Vector listaProcedimientos;
    private Vector listaClasifTramite;
    private String paginaListado;
    private String numLineasPaginaListado;
    private String codMunicipio;
    private String ejercicio;
    private Vector listaExpedientesRel;
    private String codMunicipioRel;
    private String ejercicioRel;
    private String numeroExpedienteRel;
    private String expRelacionado;
    private String codMunicipioIni;
    private String ejercicioIni;
    private String numeroExpedienteIni;
    private String respOpcion;
    private String modoConsulta;
    private String deAdjuntar;
    private String refCatastral;
    private Hashtable criterios;
    private HashMap camposSuplementarios;
    private String codTipoAnotacion;
    private String descTipoAnotacion;
    private String    ejercicioAnotacion;
    private String    numeroAnotacion;
    private Vector camposListados;
    private String asunto;
    private String observaciones;
    private boolean verTramitesAbiertos = true;
    private boolean desdeInformesGestion = false;
    private boolean imprimiendo = false;
    private String TipoInicio;
    private ArrayList<EstructuraCampoModuloIntegracionVO> camposConsultaModuloExterno;
    
    private String fechaInicioTramiteBusquedaDesdeInformesGestion;
    private String fechaFinTramiteBusquedaDesdeInformesGestion;
    //  1 --> Tareas pendientes, 2--> Tareas finalizadas, 3 --> Todas
    private int estadoTramiteBusquedaDesdeInformesGestion;
    //Mai: tarea filtrar por Rol
    private String codigoRol;
    private String descripcionRol;
    private boolean reaperturaExpediente;
    
    private String tipoBusqueda;
    private boolean expHistorico;
    //Para indicar si el expediente se ha iniciado desde registro telemático
    private String busqTelematicos;
    private boolean deRegTelematico;

    private String usuario;
    private String nombreUsuario;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
            
            
    public boolean isExpHistorico() {
        return expHistorico;
    }

    public void setExpHistorico(boolean expHistorico) {
        this.expHistorico = expHistorico;
    }

    public String getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }
    
    
   
    
    
    public String getFechaInicioTramiteBusquedaDesdeInformesGestion(){
        return this.fechaInicioTramiteBusquedaDesdeInformesGestion;
    }
    
    public void setFechaInicioTramiteBusquedaDesdeInformesGestion(String fecha){
        this.fechaInicioTramiteBusquedaDesdeInformesGestion = fecha;
    }
    
    public String getFechaFinTramiteBusquedaDesdeInformesGestion(){
        return this.fechaFinTramiteBusquedaDesdeInformesGestion;
    }
    
    public void setFechaFinTramiteBusquedaDesdeInformesGestion(String fecha){
        this.fechaFinTramiteBusquedaDesdeInformesGestion = fecha;
    }
    
    
    public void setEstadoTramiteBusquedaDesdeInformesGestion(int flag){
        this.estadoTramiteBusquedaDesdeInformesGestion = flag;
    }
    
    public int getEstadoTramiteBusquedaDesdeInformesGestion(){
        return this.estadoTramiteBusquedaDesdeInformesGestion;
    }
    

    /**
     * @return the desdeInformesGestion
     */
    public boolean isDesdeInformesGestion() {
        return desdeInformesGestion;
    }

    /**
     * @param desdeInformesGestion the desdeInformesGestion to set
     */
    public void setDesdeInformesGestion(boolean desdeInformesGestion) {
        this.desdeInformesGestion = desdeInformesGestion;
    }

    public boolean isImprimiendo() {
        return imprimiendo;
    }

    public void setImprimiendo(boolean imprimiendo) {
        this.imprimiendo = imprimiendo;
    }

  	public String getCodigoRol(){
      return this.codigoRol;
    }
    
    public void setCodigoRol(String codRol){
      this.codigoRol=codRol;
      
    }
    
    public String getDescripcionRol(){
      return this.descripcionRol;
    }
    
    public void setDescripcionRol(String descripcionRol){
      this.descripcionRol=descripcionRol;
      
    }


    /**
     * @return the tipoDocumentoTercero
     */
    public String getTipoDocumentoTercero() {
        return tipoDocumentoTercero;
    }

    /**
     * @param tipoDocumentoTercero the tipoDocumentoTercero to set
     */
    public void setTipoDocumentoTercero(String tipoDocumentoTercero) {
        this.tipoDocumentoTercero = tipoDocumentoTercero;
    }

    /**
     * @return the documentoTercero
     */
    public String getDocumentoTercero() {
        return documentoTercero;
    }

    /**
     * @param documentoTercero the documentoTercero to set
     */
    public void setDocumentoTercero(String documentoTercero) {
        this.documentoTercero = documentoTercero;
    }

    public boolean isReaperturaExpediente() {
        return reaperturaExpediente;
    }

    public void setReaperturaExpediente(boolean reaperturaExpediente) {
        this.reaperturaExpediente = reaperturaExpediente;
    }

    /**
     * @return the busqTelematicos
     */
    public String getBusqTelematicos() {
        return busqTelematicos;
    }

    /**
     * @param busqTelematicos the busqTelematicos to set
     */
    public void setBusqTelematicos(String busqTelematicos) {
        this.busqTelematicos = busqTelematicos;
    }

    /**
     * @return the deRegTelematico
     */
    public boolean isDeRegTelematico() {
        return deRegTelematico;
    }

    /**
     * @param deRegTelematico the deRegTelematico to set
     */
    public void setDeRegTelematico(boolean deRegTelematico) {
        this.deRegTelematico = deRegTelematico;
    }




}
