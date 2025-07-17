/**
 * RespuestasTramitacionVO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.documentos.bd.datos;

public class RespuestasTramitacionVO  implements java.io.Serializable {
    private es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO expediente;

    private es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO idExpediente;

    private es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO idtramite;

    private es.altia.flexiaWS.tramitacion.bd.datos.SalidaBoolean salida;

    private es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO tramite;

    private es.altia.flexiaWS.documentos.bd.datos.TramiteVO[] tramitesIniciados;

    public RespuestasTramitacionVO() {
    }

    public RespuestasTramitacionVO(
           es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO expediente,
           es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO idExpediente,
           es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO idtramite,
           es.altia.flexiaWS.tramitacion.bd.datos.SalidaBoolean salida,
           es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO tramite,
           es.altia.flexiaWS.documentos.bd.datos.TramiteVO[] tramitesIniciados) {
           this.expediente = expediente;
           this.idExpediente = idExpediente;
           this.idtramite = idtramite;
           this.salida = salida;
           this.tramite = tramite;
           this.tramitesIniciados = tramitesIniciados;
    }


    /**
     * Gets the expediente value for this RespuestasTramitacionVO.
     * 
     * @return expediente
     */
    public es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO getExpediente() {
        return expediente;
    }


    /**
     * Sets the expediente value for this RespuestasTramitacionVO.
     * 
     * @param expediente
     */
    public void setExpediente(es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO expediente) {
        this.expediente = expediente;
    }


    /**
     * Gets the idExpediente value for this RespuestasTramitacionVO.
     * 
     * @return idExpediente
     */
    public es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO getIdExpediente() {
        return idExpediente;
    }


    /**
     * Sets the idExpediente value for this RespuestasTramitacionVO.
     * 
     * @param idExpediente
     */
    public void setIdExpediente(es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO idExpediente) {
        this.idExpediente = idExpediente;
    }


    /**
     * Gets the idtramite value for this RespuestasTramitacionVO.
     * 
     * @return idtramite
     */
    public es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO getIdtramite() {
        return idtramite;
    }


    /**
     * Sets the idtramite value for this RespuestasTramitacionVO.
     * 
     * @param idtramite
     */
    public void setIdtramite(es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO idtramite) {
        this.idtramite = idtramite;
    }


    /**
     * Gets the salida value for this RespuestasTramitacionVO.
     * 
     * @return salida
     */
    public es.altia.flexiaWS.tramitacion.bd.datos.SalidaBoolean getSalida() {
        return salida;
    }


    /**
     * Sets the salida value for this RespuestasTramitacionVO.
     * 
     * @param salida
     */
    public void setSalida(es.altia.flexiaWS.tramitacion.bd.datos.SalidaBoolean salida) {
        this.salida = salida;
    }


    /**
     * Gets the tramite value for this RespuestasTramitacionVO.
     * 
     * @return tramite
     */
    public es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO getTramite() {
        return tramite;
    }


    /**
     * Sets the tramite value for this RespuestasTramitacionVO.
     * 
     * @param tramite
     */
    public void setTramite(es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO tramite) {
        this.tramite = tramite;
    }


    /**
     * Gets the tramitesIniciados value for this RespuestasTramitacionVO.
     * 
     * @return tramitesIniciados
     */
    public es.altia.flexiaWS.documentos.bd.datos.TramiteVO[] getTramitesIniciados() {
        return tramitesIniciados;
    }


    /**
     * Sets the tramitesIniciados value for this RespuestasTramitacionVO.
     * 
     * @param tramitesIniciados
     */
    public void setTramitesIniciados(es.altia.flexiaWS.documentos.bd.datos.TramiteVO[] tramitesIniciados) {
        this.tramitesIniciados = tramitesIniciados;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RespuestasTramitacionVO)) return false;
        RespuestasTramitacionVO other = (RespuestasTramitacionVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.expediente==null && other.getExpediente()==null) || 
             (this.expediente!=null &&
              this.expediente.equals(other.getExpediente()))) &&
            ((this.idExpediente==null && other.getIdExpediente()==null) || 
             (this.idExpediente!=null &&
              this.idExpediente.equals(other.getIdExpediente()))) &&
            ((this.idtramite==null && other.getIdtramite()==null) || 
             (this.idtramite!=null &&
              this.idtramite.equals(other.getIdtramite()))) &&
            ((this.salida==null && other.getSalida()==null) || 
             (this.salida!=null &&
              this.salida.equals(other.getSalida()))) &&
            ((this.tramite==null && other.getTramite()==null) || 
             (this.tramite!=null &&
              this.tramite.equals(other.getTramite()))) &&
            ((this.tramitesIniciados==null && other.getTramitesIniciados()==null) || 
             (this.tramitesIniciados!=null &&
              java.util.Arrays.equals(this.tramitesIniciados, other.getTramitesIniciados())));
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
        if (getExpediente() != null) {
            _hashCode += getExpediente().hashCode();
        }
        if (getIdExpediente() != null) {
            _hashCode += getIdExpediente().hashCode();
        }
        if (getIdtramite() != null) {
            _hashCode += getIdtramite().hashCode();
        }
        if (getSalida() != null) {
            _hashCode += getSalida().hashCode();
        }
        if (getTramite() != null) {
            _hashCode += getTramite().hashCode();
        }
        if (getTramitesIniciados() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTramitesIniciados());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTramitesIniciados(), i);
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
        new org.apache.axis.description.TypeDesc(RespuestasTramitacionVO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "RespuestasTramitacionVO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expediente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "expediente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "ExpedienteVO"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idExpediente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idExpediente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "ExpedienteVO"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idtramite");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idtramite"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "TramiteVO"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("salida");
        elemField.setXmlName(new javax.xml.namespace.QName("", "salida"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "SalidaBoolean"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tramite");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tramite"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "TramiteVO"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tramitesIniciados");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tramitesIniciados"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "TramiteVO"));
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
