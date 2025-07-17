package es.altia.agora.business.integracionsw.procesos.constructor;

import es.altia.agora.business.integracionsw.*;
import es.altia.agora.business.integracionsw.persistence.ReconstruccionSWManager;
import es.altia.agora.business.integracionsw.exception.TipoNoValidoException;
import es.altia.util.exceptions.InternalErrorException;

import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.Collection;
import java.util.Enumeration;
import java.util.ArrayList;

import org.apache.axis.client.Stub;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.NoEndPointException;
import org.apache.axis.AxisEngine;
import org.apache.axis.Constants;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.soap.SOAPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;

public class ConstructorMensaje extends Stub {

    protected static Log m_Log = LogFactory.getLog(ConstructorMensaje.class.getName());

    private Vector<Class> cachedSerClasses = new Vector<Class>();
    private Vector<QName> cachedSerQNames = new Vector<QName>();

    private static Vector<Object> values;

    private int codigoSW;
    private String[] paramsBD;

    public ConstructorMensaje(int codigoSW, String[] params) {
        this.codigoSW = codigoSW;
        this.paramsBD = params;
    }

    public TipoServicioWebVO callWebService(InfoServicioWebVO infoSW, OperacionServicioWebVO operacion)
            throws RemoteException, MalformedURLException, InternalErrorException {

        try {

            values = new Vector<Object>();

            if (infoSW.getUrlAccesoSW() == null) throw new NoEndPointException();
            else super.cachedEndpoint = new URL(infoSW.getUrlAccesoSW());

            analizarTipos(operacion.getNamespace());

            Call llamada = crearLlamada();
            llamada.setOperation(crearOperationDesc(operacion));
            llamada.setUseSOAPAction(true);
            llamada.setSOAPActionURI(operacion.getSoapActionUri());
            if (operacion.getOpStyle().equals("wrapped")) {
                llamada.setEncodingStyle(null);
                llamada.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
                llamada.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
            }
            llamada.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
            llamada.setOperationName(new QName(operacion.getNamespace(), operacion.getNombreOperacion()));

            setRequestHeaders(llamada);
            setAttachments(llamada);

            m_Log.info("Se llama al endpoint: " + llamada.getTargetEndpointAddress());
            m_Log.debug("Valores para la llamada: " + values);
            Object respuesta = llamada.invoke(values.toArray());
            m_Log.info("(No error) LA RESPUESTA OBTENIDA HA SIDO: " + respuesta);

            if (respuesta instanceof RemoteException) throw (RemoteException)respuesta;
            if (respuesta instanceof Collection) {
                Collection arrayRespuesta = (Collection)respuesta;
                TipoArrayVO tipoRespArray;
                QName returnType = llamada.getOperation().getReturnType();
                if (operacion.getOpStyle().equals("wrapped")) {
                    tipoRespArray = new TipoArrayVO(llamada.getOperation().getReturnQName().getLocalPart());
                } else {
                    tipoRespArray = new TipoArrayVO(returnType.getLocalPart());
                }
                TipoServicioWebVO tipoContRetorno = ((TipoArrayVO)operacion.getSalida().getTipoParametro()).getTipoContenido();
                if (returnType.getNamespaceURI().equals(Constants.URI_2001_SCHEMA_XSD) || tipoContRetorno.esTipoBase()) {
                    // Array de tipos basicos.
                    for (Object objTipoBasico: arrayRespuesta) {
                        TipoBasicoVO tipoContenido = new TipoBasicoVO(tipoContRetorno.getNombreTipo());
                        tipoContenido.setValor(objTipoBasico, tipoContRetorno.getNombreTipo());
                        if (tipoRespArray.getTipoContenido() == null) tipoRespArray.setTipoContenido(tipoContenido);
                        if (tipoRespArray.getArray() == null) tipoRespArray.setArray(new Vector<TipoServicioWebVO>());
                        tipoRespArray.getArray().add(tipoContenido);
                    }
                } else {
                    for (Object objTipo: arrayRespuesta) {
                        TipoServicioWebVO tipoContenido = (TipoServicioWebVO)objTipo;
                        if (tipoRespArray.getTipoContenido() == null) tipoRespArray.setTipoContenido(tipoContenido);
                        if (tipoRespArray.getArray() == null) tipoRespArray.setArray(new Vector<TipoServicioWebVO>());
                        tipoRespArray.getArray().add(tipoContenido);
                    }
                }

                respuesta = tipoRespArray;

            } else if (operacion.getSalida().getTipoParametro() instanceof TipoBasicoVO) {
                // La respuesta es un tipo Basico. Por lo que el servicio web devolverá una instancia de ese tipo basico
                // que encapsularemos dentro de una clase TipoBasicoVO.
                String nombreTipo = llamada.getOperation().getReturnType().getLocalPart();
                TipoBasicoVO tipoBasico = new TipoBasicoVO(nombreTipo);
                tipoBasico.setValor(respuesta, nombreTipo);
                respuesta = tipoBasico;
            }

            return (TipoServicioWebVO)respuesta;

        } catch (TipoNoValidoException tnve) {
            tnve.printStackTrace();
            throw new InternalErrorException(tnve);
        }
    }

