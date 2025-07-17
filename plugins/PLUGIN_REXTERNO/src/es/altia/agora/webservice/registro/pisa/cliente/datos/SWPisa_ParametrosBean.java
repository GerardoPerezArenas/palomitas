/**
 * SWPisa_ParametrosBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.registro.pisa.cliente.datos;

public class SWPisa_ParametrosBean  implements java.io.Serializable {
    private es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_AnotacionesBean anotacion;

    private es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_DomiciliosBean domicilio;

    private java.lang.String efecto;

    private java.lang.String ejercicio;

    private java.lang.String entidad;

    private int error;

    private java.lang.String estado;

    private java.lang.String expediente;

    private java.lang.String fechaDesde;

    private java.lang.String fechaHasta;

    private int generar;

    private java.lang.String numeros;

    private java.lang.String organizacion;

    private java.lang.String sml;

    private es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_TercerosBean tercero;

    private java.lang.String textoError;

    private java.lang.String tipo;

    private java.lang.String unidad;

    private es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_ViasBean via;

    public SWPisa_ParametrosBean() {
    }

    public SWPisa_ParametrosBean(
           es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_AnotacionesBean anotacion,
           es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_DomiciliosBean domicilio,
           java.lang.String efecto,
           java.lang.String ejercicio,
           java.lang.String entidad,
           int error,
           java.lang.String estado,
           java.lang.String expediente,
           java.lang.String fechaDesde,
           java.lang.String fechaHasta,
           int generar,
           java.lang.String numeros,
           java.lang.String organizacion,
           java.lang.String sml,
           es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_TercerosBean tercero,
           java.lang.String textoError,
           java.lang.String tipo,
           java.lang.String unidad,
           es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_ViasBean via) {
           this.anotacion = anotacion;
           this.domicilio = domicilio;
           this.efecto = efecto;
           this.ejercicio = ejercicio;
           this.entidad = entidad;
           this.error = error;
           this.estado = estado;
           this.expediente = expediente;
           this.fechaDesde = fechaDesde;
           this.fechaHasta = fechaHasta;
           this.generar = generar;
           this.numeros = numeros;
           this.organizacion = organizacion;
           this.sml = sml;
           this.tercero = tercero;
           this.textoError = textoError;
           this.tipo = tipo;
           this.unidad = unidad;
           this.via = via;
    }


    /**
     * Gets the anotacion value for this SWPisa_ParametrosBean.
     * 
     * @return anotacion
     */
    public es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_AnotacionesBean getAnotacion() {
        return anotacion;
    }


    /**
     * Sets the anotacion value for this SWPisa_ParametrosBean.
     * 
     * @param anotacion
     */
    public void setAnotacion(es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_AnotacionesBean anotacion) {
        this.anotacion = anotacion;
    }


    /**
     * Gets the domicilio value for this SWPisa_ParametrosBean.
     * 
     * @return domicilio
     */
    public es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_DomiciliosBean getDomicilio() {
        return domicilio;
    }


    /**
     * Sets the domicilio value for this SWPisa_ParametrosBean.
     * 
     * @param domicilio
     */
    public void setDomicilio(es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_DomiciliosBean domicilio) {
        this.domicilio = domicilio;
    }


    /**
     * Gets the efecto value for this SWPisa_ParametrosBean.
     * 
     * @return efecto
     */
    public java.lang.String getEfecto() {
        return efecto;
    }


    /**
     * Sets the efecto value for this SWPisa_ParametrosBean.
     * 
     * @param efecto
     */
    public void setEfecto(java.lang.String efecto) {
        this.efecto = efecto;
    }


    /**
     * Gets the ejercicio value for this SWPisa_ParametrosBean.
     * 
     * @return ejercicio
     */
    public java.lang.String getEjercicio() {
        return ejercicio;
    }


    /**
     * Sets the ejercicio value for this SWPisa_ParametrosBean.
     * 
     * @param ejercicio
     */
    public void setEjercicio(java.lang.String ejercicio) {
        this.ejercicio = ejercicio;
    }


    /**
     * Gets the entidad value for this SWPisa_ParametrosBean.
     * 
     * @return entidad
     */
    public java.lang.String getEntidad() {
        return entidad;
    }


    /**
     * Sets the entidad value for this SWPisa_ParametrosBean.
     * 
     * @param entidad
     */
    public void setEntidad(java.lang.String entidad) {
        this.entidad = entidad;
    }


    /**
     * Gets the error value for this SWPisa_ParametrosBean.
     * 
     * @return error
     */
    public int getError() {
        return error;
    }


    /**
     * Sets the error value for this SWPisa_ParametrosBean.
     * 
     * @param error
     */
    public void setError(int error) {
        this.error = error;
    }


    /**
     * Gets the estado value for this SWPisa_ParametrosBean.
     * 
     * @return estado
     */
    public java.lang.String getEstado() {
        return estado;
    }


    /**
     * Sets the estado value for this SWPisa_ParametrosBean.
     * 
     * @param estado
     */
    public void setEstado(java.lang.String estado) {
        this.estado = estado;
    }


    /**
     * Gets the expediente value for this SWPisa_ParametrosBean.
     * 
     * @return expediente
     */
    public java.lang.String getExpediente() {
        return expediente;
    }


    /**
     * Sets the expediente value for this SWPisa_ParametrosBean.
     * 
     * @param expediente
     */
    public void setExpediente(java.lang.String expediente) {
        this.expediente = expediente;
    }


    /**
     * Gets the fechaDesde value for this SWPisa_ParametrosBean.
     * 
     * @return fechaDesde
     */
    public java.lang.String getFechaDesde() {
        return fechaDesde;
    }


    /**
     * Sets the fechaDesde value for this SWPisa_ParametrosBean.
     * 
     * @param fechaDesde
     */
    public void setFechaDesde(java.lang.String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }


    /**
     * Gets the fechaHasta value for this SWPisa_ParametrosBean.
     * 
     * @return fechaHasta
     */
    public java.lang.String getFechaHasta() {
        return fechaHasta;
    }


    /**
     * Sets the fechaHasta value for this SWPisa_ParametrosBean.
     * 
     * @param fechaHasta
     */
    public void setFechaHasta(java.lang.String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }


    /**
     * Gets the generar value for this SWPisa_ParametrosBean.
     * 
     * @return generar
     */
    public int getGenerar() {
        return generar;
    }


    /**
     * Sets the generar value for this SWPisa_ParametrosBean.
     * 
     * @param generar
     */
    public void setGenerar(int generar) {
        this.generar = generar;
    }


    /**
     * Gets the numeros value for this SWPisa_ParametrosBean.
     * 
     * @return numeros
     */
    public java.lang.String getNumeros() {
        return numeros;
    }


    /**
     * Sets the numeros value for this SWPisa_ParametrosBean.
     * 
     * @param numeros
     */
    public void setNumeros(java.lang.String numeros) {
        this.numeros = numeros;
    }


    /**
     * Gets the organizacion value for this SWPisa_ParametrosBean.
     * 
     * @return organizacion
     */
    public java.lang.String getOrganizacion() {
        return organizacion;
    }


    /**
     * Sets the organizacion value for this SWPisa_ParametrosBean.
     * 
     * @param organizacion
     */
    public void setOrganizacion(java.lang.String organizacion) {
        this.organizacion = organizacion;
    }


    /**
     * Gets the sml value for this SWPisa_ParametrosBean.
     * 
     * @return sml
     */
    public java.lang.String getSml() {
        return sml;
    }


    /**
     * Sets the sml value for this SWPisa_ParametrosBean.
     * 
     * @param sml
     */
    public void setSml(java.lang.String sml) {
        this.sml = sml;
    }


    /**
     * Gets the tercero value for this SWPisa_ParametrosBean.
     * 
     * @return tercero
     */
    public es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_TercerosBean getTercero() {
        return tercero;
    }


    /**
     * Sets the tercero value for this SWPisa_ParametrosBean.
     * 
     * @param tercero
     */
    public void setTercero(es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_TercerosBean tercero) {
        this.tercero = tercero;
    }


    /**
     * Gets the textoError value for this SWPisa_ParametrosBean.
     * 
     * @return textoError
     */
    public java.lang.String getTextoError() {
        return textoError;
    }


    /**
     * Sets the textoError value for this SWPisa_ParametrosBean.
     * 
     * @param textoError
     */
    public void setTextoError(java.lang.String textoError) {
        this.textoError = textoError;
    }


    /**
     * Gets the tipo value for this SWPisa_ParametrosBean.
     * 
     * @return tipo
     */
    public java.lang.String getTipo() {
        return tipo;
    }


    /**
     * Sets the tipo value for this SWPisa_ParametrosBean.
     * 
     * @param tipo
     */
    public void setTipo(java.lang.String tipo) {
        this.tipo = tipo;
    }


    /**
     * Gets the unidad value for this SWPisa_ParametrosBean.
     * 
     * @return unidad
     */
    public java.lang.String getUnidad() {
        return unidad;
    }


    /**
     * Sets the unidad value for this SWPisa_ParametrosBean.
     * 
     * @param unidad
     */
    public void setUnidad(java.lang.String unidad) {
        this.unidad = unidad;
    }


    /**
     * Gets the via value for this SWPisa_ParametrosBean.
     * 
     * @return via
     */
    public es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_ViasBean getVia() {
        return via;
    }


    /**
     * Sets the via value for this SWPisa_ParametrosBean.
     * 
     * @param via
     */
    public void setVia(es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_ViasBean via) {
        this.via = via;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SWPisa_ParametrosBean)) return false;
        SWPisa_ParametrosBean other = (SWPisa_ParametrosBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.anotacion==null && other.getAnotacion()==null) || 
             (this.anotacion!=null &&
              this.anotacion.equals(other.getAnotacion()))) &&
            ((this.domicilio==null && other.getDomicilio()==null) || 
             (this.domicilio!=null &&
              this.domicilio.equals(other.getDomicilio()))) &&
            ((this.efecto==null && other.getEfecto()==null) || 
             (this.efecto!=null &&
              this.efecto.equals(other.getEfecto()))) &&
            ((this.ejercicio==null && other.getEjercicio()==null) || 
             (this.ejercicio!=null &&
              this.ejercicio.equals(other.getEjercicio()))) &&
            ((this.entidad==null && other.getEntidad()==null) || 
             (this.entidad!=null &&
              this.entidad.equals(other.getEntidad()))) &&
            this.error == other.getError() &&
            ((this.estado==null && other.getEstado()==null) || 
             (this.estado!=null &&
              this.estado.equals(other.getEstado()))) &&
            ((this.expediente==null && other.getExpediente()==null) || 
             (this.expediente!=null &&
              this.expediente.equals(other.getExpediente()))) &&
            ((this.fechaDesde==null && other.getFechaDesde()==null) || 
             (this.fechaDesde!=null &&
              this.fechaDesde.equals(other.getFechaDesde()))) &&
            ((this.fechaHasta==null && other.getFechaHasta()==null) || 
             (this.fechaHasta!=null &&
              this.fechaHasta.equals(other.getFechaHasta()))) &&
            this.generar == other.getGenerar() &&
            ((this.numeros==null && other.getNumeros()==null) || 
             (this.numeros!=null &&
              this.numeros.equals(other.getNumeros()))) &&
            ((this.organizacion==null && other.getOrganizacion()==null) || 
             (this.organizacion!=null &&
              this.organizacion.equals(other.getOrganizacion()))) &&
            ((this.sml==null && other.getSml()==null) || 
             (this.sml!=null &&
              this.sml.equals(other.getSml()))) &&
            ((this.tercero==null && other.getTercero()==null) || 
             (this.tercero!=null &&
              this.tercero.equals(other.getTercero()))) &&
            ((this.textoError==null && other.getTextoError()==null) || 
             (this.textoError!=null &&
              this.textoError.equals(other.getTextoError()))) &&
            ((this.tipo==null && other.getTipo()==null) || 
             (this.tipo!=null &&
              this.tipo.equals(other.getTipo()))) &&
            ((this.unidad==null && other.getUnidad()==null) || 
             (this.unidad!=null &&
              this.unidad.equals(other.getUnidad()))) &&
            ((this.via==null && other.getVia()==null) || 
             (this.via!=null &&
              this.via.equals(other.getVia())));
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
        if (getAnotacion() != null) {
            _hashCode += getAnotacion().hashCode();
        }
        if (getDomicilio() != null) {
            _hashCode += getDomicilio().hashCode();
        }
        if (getEfecto() != null) {
            _hashCode += getEfecto().hashCode();
        }
        if (getEjercicio() != null) {
            _hashCode += getEjercicio().hashCode();
        }
        if (getEntidad() != null) {
            _hashCode += getEntidad().hashCode();
        }
        _hashCode += getError();
        if (getEstado() != null) {
            _hashCode += getEstado().hashCode();
        }
        if (getExpediente() != null) {
            _hashCode += getExpediente().hashCode();
        }
        if (getFechaDesde() != null) {
            _hashCode += getFechaDesde().hashCode();
        }
        if (getFechaHasta() != null) {
            _hashCode += getFechaHasta().hashCode();
        }
        _hashCode += getGenerar();
        if (getNumeros() != null) {
            _hashCode += getNumeros().hashCode();
        }
        if (getOrganizacion() != null) {
            _hashCode += getOrganizacion().hashCode();
        }
        if (getSml() != null) {
            _hashCode += getSml().hashCode();
        }
        if (getTercero() != null) {
            _hashCode += getTercero().hashCode();
        }
        if (getTextoError() != null) {
            _hashCode += getTextoError().hashCode();
        }
        if (getTipo() != null) {
            _hashCode += getTipo().hashCode();
        }
        if (getUnidad() != null) {
            _hashCode += getUnidad().hashCode();
        }
        if (getVia() != null) {
            _hashCode += getVia().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SWPisa_ParametrosBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_ParametrosBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anotacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anotacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_AnotacionesBean"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("domicilio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "domicilio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_DomiciliosBean"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("efecto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "efecto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ejercicio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ejercicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidad");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("error");
        elemField.setXmlName(new javax.xml.namespace.QName("", "error"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "estado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expediente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "expediente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaDesde");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fechaDesde"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaHasta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fechaHasta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("generar");
        elemField.setXmlName(new javax.xml.namespace.QName("", "generar"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeros");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeros"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "organizacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sml");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sml"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tercero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tercero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_TercerosBean"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("textoError");
        elemField.setXmlName(new javax.xml.namespace.QName("", "textoError"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unidad");
        elemField.setXmlName(new javax.xml.namespace.QName("", "unidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("via");
        elemField.setXmlName(new javax.xml.namespace.QName("", "via"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_ViasBean"));
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
