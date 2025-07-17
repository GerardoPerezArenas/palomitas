package es.altia.agora.business.sge;

import java.util.ArrayList;


public class ResultadoAltaCampoProcedimientoBibliotecaVO {
    private int estado;
    private ArrayList<DefinicionAgrupacionCamposValueObject> noInsertado = null;

    /**
     * @return the estado
     */
    public int getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(int estado) {
        this.estado = estado;
    }

    /**
     * @return the noInsertado
     */
    public ArrayList<DefinicionAgrupacionCamposValueObject> getNoInsertado() {
        return noInsertado;
    }

    /**
     * @param noInsertado the noInsertado to set
     */
    public void setNoInsertado(ArrayList<DefinicionAgrupacionCamposValueObject> noInsertado) {
        this.noInsertado = noInsertado;
    }
    
}
