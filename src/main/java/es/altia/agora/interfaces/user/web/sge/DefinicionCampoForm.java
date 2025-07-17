package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.DefinicionCampoValueObject;
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
public class DefinicionCampoForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(DefinicionCampoForm.class.getName());

    //Reutilizamos
    DefinicionCampoValueObject defCampVO = new DefinicionCampoValueObject();

    public DefinicionCampoValueObject getDefinicionCampo() {
        return defCampVO;
    }

    public void setDefinicionCampo(DefinicionCampoValueObject defCampVO) {
        this.defCampVO = defCampVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

public String getCodCampo() {
  return defCampVO.getCodCampo();
}
public void setCodCampo(String codCampo) {
  defCampVO.setCodCampo(codCampo);
}
public String getCodPlantilla() {
  return defCampVO.getCodPlantilla();
}
public void setCodPlantilla(String codPlantilla) {
  defCampVO.setCodPlantilla(codPlantilla);
}
public String getCodTipoDato() {
  return defCampVO.getCodTipoDato();
}
public void setCodTipoDato(String codTipoDato) {
  defCampVO.setCodTipoDato(codTipoDato);
}
public String getDescCampo() {
  return defCampVO.getDescCampo();
}
public void setDescCampo(String descCampo) {
  defCampVO.setDescCampo(descCampo);
}
public String getDescPlantilla() {
  return defCampVO.getDescPlantilla();
}
public void setDescPlantilla(String descPlantilla) {
  defCampVO.setDescPlantilla(descPlantilla);
}
public String getDescTipoDato() {
  return defCampVO.getDescTipoDato();
}
public void setDescTipoDato(String descTipoDato) {
  defCampVO.setDescTipoDato(descTipoDato);
}
public String getDescMascara() {
  return defCampVO.getDescMascara();
}
public void setDescMascara(String descMascara) {
  defCampVO.setDescMascara(descMascara);
}
public String getObligat() {
  return defCampVO.getObligat();
}
public void setObligat(String obligat) {
  defCampVO.setObligat(obligat);
}
public String getOrden() {
  return defCampVO.getOrden();
}
public void setOrden(String orden) {
  defCampVO.setOrden(orden);
}
public String getTamano() {
  return defCampVO.getTamano();
}
public void setTamano(String tamano) {
  defCampVO.setTamano(tamano);
}
public String getVisible() {
  return defCampVO.getVisible();
}
public void setVisible(String visible) {
  defCampVO.setVisible(visible);
}
public String getRotulo() {
  return defCampVO.getRotulo();
}
public void setRotulo(String rotulo) {
  defCampVO.setRotulo(rotulo);
}

public String getOculto() {
  return defCampVO.getOculto();
}
public void setOculto(String oculto) {
  defCampVO.setOculto(oculto);
}

public String getBloqueado() {
  return defCampVO.getBloqueado();
}
public void setBloqueado(String bloqueado) {
  defCampVO.setBloqueado(bloqueado);
}

public String getPlazoFecha() {
  return defCampVO.getPlazoFecha();
}
public void setPlazoFecha(String plazoFecha) {
  defCampVO.setPlazoFecha(plazoFecha);
}

public String getCheckPlazoFecha(){
    return defCampVO.getCheckPlazoFecha();
}

public void setCheckPlazoFecha(String checkPlazoFecha){
    defCampVO.setCheckPlazoFecha(checkPlazoFecha);
}
public String getValidacion(){
    return defCampVO.getValidacion();
}
public void setValidacion(String texto){
    defCampVO.setValidacion(texto);
}
public String getOperacion(){
    return defCampVO.getOperacion(); 
}
public void setOperacion(String texto){
    defCampVO.setOperacion(texto);
}

public String getCodAgrupacion() {
    return defCampVO.getCodAgrupacion();
}
public void setCodAgrupacion(String codAgrupacion) {
    defCampVO.setCodAgrupacion(codAgrupacion);
}
 
public String getDescAgrupacion() {
    return defCampVO.getDescAgrupacion();
}
public void setDescAgrupacion(String descAgrupacion) {
    defCampVO.setDescAgrupacion(descAgrupacion);
}

public Vector getListaPlantillas() {
  return defCampVO.getListaPlantillas();
}
public void setListaPlantillas(Vector listaPlantillas) {
  defCampVO.setListaPlantillas(listaPlantillas);
}
public Vector getListaTipoDato() {
  return defCampVO.getListaTipoDato();
}
public void setListaTipoDato(Vector listaTipoDato) {
  defCampVO.setListaTipoDato(listaTipoDato);
}
public Vector getListaRelacionTipoDatoPlantillas() {
  return defCampVO.getListaRelacionTipoDatoPlantillas();
}
public Vector getListaMascaras() {
  return defCampVO.getListaMascaras();
}
public Vector getListaRelacionTipoDatoMascaras() {
  return defCampVO.getListaRelacionTipoDatoMascaras();
}

public Vector getListaCamposDesplegables() {
  return defCampVO.getListaCamposDesplegables();
}
public Vector getListaCampos() {
  return defCampVO.getListaCampos();
}

public void setListaCampos(Vector listaCampos) {
  defCampVO.setListaCampos(listaCampos); 
}

public void setListaCamposDesplegables(Vector listaCamposDesplegables) {
  defCampVO.setListaCamposDesplegables(listaCamposDesplegables);
}

public Vector getListaAgrupaciones() {
    return defCampVO.getListaAgrupaciones();
}
public void setListaAgrupaciones(Vector listaAgrupaciones) {
    defCampVO.setListaAgrupaciones(listaAgrupaciones);
}

public Vector getListaCamposDesplegablesExterno() {
  return defCampVO.getListaCamposDesplegablesExterno();
}

public void setListaCamposDesplegablesExterno(Vector listaCamposDesplegablesExterno) {
  defCampVO.setListaCamposDesplegablesExterno(listaCamposDesplegablesExterno);
}
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
              Config m_Conf = ConfigServiceHelper.getConfig("common");
              String campo="";
              String codPlantilla = request.getParameter("codPlantilla");
              m_Log.debug("COD PLANTILLA " + codPlantilla);
              if ((codPlantilla.equals("1")) || (codPlantilla.equals("3"))) {
                  campo = "TamMaximo.CampoTexto";
              } else if (codPlantilla.equals("2")) {
                  campo = "TamMaximo.InputCampoNumerico";
              }
              if (!campo.equals("")) {
                Integer tamanoMaximo = new Integer(m_Conf.getString(campo));
                Integer tamanoCampo = new Integer(getTamano());
                if (tamanoCampo.intValue()>tamanoMaximo.intValue()) {
                  request.setAttribute("tamanoMaximo",tamanoMaximo);
                  errors.add("tamano", new ActionError("msjTamMaximoCampo"));
                }
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
