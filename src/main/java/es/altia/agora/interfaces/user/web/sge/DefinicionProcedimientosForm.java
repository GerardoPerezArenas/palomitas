package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.CampoListadoPendientesProcedimientoVO;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.common.service.config.*;
import es.altia.flexia.expedientes.anulacion.plugin.VerificacionFinNoConvencionalExpediente;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;


/** Clase utilizada para capturar o mostrar el estado de un RegistroEntrada */
public class DefinicionProcedimientosForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(DefinicionProcedimientosForm.class.getName());

	private ArrayList<ModuloIntegracionExterno> listaModulosPantallasDefinicionProcedimientos = new ArrayList<ModuloIntegracionExterno>();
    //Reutilizamos
    DefinicionProcedimientosValueObject defProcVO = new DefinicionProcedimientosValueObject();

    public DefinicionProcedimientosValueObject getDefinicionProcedimientos() {
        return defProcVO;
    }

    public void setDefinicionProcedimientos(DefinicionProcedimientosValueObject dProcVO) {
        this.defProcVO = dProcVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

    public String getRequiereFirma (){
        return defProcVO.getRequiereFirma();
    }
    
    public void setRequiereFirma(String requiereFirma){
        defProcVO.setRequiereFirma(requiereFirma);
    }
public String getCodTipoProcedimiento() {
  return defProcVO.getCodTipoProcedimiento();
}
public void setCodTipoProcedimiento(String codTipoProcedimiento) {
  defProcVO.setCodTipoProcedimiento(codTipoProcedimiento);
}
public String getCodUnidadInicio() {
  return defProcVO.getCodUnidadInicio();
}
public void setCodUnidadInicio(String codUnidadInicio) {
  defProcVO.setCodUnidadInicio(codUnidadInicio);
}
public String getCodVisibleUnidadInicio() {
  return defProcVO.getCodVisibleUnidadInicio();
}
public void setCodVisibleUnidadInicio(String codVisibleUnidadInicio) {
  defProcVO.setCodVisibleUnidadInicio(codVisibleUnidadInicio);
}
public String getDescTipoProcedimiento() {
  return defProcVO.getDescTipoProcedimiento();
}
public void setDescTipoProcedimiento(String descTipoProcedimiento) {
  defProcVO.setDescTipoProcedimiento(descTipoProcedimiento);
}
public String getDescUnidadInicio() {
  return defProcVO.getDescUnidadInicio();
}
public void setDescUnidadInicio(String descUnidadInicio) {
  defProcVO.setDescUnidadInicio(descUnidadInicio);
}
public String getDisponible() {
  return defProcVO.getDisponible();
}
public void setDisponible(String disponible) {
  defProcVO.setDisponible(disponible);
}
public String getTramitacionInternet() {
   return defProcVO.getTramitacionInternet();
 }
 public void setTramitacionInternet(String tramitacionInternet) {
   defProcVO.setTramitacionInternet(tramitacionInternet);
 }
 public String getInteresadoOblig() {
   return defProcVO.getInteresadoOblig();
 }
 public void setInteresadoOblig(String interesadoOblig) {
   defProcVO.setInteresadoOblig(interesadoOblig);
 }
 
 //Mai. Para soloWS (que indica si un procedimiento solo se puede inicializar por WebService)
 public String getSoloWS() {
    
   return defProcVO.getSoloWS();
 }
 public void setSoloWS(String soloWS) {
   
   defProcVO.setSoloWS(soloWS);
 }
 
public String getPlazo() {
  return defProcVO.getPlazo();
}
public void setPlazo(String plazo) {
  defProcVO.setPlazo(plazo);
}
public String getTipoPlazo() {
  return defProcVO.getTipoPlazo();
}
public void setTipoPlazo(String tipoPlazo) {
  defProcVO.setTipoPlazo(tipoPlazo);
}
public String getTipoSilencio() {
  return defProcVO.getTipoSilencio();
}
public void setTipoSilencio(String tipoSilencio) {
  defProcVO.setTipoSilencio(tipoSilencio);
}
public String getCodArea() {
  return defProcVO.getCodArea();
}
public void setCodArea(String codArea) {
  defProcVO.setCodArea(codArea);
}
public String getCodEstado() {
  return defProcVO.getCodEstado();
}
public void setCodEstado(String codEstado) {
  defProcVO.setCodEstado(codEstado);
}

public String getCqUnidadInicio() {
  return defProcVO.getCqUnidadInicio();
}
public void setcqUnidadInicio(String cqUnidadInicio) {
  defProcVO.setCqUnidadInicio(cqUnidadInicio);
}

public String getDescArea() {
  return defProcVO.getDescArea();
}
public void setDescArea(String descArea) {
  defProcVO.setDescArea(descArea);
}
public String getDescEstado() {
  return defProcVO.getDescEstado();
}
public void setDescEstado(String descEstado) {
  defProcVO.setDescEstado(descEstado);
}
public String getFechaLimiteDesde() {
  return defProcVO.getFechaLimiteDesde();
}
public void setFechaLimiteDesde(String fechaLimiteDesde) {
  defProcVO.setFechaLimiteDesde(fechaLimiteDesde);
}
public String getFechaLimiteHasta() {
  return defProcVO.getFechaLimiteHasta();
}
public void setFechaLimiteHasta(String fechaLimiteHasta) {
  defProcVO.setFechaLimiteHasta(fechaLimiteHasta);
  }
public String getTxtCodigo() {
  return defProcVO.getTxtCodigo();
}
public void setTxtCodigo(String txtCodigo) {
  defProcVO.setTxtCodigo(txtCodigo);
}
public String getTxtDescripcion() {
  return defProcVO.getTxtDescripcion();
}
public void setTxtDescripcion(String txtDescripcion) {
  defProcVO.setTxtDescripcion(txtDescripcion);
}
public String getNumeroTramite() {
  return defProcVO.getNumeroTramite();
}
public void setNumeroTramite(String numeroTramite) {
  defProcVO.setNumeroTramite(numeroTramite);
}
public String getCodigoTramite() {
   return defProcVO.getCodigoTramite();
 }
public void setCodigoTramite(String codigoTramite) {
   defProcVO.setCodigoTramite(codigoTramite);
 }
public String getCodClasificacionTramite() {
   return defProcVO.getCodClasificacionTramite();
 }
public void setCodClasificacionTramite(String codigoTramite) {
   defProcVO.setCodClasificacionTramite(codigoTramite);
 }
public String getDescClasificacionTramite() {
   return defProcVO.getDescClasificacionTramite();
 }
public void setDescClasificacionTramite(String codigoTramite) {
   defProcVO.setDescClasificacionTramite(codigoTramite);
 }

public String getCodMunicipio() {
    return defProcVO.getCodMunicipio();
  }
  public void setCodMunicipio(String codMunicipio) {
    defProcVO.setCodMunicipio(codMunicipio);
  }
  public String getDescMunicipio() {
    return defProcVO.getDescMunicipio();
  }
  public void setDescMunicipio(String descMunicipio) {
    defProcVO.setDescMunicipio(descMunicipio);
  }
  public String getCodAplicacion() {
    return defProcVO.getCodAplicacion();
  }
  public void setCodAplicacion(String codAplicacion) {
    defProcVO.setCodAplicacion(codAplicacion);
  }
  public String getImportar() {
    return defProcVO.getImportar();
  }
  public void setImportar(String importar) {
    defProcVO.setImportar(importar);
  }
  public String getDescTipoInicio() {
    return defProcVO.getDescTipoInicio();
  }
  public void setDescTipoInicio(String descTipoInicio) {
    defProcVO.setDescTipoInicio(descTipoInicio);
  }
  public String getCodTipoInicio() {
    return defProcVO.getCodTipoInicio();
  }
  public void setCodTipoInicio(String codTipoInicio) {
    defProcVO.setCodTipoInicio(codTipoInicio);
  }

public String getLocalizacion() {  return defProcVO.getLocalizacion(); }
public void setLocalizacion(String param) {  defProcVO.setLocalizacion(param);}

public String getNormativa() {  return defProcVO.getNormativa(); }
public void setNormativa(String param) {  defProcVO.setNormativa(param);}

public String getNombreTramite() {
  return defProcVO.getNombreTramite();
}
public void setNombreTramite(String nombreTramite) {
  defProcVO.setNombreTramite(nombreTramite);
}
public String getPosicionProcedimiento() {
   return defProcVO.getPosicionProcedimiento();
 }
 public void setPosicionProcedimiento(String posicionProcedimiento) {
   defProcVO.setPosicionProcedimiento(posicionProcedimiento);
 }
 public String getNumLineasPaginaListado() {
  return defProcVO.getNumLineasPaginaListado();
}
public void setNumLineasPaginaListado(String numLineasPaginaListado) {
  defProcVO.setNumLineasPaginaListado(numLineasPaginaListado);
}
public String getPaginaListado() {
  return defProcVO.getPaginaListado();
}
public void setPaginaListado(String paginaListado) {
  defProcVO.setPaginaListado(paginaListado);
  }

public Vector getTramites() {
  return defProcVO.getTramites();
}
public void setTramites(Vector v) {
  defProcVO.setTramites(v);
}



  // Listas de valores
  // 1. Tipos de area.
  public Vector getListaArea() {
    return (Vector) defProcVO.getListaArea();
  }

  // 2. Tipos de procedimientos.
  public Vector getListaTiposProcedimientos() {
    return (Vector) defProcVO.getListaTiposProcedimientos();
  }

  // 3. Tipos de unidad de inicio.
  public Vector getListaUnidadInicio() {
    return (Vector) defProcVO.getListaUnidadInicio();
  }

  // 4. Tipos de documentos.
  public Vector getListaTiposDocumentos() {
   return (Vector) defProcVO.getListaTiposDocumentos();
 }

  // pestaña de Documentos

 /*public Vector getListaCodigosDoc() {
    return (Vector) defProcVO.getListaCodigosDoc();
  }
  public Vector getListaCodTipoDoc() {
    return (Vector) defProcVO.getListaCodTipoDoc();
  }
  public Vector getListaCondicionDoc() {
    return (Vector) defProcVO.getListaCondicionDoc();
  }
  public Vector getListaNombresDoc() {
    return (Vector) defProcVO.getListaNombresDoc();
  }
  public Vector getListaObligatoriosDoc() {
    return (Vector) defProcVO.getListaObligatoriosDoc();
  }*/

  public String getCodigoDocumento() {
    return defProcVO.getCodigoDocumento();
  }
  public void setCodigoDocumento(String codigoDocumento) {
    defProcVO.setCodigoDocumento(codigoDocumento);
  }
  public String getCodTipoDocumento() {
    return defProcVO.getCodTipoDocumento();
  }
  public void setCodTipoDocumento(String codTipoDocumento) {
    defProcVO.setCodTipoDocumento(codTipoDocumento);
  }
  public String getCondicion() {
    return defProcVO.getCondicion();
  }
  public void setCondicion(String condicion) {
    defProcVO.setCondicion(condicion);
  }
  public String getDescTipoDocumento() {
    return defProcVO.getDescTipoDocumento();
  }
  public void setDescTipoDocumento(String descTipoDocumento) {
    defProcVO.setDescTipoDocumento(descTipoDocumento);
  }
  public String getNombreDocumento() {
    return defProcVO.getNombreDocumento();
  }
  public void setNombreDocumento(String nombreDocumento) {
    defProcVO.setNombreDocumento(nombreDocumento);
  }
  public String getObligatorio() {
    return defProcVO.getObligatorio();
  }
  public void setObligatorio(String obligatorio) {
    defProcVO.setObligatorio(obligatorio);
    }

  public Vector getListasDoc() {
    return (Vector) defProcVO.getListasDoc();
  }

  public String getNoModificar() {
    return defProcVO.getNoModificar();
  }
  public void setNoModificar(String noModificar) {
    defProcVO.setNoModificar(noModificar);
  }

  public String getDeCatalogo() {
    return defProcVO.getDeCatalogo();
  }
  public void setDeCatalogo(String deCatalogo) {
    defProcVO.setDeCatalogo(deCatalogo);
  }
  public String getDeCatalogoDeProcedimiento() {
    return defProcVO.getDeCatalogoDeProcedimiento();
  }
  public void setDeCatalogoDeProcedimiento(String deCatalogoDeProcedimiento) {
    defProcVO.setDeCatalogoDeProcedimiento(deCatalogoDeProcedimiento);
  }

  public Vector getTablaUnidadInicio() {
    return defProcVO.getTablaUnidadInicio();
  }
  public void setTablaUnidadInicio(Vector tablaUnidadInicio) {
    defProcVO.setTablaUnidadInicio(tablaUnidadInicio);
  }

  public Vector getListaCampos() {
    return defProcVO.getListaCampos();
  }
  public void setListaCampos(Vector listaCampos) {
    defProcVO.setListaCampos(listaCampos);
  }

  public Vector getListaEnlaces() {
    return defProcVO.getListaEnlaces();
  }

  public String getCodRol() {
    return defProcVO.getCodRol();
  }
  public void setCodRol(String codRol) {
    defProcVO.setCodRol(codRol);
  }
  public String getDescRol() {
    return defProcVO.getDescRol();
  }
  public void setDescRol(String descRol) {
    defProcVO.setDescRol(descRol);
  }
  public Vector getListaRoles() {
    return defProcVO.getListaRoles();
  }
  public String getRolPorDefecto() {
    return defProcVO.getRolPorDefecto();
  }
  public void setRolPorDefecto(String rolPorDefecto) {
    defProcVO.setRolPorDefecto(rolPorDefecto);
  }
  public String getConsultaWebRol() {
    return defProcVO.getConsultaWebRol();
  }
  public void setConsultaWebRol(String consultaWeb) {
    defProcVO.setConsultaWebRol(consultaWeb);
  }  
  public String getTramiteInicio() {
    return defProcVO.getTramiteInicio();
  }
  public void setTramiteInicio(String tramiteInicio) {
    defProcVO.setTramiteInicio(tramiteInicio);
  }
  
  public String getRestringido() {  return defProcVO.getRestringido(); }  
  public void setRestringido(String param) {  defProcVO.setRestringido(param);}

    public ArrayList<ModuloIntegracionExterno> getListaModulosPantallasDefinicionProcedimientos() {
        return listaModulosPantallasDefinicionProcedimientos;
    }

    public void setListaModulosPantallasDefinicionProcedimientos(ArrayList<ModuloIntegracionExterno> listaModulosPantallasDefinicionProcedimientos) {
        this.listaModulosPantallasDefinicionProcedimientos = listaModulosPantallasDefinicionProcedimientos;
    }



    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            defProcVO.validate(idioma);
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


    // Para campo Descripcion Breve.
    public String getDescripcionBreve() {
        return defProcVO.getDescripcionBreve();
    }

    public void setDescripcionBreve(String descripcionBreve) {
        defProcVO.setDescripcionBreve(descripcionBreve);
    }


    public String getPorcentaje(){
        return defProcVO.getPorcentaje();
    }

    public void setPorcentaje(String porcentaje){
        defProcVO.setPorcentaje(porcentaje);
    }
    
    public ArrayList<CampoListadoPendientesProcedimientoVO> getCamposPendientesSeleccionados(){
        return this.defProcVO.getCamposPendientesSeleccionados();
    }

    public void setCamposPendientesSeleccionados(ArrayList<CampoListadoPendientesProcedimientoVO> campos){
        this.defProcVO.setCamposPendientesSeleccionados(campos);
    }

    

     /**
     * @return the listaCodigosCamposPendientes
     */
    public String getListaCodigosCamposPendientes() {
        return this.defProcVO.getListaCodigosCamposPendientes();
    }

    /**
     * @param listaCodigosCamposPendientes the listaCodigosCamposPendientes to set
     */
    public void setListaCodigosCamposPendientes(String listaCodigosCamposPendientes) {
        this.defProcVO.setListaCodigosCamposPendientes(listaCodigosCamposPendientes);
    }

    /**
     * @return the listaNombresCamposPendientes
     */
    public String getListaNombresCamposPendientes() {
        return this.defProcVO.getListaNombresCamposPendientes();
    }

    /**
     * @param listaNombresCamposPendientes the listaNombresCamposPendientes to set
     */
    public void setListaNombresCamposPendientes(String listaNombresCamposPendientes) {
        this.defProcVO.setListaNombresCamposPendientes(listaNombresCamposPendientes);
    }

    /**
     * @return the listaTamanhoCamposPendientes
     */
    public String getListaTamanhoCamposPendientes() {
        return this.defProcVO.getListaTamanhoCamposPendientes();
    }

    /**
     * @param listaTamanhoCamposPendientes the listaTamanhoCamposPendientes to set
     */
    public void setListaTamanhoCamposPendientes(String listaTamanhoCamposPendientes) {
        this.defProcVO.setListaTamanhoCamposPendientes(listaTamanhoCamposPendientes);
    }

    /**
     * @return the listaOrdenCamposPendientes
     */
    public String getListaOrdenCamposPendientes() {
        return this.defProcVO.getListaOrdenCamposPendientes();
    }

    /**
     * @param listaOrdenCamposPendientes the listaOrdenCamposPendientes to set
     */
    public void setListaOrdenCamposPendientes(String listaOrdenCamposPendientes) {
        this.defProcVO.setListaOrdenCamposPendientes(listaOrdenCamposPendientes);
    }

    /**
     * @return the listaCampoSupCamposPendientes
     */
    public String getListaCampoSupCamposPendientes() {
        return this.defProcVO.getListaCampoSupCamposPendientes();
    }

    /**
     * @param listaCampoSupCamposPendientes the listaCampoSupCamposPendientes to set
     */
    public void setListaCampoSupCamposPendientes(String listaCampoSupCamposPendientes) {
        this.defProcVO.setListaCampoSupCamposPendientes(listaCampoSupCamposPendientes);
    }
    
    
    public String getDescServicioFinalizacion(){
        return this.defProcVO.getDescServicioFinalizacion();
    }
        
   /**
     * @param descServicioFinalizacion the descServicioFinalizacion to set
     */
    public void setDescServicioFinalizacion(String descServicioFinalizacion) {
        this.defProcVO.setDescServicioFinalizacion(descServicioFinalizacion);
    }

    
    /**
     * @return the codServicioFinalizacion
     */
    public String getCodServicioFinalizacion() {
        return this.defProcVO.getCodServicioFinalizacion();
    }

    /**
     * @param codServicioFinalizacion the codServicioFinalizacion to set
     */
    public void setCodServicioFinalizacion(String codServicioFinalizacion) {
        this.defProcVO.setCodServicioFinalizacion(codServicioFinalizacion);
    }    
    
    
    
    private ArrayList<VerificacionFinNoConvencionalExpediente>  serviciosVerificacionFinExpediente = null;

    /**
     * @return the serviciosVerificacionFinExpediente
     */
    public ArrayList<VerificacionFinNoConvencionalExpediente> getServiciosVerificacionFinExpediente() {
        return serviciosVerificacionFinExpediente;
    }

    /**
     * @param serviciosVerificacionFinExpediente the serviciosVerificacionFinExpediente to set
     */
    public void setServiciosVerificacionFinExpediente(ArrayList<VerificacionFinNoConvencionalExpediente> serviciosVerificacionFinExpediente) {
        this.serviciosVerificacionFinExpediente = serviciosVerificacionFinExpediente;
    }
     
    
   /**
     * @return the implClassServicioFinalizacion
     */
    public String getImplClassServicioFinalizacion() {
        return this.defProcVO.getImplClassServicioFinalizacion();
    }

    /**
     * @param implClassServicioFinalizacion the implClassServicioFinalizacion to set
     */
    public void setImplClassServicioFinalizacion(String implClassServicioFinalizacion) {
        this.defProcVO.setImplClassServicioFinalizacion(implClassServicioFinalizacion);
    }

    public Vector getListaAgrupaciones() {
        return defProcVO.getListaAgrupaciones();
    }
    public void setListaAgrupaciones(Vector listaAgrupaciones) {
        defProcVO.setListaAgrupaciones(listaAgrupaciones);
    }
    
    /**
     * @return the codClaseHist
     */
    public String getClaseBuzonEntradaHistorico() {
        return this.defProcVO.getClaseBuzonEntradaHistorico();
    }

    /**
     * @param codClaseHist the codClaseHist to set
     */
    public void setClaseBuzonEntradaHistorico(String claseHist) {
        this.defProcVO.setClaseBuzonEntradaHistorico(claseHist);
    } 
    
    /**
     * @return the codClaseHist
     */
    public String getcodClaseHist() {
        return this.defProcVO.getCodClaseHist();
    }

    /**
     * @param codClaseHist the codClaseHist to set
     */
    public void setcodClaseHisto(String codClaseHist) {
        this.defProcVO.setCodClaseHist(codClaseHist);
    } 
    
    /**
     * @return the codClaseHist
     */
    public String getdescClaseHist() {
        return this.defProcVO.getDescClaseHist();
    }

    /**
     * @param codClaseHist the codClaseHist to set
     */
    public void setdescClaseHist(String descClaseHist) {
        this.defProcVO.setDescClaseHist(descClaseHist);
    }
    
    public String getBiblioteca(){
        return this.defProcVO.getBiblioteca();
    }
    
    public void setBiblioteca(String biblioteca){
        this.defProcVO.setBiblioteca(biblioteca);
    }
    
    public String getPropNumExpedientesAnoAsientoBuzon() {
        return this.defProcVO.getPropNumExpedientesAnoAsientoBuzon();
    }
    
    public void setPropNumExpedientesAnoAsientoBuzon(String propNumExpedientesAnoAsientoBuzon) {
        this.defProcVO.setPropNumExpedientesAnoAsientoBuzon(propNumExpedientesAnoAsientoBuzon);
    }
    
    public int getNumeracionExpedientesAnoAsiento() {
        return this.defProcVO.getNumeracionExpedientesAnoAsiento();
    }

    public void setNumeracionExpedientesAnoAsiento(int numeracionExpedientesAnoAsiento) {
        this.defProcVO.setNumeracionExpedientesAnoAsiento(numeracionExpedientesAnoAsiento);
    }
    
    // Reseteamos el formulario
    public void reset(){
        this.defProcVO.setNumeracionExpedientesAnoAsiento(0);
    }
}