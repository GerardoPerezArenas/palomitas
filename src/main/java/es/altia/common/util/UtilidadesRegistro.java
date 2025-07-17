package es.altia.common.util;

import java.util.ResourceBundle;

/**
 * Clase con métodos estáticos de utilidad para el registro
 * @author oscar
 */
public class UtilidadesRegistro {
    
    private static final String FICHERO_CONFIGURACION_REGISTRO = "Registro";
    
    /**
     * Comprueba si un determinado servicio de búsqueda del buzón de entrada, es el único que está activo para una determinada
     * organización     
     * @param servicio: Nombre del servicio, por ejemplo: SGE
     * @param codOrganizacion: Código de la organización 
     * @return Un boolean
     */
    public static boolean estaActivadoSoloServicioBusquedaBuzonEntrada(String servicio,int codOrganizacion){
        boolean salida = false;
        
        ResourceBundle CONFIG_REGISTRO = ResourceBundle.getBundle(FICHERO_CONFIGURACION_REGISTRO);         
        String SERVICIOS_BUSQUEDA      = CONFIG_REGISTRO.getString("Registro/" + codOrganizacion + "/serviciosDisp");
        if(!"".equals(SERVICIOS_BUSQUEDA) && SERVICIOS_BUSQUEDA!=null){
            String[] lista = SERVICIOS_BUSQUEDA.split(";");
            
            if(lista!=null && lista.length==1 && lista[0].equalsIgnoreCase(servicio))
                salida = true;
        }                            
        return salida;        
    }
    
    public static boolean estaActivadoSoloServicioBusquedaBuzonEntrada2(String servicio,int codOrganizacion){
        boolean salida = false;
        
        ResourceBundle CONFIG_REGISTRO = ResourceBundle.getBundle(FICHERO_CONFIGURACION_REGISTRO);         
        String SERVICIOS_BUSQUEDA      = CONFIG_REGISTRO.getString("Registro/" + codOrganizacion + "/serviciosDisp");
      
        if(!"".equals(SERVICIOS_BUSQUEDA) && SERVICIOS_BUSQUEDA!=null){
            
            
            if(SERVICIOS_BUSQUEDA.equalsIgnoreCase(servicio))
                salida = true;
        }                            
        return salida;        
    }
	
	/**
	 * Devuelve S o Relacion_S como S y E o Relacion_E como E. Tiene como prerrequisito que la entrada no sea null.
	 * 
	 * @param tipoES
	 * @return 
	 */
	public static String parsearTipoEntradaSalida(String tipoES) {
		
		if ("S".equals(tipoES) || "Relacion_S".equals(tipoES)) {
			return "S";
		}
		
		if ("E".equals(tipoES) || "Relacion_E".equals(tipoES)) {
			return "E";
		}	
		
		return null;
	}
    
}
