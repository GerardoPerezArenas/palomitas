package es.altia.agora.webservice.tercero.egim;

import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.mantenimiento.MunicipioVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.webservice.tercero.FachadaBusquedaTercero;
import es.altia.agora.webservice.tercero.FachadaSGETercero;
import es.altia.agora.webservice.tercero.exception.EjecucionBusquedaTerceroException;
import es.isap.esijad.pe.ws.PersonaEntidadStub;
import es.isap.esijad.pe.ws.PersonaEntidadStub.Contacto;
import es.isap.esijad.pe.ws.PersonaEntidadStub.Domicilio;
import es.isap.esijad.pe.ws.PersonaEntidadStub.PersonaEntidadResponse;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

/**
 * Plugin de búsqueda de terceros a través del servicio web de terceros de E-gim
 * @author oscar.rodriguez
 */
public class BusquedaTerceroEgimImpl implements FachadaBusquedaTercero {

    private String nombreServicio;
    private String prefijoPropiedad;
    private String[] paramsConexionBD;
    protected static Logger m_Log = Logger.getLogger(BusquedaTerceroEgimImpl.class);
    private FachadaSGETercero fachadaSGE = new FachadaSGETercero();
    
    private static String COD_PAIS_ESPANHA = "108";
    private static String DESC_PAIS_ESPANHA = "ESPAÑA";
    private final String DESCRIPCION_MOVIL_EGIM = "MOVIL";
    private final String DESCRIPCION_FAX_EGIM     = "FAX";
    private final String DESCRIPCION_FIJO_EGIM    = "FIJO";
    private final String DESCRIPCION_EMAIL_EGIM    = "EMAIL";

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

    private String eliminarComodines(String dato){
         return dato.replaceAll("[*]", "");        
    }

