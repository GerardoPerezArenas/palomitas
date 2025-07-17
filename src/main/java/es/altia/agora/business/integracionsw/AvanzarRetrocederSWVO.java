package es.altia.agora.business.integracionsw;

import java.io.Serializable;
import java.util.List;

public class AvanzarRetrocederSWVO implements Serializable {

    /**
     * @return the obligatorio
     */
    public int getObligatorio() {
        return obligatorio;
    }

    /**
     * @param obligatorio the obligatorio to set
     */
    public void setObligatorio(int obligatorio) {
        this.obligatorio = obligatorio;
    }

    /**
     * @return the codOperacion
     */
    public int getCodOperacion() {
        if(codOperacion == -1)
            return getCodigoFinal();
        else
            return codOperacion;
    }

    /**
     * @param codOperacion the codOperacion to set
     */
    public void setCodOperacion(int codOperacion) {
        this.codOperacion = codOperacion;
    }

    /**
     * Devuelve el tipo de integracion: AVANZAR,  INICIAR, RETROCEDER
     */
    public int getCodIntegracion(){
        if(codIntegracion != -1) return codIntegracion;
        else if(cfoAvanzar != -1) return 1;
        else if(cfoIniciar != -1) return 2;
        else return 0;
    }

    /**
     * @param codIntegracion the codIntegracion to set
     */
    public void setCodIntegracion(int codIntegracion) {
        this.codIntegracion = codIntegracion;
    }

    /**
     * @return the nombreModulo
     */
    public String getNombreModulo() {
        return nombreModulo;
    }

