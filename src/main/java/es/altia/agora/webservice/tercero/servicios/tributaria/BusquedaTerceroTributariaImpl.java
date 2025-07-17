package es.altia.agora.webservice.tercero.servicios.tributaria;

import es.altia.agora.webservice.tercero.FachadaBusquedaTercero;
import es.altia.agora.webservice.tercero.FachadaSGETercero;
import es.altia.agora.webservice.tercero.servicios.tributaria.cliente.WSBusquedaTerceroServiceLocator;
import es.altia.agora.webservice.tercero.servicios.tributaria.cliente.DatosTerceroVO;
import es.altia.agora.webservice.tercero.servicios.tributaria.cliente.WSBusquedaTerceroPort;
import es.altia.agora.webservice.tercero.servicios.tributaria.cliente.DatosDomicilioVO;
import es.altia.agora.webservice.tercero.exception.EjecucionBusquedaTerceroException;
import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.mantenimiento.MunicipioVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import java.util.Vector;
import java.net.URL;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.rpc.ServiceException;

public class BusquedaTerceroTributariaImpl implements FachadaBusquedaTercero {

    private String nombreServicio;
    private String prefijoPropiedad;
    private String[] paramsConexionBD;
    protected static Log m_Log = LogFactory.getLog(BusquedaTerceroTributariaImpl.class.getName());
    private FachadaSGETercero fachadaSGE = new FachadaSGETercero();

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


    public Vector getTercero(CondicionesBusquedaTerceroVO condsBusqueda, String[] params) throws EjecucionBusquedaTerceroException {

        paramsConexionBD = params;
        Config m_ConfigTerceros = ConfigServiceHelper.getConfig("Terceros");

        String strUrlEndPoint = m_ConfigTerceros.getString(prefijoPropiedad + "urlEndPoint");

        String doi = condsBusqueda.getDocumento();
        String tipoDocumento = Integer.toString(condsBusqueda.getTipoDocumento());
        String nombre = condsBusqueda.getNombre();
        String apellido1 = condsBusqueda.getApellido1();
        String apellido2 = condsBusqueda.getApellido2();

        try {
            URL urlEndPoint = new URL(strUrlEndPoint);
            WSBusquedaTerceroServiceLocator terceroTributariaLocator = new WSBusquedaTerceroServiceLocator();
            WSBusquedaTerceroPort terceroTributariaService = terceroTributariaLocator.getWSBusquedaTerceroEndPoint(urlEndPoint);
            DatosTerceroVO[] resultadoSW = terceroTributariaService.findTercero(doi, tipoDocumento, nombre, apellido1, apellido2);
            return transformarTerceroArraySW(resultadoSW);

        } catch (ServiceException se) {
            throw new EjecucionBusquedaTerceroException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", se);
        } catch (RemoteException re) {
            throw new EjecucionBusquedaTerceroException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", re);
        } catch (MalformedURLException mue) {
            throw new EjecucionBusquedaTerceroException("NO SE HA PODIDO REALIZAR LA LLAMADA AL SERVICIO WEB", mue);
        }
    }

    private Vector<TercerosValueObject> transformarTerceroArraySW(DatosTerceroVO[] resultadoSW) {

        Vector<TercerosValueObject> arrayTerceroSGE = new Vector<TercerosValueObject>();
        for (DatosTerceroVO terceroSW: resultadoSW) {
            arrayTerceroSGE.add(transformarTerceroSW(terceroSW));
        }
        return arrayTerceroSGE;
    }

