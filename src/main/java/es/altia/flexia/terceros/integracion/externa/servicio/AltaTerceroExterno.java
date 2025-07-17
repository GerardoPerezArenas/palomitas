package es.altia.flexia.terceros.integracion.externa.servicio;

import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.flexia.terceros.integracion.externa.excepciones.CamposObligatoriosTerceroExternoException;
import es.altia.flexia.terceros.integracion.externa.excepciones.EjecucionTerceroExternoException;
import es.altia.flexia.terceros.integracion.externa.excepciones.RestriccionTerceroExternoException;
import es.altia.flexia.terceros.integracion.externa.excepciones.SalidaAltaPersonaFisicaException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;


public abstract class AltaTerceroExterno {
    private String NOMBRE_SERVICIO;
    private String codOrganizacion;
    private ArrayList<String> restriccionesTipoDocumentoAlta;
    private ArrayList<String> restriccionesTipoDocumentoMod;
       

    public abstract boolean setTercero(TercerosValueObject terVO,Vector<EstructuraCampo> estructura,Vector valores,GeneralValueObject via,Connection con) throws EjecucionTerceroExternoException, RestriccionTerceroExternoException,CamposObligatoriosTerceroExternoException,SalidaAltaPersonaFisicaException;
    public abstract boolean modificarTercero(TercerosValueObject terVO,Vector<EstructuraCampo> estructura,Vector valores,GeneralValueObject via,Connection con) throws EjecucionTerceroExternoException, RestriccionTerceroExternoException,CamposObligatoriosTerceroExternoException,SalidaAltaPersonaFisicaException;

    /**
     * Devuelve el nombre del servicio de alta de terceros externos
     * @return the NOMBRE_SERVICIO
     */
    public String getNOMBRE_SERVICIO() {
        return NOMBRE_SERVICIO;
    }

    /**
     * Establece el nombre del servicio de alta de terceros externos
     * @param NOMBRE_SERVICIO the NOMBRE_SERVICIO to set
     */
    public void setNOMBRE_SERVICIO(String NOMBRE_SERVICIO) {
        this.NOMBRE_SERVICIO = NOMBRE_SERVICIO;
    }

    /**
     * Devuelve el código de la organización
     * @return the codOrganizacion
     */
    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    /**
     * Establece el código de la organización
     * @param codOrganizacion the codOrganizacion to set
     */
    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }
    
    /**
     * Devuelve una colección con los tipos de documentos de terceros de Flexia, que no se tendrán en cuenta a la hora
     * de dar de alta un tercero en el sistema externo
     * @return the restriccionesTipoDocumentoAlta
     */
    public ArrayList<String> getRestriccionesTipoDocumentoAlta() {
        return restriccionesTipoDocumentoAlta;
    }

    /**
     * Establece una colección con los tipos de documentos de terceros de Flexia, que no se tendrán en cuenta a la hora
     * de dar de alta un tercero en el sistema externo
     * @param restriccionesTipoDocumentoAlta the restriccionesTipoDocumentoAlta to set
     */
    public void setRestriccionesTipoDocumentoAlta(ArrayList<String> restriccionesTipoDocumentoAlta) {
        this.restriccionesTipoDocumentoAlta = restriccionesTipoDocumentoAlta;
    }

   /**
     * Devuelve una colección con los tipos de documentos de terceros de Flexia, que no se tendrán en cuenta a la hora
     * de modificar un tercero en el sistema externo
     * @return the restriccionesTipoDocumentoMod
     */
    public ArrayList<String> getRestriccionesTipoDocumentoMod() {
        return restriccionesTipoDocumentoMod;
    }

   /**
     * Establece una colección con los tipos de documentos de terceros de Flexia, que no se tendrán en cuenta a la hora
     * de modificar un tercero en el sistema externo
     * @param restriccionesTipoDocumentoMod
     */
    public void setRestriccionesTipoDocumentoMod(ArrayList<String> restriccionesTipoDocumentoMod) {
        this.restriccionesTipoDocumentoMod = restriccionesTipoDocumentoMod;
    }

        
}