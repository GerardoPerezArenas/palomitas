package es.altia.agora.business.integracionsw.procesos.constructor;

import org.apache.axis.encoding.ser.BaseSerializerFactory;

import javax.xml.namespace.QName;

public class CustomSerializerFactory extends BaseSerializerFactory {

    public CustomSerializerFactory(Class javaType, QName xmlType) {
        super(CustomSerializer.class, xmlType, javaType);
    }
}
