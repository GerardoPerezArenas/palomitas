/**
 * TipoMetadatos.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos;

public class TipoMetadatos  implements java.io.Serializable {
    private org.apache.axis.types.URI versionNTI;

    private java.lang.String identificador;

    private java.lang.String[] organo;

    private java.util.Calendar fechaCaptura;

    private boolean origenCiudadanoAdministracion;

    /* - EE01 - Original. 	
     * - EE02 - Copia electrónica auténtica con cambio de formato.	
     * - EE03 - Copia electrónica auténtica de documento papel. 	
     * - EE04 - Copia electrónica parcial auténtica.	
     * - EE99 - Otros. */
    private es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.TipoEstadoElaboracion estadoElaboracion;

    /* /*Documentos de decisión* /
     * - TD01 - Resolución.
     * - TD02 - Acuerdo.
     * - TD03 - Contrato.
     * - TD04 - Convenio.
     * - TD05 - Declaración.
     * /*Documentos de transmisión* /
     * - TD06 - Comunicación.
     * - TD07 - Notificación.
     * - TD08 - Publicación.
     * - TD09 - Acuse de recibo.
     * /*Documentos de constancia* /
     * - TD10 - Acta.
     * - TD11 - Certificado.
     * - TD12 - Diligencia.
     * /*Documentos de juicio* /
     * - TD13 - Informe.
     * /*Documentos de ciudadano* /
     * - TD14 - Solicitud.
     * - TD15 - Denuncia.
     * - TD16 - Alegación.
     * - TD17 - Recursos.
     * - TD18 - Comunicación ciudadano.
     * - TD19 - Factura.
     * - TD20 - Otros incautados.
     * /*Otros* /
     * - TD99 - Otros. */
    private es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.TipoDocumental tipoDocumental;

    private org.apache.axis.types.Id id;  // attribute

    public TipoMetadatos() {
    }

    public TipoMetadatos(
           org.apache.axis.types.URI versionNTI,
           java.lang.String identificador,
           java.lang.String[] organo,
           java.util.Calendar fechaCaptura,
           boolean origenCiudadanoAdministracion,
           es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.TipoEstadoElaboracion estadoElaboracion,
           es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.TipoDocumental tipoDocumental,
           org.apache.axis.types.Id id) {
           this.versionNTI = versionNTI;
           this.identificador = identificador;
           this.organo = organo;
           this.fechaCaptura = fechaCaptura;
           this.origenCiudadanoAdministracion = origenCiudadanoAdministracion;
           this.estadoElaboracion = estadoElaboracion;
           this.tipoDocumental = tipoDocumental;
           this.id = id;
    }


    /**
     * Gets the versionNTI value for this TipoMetadatos.
     * 
     * @return versionNTI
     */
    public org.apache.axis.types.URI getVersionNTI() {
        return versionNTI;
    }


    /**
     * Sets the versionNTI value for this TipoMetadatos.
     * 
     * @param versionNTI
     */
    public void setVersionNTI(org.apache.axis.types.URI versionNTI) {
        this.versionNTI = versionNTI;
    }


    /**
     * Gets the identificador value for this TipoMetadatos.
     * 
     * @return identificador
     */
    public java.lang.String getIdentificador() {
        return identificador;
    }


    /**
     * Sets the identificador value for this TipoMetadatos.
     * 
     * @param identificador
     */
    public void setIdentificador(java.lang.String identificador) {
        this.identificador = identificador;
    }


    /**
     * Gets the organo value for this TipoMetadatos.
     * 
     * @return organo
     */
    public java.lang.String[] getOrgano() {
        return organo;
    }


    /**
     * Sets the organo value for this TipoMetadatos.
     * 
     * @param organo
     */
    public void setOrgano(java.lang.String[] organo) {
        this.organo = organo;
    }

    public java.lang.String getOrgano(int i) {
        return this.organo[i];
    }

    public void setOrgano(int i, java.lang.String _value) {
        this.organo[i] = _value;
    }


    /**
     * Gets the fechaCaptura value for this TipoMetadatos.
     * 
     * @return fechaCaptura
     */
    public java.util.Calendar getFechaCaptura() {
        return fechaCaptura;
    }


    /**
     * Sets the fechaCaptura value for this TipoMetadatos.
     * 
     * @param fechaCaptura
     */
    public void setFechaCaptura(java.util.Calendar fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }


    /**
     * Gets the origenCiudadanoAdministracion value for this TipoMetadatos.
     * 
     * @return origenCiudadanoAdministracion
     */
    public boolean isOrigenCiudadanoAdministracion() {
        return origenCiudadanoAdministracion;
    }


    /**
     * Sets the origenCiudadanoAdministracion value for this TipoMetadatos.
     * 
     * @param origenCiudadanoAdministracion
     */
    public void setOrigenCiudadanoAdministracion(boolean origenCiudadanoAdministracion) {
        this.origenCiudadanoAdministracion = origenCiudadanoAdministracion;
    }


    /**
     * Gets the estadoElaboracion value for this TipoMetadatos.
     * 
     * @return estadoElaboracion   * - EE01 - Original. 	
     * - EE02 - Copia electrónica auténtica con cambio de formato.	
     * - EE03 - Copia electrónica auténtica de documento papel. 	
     * - EE04 - Copia electrónica parcial auténtica.	
     * - EE99 - Otros.
     */
    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.TipoEstadoElaboracion getEstadoElaboracion() {
        return estadoElaboracion;
    }


    /**
     * Sets the estadoElaboracion value for this TipoMetadatos.
     * 
     * @param estadoElaboracion   * - EE01 - Original. 	
     * - EE02 - Copia electrónica auténtica con cambio de formato.	
     * - EE03 - Copia electrónica auténtica de documento papel. 	
     * - EE04 - Copia electrónica parcial auténtica.	
     * - EE99 - Otros.
     */
    public void setEstadoElaboracion(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.TipoEstadoElaboracion estadoElaboracion) {
        this.estadoElaboracion = estadoElaboracion;
    }


    /**
     * Gets the tipoDocumental value for this TipoMetadatos.
     * 
     * @return tipoDocumental   * /*Documentos de decisión* /
     * - TD01 - Resolución.
     * - TD02 - Acuerdo.
     * - TD03 - Contrato.
     * - TD04 - Convenio.
     * - TD05 - Declaración.
     * /*Documentos de transmisión* /
     * - TD06 - Comunicación.
     * - TD07 - Notificación.
     * - TD08 - Publicación.
     * - TD09 - Acuse de recibo.
     * /*Documentos de constancia* /
     * - TD10 - Acta.
     * - TD11 - Certificado.
     * - TD12 - Diligencia.
     * /*Documentos de juicio* /
     * - TD13 - Informe.
     * /*Documentos de ciudadano* /
     * - TD14 - Solicitud.
     * - TD15 - Denuncia.
     * - TD16 - Alegación.
     * - TD17 - Recursos.
     * - TD18 - Comunicación ciudadano.
     * - TD19 - Factura.
     * - TD20 - Otros incautados.
     * /*Otros* /
     * - TD99 - Otros.
     */
    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.TipoDocumental getTipoDocumental() {
        return tipoDocumental;
    }


    /**
     * Sets the tipoDocumental value for this TipoMetadatos.
     * 
     * @param tipoDocumental   * /*Documentos de decisión* /
     * - TD01 - Resolución.
     * - TD02 - Acuerdo.
     * - TD03 - Contrato.
     * - TD04 - Convenio.
     * - TD05 - Declaración.
     * /*Documentos de transmisión* /
     * - TD06 - Comunicación.
     * - TD07 - Notificación.
     * - TD08 - Publicación.
     * - TD09 - Acuse de recibo.
     * /*Documentos de constancia* /
     * - TD10 - Acta.
     * - TD11 - Certificado.
     * - TD12 - Diligencia.
     * /*Documentos de juicio* /
     * - TD13 - Informe.
     * /*Documentos de ciudadano* /
     * - TD14 - Solicitud.
     * - TD15 - Denuncia.
     * - TD16 - Alegación.
     * - TD17 - Recursos.
     * - TD18 - Comunicación ciudadano.
     * - TD19 - Factura.
     * - TD20 - Otros incautados.
     * /*Otros* /
     * - TD99 - Otros.
     */
    public void setTipoDocumental(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.documento_e.metadatos.TipoDocumental tipoDocumental) {
        this.tipoDocumental = tipoDocumental;
    }


    /**
     * Gets the id value for this TipoMetadatos.
     * 
     * @return id
     */
    public org.apache.axis.types.Id getId() {
        return id;
    }


    /**
     * Sets the id value for this TipoMetadatos.
     * 
     * @param id
     */
    public void setId(org.apache.axis.types.Id id) {
        this.id = id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TipoMetadatos)) return false;
        TipoMetadatos other = (TipoMetadatos) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.versionNTI==null && other.getVersionNTI()==null) || 
             (this.versionNTI!=null &&
              this.versionNTI.equals(other.getVersionNTI()))) &&
            ((this.identificador==null && other.getIdentificador()==null) || 
             (this.identificador!=null &&
              this.identificador.equals(other.getIdentificador()))) &&
            ((this.organo==null && other.getOrgano()==null) || 
             (this.organo!=null &&
              java.util.Arrays.equals(this.organo, other.getOrgano()))) &&
            ((this.fechaCaptura==null && other.getFechaCaptura()==null) || 
             (this.fechaCaptura!=null &&
              this.fechaCaptura.equals(other.getFechaCaptura()))) &&
            this.origenCiudadanoAdministracion == other.isOrigenCiudadanoAdministracion() &&
            ((this.estadoElaboracion==null && other.getEstadoElaboracion()==null) || 
             (this.estadoElaboracion!=null &&
              this.estadoElaboracion.equals(other.getEstadoElaboracion()))) &&
            ((this.tipoDocumental==null && other.getTipoDocumental()==null) || 
             (this.tipoDocumental!=null &&
              this.tipoDocumental.equals(other.getTipoDocumental()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId())));
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
        if (getVersionNTI() != null) {
            _hashCode += getVersionNTI().hashCode();
        }
        if (getIdentificador() != null) {
            _hashCode += getIdentificador().hashCode();
        }
        if (getOrgano() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOrgano());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOrgano(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getFechaCaptura() != null) {
            _hashCode += getFechaCaptura().hashCode();
        }
        _hashCode += (isOrigenCiudadanoAdministracion() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getEstadoElaboracion() != null) {
            _hashCode += getEstadoElaboracion().hashCode();
        }
        if (getTipoDocumental() != null) {
            _hashCode += getTipoDocumental().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TipoMetadatos.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "TipoMetadatos"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("id");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Id"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "ID"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("versionNTI");
        elemField.setXmlName(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "VersionNTI"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificador");
        elemField.setXmlName(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "Identificador"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "Organo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaCaptura");
        elemField.setXmlName(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "FechaCaptura"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("origenCiudadanoAdministracion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "OrigenCiudadanoAdministracion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estadoElaboracion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "EstadoElaboracion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "TipoEstadoElaboracion"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoDocumental");
        elemField.setXmlName(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "TipoDocumental"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "tipoDocumental"));
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
