 /* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.interfaces.user.web.mantenimiento;


import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.eni.GestionEni;
import es.altia.flexia.eni.exception.CodigoMensajeEni;
import es.altia.flexia.eni.exception.GestionEniException;
import es.altia.flexia.eni.util.GestionEniConstantes;
import es.altia.util.LectorProperties;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Elías
 */
public final class ImportarExpedienteAction extends DispatchAction {
	
	private final Logger log = Logger.getLogger(ImportarExpedienteAction.class);
	
	public ActionForward importar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
			log.debug("================= ImportarExpediente ======================>");
			UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
			GestionEni gestionEni = new GestionEni();
			String ficheros = request.getParameter("importeExpediente");
			log.debug("Importando: " + ficheros);
			
			PrintWriter insWriter = response.getWriter();
			String numeroExpediente;
			String respuesta;
			
			LectorProperties properties = new LectorProperties(GestionEniConstantes.PROPERTIES_MENSAJES_ENI, usuario.getIdioma());
		try {
			numeroExpediente = gestionEni.obtenerExpedienteEni(usuario, ficheros);
			
			respuesta = properties.getMensaje(CodigoMensajeEni.IMPORTACION_CORRECTA.getCodigo(), numeroExpediente);
		} catch (GestionEniException ex) {
			log.error("Se ha producido un error durante la importacion", ex);
			respuesta = properties.getMensaje(ex.getCodigo(), ex.getParametros());
		}
		insWriter.write("<input type='hidden' id='respuesta' value='" + respuesta + "'/>");
		return null;
	}
}