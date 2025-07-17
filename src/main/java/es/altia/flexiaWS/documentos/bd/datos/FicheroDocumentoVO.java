/**
 * FicheroDocumentoVO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.documentos.bd.datos;

public class FicheroDocumentoVO  implements java.io.Serializable {
    private java.lang.String nombreDoc;

    private java.lang.String tipomime;

    private byte[] bytes;

    public FicheroDocumentoVO() {
    }

    public FicheroDocumentoVO(
           java.lang.String nombreDoc,
           java.lang.String tipomime,
           byte[] bytes) {
           this.nombreDoc = nombreDoc;
           this.tipomime = tipomime;
           this.bytes = bytes;
    }


    /**
     * Gets the nombreDoc value for this FicheroDocumentoVO.
     * 
     * @return nombreDoc
     */
    public java.lang.String getNombreDoc() {
        return nombreDoc;
    }


    /**
     * Sets the nombreDoc value for this FicheroDocumentoVO.
     * 
     * @param nombreDoc
     */
    public void setNombreDoc(java.lang.String nombreDoc) {
        this.nombreDoc = nombreDoc;
    }


    /**
     * Gets the tipomime value for this FicheroDocumentoVO.
     * 
     * @return tipomime
     */
    public java.lang.String getTipomime() {
        return tipomime;
    }


    /**
     * Sets the tipomime value for this FicheroDocumentoVO.
     * 
     * @param tipomime
     */
    public void setTipomime(java.lang.String tipomime) {
        this.tipomime = tipomime;
    }


    /**
     * Gets the bytes value for this FicheroDocumentoVO.
     * 
     * @return bytes
     */
    public byte[] getBytes() {
        return bytes;
    }


    /**
     * Sets the bytes value for this FicheroDocumentoVO.
     * 
     * @param bytes
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FicheroDocumentoVO)) return false;
        FicheroDocumentoVO other = (FicheroDocumentoVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.nombreDoc==null && other.getNombreDoc()==null) || 
             (this.nombreDoc!=null &&
              this.nombreDoc.equals(other.getNombreDoc()))) &&
            ((this.tipomime==null && other.getTipomime()==null) || 
             (this.tipomime!=null &&
              this.tipomime.equals(other.getTipomime()))) &&
            ((this.bytes==null && other.getBytes()==null) || 
             (this.bytes!=null &&
              java.util.Arrays.equals(this.bytes, other.getBytes())));
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
        if (getNombreDoc() != null) {
            _hashCode += getNombreDoc().hashCode();
        }
        if (getTipomime() != null) {
            _hashCode += getTipomime().hashCode();
        }
        if (getBytes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBytes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBytes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FicheroDocumentoVO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "FicheroDocumentoVO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreDoc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombreDoc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipomime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipomime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bytes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bytes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(true);
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
