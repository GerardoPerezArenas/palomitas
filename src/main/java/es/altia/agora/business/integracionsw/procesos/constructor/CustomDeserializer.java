package es.altia.agora.business.integracionsw.procesos.constructor;

import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Target;
import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.encoding.ser.SimpleDeserializer;
import org.apache.axis.message.SOAPHandler;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.EnvelopeHandler;
import org.apache.axis.message.SAX2EventRecorder;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.description.FieldDesc;
import org.apache.axis.soap.SOAPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.Vector;

import es.altia.agora.business.integracionsw.*;


public class CustomDeserializer extends SOAPHandler implements Deserializer, Serializable {

    protected static Log m_Log = LogFactory.getLog(CustomDeserializer.class.getName());

    private TypeDesc typeDesc;
    private TipoServicioWebVO value;
    private QName xmlType;

    protected Vector<Target> targets = null;


    public CustomDeserializer(Class javaType, QName xmlType) {
        this(javaType, xmlType, GlobalTypeDescSingleton.getInstance().getTypeDesc(xmlType));
    }

    public CustomDeserializer(Class javaType, QName xmlType, TypeDesc typeDesc) {
        m_Log.debug("DESERIALIZADOR INSTANCIADO PARA LA CLASE: " + javaType + " | TIPO: " + xmlType + " | TYPEDESC: " + typeDesc);
        this.typeDesc = typeDesc;
        if (javaType == TipoCompuestoVO.class) {
            TipoCompuestoVO tipoCompuesto = new TipoCompuestoVO();
            tipoCompuesto.setNombreTipo(xmlType.getLocalPart());
            tipoCompuesto.setNamespace(xmlType.getNamespaceURI());
            value = tipoCompuesto;
        } else if (javaType == TipoArrayVO.class) {
            TipoArrayVO tipoArray = new TipoArrayVO();
            tipoArray.setNombreTipo(xmlType.getLocalPart());
            value = tipoArray;
        }
        this.xmlType = xmlType;

    }


    public Object getValue() {
        return null;
    }

    public void setValue(Object object) {}


    public Object getValue(Object object) {
        return null;
    }

    public void setChildValue(Object object, Object object1) throws SAXException {

    }

    public void setDefaultType(QName qName) {

    }

    public QName getDefaultType() {
        return null;
    }


    public void registerValueTarget(Target target) {
        if (targets == null) targets = new Vector<Target>();
        targets.addElement(target);
    }

    public Vector getValueTargets() {
        return null;
    }

    public void removeValueTargets() {

    }

    public void moveValueTargets(Deserializer deserializer) {

    }

    public boolean componentsReady() {
        return false;
    }

    public void valueComplete() throws SAXException {

        //if (componentsReady()) {
            if (targets != null) {
                for (Target target : targets) {
                    target.set(value);
                    m_Log.debug("SE HA COMPLETADO EL OBJETO CON VALOR: " + value);
                }
                // Don't need targets any more, so clear them
                removeValueTargets();
            }
        //}
    }

    public void startElement(String namespace, String localName, String prefix, Attributes attributes,
                             DeserializationContext context)
    throws SAXException {

        onStartElement(namespace, localName, prefix, attributes, context);
    }

