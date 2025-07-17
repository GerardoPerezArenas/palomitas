// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.formularios.mantenimiento;

// PAQUETES IMPORTADOS
import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import es.altia.agora.business.util.GeneralValueObject;

public class MantenimientoTiposForm extends ActionForm  {
  String codigoAntiguo= "";
  String codigo = "";
  String descripcion = "";

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoAntiguo() {
        return codigoAntiguo;
    }

    public void setCodigoAntiguo(String codigoAntiguo) {
        this.codigoAntiguo = codigoAntiguo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}