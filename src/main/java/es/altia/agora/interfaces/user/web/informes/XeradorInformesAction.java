package es.altia.agora.interfaces.user.web.informes;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.common.service.config.*;
import es.altia.common.util.*;
import es.altia.util.struts.StrutsUtilOperations;

import es.altia.agora.business.geninformes.*;
import es.altia.agora.business.geninformes.utils.*;
import es.altia.agora.business.geninformes.utils.XeracionInformes.*;

import java.io.IOException;
import java.util.Vector;


import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class XeradorInformesAction extends ActionSession {
	String[] params = null;

	protected static Log m_Log =
		LogFactory.getLog(XeradorInformesAction.class.getName());
    
    public ActionForward performSession(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {
		//System.err.println("Entroooo");

		m_Log.debug("perform");
		ActionHelper myActionHelper =
			new ActionHelper(getLocale(request), getResources(request));
		ActionErrors errors = new ActionErrors();
		ActionForward af = null;

		HttpSession session = request.getSession();

		UsuarioValueObject usuarioVO = null;

		if (session.getAttribute("usuario") != null) {
			usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
			params = usuarioVO.getParamsCon();
		}
		Config mTech = ConfigServiceHelper.getConfig("common");
		//Para el fichero de configuracion tecnico

		//Registro parametros=null;
		//MultipartRequest mr=null;

		es.altia.agora.interfaces.user.web.informes.XeradorInformesForm mForm =
			(es
				.altia
				.agora
				.interfaces
				.user
				.web
				.informes
				.XeradorInformesForm) form;

		GeneradorInformesMgr _opXeradorInformes =	new GeneradorInformesMgr(params);

		m_Log.debug("Antes de todo.");

		try {
			String operacion = request.getParameter("operacion");

            if (m_Log.isInfoEnabled()) m_Log.info("la operacion en XeradorInformesAction es : " + operacion);            

			HttpSession sesion = request.getSession(true);

			es.altia.util.HashtableWithNull hash_Datos =
				new es.altia.util.HashtableWithNull();
			es.altia.util.HashtableWithNull listadoXerador = null;
			es.altia.util.HashtableWithNull etiquetaXerador = null;
			java.util.Vector camposSeleccion = new java.util.Vector();
			java.util.Vector camposOrde = new java.util.Vector();
			java.util.Vector camposCondicion = new java.util.Vector();
			m_Log.debug("Dentro try.");
			//System.err.println("Dentro try");
			hash_Datos.put(
				"COD_INFORMEXERADOR",
				mForm.getCOD_INFORMEXERADOROculto());
			hash_Datos.put("NOME", mForm.getTextNomeInforme());
			hash_Datos.put("DESCRIPCION", mForm.getTextDescripcion());
			hash_Datos.put("FORMATO", mForm.getComboFormatoInforme());
			hash_Datos.put(
				"COD_ENTIDADEINFORME",
				mForm.getComboEntidadeInforme());
			hash_Datos.put("APL_COD", mForm.getCodAplicacion()); //usuarioVO.getAppCod()+"");
			hash_Datos.put("FORMATO", "L");


			if (!(operacion.equals("C")
				|| operacion.equals("B")
				|| operacion.equals("CLI"))) {
				camposSeleccion =
					creaCamposSeleccion(
						mForm,
						mForm.getCOD_INFORMEXERADOROculto());
				camposOrde =
					creaCamposOrde(
						mForm,
						mForm.getCOD_INFORMEXERADOROculto(),
						camposSeleccion);
				camposCondicion =
					creaCamposCondicion(
						mForm,
						mForm.getCOD_INFORMEXERADOROculto());

				hash_Datos.put("CAMPOSSELECCION", camposSeleccion);
				hash_Datos.put("CAMPOSORDE", camposOrde);
				hash_Datos.put("CAMPOSCONDICION", camposCondicion);
				//System.err.println("Antes mete datos");
				//meteDatosProba(hash_Datos);
				//System.err.println(
				//	"DesdeMeteDatos:cs:"
				//		+ hash_Datos.get("CAMPOSSELECCION")
				//		+ ".");

				EstructuraEntidades ee =	creaEstructuraXeradorInformes(hash_Datos);
				m_Log.debug("Desp meter datos proba.");
				//System.err.println("Desp meter datos proba.");

				hash_Datos.put("ESTRUCTURAINFORME", ee);
				//System.err.println("Meto en ee:" + ee + ".");
			}
			//			}

			if (operacion.equals("C")) {
                if (m_Log.isDebugEnabled()) m_Log.debug("Entro en op C en  SecreatriaCorreoEntradas.Recibo:"+ hash_Datos+ ".");
				hash_Datos.put("COD_INFORMEXERADOR",mForm.getCOD_INFORMEXERADOROculto());
				es.altia.util.conexion.Cursor cursor =_opXeradorInformes.ConsultarInforme(hash_Datos);
				
				if (cursor.next()) {
					if (m_Log.isDebugEnabled()){
                        m_Log.debug("En XeradorAction.Camposcondicion es:"+ cursor.getObject("CAMPOSCONDICION")
							+ ".Camposorde:"+ cursor.getObject("CAMPOSORDE")+ ".CamposSeleccion:"
							+ cursor.getObject("CAMPOSSELECCION")+ ".");
					m_Log.debug("Cursor de consulta informe é:cuirsor.hashNombres="+ cursor.hash_Nombres
							+ ".cursor.hash="+ cursor.hash+ ".");
                    }

                    cursor.Indice = -1;
					request.setAttribute("vectorInforme",Utilidades.ConvertirCursorToArrayJSEscaped(cursor));

				}

			} else if (operacion.equals("CLI")) { // Consulta de lista de informes
				m_Log.debug("Entro en op CLI en SecreatriaCorreoEntradasAction.");
				
				String codAplicacion = mForm.getCodAplicacion();
				hash_Datos.put("APL_COD", codAplicacion);

				es.altia.util.conexion.Cursor cursor =_opXeradorInformes.ConsultarInformesAplicacion(hash_Datos);

				request.setAttribute("vectorInformes",Utilidades.ConvertirCursorToArrayJSEscaped(cursor));


			} else if (operacion.equals("A")) { // Engadir

				if (m_Log.isDebugEnabled()) m_Log.debug("Entro en op A en  XeradorInformesAction.Recibo:"+ hash_Datos+ ".");

				hash_Datos.remove("COD_INFORMEXERADOR");

				String codAplicacion = mForm.getCodAplicacion();
				hash_Datos.put("APL_COD", codAplicacion);

				es.altia.util.conexion.Cursor salida =
					_opXeradorInformes.EngadirInforme(hash_Datos);
				if (salida.next())
					request.setAttribute("COD_INFORMEXERADOR",salida.getString("COD_INFORMEXERADOR"));
				es.altia.util.HashtableWithNull entradaXerador =new es.altia.util.HashtableWithNull();
				entradaXerador.put("NOMEREPORT","report_" + request.getAttribute("COD_INFORMEXERADOR"));

				hash_Datos.put("NOMEREPORT","report_" + request.getAttribute("COD_INFORMEXERADOR"));

				m_Log.debug("En XeradorInformes A ._opXeradorInformes.EngadirInformeCentro.Pongo:"
						+ request.getAttribute("COD_INFORMEXERADOR")+ ".");

			} else if (operacion.equals("M")) { // Modificar
				if (m_Log.isDebugEnabled())
				    m_Log.debug("Entro en op M en XeradorInformesAction.Recibo:"+ hash_Datos+ ".");

				_opXeradorInformes.ModificarInforme(hash_Datos);

				hash_Datos.put("NOMEREPORT", "report_"+mForm.getCOD_INFORMEXERADOROculto());

				es.altia.util.HashtableWithNull entradaXerador = new es.altia.util.HashtableWithNull();

				m_Log.debug("Salgo de M en  INFORMEXERADOR.");

			} else if (operacion.equals("B")) { // Eliminar
				m_Log.debug("Entro en op B en  XeradorInformesAction.");
				//System.err.println(
				//	"Entro en op B en  XeradorInformesAction.Recibo:"
				//		+ hash_Datos
				//		+ ".");

				_opXeradorInformes.EliminarInforme(hash_Datos);

				hash_Datos.put("NOMEREPORT", "report_"
				//  + usuario.getCOD_CENTRO()
				+"_" + mForm.getCOD_INFORMEXERADOROculto());

				//System.err.println("Salgo de B en XeradorInformesAction.");

				m_Log.debug("Salgo de B en XeradorInformesAction.");

			} else if (operacion.equals("I")) { // Lanzar informe
				m_Log.debug("Entro en op I en  XeradorInformesAction.");
				//System.err.println(
				//	"Entro en op I en  XeradorInformesAction.Recibo:"
				//		+ hash_Datos
				//		+ ".");
				es.altia.util.HashtableWithNull hash_Param =
					new es.altia.util.HashtableWithNull();

				es.altia.util.HashtableWithNull salida =
					_opXeradorInformes.ejecutarInforme(hash_Datos);

				//String xmlSaida=(String) salida.get("XML");
				//byte[] ficheroDot=(byte[]) salida.get("F_DOT");

				//Escribir el .DOT en un fichero temporal

				java.util.Date fecha = new java.util.Date();
				long selloAux = fecha.getTime();
				String sello = "" + selloAux;
				request.setAttribute("sello", sello);

				java.io.DataOutputStream fichero =
					new java.io.DataOutputStream(
						new java.io.FileOutputStream(
							this.getServlet().getServletContext().getRealPath("")   // mTech.getString("PATH.publicHtml")
								+ "/documentos/temp/"
								+ sello
								+ ".doc"));
				java.io.DataOutputStream fichero2 =
					new java.io.DataOutputStream(
						new java.io.FileOutputStream(
							this.getServlet().getServletContext().getRealPath("")   // mTech.getString("PATH.publicHtml")
								+ "/documentos/temp/"
								+ sello
								+ ".xml"));
								

				if (salida.get("XML") != null) {
					byte[] ficheroXML = ((String) salida.get("XML")).getBytes();

					if (ficheroXML != null) {
						fichero2.write(ficheroXML, 0, ficheroXML.length);
						fichero2.close();
					}
				}
				String salidaXML =Utilidades.js_escape(salida.get("XML").toString());

				request.setAttribute("XML", salidaXML);
		
				String protocolo = StrutsUtilOperations.getProtocol(request);                
				m_Log.debug("PROTOCOLO en uso :" + protocolo);
				
 	request.setAttribute(
					"URL_FDOT",
					protocolo + "://"+request.getHeader("Host") + request.getContextPath()
						+ "/temp/documentosGenerador/"
						+params[0]+"/"+params[6]+"/Documento"+mForm.getCOD_INFORMEXERADOROculto()
						+ ".doc");

				request.setAttribute(
					"URL_XML",
                    protocolo + "://" + request.getHeader("Host") + request.getContextPath()
						+ "/documentos/temp/"
						+ sello
						+ ".xml");

				//rd =
				//	getServletContext().getRequestDispatcher(
				//		"/jsp/editor/editorWord.jsp");
			} else if (operacion.equals("EP")) { // Editar plantilla
				//				m_Log.debug("Entro en op EP en  XeradorInformesAction.");
				//				m_Log.debug(
				//					"Entro en op EP en  XeradorInformesAction.Recibo:"
				//						+ hash_Datos
				//						+ ".");
				//				es.altia.util.HashtableWithNull hash_Param =
				//					new es.altia.util.HashtableWithNull();
				//
				//				es.altia.util.HashtableWithNull salida =
				//					_opXeradorInformes.ejecutarInforme(hash_Datos);
				//				//Escribir el .DOT en un fichero temporal
				//
				//				java.util.Date fecha = new java.util.Date();
				//				long selloAux = fecha.getTime();
				//				String sello = "" + selloAux;
				//				request.setAttribute("sello", sello);
				//
				//				java.io.DataOutputStream fichero =
				//					new java.io.DataOutputStream(
				//						new java.io.FileOutputStream(
				//							mTech.getString("PATH.publicHtml")
				//								+ "/documentos/temp/"
				//								+ sello
				//								+ ".doc"));
				//
				//				//Registro plantillaSinDatos =
				//				//	(Registro) datosPlantilla.get("plantillaSinDatos");
				//				//plantillaSinDatos.setString(
				//				//	"host",
				//				//	(String) parametros.get("host"));
				//
				//				byte[] ficheroWord = (byte[]) salida.get("F_DOT");
				//
				//				fichero.write(ficheroWord, 0, ficheroWord.length);
				//				fichero.close();
				//
				//				//rd =
				//				//	getServletContext().getRequestDispatcher(
				//				//		"/jsp/editor/editorWord.jsp");
				//			} else {
				//				//
				//				// Código para la edición de plantillas
				//				//	
				//				String opcion = request.getParameter("opcion");
				//				Registro parametros = null;
				//				MultipartRequest mr = null;
				//
				//				if (request.getContentType() != null) {
				//					if (request
				//						.getContentType()
				//						.equals("application/x-www-form-urlencoded")) {
				//						parametros = obtenerParametros(request);
				//					} else {
				//						mr = new MultipartRequest(request);
				//						parametros = obtenerParametros(mr, request);
				//					}
				//				}
				//
				//				if (parametros != null) {
				//					request.setAttribute("parametros", parametros);
				//					opcion = parametros.getString("opcion");
				//				} else {
				//					opcion = request.getParameter("opcion");
				//				}
				//
				//				if (opcion == null)
				//					opcion = "CARGA_INICIAL";
				//
				//				if (opcion.equals("CARGA_INICIAL")) {
				//					// Para cargar el editor la primera vez
				//					Vector etiquetas =
				//						editorMan.obtenerDatosEtiquetas(params, parametros);
				//					request.setAttribute("Etiquetas", etiquetas);
				//					request.setAttribute("parametros", parametros);
				//					request.setAttribute("opcion", "CARGA_INICIAL");
				//					rd =
				//						getServletContext().getRequestDispatcher(
				//							"/jsp/editor/editorWord.jsp");
				//				} else if (opcion.equals("GRABAR_PLANTILLA")) {
				//					MultipartRequest.FileInfo[] files =
				//						mr.getFileInfoValues("fichero");
				//					byte[] fichero = files[0].getContent();
				//					request.setAttribute("F_DOT", fichero);
				//					request.setAttribute(
				//						"PLANTILLA_GRABADA",
				//						new Boolean(true));
				//
				//					//  parametros.put("plt_doc",fichero);
				//					// editorMan.grabarPlantilla(params, parametros);
				//					//rd = getServletContext().getRequestDispatcher("/jsp/editor/ocultoEditor.jsp");
				//				}
				//				//		  else if (opcion.equals("grabarCRD")) {
				//				//			  String nombreDocumento = mr.getParameter("nombreDocumento");
				//				//			  parametros.setString("nombreDocumento", nombreDocumento);
				//				//			  parametros = obtenerParametrosDeTramitacionExpedientes(parametros,request);
				//				//			  MultipartRequest.FileInfo [] files = mr.getFileInfoValues("fichero");
				//				//			  byte [] fichero = files[0].getContent();
				//				//			  parametros.put("crd_fil",fichero);
				//				//			  rd = getServletContext().getRequestDispatcher("/jsp/editor/ocultoEditorCronologia.jsp");
				//				//		  }
				//				else if (opcion.equals("OBTENER_DATOS_PLANTILLA")) {
				//					Registro datosPlantilla =
				//						editorMan.obtenerDatosPlantilla(params, parametros);
				//
				//					request.setAttribute("datosPlantilla", datosPlantilla);
				//					request.setAttribute("opcion", "OBTENER_DATOS_PLANTILLA");
				//
				//					Date fecha = new Date();
				//					long selloAux = fecha.getTime();
				//					String sello = "" + selloAux;
				//					request.setAttribute("sello", sello);
				//
				//					DataOutputStream fichero =
				//						new DataOutputStream(
				//							new FileOutputStream(
				//								mTech.getString("PATH.publicHtml")
				//									+ "/documentos/temp/"
				//									+ sello
				//									+ ".doc"));
				//
				//					Registro plantillaSinDatos =
				//						(Registro) datosPlantilla.get("plantillaSinDatos");
				//					plantillaSinDatos.setString(
				//						"host",
				//						(String) parametros.get("host"));
				//
				//					byte[] ficheroWord =
				//						(byte[]) plantillaSinDatos.get("plt_doc");
				//
				//					fichero.write(ficheroWord, 0, ficheroWord.length);
				//					fichero.close();
				//
				//					rd =
				//						getServletContext().getRequestDispatcher(
				//							"/jsp/editor/editorWord.jsp");
				//				} else if (
				//					opcion.equals("OBTENER_PLANTILLA")
				//						|| opcion.equals(
				//							"VER_PLANTILLA")) { // Para modificarla O VISUALIZARLA DESDE TRAMITES
				//					Vector etiquetas =
				//						editorMan.obtenerDatosEtiquetas(params, parametros);
				//
				//					request.setAttribute("Etiquetas", etiquetas);
				//					Registro datosPlantilla =
				//						editorMan.obtenerPlantilla(params, parametros);
				//					datosPlantilla.setString(
				//						"host",
				//						(String) parametros.get("host"));
				//					datosPlantilla.setString(
				//						"codClasif",
				//						parametros.getString("codClasif"));
				//					datosPlantilla.setString(
				//						"descClasif",
				//						parametros.getString("descClasif"));
				//					request.setAttribute("datosPlantilla", datosPlantilla);
				//					request.setAttribute("opcion", "OBTENER_PLANTILLA");
				//
				//					Date fecha = new Date();
				//					long selloAux = fecha.getTime();
				//					String sello = "" + selloAux;
				//					request.setAttribute("sello", sello);
				//					DataOutputStream fichero =
				//						new DataOutputStream(
				//							new FileOutputStream(
				//								mTech.getString("PATH.publicHtml")
				//									+ "/documentos/temp/"
				//									+ sello
				//									+ ".doc"));
				//
				//					byte[] ficheroWord = (byte[]) datosPlantilla.get("plt_doc");
				//					fichero.write(ficheroWord, 0, ficheroWord.length);
				//					fichero.close();
				//					if (opcion.equals("OBTENER_PLANTILLA"))
				//						rd =
				//							getServletContext().getRequestDispatcher(
				//								"/jsp/editor/editorWord.jsp");
				//					else if (opcion.equals("VER_PLANTILLA")) {
				//						request.setAttribute("opcion", "VER_PLANTILLA");
				//						rd =
				//							getServletContext().getRequestDispatcher(
				//								"/jsp/editor/ocultoEditorWord.jsp");
				//					}
				//				} else if (opcion.equals("MODIFICAR_PLANTILLA")) {
				//					MultipartRequest.FileInfo[] files =
				//						mr.getFileInfoValues("fichero");
				//					byte[] fichero = files[0].getContent();
				//					parametros.put("plt_doc", fichero);
				//					editorMan.modificarPlantilla(params, parametros);
				//					rd =
				//						getServletContext().getRequestDispatcher(
				//							"/jsp/editor/ocultoEditor.jsp");
				//				} else if (opcion.equals("modificarCRD")) {
				//					String nombreDocumento = mr.getParameter("nombreDocumento");
				//					parametros.setString("nombreDocumento", nombreDocumento);
				//					parametros =
				//						obtenerParametrosDeTramitacionExpedientes(
				//							parametros,
				//							request);
				//						"el fichero en el servlet es : "
				//							+ mr.getFileInfoValues("fichero"));
				//					MultipartRequest.FileInfo[] files =
				//						mr.getFileInfoValues("fichero");
				//					byte[] fichero = files[0].getContent();
				//						"el tamaño del fichero en el servlet es : "
				//							+ fichero.length);
				//					parametros.put("crd_fil", fichero);
				//					editorMan.modificarPlantillaCRD(params, parametros);
				//					rd =
				//						getServletContext().getRequestDispatcher(
				//							"/jsp/editor/ocultoEditorCronologia.jsp");
				//				} else if (
				//					opcion.equals(
				//						"OBTENER_DATOS_PLANTILLA_DESDE_CRONOLOGIA")) {
				//					UsuarioValueObject usuarioVO = null;
				//					HttpSession session = request.getSession();
				//					parametros =
				//						obtenerParametrosDeTramitacionExpedientes(
				//							parametros,
				//							request);
				//					String codAplicacion = "";
				//					if (session.getAttribute("usuario") != null) {
				//						usuarioVO =
				//							(UsuarioValueObject) session.getAttribute(
				//								"usuario");
				//						codAplicacion = Integer.toString(usuarioVO.getAppCod());
				//					}
				//					parametros.setString("plt_apl", codAplicacion);
				//					Registro datosPlantilla =
				//						editorMan.obtenerDatosPlantilla(params, parametros);
				//
				//					Date fecha = new Date();
				//					long selloAux = fecha.getTime();
				//					String sello = "" + selloAux;
				//					request.setAttribute("sello", sello);
				//
				//					DataOutputStream fichero =
				//						new DataOutputStream(
				//							new FileOutputStream(
				//								mTech.getString("PATH.publicHtml")
				//									+ "/documentos/temp/"
				//									+ sello
				//									+ ".doc"));
				//					Registro plantillaSinDatos =
				//						editorMan.obtenerPlantilla(params, parametros);
				//					plantillaSinDatos.setString(
				//						"host",
				//						parametros.getString("host"));
				//					plantillaSinDatos.setString(
				//						"nombreDocumento",
				//						parametros.getString("nombreDocumento"));
				//					datosPlantilla.put("plantillaSinDatos", plantillaSinDatos);
				//
				//					request.setAttribute("datosPlantilla", datosPlantilla);
				//					request.setAttribute(
				//						"opcion",
				//						"OBTENER_DATOS_PLANTILLA_DESDE_CRONOLOGIA");
				//					byte[] ficheroWord =
				//						(byte[]) plantillaSinDatos.get("plt_doc");
				//
				//					fichero.write(ficheroWord, 0, ficheroWord.length);
				//					fichero.close();
				//
				//					rd =
				//						getServletContext().getRequestDispatcher(
				//							"/jsp/editor/editorWord.jsp");
				//				} else if (
				//					opcion.equals("OBTENER_PLANTILLA_DESDE_CRONOLOGIA")) {
				//					UsuarioValueObject usuarioVO = null;
				//					HttpSession session = request.getSession();
				//					parametros =
				//						obtenerParametrosDeTramitacionExpedientes(
				//							parametros,
				//							request);
				//					String codAplicacion = "";
				//					if (session.getAttribute("usuario") != null) {
				//						usuarioVO =
				//							(UsuarioValueObject) session.getAttribute(
				//								"usuario");
				//						codAplicacion = Integer.toString(usuarioVO.getAppCod());
				//					}
				//					parametros.setString("plt_apl", codAplicacion);
				//					Vector etiquetas =
				//						editorMan.obtenerDatosEtiquetas(params, parametros);
				//					request.setAttribute("Etiquetas", etiquetas);
				//
				//					Registro datosPlantilla =
				//						editorMan.obtenerPlantillaCRD(params, parametros);
				//					datosPlantilla.setString(
				//						"host",
				//						parametros.getString("host"));
				//					datosPlantilla.setString(
				//						"nombreDocumento",
				//						parametros.getString("nombreDocumento"));
				//					request.setAttribute("datosPlantilla", datosPlantilla);
				//					request.setAttribute(
				//						"opcion",
				//						"OBTENER_PLANTILLA_DESDE_CRONOLOGIA");
				//
				//					Date fecha = new Date();
				//					long selloAux = fecha.getTime();
				//					String sello = "" + selloAux;
				//					request.setAttribute("sello", sello);
				//
				//					DataOutputStream fichero =
				//						new DataOutputStream(
				//							new FileOutputStream(
				//								mTech.getString("PATH.publicHtml")
				//									+ "/documentos/temp/"
				//									+ sello
				//									+ ".doc"));
				//					byte[] ficheroWord = (byte[]) datosPlantilla.get("crd_fil");
				//					fichero.write(ficheroWord, 0, ficheroWord.length);
				//					fichero.close();
				//
				//					rd =
				//						getServletContext().getRequestDispatcher(
				//							"/jsp/editor/editorWord.jsp");
				//				}

			} else if (operacion.equals("GEP")) { // Subir y grabar plantilla editada
				//	MultipartRequest mr = null;
				//	if (request.getContentType() != null) {
				//		if (request
				//			.getContentType()
				//			.equals("application/x-www-form-urlencoded")) {

				//		} else {
				//			mr = new MultipartRequest(request);

				//		}
				//	}
				org.apache.struts.upload.FormFile formFichero =
					mForm.getFicheroWord();

				//MultipartRequest.FileInfo[] files =
				//	mr.getFileInfoValues("fichero");
				if (formFichero != null) {
					byte[] fichero = formFichero.getFileData();
					hash_Datos.put("F_DOT", fichero);
					hash_Datos.put(
						"COD_INFORMEXERADOR",
						mForm.getCOD_INFORMEXERADOROculto());

					_opXeradorInformes.EngadirPlantillaInforme(hash_Datos);
					request.setAttribute(
						"COD_INFORMEXERADOR",
						mForm.getCOD_INFORMEXERADOROculto());
				}
				af = (mapping.findForward("ocultoXeradorPlantilla"));
			}

			//
			// Inicio código antiguo
			//

			if (m_Log.isDebugEnabled()) m_Log.debug("Antes del if ==nul af:" + af + ".");

			if (af == null)
				af = (mapping.findForward("XeradorInformesCorrecto"));
			//			  } // Fin de usuario != null
		} catch (Throwable t) {
			af = (mapping.findForward("errorAlert"));
			//Trtar la excepcion
			//request.setAttribute(es.altia.agora.business.geninformes.utils.Constantes.EXCEPCION, e);
			m_Log.debug(
				"Hgo forward a :ErrorAlert en XeradorInformesAction:"
					+ af
					+ ".Exception es:"
					+ t
					+ ".");
			//System.err.println(
			//	"Hgo forward a :ErrorAlert en XeradorInformesAction:"
			//		+ af
			//			+ ".Exception es:"
			//			+ t
			//			+ ".");
			t.printStackTrace(System.err);

		} finally {
			m_Log.debug("En finally.");
			//			  if (rp != null) {
			//				  if (ib != null) {
			//					  try {
			//						  ib.liberarRecursos();
			//					  } catch (Exception e) {
			//						  m_Log.debug(
			//							  "Excepción en liberar recursos:" + e + ".");
			//					  }
			//
			//					  String formato =
			//						  (mForm.getComboFormatoInforme() != null)
			//							  && (!mForm.getComboFormatoInforme().trim().equals(""))
			//								  ? mForm.getComboFormatoInforme().trim()
			//								  : "L";
			//					  rp.releaseReusable(formato, ib);
			//				  }
			//			  }
		}

		if (m_Log.isDebugEnabled()) m_Log.debug("Hgo forward a :ocultoXeradorPlantilla:" + af + ".");

		return af;

	}

	private String preparaString(String entrada, int tamanoMax) {
		String salida = null;
		if (entrada != null) {
			entrada = entrada.trim();
			salida =
				(entrada.length() > tamanoMax)
					? Utilidades.recortaString(entrada,tamanoMax): entrada;
		}
		return salida;
	}

	private es.altia.util.HashtableWithNull creaListadoXerador(
		XeradorInformesForm mForm,
		String cix) {
		es.altia.util.HashtableWithNull salida =
			new es.altia.util.HashtableWithNull();
		salida.put("COD_INFORMEXERADOR", cix);
		salida.put(
			"TEXTOCABECEIRA",
			((mForm.getTextCabeceira() != null)
				&& (!mForm.getTextCabeceira().trim().equals("")))
				? mForm.getTextCabeceira().trim()
				: null);
		salida.put("FONTECABECEIRA", mForm.getComboFonteCabeceira());
		salida.put("TAMANOFONTECABECEIRA", mForm.getComboTamanoCabeceira());
		salida.put("NEGRITACABECEIRA", mForm.getNegritaCabeceira());
		salida.put("SUBRAIADOCABECEIRA", mForm.getSubraiadoCabeceira());
		salida.put("CURSIVACABECEIRA", mForm.getCursivaCabeceira());
		salida.put("CABECEIRAOFICIAL", mForm.getCabeceiraOficial());
		salida.put("CABECEIRACENTRO", mForm.getCabeceiraCentro());
		salida.put("CABECEIRACOLUMNAS", mForm.getCabeceiraColumnas());
		salida.put(
			"TEXTOPE",
			((mForm.getTextPe() != null)
				&& (!mForm.getTextPe().trim().equals("")))
				? mForm.getTextPe().trim()
				: null);
		salida.put("FONTEPE", mForm.getComboFontePe());
		salida.put("TAMANOFONTEPE", mForm.getComboTamanoPe());
		salida.put("NUMEROPAXINAPE", mForm.getNumeroPaxinaPe());
		salida.put("DATAINFORMEPE", mForm.getDataInformePe());
		salida.put("FONTEDETALLE", mForm.getComboFonteDetalle());
		salida.put("TAMANOFONTEDETALLE", mForm.getComboTamanoDetalle());

		salida.put(
			"MARXEESQUERDA",
			(mForm.getTextMarxeEsquerdo() != null)
				? mForm.getTextMarxeEsquerdo().trim().replace(',', '.')
				: null);
		salida.put(
			"MARXEDEREITA",
			(mForm.getTextMarxeDereito() != null)
				? mForm.getTextMarxeDereito().trim().replace(',', '.')
				: null);
		salida.put("ORIENTACIONPAXINA", mForm.getOrientacionPaxina());
		salida.put("NUMERARLINHAS", mForm.getNumerarLinhas());
		return salida;
	}

	private es.altia.util.HashtableWithNull creaEtiquetaXerador(
		XeradorInformesForm mForm,
		String cix) {
		es.altia.util.HashtableWithNull salida =
			new es.altia.util.HashtableWithNull();
		salida.put("COD_INFORMEXERADOR", cix);
		salida.put("COD_ETIQUETA", mForm.getComboTipoEtiqueta());
		salida.put(
			"ANCHO",
			(mForm.getEtTextAncho() != null)
				? mForm.getEtTextAncho().trim().replace(',', '.')
				: null);
		salida.put(
			"ALTO",
			(mForm.getEtTextAlto() != null)
				? mForm.getEtTextAlto().trim().replace(',', '.')
				: null);
		salida.put(
			"ESPACIOHORIZONTAL",
			(mForm.getEtTextHorizontal() != null)
				? mForm.getEtTextHorizontal().trim().replace(',', '.')
				: null);
		salida.put(
			"ESPACIOVERTICAL",
			(mForm.getEtTextVertical() != null)
				? mForm.getEtTextVertical().trim().replace(',', '.')
				: null);
		salida.put(
			"MARXESUPERIOR",
			(mForm.getEtTextSuperior() != null)
				? mForm.getEtTextSuperior().trim().replace(',', '.')
				: null);
		salida.put(
			"MARXEINFERIOR",
			(mForm.getEtTextInferior() != null)
				? mForm.getEtTextInferior().trim().replace(',', '.')
				: null);
		salida.put(
			"MARXEESQUERDA",
			(mForm.getEtTextEsquerdo() != null)
				? mForm.getEtTextEsquerdo().trim().replace(',', '.')
				: null);
		salida.put(
			"MARXEDEREITA",
			(mForm.getEtTextDereito() != null)
				? mForm.getEtTextDereito().trim().replace(',', '.')
				: null);
		salida.put(
			"MARXESUPERIORETIQUETA",
			(mForm.getEtTextSuperiorEtiqueta() != null)
				? mForm.getEtTextSuperiorEtiqueta().trim().replace(',', '.')
				: null);
		salida.put(
			"MARXEESQUERDAETIQUETA",
			(mForm.getEtTextEsquerdoEtiqueta() != null)
				? mForm.getEtTextEsquerdoEtiqueta().trim().replace(',', '.')
				: null);
		salida.put("BORDEETIQUETA", mForm.getEtBordeEtiqueta());
		salida.put("FONTEDETALLE", mForm.getEtComboFonteDetalle());
		salida.put("TAMANOFONTEDETALLE", mForm.getEtComboTamanoDetalle());
		return salida;
	}

	private java.util.Vector creaCamposSeleccion(
		XeradorInformesForm mForm,
		String cix) {
		java.util.Vector salida = new java.util.Vector();
		es.altia.util.HashtableWithNull temp =
			new es.altia.util.HashtableWithNull();
		if (mForm.getComboCamposElexidos() != null)
			for (int i = 0; i < mForm.getComboCamposElexidos().length; i++) {
				temp = new es.altia.util.HashtableWithNull();
				temp.put("COD_INFORMEXERADOR", cix);
				temp.put("POSICION", new Integer(i));
				temp.put("COD_CAMPOINFORME", mForm.getComboCamposElexidos()[i]);
				salida.add(temp);
			}
		//
		// Para Estatísticas
		//
		//			if (mForm.getComboFormatoInforme().equals("E")) {
		//				temp = new es.altia.util.HashtableWithNull();
		//				temp.put("COD_INFORMEXERADOR", cix);
		//				temp.put("POSICION", new Integer(i));
		//				temp.put("COD_CAMPOINFORME", mForm.getComboCamposElexidos()[i]);
		//			salida.add(temp);
		//			}
		//
		// FIN: Para Estatísticas
		//

		return salida;
	}

	private java.util.Vector creaCamposOrde(
		XeradorInformesForm mForm,
		String cix,
		java.util.Collection camposSeleccion) {
		java.util.Vector salida = new java.util.Vector();
		es.altia.util.HashtableWithNull temp =
			new es.altia.util.HashtableWithNull();

		// Modificado para que tome los valores de los nuevos combos
		if ((mForm.getCodCampoOrdenacion_1() != null)
			&& !(mForm.getCodCampoOrdenacion_1().trim().equals(""))) {
			temp.put("COD_INFORMEXERADOR", cix);
			temp.put(
				"POSICION",
				buscaPosicionDeCampoEnSeleccion(
					mForm.getCodCampoOrdenacion_1(),
					camposSeleccion));
			temp.put("COD_CAMPOINFORME", mForm.getCodCampoOrdenacion_1());
			temp.put("TIPOORDE", mForm.getCodSentidoOrdenacion_1());
			salida.add(temp);
		}

		if ((mForm.getCodCampoOrdenacion_2() != null)
			&& !(mForm.getCodCampoOrdenacion_2().trim().equals(""))) {

			temp = new es.altia.util.HashtableWithNull();
			temp.put("COD_INFORMEXERADOR", cix);
			temp.put(
				"POSICION",
				buscaPosicionDeCampoEnSeleccion(
					mForm.getCodCampoOrdenacion_2(),
					camposSeleccion));

			temp.put("COD_CAMPOINFORME", mForm.getCodCampoOrdenacion_2());
			temp.put("TIPOORDE", mForm.getCodSentidoOrdenacion_2());
			salida.add(temp);
		}
		if ((mForm.getCodCampoOrdenacion_3() != null)
			&& !(mForm.getCodCampoOrdenacion_3().trim().equals(""))) {

			temp = new es.altia.util.HashtableWithNull();
			temp.put("COD_INFORMEXERADOR", cix);
			temp.put(
				"POSICION",
				buscaPosicionDeCampoEnSeleccion(
					mForm.getCodCampoOrdenacion_3(),
					camposSeleccion));

			temp.put("COD_CAMPOINFORME", mForm.getCodCampoOrdenacion_3());
			temp.put("TIPOORDE", mForm.getCodSentidoOrdenacion_3());
			salida.add(temp);
		}
		if ((mForm.getCodCampoOrdenacion_4() != null)
			&& !(mForm.getCodCampoOrdenacion_4().trim().equals(""))) {

			temp = new es.altia.util.HashtableWithNull();
			temp.put("COD_INFORMEXERADOR", cix);
			temp.put(
				"POSICION",
				buscaPosicionDeCampoEnSeleccion(
					mForm.getCodCampoOrdenacion_4(),
					camposSeleccion));

			temp.put("COD_CAMPOINFORME", mForm.getCodCampoOrdenacion_4());
			temp.put("TIPOORDE", mForm.getCodSentidoOrdenacion_4());
			salida.add(temp);
		}
		if ((mForm.getCodCampoOrdenacion_5() != null)
			&& !(mForm.getCodCampoOrdenacion_5().trim().equals(""))) {

			temp = new es.altia.util.HashtableWithNull();
			temp.put("COD_INFORMEXERADOR", cix);
			temp.put(
				"POSICION",
				buscaPosicionDeCampoEnSeleccion(
					mForm.getCodCampoOrdenacion_5(),
					camposSeleccion));

			temp.put("COD_CAMPOINFORME", mForm.getCodCampoOrdenacion_5());
			temp.put("TIPOORDE", mForm.getCodSentidoOrdenacion_5());
			salida.add(temp);
		}

		////
		//  if ((mForm.getComboCampoOrdenacion_1() != null)
		//		  && !(mForm.getComboCampoOrdenacion_1().trim().equals(""))) {
		//		  temp.put("COD_INFORMEXERADOR", cix);
		//		  temp.put(
		//			  "POSICION",
		//			  buscaPosicionDeCampoEnSeleccion(
		//				  mForm.getComboCampoOrdenacion_1(),
		//				  camposSeleccion));
		//		  temp.put("COD_CAMPOINFORME", mForm.getComboCampoOrdenacion_1());
		//		  temp.put("TIPOORDE", mForm.getComboSentidoOrdenacion_1());
		//		  salida.add(temp);
		//	  }
		//
		//	  if ((mForm.getComboCampoOrdenacion_2() != null)
		//		  && !(mForm.getComboCampoOrdenacion_2().trim().equals(""))) {
		//
		//		  temp = new es.altia.util.HashtableWithNull();
		//		  temp.put("COD_INFORMEXERADOR", cix);
		//		  temp.put(
		//			  "POSICION",
		//			  buscaPosicionDeCampoEnSeleccion(
		//				  mForm.getComboCampoOrdenacion_2(),
		//				  camposSeleccion));
		//
		//		  temp.put("COD_CAMPOINFORME", mForm.getComboCampoOrdenacion_2());
		//		  temp.put("TIPOORDE", mForm.getComboSentidoOrdenacion_2());
		//		  salida.add(temp);
		//	  }
		//	  if ((mForm.getComboCampoOrdenacion_3() != null)
		//		  && !(mForm.getComboCampoOrdenacion_3().trim().equals(""))) {
		//
		//		  temp = new es.altia.util.HashtableWithNull();
		//		  temp.put("COD_INFORMEXERADOR", cix);
		//		  temp.put(
		//			  "POSICION",
		//			  buscaPosicionDeCampoEnSeleccion(
		//				  mForm.getComboCampoOrdenacion_3(),
		//				  camposSeleccion));
		//
		//		  temp.put("COD_CAMPOINFORME", mForm.getComboCampoOrdenacion_3());
		//		  temp.put("TIPOORDE", mForm.getComboSentidoOrdenacion_3());
		//		  salida.add(temp);
		//	  }
		//	  if ((mForm.getComboCampoOrdenacion_4() != null)
		//		  && !(mForm.getComboCampoOrdenacion_4().trim().equals(""))) {
		//
		//		  temp = new es.altia.util.HashtableWithNull();
		//		  temp.put("COD_INFORMEXERADOR", cix);
		//		  temp.put(
		//			  "POSICION",
		//			  buscaPosicionDeCampoEnSeleccion(
		//				  mForm.getComboCampoOrdenacion_4(),
		//				  camposSeleccion));
		//
		//		  temp.put("COD_CAMPOINFORME", mForm.getComboCampoOrdenacion_4());
		//		  temp.put("TIPOORDE", mForm.getComboSentidoOrdenacion_4());
		//		  salida.add(temp);
		//	  }
		//	  if ((mForm.getComboCampoOrdenacion_5() != null)
		//		  && !(mForm.getComboCampoOrdenacion_5().trim().equals(""))) {
		//
		//		  temp = new es.altia.util.HashtableWithNull();
		//		  temp.put("COD_INFORMEXERADOR", cix);
		//		  temp.put(
		//			  "POSICION",
		//			  buscaPosicionDeCampoEnSeleccion(
		//				  mForm.getComboCampoOrdenacion_5(),
		//				  camposSeleccion));
		//
		//		  temp.put("COD_CAMPOINFORME", mForm.getComboCampoOrdenacion_5());
		//		  temp.put("TIPOORDE", mForm.getComboSentidoOrdenacion_5());
		//		  salida.add(temp);
		//	  }
		////
		return salida;
	}

	private java.util.Vector creaCamposCondicion(
		XeradorInformesForm mForm,
		String cix) {
		java.util.Vector salida = new java.util.Vector();
		es.altia.util.HashtableWithNull temp =
			new es.altia.util.HashtableWithNull();

		if ((mForm.getCodCampo_1() != null)
			&& !(mForm.getCodCampo_1().trim().equals(""))
			&& ((mForm.getTextValor_1() != null)
				&& !(mForm.getTextValor_1().toString().equals(""))
				|| (mForm.getCodCondicion_1().trim().equals("IS NOT NULL"))
				|| (mForm.getCodCondicion_1().trim().equals("IS NULL")))) {

			temp = new es.altia.util.HashtableWithNull();
			temp.put("COD_INFORMEXERADOR", cix);
			temp.put("POSICION", new Integer(0));
			temp.put("CLAUSULA", null);
			temp.put("COD_CAMPOINFORME", mForm.getCodCampo_1());
			temp.put("OPERADOR", mForm.getCodCondicion_1());
			temp.put("VALOR", mForm.getTextValor_1());
			salida.add(temp);
		}
		if ((mForm.getCodCampo_2() != null)
			&& !(mForm.getCodCampo_2().trim().equals(""))
			&& (mForm.getTextValor_2() != null)
			&& !(mForm.getTextValor_2().toString().equals(""))) {

			temp = new es.altia.util.HashtableWithNull();
			temp.put("COD_INFORMEXERADOR", cix);
			temp.put("POSICION", new Integer(1));
			temp.put("CLAUSULA", mForm.getCodOperador_2());
			temp.put("COD_CAMPOINFORME", mForm.getCodCampo_2());
			temp.put("OPERADOR", mForm.getCodCondicion_2());
			temp.put("VALOR", mForm.getTextValor_2());
			salida.add(temp);
		}

		if ((mForm.getCodCampo_3() != null)
			&& !(mForm.getCodCampo_3().trim().equals(""))
			&& (mForm.getTextValor_3() != null)
			&& !(mForm.getTextValor_3().toString().equals(""))) {

			temp = new es.altia.util.HashtableWithNull();
			temp.put("COD_INFORMEXERADOR", cix);
			temp.put("POSICION", new Integer(2));
			temp.put("CLAUSULA", mForm.getCodOperador_3());
			temp.put("COD_CAMPOINFORME", mForm.getCodCampo_3());
			temp.put("OPERADOR", mForm.getCodCondicion_3());
			temp.put("VALOR", mForm.getTextValor_3());
			salida.add(temp);
		}

		if ((mForm.getCodCampo_4() != null)
			&& !(mForm.getCodCampo_4().trim().equals(""))
			&& (mForm.getTextValor_4() != null)
			&& !(mForm.getTextValor_4().toString().equals(""))) {

			temp = new es.altia.util.HashtableWithNull();
			temp.put("COD_INFORMEXERADOR", cix);
			temp.put("POSICION", new Integer(3));
			temp.put("CLAUSULA", mForm.getCodOperador_4());
			temp.put("COD_CAMPOINFORME", mForm.getCodCampo_4());
			temp.put("OPERADOR", mForm.getCodCondicion_4());
			temp.put("VALOR", mForm.getTextValor_4());
			salida.add(temp);
		}

		if ((mForm.getCodCampo_5() != null)
			&& !(mForm.getCodCampo_5().trim().equals(""))
			&& (mForm.getTextValor_5() != null)
			&& !(mForm.getTextValor_5().toString().equals(""))) {

			temp = new es.altia.util.HashtableWithNull();
			temp.put("COD_INFORMEXERADOR", cix);
			temp.put("POSICION", new Integer(4));
			temp.put("CLAUSULA", mForm.getCodOperador_5());
			temp.put("COD_CAMPOINFORME", mForm.getCodCampo_5());
			temp.put("OPERADOR", mForm.getCodCondicion_5());
			temp.put("VALOR", mForm.getTextValor_5());
			salida.add(temp);
		}

		if ((mForm.getCodCampo_6() != null)
			&& !(mForm.getCodCampo_6().trim().equals(""))
			&& (mForm.getTextValor_6() != null)
			&& !(mForm.getTextValor_6().toString().equals(""))) {

			temp = new es.altia.util.HashtableWithNull();
			temp.put("COD_INFORMEXERADOR", cix);
			temp.put("POSICION", new Integer(5));
			temp.put("CLAUSULA", mForm.getCodOperador_6());
			temp.put("COD_CAMPOINFORME", mForm.getCodCampo_6());
			temp.put("OPERADOR", mForm.getCodCondicion_6());
			temp.put("VALOR", mForm.getTextValor_6());
			salida.add(temp);
		}

		if ((mForm.getCodCampo_7() != null)
			&& !(mForm.getCodCampo_7().trim().equals(""))
			&& (mForm.getTextValor_7() != null)
			&& !(mForm.getTextValor_7().toString().equals(""))) {

			temp = new es.altia.util.HashtableWithNull();
			temp.put("COD_INFORMEXERADOR", cix);
			temp.put("POSICION", new Integer(6));
			temp.put("CLAUSULA", mForm.getCodOperador_7());
			temp.put("COD_CAMPOINFORME", mForm.getCodCampo_7());
			temp.put("OPERADOR", mForm.getCodCondicion_7());
			temp.put("VALOR", mForm.getTextValor_7());
			salida.add(temp);
		}

		//		//
		//		if ((mForm.getComboCampo_1() != null)
		//			&& !(mForm.getComboCampo_1().trim().equals(""))
		//			&& (mForm.getTextValor_1() != null)
		//			&& !(mForm.getTextValor_1().toString().equals(""))) {
		//
		//			temp = new es.altia.util.HashtableWithNull();
		//			temp.put("COD_INFORMEXERADOR", cix);
		//			temp.put("POSICION", new Integer(0));
		//			temp.put("CLAUSULA", null);
		//			temp.put("COD_CAMPOINFORME", mForm.getComboCampo_1());
		//			temp.put("OPERADOR", mForm.getComboCondicion_1());
		//			temp.put("VALOR", mForm.getTextValor_1());
		//			salida.add(temp);
		//		}
		//		if ((mForm.getComboCampo_2() != null)
		//			&& !(mForm.getComboCampo_2().trim().equals(""))
		//			&& (mForm.getTextValor_2() != null)
		//			&& !(mForm.getTextValor_2().toString().equals(""))) {
		//
		//			temp = new es.altia.util.HashtableWithNull();
		//			temp.put("COD_INFORMEXERADOR", cix);
		//			temp.put("POSICION", new Integer(1));
		//			temp.put("CLAUSULA", mForm.getComboOperador_2());
		//			temp.put("COD_CAMPOINFORME", mForm.getComboCampo_2());
		//			temp.put("OPERADOR", mForm.getComboCondicion_2());
		//			temp.put("VALOR", mForm.getTextValor_2());
		//			salida.add(temp);
		//		}
		//
		//		if ((mForm.getComboCampo_3() != null)
		//			&& !(mForm.getComboCampo_3().trim().equals(""))
		//			&& (mForm.getTextValor_3() != null)
		//			&& !(mForm.getTextValor_3().toString().equals(""))) {
		//
		//			temp = new es.altia.util.HashtableWithNull();
		//			temp.put("COD_INFORMEXERADOR", cix);
		//			temp.put("POSICION", new Integer(2));
		//			temp.put("CLAUSULA", mForm.getComboOperador_3());
		//			temp.put("COD_CAMPOINFORME", mForm.getComboCampo_3());
		//			temp.put("OPERADOR", mForm.getComboCondicion_3());
		//			temp.put("VALOR", mForm.getTextValor_3());
		//			salida.add(temp);
		//		}
		//
		//		if ((mForm.getComboCampo_4() != null)
		//			&& !(mForm.getComboCampo_4().trim().equals(""))
		//			&& (mForm.getTextValor_4() != null)
		//			&& !(mForm.getTextValor_4().toString().equals(""))) {
		//
		//			temp = new es.altia.util.HashtableWithNull();
		//			temp.put("COD_INFORMEXERADOR", cix);
		//			temp.put("POSICION", new Integer(3));
		//			temp.put("CLAUSULA", mForm.getComboOperador_4());
		//			temp.put("COD_CAMPOINFORME", mForm.getComboCampo_4());
		//			temp.put("OPERADOR", mForm.getComboCondicion_4());
		//			temp.put("VALOR", mForm.getTextValor_4());
		//			salida.add(temp);
		//		}
		//
		//		if ((mForm.getComboCampo_5() != null)
		//			&& !(mForm.getComboCampo_5().trim().equals(""))
		//			&& (mForm.getTextValor_5() != null)
		//			&& !(mForm.getTextValor_5().toString().equals(""))) {
		//
		//			temp = new es.altia.util.HashtableWithNull();
		//			temp.put("COD_INFORMEXERADOR", cix);
		//			temp.put("POSICION", new Integer(4));
		//			temp.put("CLAUSULA", mForm.getComboOperador_5());
		//			temp.put("COD_CAMPOINFORME", mForm.getComboCampo_5());
		//			temp.put("OPERADOR", mForm.getComboCondicion_5());
		//			temp.put("VALOR", mForm.getTextValor_5());
		//			salida.add(temp);
		//		}
		//
		//		if ((mForm.getComboCampo_6() != null)
		//			&& !(mForm.getComboCampo_6().trim().equals(""))
		//			&& (mForm.getTextValor_6() != null)
		//			&& !(mForm.getTextValor_6().toString().equals(""))) {
		//
		//			temp = new es.altia.util.HashtableWithNull();
		//			temp.put("COD_INFORMEXERADOR", cix);
		//			temp.put("POSICION", new Integer(5));
		//			temp.put("CLAUSULA", mForm.getComboOperador_6());
		//			temp.put("COD_CAMPOINFORME", mForm.getComboCampo_6());
		//			temp.put("OPERADOR", mForm.getComboCondicion_6());
		//			temp.put("VALOR", mForm.getTextValor_6());
		//			salida.add(temp);
		//		}
		//
		//		if ((mForm.getComboCampo_7() != null)
		//			&& !(mForm.getComboCampo_7().trim().equals(""))
		//			&& (mForm.getTextValor_7() != null)
		//			&& !(mForm.getTextValor_7().toString().equals(""))) {
		//
		//			temp = new es.altia.util.HashtableWithNull();
		//			temp.put("COD_INFORMEXERADOR", cix);
		//			temp.put("POSICION", new Integer(6));
		//			temp.put("CLAUSULA", mForm.getComboOperador_7());
		//			temp.put("COD_CAMPOINFORME", mForm.getComboCampo_7());
		//			temp.put("OPERADOR", mForm.getComboCondicion_7());
		//			temp.put("VALOR", mForm.getTextValor_7());
		//			salida.add(temp);
		//		}
		//
		//		//
		return salida;
	}

	private EstructuraEntidades creaEstructuraXeradorInformes(
			es.altia.util.HashtableWithNull tabla)
		throws Exception {
		EstructuraEntidades salida =new EstructuraEntidades(
					(String) tabla.get("COD_INFORMEXERADOR"),
					(String) tabla.get("COD_ENTIDADEINFORME"));

		Vector cs = (Vector) tabla.get("CAMPOSSELECCION");

		Vector co = (Vector) tabla.get("CAMPOSORDE");
		Vector cc = (Vector) tabla.get("CAMPOSCONDICION");

		es.altia.util.HashtableWithNull temp = null;

		java.util.Iterator iter = null;
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.XeracionInformes
			.CampoCondicionInforme cci =
			null;
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.XeracionInformes
			.CampoSeleccionInforme csi =
			null;
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.XeracionInformes
			.CampoOrdeInforme coi =
			null;

		java.util.Vector vectorCampos = null;
		//
		//Aquí consulto los datos del campo y los pongo
		es.altia.util.HashtableWithNull tabla2 =
			new es.altia.util.HashtableWithNull();
		es.altia.util.HashtableWithNull temp2 = null;
		java.util.Iterator iterCampos = null;

		tabla2.put("PARAMS", params);
		tabla2.put("COD_ENTIDADEINFORME",(String) tabla.get("COD_ENTIDADEINFORME"));

		es.altia.util.conexion.Cursor cursorEntidade =
			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.UtilidadesXerador
				.consultaDatosEntidadeInforme(tabla2);
		if (cursorEntidade.next()) {
			salida.setNomeEntidade(cursorEntidade.getString("NOME"));
			salida.setNomeVista(cursorEntidade.getString("NOMEVISTA"));

		}

		if (cs != null) {
			iter = cs.iterator();
			//System.err.println("nomeEle" + cs.size() + ".cs:" + cs + ".");

			tabla2.put("CAMPOS", cs);
			vectorCampos =UtilidadesXerador.consultaDatosCamposXerador(tabla2);
			iterCampos = vectorCampos.iterator();

			while (iter.hasNext()) {
				temp = (es.altia.util.HashtableWithNull) iter.next();
				temp2 = (es.altia.util.HashtableWithNull) iterCampos.next();

				csi =
					new es
						.altia
						.agora
						.business
						.geninformes
						.utils
						.XeracionInformes
						.CampoSeleccionInforme();

				csi.setCodCampoInforme((String) temp.get("COD_CAMPOINFORME"));
				//System.err.println(
				//	"MEto posicion:" + temp.get("POSICION") + "");

				csi.setPosicion(temp.get("POSICION") + "");
				csi.setAncho((String) temp.get("ANCHO"));

				csi.setNomeCampo((String) temp2.get("NOME"));
				csi.setCampo((String) temp2.get("CAMPO"));
				csi.setFromValores("");
				csi.setLonxitudeCampo((String) temp2.get("LONXITUDE"));
				csi.setTipoCampo((String) temp2.get("TIPO"));
				csi.setSelectValores("");
				csi.setWhereValores("");

				//
				salida.addCampo(csi);
			}
		}

		if (cc != null) {
			iter = cc.iterator();
			tabla2.put("CAMPOS", cc);
			vectorCampos =
				es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.UtilidadesXerador
					.consultaDatosCamposXerador(tabla2);
			iterCampos = vectorCampos.iterator();
			while (iter.hasNext()) {
				temp = (es.altia.util.HashtableWithNull) iter.next();
				temp2 = (es.altia.util.HashtableWithNull) iterCampos.next();

				cci =
					new es
						.altia
						.agora
						.business
						.geninformes
						.utils
						.XeracionInformes
						.CampoCondicionInforme();

				cci.setCodCampoInforme((String) temp.get("COD_CAMPOINFORME"));
				cci.setPosicion(temp.get("POSICION") + "");
				cci.setClausula((String) temp.get("CLAUSULA"));
				cci.setOperador((String) temp.get("OPERADOR"));
				cci.setValor((String) temp.get("VALOR"));

				cci.setNomeCampo((String) temp2.get("NOME"));
				cci.setCampo((String) temp2.get("CAMPO"));
				cci.setFromValores("");
				cci.setLonxitudeCampo((String) temp2.get("LONXITUDE"));
				cci.setTipoCampo((String) temp2.get("TIPO"));
				cci.setSelectValores("");
				cci.setWhereValores("");

				salida.addCampo(cci);
			}
		}

		if (co != null) {
			iter = co.iterator();
			tabla2.put("CAMPOS", co);
			vectorCampos =
				es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.UtilidadesXerador
					.consultaDatosCamposXerador(tabla2);
			iterCampos = vectorCampos.iterator();

			while (iter.hasNext()) {
				temp = (es.altia.util.HashtableWithNull) iter.next();
				temp2 = (es.altia.util.HashtableWithNull) iterCampos.next();

				coi =
					new es
						.altia
						.agora
						.business
						.geninformes
						.utils
						.XeracionInformes
						.CampoOrdeInforme();

				coi.setCodCampoInforme((String) temp.get("COD_CAMPOINFORME"));
				coi.setPosicion(temp.get("POSICION") + "");

				coi.setTipoOrde((String) temp.get("TIPOORDE"));

				coi.setNomeCampo((String) temp2.get("NOME"));
				coi.setCampo((String) temp2.get("CAMPO"));
				coi.setFromValores("");
				coi.setLonxitudeCampo((String) temp2.get("LONXITUDE"));
				coi.setTipoCampo((String) temp2.get("TIPO"));
				coi.setSelectValores("");
				coi.setWhereValores("");

				salida.addCampo(coi);
			}
		}

		return salida;
	}

	private Integer buscaPosicionDeCampoEnSeleccion(
		String codCampoInforme,
		java.util.Collection camposSeleccion) {
		es.altia.util.HashtableWithNull temp = null;
		Integer salida = null;
		boolean atopado = false;
		java.util.Iterator iter = camposSeleccion.iterator();
		while ((iter.hasNext()) && (!atopado)) {
			temp = (es.altia.util.HashtableWithNull) iter.next();
			atopado =
				(temp
					.get("COD_CAMPOINFORME")
					.toString()
					.equals(codCampoInforme));
		}

		salida = (atopado) ? ((Integer) temp.get("POSICION")) : null;

		return salida;
	}

	//
	// Fin código antiguo
	//

	//}	

	private Registro obtenerMasParametros(
		Registro parametros,
		HttpServletRequest request)
		throws Exception {
		try {
			HttpSession session = request.getSession();
			//	MantAnotacionRegistroForm marf =
			//		(MantAnotacionRegistroForm) session.getAttribute(
			//			"MantAnotacionRegistroForm");
			//	if (marf != null) {
			//		RegistroValueObject rvo = marf.getRegistro();
			//		if (rvo != null) {
			//			parametros.setString(
			//				"identDepart",
			//				rvo.getIdentDepart() + "");
			//			parametros.setString(
			//				"unidadOrgan",
			//				rvo.getUnidadOrgan() + "");
			//			parametros.setString("tipoReg", rvo.getTipoReg());
			//			parametros.setString("anoReg", rvo.getAnoReg() + "");
			//			parametros.setString("numReg", rvo.getNumReg() + "");
			//		}
			//	}

			String host = obtenerHostLocal(request);
			parametros.setString("host", host);

			return parametros;
		} catch (Exception e) {
			throw e;
		}
	}

	private Registro obtenerParametros(HttpServletRequest request)
		throws Exception {
		try {
			Registro parametros = new Registro();

			parametros.setString("opcion", request.getParameter("opcion"));
			parametros.setString("plt_des", request.getParameter("plt_des"));
			parametros.setString("plt_cod", request.getParameter("plt_cod"));
			parametros.setString("plt_apl", request.getParameter("plt_apl"));
			parametros.setString(
				"tipVentana",
				request.getParameter("tipVentana"));
			parametros.setString(
				"codClasif",
				request.getParameter("codClasif"));
			parametros.setString(
				"descClasif",
				request.getParameter("descClasif"));

			return parametros = obtenerMasParametros(parametros, request);
		} catch (Exception e) {
			throw e;
		}
	}

	private String[] obtenerParametrosConexion(HttpServletRequest request)
		throws Exception {
		try {
			HttpSession session = request.getSession();
			UsuarioValueObject usuarioVO = null;
			String[] params = null;

			if (session.getAttribute("usuario") != null) {
				usuarioVO =
					(UsuarioValueObject) session.getAttribute("usuario");
				params = usuarioVO.getParamsCon();
			}

			return params;
		} catch (Exception e) {
			throw e;
		}
	}

	private String obtenerHostLocal(HttpServletRequest req) throws Exception {
		try {
			String host = "";
            String contexto = "";
            if (req.getHeader("Host") != null) {
				host = req.getHeader("Host") + req.getContextPath();			
				String protocolo = StrutsUtilOperations.getProtocol(req);                				
				if (host != null)
					host = protocolo + "://" + host + "/";
			}
			return host;
		} catch (Exception e) {
			throw e;
		}
	}

	private void meteDatosProba(es.altia.util.HashtableWithNull tabla) {
		tabla.put("COD_INFORMEXERADOR", "");
		String ran = Math.random() + "";

		tabla.put("NOME", "Nome" + ran);
		tabla.put("DESCRIPCION", "Descripción de proba" + ran);
		tabla.put("APL_COD", "3");
		tabla.put("MUN_PAI", "34");
		tabla.put("MUN_PRV", "15");
		tabla.put("MUN_COD", "009");

		tabla.put("FORMATO", "E");
		tabla.put("COD_ENTIDADEINFORME", "1");
		tabla.put("LISTADOXERADOR", "");
		java.util.Vector camposSeleccion = new java.util.Vector();
		java.util.Vector camposOrde = new java.util.Vector();

		java.util.Vector camposCondicion = new java.util.Vector();
		es.altia.util.HashtableWithNull temp =
			new es.altia.util.HashtableWithNull();

		temp = new es.altia.util.HashtableWithNull();
		temp.put("COD_INFORMEXERADOR", "");
		temp.put("POSICION", "0");
		temp.put("COD_CAMPOINFORME", "3");
		camposSeleccion.add(temp);

		temp = new es.altia.util.HashtableWithNull();
		temp.put("COD_INFORMEXERADOR", "");
		temp.put("POSICION", "1");
		temp.put("COD_CAMPOINFORME", "2");
		camposSeleccion.add(temp);

		temp = new es.altia.util.HashtableWithNull();
		temp.put("COD_INFORMEXERADOR", "");
		temp.put("POSICION", "2");
		temp.put("COD_CAMPOINFORME", "1");
		camposSeleccion.add(temp);
		//System.err.println("camposSeleccion:"+camposSeleccion+"."+camposSeleccion.size()+".");

		tabla.put("CAMPOSSELECCION", camposSeleccion);
		camposSeleccion = null;

		temp = new es.altia.util.HashtableWithNull();

		temp.put("COD_INFORMEXERADOR", "");
		temp.put("POSICION", (new Integer(0)));
		temp.put("COD_CAMPOINFORME", "1");
		temp.put("TIPOORDE", "D");
		camposOrde.add(temp);

		temp = new es.altia.util.HashtableWithNull();
		temp.put("COD_INFORMEXERADOR", "");
		temp.put("POSICION", (new Integer(1)));
		temp.put("COD_CAMPOINFORME", "2");
		temp.put("TIPOORDE", "D");
		camposOrde.add(temp);

		tabla.put("CAMPOSORDE", camposOrde);

		temp = new es.altia.util.HashtableWithNull();
		temp.put("COD_INFORMEXERADOR", "");
		temp.put("POSICION", (new Integer(0)));
		temp.put("CLAUSULA", null);
		temp.put("COD_CAMPOINFORME", "1");
		temp.put("OPERADOR", "<");
		temp.put("VALOR", "55");
		camposCondicion.add(temp);

		temp = new es.altia.util.HashtableWithNull();
		temp.put("COD_INFORMEXERADOR", "");
		temp.put("POSICION", (new Integer(1)));
		temp.put("CLAUSULA", "OR");
		temp.put("COD_CAMPOINFORME", "2");
		temp.put("OPERADOR", ">=");
		temp.put("VALOR", "18");
		camposCondicion.add(temp);

		tabla.put("CAMPOSCONDICION", camposCondicion);

	}

}
