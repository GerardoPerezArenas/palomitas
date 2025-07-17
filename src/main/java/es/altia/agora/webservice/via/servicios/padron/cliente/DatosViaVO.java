/**
 * DatosViaVO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.via.servicios.padron.cliente;

public class DatosViaVO  implements java.io.Serializable {
    private java.lang.String codMunicipio;

    private java.lang.String codPais;

    private java.lang.String codProvincia;

    private java.lang.String codTipoVia;

    private java.lang.String descMunicipio;

    private java.lang.String descPais;

    private java.lang.String descProvincia;

    private java.lang.String descTipoVia;

    private java.lang.String idVia;

    private java.lang.String nombre;

    public DatosViaVO() {
    }

    public DatosViaVO(
           java.lang.String codMunicipio,
           java.lang.String codPais,
           java.lang.String codProvincia,
           java.lang.String codTipoVia,
           java.lang.String descMunicipio,
           java.lang.String descPais,
           java.lang.String descProvincia,
           java.lang.String descTipoVia,
           java.lang.String idVia,
           java.lang.String nombre) {
           this.codMunicipio = codMunicipio;
           this.codPais = codPais;
           this.codProvincia = codProvincia;
           this.codTipoVia = codTipoVia;
           this.descMunicipio = descMunicipio;
           this.descPais = descPais;
           this.descProvincia = descProvincia;
           this.descTipoVia = descTipoVia;
           this.idVia = idVia;
           this.nombre = nombre;
    }


    /**
     * Gets the codMunicipio value for this DatosViaVO.
     * 
     * @return codMunicipio
     */
    public java.lang.String getCodMunicipio() {
        return codMunicipio;
    }


    /**
     * Sets the codMunicipio value for this DatosViaVO.
     * 
     * @param codMunicipio
     */
    public void setCodMunicipio(java.lang.String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }


    /**
     * Gets the codPais value for this DatosViaVO.
     * 
     * @return codPais
     */
    public java.lang.String getCodPais() {
        return codPais;
    }


    /**
     * Sets the codPais value for this DatosViaVO.
     * 
     * @param codPais
     */
    public void setCodPais(java.lang.String codPais) {
        this.codPais = codPais;
    }


    /**
     * Gets the codProvincia value for this DatosViaVO.
     * 
     * @return codProvincia
     */
    public java.lang.String getCodProvincia() {
        return codProvincia;
    }


    /**
     * Sets the codProvincia value for this DatosViaVO.
     * 
     * @param codProvincia
     */
    public void setCodProvincia(java.lang.String codProvincia) {
        this.codProvincia = codProvincia;
    }


    /**
     * Gets the codTipoVia value for this DatosViaVO.
     * 
     * @return codTipoVia
     */
    public java.lang.String getCodTipoVia() {
        return codTipoVia;
    }


    /**
     * Sets the codTipoVia value for this DatosViaVO.
     * 
     * @param codTipoVia
     */
    public void setCodTipoVia(java.lang.String codTipoVia) {
        this.codTipoVia = codTipoVia;
    }


    /**
     * Gets the descMunicipio value for this DatosViaVO.
     * 
     * @return descMunicipio
     */
    public java.lang.String getDescMunicipio() {
        return descMunicipio;
    }


    /**
     * Sets the descMunicipio value for this DatosViaVO.
     * 
     * @param descMunicipio
     */
    public void setDescMunicipio(java.lang.String descMunicipio) {
        this.descMunicipio = descMunicipio;
    }


    /**
     * Gets the descPais value for this DatosViaVO.
     * 
     * @return descPais
     */
    public java.lang.String getDescPais() {
        return descPais;
    }


    /**
     * Sets the descPais value for this DatosViaVO.
     * 
     * @param descPais
     */
    public void setDescPais(java.lang.String descPais) {
        this.descPais = descPais;
    }


    /**
     * Gets the descProvincia value for this DatosViaVO.
     * 
     * @return descProvincia
     */
    public java.lang.String getDescProvincia() {
        return descProvincia;
    }


    /**
     * Sets the descProvincia value for this DatosViaVO.
     * 
     * @param descProvincia
     */
    public void setDescProvincia(java.lang.String descProvincia) {
        this.descProvincia = descProvincia;
    }


    /**
     * Gets the descTipoVia value for this DatosViaVO.
     * 
     * @return descTipoVia
     */
    public java.lang.String getDescTipoVia() {
        return descTipoVia;
    }


    /**
     * Sets the descTipoVia value for this DatosViaVO.
     * 
     * @param descTipoVia
     */
    public void setDescTipoVia(java.lang.String descTipoVia) {
        this.descTipoVia = descTipoVia;
    }


    /**
     * Gets the idVia value for this DatosViaVO.
     * 
     * @return idVia
     */
    public java.lang.String getIdVia() {
        return idVia;
    }


    /**
     * Sets the idVia value for this DatosViaVO.
     * 
     * @param idVia
     */
    public void setIdVia(java.lang.String idVia) {
        this.idVia = idVia;
    }


    /**
     * Gets the nombre value for this DatosViaVO.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this DatosViaVO.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosViaVO)) return false;
        DatosViaVO other = (DatosViaVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codMunicipio==null && other.getCodMunicipio()==null) || 
             (this.codMunicipio!=null &&
              this.codMunicipio.equals(other.getCodMunicipio()))) &&
            ((this.codPais==null && other.getCodPais()==null) || 
             (this.codPais!=null &&
              this.codPais.equals(other.getCodPais()))) &&
            ((this.codProvincia==null && other.getCodProvincia()==null) || 
             (this.codProvincia!=null &&
              this.codProvincia.equals(other.getCodProvincia()))) &&
            ((this.codTipoVia==null && other.getCodTipoVia()==null) || 
             (this.codTipoVia!=null &&
              this.codTipoVia.equals(other.getCodTipoVia()))) &&
            ((this.descMunicipio==null && other.getDescMunicipio()==null) || 
             (this.descMunicipio!=null &&
              this.descMunicipio.equals(other.getDescMunicipio()))) &&
            ((this.descPais==null && other.getDescPais()==null) || 
             (this.descPais!=null &&
              this.descPais.equals(other.getDescPais()))) &&
            ((this.descProvincia==null && other.getDescProvincia()==null) || 
             (this.descProvincia!=null &&
              this.descProvincia.equals(other.getDescProvincia()))) &&
            ((this.descTipoVia==null && other.getDescTipoVia()==null) || 
             (this.descTipoVia!=null &&
              this.descTipoVia.equals(other.getDescTipoVia()))) &&
            ((this.idVia==null && other.getIdVia()==null) || 
             (this.idVia!=null &&
              this.idVia.equals(other.getIdVia()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre())));
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
        if (getCodMunicipio() != null) {
            _hashCode += getCodMunicipio().hashCode();
        }
        if (getCodPais() != null) {
            _hashCode += getCodPais().hashCode();
        }
        if (getCodProvincia() != null) {
            _hashCode += getCodProvincia().hashCode();
        }
        if (getCodTipoVia() != null) {
            _hashCode += getCodTipoVia().hashCode();
        }
        if (getDescMunicipio() != null) {
            _hashCode += getDescMunicipio().hashCode();
        }
        if (getDescPais() != null) {
            _hashCode += getDescPais().hashCode();
        }
        if (getDescProvincia() != null) {
            _hashCode += getDescProvincia().hashCode();
        }
        if (getDescTipoVia() != null) {
            _hashCode += getDescTipoVia().hashCode();
        }
        if (getIdVia() != null) {
            _hashCode += getIdVia().hashCode();
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosViaVO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://via.ws.altia.es", "DatosViaVO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codMunicipio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codMunicipio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codPais");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codPais"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codProvincia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codProvincia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codTipoVia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codTipoVia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descMunicipio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descMunicipio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descPais");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descPais"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descProvincia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descProvincia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descTipoVia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descTipoVia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idVia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idVia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombre"));
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
