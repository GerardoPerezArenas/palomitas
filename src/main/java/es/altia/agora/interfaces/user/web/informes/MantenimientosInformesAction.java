package es.altia.agora.interfaces.user.web.informes;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.common.service.config.*;
import es.altia.common.util.*;
import es.altia.util.struts.StrutsUtilOperations;

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

public final class MantenimientosInformesAction extends ActionSession {
	String[] params = null;

	protected static Log m_Log =
		LogFactory.getLog(MantenimientosInformesAction.class.getName());
	public ActionForward performSession(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {

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

		es
			.altia
			.agora
			.interfaces
			.user
			.web
			.informes
			.MantenimientosInformesForm mForm =
			(es
				.altia
				.agora
				.interfaces
				.user
				.web
				.informes
				.MantenimientosInformesForm) form;

		es
			.altia
			.agora
			.business
			.geninformes
			.GeneradorInformesMgr _opXeradorInformes =
			new es.altia.agora.business.geninformes.GeneradorInformesMgr(
				params);

		m_Log.debug("Antes de todo.");

		try {
			String operacion = request.getParameter("operacion");
			if (operacion.equals("listaEstructuras")) {
				af = (mapping.findForward("listaEstructuras"));
			} else if (operacion.equals("listaEntidades")) {
				af = (mapping.findForward("listaEntidades"));
			} else {

				HttpSession sesion = request.getSession(true);

				es.altia.util.HashtableWithNull hash_Datos =
					new es.altia.util.HashtableWithNull();
				es.altia.util.HashtableWithNull listadoXerador = null;
				es.altia.util.HashtableWithNull etiquetaXerador = null;
				java.util.Vector camposInforme = new java.util.Vector();
				java.util.Vector aplicaciones = new java.util.Vector();
				java.util.Vector subEntidades = new java.util.Vector();
				java.util.Vector camposSeleccion = new java.util.Vector();
				java.util.Vector camposOrde = new java.util.Vector();
				java.util.Vector camposCondicion = new java.util.Vector();
				String codAplicacion = mForm.getCodAplicacion();
				hash_Datos.put("APL_COD", codAplicacion);

				java.util.Vector vectorSubes = new java.util.Vector();
				if ((mForm.getListaSubentidadeCodigo() != null)
					&& (mForm.getListaSubentidadeCodigo().length > 0)) {
					for (int i = 0;
						i < mForm.getListaSubentidadeCodigo().length;
						i++) {
						vectorSubes.add(mForm.getListaSubentidadeCodigo()[i]);
					}
					hash_Datos.put("SUBESTRUCTURAS", vectorSubes);
				}
                m_Log.debug("SUBESTRUCTURAS: "+hash_Datos.get("SUBESTRUCTURAS"));

				hash_Datos.put("COD_INFORMEXERADOR",mForm.getCOD_INFORMEXERADOROculto());
				hash_Datos.put("NOME", mForm.getTextNomeInforme());
				hash_Datos.put("DESCRIPCION", mForm.getTextDescripcion());
				hash_Datos.put("FORMATO", mForm.getComboFormatoInforme());
				hash_Datos.put("COD_ENTIDADEINFORME",mForm.getCodEntidadeInforme());
				m_Log.debug("****************************************************************************************");
                m_Log.debug("En MantenimientosInformesAction COD_ENTIDADEINFORME: "+hash_Datos.get("COD_ENTIDADEINFORME"));
                m_Log.debug("En MantenimientosInformesAction COD_INFORMEXERADOR: "+hash_Datos.get("COD_INFORMEXERADOR"));
                m_Log.debug("En MantenimientosInformesAction NOME: "+hash_Datos.get("NOME"));
                m_Log.debug("En MantenimientosInformesAction DESCRIPCION: "+hash_Datos.get("DESCRIPCION"));
                m_Log.debug("En MantenimientosInformesAction OPERACION: "+hash_Datos.get("operacion"));
				m_Log.debug("****************************************************************************************");
				//hash_Datos.put("APL_COD", "3"); //usuarioVO.getAppCod()+"");
				hash_Datos.put("FORMATO", "L");

				if ((!(operacion.equals("CE")
					|| operacion.equals("BE")
					|| operacion.equals("CLE")))
					&& (!(operacion.equals("C")
						|| operacion.equals("B")
						|| operacion.equals("CLEST")))
					&& (!(operacion.equals("A")
						|| operacion.equals("B")
						|| operacion.equals("M")))) {
                    m_Log.debug("Entro en op BE o CLE o B o CLEST o B o M en MantenimientoInformesAction.");
					camposInforme = creaCamposInforme(mForm);
					aplicaciones = creaAplicaciones(mForm);
					subEntidades = creaSubEntidades(mForm);

					hash_Datos.put("CAMPOSINFORME", camposInforme);
					hash_Datos.put("APLICACIONES", aplicaciones);
					hash_Datos.put("SUBENTIDADES", subEntidades);

					m_Log.debug("Desp meter datos proba.");

				}
				//			}

				if ((!(operacion.equals("C")
					|| operacion.equals("B")
					|| operacion.equals("CLEST")))
					&& (!(operacion.equals("C")
						|| operacion.equals("B")
						|| operacion.equals("CLE")))
					&& (!(operacion.equals("AE")
						|| operacion.equals("BE")
						|| operacion.equals("CLE")))) {
                    m_Log.debug("Entro en op B o CLEST o B o CLE o BE o CLE en MantenimientoInformesAction.");
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

					es
						.altia
						.agora
						.business
						.geninformes
						.utils
						.XeracionInformes
						.EstructuraEntidades ee =
						creaEstructuraXeradorInformes(hash_Datos);
					m_Log.debug("Desp meter datos proba.");
					hash_Datos.put("ESTRUCTURAINFORME", ee);
				}

				if (operacion.equals("CE")) { //Consultar Entidade
					m_Log.debug("Entro en op CE en MantenimientoInformesAction.");

					hash_Datos.put("COD_ENTIDADEINFORME",mForm.getCOD_INFORMEXERADOROculto());
					m_Log.debug("Entro en op C en  MantenimientoInformesAction.Recibo:" + hash_Datos);
					es.altia.util.HashtableWithNull salida = _opXeradorInformes.ConsultarEntidadeInforme(hash_Datos);

					//m_Log.debug("En MantCapacidadesAction C .La hash_del cursor es:"+cursor.hash+".hash_Nombres es:"+cursor.hash_Nombres+".");
					es.altia.util.conexion.Cursor cursor = (es.altia.util.conexion.Cursor) salida.get("ENTIDADE");

					request.setAttribute(
						"ENTIDADE",
						es
							.altia
							.agora
							.business
							.geninformes
							.utils
							.Utilidades
							.ConvertirCursorToArrayJSEscaped(cursor));

					cursor = (es.altia.util.conexion.Cursor) salida.get("SUBENTIDADES");
					request.setAttribute("SUBENTIDADES",
						    es
							.altia
							.agora
							.business
							.geninformes
							.utils
							.Utilidades
							.ConvertirCursorToArrayJSEscaped(cursor));

					cursor = (es.altia.util.conexion.Cursor) salida.get("CAMPOS");
					request.setAttribute("CAMPOS",
						    es
							.altia
							.agora
							.business
							.geninformes
							.utils
							.Utilidades
							.ConvertirCursorToArrayJSEscaped(cursor));

					cursor = (es.altia.util.conexion.Cursor) salida.get("APLICACIONES");
					request.setAttribute("APLICACIONES",
						    es
							.altia
							.agora
							.business
							.geninformes
							.utils
							.Utilidades
							.ConvertirCursorToArrayJSEscaped(cursor));

				} else if (
					operacion.equals("CLE")) { // Consulta de lista de ENTIDADES
					m_Log.debug("Entro en op CLE en MantenimientoInformesAction.");

					es.altia.util.conexion.Cursor cursor = _opXeradorInformes.ConsultarEntidadesInforme(hash_Datos);

					request.setAttribute("vectorInformes",
						es
							.altia
							.agora
							.business
							.geninformes
							.utils
							.Utilidades
							.ConvertirCursorToArrayJSEscaped(cursor));

				} else if (operacion.equals("AE")) { // Engadir

					m_Log.debug("Entro en op AE en  MantenimientosInformesAction.");
					m_Log.debug("Entro en op AE en  MantenimientosInformesAction.Recibo:"+ hash_Datos);

					hash_Datos.remove("COD_INFORMEXERADOR");

					hash_Datos.put("NOME", mForm.getNomeEntidade());
					hash_Datos.put("NOMEVISTA", mForm.getNomeVista());

					es.altia.util.conexion.Cursor salida =
						_opXeradorInformes.EngadirEntidadeInforme(hash_Datos);
					if (salida.next())
						request.setAttribute(
							"COD_ENTIDADEINFORME",
							salida.getString("COD_ENTIDADEINFORME"));
					es.altia.util.HashtableWithNull entradaXerador =
						new es.altia.util.HashtableWithNull();

					m_Log.debug(
						"En XeradorInformes AE ._opXeradorInformes.EngadirInformeCentro.Pongo:"
							+ request.getAttribute("COD_ENTIDADEINFORME")
							+ ".");

				} else if (operacion.equals("ME")) { // Modificar
					m_Log.debug("Entro en op ME en MantenimientosInformesAction.");
					m_Log.debug("Entro en op ME en MantenimientosInformesAction.Recibo:"+ hash_Datos + ".");

					hash_Datos.put("NOME", mForm.getNomeEntidade());
					hash_Datos.put("NOMEVISTA", mForm.getNomeVista());
					hash_Datos.put("COD_ENTIDADEINFORME",mForm.getCOD_ENTIDADEINFORMEOculto());
					_opXeradorInformes.ModificarEntidadeInforme(hash_Datos);

					es.altia.util.HashtableWithNull entradaXerador =
						new es.altia.util.HashtableWithNull();

					m_Log.debug("Salgo de ME en MantenimientosInformesAction.");

				} else if (operacion.equals("BE")) { // Eliminar
					m_Log.debug("Entro en op BE en  MantenimientosInformesAction.");
					hash_Datos.put(
						"COD_ENTIDADEINFORME",
						mForm.getCOD_ENTIDADEINFORMEOculto());

					_opXeradorInformes.EliminarEntidadeInforme(hash_Datos);

					m_Log.debug("Salgo de EliminarEntidadeInforme en MantenimientosInformesAction.");

				} else if (operacion.equals("I")) { // Lanzar informe
					m_Log.debug("Entro en op I en  MantenimientosInformesAction.");
					es.altia.util.HashtableWithNull hash_Param =
						new es.altia.util.HashtableWithNull();

					es.altia.util.HashtableWithNull salida =
						_opXeradorInformes.ejecutarInforme(hash_Datos);

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
						byte[] ficheroXML =
							((String) salida.get("XML")).getBytes();

						if (ficheroXML != null) {
							fichero2.write(ficheroXML, 0, ficheroXML.length);
							fichero2.close();
						}
					}
					String salidaXML =
						es
							.altia
							.agora
							.business
							.geninformes
							.utils
							.Utilidades
							.js_escape(
							salida.get("XML").toString());

					request.setAttribute("XML", salidaXML);
					
					String protocolo = StrutsUtilOperations.getProtocol(request);
					m_Log.debug("PROTOCOLO en uso :" + protocolo);

					request.setAttribute(
						"URL_FDOT",
						protocolo + "://"
							+ request.getHeader("Host") + request.getContextPath()
							+ "/temp/documentosGenerador/"
							+ params[0]
							+ "/"
							+ params[6]
							+ "/Documento"
							+ mForm.getCOD_INFORMEXERADOROculto()
							+ ".doc");

					request.setAttribute(
						"URL_XML",
                        protocolo + "://" + request.getHeader("Host") + request.getContextPath()
							+ "/documentos/temp/"
							+ sello
							+ ".xml");

				} else if (operacion.equals("EP")) { // Editar plantilla

				} else if (
					operacion.equals(
						"GEP")) { // Subir y grabar plantilla editada
					org.apache.struts.upload.FormFile formFichero =
						mForm.getFicheroWord();

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
				} else if (operacion.equals("C")) {
					m_Log.debug(
						"Entro en op C en MantenimientoInformesAction.");

					hash_Datos.put(
						"COD_ESTRUCTURA",
						mForm.getCOD_INFORMEXERADOROculto());
					m_Log.debug("Entro en op C en  MantenimientoInformesAction.Recibo:"+ hash_Datos);
					es.altia.util.conexion.Cursor cursor = _opXeradorInformes.ConsultarEstructuraInforme(hash_Datos);
					//m_Log.debug("En MantCapacidadesAction C .La hash_del cursor es:"+cursor.hash+".hash_Nombres es:"+cursor.hash_Nombres+".");

					if (cursor.next()) {
						m_Log.debug("En MantenimientoInformesAction.Camposcondicion es:"
								+ cursor.getObject("CAMPOSCONDICION")
								+ ".Camposorde:"
								+ cursor.getObject("CAMPOSORDE")
								+ ".CamposSeleccion:"
								+ cursor.getObject("CAMPOSSELECCION")
								+ ".");

						m_Log.debug("Cursor de consulta informe é:cuirsor.hashNombres="
								+ cursor.hash_Nombres
								+ ".cursor.hash="
								+ cursor.hash
								+ ".");

						cursor.Indice = -1;
						request.setAttribute(
							"vectorInforme",
							es
								.altia
								.agora
								.business
								.geninformes
								.utils
								.Utilidades
								.ConvertirCursorToArrayJSEscaped(cursor));
						es.altia.util.conexion.Cursor cursorSubentidades =
							_opXeradorInformes.ConsultarSubEstructuras(
								hash_Datos);

						request.setAttribute(
							"vectorSubestructuras",
							es
								.altia
								.agora
								.business
								.geninformes
								.utils
								.Utilidades
								.ConvertirCursorToArrayJSEscaped(cursorSubentidades));
					}
					//m_Log.debug(
					m_Log.debug("Salgo de C en XeradorInformesAction.\nvectorInforme: "+ request.getAttribute("vectorInforme")
							+ " vectorSubestructuras: "+ request.getAttribute("vectorSubestructuras"));

				} else if (
					operacion.equals(
						"CLEST")) { // Consulta de lista de informes
					m_Log.debug("Entro en op CLEST en SecreatriaCorreoEntradasAction.con "+ hash_Datos);

					m_Log.debug("Entro en op CLEST en SecreatriaCorreoEntradasAction.con "+ hash_Datos);

					es.altia.util.conexion.Cursor cursor = _opXeradorInformes.ConsultarEstructurasInforme(hash_Datos);

					request.setAttribute("vectorInformes",
						es
							.altia
							.agora
							.business
							.geninformes
							.utils
							.Utilidades
							.ConvertirCursorToArrayJSEscaped(cursor));

				} else if (operacion.equals("A")) { // Engadir

                    if (m_Log.isDebugEnabled())
					    m_Log.debug("Entro en op A en  MantenimientoInformesAction.Recibo:"+ hash_Datos);

					hash_Datos.remove("COD_INFORMEXERADOR");
					hash_Datos.put("DOC_NOM", mForm.getTextNomeInforme());

					m_Log.debug("Entro en operacion A en  MantenimientoInformesAction.Recibo:"+ hash_Datos);
					m_Log.debug("****************************************");
					m_Log.debug("MEto codentidadeinforme en operacion A: "+hash_Datos.get("COD_ENTIDADEINFORME")+" DOC_NOM: "+hash_Datos.get("DOC_NOM"));
					m_Log.debug("********************************+");
					// Si ya tengo la plantilla lo grabo
					es.altia.util.conexion.Cursor salida =
						_opXeradorInformes.EngadirEstructuraInforme(hash_Datos);
					if (salida.next())
						request.setAttribute("COD_INFORMEXERADOR", "");
					es.altia.util.HashtableWithNull entradaXerador =
						new es.altia.util.HashtableWithNull();

					m_Log.debug(
						"En MantenimientoInformesAction A ._opXeradorInformes.EngadirInformeCentro.Pongo:"
							+ request.getAttribute("COD_INFORMEXERADOR")
							+ ".");

				} else if (operacion.equals("M")) { // Modificar
					m_Log.debug("Entro en op M en MantenimientoInformesAction.");

					hash_Datos.put(
						"COD_ESTRUCTURA",
						mForm.getCOD_INFORMEXERADOROculto());
					m_Log.debug("Entro en op M en MantenimientoInformesAction.Recibo:"+ hash_Datos);
					hash_Datos.put("DOC_NOM", mForm.getTextNomeInforme());

					_opXeradorInformes.ModificarEstructuraInforme(hash_Datos);

					es.altia.util.HashtableWithNull entradaXerador =
						new es.altia.util.HashtableWithNull();

					m_Log.debug("Salgo de M en  INFORMEXERADOR.");

				} else if (operacion.equals("B")) { // Eliminar
					m_Log.debug("Entro en op B en MantenimientoInformesAction.");
					hash_Datos.put(
						"COD_ESTRUCTURA",
						mForm.getCOD_INFORMEXERADOROculto());
					m_Log.debug("Entro en op B en  MantenimientoInformesAction.Recibo:"+ hash_Datos);

					_opXeradorInformes.EliminarEstructuraInforme(hash_Datos);

					m_Log.debug("Salgo de B en XeradorInformesAction.");

				}
			}
			//
			// Inicio código antiguo
			//

			if (m_Log.isDebugEnabled()) m_Log.debug("Antes del if ==nul af:" + af + ".");

			if (af == null)
				af = (mapping.findForward("MantenimientosInformesCorrecto"));

			m_Log.debug("Hago forward a: " + af + ".");

			//			  } // Fin de usuario != null
		} catch (Throwable t) {
			af = (mapping.findForward("errorAlert"));
			//Trtar la excepcion
			//request.setAttribute(es.altia.agora.business.geninformes.utils.Constantes.EXCEPCION, e);
			if (m_Log.isDebugEnabled()) m_Log.debug("Hago forward a :ErrorAlert en XeradorInformesAction:"
					                    + af
					                    + ".Exception es:"
					                    + t
					                    + ".");
			t.printStackTrace(System.err);

		} finally {
			m_Log.debug("En finally.");
		}

		if (m_Log.isDebugEnabled()) m_Log.debug("Hgo forward a :XeradorInformesCorrecto:" + af + ".");

		return af;

	}