    /**
     * @param nombreModulo the nombreModulo to set
     */
    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }
	
    private int codAvanzar;
    private int codRetroceder;
    private long cfoAvanzar;
    private long cfoRetroceder;
    
    private int codIniciar;
    private long cfoIniciar;
    private int idTareaPendiente;
    private String tipoOperacion;
    private String nombreOperacion;
    private String tipoOrigenOperacion;

    private String nombreOperacionIniciar;
    private String nombreOperacionAvanzar;
    private String nombreOperacionRetroceder;

    private String tipoOperacionIniciar;
    private String tipoOperacionAvanzar;
    private String tipoOperacionRetroceder;

    private String nombreModuloIniciar;
    private String nombreModuloAvanzar;
    private String nombreModuloRetroceder;
    
    private int tipoRetroceso;
    //indica def_tra_ord /* Mila Noya 17/06/2015 */
    private int numeroOrden;
    private int obligatorio;
    private int codOperacion = -1;
    private int codIntegracion = -1;
    private String nombreModulo;
    private List<ParametroConfigurableVO> listaParametros;
	
	public long getCfoAvanzar() {
		return cfoAvanzar;
	}
	public void setCfoAvanzar(long cfoAvanzar) {
		this.cfoAvanzar = cfoAvanzar;
	}
	public long getCfoRetroceder() {
		return cfoRetroceder;
	}
	public void setCfoRetroceder(long cfoRetroceder) {
		this.cfoRetroceder = cfoRetroceder;
	}
	public int getCodAvanzar() {
		return codAvanzar;
	}
	public void setCodAvanzar(int codAvanzar) {
		this.codAvanzar = codAvanzar;
	}
	public int getCodRetroceder() {
		return codRetroceder;
	}
	public void setCodRetroceder(int codRetroceder) {
		this.codRetroceder = codRetroceder;
	}
	
    /**
     * @return the codIniciar
     */
    public int getCodIniciar() {
        return codIniciar;
    }

    /**
     * @param codIniciar the codIniciar to set
     */
    public void setCodIniciar(int codIniciar) {
        this.codIniciar = codIniciar;
    }

    /**
     * @return the cfoIniciar
     */
    public long getCfoIniciar() {
        return cfoIniciar;
    }

    /**
     * @param cfoIniciar the cfoIniciar to set
     */
    public void setCfoIniciar(long cfoIniciar) {
        this.cfoIniciar = cfoIniciar;
    }


    public String toString() {
		return ("Codigo operacion avanzar: " + codAvanzar + "\nCodigo operacion retroceder: " + codRetroceder + "\nCódigo operacion iniciar: " + codIniciar +
				"\nClave Foranea Avanzar: " + cfoAvanzar + "\nClave Foranea Retroceder : " + cfoRetroceder + "\nClave Foránea Iniciar: " + cfoIniciar);

	}

    /**
     * @return the idTareaPendiente
     */
    public int getIdTareaPendiente() {
        return idTareaPendiente;
    }

    /**
     * @param idTareaPendiente the idTareaPendiente to set
     */
    public void setIdTareaPendiente(int idTareaPendiente) {
        this.idTareaPendiente = idTareaPendiente;
    }

    /**
     * @return the tipoOperacion
     */
    public String getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * @param tipoOperacion the tipoOperacion to set
     */
    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    /**
     * @return the nombreOperacion
     */
    public String getNombreOperacion() {
        return nombreOperacion;
    }

    /**
     * @param nombreOperacion the nombreOperacion to set
     */
    public void setNombreOperacion(String nombreOperacion) {
        this.nombreOperacion = nombreOperacion;
    }

    /**
     * @return the tipoOrigenOperacion
     */
    public String getTipoOrigenOperacion() {
        return tipoOrigenOperacion;
    }

    /**
     * @param tipoOrigenOperacion the tipoOrigenOperacion to set
     */
    public void setTipoOrigenOperacion(String tipoOrigenOperacion) {
        this.tipoOrigenOperacion = tipoOrigenOperacion;
    }

    /**
     * @return the nombreOperacionIniciar
     */
    public String getNombreOperacionIniciar() {
        return nombreOperacionIniciar;
    }

    /**
     * @param nombreOperacionIniciar the nombreOperacionIniciar to set
     */
    public void setNombreOperacionIniciar(String nombreOperacionIniciar) {
        this.nombreOperacionIniciar = nombreOperacionIniciar;
    }

    /**
     * @return the nombreOperacionAvanzar
     */
    public String getNombreOperacionAvanzar() {
        return nombreOperacionAvanzar;
    }

    /**
     * @param nombreOperacionAvanzar the nombreOperacionAvanzar to set
     */
    public void setNombreOperacionAvanzar(String nombreOperacionAvanzar) {
        this.nombreOperacionAvanzar = nombreOperacionAvanzar;
    }

    /**
     * @return the nombreOperacionRetroceder
     */
    public String getNombreOperacionRetroceder() {
        return nombreOperacionRetroceder;
    }
    
    /**
     * Devuelve el nombre de la operacion teniendo en cuenta los distintos tipos de integraci?n posibles
     */
    public String getNombreOperacionFinal(){
        if(nombreOperacion != null) return nombreOperacion;
        else if(nombreOperacionAvanzar != null) return nombreOperacionAvanzar;
        else if(nombreOperacionIniciar != null) return nombreOperacionIniciar;
        else return nombreOperacionRetroceder;
    }

    /**
     * @param nombreOperacionRetroceder the nombreOperacionRetroceder to set
     */
    public void setNombreOperacionRetroceder(String nombreOperacionRetroceder) {
        this.nombreOperacionRetroceder = nombreOperacionRetroceder;
    }

    /**
     * @return the tipoOperacionIniciar
     */
    public String getTipoOperacionIniciar() {
        return tipoOperacionIniciar;
    }

    /**
     * @param tipoOperacionIniciar the tipoOperacionIniciar to set
     */
    public void setTipoOperacionIniciar(String tipoOperacionIniciar) {
        this.tipoOperacionIniciar = tipoOperacionIniciar;
    }

    /**
     * @return the tipoOperacionAvanzar
     */
    public String getTipoOperacionAvanzar() {
        return tipoOperacionAvanzar;
    }

    /**
     * @param tipoOperacionAvanzar the tipoOperacionAvanzar to set
     */
    public void setTipoOperacionAvanzar(String tipoOperacionAvanzar) {
        this.tipoOperacionAvanzar = tipoOperacionAvanzar;
    }

    /**
     * @return the tipoOperacionRetroceder
     */
    public String getTipoOperacionRetroceder() {
        return tipoOperacionRetroceder;
    }

    /**
     * @param tipoOperacionRetroceder the tipoOperacionRetroceder to set
     */
    public void setTipoOperacionRetroceder(String tipoOperacionRetroceder) {
        this.tipoOperacionRetroceder = tipoOperacionRetroceder;
    }

    /**
     * @return the nombreModuloIniciar
     */
    public String getNombreModuloIniciar() {
        return nombreModuloIniciar;
    }

    /**
     * @param nombreModuloIniciar the nombreModuloIniciar to set
     */
    public void setNombreModuloIniciar(String nombreModuloIniciar) {
        this.nombreModuloIniciar = nombreModuloIniciar;
    }

    /**
     * @return the nombreModuloAvanzar
     */
    public String getNombreModuloAvanzar() {
        return nombreModuloAvanzar;
    }
    

    /**
     * @param nombreModuloAvanzar the nombreModuloAvanzar to set
     */
    public void setNombreModuloAvanzar(String nombreModuloAvanzar) {
        this.nombreModuloAvanzar = nombreModuloAvanzar;
    }

    /**
     * @return the nombreModuloRetroceder
     */
    public String getNombreModuloRetroceder() {
        return nombreModuloRetroceder;
    }

    /**
     * @param nombreModuloRetroceder the nombreModuloRetroceder to set
     */
    public void setNombreModuloRetroceder(String nombreModuloRetroceder) {
        this.nombreModuloRetroceder = nombreModuloRetroceder;
    }

    /**
     * Devuelve el nombre del m?dulo, si la operacion es de modulo externo
     */
    public String getNombreModuloFinal(){
        if(nombreModuloAvanzar != null && !nombreModuloAvanzar.equals(""))
            return nombreModuloAvanzar;
        else if(nombreModuloIniciar != null && !nombreModuloIniciar.equals(""))
            return nombreModuloIniciar;
        else if(nombreModuloRetroceder != null && !nombreModuloRetroceder.equals(""))
            return nombreModuloRetroceder;
        return null;
    }
    
    /**
     * @return the tipoRetroceso
     */
    public int getTipoRetroceso() {
        return tipoRetroceso;
    }

    /**
     * @param tipoRetroceso the tipoRetroceso to set
     */
    public void setTipoRetroceso(int tipoRetroceso) {
        this.tipoRetroceso = tipoRetroceso;
    }

    /**
     * @return the numeroOrden
     */
    public int getNumeroOrden() {
        return numeroOrden;
    }

    /**
     * @param numeroOrden the numeroOrden to set
     */
    public void setNumeroOrden(int numeroOrden) {
        this.numeroOrden = numeroOrden;
    }
    
    /**
     * Devuelve el cfo con valor != -1 entre los cfo de avanzar, iniciar y retrocede
     */
    public long getCfoFinal(){
        if(cfoAvanzar != -1) return cfoAvanzar;
        else if(cfoIniciar != -1) return cfoIniciar;
        else return cfoRetroceder;
    }
    
    /**
     * Devuelve el codigo de operacion con valor != -1 entre los codigos de avanzar, iniciar y retrocede
     */
    public int getCodigoFinal(){
        if(codAvanzar != -1) return codAvanzar;
        else if(codIniciar != -1) return codIniciar;
        else return codRetroceder;
    }
	
    /**
     * Devuelve el tipo de operacion según el origen: SW o MODULO
     */
    public String getTipoOperacionFinal(){
        if(getTipoOperacion() != null) return getTipoOperacion();
        else if(getTipoOrigenOperacion() != null) return getTipoOrigenOperacion();
        else if(cfoAvanzar != -1) return this.getTipoOperacionAvanzar();
        else if(cfoIniciar != -1) return this.getTipoOperacionIniciar();
        else return this.getTipoOperacionRetroceder();
    }

    /**
     * @return the listaParametros
     */
    public List<ParametroConfigurableVO> getListaParametros() {
        return listaParametros;
    }

    /**
     * @param listaParametros the listaParametros to set
     */
    public void setListaParametros(List<ParametroConfigurableVO> listaParametros) {
        this.listaParametros = listaParametros;
    }
    
    
}