package es.altia.agora.business.integracionsw.procesos.constructor;

import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.wsdl.fromJava.Types;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.description.FieldDesc;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.utils.Messages;
import org.apache.axis.utils.JavaUtils;
import org.apache.axis.MessageContext;
import org.apache.axis.Constants;
import org.apache.axis.AxisEngine;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.schema.SchemaVersion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.io.Serializable;
import java.io.IOException;
import java.util.Vector;
import java.lang.reflect.Array;

import es.altia.agora.business.integracionsw.*;

public class CustomSerializer implements Serializer, Serializable {

    private QName xmlType;
    private Class javaType;
    private TypeDesc typeDesc;

    protected static Log m_Log = LogFactory.getLog(CustomSerializer.class.getName());

    // Construct CustomSerializer for the indicated class/qname
    public CustomSerializer(Class javaType, QName xmlType) {
        this(javaType, xmlType, GlobalTypeDescSingleton.getInstance().getTypeDesc(xmlType));
    }

    // Construct CustomSerializer for the indicated class/qname
    public CustomSerializer(Class javaType, QName xmlType, TypeDesc typeDesc) {

        this.xmlType = xmlType;
        this.javaType = javaType;
        this.typeDesc = typeDesc;

    }

    public void serialize(QName qName, Attributes attributes, Object object, SerializationContext context) throws IOException {
        m_Log.debug("SE PRETENDE SERIALIZAR: " + qName + " | " + object + " | " + object.getClass() + " | " + javaType);
        if (object.getClass().equals(TipoArrayVO.class)) {
            // Serializamos un array
            serializeArray(qName, attributes, (TipoArrayVO)object, context);
        } else if (object.getClass().equals(TipoCompuestoVO.class)) {
            // Serializamos un array
            serializeBean(qName, attributes, (TipoCompuestoVO)object, context);
        }
    }

    public Element writeSchema(Class aClass, Types types) throws Exception {
        return null;
    }

    public String getMechanismType() {
        return null;
    }

