package es.altia.agora.business.sge;

import java.util.ArrayList;

/**
 *
 * @author oscar.rodriguez
 */
public class ExistenciaUorImportacionTramiteVO {

    private String nombreTramite;
    private String codigoTramite;
    private String codigoUorInicioManual;
    private String descripcionUorInicioManual;
    private boolean obligatorioUorInicioManual = false;
    private ArrayList<ExistenciaUorImportacionVO> uorsExisten = new ArrayList<ExistenciaUorImportacionVO>();
    private ArrayList<ExistenciaUorImportacionVO> uorsNoExisten = new ArrayList<ExistenciaUorImportacionVO>();
    private ArrayList<ExistenciaUorImportacionVO> uorsManualesExisten = new ArrayList<ExistenciaUorImportacionVO>();
    private boolean existen = false;

    /**
     * @return the codigoTramite
     */
    public String getCodigoTramite() {
        return codigoTramite;
    }

    /**
     * @param codigoTramite the codigoTramite to set
     */
    public void setCodigoTramite(String codigoTramite) {
        this.codigoTramite = codigoTramite;
    }

    /**
     * @return the codigoUorInicioManual
     */
    public String getCodigoUorInicioManual() {
        return codigoUorInicioManual;
    }

    /**
     * @param codigoUorInicioManual the codigoUorInicioManual to set
     */
    public void setCodigoUorInicioManual(String codigoUorInicioManual) {
        this.codigoUorInicioManual = codigoUorInicioManual;
    }

    /**
     * @return the descripcionUorInicioManual
     */
    public String getDescripcionUorInicioManual() {
        return descripcionUorInicioManual;
    }

    /**
     * @param descripcionUorInicioManual the descripcionUorInicioManual to set
     */
    public void setDescripcionUorInicioManual(String descripcionUorInicioManual) {
        this.descripcionUorInicioManual = descripcionUorInicioManual;
    }

    /**
     * @return the uorsExisten
     */
    public ArrayList<ExistenciaUorImportacionVO> getUorsExisten() {
        return uorsExisten;
    }

    /**
     * @param uorsExisten the uorsExisten to set
     */
    public void setUorsExisten(ArrayList<ExistenciaUorImportacionVO> uorsExisten) {
        this.uorsExisten = uorsExisten;
    }

    /**
     * @return the uorsNoExisten
     */
    public ArrayList<ExistenciaUorImportacionVO> getUorsNoExisten() {
        return uorsNoExisten;
    }

    /**
     * @param uorsNoExisten the uorsNoExisten to set
     */
    public void setUorsNoExisten(ArrayList<ExistenciaUorImportacionVO> uorsNoExisten) {
        this.uorsNoExisten = uorsNoExisten;
    }

    /**
     * @return the nombreTramite
     */
    public String getNombreTramite() {
        return nombreTramite;
    }

    /**
     * @param nombreTramite the nombreTramite to set
     */
    public void setNombreTramite(String nombreTramite) {
        this.nombreTramite = nombreTramite;
    }

    /**
     * @return the obligatorioUorInicioManual
     */
    public boolean isObligatorioUorInicioManual() {
        return obligatorioUorInicioManual;
    }

    /**
     * @param obligatorioUorInicioManual the obligatorioUorInicioManual to set
     */
    public void setObligatorioUorInicioManual(boolean obligatorioUorInicioManual) {
        this.obligatorioUorInicioManual = obligatorioUorInicioManual;
    }

    /**
     * @return the uorsManualesExisten
     */
    public ArrayList<ExistenciaUorImportacionVO> getUorsManualesExisten() {
        return uorsManualesExisten;
    }

    /**
     * @param uorsManualesExisten the uorsManualesExisten to set
     */
    public void setUorsManualesExisten(ArrayList<ExistenciaUorImportacionVO> uorsManualesExisten) {
        this.uorsManualesExisten = uorsManualesExisten;
    }

    /**
     * @return the existen
     */
    public boolean isExisten() {
        return existen;
    }

    /**
     * @param existen the existen to set
     */
    public void setExisten(boolean existen) {
        this.existen = existen;
    }
   

}