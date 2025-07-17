/**
 * EnumeracionEstadoElaboracion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos;

public class EnumeracionEstadoElaboracion implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected EnumeracionEstadoElaboracion(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _EE01 = "EE01";
    public static final java.lang.String _EE02 = "EE02";
    public static final java.lang.String _EE03 = "EE03";
    public static final java.lang.String _EE04 = "EE04";
    public static final java.lang.String _EE99 = "EE99";
    public static final EnumeracionEstadoElaboracion EE01 = new EnumeracionEstadoElaboracion(_EE01);
    public static final EnumeracionEstadoElaboracion EE02 = new EnumeracionEstadoElaboracion(_EE02);
    public static final EnumeracionEstadoElaboracion EE03 = new EnumeracionEstadoElaboracion(_EE03);
    public static final EnumeracionEstadoElaboracion EE04 = new EnumeracionEstadoElaboracion(_EE04);
    public static final EnumeracionEstadoElaboracion EE99 = new EnumeracionEstadoElaboracion(_EE99);
    public java.lang.String getValue() { return _value_;}
    public static EnumeracionEstadoElaboracion fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        EnumeracionEstadoElaboracion enumeration = (EnumeracionEstadoElaboracion)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static EnumeracionEstadoElaboracion fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EnumeracionEstadoElaboracion.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "enumeracionEstadoElaboracion"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
