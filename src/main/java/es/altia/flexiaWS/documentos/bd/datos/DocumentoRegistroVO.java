/**
 * DocumentoRegistroVO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.documentos.bd.datos;

public class DocumentoRegistroVO  implements java.io.Serializable {
    private java.lang.String codDepartamento;

    private java.lang.String codUor;

    private java.lang.String ejercicio;

    private java.util.Calendar fechaDocumento;

    private java.lang.String fichero;

    private java.lang.String nombreDocumento;

    private java.lang.String numeroAnotacion;

    private java.lang.String tipoEntrada;

    private java.lang.String tipoMime;

    private java.lang.String extension;

    private java.lang.String codigoSeguroVerificacion;

    public DocumentoRegistroVO() {
    }

    public DocumentoRegistroVO(
           java.lang.String codDepartamento,
           java.lang.String codUor,
           java.lang.String ejercicio,
           java.util.Calendar fechaDocumento,
           java.lang.String fichero,
           java.lang.String nombreDocumento,
           java.lang.String numeroAnotacion,
           java.lang.String tipoEntrada,
           java.lang.String tipoMime,
           java.lang.String extension,
           java.lang.String codigoSeguroVerificacion) {
           this.codDepartamento = codDepartamento;
           this.codUor = codUor;
           this.ejercicio = ejercicio;
           this.fechaDocumento = fechaDocumento;
           this.fichero = fichero;
           this.nombreDocumento = nombreDocumento;
           this.numeroAnotacion = numeroAnotacion;
           this.tipoEntrada = tipoEntrada;
           this.tipoMime = tipoMime;
           this.extension = extension;
           this.codigoSeguroVerificacion = codigoSeguroVerificacion;
    }


    /**
     * Gets the codDepartamento value for this DocumentoRegistroVO.
     * 
     * @return codDepartamento
     */
    public java.lang.String getCodDepartamento() {
        return codDepartamento;
    }


    /**
     * Sets the codDepartamento value for this DocumentoRegistroVO.
     * 
     * @param codDepartamento
     */
    public void setCodDepartamento(java.lang.String codDepartamento) {
        this.codDepartamento = codDepartamento;
    }


    /**
     * Gets the codUor value for this DocumentoRegistroVO.
     * 
     * @return codUor
     */
    public java.lang.String getCodUor() {
        return codUor;
    }


    /**
     * Sets the codUor value for this DocumentoRegistroVO.
     * 
     * @param codUor
     */
    public void setCodUor(java.lang.String codUor) {
        this.codUor = codUor;
    }


    /**
     * Gets the ejercicio value for this DocumentoRegistroVO.
     * 
     * @return ejercicio
     */
    public java.lang.String getEjercicio() {
        return ejercicio;
    }


    /**
     * Sets the ejercicio value for this DocumentoRegistroVO.
     * 
     * @param ejercicio
     */
    public void setEjercicio(java.lang.String ejercicio) {
        this.ejercicio = ejercicio;
    }


    /**
     * Gets the fechaDocumento value for this DocumentoRegistroVO.
     * 
     * @return fechaDocumento
     */
    public java.util.Calendar getFechaDocumento() {
        return fechaDocumento;
    }


    /**
     * Sets the fechaDocumento value for this DocumentoRegistroVO.
     * 
     * @param fechaDocumento
     */
    public void setFechaDocumento(java.util.Calendar fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }


    /**
     * Gets the fichero value for this DocumentoRegistroVO.
     * 
     * @return fichero
     */
    public java.lang.String getFichero() {
        return fichero;
    }


    /**
     * Sets the fichero value for this DocumentoRegistroVO.
     * 
     * @param fichero
     */
    public void setFichero(java.lang.String fichero) {
        this.fichero = fichero;
    }


    /**
     * Gets the nombreDocumento value for this DocumentoRegistroVO.
     * 
     * @return nombreDocumento
     */
    public java.lang.String getNombreDocumento() {
        return nombreDocumento;
    }


    /**
     * Sets the nombreDocumento value for this DocumentoRegistroVO.
     * 
     * @param nombreDocumento
     */
    public void setNombreDocumento(java.lang.String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }


    /**
     * Gets the numeroAnotacion value for this DocumentoRegistroVO.
     * 
     * @return numeroAnotacion
     */
    public java.lang.String getNumeroAnotacion() {
        return numeroAnotacion;
    }


    /**
     * Sets the numeroAnotacion value for this DocumentoRegistroVO.
     * 
     * @param numeroAnotacion
     */
    public void setNumeroAnotacion(java.lang.String numeroAnotacion) {
        this.numeroAnotacion = numeroAnotacion;
    }


    /**
     * Gets the tipoEntrada value for this DocumentoRegistroVO.
     * 
     * @return tipoEntrada
     */
    public java.lang.String getTipoEntrada() {
        return tipoEntrada;
    }


    /**
     * Sets the tipoEntrada value for this DocumentoRegistroVO.
     * 
     * @param tipoEntrada
     */
    public void setTipoEntrada(java.lang.String tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }


    /**
     * Gets the tipoMime value for this DocumentoRegistroVO.
     * 
     * @return tipoMime
     */
    public java.lang.String getTipoMime() {
        return tipoMime;
    }


    /**
     * Sets the tipoMime value for this DocumentoRegistroVO.
     * 
     * @param tipoMime
     */
    public void setTipoMime(java.lang.String tipoMime) {
        this.tipoMime = tipoMime;
    }


    /**
     * Gets the extension value for this DocumentoRegistroVO.
     * 
     * @return extension
     */
    public java.lang.String getExtension() {
        return extension;
    }


    /**
     * Sets the extension value for this DocumentoRegistroVO.
     * 
     * @param extension
     */
    public void setExtension(java.lang.String extension) {
        this.extension = extension;
    }


    /**
     * Gets the codigoSeguroVerificacion value for this DocumentoRegistroVO.
     * 
     * @return codigoSeguroVerificacion
     */
    public java.lang.String getCodigoSeguroVerificacion() {
        return codigoSeguroVerificacion;
    }


    /**
     * Sets the codigoSeguroVerificacion value for this DocumentoRegistroVO.
     * 
     * @param codigoSeguroVerificacion
     */
    public void setCodigoSeguroVerificacion(java.lang.String codigoSeguroVerificacion) {
        this.codigoSeguroVerificacion = codigoSeguroVerificacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DocumentoRegistroVO)) return false;
        DocumentoRegistroVO other = (DocumentoRegistroVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codDepartamento==null && other.getCodDepartamento()==null) || 
             (this.codDepartamento!=null &&
              this.codDepartamento.equals(other.getCodDepartamento()))) &&
            ((this.codUor==null && other.getCodUor()==null) || 
             (this.codUor!=null &&
              this.codUor.equals(other.getCodUor()))) &&
            ((this.ejercicio==null && other.getEjercicio()==null) || 
             (this.ejercicio!=null &&
              this.ejercicio.equals(other.getEjercicio()))) &&
            ((this.fechaDocumento==null && other.getFechaDocumento()==null) || 
             (this.fechaDocumento!=null &&
              this.fechaDocumento.equals(other.getFechaDocumento()))) &&
            ((this.fichero==null && other.getFichero()==null) || 
             (this.fichero!=null &&
              this.fichero.equals(other.getFichero()))) &&
            ((this.nombreDocumento==null && other.getNombreDocumento()==null) || 
             (this.nombreDocumento!=null &&
              this.nombreDocumento.equals(other.getNombreDocumento()))) &&
            ((this.numeroAnotacion==null && other.getNumeroAnotacion()==null) || 
             (this.numeroAnotacion!=null &&
              this.numeroAnotacion.equals(other.getNumeroAnotacion()))) &&
            ((this.tipoEntrada==null && other.getTipoEntrada()==null) || 
             (this.tipoEntrada!=null &&
              this.tipoEntrada.equals(other.getTipoEntrada()))) &&
            ((this.tipoMime==null && other.getTipoMime()==null) || 
             (this.tipoMime!=null &&
              this.tipoMime.equals(other.getTipoMime()))) &&
            ((this.extension==null && other.getExtension()==null) || 
             (this.extension!=null &&
              this.extension.equals(other.getExtension()))) &&
            ((this.codigoSeguroVerificacion==null && other.getCodigoSeguroVerificacion()==null) || 
             (this.codigoSeguroVerificacion!=null &&
              this.codigoSeguroVerificacion.equals(other.getCodigoSeguroVerificacion())));
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
        if (getCodDepartamento() != null) {
            _hashCode += getCodDepartamento().hashCode();
        }
        if (getCodUor() != null) {
            _hashCode += getCodUor().hashCode();
        }
        if (getEjercicio() != null) {
            _hashCode += getEjercicio().hashCode();
        }
        if (getFechaDocumento() != null) {
            _hashCode += getFechaDocumento().hashCode();
        }
        if (getFichero() != null) {
            _hashCode += getFichero().hashCode();
        }
        if (getNombreDocumento() != null) {
            _hashCode += getNombreDocumento().hashCode();
        }
        if (getNumeroAnotacion() != null) {
            _hashCode += getNumeroAnotacion().hashCode();
        }
        if (getTipoEntrada() != null) {
            _hashCode += getTipoEntrada().hashCode();
        }
        if (getTipoMime() != null) {
            _hashCode += getTipoMime().hashCode();
        }
        if (getExtension() != null) {
            _hashCode += getExtension().hashCode();
        }
        if (getCodigoSeguroVerificacion() != null) {
            _hashCode += getCodigoSeguroVerificacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DocumentoRegistroVO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "DocumentoRegistroVO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codDepartamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codDepartamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codUor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codUor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ejercicio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ejercicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fechaDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fichero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fichero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombreDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroAnotacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroAnotacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoEntrada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoEntrada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoMime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoMime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extension");
        elemField.setXmlName(new javax.xml.namespace.QName("", "extension"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoSeguroVerificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoSeguroVerificacion"));
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
