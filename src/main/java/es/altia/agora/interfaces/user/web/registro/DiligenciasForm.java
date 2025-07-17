// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.registro;

// PAQUETES IMPORTADOS
import es.altia.agora.business.registro.DiligenciasValueObject;
import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class DiligenciasForm extends org.apache.struts.action.ActionForm {

  //Queremos usar el fichero de configuración technical
  protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
  //Necesitamos el servicio de log
  protected static Log m_Log =
          LogFactory.getLog(DiligenciasForm.class.getName());
    
  //Reutilizamos
  DiligenciasValueObject laDiligVO = new DiligenciasValueObject();
  int correcto;

  public void setDiligencia(DiligenciasValueObject DiligVO){
    laDiligVO=DiligVO;
  }
  public DiligenciasValueObject getDiligencia(){
    return laDiligVO;
  }
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
    m_Log.debug("validate");
    ActionErrors errors = new ActionErrors();
    //RegistroEntradaValueObject hara el trabajo para nostros ...
    try {
      laDiligVO.validate(idioma);
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

  public String getFecha() {
    return laDiligVO.getFecha();
  }

  public void setFecha(String fecha) {
    laDiligVO.setFecha(fecha);
  }

  public String getAnotacion() {
    return laDiligVO.getAnotacion();
  }

  public void setAnotacion(String anotacion) {
    laDiligVO.setAnotacion(anotacion);
  }
  
  public String getFechaBuscada() {
    return laDiligVO.getFechaBuscada();
  }

  public void setFechaBuscada(String fechaBuscada) {
    laDiligVO.setFechaBuscada(fechaBuscada);
  }

  public String getAnotacionBuscada() {
    return laDiligVO.getAnotacionBuscada();
  }

  public void setAnotacionBuscada(String anotacionBuscada) {
    laDiligVO.setAnotacionBuscada(anotacionBuscada);
  }

  public void setCorrecto(int co){
    correcto = co;
  }

  public int getCorrecto(){
    return correcto;
  }
  
  public char getTipo(){
    return laDiligVO.getTipo();
  }

  public void setTipo(char tipo){
    laDiligVO.setTipo(tipo);
  }
  
  public void setListaDiligencias(Vector listaDiligencias){
    laDiligVO.setListaDiligencias(listaDiligencias);
  }

  public Vector getListaDiligencias(){
    return laDiligVO.getListaDiligencias();
  }
}