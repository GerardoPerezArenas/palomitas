/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.eni.util;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.eni.persistence.GestionEniManager;
import es.altia.eni.conversoreni.domain.Documento;
import es.altia.eni.conversoreni.domain.Expediente;
import es.altia.eni.conversoreni.domain.MetadatosExpediente;
import es.altia.eni.conversoreni.domain.Organo;
import es.altia.eni.conversoreni.domain.TipoDocumental;
import es.altia.flexia.eni.exception.CodigoMensajeEni;
import es.altia.flexia.eni.exception.GestionEniException;
import es.altia.flexia.historico.expediente.vo.CronoVO;
import es.altia.flexia.historico.expediente.vo.ExpedienteVO;
import es.altia.util.jdbc.sqlbuilder.QueryResult;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Kevin
 */
public class GestionEniMapper {

	private final static Logger LOGGER = Logger.getLogger(GestionEniMapper.class);
	
	/**
	 * Metodo que enlaza cada Tipo Documental con su correspondiente tabla de
	 * base de datos y llama al metodo correspondiente para crear la sql y
	 * devolverla al metodo que la ejecute. En caso de que no este determinado
	 * se introducira en la tabla encargada de gestionar los otros documentos
	 * del expediente.
	 *
	 * @param documentoEni Documento Eni. Este recoge la informacion que tenemos
	 * que guardar en base de datos
	 * @param numExpediente
	 */
	public static void getQueryByTipoDocumental(Documento documentoEni, UsuarioValueObject usuario, String numExpediente, Connection con) throws GestionEniException {
		switch (documentoEni.getMetadatosEni().getTipoDocumental()) {
//			case COMUNICACION:
//				GestionEniManager.getInstance().queryInsertAdjuntoExtNotificacion(documentoEni, usuario, numExpediente, con);
//				break;
//			case DILIGENCIA:
//				GestionEniManager.getInstance().queryInsertDocumentoTramite(documentoEni, usuario, numExpediente, con);
//				break;
//			case FACTURA:
//				GestionEniManager.getInstance().queryInsertDocumentosSuplTramite(documentoEni, usuario, numExpediente, con);
//				break;
			case NOTIFICACION:
				GestionEniManager.getInstance().queryInsertNotificacion(documentoEni, usuario, numExpediente, con);
				break;
			case OTROS:
				GestionEniManager.getInstance().queryInsertDocumentoExterno(documentoEni, usuario, numExpediente, con);
				break;
//			case SOLICITUD:
//				GestionEniManager.getInstance().queryInsertDocumentosPresentados(documentoEni, usuario, numExpediente, con);
//				break;
//			case RECURSOS:
//				GestionEniManager.getInstance().queryInsertDocumentosSuplProcedimiento(documentoEni, usuario, numExpediente, con);
//				break;
			default:
				GestionEniManager.getInstance().queryInsertDocumentoExterno(documentoEni, usuario, numExpediente, con);
				break;
		}
	}
	
	/**
	 * Metodo que devuelve el tipo documental dependiendo de la tabla de base de
	 * datos de la que proceda el documento. //	* @param gestionEniTablasEnum
	 * Enumerado con el nombre de la tabla correspondiente
	 *
	 * @param gestionEniTablasEnum
	 * @return
	 */
	public static TipoDocumental getTipoDocumental(GestionEniEnumTablas gestionEniTablasEnum) {
		switch (gestionEniTablasEnum) {
//			case ADJUNTO_EXT_NOTIFICACION:
//				return TipoDocumental.COMUNICACION;
//			case E_CRD:
//				return TipoDocumental.DILIGENCIA;
			case E_DOC_EXT:
				return TipoDocumental.OTROS;
//			case E_DOCS_PRESENTADOS:
//				return TipoDocumental.SOLICITUD;
//			case E_TFI:
//				return TipoDocumental.RECURSOS;
//			case E_TFIT:
//				return TipoDocumental.FACTURA;
			case NOTIFICACION:
				return TipoDocumental.NOTIFICACION;
			default:
				return TipoDocumental.OTROS;
		}
	}
	
