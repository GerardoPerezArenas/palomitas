package es.altia.flexia.portafirmasexternocliente.plugin.sanse;

import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.portafirmas.plugin.util.ConstantesPortafirmas;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.vo.ModifyServiceBaseParams;
import es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.vo.QueryServiceBaseParams;
import java.util.regex.Pattern;
import org.apache.commons.lang.math.NumberUtils;

public class SanseUtils {

    // Propiedades
    private static final Config configPortafirmas = ConfigServiceHelper.getConfig("Portafirmas");
    
    /**
     * Devulve una propiedad definida en el fichero Portafirmas para el cliente
     * externo de SANSE
     *
     * @param propiedad
     * @return
     */
    public static String getPropiedadSanse(String propiedad) {
        return getPropiedadSanse(null, propiedad);
    }

    /**
     * Devulve una propiedad definida en el fichero Portafirmas para el cliente
     * externo de SANSE
     *
     * @param codOrganizacion
     * @param propiedad
     * @return
     */
    public static String getPropiedadSanse(String codOrganizacion, String propiedad) {
        String valorPropiedad = null;

        if (codOrganizacion != null) {
            valorPropiedad = configPortafirmas.getString(
                    String.format(ConstantesPortafirmasSanse.PROPERTIES_PATTERN_WITH_ORG,
                            codOrganizacion,
                            ConstantesPortafirmasSanse.PROPERTIES_BASE,
                            propiedad));
        } else {
            valorPropiedad = configPortafirmas.getString(
                    String.format(ConstantesPortafirmasSanse.PROPERTIES_PATTERN_WITHOUT_ORG,
                            ConstantesPortafirmasSanse.PROPERTIES_BASE,
                            propiedad));
        }

        return valorPropiedad;
    }

    /**
     * Devulve una propiedad como booleano definida en el fichero Portafirmas
     * para el cliente externo de SANSE
     *
     * @param propiedad
     * @return
     */
    public static Boolean getPropiedadSanseBoolean(String propiedad) {
        return getPropiedadSanseBoolean(null, propiedad);
    }

    /**
     * Devulve una propiedad como booleano definida en el fichero Portafirmas
     * para el cliente externo de SANSE
     *
     * @param codOrganizacion
     * @param propiedad
     * @return
     */
    public static Boolean getPropiedadSanseBoolean(String codOrganizacion, String propiedad) {
        Boolean valorPropiedad = null;

        if (codOrganizacion != null) {
            valorPropiedad = configPortafirmas.getBoolean(
                    String.format(ConstantesPortafirmasSanse.PROPERTIES_PATTERN_WITH_ORG,
                            codOrganizacion,
                            ConstantesPortafirmasSanse.PROPERTIES_BASE,
                            propiedad));
        } else {
            valorPropiedad = configPortafirmas.getBoolean(
                    String.format(ConstantesPortafirmasSanse.PROPERTIES_PATTERN_WITHOUT_ORG,
                            ConstantesPortafirmasSanse.PROPERTIES_BASE,
                            propiedad));
        }

        return valorPropiedad;
    }

    /**
     * Devulve una propiedad como entero definida en el fichero Portafirmas para
     * el cliente externo de SANSE
     *
     * @param propiedad
     * @return
     */
    public static Integer getPropiedadSanseInt(String propiedad) {
        return getPropiedadSanseInt(null, propiedad);
    }

    /**
     * Devulve una propiedad como entero definida en el fichero Portafirmas para
     * el cliente externo de SANSE
     *
     * @param codOrganizacion
     * @param propiedad
     * @return
     */
    public static Integer getPropiedadSanseInt(String codOrganizacion, String propiedad) {
        Integer valorPropiedad = null;

        if (codOrganizacion != null) {
            valorPropiedad = configPortafirmas.getInt(
                    String.format(ConstantesPortafirmasSanse.PROPERTIES_PATTERN_WITH_ORG,
                            codOrganizacion,
                            ConstantesPortafirmasSanse.PROPERTIES_BASE,
                            propiedad));
        } else {
            valorPropiedad = configPortafirmas.getInt(
                    String.format(ConstantesPortafirmasSanse.PROPERTIES_PATTERN_WITHOUT_ORG,
                            ConstantesPortafirmasSanse.PROPERTIES_BASE,
                            propiedad));
        }

        return valorPropiedad;
    }
    
