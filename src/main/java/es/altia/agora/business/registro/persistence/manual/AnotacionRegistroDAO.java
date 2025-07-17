package es.altia.agora.business.registro.persistence.manual;


import es.altia.agora.business.administracion.mantenimiento.CamposListadosParametrizablesVO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.SimpleRegistroValueObject;
import es.altia.agora.business.registro.exception.AbrirCerrarRegistroException;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.persistence.RegistroAperturaCierreManager;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.Fecha;
import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import java.util.logging.Level;
import java.util.logging.Logger; 

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.*;
import es.altia.util.StringUtils;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
//para generacion de documentos justificante:
import es.altia.agora.business.geninformes.GeneradorInformesMgr;
import es.altia.agora.business.geninformes.utils.UtilidadesXerador;
import es.altia.agora.business.geninformes.utils.XeracionInformes.EstructuraEntidades;
import es.altia.agora.business.geninformes.utils.XeracionInformes.NodoEntidad;
import es.altia.agora.business.registro.DescripcionRegistroValueObject;
import es.altia.agora.business.registro.DocumentoValueObject;
import es.altia.agora.business.registro.HistoricoMovimientoValueObject;
import es.altia.agora.business.registro.mantenimiento.MantAsuntosValueObject;
import es.altia.agora.business.registro.mantenimiento.persistence.manual.MantAsuntosDAO;
import es.altia.agora.business.registro.persistence.HistoricoMovimientoManager;
import es.altia.agora.business.sge.DocumentoAnotacionRegistroVO;
import es.altia.agora.business.sge.persistence.manual.DefinicionProcedimientosDAO;
import es.altia.agora.business.sge.persistence.manual.InteresadosDAO;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.business.util.HistoricoAnotacionHelper;
import es.altia.flexia.expedientes.relacionados.plugin.PluginExpedientesRelacionados;
import es.altia.flexia.expedientes.relacionados.plugin.factoria.PluginExpedientesRelacionadosFactoria;
import es.altia.flexia.expedientes.relacionados.plugin.vo.ExpedienteRelacionadoVO;
import es.altia.util.commons.DateOperations;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import es.altia.agora.business.escritorio.UsuarioValueObject; 
import es.altia.agora.business.registro.DocumentoMetadatosVO;
import es.altia.agora.business.registro.TablaMetadatos;
import es.altia.flexia.registro.digitalizacion.lanbide.persistence.manual.DigitalizacionDocumentosLanbideDAO;
import es.altia.flexia.registro.digitalizacion.lanbide.util.DocumentoCatalogacionConversor;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.DocumentoCatalogacionVO;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.agora.business.sge.MetadatosDocumentoVO;
import es.altia.agora.business.sge.persistence.manual.DocumentoDAO;
import es.altia.util.jdbc.JdbcOperations;
import es.altia.util.persistance.KeyValueObject;
import org.apache.commons.lang.BooleanUtils;
// Importamos la libreria gestion-numeracion-anotacion_1.0.jar
import es.altia.flexia.business.registro.util.GestionSecuenciacionNumerosAnotacion;
import es.altia.flexia.business.registro.vo.AnotacionRegistroVO;

public class AnotacionRegistroDAO {

	/*
	 * Declaracion de servicios
	 */

	protected static Config m_ConfigTechnical; //Para el fichero de configuracion tecnico
        protected static Config m_ConfigRegistro;
	protected static Config m_ConfigError; //Para los mensajes de error localizados
	protected static Log m_Log =
		LogFactory.getLog(AnotacionRegistroDAO.class.getName());

	protected static String sql_ideTipoDoc;
	protected static String sql_codTipoDoc;
	protected static String sql_descTipoDoc;
	protected static String sql_actTipoDoc;
	protected static String sql_ideTipoRemit;
	protected static String sql_codTipoRemit;
	protected static String sql_descTipoRemit;
	protected static String sql_actTipoRemit;
	protected static String sql_ideTipoTransp;
	protected static String sql_codTipoTransp;
	protected static String sql_descTipoTransp;
	protected static String sql_actTipoTransp;
	protected static String sql_ideTipoAct;
	protected static String sql_codTipoAct;
	protected static String sql_descTipoAct;
	protected static String sql_actFechaDesde;
	protected static String sql_actFechaHasta;
	protected static String sql_codDpto;
	protected static String sql_descDpto;
	protected static String sql_organizacionDEP;
	protected static String sql_entidadDEP;
	protected static String sql_codigoENT;
	protected static String sql_organizacionENT;
	protected static String sql_nombreENT;

	protected static String sql_nombreUOR;
	protected static String sql_codiVisibleUOR;
	//protected static String sql_departamentoUOR;
	protected static String sql_codigoUOR;
	//protected static String sql_entidadUOR;
	//protected static String sql_organizacionUOR;

	protected static String sql_ultNumAnotacion;
	protected static String sql_ejercicio;
	protected static String sql_nre_codDpto;
	protected static String sql_nre_codUni;
	protected static String sql_nre_tipoReg;
	protected static String sql_codDptoAnotacion;
	protected static String sql_codUnidadAnotacion;
	protected static String sql_tipoRegAnotacion;
	protected static String sql_ejercicioAnotacion;
	protected static String sql_numeroAnotacion;
	protected static String sql_fechaAnotacion;
	protected static String sql_fechaDocumento;
	protected static String sql_asuntoAnotacion;
	protected static String sql_tipoAnotacionDestino;
	protected static String sql_tipoDocAnotacion;
	protected static String sql_tipoRemitenteAnotacion;
	protected static String sql_codTerceroAnotacion;
	protected static String sql_domicTerceroAnotacion;
	protected static String sql_numModifInteresado;
	protected static String sql_numTransp;
	protected static String sql_tipTransp;
	protected static String sql_entidadDestino;
	//tiruritata
	//protected static String sql_dptoDestino;
	protected static String sql_unidRegDestino;
	protected static String sql_orgDestAnot;
	// tiruritata
	//protected static String sql_departOrigDest;
	protected static String sql_unidOrigDest;
	protected static String sql_orgOrigAnot;
	protected static String sql_departOrigAnot;
	protected static String sql_unidOrigAnot;
	protected static String sql_entOrigAnot;
	protected static String sql_tipoOrigDest;
	protected static String sql_ejeOrig;
	protected static String sql_numOrig;
	protected static String sql_actuacion;
	protected static String sql_estAnot;
	protected static String sql_diligencia;
	protected static String sql_tipoTransporte;
	protected static String sql_entradaRelacionada;
	protected static String sql_autoridad; //Autoridad a la que se dirige el registro
	protected static String sql_procedimiento;
	protected static String sql_munProcedimiento;

	protected static String sql_ejercicioRER;
	protected static String sql_numeroRER;
	protected static String sql_fechaRER;
	protected static String sql_tipoRER;
	protected static String sql_codigoDepartamentoRER;
	protected static String sql_codigoUnidadRER;

	// Lista de temas.
	protected static String sql_idTema;
	protected static String sql_codTema;
	protected static String sql_descTema;
	// Temas
	protected static String sql_temasDepto;
	protected static String sql_temasUnid;
	protected static String sql_temasEjerc;
	protected static String sql_temasNum;
	protected static String sql_temasTipo;
	protected static String sql_temasOrden;
	protected static String sql_temasIdTema;
	// Docs
	protected static String sql_docsDepto;
	protected static String sql_docsUnid;
	protected static String sql_docsEjerc;
	protected static String sql_docsNum;
	protected static String sql_docsTipo;
	protected static String sql_docsNombreDoc;
	protected static String sql_docsTipoDoc;
	protected static String sql_docsDoc;
        protected static String sql_docsFec;
	//Interesados
	protected static String sql_intecodUor;
	protected static String sql_intetipo;
	protected static String sql_inteano;
	protected static String sql_intenumero;
	protected static String sql_intecoddepartamento;
	protected static String sql_intecodTercero;
	protected static String sql_inteverTercero;
	protected static String sql_intecodDomicilio;
	protected static String sql_interolTercero;
	// Buzon.
	protected static String sql_buzonOrg;
	protected static String sql_buzonEnt;
	protected static String sql_buzonDpto;
	protected static String sql_buzonUnidReg;
	protected static String sql_buzonTipo;
	protected static String sql_buzonEjerc;
	protected static String sql_buzonNum;
	protected static String sql_buzonFechEntrada;
	protected static String sql_buzonFechDoc;
	protected static String sql_buzonAsunto;
	protected static String sql_buzonOrgDest;
	protected static String sql_buzonEntDest;
	protected static String sql_buzonDptoDest;
	protected static String sql_buzonUnidDest;
	protected static String sql_buzonTipoTransp;
	protected static String sql_buzonNumTransp;
	protected static String sql_buzonTipoDoc;
	protected static String sql_buzonTipoRemit;
	protected static String sql_buzonActuacion;
	protected static String sql_buzonIdTercero;
	protected static String sql_buzonDomTercero;
	protected static String sql_buzonNumModifInfTercero;

	protected static String sql_codigoORG;
	protected static String sql_nombreORG;

	protected static String sql_codigoORGEX;
	protected static String sql_nombreORGEX;

	protected static String sql_nombreUOREX;
	protected static String sql_codigoUOREX;
	protected static String sql_organizacionUOREX;

	protected static String sql_hte_ter; // Identificador tercero
	protected static String sql_hte_nvr; // Version tercero
	protected static String sql_hte_nan;
	protected static String sql_hte_a1a;
	protected static String sql_hte_a2a;
	protected static String sql_hte_noc;
	protected static String sql_hte_p1a;
	protected static String sql_hte_p2a;
	protected static String sql_dnn_dom; // Domicilio tercero, no normalizado
	protected static String sql_dpo_dom; // Domicilio tercero, normalizado
	protected static String sql_dpo_dirSuelo;
	protected static String sql_dsu_codDirSuelo;
	protected static String sql_dsu_pais;
	protected static String sql_dsu_prov;
	protected static String sql_dsu_mun;
	protected static String sql_mun_nom;
	protected static String sql_dnn_pai;
	protected static String sql_mun_pai;
	protected static String sql_dnn_prv;
	protected static String sql_mun_prv;
	protected static String sql_dnn_mun;
	protected static String sql_mun_cod;
	protected static String sql_hte_tipoDoc;
	protected static String sql_tid_codDocum; // Codigo del documento
	protected static String sql_tid_descDocum; // Descripcion del documento
	protected static String sql_tid_persFisicJurid; // Tipo de persona: fisica o juridica.



	// Datos del usuario que registra
	protected static String sql_res_usuarioQRegistra;
	protected static String sql_res_dptoUsuarioQResgistra;
	protected static String sql_res_unidOrgUsuarioQRegistra;

	// Datos anotacion contestada
	protected static String sql_res_depContestada;
	protected static String sql_res_uorContestada;
	protected static String sql_res_tipContestada;
	protected static String sql_res_ejeContestada;
	protected static String sql_res_numContestada;

	//Datos de Observaciones del Registro
	protected static String sql_res_observaciones;

	// Datos de expedientes relacionados.
	protected static String sql_exr_codPro;
	protected static String sql_exr_depAnot;
	protected static String sql_exr_uorAnot;
	protected static String sql_exr_ejeAnot;
	protected static String sql_exr_numReg;
	protected static String sql_exr_tipoReg;
	protected static String sql_exr_numExp;

	// Nombres de procedimientos
	protected static String sql_pml_mun;
	protected static String sql_pml_cod;
	protected static String sql_pml_cmp;
	protected static String sql_pml_leng;
	protected static String sql_pml_valor;

	// Relaciones entre asientos
	protected static String sql_rel_uor;
	protected static String sql_rel_dep;
	protected static String sql_rel_tipoA;
	protected static String sql_rel_tipoB;
	protected static String sql_rel_ejercicioA;
	protected static String sql_rel_ejercicioB;
	protected static String sql_rel_numeroA;
	protected static String sql_rel_numeroB;

	protected static char caracterEscape='·';
	/**
	 * Construye un nuevo SelectListaDAO. Es protected, por lo que la unica manera de instanciar esta clase
	 * es usando el factory method <code>getInstance</code>
	 */
	protected AnotacionRegistroDAO() {
		//Queremos usar el fichero de configuracion techserver
		m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
		//Queremos tener acceso a los mensajes de error localizados
		m_ConfigError = ConfigServiceHelper.getConfig("error");
                m_ConfigRegistro = ConfigServiceHelper.getConfig("Registro");

		sql_ideTipoDoc = m_ConfigTechnical.getString("SQL.R_TDO.identificador");
		sql_codTipoDoc = m_ConfigTechnical.getString("SQL.R_TDO.codigo");
		sql_descTipoDoc = m_ConfigTechnical.getString("SQL.R_TDO.descripcion");
		sql_actTipoDoc = m_ConfigTechnical.getString("SQL.R_TDO.act");
		sql_ideTipoRemit = m_ConfigTechnical.getString("SQL.R_TPE.identificador");
		sql_codTipoRemit = m_ConfigTechnical.getString("SQL.R_TPE.codigo");
		sql_descTipoRemit = m_ConfigTechnical.getString("SQL.R_TPE.descripcion");
		sql_actTipoRemit = m_ConfigTechnical.getString("SQL.R_TPE.activo");
		sql_ideTipoTransp = m_ConfigTechnical.getString("SQL.R_TTR.identificador");
		sql_codTipoTransp = m_ConfigTechnical.getString("SQL.R_TTR.codigo");
		sql_descTipoTransp = m_ConfigTechnical.getString("SQL.R_TTR.descripcion");
		sql_actTipoTransp = m_ConfigTechnical.getString("SQL.R_TTR.activo");
		sql_ideTipoAct = m_ConfigTechnical.getString("SQL.R_ACT.identificador");
		sql_codTipoAct = m_ConfigTechnical.getString("SQL.R_ACT.codigo");
		sql_descTipoAct = m_ConfigTechnical.getString("SQL.R_ACT.descripcion");
		sql_actFechaDesde = m_ConfigTechnical.getString("SQL.R_ACT.fechaDesde");
		sql_actFechaHasta= m_ConfigTechnical.getString("SQL.R_ACT.fechaHasta");
		sql_nombreUOR = m_ConfigTechnical.getString("SQL.A_UOR.nombre");
		sql_codiVisibleUOR = m_ConfigTechnical.getString("SQL.A_UOR.codigoVisible");
		//sql_departamentoUOR = m_ConfigTechnical.getString("SQL.A_UOR.departamento");
		sql_codigoUOR = m_ConfigTechnical.getString("SQL.A_UOR.codigo");
		//sql_entidadUOR = m_ConfigTechnical.getString("SQL.A_UOR.entidad");
		//sql_organizacionUOR = m_ConfigTechnical.getString("SQL.A_UOR.organizacion");
		sql_codigoENT = m_ConfigTechnical.getString("SQL.A_ENT.codigo");
		sql_organizacionENT = m_ConfigTechnical.getString("SQL.A_ENT.organizacion");
		sql_nombreENT = m_ConfigTechnical.getString("SQL.A_ENT.nombre");
		sql_codigoORG = m_ConfigTechnical.getString("SQL.A_ORG.codigo");
		sql_nombreORG = m_ConfigTechnical.getString("SQL.A_ORG.descripcion");

		sql_codigoORGEX = m_ConfigTechnical.getString("SQL.A_ORGEX.codigo");
		sql_nombreORGEX = m_ConfigTechnical.getString("SQL.A_ORGEX.descripcion");

		sql_nombreUOREX = m_ConfigTechnical.getString("SQL.A_UOREX.descripcion");
		sql_codigoUOREX = m_ConfigTechnical.getString("SQL.A_UOREX.codigo");
		sql_organizacionUOREX = m_ConfigTechnical.getString("SQL.A_UOREX.organizacion");

		sql_ultNumAnotacion = m_ConfigTechnical.getString("SQL.R_NRE.txtNumRegistrado");
		sql_ejercicio = m_ConfigTechnical.getString("SQL.R_NRE.ejercicio");
		sql_nre_codDpto = m_ConfigTechnical.getString("SQL.R_NRE.codDepto");
		sql_nre_codUni = m_ConfigTechnical.getString("SQL.R_NRE.codUnidad");
		sql_nre_tipoReg = m_ConfigTechnical.getString("SQL.R_NRE.tipoReg");

		sql_codDptoAnotacion = m_ConfigTechnical.getString("SQL.R_RES.codDpto");
		sql_codUnidadAnotacion = m_ConfigTechnical.getString("SQL.R_RES.codUnidad");
		sql_tipoRegAnotacion = m_ConfigTechnical.getString("SQL.R_RES.tipoReg");
		sql_ejercicioAnotacion = m_ConfigTechnical.getString("SQL.R_RES.ejercicio");
		sql_numeroAnotacion = m_ConfigTechnical.getString("SQL.R_RES.numeroAnotacion");
		sql_fechaAnotacion = m_ConfigTechnical.getString("SQL.R_RES.fechaAnotacion");
		sql_fechaDocumento = m_ConfigTechnical.getString("SQL.R_RES.fechaDocumento");
		sql_asuntoAnotacion = m_ConfigTechnical.getString("SQL.R_RES.asunto");
		sql_tipoAnotacionDestino = m_ConfigTechnical.getString("SQL.R_RES.tipoAnotacionDestino");
		sql_tipoDocAnotacion = m_ConfigTechnical.getString("SQL.R_RES.tipoDoc");
		sql_tipoRemitenteAnotacion = m_ConfigTechnical.getString("SQL.R_RES.tipoRemitente");
		sql_codTerceroAnotacion = m_ConfigTechnical.getString("SQL.R_RES.codTercero");
		sql_domicTerceroAnotacion = m_ConfigTechnical.getString("SQL.R_RES.domicTercero");
		sql_numModifInteresado = m_ConfigTechnical.getString("SQL.R_RES.modifInteresado");
		sql_numTransp = m_ConfigTechnical.getString("SQL.R_RES.numTransporte");
		sql_tipoTransporte = m_ConfigTechnical.getString("SQL.R_RES.tipoTransporte");
		sql_entidadDestino = m_ConfigTechnical.getString("SQL.R_RES.entidadDestino");
		// tiruritata
		//sql_dptoDestino = m_ConfigTechnical.getString("SQL.R_RES.dptoDestino");
		sql_unidRegDestino = m_ConfigTechnical.getString("SQL.R_RES.unidRegDestino");
		sql_orgDestAnot = m_ConfigTechnical.getString("SQL.R_RES.orgDestAnot");
		// tiruritata
		//sql_departOrigDest = m_ConfigTechnical.getString("SQL.R_RES.departOrigDest");
		sql_unidOrigDest = m_ConfigTechnical.getString("SQL.R_RES.unidOrigDest");
		sql_orgOrigAnot = m_ConfigTechnical.getString("SQL.R_RES.orgOrigAnot");
		sql_departOrigAnot = m_ConfigTechnical.getString("SQL.R_RES.departOrigAnot");
		sql_unidOrigAnot = m_ConfigTechnical.getString("SQL.R_RES.unidOrigAnot");
		sql_entOrigAnot = m_ConfigTechnical.getString("SQL.R_RES.entOrigAnot");
		sql_tipoOrigDest = m_ConfigTechnical.getString("SQL.R_RES.tipoOrigDest");
		sql_numOrig = m_ConfigTechnical.getString("SQL.R_RES.numOrig");
		sql_ejeOrig = m_ConfigTechnical.getString("SQL.R_RES.ejeOrig");
		sql_actuacion = m_ConfigTechnical.getString("SQL.R_RES.actuacion");
		sql_estAnot = m_ConfigTechnical.getString("SQL.R_RES.estAnot");
		sql_diligencia = m_ConfigTechnical.getString("SQL.R_RES.diligencia");
		sql_entradaRelacionada = m_ConfigTechnical.getString("SQL.R_RES.entradaRelacionada");
		sql_autoridad = m_ConfigTechnical.getString ("SQL.R_RES.autoridad");
		sql_procedimiento = m_ConfigTechnical.getString ("SQL.R_RES.procedimiento");
	    sql_munProcedimiento = m_ConfigTechnical.getString ("SQL.R_RES.procmun");

		//Interesados
		sql_intecodUor= m_ConfigTechnical.getString ("SQL.R_EXT.codUor");
		sql_intetipo= m_ConfigTechnical.getString ("SQL.R_EXT.tipo");
		sql_inteano= m_ConfigTechnical.getString ("SQL.R_EXT.ano");
		sql_intenumero= m_ConfigTechnical.getString ("SQL.R_EXT.numero");
		sql_intecoddepartamento= m_ConfigTechnical.getString ("SQL.R_EXT.coddepartamento");
		sql_intecodTercero= m_ConfigTechnical.getString ("SQL.R_EXT.codTercero");
		sql_inteverTercero= m_ConfigTechnical.getString ("SQL.R_EXT.verTercero");
		sql_intecodDomicilio= m_ConfigTechnical.getString ("SQL.R_EXT.codDomicilio");
		sql_interolTercero= m_ConfigTechnical.getString ("SQL.R_EXT.rolTercero");


		sql_ejercicioRER = m_ConfigTechnical.getString("SQL.R_RER.ejercicio");
		sql_numeroRER = m_ConfigTechnical.getString("SQL.R_RER.numero");
		sql_fechaRER = m_ConfigTechnical.getString("SQL.R_RER.fecha");
		sql_tipoRER = m_ConfigTechnical.getString("SQL.R_RER.tipo");
		sql_codigoDepartamentoRER = m_ConfigTechnical.getString("SQL.R_RER.dep_cod");
		sql_codigoUnidadRER = m_ConfigTechnical.getString("SQL.R_RER.uor_cod");


		// Lista temas.
		sql_idTema= m_ConfigTechnical.getString("SQL.R_TEM.identificador");
		sql_codTema= m_ConfigTechnical.getString("SQL.R_TEM.codigo");
		sql_descTema= m_ConfigTechnical.getString("SQL.R_TEM.descripcion");
		// Temas
		sql_temasDepto = m_ConfigTechnical.getString("SQL.R_RET.codDpto");
		sql_temasUnid = m_ConfigTechnical.getString("SQL.R_RET.codUnidad");
		sql_temasEjerc = m_ConfigTechnical.getString("SQL.R_RET.ejercicio");
		sql_temasNum = m_ConfigTechnical.getString("SQL.R_RET.numeroAnotacion");
		sql_temasTipo = m_ConfigTechnical.getString("SQL.R_RET.tipoReg");
		sql_temasOrden = m_ConfigTechnical.getString("SQL.R_RET.orden");
		sql_temasIdTema = m_ConfigTechnical.getString("SQL.R_RET.idTema");
		// Docs
		sql_docsDepto = m_ConfigTechnical.getString("SQL.R_RED.codDpto");
		sql_docsUnid = m_ConfigTechnical.getString("SQL.R_RED.codUnidad");
		sql_docsEjerc = m_ConfigTechnical.getString("SQL.R_RED.ejercicio");
		sql_docsNum = m_ConfigTechnical.getString("SQL.R_RED.numeroAnotacion");
		sql_docsTipo = m_ConfigTechnical.getString("SQL.R_RED.tipoReg");
		sql_docsNombreDoc = m_ConfigTechnical.getString("SQL.R_RED.nombreDoc");
		sql_docsTipoDoc = m_ConfigTechnical.getString("SQL.R_RED.tipoDoc");
		sql_docsDoc = m_ConfigTechnical.getString("SQL.R_RED.doc");
                sql_docsFec=m_ConfigTechnical.getString("SQL.R_RED.fecDoc");
		// Buzon
		sql_buzonOrg  = m_ConfigTechnical.getString("SQL.R_BRE.organizacion");
		sql_buzonEnt  = m_ConfigTechnical.getString("SQL.R_BRE.entidad");
		sql_buzonDpto  = m_ConfigTechnical.getString("SQL.R_BRE.departamento");
		sql_buzonUnidReg  = m_ConfigTechnical.getString("SQL.R_BRE.unidadRegistro");
		sql_buzonTipo  = m_ConfigTechnical.getString("SQL.R_BRE.tipo");
		sql_buzonEjerc  = m_ConfigTechnical.getString("SQL.R_BRE.ejercicio");
		sql_buzonNum  = m_ConfigTechnical.getString("SQL.R_BRE.numero");
		sql_buzonFechEntrada  = m_ConfigTechnical.getString("SQL.R_BRE.fechaEntrada");
		sql_buzonFechDoc  = m_ConfigTechnical.getString("SQL.R_BRE.fechaDoc");
		sql_buzonAsunto  = m_ConfigTechnical.getString("SQL.R_BRE.asunto");
		sql_buzonOrgDest  = m_ConfigTechnical.getString("SQL.R_BRE.organizacionDestino");
		sql_buzonEntDest  = m_ConfigTechnical.getString("SQL.R_BRE.entidadDestino");
		sql_buzonDptoDest  = m_ConfigTechnical.getString("SQL.R_BRE.departamentoDestino");
		sql_buzonUnidDest  = m_ConfigTechnical.getString("SQL.R_BRE.unidadRegistroDestino");
		sql_buzonTipoTransp  = m_ConfigTechnical.getString("SQL.R_BRE.idTipoTransporte");
		sql_buzonNumTransp  = m_ConfigTechnical.getString("SQL.R_BRE.numTransporte");
		sql_buzonTipoDoc  = m_ConfigTechnical.getString("SQL.R_BRE.idTipoDocumento");
		sql_buzonTipoRemit  = m_ConfigTechnical.getString("SQL.R_BRE.idTipoRemitente");
		sql_buzonActuacion  = m_ConfigTechnical.getString("SQL.R_BRE.idActuacion");
		sql_buzonIdTercero  = m_ConfigTechnical.getString("SQL.R_BRE.idTercero");
		sql_buzonDomTercero  = m_ConfigTechnical.getString("SQL.R_BRE.idDomicilio");
		sql_buzonNumModifInfTercero  = m_ConfigTechnical.getString("SQL.R_BRE.numModInfTercero");

		// Datos para el listado: terceros
		sql_hte_ter = m_ConfigTechnical.getString("SQL.T_HTE.identificador");
		sql_hte_nvr = m_ConfigTechnical.getString("SQL.T_HTE.version");
		sql_dnn_dom = m_ConfigTechnical.getString("SQL.T_DNN.idDomicilio");
		sql_hte_nan = m_ConfigTechnical.getString("SQL.T_HTE.nombre");
		sql_hte_a1a = m_ConfigTechnical.getString("SQL.T_HTE.apellido1");
		sql_hte_a2a = m_ConfigTechnical.getString("SQL.T_HTE.apellido2");
		sql_hte_p1a = m_ConfigTechnical.getString("SQL.T_HTE.partApellido1");
		sql_hte_p2a = m_ConfigTechnical.getString("SQL.T_HTE.partApellido2");
		sql_hte_noc = m_ConfigTechnical.getString("SQL.T_HTE.nombreCompleto");
		sql_mun_nom = m_ConfigTechnical.getString("SQL.T_MUN.nombre");
		sql_dnn_pai = m_ConfigTechnical.getString("SQL.T_DNN.idPaisD");
		sql_mun_pai = m_ConfigTechnical.getString("SQL.T_MUN.idPais");
		sql_dnn_prv = m_ConfigTechnical.getString("SQL.T_DNN.idProvinciaD");
		sql_mun_prv = m_ConfigTechnical.getString("SQL.T_MUN.idProvincia");
		sql_dnn_mun = m_ConfigTechnical.getString("SQL.T_DNN.idMunicipioD");
		sql_mun_cod = m_ConfigTechnical.getString("SQL.T_MUN.idMunicipio");
		sql_hte_tipoDoc = m_ConfigTechnical.getString("SQL.T_HTE.tipoDocumento");
		sql_tid_codDocum = m_ConfigTechnical.getString("SQL.T_TID.codigo");
		sql_tid_descDocum = m_ConfigTechnical.getString("SQL.T_TID.nombre");
		sql_tid_persFisicJurid = m_ConfigTechnical.getString("SQL.T_TID.fisJur");
		sql_res_usuarioQRegistra = m_ConfigTechnical.getString("SQL.R_RES.usuarioQRegistra");
		sql_res_dptoUsuarioQResgistra = m_ConfigTechnical.getString("SQL.R_RES.dptoUsuarioQRegistra");
		sql_res_unidOrgUsuarioQRegistra = m_ConfigTechnical.getString("SQL.R_RES.unidOrgUsuarioQRegistra");
		sql_dpo_dom = m_ConfigTechnical.getString("SQL.T_DPO.domicilio"); // Domicilio tercero, normalizado
		sql_dpo_dirSuelo = m_ConfigTechnical.getString("SQL.T_DPO.suelo");
		sql_dsu_codDirSuelo = m_ConfigTechnical.getString("SQL.T_DSU.identificador");
		sql_dsu_pais= m_ConfigTechnical.getString("SQL.T_DSU.pais");
		sql_dsu_prov= m_ConfigTechnical.getString("SQL.T_DSU.provincia");
		sql_dsu_mun = m_ConfigTechnical.getString("SQL.T_DSU.municipio");
		sql_res_depContestada = m_ConfigTechnical.getString("SQL.R_RES.codDptoContestada"); // Anotacion contestada
		sql_res_uorContestada = m_ConfigTechnical.getString("SQL.R_RES.codUnidadContestada");
		sql_res_tipContestada = m_ConfigTechnical.getString("SQL.R_RES.tipoRegContestada");
		sql_res_ejeContestada = m_ConfigTechnical.getString("SQL.R_RES.ejercicioContestada");
		sql_res_numContestada = m_ConfigTechnical.getString("SQL.R_RES.numeroAnotacionContestada");
		sql_res_observaciones = m_ConfigTechnical.getString("SQL.R_RES.observaciones");

		// Para poder hacer busquedas por expedientes relacionados.
		sql_exr_codPro = m_ConfigTechnical.getString("SQL.E_EXR.codProcedimiento");
		sql_exr_depAnot = m_ConfigTechnical.getString("SQL.E_EXR.departamentoAnotacion");
		sql_exr_uorAnot = m_ConfigTechnical.getString("SQL.E_EXR.unidadAnotacion");
		sql_exr_ejeAnot = m_ConfigTechnical.getString("SQL.E_EXR.ejercicioAnotacion");
		sql_exr_numReg = m_ConfigTechnical.getString("SQL.E_EXR.numeroRegistro");
		sql_exr_tipoReg = m_ConfigTechnical.getString("SQL.E_EXR.tipoRegistro");
		sql_exr_numExp = m_ConfigTechnical.getString("SQL.E_EXR.numero");

		// Para obtener los nombres de los procedimientos
		sql_pml_mun = m_ConfigTechnical.getString("SQL.E_PML.codMunicipio");
		sql_pml_cod = m_ConfigTechnical.getString("SQL.E_PML.codProcedimiento");
		sql_pml_cmp = m_ConfigTechnical.getString("SQL.E_PML.codCampoML");
		sql_pml_leng = m_ConfigTechnical.getString("SQL.E_PML.idioma");
		sql_pml_valor = m_ConfigTechnical.getString("SQL.E_PML.valor");

		// Relaciones entre asientos
		sql_rel_uor = m_ConfigTechnical.getString("SQL.R_REL.uor");
	    sql_rel_dep = m_ConfigTechnical.getString("SQL.R_REL.dep");
	    sql_rel_tipoA = m_ConfigTechnical.getString("SQL.R_REL.tipoA");
	    sql_rel_tipoB = m_ConfigTechnical.getString("SQL.R_REL.tipoB");
	    sql_rel_ejercicioA = m_ConfigTechnical.getString("SQL.R_REL.ejeA");
	    sql_rel_ejercicioB = m_ConfigTechnical.getString("SQL.R_REL.ejeB");
	    sql_rel_numeroA = m_ConfigTechnical.getString("SQL.R_REL.numA");
	    sql_rel_numeroB = m_ConfigTechnical.getString("SQL.R_REL.numB");
	}

	/**
	 * Devuelve el ultimo numero de entrada que existe para la combinacion de departamento, unidad organica, tipo de
	 * registro y ejercicio, ademas de introducir el siguiente en el objeto {@link RegistroValueObject} pasado por 
	 * parametro.
	 * 
	 * @param c
	 * @param regVO
	 * @return
	 * @throws AnotacionRegistroException
	 * @throws TechnicalException 
	 */
    private Long getNumeroEntrada(Connection c, RegistroValueObject regVO)
            throws AnotacionRegistroException, TechnicalException {
		Long numeroAnotacion = null;
		AnotacionRegistroVO anotacion = null;
		
        try {
			// Necesitamos convertir el RegistroValueObject a AnotacionRegistroVO de la libreria
			anotacion = new AnotacionRegistroVO(regVO.getIdentDepart(), regVO.getUnidadOrgan(), regVO.getTipoReg(), regVO.getAnoReg());
			numeroAnotacion = GestionSecuenciacionNumerosAnotacion.getNumeroEntrada(anotacion, c);
			regVO.setNumReg(numeroAnotacion);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.insercionDatosRegistro"), ex);
        } 
		
		return numeroAnotacion;
    }

    private String getUltimaFechaAlta(AdaptadorSQLBD oad, Connection c, RegistroValueObject regVO)
            throws AnotacionRegistroException, TechnicalException {
        //RECOGE LA FECHA DE ANOTACION ES DECIR LA FECHA DE PRESENTACION
        String sql = "SELECT " + oad.convertir(oad.funcionMatematica(AdaptadorSQLBD.FUNCIONMATEMATICA_MAX,
                new String[]{sql_fechaAnotacion}), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                " AS FECHA  FROM R_RES ";
        sql += " WHERE " + sql_codDptoAnotacion + "=" + regVO.getIdentDepart();
        sql += " AND " + sql_codUnidadAnotacion + "=" + regVO.getUnidadOrgan();
        sql += " AND " + sql_tipoRegAnotacion + "='" + regVO.getTipoReg() + "' ";
        sql += " AND " + sql_ejercicioAnotacion + "=" + regVO.getAnoReg();
        // sql += " AND (TO_DATE(TO_CHAR(" + sql_fechaAnotacion + ",'DD/MM/YYYY'),'DD/MM/YYYY') > TO_DATE('"+ regVO.getFecEntrada().substring(0,10) + "','DD/MM/YYYY'))";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO (getUltimaFechaAlta): Sentencia SQL:" + sql);
            }
            ps = c.prepareStatement(sql);
            rs = ps.executeQuery();

            String ultimaFechaAlta = null;
            if (rs.next()) {
                ultimaFechaAlta = rs.getString("FECHA");
            } else {
                ultimaFechaAlta = null;
            }

            return ultimaFechaAlta;
        } catch (SQLException ex) {
            ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getUltimaFechaAlta.sql"), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /**
     * Prepara la sentencia SQL que se debe ejecutar para modificar una determinada anotación de registro
     * @param registro: Datos del registro actuales
     * @param asuntoAnteriorBaja: Si está a true indica que la anotación anteriormente a la modificación tenía un código de asunto codificado asignado
     *                            dado de baja
     * @param oad: Objeto de la clase AdaptadorSQLBD necesario para realizar la consulta
     * @return Consulta SQL
     */
    private String construirConsultaModificacion(RegistroValueObject registro,boolean asuntoAnteriorBaja, AdaptadorSQLBD oad) {
        m_Log.debug("Dentro del registrar_actualizacion_aceptada de modify en el DAO");

        int codigoDepartamento = registro.getIdentDepart();
        //m_Log.debug("el codigo de departamento es : *********" + codigoDepartamento);
        int codigoUnidad = registro.getUnidadOrgan();
        String tipoRegistro = registro.getTipoReg();
        int ejercicio = registro.getAnoReg();
        Long numeroRegistro = registro.getNumReg();
        String fechaAnotacion = registro.getFecEntrada();
         String fechaDocu = registro.getFechaDocu();
        m_Log.debug("la fecha de anotacion en el DAO es : " + fechaAnotacion);
        String fechaDocumento = registro.getFecHoraDoc();
        String asunto = registro.getAsunto();
        if (asunto == null) {
            asunto = "";
        }else
        {
            asunto=StringUtils.escapeCP1252(asunto, "ISO-8859-1");
        }

        //m_Log.debug("el asunto en el DAO es : ***********" + asunto);
        int tipoAnotacionDestino = registro.getTipoAnot();
        int tipoDocAnotacion = registro.getIdTipoDoc();
        m_Log.debug("el tipo de documento en el DAO es: " + tipoDocAnotacion);
        int tipoRemitenteAnotacion = registro.getIdTipoPers();
        int codTerceroAnotacion = registro.getCodInter();
        int domicTerceroAnotacion = registro.getDomicInter();
        int numModifInteresado = registro.getNumModInfInt();
        String numTransp = registro.getNumTransporte();
        int tipoTransporte = registro.getIdTransporte();
        String entidadDestino = registro.getOrgEntDest();
        //String dptoDestino = registro.getIdDepDestino();
        String unidRegDestino = registro.getIdUndRegDest(); 
        String orgDestAnot = registro.getOrgDestino();
        String unidOrigDest = registro.getIdUndTramitad();
        m_Log.debug("codigo unidad organica en el DAO es: " + unidOrigDest);
        int departOrigAnot = registro.getIdDepOrigen();
        String orgOrigAnot = registro.getOrganizacionOrigen();
        String unidOrigAnot = registro.getIdUndRegOrigen();
        int entOrigAnot = registro.getOrgEntOrigen();
        String entradaRel = registro.getEjeEntradaRel();
        int actuacion = registro.getIdActuacion();
        int estAnot = registro.getEstAnotacion();
        String diligencia = registro.getDilAnulacion();
        String obser = registro.getObservaciones();
        obser=StringUtils.escapeCP1252(obser, "ISO-8859-1");
        

        m_Log.debug("antes del sql del modify del DAO");

        String sql = "UPDATE R_RES SET ";
        if (fechaAnotacion.length() > 10) {
            sql += " RES_FEDOC= ";
            if (fechaDocu == null ||"".equals(fechaDocu) ) {
                sql += "null ,";
            }else{
                sql+= oad.convertir("'" + fechaDocu + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,
                        "DD/MM/YYYY") + ",";
            }
            sql += sql_fechaAnotacion + "= " +
                    oad.convertir("'" + fechaAnotacion + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,
                    "DD/MM/YYYY HH24:MI:SS") + ",";
        } else {
            sql += sql_fechaAnotacion + "=" + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                    new String[]{oad.convertir("'" + fechaAnotacion + "'",
                AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY HH24:MI:SS"), oad.convertir(
                oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null),
                AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS")
            }) + ",";
        }

        sql += sql_asuntoAnotacion + "='" + TransformacionAtributoSelect.replace(asunto, "'", "''") +
                "'," + sql_tipoAnotacionDestino + "=" + tipoAnotacionDestino +
                "," + sql_tipoDocAnotacion + "=";

        if (tipoDocAnotacion == 0) {
            sql += "null";
        } else {
            sql += tipoDocAnotacion;
        }

        sql +=  "," + sql_tipoRemitenteAnotacion + "=";

        if (tipoRemitenteAnotacion == 0) {
            sql += "null";
        } else {
            sql += tipoRemitenteAnotacion;
        }
        sql += "," + sql_res_observaciones + "='" + TransformacionAtributoSelect.replace(obser, "'", "''") + "'";
        sql += "," + sql_codTerceroAnotacion + "=" + codTerceroAnotacion +
                "," + sql_domicTerceroAnotacion + "=" + domicTerceroAnotacion +
                "," + sql_numModifInteresado + "=" + numModifInteresado +
                "," + sql_numTransp + "=";

        if (numTransp == null) {
            sql += null;
        } else {
            sql += "'" + numTransp + "'";
        }
        sql += "," + sql_tipoTransporte + "=";
        if (tipoTransporte != 0) {
            sql += tipoTransporte;
        } else {
            sql += " null ";
        }
        // Destino

        if (registro.getTipoAnot() == 0 || registro.getTipoAnot() == 2) {

            //sql += "," + sql_departOrigDest + "=" + departOrigDest;
            sql += "," + sql_unidOrigDest + "=" + unidOrigDest;
            sql += "," + sql_entidadDestino + "=null";

            //sql += "," + sql_dptoDestino + "=null";
            sql += "," + sql_unidRegDestino + "=null";
            sql += "," + sql_orgDestAnot + "=null";
        } else {

            //sql += "," + sql_departOrigDest + "= null";
            // NOTA: unidOrigDest es en realidad el codigo de unidad organica
            if (unidOrigDest != null && !unidOrigDest.equals("")) {
                sql += "," + sql_unidOrigDest + "=" + unidOrigDest;
            } else {
                sql += "," + sql_unidOrigDest + "=null";
            }
            sql += "," + sql_entidadDestino + "=";
            if (entidadDestino != null) {
                if (!"".equals(entidadDestino.trim())) {
                    sql += entidadDestino;
                } else {
                    sql += " null ";
                }
            } else {
                sql += " null ";
            }
            /*
            sql += "," + sql_dptoDestino + "=";
            if (dptoDestino != null)
            if (!"".equals(dptoDestino.trim()))
            sql += dptoDestino ;
            else
            sql += " null ";
            else
            sql += " null ";*/
            sql += "," + sql_unidRegDestino + "=";
            if (unidRegDestino != null) {
                if (!"".equals(unidRegDestino.trim())) {
                    sql += unidRegDestino;
                } else {
                    sql += " null ";
                }
            } else {
                sql += " null ";
            }
            sql += "," + sql_orgDestAnot + "=";
            if (orgDestAnot != null) {
                if (!"".equals(orgDestAnot.trim())) {
                    sql += orgDestAnot;
                } else {
                    sql += " null ";
                }
            } else {
                sql += " null ";
            }
        }
        sql += "," + sql_departOrigAnot + "=";
        if (departOrigAnot != 0) {
            sql += departOrigAnot;
        } else {
            sql += " null ";
        }
        sql += "," + sql_orgOrigAnot + "=";
        if (orgOrigAnot != null && !orgOrigAnot.equals("")) {
            sql += orgOrigAnot;
        } else {
            sql += " null ";
        }
        sql += "," + sql_unidOrigAnot + "=";
        if (unidOrigAnot != null && !unidOrigAnot.equals("")) {
            sql += unidOrigAnot;
        } else {
            sql += " null ";
        }
        sql += "," + sql_entOrigAnot + "=";
        if (entOrigAnot != 0) {
            sql += entOrigAnot;
        } else {
            sql += " null ";
        }
        sql += "," + sql_tipoOrigDest + "=null";
        sql += "," + sql_numOrig + "=null";
        sql += "," + sql_ejeOrig + "=null";
        sql += "," + sql_entradaRelacionada + "=";
        if (entradaRel != null || !"".equals(entradaRel)) {
            sql += "'" + entradaRel + "'";
        } else {
            sql += " null ";
        }
        sql += "," + sql_actuacion + "=";
        if (actuacion != 0) {
            sql += actuacion;
        } else {
            sql += " null ";
        }
        sql += "," + sql_estAnot + "=" + estAnot;
        sql += "," + sql_diligencia + "=";
        if (diligencia == null) {
            sql += "null";
        } else {
            sql += "'" + diligencia + "'";
        }
        // --> Datos del que registra.
        sql += "," + sql_res_usuarioQRegistra + " = " + registro.getUsuarioQRegistra();
        sql += "," + sql_res_dptoUsuarioQResgistra + "=" + registro.getDptoUsuarioQRegistra();
        sql += "," + sql_res_unidOrgUsuarioQRegistra + "=" + registro.getUnidOrgUsuarioQRegistra();
        sql += "," + sql_autoridad + "=";
        if ((registro.getAutoridad().equals("")) || (registro.getAutoridad() == null)) {
            sql += "null";
        } else {
            sql += "'" + registro.getAutoridad() + "'";
        }

        // Procedimiento
        sql += "," + sql_procedimiento + "='" + registro.getCodProcedimiento() + "'";
        if ("".equals(registro.getCodProcedimiento())) {
            sql += "," + sql_munProcedimiento + "=null";
        } else {
            sql += "," + sql_munProcedimiento + "=" + registro.getMunProcedimiento();
        }
        // Asunto codificado
        String codAsunto = registro.getCodAsunto();
        
        if (!asuntoAnteriorBaja && (codAsunto == null || codAsunto.equals(""))) {
            // Si no hay asunto y además anteriormente la anotación no tenía asignada un código de asunto dado de baja, se pone a null
            sql += ", ASUNTO = null";            
        } else
        if(codAsunto!=null && !"".equals(codAsunto)){
            sql += ", ASUNTO ='" + codAsunto + "'";
        }
        
        // Fin digitalizacion
        sql += ", FIN_DIGITALIZACION = " + JdbcOperations.convertBooleanToIntegerForDB(registro.isFinDigitalizacion());
                
        sql += " WHERE " + sql_codDptoAnotacion + "=" + codigoDepartamento + " AND " +
                sql_codUnidadAnotacion + "=" + codigoUnidad + " AND " +
                sql_tipoRegAnotacion + "='" + tipoRegistro + "' AND " +
                sql_ejercicioAnotacion + "=" + ejercicio + " AND " +
                sql_numeroAnotacion + "=" + numeroRegistro;

        return sql;
    }

    /* Consigue la fecha anotada para este asiento en BD, (la que viene en el VO es la que
     * introdujo el usuario) */
    private String getFechaOriginal(AdaptadorSQLBD oad, Connection c, RegistroValueObject registro)
            throws AnotacionRegistroException, TechnicalException {

        PreparedStatement ps = null;
        ResultSet rs = null;

        String fechaOriginal = null;
        int codigoDepartamento = registro.getIdentDepart();
        int codigoUnidad = registro.getUnidadOrgan();
        String tipoRegistro = registro.getTipoReg();
        int ejercicio = registro.getAnoReg();
        Long numeroRegistro = registro.getNumReg();

        String sql = "SELECT " + oad.convertir(sql_fechaAnotacion, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                " AS FECHA  FROM R_RES WHERE ";
        sql += sql_codDptoAnotacion + "=" + codigoDepartamento + " AND " +
                sql_codUnidadAnotacion + "=" + codigoUnidad + " AND " +
                sql_tipoRegAnotacion + "='" + tipoRegistro + "' AND " +
                sql_ejercicioAnotacion + "=" + ejercicio + " AND " +
                sql_numeroAnotacion + "=" + numeroRegistro;

        try {
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO (getFechaOriginal): Sentencia SQL:" + sql);
            }

            ps = c.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                fechaOriginal = rs.getString("FECHA");
                m_Log.debug("FECHA ORIGINAL: " + fechaOriginal);
            } else {
                fechaOriginal = null;
            }

            return fechaOriginal;

        } catch (SQLException ex) {
            ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getFechaOriginal.sql"), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /* Busca la fecha inmediatamente anterior a la fecha dada entre las fechas/horas de entrada de los asientos
     * Devuelve null si no hay ninguna fecha anterior */
    private String getUltimaFechaAnterior(AdaptadorSQLBD oad, Connection c, RegistroValueObject registro, String fechaOriginal)
            throws AnotacionRegistroException, TechnicalException {

        String tipoRegistro = registro.getTipoReg();
        String sql = "SELECT " + oad.convertir(oad.funcionMatematica(AdaptadorSQLBD.FUNCIONMATEMATICA_MAX,
                new String[]{sql_fechaAnotacion}), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                " AS FECHA  FROM R_RES WHERE ";
        sql += sql_tipoRegAnotacion + "='" + tipoRegistro + "' AND " +
                sql_codDptoAnotacion + "=" + registro.getIdentDepart() + " AND " +
                sql_codUnidadAnotacion + "=" + registro.getUnidadOrgan() +
                " AND " + sql_fechaAnotacion + " < " +
                oad.convertir("'" + fechaOriginal + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY HH24:MI:SS");

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO (getUltimaFechaAnterior): Sentencia SQL:" + sql);
            }

            ps = c.prepareStatement(sql);
            rs = ps.executeQuery();
            String ultimaFechaAnterior = null;
            if (rs.next()) {
                ultimaFechaAnterior = rs.getString("FECHA");
            } else {
                ultimaFechaAnterior = null;
            }
            return ultimaFechaAnterior;

        } catch (SQLException ex) {
            ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getUltimaFechaAnterior.sql"), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /* Busca la fecha inmediatamente posterior a la fecha dada entre las fechas/horas de entrada de los asientos
     * Devuelve null si no hay ninguna fecha posterior */
    private String getPrimeraFechaPosterior(AdaptadorSQLBD oad, Connection c, RegistroValueObject registro, String fechaOriginal)
            throws AnotacionRegistroException, TechnicalException {

        String tipoRegistro = registro.getTipoReg();
        String sql = "SELECT " + oad.convertir(oad.funcionMatematica(AdaptadorSQLBD.FUNCIONMATEMATICA_MIN,
                new String[]{sql_fechaAnotacion}), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                " AS FECHA  FROM R_RES WHERE ";
        sql += sql_tipoRegAnotacion + "='" + tipoRegistro + "' AND " +
                sql_codDptoAnotacion + "=" + registro.getIdentDepart() + " AND " +
                sql_codUnidadAnotacion + "=" + registro.getUnidadOrgan() +
                " AND " + sql_fechaAnotacion + " > " +
                oad.convertir("'" + fechaOriginal + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY HH24:MI:SS");

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO (getPrimeraFechaPosterior): Sentencia SQL:" + sql);
            }

            ps = c.prepareStatement(sql);
            rs = ps.executeQuery();

            String primeraFechaPosterior = null;
            if (rs.next()) {
                primeraFechaPosterior = rs.getString("FECHA");
            } else {
                primeraFechaPosterior = null;
            }
            return primeraFechaPosterior;

        } catch (SQLException ex) {
            ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getPrimeraFechaPosterior.sql"), ex);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /* Función para dar de alta un registro... pero no solo inserta la anotación.
     * Usa insertarAnotacion para insertar los datos principales, y otras funciones para insertar
     * otros datos (documentos, temas...) */
    public RegistroValueObject insertRegistroValueObject(Connection con,RegistroValueObject regESVO_BD, String[] params)
            throws AbrirCerrarRegistroException, AnotacionRegistroException, TechnicalException {

        m_Log.debug("Dentro de AnotacionRegistroDAO.insertRegistroValueObject");      

        // Obtener los IDEs de las listas.
        String c = getIDELista(regESVO_BD.getCodTipoDoc(), "R_TDO", sql_codTipoDoc, sql_ideTipoDoc, params);
        if (c != null) {
            regESVO_BD.setIdTipoDoc(Integer.parseInt(c));
        }

        if (regESVO_BD.getCodTipoTransp() != null) {
            c = getIDELista(regESVO_BD.getCodTipoTransp(), "R_TTR", sql_codTipoTransp, sql_ideTipoTransp, params);
            if (c != null) {
                regESVO_BD.setIdTransporte(Integer.parseInt(c));
            }
        }
        c = getIDELista(regESVO_BD.getCodTipoRemit(), "R_TPE", sql_codTipoRemit, sql_ideTipoRemit, params);
        if (c != null) {
            regESVO_BD.setIdTipoPers(Integer.parseInt(c));
        }
        if (regESVO_BD.getCodAct() != null) {
            c = getIDELista(regESVO_BD.getCodAct(), "R_ACT", sql_codTipoAct, sql_ideTipoAct, params);
            if (c != null) {
                regESVO_BD.setIdActuacion(Integer.parseInt(c));
            }
        }

        // Búsqueda de los ids de los temas asignados. Sin transaccion.
        Vector idsTemasAsignados = new Vector();
        Vector temas = regESVO_BD.getListaTemasAsignados();
        String id;
        if (temas != null) {
            for (int i = 0; i < temas.size(); i++) {
                c = (String) temas.elementAt(i);
                id = getIDELista(c, "R_TEM", sql_codTema, sql_idTema, params);
                if (id != null) {
                    idsTemasAsignados.addElement(id);
                }
            }
        }

        // #278739: Obtener entrada relacionada
        String valorEntRel = "";
        String anoEntRel = (regESVO_BD.getEjeEntradaRel() != null) ? regESVO_BD.getEjeEntradaRel().trim() : "";
        String numEntRel = (regESVO_BD.getNumEntradaRel() != null) ? regESVO_BD.getNumEntradaRel().trim() : "";
        if (!anoEntRel.equals("") && !numEntRel.equals("")) {
            valorEntRel = anoEntRel + "/" + numEntRel;
        } else if (anoEntRel.equals("") && !numEntRel.equals("")) {
            valorEntRel = numEntRel;
        } else if (!anoEntRel.equals("") && numEntRel.equals("")) {
            valorEntRel = anoEntRel;
        }
        regESVO_BD.setEjeEntradaRel(valorEntRel);
        regESVO_BD.setNumEntradaRel("");

        
        Vector docs = regESVO_BD.getListaDocsAsignados();

        //Connection con = null;
        String confirmarAlta = regESVO_BD.getRespOpcion();
        AdaptadorSQLBD oad = null;

        PreparedStatement ps = null, psUpdate = null;
        ResultSet rs = null;

        try {

            oad = new AdaptadorSQLBD(params);

            // Comprobar si el registro está abierto.
            m_Log.debug("conseguir fecha de entrada a partir de : " + regESVO_BD.getFecEntrada());
            Date fEntr = Fecha.obtenerDateCompleto2(regESVO_BD.getFecEntrada());
            Date fEntrSinhora=Fecha.obtenerDate(regESVO_BD.getFecEntrada());
            m_Log.debug("Fecha de entrada : " + fEntr);

            Date fRegCerrado = RegistroAperturaCierreManager.getInstance().getFechaRegistroCerrado(con, regESVO_BD);
            Calendar calRegCerrado = null;
            if (fRegCerrado != null) {
                calRegCerrado = Calendar.getInstance();
                calRegCerrado.setTime(fRegCerrado);
                calRegCerrado.set(Calendar.HOUR_OF_DAY, 23);
                calRegCerrado.set(Calendar.MINUTE, 59);
                calRegCerrado.set(Calendar.SECOND, 59);
                calRegCerrado.set(Calendar.MILLISECOND, 999);
            }

            if (fRegCerrado == null || fEntr.compareTo(calRegCerrado.getTime()) > 0) {

                regESVO_BD.setRespOpcion("registrar_alta_entrada_aceptada");
                if ("alta_sin_confirmar".equals(confirmarAlta)) {
                    // NOS DEVUELVE LA ULTIMA FECHA DE PRESENTACION NO DE GRAVACION
                    String strUltimaFechaAlta = getUltimaFechaAlta(oad, con, regESVO_BD);
                    Date ultimaFechaAlta = null;
                    
                    if (m_ConfigRegistro.getString("restriccion_fecha_presentacion").equalsIgnoreCase("SI")){
                    if ((strUltimaFechaAlta != null) && !(strUltimaFechaAlta.equals(""))) {
                        ultimaFechaAlta = Fecha.obtenerDateCompleto2(strUltimaFechaAlta);
                        Date ultimaFechaAltaSinhora=Fecha.obtenerDate(strUltimaFechaAlta);

                       m_Log.debug("Ultima fecha de entrada : " + fEntr);
                        if (fEntrSinhora.compareTo(ultimaFechaAltaSinhora) < 0) {
                            m_Log.debug("ALERTAR USUARIO la fecha de entrada es anterior a la ultima fecha anotada : " + fEntrSinhora + " < " + ultimaFechaAltaSinhora);
                            regESVO_BD.setRespOpcion("registrar_alta_sin_confirmar");
                        }
                    }
                }
                }

                /* Enlace con SGE.
                 * Fecha: 21/07/2003. */
                boolean existeExpediente = false;
                boolean procMalRelacionado = false;
                TramitacionValueObject tvo = new TramitacionValueObject();

                if ("registrar_alta_entrada_aceptada".equals(regESVO_BD.getRespOpcion())) {

                    if (regESVO_BD.getNumExpediente() != null) {
                        if (!"".equals(regESVO_BD.getNumExpediente().trim())) {
                            ExpedienteRelacionadoVO expediente = new ExpedienteRelacionadoVO();
                            expediente.setNumExpedienteRelacionado(regESVO_BD.getNumExpediente());
                            expediente.setCodProcedimiento(regESVO_BD.getCodProcedimiento());
                            expediente.setCodigoOrganizacion(Integer.toString(regESVO_BD.getIdOrganizacion()));
                            expediente.setCodigoUsuario(regESVO_BD.getUsuarioQRegistra());
                            expediente.setParams(params);
                            expediente.setCerrarConexionFlexia(false);
                            expediente.setConnection(con);
                            
                            PluginExpedientesRelacionados plugin = PluginExpedientesRelacionadosFactoria.getImplClass(Integer.toString(regESVO_BD.getIdOrganizacion()));
                            int r = plugin.existeExpediente(expediente);
                            m_Log.debug("el resultado de localizaExpediente es : " + r);
                            //Solo se le pide que el expediente exista
                            existeExpediente = (r!=4); //
                            procMalRelacionado = (r == 5);
  
                            tvo.setNumeroExpediente(regESVO_BD.getNumExpediente());
                            tvo.setCodProcedimiento(regESVO_BD.getCodProcedimiento());                          

                            if (!existeExpediente && !procMalRelacionado) {
                                regESVO_BD.setRespOpcion("no_existe_expediente");
                            } else if (procMalRelacionado) {
                                regESVO_BD.setRespOpcion("proc_mal_relacionado");
                            } else {
                                regESVO_BD.setRespOpcion("registrar_alta_entrada_aceptada");
                            }
                        }
                    }
                }
                m_Log.debug("la respuesta en el DAO es 1 : " + regESVO_BD.getRespOpcion());
                /* Fin Enlace con SGE. */

                if ("registrar_alta_entrada_aceptada".equals(regESVO_BD.getRespOpcion())) {   // Seguimos con el alta.
                    // Coger número de entrada.
                    Long numeroAnotacion = getNumeroEntrada(con, regESVO_BD);
                    
                    if (numeroAnotacion != null) { // Si falla se recibe una excepción.
						
                        if (insertarAnotacion(oad, con, regESVO_BD) > 0) {
                            insertarTemasAsignados(con, regESVO_BD, idsTemasAsignados);
                            insertarInteresados(con, regESVO_BD);
                            if(regESVO_BD.getRelaciones()!=null){
                            	insertarRelaciones(con, regESVO_BD);
							}

                            // Insertar alta (normal o de finalización) en el historico de movimientos
							insertarAltaHistorico(regESVO_BD, con, params);
							
                            regESVO_BD.setRespOpcion("registrar_alta_entrada_aceptada");
                            if (existeExpediente) {
                                if (tvo.getNumeroExpediente() != null) {                                    
                                    ExpedienteRelacionadoVO expediente = new ExpedienteRelacionadoVO();
                                    expediente.setCodigoOrganizacion(Integer.toString(regESVO_BD.getIdOrganizacion()));
                                    expediente.setCodigoDepartamento(Integer.toString(regESVO_BD.getIdentDepart()));
                                    expediente.setCodigoUnidadRegistro(Integer.toString(regESVO_BD.getUnidadOrgan()));
                                    expediente.setTipoRegistro(regESVO_BD.getTipoReg());
                                    expediente.setEjercicioRegistro(Integer.toString(regESVO_BD.getAnoReg()));
                                    expediente.setNumeroRegistro(Long.toString(regESVO_BD.getNumReg()));
                                    expediente.setCodigoTercero(Integer.toString(regESVO_BD.getCodInter()));
                                    expediente.setVersionTercero(Integer.toString(regESVO_BD.getNumModInfInt()));
                                    expediente.setCodigoDomicilio(Integer.toString(regESVO_BD.getDomicInter()));
                                    expediente.setCodProcedimiento(regESVO_BD.getCodProcedimiento());
                                    expediente.setNumExpedienteRelacionado(tvo.getNumeroExpediente());
                                    expediente.setNumExpedienteAntiguo(tvo.getNumeroExpedienteAntiguo());
                                    expediente.setConnection(con);

                                    PluginExpedientesRelacionados plugin = PluginExpedientesRelacionadosFactoria.getImplClass(Integer.toString(regESVO_BD.getIdOrganizacion()));                                 
                                    int r = plugin.insertarExpedienteRelacionado(expediente);
                                    if (r == 1) {
                                        regESVO_BD.setRespOpcion("registrar_alta_entrada_aceptada");
                                    } else {
                                        regESVO_BD.setRespOpcion("registrar_alta_entrada_denegada");
                                    }                                    
                                  
                                }
                            }

                            /* Cuando el registro de salida se le asocia un expediente se le pone
                             * automaticamente aceptada a esa anotación */
                            if ("S".equals(regESVO_BD.getTipoReg()) && existeExpediente) {
                                aceptarAnotacion(con, regESVO_BD);
                            }

                            /* Fin Enlace con SGE. */

                            if ("registrar_alta_entrada_aceptada".equals(regESVO_BD.getRespOpcion())) { // Seguimos con el alta.
                                // Actualizar numero.

                                m_Log.debug("********** entro en registrar_alta_entrada_aceptada");
                                long res2 = GestionSecuenciacionNumerosAnotacion.recuperarNumeroSecuencia(regESVO_BD.getIdentDepart(), regESVO_BD.getUnidadOrgan(), 
										regESVO_BD.getTipoReg(), regESVO_BD.getAnoReg(), con);
                                if (res2 > 0) {
                                    if (regESVO_BD.getHayBuzon()) {
                                        String sql = "SELECT * FROM R_RES WHERE " +
                                                sql_codDptoAnotacion + "=" +
                                                regESVO_BD.getIdDepOrigen() + " and " +
                                                sql_codUnidadAnotacion + "=" +
                                                regESVO_BD.getIdUndRegOrigen() + " and " +
                                                sql_ejercicioAnotacion + "=" +
                                                regESVO_BD.getEjeOrigen() + " and " +
                                                sql_numeroAnotacion + "=" +
                                                regESVO_BD.getNumOrigen() + " and " +
                                                sql_tipoRegAnotacion + "='" +
                                                regESVO_BD.getTipoRegOrigen() + "' and " +
                                                sql_estAnot + "=1";
                                        if (m_Log.isDebugEnabled()) {
                                            m_Log.debug("Sentencia para comprobar que el buzón no" + " ha sido todavía actualizado: " + sql);
                                        }
                                        ps = con.prepareStatement(sql);
                                        rs = ps.executeQuery();

                                        if (rs.next()) {//todavía está en el buzón
                                            String sqlUpdate = "UPDATE R_RES SET " + sql_estAnot + "=0 " +
                                                    "where " + sql_codDptoAnotacion + "=" +
                                                    regESVO_BD.getIdDepOrigen() + " and " +
                                                    sql_codUnidadAnotacion + "=" +
                                                    regESVO_BD.getIdUndRegOrigen() + " and " +
                                                    sql_tipoRegAnotacion + "='" +
                                                    regESVO_BD.getTipoRegOrigen() + "' and " +
                                                    sql_ejercicioAnotacion + "=" +
                                                    regESVO_BD.getEjeOrigen() + " and " +
                                                    sql_numeroAnotacion + "=" +
                                                    regESVO_BD.getNumOrigen();
                                            if (m_Log.isDebugEnabled()) {
                                                m_Log.debug("Buzon: " + sql);
                                            }
                                            int res = psUpdate.executeUpdate(sqlUpdate);

                                            if (res < 1) {
                                                m_Log.debug("No se pudo actualizar el buzón");
                                                regESVO_BD.setRespOpcion("registrar_alta_entrada_denegada");
                                            } else {
                                                regESVO_BD.setRespOpcion("registrar_alta_entrada_aceptada");
                                                m_Log.debug("Actualización del buzón realizada");
                                            }
                                        } else {
                                            m_Log.debug("El buzón ya estaba actualizado");
                                            regESVO_BD.setRespOpcion("actualizacion_ya_realizada");
                                        }

                                    } else {//no veníamos del buzón
                                        regESVO_BD.setRespOpcion("registrar_alta_entrada_aceptada");
                                    }
                                } // fin res2
                                else // seguimos alta.
                                {
                                    regESVO_BD.setRespOpcion("registrar_alta_entrada_denegada");
                                }
                            }
                        } else {
                            regESVO_BD.setRespOpcion("registrar_alta_entrada_denegada");
                        }
                    } else {
                        regESVO_BD.setRespOpcion("registrar_alta_entrada_denegada");
                    }
                } // Seguir con el alta.

            } else { // El registro ha sido cerrado
                regESVO_BD.setRespOpcion("registro_cerrado");
            }
            m_Log.debug("la respuesta en el DAO es 2 : " + regESVO_BD.getRespOpcion());

        } catch (SQLException sqle) {
            //SigpGeneralOperations.rollBack(oad, con);
            sqle.printStackTrace();
            m_Log.error("SQLException: " + sqle.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.insercionDatosRegistro"), sqle);
        } catch (Exception e) {
            //SigpGeneralOperations.rollBack(oad, con);
            m_Log.error("Exception: " + e.getMessage());
            e.printStackTrace();            
            throw new TechnicalException("Exception no clasificada: ", e);   			
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.closeStatement(psUpdate);
            //SigpGeneralOperations.devolverConexion(oad, con);
        }
        
        return regESVO_BD;
    }

    /**
     * getTiposDocumentos()
     * - Vector de objetos ElementoListaValueObject.
     *		   - NULL si falla el acceso a BD.
     */
    public Vector getListaTiposDocumentos(String[] params)
            throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaTiposDocumentos");
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT " + sql_codTipoDoc + "," + sql_descTipoDoc + "," + sql_actTipoDoc + " FROM  R_TDO ORDER BY 2";

        try {
            con = abd.getConnection();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO, getListaTiposDocumentos: Sentencia SQL:" + sql);
            }

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            Vector list = new Vector();
            int orden = 0;
            while (rs.next()) {
                ElementoListaValueObject elemListVO = new ElementoListaValueObject(rs.getString(sql_codTipoDoc), rs.getString(sql_descTipoDoc), rs.getString(sql_actTipoDoc), orden++, "");
                list.addElement(elemListVO);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO, getListaTiposDocumentos: Lista tipos documentos cargada");
                m_Log.debug("AnotacionRegistroDAO, getListaTiposDocumentos: Tamaño lista:" + list.size());
            }

            return list;
        } catch (BDException ex) {
            Logger.getLogger(AnotacionRegistroDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaTiposDocumentos"), ex);
        } catch (SQLException e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaTiposDocumentos"), e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, con);
        }
    }
    
    
     public Vector getListaTiposDocumentosAlta(String[] params)
            throws AnotacionRegistroException, TechnicalException {
        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaTiposDocumentos");

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT " + sql_codTipoDoc + "," + sql_descTipoDoc + "," + sql_actTipoDoc + " FROM  R_TDO WHERE TDO_EST = 1 ORDER BY 2";

        try {
            con = abd.getConnection();

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO, getListaTiposDocumentosAlta: Sentencia SQL:" + sql);
            }

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            Vector list = new Vector();
            int orden = 0;

            while (rs.next()) {
                ElementoListaValueObject elemListVO = new ElementoListaValueObject(rs.getString(sql_codTipoDoc), rs.getString(sql_descTipoDoc), rs.getString(sql_actTipoDoc), orden++, "");
                list.addElement(elemListVO);
            }

            if (m_Log.isInfoEnabled()) {
                m_Log.info("AnotacionRegistroDAO, getListaTiposDocumentosAlta: Lista tipos documentos cargada");
                m_Log.info("AnotacionRegistroDAO, getListaTiposDocumentosAlta: Tama?o lista:" + list.size());
            }

            return list;

        } catch (BDException ex) {
            Logger.getLogger(AnotacionRegistroDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaTiposDocumentos"), ex);
        } catch (SQLException e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaTiposDocumentos"), e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, con);
        }
    }


    /**
     * getDocuemntos justificantes()
     * - Vector de objetos ElementoListaValueObject.
     *		   - NULL si falla el acceso a BD.
     * Recogemos las plantillas que pertenecen a la aplicacion tipo registro (apl =1)
     */
    public Vector getListaDocumentosJustificantes(String[] params)
            throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaDocumentosJustificantes");
        //Usar el JDBCWrapper es mas sencillo que usar JDBC directamente
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT PLT_COD,PLT_DES,PLT_EDITOR_TEXTO from A_PLT WHERE plt_apl=1";

        try {
            con = abd.getConnection();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(" Sentencia SQL:" + sql);
            }

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            Vector list = new Vector();
            int orden = 0;
            while (rs.next()) {
                ElementoListaValueObject elemListVO = new ElementoListaValueObject(rs.getString("plt_cod"), rs.getString("plt_des"));
                elemListVO.setEditor(rs.getString("PLT_EDITOR_TEXTO"));
                list.addElement(elemListVO);

            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO, getListaDocumentosJustificantes: Tamaño lista:" + list.size() + ", " + list.toString());
            }
            return list;

        } catch (BDException ex) {
            Logger.getLogger(AnotacionRegistroDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaDocumentosJustificantes"), ex);
        } catch (SQLException e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaDocumentosJustificantes"), e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, con);
        }
    }

    private String obtenEstructuraRaiz(String codAplicacion, String[] params) throws AnotacionRegistroException, TechnicalException {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {

            conexion = abd.getConnection();
            // Creamos la select con los parametros adecuados.
            StringBuffer sql = new StringBuffer();
            sql.append(" select DOC_CEI FROM A_DOC WHERE DOC_APL = ").append(codAplicacion);

            stmt = conexion.createStatement();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql.toString());
            }
            rs = stmt.executeQuery(sql.toString());

            String raiz = "";
            while (rs.next()) {
                raiz = rs.getString("DOC_CEI");
            }
            return raiz;

        } catch (BDException ex) {
            Logger.getLogger(AnotacionRegistroDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaDocumentosJustificantes"), ex);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaDocumentosJustificantes"), sqle);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
    }

    private String obtenEstructuraRaiz(String codAplicacion, String tipoInforme, String[] params) throws AnotacionRegistroException, TechnicalException {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {

            conexion = abd.getConnection();
            if(tipoInforme.contains("_"))
                tipoInforme = tipoInforme.replace("_", "%");
            // Creamos la select con los parametros adecuados.
            StringBuffer sql = new StringBuffer();
            sql.append(" select DOC_CEI FROM A_DOC WHERE DOC_APL = ").append(codAplicacion)
                    .append(" AND DOC_NOM LIKE '%").append(tipoInforme.toUpperCase()).append("%'");

            stmt = conexion.createStatement();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql.toString());
            }
            rs = stmt.executeQuery(sql.toString());

            String raiz = "";
            while (rs.next()) {
                raiz = rs.getString("DOC_CEI");
            }
            return raiz;

        } catch (BDException ex) {
            Logger.getLogger(AnotacionRegistroDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaDocumentosJustificantes"), ex);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaDocumentosJustificantes"), sqle);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
    }


 //selecionamos los documentos especificos para un registro
 public Vector cargaListaDocumentos(GeneralValueObject gVO, String[] params)
	throws AnotacionRegistroException, TechnicalException {
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql;


        Vector<GeneralValueObject> expedientes = new Vector<GeneralValueObject>();

        String codOur = (String) gVO.getAtributo("codOur");
        String codTip = (String) gVO.getAtributo("codTip");
        String ejercicio = (String) gVO.getAtributo("ejercicio");
        String numero = (String) gVO.getAtributo("numero");

        try {

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st = con.createStatement();

			sql = "SELECT " + sql_docsNombreDoc + " as nom," + sql_docsTipoDoc + " as tipo, RED_ENT FROM R_RED WHERE " + sql_docsUnid + "='" + codOur +
		"' AND " + sql_docsTipo + "='" + codTip + "' AND " + sql_docsNum + "='" +
		numero + "' AND " + sql_docsEjerc + "='" + ejercicio + "' AND " + sql_docsTipoDoc + " is not null ORDER BY " + sql_docsNombreDoc;

            if (m_Log.isDebugEnabled()) m_Log.debug(sql);
            rs = st.executeQuery(sql);

            while (rs.next()) {
                gVO = new GeneralValueObject();
                gVO.setAtributo("nomDoc", rs.getString("nom"));
                gVO.setAtributo("tipDoc", rs.getString("tipo"));
                 gVO.setAtributo("entregado", rs.getString("RED_ENT"));
                 expedientes.addElement(gVO);
             }



        } catch (BDException ex) {
            expedientes = new Vector<GeneralValueObject>(); // Vacío
            Logger.getLogger(AnotacionRegistroDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException e) {
            expedientes = new Vector<GeneralValueObject>(); // Vacío
            if (m_Log.isErrorEnabled()) {
                m_Log.error(e.getMessage());
            }
            e.printStackTrace();
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        return expedientes;
	}


 /**
  * Recupera los documentos de los interesados de una anotación de registro y los concatena en un String
  * @param datos: Colección a partir de la cual se construye el XML
  * @return String
  */
  private String getDocumentosInteresadosAnotacion(java.util.Collection datos){
      String salida = "";

      int x = 13;
      char newline = (char) x;

      for (Object objNodo : datos) {
        NodoEntidad n = (NodoEntidad) objNodo;
        String cifRegistro = n.getCampo("DOCSINT");
        m_Log.debug("documento del interesado: " + cifRegistro);
        salida += cifRegistro  + newline;
      }// for

      return salida;

  }// getDocumentosInteresadosAnotacion


  public String consultaXML(GeneralValueObject gvo,String[] params) throws AnotacionRegistroException, TechnicalException{

     m_Log.info("Inicio --> DocumentosJUSTIFICANTE");

     String xml = "";
     String tipoInforme = null;
     String raiz = null;
     
     if(gvo.getAtributo("tipoInforme")!=null){
         tipoInforme = (String)gvo.getAtributo("tipoInforme");
         raiz = obtenEstructuraRaiz((String)gvo.getAtributo("codAplicacion"),tipoInforme,params);
     } else {
         raiz = obtenEstructuraRaiz((String)gvo.getAtributo("codAplicacion"),params);
     }
         
        if(m_Log.isDebugEnabled()) m_Log.debug("--> RAIZ en obten estructura2 : " + raiz);
        EstructuraEntidades eeRaiz;
        String regNum=(String)gvo.getAtributo("ejercicio")+"/"+(String)gvo.getAtributo("numero");
        Vector<String> parametros = new Vector<String>();
        parametros.add((String)regNum);
        parametros.add((String)gvo.getAtributo("codOur"));
        parametros.add((String)gvo.getAtributo("codTip"));

        if(m_Log.isDebugEnabled()) m_Log.debug("regNum::"+regNum);
        if(m_Log.isDebugEnabled()) m_Log.debug("codOur::"+gvo.getAtributo("codOur"));
        if(m_Log.isDebugEnabled()) m_Log.debug("codTip::"+gvo.getAtributo("codTip"));
        
        if(tipoInforme!=null && tipoInforme.indexOf("peticion")!=-1){
            parametros.add((String)gvo.getAtributo("codOfiReg"));
            if(m_Log.isDebugEnabled()) m_Log.debug("codOfiReg::"+gvo.getAtributo("codOfiReg"));
        }

        try{
            eeRaiz = UtilidadesXerador.construyeEstructuraEntidadesInforme(params, "", raiz);
            eeRaiz.setValoresParametrosConsulta(parametros);
            // #239565: se indica en la estructura el tipo de informe que se solicita, solo para peticion o justificante, para otros será nulo
            eeRaiz.setDescTipoInforme(tipoInforme);
            // #267396: se indica en la estructura el formato de fecha
            eeRaiz.setFormatoFecha((String) gvo.getAtributo("formatoFecha"));
            
            UtilidadesXerador.construyeEstructuraDatos(params, eeRaiz, null);
            java.util.Collection col = eeRaiz.getListaInstancias();

            for (Object objNodo : col) {
                NodoEntidad n = (NodoEntidad) objNodo;

                GeneralValueObject temp = new GeneralValueObject();
                temp.setAtributo("codOur", (String)gvo.getAtributo("codOur"));
                temp.setAtributo("numero", (String)gvo.getAtributo("numero"));
                temp.setAtributo("ejercicio", (String)gvo.getAtributo("ejercicio"));
                temp.setAtributo("codTip", (String)gvo.getAtributo("codTip"));

                if (m_Log.isDebugEnabled()) m_Log.debug("_________RELACION________");
                if (m_Log.isDebugEnabled()) m_Log.debug("NUM::" + temp.getAtributo("numero"));
                if (m_Log.isDebugEnabled()) m_Log.debug("COD_TIP::" + temp.getAtributo("codTip"));
                if (m_Log.isDebugEnabled()) m_Log.debug("COD_our::" + temp.getAtributo("codOur"));
                if (m_Log.isDebugEnabled()) m_Log.debug("EJERCICIO ::" + temp.getAtributo("ejercicio"));
                if (m_Log.isDebugEnabled()) m_Log.debug("CARGAR RELACION EXPEDIENTES --> INI cargaListaExpedientes");

            }//Fin del While
            xml = new GeneradorInformesMgr(params).generaXMLResultado(eeRaiz);

        } catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("XML ::"+xml);
        return xml;
    }

    public Vector getListaTiposRemitentes(String[] params)
            throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaTiposRemitentes");
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT " + sql_codTipoRemit + "," + sql_descTipoRemit + "," + sql_actTipoRemit + " FROM  R_TPE ORDER BY 2";

        try {

            con = abd.getConnection();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO, getListaTiposRemitentes: Sentencia SQL:" + sql);
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            Vector list = new Vector();
            int orden = 0;
            while (rs.next()) {
                ElementoListaValueObject elemListVO = new ElementoListaValueObject(rs.getString(sql_codTipoRemit), rs.getString(sql_descTipoRemit), rs.getString(sql_actTipoRemit), orden++, "");
                list.addElement(elemListVO);
            }
            m_Log.debug("AnotacionRegistroDAO, getListaTiposRemitentes: Lista tipos remitentes cargada");
            m_Log.debug("AnotacionRegistroDAO, getListaTiposRemitentes: Tamaño lista:" + list.size());

            return list;
        } catch (BDException ex) {
            Logger.getLogger(AnotacionRegistroDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaTiposRemitentes"), ex);
        } catch (SQLException e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaTiposRemitentes"), e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, con);
        }
    }

    public Vector getListaTiposTransportes(String[] params)
            throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaTiposTransportes");
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT " + sql_codTipoTransp + "," + sql_descTipoTransp + "," + sql_actTipoTransp + " FROM  R_TTR ORDER BY 2";

        try {
            con = abd.getConnection();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO, getListaTiposTransportes: Sentencia SQL:" + sql);
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            Vector list = new Vector();
            int orden = 0;
            while (rs.next()) {
                ElementoListaValueObject elemListVO = new ElementoListaValueObject(rs.getString(sql_codTipoTransp), rs.getString(sql_descTipoTransp), rs.getString(sql_actTipoTransp), orden++, "");
                list.addElement(elemListVO);
            }
                m_Log.debug("AnotacionRegistroDAO, getListaTiposTransportes: Lista tipos Transportes cargada");
                m_Log.debug("AnotacionRegistroDAO, getListaTiposTransportes: Tamaño lista:" + list.size());

            return list;

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaTiposTransportes"), e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, con);
        }
    }

    public Vector getListaActuaciones(String[] params)
            throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaActuaciones");
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT " + sql_codTipoAct + "," + sql_descTipoAct + " FROM R_ACT ";
        sql += " WHERE (" + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ">= " + sql_actFechaDesde + ") ";
        sql += " AND ( (" + sql_actFechaHasta + " IS NULL) ";
        sql += "       OR ( " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + "-15 <= " + sql_actFechaHasta + ") )";
        sql += " ORDER BY 2 ";

        try {
            con = abd.getConnection();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO, getListaActuaciones: Sentencia SQL:" + sql);
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            Vector list = new Vector();
            int orden = 0;
            while (rs.next()) {
                ElementoListaValueObject elemListVO = new ElementoListaValueObject(rs.getString(sql_codTipoAct), rs.getString(sql_descTipoAct), orden++);
                list.addElement(elemListVO);
            }
                m_Log.debug("AnotacionRegistroDAO, getListaActuaciones: Lista Actuaciones cargada");
                m_Log.debug("AnotacionRegistroDAO, getListaActuaciones: Tamaño lista:" + list.size());

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaActuaciones"), e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, con);
        }
    }

    public RegistroValueObject getByPrimaryKey(RegistroValueObject registro, String[] params)
            throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getByPrimaryKey");
        //Usar el JDBCWrapper es mas sencillo que usar JDBC directamente
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        Vector temas = new Vector();
        Vector docs = new Vector();
        int codigoDepartamento = registro.getIdentDepart();
        int codigoUnidad = registro.getUnidadOrgan();
        String tipoRegistro = registro.getTipoReg();
        int ejercicio = registro.getAnoReg();
        Long numeroRegistro = registro.getNumReg();
        int cont = 0;

        try {

            con = oad.getConnection();

            String sql = "SELECT " + oad.convertir(sql_fechaAnotacion, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                    " AS " + sql_fechaAnotacion + "," + sql_tipoDocAnotacion + "," + sql_asuntoAnotacion + "," +
                    sql_tipoAnotacionDestino +
                    ", " + oad.convertir(sql_fechaDocumento, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                    " AS " + sql_fechaDocumento + "," +
                    sql_tipoRemitenteAnotacion + "," + sql_codTerceroAnotacion + "," +
                    sql_domicTerceroAnotacion + "," + sql_numModifInteresado + "," +
                    sql_numTransp + "," + sql_tipoTransporte + "," +
                    sql_entidadDestino + "," + sql_unidRegDestino + "," +
                    sql_orgDestAnot + "," + sql_unidOrigDest + "," +
                    sql_departOrigAnot + "," + sql_orgOrigAnot + "," + sql_unidOrigAnot + "," +
                    sql_entOrigAnot + "," + sql_tipoOrigDest + "," + sql_numOrig + "," + sql_entradaRelacionada + "," +
                    sql_actuacion + "," + sql_estAnot + "," + sql_diligencia +
                    "," + sql_codiVisibleUOR +
                    "," + sql_res_ejeContestada + ", " + sql_res_numContestada + ",r_tdo." + sql_codTipoDoc + ",r_tpe." + sql_codTipoRemit + ",r_tdo." + sql_descTipoDoc +
                    ",r_tpe." + sql_descTipoRemit + ",r_ttr." + sql_descTipoTransp + ",r_ttr." + sql_codTipoTransp +
                    " AS COD_TRANSP,r_act." + sql_descTipoAct + ",r_act." + sql_codTipoAct + ",a_uorex." + sql_nombreUOREX +
                    ",a_orgex." + sql_nombreORGEX + ",A_EX." + sql_nombreUOREX +
                    " AS UOR_ORIGEN," +
                    //"O." + sql_nombreORGEX + " AS ORG_ORIGEN, A1." + sql_nombreUOR + " AS UOR_DESTINO, " + sql_res_observaciones + "," + sql_autoridad + "," + sql_procedimiento + "," + sql_munProcedimiento + "," + sql_pml_valor +
                    // Se extrae el municipio del procedimiento del campo PML_MUN
                    "O." + sql_nombreORGEX + " AS ORG_ORIGEN, A1." + sql_nombreUOR + " AS UOR_DESTINO, " + sql_res_observaciones + "," + sql_autoridad + "," + sql_procedimiento + ", DIGIT_DOC_REGISTRO, PML_MUN," + sql_pml_valor + ", FIN_DIGITALIZACION " +
                    ",(SELECT COUNT (*) FROM  R_EXT WHERE res_uor=ext_uor AND res_dep=ext_dep AND res_tip=ext_tip AND res_eje=ext_eje AND res_num=ext_num )AS num_terceros " +
                    ", ASUNTO,"  + oad.convertir("RES_FEDOC", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                    " AS RES_FEDOC, OFICINA_REGISTRO, REGISTRO_TELEMATICO, FIN_DIGITALIZACION ";


            sql += " FROM R_RES LEFT JOIN R_TDO ON (R_RES." + sql_tipoDocAnotacion + " = R_TDO." + sql_ideTipoDoc + ")";
            sql += " LEFT JOIN R_TPE ON (R_RES." + sql_tipoRemitenteAnotacion + " = R_TPE." + sql_ideTipoRemit + ")";
            sql += " LEFT JOIN R_TTR ON (R_RES." + sql_tipoTransporte + " = R_TTR." + sql_ideTipoTransp + ")";
            sql += " LEFT JOIN R_ACT ON (R_RES." + sql_actuacion + " = R_ACT." + sql_ideTipoAct + ")";
            sql += " LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_ORGEX A_ORGEX ON (R_RES." +
                    sql_orgDestAnot + " = A_ORGEX." + sql_codigoORGEX + ")";
            sql += " LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOREX A_UOREX ON (R_RES." +
                    sql_unidRegDestino + " = A_UOREX." + sql_codigoUOREX +  " AND R_RES." + sql_orgDestAnot + " = A_UOREX." + sql_organizacionUOREX + ")";
            sql += " LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_ORGEX O ON (R_RES." +
                    sql_orgOrigAnot + " = O." + sql_codigoORGEX + ")";
            sql += " LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOREX A_EX ON (R_RES." +
                    sql_unidOrigAnot + " = A_EX." + sql_codigoUOREX +  " AND R_RES." + sql_orgOrigAnot + " = A_EX." + sql_organizacionUOREX +  ")";
            sql += " LEFT JOIN A_UOR A1 ON (R_RES." + sql_unidOrigDest + " = A1." + sql_codigoUOR + ")";
            /*
            sql += " LEFT JOIN E_PML ON (R_RES." + sql_procedimiento + "= E_PML." + sql_pml_cod +
                    " AND R_RES." + sql_munProcedimiento + "= E_PML." + sql_pml_mun + ")";
            */
            sql += " LEFT JOIN E_PML ON (R_RES." + sql_procedimiento + "= E_PML." + sql_pml_cod + ")";

            sql += " LEFT JOIN PLUGIN_DOC_PROCEDIMIENTO ON (R_RES." + sql_procedimiento + "= PLUGIN_DOC_PROCEDIMIENTO.COD_PROCEDIMIENTO)";
            sql += " WHERE " + sql_codDptoAnotacion + "=" + codigoDepartamento + " AND " +
                    sql_codUnidadAnotacion + "=" + codigoUnidad + " AND " +
                    sql_tipoRegAnotacion + "='" + tipoRegistro + "' AND " +
                    sql_ejercicioAnotacion + "=" + ejercicio + " AND " +
                    sql_numeroAnotacion + "=" + numeroRegistro + " AND (" +
                    sql_pml_cmp + "='NOM' OR " + sql_pml_cmp + " IS NULL) AND (" +
                    sql_pml_leng + "='"+ m_ConfigTechnical.getString("idiomaDefecto")+"' OR " + sql_pml_leng + " IS NULL)";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            cont = 0;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                cont++;
                String fechaAnotacion = rs.getString(sql_fechaAnotacion);
                registro.setFecEntrada(fechaAnotacion);
                //m_Log.debug(registro.getHoraEnt());
                //m_Log.debug(registro.getMinEnt());
                String fechaDocumento = rs.getString(sql_fechaDocumento);
                registro.setFecHoraDoc(fechaDocumento);
                //m_Log.debug(fechaDocumento);
                String asunto = rs.getString(sql_asuntoAnotacion);
                registro.setAsunto(AdaptadorSQLBD.js_escape(TransformacionAtributoSelect.replace(asunto, "'", "\'")));
                //m_Log.debug(asunto);
                int tipoAnotacionDestino = rs.getInt(sql_tipoAnotacionDestino);
                registro.setTipoAnot(tipoAnotacionDestino);
                //m_Log.debug(tipoAnotacionDestino);
                int tipoDocAnotacion = rs.getInt(sql_tipoDocAnotacion);
                registro.setIdTipoDoc(tipoDocAnotacion);
                //m_Log.debug(tipoDocAnotacion);
                int tipoRemitenteAnotacion = rs.getInt(sql_tipoRemitenteAnotacion);
                registro.setIdTipoPers(tipoRemitenteAnotacion);
                //m_Log.debug(tipoRemitenteAnotacion);
                int codTerceroAnotacion = rs.getInt(sql_codTerceroAnotacion);
                registro.setCodInter(codTerceroAnotacion);
                int domicTerceroAnotacion = rs.getInt(sql_domicTerceroAnotacion);
                registro.setDomicInter(domicTerceroAnotacion);
                //m_Log.debug(domicTerceroAnotacion);
                int numModifInteresado = rs.getInt(sql_numModifInteresado);
                registro.setNumModInfInt(numModifInteresado);
                //m_Log.debug(numModifInteresado);
                String numTransp = rs.getString(sql_numTransp);
                registro.setNumTransporte(numTransp);
                //m_Log.debug(numTransp);
                String tipoTransporte = rs.getString("COD_TRANSP");
                registro.setCodTipoTransp(tipoTransporte);
                //m_Log.debug(tipoTransporte);
                String entidadDestino = rs.getString(sql_entidadDestino);
                registro.setOrgEntDest(entidadDestino);
                //m_Log.debug("entidad destino " + entidadDestino);
				/* tiruritata
                String dptoDestino = sqlExec.getString(sql_dptoDestino);
                registro.setIdDepDestino(dptoDestino);
                //m_Log.debug("departamento destino " + dptoDestino);
                 */
                String unidRegDestino = rs.getString(sql_unidRegDestino);
                registro.setIdUndRegDest(unidRegDestino);
                //m_Log.debug("unidad registro destino " + unidRegDestino);
                String orgDestAnot = rs.getString(sql_orgDestAnot);
                registro.setOrgDestino(orgDestAnot);
                //m_Log.debug("organizacion destino " + orgDestAnot);
                // tiruritata
				/*String departOrigDest = sqlExec.getString(sql_departOrigDest);
                registro.setIdDepartemento(departOrigDest);
                //m_Log.debug(departOrigDest);
                 */
                String unidOrigDest = rs.getString(sql_unidOrigDest);
                registro.setIdUndTramitad(unidOrigDest);
                //m_Log.debug(unidOrigDest);
				/* Fecha 28/07/2003
                 * Enlace con SGE */
                // tiruritata
                //registro.setCodDptoDestBD(departOrigDest);
                registro.setCodUORDestBD(unidOrigDest);
                int departOrigAnot = rs.getInt(sql_departOrigAnot);
                registro.setIdDepOrigen(departOrigAnot);
                //m_Log.debug("departamento origen " + departOrigAnot);
                String orgOrigAnot = rs.getString(sql_orgOrigAnot);
                registro.setOrganizacionOrigen(orgOrigAnot);
                //m_Log.debug("organizacion origen " + orgOrigAnot);
                String unidOrigAnot = rs.getString(sql_unidOrigAnot);
                registro.setIdUndRegOrigen(unidOrigAnot);
                //m_Log.debug("unidad registro origen " + unidOrigAnot);
                int entOrigAnot = rs.getInt(sql_entOrigAnot);
                registro.setOrgEntOrigen(entOrigAnot);
                //m_Log.debug("entidad origen " + entOrigAnot);
                String tipoOrigDest = rs.getString(sql_tipoOrigDest);
                registro.setTipoRegOrigen(tipoOrigDest);
                if(registro.getTipoRegOrigen() == null || "".equalsIgnoreCase(registro.getTipoRegOrigen())){
                    registro.setTipoRegOrigen("SGE");
                }//if(registro.getTipoRegOrigen() == null || "".equalsIgnoreCase(registro.getTipoRegOrigen()))
                //m_Log.debug(tipoOrigDest);
                String numOrig = rs.getString(sql_numOrig);
                registro.setNumOrigen(numOrig);
                String entradaRel = rs.getString(sql_entradaRelacionada);
                if(entradaRel != null && entradaRel.length()>0 && entradaRel.contains("/")){
                    String[] partes = entradaRel.split("/");
                    registro.setEjeEntradaRel(partes[0]);
                    registro.setNumEntradaRel(partes[1]);
                } else {
                    registro.setNumEntradaRel(entradaRel);
                }
                //m_Log.debug(numOrig);
                int actuacion = rs.getInt(sql_actuacion);
                registro.setIdActuacion(actuacion);
                //m_Log.debug(actuacion);
                int estAnot = rs.getInt(sql_estAnot);
                registro.setEstAnotacion(estAnot);
                //m_Log.debug(estAnot);
                String diligencia = rs.getString(sql_diligencia);
                if (diligencia != null) {
                    registro.setDilAnulacion(AdaptadorSQLBD.js_escape(diligencia));
                }
                //m_Log.debug(diligencia);
                String codigo_visible = rs.getString(sql_codiVisibleUOR);
                if (codigo_visible != null) {
                    registro.setUorCodVisible(codigo_visible);
                }
                m_Log.debug("UOR cod visible: " + registro.getUorCodVisible());
                String codigoDocumento = rs.getString(sql_codTipoDoc);
                registro.setCodTipoDoc(codigoDocumento);
                String codigoTipoRemitente = rs.getString(sql_codTipoRemit);
                registro.setCodTipoRemit(codigoTipoRemitente);
                String descDocumento = rs.getString(sql_descTipoDoc);
                registro.setDescTipoDoc(descDocumento);
                String descTipoRemitente = rs.getString(sql_descTipoRemit);
                registro.setDescTipoRem(descTipoRemitente);
                String descTipoTransp = rs.getString(sql_descTipoTransp);
                registro.setDescTipoTransporte(descTipoTransp);
                String codigoActuacion = rs.getString(sql_codTipoAct);
                registro.setCodAct(codigoActuacion);
                String descActuacion = rs.getString(sql_descTipoAct);
                registro.setDescActuacion(descActuacion);
                String nomUnidadRegistroDestino = rs.getString(sql_nombreUOREX);
                registro.setNomUniRegDestino(nomUnidadRegistroDestino);
                String nomDepartamentoDestino = "";
                registro.setNomDptoDestino(nomDepartamentoDestino);
                String nomEntidadDestino = "";
                registro.setNomEntidadDestino(nomEntidadDestino);
                String nomOrganizacionDestino = rs.getString(sql_nombreORGEX);
                registro.setNomOrganizacionDestino(nomOrganizacionDestino);
                String nomOrganizacionOrigen = rs.getString("ORG_ORIGEN");
                registro.setNomOrganizacionOrigen(nomOrganizacionOrigen);
                String nomEntidadOrigen = "";
                registro.setNomEntidadOrigen(nomEntidadOrigen);
                String nomDepartamentoOrigen = "";
                registro.setNomDptoOrigen(nomDepartamentoOrigen);
                String nomUnidadRegistroOrigen = rs.getString("UOR_ORIGEN");
                registro.setNomUniRegOrigen(nomUnidadRegistroOrigen);
                //String nomDepartamentoDestino1 = sqlExec.getString("DEP_DESTINO"); ++ELIMINAR DEPTOS++
                registro.setNomDptoDestinoOrd("DEPARTAMENTO1");
                String nomUnidadTramitadoraDestino = rs.getString("UOR_DESTINO");
                registro.setNomUniDestinoOrd(nomUnidadTramitadoraDestino);
                registro.setEjercicioAnotacionContestada(rs.getString(sql_res_ejeContestada));
                registro.setNumeroAnotacionContestada(rs.getString(sql_res_numContestada));
                //registro.setObservaciones(sqlExec.getString(sql_res_observaciones));
                String observaciones = rs.getString(sql_res_observaciones);
                registro.setObservaciones(AdaptadorSQLBD.js_escape(TransformacionAtributoSelect.replace(observaciones, "'", "\'")));
                
                registro.setRegistroTelematico(false);
                int registroTelematico = rs.getInt("REGISTRO_TELEMATICO");                
                if(registroTelematico==1){
                    registro.setRegistroTelematico(true);
                }
                
                boolean finDigitalizacion = BooleanUtils.toBoolean(
                        rs.getInt("FIN_DIGITALIZACION"), JdbcOperations.VALOR_TRUE_EN_CAMPOS_BBDD, JdbcOperations.VALOR_FALSE_EN_CAMPOS_BBDD);
                registro.setFinDigitalizacion(finDigitalizacion);
                
                String aut = rs.getString(sql_autoridad);
                if (aut != null) {
                    registro.setAutoridad(AdaptadorSQLBD.js_escape(TransformacionAtributoSelect.replace(aut, "'", "\'")));
                }
                String codProcedimiento = rs.getString(sql_procedimiento);
                if (codProcedimiento == null) {
                    registro.setCodProcedimiento("");
                    registro.setProcedimientoDigitalizacion(false);
                } else {
                    registro.setCodProcedimiento(codProcedimiento);
                    String procDigitalizacion = rs.getString("DIGIT_DOC_REGISTRO");
                    if(procDigitalizacion.toUpperCase().equals("SI")){
                        registro.setProcedimientoDigitalizacion(true);
                    }else{
                        registro.setProcedimientoDigitalizacion(false);
                    }
                }
                registro.setCodProcedimientoRoles(registro.getCodProcedimiento());
                
                String descProcedimiento = rs.getString(sql_pml_valor);
                if (descProcedimiento == null) {
                    registro.setDescProcedimiento("");
                } else {
                    registro.setDescProcedimiento(descProcedimiento);
                }

                //registro.setMunProcedimiento(rs.getString(sql_munProcedimiento));
                // Se extrae el municipio del procedimiento del campo PML_MUN y no de R_RES.PROCMUN
                registro.setMunProcedimiento(rs.getString("PML_MUN"));
                registro.setNumTerceros(rs.getInt("num_terceros"));

                // original
                //registro.setCodAsunto(rs.getString("ASUNTO"));
                
                String codigo_asunto = rs.getString("ASUNTO");
                registro.setCodAsunto(codigo_asunto);
                m_Log.debug(" *********** LA ANOTACIÓN " + numeroRegistro + " y ejercicio: " + ejercicio + " tiene como asunto: " + codigo_asunto);
                String fecBaja = MantAsuntosDAO.getInstance().estaDeBajaAsuntoCodificado(codigo_asunto,Integer.toString(codigoUnidad),tipoRegistro,con);
                registro.setAsuntoAnotacionBaja(false);
                if(fecBaja!=null && !"".equals(fecBaja)){
                    m_Log.debug(" *********** asunto dado de baja con fecha " + fecBaja);
                    registro.setAsuntoAnotacionBaja(true);
                    registro.setFechaBajaAsuntoCodificadoRegistro(fecBaja);
                }

                
                registro.setFechaDocu(rs.getString("RES_FEDOC"));
                String codOficinaRegistro = rs.getString("OFICINA_REGISTRO");
                if(codOficinaRegistro!=null && !"".equals(codOficinaRegistro))
                    registro.setCodOficinaRegistro(new Integer(codOficinaRegistro));
                
                registro.setFinDigitalizacion(rs.getBoolean("FIN_DIGITALIZACION"));
                
            }

            // Cerramos el resultSet y statement relativos a la consulta anterior.
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);

            if (cont != 0) {
                registro.setFallo("bien");
                /* Enlace con SGE.
                 * Fecha: 22/07/2003. */

                
                PluginExpedientesRelacionados plugin = (PluginExpedientesRelacionados)PluginExpedientesRelacionadosFactoria.getImplClass(Integer.toString(registro.getIdOrganizacion()));                
                ExpedienteRelacionadoVO exp = new ExpedienteRelacionadoVO();
                exp.setCodigoDepartamento(Integer.toString(registro.getIdentDepart()));
                exp.setCodigoUnidadRegistro(Integer.toString(registro.getUnidadOrgan()));
                exp.setTipoRegistro(registro.getTipoReg());
                exp.setEjercicioRegistro(Integer.toString(registro.getAnoReg()));
                exp.setNumeroRegistro(Long.toString(registro.getNumReg()));
                exp.setConnection(con);
                
                exp = plugin.getNumeroExpedienteRelacionado(exp);
                /* Original (Modificaciones para multiples expedientes relacionados)
                registro.setNumExpediente(exp.getNumExpedienteRelacionado());
                if(exp.getCodProcedimiento()!=null && exp.getCodProcedimiento().length()>0){
                    registro.setCodProcedimiento(exp.getCodProcedimiento());
                    registro.setDescProcedimiento(exp.getNombreProcedimiento());
                }
                */
                
                ArrayList<String> listaNumerosExpedientesRelacionados = exp.getNumExpedientesRelacionados();
                ArrayList<String> listaCodProcExpedientesRelacionados = exp.getCodProcedimientoExpedientesRelacionados();
                if(listaNumerosExpedientesRelacionados.size() > 0){
                    registro.setNumExpedientesRelacionados(listaNumerosExpedientesRelacionados);
                }else{
                    registro.setNumExpedientesRelacionados(new ArrayList<String>());
                }//if(listaNumerosExpedientesRelacionados.size() > 0)
                if(listaCodProcExpedientesRelacionados.size() > 0){
                    registro.setCodProcedimientoExpedientesRelacionados(listaCodProcExpedientesRelacionados);
                }else{
                    registro.setCodProcedimientoExpedientesRelacionados(new ArrayList<String>());
                }//if(listaCodProcExpedientesRelacionados.size() > 0)
                
            }

            
            String sql2 = "SELECT " + sql_codTema + "," + sql_descTema + " FROM R_RET,R_TEM WHERE " +
                    sql_temasDepto + "=" + codigoDepartamento + " AND " + sql_temasUnid + "=" + codigoUnidad +
                    " AND " + sql_temasTipo + "='" + tipoRegistro + "' AND " + sql_temasNum + "=" +
                    numeroRegistro + " AND " + sql_temasEjerc + "=" + ejercicio + " AND r_ret." + sql_temasIdTema +
                    "=r_tem." + sql_idTema + " ORDER BY r_ret." + sql_temasOrden;
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql2);
            }

            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();

            RegistroValueObject registro1;
            while (rs.next()) {
                registro1 = new RegistroValueObject();
                String codigoTema = rs.getString(sql_codTema);
                registro1.setCodigoTema(codigoTema);
                //m_Log.debug("el codigo de tema es : " + codigoTema);
                String descTema = rs.getString(sql_descTema);
                registro1.setDescTema(descTema);
                //m_Log.debug("la descripcion de tema es : " + descTema);
                temas.addElement(registro1);
            }

            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);

            m_Log.debug("el tamaño del vector listaTemas es : " + temas.size());
            registro.setListaTemasAsignados(temas);
            m_Log.debug("el tamaño del vector listaTemas es : " + registro.getListaTemasAsignados().size());


            // Recuperacion de la lista de asientos relacionados.
            registro.setRelaciones(getRelaciones(registro, con));
            m_Log.debug("Relaciones recuperadas: " + registro.getRelaciones());

            m_Log.debug("el tamaño del vector listaDocs es : " + docs.size());

            //Vector documentos = getDocumentos(registro, con, params);
            
            Vector documentos = getListaDocumentos(registro,con);
            
            
            DocumentoValueObject[] arrayDocs = convertToArray(documentos);
            // Documentos asociados a la anotacion para ver en buzon de entrada
            registro.setDocumentos(arrayDocs);
            // documentos asociados a la anotacion para ver en registro
            registro.setListaDocsAsignados(documentos);
            
            //Vector documentosAnteriores	
            Vector documentosAnteriores=getListaEntregadosAnterior(registro,con);
            registro.setListaDocsAnteriores(documentosAnteriores);
            

            m_Log.debug("el tamaño del vector listaDocs es : " + documentos.size());
            if (cont == 0) {

                String sql1 = "SELECT " + oad.convertir(sql_fechaRER, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") +
                        " AS " + sql_fechaRER + " FROM R_RER WHERE " +
                        sql_codigoDepartamentoRER + "=" + codigoDepartamento + " AND " +
                        sql_codigoUnidadRER + "=" + codigoUnidad + " AND " +
                        sql_tipoRER + "='" + tipoRegistro + "' AND " +
                        sql_ejercicioRER + "=" + ejercicio + " AND " +
                        sql_numeroRER + "=" + numeroRegistro;
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql1);
                }

                ps = con.prepareStatement(sql1);
                rs = ps.executeQuery();

                int cont1 = 0; // Se inicializa a un valor distinto de 0 en getByPrimaryKey si la busqueda es una reserva
                // setContador Solo se inicializa si hay reservas
                while (rs.next()) {
                    cont1++;
                    String fechaAnotacion = rs.getString(sql_fechaRER);
                    registro.setFecEntrada(fechaAnotacion);
                }
                registro.setContador(cont1);
                m_Log.debug("el numero de cuenta es : " + cont1);
                if ((cont == 0) && (cont1 == 0)) {
                    registro.setFallo("fallo");
                } else {
                    registro.setFallo("bien");
                }
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            
            // #234108: obtenemos el valor del campo de la tabla asunto que indica obligatoriedad de documento en interesado
            if(registro.getCodAsunto()!=null && !registro.getCodAsunto().equals("")){
                  String sqlAsunto = "SELECT DOCINT_OBLIGATORIO, BLOQUEAR_DESTINO, BLOQUEAR_PROCEDIMIENTO FROM R_TIPOASUNTO WHERE CODIGO=? AND UNIDADREGISTRO=?";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sqlAsunto);
                }

                ps = con.prepareStatement(sqlAsunto);
                int contbd = 1;
                ps.setString(contbd++, registro.getCodAsunto());
                ps.setInt(contbd, codigoUnidad);
                rs = ps.executeQuery();
                
                if(rs.next()){
                    registro.setTipoDocInteresadoOblig(rs.getBoolean(1));
                    registro.setBloquearDestino(rs.getBoolean(2));
                    registro.setBloquearProcedimiento(rs.getBoolean(3));
                }
            }
            
            //Queremos estar informados de cuando este metodo ha finalizado
            m_Log.debug("getByPrimaryKey");
            return registro;

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaActuaciones"), e);

        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }

    public RegistroValueObject getCampoAnotacionByPrimaryKey(RegistroValueObject registro, String campo, Connection con)
            throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getCampoAnotacionByPrimaryKey BEGIN");
        

        PreparedStatement ps = null;
        ResultSet rs = null;

        int codigoDepartamento = registro.getIdentDepart();
        int codigoUnidad = registro.getUnidadOrgan();
        String tipoRegistro = registro.getTipoReg();
        int ejercicio = registro.getAnoReg();
        Long numeroRegistro = registro.getNumReg();
        
        try {
            String sql = "SELECT " + campo + " FROM R_RES WHERE " + sql_codDptoAnotacion + "=" + codigoDepartamento + " AND " +
                    sql_codUnidadAnotacion + "=" + codigoUnidad + " AND " +
                    sql_tipoRegAnotacion + "='" + tipoRegistro + "' AND " +
                    sql_ejercicioAnotacion + "=" + ejercicio + " AND " +
                    sql_numeroAnotacion + "=" + numeroRegistro;

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Object valorCampo = rs.getObject(sql_unidOrigDest);
                if(campo.equals(sql_unidOrigDest)){
                    registro.setCodUORDestBD(valorCampo.toString());
                }
            }//Queremos estar informados de cuando este metodo ha finalizado
            m_Log.debug("getCampoAnotacionByPrimaryKey END");
            return registro;

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaActuaciones"), e);

        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
    }


    private DocumentoValueObject[] convertToArray(Vector documentos){
        m_Log.debug("convertToArray convertiendo Vector de documentos en array de DocumentoValueObject");
        DocumentoValueObject[] docAnotacion = new DocumentoValueObject[documentos.size()];
        m_Log.debug("convertToArray convertiendo Vector de documentos en array de DocumentoValueObject nun docs: " + documentos.size());
        for(int i=0;i<documentos.size();i++){
            RegistroValueObject registro = (RegistroValueObject)documentos.get(i);
            DocumentoValueObject doc = new DocumentoValueObject();
            doc.setNombre(registro.getNombreDoc());
            doc.setExtension(registro.getTipoDoc());
            doc.setFichero(registro.getDoc());
            doc.setFecha(registro.getFechaDoc());
            doc.setCatalogado(registro.getCatalogado());
            doc.setDigitalizado(registro.getCompulsado());
            doc.setUnidadOrg(registro.getUnidadOrgan());
            doc.setTipoDocumental(registro.getDescripcionTipoDocumental());
            doc.setIdDocumento(registro.getIdDocumento());
            docAnotacion[i] = doc;
        }
        return docAnotacion;
    }


    /**
     * Devuelve un vector de SimpleRegistroValueObject representando la lista de asientos relacionados.
     */
    private Vector<SimpleRegistroValueObject> getRelaciones(RegistroValueObject asiento, Connection con)
            throws TechnicalException {

        m_Log.debug("getRelaciones");

        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector<SimpleRegistroValueObject> lista = new Vector<SimpleRegistroValueObject>();

        try {
            // Como la relación es simétrica y sólo hay una fila por cada par de asientos relacionados,
            // hay que hacer la consulta comparando con el primero y con el segundo de la relación.
            long numReg = asiento.getNumReg();
            String sql = "SELECT " + sql_rel_tipoB + ", " + sql_rel_ejercicioB + ", " + sql_rel_numeroB +
                    " FROM R_REL" +
                    " WHERE " + sql_rel_uor + "=" + asiento.getUnidadOrgan() +
                    " AND " + sql_rel_dep + "=" + asiento.getIdentDepart() +
                    " AND " + sql_rel_tipoA + "='" + asiento.getTipoReg() + "'" +
                    " AND " + sql_rel_ejercicioA + "=" + asiento.getAnoReg() +
                    " AND " + sql_rel_numeroA + "=" + numReg +
                    " UNION " +
                    " SELECT " + sql_rel_tipoA + ", " + sql_rel_ejercicioA + ", " + sql_rel_numeroA +
                    " FROM R_REL" +
                    " WHERE " + sql_rel_uor + "=" + asiento.getUnidadOrgan() +
                    " AND " + sql_rel_dep + "=" + asiento.getIdentDepart() +
                    " AND " + sql_rel_tipoB + "='" + asiento.getTipoReg() + "'" +
                    " AND " + sql_rel_ejercicioB + "=" + asiento.getAnoReg() +
                    " AND " + sql_rel_numeroB + "=" + numReg +
                    " ORDER BY 1,2,3";

            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                SimpleRegistroValueObject reg = new SimpleRegistroValueObject();
                reg.setUor(Integer.toString(asiento.getUnidadOrgan()));
                reg.setDep(Integer.toString(asiento.getIdentDepart()));
                reg.setTipo(rs.getString(1));
                reg.setEjercicio(rs.getString(2));
                reg.setNumero(rs.getString(3));
                lista.add(reg);
            }

            return lista;

        } catch (Exception e) {
            e.printStackTrace();
            throw new TechnicalException("Error.AnotacionRegistroDAO->getRelaciones", e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    private Vector getDocumentos(RegistroValueObject registro, Connection con, String[] params)
            throws AnotacionRegistroException, TechnicalException {

        m_Log.debug("AnotacionRegistroDAO.getDocumentos");
        //Queremos estar informados de cuando este metod es ejecutado
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);

        PreparedStatement stmt = null;
        ResultSet rs = null;

        Vector docs = new Vector();
        int codigoDepartamento = registro.getIdentDepart();
        int codigoUnidad = registro.getUnidadOrgan();
        String tipoRegistro = registro.getTipoReg();
        int ejercicio = registro.getAnoReg();
        Long numeroRegistro = registro.getNumReg();

        byte[] fichero = null;
        RegistroValueObject registro2;
        try {

            String sql = "SELECT " + sql_docsNombreDoc + "," + sql_docsTipoDoc + "," + abd.convertir(sql_docsFec, AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + "," + sql_docsDoc + " ,RED_ENT FROM R_RED WHERE " +
                    sql_docsDepto + "=" + codigoDepartamento + " AND " + sql_docsUnid + "=" + codigoUnidad +
                    " AND " + sql_docsTipo + "='" + tipoRegistro + "' AND " + sql_docsNum + "=" +
                    numeroRegistro + " AND " + sql_docsEjerc + "='" + ejercicio + "' ORDER BY " + sql_docsNombreDoc;
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                registro2 = new RegistroValueObject();
                String nombreDoc = rs.getString(sql_docsNombreDoc);
                registro2.setNombreDoc(nombreDoc);
                //m_Log.debug("el codigo de tema es : " + codigoTema);


                String fecDoc = rs.getString(3);
                if (fecDoc == null) {
                    fecDoc = "";
                }
                registro2.setFechaDoc(fecDoc);

                String tipoDoc = rs.getString(sql_docsTipoDoc);
                if (tipoDoc == null) {
                    tipoDoc = "";
                }
                registro2.setTipoDoc(tipoDoc);

                InputStream st = rs.getBinaryStream(sql_docsDoc);
                if (st != null) {
                    ByteArrayOutputStream ot = new ByteArrayOutputStream();
                    int c;
                    while ((c = st.read()) != -1) {
                        ot.write(c);
                    }
                    ot.flush();
                    fichero = ot.toByteArray();
                    registro2.setDoc(fichero);
                } else {
                    registro2.setDoc(new byte[0]);
                }

                String entregado = rs.getString("RED_ENT");
                registro2.setEntregado(entregado);


                m_Log.debug("Recuperado documento: " + registro2.getNombreDoc() + " Tipo: " + registro2.getTipoDoc() + "Fecha: " + registro2.getFechaDoc());
                docs.addElement(registro2);
            }

            return docs;

        } catch (Exception e) {
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        }
    }

     /**
     * Recupera una lista de documentos entregados enteriormente a una determinada anotación de registro
     * @param registro: Objeto de tipo RegistroValueObject que contiene toda la información de la anotación de registro
     * @param con: Conexión a la Base de Datos
     * @return Vector de objetos RegistroValueObject con la información de los objetos a recuperar
     * @throws AnotacionRegistroException
     */
    private Vector<RegistroValueObject> getListaEntregadosAnterior(RegistroValueObject registro, Connection con)
            throws TechnicalException{
        m_Log.debug("AnotacionRegistroDAO.getListaEntregadosAnterior");
        PreparedStatement stmt=null;
        ResultSet rs=null;
        
        Vector<RegistroValueObject> docs=new Vector<RegistroValueObject>();
        int codigoDepartamento=registro.getIdentDepart();
        String tipoRegistro=registro.getTipoReg();
        int ejercicio=registro.getAnoReg();
        Long numeroRegistro=registro.getNumReg();
        RegistroValueObject registro2;
        try {

            String sql = "SELECT R_DOC_APORTADOS_NOM_DOC,R_DOC_APORTADOS_UOR,R_DOC_APORTADOS_NUM,"
                    + "R_DOC_APORTADOS_DEP,R_DOC_APORTADOS_EJE,R_DOC_APORTADOS_TIP_DOC,R_DOC_APORTADOS_FEC_DOC,"
                    + "R_DOC_APORTADOS_ORGANO,R_DOC_APORTADOS_TIP FROM R_DOC_APORTADOS_ANTERIOR WHERE " +
                         "R_DOC_APORTADOS_DEP=" + codigoDepartamento + 
                         " AND R_DOC_APORTADOS_TIP ='" + tipoRegistro + "' AND R_DOC_APORTADOS_NUM =" +
                         numeroRegistro + " AND R_DOC_APORTADOS_EJE ='" + ejercicio + "' ORDER BY R_DOC_APORTADOS_NOM_DOC";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                registro2 = new RegistroValueObject();
                String nombreDocAnterior = rs.getString("R_DOC_APORTADOS_NOM_DOC");
                registro2.setNombreDocAnterior(nombreDocAnterior);
                                
                java.sql.Timestamp tFecDocumento = rs.getTimestamp("R_DOC_APORTADOS_FEC_DOC");
                if(tFecDocumento!=null){                    
                    Calendar cFecDocumento = DateOperations.toCalendar(tFecDocumento);
                    registro2.setFechaDocAnterior(DateOperations.toString(cFecDocumento,"dd/MM/yyyy"));
                }
                String organoDocAnterior=rs.getString("R_DOC_APORTADOS_ORGANO");
                registro2.setOrganoDocAnterior(organoDocAnterior);
                
                String tipoDocAnterior=rs.getString("R_DOC_APORTADOS_TIP_DOC");
                registro2.setTipoDocAnterior(tipoDocAnterior);
                
                registro2.setAnoReg(rs.getInt("R_DOC_APORTADOS_EJE"));
                registro2.setNumReg(rs.getLong("R_DOC_APORTADOS_NUM"));
                registro2.setUorCodVisible(rs.getString("R_DOC_APORTADOS_UOR"));
                registro2.setIdentDepart(rs.getInt("R_DOC_APORTADOS_DEP"));
                registro2.setTipoReg(rs.getString("R_DOC_APORTADOS_TIP"));
                registro2.setEstadoEntregadoAnterior(ConstantesDatos.ESTADO_DOCUMENTO_GRABADO);
                
                m_Log.debug("Recuperado doc aportado anterior: " + registro2.getNombreDocAnterior() + " Tipo: " + registro2.getTipoDocAnterior() +
                        "Fecha: " + registro2.getFechaDoc()+" Organo: "+registro2.getOrganoDocAnterior());
                docs.addElement(registro2);
            }
                return docs;

        } catch (Exception e) {
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        }
    }
    
    /**
     * Recupera una lista de documentos asociados a un determinada anotación de registro
     * @param registro: Objeto de tipo RegistroValueObject que contiene toda la información de la anotación de registro
     * @param con: Conexión a la BBDD
     * @return Vector de objetos RegistroValueObject con la información de los objetos a recuperar
     * @throws AnotacionRegistroException 
     */
    
    /**
     * Recupera una lista de documentos asociados a un determinada anotación de registro
     * @param registro: Objeto de tipo RegistroValueObject que contiene toda la información de la anotación de registro
     * @param con: Conexión a la BBDD
     * @return Vector de objetos RegistroValueObject con la información de los objetos a recuperar
     * @throws AnotacionRegistroException 
     */
    public Vector getListaDocumentos(RegistroValueObject registro, Connection con)
            throws TechnicalException{

        m_Log.debug("AnotacionRegistroDAO.getDocumentos");
        
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Vector<RegistroValueObject> docs = new Vector<RegistroValueObject>();
        int codigoDepartamento = registro.getIdentDepart();
        int codigoUnidad = registro.getUnidadOrgan();
        String tipoRegistro = registro.getTipoReg();
        int ejercicio = registro.getAnoReg();
        Long numeroRegistro = registro.getNumReg();
        
         m_Log.debug("numeroRegistro "+ numeroRegistro);

        byte[] fichero = null;
        RegistroValueObject registro2;
        try {
			String sql = "SELECT RED_DOC_ID,RED_NOM_DOC,RED_UOR,RED_NUM,RED_DEP,RED_EJE,RED_TIP_DOC,RED_FEC_DOC,RED_ENT,RED_TIP,RED_TAMANHO, "
					+ "RED_DOCDIGIT,TIPDOC_LANBIDE_ES AS DESCRIPCION_TIPDOCUMENTAL, REPLACE(RED_OBSERV,CHR(10),'|') AS RED_OBSERV, "
					+ "mdc.DEPARTAMENTO AS DEPARTAMENTO, mdc.UOR AS UOR, mdc.EJERCICIO AS EJERCICIO, mdc.NUMERO AS NUMERO, mdc.TIPO_REGISTRO AS TIPO_REGISTRO, "
					+ "mdc.NOMBRE_DOC AS NOMBRE_DOC, mdc.VERSION_NTI AS VERSION_NTI, mdc.ID_DOCUMENTO AS ID_DOCUMENTO, mdc.ORGANO AS ORGANO, "
					+ "mdc.FECHA_CAPTURA AS FECHA_CAPTURA, mdc.ORIGEN AS ORIGEN, mdc.ESTADO_ELABORACION AS ESTADO_ELABORACION, mdc.NOMBRE_FORMATO AS NOMBRE_FORMATO, "
					+ "mdc.TIPO_DOCUMENTAL AS TIPO_DOCUMENTAL, mdc.TIPO_FIRMA AS TIPO_FIRMA " +
                         "FROM R_RED LEFT JOIN METADATO_DOC_COTEJADOS mdc " +
                                            "ON R_RED.RED_DEP = mdc.DEPARTAMENTO AND " +
                                            "R_RED.RED_UOR = mdc.UOR AND " +
                                            "R_RED.RED_EJE = mdc.EJERCICIO AND " +
                                            "R_RED.RED_NUM = mdc.NUMERO AND " +
                                            "R_RED.RED_TIP = mdc.TIPO_REGISTRO AND " +
                                            "R_RED.RED_NOM_DOC = mdc.NOMBRE_DOC " + 
                         "LEFT JOIN MELANBIDE68_TIPDOC_LANBIDE ON R_RED.RED_TIPODOC_ID = MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_ID " +                     
                         "WHERE " +
                         "RED_DEP=" + codigoDepartamento + " AND " + sql_docsUnid + "=" + codigoUnidad +
                         " AND " + sql_docsTipo + "='" + tipoRegistro + "' AND " + sql_docsNum + "=" +
                         numeroRegistro + " AND " + sql_docsEjerc + "='" + ejercicio + "' "
					+ "ORDER BY " + sql_docsFec + " ASC" + "," + sql_docsNombreDoc + " ASC ";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                registro2 = new RegistroValueObject();
                
                // Recuperacion de los metadatos
                Integer departamentoMetadato = JdbcOperations.getIntegerFromResultSet(rs, "DEPARTAMENTO");
                if (departamentoMetadato != null) {
                    DocumentoMetadatosVO metadatos = new DocumentoMetadatosVO();

                    metadatos.setDepartamento(departamentoMetadato);
                    metadatos.setUor(JdbcOperations.getIntegerFromResultSet(rs, "UOR"));
                    metadatos.setEjercicio(JdbcOperations.getIntegerFromResultSet(rs, "EJERCICIO"));
                    metadatos.setNumero(JdbcOperations.getLongFromResultSet(rs, "NUMERO"));
                    metadatos.setTipoRegistro(rs.getString("TIPO_REGISTRO"));
                    metadatos.setNombreDoc(rs.getString("NOMBRE_DOC"));
                    metadatos.setVersionNTI(rs.getString("VERSION_NTI"));
                    metadatos.setIdDocumento(JdbcOperations.getLongFromResultSet(rs, "ID_DOCUMENTO"));
                    metadatos.setOrgano(rs.getString("ORGANO"));
                    metadatos.setFechaCaptura(DateOperations.timestampToCalendar(rs.getTimestamp("FECHA_CAPTURA")));
                    metadatos.setOrigen(JdbcOperations.getIntegerFromResultSet(rs, "ORIGEN"));
                    metadatos.setEstadoElaboracion(JdbcOperations.getIntegerFromResultSet(rs, "ESTADO_ELABORACION"));
                    metadatos.setNombreFormato(rs.getString("NOMBRE_FORMATO"));
                    metadatos.setTipoDocumental(rs.getInt("TIPO_DOCUMENTAL"));
                    metadatos.setTipoFirma(JdbcOperations.getIntegerFromResultSet(rs, "TIPO_FIRMA"));
                    registro2.setMetadatosDoc(metadatos);
                    
                    registro2.setCotejado(ConstantesDatos.SI);
                } else {
                    registro2.setCotejado(ConstantesDatos.NO);
                }
				
                // Se recupera la propiedad que indica si el documento proviene de la digitalizacion
                 Integer documentoDigitalizado = JdbcOperations.getIntegerFromResultSet(rs, "RED_DOCDIGIT");
                if (documentoDigitalizado == 1) {
                    registro2.setCompulsado(ConstantesDatos.SI);
                } else {
                    registro2.setCompulsado(ConstantesDatos.NO);
                }
				
                // Se recupera el tipo documental y el estado de catalogacion del documento
                String tipoDocumental = rs.getString("DESCRIPCION_TIPDOCUMENTAL");
                if(tipoDocumental == null){
                    tipoDocumental="";
                    registro2.setCatalogado(ConstantesDatos.NO);
                } else {                    
                    registro2.setCatalogado(ConstantesDatos.SI);
                } 
                registro2.setDescripcionTipoDocumental(tipoDocumental);
                
                // Datos del fichero
                
                String nombreDoc = rs.getString("RED_NOM_DOC");
                registro2.setNombreDoc(nombreDoc);
                                
                java.sql.Timestamp tFecDocumento = rs.getTimestamp("RED_FEC_DOC");
                if(tFecDocumento!=null){                    
                    Calendar cFecDocumento = DateOperations.toCalendar(tFecDocumento);
                    registro2.setFechaDoc(DateOperations.toString(cFecDocumento,"dd/MM/yyyy"));
                }
                
                String tipoDoc = rs.getString("RED_TIP_DOC");
                if (tipoDoc == null) {
                    tipoDoc = "";
                    registro2.setCotejado("");
                }
                registro2.setTipoDoc(tipoDoc);
                String entregado = rs.getString("RED_ENT");
                registro2.setEntregado(entregado);
                
                registro2.setIdDocumento(rs.getInt("RED_DOC_ID"));
                registro2.setAnoReg(rs.getInt("RED_EJE"));
                registro2.setNumReg(rs.getLong("RED_NUM"));
                registro2.setUnidadOrgan(rs.getInt("RED_UOR"));
                registro2.setDptoUsuarioQRegistra(rs.getString("RED_DEP"));
                registro2.setTipoReg(rs.getString("RED_TIP"));
                registro2.setLongitudDocumento(rs.getInt("RED_TAMANHO"));
                registro2.setRutaDocumentoRegistroDisco(null);
                registro2.setEstadoDocumentoRegistro(ConstantesDatos.ESTADO_DOCUMENTO_GRABADO);
                
                String observDoc = rs.getString("RED_OBSERV");
                
                if (observDoc == null)
                    observDoc = "";
                registro2.setObservDoc(observDoc);
                
                
                m_Log.debug("Recuperado documento ID " + registro2.getIdDocumento() + ": " + registro2.getNombreDoc() + " Tipo: " + registro2.getTipoDoc() + " Fecha: " + registro2.getFechaDoc() + " Observaciones: " + registro2.getObservDoc());
                docs.addElement(registro2);
            }
            
            return docs;
            
        } catch (Exception e) {
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        }
    }
    
     public DocumentoValueObject getDocumento(RegistroValueObject registro,Connection con)
            throws AnotacionRegistroException, TechnicalException {

        m_Log.debug("AnotacionRegistroDAO.getDocumento");
        //Queremos estar informados de cuando este metod es ejecutado
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Vector docs = new Vector();

        String nombreFichero = registro.getNombreDoc();
        String extensionFichero = registro.getTipoDoc();
        String fechaFichero = registro.getFechaDoc();


        int ejercicio = registro.getAnoReg();
        Long numeroRegistro = registro.getNumReg();
        DocumentoValueObject doc = new DocumentoValueObject();


        byte[] fichero = null;
        RegistroValueObject registro2;
        try {

            String sql = "SELECT RED_NOM_DOC,RED_TIP_DOC,RED_FEC_DOC,RED_DOC" +
                        " FROM R_RED WHERE " +
                        " RED_NUM=? AND RED_NOM_DOC=? AND RED_TIP_DOC=? AND RED_EJE=?";

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            stmt = con.prepareStatement(sql);
            int i=1;
            stmt.setLong(i++,numeroRegistro);
            stmt.setString(i++,nombreFichero);
            stmt.setString(i++,extensionFichero);
            stmt.setInt(i++,ejercicio);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String nombreDoc = rs.getString(sql_docsNombreDoc);
                doc.setNombre(nombreDoc);
                String fecDoc = rs.getString(3);
                if (fecDoc == null) {
                    fecDoc = "";
                }

                doc.setFecha(fecDoc);
                doc.setExtension(rs.getString("RED_TIP_DOC"));
                InputStream st = rs.getBinaryStream(sql_docsDoc);
                if (st != null) {
                    ByteArrayOutputStream ot = new ByteArrayOutputStream();
                    int c;
                    while ((c = st.read()) != -1) {
                        ot.write(c);
                    }
                    ot.flush();
                    fichero = ot.toByteArray();
                    doc.setFichero(fichero);
                } else {
                    doc.setFichero(new byte[0]);
                }
            }

            return doc;

        } catch (Exception e) {
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        }
    }



	/* *******************************************************************************************
	 * Sólo se pueden modificar o anular (baja lógica) entradas con fecha igual a la que esta
	 * abierto el registro.
	 * Transacción.
	 **********************************************************************************************/
    public RegistroValueObject modify(Connection con, RegistroValueObject registro, boolean digitFinalizada, String[] params)
            throws AnotacionRegistroException, TechnicalException {

        
        String confirmarActualizacion = registro.getRespOpcion();
        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("AnotacionRegistroDAO.modify");        
        AdaptadorSQLBD oad = null;

        // #278739: Obtener entrada relacionada
        String valorEntRel = "";
        String anoEntRel = registro.getEjeEntradaRel();
        String numEntRel = registro.getNumEntradaRel();
        anoEntRel = anoEntRel.trim();
        numEntRel = numEntRel.trim();
        if(!anoEntRel.equals("") && !numEntRel.equals("")){
            valorEntRel = anoEntRel + "/" + numEntRel;
        } else if(anoEntRel.equals("") && !numEntRel.equals("")){
            valorEntRel = numEntRel;
        } else if(!anoEntRel.equals("") && numEntRel.equals("")){
            valorEntRel = anoEntRel;
        }                 
        registro.setEjeEntradaRel(valorEntRel);
        registro.setNumEntradaRel("");
        
        // Obtener los IDEs de las listas.
        String c = getIDELista(registro.getCodTipoDoc(), "R_TDO", sql_codTipoDoc, sql_ideTipoDoc, params);
        if (c != null) {
            registro.setIdTipoDoc(Integer.parseInt(c));
        } else {
            registro.setIdTipoDoc(0);
        }

        if (registro.getCodTipoTransp() != null) {
            c = getIDELista(registro.getCodTipoTransp(), "R_TTR", sql_codTipoTransp, sql_ideTipoTransp, params);
            if (c != null) {
                registro.setIdTransporte(Integer.parseInt(c));
            }
        }

        c = getIDELista(registro.getCodTipoRemit(), "R_TPE", sql_codTipoRemit, sql_ideTipoRemit, params);
        m_Log.debug("#### " + c);
        if (c != null) {
            registro.setIdTipoPers(Integer.parseInt(c));
        } else {
            registro.setIdTipoPers(0);
        }

        if (registro.getCodAct() != null) {
            c = getIDELista(registro.getCodAct(), "R_ACT", sql_codTipoAct, sql_ideTipoAct, params);
            if (c != null) {
                registro.setIdActuacion(Integer.parseInt(c));
            }
        } else registro.setIdActuacion(0);
        //c = getIDELista(registro.getCodTipoDoc(),"R_TDO",sql_codTipoDoc,sql_ideTipoDoc,params);
        Vector idsTemasAsignados = new Vector();
        Vector temas = registro.getListaTemasAsignados();
        String id;

        if (temas != null) {
            for (int i = 0; i < temas.size(); i++) {
                RegistroValueObject rvo;
                rvo = (RegistroValueObject) temas.elementAt(i);
                c = rvo.getCodigoTema();
                //c = (String) temas.elementAt(i);
                id = getIDELista(c, "R_TEM", sql_codTema, sql_idTema, params);
                if (id != null) {
                    idsTemasAsignados.addElement(id);
                }
            }
        }

        Vector docs = registro.getListaDocsAsignados();

        PreparedStatement ps = null, psDelTemas = null, psDelete = null, psDelTerceros = null, psDelDocs =  null;
        try {

            m_Log.debug("dentro del try del modify en el DAO******************");

            oad = new AdaptadorSQLBD(params);

            // Comprobar si el registro está abierto.
            // La entrada tiene la misma fecha que en la que está abierto el registro.
            m_Log.debug("conseguir fecha de entrada a partir de : " + registro.getFecEntrada());
            Date fEntr = Fecha.obtenerDateCompleto2(registro.getFecEntrada());
            m_Log.debug("fecha de entrada es : " + fEntr);
            Date fEntrSinhora=Fecha.obtenerDate(registro.getFecEntrada());
            Date fRegCerrado = RegistroAperturaCierreManager.getInstance().getFechaRegistroCerrado(con, registro);

            /* Enlace con SGE.
             * Fecha: 24/07/2003. */
            boolean existeExpediente = false;
            boolean procMalRelacionado = false;
            TramitacionValueObject tvo = new TramitacionValueObject();
            /* Fin Enlace con SGE. */

            if (fRegCerrado != null && fEntr.compareTo(fRegCerrado) <= 0) {
                registro.setRespOpcion("registro_cerrado");
                m_Log.debug("la fecha registro cerrado es : " + fRegCerrado);
            } else {
                registro.setRespOpcion("registrar_actualizacion_aceptada");
                if ("actualizacion_sin_confirmar".equals(confirmarActualizacion) && registro.getContador() == 0) {
                    String txtFechaOriginal = getFechaOriginal(oad, con, registro);
                    m_Log.debug("la fecha original en BD es : " + txtFechaOriginal);
                    String txtUltimaFechaAnterior = getUltimaFechaAnterior(oad, con, registro, txtFechaOriginal);
                    m_Log.debug("la fecha anterior a esa en BD es : " + txtUltimaFechaAnterior);
                    String txtPrimeraFechaPosterior = getPrimeraFechaPosterior(oad, con, registro, txtFechaOriginal);
                    m_Log.debug("y la fecha posterior a esa en BD es : " + txtPrimeraFechaPosterior);

                    /*Si la fecha de presentación no se modifica no se realizan las comprobaciones. Esto se debe a que
                     la anotación puede no tener una fecha "validada" si se dio de alta permitiendo meter cualquier
                     fecha. Para más señas mirar #45022.*/
                    if (!txtFechaOriginal.equals(Fecha.obtenerStringCompleto(fEntr)) &&
                            m_ConfigRegistro.getString("restriccion_fecha_presentacion").equalsIgnoreCase("SI")){
                    if ((txtUltimaFechaAnterior != null) && (!"".equals(txtUltimaFechaAnterior))) {
                        Date ultimaFechaAnteriorSinhora=Fecha.obtenerDate(txtUltimaFechaAnterior);
                        if (fEntrSinhora.compareTo(ultimaFechaAnteriorSinhora) < 0) {
                            m_Log.debug("ALERTAR USUARIO la fecha de entrada es anterior a la fecha inmediatamente anterior : " + fEntrSinhora + " < " + ultimaFechaAnteriorSinhora);
                            registro.setRespOpcion("registrar_actualizacion_fecha_anterior_sin_confirmar");
                            registro.setFechaComprobacion(txtUltimaFechaAnterior);
                        }
                    }

                    if ((txtPrimeraFechaPosterior != null) && (!"".equals(txtPrimeraFechaPosterior))) {
                        Date primeraFechaPosteriorSinhora=Fecha.obtenerDate(txtPrimeraFechaPosterior);
                        if (fEntrSinhora.compareTo(primeraFechaPosteriorSinhora) > 0) {
                            m_Log.debug("ALERTAR USUARIO la fecha de entrada es posterior a la fecha inmediatamente posterior : " + fEntrSinhora + " > " + primeraFechaPosteriorSinhora);
                            registro.setRespOpcion("registrar_actualizacion_fecha_posterior_sin_confirmar");
                            registro.setFechaComprobacion(txtPrimeraFechaPosterior);
                        }
                    }
                }
                }



                /* Enlace con SGE.
                 * Fecha: 21/07/2003. */

                if ("registrar_actualizacion_aceptada".equals(registro.getRespOpcion())) {

                    tvo.setNumeroExpedienteAntiguo(registro.getNumExpedienteBD());
                    m_Log.debug("ANOTACIONREGISTRODAO. numero de expediente nuevo ::" + tvo.getNumeroExpediente() + ":: numero de esxpediente viejo ::" + tvo.getNumeroExpedienteAntiguo() + "::");
                    if (registro.getNumExpediente() != null) {
                        if (!"".equals(registro.getNumExpediente().trim())) {

                            // SE COMPRUEBA LA EXISTENCIA DEL EXPEDIENTE RELACIONADO ANTES DE PROCEDER A REALIZAR
                            // LA MODIFICACIÓN DE LA ANOTACIÓN
                            ExpedienteRelacionadoVO expediente = new ExpedienteRelacionadoVO();
                            expediente.setCodigoOrganizacion(Integer.toString(registro.getIdOrganizacion()));
                            expediente.setCodProcedimiento(registro.getCodProcedimiento());
                            expediente.setNumExpedienteRelacionado(registro.getNumExpediente());
                            expediente.setParams(params);
                            expediente.setCodigoUsuario(registro.getUsuarioQRegistra());
                            expediente.setCerrarConexionFlexia(false);
                            expediente.setConnection(con);
                            
                            PluginExpedientesRelacionados plugin = PluginExpedientesRelacionadosFactoria.getImplClass(Integer.toString(registro.getIdOrganizacion()));
                            int r = plugin.existeExpediente(expediente);
                            //Solo se le pide que el expediente exista
                            existeExpediente = (r!=4); //
                            procMalRelacionado = (r == 5);

                            tvo.setNumeroExpediente(registro.getNumExpediente());
                            tvo.setCodProcedimiento(registro.getCodProcedimiento());
                        } else {
                            tvo.setNumeroExpediente(null);
                            existeExpediente = true;
                        }
                    } else { // Sin expediente.
                        tvo.setNumeroExpediente(null);
                        existeExpediente = true;
                    }

                    if (!existeExpediente && !procMalRelacionado) {
                        registro.setRespOpcion("no_existe_expediente");
                    } else if (procMalRelacionado) {
                        registro.setRespOpcion("proc_mal_relacionado");
                    } else {
                        registro.setRespOpcion("registrar_actualizacion_aceptada");
                    }

                }

                /* Fin Enlace con SGE. */

                if ("registrar_actualizacion_aceptada".equals(registro.getRespOpcion())) {
                    // Seguimos con la actualizacion
                    // No es un alta desde reserva
                    if (registro.getContador() == 0) {
                        // Obtener VO anterior para comparación en el histórico.
                        RegistroValueObject antiguo = new RegistroValueObject();
                        antiguo.setIdentDepart(registro.getIdentDepart());
                        antiguo.setUnidadOrgan(registro.getUnidadOrgan());
                        antiguo.setTipoReg(registro.getTipoReg());
                        antiguo.setAnoReg(registro.getAnoReg());
                        antiguo.setNumReg(registro.getNumReg());
                        
                        m_Log.debug(" ************** AnotacionRegistroDAO.modify oficinaregistro: " + registro.getCodOficinaRegistro());
                        
                        antiguo.setCodOficinaRegistro(registro.getCodOficinaRegistro());
                        m_Log.debug(" ************** AnotacionRegistroDAO.modify antiguo oficinaregistro: " + antiguo.getCodOficinaRegistro());
                        antiguo = getByPrimaryKey(antiguo,params);
                        antiguo.setCodOficinaRegistro(registro.getCodOficinaRegistro());
						DescripcionRegistroValueObject antiguoVO = null;
						m_Log.debug("CREAR VO PARA HISTORICO ANTES DE MODIFICAR");
						antiguoVO =	crearVOParaHistorico(antiguo, true, antiguo.getCodProcedimientoRoles(), con, params);
						

                        /** Se comprueba si hay que recuperar el código del municipio del procedimiento para poder construir correctamente la
                         *  sentencia de actualización de la anotación
                         */
                        
                        if( !"".equals(registro.getCodProcedimiento())) registro.setMunProcedimiento(antiguo.getMunProcedimiento());
                        m_Log.debug( "  **************************** MODIFY cod procedimiento: " + registro.getCodProcedimiento());
                        m_Log.debug( "  **************************** MODIFY mun procedimiento: " + registro.getMunProcedimiento());
                        
                        if("".equals(registro.getMunProcedimiento()) && !"".equals(registro.getCodProcedimiento()))
                            registro.setMunProcedimiento(DefinicionProcedimientosDAO.getInstance().getCodigoMunicipioProcedimiento(registro.getCodProcedimiento(), con));

                        
                        
                        String sql = construirConsultaModificacion(registro,antiguo.isAsuntoAnotacionBaja(), oad);
                        ps = con.prepareStatement(sql);
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug("======================== " + sql);
                        }
                        int res = ps.executeUpdate();
                        if (res <= 0) {
                            registro.setRespOpcion("modify_no_realizado");
                        } else {
                            registro.setRespOpcion("modify_realizado");
                        }
                        long numReg2 = registro.getNumReg();
                        String sql2 = "DELETE FROM R_RET WHERE " + sql_temasDepto + "=" +
                                registro.getIdentDepart() + " AND " + sql_temasUnid + "=" +
                                registro.getUnidadOrgan() + " AND " + sql_temasTipo + "='" + registro.getTipoReg() +
                                "' AND " + sql_temasEjerc + "=" + registro.getAnoReg() + " AND " +
                                sql_temasNum + "=" + numReg2;
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug("Sentencia SQL DE BORRAR Temas :" + sql2);
                        }
                        psDelTemas = con.prepareStatement(sql2);
                        res = psDelTemas.executeUpdate();
                        m_Log.debug("las filas afectadas en el delete son : " + res);

                        insertarTemasAsignados(con, registro, idsTemasAsignados);
                        insertarRelaciones(con, registro);

                        Vector listaCodTercero = (Vector) registro.getlistaCodTercero();
                        //inserto interesados si hubo algun cambio
                        if (listaCodTercero.size() > 0) {
                            long numReg4 = registro.getNumReg(); 
                            String sql4 = "DELETE FROM R_EXT WHERE " + sql_intecoddepartamento + "=" +
                                    registro.getIdentDepart() + " AND " + sql_intecodUor + "=" +
                                    registro.getUnidadOrgan() + " AND " + sql_intetipo + "='" + registro.getTipoReg() +
                                    "' AND " + sql_inteano + "=" + registro.getAnoReg() + " AND " +
                                    sql_intenumero + "=" + numReg4;
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("Sentencia SQL DE BORRAR Docs :" + sql4);
                            }
                            psDelTerceros = con.prepareStatement(sql4);
                            res = psDelTerceros.executeUpdate();

                            insertarInteresados(con, registro);
                        }

                        // Insertar modificacion en el historico de movimientos
                        HistoricoMovimientoValueObject hvo = new HistoricoMovimientoValueObject();
                        hvo.setCodigoUsuario(Integer.parseInt(registro.getUsuarioQRegistra()));
                        hvo.setTipoEntidad(ConstantesDatos.HIST_ENTIDAD_ANOTACION);
                        hvo.setCodigoEntidad(HistoricoAnotacionHelper.crearClaveHistorico(registro));
						if(registro.isFinDigitalizacion() && !digitFinalizada){
							hvo.setTipoMovimiento(ConstantesDatos.HIST_ANOT_FINALIZAR_MODIFICACION);
							hvo.setDetallesMovimiento(HistoricoAnotacionHelper.crearXMLFinalizacionModificacion());
							HistoricoMovimientoManager.getInstance().insertarMovimientoHistorico(hvo, con, params);
						} 
						hvo.setTipoMovimiento(ConstantesDatos.HIST_ANOT_MODIFICAR);
						m_Log.debug("CREAR VO PARA HISTORICO DESPUES DE MODIFICAR");
						DescripcionRegistroValueObject nuevoVO = crearVOParaHistorico(registro, true, registro.getCodProcedimiento(), con, params);
						hvo.setDetallesMovimiento(HistoricoAnotacionHelper.crearXMLModificacion(antiguoVO, nuevoVO,antiguo.isAsuntoAnotacionBaja()));
						
                        HistoricoMovimientoManager.getInstance().insertarMovimientoHistorico(hvo, con, params);

                    // Es un alta desde reserva
                    } else {

                        // Obtener los IDEs de las listas.
                        c = getIDELista(registro.getCodTipoDoc(), "R_TDO", sql_codTipoDoc, sql_ideTipoDoc, params);
                        if (c != null) {
                            registro.setIdTipoDoc(Integer.parseInt(c));
                        }

                        if (registro.getCodTipoTransp() != null) {
                            c = getIDELista(registro.getCodTipoTransp(), "R_TTR", sql_codTipoTransp, sql_ideTipoTransp, params);
                            if (c != null) {
                                registro.setIdTransporte(Integer.parseInt(c));
                            }
                        }

                        c = getIDELista(registro.getCodTipoRemit(), "R_TPE", sql_codTipoRemit, sql_ideTipoRemit, params);
                        if (c != null) {
                            registro.setIdTipoPers(Integer.parseInt(c));
                        }

                        if (registro.getCodAct() != null) {
                            c = getIDELista(registro.getCodAct(), "R_ACT", sql_codTipoAct, sql_ideTipoAct, params);
                            if (c != null) {
                                registro.setIdActuacion(Integer.parseInt(c));
                            }
                        }

                        int i = insertarAnotacion(oad, con, registro);
                        if (i > 0) {
                            registro.setRespOpcion("modify_realizado");
                            long numRegR = registro.getNumReg(); 
                            String sql3 = "DELETE FROM R_RER WHERE " + sql_codigoDepartamentoRER + "=" +
                                    registro.getIdentDepart() + " AND " + sql_codigoUnidadRER + "=" + registro.getUnidadOrgan() +
                                    " AND " + sql_tipoRER + "='" + registro.getTipoReg() + "' AND " +
                                    sql_ejercicioRER + "=" + registro.getAnoReg() + " AND " +
                                    sql_numeroRER + "=" + numRegR;
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("Sentencia SQL DE BORRAR EN LA TABLA R_RER:" + sql3);
                            }
                            psDelete = con.prepareStatement(sql3);
                            psDelete.executeUpdate();

                            insertarTemasAsignados(con, registro, idsTemasAsignados);
                            insertarInteresados(con, registro);
                            insertarRelaciones(con, registro);

                            // Insertar alta desde reserva en el historico de movimientos
                            HistoricoMovimientoValueObject hvo = new HistoricoMovimientoValueObject();
                            hvo.setCodigoUsuario(Integer.parseInt(registro.getUsuarioQRegistra()));
                            hvo.setTipoEntidad(ConstantesDatos.HIST_ENTIDAD_ANOTACION);
                            hvo.setCodigoEntidad(HistoricoAnotacionHelper.crearClaveHistorico(registro));
                            hvo.setTipoMovimiento(ConstantesDatos.HIST_ANOT_ALTA);
                            m_Log.debug("CREAR VO PARA HISTORICO EN ALTA DESDE RESERVA");
                            DescripcionRegistroValueObject reg = crearVOParaHistorico(registro, true, registro.getCodProcedimiento(), con, params);
                            hvo.setDetallesMovimiento(HistoricoAnotacionHelper.crearXMLAlta(reg));
                            HistoricoMovimientoManager.getInstance().insertarMovimientoHistorico(hvo, con, params);
                        }
                    }
                }
            }

            m_Log.debug("La respuesta es : " + registro.getRespOpcion());
            /* Enlace con SGE.
             * Fecha: 21/07/2003. */
            if (existeExpediente && !((tvo.getNumeroExpedienteAntiguo() == null) && tvo.getNumeroExpediente() == null)) {              
                ExpedienteRelacionadoVO expediente = new ExpedienteRelacionadoVO();
                expediente.setCodigoOrganizacion(Integer.toString(registro.getIdOrganizacion()));
                expediente.setCodigoDepartamento(Integer.toString(registro.getIdentDepart()));
                expediente.setCodigoUnidadRegistro(Integer.toString(registro.getUnidadOrgan()));
                expediente.setTipoRegistro(registro.getTipoReg());
                expediente.setEjercicioRegistro(Integer.toString(registro.getAnoReg()));
                expediente.setNumeroRegistro(Long.toString(registro.getNumReg()));
                expediente.setCodigoTercero(Integer.toString(registro.getCodInter()));
                expediente.setVersionTercero(Integer.toString(registro.getNumModInfInt()));
                expediente.setCodigoDomicilio(Integer.toString(registro.getDomicInter()));
                expediente.setCodProcedimiento(registro.getCodProcedimiento());
                expediente.setNumExpedienteRelacionado(tvo.getNumeroExpediente());
                expediente.setNumExpedienteAntiguo(tvo.getNumeroExpedienteAntiguo());
                expediente.setDejarAnotacionBuzonEntrada(registro.getOpcionPermanencia());
                expediente.setConnection(con);

                PluginExpedientesRelacionados plugin = PluginExpedientesRelacionadosFactoria.getImplClass(Integer.toString(registro.getIdOrganizacion()));
                int r = plugin.insertarExpedienteRelacionado(expediente);
                
                if (r == 1) {
                    registro.setRespOpcion("modify_realizado");
                } else {
                    registro.setRespOpcion("modify_no_realizado");
                }
            }

            /* Cuando el registro de salida se le asocia un expediente se le pone
             * automaticamente aceptada a esa anotación */
            if ("S".equals(registro.getTipoReg()) && !((tvo.getNumeroExpediente()==null) || ("".equals(tvo.getNumeroExpediente())))) {
                aceptarAnotacion(con, registro);
            }

            /* Fin Enlace con SGE. */

            //Queremos estar informados de cuando este metodo ha finalizado
            m_Log.debug("AnotacionRegistroDAO.modify terminado");

        } catch (Exception ex) {
            ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.modificacionDatosRegistro"), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.closeStatement(psDelTemas);
            SigpGeneralOperations.closeStatement(psDelDocs);
            SigpGeneralOperations.closeStatement(psDelTerceros);
            SigpGeneralOperations.closeStatement(psDelete);
        }
        
        return registro;
    }

    public String[] loadSiguienteRER(RegistroValueObject reservaVO, String[] params)
            throws TechnicalException, BDException {

        /* INICIALIZAMOS LAS VARIABLES */
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        long numReg = reservaVO.getNumReg(); 
        String sql = "SELECT RER_EJE,  RER_NUM,RER_FEC " +
                "FROM R_RER " +
                "WHERE RER_DEP =" + reservaVO.getIdentDepart() + " " +
                "AND RER_UOR = " + reservaVO.getUnidadOrgan() + " " +
                "AND RER_TIP ='" + reservaVO.getTipoReg() + "' " +
                "AND RER_NUM > " + numReg + " " +
                "AND RER_EJE >= " + reservaVO.getAnoReg() + " ORDER BY 1, 2";

        try {
            /* Obtenemos la conexion */
            m_Log.debug("A por el OAD");
            abd = new AdaptadorSQLBD(params);
            m_Log.debug("A por la conexion");
            conexion = abd.getConnection();

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            /* EJECUTAR LA QUERY */
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            //sqlExec.execute(sql);
            java.sql.Timestamp dbSqlTimestamp;
            java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm");

            /* OBTENER LOS RESULTADOS: Solo cogemos el primer resultado. */
            String[] salida = new String[3];
            if (rs.next()) {
                salida[0] = rs.getString(1);
                salida[1] = rs.getString(2);
                dbSqlTimestamp = rs.getTimestamp(3);
                String fecha = sdf.format(dbSqlTimestamp);
                salida[2] = fecha;
            }

            return salida;

        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error(e.getMessage());
            }
            throw new TechnicalException(e.getMessage());
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, conexion);
        }
    }

    public void anular(RegistroValueObject registro,boolean digitalizacion, String[] params)
            throws AnotacionRegistroException, TechnicalException {

        m_Log.debug("AnotacionRegistroDAO.anular");

        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = oad.getConnection();
            oad.inicioTransaccion(con); // Inicio transacción

            // Comprobar si el registro está abierto.
            // La entrada tiene la misma fecha que en la que está abierto el registro.
            Date fEntr = Fecha.obtenerDate(registro.getFecEntrada());
            Date fRegCerrado = RegistroAperturaCierreManager.getInstance().getFechaRegistroCerrado(con, registro);

            if (fRegCerrado != null && fEntr.compareTo(fRegCerrado) <= 0) {
                registro.setRespOpcion("registro_cerrado");
            } else {
                registro.setRespOpcion("registrar_actualizacion_aceptada");
                if ("registrar_actualizacion_aceptada".equals(registro.getRespOpcion())) {	// Seguimos con la actualizacion
                    // Actualizar
                    int codigoDepartamento = registro.getIdentDepart();
                    int codigoUnidad = registro.getUnidadOrgan();
                    String tipoRegistro = registro.getTipoReg();
                    int ejercicio = registro.getAnoReg();
                    Long numeroRegistro = registro.getNumReg();
                    int estAnot = registro.getEstAnotacion();
                    String diligencia = registro.getDilAnulacion();
                    int digit = 1;
                    String sql = "UPDATE R_RES SET " + sql_estAnot + "=?," + sql_diligencia + "=?";

                    if (estAnot != 9) {
                        diligencia = null;
                        digit = 0;
                    }

                    if (digitalizacion) {
                        sql += ",FIN_DIGITALIZACION=?";
                    }

                    sql += " WHERE " + sql_codDptoAnotacion + "=? AND "
                            + sql_codUnidadAnotacion + "=? AND "
                            + sql_tipoRegAnotacion + "=? AND "
                            + sql_ejercicioAnotacion + "=? AND "
                            + sql_numeroAnotacion + "=? ";

                    ps = con.prepareStatement(sql);
                    int contbd = 1;
                    ps.setInt(contbd++, estAnot);
                    ps.setString(contbd++, diligencia);
                    if (digitalizacion) {
                        ps.setInt(contbd++, digit);
                    }
                    ps.setInt(contbd++, codigoDepartamento);
                    ps.setInt(contbd++, codigoUnidad);
                    ps.setString(contbd++, tipoRegistro);
                    ps.setInt(contbd++, ejercicio);
                    ps.setLong(contbd++, numeroRegistro);
                    int res = ps.executeUpdate();

                    if (res <= 0) {
                        registro.setRespOpcion("modify_no_realizado");
                    } else {
                        m_Log.debug("query: "+sql);
                        registro.setRespOpcion("modify_realizado");

                        // Insertar anular/desanular en el historico de movimientos
                        HistoricoMovimientoValueObject hvo = new HistoricoMovimientoValueObject();
                        hvo.setCodigoUsuario(Integer.parseInt(registro.getUsuarioQRegistra()));
                        hvo.setTipoEntidad(ConstantesDatos.HIST_ENTIDAD_ANOTACION);
                        hvo.setCodigoEntidad(HistoricoAnotacionHelper.crearClaveHistorico(registro));
                        // Caso de anular
                        if (estAnot == ConstantesDatos.REG_ANOTACION_ESTADO_ANULADA) {
                            hvo.setTipoMovimiento(ConstantesDatos.HIST_ANOT_ANULAR);
                            hvo.setDetallesMovimiento(HistoricoAnotacionHelper.crearXMLAnular(diligencia));
                        // Caso de recuperar anulada
                        } else {
                            hvo.setTipoMovimiento(ConstantesDatos.HIST_ANOT_DESANULAR);
                            hvo.setDetallesMovimiento(HistoricoAnotacionHelper.crearXMLDesanular());
                        }
                        HistoricoMovimientoManager.getInstance().insertarMovimientoHistorico(hvo, con, params);
                    }
                }
            }

            m_Log.debug("la respuesta es : " + registro.getRespOpcion());

            if ("modify_realizado".equals(registro.getRespOpcion())) {
                SigpGeneralOperations.commit(oad, con);
            } else {
                SigpGeneralOperations.devolverConexion(oad, con);
            }

        } catch (Exception ex) {
            SigpGeneralOperations.rollBack(oad, con);
            ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.modifyRegistroValueObject.sql"), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(oad, con);
        }

        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("AnotacionRegistroDAO.anular");
    }

    public Vector getListaDepartamentos(String[] params) throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaDepartamentos");

        Vector list = new Vector();
        ElementoListaValueObject elemListVO = new ElementoListaValueObject("1", "DEPARTAMENTO1", 0);
        list.addElement(elemListVO);

        //Queremos estar informados de cuando este metodo ha finalizado
        m_Log.debug("getListaDepartamentos");
        return list;
    }

    public Vector getListaTiposIdInteresado(String[] params) throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaTiposIdentificadoresInteresado");

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT " + sql_tid_codDocum + "," + sql_tid_descDocum +
                    " FROM  T_TID ORDER BY " + sql_tid_codDocum;

        try {
            con = abd.getConnection();

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO, getListaTiposIdentificadoresInteresado: Sentencia SQL:" + sql);
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            Vector list = new Vector();
            int orden = 0;

            while (rs.next()) {
                ElementoListaValueObject elemListVO = new ElementoListaValueObject(rs.getString(sql_tid_codDocum), rs.getString(sql_tid_descDocum), orden++);
                list.addElement(elemListVO);
            }
                m_Log.debug("AnotacionRegistroDAO, getListaTiposIdentificadoresInteresado: Lista cargada");
                m_Log.debug("AnotacionRegistroDAO, getListaTiposIdentificadoresInteresado: Tamaño lista:" + list.size());
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaTiposIdentificadoresInteresado"), e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, con);
        }
    }

    public String getIndicadorPersonaFisicaJuridica(String codigoDoc, String[] params) throws AnotacionRegistroException, TechnicalException {

        return getIDELista(codigoDoc, "T_TID", sql_tid_codDocum, sql_tid_persFisicJurid, params);

    }

    public String getIDELista(String codigo, String nombreTabla, String atribCodigo, String atribIDE, String[] params)
            throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getIDELista");

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT " + atribIDE + " FROM " + nombreTabla;
        sql = sql + " WHERE " + atribCodigo + "='" + codigo + "'";

        try {
            con = abd.getConnection();

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("getIDELista: Sentencia SQL:" + sql);
            }

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            String ide = null;
            if (rs.next()) {
                ide = rs.getString(atribIDE);
                m_Log.debug("getIDELista: Cargado el registro");
            }
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            return ide;

        } catch (Exception e) {
            e.printStackTrace();
            //Si la lectura tiene problemas tenemos que lanzar y loggear la excepcion
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getIDELista"), e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, con);
        }
    }


    /* ***********************************************************************************
    Función: getListaTemas.
    Recupera de la base de datos los temas que pueden asignarse a las entradas.
     ************************************************************************************** */
    public Vector getListaTemas(String[] params)
            throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getListaTemas");

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT " + sql_codTema + "," + sql_descTema + " FROM  R_TEM";

        try {
            con = abd.getConnection();

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO, getListaTemas: Sentencia SQL:" + sql);
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            Vector list = new Vector();
            int orden = 0;
            while (rs.next()) {
                ElementoListaValueObject elemListVO = new ElementoListaValueObject(rs.getString(sql_codTema), rs.getString(sql_descTema), orden++);
                list.addElement(elemListVO);
            }
            m_Log.debug("AnotacionRegistroDAO, getListaTemas: Lista temas cargada");

            return list;

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaTemas"), e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(abd, con);
        }
    }

    /* *************************************************************************************
    Función: insertarEntrada.
    Insertar la entrada que se quiere dar de alta.
    La inserción se realiza dentro de la transacción abierta por la conexión c.
    Lanza al método que lo invoca las excepciones SQL que genera.
     ************************************************************************************* */
    public int insertarAnotacion(AdaptadorSQLBD oad, Connection c, RegistroValueObject reg)
            throws AnotacionRegistroException, TechnicalException {

        m_Log.debug("AnotacionRegistroDAO.insertarAnotacion");
        m_Log.debug("AnotacionRegistroDAO.insertarAnotacion.Codigo de la oficina de registro:"+ reg.getCodOficinaRegistro());
        PreparedStatement ps = null;

        try {
			
            String sql = "INSERT INTO R_RES ( ";
            sql += sql_codDptoAnotacion + "," + sql_codUnidadAnotacion + "," + sql_tipoRegAnotacion;
            sql += "," + sql_ejercicioAnotacion + "," + sql_numeroAnotacion + "," + sql_fechaAnotacion;
            sql += "," + sql_fechaDocumento + "," + sql_asuntoAnotacion + "," + sql_tipoAnotacionDestino;
            sql += "," + sql_tipoDocAnotacion + "," + sql_tipoRemitenteAnotacion + "," + sql_codTerceroAnotacion;
            sql += "," + sql_domicTerceroAnotacion;
            sql += "," + sql_numModifInteresado;
            sql += "," + sql_tipoTransporte;
            sql += "," + sql_numTransp;
            sql += "," + sql_actuacion;
            // --> Destino
            // tiruritata
            sql += /*", "+ sql_departOrigDest +*/ "," + sql_unidOrigDest;
            sql += "," + sql_orgDestAnot + "," + sql_entidadDestino;
            // tiruritata
            sql += /*"," + sql_dptoDestino + */ "," + sql_unidRegDestino;
            sql += "," + sql_estAnot + "," + sql_diligencia;
            // -->Origen
            sql += "," + sql_orgOrigAnot + "," + sql_departOrigAnot + "," + sql_unidOrigAnot + "," +
                    sql_entOrigAnot + "," + sql_tipoOrigDest + "," + sql_numOrig + "," + sql_ejeOrig +
                    "," + sql_entradaRelacionada;
            // --> Datos del que registra.
            sql += "," + sql_res_usuarioQRegistra + "," + sql_res_dptoUsuarioQResgistra + "," + sql_res_unidOrgUsuarioQRegistra;
            // --> Datos de la anotacion que contesta si es el caso.
            sql += ", " + sql_res_depContestada + ", " + sql_res_uorContestada + ", " + sql_res_tipContestada + ", " + sql_res_ejeContestada + ", " + sql_res_numContestada;
            sql += "," + sql_res_observaciones + "," + sql_autoridad + "," + sql_procedimiento;
            sql += "," + sql_munProcedimiento + ", ASUNTO,RES_FEDOC, RES_OFI, FIN_DIGITALIZACION";
            sql += " ) VALUES ( ";

            // sql_codDptoAnotacion y sql_codUnidadAnotacion
            sql += reg.getIdentDepart() + "," + reg.getUnidadOrgan() + ",";
            // sql_tipoRegAnotacion y sql_ejercicioAnotacion
            sql += "'" + reg.getTipoReg() + "'," + reg.getAnoReg() + ",";
            // sql_numeroAnotacion
            long numReg = reg.getNumReg(); 
            sql += numReg + ",";
            // sql_fechaAnotacion
            if (reg.getFecEntrada().length() > 11) {
                sql += oad.convertir("'" + reg.getFecEntrada() + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY HH24:MI:SS") + ",";
            //sql += "TO_DATE(TO_CHAR(sysdate,'DD/MM/YYYY HH24:MI:SS'),'DD/MM/YYYY HH24:MI:SS'),";
            } else {
                // LA FECHA DE ENTRADA CONSISTE SOLO DE DIA. SE ANHADE LA HORA DE LA BBDD.
                sql += oad.convertir(
                        oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                        new String[]{"'" + reg.getFecEntrada() + "'",
                    oad.convertir(
                    oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null),
                    AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,
                    "HH24:MI:SS")
                }),
                        AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,
                        "DD/MM/YYYY HH24:MI:SS") + ",";

            }
            //FECHA DE GRAVACION
            sql += oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ",";
            // sql_fechaDocumento
          /*  if (reg.getFecHoraDoc().length() > 11) {
                sql += oad.convertir("'" + reg.getFecHoraDoc() + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY HH24:MI:SS") + ",";
            } else {
                /*sql += oad.funcionCadena(oad.FUNCIONCADENA_CONCAT,
                new String[]{oad.convertir("'" + reg.getFecHoraDoc() + "'",
                oad.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY"), oad.convertir(
                oad.funcionFecha(oad.FUNCIONFECHA_SYSDATE, null),
                oad.CONVERTIR_COLUMNA_TEXTO,  "HH24:MI:SS")}) + ",";
                sql += oad.convertir(
                        oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                        new String[]{"'" + reg.getFecHoraDoc() + "'",
                    oad.convertir(
                    oad.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null),
                    AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO,
                    "HH24:MI:SS")
                }),
                        AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA,
                        "DD/MM/YYYY HH24:MI:SS") + ",";
            }*/
            //sql += "TO_DATE('"+reg.getFecHoraDoc()+"','DD/MM/YYYY HH24:MI:SS'),";
            // sql_asuntoAnotacion

            //No permitir que se guarden en BBDD caracteres ASCII no imprimibles (caracteres ascii de control, excepto salto de linea, tab...)   
            String asunto=reg.getAsunto();
            asunto=StringUtils.escapeCP1252(asunto, "ISO-8859-1");
            sql += "'" + TransformacionAtributoSelect.replace(asunto, "'", "''") + "' ,";
            // sql_tipoAnotacionDestino
            sql += reg.getTipoAnot() + ",";
            // sql_tipoDocAnotacion
            if (reg.getIdTipoDoc() == -1 || (reg.getTipoAnot() == 1 && "E".equalsIgnoreCase(reg.getTipoReg()))) { // Con TipoReg="E" Entrada y TipoAnot=1 'Destino otro registro' no hay tipoDoc
                sql += " null,";
            } else {
                sql += reg.getIdTipoDoc() + ",";
            }
            // sql_tipoRemitenteAnotacion
            if (reg.getIdTipoPers() == 0) {
                sql += "null,";
            } else {
                sql += reg.getIdTipoPers() + ",";
            }
            // sql_codTerceroAnotacion
            sql += reg.getCodInter() + ",";
            // sql_domicTerceroAnotacion
            sql += reg.getDomicInter() + ",";
            // sql_numModifInteresado
            sql += reg.getNumModInfInt();
            // --> Atributos que pueden ser nulos.
            // sql_tipoTransporte
           if (reg.getCodTipoTransp() != null && !"0".equals(reg.getCodTipoTransp())) {
               if (reg.getIdTransporte() != 0) {
                    sql += "," + reg.getIdTransporte(); 
               }else{
                    sql += ", null";
               }
            } else {
                sql += ", null";
            }
            // sql_numTransp
            if (reg.getNumTransporte() != null) {
                sql += ",'" + reg.getNumTransporte() + "'";
            } else {
                sql += ", null";
            }
            // sql_actuacion
            if (reg.getCodAct() != null) {
                sql += "," + reg.getIdActuacion();
            } else {
                sql += ", null";
            }
            // --> Destino

            if (reg.getTipoAnot() == 0 || reg.getTipoAnot() == 2) { // Ordinaria.

                if (reg.getIdUndTramitad() != null && !reg.getIdUndTramitad().trim().equals("")) {
                    sql += "," + reg.getIdUndTramitad();
                } else {
                    sql += ",null";
                }

                sql += ",null,null,null";

            } else { // A otro registro.

                m_Log.debug("AnotacionRegistroDAO, codigo UOR:" + reg.getIdUndTramitad());
                if (reg.getIdUndTramitad() != null && !reg.getIdUndTramitad().trim().equals("")) {
                    sql += "," + reg.getIdUndTramitad();
                } else {
                    sql += ",null";
                }
                // sql_orgDestAnot
                if (reg.getOrgDestino() == null) {
                    sql += ",null";
                } else {
                    sql += "," + reg.getOrgDestino();
                }
                // sql_entidadDestino
                if (reg.getOrgEntDest() == null) {
                    sql += ",null";
                } else {
                    sql += "," + reg.getOrgEntDest();
                }
                // sql_unidRegDestino
                if (reg.getIdUndRegDest() == null) {
                    sql += ",null";
                } else {
                    sql += "," + reg.getIdUndRegDest();
                }
            }

            // Estado de la anotacion
            // Se permite insertar una anotacion en cualquier estado.
            sql += ", " + reg.getEstAnotacion();

            // Diligencia de anulación
            if (reg.getDilAnulacion() != null) {
                sql += ",'" + reg.getDilAnulacion() + "'";
            } else {
                sql += ", null";
            }

            // --> Origen
            if (reg.getOrganizacionOrigen() == null || reg.getOrganizacionOrigen().equals("")) {
                sql += ",null";
            } else {
                sql += "," + reg.getOrganizacionOrigen();
            }
            if (reg.getIdDepOrigen() == 0) {
                sql += ",null";
            } else {
                sql += "," + reg.getIdDepOrigen();
            }
            if (reg.getIdUndRegOrigen() == null || reg.getIdUndRegOrigen().equals("")) {
                sql += ",null";
            } else {
                sql += "," + reg.getIdUndRegOrigen();
            }
            if (reg.getOrgEntOrigen() == 0) {
                sql += ",null";
            } else {
                sql += "," + reg.getOrgEntOrigen();
            }
            sql += ",null,null,null";
            m_Log.debug("en el DAO la entrada relacionada es : " + reg.getEjeEntradaRel());
            if (reg.getEjeEntradaRel()== null || reg.getEjeEntradaRel().equals("")) {
                sql += ",null";
            } else {
                sql += ",'" + reg.getEjeEntradaRel() + "'";
            }
            // --> Datos del que registra.
            sql += "," + reg.getUsuarioQRegistra() + "," + reg.getDptoUsuarioQRegistra() + "," + reg.getUnidOrgUsuarioQRegistra();
            // --> Anotacion contestada.
            if ((reg.getEjercicioAnotacionContestada() != null) && (reg.getNumeroAnotacionContestada() != null)) {
                if ((!"".equals(reg.getEjercicioAnotacionContestada())) && (!"".equals(reg.getNumeroAnotacionContestada()))) {
                    sql += "," + reg.getIdentDepart() + "," + reg.getUnidadOrgan() + "," + "'E'" + "," + reg.getEjercicioAnotacionContestada() + "," + reg.getNumeroAnotacionContestada();
                } else {
                    sql += ", null, null, null, null, null ";
                }
            } else {
                sql += ", null, null, null, null, null ";
            }
            String observaciones=reg.getObservaciones();
            
            observaciones=StringUtils.escapeCP1252(observaciones, "ISO-8859-1");
                    
            sql += ",'" + TransformacionAtributoSelect.replace(observaciones, "'", "''") + "'";

            if ((reg.getAutoridad() == null) || (reg.getAutoridad().equals(""))) {
                sql += ",null";
            } else {
                sql += "," + "'" + reg.getAutoridad() + "'";
            }

            // Procedimiento
            String munProcedimiento = reg.getMunProcedimiento();
            if (munProcedimiento == null || munProcedimiento.equals("")) {
                munProcedimiento = "0";
            }
            if (reg.getCodProcedimiento() != null) {
                sql += ",'" + reg.getCodProcedimiento() + "'," + munProcedimiento;
            } else {
                sql += ", null," + munProcedimiento;
            }

            // Asunto codificado
            String codAsunto = reg.getCodAsunto();
            if (codAsunto == null) {
                codAsunto = "";
            }
            sql += ",'" + codAsunto + "',";

            if (reg.getFechaDocu() == null || "".equals(reg.getFechaDocu())) sql += " NULL";
            else
                sql += oad.convertir("'" + reg.getFechaDocu() + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY");
             m_Log.debug("\n Antes de hacer el geTCodOficinaRegistro");
            sql +=", " + reg.getCodOficinaRegistro();
            sql += ", " + JdbcOperations.convertBooleanToIntegerForDB(reg.isFinDigitalizacion()); 

            sql += ")";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO, insertar anotacion:" + sql);
            }

            ps = c.prepareStatement(sql);

            return ps.executeUpdate();

        } catch (SQLException ex) {
            //ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.insercionDatosRegistro"), ex);
        } catch (Exception ex) {
            //ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.insercionDatosRegistro"), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }

    }

    /* *************************************************************************************
    Función: insertarTemasAsignados.
    Insertar los temas asignados a una entrada al realizar su alta.
    La inserción se realiza dentro de la transacción abierta por la conexión c.
    Lanza al método que lo invoca las excepciones SQL que genera.
     ************************************************************************************* */
    private void insertarTemasAsignados(Connection c, RegistroValueObject reg, Vector list)
            throws AnotacionRegistroException, TechnicalException {

        m_Log.debug("insertarTemasAsignados");
        PreparedStatement ps = null;
        long numReg = reg.getNumReg(); 
        try {
            // Común para todos.
            String sql = "INSERT INTO R_RET ( ";
            sql += sql_temasDepto + "," + sql_temasUnid + "," + sql_temasEjerc + "," + sql_temasNum;
            sql += "," + sql_temasTipo + "," + sql_temasOrden + "," + sql_temasIdTema + ") VALUES (";
            sql += reg.getIdentDepart() + "," + reg.getUnidadOrgan() + ",";
            sql += reg.getAnoReg() + "," + numReg + ",";
            sql += "'" + reg.getTipoReg() + "',";

            for (int i = 0; i < list.size(); i++) {

                String idTema = (String) list.elementAt(i);
                String sql2 = sql + i + "," + idTema + ")";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("insertarListaTemas: Sentencia SQL:" + sql2);
                }
                ps = c.prepareStatement(sql2);
                ps.executeUpdate();
                m_Log.debug("insertarListaTemas: Inserción realizada");
                SigpGeneralOperations.closeStatement(ps);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.insertarTemasAnotacion.sql"), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    
    /**
     * Inserta los documentos asociados a un registro
     *
     * @param con Conexión con la BD
     * @param docsReg Lista de documentos a insertar     
     * @throws AnotacionRegistroException. En caso de que se produzca algún error.
     */
    public void insertarDocsRegistro(Connection con, ArrayList<Documento> docsReg)
            throws AnotacionRegistroException {
        
        PreparedStatement ps = null;
        Statement st = null;
        
        try {

            for (int i = 0; i < docsReg.size(); i++) {
                             
                Documento docReg = docsReg.get(i);
                byte[] valorDato = null;
                int tamanhoFichero = 0;
                
                if(docReg.getFichero()!=null){
                    valorDato = docReg.getFichero();
                    tamanhoFichero = valorDato.length;
                }
                
		// Insertar el metadato CSV en la tabla de metadatos
                MetadatosDocumentoVO metadatos = new MetadatosDocumentoVO();
                metadatos.setCsv(docReg.getMetadatoDocumentoCsv());
                metadatos.setCsvAplicacion(docReg.getMetadatoDocumentoCsvAplicacion());
                metadatos.setCsvUri(docReg.getMetadatoDocumentoCsvUri());
                
                Long idMetadatos = null;
                if (org.apache.commons.lang.StringUtils.isNotEmpty(metadatos.getCsv())) {
                    m_Log.debug("Insertando metadatos CSV...");
                    
                    idMetadatos = DocumentoDAO.getInstance().insertarMetadatoCSV(metadatos, con, docReg.getParams());
                }
                
                String sql = "INSERT INTO R_RED (RED_DEP, RED_UOR, RED_EJE, RED_NUM, RED_TIP, RED_NOM_DOC, RED_TIP_DOC, RED_DOC, RED_FEC_DOC,RED_ENT,RED_TAMANHO, RED_ID_METADATO, RED_IDDOC_GESTOR) ";
                sql += "VALUES (" + docReg.getCodigoDepartamento() + "," + docReg.getCodigoUnidadOrganica() + ",";
                sql += docReg.getEjercicioAnotacion() + "," + docReg.getNumeroRegistro() + ",";
                sql += "'" + docReg.getTipoRegistro() + "','";
                
                m_Log.debug("Nombre/Titulo: " + docReg.getNombreDocumento());                 
                m_Log.debug("TipoDoc: " + docReg.getTipoDocumento());                
                

                String strAux=""; 
                if(("S".equals(docReg.getEntregado()))||("SI".equals(docReg.getEntregado()))) strAux="S";
                else if(("N".equals(docReg.getEntregado())) || ("NO".equals(docReg.getEntregado()))) strAux = "N";
                else strAux="";

                String sql2 = sql + docReg.getNombreDocumento() + "','" + docReg.getTipoDocumento() + "', ?, ?,'"+strAux+"'," + tamanhoFichero + ", ?, ?)";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("insertarDocsRegistro: Sentencia SQL:" + sql2);
                }

                ps = con.prepareStatement(sql2);
                int contbd = 1;
                
                // Creamos un stream de lectura a partir del buffer
                if(valorDato!=null){
                    ByteArrayInputStream bais = new ByteArrayInputStream(valorDato);
                    ps.setBinaryStream(contbd++, bais, valorDato.length);
                }else{
                    ps.setNull(contbd++,java.sql.Types.BINARY);
                }
                
                
                if (docReg.getFechaDocumento() != null && !"".equals(docReg.getFechaDocumento())) {
                    SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
                    Date dt = parser.parse(docReg.getFechaDocumento());
                    java.sql.Date sqlDate = new java.sql.Date(dt.getTime());
                    ps.setDate(contbd++, sqlDate);
                } else {
                    ps.setNull(contbd++, java.sql.Types.TIMESTAMP);
                }
                //RED_ID_METADATO, RED_IDDOC_GESTOR
                JdbcOperations.setValues(ps, contbd, idMetadatos, docReg.getIdDocGestor());
                
                ps.executeUpdate();
                SigpGeneralOperations.closeStatement(ps);
                // Se insertan los metadatos si existen (si cualquiera de los valores no es nulo (salvo ID_DOCUMENTO)
                if (docReg.getVersionNTIMetadatos() != null) {
                    insertarMetadatosDocsRegistro(docReg, con);
                }
                
            }

        } catch (ParseException pe) {
           m_Log.error("insertarDocsRegistro: " + pe.getMessage());  
            pe.printStackTrace();            
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.insertarDocumentos.sql"), pe);
        } catch (SQLException ex) {
            m_Log.error("insertarDocsRegistro: " + ex.getMessage());  
            ex.printStackTrace();            
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.insertarDocumentos.sql"), ex);
        } catch (Exception e) {
            m_Log.error("insertarDocsRegistro: " + e.getMessage());  
            e.printStackTrace();            
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.insertarDocumentos.sql"), e);            
        } finally {
            try{
                SigpGeneralOperations.closeStatement(ps);
                SigpGeneralOperations.closeStatement(st);
            }catch(TechnicalException e){
                m_Log.error("Error al cerrar recurso asociado a la conexión de base de datos: " + e.getMessage());
            }
        }
    }
    
    /**
    *Inserta los datos de los documentos aportados anteriormente a una anotación de registro en la tabla R_DOC_APORTADOS_ANTERIOR
    * @param con Conexión con la BD
    * @param docsAntReg Lista de documentos a insertar
    * @throws AnotacionRegistroException. En caso de que se produzca algún error
    */
    public void insertarDocsEntregadosAnterior(Connection con, ArrayList<RegistroValueObject> docsReg)
            throws AnotacionRegistroException, TechnicalException, ParseException{
        PreparedStatement ps=null;
        try{
            for(int i=0; i<docsReg.size(); i++){
                RegistroValueObject docReg=docsReg.get(i);
                String sql="INSERT INTO R_DOC_APORTADOS_ANTERIOR(R_DOC_APORTADOS_DEP, R_DOC_APORTADOS_UOR, R_DOC_APORTADOS_EJE,R_DOC_APORTADOS_NUM, "
                        + "R_DOC_APORTADOS_TIP, R_DOC_APORTADOS_NOM_DOC,R_DOC_APORTADOS_TIP_DOC,R_DOC_APORTADOS_ORGANO,R_DOC_APORTADOS_FEC_DOC) VALUES ("; 
                sql+=docReg.getIdentDepart()+","+docReg.getUnidadOrgan()+",";
                sql+=docReg.getAnoReg()+","+docReg.getNumReg()+",'";
                sql+=docReg.getTipoReg()+"',";
                m_Log.debug("Nombre: "+docReg.getNombreDocAnterior());
                m_Log.debug("Tipo Documento:"+docReg.getTipoDocAnterior());
               
                 String sql2 = sql +"'"+ docReg.getNombreDocAnterior() + "','" + docReg.getTipoDocAnterior()+"','"+docReg.getOrganoDocAnterior()+"',?)";
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("insertarDocsRegistro: Sentencia SQL:" + sql2);
                }
                 ps = con.prepareStatement(sql2);
                
                if (docReg.getFechaDocAnterior() != null) {
                    SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
                    Date dt = parser.parse(docReg.getFechaDocAnterior());
                    java.sql.Date sqlDate = new java.sql.Date(dt.getTime());
                    ps.setDate(1, sqlDate);
                } else {
                    ps.setNull(1, java.sql.Types.TIMESTAMP);
                }

                ps.executeUpdate();
                SigpGeneralOperations.closeStatement(ps);
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.insertarDocEntregadosAnterior.sql"), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
      
        }
    }
    
    
     /**
     * Inserta los documentos asociados a una anotación de registro en la tabla R_RED, pero sin 
     * almacenar el archivo en base de datos, porque se envía a un gestor documental, como puede ser alfresco     
     * @param con Conexión con la BD
     * @param docsReg Lista de documentos a insertar     
     * @throws AnotacionRegistroException. En caso de que se produzca algún error.
     */
    public void insertarDocsRegistroSinBinario(Connection con, ArrayList<Documento> docsReg)
            throws AnotacionRegistroException {
        
        PreparedStatement ps = null;

        try {

            for (int i = 0; i < docsReg.size(); i++) {
                             
                Documento docReg = docsReg.get(i);
                byte[] valorDato = null;
                int tamanhoFichero = docReg.getLongitudDocumento();
                
                // Insertar el metadato CSV en la tabla de metadatos
	
                MetadatosDocumentoVO metadatos = new MetadatosDocumentoVO();	
                metadatos.setCsv(docReg.getMetadatoDocumentoCsv());	
                metadatos.setCsvAplicacion(docReg.getMetadatoDocumentoCsvAplicacion());	
                metadatos.setCsvUri(docReg.getMetadatoDocumentoCsvUri());	
                	
                Long idMetadatos = null;	
                if (org.apache.commons.lang.StringUtils.isNotEmpty(metadatos.getCsv())) {	
                    m_Log.debug("Insertando metadatos CSV...");	
                    	
                    idMetadatos = DocumentoDAO.getInstance().insertarMetadatoCSV(metadatos, con, docReg.getParams());	
                }
                
                String sql = "INSERT INTO R_RED (RED_DEP, RED_UOR, RED_EJE, RED_NUM, RED_TIP, RED_NOM_DOC, RED_TIP_DOC, RED_DOC, "
						+ "RED_FEC_DOC,RED_ENT,RED_TAMANHO,RED_ID_METADATO,RED_DOCDIGIT,RED_IDDOC_GESTOR) VALUES ("; 
                sql += docReg.getCodigoDepartamento() + "," + docReg.getCodigoUnidadOrganica() + ",";
                sql += docReg.getEjercicioAnotacion() + "," + docReg.getNumeroRegistro() + ",";
                sql += "'" + docReg.getTipoRegistro() + "','";
                
                m_Log.debug("Nombre/Titulo: " + docReg.getNombreDocumento());                 
                m_Log.debug("TipoDoc: " + docReg.getTipoDocumento());
                m_Log.debug("Fecha documento: " + docReg.getFechaDocumento());

                String strAux=""; 
                if(("S".equals(docReg.getEntregado()))||("SI".equals(docReg.getEntregado()))) strAux="S";
                else if(("N".equals(docReg.getEntregado())) || ("NO".equals(docReg.getEntregado()))) strAux = "N";
                else strAux="";

                String sql2 = sql + docReg.getNombreDocumento() + "','" + docReg.getTipoDocumento() + "', ?, ?,'"+strAux+"'," + tamanhoFichero + ", ?, 1, ?)";
                
                m_Log.info("insertarDocsRegistro: Sentencia SQL:" + sql2);
                
                ps = con.prepareStatement(sql2);
                
                // Se inserta un NULL en el campo RED_DOC 
                ps.setNull(1,java.sql.Types.BINARY);
                
                
                
                if (docReg.getFechaDocumento() != null && !"".equals(docReg.getFechaDocumento())) {
                    Timestamp datetime = DateOperations.toTimestamp(docReg.getFechaDocumento(), DateOperations.LATIN_DATETIME_FORMAT);
                    if(datetime == null){
                        datetime = DateOperations.toTimestamp(docReg.getFechaDocumento(), DateOperations.LATIN_DATE_FORMAT);
                    }
                    ps.setTimestamp(2, datetime);
                } else {
                    ps.setNull(2, java.sql.Types.TIMESTAMP);
                }
				
                JdbcOperations.setValues(ps, 3, idMetadatos);
				
                // Insertamos idDocGestor, que puede ser nulo
                if(docReg.getIdDocGestor()!=null)
                    ps.setString(4, docReg.getIdDocGestor());
                else
                    ps.setNull(4, java.sql.Types.VARCHAR);

                ps.executeUpdate();
                SigpGeneralOperations.closeStatement(ps);
                 // Se insertan los metadatos si existen (si cualquiera de los valores de metadatos no es nulo (salvo ID_DOCUMENTO)
                if (docReg.getVersionNTIMetadatos() != null) {
                    insertarMetadatosDocsRegistro(docReg, con);
                }
            }

        } catch (SQLException ex) {
            m_Log.error("insertarDocsRegistro: " + ex.getMessage(),ex);  
            ex.printStackTrace();            
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.insertarDocumentos.sql"), ex);
        } catch (Exception e) {
            m_Log.error("insertarDocsRegistro: " + e.getMessage(),e);  
            e.printStackTrace();            
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.insertarDocumentos.sql"), e);            
        } finally {
            try{
                SigpGeneralOperations.closeStatement(ps);
            }catch(TechnicalException e){
                m_Log.error("Error al cerrar recurso asociado a la conexión de base de datos: " + e.getMessage(),e);
            }
        }
    }
     
        /**
     * Elimina los metadatos de un determinado documento de registro.
     * @param doc Documento
     * @param con Conexi\F3n con la BD
     * @throws AnotacionRegistroException. En caso de que se produzca alg\FAn error.
     */
    public void insertarMetadatosDocsRegistro(Documento doc, Connection con) throws AnotacionRegistroException {
        m_Log.debug("insertarMetadatosDocsRegistro ===>");        
        PreparedStatement pst = null;
        AdaptadorSQLBD oad = new AdaptadorSQLBD(doc.getParams());
        
        try {
            StringBuilder sql = new StringBuilder();
            if (ConstantesDatos.ORACLE.equalsIgnoreCase(oad.getTipoGestor())) {
                sql.append("INSERT INTO METADATO_DOC_COTEJADOS (DEPARTAMENTO,UOR,EJERCICIO,NUMERO,TIPO_REGISTRO,NOMBRE_DOC,VERSION_NTI,ID_DOCUMENTO,ORGANO,FECHA_CAPTURA,ORIGEN,ESTADO_ELABORACION,NOMBRE_FORMATO,TIPO_DOCUMENTAL,TIPO_FIRMA) ")
                   .append(" VALUES (?,?,?,?,?,?,?,metadato_id_documento.NEXTVAL,?,?,?,?,?,?,?) ");
            } else if (ConstantesDatos.SQLSERVER.equalsIgnoreCase(oad.getTipoGestor())) {
                sql.append("INSERT INTO METADATO_DOC_COTEJADOS (DEPARTAMENTO,UOR,EJERCICIO,NUMERO,TIPO_REGISTRO,NOMBRE_DOC,VERSION_NTI,ORGANO,FECHA_CAPTURA,ORIGEN,ESTADO_ELABORACION,NOMBRE_FORMATO,TIPO_DOCUMENTAL,TIPO_FIRMA) ")
                   .append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
            }

            m_Log.debug("Sentencia SQL DE INSERCION Metadatos de Docs :" + sql.toString());
            pst = con.prepareStatement(sql.toString());
            
            JdbcOperations.setValues(pst, 1, 
                    doc.getCodigoDepartamento(),
                    doc.getCodigoUnidadOrganica(),
                    doc.getEjercicioAnotacion(),
                    doc.getNumeroRegistro(),
                    doc.getTipoRegistro(),
                    doc.getNombreDocumento(),
                    doc.getVersionNTIMetadatos(),
                    doc.getOrganoMetadatos(),
                    DateOperations.toTimestamp(doc.getFechaCapturaMetadatos()),
                    doc.getOrigenMetadatos(),
                    doc.getEstadoElaboracionMetadatos(),
                    doc.getNombreFormatoMetadatos(),
                    doc.getTipoDocumentalMetadatos(),
                    doc.getTipoFirmaMetadatos());
            
            int res = pst.executeUpdate();
            
            m_Log.debug("las filas afectadas en el delete son : " + res);
        } catch (SQLException ex) {
            m_Log.error("insertarMetadatosDocsRegistro: " + ex.getMessage());  
            ex.printStackTrace();            
            throw new AnotacionRegistroException("Se ha producido un error al insertar los metadatos de un documento", ex);
        } catch (Exception e) {
            m_Log.error("insertarMetadatosDocsRegistro: " + e.getMessage());  
            e.printStackTrace();            
            throw new AnotacionRegistroException("Se ha producido un error al insertar los metadatos de un documento", e);            
        } finally {
            try{
                SigpGeneralOperations.closeStatement(pst);
            }catch(TechnicalException e){
                m_Log.error("Error al cerrar recursos asociados a la conexion de base de datos: " + e.getMessage());
            }
        }
    }
    
    
    /**
     * Elimina los documentos asociados a un registro
     *
     * @param con Conexión con la BD
     * @param regVO Registro
     * @param params Parametros de la conexion
     * @throws AnotacionRegistroException. En caso de que se produzca algún error.
     */
    public void eliminarDocsRegistro(Connection con, RegistroValueObject regVO, String[] params)
            throws AnotacionRegistroException, TechnicalException {

        m_Log.debug("insertarDocsRegistro");
        
        PreparedStatement ps = null;

        try {

            long numReg3 = regVO.getNumReg(); 
            String sql3 = "DELETE FROM R_RED WHERE " + sql_docsDepto + "=" +
                    regVO.getIdentDepart() + " AND " + sql_docsUnid + "=" +
                    regVO.getUnidadOrgan() + " AND " + sql_docsTipo + "='" + regVO.getTipoReg() +
                    "' AND " + sql_docsEjerc + "=" + regVO.getAnoReg() + " AND " +
                    sql_docsNum + "=" + numReg3;
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Sentencia SQL DE BORRAR Docs :" + sql3);
            }
            ps = con.prepareStatement(sql3);
            int res = ps.executeUpdate();
            m_Log.debug("las filas afectadas en el delete son : " + res);
            

        } catch (SQLException ex) {
            m_Log.error("eliminarDocsRegistro: " + ex.getMessage());  
            ex.printStackTrace();            
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.eliminarDocumentos.sql"), ex);
        } catch (Exception e) {
            m_Log.error("eliminarDocsRegistro: " + e.getMessage());  
            e.printStackTrace();            
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.eliminarDocumentos.sql"), e);            
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /* *************************************************************************************
    Función: inserta los intesados.
    Insertar los interesados pertenecientes a este registro en R_EXT
    La inserción se realiza dentro de la transacción abierta por la conexión c.
    Lanza al método que lo invoca las excepciones SQL que genera.
     ************************************************************************************* */
    public void insertarInteresados(Connection c, RegistroValueObject reg)
            throws AnotacionRegistroException, TechnicalException {

        m_Log.debug("insertarInteresados");

        PreparedStatement psInsert = null, psUpdate = null;
        long numReg = reg.getNumReg();
        try {

            Vector listaCodTercero = (Vector) reg.getlistaCodTercero();
            Vector listaVersionTercero = (Vector) reg.getlistaVersionTercero();
            Vector listaCodDomicilio = (Vector) reg.getlistaCodDomicilio();
            Vector listaRol = (Vector) reg.getlistaRol();

            if (listaCodTercero.size() > 0) {
                for (int i = 0; i < listaCodTercero.size(); i++) {
                    String sql = "INSERT INTO R_EXT ( " + sql_intecodUor + "," + sql_intetipo + "," + sql_inteano + "," + sql_intenumero + "," +
                            sql_intecoddepartamento + "," + sql_intecodTercero + "," + sql_inteverTercero + "," + sql_intecodDomicilio + "," + sql_interolTercero + ") VALUES (" + reg.getUnidadOrgan() + ",'" + reg.getTipoReg() +
                            "'," + reg.getAnoReg() + "," + numReg + "," + reg.getIdentDepart() + "," +
                            listaCodTercero.elementAt(i) + "," + listaVersionTercero.elementAt(i) + "," +
                            listaCodDomicilio.elementAt(i) + "," + listaRol.elementAt(i) + ")";
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug(sql);
                    }
                    psInsert = c.prepareStatement(sql);
                    psInsert.executeUpdate();

                    // Si el tercero es el que se guarda en R_RES, actualizamos el numero de version
                    if (Integer.parseInt((String) listaCodTercero.elementAt(i)) == reg.getCodInter()) {
                        String sqlUpdate = "UPDATE R_RES SET RES_TNV=" + listaVersionTercero.elementAt(i) + " WHERE RES_UOR =" + reg.getUnidadOrgan() + " AND RES_TIP ='" + reg.getTipoReg() + "' AND RES_EJE=" + reg.getAnoReg() + " AND RES_NUM=" + numReg + " AND RES_DEP=" + reg.getIdentDepart();
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug("UPDATE NUMERO DE VERSION: " + sqlUpdate);
                        }
                        psUpdate = c.prepareStatement(sqlUpdate);
                        psUpdate.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.insertarTemasAnotacion.sql"), ex);
        } finally {
            SigpGeneralOperations.closeStatement(psInsert);
            SigpGeneralOperations.closeStatement(psUpdate);
        }
    }

    /**
     * Inserta la lista de relaciones incluida en el VO pasado como argumento. Primero se borran
     * todas las relaciones existentes para ese asiento y despues se introducen las nuevas.
     * Usa una conexión con una transacción ya abierta.
     */
    private void insertarRelaciones(Connection c, RegistroValueObject reg)
            throws AnotacionRegistroException, TechnicalException {

        PreparedStatement ps = null, ps2 = null;
        long numReg = reg.getNumReg();
        m_Log.debug("insertarRelaciones");
        try {
            // Como la relación es simétrica y sólo hay una fila por cada par de asientos relacionados,
            // hay que hacer el borrado comparando con el primero y con el segundo de la relación.
            String sql = "DELETE FROM R_REL" +
                    " WHERE (" + sql_rel_uor + "=" + reg.getUnidadOrgan() +
                    " AND " + sql_rel_dep + "=" + reg.getIdentDepart() +
                    " AND " + sql_rel_tipoA + "='" + reg.getTipoReg() + "'" +
                    " AND " + sql_rel_ejercicioA + "=" + reg.getAnoReg() +
                    " AND " + sql_rel_numeroA + "=" + numReg +
                    " ) OR ( " + sql_rel_uor + "=" + reg.getUnidadOrgan() +
                    " AND " + sql_rel_dep + "=" + reg.getIdentDepart() +
                    " AND " + sql_rel_tipoB + "='" + reg.getTipoReg() + "'" +
                    " AND " + sql_rel_ejercicioB + "=" + reg.getAnoReg() +
                    " AND " + sql_rel_numeroB + "=" + numReg +
                    ")";

            m_Log.debug(sql);
            ps = c.prepareStatement(sql);
            ps.executeUpdate();

            // Una vez borradas las relaciones existentes, insertamos las de la lista
            String sqlInsert = "INSERT INTO R_REL (" + sql_rel_uor + "," + sql_rel_dep + "," + sql_rel_tipoA + "," + sql_rel_ejercicioA + "," + sql_rel_numeroA + "," + sql_rel_tipoB + "," + sql_rel_ejercicioB + "," + sql_rel_numeroB +
                    ") VALUES ( " + reg.getUnidadOrgan() + "," + reg.getIdentDepart() + ",'" + reg.getTipoReg() + "'," + reg.getAnoReg() + "," + numReg + "," + "?,?,?)";

            m_Log.debug(sqlInsert);
            Vector<SimpleRegistroValueObject> relaciones = reg.getRelaciones();
            int i;
            for (SimpleRegistroValueObject relacion : relaciones) {
                ps2 = c.prepareStatement(sqlInsert);
                i = 1;
                ps2.setString(i++, relacion.getTipo());
                ps2.setString(i++, relacion.getEjercicio());
                ps2.setString(i++, relacion.getNumero());
                ps2.executeUpdate();
                SigpGeneralOperations.closeStatement(ps2);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.insertarRelaciones.sql"), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.closeStatement(ps2);
        }
    }

    /* *************************************************************************************
    Función: aceptarAnotacion.
    Acepta la anotacion de salida cuando esta asociada a un expediente
    La inserción se realiza dentro de la transacción abierta por la conexión c.
     ************************************************************************************* */
    private int aceptarAnotacion(Connection c, RegistroValueObject reg)
            throws AnotacionRegistroException, TechnicalException {

        m_Log.debug("aceptarAnotacion");

        PreparedStatement ps = null;
        long numReg = reg.getNumReg();
        try {

            String sql = "UPDATE R_RES SET " + sql_estAnot + "=1 WHERE " +
                    sql_codDptoAnotacion + "=" + reg.getIdentDepart() + " AND " +
                    sql_codUnidadAnotacion + "=" + reg.getUnidadOrgan() + " AND " +
                    sql_tipoRegAnotacion + "='" + reg.getTipoReg() + "' AND " +
                    sql_ejercicioAnotacion + "=" + reg.getAnoReg() + " AND " +
                    sql_numeroAnotacion + "=" + numReg;

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("aceptarAnotacion : " + sql);
            }

            ps = c.prepareStatement(sql);

            return ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.insertarBuzón.sql"), ex);
        } finally {
            SigpGeneralOperations.closeStatement(ps);
        }
    }

    /* **********************************************************************************************
    relacionRegistroValueObject: lista con las claves primarias de las anotaciones que cumplen
    las condiciones de un  "value object" de tipo Registro que se toma como patrón.
     ********************************************************************************************* */
    public Vector relacionRegistroValueObject(RegistroValueObject patron, String[] params, int startIndex, int count, int columna, String tipoOrden)
            throws AnotacionRegistroException, TechnicalException, Exception {
 
        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("relacionRegistroValue");

        Vector<Vector> lista = new Vector<Vector>();
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Vector filtros=new Vector();
        Connection con = null; 
        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector <Object> coleccionDevuelta=new Vector();
        boolean consultaInsensitiva=false;
        try {

            con = oad.getConnection();
            coleccionDevuelta = getSQLConsultaAnotaciones(patron, params, false,columna, tipoOrden,count,false,false,false);
           

            consultaInsensitiva=((Boolean) coleccionDevuelta.get(2)).booleanValue(); 
            String alter1=null;
            String alter2=null;
            if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                if(consultaInsensitiva){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    ps=con.prepareStatement(alter1);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                    ps=con.prepareStatement(alter2);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                }
            } 
            
            
            String sql=(String) coleccionDevuelta.get(0);
            m_Log.debug(" ======> relacionRegistroValueObject sql : " + sql);
            filtros=(Vector)coleccionDevuelta.get(1);
            
            
            
            
            if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                  
                  String sqlAux="(select rtrim (xmlagg (xmlelement (e,EXR_NUM || '\\n')).extract ('//text()'), '\\n') as valor	"+
		  " FROM E_EXR WHERE EXR_DEP = resultado.RES_DEP AND EXR_UOR = resultado.RES_UOR AND EXR_TIP = resultado.RES_TIP "+
		  "AND EXR_EJR = resultado.RES_EJE AND EXR_NRE = resultado.RES_NUM)	AS expedientes,"+
                  " (select rtrim (xmlagg (xmlelement (e,EXR_PRO || '\\n')).extract ('//text()'), '\\n') as valor	"+
		  " FROM E_EXR WHERE EXR_DEP = resultado.RES_DEP AND EXR_UOR = resultado.RES_UOR AND EXR_TIP = resultado.RES_TIP "+
		  "AND EXR_EJR = resultado.RES_EJE AND EXR_NRE = resultado.RES_NUM)	AS procedimientos,"+
                  " (select rtrim (xmlagg (xmlelement (e,EXR_NUM || '\\n')).extract ('//text()'), '\\n') as valor"+	
		  " FROM HIST_E_EXR WHERE EXR_DEP = resultado.RES_DEP AND EXR_UOR = resultado.RES_UOR "+
		  " AND EXR_TIP = resultado.RES_TIP AND EXR_EJR = resultado.RES_EJE AND EXR_NRE = resultado.RES_NUM)	AS expedientes_historico, "+
                  "(select rtrim (xmlagg (xmlelement (e,EXR_PRO || '\\n')).extract ('//text()'), '\\n') as valor"+	
		  " FROM HIST_E_EXR WHERE EXR_DEP = resultado.RES_DEP AND EXR_UOR = resultado.RES_UOR "+
		  " AND EXR_TIP = resultado.RES_TIP AND EXR_EJR = resultado.RES_EJE AND EXR_NRE = resultado.RES_NUM)	AS procedimientos_historico ";                  
                  
                   sql = "select resultado.*,"+sqlAux+" from (" + sql + ")  resultado where rn between ? and ? " +
                    " order by rn";
                  
              }else{
              //se necesita el alias "resultado" para que sea compatible en sqlserver
              sql = "select * from (" + sql + ")  resultado where rn between ? and ? " +
                    " order by rn";
              }
            
            
             
              
               if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO: relacionRegistroValueObject --> " + sql);
            }
             
            ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            
           int posicion_interrogacion=1;
            for (int i=0;i<filtros.size();i++)
            {
                Object elemento=new Object();
                elemento=filtros.get(i);

                m_Log.debug(" ==============> filtros elemento: " + elemento);
                
                if(elemento instanceof Integer)
                {
                    int e=(Integer)filtros.get(i);
                    ps.setInt(posicion_interrogacion, e);
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                if(elemento instanceof String)
                {
                    String e=(String)filtros.get(i);
                    ps.setString(posicion_interrogacion, e);
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                if(elemento instanceof Long)
                {
                    Long e=(Long)filtros.get(i);
                    ps.setLong(posicion_interrogacion, e);
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                posicion_interrogacion=posicion_interrogacion+1;
                
            }
			
            ps.setInt(posicion_interrogacion++, startIndex);
            ps.setInt(posicion_interrogacion++, count+startIndex-1); 
            
            rs = ps.executeQuery();

            int currentCount = 0;           
            if (startIndex >= 1){
                while(rs.next()){
                    Vector<String> clavePrimariaRegistroVO = new Vector<String>();
                    String codDepartamento = rs.getString("RES_DEP");
                    String codUor = rs.getString("RES_UOR");
                    String tipoEntrada = rs.getString("RES_TIP");
                    String ejercicio = rs.getString("RES_EJE");
                    String numeroAnotacion = rs.getString("RES_NUM");                    
                    clavePrimariaRegistroVO.addElement(codDepartamento);
                    clavePrimariaRegistroVO.addElement(codUor);                    
                    clavePrimariaRegistroVO.addElement(tipoEntrada);                    
                    clavePrimariaRegistroVO.addElement(ejercicio);                    
                    clavePrimariaRegistroVO.addElement(numeroAnotacion);
                    clavePrimariaRegistroVO.addElement(rs.getString("FECHA"));
                    clavePrimariaRegistroVO.addElement(rs.getString("FECHADOC"));
                    clavePrimariaRegistroVO.addElement(AdaptadorSQLBD.js_escape(rs.getString("RES_ASU")));
                    clavePrimariaRegistroVO.addElement(rs.getString("HTE_NOM"));
                    clavePrimariaRegistroVO.addElement(rs.getString("APELLIDO1"));
                    clavePrimariaRegistroVO.addElement(rs.getString("APELLIDO2"));
                    clavePrimariaRegistroVO.addElement(rs.getString("MUN_NOM"));
                    String destinoOrg = rs.getString("DESTINO");
                    String destinoOrgExt = rs.getString("DESTINO_ORG_EXT");
                    String origenOrgExt = rs.getString("ORIGEN_ORG_EXT");
                    String destino = "";
                    if (destinoOrg != null) {
                        destino = destinoOrg;
                    } else if (destinoOrgExt != null) {
                        destino = destinoOrgExt;
                    } else if (origenOrgExt != null) {
                        destino = origenOrgExt;
                    }
                    clavePrimariaRegistroVO.addElement(destino);

                    Vector expedientesRelacionados=new Vector();
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                         String expedientesRel=rs.getString("expedientes");
                         String expedientesHistoricosRel=rs.getString("expedientes_historico");
                         if((!"".equals(expedientesHistoricosRel))&&(!"null".equals(expedientesHistoricosRel))&&(expedientesHistoricosRel!=null)){
                             if((!"".equals(expedientesRel))&&(!"null".equals(expedientesRel))&&(expedientesRel!=null)) expedientesRel=expedientesRel+"\\n"+expedientesHistoricosRel;
                             else expedientesRel=expedientesHistoricosRel;
                         }
                         if((!"".equals(expedientesRel))&&(!"null".equals(expedientesRel))&&(expedientesRel!=null))expedientesRelacionados.add(expedientesRel);
                         
                         String procedimientosRel=rs.getString("procedimientos");
                         String procedimientosHistoricosRel=rs.getString("procedimientos_historico");
                         if((!"".equals(procedimientosHistoricosRel))&&(!"null".equals(procedimientosHistoricosRel))&&(procedimientosHistoricosRel!=null)){ 
                             
                            if((!"".equals(procedimientosRel))&&(!"null".equals(procedimientosRel))&&(procedimientosRel!=null))  procedimientosRel=procedimientosRel+"\\n"+procedimientosHistoricosRel;
                            else  procedimientosRel=procedimientosHistoricosRel;
                         }
                         
                         if((!"".equals(procedimientosRel))&&(!"null".equals(procedimientosRel))&&(procedimientosRel!=null))  expedientesRelacionados.add(procedimientosRel);
                    }
                    else{
                        expedientesRelacionados=getExpedientesAsociadosARegistro(con,Integer.parseInt(codDepartamento),Integer.parseInt(codUor),tipoEntrada,Integer.parseInt(ejercicio),Integer.parseInt(numeroAnotacion));
                    }
                    
                    String numeroExp=null;
                    String procedimiento=null;
                    
                    
                    try{
                        if(expedientesRelacionados!=null && expedientesRelacionados.size()==2){                            
                          numeroExp=(String) expedientesRelacionados.get(0);
                          procedimiento=(String) expedientesRelacionados.get(1);  
                        }
                        
                    }catch(Exception e){
                        m_Log.error("  =========> ERROR EXPEDIENTES RELACIONADOS:  " + e.getMessage());
                    }
                    
                    //rs.getString("EXR_NUM");
                    String estado = rs.getString("RES_EST");
                    if (numeroExp != null && !"".equals(numeroExp) && !"null".equals(numeroExp)) {//SOLO ESTA ACEPTADA SIN PROCEDIMIENTOS ASOCIADOS
                        clavePrimariaRegistroVO.addElement("3");
                    } else {
                        if("3".equals(estado))
                            clavePrimariaRegistroVO.addElement("99");
                        else
                            clavePrimariaRegistroVO.addElement(estado);
                    }
                    
                    clavePrimariaRegistroVO.addElement(rs.getString("TDO_DES"));
                    clavePrimariaRegistroVO.addElement(rs.getString("num_terceros"));
                    String observa = rs.getString("RES_OBS");
                    clavePrimariaRegistroVO.addElement(observa);
                    clavePrimariaRegistroVO.addElement(procedimiento);
                    clavePrimariaRegistroVO.addElement(numeroExp);
                    clavePrimariaRegistroVO.addElement(rs.getString("USU_NOM"));
                    clavePrimariaRegistroVO.addElement(rs.getString("RES_MOD"));
                    clavePrimariaRegistroVO.addElement(rs.getString("RES_FEDOC"));
                    clavePrimariaRegistroVO.addElement(rs.getString("FIN_DIGITALIZACION"));
                    //si no posee numero de expediente se evita realizar la conslta para obtener el estado de este
                    if (numeroExp != null && !"".equals(numeroExp)) {
                        clavePrimariaRegistroVO.addElement(insertarEstadoExpediente(numeroExp,con));
                    } else {
                        clavePrimariaRegistroVO.addElement(null);
                    }
                    lista.addElement(clavePrimariaRegistroVO);
					
                }//while(rs.next())
            }//if (startIndex >= 1)
			
            return lista;

            //return lista;
        } catch (SQLException sqle) {
            m_Log.error(sqle.getMessage());
            sqle.printStackTrace();
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getRelacionAnotaciones.sql"), sqle);
        } catch (BDException bde) {
            m_Log.error(bde.getMessage());
            bde.printStackTrace();
            throw new TechnicalException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getRelacionAnotaciones.sql"), bde);
        }  catch (Exception ex) {
            m_Log.error(ex.getMessage());
            ex.printStackTrace();
            throw new TechnicalException("Error: listas vacia",ex);
        } 
        finally {
            
             if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                if(consultaInsensitiva){
                    String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                    String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                    ps=con.prepareStatement(alter1);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                    ps=con.prepareStatement(alter2);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                }
            } 
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }

     
    public String insertarEstadoExpediente(String numExpediente, Connection con) throws SQLException {
		String estadoExpediente=null;
		PreparedStatement ps = null;
		String sql = null;
		ResultSet rs = null;
		
		try{
			sql = "SELECT EXP_EST FROM E_EXP WHERE EXP_NUM = ?";
			m_Log.debug("Query = " + sql);
			m_Log.debug("Parámetros de query: " + numExpediente);
			
			ps = con.prepareStatement(sql);
			ps.setString(1, numExpediente);
			rs = ps.executeQuery();
			while(rs.next()){
				estadoExpediente=rs.getString("EXP_EST");
			}
		} catch (SQLException sqle){
			m_Log.error("Ha ocurrido un error al obtener el estado del expediente asociado.",sqle);
			throw sqle;
		} finally {
			try {
				if(ps != null) ps.close();
			} catch (SQLException ex) {
				m_Log.error("Ha ocurrido un error al cerrar los recursos de base de datos.",ex);
			}
			
		}
		
		
		return estadoExpediente;
	}
	
    public ArrayList<Vector> relacionRegistroValueObjectImprimirOptimo(RegistroValueObject patron, String[] params, int startIndex, int count, int columna, String tipoOrden)
            throws AnotacionRegistroException, TechnicalException, Exception {
 
        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("relacionRegistroValueObjectImprimirOptimo");

        //Vector<Vector> lista = new Vector<Vector>();
        ArrayList<Vector> lista = new ArrayList<Vector>();
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Vector filtros=new Vector(); 
        Connection con = null; 
        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector <Object> coleccionDevuelta=new Vector();
        boolean consultaInsensitiva=false;
        try {

            con = oad.getConnection();
            coleccionDevuelta = getSQLConsultaAnotaciones(patron, params, false,columna, tipoOrden,count, true, false,false);
           

            consultaInsensitiva=((Boolean) coleccionDevuelta.get(2)).booleanValue(); 
            String alter1=null;
            String alter2=null;
            if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                if(consultaInsensitiva){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    ps=con.prepareStatement(alter1);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                    ps=con.prepareStatement(alter2);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                }
            } 
            
            
            String sql=(String) coleccionDevuelta.get(0);
            m_Log.debug(" ======> relacionRegistroValueObjectImprimirOptimo sql : " + sql);
            filtros=(Vector)coleccionDevuelta.get(1); 
            
            //optimizacion para oracle para obtener los expedientes relacionados en una sola consulta
              if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                  
                  String sqlAux="(select rtrim (xmlagg (xmlelement (e,EXR_NUM || '\\n')).extract ('//text()'), '\\n') as valor	"+
		  " FROM E_EXR WHERE EXR_DEP = resultado.RES_DEP AND EXR_UOR = resultado.RES_UOR AND EXR_TIP = resultado.RES_TIP "+
		  "AND EXR_EJR = resultado.RES_EJE AND EXR_NRE = resultado.RES_NUM)	AS expedientes,"+
                  " (select rtrim (xmlagg (xmlelement (e,EXR_PRO || '\\n')).extract ('//text()'), '\\n') as valor	"+
		  " FROM E_EXR WHERE EXR_DEP = resultado.RES_DEP AND EXR_UOR = resultado.RES_UOR AND EXR_TIP = resultado.RES_TIP "+
		  "AND EXR_EJR = resultado.RES_EJE AND EXR_NRE = resultado.RES_NUM)	AS procedimientos,"+
                  " (select rtrim (xmlagg (xmlelement (e,EXR_NUM || '\\n')).extract ('//text()'), '\\n') as valor"+	
		  " FROM HIST_E_EXR WHERE EXR_DEP = resultado.RES_DEP AND EXR_UOR = resultado.RES_UOR "+
		  " AND EXR_TIP = resultado.RES_TIP AND EXR_EJR = resultado.RES_EJE AND EXR_NRE = resultado.RES_NUM)	AS expedientes_historico, "+
                  "(select rtrim (xmlagg (xmlelement (e,EXR_PRO || '\\n')).extract ('//text()'), '\\n') as valor"+	
		  " FROM HIST_E_EXR WHERE EXR_DEP = resultado.RES_DEP AND EXR_UOR = resultado.RES_UOR "+
		  " AND EXR_TIP = resultado.RES_TIP AND EXR_EJR = resultado.RES_EJE AND EXR_NRE = resultado.RES_NUM)	AS procedimientos_historico ";                  
                  
                   sql = "select resultado.*,"+sqlAux+" from (" + sql + ")  resultado";
                  
              }else{
              //se necesita el alias "resultado" para que sea compatible en sqlserver
              sql = "select * from (" + sql + ")  resultado ";
              }
              
               if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO: relacionRegistroValueObjectImprimirOptimo --> " + sql);
            }
             
            ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            
           int posicion_interrogacion=1;
            for (int i=0;i<filtros.size();i++)
            {
                Object elemento=new Object();
                elemento=filtros.get(i);

                m_Log.debug(" ==============> filtros elemento: " + elemento);
                
                if(elemento instanceof Integer)
                {
                    int e=(Integer)filtros.get(i);
                    ps.setInt(posicion_interrogacion, e);
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                if(elemento instanceof String)
                {
                    String e=(String)filtros.get(i);
                    ps.setString(posicion_interrogacion, e);
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                if(elemento instanceof Long)
                {
                    Long e=(Long)filtros.get(i);
                    ps.setLong(posicion_interrogacion, e); 
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                posicion_interrogacion=posicion_interrogacion+1;
                
            }
           
               
            rs = ps.executeQuery();

            int currentCount = 0;           
            if (startIndex >= 1){
                while(rs.next()){
                    Vector<String> clavePrimariaRegistroVO = new Vector<String>();
                    String codDepartamento = rs.getString("RES_DEP");
                    String codUor = rs.getString("RES_UOR");
                    String tipoEntrada = rs.getString("RES_TIP");
                    String ejercicio = rs.getString("RES_EJE");
                    String numeroAnotacion = rs.getString("RES_NUM");                    
                    clavePrimariaRegistroVO.addElement(codDepartamento);                    
                    clavePrimariaRegistroVO.addElement(codUor);                    
                    clavePrimariaRegistroVO.addElement(tipoEntrada);                    
                    clavePrimariaRegistroVO.addElement(ejercicio);                    
                    clavePrimariaRegistroVO.addElement(numeroAnotacion);
                    clavePrimariaRegistroVO.addElement(rs.getString("FECHA"));
                    clavePrimariaRegistroVO.addElement(rs.getString("FECHADOC"));
                    clavePrimariaRegistroVO.addElement(AdaptadorSQLBD.js_escape(rs.getString("RES_ASU")));
                    clavePrimariaRegistroVO.addElement(rs.getString("HTE_NOM"));
                    clavePrimariaRegistroVO.addElement(rs.getString("APELLIDO1"));
                    clavePrimariaRegistroVO.addElement(rs.getString("APELLIDO2"));
                    clavePrimariaRegistroVO.addElement(rs.getString("MUN_NOM"));
                    String destinoOrg = rs.getString("DESTINO");
                    String destinoOrgExt = rs.getString("DESTINO_ORG_EXT");
                    String origenOrgExt = rs.getString("ORIGEN_ORG_EXT");
                    String destino = "";
                    if (destinoOrg != null) {
                        destino = destinoOrg;
                    } else if (destinoOrgExt != null) {
                        destino = destinoOrgExt;
                    } else if (origenOrgExt != null) {
                        destino = origenOrgExt;
                    }
                    clavePrimariaRegistroVO.addElement(destino);
                    
                    //TODO modificar esto, para que procese lo devuelto por la consulta principal
                    //optimizacion para oracle
                    Vector expedientesRelacionados=new Vector();
                    if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                         String expedientesRel=rs.getString("expedientes");
                         String expedientesHistoricosRel=rs.getString("expedientes_historico");
                         if((!"".equals(expedientesHistoricosRel))&&(!"null".equals(expedientesHistoricosRel))&&(expedientesHistoricosRel!=null)){
                             expedientesRel=expedientesRel+"\\n"+expedientesHistoricosRel;
                         }
                         if((!"".equals(expedientesRel))&&(!"null".equals(expedientesRel))&&(expedientesRel!=null))expedientesRelacionados.add(expedientesRel);
                         
                         String procedimientosRel=rs.getString("procedimientos");
                         String procedimientosHistoricosRel=rs.getString("procedimientos_historico");
                        if((!"".equals(procedimientosHistoricosRel))&&(!"null".equals(procedimientosHistoricosRel))&&(procedimientosHistoricosRel!=null)){ 
                             procedimientosRel=procedimientosRel+"\\n"+procedimientosHistoricosRel;
                         }
                         
                         if((!"".equals(procedimientosRel))&&(!"null".equals(procedimientosRel))&&(procedimientosRel!=null))  expedientesRelacionados.add(procedimientosRel);
                    }
                    else{
                        expedientesRelacionados=getExpedientesAsociadosARegistro(con,Integer.parseInt(codDepartamento),Integer.parseInt(codUor),tipoEntrada,Integer.parseInt(ejercicio),Integer.parseInt(numeroAnotacion));
                    }
                    
                    String numeroExp=null;
                    String procedimiento=null;
                    
                    
                    try{
                        if(expedientesRelacionados!=null && expedientesRelacionados.size()==2){                            
                          numeroExp=(String) expedientesRelacionados.get(0);
                          procedimiento=(String) expedientesRelacionados.get(1);  
                        }
                        
                    }catch(Exception e){
                        m_Log.error("  =========> ERROR EXPEDIENTES RELACIONADOS:  " + e.getMessage());
                    }
                    
                    //rs.getString("EXR_NUM");
                    String estado = rs.getString("RES_EST");
                    if (numeroExp != null && !"".equals(numeroExp) && !"null".equals(numeroExp)) {//SOLO ESTA ACEPTADA SIN PROCEDIMIENTOS ASOCIADOS
                        clavePrimariaRegistroVO.addElement("3");
                    } else {
                        if("3".equals(estado))
                            clavePrimariaRegistroVO.addElement("99");
                        else
                            clavePrimariaRegistroVO.addElement(estado);
                    }
                    
                    clavePrimariaRegistroVO.addElement(rs.getString("TDO_DES"));
                    clavePrimariaRegistroVO.addElement(rs.getString("num_terceros"));
                    String observa = rs.getString("RES_OBS");
                    clavePrimariaRegistroVO.addElement(observa);
                    clavePrimariaRegistroVO.addElement(procedimiento);
                    clavePrimariaRegistroVO.addElement(numeroExp);
                    clavePrimariaRegistroVO.addElement(rs.getString("USU_NOM"));
                    clavePrimariaRegistroVO.addElement(rs.getString("RES_MOD"));
                    clavePrimariaRegistroVO.addElement(rs.getString("RES_FEDOC"));
                    clavePrimariaRegistroVO.addElement(rs.getString("HTE_DOC"));
                    clavePrimariaRegistroVO.addElement(rs.getString("USU_NOM"));
                    lista.add(clavePrimariaRegistroVO);
                }//while(rs.next())
            }//if (startIndex >= 1)

           
            return lista;

            //return lista;
        } catch (SQLException sqle) {
            m_Log.error(sqle.getMessage());
            sqle.printStackTrace();
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getRelacionAnotaciones.sql"), sqle);
        } catch (BDException bde) {
            m_Log.error(bde.getMessage());
            bde.printStackTrace();
            throw new TechnicalException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getRelacionAnotaciones.sql"), bde);
        }  catch (Exception ex) {
            m_Log.error(ex.getMessage());
            ex.printStackTrace();
            throw new TechnicalException("Error: listas vacia",ex);
        } 
        finally {
            
            if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                if(consultaInsensitiva){
                    String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                    String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                    ps=con.prepareStatement(alter1);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                    ps=con.prepareStatement(alter2);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                }
            } 
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }
    
    public ArrayList relacionCampoRegistroValueObject(RegistroValueObject patron, String campo, String[] params) throws AnotacionRegistroException, TechnicalException, Exception {
        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.info("relacionCampoRegistroValueObject");

        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        Connection con = null;
        ArrayList lista = null;
        ArrayList filtros = null; 
        ArrayList coleccionDevuelta = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean consultaInsensitiva=false;
        boolean resOfi = false;
        boolean resNum = false;
        
        try {

            con = adapt.getConnection();
            
            if(campo.equals("RES_OFI")) resOfi = true;
            else if(campo.equals("RES_NUM")) resNum = true;
            coleccionDevuelta = new ArrayList(getSQLConsultaAnotaciones(patron, params, false, 0, null, 0, false, resOfi, resNum));
           

            consultaInsensitiva=((Boolean) coleccionDevuelta.get(2)).booleanValue(); 
            String alter1=null;
            String alter2=null;
            if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                if(consultaInsensitiva){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    ps=con.prepareStatement(alter1);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                    ps=con.prepareStatement(alter2);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                }
            } 
            
            
            String sql=(String) coleccionDevuelta.get(0);
            m_Log.debug(" ======> relacionCampoRegistroValueObject sql : " + sql);
            filtros = new ArrayList((Vector)coleccionDevuelta.get(1)); 
            
            ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            int posicion_interrogacion=1;
            for (int i=0;i<filtros.size();i++) {
                Object elemento = filtros.get(i);             
                if(elemento instanceof Integer) {
                    int e=(Integer)filtros.get(i);
                    ps.setInt(posicion_interrogacion, e);
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                } else if(elemento instanceof String) {
                    String e=(String)filtros.get(i);
                    ps.setString(posicion_interrogacion, e);
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                } else if(elemento instanceof Long) {
                    Long e=(Long)filtros.get(i);
                    ps.setLong(posicion_interrogacion, e); 
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                posicion_interrogacion++;
                
            }          
               
            rs = ps.executeQuery();    
            if(resOfi){
                lista = new ArrayList<UORDTO>();
                while(rs.next()){
                    UORDTO uor = new UORDTO();
                    uor.setUor_cod(String.valueOf(rs.getInt("COD_OFICINA")));
                    uor.setUor_cod_vis(rs.getString("CODVIS_OFICINA"));
                    uor.setUor_nom(rs.getString("NOM_OFICINA"));
                    lista.add(uor);
                }//while(rs.next())
            } else if(resNum){
                lista = new ArrayList<Long>();
                while(rs.next()){
                    lista.add(rs.getLong("NUMREG"));
                }//while(rs.next())
            }

            m_Log.info("relacionCampoRegistroValueObject");
            return lista;

            //return lista;
        } catch (SQLException sqle) {
            m_Log.error(sqle.getMessage());
            sqle.printStackTrace();
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getRelacionAnotaciones.sql"), sqle);
        } catch (BDException bde) {
            m_Log.error(bde.getMessage());
            bde.printStackTrace();
            throw new TechnicalException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getRelacionAnotaciones.sql"), bde);
        }  catch (Exception ex) {
            m_Log.error(ex.getMessage());
            ex.printStackTrace();
            throw new TechnicalException("Error: listas vacia",ex);
        } 
        finally {
            
            if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                if(consultaInsensitiva){
                    String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                    String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                    ps=con.prepareStatement(alter1);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                    ps=con.prepareStatement(alter2);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                }
            } 
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(adapt, con);
        }
    }
   
    public Vector getExpedientesAsociadosARegistro (Connection con,int departamento,int codUor,String tipo,int ejercicio,int numero){        
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Vector resultado=new Vector();
        
        try{
            String sql = "SELECT EXR_NUM,EXR_PRO FROM E_EXR " +
                         "WHERE EXR_DEP=? AND EXR_UOR=? AND EXR_TIP=? AND EXR_EJR=? AND EXR_NRE=?";
            
             String sql2 = "SELECT EXR_NUM,EXR_PRO FROM HIST_E_EXR " +
                         "WHERE EXR_DEP=? AND EXR_UOR=? AND EXR_TIP=? AND EXR_EJR=? AND EXR_NRE=?";
             
           String sqlfinal=sql+" union all "+ sql2;
           
            m_Log.debug(sqlfinal);
            
            ps = con.prepareStatement(sqlfinal);
            
            ps.setInt(1,departamento);
            ps.setInt(2,codUor);
            ps.setString(3,tipo);
            ps.setInt(4,ejercicio);
            ps.setInt(5,numero);                        
            ps.setInt(6,departamento);
            ps.setInt(7,codUor);
            ps.setString(8,tipo);
            ps.setInt(9,ejercicio);
            ps.setInt(10,numero);
            rs = ps.executeQuery();
            
            m_Log.debug("parámetros: "+departamento+","+codUor+","+tipo+","+ejercicio+","+numero);
            
            String procedimiento="";
            String numeroExpediente="";
            String saltoLinea="";
            
            while(rs.next()){
               procedimiento=procedimiento+saltoLinea+rs.getString("EXR_PRO");
               numeroExpediente=numeroExpediente+saltoLinea+rs.getString("EXR_NUM");              
               saltoLinea=" \\n ";
            }// while
            
            
            m_Log.debug(" ====================>AnotacionRegistroDAO.getExpedientesAsociadosARegistro() anotacion: " + ejercicio+"/"+numero + " tiene expedientes: " + numeroExpediente + "y procedimiento: " + procedimiento);
            
            if(!"".equals(numeroExpediente)){
                resultado.add(numeroExpediente);
                resultado.add(procedimiento);
            }else resultado=null;
            
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            }catch(SQLException e){
               m_Log.error("Error en la claúsula finally al cerrar los recursos asociados a la conexión de BD: " + e.getMessage());
            }
        }
        return resultado;
    }
    
    
    
     
    /**
     * Se le envia una lista de entradas de registro que pueden estar "duplicadas" puesto que si una entrada de registro
     * tiene mas de un expediente asociado las trata como si fuesen entradas distintas por lo que esta funcion agrupa todas
     * esas entradas poniendo lo numeros de expediente asociados todos juntos en el mismo campo asi se visualizara una sola entrada
     * con N numeros de expediente asociados.
     * 
     */
   
    public Vector<Vector> listaEntradasExpedientesRelacionadosAgrupados (Vector<Vector> lista){
        if(m_Log.isDebugEnabled()) m_Log.debug("listaEntradasExpedientesRelacionadosAgrupados() : BEGIN");
        Vector<Vector> listaAgrupada = new Vector<Vector>();
        Vector<String> nuevaEntrada = new Vector<String>();
        if(m_Log.isDebugEnabled()) m_Log.debug("Comenzamos recorriendo la lista con todas las entradas");
        for(int i=0; i<lista.size(); i++){
            //if(m_Log.isDebugEnabled()) m_Log.debug("Cogemos el elemento actual de la lista con todas las entradas segun el inidice");
            Vector<String> entradaActual = lista.get(i);
            /*if(m_Log.isDebugEnabled()) m_Log.debug("Copiamos el elemento actual de la lista con todas las entradas segun el inidice"
                    + "en el vector de nueva entrada");*/
            nuevaEntrada = entradaActual;
            /*if(m_Log.isDebugEnabled()) m_Log.debug("Recorremos las posiciones desde la entrada actual hasta el final buscando coincidencias"
                    + "con el numero y el ejercicio de la entrada del registro");*/
            String resDep = entradaActual.get(0);
            String resEje = entradaActual.get(3);
            String resNum = entradaActual.get(4);
            
            //Si res num es distinto de NULL y no tiene valor en la posicion 17 averiguamos por el numero de expediente cual es el procedimiento
            if(nuevaEntrada.get(18) != null && !"".equalsIgnoreCase(nuevaEntrada.get(18)) && resNum != null && !"".equalsIgnoreCase(resNum)){
                String[] datosExpediente = nuevaEntrada.get(18).split("/");
                    String codProc = datosExpediente[1];
                nuevaEntrada.set(17, codProc);
            }//if(nuevaEntrada.get(17) != null && !"".equalsIgnoreCase(nuevaEntrada.get(17)) && resNum != null && !"".equalsIgnoreCase(resNum))
            for(int x=i+1; x<lista.size(); x++){
                Vector<String> entradaAuxiliar = lista.get(x);
                String resDepAux = entradaAuxiliar.get(0);
                String resEjeAux = entradaAuxiliar.get(3);
                String resNumAux = entradaAuxiliar.get(4);
                
                if((resDep.equalsIgnoreCase(resDepAux)) && (resEje.equalsIgnoreCase(resEjeAux)) &&(resNum.equalsIgnoreCase(resNumAux))){
                    String resNumExpAux = entradaAuxiliar.get(18);
                    String resNumExp = nuevaEntrada.get(18);
                    nuevaEntrada.set(18, resNumExp + " " + resNumExpAux);
                    //En funcion del nuevo numero de expediente averiguamos cual es el procedimiento de la anotacion
                    if(resNumExpAux != null && !"".equalsIgnoreCase(resNumExpAux)){
                        String[] datosExpediente = resNumExpAux.split("/");
                        String codProcAux = datosExpediente[1];
                        String resCodProc = nuevaEntrada.get(17);
                        nuevaEntrada.set(17, resCodProc + " \\n " + codProcAux);
                    }//if(resNumExpAux != null && !"".equalsIgnoreCase(resNumExpAux))
                    lista.remove(x);
                    x-=1;
                }//if((resDep.equalsIgnoreCase(resDepAux)) && (resEje.equalsIgnoreCase(resEjeAux)) &&(resNum.equalsIgnoreCase(resNumAux)))
            }//for(int x=i+1; x<lista.size(); x++)
            listaAgrupada.add(nuevaEntrada);
        }//for(int i=0; i<lista.size(); i++)
        if(m_Log.isDebugEnabled()) m_Log.debug("listaEntradasExpedientesRelacionadosAgrupados() : END");
        return listaAgrupada;
    }//listaEntradasExpedientesRelacionadosAgrupados

    public Vector getCamposListado(int codListado, String[] params)
            throws AnotacionRegistroException, TechnicalException {

    Vector resultado = new Vector();
    AdaptadorSQLBD abd = null;
    Connection conexion = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try{
      //m_Log.debug("A por el OAD");
      abd = new AdaptadorSQLBD(params);
      //m_Log.debug("A por la conexion");
      conexion = abd.getConnection();
      // Creamos la select con los parametros adecuados.
      sql = "SELECT * FROM A_CAMPLIST WHERE CAMPLIST_CODLIST="+codListado+" ORDER BY CAMPLIST_COD";
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);
      stmt = conexion.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        CamposListadosParametrizablesVO gVOCampos = new CamposListadosParametrizablesVO();
        gVOCampos.setCodCampo(Integer.parseInt(rs.getString("CAMPLIST_COD")));
        gVOCampos.setNomCampo(rs.getString("CAMPLIST_NOM"));
        int act=Integer.parseInt(rs.getString("CAMPLIST_ACTIVO"));
        String activo="";
        if (1==act){
            activo="SI";
        }else{
            activo="NO";
        }
        gVOCampos.setActCampo(activo);
        gVOCampos.setTamanoCampo(Integer.parseInt(rs.getString("CAMPLIST_TAMANO")));
        gVOCampos.setOrdenCampo(Integer.parseInt(rs.getString("CAMPLIST_ORDEN")));
          
        resultado.add(gVOCampos);
         
      }
    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
    }finally{
        SigpGeneralOperations.closeResultSet(rs);
        SigpGeneralOperations.closeStatement(stmt);
        SigpGeneralOperations.devolverConexion(abd, conexion);
    }
    return resultado;

    }

    public int getNumeroTotalAnotaciones(RegistroValueObject patron, String[] params)
            throws AnotacionRegistroException, TechnicalException, SQLException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("getNumeroTotalAnotaciones");

        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Vector filtros=new Vector();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector <Object> coleccionDevuelta= new Vector();
        boolean consultaInsensitiva=false;

        try {

            con = oad.getConnection();
            //los dos string que pasamos no son necesarios para esta consulta ya que son los parametreos pq ordenar
            coleccionDevuelta = getSQLConsultaAnotaciones(patron, params, true,0,"0",0,false,false,false);
            

            consultaInsensitiva=((Boolean) coleccionDevuelta.get(2)).booleanValue(); 
            String alter1=null;
            String alter2=null;
            if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                if(consultaInsensitiva){
                    alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
                    alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";
                    ps=con.prepareStatement(alter1);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                    ps=con.prepareStatement(alter2);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                }
            }

            String sql=(String)coleccionDevuelta.get(0);
            filtros=(Vector)coleccionDevuelta.get(1);
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO: getNumeroTotalAnotaciones --> " + sql);
            }
            
                       
            ps = con.prepareStatement(sql);
            
            int posicion_interrogacion=1;
            for (int i=0;i<filtros.size();i++)
            {
                Object elemento=new Object();
                elemento=filtros.get(i);
                if(elemento instanceof Integer)
                {
                    int e=(Integer)filtros.get(i);
                    ps.setInt(posicion_interrogacion, e);
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                if(elemento instanceof String)
                {
                    String e=(String)filtros.get(i);
                    ps.setString(posicion_interrogacion, e);
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                if(elemento instanceof Long)
                {
                    Long e=(Long)filtros.get(i);
                    ps.setLong(posicion_interrogacion, e);
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                posicion_interrogacion=posicion_interrogacion+1;
                
            }
//            //Se vuelven a anhadir las bind variables ya que la consulta viene con un union
//            for (int i=0;i<filtros.size();i++)
//            {
//                Object elemento=new Object();
//                elemento=filtros.get(i);
//                if(elemento instanceof Integer)
//                {
//                    int e=(Integer)filtros.get(i);
//                    ps.setInt(posicion_interrogacion, e);
//                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
//                }
//                if(elemento instanceof String)
//                {
//                    String e=(String)filtros.get(i);
//                    ps.setString(posicion_interrogacion, e);
//                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
//                }
//                if(elemento instanceof Long)
//                {
//                    Long e=(Long)filtros.get(i);
//                    ps.setLong(posicion_interrogacion, e);
//                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
//                }
//                
//                posicion_interrogacion=posicion_interrogacion+1;
//                
//            }
            
            rs = ps.executeQuery();
            
            

            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
            return totalCount;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getRelacionAnotaciones.sql"), sqle);
        } catch (BDException bde) {
            bde.printStackTrace();
            throw new TechnicalException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getRelacionAnotaciones.sql"), bde);
        } finally {
            
            if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                if(consultaInsensitiva){
                    String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                    String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                    ps=con.prepareStatement(alter1);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                    ps=con.prepareStatement(alter2);
                    ps.executeQuery();
                    SigpGeneralOperations.closeStatement(ps);
                }
            } 
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(oad, con);
        }


    }

    private Vector getSQLConsultaAnotaciones(RegistroValueObject patron, String[] params, boolean countAll,int columna,String tipoOrden,int numeroResultados, boolean imprimir, boolean oficina, boolean resNum) throws TechnicalException {

        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        TransformacionAtributoSelect transformador = new TransformacionAtributoSelect(oad);
        Vector filtros= new Vector();
        Vector <Object>retorno=new Vector();
        int posfiltros=0;
        boolean consultaInsensitiva=false;
        
        
        ArrayList lista = new ArrayList(); //Lista para ordenar por campos
        lista.add("RES_DEP");lista.add("RES_UOR");lista.add("RES_TIP");lista.add("RES_EJE");lista.add("RES_NUM");lista.add("PROCEDIMIENTO");lista.add("RES_EST");lista.add("RES_FEC");
        lista.add("RES_FED");lista.add("RES_FEC");lista.add("RES_FED");lista.add("TERCERO.HTE_NOM");lista.add("RES_ASU");lista.add("APELLIDO1");lista.add("APELLIDO2");lista.add("MUN_NOM");lista.add("RES_FEC");
        lista.add("TDO_DES");lista.add("RES_OBS");lista.add("DEST.UOR_NOM");lista.add("ORG_DES.UOREX_NOM");lista.add("ORG_ORI.UOREX_NOM");lista.add("num_terceros");lista.add("USU_NOM");lista.add("RES_MOD");lista.add("RES_FEDOC");
        
        String directivaSalidaUsuario=patron.getDirectivaUsuPermisoUor();
      


        try {
            String sqlSelectComun = "/*+first_rows("+numeroResultados+")*/ DISTINCT RES_DEP, RES_UOR, RES_TIP, RES_EJE, RES_NUM,FIN_DIGITALIZACION, PROCEDIMIENTO, RES_EST, " +
                    oad.convertir("RES_FEC", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS FECHAORD," +
                    oad.convertir("RES_FED", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS FECHADOCORD," +
                    oad.convertir("RES_FEC", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") + " AS FECHA," +
                    oad.convertir("RES_FED", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS") + " AS FECHADOC, TERCERO.HTE_DOC AS HTE_DOC, TERCERO.HTE_NOM AS HTE_NOM, RES_ASU, " +
                    oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_CONCAT,
                    new String[]{"TERCERO.HTE_PA1", "' '", "TERCERO.HTE_AP1"}) + " AS APELLIDO1, " +
                    oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_CONCAT,
                    new String[]{"TERCERO.HTE_PA2", "' '", "TERCERO.HTE_AP2"}) + " AS APELLIDO2, " +
                    "MUN_NOM, RES_FEC, RES_FED, TDO_DES, RES_OBS, DEST.UOR_NOM AS DESTINO, ORG_DES.UOREX_NOM AS DESTINO_ORG_EXT, ORG_ORI.UOREX_NOM AS ORIGEN_ORG_EXT, ";
                    if (imprimir) { //solo se imprime un interesado en el listado
                        sqlSelectComun=sqlSelectComun+ "1 AS num_terceros, ";
                    }
                    else{
                        sqlSelectComun=sqlSelectComun+"(SELECT COUNT (*) FROM  R_EXT WHERE res_uor=ext_uor AND res_dep=ext_dep AND res_tip=ext_tip AND res_eje=ext_eje AND res_num=ext_num )AS num_terceros, ";
                    }
                    sqlSelectComun=sqlSelectComun+" A_USU.USU_NOM AS USU_NOM, RES_MOD,RES_FEDOC ";


            // Es necesario usar DISTINCT con los campos que son clave primaria de R_RES
            // para no repetir anotaciones al contarlas. Se concatenan en un string porque la
            // sintaxis COUNT (DISTINCT ...) no permite mas de un campo.
            String selectCount = "COUNT (DISTINCT " +
                    oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_CONCAT,
                    new String[]{oad.convertir("RES_DEP", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null), "'-'",
                oad.convertir("RES_UOR", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null), "'-'",
                "RES_TIP", "'-'",
                oad.convertir("RES_EJE", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null), "'-'",
                oad.convertir("RES_NUM", AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null)
            }) + ") TOTAL";

            // Partes Join Comunes.
            ArrayList<String> joinComunes = new ArrayList<String>();
            joinComunes.add("R_RES");
            joinComunes.add("LEFT");
            joinComunes.add("R_TDO");
            joinComunes.add("RES_TDO = TDO_IDE");
            joinComunes.add("INNER");
            joinComunes.add("R_EXT");  // Multiinteresado
            joinComunes.add("RES_EJE = EXT_EJE AND RES_NUM = EXT_NUM AND RES_UOR = EXT_UOR AND RES_TIP = EXT_TIP AND RES_DEP = EXT_DEP");
            joinComunes.add("INNER");
            //joinComunes.add("T_HTE MULTIINTERESADOS");  // Para poder buscar en multiinteresados, join a traves de R_EXT
            //joinComunes.add("EXT_TER = MULTIINTERESADOS.HTE_TER AND EXT_NVR = MULTIINTERESADOS.HTE_NVR");
            //joinComunes.add("INNER");
            //joinComunes.add("T_HTE TERCERO"); // Para el nombre del tercero que se muestra por defecto, join con R_RES
            //joinComunes.add("RES_TER = TERCERO.HTE_TER AND RES_TNV = TERCERO.HTE_NVR");
            //joinComunes.add("LEFT");
            joinComunes.add("T_HTE TERCERO"); // Para el nombre del tercero que se muestra por defecto, join con R_RES
            joinComunes.add("RES_TER = TERCERO.HTE_TER AND  RES_TNV = TERCERO.HTE_NVR AND EXT_TER = TERCERO.HTE_TER AND EXT_NVR = TERCERO.HTE_NVR");
            joinComunes.add("LEFT");
            joinComunes.add("A_UOR DEST");
            joinComunes.add("RES_UOD = DEST.UOR_COD"); 
            joinComunes.add("LEFT");
            joinComunes.add(GlobalNames.ESQUEMA_GENERICO + "A_UOREX ORG_DES");
            joinComunes.add("RES_UCD = ORG_DES.UOREX_COD AND RES_OCD = ORG_DES.UOREX_ORG ");
            joinComunes.add("LEFT");
            joinComunes.add(GlobalNames.ESQUEMA_GENERICO + "A_UOREX ORG_ORI");
            joinComunes.add("RES_UCO = ORG_ORI.UOREX_COD AND RES_OCO = ORG_ORI.UOREX_ORG ");

            joinComunes.add("RIGHT");
            joinComunes.add(GlobalNames.ESQUEMA_GENERICO + "A_USU A_USU");
            joinComunes.add("A_USU.USU_COD=RES_USU");
            
            // #262348: Obtener la oficina del registro (RES_OFI,UOR_NOM,UOR_COD_VIS).Como ya se hace join con A_UOR por otro campo, se utilizan alias
            String sqlSelectOficina = "";
            if(oficina) {       
                sqlSelectOficina += "RES_OFI AS COD_OFICINA, OFI.UOR_NOM AS NOM_OFICINA, OFI.UOR_COD_VIS AS CODVIS_OFICINA ";
                joinComunes.add("LEFT");
                joinComunes.add("A_UOR OFI");
                joinComunes.add("RES_OFI = OFI.UOR_COD");
            }
            // #288821: Se construye una select para obtener únicamente los números de registro de las anotaciones que superan los filtros
            String sqlSelectResNum = "";
            if(resNum) {       
                sqlSelectResNum += "RES_NUM AS NUMREG ";
            }


            // Parte Where Comun.
            String sqlWhereComun = "RES_DEP = ? AND RES_UOR = ? AND RES_TIP = ? ";
            
            filtros.add(posfiltros++,patron.getIdentDepart());
            filtros.add(posfiltros++,patron.getUnidadOrgan());
            filtros.add(posfiltros++,patron.getTipoReg());
            
            if(("SI".equals(directivaSalidaUsuario))&&("S".equals(patron.getTipoReg()))){
                
                UsuarioValueObject usuVO=patron.getUsuarioLogueado();
                  
                sqlWhereComun=sqlWhereComun+
                     " and res_uod in (select uou_uor from "+GlobalNames.ESQUEMA_GENERICO + "a_uou , a_uor where"
                    + " uou_usu=? and uou_org=? and uou_ent=? and uor_cod=uou_uor and  (UOR_TIP IS NULL OR UOR_TIP=0))";
                
                filtros.add(posfiltros++,usuVO.getIdUsuario());
                filtros.add(posfiltros++,usuVO.getOrgCod());
                filtros.add(posfiltros++,usuVO.getEntCod());
            }

            // Parte join para domicilios no normalizados.
//            ArrayList<String> joinNoNormalizado = new ArrayList<String>();
//            joinNoNormalizado.add("INNER");
//            joinNoNormalizado.add("T_DPO");
//            joinNoNormalizado.add("RES_DOM = DPO_DOM");
//            joinNoNormalizado.add("INNER");
//            joinNoNormalizado.add("T_DSU");
//            joinNoNormalizado.add("DPO_DSU = DSU_COD");
//            joinNoNormalizado.add("INNER");
//            joinNoNormalizado.add(GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN");
//            joinNoNormalizado.add("DSU_PAI = MUN_PAI AND DSU_PRV = MUN_PRV AND DSU_MUN = MUN_COD");

            //Parte join para domicilios normalizados.
            ArrayList<String> joinNormalizado = new ArrayList<String>();
            joinNormalizado.add("INNER");
            joinNormalizado.add("T_DNN");
            joinNormalizado.add("RES_DOM = DNN_DOM");
            joinNormalizado.add("INNER");
            joinNormalizado.add(GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN");
            joinNormalizado.add("DNN_PAI = MUN_PAI AND DNN_PRV = MUN_PRV AND DNN_MUN = MUN_COD");

            // Analizamos los diferentes campos para los criterios de busqueda.
            Vector<String> partesWhere = new Vector<String>();
            // Identificador de Entrada.
            // Anho de Registro.
              if (patron.getAnoReg() != 0) {   
                
                if(patron.getAnoReg()==2013) 
                {
                    partesWhere.addElement("(RES_EJE <2014 AND RES_EJE>2012) ");
                    
                }else 
                {
                partesWhere.addElement("RES_EJE = ? ");
                filtros.add(posfiltros++,patron.getAnoReg());
                }
            }
            // Numero de Registro.
            long numReg = patron.getNumReg();
            if (numReg != 0) {
                partesWhere.addElement("RES_NUM = ? " );
                filtros.add(posfiltros++,numReg);
            }

            String condicion;
            Vector condicionYFiltros=new Vector();
            // Pestanha de Datos Generales.
            // Fecha de Entrada.
            if (patron.getFecEntrada() != null) {
                if (!patron.getFecEntrada().equals("")) {
                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("RES_FEC", patron.getFecEntrada());
                    partesWhere.addElement((String)condicionYFiltros.get(0));
                    Vector filtroOperadores=new Vector();
                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                    for (int j=0;j<filtroOperadores.size();j++)
                    {
                        filtros.add(posfiltros++,filtroOperadores.get(j));
                    }
                        
                    
                    
                }
            }
            // Fecha de Presentacion.
            if (patron.getFecHoraDoc() != null) {
                if (!patron.getFecHoraDoc().equals("")) {
                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("RES_FED", patron.getFecHoraDoc());
                    partesWhere.addElement((String)condicionYFiltros.get(0));
                    Vector filtroOperadores=new Vector();
                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                    for (int j=0;j<filtroOperadores.size();j++)
                    {
                        filtros.add(posfiltros++,filtroOperadores.get(j));
                    }
                }
            }
              m_Log.debug(" ****************   patron.getFechaDocu()= " +patron.getFechaDocu());
            // Fecha de Documento.
            if (patron.getFechaDocu() != null) {
                if (!patron.getFechaDocu().equals("")) {
                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("RES_FEDOC", patron.getFechaDocu());
                    partesWhere.addElement((String)condicionYFiltros.get(0));
                    Vector filtroOperadores=new Vector();
                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                    for (int j=0;j<filtroOperadores.size();j++)
                    {
                        filtros.add(posfiltros++,filtroOperadores.get(j));
                    }
                }
            }
            // Asunto.
            if (patron.getAsunto() != null) {
                if (!patron.getAsunto().trim().equals("")) {
                    consultaInsensitiva=true;
                    String asunto=tratarBusquedaAbierta(patron.getAsunto().trim());
                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("RES_ASU",asunto, false);
                    partesWhere.addElement((String)condicionYFiltros.get(0));
                    Vector filtroOperadores=new Vector();
                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                    for (int j=0;j<filtroOperadores.size();j++)
                    {
                        filtros.add(posfiltros++,filtroOperadores.get(j));
                    }
                }
            }
            // Unidad Tramitadora.
            if (patron.getIdUndTramitad() != null) {
                if (!patron.getIdUndTramitad().trim().equals("")) {
                    //partesWhere.addElement("RES_UOD = " + patron.getIdUndTramitad().trim());
                    partesWhere.addElement("DEST.UOR_COD = ?" );
                    filtros.add(posfiltros++,patron.getIdUndTramitad().trim());
                }
            }
            // Autoridad a la que se dirige
            if (patron.getAutoridad() != null) {
                if (!patron.getAutoridad().trim().equals("")) {
                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("RES_AUT", patron.getAutoridad().trim(), false);
                    partesWhere.addElement((String)condicionYFiltros.get(0));
                    Vector filtroOperadores=new Vector();
                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                    for (int j=0;j<filtroOperadores.size();j++)
                    {
                        filtros.add(posfiltros++,filtroOperadores.get(j));
                    }
                }
            }
            // Observaciones.
            if (patron.getObservaciones() != null) {
                if (!patron.getObservaciones().trim().equals("")) {
                    consultaInsensitiva=true;
                    String observaciones=tratarBusquedaAbierta(patron.getObservaciones().trim());
                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("RES_OBS",observaciones, false);
                    partesWhere.addElement((String)condicionYFiltros.get(0));
                    Vector filtroOperadores=new Vector();
                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                    for (int j=0;j<filtroOperadores.size();j++)
                    {
                        filtros.add(posfiltros++,filtroOperadores.get(j));
                    }
                }
            } 
            
            // anotaciones del usuario logueado (se emplea en el filtro de entradas rechazadas)
            if(patron.isMisRechazadas()){
                 UsuarioValueObject usuVO=patron.getUsuarioLogueado();
                 String codUsu =(String) String.valueOf(usuVO.getIdUsuario());
                condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("USU_COD", codUsu, false);
                partesWhere.addElement((String)condicionYFiltros.get(0));
                Vector filtroOperadores = new Vector();
                filtroOperadores = (Vector)condicionYFiltros.get(1);
                for(int j=0; j<filtroOperadores.size(); j++){
                    filtros.add(posfiltros++, filtroOperadores.get(j));
                }
            }
            
            // rango de fechas de anotación (se emplea en el filtro de entradas rechazadas)
            if(patron.getFechaDesde()!= null && !patron.getFechaDesde().equals("")){
                partesWhere.addElement("RES_FEC BETWEEN ? AND ?");
                filtros.add(posfiltros++, patron.getFechaDesde());
                filtros.add(posfiltros++, patron.getFechaHasta());
            }
            
            //Registro telematico
            if(patron.isRegistroTelematico())
            {
                partesWhere.addElement("REGISTRO_TELEMATICO = ?" );
                filtros.add(posfiltros++, "1");
            }

            // PESTANHA DE INTERESADO.
            // Identificador de Tercero e Identificador de Domicilio (si existe).
            if (patron.getCodInter() != 0) {
                //partesWhere.addElement("MULTIINTERESADOS.HTE_TER = ? " ); 
                partesWhere.addElement("TERCERO.HTE_TER = ? " ); 
                 
                filtros.add(posfiltros++,patron.getCodInter());
                 
            // Se deshabilita la busqueda por domicilio pues la pantalla de busqueda (altaRE) no permite elegirlo
            //if (patron.getDomicInter() != 0) {
            //    partesWhere.addElement("RES_DOM = '" + patron.getDomicInter() + "'");
            //}
            } else {
                // Tipo De Documento de Interesado.
                if (patron.getTipoDocInteresado() != null) {
                    if (!patron.getTipoDocInteresado().trim().equals("")) {
                        //condicion = "MULTIINTERESADOS.HTE_TID = ?" ;
                        condicion = "TERCERO.HTE_TID = ?" ;
                        partesWhere.addElement(condicion);
                        filtros.add(posfiltros++,patron.getTipoDocInteresado().trim());
                    }
                }
                // Tipo De Documento de Interesado.
                if (patron.getDocumentoInteresado() != null) {
                    if (!patron.getDocumentoInteresado().trim().equals("")) {
                        //condicion = "MULTIINTERESADOS.HTE_DOC LIKE ?";
                        condicion = "TERCERO.HTE_DOC LIKE ?";
                        partesWhere.addElement(condicion);
                        filtros.add(posfiltros++,patron.getDocumentoInteresado().trim());
                    }
                }
                // Descripcion del interesado: Nombre o Razon Sozial
                if (patron.getTxtInteresado() != null) {
                    if (!patron.getTxtInteresado().trim().equals("")) {
                        consultaInsensitiva=true;
                        String nombre=tratarBusquedaAbierta(patron.getTxtInteresado().trim());
                        //condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("MULTIINTERESADOS.HTE_NOC", nombre, false);
                        condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("TERCERO.HTE_NOC", nombre, false);
                        partesWhere.addElement((String)condicionYFiltros.get(0));
                        Vector filtroOperadores=new Vector();
                        filtroOperadores=(Vector)condicionYFiltros.get(1);
                        for (int j=0;j<filtroOperadores.size();j++)
                        {
                            filtros.add(posfiltros++,filtroOperadores.get(j));
                        }
                    }
                }
            }

            // PESTANHA OTROS DATOS.
            ArrayList<String> joinOtros = new ArrayList<String>();
            // Tipo de documento adjunto.
            if (patron.getCodTipoDoc() != null) {
                if (!patron.getCodTipoDoc().trim().equals("")) {
                    condicion = "TDO_COD = ?";
                    partesWhere.addElement(condicion);
                    filtros.add(posfiltros++,patron.getCodTipoDoc().trim());
                }
            }

			// Identificador de registro en SIR
			if (patron.getIdentificadorRegistroSIR() != null) {
				joinOtros.add("LEFT");
				joinOtros.add("SIR_DESTINO_R_RES");
				joinOtros.add("SIR_DESTINO_R_RES.RES_DEP = R_RES.RES_DEP AND SIR_DESTINO_R_RES.RES_UOR = R_RES.RES_UOR AND SIR_DESTINO_R_RES.RES_NUM = R_RES.RES_NUM AND SIR_DESTINO_R_RES.RES_EJE = R_RES.RES_EJE AND SIR_DESTINO_R_RES.RES_TIP = R_RES.RES_TIP");

				partesWhere.addElement("NUMEROREGISTROSIR = ? ");
				filtros.add(posfiltros++, patron.getIdentificadorRegistroSIR());
			}


            // Tipo de Entradas.
            // Entrada Destinada a Otro Registro.
            int tipoAnotaciones = patron.getTipoAnot();
            if (tipoAnotaciones == -1) {
                try {
                    ResourceBundle propsRegistro = ResourceBundle.getBundle("Registro");
                    tipoAnotaciones = Integer.parseInt(propsRegistro.getString("Registro/Busqueda/TipoEntradaDefecto"));
                } catch (NumberFormatException nfe) {
                    tipoAnotaciones = -1;
                }
            }
            m_Log.debug("ORG DESTINO = " + patron.getOrgDestino());
            m_Log.debug("UOR DESTINO = " + patron.getIdUndRegDest());
            m_Log.debug("ORG ORIGEN = " + patron.getOrganizacionOrigen());
            m_Log.debug("UOR ORIGEN = " + patron.getIdUndRegOrigen());
            if (tipoAnotaciones == ConstantesDatos.REG_ANOT_TIPO_DESTINO_OTRO_REG) {               
                partesWhere.addElement("RES_MOD = ? ");
                filtros.add(posfiltros++,ConstantesDatos.REG_ANOT_TIPO_DESTINO_OTRO_REG);
                // Organizacion de Origen
                if (patron.getOrgDestino() != null) {
                    partesWhere.addElement("RES_OCD = ? " );
                    filtros.add(posfiltros++,patron.getOrgDestino());
                    
                    // Unidad de registro de origen.
                    if (patron.getIdUndRegDest() != null) {
                        partesWhere.addElement("RES_UCD = ? " );
                        filtros.add(posfiltros++,patron.getIdUndRegDest());
                        
                    }
                }
            // Entrada procedente de otro registro.
            } else if (tipoAnotaciones == ConstantesDatos.REG_ANOT_TIPO_ORIGEN_OTRO_REG) {                
                partesWhere.addElement("RES_MOD = ? ");
                filtros.add(posfiltros++,ConstantesDatos.REG_ANOT_TIPO_ORIGEN_OTRO_REG);
                // Organizacion de Origen.
                if (patron.getOrganizacionOrigen() != null) {
                    if (!patron.getOrganizacionOrigen().trim().equals("")) {
                        partesWhere.addElement("RES_OCO = ? " );
                        filtros.add(posfiltros++,patron.getOrganizacionOrigen());
                        // Unidad Organica de Origen.
                        if (patron.getIdUndRegOrigen() != null) {
                            if (!patron.getIdUndRegOrigen().trim().equals("")) {
                                partesWhere.addElement("RES_UCO = ? " );
                                filtros.add(posfiltros++,patron.getIdUndRegOrigen());
                            }
                        }
                    }
                }
                // Entrada Relacionada. Ejercicio y Numero
                if (patron.getEjeEntradaRel() != null && !patron.getEjeEntradaRel().trim().equals("")) {
                    String valor = patron.getEjeEntradaRel().trim();
                    
                    if(patron.getNumEntradaRel()!= null && !patron.getNumEntradaRel().trim().equals("")) {
                        valor += "/" + patron.getNumEntradaRel().trim();
                    } else {
                        valor += "*";
                    }
                    
                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("RES_ENR", valor);
                    partesWhere.addElement((String)condicionYFiltros.get(0));
                    Vector filtroOperadores=new Vector();
                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                    
                    for (int j=0;j<filtroOperadores.size();j++)
                    {
                        filtros.add(posfiltros++,filtroOperadores.get(j));
                    }
                } else if (patron.getNumEntradaRel() != null && !patron.getNumEntradaRel().trim().equals("")) {
                    String valor = patron.getNumEntradaRel().trim();
                    
                    if(patron.getEjeEntradaRel()!= null && !patron.getEjeEntradaRel().trim().equals("")) {
                        valor = patron.getEjeEntradaRel().trim() + "/" + valor;
                    } else {
                        valor = "*" + valor;
                    }
                    
                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("RES_ENR", valor);
                    partesWhere.addElement((String)condicionYFiltros.get(0));
                    Vector filtroOperadores=new Vector();
                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                    
                    for (int j=0;j<filtroOperadores.size();j++)
                    {
                        filtros.add(posfiltros++,filtroOperadores.get(j));
                    }
                }
            } else if (tipoAnotaciones == ConstantesDatos.REG_ANOT_TIPO_ORDINARIA) {               
                partesWhere.addElement("RES_MOD = ? ");
                filtros.add(posfiltros++,ConstantesDatos.REG_ANOT_TIPO_ORDINARIA);
            }
            // Tipo de remitente.
            if (patron.getCodTipoRemit() != null) {
                if (!patron.getCodTipoRemit().trim().equals("")) {
                    joinOtros.add("INNER");
                    joinOtros.add("R_TPE");
                    joinOtros.add("TPE_IDE = RES_TPE");
                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("TPE_COD", patron.getCodTipoRemit().trim());
                    partesWhere.addElement((String)condicionYFiltros.get(0));
                    Vector filtroOperadores=new Vector();
                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                    for (int j=0;j<filtroOperadores.size();j++)
                    {
                        filtros.add(posfiltros++,filtroOperadores.get(j));
                    }
                }
            }
            
            // Procedimiento (del registro, no del expediente asociado)
            if (org.apache.commons.lang.StringUtils.isNotEmpty(patron.getCodProcedimiento())) {
                partesWhere.addElement("(R_RES.PROCEDIMIENTO = ?)");
                filtros.add(posfiltros++,patron.getCodProcedimiento());
            }
            
            /**** NO FUNCIONA, LE FALTA JOIN CON E_EXR
            // Procedimiento y expediente relacionado.
            if ((patron.getCodProcedimiento() != null && !patron.getCodProcedimiento().trim().equals("")) ||
                    (patron.getNumExpediente() != null && !patron.getNumExpediente().trim().equals(""))) {
                if (patron.getCodProcedimiento() != null) {
                    if (!patron.getCodProcedimiento().trim().equals("")) {
                        partesWhere.addElement("(EXR_PRO = " + "? OR EXR_PRO IS NULL)");
                        filtros.add(posfiltros++,patron.getCodProcedimiento());
                        partesWhere.addElement("(R_RES.PROCEDIMIENTO = " + "?)");
                        filtros.add(posfiltros++,patron.getCodProcedimiento());
                    }
                }
                // Expediente Relacionado ojo!! solo busca los que coinciden, no muestra los nulos
                if (patron.getNumExpediente() != null) {
                    if (!patron.getNumExpediente().trim().equals("")) {
                        partesWhere.addElement("(EXR_NUM = ?)");
                        filtros.add(posfiltros++,patron.getNumExpediente());
                    }
                }
            }
            *******/
            
            // Tipo de transporte.
            if (patron.getCodTipoTransp() != null) {
                if (!patron.getCodTipoTransp().trim().equals("")) {
                    joinOtros.add("INNER");
                    joinOtros.add("R_TTR");
                    joinOtros.add("TTR_IDE = RES_TTR");
                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("TTR_COD", patron.getCodTipoTransp().trim());
                    partesWhere.addElement((String)condicionYFiltros.get(0));
                    Vector filtroOperadores=new Vector();
                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                    for (int j=0;j<filtroOperadores.size();j++)
                    {
                        filtros.add(posfiltros++,filtroOperadores.get(j));
                    }
                } 
            }
            // Numero de Transporte.
            if (patron.getNumTransporte() != null) {
                if (!patron.getNumTransporte().trim().equals("")) {
                    partesWhere.addElement("RES_NTR =? ");
                    filtros.add(posfiltros++,patron.getNumTransporte());
                }
            }

            // Estado de la Anotacion.

            if (patron.getEstAnotacion() != ConstantesDatos.REG_ANOTACION_BUSCAR_TODAS) {

                if (patron.getEstAnotacion() == ConstantesDatos.REG_ANOTACION_BUSCAR_NO_ANULADAS) {
                    //Para buscar las no anuladas                   
                    partesWhere.addElement("(RES_EST IS NULL OR RES_EST <> ?)");
                    filtros.add(posfiltros++,ConstantesDatos.REG_ANOTACION_ESTADO_ANULADA);
                } else if (patron.getEstAnotacion() == 3) {//es el estado de expediente asociado
                    //devuelve todas las que tenga expediente asociado independientemente si estan pendientes o aceptadas
                    partesWhere.addElement("EXR_NUM IS NOT null");
                } else {
                    partesWhere.addElement("RES_EST = ? " );
                    filtros.add(posfiltros++,patron.getEstAnotacion());
                }
            }

            // Asunto codificado.
            if (patron.getCodAsunto() != null) {
                if (!patron.getCodAsunto().trim().equals("")) {
                    partesWhere.addElement("ASUNTO = ? ");
                    filtros.add(posfiltros++,patron.getCodAsunto());
                }
            }

            // PESTANHA TEMAS.
            // Actuaciones.
            if (patron.getCodAct() != null) {
                if (!patron.getCodAct().trim().equals("")) {
                    joinOtros.add("INNER");
                    joinOtros.add("R_ACT");
                    joinOtros.add("RES_ACT = ACT_IDE");
                    partesWhere.addElement("ACT_COD = ? " );
                    filtros.add(posfiltros++,patron.getCodAct().trim());
                }
            }
            // Temas
            Vector temas = patron.getListaTemasAsignados();
            if (temas != null) {
                if (temas.size() > 0) {
                    joinOtros.add("INNER");
                    joinOtros.add("R_RET");
                    joinOtros.add("RET_DEP = RES_DEP AND RET_UOR = RES_UOR AND RET_NUM = RES_NUM AND RET_EJE = RES_EJE " +
                            "AND RET_TIP = RES_TIP");
                    joinOtros.add("INNER");
                    joinOtros.add("R_TEM");
                    joinOtros.add("RET_TEM = TEM_IDE");
                    condicion = "(";
                    for (int i = 0; i < temas.size(); i++) {
                        if (i != 0) {
                            condicion += " OR ";
                        }
                        condicion += "TEM_COD = ? ";
                        filtros.add(posfiltros++,temas.elementAt(i));
                    }
                    condicion += ")";
                    partesWhere.addElement(condicion);
                }
            }

            // Construimos el string con las condiciones comunes del where.
            Iterator itPartesWhere = partesWhere.iterator();
            String sqlPartesWhere = "";
            while (itPartesWhere.hasNext()) {
                sqlPartesWhere += " AND " + itPartesWhere.next();
            }

            // Construccion de las diferentes subsecuencias dependiendo de los tipos de datos.
//            ArrayList<String> joinNoNormalizadoFinal = new ArrayList<String>(joinComunes);
//            joinNoNormalizadoFinal.addAll(joinNoNormalizado);
//            joinNoNormalizadoFinal.addAll(joinOtros);
//            joinNoNormalizadoFinal.add("false");

            ArrayList<String> joinNormalizadoFinal = new ArrayList<String>(joinComunes);
            joinNormalizadoFinal.addAll(joinNormalizado);
            joinNormalizadoFinal.addAll(joinOtros);
            joinNormalizadoFinal.add("false");

            String sqlNoNorm;
            String sqlNorm;
            //var que guarda el tipo de ordenacion por defecto sera por ejercicio y numero de registro

            String ordenacion="";
            if(columna==0){
                if("ASC".equals(tipoOrden)) ordenacion=" ORDER BY RES_EJE ASC, RES_NUM ASC";
                else ordenacion=" ORDER BY RES_EJE DESC, RES_NUM DESC";
            }else if (columna == 5){
                //hay que ordenar por campo numero y eje y en el tipo que nos diga el usuario
                ordenacion=" ORDER BY RES_EJE "+ tipoOrden+", RES_NUM "+ tipoOrden;
            }else if (columna==8){
                ordenacion=" ORDER BY RES_FEC "+ tipoOrden;
            }else if(columna==9){
                ordenacion=" ORDER BY RES_FED "+ tipoOrden;
            } else if (columna==14){
                  
                consultaInsensitiva=true;
                ordenacion=" ORDER BY "+ oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_CONCAT,
                    new String[]{"TERCERO.HTE_PA1", "' '", "TERCERO.HTE_AP1"}) +" "+tipoOrden + ","+ oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_CONCAT,
                    new String[]{"TERCERO.HTE_PA2", "' '", "TERCERO.HTE_AP2"})+" "+ tipoOrden + ", TERCERO.HTE_NOM "+tipoOrden;
            } else {
                //El order by con numero no funciona con first_rows. Por lo que se define la lista de campos para hacer el order by
                ordenacion=" ORDER BY "+ lista.get(columna)+" "+ tipoOrden;
            }

                m_Log.debug("DENTRO DE LA CONSULTA ORDENAR    " + columna + " , " +tipoOrden);
            
            String sqlFinal="";
            if (countAll) {
                //sqlNoNorm = oad.join(selectCount, sqlWhereComun + sqlPartesWhere, joinNoNormalizadoFinal.toArray(new String[]{}));
                sqlNorm = oad.join(selectCount, sqlWhereComun + sqlPartesWhere, joinNormalizadoFinal.toArray(new String[]{}));
                //sqlFinal= "SELECT SUM(TOTAL) FROM (" + sqlNoNorm + " UNION " + sqlNorm + ") TOTALES";
                sqlFinal= "SELECT SUM(TOTAL) FROM (" + sqlNorm + ") TOTALES";
                
              } else if (oficina || resNum){
                String sqlSelectCampo;
                if(oficina) sqlSelectCampo = sqlSelectOficina;
                else sqlSelectCampo = sqlSelectResNum;
                sqlNorm = oad.join(sqlSelectCampo, sqlWhereComun + sqlPartesWhere, joinNormalizadoFinal.toArray(new String[]{}));

                sqlFinal= sqlNorm + " ORDER BY RES_NUM DESC" ; //ordenacion por ejercicio y numero de registro
                
                if(imprimir){	
                   sqlFinal= sqlFinal+ ordenacion;	
                }

            } else {
                  
                if (!imprimir) sqlSelectComun=sqlSelectComun+",ROW_NUMBER() over ("+ordenacion+") RN ";
                
                //sqlNoNorm = oad.join(sqlSelectComun, sqlWhereComun + sqlPartesWhere, joinNoNormalizadoFinal.toArray(new String[]{}));
                sqlNorm = oad.join(sqlSelectComun, sqlWhereComun + sqlPartesWhere, joinNormalizadoFinal.toArray(new String[]{}));

                sqlFinal= sqlNorm  ; //ordenacion por ejercicio y numero de registro
                
                if(imprimir){	
                   sqlFinal= sqlFinal+ ordenacion;	
                }

            }

            retorno.add(sqlFinal);
            retorno.add(filtros);
            retorno.add(consultaInsensitiva);
            return retorno;

        } catch (BDException bde) {
            bde.printStackTrace();
            m_Log.error(bde.getMessage());
            throw new TechnicalException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getRelacionAnotaciones.sql"), bde);
        }
    }

    /**
     * Comprueba la existencia de un asiento.
     * @param reg SimpleRegistroValueObject con la clave del asiento.
     * @param params Parametros de conexión a BD.
     * @return true si existe el asiento, false en caso contrario
     * @throws AnotacionRegistroException
     * * @throws TechnicalException
     */
    public boolean existeAsiento(SimpleRegistroValueObject reg, String[] params)
         throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("AnotacionRegistroDAO.existeAsiento");

        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = oad.getConnection();
            String sql = "SELECT RES_NUM" +
                         " FROM R_RES" +
                         " WHERE RES_UOR = " + reg.getUor() +
                         " AND RES_DEP = " + reg.getDep() +
                         " AND RES_TIP = '" + reg.getTipo() + "'" +
                         " AND RES_EJE = " + reg.getEjercicio() +
                         " AND RES_NUM = " + reg.getNumero();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO.existeAsiento --> " + sql);
            }

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            boolean existeAsiento = false;
            if (rs.next()) {
                 existeAsiento = true;
            }
            return existeAsiento;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getRelacionAnotaciones.sql"), sqle);
        } catch (BDException bde) {
            bde.printStackTrace();
            throw new TechnicalException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getRelacionAnotaciones.sql"), bde);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }

    /**
     * Devuelve la descripcion de un elemento de un asiento.
     * @param codigo codigo del elemento
     * @param nombreTabla nombre de la tabla
     * @param atribCodigo nombre de la columna codigo
     * @param atribCodVis nombre de la columna codigo visual
     * @param atribDesc nombre de la columna descripcion
     * @param codigoNumerico indica si el codigo es numerico (true) o alfanumerico (false)
     * @param con conexion a BD
     * @return Una descripcion en formato 'codigo visual - descripcion'
     * @throws es.altia.agora.business.registro.exception.AnotacionRegistroException
     * @throws es.altia.common.exception.TechnicalException
     */
    public String getDescripcion(String codigo, String nombreTabla,
            String atribCodigo, String atribCodVis, String atribDesc,
            Connection con, boolean codigoNumerico)
            throws AnotacionRegistroException, TechnicalException {

        Statement st = null;
        ResultSet rs = null;
        String desc = "";

        String sql = "SELECT " + atribCodVis +"," + atribDesc +
                    " FROM " + nombreTabla;
        if (codigoNumerico) {
            sql += " WHERE " + atribCodigo + " = " + codigo;
        } else {
            sql += " WHERE " + atribCodigo + " = '" + codigo + "'";
        }

        try {
            m_Log.debug("getDescripcion: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                desc = rs.getString(atribCodVis) + " - " + rs.getString(atribDesc);
            } else {
                throw new AnotacionRegistroException("NO SE ENCONTRO LA DESCRIPCION");
            }
        } catch (SQLException sQLException) {
            sQLException.printStackTrace();
            m_Log.error(sQLException.getMessage());
            throw new AnotacionRegistroException("ERROR",sQLException);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
        }
        return desc;
    }
    
    public String getDescripcion2(String codigo, String nombreTabla,
            String atribCodigo, String atribCodVis, String atribDesc,
            Connection con, boolean codigoNumerico)
            throws AnotacionRegistroException, TechnicalException {

        Statement st = null;
        ResultSet rs = null;
        String desc = "";

        String sql = "SELECT " + atribCodVis +"," + atribDesc +
                    " FROM " + nombreTabla;
        if (codigoNumerico) {
            sql += " WHERE " + atribCodigo + " = " + codigo;
        } else {
            sql += " WHERE " + atribCodigo + " = '" + codigo + "'";
        }

        try {
            m_Log.debug("getDescripcion: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                desc = rs.getString(atribCodVis) + " - " + rs.getString(atribDesc);
            }
        } catch (SQLException sQLException) {
            sQLException.printStackTrace();
            m_Log.error(sQLException.getMessage());
            throw new AnotacionRegistroException("ERROR",sQLException);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
        }
        return desc;
    }

    public String getDescripcionUniRegExterna(String codigoUor, String codigoOrg,
            Connection con)
            throws AnotacionRegistroException, TechnicalException {

        Statement st = null;
        ResultSet rs = null;
        String desc = "";

        String sql = "SELECT UOREX_NOM" +
                    " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOREX" +
                    " WHERE UOREX_COD = " + codigoUor +
                    " AND UOREX_ORG = " + codigoOrg;

        try {
            m_Log.debug("getDescripcionUniRegExterna: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                desc = codigoUor + " - " + rs.getString("UOREX_NOM");
            } else {
                throw new AnotacionRegistroException("NO SE ENCONTRO LA DESCRIPCION");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException("ERROR",e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
        }
        return desc;
    }

    /**
     * Obtiene la descripción de un tipo de asunto.
     * @param codigo codigo del asunto
     * @param unidad unidad organica del asiento
     * @param con conexion a BD
     * @return descipcion del asunto
     * @throws AnotacionRegistroException
     * @throws TechnicalException
     */
    public String getDescAsunto(String codigo, int unidad, Connection con)
        throws AnotacionRegistroException, TechnicalException {

        Statement st = null;
        ResultSet rs = null;
        String desc = "";
        // Se busca por la unidad organica del asiento o -1 indicando que es
        // para todas las unidades. No hay problema pq no se pueden repetir
        // codigos de asunto dentro de la misma unidad y los que son para todas.
        String sql = "SELECT DESCRIPCION FROM R_TIPOASUNTO " +
                     "WHERE CODIGO = '" + codigo + "' " +
                     "AND (UNIDADREGISTRO = " + unidad +
                         " OR UNIDADREGISTRO = -1)";
        try {
            st = con.createStatement();
            m_Log.debug("getDescAsunto: " + sql);

            rs = st.executeQuery(sql);
            if (rs.next()) {
                desc = rs.getString("DESCRIPCION");
            } else {
                throw new AnotacionRegistroException("NO SE ENCONTRO EL ASUNTO");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException("ERROR",e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
        }
        return desc;
    }

    /**
     * Crea un VO con los datos del VO que se pasa pero que contiene también
     * las descripciones de los distintos valores y de los interesados.
     * @param reg RegistroValueObject con los datos
     * @param temasVO indica si los temas que contiene el asiento son
     *        String o RegistroValueObject.
     * @param codProc codigo del procedimiento que esten usando los terceros para
     *        sus roles
     * @param codAsuntoNuevo: Código del asunto codificado actual de la anotación de registro
     * @param con conexión a BD con transaccion abierta
     * @param params parametros de conexión a BD
     * @return VO con descripciones incluidas
     */
    public DescripcionRegistroValueObject crearVOParaHistorico(
            RegistroValueObject reg,boolean temasSonVO, String codProc,Connection con, String[] params)
       throws AnotacionRegistroException, TechnicalException {

      DescripcionRegistroValueObject vo = new DescripcionRegistroValueObject();
      vo.setTipoAsiento(reg.getTipoReg());
      String desc = null;
      String cod = null;

      
      m_Log.debug(" ****************************** AnotacionRegistroDAO.crearVOParaHistorico oficina: " + reg.getCodOficinaRegistro());
      
      // Fechas, se toma la hora del servidor si vienen sin hora
      //ESTA FECHA CORRESPONDE A LA ACTUAL FECHA DE PRESENTACION
      String fechaEntrada = reg.getFecEntrada();
      vo.setFechaEntrada(fechaEntrada.substring(0,10));
      if (fechaEntrada.length() > 11) {
          vo.setHoraEntrada(fechaEntrada.substring(11,16));
      } else {
          SimpleDateFormat hora = new SimpleDateFormat("HH:mm");
          vo.setHoraEntrada(hora.format(new Date()));
      }
      m_Log.debug("GetFechaEntrada" +  vo.getFechaEntrada());
      m_Log.debug("GetHoraEntrada" +  vo.getHoraEntrada());

      //FECHA DE DOCUMENTO
      String fechaDocu=reg.getFechaDocu();
      if (noEsNulo(fechaDocu)) {
          vo.setFechaDocu(fechaDocu.substring(0,10));
      }
      m_Log.debug("GetFechaDocu" +  vo.getFechaDocu());

      // Asunto
      cod = reg.getCodAsunto();
      // original      
      if (noEsNulo(cod)) {
          desc = getDescAsunto(cod, reg.getUnidadOrgan(), con);
          vo.setAsunto(cod + " - " + desc);
      }
      
      // Extracto y observaciones
      vo.setExtracto(AdaptadorSQLBD.js_unescape(reg.getAsunto()));
      desc = reg.getObservaciones();
      if (noEsNulo(desc)) {
          // Varias transformaciones necesarias: el salto de linea \n se cambia
          // por \r\n al leerlo de altaRE.jsp, hay que eliminarlo.
          desc = AdaptadorSQLBD.js_unescape(desc);
          desc = desc.replaceAll("\r","");
          vo.setObservaciones(desc);
      }

      // Unidad tramitadora
      cod = reg.getIdUndTramitad();
      if (noEsNulo(cod)) {
          desc = getDescripcion(cod,"A_UOR","UOR_COD","UOR_COD_VIS","UOR_NOM",con,true);
          vo.setUnidad(desc);
      }

      // Tipo entrada, hay que recuperar la descripción de la utilidad de
      // mensajes al hacer la traduccion, los codigos de los mensajes son:
      // 'entradaOrd', 'DestOtroReg' y 'procOtroReg'
      int tipoEntrada = reg.getTipoAnot();
      vo.setTipoEntrada(Integer.toString(tipoEntrada));

      // Tipo documento, 0 indica ninguno
      cod = reg.getCodTipoDoc();
      if (noEsNulo(cod) && !cod.equals("0")) {
          desc = getDescripcion(cod,"R_TDO","TDO_COD","TDO_COD","TDO_DES",con,false);
          vo.setTipoDoc(desc);
      }

      // Tipo de transporte, 0 indica ninguno
      cod = reg.getCodTipoTransp();
      if (noEsNulo(cod) && !cod.equals("0")) {
          desc = getDescripcion(cod,"R_TTR","TTR_COD","TTR_COD","TTR_DES",con,false);
          vo.setTipoTrans(desc);
      }

      // Numero de transporte
      desc = reg.getNumTransporte();
      if (noEsNulo(desc)) {
          vo.setNumTrans(desc);
      }

      // Tipo de remitente
      cod = reg.getCodTipoRemit();
      if (noEsNulo(cod)) {
          desc = getDescripcion(cod,"R_TPE","TPE_COD","TPE_COD","TPE_DES",con,false);
          vo.setTipoRem(desc);
      }

      // Procedimiento
      cod = reg.getCodProcedimiento();
      if (noEsNulo(cod)) {
          desc = reg.getDescProcedimiento();
          vo.setProcedimiento(cod + " - " + desc);
      }

      // Expediente
      desc = reg.getNumExpediente();
      if (noEsNulo(desc)) {
          vo.setExpediente(desc);
      }

      // Actuacion
      cod = reg.getCodAct();
      if (cod != null && !cod.equals("0")) {
          desc = getDescripcion(cod,"R_ACT","ACT_COD","ACT_COD","ACT_DES",con,false);
          vo.setActuacion(desc);
      }

      // Autoridad
      desc = reg.getAutoridad();
      if (noEsNulo(desc)) {
          vo.setAutoridad(AdaptadorSQLBD.js_unescape(desc));
      }

      // Organizacion/unidad  origen/destino
      if (tipoEntrada == ConstantesDatos.REG_ANOT_TIPO_DESTINO_OTRO_REG) {
          // Destino otro registro
          // Organizacion de destino
          String codOrg = reg.getOrgDestino();
          if (noEsNulo(codOrg)) {
              desc = getDescripcion(codOrg,GlobalNames.ESQUEMA_GENERICO + "A_ORGEX",
                      "ORGEX_COD", "ORGEX_COD", "ORGEX_DES", con, true);
              vo.setOrgDestino(desc);
              // Unidad de destino
              String codUor = reg.getIdUndRegDest();
              if (noEsNulo(codUor)) {
                 desc = getDescripcionUniRegExterna(codUor, codOrg, con);
                 vo.setUniDestino(desc);
              }
          }
      } else if (tipoEntrada == ConstantesDatos.REG_ANOT_TIPO_ORIGEN_OTRO_REG) {
          // Origen otro registro
          // Organizacion de origen
          String codOrg = reg.getOrganizacionOrigen();
          if (noEsNulo(codOrg)) {
              desc = getDescripcion(codOrg,GlobalNames.ESQUEMA_GENERICO + "A_ORGEX",
                      "ORGEX_COD", "ORGEX_COD", "ORGEX_DES", con, true);
              vo.setOrgProc(desc);
              // Unidad de origen
              String codUor = reg.getIdUndRegOrigen();
              if (noEsNulo(codUor)) {
                 desc = getDescripcionUniRegExterna(codUor, codOrg, con);
                 vo.setUniProc(desc);
              }
          }
          // Entrada relacionada
          desc = reg.getEjeOrigen();
          if (noEsNulo(desc)) vo.setEntradaRel(desc);
      }

      // TEMAS - Dependiendo del caso en el que estemos, tendremos un Vector de
      // RegistroValueObjects con los codigos de tema en el atributo codigoTema
      // o un Vector de String (codigos visuales).
      if(reg.getListaTemasAsignados()!=null){
		Vector<String> temas = new Vector<String>();
		if (temasSonVO) {
			for (RegistroValueObject tema : (Vector<RegistroValueObject>) reg.getListaTemasAsignados()) {
				desc = getDescripcion(tema.getCodigoTema(), "R_TEM", "TEM_COD", "TEM_COD", "TEM_DES", con, false);
				temas.add(desc);
			}
		} else {
			for (String codigoTema : (Vector<String>) reg.getListaTemasAsignados()) {
				desc = getDescripcion(codigoTema, "R_TEM", "TEM_COD", "TEM_COD", "TEM_DES", con, false);
				temas.add(desc);
			}
		}
		vo.setTemas(temas);
      }

      // RELACIONES - Se copian tal cual
      if(reg.getRelaciones()!=null){
		vo.setRelaciones(reg.getRelaciones());
      }
      
      //REGISTRO TELEMATICO
      vo.setRegistroTelematico((reg.isRegistroTelematico())?"SI":"NO");

      // DOCUMENTOS - Al igual que los temas, vienen en un Vector de
      // RegistroValueObject con los datos en los atributos nombreDoc, fechaDoc y tipoDoc.
            // tipoDoc y cotejoDocs.
     if(reg.getListaDocsAsignados()!=null){
		Vector<String> nombreDocs = new Vector<String>();
		Vector<String> tipoDocs = new Vector<String>();
		Vector<String> fechaDocs = new Vector<String>();
        Vector<String> cotejoDocs = new Vector<String>();
		       for (RegistroValueObject doc : (Vector<RegistroValueObject>) reg.getListaDocsAsignados()) {
             if (doc.getEstadoDocumentoRegistro() != ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO) {
                 nombreDocs.add(doc.getNombreDoc());
                 tipoDocs.add(doc.getTipoDoc());
                 fechaDocs.add(doc.getFechaDoc());
                 cotejoDocs.add(doc.getCotejado());
             }
         }
		vo.setNombreDocs(nombreDocs);
		vo.setTipoDocs(tipoDocs);
		vo.setFechaDocs(fechaDocs);
        vo.setCotejoDocs(cotejoDocs);
     }
      // TERCEROS - Recuperamos todos los datos de los terceros de esta anotacion
      // de BD, pasando el código de procedimiento correspondiente a los roles que
      // esten usando los terceros de la anotacion.
      Vector<GeneralValueObject> terceros =
          InteresadosDAO.getInstance().getListaInteresadosRegistro(reg, codProc, con, params);

      Vector<String> codTerceros = new Vector<String>();
      Vector<String> versionTerceros = new Vector<String>();
      Vector<String> codDomicilios = new Vector<String>();
      Vector<String> rolesTerceros = new Vector<String>();
      Vector<String> descTerceros = new Vector<String>();
      String descTercero = null;
      String cp = null;
      String telefono = null;
      String email = null;
      String domicilio = null;

      for (GeneralValueObject genVO : terceros) {
          // Vectores que se usaran para comparar los terceros, para los roles
          // usamos la descripción del rol para comprobar si ha cambiado.
          codTerceros.add((String) genVO.getAtributo("codigoTercero"));
          versionTerceros.add((String) genVO.getAtributo("versionTercero"));
          codDomicilios.add((String) genVO.getAtributo("domicilio"));
          rolesTerceros.add((String) genVO.getAtributo("descRol"));
          // Creamos la descripcion del tercero.
          descTercero = (String) genVO.getAtributo("descRol") + ": " +
                        (String) genVO.getAtributo("titular") + ", " +
                        (String) genVO.getAtributo("tipoDoc") + ": " +
                        (String) genVO.getAtributo("doc");

          domicilio = ((String) genVO.getAtributo("descDomicilio")).trim();
          if (noEsNulo(domicilio)) {
              descTercero += ", " + domicilio;
          }

          cp = (String) genVO.getAtributo("cp");
          if (noEsNulo(cp)) {
              descTercero += ", " + cp;
          }

          descTercero += ", " + (String) genVO.getAtributo("municipio") +
                         ", " + (String) genVO.getAtributo("provincia");

          telefono = (String) genVO.getAtributo("telefono");
          if (noEsNulo(telefono)) {
              descTercero += ", " + telefono;
          }

          email = (String) genVO.getAtributo("email");
          if (noEsNulo(email)) {
              descTercero += ", " + email;
          }
          descTerceros.add(descTercero);
      }

      vo.setCodTerceros(codTerceros);
      vo.setVersionTerceros(versionTerceros);
      vo.setCodDomicilios(codDomicilios);
      vo.setRolesTerceros(rolesTerceros);
      vo.setDescTerceros(descTerceros);

                                      
		if (reg.getCodOficinaRegistro() != null) {
			vo.setCodOficinaRegistro(reg.getCodOficinaRegistro());
			cod = String.valueOf(reg.getCodOficinaRegistro());
		} else {
			cod = "";
		}
		
      if(noEsNulo(cod)){
          String descOficinaRegistro=getDescripcion2(cod, "A_UOR","UOR_COD","UOR_COD_VIS","UOR_NOM", con, true);
          if(!"".equals(descOficinaRegistro))
          vo.setDescOficinaRegistro(descOficinaRegistro);
      }//if(noEsNulo(cod)) 
      
      m_Log.debug(vo);
      return vo;
    }



    /**
     *  Obtiene la lista de los registros que tienen asociado un determinado asunto.
     *
     * @param asunto El MantAsuntosValueObject que representa el asunto.
     * @param conexion Conexión a la BD con transacción abierta.
     * @return Vector con los registros que tienen asociado un determinado asunto.
     */
    public Vector<RegistroValueObject> getListaRegistrosPorAsunto(MantAsuntosValueObject asunto, String[] params)
            throws AnotacionRegistroException {
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("AnotacionRegistroDAO -> getListaRegistrosPorAsunto");
        }

        Vector<RegistroValueObject> listaRegistros = new Vector<RegistroValueObject>();
        String sql;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AdaptadorSQLBD abd = null;
        Connection conexion  = null;
        try {
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            sql = "SELECT RES_DEP, RES_UOR, RES_TIP, RES_EJE, RES_NUM FROM R_RES "
                    + " WHERE ASUNTO =? ";

            if (!asunto.getTipoRegistro().equals("A")) {
                sql += " AND RES_TIP=? ";
            }

            if (!asunto.getUnidadRegistro().equals("-1")) {
                sql += " AND RES_UOR=? ";
            }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            ps = conexion.prepareStatement(sql);
			int contbd = 1;
            ps.setString(contbd++, asunto.getCodigo());

            if (!asunto.getTipoRegistro().equals("A")) {
                ps.setString(contbd++, asunto.getTipoRegistro());
            }

            if (!asunto.getUnidadRegistro().equals("-1")) {
                ps.setInt(contbd++, Integer.parseInt(asunto.getUnidadRegistro()));
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                RegistroValueObject regVO = new RegistroValueObject();
                regVO.setIdentDepart(rs.getInt(1));
                regVO.setUnidadOrgan(rs.getInt(2));
                regVO.setTipoReg(rs.getString(3));
                regVO.setAnoReg(rs.getInt(4));
                regVO.setNumReg(rs.getLong(5));
                listaRegistros.addElement(regVO);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            m_Log.error(sqle.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaRegistrosPorAsunto"), sqle);

        } catch(BDException bde){
            bde.printStackTrace();
            m_Log.error(bde.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getListaRegistrosPorAsunto"), bde);

        }finally {
            try {
                SigpGeneralOperations.closeStatement(ps);
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.devolverConexion(abd, conexion);
            } catch (Exception e) {
                e.printStackTrace();
                m_Log.debug(e.getMessage());
            }
        }
        return listaRegistros;
    }



     /**
     *  Modifica el asunto de un determinado registro.
     *
     * @param registroValueObject RegistroValueObject que se va actualizar.
     * @param conexion Conexión a la BD con transacción abierta.
     */
    public void modificarAsuntoRegistro(RegistroValueObject registroValueObject, Connection conexion)
            throws AnotacionRegistroException, TechnicalException {

        if (m_Log.isDebugEnabled())  m_Log.debug("AnotacionRegistroDAO -> modificarAsuntoRegistro");
        String sql;
        PreparedStatement ps = null;

        try{
             sql = "UPDATE R_RES SET ASUNTO=? " +
                     " WHERE RES_DEP=? AND RES_UOR=? AND  RES_TIP=? AND RES_EJE=? AND RES_NUM=? ";

            if (m_Log.isDebugEnabled())
                m_Log.debug(sql);
            ps = conexion.prepareStatement(sql);
            ps.setString(1, registroValueObject.getCodAsunto());
            ps.setInt(2, registroValueObject.getIdentDepart());
            ps.setInt(3, registroValueObject.getUnidadOrgan());
            ps.setString(4, registroValueObject.getTipoReg());
            ps.setInt(5, registroValueObject.getAnoReg());
            ps.setLong(6, registroValueObject.getNumReg());

            ps.executeUpdate();

        }catch (SQLException bde) {
            bde.printStackTrace();
            m_Log.error(bde.getMessage());
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.modificarAsuntoRegistro"), bde);

        } finally {
                SigpGeneralOperations.closeStatement(ps);
        }
    }

    /**
     * Devuelve true si y solo si el string no es nulo ni vacio.
     * @param str string que se quiere probar
     * @return true o false dependiendo de si el string es nulo o vacio
     */
    private boolean noEsNulo(String str) {
        if (str!=null && !str.equals(""))
            return true;
        else
            return false;
    }

    /**
     * Factory method para el<code>Singelton</code>.
     * @return La unica instancia de SelectListaDAO.The only CustomerDAO instance.
     */
    public static AnotacionRegistroDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una
        synchronized (AnotacionRegistroDAO.class) {
            if (instance == null) {
                instance = new AnotacionRegistroDAO();
            }
        }

        return instance;
    }

    /**
     * Para realizar busqueda abierta, rodea el string con "*" si no contiene
     * ninguno de los comodines "*", "&" o "|".
     * @param str String a tratar
     * @return String rodeado de "*", si es el caso
     */

    private String tratarBusquedaAbierta(String str){
        if (str != null && !str.equals("")) {
            if (!str.contains("*") && !str.contains("&") && !str.contains("|")) {
                str = "*" + str + "*";
            }
        }
        return str;
    }

    /*
     * Mi propia instancia. Usada en el metodo getInstance
     */
    private static AnotacionRegistroDAO instance = null;


    /**** oscar **/

    /**
     * Comprueba cual es la anotación más antigua que ha iniciado o que se ha adjuntado a un expediente.
     * Se debe comprobar tanto para las anotaciones que se dan de alta en el SIGP como las que proceden de
     * fuentes externas
     * @param numeroExpediente: Número del expediente
     * @param con: Conexión a la base de datos
     * @return GeneralValueObject
     * @throws es.altia.agora.business.registro.exception.AnotacionRegistroException
     * @throws es.altia.common.exception.TechnicalException
     */
     public GeneralValueObject getAnotacionMasAntigua(String numeroExpediente, Connection con)
            throws AnotacionRegistroException, TechnicalException {

        PreparedStatement ps = null;
        ResultSet rs = null;        
        GeneralValueObject anotacion = new GeneralValueObject();        
        String BARRA = "/";
        /** Se recupera la anotación más antigua que esté asociada al expediente de entre las que se han dado
         * de alta en el SIGP
         */
        String sql = "select res_fec, exr_ejr,exr_nre from e_exr,r_res " +
                          "where exr_num=? " +
                          "and exr_ejr = res_eje " +
                          "and exr_tip = res_tip " +
                          "and exr_uor = res_uor " +
                          "and exr_nre = res_num " +
                          "and res_fec= (select min(res_fec) from r_res,e_exr " +
                          "where exr_num=? " +
                          "and exr_ejr = res_eje " +
                          "and exr_tip = res_tip " +
                          "and exr_uor = res_uor " +
                           "and exr_nre = res_num) " +
                           "order by exr_eje asc,exr_nre asc";
         
        try {
            m_Log.debug("getAnotacionMasAntigua: " + sql);
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setString(i++,numeroExpediente);
            ps.setString(i++,numeroExpediente);

            rs = ps.executeQuery();

            int ejercicioRegistroSGE = 0;
            int numeroRegistroSGE = 0;
            Calendar fechaRegistroSGE = null;            
            while(rs.next()){
                fechaRegistroSGE = DateOperations.toCalendar(rs.getTimestamp("RES_FEC"));
                ejercicioRegistroSGE = rs.getInt("EXR_EJR");
                numeroRegistroSGE  = rs.getInt("EXR_NRE");
                break;
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);

            /** Se obtiene la anotación más antigua que pueda tener asociada el expediente para las anotaciones
             * procedentes de fuentes externas
             */
             sql = "SELECT EXREXT_FECALTA, EXREXT_EJR, EXREXT_NRE " +
                      "FROM E_EXREXT " +
                      "WHERE EXREXT_NUM=? " +
                      "AND EXREXT_FECALTA IS NOT NULL " +
                      "AND EXREXT_FECALTA=(SELECT MIN(EXREXT_FECALTA) " +
                      "FROM E_EXREXT " +
                      "WHERE EXREXT_FECALTA IS NOT NULL AND EXREXT_NUM=?) " +
                      "ORDER BY EXREXT_EJR ASC,EXREXT_NRE ASC ";

            m_Log.debug("getAnotacionMasAntigua: " + sql);
             i=1;
             ps = con.prepareStatement(sql);
             ps.setString(i++,numeroExpediente);
             ps.setString(i++,numeroExpediente);

             Calendar fechaRegistroExterna = null;
             int ejercicioExterno  =0;
             String numeroExterno = "0";
             rs = ps.executeQuery();
             while(rs.next()){
                fechaRegistroExterna = DateOperations.toCalendar(rs.getTimestamp("EXREXT_FECALTA"));
                ejercicioExterno = rs.getInt("EXREXT_EJR");
                numeroExterno  = rs.getString("EXREXT_NRE");
                break;
             }

             String fechaFinal = "";
             String numeroRegistroFinal = "";
             if(fechaRegistroExterna!=null && fechaRegistroSGE!=null){
                 if(fechaRegistroSGE.before(fechaRegistroExterna)){
                     fechaFinal = DateOperations.toString(fechaRegistroSGE, "dd/MM/yyyy");
                     numeroRegistroFinal = ejercicioRegistroSGE + BARRA + numeroRegistroSGE;
                 }else{
                     fechaFinal = DateOperations.toString(fechaRegistroExterna, "dd/MM/yyyy");
                     numeroRegistroFinal = ejercicioExterno + BARRA + numeroExterno;
                 }
             }

             if(fechaRegistroExterna!=null && fechaRegistroSGE==null){
                 fechaFinal = DateOperations.toString(fechaRegistroExterna, "dd/MM/yyyy");
                 numeroRegistroFinal = ejercicioExterno + BARRA + numeroExterno;
             }else
             if(fechaRegistroExterna==null && fechaRegistroSGE!=null){
                 fechaFinal = DateOperations.toString(fechaRegistroSGE, "dd/MM/yyyy");
                numeroRegistroFinal = ejercicioRegistroSGE + BARRA + numeroRegistroSGE;
             }


            anotacion.setAtributo(ConstantesDatos.FECHA_ENTRADA_REGISTRO_ANOTACION,fechaFinal);
            anotacion.setAtributo(ConstantesDatos.NUMERO_REGISTRO_ANOTACION,numeroRegistroFinal);

        } catch (SQLException e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new AnotacionRegistroException("ERROR",e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
        }
        return anotacion;
    }

     
      /**
     * Método auxiliar que te devuelve un nombre de oficina, para la funcionalidad
     * imprimirCuneus
     * @param codOficinaRegistro codigo de la oficina, de la que queremos obtener el nombre
     * @param params información para conectarse a la BD
     * @return String nombre de la oficina
     * @throws AnotacionRegistroException
     * * @throws TechnicalException
     */
    public String dameNombreOficina(Integer codOficinaRegistro, String[] params)
         throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("AnotacionRegistroDAO.dameNombreOficina. CodOficina pasado:"+ codOficinaRegistro);

        String nombreOficina="";
        UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],String.valueOf(codOficinaRegistro));
        
        if (uorDTO!=null)
            nombreOficina = uorDTO.getUor_nom();

        return nombreOficina;
    }
 
    
       /**
     * Método auxiliar que te devuelve un codigo de oficina, para la funcionalidad
     * imprimirCuneus
     * @param codUor codigo  de la unidad orgánica
     * @param numAnotacion numero de Anotacion
     * @param resEje ejercicio de la Anotacion
     * @param tipoReg tipo de registro (Entrada "E" o Salida "S") 
     * @return int codigo de la Oficina
     * @throws AnotacionRegistroException
     * * @throws TechnicalException
     */
    public int dameCodigoOficina(Integer codDepto, Integer codUor, long numAnotacion,int resEje, String tipoReg, String[] params)
         throws AnotacionRegistroException, TechnicalException {

        //Queremos estar informados de cuando este metod es ejecutado
        m_Log.debug("AnotacionRegistroDAO.dameNombreOficina. CodUor pasado:"+ codUor);
        m_Log.debug("AnotacionRegistroDAO.dameNombreOficina. NumAnotacion  pasado:"+ numAnotacion);

        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        int codigoOficina;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = oad.getConnection();
            if(codDepto == null){
                codDepto = 1;
            }
                
            String sql = "SELECT RES_OFI" +
                        " FROM R_RES" +
                        " WHERE RES_NUM= "+ numAnotacion+
                        " AND RES_DEP = " + codDepto +
                        " AND RES_UOR= " + codUor +
                        " AND RES_EJE= " + resEje +
                        " AND RES_TIP= '" + tipoReg+ "'";
            
                        
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("AnotacionRegistroDAO.dameCodigoOficina --> " + sql);
            }

            st = con.createStatement();
            rs = st.executeQuery(sql);
           
            if (rs.next()) {
                codigoOficina = rs.getInt("RES_OFI");
                return codigoOficina;
            } else //Se devuelve un codigo que no existe 
                return -55555;
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.dameCodigoOficina.sql"), sqle);
        } catch (BDException bde) {
            bde.printStackTrace();
            throw new TechnicalException(m_ConfigError.getString("Error.AnotacionRegistroDAO.dameCodigoficinasql"), bde);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }
    
    public int dameCodigoOficina(Integer codUor, long numAnotacion,int resEje, String tipoReg, String[] params)
         throws AnotacionRegistroException, TechnicalException {
        return dameCodigoOficina(null, codUor, numAnotacion, resEje, tipoReg, params);
    }
 
    public String getOficinaUorRegistro (String tipoEntrada, Integer ejercicio, Long numero, Integer codDepto, Integer codUor, String[] params) throws AnotacionRegistroException, TechnicalException{
        int codOficina = dameCodigoOficina(codDepto, codUor, numero, ejercicio, tipoEntrada, params);
        return dameNombreOficina(codOficina, params);
    }
    
    public DocumentoAnotacionRegistroVO getDocumentoAnotacionRegistro(DocumentoAnotacionRegistroVO doc,Connection con){        
        Statement st = null;
        ResultSet rs = null;
                
        try{
            
            String sql = "SELECT RED_DOC,RED_TIP_DOC,RED_IDDOC_GESTOR, RED_NOM_DOC FROM R_RED WHERE RED_DEP=" + doc.getCodDepartamento() + " AND RED_UOR=" + doc.getCodigoUorRegistro() +
                         " AND RED_EJE=" + doc.getEjercicio() + " AND RED_NUM=" + doc.getNumeroAnotacion() + " AND RED_TIP='" + doc.getTipoEntrada() + "' " + 
                         " AND RED_NOM_DOC='" + doc.getNombreDocumento() + "'";
            
            m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            byte[] fichero = null;
            while(rs.next()){
                InputStream is = rs.getBinaryStream("RED_DOC");
                if (!rs.wasNull()) {
                    ByteArrayOutputStream ot = new ByteArrayOutputStream();
                    int c;
                    while ((c = is.read()) != -1) {
                        ot.write(c);
                    }
                    ot.flush();
                    fichero = ot.toByteArray();

                    ot.close();
                    is.close();
                }
                
                doc.setContenido(fichero);          
                doc.setTipoDocumento(rs.getString("RED_TIP_DOC"));                
                doc.setIdDocGestor(rs.getString("RED_IDDOC_GESTOR"));                
                doc.setNombreDocumento(rs.getString("RED_NOM_DOC"));
            }
            
        }catch(SQLException e){
            m_Log.error(this.getClass().getName() + " - Error al recuperar ejecutar la consulta sobre la BBDD: " + e.getMessage());
        }catch(IOException e){
            m_Log.error("Error al ejecutar la consulta sobre la BBDD: " + e.getMessage());
        }
        finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                
            }catch(SQLException e){
                
            }
        }
        
        return doc;
    }
    
    
    
    public void eliminarDocsRegistro(Connection con, RegistroValueObject regVO)
            throws AnotacionRegistroException {

        m_Log.debug("insertarDocsRegistro");
        
        PreparedStatement ps = null;

        try {

            long numReg3 = regVO.getNumReg(); 
            String sql3 = "DELETE FROM R_RED WHERE " + sql_docsDepto + "=" +
                    regVO.getIdentDepart() + " AND " + sql_docsUnid + "=" +
                    regVO.getUnidadOrgan() + " AND " + sql_docsTipo + "='" + regVO.getTipoReg() +
                    "' AND " + sql_docsEjerc + "=" + regVO.getAnoReg() + " AND " +
                    sql_docsNum + "=" + numReg3;
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Sentencia SQL DE BORRAR Docs :" + sql3);
            }
            ps = con.prepareStatement(sql3);
            int res = ps.executeUpdate();
            m_Log.debug("las filas afectadas en el delete son : " + res);
            

        } catch (SQLException ex) {
            m_Log.error("eliminarDocsRegistro: " + ex.getMessage());  
            ex.printStackTrace();            
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.eliminarDocumentos.sql"), ex);
        } catch (Exception e) {
            m_Log.error("eliminarDocsRegistro: " + e.getMessage());  
            e.printStackTrace();            
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.eliminarDocumentos.sql"), e);            
        } finally {
            try{
                SigpGeneralOperations.closeStatement(ps);
            }catch(TechnicalException e){
                m_Log.error("Error al cerrar recurso asociado a la conexión a la BBDD:" + e.getMessage());
            }
        }
    }
    
    
    
     /**
     * Modifica un determinado documento aportado anteriormente 
     * @param con Conexión con la BD
     * @param regVO Registro
     * @param params Paramétros de la conexión
     * @throws AnotaciónRegistroException. En caso de que se produzca algún error
     */
    public void modificarEntregadosAnterior(RegistroValueObject doc, Connection con)throws AnotacionRegistroException, ParseException{
        m_Log.debug("ModificarEntregadorAnterior =====>");
        PreparedStatement psUpdate=null;
        try{
            int codigoDepartamento=doc.getIdentDepart();
            int codigoUnidad=doc.getUnidadOrgan();
            String tipoRegistro=doc.getTipoReg();
            int ejercicio=doc.getAnoReg();
            Long numeroRegistro=doc.getNumReg();
            
            String sql="UPDATE R_DOC_APORTADOS_ANTERIOR SET R_DOC_APORTADOS_NOM_DOC = '"+doc.getNombreDocAnterior()+
                    "', R_DOC_APORTADOS_TIP_DOC = '"+doc.getTipoDocAnterior()+
                    "', R_DOC_APORTADOS_ORGANO = '"+doc.getOrganoDocAnterior()+
                    "', R_DOC_APORTADOS_FEC_DOC = ?"+
                    " WHERE " +
                    "R_DOC_APORTADOS_NOM_DOC = '"+doc.getNombreDocAnteriorMod()+
                    "' AND R_DOC_APORTADOS_DEP = " + codigoDepartamento + 
                    " AND R_DOC_APORTADOS_UOR = " + codigoUnidad +
                    " AND R_DOC_APORTADOS_TIP = '" + tipoRegistro +
                    "' AND R_DOC_APORTADOS_NUM = " + numeroRegistro +
                    " AND R_DOC_APORTADOS_EJE = " + ejercicio;
            m_Log.debug("ModificarEntregadosAnterior: "+sql);
            
              psUpdate = con.prepareStatement(sql);
               
            if (doc.getFechaDocAnterior() != null) {
                    SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
                    Date dt = parser.parse(doc.getFechaDocAnterior());
                    java.sql.Date sqlDate = new java.sql.Date(dt.getTime());
                    psUpdate.setDate(1, sqlDate);
                } else {
                    psUpdate.setNull(1, java.sql.Types.TIMESTAMP);
                }

             int rs = psUpdate.executeUpdate();
        }catch(SQLException ex){
            m_Log.error("ModificarEntregadosAnterior: "+ex.getMessage());
            ex.printStackTrace();
        }finally {
            try{
                SigpGeneralOperations.closeStatement(psUpdate);
            }catch(TechnicalException e){
                m_Log.error("Error al cerrar recursos asociados a la conexión de base de datos: " + e.getMessage());
            }
        }
    }
    /**
     * Elimina un determinado documento aportado anteriormente registro
     * @param con Conexión con la BD
     * @param regVO Registro
     * @param params Paramétros de la conexión
     * @throws AnotacionRegistroException. En caso de que se produzca algún error
     */
    public void eliminarEntregadosAnterior(RegistroValueObject doc, Connection con)throws AnotacionRegistroException{
        m_Log.debug("EliminarEntregadosAnterior  ====>");
        Statement st=null;
        try{
            String sql= "DELETE FROM R_DOC_APORTADOS_ANTERIOR WHERE R_DOC_APORTADOS_DEP="+doc.getIdentDepart()+
                    " AND R_DOC_APORTADOS_UOR="+doc.getUorCodVisible()+" AND R_DOC_APORTADOS_EJE="+doc.getAnoReg()+
                    " AND R_DOC_APORTADOS_NUM="+doc.getNumReg()+" AND R_DOC_APORTADOS_TIP='"+doc.getTipoReg()+"' AND R_DOC_APORTADOS_NOM_DOC='"+doc.getNombreDocAnterior()+"'";
            
            m_Log.debug("Sentencia SQL de borrar Doc aportados anteriormente: "+sql);
            st=con.createStatement();
            int res=st.executeUpdate(sql);
            m_Log.debug("Las filas afectadas en el delete son :"+res);
        }catch(SQLException ex){
            m_Log.error("EliminarEntregadosAnterior: "+ex.getMessage());
            ex.printStackTrace();
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAo.eliminarEntregadosAnterior.sql"),ex);
        }catch(Exception e){
             m_Log.error("EliminarEntregadosAnterior: "+e.getMessage());
             e.printStackTrace();
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAo.eliminarEntregadosAnterior.sql"),e);
        }finally {
            try{
                SigpGeneralOperations.closeStatement(st);
            }catch(TechnicalException e){
                m_Log.error("Error al cerrar recursos asociados a la conexión de base de datos: " + e.getMessage());
            }
        }
    }
    
    
    
    /**
     * Elimina un determinado documento de registro     
     * @param con Conexión con la BD
     * @param regVO Registro
     * @param params Parametros de la conexion
     * @throws AnotacionRegistroException. En caso de que se produzca algún error.
     */
    public void eliminarDocumentoRegistro(Documento doc,Connection con) throws AnotacionRegistroException {
        m_Log.debug("eliminarDocumentoRegistro ===>");        
        Statement st = null;

        try {
            
             // Eliminamos los metadatos si existen (si cualquiera de los valores no es nulo (salvo ID_DOCUMENTO)
            if (doc.getVersionNTIMetadatos() != null) {
                eliminarMetadatosDocumentoRegistro(doc, con);
            }
            
            String sql = "DELETE FROM R_RED WHERE RED_DEP=" + doc.getCodigoDepartamento() + 
                          " AND RED_UOR=" + doc.getCodigoUnidadOrganica() + " AND RED_EJE=" + doc.getEjercicioAnotacion() + 
                          " AND RED_NUM=" + doc.getNumeroRegistro() + " AND RED_TIP='" + doc.getTipoRegistro() + "' AND RED_NOM_DOC='" + doc.getNombreDocumento() + "'";
            
            m_Log.debug("Sentencia SQL DE BORRAR Docs :" + sql);
            st = con.createStatement();
            int res = st.executeUpdate(sql);
            
            m_Log.debug("las filas afectadas en el delete son : " + res);
            

        } catch (SQLException ex) {
            m_Log.error("eliminarDocsRegistro: " + ex.getMessage());  
            ex.printStackTrace();            
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.eliminarDocumentos.sql"), ex);
        } catch (Exception e) {
            m_Log.error("eliminarDocsRegistro: " + e.getMessage());  
            e.printStackTrace();            
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.eliminarDocumentos.sql"), e);            
        } finally {
            try{
                SigpGeneralOperations.closeStatement(st);
            }catch(TechnicalException e){
                m_Log.error("Error al cerrar recursos asociados a la conexión de base de datos: " + e.getMessage());
            }
        }
    }
            
    
    
    /**
     * Obtiene el nombre completo del interesado filtrando por codigo y version
     * @param con Conexion de bbdd
     * @param oad Objeto AdaptadorSQLBD
     * @return Objeto TercerosValueObject con el nombre y los apellidos del tercero buscado
     */
    public RegistroValueObject getDatosAnotacionById(RegistroValueObject anotacion, Connection con, AdaptadorSQLBD oad) throws TechnicalException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder query = null;
        int depart = anotacion.getIdentDepart();
        int unidad = anotacion.getUnidadOrgan();
        String tipo = anotacion.getTipoReg();
        int ano = anotacion.getAnoReg();
        long numero = anotacion.getNumReg();
        
        try {
            String selectFechaEnt = oad.convertir("R_RES.RES_FEC", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS");
            String selectFechaDoc = oad.convertir("R_RES.RES_FED", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY HH24:MI:SS");
            String selectAp1 = oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA1", "' '", "T_HTE.HTE_AP1"});
            String selectAp2 = oad.funcionCadena(AdaptadorSQL.FUNCIONCADENA_CONCAT, new String[]{"T_HTE.HTE_PA2", "' '", "T_HTE.HTE_AP2"});
            
            query = new StringBuilder("SELECT R_RES.RES_EST, ").append(selectFechaEnt).append(" AS FECHA, ").append(selectFechaDoc)
                    .append(" AS FECHADOC, R_RES.RES_ASU, ").append("T_HTE.HTE_NOM, ").append(selectAp1).append(" AS APELLIDO1, ")
                    .append(selectAp2).append(" AS APELLIDO2, ").append("T_HTE.HTE_DOC, ").append("DEST.UOR_NOM AS DESTINO, ORG_DES.UOREX_NOM AS DESTINO_EXT, ")
                    .append("R_RES.ASUNTO, rta.DESCRIPCION, ")
                    .append("ORG_ORI.UOREX_NOM AS ORIGEN_EXT, ").append("USU_NOM, ").append("RES_OBS ");
            query.append("FROM R_RES JOIN R_EXT ON (R_RES.RES_EJE=R_EXT.EXT_EJE AND R_RES.RES_NUM=R_EXT.EXT_NUM AND ")
                    .append("R_RES.RES_UOR=R_EXT.EXT_UOR AND R_RES.RES_TIP=R_EXT.EXT_TIP AND R_RES.RES_DEP=R_EXT.EXT_DEP) ")
                    .append("JOIN T_HTE ON (R_RES.RES_TER=T_HTE.HTE_TER AND R_RES.RES_TNV=T_HTE.HTE_NVR AND ")
                    .append("R_EXT.EXT_TER=T_HTE.HTE_TER AND R_EXT.EXT_NVR=T_HTE.HTE_NVR) ")
                    .append("LEFT JOIN A_UOR DEST ON (R_RES.RES_UOD=DEST.UOR_COD) LEFT JOIN ").append(GlobalNames.ESQUEMA_GENERICO).append("A_UOREX ORG_DES ")
                    .append("ON (R_RES.RES_OCD=ORG_DES.UOREX_ORG AND R_RES.RES_UCD=ORG_DES.UOREX_COD) ").append("LEFT JOIN ")
                    .append(GlobalNames.ESQUEMA_GENERICO).append("A_UOREX ORG_ORI ON (R_RES.RES_OCO=ORG_ORI.UOREX_ORG AND R_RES.RES_UCO=ORG_ORI.UOREX_COD) ")
                    .append(" LEFT JOIN R_TIPOASUNTO rta ON (R_RES.ASUNTO = rta.CODIGO AND (R_RES.RES_TIP = rta.TIPOREGISTRO OR rta.TIPOREGISTRO = 'A') AND R_RES.RES_UOR = rta.UNIDADREGISTRO)")
                    .append(" LEFT JOIN ").append(GlobalNames.ESQUEMA_GENERICO).append("A_USU ON (A_USU.USU_COD = R_RES.RES_USU) ");
             query.append("WHERE R_RES.RES_DEP=? AND R_RES.RES_UOR=? AND R_RES.RES_TIP=? AND R_RES.RES_EJE=? AND R_RES.RES_NUM=?");
                    
            m_Log.debug("sql = " + query);
            m_Log.debug("Parametros pasados a la query: " + depart + "-" + unidad + "-" + tipo+ "-" + ano + "-" + numero);
            
            ps = con.prepareStatement(query.toString());
            int contbd = 1;
            ps.setInt(contbd++, depart);
            ps.setInt(contbd++, unidad);
            ps.setString(contbd++, tipo);
            ps.setInt(contbd++, ano);
            ps.setLong(contbd++, numero);
            
            rs = ps.executeQuery();
            if(rs.next()){
                anotacion.setFecEntrada(rs.getString("FECHA"));
                anotacion.setFecHoraDoc(rs.getString("FECHADOC"));
				anotacion.setAsunto(AdaptadorSQLBD.js_escape(rs.getString("RES_ASU")));
                anotacion.setNombreInteresado(rs.getString("HTE_NOM"));
                anotacion.setApellido1Interesado(rs.getString("APELLIDO1"));
                anotacion.setApellido2Interesado(rs.getString("APELLIDO2"));
                String destinoOrg = rs.getString("DESTINO");
                String destinoOrgExt = rs.getString("DESTINO_EXT");
                String origenOrgExt = rs.getString("ORIGEN_EXT");
                String destino = "";
                if (destinoOrg != null) {
                    destino = destinoOrg;
                } else if (destinoOrgExt != null) {
                    destino = destinoOrgExt;
                } else if (origenOrgExt != null) {
                    destino = origenOrgExt;
                }
                anotacion.setNomUniRegDestino(destino);
                anotacion.setDocumentoInteresado(rs.getString("HTE_DOC"));
                String asunto=rs.getString("ASUNTO");
                if(asunto!=null){
                    anotacion.setCodAsunto(asunto);
                }else{
                    anotacion.setCodAsunto("");
                }
                String descripcionAsunto = rs.getString("DESCRIPCION");
                if(descripcionAsunto!=null){
                    anotacion.setDescripcionAsunto(descripcionAsunto);
                }else{
                    anotacion.setDescripcionAsunto("");
                }
                Vector<String> expedientesRelacionados = getExpedientesAsociadosARegistro(con, depart, unidad, tipo, ano, (int) numero);
                String numeroExp=null;
                String procedimiento=null;                   
                    
                try{
                    if(expedientesRelacionados!=null && expedientesRelacionados.size()==2){                            
                      numeroExp=(String) expedientesRelacionados.get(0);
                      procedimiento=(String) expedientesRelacionados.get(1);  
                    }

                }catch(Exception e){
                    m_Log.error("  =========> ERROR EXPEDIENTES RELACIONADOS:  " + e.getMessage());
                }

                String resEst = rs.getString("RES_EST");
                String estado;
                if (numeroExp != null && !"".equals(numeroExp) && !"null".equals(numeroExp)) {//SOLO ESTA ACEPTADA SIN PROCEDIMIENTOS ASOCIADOS
                    estado = "3";
                } else {
                    if("3".equals(resEst))
                        estado = "99";
                    else
                        estado = resEst;
                }
                anotacion.setEstAnotacion(Integer.parseInt(estado));
                anotacion.setNumExpediente(numeroExp);
                anotacion.setDocumentoInteresado(rs.getString("HTE_DOC"));
                anotacion.setUsuarioQRegistra(rs.getString("USU_NOM"));
                anotacion.setObservaciones(rs.getString("RES_OBS"));
            }
            
        } catch (Exception ex) {
            m_Log.error(ex.getMessage());
            ex.printStackTrace();
            throw new TechnicalException("Error: recuperar datos anotacion",ex);
        } finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (SQLException ex) {
                m_Log.error("Error al liberar recursos de BBDD");
            }
        }
        
        return anotacion; 
    }
    
    
     /**
     * Obtiene los datos de bloque Datos SGA
     * @param regVO
     * @param con Conexion de bbdd
     * @return Array con los datos SGA
     * @throws AnotacionRegistroException 
     */
            
     public String[] getDatosSga(RegistroValueObject regVO, Connection con)throws AnotacionRegistroException{
        m_Log.debug("-------------------------->getDatosSga");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String[] datosSga=new String[2];
        try{
            String sql="SELECT RES_SGA_COD, RES_SGA_EXP FROM R_RES WHERE RES_DEP="+regVO.getIdentDepart()+ " AND RES_UOR="+regVO.getUnidadOrgan()+" AND RES_TIP= '"+
                    regVO.getTipoReg()+"' AND RES_EJE="+regVO.getAnoReg()+" AND RES_NUM="+regVO.getNumReg();
            m_Log.debug(sql);
            
            ps = con.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                datosSga[0]=rs.getString("RES_SGA_COD");
                datosSga[1]=rs.getString("RES_SGA_EXP");
            }
        }catch(SQLException ex){
            m_Log.error("getDatosSga:"+ex.getMessage());
            ex.printStackTrace();
        }catch(Exception e){
            m_Log.error("getDatosSga:"+e.getMessage());
            e.printStackTrace();
        }finally{
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (SQLException ex) {
                m_Log.error("Error al liberar recursos de BBDD");
            }
        }
        return datosSga;
    } 
            
     /**
     * Elimina los metadatos de un determinado documento de registro.
     * @param doc Documento
     * @param con Conexi\F3n con la BD
     * @throws AnotacionRegistroException. En caso de que se produzca alg\FAn error.
     */
    public void eliminarMetadatosDocumentoRegistro(Documento doc, Connection con) throws AnotacionRegistroException {
        m_Log.debug("eliminarMetadatosDocumentoRegistro ===>");        
        PreparedStatement pst = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM METADATO_DOC_COTEJADOS ")
               .append(" WHERE DEPARTAMENTO = ? ")
               .append(" AND UOR = ? ")
               .append(" AND EJERCICIO = ? ")
               .append(" AND NUMERO = ? ")
               .append(" AND TIPO_REGISTRO = ? ")
               .append(" AND NOMBRE_DOC = ? ");
            
            m_Log.debug("Sentencia SQL DE BORRAR Metadatos de Docs :" + sql.toString());
            pst = con.prepareStatement(sql.toString());
            
            JdbcOperations.setValues(pst, 1, 
                    doc.getCodigoDepartamento(),
                    doc.getCodigoUnidadOrganica(),
                    doc.getEjercicioAnotacion(),
                    doc.getNumeroRegistro(),
                    doc.getTipoRegistro(),
                    doc.getNombreDocumento()
                    );
            
            int res = pst.executeUpdate();
            
            m_Log.debug("las filas afectadas en el delete son : " + res);
        } catch (SQLException ex) {
            m_Log.error("eliminarMetadatosDocumentoRegistro: " + ex.getMessage());  
            ex.printStackTrace();            
            throw new AnotacionRegistroException("Se ha producido un error al eliminar los metadatos de un documento", ex);
        } catch (Exception e) {
            m_Log.error("eliminarMetadatosDocumentoRegistro: " + e.getMessage());  
            e.printStackTrace();            
            throw new AnotacionRegistroException("Se ha producido un error al eliminar los metadatos de un documento", e);            
        } finally {
            try{
                SigpGeneralOperations.closeStatement(pst);
            }catch(TechnicalException e){
                m_Log.error("Error al cerrar recursos asociados a la conexion de base de datos: " + e.getMessage());
            }
        }
    }
    
    public RegistroValueObject obtenerOficinaRegistro(RegistroValueObject registro, Connection con) throws SQLException{
        ResultSet rs = null;
        PreparedStatement ps = null;
        String query;
        
        try {
            query = "SELECT RES_OFI,UOR_NOM FROM R_RES LEFT JOIN A_UOR ON RES_OFI=UOR_COD "
                    + "WHERE RES_UOR=? AND RES_TIP=? AND RES_EJE=? AND RES_NUM=?";
            m_Log.debug("sql="+query);
            m_Log.debug("parametros=["+registro.getUnidadOrgan()+","+registro.getTipoReg()+","+registro.getAnoReg()+","+registro.getNumReg()+"]");

            ps = con.prepareStatement(query);
            int cont = 1;
            ps.setInt(cont++, registro.getUnidadOrgan());
            ps.setString(cont++, registro.getTipoReg());
            ps.setInt(cont++, registro.getAnoReg());
            ps.setLong(cont++, registro.getNumReg());
            
            rs = ps.executeQuery();
            if(rs.next()){
                registro.setCodOficinaRegistro(rs.getInt("RES_OFI"));
                registro.setNombreOficinaRegistro(rs.getString("UOR_NOM"));
            }
        } catch (SQLException sqle){
            m_Log.error("Error al recuperar codigo  y  nombre de la oficina de registro");
            throw sqle;
        } finally  {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (SQLException sqle){
                m_Log.error("Error al liberar recursos de base de datos.");
            }
        }
        
        return registro;        
    }
    
    /**
     * 
     * @param id id del metadato a obtener
     * @param enumTabla la tabla a consultar
     * @param con Conexion
     * @return
     * @throws SQLException 
     */
    public List<KeyValueObject<Integer, String>> obtenerTiposMetadatosCotejo(Integer id, TablaMetadatos enumTabla, Connection con) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        StringBuilder query = new StringBuilder();
        List<KeyValueObject<Integer, String>> lista = new ArrayList<KeyValueObject<Integer, String>>();
        KeyValueObject<Integer, String> elemento = null;
        
        try {
            String tabla = null;
            
            switch (enumTabla) {
                case TIPO_DOCUMENTAL:
                    tabla = "METADATO_TIPO_DOCUMENTAL";
                    break;
                case ESTADO_ELABORACION:
                    tabla = "METADATO_ESTADO_ELABORACION";
                    break;
                case TIPO_FIRMA:
                    tabla = "METADATO_TIPO_FIRMA";
                    break;
            }
            
            // Construccion de consulta
            query.append("SELECT ID, CODIGO, DESCRIPCION ")
                 .append("FROM ")
                 .append(tabla);
                  
            if (id != null) {
                 query.append(" WHERE ID = ? ");
            }
            
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("sql=" + query.toString());
                m_Log.debug(String.format("parametros = [ id = %d]", id));
            }
            
            ps = con.prepareStatement(query.toString());

            // Variables bind
            int indexStart = 1;
            if (id != null) {
                JdbcOperations.setValues(ps, indexStart, id);
            }

            // Ejecutar consulta
            rs = ps.executeQuery();
            while (rs.next()) {
                elemento = new KeyValueObject<Integer, String>();
                elemento.setKey(rs.getInt("ID"));
                elemento.setValue(rs.getString("DESCRIPCION"));
                lista.add(elemento);
            }
        } catch (SQLException sqle) {
            m_Log.error("Error al recuperar la descripcion del tipo de firma");
            throw sqle;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                m_Log.error("Error al liberar recursos de base de datos.");
            }
        }

        return lista;
    }
    
    public HashMap consultaHashEtiquetasValor(GeneralValueObject gvo,String[] params) throws AnotacionRegistroException, TechnicalException{

     m_Log.info("AnotacionRegistroDAO.consultaHashEtiquetasValor");

     HashMap mapaEtiquetas = new HashMap();
     String tipoInforme = null;
     String raiz = null;
     
     if(gvo.getAtributo("tipoInforme")!=null){
         tipoInforme = (String)gvo.getAtributo("tipoInforme");
         raiz = obtenEstructuraRaiz((String)gvo.getAtributo("codAplicacion"),tipoInforme,params);
     } else {
         raiz = obtenEstructuraRaiz((String)gvo.getAtributo("codAplicacion"),params);
     }
         
        if(m_Log.isDebugEnabled()) m_Log.debug("--> RAIZ en obten estructura2 : " + raiz);
        EstructuraEntidades eeRaiz;
        String regNum=(String)gvo.getAtributo("ejercicio")+"/"+(String)gvo.getAtributo("numero");
        Vector<String> parametros = new Vector<String>();
        parametros.add((String)regNum);
        parametros.add((String)gvo.getAtributo("codOur"));
        parametros.add((String)gvo.getAtributo("codTip"));

        if(m_Log.isDebugEnabled()) m_Log.debug("regNum::"+regNum);
        if(m_Log.isDebugEnabled()) m_Log.debug("codOur::"+gvo.getAtributo("codOur"));
        if(m_Log.isDebugEnabled()) m_Log.debug("codTip::"+gvo.getAtributo("codTip"));
        
        try{
            eeRaiz = UtilidadesXerador.construyeEstructuraEntidadesInforme(params, "", raiz);
            eeRaiz.setValoresParametrosConsulta(parametros);
            // #239565: se indica en la estructura el tipo de informe que se solicita, solo para peticion o justificante, para otros será nulo
            eeRaiz.setDescTipoInforme(tipoInforme);
            // #267396: se indica en la estructura el formato de fecha
            eeRaiz.setFormatoFecha((String) gvo.getAtributo("formatoFecha"));
            
            UtilidadesXerador.construyeEstructuraDatos(params, eeRaiz, null);
            java.util.Collection col = eeRaiz.getListaInstancias();

            for (Object objNodo : col) {
                NodoEntidad n = (NodoEntidad) objNodo;

                GeneralValueObject temp = new GeneralValueObject();
                temp.setAtributo("codOur", (String)gvo.getAtributo("codOur"));
                temp.setAtributo("numero", (String)gvo.getAtributo("numero"));
                temp.setAtributo("ejercicio", (String)gvo.getAtributo("ejercicio"));
                temp.setAtributo("codTip", (String)gvo.getAtributo("codTip"));

                if (m_Log.isDebugEnabled()) m_Log.debug("_________RELACION________");
                if (m_Log.isDebugEnabled()) m_Log.debug("NUM::" + temp.getAtributo("numero"));
                if (m_Log.isDebugEnabled()) m_Log.debug("COD_TIP::" + temp.getAtributo("codTip"));
                if (m_Log.isDebugEnabled()) m_Log.debug("COD_our::" + temp.getAtributo("codOur"));
                if (m_Log.isDebugEnabled()) m_Log.debug("EJERCICIO ::" + temp.getAtributo("ejercicio"));
                if (m_Log.isDebugEnabled()) m_Log.debug("CARGAR RELACION EXPEDIENTES --> INI cargaListaExpedientes");
                
                mapaEtiquetas.putAll(n.getCampos());

            }//Fin del While
            

        } catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Exception capturada en: " + getClass().getName());
        }
        return mapaEtiquetas;
    }
	
    public ArrayList<RegistroValueObject> getListadoPendientesDigitalizar(RegistroValueObject regVO, String filtro, Connection con) throws AnotacionRegistroException {
        m_Log.debug("--> getListadoPendientesDigitalizar()");
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<RegistroValueObject> listadoPendientesFinalizar = new ArrayList();
        String sql;
        try {
            sql = "SELECT DISTINCT RES_NUM, RES_EJE, RES_FEC, RES_FED, UOR_NOM, RES_ASU, R_RES.ASUNTO,"
					+ " TER_NOM, TER_AP1, TER_AP2, "
					+ " (SELECT COUNT(*) FROM R_EXT"
					+ " WHERE RES_UOR = EXT_UOR"
					+ " AND RES_DEP = EXT_DEP"
					+ " AND RES_TIP = EXT_TIP"
					+ " AND RES_EJE = EXT_EJE"
					+ " AND RES_NUM = EXT_NUM) AS NUM_TERCEROS,"
					+ " USU_NOM"
					+ " FROM R_RES, A_UOR, T_TER, " + GlobalNames.ESQUEMA_GENERICO + "A_USU, R_EXT"
					+ " WHERE RES_TIP= 'E'"
					+ " AND RES_DEP = " + regVO.getUsuarioLogueado().getDepCod()
					+ " AND RES_UOR = " + regVO.getUsuarioLogueado().getUnidadOrgCod()
					+ " AND R_RES.FIN_DIGITALIZACION = 0"
					+ " AND R_RES.RES_EST = 0"
					+ " AND R_RES.RES_TER = T_TER.TER_COD "
					+ " AND R_RES.RES_USU = " + GlobalNames.ESQUEMA_GENERICO + "A_USU.USU_COD"
					+ " AND R_RES.RES_UOD = A_UOR.UOR_COD"
					+ " AND RES_NUM = EXT_NUM "
					+ " AND RES_EJE = EXT_EJE ";
			if ("1".equals(filtro)) {
				sql += " AND RES_USU = " + regVO.getUsuarioLogueado().getIdUsuario();
			} else if ("2".equals(filtro)) {
				sql += " and res_ofi in (select uor_cod from " + GlobalNames.ESQUEMA_GENERICO + "a_uou join a_uor on uor_cod=uou_uor where uou_usu=" + regVO.getUsuarioLogueado().getIdUsuario() + " and oficina_registro=1)";

			}
			sql += " ORDER BY RES_EJE DESC, RES_NUM DESC";
            m_Log.debug(sql);
            ps = con.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            Calendar fechaPresentacion = null;
            Calendar fechaGrabacion = null;

            while (rs.next()) {
                RegistroValueObject registroPendienteFinalizar = new RegistroValueObject();

                registroPendienteFinalizar.setNumReg(rs.getLong("RES_NUM"));
                registroPendienteFinalizar.setEjeOrigen(rs.getString("RES_EJE"));

                fechaPresentacion = DateOperations.toCalendar(rs.getTimestamp("RES_FEC"));
                registroPendienteFinalizar.setFecEntrada(DateOperations.toString(fechaPresentacion, "dd/MM/yyyy HH:mm:ss"));

                fechaGrabacion = DateOperations.toCalendar(rs.getTimestamp("RES_FED"));
                registroPendienteFinalizar.setFechaDocu(DateOperations.toString(fechaGrabacion, "dd/MM/yyyy HH:mm:ss"));
                registroPendienteFinalizar.setAsunto(rs.getString("RES_ASU"));  //extracto
                registroPendienteFinalizar.setCodAsunto(rs.getString("ASUNTO")); // código asunto

                if (rs.getInt("NUM_TERCEROS") > 1) {
                    registroPendienteFinalizar.setMasInteresados(true);
                } else {
                    registroPendienteFinalizar.setMasInteresados(false);
                }
                registroPendienteFinalizar.setNombreInteresado(rs.getString("TER_NOM"));
                registroPendienteFinalizar.setApellido1Interesado(rs.getString("TER_AP1"));
                registroPendienteFinalizar.setApellido2Interesado(rs.getString("TER_AP2"));
                registroPendienteFinalizar.setUorCodVisible(rs.getString("UOR_NOM"));
                registroPendienteFinalizar.setUsuarioQRegistra(rs.getString("USU_NOM"));

                listadoPendientesFinalizar.add(registroPendienteFinalizar);
            }

            // se obtiene la descripción del asunto de cada anotación
            for (RegistroValueObject regERVO : listadoPendientesFinalizar) {
                MantAsuntosValueObject asuntoVO = new MantAsuntosValueObject();
                Vector<MantAsuntosValueObject> listadoAsuntos = new Vector();
                String descAsunto = "";
                if (regERVO.getCodAsunto() != null) {
                    descAsunto = getDescAsunto(regERVO.getCodAsunto(), regVO.getUnidadOrgan(), con);
                }
                asuntoVO.setCodigo(regERVO.getCodAsunto());
                asuntoVO.setDescripcion(descAsunto);

                listadoAsuntos.add(asuntoVO);
                regERVO.setListaAsuntos(listadoAsuntos);
            }

        } catch (Exception e) {
            m_Log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(ps);

            } catch (TechnicalException ex) {
                m_Log.error("Error al liberar recursos de BBDD: " + ex.getMessage());
            }
        }
        return listadoPendientesFinalizar;
    }
    
    public ArrayList<RegistroValueObject> getListadoEntradasRechazadas(RegistroValueObject regVO, String[] params) throws AnotacionRegistroException{
        m_Log.debug("---> getListadoEntradasRechazadas()");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<RegistroValueObject> listadoEntradasRechazadas = new ArrayList();
        String select = null;
        String from = null;
        String where = null;
        String sql = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
			
            select = "SELECT R_RES.res_num, R_RES.res_eje, R_RES.res_fec, R_RES.res_fed, A_UOR.uor_nom ,R_RES.res_asu, R_RES.asunto,"
                + "T_TER.ter_nom, T_TER.ter_ap1, T_TER.ter_ap2," 
                + "(Count (R_EXT.EXT_TER) OVER (PARTITION BY R_EXT.ext_uor,R_EXT.ext_dep,R_EXT.ext_tip,R_EXT.ext_eje,R_EXT.ext_num ORDER BY R_EXT.ext_uor,R_EXT.ext_dep,R_EXT.ext_tip,R_EXT.ext_eje,R_EXT.ext_num,R_EXT.ext_ter,R_EXT.ext_nvr)) AS NUM_TERCEROS,"
		+ "A_USU.usu_nom, r_historico_prev.usu_nom  USU_RECHAZO";   
            
            from = " FROM R_RES "
                + "LEFT JOIN A_UOR ON R_RES.res_uod = A_UOR.uor_cod "
		+ "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_USU A_USU ON R_RES.res_usu = A_USU.usu_cod "
		+ "LEFT JOIN T_TER ON R_RES.res_ter = T_TER.ter_cod "
		+ "LEFT JOIN R_EXT ON R_RES.res_uor = R_EXT.ext_uor AND R_RES.res_dep = R_EXT.ext_dep AND R_RES.res_tip = R_EXT.ext_tip " + "AND R_RES.res_eje = R_EXT.ext_eje AND R_RES.res_num = R_EXT.ext_num "
		+ "LEFT JOIN (SELECT r_historico_prev.*,MAX(FECHA) OVER(PARTITION BY CODENTIDAD) MAX_FECHA,"
		+ "ROW_NUMBER() OVER(PARTITION BY CODENTIDAD order by CODENTIDAD,fecha desc) orden_fecha,A_USU.usu_nom "
		+ "FROM r_historico_prev "
                + "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_USU A_USU ON r_historico_prev.USUARIO = A_USU.usu_cod " 
		+ "WHERE r_historico_prev.tipomov = 6) r_historico_prev "
                + "ON r_historico_prev.CODENTIDAD=(R_RES.res_dep||'/'||R_RES.res_uor||'/'||R_RES.res_tip||'/'||R_RES.res_eje||'/'||R_RES.res_num) " + "AND r_historico_prev.fecha = r_historico_prev.MAX_FECHA";
		
                where = " WHERE R_RES.res_est = " + regVO.getEstAnotacion() + " AND R_RES.res_tip = '" + regVO.getTipoReg() 
                + "' AND R_RES.res_dep = " + regVO.getIdentDepart() + " AND R_RES.res_uor = " + regVO.getUnidadOrgan();
	  
//			select = "SELECT DISTINCT RES_NUM, RES_EJE, RES_FEC, RES_FED, UOR_NOM, RES_ASU, R_RES.ASUNTO, "
//                    + " TER_NOM, TER_AP1, TER_AP2,"
//                    + "(SELECT COUNT (*)" 
//                    + "     FROM   R_EXT " 
//                    + "     WHERE  RES_UOR = EXT_UOR " 
//                    + "     AND RES_DEP = EXT_DEP " 
//                    + "     AND RES_TIP = EXT_TIP " 
//                    + "     AND RES_EJE = EXT_EJE " 
//                    + "     AND RES_NUM = EXT_NUM)" 
//                    + " AS NUM_TERCEROS,   "
//                    + " USU_NOM,"
//                    + " (SELECT USU_NOM"
//                    + "     FROM "+GlobalNames.ESQUEMA_GENERICO+"A_USU, R_HISTORICO_PREV"
//                    + "     WHERE "+GlobalNames.ESQUEMA_GENERICO+"A_USU.USU_COD = R_HISTORICO_PREV.USUARIO"
//                    + "     AND SUBSTR(R_HISTORICO_PREV.CODENTIDAD,12,12)=R_RES.RES_NUM "
//                    + "     AND SUBSTR(R_HISTORICO_PREV.CODENTIDAD,7,4)= R_RES.RES_EJE"
//                    + "     AND SUBSTR(R_HISTORICO_PREV.CODENTIDAD,5,1)= R_RES.RES_TIP"
//                    + "     AND SUBSTR(R_HISTORICO_PREV.CODENTIDAD,1,1)= R_RES.RES_DEP"
//                    + "     AND SUBSTR(R_HISTORICO_PREV.CODENTIDAD,3,1)= R_RES.RES_UOR"
//                    + "     AND R_HISTORICO_PREV.FECHA = (SELECT MAX(R_HISTORICO_PREV.FECHA)"
//                    + "          FROM R_HISTORICO_PREV "
//                    + "          WHERE SUBSTR(R_HISTORICO_PREV.CODENTIDAD,12,12)=R_RES.RES_NUM"
//                    + "          AND SUBSTR(R_HISTORICO_PREV.CODENTIDAD,7,4)= R_RES.RES_EJE"
//                    + "          AND SUBSTR(R_HISTORICO_PREV.CODENTIDAD,5,1)= R_RES.RES_TIP"
//                    + "          AND SUBSTR(R_HISTORICO_PREV.CODENTIDAD,1,1)= R_RES.RES_DEP"
//                    + "          AND SUBSTR(R_HISTORICO_PREV.CODENTIDAD,3,1)= R_RES.RES_UOR))  AS USU_RECHAZO";
//            from = " FROM R_RES, A_UOR, T_TER, " +GlobalNames.ESQUEMA_GENERICO+"A_USU, R_EXT, R_HISTORICO_PREV ";
//            where = " WHERE RES_EST =" + regVO.getEstAnotacion() +" AND "
//                    + " RES_TIP ='" + regVO.getTipoReg() +"' AND "
//                    + " RES_DEP =" + regVO.getIdentDepart() +" AND "
//                    + " RES_UOR =" + regVO.getUnidadOrgan() +" AND "
//                    + " R_HISTORICO_PREV.TIPOMOV=6 AND "
//                    + " RES_EJE =  Substr(r_historico_prev.codentidad, 7, 4) AND"
//                    + " RES_NUM =  Substr(r_historico_prev.codentidad, 12, 12) AND";
            
            if(regVO.isMisRechazadas()){
                where += " AND RES_USU =" + regVO.getUsuarioLogueado().getIdUsuario()+"  ";
            } 
            if(regVO.getFechaDesde()!=null && !regVO.getFechaDesde().equals("")){
                where+= " AND R_HISTORICO_PREV.FECHA BETWEEN " + adapt.convertir("'" + regVO.getFechaDesde() + " 00:00:00'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY HH24:MI:SS") +
                        " AND " + adapt.convertir("'" + regVO.getFechaHasta() + " 23:59:59'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, "DD/MM/YYYY HH24:MI:SS ");
            } 
            if(noEsNulo(regVO.getCodAsunto())){
                where += " AND R_RES.ASUNTO = '"+regVO.getCodAsunto()+"' ";
            }
			// ELIMINAR TRAS EL CAMBIO
//            where +=" R_RES.RES_TER = T_TER.TER_COD AND "
//                    + " R_RES.RES_USU = " + GlobalNames.ESQUEMA_GENERICO + "A_USU.USU_COD AND "
//                    + " R_RES.RES_UOD = A_UOR.UOR_COD AND "
//                    + " RES_NUM = EXT_NUM AND "
//                    + " RES_EJE = EXT_EJE "
//                    + " ORDER BY RES_EJE DESC, RES_NUM DESC";
            where +=" ORDER BY RES_EJE DESC, RES_NUM DESC";
            
            sql = select + from + where;
            
            m_Log.debug(sql);
            ps = con.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            Calendar fechaPresentacion = null;
            Calendar fechaGrabacion = null;
            
            while(rs.next()){
                RegistroValueObject registroEntradaRechazada = new RegistroValueObject();
            
                registroEntradaRechazada.setNumReg(rs.getLong("RES_NUM"));
                registroEntradaRechazada.setEjeOrigen(rs.getString("RES_EJE"));
                
                fechaPresentacion = DateOperations.toCalendar(rs.getTimestamp("RES_FEC"));
                registroEntradaRechazada.setFecEntrada(DateOperations.toString(fechaPresentacion, "dd/MM/yyyy HH:mm:ss"));
                
                fechaGrabacion = DateOperations.toCalendar(rs.getTimestamp("RES_FED"));
                registroEntradaRechazada.setFechaDocu(DateOperations.toString(fechaGrabacion, "dd/MM/yyyy HH:mm:ss"));
                registroEntradaRechazada.setAsunto(rs.getString("RES_ASU"));  //extracto
                registroEntradaRechazada.setCodAsunto(rs.getString("ASUNTO")); // código asunto
               
                if(rs.getInt("NUM_TERCEROS")>1){
                    registroEntradaRechazada.setMasInteresados(true);
                } else {
                    registroEntradaRechazada.setMasInteresados(false);
                }
                registroEntradaRechazada.setNombreInteresado(rs.getString("TER_NOM"));
                registroEntradaRechazada.setApellido1Interesado(rs.getString("TER_AP1"));
                registroEntradaRechazada.setApellido2Interesado(rs.getString("TER_AP2"));
                registroEntradaRechazada.setUorCodVisible(rs.getString("UOR_NOM"));
                registroEntradaRechazada.setUsuarioQRegistra(rs.getString("USU_NOM"));
                registroEntradaRechazada.setUsuarioRechazo(rs.getString("USU_RECHAZO"));
                 
                
                listadoEntradasRechazadas.add(registroEntradaRechazada);
            }
            
            // se obtiene la descripción del asunto de cada anotación
            for(RegistroValueObject regERVO: listadoEntradasRechazadas){
                MantAsuntosValueObject asuntoVO = new MantAsuntosValueObject();
                Vector<MantAsuntosValueObject> listadoAsuntos = new Vector();
                String descAsunto="";
                if(regERVO.getCodAsunto()!=null){
                    descAsunto =getDescAsunto(regERVO.getCodAsunto(),regVO.getUnidadOrgan(),con);
                }
                asuntoVO.setCodigo(regERVO.getCodAsunto());
                asuntoVO.setDescripcion(descAsunto);
                
                listadoAsuntos.add(asuntoVO);
                regERVO.setListaAsuntos(listadoAsuntos);
            }
        }catch(Exception e){
            m_Log.error(e.getMessage());
            e.printStackTrace();
         }finally{
            try {
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(ps);
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (TechnicalException ex) {
                m_Log.error("Error al liberar recursos de BBDD: " + ex.getMessage());
            }
        }
        
        return listadoEntradasRechazadas;
        
    }
    
     public List<DocumentoMetadatosVO> getMetadatosDocumentoCatalogado(RegistroValueObject regDoc, Connection con) throws AnotacionRegistroException {
         m_Log.debug("getMetadatosDocumentoCatalogado ===>");   
        List<DocumentoMetadatosVO> listaMetadatosDocumento = new ArrayList<DocumentoMetadatosVO>();
        List<DocumentoCatalogacionVO> listaDocumentosCatalog = null;
        DocumentoCatalogacionVO docCatalog = null;
        DocumentoMetadatosVO docMetadato = null;
             
        try {
            docCatalog = DocumentoCatalogacionConversor.fromRegistroVO(regDoc);
            listaDocumentosCatalog = DigitalizacionDocumentosLanbideDAO.getInstance().recuperarDocCatalogacion(docCatalog, con);
            for(DocumentoCatalogacionVO documento : listaDocumentosCatalog){
                docMetadato = DocumentoCatalogacionConversor.toDocMetadatosVO(docCatalog);
                listaMetadatosDocumento.add(docMetadato);
            }
        } catch (SQLException ex) {
            m_Log.error("getMetadatosDocumentoCatalogado: " + ex.getMessage());  
            ex.printStackTrace();            
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getMetadatosDocumentoCatalogado.sql"), ex);
        } catch (Exception e) {
            m_Log.error("getMetadatosDocumentoCatalogado: " + e.getMessage());  
            e.printStackTrace();            
            throw new AnotacionRegistroException(m_ConfigError.getString("Error.AnotacionRegistroDAO.getMetadatosDocumentoCatalogado.sql"), e);            
        } 
        
        return  listaMetadatosDocumento;
    }
    
     public void insertMetadatosDocumentoCatalogado(DocumentoMetadatosVO metadatoDocumento, Connection con) throws TechnicalException{
         DocumentoCatalogacionVO docCatalog = null;
         try{
             docCatalog = DocumentoCatalogacionConversor.fromDocMetadatosVO(metadatoDocumento);
             DigitalizacionDocumentosLanbideDAO.getInstance().grabarMetadatoDocumento(docCatalog, con);
          } catch (SQLException ex) {
            ex.printStackTrace();
            m_Log.error("SQLException: " + ex.getMessage());
            throw new TechnicalException(ex.getMessage());
        } 
     }
     
     public String getCodProcedimientoRegistro(RegistroValueObject regVO, Connection con){
         String codProcedimiento = null;
         
         PreparedStatement ps = null;
         ResultSet rs = null;
         String query;
       
         try {
            query = "SELECT PROCEDIMIENTO FROM R_RES WHERE RES_DEP = 1 AND RES_UOR = " + regVO.getUnidadOrgan()+
                    " AND RES_TIP='"+ regVO.getTipoReg()+ "' AND RES_EJE ="+regVO.getAnoReg()+" AND RES_NUM ="+regVO.getNumReg();
            m_Log.debug("Query = " + query);
            
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            
             if (rs.next()) {
                codProcedimiento = rs.getString("PROCEDIMIENTO");
                if(codProcedimiento==null){
                    codProcedimiento="";
                }
            }
             
        } catch (SQLException sqle) {
            m_Log.error("Error al consultar el procedimiento de registro");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (SQLException ex){
                m_Log.error("Error al cerrar los recursos de la base de datos");
            }
            return codProcedimiento;
        }
     }
     
     public Boolean getFinDigitalizacionAnotacion(RegistroValueObject regVO, Connection con) throws SQLException{
         Boolean finDigitalizacion = null;
         
         PreparedStatement ps = null;
         ResultSet rs = null;
         String query;
       
         try {
            query = "SELECT FIN_DIGITALIZACION FROM R_RES WHERE RES_DEP = 1 AND RES_UOR = " + regVO.getUnidadOrgan()+
                    " AND RES_TIP='"+ regVO.getTipoReg()+ "' AND RES_EJE ="+regVO.getAnoReg()+" AND RES_NUM ="+regVO.getNumReg();
            m_Log.debug("Query = " + query);
            
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            
             if (rs.next()) {
                finDigitalizacion = BooleanUtils.toBoolean(
                        rs.getInt("FIN_DIGITALIZACION"), JdbcOperations.VALOR_TRUE_EN_CAMPOS_BBDD, JdbcOperations.VALOR_FALSE_EN_CAMPOS_BBDD);
            }
             
        } catch (SQLException sqle) {
            m_Log.error("Error al consultar el procedimiento de registro");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (SQLException ex){
                m_Log.error("Error al cerrar los recursos de la base de datos");
            }
        }
         
        return finDigitalizacion;
     }
     
     public String getDocumentoMigrado(Integer ejercicio, Integer numeroEntrada, String tituloDocumento, Connection con){  
            PreparedStatement ps = null;
         ResultSet rs = null;
         String query;
         String migrado="1";
     
         try {
             
            query = "SELECT  RED_MIGRA FROM R_RED WHERE RED_EJE=" + ejercicio +
                    " AND RED_NUM="+ numeroEntrada + " AND RED_NOM_DOC ='"+tituloDocumento+"'";
            m_Log.debug("Query = " + query);
            
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            
             if (rs.next()) {
                migrado = rs.getString("RED_MIGRA");
            }
                    
        } catch (SQLException sqle) {
            m_Log.error("Error recuperando si el documento es migrado o no");
            sqle.printStackTrace();
            
        } finally {
            try {
                if(ps!=null) ps.close();
            } catch (SQLException ex){
                m_Log.error("Error al cerrar los recursos de la base de datos");
            }
           
        }
         
         return migrado;
         
     }
     
     public void cancelarFinDigitalizacionDocumentos(RegistroValueObject regVO, Connection con){  
         PreparedStatement ps = null;
         int rs;
         String query;
     
         try {
             
            query = "UPDATE R_RES SET FIN_DIGITALIZACION = 0 WHERE  RES_DEP = 1 AND RES_UOR = " + regVO.getUnidadOrgan() +
                    " AND RES_TIP='"+ regVO.getTipoReg()+ "' AND RES_EJE ="+regVO.getAnoReg()+" AND RES_NUM ="+regVO.getNumReg();
            m_Log.debug("Query = " + query);
            
            ps = con.prepareStatement(query);
            rs = ps.executeUpdate();
                    
        } catch (SQLException sqle) {
            m_Log.error("Error al cancelar el fin de digitalización de documentos");
            sqle.printStackTrace();
            
        } finally {
            try {
                if(ps!=null) ps.close();
            } catch (SQLException ex){
                m_Log.error("Error al cerrar los recursos de la base de datos");
            }
           
        }
         
     }
     
     public void cancelarCambioProcedimiento(RegistroValueObject regVO,String codProAnterior, Connection con){  
         PreparedStatement ps = null;
         int rs;
         String query;
     
         try {
             
            query = "UPDATE R_RES SET PROCEDIMIENTO ='"+ codProAnterior +"' WHERE  RES_DEP = 1 AND RES_UOR = " + regVO.getUnidadOrgan() +
                    " AND RES_TIP='"+ regVO.getTipoReg()+ "' AND RES_EJE ="+regVO.getAnoReg()+" AND RES_NUM ="+regVO.getNumReg();
            m_Log.debug("Query = " + query);
            
            ps = con.prepareStatement(query);
            rs = ps.executeUpdate();
                    
        } catch (SQLException sqle) {
            m_Log.error("Error al cancelar el cambio de procedimiento de una anotación");
            sqle.printStackTrace();
            
        } finally {
            try {
                if(ps!=null) ps.close();
            } catch (SQLException ex){
                m_Log.error("Error al cerrar los recursos de la base de datos");
            }
           
        }
         
     }
	 
	 private void insertarAltaHistorico(RegistroValueObject registro, Connection con, String[] params) throws AnotacionRegistroException, TechnicalException {
		HistoricoMovimientoValueObject hvo = new HistoricoMovimientoValueObject();
		String tipoEntidad = ConstantesDatos.HIST_ENTIDAD_ANOTACION;
		int tipoMovimiento;
		String detallesMovimiento;
		
		DescripcionRegistroValueObject descripcionMovVO = AnotacionRegistroDAO.getInstance().crearVOParaHistorico(registro, true, registro.getCodProcedimiento(), con, params);
		if (registro.isFinDigitalizacion()) {
			tipoMovimiento = ConstantesDatos.HIST_ANOT_FINALIZAR;
			detallesMovimiento = HistoricoAnotacionHelper.crearXMLFinalizacionAlta(descripcionMovVO);
		} else {
			tipoMovimiento = ConstantesDatos.HIST_ANOT_ALTA;
			detallesMovimiento = HistoricoAnotacionHelper.crearXMLAlta(descripcionMovVO);	
		}		
		hvo.setCodigoUsuario(Integer.parseInt(registro.getUsuarioQRegistra()));
		hvo.setCodigoEntidad(HistoricoAnotacionHelper.crearClaveHistorico(registro));
		hvo.setTipoEntidad(tipoEntidad);
		hvo.setTipoMovimiento(tipoMovimiento);
		hvo.setDetallesMovimiento(detallesMovimiento);
		
		HistoricoMovimientoManager.getInstance().insertarMovimientoHistorico(hvo, con, params);
	}


}