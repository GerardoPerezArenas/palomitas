package es.altia.agora.webservice.registro.pisa.cliente;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.webservice.registro.exceptions.RegistroException;
import es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_AnotacionesBean;
import es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_ParametrosBean;
import es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_RetornoBean;
import es.altia.agora.webservice.registro.pisa.cliente.servicio.SWPisa;
import es.altia.agora.webservice.registro.pisa.cliente.servicio.SWPisaServiceLocator;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

public class FachadaClientePisa {

    private SWPisa pisaCliente;
    private static Log m_Log = LogFactory.getLog(FachadaClientePisa.class.getName());
    private static FachadaClientePisa instance;


    protected FachadaClientePisa(String prefijoPropiedad) throws RegistroException {

        try {
            Config m_ConfigRegistro = ConfigServiceHelper.getConfig("Registro");
            String strUrlEndPoint = m_ConfigRegistro.getString(prefijoPropiedad + "urlEndPoint");
            URL urlEndPoint = new URL(strUrlEndPoint);
            SWPisaServiceLocator locator = new SWPisaServiceLocator();
            pisaCliente = locator.getPisa(urlEndPoint);
        } catch (ServiceException se) {
            if (m_Log.isErrorEnabled()) m_Log.error("SE HA PRODUCIDO UN ERROR AL INTENTAR CONECTARSE AL SERVICIO WEB");
            se.printStackTrace();
            throw new RegistroException(se, "NO SE PUDO OBTENER LA CONEXION AL SERVICIO WEB");
        } catch (MalformedURLException mue) {
            if (m_Log.isErrorEnabled()) m_Log.error("SE HA PRODUCIDO UN ERROR AL INTENTAR CONECTARSE AL SERVICIO WEB");
            mue.printStackTrace();
            throw new RegistroException(mue, "NO SE PUDO OBTENER LA CONEXION AL SERVICIO WEB");
        }
    }

