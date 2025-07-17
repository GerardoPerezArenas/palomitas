package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.TareasPendientesInicioTramiteVO;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Vector;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;


/** Clase utilizada para capturar o mostrar el estado de un RegistroEntrada */
public class TramitacionExpedientesForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(TramitacionExpedientesForm.class.getName());

    //Reutilizamos
    TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();

    private boolean hayTramPend;
    // Si es distinto de null o "", indica que el usuario tiene permisos para
    // realizar altas de anotaciones de salida desde el tramite.
    private String tienePermisoSalidas = null;
    private Vector<UORDTO> uorsDeRegistro = null;
    // Uors entre las que puede elegir un usuario para iniciar un tramite dado
    private Vector<UORDTO> uorsTramitacion = null;
    // Contiene la lista de trámites siguientes al trámite actual y cuyas condiciones de entrada no se cumplen
    private Hashtable<String,GeneralValueObject> listaTramitesCondEntradaNoCumplidas = new Hashtable<String,GeneralValueObject>();
    private String expSeleccionadosMismaLocalizacion;
    private boolean tieneTareasPendientesInicio = false;
    private ArrayList<TareasPendientesInicioTramiteVO> tareasPendientesInicioTramite = null;
  
    private HashMap mapa;
    private boolean hayInteresadosNotifAutorizada;

    public boolean isHayInteresadosNotifAutorizada() {
        return hayInteresadosNotifAutorizada;
    }

    public void setHayInteresadosNotifAutorizada(boolean hayInteresadosNotifAutorizada) {
        this.hayInteresadosNotifAutorizada = hayInteresadosNotifAutorizada;
    }
     
    
    
    public GeneralValueObject getListaEstadoFicheros(){
        return this.tramExpVO.getListaEstadoFicheros();
    }
    
    public void setListaEstadoFicheros(GeneralValueObject listaEstadoFicheros){
        this.tramExpVO.setListaEstadoFicheros(listaEstadoFicheros);
    }
            
    public GeneralValueObject getListaRutaFicherosDisco(){
        return this.tramExpVO.getListaRutaFicherosDisco();
    }
        
    public void setListaRutaFicherosDisco(GeneralValueObject listaRutaFicherosDisco){
        this.tramExpVO.setListaRutaFicherosDisco(listaRutaFicherosDisco);
    }
    
    

    public HashMap getMapa() {
        return mapa;
    }

    public void setMapa(HashMap mapa) {
        this.mapa = mapa;
    }

    public boolean tieneTareasPendientesInicio(){
        return this.tieneTareasPendientesInicio;
    }

    public void setTieneTareasPendientesInicio(boolean flag){
        this.tieneTareasPendientesInicio = flag;
    }

    public Hashtable<String,GeneralValueObject> getListaTramitesCondEntradaNoCumplidas()
    {
        return this.listaTramitesCondEntradaNoCumplidas;
    }

    public void setListaTramitesCondEntradaNoCumplidas(Hashtable<String,GeneralValueObject> lista){
        this.listaTramitesCondEntradaNoCumplidas=lista;
    }
    public void setDesdeExpediente(boolean desdeExpediente){
        this.tramExpVO.setDesdeFichaExpediente(desdeExpediente);
    }

    public boolean isDesdeFichaExpediente(){
        return this.tramExpVO.isDesdeFichaExpediente();
    }


    public void addTramiteCondEntradaNoCumplida(String codTramite,GeneralValueObject gvo){
        if (gvo == null) {
            this.listaTramitesCondEntradaNoCumplidas.remove(codTramite);
            return;
        }
        if (this.listaTramitesCondEntradaNoCumplidas.get(codTramite) != null) {
            GeneralValueObject existente = this.listaTramitesCondEntradaNoCumplidas.get(codTramite);
            Vector existentesCond = (Vector)existente.getAtributo("listaCondiciones");
            Vector nuevasCond = (Vector)gvo.getAtributo("listaCondiciones");
            for (Object nuevaCond: nuevasCond) {
                existentesCond.add(nuevaCond);
            }
            existente.setAtributo("listaCondiciones", existentesCond);
            this.listaTramitesCondEntradaNoCumplidas.put(codTramite, existente);
        } else {
            this.listaTramitesCondEntradaNoCumplidas.put(codTramite, gvo);
        }
    }

    public Vector<UORDTO> getUorsTramitacion() {
        return uorsTramitacion;
    }

    public void setUorsTramitacion(Vector<UORDTO> uorsTramitacion) {
        this.uorsTramitacion = uorsTramitacion;
    }

    public boolean isHayTramPend() {
        return hayTramPend;
    }

    public void setHayTramPend(boolean hayTramPend) {
        this.hayTramPend = hayTramPend;
    }
    public String getTienePermisoSalidas() {
        return tienePermisoSalidas;
    }

    public void setTienePermisoSalidas(String tienePermisoSalidas) {
        this.tienePermisoSalidas = tienePermisoSalidas;
    }

    public Vector<UORDTO> getUorsDeRegistro() {
        return uorsDeRegistro;
    }

    public void setUorsDeRegistro(Vector<UORDTO> uorsDeRegistro) {
        this.uorsDeRegistro = uorsDeRegistro;
    }
    
    public TramitacionExpedientesValueObject getTramitacionExpedientes() {
        return tramExpVO;
    }

    public void setTramitacionExpedientes(TramitacionExpedientesValueObject tramExpVO) {
        this.tramExpVO = tramExpVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

    public String getNumeroRelacion() {
      return tramExpVO.getNumeroRelacion();
    }
    public void setNumeroRelacion(String numeroRelacion) {
      tramExpVO.setNumeroRelacion(numeroRelacion);
    }

    public String getNumeroRelacionMostrar() {
      return tramExpVO.getNumeroRelacionMostrar();
    }
    public void setNumeroRelacionMostrar(String numeroRelacionMostrar) {
      tramExpVO.setNumeroRelacionMostrar(numeroRelacionMostrar);
    }

    public String getExpAsociadoIniciado() {
      return tramExpVO.getExpAsociadoIniciado();
    }
    public void setExpAsociadoIniciado(String expAsociadoIniciado) {
      tramExpVO.setExpAsociadoIniciado(expAsociadoIniciado);
    }

    public String getClasificacionTramite() {
      return tramExpVO.getClasificacionTramite();
    }
    public void setClasificacionTramite(String clasificacionTramite) {
      tramExpVO.setClasificacionTramite(clasificacionTramite);
    }
    public void setFechaFin(String fechaFin) {
      tramExpVO.setFechaFin(fechaFin);
    }
    public String getFechaFin() {
      return tramExpVO.getFechaFin();
    }
    public String getFechaFinPlazo() {
      return tramExpVO.getFechaFinPlazo();
    }
    public void setFechaFinPlazo(String fechaFinPlazo) {
      tramExpVO.setFechaFinPlazo(fechaFinPlazo);
    }
    public String getFechaInicio() {
      return tramExpVO.getFechaInicio();
    }
    public void setFechaInicio(String fechaInicio) {
      tramExpVO.setFechaInicio(fechaInicio);
    }
    public String getFechaInicioPlazo() {
      return tramExpVO.getFechaInicioPlazo();
    }
    public void setFechaInicioPlazo(String fechaInicioPlazo) {
      tramExpVO.setFechaInicioPlazo(fechaInicioPlazo);
    }
    public String getFechaLimite() {
      return tramExpVO.getFechaLimite();
    }
    public void setFechaLimite(String fechaLimite) {
      tramExpVO.setFechaLimite(fechaLimite);
    }
    public String getNumeroExpediente() {
      return tramExpVO.getNumeroExpediente();
    }
    public void setNumeroExpediente(String numeroExpediente) {
      tramExpVO.setNumeroExpediente(numeroExpediente);
    }
    public String getPlazo() {
      return tramExpVO.getPlazo();
    }
    public void setPlazo(String plazo) {
      tramExpVO.setPlazo(plazo);
    }
    public String getProcedimiento() {
      return tramExpVO.getProcedimiento();
    }
    public void setProcedimiento(String procedimiento) {
      tramExpVO.setProcedimiento(procedimiento);
    }
    public String getTipoPlazo() {
      return tramExpVO.getTipoPlazo();
    }
    public void setTipoPlazo(String tipoPlazo) {
      tramExpVO.setTipoPlazo(tipoPlazo);
    }
    public String getTitular() {
      return tramExpVO.getTitular();
    }
    public void setTitular(String titular) {
      tramExpVO.setTitular(titular);
    }
    public String getTramite() {
      return tramExpVO.getTramite();
    }
    public void setTramite(String tramite) {
      tramExpVO.setTramite(tramite);
    }
    public String getInstrucciones() {
      return tramExpVO.getInstrucciones();
    }
    public void setInstrucciones(String instrucciones) {
      tramExpVO.setInstrucciones(instrucciones);
    }
    public String getUnidadInicio() {
      return tramExpVO.getUnidadInicio();
    }
    public void setUnidadInicio(String unidadInicio) {
      tramExpVO.setUnidadInicio(unidadInicio);
    }
    public String getUnidadTramitadora() {
      return tramExpVO.getUnidadTramitadora();
    }
    public void setUnidadTramitadora(String unidadTramitadora) {
      tramExpVO.setUnidadTramitadora(unidadTramitadora);
    }
    public String getCodMunicipio() {
      return tramExpVO.getCodMunicipio();
    }
    public void setCodMunicipio(String codMunicipio) {
      tramExpVO.setCodMunicipio(codMunicipio);
    }
    public String getCodProcedimiento() {
      return tramExpVO.getCodProcedimiento();
    }
    public void setCodProcedimiento(String codProcedimiento) {
      tramExpVO.setCodProcedimiento(codProcedimiento);
    }
    public String getEjercicio() {
      return tramExpVO.getEjercicio();
    }
    public void setEjercicio(String ejercicio) {
      tramExpVO.setEjercicio(ejercicio);
    }
    public String getNumero() {
      return tramExpVO.getNumero();
    }
    public void setNumero(String numero) {
      tramExpVO.setNumero(numero);
    }
    public String getCodTramite() {
      return tramExpVO.getCodTramite();
    }
    public void setCodTramite(String numero) {
      tramExpVO.setCodTramite(numero);
    }
    public String getOcurrenciaTramite() {
      return tramExpVO.getOcurrenciaTramite();
    }
    public void setOcurrenciaTramite(String numero) {
      tramExpVO.setOcurrenciaTramite(numero);
    }

    public Vector getListaDocumentos() {
      return tramExpVO.getListaDocumentos();
    }
    public Vector getListaCodDocumentosTramite() {
      return tramExpVO.getListaCodDocumentosTramite();
    }

    public Vector getListaDocumentosTramite() {
      return tramExpVO.getListaDocumentosTramite();
    }

    public Collection getListaAjuntosPDFTramite() {
      return tramExpVO.getListaAjuntosPDFTramite();
    }

    public Collection getListaFormsPDF() {
      return tramExpVO.getListaFormsPDF();
    }

    public String getCodFormPDF(){
        return tramExpVO.getCodFormPDF();
    }

    public void setCodFormPDF(String codFormPDF) {
      tramExpVO.setCodFormPDF(codFormPDF);
    }

    public boolean getMostrarFormsPDF(){
        return tramExpVO.getMostrarFormsPDF();
    }

    public String getRespOpcion(){
      return tramExpVO.getRespOpcion();
    }
    public void setRespOpcion(String respuesta) {
      tramExpVO.setRespOpcion(respuesta);
    }

    public String getNotificacionRealizada(){
      return tramExpVO.getNotificacionRealizada();
    }
    public void setNotificacionRealizada(String notificacionRealizada) {
      tramExpVO.setNotificacionRealizada(notificacionRealizada);
    }


public String getAccion() {
return tramExpVO.getAccion();
}
public void setAccion(String accion) {
tramExpVO.setAccion(accion);
}
public String getAccionAfirmativa() {
return tramExpVO.getAccionAfirmativa();
}
public void setAccionAfirmativa(String accionAfirmativa) {
tramExpVO.setAccionAfirmativa(accionAfirmativa);
}
public String getAccionNegativa() {
return tramExpVO.getAccionNegativa();
}
public void setAccionNegativa(String accionNegativa) {
tramExpVO.setAccionNegativa(accionNegativa);
}
public String getCodigoTramiteFlujoSalida() {
return tramExpVO.getCodigoTramiteFlujoSalida();
}
public void setCodigoTramiteFlujoSalida(String codigoTramiteFlujoSalida) {
tramExpVO.setCodigoTramiteFlujoSalida(codigoTramiteFlujoSalida);
}
public String getCodigoVisibleTramiteFlujoSalida() {
return tramExpVO.getCodigoVisibleTramiteFlujoSalida();
}
public void setCodigoVisibleTramiteFlujoSalida(String codigoVisibleTramiteFlujoSalida) {
tramExpVO.setCodigoVisibleTramiteFlujoSalida(codigoVisibleTramiteFlujoSalida);
}
public String getDescripcionTramiteFlujoSalida() {
return tramExpVO.getDescripcionTramiteFlujoSalida();
}
public void setDescripcionTramiteFlujoSalida(String descripcionTramiteFlujoSalida) {
tramExpVO.setDescripcionTramiteFlujoSalida(descripcionTramiteFlujoSalida);
}
public Vector getListaTramitesFavorables() {
return tramExpVO.getListaTramitesFavorables();
}
public Vector getListaTramitesNoFavorables() {
return tramExpVO.getListaTramitesNoFavorables();
}
public String getNumeroSecuencia() {
return tramExpVO.getNumeroSecuencia();
}
public void setNumeroSecuencia(String numeroSecuencia) {
tramExpVO.setNumeroSecuencia(numeroSecuencia);
}
public String getObligat() {
return tramExpVO.getObligatorio();
}
public void setObligat(String obligatorio) {
tramExpVO.setObligatorio(obligatorio);
}
public String getObligatorioDesf() {
  return tramExpVO.getObligatorioDesf();
}
public void setObligatorioDesf(String obligatorioDesf) {
  tramExpVO.setObligatorioDesf(obligatorioDesf);
}
public String getPregunta() {
return tramExpVO.getPregunta();
}
public void setPregunta(String pregunta) {
tramExpVO.setPregunta(pregunta);
}
public String getPermiso() {
  return tramExpVO.getPermiso();
}
public void setPermiso(String permiso) {
  tramExpVO.setPermiso(permiso);
}

public String getBloqueo() {
  return tramExpVO.getBloqueo();
}
public void setBloqueo(String bloqueo) {
  tramExpVO.setBloqueo(bloqueo);
}

public String getLocalizacion() {
  return tramExpVO.getLocalizacion();
}
public void setLocalizacion(String localizacion) {
  tramExpVO.setLocalizacion(localizacion);
}
public String getPoseeLocalizacion() {
  return tramExpVO.getPoseeLocalizacion();
}
public void setPoseeLocalizacion(String poseeLocalizacion) {
  tramExpVO.setPoseeLocalizacion(poseeLocalizacion);
  }

public String getCodProvincia() {
  return tramExpVO.getCodProvincia();
}
public void setCodProvincia(String codProvincia) {
  tramExpVO.setCodProvincia(codProvincia);
}
public String getCodTVia() {
  return tramExpVO.getCodTVia();
}
public void setCodTVia(String codTVia) {
  tramExpVO.setCodTVia(codTVia);
}
public String getDescMunicipio() {
  return tramExpVO.getDescMunicipio();
}
public void setDescMunicipio(String descMunicipio) {
  tramExpVO.setDescMunicipio(descMunicipio);
}
public String getDescPostal() {
  return tramExpVO.getDescPostal();
}
public void setDescPostal(String descPostal) {
  tramExpVO.setDescPostal(descPostal);
}
public String getDescProvincia() {
  return tramExpVO.getDescProvincia();
}
public void setDescProvincia(String descProvincia) {
  tramExpVO.setDescProvincia(descProvincia);
}
public String getDescTVia() {
  return tramExpVO.getDescTVia();
}
public void setDescTVia(String descTVia) {
  tramExpVO.setDescTVia(descTVia);
}
public String getTxtBloque() {
  return tramExpVO.getTxtBloque();
}
public void setTxtBloque(String txtBloque) {
  tramExpVO.setTxtBloque(txtBloque);
}
public String getTxtDomicilio() {
  return tramExpVO.getTxtDomicilio();
}
public void setTxtDomicilio(String txtDomicilio) {
  tramExpVO.setTxtDomicilio(txtDomicilio);
}
public String getTxtEsc() {
  return tramExpVO.getTxtEsc();
}
public void setTxtEsc(String txtEsc) {
  tramExpVO.setTxtEsc(txtEsc);
}
public String getTxtLetraDesde() {
  return tramExpVO.getTxtLetraDesde();
}
public void setTxtLetraDesde(String txtLetraDesde) {
  tramExpVO.setTxtLetraDesde(txtLetraDesde);
}
public String getTxtLetraHasta() {
  return tramExpVO.getTxtLetraHasta();
}
public void setTxtLetraHasta(String txtLetraHasta) {
  tramExpVO.setTxtLetraHasta(txtLetraHasta);
}
public String getTxtNumDesde() {
  return tramExpVO.getTxtNumDesde();
}
public void setTxtNumDesde(String txtNumDesde) {
  tramExpVO.setTxtNumDesde(txtNumDesde);
}
public String getTxtNumHasta() {
  return tramExpVO.getTxtNumHasta();
}
public void setTxtNumHasta(String txtNumHasta) {
  tramExpVO.setTxtNumHasta(txtNumHasta);
}
public String getTxtPlta() {
  return tramExpVO.getTxtPlta();
}
public void setTxtPlta(String txtPlta) {
  tramExpVO.setTxtPlta(txtPlta);
}
public String getTxtPortal() {
  return tramExpVO.getTxtPortal();
}
public void setTxtPortal(String txtPortal) {
  tramExpVO.setTxtPortal(txtPortal);
}
public String getTxtPta() {
  return tramExpVO.getTxtPta();
}
public void setTxtPta(String txtPta) {
  tramExpVO.setTxtPta(txtPta);
}
public String getTxtRefCatastral() {
  return tramExpVO.getTxtRefCatastral();
}
public void setTxtRefCatastral(String txtRefCatastral) {
  tramExpVO.setTxtRefCatastral(txtRefCatastral);
}
public String getNumLineasPaginaListado() {
  return tramExpVO.getNumLineasPaginaListado();
}
public void setNumLineasPaginaListado(String numLineasPaginaListado) {
  tramExpVO.setNumLineasPaginaListado(numLineasPaginaListado);
}
public String getPaginaListado() {
  return tramExpVO.getPaginaListado();
}
public void setPaginaListado(String paginaListado) {
  tramExpVO.setPaginaListado(paginaListado);
}
public String getIdDomicilio() {
  return tramExpVO.getIdDomicilio();
}
public void setIdDomicilio(String idDomicilio) {
  tramExpVO.setIdDomicilio(idDomicilio);
}

public Vector getListaTramitesPendientes() {
  return tramExpVO.getListaTramitesPendientes();
}
public void setListaTramitesPendientes(Vector listaTramitesPendientes) {
  tramExpVO.setListaTramitesPendientes(listaTramitesPendientes);
}

public String getModoConsulta() {
  return tramExpVO.getModoConsulta();
}
public void setModoConsulta(String modoConsulta) {
  tramExpVO.setModoConsulta(modoConsulta);
}
public String getExpRelacionado() {
  return tramExpVO.getExpRelacionado();
}
public void setExpRelacionado(String expRelacionado) {
  tramExpVO.setExpRelacionado(expRelacionado);
}

public String getCodMunicipioIni() {
  return tramExpVO.getCodMunicipioIni();
}
public void setCodMunicipioIni(String codMunicipioIni) {
  tramExpVO.setCodMunicipioIni(codMunicipioIni);
}
public String getEjercicioIni() {
  return tramExpVO.getEjercicioIni();
}
public void setEjercicioIni(String ejercicioIni) {
  tramExpVO.setEjercicioIni(ejercicioIni);
}
public String getNumeroIni() {
  return tramExpVO.getNumeroIni();
}
public void setNumeroIni(String numeroIni) {
  tramExpVO.setNumeroIni(numeroIni);
}

public String getCodLocalizacion() {
  return tramExpVO.getCodLocalizacion();
}
public void setCodLocalizacion(String codLocalizacion) {
  tramExpVO.setCodLocalizacion(codLocalizacion);
}

public String getObservaciones() {
  return tramExpVO.getObservaciones();
}
public void setObservaciones(String observaciones) {
  tramExpVO.setObservaciones(observaciones);
}

public String getCodDocumento() {
  return tramExpVO.getCodDocumento();
}
public void setCodDocumento(String codDocumento) {
  tramExpVO.setCodDocumento(codDocumento);
}
public String getCodPlantilla() {
  return tramExpVO.getCodPlantilla();
}
public void setCodPlantilla(String codPlantilla) {
  tramExpVO.setCodPlantilla(codPlantilla);
}
public String getTipoPlantilla() {
  return tramExpVO.getTipoPlantilla();
}
public void setTipoPlantilla(String tipoPlantilla) {
  tramExpVO.setTipoPlantilla(tipoPlantilla);
}
public String getEditorTexto() {
  return tramExpVO.getEditorTexto();
}
public void setEditorTexto(String editorTexto) {
  tramExpVO.setEditorTexto(editorTexto);
}
public String getCodUsuario() {
  return tramExpVO.getCodUsuario();
}
public void setCodUsuario(String codUsuario) {
  tramExpVO.setCodUsuario(codUsuario);
}

public Vector getListaEnlaces() {
  return tramExpVO.getListaEnlaces();
}

public Vector getEstructuraDatosSuplementarios() {
  return tramExpVO.getEstructuraDatosSuplementarios();
}


public void setEstructuraDatosSuplementarios(Vector estructuraDatosSuplementarios) {
        tramExpVO.setEstructuraDatosSuplementarios(estructuraDatosSuplementarios);
    }



public Vector getValoresDatosSuplementarios() {
  return tramExpVO.getValoresDatosSuplementarios();
}

public void setValoresDatosSuplementarios(Vector valoresDatosSuplementarios) {
    tramExpVO.setValoresDatosSuplementarios(valoresDatosSuplementarios);
}

public Vector getEstructuraDatosSuplExpediente() {
    return tramExpVO.getEstructuraDatosSuplExpediente();
}

public void setEstructuraDatosSuplExpediente(Vector estructuraDatosSuplExpediente) {
    tramExpVO.setEstructuraDatosSuplExpediente(estructuraDatosSuplExpediente);
}

public Vector getValoresDatosSuplExpediente() {
  return tramExpVO.getValoresDatosSuplExpediente();
}

public void setValoresDatosSuplExpediente(Vector valoresDatosSuplExpediente) {
    tramExpVO.setValoresDatosSuplExpediente(valoresDatosSuplExpediente);
}

public Vector getEstructuraDatosSuplTramites() {
    return tramExpVO.getEstructuraDatosSuplTramites();
}

public void setEstructuraDatosSuplTramites(Vector estructuraDatosSuplTramites) {
    tramExpVO.setEstructuraDatosSuplTramites(estructuraDatosSuplTramites);
}

public Vector getValoresDatosSuplTramites() {
  return tramExpVO.getValoresDatosSuplTramites();
}

public void setValoresDatosSuplTramites(Vector valoresDatosSuplTramites) {
    tramExpVO.setValoresDatosSuplTramites(valoresDatosSuplTramites);
    
}
    public Vector getListaExpedientesNoInteresados() {
      return tramExpVO.getListaExpedientesNoInteresados();
    }
    public void setListaExpedientesNoInteresados(Vector listaExpedientesNoInteresados) {
      tramExpVO.setListaExpedientesNoInteresados(listaExpedientesNoInteresados);
    }

public Vector getListaInteresados() {
  return tramExpVO.getListaInteresados();
}
public Vector getListaRoles() {
  return tramExpVO.getListaRoles();
}
public String getInteresado() {
  return tramExpVO.getInteresado();
}

public String getListaCodInteresados() {
  return tramExpVO.getListaCodInteresados();
}

public Vector getVectorCodInteresados() {
 return tramExpVO.getVectorCodInteresados();
}

public String getListaVersInteresados() {
  return tramExpVO.getListaVersInteresados();	
}
public void setListaCodInteresados(String listaCodInteresados) {
  tramExpVO.setListaCodInteresados(listaCodInteresados);
}
public void setVectorCodInteresados(Vector vectorCodInteresados) {
  tramExpVO.setVectorCodInteresados(vectorCodInteresados);
}
public void setListaVersInteresados(String listaVersInteresados) {
  tramExpVO.setListaVersInteresados(listaVersInteresados);
}

public String getDesdeInformesGestion() {
  return tramExpVO.getDesdeInformesGestion();	
}
public void setDesdeInformesGestion(String desdeInformesGestion) {
  tramExpVO.setDesdeInformesGestion(desdeInformesGestion);	
}
public String getTodos() {
  return tramExpVO.getTodos();	
}
public void setTodos(String todos) {
  tramExpVO.setTodos(todos);	
}


public GeneralValueObject getListaFicheros(){
    return tramExpVO.getListaFicheros();
}
public void setListaFicheros(GeneralValueObject listaFicheros){
    tramExpVO.setListaFicheros(listaFicheros);
}

public GeneralValueObject getListaNombresFicheros(){
    return tramExpVO.getListaNombresFicheros();
}

public void setListaNombresFicheros(GeneralValueObject listaNombresFicheros){
    tramExpVO.setListaNombresFicheros(listaNombresFicheros);
}

public GeneralValueObject getListaTiposFicheros(){
    return tramExpVO.getListaTiposFicheros();
}
public void setListaTiposFicheros(GeneralValueObject listaTiposFicheros){
    tramExpVO.setListaTiposFicheros(listaTiposFicheros);
}

public GeneralValueObject getListaMetadatosFicheros(){
    return tramExpVO.getListaMetadatosFicheros();
}

public void setListaMetadatosFicheros(GeneralValueObject listaMetadatosFicheros){
    tramExpVO.setListaMetadatosFicheros(listaMetadatosFicheros);
}

public String getEstadoFirma() {
    return tramExpVO.getEstadoFirma();
}
public void setEstadoFirma(String newValue) {
    tramExpVO.setEstadoFirma(newValue);
}
    public String getResultadoFinalizar() {
        return tramExpVO.getResultadoFinalizar();
    }
    public void setResultadoFinalizar(String resultadoFinalizar) {
        tramExpVO.setResultadoFinalizar(resultadoFinalizar);
    }

	public String getMensajeSW() {
		return tramExpVO.getMensajeSW();
	}

	public void setMensajeSW(String mensaje) {
		tramExpVO.setMensajeSW(mensaje);
	}

        
 public String getNombreUsuario(){
     return tramExpVO.getNombreUsuario();
 }
 
 public void setNombreUsuario(String nombre){
     this.tramExpVO.setNombreUsuario(nombre);
 }

 public String getCodUnidadOrganicaExp(){
     return tramExpVO.getCodUnidadOrganicaExp();
 }
 
 public void setJustificacion(String justificacion){
     this.tramExpVO.setJustificacion(justificacion);
 }

 public String getJustificacion(){
     return tramExpVO.getJustificacion();
 }

 public void setPersonaAutoriza(String personaAutoriza){
     this.tramExpVO.setPersonaAutoriza(personaAutoriza);
 }

 public String getPersonaAutoriza(){
     return tramExpVO.getPersonaAutoriza();
 }
 public void setDesdeConsulta(String desdeConsulta){
     this.tramExpVO.setDesdeConsulta(desdeConsulta);
 }

 public String getDesdeConsulta(){
     return tramExpVO.getDesdeConsulta();
 }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            tramExpVO.validate(idioma);
        } catch (ValidationException ve) {
          //Hay errores...
          //Tenemos que traducirlos a formato struts
          errors=validationException(ve,errors);
        }
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

    public boolean isBloquearPlazos() {
        return tramExpVO.isBloquearPlazos();
    }
    
    public void setBloquearPlazos(boolean bloquearPlazos) {
        tramExpVO.setBloquearPlazos(bloquearPlazos);
    }
    
    public String getProcedimientoAsociado() {
        return tramExpVO.getProcedimientoAsociado();
    }

    public void setProcedimientoAsociado(String procedimientoAsociado) {
        tramExpVO.setProcedimientoAsociado(procedimientoAsociado);
    }
    
    public String getCodUnidadTramitadoraManual() {
        return tramExpVO.getCodUnidadTramitadoraManual();
    }
    
    public void setCodUnidadTramitadoraManual(String codUnidadTramitadoraManual) {
        tramExpVO.setCodUnidadTramitadoraManual(codUnidadTramitadoraManual);
    }


     public String getAdmiteNotificacionElectronica() {
        return tramExpVO.getAdmiteNotificacionElectronica();
    }

    public void setAdmiteNotificacionElectronica(String admiteNotificacionElectronica) {
        tramExpVO.setAdmiteNotificacionElectronica(admiteNotificacionElectronica);
    }
    
    public String getNotificacionObligatoria() {
        return tramExpVO.getNotificacionObligatoria();
    }

    public void setNotificacionObligatoria(String notificacionObligatoria) {
        tramExpVO.setNotificacionObligatoria(notificacionObligatoria);
    }
    
    /**
     * @return the expSeleccionadosMismaLocalizacion
     */
    public String getExpSeleccionadosMismaLocalizacion() {
        return expSeleccionadosMismaLocalizacion;
    }

    /**
     * @param expSeleccionadosMismaLocalizacion the expSeleccionadosMismaLocalizacion to set
     */
    public void setExpSeleccionadosMismaLocalizacion(String expSeleccionadosMismaLocalizacion) {
        this.expSeleccionadosMismaLocalizacion = expSeleccionadosMismaLocalizacion;
    }

    public ArrayList<ModuloIntegracionExterno> getModulosExternos(){
        return tramExpVO.getModulosExternos();
    }

    public void setModulosExternos(ArrayList<ModuloIntegracionExterno> modulos){
        tramExpVO.setModulosExternos(modulos);
    }

    public ArrayList<String> getFuncionesJavascriptModulosExternos(){
        return tramExpVO.getFuncionesJavascriptModulosExternos();
    }

    public void setFuncionesJavascriptModulosExternos(ArrayList<String> funciones){
        this.tramExpVO.setFuncionesJavascriptModulosExternos(funciones);
    }

    /**
     * @return the tareasPendientesInicioTramite
     */
    public ArrayList<TareasPendientesInicioTramiteVO> getTareasPendientesInicioTramite() {
        return tareasPendientesInicioTramite;
    }

    /**
     * @param tareasPendientesInicioTramite the tareasPendientesInicioTramite to set
     */
    public void setTareasPendientesInicioTramite(ArrayList<TareasPendientesInicioTramiteVO> tareasPendientesInicioTramite) {
        this.tareasPendientesInicioTramite = tareasPendientesInicioTramite;
    }
    
    
     public ArrayList<String> getFuncionesJSModulosExternosAccederPantallaTramite(){
        return tramExpVO.getFuncionesJSModulosExternosAccederPantallaTramite();
    }

    public void setFuncionesJSModulosExternosAccederPantallaTramite(ArrayList<String> funciones){
        this.tramExpVO.setFuncionesJSModulosExternosAccederPantallaTramite(funciones);
    }
    public Vector getListaExpresionCalculada() {
        return tramExpVO.getExpresiones_calculadas();
    }
    
    public Vector getListaAgrupacionesCampos() {
        return tramExpVO.getListaAgrupaciones();
    }
    public void setListaAgrupacionesCampos(Vector listaAgrupacionesCampos) {
        this.tramExpVO.setListaAgrupaciones(listaAgrupacionesCampos);
    }
    
    private Boolean cargarVista = false;
    public Boolean getCargarVista() {
        return cargarVista;
    }
    public void setCargarVista(Boolean cargarVista) {
        this.cargarVista = cargarVista;
    }
    
    public boolean isInteresadoObligatorio(){
        return this.tramExpVO.isInteresadoObligatorio();
    }
    
    public void setInteresadoObligatorio(boolean valor){
        this.tramExpVO.setInteresadoObligatorio(valor);
    }
    
    
    /**
     * Devuelve el objeto que almacen las longitudes de los ficheros asociados a 
     * campos suplementarios de tipo fichero definidos a nivel de trámite
     * @return Objeto de tipo GeneralValueObject
     */
    public GeneralValueObject getListaLongitudFicheros(){
        return this.tramExpVO.getListaLongitudFicheros();
    }
    
    /**
     * Establece un objeto que almacena la longitud de los ficheros de campos de tipo fichero de un trámite
     * @param gVO: Objeto de tipo GeneralValueObject que almacena la longitud de los ficheros de un trámite
     */
    public void setListaLongitudFicheros(GeneralValueObject gVO){
        this.tramExpVO.setListaLongitudFicheros(gVO);
    }
    
    
    public boolean isExpHistorico() {
        return this.tramExpVO.isExpHistorico();
    }

    public void setExpHistorico(boolean expHistorico) {
        this.tramExpVO.setExpHistorico(expHistorico);
    }
    
    
}