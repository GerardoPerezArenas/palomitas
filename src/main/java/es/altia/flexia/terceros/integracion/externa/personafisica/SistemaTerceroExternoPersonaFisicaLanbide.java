package es.altia.flexia.terceros.integracion.externa.personafisica;

import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.manual.TercerosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.agora.webservice.tercero.servicios.lanbide.cliente.LangaiEECC;
import es.altia.agora.webservice.tercero.servicios.lanbide.cliente.LangaiEECCServiceLocator;
import es.altia.flexia.terceros.integracion.externa.excepciones.CamposObligatoriosTerceroExternoException;
import es.altia.flexia.terceros.integracion.externa.excepciones.EjecucionTerceroExternoException;
import es.altia.flexia.terceros.integracion.externa.excepciones.RestriccionTerceroExternoException;
import es.altia.flexia.terceros.integracion.externa.excepciones.SalidaAltaPersonaFisicaException;
import es.altia.flexia.terceros.integracion.externa.servicio.AltaTerceroExterno;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;

public class SistemaTerceroExternoPersonaFisicaLanbide extends AltaTerceroExterno{
    private Logger log = Logger.getLogger(SistemaTerceroExternoPersonaFisicaLanbide.class);

    // Nombre del fichero de configuración del plugin de terceros de Lanbide
    private String FICHERO_TERCEROS_LANBIDE         = "pluginTercerosLanbide";
    private String FICHERO_CONFIG_TERCEROS          = "Terceros";
    private String FICHERO_CONFIG_COMMON            = "common";
    private String PREFIJO_ALTA_TERCERO_EXTERNO     = "AltaTerceroExterno/";
    private String SUFIJO_URL_ALTA_TERCERO_EXTERNO  = "/PersonaFisicaLanbide/urlEndPoint";
    private String TIPO_DOCUMENTO_SGE               = "TipoDocumento/SGE/";
    private String LISTA_CAMPOS_OBLIGATORIOS        = "/LISTA_CAMPOS_OBLIGATORIOS/";
    private String PAISES_FLEXIA_TO_LANBIDE         = "Paises/FLEXIA_TO_LANBIDE/";
    private String TIPO_DOCUMENTO_W                 = "W";
    private String TIPO_DOCUMENTO_U                 = "U";
    private final String PROPIEDAD_CON_JNDI         = "CON.jndi";
    private final String PROPIEDAD_CON_GESTOR       = "CON.gestor";
    private final String FICHERO_CONFIGURACION      = "techserver";