    /**
     * Serialize an element that is an array.
     * @param name is the element name
     * @param attributes are the attributes...serialize is free to add more.
     * @param value is the value
     * @param context is the SerializationContext
     * @throws java.io.IOException En caso de error al analizar el tipo array
     */
    public void serializeArray(QName name, Attributes attributes, TipoArrayVO value, SerializationContext context)
    throws IOException {
        if (value == null) throw new IOException(Messages.getMessage("cantDoNullArray00"));

        MessageContext msgContext = context.getMessageContext();
        SchemaVersion schema = SchemaVersion.SCHEMA_2001;
        SOAPConstants soap = SOAPConstants.SOAP11_CONSTANTS;
        boolean encoded = context.isEncoded();

        if (msgContext != null) {
            schema = msgContext.getSchemaVersion();
            soap = msgContext.getSOAPConstants();
        }

        Vector dataVector = value.getArray();

        // Get the componentType of the array/list
        Class componentClass;
        TipoServicioWebVO tipoContenido = value.getTipoContenido();
        if (tipoContenido.esTipoBase()) {
            TipoBasicoVO tipoBasico = (TipoBasicoVO) tipoContenido;
            componentClass = tipoBasico.getClaseTipo();
        } else if (tipoContenido.esTipoArray()) {
            componentClass = TipoArrayVO.class;
        } else {
            componentClass = TipoCompuestoVO.class;
        }

        String dims = "[]";
        // Get the QName of the componentType
        // if it wasn't passed in from the constructor
        QName componentTypeQName;
        if (tipoContenido.esTipoBase()) {
            componentTypeQName = new QName(Constants.URI_DEFAULT_SCHEMA_XSD, tipoContenido.getNombreTipo());
        }
        else if (tipoContenido.esTipoArray())
            componentTypeQName = new QName(Constants.URI_DEFAULT_SCHEMA_XSD, tipoContenido.getNombreTipo());
        else {
            TipoCompuestoVO tipoCompuesto = (TipoCompuestoVO) tipoContenido;
            componentTypeQName = new QName(tipoCompuesto.getNamespace(), tipoContenido.getNombreTipo());
        }

        if (componentTypeQName == null) {
            componentTypeQName = context.getCurrentXMLType();
            if (componentTypeQName != null) {
                if ((componentTypeQName.equals(xmlType) ||
                        componentTypeQName.equals(Constants.XSD_ANYTYPE) ||
                        componentTypeQName.equals(soap.getArrayType()))) {
                    componentTypeQName = null;
                }
            }
        }

        if (componentTypeQName == null) {
            componentTypeQName = context.getItemType();
        }

        // Then check the type mapping for the class
        if (componentTypeQName == null) {
            componentTypeQName = context.getQNameForClass(componentClass);
        }

        // If still not found, look at the super classes
        if (componentTypeQName == null) {
            Class searchCls = componentClass;
            while(searchCls != null && componentTypeQName == null) {
                searchCls = searchCls.getSuperclass();
                componentTypeQName = context.getQNameForClass(searchCls);
            }
            if (componentTypeQName != null) {
                componentClass = searchCls;
            }
        }

        // Still can't find it?  Throw an error.
        if (componentTypeQName == null) {
            throw new IOException(
                    Messages.getMessage("noType00", componentClass.getName()));
        }

        int len = (dataVector == null) ? Array.getLength(value) : dataVector.size();
        String arrayType = "";
        int dim2Len = -1;
        if (encoded) {
            if (soap == SOAPConstants.SOAP12_CONSTANTS) {
                arrayType = dims + len;
            } else {
                arrayType = dims + "[" + len + "]";
            }

            // Discover whether array can be serialized directly as a two-dimensional
            // array (i.e. arrayType=int[2,3]) versus an array of arrays.
            // Benefits:
            //   - Less text passed on the wire.
            //   - Easier to read wire format
            //   - Tests the deserialization of multi-dimensional arrays.
            // Drawbacks:
            //   - Is not safe!  It is possible that the arrays are multiply
            //     referenced.  Transforming into a 2-dim array will cause the
            //     multi-referenced information to be lost.  Plus there is no
            //     way to determine whether the arrays are multi-referenced.
            //   - .NET currently (Dec 2002) does not support 2D SOAP-encoded arrays
            //
            // OLD Comment as to why this was ENABLED:
            // It is necessary for
            // interoperability (echo2DStringArray).  It is 'safe' for now
            // because Axis treats arrays as non multi-ref (see the note
            // in SerializationContext.isPrimitive(...) )
            // More complicated processing is necessary for 3-dim arrays, etc.
            //
            // Axis 1.1 - December 2002
            // Turned this OFF because Microsoft .NET can not deserialize
            // multi-dimensional SOAP-encoded arrays, and this interopability
            // is pretty high visibility. Make it a global configuration parameter:
            //  <parameter name="enable2DArrayEncoding" value="true"/>    (tomj)
            //

            // Check the message context to see if we should turn 2D processing ON
            // Default is OFF
            boolean enable2Dim = false;

            // Vidyanand : added this check
            if( msgContext != null ) {
               enable2Dim = JavaUtils.isTrueExplicitly(msgContext.getProperty(
                       AxisEngine.PROP_TWOD_ARRAY_ENCODING));
            }

            if (enable2Dim && !dims.equals("")) {
                if (len > 0) {
                    boolean okay = true;
                    // Make sure all of the component arrays are the same size
                    for (int i=0; i < len && okay; i++) {

                        Object elementValue = Array.get(value, i);
                        if (elementValue == null)
                            okay = false;
                        else if (dim2Len < 0) {
                            dim2Len = Array.getLength(elementValue);
                            if (dim2Len <= 0) {
                                okay = false;
                            }
                        } else if (dim2Len != Array.getLength(elementValue)) {
                            okay = false;
                        }
                    }
                    // Update the arrayType to use mult-dim array encoding
                    if (okay) {
                        dims = dims.substring(0, dims.length()-2);
                        if (soap == SOAPConstants.SOAP12_CONSTANTS)
                            arrayType = dims + len + " " + dim2Len;
                        else
                            arrayType = dims + "[" + len + "," + dim2Len + "]";
                    } else {
                        dim2Len = -1;
                    }
                }
            }
        }

        // Need to distinguish if this is array processing for an
        // actual schema array or for a maxOccurs usage.
        // For the maxOccurs case, the currentXMLType of the context is
        // the same as the componentTypeQName.
        QName itemQName = context.getItemQName();
        boolean maxOccursUsage = !encoded && itemQName == null &&
                componentTypeQName.equals(context.getCurrentXMLType());

        if (encoded) {
            AttributesImpl attrs;
            if (attributes == null) {
                attrs = new AttributesImpl();
            } else if (attributes instanceof AttributesImpl) {
                attrs = (AttributesImpl)attributes;
            } else {
                attrs = new AttributesImpl(attributes);
            }

            String compType = context.attributeQName2String(componentTypeQName);

            if (attrs.getIndex(soap.getEncodingURI(), soap.getAttrItemType()) == -1) {
                String encprefix = context.getPrefixForURI(soap.getEncodingURI());

                if (soap != SOAPConstants.SOAP12_CONSTANTS) {
                    compType = compType + arrayType;
                    attrs.addAttribute(soap.getEncodingURI(), soap.getAttrItemType(), encprefix + ":arrayType", "CDATA",
                                       compType);
                } else {
                    attrs.addAttribute(soap.getEncodingURI(), soap.getAttrItemType(), encprefix + ":itemType", "CDATA",
                                       compType);
                    attrs.addAttribute(soap.getEncodingURI(), "arraySize", encprefix + ":arraySize", "CDATA", arrayType);
                }
            }

            String qname = context.getPrefixForURI(schema.getXsiURI(), "xsi") + ":type";
            QName soapArray;
            if (soap == SOAPConstants.SOAP12_CONSTANTS) soapArray = Constants.SOAP_ARRAY12;
            else soapArray = Constants.SOAP_ARRAY;

            int typeI = attrs.getIndex(schema.getXsiURI(), "type");
            if (typeI != -1) {
                attrs.setAttribute(typeI, schema.getXsiURI(), "type", qname, "CDATA", context.qName2String(soapArray));
            } else {
                attrs.addAttribute(schema.getXsiURI(), "type", qname, "CDATA", context.qName2String(soapArray));
            }

            attributes = attrs;
        }

        QName elementName = name;
        Attributes serializeAttr = attributes;
        if (!maxOccursUsage) {
            serializeAttr = null;  // since we are putting them here
            m_Log.debug("EMPEZAMOS EL ELEMENTO: " + name + " | " + attributes);
            context.startElement(name, attributes);
            if (itemQName != null) elementName = itemQName;
            else if(componentTypeQName != null) elementName = componentTypeQName;
        }


        if (dim2Len < 0) {
            for (Object aValue : dataVector) {
                if (aValue instanceof TipoBasicoVO) aValue = ((TipoBasicoVO) aValue).getValorAsString();
                // Serialize the element.
                Attributes attr = (serializeAttr == null ? serializeAttr : new AttributesImpl(serializeAttr));
                context.serialize(elementName, attr,aValue,componentTypeQName, componentClass); // prefered type QName
            }
        } else {
            // Serialize as a 2 dimensional array
            for (int index = 0; index < len; index++) {
                for (int index2 = 0; index2 < dim2Len; index2++) {
                    Object aValue = Array.get(Array.get(value, index), index2);
                    if (aValue instanceof TipoBasicoVO) aValue = ((TipoBasicoVO)aValue).getValorAsString();
                    context.serialize(elementName, null, aValue, componentTypeQName, componentClass);
                }
            }
        }

        if (!maxOccursUsage)
            context.endElement();
    }

