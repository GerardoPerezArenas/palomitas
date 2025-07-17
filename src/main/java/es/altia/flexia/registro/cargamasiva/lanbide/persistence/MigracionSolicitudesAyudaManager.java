package es.altia.flexia.registro.cargamasiva.lanbide.persistence;

import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.persistence.manual.AnotacionRegistroDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.registro.cargamasiva.lanbide.exception.MigracionSolicitudesAyudaException;
import es.altia.flexia.registro.cargamasiva.lanbide.persistence.manual.MigracionSolicitudesAyudaDAO;
import es.altia.flexia.registro.cargamasiva.lanbide.util.MigracionSolicitudesAyudaConstantesDatos;
import es.altia.flexia.registro.cargamasiva.lanbide.vo.SolicitudVO;
import es.altia.flexia.webservice.objetos.DomicilioVO;
import es.altia.flexia.webservice.objetos.InfoConexionVO;
import es.altia.flexia.webservice.objetos.RegistroVO;
import es.altia.flexia.webservice.objetos.RemitenteVO;
import es.altia.flexia.webservice.objetos.SalidaRegistroESBean;
import es.altia.flexia.webservice.registro.WSRegistroESPortSoapBindingStub;
import es.altia.flexia.webservice.registro.WSRegistroESServiceLocator;
import es.altia.util.StringUtils;
import es.altia.util.commons.DateOperations;
import es.altia.util.commons.IdentificadorTerceroUtils;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;

public class MigracionSolicitudesAyudaManager {

    private static MigracionSolicitudesAyudaManager instance = null;
    private static Logger log = Logger.getLogger(MigracionSolicitudesAyudaManager.class.getName());

    public MigracionSolicitudesAyudaManager() {
    }

    public static MigracionSolicitudesAyudaManager getInstance() {
    // Necesitamos sincronización aquí para serializar (no multithread)
    // las invocaciones a este metodo
    synchronized(MigracionSolicitudesAyudaManager.class) {
        if (instance == null) {
          instance = new MigracionSolicitudesAyudaManager();
        }      
    }
    return instance;
}

