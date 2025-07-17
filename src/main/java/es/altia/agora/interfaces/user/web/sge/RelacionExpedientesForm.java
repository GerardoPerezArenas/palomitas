package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.RelacionExpedientesValueObject;
import es.altia.agora.business.util.ElementoListaValueObject;
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
public class RelacionExpedientesForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(RelacionExpedientesForm.class.getName());

    //Reutilizamos
    RelacionExpedientesValueObject relExpVO = new RelacionExpedientesValueObject();

    public RelacionExpedientesValueObject getRelacionExpedientes() {
        return relExpVO;
    }

    public void setRelacionExpedientes(RelacionExpedientesValueObject relExpVO) {
        this.relExpVO = relExpVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */
    public String getCodMunicipio() {
      return relExpVO.getCodMunicipio();
    }
    public void setCodMunicipio(String codMunicipio) {
      relExpVO.setCodMunicipio(codMunicipio);
    }

    public String getCodProcedimiento() {
      return relExpVO.getCodProcedimiento();
    }
    public void setCodProcedimiento(String codProcedimiento) {
      relExpVO.setCodProcedimiento(codProcedimiento);
    }
    public String getDescProcedimiento() {
      return relExpVO.getDescProcedimiento();
    }
    public void setDescProcedimiento(String descProcedimiento) {
      relExpVO.setDescProcedimiento(descProcedimiento);
    }

    public String getEjercicio() {
      return relExpVO.getEjercicio();
    }
    public void setEjercicio(String ejercicio) {
      relExpVO.setEjercicio(ejercicio);
    }

    public String getNumeroRelacion() {
      return relExpVO.getNumeroRelacion();
    }
    public void setNumeroRelacion(String numeroRelacion) {
      relExpVO.setNumeroRelacion(numeroRelacion);
    }

    public String getNumero() {
      return relExpVO.getNumero();
    }
    public void setNumero(String numero) {
      relExpVO.setNumero(numero);
    }


    public String getCodOcultoTramite() {
      return relExpVO.getCodOcultoTramite();
    }
    public void setCodOcultoTramite(String codOcultoTramite) {
      relExpVO.setCodOcultoTramite(codOcultoTramite);
    }
    public String getCodTramite() {
      return relExpVO.getCodTramite();
    }
    public void setCodTramite(String codTramite) {
      relExpVO.setCodTramite(codTramite);
    }
    public String getDescTramite() {
      return relExpVO.getDescTramite();
    }
    public void setDescTramite(String descTramite) {
      relExpVO.setDescTramite(descTramite);
    }

    public Vector getListaProcedimientos() {
      return relExpVO.getListaProcedimientos();
    }
    public void setListaProcedimientos(Vector listaProcedimientos) {
      relExpVO.setListaProcedimientos(listaProcedimientos);
    }

    public Vector getListaTramites() {
      return relExpVO.getListaTramites();
    }
    public void setListaTramites(Vector listaTramites) {
      relExpVO.setListaTramites(listaTramites);
    }


    public String getNumLineasPaginaListado() {
      return relExpVO.getNumLineasPaginaListado();
    }
    public void setNumLineasPaginaListado(String numLineasPaginaListado) {
      relExpVO.setNumLineasPaginaListado(numLineasPaginaListado);
    }
    public String getPaginaListado() {
      return relExpVO.getPaginaListado();
    }
    public void setPaginaListado(String paginaListado) {
      relExpVO.setPaginaListado(paginaListado);
    }
    public String getNumLineasPaginaListadoE() {
      return relExpVO.getNumLineasPaginaListadoE();
    }
    public void setNumLineasPaginaListadoE(String numLineasPaginaListadoE) {
      relExpVO.setNumLineasPaginaListadoE(numLineasPaginaListadoE);
    }
    public String getPaginaListadoE() {
      return relExpVO.getPaginaListadoE();
    }
    public void setPaginaListadoE(String paginaListadoE) {
      relExpVO.setPaginaListadoE(paginaListadoE);
    }





    public String getAsunto() {
      return relExpVO.getAsunto();
    }
    public void setAsunto(String asunto) {
      relExpVO.setAsunto(asunto);
    }
    public String getCodDepartamento() {
      return relExpVO.getCodDepartamento();
    }
    public void setCodDepartamento(String codDepartamento) {
      relExpVO.setCodDepartamento(codDepartamento);
    }
    public String getCodUnidadRegistro() {
      return relExpVO.getCodUnidadRegistro();
    }
    public void setCodUnidadRegistro(String codUnidadRegistro) {
      relExpVO.setCodUnidadRegistro(codUnidadRegistro);
    }
    public String getEjerNum() {
      return relExpVO.getEjerNum();
    }
    public void setEjerNum(String ejerNum) {
      relExpVO.setEjerNum(ejerNum);
    }
    public String getFechaAnotacion() {
      return relExpVO.getFechaAnotacion();
    }
    public void setFechaAnotacion(String fechaAnotacion) {
      relExpVO.setFechaAnotacion(fechaAnotacion);
    }
    public String getRemitente() {
      return relExpVO.getRemitente();
    }
    public void setRemitente(String remitente) {
      relExpVO.setRemitente(remitente);
    }
    public String getTipoRegistro() {
      return relExpVO.getTipoRegistro();
    }
    public void setTipoRegistro(String tipoRegistro) {
      relExpVO.setTipoRegistro(tipoRegistro);
  }

  public String getNumeroRelacionExpedientes() {
    return relExpVO.getNumeroRelacionExpedientes();
  }
  public void setNumeroRelacionExpedientes(String numeroRelacionExpedientes) {
    relExpVO.setNumeroRelacionExpedientes(numeroRelacionExpedientes);
  }
  public String getNumeroExpedienteAntiguo() {
    return relExpVO.getNumeroExpedienteAntiguo();
  }
  public void setNumeroExpedienteAntiguo(String numeroExpedienteAntiguo) {
    relExpVO.setNumeroExpedienteAntiguo(numeroExpedienteAntiguo);
  }

  public String getFechaInicioExpediente() {
    return relExpVO.getFechaInicio();
  }
  public void setFechaInicioExpediente(String fechaInicioExpediente) {
    relExpVO.setFechaInicio(fechaInicioExpediente);
  }

  public String getRespOpcion() {
    return relExpVO.getRespOpcion();
  }
  public void setRespOpcion(String respOpcion) {
    relExpVO.setRespOpcion(respOpcion);
  }
  
    public String getCodUtr() {
        return relExpVO.getCodUor();
    }

    public void setCodUtr(String codUor) {
        this.relExpVO.setCodUor(codUor);
    }

    public String getDescUtr() {
        return this.relExpVO.getDescUor();
    }

    public void setDescUtr(String descUor) {
        this.relExpVO.setDescUor(descUor);
    }
    
    public String getCodOcultoUtr() {
        return this.relExpVO.getCodOcultoUor();
    }

    public void setCodOcultoUtr(String descUor) {
        this.relExpVO.setCodOcultoUor(descUor);
    }
    
    public Vector<ElementoListaValueObject> getListaUtrs() {
        return this.relExpVO.getListaUnidadesTramitadoras();
    }

    public void setListaUtrs(Vector<ElementoListaValueObject> listaUtrs) {
        this.relExpVO.setListaUnidadesTramitadoras(listaUtrs);
    }

    public Vector<ElementoListaValueObject> getListaUnidadesProcedimiento(){
        return this.relExpVO.getListaUnidadesProcedimiento();
    }
    
    public void setListaUnidadesProcedimiento(Vector<ElementoListaValueObject> listaUnidadesProcedimiento){
        this.relExpVO.setListaUnidadesProcedimiento(listaUnidadesProcedimiento);
    }
    
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            relExpVO.validate(idioma);
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