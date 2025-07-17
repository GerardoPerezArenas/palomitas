package es.altia.agora.interfaces.user.web.util.combo;

/**
 *
 * @author ivan.perez
 */
public class ElementoComboCVO {
    private String codigoElemento;
    private String descripcionElemento;
    private String codigoInternoElemento;

    public String getCodigoElemento() {
        return codigoElemento;
    }

    public void setCodigoElemento(String codigoElemento) {
        this.codigoElemento = codigoElemento;
    }

    public String getCodigoInternoElemento() {
        return codigoInternoElemento;
    }

    public void setCodigoInternoElemento(String codigoInternoElemento) {
        this.codigoInternoElemento = codigoInternoElemento;
    }

    public String getDescripcionElemento() {
        return descripcionElemento;
    }

    public void setDescripcionElemento(String descripcionElemento) {
        this.descripcionElemento = descripcionElemento;
    }
    
    @Override
    public String toString() {
        String s = "";
        s = "Codigo Elemento: " + this.codigoElemento + "\n";
        s += "Descripcion Elemento: " + this.descripcionElemento + "\n";
        s += "Codigo Interno Elemento: " + this.codigoInternoElemento + "\n\n";
        return s;
    }
}
