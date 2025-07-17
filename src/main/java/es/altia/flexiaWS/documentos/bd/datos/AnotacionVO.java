/**
 * AnotacionVO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.documentos.bd.datos;

public class AnotacionVO  implements java.io.Serializable {
    private java.lang.Integer departamento;

    private java.lang.Integer ejercicio;

    private java.lang.Long numero;

    private java.lang.String tipo;

    private java.lang.Integer unidadRegistro;

    public AnotacionVO() {
    }

    public AnotacionVO(
           java.lang.Integer departamento,
           java.lang.Integer ejercicio,
           java.lang.Long numero,
           java.lang.String tipo,
           java.lang.Integer unidadRegistro) {
           this.departamento = departamento;
           this.ejercicio = ejercicio;
           this.numero = numero;
           this.tipo = tipo;
           this.unidadRegistro = unidadRegistro;
    }


    /**
     * Gets the departamento value for this AnotacionVO.
     * 
     * @return departamento
     */
    public java.lang.Integer getDepartamento() {
        return departamento;
    }


    /**
     * Sets the departamento value for this AnotacionVO.
     * 
     * @param departamento
     */
    public void setDepartamento(java.lang.Integer departamento) {
        this.departamento = departamento;
    }


    /**
     * Gets the ejercicio value for this AnotacionVO.
     * 
     * @return ejercicio
     */
    public java.lang.Integer getEjercicio() {
        return ejercicio;
    }


    /**
     * Sets the ejercicio value for this AnotacionVO.
     * 
     * @param ejercicio
     */
    public void setEjercicio(java.lang.Integer ejercicio) {
        this.ejercicio = ejercicio;
    }


    /**
     * Gets the numero value for this AnotacionVO.
     * 
     * @return numero
     */
    public java.lang.Long getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this AnotacionVO.
     * 
     * @param numero
     */
    public void setNumero(java.lang.Long numero) {
        this.numero = numero;
    }


    /**
     * Gets the tipo value for this AnotacionVO.
     * 
     * @return tipo
     */
    public java.lang.String getTipo() {
        return tipo;
    }


    /**
     * Sets the tipo value for this AnotacionVO.
     * 
     * @param tipo
     */
    public void setTipo(java.lang.String tipo) {
        this.tipo = tipo;
    }


    /**
     * Gets the unidadRegistro value for this AnotacionVO.
     * 
     * @return unidadRegistro
     */
    public java.lang.Integer getUnidadRegistro() {
        return unidadRegistro;
    }


    /**
     * Sets the unidadRegistro value for this AnotacionVO.
     * 
     * @param unidadRegistro
     */
    public void setUnidadRegistro(java.lang.Integer unidadRegistro) {
        this.unidadRegistro = unidadRegistro;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AnotacionVO)) return false;
        AnotacionVO other = (AnotacionVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.departamento==null && other.getDepartamento()==null) || 
             (this.departamento!=null &&
              this.departamento.equals(other.getDepartamento()))) &&
            ((this.ejercicio==null && other.getEjercicio()==null) || 
             (this.ejercicio!=null &&
              this.ejercicio.equals(other.getEjercicio()))) &&
            ((this.numero==null && other.getNumero()==null) || 
             (this.numero!=null &&
              this.numero.equals(other.getNumero()))) &&
            ((this.tipo==null && other.getTipo()==null) || 
             (this.tipo!=null &&
              this.tipo.equals(other.getTipo()))) &&
            ((this.unidadRegistro==null && other.getUnidadRegistro()==null) || 
             (this.unidadRegistro!=null &&
              this.unidadRegistro.equals(other.getUnidadRegistro())));
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
        if (getDepartamento() != null) {
            _hashCode += getDepartamento().hashCode();
        }
        if (getEjercicio() != null) {
            _hashCode += getEjercicio().hashCode();
        }
        if (getNumero() != null) {
            _hashCode += getNumero().hashCode();
        }
        if (getTipo() != null) {
            _hashCode += getTipo().hashCode();
        }
        if (getUnidadRegistro() != null) {
            _hashCode += getUnidadRegistro().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AnotacionVO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "AnotacionVO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("departamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "departamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ejercicio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ejercicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unidadRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "unidadRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
