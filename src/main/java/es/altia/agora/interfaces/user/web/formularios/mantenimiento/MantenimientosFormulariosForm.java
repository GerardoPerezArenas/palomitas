// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.formularios.mantenimiento;

// PAQUETES IMPORTADOS
import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import es.altia.agora.business.util.GeneralValueObject;

public class MantenimientosFormulariosForm extends ActionForm  {
  GeneralValueObject otrosDatos = new GeneralValueObject();
  String operacion = "";
  String ventana = "false";
  Vector listaTiposFormularios = new Vector();
  Vector listaEstadosFormularios = new Vector();

  public Vector getListaEstadosFormularios() {
      return listaEstadosFormularios;
  }

  public void setListaEstadosFormularios(Vector listaEstadosFormularios) {
      this.listaEstadosFormularios = listaEstadosFormularios;
  }

  public Vector getListaTiposFormularios() {
      return listaTiposFormularios;
  }

  public void setListaTiposFormularios(Vector listaTiposFormularios) {
      this.listaTiposFormularios = listaTiposFormularios;
  }

  public GeneralValueObject getOtrosDatos() {
    return otrosDatos;
  }

  public void setOtrosDatos(GeneralValueObject newOtrosDatos) {
    otrosDatos = newOtrosDatos;
  }
  public String getOperacion() {
    return operacion;
  }

  public void setOperacion(String newOperacion) {
    if(newOperacion!=null)
      operacion = newOperacion;
    else
      operacion = "";
  }

  public String getVentana() {
    return ventana;
  }

  public void setVentana(String newVentana) {
    if(newVentana!=null)
      ventana = newVentana;
    else
      ventana = "false";
  }

}