    public List<SolicitudVO> obtenerSolicitudes(String[] params, String codProc) throws MigracionSolicitudesAyudaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        List<SolicitudVO> solicitudes = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            solicitudes = MigracionSolicitudesAyudaDAO.getInstance().obtenerSolicitudes(con, codProc);

        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener la conexión a la BBDD", bde);
            throw new MigracionSolicitudesAyudaException(1,"Ha ocurrido un error al obtener la conexión a la BBDD", bde);
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al recuperar las solicitudes de ayuda", sqle);
            throw new MigracionSolicitudesAyudaException(2,"Ha ocurrido un error al recuperar las solicitudes de ayuda", sqle);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex){
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD", ex);
            }
        }

        return solicitudes;
    }

    public List<String> insercionRegistroFlexia(List<SolicitudVO> solicitudes, String[] params, String codProc) throws MigracionSolicitudesAyudaException {
        Config ficheroPropiedades = ConfigServiceHelper.getConfig("common");
        WSRegistroESPortSoapBindingStub binding = null;
        InfoConexionVO infoConexion = null;
        RegistroVO anotacion = null;
        SalidaRegistroESBean salida = null;
        URL urlCienteWS = null;		
        ArrayList<String> listaMensajes = new ArrayList<String>();

        try {
            String WSRegistroESEndPoint = ficheroPropiedades.getString("WSRegistroES_EndPoint");
            urlCienteWS = new URL(WSRegistroESEndPoint);
            binding = (WSRegistroESPortSoapBindingStub) new WSRegistroESServiceLocator().getWSRegistroESPort(urlCienteWS);

            log.info(String.format("MigracionSolicitudesAyudaManager.insercionRegistroFlexia(). Conexión a WSRegistro con Url: %s - Aplicación: %s - Organización: %d",
                WSRegistroESEndPoint, MigracionSolicitudesAyudaConstantesDatos.APLICACION, MigracionSolicitudesAyudaConstantesDatos.ORGANIZACION));
            infoConexion = new InfoConexionVO(MigracionSolicitudesAyudaConstantesDatos.APLICACION, MigracionSolicitudesAyudaConstantesDatos.ORGANIZACION.toString());
            for(SolicitudVO solicitud : solicitudes) {
                anotacion = convertirSolicitudVOARegistroVO(solicitud, codProc);
                log.info(String.format("MigracionSolicitudesAyudaManager.insercionRegistroFlexia(). Se va a insertar la solicitud con CORR_ = %d y FECHA_ALTA = %s",
                solicitud.getIdSolicitud(), DateOperations.toString(solicitud.getFecAlta(), "dd/MM/yyyy HH:mm:ss")));
                try {
                    salida = binding.setRegistroES(anotacion, infoConexion);
                } catch (RemoteException re) {
                    log.error("Ha ocurrido un error al llamar a la operación setRegistroES de WSRegistroES", re);
                }

                if(salida.getStatus() == 0) {
                    log.info(String.format("MigracionSolicitudesAyudaManager.insercionRegistroFlexia(). Inserción correcta. Anotación %s/%s con fecha %s",
                    salida.getEjercicio(), salida.getNumero(), salida.getFecha()));
                    anotacion.setNumero(salida.getNumero());
                    anotacion.setEjercicio(salida.getEjercicio());

                    realizarOperacionesSobreAnotacionInsertada(anotacion, solicitud, params, codProc);

                } else {
                    log.warn(String.format("MigracionSolicitudesAyudaManager.insercionRegistroFlexia(). Inserción con error. %s", salida.getDescStatus()));
                }

                listaMensajes.add(construirMensaje(solicitud.getIdSolicitud(), salida));
            }
        } catch (MalformedURLException mfue) {
                log.error("Ha ocurrido un error al obtener la url del WSRegistroES", mfue);
                throw new MigracionSolicitudesAyudaException(3,"Ha ocurrido un error al obtener la url del WSRegistroES", mfue);
        } catch (ServiceException se) {
                log.error("Ha ocurrido un error al obtener el binding de WSRegistroES", se);
                throw new MigracionSolicitudesAyudaException(4,"Ha ocurrido un error al obtener el binding de WSRegistroES", se);
        } 

        return listaMensajes;
    }

    public void realizarOperacionesSobreAnotacionInsertada(RegistroVO anotacion, SolicitudVO solicitud, String[] params, String codProc) {
        AdaptadorSQLBD adapt = null;
        Connection con = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            //Insertamos los nombres de los documentos de la solicitud como documentos aportados anterior (R_DOC_APORTADOS_ANTERIOR)
            insertarReferenciasDocumentosSolicitud(solicitud, anotacion, con, codProc);

            // Quitamos la marca de anotacion telematica a la anotacion creada
            MigracionSolicitudesAyudaDAO.getInstance().indicarAnotacionNoTelematica(anotacion, con);
        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener la conexión a la BBDD", bde);
        } catch (AnotacionRegistroException are) {
            log.error("Ha ocurrido un error al insertar las referencias a los documentos de la solicitud", are);
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al indicar que la anotación no es telemática", sqle);
        } catch (Exception e) {
            log.error("Ha ocurrido un error al insertar las referencias a los documentos de la solicitud", e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex){
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD", ex);
            }
        }
    }

    public void insertarReferenciasDocumentosSolicitud(SolicitudVO solicitud, RegistroVO anotacion, Connection con, String codProc) throws AnotacionRegistroException, TechnicalException, ParseException {
        ArrayList<RegistroValueObject> documentosSolicitud = new ArrayList<RegistroValueObject>();

        // Transformamos cada documento de la solicitud en un objeto RegistroValueObject y lo añadimos a una lista de documentos aportados
        anhadirDocumentoAListaDocumentosAportados(documentosSolicitud, anotacion, solicitud.getNombresDocumentos(), codProc);

        // Realizamos la insercion en R_DOC_APORTADOS_ANTERIOR
        AnotacionRegistroDAO.getInstance().insertarDocsEntregadosAnterior(con, documentosSolicitud);
    }

    public void anhadirDocumentoAListaDocumentosAportados(List<RegistroValueObject> documentos, RegistroVO anotacion, List<String> nombresDocumentos, String codProc) {
        RegistroValueObject doc = null;

        if (nombresDocumentos != null && nombresDocumentos.size() > 0) {
            for (int i = 0; i < nombresDocumentos.size(); i++) {
                if(StringUtils.isNotNullOrEmpty(nombresDocumentos.get(i))){
                    doc = new RegistroValueObject();

                    doc.setIdentDepart(anotacion.getDepartamento());
                    doc.setUnidadOrgan(anotacion.getCodUorRegistro());
                    doc.setAnoReg(Integer.parseInt(anotacion.getEjercicio()));
                    doc.setNumReg(Long.parseLong(anotacion.getNumero()));
                    doc.setTipoReg(anotacion.getTipo());
                    doc.setNombreDocAnterior(generarUrlDescargaDocumento(nombresDocumentos.get(i)));
                    if ("ATASE".equals(codProc)) {
                        doc.setTipoDocAnterior(obtenerDescipcionDocumentoATASE(i));
                    } else if ("ACASE".equals(codProc)) {
                        doc.setTipoDocAnterior(obtenerDescipcionDocumentoACASE(i));
                    }
                    doc.setOrganoDocAnterior("Formulario Web Lanbide");
                    doc.setFechaDocAnterior(anotacion.getFechaPresentacion());

                    documentos.add(doc);

                }

            }

        } else {
            log.debug("anhadirDocumentoAListaDocumentosAportados La lista de nombres de documentos se encuentra vacia");
        }
    }

    public ArrayList<GeneralValueObject> obtenerTipoProcedimientos(String[] params, String codProc, int idioma) throws MigracionSolicitudesAyudaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        ArrayList<GeneralValueObject> tiposProcedimientos = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            tiposProcedimientos = MigracionSolicitudesAyudaDAO.getInstance().obtenerProcPermitidos(con, codProc, idioma);

        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener la conexión a la BBDD", bde);
            throw new MigracionSolicitudesAyudaException(1,"Ha ocurrido un error al obtener la conexión a la BBDD", bde);
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al recuperar las solicitudes de ayuda", sqle);
            throw new MigracionSolicitudesAyudaException(2,"Ha ocurrido un error al recuperar las solicitudes de ayuda", sqle);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex){
                log.error("Ha ocurrido un error al cerrar la conexión a la BBDD", ex);
            }
        }
        return tiposProcedimientos;
    }

    private String obtenerDescipcionDocumentoATASE (int indiceDocumento) {
        String desc;

        switch(indiceDocumento) {
            case 0:
                    desc = "Solicitud";
                    break;
            case 1:
                    desc = "Resolución concesión prestación";
                    break;
            case 2:
                    desc = "Contrato arrendamiento";
                    break;
            case 3:
                    desc = "Acreditación constitución";
                    break;
            case 4:
                    desc = "Certificado obligaciones tributarias";
                    break;
            case 5:
                    desc = "Alta registro terceros";
                    break;
            default:
                    desc = "Documento general";
                    break;
        }

        return desc;
    }

    private String obtenerDescipcionDocumentoACASE (int indiceDocumento) {
        String desc;

        switch(indiceDocumento) {
            case 0:
                    desc = "Solicitud";
                    break;
            case 1:
                    desc = "Acreditación constitución Comunidad de Bienes";
                    break;
            case 2:
                    desc = "Declaración IRPF ejercicio 2019";
                    break;
            case 3:
                    desc = "Regulación cotizaciones";
                    break;
            case 4:
                    desc = "Acreditación actividad en la que se encontraba de alta";
                    break;
            case 5:
                    desc = "Contratos de trabajo";
                    break;
            case 6:
                    desc = "Acreditación cumplimiento obligaciones trib. y de Seg. Social";
                    break;
            case 7:
                    desc = "Alta Registro Terceros Departamento Hacienda y Economía";
                    break;
            case 8:
                    desc = "Solicitud reducción IRPF";
                    break;
            default:
                    desc = "Documento general";
                    break;
        }

        return desc;
    }

    private RegistroVO convertirSolicitudVOARegistroVO(SolicitudVO solicitud, String codProc) {
        RegistroVO anotacion = new RegistroVO();

        anotacion.setCodUorRegistro(MigracionSolicitudesAyudaConstantesDatos.CODUNIDADREGISTRO);
        anotacion.setDepartamento(MigracionSolicitudesAyudaConstantesDatos.CODDEPARTAMENTO);

        anotacion.setFechaPresentacion(DateOperations.toString(solicitud.getFecAlta(), "dd/MM/yyyy HH:mm"));
        anotacion.setCodTipoDocumento(MigracionSolicitudesAyudaConstantesDatos.TIPODOCUMENTO.toString());
        anotacion.setTipo(MigracionSolicitudesAyudaConstantesDatos.TIPOANOTACION);
        anotacion.setAsunto(prepararTextoAsunto(solicitud, codProc));
        anotacion.setProcedimiento(codProc);
        if ("ATASE".equals(codProc)){
            anotacion.setCodAsuntoCodificado(MigracionSolicitudesAyudaConstantesDatos.CODASUNTOCODIFICADOATASE);
            anotacion.setCodUorOrigenDestino(MigracionSolicitudesAyudaConstantesDatos.CODUNIDADDESTINOATASE.toString());
            anotacion.setObservaciones(prepararTextoObservaciones(solicitud));
            anotacion.setInteresados(setDatosInteresados(solicitud));
        } else if ("ACASE".equals(codProc)) {
            anotacion.setCodAsuntoCodificado(MigracionSolicitudesAyudaConstantesDatos.CODASUNTOCODIFICADOACASE);
            anotacion.setCodUorOrigenDestino(MigracionSolicitudesAyudaConstantesDatos.CODUNIDADDESTINOATASE.toString());
            anotacion.setObservaciones(prepararTextoObservaciones(solicitud));
            anotacion.setInteresados(setDatosInteresados(solicitud));
        } else if ("AERTE".equals(codProc)) {
            anotacion.setCodAsuntoCodificado(MigracionSolicitudesAyudaConstantesDatos.CODASUNTOCODIFICADOAERTE);
            anotacion.setCodUorOrigenDestino(MigracionSolicitudesAyudaConstantesDatos.CODUNIDADDESTINOAERTE.toString());
            anotacion.setObservaciones(prepararTextoObservacionesAERTE(solicitud));
            anotacion.setInteresados(setDatosInteresadosAERTE(solicitud));
        }

        return anotacion;
    }
	
    private RemitenteVO[] setDatosInteresados(SolicitudVO solicitud) {
        DomicilioVO domicilio = new DomicilioVO();
        RemitenteVO interesado = new RemitenteVO();
        RemitenteVO[] interesados = new RemitenteVO[1];

        domicilio.setCodPais(MigracionSolicitudesAyudaConstantesDatos.CODPAIS);
        domicilio.setCodProvincia(Integer.parseInt(solicitud.getCodProvincia()));
        domicilio.setCodMunicipio(Integer.parseInt(solicitud.getCodMunicipio()));
        domicilio.setCodPostal(solicitud.getCodPostal());
        domicilio.setTipoVia(MigracionSolicitudesAyudaConstantesDatos.TIPOVIA);
        domicilio.setEmplazamiento(MigracionSolicitudesAyudaConstantesDatos.EMPLAZAMIENTO);

        interesado.setNombre(solicitud.getNombreInteresado());
        interesado.setApe1(solicitud.getApe1Interesado());
        interesado.setApe2(solicitud.getApe2Interesado());
        interesado.setDocumento(solicitud.getNifInteresado());
        interesado.setTipoDoc(new Integer(IdentificadorTerceroUtils.esTipoCifNifoNie(solicitud.getNifInteresado())).toString());
        interesado.setEmail(solicitud.getEmailInteresado());
        interesado.setTelefono(solicitud.getTelfInteresado());
        interesado.setDomicilio(domicilio);

        interesados[0] = interesado;
        return interesados;
    }
	
    private RemitenteVO[] setDatosInteresadosAERTE(SolicitudVO solicitud) {
        DomicilioVO domicilio = new DomicilioVO();
        RemitenteVO interesado = new RemitenteVO();
        RemitenteVO[] interesados = new RemitenteVO[1];

        domicilio.setCodPais(MigracionSolicitudesAyudaConstantesDatos.CODPAIS);
        domicilio.setCodProvincia(Integer.parseInt(solicitud.getCodProvincia()));
        domicilio.setCodMunicipio(Integer.parseInt(solicitud.getCodMunicipio()));
        domicilio.setCodPostal(solicitud.getCodPostal());
        domicilio.setTipoVia(Integer.parseInt(solicitud.getTipoVia()));
        domicilio.setVia(solicitud.getNombreVia());
        //domicilio.setNumero(Integer.parseInt(solicitud.getNumeroVia()));
        domicilio.setEmplazamiento(solicitud.getNumeroVia());
        domicilio.setBloque(solicitud.getBisDuplicado());
        domicilio.setEscalera(solicitud.getEscalera());
        domicilio.setPlanta(solicitud.getPiso());
        domicilio.setPuerta(solicitud.getLetra());
        //domicilio.setEmplazamiento(MigracionSolicitudesAyudaConstantesDatos.EMPLAZAMIENTO);

        interesado.setNombre(solicitud.getNombreInteresado());
        interesado.setApe1(solicitud.getApe1Interesado());
        interesado.setApe2(solicitud.getApe2Interesado());
        interesado.setDocumento(solicitud.getNifInteresado());
        interesado.setTipoDoc(new Integer(IdentificadorTerceroUtils.esTipoCifNifoNie(solicitud.getNifInteresado())).toString());
        interesado.setEmail(solicitud.getEmailInteresado());
        interesado.setTelefono(solicitud.getTelfInteresado());
        interesado.setDomicilio(domicilio);

        interesados[0] = interesado;
        return interesados;
    }

    private String prepararTextoObservaciones(SolicitudVO solicitud) {
        StringBuilder xml = new StringBuilder();

        xml.append("<SOLICITUD>")
            .append("<CORR_CONSULTA>").append(solicitud.getIdSolicitud()).append("</CORR_CONSULTA>")
            .append("<REGISTRO>").append(solicitud.getCodRegistro()).append("</REGISTRO>");

         if (solicitud.getNombresDocumentos() != null && solicitud.getNombresDocumentos().size() > 0) {
             List<String> listaNombresDocumentos = solicitud.getNombresDocumentos();
            for (int i = 0; i < listaNombresDocumentos.size(); i++) {
                if(StringUtils.isNotNullOrEmpty(listaNombresDocumentos.get(i))){
                    xml.append("<DOCUMENTO"+i+">").append(listaNombresDocumentos.get(i)).append("</DOCUMENTO"+i+">");
                }

            }

         }

        xml.append("</SOLICITUD>");

        return xml.toString();
    }
	
    private String prepararTextoObservacionesAERTE(SolicitudVO solicitud) {
        StringBuilder xml = new StringBuilder();

        xml.append("<SOLICITUD>")
                .append("<CORR_REG>").append(solicitud.getIdSolicitud()).append("</CORR_REG>");

        xml.append("</SOLICITUD>");

        return xml.toString();
    }
	
    private String prepararTextoAsunto(SolicitudVO solicitud, String codProc) {
        StringBuilder asunto = new StringBuilder(MigracionSolicitudesAyudaConstantesDatos.EXTRACTOASUNTO);
        asunto.append(codProc);
        asunto.append(" - ");
        asunto.append(solicitud.getIdSolicitud().toString());
        asunto.append(", ").append(DateOperations.toString(solicitud.getFecAlta(), "dd/MM/yyyy HH:mm:ss"));

        return asunto.toString();
    }

    private String generarUrlDescargaDocumento(String nombreDocumento) {
        StringBuilder url = new StringBuilder("");

        try {
            if(StringUtils.isNotNullOrEmpty(nombreDocumento)){
                url.append(MigracionSolicitudesAyudaConstantesDatos.URLDESCARGADOCUMENTO);
                url.append(URLEncoder.encode(nombreDocumento, "ISO_8859-1"));
            }
        } catch (UnsupportedEncodingException uee) {
            log.error(String.format("El texto \"%s\" no soporta la codificación de url válida", nombreDocumento), uee);
        }

        return url.toString();
    }
	
    private String construirMensaje(Integer idSolicitud, SalidaRegistroESBean resultadoLlamada) {
        StringBuilder mensaje = new StringBuilder();

        mensaje.append("La solicitud con identificador ").append(idSolicitud).append(" ");
        if (resultadoLlamada.getStatus() == 0) {
            mensaje.append("se ha insertado correctamente. La anotación creada es ").append(resultadoLlamada.getEjercicio())
                .append("/").append(resultadoLlamada.getNumero());
        } else {
            mensaje.append("no se ha podido insertar. Para saber las razones consulte con el administrador.");
        }

        return mensaje.toString();
    }
}
