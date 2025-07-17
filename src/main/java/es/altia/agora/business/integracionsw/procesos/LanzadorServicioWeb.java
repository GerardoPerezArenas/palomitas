package es.altia.agora.business.integracionsw.procesos;

import es.altia.agora.business.integracionsw.*;
import es.altia.agora.business.integracionsw.procesos.constructor.ConstructorMensaje;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.util.exceptions.InternalErrorException;
import org.apache.axis.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class LanzadorServicioWeb {


    protected static Log m_Log = LogFactory.getLog(LanzadorServicioWeb.class.getName());

    private InfoServicioWebVO infoSW;

    public LanzadorServicioWeb(InfoServicioWebVO infoSW) {
        this.infoSW = infoSW;
    }

    public TipoServicioWebVO llamarServicioWeb(String nombreOp, String[] params) throws EjecucionSWException {

        OperacionServicioWebVO operacion = getOperacionByName(nombreOp);

        try {
            // Crear el objeto a traves del cual se invocara la llamada al Servicio Web.
            ConstructorMensaje constructor = new ConstructorMensaje(infoSW.getCodigoSW(), params);

            return constructor.callWebService(infoSW, operacion);

        } catch (MalformedURLException mue) {
            throw new EjecucionSWException(mue, "URL DE ACCESO AL SERVICIO WEB NO ES CORRECTA");
        } catch (AxisFault af) {
            af.printStackTrace();
            throw new EjecucionSWException(af, "HA FALLADO LA LLAMADA AL SERVICIO WEB");
        } catch (RemoteException re) {
            re.printStackTrace();
            throw new EjecucionSWException(re, "HA OCURRIDO UN ERROR MIENTRAS SE ANALIZABAN LOS DATOS DE ENTRADA");
        } catch (InternalErrorException iee) {
            iee.printStackTrace();
            throw new EjecucionSWException(iee, "HA OCURRIDO UN ERROR MIENTRAS SE ANALIZABAN LOS PARAMETROS DE ENTRADA");
        }
    }

    private OperacionServicioWebVO getOperacionByName(String nombreOp) {

        for (Object o : infoSW.getOperacionesSW()) {
            OperacionServicioWebVO operacion = (OperacionServicioWebVO) o;
            if (nombreOp.equals(operacion.getNombreOperacion())) return operacion;
        }
        return null;
    }

}
