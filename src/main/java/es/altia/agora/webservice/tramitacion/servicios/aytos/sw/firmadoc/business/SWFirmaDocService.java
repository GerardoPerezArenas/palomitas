package es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.business;

import es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.SWFirmadoc;
import es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.SWFirmadocServiceLocator;
import es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.SWFirmadoc_ParametrosBean;
import es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.SWFirmadoc_RetornoBean;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.agora.business.util.GeneralValueObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.rpc.ServiceException;
import java.util.ResourceBundle;
import java.net.URL;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class SWFirmaDocService {

    private SWFirmadoc clienteFirmaDoc;

    private static Log m_Log = LogFactory.getLog(SWFirmaDocService.class.getName());
    private static ResourceBundle m_ct = ResourceBundle.getBundle(
            "es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.configuracion.configuracion");

    private static SWFirmaDocService instance;

    protected SWFirmaDocService() {

        try {

            URL url = new URL(m_ct.getString("url"));
            SWFirmadocServiceLocator locator = new SWFirmadocServiceLocator();
            clienteFirmaDoc = locator.getfirmadoc(url);

        } catch (MalformedURLException e) {
            m_Log.error("SW de FirmaDoc: Error en la dirección de la URL del Servicio Web");
            e.printStackTrace();
        } catch (ServiceException e) {
            m_Log.error("SW de Firmadoc: Error al intentar obtener el Servicio Web");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static SWFirmaDocService getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(SWFirmaDocManager.class) {
                if (instance == null)
                    instance = new SWFirmaDocService();
            }
        }
        return instance;
    }


    /**
     * Almacena un documento en el Servicio Web de Firmadoc.
     *
     * @param base64 Codificacion del fichero en base64.
     * @param nombreDocumento Nombre del fichero a almacenar.
     * @param tipoDocumento Tipo nemotecnico del documento.
     * @param claves Claves identificativas externas.
     * @return String Codigo del documento asignado por firmadoc.
     * @throws es.altia.agora.webservice.tramitacion.servicios.WSException
     * Excepcion conteniendo el mensaje de fallo de la llamada al Servicio Web.
     */
    public String almacenarFichero(String base64, String nombreDocumento, String tipoDocumento,
                                   String[] claves) throws WSException {

        try {
            // Validamos parametros de entrada obligatorios.
            if ((base64 == null) || (nombreDocumento == null)) {
                m_Log.error("Parametro obligatorio a null");
                throw new WSException("WebService Exception: Parametro obligatorio con valor null");
            }

            // Rellenamos el bean con los parametros de entrada.
            SWFirmadoc_ParametrosBean parametros = new SWFirmadoc_ParametrosBean();
            parametros.setBase64(base64);
            parametros.setNombre_documento(nombreDocumento);
            parametros.setTipo_documento(tipoDocumento);
            int i = 0;
            if (claves != null) {
                while ((i < 4) && (i < claves.length)) {
                    switch(i) {
                        case 0: parametros.setClave_1(claves[0]);
                        case 1: parametros.setClave_2(claves[1]);
                        case 2: parametros.setClave_3(claves[2]);
                        case 3: parametros.setClave_4(claves[3]);
                    }
                }
            }

            // Llamada al Servicio Web.
            SWFirmadoc_RetornoBean retorno = clienteFirmaDoc.SWFirmadoc(1, parametros);

            // Devolvemos el resultado.
            if (!retorno.isResultado()) {
                m_Log.error("Fallo en el resultado de la llamada al Servicio Web");
                throw new WSException("WebService Exception: " + retorno.getTextoError());
            }
            return retorno.getCodigo_documento();

        } catch (RemoteException re) {
            re.printStackTrace();
            throw new WSException("WebService Exception:" + re.getMessage());
        }
    }


    /**
     * Extrae el contenido de un documento almacenado en el Servicio Web de Firmadoc
     *
     * @param codigoDocumento Codigo del documento (en firmadoc) a extraer.
     * @return String Resultado de codificar en base64 el documento a extraer.
     * @throws es.altia.agora.webservice.tramitacion.servicios.WSException
     * Excepcion conteniendo el mensaje de fallo de la llamada al Servicio Web.
     */
    public String extraerFichero(String codigoDocumento) throws WSException {

        try {

            // Validar los parametros de entrada.
            if (codigoDocumento == null) {
                m_Log.error("Parametro obligatorio a null");
                throw new WSException("WebService Exception: Parametro obligatorio con valor null");
            }

            // Rellenar parametros de entrada.
            SWFirmadoc_ParametrosBean parametros = new SWFirmadoc_ParametrosBean();
            parametros.setCodigo_documento(codigoDocumento);

            // Llamada al Servicio Web.
            SWFirmadoc_RetornoBean retorno = clienteFirmaDoc.SWFirmadoc(2, parametros);

            // Devolver el valor adecuado.
            if (!retorno.isResultado()) {
                m_Log.error("Fallo en el resultado de la llamada al Servicio Web");
                throw new WSException("WebService Exception: " + retorno.getTextoError());
            }
            return retorno.getBase64();

        } catch (RemoteException re) {
            re.printStackTrace();
            throw new WSException("WebService Exception:" + re.getMessage());
        }
    }


    /**
     * Crea un expediente en el Servicio Web de firmadoc.
     *
     * @param nombreExpediente Descripcion del expediente a crear. Obligatorio.
     * @param expediente El codigo alfanumerico del expediente a almacenar. Obligatorio.
     * @param ejercicio Ejercicio en el que se creara el expediente. Obligatorio.
     * @param tipoExpediente El tipo nemotecnico del expediente a crear. Obligatorio.
     * @param procedimiento Descripcion del procedimiento. Obligatorio.
     * @return String
     * @throws es.altia.agora.webservice.tramitacion.servicios.WSException
     * Excepcion conteniendo el mensaje de fallo de la llamada al Servicio Web.
     */
    public String crearExpediente(String nombreExpediente, String expediente, String ejercicio,
                                  String tipoExpediente, String procedimiento) throws WSException {

        try {

            // Validar los parametros de entrada.
            m_Log.debug("nombreExpediente:" + nombreExpediente + " | expediente:" + expediente +
                    " | ejercicio:" + ejercicio + " | tipoExpediente:" + tipoExpediente +
                    " | procedimiento");
            if ((nombreExpediente == null) || (expediente == null) || (ejercicio == null) ||
                    (tipoExpediente == null) || (procedimiento == null)) {
                m_Log.error("Parametro obligatorio a null");
                throw new WSException("WebService Exception: Parametro obligatorio con valor null");
            }

            //Rellenar parametros de entrada
            SWFirmadoc_ParametrosBean parametros = new SWFirmadoc_ParametrosBean();
            parametros.setNombre_expediente(nombreExpediente);
            parametros.setExpediente(expediente);
            parametros.setEjercicio(ejercicio);
            parametros.setTipo_expediente(tipoExpediente);
            parametros.setProcedimiento(procedimiento);

            // Llamada al Servicio Web.
            SWFirmadoc_RetornoBean retorno = clienteFirmaDoc.SWFirmadoc(3, parametros);

            // Devolver el valor adecuado.
            if (!retorno.isResultado()) {
                m_Log.error("Fallo en el resultado de la llamada al Servicio Web");
                if (retorno.getError() == 2) {
                    return expediente;
                }
                throw new WSException("WebService Exception: " + retorno.getTextoError());
            }
            return retorno.getCodigo_expediente();

        } catch (RemoteException re) {
            re.printStackTrace();
            throw new WSException("WebService Exception:" + re.getMessage());
        }
    }


    /**
     * Asocia un documento a un expediente.
     *
     * @param codigoDocumento Codigo del documento a asociar (en firmadoc).
     * @param codigoExpediente Codigo del expediente al que asociar el documento (en firmadoc).
     * @throws es.altia.agora.webservice.tramitacion.servicios.WSException
     * Excepcion conteniendo el mensaje de fallo de la llamada al Servicio Web.
     */
    public void asociarDocumentoAExpediente(String codigoDocumento, String codigoExpediente)
    throws WSException {

        try {
            // Rellenamos parametrs de entrada
            SWFirmadoc_ParametrosBean parametros = new SWFirmadoc_ParametrosBean();
            parametros.setCodigo_documento(codigoDocumento);
            parametros.setCodigo_expediente(codigoExpediente);

            // Llamada al Servicio Web.
            SWFirmadoc_RetornoBean retorno = clienteFirmaDoc.SWFirmadoc(4, parametros);

            // Comprobamos si la llamada al Servicio Web se realizo correctamente.
            if (!retorno.isResultado()) {
                m_Log.error("Fallo en el resultado de la llamada al Servicio Web");
                throw new WSException("WebService Exception: " + retorno.getTextoError());
            }

        } catch (RemoteException re) {
            re.printStackTrace();
            throw new WSException("WebService Exception:" + re.getMessage());
        }
    }


    /**
    * Borra un documento.
    *
    * @param codigoDocumento Codigo del documento a borrar (en firmadoc). Obligatorio.
    * @throws es.altia.agora.webservice.tramitacion.servicios.WSException
    * Excepcion conteniendo el mensaje de fallo de la llamada al Servicio Web.
    */
    public void borrarDocumento(String codigoDocumento) throws WSException {

        try {
            // Validamos los parametros de entrada.
            if (codigoDocumento == null) {
                m_Log.error("Parametro obligatorio a null");
                throw new WSException("WebService Exception: Parametro obligatorio con valor null");
            }

            // Rellanamos el bean con los parametros de entrada.
            SWFirmadoc_ParametrosBean parametros = new SWFirmadoc_ParametrosBean();
            parametros.setCodigo_documento(codigoDocumento);

            // Llamada al Servicio Web.
            SWFirmadoc_RetornoBean retorno = clienteFirmaDoc.SWFirmadoc(5, parametros);

            // Comprobamos si la llamada al Servicio Web se realizo correctamente.
            if (!retorno.isResultado()) {
                m_Log.error("Fallo en el resultado de la llamada al Servicio Web");
                throw new WSException("WebService Exception: " + retorno.getTextoError());
            }

        } catch (RemoteException re) {
            re.printStackTrace();
            throw new WSException("WebService Exception:" + re.getMessage());
        }
    }


    /**
     * Consulta el estado de la firma de un documento.
     *
     * @param codigoDocumento Codigo del documento a consultar (en firmadoc). Obligatorio.
     * @return String Numero con valores dependiendo del estado de la firma del documento.
     * @throws es.altia.agora.webservice.tramitacion.servicios.WSException
     * Excepcion conteniendo el mensaje de fallo de la llamada al Servicio Web.
     */
    public String consultarEstadoFirma(String codigoDocumento) throws WSException {

        try {

            // Validar los parametros de entrada.
            if (codigoDocumento == null) {
                m_Log.error("Parametro obligatorio a null");
                throw new WSException("WebService Exception: Parametro obligatorio con valor null");
            }

            //Rellenar parametros de entrada
            SWFirmadoc_ParametrosBean parametros = new SWFirmadoc_ParametrosBean();
            parametros.setCodigo_documento(codigoDocumento);

            // Llamada al Servicio Web.
            SWFirmadoc_RetornoBean retorno = clienteFirmaDoc.SWFirmadoc(6, parametros);

            // Devolver el valor adecuado.
            if (!retorno.isResultado()) {
                m_Log.error("Fallo en el resultado de la llamada al Servicio Web");
                throw new WSException("WebService Exception: " + retorno.getTextoError());
            }
            return retorno.getEstado_firma();

        } catch (RemoteException re) {
            re.printStackTrace();
            throw new WSException("WebService Exception:" + re.getMessage());
        }
    }


    /**
     * Consulta el estado de la firma de un documento.
     *
     * @param gvoEntrada GeneralValueObject conteniendo todos los posibles valores de entrada. Obligatorio.
     * @return GeneralValueObject Bean conteninedo el codigo del documento asignado
     * y el codigo del expediente agsigado.
     * @throws es.altia.agora.webservice.tramitacion.servicios.WSException
     * Excepcion conteniendo el mensaje de fallo de la llamada al Servicio Web.
     */
    public GeneralValueObject gestionCompleta(GeneralValueObject gvoEntrada)
    throws WSException {

        try {
            // Validamos los parametros de entrada.
            if (gvoEntrada == null) {
                m_Log.error("Parametro obligatorio a null");
                throw new WSException("WebService Exception: Parametro obligatorio con valor null");
            }

            // Rellenar los valores de entrada
            SWFirmadoc_ParametrosBean parametros = new SWFirmadoc_ParametrosBean();
            parametros.setBase64((String)gvoEntrada.getAtributo("base64"));
            m_Log.debug(gvoEntrada.getAtributo("base64"));
            parametros.setNombre_documento((String)gvoEntrada.getAtributo("nombreDocumento"));
            m_Log.debug(gvoEntrada.getAtributo("nombreDocumento"));
            parametros.setTipo_documento((String)gvoEntrada.getAtributo("tipoDocumento"));
            m_Log.debug(gvoEntrada.getAtributo("tipoDocumento"));
            parametros.setClave_1((String)gvoEntrada.getAtributo("clave1"));
            parametros.setClave_2((String)gvoEntrada.getAtributo("clave2"));
            parametros.setClave_3((String)gvoEntrada.getAtributo("clave3"));
            parametros.setClave_4((String)gvoEntrada.getAtributo("clave4"));
            //if (gvoEntrada.getAtributo("asuntoExpediente") != null)
              //  if (!gvoEntrada.getAtributo("asuntoExpediente").equals(""))
                    parametros.setNombre_expediente((String)gvoEntrada.getAtributo("asuntoExpediente"));
            //else
              //  parametros.setNombre_expediente("HOLA");
            m_Log.debug(gvoEntrada.getAtributo("asuntoExpediente"));
            parametros.setExpediente((String)gvoEntrada.getAtributo("numeroExpediente"));
            m_Log.debug(gvoEntrada.getAtributo("numeroExpediente"));
            parametros.setEjercicio((String)gvoEntrada.getAtributo("ejercicio"));
            m_Log.debug(gvoEntrada.getAtributo("ejercicio"));
            parametros.setTipo_expediente((String)gvoEntrada.getAtributo("codProcedimiento"));
            m_Log.debug(gvoEntrada.getAtributo("codProcedimiento"));
            parametros.setProcedimiento((String)gvoEntrada.getAtributo("descProcedimiento"));
            m_Log.debug(gvoEntrada.getAtributo("descProcedimiento"));

            // Llamada al Servicio Web.
            SWFirmadoc_RetornoBean retorno = clienteFirmaDoc.SWFirmadoc(7, parametros);

            // Devolver el valor adecuado.
            if (!retorno.isResultado()) {
                m_Log.error("Fallo en el resultado de la llamada al Servicio Web");
                throw new WSException(retorno.getTextoError());
            }

            GeneralValueObject gvoResultado = new GeneralValueObject();
            gvoResultado.setAtributo("codExpFirmaDoc", retorno.getCodigo_expediente());
            gvoResultado.setAtributo("codDocFirmaDoc", retorno.getCodigo_documento());

            return gvoResultado;

        } catch (RemoteException re) {
            re.printStackTrace();
            throw new WSException("ERROR al llamar al Servicio Web de FirmaDoc.", re);
        }      
    }

}