    /**
     * Obtiene el ejercicio a partir del numero de expediente con formato
     * "2017/EXPE/123456"
     *
     * @param numExpediente
     * @return
     */
    public static Integer extraerEjercicioNumExpediente(String numExpediente) {
        Integer ejercicio = null;

        if (numExpediente != null) {
            String[] numExpedienteSplit = numExpediente.split("/");

            if (numExpedienteSplit.length == 3) {
                ejercicio = NumberUtils.createInteger(numExpedienteSplit[0]);
            }
        }

        return ejercicio;
    }

    /**
     * Obtiene el id de peticion a partir del codigo de peticion guardado en
     * base de datos.
     *
     * @param codigoPeticion
     * @return
     */
    public static String extraerIdPeticion(String codigoPeticion) {
        return extraerCamposPeticion(codigoPeticion, ConstantesPortafirmasSanse.SANSE_POSICION_ID_REQUEST);
    }
    
    /**
     * Obtiene el id de documento a partir del codigo de peticion guardado en
     * base de datos.
     *
     * @param codigoPeticion
     * @return
     */
    public static String extraerIdDocumento(String codigoPeticion) {
        return extraerCamposPeticion(codigoPeticion, ConstantesPortafirmasSanse.SANSE_POSICION_ID_DOCUMENTO);
    }
    
    private static String extraerCamposPeticion(String codigoPeticion, int numero) {
        String idDocumento = null;

        if (codigoPeticion != null) {
            String[] codigoPeticionSplit
                    = codigoPeticion.split(Pattern.quote(ConstantesPortafirmasSanse.SANSE_ID_REQUEST_SEPARADOR_ID_DOCUMENTO));

            if (codigoPeticionSplit.length == 2) {
                idDocumento = codigoPeticionSplit[numero];
            }
        }

        return idDocumento;
    }
    
    /**
     * Para los estados de la firma del documengto propios del portafirmas
     * externo devuelve el codigo equivalente para flexia
     *
     * @param estado
     * @return
     */
    public static String mapeoEstadoFirma(String estado) {
        String codFlexia = "";
        
        String codFirmado = getPropiedadSanse(ConstantesPortafirmasSanse.PROPERTIES_ESTADO_FIRMA_FIRMADO);
        String codRechazado = getPropiedadSanse(ConstantesPortafirmasSanse.PROPERTIES_ESTADO_FIRMA_RECHAZADO);
        String codPendiente = getPropiedadSanse(ConstantesPortafirmasSanse.PROPERTIES_ESTADO_FIRMA_PENDIENTE);
        String codCaducado = getPropiedadSanse(ConstantesPortafirmasSanse.PROPERTIES_ESTADO_FIRMA_CADUCADO);
        String codRetirado = getPropiedadSanse(ConstantesPortafirmasSanse.PROPERTIES_ESTADO_FIRMA_RETIRADO);

        if (estado.equalsIgnoreCase(codFirmado)) {
            codFlexia = ConstantesPortafirmas.ESTADO_FIRMA_FIRMADA;
        } else if (estado.equalsIgnoreCase(codPendiente)) {
            codFlexia = ConstantesPortafirmas.ESTADO_FIRMA_PENDIENTE;
        } else if (estado.equalsIgnoreCase(codRechazado)
                || estado.equalsIgnoreCase(codCaducado)
                || estado.equalsIgnoreCase(codRetirado)) {
            codFlexia = ConstantesPortafirmas.ESTADO_FIRMA_RECHAZADA;
        }
                
        return codFlexia;
    }
    
