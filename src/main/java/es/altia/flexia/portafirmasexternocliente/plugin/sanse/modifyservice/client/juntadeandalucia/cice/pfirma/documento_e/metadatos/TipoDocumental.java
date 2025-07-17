/**
 * TipoDocumental.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.modifyservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos;

public class TipoDocumental implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TipoDocumental(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _TD01 = "TD01";
    public static final java.lang.String _TD02 = "TD02";
    public static final java.lang.String _TD03 = "TD03";
    public static final java.lang.String _TD04 = "TD04";
    public static final java.lang.String _TD05 = "TD05";
    public static final java.lang.String _TD06 = "TD06";
    public static final java.lang.String _TD07 = "TD07";
    public static final java.lang.String _TD08 = "TD08";
    public static final java.lang.String _TD09 = "TD09";
    public static final java.lang.String _TD10 = "TD10";
    public static final java.lang.String _TD11 = "TD11";
    public static final java.lang.String _TD12 = "TD12";
    public static final java.lang.String _TD13 = "TD13";
    public static final java.lang.String _TD14 = "TD14";
    public static final java.lang.String _TD15 = "TD15";
    public static final java.lang.String _TD16 = "TD16";
    public static final java.lang.String _TD17 = "TD17";
    public static final java.lang.String _TD18 = "TD18";
    public static final java.lang.String _TD19 = "TD19";
    public static final java.lang.String _TD20 = "TD20";
    public static final java.lang.String _TD99 = "TD99";
    public static final TipoDocumental TD01 = new TipoDocumental(_TD01);
    public static final TipoDocumental TD02 = new TipoDocumental(_TD02);
    public static final TipoDocumental TD03 = new TipoDocumental(_TD03);
    public static final TipoDocumental TD04 = new TipoDocumental(_TD04);
    public static final TipoDocumental TD05 = new TipoDocumental(_TD05);
    public static final TipoDocumental TD06 = new TipoDocumental(_TD06);
    public static final TipoDocumental TD07 = new TipoDocumental(_TD07);
    public static final TipoDocumental TD08 = new TipoDocumental(_TD08);
    public static final TipoDocumental TD09 = new TipoDocumental(_TD09);
    public static final TipoDocumental TD10 = new TipoDocumental(_TD10);
    public static final TipoDocumental TD11 = new TipoDocumental(_TD11);
    public static final TipoDocumental TD12 = new TipoDocumental(_TD12);
    public static final TipoDocumental TD13 = new TipoDocumental(_TD13);
    public static final TipoDocumental TD14 = new TipoDocumental(_TD14);
    public static final TipoDocumental TD15 = new TipoDocumental(_TD15);
    public static final TipoDocumental TD16 = new TipoDocumental(_TD16);
    public static final TipoDocumental TD17 = new TipoDocumental(_TD17);
    public static final TipoDocumental TD18 = new TipoDocumental(_TD18);
    public static final TipoDocumental TD19 = new TipoDocumental(_TD19);
    public static final TipoDocumental TD20 = new TipoDocumental(_TD20);
    public static final TipoDocumental TD99 = new TipoDocumental(_TD99);
    public java.lang.String getValue() { return _value_;}
    public static TipoDocumental fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TipoDocumental enumeration = (TipoDocumental)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TipoDocumental fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TipoDocumental.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "tipoDocumental"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
