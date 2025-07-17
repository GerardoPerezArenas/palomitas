package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.TramitacionExternaValueObject;
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
public class TramitacionExternaForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(TramitacionExternaForm.class.getName());

    //Reutilizamos
    TramitacionExternaValueObject tramExtVO = new TramitacionExternaValueObject();

    public TramitacionExternaValueObject getTramitacionExterna() {
        return tramExtVO;
    }

    public void setTramitacionExterna(TramitacionExternaValueObject tramExtVO) {
        this.tramExtVO = tramExtVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

    public String getCodDepartamento() {
      return tramExtVO.getCodDepartamento();
    }
    public void setCodDepartamento(String codDepartamento) {
      tramExtVO.setCodDepartamento(codDepartamento);
    }
    public String getCodProcedimiento() {
      return tramExtVO.getCodProcedimiento();
    }
    public void setCodProcedimiento(String codProcedimiento) {
      tramExtVO.setCodProcedimiento(codProcedimiento);
    }
    public String getDescDepartamento() {
      return tramExtVO.getDescDepartamento();
    }
    public void setDescDepartamento(String descDepartamento) {
      tramExtVO.setDescDepartamento(descDepartamento);
    }
    public String getDescProcedimiento() {
      return tramExtVO.getDescProcedimiento();
    }
    public void setDescProcedimiento(String descProcedimiento) {
      tramExtVO.setDescProcedimiento(descProcedimiento);
    }
    public String getCodTramiteExterno() {
      return tramExtVO.getCodTramiteExterno();
    }
    public void setCodTramiteExterno(String codTramiteExterno) {
      tramExtVO.setCodTramiteExterno(codTramiteExterno);
    }
    public String getDescTramiteExterno() {
      return tramExtVO.getDescTramiteExterno();
    }
    public void setDescTramiteExterno(String descTramiteExterno) {
      tramExtVO.setDescTramiteExterno(descTramiteExterno);
    }


  public Vector getListaDepartamentos() {
    return tramExtVO.getListaDepartamentos();
  }


    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            tramExtVO.validate(idioma);
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