    /**
     * Da de alta un tercero a través del servicio web de Persona Física de Lanbide
     * @param terVO: Datos del tercero
     * @param estructura: Estructura de los campo suplementarios del tercero
     * @param valores: Valores que toman los campos suplementarios del tercero
     * @param via: Datos de la vía correspondiente al domicilio
     * @return True si se ha podido dar de alta y false en caso contrario
     * @throws es.altia.flexia.terceros.integracion.externa.excepciones.EjecucionTerceroExternoException si ocurre algún error
     *         durante la ejecución del servicio
     * @throws es.altia.flexia.terceros.integracion.externa.excepciones.RestriccionTerceroExternoException si no se cumple alguna de las
     *         restricciones de entrada
     * @throws es.altia.flexia.terceros.integracion.externa.excepciones.CamposObligatoriosTerceroExternoException si hay algun campo
     *         que es obligatorio y que no ha sido cubierto
     */
    public boolean setTercero(TercerosValueObject terVO,Vector<EstructuraCampo> estructura,Vector valores,GeneralValueObject via,Connection con) throws EjecucionTerceroExternoException, RestriccionTerceroExternoException,CamposObligatoriosTerceroExternoException,SalidaAltaPersonaFisicaException{
        boolean exito = false;
        
        log.debug("SistemaTerceroExternoPersonaFisicaLanbide setTercero =================> ");

        String strUrlEndPoint = "";
        String codigoCentro   = "";
        String ubicacion      = "";
        String CAMPO_FECHA_NACIMIENTO = "";
        String CAMPO_SEXO = "";
        String CAMPO_NACIONALIDAD = "";
        String listaCamposObligatorios = "";
        ResourceBundle configTerceros = null;
        ResourceBundle configPluginLanbide = null;
        ResourceBundle configCommon = null;
        String codOrganizacion = null;
        String COD_VIA_DEFECTO_LANBIDE = null;
        String ALTA_TER_SIN_DOMICILIO = null;

        try{            
            configTerceros      = ResourceBundle.getBundle(FICHERO_CONFIG_TERCEROS);
            configPluginLanbide = ResourceBundle.getBundle(FICHERO_TERCEROS_LANBIDE);
            configCommon        = ResourceBundle.getBundle(FICHERO_CONFIG_COMMON);

            codigoCentro = configPluginLanbide.getString("codigoCentro");
            ubicacion    = configPluginLanbide.getString("ubicacion");
            codOrganizacion = terVO.getCodOrganizacion();

            log.debug("codigoCentro: " + codigoCentro);
            log.debug("ubicacion: " + ubicacion);

            String pStrUrlEndPoint = PREFIJO_ALTA_TERCERO_EXTERNO + terVO.getCodOrganizacion() + SUFIJO_URL_ALTA_TERCERO_EXTERNO;
            strUrlEndPoint = configTerceros.getString(pStrUrlEndPoint);

            CAMPO_FECHA_NACIMIENTO = configPluginLanbide.getString("CAMPO_FECHA_NACIMIENTO");
            CAMPO_SEXO             = configPluginLanbide.getString("CAMPO_SEXO");
            CAMPO_NACIONALIDAD     = configPluginLanbide.getString("CAMPO_NACIONALIDAD");
            COD_VIA_DEFECTO_LANBIDE = configPluginLanbide.getString("CodigoViaDefectoLanbide");
            ALTA_TER_SIN_DOMICILIO = configCommon.getString(terVO.getCodOrganizacion() + "/alta_tercero_sin_domicilio");

            listaCamposObligatorios= configPluginLanbide.getString(codOrganizacion + LISTA_CAMPOS_OBLIGATORIOS + terVO.getCodIdioma());

            //0/LISTA_CAMPOS_OBLIGATORIOS/4
            log.debug("nombre campo fecha nacimiento: " + CAMPO_FECHA_NACIMIENTO);
            log.debug("nombre campo sexo            : " + CAMPO_SEXO);
            log.debug("nombre campo nacionalidad    : " + CAMPO_NACIONALIDAD);


        }catch(Exception e){
            log.debug("Error al recuperar parámetros de alguno de los ficheros de configuración del plugin SistemaTerceroExternoPersonaFisicaLanbide: " + e.getMessage());
            throw new EjecucionTerceroExternoException("Error al recuperar parámetros de alguno de los ficheros de configuración del plugin SistemaTerceroExternoPersonaFisicaLanbide: " + e.getMessage());
        }

        // Se comprueba si hay algún tipo de limitación para poder dar de alta el tercero como puede ser
        // que el tipo de documento sea de persona física
        ArrayList<String> restricciones_alta = this.getRestriccionesTipoDocumentoAlta();
        if(restricciones_alta!=null && restricciones_alta.size()>0){

            int contador=0;
            for(int i=0;i<restricciones_alta.size();i++){
                if(terVO.getTipoDocumento().equalsIgnoreCase(restricciones_alta.get(i))){
                    contador++;
                    break;
                }
            }

            log.debug("********************** contadorRestricciones: " + contador);

            if(contador==0){
                ArrayList<String> etiquetasError = new ArrayList<String>();
                etiquetasError.add("msgAltaTerNotTipoDoc");
                // EL TERCERO TIENE UN TIPO DE DOCUMENTO QUE NO ESTÁ ENTRE LOS VÁLIDOS PARA REALIZAR EL ALTA DEL TERCERO
                throw new RestriccionTerceroExternoException("Error en las restricciones de entrada durante la ejecución de la operación de alta de tercero externo",etiquetasError);
            }else{
                // SE PROCEDE A COMPROBAR SI SE HAN CUBIERTO TODOS LOS CAMPOS NECESARIOS PARA DAR DE ALTA EL TERCERO

                try{
                    URL urlEndPoint = new URL(strUrlEndPoint);
                    LangaiEECCServiceLocator terceroLocator = new LangaiEECCServiceLocator();
                    LangaiEECC lanbideService = terceroLocator.getLangaiEECC(urlEndPoint);
                   
                    String tipoDocLanbide = convertTipoDocFlexiaToLanbide(configPluginLanbide,terVO.getTipoDocumento());
                    String numeroDocumento = terVO.getDocumento();
                    // Si el tipo de documento es una U o una W => El documento que se envía a Lanbide va vacío
                    /*if(tipoDocLanbide.equals(TIPO_DOCUMENTO_W))
                        numeroDocumento = "1"; */

                    String nombre = terVO.getNombre();
                    String apellido1 = terVO.getApellido1();
                    String apellido2 = terVO.getApellido2();                    
                    String fechaNacimiento = "";
                    String nacionalidad = "";
                    String sexo = "";                    
                    String telefono = terVO.getTelefono();

                    Vector domicilios = terVO.getDomicilios();
                    DomicilioSimpleValueObject domicilio = (DomicilioSimpleValueObject) domicilios.get(0);

                    //String codVia = domicilio.getCodigoVia();
                    String codVia = (String)via.getAtributo("codTipoVia");

                    String codPaisResidencia = "";
                    String codProvincia = "";
                    String codMunicipio = "";
                    String codPostal    = "";
                    String escalera     = "";
                    String piso         = "";
                    String letra        = "";
                    String nombreVia = "";
                    String duplicadoNombreVia = "";
                    String numero = "";
                    String email = "";
                    if(terVO.getEmail()!=null && !"".equals(terVO.getEmail()))
                        email = terVO.getEmail();

                    String codViaLanbide = "";

                    String emplazamiento = domicilio.getDomicilio();

                    // original
                    //if(codVia!=null && !"0".equals(codVia) && !"".equals(codVia))
                    
                    if(codVia!=null && !"0".equals(codVia) && !"".equals(codVia) && !"1".equals(codVia) && "NO".equalsIgnoreCase(ALTA_TER_SIN_DOMICILIO)
                            && !"99".equals(domicilio.getIdProvincia()) && !"999".equals(domicilio.getMunicipio()))
                    {
                        // Si la vía no es domicilio desconocido                        
                        try{
                            codViaLanbide = configPluginLanbide.getString("CodigoVia/Flexia/" + codVia);

                        }catch(Exception e){
                            log.error("No hay un tipo de vía en Lanbide para " + codVia + ". Por tanto se asocia el tipo calle");
                            codViaLanbide = COD_VIA_DEFECTO_LANBIDE;
                        }

                        // El código de provincia para pasarla a persona física tiene que tener una longitud de 2 caracteres
                        codProvincia = convertirCodProvincia(domicilio.getIdProvincia());
                        // El código de municipiop en persona física está compuesto por <codProvincia>-<codMunicipio> y tiene que
                        // tener una longitud de 5 caracteres. Se pone un 0 en medio de ambos datos si juntos no tiene dicha longitud
                        codMunicipio = convertirCodMunicipio(codProvincia,domicilio.getIdMunicipio());
                        codPostal = domicilio.getCodigoPostal();
                        codPaisResidencia = configPluginLanbide.getString(PAISES_FLEXIA_TO_LANBIDE + "108");
                        if(domicilio.getEscalera()!=null && !"".equals(domicilio.getEscalera()))
                            escalera = domicilio.getEscalera();

                        if(domicilio.getPlanta()!=null && !"".equals(domicilio.getPlanta()))
                            piso = domicilio.getPlanta();
                        
                       
                        // El nombre del domicilio se saca consultando por la vía
                        nombreVia = (String)via.getAtributo("descVia");                        
                        log.debug("******* nombreVia: " + nombreVia);                        
                        String numDesde = domicilio.getNumDesde();
                        String numHasta = domicilio.getNumHasta();

                        if(numDesde!=null && !"".equals(numDesde))
                            numero = numDesde;
                        else
                        if(numHasta!=null && !"".equals(numHasta))
                            numero = numHasta;

                        if(domicilio.getLetraDesde()!=null && !"".equals(domicilio.getLetraDesde()))
                            letra  = domicilio.getLetraDesde();
                        else
                        if(domicilio.getLetraHasta()!=null && !"".equals(domicilio.getLetraHasta()))
                            letra  = domicilio.getLetraHasta();
                    }
                    else
                    if(emplazamiento!=null && domicilio.getIdProvincia()!=null && !"99".equals(domicilio.getIdProvincia())
                            && !"0".equals(domicilio.getMunicipio()) && !"999".equals(domicilio.getMunicipio()) && "NO".equalsIgnoreCase(ALTA_TER_SIN_DOMICILIO)){

                        // El código de provincia para pasarla a persona física tiene que tener una longitud de 2 caracteres
                        codProvincia = convertirCodProvincia(domicilio.getIdProvincia());
                        // El código de municipiop en persona física está compuesto por <codProvincia>-<codMunicipio> y tiene que
                        // tener una longitud de 5 caracteres. Se pone un 0 en medio de ambos datos si juntos no tiene dicha longitud
                        codMunicipio = convertirCodMunicipio(codProvincia,domicilio.getIdMunicipio());
                        codPostal = domicilio.getCodigoPostal();
                        codPaisResidencia = configPluginLanbide.getString(PAISES_FLEXIA_TO_LANBIDE + "108");
                        if(domicilio.getEscalera()!=null && !"".equals(domicilio.getEscalera()))
                            escalera = domicilio.getEscalera();

                        if(domicilio.getPlanta()!=null && !"".equals(domicilio.getPlanta()))
                            piso = domicilio.getPlanta();


                        // El nombre del domicilio se saca consultando por la vía
                        nombreVia = emplazamiento;
                        log.debug("******* nombreVia: " + nombreVia);
                        String numDesde = domicilio.getNumDesde();
                        String numHasta = domicilio.getNumHasta();

                        if(numDesde!=null && !"".equals(numDesde))
                            numero = numDesde;
                        else
                        if(numHasta!=null && !"".equals(numHasta))
                            numero = numHasta;

                        if(domicilio.getLetraDesde()!=null && !"".equals(domicilio.getLetraDesde()))
                            letra  = domicilio.getLetraDesde();
                        else
                        if(domicilio.getLetraHasta()!=null && !"".equals(domicilio.getLetraHasta()))
                            letra  = domicilio.getLetraHasta();

                    }// if

                    
                    
                    log.debug("****** tipoDocumento: " + terVO.getTipoDocumento());
                    log.debug("****** tipoDocLanbide: " + tipoDocLanbide);
                    log.debug("****** numeroDocumento: " + numeroDocumento);
                    log.debug("****** codigoVia: " + codVia);
                    log.debug("****** codProvincia: " + codProvincia);
                    log.debug("****** codMunicipio: " + codMunicipio);
                                  
                    // Se recupera la fecha de nacimiento del campo suplementario de tipo fecha                    
                    fechaNacimiento = this.getValor(estructura,valores,CAMPO_FECHA_NACIMIENTO);
                    nacionalidad    = this.getValor(estructura,valores,CAMPO_NACIONALIDAD);
                    sexo            = this.getValor(estructura,valores,CAMPO_SEXO);

                    log.debug("Valor de la fecha de nacimiento: " + fechaNacimiento + ", nacionalidad: " + nacionalidad + ",sexo: " + sexo);

                    if(telefono==null || "".equals(telefono) || sexo==null || "".equals(sexo) || nacionalidad==null || "".equals(nacionalidad)
                            || fechaNacimiento==null || "".equals(fechaNacimiento) || nombre==null || "".equals(nombre) || apellido1==null
                            || "".equals(apellido1) || tipoDocLanbide==null || "".equals(tipoDocLanbide)){

                        throw new CamposObligatoriosTerceroExternoException("Se cancela la operación porque hay campos obligatorios por cubrir",listaCamposObligatorios);
                    }else{

                        // LA LLAMADA A ESTE SERVICIO DEVOLVERÁ EL GEN_PER_COD PARA ACTUALIZAR EL TERCERO EN BASE DE DATOS CON DICHO VALOR                        
                        //String salida = lanbideService.altaSimplePF(codigoCentro, ubicacion, tipoDocLanbide, numeroDocumento, nombre, apellido1,apellido2,"", fechaNacimiento,sexo, "", nacionalidad, telefono,"","","","","",email, codPaisResidencia, "", "", "", "", codProvincia,codMunicipio, codPostal, codViaLanbide,nombreVia,numero,duplicadoNombreVia, escalera,piso, letra, "", "", "","", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "0","","","","","","","","");
                        String salida = lanbideService.altaSimplePF(codigoCentro, ubicacion, tipoDocLanbide, numeroDocumento, nombre, apellido1, apellido2,"", fechaNacimiento, sexo,"", nacionalidad, telefono,"","","","", email, codPaisResidencia, "", "", "", "", codProvincia, codMunicipio, codPostal, codViaLanbide, nombreVia, numero, duplicadoNombreVia, escalera, piso, letra,"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "0", "", "", "", "", "", "");
                        log.debug("salida de la operacion altaSimplePF: " + salida);
                        String[] datosSalida = salida.split(ConstantesDatos.COMMA_SIMPLE);
                        log.debug("num datos salida = " + datosSalida.length);
                        if(datosSalida!=null){
                            log.debug("error posición 0: " + datosSalida[0]);

                            if(datosSalida[0].equals("0")){
                                // El siguiente dato es el GEN_PER_COD que hay que recuperar para actualizar la info del tercero en T_TER y en T_HTE
                                
                                String externalCode = null;
                                String documento = null;
                                log.debug("longitud de los datos de salida : " + datosSalida.length);

                                if(tipoDocLanbide!=null && (TIPO_DOCUMENTO_W.equalsIgnoreCase(tipoDocLanbide) || TIPO_DOCUMENTO_U.equalsIgnoreCase(tipoDocLanbide))){
                                    if(datosSalida.length==3){
                                        // Si la operación es correcta se devuelve el GEN_PER_CODD (Código externo de tercero).
                                        // junto con el documento para este tipo de tercero. Habrá que actualizar esta información del tercero
                                        externalCode = datosSalida[1];
                                        documento    = datosSalida[2];
                                    }
                                }else{
                                    
                                    externalCode = datosSalida[1];
                                    documento = null;
                                    
                                }
                                
                                exito = TercerosDAO.getInstance().actualizarCodigoExternoTercero(terVO.getIdentificador(), externalCode, documento,con);
                                if(exito && (TIPO_DOCUMENTO_W.equalsIgnoreCase(tipoDocLanbide) || TIPO_DOCUMENTO_U.equalsIgnoreCase(tipoDocLanbide))){
                                    terVO.setDocumento(documento);
                                }
                                
                            }else{
                                // HAY FALLOS => SE TRATAN
                                
                                ArrayList<String> codigosErrorLanbide = new ArrayList<String>();                                
                                for(int i=0;datosSalida!=null && i<datosSalida.length;i++){
                                    String codError = datosSalida[i];
                                    log.debug("Código de error: " + codError);
                                    codigosErrorLanbide.add(codError);
                                    
                                }// for

                                throw new SalidaAltaPersonaFisicaException("Se han producido errores en la llamada a la operacion altaSimplePF del servicio de persona física Lanbide", this.getErroresPersonalizadosLanbide(codigosErrorLanbide,terVO.getCodIdioma()));

                            }// for
                        }
                        log.debug("Salida del servicio web: " + salida);
                    }


                }
                catch(MalformedURLException e){
                    log.error("URL del servicio web con errores: " + e.getMessage());
                    throw new EjecucionTerceroExternoException("URL del servicio web con errores: " + e.getMessage());
                }catch(ServiceException e){
                    log.error("Error durante la llamada a la operación altaSimplePF del servicio de persona física de Lanbide: " + e.getMessage());
                    throw new EjecucionTerceroExternoException("Error durante la llamada a la operación altaSimplePF del servicio de persona física de Lanbide: " + e.getMessage());
                }catch(java.rmi.RemoteException e){
                    log.error("Error durante la llamada a la operación altaSimplePF del servicio de persona física de Lanbide: " + e.getMessage());
                    throw new EjecucionTerceroExternoException("Error durante la llamada a la operación altaSimplePF del servicio de persona física de Lanbide: " + e.getMessage());
                }                                
            }

        }// if
        
        log.debug("SistemaTerceroExternoPersonaFisicaLanbide setTercero <================= ");
        return exito;        
    }


    /**
     * Recupera los codigos de error del fichero de configuración del plugin
     * @param codigosError: ArrayList<String>
     */
    private ArrayList<String> getErroresPersonalizadosLanbide(ArrayList<String> codigosError,String codIdioma){
        ArrayList<String> salida = new ArrayList<String>();
        for(int i=0;i<codigosError.size();i++){
            String codigo = codigosError.get(i);

            ResourceBundle config = ResourceBundle.getBundle("pluginTercerosLanbide");
            String mensaje = null;
            try{
                mensaje = config.getString("ErrorPersonalizadoAltaSimplePF/" + codigo + "/" + codIdioma);

            }catch(Exception e){
                mensaje = config.getString("ErrorMensajeGenericoLanbide1/" + codIdioma) + " " + codigo +  " " + config.getString("ErrorMensajeGenericoLanbide2/" + codIdioma);
            }
            salida.add(mensaje);
        }// for
        return salida;
    }


    /**
     * El código de provincia en persona física tiene que tener dos caracteres
     * @param codProvincia: Código de provincia
     * @return Un String 
     */
    private String convertirCodProvincia(String codProvincia){
        String codigo = "";
        if(codProvincia!=null && codProvincia.length()==2){
            codigo = codProvincia;
        }else
        if(codProvincia!=null && codProvincia.length()==1){
            codigo = "0" + codProvincia;
        }
        return codigo;
    }


    /**
     * Concatena el código de provincia seguido del de municipio. Tiene que tener 5 caracteres, si
     * falta algún caracter se añade un cero entre el código de la provincia y el código de municipio.
     * @param codProvincia: Código de provincia 
     * @param codMunicipio: Código de municipio
     * @return Nuevo código
     */
    public String convertirCodMunicipio(String codProvincia,String codMunicipio){
        String salida = null;

        int len = codProvincia.length() + codMunicipio.length();
        if(len==5){
            salida = codProvincia + codMunicipio;
        }else
        if(len<5){
            StringBuffer sb = new StringBuffer();
            sb.append(codProvincia);
            int fin = 5-len;
            for(int i=0;i<fin;i++){
                sb.append("0");
            }
            sb.append(codMunicipio);
            salida = sb.toString();
        }
        return salida;
    }


    /**
     * Recupera el valor dado a un determinado campo suplementario de tercero
     * @param estructura :Estructura de los campos suplementarios de tercero
     * @param valores: Valores de los campos suplementarios dados para un determinado tercero
     * @param codBusqueda: String con el código del campo para el que se busca el valor
     * @return String
     */
    private String getValor(Vector<EstructuraCampo> estructura,Vector valores,String codCampo){

        String salida = null;
        for(int i=0;estructura!=null && i<estructura.size();i++) {
            log.debug("I ... "+i);            
            EstructuraCampo eC = new EstructuraCampo();
            eC = (EstructuraCampo) estructura.elementAt(i);
            log.debug("CODIGO ... "+eC.getCodCampo());
            log.debug("NOMBRE ... "+eC.getDescCampo());
            log.debug("TIPO DATO ... "+eC.getCodTipoDato());

            GeneralValueObject valor = (GeneralValueObject) valores.elementAt(i);

            if(codCampo.equals(eC.getCodCampo())){
                salida = (String)valor.getAtributo(eC.getCodCampo());
                break;
            }
            
        }// for
        log.debug("getValor(). El campo " + codCampo + " tiene el valor " + salida);
        return salida;
    }

    private String convertTipoDocFlexiaToLanbide(ResourceBundle configPluginLanbide,String tipoDocumento) throws EjecucionTerceroExternoException{
        String salida = null;

        if(configPluginLanbide!=null && tipoDocumento!=null && !"".equals(tipoDocumento)){

            try{
                salida = configPluginLanbide.getString(TIPO_DOCUMENTO_SGE + tipoDocumento);

            }catch(Exception e){
                log.error("Error al recuperar el tipo de documento de " + e.getMessage());
                salida = null;
                throw new EjecucionTerceroExternoException("Error al recuperar para el tipo de documento " + tipoDocumento + " el tipo de Persona física equivalente: ");
            }
        }
        return salida;
    }

    public boolean modificarTercero(TercerosValueObject terVO,Vector<EstructuraCampo> estructura,Vector valores,GeneralValueObject via,Connection con) throws EjecucionTerceroExternoException, RestriccionTerceroExternoException,CamposObligatoriosTerceroExternoException,SalidaAltaPersonaFisicaException{
        return false;
    }

}