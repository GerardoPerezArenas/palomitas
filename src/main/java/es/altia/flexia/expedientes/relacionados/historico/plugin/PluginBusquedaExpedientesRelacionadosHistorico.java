package es.altia.flexia.expedientes.relacionados.historico.plugin;

import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.flexia.expedientes.relacionados.historico.vo.ExpedientesAsociadosVO;
import java.util.ArrayList;

/**
 * @author david.caamano
 * 
 * Clase que encapsulara las peculiaridades de las busquedas para la desasociacion
 * de los expedientes relacionados en las entradas de registro que se encuentren en el
 * historico y se quieran devolver al buzon
 */
public abstract class PluginBusquedaExpedientesRelacionadosHistorico {
    
    /**
     * Este metodo indicara si debemos bloquear la posibilidad de deseleccionar expedientes de la lista de expedientes relacionados
     * de una anotacion al devolverla al buzon desde el historico.
     * 
     * @param codOrganizacion
     * @return Boolean
     */
    public abstract Boolean bloquearPantalla(String codOrganizacion);
    
    /**
     * Este metodo devolvera encapsulado en el objeto ExpedientesAsociadosVO la información que indica si permitimos desasociar los
     * expedientes asociados a una entrada de registro que se encuentre en el historico y el numero de expedientes asociados de la
     * entrada.
     * 
     * @param departamento
     * @param ejercicio
     * @param tipo_reg
     * @param params
     * @return ExpedientesAsociadosVO
     * @throws TramitacionException 
     */
    public abstract ExpedientesAsociadosVO getComprobarDesasociacion(String cod_uni,String ejercicio, String tipo_reg ,
            String[] params) throws TramitacionException;
    
    public abstract ExpedientesAsociadosVO getComprobarDesasociacion(String departamento,String ejercicio, String tipo_reg ,String cod_uni,
            String[] params) throws TramitacionException;
    
    /**
     * Este metodo devolvera la lista de numeros de expediente asociados a la entrada de registro.
     * 
     * @param departamento
     * @param ejercicio
     * @param tipo_reg
     * @param params
     * @return ArrayList<String>
     * @throws TramitacionException 
     */
    public abstract ArrayList<String> listaExpedientes(String departamento, String ejercicio, String tipo_reg , 
            String[] params) throws TramitacionException; 
     
    
    public abstract ArrayList<String> listaExpedientes(String departamento, String ejercicio, String tipo_reg , String unidadReg,
            String[] params) throws TramitacionException;
    
}//class