     public void serializeBean(QName name, Attributes attributes, TipoCompuestoVO value, SerializationContext context)
     throws IOException {

        // Get the encoding style
        boolean isEncoded = context.isEncoded();

        // check whether we have and xsd:any namespace="##any" type
        boolean suppressElement = !isEncoded && name.getNamespaceURI().equals("") && name.getLocalPart().equals("any");

        if (!suppressElement) context.startElement(name, attributes);

        try {
            // Serialize each property
            for (CampoTipoCompuestoVO campo: value.getFields()) {
                String propName = campo.getIdCampo();

                QName qname = null;
                QName xmlType = null;

                TipoServicioWebVO tipoContenido = campo.getTipoCampo();
                Class javaType;
                if (tipoContenido.esTipoBase()) {
                    TipoBasicoVO tipoBasico = (TipoBasicoVO)tipoContenido;
                    javaType = tipoBasico.getClaseTipo();
                } else if (tipoContenido.esTipoArray()) {
                    javaType = TipoArrayVO.class;
                } else {
                    javaType = TipoCompuestoVO.class;
                }

                boolean isOmittable = false;
                // isNillable default value depends on the field type
                boolean isNillable = Types.isNullable(javaType);
                // isArray
                boolean isArray = false;
                QName itemQName = null;

                // If we have type metadata, check to see what we're doing
                // with this field.  If it's an attribute, skip it.  If it's
                // an element, use whatever qname is in there.  If we can't
                // find any of this info, use the default.
                if (typeDesc != null) {
                    FieldDesc field = typeDesc.getFieldByName(propName);
                    if (field != null) {
                        if (!field.isElement()) {
                            continue;
                        }

                        ElementDesc element = (ElementDesc)field;

                        // If we're SOAP encoded, just use the local part,
                        // not the namespace.  Otherwise use the whole
                        // QName.
                        if (isEncoded) {
                            qname = new QName(element.getXmlName().getLocalPart());
                        } else {
                            qname = element.getXmlName();
                        }
                        isOmittable = element.isMinOccursZero();
                        isNillable = element.isNillable();
                        isArray = element.isMaxOccursUnbounded();
                        xmlType = element.getXmlType();
                        itemQName = element.getItemQName();
                        context.setItemQName(itemQName);
                    }
                }

                if (qname == null) {
                    qname = new QName(isEncoded ? "" : name.getNamespaceURI(), propName);
                }

                if (xmlType == null) {
                    // look up the type QName using the class
                    if (tipoContenido instanceof TipoCompuestoVO) {
                        TipoCompuestoVO tipoCompuesto = (TipoCompuestoVO)tipoContenido;
                        xmlType = new QName(tipoCompuesto.getNamespace(), tipoCompuesto.getNombreTipo());
                    } else {
                        xmlType = context.getQNameForClass(javaType);
                    }
                }

                Object valueContenido;
                if (tipoContenido instanceof TipoBasicoVO) valueContenido = ((TipoBasicoVO)tipoContenido).getValorAsString();
                else valueContenido = tipoContenido;
                context.serialize(qname, null, valueContenido, xmlType, javaType);
            }

        } catch (Exception e) {
            m_Log.error(Messages.getMessage("exception00"), e);
            throw new IOException(e.toString());
        }

        if (!suppressElement)
            context.endElement();
    }

}
