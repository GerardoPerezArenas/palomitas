/**
 * TipoMetadatosAdicionales.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type;

public class TipoMetadatosAdicionales  implements java.io.Serializable {
    private es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.MetadatoAdicional metadatoAdicional;

    public TipoMetadatosAdicionales() {
    }

    public TipoMetadatosAdicionales(
           es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.MetadatoAdicional metadatoAdicional) {
           this.metadatoAdicional = metadatoAdicional;
    }


    /**
     * Gets the metadatoAdicional value for this TipoMetadatosAdicionales.
     * 
     * @return metadatoAdicional
     */
    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.MetadatoAdicional getMetadatoAdicional() {
        return metadatoAdicional;
    }


    /**
     * Sets the metadatoAdicional value for this TipoMetadatosAdicionales.
     * 
     * @param metadatoAdicional
     */
    public void setMetadatoAdicional(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.MetadatoAdicional metadatoAdicional) {
        this.metadatoAdicional = metadatoAdicional;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TipoMetadatosAdicionales)) return false;
        TipoMetadatosAdicionales other = (TipoMetadatosAdicionales) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.metadatoAdicional==null && other.getMetadatoAdicional()==null) || 
             (this.metadatoAdicional!=null &&
              this.metadatoAdicional.equals(other.getMetadatoAdicional())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getMetadatoAdicional() != null) {
            _hashCode += getMetadatoAdicional().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TipoMetadatosAdicionales.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "TipoMetadatosAdicionales"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("metadatoAdicional");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MetadatoAdicional"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "MetadatoAdicional"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
