package es.altia.agora.webservice.tercero.servicios.pisa;

import es.altia.agora.webservice.tercero.FachadaBusquedaTercero;
import es.altia.agora.webservice.tercero.FachadaSGETercero;
import es.altia.agora.webservice.tercero.servicios.pisa.cliente.*;
import es.altia.agora.webservice.tercero.exception.EjecucionBusquedaTerceroException;
import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.mantenimiento.MunicipioVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import java.util.Vector;
import java.util.HashMap;
import java.net.URL;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.rpc.ServiceException;

public class BusquedaTerceroPisaImpl implements FachadaBusquedaTercero {

    private String nombreServicio;
    private String prefijoPropiedad;
    private String[] paramsConexionBD;
    protected static Log m_Log = LogFactory.getLog(BusquedaTerceroPisaImpl.class.getName());
    private FachadaSGETercero fachadaSGE = new FachadaSGETercero();

    private static int FUNCION_BUSQUEDA_TERCERO = 3;
    private static int FUNCION_BUSQUEDA_DOMICILIO = 4;
    private static String PROP_COD_ORGANIZACION = "codOrganizacion";
    private static String PROP_COD_ENTIDAD = "codEntidad";
    private static String PROP_URL_SERVICE = "urlEndPoint";
    private static String FICHERO_TIPO_DOCUMENTOS = "es.altia.agora.webservice.tercero.servicios.pisa.TiposDocumentosPisa";
    private static String PREFIX_TIPO_DOCUMENTO_SGE = "TipoDocumento/SGE/";
    private static String PREFIX_TIPO_DOCUMENTO_PISA = "TipoDocumento/Pisa/";
    private static String COD_PAIS_ESPANHA = "108";
    private static String DESC_PAIS_ESPANHA = "ESPAÑA";

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getPrefijoPropiedad() {
        return prefijoPropiedad;
    }

    public void setPrefijoPropiedad(String prefijoPropiedad) {
        this.prefijoPropiedad = prefijoPropiedad;
    }

