package es.altia.agora.interfaces.user.web.informes;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.*;
import es.altia.agora.business.informes.InformesValueObject;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author sin atribuir
 * @version 1.0
 */

public class InformesForm extends ActionForm{

   protected static Log log =
           LogFactory.getLog(InformesForm.class.getName());
    
   private Vector estadisticas;
   private String tipoSalida;

   InformesValueObject iVO = new InformesValueObject();

   public void setEstadisticas(Vector estadisticas) {
	this.estadisticas = estadisticas;
   }

   public Vector getEstadisticas() {
	return estadisticas;
   }

   public void setTipoSalida (String tipoSalida)
   {
     this.tipoSalida = tipoSalida;
   }

   public String getTipoSalida ()
   {
     return tipoSalida;
   }

   public void setInformesVO(InformesValueObject iVO){
     this.iVO = iVO;
   }

   public InformesValueObject getInformesVO(){
     return iVO;
   }

   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
	log.debug("validate");
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