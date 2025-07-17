package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.PendientesPorUnidadValueObject;
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
public class PendientesPorUnidadForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(PendientesPorUnidadForm.class.getName());

    //Reutilizamos
    PendientesPorUnidadValueObject pendUnidVO = new PendientesPorUnidadValueObject();

    public PendientesPorUnidadValueObject getPendientesPorUnidad() {
        return pendUnidVO;
    }

    public void setPendientesPorUnidad(PendientesPorUnidadValueObject pendUnidVO) {
        this.pendUnidVO = pendUnidVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

public String getCodUnidad() {
  return pendUnidVO.getCodUnidad();
}
public void setCodUnidad(String codUnidad) {
  pendUnidVO.setCodUnidad(codUnidad);
}
public String getDescUnidad() {
  return pendUnidVO.getDescUnidad();
}
public void setDescUnidad(String descUnidad) {
  pendUnidVO.setDescUnidad(descUnidad);
}
public String getTxtRegistros() {
  return pendUnidVO.getTxtRegistros();
}
public void setTxtRegistros(String txtRegistros) {
  pendUnidVO.setTxtRegistros(txtRegistros);
}

public Vector getListaUnidades() {
  return pendUnidVO.getListaUnidades();
}

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            pendUnidVO.validate(idioma);
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
