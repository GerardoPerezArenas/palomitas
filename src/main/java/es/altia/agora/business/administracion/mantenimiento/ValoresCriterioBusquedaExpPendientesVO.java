package es.altia.agora.business.administracion.mantenimiento;

import java.util.ArrayList;


public class ValoresCriterioBusquedaExpPendientesVO {

    private String codigoCriterioBusqueda;
    private String tipoCampoCriterioBusqueda;
    private int operadorCriterioBusqueda;
    private ArrayList<String> valoresCriterioBusqueda;
    private boolean campoSuplementarioCriterioBusqueda;
    private String tipoCampoSuplementarioCriterioBusqueda;
    private String codigoDesplegable;

    /**
     * @return the codigoCriterioBusqueda
     */
    public String getCodigoCriterioBusqueda() {
        return codigoCriterioBusqueda;
    }

    /**
     * @param codigoCriterioBusqueda the codigoCriterioBusqueda to set
     */
    public void setCodigoCriterioBusqueda(String codigoCriterioBusqueda) {
        this.codigoCriterioBusqueda = codigoCriterioBusqueda;
    }

    /**
     * @return the tipoCampoCriterioBusqueda
     */
    public String getTipoCampoCriterioBusqueda() {
        return tipoCampoCriterioBusqueda;
    }

    /**
     * @param tipoCampoCriterioBusqueda the tipoCampoCriterioBusqueda to set
     */
    public void setTipoCampoCriterioBusqueda(String tipoCampoCriterioBusqueda) {
        this.tipoCampoCriterioBusqueda = tipoCampoCriterioBusqueda;
    }

    /**
     * @return the operadorCriterioBusqueda
     */
    public int getOperadorCriterioBusqueda() {
        return operadorCriterioBusqueda;
    }

    /**
     * @param operadorCriterioBusqueda the operadorCriterioBusqueda to set
     */
    public void setOperadorCriterioBusqueda(int operadorCriterioBusqueda) {
        this.operadorCriterioBusqueda = operadorCriterioBusqueda;
    }

    /**
     * @return the valoresCriterioBusqueda
     */
    public ArrayList<String> getValoresCriterioBusqueda() {
        return valoresCriterioBusqueda;
    }

    /**
     * @param valoresCriterioBusqueda the valoresCriterioBusqueda to set
     */
    public void setValoresCriterioBusqueda(ArrayList<String> valoresCriterioBusqueda) {
        this.valoresCriterioBusqueda = valoresCriterioBusqueda;
    }

    /**
     * @return the tipoCampoSuplementarioCriterioBusqueda
     */
    public String getTipoCampoSuplementarioCriterioBusqueda() {
        return tipoCampoSuplementarioCriterioBusqueda;
    }

    /**
     * @param tipoCampoSuplementarioCriterioBusqueda the tipoCampoSuplementarioCriterioBusqueda to set
     */
    public void setTipoCampoSuplementarioCriterioBusqueda(String tipoCampoSuplementarioCriterioBusqueda) {
        this.tipoCampoSuplementarioCriterioBusqueda = tipoCampoSuplementarioCriterioBusqueda;
    }

    /**
     * @return the codigoDesplegable
     */
    public String getCodigoDesplegable() {
        return codigoDesplegable;
    }

    /**
     * @param codigoDesplegable the codigoDesplegable to set
     */
    public void setCodigoDesplegable(String codigoDesplegable) {
        this.codigoDesplegable = codigoDesplegable;
    }

    /**
     * @return the campoSuplementarioCriterioBusqueda
     */
    public boolean isCampoSuplementarioCriterioBusqueda() {
        return campoSuplementarioCriterioBusqueda;
    }

    /**
     * @param campoSuplementarioCriterioBusqueda the campoSuplementarioCriterioBusqueda to set
     */
    public void setCampoSuplementarioCriterioBusqueda(boolean campoSuplementarioCriterioBusqueda) {
        this.campoSuplementarioCriterioBusqueda = campoSuplementarioCriterioBusqueda;
    }

    public static String getOperador(int tipo){
        String operador="";
        switch(tipo){
            case 0: operador = " = ";
                    break;
            case 1: operador = " > ";
                    break;
            case 2: operador = " >= ";
                    break;
            case 3: operador = " < ";
                    break;
            case 4: operador = " <= ";
                    break;
            case 5: operador = " ENTRE ";
                    break;
            case 6: operador = " != ";
                    break;
            case 7: operador = " LIKE ";
                    break;
        }// switch
        return operador;
    }
}