package es.altia.agora.business.integracionsw.procesos.constructor;

import org.apache.axis.encoding.ser.BaseDeserializerFactory;
import org.apache.axis.description.TypeDesc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import java.util.Map;

public class CustomDeserializerFactory extends BaseDeserializerFactory {

    protected static Log m_Log = LogFactory.getLog(CustomDeserializerFactory.class.getName());

    /**
     * Type metadata about this class for XML deserialization
     */
    protected transient TypeDesc typeDesc = null;
    protected transient Map propertyMap = null;

    public CustomDeserializerFactory(Class javaType, QName qName) {
        super(CustomDeserializer.class, qName, javaType);

        typeDesc = GlobalTypeDescSingleton.getInstance().getTypeDesc(qName);
        propertyMap = getProperties(typeDesc);
    }

    public static Map getProperties(TypeDesc typeDesc) {
        Map propertyMap = null;

        if (typeDesc != null) {
            propertyMap = typeDesc.getPropertyDescriptorMap();
        }
        
        return propertyMap;
    }

}