    public void onStartElement(String namespace, String localName, String prefix, Attributes attributes,
                               DeserializationContext context)
    throws SAXException {

        m_Log.debug("VAMOS A DESERIALIZAR PARA (" + namespace + "|" + localName +")");
        if (!(new QName(namespace, localName).equals(xmlType))) {
            try {
                if (typeDesc != null && typeDesc.getFieldByName(localName) != null) {
                    // Como existe un typeDesc, estamos deserializando elementos de un tipo compuesto.
                    TipoCompuestoVO tipoCompuesto = (TipoCompuestoVO)value;

                    FieldDesc field = typeDesc.getFieldByName(localName);
                    QName fieldType = field.getXmlType();

                    Deserializer dSer = context.getDeserializerForType(fieldType);
                    MessageElement me = context.getCurElement();
                    if (dSer == null) {
                        m_Log.debug("NO HEMOS ENCONTRADO UN DESERIALIZADOR PARA (" + namespace + "|" + localName + ") Y NO ES UN TIPO BASICO");

                    } else {
                        m_Log.debug("HEMOS ENCONTRADO UN DESERIALIZADOR PARA (" + namespace + "|" + localName +")");
                        if (dSer instanceof SimpleDeserializer) {
                            // Como el deserializador es de tipo Simple, el elemento a deserializar es de Tipo Basico.
                            TipoBasicoVO tipoBasicoVO = new TipoBasicoVO(fieldType.getLocalPart());
                            tipoBasicoVO.setValor(me.getObjectValue(tipoBasicoVO.getClaseTipo()), tipoBasicoVO.getNombreTipo());

                            tipoCompuesto.getFields().add(new CampoTipoCompuestoVO(localName, tipoBasicoVO));

                        } else {
                            m_Log.debug("HEMOS ENCONTRADO UN VALOR DE TIPO COMPUESTO PARA EL TIPO COMPUESTO");
                        }
                    }

                } else if (typeDesc == null) {
                    // No existe el typeDesc, en este caso estamos deserializando el hijo de un array
                    TipoArrayVO tipoArray = (TipoArrayVO)value;

                    m_Log.debug("ESTAMOS DESERIALIZANDO EL HIJO DE UN TIPO ARRAY");
                    QName fieldType = new QName(namespace, localName);
                    Deserializer dSer = context.getDeserializerForType(fieldType);
                    MessageElement me = context.getCurElement();
                    m_Log.debug("HIJOS DEL MESSAGE:" + me.getChildNodes());
                    if (dSer == null && me.getChildNodes() != null) {
                        m_Log.debug("NO HEMOS ENCONTRADO UN DESERIALIZADOR PARA (" + namespace + "|" + localName + ")");
                    } else {
                        m_Log.debug("HEMOS ENCONTRADO UN DESERIALIZADOR PARA (" + namespace + "|" + localName +")");
                        if (dSer instanceof SimpleDeserializer) {
                            // Como el deserializador es de tipo Simple, el elemento a deserializar es de Tipo Basico.
                            TipoBasicoVO tipoBasicoVO = new TipoBasicoVO(fieldType.getLocalPart());
                            tipoBasicoVO.setValor(me.getObjectValue(tipoBasicoVO.getClaseTipo()), tipoBasicoVO.getNombreTipo());

                            if (tipoArray.getTipoContenido() == null) tipoArray.setTipoContenido(tipoBasicoVO);
                            if (tipoArray.getArray() == null) tipoArray.setArray(new Vector<TipoServicioWebVO>());
                            tipoArray.getArray().add(tipoBasicoVO);

                        } else {
                            m_Log.debug("HEMOS ENCONTRADO UN VALOR DE TIPO COMPUESTO PARA EL TIPO ARRAY");
                        }
                    }
                } else if (typeDesc != null) {
                    // Existe un typeDesc pero no se encuentra el campo. Comprobamos si tiene un id de referencia.
                    SOAPConstants soapConstants = context.getSOAPConstants();
                    String href = attributes.getValue(soapConstants.getAttrHref());

                    if (href != null) {

                        Object ref = context.getObjectByRef(href);
                        m_Log.debug("SE RECUPERO EL OBJETO DE REFERENCIA: " + ref);
                        if (ref instanceof MessageElement) {
                            context.replaceElementHandler(new EnvelopeHandler(this));

                            SAX2EventRecorder r = context.getRecorder();
                            context.setRecorder(null);
                            ((MessageElement)ref).publishToHandler(context);
                            context.setRecorder(r);
                        }
                    }

                    
                }


            } catch (Exception e) {
                e.printStackTrace();
                throw new SAXException(e);
            }
        }


    }

    public SOAPHandler onStartChild(String namespace, String localName, String prefix, Attributes attributes,
                                    DeserializationContext context)
    throws SAXException {
        m_Log.debug("HEMOS ENTRADO EN EL HIJO: " + namespace + " | " + localName);

        if (typeDesc != null) {
            try {
                // Como existe un typeDesc, estamos deserializando elementos de un tipo compuesto.
                FieldDesc field = typeDesc.getFieldByName(localName);
                QName fieldType = field.getXmlType();

                Deserializer dSer = context.getDeserializerForType(fieldType);
                if (dSer == null) {
                    m_Log.debug("NO HEMOS ENCONTRADO UN DESERIALIZADOR PARA (" + namespace + "|" + localName + ") --> CAMPO ARRAY DE TIPO COMPUESTO");
                    dSer = new CustomDeserializer(TipoArrayVO.class, fieldType, null);
                    dSer.registerValueTarget(new TipoServicioWebTarget(value, localName));
                    return (SOAPHandler)dSer;
                } else {
                    m_Log.debug("HEMOS ENCONTRADO UN DESERIALIZADOR PARA (" + namespace + "|" + localName + ") --> CAMPO NO ARRAY DE TIPO COMPUESTO");
                    dSer.registerValueTarget(new TipoServicioWebTarget(value, localName, fieldType.getLocalPart()));
                    return (SOAPHandler)dSer;
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SAXException(e);
            }
        } else {
            m_Log.debug("ESTAMOS DESERIALIZANDO EL HIJO DE UN TIPO ARRAY");
            QName fieldType = new QName(namespace, localName);
            Deserializer dSer = context.getDeserializerForType(fieldType);

            if (dSer == null) {
                m_Log.debug("NO HEMOS ENCONTRADO UN DESERIALIZADOR PARA (" + namespace + "|" + localName + ") --> CAMPO ARRAY DE TIPO ARRAY");
                if (fieldType.getLocalPart().equals("anyType")) dSer = new SimpleDeserializer(String.class, fieldType, null);
                else dSer = new CustomDeserializer(TipoArrayVO.class, fieldType, null);
            }

            dSer.registerValueTarget(new TipoServicioWebTarget(value, localName, fieldType.getLocalPart()));
            return (SOAPHandler)dSer;
        }

    }

    public void endElement(String namespace, String localName, DeserializationContext context)
    throws SAXException {
        
        m_Log.debug("FINALIZAMOS DE PROCESAR EL ELEMENTO: " + namespace + "|" + localName);
        super.endElement(namespace, localName, context);

        

        valueComplete();

    }

    public void onEndElement(String string, String string1, DeserializationContext deserializationContext)
    throws SAXException {

    }

    public String getMechanismType() {
        return null;
    }

    public void setValue(Object object, Object object1) throws SAXException {

    }
}
