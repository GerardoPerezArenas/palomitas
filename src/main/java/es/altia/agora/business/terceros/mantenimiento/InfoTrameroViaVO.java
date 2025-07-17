package es.altia.agora.business.terceros.mantenimiento;

public class InfoTrameroViaVO {

    private int codigoECO;
    private String descECO;
    private int codigoESI;
    private String descESI;
    private int codigoNUC;
    private String descNUC;
    private int primerNumero;
    private String primeraLetra;
    private int ultimoNumero;
    private String ultimaLetra;

    public InfoTrameroViaVO() {
        this.codigoECO = -1;
        this.descECO = null;
        this.codigoESI = -1;
        this.descESI = null;
        this.codigoNUC = -1;
        this.descNUC = null;
        this.primerNumero = -1;
        this.primeraLetra = null;
        this.ultimoNumero = -1;
        this.ultimaLetra = null;
    }

    public InfoTrameroViaVO(int codigoECO, String descECO, int codigoESI, String descESI, int codigoNUC, String descNUC,
                            int primerNumero, String primeraLetra, int ultimoNumero, String ultimaLetra) {
        this.codigoECO = codigoECO;
        this.descECO = descECO;
        this.codigoESI = codigoESI;
        this.descESI = descESI;
        this.codigoNUC = codigoNUC;
        this.descNUC = descNUC;
        this.primerNumero = primerNumero;
        this.primeraLetra = primeraLetra;
        this.ultimoNumero = ultimoNumero;
        this.ultimaLetra = ultimaLetra;
    }

    public int getCodigoECO() {
        return codigoECO;
    }

    public void setCodigoECO(int codigoECO) {
        this.codigoECO = codigoECO;
    }

    public String getDescECO() {
        return descECO;
    }

    public void setDescECO(String descECO) {
        this.descECO = descECO;
    }

    public int getCodigoESI() {
        return codigoESI;
    }

    public void setCodigoESI(int codigoESI) {
        this.codigoESI = codigoESI;
    }

    public String getDescESI() {
        return descESI;
    }

    public void setDescESI(String descESI) {
        this.descESI = descESI;
    }

    public int getCodigoNUC() {
        return codigoNUC;
    }

    public void setCodigoNUC(int codigoNUC) {
        this.codigoNUC = codigoNUC;
    }

    public String getDescNUC() {
        return descNUC;
    }

    public void setDescNUC(String descNUC) {
        this.descNUC = descNUC;
    }

    public int getPrimerNumero() {
        return primerNumero;
    }

    public void setPrimerNumero(int primerNumero) {
        this.primerNumero = primerNumero;
    }

    public String getPrimeraLetra() {
        return primeraLetra;
    }

    public void setPrimeraLetra(String primeraLetra) {
        this.primeraLetra = primeraLetra;
    }

    public int getUltimoNumero() {
        return ultimoNumero;
    }

    public void setUltimoNumero(int ultimoNumero) {
        this.ultimoNumero = ultimoNumero;
    }

    public String getUltimaLetra() {
        return ultimaLetra;
    }

    public void setUltimaLetra(String ultimaLetra) {
        this.ultimaLetra = ultimaLetra;
    }
}
