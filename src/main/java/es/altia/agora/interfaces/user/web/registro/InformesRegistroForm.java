package es.altia.agora.interfaces.user.web.registro;
// PAQUETES IMPORTADOS
import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;
import es.altia.agora.business.registro.RegistroValueObject;


import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.*;

/**
 * Título: Proyecto @gora
 * Empresa: Altia
 *
 */
public class InformesRegistroForm extends org.apache.struts.action.ActionForm {

  //Queremos usar el fichero de configuración technical
  protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
  //Necesitamos el servicio de log
  protected Log m_Log = LogFactory.getLog(this.getClass().getName());
  //Reutilizamos
  Vector estadisticas= new Vector();
  
   private Vector<RegistroValueObject> anotacionesLibro; 
  private  RegistroValueObject registroVO = new RegistroValueObject();

    public Vector<RegistroValueObject> getAnotacionesLibro() {
        return anotacionesLibro;
    }
    
    public void setAnotacionesLibro(Vector<RegistroValueObject> anotacionesLibro) {
        this.anotacionesLibro = anotacionesLibro;
    }
   
    public Long getNumReg(){
        return registroVO.getNumReg();
    }
     
    public void setNumReg (Long numReg){
        registroVO.setNumReg(numReg);
    }
    public String getFecEntrada(){
        return registroVO.getFecEntrada();
    }
    
    public void setFecEntrada(String fecha){
        registroVO.setFecEntrada(fecha);
    }
    public String getAsunto(){
        return registroVO.getAsunto();
    }
    public void setAsunto(String asunto){
        registroVO.setAsunto(asunto);
    }
    public String getNombre(){
        return registroVO.getNombreInteresado();
    }
    public void setNombre(String nombre){
        registroVO.setNombreInteresado(nombre);
    }
    public String getApellido1Interesado(){
        return registroVO.getApellido1Interesado();
    }
    public void setApellido1Interesado(String apellido1){
        registroVO.setApellido1Interesado(apellido1);
    }
    public String getApellido2Interesado(){
        return registroVO.getApellido2Interesado();
    }
    public String getNomCompleto(){
        return registroVO.getNomCompletoInteresado();
    }
    public String getOrganizacionOrigen(){
        return registroVO.getOrganizacionOrigen();
    }
    public void setOrganizacionOrigen(String destino){
        registroVO.setOrganizacionOrigen(destino);
    }

  public void setEstadisticas(Vector e){
	this.estadisticas=e;
  }
  public Vector getEstadisticas(){
	return this.estadisticas;
  }


  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
	  m_Log.debug("validate");
	  ActionErrors errors = new ActionErrors();	  
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
