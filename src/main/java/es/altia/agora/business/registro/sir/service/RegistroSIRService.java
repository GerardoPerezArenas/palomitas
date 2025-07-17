/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.exception.JustificanteRegistroException;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.registro.persistence.manual.AnotacionRegistroDAO;
import es.altia.agora.business.registro.sir.dao.SirDestinoRResDAO;
import es.altia.agora.business.registro.sir.dao.SirLocalidadesDAO;
import es.altia.agora.business.registro.sir.dao.SirOficinaDAO;
import es.altia.agora.business.registro.sir.dao.SirUnidadDIR3DAO;
import es.altia.agora.business.registro.sir.exception.ServicioSIRException;
import es.altia.agora.business.registro.sir.vo.*;
import es.altia.agora.business.sge.DocumentoAnotacionRegistroVO;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoDokusiImpl;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.interfaces.user.web.registro.informes.EjecutaJustificantePDFFactoria;
import es.altia.flexia.interfaces.user.web.registro.informes.IEjecutaJustificantePDF;
import es.altia.flexia.registro.digitalizacion.lanbide.persistence.MelanbideDokusiPlateaProService;
import es.altia.flexia.registro.justificante.vo.JustificanteRegistroPersonalizadoVO;
import es.altia.flexiaWS.documentos.bd.datos.AnotacionVO;
import es.altia.flexiaWS.documentos.bd.util.JusticanteRegistro;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.lanbide.lan6.adaptadoresPlatea.aiir.beans.*;
import es.lanbide.lan6.adaptadoresPlatea.aiir.servicios.Lan6AE20IntercambioRegistralServicios;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6SIRExcepcion;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6UtilExcepcion;
import es.lanbide.lan6.adaptadoresPlatea.sir.beans.*;
import es.lanbide.lan6.adaptadoresPlatea.sir.servicios.Lan6SIRServicios;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author INGDGC
 */
public class RegistroSIRService {
    
    private static final Logger log  = Logger.getLogger(RegistroSIRService.class);
    
    private  final AnotacionRegistroDAO anotacionRegistroDAO = AnotacionRegistroDAO.getInstance();

    private final AnotacionRegistroManager anotacionRegistroManager = AnotacionRegistroManager.getInstance();

    private  final SirUnidadDIR3DAO sirUnidadDIR3DAO = new SirUnidadDIR3DAO();
    private  final SirOficinaDAO sirOficinaDAO = new SirOficinaDAO();
    private  final SirLocalidadesDAO sirLocalidadesDAO = new SirLocalidadesDAO();
    private static final SIRDestinoRResService sIRDestinoRResService = new SIRDestinoRResService();
    private static final MelanbideDokusiPlateaProService melanbideDokusiPlateaProService = new MelanbideDokusiPlateaProService();
    RegistroSIRExceptionService registroSIRExceptionService = new RegistroSIRExceptionService();
    protected static Config registroConf = ConfigServiceHelper.getConfig("Registro");
    // Traductor Segun idioma de Usuario
    TraductorAplicacionBean traductorAplicacionBean = new TraductorAplicacionBean();

    private GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("dd/MM/yyyy").serializeNulls();
    Gson gson = gsonBuilder.create();

    private SimpleDateFormat formatDateddMMyyyyHHmmss = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            
    