    public void setCodigoSW(int codigoSW) {
        this.codigoSW = codigoSW;
    }

    public void setParamsBD(String[] params) {
        this.paramsBD = params;
    }

    private void analizarTipos(String opNamespace) throws InternalErrorException {

        cachedSerClasses = new Vector<Class>();
        cachedSerQNames = new Vector<QName>();

        Collection<TipoServicioWebVO> tiposSerializables =
                ReconstruccionSWManager.getInstance().getTiposSerializables(codigoSW, paramsBD);

        for (TipoServicioWebVO tipo: tiposSerializables) {
            if (tipo.esTipoComplejo()) {
                GlobalTypeDescSingleton.getInstance().registerComplexTypeDesc((TipoCompuestoVO)tipo);
                TipoCompuestoVO tipoCompuesto = (TipoCompuestoVO) tipo;
                cachedSerClasses.add(TipoCompuestoVO.class);
                cachedSerQNames.add(new QName(tipoCompuesto.getNamespace(), tipoCompuesto.getNombreTipo()));
            } else if (tipo.esTipoArray()) {
                TipoArrayVO tipoArray = (TipoArrayVO) tipo;
                cachedSerClasses.add(TipoArrayVO.class);
                cachedSerQNames.add(new QName(opNamespace, tipoArray.getNombreTipo()));
            }
        }

    }

