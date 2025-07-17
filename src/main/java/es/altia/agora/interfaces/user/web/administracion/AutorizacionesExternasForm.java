package es.altia.agora.interfaces.user.web.administracion;

import es.altia.agora.business.administracion.AutorizacionesExternasValueObject;
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


/** Clase utilizada para capturar o mostrar el estado de una Tramitacion */
public class AutorizacionesExternasForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(AutorizacionesExternasForm.class.getName());

    //Reutilizamos
    AutorizacionesExternasValueObject aeVO = new AutorizacionesExternasValueObject();

    public AutorizacionesExternasValueObject getAutorizacionesExternas() {
        return aeVO;
    }

    public void setAutorizacionesExternas(AutorizacionesExternasValueObject aeVO) {
        this.aeVO = aeVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

	public void setRespOpcion(String respOpcion) {
		aeVO.setRespOpcion(respOpcion); 
	}

	public String getRespOpcion() {
		return aeVO.getRespOpcion(); 
	}

	public Vector getListaComboAplicaciones() {
		return aeVO.getListaComboAplicaciones(); 
	}

	public Vector getListaEntidades() {
		return aeVO.getListaEntidades(); 
	}

	public Vector getListaUsuarios() {
		return aeVO.getListaUsuarios(); 
	}

	public Vector getListaAplicaciones() {
		return aeVO.getListaAplicaciones(); 
	}

	public Vector getListaEnt() {
		return aeVO.getListaEnt(); 
	}
	
	public String getCodAplicacion() {
		return aeVO.getCodAplicacion(); 
	}

	public String getCodOrganizacion() {
		return aeVO.getCodOrganizacion(); 
	}

	public String getCodEntidad() {
		return aeVO.getCodEntidad(); 
	}
	public String getNombreOrganizacion() {
		return aeVO.getNombreOrganizacion(); 
	}

	public String getNombreEntidad() {
		return aeVO.getNombreEntidad(); 
	}
	
	public String getAutorizacion() {
		return aeVO.getAutorizacion(); 
	}
	
	public String getCodUsuario() {
		return aeVO.getCodUsuario(); 
	}
	
	public String getNombreAplicacion() {
		return aeVO.getNombreAplicacion(); 
	}
	public String getBaseDeDatos() {
		return aeVO.getBaseDeDatos(); 
	}
	public void setBaseDeDatos(String baseDeDatos) {
		aeVO.setBaseDeDatos(baseDeDatos); 
	}
    
  

    

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            aeVO.validate(idioma);
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