    public String getDir3UnidadOrganicaOrigenEnvioSIR(String codVisibleUorFlexia, AdaptadorSQLBD adaptadorSQLBD) throws Exception {
        Connection con = null;
        try {
            con = adaptadorSQLBD.getConnection();
            SirUnidadDIR3 tempDir3 = sirUnidadDIR3DAO.getSirUnidadDIR3ByCodVisibleUorFlexia(codVisibleUorFlexia, con);
            return (tempDir3!=null?tempDir3.getCodigoUnidad():"");
        } catch (Exception e) {
            log.error("Error al recuperar codigo DIR3 Origen : " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptadorSQLBD.devolverConexion(con);
                log.error("getDir3UnidadOrganicaOrigenEnvioSIR Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(), e);
            }
        }
    }

    public List<SirUnidadDIR3Dto> getAllSirUnidadDIR3ByFilter(SirUnidadDIR3Filtros sirUnidadDIR3Filtros,AdaptadorSQLBD adapt) throws Exception {
        try {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(adapt.getDataSource(adapt.getParametros()[6]));
            return sirUnidadDIR3DAO.getAllSirUnidadDIR3ByFilter(sirUnidadDIR3Filtros,namedParameterJdbcTemplate);
        } catch (Exception e) {
            log.error("Error al recuperar unidad DIR3 : " +e.getMessage(), e);
            throw e;
        } finally {
            log.error("getUnidadesDir3ByCodigo Service - End ");
        }
    }
    
    public SirUnidadDIR3 getUnidadDir3ByCodigo(String CodigoDIR3,AdaptadorSQLBD adapt) throws Exception {
        SirUnidadDIR3 respuesta = null;
        Connection con = null;
        try {
            con = adapt.getConnection();
            respuesta = sirUnidadDIR3DAO.getSirUnidadDIR3ByCodigo(CodigoDIR3,con);
        } catch (Exception e) {
            log.error("Error al recuperar unidad DIR3 : " +e.getMessage(), e);
            throw e;
        } finally {
            try {
            adapt.devolverConexion(con);
            log.error("getUnidadesDir3ByCodigo Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(),e);
            }
        }
        return respuesta;
    }

    public List<Lan6DocumentoSalidaSIR> getListaOIDsDocumentosRegistro(Vector<RegistroValueObject> listaDocumentos, AdaptadorSQLBD adaptadorSQLBD) {
        List<Lan6DocumentoSalidaSIR> respuesta = new ArrayList<Lan6DocumentoSalidaSIR>();
        Connection con = null;
        try {
            con = adaptadorSQLBD.getConnection();
            if (listaDocumentos != null && !listaDocumentos.isEmpty()) {
                for (RegistroValueObject registroValueObject : listaDocumentos) {
                    DocumentoAnotacionRegistroVO documentoAnotacionRegistroVO =  new DocumentoAnotacionRegistroVO();
                    documentoAnotacionRegistroVO.setCodDepartamento(Integer.parseInt(registroValueObject.getDptoUsuarioQRegistra()));
                    documentoAnotacionRegistroVO.setCodigoUorRegistro(registroValueObject.getUnidadOrgan());
                    documentoAnotacionRegistroVO.setEjercicio(registroValueObject.getAnoReg());
                    documentoAnotacionRegistroVO.setNumeroAnotacion((int)registroValueObject.getNumReg());
                    documentoAnotacionRegistroVO.setTipoEntrada(registroValueObject.getTipoReg());
                    documentoAnotacionRegistroVO.setNombreDocumento(registroValueObject.getNombreDoc());
                    DocumentoAnotacionRegistroVO documento = anotacionRegistroDAO.getDocumentoAnotacionRegistro(documentoAnotacionRegistroVO, con);
                    if(documento!=null && documento.getIdDocGestor()!=null
                            && !documento.getIdDocGestor().isEmpty()){
                        Lan6DocumentoSalidaSIR docLan6SalidaSIRTemp = new Lan6DocumentoSalidaSIR();
                        //Limitamos el nombre a 27 (Maximo admitido es 32, pero en ellos concatenan la extension en el nombre)
                        docLan6SalidaSIRTemp.setNombreDocumentoSIR(quitarReservadosXMLandLimitarTamanoString(documento.getNombreDocumento(),27));
                        docLan6SalidaSIRTemp.setOidDocumentoSIR(documento.getIdDocGestor());
                        docLan6SalidaSIRTemp.setExtensionDocumentoSIR(MimeTypes.getExtension(documento.getTipoDocumento()));
                        respuesta.add(docLan6SalidaSIRTemp);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error al preparar los OID de los documentos " + ex.getMessage(),ex);
        }finally{
            try {
                if(con!=null) adaptadorSQLBD.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar la conexion .." + e.getMessage(), e);
            }   
        }
        return respuesta;
    }
    
    public int getTotalSirUnidadDIR3Registradas(AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return sirUnidadDIR3DAO.getTotalSirUnidadDIR3Registradas(con);
        } catch (Exception e) {
            log.error("Error al recuperar total unidad DIR3 sistema : " +e.getMessage(), e);
            throw e;
        } finally {
            try {
            adapt.devolverConexion(con);
            log.error("getTotalSirUnidadDIR3Registradas Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(),e);
            }
        }
    }

    public int calcularNumeroPaginasTotalesPaginador(int numResultadosTotal, int numResultadosPorPagina) {
        return (int)Math.ceil(new Double(numResultadosTotal)/
                    (numResultadosPorPagina > 0 ? new Double(numResultadosPorPagina) : new Double(1))
                );
    }

    public List<Lan6InteresadoSIR> getListaTercerosRegistro(Vector listaCodTercero, Vector listaVersionTercero, Vector listaCodDomicilio, String[] params) {
        List<Lan6InteresadoSIR> respuesta = new ArrayList<Lan6InteresadoSIR>();
        if(listaCodTercero!=null && !listaCodTercero.isEmpty()
                && listaVersionTercero!=null && !listaVersionTercero.isEmpty()
                && listaCodDomicilio!=null && !listaCodDomicilio.isEmpty()
                && (listaCodTercero.size()==listaVersionTercero.size() && listaCodTercero.size()==listaCodDomicilio.size())){
            for (int i = 0; i < listaCodTercero.size(); i++) {
                TercerosValueObject elTercero = new TercerosValueObject();
                int codInter = Integer.parseInt((String)listaCodTercero.get(i));
                int numModInfInt = Integer.parseInt((String)listaVersionTercero.get(i));
                int domicInter = Integer.parseInt((String)listaCodDomicilio.get(i));
                elTercero.setIdentificador(String.valueOf(codInter));
                elTercero.setVersion(String.valueOf(numModInfInt));
                elTercero.setIdDomicilio(String.valueOf(domicInter));
                TercerosValueObject terc = (TercerosValueObject) TercerosManager.getInstance().getByHistorico(elTercero, params).firstElement();
                if(terc!=null){
                    /**
                     * NOMBRE + " " + APELLIDO1 + " " + APELLIDO2 + "-" + NUM_IDENTIFICACION  -> No pueden exceder de 80
                     *
                     */
                    int limiteMaximo = 80;
                    int factorRevision = 0;
                    int tamanoNecesarioDocumento = (terc.getDocumento() != null && !terc.getDocumento().isEmpty() ? ("-" +  terc.getDocumento()).length() :"-".length());
                    String validacion1 = terc.getNombre()+ " " + terc.getApellido1()+ " " + terc.getApellido2() + "-" +  terc.getDocumento();
                    if(validacion1.length() > limiteMaximo){
                        factorRevision = validacion1.length() - limiteMaximo;
                        if(terc.getApellido2() != null) {
                            if((" " + terc.getApellido2()).length() >= factorRevision) {
                                terc.setApellido2(StringUtils.right(terc.getApellido2(), factorRevision));
                            }else{
                                if(terc.getApellido2() != null) {
                                    factorRevision = factorRevision - (" " + terc.getApellido2()).length();
                                    terc.setApellido2(null);
                                }
                                if(terc.getApellido1() != null) {
                                    if((" " + terc.getApellido1()).length() >= factorRevision) {
                                        terc.setApellido1(StringUtils.right(terc.getApellido1(), factorRevision));
                                        factorRevision=0;
                                    }else{
                                        factorRevision = factorRevision - (" " + terc.getApellido1()).length();
                                        terc.setApellido1("");
                                    }
                                }
                                if(factorRevision>0){
                                    if(terc.getNombre() != null) {
                                        if(terc.getNombre().length() >= factorRevision) {
                                            terc.setNombre(StringUtils.right(terc.getNombre(), factorRevision));
                                            factorRevision =0;
                                        }else{
                                            factorRevision = factorRevision - terc.getNombre().length();
                                            terc.setNombre("");
                                        }
                                    }
                                }
                            }
                        }

                    }
                    String validacion2 = terc.getNombreCompleto() + "-"+  terc.getDocumento();
                    if(validacion2.length() > limiteMaximo) {
                        factorRevision = limiteMaximo - tamanoNecesarioDocumento;
                        if (terc.getNombreCompleto() != null) {
                            if (terc.getNombreCompleto().length() >= factorRevision) {
                                terc.setNombreCompleto(StringUtils.right(terc.getNombreCompleto(), factorRevision));
                                factorRevision = 0;
                            } else {
                                factorRevision = factorRevision - terc.getNombreCompleto().length();
                                terc.setNombre("");
                            }
                        }
                    }
                    Lan6InteresadoSIR lan6InteresadoSIR = new Lan6InteresadoSIR(terc.getDocumento()
                            , terc.getTipoDocumento()
                            , quitarReservadosXMLandLimitarTamanoString(terc.getNombre(),limiteMaximo)
                            , quitarReservadosXMLandLimitarTamanoString(terc.getApellido1(),limiteMaximo)
                            , quitarReservadosXMLandLimitarTamanoString(terc.getApellido2(),limiteMaximo)
                            , quitarReservadosXMLandLimitarTamanoString(terc.getNombreCompleto(),limiteMaximo)
                            , quitarReservadosXMLandLimitarTamanoString(terc.getNombreCompleto(),limiteMaximo)
                            , terc.getTipoDocumento());
                    respuesta.add(lan6InteresadoSIR);
                }
            }
            
        }else{
            log.error("Una de la listas listaCodTercero, listaVersionTercero viene a null o no contienen los mismos elementos .."
                    + "listaCodTercero : " +(listaCodTercero!=null?listaCodTercero.size():"null")
                    + "listaVersionTercero : " +(listaVersionTercero!=null?listaVersionTercero.size():"null")
                    + "listaCodDomicilio : " +(listaCodDomicilio!=null?listaCodDomicilio.size():"null")
            );
        }
        return respuesta;
    }

    public SirRegistroSalidaResponse enviarSalidaSistemaSir(RegistroValueObject elRegistroESVO,int idiomaUsuario, String BDconnectionParams[],int registroRes_Dep, int registroRes_Uor, boolean alModificarSalida){
        SirRegistroSalidaResponse sirRegistroSalidaResponse = new SirRegistroSalidaResponse();
        try {
            if(elRegistroESVO!=null){
                log.info("Salida de registro " + elRegistroESVO.getAnoAnotacion() + "/" + elRegistroESVO.getNumReg() + " En regexlan, procedemos a darla de alta en el SIR."
                        + (alModificarSalida ? "Estamos Modificando Salida" : "Se ha dado de alta una nueva Salida"));
                log.info("Datos procesar: "
                        + elRegistroESVO.toString());
                // Recuperamos Informacion si es al modificar para validar si se puede modificar
                AdaptadorSQLBD adaptadorSQLBD = new AdaptadorSQLBD(BDconnectionParams);
                SirDestinoRRes sirDestinoRRes = new SirDestinoRRes();
                sirDestinoRRes.setRES_DEP(registroRes_Dep);
                sirDestinoRRes.setRES_UOR(registroRes_Uor);
                sirDestinoRRes.setRES_TIP(elRegistroESVO.getTipoReg());
                sirDestinoRRes.setRES_EJE(elRegistroESVO.getAnoReg());
                sirDestinoRRes.setRES_NUM((int) elRegistroESVO.getNumReg());
                sirDestinoRRes.setCodigoUnidad(elRegistroESVO.getCodigoUnidadDestinoSIR());
                SirDestinoRRes datosActuales = null;
                if(alModificarSalida){
                    datosActuales=sIRDestinoRResService.getSirDestinoRResByPKRRes(idiomaUsuario, sirDestinoRRes, adaptadorSQLBD);
                    if(datosActuales==null){
                        datosActuales = sIRDestinoRResService.crearDatosSirDestinoRRes(idiomaUsuario, sirDestinoRRes, adaptadorSQLBD);
                    }else{
                        if (sirDestinoRRes.getCodigoUnidad() != null && !sirDestinoRRes.getCodigoUnidad().isEmpty()) {
                            if (!sirDestinoRRes.getCodigoUnidad().equalsIgnoreCase(datosActuales.getCodigoUnidad())) {
                                // El dato recibido es diferente, actualizamos
                                log.info("Actualizamos UnidadDir3 destino Regexlan - Regexlan ---");
                                sirDestinoRRes = sIRDestinoRResService.actualizarCodigoUnidadSirDestinoRRes(idiomaUsuario, sirDestinoRRes, adaptadorSQLBD);
                            }else {
                                log.info("Datos UnidadDir3 destino Regexlan iguales, NO Actualizamos ---");
                            }
                        }
                    }                   
                }
                
                // Marcamos la salida como Gestionada desde SIR 
                elRegistroESVO.setRequiereGestionSIR(true);

                log.info("Recuperamos el codigo de procedimiento de platea para : " + elRegistroESVO.getCodProcedimiento());
                String codigoProcedimientoPlatea = melanbideDokusiPlateaProService.getCodigoProcedimientoPlatea(elRegistroESVO.getCodProcedimiento(), adaptadorSQLBD);
                elRegistroESVO.getCodProcedimiento();
                if (codigoProcedimientoPlatea != null && !codigoProcedimientoPlatea.isEmpty() && !codigoProcedimientoPlatea.equalsIgnoreCase("null")) {
                    String dir3Origen = getDir3UnidadOrganicaOrigenEnvioSIR(elRegistroESVO.getUorCodVisible(), adaptadorSQLBD); // O00017865 - Registro de Lanbide-Servicio Vasco de Empleo
                    String dir3Destino = elRegistroESVO.getCodigoUnidadDestinoSIR(); //registroSIRService.getDir3UnidadOrganicaDestinoEnvioSIR(elRegistroESVO.getIdDepDestino(),adaptadorSQLBD); // La seleccionada por el usuario 
                    Vector<RegistroValueObject> listaDocumentos = AnotacionRegistroManager.getInstance().getListaDocumentos(elRegistroESVO, BDconnectionParams);
                    List<Lan6DocumentoSalidaSIR> lan6DocumentoSalidaSIRList = getListaOIDsDocumentosRegistro(listaDocumentos, adaptadorSQLBD);
                    List<Lan6InteresadoSIR> lan6InteresadoSIRList = getListaTercerosRegistro(elRegistroESVO.getlistaCodTercero(), elRegistroESVO.getlistaVersionTercero(), elRegistroESVO.getlistaCodDomicilio(), BDconnectionParams);
                    // Montar llamada a Adaptdores Para crear el Registro de Salida
                    // Actualizar el Objeto Form para que se vea reflejado el dato de regsitro SIR en pantalla
                    Lan6SalidaRegistroSIR lan6SalidaRegistroSIR = new Lan6SalidaRegistroSIR();
                    lan6SalidaRegistroSIR.setAsuntoRegistro(quitarReservadosXMLandLimitarTamanoString(elRegistroESVO.getAsunto(),null));
                    //Lan6DatosModeloExtendido lan6DatosModeloExtendido = new Lan6DatosModeloExtendido();
                    //lan6DatosModeloExtendido.setIdCampo("");
                    //lan6DatosModeloExtendido.setValueCampo("");
                    //Lan6DatosModeloExtendido[] lan6DatosModeloExtendidoArreglo;
                    //lan6SalidaRegistroSIR.setDatosModeloExtendido(lan6DatosModeloExtendidoArreglo);
                    Lan6DestinoDIR3 lan6DestinoDIR3 = new Lan6DestinoDIR3();
                    SirUnidadDIR3 sirUnidadDIR3 = getUnidadDir3ByCodigo(dir3Destino, adaptadorSQLBD);
                    if (sirUnidadDIR3 != null) {
                        lan6DestinoDIR3.setUnidadOrganicaDestino(sirUnidadDIR3.getCodigoUnidad());
                        lan6DestinoDIR3.setCodigoOficina(sirUnidadDIR3.getCodigoOficina());
                    }
                    lan6SalidaRegistroSIR.setDestino(lan6DestinoDIR3);
                    if (lan6DocumentoSalidaSIRList != null && !lan6DocumentoSalidaSIRList.isEmpty()) {
                        Lan6DocumentoSalidaSIR[] tempArray = lan6DocumentoSalidaSIRList.toArray(new Lan6DocumentoSalidaSIR[lan6DocumentoSalidaSIRList.size()]);
                        lan6SalidaRegistroSIR.setDocumentos(tempArray);
                    }
                    lan6SalidaRegistroSIR.setIdProcedimiento(codigoProcedimientoPlatea);
                    lan6SalidaRegistroSIR.setIdioma((4 == idiomaUsuario ? "eu" : "es"));
                    if (lan6InteresadoSIRList != null && !lan6InteresadoSIRList.isEmpty()) {
                        Lan6InteresadoSIR[] tempArray = lan6InteresadoSIRList.toArray(new Lan6InteresadoSIR[lan6InteresadoSIRList.size()]);
                        lan6SalidaRegistroSIR.setInteresados(tempArray);
                    }
                    //lan6SalidaRegistroSIR.setNumeroTransporte((String.valueOf(elRegistroESVO.getIdTransporte())));
                    //lan6SalidaRegistroSIR.setTipoAsuntoRegistro(elRegistroESVO.getCodAsunto());
                    //lan6SalidaRegistroSIR.setUidTipoTransporte((String.valueOf(elRegistroESVO.getIdTransporte())));
                    // Deberia ser DIR3 En test en desarrollo se informa y falla, pero si se pasa el codigo de Unidad Administrativa funciona correctamente
                    String codigoUnidadAdministrativa = registroConf.getString("UNIDAD_ORIGEN_SIR_UNIDAD_ADMINISTRATIVA");
                    lan6SalidaRegistroSIR.setUnidadOrganicaOrigen(codigoUnidadAdministrativa); //dir3Origen
                    lan6SalidaRegistroSIR.setUnidadOrganicaTramitador(codigoUnidadAdministrativa);
                    Lan6SalidaRegistroSIRRespuesta respuestaAltaSalidaSIR = null;
                    try {
                        log.info("instanciamos: Lan6SIRServicios lan6SIRServicios = new Lan6SIRServicios("+codigoProcedimientoPlatea+");");
                        Lan6SIRServicios lan6SIRServicios = new Lan6SIRServicios(codigoProcedimientoPlatea);
                        log.info("lan6SalidaRegistroSIR ==> " + gson.toJson(lan6SalidaRegistroSIR));
                        respuestaAltaSalidaSIR = lan6SIRServicios.registrarSalidaSIR(lan6SalidaRegistroSIR);
                        if (respuestaAltaSalidaSIR != null) {
                            //Guardamos los datos de relacion envio SIR si ha sido correcto              
                            sirDestinoRRes.setOficinaRegistroSIR(respuestaAltaSalidaSIR.getOficinaRegistro());
                            sirDestinoRRes.setNumeroRegistroSIR(respuestaAltaSalidaSIR.getNumeroRegistro());
                            sirDestinoRRes.setUsuarioRegistroSIR(respuestaAltaSalidaSIR.getUsuarioRegistro());
                            sirDestinoRRes.setFechaRegistroSIR(respuestaAltaSalidaSIR.getFechaRegistro());
                            sirDestinoRRes.setFechaRegistroSistemaSIR(respuestaAltaSalidaSIR.getFechaRegistroSistema());
                            if (datosActuales != null) { //sIRDestinoRResService.existenDatosSirDestinoRResForPKRRes(sirDestinoRRes, adaptadorSQLBD)
                                log.info("Actualizamos --- Existen datos de Destino en SIR para la entrada. Actualizamos daatos de Registro en SIR. " + elRegistroESVO.getAnoReg() + "/" + elRegistroESVO.getNumReg());
                                sirDestinoRRes = sIRDestinoRResService.updateSirDestinoRRes(idiomaUsuario, sirDestinoRRes, adaptadorSQLBD);
                            } else {
                                log.info("Insertamos -- No Existen datos de Destino en SIR para la entrada " + elRegistroESVO.getAnoReg() + "/" + elRegistroESVO.getNumReg());
                                sirDestinoRRes = sIRDestinoRResService.crearDatosSirDestinoRRes(idiomaUsuario, sirDestinoRRes, adaptadorSQLBD);
                            }
                            log.info("Finalizamos proceso de creacion/Actualizacion SirDestinoRRes: " + (sirDestinoRRes != null ? sirDestinoRRes.toString() : "null"));
                            // Pasamos la info para mostrar al usuario
                            sirRegistroSalidaResponse.setCodigoIntRespuesta(0);
                            sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgAltaSalidaSIRCorrecto");
                            sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(MessageFormat.format(("" + traductorAplicacionBean.getDescripcion("msgAltaSalidaSIRCorrecto")), respuestaAltaSalidaSIR.getNumeroRegistro()));
                        } else {
                            log.error("Respuesta del servicio recibida a null, no se ha podido dar de alta la anotacion de salida en el SIR");
                            // Gestionar respuesta al usuario en el alta. 
                            //opcion = "error";
                            //elRegistroESVO.setRespOpcion("errorAltaSalidaSIR");
                            //elRegistroESVO.setRetramitarDocumentosDetalleOPeracion(traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIRNoResp"));
                            //registroForm.setRegistro(elRegistroESVO);
                            sirRegistroSalidaResponse.setCodigoIntRespuesta(3);
                            sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgErrorAltaSalidaSIRNoResp");
                            sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIRNoResp"));
                            //return mapping.findForward("error");
                        }
                    } catch (Lan6SIRExcepcion e) {
                        log.error("Lan6SIRExcepcion en la invocacion al servicio alta de Salida en SIR ", e);
                        //opcion = "error";
                        //elRegistroESVO.setRespOpcion("errorAltaSalidaSIR");
                        sirRegistroSalidaResponse.setCodigoIntRespuesta(-4);
                        sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgErrorAltaSalidaSIR");
                        sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(MessageFormat.format(
                                traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIR"),
                                registroSIRExceptionService.getTextoMensajeErrorFromLan6SirException(e)
                        ));
                        //registroForm.setRegistro(elRegistroESVO);
                        //return mapping.findForward("error");
                    } catch (Lan6UtilExcepcion e) {
                        log.error("Lan6UtilExcepcion en la invocacion al servicio alta de Salida en SIR ", e);
                        //opcion = "error";
                        //elRegistroESVO.setRespOpcion("errorAltaSalidaSIR");
                        sirRegistroSalidaResponse.setCodigoIntRespuesta(-3);
                        sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgErrorAltaSalidaSIR");
                        sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(MessageFormat.format(
                                traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIR"),
                                registroSIRExceptionService.getTextoMensajeErrorFromLan6UtilExcepcion(e)
                        ));
                        //registroForm.setRegistro(elRegistroESVO);
                        //return mapping.findForward("error");
                    } catch (Lan6Excepcion e) {
                        log.error("Lan6Excepcion en la invocacion al servicio alta de Salida en SIR ", e);
                        //opcion = "error";
                        //elRegistroESVO.setRespOpcion("errorAltaSalidaSIR");
                        sirRegistroSalidaResponse.setCodigoIntRespuesta(-2);
                        sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgErrorAltaSalidaSIR");
                        sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(MessageFormat.format(
                                traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIR"),
                                registroSIRExceptionService.getTextoMensajeErrorFromLan6Excepcion(e)
                        ));
                        //registroForm.setRegistro(elRegistroESVO);
                        //return mapping.findForward("error");
                    }
                } else {
                    log.error("No se ha recibido el codigo de procedimiento, no se puede crear la anotacion de salida registro en SIR ... ");
                    // Procesar respuesta para mostrar al usuario que no se puede dar de alta una salida en SIR sin procedimiento de platea
                    //opcion = "error";
                    //elRegistroESVO.setRespOpcion("errorAltaSalidaSIR");
                    //elRegistroESVO.setRetramitarDocumentosDetalleOPeracion(traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIRNoProc"));
                    //registroForm.setRegistro(elRegistroESVO);
                    sirRegistroSalidaResponse.setCodigoIntRespuesta(2);
                    sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgErrorAltaSalidaSIRNoProc");
                    sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIRNoProc"));
                    //return mapping.findForward("error");
                }
            }else{
                log.error("No se puede proecesar el Registro SIR. No se reciben datos correctos del registro o el usuario. "
                        + "elRegistroESVO="+(elRegistroESVO!=null)
                );
                sirRegistroSalidaResponse.setCodigoIntRespuesta(1);
                sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgErrorAltaSalidaSIR");
                sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(MessageFormat.format(
                        traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIR")
                        ,"Objeto elRegistroESVO con datos de registro recibido null, no hay informacion suficiente para procesar la solicitud")
                );
            }
        } catch (Exception e) {
            log.error("Error en el envio Salida al Sistema SIR Lanbide ... " + e.getMessage() + " - " + e.getLocalizedMessage() , e);
            sirRegistroSalidaResponse.setCodigoIntRespuesta(-1);
            sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgErrorAltaSalidaSIR");
            sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(MessageFormat.format(
                                traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIR"),
                                e.getMessage() + " - " + e.getLocalizedMessage())
                    );
        }
        return sirRegistroSalidaResponse;
    }
    
    public boolean isSalidaEnviadaSIR(RegistroValueObject elRegistroESVO,int registroRes_Dep, int registroRes_Uor, String BDconnectionParams[]){
        boolean respuesta = false;
        try {
            AdaptadorSQLBD adaptadorSQLBD = new AdaptadorSQLBD(BDconnectionParams);
            SirDestinoRRes sirDestinoRRes = new SirDestinoRRes();
            sirDestinoRRes.setRES_DEP(registroRes_Dep);
            sirDestinoRRes.setRES_UOR(registroRes_Uor);
            sirDestinoRRes.setRES_TIP(elRegistroESVO.getTipoReg());
            sirDestinoRRes.setRES_EJE(elRegistroESVO.getAnoReg());
            sirDestinoRRes.setRES_NUM((int) elRegistroESVO.getNumReg());
            sirDestinoRRes.setCodigoUnidad(elRegistroESVO.getCodigoUnidadDestinoSIR());
            SirDestinoRRes datosActuales = sIRDestinoRResService.getSirDestinoRResByPKRRes(1, sirDestinoRRes, adaptadorSQLBD);
            respuesta = (datosActuales!=null && datosActuales.getNumeroRegistroSIR()!=null && !datosActuales.getNumeroRegistroSIR().isEmpty());
        } catch (Exception e) {
            log.error("Error al validar si ya se ha enviado una Salida de Registro de Regexlan al SIR. " + e.getMessage(), e);
        }
        return respuesta;
    }
    
    public String quitarReservadosXMLandLimitarTamanoString(String textoValidar, Integer limiteTexto){
        String respuesta = "";
        log.info("quitarReservadosXMLandLimitarTamanoString : " + textoValidar + " ==> " + limiteTexto);
        try {
            if(textoValidar!=null && !textoValidar.isEmpty()){
                // Primero quitar caracteres Reservados XML
                /**
                 * 12/11/2024 => Caracteres indicados por EJIE invalidos: \\/?¿!¡*:|<>\;"&%#
                 */
                respuesta = textoValidar.replaceAll("&", "-")
                        .replaceAll("<", "-")
                        .replaceAll(">", "-")
                        .replaceAll("\"", "-")
                        .replaceAll("\\|", "-")
                        .replaceAll("\\\\", "-")
                        .replaceAll("/", "-")
                        .replaceAll("!", "-")
                        .replaceAll("\r\n", " _ ")
                        .replaceAll("\n", " _ ")
                        .replaceAll("\r", " _ ")
                        .replaceAll("\\?", "-")
                        .replaceAll("¿", "-")
                        .replaceAll("¡", "-")
                        .replaceAll("\\*", "-")
                        .replaceAll(":", "-")
                        .replaceAll(";", "-")
                        .replaceAll("%", "-")
                        .replaceAll("#", "-")
                        ;
                // Aplicamos el limite si este es >=0
                if (limiteTexto != null && limiteTexto >= 0) {
                    if (limiteTexto == 0) {
                        respuesta = "";
                    } else {
                        if (respuesta.length() > limiteTexto) {
                            respuesta = respuesta.substring(0, limiteTexto - 1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error al escapar caracteres para XML y al limitar tamano String. " + e.getMessage(), e);
        }
        log.info("quitarReservadosXMLandLimitarTamanoString ==> " + respuesta);
        return respuesta;
    }

    public SirRegistroSalidaResponse enviarEntradaSistemaSir(RegistroValueObject elRegistroESVO,int idiomaUsuario, String BDconnectionParams[],int registroRes_Dep, int registroRes_Uor, boolean alModificarEntrada, UsuarioValueObject usuarioVO){
        SirRegistroSalidaResponse sirRegistroSalidaResponse = new SirRegistroSalidaResponse();
        try {
            if(elRegistroESVO!=null){
                log.info("Entrada de registro " + elRegistroESVO.getAnoAnotacion() + "/" + elRegistroESVO.getNumReg() + " En regexlan, procedemos a darla de alta en el SIR."
                        + (alModificarEntrada ? "Estamos Modificando Entrada" : "Se ha dado de alta una nueva Salida"));
                log.info("Datos procesar: "
                        + elRegistroESVO.toString());
                // Recuperamos Informacion si es al modificar para validar si se puede modificar
                AdaptadorSQLBD adaptadorSQLBD = new AdaptadorSQLBD(BDconnectionParams);
                SirDestinoRRes sirDestinoRRes = new SirDestinoRRes();
                sirDestinoRRes.setRES_DEP(registroRes_Dep);
                sirDestinoRRes.setRES_UOR(registroRes_Uor);
                sirDestinoRRes.setRES_TIP(elRegistroESVO.getTipoReg());
                sirDestinoRRes.setRES_EJE(elRegistroESVO.getAnoReg());
                sirDestinoRRes.setRES_NUM((int) elRegistroESVO.getNumReg());
                sirDestinoRRes.setCodigoUnidad(elRegistroESVO.getCodigoUnidadDestinoSIR());
                SirDestinoRRes datosActuales = null;
                if(alModificarEntrada){
                    datosActuales=sIRDestinoRResService.getSirDestinoRResByPKRRes(idiomaUsuario, sirDestinoRRes, adaptadorSQLBD);
                    if(datosActuales==null){
                        datosActuales = sIRDestinoRResService.crearDatosSirDestinoRRes(idiomaUsuario, sirDestinoRRes, adaptadorSQLBD);
                    }else{
                        if (sirDestinoRRes.getCodigoUnidad() != null && !sirDestinoRRes.getCodigoUnidad().isEmpty()) {
                            if (!sirDestinoRRes.getCodigoUnidad().equalsIgnoreCase(datosActuales.getCodigoUnidad())) {
                                // El dato recibido es diferente, actualizamos
                                log.info("Actualizamos UnidadDir3 destino Regexlan - Regexlan ---");
                                sirDestinoRRes = sIRDestinoRResService.actualizarCodigoUnidadSirDestinoRRes(idiomaUsuario, sirDestinoRRes, adaptadorSQLBD);
                            }else {
                                log.info("Datos UnidadDir3 destino Regexlan iguales, NO Actualizamos ---");
                            }
                        }
                    }
                }

                // Marcamos la entrada como Gestionada desde SIR
                elRegistroESVO.setRequiereGestionSIR(true);

                log.info("Recuperamos el codigo de procedimiento de platea para : " + elRegistroESVO.getCodProcedimiento());
                String codigoProcedimientoPlatea = melanbideDokusiPlateaProService.getCodigoProcedimientoPlatea(elRegistroESVO.getCodProcedimiento(), adaptadorSQLBD);
                if (codigoProcedimientoPlatea != null && !codigoProcedimientoPlatea.isEmpty() && !codigoProcedimientoPlatea.equalsIgnoreCase("null")) {
                    //Hay que rellenarlo en Interfaz o leer de properties y luego de BD .... ***** Pendiente Revisar ***
                    String dir3Origen = getDir3UnidadOrganicaOrigenEnvioSIR(elRegistroESVO.getUorCodVisible(), adaptadorSQLBD); // O00017865 - Registro de Lanbide-Servicio Vasco de Empleo

                    String dir3Destino = elRegistroESVO.getCodigoUnidadDestinoSIR(); //registroSIRService.getDir3UnidadOrganicaDestinoEnvioSIR(elRegistroESVO.getIdDepDestino(),adaptadorSQLBD); // La seleccionada por el usuario
                    Vector<RegistroValueObject> listaDocumentos = AnotacionRegistroManager.getInstance().getListaDocumentos(elRegistroESVO, BDconnectionParams);
                    List<Lan6Anexo> lan6DocumentoEntradaSIRList = getListaAnexosEntradasRegEnvioSIR(listaDocumentos, adaptadorSQLBD, usuarioVO);
                    List<Lan6InteresadoAsientoRegistral> lan6InteresadoAsientoRegistralList = getListaLan6InteresadoAsientoRegistral(elRegistroESVO.getlistaCodTercero(), elRegistroESVO.getlistaVersionTercero(), elRegistroESVO.getlistaCodDomicilio(), BDconnectionParams);
                    // Montar llamada a Adaptadores Para crear el Registro de Entrada
                    // Actualizar el Objeto Form para que se vea reflejado el dato de regsitro SIR en pantalla
                    // Test Llamada Ibermatica
                    Lan6AsientoRegistral lan6AsientoRegistral = new Lan6AsientoRegistral();
                    SirUnidadDIR3 sirUnidadDIR3Origen = getUnidadDir3ByCodigo(dir3Origen, adaptadorSQLBD);
                    if (sirUnidadDIR3Origen != null) {
                        lan6AsientoRegistral.setCodigoEntidadRegistral(sirUnidadDIR3Origen.getCodigoOficina()); // "O00045691"
                        lan6AsientoRegistral.setCodigoEntidadRegistralInicio(sirUnidadDIR3Origen.getCodigoOficina()); // "O00045691"
                        lan6AsientoRegistral.setCodigoEntidadRegistralOrigen(sirUnidadDIR3Origen.getCodigoOficina()); // "O00045691"
                        lan6AsientoRegistral.setCodigoUnidadTramitacionOrigen(sirUnidadDIR3Origen.getCodigoUnidad());  //CodigoOficina "A16059264"
                        lan6AsientoRegistral.setDescripcionUnidadTramitacionOrigen(idiomaUsuario==4?sirUnidadDIR3Origen.getNombreUnidad_EU():sirUnidadDIR3Origen.getNombreUnidad_ES());   // "Lanbide-Servicio Vasco de Empleo"

                        SirOficina sirOficina = getSirOficinaByCodigo(sirUnidadDIR3Origen.getCodigoOficina(),adaptadorSQLBD);
                        if(sirOficina!=null) {
                            lan6AsientoRegistral.setDescripcionEntidadRegistralOrigen(idiomaUsuario==4?sirOficina.getNombre_EU():sirOficina.getNombre_ES());    //"Registro de Lanbide-Servicio Vasco de Empleo"
                            lan6AsientoRegistral.setDescripcionEntidadRegistralInicio(idiomaUsuario==4?sirOficina.getNombre_EU():sirOficina.getNombre_ES());    //"Registro de Lanbide-Servicio Vasco de Empleo"
                            //lan6AsientoRegistral.setDescripcionUnidadTramitacionOrigen(idiomaUsuario==4?sirOficina.getNombre_EU():sirOficina.getNombre_ES());   // "Lanbide-Servicio Vasco de Empleo"
                        }
                    }

                    SirUnidadDIR3 sirUnidadDIR3Destino = getUnidadDir3ByCodigo(dir3Destino, adaptadorSQLBD);
                    if (sirUnidadDIR3Destino != null) {
                        lan6AsientoRegistral.setCodigoEntidadRegistralDestino(sirUnidadDIR3Destino.getCodigoOficina()); //"O00043591"
                        SirOficina sirOficina = getSirOficinaByCodigo(sirUnidadDIR3Destino.getCodigoOficina(),adaptadorSQLBD);
                        if(sirOficina!=null)
                            lan6AsientoRegistral.setDescripcionEntidadRegistralDestino(idiomaUsuario==4?sirOficina.getNombre_EU():sirOficina.getNombre_ES()); //"Registro General del Ayuntamiento de Catarroja"
                        lan6AsientoRegistral.setCodigoUnidadTramitacionDestino(sirUnidadDIR3Destino.getCodigoUnidad()); // "LA1000323"
                        lan6AsientoRegistral.setDescripcionUnidadTramitacionDestino(idiomaUsuario == 4 ? sirUnidadDIR3Destino.getNombreUnidad_EU() : sirUnidadDIR3Destino.getNombreUnidad_ES()); // "Ayuntamiento de Catarroja"

                    }

                    lan6AsientoRegistral.setContactoUsuario(elRegistroESVO.getTlfInteresado()); //"Telefono:  Email:"
                    lan6AsientoRegistral.setDocumentacionFisica(Lan6Constantes.IR.IR_DOC_FISICA.IR_DOC_FISICA_NO);

                    lan6AsientoRegistral.setNumeroRegistro(elRegistroESVO.getAnoReg()+"/"+elRegistroESVO.getNumReg());
                    if(elRegistroESVO.getFecEntrada()!= null &&! elRegistroESVO.getFecEntrada().isEmpty()){
                        try {
                            log.info("elRegistroESVO.getFecEntrada() = " + elRegistroESVO.getFecEntrada());
                            if(elRegistroESVO.getFecEntrada().length()<19) // agregamos los segundos
                                elRegistroESVO.setFecEntrada(elRegistroESVO.getFecEntrada()+":00");
                            lan6AsientoRegistral.setFechaRegistro(formatDateddMMyyyyHHmmss.parse(elRegistroESVO.getFecEntrada()));
                        }catch (Exception e){
                            log.error("Error al hacer el set de la fecha de entrada de registro "+ lan6AsientoRegistral.getNumeroRegistro() + " : " + elRegistroESVO.getFecEntrada(), e);
                        }
                    }else{
                        lan6AsientoRegistral.setFechaRegistro(new Date());
                    }
                    lan6AsientoRegistral.setResumen(quitarReservadosXMLandLimitarTamanoString(elRegistroESVO.getAsunto(),null));
                    lan6AsientoRegistral.setTipoRegistro(Lan6Constantes.IR.IR_TIPO_REGISTRO.IR_TIPO_REG_ENTRADA);

                    // Generar el justificante de registro

                    try {
                        // Se recupera el justificante de registro personalizado que esta activo, si lo hay
                        //JustificanteRegistroPersonalizadoVO justificanteActivo = JusticanteRegistro.getPlantillaActiva(BDconnectionParams);
                        JustificanteRegistroPersonalizadoVO justificanteActivo = JusticanteRegistro.getJustificantebyName("justificanteRegistroSIR",BDconnectionParams);
                        AnotacionVO datosAnotacion = new AnotacionVO();
                        datosAnotacion.setDepartamento(sirDestinoRRes.getRES_DEP());
                        datosAnotacion.setUnidadRegistro(sirDestinoRRes.getRES_UOR());
                        datosAnotacion.setTipo(sirDestinoRRes.getRES_TIP());
                        datosAnotacion.setEjercicio(sirDestinoRRes.getRES_EJE());
                        datosAnotacion.setNumero((long) sirDestinoRRes.getRES_NUM());
                        if(justificanteActivo != null){
                            String directorio = JusticanteRegistro.getRutaPlantillas(elRegistroESVO.getIdOrganizacion());
                            //String oficinaRegistro = JusticanteRegistro.getOficinaRegistro(datosAnotacion, BDconnectionParams);

                            //Generamos el xml con los datos que tendra el justificante
                            //String formatoFecha = JusticanteRegistro.getFormatoFecha(elRegistroESVO.getIdOrganizacion());
                            //String xml = JusticanteRegistro.generarXml(datosAnotacion, String.valueOf(elRegistroESVO.getIdOrganizacion()), formatoFecha, BDconnectionParams);

                            Map<String,Object> parametrosInforme = prepararParametrosJustificanteRegistro_SIR(elRegistroESVO,lan6InteresadoAsientoRegistralList,lan6DocumentoEntradaSIRList,idiomaUsuario,adaptadorSQLBD);

                            //Obenemos el array de bytes correspondiente al justificante
                            IEjecutaJustificantePDF pdf = EjecutaJustificantePDFFactoria.getInstance().getImplClass(elRegistroESVO.getIdOrganizacion());

                            try {
                                //byte[] informe = pdf.generaJustificantePDF(directorio, justificanteActivo.getNombreJustificante(), oficinaRegistro, xml, true);
                                byte[] informe = pdf.generaJustificantePDF_SIR(directorio, justificanteActivo.getNombreJustificante(),parametrosInforme);
                                if(informe != null){

                                    // Test Creamos en disco para verificar el formato
                                    FileUtils.writeByteArrayToFile(new File(directorio+"/testSir/JustificanteIntercambioRegistral_"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+".pdf"),informe);

                                    Lan6Anexo lan6Anexo2 = new Lan6Anexo();
                                    lan6Anexo2.setItJustificante(Lan6Constantes.IR.IR_JUSTIFICANTE.IR_JUSTIF_SI);
                                    Lan6Documento lan6Documento2 = new Lan6Documento();
                                    lan6Documento2.setNombre(justificanteActivo.getNombreJustificante()+"_"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));  //"JustificanteIntercambioRegistral"
                                    lan6Documento2.setTipoDocumental("ejgv_d_justificante");
                                    lan6Documento2.setOrigen(Lan6Constantes.MD_ORIGEN_ADMINISTRACION);
                                    lan6Documento2.setFormat(Lan6Constantes.FORMATO_DOCUMENTO_PDF);
                                    lan6Documento2.setMimeType("application/pdf");
                                    lan6Documento2.setTitulo(justificanteActivo.getDescripcionJustificante()); //"Titulo JustificanteIntercambioRegistral"
                                    lan6Documento2.setNaturaleza(Lan6Constantes.MD_NATURALEZA_ELECTRONICO);
                                    lan6Documento2.setContenido(informe);
                                    lan6Anexo2.setLan6Documento(lan6Documento2);
                                    if (lan6DocumentoEntradaSIRList==null)
                                        lan6DocumentoEntradaSIRList=new ArrayList<Lan6Anexo>();
                                    lan6DocumentoEntradaSIRList.add(lan6Anexo2);
                                } else {
                                    throw new JustificanteRegistroException("Ha ocurrido un error al obtener el byte[] del justificante de registro");
                                }
                            } catch (JustificanteRegistroException e) {
                                throw e;
                            } catch (Exception e) {
                                throw new JustificanteRegistroException("Ha ocurrido un error al obtener el byte[] del justificante de registro");
                            }
                        }
                    }catch (Exception ex){
                        log.error("Error generando el justificante de registro ", ex);
                    }


                    if (lan6DocumentoEntradaSIRList!= null && !lan6DocumentoEntradaSIRList.isEmpty()){
                        lan6AsientoRegistral.setListaAnexos(lan6DocumentoEntradaSIRList);
                    }
                    if (lan6InteresadoAsientoRegistralList!= null && !lan6InteresadoAsientoRegistralList.isEmpty()){
                        lan6AsientoRegistral.setListaInteresados(lan6InteresadoAsientoRegistralList);
                    }

                    try {
                        Lan6AsientoRegistralRespuesta lan6AsientoRegistralRespuesta;
                        log.info("instanciamos: Lan6AE20IntercambioRegistralServicios servicios = new Lan6AE20IntercambioRegistralServicios("+codigoProcedimientoPlatea+");");
                        Lan6AE20IntercambioRegistralServicios lan6AE20IntercambioRegistralServicios = new Lan6AE20IntercambioRegistralServicios(codigoProcedimientoPlatea);
                        log.info("lan6AsientoRegistral ==> " + gson.toJson(lan6AsientoRegistral));
                        lan6AsientoRegistralRespuesta  = lan6AE20IntercambioRegistralServicios.enviarAsientoRegistral(lan6AsientoRegistral);
                        if (lan6AsientoRegistralRespuesta  != null) {
                            //Guardamos los datos de relacion envio SIR si ha sido correcto
                            sirDestinoRRes.setOficinaRegistroSIR(lan6AsientoRegistralRespuesta.getCodigoUnidadTramitacionDestino());
                            sirDestinoRRes.setNumeroRegistroSIR(lan6AsientoRegistralRespuesta.getIdentificadorIntercambio());
                            sirDestinoRRes.setUsuarioRegistroSIR(lan6AsientoRegistralRespuesta.getNombreUsuario());
                            sirDestinoRRes.setFechaRegistroSIR(lan6AsientoRegistralRespuesta.getFechaRegistro());
                            sirDestinoRRes.setFechaRegistroSistemaSIR(lan6AsientoRegistralRespuesta.getFechaRegistroInicial());
                            if (datosActuales != null) {
                                log.info("Actualizamos --- Existen datos de Destino en SIR para la entrada. Actualizamos daatos de Registro en SIR. " + elRegistroESVO.getAnoReg() + "/" + elRegistroESVO.getNumReg());
                                sirDestinoRRes = sIRDestinoRResService.updateSirDestinoRRes(idiomaUsuario, sirDestinoRRes, adaptadorSQLBD);
                            } else {
                                log.info("Insertamos -- No Existen datos de Destino en SIR para la entrada " + elRegistroESVO.getAnoReg() + "/" + elRegistroESVO.getNumReg());
                                sirDestinoRRes = sIRDestinoRResService.crearDatosSirDestinoRRes(idiomaUsuario, sirDestinoRRes, adaptadorSQLBD);
                            }
                            log.info("Finalizamos proceso de creacion/Actualizacion SirDestinoRRes: " + (sirDestinoRRes != null ? sirDestinoRRes.toString() : "null"));
                            // Pasamos la info para mostrar al usuario
                            sirRegistroSalidaResponse.setCodigoIntRespuesta(0);
                            sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgAltaSalidaSIRCorrecto");
                            sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(MessageFormat.format(("" + traductorAplicacionBean.getDescripcion("msgAltaSalidaSIRCorrecto")), lan6AsientoRegistralRespuesta.getIdentificadorIntercambio()));
                        } else {
                            log.error("Respuesta del servicio recibida a null, no se ha podido dar de alta la anotacion de salida en el SIR");
                            // Gestionar respuesta al usuario en el alta.
                            //opcion = "error";
                            //elRegistroESVO.setRespOpcion("errorAltaSalidaSIR");
                            //elRegistroESVO.setRetramitarDocumentosDetalleOPeracion(traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIRNoResp"));
                            //registroForm.setRegistro(elRegistroESVO);
                            sirRegistroSalidaResponse.setCodigoIntRespuesta(3);
                            sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgErrorAltaSalidaSIRNoResp");
                            sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIRNoResp"));
                            //return mapping.findForward("error");
                        }
                    } catch (Lan6SIRExcepcion e) {
                        log.error("Lan6SIRExcepcion en la invocacion al servicio alta de Salida en SIR ", e);
                        //opcion = "error";
                        //elRegistroESVO.setRespOpcion("errorAltaSalidaSIR");
                        sirRegistroSalidaResponse.setCodigoIntRespuesta(-4);
                        sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgErrorAltaSalidaSIR");
                        sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(MessageFormat.format(
                                traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIR"),
                                registroSIRExceptionService.getTextoMensajeErrorFromLan6SirException(e)
                        ));
                        //registroForm.setRegistro(elRegistroESVO);
                        //return mapping.findForward("error");
                    } catch (Lan6UtilExcepcion e) {
                        log.error("Lan6UtilExcepcion en la invocacion al servicio alta de Salida en SIR ", e);
                        //opcion = "error";
                        //elRegistroESVO.setRespOpcion("errorAltaSalidaSIR");
                        sirRegistroSalidaResponse.setCodigoIntRespuesta(-3);
                        sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgErrorAltaSalidaSIR");
                        sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(MessageFormat.format(
                                traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIR"),
                                registroSIRExceptionService.getTextoMensajeErrorFromLan6UtilExcepcion(e)
                        ));
                        //registroForm.setRegistro(elRegistroESVO);
                        //return mapping.findForward("error");
                    } catch (Lan6Excepcion e) {
                        log.error("Lan6Excepcion en la invocacion al servicio alta de Salida en SIR ", e);
                        //opcion = "error";
                        //elRegistroESVO.setRespOpcion("errorAltaSalidaSIR");
                        sirRegistroSalidaResponse.setCodigoIntRespuesta(-2);
                        sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgErrorAltaSalidaSIR");
                        sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(MessageFormat.format(
                                traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIR"),
                                registroSIRExceptionService.getTextoMensajeErrorFromLan6Excepcion(e)
                        ));
                        //registroForm.setRegistro(elRegistroESVO);
                        //return mapping.findForward("error");
                    }
                } else {
                    log.error("No se ha recibido el codigo de procedimiento, no se puede crear la anotacion de salida registro en SIR ... ");
                    // Procesar respuesta para mostrar al usuario que no se puede dar de alta una salida en SIR sin procedimiento de platea
                    //opcion = "error";
                    //elRegistroESVO.setRespOpcion("errorAltaSalidaSIR");
                    //elRegistroESVO.setRetramitarDocumentosDetalleOPeracion(traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIRNoProc"));
                    //registroForm.setRegistro(elRegistroESVO);
                    sirRegistroSalidaResponse.setCodigoIntRespuesta(2);
                    sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgErrorAltaSalidaSIRNoProc");
                    sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(traductorAplicacionBean.getDescripcion("msgErrorAltaSalidaSIRNoProc"));
                    //return mapping.findForward("error");
                }
            }else{
                log.error("No se puede proecesar el Registro SIR. No se reciben datos correctos del registro o el usuario. "
                        + "elRegistroESVO="+(elRegistroESVO!=null)
                );
                sirRegistroSalidaResponse.setCodigoIntRespuesta(1);
                sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgErrorNoDadaAltaSIR");
                sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(MessageFormat.format(
                        traductorAplicacionBean.getDescripcion("msgErrorNoDadaAltaSIR")
                        ,"Objeto elRegistroESVO con datos de registro recibido null, no hay informacion suficiente para procesar la solicitud")
                );
            }
        } catch (Exception e) {
            log.error("Error en el envio Salida al Sistema SIR Lanbide ... " + e.getMessage() + " - " + e.getLocalizedMessage() , e);
            sirRegistroSalidaResponse.setCodigoIntRespuesta(-1);
            sirRegistroSalidaResponse.setCodEstadoRespGestionSIR("msgErrorNoDadaAltaSIR");
            sirRegistroSalidaResponse.setDescEstadoRespGestionSIR(MessageFormat.format(
                    traductorAplicacionBean.getDescripcion("msgErrorNoDadaAltaSIR"),
                    e.getMessage() + " - " + e.getLocalizedMessage())
            );
        }
        return sirRegistroSalidaResponse;
    }

    public SirOficina getSirOficinaByCodigo(String codigoOficina,AdaptadorSQLBD adapt) throws Exception {
        SirOficina respuesta = null;
        Connection con = null;
        try {
            con = adapt.getConnection();
            respuesta = sirOficinaDAO.getSirOficinaByCodigo(codigoOficina,con);
        } catch (Exception e) {
            log.error("Error al recuperar Datos de oficina unidad DIR3 : " +e.getMessage(), e);
            throw e;
        } finally {
            try {
                adapt.devolverConexion(con);
                log.error("getSirOficinaByCodigo Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(),e);
            }
        }
        return respuesta;
    }

    public List<Lan6Anexo> getListaAnexosEntradasRegEnvioSIR(Vector<RegistroValueObject> listaDocumentos, AdaptadorSQLBD adaptadorSQLBD, UsuarioValueObject usuarioVO) {
        List<Lan6Anexo> respuesta = new ArrayList<Lan6Anexo>();
        Connection con = null;
        try {
            con = adaptadorSQLBD.getConnection();
            if (listaDocumentos != null && !listaDocumentos.isEmpty()) {
                for (RegistroValueObject registroValueObject : listaDocumentos) {
                    DocumentoAnotacionRegistroVO documentoAnotacionRegistroVO =  new DocumentoAnotacionRegistroVO();
                    documentoAnotacionRegistroVO.setCodDepartamento(Integer.parseInt(registroValueObject.getDptoUsuarioQRegistra()));
                    documentoAnotacionRegistroVO.setCodigoUorRegistro(registroValueObject.getUnidadOrgan());
                    documentoAnotacionRegistroVO.setEjercicio(registroValueObject.getAnoReg());
                    documentoAnotacionRegistroVO.setNumeroAnotacion((int)registroValueObject.getNumReg());
                    documentoAnotacionRegistroVO.setTipoEntrada(registroValueObject.getTipoReg());
                    documentoAnotacionRegistroVO.setNombreDocumento(registroValueObject.getNombreDoc());

                    //Recuperamos el OID
                    DocumentoAnotacionRegistroVO documentoRegistroOID = anotacionRegistroDAO.getDocumentoAnotacionRegistro(documentoAnotacionRegistroVO, con);
                    Documento documentoRegistro = anotacionRegistroManager.getDocumentoFichero(registroValueObject,usuarioVO);
                    if(documentoRegistroOID!= null && documentoRegistro != null)
                        documentoRegistro.setIdDocGestor(documentoRegistroOID.getIdDocGestor());

                    if(documentoRegistro!=null && documentoRegistro.getIdDocGestor()!=null
                            && !documentoRegistro.getIdDocGestor().isEmpty()){
                        Lan6Anexo lan6Anexo = new Lan6Anexo();
                        lan6Anexo.setNombreFichero(quitarReservadosXMLandLimitarTamanoString(documentoRegistro.getNombreDocumento(),27));
                        lan6Anexo.setOidDokusi(documentoRegistro.getIdDocGestor());
                        // Generamos el hash con el contenido byte[] a Codificado a Base64
                        lan6Anexo.setTipoMIME(documentoRegistro.getTipoDocumento());
                        //lan6Anexo.setHash(String.valueOf(Base64.encodeBase64String(documentoRegistro.getFichero()).hashCode()));
                        try {
                            if(documentoRegistro.getFichero()==null){
                                AlmacenDocumentoDokusiImpl almacenDocumentoDokusi = new AlmacenDocumentoDokusiImpl();
                                documentoRegistro = almacenDocumentoDokusi.getDocumentoRegistro(documentoRegistro);
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                            log.error("Error al intentar recuperar el contenido del fichero - Directamente con adaptadores" + ex.getMessage());
                        }
                        byte[] hash = MessageDigest.getInstance("MD5").digest(documentoRegistro.getFichero());
                        String hashStrg = new BigInteger(1, hash).toString(16);
                        lan6Anexo.setHash(hashStrg);
                        respuesta.add(lan6Anexo);
                    }else{
                        log.info("No es un documento alojado en DOKUSI, No lo enviamos en la entrada del SIR.");
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error al preparar los OID de los documentos " + ex.getMessage(),ex);
        }finally{
            try {
                if(con!=null) adaptadorSQLBD.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar la conexion .." + e.getMessage(), e);
            }
        }
        return respuesta;
    }

    public List<Lan6InteresadoAsientoRegistral> getListaLan6InteresadoAsientoRegistral(Vector listaCodTercero, Vector listaVersionTercero, Vector listaCodDomicilio, String[] params) {
        List<Lan6InteresadoAsientoRegistral> respuesta = new ArrayList<Lan6InteresadoAsientoRegistral>();
        if(listaCodTercero!=null && !listaCodTercero.isEmpty()
                && listaVersionTercero!=null && !listaVersionTercero.isEmpty()
                && listaCodDomicilio!=null && !listaCodDomicilio.isEmpty()
                && (listaCodTercero.size()==listaVersionTercero.size() && listaCodTercero.size()==listaCodDomicilio.size())){
            for (int i = 0; i < listaCodTercero.size(); i++) {
                TercerosValueObject elTercero = new TercerosValueObject();
                int codInter = Integer.parseInt((String)listaCodTercero.get(i));
                int numModInfInt = Integer.parseInt((String)listaVersionTercero.get(i));
                int domicInter = Integer.parseInt((String)listaCodDomicilio.get(i));
                elTercero.setIdentificador(String.valueOf(codInter));
                elTercero.setVersion(String.valueOf(numModInfInt));
                elTercero.setIdDomicilio(String.valueOf(domicInter));
                TercerosValueObject terc = (TercerosValueObject) TercerosManager.getInstance().getByHistorico(elTercero, params).firstElement();
                String direccionInteresado ="";
                if(terc!=null){
                    Lan6InteresadoAsientoRegistral lan6InteresadoAsientoRegistral = new Lan6InteresadoAsientoRegistral();
                    lan6InteresadoAsientoRegistral.setCanalPreferenteComunicacionInteresado(Lan6Constantes.IR.IR_CANAL_NOTIF.IR_CANAL_NOTIF_DIR_POSTAL);
                    if(terc.getDomicilios()!= null){
                        for(Object domicilioObj : terc.getDomicilios()){
                            DomicilioSimpleValueObject domicilio = (DomicilioSimpleValueObject) domicilioObj;
                            if(domicilio.getIdDomicilio()!= null && domicilio.getIdDomicilio().equals(terc.getIdDomicilio())){
                                //lan6InteresadoAsientoRegistral.setCodigoPaisInteresado(domicilio.getIdPaisVia()!= null ? domicilio.getIdPaisVia() : domicilio.getIdPais());
                                String provinciaFormated = domicilio.getIdProvinciaVia() != null ? domicilio.getIdProvinciaVia() : domicilio.getIdProvincia();
                                provinciaFormated = (provinciaFormated!=null && !provinciaFormated.isEmpty() ? StringUtils.leftPad(provinciaFormated,2,"0") : "");
                                String municipipoFormated = domicilio.getIdMunicipioVia() != null ? domicilio.getIdMunicipioVia() : domicilio.getIdMunicipio();
                                municipipoFormated = (municipipoFormated!=null && !municipipoFormated.isEmpty() ? StringUtils.leftPad(municipipoFormated,3,"0") : "");
                                log.error("Consultamos municipio en formato SIR: " + provinciaFormated + " - " + municipipoFormated);
                                try {
                                    SirLocalidades sirLocalidad  = getSirLocalidadesByCodigoProvinciaCodmunFlexia(provinciaFormated,municipipoFormated,new AdaptadorSQLBD(params));
                                    log.error("Municipio en formato SIR: " + sirLocalidad!=null?sirLocalidad.toString():"null");
                                    municipipoFormated=sirLocalidad.getCodLocalidad();
                                } catch (Exception e) {
                                    log.error("Error al leer los datos del municipio en formato SIR " + provinciaFormated + " - " + municipipoFormated);
                                }
                                log.error("Consultamos municipio en formato SIR: " + provinciaFormated + " =>(Obtenemos): " + municipipoFormated);
                                lan6InteresadoAsientoRegistral.setCodigoProvinciaInteresado(provinciaFormated);
                                lan6InteresadoAsientoRegistral.setCodigoMunicipioInteresado(municipipoFormated);
                                lan6InteresadoAsientoRegistral.setCodigoPostalInteresado(domicilio.getCodigoPostal());
                                if(terc.getDomPrincipal()==null || terc.getDomPrincipal().isEmpty()){
                                    String domicilioComp = "";
                                    if(domicilio.getDomicilio()!=null && !domicilio.getDomicilio().isEmpty()){
                                        direccionInteresado=domicilio.getDomicilio();
                                    }else{
                                        direccionInteresado = domicilio.getDescVia();
                                        direccionInteresado += (domicilio.getPortal()!=null && !domicilio.getPortal().isEmpty() ? " ".concat(domicilio.getPortal()):"");
                                        direccionInteresado += (domicilio.getBloque()!=null && !domicilio.getBloque().isEmpty() ? " ".concat(domicilio.getBloque()):"");
                                        direccionInteresado += (domicilio.getEscalera()!=null && !domicilio.getEscalera().isEmpty() ? " ".concat(domicilio.getEscalera()):"");
                                        direccionInteresado += (domicilio.getPlanta()!=null && !domicilio.getPlanta().isEmpty() ? " ".concat(domicilio.getPlanta()):"");
                                        direccionInteresado += (domicilio.getPuerta()!=null && !domicilio.getPuerta().isEmpty() ? " ".concat(domicilio.getPuerta()):"");
                                        direccionInteresado += (domicilio.getProvincia()!=null && !domicilio.getProvincia().isEmpty() ? " ".concat(domicilio.getProvincia()):"");
                                        direccionInteresado += (domicilio.getMunicipio()!=null && !domicilio.getMunicipio().isEmpty() ? " ".concat(domicilio.getMunicipio()):"");
                                        direccionInteresado += (domicilio.getCodigoPostal()!=null && !domicilio.getCodigoPostal().isEmpty() ? " ".concat(domicilio.getCodigoPostal()):"");
                                        direccionInteresado += (domicilio.getPais()!=null && !domicilio.getPais().isEmpty() ? " ".concat(domicilio.getPais()):"");
                                    }
                                }else{
                                    direccionInteresado=domicilio.getDomicilio();
                                }
                                break;
                            }
                        }
                    }
                    //if(lan6InteresadoAsientoRegistral.getCodigoPaisInteresado()== null || lan6InteresadoAsientoRegistral.getCodigoPaisInteresado().isEmpty())
                    lan6InteresadoAsientoRegistral.setCodigoPaisInteresado("724"); // Espana por defecto
                    lan6InteresadoAsientoRegistral.setDireccionInteresado(direccionInteresado); // "DIRECCION"
                    lan6InteresadoAsientoRegistral.setDocumentoIdentificacionInteresado(terc.getDocumento());  //"AA123456"
                    lan6InteresadoAsientoRegistral.setNombreInteresado(quitarReservadosXMLandLimitarTamanoString(terc.getNombre(),30));  // "JOHN"
                    lan6InteresadoAsientoRegistral.setPrimerApellidoInteresado(quitarReservadosXMLandLimitarTamanoString(terc.getApellido1(),30)); // "SMITH"
                    lan6InteresadoAsientoRegistral.setSegundoApellidoInteresado(quitarReservadosXMLandLimitarTamanoString(terc.getApellido2(),30));
                    lan6InteresadoAsientoRegistral.setRazonSocialInteresado(quitarReservadosXMLandLimitarTamanoString(terc.getNombre(),30));  // "JOHN"
                    lan6InteresadoAsientoRegistral.setTipoDocumentoIdentificacionInteresado(getTipoDocumentoAdaptadoresFromTipoDocRegexlan(Integer.valueOf(terc.getTipoDocumento())));  // Lan6Constantes.IR.IR_TIPO_IDENTIFICADOR.IR_TIPO_IDENT_PASAPORTE
                    respuesta.add(lan6InteresadoAsientoRegistral);
                }
            }

        }else{
            log.error("Una de la listas listaCodTercero, listaVersionTercero viene a null o no contienen los mismos elementos .."
                    + "listaCodTercero : " +(listaCodTercero!=null?listaCodTercero.size():"null")
                    + "listaVersionTercero : " +(listaVersionTercero!=null?listaVersionTercero.size():"null")
                    + "listaCodDomicilio : " +(listaCodDomicilio!=null?listaCodDomicilio.size():"null")
            );
        }
        return respuesta;
    }

    public String getTipoDocumentoAdaptadoresFromTipoDocRegexlan(Integer tipoDocumentoRegexlan){
        String respuesta = "";
        log.info("getTipoDocumentoAdaptadoresFromTipoDocRegexlan - Begin - " + tipoDocumentoRegexlan);
        try {
            if(tipoDocumentoRegexlan!=null){
                /**  --- Adaptadores platea => Lan6Constantes.IR.IR_TIPO_IDENTIFICADOR
                 *          public static final String IR_TIPO_IDENT_NIF = "N";
                 * 			public static final String IR_TIPO_IDENT_CIF = "C";
                 * 			public static final String IR_TIPO_IDENT_PASAPORTE = "P";
                 * 			public static final String IR_TIPO_IDENT_NIE = "E";
                 * 			public static final String IR_TIPO_IDENT_OTROS_PF = "X";
                 * 			public static final String IR_TIPO_IDENT_COD_ORIGEN = "O";
                 *
                 * 	--- REGEXLAN
                 *  0	SIN DOCUMENTO
                 *  1	N.I.F.
                 *  2	PASAPORTE
                 *  3	TARJETA RESIDENCIA
                 *  4	C.I.F.
                 *  5	C.I.F. ENT. PUBLICA
                 *  6	NEMOT�CNICO
                 *  7	UOR
                 *  8	W
                 *  9	U
                 */
                switch (tipoDocumentoRegexlan){
                    case 1 :
                        respuesta=Lan6Constantes.IR.IR_TIPO_IDENTIFICADOR.IR_TIPO_IDENT_NIF;
                        break;
                    case 2 :
                        respuesta=Lan6Constantes.IR.IR_TIPO_IDENTIFICADOR.IR_TIPO_IDENT_PASAPORTE;
                        break;
                    case 3 :
                        respuesta=Lan6Constantes.IR.IR_TIPO_IDENTIFICADOR.IR_TIPO_IDENT_NIE;
                        break;
                    case 4: case 5 :
                        respuesta=Lan6Constantes.IR.IR_TIPO_IDENTIFICADOR.IR_TIPO_IDENT_CIF;
                        break;
                    case 8: case 9 :
                        respuesta=Lan6Constantes.IR.IR_TIPO_IDENTIFICADOR.IR_TIPO_IDENT_OTROS_PF;
                        break;
                    default:
                        respuesta="";
                }
            }
        }catch (Exception ex){
            log.error("Error al parsear el tipo de documento regexlan a tipo documento Adaptadores ");
        }
        log.info("getTipoDocumentoAdaptadoresFromTipoDocRegexlan - End - " + respuesta);
        return respuesta;
    }

    public DomicilioSimpleValueObject getDomicilioPrincipalTecero(Vector listaCodTercero, Vector listaVersionTercero, Vector listaCodDomicilio, String[] params) {
        DomicilioSimpleValueObject respuesta = new DomicilioSimpleValueObject();
        if(listaCodTercero!=null && !listaCodTercero.isEmpty()
                && listaVersionTercero!=null && !listaVersionTercero.isEmpty()
                && listaCodDomicilio!=null && !listaCodDomicilio.isEmpty()
                && (listaCodTercero.size()==listaVersionTercero.size() && listaCodTercero.size()==listaCodDomicilio.size())){
            for (int i = 0; i < listaCodTercero.size(); i++) {
                TercerosValueObject elTercero = new TercerosValueObject();
                int codInter = Integer.parseInt((String)listaCodTercero.get(i));
                int numModInfInt = Integer.parseInt((String)listaVersionTercero.get(i));
                int domicInter = Integer.parseInt((String)listaCodDomicilio.get(i));
                elTercero.setIdentificador(String.valueOf(codInter));
                elTercero.setVersion(String.valueOf(numModInfInt));
                elTercero.setIdDomicilio(String.valueOf(domicInter));
                TercerosValueObject terc = (TercerosValueObject) TercerosManager.getInstance().getByHistorico(elTercero, params).firstElement();
                String direccionInteresado ="";
                if(terc!=null){
                    if(terc.getDomicilios()!= null){
                        for(Object domicilioObj : terc.getDomicilios()){
                            DomicilioSimpleValueObject domicilio = (DomicilioSimpleValueObject) domicilioObj;
                            if(domicilio.getIdDomicilio()!= null && domicilio.getIdDomicilio().equals(terc.getIdDomicilio())){
                                respuesta=domicilio;
                                break;
                            }
                        }
                    }
                }
            }

        }else{
            log.error("getDomicilioPrincipalTecero => Una de la listas listaCodTercero, listaVersionTercero viene a null o no contienen los mismos elementos .."
                    + "listaCodTercero : " +(listaCodTercero!=null?listaCodTercero.size():"null")
                    + "listaVersionTercero : " +(listaVersionTercero!=null?listaVersionTercero.size():"null")
                    + "listaCodDomicilio : " +(listaCodDomicilio!=null?listaCodDomicilio.size():"null")
            );
        }
        return respuesta;
    }

    public SirLocalidades getSirLocalidadesByCodigoProvinciaCodmunFlexia(String codigoProvincia,String codigoMunicipio, AdaptadorSQLBD adapt) throws Exception {
        SirLocalidades respuesta = null;
        Connection con = null;
        try {
            con = adapt.getConnection();
            respuesta = sirLocalidadesDAO.getSirLocalidadesByCodigoProvinciaCodFlexia(codigoProvincia,codigoMunicipio,con);
        } catch (Exception e) {
            log.error("Error al recuperar Datos de Munici unidad DIR3 : " +e.getMessage(), e);
            throw e;
        } finally {
            try {
                adapt.devolverConexion(con);
                log.error("getSirLocalidadesByCodigoProvinciaCodmunFlexia Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(),e);
            }
        }
        return respuesta;
    }

    public Map<String,Object> prepararParametrosJustificanteRegistro_SIR(RegistroValueObject elRegistroESVO, List<Lan6InteresadoAsientoRegistral> lan6InteresadoAsientoRegistralList, List<Lan6Anexo> lan6DocumentoEntradaSIRList, int idiomaUsuario, AdaptadorSQLBD adaptadorSQLBD){
        // Parametros del Informe en el Map
        Map<String,Object> parametrosInforme = new HashMap<String, Object>();
        parametrosInforme.put("DESC_TIPO_REGISTRO",(elRegistroESVO.getTipoReg()!= null  && elRegistroESVO.getTipoReg().equalsIgnoreCase("E") ? "Entrada" : "Salida") + " Registro");
        parametrosInforme.put("NUMERO_REGISTRO",elRegistroESVO.getAnoReg() +"/"+elRegistroESVO.getNumReg());
        //try {
            parametrosInforme.put("FECHA_PRESENTACION",elRegistroESVO.getFecEntrada()); //formatDateddMMyyyyHHmmss.parse(elRegistroESVO.getFecEntrada())
        //} catch (ParseException e) {
        //    e.printStackTrace();
       // }
        if(lan6InteresadoAsientoRegistralList!=null && !lan6InteresadoAsientoRegistralList.isEmpty()){
            parametrosInforme.put("NOMBRE_INTERESADO",lan6InteresadoAsientoRegistralList.get(0).getNombreInteresado());
            parametrosInforme.put("NIF",lan6InteresadoAsientoRegistralList.get(0).getDocumentoIdentificacionInteresado());
        }else {
            parametrosInforme.put("NOMBRE_INTERESADO", "");
            parametrosInforme.put("NIF", "");
        }
        // Recuperar los datos del domicilio
        DomicilioSimpleValueObject domicilioPrincipalTecero = getDomicilioPrincipalTecero(elRegistroESVO.getlistaCodTercero(), elRegistroESVO.getlistaVersionTercero(), elRegistroESVO.getlistaCodDomicilio(),adaptadorSQLBD.getParametros());
        String domicilioComp = "";
        if(domicilioPrincipalTecero.getDomicilio()!=null && !domicilioPrincipalTecero.getDomicilio().isEmpty()){
            domicilioComp=domicilioPrincipalTecero.getDomicilio();
        }else{
            domicilioComp = domicilioPrincipalTecero.getDescVia();
            domicilioComp += (domicilioPrincipalTecero.getPortal()!=null && !domicilioPrincipalTecero.getPortal().isEmpty() ? " ".concat(domicilioPrincipalTecero.getPortal()):"");
            domicilioComp += (domicilioPrincipalTecero.getBloque()!=null && !domicilioPrincipalTecero.getBloque().isEmpty() ? " ".concat(domicilioPrincipalTecero.getBloque()):"");
            domicilioComp += (domicilioPrincipalTecero.getEscalera()!=null && !domicilioPrincipalTecero.getEscalera().isEmpty() ? " ".concat(domicilioPrincipalTecero.getEscalera()):"");
            domicilioComp += (domicilioPrincipalTecero.getPlanta()!=null && !domicilioPrincipalTecero.getPlanta().isEmpty() ? " ".concat(domicilioPrincipalTecero.getPlanta()):"");
            domicilioComp += (domicilioPrincipalTecero.getPuerta()!=null && !domicilioPrincipalTecero.getPuerta().isEmpty() ? " ".concat(domicilioPrincipalTecero.getPuerta()):"");
        }
        parametrosInforme.put("DOMICILIO",domicilioComp);
        parametrosInforme.put("POBLACION",domicilioPrincipalTecero.getMunicipio());
        parametrosInforme.put("CODIGO_POSTAL",domicilioPrincipalTecero.getCodigoPostal());
        parametrosInforme.put("PROVINCIA",domicilioPrincipalTecero.getProvincia());
        parametrosInforme.put("PAIS",domicilioPrincipalTecero.getPais());

        if(elRegistroESVO.getUorCodVisible()== null || elRegistroESVO.getUorCodVisible().isEmpty() ){
            String codigoVisible = UORsManager.getInstance().getCodigoVisibleUorByCodUor(elRegistroESVO.getIdUndTramitad(), adaptadorSQLBD.getParametros());
            elRegistroESVO.setUorCodVisible(codigoVisible);
        }
        String dir3Origen = ""; // O00017865 - Registro de Lanbide-Servicio Vasco de Empleo
        try {
            dir3Origen = getDir3UnidadOrganicaOrigenEnvioSIR(elRegistroESVO.getUorCodVisible(), adaptadorSQLBD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SirOficina sirOficinaOrigen = new SirOficina();
        SirUnidadDIR3 sirUnidadDIR3Origen = new SirUnidadDIR3();
        SirOficina sirOficinaDestino = new SirOficina();
        SirUnidadDIR3 sirUnidadDIR3Destino = new SirUnidadDIR3();

        try {
            sirUnidadDIR3Origen = getUnidadDir3ByCodigo(dir3Origen, adaptadorSQLBD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sirUnidadDIR3Destino != null) {
            try {
                sirOficinaOrigen = getSirOficinaByCodigo(sirUnidadDIR3Origen.getCodigoOficina(), adaptadorSQLBD);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            sirUnidadDIR3Destino = getUnidadDir3ByCodigo(elRegistroESVO.getCodigoUnidadDestinoSIR(), adaptadorSQLBD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sirUnidadDIR3Destino != null) {
            try {
                sirOficinaDestino = getSirOficinaByCodigo(sirUnidadDIR3Destino.getCodigoOficina(),adaptadorSQLBD);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(sirOficinaOrigen == null)
            sirOficinaOrigen = new SirOficina();
        if(sirOficinaDestino == null)
            sirOficinaDestino = new SirOficina();
        if(sirUnidadDIR3Origen==null)
            sirUnidadDIR3Origen= new SirUnidadDIR3();
        if (sirUnidadDIR3Destino == null)
            sirUnidadDIR3Destino= new SirUnidadDIR3();

        parametrosInforme.put("ORIGEN_OFICINA",sirUnidadDIR3Origen.getCodigoOficina() + " - " + (idiomaUsuario==4?sirOficinaOrigen.getNombre_EU():sirOficinaOrigen.getNombre_ES()));
        parametrosInforme.put("ORIGEN_UNIDAD_TRAMITACION",sirUnidadDIR3Origen.getCodigoUnidad() + " - " + (idiomaUsuario==4?sirUnidadDIR3Origen.getNombreUnidad_EU():sirUnidadDIR3Origen.getNombreUnidad_ES()));
        parametrosInforme.put("DESTINO_OFICINA",sirUnidadDIR3Destino.getCodigoOficina() + " - " + (idiomaUsuario==4?sirOficinaDestino.getNombre_EU():sirOficinaDestino.getNombre_ES()) );
        parametrosInforme.put("DESTINO_UNIDAD_TRAMITADORA", sirUnidadDIR3Destino.getCodigoUnidad() + " - " + (idiomaUsuario == 4 ? sirUnidadDIR3Destino.getNombreUnidad_EU() : sirUnidadDIR3Destino.getNombreUnidad_ES()));
        parametrosInforme.put("RESUMEN",elRegistroESVO.getAsunto());
        parametrosInforme.put("NUMERO_EXPEDIENTE",elRegistroESVO.getNumExpediente());
        parametrosInforme.put("TIPO_TRANSPORTE",elRegistroESVO.getCodTipoTransp());
        parametrosInforme.put("NUMERO_TRANSPORTE",elRegistroESVO.getNumTransporte());
        parametrosInforme.put("EXPONE_TEXTO","");
        parametrosInforme.put("SOLICITA_TEXTO","");
        parametrosInforme.put("LISTA_DOCUMENTOS",lan6DocumentoEntradaSIRList);

        return parametrosInforme;
    }

    public String getCodigoOficinaForCodigoUnidad(String codigoUnidad, AdaptadorSQLBD adapt) throws Exception {
        String respuesta = null;
        Connection con = null;
        String server = null;
        try {
            server = getServer();

            if (server != null && server.contains("pargi")) {
                con = adapt.getConnection();
                respuesta =  new SirDestinoRResDAO().getCodigoOficinaForCodigoUnidad(codigoUnidad.trim(), con);

            } else {
                respuesta = "O00045691";
            }

        } catch (Exception e) {
            log.error("Error al recuperar Datos de Oficina DIR3 : " +e.getMessage(), e);
            throw e;
        } finally {
            try {
                adapt.devolverConexion(con);
                log.error("getCodigoOficinaForCodigoUnidad Service - End ");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(),e);
            }
        }
        return respuesta;
    }
    public List<Lan6AsientoRegistralRespuesta> buscarRegistrosSIR(String estado, String identificador, String codigoUnidadDestino,  String codigoUnidadOrigen, String[] paramsbd) throws ServicioSIRException {
        log.debug("Entramos en findAsientosRegistrales con idAsiento: " + identificador);
        List<Lan6AsientoRegistralRespuesta> listaRegistros = null;
        Connection con = null;
        String codigoProcedimiento = "SIRTR";

        try {
            log.debug("Instanciamos el objeto Lan6AE20IntercambioRegistralServicios con codigo procedimiento" + codigoProcedimiento);
            Lan6AE20IntercambioRegistralServicios lan6AE20IntercambioRegistralServicios = new Lan6AE20IntercambioRegistralServicios(codigoProcedimiento);

            // Prepara criterios
            List<Lan6Criterio> listaCriterio = new ArrayList<Lan6Criterio>();

            // Criterio identificador
            if (identificador != null && !identificador.trim().isEmpty()) {
                List<String> valoresIdentificador = new ArrayList<String>();
                valoresIdentificador.add(identificador != null ? identificador.trim() : "");
                Lan6Criterio criterioIdentificador = generarCriterio("cd_intercambio", "=", valoresIdentificador);
                listaCriterio.add(criterioIdentificador);
            }

            // Criterio estado
            if (estado != null && !estado.trim().isEmpty()) {
                List<String> valoresEstado = new ArrayList<String>();
                valoresEstado.add(estado != null ? estado.trim() : "");
                Lan6Criterio criterioEstado = generarCriterio("cd_estado", "=", valoresEstado);
                listaCriterio.add(criterioEstado);
            }

            // Criterio unidad destino (usando el código de oficina)
            if (codigoUnidadDestino != null && !codigoUnidadDestino.trim().isEmpty()) {
                String codigoOficinaDestino = getCodigoOficinaForCodigoUnidad(codigoUnidadDestino.trim(), new AdaptadorSQLBD(paramsbd));
                //codigoOficinaDestino = "O00045691";

                if (codigoOficinaDestino != null && !codigoOficinaDestino.trim().isEmpty()) {
                    List<String> valoresUnidadDestino = new ArrayList<String>();
                    valoresUnidadDestino.add(codigoOficinaDestino.trim());
                    Lan6Criterio criterioUnidadDestino = generarCriterio("cd_ent_reg_destino", "=", valoresUnidadDestino);
                    listaCriterio.add(criterioUnidadDestino);
                } else {
                    log.error("No se ha encontrado código de oficina para la unidad: " + codigoUnidadDestino);
                    throw new ServicioSIRException("No se ha encontrado código de oficina para la unidad: " + codigoUnidadDestino);
                }
            }

            // Criterio unidad origen (usando el código de oficina)
            // if ( codigoUnidadOrigen != null && !codigoUnidadOrigen.trim().isEmpty()) {
                 //El codigo de oficina por defecto en DESA y PRE para PRO sera diferente TODO funcion pora recuperar codigo por entorno
                 String codigoOficinaOrigen = "O00045691";
                 if (codigoOficinaOrigen != null && !codigoOficinaOrigen.trim().isEmpty()) {
                     List<String> valoresUnidadOrigen= new ArrayList<String>();
                     valoresUnidadOrigen.add(codigoOficinaOrigen.trim());
                     Lan6Criterio criterioUnidadOrigen = generarCriterio("cd_ent_reg_origen", "=", valoresUnidadOrigen);
                     listaCriterio.add(criterioUnidadOrigen);
                 } else {
                     log.error("No se ha encontrado código de oficina para la unidad: " + codigoOficinaOrigen);
                     throw new ServicioSIRException("No se ha encontrado código de oficina para la unidad: " + codigoOficinaOrigen);
                     }
             //}

            // Lan6PageInfo pageInfo = new Lan6PageInfo();
            // pageInfo.setPageNumber(1);
            // pageInfo.setObjectsPerPage(10);
            // pageInfo.setMaxNumItems(133);

            Lan6Criterios criterios = new Lan6Criterios();
            criterios.setListaLan6Criterios(listaCriterio);
            // criterios.setLan6PageInfo(pageInfo);
            listaRegistros = lan6AE20IntercambioRegistralServicios.findAsientosRegistrales(criterios);

            return listaRegistros;

        } catch (Exception e) {
            log.error("Error grave al llamar al SIR: " + e.getMessage(), e);
            throw new ServicioSIRException("Error al recuperar lista de asientos en el SIR", e);
        }
    }

    private Lan6Criterio generarCriterio(String nombre, String operador, List<String> listaValores) {
        Lan6Criterio criterio = new Lan6Criterio();
        criterio.setNombre(nombre);
        criterio.setOperador(operador);
        criterio.setValor(listaValores);
        return criterio;
    }

    private String getServer(){
        log.debug("RegistroSIRService.getServer()::BEGIN");
        Config configLanbide = ConfigServiceHelper.getConfig("Lanbide");
        String server = configLanbide.getString("SERVIDOR");
        log.debug("RegistroSIRService.getServer()::END - Server: " + server);
        return server;
    }
}