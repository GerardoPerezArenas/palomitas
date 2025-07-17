package es.altia.agora.business.geninformes;

import es.altia.agora.business.geninformes.utils.UtilidadesXerador;
import es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades;
import es.altia.agora.business.geninformes.utils.XeracionInformes.NodoEntidad;

import es.altia.agora.business.geninformes.utils.valRegla.ValRegXerador;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.util.FormateadorTercero;


import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


import es.altia.util.conexion.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.CharArrayWriter;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
// imports para crear el XML
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public final class GeneradorInformesMgr {
	private static int CONSULTA = 0;
	private static int MODIFICACION = 1;

	//	Para el fichero de configuracion technical.
	protected static Config m_ConfigTechnical;

	// Para el fichero de mensajes de error localizados.
	protected static Config m_ConfigError;
	protected static Log m_Log =
            LogFactory.getLog(GeneradorInformesMgr.class.getName());

    /**
	 *
	 * Variables referentes a permisos sobre pantallas (para el método ValidaPermisoPantalla)
	 *
	 */
	private Connection con;
	private static String[] _params;

	/****************************************************************************/

	/*      METODOS DEL EJB                                                     */

	/****************************************************************************/
	/****************************************************************************/
	public GeneradorInformesMgr(String[] params) {
		_params = params;

		//Queremos usar el fichero de configuración technical
		m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");

		//Queremos tener acceso a los mensajes de error localizados
		m_ConfigError = ConfigServiceHelper.getConfig("error");
	}

	/* **************************************************************************/
	/*      METODOS PUBLICOS                                                    */
	/* **************************************************************************/
	public es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades 
	  ConstruyeEstructuraEntidadesInforme(es.altia.util.HashtableWithNull entrada) throws Exception {
		
		es.altia.util.conexion.Cursor cursorSalida = null;
		es.altia.agora.business.geninformes.utils.bd.EstructuraXerador ex =	null;
		es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades ee =null;
		es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades tempEe =			null;

		es.altia.util.conexion.Cursor cursorFormato =	new es.altia.util.conexion.Cursor();
		es.altia.util.conexion.Cursor cursorOrdeInforme =	new es.altia.util.conexion.Cursor();
		es.altia.util.conexion.Cursor cursorSeleccionInforme = new es.altia.util.conexion.Cursor();
		es.altia.util.conexion.Cursor cursorCondicionInforme = new es.altia.util.conexion.Cursor();

		try {
			AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

			//ValidaPermisoPantalla(hdlSesion, MODIFICACION);
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			if (this.con == null) {
				con = adapBD.getConnection();
			}

			es.altia.agora.business.geninformes.utils.bd.CampoCondicionInforme 
			  campoCondicionInforme =	new es.altia.agora.business.geninformes.utils.bd.CampoCondicionInforme(this.con);
			es.altia.agora.business.geninformes.utils.bd.CampoSeleccionInforme 
			  campoSeleccionInforme =	new es.altia.agora.business.geninformes.utils.bd.CampoSeleccionInforme(this.con);
			es.altia.agora.business.geninformes.utils.bd.CampoOrdeInforme 
			  campoOrdeInforme = new es.altia.agora.business.geninformes.utils.bd.CampoOrdeInforme(this.con);

			ex = new es.altia.agora.business.geninformes.utils.bd.EstructuraXerador(con);

			cursorSalida = ex.ConsultaEstructuraEntidadesInforme(entrada);

			String codEstructura;
			String codEntidade;

			if (cursorSalida.next()) {
				codEstructura = cursorSalida.getString("COD_ESTRUCTURA");
				codEntidade = cursorSalida.getString("COD_ENTIDADEINFORME");
				ee = new es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades(codEstructura, codEntidade);

				ee.setNomeEntidade(cursorSalida.getString("NOME"));

				ee.setClausulaFrom(cursorSalida.getString("CLAUSULAFROM"));
				ee.setClausulaWhere(cursorSalida.getString("CLAUSULAWHERE"));
				ee.setNomeVista(cursorSalida.getString("NOMEVISTA"));

				es.altia.util.HashtableWithNull temp = new es.altia.util.HashtableWithNull();

				// Campos
				temp.put("COD_ESTRUCTURA", codEstructura);

				// CAmpos seleccion
				es.altia.agora.business.geninformes.utils.XeracionInformes.CampoSeleccionInforme cs = null;

				cursorSeleccionInforme = campoSeleccionInforme.ConsultaCampoSeleccionInforme(temp);

				while (cursorSeleccionInforme.next()) {
					cs = new es.altia.agora.business.geninformes.utils.XeracionInformes.CampoSeleccionInforme();
					cs.setCampo(cursorSeleccionInforme.getString("CAMPO"));
					cs.setAncho(cursorSeleccionInforme.getString("ANCHO"));
					cs.setCodCampoInforme(cursorSeleccionInforme.getString("COD_CAMPOINFORME"));
					cs.setCodEstructura(cursorSeleccionInforme.getString(""));

					cs.setLonxitudeCampo(cursorSeleccionInforme.getString("LONXITUDE"));

					//cs.getNomeCampo(cursorSeleccionInforme.getString("NOME"));
					cs.setPosicion(cursorSeleccionInforme.getString("POSICION"));
					cs.setTipoCampo(cursorSeleccionInforme.getString("TIPO"));

					ee.addCampo(cs);
				}

				// Campos Ordenación
				es.altia.agora.business.geninformes.utils.XeracionInformes.CampoOrdeInforme co =null;
				cursorOrdeInforme =	campoOrdeInforme.ConsultaCampoOrdeInforme(temp);

				while (cursorOrdeInforme.next()) {
					co = new es.altia.agora.business.geninformes.utils.XeracionInformes.CampoOrdeInforme();
					co.setCampo(cursorOrdeInforme.getString("CAMPO"));

					//co.setAncho(cursorOrdeInforme.getString("ANCHO"));
					co.setCodCampoInforme(cursorOrdeInforme.getString("COD_CAMPOINFORME"));
					co.setCodEstructura(cursorOrdeInforme.getString(""));

					co.setLonxitudeCampo(cursorOrdeInforme.getString("LONXITUDE"));

					//co.getNomeCampo(cursorOrdeInforme.getString("NOME"));
					co.setPosicion(cursorOrdeInforme.getString("POSICION"));
					co.setTipoCampo(cursorOrdeInforme.getString("TIPO"));

					ee.addCampo(co);
				}

				// CAmpos condición
				es.altia.agora.business.geninformes.utils.XeracionInformes.CampoCondicionInforme cc = null;
				cursorCondicionInforme =campoCondicionInforme.ConsultaCampoCondicionInforme(temp);

				while (cursorCondicionInforme.next()) {
					cc = new es.altia.agora.business.geninformes.utils.XeracionInformes.CampoCondicionInforme();
					cc.setClausula(cursorCondicionInforme.getString("CLAUSULA"));
					cc.setOperador(cursorCondicionInforme.getString("OPERADOR"));
					cc.setValor(cursorCondicionInforme.getString("VALOR"));
					cc.setCampo(cursorCondicionInforme.getString("CAMPO"));

					cc.setCodCampoInforme(cursorCondicionInforme.getString("COD_CAMPOINFORME"));
					cc.setCodEstructura(cursorCondicionInforme.getString(""));

					cc.setLonxitudeCampo(cursorCondicionInforme.getString("LONXITUDE"));
					cc.setNomeCampo(cursorCondicionInforme.getString("NOME"));

					cc.setPosicion(cursorCondicionInforme.getString("POSICION"));
					cc.setTipoCampo(cursorCondicionInforme.getString("TIPO"));
					cc.setSelectValores(cursorCondicionInforme.getString("SELECTVALORES"));
					cc.setFromValores(cursorCondicionInforme.getString("FROMVALORES"));
					cc.setWhereValores(cursorCondicionInforme.getString("WHEREVALORES"));
					ee.addCampo(cc);
				}

				//
				temp.put("COD_ESTRUCTURA", codEstructura);
				cursorSalida = ex.ConsultaHijosEstructuraInforme(temp);

				while (cursorSalida.next()) {
					temp.put("COD_ESTRUCTURA",cursorSalida.getString("COD_ESTRUCTURA"));
					ee.addHijo(ConstruyeEstructuraEntidadesInforme(temp));
				}
			}
		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade exc) {
            exc.printStackTrace();
            throw exc;
		} catch (Exception e) {
            e.printStackTrace();
            throw new es.altia.agora.business.geninformes.utils.ExceptionXade(
				999,
				e,
				(Object) "");
		}

		return ee;
	}

	/**
	 * Method ConsultarInformesAplicacion.
	 * @param entrada
	 * @return Cursor
	 * @throws es.altia.agora.business.geninformes.utils.ExceptionXade
	 */
	public es.altia.util.conexion.Cursor ConsultarInformesAplicacion(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		es.altia.util.conexion.Cursor cursorSalida =
			new es.altia.util.conexion.Cursor();
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);
		try {

			//ValidaPermisoPantalla(hdlSesion, MODIFICACION);
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			con = adapBD.getConnection();

			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.InformeXerador informeXerador =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.InformeXerador(
					this.con);

			//ValidaPermisoPantalla(hdlSesion, CONSULTA);
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			cursorSalida =
				informeXerador.ConsultaInformesXeradorAplicacionAdaptada(
					entrada);
		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(
				999,
				e,
				(Object) "");
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(bde.getMessage());
            }
		}

		return cursorSalida;
	}

	/**
	 * Method ConsultarInforme.
	 * @param entrada
	 * @return Cursor
	 * @throws es.altia.agora.business.geninformes.utils.ExceptionXade
	 */
	public es.altia.util.conexion.Cursor ConsultarInforme(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		String formato = null;

		es.altia.util.conexion.Cursor cursorTemp = null;
		es.altia.util.conexion.Cursor cursorFormato =
			new es.altia.util.conexion.Cursor();
		es.altia.util.conexion.Cursor cursorOrdeInforme =
			new es.altia.util.conexion.Cursor();
		es.altia.util.conexion.Cursor cursorSeleccionInforme =
			new es.altia.util.conexion.Cursor();
		es.altia.util.conexion.Cursor cursorCondicionInforme =
			new es.altia.util.conexion.Cursor();

		es.altia.util.conexion.Cursor cursorSalida = null;

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			//ValidaPermisoPantalla(hdlSesion, CONSULTA);
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			//ValidaPermisoPantalla(hdlSesion, MODIFICACION);
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			con = adapBD.getConnection();

			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.InformeXerador informeXerador =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.InformeXerador(
					this.con);
			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.EtiquetaXerador etiquetaXerador =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.EtiquetaXerador(
					this.con);
			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.ListadoXerador listadoXerador =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.ListadoXerador(
					this.con);
			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.CampoCondicionInforme campoCondicionInforme =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.CampoCondicionInforme(
					this.con);
			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.CampoSeleccionInforme campoSeleccionInforme =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.CampoSeleccionInforme(
					this.con);
			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.CampoOrdeInforme campoOrdeInforme =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.CampoOrdeInforme(
					this.con);
			if(m_Log.isDebugEnabled()) m_Log.debug("entrada antes de consinforme es:" + entrada + ".");
			cursorTemp = informeXerador.ConsultaInformeXerador(entrada);
			cursorTemp.next();

			String codEstructura = cursorTemp.getString("COD_ESTRUCTURA");
			if(m_Log.isDebugEnabled()) m_Log.debug("Despues de consinf:CodEstructura:" + codEstructura + ".");

			formato = cursorTemp.getString("FORMATO");

			cursorTemp = informeXerador.ConsultaInformeXeradorAdaptada(entrada);

			if (cursorTemp.next()) {
				entrada.put("COD_ESTRUCTURA", codEstructura);

				cursorOrdeInforme =	campoOrdeInforme.ConsultaCampoOrdeInforme(entrada);
				cursorSeleccionInforme =campoSeleccionInforme.ConsultaCampoSeleccionInforme(entrada);
				cursorCondicionInforme =campoCondicionInforme.ConsultaCampoCondicionInforme(entrada);
				cursorTemp.anadirColumna("CAMPOSCONDICION");

				if (!cursorCondicionInforme.esVacio()) {
					cursorTemp.anadiraTupla(cursorCondicionInforme);
				} else {
					es.altia.util.conexion.Cursor tempCursor =
						new es.altia.util.conexion.Cursor(0,new java.util.Vector());

					cursorTemp.anadiraTupla(tempCursor);
				}

				cursorTemp.anadirColumna("CAMPOSSELECCION");
				cursorTemp.anadiraTupla(cursorSeleccionInforme);
				cursorTemp.anadirColumna("CAMPOSORDE");

				if (!cursorOrdeInforme.esVacio()) {
					cursorTemp.anadiraTupla(cursorOrdeInforme);
				} else {
					cursorTemp.anadiraTupla(cursorOrdeInforme);

					//es.altia.util.Debug.println("Cursor CamposOrde é baleiro");
				}

				if (formato
					.equals(
						es
							.altia
							.agora
							.business
							.geninformes
							.utils
							.ConstantesXerador
							.CODIGO_FORMATO_LISTADO)
					|| formato.equals(
						es
							.altia
							.agora
							.business
							.geninformes
							.utils
							.ConstantesXerador
							.CODIGO_FORMATO_ESTATISTICA)) {
					cursorFormato =
						listadoXerador.ConsultaListadoXerador(entrada);
					cursorTemp.anadirColumna("LISTADOXERADOR");
				} else if (
					formato.equals(
						es
							.altia
							.agora
							.business
							.geninformes
							.utils
							.ConstantesXerador
							.CODIGO_FORMATO_ETIQUETA)) {
					cursorFormato =
						etiquetaXerador.ConsultaEtiquetaXerador(entrada);
					cursorTemp.anadirColumna("ETIQUETAXERADOR");
				}

				cursorTemp.anadiraTupla(cursorFormato);
				cursorTemp.Indice = -1;
			}

			cursorSalida = cursorTemp;
		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(
				999,
				e,
				(Object) "");
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}

		return cursorSalida;
	}

	/**
	 * Method EliminarInforme.
	 * @param entrada
	 * @throws es.altia.agora.business.geninformes.utils.ExceptionXade
	 */
	public void EliminarInforme(es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		if (entrada == null) {
			entrada = new es.altia.util.HashtableWithNull();
		}

		//String[] _params = (String[]) entrada.get("PARAMS");
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			//ValidaPermisoPantalla(hdlSesion, MODIFICACION);
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			con = adapBD.getConnection();
			adapBD.inicioTransaccion(con);
			RealizaEliminacionInforme(entrada);
			adapBD.finTransaccion(con);

			//
			// Ahora debemos eliminar el report y su query del catálogo.
			//
		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception te) {
			try {
				adapBD.rollBack(con);
			} catch (BDException te2) {
                te2.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te2.getMessage());
			} finally {
				throw new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ExceptionXade(
					999,
					te,
					(Object) "");
			}
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}
	}

	/**
	 * Method EngadirInforme.
	 * @param entrada
	 * @return Cursor
	 * @throws es.altia.agora.business.geninformes.utils.ExceptionXade
	 */
	public es.altia.util.conexion.Cursor EngadirInforme(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		es.altia.util.conexion.Cursor salida = null;

		if (entrada == null) {
			entrada = new es.altia.util.HashtableWithNull();
		}
        entrada.put("PARAMS", _params);
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			con = adapBD.getConnection();

			String nome = entrada.get("NOME").toString();

			//ValidaInforme(entrada);

			adapBD.inicioTransaccion(con);

			salida = RealizaAltaInforme(entrada,"no");

			adapBD.finTransaccion(con);

		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			try {
				adapBD.rollBack(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			} finally {
				throw new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ExceptionXade(
					999,
					e,
					(Object) "");
			}
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}

		return salida;
	}

	public void EngadirPlantillaInforme(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		es.altia.util.conexion.Cursor salida = null;

		if (entrada == null) {
			entrada = new es.altia.util.HashtableWithNull();
		}

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			con = adapBD.getConnection();

			//ValidaInforme(entrada);

			adapBD.inicioTransaccion(con);

			RealizaAltaPlantillaInforme(entrada);

			adapBD.finTransaccion(con);

		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			try {
				adapBD.rollBack(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			} finally {
				throw new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ExceptionXade(
					999,
					e,
					(Object) "");
			}
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}

	}

	/**
	 * Method ModificarInforme.
	 * @param entrada
	 * @throws es.altia.agora.business.geninformes.utils.ExceptionXade
	 */
	public void ModificarInforme(es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		if (entrada == null) {
			entrada = new es.altia.util.HashtableWithNull();
		}
        entrada.put("PARAMS", _params);
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			con = adapBD.getConnection();

			adapBD.inicioTransaccion(con);

			RealizaModificacionInforme(entrada);

			adapBD.finTransaccion(con);

		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			try {
				adapBD.rollBack(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			} finally {
				throw new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ExceptionXade(
					999,
					e,
					(Object) "");
			}
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}
	}

	public es.altia.util.HashtableWithNull ejecutarInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es.altia.util.HashtableWithNull salida =
			new es.altia.util.HashtableWithNull();

		String xmlSaida = null;

		if (entrada == null) {
			entrada = new es.altia.util.HashtableWithNull();
		}

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);
		es.altia.util.conexion.Cursor cursorInforme = null;
		es.altia.util.conexion.Cursor cursorEstructura = null;
		byte[] ficheroPlantilla = null;
		String codEstructuraRaiz = null;

		try {
			con = adapBD.getConnection();

			es.altia.agora.business.geninformes.utils.bd.InformeXerador ix =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.InformeXerador(
					con);

			// 1.- Recuperar datos de informe
			cursorInforme = ix.ConsultaPlantillaWordInformeXerador(entrada);

			if(m_Log.isDebugEnabled()) m_Log.debug("Entes de mirar cursorInforme:"
					                        + cursorInforme.numTuplas()
					                        + ".");
			if (cursorInforme.next()) {
				ficheroPlantilla = (byte[]) cursorInforme.getObject("F_DOT");
				if (m_Log.isDebugEnabled()) m_Log.debug(
					"Fichero plantilla es:" + ficheroPlantilla + ".");

				codEstructuraRaiz = cursorInforme.getString("COD_ESTRUCTURA");
			}

			// 2.- Recuperar toda la EstructuraEntidades

			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.XeracionInformes
				.EstructuraEntidades eeRaiz =
				null;

			eeRaiz =
				UtilidadesXerador.construyeEstructuraEntidadesInforme(
					_params,
					(String) entrada.get("COD_INFORMEXERADOR"),
					codEstructuraRaiz);

			if(m_Log.isDebugEnabled()) m_Log.debug("eeRaiz:" + eeRaiz + ".");

			// 3.- Ejecutar las consultas y construir la estructura de instancias
			//     Se establecen los valores de los parámetros para las ? de la consulta

			eeRaiz.setValoresParametrosConsulta(new java.util.Vector());

			UtilidadesXerador.construyeEstructuraDatos(_params, eeRaiz,null);
			if(m_Log.isDebugEnabled()) m_Log.debug("Despues de ejecutar las consultas:"
					                        + eeRaiz
					                        + ".eeRaiz.getConsultaEjecutada:"
					                        + eeRaiz.getCursorConsulta().hash
					                        + ".");

			// 4.- Generar el XML
			xmlSaida = generaXMLResultado(eeRaiz);

			//m_Log.debug("O XML DE saida:" + xmlSaida + ".");

			// 5.- Devolver el XML
			//} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			//	throw ex;
		} catch (Exception e) {
			try {
				adapBD.rollBack(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			} finally {
				throw new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ExceptionXade(
					999,
					e,
					(Object) "");
			}
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}

		salida.put("XML", xmlSaida);
		salida.put("F_DOT", ficheroPlantilla);
		return salida;
	}

	/**
	 * Method EliminarEtiquetaCentro.
	 * @param entrada
	 * @throws es.altia.agora.business.geninformes.utils.ExceptionXade
	 */
	public void EliminarEtiquetaCentro(es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		if (entrada == null) {
			entrada = new es.altia.util.HashtableWithNull();
		}

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			//ValidaPermisoPantalla(hdlSesion, MODIFICACION);
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			//String cencod = getSesion(hdlSesion).getCOD_CENTRO();
			con = adapBD.getConnection();

			String codEtiqueta = entrada.get("CODIGO").toString();

			ValRegXerador.ValidaNonExisteInformeVinculadoEtiqueta(
				codEtiqueta,
				"",
				this.con);

			adapBD.inicioTransaccion(con);
			RealizaEliminacionEtiquetaCentro(entrada);
			adapBD.finTransaccion(con);

			//
			// Ahora debemos eliminar el report y su query del catálogo.
			//
		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			try {
				adapBD.rollBack(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			} finally {
				throw new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ExceptionXade(
					999,
					e,
					(Object) "");
			}
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}
	}

	/**
	 * Method EngadirEtiquetaCentro.
	 * @param entrada
	 * @return Cursor
	 * @throws es.altia.agora.business.geninformes.utils.ExceptionXade
	 */
	public es.altia.util.conexion.Cursor EngadirEtiquetaCentro(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		es.altia.util.conexion.Cursor salida = null;

		if (entrada == null) {
			entrada = new es.altia.util.HashtableWithNull();
		}
        entrada.put("PARAMS", _params);
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			//ValidaPermisoPantalla(hdlSesion, MODIFICACION);
			//ValidaInforme(entrada);
			con = adapBD.getConnection();
			adapBD.inicioTransaccion(con);

			salida = RealizaAltaEtiquetaCentro(entrada);

			adapBD.finTransaccion(con);

			//
			// Ahora debemos crear la query, el report y meterlos en el catálogo.
			//
		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			try {
				adapBD.rollBack(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			} finally {
				throw new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ExceptionXade(
					999,
					e,
					(Object) "");
			}
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}

		return salida;
	}

	/**
	 * Method ModificarEtiquetaCentro.
	 * @param entrada
	 * @throws es.altia.agora.business.geninformes.utils.ExceptionXade
	 */
	public void ModificarEtiquetaCentro(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		if (entrada == null) {
			entrada = new es.altia.util.HashtableWithNull();
		}

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			//ValidaPermisoPantalla(hdlSesion, MODIFICACION);
			//ValidaInforme(entrada);
			con = adapBD.getConnection();
			adapBD.inicioTransaccion(con);

			RealizaModificacionEtiquetaCentro(entrada);

			adapBD.finTransaccion(con);

			//
			// Se borra el informe y la query del mismo
			//
			//
			// Se crea de nuevo la query y el informe
			//
		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			try {
				adapBD.rollBack(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			} finally {
				throw new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ExceptionXade(
					999,
					e,
					(Object) "");
			}
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}
	}

	private void RealizaAltaPlantillaInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.InformeXerador informeXerador =
			new es.altia.agora.business.geninformes.utils.bd.InformeXerador(
				this.con);

		informeXerador.ModificaPlantillaInforme(entrada);

	}

	/**
	 * Method RealizaAltaInforme.
	 * @param entrada
	 * @return Cursor
	 * @throws Exception
	 */
	private es.altia.util.conexion.Cursor RealizaAltaInforme(es.altia.util.HashtableWithNull entrada,String modificar)
		throws Exception {
		Long salida = null;

		es.altia.util.conexion.Cursor cursorEntidade = null;

		es.altia.agora.business.geninformes.utils.bd.InformeXerador 
		  informeXerador =new es.altia.agora.business.geninformes.utils.bd.InformeXerador(this.con);
		es.altia.agora.business.geninformes.utils.bd.EtiquetaXerador 
		  etiquetaXerador = new es.altia.agora.business.geninformes.utils.bd.EtiquetaXerador(this.con);
		es.altia.agora.business.geninformes.utils.bd.ListadoXerador 
		  listadoXerador = new es.altia.agora.business.geninformes.utils.bd.ListadoXerador(this.con);
		es.altia.agora.business.geninformes.utils.bd.CampoCondicionInforme 
		  campoCondicionInforme =	new es.altia.agora.business.geninformes.utils.bd.CampoCondicionInforme(this.con);
		es.altia.agora.business.geninformes.utils.bd.CampoSeleccionInforme 
		  campoSeleccionInforme = new es.altia.agora.business.geninformes.utils.bd.CampoSeleccionInforme(this.con);
		es.altia.agora.business.geninformes.utils.bd.CampoOrdeInforme 
		  campoOrdeInforme =new es.altia.agora.business.geninformes.utils.bd.CampoOrdeInforme(this.con);

		es.altia.agora.business.geninformes.utils.bd.EstructuraXerador 
		  ex =new es.altia.agora.business.geninformes.utils.bd.EstructuraXerador(this.con);

		es.altia.agora.business.geninformes.utils.bd.EtiqPlt 
		  etiqPlt = new es.altia.agora.business.geninformes.utils.bd.EtiqPlt(this.con);
		es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades ee =	null;

		//
		// Al grabar la estructura vamos grabando también las etiquetas disponibles
		//
		String prefijo = "";
		java.util.HashMap etiquetas = new java.util.HashMap();

		//String codEstructuraRaiz = null;
		//
		// 1.- Graba informe y estructura en la BBDD
		//
		//
		// Alta de nodo raíz de la estructura de entidades
		//
		es.altia.util.HashtableWithNull tempEx =new es.altia.util.HashtableWithNull();
		es.altia.util.HashtableWithNull tempCampo =new es.altia.util.HashtableWithNull();
		ee =(es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades) entrada.get("ESTRUCTURAINFORME");

		Integer posicion = new Integer(0);

		String querySQL =	es.altia.agora.business.geninformes.utils.UtilidadesXerador.creaQuerySQLEE(ee);

		tempEx.put("CONSULTASQL", querySQL);
		tempEx.put("COD_PAI", null);
		tempEx.put("COD_ENTIDADEINFORME", entrada.get("COD_ENTIDADEINFORME"));
		tempEx.put("POSICION", posicion);
        tempEx.put("PARAMS", _params);
		Long codEstructuraRaiz = ex.AltaEstructuraXerador(tempEx);
		tempEx.clear();

		//
		// Consulto datos da entidade
		//		
		tempEx.put("PARAMS", _params);
		tempEx.put("COD_ENTIDADEINFORME", entrada.get("COD_ENTIDADEINFORME"));
		cursorEntidade = es.altia.agora.business.geninformes.utils.bd.UtilesXerador.ConsultaDatosEntidadeInforme(tempEx);
		tempEx.clear();
		cursorEntidade.next();
		ee.setNomeEntidade(cursorEntidade.getString("NOME"));

		if(m_Log.isDebugEnabled()) m_Log.debug("MEtí nome de entidade:" + ee.getNomeEntidade() + ".");

		entrada.put("COD_ESTRUCTURA", codEstructuraRaiz);

		java.util.Iterator iterHijosNivel = null;
		es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades hijo =	null;

		es.altia.agora.business.geninformes.utils.XeracionInformes.CampoSeleccionInforme itemSeleccion =null;

		es.altia.agora.business.geninformes.utils.XeracionInformes.CampoCondicionInforme itemCondicion =null;

		es.altia.agora.business.geninformes.utils.XeracionInformes.CampoOrdeInforme itemOrde =null;
		//
		//
		//
		//Campos de selección
		java.util.Iterator iterCond = null;
		tempCampo.clear();
		java.util.Vector vectorEtiquetas = null;

		if (ee.getCamposSeleccion() != null) {iterCond = ee.getCamposSeleccion().iterator();

			while (iterCond.hasNext()) {
				itemSeleccion =(es.altia.agora.business.geninformes.utils.XeracionInformes.CampoSeleccionInforme) iterCond.next();

				tempCampo.put("COD_ESTRUCTURA", codEstructuraRaiz);
				tempCampo.put("COD_CAMPOINFORME",itemSeleccion.getCodCampoInforme());
				tempCampo.put("POSICION", itemSeleccion.getPosicion());
				tempCampo.put("ANCHO", itemSeleccion.getAncho());
				if(m_Log.isDebugEnabled()) m_Log.debug("Le paso a altacamposel=" + tempCampo + ".");
				campoSeleccionInforme.AltaCampoSeleccionInforme(tempCampo);

				//
				// Para obtener etiqeutas
				//
				prefijo = ee.getPrefijo();
				vectorEtiquetas = (java.util.Vector) etiquetas.get(prefijo);
				if (vectorEtiquetas == null) {
					vectorEtiquetas = new java.util.Vector();
					vectorEtiquetas.add(itemSeleccion.getCodCampoInforme());
					etiquetas.put(prefijo, vectorEtiquetas);
				} else
					vectorEtiquetas.add(itemSeleccion.getCodCampoInforme());

				//
				// FIN: Para obtener etiqeutas
				//

			}
		}

		//	Campos de condición
		tempCampo.clear();

		if (ee.getCamposCondicion() != null) {
			iterCond = ee.getCamposCondicion().iterator();

			while (iterCond.hasNext()) {
				itemCondicion =
					(es.altia.agora.business.geninformes.utils.XeracionInformes.CampoCondicionInforme) iterCond.next();

				tempCampo.put("COD_ESTRUCTURA", codEstructuraRaiz);
				tempCampo.put("COD_CAMPOINFORME",itemCondicion.getCodCampoInforme());
				tempCampo.put("POSICION", itemCondicion.getPosicion());
				tempCampo.put("CLAUSULA", itemCondicion.getClausula());
				tempCampo.put("OPERADOR", itemCondicion.getOperador());
				tempCampo.put("VALOR", itemCondicion.getValor());

				campoCondicionInforme.AltaCampoCondicionInforme(tempCampo);
			}
		}

		//	Campos de Ordenación
		tempCampo.clear();

		if (ee.getCamposOrde() != null) {
			iterCond = ee.getCamposOrde().iterator();

			while (iterCond.hasNext()) {
				itemOrde =(es.altia.agora.business.geninformes.utils.XeracionInformes.CampoOrdeInforme) iterCond.next();

				tempCampo.put("COD_ESTRUCTURA", codEstructuraRaiz);
				tempCampo.put("COD_CAMPOINFORME",itemOrde.getCodCampoInforme());
				tempCampo.put("POSICION", itemOrde.getPosicion());
				tempCampo.put("TIPOORDE", itemOrde.getTipoOrde());

				campoOrdeInforme.AltaCampoOrdeInforme(tempCampo);
			}
		}

		//
		//
		//
		java.util.Vector nuevosHijosNivel = null;
		Long salidaEstructura = null;

		java.util.Vector hijosNivel = ee.getHijos();

		// Primer bucle recorre cada nivel
		while ((hijosNivel != null) && (hijosNivel.size() > 0)) {
			iterHijosNivel = hijosNivel.iterator();
			nuevosHijosNivel = new java.util.Vector();

			// Bucle recorre hijos de un nivel
			while (iterHijosNivel.hasNext()) {
				hijo =(es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades) iterHijosNivel.next();

				tempEx.put("COD_ENTIDADEINFORME", hijo.getCodEntidadeInforme());
				tempEx.put("COD_PAI", hijo.getCodPai());
				tempEx.put("POSICION", hijo.getPosicion());
				querySQL =es.altia.agora.business.geninformes.utils.UtilidadesXerador.creaQuerySQLEE(hijo);

				tempEx.put("CONSULTASQL", querySQL);
                tempEx.put("PARAMS", _params);
				salidaEstructura = ex.AltaEstructuraXerador(tempEx);

				//
				// Consulto datos da entidade
				//		
				tempEx.put("PARAMS", _params);
				tempEx.put("COD_ENTIDADEINFORME", hijo.getCodEntidadeInforme());
				cursorEntidade =
					es.altia.agora.business.geninformes.utils.bd.UtilesXerador.ConsultaDatosEntidadeInforme(tempEx);
				tempEx.clear();
				cursorEntidade.next();
				hijo.setNomeEntidade(cursorEntidade.getString("NOME"));
				if(m_Log.isDebugEnabled()) m_Log.debug("MEtí nome de entidade:" + hijo.getNomeEntidade() + ".");
				java.util.Iterator iterTemp = null;

				if (hijo.getHijos() != null) {
					iterTemp = hijo.getHijos().iterator();

					int posicionHijo = 0;
					es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades eeTemp =null;

					while (iterTemp.hasNext()) {
						eeTemp =(es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades) iterTemp.next();
						eeTemp.setCodPai(salidaEstructura.toString());
						eeTemp.setPai(hijo);
						eeTemp.setPosicion(posicionHijo + "");
						posicionHijo++;
					}

					nuevosHijosNivel.addAll(hijo.getHijos());
				}

				tempEx.put("COD_ESTRUCTURA", salidaEstructura);

				// Campos de selección
				tempCampo.clear();

				if (hijo.getCamposSeleccion() != null) {
					iterCond = hijo.getCamposSeleccion().iterator();

					while (iterCond.hasNext()) {
						itemSeleccion =(es.altia.agora.business.geninformes.utils.XeracionInformes.CampoSeleccionInforme) iterCond.next();

						tempCampo.put("COD_ESTRUCTURA", salidaEstructura);
						tempCampo.put("COD_CAMPOINFORME",itemSeleccion.getCodCampoInforme());
						tempCampo.put("POSICION", itemSeleccion.getPosicion());
						tempCampo.put("ANCHO", itemSeleccion.getAncho());

						campoSeleccionInforme.AltaCampoSeleccionInforme(tempCampo);

						//
						// PAra obtener etiquetas
						// 
						prefijo = hijo.getPrefijo();
						vectorEtiquetas =(java.util.Vector) etiquetas.get(prefijo);
						if (vectorEtiquetas == null) {
							vectorEtiquetas = new java.util.Vector();
							vectorEtiquetas.add(itemSeleccion.getCodCampoInforme());
							etiquetas.put(prefijo, vectorEtiquetas);
						} else
							vectorEtiquetas.add(
								itemSeleccion.getCodCampoInforme());
						//
						//			FIN: PAra obtener etiquetas
						// 

					}
				}

				//	Campos de condición
				tempCampo.clear();

				if (hijo.getCamposCondicion() != null) {
					iterCond = hijo.getCamposCondicion().iterator();

					while (iterCond.hasNext()) {
						itemCondicion =(es.altia.agora.business.geninformes.utils.XeracionInformes.CampoCondicionInforme) iterCond.next();

						tempCampo.put("COD_ESTRUCTURA", salidaEstructura);
						tempCampo.put("COD_CAMPOINFORME",itemCondicion.getCodCampoInforme());
						tempCampo.put("POSICION", itemCondicion.getPosicion());
						tempCampo.put("CLAUSULA", itemCondicion.getClausula());
						tempCampo.put("OPERADOR", itemCondicion.getOperador());
						tempCampo.put("VALOR", itemCondicion.getValor());

						campoCondicionInforme.AltaCampoCondicionInforme(tempCampo);
					}
				}

				//				Campos de Ordenación
				tempCampo.clear();

				if (hijo.getCamposOrde() != null) {
					iterCond = hijo.getCamposOrde().iterator();

					while (iterCond.hasNext()) {
						itemOrde =(es.altia.agora.business.geninformes.utils.XeracionInformes.CampoOrdeInforme) 
						          iterCond.next();

						tempCampo.put("COD_ESTRUCTURA", salidaEstructura);
						tempCampo.put("COD_CAMPOINFORME",itemOrde.getCodCampoInforme());
						tempCampo.put("POSICION", itemOrde.getPosicion());
						tempCampo.put("TIPOORDE", itemOrde.getTipoOrde());

						campoOrdeInforme.AltaCampoOrdeInforme(tempCampo);
					}
				}
			}

			hijosNivel = nuevosHijosNivel;
		}

		entrada.put("COD_ESTRUCTURA", codEstructuraRaiz);

		//
		// Generamos  las vistas 
		//
		//String dtdContido=es.altia.agora.business.geninformes.utils.UtilidadesXerador.generaDTD(es.altia.agora.business.geninformes.utils.UtilidadesXerador.generaArbolCompleto(ee));
		//entrada.put("DTD_CONTIDO",dtdContido);
		entrada.put("DTD_CONTIDO", null);

		//
		//		2.- Generar plantilla .dot
		//
		// 
		// Enviar fichero de la plantilla como un array de bytes
		//

		//byte[] aa =(byte [])entrada.get("F_DOT");
		//entrada.put("F_DOT", aa);

		//
		// Alta de Informe
		// 

		if ((entrada.get("COD_INFORMEXERADOR") == null)
			|| (entrada.get("COD_INFORMEXERADOR").toString().trim().equals(""))) {

			salida = informeXerador.AltaInformeXerador(entrada);
			if(m_Log.isDebugEnabled()) m_Log.debug("ENtré por alt y salgo con salida:" + salida + ".");
		} else {
			salida = new Long(entrada.get("COD_INFORMEXERADOR").toString());
			informeXerador.ModificaInformeXerador(entrada);
			if(m_Log.isDebugEnabled()) m_Log.debug("ENtré por modif y salgo con salida:" + salida + ".");
		}
		
		// MODIFICACION DEL 4/8/2004 PORQUE AL MODIFICAR UN INFORME NO DABA DE ALTA
		// NUEVOS CAMPOS
		if(!"no".equals(modificar)) {
			tempEx.clear();
			tempEx.put("COD_INFORMEXERADOR", salida + "");
			etiqPlt.BorraEtiqPlt(tempEx);	
		}
		//if(modificar.equals("no")) {

			//
			// DOy de alta las etiquetas
			//
			java.util.Iterator iterCI = null;
			String codCI = null;
	
			//m_Log.debug("Etiquetas es:"+etiquetas.toString()+".");
	
			if ((etiquetas != null) && (etiquetas.size() > 0)) {
				java.util.Iterator iterEt = etiquetas.keySet().iterator();
				while (iterEt.hasNext()) {
					tempEx.clear();
					prefijo = iterEt.next().toString();
	
					tempEx.put("COD_INFORMEXERADOR", salida + "");
					tempEx.put("PREFIJO", prefijo);
					if(m_Log.isDebugEnabled()) m_Log.debug("TempEx" + tempEx + ".");
	
					vectorEtiquetas = (java.util.Vector) etiquetas.get(prefijo);
					if (vectorEtiquetas != null) {
						iterCI = vectorEtiquetas.iterator();
						while (iterCI.hasNext()) {
							codCI = iterCI.next().toString();
							tempEx.put("COD_CAMPOINFORME", codCI);
							etiqPlt.AltaEtiqPlt(tempEx);
						}
					}
	
				}
	
			}
			//
			// FIN: DOy de alta las etiquetas
			//
	  //}

		java.util.Vector tempDatos = new java.util.Vector();
		java.util.Vector tempNomes = new java.util.Vector();
		java.util.Vector temp2 = new java.util.Vector();
		temp2.add(salida);
		tempDatos.add(temp2);
		tempNomes.add("COD_INFORMEXERADOR");

		es.altia.util.conexion.Cursor cursorSalida =
			new es.altia.util.conexion.Cursor(0, tempDatos, tempNomes);

		return cursorSalida;
	}

	//	String codEntidadeInforme = ee.getCodEntidadeInforme();
	//
	//	tempEx.put("COD_ENTIDADEINFORME", codEntidadeInforme);
	//	tempEx.put("COD_PAI", null);
	//	tempEx.put("POSICION", posicion);
	//
	//	Long salidaEstructura = ex.AltaEstructuraXerador(entrada);
	//
	//	entrada.put("COD_ESTRUCTURA", salidaEstructura);
	//
	//	//
	//	// Alta de Informe
	//	//
	//	salida = informeXerador.AltaInformeXerador(entrada);
	//
	//	entrada.put("COD_INFORMEXERADOR", salida);
	//
	//	//
	//	// Alta de nodos hijos de la estructura de entidades
	//	//
	//
	//	java.util.Collection condiciones =
	//		(java.util.Collection) entrada.get("CAMPOSSELECCION");
	//	java.util.Iterator iterCond = null;
	//	es.altia.util.HashtableWithNull item = null;
	//
	//	if (condiciones != null) {
	//		iterCond = condiciones.iterator();
	//		while (iterCond.hasNext()) {
	//			item = (es.altia.util.HashtableWithNull) iterCond.next();
	//			item.put("COD_ESTRUCTURA", salidaEstructura);
	//			campoSeleccionInforme.AltaCampoSeleccionInforme(item);
	//		}
	//
	//	}
	//
	//	condiciones = (java.util.Collection) entrada.get("CAMPOSORDE");
	//
	//	iterCond = null;
	//	item = null;
	//
	//	if (condiciones != null) {
	//		iterCond = condiciones.iterator();
	//		while (iterCond.hasNext()) {
	//			item = (es.altia.util.HashtableWithNull) iterCond.next();
	//			item.put("COD_ESTRUCTURA", salidaEstructura);
	//			campoOrdeInforme.AltaCampoOrdeInforme(item);
	//		}
	//
	//	}
	//
	//	condiciones = (java.util.Collection) entrada.get("CAMPOSCONDICION");
	//
	//	iterCond = null;
	//	item = null;
	//
	//	if (condiciones != null) {
	//		iterCond = condiciones.iterator();
	//		while (iterCond.hasNext()) {
	//			item = (es.altia.util.HashtableWithNull) iterCond.next();
	//			item.put("COD_ESTRUCTURA", salidaEstructura);
	//			campoCondicionInforme.AltaCampoCondicionInforme(item);
	//		}
	//
	//	}
	//
	//	if (entrada
	//		.get("FORMATO")
	//		.toString()
	//		.equals(
	//			es
	//				.altia
	//				.agora
	//				.business
	//				.geninformes
	//				.utils
	//				.ConstantesXerador
	//				.CODIGO_FORMATO_LISTADO)
	//		|| entrada.get("FORMATO").toString().equals(
	//			es
	//				.altia
	//				.agora
	//				.business
	//				.geninformes
	//				.utils
	//				.ConstantesXerador
	//				.CODIGO_FORMATO_ESTATISTICA)) {
	//		es.altia.util.HashtableWithNull entradaListado =
	//			(es.altia.util.HashtableWithNull) entrada.get("LISTADOXERADOR");
	//		entradaListado.put("COD_INFORMEXERADOR", salida);
	//		listadoXerador.AltaListadoXerador(entradaListado);
	//
	//	} else if (
	//		entrada.get("FORMATO").toString().equals(
	//			es
	//				.altia
	//				.agora
	//				.business
	//				.geninformes
	//				.utils
	//				.ConstantesXerador
	//				.CODIGO_FORMATO_ETIQUETA)) {
	//		es.altia.util.HashtableWithNull entradaListado =
	//			(es.altia.util.HashtableWithNull) entrada.get("ETIQUETAXERADOR");
	//		entradaListado.put("COD_INFORMEXERADOR", salida);
	//		etiquetaXerador.AltaEtiquetaXerador(entradaListado);
	//	}
	//
	//	java.util.Vector tempDatos = new java.util.Vector();
	//
	//	java.util.Vector tempNomes = new java.util.Vector();
	//	java.util.Vector temp2 = new java.util.Vector();
	//	temp2.add(entrada.get("COD_INFORMEXERADOR").toString());
	//	tempDatos.add(temp2);
	//	tempNomes.add("COD_INFORMEXERADOR");
	//	es.altia.util.conexion.Cursor cursorSalida =
	//		new es.altia.util.conexion.Cursor(0, tempDatos, tempNomes);
	//
	//	return cursorSalida;
	//}

	/**
	 * Method RealizaModificacionInforme.
	 * @param entrada
	 * @throws Exception
	 */
	private void RealizaModificacionInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.InformeXerador informeXerador =
			new es.altia.agora.business.geninformes.utils.bd.InformeXerador(
				this.con);

		es.altia.util.HashtableWithNull tempTabla =
			new es.altia.util.HashtableWithNull();

		es.altia.util.conexion.Cursor cursorInforme =
			informeXerador.ConsultaInformeXerador(entrada);
		if (cursorInforme.next()) {
			tempTabla.put(
				"COD_ESTRUCTURA",
				cursorInforme.getString("COD_ESTRUCTURA"));

			RealizaEliminacionEstructura(tempTabla);

			//campoCondicionInforme.BorraCampoCondicionInforme(entrada);
			//campoOrdeInforme.BorraCampoOrdeInforme(entrada);
			//campoSeleccionInforme.BorraCampoSeleccionInforme(entrada);
			//etiquetaXerador.BorraEtiquetaXerador(entrada);
			//listadoXerador.BorraListadoXerador(entrada);

			RealizaAltaInforme(entrada,"si");

		}

	}

	private void RealizaModificacionEtiquetaCentro(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es.altia.agora.business.geninformes.utils.bd.Etiqueta etiqueta =
			new es.altia.agora.business.geninformes.utils.bd.Etiqueta(this.con);
		etiqueta.ActualizaEtiqueta(entrada);
	}

	private void RealizaEliminacionInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.InformeXerador informeXerador =
			new es.altia.agora.business.geninformes.utils.bd.InformeXerador(
				this.con);

		//
		// 1.-Eliminar vistas en la BBDD
		//
		//
		// 2.-Eliminar DTD (ya se elimina en el DELETE)
		//
		//
		// 3.-Eliminar plantilla WORD (.dot) se elimina en el DELETE
		//
		//
		// 4.- ELiminar informe de BBDD
		//
		//
		// Confiamos en el ON DELETE CASCADE DEL SGBD
		//
		///		campoCondicionInforme.BorraCampoCondicionInforme(entrada);
		//		campoOrdeInforme.BorraCampoOrdeInforme(entrada);
		//		campoSeleccionInforme.BorraCampoSeleccionInforme(entrada);
		//		estructuraXerador.BorraEstructuraXerador(entrada);
		//		
		//		etiquetaXerador.BorraEtiquetaXerador(entrada);
		//		listadoXerador.BorraListadoXerador(entrada);
		// Borrar la estructura principal

		es.altia.util.HashtableWithNull tempTabla =
			new es.altia.util.HashtableWithNull();

		es.altia.util.conexion.Cursor cursorInforme =
			informeXerador.ConsultaInformeXerador(entrada);
		if (cursorInforme.next()) {
			tempTabla.put(
				"COD_ESTRUCTURA",
				cursorInforme.getString("COD_ESTRUCTURA"));
			if(m_Log.isDebugEnabled()) m_Log.debug("Antes de eliminar estructura pillé:"
					                        + cursorInforme.getString("COD_ESTRUCTURA")
					                        + ".");

			RealizaEliminacionEstructura(tempTabla);
		}
		if(m_Log.isDebugEnabled()) m_Log.debug("Después de eliminar estructura pillé.COD_INFORMEXERADOR:"
				        + entrada.get("COD_INFORMEXERADOR")
				        + ".");
		informeXerador.BorraInformeXerador(entrada);
		//m_Log.info("Después de eliminar informe pillé.");
	}

	private void RealizaEliminacionEtiquetaCentro(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es.altia.agora.business.geninformes.utils.bd.Etiqueta etiqueta =
			new es.altia.agora.business.geninformes.utils.bd.Etiqueta(this.con);

		etiqueta.BorraEtiqueta(entrada);
	}

	private es.altia.util.conexion.Cursor RealizaAltaEtiquetaCentro(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		Long salida = null;

		es.altia.agora.business.geninformes.utils.bd.Etiqueta etiqueta =
			new es.altia.agora.business.geninformes.utils.bd.Etiqueta(this.con);

		salida = etiqueta.AltaEtiqueta(entrada);

		entrada.put("CODIGO", salida);

		java.util.Vector tempDatos = new java.util.Vector();

		java.util.Vector tempNomes = new java.util.Vector();
		java.util.Vector temp2 = new java.util.Vector();
		temp2.add(entrada.get("CODIGO").toString());
		tempDatos.add(temp2);
		tempNomes.add("CODIGO");

		es.altia.util.conexion.Cursor cursorSalida =
			new es.altia.util.conexion.Cursor(0, tempDatos, tempNomes);

		return cursorSalida;
	}

	private void ValidaInforme(es.altia.util.HashtableWithNull entrada)
		throws Exception {
		String codigoEntidade =
			((entrada.get("COD_ENTIDADEINFORME") != null)
				&& !(entrada
					.get("COD_ENTIDADEINFORME")
					.toString()
					.trim()
					.equals("")))
				? entrada.get("COD_ENTIDADEINFORME").toString().trim()
				: null;
		String formato =
			((entrada.get("FORMATO") != null)
				&& !(entrada.get("FORMATO").toString().trim().equals("")))
				? entrada.get("FORMATO").toString().trim()
				: null;

		if (codigoEntidade == null) {
			throw (
				new es.altia.agora.business.geninformes.utils.ExceptionXade(
					2012));
		}

		if (formato == null) {
			throw (
				new es.altia.agora.business.geninformes.utils.ExceptionXade(
					2013));
		}

		ValRegXerador.ValidaExisteEntidadeInforme(codigoEntidade, this.con);
		ValidaSeleccionInforme(entrada);
		ValidaOrdenacionInforme(entrada);
		ValidaCondicionInforme(entrada);

		if (formato
			.equals(
				es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ConstantesXerador
					.CODIGO_FORMATO_LISTADO)
			|| formato.equals(
				es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ConstantesXerador
					.CODIGO_FORMATO_ESTATISTICA)) {
			ValidaFormatoListadoInforme(entrada);
		} else if (
			formato.equals(
				es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ConstantesXerador
					.CODIGO_FORMATO_ETIQUETA)) {
			ValidaFormatoEtiquetasInforme(entrada);
		}
	}

	private void ValidaSeleccionInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		java.util.Collection condiciones =
			(java.util.Collection) entrada.get("CAMPOSSELECCION");
		java.util.Iterator iterCond = null;
		es.altia.util.HashtableWithNull item = null;
		String codigoEntidade = null;
		String codigoCampo = null;
		codigoEntidade = (String) entrada.get("COD_ENTIDADEINFORME");

		if (condiciones != null) {
			iterCond = condiciones.iterator();

			while (iterCond.hasNext()) {
				item = (es.altia.util.HashtableWithNull) iterCond.next();
				es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.valFormato
					.ValForCAMPOCONDICIONINFORME
					.ValidaFormatoPosicion(item.get("POSICION").toString());

				codigoCampo = (String) item.get("COD_CAMPOINFORME");
				ValRegXerador.ValidaCampoEntidadeInforme(
					codigoEntidade,
					codigoCampo,
					this.con);
			}
		}
	}

	private void ValidaOrdenacionInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		java.util.Collection condiciones =
			(java.util.Collection) entrada.get("CAMPOSORDE");
		java.util.Iterator iterCond = null;
		es.altia.util.HashtableWithNull item = null;
		String codigoEntidade = null;
		String codigoCampo = null;
		codigoEntidade = (String) entrada.get("COD_ENTIDADEINFORME");

		if (condiciones != null) {
			iterCond = condiciones.iterator();

			while (iterCond.hasNext()) {
				item = (es.altia.util.HashtableWithNull) iterCond.next();
				es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.valFormato
					.ValForCAMPOCONDICIONINFORME
					.ValidaFormatoPosicion(item.get("POSICION").toString());
				es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.valFormato
					.ValForCAMPOORDEINFORME
					.ValidaFormatoTipoOrde((String) item.get("TIPOORDE"));
				codigoCampo = (String) item.get("COD_CAMPOINFORME");
				ValRegXerador.ValidaCampoEntidadeInforme(
					codigoEntidade,
					codigoCampo,
					this.con);
			}
		}
	}

	private void ValidaCondicionInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		java.util.Collection condiciones =
			(java.util.Collection) entrada.get("CAMPOSCONDICION");
		java.util.Iterator iterCond = null;
		es.altia.util.HashtableWithNull item = null;
		String codigoEntidade = null;
		String codigoCampo = null;
		codigoEntidade = (String) entrada.get("COD_ENTIDADEINFORME");

		if (condiciones != null) {
			iterCond = condiciones.iterator();

			while (iterCond.hasNext()) {
				item = (es.altia.util.HashtableWithNull) iterCond.next();
				es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.valFormato
					.ValForCAMPOCONDICIONINFORME
					.ValidaFormatoPosicion(item.get("POSICION").toString());
				es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.valFormato
					.ValForCAMPOCONDICIONINFORME
					.ValidaFormatoClausula((String) item.get("CLAUSULA"));
				es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.valFormato
					.ValForCAMPOCONDICIONINFORME
					.ValidaFormatoOperador((String) item.get("OPERADOR"));
				codigoCampo = (String) item.get("COD_CAMPOINFORME");
				es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.valRegla
					.ValRegXerador
					.ValidaCampoEntidadeInforme(
						codigoEntidade,
						codigoCampo,
						this.con);
			}
		}
	}

	private void ValidaFormatoListadoInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es.altia.util.HashtableWithNull condiciones =
			(es.altia.util.HashtableWithNull) entrada.get("LISTADOXERADOR");
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoTamanoFonte(
				(String) condiciones.get("TAMANOFONTECABECEIRA"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoTamanoFonte((String) condiciones.get("TAMANOFONTEPE"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoTamanoFonte(
				(String) condiciones.get("TAMANOFONTEDETALLE"));

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoOpcionSN((String) condiciones.get("NEGRITACABECEIRA"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoOpcionSN((String) condiciones.get("CURSIVACABECEIRA"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoOpcionSN((String) condiciones.get("CABECEIRAOFICIAL"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoOpcionSN((String) condiciones.get("CABECEIRACENTRO"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoOpcionSN(
				(String) condiciones.get("CABECEIRACOLUMNAS"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoOpcionSN((String) condiciones.get("NUMEROPAXINAPE"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoOpcionSN((String) condiciones.get("DATAINFORMEPE"));

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoMedidaCm((String) condiciones.get("MARXEESQUERDA"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoMedidaCm((String) condiciones.get("MARXEDEREITA"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForLISTADOXERADOR
			.ValidaFormatoOrientacion(
				(String) condiciones.get("ORIENTACIONPAXINA"));

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valRegla
			.ValRegXerador
			.ValidaFonte(
			(String) condiciones.get("FONTEDETALLE"),
			this.con);

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valRegla
			.ValRegXerador
			.ValidaFonte(
			(String) condiciones.get("FONTEPE"),
			this.con);
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valRegla
			.ValRegXerador
			.ValidaFonte(
			(String) condiciones.get("FONTECABECEIRA"),
			this.con);
	}

	private void ValidaFormatoEtiquetasInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es.altia.util.HashtableWithNull condiciones =
			(es.altia.util.HashtableWithNull) entrada.get("ETIQUETAXERADOR");
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoTamanoFonte(
				(String) condiciones.get("TAMANOFONTEDETALLE"));

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoOpcionSN((String) condiciones.get("BORDEETIQUETA"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoMedidaCm((String) condiciones.get("ANCHO"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoMedidaCm((String) condiciones.get("ALTO"));

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoMedidaCm(
				(String) condiciones.get("ESPACIOHORIZONTAL"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoMedidaCm((String) condiciones.get("ESPACIOVERTICAL"));

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoMedidaCm((String) condiciones.get("MARXESUPERIOR"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoMedidaCm((String) condiciones.get("MARXEINFERIOR"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoMedidaCm((String) condiciones.get("MARXEESQUERDA"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoMedidaCm((String) condiciones.get("MARXEDEREITA"));

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoMedidaCm(
				(String) condiciones.get("MARXEESQUERDAETIQUETA"));
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valFormato
			.ValForGeneralXerador
			.ValidaFormatoMedidaCm(
				(String) condiciones.get("MARXESUPERIORETIQUETA"));

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.valRegla
			.ValRegXerador
			.ValidaFonte(
			(String) condiciones.get("FONTEDETALLE"),
			this.con);
	}

	/**
	 * Method generaXMLResultado.
	 * @param ee  La entidad raíz que contiene la estructura de nodos instancia
	 * de esa entidad (que a su vez contienen sus subnodos instancia)
	 * @return String El fichero XML de resultado como un String
	 * @throws Exception Si ocurre algun error.
	 */
    public String generaXMLResultado(EstructuraEntidades ee) throws Exception {

        Document xmlDoc;

		try {
			//Create the document builder
			DocumentBuilderFactory dbFactory =
			DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();

			//Create the document
			xmlDoc = docBuilder.newDocument();
		} catch (Exception e) {
            e.printStackTrace();
			if (m_Log.isErrorEnabled()) m_Log.error("Error creating document: " + e.getMessage());
            throw e;
        }

		Element dataRoot = xmlDoc.createElement("DOCUMENT");

		org.w3c.dom.Node colRaiz = xmlDoc.createElement(ee.getNomeEntidade().toUpperCase() + "_COL");

                if(ee.getDescTipoInforme()!=null && ee.getDescTipoInforme().indexOf("peticion")!=-1){
                    if(ee.getListaInstancias().size()>0){
                        Iterator iterNodos = ee.getListaInstancias().iterator();
                        m_Log.debug("######################## --> NUMERO DE NODOS A PROCESAR: " + ee.getListaInstancias().size());
                        while (iterNodos.hasNext()) {
                            NodoEntidad nodo = (NodoEntidad) iterNodos.next();
                            String domicilio = FormateadorTercero.formatearDomicilioTerceroUOR(nodo.getCampos(), false);

                            colRaiz.appendChild(generaXMLNodoUnico(nodo.getNomeEntidade(), domicilio, "OFICINAREGISTRODOM", xmlDoc));
                        }
                    } else {
                        colRaiz.appendChild(generaXMLNodoUnico("REGISTRO", "Sin Oficina de Registro", "OFICINAREGISTRODOM", xmlDoc));
                    }
                } else {
                    if (ee.getListaInstancias() != null) {
                        Iterator iterNodos = ee.getListaInstancias().iterator();
                        m_Log.debug("######################## --> NUMERO DE NODOS A PROCESAR: " + ee.getListaInstancias().size());
                        while (iterNodos.hasNext()) {
                            NodoEntidad nodo = (NodoEntidad) iterNodos.next();

                            //	Add the row element to the root
                            colRaiz.appendChild(generaXMLNodo(nodo, xmlDoc));
                        }
                    }   
                }

		dataRoot.appendChild(colRaiz);

		// add the root to the document
		xmlDoc.appendChild(dataRoot);

		//		PERFORM THE TRANSFORMATION
		//Create the Transformer
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();

		//Set the source and result
		Source source = new DOMSource(xmlDoc);
		java.io.CharArrayWriter cAW = new CharArrayWriter();

		Result result = new StreamResult(cAW);

		//Copy the Document to std output
		transformer.transform(source, result);
		if (m_Log.isDebugEnabled()) m_Log.debug(cAW.toString());

		return cAW.toString();
	}
	

    /**
     * Se llama para generar un documento normal que se añade en una relación.
     * Por tanto, se mostrará una página por cada expediente de la relación
     * @param ee
     * @return
     * @throws java.lang.Exception
     */
    public String generaXMLResultadoPorExpediente(EstructuraEntidades ee) throws Exception {

        Document xmlDoc;

		try {
			//Create the document builder
			DocumentBuilderFactory dbFactory =
			DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();

			//Create the document
			xmlDoc = docBuilder.newDocument();
		} catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Error creating document: " + e.getMessage());
            throw e;
        }

		Element dataRoot = xmlDoc.createElement("DOCUMENT");

		org.w3c.dom.Node colRaiz;

        if (ee.getListaInstancias() != null) {
			Iterator iterNodos = ee.getListaInstancias().iterator();
           while (iterNodos.hasNext()) {
				//colRaiz = xmlDoc.createElement(ee.getNomeEntidade().toUpperCase() + "_COL");
               colRaiz = xmlDoc.createElement("EXPEDIENTE" + "_COL");
				NodoEntidad nodo = (NodoEntidad) iterNodos.next();
                nodo.setNomeEntidade("EXPEDIENTE");
                //	Add the row element to the root
				colRaiz.appendChild(generaXMLNodo(nodo, xmlDoc));
				dataRoot.appendChild(colRaiz);
                }// while
            }// while
        

		// add the root to the document
		xmlDoc.appendChild(dataRoot);

		//		PERFORM THE TRANSFORMATION
		//Create the Transformer
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();

		//Set the source and result
		Source source = new DOMSource(xmlDoc);
        m_Log.debug(" ******************************************************************** ");
        m_Log.debug(xmlDoc.toString());
        m_Log.debug(" ******************************************************************** ");
		java.io.CharArrayWriter cAW = new CharArrayWriter();

		Result result = new StreamResult(cAW);

		//Copy the Document to std output
		transformer.transform(source, result);
		if (m_Log.isDebugEnabled()) m_Log.debug(cAW.toString());

		return cAW.toString();
	}


	public String generaXMLResultado2(EstructuraEntidades ee) throws Exception {

        Document xmlDoc;

		try {
			//Create the document builder
			DocumentBuilderFactory dbFactory =
			DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();

			//Create the document
			xmlDoc = docBuilder.newDocument();
		} catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) m_Log.error("Error creating document: " + e.getMessage());
            throw e;
        }

		Element dataRoot = xmlDoc.createElement("DOCUMENT");

		org.w3c.dom.Node colRaiz;

        if (ee.getListaInstancias() != null) {
			Iterator iterNodos = ee.getListaInstancias().iterator();

            m_Log.debug(" *********** NOME DA ENTIDADE: " + ee.getNomeEntidade().toUpperCase());
            if(ConstantesDatos.ENTIDAD_DOCUMENTO_EXPEDIENTE_INTERESADO.equals(ee.getNomeEntidade().toUpperCase())){
                while (iterNodos.hasNext()) {
                    colRaiz = xmlDoc.createElement(ee.getNomeEntidade().toUpperCase() + "_COL");
                    NodoEntidad nodo = (NodoEntidad) iterNodos.next();

                    //	Add the row element to the root
                    colRaiz.appendChild(generaXMLNodo(nodo, xmlDoc));
                    dataRoot.appendChild(colRaiz);
                }
           }

          if(ConstantesDatos.ENTIDAD_DOCUMENTO_RELACION.equals(ee.getNomeEntidade().toUpperCase())){
				colRaiz = xmlDoc.createElement(ee.getNomeEntidade().toUpperCase() + "_COL");
            while (iterNodos.hasNext()) {
				NodoEntidad nodo = (NodoEntidad) iterNodos.next();           

                //	Add the row element to the root
				colRaiz.appendChild(generaXMLNodo(nodo, xmlDoc));
				dataRoot.appendChild(colRaiz);
			}
		}
        }

		// add the root to the document
		xmlDoc.appendChild(dataRoot);

		//		PERFORM THE TRANSFORMATION
		//Create the Transformer
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();

		//Set the source and result
		Source source = new DOMSource(xmlDoc);
		java.io.CharArrayWriter cAW = new CharArrayWriter();

		Result result = new StreamResult(cAW);

		//Copy the Document to std output
		transformer.transform(source, result);
		if (m_Log.isDebugEnabled()) m_Log.debug(cAW.toString());

		return cAW.toString();
	}

	/**
	 * Method generaXMLNodo.
	 * @param nodo NodoEntidad que contiene una instancia de entidad con sus
	 * campos y entidades dependientes
	 * @param xmlDoc El documento donde se crearán los elementos
	 * @return Node Un nodo XML ...
	 */
	private org.w3c.dom.Node generaXMLNodo(NodoEntidad nodo, Document xmlDoc) {

		Element salida = xmlDoc.createElement(nodo.getNomeEntidade().toUpperCase());

        if ((nodo.getCampos() != null) && (nodo.getCampos().size() > 0)) {
			java.util.Set setCampos = nodo.getCampos().keySet();
			if ((setCampos != null) && (setCampos.size() > 0)) {

				//	For each row, loop thru  each column
                for (Object objCampo : setCampos) {
                    String campo = objCampo.toString();

                    /*Se eliminan los dos nodos que analizados en el sigueinte if porque, independientemente
                     del nombre del rol, siempre se mete estas 2 etiquetas, y se pueden crear conflictor cuando
                     la descripción del rol en cuestión es "INTERESADO". Este problema solo sucede cuando la
                     plantilla es NO por interesado y NO por relacion, con lo que habrá que tenerlo en cuenta.*/
                    if ((!campo.equals("NOMBREINTERESADO") && !campo.equals("ROLINTERESADO"))
                            || nodo.getNomeEntidade().equals(ConstantesDatos.ENTIDAD_DOCUMENTO_EXPEDIENTE_INTERESADO)
                            || nodo.getNomeEntidade().equals(ConstantesDatos.ENTIDAD_DOCUMENTO_REGISTRO)){

                    String colName = campo.toUpperCase();
                    String colValue = (String) nodo.getCampos().get(campo);

                    Element columnElement = xmlDoc.createElement(colName);

                    Text textData = xmlDoc.createTextNode(colValue);
                    columnElement.appendChild(textData);

                    salida.appendChild(columnElement);
                }
            }
		}
		}
		java.util.HashMap hijos = nodo.getHijosPorTipo();

		if ((hijos != null) && (hijos.size() > 0)) {
			if (hijos.keySet() != null) {

				for (Object objTipoHijo: hijos.keySet()) {
					String tipoHijo = objTipoHijo.toString();

					Vector vectorHijosTipo = (java.util.Vector) hijos.get(tipoHijo);

					if ((vectorHijosTipo != null) && (vectorHijosTipo.size() > 0)) {

                        for (Object objNodoHijo: vectorHijosTipo) {
                            NodoEntidad nodoHijo = (NodoEntidad)objNodoHijo;
                            salida.appendChild(generaXMLNodo(nodoHijo, xmlDoc));
						}
					}
				}
			}
		}

		return salida;
	}
	/**
	 * Method generaXMLNodo.
	 * @param nodo NodoEntidad que contiene una instancia de entidad con sus
	 * campos y entidades dependientes
	 * @param xmlDoc El documento donde se crearán los elementos
	 * @return Node Un nodo XML ...
	 */
	private org.w3c.dom.Node generaXMLNodoUnico(String entidad, String datos, String etiqueta, Document xmlDoc) {

            Element salida = xmlDoc.createElement(entidad.toUpperCase());

            if ((datos != null) && (datos.length() > 0)) {
                Element columnElement = xmlDoc.createElement(etiqueta);

                Text textData = xmlDoc.createTextNode(datos);
                columnElement.appendChild(textData);

                salida.appendChild(columnElement);
            }

            return salida;
	}

	private void RealizaEliminacionEstructura(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.EstructuraXerador estructuraXerador =
			new es.altia.agora.business.geninformes.utils.bd.EstructuraXerador(
				this.con);

		// Borrar la estructura principal
		estructuraXerador.BorraEstructuraXerador(entrada);

	}

	/**
	 * Returns the _params.
	 * @return String[]
	 */
	public static String[] get_params() {
		return _params;
	}

	/**
	 * Sets the _params.
	 * @param _params The _params to set
	 */
	public static void set_params(String[] _params) {
		GeneradorInformesMgr._params = _params;
	}

	//
	// Métodos de mantenimientos
	//
	public es.altia.util.conexion.Cursor ConsultarEntidadesInforme(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		es.altia.util.conexion.Cursor cursorSalida = null;

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);
		try {

			con = adapBD.getConnection();

			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.EntidadeInforme entidadeInforme =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.EntidadeInforme(
					this.con);

			es.altia.agora.business.geninformes.utils.bd.EntSubent entSubent =
				new es.altia.agora.business.geninformes.utils.bd.EntSubent(
					this.con);

			cursorSalida = entidadeInforme.ConsultaEntidadesInforme(entrada);

		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
            throw ex;
		} catch (Exception e) {
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(
				999,
				e,
				(Object) "");
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(bde.getMessage());
            }
		}

		return cursorSalida;
	}

	public es.altia.util.HashtableWithNull ConsultarEntidadeInforme(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		es.altia.util.conexion.Cursor cursorSalida = null;
		es.altia.util.conexion.Cursor cursorSubentidades = null;

		es.altia.util.conexion.Cursor cursorCampos = null;
		es.altia.util.conexion.Cursor cursorAplicaciones = null;
		es.altia.util.HashtableWithNull salida =
			new es.altia.util.HashtableWithNull();

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);
		try {

			//ValidaPermisoPantalla(hdlSesion, MODIFICACION);
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			con = adapBD.getConnection();

			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.EntidadeInforme entidadeInforme =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.EntidadeInforme(
					this.con);
			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.CampoInforme campoInforme =
				new es.altia.agora.business.geninformes.utils.bd.CampoInforme(
					this.con);

			es.altia.agora.business.geninformes.utils.bd.EntSubent entSubent =
				new es.altia.agora.business.geninformes.utils.bd.EntSubent(
					this.con);

			cursorSalida = entidadeInforme.ConsultaEntidadeInforme(entrada);
			salida.put("ENTIDADE", cursorSalida);

			//
			// 1.- Añado cursor de subentidades 
			//
			entrada.put("PARAMS", GeneradorInformesMgr.get_params());

			cursorSubentidades =
				entSubent.ConsultaSubentidadesConCamposJoin(entrada);
			//				es
			//					.altia
			//					.agora
			//					.business
			//					.geninformes
			//					.utils
			//					.bd
			//					.UtilesXerador
			//					.ConsultaSubEntidadesPorEntidadeInforme(entrada);
			salida.put("SUBENTIDADES", cursorSubentidades);

			//
			// 2.- Consulto campos y añado campos de entidad  
			//			
			cursorCampos = campoInforme.ConsultaCamposInforme(entrada);
			salida.put("CAMPOS", cursorCampos);

			//
			// 3.- Consulto aplicaciones de la entidad
			//
			cursorAplicaciones =
				entidadeInforme.ConsultaEntidadeAplicacion(entrada);
			salida.put("APLICACIONES", cursorAplicaciones);

		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(
				999,
				e,
				(Object) "");
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(bde.getMessage());
            }
		}

		return salida;
	}

	public es.altia.util.conexion.Cursor EngadirEntidadeInforme(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		es.altia.util.conexion.Cursor salida = null;

		if (entrada == null) {
			entrada = new es.altia.util.HashtableWithNull();
        }

        entrada.put("PARAMS", _params);
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			con = adapBD.getConnection();

			//String nome = entrada.get("NOME").toString();

			//ValidaInforme(entrada);

			adapBD.inicioTransaccion(con);

			salida = RealizaAltaEntidadeInforme(entrada);

			adapBD.finTransaccion(con);

		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			try {
				adapBD.rollBack(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			} finally {
				throw new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ExceptionXade(
					999,
					e,
					(Object) "");
			}
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}

		return salida;
	}

	private es.altia.util.conexion.Cursor RealizaAltaEntidadeInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		Long salida = null;

		es.altia.util.conexion.Cursor cursorEntidade = null;

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.EntidadeInforme entidadeInforme =
			new es.altia.agora.business.geninformes.utils.bd.EntidadeInforme(
				this.con);

		es.altia.agora.business.geninformes.utils.bd.CampoInforme campoInforme =
			new es.altia.agora.business.geninformes.utils.bd.CampoInforme(
				this.con);

		es.altia.agora.business.geninformes.utils.bd.EntSubent entSubent =
			new es.altia.agora.business.geninformes.utils.bd.EntSubent(
				this.con);

		Long codEse = null;

		//
		// Al grabar la estructura vamos grabando también las etiquetas disponibles
		//
		String prefijo = "";
		java.util.HashMap etiquetas = new java.util.HashMap();

		//
		//
		// 1.- Graba entidad informe 
		//
		//
		// 
		//
		Long codEntidade = null;

		if ((entrada.get("COD_ENTIDADEINFORME") == null)
			|| (entrada
				.get("COD_ENTIDADEINFORME")
				.toString()
				.trim()
				.equals(""))) {
			codEntidade = entidadeInforme.AltaEntidadeInforme(entrada);
		} else {
			entidadeInforme.ModificaEntidadeInforme(entrada);
			codEntidade =
				new Long(entrada.get("COD_ENTIDADEINFORME").toString().trim());
		}

		entrada.put("COD_PAI", codEntidade);

		//
		//
		// 2.- Graba relación con subentidades  con sus campos de Join
		//
		//
		//
		// 2.1- Graba relaciones entre entidades y subentidades
		//

		//
		// 2.2- Graba campos de join
		//

		es.altia.util.HashtableWithNull tempTabla =
			new es.altia.util.HashtableWithNull();
		es.altia.util.HashtableWithNull itemSubent = null;
		es.altia.util.HashtableWithNull itemCamposJoin = null;
		java.util.Iterator iterSubents = null;
		java.util.Iterator iterCamposJoin = null;
		java.util.Vector vectorSubents =
			(java.util.Vector) entrada.get("SUBENTIDADES");
		java.util.Vector vectorCamposjoin = null;
		Long codEsePai = null;

		//m_Log.info("En grbando campos de join:antes if!=null.");
		if (vectorSubents != null) {
			iterSubents = vectorSubents.iterator();
			tempTabla.clear();

			tempTabla.put("COD_ENTIDADEINFORME", codEntidade + "");
            tempTabla.put("PARAMS", _params);

            codEsePai = entSubent.AltaEntSubent(tempTabla);

			tempTabla.put("COD_ESE", codEsePai);

			entSubent.BorraEntSubentHijas(tempTabla);
			entSubent.BorraCamposJoinEse(tempTabla);

			while (iterSubents.hasNext()) {
				itemSubent =
					(es.altia.util.HashtableWithNull) iterSubents.next();

				itemSubent.put("COD_PAI", codEsePai);
				if(m_Log.isDebugEnabled()) m_Log.debug("En itemSubent hay:" + itemSubent);
                itemSubent.put("PARAMS", _params);
                codEse = entSubent.AltaEntSubent(itemSubent);
				tempTabla.put("COD_ESE",codEse);
				
				entSubent.BorraCamposJoinEse(tempTabla);

				if(m_Log.isDebugEnabled()) m_Log.debug( "Despues de BorraCamposJoinEse codESePAi: "+ codEsePai
				    + " codEse: " + codEse);
				vectorCamposjoin =
					(java.util.Vector) itemSubent.get("CAMPOSJOIN");
				if (vectorCamposjoin != null) {
					iterCamposJoin = vectorCamposjoin.iterator();
					while (iterCamposJoin.hasNext()) {
						itemCamposJoin = (es.altia.util.HashtableWithNull) iterCamposJoin.next();
						itemCamposJoin.put("COD_ESE", codEse);
						if(m_Log.isDebugEnabled()) m_Log.debug("Antes de AltaCampoJoinEntSubent codESe: "
                                                    + codEse+ " Item: "+ itemCamposJoin);
						entSubent.AltaCampoJoinEntSubent(itemCamposJoin);
						if(m_Log.isDebugEnabled()) m_Log.debug("Despues de AltaCampoJoinEntSubent codESe: "+ codEse);
					}
				}

			}
		}
		//
		// 3.- Graba campos de informe
		//
		java.util.Vector vectorCamposInforme = null;
		Long codCampoInforme = null;
		String codCampo = null;

		vectorCamposInforme = (java.util.Vector) entrada.get("CAMPOSINFORME");
		if (vectorCamposInforme != null) {
            campoInforme.EliminaCamposInforme(codEntidade);
			iterSubents = vectorCamposInforme.iterator();
			while (iterSubents.hasNext()) {
				itemSubent = (es.altia.util.HashtableWithNull) iterSubents.next();
				itemSubent.put("COD_ENTIDADEINFORME", codEntidade);
                itemSubent.put("PARAMS", _params);
				codCampoInforme = campoInforme.AltaCampoInformeEntidade(itemSubent);
			}
		}

		//
		// 4.- Graba aplicaciones relacionadas
		//
		java.util.Vector vectorAplicaciones = null;
		//Long codCampoInforme = null;

		vectorAplicaciones = (java.util.Vector) entrada.get("APLICACIONES");
		if (vectorAplicaciones != null) {
			iterSubents = vectorAplicaciones.iterator();

			while (iterSubents.hasNext()) {
				itemSubent =
					(es.altia.util.HashtableWithNull) iterSubents.next();
				itemSubent.put("COD_ENTIDADEINFORME", codEntidade);

				entidadeInforme.AltaEntidadeAplicacion(itemSubent);

			}
		}

		//
		// Alta de Informe
		// 

		if ((entrada.get("COD_ENTIDADEINFORME") == null)
			|| (entrada
				.get("COD_ENTIDADEINFORME")
				.toString()
				.trim()
				.equals(""))) {

			salida = codEntidade;
			if(m_Log.isDebugEnabled()) m_Log.debug("ENtré por alt y salgo con salida:" + salida + ".");
		} else {
			salida = new Long(entrada.get("COD_ENTIDADEINFORME").toString());

			if(m_Log.isDebugEnabled()) m_Log.debug("ENtré por modif y salgo con salida:" + salida + ".");
		}

		//

		java.util.Vector tempDatos = new java.util.Vector();
		java.util.Vector tempNomes = new java.util.Vector();
		java.util.Vector temp2 = new java.util.Vector();
		temp2.add(salida);
		tempDatos.add(temp2);
		tempNomes.add("COD_ENTIDADEINFORME");

		es.altia.util.conexion.Cursor cursorSalida =
			new es.altia.util.conexion.Cursor(0, tempDatos, tempNomes);

		return cursorSalida;
	}

	public void ModificarEntidadeInforme(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		if (entrada == null) {
			entrada = new es.altia.util.HashtableWithNull();
		}

        entrada.put("PARAMS", _params);
        AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			con = adapBD.getConnection();

			adapBD.inicioTransaccion(con);

			RealizaModificacionEntidadeInforme(entrada);

			adapBD.finTransaccion(con);

		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			try {
                e.printStackTrace();
                adapBD.rollBack(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			} finally {
				throw new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ExceptionXade(
					999,
					e,
					(Object) "");
			}
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}
	}

	private void RealizaModificacionEntidadeInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {

		//RealizaEliminacionEntidadeInforme(entrada);
		RealizaEliminacionDependientesDeEntidade(entrada);
		RealizaAltaEntidadeInforme(entrada);

	}

	private void RealizaEliminacionDependientesDeEntidade(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.EntidadeInforme entidadeInforme =
			new es.altia.agora.business.geninformes.utils.bd.EntidadeInforme(
				this.con);

		// Borrar la estructura principal
		entidadeInforme.BorraEntidadeAplicacion(entrada);

	}

	private void RealizaEliminacionEntidadeInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.EntidadeInforme entidadeInforme =
			new es.altia.agora.business.geninformes.utils.bd.EntidadeInforme(
				this.con);

		// Borrar la estructura principal
		entidadeInforme.BorraEntidadeInforme(entrada);
	}

	public void EliminarEntidadeInforme(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		if (entrada == null) {
			entrada = new es.altia.util.HashtableWithNull();
		}

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			con = adapBD.getConnection();
			adapBD.inicioTransaccion(con);
			RealizaEliminacionEntidadeInforme(entrada);
			adapBD.finTransaccion(con);

			//
			// Ahora debemos eliminar el report y su query del catálogo.
			//
		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception te) {
			try {
				adapBD.rollBack(con);
			} catch (BDException te2) {
                te2.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te2.getMessage());
			} finally {
				throw new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ExceptionXade(
					999,
					te,
					(Object) "");
			}
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}
	}

	//
	// Métodos para mantenimiento de estructuras
	//

	/**
		   * Method ConsultarEstructuraInforme.
		   * @param entrada
		   * @return Cursor
		   * @throws es.altia.agora.business.geninformes.utils.ExceptionXade
		   */
	public es.altia.util.conexion.Cursor ConsultarEstructuraInforme(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		String formato = null;

		es.altia.util.conexion.Cursor cursorTemp = null;
		es.altia.util.conexion.Cursor cursorFormato =
			new es.altia.util.conexion.Cursor();
		es.altia.util.conexion.Cursor cursorOrdeInforme =
			new es.altia.util.conexion.Cursor();
		es.altia.util.conexion.Cursor cursorSeleccionInforme =
			new es.altia.util.conexion.Cursor();
		es.altia.util.conexion.Cursor cursorCondicionInforme =
			new es.altia.util.conexion.Cursor();

		es.altia.util.conexion.Cursor cursorSalida = null;

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			//ValidaPermisoPantalla(hdlSesion, CONSULTA);
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			//ValidaPermisoPantalla(hdlSesion, MODIFICACION);
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			con = adapBD.getConnection();

			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.InformeXerador informeXerador =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.InformeXerador(
					this.con);
			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.EstructuraXerador estructuraXerador =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.EstructuraXerador(
					this.con);
			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.EtiquetaXerador etiquetaXerador =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.EtiquetaXerador(
					this.con);
			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.ListadoXerador listadoXerador =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.ListadoXerador(
					this.con);
			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.CampoCondicionInforme campoCondicionInforme =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.CampoCondicionInforme(
					this.con);
			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.CampoSeleccionInforme campoSeleccionInforme =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.CampoSeleccionInforme(
					this.con);
			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.CampoOrdeInforme campoOrdeInforme =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.CampoOrdeInforme(
					this.con);
			if(m_Log.isDebugEnabled()) m_Log.debug("entrada antes de consinforme es:" + entrada + ".");
			//cursorTemp = informeXerador.ConsultaInformeXerador(entrada);
			//cursorTemp.next();

			//String codEstructura = cursorTemp.getString("COD_ESTRUCTURA");
			//m_Log.debug(
			//	"Despues de consinf:CodEstructura:" + codEstructura + ".");

			//formato = cursorTemp.getString("FORMATO");
			formato = "L";

			cursorTemp =
				estructuraXerador.ConsultaEstructuraEntidadesInformeAdaptada(
					entrada);

			if (cursorTemp.next()) {
				entrada.put(
					"COD_ESTRUCTURA",
					cursorTemp.getString("COD_ESTRUCTURA"));

				cursorOrdeInforme =
					campoOrdeInforme.ConsultaCampoOrdeInforme(entrada);
				cursorSeleccionInforme =
					campoSeleccionInforme.ConsultaCampoSeleccionInforme(
						entrada);
				cursorCondicionInforme =
					campoCondicionInforme.ConsultaCampoCondicionInforme(
						entrada);
				cursorTemp.anadirColumna("CAMPOSCONDICION");

				if (!cursorCondicionInforme.esVacio()) {
					cursorTemp.anadiraTupla(cursorCondicionInforme);
				} else {
					es.altia.util.conexion.Cursor tempCursor =
						new es.altia.util.conexion.Cursor(
							0,
							new java.util.Vector());

					//es.altia.util.Debug.println(
					//	"En OPXEB: hash.get"
					//		+ tempCursor.hash.get(
					//			es.altia.util.conexion.Cursor.CAMPO_DATOS)
					//		+ ".");
					//es.altia.util.Debug.println(
					//	"En OPXEB curs es:" + tempCursor.hash_Nombres + ".");
					//es.altia.util.Debug.println(
					//	"En OPXEB curs es:"
					//		+ tempCursor.hash_Nombres
					//		+ ".esVAcio:"
					//		+ tempCursor.esVacio()
					//		+ ".");
					cursorTemp.anadiraTupla(tempCursor);

					//es.altia.util.Debug.println(
					//	"Cursor CamposCondicion é baleiro");
				}

				cursorTemp.anadirColumna("CAMPOSSELECCION");
				cursorTemp.anadiraTupla(cursorSeleccionInforme);
				cursorTemp.anadirColumna("CAMPOSORDE");

				if (!cursorOrdeInforme.esVacio()) {
					cursorTemp.anadiraTupla(cursorOrdeInforme);
				} else {
					cursorTemp.anadiraTupla(cursorOrdeInforme);

					//es.altia.util.Debug.println("Cursor CamposOrde é baleiro");
				}

				if (formato
					.equals(
						es
							.altia
							.agora
							.business
							.geninformes
							.utils
							.ConstantesXerador
							.CODIGO_FORMATO_LISTADO)
					|| formato.equals(
						es
							.altia
							.agora
							.business
							.geninformes
							.utils
							.ConstantesXerador
							.CODIGO_FORMATO_ESTATISTICA)) {
					cursorFormato =
						listadoXerador.ConsultaListadoXerador(entrada);
					cursorTemp.anadirColumna("LISTADOXERADOR");
				} else if (
					formato.equals(
						es
							.altia
							.agora
							.business
							.geninformes
							.utils
							.ConstantesXerador
							.CODIGO_FORMATO_ETIQUETA)) {
					cursorFormato =
						etiquetaXerador.ConsultaEtiquetaXerador(entrada);
					cursorTemp.anadirColumna("ETIQUETAXERADOR");
				}

				cursorTemp.anadiraTupla(cursorFormato);
				cursorTemp.Indice = -1;
			}

			cursorSalida = cursorTemp;
		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(
				999,
				e,
				(Object) "");
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}

		return cursorSalida;
	}

	/**
			   * Method ConsultarEstructuraInforme.
			   * @param entrada
			   * @return Cursor
			   * @throws es.altia.agora.business.geninformes.utils.ExceptionXade
			   */
	public es.altia.util.conexion.Cursor ConsultarSubEstructuras(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		String formato = null;

		es.altia.util.conexion.Cursor cursorSalida = null;

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			//ValidaPermisoPantalla(hdlSesion, CONSULTA);
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			//ValidaPermisoPantalla(hdlSesion, MODIFICACION);
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			con = adapBD.getConnection();

			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.EstructuraXerador estructuraXerador =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.EstructuraXerador(
					this.con);

			if(m_Log.isDebugEnabled()) m_Log.debug("entrada antes de conssubestinforme es:" + entrada + ".");

			cursorSalida =
				estructuraXerador.ConsultaHijosEstructuraInformeAdaptada(
					entrada);

			if(m_Log.isDebugEnabled()) m_Log.debug("Despues de conssubest:cursorSalida:" + cursorSalida + ".");

		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(
				999,
				e,
				(Object) "");
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}

		return cursorSalida;
	}

	/**
   * Method ConsultarEstructurasInforme.
   * @param entrada
   * @return Cursor
   * @throws es.altia.agora.business.geninformes.utils.ExceptionXade
   */
	public es.altia.util.conexion.Cursor ConsultarEstructurasInforme(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		String formato = null;

		es.altia.util.conexion.Cursor cursorSalida = null;

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			con = adapBD.getConnection();

			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.EstructuraXerador estructuraXerador =
				new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.bd
					.EstructuraXerador(
					this.con);

			if(m_Log.isDebugEnabled()) m_Log.debug("entrada antes de conssubestinforme es:" + entrada + ".");

			cursorSalida =
				estructuraXerador.ConsultaEstructurasInforme(entrada);

			if(m_Log.isDebugEnabled()) m_Log.debug("Despues de conssubest:cursorSalida:" + cursorSalida + ".");

		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(
				999,
				e,
				(Object) "");
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}

		return cursorSalida;
	}

	/**
	 * Method EliminarEstructuraInforme.
	 * @param entrada
	 * @throws es.altia.agora.business.geninformes.utils.ExceptionXade
	 */
	public void EliminarEstructuraInforme(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		if (entrada == null) {
			entrada = new es.altia.util.HashtableWithNull();
		}

		//String[] _params = (String[]) entrada.get("PARAMS");
		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			//ValidaPermisoPantalla(hdlSesion, MODIFICACION);
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			con = adapBD.getConnection();
			adapBD.inicioTransaccion(con);
			RealizaEliminacionEstructuraInforme(entrada);
			adapBD.finTransaccion(con);

			//
			// Ahora debemos eliminar el report y su query del catálogo.
			//
		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception te) {
			try {
				adapBD.rollBack(con);
			} catch (BDException te2) {
                te2.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te2.getMessage());
			} finally {
				throw new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ExceptionXade(
					999,
					te,
					(Object) "");
			}
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}
	}

	/**
	 * Method EngadirEstructuraInforme.
	 * @param entrada
	 * @return Cursor
	 * @throws es.altia.agora.business.geninformes.utils.ExceptionXade
	 */
	public es.altia.util.conexion.Cursor EngadirEstructuraInforme(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		es.altia.util.conexion.Cursor salida = null;

		if (entrada == null) {
			entrada = new es.altia.util.HashtableWithNull();
		}

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			con = adapBD.getConnection();

			String nome = entrada.get("NOME").toString();

			//ValidaInforme(entrada);

			adapBD.inicioTransaccion(con);

			salida = RealizaAltaEstructuraInforme(entrada);

			adapBD.finTransaccion(con);

		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			try {
				adapBD.rollBack(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			} finally {
				throw new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ExceptionXade(
					999,
					e,
					(Object) "");
			}
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}

		return salida;
	}

	/**
	 * Method ModificarEstructuraInforme.
	 * @param entrada
	 * @throws es.altia.agora.business.geninformes.utils.ExceptionXade
	 */
	public void ModificarEstructuraInforme(
		es.altia.util.HashtableWithNull entrada)
		throws es.altia.agora.business.geninformes.utils.ExceptionXade {
		if (entrada == null) {
			entrada = new es.altia.util.HashtableWithNull();
		}

		AdaptadorSQLBD adapBD = new AdaptadorSQLBD(_params);

		try {
			//entrada.put("CENCOD", getSesion(hdlSesion).getCOD_CENTRO());
			//ValidaPermisoPantalla(hdlSesion, MODIFICACION);
			this.con = adapBD.getConnection();

			//ValidaInforme(entrada);

			adapBD.inicioTransaccion(this.con);

			RealizaModificacionEstructuraInforme(entrada);

			adapBD.finTransaccion(this.con);

			//
			// Se borra el informe y la query del mismo
			//
			//
			// Se crea de nuevo la query y el informe
			//
		} catch (es.altia.agora.business.geninformes.utils.ExceptionXade ex) {
			throw ex;
		} catch (Exception e) {
			try {
				adapBD.rollBack(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			} finally {
				throw new es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.ExceptionXade(
					999,
					e,
					(Object) "");
			}
		} finally {
			try {
				adapBD.devolverConexion(con);
			} catch (BDException bde) {
                bde.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + bde.getMessage());
			}
		}
	}

	private void RealizaEliminacionEstructuraInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.InformeXerador informeXerador =
			new es.altia.agora.business.geninformes.utils.bd.InformeXerador(
				this.con);

		//
		// 1.-Eliminar vistas en la BBDD
		//
		//
		// 2.-Eliminar DTD (ya se elimina en el DELETE)
		//
		//
		// 3.-Eliminar plantilla WORD (.dot) se elimina en el DELETE
		//
		//
		// 4.- ELiminar informe de BBDD
		//
		//
		// Confiamos en el ON DELETE CASCADE DEL SGBD
		//
		///		campoCondicionInforme.BorraCampoCondicionInforme(entrada);
		//		campoOrdeInforme.BorraCampoOrdeInforme(entrada);
		//		campoSeleccionInforme.BorraCampoSeleccionInforme(entrada);
		//		estructuraXerador.BorraEstructuraXerador(entrada);
		//		
		//		etiquetaXerador.BorraEtiquetaXerador(entrada);
		//		listadoXerador.BorraListadoXerador(entrada);
		// Borrar la estructura principal

		es.altia.util.HashtableWithNull tempTabla =
			new es.altia.util.HashtableWithNull();

		RealizaEliminacionEstructura(entrada);

	}

	/**
		 * Method RealizaModificacionEstructuraInforme.
		 * @param entrada
		 * @throws Exception
		 */
	private void RealizaModificacionEstructuraInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.EstructuraXerador estructuraXerador =
			new es.altia.agora.business.geninformes.utils.bd.EstructuraXerador(
				this.con);
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.CampoCondicionInforme campoCondicionInforme =
			new es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.CampoCondicionInforme(
				this.con);
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.CampoOrdeInforme campoOrdeInforme =
			new es.altia.agora.business.geninformes.utils.bd.CampoOrdeInforme(
				this.con);
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.CampoSeleccionInforme campoSeleccionInforme =
			new es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.CampoSeleccionInforme(
				this.con);
		es.altia.util.HashtableWithNull tempTabla =
			new es.altia.util.HashtableWithNull();

		campoCondicionInforme.BorraCampoCondicionInforme(entrada);
		campoOrdeInforme.BorraCampoOrdeInforme(entrada);
		campoSeleccionInforme.BorraCampoSeleccionInforme(entrada);
		estructuraXerador.BorraSubEstructurasEstructura(entrada);
		RealizaAltaEstructuraInforme(entrada);

		//			}

	}

	/**
	 * Method RealizaAltaEstructuraInforme.
	 * @param entrada
	 * @return Cursor
	 * @throws Exception
	 */
	private es.altia.util.conexion.Cursor RealizaAltaEstructuraInforme(
		es.altia.util.HashtableWithNull entrada)
		throws Exception {
		Long salida = null;

		es.altia.util.conexion.Cursor cursorEntidade = null;

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.InformeXerador informeXerador =
			new es.altia.agora.business.geninformes.utils.bd.InformeXerador(
				this.con);
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.EtiquetaXerador etiquetaXerador =
			new es.altia.agora.business.geninformes.utils.bd.EtiquetaXerador(
				this.con);
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.ListadoXerador listadoXerador =
			new es.altia.agora.business.geninformes.utils.bd.ListadoXerador(
				this.con);
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.CampoCondicionInforme campoCondicionInforme =
			new es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.CampoCondicionInforme(
				this.con);
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.CampoSeleccionInforme campoSeleccionInforme =
			new es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.CampoSeleccionInforme(
				this.con);
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.bd
			.CampoOrdeInforme campoOrdeInforme =
			new es.altia.agora.business.geninformes.utils.bd.CampoOrdeInforme(
				this.con);

		es.altia.agora.business.geninformes.utils.bd.EstructuraXerador ex =
			new es.altia.agora.business.geninformes.utils.bd.EstructuraXerador(
				this.con);

		es.altia.agora.business.geninformes.utils.bd.EtiqPlt etiqPlt =
			new es.altia.agora.business.geninformes.utils.bd.EtiqPlt(this.con);
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.XeracionInformes
			.EstructuraEntidades ee =
			null;

		//
		// Al grabar la estructura vamos grabando también las etiquetas disponibles
		//
		String prefijo = "";
		java.util.HashMap etiquetas = new java.util.HashMap();

		//String codEstructuraRaiz = null;
		//
		// 1.- Graba informe y estructura en la BBDD
		//
		//
		// Alta de nodo raíz de la estructura de entidades
		//
		es.altia.util.HashtableWithNull tempEx =
			new es.altia.util.HashtableWithNull();
		es.altia.util.HashtableWithNull tempCampo =
			new es.altia.util.HashtableWithNull();
		ee =
			(
				es
					.altia
					.agora
					.business
					.geninformes
					.utils
					.XeracionInformes
					.EstructuraEntidades) entrada
					.get(
				"ESTRUCTURAINFORME");

		Integer posicion = new Integer(0);

		String querySQL =
			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.UtilidadesXerador
				.creaQuerySQLEE(
				ee);

		tempEx.put("CONSULTASQL", querySQL);
		tempEx.put("COD_PAI", null);
		tempEx.put("COD_ENTIDADEINFORME", entrada.get("COD_ENTIDADEINFORME"));
		tempEx.put("POSICION", posicion);
        tempEx.put("PARAMS", _params);

        Long codEstructuraRaiz = null;

		if ((entrada.get("COD_ESTRUCTURA") == null)
			|| (entrada.get("COD_ESTRUCTURA").toString().trim().equals(""))) {

			salida = ex.AltaEstructuraXerador(tempEx);
			codEstructuraRaiz = salida;
			if(m_Log.isDebugEnabled()) m_Log.debug("ENtré por alt y salgo con salida:" + salida + ".");
		} else {
			salida = new Long(entrada.get("COD_ESTRUCTURA").toString());
			codEstructuraRaiz = salida;
			String[] _params2 = (String[]) entrada.get("PARAMS");
			if(m_Log.isDebugEnabled()) m_Log.debug("los parametros en realizaAltaEstructuraInforme de entrada son : " + _params2);
			tempEx.put("PARAMS", _params2);
			String[] _params1 = (String[]) tempEx.get("PARAMS");
			if (m_Log.isDebugEnabled()) m_Log.debug("los parametros en realizaAltaEstructuraInforme son : " + _params1);
			ex.ModificarEstructuraXerador(tempEx);
			if(m_Log.isDebugEnabled()) m_Log.debug("ENtré por modif y salgo con salida:" + salida + ".");
		}

		tempEx.clear();

		//
		// Meto en A_DOC
		//
		if ((entrada.get("COD_ESTRUCTURA") == null)
			|| (entrada.get("COD_ESTRUCTURA").toString().trim().equals(""))) {

			tempEx.put("COD_ESTRUCTURA", codEstructuraRaiz);
			tempEx.put("APL_COD", entrada.get("APL_COD"));
			tempEx.put("DOC_NOM", entrada.get("DOC_NOM"));
			if (m_Log.isDebugEnabled()) m_Log.debug("tempEX:" + tempEx + ".");

			ex.AltaEstructuraAplicacion(tempEx);
			//m_Log.info("Por COD_ESTRCUTURA==null");
		} else {
			
            //m_Log.info("Por COD_ESTRCUTURA!=null");
            tempEx.put("COD_ESTRUCTURA", entrada.get("COD_ESTRUCTURA"));
			tempEx.put("APL_COD", entrada.get("APL_COD"));
			tempEx.put("DOC_NOM", entrada.get("DOC_NOM"));
			if(m_Log.isDebugEnabled()) m_Log.debug("tempEX:" + tempEx + ".");
	
			ex.ModificaEstructuraAplicacion(tempEx);
		}
		//
		//
		//		
		tempEx.clear();

		//
		// Pongo estructuras hijas apuntando a este padre
		//
		java.util.Vector vSubEstructuras =
			(java.util.Vector) entrada.get("SUBESTRUCTURAS");
		if ((vSubEstructuras != null) && (vSubEstructuras.size() > 0)) {
			java.util.Iterator iterSubes = vSubEstructuras.iterator();
			String codSubes = null;

			int pos = 0;
			while (iterSubes.hasNext()) {
				codSubes = iterSubes.next().toString();
				tempEx.clear();
				tempEx.put("COD_ESTRUCTURA", codSubes);
				tempEx.put("POSICION", pos + "");
				tempEx.put("COD_PAI", codEstructuraRaiz);

				ex.ModificarPaiEstructuraXerador(tempEx);
				pos++;
			}
		}
		//
		//
		// 
		tempEx.clear();
		//
		// Consulto datos da entidade
		//		
		tempEx.put("PARAMS", _params);
		tempEx.put("COD_ENTIDADEINFORME", entrada.get("COD_ENTIDADEINFORME"));
		cursorEntidade =
			es
				.altia
				.agora
				.business
				.geninformes
				.utils
				.bd
				.UtilesXerador
				.ConsultaDatosEntidadeInforme(tempEx);
		tempEx.clear();
		cursorEntidade.next();
		ee.setNomeEntidade(cursorEntidade.getString("NOME"));

		if(m_Log.isDebugEnabled()) m_Log.debug("MEtí nome de entidade:" + ee.getNomeEntidade() + ".");

		entrada.put("COD_ESTRUCTURA", codEstructuraRaiz);

		java.util.Iterator iterHijosNivel = null;
		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.XeracionInformes
			.EstructuraEntidades hijo =
			null;

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.XeracionInformes
			.CampoSeleccionInforme itemSeleccion =
			null;

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.XeracionInformes
			.CampoCondicionInforme itemCondicion =
			null;

		es
			.altia
			.agora
			.business
			.geninformes
			.utils
			.XeracionInformes
			.CampoOrdeInforme itemOrde =
			null;
		//
		//
		//
		//Campos de selección
		java.util.Iterator iterCond = null;
		tempCampo.clear();
		java.util.Vector vectorEtiquetas = null;

		if (ee.getCamposSeleccion() != null) {
			iterCond = ee.getCamposSeleccion().iterator();

			while (iterCond.hasNext()) {
				itemSeleccion =
					(es
						.altia
						.agora
						.business
						.geninformes
						.utils
						.XeracionInformes
						.CampoSeleccionInforme) iterCond
						.next();

				tempCampo.put("COD_ESTRUCTURA", codEstructuraRaiz);
				tempCampo.put(
					"COD_CAMPOINFORME",
					itemSeleccion.getCodCampoInforme());
				tempCampo.put("POSICION", itemSeleccion.getPosicion());
				tempCampo.put("ANCHO", itemSeleccion.getAncho());
				if(m_Log.isDebugEnabled()) m_Log.debug("Le paso a altacamposel=" + tempCampo + ".");
				campoSeleccionInforme.AltaCampoSeleccionInforme(tempCampo);

				//
				// Para obtener etiqeutas
				//
				prefijo = ee.getPrefijo();
				vectorEtiquetas = (java.util.Vector) etiquetas.get(prefijo);
				if (vectorEtiquetas == null) {
					vectorEtiquetas = new java.util.Vector();
					vectorEtiquetas.add(itemSeleccion.getCodCampoInforme());
					etiquetas.put(prefijo, vectorEtiquetas);
				} else
					vectorEtiquetas.add(itemSeleccion.getCodCampoInforme());

				//
				// FIN: Para obtener etiqeutas
				//

			}
		}

		//	Campos de condición
		tempCampo.clear();

		if (ee.getCamposCondicion() != null) {
			iterCond = ee.getCamposCondicion().iterator();

			while (iterCond.hasNext()) {
				itemCondicion =
					(es
						.altia
						.agora
						.business
						.geninformes
						.utils
						.XeracionInformes
						.CampoCondicionInforme) iterCond
						.next();

				tempCampo.put("COD_ESTRUCTURA", codEstructuraRaiz);
				tempCampo.put(
					"COD_CAMPOINFORME",
					itemCondicion.getCodCampoInforme());
				tempCampo.put("POSICION", itemCondicion.getPosicion());
				tempCampo.put("CLAUSULA", itemCondicion.getClausula());
				tempCampo.put("OPERADOR", itemCondicion.getOperador());
				tempCampo.put("VALOR", itemCondicion.getValor());

				campoCondicionInforme.AltaCampoCondicionInforme(tempCampo);
			}
		}

		//	Campos de Ordenación
		tempCampo.clear();

		if (ee.getCamposOrde() != null) {
			iterCond = ee.getCamposOrde().iterator();

			while (iterCond.hasNext()) {
				itemOrde =
					(es
						.altia
						.agora
						.business
						.geninformes
						.utils
						.XeracionInformes
						.CampoOrdeInforme) iterCond
						.next();

				tempCampo.put("COD_ESTRUCTURA", codEstructuraRaiz);
				tempCampo.put(
					"COD_CAMPOINFORME",
					itemOrde.getCodCampoInforme());
				tempCampo.put("POSICION", itemOrde.getPosicion());
				tempCampo.put("TIPOORDE", itemOrde.getTipoOrde());

				campoOrdeInforme.AltaCampoOrdeInforme(tempCampo);
			}
		}

		//
		//
		//
		java.util.Vector nuevosHijosNivel = null;
		Long salidaEstructura = null;

		java.util.Vector hijosNivel = ee.getHijos();

		// Primer bucle recorre cada nivel
		while ((hijosNivel != null) && (hijosNivel.size() > 0)) {
			iterHijosNivel = hijosNivel.iterator();
			nuevosHijosNivel = new java.util.Vector();

			// Bucle recorre hijos de un nivel
			while (iterHijosNivel.hasNext()) {
				hijo =
					(es
						.altia
						.agora
						.business
						.geninformes
						.utils
						.XeracionInformes
						.EstructuraEntidades) iterHijosNivel
						.next();

				tempEx.put("COD_ENTIDADEINFORME", hijo.getCodEntidadeInforme());
				tempEx.put("COD_PAI", hijo.getCodPai());
				tempEx.put("POSICION", hijo.getPosicion());
				querySQL =
					es
						.altia
						.agora
						.business
						.geninformes
						.utils
						.UtilidadesXerador
						.creaQuerySQLEE(
						hijo);

				tempEx.put("CONSULTASQL", querySQL);
                tempEx.put("PARAMS", _params);
				salidaEstructura = ex.AltaEstructuraXerador(tempEx);

				//
				// Consulto datos da entidade
				//		
				tempEx.put("PARAMS", _params);
				tempEx.put("COD_ENTIDADEINFORME", hijo.getCodEntidadeInforme());
				cursorEntidade =
					es
						.altia
						.agora
						.business
						.geninformes
						.utils
						.bd
						.UtilesXerador
						.ConsultaDatosEntidadeInforme(tempEx);
				tempEx.clear();
				cursorEntidade.next();
				hijo.setNomeEntidade(cursorEntidade.getString("NOME"));
				if(m_Log.isDebugEnabled()) m_Log.debug("MEtí nome de entidade:" + hijo.getNomeEntidade() + ".");
				java.util.Iterator iterTemp = null;

				if (hijo.getHijos() != null) {
					iterTemp = hijo.getHijos().iterator();

					int posicionHijo = 0;
					es
						.altia
						.agora
						.business
						.geninformes
						.utils
						.XeracionInformes
						.EstructuraEntidades eeTemp =
						null;

					while (iterTemp.hasNext()) {
						eeTemp =
							(es
								.altia
								.agora
								.business
								.geninformes
								.utils
								.XeracionInformes
								.EstructuraEntidades) iterTemp
								.next();
						eeTemp.setCodPai(salidaEstructura.toString());
						eeTemp.setPai(hijo);
						eeTemp.setPosicion(posicionHijo + "");
						posicionHijo++;
					}

					nuevosHijosNivel.addAll(hijo.getHijos());
				}

				tempEx.put("COD_ESTRUCTURA", salidaEstructura);

				// Campos de selección
				tempCampo.clear();

				if (hijo.getCamposSeleccion() != null) {
					iterCond = hijo.getCamposSeleccion().iterator();

					while (iterCond.hasNext()) {
						itemSeleccion =
							(es
								.altia
								.agora
								.business
								.geninformes
								.utils
								.XeracionInformes
								.CampoSeleccionInforme) iterCond
								.next();

						tempCampo.put("COD_ESTRUCTURA", salidaEstructura);
						tempCampo.put(
							"COD_CAMPOINFORME",
							itemSeleccion.getCodCampoInforme());
						tempCampo.put("POSICION", itemSeleccion.getPosicion());
						tempCampo.put("ANCHO", itemSeleccion.getAncho());

						campoSeleccionInforme.AltaCampoSeleccionInforme(
							tempCampo);

						//
						// PAra obtener etiquetas
						// 
						prefijo = hijo.getPrefijo();
						vectorEtiquetas =
							(java.util.Vector) etiquetas.get(prefijo);
						if (vectorEtiquetas == null) {
							vectorEtiquetas = new java.util.Vector();
							vectorEtiquetas.add(
								itemSeleccion.getCodCampoInforme());
							etiquetas.put(prefijo, vectorEtiquetas);
						} else
							vectorEtiquetas.add(
								itemSeleccion.getCodCampoInforme());
						//
						//			FIN: PAra obtener etiquetas
						// 

					}
				}

				//	Campos de condición
				tempCampo.clear();

				if (hijo.getCamposCondicion() != null) {
					iterCond = hijo.getCamposCondicion().iterator();

					while (iterCond.hasNext()) {
						itemCondicion =
							(es
								.altia
								.agora
								.business
								.geninformes
								.utils
								.XeracionInformes
								.CampoCondicionInforme) iterCond
								.next();

						tempCampo.put("COD_ESTRUCTURA", salidaEstructura);
						tempCampo.put(
							"COD_CAMPOINFORME",
							itemCondicion.getCodCampoInforme());
						tempCampo.put("POSICION", itemCondicion.getPosicion());
						tempCampo.put("CLAUSULA", itemCondicion.getClausula());
						tempCampo.put("OPERADOR", itemCondicion.getOperador());
						tempCampo.put("VALOR", itemCondicion.getValor());

						campoCondicionInforme.AltaCampoCondicionInforme(
							tempCampo);
					}
				}

				//				Campos de Ordenación
				tempCampo.clear();

				if (hijo.getCamposOrde() != null) {
					iterCond = hijo.getCamposOrde().iterator();

					while (iterCond.hasNext()) {
						itemOrde =
							(es
								.altia
								.agora
								.business
								.geninformes
								.utils
								.XeracionInformes
								.CampoOrdeInforme) iterCond
								.next();

						tempCampo.put("COD_ESTRUCTURA", salidaEstructura);
						tempCampo.put(
							"COD_CAMPOINFORME",
							itemOrde.getCodCampoInforme());
						tempCampo.put("POSICION", itemOrde.getPosicion());
						tempCampo.put("TIPOORDE", itemOrde.getTipoOrde());

						campoOrdeInforme.AltaCampoOrdeInforme(tempCampo);
					}
				}
			}

			hijosNivel = nuevosHijosNivel;
		}

		entrada.put("COD_ESTRUCTURA", codEstructuraRaiz);

		//
		// Generamos  las vistas 
		//
		//String dtdContido=es.altia.agora.business.geninformes.utils.UtilidadesXerador.generaDTD(es.altia.agora.business.geninformes.utils.UtilidadesXerador.generaArbolCompleto(ee));
		//entrada.put("DTD_CONTIDO",dtdContido);
		entrada.put("DTD_CONTIDO", null);

		//
		//		2.- Generar plantilla .dot
		//
		// 
		// Enviar fichero de la plantilla como un array de bytes
		//

		//byte[] aa =(byte [])entrada.get("F_DOT");
		//entrada.put("F_DOT", aa);

		//
		// Alta de Informe
		// 

		//
		// DOy de alta las etiquetas
		//
		java.util.Iterator iterCI = null;
		String codCI = null;

		//m_Log.debug("Etiquetas es:"+etiquetas.toString()+".");

		//		if ((etiquetas != null) && (etiquetas.size() > 0)) {
		//			java.util.Iterator iterEt = etiquetas.keySet().iterator();
		//			while (iterEt.hasNext()) {
		//				tempEx.clear();
		//				prefijo = iterEt.next().toString();

		//				tempEx.put("COD_INFORMEXERADOR", salida + "");
		//				tempEx.put("PREFIJO", prefijo);
		//				m_Log.debug("TempEx" + tempEx + ".");

		//				vectorEtiquetas = (java.util.Vector) etiquetas.get(prefijo);
		//				if (vectorEtiquetas != null) {
		//					iterCI = vectorEtiquetas.iterator();
		//					while (iterCI.hasNext()) {
		//						codCI = iterCI.next().toString();
		//						tempEx.put("COD_CAMPOINFORME", codCI);
		//						etiqPlt.AltaEtiqPlt(tempEx);
		//					}
		//				}
		//
		//			}
		//
		//		}
		//		//
		// FIN: DOy de alta las etiquetas
		//

		java.util.Vector tempDatos = new java.util.Vector();
		java.util.Vector tempNomes = new java.util.Vector();
		java.util.Vector temp2 = new java.util.Vector();
		temp2.add(salida);
		tempDatos.add(temp2);
		tempNomes.add("COD_ESTRUCTURA");

		es.altia.util.conexion.Cursor cursorSalida =
			new es.altia.util.conexion.Cursor(0, tempDatos, tempNomes);

		return cursorSalida;
	}

}