/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.notificacion.plugin;


import java.util.Vector;


import es.altia.common.exception.TechnicalException;


import es.altia.flexia.notificacion.vo.*;

public interface PluginNotificacion {

    public String getCodOrganizacion() throws TechnicalException ;

    public void setCodOrganizacion(String codOrganizacion) throws TechnicalException ;

    public boolean existenInteresadosAdmitenNotificacion(NotificacionVO notificacionVO,String[] params)  throws TechnicalException ;

    public NotificacionVO getNotificacion (NotificacionVO notificacionVO,String[] params)  throws TechnicalException ;

    public String getUrlPantallaDatosNotificacion ()  throws TechnicalException ;

    public boolean guardarFirma(int codigoNotificacion,String firma,String[] params) throws TechnicalException ;

    public boolean grabarNotificacion(NotificacionVO notificacionVO,String[] params)  throws TechnicalException ;

    public boolean enviarNotificacion (NotificacionVO notificacionVO,String[] params)  throws TechnicalException ;

    public boolean existenDocumentosFirmados (NotificacionVO notificacionVO,String[] params)  throws TechnicalException ;
    
    public boolean existenDocumentosPendientesFirma (NotificacionVO notificacionVO,String[] params)  throws TechnicalException ;

    public String getNotificacionFirma (NotificacionVO notificacionVO,String[] params)  throws TechnicalException ;

    public boolean verificarFirma (NotificacionVO notificacionVO,String firma,String[] params)  throws TechnicalException ;

    public boolean guardarEstadoNotificacionEnviada (NotificacionVO notificacionVO,String[] params)  throws TechnicalException ;
        
    public boolean actualizarNotificacion(NotificacionVO notificacionVO, String[] params) throws TechnicalException;
        
    public void crearNotificacionDefecto(NotificacionVO notificacion,String[] params);

    public es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.NotificacionVO consultarNotificacionSNE(String codOrganizacion,String numeroRegistroRT);
}
