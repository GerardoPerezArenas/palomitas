/**
 * CondicionFinalizacionVO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.tramitacion.bd.datos;

public class CondicionFinalizacionVO  implements java.io.Serializable {
    private es.altia.flexiaWS.tramitacion.bd.datos.FlujoFinalizacionVO flujoNO;

    private es.altia.flexiaWS.tramitacion.bd.datos.FlujoFinalizacionVO flujoSI;

    private java.lang.String pregunta;

    private java.lang.String respuesta;

    private java.lang.String tipoFinalizacion;

    public CondicionFinalizacionVO() {
    }

    public CondicionFinalizacionVO(
           es.altia.flexiaWS.tramitacion.bd.datos.FlujoFinalizacionVO flujoNO,
           es.altia.flexiaWS.tramitacion.bd.datos.FlujoFinalizacionVO flujoSI,
           java.lang.String pregunta,
           java.lang.String respuesta,
           java.lang.String tipoFinalizacion) {
           this.flujoNO = flujoNO;
           this.flujoSI = flujoSI;
           this.pregunta = pregunta;
           this.respuesta = respuesta;
           this.tipoFinalizacion = tipoFinalizacion;
    }


    /**
     * Gets the flujoNO value for this CondicionFinalizacionVO.
     * 
     * @return flujoNO
     */
    public es.altia.flexiaWS.tramitacion.bd.datos.FlujoFinalizacionVO getFlujoNO() {
        return flujoNO;
    }


    /**
     * Sets the flujoNO value for this CondicionFinalizacionVO.
     * 
     * @param flujoNO
     */
    public void setFlujoNO(es.altia.flexiaWS.tramitacion.bd.datos.FlujoFinalizacionVO flujoNO) {
        this.flujoNO = flujoNO;
    }


    /**
     * Gets the flujoSI value for this CondicionFinalizacionVO.
     * 
     * @return flujoSI
     */
    public es.altia.flexiaWS.tramitacion.bd.datos.FlujoFinalizacionVO getFlujoSI() {
        return flujoSI;
    }


    /**
     * Sets the flujoSI value for this CondicionFinalizacionVO.
     * 
     * @param flujoSI
     */
    public void setFlujoSI(es.altia.flexiaWS.tramitacion.bd.datos.FlujoFinalizacionVO flujoSI) {
        this.flujoSI = flujoSI;
    }


    /**
     * Gets the pregunta value for this CondicionFinalizacionVO.
     * 
     * @return pregunta
     */
    public java.lang.String getPregunta() {
        return pregunta;
    }


    /**
     * Sets the pregunta value for this CondicionFinalizacionVO.
     * 
     * @param pregunta
     */
    public void setPregunta(java.lang.String pregunta) {
        this.pregunta = pregunta;
    }


    /**
     * Gets the respuesta value for this CondicionFinalizacionVO.
     * 
     * @return respuesta
     */
    public java.lang.String getRespuesta() {
        return respuesta;
    }


    /**
     * Sets the respuesta value for this CondicionFinalizacionVO.
     * 
     * @param respuesta
     */
    public void setRespuesta(java.lang.String respuesta) {
        this.respuesta = respuesta;
    }


    /**
     * Gets the tipoFinalizacion value for this CondicionFinalizacionVO.
     * 
     * @return tipoFinalizacion
     */
    public java.lang.String getTipoFinalizacion() {
        return tipoFinalizacion;
    }


    /**
     * Sets the tipoFinalizacion value for this CondicionFinalizacionVO.
     * 
     * @param tipoFinalizacion
     */
    public void setTipoFinalizacion(java.lang.String tipoFinalizacion) {
        this.tipoFinalizacion = tipoFinalizacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CondicionFinalizacionVO)) return false;
        CondicionFinalizacionVO other = (CondicionFinalizacionVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.flujoNO==null && other.getFlujoNO()==null) || 
             (this.flujoNO!=null &&
              this.flujoNO.equals(other.getFlujoNO()))) &&
            ((this.flujoSI==null && other.getFlujoSI()==null) || 
             (this.flujoSI!=null &&
              this.flujoSI.equals(other.getFlujoSI()))) &&
            ((this.pregunta==null && other.getPregunta()==null) || 
             (this.pregunta!=null &&
              this.pregunta.equals(other.getPregunta()))) &&
            ((this.respuesta==null && other.getRespuesta()==null) || 
             (this.respuesta!=null &&
              this.respuesta.equals(other.getRespuesta()))) &&
            ((this.tipoFinalizacion==null && other.getTipoFinalizacion()==null) || 
             (this.tipoFinalizacion!=null &&
              this.tipoFinalizacion.equals(other.getTipoFinalizacion())));
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
        if (getFlujoNO() != null) {
            _hashCode += getFlujoNO().hashCode();
        }
        if (getFlujoSI() != null) {
            _hashCode += getFlujoSI().hashCode();
        }
        if (getPregunta() != null) {
            _hashCode += getPregunta().hashCode();
        }
        if (getRespuesta() != null) {
            _hashCode += getRespuesta().hashCode();
        }
        if (getTipoFinalizacion() != null) {
            _hashCode += getTipoFinalizacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CondicionFinalizacionVO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "CondicionFinalizacionVO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("flujoNO");
        elemField.setXmlName(new javax.xml.namespace.QName("", "flujoNO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "FlujoFinalizacionVO"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("flujoSI");
        elemField.setXmlName(new javax.xml.namespace.QName("", "flujoSI"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "FlujoFinalizacionVO"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pregunta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pregunta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("respuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "respuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoFinalizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoFinalizacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