    private TercerosValueObject transformarTerceroSW(DatosTerceroVO terceroSW) {

        TercerosValueObject terceroSGE = new TercerosValueObject();
        terceroSGE.setApellido1(sustituirComillas(terceroSW.getApellido1()));
        terceroSGE.setApellido2(sustituirComillas(terceroSW.getApellido2()));
        terceroSGE.setNombre(sustituirComillas(terceroSW.getNombre()));
        terceroSGE.setIdentificador("0");
        terceroSGE.setTelefono(terceroSW.getTelefono());
        //Comporbar el tipo de docuemento
        if((terceroSW.getTipoDocumento()).equals("4")) terceroSGE.setTipoDocumento("4"); //Es CIF
        else terceroSGE.setTipoDocumento(compruebaDocumento(terceroSW.getTipoDocumento()));
        
        terceroSGE.setDocumento(terceroSW.getDoi());
        terceroSGE.setOrigen(nombreServicio);
        terceroSGE.setDomicilios(transformarDomicilioArraySW(terceroSW.getDomicilios()));
        terceroSGE.setSituacion('A');

        return terceroSGE;
    }
    private String sustituirComillas(String valor) {
        if (("".equals(valor)) || (valor == null)) return valor;
        else{
            valor = valor.replace('\"', '\'');
            return valor;
        }
    }
    private Vector<DomicilioSimpleValueObject> transformarDomicilioArraySW(DatosDomicilioVO[] resultadoSW) {

        Vector<DomicilioSimpleValueObject> arrayDomicilioSGE = new Vector<DomicilioSimpleValueObject>();
        for (DatosDomicilioVO domicilioSW : resultadoSW) {
            arrayDomicilioSGE.add(transformarDomicilioSW(domicilioSW));
        }
        return arrayDomicilioSGE;
    }

    private DomicilioSimpleValueObject transformarDomicilioSW(DatosDomicilioVO domicilioSW) {

        DomicilioSimpleValueObject domicilioSGE = new DomicilioSimpleValueObject();
        domicilioSGE.setBloque(domicilioSW.getBloque());
        domicilioSGE.setCodigoPostal(domicilioSW.getCodPostal());
        domicilioSGE.setMunicipio(sustituirComillas(domicilioSW.getDescMunicipio()));
        domicilioSGE.setProvincia(domicilioSW.getDescProvincia());
        domicilioSGE.setPais(domicilioSW.getDescPais());
        domicilioSGE.setEscalera(domicilioSW.getEscalera());
        domicilioSGE.setIdDomicilio("0");
        domicilioSGE.setLetraDesde(domicilioSW.getLetraDesde());
        domicilioSGE.setNumDesde(domicilioSW.getNumDesde());
        domicilioSGE.setPlanta(domicilioSW.getPlanta());
        domicilioSGE.setPortal(domicilioSW.getPortal());
        domicilioSGE.setPuerta(domicilioSW.getPuerta());
        domicilioSGE.setOrigen(nombreServicio);

        domicilioSGE.setIdVia("0");
        domicilioSGE.setCodigoVia("0");
        domicilioSGE.setDescVia(sustituirComillas(domicilioSW.getVia().getNombre()));

        GeneralValueObject datosTipoVia = new GeneralValueObject();
        try {
            String abrvTipoViaTrib = domicilioSW.getVia().getDescTipoVia();
            datosTipoVia = fachadaSGE.getTipoViaByAbreviatura(abrvTipoViaTrib, paramsConexionBD);
        } catch (Exception e) {
            datosTipoVia.setAtributo("codTipoVia", "0");
            datosTipoVia.setAtributo("abrvTipoVia", "SV");
            datosTipoVia.setAtributo("descTipoVia", "SIN TIPO VIA");
        }
        domicilioSGE.setIdTipoVia((String)datosTipoVia.getAtributo("codTipoVia"));
        domicilioSGE.setTipoVia((String)datosTipoVia.getAtributo("descTipoVia"));

        try {
            GeneralValueObject infoPais = fachadaSGE.getPaisByDescription(domicilioSW.getDescPais(), paramsConexionBD);
            String codPais = (String)infoPais.getAtributo("codigoPais");
            domicilioSGE.setIdPais(codPais);
            domicilioSGE.setIdPaisVia(codPais);

            if (!codPais.equals("108")) {
                m_Log.debug("SE TRATA DE UN DOMICILIO EXTRANJERO. PONEMOS LA CONFIGURACION PARA DOMICILIOS EXTRANJEROS");
                domicilioSGE.setIdPais("108");
                domicilioSGE.setIdProvincia("66");
                domicilioSGE.setIdMunicipio(codPais);
                domicilioSGE.setIdPaisVia("108");
                domicilioSGE.setIdProvinciaVia("66");
                domicilioSGE.setIdMunicipioVia(codPais);

            } else {
                m_Log.debug("SE TRATA DE UN DOMICILIO EN ESPAÑA. BUSCAMOS LA INFORMACION DE PROVINCIA Y MUNICIPIO");
                GeneralValueObject infoProvincia = fachadaSGE.getProvinciaByPaisAndDesc(Integer.parseInt(codPais),
                        domicilioSW.getDescProvincia(), paramsConexionBD);

                String codProvincia = (String)infoProvincia.getAtributo("codigoProvincia");
                domicilioSGE.setIdProvincia(codProvincia);
                domicilioSGE.setIdProvinciaVia(codProvincia);

                MunicipioVO infoMunicipio = fachadaSGE.getMunicipioByPaisAndProvAndDesc(Integer.parseInt(codPais),
                        Integer.parseInt(codProvincia), domicilioSW.getDescMunicipio(), paramsConexionBD);

                domicilioSGE.setIdMunicipio(Integer.toString(infoMunicipio.getCodigoMunicipio()));
                domicilioSGE.setIdMunicipioVia(Integer.toString(infoMunicipio.getCodigoMunicipio()));

            }

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("NO SE HA PODIDO RECUPERAR ALGUN CODIGO DE PAIS, PROVINCIA O MUNICIPIO. PONEMOS LOS VALORES POR DEFECTO");
            domicilioSGE.setIdPais("108");
            domicilioSGE.setIdProvincia("99");
            domicilioSGE.setIdMunicipio("999");
            domicilioSGE.setIdPaisVia("108");
            domicilioSGE.setIdProvinciaVia("99");
            domicilioSGE.setIdMunicipioVia("999");
        }

        return domicilioSGE;
    }