    public Vector getTercero(CondicionesBusquedaTerceroVO condsBusqueda, String[] params)
            throws EjecucionBusquedaTerceroException {

        this.paramsConexionBD = params;
        Vector<TercerosValueObject> terceros = new Vector<TercerosValueObject>();
        int codOrganizacion = condsBusqueda.getCodOrganizacion();
        ResourceBundle bundle = ResourceBundle.getBundle("Terceros");
        
        String propiedadUrl = ConstantesDatos.BUSQUEDA_TERCERO + ConstantesDatos.BARRA + codOrganizacion + ConstantesDatos.BARRA +
                ConstantesDatos.PLUGIN_EGIM + ConstantesDatos.BARRA + ConstantesDatos.URL_END_POINT;
        String propiedadUsuario = ConstantesDatos.BUSQUEDA_TERCERO + ConstantesDatos.BARRA + 
                ConstantesDatos.PLUGIN_EGIM + ConstantesDatos.BARRA + ConstantesDatos.BUSQUEDA_TERCERO_CONEXION_EGIM_USUARIO;
        String propiedadPassword = ConstantesDatos.BUSQUEDA_TERCERO + ConstantesDatos.BARRA +
                ConstantesDatos.PLUGIN_EGIM + ConstantesDatos.BARRA + ConstantesDatos.BUSQUEDA_TERCERO_CONEXION_EGIM_PASSWORD;
        String propiedadEntidad = ConstantesDatos.BUSQUEDA_TERCERO + ConstantesDatos.BARRA + 
                ConstantesDatos.PLUGIN_EGIM + ConstantesDatos.BARRA + ConstantesDatos.BUSQUEDA_TERCERO_CONEXION_EGIM_ENTIDAD;
        String propiedadMunicipio = ConstantesDatos.BUSQUEDA_TERCERO + ConstantesDatos.BARRA +
                ConstantesDatos.PLUGIN_EGIM + ConstantesDatos.BARRA + ConstantesDatos.BUSQUEDA_TERCERO_CONEXION_EGIM_MUNICIPIO;
      
        es.isap.esijad.pe.ws.PersonaEntidadStub.PersonaEntidadRequest persona = new es.isap.esijad.pe.ws.PersonaEntidadStub.PersonaEntidadRequest();
        /** SE ESTABLECEN LAS CONDICIONES DE BÚSQUEDA PARA EL SERVICIO WEB DE TERCEROS DE EGIM **/
        int intTipoDocumento = condsBusqueda.getTipoDocumento();
        if(intTipoDocumento!=-1){
            String tipoDocumentoEGim = bundle.getString(ConstantesDatos.BUSQUEDA_TERCERO + ConstantesDatos.BARRA + codOrganizacion + ConstantesDatos.BARRA + ConstantesDatos.PLUGIN_EGIM
                    + ConstantesDatos.BARRA + intTipoDocumento + ConstantesDatos.BARRA + ConstantesDatos.MAPEO_DOCUMENTO_EGIM);
            persona.setTipoDocumentoIdentificativo(tipoDocumentoEGim);
        }else persona.setTipoDocumentoIdentificativo("");
        
        if(condsBusqueda.getDocumento()!=null && condsBusqueda.getDocumento().length()>0){
            persona.setDocumentoIdentificacion(condsBusqueda.getDocumento());
        }else persona.setDocumentoIdentificacion("");

        if(condsBusqueda.getApellido1()!=null && condsBusqueda.getApellido1().length()>0){
            persona.setApellido1(eliminarComodines(condsBusqueda.getApellido1()));
        }else persona.setApellido1("");

        if(condsBusqueda.getApellido2()!=null && condsBusqueda.getApellido2().length()>0){
            persona.setApellido2(eliminarComodines(condsBusqueda.getApellido2()));
        }else persona.setApellido2("");

        if(condsBusqueda.getNombre()!=null && condsBusqueda.getNombre().length()>0){
            if(intTipoDocumento==4 || intTipoDocumento==5)
                persona.setRazonSocial(eliminarComodines(condsBusqueda.getNombre()));
            else{
                persona.setNombre(eliminarComodines(condsBusqueda.getNombre()));                
            }
        }else persona.setNombre("");



        /** SE LLAMA AL SERVICIO WEB **/
        try{

            PersonaEntidadStub stub = new PersonaEntidadStub(bundle.getString(propiedadUrl));
            es.isap.esijad.pe.ws.PersonaEntidadStub.ConsultarPersonaEntidad consulta = new es.isap.esijad.pe.ws.PersonaEntidadStub.ConsultarPersonaEntidad();

            es.isap.esijad.pe.ws.PersonaEntidadStub.DatosConexion conexion = new es.isap.esijad.pe.ws.PersonaEntidadStub.DatosConexion();
            conexion.setEntidad(bundle.getString(propiedadEntidad));
            conexion.setMunicipio(bundle.getString(propiedadMunicipio));
            conexion.setUsuario(bundle.getString(propiedadUsuario));
            conexion.setPassword(bundle.getString(propiedadPassword));
            
            consulta.setPDatosConexion(conexion);
            consulta.setPPersonaEntidad(persona);
            es.isap.esijad.pe.ws.PersonaEntidadStub.ConsultarPersonaEntidadResponse response =  stub.consultarPersonaEntidad(consulta);
            
            es.isap.esijad.pe.ws.PersonaEntidadStub.PersonaEntidadOut personaEntidadOut = response.get_return();
            String codigoRespuesta = personaEntidadOut.getCodigoRespuesta();
            String descripcion = personaEntidadOut.getDescripcion();

            m_Log.debug("=====================> codigoRespuesta: " + codigoRespuesta + ", descripcion: " + descripcion);
            m_Log.debug("=====================> número total de terceros recuperados: " + personaEntidadOut.getNumeroTotal());

            if(codigoRespuesta!=null && codigoRespuesta.equals("ERR_012")){
                // No hay datos
                  return new Vector<TercerosValueObject>();
            }else{

                if(personaEntidadOut.getNumeroTotal()>0){
                    PersonaEntidadResponse[] respuesta = personaEntidadOut.getPersonaEntidadResponse();

                    /** SE MAPEAN LOS TERCEROS QUE DEVUELVE EL WS DE PERSONAS DE EGIM A UN TerceroValueObject **/
                    for(int i=0;respuesta!=null && i<respuesta.length;i++){
                        TercerosValueObject tercero = transformarTerceroSW(respuesta[i],codOrganizacion);
                        terceros.add(tercero);
                    }
                }
              
            }//else

        } catch(AxisFault e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

          return terceros;
    }



    /**
     * Convierte un objeto PersonaEntidadResponse en un TercerosValueObject utilizado en Flexia
     * @param terceroEgim: PersonaEntidadResponse
     * @param codOrganizacion: Código de organización
     * @return TercerosValueObject
     */
    private TercerosValueObject transformarTerceroSW(PersonaEntidadResponse terceroEgim,int codOrganizacion) {
        
        TercerosValueObject terceroFLEXIA = new TercerosValueObject();

        ResourceBundle bundle = ResourceBundle.getBundle("Terceros");
        String nombrePropiedad = ConstantesDatos.BUSQUEDA_TERCERO + ConstantesDatos.BARRA + codOrganizacion +
                ConstantesDatos.BARRA + ConstantesDatos.PLUGIN_EGIM + ConstantesDatos.BARRA
                + ConstantesDatos.NOMBRE_ORIGEN;

        m_Log.debug("nombrePropiedad : " + nombrePropiedad);
        String nombreOrigen = bundle.getString(nombrePropiedad);
        m_Log.debug("nombreOrigen : " + nombreOrigen);

        String apellido1 = "";
        String apellido2 = "";

        if(terceroEgim.getPersonaEntidad().getApellido1()!=null && terceroEgim.getPersonaEntidad().getApellido1().length()>=1)
            apellido1 = terceroEgim.getPersonaEntidad().getApellido1();

        if(terceroEgim.getPersonaEntidad().getApellido2()!=null && terceroEgim.getPersonaEntidad().getApellido2().length()>=1)
            apellido2 = terceroEgim.getPersonaEntidad().getApellido2();
        terceroFLEXIA.setApellido1(apellido1);
        terceroFLEXIA.setApellido2(apellido2);
        terceroFLEXIA.setIdentificador("0");
        PersonaEntidadStub.Contacto[] contactos = terceroEgim.getContacto();

    
        m_Log.debug(" ===========> DATOS DE CONTACTO DE LA PERSONA " + terceroEgim.getPersonaEntidad().getNombre() + ", " + terceroEgim.getPersonaEntidad().getApellido1() + "  " +
               terceroEgim.getPersonaEntidad().getApellido2()  +" <=================");

        Contacto contacto = null;
        if(contactos!=null && contactos.length>1){
            // Se recupera entre los contacto, el principal que será el que tenga el menor orden            
            int menor = contactos[0].getOrden();
            int pos = 0;
            for(int i=1;i<contactos.length;i++){
                 if(menor>contactos[i].getOrden()){
                    menor = contactos[i].getOrden();
                    pos = i;
                 }//if
            }// for
            contacto = contactos[pos];
        }//if
        else
        if(contactos!=null && contactos.length==1) contacto = contactos[0];

        String datoContacto  ="";
        if(contacto!=null && contacto.getDatoContacto()!=null && contacto.getDatoContacto().length()>=1)
            datoContacto = contacto.getDatoContacto();

        if(contacto!=null && contacto!=null && contacto.getTipoContacto().equals(this.DESCRIPCION_EMAIL_EGIM))
            terceroFLEXIA.setEmail(datoContacto);
        else
        if(contacto!=null && (contacto.getTipoContacto().equals(this.DESCRIPCION_FAX_EGIM) || contacto.getTipoContacto().equals(this.DESCRIPCION_FIJO_EGIM) ||contacto.getTipoContacto().equals(this.DESCRIPCION_MOVIL_EGIM)))
            terceroFLEXIA.setTelefono(datoContacto);
               
        String tipoDocumentoFlexia = bundle.getString(ConstantesDatos.BUSQUEDA_TERCERO + ConstantesDatos.BARRA + codOrganizacion + ConstantesDatos.BARRA + ConstantesDatos.PLUGIN_EGIM + ConstantesDatos.BARRA
                + terceroEgim.getPersonaEntidad().getTipoPersona() + ConstantesDatos.BARRA + ConstantesDatos.MAPEO_DOCUMENTO_FLEXIA);
        terceroFLEXIA.setTipoDocumento(tipoDocumentoFlexia);

        if(tipoDocumentoFlexia.equals("4") || tipoDocumentoFlexia.equals("5"))
        {
            // El tipo de documento se trata de un CIF o de un CIF.ENT.PUBLICA
            if(terceroEgim.getPersonaEntidad().getRazonSocial()!=null && terceroEgim.getPersonaEntidad().getRazonSocial().length()>=1)
                terceroFLEXIA.setNombre(terceroEgim.getPersonaEntidad().getRazonSocial());
            else
                terceroFLEXIA.setNombre("");            
        }else{ // Otro tipo de documento
            if(terceroEgim.getPersonaEntidad().getNombre()!=null && terceroEgim.getPersonaEntidad().getNombre().length()>=1)
                terceroFLEXIA.setNombre(terceroEgim.getPersonaEntidad().getNombre());
            else
                terceroFLEXIA.setNombre("");
        }

        terceroFLEXIA.setDocumento(terceroEgim.getPersonaEntidad().getNumeroDocumento());                
        terceroFLEXIA.setOrigen(nombreOrigen);
        terceroFLEXIA.setSituacion('A');

        /** SE MAPEA EL DOMICILIO DEL TERCERO OBTENIDO A TRAVÉS DEL WS DE PERSONA DE EGIM **/
        Domicilio domicilio =  terceroEgim.getDomicilio();
        Vector domicilios = new Vector();
        domicilios.add(transformarDomicilioSW(domicilio));
        
        terceroFLEXIA.setDomicilios(domicilios);
        return terceroFLEXIA;
    }


   /**
     * Convierte un domicilio de un tercero de EGIM en un DomicilioSimpleValueObject
     * @param domicilioSW: Domicilio
     * @return DomicilioSimpleValueObject
     */
    private DomicilioSimpleValueObject transformarDomicilioSW(Domicilio domicilioSW) {
          DomicilioSimpleValueObject domicilioFLEXIA = new DomicilioSimpleValueObject();

         domicilioFLEXIA.setPais(DESC_PAIS_ESPANHA);
         domicilioFLEXIA.setIdPais(COD_PAIS_ESPANHA);
         domicilioFLEXIA.setIdPaisVia(COD_PAIS_ESPANHA);
         domicilioFLEXIA.setBloque(domicilioSW.getBloque());
         domicilioFLEXIA.setNumDesde(Short.toString(domicilioSW.getNumeroInf()));
         domicilioFLEXIA.setNumHasta(Short.toString(domicilioSW.getNumeroSup()));
         domicilioFLEXIA.setLetraDesde(domicilioSW.getLetraInf());
         domicilioFLEXIA.setLetraHasta(domicilioSW.getLetraSup());
         domicilioFLEXIA.setPortal(domicilioSW.getPortal());
         domicilioFLEXIA.setEscalera(domicilioSW.getEscalera());
         domicilioFLEXIA.setPlanta(domicilioSW.getPlanta());
         domicilioFLEXIA.setPuerta(domicilioSW.getPuerta());
         domicilioFLEXIA.setCodigoPostal(domicilioSW.getCodigoPostal());
         domicilioFLEXIA.setDomicilio(domicilioSW.getDomicilio());
         domicilioFLEXIA.setNormalizado("2");
         domicilioFLEXIA.setIdDomicilio("0");
       
        try {

            m_Log.debug(" ==============> PROVINCIA DOMICILIO EGIM :  " + domicilioSW.getProvincia());
            m_Log.debug(" ==============> MUNICIPIO DOMICILIO EGIM :  " + domicilioSW.getMunicipio());

            m_Log.debug("SE TRATA DE UN DOMICILIO EN ESPAÑA. BUSCAMOS LA INFORMACION DE PROVINCIA Y MUNICIPIO");
            GeneralValueObject infoProvincia = fachadaSGE.getProvinciaByPaisAndCodigo(Integer.parseInt(COD_PAIS_ESPANHA),
                    Integer.parseInt(domicilioSW.getProvincia()), paramsConexionBD);

            String codProvincia = (String) infoProvincia.getAtributo("codigoProvincia");
            m_Log.debug(" ==============> MUNICIPIO DOMICILIO FLEXIA :  " + codProvincia);

            domicilioFLEXIA.setIdProvincia(codProvincia);
            domicilioFLEXIA.setIdProvinciaVia(codProvincia);
            String descProvincia = (String) infoProvincia.getAtributo("nombreProvincia");            
            domicilioFLEXIA.setProvincia(descProvincia);
            m_Log.debug(" ==============> MUNICIPIO DOMICILIO FLEXIA :  " + descProvincia);

            MunicipioVO infoMunicipio = fachadaSGE.getMunicipioByPaisAndProvAndCodigo(Integer.parseInt(COD_PAIS_ESPANHA),
                    Integer.parseInt(codProvincia), Integer.parseInt(domicilioSW.getMunicipio()), paramsConexionBD);

            m_Log.debug(" ========> ");
            domicilioFLEXIA.setIdMunicipio(Integer.toString(infoMunicipio.getCodigoMunicipio()));
            domicilioFLEXIA.setIdMunicipioVia(Integer.toString(infoMunicipio.getCodigoMunicipio()));
            domicilioFLEXIA.setMunicipio(infoMunicipio.getNombreOficial());

           String codigoVia = domicilioSW.getCodigoVia();
           m_Log.debug(" ====> codigoVia : " + codigoVia);

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("NO SE HA PODIDO RECUPERAR ALGUN CODIGO DE PAIS, PROVINCIA O MUNICIPIO. PONEMOS LOS VALORES POR DEFECTO");
            domicilioFLEXIA.setIdProvincia("99");
            domicilioFLEXIA.setProvincia("DESCONOCIDA");
            domicilioFLEXIA.setIdMunicipio("999");
            domicilioFLEXIA.setMunicipio("DESCONOCIDO");
            domicilioFLEXIA.setIdProvinciaVia("99");
            domicilioFLEXIA.setIdMunicipioVia("999");
        }

        try {

            String nombreVia = domicilioSW.getNombreVia();
            domicilioFLEXIA.setDescVia(nombreVia);

            ResourceBundle bundle = ResourceBundle.getBundle("Terceros");
            String propiedad = ConstantesDatos.BUSQUEDA_TERCERO + ConstantesDatos.BARRA + domicilioSW.getCodigoVia() + ConstantesDatos.BARRA +
                    ConstantesDatos.MAPEO_VIA_FLEXIA;
            /** Se obtiene la abreviatura del tipo de vía de Flexia a partir de la de eGim. **/
            String abrevViaFlexia =  bundle.getString(propiedad);
            
            GeneralValueObject infoTipoVia = fachadaSGE.getTipoViaByAbreviatura(abrevViaFlexia, paramsConexionBD);
            
            String codTipoVia = (String)infoTipoVia.getAtributo("codTipoVia");            
            String abrVia       =  (String)infoTipoVia.getAtributo("abrTipoVia");
            domicilioFLEXIA.setCodigoVia(codTipoVia);
            domicilioFLEXIA.setIdTipoVia(abrVia);
            domicilioFLEXIA.setIdTipoVia(codTipoVia);            
            
        }catch (Exception e) {
            domicilioFLEXIA.setCodigoVia("0");
            domicilioFLEXIA.setIdTipoVia("0");
            domicilioFLEXIA.setTipoVia("SIN TIPO VIA");
        }

        domicilioFLEXIA.setOrigen("EGIM");
        return domicilioFLEXIA;
    }

}