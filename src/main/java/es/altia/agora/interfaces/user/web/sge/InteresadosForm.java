// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.sge;

// PAQUETES IMPORTADOS
import es.altia.agora.business.sge.InteresadoExpedienteVO;
import es.altia.agora.business.util.GeneralValueObject;

import java.util.Vector;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class InteresadosForm extends ActionForm {

  Vector<InteresadoExpedienteVO> listaInteresados = new Vector<InteresadoExpedienteVO>();
  Vector listaRoles = new Vector();
  GeneralValueObject expTerVO = new GeneralValueObject();

  public Vector<InteresadoExpedienteVO> getListaInteresados() { return listaInteresados; }
  public void setListaInteresados(Vector<InteresadoExpedienteVO> listaInteresados) { this.listaInteresados = listaInteresados; }

  public Vector getListaRoles() { return listaRoles; }
  public void setListaRoles(Vector listaRoles) { this.listaRoles = listaRoles; }

  public GeneralValueObject getExpTerVO() { return expTerVO; }
  public void setExpTerVO(GeneralValueObject expTerVO) { this.expTerVO = expTerVO; }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    return super.validate(mapping, request);
  }

}