    /**
     * Para los estados propios del firmante del portafirmas del externo
     * devuelve el codigo equivalente para flexia
     *
     * @param estado
     * @return
     */
    public static String mapeoEstadoFirmante(String estado) {
        String codFlexia = "";

        String codFirmado = getPropiedadSanse(ConstantesPortafirmasSanse.PROPERTIES_ESTADO_FIRMANTE_FIRMADO);
        String codRechazado = getPropiedadSanse(ConstantesPortafirmasSanse.PROPERTIES_ESTADO_FIRMANTE_RECHAZADO);
        String codCaducado = getPropiedadSanse(ConstantesPortafirmasSanse.PROPERTIES_ESTADO_FIRMANTE_CADUCADO);
        String codRetirado = getPropiedadSanse(ConstantesPortafirmasSanse.PROPERTIES_ESTADO_FIRMANTE_RETIRADO);
        String codNuevo = getPropiedadSanse(ConstantesPortafirmasSanse.PROPERTIES_ESTADO_FIRMANTE_NUEVO);
        String codLeido = getPropiedadSanse(ConstantesPortafirmasSanse.PROPERTIES_ESTADO_FIRMANTE_LEIDO);
        String codEnEspera = getPropiedadSanse(ConstantesPortafirmasSanse.PROPERTIES_ESTADO_FIRMANTE_EN_ESPERA);
        
        if (estado.equalsIgnoreCase(codFirmado)) {
            codFlexia = ConstantesPortafirmas.ESTADO_FIRMA_FIRMADA;
        } else if (estado.equalsIgnoreCase(codNuevo)
                || estado.equalsIgnoreCase(codLeido)
                || estado.equalsIgnoreCase(codEnEspera)) {
            codFlexia = ConstantesPortafirmas.ESTADO_FIRMA_PENDIENTE;
        } else if (estado.equalsIgnoreCase(codRechazado)
                || estado.equalsIgnoreCase(codCaducado)
                || estado.equalsIgnoreCase(codRetirado)) {
            codFlexia = ConstantesPortafirmas.ESTADO_FIRMA_RECHAZADA;
        }

        return codFlexia;
    }
    
    /**
     * Rellena los parametros base de las peticiones a los web service de
     * ModifyService
     *
     * @param codOrganizacion
     * @param params
     * @throws TechnicalException
     */
    public static void rellenarBaseParams(String codOrganizacion, ModifyServiceBaseParams params) throws TechnicalException {
        if (params != null) {
            try {
                String endpoint = SanseUtils.getPropiedadSanse(
                            codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_MODIFYSERVICE_URL);
                String user = SanseUtils.getPropiedadSanse(
                            codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_MODIFYSERVICE_USER);
                String password = SanseUtils.getPropiedadSanse(
                            codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_MODIFYSERVICE_PASS);
                
                params.setCodOrganizacion(NumberUtils.createInteger(codOrganizacion));
                params.setEndPoint(endpoint);
                params.setUsername(user);
                params.setPassword(password);
            } catch (Exception ex) {
                throw new TechnicalException(String.format("Se ha producido un error al obtener las propiedades: %s", ex.getMessage()));
            }
        }
    }

    /**
     * Rellena los parametros base de las peticiones a los web service de
     * QueryService
     *
     * @param codOrganizacion
     * @param params
     * @throws TechnicalException
     */
    public static void rellenarBaseParams(String codOrganizacion, QueryServiceBaseParams params) throws TechnicalException {
        if (params != null) {
            try {
                String endpoint = SanseUtils.getPropiedadSanse(
                            codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_QUERYSERVICE_URL);
                String user = SanseUtils.getPropiedadSanse(
                            codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_QUERYSERVICE_USER);
                String password = SanseUtils.getPropiedadSanse(
                            codOrganizacion, ConstantesPortafirmasSanse.PROPERTIES_QUERYSERVICE_PASS);

                params.setCodOrganizacion(NumberUtils.createInteger(codOrganizacion));
                params.setEndPoint(endpoint);
                params.setUsername(user);
                params.setPassword(password);
            } catch (Exception ex) {
                throw new TechnicalException(String.format("Se ha producido un error al obtener las propiedades: %s", ex.getMessage()));
            }
        }
    }
}