	//--MAPPER VO---------------------------------------------------------------
	/**
	 * Utiliza un {@link Expediente} para mapear los campos de un
	 * {@link ExpedienteVO} y devolverlo.
	 *
	 * @param expedienteEni
	 * @param numExpediente
	 * @param codUsuario
	 * @param codUorInicio
	 * @return
	 */
	public static ExpedienteVO mapearExpedienteVO(Expediente expedienteEni, String numExpediente, UsuarioValueObject usuario,
			int codUorInicio) {

		ExpedienteVO expediente = new ExpedienteVO();
		// Se coge el año actual
		int ejercicio = Calendar.getInstance().get(Calendar.YEAR);

		Calendar fechaExpediente = Calendar.getInstance(); // Por defecto, ahora
		if (expedienteEni.getMetadatosEni() != null) {
			MetadatosExpediente metadatos = expedienteEni.getMetadatosEni();
			fechaExpediente = metadatos.getFecha();
		}
		
		// Numero sacado en el momento de la insercion a traves de la tabla E_NEX, que genera numExp al insertar
		expediente.setNumExpediente(numExpediente);														//EXP_NUM  NOT NULL
		
		expediente.setAsunto(GestionEniConstantes.ASUNTO_ENI);											//EXP_ASU
		expediente.setCodProcedimiento(GestionEniConstantes.PROCEDIMIENTO_ENI);							//EXP_PRO  NOT NULL
//		expediente.setCodLocalizacion(null);															//EXP_CLO
		expediente.setCodOrganizacion(usuario.getOrgCod());												//EXP_MUN  NOT NULL
		expediente.setCodUsuario(usuario.getIdUsuario());												//EXP_USU
		expediente.setCodTramitePendiente(GestionEniConstantes.TRAMITE_INICIO_ENI_CODIGO);				//EXP_TRA
		expediente.setCodUorInicio(codUorInicio);														//EXP_UOR
		expediente.setFechaInicio(fechaExpediente);														//EXP_FEI  NOT NULL
		expediente.setFechaPendiente(Calendar.getInstance());											//EXP_PEND
//		expediente.setFechaFin(null);																	//EXP_FEF
		expediente.setEjercicio(ejercicio);																//EXP_EJE  NOT NULL
		expediente.setEstado(ConstantesDatos.ESTADO_EXPEDIENTE_PENDIENTE);								//EXP_EST  NOT NULL
		expediente.setImportante("N");																	//EXP_IMP 
//		expediente.setLocalizacion(null);																//EXP_LOC
		expediente.setOcurrenciaTramitePendiente(GestionEniConstantes.TRAMITE_INICIO_ENI_OCURRENCIA);	//EXP_TOCU
		expediente.setObservaciones("Expediente importacion ENI");										//EXP_OBS
//		expediente.setReferencia("");																	//EXP_REF
//		expediente.setUbicacionDocumentacion("0");														//EXP_UBICACION_DOC
//		expediente.setIdProceso(0);																		//HIST_E_EXP.ID_PROCESO
		
		return expediente;
	}
	
	public static ArrayList<CronoVO> mapearListaCroVO(ExpedienteVO expedienteVO) {
		ArrayList<CronoVO> listaCro = new ArrayList<CronoVO>();
		
		CronoVO eCro = new CronoVO();
			eCro.setAvisoFinPlazo(0);																	//CRO_AVISADOFDP
			eCro.setAvisoCercanaFinPlazo(0);															//CRO_AVISADOCFP
			eCro.setCodProcedimiento(expedienteVO.getCodProcedimiento());								//CRO_PRO NOT NULL
			eCro.setCodTramite(expedienteVO.getCodTramitePendiente());									//CRO_TRA NOT NULL
			eCro.setCodOrganizacion(expedienteVO.getCodOrganizacion());									//CRO_MUN NOT NULL
			eCro.setCodUsuario(expedienteVO.getCodUsuario());											//CRO_USU NOT NULL
			eCro.setCodUorTramitadora(expedienteVO.getCodUorInicio());									//CRO_UTR NOT NULL
//			eCro.setFechaFin(null);																		//CRO_FEF
//			eCro.setFechaFinPlazo(null);																//CRO_FFP
			eCro.setFechaInicio(expedienteVO.getFechaInicio());											//CRO_FEI NOT NULL
//			eCro.setFechaInicioPlazo(null);																//CRO_FIP
//			eCro.setFechaLimite(null);																	//CRO_FLI
			eCro.setEjercicio(expedienteVO.getEjercicio());												//CRO_EJE NOT NULL
//			eCro.setIdProceso(expedienteVO.getIdProceso());												//ID_PROCESO.
			eCro.setNumExpediente(expedienteVO.getNumExpediente());										//CRO_NUM NOT NULL
			eCro.setReserva(0);																			//CRO_RES
			eCro.setObservaciones(expedienteVO.getObservaciones());										//CRO_OBS
			eCro.setOcurrenciaTramite(expedienteVO.getOcurrenciaTramitePendiente());					//CRO_OCU NOT NULL
			eCro.setUsuarioFinalizacion(expedienteVO.getCodUsuario());									//CRO_USF
		listaCro.add(eCro);
		
		return listaCro;
	}
	
	//UNIDADES TRAMITADORAS-----------------------------------------------------
	/**
	 * Metodo para recuperar la lista del Unidades Organicas.
	 *
	 * @param numExpediente Numero del expediente
	 * @param params Parametros de conexion
	 * @return Devuelve la lista de organos
	 */
	public static List<Organo> recuperarUnidadesOrganicas(String numExpediente, String[] params) throws GestionEniException {
		List<Organo> organos = new ArrayList<Organo>();
		QueryResult result = GestionEniManager.getInstance().recuperarUnidadesOrganicasByExp(params, numExpediente);
		while (result.next()) {
			Organo organo = new Organo();
			organo.setCodigo(result.getString("UNIDAD_ORGANICA"));
			organos.add(organo);
		}
		if(organos == null || organos.isEmpty()){
			throw new GestionEniException(CodigoMensajeEni.ERROR_AL_RECUPERAR_UNIDADES_ORGANICAR.getCodigo(),
					"Error al recuperar la lista de Unidades Tramitadoras", numExpediente);
		}
		return organos;
	}
	
}
