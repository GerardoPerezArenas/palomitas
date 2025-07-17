package es.altia.agora.business.administracion.mantenimiento;

import java.io.Serializable;


public class CriterioBusquedaPendientesVO implements Serializable{
    private String codigo;
    private String nombre;
    private String tipoCampoFijo;
    private boolean campoSuplementario;
    private String tipoCampoSuplementario;
    private String codigoDesplegable;


    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the campoSuplementario
     */
    public boolean isCampoSuplementario() {
        return campoSuplementario;
    }

    /**
     * @param campoSuplementario the campoSuplementario to set
     */
    public void setCampoSuplementario(boolean campoSuplementario) {
        this.campoSuplementario = campoSuplementario;
    }

    /**
     * @return the tipoCampoSuplementario
     */
    public String getTipoCampoSuplementario() {
        return tipoCampoSuplementario;
    }

    /**
     * @param tipoCampoSuplementario the tipoCampoSuplementario to set
     */
    public void setTipoCampoSuplementario(String tipoCampoSuplementario) {
        this.tipoCampoSuplementario = tipoCampoSuplementario;
    }

    /**
     * @return the tipoCampoFijo
     */
    public String getTipoCampoFijo() {
        return tipoCampoFijo;
    }

    /**
     * @param tipoCampoFijo the tipoCampoFijo to set
     */
    public void setTipoCampoFijo(String tipoCampoFijo) {
        this.tipoCampoFijo = tipoCampoFijo;
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
 
    
}