	private String preparaString(String entrada, int tamanoMax) {
		String salida = null;
		if (entrada != null) {
			entrada = entrada.trim();
			salida =
				(entrada.length() > tamanoMax)
					? es
						.altia
						.agora
						.business
						.geninformes
						.utils
						.Utilidades
						.recortaString(
						entrada,
						tamanoMax)
					: entrada;
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

	private java.util.Vector creaSubEntidades(
		MantenimientosInformesForm mForm) {
		java.util.Vector salida = new java.util.Vector();
		java.util.Vector camposJoin = new java.util.Vector();
		es.altia.util.HashtableWithNull temp =
			new es.altia.util.HashtableWithNull();

		es.altia.util.HashtableWithNull temp2 = null;
		String anterior;
		boolean cambio = false;

		if (mForm.getListaSubentidadeCodigo() != null) {
			anterior = mForm.getListaSubentidadeCodigo()[0];

			for (int i=0;i<mForm.getListaSubentidadeCodigo().length;i++) {

				cambio = !(anterior.equals(mForm.getListaSubentidadeCodigo()[i]));
                m_Log.debug("mForm.getListaSubentidadeCodigo()["+i+"]: "+ mForm.getListaSubentidadeCodigo()[i]);

				if (cambio) {
					temp.put("COD_ENTIDADEINFORME", anterior);
					temp.put("CAMPOSJOIN", camposJoin);
					salida.add(temp);
					anterior = mForm.getListaSubentidadeCodigo()[i];
					temp = new es.altia.util.HashtableWithNull();
					camposJoin = new java.util.Vector();
                    temp2 = new es.altia.util.HashtableWithNull();
                    temp2.put("CAMPO_ENT",mForm.getListaSubentidadeCampoJoinCampoEnt()[i]);
                    temp2.put("CAMPO_SUBENT",mForm.getListaSubentidadeCampoJoinCampoSubent()[i]);
                    temp2.put("OUTER_JOIN",mForm.getListaSubentidadeCampoJoinOuterJoin()[i]);
                    camposJoin.add(temp2);

				} else {
					temp2 = new es.altia.util.HashtableWithNull();
					temp2.put("CAMPO_ENT",mForm.getListaSubentidadeCampoJoinCampoEnt()[i]);
					temp2.put("CAMPO_SUBENT",mForm.getListaSubentidadeCampoJoinCampoSubent()[i]);
					temp2.put("OUTER_JOIN",mForm.getListaSubentidadeCampoJoinOuterJoin()[i]);
					camposJoin.add(temp2);
				}

			}

			temp.put("COD_ENTIDADEINFORME", anterior);
			temp.put("CAMPOSJOIN", camposJoin);
			salida.add(temp);

		}
        m_Log.debug("SALIDA: "+ salida);
		return salida;
	}

	private java.util.Vector creaCamposInforme(
		MantenimientosInformesForm mForm) {
		java.util.Vector salida = new java.util.Vector();
		es.altia.util.HashtableWithNull temp =
			new es.altia.util.HashtableWithNull();

		if ((mForm.getListaCampoInformeNome() != null)
			&& (mForm.getListaCampoInformeNome().length > 0)) {

			for (int i = 0; i < mForm.getListaCampoInformeNome().length; i++) {
				temp = new es.altia.util.HashtableWithNull();

				temp.put(
					"CODIGO",
					(mForm.getListaCampoInformeCodigo()[i] != null)
						? mForm.getListaCampoInformeCodigo()[i].trim()
						: "");
				temp.put("NOME", mForm.getListaCampoInformeNome()[i]);
				temp.put("CAMPO", mForm.getListaCampoInformeCampo()[i]);
				temp.put("TIPO", mForm.getListaCampoInformeTipo()[i]);
				temp.put("LONXITUDE", mForm.getListaCampoInformeLonxitude()[i]);
				temp.put("NOMEAS", mForm.getListaCampoInformeNomeas()[i]);
				salida.add(temp);
			}

		}

		return salida;
	}

	private java.util.Vector creaAplicaciones(
		MantenimientosInformesForm mForm) {
		java.util.Vector salida = new java.util.Vector();
		es.altia.util.HashtableWithNull temp =
			new es.altia.util.HashtableWithNull();
		if ((mForm.getListaAplAplcod() != null)
			&& (mForm.getListaAplAplcod().length > 0)) {
			for (int i = 0; i < mForm.getListaAplAplcod().length; i++) {
				temp = new es.altia.util.HashtableWithNull();
				temp.put("APL_COD", mForm.getListaAplAplcod()[i]);
				salida.add(temp);
			}
		}

		return salida;
	}

	private es
		.altia
		.agora
		.business
		.geninformes
		.utils
		.XeracionInformes
		.EstructuraEntidades creaEstructuraXeradorInformes(
			es.altia.util.HashtableWithNull tabla)
		throws Exception {
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.XeracionInformes
			.EstructuraEntidades salida =
			new es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.XeracionInformes
				.EstructuraEntidades(
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
		tabla2.put(
			"COD_ENTIDADEINFORME",
			(String) tabla.get("COD_ENTIDADEINFORME"));

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

			tabla2.put("CAMPOS", cs);
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

	private java.util.Vector creaCamposSeleccion(
		es
			.altia
			.agora
			.interfaces
			.user
			.web
			.informes
			.MantenimientosInformesForm mForm,
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
		es
			.altia
			.agora
			.interfaces
			.user
			.web
			.informes
			.MantenimientosInformesForm mForm,
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
		es
			.altia
			.agora
			.interfaces
			.user
			.web
			.informes
			.MantenimientosInformesForm mForm,
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

}