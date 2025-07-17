package es.altia.flexia.portafirmasexternocliente.plugin;

import es.altia.agora.business.sge.TramitacionExpedientesValueObject;

/**
 * @author david.caamano
 * @version 04/03/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 04/03/2013 * #53275 Edición inicial</li>
 * </ol> 
 */
public interface PluginPortafirmasExternoCliente {
    
    public final static String OPERACION_CORRECTA = "C";
    public final static String OPERACION_ERROR = "E";
    
    public String enviarDocumentoTramitacionPortafirmas(String codOrganizacion, String codProcedimiento, String numExpediente, String codTramite, 
            String ocurrenciaTramite, String codDocumento, int idUsuario, byte[] contenido, String[] params, String portafirmas);
    
    public String enviarDocumentoTramitacionPortafirmas(String codOrganizacion, String codProcedimiento, String numExpediente, String codTramite, 
            String ocurrenciaTramite, String codDocumentoEnviar, String codDocumentoOriginal, int idUsuario, byte[] contenido, String[] params, String portafirmas);
    
    @Deprecated
    public String actualizarFirmasTramitacionExpediente(String codOrganizacion, String numExpediente, String[] params);
    
    /**
     * Comprueba el estado de las firmas de los documentos de los tramites pendientes
     * y actualiza la base de datos
     * 
     * @param codOrganizacion codigo de organizacion
     * @param numExpediente numero de expediente
     * @param codUsuario codigo de usuario que solicita la operacion (usuario de sesion)
     * @param params parametros de conexion a base de datos
     * @return 
     */
    public String actualizarFirmasTramitacionExpediente(String codOrganizacion, String numExpediente, String codUsuario, String[] params);
    
    public String actualizarFirmasDocumentosTramitacion(TramitacionExpedientesValueObject tEVO, String[] params);
    
    /**
     * Comprueba si el usuario existe en el portafirmas externo
     * 
     * @param codOrganizacion
     * @param documento
     * @param params
     * @return 
     */
    public Boolean comprobarUsuarioPortafirmas(String codOrganizacion, String documento, String[] params);
    
}//class