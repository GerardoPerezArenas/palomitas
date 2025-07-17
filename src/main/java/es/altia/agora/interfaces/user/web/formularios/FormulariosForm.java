// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.formularios;

// PAQUETES IMPORTADOS
import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import es.altia.agora.business.util.GeneralValueObject;

public class FormulariosForm extends ActionForm  {
  GeneralValueObject otrosDatos = new GeneralValueObject();
  Vector listaFormularios = new Vector();
  Vector listaTipos = new Vector();
  Vector listaProcedimientos = new Vector();

    public Vector getListaFormularios() {
        return listaFormularios;
    }

    public void setListaFormularios(Vector listaFormularios) {
        this.listaFormularios = listaFormularios;
    }

    public Vector getListaFormulariosTipo0() {
        return listaFormularios;
    }

    public void setListaFormulariosTipo0(Vector listaFormularios) {
        this.listaFormularios = listaFormularios;
    }

    public Vector getListaFormulariosSinParametros() {
        return listaFormularios;
    }

    public void setListaFormulariosSinParametros(Vector listaFormularios) {
        this.listaFormularios = listaFormularios;
    }

    public Vector getListaTipos() {
        return listaTipos;
    }

    public void setListaTipos(Vector listaTipos) {
        this.listaTipos=listaTipos;
    }

    public Vector getListaProcedimientos() {
        return listaProcedimientos;
    }

    public void setListaProcedimientos(Vector listaProcedimientos) {
        this.listaProcedimientos=listaProcedimientos;
    }

}