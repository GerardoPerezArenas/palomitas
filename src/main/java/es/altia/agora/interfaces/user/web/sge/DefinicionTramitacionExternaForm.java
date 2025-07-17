package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.DefinicionTramitacionExternaValueObject;
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
public class DefinicionTramitacionExternaForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(DefinicionTramitacionExternaForm.class.getName());

    //Reutilizamos
    DefinicionTramitacionExternaValueObject defTramExtVO = new DefinicionTramitacionExternaValueObject();

    public DefinicionTramitacionExternaValueObject getDefinicionTramitacionExterna() {
        return defTramExtVO;
    }

    public void setDefinicionTramitacionExterna(DefinicionTramitacionExternaValueObject defTramExtVO) {
        this.defTramExtVO = defTramExtVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

    public String getCodDepartamento() {
        return defTramExtVO.getCodDepartamento();
      }
      public void setCodDepartamento(String codDepartamento) {
        defTramExtVO.setCodDepartamento(codDepartamento);
      }
      public String getCodProcedimiento() {
        return defTramExtVO.getCodProcedimiento();
      }
      public void setCodProcedimiento(String codProcedimiento) {
        defTramExtVO.setCodProcedimiento(codProcedimiento);
      }
      public String getCodTramiteExterno() {
        return defTramExtVO.getCodTramiteExterno();
      }
      public void setCodTramiteExterno(String codTramiteExterno) {
        defTramExtVO.setCodTramiteExterno(codTramiteExterno);
      }
      public String getDescDepartamento() {
        return defTramExtVO.getDescDepartamento();
      }
      public void setDescDepartamento(String descDepartamento) {
        defTramExtVO.setDescDepartamento(descDepartamento);
      }
      public String getDescProcedimiento() {
        return defTramExtVO.getDescProcedimiento();
      }
      public void setDescProcedimiento(String descProcedimiento) {
        defTramExtVO.setDescProcedimiento(descProcedimiento);
      }
      public String getNomTramiteExterno() {
        return defTramExtVO.getNomTramiteExterno();
      }
      public void setNomTramiteExterno(String nomTramiteExterno) {
        defTramExtVO.setNomTramiteExterno(nomTramiteExterno);
      }
      public String getTramiteRelacionado() {
        return defTramExtVO.getTramiteRelacionado();
      }
      public void setTramiteRelacionado(String tramiteRelacionado) {
        defTramExtVO.setTramiteRelacionado(tramiteRelacionado);
      }

      public Vector getListaDepartamentos() {
        return defTramExtVO.getListaDepartamentos();
      }




    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            defTramExtVO.validate(idioma);
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
