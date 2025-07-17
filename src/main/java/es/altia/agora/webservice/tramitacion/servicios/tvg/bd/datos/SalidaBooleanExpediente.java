/**
 * SalidaBooleanExpediente.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos;

public class SalidaBooleanExpediente  implements java.io.Serializable {
    private java.lang.String codigoTramite;

    private java.lang.String incidencias;

    private java.lang.String numExpediente;

    private java.lang.String ocurrenciaTramite;

    private java.lang.Boolean resultado;

    public SalidaBooleanExpediente() {
    }

    public SalidaBooleanExpediente(
           java.lang.String codigoTramite,
           java.lang.String incidencias,
           java.lang.String numExpediente,
           java.lang.String ocurrenciaTramite,
           java.lang.Boolean resultado) {
           this.codigoTramite = codigoTramite;
           this.incidencias = incidencias;
           this.numExpediente = numExpediente;
           this.ocurrenciaTramite = ocurrenciaTramite;
           this.resultado = resultado;
    }


    /**
     * Gets the codigoTramite value for this SalidaBooleanExpediente.
     * 
     * @return codigoTramite
     */
    public java.lang.String getCodigoTramite() {
        return codigoTramite;
    }


    /**
     * Sets the codigoTramite value for this SalidaBooleanExpediente.
     * 
     * @param codigoTramite
     */
    public void setCodigoTramite(java.lang.String codigoTramite) {
        this.codigoTramite = codigoTramite;
    }


    /**
     * Gets the incidencias value for this SalidaBooleanExpediente.
     * 
     * @return incidencias
     */
    public java.lang.String getIncidencias() {
        return incidencias;
    }


    /**
     * Sets the incidencias value for this SalidaBooleanExpediente.
     * 
     * @param incidencias
     */
    public void setIncidencias(java.lang.String incidencias) {
        this.incidencias = incidencias;
    }


    /**
     * Gets the numExpediente value for this SalidaBooleanExpediente.
     * 
     * @return numExpediente
     */
    public java.lang.String getNumExpediente() {
        return numExpediente;
    }


    /**
     * Sets the numExpediente value for this SalidaBooleanExpediente.
     * 
     * @param numExpediente
     */
    public void setNumExpediente(java.lang.String numExpediente) {
        this.numExpediente = numExpediente;
    }


    /**
     * Gets the ocurrenciaTramite value for this SalidaBooleanExpediente.
     * 
     * @return ocurrenciaTramite
     */
    public java.lang.String getOcurrenciaTramite() {
        return ocurrenciaTramite;
    }


    /**
     * Sets the ocurrenciaTramite value for this SalidaBooleanExpediente.
     * 
     * @param ocurrenciaTramite
     */
    public void setOcurrenciaTramite(java.lang.String ocurrenciaTramite) {
        this.ocurrenciaTramite = ocurrenciaTramite;
    }


    /**
     * Gets the resultado value for this SalidaBooleanExpediente.
     * 
     * @return resultado
     */
    public java.lang.Boolean getResultado() {
        return resultado;
    }


    /**
     * Sets the resultado value for this SalidaBooleanExpediente.
     * 
     * @param resultado
     */
    public void setResultado(java.lang.Boolean resultado) {
        this.resultado = resultado;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SalidaBooleanExpediente)) return false;
        SalidaBooleanExpediente other = (SalidaBooleanExpediente) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigoTramite==null && other.getCodigoTramite()==null) || 
             (this.codigoTramite!=null &&
              this.codigoTramite.equals(other.getCodigoTramite()))) &&
            ((this.incidencias==null && other.getIncidencias()==null) || 
             (this.incidencias!=null &&
              this.incidencias.equals(other.getIncidencias()))) &&
            ((this.numExpediente==null && other.getNumExpediente()==null) || 
             (this.numExpediente!=null &&
              this.numExpediente.equals(other.getNumExpediente()))) &&
            ((this.ocurrenciaTramite==null && other.getOcurrenciaTramite()==null) || 
             (this.ocurrenciaTramite!=null &&
              this.ocurrenciaTramite.equals(other.getOcurrenciaTramite()))) &&
            ((this.resultado==null && other.getResultado()==null) || 
             (this.resultado!=null &&
              this.resultado.equals(other.getResultado())));
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
        if (getCodigoTramite() != null) {
            _hashCode += getCodigoTramite().hashCode();
        }
        if (getIncidencias() != null) {
            _hashCode += getIncidencias().hashCode();
        }
        if (getNumExpediente() != null) {
            _hashCode += getNumExpediente().hashCode();
        }
        if (getOcurrenciaTramite() != null) {
            _hashCode += getOcurrenciaTramite().hashCode();
        }
        if (getResultado() != null) {
            _hashCode += getResultado().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SalidaBooleanExpediente.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://datos.bd.tvg.servicios.tramitacion.webservice.agora.altia.es", "SalidaBooleanExpediente"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoTramite");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoTramite"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incidencias");
        elemField.setXmlName(new javax.xml.namespace.QName("", "incidencias"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numExpediente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numExpediente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ocurrenciaTramite");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ocurrenciaTramite"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resultado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