    public static FachadaClientePisa getInstance(String prefijoPropiedad) throws RegistroException {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(FachadaClientePisa.class) {
                if (instance == null)
                    instance = new FachadaClientePisa(prefijoPropiedad);
            }
        }
        return instance;
    }

    /**
     * Permite buscar las entradas según sus propiedades
     *
     * @param dato: Bean con los parametros de la llamada al SW de Pisa.
     * @return Array de SWPisa_AnotacionesBean con todas las anotaciones encontradas por el servicio
     * @throws RegistroException En caso de error en las llamadas al servicio
     */
    public SWPisa_AnotacionesBean[] buscarEntradas(SWPisa_ParametrosBean dato) throws RegistroException {

        try {
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Entidad " + dato.getEntidad());
                m_Log.debug("Organizacion " + dato.getOrganizacion());
                m_Log.debug("Fecha desde " + dato.getFechaDesde());
                m_Log.debug("Fecha hasta " + dato.getFechaHasta());
                m_Log.debug("Ejercicio " + dato.getEjercicio());
                m_Log.debug("Tipo " + dato.getTipo());
                m_Log.debug("Estado " + dato.getEstado());
                m_Log.debug("Expediente " + dato.getExpediente());
                m_Log.debug("Unidades " + dato.getUnidad());
                m_Log.debug("Numeros " + dato.getNumeros());
            }

            if (m_Log.isInfoEnabled()) m_Log.info("Conectando con el servicio web");
            SWPisa_RetornoBean retorno = pisaCliente.SWPisa(1, dato);

            if (retorno.isResultado()) {
                if (m_Log.isInfoEnabled()) m_Log.info("Se obtiene un resultado del servicio web correcto");
                return retorno.getAnotaciones();
            } else {
                if (m_Log.isInfoEnabled()) m_Log.info("Se obtiene un resultado del servicio web incorrecto");
                throw new RegistroException(new Exception(), "(" + retorno.getError() + ") " + retorno.getTextoError().toUpperCase());
            }

        } catch (RemoteException re) {
            if (m_Log.isErrorEnabled()) m_Log.error("SE HA PRODUCIDO UN ERROR DURANTE LA CONEXION CON EL SERVICIO WEB");
            re.printStackTrace();
            throw new RegistroException(re, "NO SE HA PODIDO REALIZAR LA CONSULTA EN ESTE MOMENTO");
        }
    }


    /**
     * Permite asignar un expediente a una anotación
     *
     * @param dato: Bean con los parametros de la llamada al SW de Pisa.
     * @throws RegistroException En caso de error en las llamadas al servicio
     */
    public void asignarExpediente(SWPisa_ParametrosBean dato) throws RegistroException {

        try {
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Entidad " + dato.getEntidad());
                m_Log.debug("Organizacion " + dato.getOrganizacion());
                m_Log.debug("Tipo " + dato.getAnotacion().getTipo());
                m_Log.debug("Ejercicio " + dato.getAnotacion().getEjercicio());
                m_Log.debug("Numero " + dato.getAnotacion().getNumero());
                m_Log.debug("Expediente " + dato.getAnotacion().getExpediente());
                m_Log.debug("Estado " + dato.getAnotacion().getEstado());
            }

            if (m_Log.isInfoEnabled()) m_Log.info("Conectando con el servicio web");

            SWPisa_RetornoBean retorno = pisaCliente.SWPisa(6, dato);

            if (!retorno.isResultado()) {
                if (m_Log.isInfoEnabled()) m_Log.info("Se obtiene un resultado del servicio web incorrecto");
                throw new RegistroException(new Exception(), "(" + retorno.getError() + ") " + retorno.getTextoError().toUpperCase());
            }
        } catch (RemoteException re) {
            if (m_Log.isErrorEnabled()) m_Log.error("SE HA PRODUCIDO UN ERROR DURANTE LA CONEXION CON EL SERVICIO WEB");
            re.printStackTrace();
            throw new RegistroException(re, "NO SE HA PODIDO REALIZAR LA CONSULTA EN ESTE MOMENTO");
        }
    } 
    
        /**
     * Da de alta una anotacion
     *
     * @param dato: Bean con los parametros de la llamada al SW de Pisa.
     * @throws RegistroException En caso de error en las llamadas al servicio
     */
    public SWPisa_RetornoBean gestionarEntradas(SWPisa_ParametrosBean dato) throws RegistroException {

        try {
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Entidad " + dato.getEntidad());
                m_Log.debug("Organizacion " + dato.getOrganizacion());
                m_Log.debug("Tipo " + dato.getAnotacion().getTipo());
                m_Log.debug("Asunto " + dato.getAnotacion().getAsunto());
                m_Log.debug("Unidad Organica " + dato.getUnidad());
                m_Log.debug("Ejercicio " + dato.getAnotacion().getEjercicio());
                
                m_Log.debug("Tipo Documento Interesado " + dato.getAnotacion().getInteresados()[0].getCodigo_tipo_documento());
                m_Log.debug("Documento Interesado " + dato.getAnotacion().getInteresados()[0].getDocumento());
                m_Log.debug("Nombre Interesado " + dato.getAnotacion().getInteresados()[0].getNombre());
                m_Log.debug("DomicilioInteresado " + dato.getAnotacion().getInteresados()[0].getDomicilio());
  
            }

            if (m_Log.isInfoEnabled()) m_Log.info("Conectando con el servicio web");

            SWPisa_RetornoBean retorno = pisaCliente.SWPisa(2, dato);

            if (!retorno.isResultado()) {
                if (m_Log.isInfoEnabled()) m_Log.info("Se obtiene un resultado del servicio web incorrecto");
                throw new RegistroException(new Exception(), "(" + retorno.getError() + ") " + retorno.getTextoError().toUpperCase());
            }
            return retorno;
        } catch (RemoteException re) {
            if (m_Log.isErrorEnabled()) m_Log.error("SE HA PRODUCIDO UN ERROR DURANTE LA CONEXION CON EL SERVICIO WEB");
            re.printStackTrace();
            throw new RegistroException(re, "NO SE HA PODIDO REALIZAR LA CONSULTA EN ESTE MOMENTO");
        }
    } 
}
