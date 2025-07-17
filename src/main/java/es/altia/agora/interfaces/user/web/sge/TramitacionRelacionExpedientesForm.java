package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.TramitacionRelacionExpedientesValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/** Clase utilizada para capturar o mostrar el estado de un RegistroEntrada */
public class TramitacionRelacionExpedientesForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(TramitacionRelacionExpedientesForm.class.getName());

    //Reutilizamos
    TramitacionRelacionExpedientesValueObject tramRelExpVO = new TramitacionRelacionExpedientesValueObject();

    public TramitacionRelacionExpedientesValueObject getTramitacionExpedientes() {
        return tramRelExpVO;
    }

    public void setTramitacionExpedientes(TramitacionRelacionExpedientesValueObject tramRelExpVO) {
        this.tramRelExpVO = tramRelExpVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

    public String getOpcionGrabar() {
      return tramRelExpVO.getOpcionGrabar();
    }
    public void setOpcionGrabar(String opcionGrabar) {
      tramRelExpVO.setOpcionGrabar(opcionGrabar);
    }

    public String getNumeroRelacion() {
      return tramRelExpVO.getNumeroRelacion();
    }
    public void setNumeroRelacion(String numeroRelacion) {
      tramRelExpVO.setNumeroRelacion(numeroRelacion);
    }

    public String getNumeroRelacionMostrar() {
      return tramRelExpVO.getNumeroRelacionMostrar();
    }
    public void setNumeroRelacionMostrar(String numeroRelacionMostrar) {
      tramRelExpVO.setNumeroRelacionMostrar(numeroRelacionMostrar);
    }
    public String getClasificacionTramite() {
      return tramRelExpVO.getClasificacionTramite();
    }
    public void setClasificacionTramite(String clasificacionTramite) {
      tramRelExpVO.setClasificacionTramite(clasificacionTramite);
    }
    public void setFechaFin(String fechaFin) {
      tramRelExpVO.setFechaFin(fechaFin);
    }
    public String getFechaFin() {
      return tramRelExpVO.getFechaFin();
    }
    public String getFechaFinPlazo() {
      return tramRelExpVO.getFechaFinPlazo();
    }
    public void setFechaFinPlazo(String fechaFinPlazo) {
      tramRelExpVO.setFechaFinPlazo(fechaFinPlazo);
    }
    public String getFechaInicio() {
      return tramRelExpVO.getFechaInicio();
    }
    public void setFechaInicio(String fechaInicio) {
      tramRelExpVO.setFechaInicio(fechaInicio);
    }
    public String getFechaInicioPlazo() {
      return tramRelExpVO.getFechaInicioPlazo();
    }
    public void setFechaInicioPlazo(String fechaInicioPlazo) {
      tramRelExpVO.setFechaInicioPlazo(fechaInicioPlazo);
    }
    public String getFechaLimite() {
      return tramRelExpVO.getFechaLimite();
    }
    public void setFechaLimite(String fechaLimite) {
      tramRelExpVO.setFechaLimite(fechaLimite);
    }
    public String getNumeroExpediente() {
      return tramRelExpVO.getNumeroExpediente();
    }
    public void setNumeroExpediente(String numeroExpediente) {
      tramRelExpVO.setNumeroExpediente(numeroExpediente);
    }
    public String getPlazo() {
      return tramRelExpVO.getPlazo();
    }
    public void setPlazo(String plazo) {
      tramRelExpVO.setPlazo(plazo);
    }
    public String getProcedimiento() {
      return tramRelExpVO.getProcedimiento();
    }
    public void setProcedimiento(String procedimiento) {
      tramRelExpVO.setProcedimiento(procedimiento);
    }
    public String getTipoPlazo() {
      return tramRelExpVO.getTipoPlazo();
    }
    public void setTipoPlazo(String tipoPlazo) {
      tramRelExpVO.setTipoPlazo(tipoPlazo);
    }
    public String getTitular() {
      return tramRelExpVO.getTitular();
    }
    public void setTitular(String titular) {
      tramRelExpVO.setTitular(titular);
    }
    public String getTramite() {
      return tramRelExpVO.getTramite();
    }
    public void setTramite(String tramite) {
      tramRelExpVO.setTramite(tramite);
    }
    public String getInstrucciones() {
      return tramRelExpVO.getInstrucciones();
    }
    public void setInstrucciones(String instrucciones) {
      tramRelExpVO.setInstrucciones(instrucciones);
    }
    public String getUnidadInicio() {
      return tramRelExpVO.getUnidadInicio();
    }
    public void setUnidadInicio(String unidadInicio) {
      tramRelExpVO.setUnidadInicio(unidadInicio);
    }
    public String getUnidadTramitadora() {
      return tramRelExpVO.getUnidadTramitadora();
    }
    public void setUnidadTramitadora(String unidadTramitadora) {
      tramRelExpVO.setUnidadTramitadora(unidadTramitadora);
    }
    public String getCodMunicipio() {
      return tramRelExpVO.getCodMunicipio();
    }
    public void setCodMunicipio(String codMunicipio) {
      tramRelExpVO.setCodMunicipio(codMunicipio);
    }
    public String getCodProcedimiento() {
      return tramRelExpVO.getCodProcedimiento();
    }
    public void setCodProcedimiento(String codProcedimiento) {
      tramRelExpVO.setCodProcedimiento(codProcedimiento);
    }
    public String getEjercicio() {
      return tramRelExpVO.getEjercicio();
    }
    public void setEjercicio(String ejercicio) {
      tramRelExpVO.setEjercicio(ejercicio);
    }
    public String getNumero() {
      return tramRelExpVO.getNumero();
    }
    public void setNumero(String numero) {
      tramRelExpVO.setNumero(numero);
    }
    public String getCodTramite() {
      return tramRelExpVO.getCodTramite();
    }
    public void setCodTramite(String numero) {
      tramRelExpVO.setCodTramite(numero);
    }
    public String getOcurrenciaTramite() {
      return tramRelExpVO.getOcurrenciaTramite();
    }
    public void setOcurrenciaTramite(String numero) {
      tramRelExpVO.setOcurrenciaTramite(numero);
    }

    public Vector getListaDocumentos() {
      return tramRelExpVO.getListaDocumentos();
    }
    public Vector getListaCodDocumentosTramite() {
      return tramRelExpVO.getListaCodDocumentosTramite();
    }

    public Vector getListaDocumentosTramite() {
      return tramRelExpVO.getListaDocumentosTramite();
    }


    public String getRespOpcion(){
      return tramRelExpVO.getRespOpcion();
    }
    public void setRespOpcion(String respuesta) {
      tramRelExpVO.setRespOpcion(respuesta);
    }

    public String getNotificacionRealizada(){
      return tramRelExpVO.getNotificacionRealizada();
    }
    public void setNotificacionRealizada(String notificacionRealizada) {
      tramRelExpVO.setNotificacionRealizada(notificacionRealizada);
    }


public String getAccion() {
return tramRelExpVO.getAccion();
}
public void setAccion(String accion) {
tramRelExpVO.setAccion(accion);
}
public String getAccionAfirmativa() {
return tramRelExpVO.getAccionAfirmativa();
}
public void setAccionAfirmativa(String accionAfirmativa) {
tramRelExpVO.setAccionAfirmativa(accionAfirmativa);
}
public String getAccionNegativa() {
return tramRelExpVO.getAccionNegativa();
}
public void setAccionNegativa(String accionNegativa) {
tramRelExpVO.setAccionNegativa(accionNegativa);
}
public String getCodigoTramiteFlujoSalida() {
return tramRelExpVO.getCodigoTramiteFlujoSalida();
}
public void setCodigoTramiteFlujoSalida(String codigoTramiteFlujoSalida) {
tramRelExpVO.setCodigoTramiteFlujoSalida(codigoTramiteFlujoSalida);
}
public String getCodigoVisibleTramiteFlujoSalida() {
return tramRelExpVO.getCodigoVisibleTramiteFlujoSalida();
}
public void setCodigoVisibleTramiteFlujoSalida(String codigoVisibleTramiteFlujoSalida) {
tramRelExpVO.setCodigoVisibleTramiteFlujoSalida(codigoVisibleTramiteFlujoSalida);
}
public String getDescripcionTramiteFlujoSalida() {
return tramRelExpVO.getDescripcionTramiteFlujoSalida();
}
public void setDescripcionTramiteFlujoSalida(String descripcionTramiteFlujoSalida) {
tramRelExpVO.setDescripcionTramiteFlujoSalida(descripcionTramiteFlujoSalida);
}
public Vector getListaTramitesFavorables() {
return tramRelExpVO.getListaTramitesFavorables();
}
public Vector getListaTramitesNoFavorables() {
return tramRelExpVO.getListaTramitesNoFavorables();
}
public String getNumeroSecuencia() {
return tramRelExpVO.getNumeroSecuencia();
}
public void setNumeroSecuencia(String numeroSecuencia) {
tramRelExpVO.setNumeroSecuencia(numeroSecuencia);
}
public String getObligat() {
return tramRelExpVO.getObligatorio();
}
public void setObligat(String obligatorio) {
tramRelExpVO.setObligatorio(obligatorio);
}
public String getObligatorioDesf() {
  return tramRelExpVO.getObligatorioDesf();
}
public void setObligatorioDesf(String obligatorioDesf) {
  tramRelExpVO.setObligatorioDesf(obligatorioDesf);
}
public String getPregunta() {
return tramRelExpVO.getPregunta();
}
public void setPregunta(String pregunta) {
tramRelExpVO.setPregunta(pregunta);
}
public String getPermiso() {
  return tramRelExpVO.getPermiso();
}
public void setPermiso(String permiso) {
  tramRelExpVO.setPermiso(permiso);
}

public String getBloqueo() {
  return tramRelExpVO.getBloqueo();
}
public void setBloqueo(String bloqueo) {
  tramRelExpVO.setBloqueo(bloqueo);
}


public String getLocalizacion() {
  return tramRelExpVO.getLocalizacion();
}
public void setLocalizacion(String localizacion) {
  tramRelExpVO.setLocalizacion(localizacion);
}
public String getPoseeLocalizacion() {
  return tramRelExpVO.getPoseeLocalizacion();
}
public void setPoseeLocalizacion(String poseeLocalizacion) {
  tramRelExpVO.setPoseeLocalizacion(poseeLocalizacion);
  }

public String getCodProvincia() {
  return tramRelExpVO.getCodProvincia();
}
public void setCodProvincia(String codProvincia) {
  tramRelExpVO.setCodProvincia(codProvincia);
}
public String getCodTVia() {
  return tramRelExpVO.getCodTVia();
}
public void setCodTVia(String codTVia) {
  tramRelExpVO.setCodTVia(codTVia);
}
public String getDescMunicipio() {
  return tramRelExpVO.getDescMunicipio();
}
public void setDescMunicipio(String descMunicipio) {
  tramRelExpVO.setDescMunicipio(descMunicipio);
}
public String getDescPostal() {
  return tramRelExpVO.getDescPostal();
}
public void setDescPostal(String descPostal) {
  tramRelExpVO.setDescPostal(descPostal);
}
public String getDescProvincia() {
  return tramRelExpVO.getDescProvincia();
}
public void setDescProvincia(String descProvincia) {
  tramRelExpVO.setDescProvincia(descProvincia);
}
public String getDescTVia() {
  return tramRelExpVO.getDescTVia();
}
public void setDescTVia(String descTVia) {
  tramRelExpVO.setDescTVia(descTVia);
}
public String getTxtBloque() {
  return tramRelExpVO.getTxtBloque();
}
public void setTxtBloque(String txtBloque) {
  tramRelExpVO.setTxtBloque(txtBloque);
}
public String getTxtDomicilio() {
  return tramRelExpVO.getTxtDomicilio();
}
public void setTxtDomicilio(String txtDomicilio) {
  tramRelExpVO.setTxtDomicilio(txtDomicilio);
}
public String getTxtEsc() {
  return tramRelExpVO.getTxtEsc();
}
public void setTxtEsc(String txtEsc) {
  tramRelExpVO.setTxtEsc(txtEsc);
}
public String getTxtLetraDesde() {
  return tramRelExpVO.getTxtLetraDesde();
}
public void setTxtLetraDesde(String txtLetraDesde) {
  tramRelExpVO.setTxtLetraDesde(txtLetraDesde);
}
public String getTxtLetraHasta() {
  return tramRelExpVO.getTxtLetraHasta();
}
public void setTxtLetraHasta(String txtLetraHasta) {
  tramRelExpVO.setTxtLetraHasta(txtLetraHasta);
}
public String getTxtNumDesde() {
  return tramRelExpVO.getTxtNumDesde();
}
public void setTxtNumDesde(String txtNumDesde) {
  tramRelExpVO.setTxtNumDesde(txtNumDesde);
}
public String getTxtNumHasta() {
  return tramRelExpVO.getTxtNumHasta();
}
public void setTxtNumHasta(String txtNumHasta) {
  tramRelExpVO.setTxtNumHasta(txtNumHasta);
}
public String getTxtPlta() {
  return tramRelExpVO.getTxtPlta();
}
public void setTxtPlta(String txtPlta) {
  tramRelExpVO.setTxtPlta(txtPlta);
}
public String getTxtPortal() {
  return tramRelExpVO.getTxtPortal();
}
public void setTxtPortal(String txtPortal) {
  tramRelExpVO.setTxtPortal(txtPortal);
}
public String getTxtPta() {
  return tramRelExpVO.getTxtPta();
}
public void setTxtPta(String txtPta) {
  tramRelExpVO.setTxtPta(txtPta);
}
public String getTxtRefCatastral() {
  return tramRelExpVO.getTxtRefCatastral();
}
public void setTxtRefCatastral(String txtRefCatastral) {
  tramRelExpVO.setTxtRefCatastral(txtRefCatastral);
}
public String getNumLineasPaginaListado() {
  return tramRelExpVO.getNumLineasPaginaListado();
}
public void setNumLineasPaginaListado(String numLineasPaginaListado) {
  tramRelExpVO.setNumLineasPaginaListado(numLineasPaginaListado);
}
public String getPaginaListado() {
  return tramRelExpVO.getPaginaListado();
}
public void setPaginaListado(String paginaListado) {
  tramRelExpVO.setPaginaListado(paginaListado);
}
public String getIdDomicilio() {
  return tramRelExpVO.getIdDomicilio();
}
public void setIdDomicilio(String idDomicilio) {
  tramRelExpVO.setIdDomicilio(idDomicilio);
}

public Vector getListaTramitesPendientes() {
  return tramRelExpVO.getListaTramitesPendientes();
}
public void setListaTramitesPendientes(Vector listaTramitesPendientes) {
  tramRelExpVO.setListaTramitesPendientes(listaTramitesPendientes);
}

public String getModoConsulta() {
  return tramRelExpVO.getModoConsulta();
}
public void setModoConsulta(String modoConsulta) {
  tramRelExpVO.setModoConsulta(modoConsulta);
}
public String getExpRelacionado() {
  return tramRelExpVO.getExpRelacionado();
}
public void setExpRelacionado(String expRelacionado) {
  tramRelExpVO.setExpRelacionado(expRelacionado);
}

public String getCodMunicipioIni() {
  return tramRelExpVO.getCodMunicipioIni();
}
public void setCodMunicipioIni(String codMunicipioIni) {
  tramRelExpVO.setCodMunicipioIni(codMunicipioIni);
}
public String getEjercicioIni() {
  return tramRelExpVO.getEjercicioIni();
}
public void setEjercicioIni(String ejercicioIni) {
  tramRelExpVO.setEjercicioIni(ejercicioIni);
}
public String getNumeroIni() {
  return tramRelExpVO.getNumeroIni();
}
public void setNumeroIni(String numeroIni) {
  tramRelExpVO.setNumeroIni(numeroIni);
}

public String getCodLocalizacion() {
  return tramRelExpVO.getCodLocalizacion();
}
public void setCodLocalizacion(String codLocalizacion) {
  tramRelExpVO.setCodLocalizacion(codLocalizacion);
}

public String getObservaciones() {
  return tramRelExpVO.getObservaciones();
}
public void setObservaciones(String observaciones) {
  tramRelExpVO.setObservaciones(observaciones);
}

public String getCodDocumento() {
  return tramRelExpVO.getCodDocumento();
}
public void setCodDocumento(String codDocumento) {
  tramRelExpVO.setCodDocumento(codDocumento);
}
public String getCodPlantilla() {
  return tramRelExpVO.getCodPlantilla();
}
public void setCodPlantilla(String codPlantilla) {
  tramRelExpVO.setCodPlantilla(codPlantilla);
}
    public String getTipoPlantilla() {
      return tramRelExpVO.getTipoPlantilla();
    }
    public void setTipoPlantilla(String tipoPlantilla) {
      tramRelExpVO.setTipoPlantilla(tipoPlantilla);
    }
public String getCodUsuario() {
  return tramRelExpVO.getCodUsuario();
}
public void setCodUsuario(String codUsuario) {
  tramRelExpVO.setCodUsuario(codUsuario);
}

public Vector getListaEnlaces() {
  return tramRelExpVO.getListaEnlaces();
}

public Vector getEstructuraDatosSuplementarios() {
  return tramRelExpVO.getEstructuraDatosSuplementarios();
}
public Vector getValoresDatosSuplementarios() {
  return tramRelExpVO.getValoresDatosSuplementarios();
}

public Vector getListaInteresados() {
  return tramRelExpVO.getListaInteresados();
}
public Vector getListaRoles() {
  return tramRelExpVO.getListaRoles();
}
public String getInteresado() {
  return tramRelExpVO.getInteresado();
}

public String getEditorTexto() {
  return tramRelExpVO.getEditorTexto();
}

public void setEditorTexto(String editorTexto) {
  tramRelExpVO.setEditorTexto(editorTexto);
}

public String getListaCodInteresados() {
  return tramRelExpVO.getListaCodInteresados();
}

public Vector getVectorCodInteresados() {
 return tramRelExpVO.getVectorCodInteresados();
}

public String getListaVersInteresados() {
  return tramRelExpVO.getListaVersInteresados();
}
public void setListaCodInteresados(String listaCodInteresados) {
  tramRelExpVO.setListaCodInteresados(listaCodInteresados);
}
public void setVectorCodInteresados(Vector vectorCodInteresados) {
  tramRelExpVO.setVectorCodInteresados(vectorCodInteresados);
}
public void setListaVersInteresados(String listaVersInteresados) {
  tramRelExpVO.setListaVersInteresados(listaVersInteresados);
}

public String getDesdeInformesGestion() {
  return tramRelExpVO.getDesdeInformesGestion();
}
public void setDesdeInformesGestion(String desdeInformesGestion) {
  tramRelExpVO.setDesdeInformesGestion(desdeInformesGestion);
}
public String getTodos() {
  return tramRelExpVO.getTodos();
}
public void setTodos(String todos) {
  tramRelExpVO.setTodos(todos);
}


public GeneralValueObject getListaFicheros(){
    return tramRelExpVO.getListaFicheros();
}
public void setListaFicheros(GeneralValueObject listaFicheros){
    tramRelExpVO.setListaFicheros(listaFicheros);
}
public GeneralValueObject getListaTiposFicheros(){
    return tramRelExpVO.getListaTiposFicheros();
}
public void setListaTiposFicheros(GeneralValueObject listaTiposFicheros){
    tramRelExpVO.setListaTiposFicheros(listaTiposFicheros);
}

public String getEstadoFirma() {
    return tramRelExpVO.getEstadoFirma();
}
public void setEstadoFirma(String newValue) {
    tramRelExpVO.setEstadoFirma(newValue);
}

	public String getMensajeSW() {
		return tramRelExpVO.getMensajeSW();
	}

	public void setMensajeSW(String mensaje) {
		tramRelExpVO.setMensajeSW(mensaje);
	}


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            tramRelExpVO.validate(idioma);
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


}