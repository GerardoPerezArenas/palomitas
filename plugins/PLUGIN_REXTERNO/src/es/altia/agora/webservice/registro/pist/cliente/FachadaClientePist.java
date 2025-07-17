package es.altia.agora.webservice.registro.pist.cliente;

import es.altia.agora.webservice.registro.pist.cliente.datos.ReturnSWPistVO;
import es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationInfoVO;
import es.altia.agora.webservice.registro.pist.cliente.datos.SearchAnnotationInfoVO;
import es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationPersonInfoVO;
import es.altia.agora.webservice.registro.pist.cliente.servicio.WSPistImplServiceLocator;
import es.altia.agora.webservice.registro.pist.cliente.servicio.WSPistImpl;
import es.altia.agora.webservice.registro.exceptions.RegistroException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import javax.xml.rpc.ServiceException;
import java.util.Calendar;
import java.rmi.RemoteException;
import java.net.URL;
import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FachadaClientePist {

    private static Log m_Log = LogFactory.getLog(FachadaClientePist.class.getName());
    private static FachadaClientePist instance;
    private String prefijoPropiedad;

    protected FachadaClientePist() throws RegistroException {}

    public String getPrefijoPropiedad() {
        return prefijoPropiedad;
    }

    public void setPrefijoPropiedad(String prefijoPropiedad) {
        this.prefijoPropiedad = prefijoPropiedad;
    }

    private WSPistImpl getClienteSW() throws RegistroException {

        try {
            Config m_ConfigRegistro = ConfigServiceHelper.getConfig("Registro");
            String strUrlEndPoint = m_ConfigRegistro.getString(prefijoPropiedad + "urlEndPoint");
            URL urlEndPoint = new URL(strUrlEndPoint);
            WSPistImplServiceLocator locator = new WSPistImplServiceLocator();
            return locator.getWSPistImpl(urlEndPoint);
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

    public static FachadaClientePist getInstance() throws RegistroException {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(FachadaClientePist.class) {
                if (instance == null)
                    instance = new FachadaClientePist();
            }
        }
        return instance;
    }

    public AnnotationInfoVO[] getAnnotations(Calendar soonerDate, Calendar laterDate, String state, String[] groupingNames, String annotationType)
            throws RegistroException {

        try {
            WSPistImpl pistCliente = getClienteSW();
            ReturnSWPistVO retorno = pistCliente.getAnnotations(soonerDate, laterDate, state, groupingNames, annotationType);
            if (retorno.getStatus() == 0) return retorno.getFindedAnnotations();
            else throw new RegistroException(new Exception(), "(" + retorno.getStatus() + ") " + retorno.getDescStatus());
        } catch (RemoteException re) {
            if (m_Log.isErrorEnabled()) m_Log.error("SE HA PRODUCIDO UN ERROR DURANTE LA CONEXION CON EL SERVICIO WEB");
            re.printStackTrace();
            throw new RegistroException(re, "NO SE HA PODIDO REALIZAR LA CONSULTA EN ESTE MOMENTO");
        }
    }


    public AnnotationInfoVO getAnnotationInfo(String numAnotacion, String tipoReg) throws RegistroException {
        try {
            WSPistImpl pistCliente = getClienteSW();
            ReturnSWPistVO retorno = pistCliente.getAnnotationInfo(numAnotacion, tipoReg);
            if (retorno.getStatus() == 0) return retorno.getAnnotationInfo();
            else throw new RegistroException(new Exception(), "(" + retorno.getStatus() + ") " + retorno.getDescStatus());
        } catch (RemoteException re) {
            if (m_Log.isErrorEnabled()) m_Log.error("SE HA PRODUCIDO UN ERROR DURANTE LA CONEXION CON EL SERVICIO WEB");
            re.printStackTrace();
            throw new RegistroException(re, "NO SE HA PODIDO REALIZAR LA CONSULTA EN ESTE MOMENTO");
        }
    }

    public void manageAnnotations(String numAnotacion, String tipoReg, String annotationState) throws RegistroException {
        try {
            WSPistImpl pistCliente = getClienteSW();
            ReturnSWPistVO retorno = pistCliente.manageAnnotations(numAnotacion, tipoReg, annotationState);
            if (retorno.getStatus() != 0)
                throw new RegistroException(new Exception(), "(" + retorno.getStatus() + ") " + retorno.getDescStatus());
        } catch (RemoteException re) {
            if (m_Log.isErrorEnabled()) m_Log.error("SE HA PRODUCIDO UN ERROR DURANTE LA CONEXION CON EL SERVICIO WEB");
            re.printStackTrace();
            throw new RegistroException(re, "NO SE HA PODIDO REALIZAR LA CONSULTA EN ESTE MOMENTO");
        }
    }

    public AnnotationInfoVO[] getAnnotationsByNumbers(SearchAnnotationInfoVO[] annotInfoVOs) throws RegistroException {
        try {
            WSPistImpl pistCliente = getClienteSW();
            ReturnSWPistVO retorno = pistCliente.getAnnotationsByNumbers(annotInfoVOs);
            if (retorno.getStatus() == 0) return retorno.getFindedAnnotations();
            else throw new RegistroException(new Exception(), "(" + retorno.getStatus() + ") " + retorno.getDescStatus());
        } catch (RemoteException re) {
            if (m_Log.isErrorEnabled()) m_Log.error("SE HA PRODUCIDO UN ERROR DURANTE LA CONEXION CON EL SERVICIO WEB");
            re.printStackTrace();
            throw new RegistroException(re, "NO SE HA PODIDO REALIZAR LA CONSULTA EN ESTE MOMENTO");
        }
    }

    public AnnotationPersonInfoVO getAnnotationPersonInfo(SearchAnnotationInfoVO searchInfo) throws RegistroException {
        try {
            WSPistImpl pistCliente = getClienteSW();
            ReturnSWPistVO retorno = pistCliente.getAnnotationInterested(searchInfo);
            if (retorno.getStatus() == 0) return retorno.getPersonInfo();
            else throw new RegistroException(new Exception(), "(" + retorno.getStatus() + ") " + retorno.getDescStatus());
        } catch (RemoteException re) {
            if (m_Log.isErrorEnabled()) m_Log.error("SE HA PRODUCIDO UN ERROR DURANTE LA CONEXION CON EL SERVICIO WEB");
            re.printStackTrace();
            throw new RegistroException(re, "NO SE HA PODIDO REALIZAR LA CONSULTA EN ESTE MOMENTO");
        }
    }
        
}
