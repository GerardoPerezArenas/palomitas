package es.altia.agora.interfaces.user.web.util.combo;

import java.util.Vector;

/**
 *
 * @author ivan.perez
 */
public class ComboCVO {
 private String nombreCombo;
 private Vector<ElementoComboCVO> elementosCombo;
 
    public String getNombreCombo() {
        return nombreCombo;
    }

    public Vector getElementosCombo() {
        return elementosCombo;
    }

    public void setElementosCombo(Vector elementosCombo) {
        this.elementosCombo = elementosCombo;
    }

    public void setNombreCombo(String nombreCombo) {
        this.nombreCombo = nombreCombo;
    }
}
