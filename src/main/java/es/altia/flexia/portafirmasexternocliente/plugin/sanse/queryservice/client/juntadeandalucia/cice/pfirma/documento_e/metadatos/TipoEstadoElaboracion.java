/**
 * TipoEstadoElaboracion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos;

public class TipoEstadoElaboracion  implements java.io.Serializable {
    private es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.EnumeracionEstadoElaboracion valorEstadoElaboracion;

    private java.lang.String identificadorDocumentoOrigen;

    public TipoEstadoElaboracion() {
    }

    public TipoEstadoElaboracion(
           es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.EnumeracionEstadoElaboracion valorEstadoElaboracion,
           java.lang.String identificadorDocumentoOrigen) {
           this.valorEstadoElaboracion = valorEstadoElaboracion;
           this.identificadorDocumentoOrigen = identificadorDocumentoOrigen;
    }


    /**
     * Gets the valorEstadoElaboracion value for this TipoEstadoElaboracion.
     * 
     * @return valorEstadoElaboracion
     */
    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.EnumeracionEstadoElaboracion getValorEstadoElaboracion() {
        return valorEstadoElaboracion;
    }


    /**
     * Sets the valorEstadoElaboracion value for this TipoEstadoElaboracion.
     * 
     * @param valorEstadoElaboracion
     */
    public void setValorEstadoElaboracion(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.EnumeracionEstadoElaboracion valorEstadoElaboracion) {
        this.valorEstadoElaboracion = valorEstadoElaboracion;
    }


    /**
     * Gets the identificadorDocumentoOrigen value for this TipoEstadoElaboracion.
     * 
     * @return identificadorDocumentoOrigen
     */
    public java.lang.String getIdentificadorDocumentoOrigen() {
        return identificadorDocumentoOrigen;
    }


    /**
     * Sets the identificadorDocumentoOrigen value for this TipoEstadoElaboracion.
     * 
     * @param identificadorDocumentoOrigen
     */
    public void setIdentificadorDocumentoOrigen(java.lang.String identificadorDocumentoOrigen) {
        this.identificadorDocumentoOrigen = identificadorDocumentoOrigen;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TipoEstadoElaboracion)) return false;
        TipoEstadoElaboracion other = (TipoEstadoElaboracion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.valorEstadoElaboracion==null && other.getValorEstadoElaboracion()==null) || 
             (this.valorEstadoElaboracion!=null &&
              this.valorEstadoElaboracion.equals(other.getValorEstadoElaboracion()))) &&
            ((this.identificadorDocumentoOrigen==null && other.getIdentificadorDocumentoOrigen()==null) || 
             (this.identificadorDocumentoOrigen!=null &&
              this.identificadorDocumentoOrigen.equals(other.getIdentificadorDocumentoOrigen())));
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
        if (getValorEstadoElaboracion() != null) {
            _hashCode += getValorEstadoElaboracion().hashCode();
        }
        if (getIdentificadorDocumentoOrigen() != null) {
            _hashCode += getIdentificadorDocumentoOrigen().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TipoEstadoElaboracion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "TipoEstadoElaboracion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valorEstadoElaboracion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "ValorEstadoElaboracion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "enumeracionEstadoElaboracion"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificadorDocumentoOrigen");
        elemField.setXmlName(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "IdentificadorDocumentoOrigen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
