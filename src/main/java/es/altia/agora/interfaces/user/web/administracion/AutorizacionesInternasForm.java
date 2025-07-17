// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.administracion;

// PAQUETES IMPORTADOS
import java.util.Vector;
import es.altia.agora.business.util.GeneralValueObject;
import javax.servlet.http.*;
import org.apache.struts.action.*;

public class AutorizacionesInternasForm extends ActionForm  {
  Vector listaOrganizaciones = new Vector();
  Vector listaEntidades = new Vector();
  Vector listaAplicaciones = new Vector();
  Vector listaMenus = new Vector();
  Vector listaGrupos = new Vector();
  Vector listaUsuarios = new Vector();
  
  GeneralValueObject otrosDatos = new GeneralValueObject();

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    return super.validate(mapping, request);
  }

  public Vector getListaEntidades() {
    return listaEntidades;
  }

  public void setListaEntidades(Vector newListaEntidades) {
    listaEntidades = newListaEntidades;
  }

  public Vector getListaOrganizaciones() {
    return listaOrganizaciones;
  }

  public void setListaOrganizaciones(Vector newListaOrganizaciones) {
    listaOrganizaciones = newListaOrganizaciones;
  }

  public Vector getListaMenus() {
    return listaMenus;
  }

  public void setListaMenus(Vector newListaMenus) {
    listaMenus = newListaMenus;
  }

  public Vector getListaGrupos() {
    return listaGrupos;
  }

  public void setListaGrupos(Vector newListaGrupos) {
    listaGrupos = newListaGrupos;
  }

  public Vector getListaUsuarios() {
    return listaUsuarios;
  }

  public void setListaUsuarios(Vector newListaUsuarios) {
    listaUsuarios = newListaUsuarios;
  }
  public GeneralValueObject getOtrosDatos() {
    return otrosDatos;
  }

  public void setOtrosDatos(GeneralValueObject newOtrosDatos) {
    otrosDatos = newOtrosDatos;
  }


  public Vector getListaAplicaciones() {
    return listaAplicaciones;
  }

  public void setListaAplicaciones(Vector newListaAplicaciones) {
    listaAplicaciones = newListaAplicaciones;
  }

}