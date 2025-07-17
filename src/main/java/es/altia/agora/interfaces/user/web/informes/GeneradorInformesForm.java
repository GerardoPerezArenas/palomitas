package es.altia.agora.interfaces.user.web.informes;

import es.altia.agora.business.geninformes.GeneradorInformesValueObject;
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
public class GeneradorInformesForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(GeneradorInformesForm.class.getName());

    //Reutilizamos
    GeneradorInformesValueObject genInfVO = new GeneradorInformesValueObject();

    public GeneradorInformesValueObject getGeneradorInformes() {
        return genInfVO;
    }

    public void setGeneradorInformes(GeneradorInformesValueObject genInfVO) {
        this.genInfVO = genInfVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */
    
    public Vector getListaEntidades() {
    	return genInfVO.getListaEntidades();
    }
    
    public void setListaEntidades(Vector listaEntidades) {
    	genInfVO.setListaEntidades(listaEntidades);
    }
    
    public Vector getListaCamposDisponibles() {
    	return genInfVO.getListaCamposDisponibles();
    }
    
    public void setListaCamposDisponibles(Vector listaCamposDisponibles) {
    	genInfVO.setListaCamposDisponibles(listaCamposDisponibles);
    }
    
    public String getCodAplicacion() {
    	return genInfVO.getCodAplicacion();
    }
    
    public void setCodAplicacion(String codAplicacion) {
    	genInfVO.setCodAplicacion(codAplicacion);
    }
    
    public String getOperacion() {
    	return genInfVO.getOperacion();
    }
    
    public void setOperacion(String operacion) {
    	genInfVO.setOperacion(operacion);
    }
    
    public GeneralValueObject getGVO() {
    	return genInfVO.getGVO();
    }
    
    public void setGVO(GeneralValueObject gVO) {
    	genInfVO.setGVO(gVO);
    }
    
    
    

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            genInfVO.validate(idioma);
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

