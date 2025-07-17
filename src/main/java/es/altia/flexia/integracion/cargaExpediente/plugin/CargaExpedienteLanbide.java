/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.cargaExpediente.plugin;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.integracion.cargaExpediente.CargaExpediente;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;


public class CargaExpedienteLanbide implements CargaExpediente {
    
    //Logger
    private static final Logger m_Log = Logger.getLogger(CargaExpedienteLanbide.class);
    

    @Override
    public void opcionCargarExpediente(HttpServletRequest request, UsuarioValueObject usuarioVO, String codOrganizacion, String numExpediente) throws Exception {
        //Agregamos nueva validacion de terceros con rol de represenntes al cargar un expediente. 
        // Debe agregarse antes de recoger los interesados del expediente porque es necesario que los cargue actualizados
        try {

            // Invocamos el Metodo del Modulo de inegracion M43
            m_Log.info("Vamos a validar el representante legal .... contra RdR");
            final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide43.gestionrdr.MELANBIDE43_GestionRdR");
            m_Log.info("Despues de instanciar class for name");
            final Object me43Class = cls.newInstance();
            final Class[] types = {int.class, String.class, int.class,int.class};
            m_Log.info("Creada instancia y definidos types");
            final Method method = cls.getMethod("validarExistActualRolRepresContraRdR", types);
            m_Log.info("GetMethod... Ahora invocamos");
            int idioma = (usuarioVO != null ? usuarioVO.getIdioma() : 1);
            int codOrganizacionInt = (codOrganizacion != null && !codOrganizacion.isEmpty() ? Integer.parseInt(codOrganizacion) : 0);
            int idNotificacion = -1; // Asignamos -1 porque desde este punto no conocemos el id de la notificacion asociada al representen en la tabla autorizados notificacion
            Map<String, String> respuestaValidacionRdR = (Map<String, String>) method.invoke(me43Class, codOrganizacionInt, numExpediente, idioma,idNotificacion);
            m_Log.info("GetMethod... Despues de invoke metodo");
            if (respuestaValidacionRdR != null) {
                String codRespuestaValRdR = respuestaValidacionRdR.get("codigo");
                String mensajeRespuestaValRdR = respuestaValidacionRdR.get("descripcion");
                m_Log.info("Hemos recibido respuesta : " + codRespuestaValRdR + " / " + mensajeRespuestaValRdR);
                if (codRespuestaValRdR != null && !"0".equalsIgnoreCase(codRespuestaValRdR)
                        && !"1".equalsIgnoreCase(codRespuestaValRdR)
                        && !"2".equalsIgnoreCase(codRespuestaValRdR)) {
                    request.setAttribute("MostrarMensajeRespuestaValRdR", "si");
                    request.setAttribute("mensajeRespuestaValRdR", codRespuestaValRdR + " - " + mensajeRespuestaValRdR);
                } else {
                    request.setAttribute("MostrarMensajeRespuestaValRdR", "no");
                }
            }
        } catch (Exception e) {
            m_Log.error("Error al validar el rol de representante legal del expediente contra RdR " + e.getMessage(), e);
            throw e;
        }
    }
    
}