    private boolean validarNif(String Nif) {

        String valores_numeros="0123456789";
        String correspondencia="TRWAGMYFPDXBNJZSQVHLCKE";
        char letra;
        int longitud=Nif.length();
        Nif=Nif.toUpperCase();

        if(longitud==9)
        {
            letra=Nif.charAt(8);
            int numero=0;
            try
            {
                numero=Integer.parseInt(Nif.substring(0,longitud-1));
            }
            catch (NumberFormatException e)
            {
                return false;
            }
            char letraReal=correspondencia.charAt(numero % 23);

            if(letra==letraReal) return true;
            else return false;

        }
        else return false;

    }

    private boolean validarNie(String Nie)
    {

        int LONGITUD = 9;

    // Si se trata de un NIF
    // Primero comprobamos la longitud, si es distinta de la esperada, rechazamos.
    if (Nie.length() != LONGITUD) {

        return false;
    }

    // Comprobas que el formato se corresponde con el de un NIE
    char primeraLetra = Nie.charAt(0);
    String numero = Nie.substring(1,8);
    char ultimaLetra = Nie.charAt(8);
    int numeros=0;
    try
     {
       numeros=Integer.parseInt(numero);
     }
     catch (NumberFormatException e)
     {
        return false;
     }

    // Comprobamos que la primera letra es X, Y, o Z modificando el numero como corresponda.
    if (primeraLetra =='Y') numeros = numeros + 10000000;
    else if (primeraLetra == 'Z') numeros = numeros + 20000000;
    else if (primeraLetra != 'X') {

        return false;
    }

    // Validamos el caracter de control.
    char letraCorrecta = getLetraNif(numeros);
    if (ultimaLetra != letraCorrecta) {

        return false;
    }
    return true;
    }

    private char getLetraNif(int dni) {
    String lockup = "TRWAGMYFPDXBNJZSQVHLCKE";

    char letraReal=lockup.charAt(dni % 23);
    return letraReal;
    }


    private String compruebaDocumento(String documento)
    {
        if(validarNif(documento)) return ("1");
        else if (validarNie(documento)) return ("3");
        else return ("2"); //Es un pasaporte
    }
}
