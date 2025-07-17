/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.sir;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.sir.exception.CodigoMensajeSir;
import es.altia.flexia.sir.exception.GestionSirException;
import es.altia.flexia.sir.model.Anexo;
import es.altia.flexia.sir.model.EstadoAsiento;
import es.altia.flexia.sir.model.Intercambio;
import es.altia.flexia.sir.util.GestionSirConstantes;
import es.altia.flexia.sir.vo.AsientoValueObject;
import es.altia.util.LectorProperties;
import es.altia.util.StringUtils;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.json.JSONException;
import org.json.JSONObject;

public class BuzonEntradaAsientoAction extends DispatchAction {

	private final Logger LOGGER = Logger.getLogger(BuzonEntradaAsientoAction.class);
	private final String FORMATO_FECHA = "dd/MM/yyyy";
	
	/**
	 * Metodo que se llama al cargar la pagina buzonEntradaAsiento.jsp. Este
	 * metodo se encarga de cargar la tabla con la lista de asientos pendientes.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward cargarBuzon(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOGGER.debug("BuzonEntradaAsientoAction.cargarBuzon");
		HttpSession session = request.getSession();
		UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
		String[] params = usuario.getParamsCon();

		GestionSir gestionSir = new GestionSir();
		try {
			List<Intercambio> listaIntercambios = gestionSir.cargarIntercambiosPendientes(params);

			List<AsientoValueObject> listaAsientoValueObject = new ArrayList<AsientoValueObject>();
			for (Intercambio intercambio : listaIntercambios) {
				listaAsientoValueObject.add(new AsientoValueObject(intercambio));
			}
			Gson gson = new GsonBuilder().setDateFormat(FORMATO_FECHA).create();
			String json = gson.toJson(listaAsientoValueObject);
			request.setAttribute("listaAsientos", json);
		} catch (GestionSirException ex) {
			LOGGER.error("Error al cargar los asientos: ", ex);
			LectorProperties properties = new LectorProperties(GestionSirConstantes.PROPERTIES_MENSAJES_SIR, 
					usuario.getIdioma());
			response.getWriter().write(properties.getMensaje(ex.getCodigo(), ex.getParametros()));
		}
		return mapping.findForward("cargarTabla");
	}

	/**
	 * Metodo al que se llama al pulsar el boton de aceptar en
	 * buzonEntradaAsiento.jsp. Este metodo acepta el Asiento.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward aceptar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOGGER.debug("BuzonEntradaAsientoAction.aceptar");

		HttpSession session = request.getSession();
		UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
		String codigoIntercambio = request.getParameter("codigoIntercambio");
		LectorProperties properties = new LectorProperties(GestionSirConstantes.PROPERTIES_MENSAJES_SIR, usuario.getIdioma());
		try {
			GestionSir gestionSir = new GestionSir();
			gestionSir.aceptarAsiento(codigoIntercambio, usuario);	
			response.getWriter().write(properties.getMensaje(CodigoMensajeSir.EXITO_ACEPTAR_ASIENTO.getCodigo()));
		} catch (GestionSirException ex) {
			LOGGER.error("Error al aceptar el asiento: ", ex);
			response.getWriter().write(properties.getMensaje(ex.getCodigo(), ex.getParametros()));
		}
		return mapping.findForward("Success");
	}

	/**
	 * Metodo al que se llama al pulsar el boton de rechazar en
	 * buzonEntradaAsiento.jsp. Este metodo rechaza el Asiento.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward rechazar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOGGER.debug("BuzonEntradaAsientoAction.rechazar");
		HttpSession session = request.getSession();
		UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
		String[] params = usuario.getParamsCon();
		String codigoIntercambio = request.getParameter("codigoIntercambio");
		String descTipoAnotacion = request.getParameter("descTipoAnotacion");
		LectorProperties properties = new LectorProperties(GestionSirConstantes.PROPERTIES_MENSAJES_SIR, usuario.getIdioma());
		try {
			GestionSir gestionSir = new GestionSir();
			gestionSir.rechazarAsiento(codigoIntercambio, descTipoAnotacion,
					Integer.toString(usuario.getUnidadOrgCod()), params);
			response.getWriter().write(properties.getMensaje(CodigoMensajeSir.EXITO_RECHAZAR_REGISTRO.getCodigo()));
		} catch (GestionSirException ex) {
			LOGGER.error("Error al rechazar el asiento: ", ex);
			response.getWriter().write(properties.getMensaje(ex.getCodigo(), ex.getParametros()));
		}
		return mapping.findForward("Success");
	}

	/**
	 * Metodo que se llama al hacer doble click sobre la tabla de
	 * buzonEntradaAsientos.jsp.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward visualizarRegistro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOGGER.debug("BuzonEntradaAsientoAction.visualizarRegistro");
		HttpSession session = request.getSession();
		UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
		String[] params = usuario.getParamsCon();
		String codigoIntercambio = request.getParameter("codigoIntercambio");
		LectorProperties properties = new LectorProperties(GestionSirConstantes.PROPERTIES_MENSAJES_SIR, usuario.getIdioma());
		try {
			GestionSir gestionSir = new GestionSir();
			Intercambio intercambio = gestionSir.buscarIntercambio(codigoIntercambio, params);
			Gson gson = new GsonBuilder().setDateFormat(FORMATO_FECHA).create();
			String json = gson.toJson(new AsientoValueObject(intercambio));
			request.setAttribute("asientosSeleccionado", json);
		} catch (GestionSirException ex) {
			LOGGER.error("Error al cargar el registro para visualizarlo: ", ex);
			response.getWriter().write(properties.getMensaje(ex.getCodigo(), ex.getParametros()));
		}
		return mapping.findForward("visualizarRegistro");
	}

	/**
	 * Se actualiza el {@link EstadoAsiento} del {@link Intercambio} relacionado con el codIntercambio pasado por 
	 * sesion.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws GestionSirException 
	 */
	public ActionForward actualizarEstadoAsiento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOGGER.debug("BuzonEntradaAsientoAction.actualizarEstado");
		HttpSession session = request.getSession();
		UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
		String[] params = usuario.getParamsCon();
		String asientosCambiadosJson = request.getParameter("asientosCambiados");
		LectorProperties properties = new LectorProperties(GestionSirConstantes.PROPERTIES_MENSAJES_SIR, usuario.getIdioma());
		try {
			Gson gson = new GsonBuilder().setDateFormat(FORMATO_FECHA).create();
			GestionSir gestionSir = new GestionSir();
			for (JsonElement json : new JsonParser().parse(asientosCambiadosJson).getAsJsonArray()) {
				AsientoValueObject asiento = gson.fromJson(json, AsientoValueObject.class);
				gestionSir.actualizarEstadoAsiento(asiento.getCodIntercambio(), asiento.getEstado(), params);
			}
			response.getWriter().write(properties.getMensaje(CodigoMensajeSir.EXITO_CAMBIO_ESTADO_INTERCAMBIO.getCodigo()));
		} catch (GestionSirException ex) {
			LOGGER.error("Error al actualizar el estado del asiento: ", ex);
			response.getWriter().write(properties.getMensaje(ex.getCodigo(), ex.getParametros()));
		}
		return mapping.findForward("Success");
	}

	/**
	 * Recupera el contenido del fichero de intercambio cuyo codigo es enviado a este metodo.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public ActionForward cargarFicheroIntercambio(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOGGER.debug("BuzonEntradaAsientoAction.cargarFicheroIntercambio");
		HttpSession session = request.getSession();
		UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
		String[] params = usuario.getParamsCon();
		String codIntercambio = request.getParameter("codigoIntercambio");
		LectorProperties properties = new LectorProperties(GestionSirConstantes.PROPERTIES_MENSAJES_SIR, usuario.getIdioma());
		try {
			Gson gson = new GsonBuilder().setDateFormat(FORMATO_FECHA).create();
			GestionSir gestionSir = new GestionSir();
			String ficheroIntercambio = gestionSir.cargarFicheroIntercambio(codIntercambio, params);
			request.setAttribute("ficheroIntercambio", ficheroIntercambio);
			response.getWriter().write(ficheroIntercambio);
		} catch (GestionSirException ex) {
			LOGGER.error("Error al cargar el fichero intercambio SICRES: ", ex);
			response.getWriter().write(properties.getMensaje(ex.getCodigo(), ex.getParametros()));
		}
		return mapping.findForward("Success");
	}

	public ActionForward recuperarFicheroAnexoVisualizar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOGGER.debug("BuzonEntradaAsientoAction.recuperarFicheroParaVisualizar");
		HttpSession session = request.getSession();
		UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
		GeneralValueObject respuesta = new GeneralValueObject();
		LectorProperties properties = new LectorProperties(GestionSirConstantes.PROPERTIES_MENSAJES_SIR, usuario.getIdioma());
		String[] params = usuario.getParamsCon();
		String idFicheroAnexo = request.getParameter("datosAnexo");
		//llega el json 
		Anexo anexo = null;
		try {
			anexo = new GestionSir().recuperarFicheroAnexoVisualizar(idFicheroAnexo, params);
			if(anexo != null){
				retornarJSON(createJsonVisualizarAnexo(anexo), response);
			}
		} catch (GestionSirException ex) {
			LOGGER.error("Error al encontrar el archivo para exportar: ", ex);
			response.getWriter().write(properties.getMensaje(ex.getCodigo(), ex.getParametros()));
		}
		return null;
	}
	
	private String createJsonVisualizarAnexo(Anexo anexo) {
		JSONObject json = new JSONObject();
		try {
			json.put("nombreDocumento", anexo.getNombreFichero());
			if(anexo.getContenido() != null){
				json.put("contenidoDocumento", Base64.encodeBase64String(anexo.getContenido()));
			}
		} catch (JSONException ex) {
			LOGGER.error("Error al crear el Json: ", ex);
		}
		return json.toString();
	}

	public ActionForward visualizarDocumentoAnexo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String nombreDocumentoAnexo = request.getParameter("nombreDocumento");
		String contenidoDocumentoAnexo = request.getParameter("contenidoDocumento");
		HttpSession session = request.getSession();
		UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
		LectorProperties properties = new LectorProperties(GestionSirConstantes.PROPERTIES_MENSAJES_SIR, usuario.getIdioma());
		if (StringUtils.isNotNullOrEmpty(nombreDocumentoAnexo)) {
			try {
				byte[] result =  Base64.decodeBase64(contenidoDocumentoAnexo);
				response.setContentType("application/zip");
				response.setHeader("Content-Disposition", "attachment; filename=" + nombreDocumentoAnexo);
				
				ServletOutputStream out = response.getOutputStream();
				response.setContentLength(result.length);
				BufferedOutputStream bos = new BufferedOutputStream(out);
				bos.write(result, 0, result.length);
				bos.flush();
				bos.close();
			} catch (IOException ex) {
				LOGGER.error("Error al encontrar el archivo para exportar: ", ex);
				response.getWriter().write(properties.getMensaje(CodigoMensajeSir.ERROR_CARGAR_FICHERO.getCodigo()));
			}
		}
		return null;
	}
	
	
	/**
	 * Método llamado para devolver un String en formato JSON al cliente que ha realiza la petición a alguna de las
	 * operaciones de este action
	 *
	 * @param json: String que contiene el JSON a devolver
	 * @param response: Objeto de tipo HttpServletResponse a través del cual se devuelve la salida al cliente que ha
	 * realizado la solicitud
	 */
	private void retornarJSON(String json, HttpServletResponse response) {

		try {
			if (json != null) {
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				out.print(json);
				out.flush();
				out.close();
			}

		} catch (Exception ex) {
			LOGGER.error("Error al devolver el JSON: ", ex);
		}

	}
	
}
