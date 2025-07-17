/**
 * VolanteE.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tercero.servicios.iasoft.cliente;

public class VolanteE  implements java.io.Serializable {
    private java.lang.String codigoProvincia;

    private java.lang.String codigoMunicipio;

    private java.lang.String documentacion;

    public VolanteE() {
    }

    public VolanteE(
           java.lang.String codigoProvincia,
           java.lang.String codigoMunicipio,
           java.lang.String documentacion) {
           this.codigoProvincia = codigoProvincia;
           this.codigoMunicipio = codigoMunicipio;
           this.documentacion = documentacion;
    }


    /**
     * Gets the codigoProvincia value for this VolanteE.
     * 
     * @return codigoProvincia
     */
    public java.lang.String getCodigoProvincia() {
        return codigoProvincia;
    }


    /**
     * Sets the codigoProvincia value for this VolanteE.
     * 
     * @param codigoProvincia
     */
    public void setCodigoProvincia(java.lang.String codigoProvincia) {
        this.codigoProvincia = codigoProvincia;
    }


    /**
     * Gets the codigoMunicipio value for this VolanteE.
     * 
     * @return codigoMunicipio
     */
    public java.lang.String getCodigoMunicipio() {
        return codigoMunicipio;
    }


    /**
     * Sets the codigoMunicipio value for this VolanteE.
     * 
     * @param codigoMunicipio
     */
    public void setCodigoMunicipio(java.lang.String codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }


    /**
     * Gets the documentacion value for this VolanteE.
     * 
     * @return documentacion
     */
    public java.lang.String getDocumentacion() {
        return documentacion;
    }


    /**
     * Sets the documentacion value for this VolanteE.
     * 
     * @param documentacion
     */
    public void setDocumentacion(java.lang.String documentacion) {
        this.documentacion = documentacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VolanteE)) return false;
        VolanteE other = (VolanteE) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigoProvincia==null && other.getCodigoProvincia()==null) || 
             (this.codigoProvincia!=null &&
              this.codigoProvincia.equals(other.getCodigoProvincia()))) &&
            ((this.codigoMunicipio==null && other.getCodigoMunicipio()==null) || 
             (this.codigoMunicipio!=null &&
              this.codigoMunicipio.equals(other.getCodigoMunicipio()))) &&
            ((this.documentacion==null && other.getDocumentacion()==null) || 
             (this.documentacion!=null &&
              this.documentacion.equals(other.getDocumentacion())));
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
        if (getCodigoProvincia() != null) {
            _hashCode += getCodigoProvincia().hashCode();
        }
        if (getCodigoMunicipio() != null) {
            _hashCode += getCodigoMunicipio().hashCode();
        }
        if (getDocumentacion() != null) {
            _hashCode += getDocumentacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VolanteE.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.red.es/padron", ">VolanteE"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoProvincia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.red.es/padron", "CodigoProvincia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoMunicipio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.red.es/padron", "CodigoMunicipio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.red.es/padron", "Documentacion"));
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