    protected Call crearLlamada() throws RemoteException {
        try {

            // Creamos el Servicio.
            super.service = new Service();
            ((Service)super.service).setTypeMappingVersion("1.1");

            Call llamada = super._createCall();

            if (super.maintainSessionSet) llamada.setMaintainSession(super.maintainSession);
            if (super.cachedUsername != null) llamada.setUsername(super.cachedUsername);
            if (super.cachedPassword != null) llamada.setPassword(super.cachedPassword);
            if (super.cachedEndpoint != null) llamada.setTargetEndpointAddress(super.cachedEndpoint);
            if (super.cachedTimeout != null) llamada.setTimeout(super.cachedTimeout);
            if (super.cachedPortName != null) llamada.setPortName(super.cachedPortName);

            Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                llamada.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {

                    llamada.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerQNames.size(); ++i) {
                        Class cls = cachedSerClasses.get(i);
                        QName qName = cachedSerQNames.get(i);
                        m_Log.debug("REGISTRAMOS EL TYPE MAPPING --> Clase: " + cls + " | QName: " + qName);
                        llamada.registerTypeMapping(cls, qName, CustomSerializerFactory.class, CustomDeserializerFactory.class, false);
                    }
                }
            }
            return llamada;
        }
        catch (Throwable t) {
            throw new org.apache.axis.AxisFault("ERROR AL INTENTAR CREAR EL OBJETO CALL", t);
        }
    }

    private OperationDesc crearOperationDesc(OperacionServicioWebVO operacion) throws RemoteException, TipoNoValidoException {
        m_Log.info("crearOperationDesc() BEGIN");
        OperationDesc oper = new OperationDesc();
        ParameterDesc param;

        oper.setName(operacion.getNombreOperacion());

        for (Object objParam : operacion.getParamsEntrada()) {
            ParametroSWVO paramEntrada = (ParametroSWVO) objParam;
            TipoServicioWebVO tipoParam = paramEntrada.getTipoParametro();

            if (tipoParam instanceof TipoCompuestoVO) {
                TipoCompuestoVO tipoCompuesto;
                try {
                    tipoCompuesto = (TipoCompuestoVO) tipoParam;
                } catch (ClassCastException cca) {
                    cca.printStackTrace();
                    throw new RemoteException("ERROR");
                }

                if (operacion.getOpStyle().equals("wrapped")) {
                    for (CampoTipoCompuestoVO campo : tipoCompuesto.getFields()) {
                        String key = campo.getIdCampo();

                        TipoServicioWebVO tipo = campo.getTipoCampo();

                        Class classTipo;
                        QName qNameTipo;
                        QName itemQName = null;
                        if (tipo.esTipoBase()) {
                            TipoBasicoVO tipoBasico = (TipoBasicoVO) tipo;
                            classTipo = tipoBasico.getClaseTipo();
                            qNameTipo = new QName(Constants.URI_2001_SCHEMA_XSD, tipo.getNombreTipo());
                        } else if (tipo.esTipoArray()) {
                            classTipo = TipoArrayVO.class;
                            qNameTipo = new QName(operacion.getNamespace(), tipo.getNombreTipo());
                            TipoArrayVO tipoArray = (TipoArrayVO) tipo;
                            itemQName = new javax.xml.namespace.QName(operacion.getNamespace(), tipoArray.getTipoContenido().getNombreTipo());
                        } else {
                            classTipo = TipoCompuestoVO.class;
                            qNameTipo = new QName(((TipoCompuestoVO) tipo).getNamespace(), tipo.getNombreTipo());
                        }

                        param = new ParameterDesc(new QName(operacion.getNamespace(), key), ParameterDesc.IN, qNameTipo,
                                classTipo, false, false);
                        if (itemQName != null) param.setItemQName(itemQName);
                        param.setOmittable(true);
                        oper.addParameter(param);
                        if (tipo.esTipoBase()) values.add(((TipoBasicoVO) tipo).getValor());
                        else values.add(tipo);
                    }
                } else {
                    // Si el estilo de la operación es RPC, no hace falta descomponer el tipo compuesto en sus campos.
                    QName paramQName = new QName("", paramEntrada.getNombreParametro());
                    Class classTipo = TipoCompuestoVO.class;
                    QName tipoQName = new QName(tipoCompuesto.getNamespace(), tipoCompuesto.getNombreTipo());
                    param = new ParameterDesc(paramQName, ParameterDesc.IN, tipoQName, classTipo, false, false);
                    oper.addParameter(param);
                    values.add(tipoCompuesto);
                }

            } else if (tipoParam instanceof TipoArrayVO) {
                TipoArrayVO tipoArray = (TipoArrayVO) tipoParam;
                TipoServicioWebVO tipoContenido = tipoArray.getTipoContenido();

                Class classTipo = null;
                QName qNameTipo = null;
                if (operacion.getOpStyle().equals("wrapped")) {
                    if (tipoContenido.esTipoBase()) {
                        TipoBasicoVO tipoBasico = (TipoBasicoVO) tipoContenido;
                        classTipo = tipoBasico.getClaseTipo();
                        qNameTipo = new QName(Constants.URI_2001_SCHEMA_XSD, tipoContenido.getNombreTipo());
                    } else if (tipoContenido.esTipoComplejo()) {
                        TipoCompuestoVO tipoCompuesto = (TipoCompuestoVO) tipoContenido;
                        qNameTipo = new QName(tipoCompuesto.getNamespace(), tipoCompuesto.getNombreTipo());
                        classTipo = TipoCompuestoVO.class;
                    }
                } else {
                    // Si el estilo de la operacion es RPC, un parametro array se configurara con sus datos propios.
                    classTipo = TipoArrayVO.class;
                    qNameTipo = new QName(operacion.getNamespace(), tipoArray.getNombreTipo());
                }

                QName qNameParam;
                if (operacion.getOpStyle().equals("wrapped")) qNameParam = new QName(operacion.getNamespace(), tipoArray.getNombreTipo());
                else qNameParam = new QName("", paramEntrada.getNombreParametro());
                param = new ParameterDesc(qNameParam, ParameterDesc.IN, qNameTipo, classTipo, false, false);
                oper.addParameter(param);
                values.add(tipoArray);


            } else if (tipoParam.esTipoBase()) {
                // Este caso solo se da si el estilo del mensaje es RPC.
                TipoBasicoVO tipoBasico = (TipoBasicoVO) tipoParam;
                param = new ParameterDesc(new QName("", paramEntrada.getNombreParametro()), ParameterDesc.IN,
                        new QName(Constants.URI_2001_SCHEMA_XSD, tipoBasico.getNombreTipo()), tipoBasico.getClaseTipo(),
                        false, false);
                oper.addParameter(param);
                m_Log.info("Parámetro de entrada " + paramEntrada.getNombreParametro() + " con valor: " + tipoBasico.getValor());
                values.add(tipoBasico.getValor());
            }
        }

        TipoServicioWebVO tipoRetorno = operacion.getSalida().getTipoParametro();
        if (tipoRetorno.esTipoComplejo()) {
            TipoCompuestoVO tipoCompuesto = (TipoCompuestoVO) tipoRetorno;
            oper.setReturnType(new QName(tipoCompuesto.getNamespace(), tipoCompuesto.getNombreTipo()));
            oper.setReturnClass(TipoCompuestoVO.class);
            oper.setReturnQName(new QName(tipoCompuesto.getNamespace(), tipoCompuesto.getNombreTipo()));
        } else if (tipoRetorno.esTipoArray()) {
            TipoArrayVO tipoArray = (TipoArrayVO) tipoRetorno;
            TipoServicioWebVO tipoContenido = tipoArray.getTipoContenido();

            Class classTipo = null;
            QName qNameTipo = null;
            QName qNameRetorno;
            if (operacion.getOpStyle().equals("wrapped")) {
                if (tipoContenido.esTipoBase()) {
                    TipoBasicoVO tipoBasico = (TipoBasicoVO) tipoContenido;
                    classTipo = tipoBasico.getClaseTipo();
                    qNameTipo = new QName(Constants.URI_2001_SCHEMA_XSD, tipoContenido.getNombreTipo());
                } else if (tipoContenido.esTipoComplejo()) {
                    TipoCompuestoVO tipoCompuesto = (TipoCompuestoVO) tipoContenido;
                    qNameTipo = new QName(tipoCompuesto.getNamespace(), tipoCompuesto.getNombreTipo());
                    classTipo = TipoCompuestoVO.class;
                }
                qNameRetorno = new QName(operacion.getNamespace(), tipoArray.getNombreTipo());

            } else {
                // Si el estilo de la operacion es RPC, un parametro array se configurara con sus datos propios.
                classTipo = ArrayList.class;
                qNameTipo = new QName(operacion.getNamespace(), tipoArray.getNombreTipo());
                qNameRetorno = new QName("", operacion.getSalida().getNombreParametro());
            }
            oper.setReturnType(qNameTipo);
            oper.setReturnClass(classTipo);
            oper.setReturnQName(qNameRetorno);
        } else if (tipoRetorno.esTipoBase()) {
            // Este caso solo se da si el estilo del mensaje es RPC.
            TipoBasicoVO tipoBasico = (TipoBasicoVO) tipoRetorno;
            oper.setReturnType(new QName(Constants.URI_2001_SCHEMA_XSD, tipoBasico.getNombreTipo()));
            oper.setReturnClass(tipoBasico.getClaseTipo());
            oper.setReturnQName(new QName("", operacion.getSalida().getNombreParametro()));
        }

        if (operacion.getOpStyle().equals("wrapped")) {
            oper.setStyle(Style.WRAPPED);
            oper.setUse(Use.LITERAL);
        } else if (operacion.getOpStyle().equals("rpc")) {
            oper.setStyle(Style.RPC);
            oper.setUse(Use.ENCODED);
        }

        return oper;
    }
    
}
