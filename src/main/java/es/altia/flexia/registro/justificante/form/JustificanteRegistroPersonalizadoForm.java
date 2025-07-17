package es.altia.flexia.registro.justificante.form;

import es.altia.flexia.registro.justificante.vo.JustificanteRegistroPersonalizadoVO;
import java.util.ArrayList;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class JustificanteRegistroPersonalizadoForm extends ActionForm{

    private ArrayList<JustificanteRegistroPersonalizadoVO> justificantes = null;
    private String nombreJustificante;
    private String descripcionJustificante;    
    private FormFile fichero;
    
    private int codigoError;
    
    // #239565: lista de informes de tipo 'peticion' 
    private ArrayList<JustificanteRegistroPersonalizadoVO> modelos = null;

    /**
     * @return the justificantes
     */
    public ArrayList<JustificanteRegistroPersonalizadoVO> getJustificantes() {
        return justificantes;
    }

    /**
     * @param justificantes the justificantes to set
     */
    public void setJustificantes(ArrayList<JustificanteRegistroPersonalizadoVO> justificantes) {
        this.justificantes = justificantes;
    }

    /**
     * @return the codigoError
     */
    public int getCodigoError() {
        return codigoError;
    }

    /**
     * @param codigoError the codigoError to set
     */
    public void setCodigoError(int codigoError) {
        this.codigoError = codigoError;
    }

    /**
     * @return the nombreJustificante
     */
    public String getNombreJustificante() {
        return nombreJustificante;
    }

    /**
     * @param nombreJustificante the nombreJustificante to set
     */
    public void setNombreJustificante(String nombreJustificante) {
        this.nombreJustificante = nombreJustificante;
    }

    /**
     * @return the descripcionJustificante
     */
    public String getDescripcionJustificante() {
        return descripcionJustificante;
    }

    /**
     * @param descripcionJustificante the descripcionJustificante to set
     */
    public void setDescripcionJustificante(String descripcionJustificante) {
        this.descripcionJustificante = descripcionJustificante;
    }

    /**
     * @return the fichero
     */
    public FormFile getFichero() {
        return fichero;
    }

    /**
     * @param fichero the fichero to set
     */
    public void setFichero(FormFile fichero) {
        this.fichero = fichero;
    }

    /**
     * @return the modelos
     */
    public ArrayList<JustificanteRegistroPersonalizadoVO> getModelos() {
        return modelos;
    }

    /**
     * @param modelos the modelos to set
     */
    public void setModelos(ArrayList<JustificanteRegistroPersonalizadoVO> modelos) {
        this.modelos = modelos;
    }
    
}