    public Vector getTercero(CondicionesBusquedaTerceroVO condsBusqueda, String[] params)
            throws EjecucionBusquedaTerceroException {

        this.paramsConexionBD = params;

        Config m_ConfigTerceros = ConfigServiceHelper.getConfig("Terceros");
        Config m_TipoDocs = ConfigServiceHelper.getConfig(FICHERO_TIPO_DOCUMENTOS);

        int intTipoDocumento = condsBusqueda.getTipoDocumento();
        String tipoDocumentoSGE;
        if (intTipoDocumento == -1) tipoDocumentoSGE = "";
        else tipoDocumentoSGE = Integer.toString(intTipoDocumento);

        String tipoDocumentoPisa = "";
        if (!tipoDocumentoSGE.equals(""))
            tipoDocumentoPisa = m_TipoDocs.getString(PREFIX_TIPO_DOCUMENTO_SGE + tipoDocumentoSGE);

        String documento = condsBusqueda.getDocumento();
        if (documento == null) documento = "";

        String nombre = condsBusqueda.getNombre();
        if (nombre == null) nombre = "";

        String apellido1 = condsBusqueda.getApellido1();
        if (apellido1 == null) apellido1 = "";

        String apellido2 = condsBusqueda.getApellido2();
        if (apellido2 == null) apellido2 = "";

        SWPisa_TercerosBean terceroABuscar = new SWPisa_TercerosBean();
        terceroABuscar.setTipo_documento(tipoDocumentoPisa);
        terceroABuscar.setDocumento(documento);
        terceroABuscar.setNombre(nombre);
        terceroABuscar.setApellido1(apellido1);
        terceroABuscar.setApellido2(apellido2);
        terceroABuscar.setCodigo_tercero("");
        terceroABuscar.setEmail("");
        terceroABuscar.setTelefono("");

        SWPisa_ParametrosBean parametrosLlamada = new SWPisa_ParametrosBean();
        String codOrganizacion = m_ConfigTerceros.getString(prefijoPropiedad + PROP_COD_ORGANIZACION);
        parametrosLlamada.setOrganizacion(codOrganizacion);
        String codEntidad = m_ConfigTerceros.getString(prefijoPropiedad + PROP_COD_ENTIDAD);
        parametrosLlamada.setEntidad(codEntidad);
        parametrosLlamada.setTercero(terceroABuscar);

        try {
            String strUrlPisa = m_ConfigTerceros.getString(prefijoPropiedad + PROP_URL_SERVICE);
            URL urlPisa = new URL(strUrlPisa);

            SWPisaServiceLocator locatorPisa = new SWPisaServiceLocator();
            SWPisa servicioPisa = locatorPisa.getPisa(urlPisa);

            SWPisa_RetornoBean retornoTerceros = servicioPisa.SWPisa(FUNCION_BUSQUEDA_TERCERO, parametrosLlamada);

            if (!retornoTerceros.isResultado()) {
                m_Log.error("SE HA PRODUCIDO UN ERROR EN LA BUSQUEDA DE TERCEROS EN EL SERVICIO " + nombreServicio);
                m_Log.debug("CODIGO: " + retornoTerceros.getError() + " | DESCRIPCION: " + retornoTerceros.getTextoError());
                throw new EjecucionBusquedaTerceroException("SE HA PRODUCIDO UN ERROR EN LA BUSQUEDA DE TERCEROS", new Exception());
            }

            SWPisa_RetornoBean retornoDoms = servicioPisa.SWPisa(FUNCION_BUSQUEDA_DOMICILIO, parametrosLlamada);

            if (!retornoDoms.isResultado()) {
                m_Log.error("SE HA PRODUCIDO UN ERROR EN LA BUSQUEDA DE DOMICILIOS EN EL SERVICIO " + nombreServicio);
                m_Log.debug("CODIGO: " + retornoDoms.getError() + " | DESCRIPCION: " + retornoDoms.getTextoError());
                throw new EjecucionBusquedaTerceroException("SE HA PRODUCIDO UN ERROR EN LA BUSQUEDA DE DOMICILIOS", new Exception());
            }

            HashMap<String, TercerosValueObject> mapaTerceros = new HashMap<String, TercerosValueObject>();
            for (SWPisa_TercerosBean terceroSW : retornoTerceros.getTerceros()) {
                TercerosValueObject terceroSGE = transformarTerceroSW(terceroSW);
                mapaTerceros.put(terceroSW.getCodigo_tercero(), terceroSGE);
            }

            for (SWPisa_DomiciliosBean domicilioSW : retornoDoms.getDomicilios()) {
                if (domicilioSW.getCodigo_tercero() != null) {
                    DomicilioSimpleValueObject domicilioSGE = transformarDomicilioSW(domicilioSW);
                    String codTercero = domicilioSW.getCodigo_tercero();
                    TercerosValueObject terceroSGE = mapaTerceros.get(codTercero);
                    terceroSGE.getDomicilios().add(domicilioSGE);
                }
            }

            return new Vector<TercerosValueObject>(mapaTerceros.values());

        } catch (ServiceException se) {
            se.printStackTrace();
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (RemoteException re) {
            re.printStackTrace();
        }


        return new Vector();
    }

    private TercerosValueObject transformarTerceroSW(SWPisa_TercerosBean terceroSW) {

        Config m_TipoDocs = ConfigServiceHelper.getConfig(FICHERO_TIPO_DOCUMENTOS);

        TercerosValueObject terceroSGE = new TercerosValueObject();
        terceroSGE.setApellido1(terceroSW.getApellido1());
        terceroSGE.setApellido2(terceroSW.getApellido2());
        terceroSGE.setNombre(terceroSW.getNombre());
        terceroSGE.setIdentificador("0");
        terceroSGE.setTelefono(terceroSW.getTelefono());
        terceroSGE.setEmail(terceroSW.getEmail());

        String tipoDocumentoPisa = terceroSW.getTipo_documento();
        String tipoDocumentoSGE = m_TipoDocs.getString(PREFIX_TIPO_DOCUMENTO_PISA + tipoDocumentoPisa);
        terceroSGE.setTipoDocumento(tipoDocumentoSGE);
        terceroSGE.setDocumento(terceroSW.getDocumento());

        terceroSGE.setDomicilios(new Vector());
        terceroSGE.setOrigen(nombreServicio);
        terceroSGE.setSituacion('A');

        return terceroSGE;
    }

    private DomicilioSimpleValueObject transformarDomicilioSW(SWPisa_DomiciliosBean domicilioSW) {

        DomicilioSimpleValueObject domicilioSGE = new DomicilioSimpleValueObject();

        // No se devuelven datos de via.
        domicilioSGE.setIdVia("0");
        domicilioSGE.setCodigoVia("0");
        domicilioSGE.setDescVia("");
        domicilioSGE.setIdTipoVia("");
        domicilioSGE.setTipoVia("");

        // Datos de Territorio.
        domicilioSGE.setPais(DESC_PAIS_ESPANHA);
        domicilioSGE.setIdPais(COD_PAIS_ESPANHA);
        domicilioSGE.setIdPaisVia(COD_PAIS_ESPANHA);

        try {
            m_Log.debug("SE TRATA DE UN DOMICILIO EN ESPAÑA. BUSCAMOS LA INFORMACION DE PROVINCIA Y MUNICIPIO");
            GeneralValueObject infoProvincia = fachadaSGE.getProvinciaByPaisAndCodigo(Integer.parseInt(COD_PAIS_ESPANHA),
                    Integer.parseInt(domicilioSW.getCodigo_provincia()), paramsConexionBD);

            String codProvincia = (String) infoProvincia.getAtributo("codigoProvincia");
            domicilioSGE.setIdProvincia(codProvincia);
            domicilioSGE.setIdProvinciaVia(codProvincia);
            String descProvincia = (String) infoProvincia.getAtributo("nombreProvincia");
            domicilioSGE.setProvincia(descProvincia);

            MunicipioVO infoMunicipio = fachadaSGE.getMunicipioByPaisAndProvAndDesc(Integer.parseInt(COD_PAIS_ESPANHA),
                    Integer.parseInt(codProvincia), domicilioSW.getMunicipio(), paramsConexionBD);

            domicilioSGE.setIdMunicipio(Integer.toString(infoMunicipio.getCodigoMunicipio()));
            domicilioSGE.setIdMunicipioVia(Integer.toString(infoMunicipio.getCodigoMunicipio()));
            domicilioSGE.setMunicipio(infoMunicipio.getNombreOficial());

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("NO SE HA PODIDO RECUPERAR ALGUN CODIGO DE PAIS, PROVINCIA O MUNICIPIO. PONEMOS LOS VALORES POR DEFECTO");
            domicilioSGE.setIdProvincia("99");
            domicilioSGE.setProvincia("DESCONOCIDA");
            domicilioSGE.setIdMunicipio("999");
            domicilioSGE.setMunicipio("DESCONOCIDO");
            domicilioSGE.setIdProvinciaVia("99");
            domicilioSGE.setIdMunicipioVia("999");
        }

        // El unico dato de direccion que hay es la descripcion de la via.
        domicilioSGE.setDomicilio(domicilioSW.getDomicilio());
        domicilioSGE.setBloque("");
        domicilioSGE.setEscalera("");
        domicilioSGE.setIdDomicilio("0");
        domicilioSGE.setLetraDesde("");
        domicilioSGE.setNumDesde("");
        domicilioSGE.setPlanta("");
        domicilioSGE.setPortal("");
        domicilioSGE.setPuerta("");

        domicilioSGE.setCodigoPostal(domicilioSW.getCodigo_postal());

        domicilioSGE.setOrigen(nombreServicio);

        return domicilioSGE;
    }
}
